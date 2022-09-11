/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.FeltPieceMerging;

import EITLERP.ComboData;
import EITLERP.EITLERPGLOBAL;
import EITLERP.Production.clsFeltProductionApprovalFlow;
import EITLERP.Variant;
import EITLERP.data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 *
 * @author  DAXESH PRAJAPATI
 *
 */
public class clsFeltPieceMerging {
    public String LastError="";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet,rsHeader;
    //private ResultSet rsResultSet,
    
    public HashMap props;
    public boolean Ready = false;
    //Felt Order Collection
    public HashMap hmFeltPieceMergingDetails;
    
    //History Related properties
    public boolean HistoryView=false;
    private String historyAmendDate="";
    private String historyAmendID="";
    private static int ModuleID = 854;
    
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
    public clsFeltPieceMerging() {
        LastError = "";
        props=new HashMap();
        
        
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("MODULE_ID",new Variant(""));
        props.put("USER_ID",new Variant(""));
        props.put("RECEIVED_DATE",new Variant(""));
        // -- Approval Specific Fields --
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REJECTED_REMARKS",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        props.put("APPROVER_REMARKS",new Variant(0));
        props.put("ENTRY_DATE",new Variant(0));
        
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("PIECE_NO",new Variant(""));
        props.put("NO_OF_DIVISION",new Variant(""));
        props.put("DIVISION_BY",new Variant(""));
        props.put("REMARK",new Variant(""));
        
        hmFeltPieceMergingDetails=new HashMap();
        
    }
    
    public boolean LoadData() {
        Ready=false;
        try {
            connection = data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.FELT_PIECE_MERGING_HEADER ORDER BY DOC_NO");
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
//            if(HistoryView) setHistoryData(historyAmendDate, historyAmendID);
//            else 
                
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
//            if(HistoryView) setHistoryData(historyAmendDate, historyAmendID);
//            else 
                
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
//            if(HistoryView) setHistoryData(historyAmendDate, historyAmendID);
//            else 
                
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
//            if(HistoryView) setHistoryData(historyAmendDate, historyAmendID);
//            else 
                
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
        ResultSet  resultSetTemp,resultSetHistory,rsHeader,rsHeaderH;
        Statement  statementTemp, statementHistory,stHeader,stHeaderH;
        try {
            // Felt Order Updation data connection
            connection = data.getConn();
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_PIECE_MERGING_DETAIL WHERE DOC_NO='1'");
            
            // Felt order Updation data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_PIECE_MERGING_DETAIL_H WHERE DOC_NO='1'");
            
            stHeader=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHeader=stHeader.executeQuery("SELECT * FROM  PRODUCTION.FELT_PIECE_MERGING_HEADER WHERE DOC_NO='1'");
            
            stHeaderH=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHeaderH=stHeaderH.executeQuery("SELECT * FROM  PRODUCTION.FELT_PIECE_MERGING_HEADER_H WHERE DOC_NO='1'");

            //stTMP=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsTMP=stTMP.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PERFORMANCE_TRACKING_SHEET_REGISTER WHERE PIECE_NO=''");
            
            //setAttribute("DOC_NO",);
                
            rsHeader.first();
            rsHeader.moveToInsertRow();
            
            rsHeader.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsHeader.updateString("DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
           
            rsHeader.updateString("UPN",getAttribute("UPN").getString());
            rsHeader.updateString("PIECE_NO",getAttribute("PIECE_NO").getString());
            rsHeader.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsHeader.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            rsHeader.updateString("LENGTH",getAttribute("LENGTH").getString());
            rsHeader.updateString("WIDTH",getAttribute("WIDTH").getString());
            rsHeader.updateString("GSM",getAttribute("GSM").getString());
            rsHeader.updateString("SQMTR",getAttribute("SQMTR").getString());
            rsHeader.updateString("WEIGHT",getAttribute("WEIGHT").getString());
            rsHeader.updateString("PRODUCT_CODE",getAttribute("PRODUCT_CODE").getString());
            rsHeader.updateString("PRODUCT_GROUP",getAttribute("PRODUCT_GROUP").getString());
            rsHeader.updateString("PRODUCT_STYLE",getAttribute("PRODUCT_STYLE").getString());
            rsHeader.updateString("EXISTING_PIECE_VALUE",getAttribute("EXISTING_PIECE_VALUE").getString());
            rsHeader.updateString("ORDER_UPN_VALUE",getAttribute("ORDER_UPN_VALUE").getString());
            rsHeader.updateString("PROFIT_LOSS",getAttribute("PROFIT_LOSS").getString());
            rsHeader.updateString("FRESH_BOOKING",getAttribute("FRESH_BOOKING").getString());
            rsHeader.updateString("REMARK",getAttribute("REMARK").getString());
            rsHeader.updateString("ORDER_REF_NO",getAttribute("ORDER_REF_NO").getString());
            rsHeader.updateString("ORDER_PIECE_NO",getAttribute("ORDER_PIECE_NO").getString());
            
            
            rsHeader.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHeader.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHeader.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHeader.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHeader.updateInt("MODIFIED_BY",0);
            rsHeader.updateString("MODIFIED_DATE","0000-00-00");
            rsHeader.updateBoolean("APPROVED",false);
            rsHeader.updateString("APPROVED_DATE","0000-00-00");
            rsHeader.updateBoolean("CANCELED",false);
            rsHeader.updateBoolean("REJECTED",false);
            rsHeader.updateString("REJECTED_DATE","0000-00-00");
            rsHeader.updateBoolean("CHANGED",false);
            rsHeader.updateString("CHANGED_DATE",EITLERPGLOBAL.formatDateDB("0000-00-00"));
            
            rsHeader.insertRow();
            
            rsHeaderH.first();
            rsHeaderH.moveToInsertRow();
            
            rsHeaderH.updateInt("REVISION_NO",1);
            
            rsHeaderH.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            
            rsHeaderH.updateString("DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
           
            rsHeaderH.updateString("UPN",getAttribute("UPN").getString());
            rsHeaderH.updateString("PIECE_NO",getAttribute("PIECE_NO").getString());
            rsHeaderH.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsHeaderH.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            rsHeaderH.updateString("LENGTH",getAttribute("LENGTH").getString());
            rsHeaderH.updateString("WIDTH",getAttribute("WIDTH").getString());
            rsHeaderH.updateString("GSM",getAttribute("GSM").getString());
            rsHeaderH.updateString("SQMTR",getAttribute("SQMTR").getString());
            rsHeaderH.updateString("WEIGHT",getAttribute("WEIGHT").getString());
            rsHeaderH.updateString("PRODUCT_CODE",getAttribute("PRODUCT_CODE").getString());
            rsHeaderH.updateString("PRODUCT_GROUP",getAttribute("PRODUCT_GROUP").getString());
            rsHeaderH.updateString("PRODUCT_STYLE",getAttribute("PRODUCT_STYLE").getString());
            rsHeaderH.updateString("EXISTING_PIECE_VALUE",getAttribute("EXISTING_PIECE_VALUE").getString());
            rsHeaderH.updateString("ORDER_UPN_VALUE",getAttribute("ORDER_UPN_VALUE").getString());
            rsHeaderH.updateString("PROFIT_LOSS",getAttribute("PROFIT_LOSS").getString());
            rsHeaderH.updateString("FRESH_BOOKING",getAttribute("FRESH_BOOKING").getString());
            rsHeaderH.updateString("REMARK",getAttribute("REMARK").getString());
            rsHeaderH.updateString("ORDER_REF_NO",getAttribute("ORDER_REF_NO").getString());
            rsHeaderH.updateString("ORDER_PIECE_NO",getAttribute("ORDER_PIECE_NO").getString());
            
            rsHeaderH.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHeaderH.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHeaderH.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateString("APPROVER_REMARKS",getAttribute("APPROVER_REMARKS").getString());
            rsHeaderH.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            
            
            rsHeaderH.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHeaderH.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            
            rsHeaderH.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            
            rsHeaderH.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHeaderH.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateInt("MODIFIED_BY",0);
            rsHeaderH.updateString("MODIFIED_DATE","0000-00-00");
            rsHeaderH.updateBoolean("APPROVED",false);
            rsHeaderH.updateString("APPROVED_DATE","0000-00-00");
            rsHeaderH.updateBoolean("CANCELED",false);
            rsHeaderH.updateBoolean("REJECTED",false);
            rsHeaderH.updateString("REJECTED_DATE","0000-00-00");
            rsHeaderH.updateBoolean("CHANGED",false);
            rsHeaderH.updateString("CHANGED_DATE","0000-00-00");
            
            ResultSet rsTmp=data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");
            
            rsHeaderH.updateString("FROM_IP",""+str_split[1]);
           
            rsHeaderH.insertRow(); 
            
            //Now Insert records into FELT_CN_TEMP_HEADER & History tables
            for(int i=1;i<=hmFeltPieceMergingDetails.size();i++) {
                clsFeltPieceMergingDetails ObjFeltSalesOrderDetails = (clsFeltPieceMergingDetails) hmFeltPieceMergingDetails.get(Integer.toString(i));
             
                //Insert records into Felt order Amendment table
                resultSetTemp.moveToInsertRow();
                
                resultSetTemp.updateString("SR_NO", ObjFeltSalesOrderDetails.getAttribute("SR_NO").getString());
                resultSetTemp.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                
                resultSetTemp.updateString("DOC_DATE", ObjFeltSalesOrderDetails.getAttribute("DOC_DATE").getString());
                resultSetTemp.updateString("PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                resultSetTemp.updateString("PARTY_CODE", ObjFeltSalesOrderDetails.getAttribute("PARTY_CODE").getString());
                resultSetTemp.updateString("PARTY_NAME", ObjFeltSalesOrderDetails.getAttribute("PARTY_NAME").getString());
                resultSetTemp.updateString("LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                resultSetTemp.updateString("WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                resultSetTemp.updateString("GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                resultSetTemp.updateString("SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQMTR").getString());
                resultSetTemp.updateString("WEIGHT", ObjFeltSalesOrderDetails.getAttribute("WEIGHT").getString());
                resultSetTemp.updateString("PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                resultSetTemp.updateString("PRODUCT_STYLE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_STYLE").getString());
                resultSetTemp.updateString("PRODUCT_GROUP", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_GROUP").getString());
                resultSetTemp.updateString("PIECE_STAGE", ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString());
                resultSetTemp.updateString("PIECE_STATUS", ObjFeltSalesOrderDetails.getAttribute("PIECE_STATUS").getString());
                
                resultSetTemp.updateString("MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                resultSetTemp.updateString("POSITION", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                resultSetTemp.updateString("POSITION_DESC", ObjFeltSalesOrderDetails.getAttribute("POSITION_DESC").getString());
                resultSetTemp.updateString("LAYER_TYPE", ObjFeltSalesOrderDetails.getAttribute("LAYER_TYPE").getString());
                
                
                resultSetTemp.insertRow();
                
                //Insert records into Felt Order Amendment History Table
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO",1);
                
                resultSetHistory.updateString("SR_NO", ObjFeltSalesOrderDetails.getAttribute("SR_NO").getString());
                resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                
                resultSetHistory.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
                resultSetHistory.updateString("PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                resultSetHistory.updateString("PARTY_CODE", ObjFeltSalesOrderDetails.getAttribute("PARTY_CODE").getString());
                resultSetHistory.updateString("PARTY_NAME", ObjFeltSalesOrderDetails.getAttribute("PARTY_NAME").getString());
                resultSetHistory.updateString("LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                resultSetHistory.updateString("WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                resultSetHistory.updateString("GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                resultSetHistory.updateString("SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQMTR").getString());
                resultSetHistory.updateString("WEIGHT", ObjFeltSalesOrderDetails.getAttribute("WEIGHT").getString());
                resultSetHistory.updateString("PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                resultSetHistory.updateString("PRODUCT_STYLE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_STYLE").getString());
                resultSetHistory.updateString("PRODUCT_GROUP", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_GROUP").getString());
                resultSetHistory.updateString("PIECE_STAGE", ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString());
                resultSetHistory.updateString("PIECE_STATUS", ObjFeltSalesOrderDetails.getAttribute("PIECE_STATUS").getString());
                
                resultSetHistory.updateString("MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                resultSetHistory.updateString("POSITION", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                resultSetHistory.updateString("POSITION_DESC", ObjFeltSalesOrderDetails.getAttribute("POSITION_DESC").getString());
                resultSetHistory.updateString("LAYER_TYPE", ObjFeltSalesOrderDetails.getAttribute("LAYER_TYPE").getString());
                
                resultSetHistory.insertRow();
            }
            
            
            
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=ModuleID; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo=getAttribute("DOC_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.FELT_PIECE_MERGING_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator=true;
            ObjFeltProductionApprovalFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks=getAttribute("REJECTED_REMARKS").getString();
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
            
                // Update  in Order Master Table To confirm that Weaving has completed
            if(ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {
      
            }
            
            
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
        
        ResultSet  resultSetTemp,resultSetHistory,rsHeader,rsHeaderH;
        Statement  statementTemp, statementHistory,stHeader,stHeaderH;
        int revisionNo;
        try {
            
            // Production data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            //Get the Maximum Revision No in History Table.
            resultSetHistory=statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_PIECE_MERGING_DETAIL_H WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
            resultSetHistory.first();
            if(resultSetHistory.getRow()>0){
                revisionNo=resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from production data table before insert
            //statementHistory.execute("DELETE FROM PRODUCTION.FELT_PIECE_MERGING_DETAIL WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
            
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_PIECE_MERGING_DETAIL_H WHERE DOC_NO=''");
            
            stHeaderH=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHeaderH=stHeaderH.executeQuery("SELECT * FROM  PRODUCTION.FELT_PIECE_MERGING_HEADER_H WHERE DOC_NO=''");
            
            
            //Now Update records into FELT_CN_TEMP_HEADER tables
            
            resultSet.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            
            resultSet.updateString("UPN",getAttribute("UPN").getString());
            resultSet.updateString("PIECE_NO",getAttribute("PIECE_NO").getString());
            resultSet.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            resultSet.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            resultSet.updateString("LENGTH",getAttribute("LENGTH").getString());
            resultSet.updateString("WIDTH",getAttribute("WIDTH").getString());
            resultSet.updateString("GSM",getAttribute("GSM").getString());
            resultSet.updateString("SQMTR",getAttribute("SQMTR").getString());
            resultSet.updateString("WEIGHT",getAttribute("WEIGHT").getString());
            resultSet.updateString("PRODUCT_CODE",getAttribute("PRODUCT_CODE").getString());
            resultSet.updateString("PRODUCT_GROUP",getAttribute("PRODUCT_GROUP").getString());
            resultSet.updateString("PRODUCT_STYLE",getAttribute("PRODUCT_STYLE").getString());
            resultSet.updateString("EXISTING_PIECE_VALUE",getAttribute("EXISTING_PIECE_VALUE").getString());
            resultSet.updateString("ORDER_UPN_VALUE",getAttribute("ORDER_UPN_VALUE").getString());
            resultSet.updateString("PROFIT_LOSS",getAttribute("PROFIT_LOSS").getString());
            resultSet.updateString("FRESH_BOOKING",getAttribute("FRESH_BOOKING").getString());
            resultSet.updateString("REMARK",getAttribute("REMARK").getString());
            resultSet.updateString("ORDER_REF_NO",getAttribute("ORDER_REF_NO").getString());
            resultSet.updateString("ORDER_PIECE_NO",getAttribute("ORDER_PIECE_NO").getString());
            
            resultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            resultSet.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            resultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            
                if(getAttribute("APPROVAL_STATUS").getString().equals("F"))
                {
                    resultSet.updateBoolean("APPROVED",true);
                    resultSet.updateString("APPROVED_BY",EITLERPGLOBAL.gNewUserID+"");
                    resultSet.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());


                }
                else
                {
                    resultSet.updateBoolean("APPROVED",false);
                    resultSet.updateString("APPROVED_DATE","0000-00-00");
                }
            resultSet.updateBoolean("CANCELED",false);
            resultSet.updateBoolean("REJECTED",false);
            resultSet.updateString("REJECTED_DATE","0000-00-00");
            //resultSet.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            resultSet.updateBoolean("CHANGED",true);
            resultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            try{
                    resultSet.updateRow();
            }catch(Exception e)
            {
                System.out.println("Header Updation Failed : "+e.getMessage());
            }
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_PIECE_MERGING_HEADER_H WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
            
            RevNo++;
           
            rsHeaderH.moveToInsertRow();
            
            rsHeaderH.updateString("REVISION_NO",RevNo+"");
            
            rsHeaderH.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            
            rsHeaderH.updateString("UPN",getAttribute("UPN").getString());
            rsHeaderH.updateString("PIECE_NO",getAttribute("PIECE_NO").getString());
            rsHeaderH.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsHeaderH.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            rsHeaderH.updateString("LENGTH",getAttribute("LENGTH").getString());
            rsHeaderH.updateString("WIDTH",getAttribute("WIDTH").getString());
            rsHeaderH.updateString("GSM",getAttribute("GSM").getString());
            rsHeaderH.updateString("SQMTR",getAttribute("SQMTR").getString());
            rsHeaderH.updateString("WEIGHT",getAttribute("WEIGHT").getString());
            rsHeaderH.updateString("PRODUCT_CODE",getAttribute("PRODUCT_CODE").getString());
            rsHeaderH.updateString("PRODUCT_GROUP",getAttribute("PRODUCT_GROUP").getString());
            rsHeaderH.updateString("PRODUCT_STYLE",getAttribute("PRODUCT_STYLE").getString());
            rsHeaderH.updateString("EXISTING_PIECE_VALUE",getAttribute("EXISTING_PIECE_VALUE").getString());
            rsHeaderH.updateString("ORDER_UPN_VALUE",getAttribute("ORDER_UPN_VALUE").getString());
            rsHeaderH.updateString("PROFIT_LOSS",getAttribute("PROFIT_LOSS").getString());
            rsHeaderH.updateString("FRESH_BOOKING",getAttribute("FRESH_BOOKING").getString());
            rsHeaderH.updateString("REMARK",getAttribute("REMARK").getString());
            rsHeaderH.updateString("ORDER_REF_NO",getAttribute("ORDER_REF_NO").getString());
            rsHeaderH.updateString("ORDER_PIECE_NO",getAttribute("ORDER_PIECE_NO").getString());
            
            rsHeaderH.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHeaderH.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsHeaderH.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateString("MODIFIED_BY",EITLERPGLOBAL.gNewUserID+"");
            rsHeaderH.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateTimeDB());
            rsHeaderH.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            rsHeaderH.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHeaderH.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHeaderH.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            if(getAttribute("APPROVAL_STATUS").getString().equals("F"))
            {
                rsHeaderH.updateBoolean("APPROVED",true);
                rsHeaderH.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            }
            else
            {
                rsHeaderH.updateBoolean("APPROVED",false);
                rsHeaderH.updateString("APPROVED_DATE","0000-00-00");
            }
            rsHeaderH.updateBoolean("CANCELED",false);
            rsHeaderH.updateBoolean("REJECTED",false);
            rsHeaderH.updateString("REJECTED_DATE","0000-00-00");
            rsHeaderH.updateBoolean("CHANGED",true);
            rsHeaderH.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            
            ResultSet rsTmp=data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");
            rsHeaderH.updateString("FROM_IP",""+str_split[1]);
            
            rsHeaderH.insertRow();
            String OrderNo=getAttribute("DOC_NO").getString();
            data.Execute("DELETE FROM PRODUCTION.FELT_PIECE_MERGING_DETAIL WHERE DOC_NO='"+OrderNo+"'");
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_PIECE_MERGING_DETAIL WHERE DOC_NO='1'");
             
            
            int RevNoH=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_PIECE_MERGING_DETAIL_H WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
            RevNoH++;

            resultSetHistory.moveToInsertRow();
            
            for(int i=1;i<=hmFeltPieceMergingDetails.size();i++) {
                clsFeltPieceMergingDetails ObjFeltSalesOrderDetails=(clsFeltPieceMergingDetails) hmFeltPieceMergingDetails.get(Integer.toString(i));
                
                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateString("SR_NO", ObjFeltSalesOrderDetails.getAttribute("SR_NO").getString());
                resultSetTemp.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                
                resultSetTemp.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
                resultSetTemp.updateString("PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                resultSetTemp.updateString("PARTY_CODE", ObjFeltSalesOrderDetails.getAttribute("PARTY_CODE").getString());
                resultSetTemp.updateString("PARTY_NAME", ObjFeltSalesOrderDetails.getAttribute("PARTY_NAME").getString());
                resultSetTemp.updateString("LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                resultSetTemp.updateString("WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                resultSetTemp.updateString("GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                resultSetTemp.updateString("SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQMTR").getString());
                resultSetTemp.updateString("WEIGHT", ObjFeltSalesOrderDetails.getAttribute("WEIGHT").getString());
                resultSetTemp.updateString("PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                resultSetTemp.updateString("PRODUCT_STYLE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_STYLE").getString());
                resultSetTemp.updateString("PRODUCT_GROUP", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_GROUP").getString());
                resultSetTemp.updateString("PIECE_STAGE", ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString());
                resultSetTemp.updateString("PIECE_STATUS", ObjFeltSalesOrderDetails.getAttribute("PIECE_STATUS").getString());
                
                resultSetTemp.updateString("MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                resultSetTemp.updateString("POSITION", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                resultSetTemp.updateString("POSITION_DESC", ObjFeltSalesOrderDetails.getAttribute("POSITION_DESC").getString());
                resultSetTemp.updateString("LAYER_TYPE", ObjFeltSalesOrderDetails.getAttribute("LAYER_TYPE").getString());
                
                resultSetTemp.insertRow();
                
                
                resultSetHistory.updateInt("REVISION_NO",RevNoH);
                
                resultSetHistory.updateString("SR_NO", ObjFeltSalesOrderDetails.getAttribute("SR_NO").getString());
                resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                
                
                resultSetHistory.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
                resultSetHistory.updateString("PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                resultSetHistory.updateString("PARTY_CODE", ObjFeltSalesOrderDetails.getAttribute("PARTY_CODE").getString());
                resultSetHistory.updateString("PARTY_NAME", ObjFeltSalesOrderDetails.getAttribute("PARTY_NAME").getString());
                resultSetHistory.updateString("LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                resultSetHistory.updateString("WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                resultSetHistory.updateString("GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                resultSetHistory.updateString("SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQMTR").getString());
                resultSetHistory.updateString("WEIGHT", ObjFeltSalesOrderDetails.getAttribute("WEIGHT").getString());
                resultSetHistory.updateString("PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                resultSetHistory.updateString("PRODUCT_STYLE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_STYLE").getString());
                resultSetHistory.updateString("PRODUCT_GROUP", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_GROUP").getString());
                resultSetHistory.updateString("PIECE_STAGE", ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString());
                resultSetHistory.updateString("PIECE_STATUS", ObjFeltSalesOrderDetails.getAttribute("PIECE_STATUS").getString());
                resultSetHistory.updateString("MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                resultSetHistory.updateString("POSITION", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                resultSetHistory.updateString("POSITION_DESC", ObjFeltSalesOrderDetails.getAttribute("POSITION_DESC").getString());
                resultSetHistory.updateString("LAYER_TYPE", ObjFeltSalesOrderDetails.getAttribute("LAYER_TYPE").getString());
                
                
                resultSetHistory.insertRow();
                
                // Final Approval and save to PIECE REGISTER 
                if(getAttribute("APPROVAL_STATUS").getString().equals("F") && !ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString().equals(""))
                {
                    //
                    if(ObjFeltSalesOrderDetails.getAttribute("PIECE_STATUS").getString().equals("OLD"))
                    {
                        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PIECE_STAGE='JOINED' WHERE PR_PIECE_NO='"+ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString()+"'");
                        data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_PIECE_STAGE='JOINED' WHERE WIP_PIECE_NO='"+ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString()+"'");
                    }
                    if(ObjFeltSalesOrderDetails.getAttribute("PIECE_STATUS").getString().equals("NEW"))
                    {
                        if(getAttribute("FRESH_BOOKING").getString().equals("0"))
                        {
                        try {
                            ResultSet rsRegister;
                            Statement stRegister;
                                stRegister = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                rsRegister = stRegister.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO=''");

                                rsRegister.moveToInsertRow();

                                
                                rsRegister.updateString("PR_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                                rsRegister.updateString("PR_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                rsRegister.updateString("PR_ORDER_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                rsRegister.updateString("PR_DOC_NO", getAttribute("DOC_NO").getString());
                                rsRegister.updateString("PR_MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                                rsRegister.updateString("PR_POSITION_NO", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                                rsRegister.updateString("PR_PARTY_CODE", getAttribute("PARTY_CODE").getString());
                                rsRegister.updateString("PR_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                                rsRegister.updateString("PR_UPN", getAttribute("UPN").getString());
                                rsRegister.updateString("PR_GROUP", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_GROUP").getString());
                                rsRegister.updateString("PR_STYLE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_STYLE").getString());
                                rsRegister.updateString("PR_LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                                rsRegister.updateString("PR_WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                                rsRegister.updateString("PR_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                                rsRegister.updateString("PR_THORITICAL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("WEIGHT").getString());
                                rsRegister.updateString("PR_SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQMTR").getString());
                                Connection Conn;
                                Statement stmt;
                                ResultSet rsData;

                                try {
                                        Conn = data.getConn();
                                        stmt = Conn.createStatement();
                                        
                                        rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_HEADER where S_ORDER_NO='"+getAttribute("ORDER_REF_NO").getString()+"'");
                                        rsData.first();
                                        
                                        rsRegister.updateString("PR_REGION", rsData.getString("REGION"));
                                        rsRegister.updateString("PR_INCHARGE", rsData.getString("SALES_ENGINEER"));
                                        rsRegister.updateString("PR_REFERENCE", rsData.getString("REFERENCE"));
                                        rsRegister.updateString("PR_REFERENCE_DATE", rsData.getString("REFERENCE_DATE"));
                                        rsRegister.updateString("PR_PO_NO", rsData.getString("P_O_NO"));
                                        rsRegister.updateString("PR_PO_DATE", rsData.getString("P_O_DATE"));
                                        
                                        rsRegister.updateString("PR_CONTACT_PERSON", rsData.getString("CONTACT_PERSON"));
                                        rsRegister.updateString("PR_EMAIL_ID", rsData.getString("EMAIL_ID"));
                                        rsRegister.updateString("PR_PHONE_NO", rsData.getString("PHONE_NUMBER"));
                                
                                }catch(Exception e)
                                {
                                    e.printStackTrace();
                                }
                                try {
                                        Conn = data.getConn();
                                        stmt = Conn.createStatement();
                                        rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DETAIL where S_ORDER_NO='"+getAttribute("ORDER_REF_NO").getString()+"' AND PIECE_NO='"+getAttribute("ORDER_PIECE_NO").getString()+"'");
                                        rsData.first();
                                        rsRegister.updateString("PR_SYN_PER", rsData.getString("SYN_PER"));
                                        rsRegister.updateString("PR_REQUESTED_MONTH", rsData.getString("REQ_MONTH"));
                                    }catch(Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                    
                                            
                                 //rsRegister.updateString("PR_PIECE_REMARK", ObjFeltSalesOrderDetails.getAttribute("REMARK").getString());

                                //rsRegister.updateString("PR_PIECE_STAGE","WEAVING");
                                rsRegister.updateString("PR_ORDER_REMARK", "Piece Merge with document number "+getAttribute("DOC_NO").getString()+" ");
                                rsRegister.updateString("PR_PIECE_STAGE", ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString());
                                

                                rsRegister.updateString("PR_RATE_INDICATOR", "NEW");

                                if (ObjFeltSalesOrderDetails.getAttribute("LAYER_TYPE").getString().equals("WITHOUT_AB")) {
                                    rsRegister.updateString("PR_PRIORITY_HOLD_CAN_FLAG", "0");
                                    rsRegister.updateString("PR_PIECE_AB_FLAG", "");
                                } else if (ObjFeltSalesOrderDetails.getAttribute("LAYER_TYPE").getString().equals("WITH_AB")) {
                                    rsRegister.updateString("PR_PRIORITY_HOLD_CAN_FLAG", "0");
                                    rsRegister.updateString("PR_PIECE_AB_FLAG", "AB");
                                }
                                //
                                rsRegister.updateString("PR_DIVERSION_FLAG", "CLOSED");

                                rsRegister.updateString("PR_BILL_LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                                rsRegister.updateString("PR_BILL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                                rsRegister.updateString("PR_BILL_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                                rsRegister.updateString("PR_BILL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("WEIGHT").getString());
                                rsRegister.updateString("PR_BILL_SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQMTR").getString());
                                rsRegister.updateString("PR_BILL_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                                rsRegister.updateString("PR_BILL_STYLE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_STYLE").getString());
                                //        rsRegister.updateString("PR_DATE_SLOT",ObjFeltSalesOrderDetails.getAttribute("DATE_SLOT").getString());
                                rsRegister.updateString("PR_TENDER_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("WEIGHT").getString());
                                rsRegister.updateString("PR_TENDER_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());

                                

                                rsRegister.updateString("PR_WARP_DATE", "0000-00-00");
                                rsRegister.updateString("PR_WVG_DATE", "0000-00-00");
                                rsRegister.updateString("PR_MND_DATE", "0000-00-00");
                                rsRegister.updateString("PR_NDL_DATE", "0000-00-00");
                                rsRegister.updateString("PR_FNSG_DATE", "0000-00-00");
                                rsRegister.updateString("PR_RCV_DATE", "0000-00-00");
                                rsRegister.updateString("PR_PACKED_DATE", "0000-00-00");
                                rsRegister.updateString("PR_EXP_DISPATCH_DATE", "0000-00-00");
                                rsRegister.updateString("PR_INVOICE_DATE", "0000-00-00");
                                rsRegister.updateString("PR_LR_DATE", "0000-00-00");
                                rsRegister.updateString("PR_HOLD_DATE", "0000-00-00");
                                rsRegister.updateString("PR_RELEASE_DATE", "0000-00-00");

                                if(ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString().equals("WEAVING"))
                                {
                                    rsRegister.updateString("PR_WIP_STATUS", "WARPED");
                                    rsRegister.updateString("PR_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    rsRegister.updateString("PR_WARP_A_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    rsRegister.updateString("PR_WARP_B_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                }
                                if(ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString().equals("MENDING"))
                                {
                                    rsRegister.updateString("PR_WIP_STATUS", "WOVEN");
                                    rsRegister.updateString("PR_WVG_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    rsRegister.updateString("PR_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    rsRegister.updateString("PR_WARP_A_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    rsRegister.updateString("PR_WARP_B_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                }
                                if(ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString().equals("NEEDLING"))
                                {
                                    rsRegister.updateString("PR_WIP_STATUS", "MENDED");
                                    rsRegister.updateString("PR_WVG_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    rsRegister.updateString("PR_MND_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    rsRegister.updateString("PR_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    rsRegister.updateString("PR_WARP_A_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    rsRegister.updateString("PR_WARP_B_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                }
                                if(ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString().equals("FINISHING"))
                                {
                                    rsRegister.updateString("PR_WIP_STATUS", "NEEDLED");
                                    rsRegister.updateString("PR_WVG_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    rsRegister.updateString("PR_MND_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    rsRegister.updateString("PR_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    rsRegister.updateString("PR_WARP_A_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    rsRegister.updateString("PR_WARP_B_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    rsRegister.updateString("PR_NDL_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    rsRegister.updateString("PR_SEAM_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                }
                                        
                                
                                try {
                                    String UPN = getAttribute("UPN").getString();
                                    String Available_Piece = data.getStringValueFromDB("SELECT PR_PIECE_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECETRIAL_CATEGORY LIKE 'UPN%' AND PR_UPN='" + UPN + "' AND PR_PIECETRIAL_FLAG=1");
                                    if (!Available_Piece.equals("")) {
                                        rsRegister.updateString("PR_PIECETRIAL_CATEGORY", "UPN ORDER");
                                        rsRegister.updateString("PR_PIECETRIAL_FLAG", "1");
                                    } else if (!data.getStringValueFromDB("SELECT FT_PIECE_NO FROM PRODUCTION.FELT_TRAIL_PIECE_SELECTION WHERE FT_PIECE_NO='" + UPN + "' AND APPROVED=1 AND COALESCE(CANCELED,0)=0 ").equalsIgnoreCase("")) {
                                        rsRegister.updateString("PR_PIECETRIAL_CATEGORY", "UPN ORDER");
                                        rsRegister.updateString("PR_PIECETRIAL_FLAG", "1");
                                    } else {
                                        rsRegister.updateString("PR_PIECETRIAL_CATEGORY", "");
                                        rsRegister.updateString("PR_PIECETRIAL_FLAG", "0");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                rsRegister.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
                                rsRegister.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
                                rsRegister.updateString("MODIFIED_BY", EITLERPGLOBAL.gNewUserID + "");
                                rsRegister.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                rsRegister.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                                rsRegister.updateString("APPROVER_BY", EITLERPGLOBAL.gNewUserID + "");
                                rsRegister.updateString("APPROVER_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                rsRegister.updateString("APPROVER_REMARK", getAttribute("APPROVER_REMARKS").getString());

                                rsRegister.insertRow();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                if (ObjFeltSalesOrderDetails.getAttribute("LAYER_TYPE").getString().equals("WITHOUT_AB")) {
                                    ResultSet rsRegister;
                                    Statement stRegister;
                                    
                                    stRegister = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                    rsRegister = stRegister.executeQuery("SELECT * FROM PRODUCTION.FELT_WIP_PIECE_REGISTER where WIP_PIECE_NO=''");

                                    rsRegister.moveToInsertRow();

                                    /*
                                           resultSetHistory.updateString("MACHINE_NO", );
                resultSetHistory.updateString("POSITION", );
                resultSetHistory.updateString("POSITION_DESC", ObjFeltSalesOrderDetails.getAttribute("POSITION_DESC").getString());
                                    */
                                    rsRegister.updateString("WIP_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                                    rsRegister.updateString("WIP_EXT_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                                    rsRegister.updateString("WIP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    rsRegister.updateString("WIP_ORDER_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    rsRegister.updateString("WIP_DOC_NO", getAttribute("DOC_NO").getString());
                                    rsRegister.updateString("WIP_UPN", getAttribute("UPN").getString());
                                    rsRegister.updateString("WIP_MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                                    rsRegister.updateString("WIP_POSITION_NO", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                                    rsRegister.updateString("WIP_PARTY_CODE", getAttribute("PARTY_CODE").getString());
                                    rsRegister.updateString("WIP_GROUP", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_GROUP").getString());
                                    rsRegister.updateString("WIP_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                                    rsRegister.updateString("WIP_BILL_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                                    rsRegister.updateString("WIP_STYLE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_STYLE").getString());
                                    rsRegister.updateString("WIP_BILL_STYLE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_STYLE").getString());
                                    rsRegister.updateString("WIP_LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                                    rsRegister.updateString("WIP_ACTUAL_LENGTH", "");
                                    rsRegister.updateString("WIP_BILL_LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                                    rsRegister.updateString("WIP_WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                                    rsRegister.updateString("WIP_ACTUAL_WIDTH", "");
                                    rsRegister.updateString("WIP_BILL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                                    rsRegister.updateString("WIP_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                                    rsRegister.updateString("WIP_TENDER_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                                    rsRegister.updateString("WIP_BILL_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                                    rsRegister.updateString("WIP_THORITICAL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("WEIGHT").getString());
                                    rsRegister.updateString("WIP_ACTUAL_WEIGHT", "");
                                    rsRegister.updateString("WIP_TENDER_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("WEIGHT").getString());
                                    rsRegister.updateString("WIP_BILL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("WEIGHT").getString());
                                    rsRegister.updateString("WIP_SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQMTR").getString());
                                    rsRegister.updateString("WIP_BILL_SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQMTR").getString());

                                    rsRegister.updateString("WIP_PRIORITY_HOLD_CAN_FLAG", "0");

                                    rsRegister.updateString("WIP_PARTY_CODE_ORIGINAL", "");
                                    rsRegister.updateString("WIP_PIECE_NO_ORIGINAL", "");
                                    
                                    
                                    Connection Conn;
                                    Statement stmt;
                                    ResultSet rsData;

                                        try {
                                                Conn = data.getConn();
                                                stmt = Conn.createStatement();

                                                rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_HEADER where S_ORDER_NO='"+getAttribute("ORDER_REF_NO").getString()+"'");
                                                rsData.first();
                                                rsRegister.updateString("WIP_REGION", rsData.getString("REGION"));
                                                rsRegister.updateString("WIP_INCHARGE", rsData.getString("SALES_ENGINEER"));
                                                rsRegister.updateString("WIP_REFERENCE", rsData.getString("REFERENCE"));
                                                rsRegister.updateString("WIP_REFERENCE_DATE", rsData.getString("REFERENCE_DATE"));
                                                rsRegister.updateString("WIP_PO_NO", rsData.getString("P_O_NO"));
                                                rsRegister.updateString("WIP_PO_DATE", rsData.getString("P_O_DATE"));

                                        }catch(Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                        try {
                                            Conn = data.getConn();
                                            stmt = Conn.createStatement();
                                            rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DETAIL where S_ORDER_NO='"+getAttribute("ORDER_REF_NO").getString()+"' AND PIECE_NO='"+getAttribute("ORDER_PIECE_NO").getString()+"'");
                                            rsData.first();

                                            rsRegister.updateString("WIP_SYN_PER", rsData.getString("SYN_PER"));
                                            rsRegister.updateString("WIP_REQUESTED_MONTH", rsData.getString("REQ_MONTH"));

                                        }catch(Exception e)
                                        {
                                            e.printStackTrace();
                                        }

                                    rsRegister.updateString("WIP_ORDER_REMARK", "Piece Merge with document number "+getAttribute("DOC_NO").getString()+" ");
                                    rsRegister.updateString("WIP_PIECE_REMARK", "");

                                    
                                
                                    //rsRegister.updateString("","");
                                    rsRegister.updateString("WIP_FELT_VALUE_WITH_GST", "");
                                    rsRegister.updateString("WIP_FELT_VALUE_WITHOUT_GST", "");
                                    rsRegister.updateString("WIP_FELT_BASE_VALUE", "");
                                    rsRegister.updateString("WIP_DAYS_ORDER_WARPED", "");
                                    rsRegister.updateString("WIP_DIVERSION_FLAG", "");
                                    rsRegister.updateString("WIP_DIVERSION_REASON", "");
                                    rsRegister.updateString("WIP_DIVERTED_FLAG", "CLOSED");
                                    rsRegister.updateString("WIP_DIVERTED_REASON", "");
                                    rsRegister.updateString("WIP_OA_NO", "");
                                    rsRegister.updateString("WIP_OA_DATE", "");
                                    rsRegister.updateString("WIP_OC_NO", "");
                                    rsRegister.updateString("WIP_OC_MONTHYEAR", "");
                                    rsRegister.updateString("WIP_CURRENT_SCH_MONTH", "");

                                    rsRegister.updateString("WIP_PIECE_STAGE", ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString());
                                  
                                    if(ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString().equals("WEAVING"))
                                    {
                                        rsRegister.updateString("WIP_STATUS", "WARPED");
                                        rsRegister.updateString("WIP_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_A_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_B_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    }
                                    if(ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString().equals("MENDING"))
                                    {
                                        rsRegister.updateString("WIP_STATUS", "WOVEN");
                                        rsRegister.updateString("WIP_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_A_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_B_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    }
                                    if(ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString().equals("NEEDLING"))
                                    {
                                        rsRegister.updateString("WIP_STATUS", "MENDED");
                                        rsRegister.updateString("WIP_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_MND_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_A_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_B_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    }
                                    if(ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString().equals("FINISHING"))
                                    {
                                        rsRegister.updateString("WIP_STATUS", "MENDED");
                                        rsRegister.updateString("WIP_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_MND_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_A_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_B_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_NDL_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_SEAM_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    }

                                    rsRegister.insertRow();

                                } else if (ObjFeltSalesOrderDetails.getAttribute("LAYER_TYPE").getString().equals("WITH_AB")) {

                                    ResultSet rsRegister;
                                    Statement stRegister;
                                    
                                    //1st PIECE DETAIL
                                    stRegister = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                    rsRegister = stRegister.executeQuery("SELECT * FROM PRODUCTION.FELT_WIP_PIECE_REGISTER where WIP_PIECE_NO=''");

                                    rsRegister.moveToInsertRow();

                                    rsRegister.updateString("WIP_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                                    rsRegister.updateString("WIP_EXT_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString() + "-A");
                                    rsRegister.updateString("WIP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    rsRegister.updateString("WIP_ORDER_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    rsRegister.updateString("WIP_DOC_NO", getAttribute("DOC_NO").getString());
                                    rsRegister.updateString("WIP_UPN", getAttribute("UPN").getString());
                                    rsRegister.updateString("WIP_MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                                    rsRegister.updateString("WIP_POSITION_NO", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                                    rsRegister.updateString("WIP_PARTY_CODE", getAttribute("PARTY_CODE").getString());
                                    rsRegister.updateString("WIP_GROUP", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_GROUP").getString());
                                    rsRegister.updateString("WIP_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                                    rsRegister.updateString("WIP_BILL_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                                    rsRegister.updateString("WIP_STYLE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_STYLE").getString());
                                    rsRegister.updateString("WIP_BILL_STYLE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_STYLE").getString());
                                    rsRegister.updateString("WIP_LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                                    rsRegister.updateString("WIP_ACTUAL_LENGTH", "");
                                    rsRegister.updateString("WIP_BILL_LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                                    rsRegister.updateString("WIP_WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                                    rsRegister.updateString("WIP_ACTUAL_WIDTH", "");
                                    rsRegister.updateString("WIP_BILL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                                    rsRegister.updateString("WIP_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                                    rsRegister.updateString("WIP_TENDER_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                                    rsRegister.updateString("WIP_BILL_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                                    rsRegister.updateString("WIP_THORITICAL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("WEIGHT").getString());
                                    rsRegister.updateString("WIP_ACTUAL_WEIGHT", "");
                                    rsRegister.updateString("WIP_TENDER_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("WEIGHT").getString());
                                    rsRegister.updateString("WIP_BILL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("WEIGHT").getString());
                                    rsRegister.updateString("WIP_SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQMTR").getString());
                                    rsRegister.updateString("WIP_BILL_SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQMTR").getString());

                                    rsRegister.updateString("WIP_PRIORITY_HOLD_CAN_FLAG", "0");
                                    rsRegister.updateString("WIP_PIECE_AB_FLAG", "AB");
                                    //

                                    rsRegister.updateString("WIP_PARTY_CODE_ORIGINAL", "");
                                    rsRegister.updateString("WIP_PIECE_NO_ORIGINAL", "");
                                    
                                    Connection Conn;
                                    Statement stmt;
                                    ResultSet rsData;

                                        try {
                                                Conn = data.getConn();
                                                stmt = Conn.createStatement();

                                                rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_HEADER where S_ORDER_NO='"+getAttribute("ORDER_REF_NO").getString()+"'");
                                                rsData.first();
                                                rsRegister.updateString("WIP_REGION", rsData.getString("REGION"));
                                                rsRegister.updateString("WIP_INCHARGE", rsData.getString("SALES_ENGINEER"));
                                                rsRegister.updateString("WIP_REFERENCE", rsData.getString("REFERENCE"));
                                                rsRegister.updateString("WIP_REFERENCE_DATE", rsData.getString("REFERENCE_DATE"));
                                                rsRegister.updateString("WIP_PO_NO", rsData.getString("P_O_NO"));
                                                rsRegister.updateString("WIP_PO_DATE", rsData.getString("P_O_DATE"));

                                        }catch(Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                        try {
                                            Conn = data.getConn();
                                            stmt = Conn.createStatement();
                                            rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DETAIL where S_ORDER_NO='"+getAttribute("ORDER_REF_NO").getString()+"' AND PIECE_NO='"+getAttribute("ORDER_PIECE_NO").getString()+"'");
                                            rsData.first();

                                            rsRegister.updateString("WIP_SYN_PER", rsData.getString("SYN_PER"));
                                            rsRegister.updateString("WIP_REQUESTED_MONTH", rsData.getString("REQ_MONTH"));

                                        }catch(Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                    
                                   
                                    //rsRegister.updateString("","");
                                    rsRegister.updateString("WIP_FELT_VALUE_WITH_GST", "");
                                    rsRegister.updateString("WIP_FELT_VALUE_WITHOUT_GST", "");
                                    rsRegister.updateString("WIP_FELT_BASE_VALUE", "");
                                    rsRegister.updateString("WIP_DAYS_ORDER_WARPED", "");
                                    rsRegister.updateString("WIP_DIVERSION_FLAG", "");
                                    rsRegister.updateString("WIP_DIVERSION_REASON", "");
                                    rsRegister.updateString("WIP_DIVERTED_FLAG", "CLOSED");
                                    rsRegister.updateString("WIP_DIVERTED_REASON", "");
                                    rsRegister.updateString("WIP_OA_NO", "");
                                    rsRegister.updateString("WIP_OA_DATE", "");
                                    rsRegister.updateString("WIP_OC_NO", "");
                                    rsRegister.updateString("WIP_OC_MONTHYEAR", "");
                                    rsRegister.updateString("WIP_CURRENT_SCH_MONTH", "");

//                                    rsRegister.updateString("WIP_PIECE_STAGE", "BOOKING");
//                                    rsRegister.updateString("WIP_STATUS", "ACCEPTED");
                                    rsRegister.updateString("WIP_PIECE_STAGE", ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString());
                                  
                                    if(ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString().equals("WEAVING"))
                                    {
                                        rsRegister.updateString("WIP_STATUS", "WARPED");
                                        rsRegister.updateString("WIP_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_A_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_B_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    }
                                    if(ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString().equals("MENDING"))
                                    {
                                        rsRegister.updateString("WIP_STATUS", "WOVEN");
                                        rsRegister.updateString("WIP_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_A_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_B_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    }
                                    if(ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString().equals("NEEDLING"))
                                    {
                                        rsRegister.updateString("WIP_STATUS", "MENDED");
                                        rsRegister.updateString("WIP_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_MND_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_A_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_B_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    }
                                    if(ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString().equals("FINISHING"))
                                    {
                                        rsRegister.updateString("WIP_STATUS", "MENDED");
                                        rsRegister.updateString("WIP_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_MND_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_A_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_B_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_NDL_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_SEAM_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    }
                                    rsRegister.insertRow();

                                    //2nd PIECE DETAIL
                                    stRegister = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                    rsRegister = stRegister.executeQuery("SELECT * FROM PRODUCTION.FELT_WIP_PIECE_REGISTER where WIP_PIECE_NO=''");

                                    rsRegister.moveToInsertRow();

                                    rsRegister.updateString("WIP_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                                    rsRegister.updateString("WIP_EXT_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString() + "-B");
                                    rsRegister.updateString("WIP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    rsRegister.updateString("WIP_ORDER_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    rsRegister.updateString("WIP_DOC_NO", getAttribute("DOC_NO").getString());
                                    rsRegister.updateString("WIP_UPN", getAttribute("UPN").getString());
                                    rsRegister.updateString("WIP_MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                                    rsRegister.updateString("WIP_POSITION_NO", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                                    rsRegister.updateString("WIP_PARTY_CODE", getAttribute("PARTY_CODE").getString());
                                    rsRegister.updateString("WIP_GROUP", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_GROUP").getString());
                                    rsRegister.updateString("WIP_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                                    rsRegister.updateString("WIP_BILL_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                                    rsRegister.updateString("WIP_STYLE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_STYLE").getString());
                                    rsRegister.updateString("WIP_BILL_STYLE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_STYLE").getString());
                                    rsRegister.updateString("WIP_LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                                    rsRegister.updateString("WIP_ACTUAL_LENGTH", "");
                                    rsRegister.updateString("WIP_BILL_LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                                    rsRegister.updateString("WIP_WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                                    rsRegister.updateString("WIP_ACTUAL_WIDTH", "");
                                    rsRegister.updateString("WIP_BILL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                                    rsRegister.updateString("WIP_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                                    rsRegister.updateString("WIP_TENDER_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                                    rsRegister.updateString("WIP_BILL_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                                    rsRegister.updateString("WIP_THORITICAL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("WEIGHT").getString());
                                    rsRegister.updateString("WIP_ACTUAL_WEIGHT", "");
                                    rsRegister.updateString("WIP_TENDER_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("WEIGHT").getString());
                                    rsRegister.updateString("WIP_BILL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("WEIGHT").getString());
                                    rsRegister.updateString("WIP_SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQMTR").getString());
                                    rsRegister.updateString("WIP_BILL_SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQMTR").getString());

                                    rsRegister.updateString("WIP_PRIORITY_HOLD_CAN_FLAG", "0");
                                    rsRegister.updateString("WIP_PIECE_AB_FLAG", "AB");
                                    //

                                    rsRegister.updateString("WIP_PARTY_CODE_ORIGINAL", "");
                                    rsRegister.updateString("WIP_PIECE_NO_ORIGINAL", "");
                                    
                                    

                                        try {
                                                Conn = data.getConn();
                                                stmt = Conn.createStatement();

                                                rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_HEADER where S_ORDER_NO='"+getAttribute("ORDER_REF_NO").getString()+"'");
                                                rsData.first();
                                                rsRegister.updateString("WIP_REGION", rsData.getString("REGION"));
                                                rsRegister.updateString("WIP_INCHARGE", rsData.getString("SALES_ENGINEER"));
                                                rsRegister.updateString("WIP_REFERENCE", rsData.getString("REFERENCE"));
                                                rsRegister.updateString("WIP_REFERENCE_DATE", rsData.getString("REFERENCE_DATE"));
                                                rsRegister.updateString("WIP_PO_NO", rsData.getString("P_O_NO"));
                                                rsRegister.updateString("WIP_PO_DATE", rsData.getString("P_O_DATE"));

                                        }catch(Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                        try {
                                            Conn = data.getConn();
                                            stmt = Conn.createStatement();
                                            rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DETAIL where S_ORDER_NO='"+getAttribute("ORDER_REF_NO").getString()+"' AND PIECE_NO='"+getAttribute("ORDER_PIECE_NO").getString()+"'");
                                            rsData.first();

                                            rsRegister.updateString("WIP_SYN_PER", rsData.getString("SYN_PER"));
                                            rsRegister.updateString("WIP_REQUESTED_MONTH", rsData.getString("REQ_MONTH"));

                                        }catch(Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                    
                                   
                                    //rsRegister.updateString("","");
                                    rsRegister.updateString("WIP_FELT_VALUE_WITH_GST", "");
                                    rsRegister.updateString("WIP_FELT_VALUE_WITHOUT_GST", "");
                                    rsRegister.updateString("WIP_FELT_BASE_VALUE", "");
                                    rsRegister.updateString("WIP_DAYS_ORDER_WARPED", "");
                                    rsRegister.updateString("WIP_DIVERSION_FLAG", "");
                                    rsRegister.updateString("WIP_DIVERSION_REASON", "");
                                    rsRegister.updateString("WIP_DIVERTED_FLAG", "CLOSED");
                                    rsRegister.updateString("WIP_DIVERTED_REASON", "");
                                    rsRegister.updateString("WIP_OA_NO", "");
                                    rsRegister.updateString("WIP_OA_DATE", "");
                                    rsRegister.updateString("WIP_OC_NO", "");
                                    rsRegister.updateString("WIP_OC_MONTHYEAR", "");
                                    rsRegister.updateString("WIP_CURRENT_SCH_MONTH", "");

//                                    rsRegister.updateString("WIP_PIECE_STAGE", "BOOKING");
//                                    rsRegister.updateString("WIP_STATUS", "ACCEPTED");
                                    rsRegister.updateString("WIP_PIECE_STAGE", ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString());
                                  
                                    if(ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString().equals("WEAVING"))
                                    {
                                        rsRegister.updateString("WIP_STATUS", "WARPED");
                                        rsRegister.updateString("WIP_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_A_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_B_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    }
                                    if(ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString().equals("MENDING"))
                                    {
                                        rsRegister.updateString("WIP_STATUS", "WOVEN");
                                        rsRegister.updateString("WIP_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_A_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_B_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    }
                                    if(ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString().equals("NEEDLING"))
                                    {
                                        rsRegister.updateString("WIP_STATUS", "MENDED");
                                        rsRegister.updateString("WIP_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_MND_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_A_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_B_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    }
                                    if(ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString().equals("FINISHING"))
                                    {
                                        rsRegister.updateString("WIP_STATUS", "MENDED");
                                        rsRegister.updateString("WIP_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_MND_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_A_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_B_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_NDL_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_SEAM_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    }
                                    rsRegister.insertRow();

                                    //3rd PIECE DETAIL
                                    stRegister = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                    rsRegister = stRegister.executeQuery("SELECT * FROM PRODUCTION.FELT_WIP_PIECE_REGISTER where WIP_PIECE_NO=''");

                                    rsRegister.moveToInsertRow();

                                    rsRegister.updateString("WIP_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                                    rsRegister.updateString("WIP_EXT_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString() + "-AB");
                                    rsRegister.updateString("WIP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    rsRegister.updateString("WIP_ORDER_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    rsRegister.updateString("WIP_DOC_NO", getAttribute("DOC_NO").getString());
                                    rsRegister.updateString("WIP_UPN", getAttribute("UPN").getString());
                                    rsRegister.updateString("WIP_MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                                    rsRegister.updateString("WIP_POSITION_NO", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                                    rsRegister.updateString("WIP_PARTY_CODE", getAttribute("PARTY_CODE").getString());
                                    rsRegister.updateString("WIP_GROUP", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_GROUP").getString());
                                    rsRegister.updateString("WIP_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                                    rsRegister.updateString("WIP_BILL_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                                    rsRegister.updateString("WIP_STYLE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_STYLE").getString());
                                    rsRegister.updateString("WIP_BILL_STYLE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_STYLE").getString());
                                    rsRegister.updateString("WIP_LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                                    rsRegister.updateString("WIP_ACTUAL_LENGTH", "");
                                    rsRegister.updateString("WIP_BILL_LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                                    rsRegister.updateString("WIP_WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                                    rsRegister.updateString("WIP_ACTUAL_WIDTH", "");
                                    rsRegister.updateString("WIP_BILL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                                    rsRegister.updateString("WIP_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                                    rsRegister.updateString("WIP_TENDER_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                                    rsRegister.updateString("WIP_BILL_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                                    rsRegister.updateString("WIP_THORITICAL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("WEIGHT").getString());
                                    rsRegister.updateString("WIP_ACTUAL_WEIGHT", "");
                                    rsRegister.updateString("WIP_TENDER_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("WEIGHT").getString());
                                    rsRegister.updateString("WIP_BILL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("WEIGHT").getString());
                                    rsRegister.updateString("WIP_SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQMTR").getString());
                                    rsRegister.updateString("WIP_BILL_SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQMTR").getString());

                                    rsRegister.updateString("WIP_PRIORITY_HOLD_CAN_FLAG", "0");
                                    rsRegister.updateString("WIP_PIECE_AB_FLAG", "AB");
                                    //

                                    rsRegister.updateString("WIP_PARTY_CODE_ORIGINAL", "");
                                    rsRegister.updateString("WIP_PIECE_NO_ORIGINAL", "");
                                    
                                        try {
                                                Conn = data.getConn();
                                                stmt = Conn.createStatement();

                                                rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_HEADER where S_ORDER_NO='"+getAttribute("ORDER_REF_NO").getString()+"'");
                                                rsData.first();
                                                rsRegister.updateString("WIP_REGION", rsData.getString("REGION"));
                                                rsRegister.updateString("WIP_INCHARGE", rsData.getString("SALES_ENGINEER"));
                                                rsRegister.updateString("WIP_REFERENCE", rsData.getString("REFERENCE"));
                                                rsRegister.updateString("WIP_REFERENCE_DATE", rsData.getString("REFERENCE_DATE"));
                                                rsRegister.updateString("WIP_PO_NO", rsData.getString("P_O_NO"));
                                                rsRegister.updateString("WIP_PO_DATE", rsData.getString("P_O_DATE"));

                                        }catch(Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                        try {
                                            Conn = data.getConn();
                                            stmt = Conn.createStatement();
                                            rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DETAIL where S_ORDER_NO='"+getAttribute("ORDER_REF_NO").getString()+"' AND PIECE_NO='"+getAttribute("ORDER_PIECE_NO").getString()+"'");
                                            rsData.first();

                                            rsRegister.updateString("WIP_SYN_PER", rsData.getString("SYN_PER"));
                                            rsRegister.updateString("WIP_REQUESTED_MONTH", rsData.getString("REQ_MONTH"));

                                        }catch(Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                    
                                   
                                    //rsRegister.updateString("","");
                                    rsRegister.updateString("WIP_FELT_VALUE_WITH_GST", "");
                                    rsRegister.updateString("WIP_FELT_VALUE_WITHOUT_GST", "");
                                    rsRegister.updateString("WIP_FELT_BASE_VALUE", "");
                                    rsRegister.updateString("WIP_DAYS_ORDER_WARPED", "");
                                    rsRegister.updateString("WIP_DIVERSION_FLAG", "");
                                    rsRegister.updateString("WIP_DIVERSION_REASON", "");
                                    rsRegister.updateString("WIP_DIVERTED_FLAG", "CLOSED");
                                    rsRegister.updateString("WIP_DIVERTED_REASON", "");
                                    rsRegister.updateString("WIP_OA_NO", "");
                                    rsRegister.updateString("WIP_OA_DATE", "");
                                    rsRegister.updateString("WIP_OC_NO", "");
                                    rsRegister.updateString("WIP_OC_MONTHYEAR", "");
                                    rsRegister.updateString("WIP_CURRENT_SCH_MONTH", "");

//                                    rsRegister.updateString("WIP_PIECE_STAGE", "BOOKING");
//                                    rsRegister.updateString("WIP_STATUS", "ACCEPTED");
                                    rsRegister.updateString("WIP_PIECE_STAGE", ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString());
                                  
                                    if(ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString().equals("WEAVING"))
                                    {
                                        rsRegister.updateString("WIP_STATUS", "WARPED");
                                        rsRegister.updateString("WIP_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_A_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_B_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    }
                                    if(ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString().equals("MENDING"))
                                    {
                                        rsRegister.updateString("WIP_STATUS", "WOVEN");
                                        rsRegister.updateString("WIP_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_A_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_B_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    }
                                    if(ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString().equals("NEEDLING"))
                                    {
                                        rsRegister.updateString("WIP_STATUS", "MENDED");
                                        rsRegister.updateString("WIP_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_MND_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_A_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_B_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    }
                                    if(ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString().equals("FINISHING"))
                                    {
                                        rsRegister.updateString("WIP_STATUS", "MENDED");
                                        rsRegister.updateString("WIP_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_MND_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_A_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_WARP_B_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_NDL_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                        rsRegister.updateString("WIP_SEAM_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    }
                                    rsRegister.insertRow();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            
                        }
                    }
                }
            }
            
            
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=ModuleID; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo=getAttribute("DOC_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=(String)getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.FELT_PIECE_MERGING_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator=true;
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
                data.Execute("UPDATE PRODUCTION.FELT_PIECE_MERGING_HEADER SET REJECTED=0,CHANGED=1 WHERE DOC_NO ='"+getAttribute("DOC_NO").getString()+"'");
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
            
            // Update  in Order Master Table To confirm that Weaving has completed
            if(ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {
                
               
                 
                                                
                ObjFeltProductionApprovalFlow.finalApproved=false;
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
    public boolean CanDelete(String documentNo,String stringProductionDate,int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            tmpStmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PIECE_MERGING_HEADER WHERE  DOC_NO='"+documentNo +"' AND APPROVED="+1;
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if(rsTmp.getInt("COUNT")>0){  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            }else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID="+ModuleID+" AND USER_ID="+userID+" AND DOC_NO='"+ documentNo +"' AND STATUS='W'";
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getInt("COUNT")>0) {
                    strSQL = "DELETE FROM PRODUCTION.FELT_PIECE_MERGING_HEADER WHERE "
                    + " DOC_DATE = '"+ EITLERPGLOBAL.formatDateDB(stringProductionDate) +"'"
                    + " AND DOC_NO ='" + documentNo + "'";
                 
                    tmpStmt.executeUpdate(strSQL);
                    LoadData();
                    return true;
                }
                else {
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
    public boolean IsEditable(String orderupdDocumentNo,int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        try {
            if(HistoryView) {
                return false;
            }
            tmpStmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PIECE_MERGING_HEADER WHERE DOC_NO='"+ orderupdDocumentNo +"' AND APPROVED=1";
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0){  //Item is Approved
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID="+ModuleID+" AND USER_ID="+userID+" AND DOC_NO='"+ orderupdDocumentNo +"' AND STATUS='W'";
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
           // String strSql = "SELECT DISTINCT AMEND_DATE FROM PRODUCTION.FELT_CN_TEMP_HEADER WHERE  " + stringFindQuery + " ORDER BY CN_DATE";
            String strSql = "SELECT * FROM PRODUCTION.FELT_PIECE_MERGING_HEADER WHERE  " + stringFindQuery + "";
            System.out.println("STR SQL : "+strSql);
            connection=data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet=statement.executeQuery(strSql);
            if(!resultSet.first()) {
                LoadData();
                Ready=true;
                return false;
            }
            else {
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
           
            setAttribute("REVISION_NO",0);
            
            setAttribute("DOC_NO",resultSet.getString("DOC_NO"));
            
            setAttribute("DOC_DATE",resultSet.getString("DOC_DATE"));
            setAttribute("UPN",resultSet.getString("UPN"));
            setAttribute("PIECE_NO",resultSet.getString("PIECE_NO"));
            setAttribute("PARTY_CODE",resultSet.getString("PARTY_CODE"));
            setAttribute("PARTY_NAME",resultSet.getString("PARTY_NAME"));
            setAttribute("LENGTH",resultSet.getString("LENGTH"));
            setAttribute("WIDTH",resultSet.getString("WIDTH"));
            setAttribute("GSM",resultSet.getString("GSM"));
            setAttribute("SQMTR",resultSet.getString("SQMTR"));
            setAttribute("WEIGHT",resultSet.getString("WEIGHT"));
            setAttribute("PRODUCT_CODE",resultSet.getString("PRODUCT_CODE"));
            setAttribute("PRODUCT_GROUP",resultSet.getString("PRODUCT_GROUP"));
            setAttribute("PRODUCT_STYLE",resultSet.getString("PRODUCT_STYLE"));
            setAttribute("EXISTING_PIECE_VALUE",resultSet.getString("EXISTING_PIECE_VALUE"));
            setAttribute("ORDER_UPN_VALUE",resultSet.getString("ORDER_UPN_VALUE"));
            setAttribute("PROFIT_LOSS",resultSet.getString("PROFIT_LOSS"));
            setAttribute("FRESH_BOOKING",resultSet.getString("FRESH_BOOKING"));
            setAttribute("REMARK",resultSet.getString("REMARK"));
            setAttribute("ORDER_REF_NO",resultSet.getString("ORDER_REF_NO"));
            setAttribute("ORDER_PIECE_NO",resultSet.getString("ORDER_PIECE_NO"));
            
            if(HistoryView) {
                RevNo=resultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",resultSet.getInt("REVISION_NO"));
            } 
           
            setAttribute("CREATED_BY",resultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",resultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",resultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",resultSet.getString("MODIFIED_DATE"));
            setAttribute("APPROVED",resultSet.getInt("APPROVED"));
            //setAttribute("APPROVED_BY",resultSet.getInt("APPROVED_BY"));
            setAttribute("APPROVED_DATE",resultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",resultSet.getBoolean("REJECTED"));
            //setAttribute("REJECTED_BY",resultSet.getBoolean("REJECTED_BY"));
            setAttribute("REJECTED_DATE",resultSet.getString("REJECTED_DATE"));
            setAttribute("CANCELED",resultSet.getInt("CANCELED"));
            setAttribute("HIERARCHY_ID",resultSet.getInt("HIERARCHY_ID"));
           // setAttribute("REJECTED_REMARKS",resultSet.getString("REJECTED_REMARKS"));
            
            hmFeltPieceMergingDetails.clear();
            
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            if(HistoryView) {
                resultSetTemp=statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_PIECE_MERGING_DETAIL_H WHERE DOC_NO='"+resultSet.getString("DOC_NO")+"'  AND REVISION_NO="+RevNo+" ORDER BY SR_NO DESC");
            }else {
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_PIECE_MERGING_DETAIL WHERE DOC_NO='"+resultSet.getString("DOC_NO")+"'  ORDER BY DOC_NO");
            }
            
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
  
                clsFeltPieceMergingDetails ObjFeltSalesOrderDetails = new clsFeltPieceMergingDetails();
                
                ObjFeltSalesOrderDetails.setAttribute("SR_NO",resultSetTemp.getString("SR_NO"));
                ObjFeltSalesOrderDetails.setAttribute("DOC_NO",resultSetTemp.getString("DOC_NO"));
                
                ObjFeltSalesOrderDetails.setAttribute("DOC_DATE",resultSetTemp.getString("DOC_DATE"));
                ObjFeltSalesOrderDetails.setAttribute("PIECE_NO",resultSetTemp.getString("PIECE_NO"));
                ObjFeltSalesOrderDetails.setAttribute("PARTY_CODE",resultSetTemp.getString("PARTY_CODE"));
                ObjFeltSalesOrderDetails.setAttribute("PARTY_NAME",resultSetTemp.getString("PARTY_NAME"));
                ObjFeltSalesOrderDetails.setAttribute("LENGTH",resultSetTemp.getString("LENGTH"));
                ObjFeltSalesOrderDetails.setAttribute("WIDTH",resultSetTemp.getString("WIDTH"));
                ObjFeltSalesOrderDetails.setAttribute("GSM",resultSetTemp.getString("GSM"));
                ObjFeltSalesOrderDetails.setAttribute("SQMTR",resultSetTemp.getString("SQMTR"));
                ObjFeltSalesOrderDetails.setAttribute("WEIGHT",resultSetTemp.getString("WEIGHT"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT_CODE",resultSetTemp.getString("PRODUCT_CODE"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT_STYLE",resultSetTemp.getString("PRODUCT_STYLE"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT_GROUP",resultSetTemp.getString("PRODUCT_GROUP"));
                ObjFeltSalesOrderDetails.setAttribute("PIECE_STAGE",resultSetTemp.getString("PIECE_STAGE"));
                ObjFeltSalesOrderDetails.setAttribute("PIECE_STATUS",resultSetTemp.getString("PIECE_STATUS"));
                
                ObjFeltSalesOrderDetails.setAttribute("MACHINE_NO",resultSetTemp.getString("MACHINE_NO"));
                ObjFeltSalesOrderDetails.setAttribute("POSITION",resultSetTemp.getString("POSITION"));
                ObjFeltSalesOrderDetails.setAttribute("POSITION_DESC",resultSetTemp.getString("POSITION_DESC"));
                ObjFeltSalesOrderDetails.setAttribute("LAYER_TYPE",resultSetTemp.getString("LAYER_TYPE"));
                
                
                hmFeltPieceMergingDetails.put(Integer.toString(serialNoCounter),ObjFeltSalesOrderDetails);
            }
            resultSetTemp.close();
            statementTemp.close();
            
            serialNoCounter=0;
            
            
            
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
           
            //Now Populate the collection, first clear the collection
            hmFeltPieceMergingDetails.clear();
            
            String strSql = "SELECT * FROM PRODUCTION.FELT_PIECE_MERGING_HEADER_H WHERE DOC_NO = " + pDocNo + "";
            System.out.println("STR SQL : "+strSql);
            connection=data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet=statement.executeQuery(strSql);
            resultSet.first();
            setAttribute("DOC_NO",resultSet.getString("DOC_NO"));
            setAttribute("REVISION_NO",resultSet.getString("REVISION_NO"));
                setAttribute("UPDATED_BY",resultSet.getString("UPDATED_BY"));
                //setAttribute("DOC_DATE",resultSet.getString("DOC_DATE"));
                setAttribute("HIERARCHY_ID",resultSet.getInt("HIERARCHY_ID"));
                
                setAttribute("DOC_NO",resultSet.getString("DOC_NO"));
                setAttribute("DOC_DATE",resultSet.getString("DOC_DATE"));
                setAttribute("UPN",resultSet.getString("UPN"));
                setAttribute("PIECE_NO",resultSet.getString("PIECE_NO"));
                setAttribute("PARTY_CODE",resultSet.getString("PARTY_CODE"));
                setAttribute("PARTY_NAME",resultSet.getString("PARTY_NAME"));
                setAttribute("LENGTH",resultSet.getString("LENGTH"));
                setAttribute("WIDTH",resultSet.getString("WIDTH"));
                setAttribute("GSM",resultSet.getString("GSM"));
                setAttribute("SQMTR",resultSet.getString("SQMTR"));
                setAttribute("WEIGHT",resultSet.getString("WEIGHT"));
                setAttribute("PRODUCT_CODE",resultSet.getString("PRODUCT_CODE"));
                setAttribute("PRODUCT_GROUP",resultSet.getString("PRODUCT_GROUP"));
                setAttribute("PRODUCT_STYLE",resultSet.getString("PRODUCT_STYLE"));
                setAttribute("EXISTING_PIECE_VALUE",resultSet.getString("EXISTING_PIECE_VALUE"));
                setAttribute("ORDER_UPN_VALUE",resultSet.getString("ORDER_UPN_VALUE"));
                setAttribute("PROFIT_LOSS",resultSet.getString("PROFIT_LOSS"));
                setAttribute("FRESH_BOOKING",resultSet.getString("FRESH_BOOKING"));
                setAttribute("REMARK",resultSet.getString("REMARK"));
            
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_PIECE_MERGING_DETAIL_H WHERE DOC_NO='"+pDocNo+"'");
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
                
                clsFeltPieceMergingDetails ObjFeltSalesOrderDetails = new clsFeltPieceMergingDetails();
                
                ObjFeltSalesOrderDetails.setAttribute("SR_NO",resultSetTemp.getString("SR_NO"));
                ObjFeltSalesOrderDetails.setAttribute("DOC_NO",resultSetTemp.getString("DOC_NO"));
                
                ObjFeltSalesOrderDetails.setAttribute("DOC_DATE",resultSetTemp.getString("DOC_DATE"));
                ObjFeltSalesOrderDetails.setAttribute("PIECE_NO",resultSetTemp.getString("PIECE_NO"));
                ObjFeltSalesOrderDetails.setAttribute("PARTY_CODE",resultSetTemp.getString("PARTY_CODE"));
                ObjFeltSalesOrderDetails.setAttribute("PARTY_NAME",resultSetTemp.getString("PARTY_NAME"));
                ObjFeltSalesOrderDetails.setAttribute("LENGTH",resultSetTemp.getString("LENGTH"));
                ObjFeltSalesOrderDetails.setAttribute("WIDTH",resultSetTemp.getString("WIDTH"));
                ObjFeltSalesOrderDetails.setAttribute("GSM",resultSetTemp.getString("GSM"));
                ObjFeltSalesOrderDetails.setAttribute("SQMTR",resultSetTemp.getString("SQMTR"));
                ObjFeltSalesOrderDetails.setAttribute("WEIGHT",resultSetTemp.getString("WEIGHT"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT_CODE",resultSetTemp.getString("PRODUCT_CODE"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT_STYLE",resultSetTemp.getString("PRODUCT_STYLE"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT_GROUP",resultSetTemp.getString("PRODUCT_GROUP"));
                ObjFeltSalesOrderDetails.setAttribute("PIECE_STAGE",resultSetTemp.getString("PIECE_STAGE"));
                ObjFeltSalesOrderDetails.setAttribute("PIECE_STATUS",resultSetTemp.getString("PIECE_STATUS"));
                
               hmFeltPieceMergingDetails.put(Integer.toString(serialNoCounter),ObjFeltSalesOrderDetails);
            }
            resultSetTemp.close();
            statementTemp.close();
            resultSet.close();
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public static HashMap getHistoryList(String stringProductionDate, String productionDocumentNo) {
        HashMap hmHistoryList=new HashMap();
        Statement stTmp;
        ResultSet rsTmp;
       // String stringProductionDate1 =EITLERPGLOBAL.formatDate(stringProductionDate);
        
        
        try {
            stTmp=connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);

            rsTmp=stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_PIECE_MERGING_HEADER_H WHERE DOC_NO='"+productionDocumentNo+"'");
            while(rsTmp.next()) {
                clsFeltPieceMerging felt_order = new clsFeltPieceMerging();
                
                felt_order.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                felt_order.setAttribute("MODIFIED_BY",rsTmp.getString("MODIFIED_BY"));
                felt_order.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                felt_order.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                felt_order.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                felt_order.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                felt_order.setAttribute("FROM_IP",rsTmp.getString("FROM_IP"));
                
                hmHistoryList.put(Integer.toString(hmHistoryList.size()+1),felt_order);
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
    
    public boolean ShowHistory(String pDocNo) {
        Ready=false;
        try {
            connection=data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.FELT_PIECE_MERGING_HEADER_H WHERE DOC_NO ='"+pDocNo+"'");
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
                strSQL="SELECT DISTINCT H.DOC_NO,H.DOC_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_PIECE_MERGING_HEADER H, PRODUCTION.FELT_PROD_DOC_DATA D  WHERE D.DOC_NO=H.DOC_NO AND STATUS='W' AND MODULE_ID="+ModuleID+" AND CANCELED=0  AND USER_ID="+pUserID+" ORDER BY RECEIVED_DATE,H.DOC_NO";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT DISTINCT H.DOC_NO,H.DOC_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_PIECE_MERGING_HEADER H,  PRODUCTION.FELT_PROD_DOC_DATA D  WHERE D.DOC_NO=H.DOC_NO AND STATUS='W' AND MODULE_ID="+ModuleID+" AND CANCELED=0  AND USER_ID="+pUserID+" ORDER BY RECEIVED_DATE,H.DOC_NO";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT DISTINCT H.DOC_NO,H.DOC_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_PIECE_MERGING_HEADER H, PRODUCTION.FELT_PROD_DOC_DATA D  WHERE D.DOC_NO=H.DOC_NO AND STATUS='W' AND MODULE_ID="+ModuleID+" AND CANCELED=0  AND USER_ID="+pUserID+"  ORDER BY H.DOC_NO";
            }
            
            ResultSet rsTmp=data.getConn().createStatement().executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp.next()) {
                
                Counter=Counter+1;
                clsFeltPieceMerging ObjDoc=new clsFeltPieceMerging();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                ObjDoc.setAttribute("DOC_DATE",rsTmp.getString("DOC_DATE"));
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
 
    
      
    
       
  public static String getNextFreeNo(int pCompanyId,int pModuleID,int pFirstFree,boolean UpdateLastNo) {
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
            strSQL="SELECT LAST_USED_NO,PADDING_BY,NO_LENGTH,PREFIX_CHARS,SUFFIX_CHARS FROM D_COM_FIRSTFREE WHERE COMPANY_ID="+pCompanyId+" AND MODULE_ID="+pModuleID+" AND FIRSTFREE_NO="+pFirstFree;
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
                    data.Execute("UPDATE D_COM_FIRSTFREE SET LAST_USED_NO='"+strNewNo.trim()+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyId+" AND MODULE_ID="+pModuleID+" AND FIRSTFREE_NO="+pFirstFree);
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
}
