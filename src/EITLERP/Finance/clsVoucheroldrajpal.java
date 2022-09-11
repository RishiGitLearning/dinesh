
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

public class clsVoucheroldrajpal {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    //Requisition Material Collection
    public HashMap colVoucherItems=new HashMap();
    public HashMap colVoucherItemsEx=new HashMap();
    
    //History Related properties
    private boolean HistoryView=false;
    
    public static int ModuleID=59;
    public boolean DoNotValidateAccounts=false;
    public boolean IsInternalPosting=false;
    public boolean DepositTransfer=false;
    private int BlockCounter=0;
    
    public boolean UseSpecificVoucherNo=false;
    public String SpecificVoucherNo="";
    public boolean ChangeVoucherNo=false;
    public String OldVoucherNo="";
    
    public int VoucherType=1;
    
    public clsVoucher objPayment;
    
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
    
    public static int getVoucherModuleID(int VoucherType) {
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
    }
    
    /** Creates new clsMaterialRequisition */
    public clsVoucheroldrajpal() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(0));
        props.put("VOUCHER_NO",new Variant(""));
        props.put("VOUCHER_DATE",new Variant(""));
        props.put("BOOK_CODE",new Variant(""));
        props.put("PO_NO",new Variant(""));
        props.put("PO_DATE",new Variant(""));
        props.put("PO_TYPE",new Variant(0));
        props.put("INVOICE_NO",new Variant(""));
        props.put("INVOICE_DATE",new Variant(""));
        props.put("INVOICE_AMOUNT",new Variant(0));
        props.put("GRN_NO",new Variant(""));
        props.put("GRN_DATE",new Variant(""));
        props.put("GRN_TYPE",new Variant(0));
        props.put("CHEQUE_NO",new Variant(""));
        props.put("CHEQUE_DATE",new Variant(""));
        props.put("REALIZATION_DATE",new Variant(""));
        props.put("BANK_NAME",new Variant(""));
        props.put("ST_CATEGORY",new Variant(""));
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
        props.put("PREFIX",new Variant("")); //For Autonumber generation
        props.put("EXCLUDE_IN_ADJ",new Variant(0));//Exclude voucher in auto adjustment
        props.put("CUSTOMER_BANK",new Variant(""));
        props.put("REASON_CODE",new Variant(""));
        
        props.put("REVISION_NO",new Variant(0));
        
        //Create a new object for MR Item collection
        colVoucherItems=new HashMap();
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        
        props.put("CHEQUE_AMOUNT",new Variant(0));
        props.put("PAYMENT_MODE",new Variant(0));
        props.put("LEGACY_NO",new Variant(""));
        props.put("LEGACY_DATE",new Variant(""));
        props.put("LINK_NO",new Variant(""));
        
        IsInternalPosting=false;
    }
    
    /*<b>Load Data</b>\nThis method loads data from database to Business Object*/
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FIN_VOUCHER_HEADER WHERE COMPANY_ID="+pCompanyID+" AND VOUCHER_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND VOUCHER_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' AND VOUCHER_TYPE="+VoucherType+" ORDER BY VOUCHER_DATE, VOUCHER_NO");
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
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FIN_VOUCHER_HEADER LIMIT 1");
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
        int RefModuleID = 0;
        try {
            
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date ReqDate=java.sql.Date.valueOf(EITLERPGLOBAL.formatDateDB(getAttribute("VOUCHER_DATE").getString()));
            
            if((ReqDate.after(FinFromDate)||ReqDate.compareTo(FinFromDate)==0)&&(ReqDate.before(FinToDate)||ReqDate.compareTo(FinToDate)==0)) {
                //Within the year
            } else {
                LastError="Voucher date is not within financial year.";
                //return false;
            }
            //=====================================================//
            
            
            //========= Check Document no. duplication ===================//
            if(UseSpecificVoucherNo) {
                String DocNo=SpecificVoucherNo;
                
                if(data.IsRecordExist("SELECT VOUCHER_NO FROM D_FIN_VOUCHER_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND VOUCHER_NO='"+DocNo+"'",FinanceGlobal.FinURL)) {
                    LastError="Voucher no. "+DocNo+" already exist. Please specify other voucher no.";
                    return false;
                }
            }
            //===========================================================//
            
            
            
            if(!Validate(EITLERPGLOBAL.ADD)) {
                if(!getAttribute("APPROVAL_STATUS").equals("F")) {
                    if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 1052, 10527) ) {
                        //User has rights to skip over validation
                    }
                    else {
                        return false;
                    }
                }
                else {
                    return false;
                }
            }
            
            
            
            //data.SetAutoCommit(false);
            //Conn.setAutoCommit(false);
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FIN_VOUCHER_HEADER_H WHERE VOUCHER_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FIN_VOUCHER_DETAIL_H WHERE VOUCHER_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            if(UseSpecificVoucherNo&&(!SpecificVoucherNo.equals(""))) {
                setAttribute("VOUCHER_NO",SpecificVoucherNo);
            }
            else {
                
                //--------- Generate Gatepass requisition no.  ------------
                String BookCode=getAttribute("BOOK_CODE").getString();
                String Prefix=getAttribute("PREFIX").getString();
                
                String VoucherNo=clsVoucher.getNextVoucherNo(EITLERPGLOBAL.gCompanyID, BookCode, Prefix);
                
                //setAttribute("VOUCHER_NO",clsFirstFree.getNextFreeNo(getAttribute("COMPANY_ID").getInt(),clsVoucher.ModuleID, getAttribute("FFNO").getInt(),true));
                setAttribute("VOUCHER_NO",VoucherNo);
                //-------------------------------------------------
                System.out.println("Voucher No : " + VoucherNo);
            }
            
            if( getAttribute("VOUCHER_TYPE").getInt()==FinanceGlobal.TYPE_JOURNAL ||
            getAttribute("VOUCHER_TYPE").getInt()==FinanceGlobal.TYPE_CASH_VOUCHER ||
            getAttribute("VOUCHER_TYPE").getInt()==FinanceGlobal.TYPE_CASH_RECEIPT_VOUCHER) {
                TVoucherNo = getAttribute("VOUCHER_NO").getString();
                EmployeeNo = getAttribute("EMPLOYEE_NO").getString();
            }
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getLong());
            rsResultSet.updateString("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
            rsResultSet.updateInt("VOUCHER_TYPE",getAttribute("VOUCHER_TYPE").getInt());
            rsResultSet.updateString("VOUCHER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("VOUCHER_DATE").getString()));
            rsResultSet.updateString("BOOK_CODE",getAttribute("BOOK_CODE").getString());
            rsResultSet.updateString("CHEQUE_NO",getAttribute("CHEQUE_NO").getString());
            rsResultSet.updateString("CHEQUE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CHEQUE_DATE").getString()));
            rsResultSet.updateString("REALIZATION_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("REALIZATION_DATE").getString()));
            rsResultSet.updateString("BANK_NAME",getAttribute("BANK_NAME").getString());
            rsResultSet.updateString("ST_CATEGORY",getAttribute("ST_CATEGORY").getString());
            rsResultSet.updateString("REASON_CODE",getAttribute("REASON_CODE").getString());
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateString("CREATED_BY",getAttribute("CREATED_BY").getString()); //EITLERPGLOBAL.gLoginID
            rsResultSet.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsResultSet.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
            rsResultSet.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateDouble("CHEQUE_AMOUNT",getAttribute("CHEQUE_AMOUNT").getDouble());
            rsResultSet.updateInt("PAYMENT_MODE",getAttribute("PAYMENT_MODE").getInt());
            rsResultSet.updateString("LEGACY_NO",getAttribute("LEGACY_NO").getString());
            rsResultSet.updateString("LINK_NO",getAttribute("LINK_NO").getString());
            rsResultSet.updateString("LEGACY_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("LEGACY_DATE").getString()));
            rsResultSet.updateInt("EXCLUDE_IN_ADJ",getAttribute("EXCLUDE_IN_ADJ").getInt());
            rsResultSet.updateString("CUSTOMER_BANK",getAttribute("CUSTOMER_BANK").getString());
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
            rsHistory.updateString("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
            rsHistory.updateString("VOUCHER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("VOUCHER_DATE").getString()));
            rsHistory.updateInt("VOUCHER_TYPE",getAttribute("VOUCHER_TYPE").getInt());
            rsHistory.updateString("BOOK_CODE",getAttribute("BOOK_CODE").getString());
            rsHistory.updateString("CHEQUE_NO",getAttribute("CHEQUE_NO").getString());
            rsHistory.updateString("CHEQUE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CHEQUE_DATE").getString()));
            rsHistory.updateString("REALIZATION_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("REALIZATION_DATE").getString()));
            rsHistory.updateString("BANK_NAME",getAttribute("BANK_NAME").getString());
            rsHistory.updateString("ST_CATEGORY",getAttribute("ST_CATEGORY").getString());
            rsHistory.updateString("REASON_CODE",getAttribute("REASON_CODE").getString());
            rsHistory.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            
            rsHistory.updateString("CREATED_BY",getAttribute("CREATED_BY").getString()); //EITLERPGLOBAL.gLoginID
            rsHistory.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsHistory.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
            rsHistory.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            
            //            rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID );
            //            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            //            rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            //            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.updateDouble("CHEQUE_AMOUNT",getAttribute("CHEQUE_AMOUNT").getDouble());
            rsHistory.updateInt("PAYMENT_MODE",getAttribute("PAYMENT_MODE").getInt());
            rsHistory.updateString("LEGACY_NO",getAttribute("LEGACY_NO").getString());
            rsHistory.updateString("LINK_NO",getAttribute("LINK_NO").getString());
            rsHistory.updateString("LEGACY_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("LEGACY_DATE").getString()));
            rsHistory.updateInt("EXCLUDE_IN_ADJ",getAttribute("EXCLUDE_IN_ADJ").getInt());
            rsHistory.updateString("CUSTOMER_BANK",getAttribute("CUSTOMER_BANK").getString());
            rsHistory.insertRow();
            
            
            ResultSet rsTmp,rsTmpEx;
            Statement tmpStmt,tmpStmtEx;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='1'");
            
            int CreditCount=0,DebitCount=0;
            tmpStmtEx=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmpEx=tmpStmtEx.executeQuery("SELECT * FROM D_FIN_VOUCHER_DETAIL_EX WHERE VOUCHER_NO='1'");
            
            Statement stDetailDocs=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsDetailDocs=stDetailDocs.executeQuery("SELECT * FROM D_FIN_VOUCHER_DETAIL_DOCS LIMIT 1");
            
            Statement stDetailDocsH=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsDetailDocsH=stDetailDocsH.executeQuery("SELECT * FROM D_FIN_VOUCHER_DETAIL_DOCS_H LIMIT 1");
            
            int CompanyID=getAttribute("COMPANY_ID").getInt();
            
            //Now Insert records into detail table
            for(int i=1;i<=colVoucherItems.size();i++) {
                clsVoucherItem objItem=(clsVoucherItem) colVoucherItems.get(Integer.toString(i));
                
                if(objItem.getAttribute("EFFECT").getString().equals("C")) {
                    CreditCount++;
                } else {
                    DebitCount++;
                }
                
                rsTmp.moveToInsertRow();
                rsTmp.updateInt("COMPANY_ID",CompanyID);
                rsTmp.updateString("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
                rsTmp.updateInt("SR_NO",objItem.getAttribute("SR_NO").getInt());
                rsTmp.updateString("EFFECT",objItem.getAttribute("EFFECT").getString());
                rsTmp.updateInt("ACCOUNT_ID",objItem.getAttribute("ACCOUNT_ID").getInt());
                rsTmp.updateString("MAIN_ACCOUNT_CODE",objItem.getAttribute("MAIN_ACCOUNT_CODE").getString().trim());
                rsTmp.updateString("SUB_ACCOUNT_CODE",objItem.getAttribute("SUB_ACCOUNT_CODE").getString().trim());
                rsTmp.updateDouble("AMOUNT",objItem.getAttribute("AMOUNT").getDouble());
                rsTmp.updateDouble("APPLICABLE_AMOUNT",objItem.getAttribute("APPLICABLE_AMOUNT").getDouble());
                rsTmp.updateDouble("PERCENTAGE",objItem.getAttribute("PERCENTAGE").getDouble());
                rsTmp.updateString("REMARKS",objItem.getAttribute("REMARKS").getString());
                rsTmp.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsTmp.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsTmp.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateInt("CANCELLED",0);
                rsTmp.updateString("PO_NO",objItem.getAttribute("PO_NO").getString());
                rsTmp.updateString("PO_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("PO_DATE").getString()));
                rsTmp.updateString("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                rsTmp.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("INVOICE_DATE").getString()));
                rsTmp.updateString("VALUE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("VALUE_DATE").getString()));
                rsTmp.updateString("GRN_NO",objItem.getAttribute("GRN_NO").getString());
                rsTmp.updateString("GRN_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("GRN_DATE").getString()));
                rsTmp.updateInt("MODULE_ID",objItem.getAttribute("MODULE_ID").getInt());
                RefModuleID = objItem.getAttribute("MODULE_ID").getInt();
                rsTmp.updateDouble("INVOICE_AMOUNT",objItem.getAttribute("INVOICE_AMOUNT").getDouble());
                rsTmp.updateInt("IS_DEDUCTION",objItem.getAttribute("IS_DEDUCTION").getInt());
                rsTmp.updateInt("REF_COMPANY_ID",objItem.getAttribute("REF_COMPANY_ID").getInt());
                rsTmp.updateString("LINK_NO",objItem.getAttribute("LINK_NO").getString());
                rsTmp.updateInt("MATCHED",0);
                rsTmp.updateString("MATCHED_DATE","0000-00-00");
                try {
                    rsTmp.updateInt("REF_SR_NO",objItem.getAttribute("REF_SR_NO").getInt());
                }
                catch(Exception c){}
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateInt("COMPANY_ID",CompanyID);
                rsHDetail.updateString("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
                rsHDetail.updateInt("SR_NO",objItem.getAttribute("SR_NO").getInt());
                rsHDetail.updateString("EFFECT",objItem.getAttribute("EFFECT").getString());
                rsHDetail.updateInt("ACCOUNT_ID",objItem.getAttribute("ACCOUNT_ID").getInt());
                rsHDetail.updateString("MAIN_ACCOUNT_CODE",objItem.getAttribute("MAIN_ACCOUNT_CODE").getString().trim());
                rsHDetail.updateString("SUB_ACCOUNT_CODE",objItem.getAttribute("SUB_ACCOUNT_CODE").getString().trim());
                rsHDetail.updateDouble("AMOUNT",objItem.getAttribute("AMOUNT").getDouble());
                rsHDetail.updateDouble("APPLICABLE_AMOUNT",objItem.getAttribute("APPLICABLE_AMOUNT").getDouble());
                rsHDetail.updateDouble("PERCENTAGE",objItem.getAttribute("PERCENTAGE").getDouble());
                rsHDetail.updateString("REMARKS",objItem.getAttribute("REMARKS").getString());
                rsHDetail.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsHDetail.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsHDetail.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateInt("CANCELLED",0);
                rsHDetail.updateString("PO_NO",objItem.getAttribute("PO_NO").getString());
                rsHDetail.updateString("PO_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("PO_DATE").getString()));
                rsHDetail.updateString("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                rsHDetail.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("INVOICE_DATE").getString()));
                rsHDetail.updateString("VALUE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("VALUE_DATE").getString()));
                rsHDetail.updateString("GRN_NO",objItem.getAttribute("GRN_NO").getString());
                rsHDetail.updateString("GRN_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("GRN_DATE").getString()));
                rsHDetail.updateInt("MODULE_ID",objItem.getAttribute("MODULE_ID").getInt());
                rsHDetail.updateDouble("INVOICE_AMOUNT",objItem.getAttribute("INVOICE_AMOUNT").getDouble());
                rsHDetail.updateInt("IS_DEDUCTION",objItem.getAttribute("IS_DEDUCTION").getInt());
                rsHDetail.updateInt("REF_COMPANY_ID",objItem.getAttribute("REF_COMPANY_ID").getInt());
                rsHDetail.updateString("LINK_NO",objItem.getAttribute("LINK_NO").getString());
                rsHDetail.updateInt("MATCHED",0);
                rsHDetail.updateString("MATCHED_DATE","0000-00-00");
                try {
                    rsHDetail.updateInt("REF_SR_NO",objItem.getAttribute("REF_SR_NO").getInt());
                }
                catch(Exception c){}
                rsHDetail.insertRow();
                
                //************ Insert Deduction document nos. *******************//
                for(int j=1;j<=objItem.colVoucherDetailDocs.size();j++) {
                    clsVoucherItem objDetailDoc=(clsVoucherItem)objItem.colVoucherDetailDocs.get(Integer.toString(j));
                    
                    rsDetailDocs.moveToInsertRow();
                    rsDetailDocs.updateInt("COMPANY_ID",CompanyID);
                    rsDetailDocs.updateString("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
                    rsDetailDocs.updateInt("VOUCHER_SR_NO",i);
                    rsDetailDocs.updateInt("SR_NO",j);
                    rsDetailDocs.updateString("DOC_NO",objDetailDoc.getAttribute("DOC_NO").getString());
                    rsDetailDocs.updateString("DOC_DATE",EITLERPGLOBAL.formatDateDB(objDetailDoc.getAttribute("DOC_DATE").getString()));
                    rsDetailDocs.updateInt("DOC_TYPE", objDetailDoc.getAttribute("DOC_TYPE").getInt());
                    rsDetailDocs.updateDouble("AMOUNT",objDetailDoc.getAttribute("AMOUNT").getDouble());
                    rsDetailDocs.updateString("REMARKS",objDetailDoc.getAttribute("REMARKS").getString());
                    rsDetailDocs.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                    rsDetailDocs.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsDetailDocs.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                    rsDetailDocs.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsDetailDocs.updateBoolean("CHANGED",true);
                    rsDetailDocs.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsDetailDocs.updateBoolean("CANCELLED",false);
                    rsDetailDocs.insertRow();
                }
                //***************************************************************//
            }
            
            if ((VoucherType!=FinanceGlobal.TYPE_JOURNAL) && (VoucherType!=FinanceGlobal.TYPE_SALES_JOURNAL)) //Do not apply seperation logic for Journal Entries
            {
                //======================================================================================//
                // ******************** New Logic of Separation/Splitting Entries ********************* //
                //======================================================================================//
                String validEntryMessage="";
                
                validEntryMessage="Invalid Entry Sequence. Please specify valid sequence as example shown below";
                validEntryMessage+="\n1. Dr.  Ac. 1   50 ";
                validEntryMessage+="\n2. Dr.  Ac. 2   50 ";
                validEntryMessage+="\n3.      Cr.  Ac. 3   100 ";
                validEntryMessage+="\n4. Dr.  Ac. 3   20 ";
                validEntryMessage+="\n5.      Cr.  Ac. 5   20 ";
                
                BlockCounter=0;
                colVoucherItemsEx.clear();
                
                String CurrentEffect="",PreviousEffect="";
                double CrTotal=0,DrTotal=0;
                HashMap sEntries=new HashMap();
                
                for(int i=1;i<=colVoucherItems.size();i++) {
                    clsVoucherItem objItem=(clsVoucherItem)colVoucherItems.get(Integer.toString(i));
                    CurrentEffect=objItem.getAttribute("EFFECT").getString();
                    
                    if(sEntries.size()==0) {
                        sEntries.put(Integer.toString(sEntries.size()+1), objItem);
                        
                        if(CurrentEffect.equals("C")) {
                            CrTotal+=objItem.getAttribute("AMOUNT").getDouble();
                        } else {
                            DrTotal+=objItem.getAttribute("AMOUNT").getDouble();
                        }
                    } else {
                        if(!CurrentEffect.equals(PreviousEffect)) {
                            if(CurrentEffect.equals("C")) {
                                double CurrentAmount=EITLERPGLOBAL.round(objItem.getAttribute("AMOUNT").getDouble(),2);
                                
                                DrTotal=EITLERPGLOBAL.round(DrTotal,2);
                                
                                if(DrTotal!=CurrentAmount) {
                                    //Not a Valid Entry
                                    LastError=validEntryMessage;
                                    return false;
                                }
                                else {
                                    
                                    sEntries.put(Integer.toString(sEntries.size()+1),objItem);
                                    
                                    //PASS ON TO SPLIT ROUTINE. FROM HERE
                                    SplitEntries(sEntries);
                                    
                                    //Now Clear it
                                    sEntries.clear();
                                    CrTotal=0;DrTotal=0;
                                }
                            }
                            else {
                                
                                double CurrentAmount=EITLERPGLOBAL.round(objItem.getAttribute("AMOUNT").getDouble(),2);
                                
                                CrTotal=EITLERPGLOBAL.round(CrTotal,2);
                                
                                if(CrTotal!=CurrentAmount) {
                                    //Not a Valid Entry
                                    LastError=validEntryMessage;
                                    return false;
                                }
                                else {
                                    sEntries.put(Integer.toString(sEntries.size()+1),objItem);
                                    
                                    //PASS ON TO SPLIT ROUTINE. FROM HERE
                                    SplitEntries(sEntries);
                                    
                                    //Now Clear it
                                    sEntries.clear();
                                    CrTotal=0;DrTotal=0;
                                }
                                
                            }
                        }
                        else {
                            //STACK UP THE ENTRY
                            sEntries.put(Integer.toString(sEntries.size()+1),objItem);
                            
                            if(objItem.getAttribute("EFFECT").getString().equals("C")) {
                                CrTotal+=objItem.getAttribute("AMOUNT").getDouble();
                            }
                            else {
                                DrTotal+=objItem.getAttribute("AMOUNT").getDouble();
                            }
                        }
                    }
                    
                    PreviousEffect=objItem.getAttribute("EFFECT").getString();
                }
                
                
                
                for(int i=1;i<=colVoucherItemsEx.size();i++) {
                    clsVoucherItem objItem=(clsVoucherItem)colVoucherItemsEx.get(Integer.toString(i));
                    
                    rsTmpEx.moveToInsertRow();
                    rsTmpEx.updateInt("COMPANY_ID",CompanyID);
                    rsTmpEx.updateString("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
                    rsTmpEx.updateInt("SR_NO",i);
                    rsTmpEx.updateInt("BLOCK_NO",objItem.getAttribute("BLOCK_NO").getInt());
                    rsTmpEx.updateString("EFFECT",objItem.getAttribute("EFFECT").getString());
                    rsTmpEx.updateInt("ACCOUNT_ID",objItem.getAttribute("ACCOUNT_ID").getInt());
                    rsTmpEx.updateString("MAIN_ACCOUNT_CODE",objItem.getAttribute("MAIN_ACCOUNT_CODE").getString().trim());
                    rsTmpEx.updateString("SUB_ACCOUNT_CODE",objItem.getAttribute("SUB_ACCOUNT_CODE").getString().trim());
                    rsTmpEx.updateDouble("AMOUNT",objItem.getAttribute("AMOUNT").getDouble());
                    rsTmpEx.updateDouble("APPLICABLE_AMOUNT",objItem.getAttribute("APPLICABLE_AMOUNT").getDouble());
                    rsTmpEx.updateDouble("PERCENTAGE",objItem.getAttribute("PERCENTAGE").getDouble());
                    rsTmpEx.updateString("REMARKS",objItem.getAttribute("REMARKS").getString());
                    rsTmpEx.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                    rsTmpEx.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsTmpEx.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                    rsTmpEx.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsTmpEx.updateBoolean("CHANGED",true);
                    rsTmpEx.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsTmpEx.updateInt("CANCELLED",0);
                    rsTmpEx.updateString("PO_NO",objItem.getAttribute("PO_NO").getString());
                    rsTmpEx.updateString("PO_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("PO_DATE").getString()));
                    rsTmpEx.updateString("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                    rsTmpEx.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("INVOICE_DATE").getString()));
                    rsTmpEx.updateString("VALUE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("VALUE_DATE").getString()));
                    rsTmpEx.updateString("GRN_NO",objItem.getAttribute("GRN_NO").getString());
                    rsTmpEx.updateString("GRN_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("GRN_DATE").getString()));
                    rsTmpEx.updateInt("MODULE_ID",objItem.getAttribute("MODULE_ID").getInt());
                    rsTmpEx.updateDouble("INVOICE_AMOUNT",objItem.getAttribute("INVOICE_AMOUNT").getDouble());
                    rsTmpEx.updateInt("IS_DEDUCTION",objItem.getAttribute("IS_DEDUCTION").getInt());
                    rsTmpEx.updateInt("REF_COMPANY_ID",objItem.getAttribute("REF_COMPANY_ID").getInt());
                    rsTmpEx.updateString("LINK_NO",objItem.getAttribute("LINK_NO").getString());
                    rsTmpEx.updateInt("MATCHED",0);
                    rsTmpEx.updateString("MATCHED_DATE","0000-00-00");
                    try {
                        rsTmpEx.updateInt("REF_SR_NO",objItem.getAttribute("REF_SR_NO").getInt());
                    }
                    catch(Exception c){}
                    rsTmpEx.insertRow();
                }
                //======================================================================================//
                // ************************** End of Separation/Splitting Entries ********************* //
                //======================================================================================//
            }
            
            
            
            //********** Make entries as it is in case of Journal Voucher ***************//
            if ((VoucherType==FinanceGlobal.TYPE_JOURNAL) || (VoucherType==FinanceGlobal.TYPE_SALES_JOURNAL)) {
                for(int i=1;i<=colVoucherItems.size();i++) {
                    clsVoucherItem objItem=(clsVoucherItem)colVoucherItems.get(Integer.toString(i));
                    
                    rsTmpEx.moveToInsertRow();
                    rsTmpEx.updateInt("COMPANY_ID",CompanyID);
                    rsTmpEx.updateString("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
                    rsTmpEx.updateInt("SR_NO",i);
                    rsTmpEx.updateInt("BLOCK_NO",1); //Always one, as there is no seperation logic
                    rsTmpEx.updateString("EFFECT",objItem.getAttribute("EFFECT").getString());
                    rsTmpEx.updateInt("ACCOUNT_ID",objItem.getAttribute("ACCOUNT_ID").getInt());
                    rsTmpEx.updateString("MAIN_ACCOUNT_CODE",objItem.getAttribute("MAIN_ACCOUNT_CODE").getString().trim());
                    rsTmpEx.updateString("SUB_ACCOUNT_CODE",objItem.getAttribute("SUB_ACCOUNT_CODE").getString().trim());
                    rsTmpEx.updateDouble("AMOUNT",objItem.getAttribute("AMOUNT").getDouble());
                    rsTmpEx.updateDouble("APPLICABLE_AMOUNT",objItem.getAttribute("APPLICABLE_AMOUNT").getDouble());
                    rsTmpEx.updateDouble("PERCENTAGE",objItem.getAttribute("PERCENTAGE").getDouble());
                    rsTmpEx.updateString("REMARKS",objItem.getAttribute("REMARKS").getString());
                    rsTmpEx.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                    rsTmpEx.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsTmpEx.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                    rsTmpEx.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsTmpEx.updateBoolean("CHANGED",true);
                    rsTmpEx.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsTmpEx.updateInt("CANCELLED",0);
                    rsTmpEx.updateString("PO_NO",objItem.getAttribute("PO_NO").getString());
                    rsTmpEx.updateString("PO_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("PO_DATE").getString()));
                    rsTmpEx.updateString("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                    rsTmpEx.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("INVOICE_DATE").getString()));
                    rsTmpEx.updateString("VALUE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("VALUE_DATE").getString()));
                    rsTmpEx.updateString("GRN_NO",objItem.getAttribute("GRN_NO").getString());
                    rsTmpEx.updateString("GRN_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("GRN_DATE").getString()));
                    rsTmpEx.updateInt("MODULE_ID",objItem.getAttribute("MODULE_ID").getInt());
                    rsTmpEx.updateDouble("INVOICE_AMOUNT",objItem.getAttribute("INVOICE_AMOUNT").getDouble());
                    rsTmpEx.updateInt("IS_DEDUCTION",objItem.getAttribute("IS_DEDUCTION").getInt());
                    rsTmpEx.updateInt("REF_COMPANY_ID",objItem.getAttribute("REF_COMPANY_ID").getInt());
                    rsTmpEx.updateString("LINK_NO",objItem.getAttribute("LINK_NO").getString());
                    rsTmpEx.updateInt("MATCHED",0);
                    rsTmpEx.updateString("MATCHED_DATE","0000-00-00");
                    rsTmpEx.insertRow();
                }
            }
            //***************************************************************************//
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=getAttribute("COMPANY_ID").getInt();
            ObjFlow.ModuleID=getVoucherModuleID(getAttribute("VOUCHER_TYPE").getInt());
            ObjFlow.DocNo=getAttribute("VOUCHER_NO").getString();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_FIN_VOUCHER_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName="VOUCHER_NO";
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
            
            int VoucherType = data.getIntValueFromDB("SELECT VOUCHER_TYPE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+ObjFlow.DocNo+"' ",FinanceGlobal.FinURL);
            if(ObjFlow.Status.equals("F")) {
                String VoucherNo = ObjFlow.DocNo;
                VoucherType = data.getIntValueFromDB("SELECT VOUCHER_TYPE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+VoucherNo+"' ",FinanceGlobal.FinURL);
                if(VoucherType == FinanceGlobal.TYPE_RECEIPT || VoucherType == FinanceGlobal.TYPE_CASH_RECEIPT_VOUCHER || (VoucherType == FinanceGlobal.TYPE_JOURNAL && (!DepositTransfer))) {
                    String MainAccountCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo+"' AND EFFECT='C' AND SUB_ACCOUNT_CODE<>'' AND MAIN_ACCOUNT_CODE IN ('132642','132635','132714') ",FinanceGlobal.FinURL);
                    if(MainAccountCode.equals("132642") || MainAccountCode.equals("132635") || MainAccountCode.equals("132714")) { //MainAccountCode.equals("132666") ||
                        if(!PostReceipt(VoucherNo,MainAccountCode)) {
                            LastError += "Dealer Deposit Receipt Not Posted Properly...";
                            return false;
                        }
                    }
                    MainAccountCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo+"' AND EFFECT='C' AND SUB_ACCOUNT_CODE<>'' AND MAIN_ACCOUNT_CODE IN ('210010','210027') ",FinanceGlobal.FinURL);
                    if(MainAccountCode.equals("210027") || MainAccountCode.equals("210010")) {
                        PostDebitNote(VoucherNo);
                    }
                }
            }
            
            if(VoucherType == FinanceGlobal.TYPE_CASH_RECEIPT_VOUCHER) {
                String currentVoucherNo = ObjFlow.DocNo.trim();
                generateNextLegacyNo(currentVoucherNo);
            }
            
            if(!TVoucherNo.equals("") && !EmployeeNo.equals("")) {
                clsTravelExp.Insert(TVoucherNo,EmployeeNo);
            }
            
            //*************************************************************************//
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
            }
            
            //MoveLast();
            return true;
        }
        catch(Exception e) {
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
        ResultSet rsHistory,rsHDetail,rsHeader,rsTmp;
        boolean Validate=true;
        int OldHierarchy=0;
        
        try {
            
            if(!Validate(EITLERPGLOBAL.EDIT)) {
                if(getAttribute("APPROVAL_STATUS").equals("R")) {
                    //Do allow to go ahead with rejection
                }
                else {
                    
                    if(!getAttribute("APPROVAL_STATUS").equals("F")) {
                        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 1052, 10527) ) {
                            //User has rights to skip over validation
                        }
                        else {
                            return false;
                        }
                    }
                    else {
                        return false;
                    }
                    
                }
            }
            
            if( getAttribute("VOUCHER_TYPE").getInt()==FinanceGlobal.TYPE_JOURNAL ||
            getAttribute("VOUCHER_TYPE").getInt()==FinanceGlobal.TYPE_CASH_VOUCHER ||
            getAttribute("VOUCHER_TYPE").getInt()==FinanceGlobal.TYPE_CASH_RECEIPT_VOUCHER) {
                TVoucherNo = getAttribute("VOUCHER_NO").getString();
                EmployeeNo = getAttribute("EMPLOYEE_NO").getString();
            }
            
            //Check that Voucher no. has been changed
            rsResultSet.updateString("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
            rsResultSet.updateString("VOUCHER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("VOUCHER_DATE").getString()));
            rsResultSet.updateString("BOOK_CODE",getAttribute("BOOK_CODE").getString());
            rsResultSet.updateString("CHEQUE_NO",getAttribute("CHEQUE_NO").getString());
            rsResultSet.updateString("CHEQUE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CHEQUE_DATE").getString()));
            rsResultSet.updateString("REALIZATION_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("REALIZATION_DATE").getString()));
            rsResultSet.updateString("BANK_NAME",getAttribute("BANK_NAME").getString());
            rsResultSet.updateString("ST_CATEGORY",getAttribute("ST_CATEGORY").getString());
            rsResultSet.updateString("REASON_CODE",getAttribute("REASON_CODE").getString());
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateString("CREATED_BY",getAttribute("CREATED_BY").getString()); //EITLERPGLOBAL.gLoginID
            rsResultSet.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsResultSet.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
            rsResultSet.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateDouble("CHEQUE_AMOUNT",getAttribute("CHEQUE_AMOUNT").getDouble());
            rsResultSet.updateInt("PAYMENT_MODE",getAttribute("PAYMENT_MODE").getInt());
            rsResultSet.updateString("LEGACY_NO",getAttribute("LEGACY_NO").getString());
            rsResultSet.updateString("LINK_NO",getAttribute("LINK_NO").getString());
            rsResultSet.updateString("LEGACY_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("LEGACY_DATE").getString()));
            rsResultSet.updateInt("EXCLUDE_IN_ADJ",getAttribute("EXCLUDE_IN_ADJ").getInt());
            rsResultSet.updateString("CUSTOMER_BANK",getAttribute("CUSTOMER_BANK").getString());
            rsResultSet.updateRow();
            
            String VoucherNo=getAttribute("VOUCHER_NO").getString();
            if(ChangeVoucherNo&&(!VoucherNo.equals(OldVoucherNo))) {
                //Change Voucher No. in all Tables
                
                int VoucherModuleID=getVoucherModuleID(getAttribute("VOUCHER_TYPE").getInt());
                
                data.Execute("UPDATE D_FIN_VOUCHER_HEADER SET VOUCHER_NO='"+VoucherNo+"' WHERE VOUCHER_NO='"+OldVoucherNo+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID,FinanceGlobal.FinURL);
                data.Execute("UPDATE D_FIN_VOUCHER_HEADER_H SET VOUCHER_NO='"+VoucherNo+"' WHERE VOUCHER_NO='"+OldVoucherNo+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID,FinanceGlobal.FinURL);
                data.Execute("UPDATE D_FIN_VOUCHER_DETAIL SET VOUCHER_NO='"+VoucherNo+"' WHERE VOUCHER_NO='"+OldVoucherNo+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID,FinanceGlobal.FinURL);
                data.Execute("UPDATE D_FIN_VOUCHER_DETAIL_H SET VOUCHER_NO='"+VoucherNo+"' WHERE VOUCHER_NO='"+OldVoucherNo+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID,FinanceGlobal.FinURL);
                data.Execute("UPDATE D_FIN_VOUCHER_DETAIL_EX SET VOUCHER_NO='"+VoucherNo+"' WHERE VOUCHER_NO='"+OldVoucherNo+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID,FinanceGlobal.FinURL);
                data.Execute("UPDATE D_FIN_VOUCHER_DETAIL_DOCS SET VOUCHER_NO='"+VoucherNo+"' WHERE VOUCHER_NO='"+OldVoucherNo+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID,FinanceGlobal.FinURL);
                data.Execute("UPDATE D_FIN_VOUCHER_DETAIL_DOCS_H SET VOUCHER_NO='"+VoucherNo+"' WHERE VOUCHER_NO='"+OldVoucherNo+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID,FinanceGlobal.FinURL);
                data.Execute("UPDATE D_COM_DOC_DATA SET DOC_NO='"+VoucherNo+"' WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+VoucherModuleID+" AND DOC_NO='"+OldVoucherNo+"'");
                
                
                //* If its receipt voucher
                //We need to update references of this receipt voucher in LC JV Entries//
                
                
                if(getAttribute("VOUCHER_TYPE").getInt()==FinanceGlobal.TYPE_RECEIPT) {
                    data.Execute("UPDATE D_FIN_VOUCHER_DETAIL SET GRN_NO='"+VoucherNo+"',GRN_DATE='"+EITLERPGLOBAL.formatDateDB(getAttribute("VOUCHER_DATE").getString())+"' WHERE GRN_NO='"+OldVoucherNo+"' AND MODULE_ID="+getVoucherModuleID(FinanceGlobal.TYPE_RECEIPT),FinanceGlobal.FinURL);
                    data.Execute("UPDATE D_FIN_VOUCHER_DETAIL_EX SET GRN_NO='"+VoucherNo+"',GRN_DATE='"+EITLERPGLOBAL.formatDateDB(getAttribute("VOUCHER_DATE").getString())+"' WHERE GRN_NO='"+OldVoucherNo+"' AND MODULE_ID="+getVoucherModuleID(FinanceGlobal.TYPE_RECEIPT),FinanceGlobal.FinURL);
                }
                
                
            }
            
            String AStatus=getAttribute("APPROVAL_STATUS").getString();
            String theDocNo=getAttribute("VOUCHER_NO").getString();
            
            if(AStatus.equals("R")) {
                Validate=false;
            }
            
            Validate=true;
            
            
            rsTmp=data.getResult("SELECT HIERARCHY_ID FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+theDocNo+"'",FinanceGlobal.FinURL);
            if(rsTmp.getRow()>0) {
                OldHierarchy=rsTmp.getInt("HIERARCHY_ID");
            }
            
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date ReqDate=java.sql.Date.valueOf( EITLERPGLOBAL.formatDateDB(getAttribute("VOUCHER_DATE").getString()));
            
            if((ReqDate.after(FinFromDate)||ReqDate.compareTo(FinFromDate)==0)&&(ReqDate.before(FinToDate)||ReqDate.compareTo(FinToDate)==0)) {
                //Withing the year
            } else {
                LastError="Voucher date is not within financial year.";
                //return false;
            }
            //=====================================================//
            
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FIN_VOUCHER_HEADER_H WHERE VOUCHER_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FIN_VOUCHER_DETAIL_H WHERE VOUCHER_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM D_FIN_VOUCHER_HEADER_H WHERE COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+" AND VOUCHER_NO='"+getAttribute("VOUCHER_NO").getString()+"'",FinanceGlobal.FinURL);
            RevNo++;
            String RevDocNo=getAttribute("VOUCHER_NO").getString();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getLong());
            rsHistory.updateString("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
            rsHistory.updateString("VOUCHER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("VOUCHER_DATE").getString()));
            rsHistory.updateInt("VOUCHER_TYPE",getAttribute("VOUCHER_TYPE").getInt());
            rsHistory.updateString("BOOK_CODE",getAttribute("BOOK_CODE").getString());
            rsHistory.updateString("CHEQUE_NO",getAttribute("CHEQUE_NO").getString());
            rsHistory.updateString("CHEQUE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CHEQUE_DATE").getString()));
            rsHistory.updateString("REALIZATION_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("REALIZATION_DATE").getString()));
            rsHistory.updateString("BANK_NAME",getAttribute("BANK_NAME").getString());
            rsHistory.updateString("ST_CATEGORY",getAttribute("ST_CATEGORY").getString());
            rsHistory.updateString("REASON_CODE",getAttribute("REASON_CODE").getString());
            rsHistory.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            
            rsHistory.updateString("CREATED_BY",getAttribute("CREATED_BY").getString()); //EITLERPGLOBAL.gLoginID
            rsHistory.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsHistory.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
            rsHistory.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.updateDouble("CHEQUE_AMOUNT",getAttribute("CHEQUE_AMOUNT").getDouble());
            rsHistory.updateInt("PAYMENT_MODE",getAttribute("PAYMENT_MODE").getInt());
            rsHistory.updateString("LEGACY_NO",getAttribute("LEGACY_NO").getString());
            rsHistory.updateString("LINK_NO",getAttribute("LINK_NO").getString());
            rsHistory.updateString("LEGACY_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("LEGACY_DATE").getString()));
            rsHistory.updateInt("EXCLUDE_IN_ADJ",getAttribute("EXCLUDE_IN_ADJ").getInt());
            rsHistory.updateString("CUSTOMER_BANK",getAttribute("CUSTOMER_BANK").getString());
            rsHistory.insertRow();
            
            data.Execute("DELETE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+" AND VOUCHER_NO='"+theDocNo+"'",FinanceGlobal.FinURL);
            data.Execute("DELETE FROM D_FIN_VOUCHER_DETAIL_EX WHERE COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+" AND VOUCHER_NO='"+theDocNo+"'",FinanceGlobal.FinURL);
            data.Execute("DELETE FROM D_FIN_VOUCHER_DETAIL_DOCS WHERE COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+" AND VOUCHER_NO='"+theDocNo+"'",FinanceGlobal.FinURL);
            
            
            ResultSet rsTmpEx;
            Statement tmpStmt,tmpStmtEx;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='1'");
            
            int CreditCount=0,DebitCount=0;
            tmpStmtEx=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmpEx=tmpStmtEx.executeQuery("SELECT * FROM D_FIN_VOUCHER_DETAIL_EX WHERE VOUCHER_NO='1'");
            
            Statement stDetailDocs=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsDetailDocs=stDetailDocs.executeQuery("SELECT * FROM D_FIN_VOUCHER_DETAIL_DOCS LIMIT 1");
            
            Statement stDetailDocsH=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsDetailDocsH=stDetailDocsH.executeQuery("SELECT * FROM D_FIN_VOUCHER_DETAIL_DOCS_H LIMIT 1");
            
            int Counter=0;
            
            //Now Insert records into detail table
            for(int i=1;i<=colVoucherItems.size();i++) {
                clsVoucherItem objItem=(clsVoucherItem) colVoucherItems.get(Integer.toString(i));
                
                if(objItem.getAttribute("EFFECT").getString().equals("C")) {
                    CreditCount++;
                }
                else {
                    DebitCount++;
                }
                
                Counter++;
                
                rsTmp.moveToInsertRow();
                rsTmp.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                rsTmp.updateString("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
                rsTmp.updateInt("SR_NO",objItem.getAttribute("SR_NO").getInt());
                rsTmp.updateString("EFFECT",objItem.getAttribute("EFFECT").getString());
                rsTmp.updateInt("ACCOUNT_ID",objItem.getAttribute("ACCOUNT_ID").getInt());
                
                
                rsTmp.updateString("MAIN_ACCOUNT_CODE",objItem.getAttribute("MAIN_ACCOUNT_CODE").getString().trim());
                rsTmp.updateString("SUB_ACCOUNT_CODE",objItem.getAttribute("SUB_ACCOUNT_CODE").getString().trim());
                rsTmp.updateDouble("AMOUNT",objItem.getAttribute("AMOUNT").getDouble());
                rsTmp.updateDouble("APPLICABLE_AMOUNT",objItem.getAttribute("APPLICABLE_AMOUNT").getDouble());
                rsTmp.updateDouble("PERCENTAGE",objItem.getAttribute("PERCENTAGE").getDouble());
                rsTmp.updateString("REMARKS",objItem.getAttribute("REMARKS").getString());
                rsTmp.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsTmp.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsTmp.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateInt("CANCELLED",0);
                
                rsTmp.updateString("PO_NO",objItem.getAttribute("PO_NO").getString());
                rsTmp.updateString("PO_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("PO_DATE").getString()));
                rsTmp.updateString("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                rsTmp.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("INVOICE_DATE").getString()));
                rsTmp.updateString("VALUE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("VALUE_DATE").getString()));
                rsTmp.updateString("GRN_NO",objItem.getAttribute("GRN_NO").getString());
                rsTmp.updateString("GRN_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("GRN_DATE").getString()));
                rsTmp.updateInt("MODULE_ID",objItem.getAttribute("MODULE_ID").getInt());
                rsTmp.updateDouble("INVOICE_AMOUNT",objItem.getAttribute("INVOICE_AMOUNT").getDouble());
                rsTmp.updateInt("IS_DEDUCTION",objItem.getAttribute("IS_DEDUCTION").getInt());
                rsTmp.updateInt("REF_COMPANY_ID",objItem.getAttribute("REF_COMPANY_ID").getInt());
                rsTmp.updateString("LINK_NO",objItem.getAttribute("LINK_NO").getString());
                rsTmp.updateInt("MATCHED",0);
                rsTmp.updateString("MATCHED_DATE","0000-00-00");
                try {
                    rsTmp.updateInt("REF_SR_NO",objItem.getAttribute("REF_SR_NO").getInt());
                    
                }
                catch(Exception c){}
                rsTmp.insertRow();
                
                Counter++;
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                rsHDetail.updateString("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
                rsHDetail.updateInt("SR_NO",objItem.getAttribute("SR_NO").getInt());
                rsHDetail.updateString("EFFECT",objItem.getAttribute("EFFECT").getString());
                rsHDetail.updateInt("ACCOUNT_ID",objItem.getAttribute("ACCOUNT_ID").getInt());
                rsHDetail.updateString("MAIN_ACCOUNT_CODE",objItem.getAttribute("MAIN_ACCOUNT_CODE").getString().trim());
                rsHDetail.updateString("SUB_ACCOUNT_CODE",objItem.getAttribute("SUB_ACCOUNT_CODE").getString().trim());
                rsHDetail.updateDouble("AMOUNT",objItem.getAttribute("AMOUNT").getDouble());
                rsHDetail.updateDouble("APPLICABLE_AMOUNT",objItem.getAttribute("APPLICABLE_AMOUNT").getDouble());
                rsHDetail.updateDouble("PERCENTAGE",objItem.getAttribute("PERCENTAGE").getDouble());
                rsHDetail.updateString("REMARKS",objItem.getAttribute("REMARKS").getString());
                rsHDetail.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsHDetail.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsHDetail.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateInt("CANCELLED",0);
                
                rsHDetail.updateString("PO_NO",objItem.getAttribute("PO_NO").getString());
                rsHDetail.updateString("PO_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("PO_DATE").getString()));
                rsHDetail.updateString("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                rsHDetail.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("INVOICE_DATE").getString()));
                rsHDetail.updateString("VALUE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("VALUE_DATE").getString()));
                rsHDetail.updateString("GRN_NO",objItem.getAttribute("GRN_NO").getString());
                rsHDetail.updateString("GRN_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("GRN_DATE").getString()));
                rsHDetail.updateInt("MODULE_ID",objItem.getAttribute("MODULE_ID").getInt());
                rsHDetail.updateDouble("INVOICE_AMOUNT",objItem.getAttribute("INVOICE_AMOUNT").getDouble());
                rsHDetail.updateInt("IS_DEDUCTION",objItem.getAttribute("IS_DEDUCTION").getInt());
                rsHDetail.updateInt("REF_COMPANY_ID",objItem.getAttribute("REF_COMPANY_ID").getInt());
                rsHDetail.updateString("LINK_NO",objItem.getAttribute("LINK_NO").getString());
                rsHDetail.updateInt("MATCHED",0);
                rsHDetail.updateString("MATCHED_DATE","0000-00-00");
                try {
                    rsHDetail.updateInt("REF_SR_NO",objItem.getAttribute("REF_SR_NO").getInt());
                }
                catch(Exception c){}
                rsHDetail.insertRow();
                
                
                
                //************ Insert Deduction document nos. *******************//
                for(int j=1;j<=objItem.colVoucherDetailDocs.size();j++) {
                    clsVoucherItem objDetailDoc=(clsVoucherItem)objItem.colVoucherDetailDocs.get(Integer.toString(j));
                    
                    rsDetailDocs.moveToInsertRow();
                    rsDetailDocs.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                    rsDetailDocs.updateString("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
                    rsDetailDocs.updateInt("VOUCHER_SR_NO",i);
                    rsDetailDocs.updateInt("SR_NO",j);
                    rsDetailDocs.updateString("DOC_NO",objDetailDoc.getAttribute("DOC_NO").getString());
                    rsDetailDocs.updateString("DOC_DATE",EITLERPGLOBAL.formatDateDB(objDetailDoc.getAttribute("DOC_DATE").getString()));
                    rsDetailDocs.updateInt("DOC_TYPE", objDetailDoc.getAttribute("DOC_TYPE").getInt());
                    rsDetailDocs.updateDouble("AMOUNT",objDetailDoc.getAttribute("AMOUNT").getDouble());
                    rsDetailDocs.updateString("REMARKS",objDetailDoc.getAttribute("REMARKS").getString());
                    rsDetailDocs.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                    rsDetailDocs.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsDetailDocs.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                    rsDetailDocs.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsDetailDocs.updateBoolean("CHANGED",true);
                    rsDetailDocs.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsDetailDocs.updateBoolean("CANCELLED",false);
                    rsDetailDocs.insertRow();
                }
                //***************************************************************//
                
            }
            
            if ((VoucherType!=FinanceGlobal.TYPE_JOURNAL) && (VoucherType!=FinanceGlobal.TYPE_SALES_JOURNAL) && (VoucherType!=FinanceGlobal.TYPE_CREDIT_NOTE)) {
                //======================================================================================//
                // ******************** New Logic of Separation/Splitting Entries ********************* //
                //======================================================================================//
                String validEntryMessage="";
                
                validEntryMessage="Invalid Entry Sequence. Please specify valid sequence as example shown below";
                validEntryMessage+="\n1. Dr.  Ac. 1   50 ";
                validEntryMessage+="\n2. Dr.  Ac. 2   50 ";
                validEntryMessage+="\n3.      Cr.  Ac. 3   100 ";
                validEntryMessage+="\n4. Dr.  Ac. 3   20 ";
                validEntryMessage+="\n5.      Cr.  Ac. 5   20 ";
                
                BlockCounter=0;
                colVoucherItemsEx.clear();
                
                String CurrentEffect="",PreviousEffect="";
                double CrTotal=0,DrTotal=0;
                HashMap sEntries=new HashMap();
                
                for(int i=1;i<=colVoucherItems.size();i++) {
                    clsVoucherItem objItem=(clsVoucherItem)colVoucherItems.get(Integer.toString(i));
                    CurrentEffect=objItem.getAttribute("EFFECT").getString();
                    
                    if(sEntries.size()==0) {
                        sEntries.put(Integer.toString(sEntries.size()+1), objItem);
                        
                        if(CurrentEffect.equals("C")) {
                            CrTotal+=objItem.getAttribute("AMOUNT").getDouble();
                        } else {
                            DrTotal+=objItem.getAttribute("AMOUNT").getDouble();
                        }
                        
                    }
                    else {
                        if(!CurrentEffect.equals(PreviousEffect)) {
                            if(CurrentEffect.equals("C")) {
                                double CurrentAmount=EITLERPGLOBAL.round(objItem.getAttribute("AMOUNT").getDouble(),2);
                                
                                DrTotal=EITLERPGLOBAL.round(DrTotal,2);
                                
                                if(DrTotal!=CurrentAmount) {
                                    //Not a Valid Entry
                                    LastError=validEntryMessage;
                                    return false;
                                } else {
                                    sEntries.put(Integer.toString(sEntries.size()+1),objItem);
                                    
                                    //PASS ON TO SPLIT ROUTINE. FROM HERE
                                    SplitEntries(sEntries);
                                    
                                    //Now Clear it
                                    sEntries.clear();
                                    CrTotal=0;DrTotal=0;
                                }
                            } else {
                                
                                double CurrentAmount=EITLERPGLOBAL.round(objItem.getAttribute("AMOUNT").getDouble(),2);
                                
                                CrTotal=EITLERPGLOBAL.round(CrTotal,2);
                                
                                if(CrTotal!=CurrentAmount) {
                                    //Not a Valid Entry
                                    LastError=validEntryMessage;
                                    return false;
                                } else {
                                    sEntries.put(Integer.toString(sEntries.size()+1),objItem);
                                    
                                    //PASS ON TO SPLIT ROUTINE. FROM HERE
                                    SplitEntries(sEntries);
                                    
                                    //Now Clear it
                                    sEntries.clear();
                                    CrTotal=0;DrTotal=0;
                                }
                            }
                        }
                        else {
                            //STACK UP THE ENTRY
                            sEntries.put(Integer.toString(sEntries.size()+1),objItem);
                            
                            if(objItem.getAttribute("EFFECT").getString().equals("C")) {
                                CrTotal+=objItem.getAttribute("AMOUNT").getDouble();
                            }
                            else {
                                DrTotal+=objItem.getAttribute("AMOUNT").getDouble();
                            }
                        }
                    }
                    
                    PreviousEffect=objItem.getAttribute("EFFECT").getString();
                }
                
                for(int i=1;i<=colVoucherItemsEx.size();i++) {
                    clsVoucherItem objItem=(clsVoucherItem)colVoucherItemsEx.get(Integer.toString(i));
                    
                    rsTmpEx.moveToInsertRow();
                    rsTmpEx.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                    rsTmpEx.updateString("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
                    rsTmpEx.updateInt("SR_NO",i);
                    rsTmpEx.updateInt("BLOCK_NO",objItem.getAttribute("BLOCK_NO").getInt());
                    rsTmpEx.updateString("EFFECT",objItem.getAttribute("EFFECT").getString());
                    rsTmpEx.updateInt("ACCOUNT_ID",objItem.getAttribute("ACCOUNT_ID").getInt());
                    rsTmpEx.updateString("MAIN_ACCOUNT_CODE",objItem.getAttribute("MAIN_ACCOUNT_CODE").getString().trim());
                    rsTmpEx.updateString("SUB_ACCOUNT_CODE",objItem.getAttribute("SUB_ACCOUNT_CODE").getString().trim());
                    rsTmpEx.updateDouble("AMOUNT",objItem.getAttribute("AMOUNT").getDouble());
                    rsTmpEx.updateDouble("APPLICABLE_AMOUNT",objItem.getAttribute("APPLICABLE_AMOUNT").getDouble());
                    rsTmpEx.updateDouble("PERCENTAGE",objItem.getAttribute("PERCENTAGE").getDouble());
                    rsTmpEx.updateString("REMARKS",objItem.getAttribute("REMARKS").getString());
                    rsTmpEx.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                    rsTmpEx.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsTmpEx.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                    rsTmpEx.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsTmpEx.updateBoolean("CHANGED",true);
                    rsTmpEx.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsTmpEx.updateInt("CANCELLED",0);
                    rsTmpEx.updateString("PO_NO",objItem.getAttribute("PO_NO").getString());
                    rsTmpEx.updateString("PO_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("PO_DATE").getString()));
                    rsTmpEx.updateString("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                    rsTmpEx.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("INVOICE_DATE").getString()));
                    rsTmpEx.updateString("VALUE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("VALUE_DATE").getString()));
                    rsTmpEx.updateString("GRN_NO",objItem.getAttribute("GRN_NO").getString());
                    rsTmpEx.updateString("GRN_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("GRN_DATE").getString()));
                    rsTmpEx.updateInt("MODULE_ID",objItem.getAttribute("MODULE_ID").getInt());
                    rsTmpEx.updateDouble("INVOICE_AMOUNT",objItem.getAttribute("INVOICE_AMOUNT").getDouble());
                    rsTmpEx.updateInt("IS_DEDUCTION",objItem.getAttribute("IS_DEDUCTION").getInt());
                    rsTmpEx.updateInt("REF_COMPANY_ID",objItem.getAttribute("REF_COMPANY_ID").getInt());
                    rsTmpEx.updateString("LINK_NO",objItem.getAttribute("LINK_NO").getString());
                    rsTmpEx.updateInt("MATCHED",0);
                    rsTmpEx.updateString("MATCHED_DATE","0000-00-00");
                    try {
                        rsTmpEx.updateInt("REF_SR_NO",objItem.getAttribute("REF_SR_NO").getInt());
                    }
                    catch(Exception c) {
                    }
                    rsTmpEx.insertRow();
                }
                
                //======================================================================================//
                // ************************** End of Separation/Splitting Entries ********************* //
                //======================================================================================//
                
            }
            
            //********** Make entries as it is in case of Journal Voucher ***************//
            if ((VoucherType==FinanceGlobal.TYPE_JOURNAL) || (VoucherType==FinanceGlobal.TYPE_SALES_JOURNAL) || (VoucherType==FinanceGlobal.TYPE_CREDIT_NOTE)) {
                String TVoucher = "";
                for(int i=1;i<=colVoucherItems.size();i++) {
                    clsVoucherItem objItem=(clsVoucherItem)colVoucherItems.get(Integer.toString(i));
                    
                    rsTmpEx.moveToInsertRow();
                    rsTmpEx.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                    rsTmpEx.updateString("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
                    TVoucher = getAttribute("VOUCHER_NO").getString();
                    rsTmpEx.updateInt("SR_NO",i);
                    rsTmpEx.updateInt("BLOCK_NO",1); //Always one, as there is no seperation logic
                    rsTmpEx.updateString("EFFECT",objItem.getAttribute("EFFECT").getString());
                    rsTmpEx.updateInt("ACCOUNT_ID",objItem.getAttribute("ACCOUNT_ID").getInt());
                    rsTmpEx.updateString("MAIN_ACCOUNT_CODE",objItem.getAttribute("MAIN_ACCOUNT_CODE").getString().trim());
                    rsTmpEx.updateString("SUB_ACCOUNT_CODE",objItem.getAttribute("SUB_ACCOUNT_CODE").getString().trim());
                    rsTmpEx.updateDouble("AMOUNT",objItem.getAttribute("AMOUNT").getDouble());
                    rsTmpEx.updateDouble("APPLICABLE_AMOUNT",objItem.getAttribute("APPLICABLE_AMOUNT").getDouble());
                    rsTmpEx.updateDouble("PERCENTAGE",objItem.getAttribute("PERCENTAGE").getDouble());
                    rsTmpEx.updateString("REMARKS",objItem.getAttribute("REMARKS").getString());
                    rsTmpEx.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                    rsTmpEx.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsTmpEx.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                    rsTmpEx.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsTmpEx.updateBoolean("CHANGED",true);
                    rsTmpEx.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsTmpEx.updateInt("CANCELLED",0);
                    rsTmpEx.updateString("PO_NO",objItem.getAttribute("PO_NO").getString());
                    rsTmpEx.updateString("PO_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("PO_DATE").getString()));
                    rsTmpEx.updateString("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                    rsTmpEx.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("INVOICE_DATE").getString()));
                    rsTmpEx.updateString("VALUE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("VALUE_DATE").getString()));
                    rsTmpEx.updateString("GRN_NO",objItem.getAttribute("GRN_NO").getString());
                    rsTmpEx.updateString("GRN_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("GRN_DATE").getString()));
                    rsTmpEx.updateInt("MODULE_ID",objItem.getAttribute("MODULE_ID").getInt());
                    rsTmpEx.updateDouble("INVOICE_AMOUNT",objItem.getAttribute("INVOICE_AMOUNT").getDouble());
                    rsTmpEx.updateInt("IS_DEDUCTION",objItem.getAttribute("IS_DEDUCTION").getInt());
                    rsTmpEx.updateInt("REF_COMPANY_ID",objItem.getAttribute("REF_COMPANY_ID").getInt());
                    rsTmpEx.updateString("LINK_NO",objItem.getAttribute("LINK_NO").getString());
                    rsTmpEx.updateInt("MATCHED",0);
                    rsTmpEx.updateString("MATCHED_DATE","0000-00-00");
                    rsTmpEx.insertRow();
                }
            }
            //***************************************************************************//
            
            ObjFlow.CompanyID=getAttribute("COMPANY_ID").getInt();
            ObjFlow.ModuleID=getVoucherModuleID(getAttribute("VOUCHER_TYPE").getInt());
            
            if(ApprovalFlow.HierarchyUpdateNeeded(EITLERPGLOBAL.gCompanyID, ObjFlow.ModuleID, getAttribute("VOUCHER_NO").getString(),getAttribute("HIERARCHY_ID").getInt(),OldHierarchy,EITLERPGLOBAL.gNewUserID,AStatus)) {
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+getAttribute("VOUCHER_NO").getString()+"' AND MODULE_ID="+ObjFlow.ModuleID);
                
                ObjFlow.DocNo=getAttribute("VOUCHER_NO").getString();
                ObjFlow.DocDate=EITLERPGLOBAL.formatDateDB(getAttribute("VOUCHER_NO").getString());
                ObjFlow.From=(int)getAttribute("FROM").getVal();
                ObjFlow.To=(int)getAttribute("TO").getVal();
                ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
                ObjFlow.TableName="D_FIN_VOUCHER_HEADER";
                ObjFlow.IsCreator=true;
                ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
                ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
                ObjFlow.FieldName="VOUCHER_NO";
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
            } else {
                
                //======== Update the Approval Flow =========
                ObjFlow.DocNo=getAttribute("VOUCHER_NO").getString();
                ObjFlow.From=getAttribute("FROM").getInt();
                ObjFlow.To=getAttribute("TO").getInt();
                ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
                ObjFlow.TableName="D_FIN_VOUCHER_HEADER";
                ObjFlow.IsCreator=false;
                ObjFlow.HierarchyID=getAttribute("HIERARCHY_ID").getInt();
                ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
                ObjFlow.FieldName="VOUCHER_NO";
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
                    data.Execute("UPDATE D_FIN_VOUCHER_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+" AND VOUCHER_NO='"+ObjFlow.DocNo+"'",FinanceGlobal.FinURL);
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
                } else {
                    if(!ObjFlow.UpdateFlow()) {
                        LastError=ObjFlow.LastError;
                    }
                }
                ObjFlow.UseSpecifiedURL=false;
                //=========== Approval Flow Update complete ===============//
            }
            String currentVoucherNo = ObjFlow.DocNo;
            if(ObjFlow.Status.equals("F")) {
                int VoucherType = data.getIntValueFromDB("SELECT VOUCHER_TYPE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+currentVoucherNo+"' ",FinanceGlobal.FinURL);
                if(VoucherType == FinanceGlobal.TYPE_RECEIPT || VoucherType == FinanceGlobal.TYPE_CASH_RECEIPT_VOUCHER || (VoucherType == FinanceGlobal.TYPE_JOURNAL && (!DepositTransfer))) {
                    String MainAccountCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+currentVoucherNo+"' AND EFFECT='C' AND SUB_ACCOUNT_CODE<>'' AND MAIN_ACCOUNT_CODE IN ('132642','132635','132714') ",FinanceGlobal.FinURL);
                    if(MainAccountCode.equals("132642") || MainAccountCode.equals("132635") || MainAccountCode.equals("132714")) { //MainAccountCode.equals("132666") ||
                        if(!PostReceipt(currentVoucherNo,MainAccountCode)) {
                            LastError += "Dealer Deposit Receipt Not Posted Properly...";
                            return false;
                        }
                    }
                    MainAccountCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+currentVoucherNo+"' AND EFFECT='C' AND SUB_ACCOUNT_CODE<>'' AND MAIN_ACCOUNT_CODE IN ('210010','210027') ",FinanceGlobal.FinURL);
                    if(MainAccountCode.equals("210027") || MainAccountCode.equals("210010")) {
                        PostDebitNote(currentVoucherNo);
                    }
                }
                
                if(VoucherType == FinanceGlobal.TYPE_LC_JV || VoucherType == FinanceGlobal.TYPE_JOURNAL || VoucherType == FinanceGlobal.TYPE_PAYMENT || VoucherType == FinanceGlobal.TYPE_PAYMENT_2) {
                    ResultSet rsData = data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+currentVoucherNo+"' AND EFFECT='D' AND MAIN_ACCOUNT_CODE IN ('210027','210010','210072') AND GRN_NO<>'' ",FinanceGlobal.FinURL);
                    if(rsData.getRow() > 0 ) {
                        while(!rsData.isAfterLast()) {
                            String MainCode = rsData.getString("MAIN_ACCOUNT_CODE");
                            String SubCode = rsData.getString("SUB_ACCOUNT_CODE");
                            String GRNNo = rsData.getString("GRN_NO");
                            double AdjustAmount = rsData.getDouble("AMOUNT");
                            while(AdjustAmount>0) {
                                if(clsVoucher.getVoucherType(GRNNo) > 0) {
                                    int GRNVoucherType = clsVoucher.getVoucherType(GRNNo);
                                    String strSQL = "SELECT B.SR_NO FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B " +
                                    "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND B.MAIN_ACCOUNT_CODE = '"+MainCode+"' " +
                                    "AND B.SUB_ACCOUNT_CODE='"+SubCode+"' AND A.APPROVED=1 AND A.CANCELLED=0 AND B.EFFECT='C' " +
                                    "AND (B.INVOICE_NO ='' OR B.INVOICE_NO LIKE 'DUM%') AND B.GRN_NO ='' AND (B.MATCHED=0 OR B.MATCHED IS NULL) " +
                                    "AND B.MODULE_ID <>"+clsVoucher.DebitNoteModuleID+" " +
                                    "AND A.VOUCHER_NO='"+GRNNo+"' " +
                                    "ORDER BY B.INVOICE_NO DESC ";
                                    int VoucherSrNo = data.getIntValueFromDB(strSQL,FinanceGlobal.FinURL);
                                    if(VoucherSrNo==0){
                                        AdjustAmount=0;
                                        continue;
                                    }
                                    strSQL = "SELECT B.AMOUNT FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B " +
                                    "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND B.MAIN_ACCOUNT_CODE = '"+MainCode+"' " +
                                    "AND B.SUB_ACCOUNT_CODE='"+SubCode+"' AND A.APPROVED=1 AND A.CANCELLED=0 AND B.EFFECT='C' " +
                                    "AND (B.INVOICE_NO ='' OR B.INVOICE_NO LIKE 'DUM%') AND B.GRN_NO ='' AND B.MODULE_ID <>"+clsVoucher.DebitNoteModuleID+" " +
                                    "AND A.VOUCHER_NO='"+GRNNo+"' AND B.SR_NO="+VoucherSrNo+" AND (B.MATCHED=0 OR B.MATCHED IS NULL) ";
                                    double AmountCanAdjust =  data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                                    
                                    if(AdjustAmount > AmountCanAdjust) {
                                        
                                        if(GRNVoucherType==FinanceGlobal.TYPE_RECEIPT || GRNVoucherType == FinanceGlobal.TYPE_CASH_RECEIPT_VOUCHER
                                        || GRNVoucherType==FinanceGlobal.TYPE_CREDIT_NOTE) {
                                            new clsDrAdjustment().AdjustReceiptAmount(2, currentVoucherNo, GRNNo, AmountCanAdjust,VoucherSrNo,SubCode);
                                        }
                                        if(GRNVoucherType==FinanceGlobal.TYPE_JOURNAL) {
                                            new clsDrAdjustment().AdjustJournalAmount(2, currentVoucherNo, GRNNo, AmountCanAdjust,VoucherSrNo,SubCode);
                                        }
                                        AdjustAmount = AdjustAmount - AmountCanAdjust;
                                    } else {
                                        if(GRNVoucherType==FinanceGlobal.TYPE_RECEIPT || GRNVoucherType == FinanceGlobal.TYPE_CASH_RECEIPT_VOUCHER
                                        || GRNVoucherType==FinanceGlobal.TYPE_CREDIT_NOTE) {
                                            new clsDrAdjustment().AdjustReceiptAmount(2, currentVoucherNo, GRNNo, AdjustAmount,VoucherSrNo,SubCode);
                                        }
                                        if(GRNVoucherType==FinanceGlobal.TYPE_JOURNAL) {
                                            new clsDrAdjustment().AdjustJournalAmount(2, currentVoucherNo, GRNNo, AdjustAmount,VoucherSrNo,SubCode);
                                        }
                                        AdjustAmount = 0;
                                    }
                                }
                            }
                            rsData.next();
                        }
                    }
                }
                
                if(VoucherType == FinanceGlobal.TYPE_PJV || VoucherType == FinanceGlobal.TYPE_PAYMENT || VoucherType == FinanceGlobal.TYPE_PAYMENT_2) {  //|| VoucherType == FinanceGlobal.TYPE_CASH_VOUCHER
                    if(EITLERPGLOBAL.FinYearFrom!= Integer.parseInt(EITLERPGLOBAL.getCurrentDateDB().substring(0,4))) {
                        String tempEndDate = EITLERPGLOBAL.getFinYearEndDate(EITLERPGLOBAL.FinYearFrom + "-04-01");
                        String tempStartDate = EITLERPGLOBAL.getFinYearStartDate(EITLERPGLOBAL.FinYearFrom + "-04-01");
                        if(java.sql.Date.valueOf(EITLERPGLOBAL.getCurrentDateDB()).after(java.sql.Date.valueOf(tempStartDate)) && java.sql.Date.valueOf(EITLERPGLOBAL.getCurrentDateDB()).before(java.sql.Date.valueOf(tempEndDate))) {
                            data.Execute("UPDATE D_FIN_VOUCHER_HEADER SET VOUCHER_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"' WHERE VOUCHER_NO='"+currentVoucherNo+"' ",FinanceGlobal.FinURL);
                            data.Execute("UPDATE D_FIN_VOUCHER_HEADER_H SET VOUCHER_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"' WHERE VOUCHER_NO='"+currentVoucherNo+"' ",FinanceGlobal.FinURL);
                            generateNextLegacyNo(currentVoucherNo);
                        } else {
                            String strSQL = "SELECT MAX(CONVERT(LEGACY_NO,SIGNED)) AS NEXT_LEGACY_NO FROM D_FIN_VOUCHER_HEADER " +
                            "WHERE VOUCHER_DATE>='"+tempStartDate+"' AND VOUCHER_DATE<='"+tempEndDate+"' " +
                            "AND BOOK_CODE="+getAttribute("BOOK_CODE").getString()+" AND VOUCHER_TYPE IN ("+clsVoucher.getVoucherType(currentVoucherNo)+") " +
                            "AND SUBSTRING(VOUCHER_NO,3,2)='"+Integer.toString(EITLERPGLOBAL.FinYearFrom).substring(2,4)+"' AND APPROVED=1 AND CANCELLED=0";
                            int nextLegacyNo = data.getIntValueFromDB(strSQL,FinanceGlobal.FinURL);
                            strSQL = "UPDATE D_FIN_VOUCHER_HEADER SET LEGACY_NO='"+Integer.toString(nextLegacyNo)+"', LEGACY_DATE='"+tempEndDate+"',VOUCHER_DATE='"+tempEndDate+"' WHERE VOUCHER_NO='"+currentVoucherNo+"' ";
                            data.Execute(strSQL,FinanceGlobal.FinURL);
                            strSQL = "UPDATE D_FIN_VOUCHER_HEADER_H SET LEGACY_NO='"+Integer.toString(nextLegacyNo)+"', LEGACY_DATE='"+tempEndDate+"',VOUCHER_DATE='"+tempEndDate+"' WHERE VOUCHER_NO='"+currentVoucherNo+"' ";
                            data.Execute(strSQL,FinanceGlobal.FinURL);
                            
                        }
                        //data.Execute("UPDATE D_FIN_VOUCHER_HEADER SET VOUCHER_DATE='"+tempEndDate+"' WHERE VOUCHER_NO='"+currentVoucherNo+"' ",FinanceGlobal.FinURL);
                        //data.Execute("UPDATE D_FIN_VOUCHER_HEADER_H SET VOUCHER_DATE='"+tempEndDate+"' WHERE VOUCHER_NO='"+currentVoucherNo+"' ",FinanceGlobal.FinURL);
                        
                    } else {
                        data.Execute("UPDATE D_FIN_VOUCHER_HEADER SET VOUCHER_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"' WHERE VOUCHER_NO='"+currentVoucherNo+"' ",FinanceGlobal.FinURL);
                        data.Execute("UPDATE D_FIN_VOUCHER_HEADER_H SET VOUCHER_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"' WHERE VOUCHER_NO='"+currentVoucherNo+"' ",FinanceGlobal.FinURL);
                        generateNextLegacyNo(currentVoucherNo);
                    }
                    
                }
                
                if(VoucherType == FinanceGlobal.TYPE_CASH_VOUCHER) {
                    data.Execute("UPDATE D_FIN_VOUCHER_HEADER SET VOUCHER_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"' WHERE VOUCHER_NO='"+currentVoucherNo+"' ",FinanceGlobal.FinURL);
                    data.Execute("UPDATE D_FIN_VOUCHER_HEADER_H SET VOUCHER_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"' WHERE VOUCHER_NO='"+currentVoucherNo+"' ",FinanceGlobal.FinURL);
                    generateNextLegacyNo(currentVoucherNo);
                }
                
                if(VoucherType==FinanceGlobal.TYPE_PJV) {
                    /*int ModuleID = data.getIntValueFromDB("SELECT DISTINCT MODULE_ID FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO = '" + VoucherNo + "' AND EFFECT='C'",FinanceGlobal.FinURL);
                if(!PostFASCardWithGRN(currentVoucherNo,ModuleID)) {
                    JOptionPane.showMessageDialog(null,"FAS Card not posted properly.\n Contact account and admin department.","ERROR MESSAGE",JOptionPane.ERROR_MESSAGE);
                }*/
                }
            }
            if(!TVoucherNo.equals("") && !EmployeeNo.equals("")) {
                clsTravelExp.Insert(TVoucherNo,EmployeeNo);
            }
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FIN_VOUCHER_HEADER WHERE COMPANY_ID="+pCompanyID+" AND VOUCHER_NO='"+pDocNo+"' AND (APPROVED=1)";
            
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
    public boolean IsEditable(int pCompanyID,String pDocNo,int pUserID,int VoucherModuleID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FIN_VOUCHER_HEADER WHERE COMPANY_ID="+pCompanyID+" AND VOUCHER_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+VoucherModuleID+" AND DOC_NO='"+pDocNo+"' AND USER_ID="+pUserID+" AND STATUS='W'";
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
            String lDocNo=getAttribute("VOUCHER_NO").getString();
            String strSQL="";
            
            //First check that record is editable
            if(CanDelete(CompanyID,lDocNo,pUserID)) {
                String strQry = "DELETE FROM D_FIN_VOUCHER_HEADER WHERE COMPANY_ID=" + CompanyID +" AND VOUCHER_NO='"+lDocNo+"'";
                data.Execute(strQry,FinanceGlobal.FinURL);
                strQry = "DELETE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID=" + CompanyID +" AND VOUCHER_NO='"+lDocNo+"'";
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
        String strCondition = " WHERE COMPANY_ID=" + CompanyID + " AND VOUCHER_NO='" + VoucherNo + "'" ;
        clsVoucher objVoucher = new clsVoucher();
        objVoucher.Filter(strCondition,CompanyID);
        return objVoucher;
    }
    
    
    public static int getVoucherCompanyID(String VoucherNo) {
        return data.getIntValueFromDB("SELECT COMPANY_ID FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+VoucherNo+"'",FinanceGlobal.FinURL);
    }
    
    public boolean Filter(String Condition,int CompanyID) {
        Ready=false;
        try {
            
            String strSql = "SELECT * FROM D_FIN_VOUCHER_HEADER " + Condition + " ORDER BY VOUCHER_DATE,VOUCHER_NO" ;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            System.out.println(strSql);
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_FIN_VOUCHER_HEADER WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND VOUCHER_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' AND VOUCHER_TYPE="+VoucherType+" ORDER BY VOUCHER_NO";
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
            
            OldVoucherNo=UtilFunctions.getString(rsResultSet,"VOUCHER_NO","");
            setAttribute("COMPANY_ID",UtilFunctions.getInt(rsResultSet,"COMPANY_ID",0));
            setAttribute("VOUCHER_TYPE",UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",1));
            setAttribute("VOUCHER_NO",UtilFunctions.getString(rsResultSet,"VOUCHER_NO",""));
            setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"VOUCHER_DATE","0000-00-00")));
            setAttribute("BOOK_CODE",UtilFunctions.getString(rsResultSet,"BOOK_CODE",""));
            setAttribute("CHEQUE_NO",UtilFunctions.getString(rsResultSet,"CHEQUE_NO",""));
            setAttribute("CHEQUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHEQUE_DATE","0000-00-00")));
            setAttribute("REALIZATION_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"REALIZATION_DATE","0000-00-00")));
            setAttribute("EMPLOYEE_NO",data.getStringValueFromDB("SELECT EMPLOYEE_NO FROM D_FIN_EMPLOYEE_TRAVEL_EXP WHERE VOUCHER_NO='"+UtilFunctions.getString(rsResultSet,"VOUCHER_NO","")+"' ",FinanceGlobal.FinURL));
            setAttribute("BANK_NAME",UtilFunctions.getString(rsResultSet,"BANK_NAME",""));
            setAttribute("ST_CATEGORY",UtilFunctions.getString(rsResultSet,"ST_CATEGORY",""));
            setAttribute("REASON_CODE",UtilFunctions.getString(rsResultSet,"REASON_CODE",""));
            setAttribute("REMARKS",UtilFunctions.getString(rsResultSet,"REMARKS",""));
            setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsResultSet,"HIERARCHY_ID",0));
            setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00")));
            setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00")));
            setAttribute("CHANGED",UtilFunctions.getInt(rsResultSet,"CHANGED",0));
            setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHANGED_DATE","0000-00-00")));
            setAttribute("APPROVED",UtilFunctions.getInt(rsResultSet,"APPROVED",0));
            setAttribute("APPROVED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"APPROVED_DATE","0000-00-00")));
            setAttribute("REJECTED",UtilFunctions.getInt(rsResultSet,"REJECTED",0));
            setAttribute("REJECTED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"REJECTED_DATE","0000-00-00")));
            setAttribute("CANCELLED",UtilFunctions.getInt(rsResultSet,"CANCELLED",0));
            setAttribute("CHEQUE_AMOUNT",UtilFunctions.getDouble(rsResultSet,"CHEQUE_AMOUNT",0));
            setAttribute("PAYMENT_MODE",UtilFunctions.getInt(rsResultSet,"PAYMENT_MODE",0));
            setAttribute("LEGACY_NO",UtilFunctions.getString(rsResultSet,"LEGACY_NO",""));
            setAttribute("LINK_NO",UtilFunctions.getString(rsResultSet,"LINK_NO",""));
            setAttribute("LEGACY_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"LEGACY_DATE","0000-00-00")));
            setAttribute("EXCLUDE_IN_ADJ",UtilFunctions.getInt(rsResultSet,"EXCLUDE_IN_ADJ",0));
            setAttribute("EMPLOYEE_NO",data.getStringValueFromDB("SELECT EMPLOYEE_NO FROM D_FIN_EMPLOYEE_TRAVEL_EXP WHERE VOUCHER_NO='"+UtilFunctions.getString(rsResultSet,"VOUCHER_NO","")+"' ",FinanceGlobal.FinURL));
            colVoucherItems.clear();
            
            int CompanyID=getAttribute("COMPANY_ID").getInt();
            String VoucherNo=getAttribute("VOUCHER_NO").getString();
            
            tmpStmt=tmpConn.createStatement();
            
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FIN_VOUCHER_DETAIL_H WHERE COMPANY_ID=" + CompanyID + " AND VOUCHER_NO='" + VoucherNo + "' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            } else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID=" + CompanyID + " AND VOUCHER_NO='" + VoucherNo + "' ORDER BY SR_NO");
            }
            
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsVoucherItem objItem = new clsVoucherItem();
                
                objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                objItem.setAttribute("SR_NO",UtilFunctions.getInt(rsTmp,"SR_NO",0));
                objItem.setAttribute("EFFECT",UtilFunctions.getString(rsTmp,"EFFECT",""));
                objItem.setAttribute("ACCOUNT_ID",UtilFunctions.getInt(rsTmp,"ACCOUNT_ID",0));
                objItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","").trim());
                objItem.setAttribute("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"SUB_ACCOUNT_CODE","").trim());
                objItem.setAttribute("AMOUNT",UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                objItem.setAttribute("APPLICABLE_AMOUNT",UtilFunctions.getDouble(rsTmp,"APPLICABLE_AMOUNT",0));
                objItem.setAttribute("PERCENTAGE",UtilFunctions.getDouble(rsTmp,"PERCENTAGE",0));
                objItem.setAttribute("REMARKS",UtilFunctions.getString(rsTmp,"REMARKS",""));
                objItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsTmp,"CREATED_BY",""));
                objItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CREATED_DATE","0000-00-00")));
                objItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsTmp,"MODIFIED_BY",""));
                objItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MODIFIED_DATE","0000-00-00")));
                objItem.setAttribute("CHANGED",true);
                objItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CHANGED_DATE","0000-00-00")));
                objItem.setAttribute("CANCELLED",0);
                objItem.setAttribute("PO_NO",UtilFunctions.getString(rsTmp,"PO_NO",""));
                objItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"PO_DATE","0000-00-00")));
                objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                objItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VALUE_DATE","0000-00-00")));
                objItem.setAttribute("GRN_NO",UtilFunctions.getString(rsTmp,"GRN_NO",""));
                objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"GRN_DATE","0000-00-00")));
                objItem.setAttribute("MODULE_ID",UtilFunctions.getInt(rsTmp,"MODULE_ID",0));
                objItem.setAttribute("INVOICE_AMOUNT",UtilFunctions.getDouble(rsTmp,"INVOICE_AMOUNT",0));
                objItem.setAttribute("IS_DEDUCTION",UtilFunctions.getInt(rsTmp,"IS_DEDUCTION",0));
                objItem.setAttribute("REF_COMPANY_ID",UtilFunctions.getInt(rsTmp,"REF_COMPANY_ID",0));
                objItem.setAttribute("LINK_NO",UtilFunctions.getString(rsTmp,"LINK_NO",""));
                objItem.setAttribute("MATCHED",UtilFunctions.getInt(rsTmp,"MATCHED",0));
                objItem.setAttribute("MATCHED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MATCHED_DATE","0000-00-00")));
                objItem.setAttribute("REF_SR_NO",UtilFunctions.getInt(rsTmp,"REF_SR_NO",0));
                colVoucherItems.put(Long.toString(Counter),objItem);
                rsTmp.next();
            }
            
            //********** Special Representation Logics *************//
            if(UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",0)==FinanceGlobal.TYPE_PAYMENT||UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",0)==FinanceGlobal.TYPE_PAYMENT_2) {
                getPaymentRepresentation(); //Convert data into Payment Voucher presentation
            }
            
            if(UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",0)==FinanceGlobal.TYPE_DEBIT_NOTE) {
                getDebitNoteRepresentation(); //Convert data into Debit Note Voucher presentation
            }
            
            if(UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",0)==FinanceGlobal.TYPE_CASH_VOUCHER) {
                getCashPaymentRepresentation(); //Convert data into Cash Payment Voucher presentation
            }
            
            if(UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",0)==FinanceGlobal.TYPE_RECEIPT) {
                getReceiptRepresentation(); //Convert data into Cash Payment Voucher presentation
            }
            
            if(UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",0)==FinanceGlobal.TYPE_CASH_RECEIPT_VOUCHER) {
                getCashReceiptRepresentation(); //Convert data into Cash Payment Voucher presentation
            }
            
            if(UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",0)==FinanceGlobal.TYPE_CREDIT_NOTE) {
                getCreditNoteRepresentation(); //Convert data into Cash Payment Voucher presentation
            }
            
            if(UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",0)==FinanceGlobal.TYPE_LC_JV) {
                getLCJVRepresentation();
            }
            
            if(UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",0)==FinanceGlobal.TYPE_PJV) {
                getPJVRepresentation();
            }
            
            if(UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",0)==FinanceGlobal.TYPE_JOURNAL) {
                getPJVRepresentation();
            }
            
            if(UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",0)==FinanceGlobal.TYPE_SALES_JOURNAL) {
                getPJVRepresentation();
            }
            //******************************************************//
            
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }
    
    public boolean getCashReceiptRepresentation() {
        ResultSet rsTmp;
        ResultSet rsDeduction;
        Connection tmpConn;
        Statement tmpStmt;
        
        objPayment=new clsVoucher();
        objPayment.colVoucherItems.clear();
        objPayment.colVoucherItemsEx.clear();
        
        tmpConn=data.getConn(FinanceGlobal.FinURL);
        
        long Counter=0;
        int RevNo=0;
        int VoucherSrNo=0;
        double ChequeAmount=0;
        double DeductionAmount=0;
        
        try {
            
            int CompanyID=getAttribute("COMPANY_ID").getInt();
            String VoucherNo=getAttribute("VOUCHER_NO").getString();
            
            //General Retrieval of the Voucher
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
                objPayment.setAttribute("REVISION_NO",UtilFunctions.getInt(rsResultSet,"REVISION_NO",0));
            }
            else {
                objPayment.setAttribute("REVISION_NO",0);
            }
            
            OldVoucherNo=UtilFunctions.getString(rsResultSet,"VOUCHER_NO","");
            objPayment.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsResultSet,"COMPANY_ID",0));
            objPayment.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsResultSet,"VOUCHER_NO",""));
            objPayment.setAttribute("VOUCHER_TYPE",UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",1));
            objPayment.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"VOUCHER_DATE","0000-00-00")));
            objPayment.setAttribute("BOOK_CODE",UtilFunctions.getString(rsResultSet,"BOOK_CODE",""));
            objPayment.setAttribute("CHEQUE_NO",UtilFunctions.getString(rsResultSet,"CHEQUE_NO",""));
            objPayment.setAttribute("CHEQUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHEQUE_DATE","0000-00-00")));
            objPayment.setAttribute("REALIZATION_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"REALIZATION_DATE","0000-00-00")));
            objPayment.setAttribute("BANK_NAME",UtilFunctions.getString(rsResultSet,"BOOK_CODE",""));
            objPayment.setAttribute("ST_CATEGORY",UtilFunctions.getString(rsResultSet,"ST_CATEGORY",""));
            objPayment.setAttribute("REASON_CODE",UtilFunctions.getString(rsResultSet,"REASON_CODE",""));
            objPayment.setAttribute("REMARKS",UtilFunctions.getString(rsResultSet,"REMARKS",""));
            objPayment.setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsResultSet,"HIERARCHY_ID",0));
            objPayment.setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            objPayment.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00")));
            objPayment.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            objPayment.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00")));
            objPayment.setAttribute("CHANGED",UtilFunctions.getInt(rsResultSet,"CHANGED",0));
            objPayment.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHANGED_DATE","0000-00-00")));
            objPayment.setAttribute("APPROVED",UtilFunctions.getInt(rsResultSet,"APPROVED",0));
            objPayment.setAttribute("APPROVED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"APPROVED_DATE","0000-00-00")));
            objPayment.setAttribute("REJECTED",UtilFunctions.getInt(rsResultSet,"REJECTED",0));
            objPayment.setAttribute("REJECTED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"REJECTED_DATE","0000-00-00")));
            objPayment.setAttribute("CANCELLED",UtilFunctions.getInt(rsResultSet,"CANCELLED",0));
            objPayment.setAttribute("LEGACY_NO",UtilFunctions.getString(rsResultSet,"LEGACY_NO",""));
            objPayment.setAttribute("LINK_NO",UtilFunctions.getString(rsResultSet,"LINK_NO",""));
            objPayment.setAttribute("LEGACY_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"LEGACY_DATE","0000-00-00")));
            objPayment.setAttribute("EMPLOYEE_NO",data.getStringValueFromDB("SELECT EMPLOYEE_NO FROM D_FIN_EMPLOYEE_TRAVEL_EXP WHERE VOUCHER_NO='"+VoucherNo+"' ",FinanceGlobal.FinURL));
            objPayment.setAttribute("EXCLUDE_IN_ADJ",UtilFunctions.getInt(rsResultSet,"EXCLUDE_IN_ADJ",0));
            
            String MainAccountCode=data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='C' AND MAIN_ACCOUNT_CODE<>'' AND IS_DEDUCTION=0 ",FinanceGlobal.FinURL);
            String BankCode=data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='D' AND MAIN_ACCOUNT_CODE<>'' AND IS_DEDUCTION=0 ",FinanceGlobal.FinURL);
            String PartyCode="";
            
            if(BankCode.trim().equals("")) {
                MainAccountCode="";
                BankCode=data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='D' AND MAIN_ACCOUNT_CODE<>'' ",FinanceGlobal.FinURL);
                PartyCode="";
            }
            
            objPayment.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode);
            objPayment.setAttribute("SUB_ACCOUNT_CODE",PartyCode);
            objPayment.setAttribute("BANK_CODE",BankCode);
            objPayment.setAttribute("CHEQUE_AMOUNT",0);
            
            
            objPayment.colVoucherItems.clear();
            objPayment.colVoucherItemsEx.clear();
            
            //Find Unique Invoices
            ResultSet rsInvoices=data.getResult("SELECT DISTINCT(INVOICE_NO) FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='C' " ,FinanceGlobal.FinURL);
            
            System.out.println("SELECT DISTINCT(INVOICE_NO) FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='C' AND IS_DEDUCTION=0 ");
            rsInvoices.first();
            
            if(rsInvoices.getRow()>0) {
                
                
                while(!rsInvoices.isAfterLast()) {
                    String InvoiceNo=UtilFunctions.getString(rsInvoices,"INVOICE_NO","");
                    
                    Counter=0;
                    
                    //Find Debit Entries - Except Normal Deduction
                    rsTmp=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND INVOICE_NO='"+InvoiceNo+"' AND EFFECT='C' AND MAIN_ACCOUNT_CODE<>'' AND IS_DEDUCTION=0 ",FinanceGlobal.FinURL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        while(!rsTmp.isAfterLast()) {
                            
                            VoucherSrNo=UtilFunctions.getInt(rsTmp,"SR_NO", 0);
                            
                            Counter=Counter+1;
                            clsVoucherItem objItem = new clsVoucherItem();
                            HashMap objDeductions=new HashMap();
                            
                            objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                            objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                            objItem.setAttribute("SR_NO",UtilFunctions.getInt(rsTmp,"SR_NO",0));
                            objItem.setAttribute("EFFECT",UtilFunctions.getString(rsTmp,"EFFECT",""));
                            objItem.setAttribute("ACCOUNT_ID",UtilFunctions.getInt(rsTmp,"ACCOUNT_ID",0));
                            objItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","").trim());
                            objItem.setAttribute("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"SUB_ACCOUNT_CODE","").trim());
                            
                            objItem.setAttribute("AMOUNT",UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                            
                            ChequeAmount+=UtilFunctions.getDouble(rsTmp,"AMOUNT",0);
                            
                            objItem.setAttribute("REMARKS",UtilFunctions.getString(rsTmp,"REMARKS",""));
                            objItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsTmp,"CREATED_BY",""));
                            objItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CREATED_DATE","0000-00-00")));
                            objItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsTmp,"MODIFIED_BY",""));
                            objItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MODIFIED_DATE","0000-00-00")));
                            objItem.setAttribute("CHANGED",true);
                            objItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CHANGED_DATE","0000-00-00")));
                            objItem.setAttribute("CANCELLED",0);
                            objItem.setAttribute("PO_NO",UtilFunctions.getString(rsTmp,"PO_NO",""));
                            objItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"PO_DATE","0000-00-00")));
                            objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                            objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                            objItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VALUE_DATE","0000-00-00")));
                            objItem.setAttribute("GRN_NO",UtilFunctions.getString(rsTmp,"GRN_NO",""));
                            objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"GRN_DATE","0000-00-00")));
                            objItem.setAttribute("MODULE_ID",UtilFunctions.getInt(rsTmp,"MODULE_ID",0));
                            objItem.setAttribute("REF_COMPANY_ID",UtilFunctions.getInt(rsTmp,"REF_COMPANY_ID",0));
                            objItem.setAttribute("LINK_NO",UtilFunctions.getString(rsTmp,"LINK_NO",""));
                            objItem.setAttribute("MATCHED",UtilFunctions.getInt(rsTmp,"MATCHED",0));
                            objItem.setAttribute("MATCHED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MATCHED_DATE","0000-00-00")));
                            DeductionAmount=0;
                            
                            objItem.setAttribute("INVOICE_AMOUNT",UtilFunctions.getDouble(rsTmp,"INVOICE_AMOUNT",0)+DeductionAmount);
                            objItem.setAttribute("INVOICE_DEDUCTIONS",objDeductions);
                            objItem.setAttribute("DEDUCTION_AMOUNT",DeductionAmount);
                            objItem.setAttribute("IS_DEDUCTION",0);
                            //************************ End of Deduction Findings ******************//
                            
                            objPayment.colVoucherItems.put(Integer.toString(objPayment.colVoucherItems.size()+1) ,objItem);
                            
                            rsTmp.next();
                        }
                    }
                    rsInvoices.next();
                }
            }
            
            objPayment.setAttribute("CHEQUE_AMOUNT",ChequeAmount);
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
        
    }
    
    public boolean getPJVRepresentation() {
        ResultSet rsTmp;
        ResultSet rsDeduction;
        Connection tmpConn;
        Statement tmpStmt;
        
        objPayment=new clsVoucher();
        objPayment.colVoucherItems.clear();
        objPayment.colVoucherItemsEx.clear();
        
        tmpConn=data.getConn(FinanceGlobal.FinURL);
        
        //        long Counter=0;
        int RevNo=0;
        int VoucherSrNo=0;
        double ChequeAmount=0;
        double DeductionAmount=0;
        try {
            int CompanyID=getAttribute("COMPANY_ID").getInt();
            String VoucherNo=getAttribute("VOUCHER_NO").getString();
            
            //General Retrieval of the Voucher -- Mrugesh
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
                objPayment.setAttribute("REVISION_NO",UtilFunctions.getInt(rsResultSet,"REVISION_NO",0));
            }
            else {
                objPayment.setAttribute("REVISION_NO",0);
            }
            
            OldVoucherNo=UtilFunctions.getString(rsResultSet,"VOUCHER_NO","");
            objPayment.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsResultSet,"COMPANY_ID",0));
            objPayment.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsResultSet,"VOUCHER_NO",""));
            objPayment.setAttribute("VOUCHER_TYPE",UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",1));
            objPayment.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"VOUCHER_DATE","0000-00-00")));
            objPayment.setAttribute("BOOK_CODE",UtilFunctions.getString(rsResultSet,"BOOK_CODE",""));
            objPayment.setAttribute("CHEQUE_NO",UtilFunctions.getString(rsResultSet,"CHEQUE_NO",""));
            objPayment.setAttribute("CHEQUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHEQUE_DATE","0000-00-00")));
            objPayment.setAttribute("REALIZATION_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"REALIZATION_DATE","0000-00-00")));
            objPayment.setAttribute("BANK_NAME",UtilFunctions.getString(rsResultSet,"BOOK_CODE",""));
            objPayment.setAttribute("ST_CATEGORY",UtilFunctions.getString(rsResultSet,"ST_CATEGORY",""));
            objPayment.setAttribute("REASON_CODE",UtilFunctions.getString(rsResultSet,"REASON_CODE",""));
            objPayment.setAttribute("REMARKS",UtilFunctions.getString(rsResultSet,"REMARKS",""));
            objPayment.setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsResultSet,"HIERARCHY_ID",0));
            objPayment.setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            objPayment.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00")));
            objPayment.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            objPayment.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00")));
            objPayment.setAttribute("CHANGED",UtilFunctions.getInt(rsResultSet,"CHANGED",0));
            objPayment.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHANGED_DATE","0000-00-00")));
            objPayment.setAttribute("APPROVED",UtilFunctions.getInt(rsResultSet,"APPROVED",0));
            objPayment.setAttribute("APPROVED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"APPROVED_DATE","0000-00-00")));
            objPayment.setAttribute("REJECTED",UtilFunctions.getInt(rsResultSet,"REJECTED",0));
            objPayment.setAttribute("REJECTED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"REJECTED_DATE","0000-00-00")));
            objPayment.setAttribute("CANCELLED",UtilFunctions.getInt(rsResultSet,"CANCELLED",0));
            objPayment.setAttribute("LEGACY_NO",UtilFunctions.getString(rsResultSet,"LEGACY_NO",""));
            objPayment.setAttribute("LEGACY_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"LEGACY_DATE","0000-00-00")));
            objPayment.setAttribute("EXCLUDE_IN_ADJ",UtilFunctions.getInt(rsResultSet,"EXCLUDE_IN_ADJ",0));
            
            // Get only non deduction Entries -- Mrugesh
            
            ResultSet rsNormalEntries=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND MAIN_ACCOUNT_CODE<>'' AND IS_DEDUCTION=0 ORDER BY SR_NO" ,FinanceGlobal.FinURL);
            rsNormalEntries.first();
            
            if(rsNormalEntries.getRow()>0) {
                while(!rsNormalEntries.isAfterLast()) {
                    VoucherSrNo=UtilFunctions.getInt(rsNormalEntries,"SR_NO", 0);
                    clsVoucherItem objItem = new clsVoucherItem();
                    HashMap objDeductions=new HashMap();
                    DeductionAmount=0;
                    objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsNormalEntries,"COMPANY_ID", 0));
                    objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsNormalEntries,"VOUCHER_NO",""));
                    objItem.setAttribute("SR_NO",UtilFunctions.getInt(rsNormalEntries,"SR_NO",0));
                    objItem.setAttribute("EFFECT",UtilFunctions.getString(rsNormalEntries,"EFFECT",""));
                    objItem.setAttribute("ACCOUNT_ID",UtilFunctions.getInt(rsNormalEntries,"ACCOUNT_ID",0));
                    objItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsNormalEntries,"MAIN_ACCOUNT_CODE","").trim());
                    objItem.setAttribute("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsNormalEntries,"SUB_ACCOUNT_CODE","").trim());
                    
                    objItem.setAttribute("AMOUNT",UtilFunctions.getDouble(rsNormalEntries,"AMOUNT",0));
                    objItem.setAttribute("REMARKS",UtilFunctions.getString(rsNormalEntries,"REMARKS",""));
                    objItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsNormalEntries,"CREATED_BY",""));
                    objItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsNormalEntries,"CREATED_DATE","0000-00-00")));
                    objItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsNormalEntries,"MODIFIED_BY",""));
                    objItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsNormalEntries,"MODIFIED_DATE","0000-00-00")));
                    objItem.setAttribute("CHANGED",true);
                    objItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsNormalEntries,"CHANGED_DATE","0000-00-00")));
                    objItem.setAttribute("CANCELLED",0);
                    objItem.setAttribute("PO_NO",UtilFunctions.getString(rsNormalEntries,"PO_NO",""));
                    objItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsNormalEntries,"PO_DATE","0000-00-00")));
                    objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsNormalEntries,"INVOICE_NO",""));
                    objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsNormalEntries,"INVOICE_DATE","0000-00-00")));
                    objItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsNormalEntries,"VALUE_DATE","0000-00-00")));
                    objItem.setAttribute("GRN_NO",UtilFunctions.getString(rsNormalEntries,"GRN_NO",""));
                    objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsNormalEntries,"GRN_DATE","0000-00-00")));
                    objItem.setAttribute("MODULE_ID",UtilFunctions.getInt(rsNormalEntries,"MODULE_ID",0));
                    objItem.setAttribute("REF_COMPANY_ID",UtilFunctions.getInt(rsNormalEntries,"REF_COMPANY_ID",0));
                    objItem.setAttribute("LINK_NO",UtilFunctions.getString(rsNormalEntries,"LINK_NO",""));
                    objItem.setAttribute("MATCHED",UtilFunctions.getInt(rsNormalEntries,"MATCHED",0));
                    objItem.setAttribute("MATCHED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsNormalEntries,"MATCHED_DATE","0000-00-00")));
                    objItem.setAttribute("INVOICE_AMOUNT",UtilFunctions.getDouble(rsNormalEntries,"AMOUNT",0));
                    objItem.setAttribute("INVOICE_DEDUCTIONS",objDeductions);
                    objItem.setAttribute("DEDUCTION_AMOUNT",DeductionAmount);
                    objItem.setAttribute("IS_DEDUCTION",0);
                    //************************ End of Deduction Findings ******************//
                    
                    objPayment.colVoucherItems.put(Integer.toString(objPayment.colVoucherItems.size()+1) ,objItem);
                    rsNormalEntries.next();
                }
            }
            
            ResultSet rsDeductionEntries=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND MAIN_ACCOUNT_CODE<>'' AND EFFECT='D' AND IS_DEDUCTION=1 ORDER BY SR_NO" ,FinanceGlobal.FinURL);
            rsDeductionEntries.first();
            
            if(rsDeductionEntries.getRow()>0) {
                while(!rsDeductionEntries.isAfterLast()) {
                    VoucherSrNo=UtilFunctions.getInt(rsDeductionEntries,"SR_NO", 0);
                    DeductionAmount=0;
                    if(data.IsRecordExist("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo+"' AND REF_SR_NO="+VoucherSrNo+" AND MAIN_ACCOUNT_CODE<>'' AND EFFECT='C' AND IS_DEDUCTION=1", FinanceGlobal.FinURL)) {
                        clsVoucherItem objItem = new clsVoucherItem();
                        HashMap objDeductions=new HashMap();
                        
                        objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsDeductionEntries,"COMPANY_ID", 0));
                        objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsDeductionEntries,"VOUCHER_NO",""));
                        objItem.setAttribute("SR_NO",UtilFunctions.getInt(rsDeductionEntries,"SR_NO",0));
                        objItem.setAttribute("EFFECT","D");
                        objItem.setAttribute("ACCOUNT_ID",UtilFunctions.getInt(rsDeductionEntries,"ACCOUNT_ID",0));
                        objItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsDeductionEntries,"MAIN_ACCOUNT_CODE","").trim());
                        objItem.setAttribute("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsDeductionEntries,"SUB_ACCOUNT_CODE","").trim());
                        String MainAccountCode = UtilFunctions.getString(rsDeductionEntries,"MAIN_ACCOUNT_CODE","").trim();
                        String SubAccountCode = UtilFunctions.getString(rsDeductionEntries,"SUB_ACCOUNT_CODE","").trim();
                        
                        //objItem.setAttribute("AMOUNT",UtilFunctions.getDouble(rsNormalEntries,"AMOUNT",0));
                        objItem.setAttribute("AMOUNT",data.getDoubleValueFromDB("SELECT SUM(AMOUNT) AS TOTAL FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo+"' AND EFFECT='D' AND MAIN_ACCOUNT_CODE<>'' AND SUB_ACCOUNT_CODE<>'' AND IS_DEDUCTION=1 AND REF_SR_NO="+VoucherSrNo,FinanceGlobal.FinURL));
                        
                        objItem.setAttribute("REMARKS","");
                        objItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsDeductionEntries,"CREATED_BY",""));
                        objItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeductionEntries,"CREATED_DATE","0000-00-00")));
                        objItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsDeductionEntries,"MODIFIED_BY",""));
                        objItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeductionEntries,"MODIFIED_DATE","0000-00-00")));
                        objItem.setAttribute("CHANGED",true);
                        objItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeductionEntries,"CHANGED_DATE","0000-00-00")));
                        objItem.setAttribute("CANCELLED",0);
                        objItem.setAttribute("PO_NO",UtilFunctions.getString(rsDeductionEntries,"PO_NO",""));
                        objItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeductionEntries,"PO_DATE","0000-00-00")));
                        objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsDeductionEntries,"INVOICE_NO",""));
                        objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeductionEntries,"INVOICE_DATE","0000-00-00")));
                        objItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeductionEntries,"VALUE_DATE","0000-00-00")));
                        objItem.setAttribute("GRN_NO",UtilFunctions.getString(rsDeductionEntries,"GRN_NO",""));
                        objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeductionEntries,"GRN_DATE","0000-00-00")));
                        objItem.setAttribute("MODULE_ID",UtilFunctions.getInt(rsDeductionEntries,"MODULE_ID",0));
                        objItem.setAttribute("REF_COMPANY_ID",UtilFunctions.getInt(rsDeductionEntries,"REF_COMPANY_ID",0));
                        objItem.setAttribute("LINK_NO",UtilFunctions.getString(rsDeductionEntries,"LINK_NO",""));
                        objItem.setAttribute("MATCHED",UtilFunctions.getInt(rsDeductionEntries,"MATCHED",0));
                        objItem.setAttribute("MATCHED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeductionEntries,"MATCHED_DATE","0000-00-00")));
                        //*************** Find Direct Deductions for this Invoice ******************//
                        rsDeduction=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND REF_SR_NO="+VoucherSrNo+" AND EFFECT='C' AND IS_DEDUCTION=1 ",FinanceGlobal.FinURL);
                        rsDeduction.first();
                        
                        if(rsDeduction.getRow()>0) {
                            while(!rsDeduction.isAfterLast()) {
                                clsVoucherItem objDeductionItem = new clsVoucherItem();
                                
                                DeductionAmount+=UtilFunctions.getDouble(rsDeduction,"AMOUNT",0);
                                
                                objDeductionItem.setAttribute("DOC_TYPE",1); // 1 for Direct Deduction
                                objDeductionItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsDeduction,"COMPANY_ID", 0));
                                objDeductionItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsDeduction,"VOUCHER_NO",""));
                                objDeductionItem.setAttribute("SR_NO",UtilFunctions.getInt(rsDeduction,"SR_NO",0));
                                objDeductionItem.setAttribute("EFFECT",UtilFunctions.getString(rsDeduction,"EFFECT",""));
                                objDeductionItem.setAttribute("ACCOUNT_ID",UtilFunctions.getInt(rsDeduction,"ACCOUNT_ID",0));
                                objDeductionItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsDeduction,"MAIN_ACCOUNT_CODE","").trim());
                                objDeductionItem.setAttribute("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsDeduction,"SUB_ACCOUNT_CODE","").trim());
                                objDeductionItem.setAttribute("AMOUNT",UtilFunctions.getDouble(rsDeduction,"AMOUNT",0));
                                objDeductionItem.setAttribute("APPLICABLE_AMOUNT",UtilFunctions.getDouble(rsDeduction,"APPLICABLE_AMOUNT",0));
                                objDeductionItem.setAttribute("PERCENTAGE",UtilFunctions.getDouble(rsDeduction,"PERCENTAGE",0));
                                objDeductionItem.setAttribute("REMARKS",UtilFunctions.getString(rsDeduction,"REMARKS",""));
                                objDeductionItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsDeduction,"CREATED_BY",""));
                                objDeductionItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"CREATED_DATE","0000-00-00")));
                                objDeductionItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsDeduction,"MODIFIED_BY",""));
                                objDeductionItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"MODIFIED_DATE","0000-00-00")));
                                objDeductionItem.setAttribute("CHANGED",true);
                                objDeductionItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"CHANGED_DATE","0000-00-00")));
                                objDeductionItem.setAttribute("CANCELLED",0);
                                objDeductionItem.setAttribute("PO_NO",UtilFunctions.getString(rsDeduction,"PO_NO",""));
                                objDeductionItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"PO_DATE","0000-00-00")));
                                objDeductionItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsDeduction,"INVOICE_NO",""));
                                objDeductionItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"INVOICE_DATE","0000-00-00")));
                                objDeductionItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"VALUE_DATE","0000-00-00")));
                                objDeductionItem.setAttribute("GRN_NO",UtilFunctions.getString(rsDeduction,"GRN_NO",""));
                                objDeductionItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"GRN_DATE","0000-00-00")));
                                objDeductionItem.setAttribute("MODULE_ID",UtilFunctions.getInt(rsDeduction,"MODULE_ID",0));
                                objDeductionItem.setAttribute("INVOICE_AMOUNT",UtilFunctions.getDouble(rsDeduction,"INVOICE_AMOUNT",0));
                                objDeductionItem.setAttribute("IS_DEDUCTION",UtilFunctions.getInt(rsDeduction,"IS_DEDUCTION",0));
                                objDeductionItem.setAttribute("MATCHED",UtilFunctions.getInt(rsDeduction,"MATCHED",0));
                                objDeductionItem.setAttribute("MATCHED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"MATCHED_DATE","0000-00-00")));
                                objDeductionItem.setAttribute("REF_COMPANY_ID",UtilFunctions.getInt(rsDeduction,"REF_COMPANY_ID",0));
                                objDeductionItem.setAttribute("IS_DEDUCTION",1); // 1 for Direct Deduction
                                objDeductionItem.setAttribute("LINK_NO",UtilFunctions.getString(rsDeduction,"LINK_NO",""));
                                
                                objDeductions.put(Integer.toString(objDeductions.size()+1),objDeductionItem);
                                rsDeduction.next();
                            }
                        }
                        //*************************** End of direct deduction **********************************//
                        
                        
                        objItem.setAttribute("INVOICE_AMOUNT",DeductionAmount);
                        objItem.setAttribute("INVOICE_DEDUCTIONS",objDeductions);
                        objItem.setAttribute("DEDUCTION_AMOUNT",DeductionAmount);
                        objItem.setAttribute("IS_DEDUCTION",1);
                        objPayment.colVoucherItems.put(Integer.toString(objPayment.colVoucherItems.size()+1) ,objItem);
                    }
                    rsDeductionEntries.next();
                }
            }
            return true;
        } catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean getPaymentRepresentation() {
        ResultSet rsTmp;
        ResultSet rsDeduction;
        Connection tmpConn;
        Statement tmpStmt;
        
        objPayment=new clsVoucher();
        objPayment.colVoucherItems.clear();
        objPayment.colVoucherItemsEx.clear();
        
        tmpConn=data.getConn(FinanceGlobal.FinURL);
        
        long Counter=0;
        int RevNo=0;
        int VoucherSrNo=0;
        double ChequeAmount=0;
        double DeductionAmount=0;
        
        try {
            
            int CompanyID=getAttribute("COMPANY_ID").getInt();
            String VoucherNo=getAttribute("VOUCHER_NO").getString();
            
            //General Retrieval of the Voucher
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
                objPayment.setAttribute("REVISION_NO",UtilFunctions.getInt(rsResultSet,"REVISION_NO",0));
            } else {
                objPayment.setAttribute("REVISION_NO",0);
            }
            
            OldVoucherNo=UtilFunctions.getString(rsResultSet,"VOUCHER_NO","");
            objPayment.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsResultSet,"COMPANY_ID",0));
            objPayment.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsResultSet,"VOUCHER_NO",""));
            objPayment.setAttribute("VOUCHER_TYPE",UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",1));
            objPayment.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"VOUCHER_DATE","0000-00-00")));
            objPayment.setAttribute("BOOK_CODE",UtilFunctions.getString(rsResultSet,"BOOK_CODE",""));
            objPayment.setAttribute("CHEQUE_NO",UtilFunctions.getString(rsResultSet,"CHEQUE_NO",""));
            objPayment.setAttribute("CHEQUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHEQUE_DATE","0000-00-00")));
            objPayment.setAttribute("REALIZATION_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"REALIZATION_DATE","0000-00-00")));
            objPayment.setAttribute("BANK_NAME",UtilFunctions.getString(rsResultSet,"BOOK_CODE",""));
            objPayment.setAttribute("ST_CATEGORY",UtilFunctions.getString(rsResultSet,"ST_CATEGORY",""));
            objPayment.setAttribute("REASON_CODE",UtilFunctions.getString(rsResultSet,"REASON_CODE",""));
            objPayment.setAttribute("REMARKS",UtilFunctions.getString(rsResultSet,"REMARKS",""));
            objPayment.setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsResultSet,"HIERARCHY_ID",0));
            objPayment.setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            objPayment.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00")));
            objPayment.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            objPayment.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00")));
            objPayment.setAttribute("CHANGED",UtilFunctions.getInt(rsResultSet,"CHANGED",0));
            objPayment.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHANGED_DATE","0000-00-00")));
            objPayment.setAttribute("APPROVED",UtilFunctions.getInt(rsResultSet,"APPROVED",0));
            objPayment.setAttribute("APPROVED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"APPROVED_DATE","0000-00-00")));
            objPayment.setAttribute("REJECTED",UtilFunctions.getInt(rsResultSet,"REJECTED",0));
            objPayment.setAttribute("REJECTED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"REJECTED_DATE","0000-00-00")));
            objPayment.setAttribute("CANCELLED",UtilFunctions.getInt(rsResultSet,"CANCELLED",0));
            objPayment.setAttribute("LEGACY_NO",UtilFunctions.getString(rsResultSet,"LEGACY_NO",""));
            objPayment.setAttribute("LEGACY_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"LEGACY_DATE","0000-00-00")));
            
            
            String MainAccountCode=data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='D' AND MAIN_ACCOUNT_CODE<>'' AND IS_DEDUCTION=0 ",FinanceGlobal.FinURL);
            String BankCode=data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='C' AND MAIN_ACCOUNT_CODE<>'' AND IS_DEDUCTION=0 ",FinanceGlobal.FinURL);
            String PartyCode=data.getStringValueFromDB("SELECT SUB_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='D' AND MAIN_ACCOUNT_CODE<>'' AND SUB_ACCOUNT_CODE<>'' AND IS_DEDUCTION=0",FinanceGlobal.FinURL);
            
            if(BankCode.trim().equals("")) {
                MainAccountCode="";
                BankCode=data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='C' AND MAIN_ACCOUNT_CODE<>'' ",FinanceGlobal.FinURL);
                PartyCode=data.getStringValueFromDB("SELECT SUB_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='D' AND MAIN_ACCOUNT_CODE<>'' AND SUB_ACCOUNT_CODE<>'' ",FinanceGlobal.FinURL);
            }
            
            objPayment.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode);
            objPayment.setAttribute("SUB_ACCOUNT_CODE",PartyCode);
            objPayment.setAttribute("BANK_CODE",BankCode);
            objPayment.setAttribute("CHEQUE_AMOUNT",0);
            
            
            objPayment.colVoucherItems.clear();
            objPayment.colVoucherItemsEx.clear();
            
            //Find Unique Invoices
            ResultSet rsInvoices=data.getResult("SELECT SR_NO FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='D' " ,FinanceGlobal.FinURL);
            rsInvoices.first();
            
            if(rsInvoices.getRow()>0) {
                while(!rsInvoices.isAfterLast()) {
                    String strSrNo=Integer.toString(UtilFunctions.getInt(rsInvoices,"SR_NO",0));
                    
                    Counter=0;
                    
                    //Find Debit Entries - Except Normal Deduction
                    rsTmp=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND SR_NO="+strSrNo+" AND EFFECT='D' AND MAIN_ACCOUNT_CODE<>'' AND IS_DEDUCTION=0 ",FinanceGlobal.FinURL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        while(!rsTmp.isAfterLast()) {
                            VoucherSrNo=UtilFunctions.getInt(rsTmp,"SR_NO", 0);
                            Counter=Counter+1;
                            clsVoucherItem objItem = new clsVoucherItem();
                            HashMap objDeductions=new HashMap();
                            
                            objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                            objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                            objItem.setAttribute("SR_NO",UtilFunctions.getInt(rsTmp,"SR_NO",0));
                            objItem.setAttribute("EFFECT",UtilFunctions.getString(rsTmp,"EFFECT",""));
                            objItem.setAttribute("ACCOUNT_ID",UtilFunctions.getInt(rsTmp,"ACCOUNT_ID",0));
                            objItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","").trim());
                            objItem.setAttribute("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"SUB_ACCOUNT_CODE","").trim());
                            
                            objItem.setAttribute("AMOUNT",UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                            
                            ChequeAmount+=UtilFunctions.getDouble(rsTmp,"AMOUNT",0);
                            
                            objItem.setAttribute("REMARKS",UtilFunctions.getString(rsTmp,"REMARKS",""));
                            objItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsTmp,"CREATED_BY",""));
                            objItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CREATED_DATE","0000-00-00")));
                            objItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsTmp,"MODIFIED_BY",""));
                            objItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MODIFIED_DATE","0000-00-00")));
                            objItem.setAttribute("CHANGED",true);
                            objItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CHANGED_DATE","0000-00-00")));
                            objItem.setAttribute("CANCELLED",0);
                            objItem.setAttribute("PO_NO",UtilFunctions.getString(rsTmp,"PO_NO",""));
                            objItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"PO_DATE","0000-00-00")));
                            objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                            objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                            objItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VALUE_DATE","0000-00-00")));
                            objItem.setAttribute("GRN_NO",UtilFunctions.getString(rsTmp,"GRN_NO",""));
                            objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"GRN_DATE","0000-00-00")));
                            objItem.setAttribute("MODULE_ID",UtilFunctions.getInt(rsTmp,"MODULE_ID",0));
                            objItem.setAttribute("REF_COMPANY_ID",UtilFunctions.getInt(rsTmp,"REF_COMPANY_ID",0));
                            objItem.setAttribute("LINK_NO",UtilFunctions.getString(rsTmp,"LINK_NO",""));
                            objItem.setAttribute("MATCHED",UtilFunctions.getInt(rsTmp,"MATCHED",0));
                            objItem.setAttribute("MATCHED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MATCHED_DATE","0000-00-00")));
                            objItem.setAttribute("REF_SR_NO",UtilFunctions.getInt(rsTmp,"REF_SR_NO",0));
                            DeductionAmount=0;
                            
                            //*************** Find Direct Deductions for this Invoice ******************//
                            //rsDeduction=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND INVOICE_NO='"+InvoiceNo+"' AND EFFECT='C' AND IS_DEDUCTION=1 ",FinanceGlobal.FinURL);
                            rsDeduction=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND REF_SR_NO="+VoucherSrNo+" AND EFFECT='C' AND IS_DEDUCTION=1 ",FinanceGlobal.FinURL);
                            rsDeduction.first();
                            
                            if(rsDeduction.getRow()>0) {
                                while(!rsDeduction.isAfterLast()) {
                                    clsVoucherItem objDeductionItem = new clsVoucherItem();
                                    
                                    DeductionAmount+=UtilFunctions.getDouble(rsDeduction,"AMOUNT",0);
                                    
                                    objDeductionItem.setAttribute("DOC_TYPE",1); //Direct Deduction
                                    objDeductionItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsDeduction,"COMPANY_ID", 0));
                                    objDeductionItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsDeduction,"VOUCHER_NO",""));
                                    objDeductionItem.setAttribute("SR_NO",UtilFunctions.getInt(rsDeduction,"SR_NO",0));
                                    objDeductionItem.setAttribute("EFFECT",UtilFunctions.getString(rsDeduction,"EFFECT",""));
                                    objDeductionItem.setAttribute("ACCOUNT_ID",UtilFunctions.getInt(rsDeduction,"ACCOUNT_ID",0));
                                    objDeductionItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsDeduction,"MAIN_ACCOUNT_CODE","").trim());
                                    objDeductionItem.setAttribute("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsDeduction,"SUB_ACCOUNT_CODE","").trim());
                                    objDeductionItem.setAttribute("AMOUNT",UtilFunctions.getDouble(rsDeduction,"AMOUNT",0));
                                    objDeductionItem.setAttribute("APPLICABLE_AMOUNT",UtilFunctions.getDouble(rsDeduction,"APPLICABLE_AMOUNT",0));
                                    objDeductionItem.setAttribute("PERCENTAGE",UtilFunctions.getDouble(rsDeduction,"PERCENTAGE",0));
                                    objDeductionItem.setAttribute("REMARKS",UtilFunctions.getString(rsDeduction,"REMARKS",""));
                                    objDeductionItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsDeduction,"CREATED_BY",""));
                                    objDeductionItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"CREATED_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsDeduction,"MODIFIED_BY",""));
                                    objDeductionItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"MODIFIED_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("CHANGED",true);
                                    objDeductionItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"CHANGED_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("CANCELLED",0);
                                    objDeductionItem.setAttribute("PO_NO",UtilFunctions.getString(rsDeduction,"PO_NO",""));
                                    objDeductionItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"PO_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsDeduction,"INVOICE_NO",""));
                                    objDeductionItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"INVOICE_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VALUE_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("GRN_NO",UtilFunctions.getString(rsDeduction,"GRN_NO",""));
                                    objDeductionItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"GRN_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("MODULE_ID",UtilFunctions.getInt(rsDeduction,"MODULE_ID",0));
                                    objDeductionItem.setAttribute("INVOICE_AMOUNT",UtilFunctions.getDouble(rsDeduction,"INVOICE_AMOUNT",0));
                                    objDeductionItem.setAttribute("IS_DEDUCTION",UtilFunctions.getInt(rsDeduction,"IS_DEDUCTION",0));
                                    objDeductionItem.setAttribute("MATCHED",UtilFunctions.getInt(rsDeduction,"MATCHED",0));
                                    objDeductionItem.setAttribute("MATCHED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"MATCHED_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("REF_COMPANY_ID",UtilFunctions.getInt(rsDeduction,"REF_COMPANY_ID",0));
                                    objDeductionItem.setAttribute("IS_DEDUCTION",1);
                                    objDeductionItem.setAttribute("LINK_NO",UtilFunctions.getString(rsDeduction,"LINK_NO",""));
                                    objDeductionItem.setAttribute("REF_SR_NO",UtilFunctions.getInt(rsDeduction,"REF_SR_NO",0));
                                    objDeductions.put(Integer.toString(objDeductions.size()+1),objDeductionItem);
                                    rsDeduction.next();
                                }
                            }
                            //*************************** End of direct deduction **********************************//
                            
                            
                            //*************** Find Debit Note Deductions for this Invoice ******************//
                            rsDeduction=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL_DOCS WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND VOUCHER_SR_NO="+VoucherSrNo+" ",FinanceGlobal.FinURL);
                            rsDeduction.first();
                            
                            if(rsDeduction.getRow()>0) {
                                while(!rsDeduction.isAfterLast()) {
                                    clsVoucherItem objDeductionItem = new clsVoucherItem();
                                    
                                    DeductionAmount+=UtilFunctions.getDouble(rsDeduction,"AMOUNT",0);
                                    
                                    objDeductionItem.setAttribute("DOC_TYPE",UtilFunctions.getInt(rsDeduction,"DOC_TYPE",0)); //Debit Note Deduction
                                    objDeductionItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsDeduction,"COMPANY_ID", 0));
                                    objDeductionItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsDeduction,"VOUCHER_NO",""));
                                    objDeductionItem.setAttribute("VOUCHER_SR_NO",VoucherSrNo);
                                    objDeductionItem.setAttribute("SR_NO",UtilFunctions.getInt(rsDeduction,"SR_NO",0));
                                    objDeductionItem.setAttribute("DOC_NO",UtilFunctions.getString(rsDeduction,"DOC_NO",""));
                                    objDeductionItem.setAttribute("DOC_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"DOC_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("AMOUNT",UtilFunctions.getDouble(rsDeduction,"AMOUNT",0));
                                    objDeductionItem.setAttribute("REMARKS",UtilFunctions.getString(rsDeduction,"REMARKS",""));
                                    objDeductionItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsDeduction,"CREATED_BY",""));
                                    objDeductionItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"CREATED_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsDeduction,"MODIFIED_BY",""));
                                    objDeductionItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"MODIFIED_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("CHANGED",true);
                                    objDeductionItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"CHANGED_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("CANCELLED",0);
                                    
                                    objDeductions.put(Integer.toString(objDeductions.size()+1),objDeductionItem);
                                    rsDeduction.next();
                                }
                            }
                            //*************************** End of direct deduction **********************************//
                            
                            objItem.setAttribute("INVOICE_AMOUNT",UtilFunctions.getDouble(rsTmp,"INVOICE_AMOUNT",0)+DeductionAmount);
                            objItem.setAttribute("INVOICE_DEDUCTIONS",objDeductions);
                            objItem.setAttribute("DEDUCTION_AMOUNT",DeductionAmount);
                            objItem.setAttribute("IS_DEDUCTION",0);
                            //************************ End of Deduction Findings ******************//
                            
                            objPayment.colVoucherItems.put(Integer.toString(objPayment.colVoucherItems.size()+1) ,objItem);
                            
                            rsTmp.next();
                        }
                    }
                    
                    //Find Additions i.e. Bank Charges etc.
                    rsTmp=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND SR_NO="+strSrNo+" AND EFFECT='D' AND MAIN_ACCOUNT_CODE<>'' AND IS_DEDUCTION=2 ",FinanceGlobal.FinURL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        while(!rsTmp.isAfterLast()) {
                            
                            Counter=Counter+1;
                            clsVoucherItem objItem = new clsVoucherItem();
                            HashMap objDeductions=new HashMap();
                            
                            objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                            objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                            objItem.setAttribute("SR_NO",UtilFunctions.getInt(rsTmp,"SR_NO",0));
                            objItem.setAttribute("EFFECT",UtilFunctions.getString(rsTmp,"EFFECT",""));
                            objItem.setAttribute("ACCOUNT_ID",UtilFunctions.getInt(rsTmp,"ACCOUNT_ID",0));
                            objItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","").trim());
                            objItem.setAttribute("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"SUB_ACCOUNT_CODE","").trim());
                            
                            objItem.setAttribute("AMOUNT",UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                            
                            
                            ChequeAmount+=UtilFunctions.getDouble(rsTmp,"AMOUNT",0);
                            
                            
                            objItem.setAttribute("REMARKS",UtilFunctions.getString(rsTmp,"REMARKS",""));
                            objItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsTmp,"CREATED_BY",""));
                            objItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CREATED_DATE","0000-00-00")));
                            objItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsTmp,"MODIFIED_BY",""));
                            objItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MODIFIED_DATE","0000-00-00")));
                            objItem.setAttribute("CHANGED",true);
                            objItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CHANGED_DATE","0000-00-00")));
                            objItem.setAttribute("CANCELLED",0);
                            objItem.setAttribute("PO_NO",UtilFunctions.getString(rsTmp,"PO_NO",""));
                            objItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"PO_DATE","0000-00-00")));
                            objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                            objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                            objItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VALUE_DATE","0000-00-00")));
                            objItem.setAttribute("GRN_NO",UtilFunctions.getString(rsTmp,"GRN_NO",""));
                            objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"GRN_DATE","0000-00-00")));
                            objItem.setAttribute("MODULE_ID",UtilFunctions.getInt(rsTmp,"MODULE_ID",0));
                            objItem.setAttribute("REF_COMPANY_ID",UtilFunctions.getInt(rsTmp,"REF_COMPANY_ID",0));
                            objItem.setAttribute("INVOICE_AMOUNT",UtilFunctions.getDouble(rsTmp,"INVOICE_AMOUNT",0));
                            objItem.setAttribute("INVOICE_DEDUCTIONS",new HashMap());
                            objItem.setAttribute("DEDUCTION_AMOUNT",0);
                            objItem.setAttribute("IS_DEDUCTION",2);
                            objItem.setAttribute("LINK_NO",UtilFunctions.getString(rsTmp,"LINK_NO",""));
                            
                            //************************ End of Deduction Findings ******************//
                            
                            objPayment.colVoucherItems.put(Integer.toString(objPayment.colVoucherItems.size()+1) ,objItem);
                            
                            rsTmp.next();
                        }
                    }
                    rsInvoices.next();
                }
            }
            objPayment.setAttribute("CHEQUE_AMOUNT",ChequeAmount);
            
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean getReceiptRepresentation() {
        ResultSet rsTmp;
        ResultSet rsDeduction;
        Connection tmpConn;
        Statement tmpStmt;
        
        objPayment=new clsVoucher();
        objPayment.colVoucherItems.clear();
        objPayment.colVoucherItemsEx.clear();
        
        tmpConn=data.getConn(FinanceGlobal.FinURL);
        
        long Counter=0;
        int RevNo=0;
        int VoucherSrNo=0;
        double ChequeAmount=0;
        double DeductionAmount=0;
        
        try {
            
            int CompanyID=getAttribute("COMPANY_ID").getInt();
            String VoucherNo=getAttribute("VOUCHER_NO").getString();
            
            //General Retrieval of the Voucher
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
                objPayment.setAttribute("REVISION_NO",UtilFunctions.getInt(rsResultSet,"REVISION_NO",0));
            }
            else {
                objPayment.setAttribute("REVISION_NO",0);
            }
            
            OldVoucherNo=UtilFunctions.getString(rsResultSet,"VOUCHER_NO","");
            objPayment.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsResultSet,"COMPANY_ID",0));
            objPayment.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsResultSet,"VOUCHER_NO",""));
            objPayment.setAttribute("VOUCHER_TYPE",UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",1));
            objPayment.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"VOUCHER_DATE","0000-00-00")));
            objPayment.setAttribute("BOOK_CODE",UtilFunctions.getString(rsResultSet,"BOOK_CODE",""));
            objPayment.setAttribute("CHEQUE_NO",UtilFunctions.getString(rsResultSet,"CHEQUE_NO",""));
            objPayment.setAttribute("CHEQUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHEQUE_DATE","0000-00-00")));
            objPayment.setAttribute("REALIZATION_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"REALIZATION_DATE","0000-00-00")));
            objPayment.setAttribute("BANK_NAME",UtilFunctions.getString(rsResultSet,"BOOK_CODE",""));
            objPayment.setAttribute("ST_CATEGORY",UtilFunctions.getString(rsResultSet,"ST_CATEGORY",""));
            objPayment.setAttribute("REASON_CODE",UtilFunctions.getString(rsResultSet,"REASON_CODE",""));
            objPayment.setAttribute("REMARKS",UtilFunctions.getString(rsResultSet,"REMARKS",""));
            objPayment.setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsResultSet,"HIERARCHY_ID",0));
            objPayment.setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            objPayment.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00")));
            objPayment.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            objPayment.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00")));
            objPayment.setAttribute("CHANGED",UtilFunctions.getInt(rsResultSet,"CHANGED",0));
            objPayment.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHANGED_DATE","0000-00-00")));
            objPayment.setAttribute("APPROVED",UtilFunctions.getInt(rsResultSet,"APPROVED",0));
            objPayment.setAttribute("APPROVED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"APPROVED_DATE","0000-00-00")));
            objPayment.setAttribute("REJECTED",UtilFunctions.getInt(rsResultSet,"REJECTED",0));
            objPayment.setAttribute("REJECTED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"REJECTED_DATE","0000-00-00")));
            objPayment.setAttribute("CANCELLED",UtilFunctions.getInt(rsResultSet,"CANCELLED",0));
            objPayment.setAttribute("LEGACY_NO",UtilFunctions.getString(rsResultSet,"LEGACY_NO",""));
            objPayment.setAttribute("LINK_NO",UtilFunctions.getString(rsResultSet,"LINK_NO",""));
            objPayment.setAttribute("LEGACY_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"LEGACY_DATE","0000-00-00")));
            objPayment.setAttribute("EXCLUDE_IN_ADJ",UtilFunctions.getInt(rsResultSet,"EXCLUDE_IN_ADJ",0));
            objPayment.setAttribute("CUSTOMER_BANK",UtilFunctions.getString(rsResultSet,"CUSTOMER_BANK",""));
            
            String MainAccountCode=data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='C' AND MAIN_ACCOUNT_CODE<>'' AND IS_DEDUCTION=0 ",FinanceGlobal.FinURL);
            String BankCode=data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='D' AND MAIN_ACCOUNT_CODE<>'' AND IS_DEDUCTION=0 ",FinanceGlobal.FinURL);
            String PartyCode=data.getStringValueFromDB("SELECT SUB_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='C' AND MAIN_ACCOUNT_CODE<>'' AND SUB_ACCOUNT_CODE<>'' AND IS_DEDUCTION=0",FinanceGlobal.FinURL);
            
            if(BankCode.trim().equals("")) {
                MainAccountCode="";
                BankCode=data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='D' AND MAIN_ACCOUNT_CODE<>'' ",FinanceGlobal.FinURL);
                PartyCode=data.getStringValueFromDB("SELECT SUB_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='C' AND MAIN_ACCOUNT_CODE<>'' AND SUB_ACCOUNT_CODE<>'' ",FinanceGlobal.FinURL);
            }
            
            objPayment.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode);
            objPayment.setAttribute("SUB_ACCOUNT_CODE",PartyCode);
            objPayment.setAttribute("BANK_CODE",BankCode);
            objPayment.setAttribute("CHEQUE_AMOUNT",0);
            
            
            objPayment.colVoucherItems.clear();
            objPayment.colVoucherItemsEx.clear();
            
            //Find Unique Invoices
            ResultSet rsInvoices=data.getResult("SELECT SR_NO FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='C' " ,FinanceGlobal.FinURL);
            rsInvoices.first();
            
            if(rsInvoices.getRow()>0) {
                
                
                while(!rsInvoices.isAfterLast()) {
                    String strSrNo=Integer.toString(UtilFunctions.getInt(rsInvoices,"SR_NO",0));
                    
                    
                    Counter=0;
                    
                    //Find Debit Entries - Except Normal Deduction
                    rsTmp=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND SR_NO="+strSrNo+" AND EFFECT='C' AND MAIN_ACCOUNT_CODE<>'' AND IS_DEDUCTION=0 ",FinanceGlobal.FinURL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        while(!rsTmp.isAfterLast()) {
                            
                            VoucherSrNo=UtilFunctions.getInt(rsTmp,"SR_NO", 0);
                            
                            Counter=Counter+1;
                            clsVoucherItem objItem = new clsVoucherItem();
                            HashMap objDeductions=new HashMap();
                            
                            objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                            objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                            objItem.setAttribute("SR_NO",UtilFunctions.getInt(rsTmp,"SR_NO",0));
                            objItem.setAttribute("EFFECT",UtilFunctions.getString(rsTmp,"EFFECT",""));
                            objItem.setAttribute("ACCOUNT_ID",UtilFunctions.getInt(rsTmp,"ACCOUNT_ID",0));
                            objItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","").trim());
                            objItem.setAttribute("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"SUB_ACCOUNT_CODE","").trim());
                            
                            objItem.setAttribute("AMOUNT",UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                            objItem.setAttribute("INVOICE_AMOUNT",UtilFunctions.getDouble(rsTmp,"INVOICE_AMOUNT",0));
                            
                            ChequeAmount+=UtilFunctions.getDouble(rsTmp,"AMOUNT",0);
                            
                            objItem.setAttribute("REMARKS",UtilFunctions.getString(rsTmp,"REMARKS",""));
                            objItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsTmp,"CREATED_BY",""));
                            objItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CREATED_DATE","0000-00-00")));
                            objItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsTmp,"MODIFIED_BY",""));
                            objItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MODIFIED_DATE","0000-00-00")));
                            objItem.setAttribute("CHANGED",true);
                            objItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CHANGED_DATE","0000-00-00")));
                            objItem.setAttribute("CANCELLED",0);
                            objItem.setAttribute("PO_NO",UtilFunctions.getString(rsTmp,"PO_NO",""));
                            objItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"PO_DATE","0000-00-00")));
                            objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                            objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                            objItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VALUE_DATE","0000-00-00")));
                            objItem.setAttribute("GRN_NO",UtilFunctions.getString(rsTmp,"GRN_NO",""));
                            objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"GRN_DATE","0000-00-00")));
                            objItem.setAttribute("MODULE_ID",UtilFunctions.getInt(rsTmp,"MODULE_ID",0));
                            objItem.setAttribute("REF_COMPANY_ID",UtilFunctions.getInt(rsTmp,"REF_COMPANY_ID",0));
                            objItem.setAttribute("LINK_NO",UtilFunctions.getString(rsTmp,"LINK_NO",""));
                            objItem.setAttribute("MATCHED",UtilFunctions.getInt(rsTmp,"MATCHED",0));
                            objItem.setAttribute("MATCHED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MATCHED_DATE","0000-00-00")));
                            DeductionAmount=0;
                            
                            //*************** Find Direct Deductions for this Invoice ******************//
                            rsDeduction=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND REF_SR_NO="+VoucherSrNo+" AND EFFECT='D' AND IS_DEDUCTION=1 ",FinanceGlobal.FinURL);
                            rsDeduction.first();
                            
                            if(rsDeduction.getRow()>0) {
                                while(!rsDeduction.isAfterLast()) {
                                    clsVoucherItem objDeductionItem = new clsVoucherItem();
                                    
                                    DeductionAmount+=UtilFunctions.getDouble(rsDeduction,"AMOUNT",0);
                                    
                                    objDeductionItem.setAttribute("DOC_TYPE",1); //Direct Deduction
                                    objDeductionItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsDeduction,"COMPANY_ID", 0));
                                    objDeductionItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsDeduction,"VOUCHER_NO",""));
                                    objDeductionItem.setAttribute("SR_NO",UtilFunctions.getInt(rsDeduction,"SR_NO",0));
                                    objDeductionItem.setAttribute("EFFECT",UtilFunctions.getString(rsDeduction,"EFFECT",""));
                                    objDeductionItem.setAttribute("ACCOUNT_ID",UtilFunctions.getInt(rsDeduction,"ACCOUNT_ID",0));
                                    objDeductionItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsDeduction,"MAIN_ACCOUNT_CODE","").trim());
                                    objDeductionItem.setAttribute("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsDeduction,"SUB_ACCOUNT_CODE","").trim());
                                    objDeductionItem.setAttribute("AMOUNT",UtilFunctions.getDouble(rsDeduction,"AMOUNT",0));
                                    objDeductionItem.setAttribute("INVOICE_AMOUNT",UtilFunctions.getDouble(rsDeduction,"INVOICE_AMOUNT",0));
                                    objDeductionItem.setAttribute("REMARKS",UtilFunctions.getString(rsDeduction,"REMARKS",""));
                                    objDeductionItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsDeduction,"CREATED_BY",""));
                                    objDeductionItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"CREATED_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsDeduction,"MODIFIED_BY",""));
                                    objDeductionItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"MODIFIED_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("CHANGED",true);
                                    objDeductionItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"CHANGED_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("CANCELLED",0);
                                    objDeductionItem.setAttribute("PO_NO",UtilFunctions.getString(rsDeduction,"PO_NO",""));
                                    objDeductionItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"PO_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsDeduction,"INVOICE_NO",""));
                                    objDeductionItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"INVOICE_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"VALUE_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("GRN_NO",UtilFunctions.getString(rsDeduction,"GRN_NO",""));
                                    objDeductionItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"GRN_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("MODULE_ID",UtilFunctions.getInt(rsDeduction,"MODULE_ID",0));
                                    objDeductionItem.setAttribute("INVOICE_AMOUNT",UtilFunctions.getDouble(rsDeduction,"INVOICE_AMOUNT",0));
                                    objDeductionItem.setAttribute("IS_DEDUCTION",UtilFunctions.getInt(rsDeduction,"IS_DEDUCTION",0));
                                    objDeductionItem.setAttribute("MATCHED",UtilFunctions.getInt(rsDeduction,"MATCHED",0));
                                    objDeductionItem.setAttribute("MATCHED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"MATCHED_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("REF_COMPANY_ID",UtilFunctions.getInt(rsDeduction,"REF_COMPANY_ID",0));
                                    objDeductionItem.setAttribute("IS_DEDUCTION",1);
                                    objDeductionItem.setAttribute("LINK_NO",UtilFunctions.getString(rsDeduction,"LINK_NO",""));
                                    
                                    objDeductions.put(Integer.toString(objDeductions.size()+1),objDeductionItem);
                                    rsDeduction.next();
                                }
                            }
                            //*************************** End of direct deduction **********************************//
                            
                            
                            //*************** Find Debit Note Deductions for this Invoice ******************//
                            rsDeduction=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL_DOCS WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND VOUCHER_SR_NO="+VoucherSrNo+" ",FinanceGlobal.FinURL);
                            rsDeduction.first();
                            
                            if(rsDeduction.getRow()>0) {
                                while(!rsDeduction.isAfterLast()) {
                                    clsVoucherItem objDeductionItem = new clsVoucherItem();
                                    
                                    DeductionAmount+=UtilFunctions.getDouble(rsDeduction,"AMOUNT",0);
                                    
                                    objDeductionItem.setAttribute("DOC_TYPE",UtilFunctions.getInt(rsDeduction,"DOC_TYPE",0)); //Debit Note Deduction
                                    objDeductionItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsDeduction,"COMPANY_ID", 0));
                                    objDeductionItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsDeduction,"VOUCHER_NO",""));
                                    objDeductionItem.setAttribute("VOUCHER_SR_NO",VoucherSrNo);
                                    objDeductionItem.setAttribute("SR_NO",UtilFunctions.getInt(rsDeduction,"SR_NO",0));
                                    objDeductionItem.setAttribute("DOC_NO",UtilFunctions.getString(rsDeduction,"DOC_NO",""));
                                    objDeductionItem.setAttribute("DOC_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"DOC_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("AMOUNT",UtilFunctions.getDouble(rsDeduction,"AMOUNT",0));
                                    objDeductionItem.setAttribute("REMARKS",UtilFunctions.getString(rsDeduction,"REMARKS",""));
                                    objDeductionItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsDeduction,"CREATED_BY",""));
                                    objDeductionItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"CREATED_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsDeduction,"MODIFIED_BY",""));
                                    objDeductionItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"MODIFIED_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("CHANGED",true);
                                    objDeductionItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"CHANGED_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("CANCELLED",0);
                                    
                                    objDeductions.put(Integer.toString(objDeductions.size()+1),objDeductionItem);
                                    rsDeduction.next();
                                }
                            }
                            //*************************** End of direct deduction **********************************//
                            
                            
                            objItem.setAttribute("INVOICE_AMOUNT",UtilFunctions.getDouble(rsTmp,"INVOICE_AMOUNT",0)+DeductionAmount);
                            objItem.setAttribute("INVOICE_DEDUCTIONS",objDeductions);
                            objItem.setAttribute("DEDUCTION_AMOUNT",DeductionAmount);
                            objItem.setAttribute("IS_DEDUCTION",0);
                            //************************ End of Deduction Findings ******************//
                            
                            objPayment.colVoucherItems.put(Integer.toString(objPayment.colVoucherItems.size()+1) ,objItem);
                            
                            rsTmp.next();
                        }
                        
                    }
                    
                    
                    
                    //Find Additions i.e. Bank Charges etc.
                    rsTmp=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND SR_NO="+strSrNo+" AND EFFECT='D' AND MAIN_ACCOUNT_CODE<>'' AND IS_DEDUCTION=2 ",FinanceGlobal.FinURL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        while(!rsTmp.isAfterLast()) {
                            
                            Counter=Counter+1;
                            clsVoucherItem objItem = new clsVoucherItem();
                            HashMap objDeductions=new HashMap();
                            
                            objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                            objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                            objItem.setAttribute("SR_NO",UtilFunctions.getInt(rsTmp,"SR_NO",0));
                            objItem.setAttribute("EFFECT",UtilFunctions.getString(rsTmp,"EFFECT",""));
                            objItem.setAttribute("ACCOUNT_ID",UtilFunctions.getInt(rsTmp,"ACCOUNT_ID",0));
                            objItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","").trim());
                            objItem.setAttribute("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"SUB_ACCOUNT_CODE","").trim());
                            
                            objItem.setAttribute("AMOUNT",UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                            
                            ChequeAmount+=UtilFunctions.getDouble(rsTmp,"AMOUNT",0);
                            
                            objItem.setAttribute("REMARKS",UtilFunctions.getString(rsTmp,"REMARKS",""));
                            objItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsTmp,"CREATED_BY",""));
                            objItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CREATED_DATE","0000-00-00")));
                            objItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsTmp,"MODIFIED_BY",""));
                            objItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MODIFIED_DATE","0000-00-00")));
                            objItem.setAttribute("CHANGED",true);
                            objItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CHANGED_DATE","0000-00-00")));
                            objItem.setAttribute("CANCELLED",0);
                            objItem.setAttribute("PO_NO",UtilFunctions.getString(rsTmp,"PO_NO",""));
                            objItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"PO_DATE","0000-00-00")));
                            objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                            objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                            objItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VALUE_DATE","0000-00-00")));
                            objItem.setAttribute("GRN_NO",UtilFunctions.getString(rsTmp,"GRN_NO",""));
                            objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"GRN_DATE","0000-00-00")));
                            objItem.setAttribute("MODULE_ID",UtilFunctions.getInt(rsTmp,"MODULE_ID",0));
                            objItem.setAttribute("REF_COMPANY_ID",UtilFunctions.getInt(rsTmp,"REF_COMPANY_ID",0));
                            objItem.setAttribute("INVOICE_AMOUNT",UtilFunctions.getDouble(rsTmp,"INVOICE_AMOUNT",0));
                            objItem.setAttribute("INVOICE_DEDUCTIONS",new HashMap());
                            objItem.setAttribute("DEDUCTION_AMOUNT",0);
                            objItem.setAttribute("IS_DEDUCTION",2);
                            objItem.setAttribute("LINK_NO",UtilFunctions.getString(rsTmp,"LINK_NO",""));
                            //************************ End of Deduction Findings ******************//
                            
                            objPayment.colVoucherItems.put(Integer.toString(objPayment.colVoucherItems.size()+1) ,objItem);
                            
                            rsTmp.next();
                        }
                        
                    }
                    
                    
                    rsInvoices.next();
                }
            }
            
            
            objPayment.setAttribute("CHEQUE_AMOUNT",ChequeAmount);
            
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
        
    }
    
    
    
    public static HashMap getPendingApprovals(int CompanyID,int UserID,int Order, int VoucherModuleID,int FinYearFrom) {
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
                strSQL="SELECT FINANCE.D_FIN_VOUCHER_HEADER.VOUCHER_NO,FINANCE.D_FIN_VOUCHER_HEADER.VOUCHER_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FIN_VOUCHER_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_VOUCHER_HEADER.VOUCHER_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FIN_VOUCHER_HEADER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FIN_VOUCHER_HEADER.COMPANY_ID="+CompanyID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND FINANCE.D_FIN_VOUCHER_HEADER.APPROVED=0 AND FINANCE.D_FIN_VOUCHER_HEADER.CANCELLED=0 AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+VoucherModuleID+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT FINANCE.D_FIN_VOUCHER_HEADER.VOUCHER_NO,FINANCE.D_FIN_VOUCHER_HEADER.VOUCHER_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FIN_VOUCHER_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_VOUCHER_HEADER.VOUCHER_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FIN_VOUCHER_HEADER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FIN_VOUCHER_HEADER.COMPANY_ID="+CompanyID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND FINANCE.D_FIN_VOUCHER_HEADER.APPROVED=0 AND FINANCE.D_FIN_VOUCHER_HEADER.CANCELLED=0 AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+VoucherModuleID+" ORDER BY D_FIN_VOUCHER_HEADER.VOUCHER_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT FINANCE.D_FIN_VOUCHER_HEADER.VOUCHER_NO,FINANCE.D_FIN_VOUCHER_HEADER.VOUCHER_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FIN_VOUCHER_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_VOUCHER_HEADER.VOUCHER_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FIN_VOUCHER_HEADER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FIN_VOUCHER_HEADER.COMPANY_ID="+CompanyID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND FINANCE.D_FIN_VOUCHER_HEADER.APPROVED=0 AND FINANCE.D_FIN_VOUCHER_HEADER.CANCELLED=0 AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+VoucherModuleID+" ORDER BY D_FIN_VOUCHER_HEADER.VOUCHER_NO";
            }
            
            rsTmp3=tmpStmt3.executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp3.next()) {
                if(EITLERPGLOBAL.isWithinDate(rsTmp3.getString("VOUCHER_DATE"),FinYearFrom)) {
                    Counter=Counter+1;
                    clsVoucher ObjDoc=new clsVoucher();
                    
                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("VOUCHER_NO",rsTmp3.getString("VOUCHER_NO"));
                    ObjDoc.setAttribute("VOUCHER_DATE",rsTmp3.getString("VOUCHER_DATE"));
                    ObjDoc.setAttribute("RECEIVED_DATE",rsTmp3.getString("RECEIVED_DATE"));
                    String GRNNo=data.getStringValueFromDB("SELECT GRN_NO FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+rsTmp3.getString("VOUCHER_NO")+"'",FinanceGlobal.FinURL);
                    ObjDoc.setAttribute("GRN_NO",GRNNo);
                    // ----------------- End of Header Fields ------------------------------------//
                    
                    String PartyCode=data.getStringValueFromDB("SELECT SUB_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+rsTmp3.getString("VOUCHER_NO")+"' AND SUB_ACCOUNT_CODE<>'' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID,FinanceGlobal.FinURL);
                    String MainCode =data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+rsTmp3.getString("VOUCHER_NO")+"' AND SUB_ACCOUNT_CODE<>'' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID,FinanceGlobal.FinURL);
                    String PartyName=clsAccount.getAccountName(MainCode,PartyCode);
                    
                    //String PartyName=data.getStringValueFromDB("SELECT PARTY_NAME FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+PartyCode+"'",FinanceGlobal.FinURL);
                    
                    ObjDoc.setAttribute("PARTY_CODE",PartyCode);
                    ObjDoc.setAttribute("PARTY_NAME",PartyName);
                    
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
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FIN_VOUCHER_HEADER_H WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+DocNo+"'");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM D_FIN_VOUCHER_HEADER_H WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+DocNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                
                while(!rsTmp.isAfterLast()) {
                    clsVoucher objVoucher=new clsVoucher();
                    
                    objVoucher.setAttribute("VOUCHER_NO",rsTmp.getString("VOUCHER_NO"));
                    objVoucher.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(rsTmp.getString("VOUCHER_DATE")));
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
    
    public static boolean CanCancel(int CompanyID,String DocNo,int VoucherModuleID) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            if(VoucherModuleID==68) {
                rsTmp=data.getResult("SELECT VOUCHER_NO,VOUCHER_TYPE FROM D_FIN_VOUCHER_HEADER WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+DocNo+"' AND CANCELLED=0",FinanceGlobal.FinURL);
                rsTmp.first();
            } else if(VoucherModuleID==94) {
                rsTmp=data.getResult("SELECT VOUCHER_NO,VOUCHER_TYPE FROM D_FIN_VOUCHER_HEADER WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+DocNo+"' AND CANCELLED=0",FinanceGlobal.FinURL);
                rsTmp.first();
            } else {
                rsTmp=data.getResult("SELECT VOUCHER_NO,VOUCHER_TYPE FROM D_FIN_VOUCHER_HEADER WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+DocNo+"' AND APPROVED=0 AND CANCELLED=0",FinanceGlobal.FinURL);
                rsTmp.first();
            }
            
            if(rsTmp.getRow()>0) {
                int VoucherType=UtilFunctions.getInt(rsTmp,"VOUCHER_TYPE", 0);
                int ModuleID=clsVoucher.getVoucherModuleID(VoucherType);
                
                if(ModuleID==VoucherModuleID) {
                    canCancel=true;
                } else {
                    canCancel=false;
                }
            }
            rsTmp.close();
        } catch(Exception e) {
        }
        return canCancel;
    }
    
    public static boolean CancelDoc(int CompanyID,String DocNo,int VoucherModuleID) {
        
        ResultSet rsTmp=null,rsIndent=null;
        boolean Cancelled=false;
        
        try {
            if(CanCancel(CompanyID,DocNo,VoucherModuleID)) {
                
                boolean ApprovedDoc=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_FIN_VOUCHER_HEADER WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+DocNo+"'",FinanceGlobal.FinURL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"' AND MODULE_ID="+(VoucherModuleID));
                
                
                
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
    
    
    
    public boolean Validate(int EditMode) {
        try {
            if(EditMode==EITLERPGLOBAL.EDIT) {
                VoucherType = getVoucherType(getAttribute("VOUCHER_NO").getString());
            }
            if(getAttribute("APPROVAL_STATUS").getString().equals("F")) {
                if(getAttribute("BOOK_CODE").getString().equals("99")) {
                    LastError="Dummy Book code 99 is not allowed in Final Approval action. Please enter proper book code and final approve";
                    return false;
                }
            }
            
            if(getAttribute("BOOK_CODE").getString().equals("")) {
                LastError="Please enter book code";
                return false;
            }
            
            double CrTotal=0;
            double DrTotal=0;
            
            for(int i=1;i<=colVoucherItems.size();i++) {
                clsVoucherItem objItem=(clsVoucherItem)colVoucherItems.get(Integer.toString(i));
                
                if(objItem.getAttribute("EFFECT").getString().equals("C")) {
                    CrTotal=EITLERPGLOBAL.round(CrTotal+EITLERPGLOBAL.round(objItem.getAttribute("AMOUNT").getDouble(),2),2);
                }
                else {
                    DrTotal=EITLERPGLOBAL.round(DrTotal+EITLERPGLOBAL.round(objItem.getAttribute("AMOUNT").getDouble(),2),2);
                }
                
                String MainAccountCode=objItem.getAttribute("MAIN_ACCOUNT_CODE").getString().trim();
                String SubAccountCode=objItem.getAttribute("SUB_ACCOUNT_CODE").getString().trim();
                if(MainAccountCode.endsWith("000")) {
                    LastError = "This Main Account Code ("+MainAccountCode+") is not allowed to enter manually. Please Varify";
                    return false;
                }
                if(clsUser.IsFunctionGranted(getAttribute("COMPANY_ID").getInt(),EITLERPGLOBAL.gNewUserID,1052,10527)) {
                    DoNotValidateAccounts=true;
                }
                
                if(getAttribute("APPROVAL_STATUS").getString().equals("F")) {
                    DoNotValidateAccounts=false;
                }
                
                if(IsInternalPosting) {
                    DoNotValidateAccounts=true;
                }
                
                if(!IsInternalPosting) {
                    if(MainAccountCode.equals("115012") || MainAccountCode.equals("115160") || MainAccountCode.equals("115153") || MainAccountCode.equals("115029") || MainAccountCode.equals("115036") || MainAccountCode.equals("115177") || MainAccountCode.equals("115201") || MainAccountCode.equals("115225") || MainAccountCode.equals("115218")) {
                        LastError = "This Main Account Code is not allowed to enter manually. Please Varify";
                        //return false;
                    }
                }
                
                if(!DoNotValidateAccounts) {
                    String MainAccountCodeEx=EITLERPGLOBAL.SplitFirst(MainAccountCode, ".");
                    if(!clsAccount.IsValidAccount(MainAccountCodeEx,SubAccountCode)) {
                        LastError="Main Account Code/Sub Account Code is not valid. Please verify";
                        return false;
                    }
                }
                
                if((!SubAccountCode.trim().equals(""))&&MainAccountCode.trim().equals("")) {
                    LastError="Please specify Main Account code when you enter Sub account code";
                    return false;
                }
                
                
                //************ Validate document nos. (e.g. Debit Notes) attached with the item ***********//
                for(int j=1;j<=objItem.colVoucherDetailDocs.size();j++) {
                    clsVoucherItem objDocs=(clsVoucherItem)objItem.colVoucherDetailDocs.get(Integer.toString(j));
                    
                    if(objDocs.getAttribute("DOC_TYPE").getInt()==4) //If its debit note
                    {
                        if(EditMode==EITLERPGLOBAL.ADD) {
                            int VoucherCount=data.getIntValueFromDB("SELECT COUNT(DISTINCT(VOUCHER_NO)) FROM D_FIN_VOUCHER_DETAIL_DOCS WHERE VOUCHER_TYPE=3 AND DOC_NO='"+objDocs.getAttribute("DOC_NO").getString()+"'",FinanceGlobal.FinURL);
                            
                            if(VoucherCount>0) {
                                LastError="Debit note no. "+objDocs.getAttribute("DOC_NO").getString()+" already linked with other payment voucher. Please verify the input";
                                return false;
                            }
                        }
                        
                        if(EditMode==EITLERPGLOBAL.EDIT) {
                            int VoucherCount=data.getIntValueFromDB("SELECT COUNT(DISTINCT(VOUCHER_NO)) FROM D_FIN_VOUCHER_DETAIL_DOCS WHERE VOUCHER_TYPE=3 AND DOC_NO='"+objDocs.getAttribute("DOC_NO").getString()+"' AND VOUCHER_NO<>'"+getAttribute("VOUCHER_NO")+"' ",FinanceGlobal.FinURL);
                            
                            if(VoucherCount>0) {
                                LastError="Debit note no. "+objDocs.getAttribute("DOC_NO").getString()+" already linked with other payment voucher. Please verify the input";
                                return false;
                            }
                        }
                    }
                }
                //*****************************************************************************************//
                
                
                //************************************** Validate Ref. Document Nos. *********************************//
                String PONo=objItem.getAttribute("PO_NO").getString();
                String GRNNo=objItem.getAttribute("GRN_NO").getString();
                int ModuleID=objItem.getAttribute("MODULE_ID").getInt();
                int RefCompanyID=objItem.getAttribute("REF_COMPANY_ID").getInt();
                String CompanyURL=clsFinYear.getDBURL(RefCompanyID,EITLERPGLOBAL.FinYearFrom);
                
                
                if(!PONo.trim().equals("")) {
                    if(!data.IsRecordExist("SELECT PO_NO FROM D_PUR_PO_HEADER WHERE PO_NO='"+PONo+"' AND CANCELLED=0")) {
                        if(!data.IsRecordExist("SELECT PO_NO FROM D_PUR_PO_HEADER WHERE PO_NO='"+PONo+"' AND CANCELLED=0",CompanyURL)) {
                            LastError="PO No. "+PONo+" is not valid. Please check and correct it";
                            return false;
                        }
                    }
                }
                
                if(!GRNNo.trim().equals("")) {
                    
                    if(ModuleID==7) //GRN General
                    {
                        if(!data.IsRecordExist("SELECT GRN_NO FROM D_INV_GRN_HEADER WHERE GRN_NO='"+GRNNo+"' AND APPROVED=1 AND CANCELLED=0",CompanyURL)) {
                            LastError="GRN No. "+GRNNo+" is not valid. Please check and correct it";
                            return false;
                        }
                    }
                    
                    if(ModuleID==8) //GRN Raw Material
                    {
                        if(!data.IsRecordExist("SELECT GRN_NO FROM D_INV_GRN_HEADER WHERE GRN_NO='"+GRNNo+"' AND APPROVED=1 AND CANCELLED=0",CompanyURL)) {
                            LastError="GRN No. "+GRNNo+" is not valid. Please check and correct it";
                            return false;
                        }
                    }
                    
                    if(ModuleID==48) //Jobwork Entry
                    {
                        if(!data.IsRecordExist("SELECT JOB_NO FROM D_INV_JOB_HEADER WHERE JOB_NO='"+GRNNo+"' AND CANCELLED=0",CompanyURL)) {
                            LastError="Jobwork Entry No. "+GRNNo+" is not valid. Please check and correct it";
                            return false;
                        }
                    }
                    
                    if(ModuleID==5 && getAttribute("VOUCHER_TYPE").getInt()==FinanceGlobal.TYPE_CASH_VOUCHER) //MIR General
                    {
                        if(!data.IsRecordExist("SELECT MIR_NO FROM D_INV_MIR_HEADER WHERE MIR_NO='"+GRNNo+"' AND CANCELLED=0",CompanyURL)) {
                            LastError="MIR No. "+GRNNo+" is not valid. Please check and correct it";
                            return false;
                        }
                    }
                    
                    if(ModuleID==6 && getAttribute("VOUCHER_TYPE").getInt()==FinanceGlobal.TYPE_CASH_VOUCHER) //MIR Raw Material
                    {
                        if(!data.IsRecordExist("SELECT MIR_NO FROM D_INV_MIR_HEADER WHERE MIR_NO='"+GRNNo+"' AND CANCELLED=0",CompanyURL)) {
                            LastError="MIR No. "+GRNNo+" is not valid. Please check and correct it";
                            return false;
                        }
                    }
                }
                //****************************************************************************************************//
                
                
            }
            
            if(EITLERPGLOBAL.round(CrTotal,2)!=EITLERPGLOBAL.round(DrTotal,2)) {
                if(DoNotValidateAccounts) {
                    
                }
                else {
                    LastError="Credit and Debit total doesn't match. Please verify the entry";
                    return false;
                }
            }
            
            
            //*** ===== CHECK THE EXCESS PAYMENT ==== ***//
            
            
            
            //======================================================================================//
            // ******************** New Logic of Separation/Splitting Entries ********************* //
            //======================================================================================//
            String validEntryMessage="";
            
            validEntryMessage="Invalid Entry Sequence. Please specify valid sequence as example shown below";
            validEntryMessage+="\n1. Dr.  Ac. 1   50 ";
            validEntryMessage+="\n2. Dr.  Ac. 2   50 ";
            validEntryMessage+="\n3.      Cr.  Ac. 3   100 ";
            validEntryMessage+="\n4. Dr.  Ac. 3   20 ";
            validEntryMessage+="\n5.      Cr.  Ac. 5   20 ";
            
            BlockCounter=0;
            CrTotal=0;DrTotal=0;
            colVoucherItemsEx.clear();
            
            String CurrentEffect="",PreviousEffect="";
            
            HashMap sEntries=new HashMap();
            
            for(int i=1;i<=colVoucherItems.size();i++) {
                clsVoucherItem objItem=(clsVoucherItem)colVoucherItems.get(Integer.toString(i));
                CurrentEffect=objItem.getAttribute("EFFECT").getString();
                
                if(sEntries.size()==0) {
                    sEntries.put(Integer.toString(sEntries.size()+1), objItem);
                    
                    if(CurrentEffect.equals("C")) {
                        CrTotal+=objItem.getAttribute("AMOUNT").getDouble();
                    }
                    else {
                        DrTotal+=objItem.getAttribute("AMOUNT").getDouble();
                    }
                }
                else {
                    if(!CurrentEffect.equals(PreviousEffect)) {
                        if(CurrentEffect.equals("C")) {
                            double CurrentAmount=EITLERPGLOBAL.round(objItem.getAttribute("AMOUNT").getDouble(),2);
                            
                            DrTotal=EITLERPGLOBAL.round(DrTotal,2);
                            
                            if(DrTotal!=CurrentAmount) {
                                //Do not perform this validation in case of Journal Vouchers & Sales Journal Vouchers
                                int VType_Journal =FinanceGlobal.TYPE_JOURNAL;
                                int Type_SalesJournal = FinanceGlobal.TYPE_SALES_JOURNAL;
                                int Type_CreditNote = FinanceGlobal.TYPE_CREDIT_NOTE;
                                VoucherType = getAttribute("VOUCHER_TYPE").getInt();
                                if ((VoucherType!=VType_Journal) && (VoucherType!=Type_SalesJournal) && (VoucherType!=Type_CreditNote)) {
                                    LastError=validEntryMessage;
                                    return false;
                                }
                            }
                            else {
                                sEntries.put(Integer.toString(sEntries.size()+1),objItem);
                                
                                
                                if(!ValidateBlock(sEntries)) {
                                    return false;
                                }
                                
                                //PASS ON TO SPLIT ROUTINE. FROM HERE
                                //SplitEntries(sEntries);
                                
                                //Now Clear it
                                sEntries.clear();
                                CrTotal=0;DrTotal=0;
                            }
                        }
                        else {
                            
                            double CurrentAmount=EITLERPGLOBAL.round(objItem.getAttribute("AMOUNT").getDouble(),2);
                            CrTotal=EITLERPGLOBAL.round(CrTotal,2);
                            
                            if(CrTotal!=CurrentAmount) {
                                //Do not perform this validation in case of Journal Vouchers & Sales Journal Vouchers
                                int VType_Journal =FinanceGlobal.TYPE_JOURNAL;
                                int Type_SalesJournal = FinanceGlobal.TYPE_SALES_JOURNAL;
                                int Type_CreditNote = FinanceGlobal.TYPE_CREDIT_NOTE;
                                if ((VoucherType!=VType_Journal) && (VoucherType!=Type_SalesJournal) && (VoucherType!=Type_CreditNote)) {
                                    LastError=validEntryMessage;
                                    return false;
                                }
                            }
                            else {
                                sEntries.put(Integer.toString(sEntries.size()+1),objItem);
                                
                                //Block must be validate even in case of journal vouchers
                                if(!ValidateBlock(sEntries)) {
                                    return false;
                                }
                                
                                //PASS ON TO SPLIT ROUTINE. FROM HERE
                                //SplitEntries(sEntries);
                                
                                //Now Clear it
                                sEntries.clear();
                                CrTotal=0;DrTotal=0;
                            }
                            
                        }
                    }
                    else {
                        //STACK UP THE ENTRY
                        sEntries.put(Integer.toString(sEntries.size()+1),objItem);
                        
                        if(objItem.getAttribute("EFFECT").getString().equals("C")) {
                            CrTotal+=objItem.getAttribute("AMOUNT").getDouble();
                        }
                        else {
                            DrTotal+=objItem.getAttribute("AMOUNT").getDouble();
                        }
                    }
                }
                
                PreviousEffect=objItem.getAttribute("EFFECT").getString();
            }
            //***************************** Entry Sequence Validation Completed ******************//
            
            
            
            
            return true;
        }
        catch(Exception e) {
            return false;
        }
        
        
    }
    
    
    private void SplitEntries(HashMap List) {
        try {
            
            int DebitCount=0,CreditCount=0;
            
            for(int i=1;i<=List.size();i++) {
                clsVoucherItem objItem=(clsVoucherItem)List.get(Integer.toString(i));
                
                if(objItem.getAttribute("EFFECT").getString().equals("C")) {
                    CreditCount++;
                }
                else {
                    DebitCount++;
                }
            }
            
            //******* Splitting Entries *******//
            if(DebitCount==CreditCount) {
                
                BlockCounter++;
                
                for(int i=1;i<=List.size();i++) {
                    clsVoucherItem objItem=(clsVoucherItem) List.get(Integer.toString(i));
                    
                    clsVoucherItem newItem=new clsVoucherItem();
                    
                    
                    newItem.setAttribute("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                    newItem.setAttribute("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
                    newItem.setAttribute("SR_NO",i);
                    newItem.setAttribute("BLOCK_NO",BlockCounter);
                    newItem.setAttribute("EFFECT",objItem.getAttribute("EFFECT").getString());
                    newItem.setAttribute("ACCOUNT_ID",objItem.getAttribute("ACCOUNT_ID").getInt());
                    String MainAccountCode= EITLERPGLOBAL.SplitFirst(objItem.getAttribute("MAIN_ACCOUNT_CODE").getString().trim(),".");
                    newItem.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode);
                    newItem.setAttribute("SUB_ACCOUNT_CODE",objItem.getAttribute("SUB_ACCOUNT_CODE").getString().trim());
                    newItem.setAttribute("AMOUNT",objItem.getAttribute("AMOUNT").getDouble());
                    newItem.setAttribute("APPLICABLE_AMOUNT",objItem.getAttribute("APPLICABLE_AMOUNT").getDouble());
                    newItem.setAttribute("PERCENTAGE",objItem.getAttribute("PERCENTAGE").getDouble());
                    newItem.setAttribute("REMARKS",objItem.getAttribute("REMARKS").getString());
                    newItem.setAttribute("CREATED_BY",EITLERPGLOBAL.gLoginID);
                    newItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    newItem.setAttribute("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                    newItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    newItem.setAttribute("CHANGED",true);
                    newItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    newItem.setAttribute("CANCELLED",0);
                    
                    newItem.setAttribute("PO_NO",objItem.getAttribute("PO_NO").getString());
                    newItem.setAttribute("PO_DATE",objItem.getAttribute("PO_DATE").getString());
                    newItem.setAttribute("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                    newItem.setAttribute("INVOICE_DATE",objItem.getAttribute("INVOICE_DATE").getString());
                    newItem.setAttribute("VALUE_DATE",objItem.getAttribute("VALUE_DATE").getString());
                    newItem.setAttribute("GRN_NO",objItem.getAttribute("GRN_NO").getString());
                    newItem.setAttribute("GRN_DATE",objItem.getAttribute("GRN_DATE").getString());
                    newItem.setAttribute("MODULE_ID",objItem.getAttribute("MODULE_ID").getInt());
                    newItem.setAttribute("IS_DEDUCTION",objItem.getAttribute("IS_DEDUCTION").getInt());
                    newItem.setAttribute("REF_COMPANY_ID",objItem.getAttribute("REF_COMPANY_ID").getInt());
                    try {
                        newItem.setAttribute("REF_VOUCHER_NO",objItem.getAttribute("REF_VOUCHER_NO").getString());
                        newItem.setAttribute("REF_VOUCHER_TYPE",objItem.getAttribute("REF_VOUCHER_TYPE").getInt());
                        newItem.setAttribute("REF_VOUCHER_COMPANY_ID",objItem.getAttribute("REF_VOUCHER_COMPANY_ID").getInt());
                        newItem.setAttribute("REF_SR_NO",objItem.getAttribute("REF_SR_NO").getInt());
                    }catch(Exception e) {}
                    if(objItem.getAttribute("IS_DEDUCTION").getInt() == 1) {
                        newItem.setAttribute("REF_SR_NO",objItem.getAttribute("REF_SR_NO").getInt());
                    } else {
                        newItem.setAttribute("REF_SR_NO",0);
                    }
                    try {
                        newItem.setAttribute("MATCHED_DATE",objItem.getAttribute("MATCHED_DATE").getString());
                        newItem.setAttribute("MATCHED",objItem.getAttribute("MATCHED").getInt());
                    } catch (Exception e) {}
                    newItem.setAttribute("LINK_NO",objItem.getAttribute("LINK_NO").getString());
                    colVoucherItemsEx.put(Integer.toString(colVoucherItemsEx.size()+1),newItem);
                }
            }
            
            
            if(DebitCount>CreditCount) {
                clsVoucherItem objCreditItem=new clsVoucherItem();
                
                for(int i=1;i<=List.size();i++) {
                    clsVoucherItem objItem=(clsVoucherItem) List.get(Integer.toString(i));
                    if(objItem.getAttribute("EFFECT").getString().equals("C")) {
                        objCreditItem=(clsVoucherItem) List.get(Integer.toString(i));
                    }
                }
                
                int Counter=0;
                
                for(int i=1;i<=List.size();i++) {
                    clsVoucherItem objItem=(clsVoucherItem) List.get(Integer.toString(i));
                    if(objItem.getAttribute("EFFECT").getString().equals("D")) {
                        
                        double Amount=objItem.getAttribute("AMOUNT").getDouble();
                        
                        Counter++;
                        BlockCounter++;
                        
                        clsVoucherItem newItem=new clsVoucherItem();
                        
                        newItem.setAttribute("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                        newItem.setAttribute("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
                        newItem.setAttribute("SR_NO",Counter);
                        newItem.setAttribute("BLOCK_NO",BlockCounter);
                        newItem.setAttribute("EFFECT",objItem.getAttribute("EFFECT").getString());
                        newItem.setAttribute("ACCOUNT_ID",objItem.getAttribute("ACCOUNT_ID").getInt());
                        String MainAccountCode= EITLERPGLOBAL.SplitFirst(objItem.getAttribute("MAIN_ACCOUNT_CODE").getString().trim(),".");
                        newItem.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode);
                        newItem.setAttribute("SUB_ACCOUNT_CODE",objItem.getAttribute("SUB_ACCOUNT_CODE").getString().trim());
                        newItem.setAttribute("AMOUNT",objItem.getAttribute("AMOUNT").getDouble());
                        newItem.setAttribute("APPLICABLE_AMOUNT",objItem.getAttribute("APPLICABLE_AMOUNT").getDouble());
                        newItem.setAttribute("PERCENTAGE",objItem.getAttribute("PERCENTAGE").getDouble());
                        newItem.setAttribute("REMARKS",objItem.getAttribute("REMARKS").getString());
                        newItem.setAttribute("CREATED_BY",EITLERPGLOBAL.gLoginID);
                        newItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        newItem.setAttribute("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                        newItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        newItem.setAttribute("CHANGED",true);
                        newItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        newItem.setAttribute("CANCELLED",0);
                        
                        newItem.setAttribute("PO_NO",objItem.getAttribute("PO_NO").getString());
                        newItem.setAttribute("PO_DATE",objItem.getAttribute("PO_DATE").getString());
                        newItem.setAttribute("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                        newItem.setAttribute("INVOICE_DATE",objItem.getAttribute("INVOICE_DATE").getString());
                        newItem.setAttribute("VALUE_DATE",objItem.getAttribute("VALUE_DATE").getString());
                        newItem.setAttribute("GRN_NO",objItem.getAttribute("GRN_NO").getString());
                        newItem.setAttribute("GRN_DATE",objItem.getAttribute("GRN_DATE").getString());
                        newItem.setAttribute("MODULE_ID",objItem.getAttribute("MODULE_ID").getInt());
                        
                        newItem.setAttribute("IS_DEDUCTION",objItem.getAttribute("IS_DEDUCTION").getInt());
                        newItem.setAttribute("REF_COMPANY_ID",objItem.getAttribute("REF_COMPANY_ID").getInt());
                        newItem.setAttribute("LINK_NO",objItem.getAttribute("LINK_NO").getString());
                        try {
                            newItem.setAttribute("MATCHED_DATE",objItem.getAttribute("MATCHED_DATE").getString());
                            newItem.setAttribute("MATCHED",objItem.getAttribute("MATCHED").getInt());
                        } catch (Exception e) {}
                        try {
                            newItem.setAttribute("REF_VOUCHER_NO",objItem.getAttribute("REF_VOUCHER_NO").getString());
                            newItem.setAttribute("REF_VOUCHER_TYPE",objItem.getAttribute("REF_VOUCHER_TYPE").getInt());
                            newItem.setAttribute("REF_VOUCHER_COMPANY_ID",objItem.getAttribute("REF_VOUCHER_COMPANY_ID").getInt());
                            newItem.setAttribute("REF_SR_NO",objItem.getAttribute("REF_SR_NO").getInt());
                        }catch(Exception e) {}
                        
                        colVoucherItemsEx.put(Integer.toString(colVoucherItemsEx.size()+1),newItem);
                        
                        Counter++;
                        
                        newItem=new clsVoucherItem();
                        
                        newItem.setAttribute("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                        newItem.setAttribute("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
                        newItem.setAttribute("SR_NO",Counter);
                        newItem.setAttribute("BLOCK_NO",BlockCounter);
                        newItem.setAttribute("EFFECT",objCreditItem.getAttribute("EFFECT").getString());
                        newItem.setAttribute("ACCOUNT_ID",objCreditItem.getAttribute("ACCOUNT_ID").getInt());
                        MainAccountCode= EITLERPGLOBAL.SplitFirst(objCreditItem.getAttribute("MAIN_ACCOUNT_CODE").getString().trim(),".");
                        newItem.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode);
                        newItem.setAttribute("SUB_ACCOUNT_CODE",objCreditItem.getAttribute("SUB_ACCOUNT_CODE").getString().trim());
                        newItem.setAttribute("AMOUNT",Amount);
                        newItem.setAttribute("APPLICABLE_AMOUNT",objItem.getAttribute("APPLICABLE_AMOUNT").getDouble());
                        newItem.setAttribute("PERCENTAGE",objItem.getAttribute("PERCENTAGE").getDouble());
                        newItem.setAttribute("REMARKS",objCreditItem.getAttribute("REMARKS").getString());
                        newItem.setAttribute("CREATED_BY",EITLERPGLOBAL.gLoginID);
                        newItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        newItem.setAttribute("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                        newItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        newItem.setAttribute("CHANGED",true);
                        newItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        newItem.setAttribute("CANCELLED",0);
                        newItem.setAttribute("PO_NO",objItem.getAttribute("PO_NO").getString());
                        newItem.setAttribute("PO_DATE",objItem.getAttribute("PO_DATE").getString());
                        newItem.setAttribute("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                        newItem.setAttribute("INVOICE_DATE",objItem.getAttribute("INVOICE_DATE").getString());
                        newItem.setAttribute("VALUE_DATE",objItem.getAttribute("VALUE_DATE").getString());
                        newItem.setAttribute("GRN_NO",objItem.getAttribute("GRN_NO").getString());
                        newItem.setAttribute("GRN_DATE",objItem.getAttribute("GRN_DATE").getString());
                        newItem.setAttribute("MODULE_ID",objItem.getAttribute("MODULE_ID").getInt());
                        newItem.setAttribute("IS_DEDUCTION",objItem.getAttribute("IS_DEDUCTION").getInt());
                        newItem.setAttribute("REF_COMPANY_ID",objItem.getAttribute("REF_COMPANY_ID").getInt());
                        newItem.setAttribute("LINK_NO",objItem.getAttribute("LINK_NO").getString());
                        try {
                            newItem.setAttribute("MATCHED_DATE",objItem.getAttribute("MATCHED_DATE").getString());
                            newItem.setAttribute("MATCHED",objItem.getAttribute("MATCHED").getInt());
                        } catch (Exception e) {}
                        try {
                            newItem.setAttribute("REF_VOUCHER_NO",objItem.getAttribute("REF_VOUCHER_NO").getString());
                            newItem.setAttribute("REF_VOUCHER_TYPE",objItem.getAttribute("REF_VOUCHER_TYPE").getInt());
                            newItem.setAttribute("REF_VOUCHER_COMPANY_ID",objItem.getAttribute("REF_VOUCHER_COMPANY_ID").getInt());
                            newItem.setAttribute("REF_SR_NO",objItem.getAttribute("REF_SR_NO").getInt());
                        }catch(Exception e) {}
                        
                        colVoucherItemsEx.put(Integer.toString(colVoucherItemsEx.size()+1),newItem);
                    }
                }
                
            }
            
            
            if(CreditCount>DebitCount) {
                
                clsVoucherItem objDebitItem=new clsVoucherItem();
                
                for(int i=1;i<=List.size();i++) {
                    clsVoucherItem objItem=(clsVoucherItem) List.get(Integer.toString(i));
                    if(objItem.getAttribute("EFFECT").getString().equals("D")) {
                        objDebitItem=(clsVoucherItem) List.get(Integer.toString(i));
                    }
                }
                
                int Counter=0;
                
                for(int i=1;i<=List.size();i++) {
                    clsVoucherItem objItem=(clsVoucherItem) List.get(Integer.toString(i));
                    if(objItem.getAttribute("EFFECT").getString().equals("C")) {
                        
                        double Amount=objItem.getAttribute("AMOUNT").getDouble();
                        
                        Counter++;
                        BlockCounter++;
                        
                        clsVoucherItem newItem=new clsVoucherItem();
                        
                        newItem.setAttribute("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                        newItem.setAttribute("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
                        newItem.setAttribute("SR_NO",Counter);
                        newItem.setAttribute("BLOCK_NO",BlockCounter);
                        newItem.setAttribute("EFFECT",objItem.getAttribute("EFFECT").getString());
                        newItem.setAttribute("ACCOUNT_ID",objItem.getAttribute("ACCOUNT_ID").getInt());
                        
                        String MainAccountCode= EITLERPGLOBAL.SplitFirst(objItem.getAttribute("MAIN_ACCOUNT_CODE").getString().trim(),".");
                        newItem.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode);
                        newItem.setAttribute("SUB_ACCOUNT_CODE",objItem.getAttribute("SUB_ACCOUNT_CODE").getString().trim());
                        newItem.setAttribute("AMOUNT",objItem.getAttribute("AMOUNT").getDouble());
                        newItem.setAttribute("APPLICABLE_AMOUNT",objItem.getAttribute("APPLICABLE_AMOUNT").getDouble());
                        newItem.setAttribute("PERCENTAGE",objItem.getAttribute("PERCENTAGE").getDouble());
                        newItem.setAttribute("REMARKS",objItem.getAttribute("REMARKS").getString());
                        newItem.setAttribute("CREATED_BY",EITLERPGLOBAL.gLoginID);
                        newItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        newItem.setAttribute("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                        newItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        newItem.setAttribute("CHANGED",true);
                        newItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        newItem.setAttribute("CANCELLED",0);
                        newItem.setAttribute("PO_NO",objItem.getAttribute("PO_NO").getString());
                        newItem.setAttribute("PO_DATE",objItem.getAttribute("PO_DATE").getString());
                        newItem.setAttribute("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                        newItem.setAttribute("INVOICE_DATE",objItem.getAttribute("INVOICE_DATE").getString());
                        newItem.setAttribute("VALUE_DATE",objItem.getAttribute("VALUE_DATE").getString());
                        newItem.setAttribute("GRN_NO",objItem.getAttribute("GRN_NO").getString());
                        newItem.setAttribute("GRN_DATE",objItem.getAttribute("GRN_DATE").getString());
                        newItem.setAttribute("MODULE_ID",objItem.getAttribute("MODULE_ID").getInt());
                        newItem.setAttribute("IS_DEDUCTION",objItem.getAttribute("IS_DEDUCTION").getInt());
                        newItem.setAttribute("REF_COMPANY_ID",objItem.getAttribute("REF_COMPANY_ID").getInt());
                        newItem.setAttribute("LINK_NO",objItem.getAttribute("LINK_NO").getString());
                        try {
                            newItem.setAttribute("MATCHED_DATE",objItem.getAttribute("MATCHED_DATE").getString());
                            newItem.setAttribute("MATCHED",objItem.getAttribute("MATCHED").getInt());
                        } catch (Exception e) {}
                        try {
                            newItem.setAttribute("REF_VOUCHER_NO",objItem.getAttribute("REF_VOUCHER_NO").getString());
                            newItem.setAttribute("REF_VOUCHER_TYPE",objItem.getAttribute("REF_VOUCHER_TYPE").getInt());
                            newItem.setAttribute("REF_VOUCHER_COMPANY_ID",objItem.getAttribute("REF_VOUCHER_COMPANY_ID").getInt());
                            newItem.setAttribute("REF_SR_NO",objItem.getAttribute("REF_SR_NO").getInt());
                        }catch(Exception e) {}
                        
                        colVoucherItemsEx.put(Integer.toString(colVoucherItemsEx.size()+1),newItem);
                        
                        
                        Counter++;
                        
                        newItem=new clsVoucherItem();
                        
                        newItem.setAttribute("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                        newItem.setAttribute("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
                        newItem.setAttribute("SR_NO",Counter);
                        newItem.setAttribute("BLOCK_NO",BlockCounter);
                        newItem.setAttribute("EFFECT",objDebitItem.getAttribute("EFFECT").getString());
                        newItem.setAttribute("ACCOUNT_ID",objDebitItem.getAttribute("ACCOUNT_ID").getInt());
                        MainAccountCode= EITLERPGLOBAL.SplitFirst(objDebitItem.getAttribute("MAIN_ACCOUNT_CODE").getString().trim(),".");
                        newItem.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode);
                        newItem.setAttribute("SUB_ACCOUNT_CODE",objDebitItem.getAttribute("SUB_ACCOUNT_CODE").getString().trim());
                        newItem.setAttribute("AMOUNT",Amount);
                        newItem.setAttribute("APPLICABLE_AMOUNT",objItem.getAttribute("APPLICABLE_AMOUNT").getDouble());
                        newItem.setAttribute("PERCENTAGE",objItem.getAttribute("PERCENTAGE").getDouble());
                        newItem.setAttribute("REMARKS",objDebitItem.getAttribute("REMARKS").getString());
                        newItem.setAttribute("CREATED_BY",EITLERPGLOBAL.gLoginID);
                        newItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        newItem.setAttribute("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                        newItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        newItem.setAttribute("CHANGED",true);
                        newItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        newItem.setAttribute("CANCELLED",0);
                        newItem.setAttribute("PO_NO",objItem.getAttribute("PO_NO").getString());
                        newItem.setAttribute("PO_DATE",objItem.getAttribute("PO_DATE").getString());
                        newItem.setAttribute("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                        newItem.setAttribute("INVOICE_DATE",objItem.getAttribute("INVOICE_DATE").getString());
                        newItem.setAttribute("VALUE_DATE",objItem.getAttribute("VALUE_DATE").getString());
                        newItem.setAttribute("GRN_NO",objItem.getAttribute("GRN_NO").getString());
                        newItem.setAttribute("GRN_DATE",objItem.getAttribute("GRN_DATE").getString());
                        newItem.setAttribute("MODULE_ID",objItem.getAttribute("MODULE_ID").getInt());
                        newItem.setAttribute("IS_DEDUCTION",objItem.getAttribute("IS_DEDUCTION").getInt());
                        newItem.setAttribute("REF_COMPANY_ID",objItem.getAttribute("REF_COMPANY_ID").getInt());
                        newItem.setAttribute("LINK_NO",objItem.getAttribute("LINK_NO").getString());
                        try {
                            newItem.setAttribute("MATCHED_DATE",objItem.getAttribute("MATCHED_DATE").getString());
                            newItem.setAttribute("MATCHED",objItem.getAttribute("MATCHED").getInt());
                        } catch (Exception e) {}
                        try {
                            newItem.setAttribute("REF_VOUCHER_NO",objItem.getAttribute("REF_VOUCHER_NO").getString());
                            newItem.setAttribute("REF_VOUCHER_TYPE",objItem.getAttribute("REF_VOUCHER_TYPE").getInt());
                            newItem.setAttribute("REF_VOUCHER_COMPANY_ID",objItem.getAttribute("REF_VOUCHER_COMPANY_ID").getInt());
                            newItem.setAttribute("REF_SR_NO",objItem.getAttribute("REF_SR_NO").getInt());
                        }catch(Exception e) {}
                        
                        colVoucherItemsEx.put(Integer.toString(colVoucherItemsEx.size()+1),newItem);
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private boolean ValidateBlock(HashMap List) {
        try {
            
            int VoucherType=getAttribute("VOUCHER_TYPE").getInt();
            
            if(VoucherType==FinanceGlobal.TYPE_SALES_JOURNAL||VoucherType==FinanceGlobal.TYPE_RECEIPT) {
                //Check the invoice no.
                for(int i=1;i<=List.size();i++) {
                    clsVoucherItem objItem=(clsVoucherItem)List.get(Integer.toString(i));
                    
                    String InvoiceNo=objItem.getAttribute("INVOICE_NO").getString();
                    String BookCode = getAttribute("BOOK_CODE").getString();
                    String MainCode=objItem.getAttribute("MAIN_ACCOUNT_CODE").getString().trim();
                    
                    if(!InvoiceNo.trim().equals("")) {
                        if (VoucherType==FinanceGlobal.TYPE_SALES_JOURNAL) {
                            BookCode = getAttribute("BOOK_CODE").getString();
                        }
                        else {
                            BookCode = data.getStringValueFromDB("SELECT BOOK_CODE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+objItem.getAttribute("GRN_NO").getString()+"'",FinanceGlobal.FinURL);
                        }
                        if (!BookCode.trim().equals("10") && (MainCode.equals("210027") || MainCode.equals("210010") || MainCode.equals("210072"))) {// ||!( || MainCode.equals("210072") || MainCode.equals("210010")) )) {
                            if(!data.IsRecordExist("SELECT INVOICE_NO FROM D_SAL_INVOICE_HEADER WHERE INVOICE_NO='"+InvoiceNo+"'")) {
                                LastError="Invalid invoice no. "+InvoiceNo+". Please correct the invoice no. and try again";
                                return false;
                            }
                        }
                    }
                }
            }
            
            
            
            //********* New Additional check: Do not allow to create payment voucher if PO is not approved ***************//
            if(VoucherType==FinanceGlobal.TYPE_PAYMENT||VoucherType==FinanceGlobal.TYPE_PAYMENT_2||(IsInternalPosting==false&&VoucherType==FinanceGlobal.TYPE_PJV)) {
                for(int i=1;i<=List.size();i++) {
                    clsVoucherItem objItem=(clsVoucherItem)List.get(Integer.toString(i));
                    
                    String PONo=objItem.getAttribute("PO_NO").getString();
                    int RefCompanyID=objItem.getAttribute("REF_COMPANY_ID").getInt();
                    String CompanyURL=clsFinYear.getDBURL(RefCompanyID,EITLERPGLOBAL.FinYearFrom);
                    
                    if(!PONo.trim().equals("")) {
                        if(!data.IsRecordExist("SELECT PO_NO FROM D_PUR_PO_HEADER WHERE PO_NO='"+PONo+"' AND APPROVED=1 AND CANCELLED=0",CompanyURL)) {
                            LastError="PO No. "+PONo+" is either not valid no or is not approved. You cannot create payment voucher of this PO";
                            return false;
                        }
                    }
                }
            }
            
            //***********************************************************************************************************//
            
            if(VoucherType==FinanceGlobal.TYPE_PJV||VoucherType==FinanceGlobal.TYPE_PAYMENT||VoucherType==FinanceGlobal.TYPE_DEBIT_NOTE||VoucherType==FinanceGlobal.TYPE_CASH_VOUCHER||VoucherType==FinanceGlobal.TYPE_PAYMENT_2) {
                
                //*** ===== CHECK THE EXCESS PAYMENT ==== ***//
                boolean PartyPaymentFound=false;
                double Payment=0;
                String PartyCode="";
                
                for(int i=1;i<=List.size();i++) {
                    clsVoucherItem objItem=(clsVoucherItem)List.get(Integer.toString(i));
                    
                    String MainAccountCode=objItem.getAttribute("MAIN_ACCOUNT_CODE").getString().trim();
                    String SubAccountCode=objItem.getAttribute("SUB_ACCOUNT_CODE").getString().trim();
                    String Effect=objItem.getAttribute("EFFECT").getString();
                    String Ref_Com_Id = objItem.getAttribute("REF_COMPANY_ID").getString();
                    //String Year = objItem.getAttribute("GRN_DATE").getString().substring(0,4);
                    
                    //String URL = data.getStringValueFromDB("SELECT DATABASE_URL FROM D_COM_FIN_YEAR WHERE COMPANY_ID=" + Ref_Com_Id +" AND YEAR_FROM= YEAR('"+ Year +"') ");
                    
                    if((!SubAccountCode.trim().equals("")) && Effect.equals("D")) {
                        PartyCode=SubAccountCode;
                        PartyPaymentFound=true;
                        Payment=objItem.getAttribute("AMOUNT").getDouble();
                        
                        String GRNNo=objItem.getAttribute("GRN_NO").getString();
                        String VoucherNo=getAttribute("VOUCHER_NO").getString();
                        int ModuleID=objItem.getAttribute("MODULE_ID").getInt();
                        
                        if(!GRNNo.trim().equals("")&&PartyPaymentFound) {
                            int RefCompanyID=objItem.getAttribute("REF_COMPANY_ID").getInt();
                            String CompanyURL=clsFinYear.getDBURL(RefCompanyID,EITLERPGLOBAL.FinYearFrom);
                            
                            //Expense Transaction Check
                            if(ModuleID==63) {
                                if(!clsExpenseTransaction.IsDocExist(EITLERPGLOBAL.gCompanyID,GRNNo)) {
                                    LastError="Expense Transaction No. "+GRNNo+" does not exist. Please check";
                                    return false;
                                }
                                else {
                                    double GRNAmount=EITLERPGLOBAL.round(clsExpenseTransaction.getExpenseAmount(EITLERPGLOBAL.gCompanyID,GRNNo),0);
                                    
                                    System.out.println(GRNNo);
                                    System.out.println(VoucherNo);
                                    
                                    String strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) AS PAID_AMOUNT FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B WHERE A.CANCELLED=0 AND A.VOUCHER_NO=B.VOUCHER_NO AND B.EFFECT='D' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND A.GRN_NO='"+GRNNo+"' AND A.MODULE_ID=63 AND A.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND A.VOUCHER_NO<>'"+VoucherNo+"'";
                                    double VoucherAmountPaid=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                                    
                                    VoucherAmountPaid+=Payment;
                                    System.out.println(VoucherAmountPaid);
                                    System.out.println(GRNAmount);
                                    
                                    
                                    VoucherAmountPaid=EITLERPGLOBAL.round(VoucherAmountPaid, 0);
                                    if(VoucherAmountPaid>(GRNAmount+1)) {
                                        if(JOptionPane.showConfirmDialog(null,"Total payment "+VoucherAmountPaid+" exceeds Excess Transaction Amount "+GRNAmount+".\nPlease check party outstanding statement for the details. Do you want to Continue ?","SDML ERP",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
                                            
                                        } else {
                                            LastError="Total payment "+VoucherAmountPaid+" exceeds Excess Transaction Amount "+GRNAmount+".\nPlease check party outstanding statement for the details";
                                            return false;
                                        }
                                    }
                                }
                            }
                            
                            
                            //General GRN - Module 7 Checks
                            if(ModuleID==7) {
                                
                                if(!clsGRNGen.IsGRNExist(GRNNo,CompanyURL)) {
                                    LastError="GRN No. "+GRNNo+" does not exist. Please check";
                                    return false;
                                }
                                else {
                                    double GRNAmount=EITLERPGLOBAL.round(clsGRNGen.getGRNTotalAmount(GRNNo,CompanyURL),0);
                                    
                                    String strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) AS PAID_AMOUNT FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B WHERE A.CANCELLED=0 AND A.VOUCHER_NO=B.VOUCHER_NO AND B.EFFECT='D' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND A.GRN_NO='"+GRNNo+"' AND A.MODULE_ID=7 AND A.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND A.VOUCHER_NO<>'"+VoucherNo+"'";
                                    double VoucherAmountPaid=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                                    
                                    VoucherAmountPaid+=Payment;
                                    
                                    VoucherAmountPaid=EITLERPGLOBAL.round(VoucherAmountPaid, 0);
                                    
                                    if(VoucherAmountPaid>(GRNAmount+1)) {
                                        if(JOptionPane.showConfirmDialog(null,"Total payment "+VoucherAmountPaid+" exceeds GRN Amount "+GRNAmount+".\nPlease check party outstanding statement for the details. Do you want to Continue ?","SDML ERP",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
                                            
                                        } else {
                                            LastError="Total payment "+VoucherAmountPaid+" exceeds GRN Amount "+GRNAmount+".\nPlease check party outstanding statement for the details";
                                            return false;
                                        }
                                    }
                                }
                            }
                            
                            //Raw Material GRN - Module 8 Checks
                            if(ModuleID==8) {
                                if(!clsGRN.IsGRNExist(GRNNo,CompanyURL)) {
                                    LastError="GRN No. "+GRNNo+" does not exist. Please check";
                                    return false;
                                }
                                else {
                                    double GRNAmount=EITLERPGLOBAL.round(clsGRNGen.getGRNTotalAmount(GRNNo,CompanyURL),0);
                                    
                                    String strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) AS PAID_AMOUNT FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B WHERE A.CANCELLED=0 AND A.VOUCHER_NO=B.VOUCHER_NO AND B.EFFECT='D' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND A.GRN_NO='"+GRNNo+"' AND A.MODULE_ID=8 AND A.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND A.VOUCHER_NO<>'"+VoucherNo+"'";
                                    double VoucherAmountPaid=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                                    
                                    VoucherAmountPaid+=Payment;
                                    
                                    VoucherAmountPaid=EITLERPGLOBAL.round(VoucherAmountPaid, 0);
                                    
                                    if(VoucherAmountPaid>(GRNAmount+1)) {
                                        if(JOptionPane.showConfirmDialog(null,"Total payment "+VoucherAmountPaid+" exceeds GRN Amount "+GRNAmount+".\nPlease check party outstanding statement for the details. Are you sure you want to Continue ?","SDML ERP",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
                                            
                                        } else {
                                            LastError="Total payment "+VoucherAmountPaid+" exceeds GRN Amount "+GRNAmount+".\nPlease check party outstanding statement for the details";
                                            return false;
                                        }
                                        
                                    }
                                    
                                }
                            }
                            
                            
                            //Job Entry - Module 48 Checks
                            if(ModuleID==48) {
                                
                                if(!clsJobwork.IsJobExist(GRNNo,CompanyURL)) {
                                    LastError="Job No. "+GRNNo+" does not exist. Please check";
                                    return false;
                                }
                                else {
                                    double GRNAmount=EITLERPGLOBAL.round(clsJobwork.getJobTotalAmount_vou(GRNNo,CompanyURL),0);
                                    
                                    String strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) AS PAID_AMOUNT FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B WHERE A.CANCELLED=0 AND A.VOUCHER_NO=B.VOUCHER_NO AND B.EFFECT='D' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND B.GRN_NO='"+GRNNo+"' AND B.MODULE_ID=48 AND A.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND A.VOUCHER_NO<>'"+VoucherNo+"'";
                                    double VoucherAmountPaid=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                                    
                                    VoucherAmountPaid+=Payment;
                                    
                                    VoucherAmountPaid=EITLERPGLOBAL.round(VoucherAmountPaid, 0);
                                    
                                    if(VoucherAmountPaid>(GRNAmount+1)) {
                                        if(JOptionPane.showConfirmDialog(null,"Total payment "+VoucherAmountPaid+" exceeds Job Entry Amount "+GRNAmount+".\nPlease check party outstanding statement for the details. Do you want to Continue ?","SDML ERP",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
                                            
                                        } else {
                                            LastError="Total payment "+VoucherAmountPaid+" exceeds Job Entry Amount "+GRNAmount+".\nPlease check party outstanding statement for the details";
                                            return false;
                                        }
                                    }
                                }
                            }
                        }
                        
                        //*********** Checking Advance Payment Terms ***************//
                        if(PartyPaymentFound&&GRNNo.trim().equals("")&&(ModuleID==0)) {
                            boolean AdvanceTermFound=false;
                            
                            //It's Advance Payment. Find the Term from the Party Master
                            HashMap Terms=clsSupplier.getPaymentTerms(PartyCode);
                            
                            for(int t=1;t<=Terms.size();t++) {
                                
                                int VoucherModuleID=getVoucherModuleID(getAttribute("VOUCHER_TYPE").getInt());
                                HashMap TermsList=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, VoucherModuleID, "ADVANCE_PAYMENT_VALIDATION", "TERM_CODE", (String)Terms.get(Integer.toString(t)));
                                
                                if(TermsList.size()>0) {
                                    clsApprovalRules objRule=(clsApprovalRules)TermsList.get(Integer.toString(1));
                                    int Allow=UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                                    if(Allow==1) {
                                        AdvanceTermFound=true;
                                    }
                                }
                            }
                            
                            if(!AdvanceTermFound) {
                                LastError="Cannot make advance payment, as party doesn't have advance payment terms in Supplier Registration";
                                //return false; !!!Ignored the condition on request of Mr. Chokshi -> 3 Feb. 2008 12:17 PM
                            }
                        }
                        //*********** Checking Advance Payment Terms Over ***************//
                    }
                }
                //===========================================//
            }
            
            if(VoucherType==FinanceGlobal.TYPE_CASH_VOUCHER) {
                double Payment = 0;
                for(int i=1;i<=List.size();i++) {
                    clsVoucherItem objItem=(clsVoucherItem)List.get(Integer.toString(i));
                    String MainAccountCode=objItem.getAttribute("MAIN_ACCOUNT_CODE").getString().trim();
                    String SubAccountCode=objItem.getAttribute("SUB_ACCOUNT_CODE").getString().trim();
                    String Effect=objItem.getAttribute("EFFECT").getString();
                    String Ref_Com_Id = objItem.getAttribute("REF_COMPANY_ID").getString();
                    Payment=objItem.getAttribute("AMOUNT").getDouble();
                    if(Effect.equals("D")) {
                        String GRNNo=objItem.getAttribute("GRN_NO").getString();
                        String VoucherNo=getAttribute("VOUCHER_NO").getString();
                        int ModuleID=objItem.getAttribute("MODULE_ID").getInt();
                        int RefCompanyID=objItem.getAttribute("REF_COMPANY_ID").getInt();
                        String CompanyURL=clsFinYear.getDBURL(RefCompanyID,EITLERPGLOBAL.FinYearFrom);
                        if(ModuleID==5) {
                            if(!clsMIRGen.IsMIRExistEx(GRNNo,CompanyURL,1)) {
                                LastError="MIR No. "+GRNNo+" does not exist. Please check";
                                return false;
                            }
                            else {
                                double MIRAmount=EITLERPGLOBAL.round(clsMIRGen.getFreightOtherCharges(GRNNo,CompanyURL,1),0);
                                
                                String strSQL="SELECT SUM(B.AMOUNT) AS PAID_AMOUNT FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B " +
                                "WHERE A.CANCELLED=0 AND A.VOUCHER_NO=B.VOUCHER_NO AND B.EFFECT='D' AND B.GRN_NO='"+GRNNo+"' " +
                                //          "AND B.MODULE_ID=5 AND A.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND A.VOUCHER_NO<>'"+VoucherNo+"'" ;
                                "AND B.MODULE_ID=5 AND A.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SUBSTRING(A.VOUCHER_NO,1,2) != 'PJ' AND A.VOUCHER_NO <>'"+VoucherNo+"'" ;
                                double VoucherAmountPaid=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                                
                                VoucherAmountPaid+=Payment;
                                
                                VoucherAmountPaid=EITLERPGLOBAL.round(VoucherAmountPaid, 0);
                                
                                System.out.println(VoucherAmountPaid);
                                System.out.println(MIRAmount);
                                if(VoucherAmountPaid>(MIRAmount+1)) {
                                    LastError="Total payment "+VoucherAmountPaid+" exceeds MIR Amount (Freight Amount + Other charges) "+MIRAmount+"." +
                                    "\nPlease check party outstanding statement for the details";
                                    return false;
                                }
                            }
                        }
                        
                        if(ModuleID==6) {
                            if(!clsMIRGen.IsMIRExistEx(GRNNo,CompanyURL,2)) {
                                LastError="MIR No. "+GRNNo+" does not exist. Please check";
                                return false;
                            }
                            else {
                                double MIRAmount=EITLERPGLOBAL.round(clsMIRGen.getFreightOtherCharges(GRNNo,CompanyURL,2),0);
                                
                                String strSQL="SELECT SUM(B.AMOUNT) AS PAID_AMOUNT FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B " +
                                "WHERE A.CANCELLED=0 AND A.VOUCHER_NO=B.VOUCHER_NO AND B.EFFECT='D' AND B.GRN_NO='"+GRNNo+"' " +
                                "AND B.MODULE_ID=6 AND A.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND A.VOUCHER_NO<>'"+VoucherNo+"' AND B.REF_COMPANY_ID="+EITLERPGLOBAL.gCompanyID;
                                double VoucherAmountPaid=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                                
                                VoucherAmountPaid+=Payment;
                                
                                VoucherAmountPaid=EITLERPGLOBAL.round(VoucherAmountPaid, 0);
                                System.out.println(VoucherAmountPaid);
                                System.out.println(MIRAmount);
                                
                                if(VoucherAmountPaid>(MIRAmount+1)) {
                                    LastError="Total payment "+VoucherAmountPaid+" exceeds MIR Amount (Freight Amount + Other charges) "+MIRAmount+"." +
                                    "\nPlease check party outstanding statement for the details";
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
            
            //************** Checking for Receipt & Credit Note Vouchers *********************//
            if(VoucherType==FinanceGlobal.TYPE_RECEIPT||VoucherType==FinanceGlobal.TYPE_CREDIT_NOTE) {
                
                //*** ===== CHECK THE EXCESS PAYMENT ==== ***//
                String VoucherNo=getAttribute("VOUCHER_NO").getString();
                double Payment=0;
                String PartyCode="";
                
                for(int i=1;i<=List.size();i++) {
                    clsVoucherItem objItem=(clsVoucherItem)List.get(Integer.toString(i));
                    
                    String MainAccountCode=objItem.getAttribute("MAIN_ACCOUNT_CODE").getString().trim();
                    String SubAccountCode=objItem.getAttribute("SUB_ACCOUNT_CODE").getString().trim();
                    String Effect=objItem.getAttribute("EFFECT").getString();
                    
                    if((!SubAccountCode.trim().equals("")) && Effect.equals("C")) {
                        PartyCode=SubAccountCode;
                        Payment=objItem.getAttribute("AMOUNT").getDouble();
                        
                        String InvoiceNo=objItem.getAttribute("INVOICE_NO").getString();
                        String InvoiceDate=objItem.getAttribute("INVOICE_DATE").getString();
                        int ModuleID=objItem.getAttribute("MODULE_ID").getInt();
                        
                        if(!InvoiceNo.trim().equals("")&&ModuleID==clsSalesInvoice.ModuleID) {
                            
                            int RefCompanyID=objItem.getAttribute("REF_COMPANY_ID").getInt();
                            String CompanyURL=clsFinYear.getDBURL(RefCompanyID,EITLERPGLOBAL.FinYearFrom);
                            
                            //Expense Transaction Check
                            if(ModuleID==clsSalesInvoice.ModuleID) {
                                
                                if(!clsSalesInvoice.IsDocExist(RefCompanyID,InvoiceNo,EITLERPGLOBAL.formatDateDB(InvoiceDate))) {
                                    LastError="Sales Invoice No. "+InvoiceNo+" does not exist. Please check";
                                    return false;
                                }
                                else {
                                    double InvoiceAmount=data.getDoubleValueFromDB("SELECT NET_AMOUNT FROM D_SAL_INVOICE_HEADER WHERE COMPANY_ID="+RefCompanyID+" AND INVOICE_NO='"+InvoiceNo+"' AND INVOICE_DATE='"+EITLERPGLOBAL.formatDateDB(InvoiceDate)+"'");
                                    
                                    String strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) AS RECEIVED_AMOUNT FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B WHERE A.CANCELLED=0 AND A.VOUCHER_NO=B.VOUCHER_NO AND B.EFFECT='C' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND B.INVOICE_NO='"+InvoiceNo+"' AND B.INVOICE_DATE='"+EITLERPGLOBAL.formatDateDB(InvoiceDate)+"' AND A.MODULE_ID="+clsSalesInvoice.ModuleID+" AND A.VOUCHER_NO<>'"+VoucherNo+"'";
                                    double VoucherAmountPaid=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                                    
                                    VoucherAmountPaid+=Payment;
                                    
                                    VoucherAmountPaid=EITLERPGLOBAL.round(VoucherAmountPaid, 0);
                                    if(VoucherAmountPaid>(InvoiceAmount+1)) {
                                        LastError="Total receipt "+VoucherAmountPaid+" exceeds invoice amount "+InvoiceAmount+".\nPlease check party outstanding statement for the details";
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //********************************************************************************//
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    
    public static String getNextVoucherNo(int CompanyID,String BookCode,String Prefix) {
        String newVoucherNo="";
        
        try {
            
            String FinYear= Integer.toString(EITLERPGLOBAL.FinYearFrom).substring(2,4);
            
            String strSQL="SELECT MAX(SUBSTRING(VOUCHER_NO,LENGTH('"+(Prefix+FinYear+BookCode)+"')+1)) AS MAX_NO FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO LIKE '"+(Prefix+FinYear+BookCode)+"%'";
            
            int MaxNo=UtilFunctions.CInt(data.getStringValueFromDB(strSQL,FinanceGlobal.FinURL))+1;
            
            String strMaxNo=Integer.toString(MaxNo);
            
            strMaxNo=EITLERPGLOBAL.Replicate("0", 5-strMaxNo.length())+strMaxNo;
            
            newVoucherNo=Prefix+FinYear+BookCode+strMaxNo;
            
        }
        catch(Exception e) {
            
        }
        
        return newVoucherNo;
    }
    
    public static int getVoucherType(String VoucherNo) {
        return data.getIntValueFromDB("SELECT VOUCHER_TYPE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+VoucherNo+"'",FinanceGlobal.FinURL);
    }
    
    
    public static void OpenVoucher(String VoucherNo,frmPendingApprovals objPendingApproval) {
        try {
            int VoucherType=clsVoucher.getVoucherType(VoucherNo);
            int CompanyID=clsVoucher.getVoucherCompanyID(VoucherNo);
            AppletFrame aFrame=new AppletFrame("Voucher");
            
            //Decide what Form to be opened based on voucher type
            if(VoucherType==1) //General/PJV Voucher
            {
                aFrame.startAppletEx("EITLERP.Finance.frmVoucher","Voucher");
                frmVoucher ObjItem=(frmVoucher) aFrame.ObjApplet;
                ObjItem.frmPA=objPendingApproval;
                ObjItem.FindEx(CompanyID,VoucherNo);
            }
            
            if(VoucherType==2) {
                aFrame.startAppletEx("EITLERP.Finance.frmPaymentVoucher","Payment Voucher");
                frmPaymentVoucher ObjItem=(frmPaymentVoucher) aFrame.ObjApplet;
                ObjItem.frmPA=objPendingApproval;
                ObjItem.FindEx(CompanyID,VoucherNo);
            }
            
            
            if(VoucherType==3) {
                aFrame.startAppletEx("EITLERP.Finance.frmDebitNoteVoucher","DebitNote Voucher");
                frmDebitNoteVoucher ObjItem=(frmDebitNoteVoucher) aFrame.ObjApplet;
                ObjItem.frmPA=objPendingApproval;
                ObjItem.FindEx(CompanyID,VoucherNo);
            }
            
            if(VoucherType==4) {
                aFrame.startAppletEx("EITLERP.Finance.frmCashVoucher","Cash Voucher");
                frmCashVoucher ObjItem=(frmCashVoucher) aFrame.ObjApplet;
                ObjItem.frmPA=objPendingApproval;
                ObjItem.FindEx(CompanyID,VoucherNo);
            }
            
            
            if(VoucherType==5) {
                aFrame.startAppletEx("EITLERP.Finance.frmSalesJournalVoucher","Sales Journal Voucher");
                frmSalesJournalVoucher ObjItem=(frmSalesJournalVoucher) aFrame.ObjApplet;
                ObjItem.frmPA=objPendingApproval;
                ObjItem.FindEx(CompanyID,VoucherNo);
            }
            
            
            if(VoucherType==6) {
                aFrame.startAppletEx("EITLERP.Finance.frmReceiptVoucher","Receipt Voucher");
                frmReceiptVoucher ObjItem=(frmReceiptVoucher) aFrame.ObjApplet;
                ObjItem.frmPA=objPendingApproval;
                ObjItem.FindEx(CompanyID,VoucherNo);
            }
            
            
            if(VoucherType==7) {
                aFrame.startAppletEx("EITLERP.Finance.frmCreditNoteVoucher","Credit Note Voucher");
                frmCreditNoteVoucher ObjItem=(frmCreditNoteVoucher) aFrame.ObjApplet;
                ObjItem.frmPA=objPendingApproval;
                ObjItem.FindEx(CompanyID,VoucherNo);
            }
            
            if(VoucherType==8) {
                aFrame.startAppletEx("EITLERP.Finance.frmCashReceiptVoucher","Cash Receipt Voucher");
                frmCashReceiptVoucher ObjItem=(frmCashReceiptVoucher) aFrame.ObjApplet;
                ObjItem.frmPA=objPendingApproval;
                ObjItem.FindEx(CompanyID,VoucherNo);
            }
            //** Add by prathmesh *//
            if(VoucherType==11) {
                aFrame.startAppletEx("EITLERP.Finance.frmLCJV","LC Voucher");
                frmLCJV ObjItem=(frmLCJV) aFrame.ObjApplet;
                ObjItem.frmPA=objPendingApproval;
                ObjItem.FindEx(CompanyID,VoucherNo);
            }
            
            if(VoucherType==9) {
                aFrame.startAppletEx("EITLERP.Finance.frmJournalVoucher","Journal Voucher");
                frmJournalVoucher ObjItem=(frmJournalVoucher) aFrame.ObjApplet;
                ObjItem.frmPA=objPendingApproval;
                ObjItem.FindEx(CompanyID,VoucherNo);
            }
            
            if(VoucherType==10) {
                aFrame.startAppletEx("EITLERP.Finance.frmPaymentVoucher2","Payment Voucher (2)");
                frmPaymentVoucher2 ObjItem=(frmPaymentVoucher2) aFrame.ObjApplet;
                ObjItem.frmPA=objPendingApproval;
                ObjItem.FindEx(CompanyID,VoucherNo);
            }
        }
        catch(Exception e) {
            
        }
        
    }
    
    
    public boolean getDebitNoteRepresentation() {
        ResultSet rsTmp;
        ResultSet rsDeduction;
        Connection tmpConn;
        Statement tmpStmt;
        
        objPayment=new clsVoucher();
        objPayment.colVoucherItems.clear();
        objPayment.colVoucherItemsEx.clear();
        
        tmpConn=data.getConn(FinanceGlobal.FinURL);
        
        long Counter=0;
        int RevNo=0;
        double ChequeAmount=0;
        double DeductionAmount=0;
        
        try {
            
            int CompanyID=getAttribute("COMPANY_ID").getInt();
            String VoucherNo=getAttribute("VOUCHER_NO").getString();
            
            //General Retrieval of the Voucher
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
                objPayment.setAttribute("REVISION_NO",UtilFunctions.getInt(rsResultSet,"REVISION_NO",0));
            }
            else {
                objPayment.setAttribute("REVISION_NO",0);
            }
            
            OldVoucherNo=UtilFunctions.getString(rsResultSet,"VOUCHER_NO","");
            objPayment.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsResultSet,"COMPANY_ID",0));
            objPayment.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsResultSet,"VOUCHER_NO",""));
            objPayment.setAttribute("VOUCHER_TYPE",UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",1));
            objPayment.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"VOUCHER_DATE","0000-00-00")));
            objPayment.setAttribute("BOOK_CODE",UtilFunctions.getString(rsResultSet,"BOOK_CODE",""));
            objPayment.setAttribute("CHEQUE_NO",UtilFunctions.getString(rsResultSet,"CHEQUE_NO",""));
            objPayment.setAttribute("CHEQUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHEQUE_DATE","0000-00-00")));
            objPayment.setAttribute("REALIZATION_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"REALIZATION_DATE","0000-00-00")));
            objPayment.setAttribute("BANK_NAME",UtilFunctions.getString(rsResultSet,"BOOK_CODE",""));
            objPayment.setAttribute("ST_CATEGORY",UtilFunctions.getString(rsResultSet,"ST_CATEGORY",""));
            objPayment.setAttribute("REASON_CODE",UtilFunctions.getString(rsResultSet,"REASON_CODE",""));
            objPayment.setAttribute("REMARKS",UtilFunctions.getString(rsResultSet,"REMARKS",""));
            objPayment.setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsResultSet,"HIERARCHY_ID",0));
            objPayment.setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            objPayment.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00")));
            objPayment.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            objPayment.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00")));
            objPayment.setAttribute("CHANGED",UtilFunctions.getInt(rsResultSet,"CHANGED",0));
            objPayment.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHANGED_DATE","0000-00-00")));
            objPayment.setAttribute("APPROVED",UtilFunctions.getInt(rsResultSet,"APPROVED",0));
            objPayment.setAttribute("APPROVED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"APPROVED_DATE","0000-00-00")));
            objPayment.setAttribute("REJECTED",UtilFunctions.getInt(rsResultSet,"REJECTED",0));
            objPayment.setAttribute("REJECTED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"REJECTED_DATE","0000-00-00")));
            objPayment.setAttribute("CANCELLED",UtilFunctions.getInt(rsResultSet,"CANCELLED",0));
            objPayment.setAttribute("LEGACY_NO",UtilFunctions.getString(rsResultSet,"LEGACY_NO",""));
            objPayment.setAttribute("LINK_NO",UtilFunctions.getString(rsResultSet,"LINK_NO",""));
            objPayment.setAttribute("LEGACY_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"LEGACY_DATE","0000-00-00")));
            
            String MainAccountCode=data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='D' AND MAIN_ACCOUNT_CODE<>'' AND IS_DEDUCTION=0 ",FinanceGlobal.FinURL);
            String PartyCode=data.getStringValueFromDB("SELECT SUB_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='D' AND MAIN_ACCOUNT_CODE<>'' AND SUB_ACCOUNT_CODE<>'' AND IS_DEDUCTION=0",FinanceGlobal.FinURL);
            
            
            objPayment.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode.trim());
            objPayment.setAttribute("SUB_ACCOUNT_CODE",PartyCode);
            objPayment.setAttribute("CHEQUE_AMOUNT",0);
            
            
            objPayment.colVoucherItems.clear();
            objPayment.colVoucherItemsEx.clear();
            
            
            //Find Debit Entries - Except Normal Deduction
            rsTmp=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='C' ",FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    
                    Counter=Counter+1;
                    clsVoucherItem objItem = new clsVoucherItem();
                    HashMap objDeductions=new HashMap();
                    
                    objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                    objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                    objItem.setAttribute("SR_NO",UtilFunctions.getInt(rsTmp,"SR_NO",0));
                    objItem.setAttribute("EFFECT",UtilFunctions.getString(rsTmp,"EFFECT",""));
                    objItem.setAttribute("ACCOUNT_ID",UtilFunctions.getInt(rsTmp,"ACCOUNT_ID",0));
                    objItem.setAttribute("DEDUCTION_CODE",UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE",""));
                    objItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","").trim());
                    objItem.setAttribute("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"SUB_ACCOUNT_CODE","").trim());
                    objItem.setAttribute("AMOUNT",UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                    
                    ChequeAmount+=UtilFunctions.getDouble(rsTmp,"AMOUNT",0);
                    
                    objItem.setAttribute("REMARKS",UtilFunctions.getString(rsTmp,"REMARKS",""));
                    objItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsTmp,"CREATED_BY",""));
                    objItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CREATED_DATE","0000-00-00")));
                    objItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsTmp,"MODIFIED_BY",""));
                    objItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MODIFIED_DATE","0000-00-00")));
                    objItem.setAttribute("CHANGED",true);
                    objItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CHANGED_DATE","0000-00-00")));
                    objItem.setAttribute("CANCELLED",0);
                    objItem.setAttribute("PO_NO",UtilFunctions.getString(rsTmp,"PO_NO",""));
                    objItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"PO_DATE","0000-00-00")));
                    objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                    objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                    objItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VALUE_DATE","0000-00-00")));
                    objItem.setAttribute("GRN_NO",UtilFunctions.getString(rsTmp,"GRN_NO",""));
                    objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"GRN_DATE","0000-00-00")));
                    objItem.setAttribute("MODULE_ID",UtilFunctions.getInt(rsTmp,"MODULE_ID",0));
                    objItem.setAttribute("REF_COMPANY_ID",UtilFunctions.getInt(rsTmp,"REF_COMPANY_ID",0));
                    objItem.setAttribute("LINK_NO",UtilFunctions.getString(rsTmp,"LINK_NO",""));
                    objItem.setAttribute("MATCHED",UtilFunctions.getInt(rsTmp,"MATCHED",0));
                    objItem.setAttribute("MATCHED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MATCHED_DATE","0000-00-00")));
                    DeductionAmount=0;
                    
                    
                    objPayment.colVoucherItems.put(Integer.toString(objPayment.colVoucherItems.size()+1) ,objItem);
                    
                    rsTmp.next();
                }
                
            }
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
        
    }
    
    
    public boolean getCashPaymentRepresentation1() {
        ResultSet rsTmp;
        ResultSet rsDeduction;
        Connection tmpConn;
        Statement tmpStmt;
        
        objPayment=new clsVoucher();
        objPayment.colVoucherItems.clear();
        objPayment.colVoucherItemsEx.clear();
        
        tmpConn=data.getConn(FinanceGlobal.FinURL);
        
        long Counter=0;
        int RevNo=0;
        int VoucherSrNo=0;
        double ChequeAmount=0;
        double DeductionAmount=0;
        
        try {
            
            int CompanyID=getAttribute("COMPANY_ID").getInt();
            String VoucherNo=getAttribute("VOUCHER_NO").getString();
            
            //General Retrieval of the Voucher
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
                objPayment.setAttribute("REVISION_NO",UtilFunctions.getInt(rsResultSet,"REVISION_NO",0));
            }
            else {
                objPayment.setAttribute("REVISION_NO",0);
            }
            
            OldVoucherNo=UtilFunctions.getString(rsResultSet,"VOUCHER_NO","");
            objPayment.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsResultSet,"COMPANY_ID",0));
            objPayment.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsResultSet,"VOUCHER_NO",""));
            objPayment.setAttribute("VOUCHER_TYPE",UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",1));
            objPayment.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"VOUCHER_DATE","0000-00-00")));
            objPayment.setAttribute("BOOK_CODE",UtilFunctions.getString(rsResultSet,"BOOK_CODE",""));
            objPayment.setAttribute("CHEQUE_NO",UtilFunctions.getString(rsResultSet,"CHEQUE_NO",""));
            objPayment.setAttribute("CHEQUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHEQUE_DATE","0000-00-00")));
            objPayment.setAttribute("REALIZATION_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"REALIZATION_DATE","0000-00-00")));
            objPayment.setAttribute("BANK_NAME",UtilFunctions.getString(rsResultSet,"BOOK_CODE",""));
            objPayment.setAttribute("ST_CATEGORY",UtilFunctions.getString(rsResultSet,"ST_CATEGORY",""));
            objPayment.setAttribute("REMARKS",UtilFunctions.getString(rsResultSet,"REMARKS",""));
            objPayment.setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsResultSet,"HIERARCHY_ID",0));
            objPayment.setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            objPayment.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00")));
            objPayment.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            objPayment.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00")));
            objPayment.setAttribute("CHANGED",UtilFunctions.getInt(rsResultSet,"CHANGED",0));
            objPayment.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHANGED_DATE","0000-00-00")));
            objPayment.setAttribute("APPROVED",UtilFunctions.getInt(rsResultSet,"APPROVED",0));
            objPayment.setAttribute("APPROVED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"APPROVED_DATE","0000-00-00")));
            objPayment.setAttribute("REJECTED",UtilFunctions.getInt(rsResultSet,"REJECTED",0));
            objPayment.setAttribute("REJECTED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"REJECTED_DATE","0000-00-00")));
            objPayment.setAttribute("CANCELLED",UtilFunctions.getInt(rsResultSet,"CANCELLED",0));
            objPayment.setAttribute("LEGACY_NO",UtilFunctions.getString(rsResultSet,"LEGACY_NO",""));
            objPayment.setAttribute("LINK_NO",UtilFunctions.getString(rsResultSet,"LINK_NO",""));
            objPayment.setAttribute("LEGACY_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"LEGACY_DATE","0000-00-00")));
            
            String MainAccountCode=data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='D' AND MAIN_ACCOUNT_CODE<>'' AND IS_DEDUCTION=0 ",FinanceGlobal.FinURL);
            String BankCode=data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='C' AND MAIN_ACCOUNT_CODE<>'' AND IS_DEDUCTION=0 ",FinanceGlobal.FinURL);
            String PartyCode="";
            
            if(BankCode.trim().equals("")) {
                MainAccountCode="";
                BankCode=data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='C' AND MAIN_ACCOUNT_CODE<>'' ",FinanceGlobal.FinURL);
                PartyCode="";
            }
            
            objPayment.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode);
            objPayment.setAttribute("SUB_ACCOUNT_CODE",PartyCode);
            objPayment.setAttribute("BANK_CODE",BankCode);
            objPayment.setAttribute("CHEQUE_AMOUNT",0);
            
            
            objPayment.colVoucherItems.clear();
            objPayment.colVoucherItemsEx.clear();
            
            //Find Unique Invoices
            ResultSet rsInvoices=data.getResult("SELECT DISTINCT(INVOICE_NO) FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='D' " ,FinanceGlobal.FinURL);
            
            System.out.println("SELECT DISTINCT(INVOICE_NO) FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='D' AND IS_DEDUCTION=0 ");
            rsInvoices.first();
            
            if(rsInvoices.getRow()>0) {
                
                
                while(!rsInvoices.isAfterLast()) {
                    String InvoiceNo=UtilFunctions.getString(rsInvoices,"INVOICE_NO","");
                    
                    Counter=0;
                    
                    //Find Debit Entries - Except Normal Deduction
                    rsTmp=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND INVOICE_NO='"+InvoiceNo+"' AND EFFECT='D' AND MAIN_ACCOUNT_CODE<>'' AND IS_DEDUCTION=0 ",FinanceGlobal.FinURL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        while(!rsTmp.isAfterLast()) {
                            
                            VoucherSrNo=UtilFunctions.getInt(rsTmp,"SR_NO", 0);
                            
                            Counter=Counter+1;
                            clsVoucherItem objItem = new clsVoucherItem();
                            HashMap objDeductions=new HashMap();
                            
                            objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                            objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                            objItem.setAttribute("SR_NO",UtilFunctions.getInt(rsTmp,"SR_NO",0));
                            objItem.setAttribute("EFFECT",UtilFunctions.getString(rsTmp,"EFFECT",""));
                            objItem.setAttribute("ACCOUNT_ID",UtilFunctions.getInt(rsTmp,"ACCOUNT_ID",0));
                            objItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","").trim());
                            objItem.setAttribute("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"SUB_ACCOUNT_CODE","").trim());
                            
                            objItem.setAttribute("AMOUNT",UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                            
                            ChequeAmount+=UtilFunctions.getDouble(rsTmp,"AMOUNT",0);
                            
                            objItem.setAttribute("REMARKS",UtilFunctions.getString(rsTmp,"REMARKS",""));
                            objItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsTmp,"CREATED_BY",""));
                            objItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CREATED_DATE","0000-00-00")));
                            objItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsTmp,"MODIFIED_BY",""));
                            objItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MODIFIED_DATE","0000-00-00")));
                            objItem.setAttribute("CHANGED",true);
                            objItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CHANGED_DATE","0000-00-00")));
                            objItem.setAttribute("CANCELLED",0);
                            objItem.setAttribute("PO_NO",UtilFunctions.getString(rsTmp,"PO_NO",""));
                            objItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"PO_DATE","0000-00-00")));
                            objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                            objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                            objItem.setAttribute("GRN_NO",UtilFunctions.getString(rsTmp,"GRN_NO",""));
                            objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"GRN_DATE","0000-00-00")));
                            objItem.setAttribute("MODULE_ID",UtilFunctions.getInt(rsTmp,"MODULE_ID",0));
                            objItem.setAttribute("REF_COMPANY_ID",UtilFunctions.getInt(rsTmp,"REF_COMPANY_ID",0));
                            objItem.setAttribute("LINK_NO",UtilFunctions.getString(rsTmp,"LINK_NO",""));
                            
                            DeductionAmount=0;
                            
                            objItem.setAttribute("INVOICE_AMOUNT",UtilFunctions.getDouble(rsTmp,"INVOICE_AMOUNT",0)+DeductionAmount);
                            objItem.setAttribute("INVOICE_DEDUCTIONS",objDeductions);
                            objItem.setAttribute("DEDUCTION_AMOUNT",DeductionAmount);
                            objItem.setAttribute("IS_DEDUCTION",0);
                            //************************ End of Deduction Findings ******************//
                            
                            objPayment.colVoucherItems.put(Integer.toString(objPayment.colVoucherItems.size()+1) ,objItem);
                            
                            rsTmp.next();
                        }
                        
                    }
                    
                    rsInvoices.next();
                }
            }
            
            objPayment.setAttribute("CHEQUE_AMOUNT",ChequeAmount);
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
        
    }
    
    public boolean getCashPaymentRepresentation() {
        ResultSet rsTmp;
        ResultSet rsDeduction;
        Connection tmpConn;
        Statement tmpStmt;
        
        objPayment=new clsVoucher();
        objPayment.colVoucherItems.clear();
        objPayment.colVoucherItemsEx.clear();
        
        tmpConn=data.getConn(FinanceGlobal.FinURL);
        
        long Counter=0;
        int RevNo=0;
        int VoucherSrNo=0;
        double ChequeAmount=0;
        double DeductionAmount=0;
        
        try {
            
            int CompanyID=getAttribute("COMPANY_ID").getInt();
            String VoucherNo=getAttribute("VOUCHER_NO").getString();
            
            //General Retrieval of the Voucher
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
                objPayment.setAttribute("REVISION_NO",UtilFunctions.getInt(rsResultSet,"REVISION_NO",0));
            }
            else {
                objPayment.setAttribute("REVISION_NO",0);
            }
            
            OldVoucherNo=UtilFunctions.getString(rsResultSet,"VOUCHER_NO","");
            objPayment.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsResultSet,"COMPANY_ID",0));
            objPayment.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsResultSet,"VOUCHER_NO",""));
            objPayment.setAttribute("VOUCHER_TYPE",UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",1));
            objPayment.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"VOUCHER_DATE","0000-00-00")));
            objPayment.setAttribute("BOOK_CODE",UtilFunctions.getString(rsResultSet,"BOOK_CODE",""));
            objPayment.setAttribute("CHEQUE_NO",UtilFunctions.getString(rsResultSet,"CHEQUE_NO",""));
            objPayment.setAttribute("CHEQUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHEQUE_DATE","0000-00-00")));
            objPayment.setAttribute("REALIZATION_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"REALIZATION_DATE","0000-00-00")));
            objPayment.setAttribute("BANK_NAME",UtilFunctions.getString(rsResultSet,"BOOK_CODE",""));
            objPayment.setAttribute("ST_CATEGORY",UtilFunctions.getString(rsResultSet,"ST_CATEGORY",""));
            objPayment.setAttribute("REASON_CODE",UtilFunctions.getString(rsResultSet,"REASON_CODE",""));
            objPayment.setAttribute("REMARKS",UtilFunctions.getString(rsResultSet,"REMARKS",""));
            objPayment.setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsResultSet,"HIERARCHY_ID",0));
            objPayment.setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            objPayment.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00")));
            objPayment.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            objPayment.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00")));
            objPayment.setAttribute("CHANGED",UtilFunctions.getInt(rsResultSet,"CHANGED",0));
            objPayment.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHANGED_DATE","0000-00-00")));
            objPayment.setAttribute("APPROVED",UtilFunctions.getInt(rsResultSet,"APPROVED",0));
            objPayment.setAttribute("APPROVED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"APPROVED_DATE","0000-00-00")));
            objPayment.setAttribute("REJECTED",UtilFunctions.getInt(rsResultSet,"REJECTED",0));
            objPayment.setAttribute("REJECTED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"REJECTED_DATE","0000-00-00")));
            objPayment.setAttribute("CANCELLED",UtilFunctions.getInt(rsResultSet,"CANCELLED",0));
            objPayment.setAttribute("LEGACY_NO",UtilFunctions.getString(rsResultSet,"LEGACY_NO",""));
            objPayment.setAttribute("LINK_NO",UtilFunctions.getString(rsResultSet,"LINK_NO",""));
            objPayment.setAttribute("LEGACY_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"LEGACY_DATE","0000-00-00")));
            objPayment.setAttribute("EMPLOYEE_NO",data.getStringValueFromDB("SELECT EMPLOYEE_NO FROM D_FIN_EMPLOYEE_TRAVEL_EXP WHERE VOUCHER_NO='"+VoucherNo+"' ",FinanceGlobal.FinURL));
            
            String MainAccountCode=data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='D' AND MAIN_ACCOUNT_CODE<>'' AND IS_DEDUCTION=0 ",FinanceGlobal.FinURL);
            String BankCode=data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='C' AND MAIN_ACCOUNT_CODE<>'' AND IS_DEDUCTION=0 ",FinanceGlobal.FinURL);
            String PartyCode="";
            
            if(BankCode.trim().equals("")) {
                MainAccountCode="";
                BankCode=data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='C' AND MAIN_ACCOUNT_CODE<>'' ",FinanceGlobal.FinURL);
                PartyCode="";
            }
            
            objPayment.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode);
            objPayment.setAttribute("SUB_ACCOUNT_CODE",PartyCode);
            objPayment.setAttribute("BANK_CODE",BankCode);
            objPayment.setAttribute("CHEQUE_AMOUNT",0);
            
            
            objPayment.colVoucherItems.clear();
            objPayment.colVoucherItemsEx.clear();
            
            //Find Unique Invoices
            //ResultSet rsDebitEntries=data.getResult("SELECT DISTINCT(INVOICE_NO) FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='D' " ,FinanceGlobal.FinURL);
            ResultSet rsDebitEntries=data.getResult("SELECT SR_NO FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='D' " ,FinanceGlobal.FinURL);
            rsDebitEntries.first();
            //System.out.println("SELECT DISTINCT(INVOICE_NO) FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='D' AND IS_DEDUCTION=0 ");
            
            if(rsDebitEntries.getRow()>0) {
                while(!rsDebitEntries.isAfterLast()) {
                    //String InvoiceNo=UtilFunctions.getString(rsDebitEntries,"INVOICE_NO","");
                    String strSrNo=Integer.toString(UtilFunctions.getInt(rsDebitEntries,"SR_NO",0));
                    
                    Counter=0;
                    
                    //Find Debit Entries - Except Normal Deduction
                    rsTmp=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND SR_NO="+strSrNo+" AND EFFECT='D' AND MAIN_ACCOUNT_CODE<>'' AND IS_DEDUCTION=0 ",FinanceGlobal.FinURL);
                    rsTmp.first();
                    //rsTmp=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND INVOICE_NO='"+InvoiceNo+"' AND EFFECT='D' AND MAIN_ACCOUNT_CODE<>'' AND IS_DEDUCTION=0 ",FinanceGlobal.FinURL);
                    //rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        while(!rsTmp.isAfterLast()) {
                            VoucherSrNo=UtilFunctions.getInt(rsTmp,"SR_NO", 0);
                            
                            Counter=Counter+1;
                            clsVoucherItem objItem = new clsVoucherItem();
                            HashMap objDeductions=new HashMap();
                            
                            objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                            objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                            objItem.setAttribute("SR_NO",UtilFunctions.getInt(rsTmp,"SR_NO",0));
                            objItem.setAttribute("EFFECT",UtilFunctions.getString(rsTmp,"EFFECT",""));
                            objItem.setAttribute("ACCOUNT_ID",UtilFunctions.getInt(rsTmp,"ACCOUNT_ID",0));
                            objItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","").trim());
                            objItem.setAttribute("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"SUB_ACCOUNT_CODE","").trim());
                            
                            objItem.setAttribute("AMOUNT",UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                            
                            ChequeAmount+=UtilFunctions.getDouble(rsTmp,"AMOUNT",0);
                            
                            objItem.setAttribute("REMARKS",UtilFunctions.getString(rsTmp,"REMARKS",""));
                            objItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsTmp,"CREATED_BY",""));
                            objItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CREATED_DATE","0000-00-00")));
                            objItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsTmp,"MODIFIED_BY",""));
                            objItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MODIFIED_DATE","0000-00-00")));
                            objItem.setAttribute("CHANGED",true);
                            objItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CHANGED_DATE","0000-00-00")));
                            objItem.setAttribute("CANCELLED",0);
                            objItem.setAttribute("PO_NO",UtilFunctions.getString(rsTmp,"PO_NO",""));
                            objItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"PO_DATE","0000-00-00")));
                            objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                            objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                            objItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VALUE_DATE","0000-00-00")));
                            objItem.setAttribute("GRN_NO",UtilFunctions.getString(rsTmp,"GRN_NO",""));
                            objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"GRN_DATE","0000-00-00")));
                            objItem.setAttribute("MODULE_ID",UtilFunctions.getInt(rsTmp,"MODULE_ID",0));
                            objItem.setAttribute("REF_COMPANY_ID",UtilFunctions.getInt(rsTmp,"REF_COMPANY_ID",0));
                            objItem.setAttribute("LINK_NO",UtilFunctions.getString(rsTmp,"LINK_NO",""));
                            objItem.setAttribute("MATCHED",UtilFunctions.getInt(rsTmp,"MATCHED",0));
                            objItem.setAttribute("MATCHED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MATCHED_DATE","0000-00-00")));
                            DeductionAmount=0;
                            //*************** Find Direct Deductions for this Invoice ******************//
                            rsDeduction=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND REF_SR_NO="+VoucherSrNo+" AND EFFECT='C' AND IS_DEDUCTION=1 ",FinanceGlobal.FinURL);
                            rsDeduction.first();
                            
                            if(rsDeduction.getRow()>0) {
                                while(!rsDeduction.isAfterLast()) {
                                    clsVoucherItem objDeductionItem = new clsVoucherItem();
                                    
                                    DeductionAmount+=UtilFunctions.getDouble(rsDeduction,"AMOUNT",0);
                                    
                                    objDeductionItem.setAttribute("DOC_TYPE",1); //Direct Deduction
                                    objDeductionItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsDeduction,"COMPANY_ID", 0));
                                    objDeductionItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsDeduction,"VOUCHER_NO",""));
                                    objDeductionItem.setAttribute("SR_NO",UtilFunctions.getInt(rsDeduction,"SR_NO",0));
                                    objDeductionItem.setAttribute("EFFECT",UtilFunctions.getString(rsDeduction,"EFFECT",""));
                                    objDeductionItem.setAttribute("ACCOUNT_ID",UtilFunctions.getInt(rsDeduction,"ACCOUNT_ID",0));
                                    objDeductionItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsDeduction,"MAIN_ACCOUNT_CODE","").trim());
                                    objDeductionItem.setAttribute("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsDeduction,"SUB_ACCOUNT_CODE","").trim());
                                    objDeductionItem.setAttribute("AMOUNT",UtilFunctions.getDouble(rsDeduction,"AMOUNT",0));
                                    objDeductionItem.setAttribute("APPLICABLE_AMOUNT",UtilFunctions.getDouble(rsDeduction,"APPLICABLE_AMOUNT",0));
                                    objDeductionItem.setAttribute("PERCENTAGE",UtilFunctions.getDouble(rsDeduction,"PERCENTAGE",0));
                                    objDeductionItem.setAttribute("REMARKS",UtilFunctions.getString(rsDeduction,"REMARKS",""));
                                    objDeductionItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsDeduction,"CREATED_BY",""));
                                    objDeductionItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"CREATED_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsDeduction,"MODIFIED_BY",""));
                                    objDeductionItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"MODIFIED_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("CHANGED",true);
                                    objDeductionItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"CHANGED_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("CANCELLED",0);
                                    objDeductionItem.setAttribute("PO_NO",UtilFunctions.getString(rsDeduction,"PO_NO",""));
                                    objDeductionItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"PO_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsDeduction,"INVOICE_NO",""));
                                    objDeductionItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"INVOICE_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"VALUE_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("GRN_NO",UtilFunctions.getString(rsDeduction,"GRN_NO",""));
                                    objDeductionItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"GRN_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("MODULE_ID",UtilFunctions.getInt(rsDeduction,"MODULE_ID",0));
                                    objDeductionItem.setAttribute("INVOICE_AMOUNT",UtilFunctions.getDouble(rsDeduction,"INVOICE_AMOUNT",0));
                                    objDeductionItem.setAttribute("IS_DEDUCTION",UtilFunctions.getInt(rsDeduction,"IS_DEDUCTION",0));
                                    objDeductionItem.setAttribute("REF_COMPANY_ID",UtilFunctions.getInt(rsDeduction,"REF_COMPANY_ID",0));
                                    objDeductionItem.setAttribute("IS_DEDUCTION",1);
                                    objDeductionItem.setAttribute("LINK_NO",UtilFunctions.getString(rsDeduction,"LINK_NO",""));
                                    
                                    objDeductions.put(Integer.toString(objDeductions.size()+1),objDeductionItem);
                                    rsDeduction.next();
                                }
                            }
                            //*************************** End of direct deduction **********************************//
                            objItem.setAttribute("INVOICE_AMOUNT",UtilFunctions.getDouble(rsTmp,"INVOICE_AMOUNT",0)+DeductionAmount);
                            objItem.setAttribute("INVOICE_DEDUCTIONS",objDeductions);
                            objItem.setAttribute("DEDUCTION_AMOUNT",DeductionAmount);
                            objItem.setAttribute("IS_DEDUCTION",0);
                            //************************ End of Deduction Findings ******************//
                            objPayment.colVoucherItems.put(Integer.toString(objPayment.colVoucherItems.size()+1) ,objItem);
                            rsTmp.next();
                        }
                    }
                    rsDebitEntries.next();
                }
            }
            
            objPayment.setAttribute("CHEQUE_AMOUNT",ChequeAmount);
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean getCreditNoteRepresentation() {
        ResultSet rsTmp;
        ResultSet rsDeduction;
        Connection tmpConn;
        Statement tmpStmt;
        
        objPayment=new clsVoucher();
        objPayment.colVoucherItems.clear();
        objPayment.colVoucherItemsEx.clear();
        
        tmpConn=data.getConn(FinanceGlobal.FinURL);
        
        long Counter=0;
        int RevNo=0;
        double ChequeAmount=0;
        double DeductionAmount=0;
        
        try {
            
            int CompanyID=getAttribute("COMPANY_ID").getInt();
            String VoucherNo=getAttribute("VOUCHER_NO").getString();
            
            //General Retrieval of the Voucher
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
                objPayment.setAttribute("REVISION_NO",UtilFunctions.getInt(rsResultSet,"REVISION_NO",0));
            }
            else {
                objPayment.setAttribute("REVISION_NO",0);
            }
            
            OldVoucherNo=UtilFunctions.getString(rsResultSet,"VOUCHER_NO","");
            objPayment.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsResultSet,"COMPANY_ID",0));
            objPayment.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsResultSet,"VOUCHER_NO",""));
            objPayment.setAttribute("VOUCHER_TYPE",UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",1));
            objPayment.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"VOUCHER_DATE","0000-00-00")));
            objPayment.setAttribute("BOOK_CODE",UtilFunctions.getString(rsResultSet,"BOOK_CODE",""));
            objPayment.setAttribute("CHEQUE_NO",UtilFunctions.getString(rsResultSet,"CHEQUE_NO",""));
            objPayment.setAttribute("CHEQUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHEQUE_DATE","0000-00-00")));
            objPayment.setAttribute("REALIZATION_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"REALIZATION_DATE","0000-00-00")));
            objPayment.setAttribute("BANK_NAME",UtilFunctions.getString(rsResultSet,"BOOK_CODE",""));
            objPayment.setAttribute("ST_CATEGORY",UtilFunctions.getString(rsResultSet,"ST_CATEGORY",""));
            objPayment.setAttribute("REASON_CODE",UtilFunctions.getString(rsResultSet,"REASON_CODE",""));
            objPayment.setAttribute("REMARKS",UtilFunctions.getString(rsResultSet,"REMARKS",""));
            objPayment.setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsResultSet,"HIERARCHY_ID",0));
            objPayment.setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            objPayment.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00")));
            objPayment.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            objPayment.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00")));
            objPayment.setAttribute("CHANGED",UtilFunctions.getInt(rsResultSet,"CHANGED",0));
            objPayment.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHANGED_DATE","0000-00-00")));
            objPayment.setAttribute("APPROVED",UtilFunctions.getInt(rsResultSet,"APPROVED",0));
            objPayment.setAttribute("APPROVED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"APPROVED_DATE","0000-00-00")));
            objPayment.setAttribute("REJECTED",UtilFunctions.getInt(rsResultSet,"REJECTED",0));
            objPayment.setAttribute("REJECTED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"REJECTED_DATE","0000-00-00")));
            objPayment.setAttribute("CANCELLED",UtilFunctions.getInt(rsResultSet,"CANCELLED",0));
            objPayment.setAttribute("LEGACY_NO",UtilFunctions.getString(rsResultSet,"LEGACY_NO",""));
            objPayment.setAttribute("LINK_NO",UtilFunctions.getString(rsResultSet,"LINK_NO",""));
            objPayment.setAttribute("LEGACY_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"LEGACY_DATE","0000-00-00")));
            objPayment.setAttribute("EXCLUDE_IN_ADJ",UtilFunctions.getInt(rsResultSet,"EXCLUDE_IN_ADJ",0));
            
            String MainAccountCode=data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='C' AND MAIN_ACCOUNT_CODE<>'' AND IS_DEDUCTION=0 ",FinanceGlobal.FinURL);
            String PartyCode=data.getStringValueFromDB("SELECT SUB_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='C' AND MAIN_ACCOUNT_CODE<>'' AND SUB_ACCOUNT_CODE<>'' AND IS_DEDUCTION=0",FinanceGlobal.FinURL);
            
            
            objPayment.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode);
            objPayment.setAttribute("SUB_ACCOUNT_CODE",PartyCode);
            objPayment.setAttribute("CHEQUE_AMOUNT",0);
            
            
            objPayment.colVoucherItems.clear();
            objPayment.colVoucherItemsEx.clear();
            
            
            //Find Debit Entries - Except Normal Deduction
            rsTmp=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='D' ",FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    
                    Counter=Counter+1;
                    clsVoucherItem objItem = new clsVoucherItem();
                    HashMap objDeductions=new HashMap();
                    
                    objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                    objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                    objItem.setAttribute("SR_NO",UtilFunctions.getInt(rsTmp,"SR_NO",0));
                    objItem.setAttribute("EFFECT",UtilFunctions.getString(rsTmp,"EFFECT",""));
                    objItem.setAttribute("ACCOUNT_ID",UtilFunctions.getInt(rsTmp,"ACCOUNT_ID",0));
                    objItem.setAttribute("DEDUCTION_CODE",UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE",""));
                    objItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","").trim());
                    objItem.setAttribute("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"SUB_ACCOUNT_CODE","").trim());
                    objItem.setAttribute("AMOUNT",UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                    
                    ChequeAmount+=UtilFunctions.getDouble(rsTmp,"AMOUNT",0);
                    
                    objItem.setAttribute("REMARKS",UtilFunctions.getString(rsTmp,"REMARKS",""));
                    objItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsTmp,"CREATED_BY",""));
                    objItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CREATED_DATE","0000-00-00")));
                    objItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsTmp,"MODIFIED_BY",""));
                    objItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MODIFIED_DATE","0000-00-00")));
                    objItem.setAttribute("CHANGED",true);
                    objItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CHANGED_DATE","0000-00-00")));
                    objItem.setAttribute("CANCELLED",0);
                    objItem.setAttribute("PO_NO",UtilFunctions.getString(rsTmp,"PO_NO",""));
                    objItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"PO_DATE","0000-00-00")));
                    objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                    objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                    objItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VALUE_DATE","0000-00-00")));
                    objItem.setAttribute("GRN_NO",UtilFunctions.getString(rsTmp,"GRN_NO",""));
                    objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"GRN_DATE","0000-00-00")));
                    objItem.setAttribute("MODULE_ID",UtilFunctions.getInt(rsTmp,"MODULE_ID",0));
                    objItem.setAttribute("REF_COMPANY_ID",UtilFunctions.getInt(rsTmp,"REF_COMPANY_ID",0));
                    objItem.setAttribute("LINK_NO",UtilFunctions.getString(rsTmp,"LINK_NO",""));
                    objItem.setAttribute("MATCHED",UtilFunctions.getInt(rsTmp,"MATCHED",0));
                    objItem.setAttribute("MATCHED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MATCHED_DATE","0000-00-00")));
                    DeductionAmount=0;
                    
                    
                    objPayment.colVoucherItems.put(Integer.toString(objPayment.colVoucherItems.size()+1) ,objItem);
                    
                    rsTmp.next();
                }
                
            }
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
        
    }
    
    
    private boolean isFirstApprovalAction(String VoucherNo) {
        try {
            if(data.IsRecordExist("SELECT VOUCHER_NO FROM D_FIN_VOUCHER_HEADER_H WHERE VOUCHER_NO='"+VoucherNo+"' AND APPROVAL_STATUS='A' ",FinanceGlobal.FinURL)) {
                return false;
            }
            else {
                return true;
            }
        }
        catch(Exception e) {
            return false;
        }
    }
    
    
    public boolean getPayment2Representation() {
        ResultSet rsTmp;
        ResultSet rsDeduction;
        Connection tmpConn;
        Statement tmpStmt;
        
        objPayment=new clsVoucher();
        objPayment.colVoucherItems.clear();
        objPayment.colVoucherItemsEx.clear();
        
        tmpConn=data.getConn(FinanceGlobal.FinURL);
        
        long Counter=0;
        int RevNo=0;
        int VoucherSrNo=0;
        double ChequeAmount=0;
        double DeductionAmount=0;
        
        try {
            
            int CompanyID=getAttribute("COMPANY_ID").getInt();
            String VoucherNo=getAttribute("VOUCHER_NO").getString();
            
            //General Retrieval of the Voucher
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
                objPayment.setAttribute("REVISION_NO",UtilFunctions.getInt(rsResultSet,"REVISION_NO",0));
            }
            else {
                objPayment.setAttribute("REVISION_NO",0);
            }
            
            OldVoucherNo=UtilFunctions.getString(rsResultSet,"VOUCHER_NO","");
            objPayment.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsResultSet,"COMPANY_ID",0));
            objPayment.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsResultSet,"VOUCHER_NO",""));
            objPayment.setAttribute("VOUCHER_TYPE",UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",1));
            objPayment.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"VOUCHER_DATE","0000-00-00")));
            objPayment.setAttribute("BOOK_CODE",UtilFunctions.getString(rsResultSet,"BOOK_CODE",""));
            objPayment.setAttribute("CHEQUE_NO",UtilFunctions.getString(rsResultSet,"CHEQUE_NO",""));
            objPayment.setAttribute("CHEQUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHEQUE_DATE","0000-00-00")));
            objPayment.setAttribute("REALIZATION_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"REALIZATION_DATE","0000-00-00")));
            objPayment.setAttribute("BANK_NAME",UtilFunctions.getString(rsResultSet,"BOOK_CODE",""));
            objPayment.setAttribute("ST_CATEGORY",UtilFunctions.getString(rsResultSet,"ST_CATEGORY",""));
            objPayment.setAttribute("REMARKS",UtilFunctions.getString(rsResultSet,"REMARKS",""));
            objPayment.setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsResultSet,"HIERARCHY_ID",0));
            objPayment.setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            objPayment.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00")));
            objPayment.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            objPayment.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00")));
            objPayment.setAttribute("CHANGED",UtilFunctions.getInt(rsResultSet,"CHANGED",0));
            objPayment.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHANGED_DATE","0000-00-00")));
            objPayment.setAttribute("APPROVED",UtilFunctions.getInt(rsResultSet,"APPROVED",0));
            objPayment.setAttribute("APPROVED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"APPROVED_DATE","0000-00-00")));
            objPayment.setAttribute("REJECTED",UtilFunctions.getInt(rsResultSet,"REJECTED",0));
            objPayment.setAttribute("REJECTED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"REJECTED_DATE","0000-00-00")));
            objPayment.setAttribute("CANCELLED",UtilFunctions.getInt(rsResultSet,"CANCELLED",0));
            objPayment.setAttribute("LEGACY_NO",UtilFunctions.getString(rsResultSet,"LEGACY_NO",""));
            objPayment.setAttribute("LEGACY_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"LEGACY_DATE","0000-00-00")));
            
            String BankCode=data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='C' AND MAIN_ACCOUNT_CODE<>'' AND IS_DEDUCTION=0 ",FinanceGlobal.FinURL);
            
            if(BankCode.trim().equals("")) {
                BankCode=data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='C' AND MAIN_ACCOUNT_CODE<>'' ",FinanceGlobal.FinURL);
            }
            
            objPayment.setAttribute("BANK_CODE",BankCode);
            objPayment.setAttribute("CHEQUE_AMOUNT",0);
            
            
            objPayment.colVoucherItems.clear();
            objPayment.colVoucherItemsEx.clear();
            
            //Find Unique Invoices
            ResultSet rsInvoices=data.getResult("SELECT SR_NO FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='D' " ,FinanceGlobal.FinURL);
            rsInvoices.first();
            
            if(rsInvoices.getRow()>0) {
                while(!rsInvoices.isAfterLast()) {
                    String strSrNo=Integer.toString(UtilFunctions.getInt(rsInvoices,"SR_NO",0));
                    
                    Counter=0;
                    
                    //Find Debit Entries - Except Normal Deduction
                    rsTmp=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND SR_NO="+strSrNo+" AND EFFECT='D' AND MAIN_ACCOUNT_CODE<>'' AND IS_DEDUCTION=0 ",FinanceGlobal.FinURL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        while(!rsTmp.isAfterLast()) {
                            VoucherSrNo=UtilFunctions.getInt(rsTmp,"SR_NO", 0);
                            Counter=Counter+1;
                            clsVoucherItem objItem = new clsVoucherItem();
                            HashMap objDeductions=new HashMap();
                            
                            objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                            objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                            objItem.setAttribute("SR_NO",UtilFunctions.getInt(rsTmp,"SR_NO",0));
                            objItem.setAttribute("EFFECT",UtilFunctions.getString(rsTmp,"EFFECT",""));
                            objItem.setAttribute("ACCOUNT_ID",UtilFunctions.getInt(rsTmp,"ACCOUNT_ID",0));
                            objItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","").trim());
                            objItem.setAttribute("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"SUB_ACCOUNT_CODE",""));
                            
                            objItem.setAttribute("AMOUNT",UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                            
                            ChequeAmount+=UtilFunctions.getDouble(rsTmp,"AMOUNT",0);
                            
                            objItem.setAttribute("REMARKS",UtilFunctions.getString(rsTmp,"REMARKS",""));
                            objItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsTmp,"CREATED_BY",""));
                            objItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CREATED_DATE","0000-00-00")));
                            objItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsTmp,"MODIFIED_BY",""));
                            objItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MODIFIED_DATE","0000-00-00")));
                            objItem.setAttribute("CHANGED",true);
                            objItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CHANGED_DATE","0000-00-00")));
                            objItem.setAttribute("CANCELLED",0);
                            objItem.setAttribute("PO_NO",UtilFunctions.getString(rsTmp,"PO_NO",""));
                            objItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"PO_DATE","0000-00-00")));
                            objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                            objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                            objItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VALUE_DATE","0000-00-00")));
                            objItem.setAttribute("GRN_NO",UtilFunctions.getString(rsTmp,"GRN_NO",""));
                            objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"GRN_DATE","0000-00-00")));
                            objItem.setAttribute("MODULE_ID",UtilFunctions.getInt(rsTmp,"MODULE_ID",0));
                            objItem.setAttribute("REF_COMPANY_ID",UtilFunctions.getInt(rsTmp,"REF_COMPANY_ID",0));
                            objItem.setAttribute("LINK_NO",UtilFunctions.getString(rsTmp,"LINK_NO",""));
                            
                            DeductionAmount=0;
                            
                            //*************** Find Direct Deductions for this Invoice ******************//
                            //rsDeduction=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND INVOICE_NO='"+InvoiceNo+"' AND EFFECT='C' AND IS_DEDUCTION=1 ",FinanceGlobal.FinURL);
                            rsDeduction=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND REF_SR_NO="+VoucherSrNo+" AND EFFECT='C' AND IS_DEDUCTION=1 ",FinanceGlobal.FinURL);
                            rsDeduction.first();
                            
                            if(rsDeduction.getRow()>0) {
                                while(!rsDeduction.isAfterLast()) {
                                    clsVoucherItem objDeductionItem = new clsVoucherItem();
                                    
                                    DeductionAmount+=UtilFunctions.getDouble(rsDeduction,"AMOUNT",0);
                                    
                                    objDeductionItem.setAttribute("DOC_TYPE",1); //Direct Deduction
                                    objDeductionItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsDeduction,"COMPANY_ID", 0));
                                    objDeductionItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsDeduction,"VOUCHER_NO",""));
                                    objDeductionItem.setAttribute("SR_NO",UtilFunctions.getInt(rsDeduction,"SR_NO",0));
                                    objDeductionItem.setAttribute("EFFECT",UtilFunctions.getString(rsDeduction,"EFFECT",""));
                                    objDeductionItem.setAttribute("ACCOUNT_ID",UtilFunctions.getInt(rsDeduction,"ACCOUNT_ID",0));
                                    objDeductionItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsDeduction,"MAIN_ACCOUNT_CODE","").trim());
                                    objDeductionItem.setAttribute("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsDeduction,"SUB_ACCOUNT_CODE","").trim());
                                    objDeductionItem.setAttribute("AMOUNT",UtilFunctions.getDouble(rsDeduction,"AMOUNT",0));
                                    objDeductionItem.setAttribute("REMARKS",UtilFunctions.getString(rsDeduction,"REMARKS",""));
                                    objDeductionItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsDeduction,"CREATED_BY",""));
                                    objDeductionItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"CREATED_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsDeduction,"MODIFIED_BY",""));
                                    objDeductionItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"MODIFIED_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("CHANGED",true);
                                    objDeductionItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"CHANGED_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("CANCELLED",0);
                                    objDeductionItem.setAttribute("PO_NO",UtilFunctions.getString(rsDeduction,"PO_NO",""));
                                    objDeductionItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"PO_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsDeduction,"INVOICE_NO",""));
                                    objDeductionItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"INVOICE_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"VALUE_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("GRN_NO",UtilFunctions.getString(rsDeduction,"GRN_NO",""));
                                    objDeductionItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"GRN_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("MODULE_ID",UtilFunctions.getInt(rsDeduction,"MODULE_ID",0));
                                    objDeductionItem.setAttribute("INVOICE_AMOUNT",UtilFunctions.getDouble(rsDeduction,"INVOICE_AMOUNT",0));
                                    objDeductionItem.setAttribute("IS_DEDUCTION",UtilFunctions.getInt(rsDeduction,"IS_DEDUCTION",0));
                                    objDeductionItem.setAttribute("REF_COMPANY_ID",UtilFunctions.getInt(rsDeduction,"REF_COMPANY_ID",0));
                                    objDeductionItem.setAttribute("IS_DEDUCTION",1);
                                    objDeductionItem.setAttribute("LINK_NO",UtilFunctions.getString(rsDeduction,"LINK_NO",""));
                                    
                                    objDeductions.put(Integer.toString(objDeductions.size()+1),objDeductionItem);
                                    rsDeduction.next();
                                }
                            }
                            //*************************** End of direct deduction **********************************//
                            
                            
                            //*************** Find Debit Note Deductions for this Invoice ******************//
                            rsDeduction=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL_DOCS WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND VOUCHER_SR_NO="+VoucherSrNo+" ",FinanceGlobal.FinURL);
                            rsDeduction.first();
                            
                            if(rsDeduction.getRow()>0) {
                                while(!rsDeduction.isAfterLast()) {
                                    clsVoucherItem objDeductionItem = new clsVoucherItem();
                                    
                                    DeductionAmount+=UtilFunctions.getDouble(rsDeduction,"AMOUNT",0);
                                    
                                    objDeductionItem.setAttribute("DOC_TYPE",UtilFunctions.getInt(rsDeduction,"DOC_TYPE",0)); //Debit Note Deduction
                                    objDeductionItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsDeduction,"COMPANY_ID", 0));
                                    objDeductionItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsDeduction,"VOUCHER_NO",""));
                                    objDeductionItem.setAttribute("VOUCHER_SR_NO",VoucherSrNo);
                                    objDeductionItem.setAttribute("SR_NO",UtilFunctions.getInt(rsDeduction,"SR_NO",0));
                                    objDeductionItem.setAttribute("DOC_NO",UtilFunctions.getString(rsDeduction,"DOC_NO",""));
                                    objDeductionItem.setAttribute("DOC_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"DOC_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("AMOUNT",UtilFunctions.getDouble(rsDeduction,"AMOUNT",0));
                                    objDeductionItem.setAttribute("REMARKS",UtilFunctions.getString(rsDeduction,"REMARKS",""));
                                    objDeductionItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsDeduction,"CREATED_BY",""));
                                    objDeductionItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"CREATED_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsDeduction,"MODIFIED_BY",""));
                                    objDeductionItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"MODIFIED_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("CHANGED",true);
                                    objDeductionItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDeduction,"CHANGED_DATE","0000-00-00")));
                                    objDeductionItem.setAttribute("CANCELLED",0);
                                    
                                    objDeductions.put(Integer.toString(objDeductions.size()+1),objDeductionItem);
                                    rsDeduction.next();
                                }
                            }
                            //*************************** End of direct deduction **********************************//
                            
                            
                            objItem.setAttribute("INVOICE_AMOUNT",UtilFunctions.getDouble(rsTmp,"INVOICE_AMOUNT",0)+DeductionAmount);
                            objItem.setAttribute("INVOICE_DEDUCTIONS",objDeductions);
                            objItem.setAttribute("DEDUCTION_AMOUNT",DeductionAmount);
                            objItem.setAttribute("IS_DEDUCTION",0);
                            //************************ End of Deduction Findings ******************//
                            
                            objPayment.colVoucherItems.put(Integer.toString(objPayment.colVoucherItems.size()+1) ,objItem);
                            
                            rsTmp.next();
                        }
                    }
                    rsInvoices.next();
                }
            }
            objPayment.setAttribute("CHEQUE_AMOUNT",ChequeAmount);
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
        
    }
    
    public boolean UpdateForAdjustment() {
        Statement stHistory,stHDetail,stHeader;
        ResultSet rsHistory,rsHDetail,rsHeader,rsTmp;
        boolean Validate=true;
        int OldHierarchy=0;
        
        try {            
            
            String AStatus=getAttribute("APPROVAL_STATUS").getString();
            String theDocNo=getAttribute("VOUCHER_NO").getString();
            
            // new code by vivek on 26/02/2014
            int RevNo=0; // new line by vivek on 26/02/2014 for migrated voucher
            
            if(!getAttribute("VOUCHER_NO").getString().startsWith("M")){ // new line by vivek on 26/02/2014 for migrated voucher
                
                RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM D_FIN_VOUCHER_DETAIL_H WHERE COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+" AND VOUCHER_NO='"+getAttribute("VOUCHER_NO").getString()+"'",FinanceGlobal.FinURL);
                RevNo++;
                
            } // new line by vivek on 26/02/2014 for migrated voucher
            
            String RevDocNo=getAttribute("VOUCHER_NO").getString();
            VoucherType = clsVoucher.getVoucherType(RevDocNo);
            data.Execute("DELETE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+" AND VOUCHER_NO='"+theDocNo+"'",FinanceGlobal.FinURL);
            data.Execute("DELETE FROM D_FIN_VOUCHER_DETAIL_EX WHERE COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+" AND VOUCHER_NO='"+theDocNo+"'",FinanceGlobal.FinURL);
            data.Execute("DELETE FROM D_FIN_VOUCHER_DETAIL_DOCS WHERE COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+" AND VOUCHER_NO='"+theDocNo+"'",FinanceGlobal.FinURL);
            
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FIN_VOUCHER_HEADER_H WHERE VOUCHER_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FIN_VOUCHER_DETAIL_H WHERE VOUCHER_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            ResultSet rsTmpEx;
            Statement tmpStmt,tmpStmtEx;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='1'");
            
            int CreditCount=0,DebitCount=0;
            tmpStmtEx=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmpEx=tmpStmtEx.executeQuery("SELECT * FROM D_FIN_VOUCHER_DETAIL_EX WHERE VOUCHER_NO='1'");
            
            Statement stDetailDocs=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsDetailDocs=stDetailDocs.executeQuery("SELECT * FROM D_FIN_VOUCHER_DETAIL_DOCS LIMIT 1");
            
            Statement stDetailDocsH=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsDetailDocsH=stDetailDocsH.executeQuery("SELECT * FROM D_FIN_VOUCHER_DETAIL_DOCS_H LIMIT 1");
            
            int Counter=0;
            
            //Now Insert records into detail table
            for(int i=1;i<=colVoucherItems.size();i++) {
                clsVoucherItem objItem=(clsVoucherItem) colVoucherItems.get(Integer.toString(i));
                
                if(objItem.getAttribute("EFFECT").getString().equals("C")) {
                    CreditCount++;
                }
                else {
                    DebitCount++;
                }
                
                Counter++;
                
                rsTmp.moveToInsertRow();
                rsTmp.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                rsTmp.updateString("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
                rsTmp.updateInt("SR_NO",objItem.getAttribute("SR_NO").getInt());
                rsTmp.updateString("EFFECT",objItem.getAttribute("EFFECT").getString());
                rsTmp.updateInt("ACCOUNT_ID",objItem.getAttribute("ACCOUNT_ID").getInt());
                
                
                rsTmp.updateString("MAIN_ACCOUNT_CODE",objItem.getAttribute("MAIN_ACCOUNT_CODE").getString().trim());
                rsTmp.updateString("SUB_ACCOUNT_CODE",objItem.getAttribute("SUB_ACCOUNT_CODE").getString().trim());
                rsTmp.updateDouble("AMOUNT",objItem.getAttribute("AMOUNT").getDouble());
                rsTmp.updateDouble("APPLICABLE_AMOUNT",objItem.getAttribute("APPLICABLE_AMOUNT").getDouble());
                rsTmp.updateDouble("PERCENTAGE",objItem.getAttribute("PERCENTAGE").getDouble());
                rsTmp.updateString("REMARKS",objItem.getAttribute("REMARKS").getString());
                rsTmp.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsTmp.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsTmp.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateInt("CANCELLED",0);
                
                rsTmp.updateString("PO_NO",objItem.getAttribute("PO_NO").getString());
                rsTmp.updateString("PO_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("PO_DATE").getString()));
                rsTmp.updateString("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                rsTmp.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("INVOICE_DATE").getString()));
                rsTmp.updateString("VALUE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("VALUE_DATE").getString()));
                rsTmp.updateString("GRN_NO",objItem.getAttribute("GRN_NO").getString());
                rsTmp.updateString("GRN_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("GRN_DATE").getString()));
                rsTmp.updateInt("MODULE_ID",objItem.getAttribute("MODULE_ID").getInt());
                rsTmp.updateDouble("INVOICE_AMOUNT",objItem.getAttribute("INVOICE_AMOUNT").getDouble());
                rsTmp.updateInt("IS_DEDUCTION",objItem.getAttribute("IS_DEDUCTION").getInt());
                rsTmp.updateInt("REF_COMPANY_ID",objItem.getAttribute("REF_COMPANY_ID").getInt());
                rsTmp.updateString("LINK_NO",objItem.getAttribute("LINK_NO").getString());
                
                rsTmp.updateString("REF_VOUCHER_NO",objItem.getAttribute("REF_VOUCHER_NO").getString());
                rsTmp.updateInt("REF_VOUCHER_TYPE",objItem.getAttribute("REF_VOUCHER_TYPE").getInt());
                rsTmp.updateInt("REF_VOUCHER_COMPANY_ID",objItem.getAttribute("REF_VOUCHER_COMPANY_ID").getInt());
                rsTmp.updateInt("REF_SR_NO",objItem.getAttribute("REF_SR_NO").getInt());
                
                try {
                    rsTmp.updateInt("MATCHED",objItem.getAttribute("MATCHED").getInt());
                    rsTmp.updateString("MATCHED_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("MATCHED_DATE").getString()));
                } catch(Exception c){}
                try {
                    rsTmp.updateInt("REF_SR_NO",objItem.getAttribute("REF_SR_NO").getInt());
                } catch(Exception c){}
                rsTmp.insertRow();
                
                Counter++;
                
                // new code by vivek
                if(!getAttribute("VOUCHER_NO").getString().startsWith("M")){ // new line by vivek on 26/02/2014 for migrated voucher
                    
                    rsHDetail.moveToInsertRow();
                    rsHDetail.updateInt("REVISION_NO",RevNo);
                    rsHDetail.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                    rsHDetail.updateString("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
                    rsHDetail.updateInt("SR_NO",objItem.getAttribute("SR_NO").getInt());
                    rsHDetail.updateString("EFFECT",objItem.getAttribute("EFFECT").getString());
                    rsHDetail.updateInt("ACCOUNT_ID",objItem.getAttribute("ACCOUNT_ID").getInt());
                    rsHDetail.updateString("MAIN_ACCOUNT_CODE",objItem.getAttribute("MAIN_ACCOUNT_CODE").getString().trim());
                    rsHDetail.updateString("SUB_ACCOUNT_CODE",objItem.getAttribute("SUB_ACCOUNT_CODE").getString().trim());
                    rsHDetail.updateDouble("AMOUNT",objItem.getAttribute("AMOUNT").getDouble());
                    rsHDetail.updateDouble("APPLICABLE_AMOUNT",objItem.getAttribute("APPLICABLE_AMOUNT").getDouble());
                    rsHDetail.updateDouble("PERCENTAGE",objItem.getAttribute("PERCENTAGE").getDouble());
                    rsHDetail.updateString("REMARKS",objItem.getAttribute("REMARKS").getString());
                    rsHDetail.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                    rsHDetail.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsHDetail.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                    rsHDetail.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsHDetail.updateBoolean("CHANGED",true);
                    rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsHDetail.updateInt("CANCELLED",0);
                    
                    rsHDetail.updateString("PO_NO",objItem.getAttribute("PO_NO").getString());
                    rsHDetail.updateString("PO_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("PO_DATE").getString()));
                    rsHDetail.updateString("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                    rsHDetail.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("INVOICE_DATE").getString()));
                    rsHDetail.updateString("VALUE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("VALUE_DATE").getString()));
                    rsHDetail.updateString("GRN_NO",objItem.getAttribute("GRN_NO").getString());
                    rsHDetail.updateString("GRN_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("GRN_DATE").getString()));
                    rsHDetail.updateInt("MODULE_ID",objItem.getAttribute("MODULE_ID").getInt());
                    rsHDetail.updateDouble("INVOICE_AMOUNT",objItem.getAttribute("INVOICE_AMOUNT").getDouble());
                    rsHDetail.updateInt("IS_DEDUCTION",objItem.getAttribute("IS_DEDUCTION").getInt());
                    rsHDetail.updateInt("REF_COMPANY_ID",objItem.getAttribute("REF_COMPANY_ID").getInt());
                    rsHDetail.updateString("LINK_NO",objItem.getAttribute("LINK_NO").getString());
                    
                    rsHDetail.updateString("REF_VOUCHER_NO",objItem.getAttribute("REF_VOUCHER_NO").getString());
                    rsHDetail.updateInt("REF_VOUCHER_TYPE",objItem.getAttribute("REF_COMPANY_ID").getInt());
                    rsHDetail.updateInt("REF_VOUCHER_COMPANY_ID",objItem.getAttribute("REF_COMPANY_ID").getInt());
                    rsHDetail.updateInt("REF_SR_NO",objItem.getAttribute("REF_COMPANY_ID").getInt());
                    try {
                        rsHDetail.updateInt("MATCHED",objItem.getAttribute("MATCHED").getInt());
                        rsHDetail.updateString("MATCHED_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("MATCHED_DATE").getString()));
                    }catch(Exception c){}
                    try {
                        rsHDetail.updateInt("REF_SR_NO",objItem.getAttribute("REF_SR_NO").getInt());
                    }catch(Exception c){}
                    rsHDetail.insertRow();
                    
                } // new line by vivek on 26/02/2014 for migrated voucher
                
                
                //************ Insert Deduction document nos. *******************//
                for(int j=1;j<=objItem.colVoucherDetailDocs.size();j++) {
                    clsVoucherItem objDetailDoc=(clsVoucherItem)objItem.colVoucherDetailDocs.get(Integer.toString(j));
                    
                    rsDetailDocs.moveToInsertRow();
                    rsDetailDocs.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                    rsDetailDocs.updateString("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
                    rsDetailDocs.updateInt("VOUCHER_SR_NO",i);
                    rsDetailDocs.updateInt("SR_NO",j);
                    rsDetailDocs.updateString("DOC_NO",objDetailDoc.getAttribute("DOC_NO").getString());
                    rsDetailDocs.updateString("DOC_DATE",EITLERPGLOBAL.formatDateDB(objDetailDoc.getAttribute("DOC_DATE").getString()));
                    rsDetailDocs.updateInt("DOC_TYPE", objDetailDoc.getAttribute("DOC_TYPE").getInt());
                    rsDetailDocs.updateDouble("AMOUNT",objDetailDoc.getAttribute("AMOUNT").getDouble());
                    rsDetailDocs.updateString("REMARKS",objDetailDoc.getAttribute("REMARKS").getString());
                    rsDetailDocs.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                    rsDetailDocs.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsDetailDocs.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                    rsDetailDocs.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsDetailDocs.updateBoolean("CHANGED",true);
                    rsDetailDocs.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsDetailDocs.updateBoolean("CANCELLED",false);
                    rsDetailDocs.insertRow();
                }
                //***************************************************************//
                
            }
            
            
            if ((VoucherType!=FinanceGlobal.TYPE_JOURNAL) && (VoucherType!=FinanceGlobal.TYPE_SALES_JOURNAL) && (VoucherType!=FinanceGlobal.TYPE_CREDIT_NOTE)) {
                //======================================================================================//
                // ******************** New Logic of Separation/Splitting Entries ********************* //
                //======================================================================================//
                String validEntryMessage="";
                
                validEntryMessage="Invalid Entry Sequence. Please specify valid sequence as example shown below";
                validEntryMessage+="\n1. Dr.  Ac. 1   50 ";
                validEntryMessage+="\n2. Dr.  Ac. 2   50 ";
                validEntryMessage+="\n3.      Cr.  Ac. 3   100 ";
                validEntryMessage+="\n4. Dr.  Ac. 3   20 ";
                validEntryMessage+="\n5.      Cr.  Ac. 5   20 ";
                
                BlockCounter=0;
                colVoucherItemsEx.clear();
                
                String CurrentEffect="",PreviousEffect="";
                double CrTotal=0,DrTotal=0;
                HashMap sEntries=new HashMap();
                
                for(int i=1;i<=colVoucherItems.size();i++) {
                    clsVoucherItem objItem=(clsVoucherItem)colVoucherItems.get(Integer.toString(i));
                    CurrentEffect=objItem.getAttribute("EFFECT").getString();
                    
                    if(sEntries.size()==0) {
                        sEntries.put(Integer.toString(sEntries.size()+1), objItem);
                        
                        if(CurrentEffect.equals("C")) {
                            CrTotal+=objItem.getAttribute("AMOUNT").getDouble();
                        }
                        else {
                            DrTotal+=objItem.getAttribute("AMOUNT").getDouble();
                        }
                        
                    }
                    else {
                        if(!CurrentEffect.equals(PreviousEffect)) {
                            if(CurrentEffect.equals("C")) {
                                double CurrentAmount=EITLERPGLOBAL.round(objItem.getAttribute("AMOUNT").getDouble(),2);
                                
                                DrTotal=EITLERPGLOBAL.round(DrTotal,2);
                                
                                if(DrTotal!=CurrentAmount) {
                                    //Not a Valid Entry
                                    LastError=validEntryMessage;
                                    return false;
                                }
                                else {
                                    sEntries.put(Integer.toString(sEntries.size()+1),objItem);
                                    
                                    //PASS ON TO SPLIT ROUTINE. FROM HERE
                                    SplitEntries(sEntries);
                                    
                                    //Now Clear it
                                    sEntries.clear();
                                    CrTotal=0;DrTotal=0;
                                }
                            }
                            else {
                                
                                double CurrentAmount=EITLERPGLOBAL.round(objItem.getAttribute("AMOUNT").getDouble(),2);
                                
                                CrTotal=EITLERPGLOBAL.round(CrTotal,2);
                                
                                if(CrTotal!=CurrentAmount) {
                                    //Not a Valid Entry
                                    LastError=validEntryMessage;
                                    return false;
                                }
                                else {
                                    sEntries.put(Integer.toString(sEntries.size()+1),objItem);
                                    
                                    //PASS ON TO SPLIT ROUTINE. FROM HERE
                                    SplitEntries(sEntries);
                                    
                                    //Now Clear it
                                    sEntries.clear();
                                    CrTotal=0;DrTotal=0;
                                }
                            }
                        }
                        else {
                            //STACK UP THE ENTRY
                            sEntries.put(Integer.toString(sEntries.size()+1),objItem);
                            
                            if(objItem.getAttribute("EFFECT").getString().equals("C")) {
                                CrTotal+=objItem.getAttribute("AMOUNT").getDouble();
                            }
                            else {
                                DrTotal+=objItem.getAttribute("AMOUNT").getDouble();
                            }
                        }
                    }
                    
                    PreviousEffect=objItem.getAttribute("EFFECT").getString();
                }
                
                for(int i=1;i<=colVoucherItemsEx.size();i++) {
                    clsVoucherItem objItem=(clsVoucherItem)colVoucherItemsEx.get(Integer.toString(i));
                    
                    rsTmpEx.moveToInsertRow();
                    rsTmpEx.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                    rsTmpEx.updateString("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
                    rsTmpEx.updateInt("SR_NO",i);
                    rsTmpEx.updateInt("BLOCK_NO",objItem.getAttribute("BLOCK_NO").getInt());
                    rsTmpEx.updateString("EFFECT",objItem.getAttribute("EFFECT").getString());
                    rsTmpEx.updateInt("ACCOUNT_ID",objItem.getAttribute("ACCOUNT_ID").getInt());
                    rsTmpEx.updateString("MAIN_ACCOUNT_CODE",objItem.getAttribute("MAIN_ACCOUNT_CODE").getString().trim());
                    rsTmpEx.updateString("SUB_ACCOUNT_CODE",objItem.getAttribute("SUB_ACCOUNT_CODE").getString().trim());
                    rsTmpEx.updateDouble("AMOUNT",objItem.getAttribute("AMOUNT").getDouble());
                    rsTmpEx.updateString("REMARKS",objItem.getAttribute("REMARKS").getString());
                    rsTmpEx.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                    rsTmpEx.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsTmpEx.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                    rsTmpEx.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsTmpEx.updateBoolean("CHANGED",true);
                    rsTmpEx.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsTmpEx.updateInt("CANCELLED",0);
                    rsTmpEx.updateString("PO_NO",objItem.getAttribute("PO_NO").getString());
                    rsTmpEx.updateString("PO_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("PO_DATE").getString()));
                    rsTmpEx.updateString("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                    rsTmpEx.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("INVOICE_DATE").getString()));
                    rsTmpEx.updateString("VALUE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("VALUE_DATE").getString()));
                    rsTmpEx.updateString("GRN_NO",objItem.getAttribute("GRN_NO").getString());
                    rsTmpEx.updateString("GRN_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("GRN_DATE").getString()));
                    rsTmpEx.updateInt("MODULE_ID",objItem.getAttribute("MODULE_ID").getInt());
                    rsTmpEx.updateDouble("INVOICE_AMOUNT",objItem.getAttribute("INVOICE_AMOUNT").getDouble());
                    rsTmpEx.updateInt("IS_DEDUCTION",objItem.getAttribute("IS_DEDUCTION").getInt());
                    rsTmpEx.updateInt("REF_COMPANY_ID",objItem.getAttribute("REF_COMPANY_ID").getInt());
                    rsTmpEx.updateString("LINK_NO",objItem.getAttribute("LINK_NO").getString());
                    try {
                        rsTmpEx.updateInt("MATCHED",objItem.getAttribute("MATCHED").getInt());
                        rsTmpEx.updateString("MATCHED_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("MATCHED_DATE").getString()));
                    } catch(Exception c){}
                    try {
                        rsTmpEx.updateInt("REF_SR_NO",objItem.getAttribute("REF_SR_NO").getInt());
                    } catch(Exception c){}
                    rsTmpEx.insertRow();
                }
                
                //======================================================================================//
                // ************************** End of Separation/Splitting Entries ********************* //
                //======================================================================================//
                
            }
            
            
            
            //********** Make entries as it is in case of Journal Voucher ***************//
            if(VoucherType==FinanceGlobal.TYPE_JOURNAL || VoucherType==FinanceGlobal.TYPE_CREDIT_NOTE) {
                for(int i=1;i<=colVoucherItems.size();i++) {
                    clsVoucherItem objItem=(clsVoucherItem)colVoucherItems.get(Integer.toString(i));
                    
                    rsTmpEx.moveToInsertRow();
                    rsTmpEx.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                    rsTmpEx.updateString("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
                    rsTmpEx.updateInt("SR_NO",i);
                    rsTmpEx.updateInt("BLOCK_NO",1); //Always one, as there is no seperation logic
                    rsTmpEx.updateString("EFFECT",objItem.getAttribute("EFFECT").getString());
                    rsTmpEx.updateInt("ACCOUNT_ID",objItem.getAttribute("ACCOUNT_ID").getInt());
                    rsTmpEx.updateString("MAIN_ACCOUNT_CODE",objItem.getAttribute("MAIN_ACCOUNT_CODE").getString().trim());
                    rsTmpEx.updateString("SUB_ACCOUNT_CODE",objItem.getAttribute("SUB_ACCOUNT_CODE").getString().trim());
                    rsTmpEx.updateDouble("AMOUNT",objItem.getAttribute("AMOUNT").getDouble());
                    rsTmpEx.updateString("REMARKS",objItem.getAttribute("REMARKS").getString());
                    rsTmpEx.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                    rsTmpEx.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsTmpEx.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                    rsTmpEx.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsTmpEx.updateBoolean("CHANGED",true);
                    rsTmpEx.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsTmpEx.updateInt("CANCELLED",0);
                    rsTmpEx.updateString("PO_NO",objItem.getAttribute("PO_NO").getString());
                    rsTmpEx.updateString("PO_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("PO_DATE").getString()));
                    rsTmpEx.updateString("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                    rsTmpEx.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("INVOICE_DATE").getString()));
                    rsTmpEx.updateString("VALUE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("VALUE_DATE").getString()));
                    rsTmpEx.updateString("GRN_NO",objItem.getAttribute("GRN_NO").getString());
                    rsTmpEx.updateString("GRN_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("GRN_DATE").getString()));
                    rsTmpEx.updateInt("MODULE_ID",objItem.getAttribute("MODULE_ID").getInt());
                    rsTmpEx.updateDouble("INVOICE_AMOUNT",objItem.getAttribute("INVOICE_AMOUNT").getDouble());
                    rsTmpEx.updateInt("IS_DEDUCTION",objItem.getAttribute("IS_DEDUCTION").getInt());
                    rsTmpEx.updateInt("REF_COMPANY_ID",objItem.getAttribute("REF_COMPANY_ID").getInt());
                    rsTmpEx.updateString("LINK_NO",objItem.getAttribute("LINK_NO").getString());
                    try {
                        rsTmpEx.updateInt("MATCHED",objItem.getAttribute("MATCHED").getInt());
                        rsTmpEx.updateString("MATCHED_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("MATCHED_DATE").getString()));
                    } catch(Exception c){}
                    try {
                        rsTmpEx.updateInt("REF_SR_NO",objItem.getAttribute("REF_SR_NO").getInt());
                    } catch(Exception c){}
                    rsTmpEx.insertRow();
                }
            }
            //***************************************************************************//
            setData();
            return true;
        }
        catch(Exception e) {
            try {
                e.printStackTrace();
            }
            catch(Exception c) {
            }
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    
    public boolean getLCJVRepresentation() {
        ResultSet rsTmp;
        ResultSet rsDeduction;
        Connection tmpConn;
        Statement tmpStmt;
        
        objPayment=new clsVoucher();
        objPayment.colVoucherItems.clear();
        objPayment.colVoucherItemsEx.clear();
        
        tmpConn=data.getConn(FinanceGlobal.FinURL);
        
        long Counter=0;
        int RevNo=0;
        int VoucherSrNo=0;
        double ChequeAmount=0;
        double DeductionAmount=0;
        
        try {
            
            int CompanyID=getAttribute("COMPANY_ID").getInt();
            String VoucherNo=getAttribute("VOUCHER_NO").getString();
            
            //General Retrieval of the Voucher
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
                objPayment.setAttribute("REVISION_NO",UtilFunctions.getInt(rsResultSet,"REVISION_NO",0));
            }
            else {
                objPayment.setAttribute("REVISION_NO",0);
            }
            
            OldVoucherNo=UtilFunctions.getString(rsResultSet,"VOUCHER_NO","");
            objPayment.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsResultSet,"COMPANY_ID",0));
            objPayment.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsResultSet,"VOUCHER_NO",""));
            objPayment.setAttribute("VOUCHER_TYPE",UtilFunctions.getInt(rsResultSet,"VOUCHER_TYPE",1));
            objPayment.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"VOUCHER_DATE","0000-00-00")));
            objPayment.setAttribute("BOOK_CODE",UtilFunctions.getString(rsResultSet,"BOOK_CODE",""));
            objPayment.setAttribute("CHEQUE_NO",UtilFunctions.getString(rsResultSet,"CHEQUE_NO",""));
            objPayment.setAttribute("CHEQUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHEQUE_DATE","0000-00-00")));
            objPayment.setAttribute("REALIZATION_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"REALIZATION_DATE","0000-00-00")));
            objPayment.setAttribute("BANK_NAME",UtilFunctions.getString(rsResultSet,"BOOK_CODE",""));
            objPayment.setAttribute("ST_CATEGORY",UtilFunctions.getString(rsResultSet,"ST_CATEGORY",""));
            objPayment.setAttribute("REASON_CODE",UtilFunctions.getString(rsResultSet,"REASON_CODE",""));
            objPayment.setAttribute("REMARKS",UtilFunctions.getString(rsResultSet,"REMARKS",""));
            objPayment.setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsResultSet,"HIERARCHY_ID",0));
            objPayment.setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            objPayment.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00")));
            objPayment.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            objPayment.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00")));
            objPayment.setAttribute("CHANGED",UtilFunctions.getInt(rsResultSet,"CHANGED",0));
            objPayment.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHANGED_DATE","0000-00-00")));
            objPayment.setAttribute("APPROVED",UtilFunctions.getInt(rsResultSet,"APPROVED",0));
            objPayment.setAttribute("APPROVED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"APPROVED_DATE","0000-00-00")));
            objPayment.setAttribute("REJECTED",UtilFunctions.getInt(rsResultSet,"REJECTED",0));
            objPayment.setAttribute("REJECTED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"REJECTED_DATE","0000-00-00")));
            objPayment.setAttribute("CANCELLED",UtilFunctions.getInt(rsResultSet,"CANCELLED",0));
            objPayment.setAttribute("LEGACY_NO",UtilFunctions.getString(rsResultSet,"LEGACY_NO",""));
            objPayment.setAttribute("LEGACY_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"LEGACY_DATE","0000-00-00")));
            
            
            String MainAccountCode=data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='D' AND MAIN_ACCOUNT_CODE<>'' AND IS_DEDUCTION=0 ",FinanceGlobal.FinURL);
            String PartyCode=data.getStringValueFromDB("SELECT SUB_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='D' AND MAIN_ACCOUNT_CODE<>'' AND SUB_ACCOUNT_CODE<>'' AND IS_DEDUCTION=0",FinanceGlobal.FinURL);
            
            
            objPayment.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode.trim());
            objPayment.setAttribute("PARTY_CODE",PartyCode);
            objPayment.setAttribute("BANK_CODE","");
            objPayment.setAttribute("CHEQUE_AMOUNT",0);
            
            
            objPayment.colVoucherItems.clear();
            
            
            rsTmp=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND EFFECT='C' " ,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    
                    clsVoucherItem objItem = new clsVoucherItem();
                    
                    
                    objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                    objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                    objItem.setAttribute("SR_NO",UtilFunctions.getInt(rsTmp,"SR_NO",0));
                    objItem.setAttribute("EFFECT",UtilFunctions.getString(rsTmp,"EFFECT",""));
                    objItem.setAttribute("ACCOUNT_ID",UtilFunctions.getInt(rsTmp,"ACCOUNT_ID",0));
                    objItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","").trim());
                    objItem.setAttribute("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"SUB_ACCOUNT_CODE",""));
                    objItem.setAttribute("AMOUNT",UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                    objItem.setAttribute("REMARKS",UtilFunctions.getString(rsTmp,"REMARKS",""));
                    objItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsTmp,"CREATED_BY",""));
                    objItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CREATED_DATE","0000-00-00")));
                    objItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsTmp,"MODIFIED_BY",""));
                    objItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MODIFIED_DATE","0000-00-00")));
                    objItem.setAttribute("CHANGED",true);
                    objItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CHANGED_DATE","0000-00-00")));
                    objItem.setAttribute("CANCELLED",0);
                    objItem.setAttribute("PO_NO",UtilFunctions.getString(rsTmp,"PO_NO",""));
                    objItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"PO_DATE","0000-00-00")));
                    objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                    objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                    objItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VALUE_DATE","0000-00-00")));
                    objItem.setAttribute("GRN_NO",UtilFunctions.getString(rsTmp,"GRN_NO",""));
                    objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"GRN_DATE","0000-00-00")));
                    objItem.setAttribute("MODULE_ID",UtilFunctions.getInt(rsTmp,"MODULE_ID",0));
                    objItem.setAttribute("REF_COMPANY_ID",UtilFunctions.getInt(rsTmp,"REF_COMPANY_ID",0));
                    objItem.setAttribute("INVOICE_AMOUNT",UtilFunctions.getDouble(rsTmp,"INVOICE_AMOUNT",0));
                    objItem.setAttribute("IS_DEDUCTION",0);
                    objItem.setAttribute("LINK_NO",UtilFunctions.getString(rsTmp,"LINK_NO",""));
                    objItem.setAttribute("MATCHED",UtilFunctions.getInt(rsTmp,"MATCHED",0));
                    objItem.setAttribute("MATCHED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MATCHED_DATE","0000-00-00")));
                    objPayment.colVoucherItems.put(Integer.toString(objPayment.colVoucherItems.size()+1) ,objItem);
                    
                    rsTmp.next();
                }
            }
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean PostReceipt(String VoucherNo,String MainAccountCode) {
        try {
            ResultSet rsVoucher = data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo+"' AND EFFECT='C' AND SUB_ACCOUNT_CODE<>'' AND MAIN_ACCOUNT_CODE IN ('132642','132635','132714') ",FinanceGlobal.FinURL);
            rsVoucher.first();
            if(rsVoucher.getRow()>0) {
                while(!rsVoucher.isAfterLast()) {
                    String PartyCode = rsVoucher.getString("SUB_ACCOUNT_CODE"); //data.getStringValueFromDB("SELECT SUB_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo+"' AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND EFFECT='C'",FinanceGlobal.FinURL);
                    MainAccountCode = rsVoucher.getString("MAIN_ACCOUNT_CODE");
                    String SQL = "SELECT PARTY_NAME,ADDRESS1,ADDRESS2,CITY_NAME,PINCODE,PHONE_NO,MOBILE_NO,EMAIL FROM D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+PartyCode+"' "; //AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"'
                    ResultSet rsParty = data.getResult(SQL);
                    rsParty.first();
                    String PartyName = UtilFunctions.getString(rsParty,"PARTY_NAME", "");
                    String Address1 = UtilFunctions.getString(rsParty,"ADDRESS1", "");
                    String Address2 = UtilFunctions.getString(rsParty,"ADDRESS2", "");
                    String City = UtilFunctions.getString(rsParty,"CITY_NAME", "");
                    String Pincode = UtilFunctions.getString(rsParty,"PINCODE", "");
                    String PhoneNo = UtilFunctions.getString(rsParty,"PHONE_NO", "");
                    String MobileNo = UtilFunctions.getString(rsParty,"MOBILE_NO", "");
                    String Email = UtilFunctions.getString(rsParty,"EMAIL", "");
                    String ContactNo = "";
                    if(!PhoneNo.equals("")) {
                        ContactNo += "\nPHONE : " + PhoneNo +" ";
                    }
                    if(!MobileNo.equals("")) {
                        ContactNo += "\nMOBILE : " + MobileNo +" ";
                    }
                    if(!Email.equals("")) {
                        ContactNo += "\nEMAIL : " + Email +" ";
                    }
                    
                    String InterestMainCode = data.getStringValueFromDB("SELECT INT_MAIN_ACCOUNT_CODE FROM D_FD_SCHEME_MASTER WHERE DEPOSIT_MAIN_ACCOUNT_CODE='"+MainAccountCode+"' ",FinanceGlobal.FinURL);
                    String SchemeID = data.getStringValueFromDB("SELECT SCHEME_ID FROM D_FD_SCHEME_MASTER WHERE DEPOSIT_MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND INT_MAIN_ACCOUNT_CODE='"+InterestMainCode+"' ",FinanceGlobal.FinURL);
                    double InterestRate = data.getDoubleValueFromDB("SELECT INTEREST_PERCENTAGE FROM D_FD_SCHEME_PERIOD WHERE SCHEME_ID='"+SchemeID+"' ",FinanceGlobal.FinURL);
                    String EffectiveDate = data.getStringValueFromDB("SELECT VALUE_DATE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo+"' AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND EFFECT='C' ",FinanceGlobal.FinURL);
                    int DepositerStatus = 4;
                    String DepositerCategory = data.getStringValueFromDB("SELECT CATEGORY FROM D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+PartyCode+"' ");
                    int DepositerCategoryID = 0;
                    if(DepositerCategory.trim().equals("Company")) {
                        DepositerCategoryID = 1;
                    } else {
                        DepositerCategoryID = 3;
                    }
                    
                    int DepositPayableTo = 1;
                    int DepositTypeID = 1;
                    String PanNo = data.getStringValueFromDB("SELECT PAN_NO FROM D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+PartyCode+"' ");
                    String PanDate = data.getStringValueFromDB("SELECT PAN_DATE FROM D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+PartyCode+"' ");
                    int TaxFormReceived = 0;
                    int TDSApplicable = 1;
                    String Particular = data.getStringValueFromDB("SELECT REMARKS FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+VoucherNo+"' ",FinanceGlobal.FinURL);
                    
                    String ChequeNo = data.getStringValueFromDB("SELECT CHEQUE_NO FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+VoucherNo+"' ",FinanceGlobal.FinURL);
                    String ChequeDate = data.getStringValueFromDB("SELECT CHEQUE_DATE FROM D_FIN_VOUCHER_HEAER WHERE VOUCHER_NO='"+VoucherNo+"' ",FinanceGlobal.FinURL);
                    double Amount = data.getDoubleValueFromDB("SELECT AMOUNT FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo+"' AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND EFFECT='C'",FinanceGlobal.FinURL);
                    String BankMainCode = "";
                    String FundTransfer = "";
                    if(data.getIntValueFromDB("SELECT VOUCHER_TYPE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+VoucherNo+"' ",FinanceGlobal.FinURL)==FinanceGlobal.TYPE_RECEIPT) {
                        BankMainCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo+"' AND EFFECT='D'",FinanceGlobal.FinURL);
                    }
                    if(data.getIntValueFromDB("SELECT VOUCHER_TYPE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+VoucherNo+"' ",FinanceGlobal.FinURL)==FinanceGlobal.TYPE_JOURNAL) {
                        FundTransfer = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL_EX WHERE VOUCHER_NO='"+VoucherNo+"' AND EFFECT='D' AND AMOUNT='"+Amount+"' ",FinanceGlobal.FinURL);
                    }
                    String VoucherDate = data.getStringValueFromDB("SELECT VOUCHER_DATE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+VoucherNo+"' ",FinanceGlobal.FinURL);
                    String Remarks = data.getStringValueFromDB("SELECT REMARKS FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+VoucherNo+"' ",FinanceGlobal.FinURL);
                    
                    clsSalesDepositMaster ObjSDM = new clsSalesDepositMaster();
                    ObjSDM.LoadData(EITLERPGLOBAL.gCompanyID);
                    String SelPrefix="";
                    String SelSuffix="";
                    int FFNo=0;
                    
                    //****** Prepare Voucher Object ********//
                    setAttribute("FIN_HIERARCHY_ID",0);
                    
                    HashMap List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesDepositMaster.ModuleID , "CHOOSE_HIERARCHY", "DEFAULT", "");
                    
                    if(List.size()>0) {
                        //Get the Result of the Rule which would be the hierarchy no.
                        clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                        int HierarchyID=UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                        setAttribute("FIN_HIERARCHY_ID",HierarchyID);
                    }
                    
                    ResultSet rsTmp=data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID="+clsSalesDepositMaster.ModuleID);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        SelPrefix=rsTmp.getString("PREFIX_CHARS");
                        SelSuffix=rsTmp.getString("SUFFIX_CHARS");
                        FFNo=rsTmp.getInt("FIRSTFREE_NO");
                    }
                    
                    String newReceiptNo = clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID,clsSalesDepositMaster.ModuleID,FFNo, false);
                    
                    //Applicant Detail
                    ObjSDM.setAttribute("FFNO",FFNo);
                    ObjSDM.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    ObjSDM.setAttribute("RECEIPT_NO",newReceiptNo);
                    ObjSDM.setAttribute("RECEIPT_DATE",EITLERPGLOBAL.formatDate(VoucherDate));
                    ObjSDM.setAttribute("TITLE","M/S");
                    ObjSDM.setAttribute("APPLICANT_NAME",PartyName);
                    ObjSDM.setAttribute("ADDRESS1",Address1);
                    ObjSDM.setAttribute("ADDRESS2",Address2);
                    ObjSDM.setAttribute("ADDRESS3","");
                    ObjSDM.setAttribute("CITY",City);
                    ObjSDM.setAttribute("PINCODE",Pincode);
                    ObjSDM.setAttribute("CONTACT_NO",ContactNo);
                    
                    //Other Detail
                    
                    ObjSDM.setAttribute("SCHEME_ID",SchemeID);
                    ObjSDM.setAttribute("DEPOSIT_TYPE_ID",DepositTypeID);
                    ObjSDM.setAttribute("DEPOSIT_PAYABLE_TO",DepositPayableTo);
                    ObjSDM.setAttribute("DEPOSITER_STATUS",DepositerStatus);
                    ObjSDM.setAttribute("DEPOSITER_CATEGORY",DepositerCategoryID);
                    
                    ObjSDM.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode.trim());
                    ObjSDM.setAttribute("INTEREST_MAIN_CODE",InterestMainCode);
                    
                    ObjSDM.setAttribute("INTEREST_RATE",InterestRate);
                    ObjSDM.setAttribute("DEPOSITER_CATEGORY_OTHERS","");
                    
                    //Extra Calculation
                    
                    //int InterestCalcPeriod = data.getIntValueFromDB("SELECT INTEREST_CALCULATION_PERIOD FROM D_FD_SCHEME_MASTER WHERE SCHEME='"+SchemeID+"' ",FinanceGlobal.FinURL);
                    String InterestCalcDate = EITLERPGLOBAL.getFinYearEndDate(EffectiveDate);
                    ObjSDM.setAttribute("EFFECTIVE_DATE",EITLERPGLOBAL.formatDate(EffectiveDate));
                    ObjSDM.setAttribute("INT_CALC_DATE",EITLERPGLOBAL.formatDate(InterestCalcDate));
                    ObjSDM.setAttribute("REFUND_DATE",EITLERPGLOBAL.formatDate("0000-00-00"));
                    //End of Extra Calculation
                    
                    ObjSDM.setAttribute("TAX_EX_FORM_RECEIVED",0);
                    ObjSDM.setAttribute("PARTY_CODE",PartyCode);
                    ObjSDM.setAttribute("TDS_APPLICABLE",1);
                    
                    ObjSDM.setAttribute("PAN_NO",PanNo);
                    ObjSDM.setAttribute("PAN_DATE",EITLERPGLOBAL.formatDate(PanDate));
                    ObjSDM.setAttribute("PARTICULARS",Remarks);
                    
                    //Bank Detail
                    ObjSDM.setAttribute("CHEQUE_NO",ChequeNo);
                    ObjSDM.setAttribute("CHEQUE_DATE",ChequeDate);
                    ObjSDM.setAttribute("REALIZATION_DATE",EITLERPGLOBAL.formatDate(EffectiveDate));
                    ObjSDM.setAttribute("AMOUNT",Amount);
                    
                    ObjSDM.setAttribute("FUND_TRANSFER_FROM",FundTransfer);
                    ObjSDM.setAttribute("BANK_MAIN_CODE",BankMainCode);
                    ObjSDM.setAttribute("BANK_NAME",data.getStringValueFromDB("SELECT BOOK_NAME FROM D_FIN_BOOK_MASTER WHERE MAIN_ACCOUNT_CODE='"+BankMainCode+"' ",FinanceGlobal.FinURL));
                    ObjSDM.setAttribute("BANK_ADDRESS","");
                    ObjSDM.setAttribute("BANK_CITY","");
                    ObjSDM.setAttribute("BANK_PINCODE","");
                    
                    //Deposit Releated Information
                    
                    ObjSDM.setAttribute("DEPOSIT_STATUS",0); // 0 for Open
                    
                    ObjSDM.setAttribute("HIERARCHY_ID",getAttribute("FIN_HIERARCHY_ID").getInt());
                    int FirstUserID=data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID="+getAttribute("FIN_HIERARCHY_ID").getInt()+" AND SR_NO=1");
                    ObjSDM.setAttribute("FROM",FirstUserID);
                    ObjSDM.setAttribute("TO",FirstUserID);
                    ObjSDM.setAttribute("FROM_REMARKS","Ref. to " + VoucherNo);
                    ObjSDM.setAttribute("APPROVAL_STATUS","F"); //Hold Receipt
                    
                    if(ObjSDM.Insert()) {
                        String ReceiptNo = ObjSDM.publicReceiptNo;
                        SQL = "UPDATE D_FD_SALES_DEPOSIT_MASTER SET REF_VOUCHER_NO='"+VoucherNo+"',CHANGED=1 " +
                        "WHERE RECEIPT_NO='"+ReceiptNo+"' AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND PARTY_CODE='"+PartyCode+"' ";
                        data.Execute(SQL,FinanceGlobal.FinURL);
                        
                        // NOT IN USE DEVLOPED AND COMMENTED ON 22/09/2010
                        /*SQL = "UPDATE D_FIN_VOUCHER_DETAIL SET GRN_NO='"+VoucherNo+"', GRN_DATE='"+VoucherDate+"', MODULE_ID="+clsSalesDepositMaster.ModuleID+" " +
                        "WHERE VOUCHER_NO='"+VoucherNo+"' AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND PARTY_CODE='"+PartyCode+"' AND EFFECT='C' " +
                        "AND AMOUNT="+Amount+" ";
                        data.Execute(SQL,FinanceGlobal.FinURL);
                        SQL = "UPDATE D_FIN_VOUCHER_DETAIL_EX SET GRN_NO='"+VoucherNo+"', GRN_DATE='"+VoucherDate+"', MODULE_ID="+clsSalesDepositMaster.ModuleID+" " +
                        "WHERE VOUCHER_NO='"+VoucherNo+"' AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND PARTY_CODE='"+PartyCode+"' AND EFFECT='C' " +
                        "AND AMOUNT="+Amount+" ";
                        data.Execute(SQL,FinanceGlobal.FinURL);*/
                        // NOT IN USE DEVLOPED AND COMMENTED ON 22/09/2010
                        //return true;
                    } else {
                        return false;
                    }
                    rsVoucher.next();
                }
            }
            
        } catch(Exception e) {
            return false;
        }
        return true;
    }
    
    private void generateNextLegacyNo(String cVoucherNo) {
        
        int cBookCode = data.getIntValueFromDB("SELECT BOOK_CODE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+cVoucherNo+"' ",FinanceGlobal.FinURL);
        int cVoucherType = data.getIntValueFromDB("SELECT VOUCHER_TYPE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+cVoucherNo+"' ",FinanceGlobal.FinURL);
        String sVoucherType = Integer.toString(cVoucherType);
        String cDate = data.getStringValueFromDB("SELECT VOUCHER_DATE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+cVoucherNo+"' ",FinanceGlobal.FinURL);
        String FinStartDate = EITLERPGLOBAL.getFinYearStartDate(cDate);
        String FinEndDate = EITLERPGLOBAL.getFinYearEndDate(cDate);
        String FinRef = Integer.toString(EITLERPGLOBAL.FinYearFrom).substring(2);
        if(cVoucherType==10 || cVoucherType==2) {
            sVoucherType = "2,10";
        }
        String Condition="";
        if(cVoucherType == FinanceGlobal.TYPE_CASH_RECEIPT_VOUCHER) {
            Condition = " AND CANCELLED=0 ";
        } else {
            Condition = " AND APPROVED=1 AND CANCELLED=0 ";
        }
        String strSQL = "";
        if(cVoucherType==FinanceGlobal.TYPE_CASH_VOUCHER) {
            strSQL = "SELECT MAX(CONVERT(LEGACY_NO,SIGNED)) AS NEXT_LEGACY_NO FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND VOUCHER_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' " +
            "AND BOOK_CODE="+cBookCode+" AND VOUCHER_TYPE IN ("+sVoucherType+")  " + Condition;
        } else {
            strSQL = "SELECT MAX(CONVERT(LEGACY_NO,SIGNED)) AS NEXT_LEGACY_NO FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND VOUCHER_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' " +
            "AND BOOK_CODE="+cBookCode+" AND VOUCHER_TYPE IN ("+sVoucherType+") AND SUBSTRING(VOUCHER_NO,3,2)='"+FinRef+"' " + Condition;
        }
        
        int nextLegacyNo = data.getIntValueFromDB(strSQL,FinanceGlobal.FinURL)+1;
        strSQL = "UPDATE D_FIN_VOUCHER_HEADER SET LEGACY_NO='"+Integer.toString(nextLegacyNo)+"', LEGACY_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"' WHERE VOUCHER_NO='"+cVoucherNo+"' ";
        data.Execute(strSQL,FinanceGlobal.FinURL);
        strSQL = "UPDATE D_FIN_VOUCHER_HEADER_H SET LEGACY_NO='"+Integer.toString(nextLegacyNo)+"', LEGACY_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"' WHERE VOUCHER_NO='"+cVoucherNo+"' ";
        data.Execute(strSQL,FinanceGlobal.FinURL);
    }
    
    public static void PostDebitNote(String currentVoucherNo) {
        ResultSet rsParty=null,rsInvoice=null,rsVoucher=null;
        clsVoucher ObjVoucher = null;
        clsDebitNoteReceiptMapping ObjRefItem=null;
        String CurrentVoucherDate = data.getStringValueFromDB("SELECT VOUCHER_DATE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+currentVoucherNo+"' ",FinanceGlobal.FinURL);
        String strSQL = "SELECT DISTINCT SUB_ACCOUNT_CODE,MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+currentVoucherNo+"' " +
        "AND EFFECT='C' AND SUB_ACCOUNT_CODE<>'' AND INVOICE_NO<>'' AND MAIN_ACCOUNT_CODE IN ('210010','210027') ORDER BY MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE";
        try {
            rsParty = data.getResult(strSQL, FinanceGlobal.FinURL);
            rsParty.first();
            String PartyCode = "",MainAccountCode="",InvoiceNo = "",InvoiceDate="",ChargeCode="",DeductionCode="",DebitNoteBookCode="",InvoiceDueDate="";
            int InvoiceType = 0;
            double PartyDebitNoteAmount=0,InvoiceDebitNoteAmount = 0,VoucherDebitNoteAmount=0, InterestPercentage=0, InvoicePaidAmount=0,InvoiceAmount=0;
            if(rsParty.getRow() > 0) {
                while(!rsParty.isAfterLast()) {
                    PartyCode = UtilFunctions.getString(rsParty, "SUB_ACCOUNT_CODE", "");
                    MainAccountCode = UtilFunctions.getString(rsParty, "MAIN_ACCOUNT_CODE", "");
                    
                    strSQL = "SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+currentVoucherNo+"' AND EFFECT='C' " +
                    "AND INVOICE_NO<>'' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' ";
                    rsInvoice  = data.getResult(strSQL, FinanceGlobal.FinURL);
                    rsInvoice.first();
                    PartyDebitNoteAmount=0;
                    ObjRefItem = new clsDebitNoteReceiptMapping();
                    ObjRefItem.colMappingDetail.clear();
                    while(!rsInvoice.isAfterLast()) {
                        InvoiceNo = UtilFunctions.getString(rsInvoice, "INVOICE_NO", "");
                        InvoiceDate = UtilFunctions.getString(rsInvoice, "INVOICE_DATE","0000-00-00");
                        InvoiceType = clsSalesInvoice.getInvoiceType(InvoiceNo, InvoiceDate);
                        ChargeCode = clsSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate);
                        InvoiceAmount = clsSalesInvoice.getInvoiceAmount(InvoiceNo,InvoiceDate);
                        InvoicePaidAmount = clsSalesInvoice.getInvoicePaidAmount(InvoiceNo,InvoiceDate);
                        strSQL = "SELECT * FROM D_FIN_DEBITNOTE_RECEIPT_MAPPING WHERE RECEIPT_VOUCHER_NO='"+currentVoucherNo+"' " +
                        "AND INVOICE_NO='"+InvoiceNo+"' AND INVOICE_DATE='"+InvoiceDate+"' AND APPROVED=1 AND CANCELLED=0 ";
                        if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                            rsInvoice.next();
                            continue;
                        }
                        if(InvoiceAmount > InvoicePaidAmount) { // by pass the invoice if invoice in not fully paid for debit note
                            rsInvoice.next();
                            continue;
                        }
                        if(!clsSalesInvoice.canDebitNotePost(InvoiceType,ChargeCode)) {
                            rsInvoice.next();
                            continue;
                        }
                        if(InvoiceType==2 && (PartyCode.equals("812081")||PartyCode.equals("813003")||PartyCode.equals("828001")||PartyCode.equals("828003"))) {
                            rsInvoice.next();
                            continue;
                        }
                        InvoiceDueDate = data.getStringValueFromDB("SELECT DUE_DATE FROM DINESHMILLS.D_SAL_INVOICE_HEADER WHERE INVOICE_NO='"+InvoiceNo+"' AND INVOICE_DATE='"+InvoiceDate+"' AND APPROVED=1 AND CANCELLED=0 ");
                         //CHANGE BY RISHI ACCORDING TO ATUL 
                        if(InvoiceType==2 && ChargeCode.equals("82")){
                            InvoiceDueDate = data.getStringValueFromDB("SELECT ADDDATE(DUE_DATE,6) FROM DINESHMILLS.D_SAL_INVOICE_HEADER WHERE INVOICE_NO='"+InvoiceNo+"' AND INVOICE_DATE='"+InvoiceDate+"' AND APPROVED=1 AND CANCELLED=0 AND INVOICE_TYPE =2 AND SUBSTRING(PAYMENT_TERM_CODE,1,1) =8 ");
                            
                        }
                        if(InvoiceDueDate.equals("")) {
                            JOptionPane.showMessageDialog(null,"Due Date not exists in Invoice No : " + InvoiceNo + " Invoice Date : " + EITLERPGLOBAL.formatDate(InvoiceDate) + " Party Code : " + PartyCode + "\n Contact Account and Administrator.");
                            rsInvoice.next();
                            continue;
                        }
                        
                        InvoiceDebitNoteAmount = 0;
                        strSQL = "SELECT A.VOUCHER_NO, B.AMOUNT, B.VALUE_DATE FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B " +
                        "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND B.INVOICE_NO='"+InvoiceNo+"' AND B.INVOICE_DATE='"+InvoiceDate+"' " +
                        "AND B.EFFECT='C' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND B.MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND A.APPROVED=1 " +
                        "AND A.CANCELLED=0 ORDER BY A.VOUCHER_DATE ";
                        rsVoucher = data.getResult(strSQL,FinanceGlobal.FinURL);
                        rsVoucher.first();
                        // All Credit Record for Effect C & Same Party for same Invoice
                        
                        while(!rsVoucher.isAfterLast()) {
                            
                            String VoucherNo = UtilFunctions.getString(rsVoucher,"VOUCHER_NO", "");
                            double VoucherAmount = UtilFunctions.getDouble(rsVoucher,"AMOUNT", 0);
                            String ValueDate =  UtilFunctions.getString(rsVoucher,"VALUE_DATE","0000-00-00");
                            if(ValueDate.equals("") || ValueDate.equals("0000-00-00")) {
                                JOptionPane.showMessageDialog(null,"Value Date not exists in Voucher No : " + VoucherNo + " against Invoice No : " + InvoiceNo + " Invoice Date : " + EITLERPGLOBAL.formatDate(InvoiceDate) + " Party Code : " + PartyCode  + "\nDebit note can not post.\nContact Account and Administrator.");
                            }
                            if(java.sql.Date.valueOf(ValueDate).before(java.sql.Date.valueOf(InvoiceDueDate)) || java.sql.Date.valueOf(ValueDate).compareTo(java.sql.Date.valueOf(InvoiceDueDate))==0) {
                                rsVoucher.next();
                                continue;
                            }
                            VoucherDebitNoteAmount = 0;
                            // calculate debit note amount start
                            int InterestDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(InvoiceDueDate),java.sql.Date.valueOf(ValueDate));
                            InterestPercentage = clsSalesInvoice.getDebitNotePercentage(ValueDate,InvoiceType,ChargeCode,PartyCode);
                            if(InvoiceType==1 && java.sql.Date.valueOf(InvoiceDate).after(java.sql.Date.valueOf("2012-12-31")) && PartyCode.equals("300170")) {
                                InterestPercentage = 15;
                            }
                            VoucherDebitNoteAmount = EITLERPGLOBAL.round(((VoucherAmount * InterestPercentage * InterestDays)/(365* 100)),2);
                            // calculate debit note amount end
                            clsDebitNoteReceiptMapping ObjtempItem = new clsDebitNoteReceiptMapping();
                            ObjtempItem.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                            ObjtempItem.setAttribute("SR_NO",(ObjRefItem.colMappingDetail.size()+1));
                            ObjtempItem.setAttribute("DEBITNOTE_VOUCHER_NO","");
                            ObjtempItem.setAttribute("RECEIPT_VOUCHER_NO",VoucherNo);
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
                    
                    
                    if(EITLERPGLOBAL.round(PartyDebitNoteAmount, 0) > 5 ) {
                        
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
                        objVoucherItem.setAttribute("REF_VOUCHER_NO",currentVoucherNo);
                        objVoucherItem.setAttribute("REF_VOUCHER_TYPE",clsVoucher.getVoucherType(currentVoucherNo));
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
                        objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",PartyCode);
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
                        objVoucherItem.setAttribute("REF_VOUCHER_NO",currentVoucherNo);
                        objVoucherItem.setAttribute("REF_VOUCHER_TYPE",clsVoucher.getVoucherType(currentVoucherNo));
                        objVoucherItem.setAttribute("REF_VOUCHER_COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                        objVoucherItem.setAttribute("MATCHED",0);
                        objVoucherItem.setAttribute("MATCHED_DATE","0000-00-00");
                        objVoucherItem.setAttribute("REF_SR_NO",0);
                        ObjVoucher.colVoucherItems.put(Integer.toString(ObjVoucher.colVoucherItems.size()+1),objVoucherItem);
                        
                        if(ObjVoucher.Insert()) {
                            String theVoucherNo = ObjVoucher.getAttribute("VOUCHER_NO").getString();
                            String Msg = "Debit Note No : " + theVoucherNo + " posted for party code "+PartyCode+" of Rs."+PartyDebitNoteAmount;
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
                                //rsTmp1.updateString("RECEIPT_VOUCHER_NO",currentVoucherNo);
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
                    rsParty.next();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static int ValidateVoucherEntry(int EditMode,String VoucherDate) {
        
        if(EditMode == EITLERPGLOBAL.ADD) {
            if(Integer.parseInt(VoucherDate.substring(5,7)) == data.getIntValueFromDB("SELECT MONTH(CURDATE()) FROM DUAL") ) {
                //return 0;
            } else {
                if(EITLERPGLOBAL.getCurrentDay()<=7) {
                    // if(EITLERPGLOBAL.getCurrentDay()<=10) {     // Sales Journal Voucher for Export Entry
                    if(Integer.parseInt(VoucherDate.substring(5,7)) == data.getIntValueFromDB("SELECT MONTH(DATE_SUB(CURDATE(),INTERVAL 1 MONTH))  FROM DUAL")) {
                        //return 0;
                    } else {
                        return EITLERPGLOBAL.ADD;
                    }
                } else {
                    return EITLERPGLOBAL.ADD;
                }
            }
        }
        
        if(EditMode == EITLERPGLOBAL.EDIT) {
            if(Integer.parseInt(VoucherDate.substring(5,7)) == data.getIntValueFromDB("SELECT MONTH(CURDATE()) FROM DUAL") ) {
                //return 0;
            } else {
                if(EITLERPGLOBAL.getCurrentDay()<=12) {
                    if(Integer.parseInt(VoucherDate.substring(5,7)) == data.getIntValueFromDB("SELECT MONTH(DATE_SUB(CURDATE(),INTERVAL 1 MONTH))  FROM DUAL")){
                        //return 0;
                    } else {
                        return EITLERPGLOBAL.EDIT;
                    }
                } else {
                    return EITLERPGLOBAL.EDIT;
                }
            }
        }
        
        
        //        int vYear = Integer.parseInt(VoucherDate.substring(0,4));
        //        int vMonth = Integer.parseInt(VoucherDate.substring(5,7));
        //        int vDate = Integer.parseInt(VoucherDate.substring(8,10));
        //        String MonthStartDate = EITLERPGLOBAL.getCurrentYear() +"-"+ EITLERPGLOBAL.padLeftEx(Integer.toString(EITLERPGLOBAL.getCurrentMonth()),"0",2)+"-" + "01";
        //        String CurrentDate = EITLERPGLOBAL.getCurrentDateDB();
        //        int cDate = EITLERPGLOBAL.getCurrentDay();
        //        int cMonth = EITLERPGLOBAL.getCurrentMonth();
        //        if(EditMode==EITLERPGLOBAL.ADD) {
        //            MonthStartDate = EITLERPGLOBAL.addDaysToDate(MonthStartDate, 7, "yyyy-MM-dd");
        //            if(java.sql.Date.valueOf(VoucherDate).after(when
        //            if(cDate > 7 && vMonth<cMonth) {
        //                if(java.sql.Date.valueOf(VoucherDate).before(java.sql.Date.valueOf(MonthStartDate))) {
        //                    return EITLERPGLOBAL.ADD;
        //                }
        //            }
        //        } else {
        //            if(cDate > 12 && vMonth<cMonth) {
        //                if(java.sql.Date.valueOf(VoucherDate).before(java.sql.Date.valueOf(MonthStartDate))) {
        //                    return EITLERPGLOBAL.EDIT;
        //                }
        //            }
        //        }
        return 0;
    }
    
    public boolean PostFASCardWithGRN(String DocNo,int pModuleID) {
        try {
            
            String VoucherNo = DocNo;
            
            String SQL="SELECT DISTINCT GRN_NO FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO = '" + DocNo + "' "+
            "ORDER BY GRN_NO DESC";
            
            String GrnNo= data.getStringValueFromDB(SQL,FinanceGlobal.FinURL);
            
            if(pModuleID == clsGRNGen.ModuleID) {
                clsGRNGen objGRN= new clsGRNGen();
                objGRN = (clsGRNGen)objGRN.getObject(EITLERPGLOBAL.gCompanyID,GrnNo);
                
                
                String GrnDate = objGRN.getAttribute("GRN_DATE").getString();
                String InvoiceNo = objGRN.getAttribute("INVOICE_NO").getString();
                String InvoiceDate = objGRN.getAttribute("INVOICE_DATE").getString();
                String SupplierId = objGRN.getAttribute("SUPP_ID").getString();
                
                
                
                SQL = "SELECT B.* "+
                "FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B "+
                "WHERE A.COMPANY_ID = '" + EITLERPGLOBAL.gCompanyID + "' AND A.VOUCHER_NO = B.VOUCHER_NO "+
                "AND A.VOUCHER_NO = '" + DocNo + "'  AND B.MODULE_ID = '" + clsGRNGen.ModuleID + "' "+
                "AND B.SUB_ACCOUNT_CODE = '" + SupplierId + "' "+
                "AND B.EFFECT = 'C' ";
                
                
                if(!data.IsRecordExist(SQL,FinanceGlobal.FinURL)) {
                    return false;
                }
                
                
                
                SQL ="SELECT * "+
                "FROM D_FAS_MASTER_HEADER "+
                "WHERE PJ_VOUCHER_NO = '" + DocNo + "' AND CANCELLED =0 ";
                
                if(data.IsRecordExist(SQL,FinanceGlobal.FinURL)) {
                    return false;
                }
                
                //String SQL = "";
                String ItemId="",PONo="",PODate ="";
                double Qty=0,TotalCenvatAmount=0,ItemAmount=0;
                int DeptID = 0;
                Statement stHHeader,stHDetail,stHDetailEx;
                Statement stHeader,stDetail,stDetailEx;
                ResultSet rsHeader,rsDetail,rsDetailEx;
                ResultSet rsHHeader,rsHDetail,rsHDetailEx;
                ResultSet rsTmp;
                
                ResultSet rsVoucher=data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID="+clsFASCardwithGRN.ModuleID);
                rsVoucher.first();
                String SelPrefix="",SelSuffix="";
                int FFNo=0;
                if(rsVoucher.getRow()>0) {
                    SelPrefix=rsVoucher.getString("PREFIX_CHARS");
                    SelSuffix=rsVoucher.getString("SUFFIX_CHARS");
                    FFNo=rsVoucher.getInt("FIRSTFREE_NO");
                }
                
                /*========== Select the Hierarchy ======== */
                int HierarchyID = 0;
                HashMap List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsFASCardwithGRN.ModuleID , "CHOOSE_HIERARCHY", "DEFAULT", "");
                
                if(List.size()>0) {
                    //Get the Result of the Rule which would be the hierarchy no.
                    clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                    HierarchyID=UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                }
                /*========== End of Hierarchy Selection ======== */
                
                for(int i=1;i<=objGRN.colGRNItems.size();i++) {
                    clsGRNGenItem objItem=(clsGRNGenItem)objGRN.colGRNItems.get(Integer.toString(i));
                    //ObjItem.getAttribute("");
                    ItemId = objItem.getAttribute("ITEM_ID").getString();
                    DeptID = objItem.getAttribute("DEPT_ID").getInt();
                    
                    SQL = "SELECT A.ITEM_ID,A.ITEM_DESCRIPTION "+
                    "FROM "+ EITLERPGLOBAL.DBName +".D_INV_ITEM_MASTER A,FINANCE.D_FAS_ITEM_MASTER_HEADER B "+
                    "WHERE B.COMPANY_ID = 2 AND A.ITEM_ID = B.ITEM_ID "+
                    "AND A.APPROVED=1 AND A.CANCELLED = 0 "+
                    "AND B.APPROVED=1 AND B.CANCELLED = 0 "+
                    "AND A.CATEGORY_ID=1 AND A.ITEM_ID = '" + ItemId + "' "+
                    "ORDER BY A.ITEM_ID ";
                    
                    if(!data.IsRecordExist(SQL)) {
                        continue;
                    }
                    
                    Qty = objItem.getAttribute("QTY").getVal();
                    //LandedRate = objItem.getAttribute("LANDED_RATE").getVal();
                    PONo = objItem.getAttribute("PO_NO").getString();
                    TotalCenvatAmount = objItem.getAttribute("COLUMN_8_AMT").getVal();
                    //CenvatAmount = EITLERPGLOBAL.round(objItem.getAttribute("COLUMN_8_AMT").getVal() / Qty,3);
                    
                    clsFASCardwithGRN ObjFASCard = new clsFASCardwithGRN();
                    ObjFASCard.LoadData(EITLERPGLOBAL.gCompanyID);
                    
                    ObjFASCard.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    ObjFASCard.setAttribute("PREFIX",SelPrefix);
                    ObjFASCard.setAttribute("SUFFIX",SelSuffix);
                    ObjFASCard.setAttribute("FFNO",FFNo);
                    //ObjFASCard.setAttribute("ASSET_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,clsFASCardwithGRN.ModuleID, (int)getAttribute("FFNO").getVal(),true));
                    ObjFASCard.setAttribute("ASSET_DATE",GrnDate);
                    ObjFASCard.setAttribute("ITEM_ID",ItemId);
                    ObjFASCard.setAttribute("SUPPLIER_CODE",SupplierId);
                    ObjFASCard.setAttribute("DEPT_ID",Integer.toString(DeptID));
                    ObjFASCard.setAttribute("PO_NO",PONo);
                    PODate = data.getStringValueFromDB("SELECT PO_DATE FROM D_PUR_PO_HEADER WHERE PO_NO = '" + PONo + "' ");
                    ObjFASCard.setAttribute("PO_DATE",PODate);
                    ObjFASCard.setAttribute("GURANTEE","");
                    ObjFASCard.setAttribute("GRN_NO",GrnNo);
                    
                    ObjFASCard.setAttribute("EXPECTED_LIFE","");
                    ObjFASCard.setAttribute("WARRANTY","");
                    ObjFASCard.setAttribute("YEAR_OF_PURCHASE",GrnDate.substring(0,4));
                    ObjFASCard.setAttribute("DISPOSED_DATE","0000-00-00");
                    ObjFASCard.setAttribute("SIZE_CAPACITY","");
                    ObjFASCard.setAttribute("INSTALLATION_DATE",GrnDate);
                    ObjFASCard.setAttribute("TOTAL_QTY",String.valueOf(Qty));
                    ObjFASCard.setAttribute("MODEL_NO","");
                    ObjFASCard.setAttribute("MACHINE_NO","");
                    ObjFASCard.setAttribute("INVOICE_NO",InvoiceNo);
                    ObjFASCard.setAttribute("INVOICE_DATE",InvoiceDate);
                    ObjFASCard.setAttribute("HEADER_REMARKS","");
                    ObjFASCard.setAttribute("ASSET_DESC","");
                    
                    
                    SQL = "SELECT A.VOUCHER_NO,A.VOUCHER_DATE,SUM(B.AMOUNT) AS AMOUNT,B.BLOCK_NO "+
                    "FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B "+
                    "WHERE A.COMPANY_ID = '" + EITLERPGLOBAL.gCompanyID + "' AND A.VOUCHER_NO = B.VOUCHER_NO AND A.VOUCHER_NO = '" + VoucherNo + "'  AND B.MODULE_ID = '" + clsGRNGen.ModuleID + "' "+
                    "AND B.EFFECT = 'C' " +
                    "AND B.SUB_ACCOUNT_CODE = '" + SupplierId + "' GROUP BY A.VOUCHER_NO,A.VOUCHER_DATE";
                    rsTmp = data.getResult(SQL,FinanceGlobal.FinURL);
                    rsTmp.first();
                    
                    ObjFASCard.setAttribute("PJ_VOUCHER_NO",rsTmp.getString("VOUCHER_NO"));
                    ObjFASCard.setAttribute("PJ_VOUCHER_DATE",rsTmp.getString("VOUCHER_DATE"));
                    
                    
                    //ObjFASCard.setAttribute("PJ_VOUCHER_AMOUNT",String.valueOf(EITLERPGLOBAL.round((Qty * LandedRate) - TotalCenvatAmount , 3)));
                    
                    ObjFASCard.setAttribute("PJ_VOUCHER_AMOUNT",String.valueOf(rsTmp.getDouble("AMOUNT") - TotalCenvatAmount));
                    
                    //LandedRate = EITLERPGLOBAL.round(( rsTmp.getDouble("AMOUNT") / Qty ) ,3);
                    //LandedRate = EITLERPGLOBAL.round(LandedRate,3);
                    ItemAmount = EITLERPGLOBAL.round((rsTmp.getDouble("AMOUNT") - TotalCenvatAmount ) /Qty,0);
                    
                    
                    
                    SQL = "SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL_EX " +
                    "WHERE VOUCHER_NO = '" + rsTmp.getString("VOUCHER_NO") + "'  AND COMPANY_ID = '" + EITLERPGLOBAL.gCompanyID + "' " +
                    "AND BLOCK_NO = '" + rsTmp.getInt("BLOCK_NO") + "' AND EFFECT <> 'C' ";
                    
                    String NominalCode= data.getStringValueFromDB(SQL,FinanceGlobal.FinURL);
                    
                    ObjFASCard.setAttribute("MAIN_ACCOUNT_CODE",NominalCode);
                    rsTmp.close();
                    
                    
                    ObjFASCard.setAttribute("CUSTOM_DUTY_VOUCHER_NO","");
                    ObjFASCard.setAttribute("CUSTOM_DUTY_VOUCHER_DATE","0000-00-00");
                    ObjFASCard.setAttribute("CUSTOM_DUTY_VOUCHER_AMOUNT","0");
                    
                    ObjFASCard.setAttribute("LANDING_VOUCHER_NO","");
                    ObjFASCard.setAttribute("LANDING_VOUCHER_DATE","0000-00-00");
                    ObjFASCard.setAttribute("LANDING_VOUCHER_AMOUNT","0");
                    ObjFASCard.setAttribute("FREIGHT_VOUCHER_NO","");
                    ObjFASCard.setAttribute("FREIGHT_VOUCHER_DATE","0000-00-00");
                    ObjFASCard.setAttribute("FREIGHT_VOUCHER_AMOUNT","0");
                    ObjFASCard.setAttribute("INSTALLATION_VOUCHER_NO","");
                    ObjFASCard.setAttribute("INSTALLATION_VOUCHER_DATE","0000-00-00");
                    ObjFASCard.setAttribute("INSTALLATION_VOUCHER_AMOUNT","0");
                    ObjFASCard.setAttribute("OTHERS_VOUCHER_NO","");
                    ObjFASCard.setAttribute("OTHERS_VOUCHER_DATE","0000-00-00");
                    ObjFASCard.setAttribute("OTHERS_VOUCHER_AMOUNT","0");
                    ObjFASCard.setAttribute("SERVICE_TAX_AMOUNT","0");
                    
                    
                    
                    ObjFASCard.setAttribute("REMARKS","");
                    
                    // System.out.println("User " + EITLERPGLOBAL.gNewUserID + " User " + EITLERPGLOBAL.gUserID);
                    ObjFASCard.setAttribute("HIERARCHY_ID",HierarchyID);
                    int FirstUserID=data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID="+HierarchyID+" AND SR_NO=1");
                    ObjFASCard.setAttribute("FROM",FirstUserID);
                    ObjFASCard.setAttribute("TO",FirstUserID);
                    ObjFASCard.setAttribute("FROM_REMARKS","");
                    // int FirstUserID=data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID='"+1320+"' AND SR_NO=1");
                    ObjFASCard.setAttribute("APPROVAL_STATUS","H");
                    ObjFASCard.setAttribute("SEND_DOC_TO",FirstUserID);
                    
                    
                    
                    ObjFASCard.colFASItemsDetail.clear();
                    
                    for(int j=1 ;j<=Qty;j++) {
                        clsFASCardwithGRNDetail ObjCardDetail = new clsFASCardwithGRNDetail();
                        
                        ObjCardDetail.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                        //ObjCardDetail.setAttribute("ASSET_NO",txtAssetNo.getText().trim());
                        ObjCardDetail.setAttribute("ITEM_ID",ItemId);
                        ObjCardDetail.setAttribute("SR_NO",String.valueOf(j));
                        ObjCardDetail.setAttribute("YEAR",String.valueOf(EITLERPGLOBAL.getYear(GrnDate)));
                        ObjCardDetail.setAttribute("DEPT_ID",Integer.toString(DeptID));
                        //ObjCardDetail.setAttribute("DEPT_ID",String.valueOf(2));
                        ObjCardDetail.setAttribute("AMOUNT",String.valueOf(ItemAmount));
                        ObjCardDetail.setAttribute("SAL_DOC_NO","");
                        ObjCardDetail.setAttribute("SALE_DATE","0000-00-00");
                        ObjCardDetail.setAttribute("SALE_VALUE","");
                        ObjCardDetail.setAttribute("SJ_NUMBER","");
                        ObjCardDetail.setAttribute("REMARKS","");
                        ObjCardDetail.setAttribute("ASSET_STATUS","");
                        ObjFASCard.colFASItemsDetail.put(Integer.toString(ObjFASCard.colFASItemsDetail.size()+1),ObjCardDetail);
                    }
                    ObjFASCard.colFASItemsDetailExBook.clear();
                    ObjFASCard.colFASItemsDetailExIT.clear();
                    
                    if(!ObjFASCard.Insert()) {
                        System.out.println(ObjFASCard.LastError);
                        return false;
                    }else {
                        JOptionPane.showMessageDialog(null,ObjFASCard.getAttribute("ASSET_NO") +" posted.","Asset Posting",JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
            else {
                if (clsJobwork.ModuleID == 48) {
                    clsJobwork objJob= new clsJobwork();
                    objJob = (clsJobwork)objJob.getObject(EITLERPGLOBAL.gCompanyID,GrnNo);
                    
                    
                    String JobDate = objJob.getAttribute("JOB_DATE").getString();
                    String JobInvoiceNo = objJob.getAttribute("INVOICE_NO").getString();
                    String JobInvoiceDate = objJob.getAttribute("INVOICE_DATE").getString();
                    String JobSupplierId = objJob.getAttribute("SUPP_ID").getString();
                    
                    
                    
                    SQL = "SELECT B.* "+
                    "FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B "+
                    "WHERE A.COMPANY_ID = '" + EITLERPGLOBAL.gCompanyID + "' AND A.VOUCHER_NO = B.VOUCHER_NO "+
                    "AND A.VOUCHER_NO = '" + DocNo + "'  AND B.MODULE_ID = '" + clsJobwork.ModuleID + "' "+
                    "AND B.SUB_ACCOUNT_CODE = '" + JobSupplierId + "' "+
                    "AND B.EFFECT = 'C' ";
                    
                    
                    if(!data.IsRecordExist(SQL,FinanceGlobal.FinURL)) {
                        return false;
                    }
                    
                    SQL ="SELECT * "+
                    "FROM D_FAS_MASTER_HEADER "+
                    "WHERE PJ_VOUCHER_NO = '" + DocNo + "' AND CANCELLED =0 ";
                    
                    if(data.IsRecordExist(SQL,FinanceGlobal.FinURL)) {
                        return false;
                    }
                    
                    //String SQL = "";
                    String ItemId="",PONo="",PODate ="";
                    double Qty=0,TotalCenvatAmount=0,ItemAmount=0;
                    int DeptID = 0;
                    Statement stHHeader,stHDetail,stHDetailEx;
                    Statement stHeader,stDetail,stDetailEx;
                    ResultSet rsHeader,rsDetail,rsDetailEx;
                    ResultSet rsHHeader,rsHDetail,rsHDetailEx;
                    ResultSet rsTmp;
                    
                    ResultSet rsVoucher=data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID="+clsFASCardwithGRN.ModuleID);
                    rsVoucher.first();
                    String SelPrefix="",SelSuffix="";
                    int FFNo=0;
                    if(rsVoucher.getRow()>0) {
                        SelPrefix=rsVoucher.getString("PREFIX_CHARS");
                        SelSuffix=rsVoucher.getString("SUFFIX_CHARS");
                        FFNo=rsVoucher.getInt("FIRSTFREE_NO");
                    }
                    
                    /*========== Select the Hierarchy ======== */
                    int HierarchyID = 0;
                    HashMap List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsFASCardwithGRN.ModuleID , "CHOOSE_HIERARCHY", "DEFAULT", "");
                    
                    if(List.size()>0) {
                        //Get the Result of the Rule which would be the hierarchy no.
                        clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                        HierarchyID=UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                    }
                    /*========== End of Hierarchy Selection ======== */
                    
                    for(int i=1;i<=objJob.colMIRItems.size();i++) {
                        clsJobworkItem objItem=(clsJobworkItem)objJob.colMIRItems.get(Integer.toString(i));
                        //ObjItem.getAttribute("");
                        ItemId = objItem.getAttribute("ITEM_ID").getString();
                        DeptID = objItem.getAttribute("DEPT_ID").getInt();
                        
                        SQL = "SELECT A.ITEM_ID,A.ITEM_DESCRIPTION "+
                        "FROM "+ EITLERPGLOBAL.DBName +".D_INV_ITEM_MASTER A,FINANCE.D_FAS_ITEM_MASTER_HEADER B "+
                        "WHERE B.COMPANY_ID = 2 AND A.ITEM_ID = B.ITEM_ID "+
                        "AND A.APPROVED=1 AND A.CANCELLED = 0 "+
                        "AND B.APPROVED=1 AND B.CANCELLED = 0 "+
                        "AND A.CATEGORY_ID=1 AND A.ITEM_ID = '" + ItemId + "' "+
                        "ORDER BY A.ITEM_ID ";
                        
                        if(!data.IsRecordExist(SQL)) {
                            continue;
                        }
                        
                        Qty = objItem.getAttribute("QTY").getVal();
                        //LandedRate = objItem.getAttribute("LANDED_RATE").getVal();
                        PONo = objItem.getAttribute("PO_NO").getString();
                        TotalCenvatAmount = objItem.getAttribute("COLUMN_8_AMT").getVal();
                        //CenvatAmount = EITLERPGLOBAL.round(objItem.getAttribute("COLUMN_8_AMT").getVal() / Qty,3);
                        
                        clsFASCardwithGRN ObjFASCard = new clsFASCardwithGRN();
                        ObjFASCard.LoadData(EITLERPGLOBAL.gCompanyID);
                        
                        ObjFASCard.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                        ObjFASCard.setAttribute("PREFIX",SelPrefix);
                        ObjFASCard.setAttribute("SUFFIX",SelSuffix);
                        ObjFASCard.setAttribute("FFNO",FFNo);
                        //ObjFASCard.setAttribute("ASSET_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,clsFASCardwithGRN.ModuleID, (int)getAttribute("FFNO").getVal(),true));
                        ObjFASCard.setAttribute("ASSET_DATE",JobDate);
                        ObjFASCard.setAttribute("ITEM_ID",ItemId);
                        ObjFASCard.setAttribute("SUPPLIER_CODE",JobSupplierId);
                        ObjFASCard.setAttribute("DEPT_ID",Integer.toString(DeptID));
                        ObjFASCard.setAttribute("PO_NO",PONo);
                        PODate = data.getStringValueFromDB("SELECT PO_DATE FROM D_PUR_PO_HEADER WHERE PO_NO = '" + PONo + "' ");
                        ObjFASCard.setAttribute("PO_DATE",PODate);
                        ObjFASCard.setAttribute("GURANTEE","");
                        ObjFASCard.setAttribute("GRN_NO",GrnNo);
                        
                        ObjFASCard.setAttribute("EXPECTED_LIFE","");
                        ObjFASCard.setAttribute("WARRANTY","");
                        ObjFASCard.setAttribute("YEAR_OF_PURCHASE",JobDate.substring(0,4));
                        ObjFASCard.setAttribute("DISPOSED_DATE","0000-00-00");
                        ObjFASCard.setAttribute("SIZE_CAPACITY","");
                        ObjFASCard.setAttribute("INSTALLATION_DATE",JobDate);
                        ObjFASCard.setAttribute("TOTAL_QTY",String.valueOf(Qty));
                        ObjFASCard.setAttribute("MODEL_NO","");
                        ObjFASCard.setAttribute("MACHINE_NO","");
                        ObjFASCard.setAttribute("INVOICE_NO",JobInvoiceNo);
                        ObjFASCard.setAttribute("INVOICE_DATE",JobInvoiceDate);
                        ObjFASCard.setAttribute("HEADER_REMARKS","");
                        
                        
                        SQL = "SELECT A.VOUCHER_NO,A.VOUCHER_DATE,SUM(B.AMOUNT) AS AMOUNT,B.BLOCK_NO "+
                        "FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B "+
                        "WHERE A.COMPANY_ID = '" + EITLERPGLOBAL.gCompanyID + "' AND A.VOUCHER_NO = B.VOUCHER_NO AND A.VOUCHER_NO = '" + VoucherNo + "'  AND B.MODULE_ID = '" + clsJobwork.ModuleID + "' "+
                        "AND B.EFFECT = 'C' " +
                        "AND B.SUB_ACCOUNT_CODE = '" + JobSupplierId + "' GROUP BY A.VOUCHER_NO,A.VOUCHER_DATE";
                        rsTmp = data.getResult(SQL,FinanceGlobal.FinURL);
                        rsTmp.first();
                        
                        ObjFASCard.setAttribute("PJ_VOUCHER_NO",rsTmp.getString("VOUCHER_NO"));
                        ObjFASCard.setAttribute("PJ_VOUCHER_DATE",rsTmp.getString("VOUCHER_DATE"));
                        
                        
                        //ObjFASCard.setAttribute("PJ_VOUCHER_AMOUNT",String.valueOf(EITLERPGLOBAL.round((Qty * LandedRate) - TotalCenvatAmount , 3)));
                        
                        ObjFASCard.setAttribute("PJ_VOUCHER_AMOUNT",String.valueOf(rsTmp.getDouble("AMOUNT") - TotalCenvatAmount));
                        
                        //LandedRate = EITLERPGLOBAL.round(( rsTmp.getDouble("AMOUNT") / Qty ) ,3);
                        //LandedRate = EITLERPGLOBAL.round(LandedRate,3);
                        ItemAmount = EITLERPGLOBAL.round((rsTmp.getDouble("AMOUNT") - TotalCenvatAmount ) /Qty,0);
                        
                        
                        
                        SQL = "SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL_EX " +
                        "WHERE VOUCHER_NO = '" + rsTmp.getString("VOUCHER_NO") + "'  AND COMPANY_ID = '" + EITLERPGLOBAL.gCompanyID + "' " +
                        "AND BLOCK_NO = '" + rsTmp.getInt("BLOCK_NO") + "' AND EFFECT <> 'C' ";
                        
                        String NominalCode= data.getStringValueFromDB(SQL,FinanceGlobal.FinURL);
                        
                        ObjFASCard.setAttribute("MAIN_ACCOUNT_CODE",NominalCode);
                        rsTmp.close();
                        
                        
                        ObjFASCard.setAttribute("CUSTOM_DUTY_VOUCHER_NO","");
                        ObjFASCard.setAttribute("CUSTOM_DUTY_VOUCHER_DATE","0000-00-00");
                        ObjFASCard.setAttribute("CUSTOM_DUTY_VOUCHER_AMOUNT","0");
                        
                        ObjFASCard.setAttribute("LANDING_VOUCHER_NO","");
                        ObjFASCard.setAttribute("LANDING_VOUCHER_DATE","0000-00-00");
                        ObjFASCard.setAttribute("LANDING_VOUCHER_AMOUNT","0");
                        ObjFASCard.setAttribute("FREIGHT_VOUCHER_NO","");
                        ObjFASCard.setAttribute("FREIGHT_VOUCHER_DATE","0000-00-00");
                        ObjFASCard.setAttribute("FREIGHT_VOUCHER_AMOUNT","0");
                        ObjFASCard.setAttribute("INSTALLATION_VOUCHER_NO","");
                        ObjFASCard.setAttribute("INSTALLATION_VOUCHER_DATE","0000-00-00");
                        ObjFASCard.setAttribute("INSTALLATION_VOUCHER_AMOUNT","0");
                        ObjFASCard.setAttribute("OTHERS_VOUCHER_NO","");
                        ObjFASCard.setAttribute("OTHERS_VOUCHER_DATE","0000-00-00");
                        ObjFASCard.setAttribute("OTHERS_VOUCHER_AMOUNT","0");
                        ObjFASCard.setAttribute("SERVICE_TAX_AMOUNT","0");
                        
                        
                        
                        ObjFASCard.setAttribute("REMARKS","");
                        
                        // System.out.println("User " + EITLERPGLOBAL.gNewUserID + " User " + EITLERPGLOBAL.gUserID);
                        ObjFASCard.setAttribute("HIERARCHY_ID",HierarchyID);
                        int FirstUserID=data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID="+HierarchyID+" AND SR_NO=1");
                        ObjFASCard.setAttribute("FROM",FirstUserID);
                        ObjFASCard.setAttribute("TO",FirstUserID);
                        ObjFASCard.setAttribute("FROM_REMARKS","");
                        // int FirstUserID=data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID='"+1320+"' AND SR_NO=1");
                        ObjFASCard.setAttribute("APPROVAL_STATUS","H");
                        ObjFASCard.setAttribute("SEND_DOC_TO",FirstUserID);
                        
                        
                        
                        ObjFASCard.colFASItemsDetail.clear();
                        
                        for(int j=1 ;j<=Qty;j++) {
                            clsFASCardwithGRNDetail ObjCardDetail = new clsFASCardwithGRNDetail();
                            
                            ObjCardDetail.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                            //ObjCardDetail.setAttribute("ASSET_NO",txtAssetNo.getText().trim());
                            ObjCardDetail.setAttribute("ITEM_ID",ItemId);
                            ObjCardDetail.setAttribute("SR_NO",String.valueOf(j));
                            ObjCardDetail.setAttribute("YEAR",String.valueOf(EITLERPGLOBAL.getYear(JobDate)));
                            ObjCardDetail.setAttribute("DEPT_ID",Integer.toString(DeptID));
                            ObjCardDetail.setAttribute("AMOUNT",String.valueOf(ItemAmount));
                            ObjCardDetail.setAttribute("SAL_DOC_NO","");
                            ObjCardDetail.setAttribute("SALE_DATE","0000-00-00");
                            ObjCardDetail.setAttribute("SALE_VALUE","");
                            ObjCardDetail.setAttribute("SJ_NUMBER","");
                            ObjCardDetail.setAttribute("REMARKS","");
                            ObjCardDetail.setAttribute("ASSET_STATUS","");
                            ObjFASCard.colFASItemsDetail.put(Integer.toString(ObjFASCard.colFASItemsDetail.size()+1),ObjCardDetail);
                        }
                        ObjFASCard.colFASItemsDetailExBook.clear();
                        ObjFASCard.colFASItemsDetailExIT.clear();
                        
                        if(!ObjFASCard.Insert()) {
                            System.out.println(ObjFASCard.LastError);
                            return false;
                        }else {
                            JOptionPane.showMessageDialog(null,ObjFASCard.getAttribute("ASSET_NO") +" posted.","Asset Posting",JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            }
            //ObjFASCard.setAttribute("CREATED_BY",EITLERPGLOBAL.gNewUserID);
            //ObjFASCard.setAttribute("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            //}
            //else {
            //    ObjFASCard.setAttribute("MODIFIED_BY",EITLERPGLOBAL.gNewUserID);
            //   ObjFASCard.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            //}
            // ---- History Related Changes ------ //
            //                stHHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //                stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //                stHDetailEx=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //
            //                stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //                stDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //                stDetailEx=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //
            //                rsHHeader=stHHeader.executeQuery("SELECT * FROM D_FAS_MASTER_HEADER_H WHERE ASSET_NO='1'"); // '1' for restricting all data retrieval
            //                rsHHeader.first();
            //                rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FAS_MASTER_DETAIL_H WHERE ASSET_NO='1'");
            //                rsHDetail.first();
            //                rsHDetailEx=stHDetailEx.executeQuery("SELECT * FROM D_FAS_MASTER_DETAIL_EX_H WHERE ASSET_NO='1'");
            //                rsHDetailEx.first();
            //
            //                rsHeader=stHeader.executeQuery("SELECT * FROM D_FAS_MASTER_HEADER WHERE ASSET_NO='1'"); // '1' for restricting all data retrieval
            //                rsHeader.first();
            //                rsDetail=stDetail.executeQuery("SELECT * FROM D_FAS_MASTER_DETAIL WHERE ASSET_NO='1'");
            //                rsDetail.first();
            //                rsDetailEx=stDetailEx.executeQuery("SELECT * FROM D_FAS_MASTER_DETAIL_EX WHERE ASSET_NO='1'");
            //                rsDetailEx.first();
            //------------------------------------//
            
            
            //--------------INSERT INTO HEADER---------------//
            //                rsResultSet.first();
            //                rsResultSet.moveToInsertRow();
            //                rsResultSet.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getLong());
            //                rsResultSet.updateString("ASSET_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,clsFASCardwithGRN.ModuleID, (int)getAttribute("FFNO").getVal(),true));
            //
            //                rsResultSet.updateString("ASSET_DATE", GrnDate);
            //                rsResultSet.updateString("ASSET_TYPE","1"); //1 with GRN ,2 without GRN
            //                rsResultSet.updateString("ITEM_ID",ItemId);
            //                rsResultSet.updateString("SUPPLIER_CODE",SupplierId);
            //                rsResultSet.updateInt("DEPT_ID",2);
            //
            //                rsResultSet.updateString("PO_NO",PONo);
            //                rsResultSet.updateString("PO_DATE","0000-00-00");
            //                rsResultSet.updateString("GUARANTEE","");
            //                rsResultSet.updateString("MAIN_ACCOUNT_CODE","");
            //                rsResultSet.updateString("EXPECTED_LIFE","");
            //                rsResultSet.updateString("WARRANTY","");
            //                rsResultSet.updateString("YEAR_OF_PURCHASE","");
            //                rsResultSet.updateString("DISPOSED_DATE","0000-00-00");
            //                rsResultSet.updateString("SIZE_CAPACITY","");
            //                rsResultSet.updateString("INSTALLATION_DATE","0000-00-00");
            //
            //                rsResultSet.updateDouble("QTY",Qty);
            //                rsResultSet.updateString("MODEL_NO","");
            //                rsResultSet.updateString("MACHINE_NO","");
            //                rsResultSet.updateString("INVOICE_NO",InvoiceNo);
            //                rsResultSet.updateString("INVOICE_DATE",InvoiceDate);
            //                rsResultSet.updateString("HEADER_REMARKS","");
            //
            //                SQL = "SELECT A.VOUCHER_NO "+
            //                "FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B "+
            //                "WHERE A.VOUCHER_NO = B.VOUCHER_NO AND GRN_NO = '" + GrnNo + "'  AND B.MODULE_ID = 7 "+
            //                "AND B.SUB_ACCOUNT_CODE = '" + SupplierId + "' ";
            //
            //                String VoucherNo = data.getStringValueFromDB(SQL,FinanceGlobal.FinURL);
            //                rsTmp = data.getResult("SELECT * FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO = '" + VoucherNo + "' ",FinanceGlobal.FinURL);
            //
            //                rsResultSet.updateString("PJ_VOUCHER_NO",VoucherNo);
            //                rsResultSet.updateString("PJ_VOUCHER_DATE",rsTmp.getString("VOUCHER_DATE"));
            //                rsResultSet.updateDouble("PJ_VOUCHER_AMOUNT",Qty * LandedRate);
            //
            //                rsResultSet.updateString("CUSTOM_DUTY_VOUCHER_NO","");
            //                rsResultSet.updateString("CUSTOM_DUTY_VOUCHER_DATE","0000-00-00");
            //                rsResultSet.updateDouble("CUSTOM_DUTY_VOUCHER_AMOUNT",0);
            //
            //                rsResultSet.updateString("LANDING_VOUCHER_NO","");
            //                rsResultSet.updateString("LANDING_VOUCHER_DATE","0000-00-00");
            //                rsResultSet.updateDouble("LANDING_VOUCHER_AMOUNT",0);
            //
            //                rsResultSet.updateString("FREIGHT_OCTROI_VOUCHER_NO","");
            //                rsResultSet.updateString("FREIGHT_OCTROI_VOUCHER_DATE","0000-00-00");
            //                rsResultSet.updateDouble("FREIGHT_OCTROI_VOUCHER_AMOUNT",0);
            //
            //                rsResultSet.updateString("INSTALLATION_VOUCHER_NO","");
            //                rsResultSet.updateString("INSTALLATION_VOUCHER_DATE","0000-00-00");
            //                rsResultSet.updateDouble("INSTALLATION_VOUCHER_AMOUNT",0);
            //
            //                rsResultSet.updateString("OTHERS_VOUCHER_NO","");
            //                rsResultSet.updateString("OTHERS_VOUCHER_DATE","0000-00-00");
            //                rsResultSet.updateDouble("OTHERS_VOUCHER_AMOUNT",0);
            //
            //                rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            
            //                rsResultSet.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            //                rsResultSet.updateString("CREATED_BY",String.valueOf(getAttribute("CREATED_BY").getInt()));
            //                rsResultSet.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            //                rsResultSet.updateString("MODIFIED_BY",String.valueOf(getAttribute("MODIFIED_BY").getInt()));
            //                rsResultSet.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            //                rsResultSet.updateBoolean("CHANGED",true);
            //                rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            //                rsResultSet.updateBoolean("APPROVED",false);
            //                rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            //                rsResultSet.updateBoolean("REJECTED",false);
            //                rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            //                rsResultSet.updateBoolean("CANCELLED",false);
            //                rsResultSet.insertRow();
            //--------------END INSERT HEADER----------------//
            
            
            
            
            
            
            
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    
}