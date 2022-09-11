/*
 * clsFeltGroupMasterAmend.java
 *
 * Created on 12, 12, 2016, 5:31 PM
 */

package EITLERP.FeltSales.GroupMasterAmend;

import EITLERP.FeltSales.GroupMaster.*;
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

public class clsFeltGroupMasterAmend {

    public static void CancelDoc(String pDocNo) {
        ResultSet rsTmp=null;
        
        if(CanCancel(pDocNo)) {
            
            boolean Approved=false;
            
            try {
                rsTmp=data.getResult("SELECT APPROVED FROM PRODUCTION.FELT_GROUP_MASTER_HEADER_AMEND WHERE DOC_NO='"+pDocNo+"' ");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    Approved=rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();
                
                if(!Approved) {
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='"+pDocNo+"' AND MODULE_ID=743");
                }
                data.Execute("UPDATE PRODUCTION.FELT_GROUP_MASTER_HEADER_AMEND SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='"+pDocNo+"'");
                
            }
            catch(Exception e) {
                
            }
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
            System.out.println("SELECT DOC_NO FROM PRODUCTION.FELT_GROUP_MASTER_HEADER_AMEND WHERE DOC_NO='"+pDocNo+"'  AND APPROVED=0 AND CANCELED=0");
            rsTmp=stTmp.executeQuery("SELECT DOC_NO FROM PRODUCTION.FELT_GROUP_MASTER_HEADER_AMEND WHERE DOC_NO='"+pDocNo+"'  AND APPROVED=0 AND CANCELED=0");
            
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
    public clsFeltGroupMasterAmend() {
        LastError = "";
        props=new HashMap();
        props.put("DOC_NO", new Variant(""));
        props.put("DOC_DATE", new Variant(""));
        props.put("GROUP_CODE", new Variant(""));
        props.put("GROUP_DESC", new Variant(""));
        props.put("AMEND_REASON", new Variant(""));
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
            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.FELT_GROUP_MASTER_HEADER_AMEND  ORDER BY DOC_NO");
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
            resultSetDetail = statementDetail.executeQuery("SELECT * FROM  PRODUCTION.FELT_GROUP_MASTER_DETAIL_AMEND WHERE DOC_NO=''");
            
           
            statementDetailHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetDetailHistory = statementDetailHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_GROUP_MASTER_DETAIL_AMEND_H WHERE DOC_NO=''");
            
            
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_GROUP_MASTER_HEADER_AMEND_H WHERE DOC_NO=''");
            
           
            setAttribute("DOC_NO",EITLERP.clsFirstFree.getNextFreeNo(2, 743, getAttribute("FFNO").getInt(), true));
            
           
//-------------------------------------------------
            
            resultSet.moveToInsertRow();
            resultSet.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSet.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
            resultSet.updateString("GROUP_CODE", getAttribute("GROUP_CODE").getString());
            resultSet.updateString("GROUP_DESC", getAttribute("GROUP_DESC").getString());
            resultSet.updateString("AMEND_REASON", getAttribute("AMEND_REASON").getString());
            resultSet.updateString("GROUP_CRITICAL_LIMIT", getAttribute("GROUP_CRITICAL_LIMIT").getString());
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
            resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSetHistory.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
            resultSetHistory.updateString("GROUP_CODE", getAttribute("GROUP_CODE").getString());
            resultSetHistory.updateString("GROUP_DESC", getAttribute("GROUP_DESC").getString());
            resultSetHistory.updateString("AMEND_REASON", getAttribute("AMEND_REASON").getString());
            resultSetHistory.updateString("GROUP_CRITICAL_LIMIT", getAttribute("GROUP_CRITICAL_LIMIT").getString());
            resultSetHistory.updateInt("UPDATED_BY", getAttribute("UPDATED_BY").getInt());
            resultSetHistory.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
            resultSetHistory.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSetHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            resultSetHistory.updateString("APPROVER_REMARKS", getAttribute("FROM_REMARKS").getString());
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
                clsFeltGroupMasterAmendDetails ObjFeltGroupMasterDetails=(clsFeltGroupMasterAmendDetails) hmFeltGroupMasterDetails.get(Integer.toString(i));
                
                //Insert records into Group detail table
                resultSetDetail.moveToInsertRow();
                resultSetDetail.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetDetail.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
                resultSetDetail.updateString("GROUP_CODE", getAttribute("GROUP_CODE").getString());
                resultSetDetail.updateString("PARTY_CODE",ObjFeltGroupMasterDetails.getAttribute("PARTY_CODE").getString());
                resultSetDetail.updateString("PARTY_NAME",ObjFeltGroupMasterDetails.getAttribute("PARTY_NAME").getString());
                resultSetDetail.updateString("PARTY_ACTIVE",ObjFeltGroupMasterDetails.getAttribute("PARTY_ACTIVE").getString());
                resultSetDetail.updateString("CRITICAL_LIMIT",ObjFeltGroupMasterDetails.getAttribute("CRITICAL_LIMIT").getString());
                resultSetDetail.updateString("CASH_DISC_FLAG",ObjFeltGroupMasterDetails.getAttribute("CASH_DISC_FLAG").getString());
                resultSetDetail.updateString("YEAR_END_DISC_FLAG",ObjFeltGroupMasterDetails.getAttribute("YEAR_END_DISC_FLAG").getString());
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
                resultSetDetailHistory.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
                resultSetDetailHistory.updateString("GROUP_CODE", getAttribute("GROUP_CODE").getString());
                resultSetDetailHistory.updateString("PARTY_CODE",ObjFeltGroupMasterDetails.getAttribute("PARTY_CODE").getString());
                resultSetDetailHistory.updateString("PARTY_NAME",ObjFeltGroupMasterDetails.getAttribute("PARTY_NAME").getString());
                resultSetDetailHistory.updateString("PARTY_ACTIVE",ObjFeltGroupMasterDetails.getAttribute("PARTY_ACTIVE").getString());
                resultSetDetailHistory.updateString("CRITICAL_LIMIT",ObjFeltGroupMasterDetails.getAttribute("CRITICAL_LIMIT").getString());
                resultSetDetailHistory.updateString("CASH_DISC_FLAG",ObjFeltGroupMasterDetails.getAttribute("CASH_DISC_FLAG").getString());
                resultSetDetailHistory.updateInt("UPDATED_BY",getAttribute("UPDATED_BY").getInt());
                resultSetDetailHistory.updateInt("CREATED_BY",getAttribute("CREATED_BY").getInt());
                resultSetDetailHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetailHistory.updateInt("CHANGED",1);
                resultSetDetailHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetailHistory.insertRow();
            }
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=743; //Felt Group
            ObjFeltProductionApprovalFlow.DocNo=getDocumentNo(getAttribute("DOC_NO").getString());
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.FELT_GROUP_MASTER_HEADER_AMEND";
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
            resultSetDetail = statementDetail.executeQuery("SELECT * FROM  PRODUCTION.FELT_GROUP_MASTER_DETAIL_AMEND WHERE DOC_NO=''");
            
            // Group detail history connection
            statementDetailHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetDetailHistory = statementDetailHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_GROUP_MASTER_DETAIL_AMEND_H WHERE DOC_NO=''");
            
            // Group data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_GROUP_MASTER_HEADER_AMEND_H WHERE DOC_NO=''");
            
            
            resultSet.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSet.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
            resultSet.updateString("GROUP_CODE", getAttribute("GROUP_CODE").getString());
            resultSet.updateString("GROUP_DESC", getAttribute("GROUP_DESC").getString());
            resultSet.updateString("AMEND_REASON", getAttribute("AMEND_REASON").getString());
            resultSet.updateString("GROUP_CRITICAL_LIMIT", getAttribute("GROUP_CRITICAL_LIMIT").getString());
            resultSet.updateInt("MODIFIED_BY",getAttribute("MODIFIED_BY").getInt());
            resultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            resultSet.updateInt("CHANGED",1);
            resultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int revisionNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_GROUP_MASTER_HEADER_AMEND_H WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
            revisionNo++;
            
            resultSetHistory.moveToInsertRow();
            resultSetHistory.updateInt("REVISION_NO",revisionNo);
            resultSetHistory.updateString("ENTRY_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSetHistory.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
            resultSetHistory.updateString("GROUP_CODE", getAttribute("GROUP_CODE").getString());
            resultSetHistory.updateString("GROUP_DESC", getAttribute("GROUP_DESC").getString());
            resultSetHistory.updateString("AMEND_REASON", getAttribute("AMEND_REASON").getString());
            resultSetHistory.updateString("GROUP_CRITICAL_LIMIT", getAttribute("GROUP_CRITICAL_LIMIT").getString());
            resultSetHistory.updateInt("UPDATED_BY",getAttribute("UPDATED_BY").getInt());
           resultSetHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            resultSetHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            resultSetHistory.updateInt("MODIFIED_BY",getAttribute("MODIFIED_BY").getInt());
            resultSetHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
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
            data.Execute("DELETE FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL_AMEND WHERE  DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
            
            //Now insert records into Group detail tables
            for(int i=1;i<=hmFeltGroupMasterDetails.size();i++) {
                clsFeltGroupMasterAmendDetails ObjFeltGroupMasterDetails=(clsFeltGroupMasterAmendDetails) hmFeltGroupMasterDetails.get(Integer.toString(i));
                
                //Insert records into Group detail table
                resultSetDetail.moveToInsertRow();
                resultSetDetail.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetDetail.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
                resultSetDetail.updateString("GROUP_CODE", getAttribute("GROUP_CODE").getString());
                resultSetDetail.updateString("PARTY_CODE",ObjFeltGroupMasterDetails.getAttribute("PARTY_CODE").getString());
                resultSetDetail.updateString("PARTY_NAME",ObjFeltGroupMasterDetails.getAttribute("PARTY_NAME").getString());
                resultSetDetail.updateString("PARTY_ACTIVE",ObjFeltGroupMasterDetails.getAttribute("PARTY_ACTIVE").getString());
                resultSetDetail.updateString("CRITICAL_LIMIT",ObjFeltGroupMasterDetails.getAttribute("CRITICAL_LIMIT").getString());
                resultSetDetail.updateString("CASH_DISC_FLAG",ObjFeltGroupMasterDetails.getAttribute("CASH_DISC_FLAG").getString());
                resultSetDetail.updateString("YEAR_END_DISC_FLAG",ObjFeltGroupMasterDetails.getAttribute("YEAR_END_DISC_FLAG").getString());
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
                resultSetDetailHistory.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
                resultSetDetailHistory.updateString("GROUP_CODE", getAttribute("GROUP_CODE").getString());
                resultSetDetailHistory.updateString("PARTY_CODE",ObjFeltGroupMasterDetails.getAttribute("PARTY_CODE").getString());
                resultSetDetailHistory.updateString("PARTY_NAME",ObjFeltGroupMasterDetails.getAttribute("PARTY_NAME").getString());
                resultSetDetailHistory.updateString("PARTY_ACTIVE",ObjFeltGroupMasterDetails.getAttribute("PARTY_ACTIVE").getString());
                resultSetDetailHistory.updateString("CRITICAL_LIMIT",ObjFeltGroupMasterDetails.getAttribute("CRITICAL_LIMIT").getString());
                resultSetDetailHistory.updateString("CASH_DISC_FLAG",ObjFeltGroupMasterDetails.getAttribute("CASH_DISC_FLAG").getString());
                resultSetDetailHistory.updateString("YEAR_END_DISC_FLAG",ObjFeltGroupMasterDetails.getAttribute("YEAR_END_DISC_FLAG").getString());
                
                resultSetDetailHistory.updateInt("UPDATED_BY",getAttribute("UPDATED_BY").getInt());
                //resultSetDetailHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("ENTRY_DATE").getString()));
                resultSetDetailHistory.updateInt("CHANGED",1);
                resultSetDetailHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetailHistory.insertRow();
            }
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=743; //Felt Group
            ObjFeltProductionApprovalFlow.DocNo=getDocumentNo(getAttribute("DOC_NO").getString());
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.FELT_GROUP_MASTER_HEADER_AMEND";
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
                data.Execute("UPDATE PRODUCTION.FELT_GROUP_MASTER_HEADER_AMEND SET REJECTED=0,CHANGED=1 WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"' ");
                
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
            if(ObjFeltProductionApprovalFlow.Status.equals("F")){
                
                data.Execute("DELETE FROM PRODUCTION.FELT_GROUP_MASTER_HEADER WHERE GROUP_CODE='"+getAttribute("GROUP_CODE").getString()+"'");
                data.Execute("INSERT INTO PRODUCTION.FELT_GROUP_MASTER_HEADER(GROUP_CODE,GROUP_DESC,GROUP_CRITICAL_LIMIT,APPROVED,APPROVED_DATE,CANCELED,CHANGED,CHANGED_DATE)(SELECT GROUP_CODE,GROUP_DESC,GROUP_CRITICAL_LIMIT,APPROVED,APPROVED_DATE,CANCELED,CHANGED,CHANGED_DATE FROM PRODUCTION.FELT_GROUP_MASTER_HEADER_AMEND WHERE GROUP_CODE='"+getAttribute("GROUP_CODE").getString()+"' AND DOC_NO='"+getAttribute("DOC_NO").getString()+"')");
                
                data.Execute("DELETE FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL WHERE GROUP_CODE='"+getAttribute("GROUP_CODE").getString()+"'");
                data.Execute("INSERT INTO PRODUCTION.FELT_GROUP_MASTER_DETAIL(GROUP_CODE,PARTY_CODE,PARTY_NAME,PARTY_ACTIVE,CRITICAL_LIMIT,CASH_DISC_FLAG,YEAR_END_DISC_FLAG)(SELECT GROUP_CODE,PARTY_CODE,PARTY_NAME,PARTY_ACTIVE,CRITICAL_LIMIT,CASH_DISC_FLAG,YEAR_END_DISC_FLAG FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL_AMEND WHERE GROUP_CODE='"+getAttribute("GROUP_CODE").getString()+"' AND DOC_NO='"+getAttribute("DOC_NO").getString()+"')");
               
                data.Execute("UPDATE PRODUCTION.FELT_GROUP_MASTER_DETAIL_AMEND A,PRODUCTION.FELT_GROUP_MASTER_HEADER_AMEND C,DINESHMILLS.D_SAL_PARTY_MASTER B SET B.AMOUNT_LIMIT=A.CRITICAL_LIMIT WHERE B.PARTY_CODE=A.PARTY_CODE AND C.DOC_NO=A.DOC_NO AND A.DOC_NO='"+getAttribute("DOC_NO").getString()+"' ");//AND C.APPROVED=1 AND C.CANCELED=0
                return true; 
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_GROUP_MASTER_HEADER_AMEND WHERE DOC_NO='"+baleNo +"' AND  APPROVED=1";
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if(rsTmp.getInt("COUNT")>0){  //Group is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            }else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=743 AND USER_ID="+userID+" AND DOC_NO='"+ getAttribute("DOC_NO").getString()+"' AND STATUS='W'";
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getInt("COUNT")>0) {
                    strSQL = "DELETE FROM PRODUCTION.FELT_GROUP_MASTER_HEADER_AMEND WHERE DOC_NO='"+baleNo+"'";
                    tmpStmt.executeUpdate(strSQL);
                    strSQL = "DELETE FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL_AMEND WHERE DOC_NO='"+baleNo+"'";
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_GROUP_MASTER_HEADER_AMEND WHERE DOC_NO='"+ baleNo +"' AND APPROVED=1";
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0){  //Item is Approved
                //Discard the request
                return false;
            }else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=743 AND USER_ID="+userID+" AND DOC_NO='"+baleNo+"' AND STATUS='W'";
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
            String strSql = "SELECT * FROM PRODUCTION.FELT_GROUP_MASTER_HEADER_AMEND WHERE " + stringFindQuery;
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
            setAttribute("GROUP_CODE",resultSet.getString("GROUP_CODE"));
            setAttribute("GROUP_DESC",resultSet.getString("GROUP_DESC"));
            setAttribute("AMEND_REASON",resultSet.getString("AMEND_REASON"));
            setAttribute("GROUP_CRITICAL_LIMIT",resultSet.getString("GROUP_CRITICAL_LIMIT"));
            setAttribute("HIERARCHY_ID",resultSet.getInt("HIERARCHY_ID"));
            
            //Now Populate the collection, first clear the collection
            hmFeltGroupMasterDetails.clear();
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            if(HistoryView) {
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_GROUP_MASTER_DETAIL_AMEND_H WHERE DOC_NO='"+ resultSet.getString("DOC_NO") +"'  AND REVISION_NO="+RevNo);
            }else {
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_GROUP_MASTER_DETAIL_AMEND WHERE DOC_NO='"+ resultSet.getString("DOC_NO") +"' ORDER BY PARTY_CODE ");
            }
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
                clsFeltGroupMasterAmendDetails ObjFeltGroupMasterDetails = new clsFeltGroupMasterAmendDetails();
                
                ObjFeltGroupMasterDetails.setAttribute("DOC_NO",resultSet.getString("DOC_NO"));
                ObjFeltGroupMasterDetails.setAttribute("DOC_DATE",resultSet.getString("DOC_DATE"));
                ObjFeltGroupMasterDetails.setAttribute("GROUP_CODE",resultSet.getString("GROUP_CODE"));
                ObjFeltGroupMasterDetails.setAttribute("PARTY_CODE",resultSetTemp.getString("PARTY_CODE"));
                ObjFeltGroupMasterDetails.setAttribute("PARTY_NAME",resultSetTemp.getString("PARTY_NAME"));
                ObjFeltGroupMasterDetails.setAttribute("PARTY_ACTIVE",resultSetTemp.getString("PARTY_ACTIVE"));
                ObjFeltGroupMasterDetails.setAttribute("CRITICAL_LIMIT",resultSetTemp.getString("CRITICAL_LIMIT"));
                ObjFeltGroupMasterDetails.setAttribute("CASH_DISC_FLAG",resultSetTemp.getString("CASH_DISC_FLAG"));
                ObjFeltGroupMasterDetails.setAttribute("YEAR_END_DISC_FLAG",resultSetTemp.getString("YEAR_END_DISC_FLAG"));
                
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
    
    public boolean ShowHistory(String baleNo) {
        Ready=false;
        try {
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.FELT_GROUP_MASTER_HEADER_AMEND_H WHERE DOC_NO='"+baleNo+"'");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_GROUP_MASTER_HEADER_AMEND_H WHERE DOC_NO='"+dpNo+"'");
            
            while(rsTmp.next()) {
                clsFeltGroupMasterAmend ObjFeltGroup=new clsFeltGroupMasterAmend();
                
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
                strSQL="SELECT H.DOC_NO,H.GROUP_CODE,RECEIVED_DATE,GROUP_DESC FROM PRODUCTION.FELT_GROUP_MASTER_HEADER_AMEND H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=743 AND CANCELED=0 ORDER BY RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT H.DOC_NO,H.GROUP_CODE,RECEIVED_DATE,GROUP_DESC FROM PRODUCTION.FELT_GROUP_MASTER_HEADER_AMEND H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=743 AND CANCELED=0 ORDER BY H.DOC_NO";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT H.DOC_NO,H.GROUP_CODE,RECEIVED_DATE,GROUP_DESC FROM PRODUCTION.FELT_GROUP_MASTER_HEADER_AMEND H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=743 AND CANCELED=0 ORDER BY H.DOC_NO";
            }
            
            ResultSet rsTmp=data.getConn().createStatement().executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp.next()) {
                
                Counter=Counter+1;
                clsFeltGroupMasterAmend ObjDoc=new clsFeltGroupMasterAmend();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                ObjDoc.setAttribute("GROUP_CODE",rsTmp.getString("GROUP_CODE"));
                ObjDoc.setAttribute("GROUP_DESC",rsTmp.getString("GROUP_DESC"));
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
        int count=data.getIntValueFromDB("SELECT COUNT(PKG_PIECE_NO) FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL_AMEND WHERE PKG_PIECE_NO='"+pieceNo+"' AND PKG_BALE_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PKG_BALE_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
        if(count>0) return true;
        else return false;
    }
    
    // checks piece no already exist in db in edit mode
    public boolean checkPieceNoInDB(String pieceNo, String baleNo, String baleDate){
        int count=data.getIntValueFromDB("SELECT COUNT(PKG_PIECE_NO) FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL_AMEND WHERE PKG_PIECE_NO='"+pieceNo+"' AND PKG_BALE_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PKG_BALE_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
        if(count>=1){
            int counter=0;
            try{
                Connection Conn=data.getConn();
                Statement stTmp=Conn.createStatement();
                ResultSet rsTmp=stTmp.executeQuery("SELECT PKG_BALE_DATE,GROUP_CODE FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL_AMEND WHERE PKG_PIECE_NO='"+pieceNo+"' AND PKG_BALE_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PKG_BALE_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
                while(rsTmp.next()){
                    if(rsTmp.getString("PKG_BALE_DATE").equals(EITLERPGLOBAL.formatDateDB(baleDate)) && rsTmp.getString("GROUP_CODE").equals(baleNo)) counter++;
                }
            }catch(SQLException e){e.printStackTrace();}
            if(counter==1 && count>=2) return true;
            else if(counter==1) return false;
            else return true;
        }else return false;
    }
    
    public static String getpartyname(String pcode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String partyname="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+pcode+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                partyname=rsTmp.getString("PARTY_NAME");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return partyname;
        }
        catch(Exception e) {
            return "";
        }
    }
       
    public static String getcritical(String pcode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String critical="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT CRITICAL_AMOUNT_LIMIT FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+pcode+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                critical=rsTmp.getString("CRITICAL_AMOUNT_LIMIT");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return critical;
        }
        catch(Exception e) {
            return "";
        }
        
    }
    
    public static String getgroupdesc(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String groupdesc="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT GROUP_DESC FROM PRODUCTION.FELT_GROUP_MASTER_HEADER WHERE GROUP_CODE='"+pPartyID+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                groupdesc=rsTmp.getString("GROUP_DESC");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return groupdesc;
        }
        catch(Exception e) {
            return "";
        }
        
    }
}
