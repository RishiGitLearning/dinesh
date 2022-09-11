/*
 * clsMaterialRequisition.java
 *
 * Created on April 19, 2004, 2:35 PM
 */

package EITLERP.Finance;

import java.util.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;


public class clsFASMovement {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    public static int ModuleID=198;
    private HashMap props;
    public HashMap colLineItems = new HashMap();
    public boolean Ready = false;
    private boolean HistoryView=false;
    public HashMap colgFasItems=new HashMap();
    public Variant getAttribute(String PropName) {
        return (Variant) props.get(PropName);
    }
    
    public void setAttribute(String PropName,Object Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,int Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,long Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,double Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,float Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,boolean Value) {
        props.put(PropName,new Variant(Value));
    }
    
    
    /** Creates new clsMaterialRequisition */
    public clsFASMovement() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE",  new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("REJECTED_REMARKS", new Variant(""));
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("CANCELLED", new Variant(false));
        
    }
    
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FAS_MOVEMENT_HEADER ORDER BY DOC_NO ");
            Ready=true;
            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public void Close() {
        try {
            Stmt.close();
            rsResultSet.close();
        }
        catch(Exception e) {
            
        }
    }
    
    
    //Navigation Methods
    public boolean MoveFirst() {
        try {
            rsResultSet.first();
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
        
    }
    
    public boolean MoveNext() {
        try {
            if(rsResultSet.isAfterLast()||rsResultSet.isLast()) {
                //Move pointer at last record
                //If it is beyond eof
                rsResultSet.last();
            }
            else {
                rsResultSet.next();
                if(rsResultSet.isAfterLast()) {
                    rsResultSet.last();
                }
            }
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    
    public boolean MovePrevious() {
        try {
            if(rsResultSet.isFirst()||rsResultSet.isBeforeFirst()) {
                rsResultSet.first();
            }
            else {
                rsResultSet.previous();
                if(rsResultSet.isBeforeFirst()) {
                    rsResultSet.first();
                }
            }
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public boolean MoveLast() {
        try {
            rsResultSet.last();
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
   public boolean Insert() {
        Statement stTmp,stHistory,stHDetail;
        ResultSet rsTmp,rsHistory,rsHDetail;
        Statement stHeader;
        ResultSet rsHeader;
        int CompanyID=0,AssetSrNo=0,cDept_id = 0;
        String AssetNo = "",cDept_name = "";
        String strSQL = "";
        String DocNo="";
        try {
            if(!Validate()) {
                return false;
            }
            ApprovalFlow ObjFlow=new ApprovalFlow();
            // Inserting records in Header
            long gCompanyID = (long) getAttribute("COMPANY_ID").getVal();
            setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            
            String AStatus = getAttribute("APPROVAL_STATUS").getString();
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FAS_MOVEMENT_HEADER_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FAS_MOVEMENT_DETAIL_H WHERE DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
           AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
            if(AStatus.equals("F")) {
                //-------- First Update the stock -------------//
                for(int i=1;i<=colLineItems.size();i++) {
                    clsFASMovementDetail ObjItem=(clsFASMovementDetail)colLineItems.get(Integer.toString(i));
                    
                    cDept_id=(int)ObjItem.getAttribute("MOVE_TO_DEPT_ID").getVal();
                    AssetSrNo=(int)ObjItem.getAttribute("DETAIL_SR_NO").getVal();
                    AssetNo=(String)ObjItem.getAttribute("ASSET_NO").getObj();
                    
                    
                    // Update Dept id 
                    data.Execute("UPDATE D_FAS_MASTER_DETAIL SET DEPT_ID="+cDept_id+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ASSET_NO='"+AssetNo.trim()+"' AND SR_NO="+AssetSrNo+" ",FinanceGlobal.FinURL);
                    //data.Execute("UPDATE D_INV_MIR_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND MIR_NO='"+MIRNo.trim()+"'");
                }  
                }
            setAttribute("DOC_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,clsFASMovement.ModuleID,getAttribute("FFNO").getInt(),true));
            
            //========= Inserting record into Header =========//
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID", getAttribute("COMPANY_ID").getInt());
            CompanyID = getAttribute("COMPANY_ID").getInt();
            rsResultSet.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            String theDocNo = getAttribute("DOC_NO").getString();
            rsResultSet.updateString("DOC_DATE", (String)getAttribute("DOC_DATE").getObj());
            rsResultSet.updateString("REMARKS", getAttribute("REMARKS").getString());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateString("REJECTED_REMARKS","");
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateString("CREATED_BY",  String.valueOf(getAttribute("CREATED_BY").getInt()));
            rsResultSet.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
//            rsResultSet.updateString("MODIFIED_BY", String.valueOf(getAttribute("MODIFIED_BY").getInt()));
//            rsResultSet.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.insertRow();
            //=========End of Inserting record into Header =========//
            
            //========= Inserting record into Header History=========//
            rsHistory.moveToInsertRow();
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("UPDATED_BY",String.valueOf(getAttribute("CREATED_BY").getInt()));
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            rsHistory.updateString("DOC_DATE", (String)getAttribute("DOC_DATE").getObj());
            rsHistory.updateString("REMARKS", getAttribute("REMARKS").getString());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateString("REJECTED_REMARKS","");
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateString("CREATED_BY",  String.valueOf(getAttribute("CREATED_BY").getInt()));
            rsHistory.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
//            rsHistory.updateString("MODIFIED_BY", String.valueOf(getAttribute("MODIFIED_BY").getInt()));
//            rsHistory.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            //=========End of Inserting record into Header history=========//
            
            //========== Inserting records Detail =========//
            Statement tmpStmt;
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FAS_MOVEMENT_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='1'");
            DocNo=getAttribute("DOC_NO").getString();
            
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            
            for(int i=1;i<=colLineItems.size();i++) {
                clsFASMovementDetail ObjItem=(clsFASMovementDetail) colLineItems.get(Integer.toString(i));
                
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",nCompanyID);
                rsTmp.updateString("DOC_NO",DocNo);
                rsTmp.updateLong("SR_NO",i);
                rsTmp.updateString("ASSET_NO",ObjItem.getAttribute("ASSET_NO").getString());
                rsTmp.updateInt("DETAIL_SR_NO",ObjItem.getAttribute("DETAIL_SR_NO").getInt());
                rsTmp.updateString("ITEM_ID",ObjItem.getAttribute("ITEM_ID").getString());
                rsTmp.updateInt("BUYER_DEPT_ID",ObjItem.getAttribute("BUYER_DEPT_ID").getInt());
                rsTmp.updateInt("CURRENT_DEPT_ID",ObjItem.getAttribute("CURRENT_DEPT_ID").getInt());
                rsTmp.updateInt("MOVE_TO_DEPT_ID",ObjItem.getAttribute("MOVE_TO_DEPT_ID").getInt());
                rsTmp.updateString("REMARKS",ObjItem.getAttribute("REMARKS").getString());
                rsTmp.updateString("CREATED_BY",  String.valueOf(getAttribute("CREATED_BY").getInt()));
                rsTmp.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
//                rsTmp.updateString("MODIFIED_BY", String.valueOf(getAttribute("MODIFIED_BY").getInt()));
//                rsTmp.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateBoolean("CANCELLED",false);
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
                rsHDetail.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("UPDATED_BY",String.valueOf(getAttribute("CREATED_BY").getInt()));
                rsHDetail.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
                rsHDetail.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
                rsHDetail.updateLong("COMPANY_ID",nCompanyID);
                rsHDetail.updateString("DOC_NO",DocNo);
                rsHDetail.updateLong("SR_NO",i);
                rsHDetail.updateString("ASSET_NO",ObjItem.getAttribute("ASSET_NO").getString());
                rsHDetail.updateInt("DETAIL_SR_NO",ObjItem.getAttribute("DETAIL_SR_NO").getInt());
                rsHDetail.updateString("ITEM_ID",ObjItem.getAttribute("ITEM_ID").getString());
                rsHDetail.updateInt("BUYER_DEPT_ID",ObjItem.getAttribute("BUYER_DEPT_ID").getInt());
                rsHDetail.updateInt("CURRENT_DEPT_ID",ObjItem.getAttribute("CURRENT_DEPT_ID").getInt());
                rsHDetail.updateInt("MOVE_TO_DEPT_ID",ObjItem.getAttribute("MOVE_TO_DEPT_ID").getInt());
                rsHDetail.updateString("REMARKS",ObjItem.getAttribute("REMARKS").getString());
                rsHDetail.updateString("CREATED_BY",  String.valueOf(getAttribute("CREATED_BY").getInt()));
                rsHDetail.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
//                rsHDetail.updateString("MODIFIED_BY", String.valueOf(getAttribute("MODIFIED_BY").getInt()));
//                rsHDetail.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());

                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CANCELLED",false);
                rsHDetail.insertRow();
            }
            
            //insert current dept id into fas master detail after final approved
            
            
            //========== Inserting records Detail =========//
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=clsFASMovement.ModuleID;
            ObjFlow.DocNo=getAttribute("DOC_NO").getString();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_FAS_MOVEMENT_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="DOC_NO";
            ObjFlow.UseSpecifiedURL=true;
            ObjFlow.SpecificURL=FinanceGlobal.FinURL;
            
             if(AStatus.equals("R")) {
                ObjFlow.To=getAttribute("SEND_DOC_TO").getInt();
                ObjFlow.ExplicitSendTo=true;
            }
            //==========================================//
            
            
            //==== Handling Rejected Documents ==========//
//            boolean IsRejected=getAttribute("REJECTED").getBool();
//            
//            if(IsRejected) {
//                //Remove the Rejected Flag First
//                data.Execute("UPDATE D_FIN_SALE_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+" AND SALE_NO='"+ObjFlow.DocNo+"'",FinanceGlobal.FinURL);
//                //Remove Old Records from D_COM_DOC_DATA
//                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+ObjFlow.DocNo+"'");
//                
//                ObjFlow.IsCreator=true;
//            }
            //==========================================//
            
            
            if(ObjFlow.Status.equals("H")) {
                //Do nothing
                //if(IsRejected) {
                    ObjFlow.Status="A";
                    ObjFlow.To=ObjFlow.From;
                    ObjFlow.UpdateFlow();
                    
               // }
            }
            else {
                if(!ObjFlow.UpdateFlow()) {
                    LastError=ObjFlow.LastError;
                }
            }
            ObjFlow.UseSpecifiedURL=false;
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    } 
    
    
    
    
    
    //Updates current record
    public boolean Update() {
        Statement stTmp,stHistory,stHDetail;
        ResultSet rsTmp,rsHistory,rsHDetail;
        Statement stHeader;
        ResultSet rsHeader;
        int CompanyID=0,AssetSrNo=0,cDept_id = 0;
        String AssetNo = "",cDept_name = "";
        String strSQL = "";
        
        boolean Validate=true;
        try {
            
            if(!Validate()) {
                return false;
            }
            //No Primary Keys will be updated & Updating of Header starts
            ApprovalFlow ObjFlow=new ApprovalFlow();
            // Inserting records in Header
            long gCompanyID= (long) getAttribute("COMPANY_ID").getVal();
            setAttribute("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
            if(AStatus.equals("F")) {
                //-------- First Update the stock -------------//
                for(int i=1;i<=colLineItems.size();i++) {
                    clsFASMovementDetail ObjItem=(clsFASMovementDetail)colLineItems.get(Integer.toString(i));
                    
                    cDept_id=(int)ObjItem.getAttribute("MOVE_TO_DEPT_ID").getVal();
                    cDept_name=(String)ObjItem.getAttribute("MOVE_TO_DEPT_NAME").getObj();
                    AssetSrNo=(int)ObjItem.getAttribute("DETAIL_SR_NO").getVal();
                    AssetNo=(String)ObjItem.getAttribute("ASSET_NO").getObj();
                    
                    
                    // Update GRN Received Qty
                    data.Execute("UPDATE D_FAS_MASTER_DETAIL SET DEPT_ID="+cDept_id+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ASSET_NO='"+AssetNo.trim()+"' AND SR_NO="+AssetSrNo+" ",FinanceGlobal.FinURL);
                    //data.Execute("UPDATE D_INV_MIR_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND MIR_NO='"+MIRNo.trim()+"'");
                }  
                }
            
            //String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
            //String AStatus=getAttribute("APPROVAL_STATUS").getString();
            if(AStatus.equals("R")) {
                Validate=false; //Do not validate while in rejection mode.
            }
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FAS_MOVEMENT_HEADER_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FAS_MOVEMENT_DETAIL_H WHERE DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            String theDocNo=(String)getAttribute("DOC_NO").getObj();
            
            rsResultSet.updateLong("COMPANY_ID", getAttribute("COMPANY_ID").getInt());
            CompanyID = getAttribute("COMPANY_ID").getInt();
            rsResultSet.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            String DocNo = getAttribute("DOC_NO").getString();
            rsResultSet.updateString("DOC_DATE", (String)getAttribute("DOC_DATE").getObj());
            rsResultSet.updateString("REMARKS", getAttribute("REMARKS").getString());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateString("REJECTED_REMARKS","");
//            rsResultSet.updateString("CREATED_BY",  String.valueOf(getAttribute("CREATED_BY").getInt()));
//            rsResultSet.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateString("MODIFIED_BY", String.valueOf(getAttribute("MODIFIED_BY").getInt()));
            rsResultSet.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateRow();
            //rsResultSet.refreshRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM FINANCE.D_FAS_MOVEMENT_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+(String)getAttribute("DOC_NO").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("DOC_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateString("UPDATED_BY",String.valueOf(getAttribute("MODIFIED_BY").getInt()));
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            rsHistory.updateString("DOC_DATE", (String)getAttribute("DOC_DATE").getObj());
            rsHistory.updateString("REMARKS", getAttribute("REMARKS").getString());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateString("REJECTED_REMARKS","");
//            rsHistory.updateString("CREATED_BY", String.valueOf(getAttribute("CREATED_BY").getInt()));
//            rsHistory.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
            rsHistory.updateString("MODIFIED_BY", String.valueOf(getAttribute("MODIFIED_BY").getInt()));
            rsHistory.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            // Inserting records in Inquiry Details
            Statement tmpStmt;
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM FINANCE.D_FAS_MOVEMENT_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DOC_NO='1'");
            String nDocno=(String) getAttribute("DOC_NO").getObj();
            
            String Del_Query = "DELETE FROM FINANCE.D_FAS_MOVEMENT_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DOC_NO='"+nDocno.trim()+"'";
            data.Execute(Del_Query,FinanceGlobal.FinURL);
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            
            for(int i=1;i<=colLineItems.size();i++) {
                clsSalesInterestItem ObjItem=(clsSalesInterestItem) colLineItems.get(Integer.toString(i));
                
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",nCompanyID);
                rsTmp.updateString("DOC_NO",nDocno);
                rsTmp.updateLong("SR_NO",i);
                rsTmp.updateString("ASSET_NO",ObjItem.getAttribute("ASSET_NO").getString());
                rsTmp.updateInt("DETAIL_SR_NO",ObjItem.getAttribute("DETAIL_SR_NO").getInt());
                rsTmp.updateString("ITEM_ID",ObjItem.getAttribute("ITEM_ID").getString());
                rsTmp.updateInt("BUYER_DEPT_ID",ObjItem.getAttribute("BUYER_DEPT_ID").getInt());
                rsTmp.updateInt("CURRENT_DEPT_ID",ObjItem.getAttribute("CURRENT_DEPT_ID").getInt());
                rsTmp.updateInt("MOVE_TO_DEPT_ID",ObjItem.getAttribute("MOVE_TO_DEPT_ID").getInt());
                rsTmp.updateString("REMARKS",ObjItem.getAttribute("REMARKS").getString());
//                rsTmp.updateString("CREATED_BY",  String.valueOf(getAttribute("CREATED_BY").getInt()));
//                rsTmp.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                rsTmp.updateString("MODIFIED_BY", String.valueOf(getAttribute("MODIFIED_BY").getInt()));
                rsTmp.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateBoolean("CANCELLED",false);
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateLong("COMPANY_ID",nCompanyID);
                rsHDetail.updateString("DOC_NO",nDocno);
                rsHDetail.updateLong("SR_NO",i);
                rsHDetail.updateString("ASSET_NO",ObjItem.getAttribute("ASSET_NO").getString());
                rsHDetail.updateInt("DETAIL_SR_NO",ObjItem.getAttribute("DETAIL_SR_NO").getInt());
                rsHDetail.updateString("ITEM_ID",ObjItem.getAttribute("ITEM_ID").getString());
                rsHDetail.updateInt("BUYER_DEPT_ID",ObjItem.getAttribute("BUYER_DEPT_ID").getInt());
                rsHDetail.updateInt("CURRENT_DEPT_ID",ObjItem.getAttribute("CURRENT_DEPT_ID").getInt());
                rsHDetail.updateInt("MOVE_TO_DEPT_ID",ObjItem.getAttribute("MOVE_TO_DEPT_ID").getInt());
                rsHDetail.updateString("REMARKS",ObjItem.getAttribute("REMARKS").getString());
//                rsHDetail.updateString("CREATED_BY",  String.valueOf(getAttribute("CREATED_BY").getInt()));
//                rsHDetail.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateString("MODIFIED_BY", String.valueOf(getAttribute("MODIFIED_BY").getInt()));
                rsHDetail.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CANCELLED",false);
                rsHDetail.insertRow();
            }
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=clsFASSale.ModuleID;
            ObjFlow.DocNo=(String)getAttribute("DOC_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_FAS_MOVEMENT_HEADER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="DOC_NO";
            ObjFlow.UseSpecifiedURL=true;
            ObjFlow.SpecificURL=FinanceGlobal.FinURL;
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_FAS_MOVEMENT_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+ObjFlow.DocNo+"'",FinanceGlobal.FinURL);
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM FINANCE.D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+clsSalesInterest.ModuleID+" AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                ObjFlow.IsCreator=true;
            }
            //==========================================//
            
            if(ObjFlow.Status.equals("H")) {
                if(IsRejected) {
                    ObjFlow.Status="A";
                    ObjFlow.To=ObjFlow.From;
                    ObjFlow.UpdateFlow();
                }
            } else {
                if(!ObjFlow.UpdateFlow()) {
                    LastError=ObjFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------//
            
            ObjFlow.UseSpecifiedURL=false;
            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    
    
    
    
    
    
    public boolean Filter(String pCondition,int pCompanyID) {
        Ready=false;
        try {
            
           
            String strSql = "SELECT * FROM D_FAS_MOVEMENT_HEADERA,D_FAS_MOVEMENT_DETAIL B " + pCondition ;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
             strSql = "SELECT * FROM D_FAS_MOVEMENT_HEADERA,D_FAS_MOVEMENT_DETAIL B WHERE A.DOC_NO = B.DOC_NO AND A.COMPANY_ID="+pCompanyID+" AND A.DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND A.DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY A.DOC_NO";
                rsResultSet=Stmt.executeQuery(strSql);
                Ready=true;
                MoveLast();
                return false;
            }
            else {
                Ready=true;
                MoveLast();
                return true;
            }
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    
    private boolean setData() {
        ResultSet rsTmp;
        Statement tmpStmt;
        Connection tmpConn;
        HashMap List=new HashMap();
        long Counter=0;
        int RevNo=0;
        tmpConn=data.getConn(FinanceGlobal.FinURL);
        
        try {
           
            
                      
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",rsResultSet.getInt("REVISION_NO"));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            //Populate the user
            setAttribute("COMPANY_ID",UtilFunctions.getInt(rsResultSet,"COMPANY_ID",0));
            setAttribute("DOC_NO",UtilFunctions.getString(rsResultSet,"DOC_NO",""));
            setAttribute("DOC_DATE",UtilFunctions.getString(rsResultSet,"DOC_DATE","0000-00-00"));
            setAttribute("REMARKS",UtilFunctions.getString(rsResultSet,"REMARKS",""));
            setAttribute("APPROVED",UtilFunctions.getInt(rsResultSet,"APPROVED",0));
            setAttribute("APPROVED_DATE",UtilFunctions.getString(rsResultSet,"APPROVED_DATE","0000-00-00"));
            setAttribute("REJECTED",UtilFunctions.getInt(rsResultSet,"REJECTED",0));
            setAttribute("REJECTED_DATE",UtilFunctions.getString(rsResultSet,"REJECTED_DATE","0000-00-00"));
            setAttribute("REJECTED_REMARKS",UtilFunctions.getString(rsResultSet,"REJECTED_REMARKS",""));
            setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            setAttribute("CREATED_DATE",UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00"));
            setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            setAttribute("MODIFIED_DATE",UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00"));
            setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsResultSet,"HIERARCHY_ID",0));
            setAttribute("CHANGED",UtilFunctions.getInt(rsResultSet,"CHANGED",0));
            setAttribute("CHANGED_DATE",UtilFunctions.getString(rsResultSet,"CHANGED_DATE","0000-00-00"));
            setAttribute("CANCELLED",UtilFunctions.getInt(rsResultSet,"CANCELLED",0));
            
            //Now Populate the collection
            //first clear the collection
            colLineItems.clear();
            
            long mCompanyID= getAttribute("COMPANY_ID").getLong();
            String mDocno= getAttribute("DOC_NO").getString();
            tmpStmt=tmpConn.createStatement();
            String SQL= "";
            if(HistoryView) {
                SQL = "SELECT * FROM D_FAS_MOVEMENT_DETAIL_H WHERE COMPANY_ID="+mCompanyID+" AND DOC_NO='"+mDocno.trim()+"' AND REVISION_NO="+RevNo+" ORDER BY DOC_NO ";
                rsTmp=tmpStmt.executeQuery(SQL);
            } else {
                SQL = "SELECT * FROM D_FAS_MOVEMENT_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND DOC_NO='"+mDocno.trim()+"' ORDER BY DOC_NO";
                rsTmp=tmpStmt.executeQuery(SQL);
            }
            rsTmp.first();
            
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsFASMovementDetail ObjItem=new clsFASMovementDetail();
                
                ObjItem.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));
                ObjItem.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                ObjItem.setAttribute("SR_NO",rsTmp.getLong("SR_NO"));
                ObjItem.setAttribute("ASSET_NO",rsTmp.getString("ASSET_NO"));
                ObjItem.setAttribute("DETAIL_SR_NO",rsTmp.getLong("DETAIL_SR_NO"));
                ObjItem.setAttribute("ITEM_ID",rsTmp.getString("ITEM_ID"));
                //ObjItem.setAttribute("ITEM_DESCRIPTION",rsTmp.getString("ITEM_DESCRIPTION"));
                ObjItem.setAttribute("BUYER_DEPT_ID",rsTmp.getLong("BUYER_DEPT_ID"));
               // ObjItem.setAttribute("BUYER_DEPT_NAME",rsTmp.getString("BUYER_DEPT_NAME"));
                ObjItem.setAttribute("CURRENT_DEPT_ID",rsTmp.getLong("CURRENT_DEPT_ID"));
                //ObjItem.setAttribute("CURRENT_DEPT_NAME",rsTmp.getString("CURRENT_DEPT_ID"));
                ObjItem.setAttribute("MOVE_TO_DEPT_ID",rsTmp.getLong("MOVE_TO_DEPT_ID"));
                //ObjItem.setAttribute("MOVE_TO_DEPT_NAME",rsTmp.getString("MOVE_TO_DEPT_NAME"));
                ObjItem.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                ObjItem.setAttribute("CREATED_BY",rsTmp.getString("CREATED_BY"));
                ObjItem.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjItem.setAttribute("MODIFIED_BY",rsTmp.getString("MODIFIED_BY"));
                ObjItem.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                ObjItem.setAttribute("CHANGED",rsTmp.getInt("CHANGED"));
                ObjItem.setAttribute("CHANGED_DATE",rsTmp.getString("CHANGED_DATE"));
                ObjItem.setAttribute("CANCELLED",rsTmp.getInt("CANCELLED"));
                
                colLineItems.put(Long.toString(Counter),ObjItem);
                rsTmp.next();
            }
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public static HashMap getHistoryList(int pCompanyID,String pDocNo) {
        HashMap List=new HashMap();
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn(FinanceGlobal.FinURL);
            stTmp=tmpConn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_FAS_MOVEMENT_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+pDocNo+"'");
            
            while(rsTmp.next()) {
                clsFASMovement ObjFASMovement=new clsFASMovement();
                
                ObjFASMovement.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                ObjFASMovement.setAttribute("DOC_DATE",rsTmp.getString("DOC_DATE"));
                ObjFASMovement.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjFASMovement.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjFASMovement.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjFASMovement.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjFASMovement.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjFASMovement);
            }
            
            rsTmp.close();
            stTmp.close();
            //tmpConn.close();
            
            return List;
            
        }
        catch(Exception e) {
            e.printStackTrace();
            return List;
            
        }
    }
   
    
    private boolean Validate() {
        return true;
    }
    
    public boolean ShowHistory(int pCompanyID,String pDocNo) {
        Ready=false;
        try {
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FAS_MOVEMENT_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' ");
            Ready=true;
            HistoryView=true;
            MoveFirst();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
   public static HashMap getPendingApprovals(int CompanyID,int UserID,int Order,int FinYearFrom)  {
        ResultSet rsTemp=null;
        String strSQL="";
        Statement tmpStmt3=null;
        Connection tmpConn;
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            tmpConn=data.getConn();
            tmpStmt3=tmpConn.createStatement();
            
            if(Order==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT FINANCE.D_FAS_MOVEMENT_HEADER.DOC_NO,FINANCE.D_FAS_MOVEMENT_HEADER.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FAS_MOVEMENT_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FAS_MOVEMENT_HEADER.COMPANY_ID=DINESHMILLS.D_COM_DOC_DATA.COMPANY_ID AND DINESHMILLS.D_COM_DOC_DATA.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND FINANCE.D_FAS_MOVEMENT_HEADER.APPROVED=0 AND FINANCE.D_FAS_MOVEMENT_HEADER.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT FINANCE.D_FAS_MOVEMENT_HEADER.DOC_NO,FINANCE.D_FAS_MOVEMENT_HEADER.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FAS_MOVEMENT_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FAS_MOVEMENT_HEADER.COMPANY_ID=DINESHMILLS.D_COM_DOC_DATA.COMPANY_ID AND DINESHMILLS.D_COM_DOC_DATA.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND FINANCE.D_FAS_MOVEMENT_HEADER.APPROVED=0 AND FINANCE.D_FAS_MOVEMENT_HEADER.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY FINANCE.D_FAS_MOVEMENT_HEADER.DOC_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT FINANCE.D_FAS_MOVEMENT_HEADER.DOC_NO,FINANCE.D_FAS_MOVEMENT_HEADER.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FAS_MOVEMENT_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FAS_MOVEMENT_HEADER.COMPANY_ID=DINESHMILLS.D_COM_DOC_DATA.COMPANY_ID AND DINESHMILLS.D_COM_DOC_DATA.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND FINANCE.D_FAS_MOVEMENT_HEADER.APPROVED=0 AND FINANCE.D_FAS_MOVEMENT_HEADER.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY FINANCE.D_FAS_MOVEMENT_HEADER.DOC_NO";
            }
            
            //rsTemp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTemp=tmpStmt3.executeQuery(strSQL);
            
            rsTemp.first();
            
            Counter=0;
            
            //if(rsTemp.getRow()>0) {
            
            while(!rsTemp.isAfterLast()) {
                if(EITLERPGLOBAL.isWithinDate(rsTemp.getString("DOC_DATE"),FinYearFrom)) {
                    Counter=Counter+1;
                    clsFASSale ObjDoc=new clsFASSale();
                    
                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("DOC_NO",UtilFunctions.getString(rsTemp,"DOC_NO",""));
                    ObjDoc.setAttribute("DOC_DATE",UtilFunctions.getString(rsTemp,"DOC_DATE","0000-00-00"));
                    ObjDoc.setAttribute("RECEIVED_DATE",UtilFunctions.getString(rsTemp,"RECEIVED_DATE","0000-00-00"));
                    // ----------------- End of Header Fields ------------------------------------//
                    
                    List.put(Long.toString(Counter),ObjDoc);
                    
                    
                    
                    //rsTemp.next();
                }
                
                rsTemp.next();
                
            }
            //}
            rsTemp.close();
            
        }
        catch(Exception e) {
        }
        
        return List;
    } 
   
    public boolean IsEditable(int pCompanyID,String pDocNo,int pUserID) {
        
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            
             strSQL="SELECT DOC_NO FROM D_FAS_MOVEMENT_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND DOC_NO='"+pDocNo+"' AND (APPROVED=1)";
            if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                return false;
            }
            else {
                strSQL="SELECT *  FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
                
                if(data.IsRecordExist(strSQL)) {
                    //Yes document is waiting for this user
                    return true;
                }
                else {
                    //Document is not editable by this user
                    return false;
                }
            }
        }
        
        /*Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FAS_MOVEMENT_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND DOC_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
                tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getInt("COUNT")>0) {
                    //Yes document is waiting for this user
                    return true;
                }
                else {
                    //Document is not editable by this user
                    return false;
                }
            }
        }*/
        catch(Exception e) {
            return false;
        }
    }
}
