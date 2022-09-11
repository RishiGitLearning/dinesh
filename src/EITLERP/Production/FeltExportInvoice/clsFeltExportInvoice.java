/*
 * clsFeltExportInvoice.java
 *
 * Created on June 19, 2013, 4:26 PM
 */

package EITLERP.Production.FeltExportInvoice;

import java.util.HashMap;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import EITLERP.EITLERPGLOBAL;
import EITLERP.Variant;
import EITLERP.data;
import EITLERP.ComboData;
import EITLERP.Production.clsFeltProductionApprovalFlow;

/**
 * @author  Vivek Kumar
 */

public class clsFeltExportInvoice {
    
    public String LastError="";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    
    public HashMap props;
    public boolean Ready = false;
    //Felt Export Invoice Details Collection
    public HashMap hmFeltExportInvoiceDetails;
    
    //History Related properties
    public boolean HistoryView=false;
    
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
    
    /** Creates new DataFeltExportInvoice */
    public clsFeltExportInvoice() {
        LastError = "";
        props=new HashMap();
        props.put("INVOICE_DATE", new Variant(""));
        props.put("INVOICE_NO", new Variant(""));
        props.put("PARTY_CODE", new Variant(""));
        props.put("DOC_NO", new Variant(""));
        props.put("ORDER_NO", new Variant(""));
        props.put("ORDER_DATE", new Variant(""));
        props.put("FELT_DESCRIPTION", new Variant(""));
        props.put("DELIVERY_TERM", new Variant(""));
        props.put("PAYMENT_TERM", new Variant(""));
        props.put("TOTAL_AMOUNT",new Variant(0.00));
        props.put("AMOUNT_IN_WORDS", new Variant(""));
        props.put("PRE_CARRIAGE_BY", new Variant(""));
        props.put("CARRIAGE_RECEIPT_PLACE", new Variant(""));
        props.put("LOADING_PORT", new Variant(""));
        props.put("DISCHARGE_PORT", new Variant(""));
        props.put("FINAL_DESTINATION", new Variant(""));
        props.put("DESTINATION_COUNTRY", new Variant(""));
        props.put("RATE",new Variant(0.00));
        props.put("BALE_SIZE", new Variant(""));
        props.put("GROSS_WEIGHT",new Variant(0.00));
        props.put("NET_WEIGHT",new Variant(0.00));
        props.put("TOTAL_PIECES",new Variant(0));
        props.put("SQR_MTRS",new Variant(0.00));
        props.put("IMP_EXP_CODE", new Variant(""));
        props.put("PAN_NO", new Variant(""));
        props.put("PACKING", new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        // -- Approval Specific Fields --
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        props.put("UPDATED_BY",new Variant(0));
        props.put("ENTRY_DATE",new Variant(""));
        props.put("APPROVER_REMARKS",new Variant(""));
        props.put("CANCELED",new Variant(0));
        props.put("APPROVED",new Variant(0));
        props.put("REJECTED",new Variant(0));
        
        hmFeltExportInvoiceDetails=new HashMap();
    }
    
    public boolean LoadData() {
        Ready=false;
        try {
            connection = data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.FELT_EXPORT_INVOICE_HEADER WHERE INVOICE_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND INVOICE_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
            HistoryView=false;
            Ready=true;
            MoveLast();
            return true;
        }catch(Exception e) {
            LastError=e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public void Close() {
        try {
            statement.close();
            resultSet.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    //Navigation Methods
    public boolean MoveFirst() {
        try {
            resultSet.first();
            setData();
            return true;
        }catch(Exception e) {
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
            setData();
            return true;
        }catch(Exception e) {
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
            setData();
            return true;
        }catch(Exception e) {
            LastError=e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean MoveLast() {
        try {
            resultSet.last();
            setData();
            return true;
        }catch(Exception e) {
            LastError=e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean Insert() {
        ResultSet  resultSetDetail, resultSetDetailHistory, resultSetHistory;
        Statement  statementDetail, statementDetailHistory, statementHistory;
        try {
            // Invoice detail connection
            statementDetail = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetDetail = statementDetail.executeQuery("SELECT * FROM  PRODUCTION.FELT_EXPORT_INVOICE_DETAIL WHERE INVOICE_NO=''");
            
            // Invoice detail history connection
            statementDetailHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetDetailHistory = statementDetailHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_EXPORT_INVOICE_DETAIL_H WHERE INVOICE_NO=''");
            
            // Invoice data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_EXPORT_INVOICE_HEADER_H WHERE INVOICE_NO=''");
            
            
            resultSet.moveToInsertRow();
            resultSet.updateString("INVOICE_NO", getAttribute("INVOICE_NO").getString());
            resultSet.updateString("INVOICE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("INVOICE_DATE").getString()));
            resultSet.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            resultSet.updateString("DOC_NO", getDocumentNo(getAttribute("INVOICE_NO").getString()));
            resultSet.updateString("ORDER_NO",getAttribute("ORDER_NO").getString());
            resultSet.updateString("ORDER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ORDER_DATE").getString()));
            resultSet.updateString("FELT_DESCRIPTION",getAttribute("FELT_DESCRIPTION").getString());
            resultSet.updateString("DELIVERY_TERM",getAttribute("DELIVERY_TERM").getString());
            resultSet.updateString("PAYMENT_TERM",getAttribute("PAYMENT_TERM").getString());
            resultSet.updateFloat("TOTAL_AMOUNT",(float)EITLERPGLOBAL.round(getAttribute("TOTAL_AMOUNT").getVal(),2));
            resultSet.updateString("PRE_CARRIAGE_BY",getAttribute("PRE_CARRIAGE_BY").getString());
            resultSet.updateString("CARRIAGE_RECEIPT_PLACE",getAttribute("CARRIAGE_RECEIPT_PLACE").getString());
            resultSet.updateString("LOADING_PORT",getAttribute("LOADING_PORT").getString());
            resultSet.updateString("DISCHARGE_PORT",getAttribute("DISCHARGE_PORT").getString());
            resultSet.updateString("FINAL_DESTINATION",getAttribute("FINAL_DESTINATION").getString());
            resultSet.updateString("DESTINATION_COUNTRY",getAttribute("DESTINATION_COUNTRY").getString());
            resultSet.updateFloat("RATE",(float)EITLERPGLOBAL.round(getAttribute("RATE").getVal(),2));
            resultSet.updateString("BALE_SIZE",getAttribute("BALE_SIZE").getString());
            resultSet.updateFloat("GROSS_WEIGHT",(float)EITLERPGLOBAL.round(getAttribute("GROSS_WEIGHT").getVal(),2));
            resultSet.updateFloat("NET_WEIGHT",(float)EITLERPGLOBAL.round(getAttribute("NET_WEIGHT").getVal(),2));
            resultSet.updateInt("TOTAL_PIECES",getAttribute("TOTAL_PIECES").getInt());
            resultSet.updateFloat("SQR_MTRS",(float)EITLERPGLOBAL.round(getAttribute("SQR_MTRS").getVal(),2));
            resultSet.updateString("IMP_EXP_CODE",getAttribute("IMP_EXP_CODE").getString());
            resultSet.updateString("PAN_NO",getAttribute("PAN_NO").getString());
            resultSet.updateString("PACKING",getAttribute("PACKING").getString());
            resultSet.updateInt("CREATED_BY",getAttribute("CREATED_BY").getInt());
            resultSet.updateString("CREATED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSet.updateInt("MODIFIED_BY",0);
            resultSet.updateString("MODIFIED_DATE","0000-00-00");
            resultSet.updateBoolean("APPROVED",false);
            resultSet.updateString("APPROVED_DATE","0000-00-00");
            resultSet.updateBoolean("REJECTED",false);
            resultSet.updateString("REJECTED_DATE","0000-00-00");
            resultSet.updateInt("CANCELED",0);
            resultSet.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            resultSet.updateInt("CHANGED",1);
            resultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            resultSet.insertRow();
            
            //========= Inserting Into Header History =================//
            resultSetHistory.moveToInsertRow();
            resultSetHistory.updateInt("REVISION_NO",1);
            resultSetHistory.updateString("INVOICE_NO", getAttribute("INVOICE_NO").getString());
            resultSetHistory.updateString("INVOICE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("INVOICE_DATE").getString()));
            resultSetHistory.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            resultSetHistory.updateString("DOC_NO", getDocumentNo(getAttribute("INVOICE_NO").getString()));
            resultSetHistory.updateString("ORDER_NO",getAttribute("ORDER_NO").getString());
            resultSetHistory.updateString("ORDER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ORDER_DATE").getString()));
            resultSetHistory.updateString("FELT_DESCRIPTION",getAttribute("FELT_DESCRIPTION").getString());
            resultSetHistory.updateString("DELIVERY_TERM",getAttribute("DELIVERY_TERM").getString());
            resultSetHistory.updateString("PAYMENT_TERM",getAttribute("PAYMENT_TERM").getString());
            resultSetHistory.updateFloat("TOTAL_AMOUNT",(float)EITLERPGLOBAL.round(getAttribute("TOTAL_AMOUNT").getVal(),2));
            resultSetHistory.updateString("PRE_CARRIAGE_BY",getAttribute("PRE_CARRIAGE_BY").getString());
            resultSetHistory.updateString("CARRIAGE_RECEIPT_PLACE",getAttribute("CARRIAGE_RECEIPT_PLACE").getString());
            resultSetHistory.updateString("LOADING_PORT",getAttribute("LOADING_PORT").getString());
            resultSetHistory.updateString("DISCHARGE_PORT",getAttribute("DISCHARGE_PORT").getString());
            resultSetHistory.updateString("FINAL_DESTINATION",getAttribute("FINAL_DESTINATION").getString());
            resultSetHistory.updateString("DESTINATION_COUNTRY",getAttribute("DESTINATION_COUNTRY").getString());
            resultSetHistory.updateFloat("RATE",(float)EITLERPGLOBAL.round(getAttribute("RATE").getVal(),2));
            resultSetHistory.updateString("BALE_SIZE",getAttribute("BALE_SIZE").getString());
            resultSetHistory.updateFloat("GROSS_WEIGHT",(float)EITLERPGLOBAL.round(getAttribute("GROSS_WEIGHT").getVal(),2));
            resultSetHistory.updateFloat("NET_WEIGHT",(float)EITLERPGLOBAL.round(getAttribute("NET_WEIGHT").getVal(),2));
            resultSetHistory.updateInt("TOTAL_PIECES",getAttribute("TOTAL_PIECES").getInt());
            resultSetHistory.updateFloat("SQR_MTRS",(float)EITLERPGLOBAL.round(getAttribute("SQR_MTRS").getVal(),2));
            resultSetHistory.updateString("IMP_EXP_CODE",getAttribute("IMP_EXP_CODE").getString());
            resultSetHistory.updateString("PAN_NO",getAttribute("PAN_NO").getString());
            resultSetHistory.updateString("PACKING",getAttribute("PACKING").getString());
            resultSetHistory.updateInt("UPDATED_BY",getAttribute("UPDATED_BY").getInt());
            resultSetHistory.updateString("ENTRY_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSetHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            resultSetHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            resultSetHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            resultSetHistory.updateInt("CHANGED",1);
            resultSetHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            resultSetHistory.insertRow();
            
            for(int i=1;i<=hmFeltExportInvoiceDetails.size();i++) {
                clsFeltExportInvoiceDetails ObjFeltExportInvoiceDetails=(clsFeltExportInvoiceDetails) hmFeltExportInvoiceDetails.get(Integer.toString(i));
                
                //Insert records into invoice detail table
                resultSetDetail.moveToInsertRow();
                resultSetDetail.updateString("INVOICE_NO", getAttribute("INVOICE_NO").getString());
                resultSetDetail.updateString("INVOICE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("INVOICE_DATE").getString()));
                resultSetDetail.updateString("PIECE_NO",ObjFeltExportInvoiceDetails.getAttribute("PIECE_NO").getString());
                resultSetDetail.updateString("FELT_SIZE",ObjFeltExportInvoiceDetails.getAttribute("FELT_SIZE").getString());
                resultSetDetail.updateFloat("WEIGHT",(float)EITLERPGLOBAL.round(ObjFeltExportInvoiceDetails.getAttribute("WEIGHT").getVal(),2));
                resultSetDetail.updateFloat("AMOUNT",(float)EITLERPGLOBAL.round(ObjFeltExportInvoiceDetails.getAttribute("AMOUNT").getVal(),2));
                resultSetDetail.updateString("BALE_NO",ObjFeltExportInvoiceDetails.getAttribute("BALE_NO").getString());
                resultSetDetail.updateInt("CREATED_BY",getAttribute("CREATED_BY").getInt());
                resultSetDetail.updateString("CREATED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetDetail.updateInt("MODIFIED_BY",0);
                resultSetDetail.updateString("MODIFIED_DATE","0000-00-00");
                resultSetDetail.updateInt("CANCELED",0);
                resultSetDetail.updateInt("CHANGED",1);
                resultSetDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetail.insertRow();
                
                //Insert records into invoice detail history table
                resultSetDetailHistory.moveToInsertRow();
                resultSetDetailHistory.updateInt("REVISION_NO",1);
                resultSetDetailHistory.updateString("INVOICE_NO", getAttribute("INVOICE_NO").getString());
                resultSetDetailHistory.updateString("INVOICE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("INVOICE_DATE").getString()));
                resultSetDetailHistory.updateString("PIECE_NO",ObjFeltExportInvoiceDetails.getAttribute("PIECE_NO").getString());
                resultSetDetailHistory.updateString("FELT_SIZE",ObjFeltExportInvoiceDetails.getAttribute("FELT_SIZE").getString());
                resultSetDetailHistory.updateFloat("WEIGHT",(float)EITLERPGLOBAL.round(ObjFeltExportInvoiceDetails.getAttribute("WEIGHT").getVal(),2));
                resultSetDetailHistory.updateFloat("AMOUNT",(float)EITLERPGLOBAL.round(ObjFeltExportInvoiceDetails.getAttribute("AMOUNT").getVal(),2));
                resultSetDetailHistory.updateString("BALE_NO",ObjFeltExportInvoiceDetails.getAttribute("BALE_NO").getString());
                resultSetDetailHistory.updateInt("UPDATED_BY",getAttribute("UPDATED_BY").getInt());
                resultSetDetailHistory.updateString("ENTRY_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetDetailHistory.updateInt("CHANGED",1);
                resultSetDetailHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetailHistory.insertRow();
            }
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=713; //Felt  EXPORT INVOICE
            ObjFeltProductionApprovalFlow.DocNo=getDocumentNo(getAttribute("INVOICE_NO").getString());
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName="FELT_EXPORT_INVOICE_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator=true;
            ObjFeltProductionApprovalFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName="DOC_NO";
            
            if(ObjFeltProductionApprovalFlow.Status.equals("H")) {
                ObjFeltProductionApprovalFlow.Status="A";
                ObjFeltProductionApprovalFlow.To=ObjFeltProductionApprovalFlow.From;
                ObjFeltProductionApprovalFlow.UpdateFlow();
            }
            else {
                if(!ObjFeltProductionApprovalFlow.UpdateFlow()) {
                    LastError=ObjFeltProductionApprovalFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------
            
            MoveLast();
            resultSetDetail.close();
            statementDetail.close();
            resultSetDetailHistory.close();
            statementDetailHistory.close();
            resultSetHistory.close();
            statementHistory.close();
            return true;
        }catch(Exception e) {
            LastError= e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    //Updates current record
    public boolean Update() {
        ResultSet  resultSetDetail, resultSetDetailHistory, resultSetHistory;
        Statement  statementDetail, statementDetailHistory, statementHistory;
        
        try {
            // Invoice detail connection
            statementDetail = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetDetail = statementDetail.executeQuery("SELECT * FROM  PRODUCTION.FELT_EXPORT_INVOICE_DETAIL WHERE INVOICE_NO=''");
            
            // Invoice detail history connection
            statementDetailHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetDetailHistory = statementDetailHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_EXPORT_INVOICE_DETAIL_H WHERE INVOICE_NO=''");
            
            // Invoice data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_EXPORT_INVOICE_HEADER_H WHERE INVOICE_NO=''");
            
            resultSet.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            resultSet.updateString("ORDER_NO",getAttribute("ORDER_NO").getString());
            resultSet.updateString("ORDER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ORDER_DATE").getString()));
            resultSet.updateString("FELT_DESCRIPTION",getAttribute("FELT_DESCRIPTION").getString());
            resultSet.updateString("DELIVERY_TERM",getAttribute("DELIVERY_TERM").getString());
            resultSet.updateString("PAYMENT_TERM",getAttribute("PAYMENT_TERM").getString());
            resultSet.updateFloat("TOTAL_AMOUNT",(float)EITLERPGLOBAL.round(getAttribute("TOTAL_AMOUNT").getVal(),2));
            resultSet.updateString("PRE_CARRIAGE_BY",getAttribute("PRE_CARRIAGE_BY").getString());
            resultSet.updateString("CARRIAGE_RECEIPT_PLACE",getAttribute("CARRIAGE_RECEIPT_PLACE").getString());
            resultSet.updateString("LOADING_PORT",getAttribute("LOADING_PORT").getString());
            resultSet.updateString("DISCHARGE_PORT",getAttribute("DISCHARGE_PORT").getString());
            resultSet.updateString("FINAL_DESTINATION",getAttribute("FINAL_DESTINATION").getString());
            resultSet.updateString("DESTINATION_COUNTRY",getAttribute("DESTINATION_COUNTRY").getString());
            resultSet.updateFloat("RATE",(float)EITLERPGLOBAL.round(getAttribute("RATE").getVal(),2));
            resultSet.updateString("BALE_SIZE",getAttribute("BALE_SIZE").getString());
            resultSet.updateFloat("GROSS_WEIGHT",(float)EITLERPGLOBAL.round(getAttribute("GROSS_WEIGHT").getVal(),2));
            resultSet.updateFloat("NET_WEIGHT",(float)EITLERPGLOBAL.round(getAttribute("NET_WEIGHT").getVal(),2));
            resultSet.updateInt("TOTAL_PIECES",getAttribute("TOTAL_PIECES").getInt());
            resultSet.updateFloat("SQR_MTRS",(float)EITLERPGLOBAL.round(getAttribute("SQR_MTRS").getVal(),2));
            resultSet.updateString("IMP_EXP_CODE",getAttribute("IMP_EXP_CODE").getString());
            resultSet.updateString("PAN_NO",getAttribute("PAN_NO").getString());
            resultSet.updateString("PACKING",getAttribute("PACKING").getString());
            resultSet.updateInt("MODIFIED_BY",getAttribute("MODIFIED_BY").getInt());
            resultSet.updateString("MODIFIED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSet.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            resultSet.updateInt("CHANGED",1);
            resultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int revisionNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_EXPORT_INVOICE_HEADER_H WHERE INVOICE_DATE='"+EITLERPGLOBAL.formatDateDB(getAttribute("INVOICE_DATE").getString())+"' AND INVOICE_NO='"+getAttribute("INVOICE_NO").getString()+"'");
            revisionNo++;
            
            resultSetHistory.moveToInsertRow();
            resultSetHistory.updateInt("REVISION_NO",revisionNo);
            resultSetHistory.updateString("INVOICE_NO", getAttribute("INVOICE_NO").getString());
            resultSetHistory.updateString("INVOICE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("INVOICE_DATE").getString()));
            resultSetHistory.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSetHistory.updateString("ORDER_NO",getAttribute("ORDER_NO").getString());
            resultSetHistory.updateString("ORDER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ORDER_DATE").getString()));
            resultSetHistory.updateString("FELT_DESCRIPTION",getAttribute("FELT_DESCRIPTION").getString());
            resultSetHistory.updateString("DELIVERY_TERM",getAttribute("DELIVERY_TERM").getString());
            resultSetHistory.updateString("PAYMENT_TERM",getAttribute("PAYMENT_TERM").getString());
            resultSetHistory.updateFloat("TOTAL_AMOUNT",(float)EITLERPGLOBAL.round(getAttribute("TOTAL_AMOUNT").getVal(),2));
            resultSetHistory.updateString("PRE_CARRIAGE_BY",getAttribute("PRE_CARRIAGE_BY").getString());
            resultSetHistory.updateString("CARRIAGE_RECEIPT_PLACE",getAttribute("CARRIAGE_RECEIPT_PLACE").getString());
            resultSetHistory.updateString("LOADING_PORT",getAttribute("LOADING_PORT").getString());
            resultSetHistory.updateString("DISCHARGE_PORT",getAttribute("DISCHARGE_PORT").getString());
            resultSetHistory.updateString("FINAL_DESTINATION",getAttribute("FINAL_DESTINATION").getString());
            resultSetHistory.updateString("DESTINATION_COUNTRY",getAttribute("DESTINATION_COUNTRY").getString());
            resultSetHistory.updateFloat("RATE",(float)EITLERPGLOBAL.round(getAttribute("RATE").getVal(),2));
            resultSetHistory.updateString("BALE_SIZE",getAttribute("BALE_SIZE").getString());
            resultSetHistory.updateFloat("GROSS_WEIGHT",(float)EITLERPGLOBAL.round(getAttribute("GROSS_WEIGHT").getVal(),2));
            resultSetHistory.updateFloat("NET_WEIGHT",(float)EITLERPGLOBAL.round(getAttribute("NET_WEIGHT").getVal(),2));
            resultSetHistory.updateInt("TOTAL_PIECES",getAttribute("TOTAL_PIECES").getInt());
            resultSetHistory.updateFloat("SQR_MTRS",(float)EITLERPGLOBAL.round(getAttribute("SQR_MTRS").getVal(),2));
            resultSetHistory.updateString("IMP_EXP_CODE",getAttribute("IMP_EXP_CODE").getString());
            resultSetHistory.updateString("PAN_NO",getAttribute("PAN_NO").getString());
            resultSetHistory.updateString("PACKING",getAttribute("PACKING").getString());
            resultSetHistory.updateInt("UPDATED_BY",getAttribute("UPDATED_BY").getInt());
            resultSetHistory.updateString("ENTRY_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSetHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            resultSetHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            resultSetHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            resultSetHistory.updateInt("CHANGED",1);
            resultSetHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            resultSetHistory.insertRow();
            
            
            //delete records from invoice detail table before insert
            data.Execute("DELETE FROM PRODUCTION.FELT_EXPORT_INVOICE_DETAIL WHERE INVOICE_DATE='"+ EITLERPGLOBAL.formatDateDB(getAttribute("INVOICE_DATE").getString())+"' AND INVOICE_NO='"+getAttribute("INVOICE_NO").getString()+"'");
            
            //Now insert records into invoice detail tables
            for(int i=1;i<=hmFeltExportInvoiceDetails.size();i++) {
                clsFeltExportInvoiceDetails ObjFeltExportInvoiceDetails=(clsFeltExportInvoiceDetails) hmFeltExportInvoiceDetails.get(Integer.toString(i));
                
                //Insert records into invoice detail table
                resultSetDetail.moveToInsertRow();
                resultSetDetail.updateString("INVOICE_NO", getAttribute("INVOICE_NO").getString());
                resultSetDetail.updateString("INVOICE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("INVOICE_DATE").getString()));
                resultSetDetail.updateString("PIECE_NO",ObjFeltExportInvoiceDetails.getAttribute("PIECE_NO").getString());
                resultSetDetail.updateString("FELT_SIZE",ObjFeltExportInvoiceDetails.getAttribute("FELT_SIZE").getString());
                resultSetDetail.updateFloat("WEIGHT",(float)EITLERPGLOBAL.round(ObjFeltExportInvoiceDetails.getAttribute("WEIGHT").getVal(),2));
                resultSetDetail.updateFloat("AMOUNT",(float)EITLERPGLOBAL.round(ObjFeltExportInvoiceDetails.getAttribute("AMOUNT").getVal(),2));
                resultSetDetail.updateString("BALE_NO",ObjFeltExportInvoiceDetails.getAttribute("BALE_NO").getString());
                resultSetDetail.updateInt("CREATED_BY",getAttribute("CREATED_BY").getInt());
                resultSetDetail.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                resultSetDetail.updateInt("MODIFIED_BY",getAttribute("MODIFIED_BY").getInt());
                resultSetDetail.updateString("MODIFIED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetDetail.updateInt("CANCELED",0);
                resultSetDetail.updateInt("CHANGED",1);
                resultSetDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetail.insertRow();
                
                //Insert records into invoice detail history table
                resultSetDetailHistory.moveToInsertRow();
                resultSetDetailHistory.updateInt("REVISION_NO",revisionNo);
                resultSetDetailHistory.updateString("INVOICE_NO", getAttribute("INVOICE_NO").getString());
                resultSetDetailHistory.updateString("INVOICE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("INVOICE_DATE").getString()));
                resultSetDetailHistory.updateString("PIECE_NO",ObjFeltExportInvoiceDetails.getAttribute("PIECE_NO").getString());
                resultSetDetailHistory.updateString("FELT_SIZE",ObjFeltExportInvoiceDetails.getAttribute("FELT_SIZE").getString());
                resultSetDetailHistory.updateFloat("WEIGHT",(float)EITLERPGLOBAL.round(ObjFeltExportInvoiceDetails.getAttribute("WEIGHT").getVal(),2));
                resultSetDetailHistory.updateFloat("AMOUNT",(float)EITLERPGLOBAL.round(ObjFeltExportInvoiceDetails.getAttribute("AMOUNT").getVal(),2));
                resultSetDetailHistory.updateString("BALE_NO",ObjFeltExportInvoiceDetails.getAttribute("BALE_NO").getString());
                resultSetDetailHistory.updateInt("UPDATED_BY",getAttribute("UPDATED_BY").getInt());
                resultSetDetailHistory.updateString("ENTRY_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetDetailHistory.updateInt("CHANGED",1);
                resultSetDetailHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetailHistory.insertRow();
            }
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=713; //Felt EXPORT INVOICE
            ObjFeltProductionApprovalFlow.DocNo=getDocumentNo(getAttribute("INVOICE_NO").getString());
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.FELT_EXPORT_INVOICE_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator=false;
            ObjFeltProductionApprovalFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName="DOC_NO";
            
            if(getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFeltProductionApprovalFlow.To=(int)getAttribute("SEND_DOC_TO").getVal();
                ObjFeltProductionApprovalFlow.ExplicitSendTo=true;
            }
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.FELT_EXPORT_INVOICE_HEADER SET REJECTED=0,CHANGED=1 WHERE INVOICE_NO='"+getAttribute("INVOICE_NO").getString()+"' AND INVOICE_DATE='"+getAttribute("INVOICE_DATE").getString()+"' AND DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
                //Remove Old Records from FELT DOCUMENT APPROVAL TABLE
                //data.Execute("DELETE FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=713 AND DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
                
                ObjFeltProductionApprovalFlow.IsCreator=true;
            }
            //==========================================//
            
            if(ObjFeltProductionApprovalFlow.Status.equals("H")) {
                if(IsRejected) {
                    ObjFeltProductionApprovalFlow.Status="A";
                    ObjFeltProductionApprovalFlow.To=ObjFeltProductionApprovalFlow.From;
                    ObjFeltProductionApprovalFlow.UpdateFlow();
                }
            }
            else {
                if(!ObjFeltProductionApprovalFlow.UpdateFlow()) {
                    LastError=ObjFeltProductionApprovalFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------
            setData();
            resultSetDetail.close();
            statementDetail.close();
            resultSetDetailHistory.close();
            statementDetailHistory.close();
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
    public boolean CanDelete(String invoiceNo,String invoiceDate,int userID) {
        if(HistoryView) {
            return false;
        }
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            tmpStmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_EXPORT_INVOICE_HEADER WHERE INVOICE_NO='"+invoiceNo +"' AND INVOICE_DATE='"+EITLERPGLOBAL.formatDateDB(invoiceDate) +"' AND APPROVED=1";
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if(rsTmp.getInt("COUNT")>0){  //Invoice is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            }else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=713 AND USER_ID="+userID+" AND DOC_NO='"+ getAttribute("DOC_NO").getString()+"' AND STATUS='W'";
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getInt("COUNT")>0) {
                    strSQL = "DELETE FROM PRODUCTION.FELT_EXPORT_INVOICE_HEADER WHERE INVOICE_NO='"+invoiceNo+"' AND INVOICE_DATE='"+EITLERPGLOBAL.formatDateDB(invoiceDate)+"'";
                    tmpStmt.executeUpdate(strSQL);
                    strSQL = "DELETE FROM PRODUCTION.FELT_EXPORT_INVOICE_DETAIL WHERE INVOICE_NO='"+invoiceNo+"' AND INVOICE_DATE='"+EITLERPGLOBAL.formatDateDB(invoiceDate)+"'";
                    tmpStmt.executeUpdate(strSQL);
                    LoadData();
                    return true;
                }else {
                    LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
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
    public boolean IsEditable(String invoiceNo, String invoiceDate, int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        try {
            if(HistoryView) {
                return false;
            }
            tmpStmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_EXPORT_INVOICE_HEADER WHERE INVOICE_NO='"+ invoiceNo +"' AND INVOICE_DATE='"+ invoiceDate +"' AND APPROVED=1";
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0){  //Item is Approved
                //Discard the request
                return false;
            }else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=713 AND USER_ID="+userID+" AND DOC_NO='"+ getAttribute("DOC_NO").getString()+"' AND STATUS='W'";
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getInt("COUNT")>0) {
                    //Yes document is waiting for this user
                    return true;
                }else {
                    //Document is not editable by this user
                    return false;
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean Filter(String stringFindQuery) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM PRODUCTION.FELT_EXPORT_INVOICE_HEADER WHERE " + stringFindQuery;
            connection=data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet=statement.executeQuery(strSql);
            if(!resultSet.first()) {
                LoadData();
                Ready=true;
                return false;
            }else {
                Ready=true;
                MoveLast();
                return true;
            }
        }catch(Exception e) {
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
        try {
            if(HistoryView) {
                RevNo=resultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",resultSet.getInt("REVISION_NO"));
            }else {
                setAttribute("REVISION_NO",0);
                setAttribute("CREATED_BY",resultSet.getInt("CREATED_BY"));
                setAttribute("CREATED_DATE",resultSet.getString("CREATED_DATE"));
                setAttribute("MODIFIED_BY",resultSet.getInt("MODIFIED_BY"));
                setAttribute("MODIFIED_DATE",resultSet.getString("MODIFIED_DATE"));
                setAttribute("APPROVED",resultSet.getInt("APPROVED"));
                setAttribute("APPROVED_DATE",resultSet.getString("APPROVED_DATE"));
                setAttribute("REJECTED",resultSet.getBoolean("REJECTED"));
                setAttribute("REJECTED_DATE",resultSet.getString("REJECTED_DATE"));
                setAttribute("CANCELED",resultSet.getInt("CANCELED"));
            }
            
            setAttribute("INVOICE_NO",resultSet.getString("INVOICE_NO"));
            setAttribute("INVOICE_DATE",resultSet.getString("INVOICE_DATE"));
            setAttribute("PARTY_CODE",resultSet.getString("PARTY_CODE"));
            setAttribute("DOC_NO",resultSet.getString("DOC_NO"));
            setAttribute("ORDER_NO",resultSet.getString("ORDER_NO"));
            setAttribute("ORDER_DATE",resultSet.getString("ORDER_DATE"));
            setAttribute("FELT_DESCRIPTION",resultSet.getString("FELT_DESCRIPTION"));
            setAttribute("DELIVERY_TERM",resultSet.getString("DELIVERY_TERM"));
            setAttribute("PAYMENT_TERM",resultSet.getString("PAYMENT_TERM"));
            setAttribute("TOTAL_AMOUNT",resultSet.getFloat("TOTAL_AMOUNT"));
            setAttribute("PRE_CARRIAGE_BY",resultSet.getString("PRE_CARRIAGE_BY"));
            setAttribute("CARRIAGE_RECEIPT_PLACE",resultSet.getString("CARRIAGE_RECEIPT_PLACE"));
            setAttribute("LOADING_PORT",resultSet.getString("LOADING_PORT"));
            setAttribute("DISCHARGE_PORT",resultSet.getString("DISCHARGE_PORT"));
            setAttribute("FINAL_DESTINATION",resultSet.getString("FINAL_DESTINATION"));
            setAttribute("DESTINATION_COUNTRY",resultSet.getString("DESTINATION_COUNTRY"));
            setAttribute("RATE",resultSet.getFloat("RATE"));
            setAttribute("BALE_SIZE",resultSet.getString("BALE_SIZE"));
            setAttribute("GROSS_WEIGHT",resultSet.getFloat("GROSS_WEIGHT"));
            setAttribute("NET_WEIGHT",resultSet.getFloat("NET_WEIGHT"));
            setAttribute("TOTAL_PIECES",resultSet.getInt("TOTAL_PIECES"));
            setAttribute("SQR_MTRS",resultSet.getFloat("SQR_MTRS"));
            setAttribute("IMP_EXP_CODE",resultSet.getString("IMP_EXP_CODE"));
            setAttribute("PAN_NO",resultSet.getString("PAN_NO"));
            setAttribute("PACKING",resultSet.getString("PACKING"));
            setAttribute("HIERARCHY_ID",resultSet.getInt("HIERARCHY_ID"));
            
            //Now Populate the collection, first clear the collection
            hmFeltExportInvoiceDetails.clear();
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            if(HistoryView) {
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_EXPORT_INVOICE_DETAIL_H WHERE INVOICE_NO='"+ resultSet.getString("INVOICE_NO") +"' AND INVOICE_DATE='"+ resultSet.getString("INVOICE_DATE")+"' AND REVISION_NO="+RevNo);
            }else {
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_EXPORT_INVOICE_DETAIL WHERE INVOICE_NO='"+ resultSet.getString("INVOICE_NO") +"' AND INVOICE_DATE='"+ resultSet.getString("INVOICE_DATE")+"'");
            }
            
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
                clsFeltExportInvoiceDetails ObjFeltExportInvoiceDetails = new clsFeltExportInvoiceDetails();
                
                ObjFeltExportInvoiceDetails.setAttribute("INVOICE_NO",resultSet.getString("INVOICE_NO"));
                ObjFeltExportInvoiceDetails.setAttribute("INVOICE_DATE",resultSet.getString("INVOICE_DATE"));
                ObjFeltExportInvoiceDetails.setAttribute("PIECE_NO",resultSetTemp.getString("PIECE_NO"));
                ObjFeltExportInvoiceDetails.setAttribute("FELT_SIZE",resultSetTemp.getString("FELT_SIZE"));
                ObjFeltExportInvoiceDetails.setAttribute("WEIGHT",resultSetTemp.getFloat("WEIGHT"));
                ObjFeltExportInvoiceDetails.setAttribute("AMOUNT",resultSetTemp.getFloat("AMOUNT"));
                ObjFeltExportInvoiceDetails.setAttribute("BALE_NO",resultSetTemp.getString("BALE_NO"));
                
                hmFeltExportInvoiceDetails.put(Integer.toString(serialNoCounter),ObjFeltExportInvoiceDetails);
            }
            resultSetTemp.close();
            statementTemp.close();
            return true;
        }catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean ShowHistory(String invoiceDate,String invoiceNo) {
        Ready=false;
        try {
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.FELT_EXPORT_INVOICE_HEADER_H WHERE INVOICE_DATE='"+ invoiceDate+"' AND INVOICE_NO='"+invoiceNo+"'");
            Ready=true;
            HistoryView=true;
            MoveFirst();
            return true;
        }catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public static HashMap getHistoryList(String invoiceDate, String invoiceNo) {
        HashMap hmHistoryList=new HashMap();
        Statement stTmp;
        ResultSet rsTmp;
        try {
            stTmp=connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp=stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_EXPORT_INVOICE_HEADER_H WHERE INVOICE_DATE='"+invoiceDate+"' AND INVOICE_NO='"+invoiceNo+"'");
            
            while(rsTmp.next()) {
                clsFeltExportInvoice ObjFeltExportInvoice=new clsFeltExportInvoice();
                
                ObjFeltExportInvoice.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjFeltExportInvoice.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjFeltExportInvoice.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjFeltExportInvoice.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjFeltExportInvoice.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                hmHistoryList.put(Integer.toString(hmHistoryList.size()+1),ObjFeltExportInvoice);
            }
            
            rsTmp.close();
            stTmp.close();
            return hmHistoryList;
        }catch(Exception e) {
            e.printStackTrace();
            return hmHistoryList;
        }
    }
    
    public static HashMap getPendingApprovals(int pUserID,int pOrder) {
        String strSQL="";
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            
            if(pOrder==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT DOC_DATE,INVOICE_NO,RECEIVED_DATE FROM PRODUCTION.FELT_EXPORT_INVOICE_HEADER H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=713 AND CANCELED=0 ORDER BY RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT DOC_DATE,INVOICE_NO,RECEIVED_DATE FROM PRODUCTION.FELT_EXPORT_INVOICE_HEADER H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=713 AND CANCELED=0 ORDER BY DOC_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT DOC_DATE,INVOICE_NO,RECEIVED_DATE FROM PRODUCTION.FELT_EXPORT_INVOICE_HEADER H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=713 AND CANCELED=0 ORDER BY INVOICE_NO";
            }
            
            ResultSet rsTmp=data.getConn().createStatement().executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp.next()) {
                
                Counter=Counter+1;
                clsFeltExportInvoice ObjDoc=new clsFeltExportInvoice();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO",rsTmp.getString("INVOICE_NO"));
                ObjDoc.setAttribute("DOC_DATE",rsTmp.getString("DOC_DATE"));
                ObjDoc.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                // ----------------- End of Header Fields ------------------------------------//
                
                List.put(Long.toString(Counter),ObjDoc);
            }
            rsTmp.close();
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        
        return List;
    }
    
    public String getInvoiceNo(char invoiceType){
        String invoiceNo="F";
        int no=Integer.parseInt(data.getStringValueFromDB("SELECT MAX(SUBSTRING(INVOICE_NO,2,(LENGTH(INVOICE_NO)-1))) FROM PRODUCTION.FELT_EXPORT_INVOICE_HEADER WHERE SUBSTRING(INVOICE_NO,2,1)='"+invoiceType+"' AND  INVOICE_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND INVOICE_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'"));
        invoiceNo+=(no+1);
        return invoiceNo;
    }
    
    public String getDocumentNo(String invoiceNo){
        return "FEI"+ String.valueOf(new GregorianCalendar().get(Calendar.YEAR)).substring(2)+ invoiceNo.substring(1);
    }
    
    public String[] getPieceDetails(String pieceNo) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        String []pieceDetails = new String[3] ;
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
     //       rsTmp=Stmt.executeQuery("SELECT CONCAT(RCVD_MTR,'X',RECD_WDTH) SIZE, RECD_KG FROM PRODUCTION.FELT_PIECE_REGISTER WHERE PIECE_NO='"+pieceNo+"' AND REMARK='IN STOCK'");
            rsTmp=Stmt.executeQuery("SELECT CONCAT(RCVD_MTR,'X',RECD_WDTH) SIZE, RECD_KG FROM PRODUCTION.FELT_PIECE_REGISTER WHERE PIECE_NO='"+pieceNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                pieceDetails[0] = rsTmp.getString("SIZE");
                pieceDetails[1] = rsTmp.getString("RECD_KG");
            }
            
            Stmt.close();
            rsTmp.close();
            
            return pieceDetails;
        }
        catch(Exception e) {
            e.printStackTrace();
            return pieceDetails;
        }
    }
    
    // checks piece no already exist in db in add mode
    public boolean checkPieceNoInDB(String pieceNo){
        int count=data.getIntValueFromDB("SELECT COUNT(PIECE_NO) FROM PRODUCTION.FELT_EXPORT_INVOICE_DETAIL WHERE PIECE_NO='"+pieceNo+"' AND INVOICE_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND INVOICE_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
        if(count>0) return true;
        else return false;
    }
    
    // checks piece no already exist in db in edit mode
    public boolean checkPieceNoInDB(String pieceNo, String invoiceNo, String invoiceDate){
        int count=data.getIntValueFromDB("SELECT COUNT(PIECE_NO) FROM PRODUCTION.FELT_EXPORT_INVOICE_DETAIL WHERE PIECE_NO='"+pieceNo+"' AND INVOICE_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND INVOICE_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
        if(count>=1){
            int counter=0;
            try{
                Connection Conn=data.getConn();
                Statement stTmp=Conn.createStatement();
                ResultSet rsTmp=stTmp.executeQuery("SELECT INVOICE_DATE,INVOICE_NO FROM PRODUCTION.FELT_EXPORT_INVOICE_DETAIL WHERE PIECE_NO='"+pieceNo+"' AND INVOICE_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND INVOICE_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
                while(rsTmp.next()){
                    if(rsTmp.getString("INVOICE_DATE").equals(EITLERPGLOBAL.formatDateDB(invoiceDate)) && rsTmp.getString("INVOICE_NO").equals(invoiceNo)) counter++;
                }
            }catch(SQLException e){e.printStackTrace();}
            if(counter==1 && count>=2) return true;
            else if(counter==1) return false;
            else return true;
        }else return false;
    }
    
}
