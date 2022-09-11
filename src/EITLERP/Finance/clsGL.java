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

public class clsGL{
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
    private boolean HistoryView=false;
    
    public static int ModuleID=57;
    
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
    public clsGL() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("ACCOUNT_ID",new Variant(0));
        props.put("MAIN_ACCOUNT_CODE",new Variant(""));
        props.put("SUB_ACCOUNT_CODE",new Variant(""));
        props.put("ACCOUNT_NAME",new Variant(""));
        props.put("SH_NAME",new Variant(""));
        props.put("SH_CODE",new Variant(""));
        props.put("GROUP_CODE",new Variant(""));
        props.put("GROUP_NAME",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("BLOCKED",new Variant(false));
        props.put("IS_SUBSIDAIRY",new Variant(0));
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
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FIN_GL ORDER BY MAIN_ACCOUNT_CODE");
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
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FIN_GL_H WHERE MAIN_ACCOUNT_CODE='1' ");
            rsHistory.first();
            //------------------------------------//
            
            //** Validations **//
            if(!Validate())
            {
              return false;  
            }
            
            if(data.IsRecordExist("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_GL WHERE MAIN_ACCOUNT_CODE='"+getAttribute("MAIN_ACCOUNT_CODE").getString()+"'",FinanceGlobal.FinURL)) {
                LastError="Account with this main account code already exist.";
                return false;
            }
            //*****************//
            
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //Start Transaction
            //Conn.setAutoCommit(false);
            //data.SetAutoCommit(false);
            
            long NewAccountID=data.getMaxID("D_FIN_GL","ACCOUNT_ID",FinanceGlobal.FinURL);
            
            //--------- Generate New Internal Account ID  ------------
            setAttribute("ACCOUNT_ID",NewAccountID);
            //--------------------------------------------------------
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsResultSet.updateLong("ACCOUNT_ID",getAttribute("ACCOUNT_ID").getLong());
            rsResultSet.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsResultSet.updateString("SUB_ACCOUNT_CODE",getAttribute("SUB_ACCOUNT_CODE").getString());
            rsResultSet.updateString("ACCOUNT_NAME",getAttribute("ACCOUNT_NAME").getString());
            rsResultSet.updateString("SH_NAME",getAttribute("SH_NAME").getString());
            rsResultSet.updateString("SH_CODE",getAttribute("SH_CODE").getString());
            rsResultSet.updateString("GROUP_CODE",getAttribute("GROUP_CODE").getString());
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateInt("IS_SUBSIDAIRY",getAttribute("IS_SUBSIDAIRY").getInt());
            rsResultSet.updateBoolean("BLOCKED",getAttribute("BLOCKED").getBool());
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
            rsHistory.updateLong("ACCOUNT_ID",getAttribute("ACCOUNT_ID").getLong());
            rsHistory.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsHistory.updateString("SUB_ACCOUNT_CODE",getAttribute("SUB_ACCOUNT_CODE").getString());
            rsHistory.updateString("ACCOUNT_NAME",getAttribute("ACCOUNT_NAME").getString());
            rsHistory.updateString("SH_NAME",getAttribute("SH_NAME").getString());
            rsHistory.updateString("SH_CODE",getAttribute("SH_CODE").getString());
            rsHistory.updateString("GROUP_CODE",getAttribute("GROUP_CODE").getString());
            rsHistory.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateInt("IS_SUBSIDAIRY",getAttribute("IS_SUBSIDAIRY").getInt());
            rsHistory.updateBoolean("BLOCKED",getAttribute("BLOCKED").getBool());
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
            ObjFlow.DocNo=Long.toString(getAttribute("ACCOUNT_ID").getLong());
            ObjFlow.From=getAttribute("FROM").getInt();
            ObjFlow.To=getAttribute("TO").getInt();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_FIN_GL";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=getAttribute("HIERARCHY_ID").getInt();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName="ACCOUNT_ID";
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
            
            String theDocNo=Long.toString(getAttribute("ACCOUNT_ID").getLong());
            
            //** Validations **//
            if(!Validate())
            {
              return false;  
            }
            
            if(data.IsRecordExist("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_GL WHERE MAIN_ACCOUNT_CODE='"+getAttribute("MAIN_ACCOUNT_CODE").getString()+"' AND ACCOUNT_ID<>"+theDocNo,FinanceGlobal.FinURL)) {
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
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FIN_GL_H WHERE ACCOUNT_ID=1"); // '1' for restricting all data retrieval
            rsHistory.first();
            //** --------------------------------**//
            
            //Start Transaction
            //Conn.setAutoCommit(false);
            //data.SetAutoCommit(false);
            
            rsResultSet.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsResultSet.updateString("SUB_ACCOUNT_CODE",getAttribute("SUB_ACCOUNT_CODE").getString());
            rsResultSet.updateString("ACCOUNT_NAME",getAttribute("ACCOUNT_NAME").getString());
            rsResultSet.updateString("SH_NAME",getAttribute("SH_NAME").getString());
            rsResultSet.updateString("SH_CODE",getAttribute("SH_CODE").getString());
            rsResultSet.updateString("GROUP_CODE",getAttribute("GROUP_CODE").getString());
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateInt("IS_SUBSIDAIRY",getAttribute("IS_SUBSIDAIRY").getInt());
            rsResultSet.updateBoolean("BLOCKED",getAttribute("BLOCKED").getBool());
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
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM D_FIN_GL_H WHERE ACCOUNT_ID="+theDocNo,FinanceGlobal.FinURL);
            RevNo++;
            String RevDocNo=Long.toString(getAttribute("ACCOUNT_ID").getLong());
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo);
            rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            rsHistory.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsHistory.updateLong("ACCOUNT_ID",getAttribute("ACCOUNT_ID").getLong());
            rsHistory.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsHistory.updateString("SUB_ACCOUNT_CODE",getAttribute("SUB_ACCOUNT_CODE").getString());
            rsHistory.updateString("ACCOUNT_NAME",getAttribute("ACCOUNT_NAME").getString());
            rsHistory.updateString("SH_NAME",getAttribute("SH_NAME").getString());
            rsHistory.updateString("SH_CODE",getAttribute("SH_CODE").getString());
            rsHistory.updateString("GROUP_CODE",getAttribute("GROUP_CODE").getString());
            rsHistory.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateInt("IS_SUBSIDAIRY",getAttribute("IS_SUBSIDAIRY").getInt());
            rsHistory.updateBoolean("BLOCKED",getAttribute("BLOCKED").getBool());
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
            ObjFlow.DocNo=Long.toString(getAttribute("ACCOUNT_ID").getLong());
            ObjFlow.From=getAttribute("FROM").getInt();
            ObjFlow.To=getAttribute("TO").getInt();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_FIN_GL";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=getAttribute("HIERARCHY_ID").getInt();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName="ACCOUNT_ID";
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
                data.Execute("UPDATE D_FIN_GL SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE ACCOUNT_ID="+theDocNo,FinanceGlobal.FinURL);
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
    
    
    public boolean CanDelete(int CompanyID,int AccountID,int UserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            
            if(HistoryView) {
                return false;
            }
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FIN_GL WHERE ACCOUNT_ID="+AccountID+" AND (APPROVED=1)";
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
    public boolean IsEditable(int CompanyID,int AccountID,int UserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT ACCOUNT_ID FROM D_FIN_GL WHERE ACCOUNT_ID="+AccountID+" AND (APPROVED=1)";
            
            if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                return false;
            }
            else {
                strSQL="SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+AccountID+"' AND USER_ID="+UserID+" AND STATUS='W'";
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
            int AccountID=getAttribute("ACCOUNT_ID").getInt();
            
            if(CanDelete(EITLERPGLOBAL.gCompanyID,AccountID,UserID)) {
                String strSQL = "DELETE FROM D_FIN_GL WHERE ACCOUNT_ID="+AccountID;
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
    
    
    public Object getObject(int CompanyID, int AccountID) {
        String strCondition = " WHERE ACCOUNT_ID=" + AccountID;
        clsGL objGL = new clsGL();
        objGL.Filter(strCondition,CompanyID);
        return objGL;
    }
    
    
    
    
    public boolean Filter(String Condition,int CompanyID) {
        Ready=false;
        
        try {
            String strSQL = "SELECT * FROM D_FIN_GL " + Condition ;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSQL);
            
            if(!rsResultSet.first()) {
                strSQL = "SELECT * FROM D_FIN_GL ORDER BY MAIN_ACCOUNT_CODE ";
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
            setAttribute("ACCOUNT_ID",UtilFunctions.getInt(rsResultSet,"ACCOUNT_ID",0));
            setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsResultSet,"MAIN_ACCOUNT_CODE",""));
            setAttribute("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsResultSet,"SUB_ACCOUNT_CODE",""));
            setAttribute("ACCOUNT_NAME",UtilFunctions.getString(rsResultSet,"ACCOUNT_NAME",""));
            setAttribute("SH_NAME",UtilFunctions.getString(rsResultSet,"SH_NAME",""));
            setAttribute("SH_CODE",UtilFunctions.getString(rsResultSet,"SH_CODE",""));
            setAttribute("GROUP_CODE",UtilFunctions.getString(rsResultSet,"GROUP_CODE",""));
            setAttribute("REMARKS",UtilFunctions.getString(rsResultSet,"REMARKS",""));
            setAttribute("IS_SUBSIDAIRY",UtilFunctions.getInt(rsResultSet,"IS_SUBSIDAIRY",0));
            setAttribute("BLOCKED",UtilFunctions.getBoolean(rsResultSet,"BLOCKED",false));
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
                strSQL="SELECT FINANCE.D_FIN_GL.ACCOUNT_ID,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FIN_GL,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_GL.ACCOUNT_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            
            if(Order==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT FINANCE.D_FIN_GL.ACCOUNT_ID,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FIN_GL,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_GL.ACCOUNT_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_FIN_GL.ACCOUNT_ID";
            }
            
            if(Order==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT FINANCE.D_FIN_GL.ACCOUNT_ID,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FIN_GL,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_GL.PARTY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_FIN_GL.ACCOUNT_ID";
            }
            
            rsTemp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTemp.first();
            
            Counter=0;
            
            if(rsTemp.getRow()>0)
            {
            while(!rsTemp.isAfterLast()) {
                Counter=Counter+1;
                clsGL ObjDoc=new clsGL();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("ACCOUNT_ID",UtilFunctions.getInt(rsTemp,"ACCOUNT_ID",0));
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
    
    
    public boolean ShowHistory(int CompanyID,int AccountID) {
        Ready=false;
        try {
            String strSQL="SELECT * FROM D_FIN_GL_H WHERE ACCOUNT_ID="+AccountID;
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
    
    
    public static HashMap getHistoryList(int CompanyID,int AccountID) {
        HashMap List=new HashMap();
        ResultSet rsTmp;
        
        try {
            String strSQL="SELECT * FROM D_FIN_GL_H WHERE ACCOUNT_ID="+AccountID;
            rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0)
            {
            while(!rsTmp.isAfterLast()) {
                clsGL objAccount=new clsGL();
                
                objAccount.setAttribute("ACCOUNT_ID",UtilFunctions.getInt(rsTmp,"ACCOUNT_ID", 0));
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
    
    
    public static String getDocStatus(int CompanyID,int AccountID) {
        ResultSet rsTmp;
        String strMessage="";
        
        try {
            
            String strSQL="SELECT ACCOUNT_ID,APPROVED,CANCELLED FROM D_FIN_GL WHERE ACCOUNT_ID="+AccountID;
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
    
    
    public static boolean CanCancel(int CompanyID,int AccountID) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            rsTmp=data.getResult("SELECT ACCOUNT_ID,MAIN_ACCOUNT_CODE FROM D_FIN_GL WHERE ACCOUNT_ID="+AccountID+" AND CANCELLED=0",FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                
                String MainAccountCode=UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","");
                
                if(data.IsRecordExist("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE CANCELLED=0 AND COMPANY_ID="+CompanyID+" AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"'",FinanceGlobal.FinURL)) {
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

    
    
    public static boolean CancelDoc(int CompanyID,int AccountID) {
        
        ResultSet rsTmp=null;
        boolean Cancelled=false;
        
        try {
            if(CanCancel(CompanyID,AccountID)) {
                
                boolean ApprovedDoc=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_FIN_GL WHERE ACCOUNT_ID="+AccountID,FinanceGlobal.FinURL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedDoc) {
                    
                }
                else {
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+AccountID+"' AND MODULE_ID="+ModuleID,FinanceGlobal.FinURL);
                }
                
                data.Execute("UPDATE D_FIN_GL SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE ACCOUNT_ID="+AccountID,FinanceGlobal.FinURL);
                
                Cancelled=true;
            }
            
            rsTmp.close();
        }
        catch(Exception e) {
            
        }
        
        return Cancelled;
    }
    
    public static String getAccountName(int CompanyID,String MainCode) {
        try {
            return data.getStringValueFromDB("SELECT ACCOUNT_NAME FROM D_FIN_GL WHERE MAIN_ACCOUNT_CODE='"+MainCode+"'",FinanceGlobal.FinURL);
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    private boolean Validate() {
        //** Validations **//
        if(getAttribute("MAIN_ACCOUNT_CODE").getString().equals("")) {
            LastError="Please specify Main Account Code";
            return false;
        }
        
        
        if(getAttribute("ACCOUNT_NAME").getString().equals("")) {
            LastError="Please specify Account Name";
            return false;
        }
        
        if(getAttribute("SH_CODE").getString().equals("")) {
            LastError="Please specify schedule code";
            return false;
        }
        
        if(getAttribute("GROUP_CODE").getString().equals("")) {
            LastError="Please specify group code";
            return false;
        }
        
        if(!data.IsRecordExist("SELECT * FROM D_FIN_GROUP_CODE_MASTER WHERE GROUP_CODE='"+getAttribute("GROUP_CODE").getString()+"' ",FinanceGlobal.FinURL)) {
            LastError="Please specify valid group code";
            return false;
        }
        //*****************//
        
        return true;
    }
}
