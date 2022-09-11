/*
 * clsFeltLCPartyMaster.java
 *
 * Created on April 27, 2009, 12:43 PM
 */

package EITLERP.FeltSales.FeltLCMaster;

import java.util.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import java.io.*;
import EITLERP.Production.clsFeltProductionApprovalFlow;

/**
 *
 * @author  root
 */
public class clsFeltLCPartyMaster {
    
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    private boolean HistoryView=false;
    private HashMap props;
    public boolean Ready = false;
    public String LastError;
    public static int ModuleID=606;
    
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
    
    /** Creates a new instance of clsPolicyLCMaster */
    public clsFeltLCPartyMaster() {
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("PARTY_CODE",new Variant(""));
        props.put("LC_NO",new Variant(""));
        props.put("EXP_DATE",new Variant("0000-00-00"));
        props.put("IND",new Variant(""));
        props.put("DISC",new Variant(0));
        props.put("PARTY_NAME",new Variant(""));
        props.put("ADDRESS",new Variant(""));
        props.put("LC_OPENER_CODE",new Variant(""));
        props.put("BANK_ID",new Variant(0));
        props.put("BANK_NAME",new Variant(""));
        props.put("BANK_ADDRESS",new Variant(""));
        props.put("BANK_CITY",new Variant(""));
        props.put("INST1",new Variant(""));
        props.put("INST2",new Variant(""));
        props.put("BNKHUN",new Variant(""));
        props.put("LOCADD1",new Variant(""));
        props.put("LOCADD2",new Variant(""));
        props.put("AMT",new Variant(0));
        props.put("BNGDOCIND",new Variant(""));
        props.put("LOCALBANK",new Variant(""));
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant("0000-00-00"));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant("0000-00-00"));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE",new Variant("0000-00-00"));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE",new Variant("0000-00-00"));
        props.put("CHANGED",new Variant(true));
        props.put("CHANGED_DATE",new Variant("0000-00-00"));
        props.put("CANCELLED",new Variant(0));
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REJECTED_REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        props.put("SEND_DOC_TO",new Variant(0));
        props.put("APPROVER_REMARKS",new Variant(0));
    }
    
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER ORDER BY LC_OPENER_CODE");
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
     
     public boolean MoveFirst() {
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
    
    public boolean MoveNext() {
        try {
            rsResultSet.next();
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
           // java.sql.Date ExpDate=java.sql.Date.valueOf(EITLERPGLOBAL.formatDateDB(getAttribute("EXP_DATE").getString()));
            
            //if((ExpDate.after(FinFromDate)||ExpDate.compareTo(FinFromDate)==0)&&(ExpDate.before(FinToDate)||ExpDate.compareTo(FinToDate)==0)) {
                //Within the year
         //  }
        //   else {
        //       LastError="Expire date is not within financial year.";
         //       return false;
        //    }
            //=====================================================//
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHistory=stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER_H WHERE PARTY_CODE='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHDetail=stHDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER WHERE PARTY_CODE='1'");
            rsHDetail.first();
            //------------------------------------//
            
            clsFeltProductionApprovalFlow ObjFlow=new clsFeltProductionApprovalFlow();
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsResultSet.updateString("LC_NO",getAttribute("LC_NO").getString());
            rsResultSet.updateString("EXP_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("EXP_DATE").getString()));
            rsResultSet.updateString("IND",getAttribute("IND").getString());
            rsResultSet.updateDouble("DISC",(double)getAttribute("DISC").getDouble());
            rsResultSet.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            rsResultSet.updateString("ADDRESS",getAttribute("ADDRESS").getString());
            rsResultSet.updateString("LC_OPENER_CODE",getAttribute("LC_OPENER_CODE").getString());
            rsResultSet.updateInt("BANK_ID",(int)getAttribute("BANK_ID").getVal());
            rsResultSet.updateString("BANK_NAME",getAttribute("BANK_NAME").getString());
            rsResultSet.updateString("BANK_ADDRESS",getAttribute("BANK_ADDRESS").getString());
            rsResultSet.updateString("BANK_CITY",getAttribute("BANK_CITY").getString());
            rsResultSet.updateString("INST1",getAttribute("INST1").getString());
            rsResultSet.updateString("INST2",getAttribute("INST2").getString());
            rsResultSet.updateString("BNKHUN",getAttribute("BNKHUN").getString());
            rsResultSet.updateString("LOCADD1",getAttribute("LOCADD1").getString());
            rsResultSet.updateString("LOCADD2",getAttribute("LOCADD2").getString());
            rsResultSet.updateDouble("AMT",(double)getAttribute("AMT").getDouble());
            rsResultSet.updateString("BNGDOCIND",getAttribute("BNGDOCIND").getString());
            rsResultSet.updateString("LOCALBANK",getAttribute("LOCALBANK").getString());
            
            rsResultSet.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
            rsResultSet.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsResultSet.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.insertRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            
            rsHistory.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
            rsHistory.updateInt("REVISION_NO",1);
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            
            rsHistory.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsHistory.updateString("LC_NO",getAttribute("LC_NO").getString());
            rsHistory.updateString("EXP_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("EXP_DATE").getString()));
            rsHistory.updateString("IND",getAttribute("IND").getString());
            rsHistory.updateDouble("DISC",(double)getAttribute("DISC").getDouble());
            rsHistory.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            rsHistory.updateString("ADDRESS",getAttribute("ADDRESS").getString());
            rsHistory.updateString("LC_OPENER_CODE",getAttribute("LC_OPENER_CODE").getString());   
            rsHistory.updateInt("BANK_ID",(int)getAttribute("BANK_ID").getVal());
            rsHistory.updateString("BANK_NAME",getAttribute("BANK_NAME").getString());
            rsHistory.updateString("BANK_ADDRESS",getAttribute("BANK_ADDRESS").getString());
            rsHistory.updateString("BANK_CITY",getAttribute("BANK_CITY").getString());
            rsHistory.updateString("INST1",getAttribute("INST1").getString());
            rsHistory.updateString("INST2",getAttribute("INST2").getString());
            rsHistory.updateString("BNKHUN",getAttribute("BNKHUN").getString());
            rsHistory.updateString("LOCADD1",getAttribute("LOCADD1").getString());
            rsHistory.updateString("LOCADD2",getAttribute("LOCADD2").getString());
            rsHistory.updateDouble("AMT",(double)getAttribute("AMT").getDouble());
            rsHistory.updateString("BNGDOCIND",getAttribute("BNGDOCIND").getString());
            rsHistory.updateString("LOCALBANK",getAttribute("LOCALBANK").getString());
            rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
            rsHistory.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsHistory.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            
            //rsResultSet.updateBoolean("CANCELED",(boolean)getAttribute("CANCELED").getBool());
            rsHistory.updateBoolean("CANCELLED",false);
            
            rsHistory.insertRow();
            
            
            //======== Update the Approval Flow =========
            //ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=606; //Material Requisition
            ObjFlow.DocNo=(String)getAttribute("LC_OPENER_CODE").getObj();
//            ObjFlow.DocNo=(String)getAttribute("PARTY_CODE").getObj();
            //ObjFlow.DocDate=(String) getAttribute("CREATED_DATE").getObj();
            ObjFlow.DocDate=(String) getAttribute("EXP_DATE").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="PRODUCTION.FELT_SALES_LC_PARTY_MASTER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="LC_OPENER_CODE";
            
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
            //--------- Approval Flow Update complete -----------
            
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
            LastError= e.getMessage();
            return false;
        }
    }
    
    
    public boolean Update() {
        Statement stHistory,stHDetail,stHeader;
        ResultSet rsHistory,rsHDetail,rsHeader;
        boolean Validate=true;
        
        try {
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            if(AStatus.equals("R")) {
                Validate=false;
            }
            
            Validate=true;
            
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
           java.sql.Date ExpirtyDate=java.sql.Date.valueOf(EITLERPGLOBAL.formatDateDB(getAttribute("EXP_DATE").getString()));
            
            //            if((ExpirtyDate.after(FinFromDate)||ExpirtyDate.compareTo(FinFromDate)==0)&&(ExpirtyDate.before(FinToDate)||ExpirtyDate.compareTo(FinToDate)==0)) {
            //                //Withing the year
            //            }
            //            else {
            //                LastError="Expirty date is not within financial year.";
            //                return false;
            //            }
            //=====================================================//
            
            
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
             rsHistory=stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER_H WHERE PARTY_CODE='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHDetail=stHDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER WHERE PARTY_CODE='1'");
            rsHDetail.first();
            //------------------------------------//
            
            String theDocNo=(String)getAttribute("PARTY_CODE").getObj();
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_REQ_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND TRIM(REQ_NO)='"+theDocNo+"'");
            //rsHeader.first();
            
            clsFeltProductionApprovalFlow ObjFlow=new clsFeltProductionApprovalFlow();
            
            rsResultSet.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsResultSet.updateString("LC_NO",getAttribute("LC_NO").getString());
            rsResultSet.updateString("EXP_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("EXP_DATE").getString()));
            rsResultSet.updateString("IND",getAttribute("IND").getString());
            rsResultSet.updateDouble("DISC",(double)getAttribute("DISC").getDouble());
            rsResultSet.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            rsResultSet.updateString("ADDRESS",getAttribute("ADDRESS").getString());
            rsResultSet.updateString("LC_OPENER_CODE",getAttribute("LC_OPENER_CODE").getString());
            rsResultSet.updateInt("BANK_ID",(int)getAttribute("BANK_ID").getVal());
            rsResultSet.updateString("BANK_NAME",getAttribute("BANK_NAME").getString());
            rsResultSet.updateString("BANK_ADDRESS",getAttribute("BANK_ADDRESS").getString());
            rsResultSet.updateString("BANK_CITY",getAttribute("BANK_CITY").getString());
            rsResultSet.updateString("INST1",getAttribute("INST1").getString());
            rsResultSet.updateString("INST2",getAttribute("INST2").getString());
            rsResultSet.updateString("BNKHUN",getAttribute("BNKHUN").getString());
            rsResultSet.updateString("LOCADD1",getAttribute("LOCADD1").getString());
            rsResultSet.updateString("LOCADD2",getAttribute("LOCADD2").getString());
            rsResultSet.updateDouble("AMT",(double)getAttribute("AMT").getDouble());
            rsResultSet.updateString("BNGDOCIND",getAttribute("BNGDOCIND").getString());
            rsResultSet.updateString("LOCALBANK",getAttribute("LOCALBANK").getString());
            
            rsResultSet.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
            rsResultSet.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsResultSet.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED",false);
            
            rsResultSet.updateRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER_H WHERE LC_OPENER_CODE='"+(String)getAttribute("LC_OPENER_CODE").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("PARTY_CODE").getObj();
            
            rsHistory.moveToInsertRow();
            
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            
            
            rsHistory.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsHistory.updateString("LC_NO",getAttribute("LC_NO").getString());
            rsHistory.updateString("EXP_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("EXP_DATE").getString()));
            rsHistory.updateString("IND",getAttribute("IND").getString());
            rsHistory.updateDouble("DISC",(double)getAttribute("DISC").getDouble());
            rsHistory.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            rsHistory.updateString("ADDRESS",getAttribute("ADDRESS").getString());
            rsHistory.updateString("LC_OPENER_CODE",getAttribute("LC_OPENER_CODE").getString());
            rsHistory.updateInt("BANK_ID",(int)getAttribute("BANK_ID").getVal());
            rsHistory.updateString("BANK_NAME",getAttribute("BANK_NAME").getString());
            rsHistory.updateString("BANK_ADDRESS",getAttribute("BANK_ADDRESS").getString());
            rsHistory.updateString("BANK_CITY",getAttribute("BANK_CITY").getString());
            rsHistory.updateString("INST1",getAttribute("INST1").getString());
            rsHistory.updateString("INST2",getAttribute("INST2").getString());
            rsHistory.updateString("BNKHUN",getAttribute("BNKHUN").getString());
            rsHistory.updateString("LOCADD1",getAttribute("LOCADD1").getString());
            rsHistory.updateString("LOCADD2",getAttribute("LOCADD2").getString());
            rsHistory.updateDouble("AMT",(double)getAttribute("AMT").getDouble());
            rsHistory.updateString("BNGDOCIND",getAttribute("BNGDOCIND").getString());
            rsHistory.updateString("LOCALBANK",getAttribute("LOCALBANK").getString());
            
            rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
            rsHistory.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsHistory.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("CANCELLED",false);
            
            rsHistory.insertRow();
            
            
            //======== Update the Approval Flow =========
            //ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=606; //Gatepass Requisition
            ObjFlow.DocNo=(String)getAttribute("LC_OPENER_CODE").getObj();
//            ObjFlow.DocNo=(String)getAttribute("PARTY_CODE").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="PRODUCTION.FELT_SALES_LC_PARTY_MASTER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="LC_OPENER_CODE";
            //==== Handling Rejected Documents ==========//
            
            
            if(AStatus.equals("R")) {
                //Remove the Rejected Flag First
                //data.Execute("UPDATE D_INV_INDENT_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from PRODUCTION.FELT_PROD_DOC_DATA
                //data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=3 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                //ObjFlow.IsCreator=true;
                ObjFlow.To=(int)getAttribute("SEND_DOC_TO").getVal();
                ObjFlow.ExplicitSendTo=true;
            }
            //==========================================//
            
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.FELT_SALES_LC_PARTY_MASTER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE LC_OPENER_CODE='"+ObjFlow.DocNo+"'");
                //Remove Old Records from PRODUCTION.FELT_PROD_DOC_DATA
                data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=606 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
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
            //--------- Approval Flow Update complete -----------
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public boolean ShowHistory(int pCompanyID,String pPartyCode) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER WHERE LC_OPENER_CODE='"+pPartyCode+"'");
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
    
    
    
    public boolean CanDelete(int pCompanyID,String pDocNo,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView) {
                return false;
            }
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER WHERE LC_OPENER_CODE='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=606 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
    
    public boolean Delete(int pUserID) {
        try {
            int lCompanyID=(int)getAttribute("COMPANY_ID").getVal();
            String lDocNo=(String)getAttribute("PARTY_CODE").getObj();
            String strSQL="";
            
            //First check that record is editable
            if(CanDelete(lCompanyID,lDocNo,pUserID)) {
                String strQry = "DELETE FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER WHERE LC_OPENER_CODE='"+lDocNo+"'";
                data.Execute(strQry);
                strQry = "DELETE FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER WHERE LC_OPENER_CODE='"+lDocNo+"'";
                data.Execute(strQry);
                
                LoadData(lCompanyID);
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
    
    
    public boolean IsEditable(int pCompanyID,String pDocNo,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER WHERE LC_OPENER_CODE='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=606 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
    
    public static Object getObjectEx(int pCompanyID,String lDocNo) {
        String strCondition = " WHERE LC_OPENER_CODE='" + lDocNo + "'" ;
        clsFeltLCPartyMaster ObjLCPartyMaster = new clsFeltLCPartyMaster();
        
        ObjLCPartyMaster.LoadData(pCompanyID);
        ObjLCPartyMaster.Filter(strCondition,pCompanyID);
        return ObjLCPartyMaster;
    }
    
    
    public boolean Filter(String pCondition,int pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER ORDER BY PARTY_CODE";
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
      //  ResultSet  rsResultSet;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getConn();
        
        //HashMap List=new HashMap();
        long Counter=0;
        int RevNo=0;
        
        try {
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",rsResultSet.getInt("REVISION_NO"));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            setAttribute("COPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("PARTY_CODE",rsResultSet.getString("PARTY_CODE"));
            setAttribute("LC_NO",rsResultSet.getString("LC_NO"));
            setAttribute("EXP_DATE",rsResultSet.getString("EXP_DATE"));
            setAttribute("IND",rsResultSet.getString("IND"));
            setAttribute("DISC",rsResultSet.getInt("DISC"));
            setAttribute("PARTY_NAME",rsResultSet.getString("PARTY_NAME"));
            setAttribute("ADDRESS",rsResultSet.getString("ADDRESS"));
            setAttribute("LC_OPENER_CODE",rsResultSet.getString("LC_OPENER_CODE"));
            setAttribute("BANK_ID",rsResultSet.getInt("BANK_ID"));
            setAttribute("BANK_NAME",rsResultSet.getString("BANK_NAME"));
            setAttribute("BANK_ADDRESS",rsResultSet.getString("BANK_ADDRESS"));
            setAttribute("BANK_CITY",rsResultSet.getString("BANK_CITY"));
            setAttribute("INST1",rsResultSet.getString("INST1"));
            setAttribute("INST2",rsResultSet.getString("INST2"));
            setAttribute("BNKHUN",rsResultSet.getString("BNKHUN"));
            setAttribute("LOCADD1",rsResultSet.getString("LOCADD1"));
            setAttribute("LOCADD2",rsResultSet.getString("LOCADD2"));
            setAttribute("AMT",rsResultSet.getDouble("AMT"));
            setAttribute("BNGDOCIND",rsResultSet.getString("BNGDOCIND"));
            setAttribute("LOCALBANK",rsResultSet.getString("LOCALBANK"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDateDB(rsResultSet.getString("CREATED_DATE")));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            setAttribute("HIERARCHY_ID",rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsResultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE",rsResultSet.getString("REJECTED_DATE"));
            setAttribute("REJECTED_REMARKS",rsResultSet.getString("REJECTED_REMARKS"));
            setAttribute("CHANGED",rsResultSet.getBoolean("CHANGED"));
            setAttribute("CHANGED_DATE",rsResultSet.getString("CHANGED_DATE"));
            setAttribute("CANCELLED",rsResultSet.getInt("CANCELLED"));
            
            
            clsFeltLcPartyMasterDetails ObjLcPartyMasterDetails = new clsFeltLcPartyMasterDetails();
            
                ObjLcPartyMasterDetails.setAttribute("COPANY_ID",rsResultSet.getString("COPANY_ID"));
                ObjLcPartyMasterDetails.setAttribute("PARTY_CODE",rsResultSet.getString("PARTY_CODE"));
                ObjLcPartyMasterDetails.setAttribute("LC_NO",rsResultSet.getString("LC_NO"));
                ObjLcPartyMasterDetails.setAttribute("EXP_DATE",EITLERPGLOBAL.formatDateDB(rsResultSet.getString("EXP_DATE")));
                ObjLcPartyMasterDetails.setAttribute("DISC",rsResultSet.getString("DISC"));
                ObjLcPartyMasterDetails.setAttribute("PARTY_NAME",rsResultSet.getString("PARTY_NAME"));
                ObjLcPartyMasterDetails.setAttribute("ADDRESS",rsResultSet.getString("ADDRESS"));
                ObjLcPartyMasterDetails.setAttribute("LC_OPENER_CODE",rsResultSet.getString("LC_OPENER_CODE"));
                ObjLcPartyMasterDetails.setAttribute("BANK_ID",rsResultSet.getString("BANK_ID"));
                ObjLcPartyMasterDetails.setAttribute("BANK_NAME",rsResultSet.getString("BANK_NAME"));
                ObjLcPartyMasterDetails.setAttribute("BANK_ADDRESS",rsResultSet.getString("BANK_ADDRESS"));
                ObjLcPartyMasterDetails.setAttribute("BANK_CITY",rsResultSet.getString("BANK_CITY"));
                ObjLcPartyMasterDetails.setAttribute("INST1",rsResultSet.getString("INST1"));
                ObjLcPartyMasterDetails.setAttribute("INST2",rsResultSet.getString("INST2"));
                ObjLcPartyMasterDetails.setAttribute("BNKHUN",rsResultSet.getString("BNKHUN"));
                ObjLcPartyMasterDetails.setAttribute("LOCADD1",rsResultSet.getString("LOCADD1"));
                ObjLcPartyMasterDetails.setAttribute("LOCADD2",rsResultSet.getString("LOCADD2"));
                ObjLcPartyMasterDetails.setAttribute("AMT",rsResultSet.getString("AMT"));
                ObjLcPartyMasterDetails.setAttribute("BNGDOCIND",rsResultSet.getString("BNGDOCIND"));
                ObjLcPartyMasterDetails.setAttribute("LOCALBANK",rsResultSet.getString("LOCALBANK"));
                //ObjLcPartyMasterDetails.setAttribute("AMT",rsResultSet.getString("CON_DATE"));
                
                rsResultSet.close();
                
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public static HashMap getHistoryList(int pCompanyID,String PartyCode) {
        HashMap List=new HashMap();
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp=stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER_H WHERE LC_OPENER_CODE='"+PartyCode+"'");
            
            while(rsTmp.next()) {
                clsFeltLCPartyMaster ObjLCPartyMaster=new clsFeltLCPartyMaster();
                
                ObjLCPartyMaster.setAttribute("PARTY_CODE",rsTmp.getString("PARTY_CODE"));
                ObjLCPartyMaster.setAttribute("EXP_DATE",EITLERPGLOBAL.formatDateDB(rsTmp.getString("EXP_DATE")));
                ObjLCPartyMaster.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjLCPartyMaster.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjLCPartyMaster.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjLCPartyMaster.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjLCPartyMaster.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjLCPartyMaster);
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
    
    
    public static HashMap getPendingApprovals(int pCompanyID,int pUserID,int pOrder) {
        String strSQL="";
        Connection tmpConn;
        tmpConn=data.getConn();
        ResultSet rsTmp;
        Statement tmpStmt;
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            if(pOrder==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT PRODUCTION.FELT_SALES_LC_PARTY_MASTER.LC_NO, PRODUCTION.FELT_SALES_LC_PARTY_MASTER.DISC, PRODUCTION.FELT_SALES_LC_PARTY_MASTER.PARTY_CODE,RECEIVED_DATE FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_SALES_LC_PARTY_MASTER.LC_OPENER_CODE=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND PRODUCTION.FELT_PROD_DOC_DATA.MODULE_ID=606 ORDER BY PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT PRODUCTION.FELT_SALES_LC_PARTY_MASTER.LC_NO, PRODUCTION.FELT_SALES_LC_PARTY_MASTER.DISC,PRODUCTION.FELT_SALES_LC_PARTY_MASTER.PARTY_CODE,RECEIVED_DATE FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_SALES_LC_PARTY_MASTER.LC_OPENER_CODE=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND PRODUCTION.FELT_PROD_DOC_DATA.MODULE_ID=606 ORDER BY PRODUCTION.FELT_SALES_LC_PARTY_MASTER.PARTY_CODE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT PRODUCTION.FELT_SALES_LC_PARTY_MASTER.LC_NO, PRODUCTION.FELT_SALES_LC_PARTY_MASTER.DISC,PRODUCTION.FELT_SALES_LC_PARTY_MASTER.PARTY_CODE,RECEIVED_DATE FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_SALES_LC_PARTY_MASTER.LC_OPENER_CODE=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND PRODUCTION.FELT_PROD_DOC_DATA.MODULE_ID=606 ORDER BY PRODUCTION.FELT_SALES_LC_PARTY_MASTER.PARTY_CODE";
            }
            
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                
                    Counter=Counter+1;
                    clsFeltLCPartyMaster ObjDoc=new clsFeltLCPartyMaster();
                    
                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("PARTY_CODE",rsTmp.getString("PARTY_CODE"));
                    ObjDoc.setAttribute("LC_NO",rsTmp.getString("LC_NO"));
                    ObjDoc.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                   // ObjDoc.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
                     ObjDoc.setAttribute("DEPT_ID","");
                    // ----------------- End of Header Fields ------------------------------------//
                    
                    //Put the prepared user object into list
                    List.put(Long.toString(Counter),ObjDoc);
                
                
                if(!rsTmp.isAfterLast()) {
                    rsTmp.next();
                }
            }//Out While
            
            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();
            
        }
        catch(Exception e) {
        }
        
        return List;
    }
     
//   public static String getLcOpenerCode(int pCompanyID,String pPartyID) {
//        Connection tmpConn;
//        Statement stTmp;
//        ResultSet rsTmp;
//        String MainAccountCode="";
//        
//        try {
//            tmpConn=data.getConn();
//            stTmp=tmpConn.createStatement();
//            rsTmp=stTmp.executeQuery("SELECT LCO_OPENER_CODE FROM DINESHMILLS.D_SAL_LC_OPENER_MASTER WHERE LCO_PARTY_CODE="+pPartyID);
//            rsTmp.first();
//            
//            if(rsTmp.getRow()>0) {
//                MainAccountCode=rsTmp.getString("LCO_OPENER_CODE");
//            }
//            
//            //tmpConn.close();
//            stTmp.close();
//            rsTmp.close();
//            
//        }
//        catch(Exception e) {
//            
//        }
//        return MainAccountCode;
//    }
// 
   public static String getLcOpenerName(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010' AND APPROVED=1 AND CANCELLED=0 AND PARTY_CODE="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                PartyName=rsTmp.getString("PARTY_NAME");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return PartyName;
    }
   
   public static String getLcOpenerAddress(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Address1="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT ADDRESS1 FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010' AND APPROVED=1 AND CANCELLED=0 AND PARTY_CODE="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Address1=rsTmp.getString("ADDRESS1");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return Address1;
    }
   
   public static String  getBankName(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String BankName="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT BANK_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010' AND APPROVED=1 AND CANCELLED=0 AND PARTY_CODE="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                BankName=rsTmp.getString("BANK_NAME");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return BankName;
    }
    
   public static String  getBankAddress(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String BankAddress="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT BANK_ADDRESS FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010' AND APPROVED=1 AND CANCELLED=0 AND PARTY_CODE="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                BankAddress=rsTmp.getString("BANK_ADDRESS");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return BankAddress;
    }
    
   public static String  getBankCity(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String BankCity="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT BANK_CITY FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010' AND APPROVED=1 AND CANCELLED=0 AND PARTY_CODE="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                BankCity=rsTmp.getString("BANK_CITY");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return BankCity;
    }
    
   public static String  getAmount(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Amount="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT AMOUNT_LIMIT FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010' AND APPROVED=1 AND CANCELLED=0 AND PARTY_CODE="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Amount=rsTmp.getString("AMOUNT_LIMIT");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return Amount;
    }
    
   public static String getLocalAddress1(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String LocalAddress1="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT ADDRESS2 FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010' AND APPROVED=1 AND CANCELLED=0 AND PARTY_CODE="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                LocalAddress1=rsTmp.getString("ADDRESS2");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return LocalAddress1;
    }
    
   public static String getLocalAddress2(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String LocalAddress2="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT CITY_ID FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010' AND APPROVED=1 AND CANCELLED=0 AND PARTY_CODE="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                LocalAddress2=rsTmp.getString("CITY_ID");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return LocalAddress2;
    }
     
   public static String  getLcIndicator(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String LcIndicator="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT PARENT_PARTY_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010' AND APPROVED=1 AND CANCELLED=0 AND PARTY_CODE="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                LcIndicator=rsTmp.getString("PARENT_PARTY_CODE");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return LcIndicator;
    }
  
  public static String getInst1(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Inst1="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
           // rsTmp=stTmp.executeQuery("SELECT INST FROM DINESHMILLS.FELT_SALES_LC_PARTY_MASTER WHERE PARTY_CODE="+pPartyID);
            rsTmp=stTmp.executeQuery("SELECT INST1 FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER WHERE PARTY_CODE="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Inst1=rsTmp.getString("INST1");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return Inst1;
    }
  
  public static String getInst2(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Inst2="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT INST2 FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER WHERE PARTY_CODE="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Inst2=rsTmp.getString("INST2");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return Inst2;   
    }
  
  public static String getBankHundi(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String BankHundi="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT BNKHUN FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER WHERE PARTY_CODE="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                BankHundi=rsTmp.getString("BNKHUN ");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return BankHundi;   
    }

  public static String  getLCDiscount(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String LCDiscount="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT DISC FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER WHERE PARTY_CODE="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                LCDiscount=rsTmp.getString("DISC");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return LCDiscount;   
    }
 
  public static String getLCNo1(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String LCNo1="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT LC_NO FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER WHERE PARTY_CODE="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                LCNo1=rsTmp.getString("LC_NO");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return LCNo1;   
    }
  
 public static String getLocalBank(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String LocalBank="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT LOCALBANK FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER WHERE PARTY_CODE="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                LocalBank=rsTmp.getString("LOCALBANK");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return LocalBank;   
    }
 
}



