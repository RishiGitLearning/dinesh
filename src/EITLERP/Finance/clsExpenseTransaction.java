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

public class clsExpenseTransaction{
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
    private boolean HistoryView=false;
    
    public static int ModuleID=63;
    
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
    public clsExpenseTransaction() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("EXPENSE_ID",new Variant(""));
        props.put("EXPENSE_DATE",new Variant(""));
        props.put("FROM_DATE",new Variant(""));
        props.put("TO_DATE",new Variant(""));
        props.put("MIR_NO", new Variant(""));
        props.put("MIR_DATE", new Variant(""));
        props.put("INVOICE_NO",new Variant(""));
        props.put("INVOICE_DATE",new Variant(""));
        props.put("INVOICE_AMOUNT",new Variant(0));
        props.put("FROM_READING",new Variant(""));
        props.put("TO_READING",new Variant(""));
        props.put("EXPENSE_DESCRIPTION",new Variant(""));
        props.put("PARTY_NAME",new Variant(""));
        props.put("ADDRESS",new Variant(""));
        props.put("CITY",new Variant(""));
        props.put("PINCODE",new Variant(""));
        props.put("REF_DOC_NO",new Variant(""));
        props.put("REF_DOC_DATE",new Variant(""));
        props.put("MODULE_ID",new Variant(0));
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
        
        props.put("AMOUNT1_CAPTION",new Variant(""));
        props.put("AMOUNT1",new Variant(0));
        props.put("AMOUNT2_CAPTION",new Variant(""));
        props.put("AMOUNT2",new Variant(0));
        props.put("AMOUNT3_CAPTION",new Variant(""));
        props.put("AMOUNT3",new Variant(0));
        props.put("AMOUNT4_CAPTION",new Variant(""));
        props.put("AMOUNT4",new Variant(0));
        props.put("AMOUNT5_CAPTION",new Variant(""));
        props.put("AMOUNT5",new Variant(0));

        
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
            //rsResultSet=Stmt.executeQuery("SELECT * FROM D_FIN_EXPENSE_TRANSACTION ORDER BY DOC_DATE");
rsResultSet=Stmt.executeQuery("SELECT * FROM D_FIN_EXPENSE_TRANSACTION WHERE COMPANY_ID=" + pCompanyID + " AND DOC_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND DOC_DATE<='" + EITLERPGLOBAL.FinToDateDB + "' ORDER BY DOC_DATE");
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
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FIN_EXPENSE_TRANSACTION_H WHERE DOC_NO='1' ");
            rsHistory.first();
            //------------------------------------//
            
            setAttribute("DOC_NO","");
            
            //** Validations **//
            if(!Validate()) {
                return false;
            }
            //*****************//
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //Start Transaction
            //Conn.setAutoCommit(false);
            //data.SetAutoCommit(false);
            
            //--------- Generate New Internal Account ID  ------------
            String DocNo=clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID,clsExpenseTransaction.ModuleID, getAttribute("FFNO").getInt(),true);
            setAttribute("DOC_NO",DocNo);
            //--------------------------------------------------------
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsResultSet.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsResultSet.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsResultSet.updateString("EXPENSE_ID",getAttribute("EXPENSE_ID").getString());
            rsResultSet.updateString("EXPENSE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("EXPENSE_DATE").getString()));
            rsResultSet.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("FROM_DATE").getString()));
            rsResultSet.updateString("TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("TO_DATE").getString()));
            rsResultSet.updateString("INVOICE_NO",getAttribute("INVOICE_NO").getString());
            rsResultSet.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("INVOICE_DATE").getString()));
            rsResultSet.updateDouble("INVOICE_AMOUNT",getAttribute("INVOICE_AMOUNT").getDouble());
            rsResultSet.updateString("FROM_READING",getAttribute("FROM_READING").getString());
            rsResultSet.updateString("TO_READING",getAttribute("TO_READING").getString());
            rsResultSet.updateString("EXPENSE_DESCRIPTION",getAttribute("EXPENSE_DESCRIPTION").getString());
            rsResultSet.updateString("MIR_NO", getAttribute("MIR_NO").getString());
            rsResultSet.updateString("MIR_DATE", getAttribute("MIR_DATE").getString());
            rsResultSet.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            rsResultSet.updateString("ADDRESS",getAttribute("ADDRESS").getString());
            rsResultSet.updateString("CITY",getAttribute("CITY").getString());
            rsResultSet.updateString("PINCODE",getAttribute("PINCODE").getString());
            rsResultSet.updateString("REF_DOC_NO",getAttribute("REF_DOC_NO").getString());
            rsResultSet.updateString("REF_DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("REF_DOC_DATE").getString()));
            rsResultSet.updateInt("MODULE_ID",getAttribute("MODULE_ID").getInt());
            
            rsResultSet.updateString("AMOUNT1_CAPTION",getAttribute("AMOUNT1_CAPTION").getString());
            rsResultSet.updateDouble("AMOUNT1",getAttribute("AMOUNT1").getDouble());
            rsResultSet.updateString("AMOUNT2_CAPTION",getAttribute("AMOUNT2_CAPTION").getString());
            rsResultSet.updateDouble("AMOUNT2",getAttribute("AMOUNT2").getDouble());
            rsResultSet.updateString("AMOUNT3_CAPTION",getAttribute("AMOUNT3_CAPTION").getString());
            rsResultSet.updateDouble("AMOUNT3",getAttribute("AMOUNT3").getDouble());
            rsResultSet.updateString("AMOUNT4_CAPTION",getAttribute("AMOUNT4_CAPTION").getString());
            rsResultSet.updateDouble("AMOUNT4",getAttribute("AMOUNT4").getDouble());
            rsResultSet.updateString("AMOUNT5_CAPTION",getAttribute("AMOUNT5_CAPTION").getString());
            rsResultSet.updateDouble("AMOUNT5",getAttribute("AMOUNT5").getDouble());
            
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
            rsHistory.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsHistory.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsHistory.updateString("EXPENSE_ID",getAttribute("EXPENSE_ID").getString());
            rsHistory.updateString("EXPENSE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("EXPENSE_DATE").getString()));
            rsHistory.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("FROM_DATE").getString()));
            rsHistory.updateString("TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("TO_DATE").getString()));
            rsHistory.updateString("INVOICE_NO",getAttribute("INVOICE_NO").getString());
            rsHistory.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("INVOICE_DATE").getString()));
            rsHistory.updateDouble("INVOICE_AMOUNT",getAttribute("INVOICE_AMOUNT").getDouble());
            rsHistory.updateString("FROM_READING",getAttribute("FROM_READING").getString());
            rsHistory.updateString("TO_READING",getAttribute("TO_READING").getString());
            rsHistory.updateString("EXPENSE_DESCRIPTION",getAttribute("EXPENSE_DESCRIPTION").getString());
            rsHistory.updateString("MIR_NO", getAttribute("MIR_NO").getString());
            rsHistory.updateString("MIR_DATE", getAttribute("MIR_DATE").getString());
            rsHistory.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            rsHistory.updateString("ADDRESS",getAttribute("ADDRESS").getString());
            rsHistory.updateString("CITY",getAttribute("CITY").getString());
            rsHistory.updateString("PINCODE",getAttribute("PINCODE").getString());
            rsHistory.updateString("REF_DOC_NO",getAttribute("REF_DOC_NO").getString());
            rsHistory.updateString("REF_DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("REF_DOC_DATE").getString()));
            rsHistory.updateInt("MODULE_ID",getAttribute("MODULE_ID").getInt());

            rsHistory.updateString("AMOUNT1_CAPTION",getAttribute("AMOUNT1_CAPTION").getString());
            rsHistory.updateDouble("AMOUNT1",getAttribute("AMOUNT1").getDouble());
            rsHistory.updateString("AMOUNT2_CAPTION",getAttribute("AMOUNT2_CAPTION").getString());
            rsHistory.updateDouble("AMOUNT2",getAttribute("AMOUNT2").getDouble());
            rsHistory.updateString("AMOUNT3_CAPTION",getAttribute("AMOUNT3_CAPTION").getString());
            rsHistory.updateDouble("AMOUNT3",getAttribute("AMOUNT3").getDouble());
            rsHistory.updateString("AMOUNT4_CAPTION",getAttribute("AMOUNT4_CAPTION").getString());
            rsHistory.updateDouble("AMOUNT4",getAttribute("AMOUNT4").getDouble());
            rsHistory.updateString("AMOUNT5_CAPTION",getAttribute("AMOUNT5_CAPTION").getString());
            rsHistory.updateDouble("AMOUNT5",getAttribute("AMOUNT5").getDouble());
            
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
            ObjFlow.DocNo=getAttribute("DOC_NO").getString();
            ObjFlow.From=getAttribute("FROM").getInt();
            ObjFlow.To=getAttribute("TO").getInt();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_FIN_EXPENSE_TRANSACTION";
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
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FIN_EXPENSE_TRANSACTION_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //** --------------------------------**//
            
            //Start Transaction
            //Conn.setAutoCommit(false);
            //data.SetAutoCommit(false);
            
            
            rsResultSet.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsResultSet.updateString("EXPENSE_ID",getAttribute("EXPENSE_ID").getString());
            rsResultSet.updateString("EXPENSE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("EXPENSE_DATE").getString()));
            rsResultSet.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("FROM_DATE").getString()));
            rsResultSet.updateString("TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("TO_DATE").getString()));
            rsResultSet.updateString("INVOICE_NO",getAttribute("INVOICE_NO").getString());
            rsResultSet.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("INVOICE_DATE").getString()));
            rsResultSet.updateDouble("INVOICE_AMOUNT",getAttribute("INVOICE_AMOUNT").getDouble());
            rsResultSet.updateString("FROM_READING",getAttribute("FROM_READING").getString());
            rsResultSet.updateString("TO_READING",getAttribute("TO_READING").getString());
            rsResultSet.updateString("EXPENSE_DESCRIPTION",getAttribute("EXPENSE_DESCRIPTION").getString());
            rsResultSet.updateString("MIR_NO", getAttribute("MIR_NO").getString());
            rsResultSet.updateString("MIR_DATE", getAttribute("MIR_DATE").getString());
            rsResultSet.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            rsResultSet.updateString("ADDRESS",getAttribute("ADDRESS").getString());
            rsResultSet.updateString("CITY",getAttribute("CITY").getString());
            rsResultSet.updateString("PINCODE",getAttribute("PINCODE").getString());
            rsResultSet.updateString("REF_DOC_NO",getAttribute("REF_DOC_NO").getString());
            rsResultSet.updateString("REF_DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("REF_DOC_DATE").getString()));
            rsResultSet.updateInt("MODULE_ID",getAttribute("MODULE_ID").getInt());
            
            rsResultSet.updateString("AMOUNT1_CAPTION",getAttribute("AMOUNT1_CAPTION").getString());
            rsResultSet.updateDouble("AMOUNT1",getAttribute("AMOUNT1").getDouble());
            rsResultSet.updateString("AMOUNT2_CAPTION",getAttribute("AMOUNT2_CAPTION").getString());
            rsResultSet.updateDouble("AMOUNT2",getAttribute("AMOUNT2").getDouble());
            rsResultSet.updateString("AMOUNT3_CAPTION",getAttribute("AMOUNT3_CAPTION").getString());
            rsResultSet.updateDouble("AMOUNT3",getAttribute("AMOUNT3").getDouble());
            rsResultSet.updateString("AMOUNT4_CAPTION",getAttribute("AMOUNT4_CAPTION").getString());
            rsResultSet.updateDouble("AMOUNT4",getAttribute("AMOUNT4").getDouble());
            rsResultSet.updateString("AMOUNT5_CAPTION",getAttribute("AMOUNT5_CAPTION").getString());
            rsResultSet.updateDouble("AMOUNT5",getAttribute("AMOUNT5").getDouble());
            
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
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM D_FIN_EXPENSE_TRANSACTION_H WHERE DOC_NO='"+theDocNo+"'",FinanceGlobal.FinURL);
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
            rsHistory.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsHistory.updateString("EXPENSE_ID",getAttribute("EXPENSE_ID").getString());
            rsHistory.updateString("EXPENSE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("EXPENSE_DATE").getString()));
            rsHistory.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("FROM_DATE").getString()));
            rsHistory.updateString("TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("TO_DATE").getString()));
            rsHistory.updateString("INVOICE_NO",getAttribute("INVOICE_NO").getString());
            rsHistory.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("INVOICE_DATE").getString()));
            rsHistory.updateDouble("INVOICE_AMOUNT",getAttribute("INVOICE_AMOUNT").getDouble());
            rsHistory.updateString("FROM_READING",getAttribute("FROM_READING").getString());
            rsHistory.updateString("TO_READING",getAttribute("TO_READING").getString());
            rsHistory.updateString("EXPENSE_DESCRIPTION",getAttribute("EXPENSE_DESCRIPTION").getString());
            rsHistory.updateString("MIR_NO", getAttribute("MIR_NO").getString());
            rsHistory.updateString("MIR_DATE", getAttribute("MIR_DATE").getString());
            rsHistory.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            rsHistory.updateString("ADDRESS",getAttribute("ADDRESS").getString());
            rsHistory.updateString("CITY",getAttribute("CITY").getString());
            rsHistory.updateString("PINCODE",getAttribute("PINCODE").getString());
            rsHistory.updateString("REF_DOC_NO",getAttribute("REF_DOC_NO").getString());
            rsHistory.updateString("REF_DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("REF_DOC_DATE").getString()));
            rsHistory.updateInt("MODULE_ID",getAttribute("MODULE_ID").getInt());
            
            rsHistory.updateString("AMOUNT1_CAPTION",getAttribute("AMOUNT1_CAPTION").getString());
            rsHistory.updateDouble("AMOUNT1",getAttribute("AMOUNT1").getDouble());
            rsHistory.updateString("AMOUNT2_CAPTION",getAttribute("AMOUNT2_CAPTION").getString());
            rsHistory.updateDouble("AMOUNT2",getAttribute("AMOUNT2").getDouble());
            rsHistory.updateString("AMOUNT3_CAPTION",getAttribute("AMOUNT3_CAPTION").getString());
            rsHistory.updateDouble("AMOUNT3",getAttribute("AMOUNT3").getDouble());
            rsHistory.updateString("AMOUNT4_CAPTION",getAttribute("AMOUNT4_CAPTION").getString());
            rsHistory.updateDouble("AMOUNT4",getAttribute("AMOUNT4").getDouble());
            rsHistory.updateString("AMOUNT5_CAPTION",getAttribute("AMOUNT5_CAPTION").getString());
            rsHistory.updateDouble("AMOUNT5",getAttribute("AMOUNT5").getDouble());
            
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
            ObjFlow.DocNo=getAttribute("DOC_NO").getString();
            ObjFlow.From=getAttribute("FROM").getInt();
            ObjFlow.To=getAttribute("TO").getInt();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_FIN_EXPENSE_TRANSACTION";
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
                data.Execute("UPDATE D_FIN_EXPENSE_TRANSACTION SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='"+theDocNo+"'",FinanceGlobal.FinURL);
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
    
    
    public boolean CanDelete(int CompanyID,String DocNo,int UserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            
            if(HistoryView) {
                return false;
            }
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FIN_EXPENSE_TRANSACTION WHERE DOC_NO='"+DocNo+"' AND (APPROVED=1)";
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
            strSQL="SELECT DOC_NO FROM D_FIN_EXPENSE_TRANSACTION WHERE DOC_NO='"+DocNo+"' AND (APPROVED=1)";
            
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
                String strSQL = "DELETE FROM D_FIN_EXPENSE_TRANSACTION WHERE DOC_NO='"+DocNo+"'";
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
        clsExpenseTransaction objExpense = new clsExpenseTransaction();
        objExpense.Filter(strCondition,CompanyID);
        return objExpense;
    }
    
    
    public boolean Filter(String Condition,int CompanyID) {
        Ready=false;
        
        try {
            String strSQL = "SELECT * FROM D_FIN_EXPENSE_TRANSACTION " + Condition ;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSQL);
            
            if(!rsResultSet.first()) {
                strSQL = "SELECT * FROM D_FIN_EXPENSE_TRANSACTION ORDER BY DOC_NO ";
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
            setAttribute("EXPENSE_ID",UtilFunctions.getString(rsResultSet,"EXPENSE_ID",""));
            setAttribute("EXPENSE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"EXPENSE_DATE","0000-00-00")));
            setAttribute("FROM_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"FROM_DATE","0000-00-00")));
            setAttribute("TO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"TO_DATE","0000-00-00")));
            setAttribute("INVOICE_NO",UtilFunctions.getString(rsResultSet,"INVOICE_NO",""));
            setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"INVOICE_DATE","0000-00-00")));
            setAttribute("INVOICE_AMOUNT",UtilFunctions.getDouble(rsResultSet,"INVOICE_AMOUNT",0));
            setAttribute("FROM_READING",UtilFunctions.getString(rsResultSet,"FROM_READING",""));
            setAttribute("TO_READING",UtilFunctions.getString(rsResultSet,"TO_READING",""));
            setAttribute("EXPENSE_DESCRIPTION",UtilFunctions.getString(rsResultSet,"EXPENSE_DESCRIPTION",""));
            setAttribute("MIR_NO", UtilFunctions.getString(rsResultSet, "MIR_NO", ""));
            setAttribute("MIR_DATE", UtilFunctions.getString(rsResultSet, "MIR_DATE", ""));
            setAttribute("PARTY_NAME",UtilFunctions.getString(rsResultSet,"PARTY_NAME",""));
            setAttribute("ADDRESS",UtilFunctions.getString(rsResultSet,"ADDRESS",""));
            setAttribute("CITY",UtilFunctions.getString(rsResultSet,"CITY",""));
            setAttribute("PINCODE",UtilFunctions.getString(rsResultSet,"PINCODE",""));
            setAttribute("REF_DOC_NO",UtilFunctions.getString(rsResultSet,"REF_DOC_NO",""));
            setAttribute("REF_DOC_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"REF_DOC_DATE","0000-00-00")));
            setAttribute("MODULE_ID",UtilFunctions.getInt(rsResultSet,"MODULE_ID",0));
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
            
            setAttribute("AMOUNT1_CAPTION",UtilFunctions.getString(rsResultSet,"AMOUNT1_CAPTION",""));
            setAttribute("AMOUNT1",UtilFunctions.getDouble(rsResultSet,"AMOUNT1",0));
            setAttribute("AMOUNT2_CAPTION",UtilFunctions.getString(rsResultSet,"AMOUNT2_CAPTION",""));
            setAttribute("AMOUNT2",UtilFunctions.getDouble(rsResultSet,"AMOUNT2",0));
            setAttribute("AMOUNT3_CAPTION",UtilFunctions.getString(rsResultSet,"AMOUNT3_CAPTION",""));
            setAttribute("AMOUNT3",UtilFunctions.getDouble(rsResultSet,"AMOUNT3",0));
            setAttribute("AMOUNT4_CAPTION",UtilFunctions.getString(rsResultSet,"AMOUNT4_CAPTION",""));
            setAttribute("AMOUNT4",UtilFunctions.getDouble(rsResultSet,"AMOUNT4",0));
            setAttribute("AMOUNT5_CAPTION",UtilFunctions.getString(rsResultSet,"AMOUNT5_CAPTION",""));
            setAttribute("AMOUNT5",UtilFunctions.getDouble(rsResultSet,"AMOUNT5",0));
            
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
                strSQL="SELECT FINANCE.D_FIN_EXPENSE_TRANSACTION.DOC_NO,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FIN_EXPENSE_TRANSACTION,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_EXPENSE_TRANSACTION.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            
            if(Order==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT FINANCE.D_FIN_EXPENSE_TRANSACTION.DOC_NO,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FIN_EXPENSE_TRANSACTION,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_EXPENSE_TRANSACTION.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_FIN_EXPENSE_TRANSACTION.DOC_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT FINANCE.D_FIN_EXPENSE_TRANSACTION.DOC_NO,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FIN_EXPENSE_TRANSACTION,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_EXPENSE_TRANSACTION.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_FIN_EXPENSE_TRANSACTION.DOC_NO";
            }
            
            rsTemp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTemp.first();
            
            Counter=0;
            
            if(rsTemp.getRow()>0) {
                while(!rsTemp.isAfterLast()) {
                    Counter=Counter+1;
                    clsExpenseTransaction ObjDoc=new clsExpenseTransaction();
                    
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
            String strSQL="SELECT * FROM D_FIN_EXPENSE_TRANSACTION_H WHERE DOC_NO='"+DocNo+"'";
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
            String strSQL="SELECT * FROM D_FIN_EXPENSE_TRANSACTION_H WHERE DOC_NO='"+DocNo+"'";
            rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsExpenseTransaction objAccount=new clsExpenseTransaction();
                    
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
            
            String strSQL="SELECT DOC_NO,APPROVED,CANCELLED FROM D_FIN_EXPENSE_TRANSACTION WHERE DOC_NO='"+DocNo+"'";
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
            rsTmp=data.getResult("SELECT DOC_NO FROM D_FIN_EXPENSE_TRANSACTION WHERE DOC_NO='"+DocNo+"' AND CANCELLED=0",FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                
                
                if(data.IsRecordExist("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE CANCELLED=0 AND COMPANY_ID="+CompanyID+" AND GRN_NO='"+DocNo+"' AND MODULE_ID="+clsExpenseTransaction.ModuleID,FinanceGlobal.FinURL)) {
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
    
    
    
    public static boolean CancelDoc(int CompanyID,String DocNo) {
        
        ResultSet rsTmp=null;
        boolean Cancelled=false;
        
        try {
            if(CanCancel(CompanyID,DocNo)) {
                
                boolean ApprovedDoc=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_FIN_EXPENSE_TRANSACTION WHERE DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedDoc) {
                    
                }
                else {
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"' AND MODULE_ID="+clsExpenseTransaction.ModuleID);
                }
                
                data.Execute("UPDATE D_FIN_EXPENSE_TRANSACTION SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
                
                Cancelled=true;
            }
            
            rsTmp.close();
        }
        catch(Exception e) {
            
        }
        
        return Cancelled;
    }
    
    
    private boolean Validate() {
        
        try {
            String ExpenseID=getAttribute("EXPENSE_ID").getString();
            String FromDate=EITLERPGLOBAL.formatDateDB(getAttribute("FROM_DATE").getString());
            String ToDate=EITLERPGLOBAL.formatDateDB(getAttribute("TO_DATE").getString());
            String DocNo=getAttribute("DOC_NO").getString();
            String PartyName=getAttribute("PARTY_NAME").getString();
            
            int ExpenseType=data.getIntValueFromDB("SELECT EXPENSE_TYPE FROM D_FIN_EXPENSE_MASTER WHERE EXPENSE_ID='"+ExpenseID+"' ",FinanceGlobal.FinURL);
            
            if(ExpenseType==0&&(FromDate.equals("")||ToDate.equals(""))) {
                LastError="Please enter the period (From date and To date) for the expense";
                return false;
            }
            
            if(PartyName.equals("")) {
                LastError="Please enter the party name";
                return false;
            }
            //!!!! Warning Part !!!! //
            
            //****** Check Period overlapping *****//
            
            String strSQL="SELECT * FROM D_FIN_EXPENSE_TRANSACTION WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND EXPENSE_ID='"+ExpenseID+"' AND ( ('"+FromDate+"'>=FROM_DATE AND '"+FromDate+"'<=TO_DATE) OR ('"+ToDate+"'>=FROM_DATE AND '"+ToDate+"'<=TO_DATE)) AND DOC_NO<>'"+DocNo+"' AND CANCELLED=0";
            if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
                rsTmp.first();
                
                String cDocNo=UtilFunctions.getString(rsTmp,"DOC_NO","");
                String cDocDate=EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"DOC_DATE","0000-00-00"));
                
                LastError="Period is overlapping with document no. "+cDocNo+" Dated "+cDocDate+". Please check";
                
                return false;
            }
            //*************************************//
            
            
            //******** Same Invoice Checking **************//
            String InvoiceNo=getAttribute("INVOICE_NO").getString();
            String InvoiceDate=EITLERPGLOBAL.formatDateDB(getAttribute("INVOICE_DATE").getString());
            
            if(!InvoiceNo.trim().equals("")) {
                strSQL=" SELECT * FROM D_FIN_EXPENSE_TRANSACTION WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND EXPENSE_ID='"+ExpenseID+"' AND INVOICE_NO='"+InvoiceNo+"' AND INVOICE_DATE='"+InvoiceDate+"' AND DOC_NO<>'"+DocNo+"' AND CANCELLED = 0";
                
                if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                    ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
                    rsTmp.first();
                    
                    String cDocNo=UtilFunctions.getString(rsTmp,"DOC_NO","");
                    String cDocDate=EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"DOC_DATE","0000-00-00"));
                    
                    LastError="Expense entry of invoice no. "+InvoiceNo+" dt "+EITLERPGLOBAL.formatDate(InvoiceDate)+" has already been made in document no. "+cDocNo;
                    
                    return false;
                }
            }
            //*********************************************//
            
            
            //******** Same Reference Document Checking **************//
            String RefDocNo=getAttribute("REF_DOC_NO").getString();
            String RefDocDate=EITLERPGLOBAL.formatDateDB(getAttribute("REF_DOC_DATE").getString());
            int ModuleID=getAttribute("MODULE_ID").getInt();
            
            if(!RefDocNo.trim().equals("")) {
                strSQL=" SELECT * FROM D_FIN_EXPENSE_TRANSACTION WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND EXPENSE_ID='"+ExpenseID+"' AND REF_DOC_NO='"+RefDocNo+"' AND REF_DOC_DATE='"+RefDocDate+"' AND MODULE_ID="+ModuleID+" AND DOC_NO<>'"+DocNo+"'";
                
                if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                    ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
                    rsTmp.first();
                    
                    String cDocNo=UtilFunctions.getString(rsTmp,"DOC_NO","");
                    String cDocDate=EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"DOC_DATE","0000-00-00"));
                    
                    LastError="Expense entry of Ref. Doc. no. "+RefDocNo+" dt "+EITLERPGLOBAL.formatDate(RefDocDate)+" has already been made in document no. "+cDocNo;
                    
                    return false;
                }
            }
            //*********************************************//
        }
        catch(Exception e) {
            
        }
        return true;
    }
    
    public static boolean IsDocExist(int CompanyID,String DocNo) {
        boolean IsExist=false;
        
        try {
            IsExist=data.IsRecordExist("SELECT DOC_NO FROM D_FIN_EXPENSE_TRANSACTION WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo.trim()+"' AND APPROVED=1 AND CANCELLED=0",FinanceGlobal.FinURL);
        }
        catch(Exception e) {
            
        }
        return IsExist;
    }
    
    public static double getExpenseAmount(int CompanyID,String DocNo) {
        double Amount=0;
        
        try {
            Amount=data.getDoubleValueFromDB("SELECT INVOICE_AMOUNT FROM D_FIN_EXPENSE_TRANSACTION WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo.trim()+"'",FinanceGlobal.FinURL);
        }
        catch(Exception e) {
            
        }
        return Amount;
    }
    
}
