/*
 * clsMaterialRequisition.java
 *
 * Created on April 19, 2004, 2:35 PM
 */

package EITLERP.Finance;

import java.util.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import EITLERP.Stores.*;
import EITLERP.Sales.*;


/**
 *
 * @author  Nitin Pithva
 * @version
 */

public class clsOBCInvoice {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    //Requisition Material Collection
    public HashMap colInvoiceItems=new HashMap();
    
    //History Related properties
    private boolean HistoryView=false;
    
    public static int ModuleID=93;
    
    
    public Variant getAttribute(String PropName) {
        if(!props.containsKey(PropName)) {
            return (new Variant(""));
        }
        else {
            return (Variant) props.get(PropName);
        }
    }
    
    public void setAttribute(String PropName,String Value) {
        props.put(PropName,new Variant(Value));
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
    public clsOBCInvoice() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(0));
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("BANK_REFERENCE_NO",new Variant(""));
        props.put("BANK_REFERENCE_DATE",new Variant(""));
        props.put("MAIN_ACCOUNT_CODE",new Variant(""));
        props.put("PARTY_CODE",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVED",new Variant(0));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("REJECTED", new Variant(""));
        props.put("REJECTED_DATE",new Variant(""));
        props.put("REJECTED_REMARKS",new Variant(""));
        props.put("MODULE_ID",new Variant(0));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("PREFIX",new Variant("")); //For Autonumber generation
        
        colInvoiceItems=new HashMap();
        
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
            rsResultSet=Stmt.executeQuery("SELECT * FROM " +
            "D_FIN_OBC_INVOICE_HEADER WHERE COMPANY_ID="+pCompanyID+" AND DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY DOC_NO");
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
        
        Statement stHistory,stHDetail,stHeader;
        ResultSet rsHistory,rsHDetail,rsHeader;
        
        try {
            
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date ReqDate=java.sql.Date.valueOf(EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            
            if((ReqDate.after(FinFromDate)||ReqDate.compareTo(FinFromDate)==0)&&(ReqDate.before(FinToDate)||ReqDate.compareTo(FinToDate)==0)) {
                //Within the year
            }
            else {
                LastError="Document date is not within financial year.";
                //return false;
            }
            //=====================================================//
            
            
            
            if(!Validate()) {
                return false;
            }
            
            //--------- Generate Gatepass requisition no.  ------------
            setAttribute("DOC_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,clsOBCInvoice.ModuleID, (int)getAttribute("FFNO").getVal(),true));
            //-------------------------------------------------

            
            //data.SetAutoCommit(false);
            //Conn.setAutoCommit(false);
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FIN_OBC_INVOICE_HEADER_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FIN_OBC_INVOICE_DETAIL_H WHERE DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getLong());
            rsResultSet.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsResultSet.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsResultSet.updateString("BANK_REFERENCE_NO",getAttribute("BANK_REFERENCE_NO").getString());
            rsResultSet.updateString("BANK_REFERENCE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("BANK_REFERENCE_DATE").getString()));
            rsResultSet.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsResultSet.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsResultSet.updateString("REASON_CODE","");
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID );
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.insertRow();
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID );
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            rsHistory.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getLong());
            rsHistory.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsHistory.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsHistory.updateString("BANK_REFERENCE_NO",getAttribute("BANK_REFERENCE_NO").getString());
            rsHistory.updateString("BANK_REFERENCE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("BANK_REFERENCE_DATE").getString()));
            rsHistory.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsHistory.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsHistory.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateString("REASON_CODE","");
            rsHistory.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID );
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",true);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            
            ResultSet rsTmp;
            Statement tmpStmt;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FIN_OBC_INVOICE_DETAIL WHERE DOC_NO='1'");
            
            int CompanyID=getAttribute("COMPANY_ID").getInt();
            
            //Now Insert records into detail table
            for(int i=1;i<=colInvoiceItems.size();i++) {
                clsOBCInvoiceItem objItem=(clsOBCInvoiceItem) colInvoiceItems.get(Integer.toString(i));
                
                
                rsTmp.moveToInsertRow();
                rsTmp.updateInt("COMPANY_ID",CompanyID);
                rsTmp.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsTmp.updateInt("SR_NO",i);
                rsTmp.updateString("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                rsTmp.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("INVOICE_DATE").getString()));
                rsTmp.updateDouble("INVOICE_AMOUNT",objItem.getAttribute("INVOICE_AMOUNT").getDouble());
                rsTmp.updateString("REMARKS",objItem.getAttribute("REMARKS").getString());
                rsTmp.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsTmp.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsTmp.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateInt("CANCELLED",0);
                rsTmp.insertRow();
                
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateInt("COMPANY_ID",CompanyID);
                rsHDetail.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsHDetail.updateInt("SR_NO",i);
                rsHDetail.updateString("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                rsHDetail.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("INVOICE_DATE").getString()));
                rsHDetail.updateDouble("INVOICE_AMOUNT",objItem.getAttribute("INVOICE_AMOUNT").getDouble());
                rsHDetail.updateString("REMARKS",objItem.getAttribute("REMARKS").getString());
                rsHDetail.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsHDetail.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsHDetail.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateInt("CANCELLED",0);
                rsHDetail.insertRow();
                
            }
            
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=getAttribute("COMPANY_ID").getInt();
            ObjFlow.ModuleID=clsOBCInvoice.ModuleID;
            ObjFlow.DocNo=getAttribute("DOC_NO").getString();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_FIN_OBC_INVOICE_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
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
            ObjFlow.UseSpecifiedURL=false; //Turn it off due to static nature
            //--------- Approval Flow Update complete -----------
            
            
            //Conn.commit();
            //data.SetCommit();
            
            //Conn.setAutoCommit(true);
            //data.SetAutoCommit(true);
            
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            
            try {
                e.printStackTrace();
                //Conn.rollback();
                //data.SetRollback();
                
                //Conn.setAutoCommit(true);
                //data.SetAutoCommit(true);
            }
            catch(Exception c) {
                
            }
            LastError= e.getMessage();
            return false;
        }
    }
    
    
    
    //Updates current record
    public boolean Update() {
        Statement stHistory,stHDetail,stHeader;
        ResultSet rsHistory,rsHDetail,rsHeader,rsTmp;
        boolean Validate=true;
        int OldHierarchy=0;
        
        
        try {
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FIN_OBC_INVOICE_HEADER_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FIN_OBC_INVOICE_DETAIL_H WHERE DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            if(!Validate()) {
                return false;
            }
            
            rsResultSet.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsResultSet.updateString("BANK_REFERENCE_NO",getAttribute("BANK_REFERENCE_NO").getString());
            rsResultSet.updateString("BANK_REFERENCE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("BANK_REFERENCE_DATE").getString()));
            rsResultSet.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsResultSet.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateString("REASON_CODE","");
            rsResultSet.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID );
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateRow();
            
            
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM D_FIN_OBC_INVOICE_HEADER_H WHERE COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+" AND DOC_NO='"+getAttribute("DOC_NO").getString()+"'",FinanceGlobal.FinURL);
            RevNo++;
            String RevDocNo=getAttribute("DOC_NO").getString();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getLong());
            rsHistory.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsHistory.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsHistory.updateString("BANK_REFERENCE_NO",getAttribute("BANK_REFERENCE_NO").getString());
            rsHistory.updateString("BANK_REFERENCE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("BANK_REFERENCE_DATE").getString()));
            rsHistory.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsHistory.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsHistory.updateString("REASON_CODE","");
            rsHistory.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID );
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            
            Statement tmpStmt;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FIN_OBC_INVOICE_DETAIL WHERE DOC_NO='1'");
            
            data.Execute("DELETE FROM D_FIN_OBC_INVOICE_DETAIL WHERE COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+" AND DOC_NO='"+getAttribute("DOC_NO").getString()+"'",FinanceGlobal.FinURL);
            
            //Now Insert records into detail table
            for(int i=1;i<=colInvoiceItems.size();i++) {
                clsOBCInvoiceItem objItem=(clsOBCInvoiceItem) colInvoiceItems.get(Integer.toString(i));
                
                
                rsTmp.moveToInsertRow();
                rsTmp.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                rsTmp.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsTmp.updateInt("SR_NO",objItem.getAttribute("SR_NO").getInt());
                rsTmp.updateString("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                rsTmp.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("INVOICE_DATE").getString()));
                rsTmp.updateDouble("INVOICE_AMOUNT",objItem.getAttribute("INVOICE_AMOUNT").getDouble());
                rsTmp.updateString("REMARKS",objItem.getAttribute("REMARKS").getString());
                rsTmp.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsTmp.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsTmp.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateInt("CANCELLED",0);
                rsTmp.insertRow();
                
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                rsHDetail.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsHDetail.updateInt("SR_NO",objItem.getAttribute("SR_NO").getInt());
                rsHDetail.updateString("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                rsHDetail.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("INVOICE_DATE").getString()));
                rsHDetail.updateDouble("INVOICE_AMOUNT",objItem.getAttribute("INVOICE_AMOUNT").getDouble());
                rsHDetail.updateString("REMARKS",objItem.getAttribute("REMARKS").getString());
                rsHDetail.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsHDetail.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsHDetail.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateInt("CANCELLED",0);
                rsHDetail.insertRow();
                
            }
            
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FIN_OBC_INVOICE_DETAIL WHERE DOC_NO='1'");
            
            int Counter=0;
            
            
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            String AStatus=getAttribute("APPROVAL_STATUS").getString();
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=EITLERPGLOBAL.gCompanyID;
            ObjFlow.DocNo=getAttribute("DOC_NO").getString();
            ObjFlow.ModuleID=clsOBCInvoice.ModuleID;
            ObjFlow.From=getAttribute("FROM").getInt();
            ObjFlow.To=getAttribute("TO").getInt();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_FIN_OBC_INVOICE_HEADER";
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
            //==========================================//
            
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_FIN_OBC_INVOICE_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+" AND DOC_NO='"+ObjFlow.DocNo+"'",FinanceGlobal.FinURL);
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+" AND MODULE_ID="+clsOBCInvoice.ModuleID+" AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                ObjFlow.IsCreator=true;
            }
            //==========================================//
            
            
            if(ObjFlow.Status.equals("H")) {
                //Do nothing
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
            //=========== Approval Flow Update complete ===============//
            
            
            
            
            //Conn.commit();
            //data.SetCommit();
            setData();
            return true;
        }
        catch(Exception e) {
            
            try {
                e.printStackTrace();
                //Conn.rollback();
                //data.SetRollback();
                
                //Conn.setAutoCommit(true);
                //data.SetAutoCommit(true);
            }
            catch(Exception c) {
                
            }
            
            
            LastError = e.getMessage();
            return false;
        }
    }
    
    //This routine checks and returns whether the item is deletable or not
    //Criteria is Approved item cannot be deleted
    //and if not approved then user id is checked whether doucment
    //is created by the user. Only creator can delete the document
    public boolean CanDelete(int pCompanyID,String pDocNo,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView) {
                return false;
            }
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FIN_OBC_INVOICE_HEADER WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+clsOBCInvoice.ModuleID+" AND DOC_NO='"+pDocNo+"' AND USER_ID="+pUserID+" AND TYPE='C' AND STATUS='W'";
                tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                rsTmp=tmpStmt.executeQuery(strSQL);
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
            return false;
        }
    }
    
    
    //This routine checks and returns whether the item is editable or not
    //Criteria is Approved item cannot be changed
    //and if not approved then user id is checked whether doucment
    //is waiting for his approval.
    public boolean IsEditable(int pCompanyID,String pDocNo,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FIN_OBC_INVOICE_HEADER WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+clsOBCInvoice.ModuleID+" AND DOC_NO='"+pDocNo+"' AND USER_ID="+pUserID+" AND STATUS='W'";
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
    
    
    
    //Deletes current record
    public boolean Delete(int pUserID) {
        try {
            int CompanyID=getAttribute("COMPANY_ID").getInt();
            String lDocNo=getAttribute("VOUCHER_NO").getString();
            String strSQL="";
            
            //First check that record is editable
            if(CanDelete(CompanyID,lDocNo,pUserID)) {
                String strQry = "DELETE FROM D_FIN_OBC_INVOICE_HEADER WHERE COMPANY_ID=" + CompanyID +" AND DOC_NO='"+lDocNo+"'";
                data.Execute(strQry,FinanceGlobal.FinURL);
                strQry = "DELETE FROM D_FIN_OBC_INVOICE_DETAIL WHERE COMPANY_ID=" + CompanyID +" AND DOC_NO='"+lDocNo+"'";
                data.Execute(strQry,FinanceGlobal.FinURL);
                
                LoadData(CompanyID);
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
        String strCondition = " WHERE COMPANY_ID=" + CompanyID + " AND DOC_NO='" + DocNo + "'" ;
        clsOBCInvoice objDoc = new clsOBCInvoice();
        objDoc.Filter(strCondition,CompanyID);
        return objDoc;
    }
    
    
    public boolean Filter(String Condition,int CompanyID) {
        Ready=false;
        try {
            
            String strSql = "SELECT * FROM D_FIN_OBC_INVOICE_HEADER " + Condition ; //A, D_FIN_OBC_INVOICE_DETAIL B 
            //String strSql = "SELECT * FROM D_FIN_OBC_INVOICE_HEADER " + Condition ;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_FIN_OBC_INVOICE_HEADER A, D_FIN_OBC_INVOICE_DETAIL B WHERE A.DOC_NO = B.DOC_NO AND A.COMPANY_ID="+CompanyID+" AND A.DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND A.DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY A.DOC_NO";
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
    
    public boolean FilterEx(String Condition,int CompanyID) {
        Ready=false;
        try {
            
            String strSql = "SELECT * FROM D_FIN_OBC_INVOICE_HEADER A, D_FIN_OBC_INVOICE_DETAIL B " + Condition ; //A, D_FIN_OBC_INVOICE_DETAIL B 
            //String strSql = "SELECT * FROM D_FIN_OBC_INVOICE_HEADER " + Condition ;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_FIN_OBC_INVOICE_HEADER A, D_FIN_OBC_INVOICE_DETAIL B WHERE A.DOC_NO = B.DOC_NO AND A.COMPANY_ID="+CompanyID+" AND A.DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND A.DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY A.DOC_NO";
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
    
    public boolean setData() {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getConn(FinanceGlobal.FinURL);
        
        long Counter=0;
        int RevNo=0;
        
        try {
            
            //General Retrieval of the Voucher
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",UtilFunctions.getInt(rsResultSet,"REVISION_NO",0));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            setAttribute("COMPANY_ID",UtilFunctions.getInt(rsResultSet,"COMPANY_ID",0));
            setAttribute("DOC_NO",UtilFunctions.getString(rsResultSet,"DOC_NO",""));
            setAttribute("DOC_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"DOC_DATE","0000-00-00")));
            setAttribute("BANK_REFERENCE_NO",UtilFunctions.getString(rsResultSet,"BANK_REFERENCE_NO",""));
            setAttribute("BANK_REFERENCE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"BANK_REFERENCE_DATE","0000-00-00")));
            setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsResultSet,"MAIN_ACCOUNT_CODE",""));
            setAttribute("PARTY_CODE",UtilFunctions.getString(rsResultSet,"PARTY_CODE",""));
            setAttribute("REASON_CODE",UtilFunctions.getString(rsResultSet,"REASON_CODE",""));
            setAttribute("REMARKS",UtilFunctions.getString(rsResultSet,"REMARKS",""));
            setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsResultSet,"HIERARCHY_ID",0));
            setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00")));
            setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00")));
            setAttribute("CHANGED",UtilFunctions.getInt(rsResultSet,"CHANGED",0));
            setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHANGED_DATE","0000-00-00")));
            setAttribute("APPROVED",UtilFunctions.getInt(rsResultSet,"APPROVED",0));
            setAttribute("APPROVED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"APPROVED_DATE","0000-00-00")));
            setAttribute("REJECTED",UtilFunctions.getInt(rsResultSet,"REJECTED",0));
            setAttribute("REJECTED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"REJECTED_DATE","0000-00-00")));
            setAttribute("CANCELLED",UtilFunctions.getInt(rsResultSet,"CANCELLED",0));
            
            colInvoiceItems.clear();
            
            int CompanyID=getAttribute("COMPANY_ID").getInt();
            String DocNo=getAttribute("DOC_NO").getString();
            
            tmpStmt=tmpConn.createStatement();
            
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FIN_OBC_INVOICE_DETAIL_H WHERE COMPANY_ID=" + CompanyID + " AND DOC_NO='" + DocNo + "' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FIN_OBC_INVOICE_DETAIL WHERE COMPANY_ID=" + CompanyID + " AND DOC_NO='" + DocNo + "' ORDER BY SR_NO");
            }
            
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsOBCInvoiceItem objItem = new clsOBCInvoiceItem();
                
                objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                objItem.setAttribute("DOC_NO",UtilFunctions.getString(rsTmp,"DOC_NO",""));
                objItem.setAttribute("SR_NO",UtilFunctions.getInt(rsTmp,"SR_NO",0));
                objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                objItem.setAttribute("INVOICE_AMOUNT",UtilFunctions.getDouble(rsTmp,"INVOICE_AMOUNT",0));
                objItem.setAttribute("REMARKS",UtilFunctions.getString(rsTmp,"REMARKS",""));
                objItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsTmp,"CREATED_BY",""));
                objItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CREATED_DATE","0000-00-00")));
                objItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsTmp,"MODIFIED_BY",""));
                objItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MODIFIED_DATE","0000-00-00")));
                objItem.setAttribute("CHANGED",true);
                objItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CHANGED_DATE","0000-00-00")));
                objItem.setAttribute("CANCELLED",0);
                
                colInvoiceItems.put(Long.toString(Counter),objItem);
                rsTmp.next();
            }
            
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public static HashMap getPendingApprovals(int CompanyID,int UserID,int Order, int FinYearFrom) {
        Connection tmpConn;
        ResultSet rsTmp3=null;
        Statement tmpStmt3=null;
        String strSQL="";
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            
            tmpConn=data.getConn(FinanceGlobal.FinURL);
            tmpStmt3=tmpConn.createStatement();
            
            if(Order==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT FINANCE.D_FIN_OBC_INVOICE_HEADER.DOC_NO,FINANCE.D_FIN_OBC_INVOICE_HEADER.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FIN_OBC_INVOICE_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_OBC_INVOICE_HEADER.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FIN_OBC_INVOICE_HEADER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FIN_OBC_INVOICE_HEADER.COMPANY_ID="+CompanyID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+clsOBCInvoice.ModuleID+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT FINANCE.D_FIN_OBC_INVOICE_HEADER.DOC_NO,FINANCE.D_FIN_OBC_INVOICE_HEADER.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FIN_OBC_INVOICE_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_OBC_INVOICE_HEADER.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FIN_OBC_INVOICE_HEADER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FIN_OBC_INVOICE_HEADER.COMPANY_ID="+CompanyID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+clsOBCInvoice.ModuleID+" ORDER BY D_FIN_OBC_INVOICE_HEADER.DOC_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT FINANCE.D_FIN_OBC_INVOICE_HEADER.DOC_NO,FINANCE.D_FIN_OBC_INVOICE_HEADER.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FIN_OBC_INVOICE_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_OBC_INVOICE_HEADER.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FIN_OBC_INVOICE_HEADER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FIN_OBC_INVOICE_HEADER.COMPANY_ID="+CompanyID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+clsOBCInvoice.ModuleID+" ORDER BY D_FIN_OBC_INVOICE_HEADER.DOC_NO";
            }
            
            rsTmp3=tmpStmt3.executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp3.next()) {
                if(EITLERPGLOBAL.isWithinDate(rsTmp3.getString("DOC_DATE"),FinYearFrom)) {
                    Counter=Counter+1;
                    clsOBCInvoice ObjDoc=new clsOBCInvoice();
                    
                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("DOC_NO",rsTmp3.getString("DOC_NO"));
                    ObjDoc.setAttribute("DOC_DATE",rsTmp3.getString("DOC_DATE"));
                    ObjDoc.setAttribute("RECEIVED_DATE",rsTmp3.getString("RECEIVED_DATE"));
                    // ----------------- End of Header Fields ------------------------------------//
                    
                    String PartyCode=data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FIN_OBC_INVOICE_HEADER WHERE DOC_NO='"+rsTmp3.getString("DOC_NO")+"' AND DOC_DATE='"+rsTmp3.getString("DOC_DATE")+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID,FinanceGlobal.FinURL);
                         String MainCode =data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_OBC_INVOICE_HEADER WHERE DOC_NO='"+rsTmp3.getString("DOC_NO")+"' AND DOC_DATE='"+rsTmp3.getString("DOC_DATE")+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID,FinanceGlobal.FinURL);
                         
                        String PartyName=clsAccount.getAccountName(MainCode,PartyCode); 
                         
                         ObjDoc.setAttribute("PARTY_CODE",PartyCode);
                          ObjDoc.setAttribute("PARTY_NAME",PartyName);
                    List.put(Long.toString(Counter),ObjDoc);
                }
            }//end of while
            
            //tmpConn.close();
            rsTmp3.close();
            tmpStmt3.close();
            
        }
        catch(Exception e) {
        }
        
        return List;
    }
    
    
    
    public boolean ShowHistory(int CompanyID,String DocNo) {
        Ready=false;
        try {
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FIN_OBC_INVOICE_HEADER_H WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"'");
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
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn(FinanceGlobal.FinURL);
            stTmp=tmpConn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_FIN_OBC_INVOICE_HEADER_H WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                
                while(!rsTmp.isAfterLast()) {
                    clsOBCInvoice objVoucher=new clsOBCInvoice();
                    
                    objVoucher.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                    objVoucher.setAttribute("DOC_DATE",EITLERPGLOBAL.formatDate(rsTmp.getString("DOC_DATE")));
                    objVoucher.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                    objVoucher.setAttribute("UPDATED_BY",rsTmp.getString("UPDATED_BY"));
                    objVoucher.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                    objVoucher.setAttribute("ENTRY_DATE",EITLERPGLOBAL.formatDate(rsTmp.getString("ENTRY_DATE")));
                    objVoucher.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                    
                    List.put(Integer.toString(List.size()+1),objVoucher);
                    
                    rsTmp.next();
                }
                
            }
            rsTmp.close();
            stTmp.close();
            //tmpConn.close();
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
            rsTmp=data.getResult("SELECT DOC_NO,APPROVED,CANCELLED FROM D_FIN_OBC_INVOICE_HEADER WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"'",FinanceGlobal.FinURL);
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
        }
        catch(Exception e) {
        }
        
        return strMessage;
    }
    
    
    
    public static boolean CanCancel(int CompanyID,String DocNo) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            rsTmp=data.getResult("SELECT DOC_NO FROM D_FIN_OBC_INVOICE_HEADER WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"' AND CANCELLED=0",FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                canCancel=true;
            }
            
            rsTmp.close();
        }
        catch(Exception e) {
            
        }
        
        return canCancel;
    }
    
    
    public static boolean CancelDoc(int CompanyID,String DocNo) {
        
        ResultSet rsTmp=null,rsIndent=null;
        boolean Cancelled=false;
        
        try {
            if(CanCancel(CompanyID,DocNo)) {
                
                boolean ApprovedDoc=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_FIN_OBC_INVOICE_HEADER WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"'",FinanceGlobal.FinURL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedDoc) {
                    
                }
                else {
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"' AND MODULE_ID="+(clsOBCInvoice.ModuleID));
                }
                
                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_FIN_OBC_INVOICE_HEADER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"'",FinanceGlobal.FinURL);
                data.Execute("UPDATE D_FIN_OBC_INVOICE_DETAIL SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"'",FinanceGlobal.FinURL);
                data.Execute("UPDATE D_FIN_OBC_INVOICE_DETAIL_EX SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"'",FinanceGlobal.FinURL);
                
                Cancelled=true;
            }
            
            rsTmp.close();
            rsIndent.close();
        }
        catch(Exception e) {
            
        }
        
        return Cancelled;
    }
    
    
    
    public boolean Validate() {
        try {
            
            if(colInvoiceItems.size()==0)
            {
               LastError="Please specify invoice nos.";
               return false;
            }
            
            
            for(int i=1;i<=colInvoiceItems.size();i++)
            {
              clsOBCInvoiceItem objItem=(clsOBCInvoiceItem)colInvoiceItems.get(Integer.toString(i));
              
              if(!clsSalesInvoice.IsDocExist(EITLERPGLOBAL.gCompanyID,objItem.getAttribute("INVOICE_NO").getString(),EITLERPGLOBAL.formatDateDB(objItem.getAttribute("INVOICE_DATE").getString())))
              {
                LastError="Invoice no. "+objItem.getAttribute("INVOICE_NO").getString()+" is invalid. Please verify and try again";
                return false;
              }
            }
            
            
            return true;
        }
        catch(Exception e) {
            return false;
        }
        
    }
    
}
