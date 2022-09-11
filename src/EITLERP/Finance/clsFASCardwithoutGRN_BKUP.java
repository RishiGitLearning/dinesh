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
import EITLERP.Stores.*;
import EITLERP.Sales.*;
import EITLERP.Finance.ReportsUI.clsPartyOutstandingItems;

/**
 *
 * @author  Nitin Pithva
 * @version
 */

public class clsFASCardwithoutGRN_BKUP {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    //Requisition Material Collection
    public HashMap colFASItemsDetail=new HashMap();
    public HashMap colFASItemsDetailExIT=new HashMap();
    public HashMap colFASItemsDetailExBook=new HashMap();
    //public HashMap colVoucherItemsEx=new HashMap();
    public HashMap colOthers;
    //History Related properties
    private boolean HistoryView=false;
    
    public static int ModuleID=189;
    public static int AssetType = 2; // 1 - with GRN , 2 - without GRN
    public static int DepreciationType = 1; // 1 - Book Value, 2 - IT Value
    public boolean DoNotValidateAccounts=false;
    public boolean IsInternalPosting=false;
    private int BlockCounter=0;
    
    //public boolean UseSpecificVoucherNo=false;
    //public String SpecificVoucherNo="";
    //public boolean ChangeVoucherNo=false;
    //public String OldVoucherNo="";
    
    //public int VoucherType=1;
    
    //public clsVoucher objPayment;
    /*
    public static final int PaymentModuleID=66;
    public static final int Payment2ModuleID=90;
    public static final int DebitNoteModuleID=65;
    public static final int PJVModuleID=59;
    public static final int CashVoucherModuleID=67;
    public static final int CashReceiptVoucherModuleID=83;
    public static final int SalesJournalVoucherModuleID=68;
    public static final int ReceiptVoucherModuleID=69;
    public static final int CreditNoteVoucherModuleID=70;
    public static final int JournalVoucherModuleID=89;
    public static final int SalesJVModuleID=94;
     */
    private String TVoucherNo = "";
    private String EmployeeNo = "";
    
    public Variant getAttribute(String PropName) {
        if(!props.containsKey(PropName)) {
            return (new Variant(""));
        }
        else {
            return (Variant) props.get(PropName);
        }
    }
    
    public void setAttribute(String PropName,String Value) {
        props.put(PropName,new Variant(Value));
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
    
   /* public static int getVoucherModuleID(int VoucherType) {
        switch(VoucherType) {
            case 1: return 59;
            case 2: return 66;
            case 3: return 65;
            case 4: return 67;
            case 5: return 68;
            case 6: return 69;
            case 7: return 70;
            case 8: return 83;
            case 9: return 89;
            case 10: return 90;
            case 11: return 94;
            default: return 59;
        }
    }*/
    
    /** Creates new clsMaterialRequisition */
    public clsFASCardwithoutGRN_BKUP() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(0));
        props.put("ASSET_NO",new Variant(""));
        props.put("PREFIX",new Variant(""));
        props.put("SUFFIX",new Variant(""));
        props.put("ASSET_DATE",new Variant(""));
        //props.put("ASSET_NO",new Variant(""));
        props.put("ITEM_ID",new Variant(""));
        props.put("ITEM_DESC",new Variant(""));
        props.put("SUPPLIER_CODE",new Variant(""));
        props.put("DEPT_ID",new Variant(0));
        props.put("PO_NO",new Variant(""));
        props.put("PO_DATE",new Variant(""));
        props.put("GURANTEE",new Variant(""));
        props.put("MAIN_ACCOUNT_CODE",new Variant(""));
        props.put("EXPECTED_LIFE",new Variant(""));
        props.put("WARRANTY",new Variant(""));
        props.put("YEAR_OF_PURCHASE",new Variant(""));
        props.put("DISPOSED_DATE",new Variant(""));
        props.put("SIZE_CAPACITY",new Variant(""));
        props.put("INSTALLTION_DATE",new Variant(""));
        props.put("TOTAL_QTY",new Variant(""));
        props.put("MODEL_NO",new Variant(""));
        props.put("MACHINE_NO",new Variant(""));
        props.put("INVOICE_NO",new Variant(""));
        props.put("INVOICE_DATE",new Variant(""));
        props.put("HEADER_REMARKS",new Variant(""));
        props.put("METHOD_ID",new Variant(0));
        props.put("BOOK_PER",new Variant(0));
        props.put("IT_PER",new Variant(0));
        props.put("PJ_VOUCHER_NO",new Variant(""));
        props.put("PJ_VOUCHER_DATE",new Variant(""));
        props.put("PJ_VOUCHER_AMOUNT",new Variant(0));
        props.put("CUSTOM_DUTY_VOUCHER_NO",new Variant(""));
        props.put("CUSTOM_DUTY_VOUCHER_DATE",new Variant(""));
        props.put("CUSTOM_DUTY_VOUCHER_AMOUNT",new Variant(0));
        props.put("LANDING_VOUCHER_NO",new Variant(""));
        props.put("LANDING_VOUCHER_DATE",new Variant(""));
        props.put("LANDING_VOUCHER_AMOUNT",new Variant(0));
        props.put("FREIGHT_VOUCHER_NO",new Variant(""));
        props.put("FREIGHT_VOUCHER_DATE",new Variant(""));
        props.put("FREIGHT_VOUCHER_AMOUNT",new Variant(0));
        props.put("INSTALLATION_VOUCHER_NO",new Variant(""));
        props.put("INSTALLATION_VOUCHER_DATE",new Variant(""));
        props.put("INSTALLATION_VOUCHER_AMOUNT",new Variant(0));
        props.put("OTHERS_VOUCHER_NO",new Variant(""));
        props.put("OTHERS_VOUCHER_DATE",new Variant(""));
        props.put("OTHERS_VOUCHER_AMOUNT",new Variant(0));
        props.put("SERVICE_TAX_AMOUNT",new Variant(0));
        
        
        
        
        props.put("REMARKS",new Variant(""));
        props.put("APPROVED",new Variant(0));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("REJECTED", new Variant(""));
        props.put("REJECTED_DATE",new Variant(""));
        props.put("REJECTED_REMARKS",new Variant(""));
        props.put("MODULE_ID",new Variant(0));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("ENTRY_DATE",new Variant(""));
        props.put("PREFIX",new Variant("")); //For Autonumber generation
        //props.put("CUSTOMER_BANK",new Variant(""));
        
        //Create a new object for MR Item collection
        colFASItemsDetail=new HashMap();
        colFASItemsDetailExIT=new HashMap();
        colFASItemsDetailExBook=new HashMap();
        colOthers = new HashMap();
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        
        //props.put("CHEQUE_AMOUNT",new Variant(0));
        //props.put("PAYMENT_MODE",new Variant(0));
        //props.put("LEGACY_NO",new Variant(""));
        //props.put("LEGACY_DATE",new Variant(""));
        //props.put("LINK_NO",new Variant(""));
        
        //IsInternalPosting=false;
    }
    
    /*<b>Load Data</b>\nThis method loads data from database to Business Object*/
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String SQL = "SELECT * FROM D_FAS_MASTER_HEADER WHERE COMPANY_ID="+pCompanyID+" AND ASSET_TYPE = '2' ORDER BY ASSET_NO";
            rsResultSet=Stmt.executeQuery(SQL);
            //===this comment for new finacial year start==========
            //rsResultSet=Stmt.executeQuery("SELECT * FROM D_FIN_VOUCHER_HEADER WHERE COMPANY_ID="+pCompanyID+" AND VOUCHER_TYPE="+VoucherType+" ORDER BY VOUCHER_NO");
            //==================================================
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
    
    public boolean LoadDataEx(long pCompanyID) {
        Ready=false;
        try {
            
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FAS_MASTER_HEADER LIMIT 1");
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
        
        Statement stHistory,stHDetail,stHeader,stDetail,stDetailEx,stHDetailEx,stOthers,stHOthers;
        ResultSet rsHistory,rsHDetail,rsHeader,rsDetail,rsDetailEx,rsHDetailEx,rsOthers,rsHOthers;
        int RefModuleID = 0;
        int CompanyID=0;
        try {
            
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date DocDate=java.sql.Date.valueOf((String)getAttribute("ASSET_DATE").getObj());
            
            if((DocDate.after(FinFromDate)||DocDate.compareTo(FinFromDate)==0)&&(DocDate.before(FinToDate)||DocDate.compareTo(FinToDate)==0)) {
                //Withing the year
            }
            else {
                //LastError="Document date is not within financial year.";
                //return false;
            }
            //=====================================================//
            
            //ApprovalFlow ObjFlow=new ApprovalFlow();
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetailEx=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHOthers=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FAS_MASTER_HEADER_H WHERE ASSET_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FAS_MASTER_DETAIL_H WHERE ASSET_NO='1'");
            rsHDetail.first();
            rsHDetailEx=stHDetailEx.executeQuery("SELECT * FROM D_FAS_MASTER_DETAIL_EX_H WHERE ASSET_NO='1'");
            rsHDetailEx.first();
            rsHOthers=stHOthers.executeQuery("SELECT * FROM D_FAS_OTHER_VOUCHER_H WHERE ASSET_NO='1'");
            rsHOthers.first();
            //------------------------------------//
            
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            
            
            //--------- Generate New GRN No. ------------
            setAttribute("ASSET_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,clsFASCardwithoutGRN.ModuleID, (int)getAttribute("FFNO").getVal(),true));
            //-------------------------------------------------
            
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getLong());
            rsResultSet.updateString("ASSET_NO",getAttribute("ASSET_NO").getString());
            rsResultSet.updateString("ASSET_DATE", getAttribute("ASSET_DATE").getString());
            rsResultSet.updateString("ASSET_TYPE",String.valueOf(clsFASCardwithoutGRN.AssetType)); //1 with GRN ,2 without GRN
            rsResultSet.updateString("ITEM_ID",getAttribute("ITEM_ID").getString());
            rsResultSet.updateString("ITEM_DESC",getAttribute("ITEM_DESC").getString());
            rsResultSet.updateString("SUPPLIER_CODE",getAttribute("SUPPLIER_CODE").getString());
            rsResultSet.updateInt("DEPT_ID",Integer.parseInt(getAttribute("DEPT_ID").getString()));
            rsResultSet.updateString("GRN_NO","");
            rsResultSet.updateString("PO_NO",getAttribute("PO_NO").getString());
            rsResultSet.updateString("PO_DATE",getAttribute("PO_DATE").getString());
            rsResultSet.updateString("GUARANTEE",getAttribute("GURANTEE").getString());
            rsResultSet.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsResultSet.updateString("EXPECTED_LIFE",getAttribute("EXPECTED_LIFE").getString());
            rsResultSet.updateString("WARRANTY",getAttribute("WARRANTY").getString());
            rsResultSet.updateString("YEAR_OF_PURCHASE",getAttribute("YEAR_OF_PURCHASE").getString());
            rsResultSet.updateString("DISPOSED_DATE",getAttribute("DISPOSED_DATE").getString());
            rsResultSet.updateString("SIZE_CAPACITY",getAttribute("SIZE_CAPACITY").getString());
            rsResultSet.updateString("INSTALLATION_DATE",getAttribute("INSTALLATION_DATE").getString());
            rsResultSet.updateDouble("QTY",Double.parseDouble(getAttribute("TOTAL_QTY").getString()));
            rsResultSet.updateString("MODEL_NO",getAttribute("MODEL_NO").getString());
            rsResultSet.updateString("MACHINE_NO",getAttribute("MACHINE_NO").getString());
            rsResultSet.updateString("INVOICE_NO",getAttribute("INVOICE_NO").getString());
            rsResultSet.updateString("INVOICE_DATE",getAttribute("INVOICE_DATE").getString());
            rsResultSet.updateString("HEADER_REMARKS",getAttribute("HEADER_REMARKS").getString());
            
            rsResultSet.updateLong("METHOD_ID",(long) getAttribute("METHOD_ID").getVal());
            rsResultSet.updateDouble("BOOK_PER",Double.parseDouble(getAttribute("BOOK_PER").getString()));
            rsResultSet.updateDouble("IT_PER",Double.parseDouble(getAttribute("IT_PER").getString()));
            
            rsResultSet.updateString("PJ_VOUCHER_NO",getAttribute("PJ_VOUCHER_NO").getString());
            rsResultSet.updateString("PJ_VOUCHER_DATE",getAttribute("PJ_VOUCHER_DATE").getString());
            rsResultSet.updateDouble("PJ_VOUCHER_AMOUNT",Double.parseDouble(getAttribute("PJ_VOUCHER_AMOUNT").getString()));
            
            rsResultSet.updateString("CUSTOM_DUTY_VOUCHER_NO",getAttribute("CUSTOM_DUTY_VOUCHER_NO").getString());
            rsResultSet.updateString("CUSTOM_DUTY_VOUCHER_DATE",getAttribute("CUSTOM_DUTY_VOUCHER_DATE").getString());
            rsResultSet.updateDouble("CUSTOM_DUTY_VOUCHER_AMOUNT",Double.parseDouble(getAttribute("CUSTOM_DUTY_VOUCHER_AMOUNT").getString()));
            
            rsResultSet.updateString("LANDING_VOUCHER_NO",getAttribute("LANDING_VOUCHER_NO").getString());
            rsResultSet.updateString("LANDING_VOUCHER_DATE",getAttribute("LANDING_VOUCHER_DATE").getString());
            rsResultSet.updateDouble("LANDING_VOUCHER_AMOUNT",Double.parseDouble(getAttribute("LANDING_VOUCHER_AMOUNT").getString()));
            
            rsResultSet.updateString("FREIGHT_OCTROI_VOUCHER_NO",getAttribute("FREIGHT_VOUCHER_NO").getString());
            rsResultSet.updateString("FREIGHT_OCTROI_VOUCHER_DATE",getAttribute("FREIGHT_VOUCHER_DATE").getString());
            rsResultSet.updateDouble("FREIGHT_OCTROI_VOUCHER_AMOUNT",Double.parseDouble(getAttribute("FREIGHT_VOUCHER_AMOUNT").getString()));
            
            rsResultSet.updateString("INSTALLATION_VOUCHER_NO",getAttribute("INSTALLATION_VOUCHER_NO").getString());
            rsResultSet.updateString("INSTALLATION_VOUCHER_DATE",getAttribute("INSTALLATION_VOUCHER_DATE").getString());
            rsResultSet.updateDouble("INSTALLATION_VOUCHER_AMOUNT",Double.parseDouble(getAttribute("INSTALLATION_VOUCHER_AMOUNT").getString()));
            
            rsResultSet.updateString("OTHERS_VOUCHER_NO",getAttribute("OTHERS_VOUCHER_NO").getString());
            rsResultSet.updateString("OTHERS_VOUCHER_DATE",getAttribute("OTHERS_VOUCHER_DATE").getString());
            rsResultSet.updateDouble("OTHERS_VOUCHER_AMOUNT",Double.parseDouble(getAttribute("OTHERS_VOUCHER_AMOUNT").getString()));
            rsResultSet.updateDouble("SERVICE_TAX_AMOUNT",Double.parseDouble(getAttribute("SERVICE_TAX_AMOUNT").getString()));
            
            
            
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateString("CREATED_BY",String.valueOf(getAttribute("CREATED_BY").getInt()));
            rsResultSet.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateString("MODIFIED_BY",String.valueOf(getAttribute("MODIFIED_BY").getInt()));
            rsResultSet.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_REMARKS","");
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.insertRow();
            
            
            
            //========= Inserting Into History =================//
            
            rsHistory.moveToInsertRow();
            rsHistory.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getLong());
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            
            rsHistory.updateString("UPDATED_BY",String.valueOf(getAttribute("CREATED_BY").getInt()));
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            
            
            rsHistory.updateString("ASSET_NO",getAttribute("ASSET_NO").getString());
            rsHistory.updateString("ASSET_DATE", getAttribute("ASSET_DATE").getString());
            rsHistory.updateString("ASSET_TYPE",String.valueOf(clsFASCardwithoutGRN.AssetType)); //1 with GRN ,2 without GRN
            rsHistory.updateString("ITEM_ID",getAttribute("ITEM_ID").getString());
            rsHistory.updateString("ITEM_DESC",getAttribute("ITEM_DESC").getString());
            rsHistory.updateString("SUPPLIER_CODE",getAttribute("SUPPLIER_CODE").getString());
            rsHistory.updateInt("DEPT_ID",Integer.parseInt(getAttribute("DEPT_ID").getString()));
            rsHistory.updateString("GRN_NO","");
            rsHistory.updateString("PO_NO",getAttribute("PO_NO").getString());
            rsHistory.updateString("PO_DATE",getAttribute("PO_DATE").getString());
            rsHistory.updateString("GUARANTEE",getAttribute("GURANTEE").getString());
            rsHistory.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsHistory.updateString("EXPECTED_LIFE",getAttribute("EXPECTED_LIFE").getString());
            rsHistory.updateString("WARRANTY",getAttribute("WARRANTY").getString());
            rsHistory.updateString("YEAR_OF_PURCHASE",getAttribute("YEAR_OF_PURCHASE").getString());
            rsHistory.updateString("DISPOSED_DATE",getAttribute("DISPOSED_DATE").getString());
            rsHistory.updateString("SIZE_CAPACITY",getAttribute("SIZE_CAPACITY").getString());
            rsHistory.updateString("INSTALLATION_DATE",getAttribute("INSTALLATION_DATE").getString());
            rsHistory.updateDouble("QTY",Double.parseDouble(getAttribute("TOTAL_QTY").getString()));
            rsHistory.updateString("MODEL_NO",getAttribute("MODEL_NO").getString());
            rsHistory.updateString("MACHINE_NO",getAttribute("MACHINE_NO").getString());
            rsHistory.updateString("INVOICE_NO",getAttribute("INVOICE_NO").getString());
            rsHistory.updateString("INVOICE_DATE",getAttribute("INVOICE_DATE").getString());
            rsHistory.updateString("HEADER_REMARKS",getAttribute("HEADER_REMARKS").getString());
            
            rsHistory.updateLong("METHOD_ID",(long) getAttribute("METHOD_ID").getVal());
            rsHistory.updateDouble("BOOK_PER",Double.parseDouble(getAttribute("BOOK_PER").getString()));
            rsHistory.updateDouble("IT_PER",Double.parseDouble(getAttribute("IT_PER").getString()));
            
            
            rsHistory.updateString("PJ_VOUCHER_NO",getAttribute("PJ_VOUCHER_NO").getString());
            rsHistory.updateString("PJ_VOUCHER_DATE",getAttribute("PJ_VOUCHER_DATE").getString());
            rsHistory.updateDouble("PJ_VOUCHER_AMOUNT",Double.parseDouble(getAttribute("PJ_VOUCHER_AMOUNT").getString()));
            
            rsHistory.updateString("CUSTOM_DUTY_VOUCHER_NO",getAttribute("CUSTOM_DUTY_VOUCHER_NO").getString());
            rsHistory.updateString("CUSTOM_DUTY_VOUCHER_DATE",getAttribute("CUSTOM_DUTY_VOUCHER_DATE").getString());
            rsHistory.updateDouble("CUSTOM_DUTY_VOUCHER_AMOUNT",Double.parseDouble(getAttribute("CUSTOM_DUTY_VOUCHER_AMOUNT").getString()));
            
            rsHistory.updateString("LANDING_VOUCHER_NO",getAttribute("LANDING_VOUCHER_NO").getString());
            rsHistory.updateString("LANDING_VOUCHER_DATE",getAttribute("LANDING_VOUCHER_DATE").getString());
            rsHistory.updateDouble("LANDING_VOUCHER_AMOUNT",Double.parseDouble(getAttribute("LANDING_VOUCHER_AMOUNT").getString()));
            
            rsHistory.updateString("FREIGHT_OCTROI_VOUCHER_NO",getAttribute("FREIGHT_VOUCHER_NO").getString());
            rsHistory.updateString("FREIGHT_OCTROI_VOUCHER_DATE",getAttribute("FREIGHT_VOUCHER_DATE").getString());
            rsHistory.updateDouble("FREIGHT_OCTROI_VOUCHER_AMOUNT",Double.parseDouble(getAttribute("FREIGHT_VOUCHER_AMOUNT").getString()));
            
            rsHistory.updateString("INSTALLATION_VOUCHER_NO",getAttribute("INSTALLATION_VOUCHER_NO").getString());
            rsHistory.updateString("INSTALLATION_VOUCHER_DATE",getAttribute("INSTALLATION_VOUCHER_DATE").getString());
            rsHistory.updateDouble("INSTALLATION_VOUCHER_AMOUNT",Double.parseDouble(getAttribute("INSTALLATION_VOUCHER_AMOUNT").getString()));
            
            rsHistory.updateString("OTHERS_VOUCHER_NO",getAttribute("OTHERS_VOUCHER_NO").getString());
            rsHistory.updateString("OTHERS_VOUCHER_DATE",getAttribute("OTHERS_VOUCHER_DATE").getString());
            rsHistory.updateDouble("OTHERS_VOUCHER_AMOUNT",Double.parseDouble(getAttribute("OTHERS_VOUCHER_AMOUNT").getString()));
            
            rsHistory.updateDouble("SERVICE_TAX_AMOUNT",Double.parseDouble(getAttribute("SERVICE_TAX_AMOUNT").getString()));
            
            rsHistory.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateString("CREATED_BY",String.valueOf(getAttribute("CREATED_BY").getInt()));
            rsHistory.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsHistory.updateString("MODIFIED_BY",String.valueOf(getAttribute("MODIFIED_BY").getInt()));
            rsHistory.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_REMARKS","");
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            
            //Now Insert records into detail table
            stDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDetail=stDetail.executeQuery("SELECT * FROM D_FAS_MASTER_DETAIL WHERE ASSET_NO='1'");
            rsDetail.first();
            
            
            
            
            
            for(int i=1;i<=colFASItemsDetail.size();i++) {
                clsFASCardwithoutGRNDetail objDetail=(clsFASCardwithoutGRNDetail) colFASItemsDetail.get(Integer.toString(i));
                
                rsDetail.moveToInsertRow();
                rsDetail.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                rsDetail.updateString("ASSET_NO",getAttribute("ASSET_NO").getString());
                rsDetail.updateInt("SR_NO",Integer.parseInt(objDetail.getAttribute("SR_NO").getString()));
                rsDetail.updateString("YEAR",objDetail.getAttribute("YEAR").getString());
                rsDetail.updateInt("DEPT_ID",Integer.parseInt(objDetail.getAttribute("DEPT_ID").getString()));
                rsDetail.updateString("ITEM_ID",objDetail.getAttribute("ITEM_ID").getString());
                rsDetail.updateDouble("AMOUNT",Double.parseDouble(objDetail.getAttribute("AMOUNT").getString()));
                rsDetail.updateString("ASSET_STATUS",objDetail.getAttribute("ASSET_STATUS").getString());
                rsDetail.updateString("SALE_VALUE",objDetail.getAttribute("SALE_VALUE").getString());
                rsDetail.updateString("SALE_DATE",objDetail.getAttribute("SALE_DATE").getString());
                rsDetail.updateString("SALE_DOC_NO",objDetail.getAttribute("SALE_DOC_NO").getString());
                // rsDetail.updateString("SJ_NUMBER",objDetail.getAttribute("SJ_NUMBER").getString());
                rsDetail.updateString("REMARKS",objDetail.getAttribute("REMARKS").getString());
                rsDetail.updateString("CREATED_BY",String.valueOf(getAttribute("CREATED_BY").getInt()));
                rsDetail.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                rsDetail.updateString("MODIFIED_BY",String.valueOf(getAttribute("MODIFIED_BY").getInt()));
                rsDetail.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
                rsDetail.updateBoolean("CHANGED",true);
                rsDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsDetail.updateBoolean("CANCELLED",false);
                rsDetail.insertRow();
                
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                
                
                
                rsHDetail.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
                
                rsHDetail.updateString("UPDATED_BY",String.valueOf(getAttribute("CREATED_BY").getInt()));
                rsHDetail.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
                rsHDetail.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
                rsHDetail.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
                
                
                
                rsHDetail.updateString("ASSET_NO",getAttribute("ASSET_NO").getString());
                rsHDetail.updateInt("SR_NO",Integer.parseInt(objDetail.getAttribute("SR_NO").getString()));
                rsHDetail.updateString("YEAR",objDetail.getAttribute("YEAR").getString());
                rsHDetail.updateInt("DEPT_ID",Integer.parseInt(objDetail.getAttribute("DEPT_ID").getString()));
                rsHDetail.updateString("ITEM_ID",objDetail.getAttribute("ITEM_ID").getString());
                rsHDetail.updateDouble("AMOUNT",Double.parseDouble(objDetail.getAttribute("AMOUNT").getString()));
                rsHDetail.updateString("ASSET_STATUS",objDetail.getAttribute("ASSET_STATUS").getString());
                rsHDetail.updateString("SALE_VALUE",objDetail.getAttribute("SALE_VALUE").getString());
                rsHDetail.updateString("SALE_DATE",objDetail.getAttribute("SALE_DATE").getString());
                rsHDetail.updateString("SALE_DOC_NO",objDetail.getAttribute("SALE_DOC_NO").getString());
                // rsHDetail.updateString("SJ_NUMBER",objDetail.getAttribute("SJ_NUMBER").getString());
                rsHDetail.updateString("REMARKS",objDetail.getAttribute("REMARKS").getString());
                rsHDetail.updateString("CREATED_BY",String.valueOf(getAttribute("CREATED_BY").getInt()));
                rsHDetail.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                rsHDetail.updateString("MODIFIED_BY",String.valueOf(getAttribute("MODIFIED_BY").getInt()));
                rsHDetail.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CANCELLED",false);
                rsHDetail.insertRow();
            }
            
            
            //Now Insert records into detail ex table
            
            stDetailEx=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDetailEx=stDetailEx.executeQuery("SELECT * FROM D_FAS_MASTER_DETAIL_EX WHERE ASSET_NO='1'");
            rsDetailEx.first();
            
            //---INSERT THE BOOK DATA IN THE DETAIL_ex TABLE
            for(int i=1;i<=colFASItemsDetailExBook.size();i++) {
                clsFASCardwithoutGRNDetailEx objDetailEx = (clsFASCardwithoutGRNDetailEx) colFASItemsDetailExBook.get(Integer.toString(i));
                
                rsDetailEx.moveToInsertRow();
                rsDetailEx.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                rsDetailEx.updateString("ASSET_NO",objDetailEx.getAttribute("ASSET_NO").getString());
                rsDetailEx.updateInt("SR_NO",objDetailEx.getAttribute("SR_NO").getInt());
                rsDetailEx.updateInt("DETAIL_SR_NO",objDetailEx.getAttribute("DETAIL_SR_NO").getInt());
                rsDetailEx.updateString("TYPE",objDetailEx.getAttribute("TYPE").getString());//  1 - Book , 2 - IT
                rsDetailEx.updateString("YEAR",objDetailEx.getAttribute("YEAR").getString());
                rsDetailEx.updateDouble("OPENING_BALANCE",Double.parseDouble(objDetailEx.getAttribute("OPENING_BALANCE").getString()));
                rsDetailEx.updateDouble("ADDITIONS",objDetailEx.getAttribute("ADDITIONS").getDouble());
                rsDetailEx.updateDouble("CLOSING_BALANCE",Double.parseDouble(objDetailEx.getAttribute("CLOSING_BALANCE").getString()));
                rsDetailEx.updateString("DEPRECIATION_FROM_DATE",objDetailEx.getAttribute("DEPRECIATION_FROM_DATE").getString());
                rsDetailEx.updateString("DEPRECIATION_TO_DATE",objDetailEx.getAttribute("DEPRECIATION_TO_DATE").getString());
                rsDetailEx.updateDouble("DEPRECIATION_PERCENTAGE",Double.parseDouble(objDetailEx.getAttribute("DEPRECIATION_PERCENTAGE").getString()));
                rsDetailEx.updateInt("DEPRECIATION_METHOD",Integer.parseInt(objDetailEx.getAttribute("DEPRECIATION_METHOD").getString()));
                rsDetailEx.updateDouble("DEPRECIATION_FOR_THE_YEAR",Double.parseDouble(objDetailEx.getAttribute("DEPRECIATION_FOR_THE_YEAR").getString()));
                rsDetailEx.updateString("SHIFT_ALLOW_FOR_THE_YEAR",objDetailEx.getAttribute("SHIFT_ALLOW_FOR_THE_YEAR").getString());
                rsDetailEx.updateDouble("CUMULATIVE_DEPRECIATION",Double.parseDouble(objDetailEx.getAttribute("CUMULATIVE_DEPRECIATION").getString()));
                rsDetailEx.updateDouble("WRITTEN_DOWN_VALUE",Double.parseDouble(objDetailEx.getAttribute("WRITTEN_DOWN_VALUE").getString()));
                rsDetailEx.updateString("REMARKS",objDetailEx.getAttribute("REMARKS").getString());
                rsDetailEx.updateString("SHIFT",objDetailEx.getAttribute("SHIFT").getString());
                rsDetailEx.updateBoolean("CHANGED",true);
                rsDetailEx.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsDetailEx.updateBoolean("CANCELLED",false);
                rsDetailEx.updateString("MODIFIED_BY",String.valueOf(getAttribute("MODIFIED_BY").getInt()));
                rsDetailEx.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
                rsDetailEx.updateString("CREATED_BY",String.valueOf(getAttribute("CREATED_BY").getInt()));
                rsDetailEx.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                rsDetailEx.insertRow();
                
                rsHDetailEx.moveToInsertRow();
                rsHDetailEx.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
                rsHDetailEx.updateString("UPDATED_BY",String.valueOf(getAttribute("CREATED_BY").getInt()));
                rsHDetailEx.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
                rsHDetailEx.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetailEx.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
                
                rsHDetailEx.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                rsHDetailEx.updateString("ASSET_NO",objDetailEx.getAttribute("ASSET_NO").getString());
                rsHDetailEx.updateInt("SR_NO",objDetailEx.getAttribute("SR_NO").getInt());
                rsHDetailEx.updateInt("DETAIL_SR_NO",objDetailEx.getAttribute("DETAIL_SR_NO").getInt());
                rsHDetailEx.updateString("TYPE",objDetailEx.getAttribute("TYPE").getString());//  1 - Book , 2 - IT
                rsHDetailEx.updateString("YEAR",objDetailEx.getAttribute("YEAR").getString());
                rsHDetailEx.updateDouble("OPENING_BALANCE",Double.parseDouble(objDetailEx.getAttribute("OPENING_BALANCE").getString()));
                rsHDetailEx.updateDouble("ADDITIONS",objDetailEx.getAttribute("ADDITIONS").getDouble());
                rsHDetailEx.updateDouble("CLOSING_BALANCE",Double.parseDouble(objDetailEx.getAttribute("CLOSING_BALANCE").getString()));
                rsHDetailEx.updateString("DEPRECIATION_FROM_DATE",objDetailEx.getAttribute("DEPRECIATION_FROM_DATE").getString());
                rsHDetailEx.updateString("DEPRECIATION_TO_DATE",objDetailEx.getAttribute("DEPRECIATION_TO_DATE").getString());
                rsHDetailEx.updateDouble("DEPRECIATION_PERCENTAGE",Double.parseDouble(objDetailEx.getAttribute("DEPRECIATION_PERCENTAGE").getString()));
                rsHDetailEx.updateInt("DEPRECIATION_METHOD",Integer.parseInt(objDetailEx.getAttribute("DEPRECIATION_METHOD").getString()));
                rsHDetailEx.updateDouble("DEPRECIATION_FOR_THE_YEAR",Double.parseDouble(objDetailEx.getAttribute("DEPRECIATION_FOR_THE_YEAR").getString()));
                rsHDetailEx.updateString("SHIFT_ALLOW_FOR_THE_YEAR",objDetailEx.getAttribute("SHIFT_ALLOW_FOR_THE_YEAR").getString());
                rsHDetailEx.updateDouble("CUMULATIVE_DEPRECIATION",Double.parseDouble(objDetailEx.getAttribute("CUMULATIVE_DEPRECIATION").getString()));
                rsHDetailEx.updateDouble("WRITTEN_DOWN_VALUE",Double.parseDouble(objDetailEx.getAttribute("WRITTEN_DOWN_VALUE").getString()));
                //rsHDetailEx.updateString("NET_BLOCK",objDetailEx.getAttribute("NET_BLOCK").getString());
                rsHDetailEx.updateString("REMARKS",objDetailEx.getAttribute("REMARKS").getString());
                rsHDetailEx.updateString("SHIFT",objDetailEx.getAttribute("SHIFT").getString());
                rsHDetailEx.updateBoolean("CHANGED",true);
                rsHDetailEx.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetailEx.updateBoolean("CANCELLED",false);
                rsHDetailEx.updateString("MODIFIED_BY",String.valueOf(getAttribute("MODIFIED_BY").getInt()));
                rsHDetailEx.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
                rsHDetailEx.updateString("CREATED_BY",String.valueOf(getAttribute("CREATED_BY").getInt()));
                rsHDetailEx.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                rsHDetailEx.insertRow();
                
            }
            
            
            //---INSERT THE IT DATA IN THE DETAIL_ex TABLE
            
            for(int i=1;i<=colFASItemsDetailExIT.size();i++) {
                clsFASCardwithoutGRNDetailEx objDetailEx = (clsFASCardwithoutGRNDetailEx) colFASItemsDetailExIT.get(Integer.toString(i));
                
                rsDetailEx.moveToInsertRow();
                rsDetailEx.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                rsDetailEx.updateString("ASSET_NO",objDetailEx.getAttribute("ASSET_NO").getString());
                rsDetailEx.updateInt("SR_NO",objDetailEx.getAttribute("SR_NO").getInt());
                rsDetailEx.updateInt("DETAIL_SR_NO",objDetailEx.getAttribute("DETAIL_SR_NO").getInt());
                rsDetailEx.updateString("TYPE",objDetailEx.getAttribute("TYPE").getString());//  1 - Book , 2 - IT
                rsDetailEx.updateString("YEAR",objDetailEx.getAttribute("YEAR").getString());
                rsDetailEx.updateDouble("OPENING_BALANCE",Double.parseDouble(objDetailEx.getAttribute("OPENING_BALANCE").getString()));
                rsDetailEx.updateDouble("ADDITIONS",objDetailEx.getAttribute("ADDITIONS").getDouble());
                rsDetailEx.updateDouble("CLOSING_BALANCE",Double.parseDouble(objDetailEx.getAttribute("CLOSING_BALANCE").getString()));
                rsDetailEx.updateString("DEPRECIATION_FROM_DATE",objDetailEx.getAttribute("DEPRECIATION_FROM_DATE").getString());
                rsDetailEx.updateString("DEPRECIATION_TO_DATE",objDetailEx.getAttribute("DEPRECIATION_TO_DATE").getString());
                rsDetailEx.updateDouble("DEPRECIATION_PERCENTAGE",Double.parseDouble(objDetailEx.getAttribute("DEPRECIATION_PERCENTAGE").getString()));
                rsDetailEx.updateInt("DEPRECIATION_METHOD",Integer.parseInt(objDetailEx.getAttribute("DEPRECIATION_METHOD").getString()));
                rsDetailEx.updateDouble("DEPRECIATION_FOR_THE_YEAR",Double.parseDouble(objDetailEx.getAttribute("DEPRECIATION_FOR_THE_YEAR").getString()));
                rsDetailEx.updateString("SHIFT_ALLOW_FOR_THE_YEAR",objDetailEx.getAttribute("SHIFT_ALLOW_FOR_THE_YEAR").getString());
                rsDetailEx.updateDouble("CUMULATIVE_DEPRECIATION",Double.parseDouble(objDetailEx.getAttribute("CUMULATIVE_DEPRECIATION").getString()));
                rsDetailEx.updateDouble("WRITTEN_DOWN_VALUE",Double.parseDouble(objDetailEx.getAttribute("WRITTEN_DOWN_VALUE").getString()));
                //rsDetailEx.updateString("NET_BLOCK",objDetailEx.getAttribute("NET_BLOCK").getString());
                rsDetailEx.updateString("REMARKS",objDetailEx.getAttribute("REMARKS").getString());
                rsDetailEx.updateString("SHIFT",objDetailEx.getAttribute("SHIFT").getString());
                rsDetailEx.updateBoolean("CHANGED",true);
                rsDetailEx.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsDetailEx.updateBoolean("CANCELLED",false);
                rsDetailEx.updateString("MODIFIED_BY",String.valueOf(getAttribute("MODIFIED_BY").getInt()));
                rsDetailEx.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
                rsDetailEx.updateString("CREATED_BY",String.valueOf(getAttribute("CREATED_BY").getInt()));
                rsDetailEx.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                rsDetailEx.insertRow();
                
                rsHDetailEx.moveToInsertRow();
                rsHDetailEx.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
                rsHDetailEx.updateString("UPDATED_BY",String.valueOf(getAttribute("CREATED_BY").getInt()));
                rsHDetailEx.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
                rsHDetailEx.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetailEx.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
                
                rsHDetailEx.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                rsHDetailEx.updateString("ASSET_NO",objDetailEx.getAttribute("ASSET_NO").getString());
                rsHDetailEx.updateInt("SR_NO",objDetailEx.getAttribute("SR_NO").getInt());
                rsHDetailEx.updateInt("DETAIL_SR_NO",objDetailEx.getAttribute("DETAIL_SR_NO").getInt());
                rsHDetailEx.updateString("TYPE",objDetailEx.getAttribute("TYPE").getString());//  1 - Book , 2 - IT
                rsHDetailEx.updateString("YEAR",objDetailEx.getAttribute("YEAR").getString());
                rsHDetailEx.updateDouble("OPENING_BALANCE",Double.parseDouble(objDetailEx.getAttribute("OPENING_BALANCE").getString()));
                rsHDetailEx.updateDouble("ADDITIONS",objDetailEx.getAttribute("ADDITIONS").getDouble());
                rsHDetailEx.updateDouble("CLOSING_BALANCE",Double.parseDouble(objDetailEx.getAttribute("CLOSING_BALANCE").getString()));
                rsHDetailEx.updateString("DEPRECIATION_FROM_DATE",objDetailEx.getAttribute("DEPRECIATION_FROM_DATE").getString());
                rsHDetailEx.updateString("DEPRECIATION_TO_DATE",objDetailEx.getAttribute("DEPRECIATION_TO_DATE").getString());
                rsHDetailEx.updateDouble("DEPRECIATION_PERCENTAGE",Double.parseDouble(objDetailEx.getAttribute("DEPRECIATION_PERCENTAGE").getString()));
                rsHDetailEx.updateInt("DEPRECIATION_METHOD",Integer.parseInt(objDetailEx.getAttribute("DEPRECIATION_METHOD").getString()));
                rsHDetailEx.updateDouble("DEPRECIATION_FOR_THE_YEAR",Double.parseDouble(objDetailEx.getAttribute("DEPRECIATION_FOR_THE_YEAR").getString()));
                rsHDetailEx.updateString("SHIFT_ALLOW_FOR_THE_YEAR",objDetailEx.getAttribute("SHIFT_ALLOW_FOR_THE_YEAR").getString());
                rsHDetailEx.updateDouble("CUMULATIVE_DEPRECIATION",Double.parseDouble(objDetailEx.getAttribute("CUMULATIVE_DEPRECIATION").getString()));
                rsHDetailEx.updateDouble("WRITTEN_DOWN_VALUE",Double.parseDouble(objDetailEx.getAttribute("WRITTEN_DOWN_VALUE").getString()));
                //rsHDetailEx.updateString("NET_BLOCK",objDetailEx.getAttribute("NET_BLOCK").getString());
                rsHDetailEx.updateString("REMARKS",objDetailEx.getAttribute("REMARKS").getString());
                rsHDetailEx.updateString("SHIFT",objDetailEx.getAttribute("SHIFT").getString());
                rsHDetailEx.updateBoolean("CHANGED",true);
                rsHDetailEx.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetailEx.updateBoolean("CANCELLED",false);
                rsHDetailEx.updateString("MODIFIED_BY",String.valueOf(getAttribute("MODIFIED_BY").getInt()));
                rsHDetailEx.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
                rsHDetailEx.updateString("CREATED_BY",String.valueOf(getAttribute("CREATED_BY").getInt()));
                rsHDetailEx.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                rsHDetailEx.insertRow();
                
            }
            
            
            
            // Inserting records in OTHERS VOUCHER TABLE
            stOthers=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsOthers=stOthers.executeQuery("SELECT * FROM D_FAS_OTHER_VOUCHER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ASSET_NO='1'");
            rsOthers.first();
            
            for(int i=1;i<=colOthers.size();i++) {
                clsFASOthersVoucher ObjOthers=(clsFASOthersVoucher) colOthers.get(Integer.toString(i));
                
                rsOthers.moveToInsertRow();
                rsOthers.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                rsOthers.updateString("ASSET_NO",getAttribute("ASSET_NO").getString());
                rsOthers.updateString("OTHERS_VOUCHER_NO",(String) ObjOthers.getAttribute("OTHERS_VOUCHER_NO").getObj());
                rsOthers.updateString("OTHERS_VOUCHER_DATE",(String) ObjOthers.getAttribute("OTHERS_VOUCHER_DATE").getObj());
                rsOthers.updateDouble("OTHERS_VOUCHER_AMOUNT",Double.parseDouble(ObjOthers.getAttribute("OTHERS_VOUCHER_AMOUNT").getString()));
                rsOthers.insertRow();
                
                rsHOthers.moveToInsertRow();
                rsHOthers.updateInt("REVISION_NO",1);
                rsHOthers.updateString("UPDATED_BY",String.valueOf(getAttribute("CREATED_BY").getInt()));
                rsHOthers.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
                rsHOthers.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHOthers.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
                rsHOthers.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                rsHOthers.updateString("ASSET_NO",getAttribute("ASSET_NO").getString());
                rsHOthers.updateString("OTHERS_VOUCHER_NO",(String) ObjOthers.getAttribute("OTHERS_VOUCHER_NO").getObj());
                rsHOthers.updateString("OTHERS_VOUCHER_DATE",(String) ObjOthers.getAttribute("OTHERS_VOUCHER_DATE").getObj());
                rsHOthers.updateDouble("OTHERS_VOUCHER_AMOUNT",Double.parseDouble(ObjOthers.getAttribute("OTHERS_VOUCHER_AMOUNT").getString()));
                rsHOthers.insertRow();
                
            }
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=getAttribute("COMPANY_ID").getInt();
            ObjFlow.ModuleID=clsFASCardwithoutGRN.ModuleID;
            ObjFlow.DocNo=getAttribute("ASSET_NO").getString();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName=clsModules.getHeaderTableName(EITLERPGLOBAL.gCompanyID,clsFASCardwithoutGRN.ModuleID);
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName="ASSET_NO";
            ObjFlow.UseSpecifiedURL=true;
            ObjFlow.SpecificURL=FinanceGlobal.FinURL;
            
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
            ObjFlow.UseSpecifiedURL=false; //Turn it off due to static nature
            //--------- Approval Flow Update complete -----------
            
            
            //*************************************************************************//
            /*
            int VoucherType = data.getIntValueFromDB("SELECT VOUCHER_TYPE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+ObjFlow.DocNo+"' ",FinanceGlobal.FinURL);
            if(ObjFlow.Status.equals("F")) {
                String VoucherNo = ObjFlow.DocNo;
                VoucherType = data.getIntValueFromDB("SELECT VOUCHER_TYPE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+VoucherNo+"' ",FinanceGlobal.FinURL);
                if(VoucherType == FinanceGlobal.TYPE_RECEIPT || VoucherType == FinanceGlobal.TYPE_JOURNAL) {
                    String MainAccountCode = data.getStringValueFromDB("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo+"' AND EFFECT='C'",FinanceGlobal.FinURL);
                    if(MainAccountCode.equals("132642") || MainAccountCode.equals("132635") || MainAccountCode.equals("132714")) { //MainAccountCode.equals("132666") ||
                        if(!PostReceipt(VoucherNo,MainAccountCode)) {
                            LastError += "Dealer Deposit Receipt Not Posted Properly...";
                        }
                    }
                }
            }
             */
           /* if(VoucherType == FinanceGlobal.TYPE_CASH_RECEIPT_VOUCHER) {
                String currentVoucherNo = ObjFlow.DocNo.trim();
                generateNextLegacyNo(currentVoucherNo);
            }
            
            if(!TVoucherNo.equals("") && !EmployeeNo.equals("")) {
                clsTravelExp.Insert(TVoucherNo,EmployeeNo);
            }
            */
            //*************************************************************************//
           /*
            if((VoucherType == FinanceGlobal.TYPE_PAYMENT || VoucherType == FinanceGlobal.TYPE_PAYMENT_2) && RefModuleID!=86 && ObjFlow.Status.equals("F")) {
                String currentVoucherNo = ObjFlow.DocNo.trim();
                data.Execute("UPDATE D_FIN_VOUCHER_HEADER SET VOUCHER_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"' WHERE VOUCHER_NO='"+currentVoucherNo+"' ",FinanceGlobal.FinURL);
                data.Execute("UPDATE D_FIN_VOUCHER_HEADER_H SET VOUCHER_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"' WHERE VOUCHER_NO='"+currentVoucherNo+"' ",FinanceGlobal.FinURL);
                generateNextLegacyNo(currentVoucherNo);
            
            }
            
            // ADDED BY MRUGESH AGAINST THE PROBLEM OF AUTO LEGACY NUMBERING
            if(VoucherType == FinanceGlobal.TYPE_PJV && (!ObjFlow.Status.equals("F"))) {
                String currentVoucherNo = ObjFlow.DocNo.trim();
                data.Execute("UPDATE D_FIN_VOUCHER_HEADER SET LEGACY_NO='',LEGACY_DATE='' WHERE VOUCHER_NO='"+currentVoucherNo+"' ",FinanceGlobal.FinURL);
                data.Execute("UPDATE D_FIN_VOUCHER_HEADER_H SET LEGACY_NO='',LEGACY_DATE='' WHERE VOUCHER_NO='"+currentVoucherNo+"' ",FinanceGlobal.FinURL);
                PostAssetCard(getAttribute("VOUCHER_NO").getString());
            }
            */
            
            if(ObjFlow.Status.equals("F")) {
                
                PostFASItem(getAttribute("ITEM_ID").getString(),getAttribute("ASSET_DATE").getString(),(int) getAttribute("METHOD_ID").getVal(),Double.parseDouble(getAttribute("BOOK_PER").getString()),Double.parseDouble(getAttribute("IT_PER").getString()));
                
            }
            
            
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
        Statement stHistory,stHDetail,stHeader,stDetail,stDetailEx,stHDetailEx,stOthers,stHOthers;
        ResultSet rsHistory,rsHDetail,rsHeader,rsDetail,rsDetailEx,rsHDetailEx,rsOthers,rsHOthers;
        int RefModuleID = 0;
        int CompanyID=0;
        boolean Validate=true;
        int OldHierarchy=0;
        String AssetNo="";
        
        try {
            
            
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            if(AStatus.equals("R")) {
                Validate=false;
            }
            
            
            //ApprovalFlow ObjFlow=new ApprovalFlow();
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetailEx=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHOthers=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FAS_MASTER_HEADER_H WHERE ASSET_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FAS_MASTER_DETAIL_H WHERE ASSET_NO='1'");
            rsHDetail.first();
            rsHDetailEx=stHDetailEx.executeQuery("SELECT * FROM D_FAS_MASTER_DETAIL_EX_H WHERE ASSET_NO='1'");
            rsHDetailEx.first();
            rsHOthers=stHOthers.executeQuery("SELECT * FROM D_FAS_OTHER_VOUCHER_H WHERE ASSET_NO='1'");
            rsHOthers.first();
            //------------------------------------//
            
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //rsResultSet.first();
            //rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getLong());
            rsResultSet.updateString("ASSET_NO",getAttribute("ASSET_NO").getString());
            rsResultSet.updateString("ASSET_DATE", getAttribute("ASSET_DATE").getString());
            rsResultSet.updateString("ASSET_TYPE",String.valueOf(clsFASCardwithoutGRN.AssetType)); //1 with GRN ,2 without GRN
            rsResultSet.updateString("ITEM_ID",getAttribute("ITEM_ID").getString());
            rsResultSet.updateString("ITEM_DESC",getAttribute("ITEM_DESC").getString());
            rsResultSet.updateString("SUPPLIER_CODE",getAttribute("SUPPLIER_CODE").getString());
            rsResultSet.updateInt("DEPT_ID",Integer.parseInt(getAttribute("DEPT_ID").getString()));
            rsResultSet.updateString("GRN_NO","");
            rsResultSet.updateString("PO_NO",getAttribute("PO_NO").getString());
            rsResultSet.updateString("PO_DATE",getAttribute("PO_DATE").getString());
            rsResultSet.updateString("GUARANTEE",getAttribute("GURANTEE").getString());
            rsResultSet.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsResultSet.updateString("EXPECTED_LIFE",getAttribute("EXPECTED_LIFE").getString());
            rsResultSet.updateString("WARRANTY",getAttribute("WARRANTY").getString());
            rsResultSet.updateString("YEAR_OF_PURCHASE",getAttribute("YEAR_OF_PURCHASE").getString());
            rsResultSet.updateString("DISPOSED_DATE",getAttribute("DISPOSED_DATE").getString());
            rsResultSet.updateString("SIZE_CAPACITY",getAttribute("SIZE_CAPACITY").getString());
            rsResultSet.updateString("INSTALLATION_DATE",getAttribute("INSTALLATION_DATE").getString());
            rsResultSet.updateDouble("QTY",Double.parseDouble(getAttribute("TOTAL_QTY").getString()));
            rsResultSet.updateString("MODEL_NO",getAttribute("MODEL_NO").getString());
            rsResultSet.updateString("MACHINE_NO",getAttribute("MACHINE_NO").getString());
            rsResultSet.updateString("INVOICE_NO",getAttribute("INVOICE_NO").getString());
            rsResultSet.updateString("INVOICE_DATE",getAttribute("INVOICE_DATE").getString());
            rsResultSet.updateString("HEADER_REMARKS",getAttribute("HEADER_REMARKS").getString());
            
            rsResultSet.updateLong("METHOD_ID",(long) getAttribute("METHOD_ID").getVal());
            rsResultSet.updateDouble("BOOK_PER",Double.parseDouble(getAttribute("BOOK_PER").getString()));
            rsResultSet.updateDouble("IT_PER",Double.parseDouble(getAttribute("IT_PER").getString()));
            
            
            rsResultSet.updateString("PJ_VOUCHER_NO",getAttribute("PJ_VOUCHER_NO").getString());
            rsResultSet.updateString("PJ_VOUCHER_DATE",getAttribute("PJ_VOUCHER_DATE").getString());
            rsResultSet.updateDouble("PJ_VOUCHER_AMOUNT",Double.parseDouble(getAttribute("PJ_VOUCHER_AMOUNT").getString()));
            
            rsResultSet.updateString("CUSTOM_DUTY_VOUCHER_NO",getAttribute("CUSTOM_DUTY_VOUCHER_NO").getString());
            rsResultSet.updateString("CUSTOM_DUTY_VOUCHER_DATE",getAttribute("CUSTOM_DUTY_VOUCHER_DATE").getString());
            rsResultSet.updateDouble("CUSTOM_DUTY_VOUCHER_AMOUNT",Double.parseDouble(getAttribute("CUSTOM_DUTY_VOUCHER_AMOUNT").getString()));
            
            rsResultSet.updateString("LANDING_VOUCHER_NO",getAttribute("LANDING_VOUCHER_NO").getString());
            rsResultSet.updateString("LANDING_VOUCHER_DATE",getAttribute("LANDING_VOUCHER_DATE").getString());
            rsResultSet.updateDouble("LANDING_VOUCHER_AMOUNT",Double.parseDouble(getAttribute("LANDING_VOUCHER_AMOUNT").getString()));
            
            rsResultSet.updateString("FREIGHT_OCTROI_VOUCHER_NO",getAttribute("FREIGHT_VOUCHER_NO").getString());
            rsResultSet.updateString("FREIGHT_OCTROI_VOUCHER_DATE",getAttribute("FREIGHT_VOUCHER_DATE").getString());
            rsResultSet.updateDouble("FREIGHT_OCTROI_VOUCHER_AMOUNT",Double.parseDouble(getAttribute("FREIGHT_VOUCHER_AMOUNT").getString()));
            
            rsResultSet.updateString("INSTALLATION_VOUCHER_NO",getAttribute("INSTALLATION_VOUCHER_NO").getString());
            rsResultSet.updateString("INSTALLATION_VOUCHER_DATE",getAttribute("INSTALLATION_VOUCHER_DATE").getString());
            rsResultSet.updateDouble("INSTALLATION_VOUCHER_AMOUNT",Double.parseDouble(getAttribute("INSTALLATION_VOUCHER_AMOUNT").getString()));
            
            rsResultSet.updateString("OTHERS_VOUCHER_NO",getAttribute("OTHERS_VOUCHER_NO").getString());
            rsResultSet.updateString("OTHERS_VOUCHER_DATE",getAttribute("OTHERS_VOUCHER_DATE").getString());
            rsResultSet.updateDouble("OTHERS_VOUCHER_AMOUNT",Double.parseDouble(getAttribute("OTHERS_VOUCHER_AMOUNT").getString()));
            
            rsResultSet.updateDouble("SERVICE_TAX_AMOUNT",Double.parseDouble(getAttribute("SERVICE_TAX_AMOUNT").getString()));
            
            //System.out.println("teUser " + getAttribute("MOFIFIED_BY").getString() + " User " + getAttribute("MOFIFIED_BY").getInt());
            //System.out.println("teUser " + getAttribute("MOFIFIED_BY").getObj().toString());
            ///System.out.println("teUser " + String.valueOf(getAttribute("MOFIFIED_BY").getVal()));
            //System.out.println("teUser " + getAttribute("MOFIFIED_BY").getLong());
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateString("CREATED_BY",(String) getAttribute("CREATED_BY").getString());
            rsResultSet.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateString("MODIFIED_BY",String.valueOf(getAttribute("MODIFIED_BY").getInt()));
            rsResultSet.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_REMARKS","");
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateRow();
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM D_FAS_MASTER_DETAIL_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ASSET_NO='"+(String)getAttribute("ASSET_NO").getObj()+"' ",FinanceGlobal.FinURL);
            RevNo++;
            String RevDocNo=(String)getAttribute("GRN_NO").getObj();
            
            
            
            //========= Inserting Into History =================//
            
            rsHistory.moveToInsertRow();
            rsHistory.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getLong());
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            
            rsHistory.updateString("UPDATED_BY",String.valueOf(getAttribute("MODIFIED_BY").getInt()) );
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            
            
            rsHistory.updateString("ASSET_NO",getAttribute("ASSET_NO").getString());
            rsHistory.updateString("ASSET_DATE", getAttribute("ASSET_DATE").getString());
            rsHistory.updateString("ASSET_TYPE",String.valueOf(clsFASCardwithoutGRN.AssetType)); //1 with GRN ,2 without GRN
            rsHistory.updateString("ITEM_ID",getAttribute("ITEM_ID").getString());
            rsHistory.updateString("ITEM_DESC",getAttribute("ITEM_DESC").getString());
            rsHistory.updateString("SUPPLIER_CODE",getAttribute("SUPPLIER_CODE").getString());
            rsHistory.updateInt("DEPT_ID",Integer.parseInt(getAttribute("DEPT_ID").getString()));
            rsHistory.updateString("GRN_NO","");
            rsHistory.updateString("PO_NO",getAttribute("PO_NO").getString());
            rsHistory.updateString("PO_DATE",getAttribute("PO_DATE").getString());
            rsHistory.updateString("GUARANTEE",getAttribute("GURANTEE").getString());
            rsHistory.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsHistory.updateString("EXPECTED_LIFE",getAttribute("EXPECTED_LIFE").getString());
            rsHistory.updateString("WARRANTY",getAttribute("WARRANTY").getString());
            rsHistory.updateString("YEAR_OF_PURCHASE",getAttribute("YEAR_OF_PURCHASE").getString());
            rsHistory.updateString("DISPOSED_DATE",getAttribute("DISPOSED_DATE").getString());
            rsHistory.updateString("SIZE_CAPACITY",getAttribute("SIZE_CAPACITY").getString());
            rsHistory.updateString("INSTALLATION_DATE",getAttribute("INSTALLATION_DATE").getString());
            rsHistory.updateDouble("QTY",Double.parseDouble(getAttribute("TOTAL_QTY").getString()));
            rsHistory.updateString("MODEL_NO",getAttribute("MODEL_NO").getString());
            rsHistory.updateString("MACHINE_NO",getAttribute("MACHINE_NO").getString());
            rsHistory.updateString("INVOICE_NO",getAttribute("INVOICE_NO").getString());
            rsHistory.updateString("INVOICE_DATE",getAttribute("INVOICE_DATE").getString());
            rsHistory.updateString("HEADER_REMARKS",getAttribute("HEADER_REMARKS").getString());
            
            rsHistory.updateLong("METHOD_ID",(long) getAttribute("METHOD_ID").getVal());
            rsHistory.updateDouble("BOOK_PER",Double.parseDouble(getAttribute("BOOK_PER").getString()));
            rsHistory.updateDouble("IT_PER",Double.parseDouble(getAttribute("IT_PER").getString()));
            
            rsHistory.updateString("PJ_VOUCHER_NO",getAttribute("PJ_VOUCHER_NO").getString());
            rsHistory.updateString("PJ_VOUCHER_DATE",getAttribute("PJ_VOUCHER_DATE").getString());
            rsHistory.updateDouble("PJ_VOUCHER_AMOUNT",Double.parseDouble(getAttribute("PJ_VOUCHER_AMOUNT").getString()));
            
            rsHistory.updateString("CUSTOM_DUTY_VOUCHER_NO",getAttribute("CUSTOM_DUTY_VOUCHER_NO").getString());
            rsHistory.updateString("CUSTOM_DUTY_VOUCHER_DATE",getAttribute("CUSTOM_DUTY_VOUCHER_DATE").getString());
            rsHistory.updateDouble("CUSTOM_DUTY_VOUCHER_AMOUNT",Double.parseDouble(getAttribute("CUSTOM_DUTY_VOUCHER_AMOUNT").getString()));
            
            rsHistory.updateString("LANDING_VOUCHER_NO",getAttribute("LANDING_VOUCHER_NO").getString());
            rsHistory.updateString("LANDING_VOUCHER_DATE",getAttribute("LANDING_VOUCHER_DATE").getString());
            rsHistory.updateDouble("LANDING_VOUCHER_AMOUNT",Double.parseDouble(getAttribute("LANDING_VOUCHER_AMOUNT").getString()));
            
            rsHistory.updateString("FREIGHT_OCTROI_VOUCHER_NO",getAttribute("FREIGHT_VOUCHER_NO").getString());
            rsHistory.updateString("FREIGHT_OCTROI_VOUCHER_DATE",getAttribute("FREIGHT_VOUCHER_DATE").getString());
            rsHistory.updateDouble("FREIGHT_OCTROI_VOUCHER_AMOUNT",Double.parseDouble(getAttribute("FREIGHT_VOUCHER_AMOUNT").getString()));
            
            rsHistory.updateString("INSTALLATION_VOUCHER_NO",getAttribute("INSTALLATION_VOUCHER_NO").getString());
            rsHistory.updateString("INSTALLATION_VOUCHER_DATE",getAttribute("INSTALLATION_VOUCHER_DATE").getString());
            rsHistory.updateDouble("INSTALLATION_VOUCHER_AMOUNT",Double.parseDouble(getAttribute("INSTALLATION_VOUCHER_AMOUNT").getString()));
            
            rsHistory.updateString("OTHERS_VOUCHER_NO",getAttribute("OTHERS_VOUCHER_NO").getString());
            rsHistory.updateString("OTHERS_VOUCHER_DATE",getAttribute("OTHERS_VOUCHER_DATE").getString());
            rsHistory.updateDouble("OTHERS_VOUCHER_AMOUNT",Double.parseDouble(getAttribute("OTHERS_VOUCHER_AMOUNT").getString()));
            rsHistory.updateDouble("SERVICE_TAX_AMOUNT",Double.parseDouble(getAttribute("SERVICE_TAX_AMOUNT").getString()));
            
            rsHistory.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
            rsHistory.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsHistory.updateString("MODIFIED_BY",String.valueOf(getAttribute("MODIFIED_BY").getInt()));
            rsHistory.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_REMARKS","");
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            
            //System.out.println("test1");
            //==== Delete Previous Entries OF DETAIL TBALE AND EX TABLE====//
            AssetNo=(String)getAttribute("ASSET_NO").getObj();
            String sql =  "DELETE FROM D_FAS_MASTER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND ASSET_NO='"+AssetNo+"' ";
            //System.out.println("DELETE FROM D_FAS_MASTER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND ASSET_NO='"+AssetNo+"' ");
            data.Execute(sql,FinanceGlobal.FinURL);
            sql = "DELETE FROM D_FAS_MASTER_DETAIL_EX WHERE COMPANY_ID="+CompanyID+" AND ASSET_NO='"+AssetNo+"' ";
            //System.out.println(sql);
            data.Execute(sql,FinanceGlobal.FinURL);
            
            
            
            //System.out.println("test2");
            //Now Insert records into detail table
            stDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDetail=stDetail.executeQuery("SELECT * FROM D_FAS_MASTER_DETAIL WHERE ASSET_NO='1'");
            rsDetail.first();
            //System.out.println("test3");
            for(int i=1;i<=colFASItemsDetail.size();i++) {
                clsFASCardwithoutGRNDetail objDetail=(clsFASCardwithoutGRNDetail) colFASItemsDetail.get(Integer.toString(i));
                
                rsDetail.moveToInsertRow();
                rsDetail.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                rsDetail.updateString("ASSET_NO",getAttribute("ASSET_NO").getString());
                rsDetail.updateInt("SR_NO",Integer.parseInt(objDetail.getAttribute("SR_NO").getString()));
                rsDetail.updateString("YEAR",objDetail.getAttribute("YEAR").getString());
                rsDetail.updateInt("DEPT_ID",Integer.parseInt(objDetail.getAttribute("DEPT_ID").getString()));
                rsDetail.updateString("ITEM_ID",objDetail.getAttribute("ITEM_ID").getString());
                rsDetail.updateDouble("AMOUNT",Double.parseDouble(objDetail.getAttribute("AMOUNT").getString()));
                rsDetail.updateString("ASSET_STATUS",objDetail.getAttribute("ASSET_STATUS").getString());
                rsDetail.updateString("SALE_VALUE",objDetail.getAttribute("SALE_VALUE").getString());
                rsDetail.updateString("SALE_DATE",objDetail.getAttribute("SALE_DATE").getString());
                rsDetail.updateString("SALE_DOC_NO",objDetail.getAttribute("SALE_DOC_NO").getString());
                //rsDetail.updateString("SJ_NUMBER",objDetail.getAttribute("SJ_NUMBER").getString());
                rsDetail.updateString("REMARKS",objDetail.getAttribute("REMARKS").getString());
                rsDetail.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
                rsDetail.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                rsDetail.updateString("MODIFIED_BY",String.valueOf(getAttribute("MODIFIED_BY").getInt()));
                rsDetail.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
                rsDetail.updateBoolean("CHANGED",true);
                rsDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsDetail.updateBoolean("CANCELLED",false);
                rsDetail.insertRow();
                
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                
                
                
                rsHDetail.updateInt("REVISION_NO",RevNo);
                
                rsHDetail.updateString("UPDATED_BY",String.valueOf(getAttribute("MODIFIED_BY").getInt()));
                rsHDetail.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
                rsHDetail.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
                rsHDetail.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
                
                
                
                rsHDetail.updateString("ASSET_NO",getAttribute("ASSET_NO").getString());
                rsHDetail.updateInt("SR_NO",Integer.parseInt(objDetail.getAttribute("SR_NO").getString()));
                rsHDetail.updateString("YEAR",objDetail.getAttribute("YEAR").getString());
                rsHDetail.updateInt("DEPT_ID",Integer.parseInt(objDetail.getAttribute("DEPT_ID").getString()));
                rsHDetail.updateString("ITEM_ID",objDetail.getAttribute("ITEM_ID").getString());
                rsHDetail.updateDouble("AMOUNT",Double.parseDouble(objDetail.getAttribute("AMOUNT").getString()));
                rsHDetail.updateString("ASSET_STATUS",objDetail.getAttribute("ASSET_STATUS").getString());
                rsHDetail.updateString("SALE_VALUE",objDetail.getAttribute("SALE_VALUE").getString());
                rsHDetail.updateString("SALE_DATE",objDetail.getAttribute("SALE_DATE").getString());
                rsHDetail.updateString("SALE_DOC_NO",objDetail.getAttribute("SALE_DOC_NO").getString());
                //rsHDetail.updateString("SJ_NUMBER",objDetail.getAttribute("SJ_NUMBER").getString());
                rsHDetail.updateString("REMARKS",objDetail.getAttribute("REMARKS").getString());
                rsHDetail.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
                rsHDetail.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                rsHDetail.updateString("MODIFIED_BY",String.valueOf(getAttribute("MODIFIED_BY").getInt()));
                rsHDetail.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CANCELLED",false);
                rsHDetail.insertRow();
            }
            
            
            
            
            //Now Insert records into detail ex table
            
            stDetailEx=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDetailEx=stDetail.executeQuery("SELECT * FROM D_FAS_MASTER_DETAIL_EX WHERE ASSET_NO='1'");
            rsDetailEx.first();
            
            //---INSERT THE BOOK DATA IN THE DETAIL_ex TABLE
            for(int i=1;i<=colFASItemsDetailExBook.size();i++) {
                clsFASCardwithoutGRNDetailEx objDetailEx = (clsFASCardwithoutGRNDetailEx) colFASItemsDetailExBook.get(Integer.toString(i));
                
                rsDetailEx.moveToInsertRow();
                rsDetailEx.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                rsDetailEx.updateString("ASSET_NO",objDetailEx.getAttribute("ASSET_NO").getString());
                rsDetailEx.updateInt("SR_NO",objDetailEx.getAttribute("SR_NO").getInt());
                rsDetailEx.updateInt("DETAIL_SR_NO",objDetailEx.getAttribute("DETAIL_SR_NO").getInt());
                rsDetailEx.updateString("TYPE",objDetailEx.getAttribute("TYPE").getString());//  1 - Book , 2 - IT
                rsDetailEx.updateString("YEAR",objDetailEx.getAttribute("YEAR").getString());
                rsDetailEx.updateDouble("OPENING_BALANCE",Double.parseDouble(objDetailEx.getAttribute("OPENING_BALANCE").getString()));
                rsDetailEx.updateDouble("ADDITIONS",objDetailEx.getAttribute("ADDITIONS").getDouble());
                rsDetailEx.updateDouble("CLOSING_BALANCE",Double.parseDouble(objDetailEx.getAttribute("CLOSING_BALANCE").getString()));
                rsDetailEx.updateString("DEPRECIATION_FROM_DATE",objDetailEx.getAttribute("DEPRECIATION_FROM_DATE").getString());
                rsDetailEx.updateString("DEPRECIATION_TO_DATE",objDetailEx.getAttribute("DEPRECIATION_TO_DATE").getString());
                rsDetailEx.updateDouble("DEPRECIATION_PERCENTAGE",Double.parseDouble(objDetailEx.getAttribute("DEPRECIATION_PERCENTAGE").getString()));
                rsDetailEx.updateInt("DEPRECIATION_METHOD",Integer.parseInt(objDetailEx.getAttribute("DEPRECIATION_METHOD").getString()));
                rsDetailEx.updateDouble("DEPRECIATION_FOR_THE_YEAR",Double.parseDouble(objDetailEx.getAttribute("DEPRECIATION_FOR_THE_YEAR").getString()));
                rsDetailEx.updateString("SHIFT_ALLOW_FOR_THE_YEAR",objDetailEx.getAttribute("SHIFT_ALLOW_FOR_THE_YEAR").getString());
                rsDetailEx.updateDouble("CUMULATIVE_DEPRECIATION",Double.parseDouble(objDetailEx.getAttribute("CUMULATIVE_DEPRECIATION").getString()));
                rsDetailEx.updateDouble("WRITTEN_DOWN_VALUE",Double.parseDouble(objDetailEx.getAttribute("WRITTEN_DOWN_VALUE").getString()));
                // rsDetailEx.updateString("NET_BLOCK",objDetailEx.getAttribute("NET_BLOCK").getString());
                rsDetailEx.updateString("REMARKS",objDetailEx.getAttribute("REMARKS").getString());
                rsDetailEx.updateString("SHIFT",objDetailEx.getAttribute("SHIFT").getString());
                rsDetailEx.updateBoolean("CHANGED",true);
                rsDetailEx.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsDetailEx.updateBoolean("CANCELLED",false);
                rsDetailEx.updateString("MODIFIED_BY",String.valueOf(getAttribute("MODIFIED_BY").getInt()));
                rsDetailEx.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
                rsDetailEx.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
                rsDetailEx.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                rsDetailEx.insertRow();
                
                rsHDetailEx.moveToInsertRow();
                rsHDetailEx.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
                rsHDetailEx.updateString("UPDATED_BY",String.valueOf(getAttribute("MODIFIED_BY").getInt()));
                rsHDetailEx.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
                rsHDetailEx.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetailEx.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
                
                rsHDetailEx.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                rsHDetailEx.updateString("ASSET_NO",objDetailEx.getAttribute("ASSET_NO").getString());
                rsHDetailEx.updateInt("SR_NO",objDetailEx.getAttribute("SR_NO").getInt());
                rsHDetailEx.updateInt("DETAIL_SR_NO",objDetailEx.getAttribute("DETAIL_SR_NO").getInt());
                rsHDetailEx.updateString("TYPE",objDetailEx.getAttribute("TYPE").getString());//  1 - Book , 2 - IT
                rsHDetailEx.updateString("YEAR",objDetailEx.getAttribute("YEAR").getString());
                rsHDetailEx.updateDouble("OPENING_BALANCE",Double.parseDouble(objDetailEx.getAttribute("OPENING_BALANCE").getString()));
                rsHDetailEx.updateDouble("ADDITIONS",objDetailEx.getAttribute("ADDITIONS").getDouble());
                rsHDetailEx.updateDouble("CLOSING_BALANCE",Double.parseDouble(objDetailEx.getAttribute("CLOSING_BALANCE").getString()));
                rsHDetailEx.updateString("DEPRECIATION_FROM_DATE",objDetailEx.getAttribute("DEPRECIATION_FROM_DATE").getString());
                rsHDetailEx.updateString("DEPRECIATION_TO_DATE",objDetailEx.getAttribute("DEPRECIATION_TO_DATE").getString());
                rsHDetailEx.updateDouble("DEPRECIATION_PERCENTAGE",Double.parseDouble(objDetailEx.getAttribute("DEPRECIATION_PERCENTAGE").getString()));
                rsHDetailEx.updateInt("DEPRECIATION_METHOD",Integer.parseInt(objDetailEx.getAttribute("DEPRECIATION_METHOD").getString()));
                rsHDetailEx.updateDouble("DEPRECIATION_FOR_THE_YEAR",Double.parseDouble(objDetailEx.getAttribute("DEPRECIATION_FOR_THE_YEAR").getString()));
                rsHDetailEx.updateString("SHIFT_ALLOW_FOR_THE_YEAR",objDetailEx.getAttribute("SHIFT_ALLOW_FOR_THE_YEAR").getString());
                rsHDetailEx.updateDouble("CUMULATIVE_DEPRECIATION",Double.parseDouble(objDetailEx.getAttribute("CUMULATIVE_DEPRECIATION").getString()));
                rsHDetailEx.updateDouble("WRITTEN_DOWN_VALUE",Double.parseDouble(objDetailEx.getAttribute("WRITTEN_DOWN_VALUE").getString()));
                //rsHDetailEx.updateString("NET_BLOCK",objDetailEx.getAttribute("NET_BLOCK").getString());
                rsHDetailEx.updateString("REMARKS",objDetailEx.getAttribute("REMARKS").getString());
                rsHDetailEx.updateString("SHIFT",objDetailEx.getAttribute("SHIFT").getString());
                rsHDetailEx.updateBoolean("CHANGED",true);
                rsHDetailEx.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetailEx.updateBoolean("CANCELLED",false);
                rsHDetailEx.updateString("MODIFIED_BY",String.valueOf(getAttribute("MODIFIED_BY").getInt()));
                rsHDetailEx.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
                rsHDetailEx.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
                rsHDetailEx.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                rsHDetailEx.insertRow();
                
            }
            
            
            //---INSERT THE IT DATA IN THE DETAIL_ex TABLE
            
            for(int i=1;i<=colFASItemsDetailExIT.size();i++) {
                clsFASCardwithoutGRNDetailEx objDetailEx = (clsFASCardwithoutGRNDetailEx) colFASItemsDetailExIT.get(Integer.toString(i));
                
                rsDetailEx.moveToInsertRow();
                rsDetailEx.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                rsDetailEx.updateString("ASSET_NO",objDetailEx.getAttribute("ASSET_NO").getString());
                rsDetailEx.updateInt("SR_NO",objDetailEx.getAttribute("SR_NO").getInt());
                rsDetailEx.updateInt("DETAIL_SR_NO",objDetailEx.getAttribute("DETAIL_SR_NO").getInt());
                rsDetailEx.updateString("TYPE",objDetailEx.getAttribute("TYPE").getString());//  1 - Book , 2 - IT
                rsDetailEx.updateString("YEAR",objDetailEx.getAttribute("YEAR").getString());
                rsDetailEx.updateDouble("OPENING_BALANCE",Double.parseDouble(objDetailEx.getAttribute("OPENING_BALANCE").getString()));
                rsDetailEx.updateDouble("ADDITIONS",objDetailEx.getAttribute("ADDITIONS").getDouble());
                rsDetailEx.updateDouble("CLOSING_BALANCE",Double.parseDouble(objDetailEx.getAttribute("CLOSING_BALANCE").getString()));
                rsDetailEx.updateString("DEPRECIATION_FROM_DATE",objDetailEx.getAttribute("DEPRECIATION_FROM_DATE").getString());
                rsDetailEx.updateString("DEPRECIATION_TO_DATE",objDetailEx.getAttribute("DEPRECIATION_TO_DATE").getString());
                rsDetailEx.updateDouble("DEPRECIATION_PERCENTAGE",Double.parseDouble(objDetailEx.getAttribute("DEPRECIATION_PERCENTAGE").getString()));
                rsDetailEx.updateInt("DEPRECIATION_METHOD",Integer.parseInt(objDetailEx.getAttribute("DEPRECIATION_METHOD").getString()));
                rsDetailEx.updateDouble("DEPRECIATION_FOR_THE_YEAR",Double.parseDouble(objDetailEx.getAttribute("DEPRECIATION_FOR_THE_YEAR").getString()));
                rsDetailEx.updateString("SHIFT_ALLOW_FOR_THE_YEAR",objDetailEx.getAttribute("SHIFT_ALLOW_FOR_THE_YEAR").getString());
                rsDetailEx.updateDouble("CUMULATIVE_DEPRECIATION",Double.parseDouble(objDetailEx.getAttribute("CUMULATIVE_DEPRECIATION").getString()));
                rsDetailEx.updateDouble("WRITTEN_DOWN_VALUE",Double.parseDouble(objDetailEx.getAttribute("WRITTEN_DOWN_VALUE").getString()));
                //rsDetailEx.updateString("NET_BLOCK",objDetailEx.getAttribute("NET_BLOCK").getString());
                rsDetailEx.updateString("REMARKS",objDetailEx.getAttribute("REMARKS").getString());
                rsDetailEx.updateString("SHIFT",objDetailEx.getAttribute("SHIFT").getString());
                rsDetailEx.updateBoolean("CHANGED",true);
                rsDetailEx.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsDetailEx.updateBoolean("CANCELLED",false);
                rsDetailEx.updateString("MODIFIED_BY",String.valueOf(getAttribute("MODIFIED_BY").getInt()));
                rsDetailEx.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
                rsDetailEx.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
                rsDetailEx.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                rsDetailEx.insertRow();
                
                rsHDetailEx.moveToInsertRow();
                rsHDetailEx.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
                rsHDetailEx.updateString("UPDATED_BY",String.valueOf(getAttribute("MODIFIED_BY").getInt()));
                rsHDetailEx.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
                rsHDetailEx.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetailEx.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
                
                rsHDetailEx.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                rsHDetailEx.updateString("ASSET_NO",objDetailEx.getAttribute("ASSET_NO").getString());
                rsHDetailEx.updateInt("SR_NO",objDetailEx.getAttribute("SR_NO").getInt());
                rsHDetailEx.updateInt("DETAIL_SR_NO",objDetailEx.getAttribute("DETAIL_SR_NO").getInt());
                rsHDetailEx.updateString("TYPE",objDetailEx.getAttribute("TYPE").getString());//  1 - Book , 2 - IT
                rsHDetailEx.updateString("YEAR",objDetailEx.getAttribute("YEAR").getString());
                rsHDetailEx.updateDouble("OPENING_BALANCE",Double.parseDouble(objDetailEx.getAttribute("OPENING_BALANCE").getString()));
                rsHDetailEx.updateDouble("ADDITIONS",objDetailEx.getAttribute("ADDITIONS").getDouble());
                rsHDetailEx.updateDouble("CLOSING_BALANCE",Double.parseDouble(objDetailEx.getAttribute("CLOSING_BALANCE").getString()));
                rsHDetailEx.updateString("DEPRECIATION_FROM_DATE",objDetailEx.getAttribute("DEPRECIATION_FROM_DATE").getString());
                rsHDetailEx.updateString("DEPRECIATION_TO_DATE",objDetailEx.getAttribute("DEPRECIATION_TO_DATE").getString());
                rsHDetailEx.updateDouble("DEPRECIATION_PERCENTAGE",Double.parseDouble(objDetailEx.getAttribute("DEPRECIATION_PERCENTAGE").getString()));
                rsHDetailEx.updateInt("DEPRECIATION_METHOD",Integer.parseInt(objDetailEx.getAttribute("DEPRECIATION_METHOD").getString()));
                rsHDetailEx.updateDouble("DEPRECIATION_FOR_THE_YEAR",Double.parseDouble(objDetailEx.getAttribute("DEPRECIATION_FOR_THE_YEAR").getString()));
                rsHDetailEx.updateString("SHIFT_ALLOW_FOR_THE_YEAR",objDetailEx.getAttribute("SHIFT_ALLOW_FOR_THE_YEAR").getString());
                rsHDetailEx.updateDouble("CUMULATIVE_DEPRECIATION",Double.parseDouble(objDetailEx.getAttribute("CUMULATIVE_DEPRECIATION").getString()));
                rsHDetailEx.updateDouble("WRITTEN_DOWN_VALUE",Double.parseDouble(objDetailEx.getAttribute("WRITTEN_DOWN_VALUE").getString()));
                //rsHDetailEx.updateString("NET_BLOCK",objDetailEx.getAttribute("NET_BLOCK").getString());
                rsHDetailEx.updateString("REMARKS",objDetailEx.getAttribute("REMARKS").getString());
                rsHDetailEx.updateString("SHIFT",objDetailEx.getAttribute("SHIFT").getString());
                rsHDetailEx.updateBoolean("CHANGED",true);
                rsHDetailEx.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetailEx.updateBoolean("CANCELLED",false);
                rsHDetailEx.updateString("MODIFIED_BY",String.valueOf(getAttribute("MODIFIED_BY").getInt()));
                rsHDetailEx.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
                rsHDetailEx.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
                rsHDetailEx.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                rsHDetailEx.insertRow();
                
            }
            
            AssetNo=(String)getAttribute("ASSET_NO").getObj();
            sql =  "DELETE FROM D_FAS_OTHER_VOUCHER WHERE COMPANY_ID="+CompanyID+" AND ASSET_NO='"+AssetNo+"' ";
            data.Execute(sql,FinanceGlobal.FinURL);
            
            // Inserting records in OTHERS VOUCHER TABLE
            stOthers=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsOthers=stOthers.executeQuery("SELECT * FROM D_FAS_OTHER_VOUCHER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ASSET_NO='1'");
            rsOthers.first();
            
            for(int i=1;i<=colOthers.size();i++) {
                clsFASOthersVoucher ObjOthers=(clsFASOthersVoucher) colOthers.get(Integer.toString(i));
                
                rsOthers.moveToInsertRow();
                rsOthers.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                rsOthers.updateString("ASSET_NO",getAttribute("ASSET_NO").getString());
                rsOthers.updateString("OTHERS_VOUCHER_NO",(String) ObjOthers.getAttribute("OTHERS_VOUCHER_NO").getObj());
                rsOthers.updateString("OTHERS_VOUCHER_DATE",(String) ObjOthers.getAttribute("OTHERS_VOUCHER_DATE").getObj());
                rsOthers.updateDouble("OTHERS_VOUCHER_AMOUNT",Double.parseDouble(ObjOthers.getAttribute("OTHERS_VOUCHER_AMOUNT").getString()));
                rsOthers.insertRow();
                
                rsHOthers.moveToInsertRow();
                rsHOthers.updateInt("REVISION_NO",RevNo);
                rsHOthers.updateString("UPDATED_BY",String.valueOf(getAttribute("MODIFIED_BY").getInt()));
                rsHOthers.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
                rsHOthers.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHOthers.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
                rsHOthers.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                rsHOthers.updateString("ASSET_NO",getAttribute("ASSET_NO").getString());
                rsHOthers.updateString("OTHERS_VOUCHER_NO",(String) ObjOthers.getAttribute("OTHERS_VOUCHER_NO").getObj());
                rsHOthers.updateString("OTHERS_VOUCHER_DATE",(String) ObjOthers.getAttribute("OTHERS_VOUCHER_DATE").getObj());
                rsHOthers.updateDouble("OTHERS_VOUCHER_AMOUNT",Double.parseDouble(ObjOthers.getAttribute("OTHERS_VOUCHER_AMOUNT").getString()));
                rsHOthers.insertRow();
                
            }
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=getAttribute("COMPANY_ID").getInt();
            ObjFlow.ModuleID=clsFASCardwithoutGRN.ModuleID;
            ObjFlow.DocNo=getAttribute("ASSET_NO").getString();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName=clsModules.getHeaderTableName(EITLERPGLOBAL.gCompanyID,clsFASCardwithoutGRN.ModuleID);
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName="ASSET_NO";
            ObjFlow.UseSpecifiedURL=true;
            ObjFlow.SpecificURL = FinanceGlobal.FinURL;
            //ObjFlow.SpecificURL=FinanceGlobal.FinURL;
            
            
            //==== Handling Rejected Documents ==========//
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
                data.Execute("UPDATE D_FAS_MASTER_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ASSET_NO='"+ObjFlow.DocNo+"' AND ASSET_TYPE = '" + clsFASCardwithoutGRN.AssetType + "' ");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID='" + clsFASCardwithoutGRN.ModuleID + "' AND DOC_NO='"+ObjFlow.DocNo+"'");
                
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
            
            if(ObjFlow.Status.equals("F")) {
                PostFASItem(getAttribute("ITEM_ID").getString(),getAttribute("ASSET_DATE").getString(),(int) getAttribute("METHOD_ID").getVal(),Double.parseDouble(getAttribute("BOOK_PER").getString()),Double.parseDouble(getAttribute("IT_PER").getString()));
                
                //PostFASItem(getAttribute("ITEM_ID").getString(),getAttribute("ASSET_DATE").getString(),(int) getAttribute("METHOD_ID").getVal(),Double.parseDouble(getAttribute("BOOK_PER").getString()),Double.parseDouble(getAttribute("IT_PER").getString()));
            }
            MoveLast();
            //--------- Approval Flow Update complete -----------
            
            
            setData();
            return true;
        }
        catch(Exception e) {
            try {
                e.printStackTrace();
            } catch(Exception c) {
            }
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FAS_MASTER_HEADER WHERE COMPANY_ID="+pCompanyID+" AND ASSET_NO='"+pDocNo+"' AND (APPROVED=1)";
            
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
    public boolean IsEditable(int pCompanyID,String pDocNo,int pUserID,int ModuleID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FAS_MASTER_HEADER WHERE COMPANY_ID="+pCompanyID+" AND ASSET_NO='"+pDocNo+"' AND (APPROVED=1)";
            
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
            String lDocNo=getAttribute("ASSET_NO").getString();
            String strSQL="";
            
            //First check that record is editable
            if(CanDelete(CompanyID,lDocNo,pUserID)) {
                String strQry = "DELETE FROM D_FAS_MASTER_HEADER WHERE COMPANY_ID=" + CompanyID +" AND ASSET_NO='"+lDocNo+"'";
                data.Execute(strQry,FinanceGlobal.FinURL);
                strQry = "DELETE FROM D_FAS_MASTER_HEADER WHERE COMPANY_ID=" + CompanyID +" AND ASSET_NO='"+lDocNo+"'";
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
    
    public boolean Filter(String Condition,int CompanyID) {
        Ready=false;
        try {
            
            String strSql = "SELECT * FROM D_FAS_MASTER_HEADER " + Condition ;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            //System.out.println(strSql);
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_FAS_MASTER_HEADER WHERE COMPANY_ID="+CompanyID+" AND ASSET_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND ASSET_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' AND ASSET_TYPE = '2' ORDER BY ASSET_NO";
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
            
            
            //General Retrieval of the Voucher
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",UtilFunctions.getInt(rsResultSet,"REVISION_NO",0));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            /*setAttribute("COMPANY_ID",UtilFunctions.getInt(rsResultSet,"COMPANY_ID",0));
            setAttribute("VOUCHER_TYPE",UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",1));
            setAttribute("VOUCHER_NO",UtilFunctions.getString(rsResultSet,"VOUCHER_NO",""));
            setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"VOUCHER_DATE","0000-00-00")));
             UtilFunctions.get(rsResultSet,"","")
             */
            
            //SET HEADER DATA
            setAttribute("COMPANY_ID",UtilFunctions.getInt(rsResultSet,"COMPANY_ID",0));
            // setAttribute("PREFIX",SelPrefix);
            //setAttribute("SUFFIX",SelSuffix);
            //setAttribute("FFNO",FFNo);
            setAttribute("ASSET_NO",UtilFunctions.getString(rsResultSet,"ASSET_NO",""));
            setAttribute("ASSET_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"ASSET_DATE","0000-00-00")));
            setAttribute("ITEM_ID",UtilFunctions.getString(rsResultSet,"ITEM_ID",""));
            setAttribute("ITEM_DESC",UtilFunctions.getString(rsResultSet,"ITEM_DESC",""));
            setAttribute("SUPPLIER_CODE",UtilFunctions.getString(rsResultSet,"SUPPLIER_CODE",""));
            setAttribute("DEPT_ID",UtilFunctions.getString(rsResultSet,"DEPT_ID",""));
            setAttribute("PO_NO",UtilFunctions.getString(rsResultSet,"PO_NO",""));
            setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"PO_DATE","0000-00-00")));
            setAttribute("GURANTEE",UtilFunctions.getString(rsResultSet,"GUARANTEE",""));
            setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsResultSet,"MAIN_ACCOUNT_CODE",""));
            setAttribute("EXPECTED_LIFE",UtilFunctions.getString(rsResultSet,"EXPECTED_LIFE",""));
            setAttribute("WARRANTY",UtilFunctions.getString(rsResultSet,"WARRANTY",""));
            setAttribute("YEAR_OF_PURCHASE",UtilFunctions.getString(rsResultSet,"YEAR_OF_PURCHASE",""));
            setAttribute("DISPOSED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"DISPOSED_DATE","0000-00-00")));
            setAttribute("SIZE_CAPACITY",UtilFunctions.getString(rsResultSet,"SIZE_CAPACITY",""));
            setAttribute("INSTALLATION_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"INSTALLATION_DATE","0000-00-00")));
            setAttribute("TOTAL_QTY",String.valueOf(EITLERPGLOBAL.round(UtilFunctions.getDouble(rsResultSet,"QTY",0),0)));
            setAttribute("MODEL_NO",UtilFunctions.getString(rsResultSet,"MODEL_NO",""));
            setAttribute("MACHINE_NO",UtilFunctions.getString(rsResultSet,"MACHINE_NO",""));
            setAttribute("INVOICE_NO",UtilFunctions.getString(rsResultSet,"INVOICE_NO",""));
            setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"INVOICE_DATE","0000-00-00")));
            setAttribute("HEADER_REMARKS",UtilFunctions.getString(rsResultSet,"HEADER_REMARKS",""));
            
            setAttribute("METHOD_ID",UtilFunctions.getDouble(rsResultSet,"METHOD_ID",0));
            setAttribute("BOOK_PER",String.valueOf(UtilFunctions.getDouble(rsResultSet,"BOOK_PER",0)));
            setAttribute("IT_PER",String.valueOf(UtilFunctions.getDouble(rsResultSet,"IT_PER",0)));
            
            setAttribute("PJ_VOUCHER_NO",UtilFunctions.getString(rsResultSet,"PJ_VOUCHER_NO",""));
            setAttribute("PJ_VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"PJ_VOUCHER_DATE","0000-00-00")));
            setAttribute("PJ_VOUCHER_AMOUNT",UtilFunctions.getDouble(rsResultSet,"PJ_VOUCHER_AMOUNT",0));
            setAttribute("CUSTOM_DUTY_VOUCHER_NO",UtilFunctions.getString(rsResultSet,"CUSTOM_DUTY_VOUCHER_NO",""));
            setAttribute("CUSTOM_DUTY_VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CUSTOM_DUTY_VOUCHER_DATE","0000-00-00")));
            setAttribute("CUSTOM_DUTY_VOUCHER_AMOUNT",UtilFunctions.getDouble(rsResultSet,"CUSTOM_DUTY_VOUCHER_AMOUNT",0));
            setAttribute("LANDING_VOUCHER_NO",UtilFunctions.getString(rsResultSet,"LANDING_VOUCHER_NO",""));
            setAttribute("LANDING_VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"LANDING_VOUCHER_DATE","0000-00-00")));
            setAttribute("LANDING_VOUCHER_AMOUNT",UtilFunctions.getDouble(rsResultSet,"LANDING_VOUCHER_AMOUNT",0));
            setAttribute("FREIGHT_VOUCHER_NO",UtilFunctions.getString(rsResultSet,"FREIGHT_OCTROI_VOUCHER_NO",""));
            setAttribute("FREIGHT_VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"FREIGHT_OCTROI_VOUCHER_DATE","0000-00-00")));
            setAttribute("FREIGHT_VOUCHER_AMOUNT",UtilFunctions.getDouble(rsResultSet,"FREIGHT_OCTROI_VOUCHER_AMOUNT",0));
            setAttribute("INSTALLATION_VOUCHER_NO",UtilFunctions.getString(rsResultSet,"INSTALLATION_VOUCHER_NO",""));
            setAttribute("INSTALLATION_VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"INSTALLATION_VOUCHER_DATE","0000-00-00")));
            setAttribute("INSTALLATION_VOUCHER_AMOUNT",UtilFunctions.getDouble(rsResultSet,"INSTALLATION_VOUCHER_AMOUNT",0));
            setAttribute("OTHERS_VOUCHER_NO",UtilFunctions.getString(rsResultSet,"OTHERS_VOUCHER_NO",""));
            setAttribute("OTHERS_VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"OTHERS_VOUCHER_DATE","0000-00-00")));
            setAttribute("OTHERS_VOUCHER_AMOUNT",UtilFunctions.getDouble(rsResultSet,"OTHERS_VOUCHER_AMOUNT",0));
            setAttribute("SERVICE_TAX_AMOUNT",UtilFunctions.getDouble(rsResultSet,"SERVICE_TAX_AMOUNT",0));
            
            setAttribute("HIERARCHY_ID",UtilFunctions.getDouble(rsResultSet,"HIERARCHY_ID",0));
            setAttribute("APPROVED",UtilFunctions.getDouble(rsResultSet,"APPROVED",0));
            setAttribute("CANCELLED",UtilFunctions.getDouble(rsResultSet,"CANCELLED",0));
            setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            setAttribute("CREATED_DATE",UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00"));
            setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            setAttribute("MODIFIED_DATE",UtilFunctions.getString(rsResultSet,"MODIFIE_DATE","0000-00-00"));
            setAttribute("REMARKS",rsResultSet.getString("REMARKS"));
            
            
            //set detail item
            colFASItemsDetail.clear();
            
            int CompanyID=getAttribute("COMPANY_ID").getInt();
            String AssetNo=getAttribute("ASSET_NO").getString();
            
            tmpStmt=tmpConn.createStatement();
            //show Data of Detail Table
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FAS_MASTER_DETAIL_H WHERE COMPANY_ID=" + CompanyID + " AND ASSET_NO='" + AssetNo + "' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            } else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FAS_MASTER_DETAIL WHERE COMPANY_ID=" + CompanyID + " AND ASSET_NO='" + AssetNo + "' ORDER BY SR_NO");
            }
            
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsFASCardwithoutGRNDetail objItem = new clsFASCardwithoutGRNDetail();
                
                objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                objItem.setAttribute("ASSET_NO",UtilFunctions.getString(rsTmp,"ASSET_NO",""));
                objItem.setAttribute("ITEM_ID",UtilFunctions.getString(rsTmp,"ITEM_ID",""));
                objItem.setAttribute("SR_NO",UtilFunctions.getString(rsTmp,"SR_NO",""));
                objItem.setAttribute("YEAR",UtilFunctions.getString(rsTmp,"YEAR",""));
                objItem.setAttribute("DEPT_ID",UtilFunctions.getString(rsTmp,"DEPT_ID","").trim());
                objItem.setAttribute("AMOUNT",UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                objItem.setAttribute("SALE_DOC_NO",UtilFunctions.getString(rsTmp,"SALE_DOC_NO",""));
                objItem.setAttribute("SALE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"SALE_DATE","0000-00-00")));
                objItem.setAttribute("SALE_VALUE",UtilFunctions.getString(rsTmp,"SALE_VALUE",""));
                //objItem.setAttribute("SJ_NUMBER",UtilFunctions.getString(rsTmp,"SJ_NUMBER",""));
                objItem.setAttribute("REMARKS",UtilFunctions.getString(rsTmp,"REMARKS",""));
                objItem.setAttribute("ASSET_STATUS",UtilFunctions.getString(rsTmp,"ASSET_STATUS",""));
                
                
                colFASItemsDetail.put(Long.toString(Counter),objItem);
                rsTmp.next();
            }
            
            //set detail EX  BOOK DETAIL
            colFASItemsDetailExBook.clear();
            //show Data of Detail EX Table
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FAS_MASTER_DETAIL_EX_H WHERE COMPANY_ID=" + CompanyID + " AND ASSET_NO='" + AssetNo + "' AND TYPE = 1 AND DETAIL_SR_NO = 1 AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            } else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FAS_MASTER_DETAIL_EX WHERE COMPANY_ID=" + CompanyID + " AND ASSET_NO='" + AssetNo + "' AND TYPE = 1 AND DETAIL_SR_NO = 1 ORDER BY SR_NO");
            }
            
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsFASCardwithoutGRNDetailEx objItem = new clsFASCardwithoutGRNDetailEx();
                
                objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                objItem.setAttribute("ASSET_NO",UtilFunctions.getString(rsTmp,"ASSET_NO",""));
                //objItem.setAttribute("ITEM_ID",UtilFunctions.getString(rsTmp,"ITEM_ID",""));
                objItem.setAttribute("SR_NO",UtilFunctions.getString(rsTmp,"SR_NO",""));
                objItem.setAttribute("DETAIL_SR_NO",UtilFunctions.getString(rsTmp,"DETAIL_SR_NO",""));
                objItem.setAttribute("TYPE",UtilFunctions.getString(rsTmp,"TYPE","").trim());
                objItem.setAttribute("YEAR",UtilFunctions.getString(rsTmp,"YEAR","").trim());
                objItem.setAttribute("OPENING_BALANCE",UtilFunctions.getDouble(rsTmp,"OPENING_BALANCE",0));
                objItem.setAttribute("ADDITIONS",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"ADDITIONS","")));
                objItem.setAttribute("CLOSING_BALANCE",UtilFunctions.getDouble(rsTmp,"CLOSING_BALANCE",0));
                objItem.setAttribute("DEPRECIATION_FROM_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"DEPRECIATION_FROM_DATE","0000-00-00")));
                objItem.setAttribute("DEPRECIATION_TO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"DEPRECIATION_TO_DATE","0000-00-00")));
                objItem.setAttribute("DEPRECIATION_PERCENTAGE",UtilFunctions.getString(rsTmp,"DEPRECIATION_PERCENTAGE",""));
                objItem.setAttribute("DEPRECIATION_METHOD",UtilFunctions.getString(rsTmp,"DEPRECIATION_METHOD",""));
                //objItem.setAttribute("DEPRECIATION_METHOD_NAME",UtilFunctions.getString(rsTmp,"DEPRECIATION_METHOD_NAME",""));
                objItem.setAttribute("DEPRECIATION_FOR_THE_YEAR",UtilFunctions.getDouble(rsTmp,"DEPRECIATION_FOR_THE_YEAR",0));
                objItem.setAttribute("SHIFT_ALLOW_FOR_THE_YEAR",UtilFunctions.getString(rsTmp,"SHIFT_ALLOW_FOR_THE_YEAR",""));
                objItem.setAttribute("CUMULATIVE_DEPRECIATION",UtilFunctions.getDouble(rsTmp,"CUMULATIVE_DEPRECIATION",0));
                objItem.setAttribute("WRITTEN_DOWN_VALUE",UtilFunctions.getDouble(rsTmp,"WRITTEN_DOWN_VALUE",0));
                //objItem.setAttribute("NET_BLOCK",UtilFunctions.getString(rsTmp,"NET_BLOCK",""));
                objItem.setAttribute("REMARKS",UtilFunctions.getString(rsTmp,"REMARKS",""));
                objItem.setAttribute("SHIFT",UtilFunctions.getString(rsTmp,"SHIFT",""));
                
                
                colFASItemsDetailExBook.put(Long.toString(Counter),objItem);
                rsTmp.next();
            }
            
            
            
            //set detail EX  IT DETAIL
            colFASItemsDetailExIT.clear();
            //show Data of Detail EX Table
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FAS_MASTER_DETAIL_EX_H WHERE COMPANY_ID=" + CompanyID + " AND ASSET_NO='" + AssetNo + "' AND TYPE = 1 AND DETAIL_SR_NO = 1 AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            } else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FAS_MASTER_DETAIL_EX WHERE COMPANY_ID=" + CompanyID + " AND ASSET_NO='" + AssetNo + "' AND TYPE = 2 AND DETAIL_SR_NO = 1 ORDER BY SR_NO");
            }
            
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsFASCardwithoutGRNDetailEx objItem = new clsFASCardwithoutGRNDetailEx();
                
                objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                objItem.setAttribute("ASSET_NO",UtilFunctions.getString(rsTmp,"ASSET_NO",""));
                //objItem.setAttribute("ITEM_ID",UtilFunctions.getString(rsTmp,"ITEM_ID",""));
                objItem.setAttribute("SR_NO",UtilFunctions.getString(rsTmp,"SR_NO",""));
                objItem.setAttribute("DETAIL_SR_NO",UtilFunctions.getString(rsTmp,"DETAIL_SR_NO",""));
                objItem.setAttribute("TYPE",UtilFunctions.getString(rsTmp,"TYPE","").trim());
                objItem.setAttribute("YEAR",UtilFunctions.getString(rsTmp,"YEAR","").trim());
                objItem.setAttribute("OPENING_BALANCE",UtilFunctions.getDouble(rsTmp,"OPENING_BALANCE",0));
                objItem.setAttribute("ADDITIONS",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"ADDITIONS","")));
                objItem.setAttribute("CLOSING_BALANCE",UtilFunctions.getDouble(rsTmp,"CLOSING_BALANCE",0));
                objItem.setAttribute("DEPRECIATION_FROM_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"DEPRECIATION_FROM_DATE","0000-00-00")));
                objItem.setAttribute("DEPRECIATION_TO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"DEPRECIATION_TO_DATE","0000-00-00")));
                objItem.setAttribute("DEPRECIATION_PERCENTAGE",UtilFunctions.getString(rsTmp,"DEPRECIATION_PERCENTAGE",""));
                objItem.setAttribute("DEPRECIATION_METHOD",UtilFunctions.getString(rsTmp,"DEPRECIATION_METHOD",""));
                //objItem.setAttribute("DEPRECIATION_METHOD_NAME",UtilFunctions.getString(rsTmp,"DEPRECIATION_METHOD_NAME",""));
                objItem.setAttribute("DEPRECIATION_FOR_THE_YEAR",UtilFunctions.getDouble(rsTmp,"DEPRECIATION_FOR_THE_YEAR",0));
                objItem.setAttribute("SHIFT_ALLOW_FOR_THE_YEAR",UtilFunctions.getString(rsTmp,"SHIFT_ALLOW_FOR_THE_YEAR",""));
                objItem.setAttribute("CUMULATIVE_DEPRECIATION",UtilFunctions.getDouble(rsTmp,"CUMULATIVE_DEPRECIATION",0));
                objItem.setAttribute("WRITTEN_DOWN_VALUE",UtilFunctions.getDouble(rsTmp,"WRITTEN_DOWN_VALUE",0));
                //objItem.setAttribute("NET_BLOCK",UtilFunctions.getString(rsTmp,"NET_BLOCK",""));
                objItem.setAttribute("REMARKS",UtilFunctions.getString(rsTmp,"REMARKS",""));
                objItem.setAttribute("SHIFT",UtilFunctions.getString(rsTmp,"SHIFT",""));
                
                
                colFASItemsDetailExIT.put(Long.toString(Counter),objItem);
                rsTmp.next();
            }
            
            colOthers.clear();
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FAS_OTHER_VOUCHER_H WHERE COMPANY_ID=" + CompanyID + " AND ASSET_NO='" + AssetNo + "' REVISION_NO="+RevNo+" ORDER BY ASSET_NO");
            } else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FAS_OTHER_VOUCHER WHERE COMPANY_ID=" + CompanyID + " AND ASSET_NO='" + AssetNo + "'  ORDER BY ASSET_NO");
            }
            
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsFASOthersVoucher objItem = new clsFASOthersVoucher();
                
                objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                objItem.setAttribute("ASSET_NO",UtilFunctions.getString(rsTmp,"ASSET_NO",""));
                //objItem.setAttribute("ITEM_ID",UtilFunctions.getString(rsTmp,"ITEM_ID",""));
                objItem.setAttribute("OTHERS_VOUCHER_NO",UtilFunctions.getString(rsTmp,"OTHERS_VOUCHER_NO",""));
                objItem.setAttribute("OTHERS_VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"OTHERS_VOUCHER_DATE","0000-00-00")));
                objItem.setAttribute("OTHERS_VOUCHER_AMOUNT",UtilFunctions.getDouble(rsTmp,"OTHERS_VOUCHER_AMOUNT",0));
                
                
                colOthers.put(Long.toString(Counter),objItem);
                rsTmp.next();
            }
            //********** Special Representation Logics *************//
            if(UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",0)==FinanceGlobal.TYPE_PAYMENT||UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",0)==FinanceGlobal.TYPE_PAYMENT_2) {
                // getPaymentRepresentation(); //Convert data into Payment Voucher presentation
            }
            
            if(UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",0)==FinanceGlobal.TYPE_DEBIT_NOTE) {
                //getDebitNoteRepresentation(); //Convert data into Debit Note Voucher presentation
            }
            
            if(UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",0)==FinanceGlobal.TYPE_CASH_VOUCHER) {
                //getCashPaymentRepresentation(); //Convert data into Cash Payment Voucher presentation
            }
            
            if(UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",0)==FinanceGlobal.TYPE_RECEIPT) {
                // getReceiptRepresentation(); //Convert data into Cash Payment Voucher presentation
            }
            
            if(UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",0)==FinanceGlobal.TYPE_CASH_RECEIPT_VOUCHER) {
                //getCashReceiptRepresentation(); //Convert data into Cash Payment Voucher presentation
            }
            
            
            if(UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",0)==FinanceGlobal.TYPE_CREDIT_NOTE) {
                // getCreditNoteRepresentation(); //Convert data into Cash Payment Voucher presentation
            }
            
            if(UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",0)==FinanceGlobal.TYPE_LC_JV) {
                //getLCJVRepresentation();
            }
            
            if(UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",0)==FinanceGlobal.TYPE_PJV) {
                //getPJVRepresentation();
            }
            
            if(UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",0)==FinanceGlobal.TYPE_JOURNAL) {
                //getPJVRepresentation();
            }
            
            if(UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",0)==FinanceGlobal.TYPE_SALES_JOURNAL) {
                //getPJVRepresentation();
            }
            //******************************************************//
            
            return true;
        }
        catch(Exception e) {
            //e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    
    
    public static HashMap getPendingApprovals(int CompanyID,int UserID,int Order, int ModuleID,int FinYearFrom) {
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
                strSQL="SELECT FINANCE.D_FAS_MASTER_HEADER.ASSET_NO,FINANCE.D_FAS_MASTER_HEADER.ASSET_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE " +
                "FROM FINANCE.D_FAS_MASTER_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA " +
                "WHERE FINANCE.D_FAS_MASTER_HEADER.ASSET_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FAS_MASTER_HEADER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID " +
                "AND FINANCE.D_FAS_MASTER_HEADER.COMPANY_ID="+CompanyID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' " +
                "AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT FINANCE.D_FAS_MASTER_HEADER.ASSET_NO,FINANCE.D_FAS_MASTER_HEADER.ASSET_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE " +
                "FROM FINANCE.D_FAS_MASTER_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA " +
                "WHERE FINANCE.D_FAS_MASTER_HEADER.ASSET_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FAS_MASTER_HEADER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID " +
                "AND FINANCE.D_FAS_MASTER_HEADER.COMPANY_ID="+CompanyID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' " +
                "AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY FINANCE.D_FAS_MASTER_HEADER.ASSET_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT FINANCE.D_FAS_MASTER_HEADER.ASSET_NO,FINANCE.D_FAS_MASTER_HEADER.ASSET_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE " +
                "FROM FINANCE.D_FAS_MASTER_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA " +
                "WHERE FINANCE.D_FAS_MASTER_HEADER.ASSET_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FAS_MASTER_HEADER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID " +
                "AND FINANCE.D_FAS_MASTER_HEADER.COMPANY_ID="+CompanyID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' " +
                "AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY FINANCE.D_FAS_MASTER_HEADER.ASSET_NO";
            }
            
            rsTmp3=tmpStmt3.executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp3.next()) {
                if(EITLERPGLOBAL.isWithinDate(rsTmp3.getString("ASSET_DATE"),FinYearFrom)) {
                    Counter=Counter+1;
                    clsFASCardwithoutGRN ObjDoc=new clsFASCardwithoutGRN();
                    
                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("ASSET_NO",rsTmp3.getString("ASSET_NO"));
                    ObjDoc.setAttribute("ASSET_DATE",rsTmp3.getString("ASSET_DATE"));
                    ObjDoc.setAttribute("MODIFIED_DATE",rsTmp3.getString("RECEIVED_DATE"));
                    //String GRNNo=data.getStringValueFromDB("SELECT GRN_NO FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+rsTmp3.getString("VOUCHER_NO")+"'",FinanceGlobal.FinURL);
                    //ObjDoc.setAttribute("GRN_NO",GRNNo);
                    // ----------------- End of Header Fields ------------------------------------//
                    
                    //String PartyCode=data.getStringValueFromDB("SELECT SUB_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+rsTmp3.getString("VOUCHER_NO")+"' AND SUB_ACCOUNT_CODE<>'' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID,FinanceGlobal.FinURL);
                    //String MainCode =data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+rsTmp3.getString("VOUCHER_NO")+"' AND SUB_ACCOUNT_CODE<>'' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID,FinanceGlobal.FinURL);
                    //String PartyName=clsAccount.getAccountName(MainCode,PartyCode);
                    
                    //String PartyName=data.getStringValueFromDB("SELECT PARTY_NAME FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+PartyCode+"'",FinanceGlobal.FinURL);
                    
                    //ObjDoc.setAttribute("PARTY_CODE",PartyCode);
                    //ObjDoc.setAttribute("PARTY_NAME",PartyName);
                    
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
    
    
    
    public boolean ShowHistory(int CompanyID,String DocNo) {
        Ready=false;
        try {
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FAS_MASTER_HEADER_H WHERE COMPANY_ID="+CompanyID+" AND ASSET_NO='"+DocNo+"'");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM D_FAS_MASTER_HEADER_H WHERE COMPANY_ID="+CompanyID+" AND ASSET_NO='"+DocNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                
                while(!rsTmp.isAfterLast()) {
                    clsFASCardwithoutGRN objDoc=new clsFASCardwithoutGRN();
                    
                    objDoc.setAttribute("ASSET_NO",rsTmp.getString("ASSET_NO"));
                    objDoc.setAttribute("ASSET_DATE",EITLERPGLOBAL.formatDate(rsTmp.getString("ASSET_DATE")));
                    objDoc.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                    objDoc.setAttribute("UPDATED_BY",rsTmp.getString("UPDATED_BY"));
                    objDoc.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                    objDoc.setAttribute("ENTRY_DATE",EITLERPGLOBAL.formatDate(rsTmp.getString("ENTRY_DATE")));
                    objDoc.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                    
                    List.put(Integer.toString(List.size()+1),objDoc);
                    
                    rsTmp.next();
                }
                
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
    
    
    
    public static String getDocStatus(int CompanyID,String DocNo) {
        ResultSet rsTmp;
        String strMessage="";
        
        try {
            rsTmp=data.getResult("SELECT VOUCHER_NO,APPROVED,CANCELLED FROM D_FIN_VOUCHER_HEADER WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+DocNo+"'",FinanceGlobal.FinURL);
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
    
    public static boolean CanCancel(int CompanyID,String DocNo,int ModuleID) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            rsTmp=data.getResult("SELECT VOUCHER_NO,VOUCHER_TYPE FROM D_FIN_VOUCHER_HEADER WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+DocNo+"' AND APPROVED=0 AND CANCELLED=0",FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                //int VoucherType=UtilFunctions.getInt(rsTmp,"VOUCHER_TYPE", 0);
                //int ModuleID=clsVoucher.getVoucherModuleID(VoucherType);
                
                //if(ModuleID==VModuleID) {
                //  canCancel=true;
                //} else {
                //  canCancel=false;
                // }
            }
            rsTmp.close();
        } catch(Exception e) {
        }
        return canCancel;
    }
    
    public Object getObject(int pCompanyID,String pDocNo) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND ASSET_NO='"+pDocNo+"' AND ASSET_TYPE='" + clsFASCardwithoutGRN.AssetType + "' ";
        clsFASCardwithoutGRN ObjFASCard = new clsFASCardwithoutGRN();
        ObjFASCard.Filter(strCondition,pCompanyID);
        return ObjFASCard;
    }
    
    public static boolean CancelDoc(int CompanyID,String DocNo,int ModuleID) {
        
        ResultSet rsTmp=null,rsIndent=null;
        boolean Cancelled=false;
        
        try {
            if(CanCancel(CompanyID,DocNo,ModuleID)) {
                
                boolean ApprovedDoc=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_FIN_VOUCHER_HEADER WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+DocNo+"'",FinanceGlobal.FinURL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedDoc) {
                    
                }
                else {
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"' AND MODULE_ID="+(ModuleID));
                }
                
                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_FIN_VOUCHER_HEADER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+DocNo+"'",FinanceGlobal.FinURL);
                data.Execute("UPDATE D_FIN_VOUCHER_DETAIL SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+DocNo+"'",FinanceGlobal.FinURL);
                data.Execute("UPDATE D_FIN_VOUCHER_DETAIL_EX SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+DocNo+"'",FinanceGlobal.FinURL);
                
                Cancelled=true;
            }
            
            rsTmp.close();
            rsIndent.close();
        }
        catch(Exception e) {
            
        }
        
        return Cancelled;
    }
    
    public boolean PostFASItem(String pItemID,String pDocDate,int pMethodID,double pBookPer,double pITPer) {
        try {
            
            ResultSet rsVoucher=data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID="+clsFASItemMapping.ModuleID);
            rsVoucher.first();
            String SelPrefix="",SelSuffix="";
            int FFNo=0;
            if(rsVoucher.getRow()>0) {
                SelPrefix=rsVoucher.getString("PREFIX_CHARS");
                SelSuffix=rsVoucher.getString("SUFFIX_CHARS");
                FFNo=rsVoucher.getInt("FIRSTFREE_NO");
            }
            String DocNo = clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,clsFASItemMapping.ModuleID, FFNo,true);
            String SQL= "INSERT INTO D_FAS_ITEM_MASTER_HEADER "+
            "(COMPANY_ID,DOC_NO,DOC_DATE,ITEM_ID,REMARKS,MODIFIED_BY,MODIFIED_DATE,APPROVED,APPROVED_DATE,CANCELLED,CREATED_BY,CREATED_DATE) "+
            "VALUES "+
            "('" + EITLERPGLOBAL.gCompanyID + "','" + DocNo + "','" + pDocDate + "','" + pItemID + "',' AutoGenerated ','0','" + EITLERPGLOBAL.getCurrentDateDB() + "','1','" + EITLERPGLOBAL.getCurrentDateDB() + "','0','0','" + EITLERPGLOBAL.getCurrentDateDB() + "') ";
            data.Execute(SQL,FinanceGlobal.FinURL);
            
            String FromDate=pDocDate;
            String ToDate = EITLERPGLOBAL.formatDate(clsDepositMaster.deductDays(FromDate,1));
            ToDate = EITLERPGLOBAL.formatDateDB(ToDate.substring(0,6)+ String.valueOf(Integer.parseInt(ToDate.substring(6))+99));
            
            SQL="INSERT INTO D_FAS_ITEM_MASTER_DETAIL "+
            "(COMPANY_ID,DOC_NO,SR_NO,METHOD_ID,FROM_DATE,TO_DATE,BOOK_PER,IT_PER,REMARKS,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE) "+
            "VALUES "+
            "('" + EITLERPGLOBAL.gCompanyID + "','" + DocNo + "','1','" + pMethodID + "','" + pDocDate + "','" + ToDate + "','" + pBookPer + "','" + pITPer + "','','0','" + EITLERPGLOBAL.getCurrentDateDB() + "','0','" + EITLERPGLOBAL.getCurrentDateDB() + "')";
            
            data.Execute(SQL,FinanceGlobal.FinURL);
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return true;
    }
    
}