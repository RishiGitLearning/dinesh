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

public class clsCreditNoteProcessModule{
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
    private boolean HistoryView=false;
    public HashMap colProcessDtl=new HashMap();
    public HashMap colProcessInvDtl=new HashMap();
    public HashMap colProcessTurnOverDtl = new HashMap();
    private clsVoucher objVoucher = new clsVoucher();
    
    public static int ModuleID=161;
    
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
    public clsCreditNoteProcessModule() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("POLICY_ID",new Variant(""));
        props.put("FROM_DATE",new Variant(""));
        props.put("TO_DATE",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("AMOUNT_SELECTION",new Variant(""));
        
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
        
        props.put("REJECTED_REMARKS", new Variant(""));
        props.put("CANCELLED", new Variant(false));
        props.put("STATUS", new Variant(""));
        
        colProcessDtl=new HashMap();
        colProcessInvDtl = new HashMap();
        colProcessTurnOverDtl = new HashMap();
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        
        props.put("FFNO",new Variant(0));
        props.put("PREFIX",new Variant(""));
        props.put("SUFFIX",new Variant(""));
        
        props.put("FIN_HIERARCHY_ID",new Variant(0));
    }
    
    /**Load Data. This method loads data from database to Business Object**/
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_SAL_POLICY_PROCESS WHERE COMPANY_ID="+ pCompanyID +" ORDER BY DOC_NO");
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
            rsHistory=stHistory.executeQuery("SELECT * FROM D_SAL_POLICY_PROCESS_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //=============================Generate New Policy No.===============================
            setAttribute("DOC_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,clsCreditNoteProcessModule.ModuleID,(int)getAttribute("FFNO").getVal(),true));
            //===============================================================================================
            
            if(data.IsRecordExist("SELECT DOC_NO FROM D_SAL_POLICY_PROCESS WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"' ")) {
                LastError="Doc No already exist.";
                return false;
            }
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsResultSet.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsResultSet.updateString("DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsResultSet.updateString("POLICY_ID",getAttribute("POLICY_ID").getString());
            rsResultSet.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("FROM_DATE").getString()));
            rsResultSet.updateString("TO_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("TO_DATE").getString()));
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateString("AMOUNT_SELECTION",getAttribute("AMOUNT_SELECTION").getString());
            
            rsResultSet.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
            rsResultSet.updateLong("FIN_HIERARCHY_ID",getAttribute("FIN_HIERARCHY_ID").getLong());
            rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CHANGED",false);
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
            rsHistory.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsHistory.updateString("DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsHistory.updateString("POLICY_ID",getAttribute("POLICY_ID").getString());
            rsHistory.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("FROM_DATE").getString()));
            rsHistory.updateString("TO_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("TO_DATE").getString()));
            rsHistory.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateString("AMOUNT_SELECTION",getAttribute("AMOUNT_SELECTION").getString());
            
            rsHistory.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
            rsHistory.updateLong("FIN_HIERARCHY_ID",getAttribute("FIN_HIERARCHY_ID").getLong());
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
            
            ResultSet rsHProcessInvDtl,rsProcessInvDtl;
            Statement stHProcessInvDtl,stProcessInvDtl;
            
            stProcessInvDtl=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsProcessInvDtl=stProcessInvDtl.executeQuery("SELECT * FROM D_SAL_POLICY_PROCESS_INV  WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            
            
            stHProcessInvDtl=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHProcessInvDtl=stHProcessInvDtl.executeQuery("SELECT * FROM D_SAL_POLICY_PROCESS_INV_H  WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHProcessInvDtl.first();
            
            
            //Now Insert records into detail table
            for(int i=1;i<=colProcessInvDtl.size();i++) {
                clsPolicyInvoiceList objProcessDtl=(clsPolicyInvoiceList) colProcessInvDtl.get(Integer.toString(i));
                
                rsProcessInvDtl.moveToInsertRow();
                rsProcessInvDtl.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsProcessInvDtl.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsProcessInvDtl.updateInt("SR_NO",i);
                rsProcessInvDtl.updateString("INVOICE_NO",objProcessDtl.getAttribute("INVOICE_NO").getString());
                rsProcessInvDtl.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objProcessDtl.getAttribute("INVOICE_DATE").getString()));
                rsProcessInvDtl.updateString("PARTY_CODE",objProcessDtl.getAttribute("PARTY_CODE").getString());
                rsProcessInvDtl.updateString("PARTY_NAME",objProcessDtl.getAttribute("PARTY_NAME").getString());
                rsProcessInvDtl.updateString("CHARGE_CODE",objProcessDtl.getAttribute("CHARGE_CODE").getString());
                rsProcessInvDtl.updateString("PIECE_NO",objProcessDtl.getAttribute("PIECE_NO").getString());
                rsProcessInvDtl.updateString("QUALITY_NO",objProcessDtl.getAttribute("QUALITY_NO").getString());
                rsProcessInvDtl.updateBoolean("SELECTION",objProcessDtl.getAttribute("SELECTION").getBool());
                rsProcessInvDtl.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsProcessInvDtl.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsProcessInvDtl.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsProcessInvDtl.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsProcessInvDtl.updateBoolean("CHANGED",false);
                rsProcessInvDtl.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsProcessInvDtl.updateBoolean("CANCELLED",false);
                
                rsProcessInvDtl.insertRow();
                
                rsHProcessInvDtl.moveToInsertRow();
                rsHProcessInvDtl.updateInt("REVISION_NO",1);
                rsHProcessInvDtl.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsHProcessInvDtl.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsHProcessInvDtl.updateInt("SR_NO",i);
                rsHProcessInvDtl.updateString("INVOICE_NO",objProcessDtl.getAttribute("INVOICE_NO").getString());
                rsHProcessInvDtl.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objProcessDtl.getAttribute("INVOICE_DATE").getString()));
                rsHProcessInvDtl.updateString("PARTY_CODE",objProcessDtl.getAttribute("PARTY_CODE").getString());
                rsHProcessInvDtl.updateString("PARTY_NAME",objProcessDtl.getAttribute("PARTY_NAME").getString());
                rsHProcessInvDtl.updateString("CHARGE_CODE",objProcessDtl.getAttribute("CHARGE_CODE").getString());
                rsHProcessInvDtl.updateString("PIECE_NO",objProcessDtl.getAttribute("PIECE_NO").getString());
                rsHProcessInvDtl.updateString("QUALITY_NO",objProcessDtl.getAttribute("QUALITY_NO").getString());
                rsHProcessInvDtl.updateBoolean("SELECTION",objProcessDtl.getAttribute("SELECTION").getBool());
                rsHProcessInvDtl.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsHProcessInvDtl.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHProcessInvDtl.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsHProcessInvDtl.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHProcessInvDtl.updateBoolean("CHANGED",false);
                rsHProcessInvDtl.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHProcessInvDtl.updateBoolean("CANCELLED",false);
                
                rsHProcessInvDtl.insertRow();
                
            }
            
            ResultSet rsHProcessDtl,rsProcessDtl;
            Statement stHProcessDtl,stProcessDtl;
            
            stProcessDtl=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsProcessDtl=stProcessDtl.executeQuery("SELECT * FROM D_SAL_POLICY_PROCESS_DETAIL  WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            
            
            stHProcessDtl=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHProcessDtl=stHProcessDtl.executeQuery("SELECT * FROM D_SAL_POLICY_PROCESS_DETAIL_H  WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHProcessDtl.first();
            
            
            //Now Insert records into detail table
            for(int i=1;i<=colProcessDtl.size();i++) {
                clsCreditNoteProcessDetail objProcessDtl=(clsCreditNoteProcessDetail) colProcessDtl.get(Integer.toString(i));
                
                rsProcessDtl.moveToInsertRow();
                rsProcessDtl.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsProcessDtl.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsProcessDtl.updateInt("SR_NO",i);
                rsProcessDtl.updateString("PARTY_ID",objProcessDtl.getAttribute("PARTY_ID").getString());
                rsProcessDtl.updateString("PARTY_MAIN_CODE",objProcessDtl.getAttribute("PARTY_MAIN_CODE").getString());
                rsProcessDtl.updateDouble("AMOUNT", objProcessDtl.getAttribute("AMOUNT").getDouble());
                rsProcessDtl.updateDouble("QUALIFYING_AMOUNT", objProcessDtl.getAttribute("QUALIFING_AMOUNT").getDouble());
                rsProcessDtl.updateDouble("PERCENTAGE", objProcessDtl.getAttribute("PERCENTAGE").getDouble());
                rsProcessDtl.updateString("CREDIT_NOTE_NO",objProcessDtl.getAttribute("CREDIT_NOTE_NO").getString());
                rsProcessDtl.updateString("INVOICE_NO",objProcessDtl.getAttribute("INVOICE_NO").getString());
                rsProcessDtl.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objProcessDtl.getAttribute("INVOICE_DATE").getString()));
                rsProcessDtl.updateString("QUALITY_ID",objProcessDtl.getAttribute("QUALITY_ID").getString());
                rsProcessDtl.updateString("PIECE_NO",objProcessDtl.getAttribute("PIECE_NO").getString());
                rsProcessDtl.updateString("SEASON_ID",objProcessDtl.getAttribute("SEASON_ID").getString());
                rsProcessDtl.updateDouble("DISCOUNT_AMOUNT", objProcessDtl.getAttribute("DISCOUNT_AMOUNT").getDouble());
                rsProcessDtl.updateInt("VOUCHER_TYPE", objProcessDtl.getAttribute("VOUCHER_TYPE").getInt());
                rsProcessDtl.updateString("BOOK_CODE",objProcessDtl.getAttribute("BOOK_CODE").getString());
                rsProcessDtl.updateString("DEDUCTION_CODE",objProcessDtl.getAttribute("DEDUCTION_CODE").getString());
                rsProcessDtl.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsProcessDtl.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsProcessDtl.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsProcessDtl.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsProcessDtl.updateBoolean("CHANGED",false);
                rsProcessDtl.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsProcessDtl.updateBoolean("CANCELLED",false);
                
                rsProcessDtl.insertRow();
                
                rsHProcessDtl.moveToInsertRow();
                rsHProcessDtl.updateInt("REVISION_NO",1);
                rsHProcessDtl.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsHProcessDtl.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsHProcessDtl.updateInt("SR_NO",i);
                rsHProcessDtl.updateString("PARTY_ID",objProcessDtl.getAttribute("PARTY_ID").getString());
                rsHProcessDtl.updateString("PARTY_MAIN_CODE",objProcessDtl.getAttribute("PARTY_MAIN_CODE").getString());
                rsHProcessDtl.updateDouble("AMOUNT", objProcessDtl.getAttribute("AMOUNT").getDouble());
                rsHProcessDtl.updateDouble("QUALIFYING_AMOUNT", objProcessDtl.getAttribute("QUALIFING_AMOUNT").getDouble());
                rsHProcessDtl.updateDouble("PERCENTAGE", objProcessDtl.getAttribute("PERCENTAGE").getDouble());
                rsHProcessDtl.updateString("CREDIT_NOTE_NO",objProcessDtl.getAttribute("CREDIT_NOTE_NO").getString());
                rsHProcessDtl.updateString("INVOICE_NO",objProcessDtl.getAttribute("INVOICE_NO").getString());
                rsHProcessDtl.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objProcessDtl.getAttribute("INVOICE_DATE").getString()));
                rsHProcessDtl.updateString("QUALITY_ID",objProcessDtl.getAttribute("QUALITY_ID").getString());
                rsHProcessDtl.updateString("PIECE_NO",objProcessDtl.getAttribute("PIECE_NO").getString());
                rsHProcessDtl.updateString("SEASON_ID",objProcessDtl.getAttribute("SEASON_ID").getString());
                rsHProcessDtl.updateDouble("DISCOUNT_AMOUNT", objProcessDtl.getAttribute("DISCOUNT_AMOUNT").getDouble());
                rsHProcessDtl.updateInt("VOUCHER_TYPE", objProcessDtl.getAttribute("VOUCHER_TYPE").getInt());
                rsHProcessDtl.updateString("BOOK_CODE",objProcessDtl.getAttribute("BOOK_CODE").getString());
                rsHProcessDtl.updateString("DEDUCTION_CODE",objProcessDtl.getAttribute("DEDUCTION_CODE").getString());
                rsHProcessDtl.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsHProcessDtl.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHProcessDtl.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsHProcessDtl.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHProcessDtl.updateBoolean("CHANGED",false);
                rsHProcessDtl.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHProcessDtl.updateBoolean("CANCELLED",false);
                
                rsHProcessDtl.insertRow();
                
            }
            
            ResultSet rsHProcessTurnOverDtl,rsProcessTurnOverDtl;
            Statement stHProcessTurnOverDtl,stProcessTurnOverDtl;
            
            stProcessTurnOverDtl=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsProcessTurnOverDtl=stProcessTurnOverDtl.executeQuery("SELECT * FROM D_SAL_POLICY_PROCESS_TURNOVER  WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            
            
            stHProcessTurnOverDtl=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHProcessTurnOverDtl=stHProcessTurnOverDtl.executeQuery("SELECT * FROM D_SAL_POLICY_PROCESS_TURNOVER_H  WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHProcessTurnOverDtl.first();
            
            
            //Now Insert records into detail table
            for(int i=1;i<=colProcessTurnOverDtl.size();i++) {
                clsPolicyTurnOverList objProcessTurnOverDtl=(clsPolicyTurnOverList) colProcessTurnOverDtl.get(Integer.toString(i));
                
                rsProcessTurnOverDtl.moveToInsertRow();
                rsProcessTurnOverDtl.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsProcessTurnOverDtl.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsProcessTurnOverDtl.updateInt("SR_NO",i);
                rsProcessTurnOverDtl.updateString("PARTY_CODE",objProcessTurnOverDtl.getAttribute("PARTY_CODE").getString());
                rsProcessTurnOverDtl.updateInt("YEAR",objProcessTurnOverDtl.getAttribute("YEAR").getInt());
                rsProcessTurnOverDtl.updateDouble("TURNOVER_AMOUNT",objProcessTurnOverDtl.getAttribute("TURNOVER_AMOUNT").getDouble());
                rsProcessTurnOverDtl.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsProcessTurnOverDtl.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsProcessTurnOverDtl.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsProcessTurnOverDtl.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsProcessTurnOverDtl.updateBoolean("CHANGED",false);
                rsProcessTurnOverDtl.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsProcessTurnOverDtl.updateBoolean("CANCELLED",false);
                
                rsProcessTurnOverDtl.insertRow();
                
                rsHProcessTurnOverDtl.moveToInsertRow();
                rsHProcessTurnOverDtl.updateInt("REVISION_NO",1);
                rsHProcessTurnOverDtl.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsHProcessTurnOverDtl.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsHProcessTurnOverDtl.updateInt("SR_NO",i);
                rsHProcessTurnOverDtl.updateString("PARTY_CODE",objProcessTurnOverDtl.getAttribute("PARTY_CODE").getString());
                rsHProcessTurnOverDtl.updateInt("YEAR",objProcessTurnOverDtl.getAttribute("YEAR").getInt());
                rsHProcessTurnOverDtl.updateDouble("TURNOVER_AMOUNT",objProcessTurnOverDtl.getAttribute("TURNOVER_AMOUNT").getDouble());                
                rsHProcessTurnOverDtl.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsHProcessTurnOverDtl.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHProcessTurnOverDtl.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsHProcessTurnOverDtl.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHProcessTurnOverDtl.updateBoolean("CHANGED",false);
                rsHProcessTurnOverDtl.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHProcessTurnOverDtl.updateBoolean("CANCELLED",false);
                
                rsHProcessTurnOverDtl.insertRow();
                
            }
            
            
            //======== Update the Approval Flow =========//
            
            ObjFlow.CompanyID=EITLERPGLOBAL.gCompanyID;
            ObjFlow.ModuleID=ModuleID;
            ObjFlow.DocNo=getAttribute("DOC_NO").getString();
            ObjFlow.From=getAttribute("FROM").getInt();
            ObjFlow.To=getAttribute("TO").getInt();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_SAL_POLICY_PROCESS";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=getAttribute("HIERARCHY_ID").getInt();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName="DOC_NO";
            ObjFlow.UseSpecifiedURL=true;
            ObjFlow.SpecificURL=EITLERPGLOBAL.DatabaseURL;
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
            
            //===============Accounts Generation =============//
            try {
                if(getAttribute("APPROVAL_STATUS").getString().equals("F")) {
                    PostVoucher(getAttribute("DOC_NO").getString());
                }
                
            }
            catch(Exception e) {
                
            }
            //====================================================//
            
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
            
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            String AStatus=getAttribute("APPROVAL_STATUS").getString();
            if(AStatus.equals("R")) {
                Validate=false; //Do not validate while in rejection mode.
            }
            
            
            //** Open History Table Connections **//
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHistory=stHistory.executeQuery("SELECT * FROM D_SAL_POLICY_PROCESS_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //** --------------------------------**//
            
            //Start Transaction
            //Conn.setAutoCommit(false);
            //data.SetAutoCommit(false);
            
            //rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            //rsResultSet.updateString("POLICY_ID",getAttribute("POLICY_ID").getString());
            rsResultSet.updateString("POLICY_ID",getAttribute("POLICY_ID").getString());
            rsResultSet.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("FROM_DATE").getString()));
            rsResultSet.updateString("TO_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("TO_DATE").getString()));
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateString("AMOUNT_SELECTION",getAttribute("AMOUNT_SELECTION").getString());
            
            rsResultSet.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
            rsResultSet.updateLong("FIN_HIERARCHY_ID",getAttribute("FIN_HIERARCHY_ID").getLong());
            rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED",false);
            
            rsResultSet.updateRow();
            
            //========= Inserting Into History =================//
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM D_SAL_POLICY_PROCESS_H WHERE DOC_NO='"+theDocNo+"'");
            
            RevNo++;
            
            String RevDocNo=getAttribute("DOC_NO").getString();
            //System.out.println("RevNo="+RevNo);
            //========= Inserting Into History =================//
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo);
            rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            rsHistory.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsHistory.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsHistory.updateString("DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsHistory.updateString("POLICY_ID",getAttribute("POLICY_ID").getString());
            rsHistory.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("FROM_DATE").getString()));
            rsHistory.updateString("TO_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("TO_DATE").getString()));
            rsHistory.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateString("AMOUNT_SELECTION",getAttribute("AMOUNT_SELECTION").getString());
            
            rsHistory.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
            rsHistory.updateLong("FIN_HIERARCHY_ID",getAttribute("FIN_HIERARCHY_ID").getLong());
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
            
            //Remove Old Records
            data.Execute("DELETE FROM D_SAL_POLICY_PROCESS_INV WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
            
            ResultSet rsHProcessInvDtl,rsProcessInvDtl;
            Statement stHProcessInvDtl,stProcessInvDtl;
            
            stProcessInvDtl=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsProcessInvDtl=stProcessInvDtl.executeQuery("SELECT * FROM D_SAL_POLICY_PROCESS_INV  WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            
            
            stHProcessInvDtl=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHProcessInvDtl=stHProcessInvDtl.executeQuery("SELECT * FROM D_SAL_POLICY_PROCESS_INV_H  WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHProcessInvDtl.first();
            
            
            //Now Insert records into detail table
            for(int i=1;i<=colProcessInvDtl.size();i++) {
                clsPolicyInvoiceList objProcessDtl=(clsPolicyInvoiceList) colProcessInvDtl.get(Integer.toString(i));
                
                rsProcessInvDtl.moveToInsertRow();
                rsProcessInvDtl.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsProcessInvDtl.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsProcessInvDtl.updateInt("SR_NO",i);
                rsProcessInvDtl.updateString("INVOICE_NO",objProcessDtl.getAttribute("INVOICE_NO").getString());
                rsProcessInvDtl.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objProcessDtl.getAttribute("INVOICE_DATE").getString()));
                rsProcessInvDtl.updateString("PARTY_CODE",objProcessDtl.getAttribute("PARTY_CODE").getString());
                rsProcessInvDtl.updateString("PARTY_NAME",objProcessDtl.getAttribute("PARTY_NAME").getString());
                rsProcessInvDtl.updateString("CHARGE_CODE",objProcessDtl.getAttribute("CHARGE_CODE").getString());
                rsProcessInvDtl.updateBoolean("SELECTION",objProcessDtl.getAttribute("SELECTION").getBool());
                rsProcessInvDtl.updateString("PIECE_NO",objProcessDtl.getAttribute("PIECE_NO").getString());
                rsProcessInvDtl.updateString("QUALITY_NO",objProcessDtl.getAttribute("QUALITY_NO").getString());
                rsProcessInvDtl.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsProcessInvDtl.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsProcessInvDtl.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsProcessInvDtl.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsProcessInvDtl.updateBoolean("CHANGED",false);
                rsProcessInvDtl.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsProcessInvDtl.updateBoolean("CANCELLED",false);
                
                rsProcessInvDtl.insertRow();
                
                rsHProcessInvDtl.moveToInsertRow();
                rsHProcessInvDtl.updateInt("REVISION_NO",RevNo);
                rsHProcessInvDtl.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsHProcessInvDtl.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsHProcessInvDtl.updateInt("SR_NO",i);
                rsHProcessInvDtl.updateString("INVOICE_NO",objProcessDtl.getAttribute("INVOICE_NO").getString());
                rsHProcessInvDtl.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objProcessDtl.getAttribute("INVOICE_DATE").getString()));
                rsHProcessInvDtl.updateString("PARTY_CODE",objProcessDtl.getAttribute("PARTY_CODE").getString());
                rsHProcessInvDtl.updateString("PARTY_NAME",objProcessDtl.getAttribute("PARTY_NAME").getString());
                rsHProcessInvDtl.updateString("CHARGE_CODE",objProcessDtl.getAttribute("CHARGE_CODE").getString());
                rsHProcessInvDtl.updateBoolean("SELECTION",objProcessDtl.getAttribute("SELECTION").getBool());
                rsHProcessInvDtl.updateString("PIECE_NO",objProcessDtl.getAttribute("PIECE_NO").getString());
                rsHProcessInvDtl.updateString("QUALITY_NO",objProcessDtl.getAttribute("QUALITY_NO").getString());
                rsHProcessInvDtl.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsHProcessInvDtl.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHProcessInvDtl.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsHProcessInvDtl.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHProcessInvDtl.updateBoolean("CHANGED",false);
                rsHProcessInvDtl.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHProcessInvDtl.updateBoolean("CANCELLED",false);
                
                rsHProcessInvDtl.insertRow();
                
            }
            
            //Remove Old Records
            data.Execute("DELETE FROM D_SAL_POLICY_PROCESS_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
            
            ResultSet rsHProcessDtl,rsProcessDtl;
            Statement stHProcessDtl,stProcessDtl;
            
            stProcessDtl=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsProcessDtl=stProcessDtl.executeQuery("SELECT * FROM D_SAL_POLICY_PROCESS_DETAIL WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            
            stHProcessDtl=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHProcessDtl=stHProcessDtl.executeQuery("SELECT * FROM D_SAL_POLICY_PROCESS_DETAIL_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHProcessDtl.first();
            
            int Counter=0;
            
            //Now Insert records into detail table
            for(int i=1;i<=colProcessDtl.size();i++) {
                clsCreditNoteProcessDetail objProcessDtl=(clsCreditNoteProcessDetail) colProcessDtl.get(Integer.toString(i));
                
                Counter++;
                
                rsProcessDtl.moveToInsertRow();
                rsProcessDtl.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsProcessDtl.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsProcessDtl.updateInt("SR_NO",Counter);
                rsProcessDtl.updateString("PARTY_MAIN_CODE",objProcessDtl.getAttribute("PARTY_MAIN_CODE").getString());
                rsProcessDtl.updateString("PARTY_ID",objProcessDtl.getAttribute("PARTY_ID").getString());
                rsProcessDtl.updateDouble("AMOUNT", objProcessDtl.getAttribute("AMOUNT").getDouble());
                rsProcessDtl.updateDouble("QUALIFYING_AMOUNT", objProcessDtl.getAttribute("QUALIFING_AMOUNT").getDouble());
                rsProcessDtl.updateDouble("PERCENTAGE", objProcessDtl.getAttribute("PERCENTAGE").getDouble());
                rsProcessDtl.updateString("CREDIT_NOTE_NO",objProcessDtl.getAttribute("CREDIT_NOTE_NO").getString());
                rsProcessDtl.updateString("INVOICE_NO",objProcessDtl.getAttribute("INVOICE_NO").getString());
                rsProcessDtl.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objProcessDtl.getAttribute("INVOICE_DATE").getString()));
                rsProcessDtl.updateString("QUALITY_ID",objProcessDtl.getAttribute("QUALITY_ID").getString());
                rsProcessDtl.updateString("PIECE_NO",objProcessDtl.getAttribute("PIECE_NO").getString());
                rsProcessDtl.updateString("SEASON_ID",objProcessDtl.getAttribute("SEASON_ID").getString());
                rsProcessDtl.updateDouble("DISCOUNT_AMOUNT", objProcessDtl.getAttribute("DISCOUNT_AMOUNT").getDouble());
                rsProcessDtl.updateInt("VOUCHER_TYPE", objProcessDtl.getAttribute("VOUCHER_TYPE").getInt());
                rsProcessDtl.updateString("BOOK_CODE",objProcessDtl.getAttribute("BOOK_CODE").getString());
                rsProcessDtl.updateString("DEDUCTION_CODE",objProcessDtl.getAttribute("DEDUCTION_CODE").getString());
                
                rsProcessDtl.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsProcessDtl.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsProcessDtl.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsProcessDtl.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsProcessDtl.updateBoolean("CHANGED",true);
                rsProcessDtl.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsProcessDtl.updateBoolean("CANCELLED",false);
                
                rsProcessDtl.insertRow();
                
                
                rsHProcessDtl.moveToInsertRow();
                rsHProcessDtl.updateInt("REVISION_NO",RevNo);
                rsHProcessDtl.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsHProcessDtl.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsHProcessDtl.updateInt("SR_NO",Counter);
                rsHProcessDtl.updateString("PARTY_MAIN_CODE",objProcessDtl.getAttribute("PARTY_MAIN_CODE").getString());
                rsHProcessDtl.updateString("PARTY_ID",objProcessDtl.getAttribute("PARTY_ID").getString());
                rsHProcessDtl.updateDouble("AMOUNT", objProcessDtl.getAttribute("AMOUNT").getDouble());
                rsHProcessDtl.updateDouble("QUALIFYING_AMOUNT", objProcessDtl.getAttribute("QUALIFING_AMOUNT").getDouble());
                rsHProcessDtl.updateDouble("PERCENTAGE", objProcessDtl.getAttribute("PERCENTAGE").getDouble());
                rsHProcessDtl.updateString("CREDIT_NOTE_NO",objProcessDtl.getAttribute("CREDIT_NOTE_NO").getString());
                rsHProcessDtl.updateString("INVOICE_NO",objProcessDtl.getAttribute("INVOICE_NO").getString());
                rsHProcessDtl.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objProcessDtl.getAttribute("INVOICE_DATE").getString()));
                rsHProcessDtl.updateString("QUALITY_ID",objProcessDtl.getAttribute("QUALITY_ID").getString());
                rsHProcessDtl.updateString("PIECE_NO",objProcessDtl.getAttribute("PIECE_NO").getString());
                rsHProcessDtl.updateString("SEASON_ID",objProcessDtl.getAttribute("SEASON_ID").getString());
                rsHProcessDtl.updateDouble("DISCOUNT_AMOUNT", objProcessDtl.getAttribute("DISCOUNT_AMOUNT").getDouble());
                rsHProcessDtl.updateInt("VOUCHER_TYPE", objProcessDtl.getAttribute("VOUCHER_TYPE").getInt());
                rsHProcessDtl.updateString("BOOK_CODE",objProcessDtl.getAttribute("BOOK_CODE").getString());
                rsHProcessDtl.updateString("DEDUCTION_CODE",objProcessDtl.getAttribute("DEDUCTION_CODE").getString());
                
                rsHProcessDtl.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsHProcessDtl.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHProcessDtl.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsHProcessDtl.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHProcessDtl.updateBoolean("CHANGED",true);
                rsHProcessDtl.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHProcessDtl.updateBoolean("CANCELLED",false);
                
                rsHProcessDtl.insertRow();
                
            }
            
            //Remove Old Records
            data.Execute("DELETE FROM D_SAL_POLICY_PROCESS_TURNOVER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
            
            ResultSet rsHProcessTurnOverDtl,rsProcessTurnOverDtl;
            Statement stHProcessTurnOverDtl,stProcessTurnOverDtl;
            
            stProcessTurnOverDtl=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsProcessTurnOverDtl=stProcessTurnOverDtl.executeQuery("SELECT * FROM D_SAL_POLICY_PROCESS_TURNOVER  WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            
            
            stHProcessTurnOverDtl=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHProcessTurnOverDtl=stHProcessTurnOverDtl.executeQuery("SELECT * FROM D_SAL_POLICY_PROCESS_TURNOVER_H  WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHProcessTurnOverDtl.first();
            
            
            //Now Insert records into detail table
            for(int i=1;i<=colProcessTurnOverDtl.size();i++) {
                clsPolicyTurnOverList objProcessTurnOverDtl=(clsPolicyTurnOverList) colProcessTurnOverDtl.get(Integer.toString(i));
                
                rsProcessTurnOverDtl.moveToInsertRow();
                rsProcessTurnOverDtl.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsProcessTurnOverDtl.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsProcessTurnOverDtl.updateInt("SR_NO",i);
                rsProcessTurnOverDtl.updateString("PARTY_CODE",objProcessTurnOverDtl.getAttribute("PARTY_CODE").getString());
                rsProcessTurnOverDtl.updateInt("YEAR",objProcessTurnOverDtl.getAttribute("YEAR").getInt());
                rsProcessTurnOverDtl.updateDouble("TURNOVER_AMOUNT",objProcessTurnOverDtl.getAttribute("TURNOVER_AMOUNT").getDouble());
                rsProcessTurnOverDtl.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsProcessTurnOverDtl.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsProcessTurnOverDtl.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsProcessTurnOverDtl.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsProcessTurnOverDtl.updateBoolean("CHANGED",false);
                rsProcessTurnOverDtl.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsProcessTurnOverDtl.updateBoolean("CANCELLED",false);
                
                rsProcessTurnOverDtl.insertRow();
                
                rsHProcessTurnOverDtl.moveToInsertRow();
                rsHProcessTurnOverDtl.updateInt("REVISION_NO",RevNo);
                rsHProcessTurnOverDtl.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsHProcessTurnOverDtl.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsHProcessTurnOverDtl.updateInt("SR_NO",i);
                rsHProcessTurnOverDtl.updateString("PARTY_CODE",objProcessTurnOverDtl.getAttribute("PARTY_CODE").getString());
                rsHProcessTurnOverDtl.updateInt("YEAR",objProcessTurnOverDtl.getAttribute("YEAR").getInt());
                rsHProcessTurnOverDtl.updateDouble("TURNOVER_AMOUNT",objProcessTurnOverDtl.getAttribute("TURNOVER_AMOUNT").getDouble());                
                rsHProcessTurnOverDtl.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsHProcessTurnOverDtl.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHProcessTurnOverDtl.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsHProcessTurnOverDtl.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHProcessTurnOverDtl.updateBoolean("CHANGED",false);
                rsHProcessTurnOverDtl.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHProcessTurnOverDtl.updateBoolean("CANCELLED",false);
                
                rsHProcessTurnOverDtl.insertRow();
                
            }
            
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=EITLERPGLOBAL.gCompanyID;
            ObjFlow.ModuleID=ModuleID;
            ObjFlow.DocNo=getAttribute("DOC_NO").getString();
            ObjFlow.From=getAttribute("FROM").getInt();
            ObjFlow.To=getAttribute("TO").getInt();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_SAL_POLICY_PROCESS";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=getAttribute("HIERARCHY_ID").getInt();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName="DOC_NO";
            ObjFlow.UseSpecifiedURL=true;
            ObjFlow.SpecificURL=EITLERPGLOBAL.DatabaseURL;
            //==== Handling Rejected Documents ==========//
            
            
            if(AStatus.equals("R")) {
                ObjFlow.To=getAttribute("SEND_DOC_TO").getInt();
                ObjFlow.ExplicitSendTo=true;
            }
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_SAL_POLICY_PROCESS SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='"+theDocNo+"'");
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
            
            //===============Accounts Generation =============//
            try {
                if(AStatus.equals("F")) {
                    PostVoucher(getAttribute("DOC_NO").getString());
                    
                }
                
            }
            catch(Exception e) {
                
            }
            //====================================================//
            
            
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
    
    
    public boolean CanDelete(int CompanyID,String sDocNo,int UserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            
            if(HistoryView) {
                return false;
            }
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_SAL_POLICY_PROCESS WHERE DOC_NO='"+sDocNo+"' AND (APPROVED=1)";
            int Count=data.getIntValueFromDB(strSQL);
            
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
    public boolean IsEditable(int CompanyID,String sDocNo,int UserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT POLICY_ID FROM D_SAL_POLICY_PROCESS WHERE DOC_NO='"+sDocNo+"' AND (APPROVED=1)";
            
            if(data.IsRecordExist(strSQL)) {
                return false;
            }
            else {
                strSQL="SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+sDocNo+"' AND USER_ID="+UserID+" AND STATUS='W'";
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
            String sDocNo=getAttribute("DOC_NO").getString();
            
            if(CanDelete(EITLERPGLOBAL.gCompanyID,sDocNo,UserID)) {
                String strSQL = "DELETE FROM D_SAL_POLICY_PROCESS WHERE DOC_NO='"+sDocNo+"'";
                data.Execute(strSQL);
                
                strSQL = "DELETE FROM D_SAL_POLICY_PROCESS WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID +" AND DOC_NO='"+sDocNo+"'";
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
    
    
    public Object getObject(int CompanyID, String sDocNo) {
        String strCondition = " WHERE DOC_NO='" + sDocNo + "' AND COMPANY_ID=" + CompanyID;
        clsCreditNoteProcessModule objProcess = new clsCreditNoteProcessModule();
        objProcess.Filter(strCondition,CompanyID);
        return objProcess;
    }
    
    
    
    
    public boolean Filter(String Condition,int CompanyID) {
        Ready=false;
        
        try {
            String strSQL = "SELECT * FROM D_SAL_POLICY_PROCESS " + Condition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSQL);
            
            if(!rsResultSet.first()) {
                strSQL = "SELECT * FROM D_SAL_POLICY_PROCESS ORDER BY DOC_NO ";
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
            setAttribute("DOC_NO",UtilFunctions.getString(rsResultSet,"DOC_NO", ""));
            setAttribute("DOC_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"DOC_DATE","0000-00-00")));
            setAttribute("POLICY_ID",UtilFunctions.getString(rsResultSet,"POLICY_ID", ""));
            setAttribute("FROM_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"FROM_DATE","0000-00-00")));
            setAttribute("TO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"TO_DATE","0000-00-00")));
            setAttribute("REMARKS",UtilFunctions.getString(rsResultSet,"REMARKS", ""));
            setAttribute("AMOUNT_SELECTION",UtilFunctions.getString(rsResultSet,"AMOUNT_SELECTION",""));
            
            setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsResultSet,"HIERARCHY_ID",0));
            setAttribute("FIN_HIERARCHY_ID",UtilFunctions.getInt(rsResultSet,"FIN_HIERARCHY_ID",0));
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
            
            colProcessInvDtl.clear();
            String DocNo = getAttribute("DOC_NO").getString();
            
            tmpStmt=tmpConn.createStatement();
            
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_SAL_POLICY_PROCESS_INV_H WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND DOC_NO='" + DocNo + "' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_SAL_POLICY_PROCESS_INV WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND DOC_NO='" + DocNo + "' ORDER BY SR_NO");
            }
            
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                
                Counter=Counter+1;
                clsPolicyInvoiceList objItem = new clsPolicyInvoiceList();
                
                objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                objItem.setAttribute("DOC_NO",UtilFunctions.getString(rsTmp,"DOC_NO",""));
                objItem.setAttribute("SR_NO",UtilFunctions.getInt(rsTmp,"SR_NO",0));
                objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                objItem.setAttribute("PARTY_CODE",UtilFunctions.getString(rsTmp,"PARTY_CODE",""));
                objItem.setAttribute("PARTY_NAME",UtilFunctions.getString(rsTmp,"PARTY_NAME",""));
                objItem.setAttribute("CHARGE_CODE",UtilFunctions.getString(rsTmp,"CHARGE_CODE",""));
                objItem.setAttribute("SELECTION",UtilFunctions.getBoolean(rsTmp,"SELECTION",false));
                objItem.setAttribute("QUALITY_NO",UtilFunctions.getString(rsTmp,"QUALITY_NO",""));
                objItem.setAttribute("PIECE_NO",UtilFunctions.getString(rsTmp,"PIECE_NO",""));
                
                objItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsTmp,"CREATED_BY",""));
                objItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CREATED_DATE","0000-00-00")));
                objItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsTmp,"MODIFIED_BY",""));
                objItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MODIFIED_DATE","0000-00-00")));
                objItem.setAttribute("CHANGED",true);
                objItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CHANGED_DATE","0000-00-00")));
                objItem.setAttribute("CANCELLED",0);
                colProcessInvDtl.put(Long.toString(Counter),objItem);
                rsTmp.next();
            }
            
            colProcessDtl.clear();
            DocNo = getAttribute("DOC_NO").getString();
            
            tmpStmt=tmpConn.createStatement();
            
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_SAL_POLICY_PROCESS_DETAIL_H WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND DOC_NO='" + DocNo + "' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_SAL_POLICY_PROCESS_DETAIL WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND DOC_NO='" + DocNo + "' ORDER BY SR_NO");
            }
            
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                
                Counter=Counter+1;
                clsCreditNoteProcessDetail objItem = new clsCreditNoteProcessDetail();
                
                objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                objItem.setAttribute("DOC_NO",UtilFunctions.getString(rsTmp,"DOC_NO",""));
                objItem.setAttribute("SR_NO",UtilFunctions.getInt(rsTmp,"SR_NO",0));
                objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                objItem.setAttribute("PARTY_ID",UtilFunctions.getString(rsTmp,"PARTY_ID",""));
                objItem.setAttribute("PARTY_MAIN_CODE",UtilFunctions.getString(rsTmp,"PARTY_MAIN_CODE",""));
                objItem.setAttribute("AMOUNT",UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                objItem.setAttribute("QUALIFING_AMOUNT",UtilFunctions.getDouble(rsTmp,"QUALIFYING_AMOUNT",0));
                objItem.setAttribute("PERCENTAGE",UtilFunctions.getDouble(rsTmp,"PERCENTAGE",0));
                objItem.setAttribute("CREDIT_NOTE_NO",UtilFunctions.getString(rsTmp,"CREDIT_NOTE_NO",""));
                objItem.setAttribute("SEASON_ID",UtilFunctions.getString(rsTmp,"SEASON_ID",""));
                objItem.setAttribute("PIECE_NO",UtilFunctions.getString(rsTmp,"PIECE_NO",""));
                objItem.setAttribute("QUALITY_ID",UtilFunctions.getString(rsTmp,"QUALITY_ID",""));
                objItem.setAttribute("DISCOUNT_AMOUNT",UtilFunctions.getDouble(rsTmp,"DISCOUNT_AMOUNT",0));
                objItem.setAttribute("VOUCHER_TYPE",UtilFunctions.getInt(rsTmp,"VOUCHER_TYPE",0));
                objItem.setAttribute("BOOK_CODE",UtilFunctions.getString(rsTmp,"BOOK_CODE",""));
                objItem.setAttribute("DEDUCTION_CODE",UtilFunctions.getString(rsTmp,"DEDUCTION_CODE",""));
                
                objItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsTmp,"CREATED_BY",""));
                objItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CREATED_DATE","0000-00-00")));
                objItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsTmp,"MODIFIED_BY",""));
                objItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MODIFIED_DATE","0000-00-00")));
                objItem.setAttribute("CHANGED",true);
                objItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CHANGED_DATE","0000-00-00")));
                objItem.setAttribute("CANCELLED",0);
                colProcessDtl.put(Long.toString(Counter),objItem);
                rsTmp.next();
            }
            
            colProcessTurnOverDtl.clear();
            DocNo = getAttribute("DOC_NO").getString();
            
            tmpStmt=tmpConn.createStatement();
            
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_SAL_POLICY_PROCESS_TURNOVER_H WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND DOC_NO='" + DocNo + "' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_SAL_POLICY_PROCESS_TURNOVER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND DOC_NO='" + DocNo + "' ORDER BY SR_NO");
            }
            
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                
                Counter=Counter+1;
                clsPolicyTurnOverList objItem = new clsPolicyTurnOverList();
                
                objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                objItem.setAttribute("DOC_NO",UtilFunctions.getString(rsTmp,"DOC_NO",""));
                objItem.setAttribute("SR_NO",UtilFunctions.getInt(rsTmp,"SR_NO",0));
                objItem.setAttribute("PARTY_CODE",UtilFunctions.getString(rsTmp,"PARTY_CODE",""));
                objItem.setAttribute("YEAR",UtilFunctions.getInt(rsTmp,"YEAR",0));
                objItem.setAttribute("TURNOVER_AMOUNT",UtilFunctions.getDouble(rsTmp,"TURNOVER_AMOUNT",0));
                
                objItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsTmp,"CREATED_BY",""));
                objItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CREATED_DATE","0000-00-00")));
                objItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsTmp,"MODIFIED_BY",""));
                objItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MODIFIED_DATE","0000-00-00")));
                objItem.setAttribute("CHANGED",true);
                objItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CHANGED_DATE","0000-00-00")));
                objItem.setAttribute("CANCELLED",0);
                colProcessTurnOverDtl.put(Long.toString(Counter),objItem);
                rsTmp.next();
            }
            
            
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
                strSQL="SELECT D_SAL_POLICY_PROCESS.DOC_NO,D_SAL_POLICY_PROCESS.POLICY_ID,D_COM_DOC_DATA.RECEIVED_DATE FROM D_SAL_POLICY_PROCESS,D_COM_DOC_DATA WHERE D_SAL_POLICY_PROCESS.DOC_NO=D_COM_DOC_DATA.DOC_NO AND D_COM_DOC_DATA.USER_ID="+UserID+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            
            if(Order==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT D_SAL_POLICY_PROCESS.DOC_NO,D_SAL_POLICY_PROCESS.POLICY_ID,D_COM_DOC_DATA.RECEIVED_DATE FROM D_SAL_POLICY_PROCESS,D_COM_DOC_DATA WHERE D_SAL_POLICY_PROCESS.DOC_NO=D_COM_DOC_DATA.DOC_NO AND D_COM_DOC_DATA.USER_ID="+UserID+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_SAL_POLICY_PROCESS.DOC_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT D_SAL_POLICY_PROCESS.DOC_NO,D_SAL_POLICY_PROCESS.POLICY_ID,D_COM_DOC_DATA.RECEIVED_DATE FROM D_SAL_POLICY_PROCESS,D_COM_DOC_DATA WHERE D_SAL_POLICY_PROCESS.DOC_NO=D_COM_DOC_DATA.DOC_NO AND D_COM_DOC_DATA.USER_ID="+UserID+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_SAL_POLICY_PROCESS.DOC_NO";
            }
            
            rsTemp=data.getResult(strSQL);
            rsTemp.first();
            
            Counter=0;
            
            if(rsTemp.getRow()>0) {
                while(!rsTemp.isAfterLast()) {
                    Counter=Counter+1;
                    clsCreditNoteProcessModule ObjPolicy=new clsCreditNoteProcessModule();
                    
                    //------------- Header Fields --------------------//
                    ObjPolicy.setAttribute("DOC_NO",UtilFunctions.getString(rsTemp,"DOC_NO",""));
                    ObjPolicy.setAttribute("POLICY_ID",UtilFunctions.getString(rsTemp,"POLICY_ID",""));
                    ObjPolicy.setAttribute("RECEIVED_DATE",UtilFunctions.getString(rsTemp,"RECEIVED_DATE","0000-00-00"));
                    // ----------------- End of Header Fields ------------------------------------//
                    
                    List.put(Long.toString(Counter),ObjPolicy);
                    rsTemp.next();
                }
            }
            rsTemp.close();
            
        }
        catch(Exception e) {
        }
        
        return List;
    }
    
    
    public boolean ShowHistory(int CompanyID,String sDocNo) {
        Ready=false;
        try {
            String strSQL="SELECT * FROM D_SAL_POLICY_PROCESS WHERE DOC_NO='"+sDocNo + "'";
            rsResultSet=data.getResult(strSQL);
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
    
    
    public static HashMap getHistoryList(int CompanyID,String sDocNo) {
        HashMap List=new HashMap();
        ResultSet rsTmp;
        
        try {
            String strSQL="SELECT * FROM D_SAL_POLICY_PROCESS_H WHERE DOC_NO='"+sDocNo+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsCreditNoteProcessModule objPolicy=new clsCreditNoteProcessModule();
                    
                    objPolicy.setAttribute("DOC_NO",UtilFunctions.getString(rsTmp,"DOC_NO", ""));
                    objPolicy.setAttribute("REVISION_NO",UtilFunctions.getInt(rsTmp,"REVISION_NO", 0));
                    objPolicy.setAttribute("UPDATED_BY",UtilFunctions.getString(rsTmp,"UPDATED_BY", ""));
                    objPolicy.setAttribute("APPROVAL_STATUS",UtilFunctions.getString(rsTmp,"APPROVAL_STATUS", ""));
                    objPolicy.setAttribute("ENTRY_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"ENTRY_DATE","0000-00-00")));
                    objPolicy.setAttribute("APPROVER_REMARKS",UtilFunctions.getString(rsTmp,"APPROVER_REMARKS", ""));
                    
                    List.put(Integer.toString(List.size()+1),objPolicy);
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
    
    
    public static String getDocStatus(int CompanyID,String sDocNo) {
        ResultSet rsTmp;
        String strMessage="";
        
        try {
            
            String strSQL="SELECT DOC_NO,APPROVED,CANCELLED FROM D_SAL_POLICY_PROCESS WHERE DOC_NO='"+sDocNo+"'";
            rsTmp=data.getResult(strSQL);
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
    
    
    public static boolean CanCancel(int CompanyID,String sDocNo) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            rsTmp=data.getResult("SELECT POLICY_ID FROM D_SAL_POLICY_PROCESS WHERE DOC_NO='"+sDocNo+"' AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                
                String sDoc_No=UtilFunctions.getString(rsTmp,"DOC_NO","");
                
                if(data.IsRecordExist("SELECT * FROM D_SAL_POLICY_PROCESS WHERE CANCELLED=0 AND COMPANY_ID="+CompanyID+" AND DOC_NO='"+sDoc_No+"'")) {
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
    
    
    
    public static boolean CancelDoc(int CompanyID,String sDocNo) {
        
        ResultSet rsTmp=null;
        boolean Cancelled=false;
        
        try {
            if(CanCancel(CompanyID,sDocNo)) {
                
                boolean ApprovedDoc=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_SAL_POLICY_PROCESS WHERE DOC_NO="+sDocNo);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedDoc) {
                    
                }
                else {
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+sDocNo+"' AND MODULE_ID="+ModuleID);
                }
                
                data.Execute("UPDATE D_SAL_POLICY_PROCESS SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO="+sDocNo);
                
                Cancelled=true;
            }
            
            rsTmp.close();
        }
        catch(Exception e) {
            
        }
        
        return Cancelled;
    }
    
    public static HashMap getPartyList(String PolicyID,String FromDate,String ToDate,HashMap InvoiceList,String Amount) {
        HashMap List=new HashMap();
        try {
            
            String str = "",PartyID="";
            int VoucherType=0;
            ResultSet rsPolicy;
            FromDate = EITLERPGLOBAL.formatDateDB(FromDate);
            ToDate = EITLERPGLOBAL.formatDateDB(ToDate);
            
            str = "SELECT POLICY_ID,POLICY_NAME,DISCOUNT_TYPE,PARTY_TYPE,INVOICE_TYPES, APPLICABILITY, "+
            "APP_AMOUNT_LIMIT,FLAT_PERCENTAGE,DISCOUNT_PERCENTAGE,EFFECTIVE_DATE,EXPIRY_DATE, "+
            "TURNOVER_CALC_TYPE,DEDUCTION_CODES,CREDIT_NOTE_TYPE,INVOICE_MAIN_TYPE "+
            "FROM D_SAL_POLICY_MASTER "+
            "WHERE COMPANY_ID="+ EITLERPGLOBAL.gCompanyID +" AND POLICY_ID='"+ PolicyID +"' " +
            "AND EFFECTIVE_DATE <= '"+ FromDate +"' "+
            "AND IF (EXPIRY_DATE='0000-00-00','"+ ToDate +"',EXPIRY_DATE) >= '"+ ToDate +"' ";
            //"AND EXPIRY_DATE >= '"+ ToDate +"'";
            rsPolicy = data.getResult(str);
            
            if (rsPolicy.getRow() > 0) {
                rsPolicy.first();
                ResultSet rsParty,rsInvoice,rsInvDetail,rsPartyGroup;
                String URL="";
                
                for(int i=1;i<=InvoiceList.size();i++) {
                    clsPolicyInvoiceList ObjItem=(clsPolicyInvoiceList) InvoiceList.get(Integer.toString(i));
                    
                    String InvNo = (String)ObjItem.getAttribute("INVOICE_NO").getObj();
                    String InvQualityNo = (String)ObjItem.getAttribute("QUALITY_NO").getObj();
                    String InvPieceNo = (String)ObjItem.getAttribute("PIECE_NO").getObj();
                    
                    str = "SELECT * FROM D_SAL_INVOICE_HEADER WHERE INVOICE_NO = '"+InvNo+"' AND INVOICE_DATE >= '" + FromDate + "' AND INVOICE_DATE <= '" + ToDate + "' ";
                    rsInvoice = data.getResult(str);
                    
                    if (rsInvoice.getRow() > 0) {
                        rsInvoice.first();
                        int cntInvoice = 0;
                        
                        while (! rsInvoice.isAfterLast()) {
                            cntInvoice += 1;
                            String InvoiceNo = rsInvoice.getString("INVOICE_NO").trim();
                            String InvoiceDate = rsInvoice.getString("INVOICE_DATE").trim();
                            String InvParty = rsInvoice.getString("PARTY_CODE").trim();
                            double Percentage=0;
                            double InvAmount =rsInvoice.getDouble(Amount);
                            
                            int cr_type = rsPolicy.getInt("CREDIT_NOTE_TYPE");
                            if ((cr_type == 2) || (cr_type == 3)) { //LC Credit Note
                                str = "SELECT A.POLICY_ID,A.PARTY_ID,A.PARTY_MAIN_CODE ,B.CHILD_PARTY_ID "+
                                " FROM D_SAL_POLICY_PARTIES A,D_SAL_POLICY_LC_GROUPING_MASTER B "+
                                " WHERE A.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND A.POLICY_ID='"+ PolicyID +"' "+
                                " AND A.COMPANY_ID=B.COMPANY_ID AND A.PARTY_ID=B.PARENT_PARTY_ID "+
                                " AND B.CHILD_PARTY_ID='"+ InvParty +"' ";
                            }
                            else {
                                str = "SELECT POLICY_ID,PARTY_ID,PARTY_MAIN_CODE FROM D_SAL_POLICY_PARTIES " +
                                "WHERE COMPANY_ID="+ EITLERPGLOBAL.gCompanyID +" AND POLICY_ID='"+ PolicyID +"' " +
                                " AND PARTY_ID='"+ InvParty +"'  ";
                            }
                            URL = EITLERPGLOBAL.DatabaseURL;
                            
                            rsParty = data.getResult(str,URL);
                            
                            if (rsParty.getRow() > 0) {
                                rsParty.first();
                                VoucherType = FinanceGlobal.TYPE_CREDIT_NOTE;
                                
                                //if (rsPolicy.getString("DISCOUNT_PERCENTAGE").trim().equals("2")) {
                                str = "SELECT * FROM D_SAL_INVOICE_DETAIL WHERE INVOICE_NO='" + InvoiceNo + "' AND QUALITY_NO='"+InvQualityNo+"' AND PIECE_NO='"+InvPieceNo+"' AND INVOICE_DATE >= '" + FromDate + "' AND INVOICE_DATE <= '" + ToDate + "' ";
                                rsInvDetail = data.getResult(str);
                                
                                if (rsInvDetail.getRow() > 0) {
                                    rsInvDetail.first();
                                    int cnt=0;
                                    
                                    while (! rsInvDetail.isAfterLast()) {
                                        
                                        InvAmount = Double.parseDouble(rsInvDetail.getString(Amount).trim());
                                        String QualityID = UtilFunctions.getString(rsInvDetail,"QUALITY_NO", "");
                                        String PieceNo = UtilFunctions.getString(rsInvDetail,"PIECE_NO", "");
                                        String SeasonID = rsInvDetail.getString("SEASON_CODE").trim();
                                        
                                        if (rsPolicy.getString("DISCOUNT_PERCENTAGE").trim().equals("0")) {
                                            Percentage = UtilFunctions.getDouble(rsPolicy,"FLAT_PERCENTAGE", 0);
                                        }
                                        else if (rsPolicy.getString("DISCOUNT_PERCENTAGE").trim().equals("1")) {
                                            str = "SELECT PERCENTAGE FROM D_SAL_POLICY_TURNOVER_SLABS "+
                                            "WHERE COMPANY_ID="+ EITLERPGLOBAL.gCompanyID +" AND POLICY_ID='"+ PolicyID +"' " +
                                            "AND FROM_VALUE >=" + InvAmount + " AND TO_VALUE <=" + InvAmount;
                                            //"FROM_VALUE,TO_VALUE
                                            Percentage = data.getDoubleValueFromDB(str);
                                        }
                                        else if (rsPolicy.getString("DISCOUNT_PERCENTAGE").trim().equals("2")) {
                                            
                                            str = "SELECT PERCENTAGE FROM  D_SAL_POLICY_SEASON_SLABS " +
                                            "WHERE COMPANY_ID="+ EITLERPGLOBAL.gCompanyID +" AND POLICY_ID='"+ PolicyID +"' "+
                                            " AND FROM_DATE >='" + InvoiceDate + "' AND TO_DATE <= '" + InvoiceDate + "' " +
                                            " AND QUALITY_ID = '" + QualityID +"' " +
                                            " AND SEASON_ID ='" + SeasonID + "' ";
                                            //"SEASON_ID,QUALITY_ID,FROM_DATE,TO_DATE
                                            Percentage = data.getDoubleValueFromDB(str);
                                        }
                                        double Discount_Amt = 0;
                                        int crtype = rsPolicy.getInt("CREDIT_NOTE_TYPE");
                                        String PartyCode = UtilFunctions.getString(rsParty,"PARTY_ID", "");
                                        int turnovercalctype = rsPolicy.getInt("TURNOVER_CALC_TYPE");
                                        Discount_Amt = InvAmount;
                                        
                                        if (turnovercalctype == 0) {
                                            str = "SELECT * FROM D_SAL_POLICY_PARTY_GROUPING WHERE PARENT_PARTY_ID='" + PartyCode + "' AND COMPANY_ID=" + EITLERPGLOBAL.gCompanyID;
                                            rsPartyGroup = data.getResult(str);
                                            if (rsPartyGroup.getRow() > 0) {
                                                rsPartyGroup.first();
                                                Discount_Amt = 0;
                                                Discount_Amt = InvAmount;
                                                while (! rsPartyGroup.isAfterLast()) {
                                                    String ChildParty = rsPartyGroup.getString("CHILD_PARTY_ID");
                                                    for(int j=1;j<=InvoiceList.size();j++) {
                                                        clsPolicyInvoiceList ObjInvItem=(clsPolicyInvoiceList) InvoiceList.get(Integer.toString(i));
                                                        String Inv_No = (String)ObjInvItem.getAttribute("INVOICE_NO").getObj();
                                                        String Party_Code = (String)ObjInvItem.getAttribute("PARTY_CODE").getObj();
                                                        double Amt = data.getDoubleValueFromDB("SELECT "+ Amount +" FROM D_SAL_INVOICE_HEADER WHERE INVOICE_NO='"+ Inv_No + "' AND PARTY_CODE='" + Party_Code + "' ");
                                                        Discount_Amt = Discount_Amt + Amt;
                                                    }                                                    
                                                    rsPartyGroup.next();
                                                }
                                            }
                                            else {
                                                Discount_Amt = Discount_Amt;
                                            }
                                        }
                                        else if (turnovercalctype == 1) {
                                            Discount_Amt = Discount_Amt;
                                        }
                                        
                                        if ((crtype == 2) || (crtype == 3)) { //LC Credit Note
                                            //Discount_Amt = 0;
                                            //InvAmount,PartyCode
                                            String RCInvNo="";
                                            double RCAmt=0,SJAmt=0;
                                            String RCDate="",InvDate="";
                                            
                                            str = "SELECT VOUHEAD.VOUCHER_NO,VOUHEAD.VOUCHER_DATE,VOUDTL.AMOUNT,VOUDTL.INVOICE_NO,VOUDTL.INVOICE_DATE " +
                                            "FROM D_FIN_VOUCHER_HEADER VOUHEAD, D_FIN_VOUCHER_DETAIL VOUDTL "+
                                            "WHERE VOUHEAD.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND VOUHEAD.VOUCHER_TYPE= "+ FinanceGlobal.TYPE_RECEIPT + " " +
                                            "AND VOUHEAD.APPROVED=1 AND VOUHEAD.CANCELLED=0  "+
                                            "AND VOUHEAD.COMPANY_ID=VOUDTL.COMPANY_ID AND VOUHEAD.VOUCHER_NO=VOUDTL.VOUCHER_NO "+
                                            "AND VOUHEAD.VOUCHER_DATE >= '"+FromDate+"' AND VOUHEAD.VOUCHER_DATE <= '"+ToDate+"' "+
                                            "AND VOUDTL.SUB_ACCOUNT_CODE = '"+PartyCode+"' ";
                                            ResultSet rsRCInv = data.getResult(str,FinanceGlobal.FinURL);
                                            if (rsRCInv.getRow() > 0) {
                                                rsRCInv.first();
                                                RCInvNo=rsRCInv.getString("INVOICE_NO");
                                                RCAmt=rsRCInv.getDouble("AMOUNT");
                                                RCDate=rsRCInv.getString("VOUCHER_DATE").trim();
                                                InvDate=InvoiceDate;
                                            }
                                            rsRCInv.close();
                                            
                                            str = "SELECT VOUDTL.AMOUNT "+
                                            "FROM D_FIN_VOUCHER_HEADER VOUHEAD, D_FIN_VOUCHER_DETAIL VOUDTL "+
                                            "WHERE VOUHEAD.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND VOUHEAD.VOUCHER_TYPE= "+ FinanceGlobal.TYPE_SALES_JOURNAL + " " +
                                            "AND VOUHEAD.APPROVED=1 AND VOUHEAD.CANCELLED=0  "+
                                            "AND VOUHEAD.COMPANY_ID=VOUDTL.COMPANY_ID AND VOUHEAD.VOUCHER_NO=VOUDTL.VOUCHER_NO "+
                                            "AND VOUHEAD.VOUCHER_DATE >= '"+FromDate+"' AND VOUHEAD.VOUCHER_DATE <= '"+ToDate+"' "+
                                            "AND VOUDTL.INVOICE_NO = '"+RCInvNo+"'";
                                            
                                            SJAmt = data.getDoubleValueFromDB(str,FinanceGlobal.FinURL);
                                            
                                            if ((RCAmt!=0) && (SJAmt!=0)) {
                                                if (RCAmt == SJAmt) {
                                                    //Discount_Amt = InvAmount;
                                                    int diffInDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(InvDate),java.sql.Date.valueOf(RCDate));
                                                    if (diffInDays <= 15) { //full discount
                                                        Discount_Amt = Discount_Amt * Percentage;
                                                    }
                                                    else if ((diffInDays >= 16) && (diffInDays <= 30)) { // 50% Discount
                                                        Percentage = Percentage * 0.50;
                                                        Discount_Amt = (Discount_Amt * Percentage)/100;
                                                    }
                                                    else if ((diffInDays >= 31) && (diffInDays <= 45)) { // Nill Discount
                                                        Percentage = 0;
                                                        Discount_Amt = Discount_Amt;
                                                    }
                                                    else if (diffInDays > 45) { // 18% Debit Note Raise
                                                        Percentage = -0.18;
                                                        Discount_Amt = (((Discount_Amt * 0.18) / 365) * diffInDays);
                                                        VoucherType = FinanceGlobal.TYPE_DEBIT_NOTE;
                                                        //PostDebitNoteVoucher();
                                                    }
                                                }
                                                else {
                                                    Discount_Amt=0;
                                                }
                                            }
                                            else {
                                                //Discount_Amt = (Discount_Amt * Percentage)/100;
                                                Discount_Amt=0;
                                            }
                                            
                                        }
                                        //                                            else if (crtype == 1) { //Cash Discount Credit Note
                                        //                                                Discount_Amt = (InvAmount * Percentage)/100;
                                        //                                            }
                                        else {
                                            Discount_Amt = (Discount_Amt * Percentage)/100;
                                        }
                                        
                                        
                                        int Discount_App = rsPolicy.getInt("APPLICABILITY");
                                        PartyID = "";
                                        if (Discount_App == 0) {
                                            if (rsPolicy.getDouble("APP_AMOUNT_LIMIT") >= InvAmount) {
                                                str = "SELECT RCDTL.SUB_ACCOUNT_CODE " +
                                                "FROM D_FIN_VOUCHER_HEADER LCHEAD,D_FIN_VOUCHER_DETAIL LCDTL,D_FIN_VOUCHER_DETAIL RCDTL  "+
                                                "WHERE LCHEAD.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND LCHEAD.APPROVED=1 "+
                                                "AND LCHEAD.CANCELLED=0 AND LCHEAD.VOUCHER_TYPE="+ FinanceGlobal.TYPE_LC_JV + " " +
                                                "AND LCHEAD.COMPANY_ID=LCDTL.COMPANY_ID AND LCHEAD.VOUCHER_NO=LCDTL.VOUCHER_NO "+
                                                "AND LCDTL.SUB_ACCOUNT_CODE='"+PartyCode+"' "+
                                                "AND LCDTL.COMPANY_ID=RCDTL.COMPANY_ID AND LCDTL.GRN_NO=RCDTL.VOUCHER_NO ";
                                                PartyID = data.getStringValueFromDB(str,FinanceGlobal.FinURL);
                                            }
                                        }
                                        else if (Discount_App == 1) {
                                            PartyID = data.getStringValueFromDB("SELECT PARENT_PARTY_ID FROM D_SAL_POLICY_PARTY_GROUPING WHERE CHILD_PARTY_ID='" + PartyCode + "' AND COMPANY_ID=" + EITLERPGLOBAL.gCompanyID);
                                        }
                                        else if (Discount_App == 2) {
                                            PartyID = PartyCode;
                                        }
                                        
                                        int CreditNoteType = rsPolicy.getInt("CREDIT_NOTE_TYPE");
                                        String BookCode = "";
                                        String DeductionCode = "";
                                        
                                        if (Discount_Amt!=0) {
                                            clsCreditNoteProcessDetail objProcessDtl=new clsCreditNoteProcessDetail();
                                            cnt ++;
                                            objProcessDtl.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                                            objProcessDtl.setAttribute("SR_NO",cnt);
                                            objProcessDtl.setAttribute("INVOICE_NO",InvoiceNo);
                                            objProcessDtl.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
                                            objProcessDtl.setAttribute("PARTY_ID",PartyID);
                                            int InvMainType = rsPolicy.getInt("INVOICE_MAIN_TYPE");
                                            String MainCode="";
                                            
                                            if (InvMainType == 0){//suiting
                                                MainCode="210027";
                                            }
                                            else if (InvMainType == 1) {//felt
                                                MainCode="210010";
                                            }
                                            else if (InvMainType == 2) {//filter
                                                MainCode="210072";
                                            }
                                            if (! clsAccount.IsValidAccount(MainCode,PartyID)) {
                                                JOptionPane.showMessageDialog(null,"Main Account Code/Sub Account Code is not valid. Please verify.");
                                                List = new HashMap();
                                                return List;
                                            }
                                            objProcessDtl.setAttribute("PARTY_MAIN_CODE",MainCode);
                                            objProcessDtl.setAttribute("AMOUNT",InvAmount);
                                            objProcessDtl.setAttribute("QUALIFING_AMOUNT",InvAmount);
                                            objProcessDtl.setAttribute("PERCENTAGE",Percentage);
                                            objProcessDtl.setAttribute("QUALITY_ID",QualityID);
                                            objProcessDtl.setAttribute("PIECE_NO",PieceNo);
                                            objProcessDtl.setAttribute("SEASON_ID",SeasonID);
                                            objProcessDtl.setAttribute("DISCOUNT_AMOUNT",Discount_Amt);
                                            objProcessDtl.setAttribute("VOUCHER_TYPE",VoucherType);
                                            objProcessDtl.setAttribute("BOOK_CODE",BookCode);
                                            objProcessDtl.setAttribute("DEDUCTION_CODE",DeductionCode);
                                            
                                            List.put(Integer.toString(List.size()+1),objProcessDtl);
                                        }
                                        
                                        rsInvDetail.next();
                                        
                                    }
                                    rsInvDetail.close();
                                }
                                //}
                                //                                else {
                                //
                                //                                    str = "SELECT * FROM D_SAL_INVOICE_DETAIL WHERE INVOICE_NO='" + InvoiceNo + "' AND QUALITY_NO='"+InvQualityNo+"' AND PIECE_NO='"+InvPieceNo+"' AND INVOICE_DATE >= '" + FromDate + "' AND INVOICE_DATE <= '" + ToDate + "' ";
                                //                                    rsInvDetail = data.getResult(str);
                                //
                                //                                    if (rsInvDetail.getRow() > 0) {
                                //                                        rsInvDetail.first();
                                //                                        int cnt=0;
                                //
                                //                                        while (! rsInvDetail.isAfterLast()) {
                                //
                                //                                            InvAmount = Double.parseDouble(rsInvDetail.getString(Amount).trim());
                                //
                                //
                                //                                            if (rsPolicy.getString("DISCOUNT_PERCENTAGE").trim().equals("0")) {
                                //                                                Percentage = UtilFunctions.getDouble(rsPolicy,"FLAT_PERCENTAGE", 0);
                                //                                            }
                                //                                            else if (rsPolicy.getString("DISCOUNT_PERCENTAGE").trim().equals("1")) {
                                //                                                str = "SELECT PERCENTAGE FROM D_SAL_POLICY_TURNOVER_SLABS "+
                                //                                                "WHERE COMPANY_ID="+ EITLERPGLOBAL.gCompanyID +" AND POLICY_ID='"+ PolicyID +"' " +
                                //                                                "AND FROM_VALUE >=" + InvAmount + " AND TO_VALUE <=" + InvAmount;
                                //                                                //"FROM_VALUE,TO_VALUE
                                //                                                Percentage = data.getDoubleValueFromDB(str);
                                //                                            }
                                //
                                //                                            double Discount_Amt = 0;
                                //                                            int crtype = rsPolicy.getInt("CREDIT_NOTE_TYPE");
                                //                                            String PartyCode = UtilFunctions.getString(rsParty,"PARTY_ID", "");
                                //                                            int turnovercalctype = rsPolicy.getInt("TURNOVER_CALC_TYPE");
                                //                                            Discount_Amt = InvAmount;
                                //
                                //                                            if (turnovercalctype == 0) {
                                //                                                str = "SELECT * FROM D_SAL_POLICY_PARTY_GROUPING WHERE PARENT_PARTY_ID='" + PartyCode + "' AND COMPANY_ID=" + EITLERPGLOBAL.gCompanyID;
                                //                                                rsPartyGroup = data.getResult(str);
                                //                                                if (rsPartyGroup.getRow() > 0) {
                                //                                                    rsPartyGroup.first();
                                //                                                    Discount_Amt = 0;
                                //                                                    Discount_Amt = InvAmount;
                                //                                                    while (! rsPartyGroup.isAfterLast()) {
                                //                                                        String ChildParty = rsPartyGroup.getString("CHILD_PARTY_ID");
                                //                                                        for(int j=1;j<=InvoiceList.size();j++) {
                                //                                                            clsPolicyInvoiceList ObjInvItem=(clsPolicyInvoiceList) InvoiceList.get(Integer.toString(i));
                                //                                                            String Inv_No = (String)ObjInvItem.getAttribute("INVOICE_NO").getObj();
                                //                                                            String Party_Code = (String)ObjInvItem.getAttribute("PARTY_CODE").getObj();
                                //                                                            double Amt = data.getDoubleValueFromDB("SELECT "+ Amount +" FROM D_SAL_INVOICE_HEADER WHERE INVOICE_NO='"+ Inv_No + "' AND PARTY_CODE='" + Party_Code + "' ");
                                //                                                            Discount_Amt = Discount_Amt + Amt;
                                //                                                        }
                                //
                                //                                                        rsPartyGroup.next();
                                //                                                    }
                                //                                                }
                                //                                                else {
                                //                                                    Discount_Amt = Discount_Amt;
                                //                                                }
                                //                                            }
                                //                                            else if (turnovercalctype == 1) {
                                //                                                Discount_Amt = Discount_Amt;
                                //                                            }
                                //
                                //                                            if ((crtype == 2) || (crtype == 3)) { //LC Credit Note
                                //                                                //Discount_Amt = 0;
                                //                                                //InvAmount,PartyCode
                                //                                                String RCInvNo="";
                                //                                                double RCAmt=0,SJAmt=0;
                                //                                                String RCDate="",InvDate="";
                                //
                                //                                                str = "SELECT VOUHEAD.VOUCHER_NO,VOUHEAD.VOUCHER_DATE,VOUDTL.AMOUNT,VOUDTL.INVOICE_NO,VOUDTL.INVOICE_DATE " +
                                //                                                "FROM D_FIN_VOUCHER_HEADER VOUHEAD, D_FIN_VOUCHER_DETAIL VOUDTL "+
                                //                                                "WHERE VOUHEAD.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND VOUHEAD.VOUCHER_TYPE="+ FinanceGlobal.TYPE_RECEIPT + " " +
                                //                                                "AND VOUHEAD.APPROVED=1 AND VOUHEAD.CANCELLED=0  "+
                                //                                                "AND VOUHEAD.COMPANY_ID=VOUDTL.COMPANY_ID AND VOUHEAD.VOUCHER_NO=VOUDTL.VOUCHER_NO "+
                                //                                                "AND VOUHEAD.VOUCHER_DATE >= '"+FromDate+"' AND VOUHEAD.VOUCHER_DATE <= '"+ToDate+"' "+
                                //                                                "AND VOUDTL.SUB_ACCOUNT_CODE = '"+PartyCode+"' ";
                                //                                                ResultSet rsRCInv = data.getResult(str,FinanceGlobal.FinURL);
                                //                                                if (rsRCInv.getRow() > 0) {
                                //                                                    rsRCInv.first();
                                //                                                    RCInvNo=rsRCInv.getString("INVOICE_NO");
                                //                                                    RCAmt=rsRCInv.getDouble("AMOUNT");
                                //                                                    RCDate=rsRCInv.getString("VOUCHER_DATE").trim();
                                //                                                    InvDate=InvoiceDate;
                                //
                                //                                                    str = "SELECT VOUDTL.AMOUNT "+
                                //                                                    "FROM D_FIN_VOUCHER_HEADER VOUHEAD, D_FIN_VOUCHER_DETAIL VOUDTL "+
                                //                                                    "WHERE VOUHEAD.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND VOUHEAD.VOUCHER_TYPE="+ FinanceGlobal.TYPE_SALES_JOURNAL + " " +
                                //                                                    "AND VOUHEAD.APPROVED=1 AND VOUHEAD.CANCELLED=0  "+
                                //                                                    "AND VOUHEAD.COMPANY_ID=VOUDTL.COMPANY_ID AND VOUHEAD.VOUCHER_NO=VOUDTL.VOUCHER_NO "+
                                //                                                    "AND VOUHEAD.VOUCHER_DATE >= '"+FromDate+"' AND VOUHEAD.VOUCHER_DATE <= '"+ToDate+"' "+
                                //                                                    "AND VOUDTL.INVOICE_NO = '"+RCInvNo+"'";
                                //
                                //                                                    SJAmt = data.getDoubleValueFromDB(str,FinanceGlobal.FinURL);
                                //
                                //                                                }
                                //                                                rsRCInv.close();
                                //
                                //                                                if ((RCAmt!=0) && (SJAmt!=0)) {
                                //                                                    if (RCAmt == SJAmt) {
                                //                                                        //Discount_Amt = InvAmount;
                                //                                                        int diffInDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(InvDate),java.sql.Date.valueOf(RCDate));
                                //                                                        if (diffInDays <= 15) { //full discount
                                //                                                            Percentage = Percentage;
                                //                                                            Discount_Amt = (Discount_Amt * Percentage)/100;
                                //                                                        }
                                //                                                        else if ((diffInDays >= 16) && (diffInDays <= 30)) { // 50% Discount
                                //                                                            Percentage = Percentage * 0.50;
                                //                                                            Discount_Amt = (Discount_Amt * Percentage)/100;
                                //                                                        }
                                //                                                        else if ((diffInDays >= 31) && (diffInDays <= 45)) { // Nill Discount
                                //                                                            Percentage = 0;
                                //                                                            Discount_Amt = Discount_Amt;
                                //                                                        }
                                //                                                        else if (diffInDays > 45) { // 18% Debit Note Raise
                                //                                                            Percentage = -0.18;
                                //                                                            Discount_Amt = (((Discount_Amt * 0.18) / 365) * diffInDays);
                                //                                                            VoucherType = clsVoucher.DebitNoteModuleID;
                                //                                                            //PostDebitNoteVoucher();
                                //                                                        }
                                //                                                    }
                                //                                                    else {
                                //                                                        Discount_Amt=0;
                                //                                                    }
                                //                                                }
                                //                                                else {
                                //                                                    //Discount_Amt = (Discount_Amt * Percentage)/100;
                                //                                                    Discount_Amt=0;
                                //                                                }
                                //
                                //                                            }
                                //                                            else {
                                //                                                Discount_Amt = (Discount_Amt * Percentage)/100;
                                //                                            }
                                //
                                //                                            int Discount_App = rsPolicy.getInt("APPLICABILITY");
                                //                                            PartyID = "";
                                //                                            if (Discount_App == 0) {
                                //                                                if (rsPolicy.getDouble("APP_AMOUNT_LIMIT") >= InvAmount) {
                                //                                                    str = "SELECT RCDTL.SUB_ACCOUNT_CODE " +
                                //                                                    "FROM D_FIN_VOUCHER_HEADER LCHEAD,D_FIN_VOUCHER_DETAIL LCDTL,D_FIN_VOUCHER_DETAIL RCDTL  "+
                                //                                                    "WHERE LCHEAD.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND LCHEAD.APPROVED=1 "+
                                //                                                    "AND LCHEAD.CANCELLED=0 AND LCHEAD.VOUCHER_TYPE="+ FinanceGlobal.TYPE_LC_JV + " " +
                                //                                                    "AND LCHEAD.COMPANY_ID=LCDTL.COMPANY_ID AND LCHEAD.VOUCHER_NO=LCDTL.VOUCHER_NO "+
                                //                                                    "AND LCDTL.SUB_ACCOUNT_CODE='"+PartyCode+"' "+
                                //                                                    "AND LCDTL.COMPANY_ID=RCDTL.COMPANY_ID AND LCDTL.GRN_NO=RCDTL.VOUCHER_NO ";
                                //                                                    PartyID = data.getStringValueFromDB(str,FinanceGlobal.FinURL);
                                //                                                }
                                //                                            }
                                //                                            else if (Discount_App == 1) {
                                //                                                PartyID = data.getStringValueFromDB("SELECT PARENT_PARTY_ID FROM D_SAL_POLICY_PARTY_GROUPING WHERE CHILD_PARTY_ID='" + PartyCode + "' AND COMPANY_ID=" + EITLERPGLOBAL.gCompanyID);
                                //                                            }
                                //                                            else if (Discount_App == 2) {
                                //                                                PartyID = PartyCode;
                                //                                            }
                                //
                                //                                            int CreditNoteType = rsPolicy.getInt("CREDIT_NOTE_TYPE");
                                //                                            String BookCode = "";
                                //                                            String DeductionCode = "";
                                //
                                //                                            //                                            if ((CreditNoteType==1) || (CreditNoteType==2) || (CreditNoteType==3) || (CreditNoteType==4) || (CreditNoteType==5) || (CreditNoteType==6) || (CreditNoteType==20)) {
                                //                                            //                                                BookCode = "14";
                                //                                            //                                                DeductionCode = "435091";
                                //                                            //                                            }
                                //                                            //                                            else if (CreditNoteType==12) {
                                //                                            //                                                BookCode = "15";
                                //                                            //                                                DeductionCode = "435013";
                                //                                            //                                            }
                                //                                            //                                            else if ((CreditNoteType==8) || (CreditNoteType==9)) {
                                //                                            //                                                BookCode = "15";
                                //                                            //                                                DeductionCode = "435022";
                                //                                            //                                            }
                                //                                            //                                            else if (CreditNoteType==10) {
                                //                                            //                                                BookCode = "13";
                                //                                            //                                                DeductionCode = "304010";
                                //                                            //                                            }
                                //                                            //                                            else if (CreditNoteType==7) {
                                //                                            //                                                BookCode = "13";
                                //                                            //                                                DeductionCode = "";
                                //                                            //                                            }
                                //
                                //                                            if (Discount_Amt != 0) {
                                //
                                //                                                clsCreditNoteProcessDetail objProcessDtl=new clsCreditNoteProcessDetail();
                                //                                                objProcessDtl.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                                //                                                objProcessDtl.setAttribute("SR_NO",cntInvoice);
                                //                                                objProcessDtl.setAttribute("INVOICE_NO",InvoiceNo);
                                //                                                objProcessDtl.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
                                //                                                objProcessDtl.setAttribute("PARTY_ID",PartyID);
                                //                                                int InvMainType = rsPolicy.getInt("INVOICE_MAIN_TYPE");
                                //                                                String MainCode="";
                                //
                                //                                                if (InvMainType == 0){//suiting
                                //                                                    MainCode="210027";
                                //                                                }
                                //                                                else if (InvMainType == 1) {//felt
                                //                                                    MainCode="210010";
                                //                                                }
                                //                                                else if (InvMainType == 2) {//filter
                                //                                                    MainCode="210072";
                                //                                                }
                                //                                                if (! clsAccount.IsValidAccount(MainCode,PartyID)) {
                                //                                                    JOptionPane.showMessageDialog(null,"Main Account Code/Sub Account Code is not valid. Please verify.");
                                //                                                    List = new HashMap();
                                //                                                    return List;
                                //                                                }
                                //                                                objProcessDtl.setAttribute("PARTY_MAIN_CODE",MainCode);
                                //                                                objProcessDtl.setAttribute("AMOUNT",InvAmount);
                                //                                                objProcessDtl.setAttribute("QUALIFING_AMOUNT",InvAmount);
                                //                                                objProcessDtl.setAttribute("PERCENTAGE",Percentage);
                                //                                                objProcessDtl.setAttribute("QUALITY_ID","");
                                //                                                objProcessDtl.setAttribute("SEASON_ID","");
                                //                                                objProcessDtl.setAttribute("DISCOUNT_AMOUNT",Discount_Amt);
                                //                                                objProcessDtl.setAttribute("VOUCHER_TYPE",VoucherType);
                                //                                                objProcessDtl.setAttribute("BOOK_CODE",BookCode);
                                //                                                objProcessDtl.setAttribute("DEDUCTION_CODE",DeductionCode);
                                //
                                //                                                List.put(Integer.toString(List.size()+1),objProcessDtl);
                                //                                            }
                                //                                            rsInvDetail.next();
                                //                                        }
                                //                                    }
                                //                                }
                            }
                            rsParty.close();
                            rsInvoice.next();
                        }
                        rsInvoice.close();
                        
                    }
                    
                }
            }
            rsPolicy.close();
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return List;
    }
    
    public boolean PostVoucher(String DocNo) {
        
        try {
            
            boolean VoucherPosted=true;
            
            objVoucher.UseSpecificVoucherNo=false;
            
            String SelPrefix="";
            String SelSuffix="";
            int FFNo=0;
            boolean boolCN = false;
            boolean boolDN = false;
            
            ResultSet rsChkVoucher = data.getResult("SELECT DISTINCT VOUCHER_TYPE AS VOUCHER_TYPE FROM D_SAL_POLICY_PROCESS_DETAIL WHERE DOC_NO='"+DocNo+"'");
            if (rsChkVoucher.getRow() > 0) {
                rsChkVoucher.first();
                while(!rsChkVoucher.isAfterLast() && rsChkVoucher.getRow()>0) {
                    if (rsChkVoucher.getInt("VOUCHER_TYPE") == FinanceGlobal.TYPE_CREDIT_NOTE) {
                        boolCN = true;
                    }
                    if (rsChkVoucher.getInt("VOUCHER_TYPE") == FinanceGlobal.TYPE_DEBIT_NOTE) {
                        boolDN = true;
                    }
                    rsChkVoucher.next();
                }
                
            }
            
            if (boolCN == true) {
                //*************************************Credit Note Voucher*******************************************//
                String sql = "SELECT DISTINCT PARTY_ID AS PARTY_ID FROM D_SAL_POLICY_PROCESS_DETAIL WHERE DOC_NO='"+DocNo+"' AND VOUCHER_TYPE="+FinanceGlobal.TYPE_CREDIT_NOTE;
                ResultSet rsParty = data.getResult(sql);
                String MainParty = "";
                if (rsParty.getRow()>0) {
                    rsParty.first();
                    
                    while(!rsParty.isAfterLast()) {
                        
                        MainParty = rsParty.getString("PARTY_ID");
                        
                        ResultSet rsVoucher=data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID="+clsVoucher.CreditNoteVoucherModuleID);
                        rsVoucher.first();
                        
                        if(rsVoucher.getRow()>0) {
                            SelPrefix=rsVoucher.getString("PREFIX_CHARS");
                            SelSuffix=rsVoucher.getString("SUFFIX_CHARS");
                            FFNo=rsVoucher.getInt("FIRSTFREE_NO");
                        }
                        
                        //Load Credit Note Processing Module
                        clsCreditNoteProcessModule objProcess=(clsCreditNoteProcessModule)getObject(EITLERPGLOBAL.gCompanyID,DocNo);
                        
                        objVoucher=new clsVoucher();
                        objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
                        
                        objVoucher.setAttribute("PREFIX",SelPrefix);
                        objVoucher.setAttribute("SUFFIX",SelSuffix);
                        objVoucher.setAttribute("FFNO",FFNo);
                        objVoucher.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                        objVoucher.setAttribute("VOUCHER_NO","");
                        objVoucher.setAttribute("VOUCHER_TYPE",FinanceGlobal.TYPE_CREDIT_NOTE);
                        objVoucher.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.getCurrentDate());
                        String str = "SELECT DISTINCT BOOK_CODE FROM D_SAL_POLICY_PROCESS_DETAIL WHERE DOC_NO='"+DocNo+"'";
                        objVoucher.setAttribute("BOOK_CODE",data.getStringValueFromDB(str));
                        objVoucher.setAttribute("CHEQUE_NO","");
                        objVoucher.setAttribute("CHEQUE_DATE","0000-00-00");
                        objVoucher.setAttribute("BANK_NAME",clsBook.getBookName(EITLERPGLOBAL.gCompanyID,data.getStringValueFromDB(str)));
                        objVoucher.setAttribute("CHEQUE_AMOUNT",0);
                        objVoucher.setAttribute("REMARKS","");
                        objVoucher.setAttribute("LEGACY_NO","");
                        objVoucher.setAttribute("LEGACY_DATE","0000-00-00");
                        
                        //----- Update Approval Specific Fields -----------//
                        objVoucher.setAttribute("HIERARCHY_ID",objProcess.getAttribute("FIN_HIERARCHY_ID").getInt());
                        int FirstUserID=data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID="+getAttribute("FIN_HIERARCHY_ID").getInt()+" AND SR_NO=1");
                        objVoucher.setAttribute("FROM",FirstUserID);
                        objVoucher.setAttribute("TO",FirstUserID);
                        objVoucher.setAttribute("FROM_REMARKS","");
                        objVoucher.setAttribute("APPROVAL_STATUS","H"); //Hold Voucher
                        //-------------------------------------------------//
                        
                        
                        objVoucher.setAttribute("CREATED_BY",FirstUserID);
                        objVoucher.setAttribute("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        
                        objVoucher.setAttribute("MODIFIED_BY",FirstUserID);
                        objVoucher.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        
                        
                        //======================= Convert Payment Format to Normal Format ============================//
                        objVoucher.colVoucherItems.clear();
                        objVoucher.colVoucherItemsEx.clear();
                        
                        int VoucherSrNo=0;
                        clsVoucherItem objItem;
                        
                        ResultSet rsTmp;
                        Statement tmpStmt;
                        str = "SELECT * FROM D_SAL_POLICY_PROCESS_DETAIL WHERE DOC_NO='"+DocNo+"' AND VOUCHER_TYPE="+FinanceGlobal.TYPE_CREDIT_NOTE + " AND PARTY_ID='"+MainParty+"' ";
                        //tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                        //rsTmp=tmpStmt.executeQuery(str);
                        rsTmp = data.getResult(str);
                        
                        if(rsTmp.getRow()>0) {
                            //Add Credit Entry
                            rsTmp.first();
                            while(!rsTmp.isAfterLast()) {
                                //for(int i=0;i<rsTmp.getRow();i++) {
                                VoucherSrNo++;
                                objItem=new clsVoucherItem();
                                objItem.setAttribute("SR_NO",VoucherSrNo);
                                
                                objItem.setAttribute("EFFECT","C");
                                objItem.setAttribute("ACCOUNT_ID",1); //Default Value
                                objItem.setAttribute("MAIN_ACCOUNT_CODE",rsTmp.getString("PARTY_MAIN_CODE"));
                                objItem.setAttribute("SUB_ACCOUNT_CODE",rsTmp.getString("PARTY_ID"));
                                objItem.setAttribute("AMOUNT",EITLERPGLOBAL.round(rsTmp.getDouble("DISCOUNT_AMOUNT"),2));
                                objItem.setAttribute("REMARKS","");
                                objItem.setAttribute("PO_NO","");
                                objItem.setAttribute("PO_DATE","0000-00-00");
                                objItem.setAttribute("INVOICE_NO",rsTmp.getString("INVOICE_NO"));
                                objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(rsTmp.getString("INVOICE_DATE")));
                                objItem.setAttribute("INVOICE_AMOUNT",rsTmp.getDouble("AMOUNT"));
                                objItem.setAttribute("GRN_NO",rsTmp.getString("DOC_NO"));
                                String GRNNo = rsTmp.getString("DOC_NO");
                                objItem.setAttribute("GRN_DATE",data.getStringValueFromDB("SELECT DOC_DATE FROM D_SAL_POLICY_PROCESS WHERE DOC_NO='"+GRNNo+"'"));
                                objItem.setAttribute("MODULE_ID",clsCreditNoteProcessModule.ModuleID);
                                objItem.setAttribute("REF_COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                                
                                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1), objItem);
                                
                                
                                //Add Debit Entry
                                VoucherSrNo++;
                                objItem=new clsVoucherItem();
                                objItem.setAttribute("SR_NO",VoucherSrNo);
                                
                                objItem.setAttribute("EFFECT","D");
                                objItem.setAttribute("ACCOUNT_ID",1); //Default Value
                                objItem.setAttribute("MAIN_ACCOUNT_CODE",rsTmp.getString("DEDUCTION_CODE"));//DataModel.getValueByVariable("DEDUCTION_CODE",i));
                                objItem.setAttribute("SUB_ACCOUNT_CODE","");
                                objItem.setAttribute("AMOUNT",EITLERPGLOBAL.round(rsTmp.getDouble("DISCOUNT_AMOUNT"),2));
                                objItem.setAttribute("REMARKS","");
                                objItem.setAttribute("PO_NO","");
                                objItem.setAttribute("PO_DATE","0000-00-00");
                                objItem.setAttribute("INVOICE_NO",rsTmp.getString("INVOICE_NO"));
                                objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(rsTmp.getString("INVOICE_DATE")));
                                objItem.setAttribute("GRN_NO",rsTmp.getString("DOC_NO"));
                                GRNNo = rsTmp.getString("DOC_NO");
                                objItem.setAttribute("GRN_DATE",data.getStringValueFromDB("SELECT DOC_DATE FROM D_SAL_POLICY_PROCESS WHERE DOC_NO='"+GRNNo+"'"));
                                objItem.setAttribute("MODULE_ID",clsCreditNoteProcessModule.ModuleID);
                                objItem.setAttribute("REF_COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                                
                                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1), objItem);
                                rsTmp.next();
                            }
                        }
                        
                        //objVoucher.LoadDataEx(EITLERPGLOBAL.gCompanyID);
                        
                        if(!objVoucher.Insert()) {
                            JOptionPane.showMessageDialog(null,"Error occured while saving. Error is "+objVoucher.LastError);
                            System.out.println(objVoucher.LastError);
                            //LastError = objVoucher.LastError;
                            VoucherPosted=false;
                        }
                        else {
                            String VoucherNo=objVoucher.getAttribute("VOUCHER_NO").getString();
                            data.Execute("UPDATE D_SAL_POLICY_PROCESS_DETAIL SET CREDIT_NOTE_NO='"+VoucherNo+"' WHERE DOC_NO='"+DocNo+"' AND PARTY_ID='"+MainParty+"' ");
                            
                        }
                        rsParty.next();
                    }
                }
                //*************************************End of Credit Note Voucher*******************************************//
            }
            
            
            if (boolDN == true) {
                //*************************************DEBIT Note Voucher*******************************************//
                
                String sql = "SELECT DISTINCT PARTY_ID AS PARTY_ID FROM D_SAL_POLICY_PROCESS_DETAIL WHERE DOC_NO='"+DocNo+"' AND VOUCHER_TYPE="+FinanceGlobal.TYPE_DEBIT_NOTE;
                ResultSet rsParty = data.getResult(sql);
                String MainParty = "";
                if (rsParty.getRow()>0) {
                    rsParty.first();
                    
                    while(!rsParty.isAfterLast()) {
                        
                        MainParty = rsParty.getString("PARTY_ID");
                        
                        ResultSet rsVoucher=data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID="+clsVoucher.DebitNoteModuleID);
                        rsVoucher.first();
                        
                        if(rsVoucher.getRow()>0) {
                            SelPrefix=rsVoucher.getString("PREFIX_CHARS");
                            SelSuffix=rsVoucher.getString("SUFFIX_CHARS");
                            FFNo=rsVoucher.getInt("FIRSTFREE_NO");
                        }
                        
                        //Load Credit Note Processing Module
                        clsCreditNoteProcessModule objProcess=(clsCreditNoteProcessModule)getObject(EITLERPGLOBAL.gCompanyID,DocNo);
                        
                        objVoucher=new clsVoucher();
                        objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
                        
                        objVoucher.setAttribute("PREFIX",SelPrefix);
                        objVoucher.setAttribute("SUFFIX",SelSuffix);
                        objVoucher.setAttribute("FFNO",FFNo);
                        objVoucher.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                        objVoucher.setAttribute("VOUCHER_NO","");
                        objVoucher.setAttribute("VOUCHER_TYPE",FinanceGlobal.TYPE_DEBIT_NOTE);
                        objVoucher.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.getCurrentDate());
                        
                        objVoucher.setAttribute("BOOK_CODE","00");
                        objVoucher.setAttribute("CHEQUE_NO","");
                        objVoucher.setAttribute("CHEQUE_DATE","0000-00-00");
                        objVoucher.setAttribute("BANK_NAME",clsBook.getBookName(EITLERPGLOBAL.gCompanyID,"00"));
                        objVoucher.setAttribute("CHEQUE_AMOUNT",0);
                        objVoucher.setAttribute("REMARKS","");
                        objVoucher.setAttribute("LEGACY_NO","");
                        objVoucher.setAttribute("LEGACY_DATE","0000-00-00");
                        
                        //----- Update Approval Specific Fields -----------//
                        objVoucher.setAttribute("HIERARCHY_ID",objProcess.getAttribute("FIN_HIERARCHY_ID").getInt());
                        int FirstUserID=data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID="+getAttribute("FIN_HIERARCHY_ID").getInt()+" AND SR_NO=1");
                        objVoucher.setAttribute("FROM",FirstUserID);
                        objVoucher.setAttribute("TO",FirstUserID);
                        objVoucher.setAttribute("FROM_REMARKS","");
                        objVoucher.setAttribute("APPROVAL_STATUS","H"); //Hold Voucher
                        //-------------------------------------------------//
                        
                        
                        objVoucher.setAttribute("CREATED_BY",FirstUserID);
                        objVoucher.setAttribute("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        
                        objVoucher.setAttribute("MODIFIED_BY",FirstUserID);
                        objVoucher.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        
                        
                        //======================= Convert Payment Format to Normal Format ============================//
                        objVoucher.colVoucherItems.clear();
                        objVoucher.colVoucherItemsEx.clear();
                        
                        int VoucherSrNo=0;
                        clsVoucherItem objItem;
                        
                        ResultSet rsTmp;
                        Statement tmpStmt;
                        
                        //tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                        //rsTmp=tmpStmt.executeQuery("SELECT * FROM D_SAL_POLICY_PROCESS_DETAIL WHERE DOC_NO='"+DocNo+"' AND VOUCHER_TYPE=3 ");
                        
                        rsTmp = data.getResult("SELECT * FROM D_SAL_POLICY_PROCESS_DETAIL WHERE DOC_NO='"+DocNo+"' AND VOUCHER_TYPE="+ FinanceGlobal.TYPE_DEBIT_NOTE + " AND PARTY_ID='"+MainParty+"' ");
                        
                        if(rsTmp.getRow()>0) {
                            //Add Debit Entry
                            for(int i=0;i<rsTmp.getRow();i++) {
                                VoucherSrNo++;
                                objItem=new clsVoucherItem();
                                objItem.setAttribute("SR_NO",VoucherSrNo);
                                
                                objItem.setAttribute("EFFECT","D");
                                objItem.setAttribute("ACCOUNT_ID",1); //Default Value
                                objItem.setAttribute("MAIN_ACCOUNT_CODE",rsTmp.getString("PARTY_MAIN_CODE"));
                                objItem.setAttribute("SUB_ACCOUNT_CODE",rsTmp.getString("PARTY_ID"));
                                objItem.setAttribute("AMOUNT",EITLERPGLOBAL.round(rsTmp.getDouble("DISCOUNT_AMOUNT"),2));
                                objItem.setAttribute("REMARKS","");
                                objItem.setAttribute("PO_NO","");
                                objItem.setAttribute("PO_DATE","0000-00-00");
                                objItem.setAttribute("INVOICE_NO",rsTmp.getString("INVOICE_NO"));
                                objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(rsTmp.getString("INVOICE_DATE")));
                                objItem.setAttribute("INVOICE_AMOUNT",rsTmp.getDouble("AMOUNT"));
                                objItem.setAttribute("GRN_NO",rsTmp.getString("DOC_NO"));
                                String GRNNo = rsTmp.getString("DOC_NO");
                                objItem.setAttribute("GRN_DATE",data.getStringValueFromDB("SELECT DOC_DATE FROM D_SAL_POLICY_PROCESS WHERE DOC_NO='"+GRNNo+"'"));
                                objItem.setAttribute("MODULE_ID",clsCreditNoteProcessModule.ModuleID);
                                objItem.setAttribute("REF_COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                                
                                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1), objItem);
                                
                                
                                //Add Credit Entry
                                VoucherSrNo++;
                                objItem=new clsVoucherItem();
                                objItem.setAttribute("SR_NO",VoucherSrNo);
                                
                                objItem.setAttribute("EFFECT","C");
                                objItem.setAttribute("ACCOUNT_ID",1); //Default Value
                                objItem.setAttribute("MAIN_ACCOUNT_CODE","");//DataModel.getValueByVariable("DEDUCTION_CODE",i));
                                objItem.setAttribute("SUB_ACCOUNT_CODE","");
                                objItem.setAttribute("AMOUNT",EITLERPGLOBAL.round(rsTmp.getDouble("DISCOUNT_AMOUNT"),2));
                                objItem.setAttribute("REMARKS","");
                                objItem.setAttribute("PO_NO","");
                                objItem.setAttribute("PO_DATE","0000-00-00");
                                objItem.setAttribute("INVOICE_NO",rsTmp.getString("INVOICE_NO"));
                                objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(rsTmp.getString("INVOICE_DATE")));
                                objItem.setAttribute("GRN_NO",rsTmp.getString("DOC_NO"));
                                GRNNo = rsTmp.getString("DOC_NO");
                                objItem.setAttribute("GRN_DATE",data.getStringValueFromDB("SELECT DOC_DATE FROM D_SAL_POLICY_PROCESS WHERE DOC_NO='"+GRNNo+"'"));
                                objItem.setAttribute("MODULE_ID",clsCreditNoteProcessModule.ModuleID);
                                objItem.setAttribute("REF_COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                                
                                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1), objItem);
                                
                            }
                        }
                        
                        if(!objVoucher.Insert()) {
                            JOptionPane.showMessageDialog(null,"Error occured while saving. Error is "+objVoucher.LastError);
                            System.out.println(objVoucher.LastError);
                            //LastError = objVoucher.LastError;
                            VoucherPosted=false;
                        }
                        else {
                            String VoucherNo=objVoucher.getAttribute("VOUCHER_NO").getString();
                            data.Execute("UPDATE D_SAL_POLICY_PROCESS_DETAIL SET CREDIT_NOTE_NO='"+VoucherNo+"' WHERE DOC_NO='"+DocNo+"' AND PARTY_ID='"+MainParty+"' ");
                        }
                        rsParty.next();
                    }
                }
                
                //*************************************End of Debit Note Voucher*******************************************//
            }
            return VoucherPosted;
        }
        catch(Exception e) {
            
            e.printStackTrace();
            return false;
        }
        
    }
    
}