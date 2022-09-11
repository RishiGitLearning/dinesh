/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package EITLERP.Finance;

import EITLERP.Finance.*;
import java.util.*;
import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import EITLERP.* ;
import EITLERP.data;
/**
 *
 * @author Prathmesh Shah
 */

public class clsSalesDepositMaster {
    public static int ModuleID=142;
    
    //*********** Local reference of data class ***************//
    private clsSalesDepositMaster ObjSalesDepositMaster;
    clsVoucher objVoucher;
    //private Connection Conn=null;
    
    public HashMap colHistory=new HashMap();
    Statement stSalesDepositMaster, stSalesDepositMasterH,stCalcDetail;
    ResultSet rsSalesDepositMaster, rsSalesDepositMasterH,rsCalcDetail;
    private Connection Conn;
    private Statement Stmt;
    
    public String LastError="";
    public String BookCode="";
    //Indent Line Items Collection
    public HashMap colLineItems=new HashMap();
    public HashMap colItemDetail=new HashMap();
    
    private HashMap props;
    public boolean Ready = false;
    
    //History Related properties
    private boolean HistoryView=false;
    
    private int LastPosition=0;
    private ResultSet rsResultSet;
    
    public boolean UserDocNo=false;
    public static boolean RenewEntry=false;
    public static String publicReceiptNo = "";
    //*********************************************************//
    
    
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
    
    public clsSalesDepositMaster() {
        LastError = "";
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("RECEIPT_NO",new Variant(""));
        props.put("FFNO",new Variant(0));
        props.put("RECEIPT_DATE",new Variant(""));
        props.put("TITLE",new Variant(""));
        
        props.put("APPLICANT_NAME",new Variant(""));
        props.put("ADDRESS1",new Variant(""));
        props.put("ADDRESS2",new Variant(""));
        props.put("ADDRESS3",new Variant(""));
        props.put("CITY",new Variant(""));
        props.put("PINCODE",new Variant(""));
        props.put("CONTACT_NO",new Variant(""));
        
        props.put("SCHEME_ID",new Variant(""));
        props.put("SCHEME_NAME",new Variant(""));
        props.put("DEPOSIT_TYPE_ID",new Variant(0));
        props.put("DEPOSIT_TYPE_NAME",new Variant(""));
        props.put("DEPOSITER_STATUS",new Variant(0));
        props.put("DEPOSITER_CATEGORY",new Variant(0));
        props.put("DEPOSIT_PAYABLE_TO",new Variant(0));
        
        props.put("INTEREST_MAIN_CODE",new Variant(""));
        props.put("MAIN_ACCOUNT_CODE",new Variant(""));
        props.put("INTEREST_RATE",new Variant(0.0));
        props.put("DEPOSITER_CATEGORY_OTHERS",new Variant(""));
        
        props.put("EFFECTIVE_DATE",new Variant(""));
        props.put("REFUND_DATE",new Variant(""));
        props.put("INT_CALC_DATE",new Variant(""));
        
        props.put("TAX_EX_FORM_RECEIVED",new Variant(0));
        props.put("PARTY_CODE",new Variant(""));
        props.put("TDS_APPLICABLE",new Variant(0));
        props.put("PAN_NO",new Variant(""));
        props.put("PAN_DATE",new Variant(""));
        props.put("PARTICULARS",new Variant(""));
        
        props.put("CHEQUE_NO",new Variant(""));
        props.put("CHEQUE_DATE",new Variant(""));
        props.put("REALIZATION_DATE",new Variant(""));
        props.put("AMOUNT",new Variant(0.0));
        props.put("FUN_TRANSFER_FROM", new Variant(""));
        
        props.put("BANK_MAIN_CODE",new Variant(""));
        props.put("BANK_NAME",new Variant(""));
        props.put("BANK_ADDRESS",new Variant(""));
        props.put("BANK_CITY",new Variant(""));
        props.put("BANK_PINCODE",new Variant(""));
        
        props.put("DEPOSIT_STATUS",new Variant(0));
        
        props.put("FIN_HIERARCHY_ID",new Variant(0));
        props.put("BOOK_CODE",new Variant(0));
        
        props.put("APPROVED",new Variant(0));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE",new Variant(""));
        props.put("REJECTED_REMARKS",new Variant(""));
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        
        props.put("HIERARCHY_ID",new Variant(0));
        
        props.put("CHANGED",new Variant(0));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("CANCELLED",new Variant(0));
    }
    
    public boolean LoadData(int pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn(FinanceGlobal.FinURL);
            //objConn=objData.getConn(strSQL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsSalesDepositMaster=Stmt.executeQuery("SELECT * FROM D_FD_SALES_DEPOSIT_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" ORDER BY RECEIPT_NO");/*EITLERPGLOBAL.gCompanyID....*/
            HistoryView=false;
            Ready=true;
            MoveFirst();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public void Close() {
        try {
            //objConn.close();
            Stmt.close();
            rsSalesDepositMaster.close();
        }
        catch(Exception e) {
        }
    }
    
    //Navigation Methods
    public boolean MoveFirst() {
        try {
            rsSalesDepositMaster.first();
            SetData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public boolean MoveNext() {
        try {
            if(rsSalesDepositMaster.isAfterLast()||rsSalesDepositMaster.isLast()) {
                //Move pointer at last record
                //If it is beyond eof
                rsSalesDepositMaster.last();
            }
            else {
                rsSalesDepositMaster.next();
                if(rsSalesDepositMaster.isAfterLast()) {
                    rsSalesDepositMaster.last();
                }
            }
            SetData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public boolean MovePrevious() {
        try {
            if(rsSalesDepositMaster.isFirst()||rsSalesDepositMaster.isBeforeFirst()) {
                rsSalesDepositMaster.first();
            }
            else {
                rsSalesDepositMaster.previous();
                if(rsSalesDepositMaster.isBeforeFirst()) {
                    rsSalesDepositMaster.first();
                }
            }
            SetData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public boolean MoveLast() {
        try {
            rsSalesDepositMaster.last();
            SetData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public boolean Insert() {
        
        try {
            //===== History Related Changes =====//
            stSalesDepositMasterH = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsSalesDepositMasterH = stSalesDepositMasterH.executeQuery("SELECT * FROM D_FD_SALES_DEPOSIT_MASTER_H WHERE RECEIPT_NO='1'");
            //===================================//
            
            if(!Validate()) {
                return false;
            }
            
            if(data.IsRecordExist("SELECT RECEIPT_NO FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+getAttribute("RECEIPT_NO").getString()+"'",FinanceGlobal.FinURL)) {
                LastError="Deposit with this Receipt No already exist.";
                return false;
            }
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //=============================Generate New ReceiptNo.===============================
            setAttribute("RECEIPT_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,clsSalesDepositMaster.ModuleID,(int)getAttribute("FFNO").getVal(),true));
            //===============================================================================================
            
            //****************** Insert Records ********************//
            rsSalesDepositMaster.moveToInsertRow();
            //Applicant Detail
            rsSalesDepositMaster.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            int CompanyID = EITLERPGLOBAL.gCompanyID;
            rsSalesDepositMaster.updateString("RECEIPT_NO",getAttribute("RECEIPT_NO").getString());
            String ReceiptNo = getAttribute("RECEIPT_NO").getString();
            rsSalesDepositMaster.updateString("RECEIPT_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("RECEIPT_DATE").getString()));
            rsSalesDepositMaster.updateString("TITLE", getAttribute("TITLE").getString());
            rsSalesDepositMaster.updateString("APPLICANT_NAME", getAttribute("APPLICANT_NAME").getString());
            rsSalesDepositMaster.updateString("ADDRESS1", getAttribute("ADDRESS1").getString());
            rsSalesDepositMaster.updateString("ADDRESS2", getAttribute("ADDRESS2").getString());
            rsSalesDepositMaster.updateString("ADDRESS3", getAttribute("ADDRESS3").getString());
            rsSalesDepositMaster.updateString("CITY", getAttribute("CITY").getString());
            rsSalesDepositMaster.updateString("PINCODE", getAttribute("PINCODE").getString());
            rsSalesDepositMaster.updateString("CONTACT_NO", getAttribute("CONTACT_NO").getString());
            
            //Other Detail
            rsSalesDepositMaster.updateString("SCHEME_ID", getAttribute("SCHEME_ID").getString());
            rsSalesDepositMaster.updateInt("DEPOSITER_STATUS", getAttribute("DEPOSITER_STATUS").getInt());
            rsSalesDepositMaster.updateInt("DEPOSITER_CATEGORY", getAttribute("DEPOSITER_CATEGORY").getInt());
            rsSalesDepositMaster.updateInt("DEPOSIT_TYPE_ID", getAttribute("DEPOSIT_TYPE_ID").getInt());
            rsSalesDepositMaster.updateInt("DEPOSIT_PAYABLE_TO", getAttribute("DEPOSIT_PAYABLE_TO").getInt());
            
            rsSalesDepositMaster.updateString("MAIN_ACCOUNT_CODE", getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsSalesDepositMaster.updateString("INTEREST_MAIN_CODE", getAttribute("INTEREST_MAIN_CODE").getString());
            rsSalesDepositMaster.updateDouble("INTEREST_RATE", getAttribute("INTEREST_RATE").getDouble());
            
            rsSalesDepositMaster.updateString("DEPOSITER_CATEGORY_OTHERS", getAttribute("DEPOSITER_CATEGORY_OTHERS").getString());
            rsSalesDepositMaster.updateString("EFFECTIVE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_DATE").getString()));
            rsSalesDepositMaster.updateString("REFUND_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("REFUND_DATE").getString()));
            rsSalesDepositMaster.updateString("INT_CALC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("INT_CALC_DATE").getString()));
            
            rsSalesDepositMaster.updateInt("TAX_EX_FORM_RECEIVED",getAttribute("TAX_EX_FORM_RECEIVED").getInt());
            rsSalesDepositMaster.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsSalesDepositMaster.updateInt("TDS_APPLICABLE", getAttribute("TDS_APPLICABLE").getInt());
            rsSalesDepositMaster.updateString("PAN_NO", getAttribute("PAN_NO").getString());
            rsSalesDepositMaster.updateString("PAN_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PAN_DATE").getString()));
            rsSalesDepositMaster.updateString("PARTICULARS", getAttribute("PARTICULARS").getString());
            
            //Bank Detail
            rsSalesDepositMaster.updateString("CHEQUE_NO", getAttribute("CHEQUE_NO").getString());
            rsSalesDepositMaster.updateString("CHEQUE_DATE", EITLERPGLOBAL.formatDateDB( getAttribute("CHEQUE_DATE").getString()));
            rsSalesDepositMaster.updateString("REALIZATION_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("REALIZATION_DATE").getString()));
            rsSalesDepositMaster.updateDouble("AMOUNT", getAttribute("AMOUNT").getDouble());
            rsSalesDepositMaster.updateString("FUND_TRANSFER_FROM", getAttribute("FUND_TRANSFER_FROM").getString());
            
            rsSalesDepositMaster.updateString("BANK_MAIN_CODE", getAttribute("BANK_MAIN_CODE").getString());
            rsSalesDepositMaster.updateString("BANK_NAME", getAttribute("BANK_NAME").getString());
            rsSalesDepositMaster.updateString("BANK_ADDRESS", getAttribute("BANK_ADDRESS").getString());
            rsSalesDepositMaster.updateString("BANK_CITY", getAttribute("BANK_CITY").getString());
            rsSalesDepositMaster.updateString("BANK_PINCODE", getAttribute("BANK_PINCODE").getString());
            
            //Deposit Releated Information
            rsSalesDepositMaster.updateInt("DEPOSIT_STATUS", getAttribute("DEPOSIT_STATUS").getInt()); //Status 0. For OPEN (new Entry) 1. For close
            
            //Approval Specific
            rsSalesDepositMaster.updateBoolean("APPROVED",false);
            rsSalesDepositMaster.updateString("APPROVED_DATE","0000-00-00");
            rsSalesDepositMaster.updateBoolean("REJECTED", false);
            rsSalesDepositMaster.updateString("REJECTED_DATE", "0000-00-00");
            rsSalesDepositMaster.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsSalesDepositMaster.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
            rsSalesDepositMaster.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsSalesDepositMaster.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
            rsSalesDepositMaster.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            
            rsSalesDepositMaster.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            rsSalesDepositMaster.updateBoolean("CHANGED", true);
            rsSalesDepositMaster.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsSalesDepositMaster.updateBoolean("CANCELLED", false);
            rsSalesDepositMaster.insertRow();
            
            
            //******************** History Update ************************//
            
            rsSalesDepositMasterH.moveToInsertRow() ;
            rsSalesDepositMasterH.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsSalesDepositMasterH.updateInt("REVISION_NO", 1); //Revision No. 1 in case of Insert
            rsSalesDepositMasterH.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsSalesDepositMasterH.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsSalesDepositMasterH.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsSalesDepositMasterH.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            
            //Applicant Detail
            rsSalesDepositMasterH.updateString("RECEIPT_NO", getAttribute("RECEIPT_NO").getString());
            rsSalesDepositMasterH.updateString("RECEIPT_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("RECEIPT_DATE").getString()));
            rsSalesDepositMasterH.updateString("TITLE", getAttribute("TITLE").getString());
            rsSalesDepositMasterH.updateString("APPLICANT_NAME", getAttribute("APPLICANT_NAME").getString());
            
            rsSalesDepositMasterH.updateString("ADDRESS1", getAttribute("ADDRESS1").getString());
            rsSalesDepositMasterH.updateString("ADDRESS2", getAttribute("ADDRESS2").getString());
            rsSalesDepositMasterH.updateString("ADDRESS3", getAttribute("ADDRESS3").getString());
            rsSalesDepositMasterH.updateString("CITY", getAttribute("CITY").getString());
            rsSalesDepositMasterH.updateString("PINCODE", getAttribute("PINCODE").getString());
            rsSalesDepositMasterH.updateString("CONTACT_NO", getAttribute("CONTACT_NO").getString());
            
            //Other Detail
            rsSalesDepositMasterH.updateString("SCHEME_ID", getAttribute("SCHEME_ID").getString());
            rsSalesDepositMasterH.updateInt("DEPOSITER_STATUS", getAttribute("DEPOSITER_STATUS").getInt());
            rsSalesDepositMasterH.updateInt("DEPOSITER_CATEGORY", getAttribute("DEPOSITER_CATEGORY").getInt());
            rsSalesDepositMasterH.updateInt("DEPOSIT_TYPE_ID", getAttribute("DEPOSIT_TYPE_ID").getInt());
            rsSalesDepositMasterH.updateInt("DEPOSIT_PAYABLE_TO", getAttribute("DEPOSIT_PAYABLE_TO").getInt());
            
            rsSalesDepositMasterH.updateDouble("INTEREST_RATE", getAttribute("INTEREST_RATE").getDouble());
            rsSalesDepositMasterH.updateString("MAIN_ACCOUNT_CODE", getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsSalesDepositMasterH.updateString("INTEREST_MAIN_CODE", getAttribute("INTEREST_MAIN_CODE").getString());
            rsSalesDepositMasterH.updateString("DEPOSITER_CATEGORY_OTHERS", getAttribute("DEPOSITER_CATEGORY_OTHERS").getString());
            
            rsSalesDepositMasterH.updateString("EFFECTIVE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_DATE").getString()));
            rsSalesDepositMasterH.updateString("REFUND_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("REFUND_DATE").getString()));
            rsSalesDepositMasterH.updateString("INT_CALC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("INT_CALC_DATE").getString()));
            
            rsSalesDepositMasterH.updateInt("TAX_EX_FORM_RECEIVED", getAttribute("TAX_EX_FORM_RECEIVED").getInt());
            rsSalesDepositMasterH.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsSalesDepositMasterH.updateInt("TDS_APPLICABLE", getAttribute("TDS_APPLICABLE").getInt());
            rsSalesDepositMasterH.updateString("PAN_NO", getAttribute("PAN_NO").getString());
            rsSalesDepositMasterH.updateString("PAN_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PAN_DATE").getString()));
            rsSalesDepositMasterH.updateString("PARTICULARS", getAttribute("PARTICULARS").getString());
            
            //Bank Detail
            rsSalesDepositMasterH.updateString("CHEQUE_NO", getAttribute("CHEQUE_NO").getString());
            rsSalesDepositMasterH.updateString("CHEQUE_DATE", EITLERPGLOBAL.formatDateDB( getAttribute("CHEQUE_DATE").getString()));
            rsSalesDepositMasterH.updateString("REALIZATION_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("REALIZATION_DATE").getString()));
            rsSalesDepositMasterH.updateDouble("AMOUNT", getAttribute("AMOUNT").getDouble());
            rsSalesDepositMasterH.updateDouble("FUND_TRANSFER_FROM", getAttribute("FUND_TRANSFER_FROM").getDouble());
            
            rsSalesDepositMasterH.updateString("BANK_MAIN_CODE", getAttribute("BANK_MAIN_CODE").getString());
            rsSalesDepositMasterH.updateString("BANK_NAME", getAttribute("BANK_NAME").getString());
            rsSalesDepositMasterH.updateString("BANK_ADDRESS", getAttribute("BANK_ADDRESS").getString());
            rsSalesDepositMasterH.updateString("BANK_CITY", getAttribute("BANK_CITY").getString());
            rsSalesDepositMasterH.updateString("BANK_PINCODE", getAttribute("BANK_PINCODE").getString());
            
            //Deposit Releated Information
            rsSalesDepositMasterH.updateInt("DEPOSIT_STATUS", getAttribute("DEPOSIT_STATUS").getInt()); //Status 0. For OPEN (new Entry) 1. For close
            
            //Approval Specific
            rsSalesDepositMasterH.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
            rsSalesDepositMasterH.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsSalesDepositMasterH.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
            rsSalesDepositMasterH.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsSalesDepositMasterH.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            
            rsSalesDepositMasterH.updateBoolean("APPROVED", false);
            rsSalesDepositMasterH.updateString("APPROVED_DATE", "0000-00-00");
            rsSalesDepositMasterH.updateBoolean("REJECTED", false);
            rsSalesDepositMasterH.updateString("REJECTED_DATE", "0000-00-00");
            rsSalesDepositMasterH.updateBoolean("CHANGED", true);
            rsSalesDepositMasterH.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsSalesDepositMasterH.updateBoolean("CANCELLED", false);
            rsSalesDepositMasterH.insertRow();
            //*************************************************************//
            
            //************ Update Approval Flow *****************//
            ObjFlow.CompanyID =getAttribute("COMPANY_ID").getInt();
            ObjFlow.ModuleID = this.ModuleID ;
            ObjFlow.DocNo = getAttribute("RECEIPT_NO").getString();
            ObjFlow.From =getAttribute("FROM").getInt();
            ObjFlow.To = getAttribute("TO").getInt();
            ObjFlow.Status = getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName = "D_FD_SALES_DEPOSIT_MASTER";
            ObjFlow.IsCreator = true;
            ObjFlow.HierarchyID =getAttribute("HIERARCHY_ID").getInt();
            ObjFlow.Remarks =getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName ="RECEIPT_NO";
            ObjFlow.UseSpecifiedURL=true;
            ObjFlow.SpecificURL=FinanceGlobal.FinURL;
            ObjFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            
            if (ObjFlow.Status.equals("H")) {
                ObjFlow.Status = "A";
                ObjFlow.To = ObjFlow.From;
                ObjFlow.UpdateFlow();
            }
            else {
                if (!ObjFlow.UpdateFlow()) {
                    LastError = ObjFlow.LastError;
                }
            }
            ObjFlow.UseSpecifiedURL=false;
            //**************End of Approval Flow *****************//
            publicReceiptNo = getAttribute("RECEIPT_NO").getString();
            MoveLast();
            /*******************************VOUCHER POSTING******************************/
            /*if(ObjFlow.Status.equals("F")) {
                //int CompanyID = getAttribute("COMPANY_ID").getInt();
                //String ReceiptNo = getAttribute("RECEIPT_NO").getString();
                String BankMainCode = data.getStringValueFromDB("SELECT BANK_MAIN_CODE FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                String FundTransferFrom = data.getStringValueFromDB("SELECT FUND_TRANSFER_FROM FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                if(!BankMainCode.equals("") && FundTransferFrom.equals("")) {
                    if(!PostReceiptVoucher(CompanyID,ReceiptNo)) {
                        LastError = " Receipt Voucher Not Posted Properly.";
                        return false;
                    }
                } else if (BankMainCode.equals("") && !FundTransferFrom.equals("")) {
                    if(!PostJournalVoucher(CompanyID,ReceiptNo)) {
                        LastError = " Receipt Voucher Not Posted Properly.";
                        return false;
                    }
                }
            }*/
            /****************************************************************************/
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LastError=e.getMessage();
            return false;
        }
    }
    
    public boolean Update() {
        
        try {
            //String ReceiptNo=lDocNo;
            boolean Validate=true;
            
            //** Validations **//
            if(!Validate()) {
                return false;
            }
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            String AStatus=getAttribute("APPROVAL_STATUS").getString();
            if(AStatus.equals("R")) {
                Validate=false; //Do not validate while in rejection mode.
            }
            
            stSalesDepositMasterH = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsSalesDepositMasterH = stSalesDepositMasterH.executeQuery("SELECT * FROM D_FD_SALES_DEPOSIT_MASTER_H ");
            
            //****************** Update Records ********************//
            //Applicant Detail
            rsSalesDepositMaster.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            int CompanyID = EITLERPGLOBAL.gCompanyID;
            rsSalesDepositMaster.updateString("RECEIPT_NO", getAttribute("RECEIPT_NO").getString());
            String ReceiptNo = getAttribute("RECEIPT_NO").getString();
            rsSalesDepositMaster.updateString("RECEIPT_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("RECEIPT_DATE").getString()));
            rsSalesDepositMaster.updateString("TITLE", getAttribute("TITLE").getString());
            rsSalesDepositMaster.updateString("APPLICANT_NAME", getAttribute("APPLICANT_NAME").getString());
            rsSalesDepositMaster.updateString("ADDRESS1", getAttribute("ADDRESS1").getString());
            rsSalesDepositMaster.updateString("ADDRESS2", getAttribute("ADDRESS2").getString());
            rsSalesDepositMaster.updateString("ADDRESS3", getAttribute("ADDRESS3").getString());
            rsSalesDepositMaster.updateString("CITY", getAttribute("CITY").getString());
            rsSalesDepositMaster.updateString("PINCODE", getAttribute("PINCODE").getString());
            rsSalesDepositMaster.updateString("CONTACT_NO", getAttribute("CONTACT_NO").getString());
            
            //Other Detail
            rsSalesDepositMaster.updateString("SCHEME_ID", getAttribute("SCHEME_ID").getString());
            rsSalesDepositMaster.updateInt("DEPOSIT_TYPE_ID", getAttribute("DEPOSIT_TYPE_ID").getInt());
            rsSalesDepositMaster.updateInt("DEPOSITER_STATUS", getAttribute("DEPOSITER_STATUS").getInt());
            rsSalesDepositMaster.updateInt("DEPOSITER_CATEGORY", getAttribute("DEPOSITER_CATEGORY").getInt());
            rsSalesDepositMaster.updateInt("DEPOSIT_PAYABLE_TO", getAttribute("DEPOSIT_PAYABLE_TO").getInt());
            
            rsSalesDepositMaster.updateString("MAIN_ACCOUNT_CODE", getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsSalesDepositMaster.updateString("INTEREST_MAIN_CODE", getAttribute("INTEREST_MAIN_CODE").getString());
            rsSalesDepositMaster.updateDouble("INTEREST_RATE", getAttribute("INTEREST_RATE").getDouble());
            rsSalesDepositMaster.updateString("DEPOSITER_CATEGORY_OTHERS", getAttribute("DEPOSITER_CATEGORY_OTHERS").getString());
            
            rsSalesDepositMaster.updateString("EFFECTIVE_DATE", EITLERPGLOBAL.formatDateDB((getAttribute("EFFECTIVE_DATE").getString())));
            rsSalesDepositMaster.updateString("REFUND_DATE", EITLERPGLOBAL.formatDateDB((getAttribute("REFUND_DATE").getString())));
            rsSalesDepositMaster.updateString("INT_CALC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("INT_CALC_DATE").getString()));
            
            rsSalesDepositMaster.updateInt("TAX_EX_FORM_RECEIVED",getAttribute("TAX_EX_FORM_RECEIVED").getInt());
            rsSalesDepositMaster.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsSalesDepositMaster.updateInt("TDS_APPLICABLE", getAttribute("TDS_APPLICABLE").getInt());
            rsSalesDepositMaster.updateString("PAN_NO", getAttribute("PAN_NO").getString());
            rsSalesDepositMaster.updateString("PAN_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PAN_DATE").getString()));
            rsSalesDepositMaster.updateString("PARTICULARS", getAttribute("PARTICULARS").getString());
            
            //Bank Detail
            rsSalesDepositMaster.updateString("CHEQUE_NO", getAttribute("CHEQUE_NO").getString());
            rsSalesDepositMaster.updateString("CHEQUE_DATE", EITLERPGLOBAL.formatDateDB( getAttribute("CHEQUE_DATE").getString()));
            rsSalesDepositMaster.updateString("REALIZATION_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("REALIZATION_DATE").getString()));
            rsSalesDepositMaster.updateDouble("AMOUNT", getAttribute("AMOUNT").getDouble());
            rsSalesDepositMaster.updateString("FUND_TRANSFER_FROM", getAttribute("FUND_TRANSFER_FROM").getString());
            
            rsSalesDepositMaster.updateString("BANK_MAIN_CODE", getAttribute("BANK_MAIN_CODE").getString());
            rsSalesDepositMaster.updateString("BANK_NAME", getAttribute("BANK_NAME").getString());
            rsSalesDepositMaster.updateString("BANK_ADDRESS", getAttribute("BANK_ADDRESS").getString());
            rsSalesDepositMaster.updateString("BANK_CITY", getAttribute("BANK_CITY").getString());
            rsSalesDepositMaster.updateString("BANK_PINCODE", getAttribute("BANK_PINCODE").getString());
            
            //Deposit Releated Information
            rsSalesDepositMaster.updateInt("DEPOSIT_STATUS", getAttribute("DEPOSIT_STATUS").getInt()); //Status 0. For OPEN (new Entry) 1. For close
            
            //Approval Specific
            rsSalesDepositMaster.updateBoolean("APPROVED", false);
            rsSalesDepositMaster.updateString("APPROVED_DATE", "0000-00-00");
            rsSalesDepositMaster.updateBoolean("REJECTED", false);
            rsSalesDepositMaster.updateString("REJECTED_DATE", "0000-00-00");
            rsSalesDepositMaster.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            //rsSalesDepositMaster.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
            //rsSalesDepositMaster.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsSalesDepositMaster.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
            rsSalesDepositMaster.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsSalesDepositMaster.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            rsSalesDepositMaster.updateBoolean("CHANGED", true);
            rsSalesDepositMaster.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsSalesDepositMaster.updateBoolean("CANCELLED", false);
            rsSalesDepositMaster.updateRow();
            
            //******************** History Update ************************//
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM FINANCE.D_FD_SALES_DEPOSIT_MASTER_H WHERE RECEIPT_NO='"+getAttribute("RECEIPT_NO").getString()+"'");
            RevNo++;
            
            rsSalesDepositMasterH.moveToInsertRow() ;
            rsSalesDepositMasterH.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsSalesDepositMasterH.updateInt("REVISION_NO", RevNo);
            rsSalesDepositMasterH.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsSalesDepositMasterH.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsSalesDepositMasterH.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsSalesDepositMasterH.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            
            //Applicant Detail
            rsSalesDepositMasterH.updateString("RECEIPT_NO", getAttribute("RECEIPT_NO").getString());
            rsSalesDepositMasterH.updateString("RECEIPT_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("RECEIPT_DATE").getString()));
            rsSalesDepositMasterH.updateString("TITLE", getAttribute("TITLE").getString());
            rsSalesDepositMasterH.updateString("APPLICANT_NAME", getAttribute("APPLICANT_NAME").getString());
            
            rsSalesDepositMasterH.updateString("ADDRESS1", getAttribute("ADDRESS1").getString());
            rsSalesDepositMasterH.updateString("ADDRESS2", getAttribute("ADDRESS2").getString());
            rsSalesDepositMasterH.updateString("ADDRESS3", getAttribute("ADDRESS3").getString());
            rsSalesDepositMasterH.updateString("CITY", getAttribute("CITY").getString());
            rsSalesDepositMasterH.updateString("PINCODE", getAttribute("PINCODE").getString());
            rsSalesDepositMasterH.updateString("CONTACT_NO", getAttribute("CONTACT_NO").getString());
            
            //Other Detail
            rsSalesDepositMasterH.updateString("SCHEME_ID", getAttribute("SCHEME_ID").getString());
            rsSalesDepositMasterH.updateInt("DEPOSITER_STATUS", getAttribute("DEPOSITER_STATUS").getInt());
            rsSalesDepositMasterH.updateInt("DEPOSITER_CATEGORY", getAttribute("DEPOSITER_CATEGORY").getInt());
            rsSalesDepositMasterH.updateInt("DEPOSIT_TYPE_ID", getAttribute("DEPOSIT_TYPE_ID").getInt());
            rsSalesDepositMasterH.updateInt("DEPOSIT_PAYABLE_TO", getAttribute("DEPOSIT_PAYABLE_TO").getInt());
            
            rsSalesDepositMasterH.updateDouble("INTEREST_RATE", getAttribute("INTEREST_RATE").getDouble());
            rsSalesDepositMasterH.updateString("MAIN_ACCOUNT_CODE", getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsSalesDepositMasterH.updateString("INTEREST_MAIN_CODE", getAttribute("INTEREST_MAIN_CODE").getString());
            rsSalesDepositMasterH.updateString("DEPOSITER_CATEGORY_OTHERS", getAttribute("DEPOSITER_CATEGORY_OTHERS").getString());
            
            rsSalesDepositMasterH.updateString("EFFECTIVE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_DATE").getString()));
            rsSalesDepositMasterH.updateString("REFUND_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("REFUND_DATE").getString()));
            rsSalesDepositMasterH.updateString("INT_CALC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("INT_CALC_DATE").getString()));
            
            rsSalesDepositMasterH.updateInt("TAX_EX_FORM_RECEIVED", getAttribute("TAX_EX_FORM_RECEIVED").getInt());
            rsSalesDepositMasterH.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsSalesDepositMasterH.updateInt("TDS_APPLICABLE", getAttribute("TDS_APPLICABLE").getInt());
            rsSalesDepositMasterH.updateString("PAN_NO", getAttribute("PAN_NO").getString());
            rsSalesDepositMasterH.updateString("PAN_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PAN_DATE").getString()));
            rsSalesDepositMasterH.updateString("PARTICULARS", getAttribute("PARTICULARS").getString());
            
            //Bank Detail
            rsSalesDepositMasterH.updateString("CHEQUE_NO", getAttribute("CHEQUE_NO").getString());
            rsSalesDepositMasterH.updateString("CHEQUE_DATE", EITLERPGLOBAL.formatDateDB( getAttribute("CHEQUE_DATE").getString()));
            rsSalesDepositMasterH.updateString("REALIZATION_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("REALIZATION_DATE").getString()));
            rsSalesDepositMasterH.updateDouble("AMOUNT", getAttribute("AMOUNT").getDouble());
            rsSalesDepositMasterH.updateString("FUND_TRANSFER_FROM", getAttribute("FUND_TRANSFER_FROM").getString());
            
            rsSalesDepositMasterH.updateString("BANK_MAIN_CODE", getAttribute("BANK_MAIN_CODE").getString());
            rsSalesDepositMasterH.updateString("BANK_NAME", getAttribute("BANK_NAME").getString());
            rsSalesDepositMasterH.updateString("BANK_ADDRESS", getAttribute("BANK_ADDRESS").getString());
            rsSalesDepositMasterH.updateString("BANK_CITY", getAttribute("BANK_CITY").getString());
            rsSalesDepositMasterH.updateString("BANK_PINCODE", getAttribute("BANK_PINCODE").getString());
            
            //Deposit Releated Information
            rsSalesDepositMasterH.updateInt("DEPOSIT_STATUS", getAttribute("DEPOSIT_STATUS").getInt()); //Status 0. For OPEN (new Entry) 1. For close
            
            //Approval Specific
            //rsSalesDepositMasterH.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
            //rsSalesDepositMasterH.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsSalesDepositMasterH.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
            rsSalesDepositMasterH.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsSalesDepositMasterH.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            
            rsSalesDepositMasterH.updateBoolean("APPROVED", false);
            rsSalesDepositMasterH.updateString("APPROVED_DATE", "0000-00-00");
            rsSalesDepositMasterH.updateBoolean("REJECTED", false);
            rsSalesDepositMasterH.updateString("REJECTED_DATE", "0000-00-00");
            rsSalesDepositMasterH.updateBoolean("CHANGED", true);
            rsSalesDepositMasterH.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsSalesDepositMasterH.updateBoolean("CANCELLED", false);
            rsSalesDepositMasterH.insertRow();
            //*************************************************************//
            
            //************ Update Approval Flow *****************//
            ObjFlow.CompanyID =getAttribute("COMPANY_ID").getInt();
            ObjFlow.ModuleID = this.ModuleID ;
            ObjFlow.DocNo = getAttribute("RECEIPT_NO").getString();
            ObjFlow.From =getAttribute("FROM").getInt();
            ObjFlow.To = getAttribute("TO").getInt();
            ObjFlow.Status = getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName = "D_FD_SALES_DEPOSIT_MASTER";
            ObjFlow.IsCreator = false;
            ObjFlow.HierarchyID =getAttribute("HIERARCHY_ID").getInt();
            ObjFlow.Remarks =getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName ="RECEIPT_NO";
            ObjFlow.UseSpecifiedURL=true;
            ObjFlow.SpecificURL=FinanceGlobal.FinURL;
            
            //==== Handling Rejected Documents ==========//
            
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_FD_SALES_DEPOSIT_MASTER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE RECEIPT_NO='"+ getAttribute("RECEIPT_NO").getString() +"' ",FinanceGlobal.FinURL);
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE MODULE_ID="+ModuleID+" AND DOC_NO='"+ getAttribute("RECEIPT_NO").getString() +"' ");
                
                ObjFlow.IsCreator=true;
            }
            //==========================================//
            
            if (ObjFlow.Status.equals("H")) {
                ObjFlow.Status = "A";
                ObjFlow.To = ObjFlow.From;
                ObjFlow.UpdateFlow();
            }
            else {
                if (!ObjFlow.UpdateFlow()) {
                    LastError = ObjFlow.LastError;
                }
            }
            ObjFlow.UseSpecifiedURL=true;
            //**************End of Approval Flow *****************//
            
            MoveLast();
            /**********************Voucher Posting*******************************/
            /*if(ObjFlow.Status.equals("F")) {
                //int CompanyID = getAttribute("COMPANY_ID").getInt();
                //String ReceiptNo = getAttribute("RECEIPT_NO").getString();
                String BankMainCode = data.getStringValueFromDB("SELECT BANK_MAIN_CODE FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                String FundTransferFrom = data.getStringValueFromDB("SELECT FUND_TRANSFER_FROM FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                if(!BankMainCode.equals("") && FundTransferFrom.equals("")) {
                    if(!PostReceiptVoucher(CompanyID,ReceiptNo)) {
                        LastError = " Receipt Voucher Not Posted Properly.";
                        return false;
                    }
                } else if (BankMainCode.equals("") && !FundTransferFrom.equals("")) {
                    if(!PostJournalVoucher(CompanyID,ReceiptNo)) {
                        LastError = " Receipt Voucher Not Posted Properly.";
                        return false;
                    }
                }
            }*/
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LastError=e.getMessage();
            return false;
        }
    }
    
    public clsSalesDepositMaster getObject(int CompanyID,String ReceiptNo) {
        
        clsSalesDepositMaster ObjSalesDepositMaster=new clsSalesDepositMaster();
        
        try {
            Connection Conn=data.getConn(FinanceGlobal.FinURL);
            Statement stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            ResultSet rsTmp;
            ResultSet rsSchemeID;
            ResultSet rsDepositTypeID;
            ResultSet rsInterestRate;
            
            rsTmp=stTmp.executeQuery("SELECT * FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO=" +ReceiptNo);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                //Applicant Detail
                ObjSalesDepositMaster.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp, "COMPANY_ID", 0));
                ObjSalesDepositMaster.setAttribute("RECEIPT_NO",UtilFunctions.getString(rsTmp, "RECEIPT_NO", ""));
                ObjSalesDepositMaster.setAttribute("RECEIPT_DATE",UtilFunctions.getString(rsTmp, "RECEIPT_DATE","0000-00-00"));
                ObjSalesDepositMaster.setAttribute("TITLE",UtilFunctions.getString(rsTmp, "TITLE", ""));
                ObjSalesDepositMaster.setAttribute("APPLICANT_NAME",UtilFunctions.getString(rsTmp, "APPLICANT_NAME", ""));
                ObjSalesDepositMaster.setAttribute("ADDRESS1",UtilFunctions.getString(rsTmp, "ADDRESS1", ""));
                ObjSalesDepositMaster.setAttribute("ADDRESS2",UtilFunctions.getString(rsTmp, "ADDRESS2", ""));
                ObjSalesDepositMaster.setAttribute("ADDRESS3",UtilFunctions.getString(rsTmp, "ADDRESS3", ""));
                ObjSalesDepositMaster.setAttribute("CITY",UtilFunctions.getString(rsTmp, "CITY", ""));
                ObjSalesDepositMaster.setAttribute("PINCODE",UtilFunctions.getString(rsTmp, "PINCODE", ""));
                ObjSalesDepositMaster.setAttribute("CONTACT_NO",UtilFunctions.getString(rsTmp, "CONTACT_NO", ""));
                
                //Other Detail
                ObjSalesDepositMaster.setAttribute("SCHEME_ID",UtilFunctions.getString(rsTmp,"SCHEME_ID", ""));
                ObjSalesDepositMaster.setAttribute("DEPOSIT_TYPE_ID",UtilFunctions.getInt(rsTmp, "DEPOSIT_TYPE_ID", 0));
                ObjSalesDepositMaster.setAttribute("DEPOSITER_STATUS",UtilFunctions.getInt(rsTmp, "DEPOSITER_STATUS", 0));
                ObjSalesDepositMaster.setAttribute("DEPOSITER_CATEGORY",UtilFunctions.getInt(rsTmp, "DEPOSITER_CATEGORY", 0));
                ObjSalesDepositMaster.setAttribute("DEPOSIT_PAYABLE_TO",UtilFunctions.getInt(rsTmp, "DEPOSIT_PAYABLE_TO", 0));
                
                ObjSalesDepositMaster.setAttribute("INTEREST_RATE",UtilFunctions.getDouble(rsTmp, "INTEREST_RATE", 0.0));
                ObjSalesDepositMaster.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsTmp, "MAIN_ACCOUNT_CODE", ""));
                ObjSalesDepositMaster.setAttribute("INTEREST_MAIN_CODE",UtilFunctions.getString(rsTmp, "INTEREST_MAIN_CODE", ""));
                ObjSalesDepositMaster.setAttribute("DEPOSITER_CATEGORY_OTHERS",UtilFunctions.getString(rsTmp, "DEPOSITER_CATEGORY_OTHERS", ""));
                
                ObjSalesDepositMaster.setAttribute("EFFECTIVE_DATE",UtilFunctions.getString(rsTmp, "EFFECTIVE_DATE","0000-00-00"));
                ObjSalesDepositMaster.setAttribute("REFUND_DATE",UtilFunctions.getString(rsTmp, "REFUND_DATE","0000-00-00"));
                ObjSalesDepositMaster.setAttribute("INT_CALC_DATE",UtilFunctions.getString(rsTmp,"INT_CALC_DATE","0000-00-00"));
                
                ObjSalesDepositMaster.setAttribute("TAX_EX_FORM_RECEIVED",UtilFunctions.getInt(rsTmp, "TAX_EX_FORM_RECEIVED", 0));
                ObjSalesDepositMaster.setAttribute("PARTY_CODE",UtilFunctions.getString(rsTmp, "PARTY_CODE", ""));
                ObjSalesDepositMaster.setAttribute("TDS_APPLICABLE",UtilFunctions.getInt(rsTmp, "TDS_APPLICABLE", 0));
                ObjSalesDepositMaster.setAttribute("PAN_NO",UtilFunctions.getString(rsTmp, "PAN_NO", ""));
                ObjSalesDepositMaster.setAttribute("PAN_DATE",UtilFunctions.getString(rsTmp, "PAN_DATE","0000-00-00"));
                ObjSalesDepositMaster.setAttribute("PARTICULARS",UtilFunctions.getString(rsTmp, "PARTICULARS", ""));
                
                //Bank Detail
                ObjSalesDepositMaster.setAttribute("CHEQUE_NO",UtilFunctions.getString(rsTmp, "CHEQUE_NO", ""));
                ObjSalesDepositMaster.setAttribute("CHEQUE_DATE",UtilFunctions.getString(rsTmp, "CHEQUE_DATE","0000-00-00"));
                ObjSalesDepositMaster.setAttribute("REALIZATION_DATE",UtilFunctions.getString(rsTmp, "REALIZATION_DATE","0000-00-00"));
                ObjSalesDepositMaster.setAttribute("AMOUNT",UtilFunctions.getDouble(rsTmp, "AMOUNT", 0.0));
                ObjSalesDepositMaster.setAttribute("FUND_TRANSFER_FROM",UtilFunctions.getString(rsTmp, "FUND_TRANSFER_FROM", ""));
                
                ObjSalesDepositMaster.setAttribute("BANK_MAIN_CODE",UtilFunctions.getString(rsTmp, "BANK_MAIN_CODE", ""));
                ObjSalesDepositMaster.setAttribute("BANK_NAME",UtilFunctions.getString(rsTmp, "BANK_NAME", ""));
                ObjSalesDepositMaster.setAttribute("BANK_ADDRESS",UtilFunctions.getString(rsTmp, "BANK_ADDRESS", ""));
                ObjSalesDepositMaster.setAttribute("BANK_CITY",UtilFunctions.getString(rsTmp, "BANK_CITY", ""));
                ObjSalesDepositMaster.setAttribute("BANK_PINCODE",UtilFunctions.getString(rsTmp, "BANK_PINCODE", ""));
                
                //Deposit Releated Information
                ObjSalesDepositMaster.setAttribute("DEPOSIT_STATUS",UtilFunctions.getInt(rsTmp, "DEPOSIT_STATUS", 0));
                
                //Approval Specific
                ObjSalesDepositMaster.setAttribute("CREATED_BY",UtilFunctions.getString(rsTmp, "CREATED_BY", ""));
                ObjSalesDepositMaster.setAttribute("CREATED_DATE",UtilFunctions.getString(rsTmp, "CREATED_DATE","0000-00-00"));
                ObjSalesDepositMaster.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsTmp, "MODIFIED_BY", ""));
                ObjSalesDepositMaster.setAttribute("MODIFIED_DATE",UtilFunctions.getString(rsTmp, "MODIFIED_DATE","0000-00-00"));
                
                ObjSalesDepositMaster.setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsTmp, "HIERARCHY_ID", 0));
                ObjSalesDepositMaster.setAttribute("APPROVED",UtilFunctions.getInt(rsTmp, "APPROVED",0));
                ObjSalesDepositMaster.setAttribute("APPROVED_DATE",UtilFunctions.getString(rsTmp, "APPROVED_DATE","0000-00-00"));
                ObjSalesDepositMaster.setAttribute("REJECTED",UtilFunctions.getInt(rsTmp, "REJECTED", 0));
                
                ObjSalesDepositMaster.setAttribute("REJECTED_DATE",UtilFunctions.getString(rsTmp, "REJECTED_DATE","0000-00-00"));
                ObjSalesDepositMaster.setAttribute("CHANGED",UtilFunctions.getInt(rsTmp, "CHANGED", 0));
                ObjSalesDepositMaster.setAttribute("CHANGED_DATE",UtilFunctions.getString(rsTmp, "CHANGED_DATE","0000-00-00"));
                ObjSalesDepositMaster.setAttribute("CANCELLED",UtilFunctions.getInt(rsTmp, "CANCELLED", 0));
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return ObjSalesDepositMaster;
    }
    
    public boolean Filter(String pCondition,int pCompanyID) {
        Ready=false;
        pCompanyID=EITLERPGLOBAL.gCompanyID;
        try {
            String strSql = "SELECT * FROM D_FD_SALES_DEPOSIT_MASTER " + pCondition ;
            // System.out.println(strSql);
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsSalesDepositMaster = Stmt.executeQuery(strSql);
            rsSalesDepositMaster.first();
            
            if(!rsSalesDepositMaster.first()) {
                strSql = "SELECT * FROM D_FD_SALES_DEPOSIT_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" ORDER BY RECEIPT_NO ";
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
    
    public static HashMap getHistoryList(int pCompanyID,String pDocNo) {
        HashMap List=new HashMap();
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            
            tmpConn=data.getConn(FinanceGlobal.FinURL);
            stTmp=tmpConn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            
            rsTmp=stTmp.executeQuery("SELECT * FROM D_FD_SALES_DEPOSIT_MASTER_H WHERE COMPANY_ID="+pCompanyID+" AND RECEIPT_NO='"+pDocNo+"'");
            while(rsTmp.next()) {
                clsSalesDepositMaster ObjSalesDepositMaster=new clsSalesDepositMaster();
                
                ObjSalesDepositMaster.setAttribute("RECEIPT_NO",rsTmp.getString("RECEIPT_NO"));
                ObjSalesDepositMaster.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjSalesDepositMaster.setAttribute("UPDATED_BY",rsTmp.getString("UPDATED_BY"));
                ObjSalesDepositMaster.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjSalesDepositMaster.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjSalesDepositMaster.setAttribute("APPROVER_REMARKS",UtilFunctions.getString(rsTmp,"APPROVER_REMARKS", ""));
                
                List.put(Integer.toString(List.size()+1),ObjSalesDepositMaster);
            }
            rsTmp.close();
            stTmp.close();
            
            return List;
        }
        catch(Exception e) {
            //e.printStackTrace();
            return List;
        }
    }
    
    public static String getDocStatus(int pCompanyID,String pDocNo) {
        ResultSet rsTmp;
        String strMessage="";
        
        try {
            //First check that Document exist
            rsTmp=data.getResult("SELECT RECEIPT_NO,APPROVED,CANCELLED FROM D_FD_SALES_DEPOSIT_MASTER WHERE COMPANY_ID="+pCompanyID+" AND RECEIPT_NO='"+pDocNo+"'",FinanceGlobal.FinURL);
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
        } catch(Exception e) {
        }
        return strMessage;
    }
    
    public static HashMap getReceiptList(int CompanyID, String PartyCode) {
        HashMap List = new HashMap();
        String SQL = "SELECT * FROM D_FD_SALES_DEPOSIT_MASTER WHERE PARTY_CODE='"+PartyCode+"' AND DEPOSIT_STATUS=0 AND APPROVED=1 AND CANCELLED=0 ORDER BY EFFECTIVE_DATE";
        try {
            ResultSet rsList = data.getResult(SQL,FinanceGlobal.FinURL);
            int Counter = 0;
            while (!rsList.isAfterLast()) {
                clsSalesDepositRefundItem ObjItem = new clsSalesDepositRefundItem();
                Counter++;
                ObjItem.setAttribute("SR_NO",Counter);
                ObjItem.setAttribute("RECEIPT_NO",rsList.getString("RECEIPT_NO"));
                ObjItem.setAttribute("RECEIPT_DATE",EITLERPGLOBAL.formatDate(rsList.getString("RECEIPT_DATE")));
                ObjItem.setAttribute("EFFECTIVE_DATE",EITLERPGLOBAL.formatDate(rsList.getString("EFFECTIVE_DATE")));
                ObjItem.setAttribute("MAIN_ACCOUNT_CODE",rsList.getString("MAIN_ACCOUNT_CODE"));
                ObjItem.setAttribute("INTEREST_MAIN_CODE",rsList.getString("INTEREST_MAIN_CODE"));
                ObjItem.setAttribute("INTEREST_RATE",rsList.getDouble("INTEREST_RATE"));
                ObjItem.setAttribute("AMOUNT",rsList.getDouble("AMOUNT"));
                List.put(Integer.toString(Counter), ObjItem);
                rsList.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return List;
        }
        return List;
    }
    
    public boolean IsEditable(int pCompanyID,String pDocNo,int pUserID) {
        
        try {
            ResultSet rsTmp;
            Statement tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String strSQL1="SELECT COUNT(*) AS COUNT FROM D_FD_SALES_DEPOSIT_MASTER WHERE COMPANY_ID="+pCompanyID+" AND RECEIPT_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            if(HistoryView) {
                return false;
            }
            
            //First check that document is approved or not
            rsTmp=tmpStmt.executeQuery(strSQL1);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0) {//Item is Approved
                //Discard the request
                return false;
            } else {
                String strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+pDocNo+"' AND USER_ID="+pUserID+" AND STATUS='W'";
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
    
    public boolean SetData() {
        try{
            ResultSet rsCalcDetail=null;
            Connection tmpConn=data.getConn(FinanceGlobal.FinURL);
            
            //Statement tmpStmt=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            //HashMap List=new HashMap();
            long Counter=0;
            int RevNo=0;
            
            if(HistoryView) {
                RevNo=rsSalesDepositMaster.getInt("REVISION_NO");
                setAttribute("REVISION_NO",RevNo);
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            //Applicant Detail
            setAttribute("COMPANY_ID",UtilFunctions.getInt(rsSalesDepositMaster, "COMPANY_ID",0));
            setAttribute("RECEIPT_NO",UtilFunctions.getString(rsSalesDepositMaster,"RECEIPT_NO",""));
            setAttribute("RECEIPT_DATE",UtilFunctions.getString(rsSalesDepositMaster,"RECEIPT_DATE","0000-00-00"));
            setAttribute("TITLE",UtilFunctions.getString(rsSalesDepositMaster,"TITLE",""));
            setAttribute("APPLICANT_NAME",UtilFunctions.getString(rsSalesDepositMaster,"APPLICANT_NAME",""));
            setAttribute("ADDRESS1",UtilFunctions.getString(rsSalesDepositMaster,"ADDRESS1",""));
            setAttribute("ADDRESS2",UtilFunctions.getString(rsSalesDepositMaster,"ADDRESS2",""));
            setAttribute("ADDRESS3",UtilFunctions.getString(rsSalesDepositMaster, "ADDRESS3",""));
            setAttribute("CITY",UtilFunctions.getString(rsSalesDepositMaster,"CITY",""));
            setAttribute("PINCODE",UtilFunctions.getString(rsSalesDepositMaster,"PINCODE",""));
            setAttribute("CONTACT_NO",UtilFunctions.getString(rsSalesDepositMaster,"CONTACT_NO",""));
            
            //Other Detail
            setAttribute("SCHEME_ID",UtilFunctions.getString(rsSalesDepositMaster,"SCHEME_ID",""));
            setAttribute("DEPOSIT_TYPE_ID",UtilFunctions.getInt(rsSalesDepositMaster, "DEPOSIT_TYPE_ID",0));
            setAttribute("DEPOSIT_PAYABLE_TO",UtilFunctions.getInt(rsSalesDepositMaster, "DEPOSIT_PAYABLE_TO",0));
            setAttribute("DEPOSITER_STATUS",UtilFunctions.getInt( rsSalesDepositMaster,"DEPOSITER_STATUS",0));
            setAttribute("DEPOSITER_CATEGORY",UtilFunctions.getInt( rsSalesDepositMaster,"DEPOSITER_CATEGORY",0));
            
            setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsSalesDepositMaster,"MAIN_ACCOUNT_CODE",""));
            setAttribute("INTEREST_MAIN_CODE",UtilFunctions.getString(rsSalesDepositMaster,"INTEREST_MAIN_CODE",""));
            setAttribute("INTEREST_RATE",UtilFunctions.getDouble(rsSalesDepositMaster, "INTEREST_RATE",0.0));
            setAttribute("DEPOSITER_CATEGORY_OTHERS",UtilFunctions.getString( rsSalesDepositMaster,"DEPOSITER_CATEGORY_OTHERS",""));
            
            setAttribute("EFFECTIVE_DATE",UtilFunctions.getString( rsSalesDepositMaster,"EFFECTIVE_DATE","0000-00-00"));
            setAttribute("REFUND_DATE",UtilFunctions.getString( rsSalesDepositMaster,"REFUND_DATE","0000-00-00"));
            setAttribute("INT_CALC_DATE",UtilFunctions.getString( rsSalesDepositMaster,"INT_CALC_DATE","0000-00-00"));
            
            setAttribute("TAX_EX_FORM_RECEIVED",UtilFunctions.getInt( rsSalesDepositMaster,"TAX_EX_FORM_RECEIVED",0));
            setAttribute("PARTY_CODE",UtilFunctions.getString( rsSalesDepositMaster,"PARTY_CODE",""));
            setAttribute("TDS_APPLICABLE",UtilFunctions.getInt( rsSalesDepositMaster,"TDS_APPLICABLE",0));
            setAttribute("PAN_NO",UtilFunctions.getString(rsSalesDepositMaster, "PAN_NO",""));
            setAttribute("PAN_DATE",UtilFunctions.getString( rsSalesDepositMaster,"PAN_DATE","0000-00-00"));
            setAttribute("PARTICULARS",UtilFunctions.getString( rsSalesDepositMaster,"PARTICULARS",""));
            
            //Bank Detail
            setAttribute("CHEQUE_NO",UtilFunctions.getString( rsSalesDepositMaster,"CHEQUE_NO",""));
            setAttribute("CHEQUE_DATE",UtilFunctions.getString( rsSalesDepositMaster,"CHEQUE_DATE","0000-00-00"));
            setAttribute("REALIZATION_DATE",UtilFunctions.getString( rsSalesDepositMaster,"REALIZATION_DATE","0000-00-00"));
            setAttribute("AMOUNT",UtilFunctions.getDouble( rsSalesDepositMaster,"AMOUNT",0.0));
            setAttribute("FUND_TRANSFER_FROM",UtilFunctions.getString( rsSalesDepositMaster,"FUND_TRANSFER_FROM",""));
            
            setAttribute("BANK_MAIN_CODE",UtilFunctions.getString( rsSalesDepositMaster,"BANK_MAIN_CODE",""));
            setAttribute("BANK_NAME",UtilFunctions.getString( rsSalesDepositMaster,"BANK_NAME",""));
            setAttribute("BANK_ADDRESS",UtilFunctions.getString( rsSalesDepositMaster,"BANK_ADDRESS",""));
            setAttribute("BANK_CITY",UtilFunctions.getString( rsSalesDepositMaster,"BANK_CITY",""));
            setAttribute("BANK_PINCODE",UtilFunctions.getString( rsSalesDepositMaster,"BANK_PINCODE",""));
            
            //Deposit Releated Information
            setAttribute("DEPOSIT_STATUS",UtilFunctions.getInt(rsSalesDepositMaster,"DEPOSIT_STATUS",0));
            
            //Approval Specific
            setAttribute("CREATED_BY",UtilFunctions.getString(rsSalesDepositMaster,"CREATED_BY",""));
            setAttribute("CREATED_DATE",UtilFunctions.getString(rsSalesDepositMaster,"CREATED_DATE","0000-00-00"));
            setAttribute("MODIFIED_BY",UtilFunctions.getString(rsSalesDepositMaster,"MODIFIED_BY",""));
            setAttribute("MODIFIED_DATE",UtilFunctions.getString(rsSalesDepositMaster,"MODIFIED_DATE","0000-00-00"));
            setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsSalesDepositMaster,"HIERARCHY_ID",0));
            setAttribute("APPROVED",UtilFunctions.getInt(rsSalesDepositMaster,"APPROVED",0));
            setAttribute("APPROVED_DATE",UtilFunctions.getString(rsSalesDepositMaster,"APPROVED_DATE","0000-00-00"));
            setAttribute("REJECTED",UtilFunctions.getInt(rsSalesDepositMaster,"REJECTED",0));
            setAttribute("REJECTED_DATE",UtilFunctions.getString(rsSalesDepositMaster,"REJECTED_DATE","0000-00-00"));
            setAttribute("REJECTED_REMARKS",UtilFunctions.getString(rsSalesDepositMaster,"REJECTED_REMARKS",""));
            setAttribute("CHANGED",UtilFunctions.getInt(rsSalesDepositMaster,"CHANGED",0));
            setAttribute("CHANGED_DATE",UtilFunctions.getString(rsSalesDepositMaster,"CHANGED_DATE","0000-00-00"));
            setAttribute("CANCELLED",UtilFunctions.getInt(rsSalesDepositMaster,"CANCELLED",0));
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public  HashMap getList() {
        Connection tmpConn;
        ResultSet rsTmp=null;
        Statement tmpStmt=null;
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            tmpConn=data.getConn(FinanceGlobal.FinURL);
            tmpStmt=tmpConn.createStatement();
            rsTmp=tmpStmt.executeQuery("SELECT SCHEME_ID, SCHEME_NAME FROM D_FD_SCHEME_MASTER WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" ORDER BY SCHEME_ID ");
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsSalesDepositMaster ObjSalesDepositMaster =new clsSalesDepositMaster();
                
                ObjSalesDepositMaster.setAttribute("SCHEME_ID",rsTmp.getString("SCHEME_ID"));
                ObjSalesDepositMaster.setAttribute("SCHEME_NAME",rsTmp.getString("SCHEME_NAME"));
                
                List.put(Long.toString(Counter),ObjSalesDepositMaster);
                rsTmp.next();
            }
            
            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();
        }
        catch(Exception e) {
        }
        return List;
    }
    
    public static HashMap getListD() {
        Connection tmpConn;
        ResultSet rsTmp=null;
        Statement tmpStmt=null;
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            tmpConn=data.getConn(FinanceGlobal.FinURL);
            tmpStmt=tmpConn.createStatement();
            rsTmp=tmpStmt.executeQuery("SELECT DEPOSIT_TYPE_ID,DEPOSIT_TYPE_NAME FROM D_FD_DEPOSIT_TYPE_MASTER");
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast()&&rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsSalesDepositMaster ObjSalesDepositMaster =new clsSalesDepositMaster();
                ObjSalesDepositMaster.setAttribute("DEPOSIT_TYPE_ID",rsTmp.getInt("DEPOSIT_TYPE_ID"));
                ObjSalesDepositMaster.setAttribute("DEPOSIT_TYPE_NAME",rsTmp.getString("DEPOSIT_TYPE_NAME"));
                List.put(Long.toString(Counter),ObjSalesDepositMaster);
                rsTmp.next();
            }
            
            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();
        }
        catch(Exception e) {
        }
        return List;
    }
    
    private boolean PostReceiptVoucher(int CompanyID,String ReceiptNo) {
        try {
            clsVoucher objVoucher=new clsVoucher();
            
            String SelPrefix="";
            String SelSuffix="";
            int FFNo=0;
            /*========== Select the Hierarchy ======== */
            setAttribute("FIN_HIERARCHY_ID",0);
            HashMap List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesDepositMaster.ModuleID, "CHOOSE_HIERARCHY", "DEFAULT", "1");
            
            if(List.size()>0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                int HierarchyID=UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                setAttribute("FIN_HIERARCHY_ID",HierarchyID);
            }
            /*========== End of Hierarchy Selection ======== */
            
            /*========== Select Prifix, Suffix and Firstfree No. ======== */
            ResultSet rsTmp=data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID="+clsVoucher.ReceiptVoucherModuleID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                SelPrefix=rsTmp.getString("PREFIX_CHARS");
                SelSuffix=rsTmp.getString("SUFFIX_CHARS");
                FFNo=rsTmp.getInt("FIRSTFREE_NO");
            }
            /*========== End of Prifix, Suffix and Firstfree No. ======== */
            
            //============= Gethering Data =================//
            int VoucherSrNo=0;
            String MainAccountCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL);
            String PartyCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL);
            String BankMainCode = data.getStringValueFromDB("SELECT BANK_MAIN_CODE FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL);
            BookCode = data.getStringValueFromDB("SELECT BOOK_CODE FROM D_FIN_BOOK_MASTER WHERE MAIN_ACCOUNT_CODE='"+BankMainCode+"' ",FinanceGlobal.FinURL);
            double Amount = data.getDoubleValueFromDB("SELECT AMOUNT FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL);
            String ChequeNo = data.getStringValueFromDB("SELECT CHEQUE_NO FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL);
            String ChequeDate = data.getStringValueFromDB("SELECT CHEQUE_DATE FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL);
            String BankName = data.getStringValueFromDB("SELECT BOOK_NAME FROM D_FIN_BOOK_MASTER WHERE BOOK_CODE='"+BookCode+"' ",FinanceGlobal.FinURL);
            String RealizationDate = data.getStringValueFromDB("SELECT REALIZATION_DATE FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL);
            String ReceiptDate = data.getStringValueFromDB("SELECT RECEIPT_DATE FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL);
            //============= Gethering Data =======================//
            
            /*============== Preparing Voucher Header ================*/
            objVoucher=new clsVoucher();
            objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
            
            objVoucher.setAttribute("PREFIX",SelPrefix);
            objVoucher.setAttribute("SUFFIX",SelSuffix);
            objVoucher.setAttribute("FFNO",FFNo);
            objVoucher.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            objVoucher.setAttribute("VOUCHER_NO","");
            
            objVoucher.setAttribute("BOOK_CODE",BookCode);
            objVoucher.setAttribute("VOUCHER_TYPE",FinanceGlobal.TYPE_RECEIPT);
            objVoucher.setAttribute("CHEQUE_NO",ChequeNo);
            objVoucher.setAttribute("CHEQUE_DATE",EITLERPGLOBAL.formatDate(ChequeDate));
            objVoucher.setAttribute("BANK_NAME",BankName);
            objVoucher.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(RealizationDate));//
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
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",BankMainCode);
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
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
            objVoucherItem.setAttribute("MODULE_ID",clsSalesDepositMaster.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            VoucherSrNo++;
            objVoucherItem=new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT","C");
            objVoucherItem.setAttribute("ACCOUNT_ID",1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode);
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
            objVoucherItem.setAttribute("MODULE_ID",clsSalesDepositMaster.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            /*============== End of Voucher Detail =================*/
            
            if(objVoucher.Insert()) {
                return true;
            } else {
                return false;
            }
        }
        
        catch(Exception e) {
            e.printStackTrace();
            LastError=objVoucher.LastError;
            return false;
        }
    }
    
    private boolean PostJournalVoucher(int CompanyID,String ReceiptNo) {
        try {
            clsVoucher objVoucher=new clsVoucher();
            
            String SelPrefix="";
            String SelSuffix="";
            int FFNo=0;
            /*========== Select the Hierarchy ======== */
            setAttribute("FIN_HIERARCHY_ID",0);
            HashMap List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesDepositMaster.ModuleID, "CHOOSE_HIERARCHY", "DEFAULT", "2");
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
            String MainAccountCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL);
            String PartyCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL);
            String FundTransferFrom = data.getStringValueFromDB("SELECT FUND_TRANSFER_FROM FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL);
            List.clear();
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesDepositMaster.ModuleID, "GET_BOOK_CODE", "DEPOSIT_TRANSFER", "");
            if(List.size()>0) {
                //Get Book Code = 21 for Journal Voucher
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                BookCode = objRule.getAttribute("RULE_OUTCOME").getString();
            }
            double Amount = data.getDoubleValueFromDB("SELECT AMOUNT FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL);
            String BankName = data.getStringValueFromDB("SELECT BOOK_NAME FROM D_FIN_BOOK_MASTER WHERE MAIN_ACCOUNT_CODE='"+BookCode+"' ",FinanceGlobal.FinURL);
            String RealizationDate = data.getStringValueFromDB("SELECT REALIZATION_DATE FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL);
            String ReceiptDate = data.getStringValueFromDB("SELECT RECEIPT_DATE FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL);
            //============= Gethering Data =======================//
            
            /*============== Preparing Voucher Header ================*/
            objVoucher=new clsVoucher();
            objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
            
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
            objVoucher.setAttribute("VOUCHER_DATE",RealizationDate);//
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
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",FundTransferFrom);
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
            objVoucherItem.setAttribute("MODULE_ID",clsSalesDepositMaster.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            VoucherSrNo++;
            objVoucherItem=new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT","C");
            objVoucherItem.setAttribute("ACCOUNT_ID",1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode);
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
            objVoucherItem.setAttribute("MODULE_ID",clsSalesDepositMaster.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            /*============== End of Voucher Detail =================*/
            
            if(objVoucher.Insert()) {
                return true;
            } else {
                return false;
            }
        }
        
        catch(Exception e) {
            e.printStackTrace();
            LastError=objVoucher.LastError;
            return false;
        }
    }
    
    private boolean Validate() {
        try {
            
            if(!data.IsRecordExist("SELECT * FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+getAttribute("SCHEME_ID").getString()+"' AND DEPOSIT_TYPE_ID="+getAttribute("DEPOSIT_TYPE_ID").getInt(),FinanceGlobal.FinURL)) {
                LastError = "Please select appropriate deposit type according to Scheme ID.";
                return false;
            }
            
            if(getAttribute("TAX_EX_FORM_RECEIVED").getInt()==0) {
                if(getAttribute("TDS_APPLICABLE").getInt()==0) {
                    double otherInterestAmount = 0.0;
                    double currrentInterestAmount = 0.0;
                    otherInterestAmount = checkTDSAmount(getAttribute("PARTY_CODE").getString(),getAttribute("RECEIPT_NO").getString(),getAttribute("EFFECTIVE_DATE").getString());
                    
                    double Amount = getAttribute("AMOUNT").getDouble();
                    String SchemeID = getAttribute("SCHEME_ID").getString();
                    double Rate = getAttribute("INTEREST_RATE").getDouble();
                    String EffectiveDate = EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_DATE").getString());
                    String EndofFinYear = Integer.toString(EITLERPGLOBAL.getCurrentFinYear()+1) + "-3-31";
                    int diffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(EffectiveDate),java.sql.Date.valueOf(EndofFinYear))+1;
                    
                    GregorianCalendar cal = new GregorianCalendar();
                    //if (cal.isLeapYear(EITLERPGLOBAL.getCurrentFinYear()+1) ) {
                    if(cal.isLeapYear(EITLERPGLOBAL.getCurrentFinYear())) {
                        currrentInterestAmount = EITLERPGLOBAL.round((Amount *  Rate * diffofDays)/36600,0);
                    } else {
                        currrentInterestAmount = EITLERPGLOBAL.round((Amount *  Rate * diffofDays)/36500,0);
                    }
                    currrentInterestAmount += otherInterestAmount;
                    
                    if(currrentInterestAmount > 5000.0) {
                        LastError = "\nInterest Amount more then 5000 Per Annum. \nPlease check Either Tax Exemption Form Received or TDS Applicable.";
                        return false;
                    }
                }
            }
            
//            if(!data.IsRecordExist("SELECT PARTY_CODE FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+getAttribute("PARTY_CODE").getString()+"' AND MAIN_ACCOUNT_CODE='"+getAttribute("MAIN_ACCOUNT_CODE").getString()+"' ",FinanceGlobal.FinURL)) {
//                LastError = "Party Code("+ getAttribute("PARTY_CODE").getString() +") with main account code("+getAttribute("MAIN_ACCOUNT_CODE").getString()+") does not exists in party master. \nPlease varify Party code with main account code in party master.";
//                return false;
//            }
//            
//            if(!data.IsRecordExist("SELECT PARTY_CODE FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+getAttribute("PARTY_CODE").getString()+"' AND MAIN_ACCOUNT_CODE='"+getAttribute("INTEREST_MAIN_CODE").getString()+"' ",FinanceGlobal.FinURL)) {
//                LastError = "Party Code("+ getAttribute("PARTY_CODE").getString() +") with interest main account code("+getAttribute("INTEREST_MAIN_CODE").getString()+") does not exists in party master. \nPlease varify Party code with main account code in party master.";
//                return false;
//            }
            
            boolean isSubsidairyAccount = clsAccount.IsSubsidairyAccount(getAttribute("FUND_TRANSFER_FROM").getString());
            
            if(!getAttribute("FUND_TRANSFER_FROM").getString().equals("") && isSubsidairyAccount) {
                if(!data.IsRecordExist("SELECT PARTY_CODE FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+getAttribute("PARTY_CODE").getString()+"' AND MAIN_ACCOUNT_CODE='"+getAttribute("FUND_TRANSFER_FROM").getString()+"' ",FinanceGlobal.FinURL)) {
                    LastError = "Party Code("+ getAttribute("PARTY_CODE").getString() +") with main account code("+getAttribute("FUND_TRANSFER_FROM").getString()+") does not exists in party master. \nPlease varify Party code with main account code in party master.";
                    return false;
                }
            }
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public static double checkTDSAmount(String PartyCode, String ReceiptNo,String eDate) {
        double interestAmount = 0.0;
        String StartFinYear = EITLERPGLOBAL.getFinYearStartDate(eDate);
        String EndFinYear = EITLERPGLOBAL.getFinYearEndDate(eDate);
        String EffectiveDate = "";
        String CloseDate = "";
        int DiffofDays = 1;
        double Rate = 0.0;
        double Amount = 0.0;
        GregorianCalendar cal = new GregorianCalendar();
        String StartDate="";
        try {
            // Matured and Closed within financial year.
            ResultSet rsTDSClose = data.getResult("SELECT * FROM D_FD_SALES_DEPOSIT_MASTER WHERE DEPOSIT_STATUS=1 AND PARTY_CODE='"+PartyCode+"' AND APPROVED=1 AND CANCELLED=0 AND REFUND_DATE>='"+StartFinYear+"' AND REFUND_DATE<='"+EndFinYear+"' ",FinanceGlobal.FinURL);
            rsTDSClose.first();
            if(rsTDSClose.getRow() > 0 ) {
                while(!rsTDSClose.isAfterLast()) {
                    DiffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(StartFinYear), java.sql.Date.valueOf(CloseDate));
                    StartDate=StartFinYear;
                    if (cal.isLeapYear(Integer.parseInt(EITLERPGLOBAL.getFinYearEndDate(StartDate).substring(0,4)))) {
                    //if (cal.isLeapYear(EITLERPGLOBAL.getCurrentFinYear()+1) ) {
                        //if(cal.isLeapYear(EITLERPGLOBAL.getCurrentFinYear())) {
                        interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36600,0);
                    } else {
                        interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36500,0);
                    }
                    rsTDSClose.next();
                }
            }
            rsTDSClose.close();
            StartDate="";
            // Open within financial year.
            ResultSet rsTDSOpen = data.getResult("SELECT * FROM D_FD_SALES_DEPOSIT_MASTER WHERE DEPOSIT_STATUS=0 AND PARTY_CODE='"+PartyCode+"' AND RECEIPT_NO<>'"+ReceiptNo+"' AND APPROVED=1 AND CANCELLED=0 ",FinanceGlobal.FinURL);
            rsTDSOpen.first();
            if(rsTDSOpen.getRow() > 0 ) {
                while(!rsTDSOpen.isAfterLast()) {
                    Amount = rsTDSOpen.getDouble("AMOUNT");
                    Rate = rsTDSOpen.getDouble("INTEREST_RATE");
                    EffectiveDate = rsTDSOpen.getString("EFFECTIVE_DATE");
                    if(java.sql.Date.valueOf(EffectiveDate).before(java.sql.Date.valueOf(StartFinYear))) {
                        DiffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(StartFinYear), java.sql.Date.valueOf(EndFinYear))+1;
                        StartDate=StartFinYear;
                    } else {
                        DiffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(EffectiveDate), java.sql.Date.valueOf(EndFinYear))+1;
                        StartDate=EffectiveDate;
                    }
                    
                    if (cal.isLeapYear(Integer.parseInt(EITLERPGLOBAL.getFinYearEndDate(StartDate).substring(0,4)))) {
                    //if (cal.isLeapYear(EITLERPGLOBAL.getCurrentFinYear()+1) ) {
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
    
    public static double calculateTDSAmount(double intAmount,String ReceiptNo) {
        double TDSPercentage = 0;
        double TDSAmount = 0;
        try {
            int DepositerCategory = data.getIntValueFromDB("SELECT DEPOSITER_CATEGORY FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            String Type="";
            if(DepositerCategory==1) {
                Type = "COMPANY";
            } else {
                Type = "OTHER";
            }
            
            if(DepositerCategory==9) {
                Type = "NO_PANNO";
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
    
    public static String deductDays(String Date, int Days) {
        String DATE_FORMAT = "yyyy-MM-dd";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(Date.substring(0,4)),Integer.parseInt(Date.substring(5,7))-1, Integer.parseInt(Date.substring(8,10)));
        //System.out.println("Set Date : " + cal.getTime() +" ==== " + sdf.format(cal.getTime())) ;
        cal.add(Calendar.DATE,-1*Days);
        //System.out.println("1. After Substracting 1 Day : " + cal.getTime()+" ==== " + sdf.format(cal.getTime())) ;
        return sdf.format(cal.getTime());
    }
    
    public static HashMap getPendingApprovals(int CompanyID,int UserID,int Order, int FinYearFrom) {
        Connection tmpConn;
        ResultSet rsTmp=null;
        Statement tmpStmt=null;
        String strSQL="";
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            
            tmpConn=data.getConn(FinanceGlobal.FinURL);
            tmpStmt=tmpConn.createStatement();
            
            if(Order==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT FINANCE.D_FD_SALES_DEPOSIT_MASTER.RECEIPT_NO,FINANCE.D_FD_SALES_DEPOSIT_MASTER.RECEIPT_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_SALES_DEPOSIT_MASTER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_SALES_DEPOSIT_MASTER.RECEIPT_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FD_SALES_DEPOSIT_MASTER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FD_SALES_DEPOSIT_MASTER.COMPANY_ID="+CompanyID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+clsSalesDepositMaster.ModuleID+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT FINANCE.D_FD_SALES_DEPOSIT_MASTER.RECEIPT_NO,FINANCE.D_FD_SALES_DEPOSIT_MASTER.RECEIPT_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_SALES_DEPOSIT_MASTER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_SALES_DEPOSIT_MASTER.RECEIPT_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FD_SALES_DEPOSIT_MASTER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FD_SALES_DEPOSIT_MASTER.COMPANY_ID="+CompanyID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+clsSalesDepositMaster.ModuleID+" ORDER BY D_FD_SALES_DEPOSIT_MASTER.RECEIPT_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT FINANCE.D_FD_SALES_DEPOSIT_MASTER.RECEIPT_NO,FINANCE.D_FD_SALES_DEPOSIT_MASTER.RECEIPT_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_SALES_DEPOSIT_MASTER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_SALES_DEPOSIT_MASTER.RECEIPT_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FD_SALES_DEPOSIT_MASTER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FD_SALES_DEPOSIT_MASTER.COMPANY_ID="+CompanyID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+clsSalesDepositMaster.ModuleID+" ORDER BY D_FD_SALES_DEPOSIT_MASTER.RECEIPT_NO";
            }
            
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                if(EITLERPGLOBAL.isWithinDate(rsTmp.getString("RECEIPT_DATE"),FinYearFrom)) {
                    Counter=Counter+1;
                    clsDepositMaster ObjDoc=new clsDepositMaster();
                    
                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("RECEIPT_NO",rsTmp.getString("RECEIPT_NO"));
                    ObjDoc.setAttribute("RECEIPT_DATE",rsTmp.getString("RECEIPT_DATE"));
                    ObjDoc.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                    String PartyCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+rsTmp.getString("RECEIPT_NO")+"' ",FinanceGlobal.FinURL);
                    String PartyName = data.getStringValueFromDB("SELECT APPLICANT_NAME FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+rsTmp.getString("RECEIPT_NO")+"' ",FinanceGlobal.FinURL);
                    ObjDoc.setAttribute("DEPT_NAME",clsDepartment.getDeptName(CompanyID,clsUser.getDeptID(CompanyID, UserID)));
                    ObjDoc.setAttribute("PARTY_CODE",PartyCode);
                    ObjDoc.setAttribute("PARTY_NAME",PartyName);
                    // ----------------- End of Header Fields ------------------------------------//
                    
                    List.put(Long.toString(Counter),ObjDoc);
                }
                rsTmp.next();
            }//end of while
            
            rsTmp.close();
            tmpStmt.close();
            
        }
        catch(Exception e) {
        }
        
        return List;
    }
    
}//end of the class
