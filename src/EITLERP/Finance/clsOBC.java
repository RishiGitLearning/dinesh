package EITLERP.Finance;

import java.util.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import EITLERP.Sales.*;


/**
 *
 * @author  nrpithva
 * @version
 */

public class clsOBC{
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
    private boolean HistoryView=false;
    
    public static int ModuleID=91;
    
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
    public clsOBC() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("BANK_REFERENCE_NO",new Variant(""));
        props.put("BANK_REFERENCE_DATE",new Variant(""));
        props.put("DRAFT_NO",new Variant(""));
        props.put("DRAFT_DATE",new Variant(""));
        props.put("MAIN_CODE",new Variant(""));
        props.put("PARTY_CODE",new Variant(""));
        props.put("BOOK_CODE",new Variant(""));
        props.put("AGENT_ALPHA",new Variant(""));
        props.put("AMOUNT",new Variant(0));
        
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("REJECTED_REMARKS", new Variant(""));
        props.put("CANCELLED", new Variant(false));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("PREFIX",new Variant("")); //For Autonumber generation
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        
        props.put("INTEREST_AMOUNT",new Variant(0));
        props.put("PAYMENT_RECEIVED_DATE",new Variant(""));
        
    }
    
    /**Load Data. This method loads data from database to Business Object**/
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FIN_OBC ORDER BY DOC_NO");
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
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FIN_OBC_H WHERE DOC_NO='1' ");
            rsHistory.first();
            //------------------------------------//
            
            //** Validations **//
            if(!Validate()) {
                return false;
            }
            //*****************//
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //Start Transaction
            //Conn.setAutoCommit(false);
            //data.SetAutoCommit(false);
            
            setAttribute("DOC_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,clsOBC.ModuleID,(int)getAttribute("FFNO").getVal(),true));
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsResultSet.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsResultSet.updateString("DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsResultSet.updateString("BANK_REFERENCE_NO",getAttribute("BANK_REFERENCE_NO").getString());
            rsResultSet.updateString("BANK_REFERENCE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("BANK_REFERENCE_DATE").getString()));
            rsResultSet.updateString("DRAFT_NO",getAttribute("DRAFT_NO").getString());
            rsResultSet.updateString("DRAFT_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DRAFT_DATE").getString()));
            rsResultSet.updateString("MAIN_CODE",getAttribute("MAIN_CODE").getString());
            rsResultSet.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsResultSet.updateString("BOOK_CODE",getAttribute("BOOK_CODE").getString());
            rsResultSet.updateString("AGENT_ALPHA",getAttribute("AGENT_ALPHA").getString());
            rsResultSet.updateDouble("AMOUNT",getAttribute("AMOUNT").getDouble());
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
            rsResultSet.updateDouble("INTEREST_AMOUNT",getAttribute("INTEREST_AMOUNT").getDouble());
            rsResultSet.updateString("PAYMENT_RECEIVED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("PAYMENT_RECEIVED_DATE").getString()));
            rsResultSet.insertRow();
            
            
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
            rsHistory.updateString("BANK_REFERENCE_NO",getAttribute("BANK_REFERENCE_NO").getString());
            rsHistory.updateString("BANK_REFERENCE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("BANK_REFERENCE_DATE").getString()));
            rsHistory.updateString("DRAFT_NO",getAttribute("DRAFT_NO").getString());
            rsHistory.updateString("DRAFT_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DRAFT_DATE").getString()));
            rsHistory.updateString("MAIN_CODE",getAttribute("MAIN_CODE").getString());
            rsHistory.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsHistory.updateString("BOOK_CODE",getAttribute("BOOK_CODE").getString());
            rsHistory.updateString("AGENT_ALPHA",getAttribute("AGENT_ALPHA").getString());
            rsHistory.updateDouble("AMOUNT",getAttribute("AMOUNT").getDouble());
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
            rsHistory.updateDouble("INTEREST_AMOUNT",getAttribute("INTEREST_AMOUNT").getDouble());
            rsHistory.updateString("PAYMENT_RECEIVED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("PAYMENT_RECEIVED_DATE").getString()));
            rsHistory.insertRow();
            
            
            //======== Update the Approval Flow =========//
            ObjFlow.CompanyID=EITLERPGLOBAL.gCompanyID;
            ObjFlow.ModuleID=clsOBC.ModuleID;
            ObjFlow.DocNo=getAttribute("DOC_NO").getString();
            ObjFlow.From=getAttribute("FROM").getInt();
            ObjFlow.To=getAttribute("TO").getInt();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_FIN_OBC";
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
            
            
            if(ObjFlow.Status.equals("F")) {
                PostReceiptVoucher(EITLERPGLOBAL.gCompanyID, getAttribute("BANK_REFERENCE_NO").getString(),EITLERPGLOBAL.formatDateDB(getAttribute("BANK_REFERENCE_DATE").getString()),getAttribute("DOC_NO").getString());
            }
            
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
            
            String theDocNo=getAttribute("DOC_NO").getString();
            
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
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FIN_OBC_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //** --------------------------------**//
            
            //Start Transaction
            //Conn.setAutoCommit(false);
            //data.SetAutoCommit(false);
            
            
            rsResultSet.updateString("DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsResultSet.updateString("BANK_REFERENCE_NO",getAttribute("BANK_REFERENCE_NO").getString());
            rsResultSet.updateString("BANK_REFERENCE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("BANK_REFERENCE_DATE").getString()));
            rsResultSet.updateString("DRAFT_NO",getAttribute("DRAFT_NO").getString());
            rsResultSet.updateString("DRAFT_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DRAFT_DATE").getString()));
            rsResultSet.updateString("MAIN_CODE",getAttribute("MAIN_CODE").getString());
            rsResultSet.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsResultSet.updateString("BOOK_CODE",getAttribute("BOOK_CODE").getString());
            rsResultSet.updateString("AGENT_ALPHA",getAttribute("AGENT_ALPHA").getString());
            rsResultSet.updateDouble("AMOUNT",getAttribute("AMOUNT").getDouble());
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
            rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateDouble("INTEREST_AMOUNT",getAttribute("INTEREST_AMOUNT").getDouble());
            rsResultSet.updateString("PAYMENT_RECEIVED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("PAYMENT_RECEIVED_DATE").getString()));
            
            rsResultSet.updateRow();
            
            //========= Inserting Into History =================//
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM D_FIN_OBC_H WHERE DOC_NO='"+theDocNo+"'",FinanceGlobal.FinURL);
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
            rsHistory.updateString("BANK_REFERENCE_NO",getAttribute("BANK_REFERENCE_NO").getString());
            rsHistory.updateString("BANK_REFERENCE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("BANK_REFERENCE_DATE").getString()));
            rsHistory.updateString("DRAFT_NO",getAttribute("DRAFT_NO").getString());
            rsHistory.updateString("DRAFT_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DRAFT_DATE").getString()));
            rsHistory.updateString("MAIN_CODE",getAttribute("MAIN_CODE").getString());
            rsHistory.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsHistory.updateString("BOOK_CODE",getAttribute("BOOK_CODE").getString());
            rsHistory.updateString("AGENT_ALPHA",getAttribute("AGENT_ALPHA").getString());
            rsHistory.updateDouble("AMOUNT",getAttribute("AMOUNT").getDouble());
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
            rsHistory.updateDouble("INTEREST_AMOUNT",getAttribute("INTEREST_AMOUNT").getDouble());
            rsHistory.updateString("PAYMENT_RECEIVED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("PAYMENT_RECEIVED_DATE").getString()));
            
            rsHistory.insertRow();
            
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=EITLERPGLOBAL.gCompanyID;
            ObjFlow.ModuleID=clsOBC.ModuleID;
            ObjFlow.DocNo=getAttribute("DOC_NO").getString();
            ObjFlow.From=getAttribute("FROM").getInt();
            ObjFlow.To=getAttribute("TO").getInt();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_FIN_OBC";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=getAttribute("HIERARCHY_ID").getInt();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName="DOC_NO";
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
                data.Execute("UPDATE D_FIN_OBC SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='"+theDocNo+"' ",FinanceGlobal.FinURL);
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
            
            
            if(ObjFlow.Status.equals("F")) {
                PostReceiptVoucher(EITLERPGLOBAL.gCompanyID, getAttribute("BANK_REFERENCE_NO").getString(),EITLERPGLOBAL.formatDateDB(getAttribute("BANK_REFERENCE_DATE").getString()),getAttribute("DOC_NO").getString());
            }
            
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
    
    
    public boolean CanDelete(int CompanyID,String DocNo,int UserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            
            if(HistoryView) {
                return false;
            }
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FIN_OBC WHERE DOC_NO='"+DocNo+"' AND (APPROVED=1)";
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
            strSQL="SELECT DOC_NO FROM D_FIN_OBC WHERE DOC_NO='"+DocNo+"' AND (APPROVED=1)";
            
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
            String DocNo=getAttribute("DOC_NO").getString();
            
            if(CanDelete(EITLERPGLOBAL.gCompanyID,DocNo,UserID)) {
                String strSQL = "DELETE FROM D_FIN_OBC WHERE DOC_NO='"+DocNo+"' ";
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
        String strCondition = " WHERE DOC_NO='" + DocNo+"' ";
        clsOBC objOBC = new clsOBC();
        objOBC.Filter(strCondition,CompanyID);
        return objOBC;
    }
    
    
    public boolean Filter(String Condition,int CompanyID) {
        Ready=false;
        
        try {
            
            String strSQL = "SELECT * FROM D_FIN_OBC " + Condition ;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSQL);
            
            if(!rsResultSet.first()) {
                strSQL = "SELECT * FROM D_FIN_OBC ORDER BY DOC_NO ";
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
            setAttribute("DOC_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"DOC_DATE","0000-00-00")));
            setAttribute("BANK_REFERENCE_NO",UtilFunctions.getString(rsResultSet,"BANK_REFERENCE_NO",""));
            setAttribute("BANK_REFERENCE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"BANK_REFERENCE_DATE","0000-00-00")));
            setAttribute("DRAFT_NO",UtilFunctions.getString(rsResultSet,"DRAFT_NO",""));
            setAttribute("DRAFT_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"DRAFT_DATE","0000-00-00")));
            setAttribute("MAIN_CODE",UtilFunctions.getString(rsResultSet,"MAIN_CODE",""));
            setAttribute("PARTY_CODE",UtilFunctions.getString(rsResultSet,"PARTY_CODE",""));
            setAttribute("BOOK_CODE",UtilFunctions.getString(rsResultSet,"BOOK_CODE",""));
            setAttribute("AGENT_ALPHA",UtilFunctions.getString(rsResultSet,"AGENT_ALPHA",""));
            setAttribute("AMOUNT",UtilFunctions.getDouble(rsResultSet,"AMOUNT",0));
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
            setAttribute("INTEREST_AMOUNT",UtilFunctions.getDouble(rsResultSet,"INTEREST_AMOUNT",0));
            setAttribute("PAYMENT_RECEIVED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"PAYMENT_RECEIVED_DATE","0000-00-00")));
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
        
    }
    
    
    
    public static HashMap getPendingApprovals(int CompanyID,int UserID,int Order,int FinYearFrom) {
        ResultSet rsTemp=null;
        String strSQL="";
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            
            if(Order==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT FINANCE.D_FIN_OBC.DOC_DATE,FINANCE.D_FIN_OBC.DOC_NO,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FIN_OBC,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_OBC.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT FINANCE.D_FIN_OBC.DOC_DATE,FINANCE.D_FIN_OBC.DOC_NO,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FIN_OBC,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_OBC.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_FIN_OBC.DOC_NO";
            }
            
            if(Order==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT FINANCE.D_FIN_OBC.DOC_DATE,FINANCE.D_FIN_OBC.DOC_NO,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FIN_OBC,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_OBC.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_FIN_OBC.DOC_NO";
            }
            
            rsTemp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTemp.first();
            
            Counter=0;
            
            if(rsTemp.getRow()>0) {
                while(!rsTemp.isAfterLast()) {
                    if(EITLERPGLOBAL.isWithinDate(rsTemp.getString("DOC_DATE"),FinYearFrom)) {
                        Counter=Counter+1;
                        clsOBC ObjDoc=new clsOBC();
                        
                        //------------- Header Fields --------------------//
                        ObjDoc.setAttribute("DOC_NO",UtilFunctions.getString(rsTemp,"DOC_NO",""));
                        ObjDoc.setAttribute("DOC_DATE",UtilFunctions.getString(rsTemp,"DOC_DATE","0000-00-00"));
                        ObjDoc.setAttribute("RECEIVED_DATE",UtilFunctions.getString(rsTemp,"RECEIVED_DATE","0000-00-00"));
                        // ----------------- End of Header Fields ------------------------------------//
                         String PartyCode=data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FIN_OBC WHERE DOC_NO='"+rsTemp.getString("DOC_NO")+"' AND DOC_DATE='"+rsTemp.getString("DOC_DATE")+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID,FinanceGlobal.FinURL);
                         String MainCode =data.getStringValueFromDB("SELECT MAIN_CODE FROM D_FIN_OBC WHERE DOC_NO='"+rsTemp.getString("DOC_NO")+"' AND DOC_DATE='"+rsTemp.getString("DOC_DATE")+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID,FinanceGlobal.FinURL);
                         
                        String PartyName=clsAccount.getAccountName(MainCode,PartyCode); 
                         
                         ObjDoc.setAttribute("PARTY_CODE",PartyCode);
                          ObjDoc.setAttribute("PARTY_NAME",PartyName);
                         List.put(Long.toString(Counter),ObjDoc);
                    }
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
            String strSQL="SELECT * FROM D_FIN_OBC_H WHERE DOC_NO='"+DocNo+"' ";
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
            String strSQL="SELECT * FROM D_FIN_OBC_H WHERE DOC_NO='"+DocNo+"' ";
            rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsOBC objAccount=new clsOBC();
                    
                    objAccount.setAttribute("DOC_NO",UtilFunctions.getString(rsTmp,"DOC_NO", ""));
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
    
    
    public static String getDocStatus(int CompanyID,String DocNo) {
        ResultSet rsTmp;
        String strMessage="";
        
        try {
            
            String strSQL="SELECT DOC_NO,APPROVED,CANCELLED FROM D_FIN_OBC WHERE DOC_NO='"+DocNo+"' ";
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
            //rsTmp=data.getResult("SELECT DOC_NO,MAIN_ACCOUNT_CODE FROM D_FIN_OBC WHERE DOC_NO='"+DocNo+"' AND CANCELLED=0",FinanceGlobal.FinURL);
            rsTmp=data.getResult("SELECT DOC_NO,MAIN_CODE FROM D_FIN_OBC WHERE DOC_NO='"+DocNo+"' AND CANCELLED=0",FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                
                if(data.IsRecordExist("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE CANCELLED=0 AND COMPANY_ID="+CompanyID+" AND GRN_NO='"+DocNo+"' AND MODULE_ID="+ModuleID+" " ,FinanceGlobal.FinURL)) {
                    canCancel=false;
                }
                else {
                    canCancel=true;
                }
            }
            
            rsTmp.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return canCancel;
    }
    
    
    
    public static boolean CancelDoc(int CompanyID,String DocNo) {
        
        ResultSet rsTmp=null;
        boolean Cancelled=false;
        
        try {
            if(CanCancel(CompanyID,DocNo)) {
                
                boolean ApprovedDoc=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_FIN_OBC WHERE DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedDoc) {
                    
                }
                else {
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"' AND MODULE_ID="+ModuleID,FinanceGlobal.FinURL);
                }
                
                data.Execute("UPDATE D_FIN_OBC SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
                
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
        if(getAttribute("MAIN_CODE").getString().equals("")) {
            LastError="Please specify Main Account Code";
            return false;
        }
        
        if(getAttribute("PARTY_CODE").getString().equals("")) {
            LastError="Please specify Party Code";
            return false;
        }
        
        if(!clsAccount.IsValidAccount(getAttribute("MAIN_CODE").getString(), getAttribute("PARTY_CODE").getString())) {
            LastError="Account code you specified is not valid. Please check and try again";
            return false;
        }
        
        if(getAttribute("AMOUNT").getDouble()<=0) {
            LastError="Please enter amount deposited by the party";
            return false;
        }
        
        //*****************//
        
        return true;
    }
    
    public boolean PostReceiptVoucher(int CompanyID,String BankRefNo, String BankRefDate,String DocNo) {
        try {
            
            ResultSet rsInvoice,rsOBCReceipt;
            double TotalCredit=0;
            
            rsOBCReceipt=data.getResult("SELECT * FROM D_FIN_OBC WHERE BANK_REFERENCE_NO='"+BankRefNo+"' AND BANK_REFERENCE_DATE='"+BankRefDate+"' AND DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
            rsOBCReceipt.first();
            
            if(rsOBCReceipt.getRow()>0) {
                
            } else {
                //This procedure should exit
                return false;
            }
            
            //Get Object
            clsVoucher objVoucher=new clsVoucher();
            
            //****** Prepare Voucher Object ********//
            
            String SelPrefix="";
            String SelSuffix="";
            int FFNo=0;
            
            setAttribute("FIN_HIERARCHY_ID",0);
            
            //(1) Select the Hierarchy
            HashMap List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsOBC.ModuleID, "CHOOSE_HIERARCHY", "DEFAULT", "1");
            // Hierarchy ID -- 974 
            if(List.size()>0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                int HierarchyID=UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                setAttribute("FIN_HIERARCHY_ID",HierarchyID);
            }
            
            
            ResultSet rsVoucher=data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID="+clsVoucher.ReceiptVoucherModuleID);
            rsVoucher.first();
            
            if(rsVoucher.getRow()>0) {
                SelPrefix=rsVoucher.getString("PREFIX_CHARS");
                SelSuffix=rsVoucher.getString("SUFFIX_CHARS");
                FFNo=rsVoucher.getInt("FIRSTFREE_NO");
            }
            
            String BookCode = UtilFunctions.getString(rsOBCReceipt,"BOOK_CODE","");
            int VoucherSrNo=0;
            
            objVoucher=new clsVoucher();
            objVoucher.LoadDataEx(EITLERPGLOBAL.gCompanyID);
            
            objVoucher.setAttribute("PREFIX",SelPrefix);
            objVoucher.setAttribute("SUFFIX",SelSuffix);
            objVoucher.setAttribute("FFNO",FFNo);
            objVoucher.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            objVoucher.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsOBCReceipt,"DOC_DATE","0000-00-00")));
            objVoucher.setAttribute("BOOK_CODE",BookCode);
            objVoucher.setAttribute("VOUCHER_TYPE",FinanceGlobal.TYPE_RECEIPT);
            objVoucher.setAttribute("CHEQUE_NO","ADVICE");
            objVoucher.setAttribute("CHEQUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsOBCReceipt,"DOC_DATE","0000-00-00")));
            objVoucher.setAttribute("PAYMENT_MODE",3);
            objVoucher.setAttribute("BANK_NAME","");
            objVoucher.setAttribute("PO_NO","");
            objVoucher.setAttribute("PO_DATE","0000-00-00");
            objVoucher.setAttribute("INVOICE_NO","");
            objVoucher.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucher.setAttribute("GRN_NO","");
            objVoucher.setAttribute("GRN_DATE","0000-00-00");
            objVoucher.setAttribute("ST_CATEGORY","");
            objVoucher.setAttribute("MODULE_ID",clsOBC.ModuleID);
            objVoucher.setAttribute("REMARKS","");
            
            objVoucher.setAttribute("HIERARCHY_ID",getAttribute("FIN_HIERARCHY_ID").getInt());
            int FirstUserID=data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID="+getAttribute("FIN_HIERARCHY_ID").getInt()+" AND SR_NO=1");
            objVoucher.setAttribute("FROM",FirstUserID);
            objVoucher.setAttribute("TO",FirstUserID);
            objVoucher.setAttribute("FROM_REMARKS","");
            objVoucher.setAttribute("APPROVAL_STATUS","F"); //Hold Voucher
            
            objVoucher.colVoucherItems.clear();
            
            String BankCode=clsBook.getBookMainCode(CompanyID, objVoucher.getAttribute("BOOK_CODE").getString());
            
            
            rsInvoice=data.getResult("SELECT A.DOC_NO,A.MAIN_ACCOUNT_CODE,A.PARTY_CODE,B.* FROM D_FIN_OBC_INVOICE_HEADER A,D_FIN_OBC_INVOICE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.DOC_NO=B.DOC_NO AND A.BANK_REFERENCE_NO='"+BankRefNo+"' AND A.BANK_REFERENCE_DATE='"+BankRefDate+"' AND A.APPROVED=1 AND A.CANCELLED=0 ",FinanceGlobal.FinURL);
            rsInvoice.first();
            String InvoiceNo= "";
            String InvoiceDate="";
            double InvoiceAmount=0;
            double DepositedAmount = 0;
            if(rsInvoice.getRow()>0) {
                while(!rsInvoice.isAfterLast()) {
                    
                    InvoiceNo=UtilFunctions.getString(rsInvoice,"INVOICE_NO","");
                    InvoiceDate=UtilFunctions.getString(rsInvoice,"INVOICE_DATE","0000-00-00");
                    InvoiceAmount=clsSalesInvoice.getInvoiceTotal(EITLERPGLOBAL.gCompanyID, InvoiceNo,InvoiceDate);
                    // change By Mrugesh Date: 05/12/2012  Start
                    
                    DepositedAmount = UtilFunctions.getDouble(rsOBCReceipt,"AMOUNT",0);
                    //DepositedAmount = UtilFunctions.getDouble(rsInvoice,"INVOICE_AMOUNT",0);
                    
                    // change By Mrugesh Date: 05/12/2012 End
                    TotalCredit+=DepositedAmount;
                    
                    VoucherSrNo++;
                    clsVoucherItem objVoucherItem=new clsVoucherItem();
                    objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                    objVoucherItem.setAttribute("EFFECT","C");
                    objVoucherItem.setAttribute("ACCOUNT_ID",1);
                    objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsInvoice,"MAIN_ACCOUNT_CODE",""));
                    objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsInvoice,"PARTY_CODE",""));
                    objVoucherItem.setAttribute("AMOUNT",DepositedAmount);
                    objVoucherItem.setAttribute("REMARKS","");
                    objVoucherItem.setAttribute("PO_NO","");
                    objVoucherItem.setAttribute("PO_DATE","0000-00-00");
                    objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
                    objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsInvoice,"INVOICE_DATE","0000-00-00")));
                    objVoucherItem.setAttribute("INVOICE_AMOUNT",InvoiceAmount);
                    objVoucherItem.setAttribute("GRN_NO",UtilFunctions.getString(rsOBCReceipt,"DOC_NO",""));
                    objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsOBCReceipt,"DOC_DATE","0000-00-00")));
                    objVoucherItem.setAttribute("MODULE_ID",clsOBC.ModuleID);
                    objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
                    objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
                    
                    rsInvoice.next();
                }
            }
            
            double InterestAmount=UtilFunctions.getDouble(rsOBCReceipt,"INTEREST_AMOUNT",0);
            TotalCredit+=InterestAmount;
            String InterestMainCode = "";
            int InvoiceType = clsSalesInvoice.getInvoiceType(InvoiceNo, InvoiceDate);
            
            List.clear();
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsOBC.ModuleID, "GET_INTEREST_MAIN_CODE", "DEFAULT", Integer.toString(InvoiceType));
            // Hierarchy ID -- 974
            if(List.size()>0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                InterestMainCode=objRule.getAttribute("RULE_OUTCOME").getString();
            }
            if(InterestAmount>0) {
                VoucherSrNo++;
                clsVoucherItem objVoucherItem=new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT","C");
                objVoucherItem.setAttribute("ACCOUNT_ID",1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",InterestMainCode );
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
                objVoucherItem.setAttribute("AMOUNT",InterestAmount);
                objVoucherItem.setAttribute("REMARKS","");
                objVoucherItem.setAttribute("PO_NO","");
                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsInvoice,"INVOICE_DATE","0000-00-00")));
                objVoucherItem.setAttribute("INVOICE_AMOUNT",InvoiceAmount);
                objVoucherItem.setAttribute("GRN_NO",UtilFunctions.getString(rsOBCReceipt,"DOC_NO",""));
                objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsOBCReceipt,"DOC_DATE","0000-00-00")));
                objVoucherItem.setAttribute("MODULE_ID",clsOBC.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            }
            
            
            //Debit to bank a/c
            VoucherSrNo++;
            clsVoucherItem objVoucherItem=new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT","D");
            objVoucherItem.setAttribute("ACCOUNT_ID",1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_BOOK_MASTER WHERE BOOK_CODE='"+BookCode+"' ",FinanceGlobal.FinURL));
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
            objVoucherItem.setAttribute("AMOUNT",TotalCredit);
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
            objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsInvoice,"INVOICE_DATE","0000-00-00")));
            objVoucherItem.setAttribute("INVOICE_AMOUNT",InvoiceAmount);
            objVoucherItem.setAttribute("GRN_NO",UtilFunctions.getString(rsOBCReceipt,"DOC_NO",""));
            objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsOBCReceipt,"DOC_DATE","0000-00-00")));
            objVoucherItem.setAttribute("MODULE_ID",clsOBC.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            objVoucher.DoNotValidateAccounts=true;
            if(objVoucher.Insert()) {
                return true;
            } else {
                LastError=objVoucher.LastError;
                return false;
            }
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
