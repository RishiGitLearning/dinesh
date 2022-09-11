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

public class clsSalesDepositSchemeTransfer {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
    private boolean HistoryView=false;
    
    public static int ModuleID=144;
    
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
    public clsSalesDepositSchemeTransfer() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("PARTY_CODE",new Variant(""));
        props.put("PARTY_NAME",new Variant(""));
        
        props.put("FROM_DATE_TURNOVER",new Variant(""));
        props.put("TO_DATE_TURNOVER",new Variant(""));
        props.put("TURNOVER",new Variant(0));
        props.put("TRANSFER_DATE",new Variant(""));
        props.put("SCHEME_ID_FROM",new Variant(""));
        props.put("SCHEME_ID_TO",new Variant(""));
        
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
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FD_SALES_DEPOSIT_SCHEME_TRANSFER ORDER BY DOC_NO");
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
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FD_SALES_DEPOSIT_SCHEME_TRANSFER_H WHERE DOC_NO='1'");
            rsHistory.first();
            //----------------------------------//
            
            //** Validations **//
            if(!Validate()) {
                return false;
            }
            
            
            if(data.IsRecordExist("SELECT DOC_NO FROM D_FD_SALES_DEPOSIT_SCHEME_TRANSFER WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"'",FinanceGlobal.FinURL)) {
                LastError="Document with this Doc No. already exist.";
                return false;
            }
            //*****************//
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //========= Inserting Into Deposit Refund =================//
            
            setAttribute("DOC_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,ModuleID,getAttribute("FFNO").getInt(),true));
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsResultSet.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            theDocNo = getAttribute("DOC_NO").getString();
            rsResultSet.updateString("DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsResultSet.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsResultSet.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            rsResultSet.updateString("FROM_DATE_TURNOVER",EITLERPGLOBAL.formatDateDB(getAttribute("FROM_DATE_TURNOVER").getString()));
            rsResultSet.updateString("TO_DATE_TURNOVER",EITLERPGLOBAL.formatDateDB(getAttribute("TO_DATE_TURNOVER").getString()));
            rsResultSet.updateDouble("TURNOVER",getAttribute("TURNOVER").getDouble());
            rsResultSet.updateString("TRANSFER_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("TRANSFER_DATE").getString()));
            rsResultSet.updateString("SCHEME_ID_FROM",getAttribute("SCHEME_ID_FROM").getString());
            rsResultSet.updateString("SCHEME_ID_TO",getAttribute("SCHEME_ID_TO").getString());
            rsResultSet.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
            rsResultSet.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
            rsResultSet.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
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
            rsHistory.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsHistory.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            rsHistory.updateString("FROM_DATE_TURNOVER",EITLERPGLOBAL.formatDateDB(getAttribute("FROM_DATE_TURNOVER").getString()));
            rsHistory.updateString("TO_DATE_TURNOVER",EITLERPGLOBAL.formatDateDB(getAttribute("TO_DATE_TURNOVER").getString()));
            rsHistory.updateDouble("TURNOVER",getAttribute("TURNOVER").getDouble());
            rsHistory.updateString("TRANSFER_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("TRANSFER_DATE").getString()));
            rsHistory.updateString("SCHEME_ID_FROM",getAttribute("SCHEME_ID_FROM").getString());
            rsHistory.updateString("SCHEME_ID_TO",getAttribute("SCHEME_ID_TO").getString());
            rsHistory.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
            rsHistory.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
            rsHistory.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            //======== Update the Approval Flow =========//
            ObjFlow.CompanyID=EITLERPGLOBAL.gCompanyID;
            ObjFlow.ModuleID=ModuleID;
            ObjFlow.DocNo=getAttribute("DOC_NO").getString();
            ObjFlow.From=getAttribute("FROM").getInt();
            ObjFlow.To=getAttribute("TO").getInt();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_FD_SALES_DEPOSIT_SCHEME_TRANSFER";
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
                String SchemeFrom = data.getStringValueFromDB("SELECT SCHEME_ID_FROM FROM D_FD_SALES_DEPOSIT_SCHEME_TRANSFER WHERE DOC_NO='"+theDocNo+"' ",FinanceGlobal.FinURL);
                String SchemeTo = data.getStringValueFromDB("SELECT SCHEME_ID_TO FROM D_FD_SALES_DEPOSIT_SCHEME_TRANSFER WHERE DOC_NO='"+theDocNo+"' ",FinanceGlobal.FinURL);
                String PartyCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_SALES_DEPOSIT_SCHEME_TRANSFER WHERE DOC_NO='"+theDocNo+"' ",FinanceGlobal.FinURL);
                ResultSet rsSalesDeposit = data.getResult("SELECT * FROM D_FD_SALES_DEPOSIT_MASTER WHERE PARTY_CODE='"+PartyCode+"' AND DEPOSIT_STATUS=0",FinanceGlobal.FinURL);
                rsSalesDeposit.first();
                while(!rsSalesDeposit.isAfterLast()) {
                    String ReceiptNo = rsSalesDeposit.getString("RECEIPT_NO");
                    if(PostJournalVoucher(ReceiptNo,SchemeFrom,SchemeTo,theDocNo)) {
                        String MainAccountCode = data.getStringValueFromDB("SELECT DEPOSIT_MAIN_ACCOUNT_CODE FROM D_FD_SCHME_MASTER WHERE SCHEME_ID='"+SchemeTo+"' ",FinanceGlobal.FinURL);
                        String InterestMainCode = data.getStringValueFromDB("SELECT INT_MAIN_ACCOUNT_CODE FROM D_FD_SCHME_MASTER WHERE SCHEME_ID='"+SchemeTo+"' ",FinanceGlobal.FinURL);
                        double InterestRate = data.getDoubleValueFromDB("SELECT INTEREST_PERCENTAGE FROM D_FD_SCHME_PERIOD WHERE SCHEME_ID='"+SchemeTo+"' ",FinanceGlobal.FinURL);
                        data.Execute("UPDATE D_FD_SALES_DEPOSIT_MASTER SET MAIN_ACCOUNT_CODE='"+MainAccountCode+"',INTEREST_MAIN_CODE='"+InterestMainCode+"',INTEREST_RATE="+InterestRate+",SCHEME_ID='"+SchemeTo+"' WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
                        data.Execute("UPDATE D_FD_SALES_DEPOSIT_MASTER_H SET MAIN_ACCOUNT_CODE='"+MainAccountCode+"',INTEREST_MAIN_CODE='"+InterestMainCode+"',INTEREST_RATE="+InterestRate+",SCHEME_ID='"+SchemeTo+"' WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
                    } else {
                        JOptionPane.showMessageDialog(null,"Voucher not posted for Receipt No = " + ReceiptNo);
                        return false;
                    }
                    rsSalesDeposit.next();
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
    
    private boolean PostJournalVoucher(String ReceiptNo,String SchemeFrom,String SchemeTo,String curDocNo) {
        try {
            String SelPrefix="";
            String SelSuffix="";
            int FFNo=0;
            int HierarchyID=0;
            /*========== Select the Hierarchy ======== */
            HashMap List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesInterest.ModuleID, "CHOOSE_HIERARCHY", "DEFAULT", "");
            if(List.size()>0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                HierarchyID=UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
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
            String OldMainAccountCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL);
            String NewMainAccountCode = data.getStringValueFromDB("SELECT DEPOSIT_MAIN_ACCOUNT_CODE FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+SchemeTo+"' ",FinanceGlobal.FinURL);
            String PartyCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL);
            String TransferDate = data.getStringValueFromDB("SELECT TRANSFER_DATE FROM D_FD_SALES_DEPOSIT_SCHEME_TRANSFER WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String BookCode = "";
            List.clear();
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesDepositMaster.ModuleID, "GET_BOOK_CODE", "DEPOSIT_TRANSFER", "");
            if(List.size()>0) {
                //Get Book Code = 21 for Journal Voucher
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                BookCode = objRule.getAttribute("RULE_OUTCOME").getString();
            }
            double Amount = data.getDoubleValueFromDB("SELECT AMOUNT FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL);
            String BankName = data.getStringValueFromDB("SELECT BOOK_NAME FROM D_FIN_BOOK_MASTER WHERE BOOK_CODE='"+BookCode+"' ",FinanceGlobal.FinURL);
            String ReceiptDate = data.getStringValueFromDB("SELECT RECEIPT_DATE FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL);
            //============= Gethering Data =======================//
            
            /*============== Preparing Voucher Header ================*/
            clsVoucher objVoucher=new clsVoucher();
            objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
            objVoucher.DepositTransfer=true;
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
            
            objVoucher.setAttribute("HIERARCHY_ID",HierarchyID);
            int FirstUserID=data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID="+HierarchyID+" AND SR_NO=1");
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
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",OldMainAccountCode);
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",PartyCode);
            objVoucherItem.setAttribute("AMOUNT",Amount);
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO",ReceiptNo);
            objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(ReceiptDate));
            objVoucherItem.setAttribute("MODULE_ID",ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            VoucherSrNo++;
            objVoucherItem=new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT","C");
            objVoucherItem.setAttribute("ACCOUNT_ID",1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",NewMainAccountCode);
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",PartyCode);
            objVoucherItem.setAttribute("AMOUNT",Amount);
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO",ReceiptNo);
            objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(ReceiptDate));
            objVoucherItem.setAttribute("MODULE_ID",ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            /*============== End of Voucher Detail =================*/
            
            if(objVoucher.Insert()) {
                objVoucher.DepositTransfer=true;
                return true;
            } else {
                objVoucher.DepositTransfer=true;
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
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FD_SALES_DEPOSIT_SCHEME_TRANSFER_H WHERE DOC_NO='1'");
            rsHistory.first();
            //** --------------------------------**//
            
            //========= Updating Into Deposit Refund =================//
            
            rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsResultSet.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsResultSet.updateString("DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            
            rsResultSet.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsResultSet.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            rsResultSet.updateString("FROM_DATE_TURNOVER",EITLERPGLOBAL.formatDateDB(getAttribute("FROM_DATE_TURNOVER").getString()));
            rsResultSet.updateString("TO_DATE_TURNOVER",EITLERPGLOBAL.formatDateDB(getAttribute("TO_DATE_TURNOVER").getString()));
            rsResultSet.updateDouble("TURNOVER",getAttribute("TURNOVER").getDouble());
            rsResultSet.updateString("TRANSFER_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("TRANSFER_DATE").getString()));
            rsResultSet.updateString("SCHEME_ID_FROM",getAttribute("SCHEME_ID_FROM").getString());
            rsResultSet.updateString("SCHEME_ID_TO",getAttribute("SCHEME_ID_TO").getString());
            rsResultSet.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
            rsResultSet.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
            rsResultSet.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateRow();
            
            //------------------------------------------------------//
            
            //========= Inserting Into History =================//
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM D_FD_SALES_DEPOSIT_SCHEME_TRANSFER_H WHERE DOC_NO='"+theDocNo+"'",FinanceGlobal.FinURL);
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
            rsHistory.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsHistory.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            rsHistory.updateString("FROM_DATE_TURNOVER",EITLERPGLOBAL.formatDateDB(getAttribute("FROM_DATE_TURNOVER").getString()));
            rsHistory.updateString("TO_DATE_TURNOVER",EITLERPGLOBAL.formatDateDB(getAttribute("TO_DATE_TURNOVER").getString()));
            rsHistory.updateDouble("TURNOVER",getAttribute("TURNOVER").getDouble());
            rsHistory.updateString("TRANSFER_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("TRANSFER_DATE").getString()));
            rsHistory.updateString("SCHEME_ID_FROM",getAttribute("SCHEME_ID_FROM").getString());
            rsHistory.updateString("SCHEME_ID_TO",getAttribute("SCHEME_ID_TO").getString());
            rsHistory.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
            rsHistory.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
            rsHistory.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
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
            ObjFlow.TableName="D_FD_SALES_DEPOSIT_SCHEME_TRANSFER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=getAttribute("HIERARCHY_ID").getInt();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName="DOC_NO";
            ObjFlow.UseSpecifiedURL=true;
            ObjFlow.SpecificURL=FinanceGlobal.FinURL;
            ObjFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            //==== Handling Rejected Documents ==========//
            
            if(AStatus.equals("R")) {
                ObjFlow.To=getAttribute("SEND_DOC_TO").getInt();
                ObjFlow.ExplicitSendTo=true;
            }
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_FD_SALES_DEPOSIT_SCHEME_TRANSFER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO="+theDocNo+"'",FinanceGlobal.FinURL);
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
                String SchemeFrom = data.getStringValueFromDB("SELECT SCHEME_ID_FROM FROM D_FD_SALES_DEPOSIT_SCHEME_TRANSFER WHERE DOC_NO='"+theDocNo+"' ",FinanceGlobal.FinURL);
                String SchemeTo = data.getStringValueFromDB("SELECT SCHEME_ID_TO FROM D_FD_SALES_DEPOSIT_SCHEME_TRANSFER WHERE DOC_NO='"+theDocNo+"' ",FinanceGlobal.FinURL);
                String PartyCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_SALES_DEPOSIT_SCHEME_TRANSFER WHERE DOC_NO='"+theDocNo+"' ",FinanceGlobal.FinURL);
                ResultSet rsSalesDeposit = data.getResult("SELECT * FROM D_FD_SALES_DEPOSIT_MASTER WHERE PARTY_CODE='"+PartyCode+"' AND DEPOSIT_STATUS=0",FinanceGlobal.FinURL);
                rsSalesDeposit.first();
                while(!rsSalesDeposit.isAfterLast()) {
                    String ReceiptNo = rsSalesDeposit.getString("RECEIPT_NO");
                    if(PostJournalVoucher(ReceiptNo,SchemeFrom,SchemeTo,theDocNo)) {
                        String MainAccountCode = data.getStringValueFromDB("SELECT DEPOSIT_MAIN_ACCOUNT_CODE FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+SchemeTo+"' ",FinanceGlobal.FinURL);
                        String InterestMainCode = data.getStringValueFromDB("SELECT INT_MAIN_ACCOUNT_CODE FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+SchemeTo+"' ",FinanceGlobal.FinURL);
                        double InterestRate = data.getDoubleValueFromDB("SELECT INTEREST_PERCENTAGE FROM D_FD_SCHEME_PERIOD WHERE SCHEME_ID='"+SchemeTo+"' ",FinanceGlobal.FinURL);
                        data.Execute("UPDATE D_FD_SALES_DEPOSIT_MASTER SET MAIN_ACCOUNT_CODE='"+MainAccountCode+"',INTEREST_MAIN_CODE='"+InterestMainCode+"',INTEREST_RATE="+InterestRate+" WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
                        data.Execute("UPDATE D_FD_SALES_DEPOSIT_MASTER_H SET MAIN_ACCOUNT_CODE='"+MainAccountCode+"',INTEREST_MAIN_CODE='"+InterestMainCode+"',INTEREST_RATE="+InterestRate+" WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
                    } else {
                        JOptionPane.showMessageDialog(null,"Voucher not posted for Receipt No = " + ReceiptNo);
                        return false;
                    }
                    rsSalesDeposit.next();
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FD_SALES_DEPOSIT_SCHEME_TRANSFER WHERE DOC_NO='"+DocNo+"' AND (APPROVED=1)";
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
            strSQL="SELECT DOC_NO FROM D_FD_SALES_DEPOSIT_SCHEME_TRANSFER WHERE DOC_NO='"+DocNo+"' AND (APPROVED=1)";
            
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
                String strSQL = "DELETE FROM D_FD_SALES_DEPOSIT_SCHEME_TRANSFER WHERE DOC_NO='"+DocNo+"' ";
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
        String strCondition = " WHERE DOC_NO='" + DocNo +"' ";
        clsSalesDepositSchemeTransfer objSalesDepositSchemeTransfer = new clsSalesDepositSchemeTransfer();
        objSalesDepositSchemeTransfer.Filter(strCondition,CompanyID);
        return objSalesDepositSchemeTransfer;
    }
    
    public boolean Filter(String Condition,int CompanyID) {
        Ready=false;
        
        try {
            String strSQL = "SELECT * FROM D_FD_SALES_DEPOSIT_SCHEME_TRANSFER  " + Condition ;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSQL);
            
            if(!rsResultSet.first()) {
                strSQL = "SELECT * FROM D_FD_SALES_DEPOSIT_SCHEME_TRANSFER ORDER BY DOC_NO";
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
            setAttribute("PARTY_CODE",UtilFunctions.getString(rsResultSet,"PARTY_CODE",""));
            setAttribute("PARTY_NAME",UtilFunctions.getString(rsResultSet,"PARTY_NAME",""));
            setAttribute("FROM_DATE_TURNOVER",UtilFunctions.getString(rsResultSet,"FROM_DATE_TURNOVER","0000-00-00"));
            setAttribute("TO_DATE_TURNOVER",UtilFunctions.getString(rsResultSet,"TO_DATE_TURNOVER","0000-00-00"));
            setAttribute("TURNOVER",UtilFunctions.getDouble(rsResultSet,"TURNOVER",0));
            setAttribute("TURNOVER_DATE",UtilFunctions.getString(rsResultSet,"TURNOVER_DATE","0000-00-00"));
            setAttribute("SCHEME_ID_FROM",UtilFunctions.getString(rsResultSet,"SCHEME_ID_FROM",""));
            setAttribute("SCHEME_ID_TO",UtilFunctions.getString(rsResultSet,"SCHEME_ID_TO",""));
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
            setAttribute("APPROVED",UtilFunctions.getInt(rsResultSet,"APPROVED",0));
            setAttribute("CANCELLED",UtilFunctions.getInt(rsResultSet,"CANCELLED",0));
            
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
                strSQL="SELECT FINANCE.D_FD_SALES_DEPOSIT_SCHEME_TRANSFER.DOC_NO,FINANCE.D_FD_SALES_DEPOSIT_SCHEME_TRANSFER.DOC_DATE, "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_SALES_DEPOSIT_SCHEME_TRANSFER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_SALES_DEPOSIT_SCHEME_TRANSFER.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT FINANCE.D_FD_SALES_DEPOSIT_SCHEME_TRANSFER.DOC_NO,FINANCE.D_FD_SALES_DEPOSIT_SCHEME_TRANSFER.DOC_DATE, "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_SALES_DEPOSIT_SCHEME_TRANSFER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_SALES_DEPOSIT_SCHEME_TRANSFER.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_FD_SALES_DEPOSIT_SCHEME_TRANSFER.DOC_NO";
            }
            
            if(Order==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT FINANCE.D_FD_SALES_DEPOSIT_SCHEME_TRANSFER.DOC_NO,FINANCE.D_FD_SALES_DEPOSIT_SCHEME_TRANSFER.DOC_DATE, "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_SALES_DEPOSIT_SCHEME_TRANSFER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_SALES_DEPOSIT_SCHEME_TRANSFER.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_FD_SALES_DEPOSIT_SCHEME_TRANSFER.DOC_NO";
            }
            
            rsTemp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTemp.first();
            
            Counter=0;
            
            if(rsTemp.getRow()>0) {
                while(!rsTemp.isAfterLast()) {
                    Counter=Counter+1;
                    clsSalesDepositSchemeTransfer ObjDoc=new clsSalesDepositSchemeTransfer();
                    
                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("DOC_NO",UtilFunctions.getString(rsTemp,"DOC_NO",""));
                    String DocNo = UtilFunctions.getString(rsTemp,"DOC_NO","");
                    ObjDoc.setAttribute("DOC_DATE",UtilFunctions.getString(rsTemp,"DOC_DATE","0000-00-00"));
                    ObjDoc.setAttribute("RECEIVED_DATE",UtilFunctions.getString(rsTemp,"RECEIVED_DATE","0000-00-00"));
                    String PartyCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_SALES_DEPOSIT_SCHEME_TRANSFER WHERE DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
                    String PartyName = data.getStringValueFromDB("SELECT PARTY_NAME FROM D_FD_SALES_DEPOSIT_SCHEME_TRANSFER WHERE DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
                    ObjDoc.setAttribute("PARTY_CODE",UtilFunctions.getString(rsTemp,"PARTY_CODE",""));
                    ObjDoc.setAttribute("PARTY_NAME",UtilFunctions.getString(rsTemp,"PARTY_NAME",""));
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
            String strSQL="SELECT * FROM D_FD_SALES_DEPOSIT_SCHEME_TRANSFER_H WHERE DOC_NO="+DocNo;
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
            String strSQL="SELECT * FROM D_FD_SALES_DEPOSIT_SCHEME_TRANSFER_H WHERE DOC_NO='"+DocNo+"'";
            rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsSalesDepositSchemeTransfer objDR=new clsSalesDepositSchemeTransfer();
                    
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
            
            String strSQL="SELECT DOC_NO,APPROVED,CANCELLED FROM D_FD_SALES_DEPOSIT_SCHEME_TRANSFER WHERE DOC_NO='"+DocNo+"'";
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
            rsTmp=data.getResult("SELECT DOC_NO FROM D_FD_SALES_DEPOSIT_SCHEME_TRANSFER WHERE DOC_NO='"+DocNo+"' AND CANCELLED=0",FinanceGlobal.FinURL);
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
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_FD_SALES_DEPOSIT_SCHEME_TRANSFER WHERE DOC_NO='"+DocNo+"'",FinanceGlobal.FinURL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                if(ApprovedDoc) {
                }
                else {
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"' AND MODULE_ID="+ModuleID,FinanceGlobal.FinURL);
                }
                
                data.Execute("UPDATE D_FD_SALES_DEPOSIT_SCHEME_TRANSFER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='"+DocNo+"'",FinanceGlobal.FinURL);
                Cancelled=true;
            }
            rsTmp.close();
        }
        catch(Exception e) {
        }
        return Cancelled;
    }
    
    private boolean Validate() {
        String PartyCode = getAttribute("PARTY_CODE").getString();
        String SchemeIDTo = getAttribute("SCHEME_ID_TO").getString();
        String MainAccountCode = data.getStringValueFromDB("SELECT DEPOSIT_MAIN_ACCOUNT_CODE FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+SchemeIDTo+"' ",FinanceGlobal.FinURL);
        String InterestMainCode = data.getStringValueFromDB("SELECT INT_MAIN_ACCOUNT_CODE FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+SchemeIDTo+"' ",FinanceGlobal.FinURL);
        
        if(!data.IsRecordExist("SELECT PARTY_CODE FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+PartyCode+"' AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' ",FinanceGlobal.FinURL)) {
            LastError = "Party Code( "+ PartyCode +" ) with main account code( "+MainAccountCode+" ) does not exists in party master. \nPlease varify Party code with main account code in party master.";
            return false;
        }
        
        if(!data.IsRecordExist("SELECT PARTY_CODE FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+PartyCode+"' AND MAIN_ACCOUNT_CODE='"+InterestMainCode+"' ",FinanceGlobal.FinURL)) {
            LastError = "Party Code( "+ PartyCode +" ) with main account code( "+InterestMainCode+" ) does not exists in party master. \nPlease varify Party code with main account code in party master.";
            return false;
        }
        return true;
    }
    
    public static double getTurnoverAmount(String PartyCode,String FromDate,String ToDate) {
        double NetAmount = 0.0;
        try {
            NetAmount=data.getDoubleValueFromDB("SELECT SUM(NET_AMOUNT) AS TOTAL_AMOUNT FROM D_SAL_INVOICE_HEADER WHERE PARTY_CODE='"+PartyCode+"' AND INVOICE_DATE>='"+FromDate+"' AND INVOICE_DATE<='"+ToDate+"'");
        }
        catch(Exception e) {
            return NetAmount;
        }
        return NetAmount;
    }
}
