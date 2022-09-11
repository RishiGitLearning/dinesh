/*
 * clsMaterialRequisition.java
 *
 * Created on April 19, 2004, 2:35 PM
 */

package EITLERP.Stores;

import java.util.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import EITLERP.Finance.UtilFunctions;


/**
 *
 * @author  jadave
 * @version
 */

public class clsSTMRequisitionRaw {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    //Requisition Material Collection
    public HashMap colSTMReqRawItems;
    
    //History Related properties
    private boolean HistoryView=false;
    
    public static int ModuleID=180;
    
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
    public clsSTMRequisitionRaw() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("STM_REQ_NO", new Variant(""));
        props.put("STM_REQ_DATE",new Variant(""));
        props.put("STM_REQ_TYPE",new Variant(2));
        props.put("FOR_DEPT_ID",new Variant(0));
        props.put("TRANSFER_TO_UNIT",new Variant(0));
        props.put("PURPOSE", new Variant(""));
        props.put("REMARKS", new Variant(""));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("CANCELLED", new Variant(false));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("PREFIX",new Variant("")); //For Autonumber generation
        
        //Create a new object for MR Item collection
        colSTMReqRawItems=new HashMap();
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("FROM_REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
    }
    
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_INV_STM_REQ_HEADER WHERE COMPANY_ID="+pCompanyID+" AND STM_REQ_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND STM_REQ_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' AND STM_REQ_TYPE=2 ORDER BY STM_REQ_NO");
            HistoryView=false;
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
            //Conn.close();
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
        
        Statement stHistory,stHDetail,stHeader;
        ResultSet rsHistory,rsHDetail,rsHeader;
        String AStatus="";
        ResultSet rsTmp;
        Statement tmpStmt;
        try {
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date ReqDate=java.sql.Date.valueOf((String)getAttribute("STM_REQ_DATE").getObj());
            
            if((ReqDate.after(FinFromDate)||ReqDate.compareTo(FinFromDate)==0)&&(ReqDate.before(FinToDate)||ReqDate.compareTo(FinToDate)==0)) {
                //Within the year
            }
            else {
                LastError="Requisition date is not within financial year.";
                return false;
            }
            //=====================================================//
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_STM_REQ_HEADER_H WHERE STM_REQ_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_INV_STM_REQ_DETAIL_H WHERE STM_REQ_NO='1'");
            rsHDetail.first();
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_STM_REQ_DETAIL WHERE STM_REQ_NO='1'");
            //------------------------------------//
            
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_REQ_HEADER WHERE STM_REQ_NO='1'");
            //rsHeader.first();
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            AStatus = getAttribute("APPROVAL_STATUS").getString();
            if(AStatus.equals("F")) {
                if(!TransferItem(EITLERPGLOBAL.gCompanyID,colSTMReqRawItems)) {
                    return false;
                }
            }
            
            //--------- Generate Gatepass requisition no.  ------------
            setAttribute("STM_REQ_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,clsSTMRequisitionRaw.ModuleID, (int)getAttribute("FFNO").getVal(),true));
            //-------------------------------------------------
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getLong());
            rsResultSet.updateString("STM_REQ_NO",getAttribute("STM_REQ_NO").getString());
            rsResultSet.updateInt("STM_REQ_TYPE",getAttribute("STM_REQ_TYPE").getInt());
            rsResultSet.updateString("STM_REQ_DATE",getAttribute("STM_REQ_DATE").getString());
            rsResultSet.updateLong("FOR_DEPT_ID",getAttribute("FOR_DEPT_ID").getLong());
            rsResultSet.updateString("PURPOSE",getAttribute("PURPOSE").getString());
            rsResultSet.updateLong("TRANSFER_TO_UNIT",getAttribute("TRANSFER_TO_UNIT").getLong());
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateBoolean("CANCELLED",getAttribute("CANCELLED").getBool());
            rsResultSet.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
            rsResultSet.updateLong("CREATED_BY",getAttribute("CREATED_BY").getLong());
            rsResultSet.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
//            rsResultSet.updateLong("MODIFIED_BY",getAttribute("MODIFIED_BY").getLong());
//            rsResultSet.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.insertRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",getAttribute("CREATED_BY").getInt());
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            rsHistory.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getLong());
            rsHistory.updateString("STM_REQ_NO",getAttribute("STM_REQ_NO").getString());
            rsHistory.updateInt("STM_REQ_TYPE",getAttribute("STM_REQ_TYPE").getInt());
            rsHistory.updateString("STM_REQ_DATE",getAttribute("STM_REQ_DATE").getString());
            rsHistory.updateLong("FOR_DEPT_ID",getAttribute("FOR_DEPT_ID").getLong());
            rsHistory.updateString("PURPOSE",getAttribute("PURPOSE").getString());
            rsHistory.updateLong("TRANSFER_TO_UNIT",getAttribute("TRANSFER_TO_UNIT").getLong());
            rsHistory.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateBoolean("CANCELLED",getAttribute("CANCELLED").getBool());
            rsHistory.updateLong("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getLong());
            rsHistory.updateLong("CREATED_BY",getAttribute("CREATED_BY").getLong());
            rsHistory.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
//            rsHistory.updateLong("MODIFIED_BY",getAttribute("MODIFIED_BY").getLong());
//            rsHistory.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            long nCompanyID=getAttribute("COMPANY_ID").getLong();
            
            //Now Insert records into detail table
            for(int i=1;i<=colSTMReqRawItems.size();i++) {
                clsSTMReqItem ObjSTMReqItem=(clsSTMReqItem) colSTMReqRawItems.get(Integer.toString(i));
                
                ObjSTMReqItem.setAttribute("BAL_QTY",ObjSTMReqItem.getAttribute("STM_REQ_QTY").getDouble());
                
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",nCompanyID);
                rsTmp.updateString("STM_REQ_NO",getAttribute("STM_REQ_NO").getString());
                rsTmp.updateInt("STM_REQ_TYPE",getAttribute("STM_REQ_TYPE").getInt());
                rsTmp.updateLong("STM_REQ_SR_NO",i);
                rsTmp.updateString("ITEM_ID",ObjSTMReqItem.getAttribute("ITEM_ID").getString());
                rsTmp.updateString("ITEM_EXTRA_DESC",ObjSTMReqItem.getAttribute("ITEM_EXTRA_DESC").getString());
                rsTmp.updateDouble("STM_REQ_QTY",ObjSTMReqItem.getAttribute("STM_REQ_QTY").getDouble());
                rsTmp.updateDouble("BAL_QTY",ObjSTMReqItem.getAttribute("STM_REQ_QTY").getDouble());
                rsTmp.updateDouble("ISSUED_QTY",0);
                rsTmp.updateLong("UNIT",ObjSTMReqItem.getAttribute("UNIT").getLong());
                rsTmp.updateString("REMARKS",ObjSTMReqItem.getAttribute("REMARKS").getString());
                rsTmp.updateString("REQUIRED_DATE",ObjSTMReqItem.getAttribute("REQUIRED_DATE").getString());
                rsTmp.updateLong("CREATED_BY",getAttribute("CREATED_BY").getLong());
                rsTmp.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
//                rsTmp.updateLong("MODIFIED_BY",getAttribute("MODIFIED_BY").getLong());
//                rsTmp.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
                rsTmp.updateBoolean("CANCELLED",false);
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateLong("COMPANY_ID",nCompanyID);
                rsHDetail.updateString("STM_REQ_NO",getAttribute("STM_REQ_NO").getString());
                rsHDetail.updateInt("STM_REQ_TYPE",getAttribute("STM_REQ_TYPE").getInt());
                rsHDetail.updateInt("STM_REQ_SR_NO",i);
                rsHDetail.updateString("ITEM_ID",ObjSTMReqItem.getAttribute("ITEM_ID").getString());
                rsHDetail.updateString("ITEM_EXTRA_DESC",ObjSTMReqItem.getAttribute("ITEM_EXTRA_DESC").getString());
                rsHDetail.updateDouble("STM_REQ_QTY",ObjSTMReqItem.getAttribute("STM_REQ_QTY").getDouble());
                rsHDetail.updateDouble("BAL_QTY",ObjSTMReqItem.getAttribute("STM_REQ_QTY").getDouble()); //Balance Qty will be balance qty
                rsHDetail.updateDouble("ISSUED_QTY",0); //Balance Qty will be balance qty
                rsHDetail.updateLong("UNIT",ObjSTMReqItem.getAttribute("UNIT").getLong());
                rsHDetail.updateString("REMARKS",ObjSTMReqItem.getAttribute("REMARKS").getString());
                rsHDetail.updateString("REQUIRED_DATE",ObjSTMReqItem.getAttribute("REQUIRED_DATE").getString());
                rsHDetail.updateLong("CREATED_BY",getAttribute("CREATED_BY").getLong());
                rsHDetail.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
//                rsHDetail.updateLong("MODIFIED_BY",getAttribute("MODIFIED_BY").getLong());
//                rsHDetail.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
                rsHDetail.updateBoolean("CANCELLED",false);
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
            }
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=getAttribute("COMPANY_ID").getInt();
            ObjFlow.ModuleID=clsSTMRequisitionRaw.ModuleID;
            ObjFlow.DocNo=getAttribute("STM_REQ_NO").getString();
            ObjFlow.From=getAttribute("FROM").getInt();
            ObjFlow.To=getAttribute("TO").getInt();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_INV_STM_REQ_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=getAttribute("HIERARCHY_ID").getInt();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName="STM_REQ_NO";
            
            if(ObjFlow.Status.equals("H")) {
                ObjFlow.Status="A";
                ObjFlow.To=ObjFlow.From;
                ObjFlow.UpdateFlow();
            } else {
                if(!ObjFlow.UpdateFlow()) {
                    LastError=ObjFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError= e.getMessage();
            return false;
        }
    }
    
    
    //Updates current record
    public boolean Update() {
        Statement stHistory,stHDetail,stHeader;
        ResultSet rsHistory,rsHDetail,rsHeader;
        String AStatus="";
        ResultSet rsTmp;
        Statement tmpStmt;
        try {
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date ReqDate=java.sql.Date.valueOf(getAttribute("STM_REQ_DATE").getString());
            
            if((ReqDate.after(FinFromDate)||ReqDate.compareTo(FinFromDate)==0)&&(ReqDate.before(FinToDate)||ReqDate.compareTo(FinToDate)==0)) {
                //Withing the year
            }
            else {
                LastError="Requisition date is not within financial year.";
                return false;
            }
            //=====================================================//
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_STM_REQ_HEADER_H WHERE STM_REQ_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_INV_STM_REQ_DETAIL_H WHERE STM_REQ_NO='1'");
            rsHDetail.first();
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_STM_REQ_DETAIL WHERE STM_REQ_NO='1'");
            //------------------------------------//
            
            String theDocNo=getAttribute("STM_REQ_NO").getString();
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_REQ_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND TRIM(STM_REQ_NO)='"+theDocNo+"'");
            //rsHeader.first();
            AStatus = getAttribute("APPROVAL_STATUS").getString();
            if(AStatus.equals("F")) {
                if(!TransferItem(EITLERPGLOBAL.gCompanyID,colSTMReqRawItems)) {
                    return false;
                }
            }
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            rsResultSet.updateString("STM_REQ_DATE",getAttribute("STM_REQ_DATE").getString());
            rsResultSet.updateLong("FOR_DEPT_ID", getAttribute("FOR_DEPT_ID").getLong());
            rsResultSet.updateString("PURPOSE",getAttribute("PURPOSE").getString());
            rsResultSet.updateLong("TRANSFER_TO_UNIT",getAttribute("TRANSFER_TO_UNIT").getLong());
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
//            rsResultSet.updateLong("CREATED_BY",getAttribute("CREATED_BY").getLong());
//            rsResultSet.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsResultSet.updateLong("MODIFIED_BY",getAttribute("MODIFIED_BY").getLong());
            rsResultSet.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_INV_STM_REQ_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND STM_REQ_NO='"+(String)getAttribute("STM_REQ_NO").getObj()+"'");
            RevNo++;
            String RevDocNo=getAttribute("STM_REQ_NO").getString();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",getAttribute("MODIFIED_BY").getInt());
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            rsHistory.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getLong());
            rsHistory.updateString("STM_REQ_NO",getAttribute("STM_REQ_NO").getString());
            rsHistory.updateInt("STM_REQ_TYPE",getAttribute("STM_REQ_TYPE").getInt());
            rsHistory.updateString("STM_REQ_DATE",getAttribute("STM_REQ_DATE").getString());
            rsHistory.updateLong("FOR_DEPT_ID",getAttribute("FOR_DEPT_ID").getLong());
            rsHistory.updateString("PURPOSE",getAttribute("PURPOSE").getString());
            rsHistory.updateLong("TRANSFER_TO_UNIT",getAttribute("TRANSFER_TO_UNIT").getLong());
            rsHistory.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
//            rsHistory.updateLong("CREATED_BY",getAttribute("CREATED_BY").getLong());
//            rsHistory.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsHistory.updateLong("MODIFIED_BY",getAttribute("MODIFIED_BY").getLong());
            rsHistory.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();
            
            //First remove the old rows
            String mCompanyID=Long.toString(getAttribute("COMPANY_ID").getLong());
            String mReqNo=getAttribute("STM_REQ_NO").getString();
            
            data.Execute("DELETE FROM D_INV_STM_REQ_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND STM_REQ_NO='"+mReqNo+"' AND STM_REQ_TYPE=2");
            
            
            //Now Insert records into detail table
            for(int i=1;i<=colSTMReqRawItems.size();i++) {
                clsSTMReqItem ObjSTMReqItem=(clsSTMReqItem) colSTMReqRawItems.get(Integer.toString(i));
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getLong());
                rsTmp.updateString("STM_REQ_NO",getAttribute("STM_REQ_NO").getString());
                rsTmp.updateInt("STM_REQ_TYPE",getAttribute("STM_REQ_TYPE").getInt());
                rsTmp.updateLong("STM_REQ_SR_NO",i);
                rsTmp.updateString("ITEM_ID",ObjSTMReqItem.getAttribute("ITEM_ID").getString());
                rsTmp.updateString("ITEM_EXTRA_DESC",ObjSTMReqItem.getAttribute("ITEM_EXTRA_DESC").getString());
                rsTmp.updateDouble("STM_REQ_QTY",ObjSTMReqItem.getAttribute("STM_REQ_QTY").getDouble());
                rsTmp.updateDouble("BAL_QTY",ObjSTMReqItem.getAttribute("STM_REQ_QTY").getDouble());
                rsTmp.updateDouble("ISSUED_QTY",0);
                rsTmp.updateLong("UNIT",ObjSTMReqItem.getAttribute("UNIT").getLong());
                rsTmp.updateString("REMARKS",ObjSTMReqItem.getAttribute("REMARKS").getString());
                rsTmp.updateString("REQUIRED_DATE",ObjSTMReqItem.getAttribute("REQUIRED_DATE").getString());
//                rsTmp.updateLong("CREATED_BY",getAttribute("CREATED_BY").getLong());
//                rsTmp.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                rsTmp.updateLong("MODIFIED_BY",getAttribute("MODIFIED_BY").getLong());
                rsTmp.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
                rsTmp.updateBoolean("CANCELLED",false);
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getLong());
                rsHDetail.updateString("STM_REQ_NO",getAttribute("STM_REQ_NO").getString());
                rsHDetail.updateInt("STM_REQ_TYPE",getAttribute("STM_REQ_TYPE").getInt());
                rsHDetail.updateInt("STM_REQ_SR_NO",i);
                rsHDetail.updateString("ITEM_ID",ObjSTMReqItem.getAttribute("ITEM_ID").getString());
                rsHDetail.updateString("ITEM_EXTRA_DESC",ObjSTMReqItem.getAttribute("ITEM_EXTRA_DESC").getString());
                rsHDetail.updateDouble("STM_REQ_QTY",ObjSTMReqItem.getAttribute("STM_REQ_QTY").getDouble());
                rsHDetail.updateDouble("ISSUED_QTY",0); //Balance Qty will be balance qty
                rsHDetail.updateDouble("BAL_QTY",ObjSTMReqItem.getAttribute("STM_REQ_QTY").getDouble()); //Balance Qty will be balance qty
                rsHDetail.updateLong("UNIT",ObjSTMReqItem.getAttribute("UNIT").getLong());
                rsHDetail.updateString("REMARKS",ObjSTMReqItem.getAttribute("REMARKS").getString());
                rsHDetail.updateString("REQUIRED_DATE",ObjSTMReqItem.getAttribute("REQUIRED_DATE").getString());
//                rsHDetail.updateLong("CREATED_BY",getAttribute("CREATED_BY").getLong());
//                rsHDetail.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                rsHDetail.updateLong("MODIFIED_BY",getAttribute("MODIFIED_BY").getLong());
                rsHDetail.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
                rsHDetail.updateBoolean("CANCELLED",false);
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
                
            }
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=getAttribute("COMPANY_ID").getInt();
            ObjFlow.ModuleID=clsSTMRequisitionRaw.ModuleID;
            ObjFlow.DocNo=getAttribute("STM_REQ_NO").getString();
            ObjFlow.From=getAttribute("FROM").getInt();
            ObjFlow.To=getAttribute("TO").getInt();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_INV_STM_REQ_HEADER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=getAttribute("HIERARCHY_ID").getInt();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName="STM_REQ_NO";
            
            
            //==== Handling Rejected Documents ==========//
            
            if(AStatus.equals("R")) {
                //Remove the Rejected Flag First
                //data.Execute("UPDATE D_INV_INDENT_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                //data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=3 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                //ObjFlow.IsCreator=true;
                ObjFlow.To=getAttribute("SEND_DOC_TO").getInt();
                ObjFlow.ExplicitSendTo=true;
            }
            //==========================================//
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_INV_STM_REQ_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND STM_REQ_NO='"+ObjFlow.DocNo+"' AND STM_REQ_TYPE=2");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+clsSTMRequisitionRaw.ModuleID+" AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                ObjFlow.IsCreator=true;
            }
            //==========================================//
            
            
            if(ObjFlow.Status.equals("H")) {
                //Do nothing
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
            //--------- Approval Flow Update complete -----------
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    //This routine checks and returns whether the item is deletable or not
    //Criteria is Approved item cannot be deleted
    //and if not approved then user id is checked whether doucment
    //is created by the user. Only creator can delete the document
    public boolean CanDelete(int pCompanyID,String pDocNo,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView) {
                return false;
            }
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_STM_REQ_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND STM_REQ_NO='"+pDocNo+"' AND STM_REQ_TYPE=2 AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID="+clsSTMRequisitionRaw.ModuleID+" AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
        }
        catch(Exception e) {
            return false;
        }
    }
    
    
    //This routine checks and returns whether the item is editable or not
    //Criteria is Approved item cannot be changed
    //and if not approved then user id is checked whether doucment
    //is waiting for his approval.
    public boolean IsEditable(int pCompanyID,String pDocNo,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_STM_REQ_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND STM_REQ_NO='"+pDocNo+"' AND STM_REQ_TYPE=2 AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID="+clsSTMRequisitionRaw.ModuleID+" AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
        }
        catch(Exception e) {
            return false;
        }
    }
    
    
    //Deletes current record
    public boolean Delete(int pUserID) {
        try {
            int lCompanyID=(int)getAttribute("COMPANY_ID").getVal();
            String lDocNo=(String)getAttribute("STM_REQ_NO").getObj();
            String strSQL="";
            
            //First check that record is editable
            if(CanDelete(lCompanyID,lDocNo,pUserID)) {
                String strQry = "DELETE FROM D_INV_STM_REQ_HEADER WHERE COMPANY_ID=" + lCompanyID +" AND STM_REQ_NO='"+lDocNo+"' AND STM_REQ_TYPE=2";
                data.Execute(strQry);
                strQry = "DELETE FROM D_INV_STM_REQ_DETAIL WHERE COMPANY_ID=" + lCompanyID +" AND STM_REQ_NO='"+lDocNo+"' AND STM_REQ_TYPE=2";
                data.Execute(strQry);
                
                LoadData(lCompanyID);
                return true;
            }
            else {
                LastError="Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false;
            }
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public Object getObject(int pCompanyID, String pReqNo) {
        String strCondition = " WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND STM_REQ_NO='" + pReqNo + "'" ;
        clsSTMRequisitionRaw ObjSTMRequisition = new clsSTMRequisitionRaw();
        ObjSTMRequisition.Filter(strCondition,pCompanyID);
        return ObjSTMRequisition;
    }
    
    public static Object getObjectEx(int pCompanyID,String pReqNo) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND STM_REQ_NO='" + pReqNo+"' ";
        clsSTMRequisitionRaw ObjSTMRequisition = new clsSTMRequisitionRaw();
        ObjSTMRequisition.LoadData(pCompanyID);
        ObjSTMRequisition.Filter(strCondition,pCompanyID);
        return ObjSTMRequisition;
    }
    
    public static HashMap getList(String pCondition) {
        ResultSet rsTmp=null;
        Connection tmpConn=null;
        Statement tmpStmt=null;
        
        ResultSet rsTmp2=null;
        Statement tmpStmt2=null;
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement();
            
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_STM_REQ_HEADER "+pCondition);
            
            Counter=0;
            while(rsTmp.next()) {
                Counter=Counter+1;
                clsSTMRequisitionRaw ObjSTMRequisition =new clsSTMRequisitionRaw();
                ObjSTMRequisition.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjSTMRequisition.setAttribute("STM_REQ_NO",rsTmp.getString("STM_REQ_NO"));
                ObjSTMRequisition.setAttribute("STM_REQ_TYPE",rsTmp.getInt("STM_REQ_TYPE"));
                ObjSTMRequisition.setAttribute("STM_REQ_DATE",rsTmp.getString("STM_REQ_DATE"));
                ObjSTMRequisition.setAttribute("FOR_DEPT_ID",rsTmp.getInt("FOR_DEPT_ID"));
                ObjSTMRequisition.setAttribute("PURPOSE",rsTmp.getString("PURPOSE"));
                ObjSTMRequisition.setAttribute("TRANSFER_TO_UNIT",rsTmp.getInt("TRANSFER_TO_UNIT"));
                ObjSTMRequisition.setAttribute("APPROVED",rsTmp.getInt("APPROVED"));
                ObjSTMRequisition.setAttribute("APPROVED_DATE",rsTmp.getString("APPROVED_DATE"));
                ObjSTMRequisition.setAttribute("REJECTED_DATE",rsTmp.getString("REJECTED_DATE"));
                ObjSTMRequisition.setAttribute("REJECTED",rsTmp.getBoolean("REJECTED"));
                ObjSTMRequisition.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                ObjSTMRequisition.setAttribute("CANCELLED",rsTmp.getInt("CANCELLED"));
                ObjSTMRequisition.setAttribute("CREATED_BY",rsTmp.getInt("CREATED_BY"));
                ObjSTMRequisition.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjSTMRequisition.setAttribute("MODIFIED_BY",rsTmp.getInt("MODIFIED_BY"));
                ObjSTMRequisition.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                
                ObjSTMRequisition.colSTMReqRawItems.clear();
                String mCompanyID=Long.toString(ObjSTMRequisition.getAttribute("COMPANY_ID").getLong());
                String mReqNo=ObjSTMRequisition.getAttribute("STM_REQ_NO").getString();
                
                tmpStmt2=tmpConn.createStatement();
                rsTmp2=tmpStmt2.executeQuery("SELECT * FROM D_INV_STM_REQ_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND STM_REQ_NO='" + mReqNo + "' AND STM_REQ_TYPE=2");
                
                Counter=0;
                while(rsTmp2.next()) {
                    Counter=Counter+1;
                    clsSTMReqItem ObjSTMReqItem=new clsSTMReqItem();
                    
                    ObjSTMReqItem.setAttribute("COMPANY_ID",rsTmp2.getLong("COMPANY_ID"));
                    ObjSTMReqItem.setAttribute("STM_REQ_NO",rsTmp2.getString("STM_REQ_NO"));
                    ObjSTMReqItem.setAttribute("STM_REQ_TYPE",rsTmp2.getInt("STM_REQ_TYPE"));
                    ObjSTMReqItem.setAttribute("STM_REQ_SR_NO",rsTmp2.getLong("STM_REQ_SR_NO"));
                    ObjSTMReqItem.setAttribute("ITEM_ID",rsTmp2.getString("ITEM_ID"));
                    ObjSTMReqItem.setAttribute("STM_REQ_QTY",EITLERPGLOBAL.round(rsTmp2.getFloat("STM_REQ_QTY"),3));
                    ObjSTMReqItem.setAttribute("ISSUED_QTY",EITLERPGLOBAL.round(rsTmp2.getFloat("ISSUED_QTY"),3));
                    ObjSTMReqItem.setAttribute("BAL_QTY",EITLERPGLOBAL.round(rsTmp2.getFloat("BAL_QTY"),3));
                    ObjSTMReqItem.setAttribute("UNIT",rsTmp2.getLong("UNIT"));
                    ObjSTMReqItem.setAttribute("REMARKS",rsTmp2.getString("REMARKS"));
                    ObjSTMReqItem.setAttribute("REQUIRED_DATE",rsTmp2.getString("REQUIRED_DATE"));
                    ObjSTMReqItem.setAttribute("CREATED_BY",rsTmp2.getLong("CREATED_BY"));
                    ObjSTMReqItem.setAttribute("CREATED_DATE",rsTmp2.getString("CREATED_DATE"));
                    ObjSTMReqItem.setAttribute("MODIFIED_BY",rsTmp2.getLong("MODIFIED_BY"));
                    ObjSTMReqItem.setAttribute("MODIFIED_DATE",rsTmp2.getString("MODIFIED_DATE"));
                    
                    ObjSTMRequisition.colSTMReqRawItems.put(Long.toString(Counter),ObjSTMReqItem);
                }// Innser while
                
                List.put(Long.toString(Counter),ObjSTMRequisition);
            }//Outer while
            
            rsTmp.close();
            //tmpConn.close();
            tmpStmt.close();
            rsTmp2.close();
            tmpStmt2.close();
            
        }
        catch(Exception e) {
            //LastError=e.getMessage();
        }
        
        return List;
    }
    
    
    public boolean Filter(String pCondition,int pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_INV_STM_REQ_HEADER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_INV_STM_REQ_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND STM_REQ_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND STM_REQ_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' AND STM_REQ_TYPE=2 ORDER BY STM_REQ_NO";
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
    
    public boolean setData() {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getConn();
        
        //HashMap List=new HashMap();
        long Counter=0;
        int RevNo=0;
        
        try {
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",rsResultSet.getInt("REVISION_NO"));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("STM_REQ_NO",rsResultSet.getString("STM_REQ_NO"));
            setAttribute("STM_REQ_TYPE",rsResultSet.getInt("STM_REQ_TYPE"));
            setAttribute("STM_REQ_DATE",rsResultSet.getString("STM_REQ_DATE"));
            setAttribute("FOR_DEPT_ID",rsResultSet.getInt("FOR_DEPT_ID"));
            setAttribute("PURPOSE",rsResultSet.getString("PURPOSE"));
            setAttribute("TRANSFER_TO_UNIT",rsResultSet.getInt("TRANSFER_TO_UNIT"));
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsResultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE",rsResultSet.getString("REJECTED_DATE"));
            setAttribute("HIERARCHY_ID",rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("REMARKS",rsResultSet.getString("REMARKS"));
            setAttribute("CANCELLED",rsResultSet.getInt("CANCELLED"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            
            //Now Populate the collection
            //first clear the collection
            colSTMReqRawItems.clear();
            
            String mCompanyID=Long.toString(getAttribute("COMPANY_ID").getLong());
            String mReqNo=getAttribute("STM_REQ_NO").getString();
            
            tmpStmt=tmpConn.createStatement();
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_STM_REQ_DETAIL_H WHERE COMPANY_ID=" + mCompanyID + " AND STM_REQ_NO='" + mReqNo + "' AND STM_REQ_TYPE=2 AND REVISION_NO="+RevNo+" ORDER BY STM_REQ_SR_NO");
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_STM_REQ_DETAIL WHERE COMPANY_ID=" + mCompanyID + " AND STM_REQ_NO='" + mReqNo + "' AND STM_REQ_TYPE=2 ORDER BY STM_REQ_SR_NO");
            }
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsSTMReqItem ObjSTMReqItem = new clsSTMReqItem();
                
                ObjSTMReqItem.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));
                ObjSTMReqItem.setAttribute("STM_REQ_NO",rsTmp.getString("STM_REQ_NO"));
                ObjSTMReqItem.setAttribute("STM_REQ_TYPE",rsTmp.getInt("STM_REQ_TYPE"));
                ObjSTMReqItem.setAttribute("STM_REQ_SR_NO",rsTmp.getLong("STM_REQ_SR_NO"));
                ObjSTMReqItem.setAttribute("ITEM_ID",rsTmp.getString("ITEM_ID"));
                ObjSTMReqItem.setAttribute("ITEM_EXTRA_DESC",rsTmp.getString("ITEM_EXTRA_DESC"));
                ObjSTMReqItem.setAttribute("STM_REQ_QTY",rsTmp.getFloat("STM_REQ_QTY"));
                ObjSTMReqItem.setAttribute("ISSUED_QTY",rsTmp.getFloat("ISSUED_QTY"));
                ObjSTMReqItem.setAttribute("BAL_QTY",rsTmp.getFloat("BAL_QTY"));
                ObjSTMReqItem.setAttribute("UNIT",rsTmp.getLong("UNIT"));
                ObjSTMReqItem.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                ObjSTMReqItem.setAttribute("CANCELLED",rsTmp.getInt("CANCELLED"));
                ObjSTMReqItem.setAttribute("REQUIRED_DATE",rsTmp.getString("REQUIRED_DATE"));
                ObjSTMReqItem.setAttribute("CREATED_BY",rsTmp.getLong("CREATED_BY"));
                ObjSTMReqItem.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjSTMReqItem.setAttribute("MODIFIED_BY",rsTmp.getLong("MODIFIED_BY"));
                ObjSTMReqItem.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                
                colSTMReqRawItems.put(Long.toString(Counter),ObjSTMReqItem);
                rsTmp.next();
            }
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public static HashMap getPendingApprovals(int pCompanyID,int pUserID,int pOrder,int FinYearFrom) {
        Connection tmpConn;
        ResultSet rsTmp3=null;
        Statement tmpStmt3=null;
        String strSQL="";
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            tmpConn=data.getConn();
            tmpStmt3=tmpConn.createStatement();
            
            if(pOrder==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT D_INV_STM_REQ_HEADER.STM_REQ_NO,D_INV_STM_REQ_HEADER.STM_REQ_DATE,RECEIVED_DATE,D_INV_STM_REQ_HEADER.FOR_DEPT_ID AS DEPT_ID FROM D_INV_STM_REQ_HEADER,D_COM_DOC_DATA WHERE D_INV_STM_REQ_HEADER.STM_REQ_TYPE=2 AND D_INV_STM_REQ_HEADER.STM_REQ_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_STM_REQ_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_STM_REQ_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_INV_STM_REQ_HEADER.STM_REQ_TYPE=2 AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID="+clsSTMRequisitionRaw.ModuleID+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT D_INV_STM_REQ_HEADER.STM_REQ_NO,D_INV_STM_REQ_HEADER.STM_REQ_DATE,RECEIVED_DATE,D_INV_STM_REQ_HEADER.FOR_DEPT_ID AS DEPT_ID FROM D_INV_STM_REQ_HEADER,D_COM_DOC_DATA WHERE D_INV_STM_REQ_HEADER.STM_REQ_TYPE=2 AND D_INV_STM_REQ_HEADER.STM_REQ_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_STM_REQ_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_STM_REQ_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_INV_STM_REQ_HEADER.STM_REQ_TYPE=2 AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID="+clsSTMRequisitionRaw.ModuleID+" ORDER BY D_INV_STM_REQ_HEADER.STM_REQ_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT D_INV_STM_REQ_HEADER.STM_REQ_NO,D_INV_STM_REQ_HEADER.STM_REQ_DATE,RECEIVED_DATE,D_INV_STM_REQ_HEADER.FOR_DEPT_ID AS DEPT_ID FROM D_INV_STM_REQ_HEADER,D_COM_DOC_DATA WHERE D_INV_STM_REQ_HEADER.STM_REQ_TYPE=2 AND D_INV_STM_REQ_HEADER.STM_REQ_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_STM_REQ_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_STM_REQ_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_INV_STM_REQ_HEADER.STM_REQ_TYPE=2 AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID="+clsSTMRequisitionRaw.ModuleID+" ORDER BY D_INV_STM_REQ_HEADER.STM_REQ_NO";
            }
            
            //strSQL="SELECT D_INV_REQ_HEADER.STM_REQ_NO,D_INV_REQ_HEADER.STM_REQ_DATE FROM D_INV_REQ_HEADER,D_COM_DOC_DATA WHERE D_INV_REQ_HEADER.STM_REQ_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_REQ_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_REQ_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=2";
            rsTmp3=tmpStmt3.executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp3.next()) {
                if(EITLERPGLOBAL.isWithinDate(rsTmp3.getString("STM_REQ_DATE"),FinYearFrom)) {
                    Counter=Counter+1;
                    clsSTMRequisitionRaw ObjDoc=new clsSTMRequisitionRaw();
                    
                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("STM_REQ_NO",rsTmp3.getString("STM_REQ_NO"));
                    ObjDoc.setAttribute("STM_REQ_DATE",rsTmp3.getString("STM_REQ_DATE"));
                    ObjDoc.setAttribute("RECEIVED_DATE",rsTmp3.getString("RECEIVED_DATE"));
                    ObjDoc.setAttribute("DEPT_ID",rsTmp3.getInt("DEPT_ID"));
                    // ----------------- End of Header Fields ------------------------------------//
                    
                    List.put(Long.toString(Counter),ObjDoc);
                }
            }//end of while
            
            //tmpConn.close();
            rsTmp3.close();
            tmpStmt3.close();
            
        }
        catch(Exception e) {
        }
        
        return List;
    }
    
    public static HashMap getSTMReqItemList(int pCompanyID,String pSTMNo,boolean pAllData,int pType) {
        ResultSet rsTmp=null;
        Connection tmpConn=null;
        Statement tmpStmt=null;
        
        ResultSet rsTmp2=null;
        Statement tmpStmt2=null;
        int Counter1 = 0;
        
        HashMap List=new HashMap();
        long Counter=0;
        String strSQL;
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            if(pAllData) {
                strSQL = "SELECT * FROM D_INV_STM_REQ_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND STM_REQ_NO='"+pSTMNo.trim()+"' AND STM_REQ_TYPE=2 AND STM_REQ_NO IN (SELECT STM_REQ_NO FROM D_INV_STM_REQ_HEADER WHERE STM_REQ_NO='"+pSTMNo+"' AND APPROVED=1) ORDER BY ITEM_ID";
            }
            else{
                
                
                //strSQL = "SELECT * FROM D_INV_STM_REQ_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND STM_REQ_NO='"+pSTMNo.trim()+"' AND ISSUED_QTY<STM_REQ_QTY AND STM_REQ_NO IN (SELECT STM_REQ_NO FROM D_INV_STM_REQ_HEADER WHERE STM_REQ_NO='"+pSTMNo+"' AND APPROVED=1) ORDER BY ITEM_ID";
                
                strSQL="SELECT B.* ";
                strSQL+="FROM ";
                strSQL+="D_INV_STM_REQ_HEADER A, ";
                strSQL+="D_INV_STM_REQ_DETAIL B ";
                strSQL+="LEFT JOIN D_INV_STM_DETAIL I ON (I.STM_REQ_NO=B.STM_REQ_NO AND I.STM_REQ_SR_NO=B.STM_REQ_SR_NO AND I.STM_NO IN (SELECT STM_NO FROM D_INV_STM_HEADER WHERE STM_NO=I.STM_NO AND APPROVED=1 AND CANCELLED=0)), ";
                strSQL+="D_COM_DEPT_MASTER D ";
                strSQL+="WHERE A.COMPANY_ID=B.COMPANY_ID AND ";
                strSQL+="A.STM_REQ_NO=B.STM_REQ_NO AND ";
                strSQL+="A.STM_REQ_TYPE=B.STM_REQ_TYPE AND ";
                strSQL+="A.COMPANY_ID="+pCompanyID+" AND ";
                strSQL+="A.APPROVED=1 AND A.CANCELLED=0 AND ";
                strSQL+="B.COMPANY_ID=D.COMPANY_ID AND A.FOR_DEPT_ID=D.DEPT_ID AND ";
                strSQL+="A.STM_REQ_TYPE="+pType+" AND A.STM_REQ_NO='"+pSTMNo+"' ";
                strSQL+="GROUP BY B.STM_REQ_NO,B.STM_REQ_TYPE,B.STM_REQ_SR_NO ";
                strSQL+="HAVING IF(SUM(I.QTY) IS NULL,0,SUM(I.QTY))<B.STM_REQ_QTY ";
                
            }
            
            rsTmp2=tmpStmt.executeQuery(strSQL);
            rsTmp2.first();
            
            Counter1=0;
            while(! rsTmp2.isAfterLast()) {
                Counter1++;
                clsSTMReqItem ObjSTMReqItem=new clsSTMReqItem();
                
                ObjSTMReqItem.setAttribute("COMPANY_ID",rsTmp2.getInt("COMPANY_ID"));
                ObjSTMReqItem.setAttribute("STM_REQ_NO",rsTmp2.getString("STM_REQ_NO"));
                ObjSTMReqItem.setAttribute("STM_REQ_TYPE",rsTmp2.getInt("STM_REQ_TYPE"));
                ObjSTMReqItem.setAttribute("STM_REQ_SR_NO",rsTmp2.getLong("STM_REQ_SR_NO"));
                ObjSTMReqItem.setAttribute("ITEM_ID",rsTmp2.getString("ITEM_ID"));
                ObjSTMReqItem.setAttribute("ITEM_EXTRA_DESC",rsTmp2.getString("ITEM_EXTRA_DESC"));
                ObjSTMReqItem.setAttribute("STM_REQ_QTY",rsTmp2.getFloat("STM_REQ_QTY"));
                
                double IssuedQty=data.getDoubleValueFromDB("SELECT SUM(QTY) AS TOTAL_QTY FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B WHERE A.STM_NO=B.STM_NO AND A.APPROVED=1 AND A.CANCELLED=0 AND B.STM_REQ_NO='"+pSTMNo+"' AND B.STM_REQ_SR_NO="+rsTmp2.getLong("STM_REQ_SR_NO"));
                
                ObjSTMReqItem.setAttribute("ISSUED_QTY",IssuedQty);
                ObjSTMReqItem.setAttribute("BAL_QTY",rsTmp2.getDouble("STM_REQ_QTY")-IssuedQty);
                ObjSTMReqItem.setAttribute("UNIT",rsTmp2.getLong("UNIT"));
                ObjSTMReqItem.setAttribute("REMARKS",rsTmp2.getString("REMARKS"));
                ObjSTMReqItem.setAttribute("REQUIRED_DATE",rsTmp2.getString("REQUIRED_DATE"));
                ObjSTMReqItem.setAttribute("CANCELLED",rsTmp2.getInt("CANCELLED"));
                ObjSTMReqItem.setAttribute("CREATED_BY",rsTmp2.getLong("CREATED_BY"));
                ObjSTMReqItem.setAttribute("CREATED_DATE",rsTmp2.getString("CREATED_DATE"));
                ObjSTMReqItem.setAttribute("MODIFIED_BY",rsTmp2.getLong("MODIFIED_BY"));
                ObjSTMReqItem.setAttribute("MODIFIED_DATE",rsTmp2.getString("MODIFIED_DATE"));
                
                List.put(Integer.toString(Counter1),ObjSTMReqItem);
                rsTmp2.next();
            }
            
            rsTmp.close();
            //tmpConn.close();
            tmpStmt.close();
            rsTmp2.close();
            tmpStmt2.close();
            
        }
        catch(Exception e) {
        }
        
        return List;
    }
    
    public boolean ShowHistory(int pCompanyID,String pDocNo) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_INV_STM_REQ_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND STM_REQ_NO='"+pDocNo+"' AND STM_REQ_TYPE=2");
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
    
    
    public static HashMap getHistoryList(int pCompanyID,String pDocNo) {
        HashMap List=new HashMap();
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_STM_REQ_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND STM_REQ_NO='"+pDocNo+"' AND STM_REQ_TYPE=2");
            
            while(rsTmp.next()) {
                clsSTMRequisitionRaw ObjMR=new clsSTMRequisitionRaw();
                
                ObjMR.setAttribute("STM_REQ_NO",rsTmp.getString("STM_REQ_NO"));
                ObjMR.setAttribute("STM_REQ_DATE",rsTmp.getString("STM_REQ_DATE"));
                ObjMR.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjMR.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjMR.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjMR.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjMR.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjMR);
            }
            
            rsTmp.close();
            stTmp.close();
            //tmpConn.close();
            return List;
            
        }
        catch(Exception e) {
            return List;
        }
    }
    
    public static String getDocStatus(int pCompanyID,String pDocNo) {
        ResultSet rsTmp;
        String strMessage="";
        
        try {
            //First check that Document exist
            rsTmp=data.getResult("SELECT STM_REQ_NO,APPROVED,CANCELLED FROM D_INV_STM_REQ_HEADER WHERE COMPANY_ID="+pCompanyID+" AND STM_REQ_NO='"+pDocNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                if(rsTmp.getBoolean("APPROVED")) {
                    if(rsTmp.getBoolean("CANCELLED")) {
                        strMessage="Document is cancelled";
                    }
                    else {
                        strMessage="";
                    }
                }
                else {
                    strMessage="Document is created but is under approval";
                }
                
            }
            else {
                strMessage="Document does not exist";
            }
            rsTmp.close();
        }
        catch(Exception e) {
        }
        
        return strMessage;
        
    }
    
    
    public static boolean CanCancel(int pCompanyID,String pDocNo) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            rsTmp=data.getResult("SELECT STM_REQ_NO FROM D_INV_STM_REQ_HEADER WHERE COMPANY_ID="+pCompanyID+" AND STM_REQ_NO='"+pDocNo+"' AND CANCELLED=0 AND STM_REQ_TYPE=2 ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                canCancel=true;
            }
            
            rsTmp.close();
        }
        catch(Exception e) {
            
            
        }
        
        return canCancel;
    }
    
    
    public static boolean CancelDoc(int pCompanyID,String pDocNo) {
        
        ResultSet rsTmp=null,rsIndent=null;
        boolean Cancelled=false;
        
        try {
            if(CanCancel(pCompanyID,pDocNo)) {
                
                boolean ApprovedDoc=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_INV_STM_REQ_HEADER WHERE COMPANY_ID="+pCompanyID+" AND STM_REQ_NO='"+pDocNo+"' AND STM_REQ_TYPE=2");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedDoc) {
                    
                }
                else {
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND MODULE_ID="+(clsSTMRequisitionRaw.ModuleID));
                }
                
                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_INV_STM_REQ_HEADER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND STM_REQ_NO='"+pDocNo+"' AND STM_REQ_TYPE=2");
                
                Cancelled=true;
            }
            
            rsTmp.close();
            rsIndent.close();
        }
        catch(Exception e) {
            
        }
        
        return Cancelled;
    }
    
    public boolean Amend() {
        Statement stHistory,stHDetail,stHeader;
        ResultSet rsHistory,rsHDetail,rsHeader;
        
        try {
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_STM_REQ_HEADER_H WHERE STM_REQ_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_INV_STM_REQ_DETAIL_H WHERE STM_REQ_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            String theDocNo=getAttribute("STM_REQ_NO").getString();
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_REQ_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND TRIM(STM_REQ_NO)='"+theDocNo+"'");
            //rsHeader.first();
            
            //=========== Short Close Mode =============//
            //Qty. must be lowered not exceeded //
            for(int i=1;i<=colSTMReqRawItems.size();i++) {
                clsSTMReqItem ObjItem=(clsSTMReqItem)colSTMReqRawItems.get(Integer.toString(i));
                
                String ItemID=ObjItem.getAttribute("ITEM_ID").getString();
                String ReqNo=getAttribute("STM_REQ_NO").getString();
                int ReqSrNo=ObjItem.getAttribute("STM_REQ_SR_NO").getInt();
                
                double Qty=ObjItem.getAttribute("STM_REQ_QTY").getDouble();
                int SrNo=ObjItem.getAttribute("STM_REQ_SR_NO").getInt();
                double ExQty=0;
                double OriginalQty=0;
                
                //Get Original Qty.
                ResultSet rsTmp=data.getResult("SELECT STM_REQ_QTY FROM D_INV_STM_REQ_DETAIL WHERE STM_REQ_NO='"+theDocNo+"' AND STM_REQ_SR_NO="+SrNo);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    OriginalQty=rsTmp.getDouble("STM_REQ_QTY");
                }
                
                
                //Get Executed Qty
                rsTmp=data.getResult("SELECT SUM(QTY) AS SUMQTY FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B WHERE A.STM_NO=B.STM_NO AND A.STM_TYPE=B.STM_TYPE AND A.CANCELLED=0 AND STM_REQ_NO='"+theDocNo+"' AND STM_REQ_SR_NO="+SrNo);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ExQty=rsTmp.getDouble("SUMQTY");
                }
                
                if(Qty<ExQty) {
                    LastError="Qty cannot be decreased to "+Qty+". It can be short closed upto "+ExQty;
                    return false;
                }
                
            }
            //=========================================//
            
            
            //=======Short Close Indent ===========//
            
            for(int i=1;i<=colSTMReqRawItems.size();i++) {
                clsSTMReqItem ObjItem=(clsSTMReqItem)colSTMReqRawItems.get(Integer.toString(i));
                
                double Qty=ObjItem.getAttribute("STM_REQ_QTY").getDouble();
                int SrNo=ObjItem.getAttribute("STM_REQ_SR_NO").getInt();
                double STMQty = data.getDoubleValueFromDB("SELECT SUM(QTY) AS SUMQTY FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B WHERE A.STM_NO=B.STM_NO AND A.STM_TYPE=B.STM_TYPE AND A.CANCELLED=0 AND STM_REQ_NO='"+theDocNo+"' AND STM_REQ_SR_NO="+SrNo);
                double IssueQty = data.getDoubleValueFromDB("SELECT ISSUE_QTY FROM D_INV_STM_REQ_DETAIL WHERE STM_REQ_NO='"+theDocNo+"' AND STM_REQ_SR_NO="+SrNo);
                
                data.Execute("UPDATE D_INV_STM_REQ_DETAIL SET STM_REQ_QTY="+Qty+",BAL_QTY="+(Qty-STMQty-IssueQty)+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE STM_REQ_NO='"+theDocNo+"' AND STM_REQ_SR_NO="+SrNo);
                data.Execute("UPDATE D_INV_STM_REQ_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE STM_REQ_NO='"+theDocNo+"' AND STM_REQ_SR_NO="+SrNo);
            }
            //=====================================//
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_INV_STM_REQ_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND STM_REQ_NO='"+(String)getAttribute("STM_REQ_NO").getObj()+"'");
            RevNo++;
            String RevDocNo=getAttribute("STM_REQ_NO").getString();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",getAttribute("MODIFIED_BY").getInt());
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            rsHistory.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getLong());
            rsHistory.updateString("STM_REQ_NO",getAttribute("STM_REQ_NO").getString());
            rsHistory.updateInt("STM_REQ_TYPE",getAttribute("STM_REQ_TYPE").getInt());
            rsHistory.updateString("STM_REQ_DATE",getAttribute("STM_REQ_DATE").getString());
            rsHistory.updateLong("FOR_DEPT_ID",getAttribute("FOR_DEPT_ID").getLong());
            rsHistory.updateString("PURPOSE",getAttribute("PURPOSE").getString());
            rsHistory.updateLong("TRANSFER_TO_UNIT",getAttribute("TRANSFER_TO_UNIT").getLong());
            rsHistory.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.updateLong("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getLong());
            rsHistory.updateLong("CREATED_BY",getAttribute("CREATED_BY").getLong());
            rsHistory.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsHistory.updateLong("MODIFIED_BY",getAttribute("MODIFIED_BY").getLong());
            rsHistory.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();
            
            //Now Insert records into detail table
            for(int i=1;i<=colSTMReqRawItems.size();i++) {
                clsSTMReqItem ObjSTMReqItem=(clsSTMReqItem) colSTMReqRawItems.get(Integer.toString(i));
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getLong());
                rsHDetail.updateString("STM_REQ_NO",getAttribute("STM_REQ_NO").getString());
                rsHDetail.updateInt("STM_REQ_TYPE",getAttribute("STM_REQ_TYPE").getInt());
                rsHDetail.updateInt("STM_REQ_SR_NO",i);
                rsHDetail.updateString("ITEM_ID",ObjSTMReqItem.getAttribute("ITEM_ID").getString());
                rsHDetail.updateString("ITEM_EXTRA_DESC",ObjSTMReqItem.getAttribute("ITEM_EXTRA_DESC").getString());
                rsHDetail.updateDouble("STM_REQ_QTY",ObjSTMReqItem.getAttribute("STM_REQ_QTY").getDouble());
                rsHDetail.updateDouble("ISSUED_QTY",0); //Balance Qty will be balance qty
                rsHDetail.updateDouble("BAL_QTY",ObjSTMReqItem.getAttribute("STM_REQ_QTY").getDouble()); //Balance Qty will be balance qty
                rsHDetail.updateLong("UNIT",ObjSTMReqItem.getAttribute("UNIT").getLong());
                rsHDetail.updateString("REMARKS",ObjSTMReqItem.getAttribute("REMARKS").getString());
                rsHDetail.updateString("REQUIRED_DATE",ObjSTMReqItem.getAttribute("REQUIRED_DATE").getString());
                rsHDetail.updateLong("CREATED_BY",getAttribute("CREATED_BY").getLong());
                rsHDetail.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                rsHDetail.updateLong("MODIFIED_BY",getAttribute("MODIFIED_BY").getLong());
                rsHDetail.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
            }
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    private boolean TransferItem(int lCompanyID, HashMap colSTMReqRawItems) {
        String ItemID= "";
        int TransferToCompanyID=0;
        String dbURL = "";
        String Remarks="";
        int ItemHierarchyID=0;
        int UserID = 0;
        Connection Conn1=null;
        try {
            for(int i=1;i<=colSTMReqRawItems.size();i++) {
                clsSTMReqItem ObjSTMReqItem=(clsSTMReqItem) colSTMReqRawItems.get(Integer.toString(i));
                ItemID = ObjSTMReqItem.getAttribute("ITEM_ID").getString();
                
                if(lCompanyID==2) {
                    TransferToCompanyID=3;
                    dbURL = clsFinYear.getDBURL(TransferToCompanyID,EITLERPGLOBAL.FinYearFrom);
                    Remarks = "Transfered from Baroda to Ank on " + EITLERPGLOBAL.getCurrentDate() + " by " + EITLERPGLOBAL.gLoginID + " For STM";
                }
                
                if(lCompanyID==3) {
                    TransferToCompanyID=2;
                    dbURL = clsFinYear.getDBURL(TransferToCompanyID,EITLERPGLOBAL.FinYearFrom);
                    Remarks = "Transfered from Ank to Baroda on " + EITLERPGLOBAL.getCurrentDate() + " by " + EITLERPGLOBAL.gLoginID + " For STM";
                }
                
                HashMap List=clsApprovalRules.getApprovalRules(TransferToCompanyID, 1, "CHOOSE_HIERARCHY", "DEFAULT", "",dbURL);
                
                if(List.size()>0) {
                    //Get the Result of the Rule which would be the hierarchy no.
                    clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                    ItemHierarchyID = UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                    UserID = clsHierarchy.getFinalApprover(TransferToCompanyID,ItemHierarchyID,dbURL); 
                }
                
                if(data.IsRecordExist("SELECT * FROM D_INV_ITEM_MASTER WHERE ITEM_ID='"+ItemID+"' AND COMPANY_ID="+TransferToCompanyID+" AND APPROVED=1 AND CANCELLED=0 ", dbURL)) {
                    continue;
                }
                
                clsItem objTransferForm = (clsItem)clsItem.getObjectEx(EITLERPGLOBAL.gCompanyID, ItemID);
                
                Conn1=data.getConn(dbURL);
                Stmt=Conn1.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                clsItem objTransferTo = new clsItem();
                objTransferTo.LoadDataToTransfer(TransferToCompanyID);
                
                long fItemSysID = objTransferForm.getAttribute("ITEM_SYS_ID").getLong();
                long tItemSysID = data.getMaxID(TransferToCompanyID,"D_INV_ITEM_MASTER","ITEM_SYS_ID",dbURL);
                objTransferTo.useSpecificURL=true;
                objTransferTo.specifiedURL=dbURL;
                objTransferTo.setAttribute("COMPANY_ID",TransferToCompanyID);
                objTransferTo.setAttribute("ITEM_ID",objTransferForm.getAttribute("ITEM_ID").getString());
                objTransferTo.setAttribute("ITEM_SYS_ID",tItemSysID );
                objTransferTo.setAttribute("ITEM_DESCRIPTION",objTransferForm.getAttribute("ITEM_DESCRIPTION").getString());
                objTransferTo.setAttribute("ONETIME",objTransferForm.getAttribute("ONETIME").getBool());
                objTransferTo.setAttribute("RND_APPROVAL",objTransferForm.getAttribute("RND_APPROVAL").getBool());
                objTransferTo.setAttribute("GROUP_CODE",objTransferForm.getAttribute("GROUP_CODE").getInt());
                objTransferTo.setAttribute("SEARCH_KEY",objTransferForm.getAttribute("SEARCH_KEY").getString());
                objTransferTo.setAttribute("WAREHOUSE_ID",objTransferForm.getAttribute("WAREHOUSE_ID").getString());
                objTransferTo.setAttribute("CATEGORY_ID",13); //objTransferForm.getAttribute("CATEGORY_ID").getInt()
                objTransferTo.setAttribute("UNIT",objTransferForm.getAttribute("UNIT").getInt());
                objTransferTo.setAttribute("LOCATION_ID",objTransferForm.getAttribute("LOCATION_ID").getString());
                objTransferTo.setAttribute("DESC",objTransferForm.getAttribute("DESC").getInt());
                objTransferTo.setAttribute("MAKE",objTransferForm.getAttribute("MAKE").getInt());
                objTransferTo.setAttribute("SIZE",objTransferForm.getAttribute("SIZE").getInt());
                objTransferTo.setAttribute("ABC",objTransferForm.getAttribute("ABC").getString());
                objTransferTo.setAttribute("XYZ",objTransferForm.getAttribute("XYZ").getString());
                objTransferTo.setAttribute("VEN",objTransferForm.getAttribute("VEN").getString());
                objTransferTo.setAttribute("FSN",objTransferForm.getAttribute("FSN").getString());
                objTransferTo.setAttribute("MF",objTransferForm.getAttribute("MF").getString());
                objTransferTo.setAttribute("MAINTAIN_STOCK",objTransferForm.getAttribute("MAINTAIN_STOCK").getString());
                objTransferTo.setAttribute("AVG_QTY",objTransferForm.getAttribute("AVG_QTY").getDouble());
                objTransferTo.setAttribute("MIN_QTY",objTransferForm.getAttribute("MIN_QTY").getDouble());
                objTransferTo.setAttribute("MAX_QTY",objTransferForm.getAttribute("MAX_QTY").getDouble());
                objTransferTo.setAttribute("TOLERANCE_LIMIT",objTransferForm.getAttribute("TOLERANCE_LIMIT").getDouble());
                objTransferTo.setAttribute("DNP",objTransferForm.getAttribute("DNP").getDouble());
                objTransferTo.setAttribute("UNIT_RATE",objTransferForm.getAttribute("UNIT_RATE").getDouble());
                objTransferTo.setAttribute("QTR1_RATE",objTransferForm.getAttribute("QTR1_RATE").getDouble());
                objTransferTo.setAttribute("QTR2_RATE",objTransferForm.getAttribute("QTR2_RATE").getDouble());
                objTransferTo.setAttribute("QTR3_RATE",objTransferForm.getAttribute("QTR3_RATE").getDouble());
                objTransferTo.setAttribute("QTR4_RATE",objTransferForm.getAttribute("QTR4_RATE").getDouble());
                objTransferTo.setAttribute("REBATE",objTransferForm.getAttribute("REBATE").getDouble());
                objTransferTo.setAttribute("EXCISE_APPLICABLE",objTransferForm.getAttribute("EXCISE_APPLICABLE").getBool());
                objTransferTo.setAttribute("EXCISE",objTransferForm.getAttribute("EXCISE").getDouble());
                objTransferTo.setAttribute("CHAPTER_NO",objTransferForm.getAttribute("CHAPTER_NO").getInt());
                objTransferTo.setAttribute("TAXABLE",objTransferForm.getAttribute("TAXABLE").getBool());
                objTransferTo.setAttribute("OPENING_QTY",objTransferForm.getAttribute("OPENING_QTY").getDouble());
                objTransferTo.setAttribute("OPENING_VALUE",objTransferForm.getAttribute("OPENING_VALUE").getDouble());
                objTransferTo.setAttribute("TOTAL_RECEIPT_QTY",objTransferForm.getAttribute("TOTAL_RECEIPT_QTY").getDouble());
                objTransferTo.setAttribute("TOTAL_ISSUED_QTY",objTransferForm.getAttribute("TOTAL_ISSUED_QTY").getDouble());
                objTransferTo.setAttribute("LAST_RECEIPT_DATE",objTransferForm.getAttribute("LAST_RECEIPT_DATE").getString());
                objTransferTo.setAttribute("LAST_ISSUED_DATE",objTransferForm.getAttribute("LAST_ISSUED_DATE").getString());
                objTransferTo.setAttribute("ZERO_RECEIPT_QTY",objTransferForm.getAttribute("ZERO_RECEIPT_QTY").getDouble());
                objTransferTo.setAttribute("ZERO_ISSUED_QTY",objTransferForm.getAttribute("ZERO_ISSUED_QTY").getDouble());
                objTransferTo.setAttribute("ZERO_VALUE_QTY",objTransferForm.getAttribute("ZERO_VALUE_QTY").getDouble());
                objTransferTo.setAttribute("REJECTED_QTY",objTransferForm.getAttribute("REJECTED_QTY").getDouble());
                objTransferTo.setAttribute("ON_HAND_QTY",objTransferForm.getAttribute("ON_HAND_QTY").getDouble());
                objTransferTo.setAttribute("AVAILABLE_QTY",objTransferForm.getAttribute("AVAILABLE_QTY").getDouble());
                objTransferTo.setAttribute("ALLOCATED_QTY",objTransferForm.getAttribute("ALLOCATED_QTY").getDouble());
                objTransferTo.setAttribute("LAST_TRANS_DATE",objTransferForm.getAttribute("LAST_TRANS_DATE").getString());
                objTransferTo.setAttribute("LAST_GRN_NO",objTransferForm.getAttribute("LAST_GRN_NO").getString());
                objTransferTo.setAttribute("LAST_GRN_DATE",objTransferForm.getAttribute("LAST_GRN_DATE").getString());
                objTransferTo.setAttribute("LAST_PO_NO",objTransferForm.getAttribute("LAST_PO_NO").getString());
                objTransferTo.setAttribute("LAST_PO_DATE",objTransferForm.getAttribute("LAST_PO_DATE").getString());
                objTransferTo.setAttribute("CAPTIVE_CONSUMABLE",objTransferForm.getAttribute("CAPTIVE_CONSUMABLE").getBool());
                objTransferTo.setAttribute("BLOCKED",objTransferForm.getAttribute("BLOCKED").getBool());
                objTransferTo.setAttribute("REORDER_LEVEL",objTransferForm.getAttribute("REORDER_LEVEL").getDouble());
                objTransferTo.setAttribute("SUPPLIER_ID",objTransferForm.getAttribute("SUPPLIER_ID").getInt());
                objTransferTo.setAttribute("ITEM_HIERARCHY_ID",ItemHierarchyID);
                objTransferTo.setAttribute("APPROVED",true);
                objTransferTo.setAttribute("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                objTransferTo.setAttribute("REJECTED",false);
                objTransferTo.setAttribute("REJECTED_DATE","0000-00-00");
                objTransferTo.setAttribute("CREATED_BY",UserID);
                objTransferTo.setAttribute("CREATED_DATE",objTransferForm.getAttribute("CREATED_DATE").getString());
                objTransferTo.setAttribute("MODIFIED_BY",EITLERPGLOBAL.gNewUserID);
                objTransferTo.setAttribute("MODIFIED_DATE",objTransferForm.getAttribute("MODIFIED_DATE").getString());
                objTransferTo.setAttribute("SPECIAL_APPROVAL",objTransferForm.getAttribute("SPECIAL_APPROVAL").getString());
                objTransferTo.setAttribute("FROM",UserID);
                objTransferTo.setAttribute("TO",UserID);
                objTransferTo.setAttribute("FROM_REMARKS",Remarks);
                objTransferTo.setAttribute("REMARKS",Remarks);
                
                objTransferTo.setAttribute("APPROVAL_STATUS","F");
                // ----------------- End of Header Fields ------------------------------------//
                
                //  ---------- Item R&D Approvers List --------------
                objTransferTo.colRNDApprovers.clear();
                int CompanyID=EITLERPGLOBAL.gCompanyID;
                Statement tmpStmt = Conn1.createStatement();
                
                ResultSet rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_ITEM_RND_APPROVERS WHERE COMPANY_ID="+CompanyID+" AND ITEM_SYS_ID="+fItemSysID );
                rsTmp.first();
                int Counter=0;
                if(rsTmp.getRow() > 0) {
                    while(!rsTmp.isAfterLast()) {
                        Counter=Counter+1;
                        clsItemRNDApprover ObjApprover=new clsItemRNDApprover();
                        
                        ObjApprover.setAttribute("COMPANY_ID",TransferToCompanyID);
                        ObjApprover.setAttribute("ITEM_SYS_ID",tItemSysID);
                        ObjApprover.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                        ObjApprover.setAttribute("USER_ID",rsTmp.getInt("USER_ID"));
                        
                        objTransferTo.colRNDApprovers.put(Long.toString(Counter),ObjApprover);
                        rsTmp.next();
                    }
                }
                if(!objTransferTo.Insert()) {
                    LastError += objTransferTo.LastError;
                    return false;
                }
            }
        }
        catch(Exception e) {
            return false;
        }
        return true;
    }
}
