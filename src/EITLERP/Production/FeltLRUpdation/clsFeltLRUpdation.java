/*
 * clsFeltLRUpdation.java
 *
 * Created on July 11, 2013, 12:10 PM
 */

package EITLERP.Production.FeltLRUpdation;

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
import EITLERP.clsDocMailer;
import EITLERP.Production.clsFeltProductionApprovalFlow;

/**
 *
 * @author  Vivek Kumar
 * @version
 */

public class clsFeltLRUpdation {
    
    public String LastError="";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    
    public HashMap props;
    //Felt LR Updation Details Collection
    public HashMap hmFeltLRUpdationDetails;
    
    //History Related properties
    public boolean HistoryView=false;
    private String historyDocumentNo="";
    
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
    
    /** Creates new DataFeltLRUpdation */
    public clsFeltLRUpdation() {
        LastError = "";
        props=new HashMap();
        props.put("INVOICE_NO", new Variant(""));
        props.put("INVOICE_DATE", new Variant(""));
        props.put("DOC_NO", new Variant(""));
        props.put("APPROVED",new Variant(0));
        props.put("REJECTED",new Variant(0));
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
        
        hmFeltLRUpdationDetails=new HashMap();
    }
    
    public boolean LoadData() {
        try {
            connection = data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet=statement.executeQuery("SELECT DISTINCT DOC_NO FROM PRODUCTION.FELT_INVOICE_EXTRA_DETAIL WHERE INVOICE_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND INVOICE_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY INVOICE_DATE");
            HistoryView=false;
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
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    //Navigation Methods
    public boolean MoveFirst() {
        try {
            resultSet.first();
            if(HistoryView) setHistoryData(historyDocumentNo);
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
            if(HistoryView) setHistoryData(historyDocumentNo);
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
            if(HistoryView) setHistoryData(historyDocumentNo);
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
            if(HistoryView) setHistoryData(historyDocumentNo);
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
        ResultSet  resultSetTemp,resultSetHistory;
        Statement  statementTemp, statementHistory;
        try {
            // Production data connection
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_INVOICE_EXTRA_DETAIL WHERE DOC_NO=''");
            
            // Production data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_INVOICE_EXTRA_DETAIL_H WHERE DOC_NO=''");
            
            //Now Insert records into production tables
            for(int i=1;i<=hmFeltLRUpdationDetails.size();i++) {
                clsFeltLRUpdationDetails ObjFeltLRUpdationDetails=(clsFeltLRUpdationDetails) hmFeltLRUpdationDetails.get(Integer.toString(i));
                
                //Insert records into LR Updation table
                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetTemp.updateString("INVOICE_NO", ObjFeltLRUpdationDetails.getAttribute("INVOICE_NO").getString());
                resultSetTemp.updateString("INVOICE_DATE",ObjFeltLRUpdationDetails.getAttribute("INVOICE_DATE").getString());
                resultSetTemp.updateString("PARTY_CODE",ObjFeltLRUpdationDetails.getAttribute("PARTY_CODE").getString());
                resultSetTemp.updateString("LR_NO",ObjFeltLRUpdationDetails.getAttribute("LR_NO").getString());
                resultSetTemp.updateString("LR_DATE",ObjFeltLRUpdationDetails.getAttribute("LR_DATE").getString());
                resultSetTemp.updateString("CARRIER",ObjFeltLRUpdationDetails.getAttribute("CARRIER").getString());
                resultSetTemp.updateDouble("FREIGHT", EITLERPGLOBAL.round(ObjFeltLRUpdationDetails.getAttribute("FREIGHT").getVal(),2));
                resultSetTemp.updateString("REMARKS",ObjFeltLRUpdationDetails.getAttribute("REMARKS").getString());
                resultSetTemp.updateInt("CREATED_BY",getAttribute("CREATED_BY").getInt());
                resultSetTemp.updateString("CREATED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetTemp.updateInt("MODIFIED_BY",0);
                resultSetTemp.updateString("MODIFIED_DATE","0000-00-00");
                resultSetTemp.updateBoolean("APPROVED",false);
                resultSetTemp.updateString("APPROVED_DATE","0000-00-00");
                resultSetTemp.updateBoolean("REJECTED",false);
                resultSetTemp.updateString("REJECTED_DATE","0000-00-00");
                resultSetTemp.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
                resultSetTemp.updateInt("CHANGED",1);
                resultSetTemp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetTemp.insertRow();
                
                //Insert records into production data history table
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO",1);
                resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetHistory.updateString("INVOICE_NO", ObjFeltLRUpdationDetails.getAttribute("INVOICE_NO").getString());
                resultSetHistory.updateString("INVOICE_DATE", ObjFeltLRUpdationDetails.getAttribute("INVOICE_DATE").getString());
                resultSetHistory.updateString("PARTY_CODE",ObjFeltLRUpdationDetails.getAttribute("PARTY_CODE").getString());
                resultSetHistory.updateString("LR_NO",ObjFeltLRUpdationDetails.getAttribute("LR_NO").getString());
                resultSetHistory.updateString("LR_DATE",ObjFeltLRUpdationDetails.getAttribute("LR_DATE").getString());
                resultSetHistory.updateString("CARRIER",ObjFeltLRUpdationDetails.getAttribute("CARRIER").getString());
                resultSetHistory.updateDouble("FREIGHT", EITLERPGLOBAL.round(ObjFeltLRUpdationDetails.getAttribute("FREIGHT").getVal(),2));
                resultSetHistory.updateString("REMARKS",ObjFeltLRUpdationDetails.getAttribute("REMARKS").getString());
                resultSetHistory.updateInt("UPDATED_BY",getAttribute("UPDATED_BY").getInt());
                resultSetHistory.updateString("ENTRY_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
                resultSetHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
                resultSetHistory.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
                resultSetHistory.updateInt("CHANGED",1);
                resultSetHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetHistory.insertRow();
            }
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=714;
            ObjFeltProductionApprovalFlow.DocNo=getAttribute("DOC_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=getAttribute("FROM").getInt();
            ObjFeltProductionApprovalFlow.To=getAttribute("TO").getInt();
            ObjFeltProductionApprovalFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.FELT_INVOICE_EXTRA_DETAIL";
            ObjFeltProductionApprovalFlow.IsCreator=true;
            ObjFeltProductionApprovalFlow.HierarchyID=getAttribute("HIERARCHY_ID").getInt();
            ObjFeltProductionApprovalFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName="DOC_NO";
            
            if(ObjFeltProductionApprovalFlow.Status.equals("H")) {
                ObjFeltProductionApprovalFlow.Status="A";
                ObjFeltProductionApprovalFlow.To=ObjFeltProductionApprovalFlow.From;
                ObjFeltProductionApprovalFlow.UpdateFlow();
            }else if(!ObjFeltProductionApprovalFlow.UpdateFlow()) {
                LastError=ObjFeltProductionApprovalFlow.LastError;
            }
            //--------- Approval Flow Update complete -----------
            
            // Update LR No in Invoice Table
            if(ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {
                ObjFeltProductionApprovalFlow.finalApproved=false;
                updateLRNo(getAttribute("DOC_NO").getString());
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
        ResultSet  resultSetTemp,resultSetHistory;
        Statement  statementTemp, statementHistory;
        int revisionNo =1;
        try {
            // Production data connection
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_INVOICE_EXTRA_DETAIL WHERE INVOICE_DATE=''");
            
            // Production data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            //Get the Maximum Revision No in History Table.
            resultSetHistory=statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_INVOICE_EXTRA_DETAIL_H WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
            resultSetHistory.first();
            if(resultSetHistory.getRow()>0){
                revisionNo=resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from production data table before insert
            statementHistory.execute("DELETE FROM PRODUCTION.FELT_INVOICE_EXTRA_DETAIL WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
            
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_INVOICE_EXTRA_DETAIL_H WHERE INVOICE_DATE=''");
            
            //Now Update records into production tables
            for(int i=1;i<=hmFeltLRUpdationDetails.size();i++) {
                clsFeltLRUpdationDetails ObjFeltLRUpdationDetails=(clsFeltLRUpdationDetails) hmFeltLRUpdationDetails.get(Integer.toString(i));
                
                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetTemp.updateString("INVOICE_NO", ObjFeltLRUpdationDetails.getAttribute("INVOICE_NO").getString());
                resultSetTemp.updateString("INVOICE_DATE", ObjFeltLRUpdationDetails.getAttribute("INVOICE_DATE").getString());
                resultSetTemp.updateString("PARTY_CODE",ObjFeltLRUpdationDetails.getAttribute("PARTY_CODE").getString());
                resultSetTemp.updateString("LR_NO",ObjFeltLRUpdationDetails.getAttribute("LR_NO").getString());
                resultSetTemp.updateString("LR_DATE",ObjFeltLRUpdationDetails.getAttribute("LR_DATE").getString());
                resultSetTemp.updateString("CARRIER",ObjFeltLRUpdationDetails.getAttribute("CARRIER").getString());
                resultSetTemp.updateDouble("FREIGHT", EITLERPGLOBAL.round(ObjFeltLRUpdationDetails.getAttribute("FREIGHT").getVal(),2));
                resultSetTemp.updateString("REMARKS",ObjFeltLRUpdationDetails.getAttribute("REMARKS").getString());
                resultSetTemp.updateInt("CREATED_BY",getAttribute("CREATED_BY").getInt());
                resultSetTemp.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                resultSetTemp.updateInt("MODIFIED_BY",getAttribute("MODIFIED_BY").getInt());
                resultSetTemp.updateString("MODIFIED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetTemp.updateBoolean("APPROVED",false);
                resultSetTemp.updateString("APPROVED_DATE","0000-00-00");
                resultSetTemp.updateBoolean("REJECTED",false);
                resultSetTemp.updateString("REJECTED_DATE","0000-00-00");
                resultSetTemp.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
                resultSetTemp.updateInt("CHANGED",1);
                resultSetTemp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetTemp.insertRow();
                
                //Insert records into production data history table
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO",revisionNo);
                resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetHistory.updateString("INVOICE_NO", ObjFeltLRUpdationDetails.getAttribute("INVOICE_NO").getString());
                resultSetHistory.updateString("INVOICE_DATE", ObjFeltLRUpdationDetails.getAttribute("INVOICE_DATE").getString());
                resultSetHistory.updateString("PARTY_CODE",ObjFeltLRUpdationDetails.getAttribute("PARTY_CODE").getString());
                resultSetHistory.updateString("LR_NO",ObjFeltLRUpdationDetails.getAttribute("LR_NO").getString());
                resultSetHistory.updateString("LR_DATE",ObjFeltLRUpdationDetails.getAttribute("LR_DATE").getString());
                resultSetHistory.updateString("CARRIER",ObjFeltLRUpdationDetails.getAttribute("CARRIER").getString());
                resultSetHistory.updateDouble("FREIGHT", EITLERPGLOBAL.round(ObjFeltLRUpdationDetails.getAttribute("FREIGHT").getVal(),2));
                resultSetHistory.updateString("REMARKS",ObjFeltLRUpdationDetails.getAttribute("REMARKS").getString());
                resultSetHistory.updateInt("UPDATED_BY",getAttribute("UPDATED_BY").getInt());
                resultSetHistory.updateString("ENTRY_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
                resultSetHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
                resultSetHistory.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
                resultSetHistory.updateInt("CHANGED",1);
                resultSetHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetHistory.insertRow();
            }
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=714;
            ObjFeltProductionApprovalFlow.DocNo=getAttribute("DOC_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=getAttribute("FROM").getInt();
            ObjFeltProductionApprovalFlow.To=getAttribute("TO").getInt();
            ObjFeltProductionApprovalFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.FELT_INVOICE_EXTRA_DETAIL";
            ObjFeltProductionApprovalFlow.IsCreator=false;
            ObjFeltProductionApprovalFlow.HierarchyID=getAttribute("HIERARCHY_ID").getInt();
            ObjFeltProductionApprovalFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName="DOC_NO";
            
            if(getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFeltProductionApprovalFlow.To=getAttribute("SEND_DOC_TO").getInt();
                ObjFeltProductionApprovalFlow.ExplicitSendTo=true;
            }
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.FELT_INVOICE_EXTRA_DETAIL SET REJECTED=0,CHANGED=1 WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
                //Remove Old Records from FELT DOCUMET APPROVAL TABLE
                //data.Execute("DELETE FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=714 AND DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
                
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
            
            // Update LR No in Invoice Table
            if(ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {
                ObjFeltProductionApprovalFlow.finalApproved=false;
                updateLRNo(getAttribute("DOC_NO").getString());
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
    public boolean CanDelete(String documentNo, int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            tmpStmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_INVOICE_EXTRA_DETAIL WHERE DOC_NO='"+documentNo +"' AND APPROVED=1";
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if(rsTmp.getInt("COUNT")>0){  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            }else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=714 AND USER_ID="+userID+" AND DOC_NO='"+ documentNo +"' AND STATUS='W'";
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getInt("COUNT")>0) {
                    strSQL = "DELETE FROM PRODUCTION.FELT_INVOICE_EXTRA_DETAIL WHERE DOC_NO='" + documentNo + "'";
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
    public boolean IsEditable(String documentNo,int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        try {
            if(HistoryView) {
                return false;
            }
            tmpStmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_INVOICE_EXTRA_DETAIL WHERE DOC_NO='"+ documentNo +"' AND APPROVED=1";
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0){  //Item is Approved
                //Discard the request
                return false;
            }else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=714 AND USER_ID="+userID+" AND DOC_NO='"+ documentNo +"' AND STATUS='W'";
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
        try {
            String strSql = "SELECT DISTINCT DOC_NO FROM PRODUCTION.FELT_INVOICE_EXTRA_DETAIL WHERE " + stringFindQuery;
            connection=data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet=statement.executeQuery(strSql);
            if(!resultSet.first()) {
                LoadData();
                return false;
            }else {
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
        try {
            setAttribute("REVISION_NO",0);
            setAttribute("DOC_NO",resultSet.getString("DOC_NO"));
            
            //Now Populate the collection, first clear the collection
            hmFeltLRUpdationDetails.clear();
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_INVOICE_EXTRA_DETAIL WHERE DOC_NO='"+ resultSet.getString("DOC_NO") +"'");
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
                setAttribute("INVOICE_DATE",resultSetTemp.getDate("INVOICE_DATE"));
                setAttribute("CREATED_BY",resultSetTemp.getInt("CREATED_BY"));
                setAttribute("CREATED_DATE",resultSetTemp.getString("CREATED_DATE"));
                setAttribute("MODIFIED_BY",resultSetTemp.getInt("MODIFIED_BY"));
                setAttribute("MODIFIED_DATE",resultSetTemp.getString("MODIFIED_DATE"));
                setAttribute("APPROVED",resultSetTemp.getInt("APPROVED"));
                setAttribute("APPROVED_DATE",resultSetTemp.getString("APPROVED_DATE"));
                setAttribute("REJECTED",resultSetTemp.getBoolean("REJECTED"));
                setAttribute("REJECTED_DATE",resultSetTemp.getString("REJECTED_DATE"));
                setAttribute("HIERARCHY_ID",resultSetTemp.getInt("HIERARCHY_ID"));
                
                clsFeltLRUpdationDetails ObjFeltLRUpdationDetails = new clsFeltLRUpdationDetails();
                
                ObjFeltLRUpdationDetails.setAttribute("INVOICE_NO",resultSetTemp.getString("INVOICE_NO"));
                ObjFeltLRUpdationDetails.setAttribute("INVOICE_DATE",resultSetTemp.getDate("INVOICE_DATE"));
                ObjFeltLRUpdationDetails.setAttribute("PARTY_CODE",resultSetTemp.getString("PARTY_CODE"));
                ObjFeltLRUpdationDetails.setAttribute("LR_NO",resultSetTemp.getString("LR_NO"));
                ObjFeltLRUpdationDetails.setAttribute("LR_DATE",resultSetTemp.getString("LR_DATE"));
                ObjFeltLRUpdationDetails.setAttribute("CARRIER",resultSetTemp.getString("CARRIER"));
                ObjFeltLRUpdationDetails.setAttribute("FREIGHT",resultSetTemp.getFloat("FREIGHT"));
                ObjFeltLRUpdationDetails.setAttribute("REMARKS",resultSetTemp.getString("REMARKS"));
                
                hmFeltLRUpdationDetails.put(Integer.toString(serialNoCounter),ObjFeltLRUpdationDetails);
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
    
    public boolean setHistoryData(String docNo) {
        ResultSet  resultSetTemp;
        Statement  statementTemp;
        int serialNoCounter=0;
        try {
            setAttribute("REVISION_NO",resultSet.getInt("REVISION_NO"));
            
            //Now Populate the collection, first clear the collection
            hmFeltLRUpdationDetails.clear();
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_INVOICE_EXTRA_DETAIL_H WHERE DOC_NO='"+docNo+"' AND REVISION_NO='"+ resultSet.getInt("REVISION_NO") +"' ORDER BY REVISION_NO");
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
                setAttribute("DOC_NO",resultSetTemp.getString("DOC_NO"));
                setAttribute("INVOICE_DATE",resultSetTemp.getString("INVOICE_DATE"));
                setAttribute("UPDATED_BY",resultSetTemp.getInt("UPDATED_BY"));
                setAttribute("ENTRY_DATE",resultSetTemp.getString("ENTRY_DATE"));
                setAttribute("HIERARCHY_ID",resultSetTemp.getInt("HIERARCHY_ID"));
                
                clsFeltLRUpdationDetails ObjFeltLRUpdationDetails = new clsFeltLRUpdationDetails();
                
                ObjFeltLRUpdationDetails.setAttribute("INVOICE_NO",resultSetTemp.getString("INVOICE_NO"));
                ObjFeltLRUpdationDetails.setAttribute("INVOICE_DATE",resultSet.getDate("INVOICE_DATE"));
                ObjFeltLRUpdationDetails.setAttribute("PARTY_CODE",resultSetTemp.getString("PARTY_CODE"));
                ObjFeltLRUpdationDetails.setAttribute("LR_NO",resultSetTemp.getString("LR_NO"));
                ObjFeltLRUpdationDetails.setAttribute("LR_DATE",resultSetTemp.getString("LR_DATE"));
                ObjFeltLRUpdationDetails.setAttribute("CARRIER",resultSetTemp.getString("CARRIER"));
                ObjFeltLRUpdationDetails.setAttribute("FREIGHT",resultSetTemp.getFloat("FREIGHT"));
                ObjFeltLRUpdationDetails.setAttribute("REMARKS",resultSetTemp.getString("REMARKS"));
                
                hmFeltLRUpdationDetails.put(Integer.toString(serialNoCounter),ObjFeltLRUpdationDetails);
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
    
    public static HashMap getHistoryList(String documentNo) {
        HashMap hmHistoryList=new HashMap();
        Statement stTmp;
        ResultSet rsTmp;
        try {
            stTmp=connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp=stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_INVOICE_EXTRA_DETAIL_H WHERE DOC_NO='"+documentNo+"' GROUP BY REVISION_NO");
            
            while(rsTmp.next()) {
                clsFeltLRUpdation ObjFeltLRUpdation=new clsFeltLRUpdation();
                
                ObjFeltLRUpdation.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjFeltLRUpdation.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjFeltLRUpdation.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjFeltLRUpdation.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjFeltLRUpdation.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                hmHistoryList.put(Integer.toString(hmHistoryList.size()+1),ObjFeltLRUpdation);
            }
            
            rsTmp.close();
            stTmp.close();
            return hmHistoryList;
        }catch(Exception e) {
            e.printStackTrace();
            return hmHistoryList;
        }
    }
    
    public boolean ShowHistory(String docNo) {
        try {
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            resultSet=statement.executeQuery("SELECT DISTINCT REVISION_NO FROM PRODUCTION.FELT_INVOICE_EXTRA_DETAIL_H WHERE DOC_NO='"+docNo+"'");
            historyDocumentNo = docNo;
            HistoryView=true;
            MoveFirst();
            return true;
        }catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public HashMap getPendingLRData(String invoiceDate) {
        HashMap pendingLRList=new HashMap();
        ResultSet  resultSetTemp;
        Statement  statementTemp;
        try {
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT INVOICE_NO, PARTY_CODE FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_DATE='"+invoiceDate+"' AND INVOICE_NO NOT IN(SELECT INVOICE_NO FROM PRODUCTION.FELT_INVOICE_EXTRA_DETAIL WHERE INVOICE_DATE='"+invoiceDate+"')");
            
            while(resultSetTemp.next()) {
                clsFeltLRUpdation ObjFeltLRUpdation=new clsFeltLRUpdation();
                ObjFeltLRUpdation.setAttribute("INVOICE_NO",resultSetTemp.getString("INVOICE_NO"));
                ObjFeltLRUpdation.setAttribute("PARTY_CODE",resultSetTemp.getString("PARTY_CODE"));
                pendingLRList.put(Integer.toString(pendingLRList.size()+1),ObjFeltLRUpdation);
            }
            resultSetTemp.close();
            statementTemp.close();
            return pendingLRList;
        }catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return pendingLRList;
        }
    }
    
    // checks piece no already exist in db
    public boolean checkInvoiceNoInDB(String invoiceNo){
        int count=data.getIntValueFromDB("SELECT COUNT(INVOICE_NO) FROM PRODUCTION.FELT_INVOICE_EXTRA_DETAIL WHERE INVOICE_NO='"+invoiceNo+"' AND INVOICE_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND INVOICE_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
        if(count>0) return true;
        else return false;
    }
    
    public boolean checkInvoiceNoInDB(String invoiceNo, String invoiceDate){
        int count=data.getIntValueFromDB("SELECT COUNT(INVOICE_NO) FROM PRODUCTION.FELT_INVOICE_EXTRA_DETAIL WHERE INVOICE_NO='"+invoiceNo+"' AND INVOICE_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND INVOICE_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
        if(count>=1){
            int counter=0;
            try{
                Connection Conn=data.getConn();
                Statement stTmp=Conn.createStatement();
                ResultSet rsTmp=stTmp.executeQuery("SELECT INVOICE_DATE FROM PRODUCTION.FELT_INVOICE_EXTRA_DETAIL WHERE INVOICE_NO='"+invoiceNo+"' AND INVOICE_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND INVOICE_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
                while(rsTmp.next()){
                    if(rsTmp.getString("INVOICE_DATE").equals(EITLERPGLOBAL.formatDateDB(invoiceDate))) counter++;
                }
            }catch(SQLException e){e.printStackTrace();}
            if(counter==1 && count>=2) return true;
            else if(counter==1) return false;
            else return true;
        }else return false;
    }
    
    public String getDocumentNo(String invoiceDate){
        String docDate=invoiceDate.substring(6,10)+ invoiceDate.substring(3,5)+ invoiceDate.substring(0,2);
        return "FLR"+docDate+data.getStringValueFromDB("SELECT COALESCE(MAX(CAST(SUBSTRING(DOC_NO,12) AS UNSIGNED)),0)+1 FROM PRODUCTION.FELT_INVOICE_EXTRA_DETAIL WHERE SUBSTRING(DOC_NO,4,8)='"+docDate+"'"); 
    }
            
    public String[] getPartyDetails(String partyCode){
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        String[] partyDetails =new String[2];
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
//            rsTmp=Stmt.executeQuery("SELECT * FROM(SELECT PARTY_CODE, TRIM(PARTY_NAME) PARTY_NAME FROM D_SAL_PARTY_MASTER  WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE+0='"+partyCode+"' ) P LEFT JOIN (SELECT PARTY_CODE, TRIM(EMAIL) EMAIL FROM PRODUCTION.FELT_PARTY_EXTRA_INFO WHERE PARTY_CODE+0='"+partyCode+"') I ON P.PARTY_CODE=I.PARTY_CODE");
            rsTmp=Stmt.executeQuery("SELECT PARTY_CODE, TRIM(PARTY_NAME) PARTY_NAME, CONCAT(EMAIL,',',EMAIL_ID2,',',EMAIL_ID3) EMAIL FROM D_SAL_PARTY_MASTER  WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE+0='"+partyCode+"'");
            partyDetails[1]="";
            int counter=0;
            while(rsTmp.next()) {
                partyDetails[0] = rsTmp.getString("PARTY_NAME");
                String email = rsTmp.getString("EMAIL");
                if(!email.equals("")){
                    if(counter!=0)partyDetails[1] +=","+email;
                    else partyDetails[1] +=email;
                    counter++;
                }
            }
            
            Stmt.close();
            rsTmp.close();
            
            return partyDetails;
        }
        catch(Exception e) {
            e.printStackTrace();
            return partyDetails;
        }
    }
    
    public String createMailBody(String partyCode, String invoiceDate, String despatchNote){
        Connection tmpConn;
        Statement stmt;
        ResultSet rsTmp;
        
        String mailbody="<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>" +
        "<html xmlns='http://www.w3.org/TR/REC-html40'>"+
        "<head>"+
        "<style><!--"+
        "/* Font Definitions */"+
        "@font-face{font-family:Calibri;panose-1:2 15 5 2 2 2 4 3 2 4;}"+
        "@font-face{font-family:Tahoma;panose-1:2 11 6 4 3 5 4 4 2 4;}"+
        "@font-face{font-family:Garamond;panose-1:2 2 4 4 3 3 1 1 8 3;}"+
        "@font-face{font-family:Verdana;panose-1:2 11 6 4 3 5 4 4 2 4;}"+
        "@font-face{font-family:Webdings;panose-1:5 3 1 2 1 5 9 6 7 3;}"+
        "/* Style Definitions */"+
        "p.MsoNormal, li.MsoNormal, div.MsoNormal{margin:0in;margin-bottom:.0001pt;font-size:12.0pt;font-family:'Times New Roman','serif';}"+
        "a:link, span.MsoHyperlink{mso-style-priority:99;color:blue;text-decoration:underline;}"+
        "a:visited, span.MsoHyperlinkFollowed{mso-style-priority:99;color:purple;text-decoration:underline;}"+
        "p{mso-style-priority:99;margin:0in;margin-bottom:.0001pt;font-size:12.0pt;font-family:'Times New Roman','serif';}"+
        "span.apple-style-span{mso-style-name:apple-style-span;}"+
        "span.EmailStyle20{mso-style-type:personal-reply;font-family:'Calibri','sans-serif';color:#1F497D;}"+
        ".MsoChpDefault{mso-style-type:export-only;font-size:10.0pt;}"+
        "--></style>"+
        "</head>"+
        "<body lang='EN-US'>"+
        "<p><span style='color:black'>Dear Sir/Madam,</span></p><br>"+
        "<p><span style='color:black'>This is to inform you that your following Felt(s) have been despatched &amp; the despatch documents are being sent to you as usual.</span></p>"+
        "<table class='MsoNormalTable' border='1' cellspacing='0' cellpadding='0' width='100%' style='width:100.0%;border-collapse:collapse;border:solid black 1.0pt'>"+
        "<tr>"+
        "<th width='12%' style='width:12.0%;border:solid black 1.0pt;padding:2.25pt 2.25pt 2.25pt 2.25pt'>"+
        "<p class='MsoNormal'>PIECE NO</p>"+
        "</th>"+
        "<th width='12%' style='width:12.0%;border:solid black 1.0pt;padding:2.25pt 2.25pt 2.25pt 2.25pt'>"+
        "<p class='MsoNormal'>SIZE(MTRS)</p>"+
        "</th>"+
        "<th width='12%' style='width:12.0%;border:solid black 1.0pt;padding:2.25pt 2.25pt 2.25pt 2.25pt'>"+
        "<p class='MsoNormal'>INVOICE NO</p>"+
        "</th>"+
        "<th width='12%' style='width:12.0%;border:solid black 1.0pt;padding:2.25pt 2.25pt 2.25pt 2.25pt'>"+
        "<p class='MsoNormal'>INVOICE DATE</p>"+
        "</th>"+
        "<th width='12%' style='width:12.0%;border:solid black 1.0pt;padding:2.25pt 2.25pt 2.25pt 2.25pt'>"+
        "<p class='MsoNormal'>AMOUNT(RS)</p>"+
        "</th>"+
        "<th width='12%' style='width:12.0%;border:solid black 1.0pt;padding:2.25pt 2.25pt 2.25pt 2.25pt'>"+
        "<p class='MsoNormal'>LR NO</p>"+
        "</th>"+
        "<th width='12%' style='width:12.0%;border:solid black 1.0pt;padding:2.25pt 2.25pt 2.25pt 2.25pt'>"+
        "<p class='MsoNormal'>LR DATE</p>"+
        "</th>"+
        "<th width='12%' style='width:12.0%;border:solid black 1.0pt;padding:2.25pt 2.25pt 2.25pt 2.25pt'>"+
        "<p class='MsoNormal'>TRANSPORTER</p>"+
        "</th>"+
        "</tr>";
        
        try {
            tmpConn=data.getConn();
            stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=stmt.executeQuery("SELECT * FROM (SELECT INVOICE_NO , INVOICE_DATE, LR_NO, LR_DATE, CARRIER FROM PRODUCTION.FELT_INVOICE_EXTRA_DETAIL WHERE APPROVED =1 AND INVOICE_DATE='"+EITLERPGLOBAL.formatDateDB(invoiceDate)+"' AND PARTY_CODE+0='"+partyCode+"') D LEFT JOIN (SELECT INVOICE_NO , INVOICE_DATE, PIECE_NO, CONCAT(LENGTH ,'X',WIDTH) SIZE, NET_AMOUNT FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_DATE='"+EITLERPGLOBAL.formatDateDB(invoiceDate)+"' AND PARTY_CODE+0='"+partyCode+"') I ON D.INVOICE_DATE=I.INVOICE_DATE AND D.INVOICE_NO=I.INVOICE_NO");
            
            while(rsTmp.next()) {
                mailbody+="<tr>"+
                "<td width='12%' style='width:12.0%;border:solid black 1.0pt;padding:2.25pt 2.25pt 2.25pt 2.25pt'>"+
                "<p class='MsoNormal'>"+rsTmp.getString("PIECE_NO")+"</p>"+
                "</td>"+
                "<td width='12%' style='width:12.0%;border:solid black 1.0pt;padding:2.25pt 2.25pt 2.25pt 2.25pt'>"+
                "<p class='MsoNormal'>"+rsTmp.getString("SIZE")+"</p>"+
                "</td>"+
                "<td width='12%' style='width:12.0%;border:solid black 1.0pt;padding:2.25pt 2.25pt 2.25pt 2.25pt'>"+
                "<p class='MsoNormal'>"+rsTmp.getString("INVOICE_NO")+"</p>"+
                "</td>"+
                "<td width='12%' style='width:12.0%;border:solid black 1.0pt;padding:2.25pt 2.25pt 2.25pt 2.25pt'>"+
                "<p class='MsoNormal'>"+EITLERPGLOBAL.formatDate(rsTmp.getString("INVOICE_DATE"))+"</p>"+
                "</td>"+
                "<td width='12%' style='width:12.0%;border:solid black 1.0pt;padding:2.25pt 2.25pt 2.25pt 2.25pt'>"+
                "<p class='MsoNormal'>"+rsTmp.getInt("NET_AMOUNT")+"</p>"+
                "</td>"+
                "<td width='12%' style='width:12.0%;border:solid black 1.0pt;padding:2.25pt 2.25pt 2.25pt 2.25pt'>"+
                "<p class='MsoNormal'>"+rsTmp.getString("LR_NO")+"</p>"+
                "</td>"+
                "<td width='12%' style='width:12.0%;border:solid black 1.0pt;padding:2.25pt 2.25pt 2.25pt 2.25pt'>"+
                "<p class='MsoNormal'>"+EITLERPGLOBAL.formatDate(rsTmp.getString("LR_DATE"))+"</p>"+
                "</td>"+
                "<td width='12%' style='width:12.0%;border:solid black 1.0pt;padding:2.25pt 2.25pt 2.25pt 2.25pt'>"+
                "<p class='MsoNormal'>"+rsTmp.getString("CARRIER")+"</p>"+
                "</td>"+
                "</tr>";
            }
            
            stmt.close();
            rsTmp.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
        
        mailbody+="</table><br><p><span style='color:black'>Note:</span></p>"+despatchNote+
        "<p><span style='color:black;margin-bottom:12.0pt'><br>Kindly confirm receipt</span></p><br>"+
        "<p class='MsoNormal' style='mso-margin-top-alt:auto;mso-margin-bottom-alt:auto'><span style='color:black'>Thanks &amp; Regards,</span></p>"+
        "<p class='MsoNormal' style='mso-margin-top-alt:auto;mso-margin-bottom-alt:auto'><span style='color:#000099'>(Despatch department)</span></p>"+
        "<p class='MsoNormal' style='mso-margin-top-alt:auto;mso-margin-bottom-alt:auto'><strong><span style='font-size:18.0pt;font-family:&quot;Garamond&quot;,&quot;serif&quot;;color:#000099'>Felt sales Division</span></strong></p>"+
        "<p class='MsoNormal' style='mso-margin-top-alt:auto;mso-margin-bottom-alt:auto'><span style='color:black'>&nbsp;</span></p>"+
        "<p class='MsoNormal' style='mso-margin-top-alt:auto;mso-margin-bottom-alt:auto'><span style='color:black'><img src='http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Images/feltIcon.jpg' alt='Felt Icon' width='169' height='193'></span></p>"+
        "<p class='MsoNormal' style='mso-margin-top-alt:auto;mso-margin-bottom-alt:auto'><span style='color:black'>&nbsp;</span><span style='font-size:3.0pt;font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;color:#7F7F7F'>Office: +91 265 2960060/61/62/63/66 | Fax: +91 265 2336195 |</span></p>"+
        "<p class='MsoNormal' style='mso-margin-top-alt:auto;mso-margin-bottom-alt:auto'><span style='font-size:3.0pt;font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;color:#7F7F7F'>Email: <a href='mailto:felts@dineshmills.com' target='_blank'>felts@dineshmills.com</a> | Website: <a href='http://felts.dineshmills.com' target='_blank'>www.dineshmills.com</a><br><span class='apple-style-span'>P O Box-2501, Padra Road, Vadodara 390 020, Gujarat, India</span></span></p>"+
        "<p class='MsoNormal' style='mso-margin-top-alt:auto;mso-margin-bottom-alt:auto'><span style='color:black'>&nbsp;</span></p>"+
        "<p class='MsoNormal' style='mso-margin-top-alt:auto;mso-margin-bottom-alt:auto'><span style='font-size:3.0pt;color:navy'>The information and attachment(s) contained by this e-mail are confidential, proprietary and legally privileged data of Shri Dinesh Mills Limited that is intended for use only by the addressee. If you are not the intended recipient, you are notified that any dissemination, distribution, or copying of this e-mail is strictly prohibited and requested to delete this e-mail immediately and notify the originator. While this e-mail has been checked for all known viruses, the addressee should also scan for viruses. Internet communications cannot be guaranteed to be timely, secure, error or virus-free as information could be intercepted, corrupted, lost, destroyed, arrive late or incomplete. SHRI DINESH MILLS LTD does not accept liability for any errors or omissions.</span></p>"+
        "<p class='MsoNormal' style='mso-margin-top-alt:auto;mso-margin-bottom-alt:auto'><span style='color:black'>&nbsp;</span></p>"+
        "<p><span><img border='0' width='28' height='25' src='http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Images/savetree.jpg' alt='Save Tree'></span><b><span style='font-size:4.0pt;color:green'>Please consider your environmental responsibility. Do not print this e-mail unless you really need to.</span></b></p>"+
        "</body>"+
        "</html>";
        
        return mailbody;
    }
    
    // Saves mail details in db
    public long saveMailDetail(String toEmailId, String message){
        HashMap sendList=new HashMap();
        String strEMail="";
        for(int i=0;i<=toEmailId.length()-1;i++) {
            if(toEmailId.substring(i,i+1).equals(",")) {
                if(!strEMail.trim().equals("")) {
                    sendList.put(Integer.toString(sendList.size()+1),strEMail);
                }
                strEMail="";
            }else {
                strEMail=strEMail+toEmailId.substring(i,i+1);
            }
        }
        
        //Last Element must be included
        if(!strEMail.trim().equals("")) {
            sendList.put(Integer.toString(sendList.size()+1),strEMail);
        }
        
        try {
            //Now insert the record into doc mailer database
            clsDocMailer ObjMailer=new clsDocMailer();
            ObjMailer.setAttribute("DOC_DATE",EITLERPGLOBAL.getCurrentDateDB());
            ObjMailer.setAttribute("MODULE_ID",714);
            ObjMailer.setAttribute("SENT_BY",EITLERPGLOBAL.gNewUserID);
            ObjMailer.setAttribute("DESCRIPTION",message);
            ObjMailer.setAttribute("FROM","felts@dineshmills.com");
            ObjMailer.setAttribute("SUBJECT","Felt Despatch Details");
            ObjMailer.setAttribute("MAIL_DOC_NO","");
            ObjMailer.setAttribute("CC","");
            //ObjMailer.setAttribute("BCC","felts@dineshmills.com");
            ObjMailer.setAttribute("BCC","");
            
            for(int i=1;i<=sendList.size();i++) {
                String email=(String)sendList.get(Integer.toString(i));
                ObjMailer.colEmail.put(Integer.toString(ObjMailer.colEmail.size()+1),email);
            }
            return ObjMailer.Insert();
        }catch(Exception e) {
            LastError=e.getMessage();
            e.printStackTrace();
            return 0;
        }
    }
    
    // Updates LR No in invoice Table
    private void updateLRNo(String docNo){
        data.Execute("UPDATE D_SAL_INVOICE_HEADER H, PRODUCTION.FELT_INVOICE_EXTRA_DETAIL D SET H.LR_NO=D.LR_NO, H.CHANGED=1, H.CHANGED_DATE=CURDATE() WHERE D.INVOICE_NO=H.INVOICE_NO AND D.INVOICE_DATE=H.INVOICE_DATE AND D.PARTY_CODE=H.PARTY_CODE AND D.APPROVED=1 AND D.DOC_NO='"+docNo+"'");
    }
    
}
