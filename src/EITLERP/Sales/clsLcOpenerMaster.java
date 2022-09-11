/*
 * clsLcOpenerMaster.java
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
public class clsLcOpenerMaster {
    
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    private boolean HistoryView=false;
    private HashMap props;
    public boolean Ready = false;
    public String LastError;
    public static int ModuleID=727;
    //public static int ModuleID=724; //72
    private String SelPrefix=""; //Selected Prefix
    private String SelSuffix=""; //Selected Prefix
    private int FFNo=0;
    
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
    
    /** Creates a new instance of clsLcOpenerMaster */
    public clsLcOpenerMaster() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
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
        props.put("LCO_AMT",new Variant(0));
        props.put("LCO_CRITICAL_LIMIT",new Variant(0));
        props.put("LCO_LOCALBANK",new Variant(""));
        props.put("LCO_REMARKS",new Variant());
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
        props.put("REJECTED_REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        props.put("SEND_DOC_TO",new Variant(0));
        props.put("APPROVAL_REMARKS",new Variant(0));
    }
    
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_SAL_LC_OPENER_MASTER WHERE COMPANY_ID="+pCompanyID+" ORDER BY APPROVED,LCO_OPENER_CODE");
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
           
            
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHistory=stHistory.executeQuery("SELECT * FROM D_SAL_LC_OPENER_MASTER_H WHERE LCO_OPENER_CODE=''"); // '1' for restricting all data retrieval
            rsHistory.first();
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_SAL_LC_OPENER_MASTER WHERE LCO_OPENER_CODE=''");
            rsHDetail.first();
            //------------------------------------//
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateString("LCO_OPENER_CODE",getAttribute("LCO_OPENER_CODE").getString());
            rsResultSet.updateString("LCO_OPENER_NAME",getAttribute("LCO_OPENER_NAME").getString());
            rsResultSet.updateString("LCO_EXPIRY_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("LCO_EXPIRY_DATE").getString()));
         //   rsResultSet.updateString("LCO_EXPIRY_DATE",getAttribute("LCO_EXPIRY_DATE").getString());
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
            rsResultSet.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
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
        //    rsHistory.updateString("UPDATE_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateInt("UPDATE_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("APPROVAL_REMARKS",getAttribute("APPROVAL_REMARKS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
            rsHistory.updateString("LCO_OPENER_CODE",getAttribute("LCO_OPENER_CODE").getString());
            rsHistory.updateString("LCO_OPENER_NAME",getAttribute("LCO_OPENER_NAME").getString());
            rsHistory.updateString("LCO_EXPIRY_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("LCO_EXPIRY_DATE").getString()));
           // rsHistory.updateString("LCO_EXPIRY_DATE",getAttribute("LCO_EXPIRY_DATE").getString());
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
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            
            //rsResultSet.updateBoolean("CANCELED",(boolean)getAttribute("CANCELED").getBool());
            rsHistory.updateBoolean("CANCELLED",false);
            
            rsHistory.insertRow();
            
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
          //  ObjFlow.CompanyID=2;
            ObjFlow.ModuleID=727; //Material Requisition
            ObjFlow.DocNo=(String)getAttribute("LCO_OPENER_CODE").getObj();
            ObjFlow.DocDate="0000-00-00";
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="DINESHMILLS.D_SAL_LC_OPENER_MASTER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="LCO_OPENER_CODE";
            
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
        
        Statement stHistory,stHeader,stHDetail;
        ResultSet rsHistory,rsHeader,rsHDetail;
        boolean Validate=true;
        
        try {
            
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            if(AStatus.equals("R")) {
                Validate=false;
            }
            Validate=true;
            
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHistory=stHistory.executeQuery("SELECT * FROM D_SAL_LC_OPENER_MASTER_H WHERE LCO_OPENER_CODE=''"); // '1' for restricting all data retrieval
            rsHistory.first();
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_SAL_LC_OPENER_MASTER_H WHERE LCO_OPENER_CODE=''");
            rsHDetail.first();
            //------------------------------------//
            
            String theDocNo=(String)getAttribute("LCO_OPENER_CODE").getObj();
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            
            rsResultSet.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateString("LCO_OPENER_CODE",getAttribute("LCO_OPENER_CODE").getString());
            rsResultSet.updateString("LCO_OPENER_NAME",getAttribute("LCO_OPENER_NAME").getString());
            rsResultSet.updateString("LCO_EXPIRY_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("LCO_EXPIRY_DATE").getString()));
           // rsResultSet.updateString("LCO_EXPIRY_DATE",getAttribute("LCO_EXPIRY_DATE").getString());
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
          //  int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_SAL_LC_OPENER_MASTER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND LCO_OPENER_CODE='"+(String)getAttribute("LCO_OPENER_CODE").getObj()+"'");
         //   RevNo++;
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_SAL_LC_OPENER_MASTER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND LCO_OPENER_CODE='"+(String)getAttribute("LCO_OPENER_CODE").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("LCO_OPENER_CODE").getObj();
            
            rsHistory.moveToInsertRow();
            
                rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
                rsHistory.updateInt("UPDATE_BY",(int)getAttribute("MODIFIED_BY").getVal());
                rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
                rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHistory.updateString("APPROVAL_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
                rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            
            
            rsHistory.updateString("LCO_OPENER_CODE",getAttribute("LCO_OPENER_CODE").getString());
            rsHistory.updateString("LCO_OPENER_NAME",getAttribute("LCO_OPENER_NAME").getString());
      //      rsHistory.updateString("LCO_EXPIRY_DATE",getAttribute("LCO_EXPIRY_DATE").getString());
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
            ObjFlow.ModuleID=727; //Gatepass Requisition
            ObjFlow.DocNo=(String)getAttribute("LCO_OPENER_CODE").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="DINESHMILLS.D_SAL_LC_OPENER_MASTER_H";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="LCO_OPENER_CODE";
            //==== Handling Rejected Documents ==========//
            
            
            if(AStatus.equals("R")) {
                //Remove the Rejected Flag First
                //data.Execute("UPDATE D_INV_INDENT_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                //data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=3 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                //ObjFlow.IsCreator=true;
                ObjFlow.To=(int)getAttribute("SEND_DOC_TO").getVal();
                ObjFlow.ExplicitSendTo=true;
            }
            //==========================================//
            
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                // data.Execute("UPDATE D_SAL_LC_OPENER_MASTER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND LCO_OPENER_CODE='"+ObjFlow.DocNo+"'");
                data.Execute("UPDATE D_SAL_LC_OPENER_MASTER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND LCO_OPENER_CODE='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=727 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                
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
    
    public boolean ShowHistory(int pCompanyID,String LCNo) {
        Ready=false;
        try {
          //  Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_SAL_LC_OPENER_MASTER_H WHERE COMPANY_ID="+pCompanyID+" AND LCO_OPENER_CODE='"+LCNo+"' ORDER BY REVISION_NO");
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_SAL_LC_OPENER_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND LCO_OPENER_CODE='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=727 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
            String lDocNo=(String)getAttribute("LCO_OPENER_CODE").getObj();
            String strSQL="";
            
            //First check that record is editable
            if(CanDelete(lCompanyID,lDocNo,pUserID)) {
                String strQry = "DELETE FROM D_SAL_LC_OPENER_MASTER WHERE COMPANY_ID=" + lCompanyID +" LCO_OPENER_CODE='"+lDocNo+"'";
                data.Execute(strQry);
                strQry = "DELETE FROM D_SAL_LC_OPENER_MASTER WHERE COMPANY_ID=" + lCompanyID +" AND LCO_OPENER_CODE='"+lDocNo+"'";
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
            
            strSQL="SELECT COUNT(*) AS COUNT FROM DINESHMILLS.D_SAL_LC_OPENER_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND LCO_OPENER_CODE='"+pDocNo+"' AND APPROVED=1";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
           
            
           
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
           else {
                strSQL="SELECT COUNT(*) AS COUNT FROM DINESHMILLS.D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=727 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
        String strCondition = " WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND LCO_OPENER_CODE='" + lDocNo + "'" ;
        clsLcOpenerMaster ObjLcOpenerMaster = new clsLcOpenerMaster();
        
        ObjLcOpenerMaster.LoadData(pCompanyID);
        ObjLcOpenerMaster.Filter(strCondition,pCompanyID);
        return ObjLcOpenerMaster;
    }
    
    
    public boolean Filter(String pCondition,int pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_SAL_LC_OPENER_MASTER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_SAL_LC_OPENER_MASTER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" ORDER BY LCO_OPENER_CODE";
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
            setAttribute("LCO_CRITICAL_LIMIT",rsResultSet.getDouble("LCO_CRITICAL_LIMIT"));
            setAttribute("LCO_AMT",rsResultSet.getString("LCO_AMT"));
            setAttribute("LCO_REMARKS",rsResultSet.getString("LCO_REMARKS"));
            setAttribute("LCO_LOCALBANK",rsResultSet.getString("LCO_LOCALBANK"));
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
            
            
            clsLcOpenerMasterDetails ObjLcOpenerMasterDetails = new clsLcOpenerMasterDetails();
            
            ObjLcOpenerMasterDetails.setAttribute("COMPANY_ID",rsResultSet.getString("COMPANY_ID"));
            ObjLcOpenerMasterDetails.setAttribute("LCO_OPENER_CODE",rsResultSet.getString("LCO_OPENER_CODE"));
            ObjLcOpenerMasterDetails.setAttribute("LCO_OPENER_NAME",rsResultSet.getString("LCO_OPENER_NAME"));
            ObjLcOpenerMasterDetails.setAttribute("LCO_EXPIRY_DATE",EITLERPGLOBAL.formatDateDB(rsResultSet.getString("LCO_EXPIRY_DATE")));
            ObjLcOpenerMasterDetails.setAttribute("LCO_ADDRESS1",rsResultSet.getString("LCO_ADDRESS1"));
            ObjLcOpenerMasterDetails.setAttribute("LCO_BANK_ID",rsResultSet.getString("LCO_BANK_ID"));
            ObjLcOpenerMasterDetails.setAttribute("LCO_BANK_NAME",rsResultSet.getString("LCO_BANK_NAME"));
            ObjLcOpenerMasterDetails.setAttribute("LCO_BANK_ADDRESS",rsResultSet.getString("LCO_BANK_ADDRESS"));
            ObjLcOpenerMasterDetails.setAttribute("LCO_BANK_CITY",rsResultSet.getString("LCO_BANK_CITY"));
            ObjLcOpenerMasterDetails.setAttribute("LCO_INST1",rsResultSet.getString("LCO_INST1"));
            ObjLcOpenerMasterDetails.setAttribute("LCO_INST2",rsResultSet.getString("LCO_INST2"));
            ObjLcOpenerMasterDetails.setAttribute("LCO_BNKHUN",rsResultSet.getString("LCO_BNKHUN"));
            ObjLcOpenerMasterDetails.setAttribute("LCO_LOCADD1",rsResultSet.getString("LCO_LOCADD1"));
            ObjLcOpenerMasterDetails.setAttribute("LCO_LOCADD2",rsResultSet.getString("LCO_LOCADD2"));
            ObjLcOpenerMasterDetails.setAttribute("LCO_AMT",rsResultSet.getString("LCO_AMT"));
            ObjLcOpenerMasterDetails.setAttribute("LCO_REMARKS",rsResultSet.getString("LCO_REMARKS"));
            ObjLcOpenerMasterDetails.setAttribute("LCO_LOCALBANK",rsResultSet.getString("LCO_LOCALBANK"));
            ObjLcOpenerMasterDetails.setAttribute("LCO_CRITICAL_LIMIT",rsResultSet.getString("LCO_CRITICAL_LIMIT"));
            
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
            rsTmp=stTmp.executeQuery("SELECT DISTINCT REVISION_NO,UPDATE_BY,MAX(ENTRY_DATE)AS ENTRY_DATE,APPROVAL_STATUS,APPROVAL_REMARKS FROM DINESHMILLS.D_SAL_LC_OPENER_MASTER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND LCO_OPENER_CODE='"+PartyCode+"' GROUP BY REVISION_NO,UPDATE_BY,APPROVAL_STATUS");
            
            
           
               while(rsTmp.next()) {
                clsLcOpenerMaster ObjLcOpenerMaster=new clsLcOpenerMaster();
                
            //    ObjLcOpenerMaster.setAttribute("LCO_OPENER_CODE",rsTmp.getString("LCO_OPENER_CODE"));
                ObjLcOpenerMaster.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjLcOpenerMaster.setAttribute("UPDATE_BY",rsTmp.getInt("UPDATE_BY"));
                ObjLcOpenerMaster.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjLcOpenerMaster.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjLcOpenerMaster.setAttribute("APPROVAL_REMARKS",rsTmp.getString("APPROVAL_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjLcOpenerMaster);
                
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
                //strSQL="SELECT D_SAL_LC_OPENER_MASTER.LC_NO, D_SAL_LC_OPENER_MASTER.DISC,D_SAL_LC_OPENER_MASTER.LCO_OPENER_CODE,RECEIVED_DATE FROM D_SAL_LC_OPENER_MASTER,D_COM_DOC_DATA WHERE D_SAL_LC_OPENER_MASTER.LCO_OPENER_CODE=D_COM_DOC_DATA.DOC_NO AND D_SAL_LC_OPENER_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_SAL_LC_OPENER_MASTER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=727 ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
                  strSQL="SELECT D_SAL_LC_OPENER_MASTER.LCO_OPENER_CODE,RECEIVED_DATE FROM D_SAL_LC_OPENER_MASTER,D_COM_DOC_DATA WHERE D_SAL_LC_OPENER_MASTER.LCO_OPENER_CODE=D_COM_DOC_DATA.DOC_NO AND D_SAL_LC_OPENER_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_SAL_LC_OPENER_MASTER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=727 ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                //strSQL="SELECT D_SAL_LC_OPENER_MASTER.LC_NO, D_SAL_LC_OPENER_MASTER.DISC,D_SAL_LC_OPENER_MASTER.LCO_OPENER_CODE,RECEIVED_DATE FROM D_SAL_LC_OPENER_MASTER,D_COM_DOC_DATA WHERE D_SAL_LC_OPENER_MASTER.LCO_OPENER_CODE=D_COM_DOC_DATA.DOC_NO AND D_SAL_LC_OPENER_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_SAL_LC_OPENER_MASTER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=727 ORDER BY D_SAL_LC_OPENER_MASTER.LCO_OPENER_CODE";
                 strSQL="SELECT D_SAL_LC_OPENER_MASTER.LCO_OPENER_CODE,RECEIVED_DATE FROM D_SAL_LC_OPENER_MASTER,D_COM_DOC_DATA WHERE D_SAL_LC_OPENER_MASTER.LCO_OPENER_CODE=D_COM_DOC_DATA.DOC_NO AND D_SAL_LC_OPENER_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_SAL_LC_OPENER_MASTER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=727 ORDER BY D_SAL_LC_OPENER_MASTER.LCO_OPENER_CODE";
              //   strSQL="SELECT D_SAL_LC_OPENER_MASTER_AMEND.LCO_OPENER_CODE,RECEIVED_DATE FROM D_SAL_LC_OPENER_MASTER_AMEND,D_COM_DOC_DATA WHERE D_SAL_LC_OPENER_MASTER_AMEND.LCO_OPENER_CODE=D_COM_DOC_DATA.DOC_NO AND D_SAL_LC_OPENER_MASTER_AMEND.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_SAL_LC_OPENER_MASTER_AMEND.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=727 ORDER BY D_SAL_LC_OPENER_MASTER_AMEND.LCO_OPENER_CODE";
            
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                //strSQL="SELECT D_SAL_LC_OPENER_MASTER.LC_NO, D_SAL_LC_OPENER_MASTER.DISC,D_SAL_LC_OPENER_MASTER.LCO_OPENER_CODE,RECEIVED_DATE FROM D_SAL_LC_OPENER_MASTER,D_COM_DOC_DATA WHERE D_SAL_LC_OPENER_MASTER.LCO_OPENER_CODE=D_COM_DOC_DATA.DOC_NO AND D_SAL_LC_OPENER_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_SAL_LC_OPENER_MASTER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=727 ORDER BY D_SAL_POLICY_LC.LCO_OPENER_CODE";
                  strSQL="SELECT D_SAL_LC_OPENER_MASTER.LC_NO, D_SAL_LC_OPENER_MASTER.DISC,D_SAL_LC_OPENER_MASTER.LCO_OPENER_CODE,RECEIVED_DATE FROM D_SAL_LC_OPENER_MASTER,D_COM_DOC_DATA WHERE D_SAL_LC_OPENER_MASTER.LCO_OPENER_CODE=D_COM_DOC_DATA.DOC_NO AND D_SAL_LC_OPENER_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_SAL_LC_OPENER_MASTER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=727 ORDER BY D_SAL_POLICY_LC.LCO_OPENER_CODE";
                }
            
            //strSQL="SELECT D_INV_ISSUE_HEADER.ISSUE_NO,D_INV_ISSUE_HEADER.ISSUE_DATE FROM D_INV_ISSUE_HEADER,D_COM_DOC_DATA WHERE D_INV_ISSUE_HEADER.ISSUE_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_ISSUE_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_ISSUE_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=14";
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                
                Counter=Counter+1;
                clsLcOpenerMaster ObjDoc=new clsLcOpenerMaster();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("LCO_OPENER_CODE",rsTmp.getString("LCO_OPENER_CODE"));
                ObjDoc.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                //ObjDoc.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
                //ObjDoc.setAttribute("DEPT_ID","");
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
            rsTmp=stTmp.executeQuery("SELECT LCO_OPENER_CODE FROM DINESHMILLS.D_SAL_LC_OPENER_MASTER WHERE LCO_OPENER_CODE="+pPartyID);
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
            rsTmp=stTmp.executeQuery("SELECT PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE="+pPartyID);
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
            rsTmp=stTmp.executeQuery("SELECT ADDRESS1 FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE="+pPartyID);
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
            rsTmp=stTmp.executeQuery("SELECT BANK_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE="+pPartyID);
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
            rsTmp=stTmp.executeQuery("SELECT BANK_ADDRESS FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE="+pPartyID);
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
            rsTmp=stTmp.executeQuery("SELECT BANK_CITY FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE="+pPartyID);
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
            rsTmp=stTmp.executeQuery("SELECT AMOUNT_LIMIT FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE="+pPartyID);
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
            rsTmp=stTmp.executeQuery("SELECT ADDRESS2 FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE="+pPartyID);
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
            rsTmp=stTmp.executeQuery("SELECT CITY_ID FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE="+pPartyID);
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
    
}



