/*
 * clsFeltFinishing.java
 *
 * Created on August 22, 2013, 11:20 AM
 */

package EITLERP.Production.FeltFinishing;

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
import EITLERP.Production.Production_Date_Updation;
import EITLERP.Production.clsFeltProductionApprovalFlow;

/**
 *
 * @author  Vivek Kumar
 */

public class clsFeltFinishing {
    
    public String LastError="";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    
    public HashMap props;
    public boolean Ready = false;
    //Felt Production Finishing Details Collection
    public HashMap hmFeltFinishingDetails;
    
    //History Related properties
    public boolean HistoryView=false;
    private String historyProductionDate="";
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
    
    /** Creates new DataFeltProductionFinishing */
    public clsFeltFinishing() {
        LastError = "";
        props=new HashMap();
        props.put("PRODUCTION_DATE", new Variant(""));
        props.put("PRODUCTION_DOCUMENT_NO", new Variant(""));
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
        
        hmFeltFinishingDetails=new HashMap();
    }
    
    public boolean LoadData() {
        Ready=false;
        try {
            connection = data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet=statement.executeQuery("SELECT DISTINCT PROD_DATE FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'FINISHING' AND PROD_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PROD_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY PROD_DATE");
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
            if(HistoryView) setHistoryData(historyProductionDate, historyDocumentNo);
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
            if(HistoryView) setHistoryData(historyProductionDate, historyDocumentNo);
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
            if(HistoryView) setHistoryData(historyProductionDate, historyDocumentNo);
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
            if(HistoryView) setHistoryData(historyProductionDate, historyDocumentNo);
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
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DATE=''");
            
            // Production data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM PRODUCTION.`FELT_PROD_DATA_H` WHERE PROD_DATE=''");
            
            //Now Insert records into production tables
            for(int i=1;i<=hmFeltFinishingDetails.size();i++) {
                clsFeltFinishingDetails ObjFeltFinishingDetails=(clsFeltFinishingDetails) hmFeltFinishingDetails.get(Integer.toString(i));
                
                //Insert records into production data table
                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateString("PROD_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()));
                resultSetTemp.updateString("PROD_DOC_NO", getAttribute("PRODUCTION_DOCUMENT_NO").getString());
                resultSetTemp.updateString("PROD_FORM_NO", getAttribute("PRODUCTION_FORM_NO").getString());
                resultSetTemp.updateString("PROD_DEPT","FINISHING");
                resultSetTemp.updateInt("SR_NO",i);
                resultSetTemp.updateString("PROD_PIECE_NO",ObjFeltFinishingDetails.getAttribute("PRODUCTION_PIECE_NO").getString());
                resultSetTemp.updateString("PROD_PARTY_CODE",ObjFeltFinishingDetails.getAttribute("PRODUCTION_PARTY_CODE").getString());
                resultSetTemp.updateFloat("LENGTH",(float)EITLERPGLOBAL.round(ObjFeltFinishingDetails.getAttribute("LENGTH").getVal(),2));
                resultSetTemp.updateFloat("WIDTH",(float)EITLERPGLOBAL.round(ObjFeltFinishingDetails.getAttribute("WIDTH").getVal(),2));
                resultSetTemp.updateFloat("WEIGHT", (float)EITLERPGLOBAL.round(ObjFeltFinishingDetails.getAttribute("WEIGHT").getVal(),2));
                resultSetTemp.updateString("REMARKS",ObjFeltFinishingDetails.getAttribute("REMARKS").getString());
                resultSetTemp.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
                resultSetTemp.updateString("CREATED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetTemp.updateInt("MODIFIED_BY",0);
                resultSetTemp.updateString("MODIFIED_DATE","0000-00-00");
                resultSetTemp.updateBoolean("APPROVED",false);
                resultSetTemp.updateString("APPROVED_DATE","0000-00-00");
                resultSetTemp.updateBoolean("REJECTED",false);
                resultSetTemp.updateString("REJECTED_DATE","0000-00-00");
                resultSetTemp.updateInt("CANCELED",0);
                resultSetTemp.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
                resultSetTemp.updateInt("CHANGED",1);
                resultSetTemp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetTemp.insertRow();
                
                //Insert records into production data history table
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO",1);
                resultSetHistory.updateString("PROD_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()));
                resultSetHistory.updateString("PROD_DOC_NO", getAttribute("PRODUCTION_DOCUMENT_NO").getString());
                resultSetHistory.updateString("PROD_FORM_NO", getAttribute("PRODUCTION_FORM_NO").getString());
                resultSetHistory.updateString("PROD_DEPT","FINISHING");
                resultSetHistory.updateInt("SR_NO",i);
                resultSetHistory.updateString("PROD_PIECE_NO",ObjFeltFinishingDetails.getAttribute("PRODUCTION_PIECE_NO").getString());
                resultSetHistory.updateString("PROD_PARTY_CODE",ObjFeltFinishingDetails.getAttribute("PRODUCTION_PARTY_CODE").getString());
                resultSetHistory.updateFloat("LENGTH",(float)EITLERPGLOBAL.round(ObjFeltFinishingDetails.getAttribute("LENGTH").getVal(),2));
                resultSetHistory.updateFloat("WIDTH",(float)EITLERPGLOBAL.round(ObjFeltFinishingDetails.getAttribute("WIDTH").getVal(),2));
                resultSetHistory.updateFloat("WEIGHT", (float)EITLERPGLOBAL.round(ObjFeltFinishingDetails.getAttribute("WEIGHT").getVal(),2));
                resultSetHistory.updateString("REMARKS",ObjFeltFinishingDetails.getAttribute("REMARKS").getString());
                resultSetHistory.updateInt("UPDATED_BY",(int)getAttribute("UPDATED_BY").getVal());
                resultSetHistory.updateString("ENTRY_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
                resultSetHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
                resultSetHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
                resultSetHistory.updateInt("CHANGED",1);
                resultSetHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetHistory.insertRow();
            }
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=716; //Felt Finishing Entry
            ObjFeltProductionApprovalFlow.DocNo=getAttribute("PRODUCTION_DOCUMENT_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName="FELT_PROD_DATA";
            ObjFeltProductionApprovalFlow.IsCreator=true;
            ObjFeltProductionApprovalFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName="PROD_DOC_NO";
            
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
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DATE=''");
            
            // Production data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            //Get the Maximum Revision No in History Table.
            resultSetHistory=statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_PROD_DATA_H WHERE PROD_DEPT = 'FINISHING' AND PROD_DATE='"+EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString())+"' AND PROD_DOC_NO='"+getAttribute("PRODUCTION_DOCUMENT_NO").getString()+"'");
            resultSetHistory.first();
            if(resultSetHistory.getRow()>0){
                revisionNo=resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from production data table before insert
            statementHistory.execute("DELETE FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DATE='"+EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString())+"' AND PROD_DOC_NO='"+getAttribute("PRODUCTION_DOCUMENT_NO").getString()+"'");
            
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM PRODUCTION.`FELT_PROD_DATA_H` WHERE PROD_DATE=''");
            
            //Now Update records into production tables
            for(int i=1;i<=hmFeltFinishingDetails.size();i++) {
                clsFeltFinishingDetails ObjFeltFinishingDetails=(clsFeltFinishingDetails) hmFeltFinishingDetails.get(Integer.toString(i));
                
                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateString("PROD_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()));
                resultSetTemp.updateString("PROD_DOC_NO", getAttribute("PRODUCTION_DOCUMENT_NO").getString());
                resultSetTemp.updateString("PROD_FORM_NO", getAttribute("PRODUCTION_FORM_NO").getString());
                resultSetTemp.updateString("PROD_DEPT","FINISHING");
                resultSetTemp.updateInt("SR_NO",i);
                resultSetTemp.updateString("PROD_PIECE_NO",ObjFeltFinishingDetails.getAttribute("PRODUCTION_PIECE_NO").getString());
                resultSetTemp.updateString("PROD_PARTY_CODE",ObjFeltFinishingDetails.getAttribute("PRODUCTION_PARTY_CODE").getString());
                resultSetTemp.updateFloat("LENGTH",(float)EITLERPGLOBAL.round(ObjFeltFinishingDetails.getAttribute("LENGTH").getVal(),2));
                resultSetTemp.updateFloat("WIDTH",(float)EITLERPGLOBAL.round(ObjFeltFinishingDetails.getAttribute("WIDTH").getVal(),2));
                resultSetTemp.updateFloat("WEIGHT", (float)EITLERPGLOBAL.round(ObjFeltFinishingDetails.getAttribute("WEIGHT").getVal(),2));
                resultSetTemp.updateString("REMARKS",ObjFeltFinishingDetails.getAttribute("REMARKS").getString());
                resultSetTemp.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
                resultSetTemp.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                resultSetTemp.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
                resultSetTemp.updateString("MODIFIED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetTemp.updateBoolean("APPROVED",false);
                resultSetTemp.updateString("APPROVED_DATE","0000-00-00");
                resultSetTemp.updateBoolean("REJECTED",false);
                resultSetTemp.updateString("REJECTED_DATE","0000-00-00");
                resultSetTemp.updateInt("CANCELED",0);
                resultSetTemp.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
                resultSetTemp.updateInt("CHANGED",1);
                resultSetTemp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetTemp.insertRow();
                
                //Insert records into production data history table
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO",revisionNo);
                resultSetHistory.updateString("PROD_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()));
                resultSetHistory.updateString("PROD_DOC_NO", getAttribute("PRODUCTION_DOCUMENT_NO").getString());
                resultSetHistory.updateString("PROD_FORM_NO", getAttribute("PRODUCTION_FORM_NO").getString());
                resultSetHistory.updateString("PROD_DEPT","FINISHING");
                resultSetHistory.updateInt("SR_NO",i);
                resultSetHistory.updateString("PROD_PIECE_NO",ObjFeltFinishingDetails.getAttribute("PRODUCTION_PIECE_NO").getString());
                resultSetHistory.updateString("PROD_PARTY_CODE",ObjFeltFinishingDetails.getAttribute("PRODUCTION_PARTY_CODE").getString());
                resultSetHistory.updateFloat("LENGTH",(float)EITLERPGLOBAL.round(ObjFeltFinishingDetails.getAttribute("LENGTH").getVal(),2));
                resultSetHistory.updateFloat("WIDTH",(float)EITLERPGLOBAL.round(ObjFeltFinishingDetails.getAttribute("WIDTH").getVal(),2));
                resultSetHistory.updateFloat("WEIGHT", (float)EITLERPGLOBAL.round(ObjFeltFinishingDetails.getAttribute("WEIGHT").getVal(),2));
                resultSetHistory.updateString("REMARKS",ObjFeltFinishingDetails.getAttribute("REMARKS").getString());
                resultSetHistory.updateInt("UPDATED_BY",(int)getAttribute("UPDATED_BY").getVal());
                resultSetHistory.updateString("ENTRY_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
                resultSetHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
                resultSetHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
                resultSetHistory.updateInt("CHANGED",1);
                resultSetHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetHistory.insertRow();
            }
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=716; //Felt Finishing Entry
            ObjFeltProductionApprovalFlow.DocNo=getAttribute("PRODUCTION_DOCUMENT_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.FELT_PROD_DATA";
            ObjFeltProductionApprovalFlow.IsCreator=false;
            ObjFeltProductionApprovalFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName="PROD_DOC_NO";
            
            if(getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFeltProductionApprovalFlow.To=(int)getAttribute("SEND_DOC_TO").getVal();
                ObjFeltProductionApprovalFlow.ExplicitSendTo=true;
            }
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.`FELT_PROD_DATA` SET REJECTED=0,CHANGED=1 WHERE PROD_DEPT='FINISHING' AND PROD_DOC_NO='"+getAttribute("PRODUCTION_DOCUMENT_NO").getString()+"'");
                //Remove Old Records from FELT DOCUMET APPROVAL TABLE
                //data.Execute("DELETE FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=716 AND DOC_NO='"+getAttribute("PRODUCTION_DOCUMENT_NO").getString()+"'");
                
                ObjFeltProductionApprovalFlow.IsCreator=true;
            }
            //==========================================//
            
            if(ObjFeltProductionApprovalFlow.Status.equals("H")) {
                if(IsRejected) {
                    ObjFeltProductionApprovalFlow.Status="A";
                    ObjFeltProductionApprovalFlow.To=ObjFeltProductionApprovalFlow.From;
                    ObjFeltProductionApprovalFlow.UpdateFlow();
                }
            }else {
                if(!ObjFeltProductionApprovalFlow.UpdateFlow()) {
                    LastError=ObjFeltProductionApprovalFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------
            
            // Update Finishing Date in Order Master Table To confirm that Finishing has completed
            if(ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {
                ObjFeltProductionApprovalFlow.finalApproved=false;
                updatePieceRegister(EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()));
                Production_Date_Updation pd=new Production_Date_Updation();
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'FINISHING' AND PROD_DOC_NO='"+documentNo +"' AND APPROVED="+1;
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if(rsTmp.getInt("COUNT")>0){  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            }else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=716 AND USER_ID="+userID+" AND DOC_NO='"+ documentNo +"' AND STATUS='W'";
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getInt("COUNT")>0) {
                    strSQL = "DELETE FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'FINISHING'"
                    + " AND PROD_DATE= '"+ EITLERPGLOBAL.formatDateDB(stringProductionDate) +"'"
                    + " AND PROD_DOC_NO='" + documentNo + "'";
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'FINISHING' AND PROD_DOC_NO='"+ documentNo +"' AND APPROVED=1";
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0){  //Item is Approved
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=716 AND USER_ID="+userID+" AND DOC_NO='"+ documentNo +"' AND STATUS='W'";
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
            String strSql = "SELECT DISTINCT PROD_DATE FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'FINISHING' " + stringFindQuery + "ORDER BY PROD_DATE";
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
        double totalWeight, previousWeight;
        try {
            setAttribute("REVISION_NO",0);
            setAttribute("PRODUCTION_DATE",resultSet.getDate("PROD_DATE"));
            totalWeight = data.getDoubleValueFromDB("SELECT SUM(WEIGHT) FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'FINISHING' AND MONTH(PROD_DATE)=MONTH('"+resultSet.getDate("PROD_DATE")+"') AND YEAR(PROD_DATE)=YEAR('"+ resultSet.getDate("PROD_DATE") +"') AND APPROVED=1");
            setAttribute("TOTAL_WEIGHT",totalWeight);
            
            previousWeight = data.getDoubleValueFromDB("SELECT SUM(WEIGHT) FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'FINISHING' AND MONTH(PROD_DATE)=MONTH('"+resultSet.getDate("PROD_DATE")+"') AND YEAR(PROD_DATE)=YEAR('"+ resultSet.getDate("PROD_DATE") +"') AND PROD_DATE<'"+resultSet.getDate("PROD_DATE")+"' AND APPROVED=1");
            setAttribute("PREVIOUS_WEIGHT",previousWeight);
            
            //Now Populate the collection, first clear the collection
            hmFeltFinishingDetails.clear();
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'FINISHING'  AND PROD_DATE='"+ resultSet.getDate("PROD_DATE") +"' ORDER BY SR_NO");
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
                setAttribute("PRODUCTION_DOCUMENT_NO",resultSetTemp.getString("PROD_DOC_NO"));
                setAttribute("PRODUCTION_FORM_NO",resultSetTemp.getString("PROD_FORM_NO"));
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
                
                clsFeltFinishingDetails ObjFeltFinishingDetails = new clsFeltFinishingDetails();
                
                ObjFeltFinishingDetails.setAttribute("PRODUCTION_PIECE_NO",resultSetTemp.getString("PROD_PIECE_NO"));
                ObjFeltFinishingDetails.setAttribute("PRODUCTION_PARTY_CODE",resultSetTemp.getString("PROD_PARTY_CODE"));
                ObjFeltFinishingDetails.setAttribute("LENGTH",resultSetTemp.getFloat("LENGTH"));
                ObjFeltFinishingDetails.setAttribute("WIDTH",resultSetTemp.getFloat("WIDTH"));
                ObjFeltFinishingDetails.setAttribute("WEIGHT",resultSetTemp.getFloat("WEIGHT"));
                ObjFeltFinishingDetails.setAttribute("REMARKS",resultSetTemp.getString("REMARKS"));
                
                hmFeltFinishingDetails.put(Integer.toString(serialNoCounter),ObjFeltFinishingDetails);
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
            hmFeltFinishingDetails.clear();
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.`FELT_PROD_DATA_H` WHERE PROD_DEPT = 'FINISHING' AND PROD_DATE='"+ pProductionDate+"' AND PROD_DOC_NO='"+pDocNo+"' AND REVISION_NO='"+ resultSet.getInt("REVISION_NO") +"' ORDER BY REVISION_NO");
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
                setAttribute("PRODUCTION_DOCUMENT_NO",resultSetTemp.getString("PROD_DOC_NO"));
                setAttribute("PRODUCTION_FORM_NO",resultSetTemp.getString("PROD_FORM_NO"));
                setAttribute("UPDATED_BY",resultSetTemp.getInt("UPDATED_BY"));
                setAttribute("ENTRY_DATE",resultSetTemp.getString("ENTRY_DATE"));
                setAttribute("HIERARCHY_ID",resultSetTemp.getInt("HIERARCHY_ID"));
                
                clsFeltFinishingDetails ObjFeltFinishingDetails = new clsFeltFinishingDetails();
                
                ObjFeltFinishingDetails.setAttribute("PRODUCTION_PIECE_NO",resultSetTemp.getString("PROD_PIECE_NO"));
                ObjFeltFinishingDetails.setAttribute("PRODUCTION_PARTY_CODE",resultSetTemp.getString("PROD_PARTY_CODE"));
                ObjFeltFinishingDetails.setAttribute("LENGTH",resultSetTemp.getFloat("LENGTH"));
                ObjFeltFinishingDetails.setAttribute("WIDTH",resultSetTemp.getFloat("WIDTH"));
                ObjFeltFinishingDetails.setAttribute("WEIGHT",resultSetTemp.getFloat("WEIGHT"));
                ObjFeltFinishingDetails.setAttribute("REMARKS",resultSetTemp.getString("REMARKS"));
                
                hmFeltFinishingDetails.put(Integer.toString(serialNoCounter),ObjFeltFinishingDetails);
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
    
    public static HashMap getHistoryList(String stringProductionDate, String productionDocumentNo) {
        HashMap hmHistoryList=new HashMap();
        Statement stTmp;
        ResultSet rsTmp;
        try {
            stTmp=connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp=stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_PROD_DATA_H WHERE PROD_DEPT = 'FINISHING' AND PROD_DATE='"+ stringProductionDate+"' AND PROD_DOC_NO='"+productionDocumentNo+"' GROUP BY REVISION_NO");
            
            while(rsTmp.next()) {
                clsFeltFinishing ObjFeltFinishing=new clsFeltFinishing();
                
                ObjFeltFinishing.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjFeltFinishing.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjFeltFinishing.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjFeltFinishing.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjFeltFinishing.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                hmHistoryList.put(Integer.toString(hmHistoryList.size()+1),ObjFeltFinishing);
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
            resultSet=statement.executeQuery("SELECT DISTINCT REVISION_NO FROM PRODUCTION.FELT_PROD_DATA_H WHERE PROD_DEPT='FINISHING' AND PROD_DATE='"+ pProductionDate+"' AND PROD_DOC_NO='"+pDocNo+"'");
            Ready=true;
            historyProductionDate = pProductionDate;
            historyDocumentNo = pDocNo;
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
                strSQL="SELECT DISTINCT PROD_DOC_NO,PROD_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_PROD_DATA,PRODUCTION.FELT_PROD_DOC_DATA WHERE PROD_DOC_NO=DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=716 AND CANCELED=0 ORDER BY RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT DISTINCT PROD_DOC_NO,PROD_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_PROD_DATA,PRODUCTION.FELT_PROD_DOC_DATA WHERE PROD_DOC_NO=DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=716 AND CANCELED=0 ORDER BY PROD_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT DISTINCT PROD_DOC_NO,PROD_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_PROD_DATA,PRODUCTION.FELT_PROD_DOC_DATA WHERE PROD_DOC_NO=DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=716 AND CANCELED=0 ORDER BY PROD_DOC_NO";
            }
            
            ResultSet rsTmp=data.getConn().createStatement().executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp.next()) {
                
                Counter=Counter+1;
                clsFeltFinishing ObjDoc=new clsFeltFinishing();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO",rsTmp.getString("PROD_DOC_NO"));
                ObjDoc.setAttribute("DOC_DATE",rsTmp.getString("PROD_DATE"));
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
    
    // Insert into Piece Register Table To confirm that Finishing has completed
    private void updatePieceRegister(String prodDate){
        try {
            // Piece Register connection
            ResultSet rsPR = data.getConn().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery("SELECT * FROM PRODUCTION.FELT_PIECE_REGISTER WHERE PIECE_NO=0");
            
            ResultSet rsTemp = data.getConn().createStatement().executeQuery("SELECT PROD_DATE,PROD_PARTY_CODE,PROD_PIECE_NO,PIECE_NO,ORDER_CODE,LENGTH,WIDTH,WEIGHT,STYLE,PRODUCT_CODE FROM (SELECT PROD_DATE,PROD_FORM_NO,REPLACE(PROD_PIECE_NO,' ','') PROD_PIECE_NO,PROD_PARTY_CODE,LENGTH,WIDTH,WEIGHT FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DATE = '"+prodDate+"' AND PROD_DEPT='FINISHING' AND APPROVED=1) D LEFT JOIN (SELECT REPLACE(BALNK,' ','') AS STYLE,CONCAT(PRODUCT_CODE,'0') PRODUCT_CODE,PIECE_NO,PARTY_CD,ORDER_CODE FROM PRODUCTION.FELT_ORDER_MASTER) O ON O.PARTY_CD = D.PROD_PARTY_CODE AND O.PIECE_NO +0 = D.PROD_PIECE_NO +0");
            while(rsTemp.next()){
                rsPR.moveToInsertRow();
                rsPR.updateInt("WH_CODE",2);
                rsPR.updateString("PRODUCT_CD", rsTemp.getString("PRODUCT_CODE"));
                rsPR.updateString("ORDER_CD", rsTemp.getString("ORDER_CODE"));
                rsPR.updateString("PIECE_NO", rsTemp.getString("PROD_PIECE_NO"));
                rsPR.updateString("RCVD_DATE", rsTemp.getString("PROD_DATE"));
                rsPR.updateString("RCVD_MTR", rsTemp.getString("LENGTH"));
                rsPR.updateString("RECD_WDTH", rsTemp.getString("WIDTH"));
                rsPR.updateString("RECD_KG", rsTemp.getString("WEIGHT"));
                rsPR.updateInt("CREATED_BY", EITLERPGLOBAL.gNewUserID);
                rsPR.updateString("CREATED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP FROM DUAL"));
                rsPR.updateString("PARTY_CODE",rsTemp.getString("PROD_PARTY_CODE"));
                rsPR.updateString("STYLE", rsTemp.getString("STYLE"));
                rsPR.updateString("REMARK", "IN STOCK");
                rsPR.updateString("ORDER_NO", rsTemp.getString("PIECE_NO"));
                rsPR.insertRow();
            }
            
            rsTemp.close();
            rsPR.close();
        }catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
        }
    }
    
    // checks piece no already exist in db
    public boolean checkPieceNoInDB(String pPieceNo){
        int count=data.getIntValueFromDB("SELECT COUNT(PROD_PIECE_NO) FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'FINISHING' AND PROD_PIECE_NO='"+pPieceNo+"' AND PROD_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PROD_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
        if(count>0) return true;
        else return false;
    }
    
    public boolean checkPieceNoInDB(String pPieceNo, String pProdDate){
        int count=data.getIntValueFromDB("SELECT COUNT(PROD_PIECE_NO) FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'FINISHING' AND PROD_PIECE_NO='"+pPieceNo+"' AND PROD_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PROD_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
        if(count>=1){
            int counter=0;
            try{
                Connection Conn=data.getConn();
                Statement stTmp=Conn.createStatement();
                ResultSet rsTmp=stTmp.executeQuery("SELECT PROD_DATE FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT='FINISHING' AND PROD_PIECE_NO='"+pPieceNo+"' AND PROD_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PROD_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
                while(rsTmp.next()){
                    if(rsTmp.getString("PROD_DATE").equals(EITLERPGLOBAL.formatDateDB(pProdDate))) counter++;
                }
            }catch(SQLException e){e.printStackTrace();}
            if(counter==1 && count>=2) return true;
            else if(counter==1) return false;
            else return true;
        }else return false;
    }
    
    // checks production date already exist in db
    public boolean checkProductionDateInDB(String pProdDate){
        int count=data.getIntValueFromDB("SELECT COUNT(PROD_DATE) FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'FINISHING' AND PROD_DATE='"+pProdDate+"'");
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
    
    // generates report data
    public  TReportWriter.SimpleDataProvider.TTable getReportData(String prodDate) {
        TReportWriter.SimpleDataProvider.TTable objData=new TReportWriter.SimpleDataProvider.TTable();
        objData.AddColumn("SR_NO");
        objData.AddColumn("REPORT_NO");
        objData.AddColumn("PROD_DATE");
        objData.AddColumn("PARTY_CODE");
        objData.AddColumn("PARTY_NAME");
        objData.AddColumn("FELT_NO");
        objData.AddColumn("LENGTH");
        objData.AddColumn("WIDTH");
        objData.AddColumn("WEIGHT");
        objData.AddColumn("SYN_PER");
        objData.AddColumn("STYLE");
        objData.AddColumn("GROUP");
        objData.AddColumn("PRODUCT_CODE");
        objData.AddColumn("ITEM_DESC");
        objData.AddColumn("TOTAL_LENGTH");
        objData.AddColumn("TOTAL_WIDTH");
        objData.AddColumn("TOTAL_WEIGHT");
        
        try{
            TReportWriter.SimpleDataProvider.TRow objRow=objData.newRow();
            
            String str ="SELECT PROD_DATE,PROD_FORM_NO,PROD_PARTY_CODE,COALESCE(PARTY_NAME,'') PARTY_NAME,PROD_PIECE_NO,LENGTH,WIDTH,WEIGHT,SYN_PER,STYLE,GRUP,PRODUCT_CODE,ITEM_DESC,TOTAL_LENGTH,TOTAL_WIDTH,TOTAL_WEIGHT FROM (SELECT PROD_DATE,PROD_FORM_NO,REPLACE(PROD_PIECE_NO,' ','') PROD_PIECE_NO,PROD_PARTY_CODE,LENGTH,WIDTH,WEIGHT FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DATE = '"+prodDate+"' AND PROD_DEPT='FINISHING') D LEFT JOIN (SELECT REPLACE(BALNK,' ','') AS STYLE,PRODUCT_CODE,PIECE_NO,PARTY_CD FROM PRODUCTION.FELT_ORDER_MASTER) O ON O.PARTY_CD = D.PROD_PARTY_CODE AND O.PIECE_NO +0 = D.PROD_PIECE_NO +0 LEFT JOIN (SELECT PARTY_CODE, SUBSTRING(TRIM(PARTY_NAME),1,23) PARTY_NAME FROM D_SAL_PARTY_MASTER) AS P ON P.PARTY_CODE = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(LENGTH),0) AS TOTAL_LENGTH FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'FINISHING' AND MONTH(PROD_DATE)=MONTH('"+prodDate+"') AND YEAR(PROD_DATE)=YEAR('"+prodDate+"') AND PROD_DATE<'"+prodDate+"' AND APPROVED=1) AS TL ON O.PARTY_CD = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WIDTH),0) AS TOTAL_WIDTH FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'FINISHING' AND MONTH(PROD_DATE)=MONTH('"+prodDate+"') AND YEAR(PROD_DATE)=YEAR('"+prodDate+"') AND PROD_DATE='"+prodDate+"' AND APPROVED=1) AS TW ON O.PARTY_CD = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS TOTAL_WEIGHT FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'FINISHING' AND MONTH(PROD_DATE)=MONTH('"+prodDate+"') AND YEAR(PROD_DATE)=YEAR('"+prodDate+"') AND PROD_DATE<='"+prodDate+"' AND APPROVED=1) AS WT ON O.PARTY_CD = D.PROD_PARTY_CODE LEFT JOIN (SELECT ITEM_CODE,TRIM(ITEM_DESC) ITEM_DESC, SYN_PER,GRUP FROM PRODUCTION.FELT_RATE_MASTER) AS R ON SUBSTRING(ITEM_CODE,1,6)=PRODUCT_CODE";
            ResultSet  rsTemp =data.getResult(str);
            int counter=1;
            while(!rsTemp.isAfterLast()){
                objRow=objData.newRow();
                objRow.setValue("SR_NO", String.valueOf(counter));
                objRow.setValue("REPORT_NO", rsTemp.getString("PROD_FORM_NO"));
                objRow.setValue("PROD_DATE", EITLERPGLOBAL.formatDate(rsTemp.getString("PROD_DATE")));
                objRow.setValue("PARTY_CODE", rsTemp.getString("PROD_PARTY_CODE"));
                objRow.setValue("PARTY_NAME", rsTemp.getString("PARTY_NAME"));
                objRow.setValue("FELT_NO", rsTemp.getString("PROD_PIECE_NO"));
                objRow.setValue("LENGTH", rsTemp.getString("LENGTH"));
                objRow.setValue("WIDTH", rsTemp.getString("WIDTH"));
                objRow.setValue("WEIGHT", rsTemp.getString("WEIGHT"));
                objRow.setValue("SYN_PER", rsTemp.getString("SYN_PER"));
                objRow.setValue("STYLE", rsTemp.getString("STYLE"));
                objRow.setValue("GROUP", rsTemp.getString("GRUP"));
                objRow.setValue("PRODUCT_CODE", rsTemp.getString("PRODUCT_CODE"));
                objRow.setValue("ITEM_DESC", rsTemp.getString("ITEM_DESC"));
                objRow.setValue("TOTAL_LENGTH", rsTemp.getString("TOTAL_LENGTH"));
                objRow.setValue("TOTAL_WIDTH", rsTemp.getString("TOTAL_WIDTH"));
                objRow.setValue("TOTAL_WEIGHT", rsTemp.getString("TOTAL_WEIGHT"));
                objData.AddRow(objRow);
                counter++;
                rsTemp.next();
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return objData;
    }
    
}
