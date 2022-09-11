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
import EITLERP.Purchase.*;
import EITLERP.Stores.*;

/**
 *
 * @author  jadave
 * @version
 */

public class clsCrAdjustment {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    //Requisition Material Collection
    public HashMap colItems;
    
    //History Related properties
    private boolean HistoryView=false;
    
    public static int ModuleID=61;
    
    
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
    public clsCrAdjustment() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(0));
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVED",new Variant(0));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("REJECTED", new Variant(""));
        props.put("REJECTED_DATE",new Variant(""));
        props.put("REJECTED_REMARKS",new Variant(""));
        
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("PREFIX",new Variant("")); //For Autonumber generation
        
        //Create a new object for MR Item collection
        colItems=new HashMap();
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
    }
    
    /*<b>Load Data</b>\nThis method loads data from database to Business Object*/
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FIN_CR_ADJ_HEADER WHERE COMPANY_ID="+pCompanyID+" AND DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY DOC_NO");
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
        
        try {
            
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date ReqDate=java.sql.Date.valueOf(EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            
            if((ReqDate.after(FinFromDate)||ReqDate.compareTo(FinFromDate)==0)&&(ReqDate.before(FinToDate)||ReqDate.compareTo(FinToDate)==0)) {
                //Within the year
            }
            else {
                LastError="Voucher date is not within financial year.";
                return false;
            }
            //=====================================================//
            
            if(!Validate()) {
                return false;
            }
            
            //data.SetAutoCommit(false);
            //Conn.setAutoCommit(false);
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FIN_CR_ADJ_HEADER_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FIN_CR_ADJ_DETAIL_H WHERE DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //--------- Generate Gatepass requisition no.  ------------
            setAttribute("DOC_NO",clsFirstFree.getNextFreeNo(getAttribute("COMPANY_ID").getInt(),clsCrAdjustment.ModuleID, getAttribute("FFNO").getInt(),true));
            //-------------------------------------------------
            
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getLong());
            rsResultSet.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsResultSet.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateLong("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getLong());
            rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID );
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
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
            rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID );
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            rsHistory.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getLong());
            rsHistory.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsHistory.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsHistory.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateLong("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getLong());
            rsHistory.updateString("CREATED_BY", getAttribute("CREATED_BY").getString());
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_BY").getString()));
            rsHistory.updateString("MODIFIED_BY", getAttribute("MODIFIED_BY").getString());
            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            ResultSet rsTmp,rsTmpEx;
            Statement tmpStmt,tmpStmtEx;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FIN_CR_ADJ_DETAIL WHERE DOC_NO='1'");
            
            int CompanyID=getAttribute("COMPANY_ID").getInt();
            
            //Now Insert records into detail table
            for(int i=1;i<=colItems.size();i++) {
                clsCrAdjustmentItem objItem=(clsCrAdjustmentItem) colItems.get(Integer.toString(i));
                
                rsTmp.moveToInsertRow();
                rsTmp.updateInt("COMPANY_ID",CompanyID);
                rsTmp.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsTmp.updateInt("SR_NO",i);
                rsTmp.updateString("VOUCHER_NO",objItem.getAttribute("VOUCHER_NO").getString());
                rsTmp.updateString("VOUCHER_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("VOUCHER_DATE").getString()));
                rsTmp.updateString("MAIN_ACCOUNT_CODE",objItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsTmp.updateString("SUB_ACCOUNT_CODE",objItem.getAttribute("SUB_ACCOUNT_CODE").getString());
                
                rsTmp.updateString("PO_NO",objItem.getAttribute("PO_NO").getString());
                rsTmp.updateString("PO_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("PO_DATE").getString()));
                rsTmp.updateString("GRN_NO",objItem.getAttribute("GRN_NO").getString());
                rsTmp.updateString("GRN_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("GRN_DATE").getString()));
                rsTmp.updateString("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                rsTmp.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("INVOICE_DATE").getString()));
                rsTmp.updateString("REMARKS",objItem.getAttribute("REMARKS").getString());
                rsTmp.updateString("CREATED_BY", getAttribute("CREATED_BY").getString());
                rsTmp.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_BY").getString()));
                rsTmp.updateString("MODIFIED_BY", getAttribute("MODIFIED_BY").getString());
                rsTmp.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateInt("CANCELLED",0);
                rsTmp.updateInt("VOUCHER_SR_NO",objItem.getAttribute("VOUCHER_SR_NO").getInt());
                rsTmp.updateDouble("PAYMENT_AMOUNT",objItem.getAttribute("PAYMENT_AMOUNT").getDouble());
                rsTmp.updateDouble("ADJUST_AMOUNT",objItem.getAttribute("ADJUST_AMOUNT").getDouble());
                rsTmp.updateInt("REF_MODULE_ID",objItem.getAttribute("REF_MODULE_ID").getInt());
                rsTmp.updateString("PJV_NO",objItem.getAttribute("PJV_NO").getString());
                rsTmp.updateString("PJV_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("PJV_DATE").getString()));
                rsTmp.updateInt("VOUCHER_COMPANY_ID",objItem.getAttribute("VOUCHER_COMPANY_ID").getInt());
                rsTmp.updateInt("PJV_COMPANY_ID",objItem.getAttribute("PJV_COMPANY_ID").getInt());
                rsTmp.updateInt("SELECTION_TYPE",objItem.getAttribute("SELECTION_TYPE").getInt());
                rsTmp.insertRow();
                
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateInt("COMPANY_ID",CompanyID);
                rsHDetail.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsHDetail.updateInt("SR_NO",i);
                rsHDetail.updateString("VOUCHER_NO",objItem.getAttribute("VOUCHER_NO").getString());
                rsHDetail.updateString("VOUCHER_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("VOUCHER_DATE").getString()));
                rsHDetail.updateString("MAIN_ACCOUNT_CODE",objItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsHDetail.updateString("SUB_ACCOUNT_CODE",objItem.getAttribute("SUB_ACCOUNT_CODE").getString());
                rsHDetail.updateString("PO_NO",objItem.getAttribute("PO_NO").getString());
                rsHDetail.updateString("PO_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("PO_DATE").getString()));
                rsHDetail.updateString("GRN_NO",objItem.getAttribute("GRN_NO").getString());
                rsHDetail.updateString("GRN_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("GRN_DATE").getString()));
                rsHDetail.updateString("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                rsHDetail.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("INVOICE_DATE").getString()));
                rsHDetail.updateString("REMARKS",objItem.getAttribute("REMARKS").getString());
                rsHDetail.updateString("CREATED_BY", getAttribute("CREATED_BY").getString());
                rsHDetail.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_BY").getString()));
                rsHDetail.updateString("MODIFIED_BY", getAttribute("MODIFIED_BY").getString());
                rsHDetail.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateInt("CANCELLED",0);
                rsHDetail.updateInt("VOUCHER_SR_NO",objItem.getAttribute("VOUCHER_SR_NO").getInt());
                rsHDetail.updateDouble("PAYMENT_AMOUNT",objItem.getAttribute("PAYMENT_AMOUNT").getDouble());
                rsHDetail.updateDouble("ADJUST_AMOUNT",objItem.getAttribute("ADJUST_AMOUNT").getDouble());
                rsHDetail.updateInt("REF_MODULE_ID",objItem.getAttribute("REF_MODULE_ID").getInt());
                rsHDetail.updateString("PJV_NO",objItem.getAttribute("PJV_NO").getString());
                rsHDetail.updateString("PJV_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("PJV_DATE").getString()));
                rsHDetail.updateInt("VOUCHER_COMPANY_ID",objItem.getAttribute("VOUCHER_COMPANY_ID").getInt());
                rsHDetail.updateInt("PJV_COMPANY_ID",objItem.getAttribute("PJV_COMPANY_ID").getInt());
                rsHDetail.updateInt("SELECTION_TYPE",objItem.getAttribute("SELECTION_TYPE").getInt());
                rsHDetail.insertRow();
                
            }
            
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=getAttribute("COMPANY_ID").getInt();
            ObjFlow.ModuleID=ModuleID; //Material Requisition
            ObjFlow.DocNo=getAttribute("DOC_NO").getString();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_FIN_CR_ADJ_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName="DOC_NO";
            ObjFlow.UseSpecifiedURL=true;
            ObjFlow.SpecificURL=FinanceGlobal.FinURL;
            ObjFlow.DocDate=EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString());
            
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
            ObjFlow.UseSpecifiedURL=false;
            //--------- Approval Flow Update complete -----------
            
            
            //======Now Adjust the Vouchers========//
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
            if(AStatus.equals("F")) {//On Final Approval
                AdjustAdvancePayment();
            }
            //=====================================//
            
            MoveLast();
            return true;
        } catch(Exception e) {
            try {
                e.printStackTrace();
            } catch(Exception c) {
            }
            LastError= e.getMessage();
            return false;
        }
    }
    
    //Updates current record
    public boolean Update() {
        Statement stHistory,stHDetail,stHeader;
        ResultSet rsHistory,rsHDetail,rsHeader;
        boolean Validate=true;
        
        
        try {
            
            if(!Validate()) {
                return false;
            }
            
            String AStatus=getAttribute("APPROVAL_STATUS").getString();
            
            if(AStatus.equals("R")) {
                Validate=false;
            }
            
            Validate=true;
            
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date ReqDate=java.sql.Date.valueOf( EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            
            if((ReqDate.after(FinFromDate)||ReqDate.compareTo(FinFromDate)==0)&&(ReqDate.before(FinToDate)||ReqDate.compareTo(FinToDate)==0)) {
                //Withing the year
            }
            else {
                LastError="Voucher date is not within financial year.";
                return false;
            }
            //=====================================================//
            
            
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FIN_CR_ADJ_HEADER_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FIN_CR_ADJ_DETAIL_H WHERE DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            String theDocNo=(String)getAttribute("DOC_NO").getObj();
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            rsResultSet.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_BY").getString()));
            rsResultSet.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateRow();
            
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM D_FIN_CR_ADJ_HEADER_H WHERE COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+" AND DOC_NO='"+getAttribute("DOC_NO").getString()+"'",FinanceGlobal.FinURL);
            RevNo++;
            String RevDocNo=getAttribute("DOC_NO").getString();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getLong());
            rsHistory.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsHistory.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsHistory.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateString("CREATED_BY", getAttribute("CREATED_BY").getString());
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_BY").getString()));
            rsHistory.updateString("MODIFIED_BY", getAttribute("MODIFIED_BY").getString());
            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            data.Execute("DELETE FROM D_FIN_CR_ADJ_DETAIL WHERE COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+" AND DOC_NO='"+theDocNo+"'",FinanceGlobal.FinURL);
            
            ResultSet rsTmp,rsTmpEx;
            Statement tmpStmt,tmpStmtEx;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FIN_CR_ADJ_DETAIL WHERE DOC_NO='1'");
            
            
            int Counter=0;
            
            //Now Insert records into detail table
            for(int i=1;i<=colItems.size();i++) {
                clsCrAdjustmentItem objItem=(clsCrAdjustmentItem) colItems.get(Integer.toString(i));
                
                Counter++;
                
                rsTmp.moveToInsertRow();
                rsTmp.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                rsTmp.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsTmp.updateInt("SR_NO",i);
                rsTmp.updateString("VOUCHER_NO",objItem.getAttribute("VOUCHER_NO").getString());
                rsTmp.updateString("VOUCHER_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("VOUCHER_DATE").getString()));
                rsTmp.updateString("MAIN_ACCOUNT_CODE",objItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsTmp.updateString("SUB_ACCOUNT_CODE",objItem.getAttribute("SUB_ACCOUNT_CODE").getString());
                rsTmp.updateString("PO_NO",objItem.getAttribute("PO_NO").getString());
                rsTmp.updateString("PO_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("PO_DATE").getString()));
                rsTmp.updateString("GRN_NO",objItem.getAttribute("GRN_NO").getString());
                rsTmp.updateString("GRN_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("GRN_DATE").getString()));
                rsTmp.updateString("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                rsTmp.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("INVOICE_DATE").getString()));
                rsTmp.updateString("REMARKS",objItem.getAttribute("REMARKS").getString());
                rsTmp.updateString("CREATED_BY", getAttribute("CREATED_BY").getString());
                rsTmp.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_BY").getString()));
                rsTmp.updateString("MODIFIED_BY", getAttribute("MODIFIED_BY").getString());
                rsTmp.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateInt("CANCELLED",0);
                rsTmp.updateInt("VOUCHER_SR_NO",objItem.getAttribute("VOUCHER_SR_NO").getInt());
                rsTmp.updateDouble("PAYMENT_AMOUNT",objItem.getAttribute("PAYMENT_AMOUNT").getDouble());
                rsTmp.updateDouble("ADJUST_AMOUNT",objItem.getAttribute("ADJUST_AMOUNT").getDouble());
                rsTmp.updateInt("REF_MODULE_ID",objItem.getAttribute("REF_MODULE_ID").getInt());
                rsTmp.updateString("PJV_NO",objItem.getAttribute("PJV_NO").getString());
                rsTmp.updateString("PJV_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("PJV_DATE").getString()));
                rsTmp.updateInt("VOUCHER_COMPANY_ID",objItem.getAttribute("VOUCHER_COMPANY_ID").getInt());
                rsTmp.updateInt("PJV_COMPANY_ID",objItem.getAttribute("PJV_COMPANY_ID").getInt());
                rsTmp.updateInt("SELECTION_TYPE",objItem.getAttribute("SELECTION_TYPE").getInt());
                rsTmp.insertRow();
                
                Counter++;
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                rsHDetail.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsHDetail.updateInt("SR_NO",i);
                rsHDetail.updateString("VOUCHER_NO",objItem.getAttribute("VOUCHER_NO").getString());
                rsHDetail.updateString("VOUCHER_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("VOUCHER_DATE").getString()));
                rsHDetail.updateString("MAIN_ACCOUNT_CODE",objItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsHDetail.updateString("SUB_ACCOUNT_CODE",objItem.getAttribute("SUB_ACCOUNT_CODE").getString());
                rsHDetail.updateString("PO_NO",objItem.getAttribute("PO_NO").getString());
                rsHDetail.updateString("PO_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("PO_DATE").getString()));
                rsHDetail.updateString("GRN_NO",objItem.getAttribute("GRN_NO").getString());
                rsHDetail.updateString("GRN_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("GRN_DATE").getString()));
                rsHDetail.updateString("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                rsHDetail.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("INVOICE_DATE").getString()));
                rsHDetail.updateString("REMARKS",objItem.getAttribute("REMARKS").getString());
                rsHDetail.updateString("CREATED_BY", getAttribute("CREATED_BY").getString());
                rsHDetail.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_BY").getString()));
                rsHDetail.updateString("MODIFIED_BY", getAttribute("MODIFIED_BY").getString());
                rsHDetail.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateInt("CANCELLED",0);
                rsHDetail.updateInt("VOUCHER_SR_NO",objItem.getAttribute("VOUCHER_SR_NO").getInt());
                rsHDetail.updateDouble("PAYMENT_AMOUNT",objItem.getAttribute("PAYMENT_AMOUNT").getDouble());
                rsHDetail.updateDouble("ADJUST_AMOUNT",objItem.getAttribute("ADJUST_AMOUNT").getDouble());
                rsHDetail.updateInt("REF_MODULE_ID",objItem.getAttribute("REF_MODULE_ID").getInt());
                rsHDetail.updateString("PJV_NO",objItem.getAttribute("PJV_NO").getString());
                rsHDetail.updateString("PJV_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("PJV_DATE").getString()));
                rsHDetail.updateInt("VOUCHER_COMPANY_ID",objItem.getAttribute("VOUCHER_COMPANY_ID").getInt());
                rsHDetail.updateInt("PJV_COMPANY_ID",objItem.getAttribute("PJV_COMPANY_ID").getInt());
                rsHDetail.updateInt("SELECTION_TYPE",objItem.getAttribute("SELECTION_TYPE").getInt());
                rsHDetail.insertRow();
                
            }
            
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=getAttribute("COMPANY_ID").getInt();
            ObjFlow.ModuleID=ModuleID; //Gatepass Requisition
            ObjFlow.DocNo=getAttribute("DOC_NO").getString();
            ObjFlow.From=getAttribute("FROM").getInt();
            ObjFlow.To=getAttribute("TO").getInt();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_FIN_CR_ADJ_HEADER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=getAttribute("HIERARCHY_ID").getInt();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName="DOC_NO";
            ObjFlow.UseSpecifiedURL=true;
            ObjFlow.SpecificURL=FinanceGlobal.FinURL;
            //==== Handling Rejected Documents ==========//
            
            
            if(AStatus.equals("R")) {
                ObjFlow.To=getAttribute("SEND_DOC_TO").getInt();
                ObjFlow.ExplicitSendTo=true;
            }
            //==========================================//
            
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_FIN_CR_ADJ_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+" AND DOC_NO='"+ObjFlow.DocNo+"'",FinanceGlobal.FinURL);
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+ObjFlow.DocNo+"'");
                
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
            ObjFlow.UseSpecifiedURL=false;
            //--------- Approval Flow Update complete -----------
            
            
            //======Now Adjust the Vouchers========//
            AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
            if(AStatus.equals("F")) //On Final Approval
            {
                AdjustAdvancePayment();
            }
            MoveLast();
            //=====================================//
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FIN_CR_ADJ_HEADER WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+pDocNo+"' AND USER_ID="+pUserID+" AND TYPE='C' AND STATUS='W'";
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FIN_CR_ADJ_HEADER WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+pDocNo+"' AND USER_ID="+pUserID+" AND STATUS='W'";
                rsTmp=data.getResult(strSQL);
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
            e.printStackTrace();
            return false;
        }
    }
    
    
    //Deletes current record
    public boolean Delete(int pUserID) {
        try {
            int CompanyID=getAttribute("COMPANY_ID").getInt();
            String lDocNo=getAttribute("DOC_NO").getString();
            String strSQL="";
            
            //First check that record is editable
            if(CanDelete(CompanyID,lDocNo,pUserID)) {
                String strQry = "DELETE FROM D_FIN_CR_ADJ_HEADER WHERE COMPANY_ID=" + CompanyID +" AND DOC_NO='"+lDocNo+"'";
                data.Execute(strQry,FinanceGlobal.FinURL);
                strQry = "DELETE FROM D_FIN_CR_ADJ_DETAIL WHERE COMPANY_ID=" + CompanyID +" AND DOC_NO='"+lDocNo+"'";
                data.Execute(strQry,FinanceGlobal.FinURL);
                
                LoadData(CompanyID);
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
    
    
    public Object getObject(int CompanyID, String VoucherNo) {
        String strCondition = " WHERE COMPANY_ID=" + CompanyID + " AND DOC_NO='" + VoucherNo + "'" ;
        clsVoucher objVoucher = new clsVoucher();
        objVoucher.Filter(strCondition,CompanyID);
        return objVoucher;
    }
    
    
    
    
    public boolean Filter(String Condition,int CompanyID) {
        Ready=false;
        try {
            
            String strSql = "SELECT * FROM D_FIN_CR_ADJ_HEADER " + Condition ;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                //strSql = "SELECT * FROM D_FIN_CR_ADJ_HEADER WHERE COMPANY_ID="+CompanyID+" AND DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY DOC_NO";
                strSql = "SELECT * FROM D_FIN_CR_ADJ_HEADER " + Condition + " ORDER BY DOC_NO";
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
    
    public boolean FilterEx(String Condition,int CompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_FIN_CR_ADJ_HEADER " + Condition ;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_FIN_CR_ADJ_HEADER WHERE COMPANY_ID="+CompanyID+" AND DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY DOC_NO";
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
        
        tmpConn=data.getConn(FinanceGlobal.FinURL);
        
        long Counter=0;
        int RevNo=0;
        
        try {
            
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",UtilFunctions.getInt(rsResultSet,"REVISION_NO",0));
            } else {
                setAttribute("REVISION_NO",0);
            }
            setAttribute("COMPANY_ID",UtilFunctions.getInt(rsResultSet,"COMPANY_ID",0));
            setAttribute("DOC_NO",UtilFunctions.getString(rsResultSet,"DOC_NO",""));
            setAttribute("DOC_DATE",UtilFunctions.getString(rsResultSet,"DOC_DATE","0000-00-00"));
            setAttribute("REMARKS",UtilFunctions.getString(rsResultSet,"REMARKS",""));
            setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsResultSet,"HIERARCHY_ID",0));
            setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            setAttribute("CREATED_DATE",UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00"));
            setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            setAttribute("MODIFIED_DATE",UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00"));
            setAttribute("CHANGED",UtilFunctions.getInt(rsResultSet,"CHANGED",0));
            setAttribute("CHANGED_DATE",UtilFunctions.getString(rsResultSet,"CHANGED_DATE","0000-00-00"));
            setAttribute("APPROVED",UtilFunctions.getInt(rsResultSet,"APPROVED",0));
            setAttribute("APPROVED_DATE",UtilFunctions.getString(rsResultSet,"APPROVED_DATE","0000-00-00"));
            setAttribute("REJECTED",UtilFunctions.getInt(rsResultSet,"REJECTED",0));
            setAttribute("REJECTED_DATE",UtilFunctions.getString(rsResultSet,"REJECTED_DATE","0000-00-00"));
            setAttribute("CANCELLED",UtilFunctions.getInt(rsResultSet,"CANCELLED",0));
            
            colItems.clear();
            
            int CompanyID=getAttribute("COMPANY_ID").getInt();
            String VoucherNo=getAttribute("DOC_NO").getString();
            
            tmpStmt=tmpConn.createStatement();
            
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FIN_CR_ADJ_DETAIL_H WHERE COMPANY_ID=" + CompanyID + " AND DOC_NO='" + VoucherNo + "' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FIN_CR_ADJ_DETAIL WHERE COMPANY_ID=" + CompanyID + " AND DOC_NO='" + VoucherNo + "' ORDER BY SR_NO");
            }
            
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                Counter++;
                clsCrAdjustmentItem objItem = new clsCrAdjustmentItem();
                
                objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                objItem.setAttribute("DOC_NO",UtilFunctions.getString(rsTmp,"DOC_NO",""));
                objItem.setAttribute("SR_NO",UtilFunctions.getInt(rsTmp,"SR_NO",0));
                objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                objItem.setAttribute("VOUCHER_DATE",UtilFunctions.getString(rsTmp,"VOUCHER_DATE","0000-00-00"));
                objItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE",""));
                objItem.setAttribute("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"SUB_ACCOUNT_CODE",""));
                objItem.setAttribute("PO_NO",UtilFunctions.getString(rsTmp,"PO_NO",""));
                objItem.setAttribute("PO_DATE",UtilFunctions.getString(rsTmp,"PO_DATE","0000-00-00"));
                objItem.setAttribute("GRN_NO",UtilFunctions.getString(rsTmp,"GRN_NO",""));
                objItem.setAttribute("GRN_DATE",UtilFunctions.getString(rsTmp,"GRN_DATE","0000-00-00"));
                objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                objItem.setAttribute("INVOICE_DATE",UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00"));
                objItem.setAttribute("REMARKS",UtilFunctions.getString(rsTmp,"REMARKS",""));
                objItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsTmp,"CREATED_BY",""));
                objItem.setAttribute("CREATED_DATE",UtilFunctions.getString(rsTmp,"CREATED_DATE","0000-00-00"));
                objItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsTmp,"MODIFIED_BY",""));
                objItem.setAttribute("MODIFIED_DATE",UtilFunctions.getString(rsTmp,"MODIFIED_DATE","0000-00-00"));
                objItem.setAttribute("CHANGED",true);
                objItem.setAttribute("CHANGED_DATE",UtilFunctions.getString(rsTmp,"CHANGED_DATE","0000-00-00"));
                objItem.setAttribute("CANCELLED",0);
                objItem.setAttribute("VOUCHER_SR_NO",UtilFunctions.getInt(rsTmp,"VOUCHER_SR_NO",0));
                objItem.setAttribute("PAYMENT_AMOUNT",UtilFunctions.getDouble(rsTmp,"PAYMENT_AMOUNT",0));
                objItem.setAttribute("ADJUST_AMOUNT",UtilFunctions.getDouble(rsTmp,"ADJUST_AMOUNT",0));
                objItem.setAttribute("REF_MODULE_ID",UtilFunctions.getInt(rsTmp,"REF_MODULE_ID",0));
                objItem.setAttribute("PJV_NO",UtilFunctions.getString(rsTmp,"PJV_NO",""));
                objItem.setAttribute("PJV_DATE",UtilFunctions.getString(rsTmp,"PJV_DATE","0000-00-00"));
                objItem.setAttribute("VOUCHER_COMPANY_ID",UtilFunctions.getInt(rsTmp,"VOUCHER_COMPANY_ID",0));
                objItem.setAttribute("PJV_COMPANY_ID",UtilFunctions.getInt(rsTmp,"PJV_COMPANY_ID",0));
                objItem.setAttribute("SELECTION_TYPE",UtilFunctions.getInt(rsTmp,"SELECTION_TYPE",0));
                colItems.put(Long.toString(Counter),objItem);
                rsTmp.next();
            }
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public static HashMap getPendingApprovals(int CompanyID,int UserID,int Order,int FinYearFrom) {
        Connection tmpConn;
        ResultSet rsTmp3=null;
        Statement tmpStmt3=null;
        String strSQL="";
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            
            tmpConn=data.getConn(FinanceGlobal.FinURL);
            tmpStmt3=tmpConn.createStatement();
            
            if(Order==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT FINANCE.D_FIN_CR_ADJ_HEADER.DOC_NO,FINANCE.D_FIN_CR_ADJ_HEADER.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FIN_CR_ADJ_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_CR_ADJ_HEADER.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FIN_CR_ADJ_HEADER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FIN_CR_ADJ_HEADER.COMPANY_ID="+CompanyID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT FINANCE.D_FIN_CR_ADJ_HEADER.DOC_NO,FINANCE.D_FIN_CR_ADJ_HEADER.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FIN_CR_ADJ_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_CR_ADJ_HEADER.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FIN_CR_ADJ_HEADER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FIN_CR_ADJ_HEADER.COMPANY_ID="+CompanyID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_FIN_CR_ADJ_HEADER.DOC_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT FINANCE.D_FIN_CR_ADJ_HEADER.DOC_NO,FINANCE.D_FIN_CR_ADJ_HEADER.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FIN_CR_ADJ_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_CR_ADJ_HEADER.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FIN_CR_ADJ_HEADER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FIN_CR_ADJ_HEADER.COMPANY_ID="+CompanyID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_FIN_CR_ADJ_HEADER.VOUCHER_NO";
            }
            
            rsTmp3=tmpStmt3.executeQuery(strSQL);
            //rsTmp3.first();
            Counter=0;
            while(rsTmp3.next()) {
                if(EITLERPGLOBAL.isWithinDate(rsTmp3.getString("DOC_DATE"),FinYearFrom)) {
                    Counter=Counter+1;
                    clsCrAdjustment ObjDoc=new clsCrAdjustment();
                    
                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("DOC_NO",rsTmp3.getString("DOC_NO"));
                    ObjDoc.setAttribute("DOC_DATE",rsTmp3.getString("DOC_DATE"));
                    ObjDoc.setAttribute("RECEIVED_DATE",rsTmp3.getString("RECEIVED_DATE"));
                    ObjDoc.setAttribute("DEPT_ID",0);
                    // ----------------- End of Header Fields ------------------------------------//
                    
                    List.put(Long.toString(Counter),ObjDoc);
                }
               // rsTmp3.next();
            }//end of while
            
            //tmpConn.close();
            rsTmp3.close();
            tmpStmt3.close();
            
        }
        catch(Exception e) {
        }
        
        return List;
    }
    
    
    public boolean ShowHistory(int CompanyID,String DocNo) {
        Ready=false;
        try {
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FIN_CR_ADJ_HEADER_H WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"'");
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
    
    
    public static HashMap getHistoryList(int CompanyID,String DocNo) {
        HashMap List=new HashMap();
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn(FinanceGlobal.FinURL);
            stTmp=tmpConn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_FIN_CR_ADJ_HEADER_H WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                
                while(!rsTmp.isAfterLast()) {
                    clsCrAdjustment objVoucher=new clsCrAdjustment();
                    
                    objVoucher.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                    objVoucher.setAttribute("DOC_DATE",EITLERPGLOBAL.formatDate(rsTmp.getString("DOC_DATE")));
                    objVoucher.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                    objVoucher.setAttribute("UPDATED_BY",rsTmp.getString("UPDATED_BY"));
                    objVoucher.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                    objVoucher.setAttribute("ENTRY_DATE",EITLERPGLOBAL.formatDate(rsTmp.getString("ENTRY_DATE")));
                    objVoucher.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                    
                    List.put(Integer.toString(List.size()+1),objVoucher);
                    
                    rsTmp.next();
                }
                
            }
            rsTmp.close();
            stTmp.close();
            //tmpConn.close();
            return List;
        } catch(Exception e) {
            return List;
        }
    }
    
    public static String getDocStatus(int CompanyID,String DocNo) {
        ResultSet rsTmp;
        String strMessage="";
        
        try {
            rsTmp=data.getResult("SELECT DOC_NO,APPROVED,CANCELLED FROM D_FIN_CR_ADJ_HEADER WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"'",FinanceGlobal.FinURL);
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
    
    
    public static boolean CanCancel(int CompanyID,String DocNo) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            rsTmp=data.getResult("SELECT DOC_NO FROM D_FIN_CR_ADJ_HEADER WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"' AND APPROVED=0 AND CANCELLED=0",FinanceGlobal.FinURL);
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
    
    
    
    
    public static boolean CancelDoc(int CompanyID,String DocNo) {
        
        ResultSet rsTmp=null,rsIndent=null;
        boolean Cancelled=false;
        
        try {
            if(CanCancel(CompanyID,DocNo)) {
                
                boolean ApprovedDoc=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_FIN_CR_ADJ_HEADER WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"'",FinanceGlobal.FinURL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedDoc) {
                    //Revert the Changes done
                    rsTmp=data.getResult("SELECT * FROM D_FIN_CR_ADJ_HEADER WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"'",FinanceGlobal.FinURL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        while(!rsTmp.isAfterLast()) {
                            String VoucherNo=UtilFunctions.getString(rsTmp,"VOUCHER_NO","");
                            
                            data.Execute("UPDATE D_FIN_VOUCHER_HEADER SET GRN_NO='',GRN_DATE='0000-00-00' WHERE VOUCHER_NO='"+VoucherNo+"'");
                            
                            rsTmp.next();
                        }
                    }
                    
                }
                else {
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"' AND MODULE_ID="+(ModuleID),clsFinYear.getDBURL(CompanyID,EITLERPGLOBAL.FinYearFrom));
                }
                
                data.Execute("UPDATE D_FIN_CR_ADJ_HEADER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"'",FinanceGlobal.FinURL);
                
                Cancelled=true;
            }
            
            rsTmp.close();
            rsIndent.close();
        }
        catch(Exception e) {
            
        }
        
        return Cancelled;
    }
    
    public boolean Validate() {
        /*int CompanyID=getAttribute("COMPANY_ID").getInt();
        for(int i=1;i<=colItems.size();i++) {
            clsCrAdjustmentItem objItem=(clsCrAdjustmentItem)colItems.get(Integer.toString(i));
            
            String VoucherNo=objItem.getAttribute("VOUCHER_NO").getString();
            String GRNNo=objItem.getAttribute("GRN_NO").getString();
            String PONo=objItem.getAttribute("PO_NO").getString();
            int theModuleID = objItem.getAttribute("MODULE_ID").getInt();
            int POType=clsPOGen.getPOType(CompanyID, PONo);
            int GRNType=clsGRN.getGRNType(EITLERPGLOBAL.gCompanyID, GRNNo);
            //Verify Document No.
            if(!PONo.trim().equals("")) {
                if(!clsPOGen.IsValidPONo(PONo,POType)) {
                    LastError="PO No. you entered is not valid. Please check and try again";
                    return false;
                }
            }
            
            if(theModuleID == clsGRN.ModuleID || theModuleID == clsGRNGen.ModuleID) {
                if(GRNNo.trim().equals("")) {
                    LastError="Please specify GRN No.";
                    return false;
                }
                
                if(!clsGRNGen.IsGRNExist(GRNNo,GRNType)) {
                    LastError="GRN No. you entered is not valid. Please check and try again";
                    return false;
                }
            }
            
            if(theModuleID == 48) {
                if(GRNNo.trim().equals("")) {
                    LastError="Please specify GRN No.";
                    return false;
                }
                
                if(!clsJobwork.IsJobExist(GRNNo,EITLERPGLOBAL.DatabaseURL)) {
                    LastError="GRN No. you entered is not valid. Please check and try again";
                    return false;
                }
            }
        }*/
        return true;
    }
    
    public boolean AdjustAdvancePayment() {
        try {
            int CompanyID=getAttribute("COMPANY_ID").getInt();
            
            for(int i=1;i<=colItems.size();i++) {
                clsCrAdjustmentItem objItem=(clsCrAdjustmentItem)colItems.get(Integer.toString(i));
                
                String PaymentVoucherNo=objItem.getAttribute("VOUCHER_NO").getString();
                int PaymentVoucherSrNo=objItem.getAttribute("VOUCHER_SR_NO").getInt();
                //double PaymentAmount=objItem.getAttribute("PAYMENT_AMOUNT").getDouble();
                String PJVNo=objItem.getAttribute("PJV_NO").getString();
                double AdjustAmount=objItem.getAttribute("ADJUST_AMOUNT").getDouble();
                int PaymentCompanyID=objItem.getAttribute("VOUCHER_COMPANY_ID").getInt();
                int PJVCompanyID=objItem.getAttribute("PJV_COMPANY_ID").getInt();
                
                String GRNNo=objItem.getAttribute("GRN_NO").getString();
                String GRNDate=objItem.getAttribute("GRN_DATE").getString();
                String PONo=objItem.getAttribute("PO_NO").getString();
                String PODate=objItem.getAttribute("PO_DATE").getString();
                String InvoiceNo=objItem.getAttribute("INVOICE_NO").getString();
                String InvoiceDate=objItem.getAttribute("INVOICE_DATE").getString();
                String MainCode = objItem.getAttribute("MAIN_ACCOUNT_CODE").getString();
                String SubCode = objItem.getAttribute("SUB_ACCOUNT_CODE").getString();
                
                //*** Fetch the Payment Voucher Object
                clsVoucher objPaymentVoucher=(clsVoucher)(new clsVoucher()).getObject(PaymentCompanyID,PaymentVoucherNo);
                
                
                while(EITLERPGLOBAL.round(AdjustAmount,2)>0) {
                    //Find Next Sr. No.
                    if(EITLERPGLOBAL.round(AdjustAmount,2)==0) {
                        break;
                    }
                    String strSQL="SELECT B.SR_NO FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B " +
                    "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND A.APPROVED=1 AND A.CANCELLED=0 " +
                    "AND B.INVOICE_NO='' AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' " +
                    "AND B.SUB_ACCOUNT_CODE='"+SubCode+"' AND B.EFFECT='D' AND B.GRN_NO='' " +
                    "AND A.VOUCHER_NO='"+PaymentVoucherNo+"' ORDER BY B.SR_NO";
                    PaymentVoucherSrNo = data.getIntValueFromDB(strSQL,FinanceGlobal.FinURL);
                    if(PaymentVoucherSrNo==0) {
                        break;
                    }
                    double PaymentAmount = data.getDoubleValueFromDB("SELECT AMOUNT FROM D_FIN_VOUCHER_DETAIL WHERE SR_NO="+PaymentVoucherSrNo+" AND VOUCHER_NO='"+PaymentVoucherNo+"' ", FinanceGlobal.FinURL);
                    if(PaymentAmount>AdjustAmount) {
                        int NewSrNo=data.getIntValueFromDB("SELECT COUNT(*) FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+PaymentCompanyID+" AND VOUCHER_NO='"+PaymentVoucherNo+"'",FinanceGlobal.FinURL)+1;
                        //Advance Payment amount is greater than PJV adjustment amount. Split the payment voucher line
                        //get the voucher line
                        for(int v=1;v<=objPaymentVoucher.colVoucherItems.size();v++) {
                            clsVoucherItem objVoucherItem=(clsVoucherItem)objPaymentVoucher.colVoucherItems.get(Integer.toString(v));
                            
                            if(objVoucherItem.getAttribute("MAIN_ACCOUNT_CODE").getString().equals(MainCode)&&objVoucherItem.getAttribute("SUB_ACCOUNT_CODE").getString().equals(SubCode)&&objVoucherItem.getAttribute("SR_NO").getInt()==PaymentVoucherSrNo) {
                                
                                //Split this line in two lines
                                objVoucherItem.setAttribute("AMOUNT",PaymentAmount-AdjustAmount);
                                objPaymentVoucher.colVoucherItems.put(Integer.toString(v),objVoucherItem);
                                
                                //Create a new voucher line
                                clsVoucherItem objNewItem=new clsVoucherItem();
                                objNewItem.setAttribute("COMPANY_ID",objVoucherItem.getAttribute("COMPANY_ID").getInt());
                                objNewItem.setAttribute("VOUCHER_NO",objVoucherItem.getAttribute("VOUCHER_NO").getString());
                                objNewItem.setAttribute("SR_NO",NewSrNo);
                                objNewItem.setAttribute("EFFECT",objVoucherItem.getAttribute("EFFECT").getString());
                                objNewItem.setAttribute("ACCOUNT_ID",objVoucherItem.getAttribute("ACCOUNT_ID").getInt());
                                objNewItem.setAttribute("MAIN_ACCOUNT_CODE",objVoucherItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                                objNewItem.setAttribute("SUB_ACCOUNT_CODE",objVoucherItem.getAttribute("SUB_ACCOUNT_CODE").getString());
                                objNewItem.setAttribute("AMOUNT",AdjustAmount);
                                objNewItem.setAttribute("REMARKS",objVoucherItem.getAttribute("REMARKS").getString() + " Part 1:  " + AdjustAmount);
                                objNewItem.setAttribute("CREATED_BY",objVoucherItem.getAttribute("CREATED_BY").getString());
                                objNewItem.setAttribute("CREATED_DATE",objVoucherItem.getAttribute("CREATED_DATE").getString());
                                objNewItem.setAttribute("MODIFIED_BY",objVoucherItem.getAttribute("MODIFIED_BY").getString());
                                objNewItem.setAttribute("MODIFIED_DATE",objVoucherItem.getAttribute("MODIFIED_DATE").getString());
                                objNewItem.setAttribute("CANCELLED",objVoucherItem.getAttribute("CANCELLED").getInt());
                                objNewItem.setAttribute("CHANGED",objVoucherItem.getAttribute("CHANGED").getInt());
                                objNewItem.setAttribute("CHANGED_DATE",objVoucherItem.getAttribute("CHANGED_DATE").getString());
                                objNewItem.setAttribute("PO_NO",PONo);
                                objNewItem.setAttribute("PO_DATE",PODate);
                                objNewItem.setAttribute("INVOICE_NO",InvoiceNo);
                                objNewItem.setAttribute("INVOICE_DATE",InvoiceDate );
                                objNewItem.setAttribute("GRN_NO",GRNNo);
                                objNewItem.setAttribute("GRN_DATE",GRNDate);
                                objNewItem.setAttribute("MODULE_ID",objItem.getAttribute("REF_MODULE_ID").getInt());
                                objNewItem.setAttribute("REF_COMPANY_ID",objItem.getAttribute("PJV_COMPANY_ID").getInt());
                                objNewItem.setAttribute("INVOICE_AMOUNT",objVoucherItem.getAttribute("INVOICE_AMOUNT").getDouble()-AdjustAmount);
                                objNewItem.setAttribute("COMPANY_ID",objVoucherItem.getAttribute("COMPANY_ID").getInt());
                                objNewItem.setAttribute("IS_DEDUCTION",objVoucherItem.getAttribute("IS_DEDUCTION").getInt());
                                objNewItem.setAttribute("DEDUCTION_TYPE",objVoucherItem.getAttribute("DEDUCTION_TYPE").getInt());
                                objNewItem.setAttribute("REF_SR_NO",objVoucherItem.getAttribute("REF_SR_NO").getInt());
                                objPaymentVoucher.colVoucherItems.put(Integer.toString(objPaymentVoucher.colVoucherItems.size()+1), objNewItem);
                                
                                AdjustAmount=0;
                                int CreditCount = 0, TCreditCount=0;
                                int DebitCount = 0, TDebitCount=0;
                                
                                clsVoucher objNewPaymentVoucher=(clsVoucher)(new clsVoucher()).getObject(PaymentCompanyID,PaymentVoucherNo);
                                for(int j=1;j<=objPaymentVoucher.colVoucherItems.size();j++) {
                                    objVoucherItem=(clsVoucherItem)objPaymentVoucher.colVoucherItems.get(Integer.toString(j));
                                    if(objVoucherItem.getAttribute("IS_DEDUCTION").getInt() > 0) {
                                        continue;
                                    }
                                    if(objVoucherItem.getAttribute("EFFECT").getString().equals("D")) {
                                        DebitCount++;
                                        objVoucherItem.setAttribute("SR_NO",DebitCount);
                                        objNewPaymentVoucher.colVoucherItems.put(Integer.toString(DebitCount), objVoucherItem);
                                    }
                                }
                                
                                CreditCount=DebitCount;
                                for(int j=1;j<=objPaymentVoucher.colVoucherItems.size();j++) {
                                    objVoucherItem=(clsVoucherItem)objPaymentVoucher.colVoucherItems.get(Integer.toString(j));
                                    if(objVoucherItem.getAttribute("IS_DEDUCTION").getInt() > 0) {
                                        continue;
                                    }
                                    if(objVoucherItem.getAttribute("EFFECT").getString().equals("C")) {
                                        CreditCount++;
                                        objVoucherItem.setAttribute("SR_NO",CreditCount);
                                        objNewPaymentVoucher.colVoucherItems.put(Integer.toString(CreditCount), objVoucherItem);
                                    }
                                }
                                
                                DebitCount=CreditCount;
                                for(int j=1;j<=objPaymentVoucher.colVoucherItems.size();j++) {
                                    objVoucherItem=(clsVoucherItem)objPaymentVoucher.colVoucherItems.get(Integer.toString(j));
                                    if(objVoucherItem.getAttribute("IS_DEDUCTION").getInt() == 0) {
                                        continue;
                                    }
                                    if(objVoucherItem.getAttribute("EFFECT").getString().equals("D")) {
                                        DebitCount++;
                                        objVoucherItem.setAttribute("SR_NO",DebitCount);
                                        objNewPaymentVoucher.colVoucherItems.put(Integer.toString(DebitCount), objVoucherItem);
                                    }
                                }
                                
                                CreditCount=DebitCount;
                                for(int j=1;j<=objPaymentVoucher.colVoucherItems.size();j++) {
                                    objVoucherItem=(clsVoucherItem)objPaymentVoucher.colVoucherItems.get(Integer.toString(j));
                                    if(objVoucherItem.getAttribute("IS_DEDUCTION").getInt() == 0) {
                                        continue;
                                    }
                                    if(objVoucherItem.getAttribute("EFFECT").getString().equals("C")) {
                                        CreditCount++;
                                        objVoucherItem.setAttribute("SR_NO",CreditCount);
                                        objNewPaymentVoucher.colVoucherItems.put(Integer.toString(CreditCount), objVoucherItem);
                                    }
                                }
                                
                                objPaymentVoucher = objNewPaymentVoucher;
                                objNewPaymentVoucher=null;
                                //AdjustAmount = EITLERPGLOBAL.round(EITLERPGLOBAL.round(AdjustAmount,2) - EITLERPGLOBAL.round(AdjustAmount,2),2);
                                AdjustAmount = 0;
                                //break;
                            }
                        }
                    } else {
                        //Payment Amount is less than or equals to Adjustment Amount, no need to split the payment
                        //entry. Update the references
                        
                        //get the voucher line
                        for(int v=1;v<=objPaymentVoucher.colVoucherItems.size();v++) {
                            clsVoucherItem objVoucherItem=(clsVoucherItem)objPaymentVoucher.colVoucherItems.get(Integer.toString(v));
                            
                            if(objVoucherItem.getAttribute("SR_NO").getInt()==PaymentVoucherSrNo) {
                                //Create a new voucher line
                                objVoucherItem.setAttribute("REMARKS",objVoucherItem.getAttribute("REMARKS").getString() + " Part 2:  " + AdjustAmount);
                                objVoucherItem.setAttribute("PO_NO",PONo);
                                objVoucherItem.setAttribute("PO_DATE",PODate);
                                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
                                objVoucherItem.setAttribute("INVOICE_DATE",InvoiceDate );
                                objVoucherItem.setAttribute("GRN_NO",GRNNo);
                                objVoucherItem.setAttribute("GRN_DATE",GRNDate);
                                objVoucherItem.setAttribute("MODULE_ID",objItem.getAttribute("REF_MODULE_ID").getInt());
                                objVoucherItem.setAttribute("INVOICE_AMOUNT",objVoucherItem.getAttribute("INVOICE_AMOUNT").getDouble()-AdjustAmount);
                                objVoucherItem.setAttribute("REF_COMPANY_ID",objItem.getAttribute("PJV_COMPANY_ID").getInt());
                                objVoucherItem.setAttribute("REF_SR_NO",objVoucherItem.getAttribute("REF_SR_NO").getInt());
                                objPaymentVoucher.colVoucherItems.put(Integer.toString(v),objVoucherItem);
                                AdjustAmount = EITLERPGLOBAL.round(EITLERPGLOBAL.round(AdjustAmount,2) - EITLERPGLOBAL.round(PaymentAmount,2),2);
                            }
                        }
                    }
                    
                    objPaymentVoucher.UpdateForAdjustment();
                }
                
                //Update the voucher
                
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        
        return true;
    }
}

