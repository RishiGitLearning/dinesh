/*
 * clsFeltYearEndDisc.java
 *
 * Created on March 12, 2013, 3:10 PM
 */

package EITLERP.Production.YearEndDiscountForm2;

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
import EITLERP.Production.FeltCreditNote.clsFeltCNAutoPosting;
import EITLERP.Production.clsFeltProductionApprovalFlow;

/**
 *
 * @author  Rishi Raj Neekhra
 * @version
 */

public class clsFeltYearEndDisc2 extends clsFeltCNAutoPosting {
    
    public String LastError="";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet,rsHeader;
    //private ResultSet rsResultSet,
    
    public HashMap props;
    public boolean Ready = false;
    //Felt Order Collection
    public HashMap hmFeltCreditNoteDetails;
    
    //History Related properties
    public boolean HistoryView=false;
    private String historyAmendDate="";
    private String historyAmendID="";
    
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
    public clsFeltYearEndDisc2() {
        LastError = "";
        props=new HashMap();
        props.put("FORM_FORM2_YEAR_END_DATE", new Variant(""));
        props.put("FORM_FORM2_YEAR_END_ID", new Variant(""));
        props.put("FORM_FORM2_FROM_DATE",new Variant(""));
        props.put("FORM_FORM2_TO_DATE",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("PREFIX",new Variant(""));
        
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
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
        
        hmFeltCreditNoteDetails=new HashMap();
    }
    
    public boolean LoadData() {
        Ready=false;
        try {
            connection = data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.FELT_YEAR_END_DISCOUNT_HEADER_FORM2 ");
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
        ResultSet  resultSetTemp,resultSetHistory,rsHeader;
        Statement  statementTemp, statementHistory,stHeader;
        try {
            // Felt Order Updation data connection
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_YEAR_END_DISCOUNT_DETAIL_FORM2 WHERE FORM2_YEAR_END_ID='' ORDER BY FORM2_YEAR_END_PARTY_CODE ");
            
            // Felt order Updation data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_YEAR_END_DISCOUNT_DETAIL_H_FORM2 WHERE FORM2_YEAR_END_ID='' ORDER BY FORM2_YEAR_END_PARTY_CODE");
            
        stHeader=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHeader=stHeader.executeQuery("SELECT * FROM  PRODUCTION.FELT_YEAR_END_DISCOUNT_HEADER_H_FORM2 WHERE FORM2_YEAR_END_ID='' ");
            
          
            
           
            setAttribute("FORM2_YEAR_END_ID",getNextFreeNo(738,true));
            
            
            
                
            resultSet.first();
            resultSet.moveToInsertRow();
            
            resultSet.updateString("FORM2_YEAR_END_ID",getAttribute("FORM2_YEAR_END_ID").getString());
            resultSet.updateString("FORM2_YEAR_END_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("FORM2_YEAR_END_DATE").getString()));
            resultSet.updateString("FORM2_FROM_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("FORM2_FROM_DATE").getString()));
            resultSet.updateString("FORM2_TO_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("FORM2_TO_DATE").getString()));
            resultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            resultSet.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            resultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            resultSet.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            resultSet.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            resultSet.updateBoolean("APPROVED",false);
            resultSet.updateString("APPROVED_DATE","");
            resultSet.updateBoolean("CANCELED",false);
            resultSet.updateBoolean("REJECTED",false);
            resultSet.updateString("REJECTED_DATE","");
            resultSet.updateBoolean("CHANGED",true);
            resultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            resultSet.insertRow();
            
            
            rsHeader.moveToInsertRow();
            rsHeader.updateInt("REVISION_NO",1);
            rsHeader.updateInt("UPDATED_BY",(int)getAttribute("UPDATED_BY").getVal());
            rsHeader.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHeader.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHeader.updateString("APPROVER_REMARKS",getAttribute("APPROVER_REMARKS").getString());
            rsHeader.updateString("FORM2_YEAR_END_ID",getAttribute("FORM2_YEAR_END_ID").getString());
            rsHeader.updateString("FORM2_YEAR_END_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("FORM2_YEAR_END_DATE").getString()));
            rsHeader.updateString("FORM2_FROM_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("FORM2_FROM_DATE").getString()));
            rsHeader.updateString("FORM2_TO_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("FORM2_TO_DATE").getString()));
            
            rsHeader.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHeader.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHeader.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHeader.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHeader.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsHeader.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            rsHeader.updateBoolean("APPROVED",false);
            rsHeader.updateString("APPROVED_DATE","");
            rsHeader.updateBoolean("CANCELED",false);
            rsHeader.updateBoolean("REJECTED",false);
            rsHeader.updateString("REJECTED_DATE","");
            rsHeader.updateBoolean("CHANGED",true);
            rsHeader.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            
            rsHeader.insertRow();
           
             
            
            //Now Insert records into FELT_YEAR_END_DISCOUNT_HEADER & History tables
            for(int i=1;i<=hmFeltCreditNoteDetails.size();i++) {
                clsFeltYearEndDiscDetails2 ObjFeltYearEndDiscDetails = (clsFeltYearEndDiscDetails2) hmFeltCreditNoteDetails.get(Integer.toString(i));
             
                //Insert records into Felt order Amendment table
                resultSetTemp.moveToInsertRow();
                
                resultSetTemp.updateString("FORM2_YEAR_END_ID", getAttribute("FORM2_YEAR_END_ID").getString());
                resultSetTemp.updateString("FORM2_YEAR_END_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("FORM2_YEAR_END_DATE").getString()));
                resultSetTemp.updateString("FORM2_FROM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("FORM2_FROM_DATE").getString()));
                resultSetTemp.updateString("FORM2_TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("FORM2_TO_DATE").getString()));
                //resultSetTemp.updateString("SR_NO",ObjFeltYearEndDiscDetails.getAttribute("SR_NO").getString());
                resultSetTemp.updateString("FORM2_YEAR_END_MAIN_PARTY_CODE",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_MAIN_PARTY_CODE").getString());
                resultSetTemp.updateString("FORM2_YEAR_END_PARTY_CODE",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_PARTY_CODE").getString());
                resultSetTemp.updateString("FORM2_YEAR_END_PARTY_NAME",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_PARTY_NAME").getString());
                resultSetTemp.updateString("FORM2_YEAR_END_PRODUCT_CODE",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_PRODUCT_CODE").getString());
                resultSetTemp.updateString("FORM2_YEAR_END_TURN_OVER",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_TURN_OVER").getString());
                resultSetTemp.updateString("FORM2_YEAR_END_TARGET_ACHIV",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_TARGET_ACHIV").getString());
                resultSetTemp.updateString("FORM2_YEAR_PERCENT",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_PERCENT").getString());
                resultSetTemp.updateString("FORM2_YEAR_SEAM_PERCENT",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_SEAM_PERCENT").getString());
                resultSetTemp.updateString("FORM2_YEAR_END_YES_NO",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_YES_NO").getString());
                resultSetTemp.updateString("FORM2_YEAR_END_REMARKS1",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_REMARKS1").getString());
                resultSetTemp.updateString("FORM2_YEAR_END_REMARKS2",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_REMARKS2").getString());
                resultSetTemp.updateString("FORM2_EFFECTIVE_FROM",ObjFeltYearEndDiscDetails.getAttribute("FORM2_EFFECTIVE_FROM").getString());
                
                if(ObjFeltYearEndDiscDetails.getAttribute("FORM2_EFFECTIVE_TO").getString().equalsIgnoreCase(""))
                    resultSetTemp.updateString("FORM2_EFFECTIVE_TO",null);
                else
                    resultSetTemp.updateString("FORM2_EFFECTIVE_TO",ObjFeltYearEndDiscDetails.getAttribute("FORM2_EFFECTIVE_TO").getString());
                
                resultSetTemp.updateString("CREATED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetTemp.updateInt("MODIFIED_BY",0);
                resultSetTemp.updateString("MODIFIED_DATE","");
                resultSetTemp.updateBoolean("APPROVED",false);
                resultSetTemp.updateString("APPROVED_DATE","");
                resultSetTemp.updateBoolean("REJECTED",false);
                resultSetTemp.updateString("REJECTED_DATE","");
                resultSetTemp.updateInt("CANCELED",0);
                resultSetTemp.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
                resultSetTemp.updateInt("CHANGED",1);
                resultSetTemp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetTemp.insertRow();
                
                
                
                //Insert records into Felt Order Amendment History Table
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO",1);
                resultSetHistory.updateString("FORM2_YEAR_END_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("FORM2_YEAR_END_DATE").getString()));
                resultSetHistory.updateString("FORM2_YEAR_END_ID", getAttribute("FORM2_YEAR_END_ID").getString());
                resultSetHistory.updateString("FORM2_FROM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("FORM2_FROM_DATE").getString()));
                resultSetHistory.updateString("FORM2_TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("FORM2_TO_DATE").getString()));
                //resultSetHistory.updateString("SR_NO",ObjFeltYearEndDiscDetails.getAttribute("SR_NO").getString());
                resultSetHistory.updateString("FORM2_YEAR_END_MAIN_PARTY_CODE",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_MAIN_PARTY_CODE").getString());
                resultSetHistory.updateString("FORM2_YEAR_END_PARTY_CODE",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_PARTY_CODE").getString());
                resultSetHistory.updateString("FORM2_YEAR_END_PARTY_NAME",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_PARTY_NAME").getString());
                resultSetHistory.updateString("FORM2_YEAR_END_PRODUCT_CODE",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_PRODUCT_CODE").getString());
                resultSetHistory.updateString("FORM2_YEAR_END_TURN_OVER",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_TURN_OVER").getString());
                resultSetHistory.updateString("FORM2_YEAR_END_TARGET_ACHIV",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_TARGET_ACHIV").getString());
                resultSetHistory.updateString("FORM2_YEAR_SEAM_PERCENT",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_SEAM_PERCENT").getString());
                resultSetHistory.updateString("FORM2_YEAR_END_YES_NO",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_YES_NO").getString());
                resultSetHistory.updateString("FORM2_YEAR_END_REMARKS1",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_REMARKS1").getString());
                resultSetHistory.updateString("FORM2_YEAR_END_REMARKS2",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_REMARKS2").getString());
                resultSetHistory.updateString("FORM2_EFFECTIVE_FROM",ObjFeltYearEndDiscDetails.getAttribute("FORM2_EFFECTIVE_FROM").getString());
                
                if(ObjFeltYearEndDiscDetails.getAttribute("FORM2_EFFECTIVE_TO").getString().equalsIgnoreCase(""))
                    resultSetHistory.updateString("FORM2_EFFECTIVE_TO",null);
                else
                    resultSetHistory.updateString("FORM2_EFFECTIVE_TO",ObjFeltYearEndDiscDetails.getAttribute("FORM2_EFFECTIVE_TO").getString());
                
                resultSetHistory.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
                resultSetHistory.updateString("CREATED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateInt("UPDATED_BY",(int)getAttribute("UPDATED_BY").getVal());
                resultSetHistory.updateString("ENTRY_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
                resultSetHistory.updateString("APPROVER_REMARKS",getAttribute("APPROVER_REMARKS").getString());
                resultSetHistory.updateInt("MODIFIED_BY",0);
                resultSetHistory.updateString("MODIFIED_DATE","");
                resultSetHistory.updateBoolean("APPROVED",false);
                resultSetHistory.updateString("APPROVED_DATE","");
                resultSetHistory.updateBoolean("REJECTED",false);
                resultSetHistory.updateString("REJECTED_DATE","");
                resultSetHistory.updateInt("CANCELED",0);
                resultSetHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
                resultSetHistory.updateInt("CHANGED",1);
                resultSetHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetHistory.insertRow();

            }
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=738; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo=getAttribute("FORM2_YEAR_END_ID").getString();
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.FELT_YEAR_END_DISCOUNT_HEADER_FORM2";
            ObjFeltProductionApprovalFlow.IsCreator=true;
            ObjFeltProductionApprovalFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks=getAttribute("REJECTED_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName="FORM2_YEAR_END_ID";
            
            
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
//            if(ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {
//      
//            }
//            
            
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
        
        ResultSet  resultSetTemp,resultSetHistory,rsHeader;;
        Statement  statementTemp, statementHistory,stHeader;;
        int revisionNo =1;
        try {
            // Production data connection
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_YEAR_END_DISCOUNT_DETAIL_FORM2 WHERE FORM2_YEAR_END_ID='' ORDER BY FORM2_YEAR_END_PARTY_CODE ");
            
            // Production data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            //Get the Maximum Revision No in History Table.
            resultSetHistory=statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_YEAR_END_DISCOUNT_DETAIL_H_FORM2 WHERE FORM2_YEAR_END_ID='"+getAttribute("FORM2_YEAR_END_ID").getString()+"' ");
            resultSetHistory.first();
            if(resultSetHistory.getRow()>0){
                revisionNo=resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from production data table before insert
            statementHistory.execute("DELETE FROM PRODUCTION.FELT_YEAR_END_DISCOUNT_DETAIL_FORM2 WHERE FORM2_YEAR_END_ID='"+getAttribute("FORM2_YEAR_END_ID").getString()+"'");
            
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_YEAR_END_DISCOUNT_DETAIL_H_FORM2 WHERE FORM2_YEAR_END_ID='"+getAttribute("FORM2_YEAR_END_ID").getString()+"' ORDER BY FORM2_YEAR_END_PARTY_CODE");
            
            stHeader=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHeader=stHeader.executeQuery("SELECT * FROM  PRODUCTION.FELT_YEAR_END_DISCOUNT_HEADER_H_FORM2 WHERE FORM2_YEAR_END_ID='' ");
            
            //Now Update records into FELT_YEAR_END_DISCOUNT_HEADER tables
            
          
            resultSet.updateString("FORM2_YEAR_END_ID",getAttribute("FORM2_YEAR_END_ID").getString());
            resultSet.updateString("FORM2_YEAR_END_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("FORM2_YEAR_END_DATE").getString()));
            resultSet.updateString("FORM2_FROM_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("FORM2_FROM_DATE").getString()));
            resultSet.updateString("FORM2_TO_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("FORM2_TO_DATE").getString()));
            resultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            resultSet.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            resultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            resultSet.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            resultSet.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            resultSet.updateBoolean("APPROVED",false);
            resultSet.updateString("APPROVED_DATE","");
            resultSet.updateBoolean("CANCELED",false);
            resultSet.updateBoolean("REJECTED",false);
            resultSet.updateString("REJECTED_DATE","");
            resultSet.updateBoolean("CHANGED",true);
            resultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            
            resultSet.updateRow();
            
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_YEAR_END_DISCOUNT_DETAIL_H_FORM2 WHERE FORM2_YEAR_END_ID='"+(String)getAttribute("FORM2_YEAR_END_ID").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("FORM2_YEAR_END_ID").getObj();
            
            
            rsHeader.moveToInsertRow();
            
            rsHeader.updateInt("REVISION_NO",RevNo);
            rsHeader.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHeader.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHeader.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHeader.updateString("APPROVER_REMARKS",getAttribute("APPROVER_REMARKS").getString());
            rsHeader.updateString("FORM2_YEAR_END_ID",getAttribute("FORM2_YEAR_END_ID").getString());
            rsHeader.updateString("FORM2_YEAR_END_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("FORM2_YEAR_END_DATE").getString()));
            rsHeader.updateString("FORM2_FROM_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("FORM2_FROM_DATE").getString()));
            rsHeader.updateString("FORM2_TO_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("FORM2_TO_DATE").getString()));
            rsHeader.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHeader.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHeader.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHeader.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHeader.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsHeader.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            rsHeader.updateBoolean("APPROVED",false);
            rsHeader.updateString("APPROVED_DATE","");
            rsHeader.updateBoolean("CANCELED",false);
            rsHeader.updateBoolean("REJECTED",false);
            rsHeader.updateString("REJECTED_DATE","");
            rsHeader.updateBoolean("CHANGED",true);
            rsHeader.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            
            rsHeader.insertRow();
           
         
            for(int i=1;i<=hmFeltCreditNoteDetails.size();i++) {
                clsFeltYearEndDiscDetails2 ObjFeltYearEndDiscDetails=(clsFeltYearEndDiscDetails2) hmFeltCreditNoteDetails.get(Integer.toString(i));
                
                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateString("FORM2_YEAR_END_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("FORM2_YEAR_END_DATE").getString()));
                resultSetTemp.updateString("FORM2_YEAR_END_ID", getAttribute("FORM2_YEAR_END_ID").getString());
                resultSetTemp.updateString("FORM2_FROM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("FORM2_FROM_DATE").getString()));
                resultSetTemp.updateString("FORM2_TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("FORM2_TO_DATE").getString()));
               // resultSetTemp.updateString("SR_NO",ObjFeltYearEndDiscDetails.getAttribute("SR_NO").getString());
                resultSetTemp.updateString("FORM2_YEAR_END_MAIN_PARTY_CODE",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_MAIN_PARTY_CODE").getString());
                resultSetTemp.updateString("FORM2_YEAR_END_PARTY_CODE",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_PARTY_CODE").getString());
                resultSetTemp.updateString("FORM2_YEAR_END_PARTY_NAME",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_PARTY_NAME").getString());
                resultSetTemp.updateString("FORM2_YEAR_END_PRODUCT_CODE",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_PRODUCT_CODE").getString());
                resultSetTemp.updateString("FORM2_YEAR_END_TURN_OVER",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_TURN_OVER").getString());
                resultSetTemp.updateString("FORM2_YEAR_END_TARGET_ACHIV",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_TARGET_ACHIV").getString());
                resultSetTemp.updateString("FORM2_YEAR_PERCENT",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_PERCENT").getString());
                resultSetTemp.updateString("FORM2_YEAR_SEAM_PERCENT",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_SEAM_PERCENT").getString());
                resultSetTemp.updateString("FORM2_YEAR_END_YES_NO",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_YES_NO").getString());
                resultSetTemp.updateString("FORM2_YEAR_END_REMARKS1",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_REMARKS1").getString());
                resultSetTemp.updateString("FORM2_YEAR_END_REMARKS2",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_REMARKS2").getString());
                resultSetTemp.updateString("FORM2_EFFECTIVE_FROM",ObjFeltYearEndDiscDetails.getAttribute("FORM2_EFFECTIVE_FROM").getString());
                
                if(ObjFeltYearEndDiscDetails.getAttribute("FORM2_EFFECTIVE_TO").getString().equalsIgnoreCase(""))
                    resultSetTemp.updateString("FORM2_EFFECTIVE_TO",null);
                else
                    resultSetTemp.updateString("FORM2_EFFECTIVE_TO",ObjFeltYearEndDiscDetails.getAttribute("FORM2_EFFECTIVE_TO").getString());
                
                resultSetTemp.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
                resultSetTemp.updateString("CREATED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetTemp.updateInt("MODIFIED_BY",0);
                resultSetTemp.updateString("MODIFIED_DATE","");
                resultSetTemp.updateBoolean("APPROVED",false);
                resultSetTemp.updateString("APPROVED_DATE","");
                resultSetTemp.updateBoolean("REJECTED",false);
                resultSetTemp.updateString("REJECTED_DATE","");
                resultSetTemp.updateInt("CANCELED",0);
                resultSetTemp.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
                resultSetTemp.updateInt("CHANGED",1);
                resultSetTemp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetTemp.insertRow();
                
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO",RevNo);
                resultSetHistory.updateString("FORM2_YEAR_END_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("FORM2_YEAR_END_DATE").getString()));
                resultSetHistory.updateString("FORM2_YEAR_END_ID", getAttribute("FORM2_YEAR_END_ID").getString());
                resultSetHistory.updateString("FORM2_FROM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("FORM2_FROM_DATE").getString()));
                resultSetHistory.updateString("FORM2_TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("FORM2_TO_DATE").getString()));
                //resultSetHistory.updateString("SR_NO",ObjFeltYearEndDiscDetails.getAttribute("SR_NO").getString());
                resultSetHistory.updateString("FORM2_YEAR_END_MAIN_PARTY_CODE",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_MAIN_PARTY_CODE").getString());
                resultSetHistory.updateString("FORM2_YEAR_END_PARTY_CODE",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_PARTY_CODE").getString());
                resultSetHistory.updateString("FORM2_YEAR_END_PARTY_NAME",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_PARTY_NAME").getString());
                resultSetHistory.updateString("FORM2_YEAR_END_PRODUCT_CODE",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_PRODUCT_CODE").getString());
                resultSetHistory.updateString("FORM2_YEAR_END_TURN_OVER",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_TURN_OVER").getString());
                resultSetHistory.updateString("FORM2_YEAR_END_TARGET_ACHIV",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_TARGET_ACHIV").getString());
                resultSetHistory.updateString("FORM2_YEAR_PERCENT",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_PERCENT").getString());
                resultSetHistory.updateString("FORM2_YEAR_SEAM_PERCENT",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_SEAM_PERCENT").getString());
                resultSetHistory.updateString("FORM2_YEAR_END_YES_NO",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_YES_NO").getString());
                resultSetHistory.updateString("FORM2_YEAR_END_REMARKS1",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_REMARKS1").getString());
                resultSetHistory.updateString("FORM2_YEAR_END_REMARKS2",ObjFeltYearEndDiscDetails.getAttribute("FORM2_YEAR_END_REMARKS2").getString());
                resultSetHistory.updateString("FORM2_EFFECTIVE_FROM",ObjFeltYearEndDiscDetails.getAttribute("FORM2_EFFECTIVE_FROM").getString());
                
                if(ObjFeltYearEndDiscDetails.getAttribute("FORM2_EFFECTIVE_TO").getString().equalsIgnoreCase(""))
                    resultSetHistory.updateString("FORM2_EFFECTIVE_TO",null);
                else
                    resultSetHistory.updateString("FORM2_EFFECTIVE_TO",ObjFeltYearEndDiscDetails.getAttribute("FORM2_EFFECTIVE_TO").getString());
                
                resultSetHistory.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
                resultSetHistory.updateString("CREATED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateInt("UPDATED_BY",(int)getAttribute("UPDATED_BY").getVal());
                resultSetHistory.updateString("ENTRY_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
                resultSetHistory.updateString("APPROVER_REMARKS",getAttribute("APPROVER_REMARKS").getString());
                resultSetHistory.updateInt("MODIFIED_BY",0);
                resultSetHistory.updateString("MODIFIED_DATE","");
                resultSetHistory.updateBoolean("APPROVED",false);
                resultSetHistory.updateString("APPROVED_DATE","");
                resultSetHistory.updateBoolean("REJECTED",false);
                resultSetHistory.updateString("REJECTED_DATE","");
                resultSetHistory.updateInt("CANCELED",0);
                resultSetHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
                resultSetHistory.updateInt("CHANGED",1);
                resultSetHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetHistory.insertRow();
            }
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=738; //Felt Order Updation Module
            ObjFeltProductionApprovalFlow.DocNo=getAttribute("FORM2_YEAR_END_ID").getString();
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.FELT_YEAR_END_DISCOUNT_HEADER_FORM2";
            ObjFeltProductionApprovalFlow.IsCreator=false;
            ObjFeltProductionApprovalFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks=getAttribute("REJECTED_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName="FORM2_YEAR_END_ID";
            
            if(getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFeltProductionApprovalFlow.To=(int)getAttribute("SEND_DOC_TO").getVal();
                ObjFeltProductionApprovalFlow.ExplicitSendTo=true;
            }

            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.FELT_YEAR_END_DISCOUNT_HEADER_FORM2 SET REJECTED=0,CHANGED=1 WHERE FORM2_YEAR_END_ID ='"+getAttribute("FORM2_YEAR_END_ID").getString()+"'");
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
//            //--------- Approval Flow Update complete -----------
//            
//            // Update  in Order Master Table To confirm that Weaving has completed
//            if(ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {
//                
//                ObjFeltProductionApprovalFlow.finalApproved=false;
//            }
//            
            if(ObjFeltProductionApprovalFlow.Status.equals("F")){
                
                GenerateYearEndCN(getAttribute("FORM2_YEAR_END_ID").getString(),EITLERPGLOBAL.formatDateDB(getAttribute("FORM2_FROM_DATE").getString()),EITLERPGLOBAL.formatDateDB(getAttribute("FORM2_TO_DATE").getString()));
                YearEndCNVoucherPosting(getAttribute("FORM2_YEAR_END_ID").getString(),EITLERPGLOBAL.formatDateDB(getAttribute("FORM2_FROM_DATE").getString()),EITLERPGLOBAL.formatDateDB(getAttribute("FORM2_TO_DATE").getString()));
                
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_YEAR_END_DISCOUNT_HEADER WHERE  FELT_AMEND_ID='"+documentNo +"' AND APPROVED="+1;
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if(rsTmp.getInt("COUNT")>0){  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            }else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=738 AND USER_ID="+userID+" AND DOC_NO='"+ documentNo +"' AND STATUS='W'";
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getInt("COUNT")>0) {
                    strSQL = "DELETE FROM PRODUCTION.FELT_YEAR_END_DISCOUNT_HEADER WHERE "
                    + " AND CN_DATE= '"+ EITLERPGLOBAL.formatDateDB(stringProductionDate) +"'"
                    + " AND FORM2_YEAR_END_ID='" + documentNo + "'";
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_YEAR_END_DISCOUNT_HEADER_FORM2 WHERE FORM2_YEAR_END_ID='"+ orderupdDocumentNo +"' AND APPROVED=1";
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0){  //Item is Approved
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=738 AND USER_ID="+userID+" AND DOC_NO='"+ orderupdDocumentNo +"' AND STATUS='W'";
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
           // String strSql = "SELECT DISTINCT AMEND_DATE FROM PRODUCTION.FELT_YEAR_END_DISCOUNT_HEADER WHERE  " + stringFindQuery + " ORDER BY CN_DATE";
            //String strSql = "SELECT * FROM PRODUCTION.FELT_YEAR_END_DISCOUNT_DETAIL_FORM2 WHERE  " + stringFindQuery + " ORDER BY FORM2_YEAR_END_PARTY_CODE";
            String strSql = "SELECT * FROM PRODUCTION.FELT_YEAR_END_DISCOUNT_HEADER_FORM2 WHERE  " + stringFindQuery + " ";
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
            
            setAttribute("FORM2_YEAR_END_DATE",resultSet.getDate("FORM2_YEAR_END_DATE"));
            setAttribute("FORM2_YEAR_END_ID",resultSet.getString("FORM2_YEAR_END_ID"));
            setAttribute("FORM2_FROM_DATE",resultSet.getDate("FORM2_FROM_DATE"));
            setAttribute("FORM2_TO_DATE",resultSet.getDate("FORM2_TO_DATE"));
            setAttribute("REJECTED_REMARKS",resultSet.getString("REJECTED_REMARKS"));
  
            hmFeltCreditNoteDetails.clear();
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_YEAR_END_DISCOUNT_DETAIL_FORM2 WHERE FORM2_YEAR_END_ID='"+resultSet.getString("FORM2_YEAR_END_ID")+"' ORDER BY FORM2_YEAR_END_PARTY_CODE");
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
  
                setAttribute("CREATED_BY",resultSetTemp.getInt("CREATED_BY"));
                setAttribute("CREATED_DATE",resultSetTemp.getString("CREATED_DATE"));
                setAttribute("MODIFIED_BY",resultSetTemp.getInt("MODIFIED_BY"));
                setAttribute("MODIFIED_DATE",resultSetTemp.getString("MODIFIED_DATE"));
                setAttribute("APPROVED",resultSetTemp.getInt("APPROVED"));
                setAttribute("APPROVED_DATE",resultSetTemp.getString("APPROVED_DATE"));
                setAttribute("REJECTED",resultSetTemp.getBoolean("REJECTED"));
                setAttribute("REJECTED_DATE",resultSetTemp.getString("REJECTED_DATE"));
                setAttribute("CANCELED",resultSetTemp.getInt("CANCELED"));
                setAttribute("HIERARCHY_ID",resultSetTemp.getInt("HIERARCHY_ID"));
                setAttribute("REJECTED_REMARKS",resultSetTemp.getString("REJECTED_REMARKS"));
                
                clsFeltYearEndDiscDetails2 ObjFeltYearEndDiscDetails = new clsFeltYearEndDiscDetails2();
                
               // ObjFeltYearEndDiscDetails.setAttribute("SR_NO",resultSetTemp.getString("SR_NO"));
                ObjFeltYearEndDiscDetails.setAttribute("FORM2_YEAR_END_MAIN_PARTY_CODE",resultSetTemp.getString("FORM2_YEAR_END_MAIN_PARTY_CODE"));
                ObjFeltYearEndDiscDetails.setAttribute("FORM2_YEAR_END_PARTY_CODE",resultSetTemp.getString("FORM2_YEAR_END_PARTY_CODE"));
                ObjFeltYearEndDiscDetails.setAttribute("FORM2_YEAR_END_PARTY_NAME",resultSetTemp.getString("FORM2_YEAR_END_PARTY_NAME"));
                ObjFeltYearEndDiscDetails.setAttribute("FORM2_YEAR_END_PRODUCT_CODE",resultSetTemp.getString("FORM2_YEAR_END_PRODUCT_CODE"));
                ObjFeltYearEndDiscDetails.setAttribute("FORM2_YEAR_END_TURN_OVER",resultSetTemp.getString("FORM2_YEAR_END_TURN_OVER"));
                ObjFeltYearEndDiscDetails.setAttribute("FORM2_YEAR_END_TARGET_ACHIV",resultSetTemp.getString("FORM2_YEAR_END_TARGET_ACHIV"));
                ObjFeltYearEndDiscDetails.setAttribute("FORM2_YEAR_PERCENT",resultSetTemp.getString("FORM2_YEAR_PERCENT"));
                ObjFeltYearEndDiscDetails.setAttribute("FORM2_YEAR_SEAM_PERCENT",resultSetTemp.getString("FORM2_YEAR_SEAM_PERCENT"));
                ObjFeltYearEndDiscDetails.setAttribute("FORM2_YEAR_END_YES_NO",resultSetTemp.getString("FORM2_YEAR_END_YES_NO"));
                ObjFeltYearEndDiscDetails.setAttribute("FORM2_YEAR_END_REMARKS1",resultSetTemp.getString("FORM2_YEAR_END_REMARKS1"));
                ObjFeltYearEndDiscDetails.setAttribute("FORM2_YEAR_END_REMARKS2",resultSetTemp.getString("FORM2_YEAR_END_REMARKS2"));
                ObjFeltYearEndDiscDetails.setAttribute("FORM2_EFFECTIVE_FROM",resultSetTemp.getString("FORM2_EFFECTIVE_FROM"));
                ObjFeltYearEndDiscDetails.setAttribute("FORM2_EFFECTIVE_TO",resultSetTemp.getString("FORM2_EFFECTIVE_TO"));
                hmFeltCreditNoteDetails.put(Integer.toString(serialNoCounter),ObjFeltYearEndDiscDetails);
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
            hmFeltCreditNoteDetails.clear();
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_YEAR_END_DISCOUNT_HEADER_H_FORM2 WHERE FORM2_YEAR_END_ID='"+pDocNo+"' ");
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
                setAttribute("FORM2_YEAR_END_ID",resultSetTemp.getString("FORM2_YEAR_END_ID"));
                setAttribute("UPDATED_BY",resultSetTemp.getInt("UPDATED_BY"));
                setAttribute("ENTRY_DATE",resultSetTemp.getString("ENTRY_DATE"));
                setAttribute("HIERARCHY_ID",resultSetTemp.getInt("HIERARCHY_ID"));
                
                clsFeltYearEndDiscDetails2 ObjFeltYearEndDiscDetails = new clsFeltYearEndDiscDetails2();
                
               // ObjFeltYearEndDiscDetails.setAttribute("SR_NO",resultSetTemp.getString("SR_NO"));
                ObjFeltYearEndDiscDetails.setAttribute("FORM2_YEAR_END_MAIN_PARTY_CODE",resultSetTemp.getString("FORM2_YEAR_END_MAIN_PARTY_CODE"));
                ObjFeltYearEndDiscDetails.setAttribute("FORM2_YEAR_END_PARTY_CODE",resultSetTemp.getString("FORM2_YEAR_END_PARTY_CODE"));
                ObjFeltYearEndDiscDetails.setAttribute("FORM2_YEAR_END_PARTY_NAME",resultSetTemp.getString("FORM2_YEAR_END_PARTY_NAME"));
                ObjFeltYearEndDiscDetails.setAttribute("FORM2_YEAR_END_PRODUCT_CODE",resultSetTemp.getString("FORM2_YEAR_END_PRODUCT_CODE"));
                ObjFeltYearEndDiscDetails.setAttribute("FORM2_YEAR_END_TURN_OVER",resultSetTemp.getString("FORM2_YEAR_END_TURN_OVER"));
                ObjFeltYearEndDiscDetails.setAttribute("FORM2_YEAR_END_TARGET_ACHIV",resultSetTemp.getString("FORM2_YEAR_END_TARGET_ACHIV"));
                ObjFeltYearEndDiscDetails.setAttribute("FORM2_YEAR_PERCENT",resultSetTemp.getString("FORM2_YEAR_PERCENT"));
                ObjFeltYearEndDiscDetails.setAttribute("FORM2_YEAR_SEAM_PERCENT",resultSetTemp.getString("FORM2_YEAR_SEAM_PERCENT"));
                ObjFeltYearEndDiscDetails.setAttribute("FORM2_YEAR_END_YES_NO",resultSetTemp.getString("FORM2_YEAR_END_YES_NO"));
                ObjFeltYearEndDiscDetails.setAttribute("FORM2_YEAR_END_REMARKS",resultSetTemp.getString("FORM2_YEAR_END_REMARKS"));
                ObjFeltYearEndDiscDetails.setAttribute("FORM2_YEAR_END_REMARKS2",resultSetTemp.getString("FORM2_YEAR_END_REMARKS2"));
                ObjFeltYearEndDiscDetails.setAttribute("FORM2_EFFECTIVE_FROM",resultSetTemp.getString("FORM2_EFFECTIVE_FROM"));
                ObjFeltYearEndDiscDetails.setAttribute("FORM2_EFFECTIVE_TO",resultSetTemp.getString("FORM2_EFFECTIVE_TO"));

                hmFeltCreditNoteDetails.put(Integer.toString(serialNoCounter),ObjFeltYearEndDiscDetails);
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
        String stringProductionDate1 =EITLERPGLOBAL.formatDate(stringProductionDate);
        
        
        try {
            stTmp=connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);

            rsTmp=stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_YEAR_END_DISCOUNT_HEADER_H_FORM2 WHERE FORM2_YEAR_END_ID='"+productionDocumentNo+"'");
            
            while(rsTmp.next()) {
                clsFeltYearEndDisc2 ObjFeltYearEndDisc = new clsFeltYearEndDisc2();
                

                ObjFeltYearEndDisc.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjFeltYearEndDisc.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjFeltYearEndDisc.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjFeltYearEndDisc.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjFeltYearEndDisc.setAttribute("REJECTED_REMARKS",rsTmp.getString("REJECTED_REMARKS"));
                
                hmHistoryList.put(Integer.toString(hmHistoryList.size()+1),ObjFeltYearEndDisc);
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
            resultSet=statement.executeQuery("SELECT DISTINCT REVISION_NO FROM PRODUCTION.FELT_YEAR_END_DISCOUNT_HEADER_H_FORM2 WHERE FORM2_YEAR_END_ID ='"+pDocNo+"'");
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
                strSQL="SELECT DISTINCT FORM2_YEAR_END_ID,FORM2_YEAR_END_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_YEAR_END_DISCOUNT_HEADER_FORM2, PRODUCTION.FELT_PROD_DOC_DATA WHERE FORM2_YEAR_END_ID=DOC_NO AND STATUS='W' AND MODULE_ID=738 AND CANCELED=0  AND USER_ID="+pUserID+"  ORDER BY RECEIVED_DATE,FORM2_YEAR_END_ID";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT DISTINCT FORM2_YEAR_END_ID,FORM2_YEAR_END_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_YEAR_END_DISCOUNT_HEADER_FORM2, PRODUCTION.FELT_PROD_DOC_DATA WHERE FORM2_YEAR_END_ID=DOC_NO AND STATUS='W' AND MODULE_ID=738 AND CANCELED=0  AND USER_ID="+pUserID+"  ORDER BY FORM2_YEAR_END_DATE,FORM2_YEAR_END_ID";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT DISTINCT FORM2_YEAR_END_ID,FORM2_YEAR_END_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_YEAR_END_DISCOUNT_HEADER_FORM2, PRODUCTION.FELT_PROD_DOC_DATA WHERE FORM2_YEAR_END_ID=DOC_NO AND STATUS='W' AND MODULE_ID=738 AND CANCELED=0  AND USER_ID="+pUserID+"  ORDER BY FORM2_YEAR_END_ID";
            }
            
            ResultSet rsTmp=data.getConn().createStatement().executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp.next()) {
                
                Counter=Counter+1;
                clsFeltYearEndDisc2 ObjDoc=new clsFeltYearEndDisc2();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO",rsTmp.getString("FORM2_YEAR_END_ID"));
                ObjDoc.setAttribute("DOC_DATE",rsTmp.getString("FORM2_YEAR_END_DATE"));
                ObjDoc.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                //ObjDoc.setAttribute("PARA_DESC",rsTmp.getString("PARA_DESC"));
               
                // ----------------- End of Header Fields ------------------------------------//
                
                List.put(Integer.toString(Counter),ObjDoc);
            }
            rsTmp.close();
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        
        return List;
    }
    
    
       public String getPartyName(String pPartyCode) {
        return data.getStringValueFromDB("SELECT PARTY_NAME FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PARTY_CODE='"+pPartyCode+"' ORDER BY INVOICE_DATE DESC");
       
    }
     public String getTrunOver(String pPartyCode) {
        return data.getStringValueFromDB("SELECT TRUN_OVER_TARGET FROM PRODUCTION.FELT_RATE_DISCOUNT_MASTER WHERE PARTY_CODE='"+pPartyCode+"'");
       
    }  
      
    
    public boolean checkPieceNoInDB1(String pPieceNo, String pAmend_Reason,String pProdindstring,String pAgreedindstring){
        int count=data.getIntValueFromDB("SELECT COUNT(FLT_PIECE_NO) FROM PRODUCTION.FELT_YEAR_END_DISCOUNT_HEADER WHERE FLT_PIECE_NO='"+pPieceNo+"' AND CN_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND CN_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
        if(count>=1){
          
            int counter=0;
            
            try{
                Connection Conn=data.getConn();
                Statement stTmp=Conn.createStatement();
                ResultSet rsTmp=stTmp.executeQuery("SELECT CN_DATE FROM PRODUCTION.FELT_YEAR_END_DISCOUNT_HEADER WHERE FLT_PIECE_NO='"+pPieceNo+"' AND CN_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND CN_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
                while(rsTmp.next()){
   //                 if(rsTmp.getString("CN_DATE").equals(EITLERPGLOBAL.formatDateDB(pProdDate))) counter++;
                }
            }catch(SQLException e){e.printStackTrace();}
            if(counter==1 && count>=2) return true;
            else if(counter==1) return false;
            else return true;
        }else return false;
    }
    
    // checks production date already exist in db
    public boolean checkProductionDateInDB(String pProdDate){
        int count=data.getIntValueFromDB("SELECT COUNT(CN_DATE) FROM PRODUCTION.FELT_YEAR_END_DISCOUNT_HEADER WHERE CN_DATE='"+pProdDate+"'");
        if(count>0) return true;
        else return false;
    }
    
    public boolean checkPartyCodeInDB(String pPartyCode){
        int count=data.getIntValueFromDB("SELECT COUNT(PARTY_CODE) FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE =210010 AND APPROVED =1 AND PARTY_CODE='"+pPartyCode+"'");
        if(count<=0) return true;
        else return false;
    }
    public boolean checkProductCodeInDB(String pProductCode){
        int count=data.getIntValueFromDB("SELECT COUNT(ITEM_CODE) FROM PRODUCTION.FELT_RATE_MASTER WHERE SUBSTRING(ITEM_CODE,1,6)='"+pProductCode+"'");
        if(count<=0) return true;
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
 
    
       public static HashMap getCreditNoteList() {
        HashMap List= new HashMap();
        try{
            Connection Conn=data.getConn();
            Statement stTmp=Conn.createStatement();
            int counter=1;
            ResultSet rsTmp=stTmp.executeQuery("SELECT 0 AS PARA_CODE,'SELECT CREDIT NOTE' AS PARA_DESC UNION ALL SELECT PARA_CODE,PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID = 'CREDIT_NOTE' ORDER BY PARA_CODE");
            while(rsTmp.next()){
                ComboData aData=new ComboData();
                aData.Code=rsTmp.getInt("PARA_CODE");
                aData.Text=rsTmp.getString("PARA_DESC");
                List.put(new Integer(counter++), aData);
            }
        }catch(SQLException e){
      //      LastError = e.getMessage();
            e.printStackTrace();
        }
        return List;
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
            strSQL="SELECT LAST_USED_NO,PADDING_BY,NO_LENGTH,PREFIX_CHARS,SUFFIX_CHARS FROM D_COM_FIRSTFREE WHERE COMPANY_ID=2 AND MODULE_ID=738 AND FIRSTFREE_NO=196";
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
                    data.Execute("UPDATE D_COM_FIRSTFREE SET LAST_USED_NO='"+strNewNo.trim()+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=2 AND MODULE_ID=738 AND FIRSTFREE_NO=196");
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
