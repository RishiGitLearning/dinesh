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
 * @author Prathmesh Thando
 */

public class clsDepositMaster {
    public static int ModuleID=85;
    
    //*********** Local reference of data class ***************//
    private clsDepositMaster ObjDepositMaster;
    clsVoucher objVoucher;
    //private Connection Conn=null;
    
    public HashMap colHistory=new HashMap();
    Statement stDepositMaster, stDepositMasterH,stCalcDetail;
    ResultSet rsDepositMaster, rsDepositMasterH,rsCalcDetail;
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
    int EditMode=0;
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
    
    public clsDepositMaster() {
        LastError = "";
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("RECEIPT_NO",new Variant(""));
        props.put("LEGACY_NO",new Variant(""));
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
        
        props.put("APPLICANT2",new Variant(""));
        props.put("APPLICANT3",new Variant(""));
        props.put("APPLICANT4",new Variant(""));
        props.put("NOMINEE_1_NAME",new Variant(""));
        props.put("NOMINEE_2_NAME",new Variant(""));
        props.put("NOMINEE_3_NAME",new Variant(""));
        
        props.put("SCHEME_ID",new Variant(""));
        props.put("SCHEME_NAME",new Variant(""));
        props.put("DEPOSIT_TYPE_ID",new Variant(0));
        props.put("DEPOSIT_TYPE_NAME",new Variant(""));
        props.put("DEPOSITER_STATUS",new Variant(0));
        props.put("DEPOSITER_CATEGORY",new Variant(0));
        props.put("DEPOSIT_PAYABLE_TO",new Variant(0));
        
        props.put("INTEREST_MAIN_CODE",new Variant(""));
        props.put("MAIN_ACCOUNT_CODE",new Variant(""));
        props.put("DEPOSIT_PERIOD",new Variant(0));
        props.put("INTEREST_RATE",new Variant(0.0));
        props.put("DEPOSITER_CATEGORY_OTHERS",new Variant(""));
        props.put("FOLIO_NO",new Variant(""));
        props.put("EMPLOYEE_CODE",new Variant(""));
        
        props.put("EFFECTIVE_DATE",new Variant(""));
        props.put("MATURITY_DATE",new Variant(""));
        props.put("INT_CALC_DATE",new Variant(""));
        
        props.put("TAX_EX_FORM_RECEIVED",new Variant(0));
        props.put("PARTY_CODE",new Variant(""));
        props.put("TDS_APPLICABLE",new Variant(0));
        props.put("OLD_RECEIPT_NO",new Variant(""));
        props.put("PAN_NO",new Variant(""));
        props.put("PAN_DATE",new Variant(""));
        props.put("PARTICULARS",new Variant(""));
        
        props.put("CHEQUE_NO",new Variant(""));
        props.put("CHEQUE_DATE",new Variant(""));
        props.put("REALIZATION_DATE",new Variant(""));
        props.put("DEPOSIT_DATE",new Variant(""));
        props.put("AMOUNT",new Variant(0.0));
        
        props.put("BANK_MAIN_CODE",new Variant(""));
        props.put("BANK_NAME",new Variant(""));
        props.put("BANK_ADDRESS",new Variant(""));
        props.put("BANK_CITY",new Variant(""));
        props.put("BANK_PINCODE",new Variant(""));
        
        props.put("BROKER_CODE",new Variant(""));
        props.put("BROKER_NAME",new Variant(""));
        props.put("BROKER_ADDRESS",new Variant(""));
        props.put("BROKER_CITY",new Variant(""));
        props.put("BROKER_PINCODE",new Variant(""));
        
        props.put("DEPOSIT_ENTRY_TYPE",new Variant(0));
        props.put("PREMATURE",new Variant(0));
        props.put("PM_DATE",new Variant(""));
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
            rsDepositMaster=Stmt.executeQuery("SELECT * FROM D_FD_DEPOSIT_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" ");/*EITLERPGLOBAL.gCompanyID....*/
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
            rsDepositMaster.close();
        }
        catch(Exception e) {
        }
    }
    
    //Navigation Methods
    public boolean MoveFirst() {
        try {
            rsDepositMaster.first();
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
            if(rsDepositMaster.isAfterLast()||rsDepositMaster.isLast()) {
                //Move pointer at last record
                //If it is beyond eof
                rsDepositMaster.last();
            }
            else {
                rsDepositMaster.next();
                if(rsDepositMaster.isAfterLast()) {
                    rsDepositMaster.last();
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
            if(rsDepositMaster.isFirst()||rsDepositMaster.isBeforeFirst()) {
                rsDepositMaster.first();
            }
            else {
                rsDepositMaster.previous();
                if(rsDepositMaster.isBeforeFirst()) {
                    rsDepositMaster.first();
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
            rsDepositMaster.last();
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
            
            stDepositMaster = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsDepositMaster = stDepositMaster.executeQuery("SELECT * FROM D_FD_DEPOSIT_MASTER LIMIT 1");
            
            //===== History Related Changes =====//
            stDepositMasterH = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsDepositMasterH = stDepositMasterH.executeQuery("SELECT * FROM D_FD_DEPOSIT_MASTER_H WHERE RECEIPT_NO='1'");
            //===================================//
            EditMode=1;
            if(!Validate()) {
                return false;
            }
            
            if(data.IsRecordExist("SELECT RECEIPT_NO FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+getAttribute("RECEIPT_NO").getString()+"'",FinanceGlobal.FinURL)) {
                LastError="Deposit with this Receipt No already exist.";
                return false;
            }
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //=============================Generate New ReceiptNo.===============================
            setAttribute("RECEIPT_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,85,(int)getAttribute("FFNO").getVal(),true));
            //===============================================================================================
            
            //****************** Insert Records ********************//
            rsDepositMaster.moveToInsertRow();
            //Applicant Detail
            rsDepositMaster.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            int CompanyID = EITLERPGLOBAL.gCompanyID;
            rsDepositMaster.updateString("RECEIPT_NO",getAttribute("RECEIPT_NO").getString());
            String ReceiptNo = getAttribute("RECEIPT_NO").getString();
            rsDepositMaster.updateString("LEGACY_NO",getAttribute("LEGACY_NO").getString());
            rsDepositMaster.updateString("RECEIPT_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("RECEIPT_DATE").getString()));
            rsDepositMaster.updateString("TITLE", getAttribute("TITLE").getString());
            rsDepositMaster.updateString("APPLICANT_NAME", getAttribute("APPLICANT_NAME").getString());
            rsDepositMaster.updateString("ADDRESS1", getAttribute("ADDRESS1").getString());
            rsDepositMaster.updateString("ADDRESS2", getAttribute("ADDRESS2").getString());
            rsDepositMaster.updateString("ADDRESS3", getAttribute("ADDRESS3").getString());
            rsDepositMaster.updateString("CITY", getAttribute("CITY").getString());
            rsDepositMaster.updateString("PINCODE", getAttribute("PINCODE").getString());
            rsDepositMaster.updateString("CONTACT_NO", getAttribute("CONTACT_NO").getString());
            rsDepositMaster.updateString("APPLICANT2", getAttribute("APPLICANT2").getString());
            rsDepositMaster.updateString("APPLICANT3", getAttribute("APPLICANT3").getString());
            rsDepositMaster.updateString("APPLICANT4", getAttribute("APPLICANT4").getString());
            rsDepositMaster.updateString("NOMINEE_1_NAME", getAttribute("NOMINEE_1_NAME").getString());
            rsDepositMaster.updateString("NOMINEE_2_NAME", getAttribute("NOMINEE_2_NAME").getString());
            rsDepositMaster.updateString("NOMINEE_3_NAME", getAttribute("NOMINEE_3_NAME").getString());
            
            //Other Detail
            rsDepositMaster.updateString("SCHEME_ID", getAttribute("SCHEME_ID").getString());
            rsDepositMaster.updateInt("DEPOSITER_STATUS", getAttribute("DEPOSITER_STATUS").getInt());
            rsDepositMaster.updateInt("DEPOSITER_CATEGORY", getAttribute("DEPOSITER_CATEGORY").getInt());
            rsDepositMaster.updateInt("DEPOSIT_TYPE_ID", getAttribute("DEPOSIT_TYPE_ID").getInt());
            rsDepositMaster.updateInt("DEPOSIT_PAYABLE_TO", getAttribute("DEPOSIT_PAYABLE_TO").getInt());
            
            rsDepositMaster.updateString("MAIN_ACCOUNT_CODE", getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsDepositMaster.updateString("INTEREST_MAIN_CODE", getAttribute("INTEREST_MAIN_CODE").getString());
            rsDepositMaster.updateInt("DEPOSIT_PERIOD", getAttribute("DEPOSIT_PERIOD").getInt());
            rsDepositMaster.updateDouble("INTEREST_RATE", getAttribute("INTEREST_RATE").getDouble());
            
            rsDepositMaster.updateString("DEPOSITER_CATEGORY_OTHERS", getAttribute("DEPOSITER_CATEGORY_OTHERS").getString());
            rsDepositMaster.updateString("FOLIO_NO", getAttribute("FOLIO_NO").getString());
            rsDepositMaster.updateString("EMPLOYEE_CODE", getAttribute("EMPLOYEE_CODE").getString());
            rsDepositMaster.updateString("EFFECTIVE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_DATE").getString()));
            rsDepositMaster.updateString("MATURITY_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("MATURITY_DATE").getString()));
            rsDepositMaster.updateString("INT_CALC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("INT_CALC_DATE").getString()));
            
            rsDepositMaster.updateInt("TAX_EX_FORM_RECEIVED",getAttribute("TAX_EX_FORM_RECEIVED").getInt());
            rsDepositMaster.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsDepositMaster.updateInt("TDS_APPLICABLE", getAttribute("TDS_APPLICABLE").getInt());
            rsDepositMaster.updateString("PAN_NO", getAttribute("PAN_NO").getString());
            rsDepositMaster.updateString("PAN_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PAN_DATE").getString()));
            rsDepositMaster.updateString("OLD_RECEIPT_NO", getAttribute("OLD_RECEIPT_NO").getString());  // Blank for new entry
            rsDepositMaster.updateString("PARTICULARS", getAttribute("PARTICULARS").getString());
            
            //Bank Detail
            rsDepositMaster.updateString("CHEQUE_NO", getAttribute("CHEQUE_NO").getString());
            rsDepositMaster.updateString("CHEQUE_DATE", EITLERPGLOBAL.formatDateDB( getAttribute("CHEQUE_DATE").getString()));
            rsDepositMaster.updateString("REALIZATION_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("REALIZATION_DATE").getString()));
            rsDepositMaster.updateString("DEPOSIT_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DEPOSIT_DATE").getString()));
            rsDepositMaster.updateDouble("AMOUNT", getAttribute("AMOUNT").getDouble());
            
            rsDepositMaster.updateString("BANK_MAIN_CODE", getAttribute("BANK_MAIN_CODE").getString());
            rsDepositMaster.updateString("BANK_NAME", getAttribute("BANK_NAME").getString());
            rsDepositMaster.updateString("BANK_ADDRESS", getAttribute("BANK_ADDRESS").getString());
            rsDepositMaster.updateString("BANK_CITY", getAttribute("BANK_CITY").getString());
            rsDepositMaster.updateString("BANK_PINCODE", getAttribute("BANK_PINCODE").getString());
            
            rsDepositMaster.updateString("BROKER_CODE", getAttribute("BROKER_CODE").getString());
            rsDepositMaster.updateString("BROKER_NAME", getAttribute("BROKER_NAME").getString());
            rsDepositMaster.updateString("BROKER_ADDRESS", getAttribute("BROKER_ADDRESS").getString());
            rsDepositMaster.updateString("BROKER_CITY", getAttribute("BROKER_CITY").getString());
            rsDepositMaster.updateString("BROKER_PINCODE", getAttribute("BROKER_PINCODE").getString());
            
            //Deposit Releated Information
            rsDepositMaster.updateInt("DEPOSIT_ENTRY_TYPE",getAttribute("DEPOSIT_ENTRY_TYPE").getInt()); // Always 1. for a new entry 2. for renewed
            rsDepositMaster.updateInt("PREMATURE", getAttribute("PREMATURE").getInt());     // 0 for a new entry
            rsDepositMaster.updateString("PM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PM_DATE").getString()));   // Blank for new entry
            rsDepositMaster.updateInt("DEPOSIT_STATUS", getAttribute("DEPOSIT_STATUS").getInt()); //Status 0. For OPEN (new Entry) 1. For close
            
            //Approval Specific
            rsDepositMaster.updateBoolean("APPROVED",false);
            rsDepositMaster.updateString("APPROVED_DATE","0000-00-00");
            rsDepositMaster.updateBoolean("REJECTED", false);
            rsDepositMaster.updateString("REJECTED_DATE", "0000-00-00");
            rsDepositMaster.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsDepositMaster.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
            rsDepositMaster.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsDepositMaster.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
            rsDepositMaster.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            
            rsDepositMaster.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            rsDepositMaster.updateBoolean("CHANGED", true);
            rsDepositMaster.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsDepositMaster.updateBoolean("CANCELLED", false);
            rsDepositMaster.insertRow();
            
            
            //******************** History Update ************************//
            
            rsDepositMasterH.moveToInsertRow() ;
            rsDepositMasterH.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsDepositMasterH.updateInt("REVISION_NO", 1); //Revision No. 1 in case of Insert
            rsDepositMasterH.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsDepositMasterH.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsDepositMasterH.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsDepositMasterH.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            
            //Applicant Detail
            rsDepositMasterH.updateString("RECEIPT_NO", getAttribute("RECEIPT_NO").getString());
            rsDepositMasterH.updateString("LEGACY_NO", getAttribute("LEGACY_NO").getString());
            rsDepositMasterH.updateString("RECEIPT_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("RECEIPT_DATE").getString()));
            rsDepositMasterH.updateString("TITLE", getAttribute("TITLE").getString());
            rsDepositMasterH.updateString("APPLICANT_NAME", getAttribute("APPLICANT_NAME").getString());
            
            rsDepositMasterH.updateString("ADDRESS1", getAttribute("ADDRESS1").getString());
            rsDepositMasterH.updateString("ADDRESS2", getAttribute("ADDRESS2").getString());
            rsDepositMasterH.updateString("ADDRESS3", getAttribute("ADDRESS3").getString());
            rsDepositMasterH.updateString("CITY", getAttribute("CITY").getString());
            rsDepositMasterH.updateString("PINCODE", getAttribute("PINCODE").getString());
            rsDepositMasterH.updateString("CONTACT_NO", getAttribute("CONTACT_NO").getString());
            rsDepositMasterH.updateString("APPLICANT2", getAttribute("APPLICANT2").getString());
            rsDepositMasterH.updateString("APPLICANT3", getAttribute("APPLICANT3").getString());
            rsDepositMasterH.updateString("APPLICANT4", getAttribute("APPLICANT4").getString());
            rsDepositMasterH.updateString("NOMINEE_1_NAME", getAttribute("NOMINEE_1_NAME").getString());
            rsDepositMasterH.updateString("NOMINEE_2_NAME", getAttribute("NOMINEE_2_NAME").getString());
            rsDepositMasterH.updateString("NOMINEE_3_NAME", getAttribute("NOMINEE_3_NAME").getString());
            
            //Other Detail
            rsDepositMasterH.updateString("SCHEME_ID", getAttribute("SCHEME_ID").getString());
            rsDepositMasterH.updateInt("DEPOSITER_STATUS", getAttribute("DEPOSITER_STATUS").getInt());
            rsDepositMasterH.updateInt("DEPOSITER_CATEGORY", getAttribute("DEPOSITER_CATEGORY").getInt());
            rsDepositMasterH.updateInt("DEPOSIT_TYPE_ID", getAttribute("DEPOSIT_TYPE_ID").getInt());
            rsDepositMasterH.updateInt("DEPOSIT_PAYABLE_TO", getAttribute("DEPOSIT_PAYABLE_TO").getInt());
            
            rsDepositMasterH.updateInt("DEPOSIT_PERIOD", getAttribute("DEPOSIT_PERIOD").getInt());
            rsDepositMasterH.updateDouble("INTEREST_RATE", getAttribute("INTEREST_RATE").getDouble());
            rsDepositMasterH.updateString("MAIN_ACCOUNT_CODE", getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsDepositMasterH.updateString("INTEREST_MAIN_CODE", getAttribute("INTEREST_MAIN_CODE").getString());
            rsDepositMasterH.updateString("DEPOSITER_CATEGORY_OTHERS", getAttribute("DEPOSITER_CATEGORY_OTHERS").getString());
            rsDepositMasterH.updateString("FOLIO_NO", getAttribute("FOLIO_NO").getString());
            rsDepositMasterH.updateString("EMPLOYEE_CODE", getAttribute("EMPLOYEE_CODE").getString());
            
            rsDepositMasterH.updateString("EFFECTIVE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_DATE").getString()));
            rsDepositMasterH.updateString("MATURITY_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("MATURITY_DATE").getString()));
            rsDepositMasterH.updateString("INT_CALC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("INT_CALC_DATE").getString()));
            
            rsDepositMasterH.updateInt("TAX_EX_FORM_RECEIVED", getAttribute("TAX_EX_FORM_RECEIVED").getInt());
            rsDepositMasterH.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsDepositMasterH.updateInt("TDS_APPLICABLE", getAttribute("TDS_APPLICABLE").getInt());
            rsDepositMasterH.updateString("OLD_RECEIPT_NO", getAttribute("OLD_RECEIPT_NO").getString());
            rsDepositMasterH.updateString("PAN_NO", getAttribute("PAN_NO").getString());
            rsDepositMasterH.updateString("PAN_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PAN_DATE").getString()));
            rsDepositMasterH.updateString("PARTICULARS", getAttribute("PARTICULARS").getString());
            
            //Bank Detail
            rsDepositMasterH.updateString("CHEQUE_NO", getAttribute("CHEQUE_NO").getString());
            rsDepositMasterH.updateString("CHEQUE_DATE", EITLERPGLOBAL.formatDateDB( getAttribute("CHEQUE_DATE").getString()));
            rsDepositMasterH.updateString("REALIZATION_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("REALIZATION_DATE").getString()));
            rsDepositMasterH.updateString("DEPOSIT_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DEPOSIT_DATE").getString()));
            rsDepositMasterH.updateDouble("AMOUNT", getAttribute("AMOUNT").getDouble());
            
            rsDepositMasterH.updateString("BANK_MAIN_CODE", getAttribute("BANK_MAIN_CODE").getString());
            rsDepositMasterH.updateString("BANK_NAME", getAttribute("BANK_NAME").getString());
            rsDepositMasterH.updateString("BANK_ADDRESS", getAttribute("BANK_ADDRESS").getString());
            rsDepositMasterH.updateString("BANK_CITY", getAttribute("BANK_CITY").getString());
            rsDepositMasterH.updateString("BANK_PINCODE", getAttribute("BANK_PINCODE").getString());
            
            rsDepositMasterH.updateString("BROKER_CODE", getAttribute("BROKER_CODE").getString());
            rsDepositMasterH.updateString("BROKER_NAME", getAttribute("BROKER_NAME").getString());
            rsDepositMasterH.updateString("BROKER_ADDRESS", getAttribute("BROKER_ADDRESS").getString());
            rsDepositMasterH.updateString("BROKER_CITY", getAttribute("BROKER_CITY").getString());
            rsDepositMasterH.updateString("BROKER_PINCODE", getAttribute("BROKER_PINCODE").getString());
            
            //Deposit Releated Information
            rsDepositMasterH.updateInt("DEPOSIT_ENTRY_TYPE",getAttribute("DEPOSIT_ENTRY_TYPE").getInt()); // Always 1. for a new entry 2. for renewed
            rsDepositMasterH.updateInt("PREMATURE", getAttribute("PREMATURE").getInt());     // 0 for a new entry
            rsDepositMasterH.updateString("PM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PM_DATE").getString()));   // Blank for new entry
            rsDepositMasterH.updateInt("DEPOSIT_STATUS", getAttribute("DEPOSIT_STATUS").getInt()); //Status 0. For OPEN (new Entry) 1. For close
            
            //Approval Specific
            rsDepositMasterH.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
            rsDepositMasterH.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsDepositMasterH.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
            rsDepositMasterH.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsDepositMasterH.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            
            rsDepositMasterH.updateBoolean("APPROVED", false);
            rsDepositMasterH.updateString("APPROVED_DATE", "0000-00-00");
            rsDepositMasterH.updateBoolean("REJECTED", false);
            rsDepositMasterH.updateString("REJECTED_DATE", "0000-00-00");
            rsDepositMasterH.updateBoolean("CHANGED", true);
            rsDepositMasterH.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsDepositMasterH.updateBoolean("CANCELLED", false);
            rsDepositMasterH.insertRow();
            //*************************************************************//
            
            
            //************ Update Approval Flow *****************//
            ObjFlow.CompanyID =getAttribute("COMPANY_ID").getInt();
            ObjFlow.ModuleID = this.ModuleID ;
            ObjFlow.DocNo = getAttribute("RECEIPT_NO").getString();
            ObjFlow.From =getAttribute("FROM").getInt();
            ObjFlow.To = getAttribute("TO").getInt();
            ObjFlow.Status = getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName = "D_FD_DEPOSIT_MASTER";
            ObjFlow.IsCreator = true;
            ObjFlow.HierarchyID =getAttribute("HIERARCHY_ID").getInt();
            ObjFlow.Remarks =getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName ="RECEIPT_NO";
            ObjFlow.UseSpecifiedURL=true;
            ObjFlow.SpecificURL=FinanceGlobal.FinURL;
            ObjFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
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
            
            MoveLast();
            /*******************************VOUCHER POSTING******************************/
            //String ReceiptNo = getAttribute("RECEIPT_NO").getString();
            //int CompanyID = getAttribute("COMPANY_ID").getInt();
            if(ObjFlow.Status.equals("F")) {
                if(!PostVoucher(CompanyID,ReceiptNo)) {
                    LastError = " Voucher Not Pasted.";
                    return false;
                }
            }
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
            EditMode=2;
            if(!Validate()) {
                return false;
            }
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            String AStatus=getAttribute("APPROVAL_STATUS").getString();
            if(AStatus.equals("R")) {
                Validate=false; //Do not validate while in rejection mode.
            }
            
            stDepositMasterH = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsDepositMasterH = stDepositMasterH.executeQuery("SELECT * FROM D_FD_DEPOSIT_MASTER_H ");
            
            //****************** Update Records ********************//
            //Applicant Detail
            rsDepositMaster.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            int CompanyID = EITLERPGLOBAL.gCompanyID;
            rsDepositMaster.updateString("RECEIPT_NO", getAttribute("RECEIPT_NO").getString());
            String ReceiptNo = getAttribute("RECEIPT_NO").getString();
            rsDepositMaster.updateString("LEGACY_NO", getAttribute("LEGACY_NO").getString());
            rsDepositMaster.updateString("RECEIPT_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("RECEIPT_DATE").getString()));
            rsDepositMaster.updateString("TITLE", getAttribute("TITLE").getString());
            rsDepositMaster.updateString("APPLICANT_NAME", getAttribute("APPLICANT_NAME").getString());
            
            rsDepositMaster.updateString("ADDRESS1", getAttribute("ADDRESS1").getString());
            rsDepositMaster.updateString("ADDRESS2", getAttribute("ADDRESS2").getString());
            rsDepositMaster.updateString("ADDRESS3", getAttribute("ADDRESS3").getString());
            rsDepositMaster.updateString("CITY", getAttribute("CITY").getString());
            rsDepositMaster.updateString("PINCODE", getAttribute("PINCODE").getString());
            rsDepositMaster.updateString("CONTACT_NO", getAttribute("CONTACT_NO").getString());
            rsDepositMaster.updateString("APPLICANT2", getAttribute("APPLICANT2").getString());
            rsDepositMaster.updateString("APPLICANT3", getAttribute("APPLICANT3").getString());
            rsDepositMaster.updateString("APPLICANT4", getAttribute("APPLICANT4").getString());
            rsDepositMaster.updateString("NOMINEE_1_NAME", getAttribute("NOMINEE_1_NAME").getString());
            rsDepositMaster.updateString("NOMINEE_2_NAME", getAttribute("NOMINEE_2_NAME").getString());
            rsDepositMaster.updateString("NOMINEE_3_NAME", getAttribute("NOMINEE_3_NAME").getString());
            
            //Other Detail
            rsDepositMaster.updateString("SCHEME_ID", getAttribute("SCHEME_ID").getString());
            rsDepositMaster.updateInt("DEPOSIT_TYPE_ID", getAttribute("DEPOSIT_TYPE_ID").getInt());
            rsDepositMaster.updateInt("DEPOSITER_STATUS", getAttribute("DEPOSITER_STATUS").getInt());
            rsDepositMaster.updateInt("DEPOSITER_CATEGORY", getAttribute("DEPOSITER_CATEGORY").getInt());
            rsDepositMaster.updateInt("DEPOSIT_PAYABLE_TO", getAttribute("DEPOSIT_PAYABLE_TO").getInt());
            
            rsDepositMaster.updateString("MAIN_ACCOUNT_CODE", getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsDepositMaster.updateString("INTEREST_MAIN_CODE", getAttribute("INTEREST_MAIN_CODE").getString());
            rsDepositMaster.updateInt("DEPOSIT_PERIOD", getAttribute("DEPOSIT_PERIOD").getInt());
            rsDepositMaster.updateDouble("INTEREST_RATE", getAttribute("INTEREST_RATE").getDouble());
            rsDepositMaster.updateString("DEPOSITER_CATEGORY_OTHERS", getAttribute("DEPOSITER_CATEGORY_OTHERS").getString());
            rsDepositMaster.updateString("FOLIO_NO", getAttribute("FOLIO_NO").getString());
            rsDepositMaster.updateString("EMPLOYEE_CODE", getAttribute("EMPLOYEE_CODE").getString());
            
            rsDepositMaster.updateString("EFFECTIVE_DATE", EITLERPGLOBAL.formatDateDB((getAttribute("EFFECTIVE_DATE").getString())));
            rsDepositMaster.updateString("MATURITY_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("MATURITY_DATE").getString()));
            rsDepositMaster.updateString("INT_CALC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("INT_CALC_DATE").getString()));
            
            rsDepositMaster.updateInt("TAX_EX_FORM_RECEIVED",getAttribute("TAX_EX_FORM_RECEIVED").getInt());
            rsDepositMaster.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsDepositMaster.updateInt("TDS_APPLICABLE", getAttribute("TDS_APPLICABLE").getInt());
            rsDepositMaster.updateString("OLD_RECEIPT_NO", getAttribute("OLD_RECEIPT_NO").getString());
            rsDepositMaster.updateString("PAN_NO", getAttribute("PAN_NO").getString());
            rsDepositMaster.updateString("PAN_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PAN_DATE").getString()));
            rsDepositMaster.updateString("PARTICULARS", getAttribute("PARTICULARS").getString());
            
            //Bank Detail
            rsDepositMaster.updateString("CHEQUE_NO", getAttribute("CHEQUE_NO").getString());
            rsDepositMaster.updateString("CHEQUE_DATE", EITLERPGLOBAL.formatDateDB( getAttribute("CHEQUE_DATE").getString()));
            rsDepositMaster.updateString("REALIZATION_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("REALIZATION_DATE").getString()));
            rsDepositMaster.updateString("DEPOSIT_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DEPOSIT_DATE").getString()));
            rsDepositMaster.updateDouble("AMOUNT", getAttribute("AMOUNT").getDouble());
            
            rsDepositMaster.updateString("BANK_MAIN_CODE", getAttribute("BANK_MAIN_CODE").getString());
            rsDepositMaster.updateString("BANK_NAME", getAttribute("BANK_NAME").getString());
            rsDepositMaster.updateString("BANK_ADDRESS", getAttribute("BANK_ADDRESS").getString());
            rsDepositMaster.updateString("BANK_CITY", getAttribute("BANK_CITY").getString());
            rsDepositMaster.updateString("BANK_PINCODE", getAttribute("BANK_PINCODE").getString());
            
            rsDepositMaster.updateString("BROKER_CODE", getAttribute("BROKER_CODE").getString());
            rsDepositMaster.updateString("BROKER_NAME", getAttribute("BROKER_NAME").getString());
            rsDepositMaster.updateString("BROKER_ADDRESS", getAttribute("BROKER_ADDRESS").getString());
            rsDepositMaster.updateString("BROKER_CITY", getAttribute("BROKER_CITY").getString());
            rsDepositMaster.updateString("BROKER_PINCODE", getAttribute("BROKER_PINCODE").getString());
            
            //Deposit Releated Information
            rsDepositMaster.updateInt("DEPOSIT_ENTRY_TYPE",getAttribute("DEPOSIT_ENTRY_TYPE").getInt()); // Always 1. for a new entry 2. for renewed
            rsDepositMaster.updateInt("PREMATURE", getAttribute("PREMATURE").getInt());     // 0 for a new entry
            rsDepositMaster.updateString("PM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PM_DATE").getString()));   // Blank for new entry
            rsDepositMaster.updateInt("DEPOSIT_STATUS", getAttribute("DEPOSIT_STATUS").getInt()); //Status 0. For OPEN (new Entry) 1. For close
            
            //Approval Specific
            rsDepositMaster.updateBoolean("APPROVED", false);
            rsDepositMaster.updateString("APPROVED_DATE", "0000-00-00");
            rsDepositMaster.updateBoolean("REJECTED", false);
            rsDepositMaster.updateString("REJECTED_DATE", "0000-00-00");
            rsDepositMaster.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            //rsDepositMaster.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
           // rsDepositMaster.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsDepositMaster.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
            rsDepositMaster.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            
            rsDepositMaster.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            rsDepositMaster.updateBoolean("CHANGED", true);
            rsDepositMaster.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsDepositMaster.updateBoolean("CANCELLED", false);
            rsDepositMaster.updateRow();
            
            //******************** History Update ************************//
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM FINANCE.D_FD_DEPOSIT_MASTER_H WHERE RECEIPT_NO='"+getAttribute("RECEIPT_NO").getString()+"'");
            RevNo++;
            
            rsDepositMasterH.moveToInsertRow() ;
            rsDepositMasterH.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsDepositMasterH.updateInt("REVISION_NO", RevNo);
            rsDepositMasterH.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsDepositMasterH.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsDepositMasterH.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsDepositMasterH.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            
            //Applicant Detail
            rsDepositMasterH.updateString("RECEIPT_NO", getAttribute("RECEIPT_NO").getString());
            rsDepositMasterH.updateString("LEGACY_NO", getAttribute("LEGACY_NO").getString());
            rsDepositMasterH.updateString("RECEIPT_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("RECEIPT_DATE").getString()));
            rsDepositMasterH.updateString("TITLE", getAttribute("TITLE").getString());
            rsDepositMasterH.updateString("APPLICANT_NAME", getAttribute("APPLICANT_NAME").getString());
            
            rsDepositMasterH.updateString("ADDRESS1", getAttribute("ADDRESS1").getString());
            rsDepositMasterH.updateString("ADDRESS2", getAttribute("ADDRESS2").getString());
            rsDepositMasterH.updateString("ADDRESS3", getAttribute("ADDRESS3").getString());
            rsDepositMasterH.updateString("CITY", getAttribute("CITY").getString());
            rsDepositMasterH.updateString("PINCODE", getAttribute("PINCODE").getString());
            rsDepositMasterH.updateString("CONTACT_NO", getAttribute("CONTACT_NO").getString());
            rsDepositMasterH.updateString("APPLICANT2", getAttribute("APPLICANT2").getString());
            rsDepositMasterH.updateString("APPLICANT3", getAttribute("APPLICANT3").getString());
            rsDepositMasterH.updateString("APPLICANT4", getAttribute("APPLICANT4").getString());
            rsDepositMasterH.updateString("NOMINEE_1_NAME", getAttribute("NOMINEE_1_NAME").getString());
            rsDepositMasterH.updateString("NOMINEE_2_NAME", getAttribute("NOMINEE_2_NAME").getString());
            rsDepositMasterH.updateString("NOMINEE_3_NAME", getAttribute("NOMINEE_3_NAME").getString());
            
            //Other Detail
            rsDepositMasterH.updateString("SCHEME_ID", getAttribute("SCHEME_ID").getString());
            rsDepositMasterH.updateInt("DEPOSITER_STATUS", getAttribute("DEPOSITER_STATUS").getInt());
            rsDepositMasterH.updateInt("DEPOSITER_CATEGORY", getAttribute("DEPOSITER_CATEGORY").getInt());
            rsDepositMasterH.updateInt("DEPOSIT_TYPE_ID", getAttribute("DEPOSIT_TYPE_ID").getInt());
            rsDepositMasterH.updateInt("DEPOSIT_PAYABLE_TO", getAttribute("DEPOSIT_PAYABLE_TO").getInt());
            
            rsDepositMasterH.updateInt("DEPOSIT_PERIOD", getAttribute("DEPOSIT_PERIOD").getInt());
            rsDepositMasterH.updateDouble("INTEREST_RATE", getAttribute("INTEREST_RATE").getDouble());
            rsDepositMasterH.updateString("MAIN_ACCOUNT_CODE", getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsDepositMasterH.updateString("INTEREST_MAIN_CODE", getAttribute("INTEREST_MAIN_CODE").getString());
            rsDepositMasterH.updateString("DEPOSITER_CATEGORY_OTHERS", getAttribute("DEPOSITER_CATEGORY_OTHERS").getString());
            rsDepositMasterH.updateString("FOLIO_NO", getAttribute("FOLIO_NO").getString());
            rsDepositMasterH.updateString("EMPLOYEE_CODE", getAttribute("EMPLOYEE_CODE").getString());
            
            rsDepositMasterH.updateString("EFFECTIVE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_DATE").getString()));
            rsDepositMasterH.updateString("MATURITY_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("MATURITY_DATE").getString()));
            rsDepositMasterH.updateString("INT_CALC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("INT_CALC_DATE").getString()));
            
            rsDepositMasterH.updateInt("TAX_EX_FORM_RECEIVED", getAttribute("TAX_EX_FORM_RECEIVED").getInt());
            rsDepositMasterH.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsDepositMasterH.updateInt("TDS_APPLICABLE", getAttribute("TDS_APPLICABLE").getInt());
            rsDepositMasterH.updateString("OLD_RECEIPT_NO", getAttribute("OLD_RECEIPT_NO").getString());
            rsDepositMasterH.updateString("PAN_NO", getAttribute("PAN_NO").getString());
            rsDepositMasterH.updateString("PAN_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PAN_DATE").getString()));
            rsDepositMasterH.updateString("PARTICULARS", getAttribute("PARTICULARS").getString());
            
            //Bank Detail
            rsDepositMasterH.updateString("CHEQUE_NO", getAttribute("CHEQUE_NO").getString());
            rsDepositMasterH.updateString("CHEQUE_DATE", EITLERPGLOBAL.formatDateDB( getAttribute("CHEQUE_DATE").getString()));
            rsDepositMasterH.updateString("REALIZATION_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("REALIZATION_DATE").getString()));
            rsDepositMasterH.updateString("DEPOSIT_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DEPOSIT_DATE").getString()));
            rsDepositMasterH.updateDouble("AMOUNT", getAttribute("AMOUNT").getDouble());
            
            rsDepositMasterH.updateString("BANK_MAIN_CODE", getAttribute("BANK_MAIN_CODE").getString());
            rsDepositMasterH.updateString("BANK_NAME", getAttribute("BANK_NAME").getString());
            rsDepositMasterH.updateString("BANK_ADDRESS", getAttribute("BANK_ADDRESS").getString());
            rsDepositMasterH.updateString("BANK_CITY", getAttribute("BANK_CITY").getString());
            rsDepositMasterH.updateString("BANK_PINCODE", getAttribute("BANK_PINCODE").getString());
            
            rsDepositMasterH.updateString("BROKER_CODE", getAttribute("BROKER_CODE").getString());
            rsDepositMasterH.updateString("BROKER_NAME", getAttribute("BROKER_NAME").getString());
            rsDepositMasterH.updateString("BROKER_ADDRESS", getAttribute("BROKER_ADDRESS").getString());
            rsDepositMasterH.updateString("BROKER_CITY", getAttribute("BROKER_CITY").getString());
            rsDepositMasterH.updateString("BROKER_PINCODE", getAttribute("BROKER_PINCODE").getString());
            
            //Deposit Releated Information
            rsDepositMasterH.updateInt("DEPOSIT_ENTRY_TYPE",getAttribute("DEPOSIT_ENTRY_TYPE").getInt()); // Always 1. for a new entry 2. for renewed
            rsDepositMasterH.updateInt("PREMATURE", getAttribute("PREMATURE").getInt());     // 0 for a new entry
            rsDepositMasterH.updateString("PM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PM_DATE").getString()));   // Blank for new entry
            rsDepositMasterH.updateInt("DEPOSIT_STATUS", getAttribute("DEPOSIT_STATUS").getInt()); //Status 0. For OPEN (new Entry) 1. For close
            
            //Approval Specific
            //rsDepositMasterH.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
            //rsDepositMasterH.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsDepositMasterH.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
            rsDepositMasterH.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsDepositMasterH.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            
            rsDepositMasterH.updateBoolean("APPROVED", false);
            rsDepositMasterH.updateString("APPROVED_DATE", "0000-00-00");
            rsDepositMasterH.updateBoolean("REJECTED", false);
            rsDepositMasterH.updateString("REJECTED_DATE", "0000-00-00");
            rsDepositMasterH.updateBoolean("CHANGED", true);
            rsDepositMasterH.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsDepositMasterH.updateBoolean("CANCELLED", false);
            rsDepositMasterH.insertRow();
            //*************************************************************//
            
            //************ Update Approval Flow *****************//
            ObjFlow.CompanyID =getAttribute("COMPANY_ID").getInt();
            ObjFlow.ModuleID = this.ModuleID ;
            ObjFlow.DocNo = getAttribute("RECEIPT_NO").getString();
            ObjFlow.From =getAttribute("FROM").getInt();
            ObjFlow.To = getAttribute("TO").getInt();
            ObjFlow.Status = getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName = "D_FD_DEPOSIT_MASTER";
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
                data.Execute("UPDATE D_FD_DEPOSIT_MASTER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE RECEIPT_NO='"+ getAttribute("RECEIPT_NO").getString() +"' ",FinanceGlobal.FinURL);
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
            //int CompanyID = getAttribute("COMPANY_ID").getInt();
            //String ReceiptNo = getAttribute("RECEIPT_NO").getString();
            if(ObjFlow.Status.equals("F")) {
                int DepositEntryType = data.getIntValueFromDB("SELECT DEPOSIT_ENTRY_TYPE FROM D_FD_DEPOSIT_MASTER WHERE COMPANY_ID="+CompanyID+" AND RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                if(DepositEntryType == 1) {
                    if(!PostVoucher(CompanyID,ReceiptNo)) {
                        LastError = " Voucher Not Posted.";
                        return false;
                    }
                } else if(DepositEntryType == 2) {
                    if(!PostVoucherRenew(CompanyID,ReceiptNo)) {
                        LastError = " Voucher Not Posted.";
                        return false;
                    }
                }
                //Update Deposit Status if Final Approved & Voucher posted Properly
                try {
                    if(!getAttribute("OLD_RECEIPT_NO").getString().equals("")) {
                        data.Execute("UPDATE D_FD_DEPOSIT_MASTER SET DEPOSIT_STATUS=1 WHERE RECEIPT_NO='"+getAttribute("OLD_RECEIPT_NO").getString()+"' AND COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+"",FinanceGlobal.FinURL);
                        data.Execute("UPDATE D_FD_DEPOSIT_MASTER_H SET DEPOSIT_STATUS=1 WHERE RECEIPT_NO='"+getAttribute("OLD_RECEIPT_NO").getString()+"' AND COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+"",FinanceGlobal.FinURL);
                    }
                } catch(Exception e) {
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LastError=e.getMessage();
            return false;
        }
    }
    
    public clsDepositMaster getObject(int CompanyID,String ReceiptNo) {
        
        clsDepositMaster ObjDepositMaster=new clsDepositMaster();
        
        try {
            Connection Conn=data.getConn(FinanceGlobal.FinURL);
            Statement stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            ResultSet rsTmp;
            ResultSet rsSchemeID;
            ResultSet rsDepositTypeID;
            ResultSet rsInterestRate;
            
            rsTmp=stTmp.executeQuery("SELECT * FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='" +ReceiptNo+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                //Applicant Detail
                ObjDepositMaster.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp, "COMPANY_ID", 0));
                ObjDepositMaster.setAttribute("RECEIPT_NO",UtilFunctions.getString(rsTmp, "RECEIPT_NO", ""));
                ObjDepositMaster.setAttribute("LEGACY_NO",UtilFunctions.getString(rsTmp, "LEGACY_NO", ""));
                ObjDepositMaster.setAttribute("RECEIPT_DATE",UtilFunctions.getString(rsTmp, "RECEIPT_DATE","0000-00-00"));
                ObjDepositMaster.setAttribute("TITLE",UtilFunctions.getString(rsTmp, "TITLE", ""));
                ObjDepositMaster.setAttribute("APPLICANT_NAME",UtilFunctions.getString(rsTmp, "APPLICANT_NAME", ""));
                ObjDepositMaster.setAttribute("ADDRESS1",UtilFunctions.getString(rsTmp, "ADDRESS1", ""));
                ObjDepositMaster.setAttribute("ADDRESS2",UtilFunctions.getString(rsTmp, "ADDRESS2", ""));
                ObjDepositMaster.setAttribute("ADDRESS3",UtilFunctions.getString(rsTmp, "ADDRESS3", ""));
                ObjDepositMaster.setAttribute("CITY",UtilFunctions.getString(rsTmp, "CITY", ""));
                ObjDepositMaster.setAttribute("PINCODE",UtilFunctions.getString(rsTmp, "PINCODE", ""));
                ObjDepositMaster.setAttribute("CONTACT_NO",UtilFunctions.getString(rsTmp, "CONTACT_NO", ""));
                ObjDepositMaster.setAttribute("APPLICANT2",UtilFunctions.getString(rsTmp, "APPLICANT2", ""));
                ObjDepositMaster.setAttribute("APPLICANT3",UtilFunctions.getString(rsTmp, "APPLICANT3", ""));
                ObjDepositMaster.setAttribute("APPLICANT4",UtilFunctions.getString(rsTmp, "APPLICANT4", ""));
                ObjDepositMaster.setAttribute("NOMINEE_1_NAME",UtilFunctions.getString(rsTmp, "NOMINEE_1_NAME", ""));
                ObjDepositMaster.setAttribute("NOMINEE_2_NAME",UtilFunctions.getString(rsTmp, "NOMINEE_2_NAME", ""));
                ObjDepositMaster.setAttribute("NOMINEE_3_NAME",UtilFunctions.getString(rsTmp, "NOMINEE_3_NAME", ""));
                
                //Other Detail
                ObjDepositMaster.setAttribute("SCHEME_ID",UtilFunctions.getString(rsTmp,"SCHEME_ID", ""));
                ObjDepositMaster.setAttribute("DEPOSIT_TYPE_ID",UtilFunctions.getInt(rsTmp, "DEPOSIT_TYPE_ID", 0));
                ObjDepositMaster.setAttribute("DEPOSITER_STATUS",UtilFunctions.getInt(rsTmp, "DEPOSITER_STATUS", 0));
                ObjDepositMaster.setAttribute("DEPOSITER_CATEGORY",UtilFunctions.getInt(rsTmp, "DEPOSITER_CATEGORY", 0));
                ObjDepositMaster.setAttribute("DEPOSIT_PAYABLE_TO",UtilFunctions.getInt(rsTmp, "DEPOSIT_PAYABLE_TO", 0));
                
                ObjDepositMaster.setAttribute("INTEREST_RATE",UtilFunctions.getDouble(rsTmp, "INTEREST_RATE", 0.0));
                ObjDepositMaster.setAttribute("DEPOSIT_PERIOD",UtilFunctions.getInt(rsTmp, "DEPOSIT_PERIOD", 0));
                ObjDepositMaster.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsTmp, "MAIN_ACCOUNT_CODE", ""));
                ObjDepositMaster.setAttribute("INTEREST_MAIN_CODE",UtilFunctions.getString(rsTmp, "INTEREST_MAIN_CODE", ""));
                ObjDepositMaster.setAttribute("DEPOSITER_CATEGORY_OTHERS",UtilFunctions.getString(rsTmp, "DEPOSITER_CATEGORY_OTHERS", ""));
                ObjDepositMaster.setAttribute("FOLIO_NO",UtilFunctions.getString(rsTmp, "FOLIO_NO", ""));
                ObjDepositMaster.setAttribute("EMPLOYEE_CODE",UtilFunctions.getString(rsTmp, "EMPLOYEE_CODE", ""));
                
                ObjDepositMaster.setAttribute("EFFECTIVE_DATE",UtilFunctions.getString(rsTmp, "EFFECTIVE_DATE","0000-00-00"));
                ObjDepositMaster.setAttribute("MATURITY_DATE",UtilFunctions.getString(rsTmp, "MATURITY_DATE","0000-00-00"));
                ObjDepositMaster.setAttribute("INT_CALC_DATE",UtilFunctions.getString(rsTmp,"INT_CALC_DATE","0000-00-00"));
                
                ObjDepositMaster.setAttribute("TAX_EX_FORM_RECEIVED",UtilFunctions.getInt(rsTmp, "TAX_EX_FORM_RECEIVED", 0));
                ObjDepositMaster.setAttribute("PARTY_CODE",UtilFunctions.getString(rsTmp, "PARTY_CODE", ""));
                ObjDepositMaster.setAttribute("TDS_APPLICABLE",UtilFunctions.getInt(rsTmp, "TDS_APPLICABLE", 0));
                ObjDepositMaster.setAttribute("OLD_RECEIPT_NO",UtilFunctions.getString(rsTmp, "OLD_RECEIPT_NO", ""));
                ObjDepositMaster.setAttribute("PAN_NO",UtilFunctions.getString(rsTmp, "PAN_NO", ""));
                ObjDepositMaster.setAttribute("PAN_DATE",UtilFunctions.getString(rsTmp, "PAN_DATE","0000-00-00"));
                ObjDepositMaster.setAttribute("PARTICULARS",UtilFunctions.getString(rsTmp, "PARTICULARS", ""));
                
                //Bank Detail
                ObjDepositMaster.setAttribute("CHEQUE_NO",UtilFunctions.getString(rsTmp, "CHEQUE_NO", ""));
                ObjDepositMaster.setAttribute("CHEQUE_DATE",UtilFunctions.getString(rsTmp, "CHEQUE_DATE","0000-00-00"));
                ObjDepositMaster.setAttribute("REALIZATION_DATE",UtilFunctions.getString(rsTmp, "REALIZATION_DATE","0000-00-00"));
                ObjDepositMaster.setAttribute("DEPOSIT_DATE",UtilFunctions.getString(rsTmp, "DEPOSIT_DATE","0000-00-00"));
                ObjDepositMaster.setAttribute("AMOUNT",UtilFunctions.getDouble(rsTmp, "AMOUNT", 0.0));
                
                ObjDepositMaster.setAttribute("BANK_MAIN_CODE",UtilFunctions.getString(rsTmp, "BANK_MAIN_CODE", ""));
                ObjDepositMaster.setAttribute("BANK_NAME",UtilFunctions.getString(rsTmp, "BANK_NAME", ""));
                ObjDepositMaster.setAttribute("BANK_ADDRESS",UtilFunctions.getString(rsTmp, "BANK_ADDRESS", ""));
                ObjDepositMaster.setAttribute("BANK_CITY",UtilFunctions.getString(rsTmp, "BANK_CITY", ""));
                ObjDepositMaster.setAttribute("BANK_PINCODE",UtilFunctions.getString(rsTmp, "BANK_PINCODE", ""));
                
                ObjDepositMaster.setAttribute("BROKER_CODE",UtilFunctions.getString(rsTmp, "BROKER_CODE", ""));
                ObjDepositMaster.setAttribute("BROKER_NAME",UtilFunctions.getString(rsTmp, "BROKER_NAME", ""));
                ObjDepositMaster.setAttribute("BROKER_ADDRESS",UtilFunctions.getString(rsTmp, "BROKER_ADDRESS", ""));
                ObjDepositMaster.setAttribute("BROKER_CITY",UtilFunctions.getString(rsTmp, "BROKER_CITY", ""));
                ObjDepositMaster.setAttribute("BROKER_PINCODE",UtilFunctions.getString(rsTmp, "BROKER_PINCODE", ""));
                
                //Deposit Releated Information
                ObjDepositMaster.setAttribute("DEPOSIT_ENTRY_TYPE",UtilFunctions.getInt(rsTmp, "DEPOSIT_ENTRY_TYPE", 0));
                ObjDepositMaster.setAttribute("PREMATURE",UtilFunctions.getInt(rsTmp, "PREMATURE", 0));
                ObjDepositMaster.setAttribute("PM_DATE",UtilFunctions.getString(rsTmp, "PM_DATE","0000-00-00"));
                ObjDepositMaster.setAttribute("DEPOSIT_STATUS",UtilFunctions.getInt(rsTmp, "DEPOSIT_STATUS", 0));
                
                //Approval Specific
                ObjDepositMaster.setAttribute("CREATED_BY",UtilFunctions.getString(rsTmp, "CREATED_BY", ""));
                ObjDepositMaster.setAttribute("CREATED_DATE",UtilFunctions.getString(rsTmp, "CREATED_DATE","0000-00-00"));
                ObjDepositMaster.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsTmp, "MODIFIED_BY", ""));
                ObjDepositMaster.setAttribute("MODIFIED_DATE",UtilFunctions.getString(rsTmp, "MODIFIED_DATE","0000-00-00"));
                
                ObjDepositMaster.setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsTmp, "HIERARCHY_ID", 0));
                ObjDepositMaster.setAttribute("APPROVED",UtilFunctions.getInt(rsTmp, "APPROVED",0));
                ObjDepositMaster.setAttribute("APPROVED_DATE",UtilFunctions.getString(rsTmp, "APPROVED_DATE","0000-00-00"));
                ObjDepositMaster.setAttribute("REJECTED",UtilFunctions.getInt(rsTmp, "REJECTED", 0));
                
                ObjDepositMaster.setAttribute("REJECTED_DATE",UtilFunctions.getString(rsTmp, "REJECTED_DATE","0000-00-00"));
                ObjDepositMaster.setAttribute("CHANGED",UtilFunctions.getInt(rsTmp, "CHANGED", 0));
                ObjDepositMaster.setAttribute("CHANGED_DATE",UtilFunctions.getString(rsTmp, "CHANGED_DATE","0000-00-00"));
                ObjDepositMaster.setAttribute("CANCELLED",UtilFunctions.getInt(rsTmp, "CANCELLED", 0));
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return ObjDepositMaster;
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
                strSQL="SELECT FINANCE.D_FD_DEPOSIT_MASTER.RECEIPT_NO,FINANCE.D_FD_DEPOSIT_MASTER.RECEIPT_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_DEPOSIT_MASTER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_DEPOSIT_MASTER.RECEIPT_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FD_DEPOSIT_MASTER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FD_DEPOSIT_MASTER.COMPANY_ID="+CompanyID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+clsDepositMaster.ModuleID+" AND FINANCE.D_FD_DEPOSIT_MASTER.CANCELLED=0 ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT FINANCE.D_FD_DEPOSIT_MASTER.RECEIPT_NO,FINANCE.D_FD_DEPOSIT_MASTER.RECEIPT_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_DEPOSIT_MASTER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_DEPOSIT_MASTER.RECEIPT_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FD_DEPOSIT_MASTER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FD_DEPOSIT_MASTER.COMPANY_ID="+CompanyID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+clsDepositMaster.ModuleID+" AND FINANCE.D_FD_DEPOSIT_MASTER.CANCELLED=0 ORDER BY D_FD_DEPOSIT_MASTER.RECEIPT_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT FINANCE.D_FD_DEPOSIT_MASTER.RECEIPT_NO,FINANCE.D_FD_DEPOSIT_MASTER.RECEIPT_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_DEPOSIT_MASTER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_DEPOSIT_MASTER.RECEIPT_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FD_DEPOSIT_MASTER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FD_DEPOSIT_MASTER.COMPANY_ID="+CompanyID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+clsDepositMaster.ModuleID+" AND FINANCE.D_FD_DEPOSIT_MASTER.CANCELLED=0 ORDER BY D_FD_DEPOSIT_MASTER.RECEIPT_NO";
            }
            
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                //if(EITLERPGLOBAL.isWithinDate(rsTmp.getString("RECEIPT_DATE"),FinYearFrom)) {
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
                //}
                rsTmp.next();
            }//end of while
            
            rsTmp.close();
            tmpStmt.close();
            
        }
        catch(Exception e) {
        }
        
        return List;
    }
    
    
    public boolean Filter(String pCondition,int pCompanyID) {
        Ready=false;
        pCompanyID=EITLERPGLOBAL.gCompanyID;
        try {
            String strSql = "SELECT * FROM D_FD_DEPOSIT_MASTER " + pCondition ;
            // System.out.println(strSql);
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDepositMaster = Stmt.executeQuery(strSql);
            rsDepositMaster.first();
            
            if(!rsDepositMaster.first()) {
                strSql = "SELECT * FROM D_FD_DEPOSIT_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" ORDER BY RECEIPT_NO ";
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
            
            rsTmp=stTmp.executeQuery("SELECT * FROM D_FD_DEPOSIT_MASTER_H WHERE COMPANY_ID="+pCompanyID+" AND RECEIPT_NO='"+pDocNo+"'");
            while(rsTmp.next()) {
                clsDepositMaster ObjDepositMaster=new clsDepositMaster();
                
                ObjDepositMaster.setAttribute("RECEIPT_NO",rsTmp.getString("RECEIPT_NO"));
                ObjDepositMaster.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjDepositMaster.setAttribute("UPDATED_BY",rsTmp.getString("UPDATED_BY"));
                ObjDepositMaster.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjDepositMaster.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjDepositMaster.setAttribute("APPROVER_REMARKS",UtilFunctions.getString(rsTmp,"APPROVER_REMARKS", ""));
                
                List.put(Integer.toString(List.size()+1),ObjDepositMaster);
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
    
    public String getDocStatus(int pCompanyID,String pDocNo) {
        ResultSet rsTmp;
        String strMessage="";
        
        try {
            //First check that Document exist
            rsTmp=data.getResult("SELECT RECEIPT_NO,APPROVED,CANCELLED FROM D_FD_DEPOSIT_MASTER WHERE COMPANY_ID="+pCompanyID+" AND RECEIPT_NO='"+pDocNo+"'",FinanceGlobal.FinURL);
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
        } catch(Exception e) {
        }
        return strMessage;
    }
    
    public boolean IsEditable(int pCompanyID,String pDocNo,int pUserID) {
        
        try {
            ResultSet rsTmp;
            Statement tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String strSQL1="SELECT COUNT(*) AS COUNT FROM D_FD_DEPOSIT_MASTER WHERE COMPANY_ID="+pCompanyID+" AND RECEIPT_NO='"+pDocNo+"' AND (APPROVED=1)";
            
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
                RevNo=rsDepositMaster.getInt("REVISION_NO");
                setAttribute("REVISION_NO",RevNo);
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            //Applicant Detail
            setAttribute("COMPANY_ID",UtilFunctions.getInt(rsDepositMaster, "COMPANY_ID",0));
            setAttribute("RECEIPT_NO",UtilFunctions.getString(rsDepositMaster,"RECEIPT_NO",""));
            setAttribute("LEGACY_NO",UtilFunctions.getString(rsDepositMaster,"LEGACY_NO",""));
            setAttribute("RECEIPT_DATE",UtilFunctions.getString(rsDepositMaster,"RECEIPT_DATE","0000-00-00"));
            setAttribute("TITLE",UtilFunctions.getString(rsDepositMaster,"TITLE",""));
            setAttribute("APPLICANT_NAME",UtilFunctions.getString(rsDepositMaster,"APPLICANT_NAME",""));
            setAttribute("ADDRESS1",UtilFunctions.getString(rsDepositMaster,"ADDRESS1",""));
            setAttribute("ADDRESS2",UtilFunctions.getString(rsDepositMaster,"ADDRESS2",""));
            setAttribute("ADDRESS3",UtilFunctions.getString(rsDepositMaster, "ADDRESS3",""));
            setAttribute("CITY",UtilFunctions.getString(rsDepositMaster,"CITY",""));
            setAttribute("PINCODE",UtilFunctions.getString(rsDepositMaster,"PINCODE",""));
            setAttribute("CONTACT_NO",UtilFunctions.getString(rsDepositMaster,"CONTACT_NO",""));
            setAttribute("APPLICANT2",UtilFunctions.getString(rsDepositMaster,"APPLICANT2",""));
            setAttribute("APPLICANT3",UtilFunctions.getString(rsDepositMaster, "APPLICANT3",""));
            setAttribute("APPLICANT4",UtilFunctions.getString(rsDepositMaster, "APPLICANT4",""));
            setAttribute("NOMINEE_1_NAME",UtilFunctions.getString(rsDepositMaster,"NOMINEE_1_NAME",""));
            setAttribute("NOMINEE_2_NAME",UtilFunctions.getString(rsDepositMaster, "NOMINEE_2_NAME",""));
            setAttribute("NOMINEE_3_NAME",UtilFunctions.getString(rsDepositMaster, "NOMINEE_3_NAME",""));
            
            //Other Detail
            setAttribute("SCHEME_ID",UtilFunctions.getString(rsDepositMaster,"SCHEME_ID",""));
            setAttribute("DEPOSIT_TYPE_ID",UtilFunctions.getInt(rsDepositMaster, "DEPOSIT_TYPE_ID",0));
            setAttribute("DEPOSIT_PAYABLE_TO",UtilFunctions.getInt(rsDepositMaster, "DEPOSIT_PAYABLE_TO",0));
            setAttribute("DEPOSITER_STATUS",UtilFunctions.getInt( rsDepositMaster,"DEPOSITER_STATUS",0));
            setAttribute("DEPOSITER_CATEGORY",UtilFunctions.getInt( rsDepositMaster,"DEPOSITER_CATEGORY",0));
            
            setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsDepositMaster,"MAIN_ACCOUNT_CODE",""));
            setAttribute("INTEREST_MAIN_CODE",UtilFunctions.getString(rsDepositMaster,"INTEREST_MAIN_CODE",""));
            setAttribute("DEPOSIT_PERIOD",UtilFunctions.getInt( rsDepositMaster,"DEPOSIT_PERIOD",0));
            setAttribute("INTEREST_RATE",UtilFunctions.getDouble(rsDepositMaster, "INTEREST_RATE",0.0));
            setAttribute("DEPOSITER_CATEGORY_OTHERS",UtilFunctions.getString( rsDepositMaster,"DEPOSITER_CATEGORY_OTHERS",""));
            setAttribute("FOLIO_NO",UtilFunctions.getString( rsDepositMaster,"FOLIO_NO",""));
            setAttribute("EMPLOYEE_CODE",UtilFunctions.getString( rsDepositMaster,"EMPLOYEE_CODE",""));
            
            setAttribute("EFFECTIVE_DATE",UtilFunctions.getString( rsDepositMaster,"EFFECTIVE_DATE","0000-00-00"));
            setAttribute("MATURITY_DATE",UtilFunctions.getString( rsDepositMaster,"MATURITY_DATE","0000-00-00"));
            setAttribute("INT_CALC_DATE",UtilFunctions.getString( rsDepositMaster,"INT_CALC_DATE","0000-00-00"));
            
            setAttribute("TAX_EX_FORM_RECEIVED",UtilFunctions.getInt( rsDepositMaster,"TAX_EX_FORM_RECEIVED",0));
            setAttribute("PARTY_CODE",UtilFunctions.getString( rsDepositMaster,"PARTY_CODE",""));
            setAttribute("TDS_APPLICABLE",UtilFunctions.getInt( rsDepositMaster,"TDS_APPLICABLE",0));
            setAttribute("OLD_RECEIPT_NO",UtilFunctions.getString( rsDepositMaster,"OLD_RECEIPT_NO",""));
            setAttribute("PAN_NO",UtilFunctions.getString(rsDepositMaster, "PAN_NO",""));
            setAttribute("PAN_DATE",UtilFunctions.getString( rsDepositMaster,"PAN_DATE","0000-00-00"));
            setAttribute("PARTICULARS",UtilFunctions.getString( rsDepositMaster,"PARTICULARS",""));
            
            //Bank Detail
            setAttribute("CHEQUE_NO",UtilFunctions.getString( rsDepositMaster,"CHEQUE_NO",""));
            setAttribute("CHEQUE_DATE",UtilFunctions.getString( rsDepositMaster,"CHEQUE_DATE","0000-00-00"));
            setAttribute("REALIZATION_DATE",UtilFunctions.getString( rsDepositMaster,"REALIZATION_DATE","0000-00-00"));
            setAttribute("DEPOSIT_DATE",UtilFunctions.getString( rsDepositMaster,"DEPOSIT_DATE","0000-00-00"));
            setAttribute("AMOUNT",UtilFunctions.getDouble( rsDepositMaster,"AMOUNT",0.0));
            
            setAttribute("BANK_MAIN_CODE",UtilFunctions.getString( rsDepositMaster,"BANK_MAIN_CODE",""));
            setAttribute("BANK_NAME",UtilFunctions.getString( rsDepositMaster,"BANK_NAME",""));
            setAttribute("BANK_ADDRESS",UtilFunctions.getString( rsDepositMaster,"BANK_ADDRESS",""));
            setAttribute("BANK_CITY",UtilFunctions.getString( rsDepositMaster,"BANK_CITY",""));
            setAttribute("BANK_PINCODE",UtilFunctions.getString( rsDepositMaster,"BANK_PINCODE",""));
            
            setAttribute("BROKER_CODE",UtilFunctions.getString( rsDepositMaster,"BROKER_CODE",""));
            setAttribute("BROKER_NAME",UtilFunctions.getString( rsDepositMaster,"BROKER_NAME",""));
            setAttribute("BROKER_ADDRESS",UtilFunctions.getString( rsDepositMaster,"BROKER_ADDRESS",""));
            setAttribute("BROKER_CITY",UtilFunctions.getString( rsDepositMaster,"BROKER_CITY",""));
            setAttribute("BROKER_PINCODE",UtilFunctions.getString(rsDepositMaster, "BROKER_PINCODE",""));
            
            //Deposit Releated Information
            setAttribute("DEPOSIT_ENTRY_TYPE",UtilFunctions.getInt( rsDepositMaster,"DEPOSIT_ENTRY_TYPE",0));
            setAttribute("PREMATURE",UtilFunctions.getInt(rsDepositMaster,"PREMATURE",0));
            setAttribute("PM_DATE",UtilFunctions.getString(rsDepositMaster,"PM_DATE","0000-00-00"));
            setAttribute("DEPOSIT_STATUS",UtilFunctions.getInt(rsDepositMaster,"DEPOSIT_STATUS",0));
            
            //Approval Specific
            setAttribute("CREATED_BY",UtilFunctions.getString(rsDepositMaster,"CREATED_BY",""));
            setAttribute("CREATED_DATE",UtilFunctions.getString(rsDepositMaster,"CREATED_DATE","0000-00-00"));
            setAttribute("MODIFIED_BY",UtilFunctions.getString(rsDepositMaster,"MODIFIED_BY",""));
            setAttribute("MODIFIED_DATE",UtilFunctions.getString(rsDepositMaster,"MODIFIED_DATE","0000-00-00"));
            setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsDepositMaster,"HIERARCHY_ID",0));
            setAttribute("APPROVED",UtilFunctions.getInt(rsDepositMaster,"APPROVED",0));
            setAttribute("APPROVED_DATE",UtilFunctions.getString(rsDepositMaster,"APPROVED_DATE","0000-00-00"));
            setAttribute("REJECTED",UtilFunctions.getInt(rsDepositMaster,"REJECTED",0));
            setAttribute("REJECTED_DATE",UtilFunctions.getString(rsDepositMaster,"REJECTED_DATE","0000-00-00"));
            setAttribute("REJECTED_REMARKS",UtilFunctions.getString(rsDepositMaster,"REJECTED_REMARKS",""));
            setAttribute("CHANGED",UtilFunctions.getInt(rsDepositMaster,"CHANGED",0));
            setAttribute("CHANGED_DATE",UtilFunctions.getString(rsDepositMaster,"CHANGED_DATE","0000-00-00"));
            setAttribute("CANCELLED",UtilFunctions.getInt(rsDepositMaster,"CANCELLED",0));
            
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
                clsDepositMaster ObjDepositMaster =new clsDepositMaster();
                
                ObjDepositMaster.setAttribute("SCHEME_ID",rsTmp.getString("SCHEME_ID"));
                ObjDepositMaster.setAttribute("SCHEME_NAME",rsTmp.getString("SCHEME_NAME"));
                
                List.put(Long.toString(Counter),ObjDepositMaster);
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
                clsDepositMaster ObjDepositMaster =new clsDepositMaster();
                ObjDepositMaster.setAttribute("DEPOSIT_TYPE_ID",rsTmp.getInt("DEPOSIT_TYPE_ID"));
                ObjDepositMaster.setAttribute("DEPOSIT_TYPE_NAME",rsTmp.getString("DEPOSIT_TYPE_NAME"));
                List.put(Long.toString(Counter),ObjDepositMaster);
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
    
    private boolean PostVoucher(int CompanyID,String ReceiptNo) {
        
        try {
            clsDepositMaster ObjDepositMaster=(clsDepositMaster)(new clsDepositMaster()).getObject(CompanyID,ReceiptNo);
            clsVoucher objVoucher=new clsVoucher();
            
            String SelPrefix="";
            String SelSuffix="";
            int FFNo=0;
            /*========== Select the Hierarchy ======== */
            setAttribute("FIN_HIERARCHY_ID",0);
            HashMap List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsDepositMaster.ModuleID, "CHOOSE_HIERARCHY", "DEFAULT", "");
            
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
            BookCode = data.getStringValueFromDB("SELECT BOOK_CODE FROM D_FIN_BOOK_MASTER WHERE MAIN_ACCOUNT_CODE='"+getAttribute("BANK_MAIN_CODE").getString()+"' ",FinanceGlobal.FinURL);
            double Amount = ObjDepositMaster.getAttribute("AMOUNT").getDouble();
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
            objVoucher.setAttribute("LEGACY_NO","");
            objVoucher.setAttribute("LEGACY_DATE","0000-00-00");
            objVoucher.setAttribute("BOOK_CODE",BookCode);
            objVoucher.setAttribute("VOUCHER_TYPE",FinanceGlobal.TYPE_RECEIPT);
            objVoucher.setAttribute("CHEQUE_NO",ObjDepositMaster.getAttribute("CHEQUE_NO").getString());
            objVoucher.setAttribute("CHEQUE_DATE",EITLERPGLOBAL.formatDate(ObjDepositMaster.getAttribute("CHEQUE_DATE").getString()));
            objVoucher.setAttribute("BANK_NAME",ObjDepositMaster.getAttribute("BANK_NAME").getString());
            objVoucher.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(ObjDepositMaster.getAttribute("REALIZATION_DATE").getString()));//
            objVoucher.setAttribute("REALIZATION_DATE",EITLERPGLOBAL.formatDate(ObjDepositMaster.getAttribute("REALIZATION_DATE").getString()));//
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
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",ObjDepositMaster.getAttribute("BANK_MAIN_CODE").getString());
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
            objVoucherItem.setAttribute("AMOUNT",Amount);
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO",ObjDepositMaster.getAttribute("RECEIPT_NO").getString());
            objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(ObjDepositMaster.getAttribute("RECEIPT_DATE").getString()));
            objVoucherItem.setAttribute("MODULE_ID",clsDepositMaster.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            VoucherSrNo++;
            objVoucherItem=new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT","C");
            objVoucherItem.setAttribute("ACCOUNT_ID",1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",ObjDepositMaster.getAttribute("MAIN_ACCOUNT_CODE").getString());
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",ObjDepositMaster.getAttribute("PARTY_CODE").getString());
            objVoucherItem.setAttribute("AMOUNT",Amount);
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO",ObjDepositMaster.getAttribute("RECEIPT_NO").getString());
            objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(ObjDepositMaster.getAttribute("RECEIPT_DATE").getString()));
            objVoucherItem.setAttribute("MODULE_ID",clsDepositMaster.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            /*============== End of Voucher Detail =================*/
            
            if(objVoucher.Insert()) {
                objVoucher.IsInternalPosting=false;
                return true;
            }
            else {
                objVoucher.IsInternalPosting=false;
                return false;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            LastError=objVoucher.LastError;
            return false;
        }
    }
    
    private boolean PostVoucherRenew(int CompanyID,String ReceiptNo) {
        try {
            clsDepositMaster ObjDepositMaster=(clsDepositMaster)(new clsDepositMaster()).getObject(CompanyID,ReceiptNo);
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
            
            //============= Gethering Data =======================//
            int VoucherSrNo=0;
            //List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsDepositRenew.ModuleID, "GET_BOOK_CODE", "BOOK_CODE", "");
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsDepositRenew.ModuleID, "GET_BOOK_CODE", "DEPOSIT_RENEWAL", "");
            if(List.size()>0) {
                //GET BOOK CODE FOR RENEWED RECEIPT.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                BookCode = objRule.getAttribute("RULE_OUTCOME").getString(); //BookCode = "39";
            }
            
            String BankName = data.getStringValueFromDB("SELECT BOOK_NAME FROM D_FIN_BOOK_MASTER WHERE BOOK_CODE='"+BookCode+"' ",FinanceGlobal.FinURL);
            double Amount = ObjDepositMaster.getAttribute("AMOUNT").getDouble();
            String ReceiptDate = data.getStringValueFromDB("SELECT RECEIPT_DATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            String oReceiptNo = data.getStringValueFromDB("SELECT OLD_RECEIPT_NO FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            String oReceiptDate = data.getStringValueFromDB("SELECT RECEIPT_DATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+oReceiptNo+"' ",FinanceGlobal.FinURL);
            String oMainAccountCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+oReceiptNo+"' ",FinanceGlobal.FinURL);
            String oSubAccountCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+oReceiptNo+"' ",FinanceGlobal.FinURL);
            String MainAccountCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            String SubAccountCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            //=============== End of Gethering Data ==============//
            
            /*============== Preparing Voucher Header ================*/
            objVoucher=new clsVoucher();
            objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
            objVoucher.IsInternalPosting=true;
            objVoucher.setAttribute("PREFIX",SelPrefix);
            objVoucher.setAttribute("SUFFIX",SelSuffix);
            objVoucher.setAttribute("FFNO",FFNo);
            objVoucher.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            objVoucher.setAttribute("VOUCHER_NO","");
            objVoucher.setAttribute("LEGACY_NO","");
            objVoucher.setAttribute("LEGACY_DATE","0000-00-00");
            objVoucher.setAttribute("BOOK_CODE",BookCode);
            objVoucher.setAttribute("VOUCHER_TYPE",FinanceGlobal.TYPE_JOURNAL);
            objVoucher.setAttribute("CHEQUE_NO",""); // Cheque No.
            objVoucher.setAttribute("CHEQUE_DATE","0000-00-00"); // Cheque Date.
            objVoucher.setAttribute("BANK_NAME",BankName);
            objVoucher.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(ReceiptDate)); // Voucher Date --> New Receipt Date
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
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",oMainAccountCode);
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",oSubAccountCode);
            objVoucherItem.setAttribute("AMOUNT",Amount);
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO",oReceiptNo);
            objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(oReceiptDate));
            objVoucherItem.setAttribute("MODULE_ID",clsDepositMaster.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            VoucherSrNo++;
            objVoucherItem=new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT","C");
            objVoucherItem.setAttribute("ACCOUNT_ID",1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode);
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",SubAccountCode);
            objVoucherItem.setAttribute("AMOUNT",Amount);
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO",ReceiptNo);
            objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(ReceiptDate));
            objVoucherItem.setAttribute("MODULE_ID",clsDepositMaster.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            /*============= End of Voucher Detail ===============*/
            
            if(objVoucher.Insert()) {
                objVoucher.IsInternalPosting=false;
                return true;
            } else {
                objVoucher.IsInternalPosting=false;
                return false;
            }
        } catch(Exception e) {
            e.printStackTrace();
            LastError=objVoucher.LastError;
            return false;
        }
    }
    
    private boolean Validate() {
        try {
            if(!RenewEntry) {
                if (getAttribute("RECEIPT_NO").getString().trim().equals("")) {
                    LastError = "Please specify ReceiptNo";
                    return false;
                }
                
                if(!data.IsRecordExist("SELECT * FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+getAttribute("SCHEME_ID").getString()+"' AND DEPOSIT_TYPE_ID="+getAttribute("DEPOSIT_TYPE_ID").getInt(),FinanceGlobal.FinURL)) {
                    LastError = "Please select appropriate deposit type according to Scheme ID.";
                    return false;
                }
                if(!getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                    if(getAttribute("TAX_EX_FORM_RECEIVED").getInt()==0) {
                        if(getAttribute("TDS_APPLICABLE").getInt()==0) {
                            double otherInterestAmount = 0.0;
                            double currrentInterestAmount = 0.0;
                            otherInterestAmount = checkTDSAmount(getAttribute("PARTY_CODE").getString());
                            if(EditMode==1) {
                                double Amount = getAttribute("AMOUNT").getDouble();
                                String SchemeID = getAttribute("SCHEME_ID").getString();
                                double Rate = getAttribute("INTEREST_RATE").getDouble();
                                String EffectiveDate = EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_DATE").getString());
                                String EndofFinYear = EITLERPGLOBAL.getFinYearEndDate(EffectiveDate);
                                int diffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(EffectiveDate),java.sql.Date.valueOf(EndofFinYear))+1;
                                
                                GregorianCalendar cal = new GregorianCalendar();
                                //if (cal.isLeapYear(EITLERPGLOBAL.getCurrentFinYear()+1) ) {
                                if (cal.isLeapYear(EITLERPGLOBAL.getCurrentFinYear()) ) {
                                    currrentInterestAmount = EITLERPGLOBAL.round((Amount *  Rate * diffofDays)/36600,0);
                                } else {
                                    currrentInterestAmount = EITLERPGLOBAL.round((Amount *  Rate * diffofDays)/36500,0);
                                }
                            }
                            currrentInterestAmount += otherInterestAmount;
                            
                            if(currrentInterestAmount > 5000.0) {
                                LastError = "\nInterest Amount more then 5000 Per Annum. \nPlease check Either Tax Exemption Form Received or TDS Applicable.";
                                return false;
                            }
                        }
                    }
                }
                
                if(!data.IsRecordExist("SELECT PARTY_CODE FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+getAttribute("PARTY_CODE").getString()+"' AND MAIN_ACCOUNT_CODE='"+getAttribute("MAIN_ACCOUNT_CODE").getString()+"' ",FinanceGlobal.FinURL)) {
                    LastError = "Party Code with this main account code does not exists. \nPlease varify Party code with this main account code in party master.";
                    return false;
                }
                
                if(getAttribute("DEPOSIT_ENTRY_TYPE").getInt()==2) {
                    String EffectiveDate = EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_DATE").getString());
                    String MaturityDate = data.getStringValueFromDB("SELECT MATURITY_DATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+getAttribute("OLD_RECEIPT_NO").getString()+"' ",FinanceGlobal.FinURL);
                    if(java.sql.Date.valueOf(EffectiveDate).before(java.sql.Date.valueOf(MaturityDate))) {
                        LastError = "\n Effective Date of Renewed Receipt not before then Maturity Date of old Receipt.";
                        return false;
                    }
                }
                
                if(data.getIntValueFromDB("SELECT SCHEME_TYPE FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+getAttribute("SCHEME_ID").getString()+"' ")==3) {
                    if(getAttribute("").getString().equals("115160")) {
                        if(!data.IsRecordExist("SELECT PARTY_CODE FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+getAttribute("PARTY_CODE").getString()+"' AND MAIN_ACCOUNT_CODE='115160'",FinanceGlobal.FinURL)) {
                            LastError = "Party Code with this cd interest main account code(115160) does not exists. \nPlease varify Party code with this interest main account code.";
                            return false;
                        } else if(getAttribute("").getString().equals("115201")) {
                            if(!data.IsRecordExist("SELECT PARTY_CODE FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+getAttribute("PARTY_CODE").getString()+"' AND MAIN_ACCOUNT_CODE='115201'",FinanceGlobal.FinURL)) {
                                LastError = "Party Code with this cd interest main account code(115201) does not exists. \nPlease varify Party code with this interest main account code.";
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public static double checkTDSAmount(String PartyCode) {
        double interestAmount = 0.0;
        String StartFinYear = Integer.toString(EITLERPGLOBAL.getCurrentFinYear())+"-04-01";
        String EndFinYear = Integer.toString(EITLERPGLOBAL.getCurrentFinYear()+1)+"-03-31";
        String EffectiveDate = "";
        String MaturityDate = "";
        String PMDate = "";
        int DiffofDays = 1;
        double Rate = 0.0;
        double Amount = 0.0;
        int DepositType = 0;
        GregorianCalendar cal = new GregorianCalendar();
        try {
            // Matured and Closed within financial year.
            ResultSet rsTDSClose = data.getResult("SELECT * FROM D_FD_DEPOSIT_MASTER WHERE DEPOSIT_STATUS=1 AND PARTY_CODE='"+PartyCode+"' AND MATURITY_DATE>='"+StartFinYear+"' AND MATURITY_DATE<='"+EndFinYear+"' AND APPROVED=1 AND CANCELLED=0",FinanceGlobal.FinURL);
            rsTDSClose.first();
            if(rsTDSClose.getRow() > 0 ) {
                while(!rsTDSClose.isAfterLast()) {
                    DepositType = rsTDSClose.getInt("DEPOSIT_TYPE_ID");
                    Amount = rsTDSClose.getDouble("AMOUNT");
                    Rate = rsTDSClose.getDouble("INTEREST_RATE");
                    MaturityDate = rsTDSClose.getString("MATURITY_DATE");
                    String StartDate = "";
                    if(DepositType==2)  {
                        ResultSet rsTmp = data.getResult("SELECT A.INTEREST_AMOUNT FROM D_FD_INT_CALC_DETAIL A, D_FD_INT_CALC_HEADER B WHERE A.RECEIPT_NO='"+rsTDSClose.getString("RECEIPT_NO")+"' AND A.DOC_NO=B.DOC_NO AND B.TDS_ONLY=0  AND B.APPROVED=1 AND B.CANCELLED=0",FinanceGlobal.FinURL);
                        rsTmp.first();
                        if(rsTmp.getRow() > 0) {
                            while(!rsTmp.isAfterLast()) {
                                Amount += rsTmp.getDouble("INTEREST_AMOUNT");
                                rsTmp.next();
                            }
                        }
                        rsTmp.close();
                    }
                    DiffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(StartFinYear), java.sql.Date.valueOf(MaturityDate));
                    StartDate = StartFinYear;
                    //if (cal.isLeapYear(EITLERPGLOBAL.getCurrentFinYear()) ) {
                    if (cal.isLeapYear(Integer.parseInt(EITLERPGLOBAL.getFinYearEndDate(StartDate).substring(0,4)))) {
                        interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36600,0);
                    } else {
                        interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36500,0);
                    }
                    rsTDSClose.next();
                }
            }
            rsTDSClose.close();
            
            // Open within financial year.
            ResultSet rsTDSOpen = data.getResult("SELECT * FROM D_FD_DEPOSIT_MASTER WHERE DEPOSIT_STATUS=0 AND PARTY_CODE='"+PartyCode+"' AND APPROVED=1 AND CANCELLED=0",FinanceGlobal.FinURL);
            rsTDSOpen.first();
            if(rsTDSOpen.getRow() > 0 ) {
                while(!rsTDSOpen.isAfterLast()) {
                    DepositType = rsTDSOpen.getInt("DEPOSIT_TYPE_ID");
                    Amount = rsTDSOpen.getDouble("AMOUNT");
                    Rate = rsTDSOpen.getDouble("INTEREST_RATE");
                    EffectiveDate = rsTDSOpen.getString("EFFECTIVE_DATE");
                    MaturityDate = rsTDSOpen.getString("MATURITY_DATE");
                    String StartDate = "";
                    if(java.sql.Date.valueOf(EffectiveDate).before(java.sql.Date.valueOf(StartFinYear))) {
                        if(java.sql.Date.valueOf(MaturityDate).before(java.sql.Date.valueOf(EndFinYear))){
                            DiffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(StartFinYear), java.sql.Date.valueOf(MaturityDate));
                            StartDate = StartFinYear;
                        } else {
                            DiffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(StartFinYear), java.sql.Date.valueOf(EndFinYear))+1;
                            StartDate = StartFinYear;
                        }
                    } else {
                        DiffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(EffectiveDate), java.sql.Date.valueOf(EndFinYear))+1;
                        StartDate = EffectiveDate;
                    }
                    
                    if(DepositType==2)  {
                        String RNo = rsTDSOpen.getString("RECEIPT_NO");
                        ResultSet rsTmp = data.getResult("SELECT A.INTEREST_AMOUNT FROM D_FD_INT_CALC_DETAIL A, D_FD_INT_CALC_HEADER B WHERE A.RECEIPT_NO='"+RNo+"' AND A.DOC_NO=B.DOC_NO AND B.TDS_ONLY=0  AND B.APPROVED=1 AND B.CANCELLED=0",FinanceGlobal.FinURL);
                        rsTmp.first();
                        if(rsTmp.getRow() > 0) {
                            while(!rsTmp.isAfterLast()) {
                                Amount += rsTmp.getDouble("INTEREST_AMOUNT");
                                rsTmp.next();
                            }
                        }
                        rsTmp.close();
                        //}
                    }
                    //if (cal.isLeapYear(EITLERPGLOBAL.getCurrentFinYear()+1) ) {
                    if (cal.isLeapYear(Integer.parseInt(EITLERPGLOBAL.getFinYearEndDate(StartDate).substring(0,4)))) {
                        interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36600,0);
                    } else {
                        interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36500,0);
                    }
                    rsTDSOpen.next();
                }
            }
            rsTDSOpen.close();
            
            // Premature within financial year.
            ResultSet rsTDSPM = data.getResult("SELECT * FROM D_FD_DEPOSIT_MASTER WHERE DEPOSIT_STATUS=2 AND PARTY_CODE='"+PartyCode+"' AND PM_DATE>='"+StartFinYear+"' AND PM_DATE<='"+EndFinYear+"'  AND APPROVED=1 AND CANCELLED=0",FinanceGlobal.FinURL);
            rsTDSPM.first();
            double PMAmount = 0.0;
            String tmpDate1 = "";
            if(rsTDSPM.getRow() > 0) {
                while(!rsTDSPM.isAfterLast()) {
                    DepositType = rsTDSPM.getInt("DEPOSIT_TYPE_ID");
                    Amount = rsTDSPM.getDouble("AMOUNT");
                    Rate = rsTDSPM.getDouble("INTEREST_RATE")-1;
                    EffectiveDate = rsTDSPM.getString("EFFECTIVE_DATE");
                    PMDate = rsTDSPM.getString("PM_DATE");
                    int Months = data.getIntValueFromDB("SELECT INTEREST_CALCULATION_PERIOD FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+rsTDSPM.getString("SCHEME_ID")+"' ",FinanceGlobal.FinURL);
                    double nyear = Months/12.0;
                    double nTimes = 12.0/Months;
                    String tmpDate = clsCalcInterest.addMonthToDate(EffectiveDate, 6);
                    String StartDate = "";
                    if(java.sql.Date.valueOf(tmpDate).before(java.sql.Date.valueOf(PMDate))) {
                        if(java.sql.Date.valueOf(EffectiveDate).after(java.sql.Date.valueOf(StartFinYear))) {
                            if(DepositType == 2 ) {
                                while(java.sql.Date.valueOf(tmpDate).before(java.sql.Date.valueOf(PMDate))) {
                                    PMAmount = EITLERPGLOBAL.round((Amount * Math.pow((1 + ((Rate/100)/nTimes)),nTimes*nyear)) - Amount,0);
                                    tmpDate1 = tmpDate;
                                    tmpDate = clsCalcInterest.addMonthToDate(tmpDate, Months);
                                    Amount += PMAmount;
                                }
                                DiffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(tmpDate1), java.sql.Date.valueOf(PMDate));
                                StartDate=tmpDate1;
                                //if (cal.isLeapYear(EITLERPGLOBAL.getYear(tmpDate1)+1) ) {
                                if (cal.isLeapYear(Integer.parseInt(EITLERPGLOBAL.getFinYearEndDate(StartDate).substring(0,4)))) {
                                    interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36600,0);
                                } else {
                                    interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36500,0);
                                }
                            } else {
                                DiffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(EffectiveDate), java.sql.Date.valueOf(PMDate));
                                StartDate=EffectiveDate;
                                //if (cal.isLeapYear(EITLERPGLOBAL.getYear(tmpDate1)+1) ) {
                                if (cal.isLeapYear(Integer.parseInt(EITLERPGLOBAL.getFinYearEndDate(StartDate).substring(0,4)))) {
                                    interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36600,0);
                                } else {
                                    interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36500,0);
                                }
                            }
                        } else {
                            if(DepositType == 2) {
                                while(java.sql.Date.valueOf(EffectiveDate).before(java.sql.Date.valueOf(PMDate)) && java.sql.Date.valueOf(EffectiveDate).before(java.sql.Date.valueOf(StartFinYear))) {
                                    PMAmount = EITLERPGLOBAL.round((Amount * Math.pow((1 + ((Rate/100)/nTimes)),nTimes*nyear)) - Amount,0);
                                    tmpDate1 = EffectiveDate;
                                    EffectiveDate = clsCalcInterest.addMonthToDate(EffectiveDate, Months);
                                    Amount += PMAmount;
                                }
                                DiffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(StartFinYear), java.sql.Date.valueOf(PMDate));
                                StartDate=StartFinYear;
                                //if (cal.isLeapYear(EITLERPGLOBAL.getYear(StartFinYear)+1) ) {
                                if (cal.isLeapYear(Integer.parseInt(EITLERPGLOBAL.getFinYearEndDate(StartDate).substring(0,4)))) {
                                    interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36600,0);
                                } else {
                                    interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36500,0);
                                }
                            }
                        }
                    }
                    rsTDSPM.next();
                }
            }
            rsTDSPM.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return interestAmount;
    }
    
    public static double calculateTDSAmount(double intAmount) {
        double TDSPercentage = 0;
        double TDSAmount = 0;
        try {
            HashMap List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsCalcInterest.ModuleID, "GET_TDS_PERCENTAGE", "PERCENTAGE", "0");
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
    
    public static HashMap getLiveReceiptList(String PartyCode) {
         HashMap List = new HashMap();
         try {
          ResultSet rsData = data.getResult("SELECT * FROM D_FD_DEPOSIT_MASTER WHERE DEPOSIT_STATUS=0 AND APPROVED=1 AND CANCELLED=0 AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
           rsData.first();
           clsDepositDeathItems objItem = null;
          int Counter=0;
          if(rsData.getRow()>0) {
            while(!rsData.isAfterLast()) {
              objItem = new clsDepositDeathItems();
                Counter++;
                 objItem.setAttribute("SR_NO",Counter);
                  objItem.setAttribute("RECEIPT_NO",rsData.getString("RECEIPT_NO"));
                  objItem.setAttribute("RECEIPT_DATE",rsData.getString("RECEIPT_DATE"));
                  objItem.setAttribute("EFFECTIVE_DATE",rsData.getString("EFFECTIVE_DATE"));
                  objItem.setAttribute("MATURITY_DATE",rsData.getString("MATURITY_DATE"));
                  objItem.setAttribute("MAIN_ACCOUNT_CODE",rsData.getString("MAIN_ACCOUNT_CODE"));
                  objItem.setAttribute("AMOUNT",rsData.getDouble("AMOUNT"));
                   List.put(Integer.toString(List.size()+1), objItem);
                  rsData.next();
                   }
                 }
           } catch(Exception e) {
          e.printStackTrace();
          return List;
         }
         return List;
    }
    
}//end of the class