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

public class clsDepositRenew {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
    private boolean HistoryView=false;
    
    public static int ModuleID=87;
    
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
    public clsDepositRenew() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("RECEIPT_NO",new Variant(""));
        props.put("PARTY_CODE",new Variant(""));
        props.put("TRANSFER_TO_PARTY",new Variant(""));
        props.put("RECEIPT_DATE",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("FFNO",new Variant(0));
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("REJECTED_REMARKS", new Variant(""));
        props.put("CANCELLED", new Variant(false));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        
        // -- For Update of Deposit Master --
        props.put("PREVIOUS_RECEIPT_NO",new Variant(""));
    }
    
    /**Load Data. This method loads data from database to Business Object**/
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FD_DEPOSIT_RENEW ORDER BY DOC_NO");
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
        
        Statement stHistory,stTmpH,stTmp,stTmpOld;
        ResultSet rsHistory,rsTmpH,rsTmp,rsTmpOld;
        String newReceiptNo="";
        try {
            
            //===== History Related Changes =====//
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FD_DEPOSIT_RENEW_H WHERE DOC_NO='1'");
            rsHistory.first();
            //----------------------------------//
            
            //** Validations **//
            if(!Validate()) {
                return false;
            }
            
            
            if(data.IsRecordExist("SELECT DOC_NO FROM D_FD_DEPOSIT_RENEW WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"'",FinanceGlobal.FinURL)) {
                LastError="Document with this Doc No. already exist.";
                return false;
            }
            //*****************//
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //========= Inserting Into Deposit Renew =================//
            
            setAttribute("DOC_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,87,getAttribute("FFNO").getInt(),true));
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsResultSet.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            String DocNo = getAttribute("DOC_NO").getString();
            rsResultSet.updateString("DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsResultSet.updateString("RECEIPT_NO",getAttribute("RECEIPT_NO").getString());
            rsResultSet.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsResultSet.updateString("TRANSFER_TO_PARTY",getAttribute("TRANSFER_TO_PARTY").getString());
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
            rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
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
            rsHistory.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsHistory.updateString("TRANSFER_TO_PARTY",getAttribute("TRANSFER_TO_PARTY").getString());
            rsHistory.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
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
            ObjFlow.TableName="D_FD_DEPOSIT_RENEW";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=getAttribute("HIERARCHY_ID").getInt();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName="DOC_NO";
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
            ObjFlow.UseSpecifiedURL=false;
            //--------- Approval Flow Update complete -----------
            
            MoveLast();
            if(ObjFlow.Status.equals("F")) {
                //int CompanyID = getAttribute("COMPANY_ID").getInt();
                //String ReceiptNo = getAttribute("RECEIPT_NO").getString();
                String ReceiptNo = data.getStringValueFromDB("SELECT RECEIPT_NO FROM D_FD_DEPOSIT_RENEW WHERE DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
                int CompanyID = data.getIntValueFromDB("SELECT COMPANY_ID FROM D_FD_DEPOSIT_RENEW WHERE DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
                if(!PostReceipt(CompanyID,ReceiptNo,DocNo)) {
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
    
    private boolean PostReceipt(int CompanyID,String ReceiptNo, String DocNo) {
        
        try {
            String SelPrefix="";
            String SelSuffix="";
            int FFNo=0;
            
            //****** Prepare Voucher Object ********//
            setAttribute("FIN_HIERARCHY_ID",0);
            
            HashMap List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsDepositRenew.ModuleID , "CHOOSE_HIERARCHY", "DEFAULT", "");
            
            if(List.size()>0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                int HierarchyID=UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                setAttribute("FIN_HIERARCHY_ID",HierarchyID);
            }
            
            ResultSet rsTmp=data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID="+clsDepositMaster.ModuleID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                SelPrefix=rsTmp.getString("PREFIX_CHARS");
                SelSuffix=rsTmp.getString("SUFFIX_CHARS");
                FFNo=rsTmp.getInt("FIRSTFREE_NO");
            }
            
            String tmpsql = "SELECT * FROM FINANCE.D_FD_DEPOSIT_MASTER WHERE COMPANY_ID="+CompanyID+" AND RECEIPT_NO='"+ReceiptNo+"' ";
            ResultSet rsReceipt = data.getResult(tmpsql,FinanceGlobal.FinURL);
            String newReceiptNo = clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID,85,FFNo, false);
            
            clsDepositMaster objDepositMaster = new clsDepositMaster();
            objDepositMaster.LoadData(CompanyID);
            objDepositMaster.RenewEntry=true;
            //Applicant Detail
            objDepositMaster.setAttribute("FFNO",FFNo);
            objDepositMaster.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsReceipt, "COMPANY_ID",0));
            objDepositMaster.setAttribute("RECEIPT_NO",newReceiptNo);
            objDepositMaster.setAttribute("LEGACY_NO","");
            objDepositMaster.setAttribute("RECEIPT_DATE",EITLERPGLOBAL.formatDate(EITLERPGLOBAL.getCurrentDateDB()));
            objDepositMaster.setAttribute("TITLE",UtilFunctions.getString(rsReceipt,"TITLE",""));
            objDepositMaster.setAttribute("APPLICANT_NAME",UtilFunctions.getString(rsReceipt,"APPLICANT_NAME",""));
            objDepositMaster.setAttribute("ADDRESS1",UtilFunctions.getString(rsReceipt,"ADDRESS1",""));
            objDepositMaster.setAttribute("ADDRESS2",UtilFunctions.getString(rsReceipt,"ADDRESS2",""));
            objDepositMaster.setAttribute("ADDRESS3",UtilFunctions.getString(rsReceipt, "ADDRESS3",""));
            objDepositMaster.setAttribute("CITY",UtilFunctions.getString(rsReceipt,"CITY",""));
            objDepositMaster.setAttribute("PINCODE",UtilFunctions.getString(rsReceipt,"PINCODE",""));
            objDepositMaster.setAttribute("CONTACT_NO",UtilFunctions.getString(rsReceipt,"CONTACT_NO", ""));
            objDepositMaster.setAttribute("APPLICANT2",UtilFunctions.getString(rsReceipt,"APPLICANT2", ""));
            objDepositMaster.setAttribute("APPLICANT3",UtilFunctions.getString(rsReceipt,"APPLICANT3", ""));
            objDepositMaster.setAttribute("APPLICANT4",UtilFunctions.getString(rsReceipt,"APPLICANT4", ""));
            objDepositMaster.setAttribute("NOMINEE_1_NAME",UtilFunctions.getString(rsReceipt,"NOMINEE_1_NAME",""));
            objDepositMaster.setAttribute("NOMINEE_2_NAME",UtilFunctions.getString(rsReceipt, "NOMINEE_2_NAME",""));
            objDepositMaster.setAttribute("NOMINEE_3_NAME",UtilFunctions.getString(rsReceipt, "NOMINEE_3_NAME",""));
            
            //Other Detail
            String SchemeID = UtilFunctions.getString(rsReceipt,"SCHEME_ID","");
            objDepositMaster.setAttribute("SCHEME_ID",UtilFunctions.getString(rsReceipt,"SCHEME_ID",""));
            objDepositMaster.setAttribute("DEPOSIT_TYPE_ID",UtilFunctions.getInt(rsReceipt, "DEPOSIT_TYPE_ID",0));
            objDepositMaster.setAttribute("DEPOSIT_PAYABLE_TO",UtilFunctions.getInt(rsReceipt, "DEPOSIT_PAYABLE_TO",0));
            objDepositMaster.setAttribute("DEPOSITER_STATUS",UtilFunctions.getInt( rsReceipt,"DEPOSITER_STATUS",0));
            objDepositMaster.setAttribute("DEPOSITER_CATEGORY",UtilFunctions.getInt( rsReceipt,"DEPOSITER_CATEGORY",0));
            
            objDepositMaster.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsReceipt,"MAIN_ACCOUNT_CODE",""));
            objDepositMaster.setAttribute("INTEREST_MAIN_CODE",UtilFunctions.getString(rsReceipt,"INTEREST_MAIN_CODE",""));
            int DepositPeriod = UtilFunctions.getInt( rsReceipt,"DEPOSIT_PERIOD",0);
            objDepositMaster.setAttribute("DEPOSIT_PERIOD",UtilFunctions.getInt( rsReceipt,"DEPOSIT_PERIOD",0));
            objDepositMaster.setAttribute("INTEREST_RATE",UtilFunctions.getDouble(rsReceipt, "INTEREST_RATE",0.0));
            objDepositMaster.setAttribute("DEPOSITER_CATEGORY_OTHERS",UtilFunctions.getString( rsReceipt,"DEPOSITER_CATEGORY_OTHERS",""));
            objDepositMaster.setAttribute("FOLIO_NO",UtilFunctions.getString( rsReceipt,"FOLIO_NO",""));
            objDepositMaster.setAttribute("EMPLOYEE_CODE",UtilFunctions.getString( rsReceipt,"EMPLOYEE_CODE",""));
            
            //Extra Calculation
            String MaturityDate = UtilFunctions.getString( rsReceipt,"MATURITY_DATE","0000-00-00");
            String EffectiveDate = UtilFunctions.getString( rsReceipt,"EFFECTIVE_DATE","0000-00-00");
            EffectiveDate = MaturityDate;
            MaturityDate = clsCalcInterest.addMonthToDate(EffectiveDate, DepositPeriod);
            int InterestCalcPeriod = data.getIntValueFromDB("SELECT INTEREST_CALCULATION_PERIOD FROM D_FD_SCHEME_MASTER WHERE SCHEME='"+SchemeID+"' ",FinanceGlobal.FinURL);
            String InterestCalcDate = clsCalcInterest.addMonthToDate(EffectiveDate, InterestCalcPeriod);
            objDepositMaster.setAttribute("EFFECTIVE_DATE",EITLERPGLOBAL.formatDate(EffectiveDate));
            objDepositMaster.setAttribute("MATURITY_DATE",EITLERPGLOBAL.formatDate(MaturityDate));
            objDepositMaster.setAttribute("INT_CALC_DATE",EITLERPGLOBAL.formatDate(InterestCalcDate));
            //End of Extra Calculation
            
            objDepositMaster.setAttribute("TAX_EX_FORM_RECEIVED",UtilFunctions.getInt( rsReceipt,"TAX_EX_FORM_RECEIVED",0));
            String TPartyCode = data.getStringValueFromDB("SELECT TRANSFER_TO_PARTY FROM D_FD_DEPOSIT_RENEW WHERE DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
            if(TPartyCode.equals("")) {
                objDepositMaster.setAttribute("PARTY_CODE",UtilFunctions.getString( rsReceipt,"PARTY_CODE",""));
            } else {
                objDepositMaster.setAttribute("PARTY_CODE",TPartyCode);
                String PartyName = data.getStringValueFromDB("SELECT PARTY_NAME FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+TPartyCode+"'",FinanceGlobal.FinURL);
                objDepositMaster.setAttribute("APPLICANT_NAME",PartyName);
            }
            
            objDepositMaster.setAttribute("TDS_APPLICABLE",UtilFunctions.getInt( rsReceipt,"TDS_APPLICABLE",0));
            objDepositMaster.setAttribute("OLD_RECEIPT_NO",ReceiptNo);
            objDepositMaster.setAttribute("PAN_NO",UtilFunctions.getString(rsReceipt, "PAN_NO",""));
            objDepositMaster.setAttribute("PAN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString( rsReceipt,"PAN_DATE","0000-00-00")));
            objDepositMaster.setAttribute("PARTICULARS",UtilFunctions.getString( rsReceipt,"PARTICULARS",""));
            
            //Bank Detail
            objDepositMaster.setAttribute("CHEQUE_NO","");
            objDepositMaster.setAttribute("CHEQUE_DATE","0000-00-00");
            objDepositMaster.setAttribute("REALIZATION_DATE","0000-00-00");
            objDepositMaster.setAttribute("DEPOSIT_DATE","0000-00-00");
            objDepositMaster.setAttribute("AMOUNT",UtilFunctions.getDouble( rsReceipt,"AMOUNT",0.0));
            
            objDepositMaster.setAttribute("BANK_MAIN_CODE","");
            objDepositMaster.setAttribute("BANK_NAME","");
            objDepositMaster.setAttribute("BANK_ADDRESS","");
            objDepositMaster.setAttribute("BANK_CITY","");
            objDepositMaster.setAttribute("BANK_PINCODE","");
            
            objDepositMaster.setAttribute("BROKER_CODE",UtilFunctions.getString( rsReceipt,"BROKER_CODE",""));
            objDepositMaster.setAttribute("BROKER_NAME",UtilFunctions.getString( rsReceipt,"BROKER_NAME",""));
            objDepositMaster.setAttribute("BROKER_ADDRESS",UtilFunctions.getString( rsReceipt,"BROKER_ADDRESS",""));
            objDepositMaster.setAttribute("BROKER_CITY",UtilFunctions.getString( rsReceipt,"BROKER_CITY",""));
            objDepositMaster.setAttribute("BROKER_PINCODE",UtilFunctions.getString(rsReceipt, "BROKER_PINCODE",""));
            
            //Deposit Releated Information
            objDepositMaster.setAttribute("DEPOSIT_ENTRY_TYPE",2); //2 for Renewal Entry
            objDepositMaster.setAttribute("PREMATURE",UtilFunctions.getInt(rsReceipt,"PREMATURE",0));
            objDepositMaster.setAttribute("PM_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsReceipt,"PM_DATE","0000-00-00")));
            objDepositMaster.setAttribute("DEPOSIT_STATUS",0); // 0 for Open
            
            objDepositMaster.setAttribute("HIERARCHY_ID",getAttribute("FIN_HIERARCHY_ID").getInt());
            int FirstUserID=data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID="+getAttribute("FIN_HIERARCHY_ID").getInt()+" AND SR_NO=1");
            objDepositMaster.setAttribute("FROM",FirstUserID);
            objDepositMaster.setAttribute("TO",FirstUserID);
            objDepositMaster.setAttribute("FROM_REMARKS","");
            objDepositMaster.setAttribute("APPROVAL_STATUS","H"); //Hold Receipt
            
            if(objDepositMaster.Insert()) {
                //data.Execute("UPDATE D_FD_DEPOSIT_MASTER SET DEPOSIT_STATUS=1 WHERE COMPANY_ID="+CompanyID+" AND RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                objDepositMaster.RenewEntry=false;
                return true;
            }
            else {
                objDepositMaster.RenewEntry=false;
                return false;
            }
        } catch(Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }
    
    public boolean Update() {
        
        Statement stHistory,stTmp,stTmpH,stTmpOld;
        ResultSet rsHistory,rsTmp,rsTmpH,rsTmpOld;
        boolean Validate=true;
        String newReceiptNo="";
        
        try {
            String theDocNo=getAttribute("DOC_NO").getString();
            
            //** Validations **//
            if(!Validate()) {
                return false;
            }
            
            if(data.IsRecordExist("SELECT DOC_NO FROM D_FD_DEPOSIT_RENEW WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"' AND DOC_NO<>'"+theDocNo+"'",FinanceGlobal.FinURL)) {
                LastError="Account with this main account code already exist.";
                return false;
            }
            //*****************//
            
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            String AStatus=getAttribute("APPROVAL_STATUS").getString();
            if(AStatus.equals("R")) {
                Validate=false; //Do not validate while in rejection mode.
            }
            
            //** Open History Table Connections **//
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FD_DEPOSIT_RENEW_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //** --------------------------------**//
            
            //========= Updating Into Deposit Renew =================//
            
            rsResultSet.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            String DocNo = getAttribute("DOC_NO").getString();
            rsResultSet.updateString("DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsResultSet.updateString("RECEIPT_NO",getAttribute("RECEIPT_NO").getString());
            rsResultSet.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsResultSet.updateString("TRANSFER_TO_PARTY",getAttribute("TRANSFER_TO_PARTY").getString());
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
            //rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            //rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateRow();
            
            //------------------------------------------------------//
            
            //========= Inserting Into History =================//
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM D_FD_DEPOSIT_RENEW_H WHERE DOC_NO='"+theDocNo+"'",FinanceGlobal.FinURL);
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
            rsHistory.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsHistory.updateString("TRANSFER_TO_PARTY",getAttribute("TRANSFER_TO_PARTY").getString());
            rsHistory.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
            rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
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
            ObjFlow.TableName="D_FD_DEPOSIT_RENEW";
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
                data.Execute("UPDATE D_FD_DEPOSIT_RENEW SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO="+theDocNo+"'",FinanceGlobal.FinURL);
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
                //int CompanyID = getAttribute("COMPANY_ID").getInt();
                //String ReceiptNo = getAttribute("RECEIPT_NO").getString();
                String ReceiptNo = data.getStringValueFromDB("SELECT RECEIPT_NO FROM D_FD_DEPOSIT_RENEW WHERE DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
                int CompanyID = data.getIntValueFromDB("SELECT COMPANY_ID FROM D_FD_DEPOSIT_RENEW WHERE DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
                if(!PostReceipt(CompanyID,ReceiptNo, DocNo)) {
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FD_DEPOSIT_RENEW WHERE DOC_NO='"+DocNo+"' AND (APPROVED=1)";
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
            strSQL="SELECT DOC_NO FROM D_FD_DEPOSIT_RENEW WHERE DOC_NO='"+DocNo+"' AND (APPROVED=1)";
            
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
                String strSQL = "DELETE FROM D_FD_DEPOSIT_RENEW WHERE DOC_NO='"+DocNo+"' ";
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
        clsDepositRenew objDepositRenew = new clsDepositRenew();
        objDepositRenew.Filter(strCondition,CompanyID);
        return objDepositRenew;
    }
    
    public boolean Filter(String Condition,int CompanyID) {
        Ready=false;
        
        try {
            String strSQL = "SELECT A.* FROM D_FD_DEPOSIT_RENEW A, D_FD_DEPOSIT_MASTER B " + Condition ;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSQL);
            
            if(!rsResultSet.first()) {
                strSQL = "SELECT * FROM D_FD_DEPOSIT_RENEW ORDER BY DOC_NO";
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
            setAttribute("PARTY_CODE",UtilFunctions.getString(rsResultSet,"PARTY_CODE",""));
            setAttribute("TRANSFER_TO_PARTY",UtilFunctions.getString(rsResultSet,"TRANSFER_TO_PARTY",""));
            setAttribute("PREVIOUS_RECEIPT_NO",UtilFunctions.getString(rsResultSet,"RECEIPT_NO",""));
            String ReceiptDate = data.getStringValueFromDB("SELECT RECEIPT_DATE FROM D_FD_DEPOSIT_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND RECEIPT_NO='"+UtilFunctions.getString(rsResultSet,"RECEIPT_NO","")+"'", FinanceGlobal.FinURL);
            setAttribute("RECEIPT_DATE",ReceiptDate);
            setAttribute("REMARKS",UtilFunctions.getString(rsResultSet,"REMARKS",""));
            setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsResultSet,"HIERARCHY_ID",0));
            setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00")));
            setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00")));
            setAttribute("CHANGED",UtilFunctions.getBoolean(rsResultSet,"CHANGED",false));
            setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHANGED_DATE","0000-00-00")));
            setAttribute("APPROVED",UtilFunctions.getBoolean(rsResultSet,"APPROVED",false));
            setAttribute("APPROVED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"APPROVED_DATE","0000-00-00")));
            setAttribute("REJECTED",UtilFunctions.getBoolean(rsResultSet,"REJECTED",false));
            setAttribute("REJECTED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"REJECTED_DATE","0000-00-00")));
            setAttribute("REJECTED_REMARKS",UtilFunctions.getString(rsResultSet,"REJECTED_REMARKS",""));
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
                strSQL="SELECT FINANCE.D_FD_DEPOSIT_RENEW.DOC_NO,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_DEPOSIT_RENEW,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_DEPOSIT_RENEW.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT FINANCE.D_FD_DEPOSIT_RENEW.DOC_NO,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_DEPOSIT_RENEW,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_DEPOSIT_RENEW.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_FD_DEPOSIT_RENEW.DOC_NO";
            }
            
            if(Order==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT FINANCE.D_FD_DEPOSIT_RENEW.DOC_NO,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_DEPOSIT_RENEW,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_DEPOSIT_RENEW.PARTY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_FD_DEPOSIT_RENEW.DOC_NO";
            }
            
            rsTemp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTemp.first();
            
            Counter=0;
            
            if(rsTemp.getRow()>0) {
                while(!rsTemp.isAfterLast()) {
                    Counter=Counter+1;
                    clsDepositRenew ObjDoc=new clsDepositRenew();
                    
                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("DOC_NO",UtilFunctions.getInt(rsTemp,"DOC_NO",0));
                    ObjDoc.setAttribute("RECEIVED_DATE",UtilFunctions.getString(rsTemp,"RECEIVED_DATE","0000-00-00"));
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
            String strSQL="SELECT * FROM D_FD_DEPOSIT_RENEW_H WHERE DOC_NO="+DocNo;
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
            String strSQL="SELECT * FROM D_FD_DEPOSIT_RENEW_H WHERE DOC_NO='"+DocNo+"'";
            rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsDepositRenew objDR=new clsDepositRenew();
                    
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
            
            String strSQL="SELECT DOC_NO,APPROVED,CANCELLED FROM D_FD_DEPOSIT_RENEW WHERE DOC_NO='"+DocNo+"'";
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
            rsTmp=data.getResult("SELECT DOC_NO FROM D_FD_DEPOSIT_RENEW WHERE DOC_NO='"+DocNo+"' AND CANCELLED=0",FinanceGlobal.FinURL);
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
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_FD_DEPOSIT_RENEW WHERE DOC_NO='"+DocNo+"'",FinanceGlobal.FinURL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                if(ApprovedDoc) {
                }
                else {
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"' AND MODULE_ID="+ModuleID,FinanceGlobal.FinURL);
                }
                
                data.Execute("UPDATE D_FD_DEPOSIT_RENEW SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='"+DocNo+"'",FinanceGlobal.FinURL);
                Cancelled=true;
            }
            rsTmp.close();
        }
        catch(Exception e) {
        }
        return Cancelled;
    }
    
    private boolean Validate() {
        
        if(getAttribute("RECEIPT_NO").getObj().toString().equals("")) {
            LastError="Please specify Receipt No.";
            return false;
        } else if(!data.IsRecordExist("SELECT * FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+getAttribute("RECEIPT_NO").getString().trim()+"' ",FinanceGlobal.FinURL)) {
            LastError="Receipt No. is not valid.";
            return false;
        }
        return true;
    }
}