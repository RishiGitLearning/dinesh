/*
 * clsFeltSalesReturns.java
 *
 * Created on 12, 12, 2016, 5:31 PM
 */

package EITLERP.FeltSales.SalesReturns;

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
import EITLERP.SelectFirstFree;
import EITLERP.clsFirstFree;

/**
 * @author Jadeja Rajpalsinh 
 */

public class clsFeltSalesReturns {
    
    public String LastError="";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private int FFNo=0;
  
    public HashMap props;
    public boolean Ready = false;
    public HashMap hmFeltGroupMasterDetails;
    
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
    
    /** Creates new DataFeltGroup */
    public clsFeltSalesReturns() {
        LastError = "";
        props=new HashMap();
        props.put("DOC_NO", new Variant(""));
        props.put("DOC_DATE", new Variant(""));
        props.put("GR_INWARD_NO", new Variant(""));
        props.put("GR_INWARD_DATE", new Variant(""));
        props.put("GR_CATEGORY", new Variant(""));
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
        
        hmFeltGroupMasterDetails=new HashMap();
    }
    
    public boolean LoadData() {
        Ready=false;
        try {
            connection = data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_RETURNS_HEADER ORDER BY DOC_NO");
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
            }else {
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
            }else {
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
            
            statementDetail = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetDetail = statementDetail.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_RETURNS_DETAIL WHERE DOC_NO=''");
            
           
            statementDetailHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetDetailHistory = statementDetailHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_RETURNS_DETAIL_H WHERE DOC_NO=''");
            
            
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_RETURNS_HEADER_H WHERE DOC_NO=''");
            
           
            setAttribute("DOC_NO",EITLERP.clsFirstFree.getNextFreeNo(2, 744, getAttribute("FFNO").getInt(), true));
            
           
//-------------------------------------------------
            
            resultSet.moveToInsertRow();
            resultSet.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSet.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            resultSet.updateString("GR_INWARD_NO", getAttribute("GR_INWARD_NO").getString());
            resultSet.updateString("GR_INWARD_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("GR_INWARD_DATE").getString()));
            resultSet.updateString("GR_CATEGORY", getAttribute("GR_CATEGORY").getString());
            resultSet.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
            resultSet.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
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
            resultSetHistory.updateString("ENTRY_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSetHistory.updateInt("UPDATED_BY", getAttribute("UPDATED_BY").getInt());
            resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSetHistory.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            resultSetHistory.updateString("GR_INWARD_NO", getAttribute("GR_INWARD_NO").getString());
            resultSetHistory.updateString("GR_INWARD_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("GR_INWARD_DATE").getString()));
            resultSetHistory.updateString("GR_CATEGORY", getAttribute("GR_CATEGORY").getString());
            resultSetHistory.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
            resultSetHistory.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSetHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            resultSetHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
            resultSetHistory.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            resultSetHistory.updateInt("CHANGED",1);
            resultSetHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            ResultSet rsTmp=data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");
            
            resultSetHistory.updateString("FROM_IP",""+str_split[1]);
            resultSetHistory.insertRow();
            
            for(int i=1;i<=hmFeltGroupMasterDetails.size();i++) {
                clsFeltSalesReturnsDetails ObjFeltGroupMasterDetails=(clsFeltSalesReturnsDetails) hmFeltGroupMasterDetails.get(Integer.toString(i));
                
                //Insert records into Group detail table
                resultSetDetail.moveToInsertRow();
                resultSetDetail.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetDetail.updateString("PIECE_NO",ObjFeltGroupMasterDetails.getAttribute("PIECE_NO").getString());
                resultSetDetail.updateString("INVOICE_NO",ObjFeltGroupMasterDetails.getAttribute("INVOICE_NO").getString());
                resultSetDetail.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(ObjFeltGroupMasterDetails.getAttribute("INVOICE_DATE").getString()));
                resultSetDetail.updateString("QUALITY_NO",ObjFeltGroupMasterDetails.getAttribute("QUALITY_NO").getString());
                resultSetDetail.updateString("PARTY_CODE",ObjFeltGroupMasterDetails.getAttribute("PARTY_CODE").getString());
                resultSetDetail.updateString("PARTY_NAME",ObjFeltGroupMasterDetails.getAttribute("PARTY_NAME").getString());
                resultSetDetail.updateString("LENGTH",ObjFeltGroupMasterDetails.getAttribute("LENGTH").getString());
                resultSetDetail.updateString("WIDTH",ObjFeltGroupMasterDetails.getAttribute("WIDTH").getString());
                resultSetDetail.updateString("ACTUAL_WEIGHT",ObjFeltGroupMasterDetails.getAttribute("ACTUAL_WEIGHT").getString());
                resultSetDetail.updateString("TOTAL_GROSS",ObjFeltGroupMasterDetails.getAttribute("TOTAL_GROSS").getString());
                resultSetDetail.updateString("TOTAL_NET_AMOUNT",ObjFeltGroupMasterDetails.getAttribute("TOTAL_NET_AMOUNT").getString());
                resultSetDetail.updateString("GROSS_SQ_MTR",ObjFeltGroupMasterDetails.getAttribute("GROSS_SQ_MTR").getString());
                resultSetDetail.updateString("GROSS_KG",ObjFeltGroupMasterDetails.getAttribute("GROSS_KG").getString());
                resultSetDetail.updateString("GROSS_AMOUNT",ObjFeltGroupMasterDetails.getAttribute("GROSS_AMOUNT").getString());
                resultSetDetail.updateString("TRD_DISCOUNT",ObjFeltGroupMasterDetails.getAttribute("TRD_DISCOUNT").getString());
                resultSetDetail.updateString("NET_AMOUNT",ObjFeltGroupMasterDetails.getAttribute("NET_AMOUNT").getString());
                resultSetDetail.updateString("REMARKS",ObjFeltGroupMasterDetails.getAttribute("REMARKS").getString());
                resultSetDetail.updateString("LR_NO",ObjFeltGroupMasterDetails.getAttribute("LR_NO").getString());
                resultSetDetail.updateString("NEW_PIECE_NO",ObjFeltGroupMasterDetails.getAttribute("NEW_PIECE_NO").getString());
                resultSetDetail.updateString("RETURN_CATEGORY",ObjFeltGroupMasterDetails.getAttribute("RETURN_CATEGORY").getString());
                resultSetDetail.updateString("OC_MONTH",ObjFeltGroupMasterDetails.getAttribute("OC_MONTH").getString());
                resultSetDetail.updateString("OBSOLETE_UPN_ASSIGN_STATUS",ObjFeltGroupMasterDetails.getAttribute("OBSOLETE_UPN_ASSIGN_STATUS").getString());
                resultSetDetail.updateString("SCRAP_REASON",ObjFeltGroupMasterDetails.getAttribute("SCRAP_REASON").getString());
                resultSetDetail.updateString("UNMAPPED_REASON",ObjFeltGroupMasterDetails.getAttribute("UNMAPPED_REASON").getString());
                resultSetDetail.updateString("CATEGORY_CONDITION",ObjFeltGroupMasterDetails.getAttribute("CATEGORY_CONDITION").getString());
                resultSetDetail.updateString("CONDITION_STATUS",ObjFeltGroupMasterDetails.getAttribute("CONDITION_STATUS").getString());
                resultSetDetail.updateInt("CREATED_BY",getAttribute("CREATED_BY").getInt());
                resultSetDetail.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetail.updateInt("CANCELED",0);
                resultSetDetail.updateInt("CHANGED",1);
                resultSetDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetail.insertRow();
                
                //Insert records into Group detail history table
                resultSetDetailHistory.moveToInsertRow();
                resultSetDetailHistory.updateInt("REVISION_NO",1);
                resultSetDetailHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetDetailHistory.updateString("PIECE_NO",ObjFeltGroupMasterDetails.getAttribute("PIECE_NO").getString());
                resultSetDetailHistory.updateString("INVOICE_NO",ObjFeltGroupMasterDetails.getAttribute("INVOICE_NO").getString());
                resultSetDetailHistory.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(ObjFeltGroupMasterDetails.getAttribute("INVOICE_DATE").getString()));
                resultSetDetailHistory.updateString("QUALITY_NO",ObjFeltGroupMasterDetails.getAttribute("QUALITY_NO").getString());
                resultSetDetailHistory.updateString("PARTY_CODE",ObjFeltGroupMasterDetails.getAttribute("PARTY_CODE").getString());
                resultSetDetailHistory.updateString("PARTY_NAME",ObjFeltGroupMasterDetails.getAttribute("PARTY_NAME").getString());
                resultSetDetailHistory.updateString("LENGTH",ObjFeltGroupMasterDetails.getAttribute("LENGTH").getString());
                resultSetDetailHistory.updateString("WIDTH",ObjFeltGroupMasterDetails.getAttribute("WIDTH").getString());
                resultSetDetailHistory.updateString("ACTUAL_WEIGHT",ObjFeltGroupMasterDetails.getAttribute("ACTUAL_WEIGHT").getString());
                resultSetDetailHistory.updateString("TOTAL_GROSS",ObjFeltGroupMasterDetails.getAttribute("TOTAL_GROSS").getString());
                resultSetDetailHistory.updateString("TOTAL_NET_AMOUNT",ObjFeltGroupMasterDetails.getAttribute("TOTAL_NET_AMOUNT").getString());
                resultSetDetailHistory.updateString("GROSS_SQ_MTR",ObjFeltGroupMasterDetails.getAttribute("GROSS_SQ_MTR").getString());
                resultSetDetailHistory.updateString("GROSS_KG",ObjFeltGroupMasterDetails.getAttribute("GROSS_KG").getString());
                resultSetDetailHistory.updateString("GROSS_AMOUNT",ObjFeltGroupMasterDetails.getAttribute("GROSS_AMOUNT").getString());
                resultSetDetailHistory.updateString("TRD_DISCOUNT",ObjFeltGroupMasterDetails.getAttribute("TRD_DISCOUNT").getString());
                resultSetDetailHistory.updateString("NET_AMOUNT",ObjFeltGroupMasterDetails.getAttribute("NET_AMOUNT").getString());
                resultSetDetailHistory.updateString("REMARKS",ObjFeltGroupMasterDetails.getAttribute("REMARKS").getString());
                resultSetDetailHistory.updateString("LR_NO",ObjFeltGroupMasterDetails.getAttribute("LR_NO").getString());
                resultSetDetailHistory.updateString("NEW_PIECE_NO",ObjFeltGroupMasterDetails.getAttribute("NEW_PIECE_NO").getString());
                resultSetDetailHistory.updateString("RETURN_CATEGORY",ObjFeltGroupMasterDetails.getAttribute("RETURN_CATEGORY").getString());
                resultSetDetailHistory.updateString("OC_MONTH",ObjFeltGroupMasterDetails.getAttribute("OC_MONTH").getString());
                resultSetDetailHistory.updateString("OBSOLETE_UPN_ASSIGN_STATUS",ObjFeltGroupMasterDetails.getAttribute("OBSOLETE_UPN_ASSIGN_STATUS").getString());
                resultSetDetailHistory.updateString("SCRAP_REASON",ObjFeltGroupMasterDetails.getAttribute("SCRAP_REASON").getString());
                resultSetDetailHistory.updateString("UNMAPPED_REASON",ObjFeltGroupMasterDetails.getAttribute("UNMAPPED_REASON").getString());
                resultSetDetailHistory.updateString("CATEGORY_CONDITION",ObjFeltGroupMasterDetails.getAttribute("CATEGORY_CONDITION").getString());
                resultSetDetailHistory.updateString("CONDITION_STATUS",ObjFeltGroupMasterDetails.getAttribute("CONDITION_STATUS").getString());
                resultSetDetailHistory.updateInt("CREATED_BY",getAttribute("CREATED_BY").getInt());
                resultSetDetailHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetailHistory.updateInt("CHANGED",1);
                resultSetDetailHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetailHistory.insertRow();
            }
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=744; //Felt Group
            ObjFeltProductionApprovalFlow.DocNo=getDocumentNo(getAttribute("DOC_NO").getString());
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName="FELT_SALES_RETURNS_HEADER";
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
            // Group detail connection
            statementDetail = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetDetail = statementDetail.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_RETURNS_DETAIL WHERE DOC_NO=''");
            
            // Group detail history connection
            statementDetailHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetDetailHistory = statementDetailHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_RETURNS_DETAIL_H WHERE DOC_NO=''");
            
            // Group data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_RETURNS_HEADER_H WHERE DOC_NO=''");
            
            resultSet.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSet.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            resultSet.updateString("GR_INWARD_NO", getAttribute("GR_INWARD_NO").getString());
            resultSet.updateString("GR_INWARD_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("GR_INWARD_DATE").getString()));
            resultSet.updateString("GR_CATEGORY", getAttribute("GR_CATEGORY").getString());
            resultSet.updateInt("MODIFIED_BY",getAttribute("MODIFIED_BY").getInt());
            resultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            resultSet.updateInt("CHANGED",1);
            resultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int revisionNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_SALES_RETURNS_HEADER_H WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
            revisionNo++;
            
            resultSetHistory.moveToInsertRow();
            resultSetHistory.updateInt("REVISION_NO",revisionNo);
            resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSetHistory.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            resultSetHistory.updateString("GR_INWARD_NO", getAttribute("GR_INWARD_NO").getString());
            resultSetHistory.updateString("GR_INWARD_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("GR_INWARD_DATE").getString()));
            resultSetHistory.updateString("GR_CATEGORY", getAttribute("GR_CATEGORY").getString());
            resultSetHistory.updateInt("UPDATED_BY",getAttribute("UPDATED_BY").getInt());
            resultSetHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            resultSetHistory.updateString("APPROVER_REMARKS",getAttribute("APPROVER_REMARKS").getString());
            resultSetHistory.updateInt("MODIFIED_BY",getAttribute("MODIFIED_BY").getInt());
            resultSetHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            resultSetHistory.updateString("ENTRY_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSetHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            resultSetHistory.updateInt("CHANGED",1);
            resultSetHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            
            ResultSet rsTmp=data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");
            
            resultSetHistory.updateString("FROM_IP",""+str_split[1]);
            
            resultSetHistory.insertRow();
            
            
            //delete records from Group detail table before insert
            data.Execute("DELETE FROM PRODUCTION.FELT_SALES_RETURNS_DETAIL WHERE  DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
            
            //Now insert records into Group detail tables
            for(int i=1;i<=hmFeltGroupMasterDetails.size();i++) {
                clsFeltSalesReturnsDetails ObjFeltGroupMasterDetails=(clsFeltSalesReturnsDetails) hmFeltGroupMasterDetails.get(Integer.toString(i));
                
                //Insert records into Group detail table
                resultSetDetail.moveToInsertRow();
                resultSetDetail.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetDetail.updateString("PIECE_NO",ObjFeltGroupMasterDetails.getAttribute("PIECE_NO").getString());
                resultSetDetail.updateString("INVOICE_NO",ObjFeltGroupMasterDetails.getAttribute("INVOICE_NO").getString());
                resultSetDetail.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(ObjFeltGroupMasterDetails.getAttribute("INVOICE_DATE").getString()));
                resultSetDetail.updateString("QUALITY_NO",ObjFeltGroupMasterDetails.getAttribute("QUALITY_NO").getString());
                resultSetDetail.updateString("PARTY_CODE",ObjFeltGroupMasterDetails.getAttribute("PARTY_CODE").getString());
                resultSetDetail.updateString("PARTY_NAME",ObjFeltGroupMasterDetails.getAttribute("PARTY_NAME").getString());
                resultSetDetail.updateString("LENGTH",ObjFeltGroupMasterDetails.getAttribute("LENGTH").getString());
                resultSetDetail.updateString("WIDTH",ObjFeltGroupMasterDetails.getAttribute("WIDTH").getString());
                resultSetDetail.updateString("ACTUAL_WEIGHT",ObjFeltGroupMasterDetails.getAttribute("ACTUAL_WEIGHT").getString());
                resultSetDetail.updateString("TOTAL_GROSS",ObjFeltGroupMasterDetails.getAttribute("TOTAL_GROSS").getString());
                resultSetDetail.updateString("TOTAL_NET_AMOUNT",ObjFeltGroupMasterDetails.getAttribute("TOTAL_NET_AMOUNT").getString());
                resultSetDetail.updateString("GROSS_SQ_MTR",ObjFeltGroupMasterDetails.getAttribute("GROSS_SQ_MTR").getString());
                resultSetDetail.updateString("GROSS_KG",ObjFeltGroupMasterDetails.getAttribute("GROSS_KG").getString());
                resultSetDetail.updateString("GROSS_AMOUNT",ObjFeltGroupMasterDetails.getAttribute("GROSS_AMOUNT").getString());
                resultSetDetail.updateString("TRD_DISCOUNT",ObjFeltGroupMasterDetails.getAttribute("TRD_DISCOUNT").getString());
                resultSetDetail.updateString("NET_AMOUNT",ObjFeltGroupMasterDetails.getAttribute("NET_AMOUNT").getString());
                resultSetDetail.updateString("REMARKS",ObjFeltGroupMasterDetails.getAttribute("REMARKS").getString());
                resultSetDetail.updateString("LR_NO",ObjFeltGroupMasterDetails.getAttribute("LR_NO").getString());
                resultSetDetail.updateString("NEW_PIECE_NO",ObjFeltGroupMasterDetails.getAttribute("NEW_PIECE_NO").getString());
                resultSetDetail.updateString("RETURN_CATEGORY",ObjFeltGroupMasterDetails.getAttribute("RETURN_CATEGORY").getString());
                resultSetDetail.updateString("OC_MONTH",ObjFeltGroupMasterDetails.getAttribute("OC_MONTH").getString());
                resultSetDetail.updateString("OBSOLETE_UPN_ASSIGN_STATUS",ObjFeltGroupMasterDetails.getAttribute("OBSOLETE_UPN_ASSIGN_STATUS").getString());
                resultSetDetail.updateString("SCRAP_REASON",ObjFeltGroupMasterDetails.getAttribute("SCRAP_REASON").getString());
                resultSetDetail.updateString("UNMAPPED_REASON",ObjFeltGroupMasterDetails.getAttribute("UNMAPPED_REASON").getString());
                resultSetDetail.updateString("CATEGORY_CONDITION",ObjFeltGroupMasterDetails.getAttribute("CATEGORY_CONDITION").getString());
                resultSetDetail.updateString("CONDITION_STATUS",ObjFeltGroupMasterDetails.getAttribute("CONDITION_STATUS").getString());
                resultSetDetail.updateInt("MODIFIED_BY",getAttribute("MODIFIED_BY").getInt());
                resultSetDetail.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetail.updateInt("CANCELED",0);
                resultSetDetail.updateInt("CHANGED",1);
                resultSetDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetail.insertRow();
                
                //Insert records into Group detail history table
                resultSetDetailHistory.moveToInsertRow();
                resultSetDetailHistory.updateInt("REVISION_NO",revisionNo);
                resultSetDetailHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetDetailHistory.updateString("PIECE_NO",ObjFeltGroupMasterDetails.getAttribute("PIECE_NO").getString());
                resultSetDetailHistory.updateString("INVOICE_NO",ObjFeltGroupMasterDetails.getAttribute("INVOICE_NO").getString());
                resultSetDetailHistory.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(ObjFeltGroupMasterDetails.getAttribute("INVOICE_DATE").getString()));
                resultSetDetailHistory.updateString("QUALITY_NO",ObjFeltGroupMasterDetails.getAttribute("QUALITY_NO").getString());
                resultSetDetailHistory.updateString("PARTY_CODE",ObjFeltGroupMasterDetails.getAttribute("PARTY_CODE").getString());
                resultSetDetailHistory.updateString("PARTY_NAME",ObjFeltGroupMasterDetails.getAttribute("PARTY_NAME").getString());
                resultSetDetailHistory.updateString("ACTUAL_WEIGHT",ObjFeltGroupMasterDetails.getAttribute("ACTUAL_WEIGHT").getString());
                resultSetDetailHistory.updateString("LENGTH",ObjFeltGroupMasterDetails.getAttribute("LENGTH").getString());
                resultSetDetailHistory.updateString("WIDTH",ObjFeltGroupMasterDetails.getAttribute("WIDTH").getString());
                resultSetDetailHistory.updateString("TOTAL_GROSS",ObjFeltGroupMasterDetails.getAttribute("TOTAL_GROSS").getString());
                resultSetDetailHistory.updateString("TOTAL_NET_AMOUNT",ObjFeltGroupMasterDetails.getAttribute("TOTAL_NET_AMOUNT").getString());
                resultSetDetailHistory.updateString("GROSS_SQ_MTR",ObjFeltGroupMasterDetails.getAttribute("GROSS_SQ_MTR").getString());
                resultSetDetailHistory.updateString("GROSS_KG",ObjFeltGroupMasterDetails.getAttribute("GROSS_KG").getString());
                resultSetDetailHistory.updateString("GROSS_AMOUNT",ObjFeltGroupMasterDetails.getAttribute("GROSS_AMOUNT").getString());
                resultSetDetailHistory.updateString("TRD_DISCOUNT",ObjFeltGroupMasterDetails.getAttribute("TRD_DISCOUNT").getString());
                resultSetDetailHistory.updateString("NET_AMOUNT",ObjFeltGroupMasterDetails.getAttribute("NET_AMOUNT").getString());
                resultSetDetailHistory.updateString("REMARKS",ObjFeltGroupMasterDetails.getAttribute("REMARKS").getString());
                resultSetDetailHistory.updateString("LR_NO",ObjFeltGroupMasterDetails.getAttribute("LR_NO").getString());
                resultSetDetailHistory.updateString("NEW_PIECE_NO",ObjFeltGroupMasterDetails.getAttribute("NEW_PIECE_NO").getString());
                resultSetDetailHistory.updateString("RETURN_CATEGORY",ObjFeltGroupMasterDetails.getAttribute("RETURN_CATEGORY").getString());
                resultSetDetailHistory.updateString("OC_MONTH",ObjFeltGroupMasterDetails.getAttribute("OC_MONTH").getString());
                resultSetDetailHistory.updateString("OBSOLETE_UPN_ASSIGN_STATUS",ObjFeltGroupMasterDetails.getAttribute("OBSOLETE_UPN_ASSIGN_STATUS").getString());
                resultSetDetailHistory.updateString("SCRAP_REASON",ObjFeltGroupMasterDetails.getAttribute("SCRAP_REASON").getString());
                resultSetDetailHistory.updateString("UNMAPPED_REASON",ObjFeltGroupMasterDetails.getAttribute("UNMAPPED_REASON").getString());
                resultSetDetailHistory.updateString("CATEGORY_CONDITION",ObjFeltGroupMasterDetails.getAttribute("CATEGORY_CONDITION").getString());
                resultSetDetailHistory.updateString("CONDITION_STATUS",ObjFeltGroupMasterDetails.getAttribute("CONDITION_STATUS").getString());
                resultSetDetailHistory.updateInt("UPDATED_BY",getAttribute("UPDATED_BY").getInt());
                resultSetDetailHistory.updateInt("CHANGED",1);
                resultSetDetailHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetailHistory.insertRow();
            }
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=744; //Felt Group
            ObjFeltProductionApprovalFlow.DocNo=getDocumentNo(getAttribute("DOC_NO").getString());
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.FELT_SALES_RETURNS_HEADER";
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
                data.Execute("UPDATE PRODUCTION.FELT_SALES_RETURNS_DETAIL A,PRODUCTION.FELT_SALES_PIECE_REGISTER B SET B.PR_PIECE_NO=A.PIECE_NO,B.PR_PIECE_STAGE='IN STOCK',B.PR_PKG_DP_NO='',B.PR_PKG_DATE='0000-00-00' WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"' AND B.PR_PIECE_NO=A.PIECE_NO ");
                
               
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
    
    /*
     * This routine checks whether the item is deletable or not.
     * Criteria is Approved item cannot be delete,
     * and if not approved then user id is checked whether doucment
     * is created by the user. Only creator can delete the document.
     * After checking it deletes the record of selected production date and document no.
     */
    public boolean CanDelete(String baleNo,int userID) {
        if(HistoryView) {
            return false;
        }
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            tmpStmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_SALES_RETURNS_HEADER WHERE DOC_NO='"+baleNo +"' AND  APPROVED=1";
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if(rsTmp.getInt("COUNT")>0){  //Group is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            }else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=744 AND USER_ID="+userID+" AND DOC_NO='"+ getAttribute("DOC_NO").getString()+"' AND STATUS='W'";
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getInt("COUNT")>0) {
                    strSQL = "DELETE FROM PRODUCTION.FELT_SALES_RETURNS_HEADER WHERE DOC_NO='"+baleNo+"'";
                    tmpStmt.executeUpdate(strSQL);
                    strSQL = "DELETE FROM PRODUCTION.FELT_SALES_RETURNS_DETAIL WHERE DOC_NO='"+baleNo+"'";
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
    
    /*
     * This routine checks and returns whether the item is editable or not.
     * Criteria is Approved item cannot be changed and if not approved then user id is
     * checked whether doucment is waiting for his approval.
     */
    public boolean IsEditable(String baleNo,int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        try {
            if(HistoryView) {
                return false;
            }
            tmpStmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_SALES_RETURNS_HEADER WHERE DOC_NO='"+ baleNo +"' AND APPROVED=1";
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0){  //Item is Approved
                //Discard the request
                return false;
            }else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=744 AND USER_ID="+userID+" AND DOC_NO='"+baleNo+"' AND STATUS='W'";
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
            String strSql = "SELECT * FROM PRODUCTION.FELT_SALES_RETURNS_HEADER WHERE 1=1 " + stringFindQuery + " GROUP BY DOC_NO ORDER BY DOC_NO ";
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
            
            setAttribute("DOC_NO",resultSet.getString("DOC_NO"));
            setAttribute("DOC_DATE",resultSet.getString("DOC_DATE"));
            setAttribute("GR_INWARD_NO",resultSet.getString("GR_INWARD_NO"));
            setAttribute("GR_INWARD_DATE",resultSet.getString("GR_INWARD_DATE"));
            setAttribute("GR_CATEGORY",resultSet.getString("GR_CATEGORY"));
            setAttribute("HIERARCHY_ID",resultSet.getInt("HIERARCHY_ID"));
            
            //Now Populate the collection, first clear the collection
            hmFeltGroupMasterDetails.clear();
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            if(HistoryView) {
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_RETURNS_DETAIL_H WHERE DOC_NO='"+ resultSet.getString("DOC_NO") +"'  AND REVISION_NO="+RevNo);
            }else {
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_RETURNS_DETAIL WHERE DOC_NO='"+ resultSet.getString("DOC_NO") +"' ");
            }
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
                clsFeltSalesReturnsDetails ObjFeltGroupMasterDetails = new clsFeltSalesReturnsDetails();
                
                ObjFeltGroupMasterDetails.setAttribute("DOC_NO",resultSet.getString("DOC_NO"));
                ObjFeltGroupMasterDetails.setAttribute("PIECE_NO",resultSetTemp.getString("PIECE_NO"));
                ObjFeltGroupMasterDetails.setAttribute("INVOICE_NO",resultSetTemp.getString("INVOICE_NO"));
                ObjFeltGroupMasterDetails.setAttribute("INVOICE_DATE",resultSetTemp.getString("INVOICE_DATE"));
                ObjFeltGroupMasterDetails.setAttribute("PARTY_CODE",resultSetTemp.getString("PARTY_CODE"));
                ObjFeltGroupMasterDetails.setAttribute("PARTY_NAME",resultSetTemp.getString("PARTY_NAME"));
                ObjFeltGroupMasterDetails.setAttribute("QUALITY_NO",resultSetTemp.getString("QUALITY_NO"));
                ObjFeltGroupMasterDetails.setAttribute("LENGTH",resultSetTemp.getString("LENGTH"));
                ObjFeltGroupMasterDetails.setAttribute("WIDTH",resultSetTemp.getString("WIDTH"));
                ObjFeltGroupMasterDetails.setAttribute("ACTUAL_WEIGHT",resultSetTemp.getString("ACTUAL_WEIGHT"));
                ObjFeltGroupMasterDetails.setAttribute("TOTAL_GROSS",resultSetTemp.getString("TOTAL_GROSS"));
                ObjFeltGroupMasterDetails.setAttribute("TOTAL_NET_AMOUNT",resultSetTemp.getString("TOTAL_NET_AMOUNT"));
                ObjFeltGroupMasterDetails.setAttribute("GROSS_SQ_MTR",resultSetTemp.getString("GROSS_SQ_MTR"));
                ObjFeltGroupMasterDetails.setAttribute("GROSS_KG",resultSetTemp.getString("GROSS_KG"));
                ObjFeltGroupMasterDetails.setAttribute("GROSS_AMOUNT",resultSetTemp.getString("GROSS_AMOUNT"));
                ObjFeltGroupMasterDetails.setAttribute("TRD_DISCOUNT",resultSetTemp.getString("TRD_DISCOUNT"));
                ObjFeltGroupMasterDetails.setAttribute("NET_AMOUNT",resultSetTemp.getString("NET_AMOUNT"));
                ObjFeltGroupMasterDetails.setAttribute("REMARKS",resultSetTemp.getString("REMARKS"));
                ObjFeltGroupMasterDetails.setAttribute("LR_NO",resultSetTemp.getString("LR_NO"));
                ObjFeltGroupMasterDetails.setAttribute("NEW_PIECE_NO",resultSetTemp.getString("NEW_PIECE_NO"));
                ObjFeltGroupMasterDetails.setAttribute("RETURN_CATEGORY",resultSetTemp.getString("RETURN_CATEGORY"));
                ObjFeltGroupMasterDetails.setAttribute("OC_MONTH",resultSetTemp.getString("OC_MONTH"));
                ObjFeltGroupMasterDetails.setAttribute("OBSOLETE_UPN_ASSIGN_STATUS",resultSetTemp.getString("OBSOLETE_UPN_ASSIGN_STATUS"));
                ObjFeltGroupMasterDetails.setAttribute("SCRAP_REASON",resultSetTemp.getString("SCRAP_REASON"));
                ObjFeltGroupMasterDetails.setAttribute("UNMAPPED_REASON",resultSetTemp.getString("UNMAPPED_REASON"));
                ObjFeltGroupMasterDetails.setAttribute("CATEGORY_CONDITION",resultSetTemp.getString("CATEGORY_CONDITION"));
                ObjFeltGroupMasterDetails.setAttribute("CONDITION_STATUS",resultSetTemp.getString("CONDITION_STATUS"));                
                
                hmFeltGroupMasterDetails.put(Integer.toString(serialNoCounter),ObjFeltGroupMasterDetails);
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
    
    public boolean ShowHistory(String baleDate,String baleNo) {
        Ready=false;
        try {
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_RETURNS_HEADER_H WHERE DOC_NO='"+baleNo+"'");
            Ready=true;
            HistoryView=true;
            MoveFirst();
            return true;
        }catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public static HashMap getHistoryList(String dpDate, String dpNo) {
        HashMap hmHistoryList=new HashMap();
        Statement stTmp;
        ResultSet rsTmp;
        try {
            stTmp=connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp=stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_RETURNS_HEADER_H WHERE  DOC_NO='"+dpNo+"'");
            
            while(rsTmp.next()) {
                clsFeltSalesReturns ObjFeltGroup=new clsFeltSalesReturns();
                
                ObjFeltGroup.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjFeltGroup.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjFeltGroup.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjFeltGroup.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjFeltGroup.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                ObjFeltGroup.setAttribute("FROM_IP",rsTmp.getString("FROM_IP"));
                
                
                
                hmHistoryList.put(Integer.toString(hmHistoryList.size()+1),ObjFeltGroup);
            }
            rsTmp.close();
            stTmp.close();
            return hmHistoryList;
        }catch(Exception e) {
            e.printStackTrace();
            return hmHistoryList;
        }
    }
    
    // Find Pending DOCUMENTS
    public static HashMap getPendingApprovals(int pUserID,int pOrder) {
        String strSQL="";
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            
            if(pOrder==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT H.DOC_NO,H.DOC_DATE,D.RECEIVED_DATE FROM PRODUCTION.FELT_SALES_RETURNS_HEADER H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=744 AND CANCELED=0 ORDER BY RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT H.DOC_NO,H.DOC_DATE,D.RECEIVED_DATE FROM PRODUCTION.FELT_SALES_RETURNS_HEADER H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=744 AND CANCELED=0 ORDER BY DOC_NO";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT H.DOC_NO,H.DOC_DATE,D.RECEIVED_DATE FROM PRODUCTION.FELT_SALES_RETURNS_HEADER H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=744 AND CANCELED=0 ORDER BY DOC_NO";
            }
            
            ResultSet rsTmp=data.getConn().createStatement().executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp.next()) {
                
                Counter=Counter+1;
                clsFeltSalesReturns ObjDoc=new clsFeltSalesReturns();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
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
    
    public String getDocumentNo(String dpNo){
        return   dpNo;
    }
    
    public String[] getPieceDetails(String pieceNo) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        String []pieceDetails = new String[6];
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT RCVD_MTR, RECD_WDTH, RECD_KG,STYLE,GSQ,SYN_PER FROM (SELECT PIECE_NO,RCVD_MTR, RECD_WDTH, RECD_KG, REPLACE(STYLE,' ','') STYLE,PRODUCT_CD FROM PRODUCTION.FELT_PIECE_REGISTER WHERE PIECE_NO+0='"+pieceNo+"' AND REMARK='IN STOCK') P LEFT JOIN (SELECT PIECE_NO,GSQ FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pieceNo+"') O ON P.PIECE_NO+0=O.PIECE_NO+0 LEFT JOIN (SELECT SYN_PER,ITEM_CODE FROM PRODUCTION.FELT_RATE_MASTER) R ON P.PRODUCT_CD=R.ITEM_CODE");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                pieceDetails[0] = rsTmp.getString("RCVD_MTR");
                pieceDetails[1] = rsTmp.getString("RECD_WDTH");
                pieceDetails[2] = rsTmp.getString("RECD_KG");
                pieceDetails[3] = rsTmp.getString("GSQ");
                pieceDetails[4] = rsTmp.getString("SYN_PER");
                pieceDetails[5] = rsTmp.getString("STYLE");
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
        int count=data.getIntValueFromDB("SELECT COUNT(PKG_PIECE_NO) FROM PRODUCTION.FELT_SALES_RETURNS_HEADER WHERE PKG_PIECE_NO='"+pieceNo+"' AND PKG_BALE_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PKG_BALE_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
        if(count>0) return true;
        else return false;
    }
    
    // checks piece no already exist in db in edit mode
    public boolean checkPieceNoInDB(String pieceNo, String baleNo, String baleDate){
        int count=data.getIntValueFromDB("SELECT COUNT(PKG_PIECE_NO) FROM PRODUCTION.FELT_SALES_RETURNS_HEADER WHERE PKG_PIECE_NO='"+pieceNo+"' AND PKG_BALE_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PKG_BALE_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
        if(count>=1){
            int counter=0;
            try{
                Connection Conn=data.getConn();
                Statement stTmp=Conn.createStatement();
                ResultSet rsTmp=stTmp.executeQuery("SELECT PKG_BALE_DATE,DOC_NO FROM PRODUCTION.FELT_SALES_RETURNS_HEADER WHERE PKG_PIECE_NO='"+pieceNo+"' AND PKG_BALE_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PKG_BALE_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
                while(rsTmp.next()){
                    if(rsTmp.getString("PKG_BALE_DATE").equals(EITLERPGLOBAL.formatDateDB(baleDate)) && rsTmp.getString("DOC_NO").equals(baleNo)) counter++;
                }
            }catch(SQLException e){e.printStackTrace();}
            if(counter==1 && count>=2) return true;
            else if(counter==1) return false;
            else return true;
        }else return false;
    }
    
    public static String getinvoiceno(String pcode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String partyname="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT INVOICE_NO FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PIECE_NO LIKE '%"+pcode+"%' AND APPROVED=1 AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                partyname=rsTmp.getString("INVOICE_NO");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return partyname;
        }
        catch(Exception e) {
            return "";
        }
    }
       
     public static String getinvoicedate(String pcode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String partyname="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT SUBSTRING(INVOICE_DATE,1,10) AS INVOICE_DATE FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PIECE_NO LIKE '%"+pcode+"%' AND APPROVED=1 AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                partyname=rsTmp.getString("INVOICE_DATE");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return partyname;
        }
        catch(Exception e) {
            return "";
        }
    }
    
      public static String getpartycode(String pcode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String partyname="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PARTY_CODE FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PIECE_NO LIKE '%"+pcode+"%' AND APPROVED=1 AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                partyname=rsTmp.getString("PARTY_CODE");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return partyname;
        }
        catch(Exception e) {
            return "";
        }
    }
    
      public static String getpartyname(String pcode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String partyname="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+pcode+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                partyname=rsTmp.getString("PARTY_NAME");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return partyname;
        }
        catch(Exception e) {
            return "";
        }
    }
      
      public static String getlength(String pcode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String partyname="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT LENGTH FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PIECE_NO LIKE '%"+pcode+"%' AND APPROVED=1 AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                partyname=rsTmp.getString("LENGTH");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return partyname;
        }
        catch(Exception e) {
            return "";
        }
    }
   
      public static String getwidth(String pcode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String partyname="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT WIDTH FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PIECE_NO LIKE '%"+pcode+"%' AND APPROVED=1 AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                partyname=rsTmp.getString("WIDTH");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return partyname;
        }
        catch(Exception e) {
            return "";
        }
    }
      public static String getweight(String pcode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String partyname="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT ACTUAL_WEIGHT FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PIECE_NO LIKE '%"+pcode+"%' AND APPROVED=1 AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                partyname=rsTmp.getString("ACTUAL_WEIGHT");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return partyname;
        }
        catch(Exception e) {
            return "";
        }
    }
      
      
      public static String getproductcode(String pcode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String partyname="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PRODUCT_CODE FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PIECE_NO LIKE '%"+pcode+"%' AND APPROVED=1 AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                partyname=rsTmp.getString("PRODUCT_CODE");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return partyname;
        }
        catch(Exception e) {
            return "";
        }
    }
      
      public static String getbaseamt(String pcode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String partyname="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT BAS_AMT FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PIECE_NO LIKE '%"+pcode+"%' AND APPROVED=1 AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                partyname=rsTmp.getString("BAS_AMT");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return partyname;
        }
        catch(Exception e) {
            return "";
        }
    }
      
      public static String gettotalnetamt(String pcode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String partyname="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT TOTAL_NET_AMOUNT FROM PRODUCTION.FELT_INVOICE_DATA WHERE PIECE_NO='"+pcode+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                partyname=rsTmp.getString("TOTAL_NET_AMOUNT");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return partyname;
        }
        catch(Exception e) {
            return "";
        }
    }
      public static String getgrosssqmtr(String pcode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String partyname="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT GROSS_SQ_MTR FROM PRODUCTION.FELT_INVOICE_DATA WHERE PIECE_NO='"+pcode+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                partyname=rsTmp.getString("GROSS_SQ_MTR");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return partyname;
        }
        catch(Exception e) {
            return "";
        }
    }
      public static String getinvoiceamt(String pcode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String partyname="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT INVOICE_AMT FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PARTY_CODE='"+pcode+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                partyname=rsTmp.getString("INVOICE_AMT");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return partyname;
        }
        catch(Exception e) {
            return "";
        }
    }
      public static String getgrossamt(String pcode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String partyname="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT GROSS_AMT FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PIECE_NO LIKE '%"+pcode+"%' AND APPROVED=1 AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                partyname=rsTmp.getString("GROSS_AMT");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return partyname;
        }
        catch(Exception e) {
            return "";
        }
    }
      public static String getdisamt(String pcode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String partyname="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT DISC_AMT FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PIECE_NO LIKE '%"+pcode+"%' AND APPROVED=1 AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                partyname=rsTmp.getString("DISC_AMT");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return partyname;
        }
        catch(Exception e) {
            return "";
        }
    }
      
      public static String getseamcharge(String pcode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String partyname="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT SEAM_CHG FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PIECE_NO LIKE '%"+pcode+"%' AND APPROVED=1 AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                partyname=rsTmp.getString("SEAM_CHG");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return partyname;
        }
        catch(Exception e) {
            return "";
        }
    }
      public static String gettaxes(String pcode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String partyname="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT (EXCISE+CST2+CST5+VAT1+VAT4+IGST_AMT+CGST_AMT+SGST_AMT) AS TAXES FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PIECE_NO LIKE '%"+pcode+"%' AND APPROVED=1 AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                partyname=rsTmp.getString("TAXES");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return partyname;
        }
        catch(Exception e) {
            return "";
        }
    }
      public static String getSDamt(String pcode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String partyname="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT SD_AMT FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PIECE_NO LIKE '%"+pcode+"%' AND APPROVED=1 AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                partyname=rsTmp.getString("SD_AMT");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return partyname;
        }
        catch(Exception e) {
            return "";
        }
    }
      public static String getnetamt(String pcode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String partyname="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT INVOICE_AMT FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PIECE_NO LIKE '%"+pcode+"%' AND APPROVED=1 AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                partyname=rsTmp.getString("INVOICE_AMT");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return partyname;
        }
        catch(Exception e) {
            return "";
        }
    }
   
   public static boolean CanCancel(String pDocNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean CanCancel=false;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            System.out.println("SELECT DOC_NO FROM PRODUCTION.FELT_SALES_RETURNS_HEADER WHERE DOC_NO='"+pDocNo+"'  AND APPROVED=0 AND CANCELED=0");
            rsTmp=stTmp.executeQuery("SELECT DOC_NO FROM PRODUCTION.FELT_SALES_RETURNS_HEADER WHERE DOC_NO='"+pDocNo+"'  AND APPROVED=0 AND CANCELED=0");
            
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                CanCancel=true;
            }
            
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return CanCancel;
    }
    
   public static void CancelDoc(String pDocNo) {
        ResultSet rsTmp=null;
        
        if(CanCancel(pDocNo)) {
            
            boolean Approved=false;
            //PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_STOCK_HEADER WHERE PIECE_AMEND_STOCK_NO
            try {
                rsTmp=data.getResult("SELECT APPROVED FROM PRODUCTION.FELT_SALES_RETURNS_HEADER WHERE DOC_NO='"+pDocNo+"' ");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    Approved=rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();
                
                if(!Approved) {
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='"+pDocNo+"' AND MODULE_ID=744");
                }
                data.Execute("UPDATE PRODUCTION.FELT_SALES_RETURNS_HEADER SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='"+pDocNo+"'");
                data.Execute("UPDATE PRODUCTION.FELT_SALES_RETURNS_DETAIL SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='"+pDocNo+"'");
              
            }
            catch(Exception e) {
                
            }
        }
        
    }
    
    
    
}
