/*
 * clsFeltWarping.java
 *
 * Created on March 12, 2013, 3:10 PM
 */

package EITLERP.Production.FeltWarping;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import EITLERP.EITLERPGLOBAL;
import EITLERP.Variant;
import EITLERP.data;
import EITLERP.Production.clsFeltProductionApprovalFlow;

/**
 *
 * @author  Vivek Kumar
 * @version
 */

public class clsFeltWarping {
    
    public String LastError="";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    
    public HashMap props;
    public boolean Ready = false;
    //Felt Production Warping Details Collection
    public HashMap hmFeltWarpingDetails;
    
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
    
    /** Creates new DataFeltWarping */
    public clsFeltWarping() {
        LastError = "";
        props=new HashMap();
        props.put("PRODUCTION_DATE", new Variant(""));
        props.put("PRODUCTION_DOCUMENT_NO", new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        
        hmFeltWarpingDetails=new HashMap();
    }
    
    public boolean LoadData() {
        Ready=false;
        try {
            connection = data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet=statement.executeQuery("SELECT DISTINCT PROD_DATE FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'WARPING' AND PROD_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PROD_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY PROD_DATE");
            HistoryView=false;
            Ready=true;
            //if(resultSet.getRow()>0){
            MoveLast();
            return true;
            //}else return false;
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
            for(int i=1;i<=hmFeltWarpingDetails.size();i++) {
                clsFeltWarpingDetails ObjFeltWarpingDetails=(clsFeltWarpingDetails) hmFeltWarpingDetails.get(Integer.toString(i));
                
                //Insert records into production data table
                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateString("PROD_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()));
                resultSetTemp.updateString("PROD_DOC_NO", getAttribute("PRODUCTION_DOCUMENT_NO").getString());
                resultSetTemp.updateString("PROD_FORM_NO", getAttribute("PRODUCTION_FORM_NO").getString());
                resultSetTemp.updateString("PROD_DEPT","WARPING");
                resultSetTemp.updateInt("SR_NO",i);
                resultSetTemp.updateString("PROD_PIECE_NO",ObjFeltWarpingDetails.getAttribute("PRODUCTION_PIECE_NO").getString());
                resultSetTemp.updateInt("LOOM_NO",(int)ObjFeltWarpingDetails.getAttribute("LOOM_NO").getVal());
                resultSetTemp.updateString("REMARKS",ObjFeltWarpingDetails.getAttribute("REMARKS").getString());
                resultSetTemp.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
                resultSetTemp.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                resultSetTemp.updateInt("MODIFIED_BY",0);
                resultSetTemp.updateString("MODIFIED_DATE","0000-00-00");
                resultSetTemp.updateBoolean("APPROVED",false);
                resultSetTemp.updateString("APPROVED_DATE","0000-00-00");
                resultSetTemp.updateBoolean("REJECTED",false);
                resultSetTemp.updateString("REJECTED_DATE","0000-00-00");
                resultSetTemp.updateInt("CANCELED",0);
                resultSetTemp.updateInt("CHANGED",1);
                resultSetTemp.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
                resultSetTemp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetTemp.insertRow();
                
                //Insert records into production data history table
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO",1);
                resultSetHistory.updateString("PROD_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()));
                resultSetHistory.updateString("PROD_DOC_NO", getAttribute("PRODUCTION_DOCUMENT_NO").getString());
                resultSetHistory.updateString("PROD_FORM_NO", getAttribute("PRODUCTION_FORM_NO").getString());
                resultSetHistory.updateString("PROD_DEPT","WARPING");
                resultSetHistory.updateInt("SR_NO",i);
                resultSetHistory.updateString("PROD_PIECE_NO",ObjFeltWarpingDetails.getAttribute("PRODUCTION_PIECE_NO").getString());
                resultSetHistory.updateInt("LOOM_NO",(int)ObjFeltWarpingDetails.getAttribute("LOOM_NO").getVal());
                resultSetHistory.updateString("REMARKS",ObjFeltWarpingDetails.getAttribute("REMARKS").getString());
                resultSetHistory.updateInt("UPDATED_BY",(int)getAttribute("UPDATED_BY").getVal());
                resultSetHistory.updateString("ENTRY_DATE",data.getStringValueFromDB("SELECT CURDATE() AS DATE FROM DUAL"));
                resultSetHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
                resultSetHistory.updateInt("CHANGED",1);
                resultSetHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
                resultSetHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
                resultSetHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetHistory.insertRow();
            }
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=709; //Felt Warping Entry
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
            resultSetHistory=statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_PROD_DATA_H WHERE PROD_DEPT = 'WARPING' AND PROD_DATE='"+EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString())+"' AND PROD_DOC_NO='"+getAttribute("PRODUCTION_DOCUMENT_NO").getString()+"'");
            resultSetHistory.first();
            if(resultSetHistory.getRow()>0){
                revisionNo=resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from production data table before insert
            statementHistory.execute("DELETE FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DATE='"+EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString())+"' AND PROD_DOC_NO='"+getAttribute("PRODUCTION_DOCUMENT_NO").getString()+"'");
            
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM PRODUCTION.`FELT_PROD_DATA_H` WHERE PROD_DATE=''");
            
            //Now Update records into production tables
            for(int i=1;i<=hmFeltWarpingDetails.size();i++) {
                clsFeltWarpingDetails ObjFeltWarpingDetails=(clsFeltWarpingDetails) hmFeltWarpingDetails.get(Integer.toString(i));
                
                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateString("PROD_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()));
                resultSetTemp.updateString("PROD_DOC_NO", getAttribute("PRODUCTION_DOCUMENT_NO").getString());
                resultSetTemp.updateString("PROD_FORM_NO", getAttribute("PRODUCTION_FORM_NO").getString());
                resultSetTemp.updateString("PROD_DEPT","WARPING");
                resultSetTemp.updateInt("SR_NO",i);
                resultSetTemp.updateString("PROD_PIECE_NO",ObjFeltWarpingDetails.getAttribute("PRODUCTION_PIECE_NO").getString());
                resultSetTemp.updateInt("LOOM_NO",(int)ObjFeltWarpingDetails.getAttribute("LOOM_NO").getVal());
                resultSetTemp.updateString("REMARKS",ObjFeltWarpingDetails.getAttribute("REMARKS").getString());
                resultSetTemp.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
                resultSetTemp.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                resultSetTemp.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
                resultSetTemp.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
                resultSetTemp.updateBoolean("APPROVED",false);
                resultSetTemp.updateString("APPROVED_DATE","0000-00-00");
                resultSetTemp.updateBoolean("REJECTED",false);
                resultSetTemp.updateString("REJECTED_DATE","0000-00-00");
                resultSetTemp.updateInt("CANCELED",0);
                resultSetTemp.updateInt("CHANGED",1);
                resultSetTemp.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
                resultSetTemp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetTemp.insertRow();
                
                //Insert records into production data history table
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO",revisionNo);
                resultSetHistory.updateString("PROD_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()));
                resultSetHistory.updateString("PROD_DOC_NO", getAttribute("PRODUCTION_DOCUMENT_NO").getString());
                resultSetHistory.updateString("PROD_FORM_NO", getAttribute("PRODUCTION_FORM_NO").getString());
                resultSetHistory.updateString("PROD_DEPT","WARPING");
                resultSetHistory.updateInt("SR_NO",i);
                resultSetHistory.updateString("PROD_PIECE_NO",ObjFeltWarpingDetails.getAttribute("PRODUCTION_PIECE_NO").getString());
                resultSetHistory.updateInt("LOOM_NO",(int)ObjFeltWarpingDetails.getAttribute("LOOM_NO").getVal());
                resultSetHistory.updateString("REMARKS",ObjFeltWarpingDetails.getAttribute("REMARKS").getString());
                resultSetHistory.updateInt("UPDATED_BY",(int)getAttribute("UPDATED_BY").getVal());
                resultSetHistory.updateString("ENTRY_DATE",data.getStringValueFromDB("SELECT CURDATE() AS DATE FROM DUAL"));
                resultSetHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
                resultSetHistory.updateInt("CHANGED",1);
                resultSetHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
                resultSetHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
                resultSetHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetHistory.insertRow();
            }
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=709; //Felt Warping Entry
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
                data.Execute("UPDATE PRODUCTION.`FELT_PROD_DATA` SET REJECTED=0,CHANGED=1 WHERE PROD_DEPT='WARPING' AND PROD_DOC_NO='"+getAttribute("PRODUCTION_DOCUMENT_NO").getString()+"'");
                //Remove Old Records from FELT DOCUMET APPROVAL TABLE
                data.Execute("DELETE FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=709 AND DOC_NO='"+getAttribute("PRODUCTION_DOCUMENT_NO").getString()+"'");
                
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
            
            // Update Warping Date in Order Master Table To confirm that Warping has completed
            if(ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {
                ObjFeltProductionApprovalFlow.finalApproved=false;
                updateWarpingDate(getAttribute("PRODUCTION_DOCUMENT_NO").getString());
            }
            
            setData();
            resultSetTemp.close();
            statementTemp.close();
            resultSetHistory.close();
            statementHistory.close();
            return true;
        }
        catch(Exception e) {
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'WARPING' AND PROD_DOC_NO='"+documentNo +"' AND APPROVED="+1;
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if(rsTmp.getInt("COUNT")>0){  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            }else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=709 AND USER_ID="+userID+" AND DOC_NO='"+ documentNo +"' AND STATUS='W'";
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getInt("COUNT")>0) {
                    strSQL = "DELETE FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'WARPING'"
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
    public boolean IsEditable(String weavingDocumentNo,int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        try {
            if(HistoryView) {
                return false;
            }
            tmpStmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'WARPING' AND PROD_DOC_NO='"+ weavingDocumentNo +"' AND APPROVED=1";
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0){  //Item is Approved
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=709 AND USER_ID="+Integer.toString(userID)+" AND DOC_NO='"+ weavingDocumentNo +"' AND STATUS='W'";
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
            String strSql = "SELECT DISTINCT PROD_DATE FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'WARPING' " + stringFindQuery + "ORDER BY PROD_DATE";
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
        double totalWeight, previousWeight;
        
        try {
            setAttribute("REVISION_NO",0);
            setAttribute("PRODUCTION_DATE",resultSet.getDate("PROD_DATE"));
            
            //Now Populate the collection, first clear the collection
            hmFeltWarpingDetails.clear();
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'WARPING'  AND PROD_DATE='"+ resultSet.getDate("PROD_DATE") +"' ORDER BY SR_NO");
            
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
                setAttribute("HIERARCHY_ID",resultSetTemp.getInt("HIERARCHY_ID"));
                setAttribute("CANCELED",resultSetTemp.getInt("CANCELED"));
                
                clsFeltWarpingDetails ObjFeltWarpingDetails = new clsFeltWarpingDetails();
                
                ObjFeltWarpingDetails.setAttribute("PRODUCTION_PIECE_NO",resultSetTemp.getString("PROD_PIECE_NO"));
                ObjFeltWarpingDetails.setAttribute("LOOM_NO",resultSetTemp.getInt("LOOM_NO"));
                ObjFeltWarpingDetails.setAttribute("REMARKS",resultSetTemp.getString("REMARKS"));
                
                hmFeltWarpingDetails.put(Integer.toString(serialNoCounter),ObjFeltWarpingDetails);
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
            hmFeltWarpingDetails.clear();
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.`FELT_PROD_DATA_H` WHERE PROD_DEPT = 'WARPING' AND PROD_DATE='"+ pProductionDate+"'AND PROD_DOC_NO='"+pDocNo+"' AND REVISION_NO='"+ resultSet.getInt("REVISION_NO") +"' ORDER BY REVISION_NO");
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
                setAttribute("PRODUCTION_DOCUMENT_NO",resultSetTemp.getString("PROD_DOC_NO"));
                setAttribute("PRODUCTION_FORM_NO",resultSetTemp.getString("PROD_FORM_NO"));
                setAttribute("UPDATED_BY",resultSetTemp.getInt("UPDATED_BY"));
                setAttribute("ENTRY_DATE",resultSetTemp.getString("ENTRY_DATE"));
                
                clsFeltWarpingDetails ObjFeltWarpingDetails = new clsFeltWarpingDetails();
                
                ObjFeltWarpingDetails.setAttribute("PRODUCTION_PIECE_NO",resultSetTemp.getString("PROD_PIECE_NO"));
                ObjFeltWarpingDetails.setAttribute("LOOM_NO",resultSetTemp.getInt("LOOM_NO"));
                ObjFeltWarpingDetails.setAttribute("REMARKS",resultSetTemp.getString("REMARKS"));
                
                hmFeltWarpingDetails.put(Integer.toString(serialNoCounter),ObjFeltWarpingDetails);
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
            rsTmp=stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_PROD_DATA_H WHERE PROD_DEPT = 'WARPING' AND PROD_DATE='"+ stringProductionDate+"' AND PROD_DOC_NO='"+productionDocumentNo+"' GROUP BY REVISION_NO");
            
            while(rsTmp.next()) {
                clsFeltWarping ObjFeltWarping=new clsFeltWarping();
                
                ObjFeltWarping.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjFeltWarping.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjFeltWarping.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjFeltWarping.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjFeltWarping.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                hmHistoryList.put(Integer.toString(hmHistoryList.size()+1),ObjFeltWarping);
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
            resultSet=statement.executeQuery("SELECT DISTINCT REVISION_NO FROM PRODUCTION.FELT_PROD_DATA_H WHERE PROD_DEPT='WARPING' AND PROD_DATE='"+ pProductionDate+"'AND PROD_DOC_NO='"+pDocNo+"'");
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
                strSQL="SELECT DISTINCT PROD_DOC_NO,PROD_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_PROD_DATA,PRODUCTION.FELT_PROD_DOC_DATA WHERE PROD_DOC_NO=DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=709 AND CANCELED=0 ORDER BY RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT DISTINCT PROD_DOC_NO,PROD_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_PROD_DATA,PRODUCTION.FELT_PROD_DOC_DATA WHERE PROD_DOC_NO=DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=709 AND CANCELED=0 ORDER BY PROD_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT DISTINCT PROD_DOC_NO,PROD_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_PROD_DATA,PRODUCTION.FELT_PROD_DOC_DATA WHERE PROD_DOC_NO=DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=709 AND CANCELED=0 ORDER BY PROD_DOC_NO";
            }
            
            ResultSet rsTmp=data.getConn().createStatement().executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp.next()) {
                
                Counter=Counter+1;
                clsFeltWarping ObjDoc=new clsFeltWarping();
                
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
    
    // Updates Warping Date in Order Master Table To confirm that Warping has completed
    private void updateWarpingDate(String documentNo){
        data.Execute("UPDATE PRODUCTION.FELT_ORDER_MASTER,PRODUCTION.FELT_PROD_DATA SET MND_DATE=PROD_DATE WHERE PIECE_NO+0=PROD_PIECE_NO+0 AND PARTY_CD=PROD_PARTY_CODE AND PROD_DEPT='WARPING' AND PROD_DOC_NO='"+documentNo+"' AND APPROVED=1");
    }
    
    // checks piece no already exist in db
    public boolean checkPieceNoInDB(String pPieceNo){
        int count=data.getIntValueFromDB("SELECT COUNT(PROD_PIECE_NO) FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'WARPING' AND PROD_PIECE_NO='"+pPieceNo+"'");
        if(count>0) return true;
        else return false;
    }
    
    public boolean checkPieceNoInDB(String pPieceNo, String pProdDate){
        int count=data.getIntValueFromDB("SELECT COUNT(PROD_PIECE_NO) FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'WARPING' AND PROD_PIECE_NO='"+pPieceNo+"'");
        if(count>=1){
            int counter=0;
            try{
                Connection Conn=data.getConn();
                Statement stTmp=Conn.createStatement();
                ResultSet rsTmp=stTmp.executeQuery("SELECT PROD_DATE FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT='WARPING' AND PROD_PIECE_NO='"+pPieceNo+"'");
                while(rsTmp.next()){
                    if(rsTmp.getString("PROD_DATE").equals(EITLERPGLOBAL.formatDateDB(pProdDate))) counter++;
                }
            }catch(SQLException e){e.printStackTrace();}
            if(counter==1 && count>=2) return true;
            else if(counter==1) return false;
            else return true;
        }else return false;
    }
    
    public boolean checkProductionDateInDB(String pProdDate){
        int count=data.getIntValueFromDB("SELECT COUNT(PROD_DATE) FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'WARPING' AND PROD_DATE='"+pProdDate+"'");
        if(count>0) return true;
        else return false;
    }
    
    public boolean checkPieceNoInOrderMaster(String pPieceNo){
        int count=data.getIntValueFromDB("SELECT COUNT(PROD_PIECE_NO) FROM PRODUCTION.`FELT_ORDER_MASTER` WHERE PROD_PIECE_NO='"+pPieceNo+"'");
        if(count>0) return true;
        else return false;
    }
}
