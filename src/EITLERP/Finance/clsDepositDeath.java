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

public class clsDepositDeath {
    public static int ModuleID=200;
    
    //*********** Local reference of data class ***************//
    public HashMap colHistory=new HashMap();
    Statement stDepositDeath, stDepositDeathH;
    ResultSet rsDepositDeath, rsDepositDeathH;
    private Connection Conn;
    private Statement Stmt;
    
    public String LastError="";
    public String BookCode="";
    //Indent Line Items Collection
    public HashMap colLineItems;
    
    
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
    
    public clsDepositDeath() {
        LastError = "";
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("PARTY_CODE",new Variant(""));
        props.put("TRANSFER_PARTY_CODE",new Variant(""));
        props.put("TRANSFER_DATE",new Variant(""));
        props.put("APPLICANT2",new Variant(""));
        props.put("APPLICANT3",new Variant(""));
        props.put("APPLICANT4",new Variant(""));
        props.put("ADDRESS1",new Variant(""));
        props.put("ADDRESS2",new Variant(""));
        props.put("ADDRESS3",new Variant(""));
        props.put("PAN_NO",new Variant(""));
        props.put("PAN_DATE",new Variant(""));
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
        
        colLineItems= new HashMap();
    }
    
    public boolean LoadData(int pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDepositDeath=Stmt.executeQuery("SELECT * FROM D_FD_DEPOSIT_DEATH_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" ORDER BY DOC_NO");
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
            rsDepositDeath.close();
        }
        catch(Exception e) {
        }
    }
    
    //Navigation Methods
    public boolean MoveFirst() {
        try {
            rsDepositDeath.first();
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
            if(rsDepositDeath.isAfterLast()||rsDepositDeath.isLast()) {
                //Move pointer at last record
                //If it is beyond eof
                rsDepositDeath.last();
            }
            else {
                rsDepositDeath.next();
                if(rsDepositDeath.isAfterLast()) {
                    rsDepositDeath.last();
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
            if(rsDepositDeath.isFirst()||rsDepositDeath.isBeforeFirst()) {
                rsDepositDeath.first();
            }
            else {
                rsDepositDeath.previous();
                if(rsDepositDeath.isBeforeFirst()) {
                    rsDepositDeath.first();
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
            rsDepositDeath.last();
            SetData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public boolean Insert() {
        Statement stHDetail;
        ResultSet rsHDetail;
        
        try {
            //===== History Related Changes =====//
            stDepositDeathH = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsDepositDeathH = stDepositDeathH.executeQuery("SELECT * FROM D_FD_DEPOSIT_DEATH_HEADER_H WHERE DOC_NO='1'");
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FD_DEPOSIT_DEATH_DETAIL_H WHERE DOC_NO='1'");
            rsHDetail.first();
            //===================================//
            
            if(!Validate()) {
                return false;
            }
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //=============================Generate New ReceiptNo.===============================
            setAttribute("DOC_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,clsDepositDeath.ModuleID,(int)getAttribute("FFNO").getVal(),true));
            //===============================================================================================
            
            //****************** Insert Records ********************//
            rsDepositDeath.moveToInsertRow();
            //Applicant Detail
            rsDepositDeath.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            int CompanyID = EITLERPGLOBAL.gCompanyID;
            rsDepositDeath.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            String DocNo = getAttribute("DOC_NO").getString();
            rsDepositDeath.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsDepositDeath.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsDepositDeath.updateString("TRANSFER_PARTY_CODE", getAttribute("TRANSFER_PARTY_CODE").getString());
            rsDepositDeath.updateString("TRANSFER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("TRANSFER_DATE").getString()));
            rsDepositDeath.updateString("APPLICANT2", getAttribute("APPLICANT2").getString());
            rsDepositDeath.updateString("APPLICANT3", getAttribute("APPLICANT3").getString());
            rsDepositDeath.updateString("APPLICANT4", getAttribute("APPLICANT4").getString());
            rsDepositDeath.updateString("ADDRESS1", getAttribute("ADDRESS1").getString());
            rsDepositDeath.updateString("ADDRESS2", getAttribute("ADDRESS2").getString());
            rsDepositDeath.updateString("ADDRESS3", getAttribute("ADDRESS3").getString());
            
            rsDepositDeath.updateString("PAN_NO", getAttribute("PAN_NO").getString());
            rsDepositDeath.updateString("PAN_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PAN_DATE").getString()));
            
            rsDepositDeath.updateString("REMARKS", getAttribute("REMARKS").getString());
            
            rsDepositDeath.updateBoolean("APPROVED",false);
            rsDepositDeath.updateString("APPROVED_DATE","0000-00-00");
            rsDepositDeath.updateBoolean("REJECTED", false);
            rsDepositDeath.updateString("REJECTED_DATE", "0000-00-00");
            rsDepositDeath.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsDepositDeath.updateString("CREATED_BY", getAttribute("CREATED_BY").getString());
            rsDepositDeath.updateString("CREATED_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_BY").getString()));
            rsDepositDeath.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            rsDepositDeath.updateBoolean("CHANGED", true);
            rsDepositDeath.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsDepositDeath.updateBoolean("CANCELLED", false);
            rsDepositDeath.insertRow();
            
            
            //******************** History Update ************************//
            
            rsDepositDeathH.moveToInsertRow() ;
            rsDepositDeathH.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsDepositDeathH.updateInt("REVISION_NO", 1); //Revision No. 1 in case of Insert
            rsDepositDeathH.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsDepositDeathH.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsDepositDeathH.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsDepositDeathH.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            
            rsDepositDeathH.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsDepositDeathH.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsDepositDeathH.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsDepositDeathH.updateString("TRANSFER_PARTY_CODE", getAttribute("TRANSFER_PARTY_CODE").getString());
            rsDepositDeathH.updateString("TRANSFER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("TRANSFER_DATE").getString()));
            rsDepositDeathH.updateString("APPLICANT2", getAttribute("APPLICANT2").getString());
            rsDepositDeathH.updateString("APPLICANT3", getAttribute("APPLICANT3").getString());
            rsDepositDeathH.updateString("APPLICANT4", getAttribute("APPLICANT4").getString());
            
            rsDepositDeathH.updateString("ADDRESS1", getAttribute("ADDRESS1").getString());
            rsDepositDeathH.updateString("ADDRESS2", getAttribute("ADDRESS2").getString());
            rsDepositDeathH.updateString("ADDRESS3", getAttribute("ADDRESS3").getString());
            
            rsDepositDeathH.updateString("PAN_NO", getAttribute("PAN_NO").getString());
            rsDepositDeathH.updateString("PAN_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PAN_DATE").getString()));
            
            rsDepositDeathH.updateString("REMARKS", getAttribute("REMARKS").getString());
            rsDepositDeathH.updateBoolean("APPROVED",false);
            rsDepositDeathH.updateString("APPROVED_DATE","0000-00-00");
            rsDepositDeathH.updateBoolean("REJECTED", false);
            rsDepositDeathH.updateString("REJECTED_DATE", "0000-00-00");
            rsDepositDeathH.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsDepositDeathH.updateString("CREATED_BY", getAttribute("CREATED_BY").getString());
            rsDepositDeathH.updateString("CREATED_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_BY").getString()));
            rsDepositDeathH.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            rsDepositDeathH.updateBoolean("CHANGED", true);
            rsDepositDeathH.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsDepositDeathH.updateBoolean("CANCELLED", false);
            rsDepositDeathH.insertRow();
            //*************************************************************//
            
            //========== Inserting records Detail =========//
            Statement StmtDetail;
            StmtDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsDetail=StmtDetail.executeQuery("SELECT * FROM D_FD_DEPOSIT_DEATH_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DOC_NO='1'");
            DocNo=getAttribute("DOC_NO").getString();
            
            //long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            
            for(int i=1;i<=colLineItems.size();i++) {
                clsDepositDeathItems ObjItem=(clsDepositDeathItems) colLineItems.get(Integer.toString(i));
                
                rsDetail.moveToInsertRow();
                rsDetail.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsDetail.updateString("DOC_NO",DocNo);
                rsDetail.updateLong("SR_NO",i);
                rsDetail.updateString("RECEIPT_NO",ObjItem.getAttribute("RECEIPT_NO").getString());
                rsDetail.updateString("RECEIPT_DATE",EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("RECEIPT_DATE").getString()));
                rsDetail.updateString("EFFECTIVE_DATE",EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("EFFECTIVE_DATE").getString()));
                rsDetail.updateString("MATURITY_DATE",EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("MATURITY_DATE").getString()));
                rsDetail.updateString("MAIN_ACCOUNT_CODE",ObjItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsDetail.updateDouble("AMOUNT",ObjItem.getAttribute("AMOUNT").getDouble());
                rsDetail.updateString("CREATED_BY", getAttribute("CREATED_BY").getString());
                rsDetail.updateString("CREATED_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_BY").getString()));
                rsDetail.updateBoolean("CHANGED",true);
                rsDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsDetail.updateBoolean("CANCELLED",false);
                rsDetail.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsHDetail.updateInt("REVISION_NO", 1); //Revision No. 1 in case of Insert
                rsHDetail.updateString("DOC_NO",DocNo);
                rsHDetail.updateLong("SR_NO",i);
                rsHDetail.updateString("RECEIPT_NO",ObjItem.getAttribute("RECEIPT_NO").getString());
                rsHDetail.updateString("RECEIPT_DATE",EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("RECEIPT_DATE").getString()));
                rsHDetail.updateString("EFFECTIVE_DATE",EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("EFFECTIVE_DATE").getString()));
                rsHDetail.updateString("MATURITY_DATE",EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("MATURITY_DATE").getString()));
                rsHDetail.updateString("MAIN_ACCOUNT_CODE",ObjItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsHDetail.updateDouble("AMOUNT",ObjItem.getAttribute("AMOUNT").getDouble());
                rsHDetail.updateString("CREATED_BY", getAttribute("CREATED_BY").getString());
                rsHDetail.updateString("CREATED_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_BY").getString()));
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CANCELLED",false);
                rsHDetail.insertRow();
            }
            //========== Inserting records Detail =========//
            
            
            //************ Update Approval Flow *****************//
            ObjFlow.CompanyID =EITLERPGLOBAL.gCompanyID;
            ObjFlow.ModuleID = this.ModuleID ;
            ObjFlow.DocNo = getAttribute("DOC_NO").getString();
            ObjFlow.From =getAttribute("FROM").getInt();
            ObjFlow.To = getAttribute("TO").getInt();
            ObjFlow.Status = getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName = "D_FD_DEPOSIT_DEATH_HEADER";
            ObjFlow.IsCreator = true;
            ObjFlow.HierarchyID =getAttribute("HIERARCHY_ID").getInt();
            ObjFlow.Remarks =getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName ="DOC_NO";
            ObjFlow.UseSpecifiedURL=true;
            ObjFlow.SpecificURL=FinanceGlobal.FinURL;
            ObjFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            
            if (ObjFlow.Status.equals("H")) {
                ObjFlow.Status = "A";
                ObjFlow.To = ObjFlow.From;
                ObjFlow.UpdateFlow();
            } else {
                if (!ObjFlow.UpdateFlow()) {
                    LastError = ObjFlow.LastError;
                }
            }
            ObjFlow.UseSpecifiedURL=false;
            //**************End of Approval Flow *****************//
            
            MoveLast();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LastError=e.getMessage();
            return false;
        }
    }
    
    public boolean Update() {
        
        Statement stTmp,stHistory,stHDetail;
        ResultSet rsDetail,rsHistory,rsHDetail;
        
        try {
            //String ReceiptNo=lDocNo;
            boolean Validate=true;
            
            //** Validations **//
            if(!Validate()) {
                return false;
            }
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            String AStatus=getAttribute("APPROVAL_STATUS").getString();
            
            stDepositDeathH = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsDepositDeathH = stDepositDeathH.executeQuery("SELECT * FROM D_FD_DEPOSIT_DEATH_HEADER_H ");
            
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FD_DEPOSIT_DEATH_DETAIL_H WHERE DOC_NO='1'");
            rsHDetail.first();
            //****************** Update Records ********************//
            //Applicant Detail
            rsDepositDeath.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            int CompanyID = EITLERPGLOBAL.gCompanyID;
            
            String DocNo = getAttribute("DOC_NO").getString();
            String PartyCode = getAttribute("PARTY_CODE").getString();
            String TransferPartyCode = getAttribute("TRANSFER_PARTY_CODE").getString();
            String Address1 = getAttribute("ADDRESS1").getString();
            String Address2 = getAttribute("ADDRESS2").getString();
            String Address3 = getAttribute("ADDRESS3").getString();
            String Applicant2 = getAttribute("APPLICANT2").getString();
            String Applicant3 = getAttribute("APPLICANT3").getString();
            String Applicant4 = getAttribute("APPLICANT4").getString();
            String PanNo = getAttribute("PAN_NO").getString();
            String PanDate = EITLERPGLOBAL.formatDateDB(getAttribute("PAN_DATE").getString());
            
            rsDepositDeath.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            rsDepositDeath.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsDepositDeath.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsDepositDeath.updateString("TRANSFER_PARTY_CODE", getAttribute("TRANSFER_PARTY_CODE").getString());
            rsDepositDeath.updateString("TRANSFER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("TRANSFER_DATE").getString()));
            rsDepositDeath.updateString("APPLICANT2", getAttribute("APPLICANT2").getString());
            rsDepositDeath.updateString("APPLICANT3", getAttribute("APPLICANT3").getString());
            rsDepositDeath.updateString("APPLICANT4", getAttribute("APPLICANT4").getString());
            
            rsDepositDeath.updateString("ADDRESS1", getAttribute("ADDRESS1").getString());
            rsDepositDeath.updateString("ADDRESS2", getAttribute("ADDRESS2").getString());
            rsDepositDeath.updateString("ADDRESS3", getAttribute("ADDRESS3").getString());
            
            rsDepositDeath.updateString("PAN_NO", getAttribute("PAN_NO").getString());
            rsDepositDeath.updateString("PAN_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PAN_DATE").getString()));
            
            rsDepositDeath.updateString("REMARKS", getAttribute("REMARKS").getString());
            //Approval Specific
            rsDepositDeath.updateBoolean("APPROVED", false);
            rsDepositDeath.updateString("APPROVED_DATE", "0000-00-00");
            rsDepositDeath.updateBoolean("REJECTED", false);
            rsDepositDeath.updateString("REJECTED_DATE", "0000-00-00");
            rsDepositDeath.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsDepositDeath.updateString("CREATED_BY", getAttribute("CREATED_BY").getString());
            rsDepositDeath.updateString("CREATED_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_BY").getString()));
            rsDepositDeath.updateString("MODIFIED_BY", EITLERPGLOBAL.gNewUserID+"");
            rsDepositDeath.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsDepositDeath.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            rsDepositDeath.updateBoolean("CHANGED", true);
            rsDepositDeath.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsDepositDeath.updateBoolean("CANCELLED", false);
            rsDepositDeath.updateRow();
            
            //******************** History Update ************************//
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM FINANCE.D_FD_DEPOSIT_DEATH_HEADER_H WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
            RevNo++;
            
            rsDepositDeathH.moveToInsertRow() ;
            rsDepositDeathH.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsDepositDeathH.updateInt("REVISION_NO", RevNo);
            rsDepositDeathH.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsDepositDeathH.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsDepositDeathH.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsDepositDeathH.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            
            rsDepositDeathH.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            rsDepositDeathH.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsDepositDeathH.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsDepositDeathH.updateString("TRANSFER_PARTY_CODE", getAttribute("TRANSFER_PARTY_CODE").getString());
            rsDepositDeathH.updateString("TRANSFER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("TRANSFER_DATE").getString()));
            rsDepositDeathH.updateString("APPLICANT2", getAttribute("APPLICANT2").getString());
            rsDepositDeathH.updateString("APPLICANT3", getAttribute("APPLICANT3").getString());
            rsDepositDeathH.updateString("APPLICANT4", getAttribute("APPLICANT4").getString());
            
            rsDepositDeathH.updateString("PAN_NO", getAttribute("PAN_NO").getString());
            rsDepositDeathH.updateString("PAN_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PAN_DATE").getString()));
            
            rsDepositDeathH.updateString("ADDRESS1", getAttribute("ADDRESS1").getString());
            rsDepositDeathH.updateString("ADDRESS2", getAttribute("ADDRESS2").getString());
            rsDepositDeathH.updateString("ADDRESS3", getAttribute("ADDRESS3").getString());
            
            rsDepositDeathH.updateString("REMARKS", getAttribute("REMARKS").getString());
            //Approval Specific
            rsDepositDeathH.updateBoolean("APPROVED", false);
            rsDepositDeathH.updateString("APPROVED_DATE", "0000-00-00");
            rsDepositDeathH.updateBoolean("REJECTED", false);
            rsDepositDeathH.updateString("REJECTED_DATE", "0000-00-00");
            rsDepositDeathH.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsDepositDeathH.updateString("CREATED_BY", getAttribute("CREATED_BY").getString());
            rsDepositDeathH.updateString("CREATED_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_BY").getString()));
            rsDepositDeathH.updateString("MODIFIED_BY", EITLERPGLOBAL.gNewUserID+"");
            rsDepositDeathH.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsDepositDeathH.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            rsDepositDeathH.updateBoolean("CHANGED", true);
            rsDepositDeathH.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsDepositDeathH.updateBoolean("CANCELLED", false);
            rsDepositDeathH.insertRow();
            //*************************************************************//
            
            Statement StmtDetail;
            StmtDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDetail=StmtDetail.executeQuery("SELECT * FROM D_FD_DEPOSIT_DEATH_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DOC_NO='1'");
            String nDocno=(String) getAttribute("DOC_NO").getObj();
            
            String Del_Query = "DELETE FROM FINANCE.D_FD_DEPOSIT_DEATH_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DOC_NO='"+nDocno.trim()+"'";
            data.Execute(Del_Query,FinanceGlobal.FinURL);
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            
            for(int i=1;i<=colLineItems.size();i++) {
                clsDepositDeathItems ObjItem=(clsDepositDeathItems) colLineItems.get(Integer.toString(i));
                
                rsDetail.moveToInsertRow();
                rsDetail.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsDetail.updateString("DOC_NO",DocNo);
                rsDetail.updateLong("SR_NO",i);
                rsDetail.updateString("RECEIPT_NO",ObjItem.getAttribute("RECEIPT_NO").getString());
                rsDetail.updateString("RECEIPT_DATE",EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("RECEIPT_DATE").getString()));
                rsDetail.updateString("EFFECTIVE_DATE",EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("EFFECTIVE_DATE").getString()));
                rsDetail.updateString("MATURITY_DATE",EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("MATURITY_DATE").getString()));
                rsDetail.updateString("MAIN_ACCOUNT_CODE",ObjItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsDetail.updateDouble("AMOUNT",ObjItem.getAttribute("AMOUNT").getDouble());
                rsDetail.updateString("CREATED_BY", getAttribute("CREATED_BY").getString());
                rsDetail.updateString("CREATED_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_BY").getString()));
                rsDetail.updateString("MODIFIED_BY", EITLERPGLOBAL.gNewUserID+"");
                rsDetail.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsDetail.updateBoolean("CHANGED",true);
                rsDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsDetail.updateBoolean("CANCELLED",false);
                rsDetail.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsHDetail.updateString("DOC_NO",DocNo);
                rsHDetail.updateLong("SR_NO",i);
                rsHDetail.updateString("RECEIPT_NO",ObjItem.getAttribute("RECEIPT_NO").getString());
                rsHDetail.updateString("RECEIPT_DATE",EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("RECEIPT_DATE").getString()));
                rsHDetail.updateString("EFFECTIVE_DATE",EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("EFFECTIVE_DATE").getString()));
                rsHDetail.updateString("MATURITY_DATE",EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("MATURITY_DATE").getString()));
                rsHDetail.updateString("MAIN_ACCOUNT_CODE",ObjItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsHDetail.updateDouble("AMOUNT",ObjItem.getAttribute("AMOUNT").getDouble());
                rsHDetail.updateString("CREATED_BY", getAttribute("CREATED_BY").getString());
                rsHDetail.updateString("CREATED_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_BY").getString()));
                rsHDetail.updateString("MODIFIED_BY", EITLERPGLOBAL.gNewUserID+"");
                rsHDetail.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CANCELLED",false);
                rsHDetail.insertRow();
            }
            
            
            //************ Update Approval Flow *****************//
            ObjFlow.CompanyID =getAttribute("COMPANY_ID").getInt();
            ObjFlow.ModuleID = this.ModuleID ;
            ObjFlow.DocNo = getAttribute("DOC_NO").getString();
            ObjFlow.From =getAttribute("FROM").getInt();
            ObjFlow.To = getAttribute("TO").getInt();
            ObjFlow.Status = getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName = "D_FD_DEPOSIT_DEATH_HEADER";
            ObjFlow.IsCreator = false;
            ObjFlow.HierarchyID =getAttribute("HIERARCHY_ID").getInt();
            ObjFlow.Remarks =getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName ="DOC_NO";
            ObjFlow.UseSpecifiedURL=true;
            ObjFlow.SpecificURL=FinanceGlobal.FinURL;
            
            //==== Handling Rejected Documents ==========//
            
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_FD_DEPOSIT_DEATH_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='"+ getAttribute("DOC_NO").getString() +"' ",FinanceGlobal.FinURL);
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE MODULE_ID="+ModuleID+" AND DOC_NO='"+ getAttribute("DOC_NO").getString() +"' ");
                
                ObjFlow.IsCreator=true;
            }
            //==========================================//
            
            if (ObjFlow.Status.equals("H")) {
                ObjFlow.Status = "A";
                ObjFlow.To = ObjFlow.From;
                ObjFlow.UpdateFlow();
            } else {
                if (!ObjFlow.UpdateFlow()) {
                    LastError = ObjFlow.LastError;
                }
            }
            ObjFlow.UseSpecifiedURL=true;
            //**************End of Approval Flow *****************//
            if(AStatus.equals("F")) {
                ResultSet rsReceiptList = data.getResult("SELECT * FROM D_FD_DEPOSIT_DEATH_DETAIL WHERE DOC_NO='"+DocNo+"' ORDER BY SR_NO",FinanceGlobal.FinURL);
                rsReceiptList.first();
                while(!rsReceiptList.isAfterLast()) {
                    String ReceiptNo = rsReceiptList.getString("RECEIPT_NO");
                    String MainCode = rsReceiptList.getString("MAIN_ACCOUNT_CODE");
                    int DepositTypeID = data.getIntValueFromDB("SELECT DEPOSIT_TYPE_ID FROM D_FD_DEPOSIT_MASTER WHERE MAIN_ACCOUNT_CODE='"+MainCode+"' AND PARTY_CODE='"+PartyCode+"' AND RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                    if(DepositTypeID==2) {
                        if(!TransferInterestAndTDS(ReceiptNo,DocNo)) {
                            JOptionPane.showMessageDialog(null," Interest and TDS of "+ ReceiptNo +" not transfered properly. \n Contact to Administrator.");
                            return false;
                        }
                    }
                    if(!TransferReceipt(ReceiptNo,DocNo)) {
                        JOptionPane.showMessageDialog(null,"Receipt of "+ ReceiptNo +" not transfered properly. \n Contact to Administrator.");
                        return false;
                    }
                    String SQL = "UPDATE D_FD_INT_CALC_DETAIL SET PARTY_CODE='"+TransferPartyCode+"' WHERE MAIN_ACCOUNT_CODE='"+MainCode+"' AND PARTY_CODE='"+PartyCode+"' AND RECEIPT_NO='"+ReceiptNo+"' ";
                    data.Execute(SQL,FinanceGlobal.FinURL);
                    SQL = "UPDATE D_FD_DEPOSIT_MASTER SET PARTY_CODE='"+TransferPartyCode+"',APPLICANT2='"+Applicant2+"', APPLICANT3='"+Applicant3+"', " +
                    "APPLICANT4='"+Applicant4+"', ADDRESS1='"+Address1+"', ADDRESS2='"+Address2+"', ADDRESS3='"+Address3+"', " +
                    "PAN_NO='" + PanNo+"',PAN_DATE='"+PanDate+"' "+
                    "WHERE MAIN_ACCOUNT_CODE='"+MainCode+"' AND PARTY_CODE='"+PartyCode+"' AND RECEIPT_NO='"+ReceiptNo+"' ";
                    data.Execute(SQL,FinanceGlobal.FinURL);
                    rsReceiptList.next();
                }
            }
            MoveLast();
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LastError=e.getMessage();
            return false;
        }
    }
    private boolean TransferInterestAndTDS(String ReceiptNo,String DocNo) {
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
            String MainAccountCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FD_DEPOSIT_DEATH_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
            String PartyCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_DEPOSIT_DEATH_HEADER WHERE DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
            String TransferPartyCode = data.getStringValueFromDB("SELECT TRANSFER_PARTY_CODE FROM D_FD_DEPOSIT_DEATH_HEADER WHERE DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
            String ReceiptDate = data.getStringValueFromDB("SELECT RECEIPT_DATE FROM D_FD_DEPOSIT_DEATH_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
            String TransferDate = data.getStringValueFromDB("SELECT TRANSFER_DATE FROM D_FD_DEPOSIT_DEATH_HEADER WHERE DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
            String DocDate = data.getStringValueFromDB("SELECT DOC_DATE FROM D_FD_DEPOSIT_DEATH_HEADER WHERE DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
            String LastDate = EITLERPGLOBAL.getFinYearStartDate(TransferDate);
            double InterestAmount = data.getDoubleValueFromDB("SELECT SUM(B.INTEREST_AMOUNT) FROM D_FD_INT_CALC_HEADER A, D_FD_INT_CALC_DETAIL B WHERE A.DOC_NO=B.DOC_NO AND A.TDS_ONLY=0 AND B.MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND B.PARTY_CODE='"+PartyCode+"' AND B.WARRANT_DATE<'"+LastDate+"' AND B.RECEIPT_NO='"+ReceiptNo+"' AND A.APPROVED=1 AND A.CANCELLED=0",FinanceGlobal.FinURL);
            double TDSAmount = data.getDoubleValueFromDB("SELECT SUM(B.TDS_AMOUNT) FROM D_FD_INT_CALC_HEADER A, D_FD_INT_CALC_DETAIL B WHERE A.DOC_NO=B.DOC_NO AND B.MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND B.PARTY_CODE='"+PartyCode+"' AND B.WARRANT_DATE<'"+LastDate+"' AND B.RECEIPT_NO='"+ReceiptNo+"' AND A.APPROVED=1 AND A.CANCELLED=0 ",FinanceGlobal.FinURL);
            List.clear();
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsCalcInterest.ModuleID, "GET_BOOK_CODE", "DEPOSIT_INT_JOURNAL", "");
            if(List.size()>0) {
                //Get Book Code = 24 for Journal Voucher
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                BookCode = objRule.getAttribute("RULE_OUTCOME").getString();
            }
            //============= Gethering Data =======================//
            
            /*============== Preparing Voucher Header ================*/
            objVoucher=new clsVoucher();
            objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
            objVoucher.IsInternalPosting = true;
            objVoucher.VoucherType = FinanceGlobal.TYPE_JOURNAL;
            objVoucher.setAttribute("PREFIX",SelPrefix);
            objVoucher.setAttribute("SUFFIX",SelSuffix);
            objVoucher.setAttribute("FFNO",FFNo);
            objVoucher.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            objVoucher.setAttribute("VOUCHER_NO","");
            objVoucher.setAttribute("BOOK_CODE",BookCode);
            objVoucher.setAttribute("VOUCHER_TYPE",FinanceGlobal.TYPE_JOURNAL);
            objVoucher.setAttribute("CHEQUE_NO","");
            objVoucher.setAttribute("CHEQUE_DATE","0000-00-00");
            objVoucher.setAttribute("BANK_NAME","");
            objVoucher.setAttribute("VOUCHER_DATE",TransferDate);//
            objVoucher.setAttribute("ST_CATEGORY","");
            objVoucher.setAttribute("MODULE_ID",0);
            objVoucher.setAttribute("REMARKS","");
            objVoucher.setAttribute("CREATED_BY",EITLERPGLOBAL.gLoginID);
            objVoucher.setAttribute("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            objVoucher.setAttribute("MODIFIED_BY","");
            objVoucher.setAttribute("MODIFIED_DATE","0000-00-00");
            objVoucher.setAttribute("HIERARCHY_ID",getAttribute("FIN_HIERARCHY_ID").getInt());
            int FirstUserID=data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID="+getAttribute("FIN_HIERARCHY_ID").getInt()+" AND SR_NO=1");
            objVoucher.setAttribute("FROM",FirstUserID);
            objVoucher.setAttribute("TO",FirstUserID);
            objVoucher.setAttribute("FROM_REMARKS","");
            objVoucher.setAttribute("APPROVAL_STATUS","F"); //Final Approved --> Voucher
            /*============== End of Voucher Header ================*/
            
            /*============== Voucher Detail =================*/
            clsVoucherItem objVoucherItem=null;
            objVoucher.colVoucherItems.clear();
            if(InterestAmount > 0) {
                VoucherSrNo++;
                objVoucherItem=new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT","D");
                objVoucherItem.setAttribute("ACCOUNT_ID",1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","115160");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",PartyCode);
                objVoucherItem.setAttribute("AMOUNT",InterestAmount);
                objVoucherItem.setAttribute("REMARKS","");
                objVoucherItem.setAttribute("PO_NO","");
                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
                objVoucherItem.setAttribute("REMARKS","");
                objVoucherItem.setAttribute("INVOICE_NO","");
                objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
                objVoucherItem.setAttribute("GRN_NO",DocNo);
                objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(DocDate));
                objVoucherItem.setAttribute("MODULE_ID",clsDepositDeath.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
                
                VoucherSrNo++;
                objVoucherItem=new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT","C");
                objVoucherItem.setAttribute("ACCOUNT_ID",1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","115160");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",TransferPartyCode);
                objVoucherItem.setAttribute("AMOUNT",InterestAmount);
                objVoucherItem.setAttribute("PO_NO","");
                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
                objVoucherItem.setAttribute("REMARKS","");
                objVoucherItem.setAttribute("INVOICE_NO","");
                objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
                objVoucherItem.setAttribute("GRN_NO",DocNo);
                objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(DocDate));
                objVoucherItem.setAttribute("MODULE_ID",clsDepositDeath.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            }
            
            if(TDSAmount > 0) {
                VoucherSrNo++;
                objVoucherItem=new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT","D");
                objVoucherItem.setAttribute("ACCOUNT_ID",1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","115160");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",TransferPartyCode);
                objVoucherItem.setAttribute("AMOUNT",TDSAmount);
                objVoucherItem.setAttribute("PO_NO","");
                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
                objVoucherItem.setAttribute("REMARKS","");
                objVoucherItem.setAttribute("INVOICE_NO","");
                objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
                objVoucherItem.setAttribute("GRN_NO",DocNo);
                objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(DocDate));
                objVoucherItem.setAttribute("MODULE_ID",clsDepositDeath.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
                
                
                VoucherSrNo++;
                objVoucherItem=new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT","C");
                objVoucherItem.setAttribute("ACCOUNT_ID",1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","115160");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",PartyCode);
                objVoucherItem.setAttribute("AMOUNT",TDSAmount);
                objVoucherItem.setAttribute("PO_NO","");
                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
                objVoucherItem.setAttribute("REMARKS","");
                objVoucherItem.setAttribute("INVOICE_NO","");
                objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
                objVoucherItem.setAttribute("GRN_NO",DocNo);
                objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(DocDate));
                objVoucherItem.setAttribute("MODULE_ID",clsDepositDeath.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
                /*============== End of Voucher Detail =================*/
            }
            if(InterestAmount > 0 || TDSAmount > 0) {
                if(objVoucher.Insert()) {
                    objVoucher.IsInternalPosting = false;
                    return true;
                } else {
                    objVoucher.IsInternalPosting = false;
                    return false;
                }
            }
            
            objVoucher.IsInternalPosting = false;
            objVoucher = null;
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            LastError=e.getMessage();
            return false;
        }
    }
    private boolean TransferReceipt(String ReceiptNo,String DocNo) {
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
            String MainAccountCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FD_DEPOSIT_DEATH_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
            String PartyCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_DEPOSIT_DEATH_HEADER WHERE DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
            String TransferPartyCode = data.getStringValueFromDB("SELECT TRANSFER_PARTY_CODE FROM D_FD_DEPOSIT_DEATH_HEADER WHERE DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
            String ReceiptDate = data.getStringValueFromDB("SELECT RECEIPT_DATE FROM D_FD_DEPOSIT_DEATH_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
            String TransferDate = data.getStringValueFromDB("SELECT TRANSFER_DATE FROM D_FD_DEPOSIT_DEATH_HEADER WHERE DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
            double Amount = data.getDoubleValueFromDB("SELECT AMOUNT FROM D_FD_DEPOSIT_DEATH_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
            String DocDate = data.getStringValueFromDB("SELECT DOC_DATE FROM D_FD_DEPOSIT_DEATH_HEADER WHERE DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
            List.clear();
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsCalcInterest.ModuleID, "GET_BOOK_CODE", "DEPOSIT_INT_JOURNAL", "");
            if(List.size()>0) {
                //Get Book Code = 24 for Journal Voucher
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                BookCode = objRule.getAttribute("RULE_OUTCOME").getString();
            }
            //============= Gethering Data =======================//
            
            /*============== Preparing Voucher Header ================*/
            objVoucher=new clsVoucher();
            objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
            objVoucher.VoucherType = FinanceGlobal.TYPE_JOURNAL;
            objVoucher.IsInternalPosting = true;
            objVoucher.setAttribute("PREFIX",SelPrefix);
            objVoucher.setAttribute("SUFFIX",SelSuffix);
            objVoucher.setAttribute("FFNO",FFNo);
            objVoucher.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            objVoucher.setAttribute("VOUCHER_NO","");
            objVoucher.setAttribute("BOOK_CODE",BookCode);
            objVoucher.setAttribute("VOUCHER_TYPE",FinanceGlobal.TYPE_JOURNAL);
            objVoucher.setAttribute("CHEQUE_NO","");
            objVoucher.setAttribute("CHEQUE_DATE","0000-00-00");
            objVoucher.setAttribute("BANK_NAME","");
            objVoucher.setAttribute("VOUCHER_DATE",TransferDate);//
            objVoucher.setAttribute("ST_CATEGORY","");
            objVoucher.setAttribute("MODULE_ID",0);
            objVoucher.setAttribute("REMARKS","");
            objVoucher.setAttribute("CREATED_BY",EITLERPGLOBAL.gLoginID);
            objVoucher.setAttribute("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            objVoucher.setAttribute("MODIFIED_BY","");
            objVoucher.setAttribute("MODIFIED_DATE","0000-00-00");
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
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",PartyCode);
            objVoucherItem.setAttribute("AMOUNT",Amount);
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO",DocNo);
            objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(DocDate));
            objVoucherItem.setAttribute("MODULE_ID",clsDepositDeath.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            VoucherSrNo++;
            objVoucherItem=new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT","C");
            objVoucherItem.setAttribute("ACCOUNT_ID",1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode);
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",TransferPartyCode);
            objVoucherItem.setAttribute("AMOUNT",Amount);
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO",DocNo);
            objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(DocDate));
            objVoucherItem.setAttribute("MODULE_ID",clsDepositDeath.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            /*============== End of Voucher Detail =================*/
            
            if(objVoucher.Insert()) {
                objVoucher.IsInternalPosting = false;
                return true;
            } else {
                objVoucher.IsInternalPosting = false;
                return false;
            }
        } catch(Exception e) {
            e.printStackTrace();
            LastError=e.getMessage();
            return false;
        }
    }
    
    public clsDepositDeath getObject(int CompanyID,String DocNo) {
        
        clsDepositDeath ObjDepositDeath=new clsDepositDeath();
        
        try {
            Connection Conn=data.getConn(FinanceGlobal.FinURL);
            Statement stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            ResultSet rsTmp;
            ResultSet rsSchemeID;
            ResultSet rsDepositTypeID;
            ResultSet rsInterestRate;
            
            rsTmp=stTmp.executeQuery("SELECT * FROM D_FD_DEPOSIT_DEATH_HEADER WHERE DOC_NO=" +DocNo);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                //Applicant Detail
                ObjDepositDeath.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp, "COMPANY_ID", 0));
                ObjDepositDeath.setAttribute("DOC_NO",UtilFunctions.getString(rsTmp, "DOC_NO", ""));
                ObjDepositDeath.setAttribute("DOC_DATE",UtilFunctions.getString(rsTmp, "DOC_DATE","0000-00-00"));
                ObjDepositDeath.setAttribute("PARTY_CODE",UtilFunctions.getString(rsTmp, "PARTY_CODE", ""));
                ObjDepositDeath.setAttribute("TRANSFER_PARTY_CODE",UtilFunctions.getString(rsTmp, "TRANSFER_PARTY_CODE", ""));
                ObjDepositDeath.setAttribute("TRANSFER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp, "TRANSFER_DATE","0000-00-00")));
                ObjDepositDeath.setAttribute("APPLICANT2",UtilFunctions.getString(rsTmp, "APPLICANT2", ""));
                ObjDepositDeath.setAttribute("APPLICANT3",UtilFunctions.getString(rsTmp, "APPLICANT3", ""));
                ObjDepositDeath.setAttribute("APPLICANT4",UtilFunctions.getString(rsTmp, "APPLICANT4", ""));
                ObjDepositDeath.setAttribute("ADDRESS1",UtilFunctions.getString(rsTmp, "ADDRESS1", ""));
                ObjDepositDeath.setAttribute("ADDRESS2",UtilFunctions.getString(rsTmp, "ADDRESS2", ""));
                ObjDepositDeath.setAttribute("ADDRESS3",UtilFunctions.getString(rsTmp, "ADDRESS3", ""));
                
                //Approval Specific
                ObjDepositDeath.setAttribute("CREATED_BY",UtilFunctions.getString(rsTmp, "CREATED_BY", ""));
                ObjDepositDeath.setAttribute("CREATED_DATE",UtilFunctions.getString(rsTmp, "CREATED_DATE","0000-00-00"));
                ObjDepositDeath.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsTmp, "MODIFIED_BY", ""));
                ObjDepositDeath.setAttribute("MODIFIED_DATE",UtilFunctions.getString(rsTmp, "MODIFIED_DATE","0000-00-00"));
                
                ObjDepositDeath.setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsTmp, "HIERARCHY_ID", 0));
                ObjDepositDeath.setAttribute("APPROVED",UtilFunctions.getInt(rsTmp, "APPROVED",0));
                ObjDepositDeath.setAttribute("APPROVED_DATE",UtilFunctions.getString(rsTmp, "APPROVED_DATE","0000-00-00"));
                ObjDepositDeath.setAttribute("REJECTED",UtilFunctions.getInt(rsTmp, "REJECTED", 0));
                
                ObjDepositDeath.setAttribute("REJECTED_DATE",UtilFunctions.getString(rsTmp, "REJECTED_DATE","0000-00-00"));
                ObjDepositDeath.setAttribute("CHANGED",UtilFunctions.getInt(rsTmp, "CHANGED", 0));
                ObjDepositDeath.setAttribute("CHANGED_DATE",UtilFunctions.getString(rsTmp, "CHANGED_DATE","0000-00-00"));
                ObjDepositDeath.setAttribute("CANCELLED",UtilFunctions.getInt(rsTmp, "CANCELLED", 0));
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return ObjDepositDeath;
    }
    
    public boolean Filter(String pCondition,int pCompanyID) {
        Ready=false;
        pCompanyID=EITLERPGLOBAL.gCompanyID;
        try {
            String strSql = "SELECT * FROM D_FD_DEPOSIT_DEATH_HEADER " + pCondition ;
            // System.out.println(strSql);
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDepositDeath = Stmt.executeQuery(strSql);
            rsDepositDeath.first();
            
            if(!rsDepositDeath.first()) {
                strSql = "SELECT * FROM D_FD_DEPOSIT_DEATH_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" ORDER BY DOC_NO ";
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
            
            rsTmp=stTmp.executeQuery("SELECT * FROM D_FD_DEPOSIT_DEATH_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"'");
            while(rsTmp.next()) {
                clsDepositDeath ObjDepositDeath=new clsDepositDeath();
                
                ObjDepositDeath.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                ObjDepositDeath.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjDepositDeath.setAttribute("UPDATED_BY",rsTmp.getString("UPDATED_BY"));
                ObjDepositDeath.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjDepositDeath.setAttribute("ENTRY_DATE",EITLERPGLOBAL.formatDate(rsTmp.getString("ENTRY_DATE")));
                ObjDepositDeath.setAttribute("APPROVER_REMARKS",UtilFunctions.getString(rsTmp,"APPROVER_REMARKS", ""));
                
                List.put(Integer.toString(List.size()+1),ObjDepositDeath);
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
            rsTmp=data.getResult("SELECT DOC_NO,APPROVED,CANCELLED FROM D_FD_DEPOSIT_DEATH_HEADER WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"'",FinanceGlobal.FinURL);
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
    
    public boolean IsEditable(int pCompanyID,String pDocNo,int pUserID) {
        
        try {
            ResultSet rsTmp;
            Statement tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String strSQL1="SELECT COUNT(*) AS COUNT FROM D_FD_DEPOSIT_DEATH_HEADER WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND (APPROVED=1)";
            
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
            long Counter=0;
            int RevNo=0;
            
            if(HistoryView) {
                RevNo=rsDepositDeath.getInt("REVISION_NO");
                setAttribute("REVISION_NO",RevNo);
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            //Applicant Detail
            setAttribute("COMPANY_ID",UtilFunctions.getInt(rsDepositDeath, "COMPANY_ID",0));
            setAttribute("DOC_NO",UtilFunctions.getString(rsDepositDeath,"DOC_NO",""));
            setAttribute("DOC_DATE",UtilFunctions.getString(rsDepositDeath,"DOC_DATE","0000-00-00"));
            setAttribute("PARTY_CODE",UtilFunctions.getString(rsDepositDeath,"PARTY_CODE",""));
            setAttribute("TRANSFER_PARTY_CODE",UtilFunctions.getString(rsDepositDeath,"TRANSFER_PARTY_CODE",""));
            setAttribute("TRANSFER_DATE",UtilFunctions.getString(rsDepositDeath,"TRANSFER_DATE","0000-00-00"));
            setAttribute("APPLICANT2",UtilFunctions.getString(rsDepositDeath,"APPLICANT2",""));
            setAttribute("APPLICANT3",UtilFunctions.getString(rsDepositDeath,"APPLICANT3",""));
            setAttribute("APPLICANT4",UtilFunctions.getString(rsDepositDeath,"APPLICANT4",""));
            
            setAttribute("ADDRESS1",UtilFunctions.getString(rsDepositDeath,"ADDRESS1",""));
            setAttribute("ADDRESS2",UtilFunctions.getString(rsDepositDeath,"ADDRESS2",""));
            setAttribute("ADDRESS3",UtilFunctions.getString(rsDepositDeath,"ADDRESS3",""));
            
            setAttribute("PAN_NO",UtilFunctions.getString(rsDepositDeath,"PAN_NO",""));
            setAttribute("PAN_DATE",UtilFunctions.getString(rsDepositDeath,"PAN_DATE","0000-00-00"));
            
            setAttribute("REMARKS",UtilFunctions.getString(rsDepositDeath,"REMARKS",""));
            setAttribute("CREATED_BY",UtilFunctions.getString(rsDepositDeath,"CREATED_BY",""));
            setAttribute("CREATED_DATE",UtilFunctions.getString(rsDepositDeath,"CREATED_DATE","0000-00-00"));
            setAttribute("MODIFIED_BY",UtilFunctions.getString(rsDepositDeath,"MODIFIED_BY",""));
            setAttribute("MODIFIED_DATE",UtilFunctions.getString(rsDepositDeath,"MODIFIED_DATE","0000-00-00"));
            setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsDepositDeath,"HIERARCHY_ID",0));
            setAttribute("APPROVED",UtilFunctions.getInt(rsDepositDeath,"APPROVED",0));
            setAttribute("APPROVED_DATE",UtilFunctions.getString(rsDepositDeath,"APPROVED_DATE","0000-00-00"));
            setAttribute("REJECTED",UtilFunctions.getInt(rsDepositDeath,"REJECTED",0));
            setAttribute("REJECTED_DATE",UtilFunctions.getString(rsDepositDeath,"REJECTED_DATE","0000-00-00"));
            setAttribute("REJECTED_REMARKS",UtilFunctions.getString(rsDepositDeath,"REJECTED_REMARKS",""));
            setAttribute("CHANGED",UtilFunctions.getInt(rsDepositDeath,"CHANGED",0));
            setAttribute("CHANGED_DATE",UtilFunctions.getString(rsDepositDeath,"CHANGED_DATE","0000-00-00"));
            setAttribute("CANCELLED",UtilFunctions.getInt(rsDepositDeath,"CANCELLED",0));
            
            colLineItems.clear();
            
            long mCompanyID= getAttribute("COMPANY_ID").getLong();
            String mDocno= getAttribute("DOC_NO").getString();
            Statement tmpStmt=tmpConn.createStatement();
            String SQL= "";
            ResultSet rsTmp;
            if(HistoryView) {
                SQL = "SELECT * FROM D_FD_DEPOSIT_DEATH_DETAIL_H WHERE COMPANY_ID="+mCompanyID+" AND DOC_NO='"+mDocno.trim()+"' AND REVISION_NO="+RevNo+" ORDER BY REVISION_NO,SR_NO ";
                rsTmp=tmpStmt.executeQuery(SQL);
            } else {
                SQL = "SELECT * FROM D_FD_DEPOSIT_DEATH_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND DOC_NO='"+mDocno.trim()+"' ORDER BY SR_NO";
                rsTmp=tmpStmt.executeQuery(SQL);
            }
            rsTmp.first();
            
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsDepositDeathItems ObjItem=new clsDepositDeathItems();
                
                ObjItem.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));
                ObjItem.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                ObjItem.setAttribute("SR_NO",rsTmp.getLong("SR_NO"));
                ObjItem.setAttribute("RECEIPT_NO",rsTmp.getString("RECEIPT_NO"));
                ObjItem.setAttribute("RECEIPT_DATE",rsTmp.getString("RECEIPT_DATE"));
                ObjItem.setAttribute("EFFECTIVE_DATE",rsTmp.getString("EFFECTIVE_DATE"));
                ObjItem.setAttribute("MATURITY_DATE",rsTmp.getString("MATURITY_DATE"));
                ObjItem.setAttribute("MAIN_ACCOUNT_CODE",rsTmp.getString("MAIN_ACCOUNT_CODE"));
                ObjItem.setAttribute("AMOUNT",rsTmp.getDouble("AMOUNT"));
                ObjItem.setAttribute("CREATED_BY",rsTmp.getString("CREATED_BY"));
                ObjItem.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjItem.setAttribute("MODIFIED_BY",rsTmp.getString("MODIFIED_BY"));
                ObjItem.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                ObjItem.setAttribute("CHANGED",rsTmp.getInt("CHANGED"));
                ObjItem.setAttribute("CHANGED_DATE",rsTmp.getString("CHANGED_DATE"));
                ObjItem.setAttribute("CANCELLED",rsTmp.getInt("CANCELLED"));
                
                colLineItems.put(Long.toString(Counter),ObjItem);
                rsTmp.next();
            }// Inner while
            
            return true;
        } catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
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
            String MainAccountCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FD_DEPOSIT_DEATH WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL);
            String PartyCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_DEPOSIT_DEATH WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL);
            String BankMainCode = data.getStringValueFromDB("SELECT BANK_MAIN_CODE FROM D_FD_DEPOSIT_DEATH WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL);
            BookCode = data.getStringValueFromDB("SELECT BOOK_CODE FROM D_FIN_BOOK_MASTER WHERE MAIN_ACCOUNT_CODE='"+BankMainCode+"' ",FinanceGlobal.FinURL);
            double Amount = data.getDoubleValueFromDB("SELECT AMOUNT FROM D_FD_DEPOSIT_DEATH WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL);
            String ChequeNo = data.getStringValueFromDB("SELECT CHEQUE_NO FROM D_FD_DEPOSIT_DEATH WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL);
            String ChequeDate = data.getStringValueFromDB("SELECT CHEQUE_DATE FROM D_FD_DEPOSIT_DEATH WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL);
            String BankName = data.getStringValueFromDB("SELECT BOOK_NAME FROM D_FIN_BOOK_MASTER WHERE BOOK_CODE='"+BookCode+"' ",FinanceGlobal.FinURL);
            String RealizationDate = data.getStringValueFromDB("SELECT REALIZATION_DATE FROM D_FD_DEPOSIT_DEATH WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL);
            String ReceiptDate = data.getStringValueFromDB("SELECT RECEIPT_DATE FROM D_FD_DEPOSIT_DEATH WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL);
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
            LastError=e.getMessage();
            return false;
        }
    }
    
    private boolean Validate() {
        try {
            
            return true;
        }
        catch (Exception e) {
            return false;
        }
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
                strSQL="SELECT FINANCE.D_FD_DEPOSIT_DEATH_HEADER.DOC_NO,FINANCE.D_FD_DEPOSIT_DEATH_HEADER.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_DEPOSIT_DEATH_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_DEPOSIT_DEATH_HEADER.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FD_DEPOSIT_DEATH_HEADER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FD_DEPOSIT_DEATH_HEADER.COMPANY_ID="+CompanyID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+clsDepositDeath.ModuleID+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT FINANCE.D_FD_DEPOSIT_DEATH_HEADER.DOC_NO,FINANCE.D_FD_DEPOSIT_DEATH_HEADER.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_DEPOSIT_DEATH_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_DEPOSIT_DEATH_HEADER.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FD_DEPOSIT_DEATH_HEADER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FD_DEPOSIT_DEATH_HEADER.COMPANY_ID="+CompanyID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+clsDepositDeath.ModuleID+" ORDER BY D_FD_DEPOSIT_DEATH_HEADER.DOC_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT FINANCE.D_FD_DEPOSIT_DEATH_HEADER.DOC_NO,FINANCE.D_FD_DEPOSIT_DEATH_HEADER.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_DEPOSIT_DEATH_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_DEPOSIT_DEATH_HEADER.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FD_DEPOSIT_DEATH_HEADER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FD_DEPOSIT_DEATH_HEADER.COMPANY_ID="+CompanyID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+clsDepositDeath.ModuleID+" ORDER BY D_FD_DEPOSIT_DEATH_HEADER.DOC_NO";
            }
            
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                if(EITLERPGLOBAL.isWithinDate(rsTmp.getString("DOC_DATE"),FinYearFrom)) {
                    Counter=Counter+1;
                    clsDepositDeath ObjDoc=new clsDepositDeath();
                    
                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                    ObjDoc.setAttribute("DOC_DATE",rsTmp.getString("DOC_DATE"));
                    ObjDoc.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                    String PartyCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_DEPOSIT_DEATH_HEADER WHERE DOC_NO='"+rsTmp.getString("DOC_NO")+"' ",FinanceGlobal.FinURL);
                    String PartyName = data.getStringValueFromDB("SELECT PARTY_NAME FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+PartyCode+"' AND MAIN_ACCOUNT_CODE IN ('115012','115153','115225','115218','115029','115036') ",FinanceGlobal.FinURL);
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
    
    public boolean ShowHistory(int pCompanyID,String pDocNo) {
        Ready=false;
        try {
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsDepositDeath=Stmt.executeQuery("SELECT * FROM FINANCE.D_FD_DEPOSIT_DEATH_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"'");
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
    
}//end of the class
