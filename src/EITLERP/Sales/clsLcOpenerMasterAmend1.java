/*
 *  clsLcOpenerMasterAmend1.java
 *
 * Created on April 27, 2009, 12:43 PM
 */

package EITLERP.Sales;

import java.util.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import java.io.*;
import EITLERP.Sales.*;

/**
 *
 * @author  root
 */
public class  clsLcOpenerMasterAmend1 {
    
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    private boolean HistoryView=false;
    private HashMap props;
    public boolean Ready = false;
    public String LastError;
    public static int ModuleID=728;
    private String SelPrefix=""; //Selected Prefix
    private String SelSuffix=""; //Selected Prefix
    private int FFNo=0;
    //public String lDocNo="";
    
    //  public Variant getAttribute(String PropName) {
    //    return (Variant) props.get(PropName);
    // }
    
    
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
    
    /** Creates a new instance of  clsLcOpenerMasterAmend1 */
    public  clsLcOpenerMasterAmend1() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("LCO_AMD_NO",new Variant(""));
        props.put("LCO_OPENER_CODE",new Variant(""));
        props.put("LCO_OPENER_NAME",new Variant(""));
        props.put("LCO_ADDRESS1",new Variant(""));
        props.put("LCO_BANK_ID",new Variant(0));
        props.put("LCO_BANK_NAME",new Variant(""));
        props.put("LCO_BANK_ADDRESS",new Variant(""));
        props.put("LCO_BANK_CITY",new Variant(""));
        props.put("LCO_INST1",new Variant(""));
        props.put("LCO_INST2",new Variant(""));
        props.put("LCO_BNKHUN",new Variant(""));
        props.put("LCO_LOCADD1",new Variant(""));
        props.put("LCO_LOCADD2",new Variant(""));
        props.put("LCO_CRITICAL_LIMIT",new Variant(""));
        props.put("LCO_AMT",new Variant(0));
        props.put("LCO_LOCALBANK",new Variant(""));
        props.put("LCO_REMARK",new Variant(""));
        props.put("LCO_EXPIRY_DATE",new Variant("0000-00-00"));
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
        
        // -- Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        //  props.put("REJECTED_REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        props.put("SEND_DOC_TO",new Variant(0));
        props.put("APPROVAL_REMARKS",new Variant(0));
    }
    
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_SAL_LC_OPENER_MASTER_AMEND WHERE COMPANY_ID="+pCompanyID+" ORDER BY APPROVED,LCO_AMD_NO");
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
            rsResultSet.first();
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
            java.sql.Date ExpDate=java.sql.Date.valueOf(EITLERPGLOBAL.formatDateDB(getAttribute("LCO_EXPIRY_DATE").getString()));
            
            String AStatus=getAttribute("APPROVAL_STATUS").getString();
            
          
                
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHistory=stHistory.executeQuery("SELECT * FROM D_SAL_LC_OPENER_MASTER_AMEND_H WHERE LCO_AMD_NO=''"); // '1' for restricting all data retrieval
            rsHistory.first();
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_SAL_LC_OPENER_MASTER_AMEND WHERE LCO_AMD_NO=''");
            rsHDetail.first();
            //------------------------------------//
            
            
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateString("LCO_AMD_NO",getAttribute("LCO_AMD_NO").getString());
            rsResultSet.updateString("LCO_OPENER_CODE",getAttribute("LCO_OPENER_CODE").getString());
            rsResultSet.updateString("LCO_OPENER_NAME",getAttribute("LCO_OPENER_NAME").getString());
            rsResultSet.updateString("LCO_EXPIRY_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("LCO_EXPIRY_DATE").getString()));
            rsResultSet.updateString("LCO_ADDRESS1",getAttribute("LCO_ADDRESS1").getString());
            rsResultSet.updateInt("LCO_BANK_ID",(int)getAttribute("LCO_BANK_ID").getVal());
            rsResultSet.updateString("LCO_BANK_NAME",getAttribute("LCO_BANK_NAME").getString());
            rsResultSet.updateString("LCO_BANK_ADDRESS",getAttribute("LCO_BANK_ADDRESS").getString());
            rsResultSet.updateString("LCO_BANK_CITY",getAttribute("LCO_BANK_CITY").getString());
            rsResultSet.updateString("LCO_INST1",getAttribute("LCO_INST1").getString());
            rsResultSet.updateString("LCO_INST2",getAttribute("LCO_INST2").getString());
            rsResultSet.updateString("LCO_BNKHUN",getAttribute("LCO_BNKHUN").getString());
            rsResultSet.updateString("LCO_LOCADD1",getAttribute("LCO_LOCADD1").getString());
            rsResultSet.updateString("LCO_LOCADD2",getAttribute("LCO_LOCADD2").getString());
            rsResultSet.updateString("LCO_AMT",getAttribute("LCO_AMT").getString());
            rsResultSet.updateDouble("LCO_CRITICAL_LIMIT",Double.parseDouble((String) getAttribute("LCO_CRITICAL_LIMIT").getString()));
            rsResultSet.updateString("LCO_LOCALBANK",getAttribute("LCO_LOCALBANK").getString());
            rsResultSet.updateString("LCO_REMARKS",getAttribute("LCO_REMARKS").getString());
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
            
            rsHistory.updateInt("REVISION_NO",1);
            
            rsHistory.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateInt("UPDATE_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("APPROVAL_REMARKS",getAttribute("APPROVAL_REMARKS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
            rsHistory.updateString("LCO_AMD_NO",getAttribute("LCO_AMD_NO").getString());
            rsHistory.updateString("LCO_OPENER_CODE",getAttribute("LCO_OPENER_CODE").getString());
            rsHistory.updateString("LCO_OPENER_NAME",getAttribute("LCO_OPENER_NAME").getString());
            rsHistory.updateString("LCO_EXPIRY_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("LCO_EXPIRY_DATE").getString()));
            rsHistory.updateString("LCO_ADDRESS1",getAttribute("LCO_ADDRESS1").getString());
            rsHistory.updateInt("LCO_BANK_ID",(int)getAttribute("LCO_BANK_ID").getVal());
            rsHistory.updateString("LCO_BANK_NAME",getAttribute("LCO_BANK_NAME").getString());
            rsHistory.updateString("LCO_BANK_ADDRESS",getAttribute("LCO_BANK_ADDRESS").getString());
            rsHistory.updateString("LCO_BANK_CITY",getAttribute("LCO_BANK_CITY").getString());
            rsHistory.updateString("LCO_INST1",getAttribute("LCO_INST1").getString());
            rsHistory.updateString("LCO_INST2",getAttribute("LCO_INST2").getString());
            rsHistory.updateString("LCO_BNKHUN",getAttribute("LCO_BNKHUN").getString());
            rsHistory.updateString("LCO_LOCADD1",getAttribute("LCO_LOCADD1").getString());
            rsHistory.updateString("LCO_LOCADD2",getAttribute("LCO_LOCADD2").getString());
            rsHistory.updateDouble("LCO_CRITICAL_LIMIT",Double.parseDouble((String) getAttribute("LCO_CRITICAL_LIMIT").getString()));
            rsHistory.updateString("LCO_AMT",getAttribute("LCO_AMT").getString());
            rsHistory.updateString("LCO_REMARKS",getAttribute("LCO_REMARKS").getString());
            rsHistory.updateString("LCO_LOCALBANK",getAttribute("LCO_LOCALBANK").getString());
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
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=728; //Material Requisition
            ObjFlow.DocNo=(String)getAttribute("LCO_AMD_NO").getObj();
            ObjFlow.lDocNo=(String)getAttribute("LCO_OPENER_CODE").getObj();
            ObjFlow.DocDate="0000-00-00";
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="DINESHMILLS.D_SAL_LC_OPENER_MASTER_AMEND";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="LCO_AMD_NO";
            
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
            if(ObjFlow.Status.equals("F")){
                
                data.Execute("DELETE FROM DINESHMILLS.D_SAL_LC_OPENER_MASTER WHERE LCO_OPENER_CODE='"+ObjFlow.lDocNo+"' AND COMPANY_ID='"+ObjFlow.CompanyID+"'");
                data.Execute("INSERT INTO DINESHMILLS.D_SAL_LC_OPENER_MASTER (COMPANY_ID,LCO_OPENER_CODE,LCO_OPENER_NAME,LCO_ADDRESS1,LCO_BANK_ID,LCO_BANK_NAME,LCO_BANK_ADDRESS,LCO_BANK_CITY,LCO_INST1,LCO_INST2,LCO_BNKHUN,LCO_LOCADD1,LCO_LOCADD2,LCO_AMT,LCO_LOCALBANK,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,CANCELLED,HIERARCHY_ID,CHANGED,CHANGED_DATE) (SELECT COMPANY_ID,LCO_OPENER_CODE,LCO_OPENER_NAME,LCO_ADDRESS1,LCO_BANK_ID,LCO_BANK_NAME,LCO_BANK_ADDRESS,LCO_BANK_CITY,LCO_INST1,LCO_INST2,LCO_BNKHUN,LCO_LOCADD1,LCO_LOCADD2,LCO_AMT,LCO_LOCALBANK,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,CANCELLED,HIERARCHY_ID,CHANGED,CHANGED_DATE FROM D_SAL_LC_OPENER_MASTER_AMEND WHERE LCO_OPENER_CODE='"+ObjFlow.lDocNo+"')");
                
            }
            
            
            
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
        
        Statement stHistory,stHeader,stHDetail;
        ResultSet rsHistory,rsHeader,rsHDetail;
        boolean Validate=true;
        
        try {
            
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            if(AStatus.equals("R")) {
                Validate=false;
            }
            Validate=true;
            
            String theDocNo=(String)getAttribute("LCO_AMD_NO").getObj();
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHistory=stHistory.executeQuery("SELECT * FROM D_SAL_LC_OPENER_MASTER_AMEND_H WHERE LCO_AMD_NO=''"); // '1' for restricting all data retrieval
            rsHistory.first();
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_SAL_LC_OPENER_MASTER_AMEND WHERE LCO_AMD_NO=''");
            rsHDetail.first();
            //------------------------------------//
            
            //String theDocNo=(String)getAttribute("LCO_AMD_NO").getObj();
            
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            
            rsResultSet.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateString("LCO_AMD_NO",getAttribute("LCO_AMD_NO").getString());
            rsResultSet.updateString("LCO_OPENER_CODE",getAttribute("LCO_OPENER_CODE").getString());
            rsResultSet.updateString("LCO_OPENER_NAME",getAttribute("LCO_OPENER_NAME").getString());
            rsResultSet.updateString("LCO_EXPIRY_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("LCO_EXPIRY_DATE").getString()));
            rsResultSet.updateString("LCO_ADDRESS1",getAttribute("LCO_ADDRESS1").getString());
            rsResultSet.updateInt("LCO_BANK_ID",(int)getAttribute("LCO_BANK_ID").getVal());
            rsResultSet.updateString("LCO_BANK_NAME",getAttribute("LCO_BANK_NAME").getString());
            rsResultSet.updateString("LCO_BANK_ADDRESS",getAttribute("LCO_BANK_ADDRESS").getString());
            rsResultSet.updateString("LCO_BANK_CITY",getAttribute("LCO_BANK_CITY").getString());
            rsResultSet.updateString("LCO_INST1",getAttribute("LCO_INST1").getString());
            rsResultSet.updateString("LCO_INST2",getAttribute("LCO_INST2").getString());
            rsResultSet.updateString("LCO_BNKHUN",getAttribute("LCO_BNKHUN").getString());
            rsResultSet.updateString("LCO_LOCADD1",getAttribute("LCO_LOCADD1").getString());
            rsResultSet.updateString("LCO_LOCADD2",getAttribute("LCO_LOCADD2").getString());
            rsResultSet.updateDouble("LCO_CRITICAL_LIMIT",Double.parseDouble((String) getAttribute("LCO_CRITICAL_LIMIT").getString()));
            rsResultSet.updateString("LCO_AMT",getAttribute("LCO_AMT").getString());
            rsResultSet.updateString("LCO_REMARKS",getAttribute("LCO_REMARKS").getString());
            rsResultSet.updateString("LCO_LOCALBANK",getAttribute("LCO_LOCALBANK").getString());
            rsResultSet.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
            rsResultSet.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsResultSet.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateBoolean("APPROVED",true);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED",false);
            
            rsResultSet.updateRow();
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            //  int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_SAL_LC_OPENER_MASTER_AMEND_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND LCO_OPENER_CODE='"+(String)getAttribute("LCO_OPENER_CODE").getObj()+"'");
            //   RevNo++;
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_SAL_LC_OPENER_MASTER_AMEND_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND LCO_AMD_NO='"+(String)getAttribute("LCO_AMD_NO").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("LCO_AMD_NO").getObj();
            
            rsHistory.moveToInsertRow();
            
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATE_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVAL_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("LCO_AMD_NO",getAttribute("LCO_AMD_NO").getString());
            rsHistory.updateString("LCO_OPENER_CODE",getAttribute("LCO_OPENER_CODE").getString());
            rsHistory.updateString("LCO_OPENER_NAME",getAttribute("LCO_OPENER_NAME").getString());
            rsHistory.updateString("LCO_EXPIRY_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("LCO_EXPIRY_DATE").getString()));
            rsHistory.updateString("LCO_ADDRESS1",getAttribute("LCO_ADDRESS1").getString());
            rsHistory.updateInt("LCO_BANK_ID",(int)getAttribute("LCO_BANK_ID").getVal());
            rsHistory.updateString("LCO_BANK_NAME",getAttribute("LCO_BANK_NAME").getString());
            rsHistory.updateString("LCO_BANK_ADDRESS",getAttribute("LCO_BANK_ADDRESS").getString());
            rsHistory.updateString("LCO_BANK_CITY",getAttribute("LCO_BANK_CITY").getString());
            rsHistory.updateString("LCO_INST1",getAttribute("LCO_INST1").getString());
            rsHistory.updateString("LCO_INST2",getAttribute("LCO_INST2").getString());
            rsHistory.updateString("LCO_BNKHUN",getAttribute("LCO_BNKHUN").getString());
            rsHistory.updateString("LCO_LOCADD1",getAttribute("LCO_LOCADD1").getString());
            rsHistory.updateString("LCO_LOCADD2",getAttribute("LCO_LOCADD2").getString());
            rsHistory.updateString("LCO_AMT",getAttribute("LCO_AMT").getString());
            rsHistory.updateDouble("LCO_CRITICAL_LIMIT",Double.parseDouble((String) getAttribute("LCO_CRITICAL_LIMIT").getString()));
            rsHistory.updateString("LCO_LOCALBANK",getAttribute("LCO_LOCALBANK").getString());
            rsHistory.updateString("LCO_REMARKS",getAttribute("LCO_REMARKS").getString());
            rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
            rsHistory.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsHistory.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateBoolean("APPROVED",true);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("CANCELLED",false);
            
            rsHistory.insertRow();
            
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=728; //Gatepass Requisition
            ObjFlow.DocNo=(String)getAttribute("LCO_AMD_NO").getObj();
            ObjFlow.lDocNo=(String)getAttribute("LCO_OPENER_CODE").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="DINESHMILLS.D_SAL_LC_OPENER_MASTER_AMEND_H";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="LCO_AMD_NO";
            //==== Handling Rejected Documents ==========//
            
            
            if(AStatus.equals("R")) {
                
                //ObjFlow.IsCreator=true;
                ObjFlow.To=(int)getAttribute("SEND_DOC_TO").getVal();
                ObjFlow.ExplicitSendTo=true;
            }
            //==========================================//
            
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                data.Execute("UPDATE D_SAL_LC_OPENER_MASTER_AMEND SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND LCO_AMD_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=728 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                
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
            if(ObjFlow.Status.equals("F")){
                
                data.Execute("DELETE FROM DINESHMILLS.D_SAL_LC_OPENER_MASTER WHERE LCO_OPENER_CODE='"+ObjFlow.lDocNo+"' AND COMPANY_ID='"+ObjFlow.CompanyID+"'");
                data.Execute("INSERT INTO DINESHMILLS.D_SAL_LC_OPENER_MASTER (COMPANY_ID,LCO_OPENER_CODE,LCO_OPENER_NAME,LCO_ADDRESS1,LCO_BANK_ID,LCO_BANK_NAME,LCO_BANK_ADDRESS,LCO_BANK_CITY,LCO_INST1,LCO_INST2,LCO_BNKHUN,LCO_LOCADD1,LCO_LOCADD2,LCO_AMT,LCO_LOCALBANK,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,CANCELLED,HIERARCHY_ID,CHANGED,CHANGED_DATE) (SELECT COMPANY_ID,LCO_OPENER_CODE,LCO_OPENER_NAME,LCO_ADDRESS1,LCO_BANK_ID,LCO_BANK_NAME,LCO_BANK_ADDRESS,LCO_BANK_CITY,LCO_INST1,LCO_INST2,LCO_BNKHUN,LCO_LOCADD1,LCO_LOCADD2,LCO_AMT,LCO_LOCALBANK,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,CANCELLED,HIERARCHY_ID,CHANGED,CHANGED_DATE FROM D_SAL_LC_OPENER_MASTER_AMEND WHERE LCO_OPENER_CODE='"+ObjFlow.lDocNo+"')");
                
            }
            //--------- Approval Flow Update complete -----------
            
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public boolean ShowHistory(int pCompanyID,String LCNo) {
        Ready=false;
        try {
            //  Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_SAL_LC_OPENER_MASTER_AMEND_H WHERE COMPANY_ID="+pCompanyID+" AND LCO_AMD_NO='"+LCNo+"' ORDER BY REVISION_NO");
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_SAL_LC_OPENER_MASTER_AMEND WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND LCO_AMD_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=728 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
            String lDocNo=(String)getAttribute("LCO_AMD_NO").getObj();
            String strSQL="";
            
            //First check that record is editable
            if(CanDelete(lCompanyID,lDocNo,pUserID)) {
                String strQry = "DELETE FROM D_SAL_LC_OPENER_MASTER_AMEND WHERE COMPANY_ID=" + lCompanyID +" LCO_AMD_NO='"+lDocNo+"'";
                data.Execute(strQry);
                strQry = "DELETE FROM D_SAL_LC_OPENER_MASTER_AMEND WHERE COMPANY_ID=" + lCompanyID +" AND LCO_AMD_NO='"+lDocNo+"'";
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
            
            strSQL="SELECT COUNT(*) AS COUNT FROM DINESHMILLS.D_SAL_LC_OPENER_MASTER_AMEND WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND LCO_AMD_NO='"+pDocNo+"' AND APPROVED=1";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            
            
            if(rsTmp.getInt("COUNT")<0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM DINESHMILLS.D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=728 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
        String strCondition = " WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND LCO_AMD_NO='" + lDocNo + "'" ;
        clsLcOpenerMasterAmend1 ObjLcOpenerMasterAmend1 = new  clsLcOpenerMasterAmend1();
        
        ObjLcOpenerMasterAmend1.LoadData(pCompanyID);
        ObjLcOpenerMasterAmend1.Filter(strCondition,pCompanyID);
        return ObjLcOpenerMasterAmend1;
    }
    
    
    public boolean Filter(String pCondition,int pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_SAL_LC_OPENER_MASTER_AMEND " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_SAL_LC_OPENER_MASTER_AMEND WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" ORDER BY LCO_AMD_NO";
                //strSql = "SELECT * FROM D_SAL_POLICY_FELT_PRICELIST_HEADER " + pCondition ;
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
            
            setAttribute("COMPANY_ID",rsResultSet.getString("COMPANY_ID"));
            setAttribute("LCO_AMD_NO",rsResultSet.getString("LCO_AMD_NO"));
            setAttribute("LCO_OPENER_CODE",rsResultSet.getString("LCO_OPENER_CODE"));
            setAttribute("LCO_OPENER_NAME",rsResultSet.getString("LCO_OPENER_NAME"));
            setAttribute("LCO_ADDRESS1",rsResultSet.getString("LCO_ADDRESS1"));
            //   setAttribute("LCO_EXPIRY_DATE",rsResultSet.getString("LCO_EXPIRY_DATE"));
            setAttribute("LCO_EXPIRY_DATE",EITLERPGLOBAL.formatDate(rsResultSet.getString("LCO_EXPIRY_DATE")));
            setAttribute("LCO_BANK_ID",rsResultSet.getInt("LCO_BANK_ID"));
            setAttribute("LCO_BANK_NAME",rsResultSet.getString("LCO_BANK_NAME"));
            setAttribute("LCO_BANK_ADDRESS",rsResultSet.getString("LCO_BANK_ADDRESS"));
            setAttribute("LCO_BANK_CITY",rsResultSet.getString("LCO_BANK_CITY"));
            setAttribute("LCO_INST1",rsResultSet.getString("LCO_INST1"));
            setAttribute("LCO_INST2",rsResultSet.getString("LCO_INST2"));
            setAttribute("LCO_BNKHUN",rsResultSet.getString("LCO_BNKHUN"));
            setAttribute("LCO_LOCADD1",rsResultSet.getString("LCO_LOCADD1"));
            setAttribute("LCO_LOCADD2",rsResultSet.getString("LCO_LOCADD2"));
            setAttribute("LCO_AMT",rsResultSet.getString("LCO_AMT"));
            setAttribute("LCO_CRITICAL_LIMIT",rsResultSet.getDouble("LCO_CRITICAL_LIMIT"));
            setAttribute("LCO_LOCALBANK",rsResultSet.getString("LCO_LOCALBANK"));
            setAttribute("LCO_REMARKS",rsResultSet.getString("LCO_REMARKS"));
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
            
            
            clsLcOpenerMasterDetailsAmend1 ObjLcOpenerMasterAmend1Details = new  clsLcOpenerMasterDetailsAmend1();
            
            ObjLcOpenerMasterAmend1Details.setAttribute("COMPANY_ID",rsResultSet.getString("COMPANY_ID"));
            ObjLcOpenerMasterAmend1Details.setAttribute("LCO_AMD_NO",rsResultSet.getString("LCO_AMD_NO"));
            ObjLcOpenerMasterAmend1Details.setAttribute("LCO_OPENER_CODE",rsResultSet.getString("LCO_OPENER_CODE"));
            ObjLcOpenerMasterAmend1Details.setAttribute("LCO_OPENER_NAME",rsResultSet.getString("LCO_OPENER_NAME"));
            ObjLcOpenerMasterAmend1Details.setAttribute("LCO_EXPIRY_DATE",EITLERPGLOBAL.formatDateDB(rsResultSet.getString("LCO_EXPIRY_DATE")));
            ObjLcOpenerMasterAmend1Details.setAttribute("LCO_ADDRESS1",rsResultSet.getString("LCO_ADDRESS1"));
            ObjLcOpenerMasterAmend1Details.setAttribute("LCO_BANK_ID",rsResultSet.getString("LCO_BANK_ID"));
            ObjLcOpenerMasterAmend1Details.setAttribute("LCO_BANK_NAME",rsResultSet.getString("LCO_BANK_NAME"));
            ObjLcOpenerMasterAmend1Details.setAttribute("LCO_BANK_ADDRESS",rsResultSet.getString("LCO_BANK_ADDRESS"));
            ObjLcOpenerMasterAmend1Details.setAttribute("LCO_BANK_CITY",rsResultSet.getString("LCO_BANK_CITY"));
            ObjLcOpenerMasterAmend1Details.setAttribute("LCO_INST1",rsResultSet.getString("LCO_INST1"));
            ObjLcOpenerMasterAmend1Details.setAttribute("LCO_INST2",rsResultSet.getString("LCO_INST2"));
            ObjLcOpenerMasterAmend1Details.setAttribute("LCO_BNKHUN",rsResultSet.getString("LCO_BNKHUN"));
            ObjLcOpenerMasterAmend1Details.setAttribute("LCO_LOCADD1",rsResultSet.getString("LCO_LOCADD1"));
            ObjLcOpenerMasterAmend1Details.setAttribute("LCO_LOCADD2",rsResultSet.getString("LCO_LOCADD2"));
            ObjLcOpenerMasterAmend1Details.setAttribute("LCO_AMT",rsResultSet.getString("LCO_AMT"));
            ObjLcOpenerMasterAmend1Details.setAttribute("LCO_CRITICAL_LIMIT",rsResultSet.getString("LCO_CRITICAL_LIMIT"));
            ObjLcOpenerMasterAmend1Details.setAttribute("LCO_LOCALBANK",rsResultSet.getString("LCO_LOCALBANK"));
            ObjLcOpenerMasterAmend1Details.setAttribute("LCO_REMARKS",rsResultSet.getString("LCO_REMARKS"));
            
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
            rsTmp=stTmp.executeQuery("SELECT DISTINCT REVISION_NO,UPDATE_BY,MAX(ENTRY_DATE)AS ENTRY_DATE,APPROVAL_STATUS,APPROVAL_REMARKS FROM DINESHMILLS.D_SAL_LC_OPENER_MASTER_AMEND_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND LCO_AMD_NO='"+PartyCode+"' GROUP BY REVISION_NO,UPDATE_BY,APPROVAL_STATUS");
            
            
            
            while(rsTmp.next()) {
                clsLcOpenerMasterAmend1 ObjLcOpenerMasterAmend1=new clsLcOpenerMasterAmend1();
                
                //    ObjLcOpenerMasterAmend1.setAttribute("LCO_OPENER_CODE",rsTmp.getString("LCO_OPENER_CODE"));
                ObjLcOpenerMasterAmend1.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjLcOpenerMasterAmend1.setAttribute("UPDATE_BY",rsTmp.getInt("UPDATE_BY"));
                ObjLcOpenerMasterAmend1.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjLcOpenerMasterAmend1.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjLcOpenerMasterAmend1.setAttribute("APPROVAL_REMARKS",rsTmp.getString("APPROVAL_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjLcOpenerMasterAmend1);
                
            }
            rsTmp.close();
            stTmp.close();
            return List;
        }
        catch(Exception e) {
            e.printStackTrace();
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
                strSQL="SELECT D_SAL_LC_OPENER_MASTER_AMEND.LCO_AMD_NO,D_SAL_LC_OPENER_MASTER_AMEND.LCO_OPENER_CODE,RECEIVED_DATE FROM D_SAL_LC_OPENER_MASTER_AMEND,D_COM_DOC_DATA WHERE D_SAL_LC_OPENER_MASTER_AMEND.LCO_AMD_NO=D_COM_DOC_DATA.DOC_NO AND D_SAL_LC_OPENER_MASTER_AMEND.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_SAL_LC_OPENER_MASTER_AMEND.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=728 ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                
                strSQL="SELECT D_SAL_LC_OPENER_MASTER_AMEND.LCO_AMD_NO,D_SAL_LC_OPENER_MASTER_AMEND.LCO_OPENER_CODE,RECEIVED_DATE FROM D_SAL_LC_OPENER_MASTER_AMEND,D_COM_DOC_DATA WHERE D_SAL_LC_OPENER_MASTER_AMEND.LCO_AMD_NO=D_COM_DOC_DATA.DOC_NO AND D_SAL_LC_OPENER_MASTER_AMEND.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_SAL_LC_OPENER_MASTER_AMEND.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=728 ORDER BY D_SAL_LC_OPENER_MASTER_AMEND.LCO_AMD_NO";
              //  strSQL="SELECT D_SAL_LC_OPENER_MASTER_AMEND.LCO_AMD_NO,D_SAL_LC_OPENER_MASTER_AMEND.LCO_OPENER_CODE,RECEIVED_DATE FROM D_SAL_LC_OPENER_MASTER_AMEND,D_COM_DOC_DATA WHERE D_SAL_LC_OPENER_MASTER_AMEND.LCO_AMD_NO=D_COM_DOC_DATA.DOC_NO AND D_SAL_LC_OPENER_MASTER_AMEND.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_SAL_LC_OPENER_MASTER_AMEND.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=728 ORDER BY D_SAL_LC_OPENER_MASTER_AMEND.LCO_AMD_NO";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT D_SAL_LC_OPENER_MASTER_AMEND.LCO_AMD_NO,D_SAL_LC_OPENER_MASTER_AMEND.LCO_OPENER_CODE,RECEIVED_DATE FROM D_SAL_LC_OPENER_MASTER_AMEND,D_COM_DOC_DATA WHERE D_SAL_LC_OPENER_MASTER_AMEND.LCO_AMD_NO=D_COM_DOC_DATA.DOC_NO AND D_SAL_LC_OPENER_MASTER_AMEND.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_SAL_LC_OPENER_MASTER_AMEND.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=728 ORDER BY D_SAL_LC_OPENER_MASTER_AMEND.LCO_AMD_NO";
            }
            
            //strSQL="SELECT D_INV_ISSUE_HEADER.ISSUE_NO,D_INV_ISSUE_HEADER.ISSUE_DATE FROM D_INV_ISSUE_HEADER,D_COM_DOC_DATA WHERE D_INV_ISSUE_HEADER.ISSUE_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_ISSUE_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_ISSUE_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=14";
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                
                Counter=Counter+1;
                clsLcOpenerMasterAmend1 ObjDoc=new  clsLcOpenerMasterAmend1();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("LCO_AMD_NO",rsTmp.getString("LCO_AMD_NO"));
                ObjDoc.setAttribute("LCO_OPENER_CODE",rsTmp.getString("LCO_OPENER_CODE"));
                ObjDoc.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
               // ObjDoc.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
               // ObjDoc.setAttribute("DEPT_ID","");
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
    
    public static String getLcOpenerCode(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String MainAccountCode="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT LCO_OPENER_CODE FROM DINESHMILLS.D_SAL_LC_OPENER_MASTER_AMEND WHERE LCO_OPENER_CODE="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                MainAccountCode=rsTmp.getString("LCO_OPENER_CODE");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return MainAccountCode;
    }
    
    public static String getLcOpenerName(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT LCO_OPENER_NAME FROM DINESHMILLS.D_SAL_LC_OPENER_MASTER WHERE LCO_OPENER_CODE="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                PartyName=rsTmp.getString("LCO_OPENER_NAME");
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
            rsTmp=stTmp.executeQuery("SELECT LCO_ADDRESS1 FROM DINESHMILLS.D_SAL_LC_OPENER_MASTER WHERE LCO_OPENER_CODE="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Address1=rsTmp.getString("LCO_ADDRESS1");
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
            rsTmp=stTmp.executeQuery("SELECT LCO_BANK_NAME FROM DINESHMILLS.D_SAL_LC_OPENER_MASTER WHERE LCO_OPENER_CODE="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                BankName=rsTmp.getString("LCO_BANK_NAME");
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
            rsTmp=stTmp.executeQuery("SELECT LCO_BANK_ADDRESS FROM DINESHMILLS.D_SAL_LC_OPENER_MASTER WHERE LCO_OPENER_CODE="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                BankAddress=rsTmp.getString("LCO_BANK_ADDRESS");
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
            rsTmp=stTmp.executeQuery("SELECT LCO_BANK_CITY FROM DINESHMILLS.D_SAL_LC_OPENER_MASTER WHERE LCO_OPENER_CODE="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                BankCity=rsTmp.getString("LCO_BANK_CITY");
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
            rsTmp=stTmp.executeQuery("SELECT LCO_AMT FROM DINESHMILLS.D_SAL_LC_OPENER_MASTER WHERE LCO_OPENER_CODE="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Amount=rsTmp.getString("LCO_AMT");
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
            rsTmp=stTmp.executeQuery("SELECT LCO_LOCADD1 FROM DINESHMILLS.D_SAL_LC_OPENER_MASTER WHERE LCO_OPENER_CODE="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                LocalAddress1=rsTmp.getString("LCO_LOCADD1");
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
            rsTmp=stTmp.executeQuery("SELECT LCO_LOCADD2 FROM DINESHMILLS.D_SAL_LC_OPENER_MASTER WHERE LCO_OPENER_CODE="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                LocalAddress2=rsTmp.getString("LCO_LOCADD2");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return LocalAddress2;
    }
    
    public static String getInst1(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Inst1="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT LCO_INST1 FROM DINESHMILLS.D_SAL_LC_OPENER_MASTER WHERE LCO_OPENER_CODE="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Inst1=rsTmp.getString("LCO_INST1");
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
            rsTmp=stTmp.executeQuery("SELECT LCO_INST2 FROM DINESHMILLS.D_SAL_LC_OPENER_MASTER WHERE LCO_OPENER_CODE="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Inst2=rsTmp.getString("LCO_INST2");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return Inst2;
    }
    
    public static String getExp(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String exp="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT LCO_EXPIRY_DATE FROM DINESHMILLS.D_SAL_LC_OPENER_MASTER WHERE LCO_OPENER_CODE="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                exp=rsTmp.getString("LCO_EXPIRY_DATE");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return exp;
    }
    
    public static String getLocalBank(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String LocalBank="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT LCO_LOCALBANK FROM DINESHMILLS.D_SAL_LC_OPENER_MASTER WHERE LCO_OPENER_CODE="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                LocalBank=rsTmp.getString("LCO_LOCALBANK");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return LocalBank;
    }
    
     public static String getCriticalLimit(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String CriticalLimit="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT LCO_CRITICAL_LIMIT FROM DINESHMILLS.D_SAL_LC_OPENER_MASTER WHERE LCO_OPENER_CODE="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                CriticalLimit=rsTmp.getString("LCO_CRITICAL_LIMIT");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return CriticalLimit;
    }
     
     public static String getLCRemarks(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String LCRemarks="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT LCO_REMARKS FROM DINESHMILLS.D_SAL_LC_OPENER_MASTER WHERE LCO_OPENER_CODE="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                LCRemarks=rsTmp.getString("LCO_REMARKS");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return LCRemarks;
    }
 }



