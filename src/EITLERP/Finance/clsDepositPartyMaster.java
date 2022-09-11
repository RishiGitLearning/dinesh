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

public class clsDepositPartyMaster{
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
    private boolean HistoryView=false;
    
    public static int ModuleID=203;
    
    public Variant getAttribute(String PropName) {
        if(!props.containsKey(PropName)) {
            return new Variant("");
        }
        else {
            return (Variant) props.get(PropName);
        }
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
    public clsDepositPartyMaster() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("DEPOSIT_PARTY_ID",new Variant(0));
        props.put("PARTY_CODE",new Variant(""));
        props.put("MAIN_ACCOUNT_CODES",new Variant(""));
        props.put("PARTY_NAME",new Variant(""));
        
        
        props.put("ADDRESS",new Variant(""));
        props.put("CITY",new Variant(""));
        props.put("PINCODE",new Variant(""));
        props.put("STATE",new Variant(""));
        props.put("COUNTRY",new Variant(""));
        props.put("PHONE",new Variant(""));
        props.put("FAX",new Variant(""));
        props.put("MOBILE",new Variant(""));
        props.put("EMAIL",new Variant(""));
        props.put("URL",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("BLOCKED",new Variant(false));
        
        
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("REJECTED_REMARKS", new Variant(""));
        props.put("CANCELLED", new Variant(false));
        props.put("STATUS", new Variant(""));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
    }
    
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FIN_DEPOSIT_PARTY_MASTER ORDER BY DEPOSIT_PARTY_ID");
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
        
        Statement stHistory,stHeader;
        ResultSet rsHistory,rsHeader,rsTmp;
        
        try {
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FIN_DEPOSIT_PARTY_MASTER_H WHERE DEPOSIT_PARTY_ID='1' ");
            rsHistory.first();
            //------------------------------------//
            
            //** Validations **//
            if(!Validate()) {
                return false;
            }
            
            if(data.IsRecordExist("SELECT PARTY_CODE FROM D_FIN_DEPOSIT_PARTY_MASTER WHERE PARTY_CODE='"+getAttribute("PARTY_CODE").getString()+"' ",FinanceGlobal.FinURL)) {
                LastError="Party with this party code already exist.";
                return false;
            }
            //*****************//
            
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //Start Transaction
            //Conn.setAutoCommit(false);
            //data.SetAutoCommit(false);
            
            long NewAccountID=data.getMaxID("D_FIN_DEPOSIT_PARTY_MASTER","DEPOSIT_PARTY_ID",FinanceGlobal.FinURL);
            
            //--------- Generate New Internal Account ID  ------------
            setAttribute("DEPOSIT_PARTY_ID",NewAccountID);
            //--------------------------------------------------------
            
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            
            rsResultSet.updateLong("DEPOSIT_PARTY_ID",getAttribute("DEPOSIT_PARTY_ID").getLong());
            rsResultSet.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsResultSet.updateString("MAIN_ACCOUNT_CODES",getAttribute("MAIN_ACCOUNT_CODES").getString());
            rsResultSet.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            
            rsResultSet.updateString("ADDRESS",getAttribute("ADDRESS").getString());
            rsResultSet.updateString("CITY",getAttribute("CITY").getString());
            rsResultSet.updateString("PINCODE",getAttribute("PINCODE").getString());
            rsResultSet.updateString("STATE",getAttribute("STATE").getString());
            rsResultSet.updateString("COUNTRY",getAttribute("COUNTRY").getString());
            rsResultSet.updateString("PHONE",getAttribute("PHONE").getString());
            rsResultSet.updateString("FAX",getAttribute("FAX").getString());
            rsResultSet.updateString("MOBILE",getAttribute("MOBILE").getString());
            rsResultSet.updateString("EMAIL",getAttribute("EMAIL").getString());
            
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
            
            
            //========= Inserting Into History =================//
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1);
            rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            rsHistory.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            
            rsHistory.updateLong("DEPOSIT_PARTY_ID",getAttribute("DEPOSIT_PARTY_ID").getLong());
            rsHistory.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsHistory.updateString("MAIN_ACCOUNT_CODES",getAttribute("MAIN_ACCOUNT_CODES").getString());
            rsHistory.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            
            rsHistory.updateString("ADDRESS",getAttribute("ADDRESS").getString());
            rsHistory.updateString("CITY",getAttribute("CITY").getString());
            rsHistory.updateString("PINCODE",getAttribute("PINCODE").getString());
            rsHistory.updateString("STATE",getAttribute("STATE").getString());
            rsHistory.updateString("COUNTRY",getAttribute("COUNTRY").getString());
            rsHistory.updateString("PHONE",getAttribute("PHONE").getString());
            rsHistory.updateString("FAX",getAttribute("FAX").getString());
            rsHistory.updateString("MOBILE",getAttribute("MOBILE").getString());
            rsHistory.updateString("EMAIL",getAttribute("EMAIL").getString());
            
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
            
            //======== Update the Approval Flow =========//
            ObjFlow.CompanyID=EITLERPGLOBAL.gCompanyID;
            ObjFlow.ModuleID=ModuleID;
            ObjFlow.DocNo=Long.toString(getAttribute("DEPOSIT_PARTY_ID").getLong());
            ObjFlow.From=getAttribute("FROM").getInt();
            ObjFlow.To=getAttribute("TO").getInt();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_FIN_DEPOSIT_PARTY_MASTER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=getAttribute("HIERARCHY_ID").getInt();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName="DEPOSIT_PARTY_ID";
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
            
            //Conn.commit();
            //data.SetCommit();
            
            //data.SetAutoCommit(true);
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            try {
                //Conn.rollback();
            }
            catch(Exception c){}
            //data.SetRollback();
            //data.SetAutoCommit(true);
            LastError= e.getMessage();
            return false;
        }
    }
    
    
    
    
    public boolean Update() {
        
        Statement stHistory,stHeader;
        ResultSet rsHistory,rsHeader;
        boolean Validate=true;
        
        try {
            
            String theDocNo=Long.toString(getAttribute("DEPOSIT_PARTY_ID").getLong());
            
            //** Validations **//
            if(!Validate()) {
                return false;
            }
            
            if(data.IsRecordExist("SELECT PARTY_CODE FROM D_FIN_DEPOSIT_PARTY_MASTER WHERE PARTY_CODE='"+getAttribute("PARTY_CODE").getString()+"' AND MAIN_ACCOUNT_CODE='"+getAttribute("MAIN_ACCOUNT_CODE").getString()+"' AND DEPOSIT_PARTY_ID<>"+theDocNo,FinanceGlobal.FinURL)) {
                LastError="Party with this party code and ledger code already exist.";
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
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FIN_DEPOSIT_PARTY_MASTER_H WHERE DEPOSIT_PARTY_ID=1"); // '1' for restricting all data retrieval
            rsHistory.first();
            //** --------------------------------**//
            
            //Start Transaction
            //Conn.setAutoCommit(false);
            //data.SetAutoCommit(false);
            
            
            rsResultSet.updateLong("DEPOSIT_PARTY_ID",getAttribute("DEPOSIT_PARTY_ID").getLong());
            rsResultSet.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsResultSet.updateString("MAIN_ACCOUNT_CODES",getAttribute("MAIN_ACCOUNT_CODES").getString());
            rsResultSet.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            
            rsResultSet.updateString("ADDRESS",getAttribute("ADDRESS").getString());
            rsResultSet.updateString("CITY",getAttribute("CITY").getString());
            rsResultSet.updateString("PINCODE",getAttribute("PINCODE").getString());
            rsResultSet.updateString("STATE",getAttribute("STATE").getString());
            rsResultSet.updateString("COUNTRY",getAttribute("COUNTRY").getString());
            rsResultSet.updateString("PHONE",getAttribute("PHONE").getString());
            rsResultSet.updateString("FAX",getAttribute("FAX").getString());
            rsResultSet.updateString("MOBILE",getAttribute("MOBILE").getString());
            rsResultSet.updateString("EMAIL",getAttribute("EMAIL").getString());
            
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            
            
            rsResultSet.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
            rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateString("REJECTED_REMARKS","");
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateRow();
            
            
            
            //========= Inserting Into History =================//
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM D_FIN_DEPOSIT_PARTY_MASTER_H WHERE DEPOSIT_PARTY_ID="+theDocNo,FinanceGlobal.FinURL);
            RevNo++;
            String RevDocNo=Long.toString(getAttribute("DEPOSIT_PARTY_ID").getLong());
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo);
            rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            rsHistory.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            
            rsHistory.updateLong("DEPOSIT_PARTY_ID",getAttribute("DEPOSIT_PARTY_ID").getLong());
            rsHistory.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsHistory.updateString("MAIN_ACCOUNT_CODES",getAttribute("MAIN_ACCOUNT_CODES").getString());
            rsHistory.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            
            rsHistory.updateString("ADDRESS",getAttribute("ADDRESS").getString());
            rsHistory.updateString("CITY",getAttribute("CITY").getString());
            rsHistory.updateString("PINCODE",getAttribute("PINCODE").getString());
            rsHistory.updateString("STATE",getAttribute("STATE").getString());
            rsHistory.updateString("COUNTRY",getAttribute("COUNTRY").getString());
            rsHistory.updateString("PHONE",getAttribute("PHONE").getString());
            rsHistory.updateString("FAX",getAttribute("FAX").getString());
            rsHistory.updateString("MOBILE",getAttribute("MOBILE").getString());
            rsHistory.updateString("EMAIL",getAttribute("EMAIL").getString());
            
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
            
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=EITLERPGLOBAL.gCompanyID;
            ObjFlow.ModuleID=ModuleID;
            ObjFlow.DocNo=Long.toString(getAttribute("DEPOSIT_PARTY_ID").getLong());
            ObjFlow.From=getAttribute("FROM").getInt();
            ObjFlow.To=getAttribute("TO").getInt();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_FIN_DEPOSIT_PARTY_MASTER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=getAttribute("HIERARCHY_ID").getInt();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName="DEPOSIT_PARTY_ID";
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
                data.Execute("UPDATE D_FIN_DEPOSIT_PARTY_MASTER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DEPOSIT_PARTY_ID="+theDocNo,FinanceGlobal.FinURL);
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+theDocNo+"'");
                
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
            //Conn.commit();
            //data.SetCommit();
            
            //data.SetAutoCommit(true);
            
            if(ObjFlow.Status.equals("F")) {
                insertToFinance();
            }
            
            return true;
        }
        catch(Exception e) {
            try {
                //Conn.rollback();
                //data.SetRollback();
            }
            catch(Exception c){}
            //data.SetAutoCommit(true);
            
            LastError = e.getMessage();
            return false;
        }
    }
    
    public boolean insertToFinance() {
        try {
            String []MainCodes=getAttribute("MAIN_ACCOUNT_CODES").getString().split(",");
            for(int i=0;i<MainCodes.length;i++) {
                String MainCode = MainCodes[i];
                clsPartyMaster party = new clsPartyMaster();
                party.LoadDataEx(EITLERPGLOBAL.gCompanyID);
                party.setAttribute("PARTY_ID", 0);
                party.setAttribute("PARTY_CODE", getAttribute("PARTY_CODE").getString());
                party.setAttribute("MAIN_ACCOUNT_CODE",MainCode);
                party.setAttribute("PARTY_NAME",getAttribute("PARTY_NAME").getString());
                party.setAttribute("SH_NAME","");
                party.setAttribute("SH_CODE","0");
                
                party.setAttribute("CREDIT_DAYS",0);
                party.setAttribute("CREDIT_LIMIT",0);
                party.setAttribute("TIN_NO","");
                party.setAttribute("TIN_DATE","0000-00-00");
                party.setAttribute("CST_NO","");
                party.setAttribute("CST_DATE","0000-00-00");
                party.setAttribute("ECC_NO","");
                party.setAttribute("ECC_DATE","0000-00-00");
                
                party.setAttribute("PAN_NO","");
                party.setAttribute("PAN_DATE","0000-00-00");
                
                party.setAttribute("SERVICE_TAX_NO","");
                party.setAttribute("SERVICE_TAX_DATE","0000-00-00");
                party.setAttribute("SSI_CATEGORY","");
                party.setAttribute("SSI_NO","");
                party.setAttribute("SSI_DATE","0000-00-00");
                party.setAttribute("ESI_NO","");
                party.setAttribute("ESI_DATE","0000-00-00");
                party.setAttribute("ADDRESS",getAttribute("ADDRESS").getString());
                party.setAttribute("CITY",getAttribute("CITY").getString());
                party.setAttribute("PINCODE",getAttribute("PINCODE").getString());
                party.setAttribute("STATE",getAttribute("STATE").getString());
                party.setAttribute("COUNTRY",getAttribute("COUNTRY").getString());
                party.setAttribute("PHONE",getAttribute("PHONE").getString());
                party.setAttribute("FAX",getAttribute("FAX").getString());
                party.setAttribute("MOBILE",getAttribute("MOBILE").getString());
                party.setAttribute("EMAIL",getAttribute("EMAIL").getString());
                party.setAttribute("URL","");
                party.setAttribute("REMARKS",getAttribute("REMARKS").getString());
                party.setAttribute("BLOCKED",false);
                
                
                party.setAttribute("CREATED_BY",EITLERPGLOBAL.gLoginID);
                party.setAttribute("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                party.setAttribute("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                party.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                party.setAttribute("CHANGED",true);
                party.setAttribute("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                party.setAttribute("APPROVED",false);
                party.setAttribute("APPROVED_DATE","0000-00-00");
                party.setAttribute("REJECTED",false);
                party.setAttribute("REJECTED_DATE","0000-00-00");
                party.setAttribute("REJECTED_REMARKS","");
                party.setAttribute("CANCELLED",false);
                
                party.setAttribute("HIERARCHY_ID",1061);
                int FirstUserID=data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID="+1061+" AND SR_NO=1");
                party.setAttribute("FROM",FirstUserID);
                party.setAttribute("TO",FirstUserID);
                party.setAttribute("FROM_REMARKS","");
                party.setAttribute("APPROVAL_STATUS","H");
                
                if(!party.Insert()) {
                    JOptionPane.showMessageDialog(null,"Party Code : " + getAttribute("PARTY_CODE").toString() + " WITH MAIN CODE  " + MainCode + " not entered.");
                    return false;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    
    public boolean CanDelete(int CompanyID,int PartyID,int UserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            
            if(HistoryView) {
                return false;
            }
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FIN_DEPOSIT_PARTY_MASTER WHERE DEPOSIT_PARTY_ID="+PartyID+" AND (APPROVED=1)";
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
    public boolean IsEditable(int CompanyID,int PartyID,int UserID,String PartyCode) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT DEPOSIT_PARTY_ID FROM D_FIN_DEPOSIT_PARTY_MASTER WHERE DEPOSIT_PARTY_ID="+PartyID+" AND PARTY_CODE='"+PartyCode+"' AND (APPROVED=1)";
            
            if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                return false;
            }
            else {
                strSQL="SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+PartyID+"' AND USER_ID="+UserID+" AND STATUS='W'";
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
            int PartyID=getAttribute("DEPOSIT_PARTY_ID").getInt();
            
            if(CanDelete(EITLERPGLOBAL.gCompanyID,PartyID,UserID)) {
                String strSQL = "DELETE FROM D_FIN_DEPOSIT_PARTY_MASTER WHERE DEPOSIT_PARTY_ID="+PartyID;
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
    
    
    public Object getObject(int CompanyID, int PartyID) {
        String strCondition = " WHERE DEPOSIT_PARTY_ID=" + PartyID;
        clsDepositPartyMaster objParty = new clsDepositPartyMaster();
        objParty.Filter(strCondition,CompanyID);
        return objParty;
    }
    
    
    
    
    public boolean Filter(String Condition,int CompanyID) {
        Ready=false;
        
        try {
            String strSQL = "SELECT * FROM D_FIN_DEPOSIT_PARTY_MASTER " + Condition ;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSQL);
            
            if(!rsResultSet.first()) {
                strSQL = "SELECT * FROM D_FIN_DEPOSIT_PARTY_MASTER ORDER BY DEPOSIT_PARTY_ID ";
                rsResultSet=Stmt.executeQuery(strSQL);
                Ready=true;
                MoveLast();
                return false;
            } else {
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
            setAttribute("DEPOSIT_PARTY_ID",UtilFunctions.getInt(rsResultSet,"DEPOSIT_PARTY_ID",0));
            
            setAttribute("PARTY_CODE",UtilFunctions.getString(rsResultSet,"PARTY_CODE",""));
            setAttribute("MAIN_ACCOUNT_CODES",UtilFunctions.getString(rsResultSet,"MAIN_ACCOUNT_CODES",""));
            setAttribute("PARTY_NAME",UtilFunctions.getString(rsResultSet,"PARTY_NAME",""));
            
            setAttribute("ADDRESS",UtilFunctions.getString(rsResultSet,"ADDRESS",""));
            setAttribute("CITY",UtilFunctions.getString(rsResultSet,"CITY",""));
            setAttribute("PINCODE",UtilFunctions.getString(rsResultSet,"PINCODE",""));
            setAttribute("STATE",UtilFunctions.getString(rsResultSet,"STATE",""));
            setAttribute("COUNTRY",UtilFunctions.getString(rsResultSet,"COUNTRY",""));
            setAttribute("PHONE",UtilFunctions.getString(rsResultSet,"PHONE",""));
            setAttribute("FAX",UtilFunctions.getString(rsResultSet,"FAX",""));
            setAttribute("MOBILE",UtilFunctions.getString(rsResultSet,"MOBILE",""));
            setAttribute("EMAIL",UtilFunctions.getString(rsResultSet,"EMAIL",""));
            
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
                strSQL="SELECT FINANCE.D_FIN_DEPOSIT_PARTY_MASTER.DEPOSIT_PARTY_ID,FINANCE.D_FIN_DEPOSIT_PARTY_MASTER.PARTY_CODE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FIN_DEPOSIT_PARTY_MASTER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_DEPOSIT_PARTY_MASTER.COMPANY_ID=DINESHMILLS.D_COM_DOC_DATA.COMPANY_ID AND DINESHMILLS.D_COM_DOC_DATA.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND FINANCE.D_FIN_PARTY_DEPOSIT_MASTER.APPROVED=0 AND FINANCE.D_FIN_DEPOSIT_PARTY_MASTER.DEPOSIT_PARTY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT FINANCE.D_FIN_DEPOSIT_PARTY_MASTER.DEPOSIT_PARTY_ID,FINANCE.D_FIN_DEPOSIT_PARTY_MASTER.PARTY_CODE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FIN_DEPOSIT_PARTY_MASTER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_DEPOSIT_PARTY_MASTER.COMPANY_ID=DINESHMILLS.D_COM_DOC_DATA.COMPANY_ID AND DINESHMILLS.D_COM_DOC_DATA.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND FINANCE.D_FIN_DEPOSIT_PARTY_MASTER.APPROVED=0 AND FINANCE.D_FIN_DEPOSIT_PARTY_MASTER.DEPOSIT_PARTY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_FIN_DEPOSIT_PARTY_MASTER.DEPOSIT_PARTY_ID";
            }
            
            if(Order==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT FINANCE.D_FIN_DEPOSIT_PARTY_MASTER.DEPOSIT_PARTY_ID,FINANCE.D_FIN_DEPOSIT_PARTY_MASTER.PARTY_CODE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FIN_DEPOSIT_PARTY_MASTER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_DEPOSIT_PARTY_MASTER.COMPANY_ID=DINESHMILLS.D_COM_DOC_DATA.COMPANY_ID AND DINESHMILLS.D_COM_DOC_DATA.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND FINANCE.D_FIN_DEPOSIT_PARTY_MASTER.APPROVED=0 AND FINANCE.D_FIN_DEPOSIT_PARTY_MASTER.DEPOSIT_PARTY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_FIN_DEPOSIT_PARTY_MASTER.DEPOSIT_PARTY_ID";
            }
            
            rsTemp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTemp.first();
            
            Counter=0;
            
            if(rsTemp.getRow()>0) {
                while(!rsTemp.isAfterLast()) {
                    Counter=Counter+1;
                    clsDepositPartyMaster ObjDoc=new clsDepositPartyMaster();
                    
                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("DEPOSIT_PARTY_ID",UtilFunctions.getInt(rsTemp,"DEPOSIT_PARTY_ID",0));
                    ObjDoc.setAttribute("PARTY_CODE",UtilFunctions.getString(rsTemp,"PARTY_CODE",""));
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
    
    
    public boolean ShowHistory(int CompanyID,int PartyID) {
        Ready=false;
        try {
            String strSQL="SELECT * FROM D_FIN_DEPOSIT_PARTY_MASTER_H WHERE DEPOSIT_PARTY_ID="+PartyID;
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
    
    
    public static HashMap getHistoryList(int CompanyID,int PartyID) {
        HashMap List=new HashMap();
        ResultSet rsTmp;
        
        try {
            String strSQL="SELECT * FROM D_FIN_DEPOSIT_PARTY_MASTER_H WHERE  DEPOSIT_PARTY_ID="+PartyID;
            rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsDepositPartyMaster objAccount=new clsDepositPartyMaster();
                    
                    objAccount.setAttribute("DEPOSIT_PARTY_ID",UtilFunctions.getInt(rsTmp,"DEPOSIT_PARTY_ID", 0));
                    objAccount.setAttribute("REVISION_NO",UtilFunctions.getInt(rsTmp,"REVISION_NO", 0));
                    objAccount.setAttribute("UPDATED_BY",UtilFunctions.getString(rsTmp,"UPDATED_BY", ""));
                    objAccount.setAttribute("APPROVAL_STATUS",UtilFunctions.getString(rsTmp,"APPROVAL_STATUS", ""));
                    objAccount.setAttribute("ENTRY_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"ENTRY_DATE","0000-00-00")));
                    objAccount.setAttribute("APPROVER_REMARKS",UtilFunctions.getString(rsTmp,"APPROVER_REMARKS", ""));
                    
                    List.put(Integer.toString(List.size()+1),objAccount);
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
    
    
    public static String getDocStatus(int CompanyID,int PartyID) {
        ResultSet rsTmp;
        String strMessage="";
        
        try {
            
            String strSQL="SELECT DEPOSIT_PARTY_ID,APPROVED,CANCELLED FROM D_FIN_DEPOSIT_PARTY_MASTER WHERE DEPOSIT_PARTY_ID="+PartyID;
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
    
    
    public static boolean CanCancel(int CompanyID,int PartyID) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            rsTmp=data.getResult("SELECT DEPOSIT_PARTY_ID,PARTY_CODE FROM D_FIN_DEPOSIT_PARTY_MASTER WHERE DEPOSIT_PARTY_ID="+PartyID+" AND CANCELLED=0",FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                
                String PartyCode=UtilFunctions.getString(rsTmp,"PARTY_CODE","");
                
                if(data.IsRecordExist("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE CANCELLED=0 AND COMPANY_ID="+CompanyID+" AND SUB_CODE='"+PartyCode+"'",FinanceGlobal.FinURL)) {
                    canCancel=false;
                }
                else {
                    canCancel=true;
                }
            }
            
            rsTmp.close();
        }
        catch(Exception e) {
            
        }
        
        return canCancel;
    }
    
    
    public static boolean CancelDoc(int CompanyID,int PartyID) {
        
        ResultSet rsTmp=null;
        boolean Cancelled=false;
        
        try {
            if(CanCancel(CompanyID,PartyID)) {
                
                boolean ApprovedDoc=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_FIN_DEPOSIT_PARTY_MASTER WHERE DEPOSIT_PARTY_ID="+PartyID);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedDoc) {
                    
                }
                else {
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE DOC_NO='"+PartyID+"' AND MODULE_ID="+ModuleID,FinanceGlobal.FinURL);
                }
                
                data.Execute("UPDATE D_FIN_DEPOSIT_PARTY_MASTER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DEPOSIT_PARTY_ID="+PartyID);
                
                Cancelled=true;
            }
            
            rsTmp.close();
        }
        catch(Exception e) {
            
        }
        
        return Cancelled;
    }
    
    public static String getAccountName(String MainCode,String SubCode) {
        try {
            
            if(SubCode.trim().equals("")) {
                return clsAccount.getAccountName(MainCode,"");
                //return data.getStringValueFromDB("SELECT ACCOUNT_NAME FROM D_FIN_GL WHERE MAIN_ACCOUNT_CODE='"+MainCode+"' ",FinanceGlobal.FinURL);
            }
            else {
                if(data.IsRecordExist("SELECT PARTY_NAME FROM D_FIN_DEPOSIT_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='"+MainCode+"' AND PARTY_CODE='"+SubCode+"'",FinanceGlobal.FinURL)) {
                    return data.getStringValueFromDB("SELECT PARTY_NAME FROM D_FIN_DEPOSIT_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='"+MainCode+"' AND PARTY_CODE='"+SubCode+"' AND MAIN_ACCOUNT_CODE='"+MainCode+"'",FinanceGlobal.FinURL);
                }
                else {
                    
                    if(data.IsRecordExist("SELECT PARTY_NAME FROM D_FIN_DEPOSIT_PARTY_MASTER WHERE PARTY_CODE='"+SubCode+"' AND TRIM(MAIN_ACCOUNT_CODE)='' ",FinanceGlobal.FinURL)) {
                        return data.getStringValueFromDB("SELECT PARTY_NAME FROM D_FIN_DEPOSIT_PARTY_MASTER WHERE PARTY_CODE='"+SubCode+"' AND TRIM(MAIN_ACCOUNT_CODE)='' ",FinanceGlobal.FinURL);
                    }
                    else {
                        return data.getStringValueFromDB("SELECT PARTY_NAME FROM D_FIN_DEPOSIT_PARTY_MASTER WHERE PARTY_CODE='"+SubCode+"'",FinanceGlobal.FinURL);
                    }
                    
                }
            }
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getAccountNameAddress(String MainCode,String SubCode) {
        try {
            if(data.IsRecordExist("SELECT CONCAT(ADDRESS,' ',CITY) FROM D_FIN_DEPOSIT_PARTY_MASTER WHERE PARTY_CODE='"+SubCode+"'",FinanceGlobal.FinURL)) {
                return data.getStringValueFromDB("SELECT CONCAT(ADDRESS,' ',CITY) FROM D_FIN_DEPOSIT_PARTY_MASTER WHERE PARTY_CODE='"+SubCode+"' AND MAIN_ACCOUNT_CODE='"+MainCode+"' ",FinanceGlobal.FinURL);
            }
            else {
                return data.getStringValueFromDB("SELECT CONCAT(ADDRESS,' ',CITY) FROM D_FIN_DEPOSIT_PARTY_MASTER WHERE PARTY_CODE='"+SubCode+"' ",FinanceGlobal.FinURL);
            }
        }
        catch(Exception e) {
            return "";
        }
    }
    
    private boolean Validate() {
        //** Validations **//
        if(getAttribute("MAIN_ACCOUNT_CODES").getString().equals("")) {
            LastError="Please specify Ledger Code";
            return false;
        }
        
        if(getAttribute("PARTY_CODE").getString().equals("")) {
            LastError="Please specify Party Code";
            return false;
        }
        
        if(data.IsRecordExist("SELECT * FROM D_FIN_DEPOSIT_PARTY_MASTER WHERE PARTY_CODE='"+getAttribute("PARTY_CODE").getString()+"' AND DEPOSIT_PARTY_ID<>"+getAttribute("DEPOSIT_PARTY_ID").getString(),FinanceGlobal.FinURL)) {
            LastError="Entry with Party Code already exists.";
            return false;
        }
        
        if(data.IsRecordExist("SELECT * FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+getAttribute("PARTY_CODE").getString()+"' ")) {
            LastError="Party Code already exists in finance party master.";
            return false;
        }
        
        
        
        //*****************//
        return true;
    }
    
}
