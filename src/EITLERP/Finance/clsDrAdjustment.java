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
import EITLERP.Sales.*;
import EITLERP.Sales.DebitMemoReceiptMapping.clsDebitMemoReceiptMapping;

/**
 *
 * @author  jadave
 * @version
 */

public class clsDrAdjustment {
    
    public String LastError="";
    private ResultSet rsResultSet=null;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    //Requisition Material Collection
    public HashMap colItems;
    
    //History Related properties
    private boolean HistoryView=false;
    
    public static int ModuleID=92;
    
    
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
    public clsDrAdjustment() {
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
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FIN_DR_ADJ_HEADER WHERE COMPANY_ID="+pCompanyID+" AND DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY DOC_NO");
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
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FIN_DR_ADJ_HEADER_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FIN_DR_ADJ_DETAIL_H WHERE DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //--------- Generate Gatepass requisition no.  ------------
            setAttribute("DOC_NO",clsFirstFree.getNextFreeNo(getAttribute("COMPANY_ID").getInt(),clsDrAdjustment.ModuleID, getAttribute("FFNO").getInt(),true));
            //-------------------------------------------------
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getLong());
            rsResultSet.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsResultSet.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateLong("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getLong());
            rsResultSet.updateString("CREATED_BY", getAttribute("CREATED_BY").getString());
            rsResultSet.updateString("CREATED_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
            rsResultSet.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
            rsResultSet.updateString("MODIFIED_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
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
            rsHistory.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateString("CREATED_BY", getAttribute("CREATED_BY").getString());
            rsHistory.updateString("CREATED_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
            rsHistory.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
            rsHistory.updateString("MODIFIED_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
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
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FIN_DR_ADJ_DETAIL WHERE DOC_NO='1'");
            
            int CompanyID=getAttribute("COMPANY_ID").getInt();
            
            //Now Insert records into detail table
            for(int i=1;i<=colItems.size();i++) {
                clsDrAdjustmentItem objItem=(clsDrAdjustmentItem) colItems.get(Integer.toString(i));
                
                rsTmp.moveToInsertRow();
                rsTmp.updateInt("COMPANY_ID",CompanyID);
                rsTmp.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsTmp.updateInt("SR_NO",i);
                rsTmp.updateString("VOUCHER_NO",objItem.getAttribute("VOUCHER_NO").getString());
                rsTmp.updateString("VOUCHER_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("VOUCHER_DATE").getString()));
                rsTmp.updateString("MAIN_ACCOUNT_CODE",objItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsTmp.updateString("SUB_ACCOUNT_CODE",objItem.getAttribute("SUB_ACCOUNT_CODE").getString());
                rsTmp.updateString("REF_DOC_NO",objItem.getAttribute("REF_DOC_NO").getString());
                rsTmp.updateString("REF_DOC_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("REF_DOC_DATE").getString()));
                rsTmp.updateString("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                rsTmp.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("INVOICE_DATE").getString()));
                rsTmp.updateString("REMARKS",objItem.getAttribute("REMARKS").getString());
                rsTmp.updateString("CREATED_BY", getAttribute("CREATED_BY").getString());
                rsTmp.updateString("CREATED_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
                rsTmp.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
                rsTmp.updateString("MODIFIED_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateInt("CANCELLED",0);
                rsTmp.updateInt("VOUCHER_SR_NO",objItem.getAttribute("VOUCHER_SR_NO").getInt());
                rsTmp.updateDouble("RECEIPT_AMOUNT",objItem.getAttribute("RECEIPT_AMOUNT").getDouble());
                rsTmp.updateDouble("ADJUST_AMOUNT",objItem.getAttribute("ADJUST_AMOUNT").getDouble());
                rsTmp.updateInt("REF_MODULE_ID",objItem.getAttribute("MODULE_ID").getInt());
                rsTmp.updateString("SJ_NO",objItem.getAttribute("SJ_NO").getString());
                rsTmp.updateString("SJ_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("SJ_DATE").getString()));
                rsTmp.updateString("AGENT_SR",objItem.getAttribute("AGENT_SR").getString());
                rsTmp.updateInt("VOUCHER_COMPANY_ID",objItem.getAttribute("VOUCHER_COMPANY_ID").getInt());
                rsTmp.updateInt("SJ_COMPANY_ID",objItem.getAttribute("SJ_COMPANY_ID").getInt());
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
                rsHDetail.updateString("REF_DOC_NO",objItem.getAttribute("REF_DOC_NO").getString());
                rsHDetail.updateString("REF_DOC_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("REF_DOC_DATE").getString()));
                rsHDetail.updateString("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                rsHDetail.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("INVOICE_DATE").getString()));
                rsHDetail.updateString("REMARKS",objItem.getAttribute("REMARKS").getString());
                rsHDetail.updateString("CREATED_BY", getAttribute("CREATED_BY").getString());
                rsHDetail.updateString("CREATED_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
                rsHDetail.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
                rsHDetail.updateString("MODIFIED_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateInt("CANCELLED",0);
                rsHDetail.updateInt("VOUCHER_SR_NO",objItem.getAttribute("VOUCHER_SR_NO").getInt());
                rsHDetail.updateDouble("RECEIPT_AMOUNT",objItem.getAttribute("RECEIPT_AMOUNT").getDouble());
                rsHDetail.updateDouble("ADJUST_AMOUNT",objItem.getAttribute("ADJUST_AMOUNT").getDouble());
                rsHDetail.updateInt("REF_MODULE_ID",objItem.getAttribute("MODULE_ID").getInt());
                rsHDetail.updateString("SJ_NO",objItem.getAttribute("SJ_NO").getString());
                rsHDetail.updateString("SJ_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("SJ_DATE").getString()));
                rsHDetail.updateString("AGENT_SR",objItem.getAttribute("AGENT_SR").getString());
                rsHDetail.updateInt("VOUCHER_COMPANY_ID",objItem.getAttribute("VOUCHER_COMPANY_ID").getInt());
                rsHDetail.updateInt("SJ_COMPANY_ID",objItem.getAttribute("SJ_COMPANY_ID").getInt());
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
            ObjFlow.TableName="D_FIN_DR_ADJ_HEADER";
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
            
            if(AStatus.equals("F")) { //On Final Approval
                AdjustAllReceiptAmounts();
                String theDocNo = getAttribute("DOC_NO").getString();
                String str = "SELECT MAIN_ACCOUNT_CODE FROM D_FIN_DR_ADJ_DETAIL WHERE DOC_NO='" + theDocNo + "' "
                + " AND SUB_ACCOUNT_CODE<>'' AND INVOICE_NO<>'' AND MAIN_ACCOUNT_CODE IN ('210010','210027') ORDER BY MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE";
                System.out.println("get main account code : "+str);
                String MainAccountCode = data.getStringValueFromDB(str, FinanceGlobal.FinURL);
                System.out.println("MAIN ACCOUNT CODE : "+MainAccountCode);
                    //String MainAccountCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='" + theDocNo + "' AND EFFECT='C' AND SUB_ACCOUNT_CODE<>'' AND MAIN_ACCOUNT_CODE IN ('210010','210027') ", FinanceGlobal.FinURL);
            
                    /*if (MainAccountCode.equals("210027") || MainAccountCode.equals("210010")) {
                        PostDebitNote(currentVoucherNo);
                    }
                    */
                    if (MainAccountCode.equals("210010")) {
//                        PostDebitNoteFelt(theDocNo);
                        PostDebitNoteFeltV2(theDocNo);
                    }
                    else if (MainAccountCode.equals("210027")) {
//                        PostDebitNoteSuitings(theDocNo);
                        PostDebitNoteSuitingsV2(theDocNo);
                    }

//                //TCS DebitNote Voucher Posting
//                if (MainAccountCode.equals("210010")) {
//                    String SubAccountCode = data.getStringValueFromDB("SELECT SUB_ACCOUNT_CODE FROM D_FIN_DR_ADJ_DETAIL WHERE DOC_NO='" + theDocNo + "' "
//                            + " AND SUB_ACCOUNT_CODE<>'' AND INVOICE_NO<>'' AND MAIN_ACCOUNT_CODE IN ('210010') ORDER BY MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE ", FinanceGlobal.FinURL);
//                    double receiptAmt = data.getDoubleValueFromDB("SELECT RC_AMT FROM  (SELECT PARTY_CODE,PARTY_NAME,PAN_NO,SUBSTRING(GSTIN_NO,3,10) AS GSTIN_PAN,CITY_ID FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE = 210010 ) AS PM  LEFT JOIN  "
//                            + " (SELECT GSTIN_PAN AS V_PAN,SUM(AMOUNT) AS RC_AMT FROM  (SELECT B.SUB_ACCOUNT_CODE, P.PARTY_NAME, SUBSTRING(TRIM(P.GSTIN_NO),3,10) AS GSTIN_PAN, SUM(B.AMOUNT) AS AMOUNT, GROUP_CONCAT(A.VOUCHER_NO) "
//                            + " FROM FINANCE.D_FIN_VOUCHER_HEADER A, FINANCE.D_FIN_VOUCHER_DETAIL B, DINESHMILLS.D_SAL_PARTY_MASTER P "
//                            + "  WHERE A.VOUCHER_NO = B.VOUCHER_NO AND P.PARTY_CODE = B.SUB_ACCOUNT_CODE  AND A.VOUCHER_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND A.VOUCHER_DATE<=CURDATE() "
//                            //                            + " AND A.VOUCHER_TYPE IN (6,7,8,9) AND B.MAIN_ACCOUNT_CODE='210010' AND A.APPROVED=1 AND A.CANCELLED=0 AND A.EXCLUDE_IN_ADJ =0 "
//                            + " AND (A.VOUCHER_TYPE IN (6,8,9) OR (A.VOUCHER_TYPE IN (7) AND A.EXCLUDE_IN_ADJ =0) ) AND B.MAIN_ACCOUNT_CODE='210010' AND A.APPROVED=1 AND A.CANCELLED=0 "
//                            + " AND B.EFFECT='C' AND B.MODULE_ID <>65 GROUP BY B.SUB_ACCOUNT_CODE  ) AS AMT GROUP BY GSTIN_PAN ) AS VPAN  "
//                            + " ON VPAN.V_PAN = PM.GSTIN_PAN WHERE PM.PARTY_CODE ='" + SubAccountCode + "'");
//                    if (receiptAmt > 5000000) {
//                        PostTCSDebitNoteFelt(theDocNo);
//                    }
//                } 
            }
            //=====================================//
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            
            try {
                e.printStackTrace();
            }
            catch(Exception c) {
                
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
            
            //data.SetAutoCommit(false);
            //Conn.setAutoCommit(false);
            
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
            
            String theDocNo=(String)getAttribute("DOC_NO").getObj();
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            rsResultSet.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            rsResultSet.updateString("DOC_NO", theDocNo);
            rsResultSet.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateString("CREATED_BY", getAttribute("CREATED_BY").getString());
            rsResultSet.updateString("CREATED_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
            rsResultSet.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
            rsResultSet.updateString("MODIFIED_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateRow();
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FIN_DR_ADJ_HEADER_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FIN_DR_ADJ_DETAIL_H WHERE DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM D_FIN_DR_ADJ_HEADER_H WHERE COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+" AND DOC_NO='"+getAttribute("DOC_NO").getString()+"'",FinanceGlobal.FinURL);
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
            rsHistory.updateString("CREATED_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
            rsHistory.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
            rsHistory.updateString("MODIFIED_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            data.Execute("DELETE FROM D_FIN_DR_ADJ_DETAIL WHERE COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+" AND DOC_NO='"+theDocNo+"'",FinanceGlobal.FinURL);
            
            ResultSet rsTmp,rsTmpEx;
            Statement tmpStmt,tmpStmtEx;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FIN_DR_ADJ_DETAIL WHERE DOC_NO='1'");
            
            
            int Counter=0;
            
            //Now Insert records into detail table
            for(int i=1;i<=colItems.size();i++) {
                clsDrAdjustmentItem objItem=(clsDrAdjustmentItem) colItems.get(Integer.toString(i));
                
                Counter++;
                
                rsTmp.moveToInsertRow();
                rsTmp.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                rsTmp.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsTmp.updateInt("SR_NO",i);
                rsTmp.updateString("VOUCHER_NO",objItem.getAttribute("VOUCHER_NO").getString());
                rsTmp.updateString("VOUCHER_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("VOUCHER_DATE").getString()));
                
                rsTmp.updateString("MAIN_ACCOUNT_CODE",objItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsTmp.updateString("SUB_ACCOUNT_CODE",objItem.getAttribute("SUB_ACCOUNT_CODE").getString());
                rsTmp.updateString("REF_DOC_NO",objItem.getAttribute("REF_DOC_NO").getString());
                rsTmp.updateString("REF_DOC_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("REF_DOC_DATE").getString()));
                
                rsTmp.updateString("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                rsTmp.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("INVOICE_DATE").getString()));
                rsTmp.updateString("REMARKS",objItem.getAttribute("REMARKS").getString());
                rsTmp.updateString("CREATED_BY", getAttribute("CREATED_BY").getString());
                rsTmp.updateString("CREATED_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
                rsTmp.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
                rsTmp.updateString("MODIFIED_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateInt("CANCELLED",0);
                rsTmp.updateInt("VOUCHER_SR_NO",objItem.getAttribute("VOUCHER_SR_NO").getInt());
                rsTmp.updateDouble("RECEIPT_AMOUNT",objItem.getAttribute("RECEIPT_AMOUNT").getDouble());
                rsTmp.updateDouble("ADJUST_AMOUNT",objItem.getAttribute("ADJUST_AMOUNT").getDouble());
                rsTmp.updateInt("REF_MODULE_ID",objItem.getAttribute("MODULE_ID").getInt());
                rsTmp.updateString("SJ_NO",objItem.getAttribute("SJ_NO").getString());
                rsTmp.updateString("SJ_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("SJ_DATE").getString()));
                rsTmp.updateString("AGENT_SR",objItem.getAttribute("AGENT_SR").getString());
                rsTmp.updateInt("VOUCHER_COMPANY_ID",objItem.getAttribute("VOUCHER_COMPANY_ID").getInt());
                rsTmp.updateInt("SJ_COMPANY_ID",objItem.getAttribute("SJ_COMPANY_ID").getInt());
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
                rsHDetail.updateString("REF_DOC_NO",objItem.getAttribute("REF_DOC_NO").getString());
                rsHDetail.updateString("REF_DOC_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("REF_DOC_DATE").getString()));
                
                rsHDetail.updateString("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                rsHDetail.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("INVOICE_DATE").getString()));
                rsHDetail.updateString("REMARKS",objItem.getAttribute("REMARKS").getString());
                rsHDetail.updateString("CREATED_BY", getAttribute("CREATED_BY").getString());
                rsHDetail.updateString("CREATED_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
                rsHDetail.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
                rsHDetail.updateString("MODIFIED_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateInt("CANCELLED",0);
                rsHDetail.updateInt("VOUCHER_SR_NO",objItem.getAttribute("VOUCHER_SR_NO").getInt());
                rsHDetail.updateDouble("RECEIPT_AMOUNT",objItem.getAttribute("RECEIPT_AMOUNT").getDouble());
                rsHDetail.updateDouble("ADJUST_AMOUNT",objItem.getAttribute("ADJUST_AMOUNT").getDouble());
                rsHDetail.updateInt("REF_MODULE_ID",objItem.getAttribute("MODULE_ID").getInt());
                rsHDetail.updateString("SJ_NO",objItem.getAttribute("SJ_NO").getString());
                rsHDetail.updateString("SJ_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("SJ_DATE").getString()));
                rsHDetail.updateString("AGENT_SR",objItem.getAttribute("AGENT_SR").getString());
                rsHDetail.updateInt("VOUCHER_COMPANY_ID",objItem.getAttribute("VOUCHER_COMPANY_ID").getInt());
                rsHDetail.updateInt("SJ_COMPANY_ID",objItem.getAttribute("SJ_COMPANY_ID").getInt());
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
            ObjFlow.TableName="D_FIN_DR_ADJ_HEADER";
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
                data.Execute("UPDATE D_FIN_DR_ADJ_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+" AND DOC_NO='"+ObjFlow.DocNo+"'",FinanceGlobal.FinURL);
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
            if(AStatus.equals("F")) { //On Final Approval
                AdjustAllReceiptAmounts();
                //PostDebitNote(theDocNo);
                // for posting of debit note automaticilly -- end
            
                String str = "SELECT MAIN_ACCOUNT_CODE FROM D_FIN_DR_ADJ_DETAIL WHERE DOC_NO='" + theDocNo + "' "
                + " AND SUB_ACCOUNT_CODE<>'' AND INVOICE_NO<>'' AND MAIN_ACCOUNT_CODE IN ('210010','210027') ORDER BY MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE";
                System.out.println("get main account code : "+str);
                String MainAccountCode = data.getStringValueFromDB(str, FinanceGlobal.FinURL);
                System.out.println("MAIN ACCOUNT CODE : "+MainAccountCode);
                    //String MainAccountCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='" + theDocNo + "' AND EFFECT='C' AND SUB_ACCOUNT_CODE<>'' AND MAIN_ACCOUNT_CODE IN ('210010','210027') ", FinanceGlobal.FinURL);
            
                    /*if (MainAccountCode.equals("210027") || MainAccountCode.equals("210010")) {
                        PostDebitNote(currentVoucherNo);
                    }
                    */
                    if (MainAccountCode.equals("210010")) {
//                        PostDebitNoteFelt(theDocNo);
                        PostDebitNoteFeltV2(theDocNo);
                    }
                    else if (MainAccountCode.equals("210027")) {
//                        PostDebitNoteSuitings(theDocNo);
                        PostDebitNoteSuitingsV2(theDocNo);
                    }
                    
//                //TCS DebitNote Voucher Posting
//                if (MainAccountCode.equals("210010")) {
//                    String SubAccountCode = data.getStringValueFromDB("SELECT SUB_ACCOUNT_CODE FROM D_FIN_DR_ADJ_DETAIL WHERE DOC_NO='" + theDocNo + "' "
//                            + " AND SUB_ACCOUNT_CODE<>'' AND INVOICE_NO<>'' AND MAIN_ACCOUNT_CODE IN ('210010') ORDER BY MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE ", FinanceGlobal.FinURL);
//                    double receiptAmt = data.getDoubleValueFromDB("SELECT RC_AMT FROM  (SELECT PARTY_CODE,PARTY_NAME,PAN_NO,SUBSTRING(GSTIN_NO,3,10) AS GSTIN_PAN,CITY_ID FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE = 210010 ) AS PM  LEFT JOIN  "
//                            + " (SELECT GSTIN_PAN AS V_PAN,SUM(AMOUNT) AS RC_AMT FROM  (SELECT B.SUB_ACCOUNT_CODE, P.PARTY_NAME, SUBSTRING(TRIM(P.GSTIN_NO),3,10) AS GSTIN_PAN, SUM(B.AMOUNT) AS AMOUNT, GROUP_CONCAT(A.VOUCHER_NO) "
//                            + " FROM FINANCE.D_FIN_VOUCHER_HEADER A, FINANCE.D_FIN_VOUCHER_DETAIL B, DINESHMILLS.D_SAL_PARTY_MASTER P "
//                            + "  WHERE A.VOUCHER_NO = B.VOUCHER_NO AND P.PARTY_CODE = B.SUB_ACCOUNT_CODE  AND A.VOUCHER_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND A.VOUCHER_DATE<=CURDATE() "
//                            //                            + " AND A.VOUCHER_TYPE IN (6,7,8,9) AND B.MAIN_ACCOUNT_CODE='210010' AND A.APPROVED=1 AND A.CANCELLED=0 AND A.EXCLUDE_IN_ADJ =0 "
//                            + " AND (A.VOUCHER_TYPE IN (6,8,9) OR (A.VOUCHER_TYPE IN (7) AND A.EXCLUDE_IN_ADJ =0) ) AND B.MAIN_ACCOUNT_CODE='210010' AND A.APPROVED=1 AND A.CANCELLED=0 "
//                            + " AND B.EFFECT='C' AND B.MODULE_ID <>65 GROUP BY B.SUB_ACCOUNT_CODE  ) AS AMT GROUP BY GSTIN_PAN ) AS VPAN  "
//                            + " ON VPAN.V_PAN = PM.GSTIN_PAN WHERE PM.PARTY_CODE ='" + SubAccountCode + "'");
//                    if (receiptAmt > 5000000) {
//                        PostTCSDebitNoteFelt(theDocNo);
//                    }
//                } 
            
            }
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FIN_DR_ADJ_HEADER WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND (APPROVED=1)";
            
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FIN_DR_ADJ_HEADER WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND (APPROVED=1)";
            
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
                String strQry = "DELETE FROM D_FIN_DR_ADJ_HEADER WHERE COMPANY_ID=" + CompanyID +" AND DOC_NO='"+lDocNo+"'";
                data.Execute(strQry,FinanceGlobal.FinURL);
                strQry = "DELETE FROM D_FIN_DR_ADJ_DETAIL WHERE COMPANY_ID=" + CompanyID +" AND DOC_NO='"+lDocNo+"'";
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
            String strSql = "SELECT * FROM D_FIN_DR_ADJ_HEADER A, D_FIN_DR_ADJ_DETAIL B " + Condition ;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_FIN_DR_ADJ_HEADER WHERE COMPANY_ID="+CompanyID+" AND DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY DOC_NO";
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
            String strSql = "SELECT * FROM D_FIN_DR_ADJ_HEADER " + Condition ;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_FIN_DR_ADJ_HEADER WHERE COMPANY_ID="+CompanyID+" AND DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY DOC_NO";
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
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FIN_DR_ADJ_DETAIL_H WHERE COMPANY_ID=" + CompanyID + " AND DOC_NO='" + VoucherNo + "' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FIN_DR_ADJ_DETAIL WHERE COMPANY_ID=" + CompanyID + " AND DOC_NO='" + VoucherNo + "' ORDER BY SR_NO");
            }
            
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsDrAdjustmentItem objItem = new clsDrAdjustmentItem();
                
                objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                objItem.setAttribute("DOC_NO",UtilFunctions.getString(rsTmp,"DOC_NO",""));
                objItem.setAttribute("SR_NO",UtilFunctions.getInt(rsTmp,"SR_NO",0));
                objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                objItem.setAttribute("VOUCHER_DATE",UtilFunctions.getString(rsTmp,"VOUCHER_DATE","0000-00-00"));
                objItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE",""));
                objItem.setAttribute("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"SUB_ACCOUNT_CODE",""));
                objItem.setAttribute("REF_DOC_NO",UtilFunctions.getString(rsTmp,"REF_DOC_NO",""));
                objItem.setAttribute("REF_DOC_DATE",UtilFunctions.getString(rsTmp,"REF_DOC_DATE","0000-00-00"));
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
                objItem.setAttribute("RECEIPT_AMOUNT",UtilFunctions.getDouble(rsTmp,"RECEIPT_AMOUNT",0));
                objItem.setAttribute("ADJUST_AMOUNT",UtilFunctions.getDouble(rsTmp,"ADJUST_AMOUNT",0));
                objItem.setAttribute("MODULE_ID",UtilFunctions.getInt(rsTmp,"REF_MODULE_ID",0));
                objItem.setAttribute("SJ_NO",UtilFunctions.getString(rsTmp,"SJ_NO",""));
                objItem.setAttribute("SJ_DATE",UtilFunctions.getString(rsTmp,"SJ_DATE","0000-00-00"));
                objItem.setAttribute("AGENT_SR",UtilFunctions.getString(rsTmp,"AGENT_SR",""));
                objItem.setAttribute("VOUCHER_COMPANY_ID",UtilFunctions.getInt(rsTmp,"VOUCHER_COMPANY_ID",0));
                objItem.setAttribute("SJ_COMPANY_ID",UtilFunctions.getInt(rsTmp,"SJ_COMPANY_ID",0));
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
                strSQL="SELECT FINANCE.D_FIN_DR_ADJ_HEADER.DOC_NO,FINANCE.D_FIN_DR_ADJ_HEADER.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FIN_DR_ADJ_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_DR_ADJ_HEADER.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FIN_DR_ADJ_HEADER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FIN_DR_ADJ_HEADER.COMPANY_ID="+CompanyID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT FINANCE.D_FIN_DR_ADJ_HEADER.DOC_NO,FINANCE.D_FIN_DR_ADJ_HEADER.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FIN_DR_ADJ_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_DR_ADJ_HEADER.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FIN_DR_ADJ_HEADER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FIN_DR_ADJ_HEADER.COMPANY_ID="+CompanyID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_FIN_DR_ADJ_HEADER.DOC_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT FINANCE.D_FIN_DR_ADJ_HEADER.DOC_NO,FINANCE.D_FIN_DR_ADJ_HEADER.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FIN_DR_ADJ_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_DR_ADJ_HEADER.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FIN_DR_ADJ_HEADER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FIN_DR_ADJ_HEADER.COMPANY_ID="+CompanyID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_FIN_DR_ADJ_HEADER.DOC_NO";
            }
            
            rsTmp3=tmpStmt3.executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp3.next()) {
                if(EITLERPGLOBAL.isWithinDate(rsTmp3.getString("DOC_DATE"),FinYearFrom)) {
                    Counter=Counter+1;
                    clsDrAdjustment ObjDoc=new clsDrAdjustment();
                    
                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("DOC_NO",rsTmp3.getString("DOC_NO"));
                    ObjDoc.setAttribute("DOC_DATE",rsTmp3.getString("DOC_DATE"));
                    ObjDoc.setAttribute("RECEIVED_DATE",rsTmp3.getString("RECEIVED_DATE"));
                    ObjDoc.setAttribute("DEPT_ID",0);
                    String PartyCode = data.getStringValueFromDB("SELECT SUB_ACCOUNT_CODE FROM D_FIN_DR_ADJ_DETAIL WHERE DOC_NO='"+rsTmp3.getString("DOC_NO")+"' ",FinanceGlobal.FinURL);
                    String MainCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_DR_ADJ_DETAIL WHERE DOC_NO='"+rsTmp3.getString("DOC_NO")+"' ",FinanceGlobal.FinURL);
                    ObjDoc.setAttribute("PARTY_CODE",PartyCode);
                    ObjDoc.setAttribute("PARTY_NAME",clsAccount.getAccountName(MainCode, PartyCode));
                    // ----------------- End of Header Fields ------------------------------------//
                    
                    List.put(Long.toString(Counter),ObjDoc);
                }
            }//end of while
            
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
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FIN_DR_ADJ_HEADER_H WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"'");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM D_FIN_DR_ADJ_HEADER_H WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                
                while(!rsTmp.isAfterLast()) {
                    clsDrAdjustment objVoucher=new clsDrAdjustment();
                    
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
            return List;
        } catch(Exception e) {
            return List;
        }
    }
    
    
    
    public static String getDocStatus(int CompanyID,String DocNo) {
        ResultSet rsTmp;
        String strMessage="";
        
        try {
            rsTmp=data.getResult("SELECT DOC_NO,APPROVED,CANCELLED FROM D_FIN_DR_ADJ_HEADER WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"'",FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                if(rsTmp.getBoolean("APPROVED")) {
                    if(rsTmp.getBoolean("CANCELLED")) {
                        strMessage="Document is cancelled";
                    } else {
                        strMessage="";
                    }
                } else {
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
            rsTmp=data.getResult("SELECT DOC_NO FROM D_FIN_DR_ADJ_HEADER WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"' AND APPROVED=0 AND CANCELLED=0",FinanceGlobal.FinURL);
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
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_FIN_DR_ADJ_HEADER WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"'",FinanceGlobal.FinURL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedDoc) {
                    //Revert the Changes done
                    rsTmp=data.getResult("SELECT * FROM D_FIN_DR_ADJ_HEADER WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"'",FinanceGlobal.FinURL);
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
                
                data.Execute("UPDATE D_FIN_DR_ADJ_HEADER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"'",FinanceGlobal.FinURL);
                
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
        
        return true;
    }
    
    public boolean AutoAdjustReceiptAmount(int CompanyID,String SJNo) {
        
        try {
            
            //************** Fetch party information from the SJ ***************//
            double SJAmount=0;
            String SJInvoiceNo="";
            String SJInvoiceDate="";
            String MainAccountCode="";
            String PartyCode="";
            double AdjustedAmount=0;
            String VoucherNo="";
            double VoucherAmount=0;
            int ReceiptCompanyID=0;
            ResultSet rsVouchers = null;
            ResultSet rsSJ=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+SJNo+"' AND SUB_ACCOUNT_CODE<>'' AND MAIN_ACCOUNT_CODE NOT IN (132802,132803) ",FinanceGlobal.FinURL);
            rsSJ.first();
            System.out.println("sd amt : a");
            if(rsSJ.getRow()>0) {
                SJAmount=UtilFunctions.getDouble(rsSJ,"AMOUNT",0);
                SJInvoiceNo=UtilFunctions.getString(rsSJ,"INVOICE_NO","");
                SJInvoiceDate=EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsSJ,"INVOICE_DATE","0000-00-00"));
                MainAccountCode=UtilFunctions.getString(rsSJ,"MAIN_ACCOUNT_CODE","");
                PartyCode=UtilFunctions.getString(rsSJ,"SUB_ACCOUNT_CODE","");
                AdjustedAmount=SJAmount;
                System.out.println("Adjusted amt "+AdjustedAmount); 
                 System.out.println("sd amt : a1");
                String strSQL = "SELECT A.VOUCHER_NO,B.AMOUNT,B.SR_NO FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B " + //SUM(B.AMOUNT) AS AMOUNT
                "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND A.VOUCHER_TYPE IN (6,7,8,9,12) AND B.MAIN_ACCOUNT_CODE = '"+MainAccountCode+"' " +
                "AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND A.APPROVED=1 AND A.CANCELLED=0 AND A.EXCLUDE_IN_ADJ =0 AND B.EFFECT='C' " +
                "AND B.INVOICE_NO ='' AND B.GRN_NO ='' AND B.MODULE_ID <>"+clsVoucher.DebitNoteModuleID+" AND (B.MATCHED=0 OR B.MATCHED IS NULL) " +
                "AND A.VOUCHER_DATE<='"+EITLERPGLOBAL.formatDateDB(SJInvoiceDate)+"' " +
                "GROUP BY A.VOUCHER_NO,B.SR_NO " +
                "ORDER BY A.VOUCHER_DATE,A.VOUCHER_NO,B.SR_NO"; //
                
                System.out.println(strSQL);
                rsVouchers = data.getResult(strSQL,FinanceGlobal.FinURL);
                rsVouchers.first();
                
                while(AdjustedAmount > 0 ) { //&& !rsVouchers.isAfterLast()
                   
                
                    // System.out.println("sd amt : a2");
                    VoucherNo = UtilFunctions.getString(rsVouchers,"VOUCHER_NO", "");
                    double AmountCanAdjust = UtilFunctions.getDouble(rsVouchers,"AMOUNT", 0);
                    int VoucherSrNo = UtilFunctions.getInt(rsVouchers,"SR_NO", 0);
                   //  System.out.println("Amount can adj1 "+AmountCanAdjust);
                    // DO NOT USE CODE FOR FINDING AMOUNT CAN ADJUST -- START =-= MRUGESH 27/05/2010
                    
                    /*strSQL = "SELECT SUM(ROUND(B.AMOUNT,2)) AS AMOUNT  FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B " +
                    "WHERE A.VOUCHER_NO = B.VOUCHER_NO AND A.VOUCHER_NO='"+VoucherNo+"' AND B.MAIN_ACCOUNT_CODE='"+MainAccountCode+"' " +
                    "AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND A.APPROVED=1 AND A.CANCELLED=0 AND A.EXCLUDE_IN_ADJ=0 " +
                    "AND B.EFFECT='C' AND B.INVOICE_NO ='' AND B.MODULE_ID<>"+clsVoucher.DebitNoteModuleID+" ";  //AND B.GRN_NO =''
                    double AmountCanAdjust = data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL); */
                    
                    // DO NOT USE CODE FOR FINDING AMOUNT CAN ADJUST -- END =-= MRUGESH 27/05/2010
                    
                    //AmountCanAdjust = clsAccount.get09AmountByVoucher(VoucherNo, MainAccountCode, PartyCode,AmountCanAdjust);
                    double Amount = 0;
                    
                    if(AmountCanAdjust <= 0) {
                        rsVouchers.next();
                        continue;
                    }
                    //sj amount > voucher amount
                    if(AdjustedAmount > AmountCanAdjust) {
                        Amount = AmountCanAdjust;
                         System.out.println("sd amt : b");
                        int VoucherType = clsVoucher.getVoucherType(VoucherNo);
                        if(VoucherType==FinanceGlobal.TYPE_RECEIPT || VoucherType==FinanceGlobal.TYPE_RECEIPT_TDS || VoucherType == FinanceGlobal.TYPE_CASH_RECEIPT_VOUCHER
                        || VoucherType==FinanceGlobal.TYPE_CREDIT_NOTE) {
                             System.out.println("sd amt : b1");
                            AdjustReceiptAmount(CompanyID, SJNo, VoucherNo, Amount,VoucherSrNo,PartyCode);
                        }
                        if(VoucherType==FinanceGlobal.TYPE_JOURNAL) {
                             System.out.println("sd amt : b2");
                            AdjustJournalAmount(CompanyID, SJNo, VoucherNo, Amount,VoucherSrNo,PartyCode);
                        }
                        AdjustedAmount = AdjustedAmount - Amount;
                    } else {
                        Amount = AdjustedAmount;
                        int VoucherType = clsVoucher.getVoucherType(VoucherNo);
                        if(VoucherType==FinanceGlobal.TYPE_RECEIPT || VoucherType==FinanceGlobal.TYPE_RECEIPT_TDS || VoucherType == FinanceGlobal.TYPE_CASH_RECEIPT_VOUCHER
                        || VoucherType==FinanceGlobal.TYPE_CREDIT_NOTE) {
                             System.out.println("sd amt : c1");
                            AdjustReceiptAmount(CompanyID, SJNo, VoucherNo, Amount,VoucherSrNo,PartyCode);
                        }
                        if(VoucherType==FinanceGlobal.TYPE_JOURNAL ) {
                             System.out.println("sd amt : c2");
                            AdjustJournalAmount(CompanyID, SJNo, VoucherNo, Amount,VoucherSrNo,PartyCode);
                        }
                        AdjustedAmount = AdjustedAmount - Amount;
                    }
                    if(AdjustedAmount<=0) {
                        AdjustedAmount=0;
                    } else {
                        rsVouchers.next();
                    }
                }
                
                //******************************* ADVANCE JOURNAL VOUCHERS PORTION COMPLETED ********************************//
                //***********************************************************************************************************//
            }
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    
    
    
       public boolean AutoAdjustReceiptAmountSD(int CompanyID,String SJNo) {
        
        try {
            System.out.println("sd amt :  15");            
            //************** Fetch party information from the SJ ***************//
            double SJAmount=0;
            String SJInvoiceNo="";
            String SJInvoiceDate="";
            String MainAccountCode="";
            String PartyCode="";
            double AdjustedAmount=0;
            String VoucherNo="";
            double VoucherAmount=0;
            int ReceiptCompanyID=0;
            ResultSet rsVouchers = null;
            ResultSet rsSJ=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+SJNo+"' AND MAIN_ACCOUNT_CODE = 132802",FinanceGlobal.FinURL);
      //      ResultSet rsSJ=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+SJNo+"' AND SUB_ACCOUNT_CODE<>''",FinanceGlobal.FinURL);
            rsSJ.first();
            
            if(rsSJ.getRow()>0) {
                System.out.println("sd amt : 16");            
                SJAmount=UtilFunctions.getDouble(rsSJ,"AMOUNT",0);
                SJInvoiceNo=UtilFunctions.getString(rsSJ,"INVOICE_NO","");
                SJInvoiceDate=EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsSJ,"INVOICE_DATE","0000-00-00"));
                MainAccountCode=UtilFunctions.getString(rsSJ,"MAIN_ACCOUNT_CODE","");
                PartyCode=UtilFunctions.getString(rsSJ,"SUB_ACCOUNT_CODE","");
                AdjustedAmount=SJAmount;
                System.out.println("Adjusted amt "+AdjustedAmount);            
                String strSQL = "SELECT A.VOUCHER_NO,B.AMOUNT,B.SR_NO FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B " + //SUM(B.AMOUNT) AS AMOUNT
                "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND A.VOUCHER_TYPE IN (6,7,8,9,12) AND B.MAIN_ACCOUNT_CODE IN (210010,132802) " +
                "AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND A.APPROVED=1 AND A.CANCELLED=0 AND A.EXCLUDE_IN_ADJ =0 AND B.EFFECT='C' " +
                "AND B.INVOICE_NO ='' AND B.GRN_NO ='' AND B.MODULE_ID <>"+clsVoucher.DebitNoteModuleID+" AND (B.MATCHED=0 OR B.MATCHED IS NULL) " +
                "AND A.VOUCHER_DATE<='"+EITLERPGLOBAL.formatDateDB(SJInvoiceDate)+"' " +
                "GROUP BY A.VOUCHER_NO,B.SR_NO " +
                "ORDER BY A.VOUCHER_DATE,A.VOUCHER_NO,B.SR_NO"; //
                System.out.println(strSQL);
                rsVouchers = data.getResult(strSQL,FinanceGlobal.FinURL);
                rsVouchers.first();
                
                while(AdjustedAmount > 0 ) { //&& !rsVouchers.isAfterLast()
                    
                    VoucherNo = UtilFunctions.getString(rsVouchers,"VOUCHER_NO", "");
                    double AmountCanAdjust = UtilFunctions.getDouble(rsVouchers,"AMOUNT", 0);
                    System.out.println("Amount can adj "+AmountCanAdjust);
                    int VoucherSrNo = UtilFunctions.getInt(rsVouchers,"SR_NO", 0);
                    
                    // DO NOT USE CODE FOR FINDING AMOUNT CAN ADJUST -- START =-= MRUGESH 27/05/2010
                    
                    /*strSQL = "SELECT SUM(ROUND(B.AMOUNT,2)) AS AMOUNT  FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B " +
                    "WHERE A.VOUCHER_NO = B.VOUCHER_NO AND A.VOUCHER_NO='"+VoucherNo+"' AND B.MAIN_ACCOUNT_CODE='"+MainAccountCode+"' " +
                    "AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND A.APPROVED=1 AND A.CANCELLED=0 AND A.EXCLUDE_IN_ADJ=0 " +
                    "AND B.EFFECT='C' AND B.INVOICE_NO ='' AND B.MODULE_ID<>"+clsVoucher.DebitNoteModuleID+" ";  //AND B.GRN_NO =''
                    double AmountCanAdjust = data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL); */
                    
                    // DO NOT USE CODE FOR FINDING AMOUNT CAN ADJUST -- END =-= MRUGESH 27/05/2010
                    
                    //AmountCanAdjust = clsAccount.get09AmountByVoucher(VoucherNo, MainAccountCode, PartyCode,AmountCanAdjust);
                    double Amount = 0;
                    
                    if(AmountCanAdjust <= 0) {
                        rsVouchers.next();
                        continue;
                    }
                    //sj amount > voucher amount
                    if(AdjustedAmount > AmountCanAdjust) {
                        System.out.println("sd amt :  17");            
                        Amount = AmountCanAdjust;
                        int VoucherType = clsVoucher.getVoucherType(VoucherNo);
                        if(VoucherType==FinanceGlobal.TYPE_RECEIPT || VoucherType==FinanceGlobal.TYPE_RECEIPT_TDS || VoucherType == FinanceGlobal.TYPE_CASH_RECEIPT_VOUCHER
                        || VoucherType==FinanceGlobal.TYPE_CREDIT_NOTE) {
                            AdjustReceiptAmount(CompanyID, SJNo, VoucherNo, Amount,VoucherSrNo,PartyCode);
                        }
                        if(VoucherType==FinanceGlobal.TYPE_JOURNAL) {
                            AdjustJournalAmount(CompanyID, SJNo, VoucherNo, Amount,VoucherSrNo,PartyCode);
                        }
                        AdjustedAmount = AdjustedAmount - Amount;
                    } else {
                        System.out.println("sd amt :  18");            
                        Amount = AdjustedAmount;
                        int VoucherType = clsVoucher.getVoucherType(VoucherNo);
                        if(VoucherType==FinanceGlobal.TYPE_RECEIPT || VoucherType==FinanceGlobal.TYPE_RECEIPT_TDS || VoucherType == FinanceGlobal.TYPE_CASH_RECEIPT_VOUCHER
                        || VoucherType==FinanceGlobal.TYPE_CREDIT_NOTE) {
                            AdjustReceiptAmount(CompanyID, SJNo, VoucherNo, Amount,VoucherSrNo,PartyCode);
                        }
                        if(VoucherType==FinanceGlobal.TYPE_JOURNAL ) {
                            AdjustJournalAmount(CompanyID, SJNo, VoucherNo, Amount,VoucherSrNo,PartyCode);
                        }
                        AdjustedAmount = AdjustedAmount - Amount;
                    }
                    if(AdjustedAmount<=0) {
                        AdjustedAmount=0;
                    } else {
                        rsVouchers.next();
                    }
                }
                
                //******************************* ADVANCE JOURNAL VOUCHERS PORTION COMPLETED ********************************//
                //***********************************************************************************************************//
            }
            System.out.println("ssd amt  18");
            return true;
            
        } catch(Exception e) {     
            System.out.println("sd amt :  19");            
            e.printStackTrace();
            return false;
        }
    }
    
    
    
    public boolean AdjustReceiptAmount(int CompanyID,String SJNo,String VoucherNo,double adjustAmount,int VoucherSrNo,String PartyCode) {
        try {
            
            //************** Fetch party information from the SJ ***************//
            double SJAmount=0;
            String SJInvoiceNo="";
            String SJInvoiceDate="";
            double SJInvoiceAmount=0;
            String MainAccountCode="";
            //String PartyCode="";
            String SJRemarks="";
            double AdjustedAmount=0;
            String ReceiptNo="";
            double ReceiptAmount=0;
            int ReceiptCompanyID=0;
            int vType = clsVoucher.getVoucherType(SJNo);
            ResultSet rsSJ=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+SJNo+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND MAIN_ACCOUNT_CODE IN ('210027','210010','210072')",FinanceGlobal.FinURL);
            rsSJ.first();
            
            if(rsSJ.getRow()>0) {
                SJInvoiceAmount = UtilFunctions.getDouble(rsSJ,"AMOUNT",0);
                SJAmount=adjustAmount;
                SJInvoiceNo=UtilFunctions.getString(rsSJ,"INVOICE_NO","");
                SJRemarks=UtilFunctions.getString(rsSJ,"REMARKS","");
                SJInvoiceDate=EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsSJ,"INVOICE_DATE","0000-00-00"));
                MainAccountCode=UtilFunctions.getString(rsSJ,"MAIN_ACCOUNT_CODE","");
                PartyCode=UtilFunctions.getString(rsSJ,"SUB_ACCOUNT_CODE","");
                AdjustedAmount=SJAmount;
                
                
                ReceiptNo=VoucherNo;
                
                clsVoucher objReceiptVoucher=(clsVoucher)(new clsVoucher()).getObject(CompanyID,ReceiptNo);
                for(int i=1;i<=objReceiptVoucher.colVoucherItems.size();i++) {
                    clsVoucherItem objItem=(clsVoucherItem)objReceiptVoucher.colVoucherItems.get(Integer.toString(i));
                    if(objItem.getAttribute("MAIN_ACCOUNT_CODE").getString().equals(MainAccountCode)&&objItem.getAttribute("SUB_ACCOUNT_CODE").getString().equals(PartyCode) && objItem.getAttribute("SR_NO").getInt()==VoucherSrNo) {
                        //This is the item to be adjusted
                        ReceiptAmount=objItem.getAttribute("AMOUNT").getDouble();
                        
                        if(ReceiptAmount>AdjustedAmount) {
                            String ValueDate = "";
                            //Split this line in two lines
                            objItem.setAttribute("AMOUNT",ReceiptAmount-AdjustedAmount);
                            
                            if(objItem.getAttribute("VALUE_DATE").getString().equals("")) {
                                String VoucherDate = data.getStringValueFromDB("SELECT VOUCHER_DATE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+objItem.getAttribute("VOUCHER_NO").getString()+"' ",FinanceGlobal.FinURL);
                                objItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(VoucherDate));
                                ValueDate = EITLERPGLOBAL.formatDate(VoucherDate);//VoucherDate;
                            } else {
                                ValueDate = objItem.getAttribute("VALUE_DATE").getString();
                            }
                            objReceiptVoucher.colVoucherItems.put(Integer.toString(i),objItem);
                            
                            //Find Next Sr. No.
                            int NewSrNo=data.getIntValueFromDB("SELECT MAX(SR_NO) FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL)+1;
                            
                            //This item to be splitted in two
                            clsVoucherItem objNewItem=new clsVoucherItem();
                            
                            objNewItem.setAttribute("COMPANY_ID",objItem.getAttribute("COMPANY_ID").getInt());
                            objNewItem.setAttribute("VOUCHER_NO",objItem.getAttribute("VOUCHER_NO").getString());
                            objNewItem.setAttribute("SR_NO",NewSrNo);
                            objNewItem.setAttribute("EFFECT",objItem.getAttribute("EFFECT").getString());
                            objNewItem.setAttribute("ACCOUNT_ID",objItem.getAttribute("ACCOUNT_ID").getInt());
                            objNewItem.setAttribute("MAIN_ACCOUNT_CODE",objItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                            objNewItem.setAttribute("SUB_ACCOUNT_CODE",objItem.getAttribute("SUB_ACCOUNT_CODE").getString());
                            objNewItem.setAttribute("AMOUNT",AdjustedAmount);
                            objNewItem.setAttribute("APPLICABLE_AMOUNT",0);
                            objNewItem.setAttribute("PERCENTAGE",0);
                            objNewItem.setAttribute("REMARKS",objItem.getAttribute("REMARKS").getString());
                            objNewItem.setAttribute("CREATED_BY",objItem.getAttribute("CREATED_BY").getString());
                            objNewItem.setAttribute("CREATED_DATE",objItem.getAttribute("CREATED_DATE").getString());
                            objNewItem.setAttribute("MODIFIED_BY",objItem.getAttribute("MODIFIED_BY").getString());
                            objNewItem.setAttribute("MODIFIED_DATE",objItem.getAttribute("MODIFIED_DATE").getString());
                            objNewItem.setAttribute("CANCELLED",objItem.getAttribute("CANCELLED").getInt());
                            objNewItem.setAttribute("CHANGED",objItem.getAttribute("CHANGED").getInt());
                            objNewItem.setAttribute("CHANGED_DATE",objItem.getAttribute("CHANGED_DATE").getString());
                            objNewItem.setAttribute("PO_NO",objItem.getAttribute("PO_NO").getString());
                            objNewItem.setAttribute("PO_DATE",objItem.getAttribute("PO_DATE").getString());
                            objNewItem.setAttribute("INVOICE_NO",SJInvoiceNo);
                            objNewItem.setAttribute("INVOICE_DATE",SJInvoiceDate);
                            objNewItem.setAttribute("VALUE_DATE",ValueDate);
                            if(vType==FinanceGlobal.TYPE_SALES_JOURNAL) {
                                objNewItem.setAttribute("GRN_NO",SJInvoiceNo);
                                objNewItem.setAttribute("GRN_DATE",SJInvoiceDate);
                                objNewItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
                                objNewItem.setAttribute("REF_VOUCHER_NO","");
                            } else {
                                if(clsVoucher.getVoucherModuleID(clsVoucher.getVoucherType(SJNo))==FinanceGlobal.TYPE_LC_JV) {
                                    objNewItem.setAttribute("INVOICE_NO","");
                                    objNewItem.setAttribute("INVOICE_DATE","0000-00-00");
                                }
                                objNewItem.setAttribute("GRN_NO",SJNo);
                                objNewItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(data.getStringValueFromDB("SELECT VOUCHER_DATE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+SJNo+"' ",FinanceGlobal.FinURL)));
                                objNewItem.setAttribute("MODULE_ID",clsVoucher.getVoucherModuleID(clsVoucher.getVoucherType(SJNo)));
                                objNewItem.setAttribute("REF_VOUCHER_NO","");
                            }
                            objNewItem.setAttribute("REF_COMPANY_ID",CompanyID);
                            objNewItem.setAttribute("INVOICE_AMOUNT",SJInvoiceAmount);
                            objNewItem.setAttribute("COMPANY_ID",CompanyID);
                            objNewItem.setAttribute("IS_DEDUCTION",0);
                            objNewItem.setAttribute("DEDUCTION_TYPE",0);
                            objNewItem.setAttribute("LINK_NO",objItem.getAttribute("LINK_NO").getString());
                            objNewItem.setAttribute("MATCHED",0);
                            objNewItem.setAttribute("MATCHED_DATE","0000-00-00");
                            objNewItem.setAttribute("REF_VOUCHER_TYPE",0);
                            objNewItem.setAttribute("REF_VOUCHER_COMPANY_ID",0);
                            objNewItem.setAttribute("REF_SR_NO",0);
                            
                            objReceiptVoucher.colVoucherItems.put(Integer.toString(objReceiptVoucher.colVoucherItems.size()+1), objNewItem);
                            
                            AdjustedAmount=0;
                            int CreditCount = 0;
                            int DebitCount = 0;
                            clsVoucher objNewReceiptVoucher=(clsVoucher)(new clsVoucher()).getObject(CompanyID,ReceiptNo);
                            for(int j=1;j<=objReceiptVoucher.colVoucherItems.size();j++) {
                                objItem=(clsVoucherItem)objReceiptVoucher.colVoucherItems.get(Integer.toString(j));
                                if(objItem.getAttribute("EFFECT").getString().equals("C")) {
                                    CreditCount++;
                                    objItem.setAttribute("SR_NO",CreditCount);
                                    objNewReceiptVoucher.colVoucherItems.put(Integer.toString(CreditCount), objItem);
                                }
                            }
                            DebitCount=CreditCount;
                            for(int j=1;j<=objReceiptVoucher.colVoucherItems.size();j++) {
                                objItem=(clsVoucherItem)objReceiptVoucher.colVoucherItems.get(Integer.toString(j));
                                if(objItem.getAttribute("EFFECT").getString().equals("D")) {
                                    DebitCount++;
                                    objItem.setAttribute("SR_NO",DebitCount);
                                    objNewReceiptVoucher.colVoucherItems.put(Integer.toString(DebitCount), objItem);
                                }
                            }
                            objReceiptVoucher = objNewReceiptVoucher;
                            objNewReceiptVoucher=null;
                            break;
                            
                        } else {
                            if(objItem.getAttribute("VALUE_DATE").getString().equals("")) {
                                String VoucherDate = data.getStringValueFromDB("SELECT VOUCHER_DATE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+objItem.getAttribute("VOUCHER_NO").getString()+"' ",FinanceGlobal.FinURL);
                                objItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(VoucherDate));
                            }
                            
                            objItem.setAttribute("INVOICE_NO",SJInvoiceNo);
                            objItem.setAttribute("INVOICE_DATE",SJInvoiceDate);
                            if(vType==FinanceGlobal.TYPE_SALES_JOURNAL) {
                                objItem.setAttribute("GRN_NO",SJInvoiceNo);
                                objItem.setAttribute("GRN_DATE",SJInvoiceDate);
                                objItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
                            } else {
                                if(clsVoucher.getVoucherModuleID(clsVoucher.getVoucherType(SJNo))==FinanceGlobal.TYPE_LC_JV) {
                                    objItem.setAttribute("INVOICE_NO","");
                                    objItem.setAttribute("INVOICE_DATE","0000-00-00");
                                }
                                objItem.setAttribute("GRN_NO",SJNo);
                                objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(data.getStringValueFromDB("SELECT VOUCHER_DATE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+SJNo+"' ",FinanceGlobal.FinURL)));
                                objItem.setAttribute("MODULE_ID",clsVoucher.getVoucherModuleID(clsVoucher.getVoucherType(SJNo)));
                            }
                            
                            objItem.setAttribute("REF_COMPANY_ID",CompanyID);
                            objItem.setAttribute("INVOICE_AMOUNT",SJInvoiceAmount);
                            objItem.setAttribute("MATCHED",0);
                            objItem.setAttribute("MATCHED_DATE","0000-00-00");
                            objItem.setAttribute("REF_VOUCHER_NO","");
                            objItem.setAttribute("REF_VOUCHER_TYPE",0);
                            objItem.setAttribute("REF_VOUCHER_COMPANY_ID",0);
                            objItem.setAttribute("REF_SR_NO",0);
                            
                            objReceiptVoucher.colVoucherItems.put(Integer.toString(i),objItem);
                            
                            AdjustedAmount-=ReceiptAmount;
                            break;
                        }
                    }
                }
                //Update the voucher
                objReceiptVoucher.UpdateForAdjustment();
            }
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    public boolean AdjustJournalAmount(int CompanyID,String SJNo,String VoucherNo,double adjustAmount,int VoucherSrNo,String PartyCode) {
        try {
            
            //************** Fetch party information from the SJ ***************//
            double SJAmount=0;
            String SJInvoiceNo="";
            String SJInvoiceDate="";
            double SJInvoiceAmount=0;
            String MainAccountCode="";
            //String PartyCode="";
            double AdjustedAmount=0;
            String JVNo="";
            double JVAmount=0;
            int JVCompanyID=0;
            int vType = clsVoucher.getVoucherType(SJNo);
            ResultSet rsSJ=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+SJNo+"' AND MAIN_ACCOUNT_CODE IN ('210027','210010','210072') AND SUB_ACCOUNT_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
            rsSJ.first();
            
            if(rsSJ.getRow()>0) {
                SJInvoiceAmount = UtilFunctions.getDouble(rsSJ,"AMOUNT",0);
                SJAmount=adjustAmount;
                SJInvoiceNo=UtilFunctions.getString(rsSJ,"INVOICE_NO","");
                SJInvoiceDate=EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsSJ,"INVOICE_DATE","0000-00-00"));
                MainAccountCode=UtilFunctions.getString(rsSJ,"MAIN_ACCOUNT_CODE","");
                PartyCode=UtilFunctions.getString(rsSJ,"SUB_ACCOUNT_CODE","");
                AdjustedAmount=SJAmount;
                
                
                JVNo=VoucherNo;
                
                clsVoucher objJVVoucher=(clsVoucher)(new clsVoucher()).getObject(CompanyID,JVNo);
                for(int i=1;i<=objJVVoucher.colVoucherItems.size();i++) {
                    clsVoucherItem objItem=(clsVoucherItem)objJVVoucher.colVoucherItems.get(Integer.toString(i));
                    if(objItem.getAttribute("MAIN_ACCOUNT_CODE").getString().equals(MainAccountCode)&&objItem.getAttribute("SUB_ACCOUNT_CODE").getString().equals(PartyCode)&&objItem.getAttribute("SR_NO").getInt()==VoucherSrNo) {
                        //This is the item to be adjusted
                        JVAmount=objItem.getAttribute("AMOUNT").getDouble();
                        
                        if(JVAmount>AdjustedAmount) {
                            String ValueDate = "";
                            //Split this line in two lines
                            objItem.setAttribute("AMOUNT",JVAmount-AdjustedAmount);
                            
                            if(objItem.getAttribute("VALUE_DATE").getString().equals("")) {
                                String VoucherDate = data.getStringValueFromDB("SELECT VOUCHER_DATE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+objItem.getAttribute("VOUCHER_NO").getString()+"' ",FinanceGlobal.FinURL);
                                objItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(VoucherDate));
                                ValueDate = EITLERPGLOBAL.formatDate(VoucherDate); //VoucherDate;
                            } else {
                                ValueDate = objItem.getAttribute("VALUE_DATE").getString();
                            }
                            objJVVoucher.colVoucherItems.put(Integer.toString(i),objItem);
                            
                            //Find Next Sr. No.
                            int NewSrNo=data.getIntValueFromDB("SELECT MAX(SR_NO) FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+JVNo+"'",FinanceGlobal.FinURL)+1;
                            
                            //This item to be splitted in two
                            clsVoucherItem objNewItem=new clsVoucherItem();
                            
                            objNewItem.setAttribute("COMPANY_ID",objItem.getAttribute("COMPANY_ID").getInt());
                            objNewItem.setAttribute("VOUCHER_NO",objItem.getAttribute("VOUCHER_NO").getString());
                            objNewItem.setAttribute("SR_NO",NewSrNo);
                            objNewItem.setAttribute("EFFECT",objItem.getAttribute("EFFECT").getString());
                            objNewItem.setAttribute("ACCOUNT_ID",objItem.getAttribute("ACCOUNT_ID").getInt());
                            objNewItem.setAttribute("MAIN_ACCOUNT_CODE",objItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                            objNewItem.setAttribute("SUB_ACCOUNT_CODE",objItem.getAttribute("SUB_ACCOUNT_CODE").getString());
                            objNewItem.setAttribute("AMOUNT",AdjustedAmount);
                            objNewItem.setAttribute("APPLICABLE_AMOUNT",0);
                            objNewItem.setAttribute("PERCENTAGE",0);
                            objNewItem.setAttribute("REMARKS",objItem.getAttribute("REMARKS").getString());
                            objNewItem.setAttribute("CREATED_BY",objItem.getAttribute("CREATED_BY").getString());
                            objNewItem.setAttribute("CREATED_DATE",objItem.getAttribute("CREATED_DATE").getString());
                            objNewItem.setAttribute("MODIFIED_BY",objItem.getAttribute("MODIFIED_BY").getString());
                            objNewItem.setAttribute("MODIFIED_DATE",objItem.getAttribute("MODIFIED_DATE").getString());
                            objNewItem.setAttribute("CANCELLED",objItem.getAttribute("CANCELLED").getInt());
                            objNewItem.setAttribute("CHANGED",objItem.getAttribute("CHANGED").getInt());
                            objNewItem.setAttribute("CHANGED_DATE",objItem.getAttribute("CHANGED_DATE").getString());
                            objNewItem.setAttribute("PO_NO",objItem.getAttribute("PO_NO").getString());
                            objNewItem.setAttribute("PO_DATE",objItem.getAttribute("PO_DATE").getString());
                            objNewItem.setAttribute("INVOICE_NO",SJInvoiceNo);
                            objNewItem.setAttribute("INVOICE_DATE",SJInvoiceDate);
                            objNewItem.setAttribute("VALUE_DATE",ValueDate);
                            if(vType==FinanceGlobal.TYPE_SALES_JOURNAL) {
                                objNewItem.setAttribute("GRN_NO",SJInvoiceNo);
                                objNewItem.setAttribute("GRN_DATE",SJInvoiceDate);
                                objNewItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
                                objNewItem.setAttribute("REF_VOUCHER_NO","");
                            } else {
                                if(clsVoucher.getVoucherModuleID(clsVoucher.getVoucherType(SJNo))==FinanceGlobal.TYPE_LC_JV) {
                                    objNewItem.setAttribute("INVOICE_NO","");
                                    objNewItem.setAttribute("INVOICE_DATE","0000-00-00");
                                }
                                objNewItem.setAttribute("GRN_NO",SJNo);
                                objNewItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(data.getStringValueFromDB("SELECT VOUCHER_DATE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+SJNo+"' ",FinanceGlobal.FinURL)));
                                objNewItem.setAttribute("MODULE_ID",clsVoucher.getVoucherModuleID(clsVoucher.getVoucherType(SJNo)));
                                objNewItem.setAttribute("REF_VOUCHER_NO","");
                            }
                            objNewItem.setAttribute("REF_COMPANY_ID",CompanyID);
                            objNewItem.setAttribute("INVOICE_AMOUNT",SJInvoiceAmount);
                            objNewItem.setAttribute("COMPANY_ID",CompanyID);
                            objNewItem.setAttribute("IS_DEDUCTION",0);
                            objNewItem.setAttribute("DEDUCTION_TYPE",0);
                            objNewItem.setAttribute("LINK_NO",objItem.getAttribute("LINK_NO").getString());
                            objNewItem.setAttribute("MATCHED",0);
                            objNewItem.setAttribute("MATCHED_DATE","0000-00-00");
                            objNewItem.setAttribute("REF_VOUCHER_TYPE",0);
                            objNewItem.setAttribute("REF_VOUCHER_COMPANY_ID",0);
                            objNewItem.setAttribute("REF_SR_NO",0);
                            
                            objJVVoucher.colVoucherItems.put(Integer.toString(objJVVoucher.colVoucherItems.size()+1), objNewItem);
                            break;
                            
                        } else {
                            if(objItem.getAttribute("VALUE_DATE").getString().equals("")) {
                                String VoucherDate = data.getStringValueFromDB("SELECT VOUCHER_DATE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+objItem.getAttribute("VOUCHER_NO").getString()+"' ",FinanceGlobal.FinURL);
                                objItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(VoucherDate));
                            }
                            
                            objItem.setAttribute("INVOICE_NO",SJInvoiceNo);
                            objItem.setAttribute("INVOICE_DATE",SJInvoiceDate);
                            if(vType==FinanceGlobal.TYPE_SALES_JOURNAL) {
                                objItem.setAttribute("GRN_NO",SJInvoiceNo);
                                objItem.setAttribute("GRN_DATE",SJInvoiceDate);
                                objItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
                            } else {
                                if(clsVoucher.getVoucherModuleID(clsVoucher.getVoucherType(SJNo))==FinanceGlobal.TYPE_LC_JV) {
                                    objItem.setAttribute("INVOICE_NO","");
                                    objItem.setAttribute("INVOICE_DATE","0000-00-00");
                                }
                                objItem.setAttribute("GRN_NO",SJNo);
                                objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(data.getStringValueFromDB("SELECT VOUCHER_DATE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+SJNo+"' ",FinanceGlobal.FinURL)));
                                objItem.setAttribute("MODULE_ID",clsVoucher.getVoucherModuleID(clsVoucher.getVoucherType(SJNo)));
                            }
                            
                            objItem.setAttribute("REF_COMPANY_ID",CompanyID);
                            objItem.setAttribute("INVOICE_AMOUNT",SJInvoiceAmount);
                            objItem.setAttribute("MATCHED",0);
                            objItem.setAttribute("MATCHED_DATE","0000-00-00");
                            objItem.setAttribute("REF_VOUCHER_NO","");
                            objItem.setAttribute("REF_VOUCHER_TYPE",0);
                            objItem.setAttribute("REF_VOUCHER_COMPANY_ID",0);
                            objItem.setAttribute("REF_SR_NO",0);
                            
                            objJVVoucher.colVoucherItems.put(Integer.toString(i),objItem);
                            
                            AdjustedAmount-=JVAmount;
                            break;
                        }
                    }
                }
                //Update the voucher
                objJVVoucher.UpdateForAdjustment();
            }
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    public boolean AdjustDummyReceiptAmount(int CompanyID,String VoucherNo,double adjustAmount,String InvoiceNo,String InvoiceDate,String MainCode, String SubCode,int VoucherSrNo) {
        try {
            
            //************** Fetch party information from the SJ ***************//
            double SJInvoiceAmount=0;
            
            String ReceiptNo="";
            double ReceiptAmount=0;
            int ReceiptCompanyID=0;
            
            ReceiptNo=VoucherNo;
            
            clsVoucher objReceiptVoucher=(clsVoucher)(new clsVoucher()).getObject(CompanyID,ReceiptNo);
            for(int i=1;i<=objReceiptVoucher.colVoucherItems.size();i++) {
                clsVoucherItem objItem=(clsVoucherItem)objReceiptVoucher.colVoucherItems.get(Integer.toString(i));
                if(objItem.getAttribute("MAIN_ACCOUNT_CODE").getString().equals(MainCode)&&objItem.getAttribute("SUB_ACCOUNT_CODE").getString().equals(SubCode)&&objItem.getAttribute("SR_NO").getInt()==VoucherSrNo) {
                    //This is the item to be adjusted
                    ReceiptAmount=objItem.getAttribute("AMOUNT").getDouble();
                    
                    if(ReceiptAmount>adjustAmount) {
                        String ValueDate = "";
                        //Split this line in two lines
                        objItem.setAttribute("AMOUNT",ReceiptAmount-adjustAmount);
                        
                        if(objItem.getAttribute("VALUE_DATE").getString().equals("")) {
                            String VoucherDate = data.getStringValueFromDB("SELECT VOUCHER_DATE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+objItem.getAttribute("VOUCHER_NO").getString()+"' ",FinanceGlobal.FinURL);
                            objItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(VoucherDate));
                            ValueDate = EITLERPGLOBAL.formatDate(VoucherDate); //VoucherDate;
                        }  else {
                            ValueDate = objItem.getAttribute("VALUE_DATE").getString();
                        }
                        objReceiptVoucher.colVoucherItems.put(Integer.toString(i),objItem);
                        
                        //Find Next Sr. No.
                        int NewSrNo=data.getIntValueFromDB("SELECT MAX(SR_NO) FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL)+1;
                        
                        //This item to be splitted in two
                        clsVoucherItem objNewItem=new clsVoucherItem();
                        
                        objNewItem.setAttribute("COMPANY_ID",objItem.getAttribute("COMPANY_ID").getInt());
                        objNewItem.setAttribute("VOUCHER_NO",objItem.getAttribute("VOUCHER_NO").getString());
                        objNewItem.setAttribute("SR_NO",NewSrNo);
                        objNewItem.setAttribute("EFFECT",objItem.getAttribute("EFFECT").getString());
                        objNewItem.setAttribute("ACCOUNT_ID",objItem.getAttribute("ACCOUNT_ID").getInt());
                        objNewItem.setAttribute("MAIN_ACCOUNT_CODE",objItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                        objNewItem.setAttribute("SUB_ACCOUNT_CODE",objItem.getAttribute("SUB_ACCOUNT_CODE").getString());
                        objNewItem.setAttribute("AMOUNT",adjustAmount);
                        objNewItem.setAttribute("APPLICABLE_AMOUNT",0);
                        objNewItem.setAttribute("PERCENTAGE",0);
                        objNewItem.setAttribute("REMARKS",objItem.getAttribute("REMARKS").getString());
                        objNewItem.setAttribute("CREATED_BY",objItem.getAttribute("CREATED_BY").getString());
                        objNewItem.setAttribute("CREATED_DATE",objItem.getAttribute("CREATED_DATE").getString());
                        objNewItem.setAttribute("MODIFIED_BY",objItem.getAttribute("MODIFIED_BY").getString());
                        objNewItem.setAttribute("MODIFIED_DATE",objItem.getAttribute("MODIFIED_DATE").getString());
                        objNewItem.setAttribute("CANCELLED",objItem.getAttribute("CANCELLED").getInt());
                        objNewItem.setAttribute("CHANGED",objItem.getAttribute("CHANGED").getInt());
                        objNewItem.setAttribute("CHANGED_DATE",objItem.getAttribute("CHANGED_DATE").getString());
                        objNewItem.setAttribute("PO_NO",objItem.getAttribute("PO_NO").getString());
                        objNewItem.setAttribute("PO_DATE",objItem.getAttribute("PO_DATE").getString());
                        objNewItem.setAttribute("INVOICE_NO",InvoiceNo);
                        objNewItem.setAttribute("INVOICE_DATE",InvoiceDate);
                        objNewItem.setAttribute("VALUE_DATE",ValueDate);
                        objNewItem.setAttribute("GRN_NO","");
                        objNewItem.setAttribute("GRN_DATE","0000-00-00");
                        objNewItem.setAttribute("MODULE_ID",0);
                        objNewItem.setAttribute("REF_COMPANY_ID",0);
                        objNewItem.setAttribute("INVOICE_AMOUNT",0);
                        objNewItem.setAttribute("COMPANY_ID",CompanyID);
                        objNewItem.setAttribute("IS_DEDUCTION",0);
                        objNewItem.setAttribute("DEDUCTION_TYPE",0);
                        objNewItem.setAttribute("REF_VOUCHER_NO","");
                        objNewItem.setAttribute("REF_VOUCHER_TYPE",0);
                        objNewItem.setAttribute("REF_VOUCHER_COMPANY_ID",0);
                        objNewItem.setAttribute("REF_SR_NO",0);
                        objNewItem.setAttribute("LINK_NO",objItem.getAttribute("LINK_NO").getString());
                        objNewItem.setAttribute("MATCHED",0);
                        objNewItem.setAttribute("MATCHED_DATE","0000-00-00");
                        objReceiptVoucher.colVoucherItems.put(Integer.toString(objReceiptVoucher.colVoucherItems.size()+1), objNewItem);
                        
                        
                        int CreditCount = 0;
                        int DebitCount = 0;
                        clsVoucher objNewReceiptVoucher=(clsVoucher)(new clsVoucher()).getObject(CompanyID,ReceiptNo);
                        for(int j=1;j<=objReceiptVoucher.colVoucherItems.size();j++) {
                            objItem=(clsVoucherItem)objReceiptVoucher.colVoucherItems.get(Integer.toString(j));
                            if(objItem.getAttribute("EFFECT").getString().equals("C")) {
                                CreditCount++;
                                objItem.setAttribute("SR_NO",CreditCount);
                                objNewReceiptVoucher.colVoucherItems.put(Integer.toString(CreditCount), objItem);
                            }
                        }
                        DebitCount=CreditCount;
                        for(int j=1;j<=objReceiptVoucher.colVoucherItems.size();j++) {
                            objItem=(clsVoucherItem)objReceiptVoucher.colVoucherItems.get(Integer.toString(j));
                            if(objItem.getAttribute("EFFECT").getString().equals("D")) {
                                DebitCount++;
                                objItem.setAttribute("SR_NO",DebitCount);
                                objNewReceiptVoucher.colVoucherItems.put(Integer.toString(DebitCount), objItem);
                            }
                        }
                        objReceiptVoucher = objNewReceiptVoucher;
                        objNewReceiptVoucher=null;
                        break;
                        
                    } else {
                        if(objItem.getAttribute("VALUE_DATE").getString().equals("")) {
                            String VoucherDate = data.getStringValueFromDB("SELECT VOUCHER_DATE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+objItem.getAttribute("VOUCHER_NO").getString()+"' ",FinanceGlobal.FinURL);
                            objItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(VoucherDate));
                        }
                        
                        objItem.setAttribute("INVOICE_NO",InvoiceNo);
                        objItem.setAttribute("INVOICE_DATE",InvoiceDate);
                        objItem.setAttribute("GRN_NO","");
                        objItem.setAttribute("GRN_DATE","0000-00-00");
                        objItem.setAttribute("MODULE_ID",0);
                        objItem.setAttribute("REF_COMPANY_ID",0);
                        objItem.setAttribute("INVOICE_AMOUNT",SJInvoiceAmount);
                        objItem.setAttribute("MATCHED",0);
                        objItem.setAttribute("MATCHED_DATE","0000-00-00");
                        objItem.setAttribute("REF_VOUCHER_NO","");
                        objItem.setAttribute("REF_VOUCHER_TYPE",0);
                        objItem.setAttribute("REF_VOUCHER_COMPANY_ID",0);
                        objItem.setAttribute("REF_SR_NO",0);
                        
                        objReceiptVoucher.colVoucherItems.put(Integer.toString(i),objItem);
                        
                        adjustAmount-=ReceiptAmount;
                        break;
                    }
                }
            }
            //Update the voucher
            objReceiptVoucher.UpdateForAdjustment();
            
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    public boolean AdjustDummyJournalAmount(int CompanyID,String VoucherNo,double adjustAmount,String InvoiceNo,String InvoiceDate,String MainCode, String SubCode,int VoucherSrNo) {
        try {
            
            //************** Fetch party information from the SJ ***************//
            double SJInvoiceAmount=0;
            
            String JVNo="";
            double JVAmount=0;
            int JVCompanyID=0;
            
            JVNo=VoucherNo;
            
            clsVoucher objJVVoucher=(clsVoucher)(new clsVoucher()).getObject(CompanyID,JVNo);
            for(int i=1;i<=objJVVoucher.colVoucherItems.size();i++) {
                clsVoucherItem objItem=(clsVoucherItem)objJVVoucher.colVoucherItems.get(Integer.toString(i));
                if(objItem.getAttribute("MAIN_ACCOUNT_CODE").getString().equals(MainCode)&&objItem.getAttribute("SUB_ACCOUNT_CODE").getString().equals(SubCode)&&objItem.getAttribute("SR_NO").getInt()==VoucherSrNo) {
                    //This is the item to be adjusted
                    JVAmount=objItem.getAttribute("AMOUNT").getDouble();
                    
                    if(JVAmount>adjustAmount) {
                        String ValueDate = "";
                        //Split this line in two lines
                        objItem.setAttribute("AMOUNT",JVAmount-adjustAmount);
                        
                        if(objItem.getAttribute("VALUE_DATE").getString().equals("")) {
                            String VoucherDate = data.getStringValueFromDB("SELECT VOUCHER_DATE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+objItem.getAttribute("VOUCHER_NO").getString()+"' ",FinanceGlobal.FinURL);
                            objItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(VoucherDate));
                            ValueDate = EITLERPGLOBAL.formatDate(VoucherDate); //VoucherDate;
                        } else {
                            ValueDate = objItem.getAttribute("VALUE_DATE").getString();
                        }
                        objJVVoucher.colVoucherItems.put(Integer.toString(i),objItem);
                        
                        //Find Next Sr. No.
                        int NewSrNo=data.getIntValueFromDB("SELECT MAX(SR_NO) FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+JVNo+"'",FinanceGlobal.FinURL)+1;
                        
                        //This item to be splitted in two
                        clsVoucherItem objNewItem=new clsVoucherItem();
                        
                        objNewItem.setAttribute("COMPANY_ID",objItem.getAttribute("COMPANY_ID").getInt());
                        objNewItem.setAttribute("VOUCHER_NO",objItem.getAttribute("VOUCHER_NO").getString());
                        objNewItem.setAttribute("SR_NO",NewSrNo);
                        objNewItem.setAttribute("EFFECT",objItem.getAttribute("EFFECT").getString());
                        objNewItem.setAttribute("ACCOUNT_ID",objItem.getAttribute("ACCOUNT_ID").getInt());
                        objNewItem.setAttribute("MAIN_ACCOUNT_CODE",objItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                        objNewItem.setAttribute("SUB_ACCOUNT_CODE",objItem.getAttribute("SUB_ACCOUNT_CODE").getString());
                        objNewItem.setAttribute("AMOUNT",adjustAmount);
                        objNewItem.setAttribute("APPLICABLE_AMOUNT",0);
                        objNewItem.setAttribute("PERCENTAGE",0);
                        objNewItem.setAttribute("REMARKS",objItem.getAttribute("REMARKS").getString());
                        objNewItem.setAttribute("CREATED_BY",objItem.getAttribute("CREATED_BY").getString());
                        objNewItem.setAttribute("CREATED_DATE",objItem.getAttribute("CREATED_DATE").getString());
                        objNewItem.setAttribute("MODIFIED_BY",objItem.getAttribute("MODIFIED_BY").getString());
                        objNewItem.setAttribute("MODIFIED_DATE",objItem.getAttribute("MODIFIED_DATE").getString());
                        objNewItem.setAttribute("CANCELLED",objItem.getAttribute("CANCELLED").getInt());
                        objNewItem.setAttribute("CHANGED",objItem.getAttribute("CHANGED").getInt());
                        objNewItem.setAttribute("CHANGED_DATE",objItem.getAttribute("CHANGED_DATE").getString());
                        objNewItem.setAttribute("PO_NO",objItem.getAttribute("PO_NO").getString());
                        objNewItem.setAttribute("PO_DATE",objItem.getAttribute("PO_DATE").getString());
                        objNewItem.setAttribute("INVOICE_NO",InvoiceNo);
                        objNewItem.setAttribute("INVOICE_DATE",InvoiceDate);
                        objNewItem.setAttribute("VALUE_DATE",ValueDate);
                        objNewItem.setAttribute("GRN_NO","");
                        objNewItem.setAttribute("GRN_DATE","0000-00-00");
                        objNewItem.setAttribute("MODULE_ID",0);
                        objNewItem.setAttribute("REF_COMPANY_ID",0);
                        objNewItem.setAttribute("INVOICE_AMOUNT",0);
                        objNewItem.setAttribute("COMPANY_ID",CompanyID);
                        objNewItem.setAttribute("IS_DEDUCTION",0);
                        objNewItem.setAttribute("DEDUCTION_TYPE",0);
                        objNewItem.setAttribute("REF_VOUCHER_NO","");
                        objNewItem.setAttribute("REF_VOUCHER_TYPE",0);
                        objNewItem.setAttribute("REF_VOUCHER_COMPANY_ID",0);
                        objNewItem.setAttribute("REF_SR_NO",0);
                        objNewItem.setAttribute("LINK_NO",objItem.getAttribute("LINK_NO").getString());
                        objNewItem.setAttribute("MATCHED",0);
                        objNewItem.setAttribute("MATCHED_DATE","0000-00-00");
                        objJVVoucher.colVoucherItems.put(Integer.toString(objJVVoucher.colVoucherItems.size()+1), objNewItem);
                        break;
                        
                    } else {
                        if(objItem.getAttribute("VALUE_DATE").getString().equals("")) {
                            String VoucherDate = data.getStringValueFromDB("SELECT VOUCHER_DATE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+objItem.getAttribute("VOUCHER_NO").getString()+"' ",FinanceGlobal.FinURL);
                            objItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(VoucherDate));
                        }
                        
                        objItem.setAttribute("INVOICE_NO",InvoiceNo);
                        objItem.setAttribute("INVOICE_DATE",InvoiceDate);
                        objItem.setAttribute("GRN_NO","");
                        objItem.setAttribute("GRN_DATE","0000-00-00");
                        objItem.setAttribute("MODULE_ID",0);
                        objItem.setAttribute("REF_COMPANY_ID",0);
                        objItem.setAttribute("INVOICE_AMOUNT",SJInvoiceAmount);
                        objItem.setAttribute("MATCHED",0);
                        objItem.setAttribute("MATCHED_DATE","0000-00-00");
                        objItem.setAttribute("REF_VOUCHER_NO","");
                        objItem.setAttribute("REF_VOUCHER_TYPE",0);
                        objItem.setAttribute("REF_VOUCHER_COMPANY_ID",0);
                        objItem.setAttribute("REF_SR_NO",0);
                        
                        objJVVoucher.colVoucherItems.put(Integer.toString(i),objItem);
                        
                        adjustAmount-=JVAmount;
                        break;
                    }
                }
            }
            //Update the voucher
            objJVVoucher.UpdateForAdjustment();
            
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    public void AdjustAllReceiptAmounts() {
        try {
            for(int i=1;i<=colItems.size();i++) {
                clsDrAdjustmentItem objItem=(clsDrAdjustmentItem)colItems.get(Integer.toString(i));
                String ReceiptVoucherNo=objItem.getAttribute("VOUCHER_NO").getString();
                String SJNo=objItem.getAttribute("SJ_NO").getString();
                double AdjustAmount=objItem.getAttribute("ADJUST_AMOUNT").getDouble();
                String InvoiceNo=objItem.getAttribute("INVOICE_NO").getString();
                String InvoiceDate=objItem.getAttribute("INVOICE_DATE").getString();
                int CompanyID=getAttribute("COMPANY_ID").getInt();
                int ReceiptVoucherType = clsVoucher.getVoucherType(ReceiptVoucherNo);
                String MainCode=objItem.getAttribute("MAIN_ACCOUNT_CODE").getString();
                String SubCode=objItem.getAttribute("SUB_ACCOUNT_CODE").getString();
                int SelectionType = objItem.getAttribute("SELECTION_TYPE").getInt();
                int VoucherType = clsVoucher.getVoucherType(ReceiptVoucherNo);
                int VoucherSrNoForDummy = objItem.getAttribute("VOUCHER_SR_NO").getInt();
                if(SelectionType!=3) {
                    while(AdjustAmount>0) {
                        String strSQL = "SELECT B.SR_NO FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B " +
                        "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND B.MAIN_ACCOUNT_CODE = '"+MainCode+"' " +
                        "AND B.SUB_ACCOUNT_CODE='"+SubCode+"' AND A.APPROVED=1 AND A.CANCELLED=0 AND B.EFFECT='C' " +
                    //    "AND (B.INVOICE_NO ='' OR B.INVOICE_NO LIKE 'DUM%') AND B.GRN_NO ='' " +
                        "AND ((B.INVOICE_NO ='' OR B.INVOICE_NO LIKE 'DUM%') OR BOOK_CODE =14) AND B.GRN_NO ='' " +
                        "AND B.MODULE_ID <>"+clsVoucher.DebitNoteModuleID+" AND (B.MATCHED=0 OR B.MATCHED IS NULL) " +
                        "AND A.VOUCHER_NO='"+ReceiptVoucherNo+"' " +
                        "ORDER BY B.INVOICE_NO DESC,B.SR_NO";
                        int VoucherSrNo = data.getIntValueFromDB(strSQL,FinanceGlobal.FinURL);
                        
                        strSQL = "SELECT B.AMOUNT FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B " +
                        "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND B.MAIN_ACCOUNT_CODE = '"+MainCode+"' " +
                        "AND B.SUB_ACCOUNT_CODE='"+SubCode+"' AND A.APPROVED=1 AND A.CANCELLED=0 AND B.EFFECT='C' " +
                        "AND ((B.INVOICE_NO ='' OR B.INVOICE_NO LIKE 'DUM%') OR BOOK_CODE =14) AND B.GRN_NO ='' AND B.MODULE_ID <>"+clsVoucher.DebitNoteModuleID+" " +
                        
                        "AND A.VOUCHER_NO='"+ReceiptVoucherNo+"' AND B.SR_NO="+VoucherSrNo+" AND (B.MATCHED=0 OR B.MATCHED IS NULL) ";
                        double AmountCanAdjust =  data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                        
                        if(AdjustAmount > AmountCanAdjust) {
                            if(VoucherType==FinanceGlobal.TYPE_RECEIPT || VoucherType==FinanceGlobal.TYPE_RECEIPT_TDS || VoucherType == FinanceGlobal.TYPE_CASH_RECEIPT_VOUCHER) {
                                AdjustReceiptAmount(CompanyID, SJNo, ReceiptVoucherNo, AmountCanAdjust,VoucherSrNo,SubCode);
                            }
                            if(VoucherType==FinanceGlobal.TYPE_JOURNAL || VoucherType==FinanceGlobal.TYPE_CREDIT_NOTE) {
                                AdjustJournalAmount(CompanyID, SJNo, ReceiptVoucherNo, AmountCanAdjust,VoucherSrNo,SubCode);
                            }
                            AdjustAmount = AdjustAmount - AmountCanAdjust;
                        } else {
                            if(VoucherType==FinanceGlobal.TYPE_RECEIPT || VoucherType==FinanceGlobal.TYPE_RECEIPT_TDS || VoucherType == FinanceGlobal.TYPE_CASH_RECEIPT_VOUCHER) {
                                AdjustReceiptAmount(CompanyID, SJNo, ReceiptVoucherNo, AdjustAmount,VoucherSrNo,SubCode);
                            }
                            if(VoucherType==FinanceGlobal.TYPE_JOURNAL || VoucherType==FinanceGlobal.TYPE_CREDIT_NOTE) {
                                AdjustJournalAmount(CompanyID, SJNo, ReceiptVoucherNo, AdjustAmount,VoucherSrNo,SubCode);
                            }
                            AdjustAmount = 0;
                        }
                    }
                } else {
                    if(ReceiptVoucherType==FinanceGlobal.TYPE_RECEIPT || VoucherType==FinanceGlobal.TYPE_RECEIPT_TDS || ReceiptVoucherType == FinanceGlobal.TYPE_CASH_RECEIPT_VOUCHER || ReceiptVoucherType==FinanceGlobal.TYPE_CREDIT_NOTE) {
                        AdjustDummyReceiptAmount(CompanyID,ReceiptVoucherNo, AdjustAmount,InvoiceNo,InvoiceDate,MainCode,SubCode,VoucherSrNoForDummy);
                    }
                    if(ReceiptVoucherType==FinanceGlobal.TYPE_JOURNAL) {
                        AdjustDummyJournalAmount(CompanyID,ReceiptVoucherNo, AdjustAmount,InvoiceNo,InvoiceDate,MainCode,SubCode,VoucherSrNoForDummy);
                    }
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void AdjustReceiptFromJV(String JVNo) {
        try {
            String AgentMainCode="";
            String AgentCode="";
            String ReceiptVoucherNo="";
            int ReceiptCompanyID=0;
            
            clsVoucher objJV=(clsVoucher)(new clsVoucher()).getObject(clsVoucher.getVoucherCompanyID(JVNo),JVNo);
            
            AgentMainCode=objJV.objPayment.getAttribute("MAIN_ACCOUNT_CODE").getString();
            AgentCode=objJV.objPayment.getAttribute("PARTY_CODE").getString();
            
            for(int i=1;i<=objJV.objPayment.colVoucherItems.size();i++) {
                
                clsVoucherItem objItem=(clsVoucherItem)objJV.objPayment.colVoucherItems.get(Integer.toString(i));
                
                //------------------------------------------------------------------------------------------//
                //********************* Find Receipt voucher and adjust it *********************************//
                //------------------------------------------------------------------------------------------//
                ReceiptVoucherNo=objItem.getAttribute("GRN_NO").getString();
                ReceiptCompanyID=objItem.getAttribute("REF_COMPANY_ID").getInt();
                
                clsVoucher objReceipt=(clsVoucher)(new clsVoucher()).getObject(ReceiptCompanyID,ReceiptVoucherNo);
                
                for(int j=1;j<=objReceipt.objPayment.colVoucherItems.size();j++) {
                    
                    clsVoucherItem objRItem=(clsVoucherItem)objReceipt.objPayment.colVoucherItems.get(Integer.toString(j));
                    if(objRItem.getAttribute("MAIN_ACCOUNT_CODE").getString().equals(AgentMainCode)&&objRItem.getAttribute("SUB_ACCOUNT_CODE").getString().equals(AgentCode)&&objRItem.getAttribute("INVOICE_NO").getString().equals("")) {
                        //Advance Receipt amount is greater than adjusted amount
                        if(objRItem.getAttribute("AMOUNT").getDouble()>objItem.getAttribute("AMOUNT").getDouble()) {
                            
                            double ReceiptAmount=objRItem.getAttribute("AMOUNT").getDouble();
                            
                            //Find Next Sr. No.
                            int NewSrNo=data.getIntValueFromDB("SELECT MAX(SR_NO) FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+objReceipt.getAttribute("COMPANY_ID").getInt()+" AND VOUCHER_NO='"+objReceipt.getAttribute("VOUCHER_NO").getString()+"'",FinanceGlobal.FinURL)+1;
                            
                            //This item to be splitted in two
                            clsVoucherItem objNewItem=new clsVoucherItem();
                            
                            objNewItem.setAttribute("COMPANY_ID",objRItem.getAttribute("COMPANY_ID").getInt());
                            objNewItem.setAttribute("VOUCHER_NO",objRItem.getAttribute("VOUCHER_NO").getString());
                            objNewItem.setAttribute("SR_NO",NewSrNo);
                            objNewItem.setAttribute("EFFECT",objRItem.getAttribute("EFFECT").getString());
                            objNewItem.setAttribute("ACCOUNT_ID",objRItem.getAttribute("ACCOUNT_ID").getInt());
                            objNewItem.setAttribute("MAIN_ACCOUNT_CODE",objRItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                            objNewItem.setAttribute("SUB_ACCOUNT_CODE",objRItem.getAttribute("SUB_ACCOUNT_CODE").getString());
                            objNewItem.setAttribute("AMOUNT",objItem.getAttribute("AMOUNT").getDouble());
                            objNewItem.setAttribute("REMARKS",objRItem.getAttribute("REMARKS").getString());
                            objNewItem.setAttribute("CREATED_BY",objRItem.getAttribute("CREATED_BY").getString());
                            objNewItem.setAttribute("CREATED_DATE",objRItem.getAttribute("CREATED_DATE").getString());
                            objNewItem.setAttribute("MODIFIED_BY",objRItem.getAttribute("MODIFIED_BY").getString());
                            objNewItem.setAttribute("MODIFIED_DATE",objRItem.getAttribute("MODIFIED_DATE").getString());
                            objNewItem.setAttribute("CANCELLED",objRItem.getAttribute("CANCELLED").getInt());
                            objNewItem.setAttribute("CHANGED",objRItem.getAttribute("CHANGED").getInt());
                            objNewItem.setAttribute("CHANGED_DATE",objRItem.getAttribute("CHANGED_DATE").getString());
                            objNewItem.setAttribute("PO_NO","");
                            objNewItem.setAttribute("PO_DATE","0000-00-00");
                            objNewItem.setAttribute("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                            objNewItem.setAttribute("INVOICE_DATE",objItem.getAttribute("INVOICE_DATE").getString());
                            objNewItem.setAttribute("GRN_NO",objItem.getAttribute("INVOICE_NO").getString());
                            objNewItem.setAttribute("GRN_DATE",objItem.getAttribute("INVOICE_DATE").getString());
                            objNewItem.setAttribute("REF_MODULE_ID",clsSalesInvoice.ModuleID);
                            objNewItem.setAttribute("REF_COMPANY_ID",objReceipt.getAttribute("COMPANY_ID").getInt());
                            objNewItem.setAttribute("INVOICE_AMOUNT",clsSalesInvoice.getInvoiceTotal(objReceipt.getAttribute("COMPANY_ID").getInt(),objItem.getAttribute("INVOICE_NO").getString(),objItem.getAttribute("INVOICE_DATE").getString()));
                            objNewItem.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                            objNewItem.setAttribute("IS_DEDUCTION",0);
                            objNewItem.setAttribute("DEDUCTION_TYPE",0);
                            
                            objReceipt.colVoucherItems.put(Integer.toString(objReceipt.colVoucherItems.size()+1),objNewItem);
                            
                            
                            //Decrease the amount of original item
                            objRItem.setAttribute("AMOUNT",ReceiptAmount-objItem.getAttribute("AMOUNT").getDouble());
                            
                            //Now update it
                            objReceipt.colVoucherItems.remove(Integer.toString(j));
                            objReceipt.colVoucherItems.put(Integer.toString(j),objRItem);
                            
                        }
                        
                        //Advance Receipt amount is equal to adjusted amount
                        if(objRItem.getAttribute("AMOUNT").getDouble()==objItem.getAttribute("AMOUNT").getDouble()) {
                            //Don't split the item, update invoice no. in this line
                            objRItem.setAttribute("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                            objRItem.setAttribute("INVOICE_DATE",objItem.getAttribute("INVOICE_DATE").getString());
                            objRItem.setAttribute("GRN_NO",objItem.getAttribute("INVOICE_NO").getString());
                            objRItem.setAttribute("GRN_DATE",objItem.getAttribute("INVOICE_DATE").getString());
                            objRItem.setAttribute("REF_MODULE_ID",clsSalesInvoice.ModuleID);
                            objRItem.setAttribute("REF_COMPANY_ID",objReceipt.getAttribute("COMPANY_ID").getInt());
                            objRItem.setAttribute("INVOICE_AMOUNT",clsSalesInvoice.getInvoiceTotal(objReceipt.getAttribute("COMPANY_ID").getInt(),objItem.getAttribute("INVOICE_NO").getString(),objItem.getAttribute("INVOICE_DATE").getString()));
                            objRItem.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                            
                            //Now update it
                            objReceipt.colVoucherItems.remove(Integer.toString(j));
                            objReceipt.colVoucherItems.put(Integer.toString(j),objRItem);
                        }
                    }
                }
                
                objReceipt.UpdateForAdjustment();
                //------------------------------------------------------------------------------------------//
                //------------------------------------------------------------------------------------------//
            }
        } catch(Exception e) {
        }
    }
    
    private void PostDebitNote(String DocNo) {
        ResultSet rsInvoice=null,rsVoucher=null; //rsParty=null,
        clsVoucher ObjVoucher = null;
        clsDebitNoteReceiptMapping ObjRefItem=null;
        String CurrentVoucherDate = "";
        try {
            String ChargeCode="",DeductionCode="",DebitNoteBookCode="",InvoiceDueDate="",SubAccountCode="",MainAccountCode="",InvoiceNo="",InvoiceDate="";
            int InvoiceType = 0;
            double PartyDebitNoteAmount=0,InvoiceDebitNoteAmount = 0,VoucherDebitNoteAmount=0, InterestPercentage=0, InvoicePaidAmount=0,InvoiceAmount=0;
            
            SubAccountCode = data.getStringValueFromDB("SELECT SUB_ACCOUNT_CODE FROM D_FIN_DR_ADJ_DETAIL WHERE DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
            MainAccountCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_DR_ADJ_DETAIL WHERE DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
            CurrentVoucherDate = data.getStringValueFromDB("SELECT DOC_DATE FROM D_FIN_DR_ADJ_HEADER WHERE DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
            String strSQL = "SELECT DISTINCT MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE,INVOICE_NO,INVOICE_DATE " +
            "FROM D_FIN_DR_ADJ_DETAIL WHERE DOC_NO='"+DocNo+"' " +
            "AND MAIN_ACCOUNT_CODE IN ('210010','210027') AND INVOICE_NO<>'' ";
            rsInvoice = data.getResult(strSQL, FinanceGlobal.FinURL);
            rsInvoice.first();
            ObjRefItem = new clsDebitNoteReceiptMapping();
            ObjRefItem.colMappingDetail.clear();
            if(rsInvoice.getRow() > 0) {
                while(!rsInvoice.isAfterLast()) {
                    InvoiceNo = UtilFunctions.getString(rsInvoice, "INVOICE_NO", "");
                    InvoiceDate = UtilFunctions.getString(rsInvoice, "INVOICE_DATE","0000-00-00");
                    InvoiceType = clsSalesInvoice.getInvoiceType(InvoiceNo, InvoiceDate);
                    ChargeCode = clsSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate);
                    InvoiceAmount = clsSalesInvoice.getInvoiceAmount(InvoiceNo,InvoiceDate);
                    InvoicePaidAmount = clsSalesInvoice.getInvoicePaidAmount(InvoiceNo,InvoiceDate);
                    if(InvoiceAmount > InvoicePaidAmount) { // by pass the invoice if invoice in not fully paid for debit note
                        rsInvoice.next();
                        continue;
                    }
                    if(!clsSalesInvoice.canDebitNotePost(InvoiceType,ChargeCode)) { // by pass the invoice if invoice in not of 2,8 for felt or not of 2,4,8 for suiting
                        rsInvoice.next();
                        continue;
                    }
//                    if(InvoiceType==2 && (SubAccountCode.equals("812081")||SubAccountCode.equals("813003")||SubAccountCode.equals("828001")||SubAccountCode.equals("828003"))) {
                    if(InvoiceType==2 && (SubAccountCode.equals("813003")||SubAccountCode.equals("828001")||SubAccountCode.equals("828003"))) {
                        rsInvoice.next();
                        continue;
                    }
                    InvoiceDueDate = data.getStringValueFromDB("SELECT DUE_DATE FROM DINESHMILLS.D_SAL_INVOICE_HEADER WHERE INVOICE_NO='"+InvoiceNo+"' AND INVOICE_DATE='"+InvoiceDate+"' AND APPROVED=1 AND CANCELLED=0 ");
                    
                    
                    //CHANGE BY RISHI ACCORDING TO ATUL 
                        
                        
                     //   if(InvoiceType==2 && ChargeCode.equals("82")){
                      //      InvoiceDueDate = data.getStringValueFromDB("SELECT ADDDATE(DUE_DATE,6) FROM DINESHMILLS.D_SAL_INVOICE_HEADER WHERE INVOICE_NO='"+InvoiceNo+"' AND INVOICE_DATE='"+InvoiceDate+"' AND APPROVED=1 AND CANCELLED=0 AND INVOICE_TYPE =2 AND SUBSTRING(PAYMENT_TERM_CODE,1,1) =8 ");
                     //   }
                               
                    
                    
                    if(InvoiceDueDate.equals("")) {
                        JOptionPane.showMessageDialog(null,"Due Date not exists in Invoice No : " + InvoiceNo + " Invoice Date : " + EITLERPGLOBAL.formatDate(InvoiceDate) + " Party Code : " + SubAccountCode + "\n Contact Account and Administrator.");
                        rsInvoice.next();
                        continue;
                    }
                    
                    InvoiceDebitNoteAmount = 0;
                    strSQL = "SELECT A.VOUCHER_NO, B.AMOUNT, B.VALUE_DATE FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B " +
                    "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND B.INVOICE_NO='"+InvoiceNo+"' AND B.INVOICE_DATE='"+InvoiceDate+"' " +
                    "AND B.EFFECT='C' AND B.SUB_ACCOUNT_CODE='"+SubAccountCode+"' AND B.MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND A.APPROVED=1 " +
                    "AND A.CANCELLED=0 ORDER BY A.VOUCHER_DATE ";
                    rsVoucher = data.getResult(strSQL,FinanceGlobal.FinURL);
                    rsVoucher.first();
                    // All Credit Record for Effect C & Same Party for same Invoice
                    
                    while(!rsVoucher.isAfterLast()) {
                        
                        String nVoucherNo = UtilFunctions.getString(rsVoucher,"VOUCHER_NO", "");
                        strSQL = "SELECT * FROM D_FIN_DEBITNOTE_RECEIPT_MAPPING WHERE RECEIPT_VOUCHER_NO='"+nVoucherNo+"' " +
                        "AND INVOICE_NO='"+InvoiceNo+"' AND INVOICE_DATE='"+InvoiceDate+"' AND APPROVED=1 AND CANCELLED=0 ";
                        if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                            rsInvoice.next();
                            continue;
                        }
                        double VoucherAmount = UtilFunctions.getDouble(rsVoucher,"AMOUNT", 0);
                        String ValueDate =  UtilFunctions.getString(rsVoucher,"VALUE_DATE","0000-00-00");
                        if(ValueDate.equals("") || ValueDate.equals("0000-00-00")) {
                            JOptionPane.showMessageDialog(null,"Value Date not exists in Voucher No : " + nVoucherNo + " against Invoice No : " + InvoiceNo + " Invoice Date : " + EITLERPGLOBAL.formatDate(InvoiceDate) + " Party Code : " + SubAccountCode  + "\nDebit note can not post.\nContact Account and Administrator.");
                        }
                        if(java.sql.Date.valueOf(ValueDate).before(java.sql.Date.valueOf(InvoiceDueDate)) || java.sql.Date.valueOf(ValueDate).compareTo(java.sql.Date.valueOf(InvoiceDueDate))==0) {
                            rsVoucher.next();
                            continue;
                        }
                        VoucherDebitNoteAmount = 0;
                        // calculate debit note amount start
                        int InterestDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(InvoiceDueDate),java.sql.Date.valueOf(ValueDate));
                        InterestPercentage = clsSalesInvoice.getDebitNotePercentage(ValueDate,InvoiceType,ChargeCode,SubAccountCode);
                        if(InvoiceType==1 && java.sql.Date.valueOf(InvoiceDate).after(java.sql.Date.valueOf("2012-12-31")) && SubAccountCode.equals("300170")) {
                            InterestPercentage = 15;
                        }
                        VoucherDebitNoteAmount = EITLERPGLOBAL.round(((VoucherAmount * InterestPercentage * InterestDays)/(365* 100)),2);
                        // calculate debit note amount end
                        clsDebitNoteReceiptMapping ObjtempItem = new clsDebitNoteReceiptMapping();
                        ObjtempItem.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                        ObjtempItem.setAttribute("SR_NO",(ObjRefItem.colMappingDetail.size()+1));
                        ObjtempItem.setAttribute("DEBITNOTE_VOUCHER_NO","");
                        ObjtempItem.setAttribute("RECEIPT_VOUCHER_NO",nVoucherNo);
                        ObjtempItem.setAttribute("INVOICE_NO",InvoiceNo);
                        ObjtempItem.setAttribute("INVOICE_DATE",InvoiceDate);
                        ObjtempItem.setAttribute("INVOICE_DUE_DATE",InvoiceDueDate);
                        ObjtempItem.setAttribute("VALUE_DATE",ValueDate);
                        ObjtempItem.setAttribute("DAYS",InterestDays);
                        ObjtempItem.setAttribute("DEBIT_NOTE_AMOUNT",VoucherDebitNoteAmount);
                        ObjtempItem.setAttribute("APPROVED",1);
                        ObjtempItem.setAttribute("APPROVED",EITLERPGLOBAL.getCurrentDateDB());
                        ObjtempItem.setAttribute("CHANGED",1);
                        ObjRefItem.colMappingDetail.put(Integer.toString(ObjRefItem.colMappingDetail.size()+1),ObjtempItem);
                        
                        InvoiceDebitNoteAmount = EITLERPGLOBAL.round(InvoiceDebitNoteAmount + VoucherDebitNoteAmount,2);
                        rsVoucher.next();
                    } // end while
                    
                    PartyDebitNoteAmount = EITLERPGLOBAL.round(PartyDebitNoteAmount + InvoiceDebitNoteAmount,2);
                    rsInvoice.next();
                }
                
                if(EITLERPGLOBAL.round(PartyDebitNoteAmount,0) > 5 ) {
                    
                    /*========== Select the Hierarchy ======== */
                    int HierarchyID = 0;
                    HashMap List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsVoucher.DebitNoteModuleID, "CHOOSE_HIERARCHY", "DEFAULT", "");
                    if(List.size()>0) {
                        //Get the Result of the Rule which would be the hierarchy no.
                        clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                        HierarchyID=UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                    }
                    /*========== End of Hierarchy Selection ======== */
                    
                    // get Book code and deduction code start
                    List.clear();
                    List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsVoucher.DebitNoteModuleID, "GET_BOOK_CODE",ChargeCode.substring(0,1), Integer.toString(InvoiceType));
                    if(List.size()>0) {
                        //Get the Result of the Rule which would be the hierarchy no.
                        clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                        DebitNoteBookCode = objRule.getAttribute("RULE_OUTCOME").getString();
                    }
                    
                    List.clear();
                    List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsVoucher.DebitNoteModuleID , "GET_ACCOUNT_CODE", "DEDUCTION_CODE", Integer.toString(InvoiceType));
                    if(List.size()>0) {
                        clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                        DeductionCode = objRule.getAttribute("RULE_OUTCOME").getString();
                    }
                    
                    ResultSet rsTmp=data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID="+clsVoucher.DebitNoteModuleID);
                    rsTmp.first();
                    String SelPrefix="",SelSuffix="";
                    int FFNo=0;
                    if(rsTmp.getRow()>0) {
                        SelPrefix=rsTmp.getString("PREFIX_CHARS");
                        SelSuffix=rsTmp.getString("SUFFIX_CHARS");
                        FFNo=rsTmp.getInt("FIRSTFREE_NO");
                    }
                    
                    int VoucherSrNo = 0;
                    ObjVoucher = new clsVoucher();
                    ObjVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
                    ObjVoucher.colVoucherItems.clear();
                    ObjVoucher.setAttribute("PREFIX",SelPrefix);
                    ObjVoucher.setAttribute("SUFFIX",SelSuffix);
                    ObjVoucher.setAttribute("FFNO",FFNo);
                    ObjVoucher.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    ObjVoucher.setAttribute("VOUCHER_NO","");
                    ObjVoucher.setAttribute("LEGACY_NO","");
                    ObjVoucher.setAttribute("LEGACY_DATE","0000-00-00");
                    ObjVoucher.setAttribute("BOOK_CODE",DebitNoteBookCode);
                    ObjVoucher.setAttribute("VOUCHER_TYPE",FinanceGlobal.TYPE_DEBIT_NOTE);
                    ObjVoucher.setAttribute("CHEQUE_NO","");
                    ObjVoucher.setAttribute("CHEQUE_DATE","0000-00-00");
                    ObjVoucher.setAttribute("BANK_NAME","");
                    //ObjVoucher.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.getCurrentDate());
                    ObjVoucher.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(CurrentVoucherDate));
                    ObjVoucher.setAttribute("ST_CATEGORY","");
                    ObjVoucher.setAttribute("MODULE_ID",0);
                    ObjVoucher.setAttribute("REMARKS","Generated By Auto Debit Note System.");
                    ObjVoucher.setAttribute("HIERARCHY_ID",HierarchyID);
                    int FirstUserID=data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID="+HierarchyID+" AND SR_NO=1");
                    ObjVoucher.setAttribute("FROM",FirstUserID);
                    ObjVoucher.setAttribute("TO",FirstUserID);
                    ObjVoucher.setAttribute("FROM_REMARKS","");
                    ObjVoucher.setAttribute("APPROVAL_STATUS","F"); //Final Approved --> Voucher
                    
                    clsVoucherItem objVoucherItem=new clsVoucherItem();
                    VoucherSrNo++;
                    objVoucherItem.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    objVoucherItem.setAttribute("VOUCHER_NO","");
                    objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                    objVoucherItem.setAttribute("EFFECT","C");
                    objVoucherItem.setAttribute("ACCOUNT_ID",1);
                    objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",DeductionCode);
                    objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
                    objVoucherItem.setAttribute("AMOUNT",EITLERPGLOBAL.round(PartyDebitNoteAmount,0));
                    objVoucherItem.setAttribute("APPLICABLE_AMOUNT",0);
                    objVoucherItem.setAttribute("PERCENTAGE",0);
                    objVoucherItem.setAttribute("REMARKS","");
                    objVoucherItem.setAttribute("PO_NO","");
                    objVoucherItem.setAttribute("PO_DATE","0000-00-00");
                    objVoucherItem.setAttribute("VALUE_DATE","0000-00-00");
                    objVoucherItem.setAttribute("INVOICE_NO","");
                    objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
                    objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
                    objVoucherItem.setAttribute("LINK_NO","");
                    objVoucherItem.setAttribute("GRN_NO","");
                    objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
                    objVoucherItem.setAttribute("MODULE_ID",0);
                    objVoucherItem.setAttribute("IS_DEDUCTION",0);
                    objVoucherItem.setAttribute("REF_VOUCHER_NO","");
                    objVoucherItem.setAttribute("REF_VOUCHER_TYPE",0);
                    objVoucherItem.setAttribute("REF_VOUCHER_COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    objVoucherItem.setAttribute("MATCHED",0);
                    objVoucherItem.setAttribute("MATCHED_DATE","0000-00-00");
                    objVoucherItem.setAttribute("REF_SR_NO",0);
                    ObjVoucher.colVoucherItems.put(Integer.toString(ObjVoucher.colVoucherItems.size()+1),objVoucherItem);
                    
                    objVoucherItem=new clsVoucherItem();
                    VoucherSrNo++;
                    objVoucherItem.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    objVoucherItem.setAttribute("VOUCHER_NO","");
                    objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                    objVoucherItem.setAttribute("EFFECT","D");
                    objVoucherItem.setAttribute("ACCOUNT_ID",1);
                    if(InvoiceType==1) {
                        objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","210027");
                    } else {
                        objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","210010");
                    }
                    objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",SubAccountCode);
                    objVoucherItem.setAttribute("AMOUNT",EITLERPGLOBAL.round(PartyDebitNoteAmount,0));
                    objVoucherItem.setAttribute("APPLICABLE_AMOUNT",0);
                    objVoucherItem.setAttribute("PERCENTAGE",0);
                    objVoucherItem.setAttribute("REMARKS","");
                    objVoucherItem.setAttribute("PO_NO","");
                    objVoucherItem.setAttribute("PO_DATE","0000-00-00");
                    objVoucherItem.setAttribute("VALUE_DATE","0000-00-00");
                    objVoucherItem.setAttribute("INVOICE_NO","");
                    objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
                    objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
                    objVoucherItem.setAttribute("LINK_NO","");
                    objVoucherItem.setAttribute("GRN_NO","");
                    objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
                    objVoucherItem.setAttribute("MODULE_ID",0);
                    objVoucherItem.setAttribute("IS_DEDUCTION",0);
                    objVoucherItem.setAttribute("REF_VOUCHER_NO","");
                    objVoucherItem.setAttribute("REF_VOUCHER_TYPE",0);
                    objVoucherItem.setAttribute("REF_VOUCHER_COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    objVoucherItem.setAttribute("MATCHED",0);
                    objVoucherItem.setAttribute("MATCHED_DATE","0000-00-00");
                    objVoucherItem.setAttribute("REF_SR_NO",0);
                    ObjVoucher.colVoucherItems.put(Integer.toString(ObjVoucher.colVoucherItems.size()+1),objVoucherItem);
                    
                    if(ObjVoucher.Insert()) {
                        String theVoucherNo = ObjVoucher.getAttribute("VOUCHER_NO").getString();
                        String Msg = "Debit Note No : " + theVoucherNo + " posted for party code "+SubAccountCode+" of Rs."+PartyDebitNoteAmount;
                        String LinkNo = EITLERPGLOBAL.padLeftEx(theVoucherNo.substring(theVoucherNo.length()-5), "0", 6)+"/"+EITLERPGLOBAL.getFinYearEndDate(EITLERPGLOBAL.getCurrentDateDB()).substring(2,4)+EITLERPGLOBAL.getFinYearStartDate(EITLERPGLOBAL.getCurrentDateDB()).substring(2,4);
                        data.Execute("UPDATE D_FIN_VOUCHER_DETAIL SET LINK_NO='"+LinkNo+"' WHERE VOUCHER_NO='"+theVoucherNo+"' ",FinanceGlobal.FinURL);
                        data.Execute("UPDATE D_FIN_VOUCHER_DETAIL_EX SET LINK_NO='"+LinkNo+"' WHERE VOUCHER_NO='"+theVoucherNo+"' ",FinanceGlobal.FinURL);
                        JOptionPane.showMessageDialog(null,Msg);
                        Connection tmpConn = data.getConn(FinanceGlobal.FinURL);
                        Statement tmpStmt1=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                        ResultSet rsTmp1=tmpStmt1.executeQuery("SELECT * FROM D_FIN_DEBITNOTE_RECEIPT_MAPPING LIMIT 1 ");
                        for(int i=1;i<=ObjRefItem.colMappingDetail.size();i++) {
                            clsDebitNoteReceiptMapping ObjItem = (clsDebitNoteReceiptMapping) ObjRefItem.colMappingDetail.get(Integer.toString(i));
                            rsTmp1.moveToInsertRow();
                            rsTmp1.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                            rsTmp1.updateInt("SR_NO",i);
                            rsTmp1.updateString("DEBITNOTE_VOUCHER_NO",theVoucherNo);
                            rsTmp1.updateString("RECEIPT_VOUCHER_NO",ObjItem.getAttribute("RECEIPT_VOUCHER_NO").getString());
                            rsTmp1.updateString("INVOICE_NO",ObjItem.getAttribute("INVOICE_NO").getString());
                            rsTmp1.updateString("INVOICE_DATE",ObjItem.getAttribute("INVOICE_DATE").getString());
                            rsTmp1.updateString("INVOICE_DUE_DATE",ObjItem.getAttribute("INVOICE_DUE_DATE").getString());
                            rsTmp1.updateString("VALUE_DATE",ObjItem.getAttribute("VALUE_DATE").getString());
                            rsTmp1.updateInt("DAYS",ObjItem.getAttribute("DAYS").getInt());
                            rsTmp1.updateInt("APPROVED",1);
                            rsTmp1.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                            rsTmp1.updateDouble("DEBIT_NOTE_AMOUNT",ObjItem.getAttribute("DEBIT_NOTE_AMOUNT").getDouble());
                            rsTmp1.updateBoolean("CHANGED",true);
                            rsTmp1.updateInt("CANCELLED",0);
                            rsTmp1.insertRow();
                        }
                    }
                } else {
                    if(clsSalesInvoice.canDebitNotePost(InvoiceType,ChargeCode)) {
                        JOptionPane.showMessageDialog(null,"Party's Debit note amount ( "+PartyDebitNoteAmount+" ) less then or equal to 5.\n Debit note not posted.");
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    //New 
    public static void PostDebitNoteFelt(String currentVoucherNo) {
        
        ResultSet rsParty = null, rsInvoice = null, rsDR_ADJ_DETAIL = null;
        clsVoucher ObjVoucher = null;
        //clsDrAdjustment ObjDrAdjustment = null;
        clsDebitMemoReceiptMapping ObjRefItem = null;
        
        
        String CurrentVoucherDate = data.getStringValueFromDB("SELECT DOC_DATE FROM D_FIN_DR_ADJ_HEADER WHERE DOC_NO='" + currentVoucherNo + "' ", FinanceGlobal.FinURL);
        System.out.println("CurrentVoucherDate : "+CurrentVoucherDate);
        //String CurrentVoucherDate = data.getStringValueFromDB("SELECT VOUCHER_DATE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='" + currentVoucherNo + "' ", FinanceGlobal.FinURL);
        
        String strSQL = "SELECT DISTINCT SUB_ACCOUNT_CODE,MAIN_ACCOUNT_CODE FROM D_FIN_DR_ADJ_DETAIL WHERE DOC_NO='" + currentVoucherNo + "' "
                + " AND SUB_ACCOUNT_CODE<>'' AND INVOICE_NO<>'' AND MAIN_ACCOUNT_CODE IN ('210010') ORDER BY MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE";
        System.out.println("strSQL 1 : "+strSQL);
        //String strSQL = "SELECT DISTINCT SUB_ACCOUNT_CODE,MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='" + currentVoucherNo + "' "
        //        + "AND EFFECT='C' AND SUB_ACCOUNT_CODE<>'' AND INVOICE_NO<>'' AND MAIN_ACCOUNT_CODE IN ('210010') ORDER BY MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE";
        //REMARKS : AND EFFECT='C' IS REMOVED FROM strSQL
        
        
        try {
            rsParty = data.getResult(strSQL, FinanceGlobal.FinURL);
            rsParty.first();
            String PartyCode = "", MainAccountCode = "", InvoiceNo = "", StateCode = "",InvoiceDate = "", ChargeCode = "", DeductionCode = "", DebitNoteBookCode = "", InvoiceDueDate = "";
            int InvoiceType = 0;
            double PartyDebitNoteAmount = 0, InvoiceDebitNoteAmount = 0,InterestDNAmount=0,PartyIntDNAmount=0, VoucherDebitNoteAmount = 0, InterestPercentage = 0, InvoicePaidAmount = 0, InvoiceAmount = 0;
            double IGST_PER = 0,CGST_PER = 0,SGST_PER = 0,IGST_AMT=0,CGST_AMT=0,SGST_AMT=0,InvoiceIGST_Amt=0,InvoiceSGST_Amt=0,InvoiceCGST_Amt=0,PartyIGST_Amt=0,PartyCGST_Amt=0,PartySGST_Amt=0;
            if (rsParty.getRow() > 0) {
                while (!rsParty.isAfterLast()) {
                    PartyCode = UtilFunctions.getString(rsParty, "SUB_ACCOUNT_CODE", "");
                    MainAccountCode = UtilFunctions.getString(rsParty, "MAIN_ACCOUNT_CODE", "");

                    strSQL = "SELECT * FROM D_FIN_DR_ADJ_DETAIL WHERE DOC_NO='" + currentVoucherNo + "' "
                            + "AND INVOICE_NO<>'' AND SUB_ACCOUNT_CODE='" + PartyCode + "' AND MAIN_ACCOUNT_CODE='" + MainAccountCode + "' ";
                    System.out.println("strSQL 2 : "+strSQL);
                    //strSQL = "SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='" + currentVoucherNo + "' AND EFFECT='C' "
                    //        + "AND INVOICE_NO<>'' AND SUB_ACCOUNT_CODE='" + PartyCode + "' AND MAIN_ACCOUNT_CODE='" + MainAccountCode + "' ";
                    //REMARKS : AND EFFECT='C' IS REMOVED FROM strSQL
                    
                    rsInvoice = data.getResult(strSQL, FinanceGlobal.FinURL);
                    rsInvoice.first();
                    PartyDebitNoteAmount = 0;
                    PartyIGST_Amt =0;
                    PartyCGST_Amt =0;
                    PartySGST_Amt =0;
                    PartyIntDNAmount =0;
                    
                    ObjRefItem = new clsDebitMemoReceiptMapping();	
                    ObjRefItem.colMappingDetail.clear();
                    while (!rsInvoice.isAfterLast()) {
                        InvoiceNo = UtilFunctions.getString(rsInvoice, "INVOICE_NO", "");
                        InvoiceDate = UtilFunctions.getString(rsInvoice, "INVOICE_DATE", "");
                        InvoiceType = clsSalesInvoice.getInvoiceType(InvoiceNo, InvoiceDate);
                        ChargeCode = clsSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate);
                        StateCode  = clsSalesInvoice.getStateCode(PartyCode, MainAccountCode);
                        InvoiceAmount = clsSalesInvoice.getInvoiceAmount(InvoiceNo, InvoiceDate);
                        InvoicePaidAmount = clsSalesInvoice.getInvoicePaidAmount(InvoiceNo, InvoiceDate);
                        strSQL = "SELECT * FROM D_FIN_DEBITMEMO_RECEIPT_MAPPING WHERE RECEIPT_VOUCHER_NO='" + currentVoucherNo + "' "
                                + "AND INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' AND APPROVED=1 AND CANCELLED=0 ";
                        if (data.IsRecordExist(strSQL, FinanceGlobal.FinURL)) {
                            rsInvoice.next();
                            continue;
                        }
                        if (InvoiceAmount > InvoicePaidAmount) { // by pass the invoice if invoice in not fully paid for debit note
                            rsInvoice.next();
                            continue;
                        }
                        if (!clsSalesInvoice.canDebitNotePost(InvoiceType, ChargeCode)) {
                            rsInvoice.next();
                            continue;
                        }
//                        if (InvoiceType == 2 && (PartyCode.equals("812081") || PartyCode.equals("813003") || PartyCode.equals("828001") || PartyCode.equals("828003"))) {
                        if (InvoiceType == 2 && (PartyCode.equals("813003") || PartyCode.equals("828001") || PartyCode.equals("828003"))) {
                            rsInvoice.next();
                            continue;
                        }
                        InvoiceDueDate = data.getStringValueFromDB("SELECT DUE_DATE FROM DINESHMILLS.D_SAL_INVOICE_HEADER WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' AND APPROVED=1 AND CANCELLED=0 ");
                        //CHANGE BY RISHI ACCORDING TO ATUL 

                        //if(InvoiceType==2 && ChargeCode.equals("82")){
                        //InvoiceDueDate = data.getStringValueFromDB("SELECT ADDDATE(DUE_DATE,6) FROM DINESHMILLS.D_SAL_INVOICE_HEADER WHERE INVOICE_NO='"+InvoiceNo+"' AND INVOICE_DATE='"+InvoiceDate+"' AND APPROVED=1 AND CANCELLED=0 AND INVOICE_TYPE =2 AND SUBSTRING(PAYMENT_TERM_CODE,1,1) =8 ");
                        //}
                        if (InvoiceDueDate.equals("")) {
                            JOptionPane.showMessageDialog(null, "Due Date not exists in Invoice No : " + InvoiceNo + " Invoice Date : " + EITLERPGLOBAL.formatDate(InvoiceDate) + " Party Code : " + PartyCode + "\n Contact Account and Administrator.");
                            rsInvoice.next();
                            continue;
                        }

                        InvoiceDebitNoteAmount = 0;
                        InvoiceIGST_Amt =0;
                        InvoiceCGST_Amt =0;
                        InvoiceSGST_Amt =0;
                        
                        
                        //strSQL = "SELECT A.DOC_NO, B.ADJUST_AMOUNT, B.VOUCHER_DATE FROM FINANCE.D_FIN_DR_ADJ_HEADER A, FINANCE.D_FIN_DR_ADJ_DETAIL B " +
                        //           "WHERE A.DOC_NO=B.DOC_NO AND B.INVOICE_NO='"+InvoiceNo+"' AND B.INVOICE_DATE='" + InvoiceDate + "' " +
                        //            "AND B.SUB_ACCOUNT_CODE='" + PartyCode + "' AND B.MAIN_ACCOUNT_CODE='" + MainAccountCode + "' AND A.APPROVED=1 " +
                        //            "AND A.CANCELLED=0 ORDER BY A.DOC_DATE ";
                        //change VALUE_DATE TO VOUCHER_DATE
                        //change AMOUNT TO ADJUST_AMOUNT
                        //AND B.EFFECT='C' IS REMOVED FROM QUERY
                        
                        strSQL = "SELECT A.VOUCHER_NO, B.AMOUNT, B.VALUE_DATE FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B "
                                + "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND B.INVOICE_NO='" + InvoiceNo + "' AND B.INVOICE_DATE='" + InvoiceDate + "' "
                                + "AND B.EFFECT='C' AND B.SUB_ACCOUNT_CODE='" + PartyCode + "' AND B.MAIN_ACCOUNT_CODE='" + MainAccountCode + "' AND A.APPROVED=1 "
                                + "AND A.CANCELLED=0 ORDER BY A.VOUCHER_DATE ";
                        
                        rsDR_ADJ_DETAIL = data.getResult(strSQL, FinanceGlobal.FinURL);
                        rsDR_ADJ_DETAIL.first();
                        // All Credit Record for Effect C & Same Party for same Invoice

                        while (!rsDR_ADJ_DETAIL.isAfterLast()) {

                            //String VoucherNo = UtilFunctions.getString(rsDR_ADJ_DETAIL, "DOC_NO", "");
                            String VoucherNo = UtilFunctions.getString(rsDR_ADJ_DETAIL, "VOUCHER_NO", "");
                            //double VoucherAmount = UtilFunctions.getDouble(rsDR_ADJ_DETAIL, "ADJUST_AMOUNT", 0);
                            double VoucherAmount = UtilFunctions.getDouble(rsDR_ADJ_DETAIL, "AMOUNT", 0);
                            //String ValueDate = UtilFunctions.getString(rsDR_ADJ_DETAIL, "VOUCHER_DATE", "");
                            String ValueDate = UtilFunctions.getString(rsDR_ADJ_DETAIL, "VALUE_DATE", "");
                            if (ValueDate.equals("") || ValueDate.equals("0000-00-00")) {
                                JOptionPane.showMessageDialog(null, "Value Date not exists in Voucher No : " + VoucherNo + " against Invoice No : " + InvoiceNo + " Invoice Date : " + EITLERPGLOBAL.formatDate(InvoiceDate) + " Party Code : " + PartyCode + "\nDebit note can not post.\nContact Account and Administrator.");
                            }
                            if (java.sql.Date.valueOf(ValueDate).before(java.sql.Date.valueOf(InvoiceDueDate)) || java.sql.Date.valueOf(ValueDate).compareTo(java.sql.Date.valueOf(InvoiceDueDate)) == 0) {
                                rsDR_ADJ_DETAIL.next();
                                continue;
                            }
                            VoucherDebitNoteAmount = 0;
                            InterestDNAmount =0;
                            IGST_AMT =0;
                            CGST_AMT =0;
                            SGST_AMT =0;
                            
                            // calculate debit note amount start
                            int InterestDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(InvoiceDueDate), java.sql.Date.valueOf(ValueDate));
                            InterestPercentage = clsSalesInvoice.getDebitNotePercentage(ValueDate, InvoiceType, ChargeCode, PartyCode);                           
                            // VoucherDebitNoteAmount = EITLERPGLOBAL.round(((VoucherAmount * InterestPercentage * InterestDays) / (365 * 100)), 2);
                            InterestDNAmount = EITLERPGLOBAL.round(((VoucherAmount * InterestPercentage * InterestDays) / (365 * 100)), 0);
                            /* 
                             IGST_PER = clsSalesInvoice.getIGSTPER(InvoiceNo, InvoiceDate,MainAccountCode,InvoiceType,StateCode);
                             IGST_AMT =  EITLERPGLOBAL.round((InterestDNAmount * (IGST_PER/100)),0);
                             CGST_PER = clsSalesInvoice.getCGSTPER(InvoiceNo, InvoiceDate,MainAccountCode,InvoiceType,StateCode);
                             CGST_AMT =  EITLERPGLOBAL.round((InterestDNAmount * (SGST_PER/100)),0);
                             SGST_PER = clsSalesInvoice.getSGSTPER(InvoiceNo, InvoiceDate,MainAccountCode,InvoiceType,StateCode);
                             SGST_AMT =  EITLERPGLOBAL.round((InterestDNAmount * (SGST_PER/100)),0);
                        
                             
                            System.out.println( "IGST_AMT = " +IGST_AMT );       
                            System.out.println( "SGST_AMT = " +SGST_AMT );
                            */
                        
                            if (IGST_PER  != 0) {
                                VoucherDebitNoteAmount =  InterestDNAmount + IGST_AMT;
                            }
                            else
                            {  
                                VoucherDebitNoteAmount =  InterestDNAmount + SGST_AMT + CGST_AMT; 
                            }
                            
                            System.out.println( "InterestDNAmount = " +InterestDNAmount );
                            System.out.println( "VoucherDN1 = " +VoucherDebitNoteAmount );
                            // calculate debit note amount end
                            //clsDebitNoteReceiptMapping ObjtempItem = new clsDebitNoteReceiptMapping();
                            clsDebitMemoReceiptMapping ObjtempItem = new clsDebitMemoReceiptMapping();
                            ObjtempItem.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                            ObjtempItem.setAttribute("SR_NO", (ObjRefItem.colMappingDetail.size() + 1));
                            ObjtempItem.setAttribute("DEBITMEMO_NO", "");                                                        
                            ObjtempItem.setAttribute("RECEIPT_VOUCHER_NO", VoucherNo);
                            ObjtempItem.setAttribute("INVOICE_NO", InvoiceNo);
                            ObjtempItem.setAttribute("INVOICE_DATE", InvoiceDate);
                            ObjtempItem.setAttribute("INVOICE_DUE_DATE", InvoiceDueDate);
                            ObjtempItem.setAttribute("VALUE_DATE", ValueDate);
                            ObjtempItem.setAttribute("DAYS", InterestDays);
                            ObjtempItem.setAttribute("DEBIT_NOTE_AMOUNT", EITLERPGLOBAL.round(VoucherDebitNoteAmount,0));
                            
                           ObjtempItem.setAttribute("IGST_PER", IGST_PER);
                           ObjtempItem.setAttribute("CGST_PER", CGST_PER);
                           ObjtempItem.setAttribute("SGST_PER", SGST_PER);
                           ObjtempItem.setAttribute("IGST_AMT", IGST_AMT);
                           ObjtempItem.setAttribute("CGST_AMT", CGST_AMT);
                           ObjtempItem.setAttribute("SGST_AMT", SGST_AMT);
                           ObjtempItem.setAttribute("INTEREST_AMT", InterestDNAmount);
                           ObjtempItem.setAttribute("INTEREST_PER", InterestPercentage);
                           ObjtempItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                           ObjtempItem.setAttribute("DB_PARTY_CODE", PartyCode);
                           ObjtempItem.setAttribute("MAIN_ACCOUNT_CODE", MainAccountCode);
                           ObjtempItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                           ObjtempItem.setAttribute("DEBITNOTE_VOUCHER_NO", "");
                           
                           
                           ObjtempItem.setAttribute("APPROVED", 1);
                           ObjtempItem.setAttribute("APPROVED", EITLERPGLOBAL.getCurrentDateDB());
                           ObjtempItem.setAttribute("CHANGED", 1);
                           ObjRefItem.colMappingDetail.put(Integer.toString(ObjRefItem.colMappingDetail.size() + 1), ObjtempItem);

                           InvoiceDebitNoteAmount = EITLERPGLOBAL.round(InvoiceDebitNoteAmount + VoucherDebitNoteAmount, 0);
                           
                           InvoiceIGST_Amt = EITLERPGLOBAL.round(InvoiceIGST_Amt + IGST_AMT, 0);
                           InvoiceCGST_Amt = EITLERPGLOBAL.round(InvoiceCGST_Amt + CGST_AMT, 0);
                           InvoiceSGST_Amt = EITLERPGLOBAL.round(InvoiceSGST_Amt + SGST_AMT, 0);
                           System.out.println( "InvoiceIGST_Amt = " +InvoiceIGST_Amt );                       
                           rsDR_ADJ_DETAIL.next();
                        } // end while

                        PartyDebitNoteAmount = EITLERPGLOBAL.round(PartyDebitNoteAmount + InvoiceDebitNoteAmount, 0);
                        PartyIGST_Amt = EITLERPGLOBAL.round(PartyIGST_Amt + InvoiceIGST_Amt, 0);
                        PartyCGST_Amt = EITLERPGLOBAL.round(PartyCGST_Amt + InvoiceCGST_Amt, 0);
                        PartySGST_Amt = EITLERPGLOBAL.round(PartySGST_Amt + InvoiceSGST_Amt, 0);
                        System.out.println( "partyIGST_Amt = " +PartyIGST_Amt ); 
                        
                        rsInvoice.next();
                    }

                    System.out.println( "SGST_AMT2 = " +PartyIntDNAmount );
                    System.out.println( "SGST_AMT3 = " +PartyDebitNoteAmount );
                    PartyIntDNAmount = EITLERPGLOBAL.round(PartyDebitNoteAmount - (PartyIGST_Amt + PartyCGST_Amt + PartySGST_Amt), 0);
                    System.out.println( "Interest Amount1 = " +PartyIntDNAmount );
                    System.out.println( "partyigst = " +PartyIGST_Amt );
                   
                   // double diff1 = 0.00;
                   // double diff2 = 0.00;
                  //  diff1 =  (PartyIntDNAmount +PartyIGST_Amt+ PartyCGST_Amt+ PartySGST_Amt+0.00);
                  //  diff2 =  (PartyDebitNoteAmount - diff1 + 0.00);
                  //   System.out.println( "Diff1 = " +diff1 );                   
                     
                   //  System.out.println( "Diff2 = " +diff2 );
                  //  PartyIntDNAmount = PartyIntDNAmount + diff2;
                    
                     System.out.println( "Interest Amount2 = " +PartyIntDNAmount );
                    if (EITLERPGLOBAL.round(PartyDebitNoteAmount, 0) > 5) {                        
                        
                        ResultSet rsTmp = data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID=99");
                        rsTmp.first();
                        String SelPrefix = "", SelSuffix = "";
                        int FFNo = 0;
                        if (rsTmp.getRow() > 0) {
                            SelPrefix = rsTmp.getString("PREFIX_CHARS");
                            SelSuffix = rsTmp.getString("SUFFIX_CHARS");
                            FFNo = rsTmp.getInt("FIRSTFREE_NO");
                        }                        
                //--------- Generate Debit Memo no.  ------------                
                String DebitMemoNo=clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 99, FFNo,  true);                                
                //setAttribute("DEBITMEMO_NO", DebitMemoNo);
                //-------------------------------------------------
                 System.out.println("DebitMemo No : " + DebitMemoNo);
                            
                            Connection tmpConn = data.getConn(FinanceGlobal.FinURL);
                            Statement tmpStmt1 = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                            ResultSet rsTmp1 = tmpStmt1.executeQuery("SELECT * FROM D_FIN_DEBITMEMO_RECEIPT_MAPPING LIMIT 1 ");
                            for (int i = 1; i <= ObjRefItem.colMappingDetail.size(); i++) {
                                //clsDebitNoteReceiptMapping ObjItem = (clsDebitNoteReceiptMapping) ObjRefItem.colMappingDetail.get(Integer.toString(i));
                                clsDebitMemoReceiptMapping ObjItem = (clsDebitMemoReceiptMapping) ObjRefItem.colMappingDetail.get(Integer.toString(i));
                                rsTmp1.moveToInsertRow();
                                rsTmp1.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                rsTmp1.updateInt("SR_NO", i);
                                rsTmp1.updateString("DEBITMEMO_NO", DebitMemoNo);
                                rsTmp1.updateString("DEBITMEMO_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                //rsTmp1.updateString("RECEIPT_VOUCHER_NO",currentVoucherNo);
                                rsTmp1.updateString("RECEIPT_VOUCHER_NO", ObjItem.getAttribute("RECEIPT_VOUCHER_NO").getString());
                                rsTmp1.updateString("MAIN_ACCOUNT_CODE", ObjItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                                rsTmp1.updateString("DB_PARTY_CODE", ObjItem.getAttribute("DB_PARTY_CODE").getString());
                                rsTmp1.updateString("INVOICE_NO", ObjItem.getAttribute("INVOICE_NO").getString());
                                rsTmp1.updateString("INVOICE_DATE", ObjItem.getAttribute("INVOICE_DATE").getString());
                                rsTmp1.updateString("INVOICE_DUE_DATE", ObjItem.getAttribute("INVOICE_DUE_DATE").getString());
                                rsTmp1.updateString("VALUE_DATE", ObjItem.getAttribute("VALUE_DATE").getString());
                                rsTmp1.updateInt("DAYS", ObjItem.getAttribute("DAYS").getInt());
                                rsTmp1.updateInt("APPROVED", 1);
                                rsTmp1.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                rsTmp1.updateDouble("DEBIT_NOTE_AMOUNT", ObjItem.getAttribute("DEBIT_NOTE_AMOUNT").getDouble());
                                rsTmp1.updateDouble("IGST_PER", ObjItem.getAttribute("IGST_PER").getDouble());
                                rsTmp1.updateDouble("CGST_PER", ObjItem.getAttribute("CGST_PER").getDouble());
                                rsTmp1.updateDouble("SGST_PER", ObjItem.getAttribute("SGST_PER").getDouble());
                                rsTmp1.updateDouble("IGST_AMT", ObjItem.getAttribute("IGST_AMT").getDouble());
                                rsTmp1.updateDouble("CGST_AMT", ObjItem.getAttribute("CGST_AMT").getDouble());
                                rsTmp1.updateDouble("SGST_AMT", ObjItem.getAttribute("SGST_AMT").getDouble());
                                rsTmp1.updateDouble("INTEREST_PER", ObjItem.getAttribute("INTEREST_PER").getDouble());
                                rsTmp1.updateDouble("INTEREST_AMT", ObjItem.getAttribute("INTEREST_AMT").getDouble());
                                rsTmp1.updateDouble("INVOICE_AMOUNT", ObjItem.getAttribute("INVOICE_AMOUNT").getDouble());
                                rsTmp1.updateString("DEBITNOTE_VOUCHER_NO", "");
                                rsTmp1.updateString("DEBITNOTE_VOUCHER_DATE", "");
                                rsTmp1.updateString("DEBITMEMO_TYPE", "AUTO");
                                
                                rsTmp1.updateBoolean("CHANGED", true);
                                rsTmp1.updateInt("CANCELLED", 0);
                                rsTmp1.insertRow();
                                
                                String Msg = "Debit Memo No : " + DebitMemoNo + " posted for party code " + PartyCode + " of Rs." + ObjItem.getAttribute("DEBIT_NOTE_AMOUNT").getDouble();
                                JOptionPane.showMessageDialog(null, Msg);
                            }                        
                    } else {
                        if (clsSalesInvoice.canDebitNotePost(InvoiceType, ChargeCode)) {
                            JOptionPane.showMessageDialog(null, "Party's Debit Memo amount ( " + PartyDebitNoteAmount + " ) less then or equal to 5.\n Debit Memo not posted.");
                        }
                    }
                    rsParty.next();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void PostDebitNoteSuitings(String currentVoucherNo) {
        
        ResultSet rsParty = null, rsInvoice = null, rsDR_ADJ_DETAIL = null;
        clsVoucher ObjVoucher = null;
        //clsDrAdjustment ObjDrAdjustment = null;
        clsDebitMemoReceiptMapping ObjRefItem = null;
        
        
        String CurrentVoucherDate = data.getStringValueFromDB("SELECT DOC_DATE FROM D_FIN_DR_ADJ_HEADER WHERE DOC_NO='" + currentVoucherNo + "' ", FinanceGlobal.FinURL);
        System.out.println("CurrentVoucherDate : "+CurrentVoucherDate);
        //String CurrentVoucherDate = data.getStringValueFromDB("SELECT VOUCHER_DATE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='" + currentVoucherNo + "' ", FinanceGlobal.FinURL);
        
        String strSQL = "SELECT DISTINCT SUB_ACCOUNT_CODE,MAIN_ACCOUNT_CODE FROM D_FIN_DR_ADJ_DETAIL WHERE DOC_NO='" + currentVoucherNo + "' "
                + " AND SUB_ACCOUNT_CODE<>'' AND INVOICE_NO<>'' AND MAIN_ACCOUNT_CODE IN ('210027') ORDER BY MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE";
        
        //String strSQL = "SELECT DISTINCT SUB_ACCOUNT_CODE,MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='" + currentVoucherNo + "' "
        //        + "AND EFFECT='C' AND SUB_ACCOUNT_CODE<>'' AND INVOICE_NO<>'' AND MAIN_ACCOUNT_CODE IN ('210027') ORDER BY MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE";
        //REMARKS : AND EFFECT='C' IS REMOVED FROM strSQL        
        System.out.println("strSQL 1 : "+strSQL);
        
        try {
            rsParty = data.getResult(strSQL, FinanceGlobal.FinURL);
            rsParty.first();
            String PartyCode = "", MainAccountCode = "", InvoiceNo = "", StateCode = "",InvoiceDate = "", ChargeCode = "", DeductionCode = "", DebitNoteBookCode = "", InvoiceDueDate = "";
            int InvoiceType = 0;
            double PartyDebitNoteAmount = 0, InvoiceDebitNoteAmount = 0,InterestDNAmount=0,PartyIntDNAmount=0, VoucherDebitNoteAmount = 0, InterestPercentage = 0, InvoicePaidAmount = 0, InvoiceAmount = 0;
            double IGST_PER = 0,CGST_PER = 0,SGST_PER = 0,IGST_AMT=0,CGST_AMT=0,SGST_AMT=0,InvoiceIGST_Amt=0,InvoiceSGST_Amt=0,InvoiceCGST_Amt=0,PartyIGST_Amt=0,PartyCGST_Amt=0,PartySGST_Amt=0;
            if (rsParty.getRow() > 0) {
                while (!rsParty.isAfterLast()) {
                    PartyCode = UtilFunctions.getString(rsParty, "SUB_ACCOUNT_CODE", "");
                    MainAccountCode = UtilFunctions.getString(rsParty, "MAIN_ACCOUNT_CODE", "");

                    strSQL = "SELECT * FROM D_FIN_DR_ADJ_DETAIL WHERE DOC_NO='" + currentVoucherNo + "' "
                            + "AND INVOICE_NO<>'' AND SUB_ACCOUNT_CODE='" + PartyCode + "' AND MAIN_ACCOUNT_CODE='" + MainAccountCode + "' ";
                    
                    //strSQL = "SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='" + currentVoucherNo + "' AND EFFECT='C' "
                    //        + "AND INVOICE_NO<>'' AND SUB_ACCOUNT_CODE='" + PartyCode + "' AND MAIN_ACCOUNT_CODE='" + MainAccountCode + "' ";
                    //REMARKS : AND EFFECT='C' IS REMOVED FROM strSQL
                    
                    System.out.println("strSQL 2 : "+strSQL);
            
                    
                    rsInvoice = data.getResult(strSQL, FinanceGlobal.FinURL);
                    rsInvoice.first();
                    PartyDebitNoteAmount = 0;
                    PartyIGST_Amt =0;
                    PartyCGST_Amt =0;
                    PartySGST_Amt =0;
                    PartyIntDNAmount =0;
                    
                    ObjRefItem = new clsDebitMemoReceiptMapping();	
                    ObjRefItem.colMappingDetail.clear();
                    while (!rsInvoice.isAfterLast()) {
                        InvoiceNo = UtilFunctions.getString(rsInvoice, "INVOICE_NO", "");
                        InvoiceDate = UtilFunctions.getString(rsInvoice, "INVOICE_DATE", "");
                        InvoiceType = clsSalesInvoice.getInvoiceType(InvoiceNo, InvoiceDate);
                        ChargeCode = clsSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate);
                        StateCode  = clsSalesInvoice.getStateCode(PartyCode, MainAccountCode);
                        InvoiceAmount = clsSalesInvoice.getInvoiceAmount(InvoiceNo, InvoiceDate);
                        InvoicePaidAmount = clsSalesInvoice.getInvoicePaidAmount(InvoiceNo, InvoiceDate);
                        strSQL = "SELECT * FROM D_FIN_DEBITMEMO_RECEIPT_MAPPING WHERE RECEIPT_VOUCHER_NO='" + currentVoucherNo + "' "
                                + "AND INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' AND APPROVED=1 AND CANCELLED=0 ";
                        if (data.IsRecordExist(strSQL, FinanceGlobal.FinURL)) {
                            rsInvoice.next();
                            continue;
                        }
                        if (InvoiceAmount > InvoicePaidAmount) { // by pass the invoice if invoice in not fully paid for debit note
                            rsInvoice.next();
                            continue;
                        }
                        if (!clsSalesInvoice.canDebitNotePost(InvoiceType, ChargeCode)) {
                            rsInvoice.next();
                            continue;
                        }
                        
                        InvoiceDueDate = data.getStringValueFromDB("SELECT DUE_DATE FROM DINESHMILLS.D_SAL_INVOICE_HEADER WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' AND APPROVED=1 AND CANCELLED=0 ");
                        //CHANGE BY RISHI ACCORDING TO ATUL 

                        //if(InvoiceType==2 && ChargeCode.equals("82")){
                        //InvoiceDueDate = data.getStringValueFromDB("SELECT ADDDATE(DUE_DATE,6) FROM DINESHMILLS.D_SAL_INVOICE_HEADER WHERE INVOICE_NO='"+InvoiceNo+"' AND INVOICE_DATE='"+InvoiceDate+"' AND APPROVED=1 AND CANCELLED=0 AND INVOICE_TYPE =2 AND SUBSTRING(PAYMENT_TERM_CODE,1,1) =8 ");
                        //}
                        if (InvoiceDueDate.equals("")) {
                            JOptionPane.showMessageDialog(null, "Due Date not exists in Invoice No : " + InvoiceNo + " Invoice Date : " + EITLERPGLOBAL.formatDate(InvoiceDate) + " Party Code : " + PartyCode + "\n Contact Account and Administrator.");
                            rsInvoice.next();
                            continue;
                        }

                        InvoiceDebitNoteAmount = 0;
                        InvoiceIGST_Amt =0;
                        InvoiceCGST_Amt =0;
                        InvoiceSGST_Amt =0;
                        
                        
                        //strSQL = "SELECT A.DOC_NO, B.ADJUST_AMOUNT, B.VOUCHER_DATE FROM FINANCE.D_FIN_DR_ADJ_HEADER A, FINANCE.D_FIN_DR_ADJ_DETAIL B " +
                        //           "WHERE A.DOC_NO=B.DOC_NO AND B.INVOICE_NO='"+InvoiceNo+"' AND B.INVOICE_DATE='" + InvoiceDate + "' " +
                        //            "AND B.SUB_ACCOUNT_CODE='" + PartyCode + "' AND B.MAIN_ACCOUNT_CODE='" + MainAccountCode + "' AND A.APPROVED=1 " +
                        //            "AND A.CANCELLED=0 ORDER BY A.DOC_DATE ";
                        //change VALUE_DATE TO VOUCHER_DATE
                        //change AMOUNT TO ADJUST_AMOUNT
                        //AND B.EFFECT='C' IS REMOVED FROM QUERY
                        
                        strSQL = "SELECT A.VOUCHER_NO, B.AMOUNT, B.VALUE_DATE FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B "
                                + "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND B.INVOICE_NO='" + InvoiceNo + "' AND B.INVOICE_DATE='" + InvoiceDate + "' "
                                + "AND B.EFFECT='C' AND B.SUB_ACCOUNT_CODE='" + PartyCode + "' AND B.MAIN_ACCOUNT_CODE='" + MainAccountCode + "' AND A.APPROVED=1 "
                                + "AND A.CANCELLED=0 ORDER BY A.VOUCHER_DATE ";
                        
                        rsDR_ADJ_DETAIL = data.getResult(strSQL, FinanceGlobal.FinURL);
                        rsDR_ADJ_DETAIL.first();
                        // All Credit Record for Effect C & Same Party for same Invoice

                        while (!rsDR_ADJ_DETAIL.isAfterLast()) {

                            //String VoucherNo = UtilFunctions.getString(rsDR_ADJ_DETAIL, "DOC_NO", "");
                            String VoucherNo = UtilFunctions.getString(rsDR_ADJ_DETAIL, "VOUCHER_NO", "");                        
                            //double VoucherAmount = UtilFunctions.getDouble(rsDR_ADJ_DETAIL, "ADJUST_AMOUNT", 0);
                            double VoucherAmount = UtilFunctions.getDouble(rsDR_ADJ_DETAIL, "AMOUNT", 0);
                            //String ValueDate = UtilFunctions.getString(rsDR_ADJ_DETAIL, "VOUCHER_DATE", "");
                            String ValueDate = UtilFunctions.getString(rsDR_ADJ_DETAIL, "VALUE_DATE", "");
                            if (ValueDate.equals("") || ValueDate.equals("0000-00-00")) {
                                JOptionPane.showMessageDialog(null, "Value Date not exists in Voucher No : " + VoucherNo + " against Invoice No : " + InvoiceNo + " Invoice Date : " + EITLERPGLOBAL.formatDate(InvoiceDate) + " Party Code : " + PartyCode + "\nDebit note can not post.\nContact Account and Administrator.");
                            }
                            if (java.sql.Date.valueOf(ValueDate).before(java.sql.Date.valueOf(InvoiceDueDate)) || java.sql.Date.valueOf(ValueDate).compareTo(java.sql.Date.valueOf(InvoiceDueDate)) == 0) {
                                rsDR_ADJ_DETAIL.next();
                                continue;
                            }
                            VoucherDebitNoteAmount = 0;
                            InterestDNAmount =0;
                            IGST_AMT =0;
                            CGST_AMT =0;
                            SGST_AMT =0;
                            
                            // calculate debit note amount start
                            int InterestDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(InvoiceDueDate), java.sql.Date.valueOf(ValueDate));
                            InterestPercentage = clsSalesInvoice.getDebitNotePercentage(ValueDate, InvoiceType, ChargeCode, PartyCode);                           
                            // VoucherDebitNoteAmount = EITLERPGLOBAL.round(((VoucherAmount * InterestPercentage * InterestDays) / (365 * 100)), 2);
                            InterestDNAmount = EITLERPGLOBAL.round(((VoucherAmount * InterestPercentage * InterestDays) / (365 * 100)), 0);
                            /* 
                             IGST_PER = clsSalesInvoice.getIGSTPER(InvoiceNo, InvoiceDate,MainAccountCode,InvoiceType,StateCode);
                             IGST_AMT =  EITLERPGLOBAL.round((InterestDNAmount * (IGST_PER/100)),0);
                             CGST_PER = clsSalesInvoice.getCGSTPER(InvoiceNo, InvoiceDate,MainAccountCode,InvoiceType,StateCode);
                             CGST_AMT =  EITLERPGLOBAL.round((InterestDNAmount * (SGST_PER/100)),0);
                             SGST_PER = clsSalesInvoice.getSGSTPER(InvoiceNo, InvoiceDate,MainAccountCode,InvoiceType,StateCode);
                             SGST_AMT =  EITLERPGLOBAL.round((InterestDNAmount * (SGST_PER/100)),0);
                        
                             
                            System.out.println( "IGST_AMT = " +IGST_AMT );       
                            System.out.println( "SGST_AMT = " +SGST_AMT );
                            */
                        
                            if (IGST_PER  != 0) {
                                VoucherDebitNoteAmount =  InterestDNAmount + IGST_AMT;
                            }
                            else
                            {  
                                VoucherDebitNoteAmount =  InterestDNAmount + SGST_AMT + CGST_AMT; 
                            }
                            
                            System.out.println( "InterestDNAmount = " +InterestDNAmount );
                            System.out.println( "VoucherDN1 = " +VoucherDebitNoteAmount );
                            // calculate debit note amount end
                            //clsDebitNoteReceiptMapping ObjtempItem = new clsDebitNoteReceiptMapping();
                            clsDebitMemoReceiptMapping ObjtempItem = new clsDebitMemoReceiptMapping();
                            ObjtempItem.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                            ObjtempItem.setAttribute("SR_NO", (ObjRefItem.colMappingDetail.size() + 1));
                            ObjtempItem.setAttribute("DEBITMEMO_NO", "");                                                        
                            ObjtempItem.setAttribute("RECEIPT_VOUCHER_NO", VoucherNo);
                            ObjtempItem.setAttribute("INVOICE_NO", InvoiceNo);
                            ObjtempItem.setAttribute("INVOICE_DATE", InvoiceDate);
                            ObjtempItem.setAttribute("INVOICE_DUE_DATE", InvoiceDueDate);
                            ObjtempItem.setAttribute("VALUE_DATE", ValueDate);
                            ObjtempItem.setAttribute("DAYS", InterestDays);
                            ObjtempItem.setAttribute("DEBIT_NOTE_AMOUNT", EITLERPGLOBAL.round(VoucherDebitNoteAmount,0));
                            
                           ObjtempItem.setAttribute("IGST_PER", IGST_PER);
                           ObjtempItem.setAttribute("CGST_PER", CGST_PER);
                           ObjtempItem.setAttribute("SGST_PER", SGST_PER);
                           ObjtempItem.setAttribute("IGST_AMT", IGST_AMT);
                           ObjtempItem.setAttribute("CGST_AMT", CGST_AMT);
                           ObjtempItem.setAttribute("SGST_AMT", SGST_AMT);
                           ObjtempItem.setAttribute("INTEREST_AMT", InterestDNAmount);
                           ObjtempItem.setAttribute("INTEREST_PER", InterestPercentage);
                           ObjtempItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                           ObjtempItem.setAttribute("DB_PARTY_CODE", PartyCode);
                           ObjtempItem.setAttribute("MAIN_ACCOUNT_CODE", MainAccountCode);
                           ObjtempItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                           ObjtempItem.setAttribute("DEBITNOTE_VOUCHER_NO", "");
                           
                           
                           ObjtempItem.setAttribute("APPROVED", 1);
                           ObjtempItem.setAttribute("APPROVED", EITLERPGLOBAL.getCurrentDateDB());
                           ObjtempItem.setAttribute("CHANGED", 1);
                           ObjRefItem.colMappingDetail.put(Integer.toString(ObjRefItem.colMappingDetail.size() + 1), ObjtempItem);

                           InvoiceDebitNoteAmount = EITLERPGLOBAL.round(InvoiceDebitNoteAmount + VoucherDebitNoteAmount, 0);
                           
                           InvoiceIGST_Amt = EITLERPGLOBAL.round(InvoiceIGST_Amt + IGST_AMT, 0);
                           InvoiceCGST_Amt = EITLERPGLOBAL.round(InvoiceCGST_Amt + CGST_AMT, 0);
                           InvoiceSGST_Amt = EITLERPGLOBAL.round(InvoiceSGST_Amt + SGST_AMT, 0);
                           System.out.println( "InvoiceIGST_Amt = " +InvoiceIGST_Amt );                       
                           rsDR_ADJ_DETAIL.next();
                        } // end while

                        PartyDebitNoteAmount = EITLERPGLOBAL.round(PartyDebitNoteAmount + InvoiceDebitNoteAmount, 0);
                        PartyIGST_Amt = EITLERPGLOBAL.round(PartyIGST_Amt + InvoiceIGST_Amt, 0);
                        PartyCGST_Amt = EITLERPGLOBAL.round(PartyCGST_Amt + InvoiceCGST_Amt, 0);
                        PartySGST_Amt = EITLERPGLOBAL.round(PartySGST_Amt + InvoiceSGST_Amt, 0);
                        System.out.println( "partyIGST_Amt = " +PartyIGST_Amt ); 
                        
                        rsInvoice.next();
                    }

                    System.out.println( "SGST_AMT2 = " +PartyIntDNAmount );
                    System.out.println( "SGST_AMT3 = " +PartyDebitNoteAmount );
                    PartyIntDNAmount = EITLERPGLOBAL.round(PartyDebitNoteAmount - (PartyIGST_Amt + PartyCGST_Amt + PartySGST_Amt), 0);
                    System.out.println( "Interest Amount1 = " +PartyIntDNAmount );
                    System.out.println( "partyigst = " +PartyIGST_Amt );
                   
                   // double diff1 = 0.00;
                   // double diff2 = 0.00;
                  //  diff1 =  (PartyIntDNAmount +PartyIGST_Amt+ PartyCGST_Amt+ PartySGST_Amt+0.00);
                  //  diff2 =  (PartyDebitNoteAmount - diff1 + 0.00);
                  //   System.out.println( "Diff1 = " +diff1 );                   
                     
                   //  System.out.println( "Diff2 = " +diff2 );
                  //  PartyIntDNAmount = PartyIntDNAmount + diff2;
                    
                     System.out.println( "Interest Amount2 = " +PartyIntDNAmount );
                    if (EITLERPGLOBAL.round(PartyDebitNoteAmount, 0) > 5) {                        
                        
                        ResultSet rsTmp = data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID=99");
                        rsTmp.first();
                        String SelPrefix = "", SelSuffix = "";
                        int FFNo = 0;
                        if (rsTmp.getRow() > 0) {
                            SelPrefix = rsTmp.getString("PREFIX_CHARS");
                            SelSuffix = rsTmp.getString("SUFFIX_CHARS");
                            FFNo = rsTmp.getInt("FIRSTFREE_NO");
                        }                        
                //--------- Generate Debit Memo no.  ------------                
                String DebitMemoNo=clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 99, FFNo,  true);                                
                //setAttribute("DEBITMEMO_NO", DebitMemoNo);
                //-------------------------------------------------
                 System.out.println("DebitMemo No : " + DebitMemoNo);
                            
                            Connection tmpConn = data.getConn(FinanceGlobal.FinURL);
                            Statement tmpStmt1 = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                            ResultSet rsTmp1 = tmpStmt1.executeQuery("SELECT * FROM D_FIN_DEBITMEMO_RECEIPT_MAPPING LIMIT 1 ");
                            for (int i = 1; i <= ObjRefItem.colMappingDetail.size(); i++) {
                                //clsDebitNoteReceiptMapping ObjItem = (clsDebitNoteReceiptMapping) ObjRefItem.colMappingDetail.get(Integer.toString(i));
                                clsDebitMemoReceiptMapping ObjItem = (clsDebitMemoReceiptMapping) ObjRefItem.colMappingDetail.get(Integer.toString(i));
                                rsTmp1.moveToInsertRow();
                                rsTmp1.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                rsTmp1.updateInt("SR_NO", i);
                                rsTmp1.updateString("DEBITMEMO_NO", DebitMemoNo);
                                rsTmp1.updateString("DEBITMEMO_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                //rsTmp1.updateString("RECEIPT_VOUCHER_NO",currentVoucherNo);
                                rsTmp1.updateString("RECEIPT_VOUCHER_NO", ObjItem.getAttribute("RECEIPT_VOUCHER_NO").getString());
                                rsTmp1.updateString("MAIN_ACCOUNT_CODE", ObjItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                                rsTmp1.updateString("DB_PARTY_CODE", ObjItem.getAttribute("DB_PARTY_CODE").getString());
                                rsTmp1.updateString("INVOICE_NO", ObjItem.getAttribute("INVOICE_NO").getString());
                                rsTmp1.updateString("INVOICE_DATE", ObjItem.getAttribute("INVOICE_DATE").getString());
                                rsTmp1.updateString("INVOICE_DUE_DATE", ObjItem.getAttribute("INVOICE_DUE_DATE").getString());
                                rsTmp1.updateString("VALUE_DATE", ObjItem.getAttribute("VALUE_DATE").getString());
                                rsTmp1.updateInt("DAYS", ObjItem.getAttribute("DAYS").getInt());
                                rsTmp1.updateInt("APPROVED", 1);
                                rsTmp1.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                rsTmp1.updateDouble("DEBIT_NOTE_AMOUNT", ObjItem.getAttribute("DEBIT_NOTE_AMOUNT").getDouble());
                                rsTmp1.updateDouble("IGST_PER", ObjItem.getAttribute("IGST_PER").getDouble());
                                rsTmp1.updateDouble("CGST_PER", ObjItem.getAttribute("CGST_PER").getDouble());
                                rsTmp1.updateDouble("SGST_PER", ObjItem.getAttribute("SGST_PER").getDouble());
                                rsTmp1.updateDouble("IGST_AMT", ObjItem.getAttribute("IGST_AMT").getDouble());
                                rsTmp1.updateDouble("CGST_AMT", ObjItem.getAttribute("CGST_AMT").getDouble());
                                rsTmp1.updateDouble("SGST_AMT", ObjItem.getAttribute("SGST_AMT").getDouble());
                                rsTmp1.updateDouble("INTEREST_PER", ObjItem.getAttribute("INTEREST_PER").getDouble());
                                rsTmp1.updateDouble("INTEREST_AMT", ObjItem.getAttribute("INTEREST_AMT").getDouble());
                                rsTmp1.updateDouble("INVOICE_AMOUNT", ObjItem.getAttribute("INVOICE_AMOUNT").getDouble());
                                rsTmp1.updateString("DEBITNOTE_VOUCHER_NO", "");
                                rsTmp1.updateString("DEBITNOTE_VOUCHER_DATE", "");
                                rsTmp1.updateString("DEBITMEMO_TYPE", "AUTO");
                                
                                rsTmp1.updateBoolean("CHANGED", true);
                                rsTmp1.updateInt("CANCELLED", 0);
                                rsTmp1.insertRow();
                                
                                String Msg = "Debit Memo No : " + DebitMemoNo + " posted for party code " + PartyCode + " of Rs." + ObjItem.getAttribute("DEBIT_NOTE_AMOUNT").getDouble();
                                JOptionPane.showMessageDialog(null, Msg);
                            }                        
                    } else {
                        if (clsSalesInvoice.canDebitNotePost(InvoiceType, ChargeCode)) {
                            JOptionPane.showMessageDialog(null, "Party's Debit Memo amount ( " + PartyDebitNoteAmount + " ) less then or equal to 5.\n Debit Memo not posted.");
                        }
                    }
                    rsParty.next();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void PostDebitNoteSuitings_Old(String currentVoucherNo) {
        ResultSet rsParty = null, rsInvoice = null, rsVoucher = null;
        //clsVoucher ObjVoucher = null;
        clsVoucher ObjVoucher = null;
        clsDebitNoteReceiptMapping ObjRefItem = null;
        //String CurrentVoucherDate = data.getStringValueFromDB("SELECT VOUCHER_DATE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='" + currentVoucherNo + "' ", FinanceGlobal.FinURL);
        //String strSQL = "SELECT DISTINCT SUB_ACCOUNT_CODE,MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='" + currentVoucherNo + "' "
        //        + "AND EFFECT='C' AND SUB_ACCOUNT_CODE<>'' AND INVOICE_NO<>'' AND MAIN_ACCOUNT_CODE IN ('210027') ORDER BY MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE";
        
        String CurrentVoucherDate = data.getStringValueFromDB("SELECT DOC_DATE FROM D_FIN_DR_ADJ_HEADER WHERE DOC_NO='" + currentVoucherNo + "' ", FinanceGlobal.FinURL);
        System.out.println("CurrentVoucherDate : "+CurrentVoucherDate);
        //String CurrentVoucherDate = data.getStringValueFromDB("SELECT VOUCHER_DATE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='" + currentVoucherNo + "' ", FinanceGlobal.FinURL);
        
        String strSQL = "SELECT DISTINCT SUB_ACCOUNT_CODE,MAIN_ACCOUNT_CODE FROM D_FIN_DR_ADJ_DETAIL WHERE DOC_NO='" + currentVoucherNo + "' "
                + " AND SUB_ACCOUNT_CODE<>'' AND INVOICE_NO<>'' AND MAIN_ACCOUNT_CODE IN ('210027') ORDER BY MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE";
        System.out.println("strSQL 1 : "+strSQL);
        //String strSQL = "SELECT DISTINCT SUB_ACCOUNT_CODE,MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='" + currentVoucherNo + "' "
        //        + "AND EFFECT='C' AND SUB_ACCOUNT_CODE<>'' AND INVOICE_NO<>'' AND MAIN_ACCOUNT_CODE IN ('210010') ORDER BY MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE";
        //REMARKS : AND EFFECT='C' IS REMOVED FROM strSQL        
        try {
            rsParty = data.getResult(strSQL, FinanceGlobal.FinURL);
            rsParty.first();
            String PartyCode = "", MainAccountCode = "", InvoiceNo = "", StateCode = "",InvoiceDate = "", ChargeCode = "", DeductionCode = "", DebitNoteBookCode = "", InvoiceDueDate = "";
            int InvoiceType = 0;
            double PartyDebitNoteAmount = 0, InvoiceDebitNoteAmount = 0,InterestDNAmount=0,PartyIntDNAmount=0, VoucherDebitNoteAmount = 0, InterestPercentage = 0, InvoicePaidAmount = 0, InvoiceAmount = 0;
            double IGST_PER = 0,CGST_PER = 0,SGST_PER = 0,IGST_AMT=0,CGST_AMT=0,SGST_AMT=0,InvoiceIGST_Amt=0,InvoiceSGST_Amt=0,InvoiceCGST_Amt=0,PartyIGST_Amt=0,PartyCGST_Amt=0,PartySGST_Amt=0;
            if (rsParty.getRow() > 0) {
                while (!rsParty.isAfterLast()) {
                    PartyCode = UtilFunctions.getString(rsParty, "SUB_ACCOUNT_CODE", "");
                    MainAccountCode = UtilFunctions.getString(rsParty, "MAIN_ACCOUNT_CODE", "");

                    //strSQL = "SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='" + currentVoucherNo + "' AND EFFECT='C' "
                    //        + "AND INVOICE_NO<>'' AND SUB_ACCOUNT_CODE='" + PartyCode + "' AND MAIN_ACCOUNT_CODE='" + MainAccountCode + "' ";
                    
                    strSQL = "SELECT * FROM D_FIN_DR_ADJ_DETAIL WHERE DOC_NO='" + currentVoucherNo + "' "
                            + "AND INVOICE_NO<>'' AND SUB_ACCOUNT_CODE='" + PartyCode + "' AND MAIN_ACCOUNT_CODE='" + MainAccountCode + "' ";
                    System.out.println("strSQL 2 : "+strSQL);
                    //strSQL = "SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='" + currentVoucherNo + "' AND EFFECT='C' "
                    //        + "AND INVOICE_NO<>'' AND SUB_ACCOUNT_CODE='" + PartyCode + "' AND MAIN_ACCOUNT_CODE='" + MainAccountCode + "' ";
                    //REMARKS : AND EFFECT='C' IS REMOVED FROM strSQL
                    
                    rsInvoice = data.getResult(strSQL, FinanceGlobal.FinURL);
                    rsInvoice.first();
                    PartyDebitNoteAmount = 0;
                    PartyIGST_Amt =0;
                    PartyCGST_Amt =0;
                    PartySGST_Amt =0;
                    PartyIntDNAmount =0;
                    
                    ObjRefItem = new clsDebitNoteReceiptMapping();
                    ObjRefItem.colMappingDetail.clear();
                    while (!rsInvoice.isAfterLast()) {
                        InvoiceNo = UtilFunctions.getString(rsInvoice, "INVOICE_NO", "");
                        InvoiceDate = UtilFunctions.getString(rsInvoice, "INVOICE_DATE", "");
                        InvoiceType = clsSalesInvoice.getInvoiceType(InvoiceNo, InvoiceDate);
                        ChargeCode = clsSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate);
                        StateCode  = clsSalesInvoice.getStateCode(PartyCode, MainAccountCode);
                        InvoiceAmount = clsSalesInvoice.getInvoiceAmount(InvoiceNo, InvoiceDate);
                        InvoicePaidAmount = clsSalesInvoice.getInvoicePaidAmount(InvoiceNo, InvoiceDate);
                        strSQL = "SELECT * FROM D_FIN_DEBITNOTE_RECEIPT_MAPPING WHERE RECEIPT_VOUCHER_NO='" + currentVoucherNo + "' "
                                + "AND INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' AND APPROVED=1 AND CANCELLED=0 ";
                        System.out.println("strSQL #1 "+strSQL);    
                        if (data.IsRecordExist(strSQL, FinanceGlobal.FinURL)) {
                            rsInvoice.next();
                            continue;
                        }
                        if (InvoiceAmount > InvoicePaidAmount) { // by pass the invoice if invoice in not fully paid for debit note
                            rsInvoice.next();
                            continue;
                        }
                        if (!clsSalesInvoice.canDebitNotePost(InvoiceType, ChargeCode)) {
                            rsInvoice.next();
                            continue;
                        }
                        
                        InvoiceDueDate = data.getStringValueFromDB("SELECT DUE_DATE FROM DINESHMILLS.D_SAL_INVOICE_HEADER WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' AND APPROVED=1 AND CANCELLED=0 ");
                        System.out.println("InvoiceDueDate #2 "+InvoiceDueDate);    
                        if (InvoiceDueDate.equals("")) {
                            JOptionPane.showMessageDialog(null, "Due Date not exists in Invoice No : " + InvoiceNo + " Invoice Date : " + EITLERPGLOBAL.formatDate(InvoiceDate) + " Party Code : " + PartyCode + "\n Contact Account and Administrator.");
                            rsInvoice.next();
                            continue;
                        }

                        InvoiceDebitNoteAmount = 0;
                        InvoiceIGST_Amt =0;
                        InvoiceCGST_Amt =0;
                        InvoiceSGST_Amt =0;
                        
                    
                       // strSQL = "SELECT A.VOUCHER_NO, B.AMOUNT, B.VALUE_DATE FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B "
                       //         + "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND B.INVOICE_NO='" + InvoiceNo + "' AND B.INVOICE_DATE='" + InvoiceDate + "' "
                       //         + "AND B.EFFECT='C' AND B.SUB_ACCOUNT_CODE='" + PartyCode + "' AND B.MAIN_ACCOUNT_CODE='" + MainAccountCode + "' AND A.APPROVED=1 "
                       //         + "AND A.CANCELLED=0 ORDER BY A.VOUCHER_DATE ";
                        
                        strSQL = "SELECT A.DOC_NO, B.ADJUST_AMOUNT, B.VOUCHER_DATE FROM FINANCE.D_FIN_DR_ADJ_HEADER A, FINANCE.D_FIN_DR_ADJ_DETAIL B " +
                                   "WHERE A.DOC_NO=B.DOC_NO AND B.INVOICE_NO='"+InvoiceNo+"' AND B.INVOICE_DATE='" + InvoiceDate + "' " +
                                    "AND B.SUB_ACCOUNT_CODE='" + PartyCode + "' AND B.MAIN_ACCOUNT_CODE='" + MainAccountCode + "' AND A.APPROVED=1 " +
                                    "AND A.CANCELLED=0 ORDER BY A.DOC_DATE ";
                        System.out.println("strSQL #3 "+strSQL);    
                        //change VALUE_DATE TO VOUCHER_DATE
                        //change AMOUNT TO ADJUST_AMOUNT
                        //AND B.EFFECT='C' IS REMOVED FROM QUERY
                        
                        rsVoucher = data.getResult(strSQL, FinanceGlobal.FinURL);
                        rsVoucher.first();
                        //All Credit Record for Effect C & Same Party for same Invoice

                        while (!rsVoucher.isAfterLast()) {
                            
                            System.out.println("in loop : #4");
                            
                            String VoucherNo = UtilFunctions.getString(rsVoucher, "DOC_NO", "");
                            double VoucherAmount = UtilFunctions.getDouble(rsVoucher, "ADJUST_AMOUNT", 0);
                            String ValueDate = UtilFunctions.getString(rsVoucher, "VOUCHER_DATE", "");
                            
                            System.out.println("DOC_NO : "+VoucherNo);
                            System.out.println("ADJUST_AMOUNT : "+VoucherAmount);
                            System.out.println("VOUCHER_DATE : "+ValueDate);
                            
                            
                            if (ValueDate.equals("") || ValueDate.equals("0000-00-00")) {
                                JOptionPane.showMessageDialog(null, "Value Date not exists in Voucher No : " + VoucherNo + " against Invoice No : " + InvoiceNo + " Invoice Date : " + EITLERPGLOBAL.formatDate(InvoiceDate) + " Party Code : " + PartyCode + "\nDebit note can not post.\nContact Account and Administrator.");
                            }
                            if (java.sql.Date.valueOf(ValueDate).before(java.sql.Date.valueOf(InvoiceDueDate)) || java.sql.Date.valueOf(ValueDate).compareTo(java.sql.Date.valueOf(InvoiceDueDate)) == 0) {
                                System.out.println("in date condition, continue loop");
                                rsVoucher.next();
                                continue;
                            }
                            VoucherDebitNoteAmount = 0;
                            InterestDNAmount =0;
                            IGST_AMT =0;
                            CGST_AMT =0;
                            SGST_AMT =0;
                            
                            // calculate debit note amount start
                            int InterestDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(InvoiceDueDate), java.sql.Date.valueOf(ValueDate));
                            InterestPercentage = clsSalesInvoice.getDebitNotePercentage(ValueDate, InvoiceType, ChargeCode, PartyCode);
                            
                            System.out.println("in loop : #5 InterestDays : "+InterestDays);
                            System.out.println("in loop : #5 InterestPercentage : "+InterestPercentage);
                            
                            if (InvoiceType == 1 && java.sql.Date.valueOf(InvoiceDate).after(java.sql.Date.valueOf("2012-12-31")) && PartyCode.equals("300170")) {
                                InterestPercentage = 15;
                            }
                           // VoucherDebitNoteAmount = EITLERPGLOBAL.round(((VoucherAmount * InterestPercentage * InterestDays) / (365 * 100)), 2);
                            InterestDNAmount = EITLERPGLOBAL.round(((VoucherAmount * InterestPercentage * InterestDays) / (365 * 100)), 0);
                             IGST_PER = clsSalesInvoice.getIGSTPER(InvoiceNo, InvoiceDate,MainAccountCode,InvoiceType,StateCode);
                             IGST_AMT =  EITLERPGLOBAL.round((InterestDNAmount * (IGST_PER/100)),0);
                             CGST_PER = clsSalesInvoice.getCGSTPER(InvoiceNo, InvoiceDate,MainAccountCode,InvoiceType,StateCode);
                             CGST_AMT =  EITLERPGLOBAL.round((InterestDNAmount * (SGST_PER/100)),0);
                             SGST_PER = clsSalesInvoice.getSGSTPER(InvoiceNo, InvoiceDate,MainAccountCode,InvoiceType,StateCode);
                             SGST_AMT =  EITLERPGLOBAL.round((InterestDNAmount * (SGST_PER/100)),0);
                        
                             
                             System.out.println( "IGST_AMT = " +IGST_AMT );       
                            System.out.println( "SGST_AMT = " +SGST_AMT );
                            
                        
                        if (IGST_PER  != 0) {
                                VoucherDebitNoteAmount =  InterestDNAmount + IGST_AMT;
                             }
                             else
                             {  
                                VoucherDebitNoteAmount =  InterestDNAmount + SGST_AMT + CGST_AMT; 
                             }
                            System.out.println( "InterestDNAmount = " +InterestDNAmount );
                            System.out.println( "VoucherDN1 = " +VoucherDebitNoteAmount );
                            // calculate debit note amount end
                            clsDebitNoteReceiptMapping ObjtempItem = new clsDebitNoteReceiptMapping();
                            ObjtempItem.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                            ObjtempItem.setAttribute("SR_NO", (ObjRefItem.colMappingDetail.size() + 1));
                            ObjtempItem.setAttribute("DEBITNOTE_VOUCHER_NO", "");
                            ObjtempItem.setAttribute("DEBITNOTE_VOUCHER_DATE", "");
                            ObjtempItem.setAttribute("RECEIPT_VOUCHER_NO", VoucherNo);
                            ObjtempItem.setAttribute("INVOICE_NO", InvoiceNo);
                            ObjtempItem.setAttribute("INVOICE_DATE", InvoiceDate);
                            ObjtempItem.setAttribute("INVOICE_DUE_DATE", InvoiceDueDate);
                            ObjtempItem.setAttribute("VALUE_DATE", ValueDate);
                            ObjtempItem.setAttribute("DAYS", InterestDays);
                            ObjtempItem.setAttribute("DEBIT_NOTE_AMOUNT", EITLERPGLOBAL.round(VoucherDebitNoteAmount,0));
                            ObjtempItem.setAttribute("IGST_PER", IGST_PER);
                            ObjtempItem.setAttribute("CGST_PER", CGST_PER);
                            ObjtempItem.setAttribute("SGST_PER", SGST_PER);
                            ObjtempItem.setAttribute("IGST_AMT", IGST_AMT);
                            ObjtempItem.setAttribute("CGST_AMT", CGST_AMT);
                            ObjtempItem.setAttribute("SGST_AMT", SGST_AMT);
                            ObjtempItem.setAttribute("INTEREST_AMT", InterestDNAmount);
                            ObjtempItem.setAttribute("INTEREST_PER", InterestPercentage);
                            ObjtempItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                            ObjtempItem.setAttribute("DB_PARTY_CODE", PartyCode);
                            ObjtempItem.setAttribute("MAIN_ACCOUNT_CODE", MainAccountCode);
                            ObjtempItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                           
                            
                           
                           
                            ObjtempItem.setAttribute("APPROVED", 1);
                            ObjtempItem.setAttribute("APPROVED", EITLERPGLOBAL.getCurrentDateDB());
                            ObjtempItem.setAttribute("CHANGED", 1);
                            ObjRefItem.colMappingDetail.put(Integer.toString(ObjRefItem.colMappingDetail.size() + 1), ObjtempItem);

                            InvoiceDebitNoteAmount = EITLERPGLOBAL.round(InvoiceDebitNoteAmount + VoucherDebitNoteAmount, 0);
                           
                            InvoiceIGST_Amt = EITLERPGLOBAL.round(InvoiceIGST_Amt + IGST_AMT, 0);
                            InvoiceCGST_Amt = EITLERPGLOBAL.round(InvoiceCGST_Amt + CGST_AMT, 0);
                            InvoiceSGST_Amt = EITLERPGLOBAL.round(InvoiceSGST_Amt + SGST_AMT, 0);
                            System.out.println( "InvoiceIGST_Amt = " +InvoiceIGST_Amt );                       
                            rsVoucher.next();
                        } // end while

                        PartyDebitNoteAmount = EITLERPGLOBAL.round(PartyDebitNoteAmount + InvoiceDebitNoteAmount, 0);
                        PartyIGST_Amt = EITLERPGLOBAL.round(PartyIGST_Amt + InvoiceIGST_Amt, 0);
                        PartyCGST_Amt = EITLERPGLOBAL.round(PartyCGST_Amt + InvoiceCGST_Amt, 0);
                        PartySGST_Amt = EITLERPGLOBAL.round(PartySGST_Amt + InvoiceSGST_Amt, 0);
                        System.out.println( "partyIGST_Amt = " +PartyIGST_Amt ); 
                        
                        rsInvoice.next();
                    }

                     System.out.println( "SGST_AMT2 = " +PartyIntDNAmount );
                      System.out.println( "SGST_AMT3 = " +PartyDebitNoteAmount );
                    PartyIntDNAmount = EITLERPGLOBAL.round(PartyDebitNoteAmount - (PartyIGST_Amt + PartyCGST_Amt + PartySGST_Amt), 0);
                    System.out.println( "Interest Amount1 = " +PartyIntDNAmount );
                   System.out.println( "partyigst = " +PartyIGST_Amt );
                   
                   // double diff1 = 0.00;
                   // double diff2 = 0.00;
                  //  diff1 =  (PartyIntDNAmount +PartyIGST_Amt+ PartyCGST_Amt+ PartySGST_Amt+0.00);
                  //  diff2 =  (PartyDebitNoteAmount - diff1 + 0.00);
                  //   System.out.println( "Diff1 = " +diff1 );
                    
                     
                   //  System.out.println( "Diff2 = " +diff2 );
                  //  PartyIntDNAmount = PartyIntDNAmount + diff2;
                    
                     System.out.println( "Interest Amount2 = " +PartyIntDNAmount );
                    if (EITLERPGLOBAL.round(PartyDebitNoteAmount, 0) > 5) {

                        /*========== Select the Hierarchy ======== */
                        int HierarchyID = 0;
                        HashMap List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsVoucher.DebitNoteModuleID, "CHOOSE_HIERARCHY", "DEFAULT", "");
                        if (List.size() > 0) {
                            //Get the Result of the Rule which would be the hierarchy no.
                            clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                            HierarchyID = UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                        }
                        /*========== End of Hierarchy Selection ======== */

                        // get Book code and deduction code start
                        List.clear();
                        List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsVoucher.DebitNoteModuleID, "GET_BOOK_CODE", ChargeCode.substring(0, 1), Integer.toString(InvoiceType));
                        if (List.size() > 0) {
                            //Get the Result of the Rule which would be the hierarchy no.
                            clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                            DebitNoteBookCode = objRule.getAttribute("RULE_OUTCOME").getString();
                        }

                        List.clear();
                        List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsVoucher.DebitNoteModuleID, "GET_ACCOUNT_CODE", "DEDUCTION_CODE", Integer.toString(InvoiceType));
                        if (List.size() > 0) {
                            clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                            DeductionCode = objRule.getAttribute("RULE_OUTCOME").getString();
                        }

                        ResultSet rsTmp = data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID=" + clsVoucher.DebitNoteModuleID);
                        rsTmp.first();
                        String SelPrefix = "", SelSuffix = "";
                        int FFNo = 0;
                        if (rsTmp.getRow() > 0) {
                            SelPrefix = rsTmp.getString("PREFIX_CHARS");
                            SelSuffix = rsTmp.getString("SUFFIX_CHARS");
                            FFNo = rsTmp.getInt("FIRSTFREE_NO");
                        }

                       int VoucherSrNo = 0;
                        ObjVoucher = new clsVoucher();
                        ObjVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
                        ObjVoucher.colVoucherItems.clear();
                        ObjVoucher.setAttribute("PREFIX", SelPrefix);
                        ObjVoucher.setAttribute("SUFFIX", SelSuffix);
                        ObjVoucher.setAttribute("FFNO", FFNo);
                        ObjVoucher.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        ObjVoucher.setAttribute("VOUCHER_NO", "");
                        ObjVoucher.setAttribute("LEGACY_NO", "");
                        ObjVoucher.setAttribute("LEGACY_DATE", "");
                        ObjVoucher.setAttribute("BOOK_CODE", DebitNoteBookCode);
                        ObjVoucher.setAttribute("VOUCHER_TYPE", FinanceGlobal.TYPE_DEBIT_NOTE);
                        ObjVoucher.setAttribute("CHEQUE_NO", "");
                        ObjVoucher.setAttribute("CHEQUE_DATE", "");
                        ObjVoucher.setAttribute("BANK_NAME", "");
                        //ObjVoucher.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.getCurrentDate());
                        ObjVoucher.setAttribute("VOUCHER_DATE", EITLERPGLOBAL.formatDate(CurrentVoucherDate));
                        ObjVoucher.setAttribute("ST_CATEGORY", "");
                        ObjVoucher.setAttribute("MODULE_ID", 0);
                        ObjVoucher.setAttribute("REMARKS", "Generated By Auto Debit Note System.");
                        ObjVoucher.setAttribute("HIERARCHY_ID", HierarchyID);
                        int FirstUserID = data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID=" + HierarchyID + " AND SR_NO=1");
                        ObjVoucher.setAttribute("FROM", FirstUserID);
                        ObjVoucher.setAttribute("TO", FirstUserID);
                        ObjVoucher.setAttribute("FROM_REMARKS", "");
                        ObjVoucher.setAttribute("APPROVAL_STATUS", "F"); //Final Approved --> Voucher

                        
                        
                        
                         clsVoucherItem objVoucherItem = new clsVoucherItem();
                        VoucherSrNo++;
                        objVoucherItem.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        objVoucherItem.setAttribute("VOUCHER_NO", "");
                        objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                        objVoucherItem.setAttribute("EFFECT", "C");
                        objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                        objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", DeductionCode);
                        objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                     //   objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(PartyDebitNoteAmount, 0));
                        objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(PartyIntDNAmount, 0));
                        objVoucherItem.setAttribute("APPLICABLE_AMOUNT", 0);
                        objVoucherItem.setAttribute("PERCENTAGE", 0);
                        objVoucherItem.setAttribute("REMARKS", "");
                        objVoucherItem.setAttribute("PO_NO", "");
                        objVoucherItem.setAttribute("PO_DATE", "");
                        objVoucherItem.setAttribute("VALUE_DATE", "");
                        objVoucherItem.setAttribute("INVOICE_NO", "");
                        objVoucherItem.setAttribute("INVOICE_DATE", "");
                        objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                        objVoucherItem.setAttribute("LINK_NO", "");
                        objVoucherItem.setAttribute("GRN_NO", "");
                        objVoucherItem.setAttribute("GRN_DATE", "");
                        objVoucherItem.setAttribute("MODULE_ID", 0);
                        objVoucherItem.setAttribute("IS_DEDUCTION", 0);
                        objVoucherItem.setAttribute("REF_VOUCHER_NO", currentVoucherNo);
                        objVoucherItem.setAttribute("REF_VOUCHER_TYPE", clsVoucher.getVoucherType(currentVoucherNo));
                        objVoucherItem.setAttribute("REF_VOUCHER_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        objVoucherItem.setAttribute("MATCHED", 0);
                        objVoucherItem.setAttribute("MATCHED_DATE", "");
                        objVoucherItem.setAttribute("REF_SR_NO", 0);
                        ObjVoucher.colVoucherItems.put(Integer.toString(ObjVoucher.colVoucherItems.size() + 1), objVoucherItem);
                        
                         System.out.println( "Effect = C" );
                          System.out.println( "PartyInt Dn_AMT = " +EITLERPGLOBAL.round(PartyIntDNAmount, 2) );
                        
                        
                        
                         if(CGST_AMT>0) {  
                             objVoucherItem = new clsVoucherItem();
                        VoucherSrNo++;
                        objVoucherItem.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        objVoucherItem.setAttribute("VOUCHER_NO", "");
                        objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                        objVoucherItem.setAttribute("EFFECT", "C");
                        objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                        if (InvoiceType == 1) {
                            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127565");
                        } else {
                            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127566");
                        }
                        objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                        objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(PartyCGST_Amt, 0));
                        objVoucherItem.setAttribute("APPLICABLE_AMOUNT", 0);
                        objVoucherItem.setAttribute("PERCENTAGE", 0);
                        objVoucherItem.setAttribute("REMARKS", "");
                        objVoucherItem.setAttribute("PO_NO", "");
                        objVoucherItem.setAttribute("PO_DATE", "");
                        objVoucherItem.setAttribute("VALUE_DATE", "");
                        objVoucherItem.setAttribute("INVOICE_NO", "");
                        objVoucherItem.setAttribute("INVOICE_DATE", "");
                        objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                        objVoucherItem.setAttribute("LINK_NO", "");
                        objVoucherItem.setAttribute("GRN_NO", "");
                        objVoucherItem.setAttribute("GRN_DATE", "");
                        objVoucherItem.setAttribute("MODULE_ID", 0);
                        objVoucherItem.setAttribute("IS_DEDUCTION", 0);
                        objVoucherItem.setAttribute("REF_VOUCHER_NO", currentVoucherNo);
                        objVoucherItem.setAttribute("REF_VOUCHER_TYPE", clsVoucher.getVoucherType(currentVoucherNo));
                        objVoucherItem.setAttribute("REF_VOUCHER_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        objVoucherItem.setAttribute("MATCHED", 0);
                        objVoucherItem.setAttribute("MATCHED_DATE", "");
                        objVoucherItem.setAttribute("REF_SR_NO", 0);
                        ObjVoucher.colVoucherItems.put(Integer.toString(ObjVoucher.colVoucherItems.size() + 1), objVoucherItem);
                        }
                        
                        if(SGST_AMT>0) {  
                             objVoucherItem = new clsVoucherItem();
                        VoucherSrNo++;
                        objVoucherItem.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        objVoucherItem.setAttribute("VOUCHER_NO", "");
                        objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                        objVoucherItem.setAttribute("EFFECT", "C");
                        objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                        if (InvoiceType == 1) {
                            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127567");
                        } else {
                            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127568");
                        }
                        objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                        objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(PartySGST_Amt, 0));
                        objVoucherItem.setAttribute("APPLICABLE_AMOUNT", 0);
                        objVoucherItem.setAttribute("PERCENTAGE", 0);
                        objVoucherItem.setAttribute("REMARKS", "");
                        objVoucherItem.setAttribute("PO_NO", "");
                        objVoucherItem.setAttribute("PO_DATE", "");
                        objVoucherItem.setAttribute("VALUE_DATE", "");
                        objVoucherItem.setAttribute("INVOICE_NO", "");
                        objVoucherItem.setAttribute("INVOICE_DATE", "");
                        objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                        objVoucherItem.setAttribute("LINK_NO", "");
                        objVoucherItem.setAttribute("GRN_NO", "");
                        objVoucherItem.setAttribute("GRN_DATE", "");
                        objVoucherItem.setAttribute("MODULE_ID", 0);
                        objVoucherItem.setAttribute("IS_DEDUCTION", 0);
                        objVoucherItem.setAttribute("REF_VOUCHER_NO", currentVoucherNo);
                        objVoucherItem.setAttribute("REF_VOUCHER_TYPE", clsVoucher.getVoucherType(currentVoucherNo));
                        objVoucherItem.setAttribute("REF_VOUCHER_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        objVoucherItem.setAttribute("MATCHED", 0);
                        objVoucherItem.setAttribute("MATCHED_DATE", "");
                        objVoucherItem.setAttribute("REF_SR_NO", 0);
                        ObjVoucher.colVoucherItems.put(Integer.toString(ObjVoucher.colVoucherItems.size() + 1), objVoucherItem);
                        }
                                 
                        if(IGST_AMT>0) {  
                             objVoucherItem = new clsVoucherItem();
                        VoucherSrNo++;
                        objVoucherItem.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        objVoucherItem.setAttribute("VOUCHER_NO", "");
                        objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                        objVoucherItem.setAttribute("EFFECT", "C");
                        objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                        if (InvoiceType == 1) {
                            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127569");
                        } else {
                            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127570");
                        }
                        objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                        objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(PartyIGST_Amt, 0));
                        objVoucherItem.setAttribute("APPLICABLE_AMOUNT", 0);
                        objVoucherItem.setAttribute("PERCENTAGE", 0);
                        objVoucherItem.setAttribute("REMARKS", "");
                        objVoucherItem.setAttribute("PO_NO", "");
                        objVoucherItem.setAttribute("PO_DATE", "");
                        objVoucherItem.setAttribute("VALUE_DATE", "");
                        objVoucherItem.setAttribute("INVOICE_NO", "");
                        objVoucherItem.setAttribute("INVOICE_DATE", "");
                        objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                        objVoucherItem.setAttribute("LINK_NO", "");
                        objVoucherItem.setAttribute("GRN_NO", "");
                        objVoucherItem.setAttribute("GRN_DATE", "");
                        objVoucherItem.setAttribute("MODULE_ID", 0);
                        objVoucherItem.setAttribute("IS_DEDUCTION", 0);
                        objVoucherItem.setAttribute("REF_VOUCHER_NO", currentVoucherNo);
                        objVoucherItem.setAttribute("REF_VOUCHER_TYPE", clsVoucher.getVoucherType(currentVoucherNo));
                        objVoucherItem.setAttribute("REF_VOUCHER_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        objVoucherItem.setAttribute("MATCHED", 0);
                        objVoucherItem.setAttribute("MATCHED_DATE", "");
                        objVoucherItem.setAttribute("REF_SR_NO", 0);
                        ObjVoucher.colVoucherItems.put(Integer.toString(ObjVoucher.colVoucherItems.size() + 1), objVoucherItem);
                        System.out.println( "Effect = C" );
                          System.out.println( "IGSTAMT = "+EITLERPGLOBAL.round(PartyIGST_Amt, 2) );
                       
                        }
                        
                          
                        objVoucherItem = new clsVoucherItem();
                        VoucherSrNo++;
                        objVoucherItem.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        objVoucherItem.setAttribute("VOUCHER_NO", "");
                        objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                        objVoucherItem.setAttribute("EFFECT", "D");
                        objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                        if (InvoiceType == 1) {
                            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "210027");
                        } else {
                            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "210010");
                        }
                        objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", PartyCode);
                        objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(PartyDebitNoteAmount, 0));
                        objVoucherItem.setAttribute("APPLICABLE_AMOUNT", 0);
                        objVoucherItem.setAttribute("PERCENTAGE", 0);
                        objVoucherItem.setAttribute("REMARKS", "");
                        objVoucherItem.setAttribute("PO_NO", "");
                        objVoucherItem.setAttribute("PO_DATE", "");
                        objVoucherItem.setAttribute("VALUE_DATE", "");
                        objVoucherItem.setAttribute("INVOICE_NO", "");
                        objVoucherItem.setAttribute("INVOICE_DATE", "");
                        objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                        objVoucherItem.setAttribute("LINK_NO", "");
                        objVoucherItem.setAttribute("GRN_NO", "");
                        objVoucherItem.setAttribute("GRN_DATE", "");
                        objVoucherItem.setAttribute("MODULE_ID", 0);
                        objVoucherItem.setAttribute("IS_DEDUCTION", 0);
                        objVoucherItem.setAttribute("REF_VOUCHER_NO", currentVoucherNo);
                        objVoucherItem.setAttribute("REF_VOUCHER_TYPE", clsVoucher.getVoucherType(currentVoucherNo));
                        objVoucherItem.setAttribute("REF_VOUCHER_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        objVoucherItem.setAttribute("MATCHED", 0);
                        objVoucherItem.setAttribute("MATCHED_DATE", "");
                        objVoucherItem.setAttribute("REF_SR_NO", 0);
                        ObjVoucher.colVoucherItems.put(Integer.toString(ObjVoucher.colVoucherItems.size() + 1), objVoucherItem);

                          System.out.println( "Effect = D" );
                          System.out.println( "Party Debit Note Amount = " +EITLERPGLOBAL.round(PartyDebitNoteAmount, 0) );
                        
                        clsVoucher.bypass = 1;
                    
                        
                        if (ObjVoucher.Insert()) {
                            String theVoucherNo = ObjVoucher.getAttribute("VOUCHER_NO").getString();
                            String theVoucherDate = EITLERPGLOBAL.formatDateDB(ObjVoucher.getAttribute("VOUCHER_DATE").getString());

                            String Msg = "Debit Note No : " + theVoucherNo + " posted for party code " + PartyCode + " of Rs." + PartyDebitNoteAmount;
                            String LinkNo = EITLERPGLOBAL.padLeftEx(theVoucherNo.substring(theVoucherNo.length() - 5), "0", 6) + "/" + EITLERPGLOBAL.getFinYearEndDate(EITLERPGLOBAL.getCurrentDateDB()).substring(2, 4) + EITLERPGLOBAL.getFinYearStartDate(EITLERPGLOBAL.getCurrentDateDB()).substring(2, 4);
                            data.Execute("UPDATE D_FIN_VOUCHER_DETAIL SET LINK_NO='" + LinkNo + "' WHERE VOUCHER_NO='" + theVoucherNo + "' ", FinanceGlobal.FinURL);
                            data.Execute("UPDATE D_FIN_VOUCHER_DETAIL_EX SET LINK_NO='" + LinkNo + "' WHERE VOUCHER_NO='" + theVoucherNo + "' ", FinanceGlobal.FinURL);
                            JOptionPane.showMessageDialog(null, Msg);
                            Connection tmpConn = data.getConn(FinanceGlobal.FinURL);
                            Statement tmpStmt1 = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                            ResultSet rsTmp1 = tmpStmt1.executeQuery("SELECT * FROM D_FIN_DEBITNOTE_RECEIPT_MAPPING LIMIT 1 ");
                            for (int i = 1; i <= ObjRefItem.colMappingDetail.size(); i++) {
                                clsDebitNoteReceiptMapping ObjItem = (clsDebitNoteReceiptMapping) ObjRefItem.colMappingDetail.get(Integer.toString(i));
                                rsTmp1.moveToInsertRow();
                                rsTmp1.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                rsTmp1.updateInt("SR_NO", i);
                                rsTmp1.updateString("DEBITNOTE_VOUCHER_NO", theVoucherNo);
                                rsTmp1.updateString("DEBITNOTE_VOUCHER_DATE", theVoucherDate);
                                //rsTmp1.updateString("RECEIPT_VOUCHER_NO",currentVoucherNo);
                                rsTmp1.updateString("RECEIPT_VOUCHER_NO", ObjItem.getAttribute("RECEIPT_VOUCHER_NO").getString());
                                rsTmp1.updateString("MAIN_ACCOUNT_CODE", ObjItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                                rsTmp1.updateString("DB_PARTY_CODE", ObjItem.getAttribute("DB_PARTY_CODE").getString());
                                rsTmp1.updateString("INVOICE_NO", ObjItem.getAttribute("INVOICE_NO").getString());
                                rsTmp1.updateString("INVOICE_DATE", ObjItem.getAttribute("INVOICE_DATE").getString());
                                rsTmp1.updateString("INVOICE_DUE_DATE", ObjItem.getAttribute("INVOICE_DUE_DATE").getString());
                                rsTmp1.updateString("VALUE_DATE", ObjItem.getAttribute("VALUE_DATE").getString());
                                rsTmp1.updateInt("DAYS", ObjItem.getAttribute("DAYS").getInt());
                                rsTmp1.updateInt("APPROVED", 1);
                                rsTmp1.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                rsTmp1.updateDouble("DEBIT_NOTE_AMOUNT", ObjItem.getAttribute("DEBIT_NOTE_AMOUNT").getDouble());
                                rsTmp1.updateDouble("IGST_PER", ObjItem.getAttribute("IGST_PER").getDouble());
                                rsTmp1.updateDouble("CGST_PER", ObjItem.getAttribute("CGST_PER").getDouble());
                                rsTmp1.updateDouble("SGST_PER", ObjItem.getAttribute("SGST_PER").getDouble());
                                rsTmp1.updateDouble("IGST_AMT", ObjItem.getAttribute("IGST_AMT").getDouble());
                                rsTmp1.updateDouble("CGST_AMT", ObjItem.getAttribute("CGST_AMT").getDouble());
                                rsTmp1.updateDouble("SGST_AMT", ObjItem.getAttribute("SGST_AMT").getDouble());
                                rsTmp1.updateDouble("INTEREST_PER", ObjItem.getAttribute("INTEREST_PER").getDouble());
                                rsTmp1.updateDouble("INTEREST_AMT", ObjItem.getAttribute("INTEREST_AMT").getDouble());
                                rsTmp1.updateDouble("INVOICE_AMOUNT", ObjItem.getAttribute("INVOICE_AMOUNT").getDouble());
                                
                                
                                rsTmp1.updateBoolean("CHANGED", true);
                                rsTmp1.updateInt("CANCELLED", 0);
                                rsTmp1.insertRow();
                            }
                        }
                    } else {
                        if (clsSalesInvoice.canDebitNotePost(InvoiceType, ChargeCode)) {
                            JOptionPane.showMessageDialog(null, "Party's Debit note amount ( " + PartyDebitNoteAmount + " ) less then or equal to 5.\n Debit note not posted.");
                        }
                    }
                    rsParty.next();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    public static void PostDebitNoteFeltV2(String currentVoucherNo) {

        ResultSet rsParty = null, rsInvoice = null, rsDR_ADJ_DETAIL = null;
        clsVoucher ObjVoucher = null;
        //clsDrAdjustment ObjDrAdjustment = null;
        clsDebitMemoReceiptMapping ObjRefItem = null;

        String CurrentVoucherDate = data.getStringValueFromDB("SELECT DOC_DATE FROM D_FIN_DR_ADJ_HEADER WHERE DOC_NO='" + currentVoucherNo + "' ", FinanceGlobal.FinURL);
        System.out.println("CurrentVoucherDate : " + CurrentVoucherDate);
        //String CurrentVoucherDate = data.getStringValueFromDB("SELECT VOUCHER_DATE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='" + currentVoucherNo + "' ", FinanceGlobal.FinURL);

        //String strSQL = "SELECT DISTINCT SUB_ACCOUNT_CODE,MAIN_ACCOUNT_CODE FROM D_FIN_DR_ADJ_DETAIL WHERE DOC_NO='" + currentVoucherNo + "' "
        //        + " AND SUB_ACCOUNT_CODE<>'' AND INVOICE_NO<>'' AND MAIN_ACCOUNT_CODE IN ('210010') ORDER BY MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE";
        String strSQL = "SELECT DISTINCT SUB_ACCOUNT_CODE,MAIN_ACCOUNT_CODE FROM D_FIN_DR_ADJ_DETAIL WHERE DOC_NO='" + currentVoucherNo + "' "
                + " AND SUB_ACCOUNT_CODE<>'' AND INVOICE_NO<>'' AND MAIN_ACCOUNT_CODE IN ('210010') ORDER BY MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE"; //AND SUBSTRING(SJ_NO,1,2)<>'DN'
        System.out.println("strSQL 1 : " + strSQL);
        //String strSQL = "SELECT DISTINCT SUB_ACCOUNT_CODE,MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='" + currentVoucherNo + "' "
        //        + "AND EFFECT='C' AND SUB_ACCOUNT_CODE<>'' AND INVOICE_NO<>'' AND MAIN_ACCOUNT_CODE IN ('210010') ORDER BY MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE";
        //REMARKS : AND EFFECT='C' IS REMOVED FROM strSQL

        try {
            rsParty = data.getResult(strSQL, FinanceGlobal.FinURL);
            rsParty.first();
            String PartyCode = "", MainAccountCode = "", InvoiceNo = "", StateCode = "", InvoiceDate = "", ChargeCode = "", DeductionCode = "", DebitNoteBookCode = "", InvoiceDueDate = "";
            int InvoiceType = 0;
            double PartyDebitNoteAmount = 0, InvoiceDebitNoteAmount = 0, InterestDNAmount = 0, PartyIntDNAmount = 0, VoucherDebitNoteAmount = 0, InterestPercentage = 0, InvoicePaidAmount = 0, InvoiceAmount = 0;
            double IGST_PER = 0, CGST_PER = 0, SGST_PER = 0, IGST_AMT = 0, CGST_AMT = 0, SGST_AMT = 0, InvoiceIGST_Amt = 0, InvoiceSGST_Amt = 0, InvoiceCGST_Amt = 0, PartyIGST_Amt = 0, PartyCGST_Amt = 0, PartySGST_Amt = 0;
            if (rsParty.getRow() > 0) {
                while (!rsParty.isAfterLast()) {
                    PartyCode = UtilFunctions.getString(rsParty, "SUB_ACCOUNT_CODE", "");
                    MainAccountCode = UtilFunctions.getString(rsParty, "MAIN_ACCOUNT_CODE", "");

                    strSQL = "SELECT * FROM D_FIN_DR_ADJ_DETAIL WHERE DOC_NO='" + currentVoucherNo + "' "
                            + "AND INVOICE_NO<>'' AND SUB_ACCOUNT_CODE='" + PartyCode + "' AND MAIN_ACCOUNT_CODE='" + MainAccountCode + "' GROUP BY INVOICE_NO,INVOICE_DATE";
                    System.out.println("strSQL 2 : " + strSQL);
                    //strSQL = "SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='" + currentVoucherNo + "' AND EFFECT='C' "
                    //        + "AND INVOICE_NO<>'' AND SUB_ACCOUNT_CODE='" + PartyCode + "' AND MAIN_ACCOUNT_CODE='" + MainAccountCode + "' ";
                    //REMARKS : AND EFFECT='C' IS REMOVED FROM strSQL

                    rsInvoice = data.getResult(strSQL, FinanceGlobal.FinURL);
                    rsInvoice.first();
                    PartyDebitNoteAmount = 0;
                    PartyIGST_Amt = 0;
                    PartyCGST_Amt = 0;
                    PartySGST_Amt = 0;
                    PartyIntDNAmount = 0;

//                    ObjRefItem = new clsDebitMemoReceiptMapping();
//                    ObjRefItem.colMappingDetail.clear();
                    
                    while (!rsInvoice.isAfterLast()) {
                        
                        ObjRefItem = new clsDebitMemoReceiptMapping();
                        ObjRefItem.colMappingDetail.clear();
                    
                        InvoiceNo = UtilFunctions.getString(rsInvoice, "INVOICE_NO", "");
                        InvoiceDate = UtilFunctions.getString(rsInvoice, "INVOICE_DATE", "");
                        InvoiceType = clsSalesInvoice.getInvoiceType(InvoiceNo, InvoiceDate);
                        ChargeCode = clsSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate);
                        StateCode = clsSalesInvoice.getStateCode(PartyCode, MainAccountCode);
                        InvoiceAmount = clsSalesInvoice.getInvoiceAmount(InvoiceNo, InvoiceDate);
                        InvoicePaidAmount = clsSalesInvoice.getInvoicePaidAmount(InvoiceNo, InvoiceDate);
                        strSQL = "SELECT * FROM D_FIN_DEBITMEMO_RECEIPT_MAPPING WHERE RECEIPT_VOUCHER_NO='" + currentVoucherNo + "' "
                                + "AND INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' AND APPROVED=1 AND CANCELLED=0 ";
                        if (data.IsRecordExist(strSQL, FinanceGlobal.FinURL)) {
                            rsInvoice.next();
                            continue;
                        }
                        if (InvoiceAmount > InvoicePaidAmount) { // by pass the invoice if invoice in not fully paid for debit note
                            rsInvoice.next();
                            continue;
                        }
                        if (!clsSalesInvoice.canDebitNotePost(InvoiceType, ChargeCode)) {
                            rsInvoice.next();
                            continue;
                        }
                        //           if (InvoiceType == 2 && (PartyCode.equals("812081") || PartyCode.equals("813003") || PartyCode.equals("828001") || PartyCode.equals("828003"))) {
                        if (InvoiceType == 2 && (PartyCode.equals("813003") || PartyCode.equals("828001") || PartyCode.equals("828003"))) {
                            rsInvoice.next();
                            continue;
                        }
                        InvoiceDueDate = data.getStringValueFromDB("SELECT DUE_DATE FROM DINESHMILLS.D_SAL_INVOICE_HEADER WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' AND APPROVED=1 AND CANCELLED=0 ");
                        //CHANGE BY RISHI ACCORDING TO ATUL 

                        //if(InvoiceType==2 && ChargeCode.equals("82")){
                        //InvoiceDueDate = data.getStringValueFromDB("SELECT ADDDATE(DUE_DATE,6) FROM DINESHMILLS.D_SAL_INVOICE_HEADER WHERE INVOICE_NO='"+InvoiceNo+"' AND INVOICE_DATE='"+InvoiceDate+"' AND APPROVED=1 AND CANCELLED=0 AND INVOICE_TYPE =2 AND SUBSTRING(PAYMENT_TERM_CODE,1,1) =8 ");
                        //}
                        if (InvoiceDueDate.equals("")) {
                            JOptionPane.showMessageDialog(null, "Due Date not exists in Invoice No : " + InvoiceNo + " Invoice Date : " + EITLERPGLOBAL.formatDate(InvoiceDate) + " Party Code : " + PartyCode + "\n Contact Account and Administrator.");
                            rsInvoice.next();
                            continue;
                        }

                        InvoiceDebitNoteAmount = 0;
                        InvoiceIGST_Amt = 0;
                        InvoiceCGST_Amt = 0;
                        InvoiceSGST_Amt = 0;

                        //strSQL = "SELECT A.DOC_NO, B.ADJUST_AMOUNT, B.VOUCHER_DATE FROM FINANCE.D_FIN_DR_ADJ_HEADER A, FINANCE.D_FIN_DR_ADJ_DETAIL B " +
                        //           "WHERE A.DOC_NO=B.DOC_NO AND B.INVOICE_NO='"+InvoiceNo+"' AND B.INVOICE_DATE='" + InvoiceDate + "' " +
                        //            "AND B.SUB_ACCOUNT_CODE='" + PartyCode + "' AND B.MAIN_ACCOUNT_CODE='" + MainAccountCode + "' AND A.APPROVED=1 " +
                        //            "AND A.CANCELLED=0 ORDER BY A.DOC_DATE ";
                        //change VALUE_DATE TO VOUCHER_DATE
                        //change AMOUNT TO ADJUST_AMOUNT
                        //AND B.EFFECT='C' IS REMOVED FROM QUERY
                        strSQL = "SELECT A.VOUCHER_NO, B.AMOUNT, B.VALUE_DATE FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B "
                                + "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND B.INVOICE_NO='" + InvoiceNo + "' AND B.INVOICE_DATE='" + InvoiceDate + "' "
                                + "AND B.EFFECT='C' AND B.SUB_ACCOUNT_CODE='" + PartyCode + "' AND B.MAIN_ACCOUNT_CODE='" + MainAccountCode + "' AND A.APPROVED=1 "
                                + "AND A.CANCELLED=0 ORDER BY A.VOUCHER_DATE ";

                        rsDR_ADJ_DETAIL = data.getResult(strSQL, FinanceGlobal.FinURL);
                        rsDR_ADJ_DETAIL.first();
                        // All Credit Record for Effect C & Same Party for same Invoice

                        while (!rsDR_ADJ_DETAIL.isAfterLast()) {

                            //String VoucherNo = UtilFunctions.getString(rsDR_ADJ_DETAIL, "DOC_NO", "");
                            String VoucherNo = UtilFunctions.getString(rsDR_ADJ_DETAIL, "VOUCHER_NO", "");
                            //double VoucherAmount = UtilFunctions.getDouble(rsDR_ADJ_DETAIL, "ADJUST_AMOUNT", 0);
                            double VoucherAmount = UtilFunctions.getDouble(rsDR_ADJ_DETAIL, "AMOUNT", 0);
                            //String ValueDate = UtilFunctions.getString(rsDR_ADJ_DETAIL, "VOUCHER_DATE", "");
                            String ValueDate = UtilFunctions.getString(rsDR_ADJ_DETAIL, "VALUE_DATE", "");
                            if (ValueDate.equals("") || ValueDate.equals("0000-00-00")) {
                                JOptionPane.showMessageDialog(null, "Value Date not exists in Voucher No : " + VoucherNo + " against Invoice No : " + InvoiceNo + " Invoice Date : " + EITLERPGLOBAL.formatDate(InvoiceDate) + " Party Code : " + PartyCode + "\nDebit note can not post.\nContact Account and Administrator.");
                            }
                            if (java.sql.Date.valueOf(ValueDate).before(java.sql.Date.valueOf(InvoiceDueDate)) || java.sql.Date.valueOf(ValueDate).compareTo(java.sql.Date.valueOf(InvoiceDueDate)) == 0) {
                                rsDR_ADJ_DETAIL.next();
                                continue;
                            }
                            VoucherDebitNoteAmount = 0;
                            InterestDNAmount = 0;
                            IGST_AMT = 0;
                            CGST_AMT = 0;
                            SGST_AMT = 0;

                            // calculate debit note amount start
                            int InterestDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(InvoiceDueDate), java.sql.Date.valueOf(ValueDate));
                            InterestPercentage = clsSalesInvoice.getDebitNotePercentage(ValueDate, InvoiceType, ChargeCode, PartyCode);
                            // VoucherDebitNoteAmount = EITLERPGLOBAL.round(((VoucherAmount * InterestPercentage * InterestDays) / (365 * 100)), 2);
                            InterestDNAmount = EITLERPGLOBAL.round(((VoucherAmount * InterestPercentage * InterestDays) / (365 * 100)), 0);
                            /* 
                             IGST_PER = clsSalesInvoice.getIGSTPER(InvoiceNo, InvoiceDate,MainAccountCode,InvoiceType,StateCode);
                             IGST_AMT =  EITLERPGLOBAL.round((InterestDNAmount * (IGST_PER/100)),0);
                             CGST_PER = clsSalesInvoice.getCGSTPER(InvoiceNo, InvoiceDate,MainAccountCode,InvoiceType,StateCode);
                             CGST_AMT =  EITLERPGLOBAL.round((InterestDNAmount * (SGST_PER/100)),0);
                             SGST_PER = clsSalesInvoice.getSGSTPER(InvoiceNo, InvoiceDate,MainAccountCode,InvoiceType,StateCode);
                             SGST_AMT =  EITLERPGLOBAL.round((InterestDNAmount * (SGST_PER/100)),0);
                        
                             
                             System.out.println( "IGST_AMT = " +IGST_AMT );       
                             System.out.println( "SGST_AMT = " +SGST_AMT );
                             */

                            if (IGST_PER != 0) {
                                VoucherDebitNoteAmount = InterestDNAmount + IGST_AMT;
                            } else {
                                VoucherDebitNoteAmount = InterestDNAmount + SGST_AMT + CGST_AMT;
                            }

                            System.out.println("InterestDNAmount = " + InterestDNAmount);
                            System.out.println("VoucherDN1 = " + VoucherDebitNoteAmount);
                            // calculate debit note amount end
                            //clsDebitNoteReceiptMapping ObjtempItem = new clsDebitNoteReceiptMapping();
                            clsDebitMemoReceiptMapping ObjtempItem = new clsDebitMemoReceiptMapping();
                            ObjtempItem.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                            ObjtempItem.setAttribute("SR_NO", (ObjRefItem.colMappingDetail.size() + 1));
                            ObjtempItem.setAttribute("DEBITMEMO_NO", "");
                            ObjtempItem.setAttribute("RECEIPT_VOUCHER_NO", VoucherNo);
                            ObjtempItem.setAttribute("INVOICE_NO", InvoiceNo);
                            ObjtempItem.setAttribute("INVOICE_DATE", InvoiceDate);
                            ObjtempItem.setAttribute("INVOICE_DUE_DATE", InvoiceDueDate);
                            ObjtempItem.setAttribute("VALUE_DATE", ValueDate);
                            ObjtempItem.setAttribute("DAYS", InterestDays);
                            ObjtempItem.setAttribute("DEBIT_NOTE_AMOUNT", EITLERPGLOBAL.round(VoucherDebitNoteAmount, 0));

                            ObjtempItem.setAttribute("IGST_PER", IGST_PER);
                            ObjtempItem.setAttribute("CGST_PER", CGST_PER);
                            ObjtempItem.setAttribute("SGST_PER", SGST_PER);
                            ObjtempItem.setAttribute("IGST_AMT", IGST_AMT);
                            ObjtempItem.setAttribute("CGST_AMT", CGST_AMT);
                            ObjtempItem.setAttribute("SGST_AMT", SGST_AMT);
                            ObjtempItem.setAttribute("INTEREST_AMT", InterestDNAmount);
                            ObjtempItem.setAttribute("INTEREST_PER", InterestPercentage);
                            ObjtempItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                            ObjtempItem.setAttribute("DB_PARTY_CODE", PartyCode);
                            ObjtempItem.setAttribute("MAIN_ACCOUNT_CODE", MainAccountCode);
                            ObjtempItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                            ObjtempItem.setAttribute("DEBITNOTE_VOUCHER_NO", "");

                            ObjtempItem.setAttribute("APPROVED", 1);
                            ObjtempItem.setAttribute("APPROVED", EITLERPGLOBAL.getCurrentDateDB());
                            ObjtempItem.setAttribute("CHANGED", 1);
                            ObjRefItem.colMappingDetail.put(Integer.toString(ObjRefItem.colMappingDetail.size() + 1), ObjtempItem);

                            InvoiceDebitNoteAmount = EITLERPGLOBAL.round(InvoiceDebitNoteAmount + VoucherDebitNoteAmount, 0);

                            InvoiceIGST_Amt = EITLERPGLOBAL.round(InvoiceIGST_Amt + IGST_AMT, 0);
                            InvoiceCGST_Amt = EITLERPGLOBAL.round(InvoiceCGST_Amt + CGST_AMT, 0);
                            InvoiceSGST_Amt = EITLERPGLOBAL.round(InvoiceSGST_Amt + SGST_AMT, 0);
                            System.out.println("InvoiceIGST_Amt = " + InvoiceIGST_Amt);
                            rsDR_ADJ_DETAIL.next();
                        } // end while

                        PartyDebitNoteAmount = EITLERPGLOBAL.round(PartyDebitNoteAmount + InvoiceDebitNoteAmount, 0);
                        PartyIGST_Amt = EITLERPGLOBAL.round(PartyIGST_Amt + InvoiceIGST_Amt, 0);
                        PartyCGST_Amt = EITLERPGLOBAL.round(PartyCGST_Amt + InvoiceCGST_Amt, 0);
                        PartySGST_Amt = EITLERPGLOBAL.round(PartySGST_Amt + InvoiceSGST_Amt, 0);
                        System.out.println("partyIGST_Amt = " + PartyIGST_Amt);

                        System.out.println("SGST_AMT2 = " + PartyIntDNAmount);
                        System.out.println("SGST_AMT3 = " + PartyDebitNoteAmount);
                        PartyIntDNAmount = EITLERPGLOBAL.round(PartyDebitNoteAmount - (PartyIGST_Amt + PartyCGST_Amt + PartySGST_Amt), 0);
                        System.out.println("Interest Amount1 = " + PartyIntDNAmount);
                        System.out.println("partyigst = " + PartyIGST_Amt);

                   // double diff1 = 0.00;
                        // double diff2 = 0.00;
                        //  diff1 =  (PartyIntDNAmount +PartyIGST_Amt+ PartyCGST_Amt+ PartySGST_Amt+0.00);
                        //  diff2 =  (PartyDebitNoteAmount - diff1 + 0.00);
                        //   System.out.println( "Diff1 = " +diff1 );                   
                        //  System.out.println( "Diff2 = " +diff2 );
                        //  PartyIntDNAmount = PartyIntDNAmount + diff2;
                        System.out.println("Interest Amount2 = " + PartyIntDNAmount);
                        if (EITLERPGLOBAL.round(PartyDebitNoteAmount, 0) > 5) {

                            ResultSet rsTmp = data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID=99");
                            rsTmp.first();
                            String SelPrefix = "", SelSuffix = "";
                            int FFNo = 0;
                            if (rsTmp.getRow() > 0) {
                                SelPrefix = rsTmp.getString("PREFIX_CHARS");
                                SelSuffix = rsTmp.getString("SUFFIX_CHARS");
                                FFNo = rsTmp.getInt("FIRSTFREE_NO");
                            }
                            //--------- Generate Debit Memo no.  ------------                
                            String DebitMemoNo = clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 99, FFNo, true);
                            //setAttribute("DEBITMEMO_NO", DebitMemoNo);
                            //-------------------------------------------------
                            System.out.println("DebitMemo No : " + DebitMemoNo);

                            Connection tmpConn = data.getConn(FinanceGlobal.FinURL);
                            Statement tmpStmt1 = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                            ResultSet rsTmp1 = tmpStmt1.executeQuery("SELECT * FROM D_FIN_DEBITMEMO_RECEIPT_MAPPING LIMIT 1 ");
                            for (int i = 1; i <= ObjRefItem.colMappingDetail.size(); i++) {
                                //clsDebitNoteReceiptMapping ObjItem = (clsDebitNoteReceiptMapping) ObjRefItem.colMappingDetail.get(Integer.toString(i));
                                clsDebitMemoReceiptMapping ObjItem = (clsDebitMemoReceiptMapping) ObjRefItem.colMappingDetail.get(Integer.toString(i));
                                rsTmp1.moveToInsertRow();
                                rsTmp1.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                rsTmp1.updateInt("SR_NO", i);
                                rsTmp1.updateString("DEBITMEMO_NO", DebitMemoNo);
                                rsTmp1.updateString("DEBITMEMO_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                //rsTmp1.updateString("RECEIPT_VOUCHER_NO",currentVoucherNo);
                                rsTmp1.updateString("RECEIPT_VOUCHER_NO", ObjItem.getAttribute("RECEIPT_VOUCHER_NO").getString());
                                rsTmp1.updateString("MAIN_ACCOUNT_CODE", ObjItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                                rsTmp1.updateString("DB_PARTY_CODE", ObjItem.getAttribute("DB_PARTY_CODE").getString());
                                rsTmp1.updateString("INVOICE_NO", ObjItem.getAttribute("INVOICE_NO").getString());
                                rsTmp1.updateString("INVOICE_DATE", ObjItem.getAttribute("INVOICE_DATE").getString());
                                rsTmp1.updateString("INVOICE_DUE_DATE", ObjItem.getAttribute("INVOICE_DUE_DATE").getString());
                                rsTmp1.updateString("VALUE_DATE", ObjItem.getAttribute("VALUE_DATE").getString());
                                rsTmp1.updateInt("DAYS", ObjItem.getAttribute("DAYS").getInt());
                                rsTmp1.updateInt("APPROVED", 1);
                                rsTmp1.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                rsTmp1.updateDouble("DEBIT_NOTE_AMOUNT", ObjItem.getAttribute("DEBIT_NOTE_AMOUNT").getDouble());
                                rsTmp1.updateDouble("IGST_PER", ObjItem.getAttribute("IGST_PER").getDouble());
                                rsTmp1.updateDouble("CGST_PER", ObjItem.getAttribute("CGST_PER").getDouble());
                                rsTmp1.updateDouble("SGST_PER", ObjItem.getAttribute("SGST_PER").getDouble());
                                rsTmp1.updateDouble("IGST_AMT", ObjItem.getAttribute("IGST_AMT").getDouble());
                                rsTmp1.updateDouble("CGST_AMT", ObjItem.getAttribute("CGST_AMT").getDouble());
                                rsTmp1.updateDouble("SGST_AMT", ObjItem.getAttribute("SGST_AMT").getDouble());
                                rsTmp1.updateDouble("INTEREST_PER", ObjItem.getAttribute("INTEREST_PER").getDouble());
                                rsTmp1.updateDouble("INTEREST_AMT", ObjItem.getAttribute("INTEREST_AMT").getDouble());
                                rsTmp1.updateDouble("INVOICE_AMOUNT", ObjItem.getAttribute("INVOICE_AMOUNT").getDouble());
                                rsTmp1.updateString("DEBITNOTE_VOUCHER_NO", "");
                                rsTmp1.updateString("DEBITNOTE_VOUCHER_DATE", "");
                                rsTmp1.updateString("DEBITMEMO_TYPE", "AUTO");

                                rsTmp1.updateBoolean("CHANGED", true);
                                rsTmp1.updateInt("CANCELLED", 0);
                                rsTmp1.insertRow();

                                String Msg = "Debit Memo No : " + DebitMemoNo + " posted for party code " + PartyCode + " of Rs." + ObjItem.getAttribute("DEBIT_NOTE_AMOUNT").getDouble();
                                JOptionPane.showMessageDialog(null, Msg);
                            }
                        } else {
                            if (clsSalesInvoice.canDebitNotePost(InvoiceType, ChargeCode)) {
                                JOptionPane.showMessageDialog(null, "Party's Debit Memo amount ( " + PartyDebitNoteAmount + " ) less then or equal to 5.\n Debit Memo not posted.");
                            }
                        }
                        
                        rsInvoice.next();
                    }

                    rsParty.next();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void PostDebitNoteSuitingsV2(String currentVoucherNo) {

        ResultSet rsParty = null, rsInvoice = null, rsDR_ADJ_DETAIL = null;
        clsVoucher ObjVoucher = null;
        //clsDrAdjustment ObjDrAdjustment = null;
        clsDebitMemoReceiptMapping ObjRefItem = null;

        String CurrentVoucherDate = data.getStringValueFromDB("SELECT DOC_DATE FROM D_FIN_DR_ADJ_HEADER WHERE DOC_NO='" + currentVoucherNo + "' ", FinanceGlobal.FinURL);
        System.out.println("CurrentVoucherDate : " + CurrentVoucherDate);
        //String CurrentVoucherDate = data.getStringValueFromDB("SELECT VOUCHER_DATE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='" + currentVoucherNo + "' ", FinanceGlobal.FinURL);

        //String strSQL = "SELECT DISTINCT SUB_ACCOUNT_CODE,MAIN_ACCOUNT_CODE FROM D_FIN_DR_ADJ_DETAIL WHERE DOC_NO='" + currentVoucherNo + "' "
        //        + " AND SUB_ACCOUNT_CODE<>'' AND INVOICE_NO<>'' AND MAIN_ACCOUNT_CODE IN ('210027') ORDER BY MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE";
        String strSQL = "SELECT DISTINCT SUB_ACCOUNT_CODE,MAIN_ACCOUNT_CODE FROM D_FIN_DR_ADJ_DETAIL WHERE DOC_NO='" + currentVoucherNo + "' "
                + " AND SUB_ACCOUNT_CODE<>'' AND INVOICE_NO<>'' AND MAIN_ACCOUNT_CODE IN ('210027') ORDER BY MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE"; //AND SUBSTRING(SJ_NO,1,2)<>'DN' 
        //String strSQL = "SELECT DISTINCT SUB_ACCOUNT_CODE,MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='" + currentVoucherNo + "' "
        //        + "AND EFFECT='C' AND SUB_ACCOUNT_CODE<>'' AND INVOICE_NO<>'' AND MAIN_ACCOUNT_CODE IN ('210027') ORDER BY MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE";
        //REMARKS : AND EFFECT='C' IS REMOVED FROM strSQL        
        System.out.println("strSQL 1 : " + strSQL);

        try {
            rsParty = data.getResult(strSQL, FinanceGlobal.FinURL);
            rsParty.first();
            String PartyCode = "", MainAccountCode = "", InvoiceNo = "", StateCode = "", InvoiceDate = "", ChargeCode = "", DeductionCode = "", DebitNoteBookCode = "", InvoiceDueDate = "";
            int InvoiceType = 0;
            double PartyDebitNoteAmount = 0, InvoiceDebitNoteAmount = 0, InterestDNAmount = 0, PartyIntDNAmount = 0, VoucherDebitNoteAmount = 0, InterestPercentage = 0, InvoicePaidAmount = 0, InvoiceAmount = 0;
            double IGST_PER = 0, CGST_PER = 0, SGST_PER = 0, IGST_AMT = 0, CGST_AMT = 0, SGST_AMT = 0, InvoiceIGST_Amt = 0, InvoiceSGST_Amt = 0, InvoiceCGST_Amt = 0, PartyIGST_Amt = 0, PartyCGST_Amt = 0, PartySGST_Amt = 0;
            if (rsParty.getRow() > 0) {
                while (!rsParty.isAfterLast()) {
                    PartyCode = UtilFunctions.getString(rsParty, "SUB_ACCOUNT_CODE", "");
                    MainAccountCode = UtilFunctions.getString(rsParty, "MAIN_ACCOUNT_CODE", "");

                    strSQL = "SELECT * FROM D_FIN_DR_ADJ_DETAIL WHERE DOC_NO='" + currentVoucherNo + "' "
                            + "AND INVOICE_NO<>'' AND SUB_ACCOUNT_CODE='" + PartyCode + "' AND MAIN_ACCOUNT_CODE='" + MainAccountCode + "' GROUP BY INVOICE_NO,INVOICE_DATE";

                    //strSQL = "SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='" + currentVoucherNo + "' AND EFFECT='C' "
                    //        + "AND INVOICE_NO<>'' AND SUB_ACCOUNT_CODE='" + PartyCode + "' AND MAIN_ACCOUNT_CODE='" + MainAccountCode + "' ";
                    //REMARKS : AND EFFECT='C' IS REMOVED FROM strSQL
                    System.out.println("strSQL 2 : " + strSQL);

                    rsInvoice = data.getResult(strSQL, FinanceGlobal.FinURL);
                    rsInvoice.first();
                    PartyDebitNoteAmount = 0;
                    PartyIGST_Amt = 0;
                    PartyCGST_Amt = 0;
                    PartySGST_Amt = 0;
                    PartyIntDNAmount = 0;

//                    ObjRefItem = new clsDebitMemoReceiptMapping();
//                    ObjRefItem.colMappingDetail.clear();
                    
                    while (!rsInvoice.isAfterLast()) {
                        
                        ObjRefItem = new clsDebitMemoReceiptMapping();
                        ObjRefItem.colMappingDetail.clear();
                        
                        InvoiceNo = UtilFunctions.getString(rsInvoice, "INVOICE_NO", "");
                        InvoiceDate = UtilFunctions.getString(rsInvoice, "INVOICE_DATE", "");
                        InvoiceType = clsSalesInvoice.getInvoiceType(InvoiceNo, InvoiceDate);
                        ChargeCode = clsSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate);
                        StateCode = clsSalesInvoice.getStateCode(PartyCode, MainAccountCode);
                        InvoiceAmount = clsSalesInvoice.getInvoiceAmount(InvoiceNo, InvoiceDate);
                        InvoicePaidAmount = clsSalesInvoice.getInvoicePaidAmount(InvoiceNo, InvoiceDate);
                        strSQL = "SELECT * FROM D_FIN_DEBITMEMO_RECEIPT_MAPPING WHERE RECEIPT_VOUCHER_NO='" + currentVoucherNo + "' "
                                + "AND INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' AND APPROVED=1 AND CANCELLED=0 ";
                        if (data.IsRecordExist(strSQL, FinanceGlobal.FinURL)) {
                            rsInvoice.next();
                            continue;
                        }
                        if (InvoiceAmount > InvoicePaidAmount) { // by pass the invoice if invoice in not fully paid for debit note
                            rsInvoice.next();
                            continue;
                        }
                        if (!clsSalesInvoice.canDebitNotePost(InvoiceType, ChargeCode)) {
                            rsInvoice.next();
                            continue;
                        }

                        InvoiceDueDate = data.getStringValueFromDB("SELECT DUE_DATE FROM DINESHMILLS.D_SAL_INVOICE_HEADER WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' AND APPROVED=1 AND CANCELLED=0 ");
                        //CHANGE BY RISHI ACCORDING TO ATUL 

                        //if(InvoiceType==2 && ChargeCode.equals("82")){
                        //InvoiceDueDate = data.getStringValueFromDB("SELECT ADDDATE(DUE_DATE,6) FROM DINESHMILLS.D_SAL_INVOICE_HEADER WHERE INVOICE_NO='"+InvoiceNo+"' AND INVOICE_DATE='"+InvoiceDate+"' AND APPROVED=1 AND CANCELLED=0 AND INVOICE_TYPE =2 AND SUBSTRING(PAYMENT_TERM_CODE,1,1) =8 ");
                        //}
                        if (InvoiceDueDate.equals("")) {
                            JOptionPane.showMessageDialog(null, "Due Date not exists in Invoice No : " + InvoiceNo + " Invoice Date : " + EITLERPGLOBAL.formatDate(InvoiceDate) + " Party Code : " + PartyCode + "\n Contact Account and Administrator.");
                            rsInvoice.next();
                            continue;
                        }

                        InvoiceDebitNoteAmount = 0;
                        InvoiceIGST_Amt = 0;
                        InvoiceCGST_Amt = 0;
                        InvoiceSGST_Amt = 0;

                        //strSQL = "SELECT A.DOC_NO, B.ADJUST_AMOUNT, B.VOUCHER_DATE FROM FINANCE.D_FIN_DR_ADJ_HEADER A, FINANCE.D_FIN_DR_ADJ_DETAIL B " +
                        //           "WHERE A.DOC_NO=B.DOC_NO AND B.INVOICE_NO='"+InvoiceNo+"' AND B.INVOICE_DATE='" + InvoiceDate + "' " +
                        //            "AND B.SUB_ACCOUNT_CODE='" + PartyCode + "' AND B.MAIN_ACCOUNT_CODE='" + MainAccountCode + "' AND A.APPROVED=1 " +
                        //            "AND A.CANCELLED=0 ORDER BY A.DOC_DATE ";
                        //change VALUE_DATE TO VOUCHER_DATE
                        //change AMOUNT TO ADJUST_AMOUNT
                        //AND B.EFFECT='C' IS REMOVED FROM QUERY
                        strSQL = "SELECT A.VOUCHER_NO, B.AMOUNT, B.VALUE_DATE FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B "
                                + "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND B.INVOICE_NO='" + InvoiceNo + "' AND B.INVOICE_DATE='" + InvoiceDate + "' "
                                + "AND B.EFFECT='C' AND B.SUB_ACCOUNT_CODE='" + PartyCode + "' AND B.MAIN_ACCOUNT_CODE='" + MainAccountCode + "' AND A.APPROVED=1 "
                                + "AND A.CANCELLED=0 ORDER BY A.VOUCHER_DATE ";

                        rsDR_ADJ_DETAIL = data.getResult(strSQL, FinanceGlobal.FinURL);
                        rsDR_ADJ_DETAIL.first();
                        // All Credit Record for Effect C & Same Party for same Invoice

                        while (!rsDR_ADJ_DETAIL.isAfterLast()) {

                            //String VoucherNo = UtilFunctions.getString(rsDR_ADJ_DETAIL, "DOC_NO", "");
                            String VoucherNo = UtilFunctions.getString(rsDR_ADJ_DETAIL, "VOUCHER_NO", "");
                            //double VoucherAmount = UtilFunctions.getDouble(rsDR_ADJ_DETAIL, "ADJUST_AMOUNT", 0);
                            double VoucherAmount = UtilFunctions.getDouble(rsDR_ADJ_DETAIL, "AMOUNT", 0);
                            //String ValueDate = UtilFunctions.getString(rsDR_ADJ_DETAIL, "VOUCHER_DATE", "");
                            String ValueDate = UtilFunctions.getString(rsDR_ADJ_DETAIL, "VALUE_DATE", "");
                            if (ValueDate.equals("") || ValueDate.equals("0000-00-00")) {
                                JOptionPane.showMessageDialog(null, "Value Date not exists in Voucher No : " + VoucherNo + " against Invoice No : " + InvoiceNo + " Invoice Date : " + EITLERPGLOBAL.formatDate(InvoiceDate) + " Party Code : " + PartyCode + "\nDebit note can not post.\nContact Account and Administrator.");
                            }
                            if (java.sql.Date.valueOf(ValueDate).before(java.sql.Date.valueOf(InvoiceDueDate)) || java.sql.Date.valueOf(ValueDate).compareTo(java.sql.Date.valueOf(InvoiceDueDate)) == 0) {
                                rsDR_ADJ_DETAIL.next();
                                continue;
                            }
                            VoucherDebitNoteAmount = 0;
                            InterestDNAmount = 0;
                            IGST_AMT = 0;
                            CGST_AMT = 0;
                            SGST_AMT = 0;

                            // calculate debit note amount start
                            int InterestDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(InvoiceDueDate), java.sql.Date.valueOf(ValueDate));
                            InterestPercentage = clsSalesInvoice.getDebitNotePercentage(ValueDate, InvoiceType, ChargeCode, PartyCode);
                            // VoucherDebitNoteAmount = EITLERPGLOBAL.round(((VoucherAmount * InterestPercentage * InterestDays) / (365 * 100)), 2);
                            InterestDNAmount = EITLERPGLOBAL.round(((VoucherAmount * InterestPercentage * InterestDays) / (365 * 100)), 0);
                            /* 
                             IGST_PER = clsSalesInvoice.getIGSTPER(InvoiceNo, InvoiceDate,MainAccountCode,InvoiceType,StateCode);
                             IGST_AMT =  EITLERPGLOBAL.round((InterestDNAmount * (IGST_PER/100)),0);
                             CGST_PER = clsSalesInvoice.getCGSTPER(InvoiceNo, InvoiceDate,MainAccountCode,InvoiceType,StateCode);
                             CGST_AMT =  EITLERPGLOBAL.round((InterestDNAmount * (SGST_PER/100)),0);
                             SGST_PER = clsSalesInvoice.getSGSTPER(InvoiceNo, InvoiceDate,MainAccountCode,InvoiceType,StateCode);
                             SGST_AMT =  EITLERPGLOBAL.round((InterestDNAmount * (SGST_PER/100)),0);
                        
                             
                             System.out.println( "IGST_AMT = " +IGST_AMT );       
                             System.out.println( "SGST_AMT = " +SGST_AMT );
                             */

                            if (IGST_PER != 0) {
                                VoucherDebitNoteAmount = InterestDNAmount + IGST_AMT;
                            } else {
                                VoucherDebitNoteAmount = InterestDNAmount + SGST_AMT + CGST_AMT;
                            }

                            System.out.println("InterestDNAmount = " + InterestDNAmount);
                            System.out.println("VoucherDN1 = " + VoucherDebitNoteAmount);
                            // calculate debit note amount end
                            //clsDebitNoteReceiptMapping ObjtempItem = new clsDebitNoteReceiptMapping();
                            clsDebitMemoReceiptMapping ObjtempItem = new clsDebitMemoReceiptMapping();
                            ObjtempItem.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                            ObjtempItem.setAttribute("SR_NO", (ObjRefItem.colMappingDetail.size() + 1));
                            ObjtempItem.setAttribute("DEBITMEMO_NO", "");
                            ObjtempItem.setAttribute("RECEIPT_VOUCHER_NO", VoucherNo);
                            ObjtempItem.setAttribute("INVOICE_NO", InvoiceNo);
                            ObjtempItem.setAttribute("INVOICE_DATE", InvoiceDate);
                            ObjtempItem.setAttribute("INVOICE_DUE_DATE", InvoiceDueDate);
                            ObjtempItem.setAttribute("VALUE_DATE", ValueDate);
                            ObjtempItem.setAttribute("DAYS", InterestDays);
                            ObjtempItem.setAttribute("DEBIT_NOTE_AMOUNT", EITLERPGLOBAL.round(VoucherDebitNoteAmount, 0));

                            ObjtempItem.setAttribute("IGST_PER", IGST_PER);
                            ObjtempItem.setAttribute("CGST_PER", CGST_PER);
                            ObjtempItem.setAttribute("SGST_PER", SGST_PER);
                            ObjtempItem.setAttribute("IGST_AMT", IGST_AMT);
                            ObjtempItem.setAttribute("CGST_AMT", CGST_AMT);
                            ObjtempItem.setAttribute("SGST_AMT", SGST_AMT);
                            ObjtempItem.setAttribute("INTEREST_AMT", InterestDNAmount);
                            ObjtempItem.setAttribute("INTEREST_PER", InterestPercentage);
                            ObjtempItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                            ObjtempItem.setAttribute("DB_PARTY_CODE", PartyCode);
                            ObjtempItem.setAttribute("MAIN_ACCOUNT_CODE", MainAccountCode);
                            ObjtempItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                            ObjtempItem.setAttribute("DEBITNOTE_VOUCHER_NO", "");

                            ObjtempItem.setAttribute("APPROVED", 1);
                            ObjtempItem.setAttribute("APPROVED", EITLERPGLOBAL.getCurrentDateDB());
                            ObjtempItem.setAttribute("CHANGED", 1);
                            ObjRefItem.colMappingDetail.put(Integer.toString(ObjRefItem.colMappingDetail.size() + 1), ObjtempItem);

                            InvoiceDebitNoteAmount = EITLERPGLOBAL.round(InvoiceDebitNoteAmount + VoucherDebitNoteAmount, 0);

                            InvoiceIGST_Amt = EITLERPGLOBAL.round(InvoiceIGST_Amt + IGST_AMT, 0);
                            InvoiceCGST_Amt = EITLERPGLOBAL.round(InvoiceCGST_Amt + CGST_AMT, 0);
                            InvoiceSGST_Amt = EITLERPGLOBAL.round(InvoiceSGST_Amt + SGST_AMT, 0);
                            System.out.println("InvoiceIGST_Amt = " + InvoiceIGST_Amt);
                            rsDR_ADJ_DETAIL.next();
                        } // end while

                        PartyDebitNoteAmount = EITLERPGLOBAL.round(PartyDebitNoteAmount + InvoiceDebitNoteAmount, 0);
                        PartyIGST_Amt = EITLERPGLOBAL.round(PartyIGST_Amt + InvoiceIGST_Amt, 0);
                        PartyCGST_Amt = EITLERPGLOBAL.round(PartyCGST_Amt + InvoiceCGST_Amt, 0);
                        PartySGST_Amt = EITLERPGLOBAL.round(PartySGST_Amt + InvoiceSGST_Amt, 0);
                        System.out.println("partyIGST_Amt = " + PartyIGST_Amt);

                        System.out.println("SGST_AMT2 = " + PartyIntDNAmount);
                        System.out.println("SGST_AMT3 = " + PartyDebitNoteAmount);
                        PartyIntDNAmount = EITLERPGLOBAL.round(PartyDebitNoteAmount - (PartyIGST_Amt + PartyCGST_Amt + PartySGST_Amt), 0);
                        System.out.println("Interest Amount1 = " + PartyIntDNAmount);
                        System.out.println("partyigst = " + PartyIGST_Amt);

                   // double diff1 = 0.00;
                        // double diff2 = 0.00;
                        //  diff1 =  (PartyIntDNAmount +PartyIGST_Amt+ PartyCGST_Amt+ PartySGST_Amt+0.00);
                        //  diff2 =  (PartyDebitNoteAmount - diff1 + 0.00);
                        //   System.out.println( "Diff1 = " +diff1 );                   
                        //  System.out.println( "Diff2 = " +diff2 );
                        //  PartyIntDNAmount = PartyIntDNAmount + diff2;
                        System.out.println("Interest Amount2 = " + PartyIntDNAmount);
                        if (EITLERPGLOBAL.round(PartyDebitNoteAmount, 0) > 5) {

                            ResultSet rsTmp = data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID=99");
                            rsTmp.first();
                            String SelPrefix = "", SelSuffix = "";
                            int FFNo = 0;
                            if (rsTmp.getRow() > 0) {
                                SelPrefix = rsTmp.getString("PREFIX_CHARS");
                                SelSuffix = rsTmp.getString("SUFFIX_CHARS");
                                FFNo = rsTmp.getInt("FIRSTFREE_NO");
                            }
                            //--------- Generate Debit Memo no.  ------------                
                            String DebitMemoNo = clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 99, FFNo, true);
                //setAttribute("DEBITMEMO_NO", DebitMemoNo);
                            //-------------------------------------------------
                            System.out.println("DebitMemo No : " + DebitMemoNo);

                            Connection tmpConn = data.getConn(FinanceGlobal.FinURL);
                            Statement tmpStmt1 = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                            ResultSet rsTmp1 = tmpStmt1.executeQuery("SELECT * FROM D_FIN_DEBITMEMO_RECEIPT_MAPPING LIMIT 1 ");
                            for (int i = 1; i <= ObjRefItem.colMappingDetail.size(); i++) {
                                //clsDebitNoteReceiptMapping ObjItem = (clsDebitNoteReceiptMapping) ObjRefItem.colMappingDetail.get(Integer.toString(i));
                                clsDebitMemoReceiptMapping ObjItem = (clsDebitMemoReceiptMapping) ObjRefItem.colMappingDetail.get(Integer.toString(i));
                                rsTmp1.moveToInsertRow();
                                rsTmp1.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                rsTmp1.updateInt("SR_NO", i);
                                rsTmp1.updateString("DEBITMEMO_NO", DebitMemoNo);
                                rsTmp1.updateString("DEBITMEMO_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                //rsTmp1.updateString("RECEIPT_VOUCHER_NO",currentVoucherNo);
                                rsTmp1.updateString("RECEIPT_VOUCHER_NO", ObjItem.getAttribute("RECEIPT_VOUCHER_NO").getString());
                                rsTmp1.updateString("MAIN_ACCOUNT_CODE", ObjItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                                rsTmp1.updateString("DB_PARTY_CODE", ObjItem.getAttribute("DB_PARTY_CODE").getString());
                                rsTmp1.updateString("INVOICE_NO", ObjItem.getAttribute("INVOICE_NO").getString());
                                rsTmp1.updateString("INVOICE_DATE", ObjItem.getAttribute("INVOICE_DATE").getString());
                                rsTmp1.updateString("INVOICE_DUE_DATE", ObjItem.getAttribute("INVOICE_DUE_DATE").getString());
                                rsTmp1.updateString("VALUE_DATE", ObjItem.getAttribute("VALUE_DATE").getString());
                                rsTmp1.updateInt("DAYS", ObjItem.getAttribute("DAYS").getInt());
                                rsTmp1.updateInt("APPROVED", 1);
                                rsTmp1.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                rsTmp1.updateDouble("DEBIT_NOTE_AMOUNT", ObjItem.getAttribute("DEBIT_NOTE_AMOUNT").getDouble());
                                rsTmp1.updateDouble("IGST_PER", ObjItem.getAttribute("IGST_PER").getDouble());
                                rsTmp1.updateDouble("CGST_PER", ObjItem.getAttribute("CGST_PER").getDouble());
                                rsTmp1.updateDouble("SGST_PER", ObjItem.getAttribute("SGST_PER").getDouble());
                                rsTmp1.updateDouble("IGST_AMT", ObjItem.getAttribute("IGST_AMT").getDouble());
                                rsTmp1.updateDouble("CGST_AMT", ObjItem.getAttribute("CGST_AMT").getDouble());
                                rsTmp1.updateDouble("SGST_AMT", ObjItem.getAttribute("SGST_AMT").getDouble());
                                rsTmp1.updateDouble("INTEREST_PER", ObjItem.getAttribute("INTEREST_PER").getDouble());
                                rsTmp1.updateDouble("INTEREST_AMT", ObjItem.getAttribute("INTEREST_AMT").getDouble());
                                rsTmp1.updateDouble("INVOICE_AMOUNT", ObjItem.getAttribute("INVOICE_AMOUNT").getDouble());
                                rsTmp1.updateString("DEBITNOTE_VOUCHER_NO", "");
                                rsTmp1.updateString("DEBITNOTE_VOUCHER_DATE", "");
                                rsTmp1.updateString("DEBITMEMO_TYPE", "AUTO");

                                rsTmp1.updateBoolean("CHANGED", true);
                                rsTmp1.updateInt("CANCELLED", 0);
                                rsTmp1.insertRow();

                                String Msg = "Debit Memo No : " + DebitMemoNo + " posted for party code " + PartyCode + " of Rs." + ObjItem.getAttribute("DEBIT_NOTE_AMOUNT").getDouble();
                                JOptionPane.showMessageDialog(null, Msg);
                            }
                        } else {
                            if (clsSalesInvoice.canDebitNotePost(InvoiceType, ChargeCode)) {
                                JOptionPane.showMessageDialog(null, "Party's Debit Memo amount ( " + PartyDebitNoteAmount + " ) less then or equal to 5.\n Debit Memo not posted.");
                            }
                        }
                        
                        rsInvoice.next();
                    }

                    rsParty.next();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void PostTCSDebitNoteFelt(String currentVoucherNo) {
        ResultSet rsParty = null, rsInvoice = null, rsVoucher = null;
        clsVoucher ObjVoucher = null;
        clsDebitNoteReceiptMapping ObjRefItem = null;
        String CurrentVoucherDate = data.getStringValueFromDB("SELECT DOC_DATE FROM D_FIN_DR_ADJ_HEADER WHERE DOC_NO='" + currentVoucherNo + "' ", FinanceGlobal.FinURL);
//        String strSQL = "SELECT SUB_ACCOUNT_CODE,MAIN_ACCOUNT_CODE,INVOICE_NO,INVOICE_DATE FROM D_FIN_DR_ADJ_DETAIL "
//                + "WHERE DOC_NO='" + currentVoucherNo + "' AND SUB_ACCOUNT_CODE<>'' AND INVOICE_NO<>'' AND MAIN_ACCOUNT_CODE IN ('210010') "
//                + "AND CONCAT(INVOICE_NO,INVOICE_DATE) NOT IN (SELECT CONCAT(INVOICE_NO,INVOICE_DATE) FROM FINANCE.D_FIN_DEBITNOTE_RECEIPT_MAPPING WHERE TCS_AMT>0) "
//                + "GROUP BY INVOICE_NO,INVOICE_DATE "
//                + "ORDER BY MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE";

	String strSQL = "SELECT D.SUB_ACCOUNT_CODE,D.MAIN_ACCOUNT_CODE,D.INVOICE_NO,D.INVOICE_DATE "
                + "FROM FINANCE.D_FIN_DR_ADJ_DETAIL D,PRODUCTION.FELT_SAL_INVOICE_HEADER I "
                + "WHERE D.INVOICE_NO=I.INVOICE_NO AND D.INVOICE_DATE=I.INVOICE_DATE AND D.SUB_ACCOUNT_CODE=I.PARTY_CODE AND I.TCS_AMT=0 "
                + "AND D.DOC_NO='" + currentVoucherNo + "' AND D.SUB_ACCOUNT_CODE<>'' AND D.INVOICE_NO<>'' AND D.MAIN_ACCOUNT_CODE IN ('210010') "
                + "AND CONCAT(D.INVOICE_NO,D.INVOICE_DATE) NOT IN (SELECT CONCAT(INVOICE_NO,INVOICE_DATE) FROM FINANCE.D_FIN_DEBITNOTE_RECEIPT_MAPPING WHERE TCS_AMT>0) "
                + "GROUP BY D.INVOICE_NO,D.INVOICE_DATE "
                + "ORDER BY D.MAIN_ACCOUNT_CODE,D.SUB_ACCOUNT_CODE";


        try {
            rsParty = data.getResult(strSQL, FinanceGlobal.FinURL);
            rsParty.first();
            String PartyCode = "", MainAccountCode = "", InvoiceNo = "", InvoiceDate = "";
            double PartyDebitNoteAmount = 0, Invoice_Amt = 0;

            if (rsParty.getRow() > 0) {
                while (!rsParty.isAfterLast()) {
                    PartyCode = UtilFunctions.getString(rsParty, "SUB_ACCOUNT_CODE", "");
                    MainAccountCode = UtilFunctions.getString(rsParty, "MAIN_ACCOUNT_CODE", "");
                    InvoiceNo = UtilFunctions.getString(rsParty, "INVOICE_NO", "");
                    InvoiceDate = UtilFunctions.getString(rsParty, "INVOICE_DATE", "");
                    Invoice_Amt = data.getDoubleValueFromDB("SELECT INVOICE_AMT FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ");

//                    PartyDebitNoteAmount = Math.round((Invoice_Amt * 0.075) / 100);
                    PartyDebitNoteAmount = Math.round((Invoice_Amt * 0.100) / 100);

                    ObjRefItem = new clsDebitNoteReceiptMapping();
                    ObjRefItem.colMappingDetail.clear();

                    clsDebitNoteReceiptMapping ObjtempItem = new clsDebitNoteReceiptMapping();
                    ObjtempItem.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                    ObjtempItem.setAttribute("SR_NO", (ObjRefItem.colMappingDetail.size() + 1));
                    ObjtempItem.setAttribute("DEBITNOTE_VOUCHER_NO", "");
                    ObjtempItem.setAttribute("DEBITNOTE_VOUCHER_DATE", "");
                    ObjtempItem.setAttribute("RECEIPT_VOUCHER_NO", currentVoucherNo);
                    ObjtempItem.setAttribute("INVOICE_NO", InvoiceNo);
                    ObjtempItem.setAttribute("INVOICE_DATE", InvoiceDate);
                    ObjtempItem.setAttribute("INVOICE_DUE_DATE", "");
                    ObjtempItem.setAttribute("VALUE_DATE", "");
                    ObjtempItem.setAttribute("DAYS", 0);
                    ObjtempItem.setAttribute("DEBIT_NOTE_AMOUNT", PartyDebitNoteAmount);
                    ObjtempItem.setAttribute("IGST_PER", 0);
                    ObjtempItem.setAttribute("CGST_PER", 0);
                    ObjtempItem.setAttribute("SGST_PER", 0);
                    ObjtempItem.setAttribute("IGST_AMT", 0);
                    ObjtempItem.setAttribute("CGST_AMT", 0);
                    ObjtempItem.setAttribute("SGST_AMT", 0);
                    ObjtempItem.setAttribute("INTEREST_AMT", 0);
                    ObjtempItem.setAttribute("INTEREST_PER", 0);
                    ObjtempItem.setAttribute("DB_PARTY_CODE", PartyCode);
                    ObjtempItem.setAttribute("MAIN_ACCOUNT_CODE", MainAccountCode);
                    ObjtempItem.setAttribute("INVOICE_AMOUNT", Invoice_Amt);
//                    ObjtempItem.setAttribute("TCS_PER", 0.075);
                    ObjtempItem.setAttribute("TCS_PER", 0.100);
                    ObjtempItem.setAttribute("TCS_AMT", PartyDebitNoteAmount);

                    ObjtempItem.setAttribute("APPROVED", 1);
                    ObjtempItem.setAttribute("APPROVED", EITLERPGLOBAL.getCurrentDateDB());
                    ObjtempItem.setAttribute("CHANGED", 1);
                    ObjRefItem.colMappingDetail.put(Integer.toString(ObjRefItem.colMappingDetail.size() + 1), ObjtempItem);

                    if (PartyDebitNoteAmount > 0) {

                        /*========== Select the Hierarchy ======== */
                        int HierarchyID = 0;
                        HashMap List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsVoucher.DebitNoteModuleID, "CHOOSE_HIERARCHY", "DEFAULT", "");
                        if (List.size() > 0) {
                            //Get the Result of the Rule which would be the hierarchy no.
                            clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                            HierarchyID = UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                        }
                        /*========== End of Hierarchy Selection ======== */

//                        // get Book code and deduction code start
//                        List.clear();
//                        List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsVoucher.DebitNoteModuleID, "GET_BOOK_CODE", ChargeCode.substring(0, 1), Integer.toString(InvoiceType));
//                        if (List.size() > 0) {
//                            //Get the Result of the Rule which would be the hierarchy no.
//                            clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
//                            DebitNoteBookCode = objRule.getAttribute("RULE_OUTCOME").getString();
//                        }
//
//                        List.clear();
//                        List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsVoucher.DebitNoteModuleID, "GET_ACCOUNT_CODE", "DEDUCTION_CODE", Integer.toString(InvoiceType));
//                        if (List.size() > 0) {
//                            clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
//                            DeductionCode = objRule.getAttribute("RULE_OUTCOME").getString();
//                        }
                        ResultSet rsTmp = data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID=" + clsVoucher.DebitNoteModuleID);
                        rsTmp.first();
                        String SelPrefix = "", SelSuffix = "";
                        int FFNo = 0;
                        if (rsTmp.getRow() > 0) {
                            SelPrefix = rsTmp.getString("PREFIX_CHARS");
                            SelSuffix = rsTmp.getString("SUFFIX_CHARS");
                            FFNo = rsTmp.getInt("FIRSTFREE_NO");
                        }

                        int VoucherSrNo = 0;
                        ObjVoucher = new clsVoucher();
                        ObjVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
                        ObjVoucher.colVoucherItems.clear();
                        ObjVoucher.setAttribute("PREFIX", SelPrefix);
                        ObjVoucher.setAttribute("SUFFIX", SelSuffix);
                        ObjVoucher.setAttribute("FFNO", FFNo);
                        ObjVoucher.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        ObjVoucher.setAttribute("VOUCHER_NO", "");
                        ObjVoucher.setAttribute("LEGACY_NO", "");
                        ObjVoucher.setAttribute("LEGACY_DATE", "");
                        ObjVoucher.setAttribute("BOOK_CODE", "18");
                        ObjVoucher.setAttribute("VOUCHER_TYPE", FinanceGlobal.TYPE_DEBIT_NOTE);
                        ObjVoucher.setAttribute("CHEQUE_NO", "");
                        ObjVoucher.setAttribute("CHEQUE_DATE", "");
                        ObjVoucher.setAttribute("BANK_NAME", "");
                        ObjVoucher.setAttribute("VOUCHER_DATE", EITLERPGLOBAL.getCurrentDate());
//                        ObjVoucher.setAttribute("VOUCHER_DATE", EITLERPGLOBAL.formatDate(CurrentVoucherDate));
                        ObjVoucher.setAttribute("ST_CATEGORY", "");
                        ObjVoucher.setAttribute("MODULE_ID", 0);
                        ObjVoucher.setAttribute("REMARKS", "Generated By Auto Debit Note System. TCS PAYABLE AG.RECEIPT VOU.NO-" + currentVoucherNo + " DTD:" + EITLERPGLOBAL.formatDate(CurrentVoucherDate) + " ON INV.NO-" + InvoiceNo + " INV.DT:" + EITLERPGLOBAL.formatDate(InvoiceDate) + ". This debit note has been raised in pursuant to section 206(1H) of IT ACT. This amount would appear in Form 26AS and the same has not been charged in our invoice.");
                        ObjVoucher.setAttribute("HIERARCHY_ID", HierarchyID);
                        int FirstUserID = data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID=" + HierarchyID + " AND SR_NO=1");
                        ObjVoucher.setAttribute("FROM", FirstUserID);
                        ObjVoucher.setAttribute("TO", FirstUserID);
                        ObjVoucher.setAttribute("FROM_REMARKS", "");
                        ObjVoucher.setAttribute("APPROVAL_STATUS", "F"); //Final Approved --> Voucher

                        clsVoucherItem objVoucherItem = new clsVoucherItem();
                        VoucherSrNo++;
                        objVoucherItem.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        objVoucherItem.setAttribute("VOUCHER_NO", "");
                        objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                        objVoucherItem.setAttribute("EFFECT", "D");
                        objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                        objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "210010");
                        objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", PartyCode);
                        objVoucherItem.setAttribute("AMOUNT", PartyDebitNoteAmount);
                        objVoucherItem.setAttribute("APPLICABLE_AMOUNT", 0);
                        objVoucherItem.setAttribute("PERCENTAGE", 0);
                        objVoucherItem.setAttribute("REMARKS", "");
                        objVoucherItem.setAttribute("PO_NO", "");
                        objVoucherItem.setAttribute("PO_DATE", "");
                        objVoucherItem.setAttribute("VALUE_DATE", "");
                        objVoucherItem.setAttribute("INVOICE_NO", "");
                        objVoucherItem.setAttribute("INVOICE_DATE", "");
                        objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                        objVoucherItem.setAttribute("LINK_NO", "");
                        objVoucherItem.setAttribute("GRN_NO", "");
                        objVoucherItem.setAttribute("GRN_DATE", "");
                        objVoucherItem.setAttribute("MODULE_ID", 0);
                        objVoucherItem.setAttribute("IS_DEDUCTION", 0);
                        objVoucherItem.setAttribute("REF_VOUCHER_NO", "");
                        objVoucherItem.setAttribute("REF_VOUCHER_TYPE", "");
                        objVoucherItem.setAttribute("REF_VOUCHER_COMPANY_ID", 0);
                        objVoucherItem.setAttribute("MATCHED", 0);
                        objVoucherItem.setAttribute("MATCHED_DATE", "");
                        objVoucherItem.setAttribute("REF_SR_NO", 0);
                        ObjVoucher.colVoucherItems.put(Integer.toString(ObjVoucher.colVoucherItems.size() + 1), objVoucherItem);

                        objVoucherItem = new clsVoucherItem();
                        VoucherSrNo++;
                        objVoucherItem.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        objVoucherItem.setAttribute("VOUCHER_NO", "");
                        objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                        objVoucherItem.setAttribute("EFFECT", "C");
                        objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                        objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "183185");
                        objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                        objVoucherItem.setAttribute("AMOUNT", PartyDebitNoteAmount);
                        objVoucherItem.setAttribute("APPLICABLE_AMOUNT", 0);
                        objVoucherItem.setAttribute("PERCENTAGE", 0);
                        objVoucherItem.setAttribute("REMARKS", "");
                        objVoucherItem.setAttribute("PO_NO", "");
                        objVoucherItem.setAttribute("PO_DATE", "");
                        objVoucherItem.setAttribute("VALUE_DATE", "");
                        objVoucherItem.setAttribute("INVOICE_NO", "");
                        objVoucherItem.setAttribute("INVOICE_DATE", "");
                        objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                        objVoucherItem.setAttribute("LINK_NO", "");
                        objVoucherItem.setAttribute("GRN_NO", "");
                        objVoucherItem.setAttribute("GRN_DATE", "");
                        objVoucherItem.setAttribute("MODULE_ID", 0);
                        objVoucherItem.setAttribute("IS_DEDUCTION", 0);
                        objVoucherItem.setAttribute("REF_VOUCHER_NO", "");
                        objVoucherItem.setAttribute("REF_VOUCHER_TYPE", "");
                        objVoucherItem.setAttribute("REF_VOUCHER_COMPANY_ID", 0);
                        objVoucherItem.setAttribute("MATCHED", 0);
                        objVoucherItem.setAttribute("MATCHED_DATE", "");
                        objVoucherItem.setAttribute("REF_SR_NO", 0);
                        ObjVoucher.colVoucherItems.put(Integer.toString(ObjVoucher.colVoucherItems.size() + 1), objVoucherItem);

                        clsVoucher.bypass = 1;

                        if (ObjVoucher.Insert()) {
                            String theVoucherNo = ObjVoucher.getAttribute("VOUCHER_NO").getString();
                            String theVoucherDate = EITLERPGLOBAL.formatDateDB(ObjVoucher.getAttribute("VOUCHER_DATE").getString());

                            String Msg = "Debit Note No : " + theVoucherNo + " posted for party code " + PartyCode + " of Rs." + PartyDebitNoteAmount;
                            String LinkNo = EITLERPGLOBAL.padLeftEx(theVoucherNo.substring(theVoucherNo.length() - 5), "0", 6) + "/" + EITLERPGLOBAL.getFinYearEndDate(EITLERPGLOBAL.getCurrentDateDB()).substring(2, 4) + EITLERPGLOBAL.getFinYearStartDate(EITLERPGLOBAL.getCurrentDateDB()).substring(2, 4);
                            data.Execute("UPDATE D_FIN_VOUCHER_DETAIL SET LINK_NO='" + LinkNo + "' WHERE VOUCHER_NO='" + theVoucherNo + "' ", FinanceGlobal.FinURL);
                            data.Execute("UPDATE D_FIN_VOUCHER_DETAIL_EX SET LINK_NO='" + LinkNo + "' WHERE VOUCHER_NO='" + theVoucherNo + "' ", FinanceGlobal.FinURL);
                            JOptionPane.showMessageDialog(null, Msg);
                            Connection tmpConn = data.getConn(FinanceGlobal.FinURL);
                            Statement tmpStmt1 = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                            ResultSet rsTmp1 = tmpStmt1.executeQuery("SELECT * FROM D_FIN_DEBITNOTE_RECEIPT_MAPPING LIMIT 1 ");
                            for (int i = 1; i <= ObjRefItem.colMappingDetail.size(); i++) {
                                clsDebitNoteReceiptMapping ObjItem = (clsDebitNoteReceiptMapping) ObjRefItem.colMappingDetail.get(Integer.toString(i));
                                rsTmp1.moveToInsertRow();
                                rsTmp1.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                rsTmp1.updateInt("SR_NO", i);
                                rsTmp1.updateString("DEBITNOTE_VOUCHER_NO", theVoucherNo);
                                rsTmp1.updateString("DEBITNOTE_VOUCHER_DATE", theVoucherDate);
                                //rsTmp1.updateString("RECEIPT_VOUCHER_NO",currentVoucherNo);
                                rsTmp1.updateString("RECEIPT_VOUCHER_NO", ObjItem.getAttribute("RECEIPT_VOUCHER_NO").getString());
                                rsTmp1.updateString("MAIN_ACCOUNT_CODE", ObjItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                                rsTmp1.updateString("DB_PARTY_CODE", ObjItem.getAttribute("DB_PARTY_CODE").getString());
                                rsTmp1.updateString("INVOICE_NO", ObjItem.getAttribute("INVOICE_NO").getString());
                                rsTmp1.updateString("INVOICE_DATE", ObjItem.getAttribute("INVOICE_DATE").getString());
                                rsTmp1.updateString("INVOICE_DUE_DATE", ObjItem.getAttribute("INVOICE_DUE_DATE").getString());
                                rsTmp1.updateString("VALUE_DATE", ObjItem.getAttribute("VALUE_DATE").getString());
                                rsTmp1.updateInt("DAYS", ObjItem.getAttribute("DAYS").getInt());
                                rsTmp1.updateInt("APPROVED", 1);
                                rsTmp1.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                rsTmp1.updateDouble("DEBIT_NOTE_AMOUNT", ObjItem.getAttribute("DEBIT_NOTE_AMOUNT").getDouble());
                                rsTmp1.updateDouble("IGST_PER", ObjItem.getAttribute("IGST_PER").getDouble());
                                rsTmp1.updateDouble("CGST_PER", ObjItem.getAttribute("CGST_PER").getDouble());
                                rsTmp1.updateDouble("SGST_PER", ObjItem.getAttribute("SGST_PER").getDouble());
                                rsTmp1.updateDouble("IGST_AMT", ObjItem.getAttribute("IGST_AMT").getDouble());
                                rsTmp1.updateDouble("CGST_AMT", ObjItem.getAttribute("CGST_AMT").getDouble());
                                rsTmp1.updateDouble("SGST_AMT", ObjItem.getAttribute("SGST_AMT").getDouble());
                                rsTmp1.updateDouble("INTEREST_PER", ObjItem.getAttribute("INTEREST_PER").getDouble());
                                rsTmp1.updateDouble("INTEREST_AMT", ObjItem.getAttribute("INTEREST_AMT").getDouble());
                                rsTmp1.updateDouble("INVOICE_AMOUNT", ObjItem.getAttribute("INVOICE_AMOUNT").getDouble());
                                rsTmp1.updateDouble("TCS_PER", ObjItem.getAttribute("TCS_PER").getDouble());
                                rsTmp1.updateDouble("TCS_AMT", ObjItem.getAttribute("TCS_AMT").getDouble());

                                rsTmp1.updateBoolean("CHANGED", true);
                                rsTmp1.updateInt("CANCELLED", 0);
                                rsTmp1.insertRow();
                            }
                        }
                    }
                    rsParty.next();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
