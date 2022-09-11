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

public class clsSalesDepositRefund {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public HashMap colLineItems;
    public HashMap colLineItemsOut;
    
    public boolean Ready = false;
    
    private boolean HistoryView=false;
    
    public static int ModuleID=145;
    
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
    public clsSalesDepositRefund() {
        LastError = "";
        props=new HashMap();
        
        props.put("FFNO",new Variant(0));
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("PARTY_CODE",new Variant(""));
        props.put("PARTY_NAME",new Variant(""));
        props.put("BANK_MAIN_CODE",new Variant(""));
        props.put("BANK_NAME",new Variant(""));
        props.put("FUND_TRANSFER",new Variant(""));
        props.put("PRINCIPLE_AMOUNT",new Variant(0.0));
        props.put("TOTAL_PRINCIPLE_AMOUNT",new Variant(0.0));
        props.put("PREVIOUS_INTEREST_AMOUNT",new Variant(0.0));
        props.put("EXCLUDE_INTEREST",new Variant(false));
        props.put("CURRENT_INTEREST_AMOUNT",new Variant(0.0));
        props.put("CURRENT_TDS_AMOUNT",new Variant(0.0));
        props.put("REFUND_DATE",new Variant(""));
        props.put("CHEQUE_NO",new Variant(""));
        props.put("CHEQUE_DATE",new Variant(""));
        props.put("VALUE_DATE",new Variant(""));
        props.put("VOUCHER_DATE",new Variant(""));
        
        props.put("DAYWISE_INTEREST",new Variant(false));
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
        
        colLineItems = new HashMap();
        colLineItemsOut = new HashMap();
    }
    
    /**Load Data. This method loads data from database to Business Object**/
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            Conn = data.getConn(FinanceGlobal.FinURL);
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery("SELECT * FROM D_FD_SALES_DEPOSIT_REFUND_HEADER ORDER BY DOC_NO");
            HistoryView = false;
            Ready = true;
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
        
        Statement stHistory,stDetail,stHDetail,stOut,stHOut; //,stTmpH,stTmp,stTmpOld
        ResultSet rsHistory,rsDetail,rsHDetail,rsOut,rsHOut; //,rsTmpH,rsTmp,rsTmpOld
        String theDocNo = "";
        try {
            
            //===== History Related Changes =====//
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FD_SALES_DEPOSIT_REFUND_HEADER_H WHERE DOC_NO='1'");
            rsHistory.first();
            //----------------------------------//
            
            //** Validations **//
            if(!Validate()) {
                return false;
            }
            
            
            if(data.IsRecordExist("SELECT DOC_NO FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"'",FinanceGlobal.FinURL)) {
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
            rsResultSet.updateString("BANK_MAIN_CODE",getAttribute("BANK_MAIN_CODE").getString());
            rsResultSet.updateString("BANK_NAME",getAttribute("BANK_NAME").getString());
            rsResultSet.updateString("FUND_TRANSFER",getAttribute("FUND_TRANSFER").getString());
            rsResultSet.updateDouble("PRINCIPLE_AMOUNT",getAttribute("PRINCIPLE_AMOUNT").getDouble());
            rsResultSet.updateDouble("TOTAL_PRINCIPLE_AMOUNT",getAttribute("TOTAL_PRINCIPLE_AMOUNT").getDouble());
            rsResultSet.updateDouble("PREVIOUS_INTEREST_AMOUNT",getAttribute("PREVIOUS_INTEREST_AMOUNT").getDouble());
            rsResultSet.updateBoolean("EXCLUDE_INTEREST",getAttribute("EXCLUDE_INTEREST").getBool());
            rsResultSet.updateDouble("CURRENT_INTEREST_AMOUNT",getAttribute("CURRENT_INTEREST_AMOUNT").getDouble());
            rsResultSet.updateDouble("CURRENT_TDS_AMOUNT",getAttribute("CURRENT_TDS_AMOUNT").getDouble());
            rsResultSet.updateString("REFUND_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("REFUND_DATE").getString()));
            rsResultSet.updateString("CHEQUE_NO",getAttribute("CHEQUE_NO").getString());
            rsResultSet.updateString("CHEQUE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CHEQUE_DATE").getString()));
            rsResultSet.updateBoolean("DAYWISE_INTEREST",getAttribute("DAYWISE_INTEREST").getBool()); //new code by vivek on 28/02/2014 for day wise interest calculation
            rsResultSet.updateString("VALUE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("VALUE_DATE").getString()));//new code by ashutosh on 12/08/2015 for insert value date
            rsResultSet.updateString("VOUCHER_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("VOUCHER_DATE").getString()));//new code by ashutosh on 12/08/2015 for insert voucher date
            
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
            rsHistory.updateString("DOC_NO", theDocNo);
            rsHistory.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsHistory.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            rsHistory.updateString("BANK_MAIN_CODE",getAttribute("BANK_MAIN_CODE").getString());
            rsHistory.updateString("BANK_NAME",getAttribute("BANK_NAME").getString());
            rsHistory.updateString("FUND_TRANSFER",getAttribute("FUND_TRANSFER").getString());
            rsHistory.updateDouble("PRINCIPLE_AMOUNT",getAttribute("PRINCIPLE_AMOUNT").getDouble());
            rsHistory.updateDouble("TOTAL_PRINCIPLE_AMOUNT",getAttribute("TOTAL_PRINCIPLE_AMOUNT").getDouble());
            rsHistory.updateDouble("PREVIOUS_INTEREST_AMOUNT",getAttribute("PREVIOUS_INTEREST_AMOUNT").getDouble());
            rsHistory.updateBoolean("EXCLUDE_INTEREST",getAttribute("EXCLUDE_INTEREST").getBool());
            rsHistory.updateDouble("CURRENT_INTEREST_AMOUNT",getAttribute("CURRENT_INTEREST_AMOUNT").getDouble());
            rsHistory.updateDouble("CURRENT_TDS_AMOUNT",getAttribute("CURRENT_TDS_AMOUNT").getDouble());
            rsHistory.updateString("REFUND_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("REFUND_DATE").getString()));
            rsHistory.updateString("CHEQUE_NO",getAttribute("CHEQUE_NO").getString());
            rsHistory.updateString("CHEQUE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CHEQUE_DATE").getString()));
            rsHistory.updateBoolean("DAYWISE_INTEREST",getAttribute("DAYWISE_INTEREST").getBool()); //new code by vivek on 28/02/2014 for day wise interest calculation 
            rsHistory.updateString("VALUE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("VALUE_DATE").getString()));//new code by ashutosh on 12/08/2015 for value date entry
            rsHistory.updateString("VOUCHER_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("VOUCHER_DATE").getString()));//new code by ashutosh on 12/08/2015 for voucher date entry
            
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
            rsHistory.updateString("REJECTED_REMARKS","");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            //--------------------------------------------------------------//
            
            //================== Inserting into Detail=================//
            
            stDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDetail=stDetail.executeQuery("SELECT * FROM D_FD_SALES_DEPOSIT_REFUND_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DOC_NO='1'");
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FD_SALES_DEPOSIT_REFUND_DETAIL_H WHERE DOC_NO='1'");
            rsDetail.first();
            rsHDetail.first();
            for(int i=1;i<=colLineItems.size();i++) {
                clsSalesDepositRefundItem ObjItem=(clsSalesDepositRefundItem) colLineItems.get(Integer.toString(i));
                rsDetail.moveToInsertRow();
                rsDetail.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsDetail.updateString("DOC_NO",theDocNo);
                rsDetail.updateLong("SR_NO",i);
                rsDetail.updateString("RECEIPT_NO",ObjItem.getAttribute("RECEIPT_NO").getString());
                rsDetail.updateString("RECEIPT_DATE",EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("RECEIPT_DATE").getString()));
                rsDetail.updateString("EFFECTIVE_DATE",EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("EFFECTIVE_DATE").getString()));
                rsDetail.updateString("MAIN_ACCOUNT_CODE", ObjItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsDetail.updateString("INTEREST_MAIN_CODE",ObjItem.getAttribute("INTEREST_MAIN_CODE").getString());
                rsDetail.updateDouble("INTEREST_RATE",ObjItem.getAttribute("INTEREST_RATE").getDouble());
                rsDetail.updateDouble("AMOUNT",ObjItem.getAttribute("AMOUNT").getDouble());
                rsDetail.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
                rsDetail.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsDetail.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
                rsDetail.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsDetail.updateBoolean("CHANGED",true);
                rsDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsDetail.updateBoolean("CANCELLED",false);
                rsDetail.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateString("DOC_NO",theDocNo);
                rsHDetail.updateLong("SR_NO",i);
                rsHDetail.updateString("RECEIPT_NO", ObjItem.getAttribute("RECEIPT_NO").getString());
                rsHDetail.updateString("RECEIPT_DATE", EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("RECEIPT_DATE").getString()));
                rsHDetail.updateString("EFFECTIVE_DATE", EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("EFFECTIVE_DATE").getString()));
                rsHDetail.updateString("MAIN_ACCOUNT_CODE",ObjItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsHDetail.updateString("INTEREST_MAIN_CODE",ObjItem.getAttribute("INTEREST_MAIN_CODE").getString());
                rsHDetail.updateDouble("INTEREST_RATE",ObjItem.getAttribute("INTEREST_RATE").getDouble());
                rsHDetail.updateDouble("AMOUNT",ObjItem.getAttribute("AMOUNT").getDouble());
                rsHDetail.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
                rsHDetail.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
                rsHDetail.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CANCELLED",false);
                rsHDetail.insertRow();
            }
            //---------------------------------------------------------//
            
            //=============== Inserting Into OutSatanding ====================//
            stOut=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsOut=stOut.executeQuery("SELECT * FROM D_FD_DEPOSIT_OUTSTANDING WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DOC_NO='1'");
            stHOut=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHOut=stHOut.executeQuery("SELECT * FROM D_FD_DEPOSIT_OUTSTANDING_H WHERE DOC_NO='1'");
            rsOut.first();
            rsHOut.first();
            
            for(int i=1;i<=colLineItemsOut.size();i++) {
                clsOutStandingItem ObjItemOut=(clsOutStandingItem) colLineItemsOut.get(Integer.toString(i));
                rsOut.moveToInsertRow();
                rsOut.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsOut.updateString("DOC_NO",theDocNo);
                rsOut.updateLong("SR_NO",i);
                rsOut.updateString("MAIN_ACCOUNT_CODE", ObjItemOut.getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsOut.updateDouble("AMOUNT",ObjItemOut.getAttribute("AMOUNT").getDouble());
                rsOut.updateString("EFFECT",ObjItemOut.getAttribute("EFFECT").getString());
                rsOut.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
                rsOut.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsOut.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
                rsOut.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsOut.updateBoolean("CHANGED",true);
                rsOut.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsOut.updateBoolean("CANCELLED",false);
                rsOut.insertRow();
                
                rsHOut.moveToInsertRow();
                rsHOut.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsHOut.updateInt("REVISION_NO",1);
                rsHOut.updateString("DOC_NO",theDocNo);
                rsHOut.updateLong("SR_NO",i);
                rsHOut.updateString("MAIN_ACCOUNT_CODE",ObjItemOut.getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsHOut.updateDouble("AMOUNT",ObjItemOut.getAttribute("AMOUNT").getDouble());
                rsHOut.updateString("EFFECT", ObjItemOut.getAttribute("EFFECT").getString());
                rsHOut.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
                rsHOut.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHOut.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
                rsHOut.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHOut.updateBoolean("CHANGED",true);
                rsHOut.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHOut.updateBoolean("CANCELLED",false);
                rsHOut.insertRow();
            }
            //----------------------------------------------------------------//
            
            //======== Update the Approval Flow =========//
            ObjFlow.CompanyID=EITLERPGLOBAL.gCompanyID;
            ObjFlow.ModuleID=ModuleID;
            ObjFlow.DocNo=getAttribute("DOC_NO").getString();
            ObjFlow.From=getAttribute("FROM").getInt();
            ObjFlow.To=getAttribute("TO").getInt();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_FD_SALES_DEPOSIT_REFUND_HEADER";
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
                int CompanyID = EITLERPGLOBAL.gCompanyID;
                String BankMainCode = data.getStringValueFromDB("SELECT BANK_MAIN_CODE FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+theDocNo+"' ",FinanceGlobal.FinURL);
                String FundTransferFrom = data.getStringValueFromDB("SELECT FUND_TRANSFER_FROM FROM D_FD_SALES_DEPOSIT_MASTER WHERE DOC_NO='"+theDocNo+"' ",FinanceGlobal.FinURL);
                if(!BankMainCode.equals("") && FundTransferFrom.equals("")) {
                    if(!PostPaymentVoucher(CompanyID, theDocNo)) {
                        JOptionPane.showMessageDialog(null,"Payment Voucher Not Posted Properly.");
                    } else {
                        String SQL = "SELECT REFUND_DATE FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+theDocNo+"' ";
                        String RefundDate = data.getStringValueFromDB(SQL,FinanceGlobal.FinURL);
                        ResultSet rsResult = data.getResult("SELECT * FROM D_FD_SALES_DEPOSIT_REFUND_DETAIL WHERE DOC_NO='"+theDocNo+"' ",FinanceGlobal.FinURL);
                        rsResult.first();
                        while(!rsResult.isAfterLast()) {
                            String ReceiptNo = rsResult.getString("RECEIPT_NO");
                            String PartyCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+theDocNo+"' ",FinanceGlobal.FinURL);
                            data.Execute("UPDATE D_FD_SALES_DEPOSIT_MASTER SET DEPOSIT_STATUS=1, REFUND_DATE='"+RefundDate+"' WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
                            data.Execute("UPDATE D_FD_SALES_DEPOSIT_MASTER_H SET DEPOSIT_STATUS=1, REFUND_DATE='"+RefundDate+"' WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
                            rsResult.next();
                        }
                    }
                } else if (BankMainCode.equals("") && !FundTransferFrom.equals("")){
                    if(!PostJournalVoucher(CompanyID, theDocNo)) {
                        JOptionPane.showMessageDialog(null,"Journal Voucher Not Posted Properly.");
                    } else {
                        String SQL = "SELECT REFUND_DATE FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+theDocNo+"' ";
                        String RefundDate = data.getStringValueFromDB(SQL,FinanceGlobal.FinURL);
                        ResultSet rsResult = data.getResult("SELECT * FROM D_FD_SALES_DEPOSIT_REFUND_DETAIL WHERE DOC_NO='"+theDocNo+"' ",FinanceGlobal.FinURL);
                        rsResult.first();
                        while(!rsResult.isAfterLast()) {
                            String ReceiptNo = rsResult.getString("RECEIPT_NO");
                            String PartyCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+theDocNo+"' ",FinanceGlobal.FinURL);
                            data.Execute("UPDATE D_FD_SALES_DEPOSIT_MASTER SET DEPOSIT_STATUS=1, REFUND_DATE='"+RefundDate+"' WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
                            data.Execute("UPDATE D_FD_SALES_DEPOSIT_MASTER_H SET DEPOSIT_STATUS=1, REFUND_DATE='"+RefundDate+"' WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
                            rsResult.next();
                        }
                    }
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
    
    public boolean PostPaymentVoucher(int CompanyID, String curDocNo) {
        try {
            String SelPrefix="";
            String SelSuffix="";
            int FFNo=0;
            
            // --- Start Common Elements --- //
            clsVoucher objVoucher=new clsVoucher();
            
            HashMap List = new HashMap();
            // --- End Common Elements --- //
            
            //--- VOUCHER POSTING FOR PAYMENT ---//
            
            //======== Gethering Requiered Data ========//
            int VoucherSrNo=0;
            String RefundDate = data.getStringValueFromDB("SELECT REFUND_DATE FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String DocDate = data.getStringValueFromDB("SELECT DOC_DATE FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+curDocNo+"'",FinanceGlobal.FinURL);
            String BankMainCode = data.getStringValueFromDB("SELECT BANK_MAIN_CODE FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String BookCode = data.getStringValueFromDB("SELECT BOOK_CODE FROM D_FIN_BOOK_MASTER WHERE MAIN_ACCOUNT_CODE='"+BankMainCode+"' ",FinanceGlobal.FinURL);
            String BankName = data.getStringValueFromDB("SELECT BOOK_NAME FROM D_FIN_BOOK_MASTER WHERE BOOK_CODE='"+BookCode+"' ",FinanceGlobal.FinURL);
            String SubAccountCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String MainAccountCode=data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FD_SALES_DEPOSIT_MASTER WHERE PARTY_CODE='"+SubAccountCode+"'",FinanceGlobal.FinURL);
            String InterestMainCode1=data.getStringValueFromDB("SELECT INTEREST_MAIN_CODE FROM D_FD_SALES_DEPOSIT_MASTER WHERE PARTY_CODE='"+SubAccountCode+"'",FinanceGlobal.FinURL).trim();
            String InterestMainCode2="";
            double pIntA = 0;
            double pIntN = 0;
            if(MainAccountCode.trim().equals("132642") || MainAccountCode.trim().equals("132666")) {
                if(InterestMainCode1.trim().equals("133155")){
                    InterestMainCode2 = "133203";
                } else {
                    InterestMainCode2 = "133155";
                }
                //String LastDate = EITLERPGLOBAL.getCurrentFinYear()+"-03-31";
                //pIntA = Math.abs(clsAccount.getClosingBalance(InterestMainCode1, SubAccountCode, RefundDate,true));
                //pIntN = Math.abs(clsAccount.getClosingBalance(InterestMainCode2, SubAccountCode, RefundDate,true));
                
                pIntA = Math.abs(clsAccount.getClosingBalance(InterestMainCode1, SubAccountCode, DocDate,true));
                pIntN = Math.abs(clsAccount.getClosingBalance(InterestMainCode2, SubAccountCode, DocDate,true));
                
            } else {
                pIntA = data.getDoubleValueFromDB("SELECT PREVIOUS_INTEREST_AMOUNT FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+curDocNo+"'",FinanceGlobal.FinURL);
            }
            boolean ExcludeInterest = data.getBoolValueFromDB("SELECT EXCLUDE_INTEREST FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String ChequeNo = data.getStringValueFromDB("SELECT CHEQUE_NO FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String ChequeDate = data.getStringValueFromDB("SELECT CHEQUE_DATE FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            //String ValueDate = data.getStringValueFromDB("SELECT VALUE_DATE FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            //String VoucherDate = data.getStringValueFromDB("SELECT VOUCHER_DATE FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            double totalAmount = 0;
            double pAmount = data.getDoubleValueFromDB("SELECT PRINCIPLE_AMOUNT FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+curDocNo+"'",FinanceGlobal.FinURL);
            double cInt = data.getDoubleValueFromDB("SELECT CURRENT_INTEREST_AMOUNT FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+curDocNo+"'",FinanceGlobal.FinURL);
            double TDSAmount = data.getDoubleValueFromDB("SELECT CURRENT_TDS_AMOUNT FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+curDocNo+"'",FinanceGlobal.FinURL);
            if(!ExcludeInterest) {
                totalAmount = EITLERPGLOBAL.round(pAmount+pIntA+pIntN+cInt, 2);
            } else {
                totalAmount = EITLERPGLOBAL.round(pAmount+cInt, 2);
            }
            
            String TDSAccountCode = "";
            List.clear();
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesInterest.ModuleID , "GET_ACCOUNT_CODE", "TDS_ACCOUNT_CODE", "");
            if(List.size()>0) {
                //Get TDS Account Code
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                TDSAccountCode = objRule.getAttribute("RULE_OUTCOME").getString();
            }
            
            /*if(TDSAmount > 0 ) {
                PostJournalVoucherTDS(CompanyID, curDocNo);
            }*/
            
            List.clear();
            setAttribute("FIN_HIERARCHY_ID",0);
            
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesDepositRefund.ModuleID , "CHOOSE_HIERARCHY", "DEFAULT", "1");
            
            if(List.size()>0) {
                // Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                int HierarchyID=UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                setAttribute("FIN_HIERARCHY_ID",HierarchyID);
            }
            
            ResultSet rsTmp=data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID="+clsVoucher.Payment2ModuleID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                SelPrefix=rsTmp.getString("PREFIX_CHARS");
                SelSuffix=rsTmp.getString("SUFFIX_CHARS");
                FFNo=rsTmp.getInt("FIRSTFREE_NO");
            }
            
            String InterestExpCode = "";
            List.clear();
            List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesInterest.ModuleID , "GET_ACCOUNT_CODE", "INTEREST_ON_CUSTOMERS_DEPOSIT", "");
            if(List.size()>0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                InterestExpCode=objRule.getAttribute("RULE_OUTCOME").getString().trim();
            }
            //======== End of Gethering Requiered Data ========//
            
            //=======Preparing Voucher Header ============//
            objVoucher=new clsVoucher();
            objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
            
            objVoucher.setAttribute("PREFIX",SelPrefix);
            objVoucher.setAttribute("SUFFIX",SelSuffix);
            objVoucher.setAttribute("FFNO",FFNo);
            objVoucher.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            objVoucher.setAttribute("VOUCHER_NO","");
            
            objVoucher.setAttribute("BOOK_CODE",BookCode); //24
            objVoucher.setAttribute("VOUCHER_TYPE",FinanceGlobal.TYPE_PAYMENT_2); // pyament
            objVoucher.setAttribute("CHEQUE_NO",ChequeNo); //
            objVoucher.setAttribute("CHEQUE_DATE",EITLERPGLOBAL.formatDate(ChequeDate));
            objVoucher.setAttribute("BANK_NAME",BankName);
            objVoucher.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.getCurrentDate());// interest calc date
            objVoucher.setAttribute("ST_CATEGORY","");
            objVoucher.setAttribute("MODULE_ID",0);
            objVoucher.setAttribute("REMARKS","DEPOSIT REFUNDED AND INTEREST PAID UP TO " + EITLERPGLOBAL.formatDate(RefundDate));
            objVoucher.setAttribute("LEGACY_NO","");
            objVoucher.setAttribute("LEGACY_DATE","0000-00-00");
            //objVoucher.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(VoucherDate));
            
            //setAttribute("FIN_HIERARCHY_ID",3726); //added by gaurang on 11/12/2018
            
            objVoucher.setAttribute("HIERARCHY_ID",getAttribute("FIN_HIERARCHY_ID").getInt());
            int FirstUserID=data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID="+getAttribute("FIN_HIERARCHY_ID").getInt()+" AND SR_NO=1");
            objVoucher.setAttribute("FROM",FirstUserID);
            objVoucher.setAttribute("TO",FirstUserID);
            objVoucher.setAttribute("FROM_REMARKS","");
            objVoucher.setAttribute("APPROVAL_STATUS","F"); //Hold Voucher
            
            objVoucher.setAttribute("APPROVAL_STATUS","H"); //Hold Voucher added by gaurang on 11/12/2018
            //=======End of  Voucher Header ============//
            
            // ====== Preparing Voucher Detail =======//
            objVoucher.colVoucherItems.clear();
            clsVoucherItem objVoucherItem=new clsVoucherItem();
            
            VoucherSrNo++;
            objVoucherItem=new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT","D");
            objVoucherItem.setAttribute("ACCOUNT_ID",1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode); // FOR CD INTEREST - 115160/115201
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",SubAccountCode);
            objVoucherItem.setAttribute("AMOUNT", pAmount);
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO",curDocNo); //ask
            objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(DocDate)); //ask
            objVoucherItem.setAttribute("MODULE_ID",clsSalesDepositRefund.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucherItem.setAttribute("IS_DEDUCTION",0);
            objVoucherItem.setAttribute("REF_SR_NO",0);
            objVoucherItem.setAttribute("LINK_NO","");
            
            
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            if(!ExcludeInterest) {
                if(pIntA > 0) {
                    VoucherSrNo++;
                    objVoucherItem=new clsVoucherItem();
                    objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                    objVoucherItem.setAttribute("EFFECT","D");
                    objVoucherItem.setAttribute("ACCOUNT_ID",1);
                    objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",InterestMainCode1); // FOR CD INTEREST - 115160/115201
                    objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",SubAccountCode);
                    objVoucherItem.setAttribute("AMOUNT", pIntA);
                    objVoucherItem.setAttribute("REMARKS","");
                    objVoucherItem.setAttribute("PO_NO","");
                    objVoucherItem.setAttribute("PO_DATE","0000-00-00");
                    objVoucherItem.setAttribute("REMARKS","");
                    objVoucherItem.setAttribute("INVOICE_NO","");
                    objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
                    objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
                    objVoucherItem.setAttribute("GRN_NO",curDocNo); //ask
                    objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(DocDate)); //ask
                    objVoucherItem.setAttribute("MODULE_ID",clsSalesDepositRefund.ModuleID);
                    objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
                    objVoucherItem.setAttribute("IS_DEDUCTION",0);
                    objVoucherItem.setAttribute("REF_SR_NO",0);
                    objVoucherItem.setAttribute("LINK_NO","");
                    objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
                }
                
                if(pIntN > 0) {
                    VoucherSrNo++;
                    objVoucherItem=new clsVoucherItem();
                    objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                    objVoucherItem.setAttribute("EFFECT","D");
                    objVoucherItem.setAttribute("ACCOUNT_ID",1);
                    objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",InterestMainCode2); // FOR CD INTEREST - 115160/115201
                    objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",SubAccountCode);
                    objVoucherItem.setAttribute("AMOUNT", pIntN);
                    objVoucherItem.setAttribute("REMARKS","");
                    objVoucherItem.setAttribute("PO_NO","");
                    objVoucherItem.setAttribute("PO_DATE","0000-00-00");
                    objVoucherItem.setAttribute("REMARKS","");
                    objVoucherItem.setAttribute("INVOICE_NO","");
                    objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
                    objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
                    objVoucherItem.setAttribute("GRN_NO",curDocNo);
                    objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(DocDate));
                    objVoucherItem.setAttribute("MODULE_ID",clsSalesDepositRefund.ModuleID);
                    objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
                    objVoucherItem.setAttribute("IS_DEDUCTION",0);
                    objVoucherItem.setAttribute("REF_SR_NO",0);
                    objVoucherItem.setAttribute("LINK_NO","");
                    objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
                }
            }
            
            VoucherSrNo++;
            int RefSrNo=VoucherSrNo;
            objVoucherItem=new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT","D");
            objVoucherItem.setAttribute("ACCOUNT_ID",1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",InterestExpCode); // INTEREST EXPENCE CODE ASK
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
            objVoucherItem.setAttribute("AMOUNT", cInt); //cInt GrossInt
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO",curDocNo);
            objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(DocDate));
            objVoucherItem.setAttribute("MODULE_ID",clsSalesDepositRefund.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucherItem.setAttribute("IS_DEDUCTION",0);
            objVoucherItem.setAttribute("REF_SR_NO",0);
            objVoucherItem.setAttribute("LINK_NO","");
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            VoucherSrNo++;
            objVoucherItem=new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT","C");
            objVoucherItem.setAttribute("ACCOUNT_ID",1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",BankMainCode); //Bank Main Account Code
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
            objVoucherItem.setAttribute("AMOUNT",totalAmount);
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO","");
            objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
            objVoucherItem.setAttribute("MODULE_ID",0);
            objVoucherItem.setAttribute("REF_COMPANY_ID",0);
            objVoucherItem.setAttribute("IS_DEDUCTION",0);
            objVoucherItem.setAttribute("REF_SR_NO",0);
            objVoucherItem.setAttribute("LINK_NO","");
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            if(TDSAmount > 0  && cInt>0) {
                
                VoucherSrNo++;
                objVoucherItem=new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                
                objVoucherItem.setAttribute("EFFECT","D");
                objVoucherItem.setAttribute("ACCOUNT_ID",1); //Default Value
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",InterestExpCode);
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
                objVoucherItem.setAttribute("AMOUNT",EITLERPGLOBAL.round(TDSAmount,0));
                objVoucherItem.setAttribute("APPLICABLE_AMOUNT",0);
                objVoucherItem.setAttribute("PERCENTAGE",0);
                objVoucherItem.setAttribute("REMARKS","");
                objVoucherItem.setAttribute("PO_NO","");
                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO","");
                objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
                objVoucherItem.setAttribute("GRN_NO",curDocNo);
                objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(DocDate));
                objVoucherItem.setAttribute("MODULE_ID",clsSalesDepositRefund.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                objVoucherItem.setAttribute("IS_DEDUCTION",1);
                objVoucherItem.setAttribute("REF_SR_NO",RefSrNo);
                objVoucherItem.setAttribute("LINK_NO","");
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
                
                
                VoucherSrNo++;
                objVoucherItem=new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                
                objVoucherItem.setAttribute("EFFECT","C");
                objVoucherItem.setAttribute("ACCOUNT_ID",1); //Default Value
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",TDSAccountCode);
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
                objVoucherItem.setAttribute("AMOUNT",EITLERPGLOBAL.round(TDSAmount,0));
                objVoucherItem.setAttribute("APPLICABLE_AMOUNT",0);
                objVoucherItem.setAttribute("PERCENTAGE",0);
                objVoucherItem.setAttribute("REMARKS","");
                objVoucherItem.setAttribute("PO_NO","");
                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO","");
                objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
                objVoucherItem.setAttribute("GRN_NO","");
                objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID",0);
                objVoucherItem.setAttribute("REF_COMPANY_ID",0);
                objVoucherItem.setAttribute("IS_DEDUCTION",1);
                objVoucherItem.setAttribute("REF_SR_NO",RefSrNo);
                objVoucherItem.setAttribute("LINK_NO","");
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            }
            
            // ====== End of Voucher Detail =======//
            
            //--- VOUCHER POSTING FOR BOOK CODE SECOND VOUCHER ---//
            
            if(objVoucher.Insert()) {
                return true;
            } else {
                return false;
            }
        } catch(Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }
    
    private boolean PostJournalVoucherTDS(int CompanyID, String curDocNo) {
        try {
            
            String SelPrefix="";
            String SelSuffix="";
            int FFNo=0;
            
            // --- Start Common Elements --- //
            clsVoucher objVoucher=new clsVoucher();
            
            setAttribute("FIN_HIERARCHY_ID",0);
            
            HashMap List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesDepositMaster.ModuleID , "CHOOSE_HIERARCHY", "DEFAULT", "2");
            
            if(List.size()>0) {
                // Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                int HierarchyID=UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                setAttribute("FIN_HIERARCHY_ID",HierarchyID);
            }
            ResultSet rsTmp=data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE PREFIX_CHARS='JV' AND MODULE_ID="+clsVoucher.JournalVoucherModuleID);
            //ResultSet rsTmp=data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID="+clsVoucher.JournalVoucherModuleID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                SelPrefix=rsTmp.getString("PREFIX_CHARS");
                SelSuffix=rsTmp.getString("SUFFIX_CHARS");
                FFNo=rsTmp.getInt("FIRSTFREE_NO");
            }
            // --- End Common Elements --- //
            
            //--- VOUCHER POSTING FOR PAYMENT ---//
            
            //======== Gethering Requiered Data ========//
            int VoucherSrNo=0;
            String DocDate = data.getStringValueFromDB("SELECT DOC_DATE FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+curDocNo+"'",FinanceGlobal.FinURL);
            String BookCode = "";
            List.clear();
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesDepositMaster.ModuleID , "GET_BOOK_CODE", "DEPOSIT_TRANSFER", ""); //ASK
            if(List.size()>0) {
                //Get Book Code 21
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                BookCode = objRule.getAttribute("RULE_OUTCOME").getString();
            }
            String BankName = data.getStringValueFromDB("SELECT BOOK_NAME FROM D_FIN_BOOK_MASTER WHERE BOOK_CODE='"+BookCode+"' ",FinanceGlobal.FinURL);
            
            String TDSAccountCode = "";
            List.clear();
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesInterest.ModuleID , "GET_ACCOUNT_CODE", "TDS_ACCOUNT_CODE", "");
            if(List.size()>0) {
                //Get TDS Account Code
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                TDSAccountCode = objRule.getAttribute("RULE_OUTCOME").getString();
            }
            
            String InterestExpCode = "";
            List.clear();
            List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesInterest.ModuleID , "GET_ACCOUNT_CODE", "INTEREST_ON_CUSTOMERS_DEPOSIT", "");
            if(List.size()>0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                InterestExpCode=objRule.getAttribute("RULE_OUTCOME").getString().trim();
            }
            
            
            String SubAccountCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String MainAccountCode=data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FD_SALES_DEPOSIT_MASTER WHERE PARTY_CODE='"+SubAccountCode+"'",FinanceGlobal.FinURL);
            
            String RefundDate = data.getStringValueFromDB("SELECT REFUND_DATE FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            double TDSAmount = data.getDoubleValueFromDB("SELECT CURRENT_TDS_AMOUNT FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+curDocNo+"'",FinanceGlobal.FinURL);
            //======== End of Gethering Requiered Data ========//
            
            //=======Preparing Voucher Header ============//
            objVoucher=new clsVoucher();
            objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
            
            objVoucher.setAttribute("PREFIX",SelPrefix);
            objVoucher.setAttribute("SUFFIX",SelSuffix);
            objVoucher.setAttribute("FFNO",FFNo);
            objVoucher.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            objVoucher.setAttribute("VOUCHER_NO","");
            
            objVoucher.setAttribute("BOOK_CODE",BookCode); //24
            objVoucher.setAttribute("VOUCHER_TYPE",FinanceGlobal.TYPE_JOURNAL); // pyament
            objVoucher.setAttribute("CHEQUE_NO",""); //
            objVoucher.setAttribute("CHEQUE_DATE","0000-00-00");
            objVoucher.setAttribute("BANK_NAME",BankName);
            objVoucher.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(RefundDate));// interest calc date
            objVoucher.setAttribute("ST_CATEGORY","");
            objVoucher.setAttribute("MODULE_ID",0);
            objVoucher.setAttribute("REMARKS","");
            
            objVoucher.setAttribute("HIERARCHY_ID",getAttribute("FIN_HIERARCHY_ID").getInt());
            int FirstUserID=data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID="+getAttribute("FIN_HIERARCHY_ID").getInt()+" AND SR_NO=1");
            objVoucher.setAttribute("FROM",FirstUserID);
            objVoucher.setAttribute("TO",FirstUserID);
            objVoucher.setAttribute("FROM_REMARKS","");
            objVoucher.setAttribute("APPROVAL_STATUS","F"); //Hold Voucher
            //=======End of  Voucher Header ============//
            
            // ====== Preparing Voucher Detail =======//
            objVoucher.colVoucherItems.clear();
            clsVoucherItem objVoucherItem=new clsVoucherItem();
            
            VoucherSrNo++;
            objVoucherItem=new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT","D");
            objVoucherItem.setAttribute("ACCOUNT_ID",1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",InterestExpCode); // INTEREST EXPENCE CODE ASK
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
            objVoucherItem.setAttribute("AMOUNT", TDSAmount); //cInt GrossInt
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO",curDocNo);
            objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(DocDate));
            objVoucherItem.setAttribute("MODULE_ID",clsSalesDepositRefund.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            VoucherSrNo++;
            objVoucherItem=new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT","C");
            objVoucherItem.setAttribute("ACCOUNT_ID",1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",TDSAccountCode);
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
            objVoucherItem.setAttribute("AMOUNT",TDSAmount);
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO",curDocNo); //ask
            objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(DocDate)); //ask
            objVoucherItem.setAttribute("MODULE_ID",clsSalesDepositRefund.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            // ====== End of Voucher Detail =======//
            
            //--- VOUCHER POSTING FOR BOOK CODE SECOND VOUCHER ---//
            
            if(objVoucher.Insert()) {
                return true;
            } else {
                return false;
            }
        } catch(Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }
    
    private boolean PostJournalVoucher(int CompanyID, String curDocNo) {
        try {            
            String SelPrefix="";
            String SelSuffix="";
            int FFNo=0;
            
            // --- Start Common Elements --- //
            clsVoucher objVoucher=new clsVoucher();
            
            setAttribute("FIN_HIERARCHY_ID",0);
            
            HashMap List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesDepositRefund.ModuleID , "CHOOSE_HIERARCHY", "DEFAULT", "2");
            
            if(List.size()>0) {
                // Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                int HierarchyID=UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                setAttribute("FIN_HIERARCHY_ID",HierarchyID);
            }
            ResultSet rsTmp=data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE PREFIX_CHARS='JV' AND MODULE_ID="+clsVoucher.JournalVoucherModuleID);
            //ResultSet rsTmp=data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE  MODULE_ID="+clsVoucher.JournalVoucherModuleID);            
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                SelPrefix=rsTmp.getString("PREFIX_CHARS");
                SelSuffix=rsTmp.getString("SUFFIX_CHARS");
                FFNo=rsTmp.getInt("FIRSTFREE_NO");
            }
            // --- End Common Elements --- //
            
            //--- VOUCHER POSTING FOR PAYMENT ---//
            
            //======== Gethering Requiered Data ========//
            int VoucherSrNo=0;
            String BookCode = "";
            List.clear();
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesDepositMaster.ModuleID , "GET_BOOK_CODE", "DEPOSIT_TRANSFER", ""); //ASK
            if(List.size()>0) {
                //Get Book Code 21
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                BookCode = objRule.getAttribute("RULE_OUTCOME").getString();
            }
            String DocDate = data.getStringValueFromDB("SELECT DOC_DATE FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String RefundDate = data.getStringValueFromDB("SELECT REFUND_DATE FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String FundTransferFrom = data.getStringValueFromDB("SELECT FUND_TRANSFER FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String BankName = data.getStringValueFromDB("SELECT BOOK_NAME FROM D_FIN_BOOK_MASTER WHERE BOOK_CODE='"+BookCode+"' ",FinanceGlobal.FinURL);
            String SubAccountCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String MainAccountCode=data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FD_SALES_DEPOSIT_MASTER WHERE PARTY_CODE='"+SubAccountCode+"' ",FinanceGlobal.FinURL);
            String InterestMainCode1 = data.getStringValueFromDB("SELECT INTEREST_MAIN_CODE FROM D_FD_SALES_DEPOSIT_MASTER WHERE PARTY_CODE='"+SubAccountCode+"' ",FinanceGlobal.FinURL);
            String InterestMainCode2="";
            double pIntA = 0;
            double pIntN = 0;
            if(MainAccountCode.trim().equals("132642") || MainAccountCode.trim().equals("132666")) {
                if(InterestMainCode1.trim().equals("133155")){
                    InterestMainCode2 = "133203";
                } else {
                    InterestMainCode2 = "133155";
                }
                //pIntA = Math.abs(clsAccount.getClosingBalance(InterestMainCode1, SubAccountCode, RefundDate,true));
                //pIntN = Math.abs(clsAccount.getClosingBalance(InterestMainCode2, SubAccountCode, RefundDate,true));
                
                pIntA = Math.abs(clsAccount.getClosingBalance(InterestMainCode1, SubAccountCode, DocDate,true));
                pIntN = Math.abs(clsAccount.getClosingBalance(InterestMainCode2, SubAccountCode, DocDate,true));
                
            } else {
                pIntA = data.getDoubleValueFromDB("SELECT PREVIOUS_INTEREST_AMOUNT FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+curDocNo+"'",FinanceGlobal.FinURL);
            }
            boolean ExcludeInterest = data.getBoolValueFromDB("SELECT EXCLUDE_INTEREST FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String ChequeNo = data.getStringValueFromDB("SELECT CHEQUE_NO FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String ChequeDate = data.getStringValueFromDB("SELECT CHEQUE_DATE FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String ValueDate = data.getStringValueFromDB("SELECT VALUE_DATE FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String VoucherDate = data.getStringValueFromDB("SELECT VOUCHER_DATE FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            double totalAmount = 0;
            double pAmount = data.getDoubleValueFromDB("SELECT PRINCIPLE_AMOUNT FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+curDocNo+"'",FinanceGlobal.FinURL);
            double cInt = data.getDoubleValueFromDB("SELECT CURRENT_INTEREST_AMOUNT FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+curDocNo+"'",FinanceGlobal.FinURL);
            double TDSAmount = data.getDoubleValueFromDB("SELECT CURRENT_TDS_AMOUNT FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+curDocNo+"'",FinanceGlobal.FinURL);
            if(!ExcludeInterest) {
                totalAmount = EITLERPGLOBAL.round(pAmount+pIntA+pIntN+cInt, 2);
            } else {
                totalAmount = EITLERPGLOBAL.round(pAmount+cInt, 2);
            }
            String TDSAccountCode = "";
            List.clear();
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesInterest.ModuleID , "GET_ACCOUNT_CODE", "TDS_ACCOUNT_CODE", "");
            if(List.size()>0) {
                //Get TDS Account Code
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                TDSAccountCode = objRule.getAttribute("RULE_OUTCOME").getString();
            }
            
            String InterestExpCode = "";
            List.clear();
            List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesInterest.ModuleID , "GET_ACCOUNT_CODE", "INTEREST_ON_CUSTOMERS_DEPOSIT", "");
            if(List.size()>0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                InterestExpCode=objRule.getAttribute("RULE_OUTCOME").getString().trim();
            }
            //======== End of Gethering Requiered Data ========//
            
            //=======Preparing Voucher Header ============//
            objVoucher=new clsVoucher();
            objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
            objVoucher.setAttribute("PREFIX",SelPrefix);
            objVoucher.setAttribute("SUFFIX",SelSuffix);
            objVoucher.setAttribute("FFNO",FFNo);
            objVoucher.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            objVoucher.setAttribute("VOUCHER_NO","");
            objVoucher.setAttribute("BOOK_CODE",BookCode);
            objVoucher.setAttribute("VOUCHER_TYPE",FinanceGlobal.TYPE_JOURNAL);
            objVoucher.setAttribute("CHEQUE_NO",ChequeNo);
            objVoucher.setAttribute("CHEQUE_DATE",EITLERPGLOBAL.formatDate(ChequeDate));
            objVoucher.setAttribute("BANK_NAME",BankName);
            //objVoucher.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(RefundDate));
            objVoucher.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(VoucherDate));
            objVoucher.setAttribute("ST_CATEGORY","");
            objVoucher.setAttribute("MODULE_ID",0);
            objVoucher.setAttribute("REMARKS","DEPOSIT TRANSFERED AND INTEREST PAID UP TO " + EITLERPGLOBAL.formatDate(RefundDate));
            objVoucher.setAttribute("LEGACY_NO","");
            objVoucher.setAttribute("LEGACY_DATE","0000-00-00");
            
            objVoucher.setAttribute("EXCLUDE_IN_ADJ",1);
            
            //setAttribute("FIN_HIERARCHY_ID",3725); //added by gaurang on 11/12/2018
            
            objVoucher.setAttribute("HIERARCHY_ID",getAttribute("FIN_HIERARCHY_ID").getInt());
            int FirstUserID=data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID="+getAttribute("FIN_HIERARCHY_ID").getInt()+" AND SR_NO=1");
            objVoucher.setAttribute("FROM",FirstUserID);
            objVoucher.setAttribute("TO",FirstUserID);
            objVoucher.setAttribute("FROM_REMARKS","");
            objVoucher.setAttribute("APPROVAL_STATUS","F");
                        
            objVoucher.setAttribute("APPROVAL_STATUS","H"); //added by gaurang on 11/12/2018
            //=======End of  Voucher Header ============//
            
            // ====== Preparing Voucher Detail =======//
            objVoucher.colVoucherItems.clear();
            clsVoucherItem objVoucherItem=new clsVoucherItem();
            
            VoucherSrNo++;
            objVoucherItem=new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT","D");
            objVoucherItem.setAttribute("ACCOUNT_ID",1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode);
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",SubAccountCode);
            objVoucherItem.setAttribute("AMOUNT", pAmount);
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO",curDocNo);
            objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(DocDate));
            objVoucherItem.setAttribute("MODULE_ID",clsSalesDepositRefund.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucherItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(ValueDate));
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            if(!ExcludeInterest) {
                if(pIntA > 0 ) {
                    VoucherSrNo++;
                    objVoucherItem=new clsVoucherItem();
                    objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                    objVoucherItem.setAttribute("EFFECT","D");
                    objVoucherItem.setAttribute("ACCOUNT_ID",1);
                    objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",InterestMainCode1); // FOR CD INTEREST - 115160/115201
                    objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",SubAccountCode);
                    objVoucherItem.setAttribute("AMOUNT", pIntA);
                    objVoucherItem.setAttribute("REMARKS","");
                    objVoucherItem.setAttribute("PO_NO","");
                    objVoucherItem.setAttribute("PO_DATE","0000-00-00");
                    objVoucherItem.setAttribute("REMARKS","");
                    objVoucherItem.setAttribute("INVOICE_NO","");
                    objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
                    objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
                    objVoucherItem.setAttribute("GRN_NO",curDocNo);
                    objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(DocDate));
                    objVoucherItem.setAttribute("MODULE_ID",clsSalesDepositRefund.ModuleID);
                    objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
                    objVoucherItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(ValueDate));
                    objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
                }
                
                if(pIntN > 0 ) {
                    VoucherSrNo++;
                    objVoucherItem=new clsVoucherItem();
                    objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                    objVoucherItem.setAttribute("EFFECT","D");
                    objVoucherItem.setAttribute("ACCOUNT_ID",1);
                    objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",InterestMainCode2);
                    objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",SubAccountCode);
                    objVoucherItem.setAttribute("AMOUNT", pIntN);
                    objVoucherItem.setAttribute("REMARKS","");
                    objVoucherItem.setAttribute("PO_NO","");
                    objVoucherItem.setAttribute("PO_DATE","0000-00-00");
                    objVoucherItem.setAttribute("REMARKS","");
                    objVoucherItem.setAttribute("INVOICE_NO","");
                    objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
                    objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
                    objVoucherItem.setAttribute("GRN_NO",curDocNo);
                    objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(DocDate));
                    objVoucherItem.setAttribute("MODULE_ID",clsSalesDepositRefund.ModuleID);
                    objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
                    objVoucherItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(ValueDate));
                    objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
                }
            }
            
            VoucherSrNo++;
            objVoucherItem=new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT","D");
            objVoucherItem.setAttribute("ACCOUNT_ID",1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",InterestExpCode); // INTEREST EXPENCE CODE ASK
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
            objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(cInt+TDSAmount,2));
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO",curDocNo);
            objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(DocDate));
            objVoucherItem.setAttribute("MODULE_ID",clsSalesDepositRefund.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucherItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(ValueDate));
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            VoucherSrNo++;
            objVoucherItem=new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT","C");
            objVoucherItem.setAttribute("ACCOUNT_ID",1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",FundTransferFrom);
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",SubAccountCode);
            objVoucherItem.setAttribute("AMOUNT",totalAmount);
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO","");
            objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
            objVoucherItem.setAttribute("MODULE_ID",0);
            objVoucherItem.setAttribute("REF_COMPANY_ID",0);
            objVoucherItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(ValueDate));
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            if(TDSAmount > 0 ) {
                VoucherSrNo++;
                objVoucherItem=new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT","C");
                objVoucherItem.setAttribute("ACCOUNT_ID",1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",TDSAccountCode);
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
                objVoucherItem.setAttribute("AMOUNT",TDSAmount);
                objVoucherItem.setAttribute("PO_NO","");
                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
                objVoucherItem.setAttribute("REMARKS","");
                objVoucherItem.setAttribute("INVOICE_NO","");
                objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
                objVoucherItem.setAttribute("GRN_NO","");
                objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID",0);
                objVoucherItem.setAttribute("REF_COMPANY_ID",0);
                objVoucherItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(ValueDate));
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            }
            // ====== End of Voucher Detail =======//
            
            //--- VOUCHER POSTING FOR BOOK CODE SECOND VOUCHER ---//
            
            if(objVoucher.Insert()) {
                return true;
            } else {
                return false;
            }
        } catch(Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }
    
    public boolean Update() {
        
        Statement stHistory,stDetail,stHDetail,stOut,stHOut; //,stTmp,stTmpH,stTmpOld
        ResultSet rsHistory,rsDetail,rsHDetail,rsOut,rsHOut; //,rsTmp,rsTmpH,rsTmpOld;
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
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FD_SALES_DEPOSIT_REFUND_HEADER_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //** --------------------------------**//
            
            //========= Updating Into Deposit Refund =================//
            
            rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsResultSet.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            theDocNo = getAttribute("DOC_NO").getString();
            rsResultSet.updateString("DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsResultSet.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsResultSet.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            rsResultSet.updateString("BANK_MAIN_CODE",getAttribute("BANK_MAIN_CODE").getString());
            rsResultSet.updateString("BANK_NAME",getAttribute("BANK_NAME").getString());
            rsResultSet.updateString("FUND_TRANSFER",getAttribute("FUND_TRANSFER").getString());
            rsResultSet.updateDouble("PRINCIPLE_AMOUNT",getAttribute("PRINCIPLE_AMOUNT").getDouble());
            rsResultSet.updateDouble("TOTAL_PRINCIPLE_AMOUNT",getAttribute("TOTAL_PRINCIPLE_AMOUNT").getDouble());
            rsResultSet.updateDouble("PREVIOUS_INTEREST_AMOUNT",getAttribute("PREVIOUS_INTEREST_AMOUNT").getDouble());
            rsResultSet.updateBoolean("EXCLUDE_INTEREST",getAttribute("EXCLUDE_INTEREST").getBool());
            rsResultSet.updateDouble("CURRENT_INTEREST_AMOUNT",getAttribute("CURRENT_INTEREST_AMOUNT").getDouble());
            rsResultSet.updateDouble("CURRENT_TDS_AMOUNT",getAttribute("CURRENT_TDS_AMOUNT").getDouble());
            rsResultSet.updateString("REFUND_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("REFUND_DATE").getString()));
            rsResultSet.updateString("CHEQUE_NO",getAttribute("CHEQUE_NO").getString());
            rsResultSet.updateString("CHEQUE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CHEQUE_DATE").getString()));
            rsResultSet.updateBoolean("DAYWISE_INTEREST",getAttribute("DAYWISE_INTEREST").getBool()); // new code by vivek on 28/02/2014 for day wise interest calculation
            rsResultSet.updateString("VALUE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("VALUE_DATE").getString()));//new code by ashutosh on 12/08/2015 for insert value date
            rsResultSet.updateString("VOUCHER_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("VOUCHER_DATE").getString()));//new code by ashutosh on 12/08/2015 for insert voucher date
            
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
            rsResultSet.updateString("REJECTED_REMARKS","");
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateRow();
            
            //------------------------------------------------------//
            
            //========= Inserting Into History =================//
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM D_FD_SALES_DEPOSIT_REFUND_HEADER_H WHERE DOC_NO='"+theDocNo+"'",FinanceGlobal.FinURL);
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
            rsHistory.updateString("BANK_MAIN_CODE",getAttribute("BANK_MAIN_CODE").getString());
            rsHistory.updateString("BANK_NAME",getAttribute("BANK_NAME").getString());
            rsHistory.updateString("FUND_TRANSFER",getAttribute("FUND_TRANSFER").getString());
            rsHistory.updateDouble("PRINCIPLE_AMOUNT",getAttribute("PRINCIPLE_AMOUNT").getDouble());
            rsHistory.updateDouble("TOTAL_PRINCIPLE_AMOUNT",getAttribute("TOTAL_PRINCIPLE_AMOUNT").getDouble());
            rsHistory.updateDouble("PREVIOUS_INTEREST_AMOUNT",getAttribute("PREVIOUS_INTEREST_AMOUNT").getDouble());
            rsHistory.updateBoolean("EXCLUDE_INTEREST",getAttribute("EXCLUDE_INTEREST").getBool());
            rsHistory.updateDouble("CURRENT_INTEREST_AMOUNT",getAttribute("CURRENT_INTEREST_AMOUNT").getDouble());
            rsHistory.updateDouble("CURRENT_TDS_AMOUNT",getAttribute("CURRENT_TDS_AMOUNT").getDouble());
            rsHistory.updateString("REFUND_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("REFUND_DATE").getString()));
            rsHistory.updateString("CHEQUE_NO",getAttribute("CHEQUE_NO").getString());
            rsHistory.updateString("CHEQUE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CHEQUE_DATE").getString()));
            rsResultSet.updateBoolean("DAYWISE_INTEREST",getAttribute("DAYWISE_INTEREST").getBool()); //new code by vivek on 28/02/2014 for day wise interest calculation 
            rsHistory.updateString("VALUE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("VALUE_DATE").getString()));//new code by ashutosh on 12/08/2015 for insert value date
            rsHistory.updateString("VOUCHER_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("VOUCHER_DATE").getString()));//new code by ashutosh on 12/08/2015 for insert voucher date
            
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
            rsHistory.updateString("REJECTED_REMARKS","");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            //-------------------------------------------------//
            
            //==========Inserting into Detail==================//
            String Del_Query = "DELETE FROM FINANCE.D_FD_SALES_DEPOSIT_REFUND_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DOC_NO='"+theDocNo.trim()+"'";
            data.Execute(Del_Query,FinanceGlobal.FinURL);
            
            stDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDetail=stDetail.executeQuery("SELECT * FROM D_FD_SALES_DEPOSIT_REFUND_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DOC_NO='1'");
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FD_SALES_DEPOSIT_REFUND_DETAIL_H WHERE DOC_NO='1'");
            rsDetail.first();
            rsHDetail.first();
            for(int i=1;i<=colLineItems.size();i++) {
                clsSalesDepositRefundItem ObjItem=(clsSalesDepositRefundItem) colLineItems.get(Integer.toString(i));
                rsDetail.moveToInsertRow();
                rsDetail.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsDetail.updateString("DOC_NO",theDocNo);
                rsDetail.updateLong("SR_NO",i);
                rsDetail.updateString("RECEIPT_NO",ObjItem.getAttribute("RECEIPT_NO").getString());
                rsDetail.updateString("RECEIPT_DATE",EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("RECEIPT_DATE").getString()));
                rsDetail.updateString("EFFECTIVE_DATE",EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("EFFECTIVE_DATE").getString()));
                rsDetail.updateString("MAIN_ACCOUNT_CODE", ObjItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsDetail.updateString("INTEREST_MAIN_CODE",ObjItem.getAttribute("INTEREST_MAIN_CODE").getString());
                rsDetail.updateDouble("INTEREST_RATE",ObjItem.getAttribute("INTEREST_RATE").getDouble());
                rsDetail.updateDouble("AMOUNT",ObjItem.getAttribute("AMOUNT").getDouble());
                rsDetail.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
                rsDetail.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsDetail.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
                rsDetail.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsDetail.updateBoolean("CHANGED",true);
                rsDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsDetail.updateBoolean("CANCELLED",false);
                rsDetail.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateString("DOC_NO",theDocNo);
                rsHDetail.updateLong("SR_NO",i);
                rsHDetail.updateString("RECEIPT_NO", ObjItem.getAttribute("RECEIPT_NO").getString());
                rsHDetail.updateString("RECEIPT_DATE", EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("RECEIPT_DATE").getString()));
                rsHDetail.updateString("EFFECTIVE_DATE", EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("EFFECTIVE_DATE").getString()));
                rsHDetail.updateString("MAIN_ACCOUNT_CODE",ObjItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsHDetail.updateString("INTEREST_MAIN_CODE",ObjItem.getAttribute("INTEREST_MAIN_CODE").getString());
                rsHDetail.updateDouble("INTEREST_RATE",ObjItem.getAttribute("INTEREST_RATE").getDouble());
                rsHDetail.updateDouble("AMOUNT",ObjItem.getAttribute("AMOUNT").getDouble());
                rsHDetail.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
                rsHDetail.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
                rsHDetail.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CANCELLED",false);
                rsHDetail.insertRow();
            }
            
            //-------------------------------------------------//
            
            //============= Inserting Into OutStanding Detail =====================//
            Del_Query = "DELETE FROM FINANCE.D_FD_DEPOSIT_OUTSTANDING WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DOC_NO='"+theDocNo.trim()+"'";
            data.Execute(Del_Query,FinanceGlobal.FinURL);
            
            stOut=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsOut=stOut.executeQuery("SELECT * FROM D_FD_DEPOSIT_OUTSTANDING WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DOC_NO='1'");
            stHOut=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHOut=stHOut.executeQuery("SELECT * FROM D_FD_DEPOSIT_OUTSTANDING_H WHERE DOC_NO='1'");
            rsOut.first();
            rsHOut.first();
            
            for(int i=1;i<=colLineItemsOut.size();i++) {
                clsOutStandingItem ObjItemOut=(clsOutStandingItem) colLineItemsOut.get(Integer.toString(i));
                rsOut.moveToInsertRow();
                rsOut.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsOut.updateString("DOC_NO",theDocNo);
                rsOut.updateLong("SR_NO",i);
                rsOut.updateString("MAIN_ACCOUNT_CODE", ObjItemOut.getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsOut.updateDouble("AMOUNT",ObjItemOut.getAttribute("AMOUNT").getDouble());
                rsOut.updateString("EFFECT",ObjItemOut.getAttribute("EFFECT").getString());
                rsOut.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
                rsOut.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsOut.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
                rsOut.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsOut.updateBoolean("CHANGED",true);
                rsOut.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsOut.updateBoolean("CANCELLED",false);
                rsOut.insertRow();
                
                rsHOut.moveToInsertRow();
                rsHOut.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsHOut.updateInt("REVISION_NO",RevNo);
                rsHOut.updateString("DOC_NO",theDocNo);
                rsHOut.updateLong("SR_NO",i);
                rsHOut.updateString("MAIN_ACCOUNT_CODE",ObjItemOut.getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsHOut.updateDouble("AMOUNT",ObjItemOut.getAttribute("AMOUNT").getDouble());
                rsHOut.updateString("EFFECT", ObjItemOut.getAttribute("EFFECT").getString());
                rsHOut.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
                rsHOut.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHOut.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
                rsHOut.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHOut.updateBoolean("CHANGED",true);
                rsHOut.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHOut.updateBoolean("CANCELLED",false);
                rsHOut.insertRow();
            }
            //---------------------------------------------------------------------//
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=EITLERPGLOBAL.gCompanyID;
            ObjFlow.ModuleID=ModuleID;
            ObjFlow.DocNo=getAttribute("DOC_NO").getObj().toString();
            ObjFlow.From=getAttribute("FROM").getInt();
            ObjFlow.To=getAttribute("TO").getInt();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_FD_SALES_DEPOSIT_REFUND_HEADER";
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
                data.Execute("UPDATE D_FD_SALES_DEPOSIT_REFUND_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO="+theDocNo+"'",FinanceGlobal.FinURL);
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
                int CompanyID = EITLERPGLOBAL.gCompanyID;
                String BankMainCode = data.getStringValueFromDB("SELECT BANK_MAIN_CODE FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+theDocNo+"' ",FinanceGlobal.FinURL);
                String FundTransferFrom = data.getStringValueFromDB("SELECT FUND_TRANSFER FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+theDocNo+"' ",FinanceGlobal.FinURL);
                if(!BankMainCode.equals("") && FundTransferFrom.equals("")) {
                    if(!PostPaymentVoucher(CompanyID, theDocNo)) {
                        JOptionPane.showMessageDialog(null,"Payment Voucher Not Posted Properly.");
                    } else {
                        String SQL = "SELECT REFUND_DATE FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+theDocNo+"' ";
                        String RefundDate = data.getStringValueFromDB(SQL,FinanceGlobal.FinURL);
                        ResultSet rsResult = data.getResult("SELECT * FROM D_FD_SALES_DEPOSIT_REFUND_DETAIL WHERE DOC_NO='"+theDocNo+"' ",FinanceGlobal.FinURL);
                        rsResult.first();
                        while(!rsResult.isAfterLast()) {
                            String ReceiptNo = rsResult.getString("RECEIPT_NO");
                            String PartyCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+theDocNo+"' ",FinanceGlobal.FinURL);
                            data.Execute("UPDATE D_FD_SALES_DEPOSIT_MASTER SET DEPOSIT_STATUS=1, REFUND_DATE='"+RefundDate+"' WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
                            data.Execute("UPDATE D_FD_SALES_DEPOSIT_MASTER_H SET DEPOSIT_STATUS=1, REFUND_DATE='"+RefundDate+"' WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
                            rsResult.next();
                        }
                    }
                } else if (BankMainCode.equals("") && !FundTransferFrom.equals("")){
                    if(!PostJournalVoucher(CompanyID, theDocNo)) {
                        JOptionPane.showMessageDialog(null,"Journal Voucher Not Posted Properly.");
                    } else {
                        String SQL = "SELECT REFUND_DATE FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+theDocNo+"' ";
                        String RefundDate = data.getStringValueFromDB(SQL,FinanceGlobal.FinURL);
                        ResultSet rsResult = data.getResult("SELECT * FROM D_FD_SALES_DEPOSIT_REFUND_DETAIL WHERE DOC_NO='"+theDocNo+"' ",FinanceGlobal.FinURL);
                        rsResult.first();
                        while(!rsResult.isAfterLast()) {
                            String ReceiptNo = rsResult.getString("RECEIPT_NO");
                            String PartyCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+theDocNo+"' ",FinanceGlobal.FinURL);
                            data.Execute("UPDATE D_FD_SALES_DEPOSIT_MASTER SET DEPOSIT_STATUS=1, REFUND_DATE='"+RefundDate+"' WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
                            data.Execute("UPDATE D_FD_SALES_DEPOSIT_MASTER_H SET DEPOSIT_STATUS=1, REFUND_DATE='"+RefundDate+"' WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
                            rsResult.next();
                        }
                    }
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+DocNo+"' AND (APPROVED=1)";
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
            strSQL="SELECT DOC_NO FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+DocNo+"' AND (APPROVED=1)";
            
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
                String strSQL = "DELETE FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+DocNo+"' ";
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
        clsSalesDepositRefund objSalesDepositRefund = new clsSalesDepositRefund();
        objSalesDepositRefund.Filter(strCondition,CompanyID);
        return objSalesDepositRefund;
    }
    
    public boolean Filter(String Condition,int CompanyID) {
        Ready=false;
        
        try {
            String strSQL = "SELECT * FROM D_FD_SALES_DEPOSIT_REFUND_HEADER " + Condition ;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSQL);
            
            if(!rsResultSet.first()) {
                strSQL = "SELECT * FROM D_FD_SALES_DEPOSIT_REFUND_HEADER ORDER BY DOC_NO";
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
        ResultSet rsDetail,rsOut;
        Connection ConnDetail,ConnOut;
        Statement StmtDetail,StmtOut;
        
        ConnDetail=data.getConn(FinanceGlobal.FinURL);
        ConnOut=data.getConn(FinanceGlobal.FinURL);
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
            setAttribute("BANK_MAIN_CODE",UtilFunctions.getString(rsResultSet,"BANK_MAIN_CODE",""));
            setAttribute("BANK_NAME",UtilFunctions.getString(rsResultSet,"BANK_NAME",""));
            setAttribute("FUND_TRANSFER",UtilFunctions.getString(rsResultSet,"FUND_TRANSFER",""));
            setAttribute("PRINCIPLE_AMOUNT",UtilFunctions.getDouble(rsResultSet,"PRINCIPLE_AMOUNT",0.0));
            setAttribute("TOTAL_PRINCIPLE_AMOUNT",UtilFunctions.getDouble(rsResultSet,"TOTAL_PRINCIPLE_AMOUNT",0.0));
            setAttribute("PREVIOUS_INTEREST_AMOUNT",UtilFunctions.getDouble(rsResultSet,"PREVIOUS_INTEREST_AMOUNT",0.0));
            setAttribute("EXCLUDE_INTEREST",UtilFunctions.getBoolean(rsResultSet,"EXCLUDE_INTEREST",false));
            setAttribute("CURRENT_INTEREST_AMOUNT",UtilFunctions.getDouble(rsResultSet,"CURRENT_INTEREST_AMOUNT",0.0));
            setAttribute("CURRENT_TDS_AMOUNT",UtilFunctions.getDouble(rsResultSet,"CURRENT_TDS_AMOUNT",0.0));
            setAttribute("REFUND_DATE",UtilFunctions.getString(rsResultSet,"REFUND_DATE","0000-00-00"));
            setAttribute("CHEQUE_NO",UtilFunctions.getString(rsResultSet,"CHEQUE_NO",""));
            setAttribute("CHEQUE_DATE",UtilFunctions.getString(rsResultSet,"CHEQUE_DATE","0000-00-00"));
            setAttribute("DAYWISE_INTEREST",UtilFunctions.getBoolean(rsResultSet,"DAYWISE_INTEREST",false)); //new code by vivek on 28/02/2014 for day wise interest calculation 
            setAttribute("VALUE_DATE",UtilFunctions.getString(rsResultSet,"VALUE_DATE","0000-00-00"));//new code by ashutosh on 12/08/2015 for insert value date
            setAttribute("VOUCHER_DATE",UtilFunctions.getString(rsResultSet,"VOUCHER_DATE","0000-00-00"));//new code by ashutosh on 12/08/2015 for insert voucher date
            
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
            
            colLineItems.clear();
            
            long mCompanyID= getAttribute("COMPANY_ID").getLong();
            String mDocno= getAttribute("DOC_NO").getString();
            StmtDetail=ConnDetail.createStatement();
            String SQL= "";
            if(HistoryView) {
                SQL = "SELECT * FROM D_FD_SALES_DEPOSIT_REFUND_DETAIL_H WHERE COMPANY_ID="+mCompanyID+" AND DOC_NO='"+mDocno.trim()+"' AND REVISION_NO="+RevNo+" ORDER BY SR_NO ";
                rsDetail=StmtDetail.executeQuery(SQL);
            } else {
                SQL = "SELECT * FROM D_FD_SALES_DEPOSIT_REFUND_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND DOC_NO='"+mDocno.trim()+"' ORDER BY SR_NO";
                rsDetail=StmtDetail.executeQuery(SQL);
            }
            rsDetail.first();
            while(!rsDetail.isAfterLast()) {
                Counter=Counter+1;
                clsSalesDepositRefundItem ObjItem=new clsSalesDepositRefundItem();
                
                ObjItem.setAttribute("COMPANY_ID",rsDetail.getLong("COMPANY_ID"));
                ObjItem.setAttribute("DOC_NO",rsDetail.getString("DOC_NO"));
                ObjItem.setAttribute("SR_NO",rsDetail.getLong("SR_NO"));
                ObjItem.setAttribute("RECEIPT_NO",rsDetail.getString("RECEIPT_NO"));
                ObjItem.setAttribute("RECEIPT_DATE",rsDetail.getString("RECEIPT_DATE"));
                ObjItem.setAttribute("EFFECTIVE_DATE",rsDetail.getString("EFFECTIVE_DATE"));
                ObjItem.setAttribute("MAIN_ACCOUNT_CODE",rsDetail.getString("MAIN_ACCOUNT_CODE"));
                ObjItem.setAttribute("INTEREST_MAIN_CODE",rsDetail.getString("INTEREST_MAIN_CODE"));
                ObjItem.setAttribute("INTEREST_RATE",rsDetail.getDouble("INTEREST_RATE"));
                ObjItem.setAttribute("AMOUNT",rsDetail.getDouble("AMOUNT"));
                ObjItem.setAttribute("CREATED_BY",rsDetail.getString("CREATED_BY"));
                ObjItem.setAttribute("CREATED_DATE",rsDetail.getString("CREATED_DATE"));
                ObjItem.setAttribute("MODIFIED_BY",rsDetail.getString("MODIFIED_BY"));
                ObjItem.setAttribute("MODIFIED_DATE",rsDetail.getString("MODIFIED_DATE"));
                ObjItem.setAttribute("CHANGED",rsDetail.getInt("CHANGED"));
                ObjItem.setAttribute("CHANGED_DATE",rsDetail.getString("CHANGED_DATE"));
                ObjItem.setAttribute("CANCELLED",rsDetail.getInt("CANCELLED"));
                
                colLineItems.put(Long.toString(Counter),ObjItem);
                rsDetail.next();
            }
            
            StmtOut=ConnOut.createStatement();
            SQL="";
            if(HistoryView) {
                SQL = "SELECT * FROM D_FD_DEPOSIT_OUTSTANDING_H WHERE COMPANY_ID="+mCompanyID+" AND DOC_NO='"+mDocno.trim()+"' AND REVISION_NO="+RevNo+" ORDER BY SR_NO ";
                rsOut=StmtOut.executeQuery(SQL);
            } else {
                SQL = "SELECT * FROM D_FD_DEPOSIT_OUTSTANDING WHERE COMPANY_ID="+mCompanyID+" AND DOC_NO='"+mDocno.trim()+"' ORDER BY SR_NO";
                rsOut=StmtOut.executeQuery(SQL);
            }
            rsOut.first();
            colLineItemsOut.clear();
            Counter=0;
            while(!rsOut.isAfterLast()) {
                Counter=Counter+1;
                clsOutStandingItem ObjOutItem=new clsOutStandingItem();
                ObjOutItem.setAttribute("COMPANY_ID",rsOut.getLong("COMPANY_ID"));
                ObjOutItem.setAttribute("DOC_NO",rsOut.getString("DOC_NO"));
                ObjOutItem.setAttribute("SR_NO",rsOut.getLong("SR_NO"));
                ObjOutItem.setAttribute("MAIN_ACCOUNT_CODE",rsOut.getString("MAIN_ACCOUNT_CODE"));
                ObjOutItem.setAttribute("AMOUNT",rsOut.getDouble("AMOUNT"));
                ObjOutItem.setAttribute("EFFECT",rsOut.getString("EFFECT"));
                ObjOutItem.setAttribute("CREATED_BY",rsOut.getString("CREATED_BY"));
                ObjOutItem.setAttribute("CREATED_DATE",rsOut.getString("CREATED_DATE"));
                ObjOutItem.setAttribute("MODIFIED_BY",rsOut.getString("MODIFIED_BY"));
                ObjOutItem.setAttribute("MODIFIED_DATE",rsOut.getString("MODIFIED_DATE"));
                ObjOutItem.setAttribute("CHANGED",rsOut.getInt("CHANGED"));
                ObjOutItem.setAttribute("CHANGED_DATE",rsOut.getString("CHANGED_DATE"));
                ObjOutItem.setAttribute("CANCELLED",rsOut.getInt("CANCELLED"));
                
                colLineItemsOut.put(Long.toString(Counter),ObjOutItem);
                rsOut.next();
            }
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
                strSQL="SELECT FINANCE.D_FD_SALES_DEPOSIT_REFUND_HEADER.DOC_NO,FINANCE.D_FD_SALES_DEPOSIT_REFUND_HEADER.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_SALES_DEPOSIT_REFUND_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_SALES_DEPOSIT_REFUND_HEADER.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT FINANCE.D_FD_SALES_DEPOSIT_REFUND_HEADER.DOC_NO,FINANCE.D_FD_SALES_DEPOSIT_REFUND_HEADER.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_SALES_DEPOSIT_REFUND_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_SALES_DEPOSIT_REFUND_HEADER.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_FD_SALES_DEPOSIT_REFUND_HEADER.DOC_NO";
            }
            
            if(Order==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT FINANCE.D_FD_SALES_DEPOSIT_REFUND_HEADER.DOC_NO,FINANCE.D_FD_SALES_DEPOSIT_REFUND_HEADER.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_SALES_DEPOSIT_REFUND_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_SALES_DEPOSIT_REFUND_HEADER.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_FD_SALES_DEPOSIT_REFUND_HEADER.DOC_NO";
            }
            
            rsTemp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTemp.first();
            
            Counter=0;
            
            if(rsTemp.getRow()>0) {
                while(!rsTemp.isAfterLast()) {
                    Counter=Counter+1;
                    clsSalesDepositRefund ObjDoc=new clsSalesDepositRefund();
                    
                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("DOC_NO",UtilFunctions.getString(rsTemp,"DOC_NO",""));
                    String DocNo = UtilFunctions.getString(rsTemp,"DOC_NO","");
                    ObjDoc.setAttribute("DOC_DATE",UtilFunctions.getString(rsTemp,"DOC_DATE","0000-00-00"));
                    ObjDoc.setAttribute("RECEIVED_DATE",UtilFunctions.getString(rsTemp,"RECEIVED_DATE","0000-00-00"));
                    String PartyCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
                    String PartyName = data.getStringValueFromDB("SELECT PARTY_NAME FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
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
            String strSQL="SELECT * FROM D_FD_SALES_DEPOSIT_REFUND_HEADER_H WHERE DOC_NO="+DocNo;
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
            String strSQL="SELECT * FROM D_FD_SALES_DEPOSIT_REFUND_HEADER_H WHERE DOC_NO='"+DocNo+"'";
            rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsSalesDepositRefund objDR=new clsSalesDepositRefund();
                    
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
            
            String strSQL="SELECT DOC_NO,APPROVED,CANCELLED FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+DocNo+"'";
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
            rsTmp=data.getResult("SELECT DOC_NO FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+DocNo+"' AND CANCELLED=0",FinanceGlobal.FinURL);
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
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_FD_SALES_DEPOSIT_REFUND_HEADER WHERE DOC_NO='"+DocNo+"'",FinanceGlobal.FinURL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                if(ApprovedDoc) {
                }
                else {
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"' AND MODULE_ID="+ModuleID,FinanceGlobal.FinURL);
                }
                
                data.Execute("UPDATE D_FD_SALES_DEPOSIT_REFUND_HEADER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='"+DocNo+"'",FinanceGlobal.FinURL);
                Cancelled=true;
            }
            rsTmp.close();
        }
        catch(Exception e) {
        }
        return Cancelled;
    }
    
    private boolean Validate() {
        
        return true;
    }
    
    public double getOutStanding(String MainAccountCode, String PartyCode, String OnDate) {
        double CrAmount = 0;
        double DrAmount = 0;
        double OutAmount = 0;
        try {
            //String FromDate = clsAccount.getLastClosingDate(OnDate);
            
            String SQL = "SELECT SUM(B.AMOUNT) FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B " +
            "WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND B.MAIN_ACCOUNT_CODE='"+ MainAccountCode + "' " +
            "AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND B.EFFECT='C' AND A.CANCELLED=0 AND A.APPROVED=1 AND A.VOUCHER_DATE<='"+OnDate+"' ";
            
            CrAmount = EITLERPGLOBAL.round(data.getDoubleValueFromDB(SQL,FinanceGlobal.FinURL),2);
            
            SQL = "SELECT SUM(B.AMOUNT) FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B " +
            "WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND B.MAIN_ACCOUNT_CODE='"+ MainAccountCode + "' " +
            "AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND B.EFFECT='D' AND A.CANCELLED=0 AND A.APPROVED=1 AND A.VOUCHER_DATE<='"+OnDate+"' ";
            
            DrAmount = EITLERPGLOBAL.round(data.getDoubleValueFromDB(SQL,FinanceGlobal.FinURL),2);
            
            OutAmount = EITLERPGLOBAL.round(DrAmount - CrAmount,2);
        } catch(Exception e) {
            return OutAmount;
        }
        return OutAmount;
    }
    
    public double getPreviousInterest(String PartyCode, String MainAccountCode,String LastDate) {
        double PreviousInterest = 0;
        try {
            //String LastDate = EITLERPGLOBAL.getCurrentFinYear()+"-03-31";
            if(MainAccountCode.equals("132642") || MainAccountCode.equals("132666")) {
                PreviousInterest += clsAccount.getClosingBalance("133155", PartyCode, LastDate,true);
                PreviousInterest += clsAccount.getClosingBalance("133203", PartyCode, LastDate,true);
            } else if (MainAccountCode.equals("132635")) {
                PreviousInterest += clsAccount.getClosingBalance("133162", PartyCode, LastDate,true);
            } else if (MainAccountCode.equals("132714")) {
                PreviousInterest += clsAccount.getClosingBalance("133179", PartyCode, LastDate,true);
            }
        } catch(Exception e) {
            return PreviousInterest;
        }
        return PreviousInterest;
    }
    
    public double getCurrentInterest(String ReceiptNo, String PartyCode, String RefundDate) {
        double CurrentInterest = 0;
        int Days = 0;
        try {
            //String StartFinYear = EITLERPGLOBAL.getCurrentFinYear()+"-04-01";
            String StartFinYear = EITLERPGLOBAL.getFinYearStartDate(RefundDate);
            String EffectiveDate = data.getStringValueFromDB("SELECT EFFECTIVE_DATE FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
            if(java.sql.Date.valueOf(RefundDate).compareTo(java.sql.Date.valueOf(EffectiveDate))!=0) {
                double Amount = data.getDoubleValueFromDB("SELECT AMOUNT FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
                double Rate = data.getDoubleValueFromDB("SELECT INTEREST_RATE FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
                if(Integer.parseInt(RefundDate.substring(8,10))<=15) {
                    int DeductDays = Integer.parseInt(RefundDate.substring(8,10));
                    RefundDate = clsDepositMaster.deductDays(RefundDate, DeductDays);
                } else {
                    String SQL = "SELECT LAST_DAY('"+RefundDate+"') FROM DUAL";
                    RefundDate = data.getStringValueFromDB(SQL);
                }
                if(java.sql.Date.valueOf(EffectiveDate).before(java.sql.Date.valueOf(StartFinYear))) {
                    Days = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(StartFinYear), java.sql.Date.valueOf(RefundDate))+1;
                } else {
                    Days = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(EffectiveDate), java.sql.Date.valueOf(RefundDate))+1;
                }
                GregorianCalendar cal = new GregorianCalendar();
                //if(cal.isLeapYear(EITLERPGLOBAL.getCurrentFinYear()+1)) {
                if(cal.isLeapYear(EITLERPGLOBAL.getCurrentFinYear())) {
                    CurrentInterest = EITLERPGLOBAL.round(((Amount * Rate * Days)/(366* 100)),0); //INTEREST AMOUNT = (AMOUNT * RATE * DAYS)/(366 * 100)
                } else {
                    CurrentInterest = EITLERPGLOBAL.round(((Amount * Rate * Days)/(365* 100)),0); //INTEREST AMOUNT = (AMOUNT * RATE * DAYS)/(365 * 100)
                }
            } else {
                CurrentInterest=0;
            }
        } catch(Exception e) {
        }
        return CurrentInterest;
    }
    
    
    //---------new code by vivek on 28/02/2014 for day wise interest calculation. start -------------------------------------------------------------------------------------------------------------------------------
    // calculates interest daywise
    public double getDaywiseCurrentInterest(String ReceiptNo, String PartyCode, String RefundDate) {
        double CurrentInterest = 0;
        int Days = 0;
        try {
            String StartFinYear = EITLERPGLOBAL.getFinYearStartDate(RefundDate);
            String EffectiveDate = data.getStringValueFromDB("SELECT EFFECTIVE_DATE FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
            if(java.sql.Date.valueOf(RefundDate).compareTo(java.sql.Date.valueOf(EffectiveDate))!=0) {
                double Amount = data.getDoubleValueFromDB("SELECT AMOUNT FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
                double Rate = data.getDoubleValueFromDB("SELECT INTEREST_RATE FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
                
                if(java.sql.Date.valueOf(EffectiveDate).before(java.sql.Date.valueOf(StartFinYear))) {
                    Days = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(StartFinYear), java.sql.Date.valueOf(RefundDate))+1;
                }else {
                    Days = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(EffectiveDate), java.sql.Date.valueOf(RefundDate))+1;
                }
                
                GregorianCalendar cal = new GregorianCalendar();
                if(cal.isLeapYear(EITLERPGLOBAL.getCurrentFinYear()+1)) {
                    CurrentInterest = EITLERPGLOBAL.round(((Amount * Rate * Days)/(366* 100)),0); //INTEREST AMOUNT = (AMOUNT * RATE * DAYS)/(366 * 100)
                }else {
                    CurrentInterest = EITLERPGLOBAL.round(((Amount * Rate * Days)/(365* 100)),0); //INTEREST AMOUNT = (AMOUNT * RATE * DAYS)/(365 * 100)
                }
                
            }else {
                CurrentInterest=0;
            }
        } catch(Exception e) { e.printStackTrace();}
        return CurrentInterest;
    }
    //------- new code by vivek on 28/02/2014 for day wise interest calculation. end ---------------------------------------------------------------------------------------------------------------------------
    
    
    public double getTDSAmount(String pCondition) {
        double interestAmount = 0.0;
        String StartFinYear = Integer.toString(EITLERPGLOBAL.getCurrentFinYear())+"-04-01";
        String EndFinYear = Integer.toString(EITLERPGLOBAL.getCurrentFinYear()+1)+"-03-31";
        String EffectiveDate = "";
        String CloseDate = "";
        int DiffofDays = 1;
        double Rate = 0.0;
        double Amount = 0.0;
        GregorianCalendar cal = new GregorianCalendar();
        try {
            // Matured and Closed within financial year.
            ResultSet rsTDSClose = data.getResult("SELECT * FROM D_FD_SALES_DEPOSIT_MASTER " + pCondition + " AND DEPOSIT_STATUS=1",FinanceGlobal.FinURL);
            rsTDSClose.first();
            if(rsTDSClose.getRow() > 0 ) {
                while(!rsTDSClose.isAfterLast()) {
                    DiffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(StartFinYear), java.sql.Date.valueOf(CloseDate));
                    if (cal.isLeapYear(EITLERPGLOBAL.getCurrentFinYear()+1) ) {
                        //if(cal.isLeapYear(EITLERPGLOBAL.getCurrentFinYear())) {
                        interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36600,0);
                    } else {
                        interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36500,0);
                    }
                    rsTDSClose.next();
                }
            }
            rsTDSClose.close();
            
            // Open within financial year.
            ResultSet rsTDSOpen = data.getResult("SELECT * FROM D_FD_SALES_DEPOSIT_MASTER " + pCondition + " AND DEPOSIT_STATUS=0",FinanceGlobal.FinURL);
            rsTDSOpen.first();
            if(rsTDSOpen.getRow() > 0 ) {
                while(!rsTDSOpen.isAfterLast()) {
                    Amount = rsTDSOpen.getDouble("AMOUNT");
                    Rate = rsTDSOpen.getDouble("INTEREST_RATE");
                    EffectiveDate = rsTDSOpen.getString("EFFECTIVE_DATE");
                    if(java.sql.Date.valueOf(EffectiveDate).before(java.sql.Date.valueOf(StartFinYear))) {
                        DiffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(StartFinYear), java.sql.Date.valueOf(EndFinYear))+1;
                    } else {
                        DiffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(EffectiveDate), java.sql.Date.valueOf(EndFinYear))+1;
                    }
                    
                    if (cal.isLeapYear(EITLERPGLOBAL.getCurrentFinYear()+1) ) {
                        //if(cal.isLeapYear(EITLERPGLOBAL.getCurrentFinYear())) {
                        interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36600,0);
                    } else {
                        interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36500,0);
                    }
                    rsTDSOpen.next();
                }
            }
            rsTDSOpen.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return interestAmount;
    }
    
    public double calculateTDSAmount(double intAmount,String PartyCode) {
        double TDSPercentage = 0;
        double TDSAmount = 0;
        try {
            int DepositerCategory = data.getIntValueFromDB("SELECT DEPOSITER_CATEGORY FROM D_FD_SALES_DEPOSIT_MASTER WHERE PARTY_CODE='"+PartyCode+"' AND DEPOSIT_STATUS=0 AND APPROVED=1 AND CANCELLED=0",FinanceGlobal.FinURL);
            String Type="";
            if(DepositerCategory==1) {
                Type = "COMPANY";
            } else {
                Type = "OTHER";
            }
            HashMap List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesDepositMaster.ModuleID, "GET_TDS_PERCENTAGE", Type, "");
            if(List.size()>0) {
                //Get Tds Percentage
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                TDSPercentage = UtilFunctions.CDbl(objRule.getAttribute("RULE_OUTCOME").getString());
            }
            TDSAmount = EITLERPGLOBAL.round(((intAmount * TDSPercentage)/100),0);
        } catch(Exception e) {
            e.printStackTrace();
            return TDSAmount;
        }
        return TDSAmount;
    }
}
