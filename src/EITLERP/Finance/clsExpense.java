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

public class clsExpense{
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    private HashMap props;
    public boolean Ready = false;
    
    private boolean HistoryView=false;
    
    public static int ModuleID=62;
    
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
    public clsExpense() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("EXPENSE_ID",new Variant(""));
        props.put("MAIN_ACCOUNT_CODE",new Variant(""));
        props.put("EXPENSE_TYPE",new Variant(0));
        props.put("EXPENSE_DESCRIPTION",new Variant(""));
        props.put("MAX_AMOUNT",new Variant(0));
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
    
    /**Load Data. This method loads data from database to Business Object**/
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FIN_EXPENSE_MASTER ORDER BY EXPENSE_ID");
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
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FIN_EXPENSE_MASTER_H WHERE EXPENSE_ID='1' ");
            rsHistory.first();
            //------------------------------------//
            
            //** Validations **//
            if(!Validate()) {
                return false;
            }
            //*****************//
            
            String ExpenseID = (String)getAttribute("EXPENSE_ID").getObj();
            
            if(data.IsRecordExist("SELECT * FROM D_FIN_EXPENSE_MASTER WHERE EXPENSE_ID ='"+ExpenseID+"'",FinanceGlobal.FinURL)) {
                LastError = "EXPENSE ID is already Existing" ;
                return false ;
            }
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //Start Transaction
            //Conn.setAutoCommit(false);
            //data.SetAutoCommit(false);
            
            //long NewExpenseID=data.getMaxID("D_FIN_EXPENSE_MASTER","EXPENSE_ID",FinanceGlobal.FinURL);
            
            //--------- Generate New Internal Account ID  ------------
            //setAttribute("EXPENSE_ID",NewExpenseID);
            //--------------------------------------------------------
            
            //--------- Generate New Internal Account ID  ------------
            //setAttribute("EXPENSE_ID","");
            //String DocNo=clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID,clsExpense.ModuleID, getAttribute("FFNO").getInt(),true);
            //setAttribute("EXPENSE_ID",DocNo);
            //--------------------------------------------------------
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsResultSet.updateString("EXPENSE_ID",getAttribute("EXPENSE_ID").getString());
            rsResultSet.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsResultSet.updateInt("EXPENSE_TYPE",getAttribute("EXPENSE_TYPE").getInt());
            rsResultSet.updateString("EXPENSE_DESCRIPTION",getAttribute("EXPENSE_DESCRIPTION").getString());
            rsResultSet.updateDouble("MAX_AMOUNT",getAttribute("MAX_AMOUNT").getDouble());
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
            rsHistory.updateString("EXPENSE_ID",getAttribute("EXPENSE_ID").getString());
            rsHistory.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsHistory.updateInt("EXPENSE_TYPE",getAttribute("EXPENSE_TYPE").getInt());
            rsHistory.updateString("EXPENSE_DESCRIPTION",getAttribute("EXPENSE_DESCRIPTION").getString());
            rsHistory.updateDouble("MAX_AMOUNT",getAttribute("MAX_AMOUNT").getDouble());
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
            ObjFlow.DocNo=getAttribute("EXPENSE_ID").getString();
            ObjFlow.From=getAttribute("FROM").getInt();
            ObjFlow.To=getAttribute("TO").getInt();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_FIN_EXPENSE_MASTER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=getAttribute("HIERARCHY_ID").getInt();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName="EXPENSE_ID";
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
            
            String theDocNo=getAttribute("EXPENSE_ID").getString();
            
            //** Validations **//
            if(!Validate()) {
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
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FIN_EXPENSE_MASTER_H WHERE EXPENSE_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //** --------------------------------**//
            
            //Start Transaction
            //Conn.setAutoCommit(false);
            //data.SetAutoCommit(false);
            
            
            rsResultSet.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsResultSet.updateInt("EXPENSE_TYPE",getAttribute("EXPENSE_TYPE").getInt());
            rsResultSet.updateString("EXPENSE_DESCRIPTION",getAttribute("EXPENSE_DESCRIPTION").getString());
            rsResultSet.updateDouble("MAX_AMOUNT",getAttribute("MAX_AMOUNT").getDouble());
            rsResultSet.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
            rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateRow();
            
            //========= Inserting Into History =================//
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM D_FIN_EXPENSE_MASTER_H WHERE EXPENSE_ID='"+theDocNo+"'",FinanceGlobal.FinURL);
            RevNo++;
            String RevDocNo=getAttribute("EXPENSE_ID").getString();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo);
            rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            rsHistory.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsHistory.updateString("EXPENSE_ID",getAttribute("EXPENSE_ID").getString());
            rsHistory.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsHistory.updateInt("EXPENSE_TYPE",getAttribute("EXPENSE_TYPE").getInt());
            rsHistory.updateString("EXPENSE_DESCRIPTION",getAttribute("EXPENSE_DESCRIPTION").getString());
            rsHistory.updateDouble("MAX_AMOUNT",getAttribute("MAX_AMOUNT").getDouble());
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
            ObjFlow.DocNo=getAttribute("EXPENSE_ID").getString();
            ObjFlow.From=getAttribute("FROM").getInt();
            ObjFlow.To=getAttribute("TO").getInt();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_FIN_EXPENSE_MASTER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=getAttribute("HIERARCHY_ID").getInt();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName="EXPENSE_ID";
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
                data.Execute("UPDATE D_FIN_EXPENSE_MASTER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE EXPENSE_ID='"+theDocNo+"'",FinanceGlobal.FinURL);
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
            
            //Conn.commit();
            //data.SetCommit();
            
            //data.SetAutoCommit(true);
            
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
    
    
    public boolean CanDelete(int CompanyID,String ExpenseID,int UserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            
            if(HistoryView) {
                return false;
            }
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FIN_EXPENSE_MASTER WHERE EXPENSE_ID='"+ExpenseID+"' AND (APPROVED=1)";
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
    public boolean IsEditable(int CompanyID,String ExpenseID,int UserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT EXPENSE_ID FROM D_FIN_EXPENSE_MASTER WHERE EXPENSE_ID LIKE '"+ExpenseID+"' AND (APPROVED=1)";
            
            if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                return false;
            }
            else {
                strSQL="SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO LIKE '"+ExpenseID+"' AND USER_ID="+UserID+" AND STATUS='W'";
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
            String ExpenseID=getAttribute("EXPENSE_ID").getString();
            
            if(CanDelete(EITLERPGLOBAL.gCompanyID,ExpenseID,UserID)) {
                String strSQL = "DELETE FROM D_FIN_EXPENSE_MASTER WHERE EXPENSE_ID='"+ExpenseID+"' ";
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
    
    
    public Object getObject(int CompanyID, String ExpenseID) {
        String strCondition = " WHERE EXPENSE_ID='" + ExpenseID+"' ";
        clsExpense objExpense = new clsExpense();
        objExpense.Filter(strCondition,CompanyID);
        return objExpense;
    }
    
    
    public boolean Filter(String Condition,int CompanyID) {
        Ready=false;
        
        try {
            String strSQL = "SELECT * FROM D_FIN_EXPENSE_MASTER " + Condition ;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSQL);
            
            if(!rsResultSet.first()) {
                strSQL = "SELECT * FROM D_FIN_EXPENSE_MASTER ORDER BY EXPENSE_ID ";
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
            setAttribute("EXPENSE_ID",UtilFunctions.getString(rsResultSet,"EXPENSE_ID",""));
            setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsResultSet,"MAIN_ACCOUNT_CODE",""));
            setAttribute("EXPENSE_TYPE",UtilFunctions.getInt(rsResultSet,"EXPENSE_TYPE",0));
            setAttribute("EXPENSE_DESCRIPTION",UtilFunctions.getString(rsResultSet,"EXPENSE_DESCRIPTION",""));
            setAttribute("MAX_AMOUNT",UtilFunctions.getDouble(rsResultSet,"MAX_AMOUNT",0));
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
                strSQL="SELECT FINANCE.D_FIN_EXPENSE_MASTER.EXPENSE_ID,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FIN_EXPENSE_MASTER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_EXPENSE_MASTER.EXPENSE_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            
            if(Order==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT FINANCE.D_FIN_EXPENSE_MASTER.EXPENSE_ID,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FIN_EXPENSE_MASTER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_EXPENSE_MASTER.EXPENSE_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_FIN_EXPENSE_MASTER.EXPENSE_ID";
            }
            
            if(Order==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT FINANCE.D_FIN_EXPENSE_MASTER.EXPENSE_ID,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FIN_EXPENSE_MASTER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_EXPENSE_MASTER.EXPENSE_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_FIN_EXPENSE_MASTER.EXPENSE_ID";
            }
            
            rsTemp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTemp.first();
            
            Counter=0;
            
            if(rsTemp.getRow()>0) {
                while(!rsTemp.isAfterLast()) {
                    Counter=Counter+1;
                    clsExpense ObjDoc=new clsExpense();
                    
                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("EXPENSE_ID",UtilFunctions.getString(rsTemp,"EXPENSE_ID",""));
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
    
    
    public boolean ShowHistory(int CompanyID,String ExpenseID) {
        Ready=false;
        try {
            String strSQL="SELECT * FROM D_FIN_EXPENSE_MASTER_H WHERE EXPENSE_ID='"+ExpenseID+"' ";
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
    
    
    public static HashMap getHistoryList(int CompanyID,String ExpenseID) {
        HashMap List=new HashMap();
        ResultSet rsTmp;
        
        try {
            String strSQL="SELECT * FROM D_FIN_EXPENSE_MASTER_H WHERE EXPENSE_ID='"+ExpenseID+"' ";
            rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsExpense objAccount=new clsExpense();
                    
                    objAccount.setAttribute("EXPENSE_ID",UtilFunctions.getString(rsTmp,"EXPENSE_ID", ""));
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
    
    
    public static String getDocStatus(int CompanyID,String ExpenseID) {
        ResultSet rsTmp;
        String strMessage="";
        
        try {
            
            String strSQL="SELECT EXPENSE_ID,APPROVED,CANCELLED FROM D_FIN_EXPENSE_MASTER WHERE EXPENSE_ID='"+ExpenseID+"' ";
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
    
    
    public static boolean CanCancel(int CompanyID,String ExpenseID) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            rsTmp=data.getResult("SELECT EXPENSE_ID,MAIN_ACCOUNT_CODE FROM D_FIN_EXPENSE_MASTER WHERE EXPENSE_ID='"+ExpenseID+"' AND APPROVED=0 AND CANCELLED=0",FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                
                String MainAccountCode=UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","");
                
                if(data.IsRecordExist("SELECT * FROM D_FIN_EXPENSE_TRANSACTION WHERE EXPENSE_ID='"+ExpenseID+"'",FinanceGlobal.FinURL)) {
                    canCancel=false;
                } else {
                    canCancel=true;
                }
            }
            
            rsTmp.close();
        }
        catch(Exception e) {
            
        }
        
        return canCancel;
    }
    
    
    
    public static boolean CancelDoc(int CompanyID,String ExpenseID) {
        
        ResultSet rsTmp=null;
        boolean Cancelled=false;
        
        try {
            if(CanCancel(CompanyID,ExpenseID)) {
                
                boolean ApprovedDoc=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_FIN_EXPENSE_MASTER WHERE EXPENSE_ID='"+ExpenseID+"' ",FinanceGlobal.FinURL);
                rsTmp.first();
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+ExpenseID+"' AND MODULE_ID="+ModuleID);
                data.Execute("UPDATE D_FIN_EXPENSE_MASTER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE EXPENSE_ID='"+ExpenseID+"' ",FinanceGlobal.FinURL);
                Cancelled=true;
            }
            
            rsTmp.close();
        }
        catch(Exception e) {
            
        }
        
        return Cancelled;
    }
    
    
    private boolean Validate() {
        //** Validations **//
        String ExpenseID ="";
        if(getAttribute("MAIN_ACCOUNT_CODE").getString().equals("")) {
            LastError="Please specify Main Account Code";
            return false;
        }
        
        
        if(getAttribute("EXPENSE_DESCRIPTION").getString().equals("")) {
            LastError="Please specify expense description";
            return false;
        }
        //*****************//
        
        
        
        return true;
    }
    
    public static String getExpenseName(String ExpenseID) {
        String ExpenseName="";
        
        try {
            ExpenseName=data.getStringValueFromDB("SELECT EXPENSE_DESCRIPTION FROM D_FIN_EXPENSE_MASTER WHERE EXPENSE_ID='"+ExpenseID+"' ",FinanceGlobal.FinURL);
        }
        catch(Exception e) {
            
        }
        
        return ExpenseName;
    }
    
    public static String getMainCode(String ExpenseID) {
        return data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_EXPENSE_MASTER WHERE EXPENSE_ID='"+ExpenseID+"'",FinanceGlobal.FinURL);
    }
}
