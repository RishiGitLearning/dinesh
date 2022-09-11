/*
 * clsFeltRateUpdate.java
 *
 * Created on September 3, 2013, 5:10 PM
 */

package EITLERP.Production.FeltRateMaster;

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
 *
 * @author  Vivek Kumar
 */

public class clsFeltRateUpdate {
    public String LastError="";
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private HashMap props;
    public boolean Ready = false;
    //History Related properties
    public boolean HistoryView=false;
    //Rate Update Collection
    public HashMap hmRateUpdate;
    
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
    
    /** Creates new clsFeltRateUpdate */
    public clsFeltRateUpdate() {
        LastError = "";
        props=new HashMap();
        
        props.put("DOC_NO", new Variant(""));
        props.put("PRODUCT_CODE",new Variant(""));
        props.put("SQM_RATE",new Variant(0));
        props.put("WT_RATE",new Variant(0));
        props.put("CHARGES",new Variant(0));
        props.put("CHANGE_DESC",new Variant(""));
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("CANCELED", new Variant(false));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));        
        props.put("RECEIVED_DATE",new Variant(""));
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        
        //Create a new object for clsFeltRateUpdate collection
        hmRateUpdate=new HashMap();
    }
    
    public boolean LoadData() {
        Ready=false;
        try {
            connection=data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.FELT_RATE_UPDATE ORDER BY PRODUCT_CODE");
            HistoryView=false;
            Ready=true;
            MoveLast();
            return true;
        }catch(Exception e) {
            LastError=e.getMessage();
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
            return false;
        }
    }
    
    public boolean Insert() {
        ResultSet  resultSetHistory;
        Statement  statementHistory;
        
        try {
            // ---- Rate Master History Connection ------ //
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_RATE_UPDATE_H WHERE PRODUCT_CODE='1'"); // '1' for restricting all data retrieval
            
            resultSet.moveToInsertRow();
            resultSet.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSet.updateString("PRODUCT_CODE", getAttribute("PRODUCT_CODE").getString());
            resultSet.updateFloat("SQM_RATE", (float)getAttribute("SQM_RATE").getVal());
            resultSet.updateFloat("WT_RATE", (float)getAttribute("WT_RATE").getVal());
            resultSet.updateFloat("CHARGES", (float)getAttribute("CHARGES").getVal());
            resultSet.updateString("CHANGE_DESC", getAttribute("CHANGE_DESC").getString());
            resultSet.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
            resultSet.updateString("CREATED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSet.updateInt("MODIFIED_BY",0);
            resultSet.updateString("MODIFIED_DATE","0000-00-00");
            resultSet.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            resultSet.updateBoolean("APPROVED",false);
            resultSet.updateString("APPROVED_DATE","0000-00-00");
            resultSet.updateBoolean("REJECTED",false);
            resultSet.updateString("REJECTED_DATE","0000-00-00");
            resultSet.updateBoolean("CANCELED",false);
            resultSet.updateBoolean("CHANGED",true);
            resultSet.updateString("CHANGED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSet.insertRow();
            
            //========= Inserting Into History =================//
            resultSetHistory.moveToInsertRow();
            resultSetHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSetHistory.updateString("PRODUCT_CODE", getAttribute("PRODUCT_CODE").getString());
            resultSetHistory.updateFloat("SQM_RATE", (float)getAttribute("SQM_RATE").getVal());
            resultSetHistory.updateFloat("WT_RATE", (float)getAttribute("WT_RATE").getVal());
            resultSetHistory.updateFloat("CHARGES", (float)getAttribute("CHARGES").getVal());
            resultSetHistory.updateString("CHANGE_DESC", getAttribute("CHANGE_DESC").getString());
            resultSetHistory.updateInt("UPDATED_BY", getAttribute("UPDATED_BY").getInt());
            resultSetHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSetHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            resultSetHistory.updateString("APPROVER_REMARKS", getAttribute("FROM_REMARKS").getString());
            resultSetHistory.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            resultSetHistory.updateBoolean("CHANGED",true);
            resultSetHistory.updateString("CHANGED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSetHistory.insertRow();
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=702; //Felt RATE UPDATE
            ObjFeltProductionApprovalFlow.DocNo=getAttribute("DOC_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName="FELT_RATE_UPDATE";
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
            resultSetHistory.close();
            statementHistory.close();
            return true;
        }catch(Exception e) {
            e.printStackTrace();
            LastError= e.getMessage();
            return false;
        }
    }
    
    //Updates current record
    public boolean Update() {
        Statement statementHistory;
        ResultSet resultSetHistory;
        
        try {
            // ---- Rate Master History Connection ------ //
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_RATE_UPDATE_H WHERE PRODUCT_CODE='1'"); // '1' for restricting all data retrieval
            
            resultSet.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSet.updateFloat("SQM_RATE", (float)getAttribute("SQM_RATE").getVal());
            resultSet.updateFloat("WT_RATE", (float)getAttribute("WT_RATE").getVal());
            resultSet.updateFloat("CHARGES", (float)getAttribute("CHARGES").getVal());
            resultSet.updateString("CHANGE_DESC", getAttribute("CHANGE_DESC").getString());
            resultSet.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
            resultSet.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
            resultSet.updateInt("MODIFIED_BY", getAttribute("MODIFIED_BY").getInt());
            resultSet.updateString("MODIFIED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSet.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            resultSet.updateBoolean("CHANGED",true);
            resultSet.updateString("CHANGED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSet.updateBoolean("CANCELED",false);
            resultSet.updateRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_RATE_UPDATE_H WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
            RevNo++;
            
            resultSetHistory.moveToInsertRow();
            resultSetHistory.updateInt("REVISION_NO",RevNo);
            resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSetHistory.updateString("PRODUCT_CODE", getAttribute("PRODUCT_CODE").getString());
            resultSetHistory.updateFloat("SQM_RATE", (float)getAttribute("SQM_RATE").getVal());
            resultSetHistory.updateFloat("WT_RATE", (float)getAttribute("WT_RATE").getVal());
            resultSetHistory.updateFloat("CHARGES", (float)getAttribute("CHARGES").getVal());
            resultSetHistory.updateString("CHANGE_DESC", getAttribute("CHANGE_DESC").getString());
            resultSetHistory.updateInt("UPDATED_BY", getAttribute("UPDATED_BY").getInt());
            resultSetHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSetHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            resultSetHistory.updateString("APPROVER_REMARKS", getAttribute("FROM_REMARKS").getString());
            resultSetHistory.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            resultSetHistory.updateBoolean("CHANGED",true);
            resultSetHistory.updateString("CHANGED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSetHistory.insertRow();
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=702; //Felt RATE UPDATE
            ObjFeltProductionApprovalFlow.DocNo=getAttribute("DOC_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.FELT_RATE_UPDATE";
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
                data.Execute("UPDATE PRODUCTION.FELT_RATE_UPDATE SET REJECTED=0,CHANGED=1, CHANGED_DATE=CURRENT_TIMESTAMP WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
                //Remove Old Records from FELT DOCUMENT APPROVAL TABLE
                //data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=702 AND DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
                
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
            
            // Update Rate Detail and Rate detail history table
            if(ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {
                ObjFeltProductionApprovalFlow.finalApproved=false;
                UpdateRateDetail();
            }
            
            setData();
            resultSetHistory.close();
            statementHistory.close();
            return true;
        }catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    //Inserts in  Rate Master details and detail history table
    public void UpdateRateDetail() {
        Statement stDetail, stDetailHistory, statementHistory;
        ResultSet rsDetail, rsDetailHistory, rsTmp, resultSetHistory;
        
        try {
            // ---- Rate Detail Connection ------ //
            stDetail=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDetail=stDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_RATE_DETAIL WHERE PRODUCT_CODE='1'"); // '1' for restricting all data retrieval
            
            // ---- Rate Detail History Connection ------ //
            stDetailHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDetailHistory=stDetailHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_RATE_DETAIL_H WHERE PRODUCT_CODE='1'"); // '1' for restricting all data retrieval
            
            // ---- Rate Master History Connection ------ //
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_RATE_MASTER_H WHERE PRODUCT_CODE='1'"); // '1' for restricting all data retrieval
            
            // check that which field is changed
            rsTmp=data.getConn().createStatement().executeQuery("SELECT * FROM PRODUCTION.FELT_RATE_DETAIL WHERE PRODUCT_CODE='"+getAttribute("PRODUCT_CODE").getString()+"' ORDER BY SR_NO DESC LIMIT 1");
            rsTmp.first();
            String detailQuery="";
            String masterQuery="";
            boolean executeQuery=false, rateChanged=false, chargesChanged=false;
            
            if(rsTmp.getRow()>0){
                if((float)getAttribute("SQM_RATE").getVal()==rsTmp.getFloat("SQM_RATE") && (float)getAttribute("WT_RATE").getVal()==rsTmp.getFloat("WT_RATE") && (float)getAttribute("CHARGES").getVal()==rsTmp.getFloat("CHARGES")){
                    
                }else if((float)getAttribute("SQM_RATE").getVal()!=rsTmp.getFloat("SQM_RATE") || (float)getAttribute("WT_RATE").getVal()!=rsTmp.getFloat("WT_RATE")){
                    detailQuery="RATE_TODT=CURDATE()";
                    if((float)getAttribute("SQM_RATE").getVal()!=rsTmp.getFloat("SQM_RATE")) masterQuery="SQM_RATE='"+(float)getAttribute("SQM_RATE").getVal()+"'";
                    if((float)getAttribute("WT_RATE").getVal()!=rsTmp.getFloat("WT_RATE")) masterQuery="WT_RATE'"+(float)getAttribute("WT_RATE").getVal()+"'";
                    if((float)getAttribute("SQM_RATE").getVal()!=rsTmp.getFloat("SQM_RATE") && (float)getAttribute("WT_RATE").getVal()!=rsTmp.getFloat("WT_RATE")) masterQuery="SQM_RATE='"+(float)getAttribute("SQM_RATE").getVal()+"', WT_RATE'"+(float)getAttribute("WT_RATE").getVal()+"'";
                    executeQuery=true;
                    rateChanged=true;
                }
                
                if((float)getAttribute("CHARGES").getVal()!=rsTmp.getFloat("CHARGES")){
                    if(executeQuery){
                        detailQuery+=", CHARGES_TODT=CURDATE()";
                        masterQuery+=", CHARGES='"+(float)getAttribute("CHARGES").getVal()+"'";
                    }else {
                        detailQuery="CHARGES_TODT=CURDATE()";
                        masterQuery="CHARGES='"+(float)getAttribute("CHARGES").getVal()+"'";
                        executeQuery=true;
                    }
                    chargesChanged=true;
                }
            }
            
            //Get the Maximum Serial No.
            int srNo=data.getIntValueFromDB("SELECT MAX(SR_NO) FROM PRODUCTION.FELT_RATE_DETAIL WHERE PRODUCT_CODE='"+getAttribute("PRODUCT_CODE").getString()+"'");
            System.out.println(" UPDATE PRODUCTION.FELT_RATE_DETAIL SET "+detailQuery+", MODIFIED_BY="+EITLERPGLOBAL.gNewUserID+", MODIFIED_DATE=CURRENT_TIMESTAMP, CHANGED=1, CHANGED_DATE=CURRENT_TIMESTAMP WHERE PRODUCT_CODE='"+getAttribute("PRODUCT_CODE").getString()+"' AND SR_NO="+srNo +"\n"+"UPDATE PRODUCTION.FELT_RATE_MASTER SET "+masterQuery+", MODIFIED_BY="+EITLERPGLOBAL.gNewUserID+", MODIFIED_DATE=CURRENT_TIMESTAMP, CHANGED=1, CHANGED_DATE=CURRENT_TIMESTAMP WHERE PRODUCT_CODE='"+getAttribute("PRODUCT_CODE").getString()+"'");
            // update rate detail table to confirm change in rate and charges
            if(executeQuery)data.Execute("UPDATE PRODUCTION.FELT_RATE_DETAIL SET "+detailQuery+", MODIFIED_BY="+EITLERPGLOBAL.gNewUserID+", MODIFIED_DATE=CURRENT_TIMESTAMP, CHANGED=1, CHANGED_DATE=CURRENT_TIMESTAMP WHERE PRODUCT_CODE='"+getAttribute("PRODUCT_CODE").getString()+"' AND SR_NO="+srNo);
            
            // update rate master to confirm change in rate and charges
            if(executeQuery)data.Execute("UPDATE PRODUCTION.FELT_RATE_MASTER SET "+masterQuery+", MODIFIED_BY="+EITLERPGLOBAL.gNewUserID+", MODIFIED_DATE=CURRENT_TIMESTAMP, CHANGED=1, CHANGED_DATE=CURRENT_TIMESTAMP WHERE PRODUCT_CODE='"+getAttribute("PRODUCT_CODE").getString()+"'");
            
            srNo++;
            
            rsDetail.moveToInsertRow();
            rsDetail.updateInt("SR_NO",srNo);
            rsDetail.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            rsDetail.updateString("PRODUCT_CODE", getAttribute("PRODUCT_CODE").getString());
            rsDetail.updateFloat("SQM_RATE", (float)getAttribute("SQM_RATE").getVal());
            rsDetail.updateFloat("WT_RATE", (float)getAttribute("WT_RATE").getVal());
            
            if(rateChanged){
                rsDetail.updateString("RATE_FRMDT", EITLERPGLOBAL.getCurrentDateDB());
                rateChanged=false;
            }else rsDetail.updateString("RATE_FRMDT", rsTmp.getString("RATE_FRMDT"));
            
            rsDetail.updateString("RATE_TODT", "");
            rsDetail.updateFloat("CHARGES", (float)getAttribute("CHARGES").getVal());
            
            if(chargesChanged){
                rsDetail.updateString("CHARGES_FRMDT", EITLERPGLOBAL.getCurrentDateDB());
                chargesChanged=false;
            }else rsDetail.updateString("CHARGES_FRMDT",rsTmp.getString("CHARGES_FRMDT"));
            
            rsDetail.updateString("CHARGES_TODT", "");
            rsDetail.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
            rsDetail.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
            rsDetail.updateInt("MODIFIED_BY", getAttribute("MODIFIED_BY").getInt());
            rsDetail.updateString("MODIFIED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            rsDetail.updateBoolean("CHANGED",true);
            rsDetail.updateString("CHANGED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            rsDetail.insertRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_RATE_DETAIL_H WHERE PRODUCT_CODE='"+getAttribute("PRODUCT_CODE").getString()+"'");
            RevNo++;
            
            rsDetailHistory.moveToInsertRow();
            rsDetailHistory.updateInt("REVISION_NO",RevNo);
            rsDetailHistory.updateInt("SR_NO",srNo);
            rsDetailHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            rsDetailHistory.updateString("PRODUCT_CODE", getAttribute("PRODUCT_CODE").getString());
            rsDetailHistory.updateFloat("SQM_RATE", (float)getAttribute("SQM_RATE").getVal());
            rsDetailHistory.updateFloat("WT_RATE", (float)getAttribute("WT_RATE").getVal());
            rsDetailHistory.updateString("RATE_FRMDT", EITLERPGLOBAL.getCurrentDateDB());
            rsDetailHistory.updateString("RATE_TODT", "");
            rsDetailHistory.updateFloat("CHARGES", (float)getAttribute("CHARGES").getVal());
            rsDetailHistory.updateString("CHARGES_FRMDT", EITLERPGLOBAL.getCurrentDateDB());
            rsDetailHistory.updateString("CHARGES_TODT", "");
            rsDetailHistory.updateInt("UPDATED_BY", getAttribute("UPDATED_BY").getInt());
            rsDetailHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            rsDetailHistory.updateBoolean("CHANGED",true);
            rsDetailHistory.updateString("CHANGED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            rsDetailHistory.insertRow();
            
            //========= Inserting Into Rate Master History =================//
            //Get the Maximum Revision No.
            int revNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_RATE_MASTER_H WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
            revNo++;
            
            resultSetHistory.moveToInsertRow();
            resultSetHistory.updateInt("REVISION_NO",revNo);
            resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSetHistory.updateString("PRODUCT_CODE", getAttribute("PRODUCT_CODE").getString());
            resultSetHistory.updateFloat("SQM_RATE", (float)getAttribute("SQM_RATE").getVal());
            resultSetHistory.updateFloat("WT_RATE", (float)getAttribute("WT_RATE").getVal());
            resultSetHistory.updateFloat("CHARGES", (float)getAttribute("CHARGES").getVal());
            resultSetHistory.updateInt("UPDATED_BY", getAttribute("UPDATED_BY").getInt());
            resultSetHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSetHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            resultSetHistory.updateBoolean("CHANGED",true);
            resultSetHistory.updateString("CHANGED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSetHistory.insertRow();
            
            
            rsDetail.close();
            rsDetailHistory.close();
        }catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
        }
    }
    
    /**
     * This routine checks and returns whether the item is editable or not.
     * Criteria is Approved item cannot be changed and if not approved then user id is
     * checked whether doucment is waiting for his approval.
     */
    public boolean IsEditable(String itemCode, int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        try {
            if(HistoryView) {
                return false;
            }
            tmpStmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_RATE_UPDATE WHERE PRODUCT_CODE='"+ itemCode +"' AND DOC_NO='"+ getAttribute("DOC_NO").getString() +"' AND APPROVED=1";
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0){  //Item is Approved
                //Discard the request
                return false;
            }else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=702 AND USER_ID="+userID+" AND DOC_NO='"+ getAttribute("DOC_NO").getString()+"' AND STATUS='W'";
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
            String strSql = "SELECT * FROM PRODUCTION.FELT_RATE_UPDATE WHERE " + stringFindQuery;
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
    
    public Object getObject(String lDocNo) {
        String strCondition = "DOC_NO='" + lDocNo + "'" ;
        clsFeltRateUpdate ObjFeltRateMaster = new clsFeltRateUpdate();
        ObjFeltRateMaster.Filter(strCondition);
        return ObjFeltRateMaster;
    }
    
    public static Object getObjectEx(String qualityId) {
        String strCondition = "PRODUCT_CODE='" + qualityId + "'" ;
        clsFeltRateUpdate ObjFeltRateMaster = new clsFeltRateUpdate();
        ObjFeltRateMaster.LoadData();
        ObjFeltRateMaster.Filter(strCondition);
        return ObjFeltRateMaster;
    }
        
    public boolean setData() {
        long Counter=0;
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
            setAttribute("PRODUCT_CODE",resultSet.getString("PRODUCT_CODE"));
            setAttribute("SQM_RATE",resultSet.getFloat("SQM_RATE"));
            setAttribute("WT_RATE",resultSet.getFloat("WT_RATE"));
            setAttribute("CHARGES",resultSet.getFloat("CHARGES"));
            setAttribute("CHANGE_DESC",resultSet.getString("CHANGE_DESC"));
            setAttribute("HIERARCHY_ID",resultSet.getInt("HIERARCHY_ID"));
            
            return true;
        }catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public static HashMap getPendingApprovals(int pUserID,int pOrder) {
        String strSQL="";
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            
            if(pOrder==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT PRODUCTION.FELT_RATE_UPDATE.DOC_NO,PRODUCTION.FELT_RATE_UPDATE.PRODUCT_CODE,RECEIVED_DATE FROM PRODUCTION.FELT_RATE_UPDATE,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_RATE_UPDATE.DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+pUserID+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=702 AND CANCELED=0 ORDER BY PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT PRODUCTION.FELT_RATE_UPDATE.DOC_NO,PRODUCTION.FELT_RATE_UPDATE.PRODUCT_CODE,RECEIVED_DATE FROM PRODUCTION.FELT_RATE_UPDATE,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_RATE_UPDATE.DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+pUserID+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=702 AND CANCELED=0 ORDER BY PRODUCTION.FELT_RATE_UPDATE.PRODUCT_CODE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT PRODUCTION.FELT_RATE_UPDATE.DOC_NO,PRODUCTION.FELT_RATE_UPDATE.PRODUCT_CODE,RECEIVED_DATE FROM PRODUCTION.FELT_RATE_UPDATE,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_RATE_UPDATE.DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+pUserID+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=702 AND CANCELED=0 ORDER BY PRODUCTION.FELT_RATE_UPDATE.DOC_NO";
            }
            
            ResultSet rsTmp=data.getConn().createStatement().executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp.next()) {
                
                Counter=Counter+1;
                clsFeltRateUpdate ObjDoc=new clsFeltRateUpdate();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                ObjDoc.setAttribute("PRODUCT_CODE",rsTmp.getString("PRODUCT_CODE"));
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
    
    public boolean ShowHistory(String docNo) {
        Ready=false;
        try {
            connection=data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.FELT_RATE_UPDATE_H WHERE DOC_NO='"+docNo+"'");
            Ready=true;
            HistoryView=true;
            MoveFirst();
            return true;
        }catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public static HashMap getHistoryList(String itemCode, String docNo) {
        HashMap hmHistoryList=new HashMap();
        Statement stTmp;
        ResultSet rsTmp;
        try {
            stTmp=data.getConn().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp=stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_RATE_UPDATE_H WHERE PRODUCT_CODE='"+itemCode+"' AND DOC_NO='"+docNo+"'");
            
            while(rsTmp.next()) {
                clsFeltRateUpdate ObjFeltRateMaster=new clsFeltRateUpdate();
                
                ObjFeltRateMaster.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjFeltRateMaster.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjFeltRateMaster.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjFeltRateMaster.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjFeltRateMaster.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                hmHistoryList.put(Integer.toString(hmHistoryList.size()+1),ObjFeltRateMaster);
            }
            rsTmp.close();
            stTmp.close();
            return hmHistoryList;
        }catch(Exception e) {
            e.printStackTrace();
            return hmHistoryList;
        }
    }
    
    public String[] getQualityDetail(String itemCode){
        String itemDetail[]= new String[3];
        ResultSet rsTmp;
        try {
            rsTmp =data.getConn().createStatement().executeQuery("SELECT SQM_RATE,WT_RATE,CHARGES FROM PRODUCTION.FELT_RATE_MASTER WHERE PRODUCT_CODE='"+itemCode+"'");
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                itemDetail[0]=rsTmp.getString("SQM_RATE");
                itemDetail[1]=rsTmp.getString("WT_RATE");
                itemDetail[2]=rsTmp.getString("CHARGES");
            }
            rsTmp.close();
            return itemDetail;
        }catch(Exception e) {
            e.printStackTrace();
            return itemDetail;
        }
    }
    
    // generate document no for rate update
    public String getDocNo(String itemCode){
        return "FRU"+itemCode+(data.getIntValueFromDB("SELECT MAX(SUBSTRING(DOC_NO,10)) FROM PRODUCTION.FELT_RATE_UPDATE WHERE PRODUCT_CODE='"+itemCode+"'")+1);
    }
    
    public boolean checkRate(String itemCode, float sqmRate, float wtRate, float charges){
        Statement stTmp;
        ResultSet rsTmp;
        boolean ret=false;
        try {
            stTmp=data.getConn().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp=stTmp.executeQuery("SELECT SQM_RATE, WT_RATE, CHARGES FROM PRODUCTION.FELT_RATE_DETAIL WHERE PRODUCT_CODE='"+itemCode+"' ORDER BY SR_NO DESC LIMIT 1");
            rsTmp.first();
            
            if(rsTmp.getRow()>0){
                if(sqmRate==rsTmp.getFloat("SQM_RATE") && wtRate==rsTmp.getFloat("WT_RATE") && charges==rsTmp.getFloat("CHARGES")) ret = true;
            }
            
            rsTmp.close();
            stTmp.close();
            return ret;
        }catch(Exception e) {
            e.printStackTrace();
            return ret;
        }
    }
    
    public static String getDocStatus(String docNo) {
        ResultSet rsTmp;
        String strMessage="";
        
        try {
            //First check that Document exist
            rsTmp=data.getResult("SELECT DOC_NO,PRODUCT_CODE,APPROVED,CANCELLED FROM PRODUCTION.FELT_RATE_UPDATE WHERE DOC_NO='"+docNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                if(rsTmp.getBoolean("APPROVED")) {
                    if(rsTmp.getBoolean("CANCELLED")) {
                        strMessage="Document is cancelled";
                    }else {
                        strMessage="";
                    }
                }else {
                    strMessage="Document is created but is under approval";
                }
                
            }else {
                strMessage="Document does not exist";
            }
            rsTmp.close();
        }catch(Exception e) {
        }
        
        return strMessage;
    }
    
    
    public static boolean CanCancel(int pCompanyID,String docNo) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            rsTmp=data.getResult("SELECT DOC_NO,PRODUCT_CODE FROM PRODUCTION.FELT_RATE_UPDATE WHERE DOC_NO='"+docNo+"' AND CANCELLED=0  ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                canCancel=true;
            }
            
            rsTmp.close();
        }catch(Exception e) {
            
        }
        return canCancel;
    }
    
    public static boolean CancelDoc(int pCompanyID,String docNo) {
        ResultSet rsTmp=null;
        boolean Cancelled=false;
        try {
            if(CanCancel(pCompanyID,docNo)) {
                boolean ApprovedDoc=false;
                rsTmp=data.getResult("SELECT APPROVED FROM PRODUCTION.FELT_RATE_UPDATE WHERE DOC_NO='"+docNo+"' ");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                if(ApprovedDoc) {
                    
                }else {
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='"+docNo+"' AND MODULE_ID="+(702));
                }
                
                //Now Update the header with cancel falg to true
                data.Execute("UPDATE PRODUCTION.FELT_RATE_UPDATE SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURRENT_TIMESTAMP WHERE DOC_NO='"+docNo+"' ");
                
                Cancelled=true;
            }
            
            rsTmp.close();
        }catch(Exception e) {
            
        }
        return Cancelled;
    }
    
}
