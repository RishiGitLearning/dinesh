/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdml.felt.ui.order;

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
import javax.swing.JOptionPane;

/**
 *
 * @author root
 */
public class clsFeltOrder {
    public String LastError="";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet,rsHeader;
    //private ResultSet rsResultSet,
    
    public HashMap props;
    public boolean Ready = false;
    //Felt Order Collection
    public HashMap hmFeltSalesOrderDetails;
    
    //History Related properties
    public boolean HistoryView=false;
    private String historyAmendDate="";
    private String historyAmendID="";
    private static int ModuleID = 602;
    
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
    public clsFeltOrder() {
        LastError = "";
        props=new HashMap();
        props.put("S_ORDER_NO", new Variant(0));
        props.put("S_ORDER_DATE", new Variant(""));
        props.put("REGION", new Variant(""));
        props.put("SALES_ENGINEER",new Variant(""));
        props.put("PARTY_CODE",new Variant(""));
        props.put("PARTY_NAME",new Variant(""));
        props.put("REFERENCE",new Variant(""));
        props.put("REFERENCE_DATE",new Variant(""));
        props.put("P_O_NO",new Variant(""));
        props.put("P_O_DATE",new Variant(""));
        props.put("REMARK",new Variant(""));
        
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("PREFIX",new Variant(""));
        
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
        
        hmFeltSalesOrderDetails=new HashMap();
    }
    
    public boolean LoadData() {
        Ready=false;
        try {
            connection = data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_HEADER ORDER BY S_ORDER_NO");
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
            if(HistoryView) setHistoryData(historyAmendDate, historyAmendID);
            else setData();
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
            if(HistoryView) setHistoryData(historyAmendDate, historyAmendID);
            else setData();
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
            if(HistoryView) setHistoryData(historyAmendDate, historyAmendID);
            else setData();
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
            if(HistoryView) setHistoryData(historyAmendDate, historyAmendID);
            else setData();
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
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_ORDER_DETAIL WHERE S_ORDER_NO='1'");
            
            // Felt order Updation data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DETAIL_H WHERE S_ORDER_NO='1'");
            
            stHeader=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHeader=stHeader.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_ORDER_HEADER WHERE S_ORDER_NO='1'");
            
            stHeaderH=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHeaderH=stHeaderH.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_ORDER_HEADER_H WHERE S_ORDER_NO='1'");

            
            
            //setAttribute("S_ORDER_NO",);
            
                
            rsHeader.first();
            rsHeader.moveToInsertRow();
            
            rsHeader.updateString("S_ORDER_NO",getAttribute("S_ORDER_NO").getInt()+"");
            rsHeader.updateString("S_ORDER_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("S_ORDER_DATE").getString()));
            rsHeader.updateString("REGION",getAttribute("REGION").getString());
            rsHeader.updateString("SALES_ENGINEER",getAttribute("SALES_ENGINEER").getString());
            rsHeader.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsHeader.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            rsHeader.updateString("REFERENCE",getAttribute("REFERENCE").getString());
            rsHeader.updateString("REFERENCE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("REFERENCE_DATE").getString()));
            rsHeader.updateString("P_O_NO",getAttribute("P_O_NO").getString());
            rsHeader.updateString("P_O_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("P_O_DATE").getString()));
            rsHeader.updateString("REMARK",getAttribute("REMARK").getString());
            
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
            
            rsHeaderH.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHeaderH.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHeaderH.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateString("APPROVER_REMARKS",getAttribute("APPROVER_REMARKS").getString());
            rsHeaderH.updateString("S_ORDER_NO",getAttribute("S_ORDER_NO").getInt()+"");
            rsHeaderH.updateString("S_ORDER_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("S_ORDER_DATE").getString()));
            rsHeaderH.updateString("REGION",getAttribute("REGION").getString());
            rsHeaderH.updateString("SALES_ENGINEER",getAttribute("SALES_ENGINEER").getString());
            rsHeaderH.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsHeaderH.updateString("REFERENCE",getAttribute("REFERENCE").getString());
            rsHeaderH.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            rsHeaderH.updateString("REFERENCE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("REFERENCE_DATE").getString()));
            rsHeaderH.updateString("P_O_NO",getAttribute("P_O_NO").getString());
            rsHeaderH.updateString("REMARK",getAttribute("REMARK").getString());
            rsHeaderH.updateString("UPDATED_BY","0");
            rsHeaderH.updateString("UPDATED_DATE","0000-00-00");
            rsHeaderH.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            
            rsHeader.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
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
            rsHeader.updateString("CHANGED_DATE","0000-00-00");
            
            rsHeaderH.insertRow();
           
            //Now Insert records into FELT_CN_TEMP_HEADER & History tables
            for(int i=1;i<=hmFeltSalesOrderDetails.size();i++) {
                clsFeltSalesOrderDetails ObjFeltSalesOrderDetails = (clsFeltSalesOrderDetails) hmFeltSalesOrderDetails.get(Integer.toString(i));
             
                //Insert records into Felt order Amendment table
                resultSetTemp.moveToInsertRow();
                
                resultSetTemp.updateString("S_ORDER_DETAIL_CODE", "0");
                resultSetTemp.updateInt("SR_NO",i);
                resultSetTemp.updateString("S_ORDER_NO", ObjFeltSalesOrderDetails.getAttribute("S_ORDER_NO").getInt()+"");
                resultSetTemp.updateString("MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                resultSetTemp.updateString("POSITION", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                resultSetTemp.updateString("POSITION_DESC", ObjFeltSalesOrderDetails.getAttribute("POSITION_DESC").getString());
                resultSetTemp.updateString("PIECE_NO",ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                resultSetTemp.updateString("PRODUCT_CODE",ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                resultSetTemp.updateString("PRODUCT_DESC",ObjFeltSalesOrderDetails.getAttribute("PRODUCT_DESC").getString());
                resultSetTemp.updateString("S_GROUP",ObjFeltSalesOrderDetails.getAttribute("S_GROUP").getString());
                resultSetTemp.updateString("LENGTH",ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                resultSetTemp.updateString("WIDTH",ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                resultSetTemp.updateString("GSM",ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                resultSetTemp.updateString("THORITICAL_WIDTH",ObjFeltSalesOrderDetails.getAttribute("THORITICAL_WIDTH").getString());
                resultSetTemp.updateString("SQ_MTR",ObjFeltSalesOrderDetails.getAttribute("SQ_MTR").getString());
                resultSetTemp.updateString("STYLE",ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                resultSetTemp.updateString("REQ_MONTH",ObjFeltSalesOrderDetails.getAttribute("REQ_MONTH").getString());
                resultSetTemp.updateString("SYN_PER",ObjFeltSalesOrderDetails.getAttribute("SYN_PER").getString());
                resultSetTemp.updateString("REMARK",ObjFeltSalesOrderDetails.getAttribute("REMARK").getString());
                resultSetTemp.updateString("OV_RATE",EITLERPGLOBAL.formatDateDB(ObjFeltSalesOrderDetails.getAttribute("OV_RATE").getString()));
                resultSetTemp.updateString("OV_BAS_AMOUNT",ObjFeltSalesOrderDetails.getAttribute("OV_BAS_AMOUNT").getString());
                resultSetTemp.updateString("OV_CHEM_TRT_CHG",ObjFeltSalesOrderDetails.getAttribute("OV_CHEM_TRT_CHG").getString());
                resultSetTemp.updateString("OV_SPIRAL_CHG",ObjFeltSalesOrderDetails.getAttribute("OV_SPIRAL_CHG").getString());
                resultSetTemp.updateString("OV_PIN_CHG",ObjFeltSalesOrderDetails.getAttribute("OV_PIN_CHG").getString());
                resultSetTemp.updateString("OV_SEAM_CHG",ObjFeltSalesOrderDetails.getAttribute("OV_SEAM_CHG").getString());
                resultSetTemp.updateString("OV_INS_IND",ObjFeltSalesOrderDetails.getAttribute("OV_INS_IND").getString());
                resultSetTemp.updateString("OV_INS_AMT",ObjFeltSalesOrderDetails.getAttribute("OV_INS_AMT").getString());
                resultSetTemp.updateString("OV_EXCISE",ObjFeltSalesOrderDetails.getAttribute("OV_EXCISE").getString());
                resultSetTemp.updateString("OV_DISC_PER",ObjFeltSalesOrderDetails.getAttribute("OV_DISC_PER").getString());
                resultSetTemp.updateString("OV_DISC_AMT",ObjFeltSalesOrderDetails.getAttribute("OV_DISC_AMT").getString());
                resultSetTemp.updateString("OV_DISC_BASAMT",ObjFeltSalesOrderDetails.getAttribute("OV_DISC_BASAMT").getString());
                resultSetTemp.updateString("OV_AMT",ObjFeltSalesOrderDetails.getAttribute("OV_AMT").getString());
                resultSetTemp.updateInt("CREATED_BY",EITLERPGLOBAL.gNewUserID);
                resultSetTemp.updateString("CREATED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
 //               resultSetTemp.updateInt("MODIFIED_BY",0);
 //               resultSetTemp.updateString("MODIFIED_DATE","0000-00-00");
//                resultSetTemp.updateBoolean("APPROVED",false);
 //               resultSetTemp.updateString("APPROVED_DATE","0000-00-00");
 //               resultSetTemp.updateBoolean("REJECTED",false);
  //              resultSetTemp.updateString("REJECTED_DATE","0000-00-00");
  //              resultSetTemp.updateInt("CANCELED",0);
  //              resultSetTemp.updateInt("HIERARCHY_ID",(int)ObjFeltSalesOrderDetails.getAttribute("HIERARCHY_ID").getVal());
  //              resultSetTemp.updateInt("CHANGED",1);
  //              resultSetTemp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetTemp.insertRow();
                
                //Insert records into Felt Order Amendment History Table
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO",1);
                resultSetHistory.updateInt("SR_NO",i);
                resultSetHistory.updateString("S_ORDER_DETAIL_CODE", "0");
                resultSetHistory.updateString("S_ORDER_NO", ObjFeltSalesOrderDetails.getAttribute("S_ORDER_NO").getInt()+"");
                resultSetHistory.updateString("MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                resultSetHistory.updateString("POSITION",ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                resultSetHistory.updateString("POSITION_DESC", ObjFeltSalesOrderDetails.getAttribute("POSITION_DESC").getString());
                resultSetHistory.updateString("PIECE_NO",ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                resultSetHistory.updateString("PRODUCT_CODE",ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                resultSetHistory.updateString("PRODUCT_DESC",ObjFeltSalesOrderDetails.getAttribute("PRODUCT_DESC").getString());
                resultSetHistory.updateString("S_GROUP",ObjFeltSalesOrderDetails.getAttribute("S_GROUP").getString());
                resultSetHistory.updateString("LENGTH",ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                resultSetHistory.updateString("WIDTH",ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                resultSetHistory.updateString("GSM",ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                resultSetHistory.updateString("THORITICAL_WIDTH",ObjFeltSalesOrderDetails.getAttribute("THORITICAL_WIDTH").getString());
                resultSetHistory.updateString("SQ_MTR",ObjFeltSalesOrderDetails.getAttribute("SQ_MTR").getString());
                resultSetHistory.updateString("STYLE",ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                resultSetHistory.updateString("REQ_MONTH",ObjFeltSalesOrderDetails.getAttribute("REQ_MONTH").getString());
                resultSetHistory.updateString("SYN_PER",ObjFeltSalesOrderDetails.getAttribute("SYN_PER").getString());
                resultSetHistory.updateString("REMARK",ObjFeltSalesOrderDetails.getAttribute("REMARK").getString());
                resultSetHistory.updateString("OV_RATE",ObjFeltSalesOrderDetails.getAttribute("OV_RATE").getString());
                resultSetHistory.updateString("OV_BAS_AMOUNT",ObjFeltSalesOrderDetails.getAttribute("OV_BAS_AMOUNT").getString());
                resultSetHistory.updateString("OV_CHEM_TRT_CHG",ObjFeltSalesOrderDetails.getAttribute("OV_CHEM_TRT_CHG").getString());
                resultSetHistory.updateString("OV_SPIRAL_CHG",ObjFeltSalesOrderDetails.getAttribute("OV_SPIRAL_CHG").getString());
                resultSetHistory.updateString("OV_PIN_CHG",ObjFeltSalesOrderDetails.getAttribute("OV_PIN_CHG").getString());
                resultSetHistory.updateString("OV_SEAM_CHG",ObjFeltSalesOrderDetails.getAttribute("OV_SEAM_CHG").getString());
                resultSetHistory.updateString("OV_INS_IND",ObjFeltSalesOrderDetails.getAttribute("OV_INS_IND").getString());
                resultSetHistory.updateString("OV_INS_AMT",ObjFeltSalesOrderDetails.getAttribute("OV_INS_AMT").getString());
                resultSetHistory.updateString("OV_EXCISE",ObjFeltSalesOrderDetails.getAttribute("OV_EXCISE").getString());
                resultSetHistory.updateString("OV_DISC_PER",ObjFeltSalesOrderDetails.getAttribute("OV_DISC_PER").getString());
                resultSetHistory.updateString("OV_DISC_AMT",ObjFeltSalesOrderDetails.getAttribute("OV_DISC_AMT").getString());
                resultSetHistory.updateString("OV_DISC_BASAMT",ObjFeltSalesOrderDetails.getAttribute("OV_DISC_BASAMT").getString());
                resultSetHistory.updateString("OV_AMT",ObjFeltSalesOrderDetails.getAttribute("OV_AMT").getString());
                resultSetTemp.updateInt("CREATED_BY",EITLERPGLOBAL.gNewUserID);
                resultSetTemp.updateString("CREATED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateInt("UPDATED_BY",0);
                resultSetHistory.updateString("UPDATED_DATE",EITLERPGLOBAL.formatDateDB("0000-00-00"));
//                resultSetHistory.updateBoolean("APPROVED",false);
//                resultSetHistory.updateString("APPROVED_DATE","0000-00-00");
//                resultSetHistory.updateBoolean("REJECTED",false);
//                resultSetHistory.updateString("REJECTED_DATE","0000-00-00");
//                resultSetHistory.updateInt("CANCELED",0);
//                resultSetHistory.updateInt("HIERARCHY_ID",(int)ObjFeltSalesOrderDetails.getAttribute("HIERARCHY_ID").getVal());
//                resultSetHistory.updateInt("CHANGED",1);
//                resultSetHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetHistory.insertRow();

            }
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=ModuleID; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo=getAttribute("S_ORDER_NO").getInt()+"";
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.FELT_SALES_ORDER_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator=true;
            ObjFeltProductionApprovalFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks=getAttribute("REJECTED_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName="S_ORDER_NO";
            
            
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
            // Production data connection
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_ORDER_DETAIL WHERE S_ORDER_NO='1'");
            
            // Production data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            //Get the Maximum Revision No in History Table.
            resultSetHistory=statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_SALES_ORDER_DETAIL_H WHERE S_ORDER_NO='1'");
            resultSetHistory.first();
            if(resultSetHistory.getRow()>0){
                revisionNo=resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from production data table before insert
            //statementHistory.execute("DELETE FROM PRODUCTION.FELT_SALES_ORDER_DETAIL WHERE S_ORDER_NO='"+getAttribute("S_ORDER_NO").getString()+"'");
            
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DETAIL_H WHERE S_ORDER_NO='1'");
            
            stHeader=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHeader=stHeader.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_ORDER_HEADER WHERE S_ORDER_NO='1'");
            System.out.println("SELECT * FROM  PRODUCTION.FELT_SALES_ORDER_HEADER WHERE S_ORDER_NO='1'");
            stHeaderH=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHeaderH=stHeaderH.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_ORDER_HEADER_H WHERE S_ORDER_NO='1'");
            System.out.println("SELECT * FROM  PRODUCTION.FELT_SALES_ORDER_HEADER_H WHERE S_ORDER_NO='1'");
            
            //Now Update records into FELT_CN_TEMP_HEADER tables
            
            rsHeader.moveToInsertRow();
            rsHeader.updateInt("S_ORDER_NO",getAttribute("S_ORDER_NO").getInt());
          
            rsHeader.updateString("S_ORDER_DATE",EITLERPGLOBAL.formatDateDB((String)getAttribute("S_ORDER_DATE").getObj()));
            rsHeader.updateString("REGION",(String)getAttribute("REGION").getObj());
            rsHeader.updateString("SALES_ENGINEER",(String)getAttribute("SALES_ENGINEER").getObj());
            rsHeader.updateString("PARTY_CODE",(String)getAttribute("PARTY_CODE").getObj());
            rsHeader.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());
            rsHeader.updateString("REFERENCE",(String)getAttribute("REFERENCE").getObj());
            rsHeader.updateString("REFERENCE_DATE",(String)getAttribute("REFERENCE_DATE").getObj());
            rsHeader.updateString("P_O_NO",(String)getAttribute("P_O_NO").getObj());
            rsHeader.updateString("P_O_DATE",(String)getAttribute("P_O_DATE").getObj());
            rsHeader.updateString("REMARK",(String)getAttribute("REMARK").getObj());
            
            rsHeader.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            //rsHeader.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            //rsHeader.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB((String)getAttribute("CREATED_DATE").getObj()));
            rsHeader.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHeader.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            //resultSet.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            rsHeader.updateBoolean("APPROVED",false);
            rsHeader.updateString("APPROVED_DATE","0000-00-00");
            rsHeader.updateBoolean("CANCELED",false);
            rsHeader.updateBoolean("REJECTED",false);
            rsHeader.updateString("REJECTED_DATE","0000-00-00");
            rsHeader.updateBoolean("CHANGED",true);
            rsHeader.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            
            rsHeader.updateRow();
            
            
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_SALES_ORDER_HEADER_H WHERE S_ORDER_NO='"+getAttribute("S_ORDER_NO").getInt()+"'");
            RevNo++;
           
            rsHeaderH.moveToInsertRow();
            
            rsHeaderH.updateString("REVISION_NO",RevNo+"");
            rsHeaderH.updateString("S_ORDER_NO",getAttribute("S_ORDER_NO").getInt()+"");
            rsHeaderH.updateString("S_ORDER_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("S_ORDER_DATE").getString()));
            rsHeaderH.updateString("REGION",getAttribute("REGION").getString());
            rsHeaderH.updateString("SALES_ENGINEER",getAttribute("SALES_ENGINEER").getString());
            rsHeaderH.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsHeaderH.updateString("REFERENCE",getAttribute("REFERENCE").getString());
            rsHeaderH.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            rsHeaderH.updateString("REFERENCE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("REFERENCE_DATE").getString()));
            rsHeaderH.updateString("P_O_NO",getAttribute("P_O_NO").getString());
            rsHeaderH.updateString("REMARK",getAttribute("REMARK").getString());
            
            rsHeaderH.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHeaderH.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsHeaderH.updateString("UPDATED_BY",EITLERPGLOBAL.gNewUserID+"");
            rsHeaderH.updateString("UPDATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            rsHeaderH.updateBoolean("APPROVED",false);
            rsHeaderH.updateString("APPROVED_DATE","0000-00-00");
            rsHeaderH.updateBoolean("CANCELED",false);
            rsHeaderH.updateBoolean("REJECTED",false);
            rsHeaderH.updateString("REJECTED_DATE","0000-00-00");
            rsHeaderH.updateBoolean("CHANGED",true);
            rsHeaderH.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            
            rsHeaderH.updateRow();
           
         
            for(int i=1;i<=hmFeltSalesOrderDetails.size();i++) {
                clsFeltSalesOrderDetails ObjFeltSalesOrderDetails=(clsFeltSalesOrderDetails) hmFeltSalesOrderDetails.get(Integer.toString(i));
                
                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateInt("SR_NO",i);
                resultSetTemp.updateString("S_ORDER_NO", ObjFeltSalesOrderDetails.getAttribute("S_ORDER_NO").getInt()+"");
                resultSetTemp.updateString("S_ORDER_DETAIL_CODE", "0");
                resultSetTemp.updateString("MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                resultSetTemp.updateString("POSITION", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                resultSetTemp.updateString("POSITION_DESC", ObjFeltSalesOrderDetails.getAttribute("POSITION_DESC").getString());
                resultSetTemp.updateString("PIECE_NO",ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                resultSetTemp.updateString("PRODUCT_CODE",ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                resultSetTemp.updateString("PRODUCT_DESC",ObjFeltSalesOrderDetails.getAttribute("PRODUCT_DESC").getString());
                resultSetTemp.updateString("S_GROUP",ObjFeltSalesOrderDetails.getAttribute("S_GROUP").getString());
                resultSetTemp.updateString("LENGTH",ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                resultSetTemp.updateString("WIDTH",ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                resultSetTemp.updateString("GSM",ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                resultSetTemp.updateString("THORITICAL_WIDTH",ObjFeltSalesOrderDetails.getAttribute("THORITICAL_WIDTH").getString());
                resultSetTemp.updateString("SQ_MTR",ObjFeltSalesOrderDetails.getAttribute("SQ_MTR").getString());
                resultSetTemp.updateString("STYLE",ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                resultSetTemp.updateString("REQ_MONTH",ObjFeltSalesOrderDetails.getAttribute("REQ_MONTH").getString());
                resultSetTemp.updateString("SYN_PER",ObjFeltSalesOrderDetails.getAttribute("SYN_PER").getString());
                resultSetTemp.updateString("REMARK",ObjFeltSalesOrderDetails.getAttribute("REMARK").getString());
                resultSetTemp.updateString("OV_RATE",ObjFeltSalesOrderDetails.getAttribute("OV_RATE").getString());
                resultSetTemp.updateString("OV_BAS_AMOUNT",ObjFeltSalesOrderDetails.getAttribute("OV_BAS_AMOUNT").getString());
                resultSetTemp.updateString("OV_CHEM_TRT_CHG",ObjFeltSalesOrderDetails.getAttribute("OV_CHEM_TRT_CHG").getString());
                resultSetTemp.updateString("OV_SPIRAL_CHG",ObjFeltSalesOrderDetails.getAttribute("OV_SPIRAL_CHG").getString());
                resultSetTemp.updateString("OV_PIN_CHG",ObjFeltSalesOrderDetails.getAttribute("OV_PIN_CHG").getString());
                resultSetTemp.updateString("OV_SEAM_CHG",ObjFeltSalesOrderDetails.getAttribute("OV_SEAM_CHG").getString());
                resultSetTemp.updateString("OV_INS_IND",ObjFeltSalesOrderDetails.getAttribute("OV_INS_IND").getString());
                resultSetTemp.updateString("OV_INS_AMT",ObjFeltSalesOrderDetails.getAttribute("OV_INS_AMT").getString());
                resultSetTemp.updateString("OV_EXCISE",ObjFeltSalesOrderDetails.getAttribute("OV_EXCISE").getString());
                resultSetTemp.updateString("OV_DISC_PER",ObjFeltSalesOrderDetails.getAttribute("OV_DISC_PER").getString());
                resultSetTemp.updateString("OV_DISC_AMT",ObjFeltSalesOrderDetails.getAttribute("OV_DISC_AMT").getString());
                resultSetTemp.updateString("OV_DISC_BASAMT",ObjFeltSalesOrderDetails.getAttribute("OV_DISC_BASAMT").getString());
                resultSetTemp.updateString("OV_AMT",ObjFeltSalesOrderDetails.getAttribute("OV_AMT").getString());
                resultSetTemp.updateInt("CREATED_BY",(int)ObjFeltSalesOrderDetails.getAttribute("CREATED_BY").getVal());
                resultSetTemp.updateString("CREATED_DATE",ObjFeltSalesOrderDetails.getAttribute("CREATED_DATE").getString());
                resultSetTemp.updateInt("MODIFIED_BY",EITLERPGLOBAL.gNewUserID);
                resultSetTemp.updateString("MODIFIED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                //resultSetTemp.updateBoolean("APPROVED",false);
                //resultSetTemp.updateString("APPROVED_DATE","0000-00-00");
                //resultSetTemp.updateBoolean("REJECTED",false);
                //resultSetTemp.updateString("REJECTED_DATE","0000-00-00");
                //resultSetTemp.updateInt("CANCELED",0);
                //resultSetTemp.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
                //resultSetTemp.updateInt("CHANGED",1);
                //resultSetTemp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetTemp.updateRow();
                
                int RevNoH=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_SALES_ORDER_DETAIL_H WHERE S_ORDER_NO='"+getAttribute("S_ORDER_NO").getInt()+"'");
                RevNoH++;
                
                resultSetHistory.moveToInsertRow();
                
                resultSetHistory.updateInt("REVISION_NO",RevNoH);
                resultSetHistory.updateInt("SR_NO",i);
                resultSetHistory.updateString("S_ORDER_DETAIL_CODE", "0");
                resultSetHistory.updateString("S_ORDER_NO", ObjFeltSalesOrderDetails.getAttribute("S_ORDER_NO").getInt()+"");
                resultSetHistory.updateString("MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                resultSetHistory.updateString("POSITION",ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                resultSetHistory.updateString("POSITION_DESC", ObjFeltSalesOrderDetails.getAttribute("POSITION_DESC").getString());
                resultSetHistory.updateString("PIECE_NO",ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                resultSetHistory.updateString("PRODUCT_CODE",ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                resultSetHistory.updateString("PRODUCT_DESC",ObjFeltSalesOrderDetails.getAttribute("PRODUCT_DESC").getString());
                resultSetHistory.updateString("S_GROUP",ObjFeltSalesOrderDetails.getAttribute("S_GROUP").getString());
                resultSetHistory.updateString("LENGTH",ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                resultSetHistory.updateString("WIDTH",ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                resultSetHistory.updateString("GSM",ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                resultSetHistory.updateString("THORITICAL_WIDTH",ObjFeltSalesOrderDetails.getAttribute("THORITICAL_WIDTH").getString());
                resultSetHistory.updateString("SQ_MTR",ObjFeltSalesOrderDetails.getAttribute("SQ_MTR").getString());
                resultSetHistory.updateString("STYLE",ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                resultSetHistory.updateString("REQ_MONTH",ObjFeltSalesOrderDetails.getAttribute("REQ_MONTH").getString());
                resultSetHistory.updateString("SYN_PER",ObjFeltSalesOrderDetails.getAttribute("SYN_PER").getString());
                resultSetHistory.updateString("REMARK",ObjFeltSalesOrderDetails.getAttribute("REMARK").getString());
                resultSetHistory.updateString("OV_RATE",ObjFeltSalesOrderDetails.getAttribute("OV_RATE").getString());
                resultSetHistory.updateString("OV_BAS_AMOUNT",ObjFeltSalesOrderDetails.getAttribute("OV_BAS_AMOUNT").getString());
                resultSetHistory.updateString("OV_CHEM_TRT_CHG",ObjFeltSalesOrderDetails.getAttribute("OV_CHEM_TRT_CHG").getString());
                resultSetHistory.updateString("OV_SPIRAL_CHG",ObjFeltSalesOrderDetails.getAttribute("OV_SPIRAL_CHG").getString());
                resultSetHistory.updateString("OV_PIN_CHG",ObjFeltSalesOrderDetails.getAttribute("OV_PIN_CHG").getString());
                resultSetHistory.updateString("OV_SEAM_CHG",ObjFeltSalesOrderDetails.getAttribute("OV_SEAM_CHG").getString());
                resultSetHistory.updateString("OV_INS_IND",ObjFeltSalesOrderDetails.getAttribute("OV_INS_IND").getString());
                resultSetHistory.updateString("OV_INS_AMT",ObjFeltSalesOrderDetails.getAttribute("OV_INS_AMT").getString());
                resultSetHistory.updateString("OV_EXCISE",ObjFeltSalesOrderDetails.getAttribute("OV_EXCISE").getString());
                resultSetHistory.updateString("OV_DISC_PER",ObjFeltSalesOrderDetails.getAttribute("OV_DISC_PER").getString());
                resultSetHistory.updateString("OV_DISC_AMT",ObjFeltSalesOrderDetails.getAttribute("OV_DISC_AMT").getString());
                resultSetHistory.updateString("OV_DISC_BASAMT",ObjFeltSalesOrderDetails.getAttribute("OV_DISC_BASAMT").getString());
                resultSetHistory.updateString("OV_AMT",ObjFeltSalesOrderDetails.getAttribute("OV_AMT").getString());
                resultSetHistory.updateInt("CREATED_BY",(int)ObjFeltSalesOrderDetails.getAttribute("CREATED_BY").getVal());
                resultSetHistory.updateString("CREATED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateInt("UPDATED_BY",EITLERPGLOBAL.gNewUserID);
                resultSetHistory.updateString("UPDATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetHistory.updateRow();
            }
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=ModuleID; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo=getAttribute("S_ORDER_NO").getInt()+"";
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=(String)getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.FELT_SALES_ORDER_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator=true;
            ObjFeltProductionApprovalFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks=getAttribute("REJECTED_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName="S_ORDER_NO";
            
            if(getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFeltProductionApprovalFlow.To=(int)getAttribute("SEND_DOC_TO").getVal();
                ObjFeltProductionApprovalFlow.ExplicitSendTo=true;
            }

            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.FELT_SALES_ORDER_HEADER SET REJECTED=0,CHANGED=1 WHERE S_ORDER_NO ='"+getAttribute("S_ORDER_NO").getString()+"'");
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_SALES_ORDER_HEADER WHERE  S_ORDER_NO='"+documentNo +"' AND APPROVED="+1;
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
                    strSQL = "DELETE FROM PRODUCTION.FELT_SALES_ORDER_HEADER WHERE "
                    + " AND S_ORDER_DATE = '"+ EITLERPGLOBAL.formatDateDB(stringProductionDate) +"'"
                    + " AND S_ORDER_NO ='" + documentNo + "'";
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_SALES_ORDER_HEADER WHERE S_ORDER_NO='"+ orderupdDocumentNo +"' AND APPROVED=1";
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
            String strSql = "SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DETAIL WHERE  " + stringFindQuery + "";
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
            
            setAttribute("S_ORDER_NO",resultSet.getString("S_ORDER_NO"));
            setAttribute("S_ORDER_DATE",resultSet.getDate("S_ORDER_DATE"));
            setAttribute("REGION",resultSet.getString("REGION"));
            setAttribute("SALES_ENGINEER",resultSet.getString("SALES_ENGINEER"));
            setAttribute("PARTY_CODE",resultSet.getString("PARTY_CODE"));
            setAttribute("PARTY_NAME",resultSet.getString("PARTY_NAME"));
            setAttribute("REFERENCE",resultSet.getString("REFERENCE"));
            setAttribute("REFERENCE_DATE",resultSet.getDate("REFERENCE_DATE"));
            setAttribute("P_O_NO",resultSet.getString("P_O_NO"));
            setAttribute("P_O_DATE",resultSet.getDate("P_O_DATE"));
            setAttribute("REMARK",resultSet.getString("REMARK"));
            setAttribute("APPROVED",resultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",resultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",resultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE",resultSet.getString("REJECTED_DATE"));
            setAttribute("CANCELED",resultSet.getInt("CANCELED"));
            
            
            hmFeltSalesOrderDetails.clear();
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_ORDER_DETAIL WHERE S_ORDER_NO='"+resultSet.getString("S_ORDER_NO")+"'  ORDER BY S_ORDER_NO");
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
  
                setAttribute("CREATED_BY",resultSetTemp.getInt("CREATED_BY"));
                setAttribute("CREATED_DATE",resultSetTemp.getString("CREATED_DATE"));
                setAttribute("MODIFIED_BY",resultSetTemp.getInt("MODIFIED_BY"));
                setAttribute("MODIFIED_DATE",resultSetTemp.getString("MODIFIED_DATE"));
                //setAttribute("APPROVED",resultSetTemp.getInt("APPROVED"));
                //setAttribute("APPROVED_DATE",resultSetTemp.getString("APPROVED_DATE"));
                //setAttribute("REJECTED",resultSetTemp.getBoolean("REJECTED"));
                //setAttribute("REJECTED_DATE",resultSetTemp.getString("REJECTED_DATE"));
                //setAttribute("CANCELED",resultSetTemp.getInt("CANCELED"));
                //setAttribute("HIERARCHY_ID",resultSetTemp.getInt("HIERARCHY_ID"));
                //setAttribute("REJECTED_REMARKS",resultSetTemp.getString("REJECTED_REMARKS"));
                
                clsFeltSalesOrderDetails ObjFeltSalesOrderDetails = new clsFeltSalesOrderDetails();
                
                //ObjFeltSalesOrderDetails.setAttribute("SR_NO",serialNoCounter);
               // ObjFeltSalesOrderDetails.setAttribute("S_ORDER_DETAIL_CODE",resultSetTemp.getString("S_ORDER_DETAIL_CODE"));
                ObjFeltSalesOrderDetails.setAttribute("S_ORDER_NO",resultSetTemp.getString("S_ORDER_NO"));
                ObjFeltSalesOrderDetails.setAttribute("MACHINE_NO",resultSetTemp.getString("MACHINE_NO"));
                ObjFeltSalesOrderDetails.setAttribute("POSITION",resultSetTemp.getString("POSITION"));
                ObjFeltSalesOrderDetails.setAttribute("POSITION_DESC",resultSetTemp.getString("POSITION_DESC"));
                ObjFeltSalesOrderDetails.setAttribute("PIECE_NO",resultSetTemp.getString("PIECE_NO"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT_CODE",resultSetTemp.getString("PRODUCT_CODE"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT_DESC",resultSetTemp.getString("PRODUCT_DESC"));
                ObjFeltSalesOrderDetails.setAttribute("S_GROUP",resultSetTemp.getString("S_GROUP"));
                ObjFeltSalesOrderDetails.setAttribute("LENGTH",resultSetTemp.getString("LENGTH"));
                ObjFeltSalesOrderDetails.setAttribute("WIDTH",resultSetTemp.getString("WIDTH"));
                ObjFeltSalesOrderDetails.setAttribute("GSM",resultSetTemp.getString("GSM"));
                ObjFeltSalesOrderDetails.setAttribute("THORITICAL_WIDTH",resultSetTemp.getString("THORITICAL_WIDTH"));
                ObjFeltSalesOrderDetails.setAttribute("SQ_MTR",resultSetTemp.getString("SQ_MTR"));
                ObjFeltSalesOrderDetails.setAttribute("STYLE",resultSetTemp.getString("STYLE"));
                ObjFeltSalesOrderDetails.setAttribute("REQ_MONTH",resultSetTemp.getString("REQ_MONTH"));
                ObjFeltSalesOrderDetails.setAttribute("SYN_PER",resultSetTemp.getString("SYN_PER"));
                ObjFeltSalesOrderDetails.setAttribute("REMARK",resultSetTemp.getString("REMARK"));
                ObjFeltSalesOrderDetails.setAttribute("OV_RATE",resultSetTemp.getString("OV_RATE"));
                ObjFeltSalesOrderDetails.setAttribute("OV_BAS_AMOUNT",resultSetTemp.getString("OV_BAS_AMOUNT"));
                ObjFeltSalesOrderDetails.setAttribute("OV_CHEM_TRT_CHG",resultSetTemp.getString("OV_CHEM_TRT_CHG"));
                ObjFeltSalesOrderDetails.setAttribute("OV_SPIRAL_CHG",resultSetTemp.getString("OV_SPIRAL_CHG"));
                ObjFeltSalesOrderDetails.setAttribute("OV_PIN_CHG",resultSetTemp.getString("OV_PIN_CHG"));
                ObjFeltSalesOrderDetails.setAttribute("OV_SEAM_CHG",resultSetTemp.getString("OV_SEAM_CHG"));
                ObjFeltSalesOrderDetails.setAttribute("OV_INS_IND",resultSetTemp.getString("OV_INS_IND"));
                ObjFeltSalesOrderDetails.setAttribute("OV_INS_AMT",resultSetTemp.getString("OV_INS_AMT"));
                ObjFeltSalesOrderDetails.setAttribute("OV_EXCISE",resultSetTemp.getString("OV_EXCISE"));
                ObjFeltSalesOrderDetails.setAttribute("OV_DISC_PER",resultSetTemp.getString("OV_DISC_PER"));
                ObjFeltSalesOrderDetails.setAttribute("OV_DISC_AMT",resultSetTemp.getString("OV_DISC_AMT"));
                ObjFeltSalesOrderDetails.setAttribute("OV_DISC_BASAMT",resultSetTemp.getString("OV_DISC_BASAMT"));
                ObjFeltSalesOrderDetails.setAttribute("OV_AMT",resultSetTemp.getString("OV_AMT"));
                
                hmFeltSalesOrderDetails.put(Integer.toString(serialNoCounter),ObjFeltSalesOrderDetails);
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
           
            //Now Populate the collection, first clear the collection
            hmFeltSalesOrderDetails.clear();
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_ORDER_HEADER_H WHERE S_ORDER_NO='"+pDocNo+"'");
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
                setAttribute("S_ORDER_NO",resultSetTemp.getString("S_ORDER_NO"));
                setAttribute("UPDATED_BY",resultSetTemp.getString("UPDATED_BY"));
                setAttribute("S_ORDER_DATE",resultSetTemp.getString("S_ORDER_DATE"));
                setAttribute("HIERARCHY_ID",resultSetTemp.getInt("HIERARCHY_ID"));
                
                clsFeltSalesOrderDetails ObjFeltSalesOrderDetails = new clsFeltSalesOrderDetails();
                
                //ObjFeltSalesOrderDetails.setAttribute("SR_NO",serialNoCounter);
               // ObjFeltSalesOrderDetails.setAttribute("S_ORDER_DETAIL_CODE",resultSetTemp.getString("S_ORDER_DETAIL_CODE"));
                ObjFeltSalesOrderDetails.setAttribute("S_ORDER_NO",resultSetTemp.getString("S_ORDER_NO"));
                ObjFeltSalesOrderDetails.setAttribute("MACHINE_NO",resultSetTemp.getString("MACHINE_NO"));
                ObjFeltSalesOrderDetails.setAttribute("POSITION",resultSetTemp.getString("POSITION"));
                ObjFeltSalesOrderDetails.setAttribute("POSITION_DESC",resultSetTemp.getString("POSITION_DESC"));
                ObjFeltSalesOrderDetails.setAttribute("PIECE_NO",resultSetTemp.getString("PIECE_NO"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT_CODE",resultSetTemp.getString("PRODUCT_CODE"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT_DESC",resultSetTemp.getString("PRODUCT_DESC"));
                ObjFeltSalesOrderDetails.setAttribute("S_GROUP",resultSetTemp.getString("S_GROUP"));
                ObjFeltSalesOrderDetails.setAttribute("LENGTH",resultSetTemp.getString("LENGTH"));
                ObjFeltSalesOrderDetails.setAttribute("WIDTH",resultSetTemp.getString("WIDTH"));
                ObjFeltSalesOrderDetails.setAttribute("GSM",resultSetTemp.getString("GSM"));
                ObjFeltSalesOrderDetails.setAttribute("THORITICAL_WIDTH",resultSetTemp.getString("THORITICAL_WIDTH"));
                ObjFeltSalesOrderDetails.setAttribute("SQ_MTR",resultSetTemp.getString("SQ_MTR"));
                ObjFeltSalesOrderDetails.setAttribute("STYLE",resultSetTemp.getString("STYLE"));
                ObjFeltSalesOrderDetails.setAttribute("REQ_MONTH",resultSetTemp.getString("REQ_MONTH"));
                ObjFeltSalesOrderDetails.setAttribute("SYN_PER",resultSetTemp.getString("SYN_PER"));
                ObjFeltSalesOrderDetails.setAttribute("REMARK",resultSetTemp.getString("REMARK"));
               ObjFeltSalesOrderDetails.setAttribute("OV_RATE",resultSetTemp.getString("OV_RATE"));
               ObjFeltSalesOrderDetails.setAttribute("OV_BAS_AMOUNT",resultSetTemp.getString("OV_BAS_AMOUNT"));
               ObjFeltSalesOrderDetails.setAttribute("OV_CHEM_TRT_CHG",resultSetTemp.getString("OV_CHEM_TRT_CHG"));
               ObjFeltSalesOrderDetails.setAttribute("OV_SPIRAL_CHG",resultSetTemp.getString("OV_SPIRAL_CHG"));
               ObjFeltSalesOrderDetails.setAttribute("OV_PIN_CHG",resultSetTemp.getString("OV_PIN_CHG"));
               ObjFeltSalesOrderDetails.setAttribute("OV_SEAM_CHG",resultSetTemp.getString("OV_SEAM_CHG"));
               ObjFeltSalesOrderDetails.setAttribute("OV_INS_IND",resultSetTemp.getString("OV_INS_IND"));
               ObjFeltSalesOrderDetails.setAttribute("OV_INS_AMT",resultSetTemp.getString("OV_INS_AMT"));
               ObjFeltSalesOrderDetails.setAttribute("OV_EXCISE",resultSetTemp.getString("OV_EXCISE"));
               ObjFeltSalesOrderDetails.setAttribute("OV_DISC_PER",resultSetTemp.getString("OV_DISC_PER"));
               ObjFeltSalesOrderDetails.setAttribute("OV_DISC_AMT",resultSetTemp.getString("OV_DISC_AMT"));
               ObjFeltSalesOrderDetails.setAttribute("OV_DISC_BASAMT",resultSetTemp.getString("OV_DISC_BASAMT"));
               ObjFeltSalesOrderDetails.setAttribute("OV_AMT",resultSetTemp.getString("OV_AMT"));
               
               hmFeltSalesOrderDetails.put(Integer.toString(serialNoCounter),ObjFeltSalesOrderDetails);
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

            rsTmp=stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_HEADER_H WHERE S_ORDER_NO='"+productionDocumentNo+"'");
            System.out.println("*** data in approval flow : SELECT * FROM PRODUCTION.FELT_SALES_ORDER_HEADER_H WHERE S_ORDER_NO='"+productionDocumentNo+"'");
            while(rsTmp.next()) {
                clsFeltOrder felt_order = new clsFeltOrder();
                

                felt_order.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                felt_order.setAttribute("UPDATED_BY",rsTmp.getString("UPDATED_BY"));
                felt_order.setAttribute("UPDATED_DATE",rsTmp.getString("UPDATED_DATE"));
                felt_order.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                felt_order.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                felt_order.setAttribute("REJECTED_REMARKS",rsTmp.getString("REJECTED_REMARKS"));
                
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
    
    public boolean ShowHistory(String pProductionDate,String pDocNo) {
        Ready=false;
        try {
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            resultSet=statement.executeQuery("SELECT DISTINCT REVISION_NO FROM PRODUCTION.FELT_SALES_ORDER_HEADER_H WHERE S_ORDER_NO ='"+pDocNo+"'");
            Ready=true;
            historyAmendDate = pProductionDate;
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
                strSQL="SELECT DISTINCT S_ORDER_NO,S_ORDER_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_SALES_ORDER_HEADER_H, PRODUCTION.FELT_PROD_DOC_DATA   WHERE S_ORDER_NO=DOC_NO AND STATUS='W' AND MODULE_ID="+ModuleID+" AND CANCELED=0  AND USER_ID="+pUserID+" ORDER BY RECEIVED_DATE,S_ORDER_NO";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT DISTINCT S_ORDER_NO,S_ORDER_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_SALES_ORDER_HEADER_H, PRODUCTION.FELT_PROD_DOC_DATA   WHERE S_ORDER_NO=DOC_NO AND STATUS='W' AND MODULE_ID="+ModuleID+" AND CANCELED=0  AND USER_ID="+pUserID+"ORDER BY CN_DATE,S_ORDER_NO";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT DISTINCT S_ORDER_NO,S_ORDER_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_SALES_ORDER_HEADER_H, PRODUCTION.FELT_PROD_DOC_DATA   WHERE S_ORDER_NO=DOC_NO AND STATUS='W' AND MODULE_ID="+ModuleID+" AND CANCELED=0  AND USER_ID="+pUserID+"  ORDER BY S_ORDER_NO";
            }
            
            ResultSet rsTmp=data.getConn().createStatement().executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp.next()) {
                
                Counter=Counter+1;
                clsFeltOrder ObjDoc=new clsFeltOrder();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO",rsTmp.getString("CN_ID"));
                ObjDoc.setAttribute("DOC_DATE",rsTmp.getString("CN_DATE"));
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
