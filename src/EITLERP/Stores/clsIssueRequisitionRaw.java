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


/**
 *
 * @author  jadave
 * @version
 */

public class clsIssueRequisitionRaw {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    //Requisition Material Collection
    public HashMap colMRItems;
    //public HashMap colIssueReqLotItems=new HashMap();
    
    
    //History Related properties
    private boolean HistoryView=false;
    
    public static int ModuleID=64;
    
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
    public clsIssueRequisitionRaw() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("REQ_NO", new Variant(""));
        props.put("REQ_TYPE",new Variant(2));
        props.put("REQ_DATE",new Variant(""));
        props.put("STORE", new Variant(""));
        props.put("SOURCE_DEPT_ID",new Variant(0));
        props.put("DEST_DEPT_ID",new Variant(0));
        props.put("PURPOSE", new Variant(""));
        props.put("REQUIRED_DATE", new Variant(""));
        props.put("BUYER",new Variant(0));
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("REMARKS", new Variant(""));
        props.put("CANCELED", new Variant(false));
        props.put("STATUS", new Variant(""));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("PREFIX",new Variant("")); //For Autonumber generation
        
        //Create a new object for MR Item collection
        colMRItems=new HashMap();
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
    }
    
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_INV_ISSUE_REQ_HEADER WHERE COMPANY_ID="+pCompanyID+" AND REQ_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND REQ_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' AND REQ_TYPE=2 ORDER BY REQ_NO");
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
        
        Statement stHistory,stHDetail,stHeader,stLot,stHLot;
        ResultSet rsHistory,rsHDetail,rsHeader,rsLot,rsHLot;
        
        try {
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date ReqDate=java.sql.Date.valueOf((String)getAttribute("REQ_DATE").getObj());
            
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
            stHLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_ISSUE_REQ_HEADER_H WHERE REQ_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_INV_ISSUE_REQ_DETAIL_H WHERE REQ_NO='1'");
            rsHDetail.first();
            rsHLot=stHLot.executeQuery("SELECT * FROM D_INV_ISSUE_REQ_LOT_H WHERE REQ_NO='1'");
            rsHLot.first();
            
            //------------------------------------//
            
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_REQ_HEADER WHERE REQ_NO='1'");
            //rsHeader.first();
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //--------- Generate Gatepass requisition no.  ------------
            setAttribute("REQ_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,clsIssueRequisitionRaw.ModuleID, (int)getAttribute("FFNO").getVal(),true));
            //-------------------------------------------------
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateString("REQ_NO",(String)getAttribute("REQ_NO").getObj());
            rsResultSet.updateInt("REQ_TYPE",getAttribute("REQ_TYPE").getInt());
            rsResultSet.updateString("REQ_DATE",(String)getAttribute("REQ_DATE").getObj());
            rsResultSet.updateString("STORE",(String)getAttribute("STORE").getObj());
            rsResultSet.updateLong("SOURCE_DEPT_ID",(long) getAttribute("SOURCE_DEPT_ID").getVal());
            rsResultSet.updateLong("DEST_DEPT_ID",(long) getAttribute("DEST_DEPT_ID").getVal());
            rsResultSet.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsResultSet.updateString("REQUIRED_DATE",(String)getAttribute("REQUIRED_DATE").getObj());
            rsResultSet.updateLong("BUYER",(long) getAttribute("BUYER").getVal());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateBoolean("CANCELED",(boolean)getAttribute("CANCELED").getBool());
            rsResultSet.updateString("STATUS",(String)getAttribute("STATUS").getObj());
            rsResultSet.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateBoolean("CANCELED",false);
            rsResultSet.insertRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("REQ_NO",(String)getAttribute("REQ_NO").getObj());
            rsHistory.updateInt("REQ_TYPE",getAttribute("REQ_TYPE").getInt());
            rsHistory.updateString("REQ_DATE",(String)getAttribute("REQ_DATE").getObj());
            rsHistory.updateString("STORE",(String)getAttribute("STORE").getObj());
            rsHistory.updateLong("SOURCE_DEPT_ID",(long) getAttribute("SOURCE_DEPT_ID").getVal());
            rsHistory.updateLong("DEST_DEPT_ID",(long) getAttribute("DEST_DEPT_ID").getVal());
            rsHistory.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsHistory.updateString("REQUIRED_DATE",(String)getAttribute("REQUIRED_DATE").getObj());
            rsHistory.updateLong("BUYER",(long) getAttribute("BUYER").getVal());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateBoolean("CANCELED",(boolean)getAttribute("CANCELED").getBool());
            rsHistory.updateString("STATUS",(String)getAttribute("STATUS").getObj());
            rsHistory.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELED",false);
            rsHistory.insertRow();
            
            
            ResultSet rsTmp;
            Statement tmpStmt;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_ISSUE_REQ_DETAIL WHERE REQ_NO='1'");
            
            stLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsLot=stLot.executeQuery("SELECT * FROM D_INV_ISSUE_REQ_LOT WHERE REQ_NO='1'");
            rsLot.first();
            
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            
            //Now Insert records into detail table
            for(int i=1;i<=colMRItems.size();i++) {
                clsIssueReqItem ObjMRItems=(clsIssueReqItem) colMRItems.get(Integer.toString(i));
                
                ObjMRItems.setAttribute("BAL_QTY",(float)ObjMRItems.getAttribute("REQ_QTY").getVal());
                
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",(long)nCompanyID);
                rsTmp.updateString("REQ_NO",(String)getAttribute("REQ_NO").getObj());
                rsTmp.updateInt("REQ_TYPE",getAttribute("REQ_TYPE").getInt());
                rsTmp.updateInt("SR_NO",i);
                rsTmp.updateString("ITEM_CODE",(String)ObjMRItems.getAttribute("ITEM_CODE").getObj());
                rsTmp.updateString("ITEM_EXTRA_DESC",(String)ObjMRItems.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsTmp.updateInt("COST_CENTER_CODE",ObjMRItems.getAttribute("COST_CENTER_CODE").getInt());
                rsTmp.updateInt("DEPT_ID",ObjMRItems.getAttribute("DEPT_ID").getInt());
                rsTmp.updateFloat("REQ_QTY",(float)ObjMRItems.getAttribute("REQ_QTY").getVal());
                rsTmp.updateFloat("BAL_QTY",(float)ObjMRItems.getAttribute("REQ_QTY").getVal()); //Balance Qty will be balance qty
                rsTmp.updateFloat("ISSUED_QTY",0); //Balance Qty will be balance qty
                rsTmp.updateLong("UNIT",(long)ObjMRItems.getAttribute("UNIT_ID").getVal());
                rsTmp.updateString("MFG_PROG_NO",(String)ObjMRItems.getAttribute("MFG_PROG_NO").getObj());
                rsTmp.updateString("BLEND_CODE",(String)ObjMRItems.getAttribute("BLEND_CODE").getObj());
                rsTmp.updateString("REMARKS",(String)ObjMRItems.getAttribute("REMARKS").getObj());
                rsTmp.updateString("REQUIRED_DATE",(String)ObjMRItems.getAttribute("REQUIRED_DATE").getObj());
                rsTmp.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateLong("COMPANY_ID",(long)nCompanyID);
                rsHDetail.updateString("REQ_NO",(String)getAttribute("REQ_NO").getObj());
                rsHDetail.updateInt("REQ_TYPE",getAttribute("REQ_TYPE").getInt());
                rsHDetail.updateInt("SR_NO",i);
                rsHDetail.updateString("ITEM_CODE",(String)ObjMRItems.getAttribute("ITEM_CODE").getObj());
                rsHDetail.updateString("ITEM_EXTRA_DESC",(String)ObjMRItems.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsHDetail.updateInt("COST_CENTER_CODE",ObjMRItems.getAttribute("COST_CENTER_CODE").getInt());
                rsHDetail.updateInt("DEPT_ID",ObjMRItems.getAttribute("DEPT_ID").getInt());
                rsHDetail.updateFloat("REQ_QTY",(float)ObjMRItems.getAttribute("REQ_QTY").getVal());
                rsHDetail.updateFloat("BAL_QTY",(float)ObjMRItems.getAttribute("REQ_QTY").getVal()); //Balance Qty will be balance qty
                rsHDetail.updateFloat("ISSUED_QTY",0); //Balance Qty will be balance qty
                rsHDetail.updateLong("UNIT",(long)ObjMRItems.getAttribute("UNIT_ID").getVal());
                rsHDetail.updateString("REMARKS",(String)ObjMRItems.getAttribute("REMARKS").getObj());
                rsHDetail.updateString("REQUIRED_DATE",(String)ObjMRItems.getAttribute("REQUIRED_DATE").getObj());
                rsHDetail.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("MFG_PROG_NO",(String)ObjMRItems.getAttribute("MFG_PROG_NO").getObj());
                rsHDetail.updateString("BLEND_CODE",(String)ObjMRItems.getAttribute("BLEND_CODE").getObj());
                rsHDetail.insertRow();
                
                //Now insert lot nos.
                for(int l=1;l<=ObjMRItems.colIssueReqLot.size();l++) {
                    clsIssueReqItemDetail ObjLot=(clsIssueReqItemDetail)ObjMRItems.colIssueReqLot.get(Integer.toString(l));
                    
                    rsLot.moveToInsertRow();
                    rsLot.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                    rsLot.updateString("REQ_NO",(String)getAttribute("REQ_NO").getObj());
                    rsLot.updateInt("REQ_SR_NO",i);
                    rsLot.updateInt("REQ_TYPE",2);
                    rsLot.updateInt("SR_NO",l);
                    rsLot.updateString("ITEM_ID",(String)ObjLot.getAttribute("ITEM_ID").getObj());
                    rsLot.updateString("ITEM_LOT_NO",(String)ObjLot.getAttribute("ITEM_LOT_NO").getObj());
                    rsLot.updateString("AUTO_LOT_NO",(String)ObjLot.getAttribute("AUTO_LOT_NO").getObj());
                    rsLot.updateDouble("ISSUED_LOT_QTY",ObjLot.getAttribute("ISSUED_LOT_QTY").getVal());
                    rsLot.updateBoolean("CANCELLED",false);
                    rsLot.updateBoolean("APPROVED",false);
                    rsLot.updateString("APPROVED_DATE","0000-00-00");            
                    rsLot.updateBoolean("CHANGED",true);
                    rsLot.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsLot.insertRow();
                    
                    rsHLot.moveToInsertRow();
                    rsHLot.updateInt("REVISION_NO",1);
                    rsHLot.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                    rsHLot.updateString("REQ_NO",(String)getAttribute("REQ_NO").getObj());
                    rsHLot.updateInt("REQ_SR_NO",i);
                    rsHLot.updateInt("REQ_TYPE",2);
                    rsHLot.updateInt("SR_NO",l);
                    rsHLot.updateString("ITEM_ID",(String)ObjLot.getAttribute("ITEM_ID").getObj());
                    rsHLot.updateString("ITEM_LOT_NO",(String)ObjLot.getAttribute("ITEM_LOT_NO").getObj());
                    rsHLot.updateString("AUTO_LOT_NO",(String)ObjLot.getAttribute("AUTO_LOT_NO").getObj());
                    rsHLot.updateDouble("ISSUED_LOT_QTY",ObjLot.getAttribute("ISSUED_LOT_QTY").getVal());
                    rsHLot.updateBoolean("CANCELLED",false);
                    rsHLot.updateBoolean("APPROVED",false);
                    rsHLot.updateString("APPROVED_DATE","0000-00-00");            
                    rsHLot.updateBoolean("CHANGED",true);
                    rsHLot.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsHLot.insertRow();
                }
            }
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=clsIssueRequisitionRaw.ModuleID;
            ObjFlow.DocNo=(String)getAttribute("REQ_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_ISSUE_REQ_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="REQ_NO";
            ObjFlow.DocDate=(String)getAttribute("REQ_DATE").getObj();
            
            if(ObjFlow.Status.equals("H")) {
                ObjFlow.Status="A";
                ObjFlow.To=ObjFlow.From;
                ObjFlow.UpdateFlow();
            }
            else {
                if(!ObjFlow.UpdateFlow()) {
                    LastError=ObjFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------
            
            String AStatus=getAttribute("APPROVAL_STATUS").getString();
            if(AStatus.equals("F")) {
                
                String SQL = "SELECT * FROM D_INV_ISSUE_REQ_LOT WHERE REQ_NO='"+getAttribute("REQ_NO").getString()+"' ORDER BY REQ_NO,REQ_SR_NO,SR_NO";
                ResultSet rsLotUpdate = data.getResult(SQL);
                rsLotUpdate.first();
                while(!rsLotUpdate.isAfterLast()) {
                    int REQSrNo = rsLotUpdate.getInt("REQ_SR_NO");
                    int SrNo = rsLotUpdate.getInt("SR_NO");
                    String UpdateRecord = "UPDATE D_INV_ISSUE_REQ_LOT SET APPROVED=1, APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"', " +
                    "CHANGED=1, CHANGED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"' WHERE REQ_NO='"+getAttribute("REQ_NO").getString()+"' AND REQ_SR_NO="+ REQSrNo +" AND SR_NO="+SrNo+" ";
                    data.Execute(UpdateRecord);
                    
                    UpdateRecord = "UPDATE D_INV_ISSUE_REQ_LOT_H SET APPROVED=1, APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"', " +
                    "CHANGED=1, CHANGED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"' WHERE REQ_NO='"+getAttribute("REQ_NO").getString()+"' AND REQ_SR_NO="+ REQSrNo +" AND SR_NO="+SrNo+" ";
                    data.Execute(UpdateRecord);
                    
                    rsLotUpdate.next();
                }
            }
            
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
        Statement stHistory,stHDetail,stHeader,stLot,stHLot;
        ResultSet rsHistory,rsHDetail,rsHeader,rsLot,rsHLot;
        
        try {
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date ReqDate=java.sql.Date.valueOf((String)getAttribute("REQ_DATE").getObj());
            
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
            stHLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_ISSUE_REQ_HEADER_H WHERE REQ_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_INV_ISSUE_REQ_DETAIL_H WHERE REQ_NO='1'");
            rsHDetail.first();
            rsHLot=stHLot.executeQuery("SELECT * FROM D_INV_ISSUE_REQ_LOT_H WHERE REQ_NO='1'");
            rsHLot.first();
            //------------------------------------//
            
            String theDocNo=(String)getAttribute("REQ_NO").getObj();
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_REQ_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND TRIM(REQ_NO)='"+theDocNo+"'");
            //rsHeader.first();
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            rsResultSet.updateString("REQ_DATE",(String)getAttribute("REQ_DATE").getObj());
            rsResultSet.updateString("STORE",(String)getAttribute("STORE").getObj());
            rsResultSet.updateLong("SOURCE_DEPT_ID",(long) getAttribute("SOURCE_DEPT_ID").getVal());
            rsResultSet.updateLong("DEST_DEPT_ID",(long) getAttribute("DEST_DEPT_ID").getVal());
            rsResultSet.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsResultSet.updateString("REQUIRED_DATE",(String)getAttribute("REQUIRED_DATE").getObj());
            rsResultSet.updateLong("BUYER",(long) getAttribute("BUYER").getVal());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateBoolean("CANCELED",false);
            rsResultSet.updateString("STATUS",(String)getAttribute("STATUS").getObj());
            rsResultSet.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELED",false);
            rsResultSet.updateRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_INV_ISSUE_REQ_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND REQ_NO='"+(String)getAttribute("REQ_NO").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("REQ_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("REQ_NO",(String)getAttribute("REQ_NO").getObj());
            rsHistory.updateInt("REQ_TYPE",getAttribute("REQ_TYPE").getInt());
            rsHistory.updateString("REQ_DATE",(String)getAttribute("REQ_DATE").getObj());
            rsHistory.updateString("STORE",(String)getAttribute("STORE").getObj());
            rsHistory.updateLong("SOURCE_DEPT_ID",(long) getAttribute("SOURCE_DEPT_ID").getVal());
            rsHistory.updateLong("DEST_DEPT_ID",(long) getAttribute("DEST_DEPT_ID").getVal());
            rsHistory.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsHistory.updateString("REQUIRED_DATE",(String)getAttribute("REQUIRED_DATE").getObj());
            rsHistory.updateLong("BUYER",(long) getAttribute("BUYER").getVal());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateBoolean("CANCELED",false);
            rsHistory.updateString("STATUS",(String)getAttribute("STATUS").getObj());
            rsHistory.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();
            
            //First remove the old rows
            String mCompanyID=Long.toString((long)getAttribute("COMPANY_ID").getVal());
            String mReqNo=(String)getAttribute("REQ_NO").getObj();
            
            data.Execute("DELETE FROM D_INV_ISSUE_REQ_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND REQ_NO='"+mReqNo+"' AND REQ_TYPE=2");
            data.Execute("DELETE FROM D_INV_ISSUE_REQ_LOT WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND REQ_NO='"+mReqNo+"' AND REQ_TYPE=2");
            
            ResultSet rsTmp;
            Statement tmpStmt;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_ISSUE_REQ_DETAIL WHERE REQ_NO='1'");
            
            stLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsLot=stLot.executeQuery("SELECT * FROM D_INV_ISSUE_REQ_LOT WHERE REQ_NO='1'");
            rsLot.first();
            
            //Now Insert records into detail table
            for(int i=1;i<=colMRItems.size();i++) {
                clsIssueReqItem ObjMRItems=(clsIssueReqItem) colMRItems.get(Integer.toString(i));
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                rsTmp.updateString("REQ_NO",(String)getAttribute("REQ_NO").getObj());
                rsTmp.updateInt("REQ_TYPE",getAttribute("REQ_TYPE").getInt());
                rsTmp.updateLong("SR_NO",i);
                rsTmp.updateString("ITEM_CODE",(String)ObjMRItems.getAttribute("ITEM_CODE").getObj());
                rsTmp.updateString("ITEM_EXTRA_DESC",(String)ObjMRItems.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsTmp.updateInt("COST_CENTER_CODE",ObjMRItems.getAttribute("COST_CENTER_CODE").getInt());
                rsTmp.updateInt("DEPT_ID",ObjMRItems.getAttribute("DEPT_ID").getInt());
                rsTmp.updateFloat("REQ_QTY",(float)ObjMRItems.getAttribute("REQ_QTY").getVal());
                rsTmp.updateFloat("BAL_QTY",(float)ObjMRItems.getAttribute("REQ_QTY").getVal());
                rsTmp.updateFloat("ISSUED_QTY",0);
                rsTmp.updateLong("UNIT",(long)ObjMRItems.getAttribute("UNIT_ID").getVal());
                rsTmp.updateString("REMARKS",(String)ObjMRItems.getAttribute("REMARKS").getObj());
                rsTmp.updateString("REQUIRED_DATE",(String)ObjMRItems.getAttribute("REQUIRED_DATE").getObj());
                rsTmp.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateString("MFG_PROG_NO",(String)ObjMRItems.getAttribute("MFG_PROG_NO").getObj());
                rsTmp.updateString("BLEND_CODE",(String)ObjMRItems.getAttribute("BLEND_CODE").getObj());
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                rsHDetail.updateString("REQ_NO",(String)getAttribute("REQ_NO").getObj());
                rsHDetail.updateInt("REQ_TYPE",getAttribute("REQ_TYPE").getInt());
                rsHDetail.updateInt("SR_NO",i);
                rsHDetail.updateString("ITEM_CODE",(String)ObjMRItems.getAttribute("ITEM_CODE").getObj());
                rsHDetail.updateString("ITEM_EXTRA_DESC",(String)ObjMRItems.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsHDetail.updateInt("COST_CENTER_CODE",ObjMRItems.getAttribute("COST_CENTER_CODE").getInt());
                rsHDetail.updateInt("DEPT_ID",ObjMRItems.getAttribute("DEPT_ID").getInt());
                rsHDetail.updateFloat("REQ_QTY",(float)ObjMRItems.getAttribute("REQ_QTY").getVal());
                rsHDetail.updateFloat("ISSUED_QTY",0); //Balance Qty will be balance qty
                rsHDetail.updateFloat("BAL_QTY",(float)ObjMRItems.getAttribute("REQ_QTY").getVal()); //Balance Qty will be balance qty
                rsHDetail.updateLong("UNIT",(long)ObjMRItems.getAttribute("UNIT_ID").getVal());
                rsHDetail.updateString("REMARKS",(String)ObjMRItems.getAttribute("REMARKS").getObj());
                rsHDetail.updateString("REQUIRED_DATE",(String)ObjMRItems.getAttribute("REQUIRED_DATE").getObj());
                rsHDetail.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("MFG_PROG_NO",(String)ObjMRItems.getAttribute("MFG_PROG_NO").getObj());
                rsHDetail.updateString("BLEND_CODE",(String)ObjMRItems.getAttribute("BLEND_CODE").getObj());
                rsHDetail.insertRow();
                
                //Now insert lot nos.
                for(int l=1;l<=ObjMRItems.colIssueReqLot.size();l++) {
                    clsIssueReqItemDetail ObjLot=(clsIssueReqItemDetail)ObjMRItems.colIssueReqLot.get(Integer.toString(l));
                    
                    rsLot.moveToInsertRow();
                    rsLot.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                    rsLot.updateString("REQ_NO",(String)getAttribute("REQ_NO").getObj());
                    rsLot.updateInt("REQ_SR_NO",i);
                    rsLot.updateInt("REQ_TYPE",2);
                    rsLot.updateInt("SR_NO",l);
                    rsLot.updateString("ITEM_ID",(String)ObjLot.getAttribute("ITEM_ID").getObj());
                    rsLot.updateString("ITEM_LOT_NO",(String)ObjLot.getAttribute("ITEM_LOT_NO").getObj());
                    rsLot.updateString("AUTO_LOT_NO",(String)ObjLot.getAttribute("AUTO_LOT_NO").getObj());
                    rsLot.updateDouble("ISSUED_LOT_QTY",ObjLot.getAttribute("ISSUED_LOT_QTY").getVal());
                    rsLot.updateBoolean("CANCELLED",false);
                    rsLot.updateBoolean("APPROVED",false);
                    rsLot.updateString("APPROVED_DATE","0000-00-00");            
                    rsLot.updateBoolean("CHANGED",true);
                    rsLot.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsLot.insertRow();
                    
                    rsHLot.moveToInsertRow();
                    rsHLot.updateInt("REVISION_NO",RevNo);
                    rsHLot.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                    rsHLot.updateString("REQ_NO",(String)getAttribute("REQ_NO").getObj());
                    rsHLot.updateInt("REQ_SR_NO",i);
                    rsHLot.updateInt("REQ_TYPE",2);
                    rsHLot.updateInt("SR_NO",l);
                    rsHLot.updateString("ITEM_ID",(String)ObjLot.getAttribute("ITEM_ID").getObj());
                    rsHLot.updateString("ITEM_LOT_NO",(String)ObjLot.getAttribute("ITEM_LOT_NO").getObj());
                    rsHLot.updateString("AUTO_LOT_NO",(String)ObjLot.getAttribute("AUTO_LOT_NO").getObj());
                    rsHLot.updateDouble("ISSUED_LOT_QTY",ObjLot.getAttribute("ISSUED_LOT_QTY").getVal());
                    rsHLot.updateBoolean("CANCELLED",false);
                    rsHLot.updateBoolean("APPROVED",false);
                    rsHLot.updateString("APPROVED_DATE","0000-00-00");            
                    rsHLot.updateBoolean("CHANGED",true);
                    rsHLot.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsHLot.insertRow();
                }
                
            }
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=clsIssueRequisitionRaw.ModuleID;
            ObjFlow.DocNo=(String)getAttribute("REQ_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_ISSUE_REQ_HEADER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="REQ_NO";
            ObjFlow.DocDate=(String)getAttribute("REQ_DATE").getObj();
            
            //==== Handling Rejected Documents ==========//
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
            if(AStatus.equals("R")) {
                //Remove the Rejected Flag First
                //data.Execute("UPDATE D_INV_INDENT_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                //data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=3 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                //ObjFlow.IsCreator=true;
                ObjFlow.To=(int)getAttribute("SEND_DOC_TO").getVal();
                ObjFlow.ExplicitSendTo=true;
            }
            //==========================================//
            
            
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_INV_REQ_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND REQ_NO='"+ObjFlow.DocNo+"' AND REQ_TYPE=2");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+clsIssueRequisitionRaw.ModuleID+" AND DOC_NO='"+ObjFlow.DocNo+"'");
                
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
            }
            else {
                if(!ObjFlow.UpdateFlow()) {
                    LastError=ObjFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------
            
            AStatus=getAttribute("APPROVAL_STATUS").getString();
            if(AStatus.equals("F")) {
                
                String SQL = "SELECT * FROM D_INV_ISSUE_REQ_LOT WHERE REQ_NO='"+getAttribute("REQ_NO").getString()+"' ORDER BY REQ_NO,REQ_SR_NO,SR_NO";
                ResultSet rsLotUpdate = data.getResult(SQL);
                rsLotUpdate.first();
                while(!rsLotUpdate.isAfterLast()) {
                    int REQSrNo = rsLotUpdate.getInt("REQ_SR_NO");
                    int SrNo = rsLotUpdate.getInt("SR_NO");
                    String UpdateRecord = "UPDATE D_INV_ISSUE_REQ_LOT SET APPROVED=1, APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"', " +
                    "CHANGED=1, CHANGED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"' WHERE REQ_NO='"+getAttribute("REQ_NO").getString()+"' AND REQ_SR_NO="+ REQSrNo +" AND SR_NO="+SrNo+" ";
                    data.Execute(UpdateRecord);
                    
                    UpdateRecord = "UPDATE D_INV_ISSUE_REQ_LOT_H SET APPROVED=1, APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"', " +
                    "CHANGED=1, CHANGED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"' WHERE REQ_NO='"+getAttribute("REQ_NO").getString()+"' AND REQ_SR_NO="+ REQSrNo +" AND SR_NO="+SrNo+" ";
                    data.Execute(UpdateRecord);
                    
                    rsLotUpdate.next();
                }
            }
            
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_ISSUE_REQ_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND REQ_NO='"+pDocNo+"' AND REQ_TYPE=2 AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID="+clsIssueRequisitionRaw.ModuleID+" AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_ISSUE_REQ_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND REQ_NO='"+pDocNo+"' AND REQ_TYPE=2 AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID="+clsIssueRequisitionRaw.ModuleID+" AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
            String lDocNo=(String)getAttribute("REQ_NO").getObj();
            String strSQL="";
            
            //First check that record is editable
            if(CanDelete(lCompanyID,lDocNo,pUserID)) {
                String strQry = "DELETE FROM D_INV_ISSUE_REQ_HEADER WHERE COMPANY_ID=" + lCompanyID +" AND REQ_NO='"+lDocNo+"' AND REQ_TYPE=2";
                data.Execute(strQry);
                strQry = "DELETE FROM D_INV_ISSUE_REQ_DETAIL WHERE COMPANY_ID=" + lCompanyID +" AND REQ_NO='"+lDocNo+"' AND REQ_TYPE=2";
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
        String strCondition = " WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND REQ_NO='" + pReqNo + "'" ;
        clsIssueRequisitionRaw ObjMaterialRequisition = new clsIssueRequisitionRaw();
        ObjMaterialRequisition.Filter(strCondition,pCompanyID);
        return ObjMaterialRequisition;
    }
    
    
    public static HashMap getList(String pCondition) {
        ResultSet rsTmp=null,rsLot=null;
        Connection tmpConn=null;
        Statement tmpStmt=null,stLot=null;
        
        ResultSet rsTmp2=null;
        Statement tmpStmt2=null;
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement();
            
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_ISSUE_REQ_HEADER "+pCondition);
            
            Counter=0;
            while(rsTmp.next()) {
                Counter=Counter+1;
                clsIssueRequisitionRaw ObjMaterialRequisition =new clsIssueRequisitionRaw();
                ObjMaterialRequisition.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjMaterialRequisition.setAttribute("REQ_NO",rsTmp.getString("REQ_NO"));
                ObjMaterialRequisition.setAttribute("REQ_TYPE",rsTmp.getInt("REQ_TYPE"));
                ObjMaterialRequisition.setAttribute("REQ_DATE",rsTmp.getString("REQ_DATE"));
                ObjMaterialRequisition.setAttribute("STORE",rsTmp.getString("STORE"));
                ObjMaterialRequisition.setAttribute("SOURCE_DEPT_ID",rsTmp.getInt("SOURCE_DEPT_ID"));
                ObjMaterialRequisition.setAttribute("DEST_DEPT_ID",rsTmp.getInt("DEST_DEPT_ID"));
                ObjMaterialRequisition.setAttribute("PURPOSE",rsTmp.getString("PURPOSE"));
                ObjMaterialRequisition.setAttribute("REQUIRED_DATE",rsTmp.getString("REQUIRED_DATE"));
                ObjMaterialRequisition.setAttribute("BUYER",rsTmp.getInt("BUYER"));
                ObjMaterialRequisition.setAttribute("APPROVED",rsTmp.getInt("APPROVED"));
                ObjMaterialRequisition.setAttribute("APPROVED_DATE",rsTmp.getString("APPROVED_DATE"));
                ObjMaterialRequisition.setAttribute("REJECTED_DATE",rsTmp.getString("REJECTED_DATE"));
                ObjMaterialRequisition.setAttribute("REJECTED",rsTmp.getBoolean("REJECTED"));
                ObjMaterialRequisition.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                ObjMaterialRequisition.setAttribute("CANCELED",rsTmp.getInt("CANCELED"));
                ObjMaterialRequisition.setAttribute("STATUS",rsTmp.getString("STATUS"));
                ObjMaterialRequisition.setAttribute("CREATED_BY",rsTmp.getInt("CREATED_BY"));
                ObjMaterialRequisition.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjMaterialRequisition.setAttribute("MODIFIED_BY",rsTmp.getInt("MODIFIED_BY"));
                ObjMaterialRequisition.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                
                ObjMaterialRequisition.colMRItems.clear();
                String mCompanyID=Long.toString((long)ObjMaterialRequisition.getAttribute("COMPANY_ID").getVal());
                String mReqNo=(String)ObjMaterialRequisition.getAttribute("REQ_NO").getObj();
                
                tmpStmt2=tmpConn.createStatement();
                rsTmp2=tmpStmt2.executeQuery("SELECT * FROM D_INV_ISSUE_REQ_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND REQ_NO='" + mReqNo + "' AND REQ_TYPE=2");
                
                Counter=0;
                while(rsTmp2.next()) {
                    Counter=Counter+1;
                    clsIssueReqItem ObjMRItems=new clsIssueReqItem();
                    
                    ObjMRItems.setAttribute("COMPANY_ID",rsTmp2.getLong("COMPANY_ID"));
                    ObjMRItems.setAttribute("REQ_NO",rsTmp2.getString("REQ_NO"));
                    ObjMRItems.setAttribute("REQ_TYPE",rsTmp2.getInt("REQ_TYPE"));
                    ObjMRItems.setAttribute("SR_NO",rsTmp2.getLong("SR_NO"));
                    ObjMRItems.setAttribute("ITEM_CODE",rsTmp2.getString("ITEM_CODE"));
                    ObjMRItems.setAttribute("REQ_QTY",rsTmp2.getFloat("REQ_QTY"));
                    ObjMRItems.setAttribute("ISSUED_QTY",rsTmp2.getFloat("ISSUED_QTY"));
                    ObjMRItems.setAttribute("BAL_QTY",rsTmp2.getFloat("BAL_QTY"));
                    ObjMRItems.setAttribute("UNIT_ID",rsTmp2.getLong("UNIT"));
                    ObjMRItems.setAttribute("REMARKS",rsTmp2.getString("REMARKS"));
                    ObjMRItems.setAttribute("REQUIRED_DATE",rsTmp2.getString("REQUIRED_DATE"));
                    ObjMRItems.setAttribute("CREATED_BY",rsTmp2.getLong("CREATED_BY"));
                    ObjMRItems.setAttribute("CREATED_DATE",rsTmp2.getString("CREATED_DATE"));
                    ObjMRItems.setAttribute("MODIFIED_BY",rsTmp2.getLong("MODIFIED_BY"));
                    ObjMRItems.setAttribute("MODIFIED_DATE",rsTmp2.getString("MODIFIED_DATE"));
                    
                    ObjMaterialRequisition.colMRItems.put(Long.toString(Counter),ObjMRItems);
                }// Innser while
                
                List.put(Long.toString(Counter),ObjMaterialRequisition);
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
            String strSql = "SELECT * FROM D_INV_ISSUE_REQ_HEADER " + pCondition + "  AND REQ_TYPE = 2 ORDER BY REQ_NO" ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_INV_ISSUE_REQ_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND REQ_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND REQ_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' AND REQ_TYPE=2 ORDER BY REQ_NO";
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
        ResultSet rsTmp,rsLot;
        Connection tmpConn;
        Statement tmpStmt,stLot;
        
        tmpConn=data.getConn();
        
        //HashMap List=new HashMap();
        long Counter=0;
        int RevNo=0,SrNo=0,LotCounter=0;
        
        try {
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",rsResultSet.getInt("REVISION_NO"));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("REQ_NO",rsResultSet.getString("REQ_NO"));
            setAttribute("REQ_TYPE",rsResultSet.getInt("REQ_TYPE"));
            setAttribute("REQ_DATE",rsResultSet.getString("REQ_DATE"));
            setAttribute("STORE",rsResultSet.getString("STORE"));
            setAttribute("SOURCE_DEPT_ID",rsResultSet.getInt("SOURCE_DEPT_ID"));
            setAttribute("DEST_DEPT_ID",rsResultSet.getInt("DEST_DEPT_ID"));
            setAttribute("PURPOSE",rsResultSet.getString("PURPOSE"));
            setAttribute("REQUIRED_DATE",rsResultSet.getString("REQUIRED_DATE"));
            setAttribute("BUYER",rsResultSet.getInt("BUYER"));
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsResultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE",rsResultSet.getString("REJECTED_DATE"));
            setAttribute("HIERARCHY_ID",rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("REMARKS",rsResultSet.getString("REMARKS"));
            setAttribute("CANCELED",rsResultSet.getInt("CANCELED"));
            setAttribute("STATUS",rsResultSet.getString("STATUS"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            
            //Now Populate the collection
            //first clear the collection
            colMRItems.clear();
            
            String mCompanyID=Long.toString( (long) getAttribute("COMPANY_ID").getVal());
            String mReqNo=(String) getAttribute("REQ_NO").getObj();
            
            tmpStmt=tmpConn.createStatement();
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_ISSUE_REQ_DETAIL_H WHERE COMPANY_ID=" + mCompanyID + " AND REQ_NO='" + mReqNo + "' AND REQ_TYPE=2 AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_ISSUE_REQ_DETAIL WHERE COMPANY_ID=" + mCompanyID + " AND REQ_NO='" + mReqNo + "' AND REQ_TYPE=2 ORDER BY SR_NO");
            }
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsIssueReqItem ObjMRItems = new clsIssueReqItem();
                
                SrNo=rsTmp.getInt("SR_NO"); //Sr. No. of Item Detail
                ObjMRItems.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));
                ObjMRItems.setAttribute("REQ_NO",rsTmp.getString("REQ_NO"));
                ObjMRItems.setAttribute("REQ_TYPE",rsTmp.getInt("REQ_TYPE"));
                ObjMRItems.setAttribute("SR_NO",rsTmp.getLong("SR_NO"));
                ObjMRItems.setAttribute("ITEM_CODE",rsTmp.getString("ITEM_CODE"));
                ObjMRItems.setAttribute("ITEM_EXTRA_DESC",rsTmp.getString("ITEM_EXTRA_DESC"));
                ObjMRItems.setAttribute("COST_CENTER_CODE",rsTmp.getInt("COST_CENTER_CODE"));
                ObjMRItems.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
                ObjMRItems.setAttribute("REQ_QTY",rsTmp.getFloat("REQ_QTY"));
                ObjMRItems.setAttribute("ISSUED_QTY",rsTmp.getFloat("ISSUED_QTY"));
                ObjMRItems.setAttribute("BAL_QTY",rsTmp.getFloat("BAL_QTY"));
                ObjMRItems.setAttribute("UNIT_ID",rsTmp.getLong("UNIT"));
                ObjMRItems.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                ObjMRItems.setAttribute("CANCELED",rsTmp.getInt("CANCELED"));
                ObjMRItems.setAttribute("REQUIRED_DATE",rsTmp.getString("REQUIRED_DATE"));
                ObjMRItems.setAttribute("CREATED_BY",rsTmp.getLong("CREATED_BY"));
                ObjMRItems.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjMRItems.setAttribute("MODIFIED_BY",rsTmp.getLong("MODIFIED_BY"));
                ObjMRItems.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                ObjMRItems.setAttribute("MFG_PROG_NO",rsTmp.getString("MFG_PROG_NO"));
                ObjMRItems.setAttribute("BLEND_CODE",rsTmp.getString("BLEND_CODE"));
                
                //== Insert Lot Nos. ==
                stLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                if(HistoryView) {
                    rsLot=stLot.executeQuery("SELECT * FROM D_INV_ISSUE_REQ_LOT_H WHERE COMPANY_ID="+mCompanyID+" AND REQ_NO='"+mReqNo+"' AND REQ_SR_NO="+SrNo+" AND REQ_TYPE=2 AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
                }
                else {
                    rsLot=stLot.executeQuery("SELECT * FROM D_INV_ISSUE_REQ_LOT WHERE COMPANY_ID="+mCompanyID+" AND REQ_NO='"+mReqNo+"' AND REQ_SR_NO="+SrNo+" AND REQ_TYPE=2 ORDER BY SR_NO");
                }
                rsLot.first();
                
                LotCounter=0;
                while((!rsLot.isAfterLast())&&(rsLot.getRow()>0)) {
                    LotCounter=LotCounter+1;
                    
                    clsIssueReqItemDetail ObjLot=new clsIssueReqItemDetail();
                    ObjLot.setAttribute("COMPANY_ID",rsLot.getInt("COMPANY_ID"));
                    ObjLot.setAttribute("ITEM_ID",rsLot.getString("ITEM_ID"));
                    ObjLot.setAttribute("REQ_NO",rsLot.getString("REQ_NO"));
                    ObjLot.setAttribute("REQ_SR_NO",rsLot.getInt("REQ_SR_NO"));
                    ObjLot.setAttribute("REQ_TYPE",rsLot.getInt("REQ_TYPE"));
                    ObjLot.setAttribute("SR_NO",rsLot.getInt("SR_NO"));
                    ObjLot.setAttribute("ITEM_LOT_NO",rsLot.getString("ITEM_LOT_NO"));
                    ObjLot.setAttribute("AUTO_LOT_NO",rsLot.getString("AUTO_LOT_NO"));
                    ObjLot.setAttribute("ISSUED_LOT_QTY",rsLot.getDouble("ISSUED_LOT_QTY"));
                    
                    ObjMRItems.colIssueReqLot.put(Integer.toString(LotCounter),ObjLot);
                    rsLot.next();
                }
                
                colMRItems.put(Long.toString(Counter),ObjMRItems);
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
                strSQL="SELECT D_INV_ISSUE_REQ_HEADER.REQ_NO,D_INV_ISSUE_REQ_HEADER.REQ_DATE,RECEIVED_DATE,D_INV_ISSUE_REQ_HEADER.DEST_DEPT_ID AS DEPT_ID FROM D_INV_ISSUE_REQ_HEADER,D_COM_DOC_DATA WHERE D_INV_ISSUE_REQ_HEADER.REQ_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_ISSUE_REQ_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_ISSUE_REQ_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_INV_ISSUE_REQ_HEADER.REQ_TYPE=2 AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID="+clsIssueRequisitionRaw.ModuleID+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT D_INV_ISSUE_REQ_HEADER.REQ_NO,D_INV_ISSUE_REQ_HEADER.REQ_DATE,RECEIVED_DATE,D_INV_ISSUE_REQ_HEADER.DEST_DEPT_ID AS DEPT_ID FROM D_INV_ISSUE_REQ_HEADER,D_COM_DOC_DATA WHERE D_INV_ISSUE_REQ_HEADER.REQ_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_ISSUE_REQ_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_ISSUE_REQ_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_INV_ISSUE_REQ_HEADER.REQ_TYPE=2 AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID="+clsIssueRequisitionRaw.ModuleID+" ORDER BY D_INV_ISSUE_REQ_HEADER.REQ_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT D_INV_ISSUE_REQ_HEADER.REQ_NO,D_INV_ISSUE_REQ_HEADER.REQ_DATE,RECEIVED_DATE,D_INV_ISSUE_REQ_HEADER.DEST_DEPT_ID AS DEPT_ID FROM D_INV_ISSUE_REQ_HEADER,D_COM_DOC_DATA WHERE D_INV_ISSUE_REQ_HEADER.REQ_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_ISSUE_REQ_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_ISSUE_REQ_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_INV_ISSUE_REQ_HEADER.REQ_TYPE=2 AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID="+clsIssueRequisitionRaw.ModuleID+" ORDER BY D_INV_ISSUE_REQ_HEADER.REQ_NO";
            }
            
            //strSQL="SELECT D_INV_REQ_HEADER.REQ_NO,D_INV_REQ_HEADER.REQ_DATE FROM D_INV_REQ_HEADER,D_COM_DOC_DATA WHERE D_INV_REQ_HEADER.REQ_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_REQ_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_REQ_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=2";
            rsTmp3=tmpStmt3.executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp3.next()) {
                if(EITLERPGLOBAL.isWithinDate(rsTmp3.getString("REQ_DATE"),FinYearFrom)) {
                    Counter=Counter+1;
                    clsIssueRequisitionRaw ObjDoc=new clsIssueRequisitionRaw();
                    
                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("REQ_NO",rsTmp3.getString("REQ_NO"));
                    ObjDoc.setAttribute("REQ_DATE",rsTmp3.getString("REQ_DATE"));
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
    
    public static HashMap getMRItemList(int pCompanyID,String pMRNo,boolean pAllData,int pType) {
        ResultSet rsTmp=null;
        Connection tmpConn=null;
        Statement tmpStmt=null;
        
        ResultSet rsTmp2=null,rsLot=null;
        Statement tmpStmt2=null,stLot=null;
        int Counter1 = 0,Counter2=0;
        
        HashMap List=new HashMap();
        long Counter=0;
        String strSQL;
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            if(pAllData) {
                strSQL = "SELECT * FROM D_INV_ISSUE_REQ_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND REQ_NO='"+pMRNo.trim()+"' AND REQ_TYPE=2 AND REQ_NO IN (SELECT REQ_NO FROM D_INV_ISSUE_REQ_HEADER WHERE REQ_NO='"+pMRNo+"' AND APPROVED=1) ORDER BY ITEM_CODE";
            }
            else{
                
                
                //strSQL = "SELECT * FROM D_INV_ISSUE_REQ_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND REQ_NO='"+pMRNo.trim()+"' AND ISSUED_QTY<REQ_QTY AND REQ_NO IN (SELECT REQ_NO FROM D_INV_ISSUE_REQ_HEADER WHERE REQ_NO='"+pMRNo+"' AND APPROVED=1) ORDER BY ITEM_CODE";
                
                strSQL="SELECT B.* ";
                strSQL+="FROM ";
                strSQL+="D_INV_ISSUE_REQ_HEADER A, ";
                strSQL+="D_INV_ISSUE_REQ_DETAIL B ";
                strSQL+="LEFT JOIN D_INV_ISSUE_DETAIL I ON (I.REQ_NO=B.REQ_NO AND I.REQ_SRNO=B.SR_NO AND I.ISSUE_NO IN (SELECT ISSUE_NO FROM D_INV_ISSUE_HEADER WHERE ISSUE_NO=I.ISSUE_NO AND APPROVED=1 AND CANCELED=0)), ";
                strSQL+="D_COM_DEPT_MASTER D ";
                strSQL+="WHERE A.COMPANY_ID=B.COMPANY_ID AND ";
                strSQL+="A.REQ_NO=B.REQ_NO AND ";
                strSQL+="A.REQ_TYPE=B.REQ_TYPE AND ";
                strSQL+="A.COMPANY_ID="+pCompanyID+" AND ";
                strSQL+="A.APPROVED=1 AND A.CANCELED=0 AND ";
                strSQL+="B.COMPANY_ID=D.COMPANY_ID AND A.DEST_DEPT_ID=D.DEPT_ID AND ";
                strSQL+="A.REQ_TYPE="+pType+" AND A.REQ_NO='"+pMRNo+"' ";
                strSQL+="GROUP BY B.REQ_NO,B.REQ_TYPE,B.SR_NO ";
                strSQL+="HAVING IF(SUM(I.QTY) IS NULL,0,SUM(I.QTY))<B.REQ_QTY ";
                
            }
            
            rsTmp2=tmpStmt.executeQuery(strSQL);
            rsTmp2.first();
            
            Counter1=0;
            while(! rsTmp2.isAfterLast()) {
                Counter1++;
                clsIssueReqItem ObjMRItems=new clsIssueReqItem();
                
                int IssueReqSrNo=rsTmp2.getInt("SR_NO");
                
                ObjMRItems.setAttribute("COMPANY_ID",rsTmp2.getInt("COMPANY_ID"));
                ObjMRItems.setAttribute("REQ_NO",rsTmp2.getString("REQ_NO"));
                ObjMRItems.setAttribute("REQ_TYPE",rsTmp2.getInt("REQ_TYPE"));
                ObjMRItems.setAttribute("SR_NO",rsTmp2.getLong("SR_NO"));
                ObjMRItems.setAttribute("ITEM_CODE",rsTmp2.getString("ITEM_CODE"));
                ObjMRItems.setAttribute("ITEM_EXTRA_DESC",rsTmp2.getString("ITEM_EXTRA_DESC"));
                ObjMRItems.setAttribute("REQ_QTY",rsTmp2.getFloat("REQ_QTY"));
                
                double IssuedQty=data.getDoubleValueFromDB("SELECT SUM(QTY) AS TOTAL_QTY FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.ISSUE_NO=B.ISSUE_NO AND A.APPROVED=1 AND A.CANCELED=0 AND A.REQ_NO='"+pMRNo+"' AND A.REQ_SRNO="+rsTmp2.getLong("SR_NO"));
                
                ObjMRItems.setAttribute("ISSUED_QTY",IssuedQty);
                ObjMRItems.setAttribute("BAL_QTY",rsTmp2.getDouble("REQ_QTY")-IssuedQty);
                ObjMRItems.setAttribute("UNIT_ID",rsTmp2.getLong("UNIT"));
                ObjMRItems.setAttribute("REMARKS",rsTmp2.getString("REMARKS"));
                ObjMRItems.setAttribute("MFG_PROG_NO",rsTmp2.getString("MFG_PROG_NO"));
                ObjMRItems.setAttribute("BLEND_CODE",rsTmp2.getString("BLEND_CODE"));
                ObjMRItems.setAttribute("REQUIRED_DATE",rsTmp2.getString("REQUIRED_DATE"));
                ObjMRItems.setAttribute("CANCELED",rsTmp2.getInt("CANCELED"));
                ObjMRItems.setAttribute("CREATED_BY",rsTmp2.getLong("CREATED_BY"));
                ObjMRItems.setAttribute("CREATED_DATE",rsTmp2.getString("CREATED_DATE"));
                ObjMRItems.setAttribute("MODIFIED_BY",rsTmp2.getLong("MODIFIED_BY"));
                ObjMRItems.setAttribute("MODIFIED_DATE",rsTmp2.getString("MODIFIED_DATE"));
                
                //Now Fetch Lots.
                stLot=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                rsLot=stLot.executeQuery("SELECT * FROM D_INV_ISSUE_REQ_LOT WHERE COMPANY_ID="+pCompanyID+" AND REQ_NO='"+pMRNo+"' AND REQ_SR_NO="+IssueReqSrNo+" AND REQ_TYPE="+pType);
                rsLot.first();
                
                //Clear existing data
                ObjMRItems.colIssueReqLot.clear();
                
                if(rsLot.getRow()>0) {
                    Counter2=0;
                    while(!rsLot.isAfterLast()) {
                        Counter2++;
                        clsIssueReqItemDetail ObjLot=new clsIssueReqItemDetail();
                        
                        ObjLot.setAttribute("COMPANY_ID",rsLot.getInt("COMPANY_ID"));
                        ObjLot.setAttribute("REQ_NO",rsLot.getString("REQ_NO"));
                        ObjLot.setAttribute("REQ_SR_NO",rsLot.getInt("REQ_SR_NO"));
                        ObjLot.setAttribute("SR_NO",rsLot.getInt("SR_NO"));
                        ObjLot.setAttribute("ITEM_ID",rsLot.getString("ITEM_ID"));
                        ObjLot.setAttribute("ITEM_LOT_NO",rsLot.getString("ITEM_LOT_NO"));
                        ObjLot.setAttribute("AUTO_LOT_NO",rsLot.getString("AUTO_LOT_NO"));
                        ObjLot.setAttribute("ISSUED_LOT_QTY",rsLot.getDouble("ISSUED_LOT_QTY"));
                        
                        ObjMRItems.colIssueReqLot.put(Integer.toString(Counter2),ObjLot);
                        rsLot.next();
                    }
                }
                
                List.put(Integer.toString(Counter1),ObjMRItems);
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
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_INV_ISSUED_REQ_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND REQ_NO='"+pDocNo+"' AND REQ_TYPE=2");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_ISSUE_REQ_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND REQ_NO='"+pDocNo+"' AND REQ_TYPE=2");
            
            while(rsTmp.next()) {
                clsIssueRequisitionRaw ObjMR=new clsIssueRequisitionRaw();
                
                ObjMR.setAttribute("REQ_NO",rsTmp.getString("REQ_NO"));
                ObjMR.setAttribute("REQ_DATE",rsTmp.getString("REQ_DATE"));
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
            rsTmp=data.getResult("SELECT REQ_NO,APPROVED,CANCELED FROM D_INV_ISSUE_REQ_HEADER WHERE COMPANY_ID="+pCompanyID+" AND REQ_NO='"+pDocNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                if(rsTmp.getBoolean("APPROVED")) {
                    if(rsTmp.getBoolean("CANCELED")) {
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
            rsTmp=data.getResult("SELECT REQ_NO FROM D_INV_ISSUE_REQ_HEADER WHERE COMPANY_ID="+pCompanyID+" AND REQ_NO='"+pDocNo+"' AND CANCELED=0 AND REQ_TYPE=2 ");
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
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_INV_ISSUE_REQ_HEADER WHERE COMPANY_ID="+pCompanyID+" AND REQ_NO='"+pDocNo+"' AND REQ_TYPE=2");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedDoc) {
                    
                }
                else {
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND MODULE_ID="+(clsIssueRequisitionRaw.ModuleID));
                }
                
                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_INV_ISSUE_REQ_HEADER SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND REQ_NO='"+pDocNo+"' AND REQ_TYPE=2");
                
                // below line is commented by VIVEK because lot not updated [cause is wrong query( CANCELED is replaced by CANCELLED )] on 22/02/2013.
                //data.Execute("UPDATE D_INV_ISSUE_REQ_LOT SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND REQ_NO='"+pDocNo+"' AND REQ_TYPE=2");
                
                //new line by VIVEK to correct wrong query( CANCELED is upadted to CANCELLED ) on 22/02/2013.
                data.Execute("UPDATE D_INV_ISSUE_REQ_LOT SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND REQ_NO='"+pDocNo+"' AND REQ_TYPE=2");
                
                Cancelled=true;
            }
            
            rsTmp.close();
            rsIndent.close();
        }
        catch(Exception e) {
            
        }
        
        return Cancelled;
    }
    
    
}
