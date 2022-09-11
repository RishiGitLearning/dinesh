package EITLERP.Finance;

import java.util.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import EITLERP.Finance.*;

/**
 *
 * @author  nrpithva
 * @version
 */

public class clsPolicyInvoiceType{
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
    public HashMap colTypeLot=new HashMap();
    
    private boolean HistoryView=false;
    
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
    public clsPolicyInvoiceType() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("SR_NO",new Variant(0));
        props.put("INVOICE_TYPE_ID",new Variant(""));
        props.put("INVOICE_TYPE_DESC",new Variant(""));
        props.put("CANCELLED", new Variant(false));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
        
    }
    
    /**Load Data. This method loads data from database to Business Object**/
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_SAL_POLICY_INVOICE_TYPE WHERE COMPANY_ID = '"+ pCompanyID +"' ORDER BY SR_NO");
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
    
    public static HashMap getList(String pCondition) {
        
        ResultSet rsTmp;
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            
            rsTmp=data.getResult("SELECT * FROM D_SAL_POLICY_INVOICE_TYPE "+pCondition+" ORDER BY SR_NO ");
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsPolicyInvoiceType ObjTypeMaster=new clsPolicyInvoiceType();
                
                //Populate the user
                ObjTypeMaster.setAttribute("INVOICE_TYPE_ID",rsTmp.getString("INVOICE_TYPE_ID"));
                ObjTypeMaster.setAttribute("INVOICE_TYPE_DESC",rsTmp.getString("INVOICE_TYPE_DESC"));
                
                //Put the prepared user object into list
                List.put(Long.toString(Counter),ObjTypeMaster);
                rsTmp.next();
            }//Out While
        }
        catch(Exception e) {
            //JOptionPane.showMessageDialog(null,"Error occured"+e.getMessage());
        }
        
        return List;
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
    
    public boolean setData() {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getConn();
        
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
            setAttribute("SR_NO",UtilFunctions.getDouble(rsResultSet,"SR_NO", 0));
            setAttribute("INVOICE_TYPE_ID",UtilFunctions.getString(rsResultSet,"INVOICE_TYPE_ID", ""));
            setAttribute("INVOICE_TYPE_DESC",UtilFunctions.getString(rsResultSet,"INVOICE_TYPE_DESC", ""));
            
            setAttribute("CANCELLED",UtilFunctions.getBoolean(rsResultSet,"CANCELLED",false));
            setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00")));
            setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00")));
            setAttribute("CHANGED",UtilFunctions.getBoolean(rsResultSet,"CHANGED",false));
            setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHANGED_DATE","0000-00-00")));
            
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    
    
    //    public boolean Insert() {
    //
    //        Statement stHistory,stHeader;
    //        ResultSet rsHistory,rsHeader,rsTmp;
    //
    //        try {
    //
    //            // ---- History Related Changes ------ //
    //            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
    //            rsHistory=stHistory.executeQuery("SELECT * FROM D_FD_SCHEME_MASTER_H WHERE SCHEME_ID='1'"); // '1' for restricting all data retrieval
    //            rsHistory.first();
    //            //------------------------------------//
    //
    //            //** Validations **//
    //            if(!Validate())
    //            {
    //              return false;
    //            }
    //
    //            if(data.IsRecordExist("SELECT SCHEME_ID FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+getAttribute("SCHEME_ID").getString()+"'")) {
    //                LastError="Scheme Id already exist.";
    //                return false;
    //            }
    //            //*****************//
    //
    //
    //            ApprovalFlow ObjFlow=new ApprovalFlow();
    //
    //            //Start Transaction
    //            //Conn.setAutoCommit(false);
    //            //data.SetAutoCommit(false);
    //
    //
    //            rsResultSet.first();
    //            rsResultSet.moveToInsertRow();
    //            rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
    //            rsResultSet.updateString("SCHEME_ID",getAttribute("SCHEME_ID").getString());
    //            rsResultSet.updateString("SCHEME_NAME",getAttribute("SCHEME_NAME").getString());
    //            rsResultSet.updateDouble("MINIMUM_AMOUNT",getAttribute("MINIMUM_AMOUNT").getDouble());
    //            rsResultSet.updateDouble("MINIMUM_PERIOD",getAttribute("MINIMUM_PERIOD").getDouble());
    //            rsResultSet.updateInt("INTEREST_CALCULATION_TYPE",getAttribute("INTEREST_CALCULATION_TYPE").getInt());
    //            rsResultSet.updateInt("INTEREST_CALCULATION_PERIOD",getAttribute("INTEREST_CALCULATION_PERIOD").getInt());
    //            rsResultSet.updateDouble("INTEREST_PERCENTAGE",getAttribute("INTEREST_PERCENTAGE").getDouble());
    //            rsResultSet.updateInt("DEPOSIT_TYPE_ID",getAttribute("DEPOSIT_TYPE_ID").getInt());
    //            rsResultSet.updateString("INT_MAIN_ACCOUNT_CODE",getAttribute("INT_MAIN_ACCOUNT_CODE").getString());
    //            rsResultSet.updateString("DEPOSIT_MAIN_ACCOUNT_CODE",getAttribute("DEPOSIT_MAIN_ACCOUNT_CODE").getString());
    //            rsResultSet.updateDouble("PM_DEDUCTION_PERCENTAGE",getAttribute("PM_DEDUCTION_PERCENTAGE").getDouble());
    //
    //            rsResultSet.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
    //            rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
    //            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
    //            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
    //            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
    //            rsResultSet.updateBoolean("CHANGED",false);
    //            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
    //            rsResultSet.updateBoolean("APPROVED",false);
    //            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
    //            rsResultSet.updateBoolean("REJECTED",false);
    //            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
    //            rsResultSet.updateString("REJECTED_REMARKS","");
    //            rsResultSet.updateBoolean("CANCELLED",false);
    //            rsResultSet.insertRow();
    //
    //
    //            //========= Inserting Into History =================//
    //            rsHistory.moveToInsertRow();
    //            rsHistory.updateInt("REVISION_NO",1);
    //            rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
    //            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
    //            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
    //            rsHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
    //            rsHistory.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
    //            rsHistory.updateString("SCHEME_ID",getAttribute("SCHEME_ID").getString());
    //            rsHistory.updateString("SCHEME_NAME",getAttribute("SCHEME_NAME").getString());
    //            rsHistory.updateDouble("MINIMUM_AMOUNT",getAttribute("MINIMUM_AMOUNT").getDouble());
    //            rsHistory.updateDouble("MINIMUM_PERIOD",getAttribute("MINIMUM_PERIOD").getDouble());
    //            rsHistory.updateInt("INTEREST_CALCULATION_TYPE",getAttribute("INTEREST_CALCULATION_TYPE").getInt());
    //            rsHistory.updateInt("INTEREST_CALCULATION_PERIOD",getAttribute("INTEREST_CALCULATION_PERIOD").getInt());
    //            rsHistory.updateDouble("INTEREST_PERCENTAGE",getAttribute("INTEREST_PERCENTAGE").getDouble());
    //            rsHistory.updateInt("DEPOSIT_TYPE_ID",getAttribute("DEPOSIT_TYPE_ID").getInt());
    //            rsHistory.updateString("INT_MAIN_ACCOUNT_CODE",getAttribute("INT_MAIN_ACCOUNT_CODE").getString());
    //            rsHistory.updateString("DEPOSIT_MAIN_ACCOUNT_CODE",getAttribute("DEPOSIT_MAIN_ACCOUNT_CODE").getString());
    //            rsHistory.updateDouble("PM_DEDUCTION_PERCENTAGE",getAttribute("PM_DEDUCTION_PERCENTAGE").getDouble());
    //
    //
    //            rsHistory.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
    //            rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
    //            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
    //            rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
    //            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
    //            rsHistory.updateBoolean("CHANGED",false);
    //            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
    //            rsHistory.updateBoolean("APPROVED",false);
    //            rsHistory.updateString("APPROVED_DATE","0000-00-00");
    //            rsHistory.updateBoolean("REJECTED",false);
    //            rsHistory.updateString("REJECTED_DATE","0000-00-00");
    //            rsHistory.updateString("REJECTED_REMARKS","");
    //            rsHistory.updateBoolean("CANCELLED",false);
    //
    //            rsHistory.insertRow();
    //
    //            //======== Update the Approval Flow =========//
    //
    //            ObjFlow.CompanyID=EITLERPGLOBAL.gCompanyID;
    //            ObjFlow.ModuleID=ModuleID;
    //            ObjFlow.DocNo=getAttribute("SCHEME_ID").getString();
    //            ObjFlow.From=getAttribute("FROM").getInt();
    //            ObjFlow.To=getAttribute("TO").getInt();
    //            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
    //            ObjFlow.TableName="D_FD_SCHEME_MASTER";
    //            ObjFlow.IsCreator=true;
    //            ObjFlow.HierarchyID=getAttribute("HIERARCHY_ID").getInt();
    //            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
    //            ObjFlow.FieldName="SCHEME_ID";
    //            ObjFlow.UseSpecifiedURL=true;
    //            ObjFlow.SpecificURL=EITLERPGLOBAL.DatabaseURL;
    //            if(ObjFlow.Status.equals("H")) {
    //                ObjFlow.Status="A";
    //                ObjFlow.To=ObjFlow.From;
    //                ObjFlow.UpdateFlow();
    //            }
    //            else {
    //                if(!ObjFlow.UpdateFlow()) {
    //                    LastError=ObjFlow.LastError;
    //                }
    //            }
    //            ObjFlow.UseSpecifiedURL=false;
    //
    //            //--------- Approval Flow Update complete -----------
    //
    //            //Conn.commit();
    //            //data.SetCommit();
    //
    //            //data.SetAutoCommit(true);
    //
    //            MoveLast();
    //            return true;
    //        }
    //        catch(Exception e) {
    //            try {
    //                //Conn.rollback();
    //            }
    //            catch(Exception c){}
    //            //data.SetRollback();
    //            //data.SetAutoCommit(true);
    //            LastError= e.getMessage();
    //            return false;
    //        }
    //    }
    //
    //
    //
    //
    //    public boolean Update() {
    //
    //        Statement stHistory,stHeader;
    //        ResultSet rsHistory,rsHeader;
    //        boolean Validate=true;
    //
    //        try {
    //
    //            String theDocNo=getAttribute("SCHEME_ID").getString();
    //
    //            //** Validations **//
    //            if(!Validate())
    //            {
    //              return false;
    //            }
    //            /*
    //            if(data.IsRecordExist("SELECT SCHEME_ID FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+ theDocNo +"'")) {
    //                LastError="Scheme Id already exist.";
    //                return false;
    //            }
    //             */
    //            //*****************//
    //
    //
    //            ApprovalFlow ObjFlow=new ApprovalFlow();
    //
    //            String AStatus=getAttribute("APPROVAL_STATUS").getString();
    //            if(AStatus.equals("R")) {
    //                Validate=false; //Do not validate while in rejection mode.
    //            }
    //
    //
    //            //** Open History Table Connections **//
    //            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
    //            rsHistory=stHistory.executeQuery("SELECT * FROM D_FD_SCHEME_MASTER_H WHERE SCHEME_ID='1'"); // '1' for restricting all data retrieval
    //            rsHistory.first();
    //            //** --------------------------------**//
    //
    //            //Start Transaction
    //            //Conn.setAutoCommit(false);
    //            //data.SetAutoCommit(false);
    //
    //            //rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
    //            //rsResultSet.updateString("SCHEME_ID",getAttribute("SCHEME_ID").getString());
    //            rsResultSet.updateString("SCHEME_NAME",getAttribute("SCHEME_NAME").getString());
    //            rsResultSet.updateDouble("MINIMUM_AMOUNT",getAttribute("MINIMUM_AMOUNT").getDouble());
    //            rsResultSet.updateDouble("MINIMUM_PERIOD",getAttribute("MINIMUM_PERIOD").getDouble());
    //            rsResultSet.updateInt("INTEREST_CALCULATION_TYPE",getAttribute("INTEREST_CALCULATION_TYPE").getInt());
    //            rsResultSet.updateInt("INTEREST_CALCULATION_PERIOD",getAttribute("INTEREST_CALCULATION_PERIOD").getInt());
    //            rsResultSet.updateDouble("INTEREST_PERCENTAGE",getAttribute("INTEREST_PERCENTAGE").getDouble());
    //            rsResultSet.updateInt("DEPOSIT_TYPE_ID",getAttribute("DEPOSIT_TYPE_ID").getInt());
    //            rsResultSet.updateString("INT_MAIN_ACCOUNT_CODE",getAttribute("INT_MAIN_ACCOUNT_CODE").getString());
    //            rsResultSet.updateString("DEPOSIT_MAIN_ACCOUNT_CODE",getAttribute("DEPOSIT_MAIN_ACCOUNT_CODE").getString());
    //            rsResultSet.updateDouble("PM_DEDUCTION_PERCENTAGE",getAttribute("PM_DEDUCTION_PERCENTAGE").getDouble());
    //
    //            rsResultSet.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
    //            rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
    //            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
    //            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
    //            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
    //            rsResultSet.updateBoolean("CHANGED",true);
    //            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
    //            rsResultSet.updateBoolean("CANCELLED",false);
    //            System.out.println("hi");
    //            rsResultSet.updateRow();
    //
    //            //========= Inserting Into History =================//
    //            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM D_FD_SCHEME_MASTER_H WHERE SCHEME_ID='"+theDocNo+"'");
    //
    //            RevNo++;
    //
    //            String RevDocNo=getAttribute("SCHEME_ID").getString();
    //            //System.out.println("RevNo="+RevNo);
    //            //========= Inserting Into History =================//
    //            rsHistory.moveToInsertRow();
    //            rsHistory.updateInt("REVISION_NO",RevNo);
    //            rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
    //            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
    //            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
    //            rsHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
    //            rsHistory.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
    //            rsHistory.updateString("SCHEME_ID",getAttribute("SCHEME_ID").getString());
    //            rsHistory.updateString("SCHEME_NAME",getAttribute("SCHEME_NAME").getString());
    //            rsHistory.updateDouble("MINIMUM_AMOUNT",getAttribute("MINIMUM_AMOUNT").getDouble());
    //            rsHistory.updateDouble("MINIMUM_PERIOD",getAttribute("MINIMUM_PERIOD").getDouble());
    //            rsHistory.updateInt("INTEREST_CALCULATION_TYPE",getAttribute("INTEREST_CALCULATION_TYPE").getInt());
    //            rsHistory.updateInt("INTEREST_CALCULATION_PERIOD",getAttribute("INTEREST_CALCULATION_PERIOD").getInt());
    //            rsHistory.updateDouble("INTEREST_PERCENTAGE",getAttribute("INTEREST_PERCENTAGE").getDouble());
    //            rsHistory.updateInt("DEPOSIT_TYPE_ID",getAttribute("DEPOSIT_TYPE_ID").getInt());
    //            rsHistory.updateString("INT_MAIN_ACCOUNT_CODE",getAttribute("INT_MAIN_ACCOUNT_CODE").getString());
    //            rsHistory.updateString("DEPOSIT_MAIN_ACCOUNT_CODE",getAttribute("DEPOSIT_MAIN_ACCOUNT_CODE").getString());
    //            rsHistory.updateDouble("PM_DEDUCTION_PERCENTAGE",getAttribute("PM_DEDUCTION_PERCENTAGE").getDouble());
    //
    //            rsHistory.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
    //            rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
    //            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
    //            rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
    //            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
    //            rsHistory.updateBoolean("CHANGED",true);
    //            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
    //            rsHistory.updateBoolean("APPROVED",false);
    //            rsHistory.updateString("APPROVED_DATE","0000-00-00");
    //            rsHistory.updateBoolean("REJECTED",false);
    //            rsHistory.updateString("REJECTED_DATE","0000-00-00");
    //            rsHistory.updateString("REJECTED_REMARKS","");
    //            rsHistory.updateBoolean("CANCELLED",false);
    //            System.out.println("hi1");
    //            rsHistory.insertRow();
    //
    //
    //            //======== Update the Approval Flow =========
    //            ObjFlow.CompanyID=EITLERPGLOBAL.gCompanyID;
    //            ObjFlow.ModuleID=ModuleID;
    //            ObjFlow.DocNo=getAttribute("SCHEME_ID").getString();
    //            ObjFlow.From=getAttribute("FROM").getInt();
    //            ObjFlow.To=getAttribute("TO").getInt();
    //            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
    //            ObjFlow.TableName="D_FD_SCHEME_MASTER";
    //            ObjFlow.IsCreator=false;
    //            ObjFlow.HierarchyID=getAttribute("HIERARCHY_ID").getInt();
    //            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
    //            ObjFlow.FieldName="SCHEME_ID";
    //            ObjFlow.UseSpecifiedURL=true;
    //            ObjFlow.SpecificURL=EITLERPGLOBAL.DatabaseURL;
    //            //==== Handling Rejected Documents ==========//
    //
    //
    //            if(AStatus.equals("R")) {
    //                ObjFlow.To=getAttribute("SEND_DOC_TO").getInt();
    //                ObjFlow.ExplicitSendTo=true;
    //            }
    //
    //            //==== Handling Rejected Documents ==========//
    //            boolean IsRejected=getAttribute("REJECTED").getBool();
    //
    //            if(IsRejected) {
    //                //Remove the Rejected Flag First
    //                data.Execute("UPDATE D_FD_SCHEME_MASTER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE SCHEME_ID='"+theDocNo+"'");
    //                //Remove Old Records from D_COM_DOC_DATA
    //                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE MODULE_ID="+ModuleID+" AND DOC_NO='"+theDocNo+"'");
    //
    //                ObjFlow.IsCreator=true;
    //            }
    //            //==========================================//
    //            if(ObjFlow.Status.equals("H")) {
    //                if(IsRejected) {
    //                    ObjFlow.Status="A";
    //                    ObjFlow.To=ObjFlow.From;
    //                    ObjFlow.UpdateFlow();
    //                }
    //            }
    //            else {
    //                if(!ObjFlow.UpdateFlow()) {
    //                    LastError=ObjFlow.LastError;
    //                }
    //            }
    //
    //            ObjFlow.UseSpecifiedURL=false;
    //
    //            //Conn.commit();
    //            //data.SetCommit();
    //
    //            //data.SetAutoCommit(true);
    //
    //            return true;
    //        }
    //        catch(Exception e) {
    //            try {
    //                //Conn.rollback();
    //                //data.SetRollback();
    //            }
    //            catch(Exception c){}
    //            //data.SetAutoCommit(true);
    //
    //            LastError = e.getMessage();
    //            return false;
    //        }
    //    }
    //
    //
    //    public boolean CanDelete(int CompanyID,String SchemeID,int UserID) {
    //        Statement tmpStmt;
    //        ResultSet rsTmp;
    //        String strSQL="";
    //
    //        try {
    //
    //            if(HistoryView) {
    //                return false;
    //            }
    //
    //            //First check that document is approved or not
    //            strSQL="SELECT COUNT(*) AS COUNT FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+SchemeID+"' AND (APPROVED=1)";
    //            int Count=data.getIntValueFromDB(strSQL);
    //
    //            if(Count>0)  //Item is Approved
    //            {
    //                //Discard the request
    //                return false;
    //            }
    //            else {
    //                return true;
    //            }
    //
    //        }
    //        catch(Exception e) {
    //            return false;
    //        }
    //    }
    //
    //
    //    //This routine checks and returns whether the item is editable or not
    //    //Criteria is Approved item cannot be changed
    //    //and if not approved then user id is checked whether doucment
    //    //is waiting for his approval.
    //    public boolean IsEditable(int CompanyID,String SchemeID,int UserID) {
    //        Statement tmpStmt;
    //        ResultSet rsTmp;
    //        String strSQL="";
    //
    //        try {
    //
    //            if(HistoryView) {
    //                return false;
    //            }
    //            //First check that document is approved or not
    //            strSQL="SELECT SCHEME_ID FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+SchemeID+"' AND (APPROVED=1)";
    //
    //            if(data.IsRecordExist(strSQL)) {
    //                return false;
    //            }
    //            else {
    //                strSQL="SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+SchemeID+"' AND USER_ID="+UserID+" AND STATUS='W'";
    //                if(data.IsRecordExist(strSQL)) {
    //                    //Yes document is waiting for this user
    //                    return true;
    //                }
    //                else {
    //                    //Document is not editable by this user
    //                    return false;
    //                }
    //            }
    //        }
    //        catch(Exception e) {
    //            return false;
    //        }
    //    }
    //
    //
    //    public boolean Delete(int UserID) {
    //        try {
    //            String SchemeID=getAttribute("SCHEME_ID").getString();
    //
    //            if(CanDelete(EITLERPGLOBAL.gCompanyID,SchemeID,UserID)) {
    //                String strSQL = "DELETE FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+SchemeID+"'";
    //                data.Execute(strSQL);
    //
    //                strSQL = "DELETE FROM D_FD_SCHEME_PERIOD WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID +" AND SCHEME_ID='"+SchemeID+"'";
    //                data.Execute(strSQL);
    //
    //                LoadData(EITLERPGLOBAL.gCompanyID);
    //                return true;
    //            }
    //            else {
    //                LastError="Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
    //                return false;
    //            }
    //        }
    //        catch(Exception e) {
    //            LastError = e.getMessage();
    //            return false;
    //        }
    //    }
    //
    //
    //    public Object getObject(int CompanyID, String SchemeID) {
    //        String strCondition = " WHERE SCHEME_ID='" + SchemeID + "'";
    //        clsPolicyMaster objScheme = new clsPolicyMaster();
    //        objScheme.Filter(strCondition,CompanyID);
    //        return objScheme;
    //    }
    //
    //
    //
    //
    //    public boolean Filter(String Condition,int CompanyID) {
    //        Ready=false;
    //
    //        try {
    //            String strSQL = "SELECT * FROM D_FD_SCHEME_MASTER " + Condition ;
    //            Conn=data.getConn();
    //            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
    //            rsResultSet=Stmt.executeQuery(strSQL);
    //
    //            if(!rsResultSet.first()) {
    //                strSQL = "SELECT * FROM D_FD_SCHEME_MASTER ORDER BY SCHEME_ID ";
    //                rsResultSet=Stmt.executeQuery(strSQL);
    //                Ready=true;
    //                MoveLast();
    //                return false;
    //            }
    //            else {
    //                Ready=true;
    //                MoveLast();
    //                return true;
    //            }
    //        }
    //        catch(Exception e) {
    //            LastError = e.getMessage();
    //            return false;
    //        }
    //    }
    //
    //
    //
    //    public static HashMap getPendingApprovals(int CompanyID,int UserID,int Order) {
    //        ResultSet rsTemp=null;
    //        String strSQL="";
    //
    //        HashMap List=new HashMap();
    //        long Counter=0;
    //
    //        try {
    //
    //            if(Order==EITLERPGLOBAL.OnRecivedDate) {
    //                strSQL="SELECT FINANCE.D_FD_SCHEME_MASTER.SCHEME_ID,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_SCHEME_MASTER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_SCHEME_MASTER.SCHEME_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
    //            }
    //
    //
    //            if(Order==EITLERPGLOBAL.OnDocDate) {
    //                strSQL="SELECT FINANCE.D_FD_SCHEME_MASTER.SCHEME_ID,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_SCHEME_MASTER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_SCHEME_MASTER.SCHEME_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_FD_SCHEME_MASTER.SCHEME_ID";
    //            }
    //
    //            if(Order==EITLERPGLOBAL.OnDocNo) {
    //                strSQL="SELECT FINANCE.D_FD_SCHEME_MASTER.SCHEME_ID,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_SCHEME_MASTER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_SCHEME_MASTER.SCHEME_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_FD_SCHEME_MASTER.SCHEME_ID";
    //            }
    //
    //            rsTemp=data.getResult(strSQL);
    //            rsTemp.first();
    //
    //            Counter=0;
    //
    //            if(rsTemp.getRow()>0)
    //            {
    //            while(!rsTemp.isAfterLast()) {
    //                Counter=Counter+1;
    //                clsPolicyMaster ObjScheme=new clsPolicyMaster();
    //
    //                //------------- Header Fields --------------------//
    //                ObjScheme.setAttribute("SCHEME_ID",UtilFunctions.getString(rsTemp,"SCHEME_ID",""));
    //                ObjScheme.setAttribute("RECEIVED_DATE",UtilFunctions.getString(rsTemp,"RECEIVED_DATE","0000-00-00"));
    //                // ----------------- End of Header Fields ------------------------------------//
    //
    //                List.put(Long.toString(Counter),ObjScheme);
    //                rsTemp.next();
    //            }
    //            }
    //            rsTemp.close();
    //
    //        }
    //        catch(Exception e) {
    //        }
    //
    //        return List;
    //    }
    //
    //
    //    public boolean ShowHistory(int CompanyID,String SchemeID) {
    //        Ready=false;
    //        try {
    //            String strSQL="SELECT * FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+SchemeID + "'";
    //            rsResultSet=data.getResult(strSQL);
    //            Ready=true;
    //            HistoryView=true;
    //            MoveFirst();
    //            return true;
    //        }
    //        catch(Exception e) {
    //            LastError=e.getMessage();
    //            return false;
    //        }
    //    }
    //
    //
    //    public static HashMap getHistoryList(int CompanyID,String SchemeID) {
    //        HashMap List=new HashMap();
    //        ResultSet rsTmp;
    //
    //        try {
    //            String strSQL="SELECT * FROM D_FD_SCHEME_MASTER_H WHERE SCHEME_ID='"+SchemeID+"'";
    //            rsTmp=data.getResult(strSQL);
    //            rsTmp.first();
    //
    //            if(rsTmp.getRow()>0)
    //            {
    //            while(!rsTmp.isAfterLast()) {
    //                clsPolicyMaster objScheme=new clsPolicyMaster();
    //
    //                objScheme.setAttribute("SCHEME_ID",UtilFunctions.getString(rsTmp,"SCHEME_ID", ""));
    //                objScheme.setAttribute("REVISION_NO",UtilFunctions.getInt(rsTmp,"REVISION_NO", 0));
    //                objScheme.setAttribute("UPDATED_BY",UtilFunctions.getString(rsTmp,"UPDATED_BY", ""));
    //                objScheme.setAttribute("APPROVAL_STATUS",UtilFunctions.getString(rsTmp,"APPROVAL_STATUS", ""));
    //                objScheme.setAttribute("ENTRY_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"ENTRY_DATE","0000-00-00")));
    //                objScheme.setAttribute("APPROVER_REMARKS",UtilFunctions.getString(rsTmp,"APPROVER_REMARKS", ""));
    //
    //                List.put(Integer.toString(List.size()+1),objScheme);
    //                rsTmp.next();
    //            }
    //            }
    //            rsTmp.close();
    //            return List;
    //
    //        }
    //        catch(Exception e) {
    //            return List;
    //        }
    //    }
    //
    //
    //    public static String getDocStatus(int CompanyID,String SchemeID) {
    //        ResultSet rsTmp;
    //        String strMessage="";
    //
    //        try {
    //
    //            String strSQL="SELECT SCHEME_ID,APPROVED,CANCELLED FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+SchemeID+"'";
    //            rsTmp=data.getResult(strSQL);
    //            rsTmp.first();
    //
    //            if(rsTmp.getRow()>0) {
    //                if(UtilFunctions.getBoolean(rsTmp,"APPROVED",false))   {
    //                    if(UtilFunctions.getBoolean(rsTmp,"CANCELLED",false))  {
    //                        strMessage="Document is cancelled";
    //                    }
    //                    else {
    //                        strMessage="";
    //                    }
    //                }
    //                else {
    //                    strMessage="Document is created but is under approval";
    //                }
    //
    //            }
    //            else {
    //                strMessage="Document does not exist";
    //            }
    //            rsTmp.close();
    //        }
    //        catch(Exception e) {
    //        }
    //
    //        return strMessage;
    //
    //    }
    //
    //
    //    public static boolean CanCancel(int CompanyID,String SchemeID) {
    //        ResultSet rsTmp=null;
    //        boolean canCancel=false;
    //
    //        try {
    //            rsTmp=data.getResult("SELECT SCHEME_ID FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+SchemeID+"' AND CANCELLED=0");
    //            rsTmp.first();
    //
    //            if(rsTmp.getRow()>0) {
    //
    //                String sScheme_ID=UtilFunctions.getString(rsTmp,"SCHEME_ID","");
    //
    //                if(data.IsRecordExist("SELECT * FROM D_FD_SCHEME_MASTER WHERE CANCELLED=0 AND COMPANY_ID="+CompanyID+" AND SCHEME_ID='"+sScheme_ID+"'")) {
    //                    canCancel=false;
    //                }
    //                else {
    //                    canCancel=true;
    //                }
    //            }
    //
    //            rsTmp.close();
    //        }
    //        catch(Exception e) {
    //
    //        }
    //
    //        return canCancel;
    //    }
    //
    //
    //
    //    public static boolean CancelDoc(int CompanyID,String SchemeID) {
    //
    //        ResultSet rsTmp=null;
    //        boolean Cancelled=false;
    //
    //        try {
    //            if(CanCancel(CompanyID,SchemeID)) {
    //
    //                boolean ApprovedDoc=false;
    //
    //                rsTmp=data.getResult("SELECT APPROVED FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID="+SchemeID);
    //                rsTmp.first();
    //
    //                if(rsTmp.getRow()>0) {
    //                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
    //                }
    //
    //
    //                if(ApprovedDoc) {
    //
    //                }
    //                else {
    //                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+SchemeID+"' AND MODULE_ID="+ModuleID);
    //                }
    //
    //                data.Execute("UPDATE D_FD_SCHEME_MASTER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE SCHEME_ID="+SchemeID);
    //
    //                Cancelled=true;
    //            }
    //
    //            rsTmp.close();
    //        }
    //        catch(Exception e) {
    //
    //        }
    //
    //        return Cancelled;
    //    }
    //
    //
    //    private boolean Validate() {
    //        //** Validations **//
    //        if(getAttribute("SCHEME_ID").getString().equals("")) {
    //            LastError="Please specify Scheme Code";
    //            return false;
    //        }
    //
    //
    //        if(getAttribute("SCHEME_NAME").getString().equals("")) {
    //            LastError="Please specify Scheme Name";
    //            return false;
    //        }
    //
    //
    //        //*****************//
    //
    //        return true;
    //    }
    //
    //    public static String getAccountName(int CompanyID,String MainCode) {
    //        try {
    //            return data.getStringValueFromDB("SELECT ACCOUNT_NAME FROM D_FIN_GL WHERE MAIN_ACCOUNT_CODE='"+MainCode+"'");
    //        }
    //        catch(Exception e) {
    //            return "";
    //        }
    //    }
    //
    
    
}
