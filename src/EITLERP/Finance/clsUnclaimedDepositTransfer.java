package EITLERP.Finance;

import java.util.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 *
 * @author  nrpithva
 * @version
 */

public class clsUnclaimedDepositTransfer {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
    private boolean HistoryView=false;
    
    public static int ModuleID=146;
    public static boolean UnclaimedEntry = false;
    
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
    public clsUnclaimedDepositTransfer() {
        LastError = "";
        props=new HashMap();
        
        props.put("FFNO",new Variant(0));
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("RECEIPT_NO",new Variant(""));
        props.put("RECEIPT_DATE",new Variant(""));
        props.put("APPLICANT_NAME",new Variant(""));
        props.put("EFFECTIVE_DATE",new Variant(""));
        props.put("MATURITY_DATE",new Variant(""));
        props.put("MAIN_ACCOUNT_CODE",new Variant(""));
        props.put("SUB_ACCOUNT_CODE",new Variant(""));
        props.put("AMOUNT",new Variant(0.0));
        props.put("TRANSFER_CODE",new Variant(""));
        props.put("TRANSFER_DATE",new Variant(""));
        
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE", new Variant(""));
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
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        
        // -- For Update of Deposit Master --
    }
    
    /**Load Data. This method loads data from database to Business Object**/
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FD_DEPOSIT_UNCLAIMED ORDER BY DOC_NO");
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
        
        Statement stHistory;
        ResultSet rsHistory;
        String theDocNo = "";
        try {
            
            //===== History Related Changes =====//
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FD_DEPOSIT_UNCLAIMED_H WHERE DOC_NO='1'");
            rsHistory.first();
            //----------------------------------//
            
            //** Validations **//
            if(!Validate()) {
                return false;
            }
            
            
            if(data.IsRecordExist("SELECT DOC_NO FROM D_FD_DEPOSIT_UNCLAIMED WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"'",FinanceGlobal.FinURL)) {
                LastError="Document with this Doc No. already exist.";
                return false;
            }
            //*****************//
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //========= Inserting Into Deposit Refund =================//
            String DocNo = clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,ModuleID,getAttribute("FFNO").getInt(),true);
            setAttribute("DOC_NO",DocNo);
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsResultSet.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            theDocNo = getAttribute("DOC_NO").getString();
            rsResultSet.updateString("DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsResultSet.updateString("RECEIPT_NO",getAttribute("RECEIPT_NO").getString());
            rsResultSet.updateString("RECEIPT_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("RECEIPT_DATE").getString()));
            rsResultSet.updateString("APPLICANT_NAME",getAttribute("APPLICANT_NAME").getString());
            rsResultSet.updateString("EFFECTIVE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_DATE").getString()));
            rsResultSet.updateString("MATURITY_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MATURITY_DATE").getString()));
            rsResultSet.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsResultSet.updateString("SUB_ACCOUNT_CODE",getAttribute("SUB_ACCOUNT_CODE").getString());
            rsResultSet.updateDouble("AMOUNT",getAttribute("AMOUNT").getDouble());
            rsResultSet.updateString("TRANSFER_CODE",getAttribute("TRANSFER_CODE").getString());
            rsResultSet.updateString("TRANSFER_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("TRANSFER_DATE").getString()));
            
            rsResultSet.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
            rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateString("REJECTED_REMARKS","");
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.insertRow();
            
            //--------------------------------------------------------//
            
            //========= Inserting Into History =================//
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1);
            rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            rsHistory.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsHistory.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsHistory.updateString("DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsHistory.updateString("RECEIPT_NO",getAttribute("RECEIPT_NO").getString());
            rsHistory.updateString("RECEIPT_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("RECEIPT_DATE").getString()));
            rsHistory.updateString("APPLICANT_NAME",getAttribute("APPLICANT_NAME").getString());
            rsHistory.updateString("EFFECTIVE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_DATE").getString()));
            rsHistory.updateString("MATURITY_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MATURITY_DATE").getString()));
            rsHistory.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsHistory.updateString("SUB_ACCOUNT_CODE",getAttribute("SUB_ACCOUNT_CODE").getString());
            rsHistory.updateDouble("AMOUNT",getAttribute("AMOUNT").getDouble());
            rsHistory.updateString("TRANSFER_CODE",getAttribute("TRANSFER_CODE").getString());
            rsHistory.updateString("TRANSFER_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("TRANSFER_DATE").getString()));
            
            rsHistory.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
            rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateString("REJECTED_REMARKS","");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            //======== Update the Approval Flow =========//
            ObjFlow.CompanyID=EITLERPGLOBAL.gCompanyID;
            ObjFlow.ModuleID=ModuleID;
            ObjFlow.DocNo=getAttribute("DOC_NO").getString();
            ObjFlow.From=getAttribute("FROM").getInt();
            ObjFlow.To=getAttribute("TO").getInt();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_FD_DEPOSIT_UNCLAIMED";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=getAttribute("HIERARCHY_ID").getInt();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName="DOC_NO";
            ObjFlow.UseSpecifiedURL=true;
            ObjFlow.SpecificURL=FinanceGlobal.FinURL;
            ObjFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            
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
            
            MoveLast();
            if(ObjFlow.Status.equals("F")) {
                String ReceiptNo = data.getStringValueFromDB("SELECT RECEIPT_NO FROM D_FD_DEPOSIT_UNCLAIMED WHERE DOC_NO='"+theDocNo+"' ",FinanceGlobal.FinURL);
                String TransferCode = data.getStringValueFromDB("SELECT TRANSFER_CODE FROM D_FD_DEPOSIT_UNCLAIMED WHERE DOC_NO='"+theDocNo+"' ",FinanceGlobal.FinURL);
                String SubAccountCode = data.getStringValueFromDB("SELECT SUB_ACCOUNT_CODE FROM D_FD_DEPOSIT_UNCLAIMED WHERE DOC_NO='"+theDocNo+"' ",FinanceGlobal.FinURL);
                String MainAccountCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FD_DEPOSIT_UNCLAIMED WHERE DOC_NO='"+theDocNo+"' ",FinanceGlobal.FinURL);
                if(PostVoucher25(EITLERPGLOBAL.gCompanyID,ReceiptNo,theDocNo)) {
                    data.Execute("UPDATE D_FD_DEPOSIT_MASTER SET MAIN_ACCOUNT_CODE="+TransferCode+" WHERE RECEIPT_NO='"+ReceiptNo+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND PARTY_CODE='"+SubAccountCode+"' ",FinanceGlobal.FinURL);
                    data.Execute("UPDATE D_FD_DEPOSIT_MASTER_H SET MAIN_ACCOUNT_CODE="+TransferCode+" WHERE RECEIPT_NO='"+ReceiptNo+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND PARTY_CODE='"+SubAccountCode+"' ",FinanceGlobal.FinURL);
                } else {
                    return false;
                }
            }
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
            LastError= e.getMessage();
            return false;
        }
    }
    
    private boolean PostVoucher25(int CompanyID,String ReceiptNo, String curDocNo) {
        try {
            clsVoucher objVoucher=new clsVoucher();
            
            String SelPrefix="";
            String SelSuffix="";
            int FFNo=0;
            
            /*========== Select the Hierarchy ======== */
            setAttribute("FIN_HIERARCHY_ID",0);
            HashMap List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsCalcInterest.ModuleID, "CHOOSE_HIERARCHY", "DEFAULT", "2");
            if(List.size()>0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                int HierarchyID=UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                setAttribute("FIN_HIERARCHY_ID",HierarchyID);
            }
            /*========== End of Hierarchy Selection ======== */
            
            /*========== Select Prifix, Suffix and Firstfree No. ======== */
            ResultSet rsTmp=data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID="+clsVoucher.JournalVoucherModuleID);
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                SelPrefix=rsTmp.getString("PREFIX_CHARS");
                SelSuffix=rsTmp.getString("SUFFIX_CHARS");
                FFNo=rsTmp.getInt("FIRSTFREE_NO");
            }
            /*========== End of Prifix, Suffix and Firstfree No. ======== */
            
            //============= Gethering Data =================//
            int VoucherSrNo=0;
            String MainAccountCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FD_DEPOSIT_UNCLAIMED WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String SubAccountCode = data.getStringValueFromDB("SELECT SUB_ACCOUNT_CODE FROM D_FD_DEPOSIT_UNCLAIMED WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String TransferCode = data.getStringValueFromDB("SELECT TRANSFER_CODE FROM D_FD_DEPOSIT_UNCLAIMED WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String TransferDate = data.getStringValueFromDB("SELECT TRANSFER_DATE FROM D_FD_DEPOSIT_UNCLAIMED WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String BookCode = "";
            List.clear();
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsUnclaimedDepositTransfer.ModuleID, "GET_BOOK_CODE", "DEPOSIT_TRANSFER", "");
            if(List.size()>0) {
                //Get Book Code = 25 for Deposit Transfer
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                BookCode = objRule.getAttribute("RULE_OUTCOME").getString();
            }
            
            double Amount = data.getDoubleValueFromDB("SELECT AMOUNT FROM D_FD_DEPOSIT_UNCLAIMED WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String BankName = data.getStringValueFromDB("SELECT BOOK_NAME FROM D_FIN_BOOK_MASTER WHERE MAIN_ACCOUNT_CODE='"+BookCode+"' ",FinanceGlobal.FinURL);
            String DocDate = data.getStringValueFromDB("SELECT DOC_DATE FROM D_FD_DEPOSIT_UNCLAIMED WHERE DOC_NO='"+curDocNo+"'",FinanceGlobal.FinURL);
            //============= Gethering Data =======================//
            
            /*============== Preparing Voucher Header ================*/
            objVoucher=new clsVoucher();
            objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
            objVoucher.IsInternalPosting=true;
            objVoucher.setAttribute("PREFIX",SelPrefix);
            objVoucher.setAttribute("SUFFIX",SelSuffix);
            objVoucher.setAttribute("FFNO",FFNo);
            objVoucher.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            objVoucher.setAttribute("VOUCHER_NO","");
            objVoucher.setAttribute("BOOK_CODE",BookCode);
            objVoucher.setAttribute("VOUCHER_TYPE",FinanceGlobal.TYPE_JOURNAL);
            objVoucher.setAttribute("CHEQUE_NO","");
            objVoucher.setAttribute("CHEQUE_DATE","0000-00-00");
            objVoucher.setAttribute("BANK_NAME",BankName);
            objVoucher.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(TransferDate));//
            objVoucher.setAttribute("ST_CATEGORY","");
            objVoucher.setAttribute("MODULE_ID",0);
            objVoucher.setAttribute("REMARKS","");
            objVoucher.setAttribute("HIERARCHY_ID",getAttribute("FIN_HIERARCHY_ID").getInt());
            int FirstUserID=data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID="+getAttribute("FIN_HIERARCHY_ID").getInt()+" AND SR_NO=1");
            objVoucher.setAttribute("FROM",FirstUserID);
            objVoucher.setAttribute("TO",FirstUserID);
            objVoucher.setAttribute("FROM_REMARKS","");
            objVoucher.setAttribute("APPROVAL_STATUS","F"); //Final Approved --> Voucher
            /*============== End of Voucher Header ================*/
            
            /*============== Voucher Detail =================*/
            objVoucher.colVoucherItems.clear();
            VoucherSrNo++;
            clsVoucherItem objVoucherItem=new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT","D");
            objVoucherItem.setAttribute("ACCOUNT_ID",1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode);
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",SubAccountCode);
            objVoucherItem.setAttribute("AMOUNT",Amount);
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO",curDocNo);
            objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(DocDate));
            objVoucherItem.setAttribute("MODULE_ID",clsUnclaimedDepositTransfer.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            VoucherSrNo++;
            objVoucherItem=new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT","C");
            objVoucherItem.setAttribute("ACCOUNT_ID",1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",TransferCode);
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",SubAccountCode);
            objVoucherItem.setAttribute("AMOUNT",Amount);
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO",curDocNo);
            objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(DocDate));
            objVoucherItem.setAttribute("MODULE_ID",clsUnclaimedDepositTransfer.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            /*============== End of Voucher Detail =================*/
            
            if(objVoucher.Insert()) {
                objVoucher.IsInternalPosting=false;
                return true;
            } else {
                objVoucher.IsInternalPosting=false;
                return false;
            }
        }
        
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean Update() {
        
        Statement stHistory;
        ResultSet rsHistory;
        boolean Validate=true;
        
        
        try {
            String theDocNo=getAttribute("DOC_NO").getString();
            
            //** Validations **//
            if(!Validate()) {
                return false;
            }
            
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            String AStatus=getAttribute("APPROVAL_STATUS").getString();
            if(AStatus.equals("R")) {
                Validate=false; //Do not validate while in rejection mode.
            }
            
            //** Open History Table Connections **//
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FD_DEPOSIT_UNCLAIMED_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //** --------------------------------**//
            
            //========= Updating Into Deposit Refund =================//
            
            rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsResultSet.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsResultSet.updateString("DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsResultSet.updateString("RECEIPT_NO",getAttribute("RECEIPT_NO").getString());
            rsResultSet.updateString("RECEIPT_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("RECEIPT_DATE").getString()));
            rsResultSet.updateString("APPLICANT_NAME",getAttribute("APPLICANT_NAME").getString());
            rsResultSet.updateString("EFFECTIVE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_DATE").getString()));
            rsResultSet.updateString("MATURITY_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MATURITY_DATE").getString()));
            rsResultSet.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsResultSet.updateString("SUB_ACCOUNT_CODE",getAttribute("SUB_ACCOUNT_CODE").getString());
            rsResultSet.updateDouble("AMOUNT",getAttribute("AMOUNT").getDouble());
            rsResultSet.updateString("TRANSFER_CODE",getAttribute("TRANSFER_CODE").getString());
            rsResultSet.updateString("TRANSFER_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("TRANSFER_DATE").getString()));
            
            rsResultSet.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
            //rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            //rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateString("REJECTED_REMARKS","");
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateRow();
            
            //------------------------------------------------------//
            
            //========= Inserting Into History =================//
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM D_FD_DEPOSIT_UNCLAIMED_H WHERE DOC_NO='"+theDocNo+"'",FinanceGlobal.FinURL);
            RevNo++;
            String RevDocNo=getAttribute("DOC_NO").getString();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo);
            rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            rsHistory.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsHistory.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsHistory.updateString("DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsHistory.updateString("RECEIPT_NO",getAttribute("RECEIPT_NO").getString());
            rsHistory.updateString("RECEIPT_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("RECEIPT_DATE").getString()));
            rsHistory.updateString("APPLICANT_NAME",getAttribute("APPLICANT_NAME").getString());
            rsHistory.updateString("EFFECTIVE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_DATE").getString()));
            rsHistory.updateString("MATURITY_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MATURITY_DATE").getString()));
            rsHistory.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsHistory.updateString("SUB_ACCOUNT_CODE",getAttribute("SUB_ACCOUNT_CODE").getString());
            rsHistory.updateDouble("AMOUNT",getAttribute("AMOUNT").getDouble());
            rsHistory.updateString("TRANSFER_CODE",getAttribute("TRANSFER_CODE").getString());
            rsHistory.updateString("TRANSFER_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("TRANSFER_DATE").getString()));
            
            rsHistory.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
            rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateString("REJECTED_REMARKS","");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            //-------------------------------------------------//
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=EITLERPGLOBAL.gCompanyID;
            ObjFlow.ModuleID=ModuleID;
            ObjFlow.DocNo=getAttribute("DOC_NO").getObj().toString();
            ObjFlow.From=getAttribute("FROM").getInt();
            ObjFlow.To=getAttribute("TO").getInt();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_FD_DEPOSIT_UNCLAIMED";
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
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_FD_DEPOSIT_UNCLAIMED SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO="+theDocNo+"'",FinanceGlobal.FinURL);
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE MODULE_ID="+ModuleID+" AND DOC_NO='"+theDocNo+"'");
                
                ObjFlow.IsCreator=true;
            }
            //==========================================//
            
            if(ObjFlow.Status.equals("H")) {
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
            MoveLast();
            if(ObjFlow.Status.equals("F")) {
                String ReceiptNo = data.getStringValueFromDB("SELECT RECEIPT_NO FROM D_FD_DEPOSIT_UNCLAIMED WHERE DOC_NO='"+theDocNo+"' ",FinanceGlobal.FinURL);
                String TransferCode = data.getStringValueFromDB("SELECT TRANSFER_CODE FROM D_FD_DEPOSIT_UNCLAIMED WHERE DOC_NO='"+theDocNo+"' ",FinanceGlobal.FinURL);
                String SubAccountCode = data.getStringValueFromDB("SELECT SUB_ACCOUNT_CODE FROM D_FD_DEPOSIT_UNCLAIMED WHERE DOC_NO='"+theDocNo+"' ",FinanceGlobal.FinURL);
                String MainAccountCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FD_DEPOSIT_UNCLAIMED WHERE DOC_NO='"+theDocNo+"' ",FinanceGlobal.FinURL);
                if(PostVoucher25(EITLERPGLOBAL.gCompanyID,ReceiptNo,theDocNo)) {
                    data.Execute("UPDATE D_FD_DEPOSIT_MASTER SET MAIN_ACCOUNT_CODE="+TransferCode+" WHERE RECEIPT_NO='"+ReceiptNo+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND PARTY_CODE='"+SubAccountCode+"' ",FinanceGlobal.FinURL);
                    data.Execute("UPDATE D_FD_DEPOSIT_MASTER_H SET MAIN_ACCOUNT_CODE="+TransferCode+" WHERE RECEIPT_NO='"+ReceiptNo+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND PARTY_CODE='"+SubAccountCode+"' ",FinanceGlobal.FinURL);
                } else {
                    //JOptionPane.showMessageDialog(null,"Voucher Not Pasted for receipt = " +ReceiptNo);
                    LastError = "Voucher Not Pasted for receipt = " +ReceiptNo;
                    return false;
                }
            }
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }
    
    public boolean CanDelete(int CompanyID,String DocNo, int UserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            
            if(HistoryView) {
                return false;
            }
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FD_DEPOSIT_UNCLAIMED WHERE DOC_NO='"+DocNo+"' AND (APPROVED=1)";
            int Count=data.getIntValueFromDB(strSQL, FinanceGlobal.FinURL);
            
            if(Count>0)  //Item is Approved
            {
                //Discard the request
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
    
    
    //This routine checks and returns whether the item is editable or not
    //Criteria is Approved item cannot be changed
    //and if not approved then user id is checked whether doucment
    //is waiting for his approval.
    public boolean IsEditable(int CompanyID,String DocNo,int UserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT DOC_NO FROM D_FD_DEPOSIT_UNCLAIMED WHERE DOC_NO='"+DocNo+"' AND (APPROVED=1)";
            
            if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                return false;
            }
            else {
                strSQL="SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+DocNo+"' AND USER_ID="+UserID+" AND STATUS='W'";
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
        catch(Exception e) {
            return false;
        }
    }
    
    public boolean Delete(int UserID) {
        try {
            String DocNo=getAttribute("DOC_NO").getObj().toString();
            
            if(CanDelete(EITLERPGLOBAL.gCompanyID,DocNo,UserID)) {
                String strSQL = "DELETE FROM D_FD_DEPOSIT_UNCLAIMED WHERE DOC_NO='"+DocNo+"' ";
                data.Execute(strSQL);
                
                LoadData(EITLERPGLOBAL.gCompanyID);
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
    
    public Object getObject(int CompanyID, String DocNo) {
        String strCondition = " WHERE DOC_NO='" + DocNo +"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" ";
        clsUnclaimedDepositTransfer objUnclaimedDepositTransfer = new clsUnclaimedDepositTransfer();
        objUnclaimedDepositTransfer.Filter(strCondition,CompanyID);
        return objUnclaimedDepositTransfer;
    }
    
    public boolean Filter(String Condition,int CompanyID) {
        Ready=false;
        
        try {
            String strSQL = "SELECT * FROM D_FD_DEPOSIT_UNCLAIMED " + Condition ;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSQL);
            
            if(!rsResultSet.first()) {
                strSQL = "SELECT * FROM D_FD_DEPOSIT_UNCLAIMED ORDER BY DOC_NO";
                rsResultSet=Stmt.executeQuery(strSQL);
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
                RevNo=UtilFunctions.getInt(rsResultSet,"REVISION_NO",0);
                setAttribute("REVISION_NO",RevNo);
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            setAttribute("COMPANY_ID",UtilFunctions.getInt(rsResultSet,"COMPANY_ID",0));
            setAttribute("DOC_NO",UtilFunctions.getString(rsResultSet,"DOC_NO",""));
            setAttribute("DOC_DATE",UtilFunctions.getString(rsResultSet,"DOC_DATE","0000-00-00"));
            setAttribute("RECEIPT_NO",UtilFunctions.getString(rsResultSet,"RECEIPT_NO",""));
            setAttribute("RECEIPT_DATE",UtilFunctions.getString(rsResultSet,"RECEIPT_DATE","0000-00-00"));
            setAttribute("APPLICANT_NAME",UtilFunctions.getString(rsResultSet,"APPLICANT_NAME",""));
            setAttribute("EFFECTIVE_DATE",UtilFunctions.getString(rsResultSet,"EFFECTIVE_DATE","0000-00-00"));
            setAttribute("MATURITY_DATE",UtilFunctions.getString(rsResultSet,"MATURITY_DATE","0000-00-00"));
            setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsResultSet,"MAIN_ACCOUNT_CODE",""));
            setAttribute("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsResultSet,"SUB_ACCOUNT_CODE",""));
            setAttribute("AMOUNT",UtilFunctions.getDouble(rsResultSet,"AMOUNT",0.0));
            setAttribute("TRANSFER_CODE",UtilFunctions.getString(rsResultSet,"TRANSFER_CODE",""));
            setAttribute("TRANSFER_DATE",UtilFunctions.getString(rsResultSet,"TRANSFER_DATE","0000-00-00"));
            setAttribute("APPROVED",UtilFunctions.getBoolean(rsResultSet,"APPROVED",false));
            setAttribute("APPROVED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"APPROVED_DATE","0000-00-00")));
            setAttribute("REJECTED",UtilFunctions.getBoolean(rsResultSet,"REJECTED",false));
            setAttribute("REJECTED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"REJECTED_DATE","0000-00-00")));
            setAttribute("REJECTED_REMARKS",UtilFunctions.getString(rsResultSet,"REJECTED_REMARKS",""));
            setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00")));
            setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00")));
            setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsResultSet,"HIERARCHY_ID",0));
            setAttribute("CHANGED",UtilFunctions.getBoolean(rsResultSet,"CHANGED",false));
            setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHANGED_DATE","0000-00-00")));
            setAttribute("CANCELLED",UtilFunctions.getBoolean(rsResultSet,"CANCELLED",false));
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public static HashMap getPendingApprovals(int CompanyID,int UserID,int Order) {
        ResultSet rsTemp=null;
        String strSQL="";
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            
            if(Order==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT FINANCE.D_FD_DEPOSIT_UNCLAIMED.DOC_NO,FINANCE.D_FD_DEPOSIT_UNCLAIMED.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_DEPOSIT_UNCLAIMED,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_DEPOSIT_UNCLAIMED.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT FINANCE.D_FD_DEPOSIT_UNCLAIMED.DOC_NO,FINANCE.D_FD_DEPOSIT_UNCLAIMED.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_DEPOSIT_UNCLAIMED,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_DEPOSIT_UNCLAIMED.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_FD_DEPOSIT_UNCLAIMED.DOC_NO";
            }
            
            if(Order==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT FINANCE.D_FD_DEPOSIT_UNCLAIMED.DOC_NO,FINANCE.D_FD_DEPOSIT_UNCLAIMED.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_DEPOSIT_UNCLAIMED,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_DEPOSIT_UNCLAIMED.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_FD_DEPOSIT_UNCLAIMED.DOC_NO";
            }
            
            rsTemp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTemp.first();
            
            Counter=0;
            
            if(rsTemp.getRow()>0) {
                while(!rsTemp.isAfterLast()) {
                    Counter=Counter+1;
                    clsUnclaimedDepositTransfer ObjDoc=new clsUnclaimedDepositTransfer();
                    
                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("DOC_NO",UtilFunctions.getString(rsTemp,"DOC_NO",""));
                    String DocNo = UtilFunctions.getString(rsTemp,"DOC_NO","");
                    ObjDoc.setAttribute("DOC_DATE",UtilFunctions.getString(rsTemp,"DOC_DATE","0000-00-00"));
                    ObjDoc.setAttribute("RECEIVED_DATE",UtilFunctions.getString(rsTemp,"RECEIVED_DATE","0000-00-00"));
                    ObjDoc.setAttribute("DEPT_NAME","");
                    String PartyCode = data.getStringValueFromDB("SELECT SUB_ACCOUNT_CODE FROM D_FD_DEPOSIT_UNCLAIMED WHERE DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
                    String PartyName = data.getStringValueFromDB("SELECT APPLICANT_NAME FROM D_FD_DEPOSIT_UNCLAIMED WHERE DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
                    ObjDoc.setAttribute("PARTY_CODE",PartyCode);
                    ObjDoc.setAttribute("PARTY_NAME",PartyName);
                    // ----------------- End of Header Fields ------------------------------------//
                    
                    List.put(Long.toString(Counter),ObjDoc);
                    rsTemp.next();
                }
            }
            rsTemp.close();
        }
        catch(Exception e) {
        }
        return List;
    }
    
    public boolean ShowHistory(int CompanyID,String DocNo) {
        Ready=false;
        try {
            String strSQL="SELECT * FROM D_FD_DEPOSIT_UNCLAIMED_H WHERE DOC_NO='"+DocNo + "' ";
            rsResultSet=data.getResult(strSQL,FinanceGlobal.FinURL);
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
        ResultSet rsTmp;
        
        try {
            String strSQL="SELECT * FROM D_FD_DEPOSIT_UNCLAIMED_H WHERE DOC_NO='"+DocNo+"' ";
            rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsUnclaimedDepositTransfer objDR=new clsUnclaimedDepositTransfer();
                    
                    objDR.setAttribute("DOC_NO",UtilFunctions.getInt(rsTmp,"DOC_NO", 0));
                    objDR.setAttribute("REVISION_NO",UtilFunctions.getInt(rsTmp,"REVISION_NO", 0));
                    objDR.setAttribute("UPDATED_BY",UtilFunctions.getString(rsTmp,"UPDATED_BY", ""));
                    objDR.setAttribute("APPROVAL_STATUS",UtilFunctions.getString(rsTmp,"APPROVAL_STATUS", ""));
                    objDR.setAttribute("ENTRY_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"ENTRY_DATE","0000-00-00")));
                    objDR.setAttribute("APPROVER_REMARKS",UtilFunctions.getString(rsTmp,"APPROVER_REMARKS", ""));
                    
                    List.put(Integer.toString(List.size()+1),objDR);
                    rsTmp.next();
                }
            }
            rsTmp.close();
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
            
            String strSQL="SELECT DOC_NO,APPROVED,CANCELLED FROM D_FD_DEPOSIT_UNCLAIMED WHERE DOC_NO='"+DocNo+"'";
            rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                if(UtilFunctions.getBoolean(rsTmp,"APPROVED",false))   {
                    if(UtilFunctions.getBoolean(rsTmp,"CANCELLED",false))  {
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
            rsTmp=data.getResult("SELECT DOC_NO FROM D_FD_DEPOSIT_UNCLAIMED WHERE DOC_NO='"+DocNo+"' AND CANCELLED=0",FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
            }
            else {
                canCancel=true;
            }
            rsTmp.close();
        }
        catch(Exception e) {
        }
        return canCancel;
    }
    
    public static boolean CancelDoc(int CompanyID,String DocNo) {
        
        ResultSet rsTmp=null;
        boolean Cancelled=false;
        
        try {
            if(CanCancel(CompanyID,DocNo)) {
                
                boolean ApprovedDoc=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_FD_DEPOSIT_UNCLAIMED WHERE DOC_NO='"+DocNo+"'",FinanceGlobal.FinURL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                if(ApprovedDoc) {
                }
                else {
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"' AND MODULE_ID="+ModuleID,FinanceGlobal.FinURL);
                }
                
                data.Execute("UPDATE D_FD_DEPOSIT_UNCLAIMED SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='"+DocNo+"'",FinanceGlobal.FinURL);
                Cancelled=true;
            }
            rsTmp.close();
        }
        catch(Exception e) {
        }
        return Cancelled;
    }
    
    private boolean Validate() {
        if(!UnclaimedEntry) {
            if(getAttribute("RECEIPT_NO").getString().trim().equals("")) {
                LastError="Please specify Receipt No.";
                return false;
            } else if(!data.IsRecordExist("SELECT * FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+getAttribute("RECEIPT_NO").getString()+"' AND DEPOSIT_STATUS=0",FinanceGlobal.FinURL)) {
                LastError="Receipt No. does not exist. or Receipt is already closed.";
                return false;
            }
            
            if(!data.IsRecordExist("SELECT PARTY_CODE FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+getAttribute("SUB_ACCOUNT_CODE").getString()+"' AND MAIN_ACCOUNT_CODE='"+getAttribute("TRANSFER_CODE").getString()+"' ",FinanceGlobal.FinURL)) {
                LastError = "Party Code("+ getAttribute("SUB_ACCOUNT_CODE").getString() +") with main account code("+getAttribute("TRANSFER_CODE").getString()+") does not exists in party master. \nPlease varify Party code with main account code in party master.";
                return false;
            }
        }
        return true;
    }
}
