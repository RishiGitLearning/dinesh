/*
 * clsFeltOrderUpd.java
 *
 * Created on March 12, 2013, 3:10 PM
 */

//package EITLERP.Production.Felt_Order_Updation;
package EITLERP.Sales;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import EITLERP.EITLERPGLOBAL;
import EITLERP.Variant;
import EITLERP.data;
import EITLERP.ComboData;
import EITLERP.*;


/**
 *
 * @author  Ashutosh
 * @version
 */

public class clsLCPartyUpd {
    
    public String LastError="";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    
    public HashMap props;
    public boolean Ready = false;
    //Felt Order Collection
    //public HashMap hmFeltOrderUpdDetails;
    public HashMap hmLCPartyDetails;    
    
    //History Related properties
    public boolean HistoryView=false;
    private String historyAmendDate="";
    private String historyAmendID="";
    
    public static int ModuleID=171;
    
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
    
    /** Creates new Data Felt Order Updation */
    public clsLCPartyUpd() {
        LastError = "";
        props=new HashMap();
        props.put("COMPANY_ID", new Variant(0));
        props.put("LC_OPENER",new Variant(""));
        props.put("LC_ID", new Variant(""));        
        props.put("TYPE",new Variant(""));
        props.put("PREFIX",new Variant(""));
        props.put("LC_REASON",new Variant(""));
        
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));        
        props.put("APPROVED",new Variant(0));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("REJECTED",new Variant(0));
        props.put("REJECTED_DATE",new Variant(""));        
        props.put("REJECTED_REMARKS",new Variant(""));
        props.put("CHANGED",new Variant(0));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("CANCELLED",new Variant(0));
        
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("RECEIVED_DATE",new Variant(""));
        
        // -- Approval Specific Fields --
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        
        //hmFeltOrderUpdDetails=new HashMap();
        hmLCPartyDetails=new HashMap();
    }
    
    public boolean LoadData() {
        Ready=false;
        try {
            connection = data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //resultSet=statement.executeQuery("SELECT FLT_AMEND_ID,FLT_AMEND_DATE FROM PRODUCTION.FELT_ORDER_AMENDMENT WHERE FLT_AMEND_DATE >= '"+EITLERPGLOBAL.FinFromDateDB+"' AND FLT_AMEND_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY FLT_AMEND_DATE");
            resultSet=statement.executeQuery("SELECT * FROM D_SAL_LC_PARTY_UPD ORDER BY LC_ID");
            //resultSet=statement.executeQuery("SELECT DISTINCT LC_ID FROM D_SAL_LC_PARTY_UPD ORDER BY LC_ID");
            HistoryView=false;
            Ready=true;
            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean LoadData(int LCOpenerCode) {
      
        Ready=false;
        try {
            connection = data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //resultSet=statement.executeQuery("SELECT * FROM DINESHMILLS.D_SAL_POLICY_LC_MASTER WHERE LC_OPENER_CODE='"+LCOpenerCode+"' ORDER BY PARTY_CODE");
            resultSet=statement.executeQuery("SELECT * FROM DINESHMILLS.D_SAL_LC_PARTY_UPD WHERE LC_OPENER='"+LCOpenerCode+"' ORDER BY LC_PARTY");
            HistoryView=false;
            Ready=true;
            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            e.printStackTrace();
            return false;
        }
        
    }
    
    public void Close() {
        try {
            statement.close();
            resultSet.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    //Navigation Methods
    public boolean MoveFirst() {
        try {
            resultSet.first();
            //if(HistoryView) setHistoryData(historyAmendDate, historyAmendID);
            //else setData();
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean MoveNext() {
        try {
            if(resultSet.isAfterLast()||resultSet.isLast()) {
                //Move pointer at last record if it is beyond eof
                resultSet.last();
            }
            else {
                resultSet.next();
                if(resultSet.isAfterLast()) {
                    resultSet.last();
                }
            }
            //if(HistoryView) setHistoryData(historyAmendDate, historyAmendID);
            //else setData();
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean MovePrevious() {
        try {
            if(resultSet.isFirst()||resultSet.isBeforeFirst()) {
                resultSet.first();
            }
            else {
                resultSet.previous();
                if(resultSet.isBeforeFirst()) {
                    resultSet.first();
                }
            }
            //if(HistoryView) setHistoryData(historyAmendDate, historyAmendID);
            //else setData();
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean MoveLast() {
        try {
            resultSet.last();
            //if(HistoryView) setHistoryData(historyAmendDate, historyAmendID);
            //else setData();
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean Insert() {        
        ResultSet  resultSetTemp,resultSetHistory;
        Statement  statementTemp, statementHistory;
        try {
            // Felt Order Updation data connection
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM D_SAL_LC_PARTY_UPD WHERE LC_OPENER=''");            
            // Felt order Updation data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM D_SAL_LC_PARTY_UPD_H WHERE LC_OPENER=''");                 

             // Felt order Updation Last Free No, 
             //setAttribute("LC_ID",getNextFreeNo(720,true));
            setAttribute("LC_ID",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,171, (int)getAttribute("FFNO").getVal(),true));
            
            //Now Insert records into Felt_Order_Amendment & History tables
            for(int i=1;i<=hmLCPartyDetails.size();i++) {
                //clsFeltOrderUpdDetails ObjFeltOrderUpdDetails = (clsFeltOrderUpdDetails) hmFeltOrderUpdDetails.get(Integer.toString(i));
                clsLCPartyDetails ObjLCPartyDetails=(clsLCPartyDetails) hmLCPartyDetails.get(Integer.toString(i));
             
                //Insert records into Felt order Amendment table
                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                resultSetTemp.updateString("LC_OPENER",getAttribute("LC_OPENER").getString());
                resultSetTemp.updateString("LC_ID", getAttribute("LC_ID").getString());
                resultSetTemp.updateString("TYPE", getAttribute("TYPE").getString());
                resultSetTemp.updateString("LC_PARTY",ObjLCPartyDetails.getAttribute("LC_PARTY").getString());
                resultSetTemp.updateString("LC_PARTY_NAME", ObjLCPartyDetails.getAttribute("LC_PARTY_NAME").getString());
                resultSetTemp.updateString("LC_CHARGE_CODE", ObjLCPartyDetails.getAttribute("LC_CHARGE_CODE").getString());
                resultSetTemp.updateString("LC_PARTY_STATE", ObjLCPartyDetails.getAttribute("LC_PARTY_STATE").getString());
                resultSetTemp.updateString("LC_REASON",getAttribute("LC_REASON").getString());
                
                resultSetTemp.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
                resultSetTemp.updateString("CREATED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetTemp.updateInt("MODIFIED_BY",0);
                resultSetTemp.updateString("MODIFIED_DATE","");
                resultSetTemp.updateBoolean("APPROVED",false);
                resultSetTemp.updateString("APPROVED_DATE","");
                resultSetTemp.updateBoolean("REJECTED",false);
                resultSetTemp.updateString("REJECTED_DATE","");
                resultSetTemp.updateInt("CANCELLED",0);
                resultSetTemp.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
                resultSetTemp.updateInt("CHANGED",1);
                resultSetTemp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetTemp.insertRow();
                
                //Insert records into Felt Order Amendment History Table
                
                resultSetHistory.moveToInsertRow();
                
                resultSetHistory.updateInt("REVISION_NO",1);
                resultSetHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
                resultSetHistory.updateString("LC_OPENER", getAttribute("LC_OPENER").getString());
                resultSetHistory.updateString("LC_ID", getAttribute("LC_ID").getString());
                resultSetHistory.updateString("TYPE", getAttribute("TYPE").getString());
                
                resultSetHistory.updateString("LC_PARTY",ObjLCPartyDetails.getAttribute("LC_PARTY").getString());
                resultSetHistory.updateString("LC_PARTY_NAME",ObjLCPartyDetails.getAttribute("LC_PARTY_NAME").getString());
                resultSetHistory.updateString("LC_CHARGE_CODE",ObjLCPartyDetails.getAttribute("LC_CHARGE_CODE").getString());
                resultSetHistory.updateString("LC_PARTY_STATE",ObjLCPartyDetails.getAttribute("LC_PARTY_STATE").getString());
                resultSetHistory.updateString("LC_REASON",getAttribute("LC_REASON").getString());
                
                resultSetHistory.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
                resultSetHistory.updateString("CREATED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateInt("MODIFIED_BY",0);
                resultSetHistory.updateString("MODIFIED_DATE","");
                resultSetHistory.updateBoolean("APPROVED",false);
                resultSetHistory.updateString("APPROVED_DATE","");
                resultSetHistory.updateBoolean("REJECTED",false);
                resultSetHistory.updateString("REJECTED_DATE","");
                resultSetHistory.updateInt("CANCELLED",0);
                resultSetHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
                resultSetHistory.updateInt("CHANGED",1);
                resultSetHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                
                resultSetHistory.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
                resultSetHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
                resultSetHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
                resultSetHistory.insertRow();
            }
            
            //======== Update the Approval Flow =========
            ApprovalFlow ObjFlow=new ApprovalFlow();
            ObjFlow.CompanyID=EITLERPGLOBAL.gCompanyID;
            ObjFlow.ModuleID=171; //LC Party Master
            ObjFlow.DocNo=getAttribute("LC_ID").getString();
            //ObjFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_SAL_LC_PARTY_UPD";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName="LC_ID";
            
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
 
            LoadData();
            resultSetTemp.close();
            statementTemp.close();
            resultSetHistory.close();
            statementHistory.close();
            return true;
 
        }
        catch(Exception e) {
            LastError= e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    //Updates current record
    public boolean Update() {
        ResultSet  resultSetTemp,resultSetHistory;
        Statement  statementTemp, statementHistory;
        int revisionNo =1;
        try {
            // Production data connection
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM D_SAL_LC_PARTY_UPD WHERE LC_OPENER=''");
            
            // Production data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            //Get the Maximum Revision No in History Table.
            resultSetHistory=statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM D_SAL_LC_PARTY_UPD_H WHERE LC_ID='"+getAttribute("LC_ID").getString()+"'");
            resultSetHistory.first();
            if(resultSetHistory.getRow()>0){
                revisionNo=resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from production data table before insert
            statementHistory.execute("DELETE FROM D_SAL_LC_PARTY_UPD WHERE LC_ID='"+getAttribute("LC_ID").getString()+"'");
            
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM D_SAL_LC_PARTY_UPD_H WHERE LC_ID='"+getAttribute("LC_ID").getString()+"'");
            
            //Now Update records into Felt_Order_Amendment tables
            for(int i=1;i<=hmLCPartyDetails.size();i++) {
                //clsFeltOrderUpdDetails ObjFeltOrderUpdDetails=(clsFeltOrderUpdDetails) hmFeltOrderUpdDetails.get(Integer.toString(i));
                clsLCPartyDetails ObjLCPartyDetails=(clsLCPartyDetails) hmLCPartyDetails.get(Integer.toString(i));
                
           resultSetTemp.moveToInsertRow();
                resultSetTemp.updateString("LC_OPENER", getAttribute("LC_OPENER").getString());
                resultSetTemp.updateString("LC_ID", getAttribute("LC_ID").getString());
                resultSetTemp.updateString("TYPE", getAttribute("TYPE").getString());
                resultSetTemp.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
                resultSetTemp.updateString("LC_PARTY",ObjLCPartyDetails.getAttribute("LC_PARTY").getString());
                resultSetTemp.updateString("LC_PARTY_NAME",ObjLCPartyDetails.getAttribute("LC_PARTY_NAME").getString());
                resultSetTemp.updateString("LC_CHARGE_CODE",ObjLCPartyDetails.getAttribute("LC_CHARGE_CODE").getString());
                resultSetTemp.updateString("LC_PARTY_STATE",ObjLCPartyDetails.getAttribute("LC_PARTY_STATE").getString());
                resultSetTemp.updateString("LC_REASON",getAttribute("LC_REASON").getString());
                
                resultSetTemp.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
                resultSetTemp.updateString("CREATED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetTemp.updateInt("MODIFIED_BY",0);
                resultSetTemp.updateString("MODIFIED_DATE","");
                resultSetTemp.updateBoolean("APPROVED",false);
                resultSetTemp.updateString("APPROVED_DATE","");
                resultSetTemp.updateBoolean("REJECTED",false);
                resultSetTemp.updateString("REJECTED_DATE","");
                resultSetTemp.updateInt("CANCELLED",0);
                resultSetTemp.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
                resultSetTemp.updateInt("CHANGED",1);
                resultSetTemp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetTemp.insertRow();

                
                //Insert records into Felt_Order_Amendment_H table
               resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO",revisionNo);
                resultSetHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
                resultSetHistory.updateString("LC_OPENER", getAttribute("LC_OPENER").getString());
                resultSetHistory.updateString("LC_ID", getAttribute("LC_ID").getString());
                resultSetHistory.updateString("TYPE", getAttribute("TYPE").getString());
                
                resultSetHistory.updateString("LC_PARTY",ObjLCPartyDetails.getAttribute("LC_PARTY").getString());
                resultSetHistory.updateString("LC_PARTY_NAME",ObjLCPartyDetails.getAttribute("LC_PARTY_NAME").getString());
                resultSetHistory.updateString("LC_CHARGE_CODE",ObjLCPartyDetails.getAttribute("LC_CHARGE_CODE").getString());
                resultSetHistory.updateString("LC_PARTY_STATE",ObjLCPartyDetails.getAttribute("LC_PARTY_STATE").getString());
                resultSetHistory.updateString("LC_REASON",getAttribute("LC_REASON").getString());
                
                resultSetHistory.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
                resultSetHistory.updateString("CREATED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateInt("MODIFIED_BY",0);
                resultSetHistory.updateString("MODIFIED_DATE","");
                resultSetHistory.updateBoolean("APPROVED",false);
                resultSetHistory.updateString("APPROVED_DATE","");
                resultSetHistory.updateBoolean("REJECTED",false);
                resultSetHistory.updateString("REJECTED_DATE","");
                resultSetHistory.updateInt("CANCELLED",0);
                resultSetHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
                resultSetHistory.updateInt("CHANGED",1);
                resultSetHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                
                resultSetHistory.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
                resultSetHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
                resultSetHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
                
                resultSetHistory.insertRow();
            }
            
            //======== Update the Approval Flow =========
            ApprovalFlow ObjFlow=new ApprovalFlow();
            ObjFlow.CompanyID=EITLERPGLOBAL.gCompanyID;
            ObjFlow.ModuleID=171; //LC Party Master
            ObjFlow.DocNo=getAttribute("LC_ID").getString();
            //ObjFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_SAL_LC_PARTY_UPD";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName="LC_ID";
            
            if(getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFlow.To=(int)getAttribute("SEND_DOC_TO").getVal();
                ObjFlow.ExplicitSendTo=true;
            }

            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_SAL_LC_PARTY_UPD SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE LC_ID ='"+getAttribute("LC_ID").getString()+"'");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=171 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
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
            //--------- Approval Flow Update complete -----------
            
            
            if(ObjFlow.Status.equals("F")) {
                //AddPartyToPolicyLCMaster(getAttribute("LC_ID").getString());
                if(getAttribute("TYPE").getString().equals("ADD")){                
                //data.Execute("INSERT INTO D_SAL_POLICY_LC_MASTER (COMPANY_ID,PARTY_CODE,LC_NO,EXP_DATE,IND,DISC,LC_OPENER_CODE,PARTY_NAME,ADDRESS,BANK_ID,BANK_NAME,BANK_ADDRESS,BANK_CITY,INST1,INST2,BNKHUN,LOCADD1,LOCADD2,AMT,BNGDOCIND,LOCALBANK,PARTY_STATUS,CRITICAL_LIMIT,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,HIERARCHY_ID,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,REJECTED_REMARKS,CHANGED,CHANGED_DATE,CANCELLED) (SELECT A.COMPANY_ID,A.LC_PARTY,'',B.LCO_EXPIRY_DATE,'','',A.LC_OPENER,A.LC_PARTY_NAME,'',B.LCO_BANK_ID,B.LCO_BANK_NAME,B.LCO_BANK_ADDRESS,B.LCO_BANK_CITY,B.LCO_INST1,B.LCO_INST2,B.LCO_BNKHUN,B.LCO_LOCADD1,B.LCO_LOCADD2,B.LCO_AMT,'',B.LCO_LOCALBANK,CASE WHEN TYPE='ADD'  THEN 'ACTIVE' WHEN TYPE='BLOCK' THEN 'INACTIVE' WHEN TYPE='UNBLOCK' THEN 'ACTIVE' END AS PARTY_STATUS,'',A.CREATED_BY,A.CREATED_DATE,A.MODIFIED_BY,A.MODIFIED_DATE,A.HIERARCHY_ID,A.APPROVED,A.APPROVED_DATE,A.REJECTED,A.REJECTED_DATE,A.REJECTED_REMARKS,A.CHANGED,A.CHANGED_DATE,A.CANCELLED FROM D_SAL_LC_PARTY_UPD A,D_SAL_LC_OPENER_MASTER B WHERE A.LC_OPENER=B.LCO_OPENER_CODE AND LC_ID='"+ObjFlow.DocNo+"')");
                data.Execute("INSERT INTO D_SAL_POLICY_LC_MASTER (COMPANY_ID,PARTY_CODE,LC_NO,EXP_DATE,IND,DISC,LC_OPENER_CODE,OPENER_NAME,ADDRESS,BANK_ID,BANK_NAME,BANK_ADDRESS,BANK_CITY,INST1,INST2,BNKHUN,LOCADD1,LOCADD2,AMT,BNGDOCIND,LOCALBANK,LC_PARTY_NAME,PARTY_STATUS,CRITICAL_LIMIT,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,HIERARCHY_ID,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,REJECTED_REMARKS,CHANGED,CHANGED_DATE,CANCELLED) (SELECT A.COMPANY_ID,A.LC_PARTY,'',B.LCO_EXPIRY_DATE,'','',A.LC_OPENER,B.LCO_OPENER_NAME,'',B.LCO_BANK_ID,B.LCO_BANK_NAME,B.LCO_BANK_ADDRESS,B.LCO_BANK_CITY,B.LCO_INST1,B.LCO_INST2,B.LCO_BNKHUN,B.LCO_LOCADD1,B.LCO_LOCADD2,B.LCO_AMT,'',B.LCO_LOCALBANK,A.LC_PARTY_NAME,CASE WHEN TYPE='ADD'  THEN 'ACTIVE' WHEN TYPE='BLOCK' THEN 'INACTIVE' WHEN TYPE='UNBLOCK' THEN 'ACTIVE' END AS PARTY_STATUS,'',A.CREATED_BY,A.CREATED_DATE,A.MODIFIED_BY,A.MODIFIED_DATE,A.HIERARCHY_ID,A.APPROVED,A.APPROVED_DATE,A.REJECTED,A.REJECTED_DATE,A.REJECTED_REMARKS,A.CHANGED,A.CHANGED_DATE,A.CANCELLED FROM D_SAL_LC_PARTY_UPD A,D_SAL_LC_OPENER_MASTER B WHERE A.LC_OPENER=B.LCO_OPENER_CODE AND LC_ID='"+ObjFlow.DocNo+"')");
                }
                else if(getAttribute("TYPE").getString().equals("BLOCK")){
                data.Execute("DELETE FROM D_SAL_POLICY_LC_MASTER WHERE PARTY_CODE IN (SELECT LC_PARTY FROM D_SAL_LC_PARTY_UPD WHERE LC_ID='"+ObjFlow.DocNo+"') AND LC_OPENER_CODE='"+getAttribute("LC_OPENER").getString()+"'");    
                //data.Execute("INSERT INTO D_SAL_POLICY_LC_MASTER (COMPANY_ID,PARTY_CODE,LC_NO,EXP_DATE,IND,DISC,LC_OPENER_CODE,PARTY_NAME,ADDRESS,BANK_ID,BANK_NAME,BANK_ADDRESS,BANK_CITY,INST1,INST2,BNKHUN,LOCADD1,LOCADD2,AMT,BNGDOCIND,LOCALBANK,PARTY_STATUS,CRITICAL_LIMIT,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,HIERARCHY_ID,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,REJECTED_REMARKS,CHANGED,CHANGED_DATE,CANCELLED) (SELECT A.COMPANY_ID,A.LC_PARTY,'',B.LCO_EXPIRY_DATE,'','',A.LC_OPENER,A.LC_PARTY_NAME,'',B.LCO_BANK_ID,B.LCO_BANK_NAME,B.LCO_BANK_ADDRESS,B.LCO_BANK_CITY,B.LCO_INST1,B.LCO_INST2,B.LCO_BNKHUN,B.LCO_LOCADD1,B.LCO_LOCADD2,B.LCO_AMT,'',B.LCO_LOCALBANK,CASE WHEN TYPE='ADD'  THEN 'ACTIVE' WHEN TYPE='BLOCK' THEN 'INACTIVE' WHEN TYPE='UNBLOCK' THEN 'ACTIVE' END AS PARTY_STATUS,'',A.CREATED_BY,A.CREATED_DATE,A.MODIFIED_BY,A.MODIFIED_DATE,A.HIERARCHY_ID,A.APPROVED,A.APPROVED_DATE,A.REJECTED,A.REJECTED_DATE,A.REJECTED_REMARKS,A.CHANGED,A.CHANGED_DATE,A.CANCELLED FROM D_SAL_LC_PARTY_UPD A,D_SAL_LC_OPENER_MASTER B WHERE A.LC_OPENER=B.LCO_OPENER_CODE AND LC_ID='"+ObjFlow.DocNo+"')");
                data.Execute("INSERT INTO D_SAL_POLICY_LC_MASTER (COMPANY_ID,PARTY_CODE,LC_NO,EXP_DATE,IND,DISC,LC_OPENER_CODE,OPENER_NAME,ADDRESS,BANK_ID,BANK_NAME,BANK_ADDRESS,BANK_CITY,INST1,INST2,BNKHUN,LOCADD1,LOCADD2,AMT,BNGDOCIND,LOCALBANK,LC_PARTY_NAME,PARTY_STATUS,CRITICAL_LIMIT,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,HIERARCHY_ID,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,REJECTED_REMARKS,CHANGED,CHANGED_DATE,CANCELLED) (SELECT A.COMPANY_ID,A.LC_PARTY,'',B.LCO_EXPIRY_DATE,'','',A.LC_OPENER,B.LCO_OPENER_NAME,'',B.LCO_BANK_ID,B.LCO_BANK_NAME,B.LCO_BANK_ADDRESS,B.LCO_BANK_CITY,B.LCO_INST1,B.LCO_INST2,B.LCO_BNKHUN,B.LCO_LOCADD1,B.LCO_LOCADD2,B.LCO_AMT,'',B.LCO_LOCALBANK,A.LC_PARTY_NAME,CASE WHEN TYPE='ADD'  THEN 'ACTIVE' WHEN TYPE='BLOCK' THEN 'INACTIVE' WHEN TYPE='UNBLOCK' THEN 'ACTIVE' END AS PARTY_STATUS,'',A.CREATED_BY,A.CREATED_DATE,A.MODIFIED_BY,A.MODIFIED_DATE,A.HIERARCHY_ID,A.APPROVED,A.APPROVED_DATE,A.REJECTED,A.REJECTED_DATE,A.REJECTED_REMARKS,A.CHANGED,A.CHANGED_DATE,A.CANCELLED FROM D_SAL_LC_PARTY_UPD A,D_SAL_LC_OPENER_MASTER B WHERE A.LC_OPENER=B.LCO_OPENER_CODE AND LC_ID='"+ObjFlow.DocNo+"')");
                }else{
                data.Execute("DELETE FROM D_SAL_POLICY_LC_MASTER WHERE PARTY_CODE IN (SELECT LC_PARTY FROM D_SAL_LC_PARTY_UPD WHERE LC_ID='"+ObjFlow.DocNo+"') AND LC_OPENER_CODE='"+getAttribute("LC_OPENER").getString()+"'");    
                //data.Execute("INSERT INTO D_SAL_POLICY_LC_MASTER (COMPANY_ID,PARTY_CODE,LC_NO,EXP_DATE,IND,DISC,LC_OPENER_CODE,PARTY_NAME,ADDRESS,BANK_ID,BANK_NAME,BANK_ADDRESS,BANK_CITY,INST1,INST2,BNKHUN,LOCADD1,LOCADD2,AMT,BNGDOCIND,LOCALBANK,PARTY_STATUS,CRITICAL_LIMIT,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,HIERARCHY_ID,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,REJECTED_REMARKS,CHANGED,CHANGED_DATE,CANCELLED) (SELECT A.COMPANY_ID,A.LC_PARTY,'',B.LCO_EXPIRY_DATE,'','',A.LC_OPENER,A.LC_PARTY_NAME,'',B.LCO_BANK_ID,B.LCO_BANK_NAME,B.LCO_BANK_ADDRESS,B.LCO_BANK_CITY,B.LCO_INST1,B.LCO_INST2,B.LCO_BNKHUN,B.LCO_LOCADD1,B.LCO_LOCADD2,B.LCO_AMT,'',B.LCO_LOCALBANK,CASE WHEN TYPE='ADD'  THEN 'ACTIVE' WHEN TYPE='BLOCK' THEN 'INACTIVE' WHEN TYPE='UNBLOCK' THEN 'ACTIVE' END AS PARTY_STATUS,'',A.CREATED_BY,A.CREATED_DATE,A.MODIFIED_BY,A.MODIFIED_DATE,A.HIERARCHY_ID,A.APPROVED,A.APPROVED_DATE,A.REJECTED,A.REJECTED_DATE,A.REJECTED_REMARKS,A.CHANGED,A.CHANGED_DATE,A.CANCELLED FROM D_SAL_LC_PARTY_UPD A,D_SAL_LC_OPENER_MASTER B WHERE A.LC_OPENER=B.LCO_OPENER_CODE AND LC_ID='"+ObjFlow.DocNo+"')");                    
                data.Execute("INSERT INTO D_SAL_POLICY_LC_MASTER (COMPANY_ID,PARTY_CODE,LC_NO,EXP_DATE,IND,DISC,LC_OPENER_CODE,OPENER_NAME,ADDRESS,BANK_ID,BANK_NAME,BANK_ADDRESS,BANK_CITY,INST1,INST2,BNKHUN,LOCADD1,LOCADD2,AMT,BNGDOCIND,LOCALBANK,LC_PARTY_NAME,PARTY_STATUS,CRITICAL_LIMIT,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,HIERARCHY_ID,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,REJECTED_REMARKS,CHANGED,CHANGED_DATE,CANCELLED) (SELECT A.COMPANY_ID,A.LC_PARTY,'',B.LCO_EXPIRY_DATE,'','',A.LC_OPENER,B.LCO_OPENER_NAME,'',B.LCO_BANK_ID,B.LCO_BANK_NAME,B.LCO_BANK_ADDRESS,B.LCO_BANK_CITY,B.LCO_INST1,B.LCO_INST2,B.LCO_BNKHUN,B.LCO_LOCADD1,B.LCO_LOCADD2,B.LCO_AMT,'',B.LCO_LOCALBANK,A.LC_PARTY_NAME,CASE WHEN TYPE='ADD'  THEN 'ACTIVE' WHEN TYPE='BLOCK' THEN 'INACTIVE' WHEN TYPE='UNBLOCK' THEN 'ACTIVE' END AS PARTY_STATUS,'',A.CREATED_BY,A.CREATED_DATE,A.MODIFIED_BY,A.MODIFIED_DATE,A.HIERARCHY_ID,A.APPROVED,A.APPROVED_DATE,A.REJECTED,A.REJECTED_DATE,A.REJECTED_REMARKS,A.CHANGED,A.CHANGED_DATE,A.CANCELLED FROM D_SAL_LC_PARTY_UPD A,D_SAL_LC_OPENER_MASTER B WHERE A.LC_OPENER=B.LCO_OPENER_CODE AND LC_ID='"+ObjFlow.DocNo+"')");
                }
            }
            
            setData();
            resultSetTemp.close();
            statementTemp.close();
            resultSetHistory.close();
            statementHistory.close();
            return true;
        }catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * This routine checks whether the item is deletable or not.
     * Criteria is Approved item cannot be delete,
     * and if not approved then user id is checked whether doucment
     * is created by the user. Only creator can delete the document.
     * After checking it deletes the record of selected production date and document no.
     */
    public boolean CanDelete(int pCompanyID,String pDocNo,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            tmpStmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            strSQL="SELECT COUNT(*) AS COUNT FROM D_SAL_LC_PARTY_UPD WHERE LC_ID='"+pDocNo +"' AND APPROVED="+1;
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if(rsTmp.getInt("COUNT")>0){  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            }else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE MODULE_ID=171 AND USER_ID="+pUserID+" AND DOC_NO='"+ pDocNo +"' AND STATUS='W'";
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getInt("COUNT")>0) {
                    strSQL = "DELETE FROM D_SAL_LC_PARTY_UPD WHERE "                    
                    + " LC_ID='" + pDocNo + "'";
                    tmpStmt.executeUpdate(strSQL);
                    LoadData();
                    return true;
                }
                else {
                    return false;
                }
            }
        }catch(Exception e) {
            LastError = "Error occured while deleting."+e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean CanDelete(String documentNo,int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            tmpStmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            strSQL="SELECT COUNT(*) AS COUNT FROM D_SAL_LC_PARTY_UPD WHERE LC_ID='"+documentNo +"' AND APPROVED="+1;
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if(rsTmp.getInt("COUNT")>0){  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            }else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE MODULE_ID=171 AND USER_ID="+userID+" AND DOC_NO='"+ documentNo +"' AND STATUS='W'";
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getInt("COUNT")>0) {
                    strSQL = "DELETE FROM D_SAL_LC_PARTY_UPD WHERE "                    
                    + " LC_ID='" + documentNo + "'";
                    tmpStmt.executeUpdate(strSQL);
                    LoadData();
                    return true;
                }
                else {
                    return false;
                }
            }
        }catch(Exception e) {
            LastError = "Error occured while deleting."+e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * This routine checks and returns whether the item is editable or not.
     * Criteria is Approved item cannot be changed and if not approved then user id is
     * checked whether doucment is waiting for his approval.
     */
    public boolean IsEditable(int pCompanyID,String pDocNo,int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        try {
            if(HistoryView) {
                return false;
            }
            tmpStmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_SAL_LC_PARTY_UPD WHERE LC_ID='"+ pDocNo +"' AND APPROVED=1";
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0){  //Item is Approved
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE MODULE_ID=171 AND USER_ID="+userID+" AND DOC_NO='"+ pDocNo +"' AND STATUS='W'";
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
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean Filter(String stringFindQuery) {
Ready=false;
        try {
            //String strSql = "" + pCondition ;
            String strSql = "SELECT * FROM D_SAL_LC_PARTY_UPD " + stringFindQuery ;
            connection=data.getConn();
            statement=connection.createStatement();
            
            if (statement.execute(strSql)) {
                resultSet = statement.getResultSet();
                Ready=MoveFirst();
            }
            else {
                Ready=false;
            }
            
            return Ready;
        }
        catch(Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }        
    }
    
    public boolean setData() {
        ResultSet  resultSetTemp;
        Statement  statementTemp;
        int serialNoCounter=0;
        int RevNo=0;
        double totalWeight, previousWeight;
        try {
            if(HistoryView) {
                RevNo=resultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",resultSet.getInt("REVISION_NO"));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            //setAttribute("REVISION_NO",0);
            setAttribute("COMPANY_ID",resultSet.getInt("COMPANY_ID"));
            setAttribute("LC_OPENER",resultSet.getString("LC_OPENER"));
            setAttribute("LC_ID",resultSet.getString("LC_ID"));
            setAttribute("TYPE",resultSet.getString("TYPE"));
  
            //hmFeltOrderUpdDetails.clear();
            hmLCPartyDetails.clear();
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            //resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_ORDER_AMENDMENT WHERE FLT_AMEND_ID = '"+ resultSet.getString("FLT_AMEND_ID") +"' AND FLT_AMEND_DATE='"+ resultSet.getDate("FLT_AMEND_DATE") +"' ORDER BY FLT_AMEND_ID DESC");
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM D_SAL_LC_PARTY_UPD WHERE LC_ID ='"+resultSet.getString("LC_ID")+"' AND LC_OPENER='"+resultSet.getString("LC_OPENER")+"' ORDER BY LC_ID DESC");
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
  //            setAttribute("PRODUCTION_FORM_NO",resultSetTemp.getString("PROD_FORM_NO"));
                setAttribute("CREATED_BY",resultSetTemp.getInt("CREATED_BY"));
                setAttribute("CREATED_DATE",resultSetTemp.getString("CREATED_DATE"));
                setAttribute("MODIFIED_BY",resultSetTemp.getInt("MODIFIED_BY"));
                setAttribute("MODIFIED_DATE",resultSetTemp.getString("MODIFIED_DATE"));
                setAttribute("APPROVED",resultSetTemp.getInt("APPROVED"));
                setAttribute("APPROVED_DATE",resultSetTemp.getString("APPROVED_DATE"));
                setAttribute("REJECTED",resultSetTemp.getBoolean("REJECTED"));
                setAttribute("REJECTED_DATE",resultSetTemp.getString("REJECTED_DATE"));
                setAttribute("CANCELLED",resultSetTemp.getInt("CANCELLED"));
                setAttribute("HIERARCHY_ID",resultSetTemp.getInt("HIERARCHY_ID"));
                setAttribute("LC_REASON",resultSetTemp.getString("LC_REASON"));                                
                //clsFeltOrderUpdDetails ObjFeltOrderUpdDetails = new clsFeltOrderUpdDetails();
                clsLCPartyDetails ObjLCPartyDetails= new clsLCPartyDetails(); 
                
                ObjLCPartyDetails.setAttribute("LC_PARTY",resultSetTemp.getString("LC_PARTY"));
                ObjLCPartyDetails.setAttribute("LC_PARTY_NAME",resultSetTemp.getString("LC_PARTY_NAME"));
                ObjLCPartyDetails.setAttribute("LC_PARTY_STATE",resultSetTemp.getString("LC_PARTY_STATE"));
                ObjLCPartyDetails.setAttribute("LC_CHARGE_CODE",resultSetTemp.getString("LC_CHARGE_CODE"));
                
                
                //hmFeltOrderUpdDetails.put(Integer.toString(serialNoCounter),ObjFeltOrderUpdDetails);
                hmLCPartyDetails.put(Integer.toString(serialNoCounter),ObjLCPartyDetails);
            }
            resultSetTemp.close();
            statementTemp.close();
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean setHistoryData(String pProductionDate,String pDocNo) {
        ResultSet  resultSetTemp;
        Statement  statementTemp;
        int serialNoCounter=0;
        int RevNo=0;
        try {
            RevNo=resultSet.getInt("REVISION_NO");
            setAttribute("REVISION_NO",resultSet.getInt("REVISION_NO"));
            
            //Now Populate the collection, first clear the collection
            //hmFeltOrderUpdDetails.clear();
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  D_SAL_LC_PARTY_UPD_H WHERE LC_ID='"+pDocNo+"' AND REVISION_NO='"+ resultSet.getInt("REVISION_NO") +"' ORDER BY REVISION_NO");
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
                setAttribute("LC_ID",resultSetTemp.getString("LC_ID"));
      //          setAttribute("PRODUCTION_FORM_NO",resultSetTemp.getString("PROD_FORM_NO"));
                setAttribute("UPDATED_BY",resultSetTemp.getInt("UPDATED_BY"));
                setAttribute("ENTRY_DATE",resultSetTemp.getString("ENTRY_DATE"));
                setAttribute("HIERARCHY_ID",resultSetTemp.getInt("HIERARCHY_ID"));
                
                /*clsFeltOrderUpdDetails ObjFeltOrderUpdDetails = new clsFeltOrderUpdDetails();
                
                 
                ObjFeltOrderUpdDetails.setAttribute("FLT_PIECE_NO",resultSetTemp.getString("FLT_PIECE_NO"));
                ObjFeltOrderUpdDetails.setAttribute("FLT_PARTY_CODE",resultSetTemp.getString("FLT_PARTY_CODE"));
                ObjFeltOrderUpdDetails.setAttribute("FLT_ORDER_DATE",resultSetTemp.getString("FLT_ORDER_DATE"));
                ObjFeltOrderUpdDetails.setAttribute("FLT_PRODUCT_CODE",resultSetTemp.getString("FLT_PRODUCT_CODE"));
                ObjFeltOrderUpdDetails.setAttribute("FLT_GROUP",resultSetTemp.getString("FLT_GROUP"));
                ObjFeltOrderUpdDetails.setAttribute("FLT_STYLE",resultSetTemp.getString("FLT_STYLE"));
                ObjFeltOrderUpdDetails.setAttribute("FLT_LENGTH",resultSetTemp.getString("FLT_LENGTH"));
                ObjFeltOrderUpdDetails.setAttribute("FLT_WIDTH",resultSetTemp.getString("FLT_WIDTH"));
                ObjFeltOrderUpdDetails.setAttribute("FLT_GSQ",resultSetTemp.getString("FLT_GSQ"));
                ObjFeltOrderUpdDetails.setAttribute("FLT_WIEGHT",resultSetTemp.getString("FLT_WIEGHT"));
                ObjFeltOrderUpdDetails.setAttribute("FLT_REQ_DATE",resultSetTemp.getString("FLT_REQ_DATE"));
                ObjFeltOrderUpdDetails.setAttribute("FLT_COMM_DATE",resultSetTemp.getString("FLT_COMM_DATE"));
                ObjFeltOrderUpdDetails.setAttribute("FLT_AGREED_DATE",resultSetTemp.getString("FLT_AGREED_DATE"));
                ObjFeltOrderUpdDetails.setAttribute("FLT_REV_REQ_DATE",resultSetTemp.getString("FLT_REV_REQ_DATE"));
                ObjFeltOrderUpdDetails.setAttribute("FLT_REV_COMM_DATE",resultSetTemp.getString("FLT_REV_COMM_DATE"));
                ObjFeltOrderUpdDetails.setAttribute("FLT_REV_AGREED_DATE",resultSetTemp.getString("FLT_REV_AGREED_DATE"));
                ObjFeltOrderUpdDetails.setAttribute("FLT_REV_REQ_REASON",resultSetTemp.getString("FLT_REV_REQ_REASON"));
                ObjFeltOrderUpdDetails.setAttribute("FLT_REV_COMM_REASON",resultSetTemp.getString("FLT_REV_COMM_REASON"));
                ObjFeltOrderUpdDetails.setAttribute("FLT_MACHINE_NO",resultSetTemp.getString("FLT_MACHINE_NO"));
                ObjFeltOrderUpdDetails.setAttribute("FLT_POSITION",resultSetTemp.getString("FLT_POSITION"));
                ObjFeltOrderUpdDetails.setAttribute("FLT_REF_NO",resultSetTemp.getString("FLT_REF_NO"));
                ObjFeltOrderUpdDetails.setAttribute("FLT_CONF_NO",resultSetTemp.getString("FLT_CONF_NO"));
                ObjFeltOrderUpdDetails.setAttribute("FLT_PO_NO",resultSetTemp.getString("FLT_PO_NO"));
                ObjFeltOrderUpdDetails.setAttribute("FLT_PO_DATE",resultSetTemp.getString("FLT_PO_DATE"));
                ObjFeltOrderUpdDetails.setAttribute("FLT_PO_REMARK",resultSetTemp.getString("FLT_PO_REMARK"));
                ObjFeltOrderUpdDetails.setAttribute("FLT_WVG_DATE",resultSetTemp.getString("FLT_WVG_DATE"));
                ObjFeltOrderUpdDetails.setAttribute("FLT_MND_DATE",resultSetTemp.getString("FLT_MND_DATE"));
                ObjFeltOrderUpdDetails.setAttribute("FLT_NDL_DATE",resultSetTemp.getString("FLT_NDL_DATE"));

                */
              /*  
                
                
                ObjFeltOrderUpdDetails.setAttribute("PRODUCTION_PIECE_NO",resultSetTemp.getString("PROD_PIECE_NO"));
                ObjFeltOrderUpdDetails.setAttribute("PRODUCTION_PARTY_CODE",resultSetTemp.getString("PROD_PARTY_CODE"));
                ObjFeltOrderUpdDetails.setAttribute("WEIGHT",resultSetTemp.getFloat("WEIGHT"));
                ObjFeltOrderUpdDetails.setAttribute("PICKS_PER_10CMS",resultSetTemp.getString("PICKS_PER_10CMS"));
                ObjFeltOrderUpdDetails.setAttribute("REED_SPACE",resultSetTemp.getFloat("REED_SPACE"));
                ObjFeltOrderUpdDetails.setAttribute("LENGTH",resultSetTemp.getFloat("LENGTH"));
                ObjFeltOrderUpdDetails.setAttribute("LOOM_NO",resultSetTemp.getInt("LOOM_NO"));
                ObjFeltOrderUpdDetails.setAttribute("REMARKS",resultSetTemp.getString("REMARKS"));
                ObjFeltOrderUpdDetails.setAttribute("WEAVE_DATE",resultSetTemp.getString("WEAVE_DATE"));
                ObjFeltOrderUpdDetails.setAttribute("WARP_NO",resultSetTemp.getString("WARP_NO"));
                ObjFeltOrderUpdDetails.setAttribute("WEAVE_DIFF_DAYS",resultSetTemp.getString("WEAVE_DIFF_DAYS"));
                */
                
                //hmFeltOrderUpdDetails.put(Integer.toString(serialNoCounter),ObjFeltOrderUpdDetails);
            }
            resultSetTemp.close();
            statementTemp.close();
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public static HashMap getHistoryList(int gCompanyID, String productionDocumentNo) {
        HashMap hmHistoryList=new HashMap();
        Statement stTmp;
        ResultSet rsTmp;
        try {
            stTmp=connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_SAL_LC_PARTY_UPD_H WHERE LC_ID='"+productionDocumentNo+"' GROUP BY REVISION_NO");
            
            while(rsTmp.next()) {
                clsLCPartyUpd ObjFeltOrderUpd = new clsLCPartyUpd();
                
                ObjFeltOrderUpd.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjFeltOrderUpd.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjFeltOrderUpd.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjFeltOrderUpd.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjFeltOrderUpd.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                hmHistoryList.put(Integer.toString(hmHistoryList.size()+1),ObjFeltOrderUpd);
            }
            
            rsTmp.close();
            stTmp.close();
            return hmHistoryList;
        }
        catch(Exception e) {
            e.printStackTrace();
            return hmHistoryList;
        }
    }
    
    public boolean ShowHistory(int pCompanyID,String pDocNo) {
        Ready=false;
        try {
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            resultSet=statement.executeQuery("SELECT DISTINCT REVISION_NO FROM D_SAL_LC_PARTY_UPD_H WHERE LC_ID ='"+pDocNo+"'");
            Ready=true;            
            historyAmendID = pDocNo;
            HistoryView=true;
            MoveFirst();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    
    
    
    // Find Pending DOCUMENTS
    public static HashMap getPendingApprovals(int pUserID,int pOrder) {
        String strSQL="";
        
        HashMap List=new HashMap();
        int Counter=0;
        
        try {
            
            if(pOrder==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT DISTINCT LC_ID,LC_OPENER,RECEIVED_DATE FROM D_SAL_LC_PARTY_UPD,D_COM_DOC_DATA WHERE LC_ID=DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=171 AND CANCELLED=0 ORDER BY RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT DISTINCT LC_ID,LC_OPENER,RECEIVED_DATE FROM D_SAL_LC_PARTY_UPD,D_COM_DOC_DATA WHERE LC_ID=DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=171 AND CANCELLED=0 ORDER BY RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT DISTINCT LC_ID,LC_OPENER,RECEIVED_DATE FROM D_SAL_LC_PARTY_UPD,D_COM_DOC_DATA WHERE LC_ID=DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=171 AND CANCELLED=0 ORDER BY LC_ID";
            }
            
            ResultSet rsTmp=data.getConn().createStatement().executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp.next()) {
                
                Counter=Counter+1;
                clsLCPartyUpd ObjDoc=new clsLCPartyUpd();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("LC_ID",rsTmp.getString("LC_ID"));
                ObjDoc.setAttribute("LC_OPENER",rsTmp.getString("LC_OPENER"));
                ObjDoc.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                // ----------------- End of Header Fields ------------------------------------//
                
                List.put(Integer.toString(Counter),ObjDoc);
            }
            rsTmp.close();
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        
        return List;
    }
    
    public String getPartyCode(String pPieceNo) {
        return data.getStringValueFromDB("SELECT PARTY_CD FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
       
    }
       public String getOrderDate(String pPieceNo) {
        return data.getStringValueFromDB("SELECT ORDER_DATE FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
       
    }
       public String getProductCode(String pPieceNo) {
        return data.getStringValueFromDB("SELECT PRODUCT_CODE FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
       
    }
       public String getGroupCD(String pPieceNo) {
        return data.getStringValueFromDB("SELECT GRUP FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
       
    }
       public String getStyle(String pPieceNo) {
        return data.getStringValueFromDB("SELECT BALNK FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
       
    }
       public String getLength(String pPieceNo) {
        return data.getStringValueFromDB("SELECT LNGTH FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
       
    }
       public String getWidth(String pPieceNo) {
        return data.getStringValueFromDB("SELECT WIDTH FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
       
    }
       public String getGSM(String pPieceNo) {
        return data.getStringValueFromDB("SELECT GSQ FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
       
    }
       public String getWeight(String pPieceNo) {
        return data.getStringValueFromDB("SELECT WEIGHT FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
       
    }
       public String getReqDate(String pPieceNo) {
        return data.getStringValueFromDB("SELECT DELIV_DATE FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
       
    }
       public String getCommDate(String pPieceNo) {
        return data.getStringValueFromDB("SELECT COMM_DATE FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
       
    }
       public String getAgreedDate(String pPieceNo) {
        return data.getStringValueFromDB("SELECT AGREED_DATE FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
       
    }
       public String getRevReqDate(String pPieceNo) {
        return data.getStringValueFromDB("SELECT REV_REQ_DATE FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
       
    }
       public String getRevCommDate(String pPieceNo) {
        return data.getStringValueFromDB("SELECT REV_COMM_DATE FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
       
    }
       public String getRevAgreedDate(String pPieceNo) {
        return data.getStringValueFromDB("SELECT REV_AGREED_DATE FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
       
    }
       public String getRevReqReason(String pPieceNo) {
        return data.getStringValueFromDB("SELECT REV_REQ_REASON FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
       
    }
       public String getRevCommReason(String pPieceNo) {
        return data.getStringValueFromDB("SELECT REV_COMM_REASON FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
       
    }
       public String getMachineNo(String pPieceNo) {
        return data.getStringValueFromDB("SELECT MACHINE_NO FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
       
    }
       public String getPositionNo(String pPieceNo) {
        return data.getStringValueFromDB("SELECT POSITION FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
       
    }
       public String getRefNo(String pPieceNo) {
        return data.getStringValueFromDB("SELECT REF_NO FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
       
    }
       public String getConfNo(String pPieceNo) {
        return data.getStringValueFromDB("SELECT CONF_NO FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
       
    }
       public String getPONo(String pPieceNo) {
        return data.getStringValueFromDB("SELECT PO_NO FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
       
    }
       public String getPODate(String pPieceNo) {
        return data.getStringValueFromDB("SELECT PO_DATE FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
       
    }
       public String getPORemarks(String pPieceNo) {
        return data.getStringValueFromDB("SELECT PO_REMARK FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
       
    }
       public String getWvgDate(String pPieceNo) {
        return data.getStringValueFromDB("SELECT WVG_DATE FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
       
    } public String getMndDate(String pPieceNo) {
        return data.getStringValueFromDB("SELECT MND_DATE FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
       
    } public String getNdlDate(String pPieceNo) {
        return data.getStringValueFromDB("SELECT NDL_DATE FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
       
    }
     
       
    // Updates Weaving Date in Order Master Table To confirm that Weaving has completed
   // private void updateWeavingDate(String documentNo){
     //   data.Execute("UPDATE PRODUCTION.FELT_ORDER_MASTER,PRODUCTION.FELT_PROD_DATA SET WVG_DATE=PROD_DATE WHERE PIECE_NO+0=PROD_PIECE_NO+0 AND PARTY_CD=PROD_PARTY_CODE AND PROD_DEPT='WEAVING' AND PROD_DOC_NO='"+documentNo+"' AND APPROVED=1");
  
   // }
   
    // private void updateWeavingcounter(String documentNo){
     //   data.Execute("UPDATE PRODUCTION.FELT_PROD_DATA  SET WEAVE_DIFF_DAYS =DATEDIFF(PROD_DATE,WEAVE_DATE)  WHERE PROD_DEPT='WEAVING'  AND PROD_DOC_NO='"+documentNo+"'");

   // }
    
    // checks piece no already exist in db
    public boolean checkPieceNoInDB(String pPieceNo){
        int count=data.getIntValueFromDB("SELECT COUNT(FLT_PIECE_NO) FROM PRODUCTION.FELT_ORDER_AMENDMENT WHERE FLT_PIECE_NO='"+pPieceNo+"' AND FLT_AMEND_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND FLT_AMEND_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
        if(count>0) return true;
        else return false;
    }
    
    public boolean checkPieceNoInDB(String pPieceNo, String pProdDate){
        int count=data.getIntValueFromDB("SELECT COUNT(FLT_PIECE_NO) FROM PRODUCTION.FELT_ORDER_AMENDMENT WHERE FLT_PIECE_NO='"+pPieceNo+"' AND FLT_AMEND_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND FLT_AMEND_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
        if(count>=1){
            int counter=0;
            try{
                Connection Conn=data.getConn();
                Statement stTmp=Conn.createStatement();
                ResultSet rsTmp=stTmp.executeQuery("SELECT FLT_AMEND_DATE FROM PRODUCTION.FELT_ORDER_AMENDMENT WHERE FLT_PIECE_NO='"+pPieceNo+"' AND FLT_AMEND_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND FLT_AMEND_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
                while(rsTmp.next()){
                    if(rsTmp.getString("FLT_AMEND_DATE").equals(EITLERPGLOBAL.formatDateDB(pProdDate))) counter++;
                }
            }catch(SQLException e){e.printStackTrace();}
            if(counter==1 && count>=2) return true;
            else if(counter==1) return false;
            else return true;
        }else return false;
    }
    
    // checks production date already exist in db
    public boolean checkProductionDateInDB(String pProdDate){
        int count=data.getIntValueFromDB("SELECT COUNT(FLT_AMEND_DATE) FROM PRODUCTION.FELT_ORDER_AMENDMENT WHERE FLT_AMEND_DATE='"+pProdDate+"'");
        if(count>0) return true;
        else return false;
    }
    
    // it creates list of user name(felt) to be displayed
    public HashMap getUserNameList(int pHierarchyId, int pUserId, String pModule){
        HashMap hmUserNameList= new HashMap();
        char category=' ';
        try{
            Connection Conn=data.getConn();
            Statement stTmp=Conn.createStatement();
            ResultSet rsHierarchyRights=stTmp.executeQuery("SELECT CREATOR, APPROVER, FINAL_APPROVER FROM D_COM_HIERARCHY_RIGHTS WHERE COMPANY_ID=2 AND HIERARCHY_ID="+pHierarchyId+" AND USER_ID="+pUserId);
            while(rsHierarchyRights.next()){
                boolean creator=rsHierarchyRights.getBoolean("CREATOR");
                boolean approver=rsHierarchyRights.getBoolean("APPROVER");
                boolean finalApprover=rsHierarchyRights.getBoolean("FINAL_APPROVER");
                if(approver)category='A';
                if(creator)category='C';
                if(finalApprover)category='F';
            }
            
            int counter=1;
            ComboData cData=new ComboData();
            cData.Code=0;
            cData.Text="Select User";
            hmUserNameList.put(new Integer(counter++), cData);
            ResultSet rsTmp=stTmp.executeQuery("SELECT USER_ID,USER_NAME FROM PRODUCTION.`FELT_USER` WHERE USER_MODULE='"+pModule+"' AND USER_CATEG='"+category+"' ORDER BY USER_NAME");
            while(rsTmp.next()){
                ComboData aData=new ComboData();
                aData.Code=rsTmp.getInt("USER_ID");
                aData.Text=rsTmp.getString("USER_NAME");
                hmUserNameList.put(new Integer(counter++), aData);
            }
        }catch(SQLException e){e.printStackTrace();}
        return hmUserNameList;
    }
   
    public static String[] getPiecedetail(String pPieceNo,String pPartyCode){
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String []Piecedetail = new String[40];
        String []error = {"error"};
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsTmp=stTmp.executeQuery("SELECT GSQ FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO="+pPieceNo+" AND PARTY_CD='"+pPartyCode+"'");
            String strSQL="SELECT PIECE_NO,PARTY_CD,ORDER_DATE,PRODUCT_CODE,GRUP,BALNK AS STYLE,LNGTH,WIDTH,GSQ,WEIGHT,DELIV_DATE,COMM_DATE,AGREED_DATE,REV_REQ_DATE,REV_COMM_DATE,REV_AGREED_DATE,REV_REQ_REASON,REV_COMM_REASON,MACHINE_NO,POSITION,REF_NO,CONF_NO,PO_NO,PO_DATE,PO_REMARK,WVG_DATE,MND_DATE,NDL_DATE FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO="+pPieceNo+" AND PARTY_CD='"+pPartyCode+"'";
            rsTmp=stTmp.executeQuery(strSQL);
            System.out.println(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow() > 0) {

                Piecedetail[1]=rsTmp.getString("PIECE_NO");
                Piecedetail[2]=rsTmp.getString("PARTY_CD");
                Piecedetail[3]=rsTmp.getString("ORDER_DATE");
                //Piecedetail[4]=rsTmp.getString("PRODUCT_C0DE");
                Piecedetail[5]=rsTmp.getString("GRUP");
                Piecedetail[6]=rsTmp.getString("STYLE");
                Piecedetail[7]=rsTmp.getString("LNGTH");
                Piecedetail[8]=rsTmp.getString("WIDTH");
                Piecedetail[9]=rsTmp.getString("GSQ");
                Piecedetail[10]=rsTmp.getString("WEIGHT");                                
                Piecedetail[11]=rsTmp.getString("DELIV_DATE");
                Piecedetail[12]=rsTmp.getString("COMM_DATE");
                Piecedetail[13]=rsTmp.getString("AGREED_DATE");
                Piecedetail[14]=rsTmp.getString("REV_REQ_DATE");
                Piecedetail[15]=rsTmp.getString("REV_COMM_DATE");
                Piecedetail[16]=rsTmp.getString("REV_AGREED_DATE");
                Piecedetail[17]=rsTmp.getString("REV_REQ_REASON");
                Piecedetail[18]=rsTmp.getString("REV_COMM_REASON");
                Piecedetail[19]=rsTmp.getString("MACHINE_NO");
                Piecedetail[20]=rsTmp.getString("POSITION");
                Piecedetail[21]=rsTmp.getString("REF_NO");
                Piecedetail[22]=rsTmp.getString("CONF_NO");
                Piecedetail[23]=rsTmp.getString("PO_NO");
                Piecedetail[24]=rsTmp.getString("PO_DATE");
                Piecedetail[25]=rsTmp.getString("PO_REMARKS");
                Piecedetail[26]=EITLERPGLOBAL.formatDate(rsTmp.getString("WVG_DATE"));
                Piecedetail[27]=EITLERPGLOBAL.formatDate(rsTmp.getString("MND_DATE"));
                Piecedetail[28]=EITLERPGLOBAL.formatDate(rsTmp.getString("NDL_DATE"));
  //              Piecedetail[30]=rsTmp.getString("INCHARGE_NAME");
                /*Piecedetail[31]=rsTmp.getString("CHEM_TRT_IN");
                Piecedetail[32]=rsTmp.getString("PIN_IND");
                Piecedetail[33]=rsTmp.getString("CHARGES");
                Piecedetail[34]=rsTmp.getString("SPR_IND");
                Piecedetail[35]=rsTmp.getString("SQM_IND");                
                */
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            return Piecedetail;
            
        }
        catch(Exception e) {
            e.printStackTrace();
            return error;
        }
    }
           
    public static String getNextFreeNo(int pModuleID,boolean UpdateLastNo) {
        Connection tmpConn;
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        String strNewNo="";
        int lnNewNo=0;
        String Prefix="";
        String Suffix="";
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            //strSQL="SELECT LAST_USED_NO,PADDING_BY,NO_LENGTH FROM D_COM_FIRSTFREE WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND PREFIX_CHARS='"+pPrefix+"' AND SUFFIX_CHARS='"+pSuffix+"'";
            strSQL="SELECT LAST_USED_NO,PADDING_BY,NO_LENGTH,PREFIX_CHARS,SUFFIX_CHARS FROM D_COM_FIRSTFREE WHERE COMPANY_ID=2 AND MODULE_ID=171 AND FIRSTFREE_NO=178";
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                lnNewNo=rsTmp.getInt("LAST_USED_NO")+1;
                strNewNo=EITLERPGLOBAL.Padding(Integer.toString(lnNewNo),rsTmp.getInt("NO_LENGTH"),rsTmp.getString("PADDING_BY"));
                Prefix=rsTmp.getString("PREFIX_CHARS");
                Suffix=rsTmp.getString("SUFFIX_CHARS");
                
                if(UpdateLastNo) {
                    //Update last no. in database
                    //data.Execute("UPDATE D_COM_FIRSTFREE SET LAST_USED_NO='"+strNewNo.trim()+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND PREFIX_CHARS='"+pPrefix+"' AND SUFFIX_CHARS='"+pSuffix+"'");
                    data.Execute("UPDATE D_COM_FIRSTFREE SET LAST_USED_NO='"+strNewNo.trim()+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=2 AND MODULE_ID=171 AND FIRSTFREE_NO=178");
                }
                
                strNewNo = Prefix+ strNewNo+Suffix;
                
                //tmpConn.close();
                tmpStmt.close();
                rsTmp.close();
                
                return strNewNo;
            }
            else {
                return "";
            }
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    public void AddPartyToPolicyLCMaster(String LcID) {
        //========== Add Party to Policy LC Master ===============//        
        /*
        clsSupplier objSupplier=new clsSupplier();
        clsLCPartyUpd objPartyUpd=new clsLCPartyUpd();        
        
        if(objSupplier.Filter(" WHERE LC_ID='"+LcID+"' ")) {
            try {
                Connection Conn;
                Statement stLCParty;
                ResultSet rsLCParty;
                
                Conn=data.getConn();
                stLCParty=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsLCParty=stLCParty.executeQuery("SELECT * FROM D_SAL_POLICY_LC_MASTER LIMIT 1");
                
                String MainCodes[] = objPartyUpd.getAttribute("MAIN_ACCOUNT_CODE").getString().split(",");
                
                for(int i=0;i<MainCodes.length;i++) {
                    
                    long Counter=data.getLongValueFromDB("SELECT MAX(PARTY_ID) AS PARTY_ID FROM D_FIN_PARTY_MASTER", FinanceGlobal.FinURL);
                    Counter++;
                    rsFinParty.moveToInsertRow();
                    rsFinParty.updateInt("COMPANY_ID",2);
                    rsFinParty.updateString("PARTY_CODE",objSupplier.getAttribute("SUPPLIER_CODE").getString());
                    rsFinParty.updateString("MAIN_ACCOUNT_CODE",MainCodes[i]);
                    rsFinParty.updateLong("PARTY_ID",Counter);
                    rsFinParty.updateString("PARTY_NAME",objSupplier.getAttribute("SUPP_NAME").getString());
                    rsFinParty.updateString("SH_NAME","");
                    rsFinParty.updateString("REMARKS","");
                    rsFinParty.updateString("SH_CODE","");
                    rsFinParty.updateDouble("CREDIT_DAYS",0);
                    rsFinParty.updateDouble("CREDIT_LIMIT",0);
                    rsFinParty.updateDouble("DEBIT_LIMIT",0);
                    rsFinParty.updateString("TIN_NO",objSupplier.getAttribute("GST_NO").getString());
                    rsFinParty.updateString("TIN_DATE",objSupplier.getAttribute("GST_DATE").getString());
                    rsFinParty.updateString("CST_NO",objSupplier.getAttribute("CST_NO").getString());
                    rsFinParty.updateString("CST_DATE",objSupplier.getAttribute("CST_DATE").getString());
                    rsFinParty.updateString("ECC_NO",objSupplier.getAttribute("ECC_NO").getString());
                    rsFinParty.updateString("ECC_DATE",objSupplier.getAttribute("ECC_DATE").getString());
                    rsFinParty.updateString("SERVICE_TAX_NO",objSupplier.getAttribute("SERVICETAX_NO").getString());
                    rsFinParty.updateString("SERVICE_TAX_DATE",objSupplier.getAttribute("SERVICETAX_DATE").getString());
                    rsFinParty.updateString("SSI_NO",objSupplier.getAttribute("SSIREG_NO").getString());
                    rsFinParty.updateString("SSI_DATE",objSupplier.getAttribute("SSIREG_DATE").getString());
                    rsFinParty.updateString("ESI_NO",objSupplier.getAttribute("ESIREG_NO").getString());
                    rsFinParty.updateString("ESI_DATE",objSupplier.getAttribute("ESIREG_DATE").getString());
                    rsFinParty.updateString("ADDRESS",objSupplier.getAttribute("ADD1").getString().trim()+objSupplier.getAttribute("ADD2").getString().trim()+objSupplier.getAttribute("ADD3").getString().trim());
                    rsFinParty.updateString("CITY",objSupplier.getAttribute("CITY").getString());
                    rsFinParty.updateString("PINCODE",objSupplier.getAttribute("PINCODE").getString());
                    rsFinParty.updateString("STATE",objSupplier.getAttribute("STATE").getString());
                    rsFinParty.updateString("COUNTRY",objSupplier.getAttribute("COUNTRY").getString());
                    rsFinParty.updateString("PHONE",objSupplier.getAttribute("PHONE_O").getString());
                    rsFinParty.updateString("FAX",objSupplier.getAttribute("FAX_NO").getString());
                    rsFinParty.updateString("MOBILE",objSupplier.getAttribute("MOBILE_NO").getString());
                    rsFinParty.updateString("EMAIL",objSupplier.getAttribute("EMAIL_ADD").getString());
                    rsFinParty.updateString("URL",objSupplier.getAttribute("URL").getString());
                    rsFinParty.updateInt("APPROVED",1);
                    rsFinParty.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsFinParty.updateInt("REJECTED",0);
                    rsFinParty.updateString("REJECTED_DATE","");
                    rsFinParty.updateString("REJECTED_REMARKS","");
                    rsFinParty.updateString("REJECTED_DATE","");
                    rsFinParty.updateInt("HIERARCHY_ID",0);
                    rsFinParty.updateInt("CANCELLED",0);
                    rsFinParty.updateInt("BLOCKED",0);
                    rsFinParty.updateString("PAN_NO",objSupplier.getAttribute("PAN_NO").getString().trim());
                    rsFinParty.updateString("PAN_DATE",objSupplier.getAttribute("PAN_DATE").getString().trim());
                    rsFinParty.updateInt("CHANGED",1);
                    rsFinParty.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsFinParty.updateString("CREATED_BY","System");
                    rsFinParty.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsFinParty.updateString("MODIFIED_BY","System");
                    rsFinParty.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsFinParty.updateDouble("CLOSING_BALANCE",0);
                    rsFinParty.updateString("CLOSING_EFFECT","D");
                    
                    //BHAVESH
                    rsFinParty.updateInt("PARTY_TYPE",0);
                    rsFinParty.updateString("CHARGE_CODE_II","");
                    rsFinParty.updateString("CHARGE_CODE","");
                    
                    rsFinParty.insertRow();
                }
                
                //rsFinParty.close();
                //stFinParty.close();
                //FinConn.close();
            }
            catch(Exception p) {
                
            }
         */
            //============================================================//
        }
    
}
