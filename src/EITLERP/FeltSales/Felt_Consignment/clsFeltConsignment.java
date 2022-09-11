/*
 * clsFeltConsignment.java
 *
 * Created on March 12, 2013, 3:10 PM
 */

package EITLERP.FeltSales.Felt_Consignment;

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
import EITLERP.Production.clsFeltProductionApprovalFlow;

/**
 *
 * @author
 * @version
 */

public class clsFeltConsignment {
    
    public String LastError="";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    
    public HashMap props;
    public boolean Ready = false;
    //Felt Order Collection
    public HashMap hmFeltConsignmentDetails;
    
    //History Related properties
    public boolean HistoryView=false;
    private String historyConsignmentDate="";
    private String historyConsignmentNo="";
    
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
    public clsFeltConsignment() {
        LastError = "";
        props=new HashMap();
        props.put("CON_NO", new Variant(""));
        props.put("CON_DOC_DATE", new Variant(""));
       props.put("CON_PARTY_CODE", new Variant(""));
       props.put("CON_PARTY_NAME", new Variant(""));  
       props.put("CON_DATE", new Variant(""));  

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
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        
        hmFeltConsignmentDetails=new HashMap();
    }
    
    public boolean LoadData() {
        Ready=false;
        try {
            connection = data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //resultSet=statement.executeQuery("SELECT CON_NO,CON_DATE FROM PRODUCTION.FELT_CONSIGNMENT WHERE CON_DATE >= '"+EITLERPGLOBAL.FinFromDateDB+"' AND CON_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY CON_DATE");
            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.FELT_CONSIGNMENT ORDER BY CON_NO");
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
            if(HistoryView) setHistoryData(historyConsignmentDate, historyConsignmentNo);
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
            if(HistoryView) setHistoryData(historyConsignmentDate, historyConsignmentNo);
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
            resultSetDetail = statementDetail.executeQuery("SELECT * FROM  PRODUCTION.FELT_CONSIGNMENT_DETAIL WHERE CON_NO=''");
            
           
            statementDetailHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetDetailHistory = statementDetailHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_CONSIGNMENT_DETAIL_H WHERE CON_NO=''");
            
            
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_CONSIGNMENT_H WHERE CON_NO=''");
            
            
            
            
            // Felt order Updation Last Free No,
            setAttribute("CON_NO",getNextFreeNo(721,true));
            
            resultSet.moveToInsertRow();
            resultSet.updateString("CON_NO", getAttribute("CON_NO").getString());
            resultSet.updateString("CON_DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CON_DOC_DATE").getString()));
            resultSet.updateString("CON_PARTY_CODE", getAttribute("CON_PARTY_CODE").getString());
            resultSet.updateString("CON_PARTY_NAME",getAttribute("CON_PARTY_NAME").getString());
            resultSet.updateString("CON_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CON_DATE").getString()));
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
            
            
            resultSetHistory.moveToInsertRow();
            resultSetHistory.updateInt("REVISION_NO",1);
            resultSetHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            resultSetHistory.updateInt("UPDATED_BY", getAttribute("UPDATED_BY").getInt());
            resultSetHistory.updateString("CON_NO", getAttribute("CON_NO").getString());
            resultSetHistory.updateString("CON_DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CON_DOC_DATE").getString()));
            resultSetHistory.updateString("CON_PARTY_CODE", getAttribute("CON_PARTY_CODE").getString());
            resultSetHistory.updateString("CON_PARTY_NAME", getAttribute("CON_PARTY_NAME").getString());
            resultSetHistory.updateString("CON_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CON_DATE").getString()));
            resultSetHistory.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
            resultSetHistory.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSetHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
           // resultSetHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
            resultSetHistory.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            resultSetHistory.updateInt("CHANGED",1);
            resultSetHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            resultSetHistory.insertRow();



            //Now Insert records into FELT_CONSIGNMENT  tables
            for(int i=1;i<=hmFeltConsignmentDetails.size();i++) {
                clsFeltConsignmentDetails ObjFeltConsignmentDetails = (clsFeltConsignmentDetails) hmFeltConsignmentDetails.get(Integer.toString(i));
                
                //Insert records into Consignment detail table
                resultSetDetail.moveToInsertRow();
                resultSetDetail.updateString("CON_NO", getAttribute("CON_NO").getString());
                resultSetDetail.updateString("CON_DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CON_DOC_DATE").getString()));
                resultSetDetail.updateString("CON_PIECE_NO",ObjFeltConsignmentDetails.getAttribute("CON_PIECE_NO").getString());
//                resultSetDetail.updateString("CON_PARTY_CODE",ObjFeltConsignmentDetails.getAttribute("CON_PARTY_CODE").getString());
//                resultSetDetail.updateString("CON_PARTY_NAME",ObjFeltConsignmentDetails.getAttribute("CON_PARTY_NAME").getString());
                resultSetDetail.updateString("CON_PRODUCT_CODE",ObjFeltConsignmentDetails.getAttribute("CON_PRODUCT_CODE").getString());
                resultSetDetail.updateString("CON_LENGTH",ObjFeltConsignmentDetails.getAttribute("CON_LENGTH").getString());
                resultSetDetail.updateString("CON_WIDTH",ObjFeltConsignmentDetails.getAttribute("CON_WIDTH").getString());
                resultSetDetail.updateString("CON_GSQ",ObjFeltConsignmentDetails.getAttribute("CON_GSQ").getString());
                resultSetDetail.updateString("CON_AGREED_DATE",ObjFeltConsignmentDetails.getAttribute("CON_AGREED_DATE").getString());
                //resultSetDetail.updateString("CON_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CON_DATE").getString()));
                resultSetDetail.updateInt("CREATED_BY",getAttribute("CREATED_BY").getInt());
                resultSetDetail.updateString("CREATED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetDetail.updateInt("MODIFIED_BY",0);
                resultSetDetail.updateString("MODIFIED_DATE","0000-00-00");
                resultSetDetail.updateInt("CANCELED",0);
                resultSetDetail.updateInt("CHANGED",1);
                resultSetDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetail.insertRow();
                
                
                //Insert records into Felt Consignment History Table
                resultSetDetailHistory.moveToInsertRow();
                resultSetDetailHistory.updateInt("REVISION_NO",1);
                resultSetDetailHistory.updateInt("UPDATED_BY", getAttribute("UPDATED_BY").getInt());
                
                resultSetDetailHistory.updateString("CON_DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CON_DOC_DATE").getString()));
                resultSetDetailHistory.updateString("CON_NO", getAttribute("CON_NO").getString());
                resultSetDetailHistory.updateString("CON_PIECE_NO",ObjFeltConsignmentDetails.getAttribute("CON_PIECE_NO").getString());
//                resultSetHistory.updateString("CON_PARTY_CODE",ObjFeltConsignmentDetails.getAttribute("CON_PARTY_CODE").getString());
//                resultSetHistory.updateString("CON_PARTY_NAME",ObjFeltConsignmentDetails.getAttribute("CON_PARTY_NAME").getString());
                resultSetDetailHistory.updateString("CON_PRODUCT_CODE",ObjFeltConsignmentDetails.getAttribute("CON_PRODUCT_CODE").getString());
                resultSetDetailHistory.updateString("CON_LENGTH",ObjFeltConsignmentDetails.getAttribute("CON_LENGTH").getString());
                resultSetDetailHistory.updateString("CON_WIDTH",ObjFeltConsignmentDetails.getAttribute("CON_WIDTH").getString());
                resultSetDetailHistory.updateString("CON_GSQ",ObjFeltConsignmentDetails.getAttribute("CON_GSQ").getString());
                resultSetDetailHistory.updateString("CON_AGREED_DATE",ObjFeltConsignmentDetails.getAttribute("CON_AGREED_DATE").getString());
                //resultSetHistory.updateString("CON_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CON_DATE").getString()));
                // resultSetHistory.updateString("CON_DATE",ObjFeltConsignmentDetails.getAttribute("CON_DATE").getString());
                resultSetDetailHistory.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
                resultSetDetailHistory.updateString("CREATED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetDetailHistory.updateInt("MODIFIED_BY",0);
                resultSetDetailHistory.updateString("MODIFIED_DATE","0000-00-00");
                resultSetDetailHistory.updateBoolean("APPROVED",false);
                resultSetDetailHistory.updateString("APPROVED_DATE","0000-00-00");
                resultSetDetailHistory.updateBoolean("REJECTED",false);
                resultSetDetailHistory.updateString("REJECTED_DATE","0000-00-00");
                resultSetDetailHistory.updateInt("CANCELED",0);
                resultSetDetailHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
                resultSetDetailHistory.updateInt("CHANGED",1);
                resultSetDetailHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetailHistory.insertRow();
                
            }
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=721; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo=getAttribute("CON_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName="FELT_CONSIGNMENT";
            ObjFeltProductionApprovalFlow.IsCreator=true;
            ObjFeltProductionApprovalFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName="CON_NO";
            
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
            resultSetDetail.close();
            statementDetail.close();
            //    resultSetDetailHistory.close();
            //   statementDetailHistory.close();
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
        ResultSet  resultSetDetail, resultSetDetailHistory, resultSetHistory;
        Statement  statementDetail, statementDetailHistory, statementHistory;
        
        try {
            // Group detail connection
            statementDetail = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetDetail = statementDetail.executeQuery("SELECT * FROM  PRODUCTION.FELT_CONSIGNMENT_DETAIL WHERE CON_NO=''");
            
            // Group detail history connection
            statementDetailHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetDetailHistory = statementDetailHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_CONSIGNMENT_DETAIL_H WHERE CON_NO=''");
            
            // Group data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_CONSIGNMENT_H WHERE CON_NO=''");
         
            
            resultSet.moveToInsertRow();
            resultSet.updateString("CON_NO", getAttribute("CON_NO").getString());
            resultSet.updateString("CON_DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CON_DOC_DATE").getString()));
            resultSet.updateString("CON_PARTY_CODE", getAttribute("CON_PARTY_CODE").getString());
            resultSet.updateString("CON_PARTY_NAME",getAttribute("CON_PARTY_NAME").getString());
            resultSet.updateString("CON_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CON_DATE").getString()));
            resultSet.updateInt("MODIFIED_BY", getAttribute("MODIFIED_BY").getInt());
            resultSet.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            resultSet.updateInt("CHANGED",1);
            resultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateRow();
            
            
            int revisionNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_CONSIGNMENT_H WHERE CON_NO='"+getAttribute("CON_NO").getString()+"'");
            revisionNo++;
            
            resultSetHistory.moveToInsertRow();
            resultSetHistory.updateInt("REVISION_NO",revisionNo);
            resultSetHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            resultSetHistory.updateInt("UPDATED_BY", getAttribute("UPDATED_BY").getInt());
            resultSetHistory.updateString("CON_NO", getAttribute("CON_NO").getString());
            resultSetHistory.updateString("CON_DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CON_DOC_DATE").getString()));
            resultSetHistory.updateString("CON_PARTY_CODE", getAttribute("CON_PARTY_CODE").getString());
            resultSetHistory.updateString("CON_PARTY_NAME", getAttribute("CON_PARTY_NAME").getString());
            resultSetHistory.updateString("CON_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CON_DATE").getString()));
            resultSetHistory.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
            resultSetHistory.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSetHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
         //   resultSetHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
            resultSetHistory.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            resultSetHistory.updateInt("CHANGED",1);
            resultSetHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            resultSetHistory.insertRow();
           
            
            data.Execute("DELETE FROM PRODUCTION.FELT_CONSIGNMENT_DETAIL WHERE  CON_NO='"+getAttribute("CON_NO").getString()+"'");



            //Now Update records into FELT_CONSIGNMENT tables
            for(int i=1;i<=hmFeltConsignmentDetails.size();i++) {
                clsFeltConsignmentDetails ObjFeltConsignmentDetails=(clsFeltConsignmentDetails) hmFeltConsignmentDetails.get(Integer.toString(i));
                
                //Insert records into Consignment detail table
                resultSetDetail.moveToInsertRow();
                resultSetDetail.updateString("CON_NO", getAttribute("CON_NO").getString());
                resultSetDetail.updateString("CON_DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CON_DOC_DATE").getString()));
                resultSetDetail.updateString("CON_PIECE_NO",ObjFeltConsignmentDetails.getAttribute("CON_PIECE_NO").getString());
                resultSetDetail.updateString("CON_PRODUCT_CODE",ObjFeltConsignmentDetails.getAttribute("CON_PRODUCT_CODE").getString());
                resultSetDetail.updateString("CON_LENGTH",ObjFeltConsignmentDetails.getAttribute("CON_LENGTH").getString());
                resultSetDetail.updateString("CON_WIDTH",ObjFeltConsignmentDetails.getAttribute("CON_WIDTH").getString());
                resultSetDetail.updateString("CON_GSQ",ObjFeltConsignmentDetails.getAttribute("CON_GSQ").getString());
                resultSetDetail.updateString("CON_AGREED_DATE",ObjFeltConsignmentDetails.getAttribute("CON_AGREED_DATE").getString());
                resultSetDetail.updateString("CON_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CON_DATE").getString()));
                resultSetDetail.updateInt("CREATED_BY",getAttribute("CREATED_BY").getInt());
                resultSetDetail.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                resultSetDetail.updateInt("MODIFIED_BY",getAttribute("MODIFIED_BY").getInt());
                resultSetDetail.updateString("MODIFIED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetDetail.updateInt("CANCELED",0);
                resultSetDetail.updateInt("CHANGED",1);
                resultSetDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetail.insertRow();
                
               
                
                //Insert records into FELT_CONSIGNMENT_H table
                resultSetDetailHistory.moveToInsertRow();
                resultSetDetailHistory.updateInt("REVISION_NO",revisionNo);
                resultSetDetailHistory.updateString("CON_NO", getAttribute("CON_NO").getString());
                resultSetDetailHistory.updateString("CON_DOC_DATE",EITLERPGLOBAL.formatDateDB(ObjFeltConsignmentDetails.getAttribute("CON_DOC_DATE").getString()));
                resultSetDetailHistory.updateString("CON_PIECE_NO",ObjFeltConsignmentDetails.getAttribute("CON_PIECE_NO").getString());
                resultSetDetailHistory.updateString("CON_PRODUCT_CODE",ObjFeltConsignmentDetails.getAttribute("CON_PRODUCT_CODE").getString());
                resultSetDetailHistory.updateString("CON_LENGTH",ObjFeltConsignmentDetails.getAttribute("CON_LENGTH").getString());
                resultSetDetailHistory.updateString("CON_WIDTH",ObjFeltConsignmentDetails.getAttribute("CON_WIDTH").getString());
                resultSetDetailHistory.updateString("CON_GSQ",ObjFeltConsignmentDetails.getAttribute("CON_GSQ").getString());
                resultSetDetailHistory.updateString("CON_AGREED_DATE",ObjFeltConsignmentDetails.getAttribute("CON_AGREED_DATE").getString());
                resultSetDetailHistory.updateString("CON_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CON_DATE").getString()));
                resultSetDetailHistory.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
                resultSetDetailHistory.updateString("CREATED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetDetailHistory.updateInt("MODIFIED_BY",0);
                resultSetDetailHistory.updateString("MODIFIED_DATE","0000-00-00");
                resultSetDetailHistory.updateInt("CHANGED",1);
                resultSetDetailHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetailHistory.insertRow();
            }
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=721; //Felt Consignment Module
            ObjFeltProductionApprovalFlow.DocNo=getAttribute("CON_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.FELT_CONSIGNMENT";
            ObjFeltProductionApprovalFlow.IsCreator=false;
            ObjFeltProductionApprovalFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName="CON_NO";
            
            if(getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFeltProductionApprovalFlow.To=(int)getAttribute("SEND_DOC_TO").getVal();
                ObjFeltProductionApprovalFlow.ExplicitSendTo=true;
            }
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.FELT_CONSIGNMENT SET REJECTED=0,CHANGED=1 WHERE CON_NO ='"+getAttribute("CON_NO").getString()+"'");
                //Remove Old Records from FELT DOCUMET APPROVAL TABLE
                //data.Execute("DELETE FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=721 AND DOC_NO='"+getAttribute("PRODUCTION_DOCUMENT_NO").getString()+"'");
                
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
            
            // Update Weaving Date in Order Master Table To confirm that Weaving has completed
            if(ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {
                ObjFeltProductionApprovalFlow.finalApproved=false;
                //          updateWeavingDate(getAttribute("CON_NO").getString());
            }
            //--------- Approval Flow Update complete -----------
            setData();
            resultSetDetail.close();
            statementDetail.close();
            resultSetDetailHistory.close();
            statementDetailHistory.close();
            resultSetHistory.close();
            statementHistory.close();
            //  resultSetDetailHistory.close();
            //  statementDetailHistory.close();
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
 //   public boolean CanDelete(String documentNo,String stringProductionDate,int userID) {
      public boolean CanDelete(int consignmentNo,String pDocDate,int userID) {
    
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
         try {
            if(HistoryView) {
                return false;
            }
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_CONSIGNMENT WHERE  CON_NO='"+consignmentNo +"' AND APPROVED="+1;            
          //  tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            tmpStmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
              //  strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=708 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=721 AND USER_ID="+userID+" AND DOC_NO='"+ consignmentNo+"' AND STATUS='W'";
               tmpStmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
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
            int lCompanyID=EITLERPGLOBAL.gCompanyID;
            String ConNo=(String)getAttribute("CON_NO").getObj();
            String strSQL="";
            
            //First check that record is editable
            if(CanDelete(lCompanyID,ConNo,pUserID)) {
                String strQry = "DELETE FROM PRODUCTION.FELT_CONSIGNMENT_H WHERE CON_NO='"+ConNo+"'";
                data.Execute(strQry);
                strQry = "DELETE FROM PRODUCTION.FELT_CONSIGNMENT WHERE CON_NO='"+ConNo+"'";
                data.Execute(strQry);
                
               LoadData();
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
    
    /**
     * This routine checks and returns whether the item is editable or not.
     * Criteria is Approved item cannot be changed and if not approved then user id is
     * checked whether doucment is waiting for his approval.
     */
    public boolean IsEditable(String consignmentNo,int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        try {
            if(HistoryView) {
                return false;
            }
            tmpStmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_CONSIGNMENT WHERE CON_NO='"+ consignmentNo +"' AND APPROVED=1";
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0){  //Item is Approved
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=721 AND USER_ID="+userID+" AND DOC_NO='"+ consignmentNo +"' AND STATUS='W'";
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
            String strSql = "SELECT DISTINCT CON_DOC_DATE FROM PRODUCTION.FELT_CONSIGNMENT WHERE  " + stringFindQuery + " ORDER BY CON_DOC_DATE";
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
       //     setAttribute("CON_DATE",resultSet.getDate("CON_DATE"));
            setAttribute("CON_NO",resultSet.getString("CON_NO"));
            setAttribute("CON_DOC_DATE",resultSet.getString("CON_DOC_DATE"));
            setAttribute("CON_PARTY_CODE",resultSet.getString("CON_PARTY_CODE"));
            setAttribute("CON_PARTY_NAME",resultSet.getString("CON_PARTY_NAME"));
            setAttribute("CON_DATE",resultSet.getString("CON_DATE"));

            hmFeltConsignmentDetails.clear();
              statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            if(HistoryView) {
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_CONSIGNMENT_DETAIL_H WHERE CON_NO='"+ resultSet.getString("CON_NO") +"'  AND REVISION_NO="+RevNo);
            }else {
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_CONSIGNMENT_DETAIL WHERE CON_NO='"+ resultSet.getString("CON_NO") +"' ");
            }
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
                //              setAttribute("PRODUCTION_FORM_NO",resultSetTemp.getString("PROD_FORM_NO"));
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
                
                clsFeltConsignmentDetails ObjFeltConsignmentDetails = new clsFeltConsignmentDetails();
                
                ObjFeltConsignmentDetails.setAttribute("CON_PIECE_NO",resultSetTemp.getString("CON_PIECE_NO"));
                ObjFeltConsignmentDetails.setAttribute("CON_PRODUCT_CODE",resultSetTemp.getString("CON_PRODUCT_CODE"));
                ObjFeltConsignmentDetails.setAttribute("CON_LENGTH",resultSetTemp.getString("CON_LENGTH"));
                ObjFeltConsignmentDetails.setAttribute("CON_WIDTH",resultSetTemp.getString("CON_WIDTH"));
                ObjFeltConsignmentDetails.setAttribute("CON_GSQ",resultSetTemp.getString("CON_GSQ"));
                ObjFeltConsignmentDetails.setAttribute("CON_AGREED_DATE",resultSetTemp.getString("CON_AGREED_DATE"));
                //ObjFeltConsignmentDetails.setAttribute("CON_DATE",resultSetTemp.getString("CON_DATE"));
                
               
                hmFeltConsignmentDetails.put(Integer.toString(serialNoCounter),ObjFeltConsignmentDetails);
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
            //RevNo=resultSet.getInt("REVISION_NO");
            //setAttribute("REVISION_NO",resultSet.getInt("REVISION_NO"));
            
            //Now Populate the collection, first clear the collection
            hmFeltConsignmentDetails.clear();
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_CONSIGNMENT_H WHERE CON_DOC_DATE='"+ pProductionDate+"' AND CON_NO='"+pDocNo+"'");
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
                setAttribute("CON_NO",resultSetTemp.getString("CON_NO"));
                //          setAttribute("PRODUCTION_FORM_NO",resultSetTemp.getString("PROD_FORM_NO"));
                setAttribute("UPDATED_BY",resultSetTemp.getInt("UPDATED_BY"));
                setAttribute("ENTRY_DATE",resultSetTemp.getString("ENTRY_DATE"));
                setAttribute("HIERARCHY_ID",resultSetTemp.getInt("HIERARCHY_ID"));
                
                clsFeltConsignmentDetails ObjFeltConsignmentDetails = new clsFeltConsignmentDetails();
                
                
                ObjFeltConsignmentDetails.setAttribute("CON_PIECE_NO",resultSetTemp.getString("CON_PIECE_NO"));
                ObjFeltConsignmentDetails.setAttribute("CON_PARTY_CODE",resultSetTemp.getString("CON_PARTY_CODE"));
                ObjFeltConsignmentDetails.setAttribute("CON_PRODUCT_CODE",resultSetTemp.getString("CON_PRODUCT_CODE"));
                ObjFeltConsignmentDetails.setAttribute("CON_LENGTH",resultSetTemp.getString("CON_LENGTH"));
                ObjFeltConsignmentDetails.setAttribute("CON_WIDTH",resultSetTemp.getString("CON_WIDTH"));
                ObjFeltConsignmentDetails.setAttribute("CON_GSQ",resultSetTemp.getString("CON_GSQ"));
                ObjFeltConsignmentDetails.setAttribute("CON_AGREED_DATE",resultSetTemp.getString("CON_AGREED_DATE"));
                
                ObjFeltConsignmentDetails.setAttribute("CON_DATE",resultSetTemp.getString("CON_DATE"));
              /*
               
               
                ObjFeltConsignmentDetails.setAttribute("PRODUCTION_PIECE_NO",resultSetTemp.getString("PROD_PIECE_NO"));
                ObjFeltConsignmentDetails.setAttribute("PRODUCTION_PARTY_CODE",resultSetTemp.getString("PROD_PARTY_CODE"));
                ObjFeltConsignmentDetails.setAttribute("WEIGHT",resultSetTemp.getFloat("WEIGHT"));
                ObjFeltConsignmentDetails.setAttribute("PICKS_PER_10CMS",resultSetTemp.getString("PICKS_PER_10CMS"));
                ObjFeltConsignmentDetails.setAttribute("REED_SPACE",resultSetTemp.getFloat("REED_SPACE"));
                ObjFeltConsignmentDetails.setAttribute("LENGTH",resultSetTemp.getFloat("LENGTH"));
                ObjFeltConsignmentDetails.setAttribute("LOOM_NO",resultSetTemp.getInt("LOOM_NO"));
                ObjFeltConsignmentDetails.setAttribute("REMARKS",resultSetTemp.getString("REMARKS"));
                ObjFeltConsignmentDetails.setAttribute("WEAVE_DATE",resultSetTemp.getString("WEAVE_DATE"));
                ObjFeltConsignmentDetails.setAttribute("WARP_NO",resultSetTemp.getString("WARP_NO"));
                ObjFeltConsignmentDetails.setAttribute("WEAVE_DIFF_DAYS",resultSetTemp.getString("WEAVE_DIFF_DAYS"));
               */
                
                hmFeltConsignmentDetails.put(Integer.toString(serialNoCounter),ObjFeltConsignmentDetails);
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
    
    public static HashMap getHistoryList(String productionDocumentNo) {
        HashMap hmHistoryList=new HashMap();
        Statement stTmp;
        ResultSet rsTmp;
        try {
            stTmp=connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp=stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_CONSIGNMENT_H WHERE CON_NO='"+productionDocumentNo+"'");
            
            while(rsTmp.next()) {
                clsFeltConsignment ObjFeltConsignment = new clsFeltConsignment();
                
                ObjFeltConsignment.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjFeltConsignment.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjFeltConsignment.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjFeltConsignment.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjFeltConsignment.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                hmHistoryList.put(Integer.toString(hmHistoryList.size()+1),ObjFeltConsignment);
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
            resultSet=statement.executeQuery("SELECT DISTINCT FROM PRODUCTION.FELT_CONSIGNMENT_H WHERE CON_DOC_DATE='"+ pProductionDate+"' AND CON_NO ='"+pDocNo+"'");
            Ready=true;
            historyConsignmentDate = pProductionDate;
            historyConsignmentNo = pDocNo;
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
                strSQL="SELECT DISTINCT CON_NO,CON_DOC_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_CONSIGNMENT,PRODUCTION.FELT_PROD_DOC_DATA WHERE CON_NO=DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=721 AND CANCELED=0 ORDER BY RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT DISTINCT CON_NO,CON_DOC_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_CONSIGNMENT,PRODUCTION.FELT_PROD_DOC_DATA WHERE CON_NO=DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=721 AND CANCELED=0 ORDER BY CON_DOC_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT DISTINCT CON_NO,CON_DOC_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_CONSIGNMENT,PRODUCTION.FELT_PROD_DOC_DATA WHERE CON_NO=DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=721 AND CANCELED=0 ORDER BY CON_NO";
            }
            
            ResultSet rsTmp=data.getConn().createStatement().executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp.next()) {
                
                Counter=Counter+1;
                clsFeltConsignment ObjDoc=new clsFeltConsignment();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO",rsTmp.getString("CON_NO"));
                ObjDoc.setAttribute("DOC_DOC_DATE",rsTmp.getString("CON_DOC_DATE"));
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
        return data.getStringValueFromDB("SELECT PR_PARTY_CODE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO+0='"+pPieceNo+"'");
    }
    public String getProductCode(String pPieceNo) {
        return data.getStringValueFromDB("SELECT PR_PRODUCT_CODE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO+0='"+pPieceNo+"'");
        
    }
    
    public String getLength(String pPieceNo) {
        return data.getStringValueFromDB("SELECT PR_LENGTH FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO+0='"+pPieceNo+"'");
        
    }
    public String getWidth(String pPieceNo) {
        return data.getStringValueFromDB("SELECT PR_WIDTH FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO+0='"+pPieceNo+"'");
        
    }
    public String getGSQ(String pPieceNo) {
        return data.getStringValueFromDB("SELECT PR_GSM FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO+0='"+pPieceNo+"'");
        
    }
    
    public String getAgreedDate(String pPieceNo) {
        return data.getStringValueFromDB("SELECT PR_REQUESTED_MONTH FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO+0='"+pPieceNo+"'");
        
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
        int count=data.getIntValueFromDB("SELECT COUNT(CON_PIECE_NO) FROM PRODUCTION.FELT_CONSIGNMENT WHERE CON_PIECE_NO='"+pPieceNo+"' AND CON_DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND CON_DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
        if(count>0) return true;
        else return false;
    }
    
    public boolean checkPieceNoInDB(String pPieceNo, String pProdDate){
        int count=data.getIntValueFromDB("SELECT COUNT(CON_PIECE_NO) FROM PRODUCTION.FELT_CONSIGNMENT WHERE CON_PIECE_NO='"+pPieceNo+"' AND CON_DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND CON_DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
        if(count>=1){
            int counter=0;
            try{
                Connection Conn=data.getConn();
                Statement stTmp=Conn.createStatement();
                ResultSet rsTmp=stTmp.executeQuery("SELECT CON_DATE FROM PRODUCTION.FELT_CONSIGNMENT WHERE CON_PIECE_NO='"+pPieceNo+"' AND CON_DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND CON_DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
                while(rsTmp.next()){
                    if(rsTmp.getString("CON_DOC_DATE").equals(EITLERPGLOBAL.formatDateDB(pProdDate))) counter++;
                }
            }catch(SQLException e){e.printStackTrace();}
            if(counter==1 && count>=2) return true;
            else if(counter==1) return false;
            else return true;
        }else return false;
    }
    
    // checks production date already exist in db
    public boolean checkProductionDateInDB(String pProdDate){
        int count=data.getIntValueFromDB("SELECT COUNT(CON_DOC_DATE) FROM PRODUCTION.FELT_CONSIGNMENT WHERE CON_DOC_DATE='"+pProdDate+"'");
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
    
    /*
    // generates report data
    public  TReportWriter.SimpleDataProvider.TTable getReportData(String prodDate) {
        TReportWriter.SimpleDataProvider.TTable objData=new TReportWriter.SimpleDataProvider.TTable();
        objData.AddColumn("SR_NO");
        objData.AddColumn("FORM_NO");
        objData.AddColumn("PROD_DATE");
        objData.AddColumn("DOC_NO");
        objData.AddColumn("PIECE_NO");
        objData.AddColumn("STYLE");
        objData.AddColumn("PARTY_NAME");
        objData.AddColumn("WEIGHT");
        objData.AddColumn("PICKS_PER_10CMS");
        objData.AddColumn("REED_SPACE");
        objData.AddColumn("LENGTH");
        objData.AddColumn("LOOM_NO");
        objData.AddColumn("ORDER_LENGTH");
        objData.AddColumn("ORDER_WIDTH");
        objData.AddColumn("GSQ");
        objData.AddColumn("GROUP");
        objData.AddColumn("DAY_WEIGHT");
        objData.AddColumn("PREVIOUS_WEIGHT");
        objData.AddColumn("BASE_WEIGHT");
        objData.AddColumn("CLOTH_WEIGHT");
        objData.AddColumn("TOTAL_WEIGHT");
     
        try{
            TReportWriter.SimpleDataProvider.TRow objRow=objData.newRow();
     
            String str ="SELECT PROD_DATE,PROD_FORM_NO,PROD_PIECE_NO,STYLE, WEIGHT,PICKS_PER_10CMS,REED_SPACE,LENGTH,LOOM_NO,GSQ,LNGTH,WIDTH,PARTY_NAME,PREVIOUS_WEIGHT,DAY_WEIGHT,TOTAL_WEIGHT,BASE_WEIGHT,CLOTH_WEIGHT,GRUP FROM (SELECT PROD_DATE,PROD_FORM_NO,REPLACE(PROD_PIECE_NO,' ','') PROD_PIECE_NO,PROD_PARTY_CODE,WEIGHT,PICKS_PER_10CMS,REED_SPACE,LENGTH,LOOM_NO FROM PRODUCTION.FELT_PROD_DATA A WHERE PROD_DATE = '"+prodDate+"' AND PROD_DEPT='WEAVING') D LEFT JOIN (SELECT REPLACE(BALNK,' ','') AS STYLE,GRUP, GSQ,LNGTH,WIDTH,PRODUCT_CODE,PIECE_NO,PARTY_CD FROM PRODUCTION.FELT_ORDER_MASTER) O ON O.PARTY_CD = D.PROD_PARTY_CODE AND O.PIECE_NO +0 = D.PROD_PIECE_NO +0 LEFT JOIN (SELECT PARTY_CODE,TRIM(PARTY_NAME) PARTY_NAME FROM D_SAL_PARTY_MASTER) AS P ON P.PARTY_CODE = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS PREVIOUS_WEIGHT FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'WEAVING' AND MONTH(PROD_DATE)=MONTH('"+prodDate+"') AND YEAR(PROD_DATE)=YEAR('"+prodDate+"') AND PROD_DATE<'"+prodDate+"' AND APPROVED=1) AS W ON O.PARTY_CD = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS DAY_WEIGHT FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'WEAVING' AND MONTH(PROD_DATE)=MONTH('"+prodDate+"') AND YEAR(PROD_DATE)=YEAR('"+prodDate+"') AND PROD_DATE='"+prodDate+"' AND APPROVED=1) AS DW ON O.PARTY_CD = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS TOTAL_WEIGHT FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'WEAVING' AND MONTH(PROD_DATE)=MONTH('"+prodDate+"') AND YEAR(PROD_DATE)=YEAR('"+prodDate+"') AND PROD_DATE<='"+prodDate+"' AND APPROVED=1) AS T ON O.PARTY_CD = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS BASE_WEIGHT FROM (SELECT BALNK AS STYLE, A.WEIGHT FROM PRODUCTION.FELT_PROD_DATA A, PRODUCTION.FELT_ORDER_MASTER B WHERE PIECE_NO +0 = PROD_PIECE_NO +0 AND PROD_DATE <= '"+prodDate+"' AND PROD_DEPT='WEAVING' AND MONTH(PROD_DATE)=MONTH('"+prodDate+"') AND YEAR(PROD_DATE)=YEAR('"+prodDate+"') AND APPROVED=1) AS O WHERE SUBSTRING(O.STYLE,1,3) IN('XY','XYA','Y')) AS BW ON O.PARTY_CD = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS CLOTH_WEIGHT FROM  (SELECT BALNK AS STYLE, A.WEIGHT FROM PRODUCTION.FELT_PROD_DATA A, PRODUCTION.FELT_ORDER_MASTER B WHERE PIECE_NO +0 = PROD_PIECE_NO +0 AND PROD_DATE <= '"+prodDate+"' AND PROD_DEPT='WEAVING' AND MONTH(PROD_DATE)=MONTH('"+prodDate+"') AND YEAR(PROD_DATE)=YEAR('"+prodDate+"') AND APPROVED=1) AS O WHERE SUBSTRING(O.STYLE,1,3) NOT IN('XY','XYA','Y')) AS CW ON O.PARTY_CD = D.PROD_PARTY_CODE";
            ResultSet  rsTemp =data.getResult(str);
            int counter=1;
            while(!rsTemp.isAfterLast()){
                objRow=objData.newRow();
                objRow.setValue("SR_NO", String.valueOf(counter));
                objRow.setValue("FORM_NO", rsTemp.getString("PROD_FORM_NO"));
                objRow.setValue("PROD_DATE", EITLERPGLOBAL.formatDate(rsTemp.getString("PROD_DATE")));
                objRow.setValue("PIECE_NO", rsTemp.getString("PROD_PIECE_NO"));
                objRow.setValue("STYLE", rsTemp.getString("STYLE"));
                objRow.setValue("PARTY_NAME", rsTemp.getString("PARTY_NAME"));
                objRow.setValue("WEIGHT", rsTemp.getString("WEIGHT"));
                objRow.setValue("PICKS_PER_10CMS",rsTemp.getString("PICKS_PER_10CMS"));
                objRow.setValue("REED_SPACE",rsTemp.getString("REED_SPACE"));
                objRow.setValue("LENGTH", rsTemp.getString("LENGTH"));
                objRow.setValue("LOOM_NO",rsTemp.getString("LOOM_NO"));
                objRow.setValue("ORDER_LENGTH",rsTemp.getString("LNGTH"));
                objRow.setValue("ORDER_WIDTH",rsTemp.getString("WIDTH"));
                objRow.setValue("ORDER_GSQ",rsTemp.getString("GSQ"));
                objRow.setValue("ORDER_GROUP", rsTemp.getString("GRUP"));
                objRow.setValue("DAY_WEIGHT", rsTemp.getString("DAY_WEIGHT"));
                objRow.setValue("PREVIOUS_WEIGHT", rsTemp.getString("PREVIOUS_WEIGHT"));
                objRow.setValue("BASE_WEIGHT", rsTemp.getString("BASE_WEIGHT"));
                objRow.setValue("CLOTH_WEIGHT", rsTemp.getString("CLOTH_WEIGHT"));
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
     */
    
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
            String strSQL="SELECT PIECE_NO,PARTY_CD,PRODUCT_CODE,LNGTH,WIDTH,GSQ,AGREED_DATE FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO="+pPieceNo+"";
            rsTmp=stTmp.executeQuery(strSQL);
            System.out.println(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow() > 0) {
                
                Piecedetail[1]=rsTmp.getString("PIECE_NO");
                Piecedetail[2]=rsTmp.getString("PARTY_CODE");
                Piecedetail[2]=rsTmp.getString("PRODUCT_C0DE");
                Piecedetail[3]=rsTmp.getString("LNGTH");
                Piecedetail[4]=rsTmp.getString("WIDTH");
                Piecedetail[5]=rsTmp.getString("GSQ");
                Piecedetail[6]=rsTmp.getString("AGREED_DATE");
                
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
            strSQL="SELECT LAST_USED_NO,PADDING_BY,NO_LENGTH,PREFIX_CHARS,SUFFIX_CHARS FROM D_COM_FIRSTFREE WHERE COMPANY_ID=2 AND MODULE_ID=721 AND FIRSTFREE_NO=174";
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
                    data.Execute("UPDATE D_COM_FIRSTFREE SET LAST_USED_NO='"+strNewNo.trim()+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=2 AND MODULE_ID=721 AND FIRSTFREE_NO=174");
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
    
     public static String getParyName(int pCompanyID,String pPartyID) {
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
}


