/*
 * clsFeltReopenBale.java
 *
 * Created on July 19, 2013, 5:31 PM
 */

package EITLERP.FeltSales.ReopenBale12;

import EITLERP.FeltSales.FeltPacking.*;
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
import javax.swing.JOptionPane;


public class clsFeltReopenBale {
    
    public String LastError="";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private int FFNo=0;
    public HashMap props;
    public boolean Ready = false;
    public HashMap hmFeltReopenBaleDetails;
    
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
    
    /** Creates new DataFeltPacking */
    public clsFeltReopenBale() {
        LastError = "";
        props=new HashMap();
        props.put("DOC_NO", new Variant(""));
        props.put("BALE_NO", new Variant(""));
        props.put("BALE_DATE", new Variant(""));
        props.put("PARTY_CODE", new Variant(""));
        props.put("PARTY_NAME", new Variant(""));
        props.put("STATION", new Variant(""));
        props.put("TRANSPORT_MODE", new Variant(""));
        props.put("MODE_REOPEN_BALE", new Variant(""));
        props.put("BOX_SIZE", new Variant(""));
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
        
        hmFeltReopenBaleDetails=new HashMap();
    }
    
    public boolean LoadData() {
        Ready=false;
        try {
            connection = data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.FELT_REOPEN_BALE_HEADER ORDER BY DOC_NO");
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
            // Packing detail connection
            statementDetail = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetDetail = statementDetail.executeQuery("SELECT * FROM  PRODUCTION.FELT_REOPEN_BALE_DETAIL WHERE DOC_NO=''");
            
            // Packing detail history connection
            statementDetailHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetDetailHistory = statementDetailHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_REOPEN_BALE_DETAIL_H WHERE DOC_NO=''");
            
            // Packing data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_REOPEN_BALE_HEADER_H WHERE DOC_NO=''");
            
            
            setAttribute("DOC_NO", EITLERP.clsFirstFree.getNextFreeNo(2, 740, 223, true));
            //--------- Generate Bale no. ---------------------
            //setAttribute("BALE_NO",EITLERP.clsFirstFree.getNextFreeNo(2, 740, getAttribute("FFNO").getInt(), true));
            //-------------------------------------------------
            
            resultSet.moveToInsertRow();
            resultSet.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSet.updateString("BALE_NO", getAttribute("BALE_NO").getString());
            resultSet.updateString("BALE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("BALE_DATE").getString()));
            resultSet.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            resultSet.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
            resultSet.updateString("STATION", getAttribute("STATION").getString());
            resultSet.updateString("TRANSPORT_MODE", getAttribute("TRANSPORT_MODE").getString());
            resultSet.updateString("BOX_SIZE", getAttribute("BOX_SIZE").getString());
            resultSet.updateString("MODE_PACKING", getAttribute("MODE_PACKING").getString());
            resultSet.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
            resultSet.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateInt("MODIFIED_BY",0);
            resultSet.updateString("MODIFIED_DATE","0000-00-00");
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
            resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSetHistory.updateString("BALE_NO", getAttribute("BALE_NO").getString());
            resultSetHistory.updateString("BALE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("BALE_DATE").getString()));
            resultSetHistory.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            resultSetHistory.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
            resultSetHistory.updateString("STATION", getAttribute("STATION").getString());
            resultSetHistory.updateString("TRANSPORT_MODE", getAttribute("TRANSPORT_MODE").getString());
            resultSetHistory.updateString("BOX_SIZE", getAttribute("BOX_SIZE").getString());
            resultSetHistory.updateString("MODE_PACKING", getAttribute("MODE_PACKING").getString());
            resultSetHistory.updateInt("UPDATED_BY", getAttribute("UPDATED_BY").getInt());
            resultSetHistory.updateString("ENTRY_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
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
            
            for(int i=1;i<=hmFeltReopenBaleDetails.size();i++) {
                clsFeltReopenBaleDetails ObjFeltReopenBaleDetails=(clsFeltReopenBaleDetails) hmFeltReopenBaleDetails.get(Integer.toString(i));
                
                //Insert records into Packing detail table
                resultSetDetail.moveToInsertRow();
                resultSetDetail.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetDetail.updateString("BALE_NO", getAttribute("BALE_NO").getString());
                resultSetDetail.updateString("BALE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("BALE_DATE").getString()));
                resultSetDetail.updateString("PIECE_NO",ObjFeltReopenBaleDetails.getAttribute("PIECE_NO").getString());
                resultSetDetail.updateFloat("LENGTH",(float)ObjFeltReopenBaleDetails.getAttribute("LENGTH").getVal());
                resultSetDetail.updateFloat("WIDTH",(float)ObjFeltReopenBaleDetails.getAttribute("WIDTH").getVal());
                resultSetDetail.updateFloat("GSM",(float)ObjFeltReopenBaleDetails.getAttribute("GSM").getVal());
                resultSetDetail.updateFloat("SQM",(float)ObjFeltReopenBaleDetails.getAttribute("SQM").getVal());
                resultSetDetail.updateFloat("SYN_PER",(float)ObjFeltReopenBaleDetails.getAttribute("SYN_PER").getVal());
                resultSetDetail.updateString("STYLE", ObjFeltReopenBaleDetails.getAttribute("STYLE").getString());
                resultSetDetail.updateString("MCN_POSITION_DESC",ObjFeltReopenBaleDetails.getAttribute("MCN_POSITION_DESC").getString());
                resultSetDetail.updateString("MACHINE_NO",ObjFeltReopenBaleDetails.getAttribute("MACHINE_NO").getString());
                resultSetDetail.updateString("ORDER_NO",ObjFeltReopenBaleDetails.getAttribute("ORDER_NO").getString());
                resultSetDetail.updateString("ORDER_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltReopenBaleDetails.getAttribute("ORDER_DATE").getString()));
                resultSetDetail.updateString("PRODUCT_CODE",ObjFeltReopenBaleDetails.getAttribute("PRODUCT_CODE").getString());
                resultSetDetail.updateInt("CREATED_BY",getAttribute("CREATED_BY").getInt());
                resultSetDetail.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetail.updateInt("MODIFIED_BY",0);
                resultSetDetail.updateString("MODIFIED_DATE","0000-00-00");
                resultSetDetail.updateInt("CANCELED",0);
                resultSetDetail.updateInt("CHANGED",1);
                resultSetDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetail.insertRow();
                
                //Insert records into Packing detail history table
                resultSetDetailHistory.moveToInsertRow();
                resultSetDetailHistory.updateInt("REVISION_NO",1);
                resultSetDetailHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetDetailHistory.updateString("BALE_NO", getAttribute("BALE_NO").getString());
                resultSetDetailHistory.updateString("BALE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("BALE_DATE").getString()));
                resultSetDetailHistory.updateString("PIECE_NO",ObjFeltReopenBaleDetails.getAttribute("PIECE_NO").getString());
                resultSetDetailHistory.updateFloat("LENGTH",(float)ObjFeltReopenBaleDetails.getAttribute("LENGTH").getVal());
                resultSetDetailHistory.updateFloat("WIDTH",(float)ObjFeltReopenBaleDetails.getAttribute("WIDTH").getVal());
                resultSetDetailHistory.updateFloat("WEIGHT",(float)ObjFeltReopenBaleDetails.getAttribute("WEIGHT").getVal());
                resultSetDetailHistory.updateFloat("GSM",(float)ObjFeltReopenBaleDetails.getAttribute("GSM").getVal());
                resultSetDetailHistory.updateFloat("SQM",(float)ObjFeltReopenBaleDetails.getAttribute("SQM").getVal());
                resultSetDetailHistory.updateInt("SYN_PER",ObjFeltReopenBaleDetails.getAttribute("SYN_PER").getInt());
                resultSetDetailHistory.updateString("STYLE", ObjFeltReopenBaleDetails.getAttribute("STYLE").getString());
                resultSetDetailHistory.updateString("MCN_POSITION_DESC",ObjFeltReopenBaleDetails.getAttribute("MCN_POSITION_DESC").getString());
                resultSetDetailHistory.updateString("MACHINE_NO",ObjFeltReopenBaleDetails.getAttribute("MACHINE_NO").getString());
                resultSetDetailHistory.updateString("ORDER_NO",ObjFeltReopenBaleDetails.getAttribute("ORDER_NO").getString());
                resultSetDetailHistory.updateString("ORDER_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltReopenBaleDetails.getAttribute("ORDER_DATE").getString()));
                resultSetDetailHistory.updateString("PRODUCT_CODE",ObjFeltReopenBaleDetails.getAttribute("PRODUCT_CODE").getString());
                resultSetDetailHistory.updateInt("UPDATED_BY",getAttribute("UPDATED_BY").getInt());
                resultSetDetailHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetailHistory.updateInt("CHANGED",1);
                resultSetDetailHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetailHistory.insertRow();
            }
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=740; //Felt Packing
            ObjFeltProductionApprovalFlow.DocNo=getDocumentNo(getAttribute("DOC_NO").getString());
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName="FELT_REOPEN_BALE_HEADER";
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
            // Packing detail connection
            statementDetail = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetDetail = statementDetail.executeQuery("SELECT * FROM  PRODUCTION.FELT_REOPEN_BALE_DETAIL WHERE DOC_NO=''");
            
            // Packing detail history connection
            statementDetailHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetDetailHistory = statementDetailHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_REOPEN_BALE_DETAIL_H WHERE DOC_NO=''");
            
            // Packing data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_REOPEN_BALE_HEADER_H WHERE DOC_NO=''");
            
            resultSet.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSet.updateString("BALE_NO", getAttribute("BALE_NO").getString());
            resultSet.updateString("BALE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("BALE_DATE").getString()));
            resultSet.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            resultSet.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
            resultSet.updateString("STATION", getAttribute("STATION").getString());
            resultSet.updateString("TRANSPORT_MODE", getAttribute("TRANSPORT_MODE").getString());
            resultSet.updateString("BOX_SIZE", getAttribute("BOX_SIZE").getString());
            resultSet.updateString("MODE_PACKING", getAttribute("MODE_PACKING").getString());
            resultSet.updateInt("MODIFIED_BY",getAttribute("MODIFIED_BY").getInt());
            resultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            resultSet.updateInt("CHANGED",1);
            resultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int revisionNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_REOPEN_BALE_HEADER_H WHERE BALE_DATE='"+EITLERPGLOBAL.formatDateDB(getAttribute("BALE_DATE").getString())+"' AND DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
            revisionNo++;
            
            resultSetHistory.moveToInsertRow();
            resultSetHistory.updateInt("REVISION_NO",revisionNo);
            resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSetHistory.updateString("BALE_NO", getAttribute("BALE_NO").getString());
            resultSetHistory.updateString("BALE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("BALE_DATE").getString()));
            resultSetHistory.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            resultSetHistory.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
            resultSetHistory.updateString("STATION", getAttribute("STATION").getString());
            resultSetHistory.updateString("TRANSPORT_MODE", getAttribute("TRANSPORT_MODE").getString());
            resultSetHistory.updateString("BOX_SIZE", getAttribute("BOX_SIZE").getString());
            resultSetHistory.updateString("MODE_PACKING", getAttribute("MODE_PACKING").getString());
            resultSetHistory.updateInt("UPDATED_BY",getAttribute("UPDATED_BY").getInt());
            resultSetHistory.updateString("ENTRY_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSetHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            resultSetHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            resultSetHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            resultSetHistory.updateInt("CHANGED",1);
            resultSetHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            
            ResultSet rsTmp=data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");
            
            resultSetHistory.updateString("FROM_IP",""+str_split[1]);
            
            resultSetHistory.insertRow();
            
            
            //delete records from Packing detail table before insert
            data.Execute("DELETE FROM PRODUCTION.FELT_REOPEN_BALE_DETAIL WHERE BALE_DATE='"+ EITLERPGLOBAL.formatDateDB(getAttribute("BALE_DATE").getString())+"' AND DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
            
            //Now insert records into Packing detail tables
            for(int i=1;i<=hmFeltReopenBaleDetails.size();i++) {
                clsFeltReopenBaleDetails ObjFeltReopenBaleDetails=(clsFeltReopenBaleDetails) hmFeltReopenBaleDetails.get(Integer.toString(i));
                
                //Insert records into Packing detail table
                resultSetDetail.moveToInsertRow();
                resultSetDetail.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetDetail.updateString("BALE_NO", getAttribute("BALE_NO").getString());
                resultSetDetail.updateString("BALE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("BALE_DATE").getString()));
                resultSetDetail.updateString("PIECE_NO",ObjFeltReopenBaleDetails.getAttribute("PIECE_NO").getString());
                resultSetDetail.updateFloat("LENGTH",(float)ObjFeltReopenBaleDetails.getAttribute("LENGTH").getVal());
                resultSetDetail.updateFloat("WIDTH",(float)ObjFeltReopenBaleDetails.getAttribute("WIDTH").getVal());
                resultSetDetail.updateFloat("GSM", (float)ObjFeltReopenBaleDetails.getAttribute("GSM").getVal());
                resultSetDetail.updateFloat("SQM", (float)ObjFeltReopenBaleDetails.getAttribute("SQM").getVal());
                resultSetDetail.updateFloat("SYN_PER",(float)ObjFeltReopenBaleDetails.getAttribute("SYN_PER").getVal());
                resultSetDetail.updateString("STYLE", ObjFeltReopenBaleDetails.getAttribute("STYLE").getString());
                resultSetDetail.updateString("MCN_POSITION_DESC",ObjFeltReopenBaleDetails.getAttribute("MCN_POSITION_DESC").getString());
                resultSetDetail.updateString("MACHINE_NO",ObjFeltReopenBaleDetails.getAttribute("MACHINE_NO").getString());
                resultSetDetail.updateString("ORDER_NO",ObjFeltReopenBaleDetails.getAttribute("ORDER_NO").getString());
                resultSetDetail.updateString("ORDER_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltReopenBaleDetails.getAttribute("ORDER_DATE").getString()));
                resultSetDetail.updateString("PRODUCT_CODE",ObjFeltReopenBaleDetails.getAttribute("PRODUCT_CODE").getString());
                resultSetDetail.updateInt("MODIFIED_BY",getAttribute("MODIFIED_BY").getInt());
                resultSetDetail.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetail.updateInt("CANCELED",0);
                resultSetDetail.updateInt("CHANGED",1);
                resultSetDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetail.insertRow();
                
                //Insert records into Packing detail history table
                resultSetDetailHistory.moveToInsertRow();
                resultSetDetailHistory.updateInt("REVISION_NO",revisionNo);
                resultSetDetailHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetDetailHistory.updateString("BALE_NO", getAttribute("BALE_NO").getString());
                resultSetDetailHistory.updateString("BALE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("BALE_DATE").getString()));
                resultSetDetailHistory.updateString("PIECE_NO",ObjFeltReopenBaleDetails.getAttribute("PIECE_NO").getString());
                resultSetDetailHistory.updateFloat("LENGTH",(float)ObjFeltReopenBaleDetails.getAttribute("LENGTH").getVal());
                resultSetDetailHistory.updateFloat("WIDTH",(float)ObjFeltReopenBaleDetails.getAttribute("WIDTH").getVal());
                resultSetDetailHistory.updateFloat("WEIGHT",(float)ObjFeltReopenBaleDetails.getAttribute("WEIGHT").getVal());
                resultSetDetailHistory.updateFloat("GSM",(float)ObjFeltReopenBaleDetails.getAttribute("GSM").getVal());
                resultSetDetailHistory.updateFloat("SQM",(float)ObjFeltReopenBaleDetails.getAttribute("SQM").getVal());
                resultSetDetailHistory.updateFloat("SYN_PER",(float)ObjFeltReopenBaleDetails.getAttribute("SYN_PER").getVal());
                resultSetDetailHistory.updateString("STYLE", ObjFeltReopenBaleDetails.getAttribute("STYLE").getString());
                resultSetDetailHistory.updateString("MCN_POSITION_DESC",ObjFeltReopenBaleDetails.getAttribute("MCN_POSITION_DESC").getString());
                resultSetDetailHistory.updateString("MACHINE_NO",ObjFeltReopenBaleDetails.getAttribute("MACHINE_NO").getString());
                resultSetDetailHistory.updateString("ORDER_NO",ObjFeltReopenBaleDetails.getAttribute("ORDER_NO").getString());
                resultSetDetailHistory.updateString("ORDER_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltReopenBaleDetails.getAttribute("ORDER_DATE").getString()));
                resultSetDetailHistory.updateString("PRODUCT_CODE",ObjFeltReopenBaleDetails.getAttribute("PRODUCT_CODE").getString());
                resultSetDetailHistory.updateInt("UPDATED_BY",getAttribute("UPDATED_BY").getInt());
                resultSetDetailHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetailHistory.updateInt("CHANGED",1);
                resultSetDetailHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetailHistory.insertRow();
            }
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=740; //Felt Packing
            ObjFeltProductionApprovalFlow.DocNo=getDocumentNo(getAttribute("DOC_NO").getString());
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.FELT_REOPEN_BALE_HEADER";
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
                data.Execute("UPDATE PRODUCTION.FELT_REOPEN_BALE_HEADER SET REJECTED=0,CHANGED=1 WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"' AND BALE_DATE='"+getAttribute("BALE_DATE").getString()+"' AND DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
                //Remove Old Records from FELT DOCUMENT APPROVAL TABLE
                //data.Execute("DELETE FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=740 AND DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
                
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
            
            if(ObjFeltProductionApprovalFlow.Status.equals("F")) {
                data.Execute("UPDATE PRODUCTION.FELT_PKG_SLIP_HEADER SET BALE_REOPEN_FLG=1 WHERE PKG_BALE_NO='"+getAttribute("BALE_NO").getString()+"' AND PKG_BALE_DATE='"+EITLERPGLOBAL.formatDateDB(getAttribute("BALE_DATE").getString())+"' ");
                data.Execute("UPDATE PRODUCTION.FELT_PKG_SLIP_DETAIL SET BALE_REOPEN_FLG=1 WHERE PKG_BALE_NO='"+getAttribute("BALE_NO").getString()+"' AND PKG_BALE_DATE='"+EITLERPGLOBAL.formatDateDB(getAttribute("BALE_DATE").getString())+"' ");
                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PIECE_STAGE='IN STOCK',PR_PKG_DP_NO='',PR_PKG_DP_DATE='0000-00-00',PR_BALE_NO='',PR_PACKED_DATE='0000-00-00' WHERE PR_BALE_NO='"+getAttribute("BALE_NO").getString()+"' AND PR_PACKED_DATE='"+EITLERPGLOBAL.formatDateDB(getAttribute("BALE_DATE").getString())+"'");
            }

            //--------- Approval Flow Update complete -----------
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
    
    /**
     * This routine checks whether the item is deletable or not.
     * Criteria is Approved item cannot be delete,
     * and if not approved then user id is checked whether doucment
     * is created by the user. Only creator can delete the document.
     * After checking it deletes the record of selected production date and document no.
     */
    public boolean CanDelete(String baleNo,String baleDate,int userID) {
        if(HistoryView) {
            return false;
        }
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            tmpStmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_REOPEN_BALE_HEADER WHERE DOC_NO='"+baleNo +"' AND BALE_DATE='"+EITLERPGLOBAL.formatDateDB(baleDate) +"' AND APPROVED=1";
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if(rsTmp.getInt("COUNT")>0){  //Packing is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            }else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=740 AND USER_ID="+userID+" AND DOC_NO='"+ getAttribute("DOC_NO").getString()+"' AND STATUS='W'";
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getInt("COUNT")>0) {
                    strSQL = "DELETE FROM PRODUCTION.FELT_REOPEN_BALE_HEADER WHERE DOC_NO='"+baleNo+"' AND BALE_DATE='"+EITLERPGLOBAL.formatDateDB(baleDate)+"'";
                    tmpStmt.executeUpdate(strSQL);
                    strSQL = "DELETE FROM PRODUCTION.FELT_REOPEN_BALE_DETAIL WHERE DOC_NO='"+baleNo+"' AND BALE_DATE='"+EITLERPGLOBAL.formatDateDB(baleDate)+"'";
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
    
    /**
     * This routine checks and returns whether the item is editable or not.
     * Criteria is Approved item cannot be changed and if not approved then user id is
     * checked whether doucment is waiting for his approval.
     */
    public boolean IsEditable(String baleNo, String baleDate, int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        try {
            if(HistoryView) {
                return false;
            }
            tmpStmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_REOPEN_BALE_HEADER WHERE DOC_NO='"+ baleNo +"' AND BALE_DATE='"+ baleDate +"' AND APPROVED=1";
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0){  //Item is Approved
                //Discard the request
                return false;
            }else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=740 AND USER_ID="+userID+" AND DOC_NO='"+baleNo+"' AND STATUS='W'";
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
            if(stringFindQuery.substring(0,9).equalsIgnoreCase(" PIECE_NO")){
                
                System.out.println("QQQ - SELECT GROUP_CONCAT(A.DOC_NO) FROM PRODUCTION.FELT_REOPEN_BALE_HEADER A,PRODUCTION.FELT_REOPEN_BALE_DETAIL B WHERE A.BALE_NO=B.BALE_NO AND A.BALE_DATE=B.BALE_DATE AND B.PIECE_NO='"+stringFindQuery.substring(12));
                String docNo=data.getStringValueFromDB("SELECT GROUP_CONCAT(A.DOC_NO) FROM PRODUCTION.FELT_REOPEN_BALE_HEADER A,PRODUCTION.FELT_REOPEN_BALE_DETAIL B WHERE A.BALE_NO=B.BALE_NO AND A.BALE_DATE=B.BALE_DATE AND B.PIECE_NO='"+stringFindQuery.substring(12));
                stringFindQuery=" ";
                String[] Pieces = docNo.split(",");
                for (int i = 0; i < Pieces.length; i++) {
                    if (i == 0) {
                        stringFindQuery += " (DOC_NO = '" + Pieces[i] + "' ";
                    } else {
                        stringFindQuery += " OR DOC_NO = '" + Pieces[i] + "' ";
                    }
                }
                stringFindQuery += ") ";
                
                System.out.println(" stringFindQuery" +stringFindQuery);
            }
            String strSql = "SELECT * FROM PRODUCTION.FELT_REOPEN_BALE_HEADER WHERE " + stringFindQuery;
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
            setAttribute("BALE_NO",resultSet.getString("BALE_NO"));
            setAttribute("BALE_DATE",resultSet.getString("BALE_DATE"));
            setAttribute("PARTY_CODE",resultSet.getString("PARTY_CODE"));
            setAttribute("PARTY_NAME",resultSet.getString("PARTY_NAME"));
            setAttribute("STATION",resultSet.getString("STATION"));
            setAttribute("TRANSPORT_MODE",resultSet.getString("TRANSPORT_MODE"));
            setAttribute("BOX_SIZE",resultSet.getString("BOX_SIZE"));
            setAttribute("MODE_PACKING",resultSet.getString("MODE_PACKING"));
            setAttribute("HIERARCHY_ID",resultSet.getInt("HIERARCHY_ID"));
            
            //Now Populate the collection, first clear the collection
            hmFeltReopenBaleDetails.clear();
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            if(HistoryView) {
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_REOPEN_BALE_DETAIL_H WHERE DOC_NO='"+ resultSet.getString("DOC_NO") +"' AND BALE_DATE='"+ resultSet.getString("BALE_DATE")+"' AND REVISION_NO="+RevNo);
            }else {
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_REOPEN_BALE_DETAIL WHERE DOC_NO='"+ resultSet.getString("DOC_NO") +"' AND BALE_DATE='"+ resultSet.getString("BALE_DATE")+"'");
            }
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
                clsFeltReopenBaleDetails ObjFeltReopenBaleDetails = new clsFeltReopenBaleDetails();
                
                ObjFeltReopenBaleDetails.setAttribute("DOC_NO",resultSet.getString("DOC_NO"));
                ObjFeltReopenBaleDetails.setAttribute("BALE_NO",resultSet.getString("BALE_NO"));
                ObjFeltReopenBaleDetails.setAttribute("BALE_DATE",resultSet.getString("BALE_DATE"));
                ObjFeltReopenBaleDetails.setAttribute("PIECE_NO",resultSetTemp.getString("PIECE_NO"));
                ObjFeltReopenBaleDetails.setAttribute("LENGTH",resultSetTemp.getFloat("LENGTH"));
                ObjFeltReopenBaleDetails.setAttribute("WIDTH",resultSetTemp.getFloat("WIDTH"));
                ObjFeltReopenBaleDetails.setAttribute("WEIGHT",resultSetTemp.getFloat("WEIGHT"));
                ObjFeltReopenBaleDetails.setAttribute("GSM",resultSetTemp.getInt("GSM"));
                ObjFeltReopenBaleDetails.setAttribute("SQM",resultSetTemp.getInt("SQM"));
                ObjFeltReopenBaleDetails.setAttribute("SYN_PER",resultSetTemp.getInt("SYN_PER"));
                ObjFeltReopenBaleDetails.setAttribute("STYLE",resultSetTemp.getString("STYLE"));
                ObjFeltReopenBaleDetails.setAttribute("PRODUCT_CODE",resultSetTemp.getString("PRODUCT_CODE"));
                ObjFeltReopenBaleDetails.setAttribute("MCN_POSITION_DESC",resultSetTemp.getString("MCN_POSITION_DESC"));
                ObjFeltReopenBaleDetails.setAttribute("MACHINE_NO",resultSetTemp.getString("MACHINE_NO"));
                ObjFeltReopenBaleDetails.setAttribute("ORDER_NO",resultSetTemp.getString("ORDER_NO"));
                ObjFeltReopenBaleDetails.setAttribute("ORDER_DATE",resultSetTemp.getString("ORDER_DATE"));
                
                hmFeltReopenBaleDetails.put(Integer.toString(serialNoCounter),ObjFeltReopenBaleDetails);
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
            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.FELT_REOPEN_BALE_HEADER_H WHERE BALE_DATE='"+ baleDate+"' AND DOC_NO='"+baleNo+"'");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_REOPEN_BALE_HEADER_H WHERE BALE_DATE='"+dpDate+"' AND DOC_NO='"+dpNo+"'");
            
            while(rsTmp.next()) {
                clsFeltReopenBale ObjFeltReopenBale=new clsFeltReopenBale();
                
                ObjFeltReopenBale.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjFeltReopenBale.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjFeltReopenBale.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjFeltReopenBale.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjFeltReopenBale.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                ObjFeltReopenBale.setAttribute("FROM_IP",rsTmp.getString("FROM_IP"));
                
                hmHistoryList.put(Integer.toString(hmHistoryList.size()+1),ObjFeltReopenBale);
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
                strSQL="SELECT BALE_DATE,H.DOC_NO,RECEIVED_DATE,PARTY_CODE FROM PRODUCTION.FELT_REOPEN_BALE_HEADER H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=740 AND CANCELED=0 ORDER BY RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT BALE_DATE,H.DOC_NO,RECEIVED_DATE,PARTY_CODE FROM PRODUCTION.FELT_REOPEN_BALE_HEADER H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=740 AND CANCELED=0 ORDER BY BALE_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT BALE_DATE,H.DOC_NO,RECEIVED_DATE,PARTY_CODE FROM PRODUCTION.FELT_REOPEN_BALE_HEADER H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=740 AND CANCELED=0 ORDER BY DOC_NO";
            }
            
            ResultSet rsTmp=data.getConn().createStatement().executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp.next()) {
                
                Counter=Counter+1;
                clsFeltReopenBale ObjDoc=new clsFeltReopenBale();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                ObjDoc.setAttribute("DOC_DATE",rsTmp.getString("BALE_DATE"));
                ObjDoc.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                ObjDoc.setAttribute("PARTY_CODE",rsTmp.getString("PARTY_CODE"));
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
        int count=data.getIntValueFromDB("SELECT COUNT(PKG_PIECE_NO) FROM PRODUCTION.FELT_REOPEN_BALE_DETAIL WHERE PKG_PIECE_NO='"+pieceNo+"' AND PKG_BALE_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PKG_BALE_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
        System.out.println("SELECT COUNT(PKG_PIECE_NO) FROM PRODUCTION.FELT_REOPEN_BALE_DETAIL WHERE PKG_PIECE_NO='"+pieceNo+"' AND PKG_BALE_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PKG_BALE_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");        
        if(count>0) return true;
        else return false;
    }
    
    // checks piece no already exist in db in edit mode
    public boolean checkPieceNoInDB(String pieceNo, String baleNo, String baleDate){
        int count=data.getIntValueFromDB("SELECT COUNT(PKG_PIECE_NO) FROM PRODUCTION.FELT_REOPEN_BALE_DETAIL WHERE PKG_PIECE_NO='"+pieceNo+"' AND PKG_BALE_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PKG_BALE_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
        if(count>=1){
            int counter=0;
            try{
                Connection Conn=data.getConn();
                Statement stTmp=Conn.createStatement();
                ResultSet rsTmp=stTmp.executeQuery("SELECT PKG_BALE_DATE,DOC_NO FROM PRODUCTION.FELT_REOPEN_BALE_DETAIL WHERE PKG_PIECE_NO='"+pieceNo+"' AND PKG_BALE_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PKG_BALE_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
                while(rsTmp.next()){
                    if(rsTmp.getString("PKG_BALE_DATE").equals(EITLERPGLOBAL.formatDateDB(baleDate)) && rsTmp.getString("DOC_NO").equals(baleNo)) counter++;
                }
            }catch(SQLException e){e.printStackTrace();}
            if(counter==1 && count>=2) return true;
            else if(counter==1) return false;
            else return true;
        }else return false;
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
            
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return PartyName;
    }
    
    public static String getStation(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT PKG_STATION FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_BALE_NO='"+pPartyID+"' ORDER BY PKG_BALE_DATE DESC");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                PartyName=rsTmp.getString("PKG_STATION");
            }            
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return PartyName;
    }
    public static String getBaleDate(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT PKG_BALE_DATE FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_BALE_NO='"+pPartyID+"' ORDER BY PKG_BALE_DATE DESC");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                PartyName=rsTmp.getString("PKG_BALE_DATE");
            }
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return PartyName;
    }
    public static String getPartyCode(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT PKG_PARTY_CODE FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_BALE_NO='"+pPartyID+"' ORDER BY PKG_BALE_DATE DESC");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                PartyName=rsTmp.getString("PKG_PARTY_CODE");
            }
            
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return PartyName;
    }
    public static String getboxsize(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT PKG_BOX_SIZE FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_BALE_NO='"+pPartyID+"' ORDER BY PKG_BALE_DATE DESC");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                PartyName=rsTmp.getString("PKG_BOX_SIZE");
            }
            
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return PartyName;
    }
    public static String getmode(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT PKG_MODE_PACKING FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_BALE_NO='"+pPartyID+"' ORDER BY PKG_BALE_DATE DESC");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                PartyName=rsTmp.getString("PKG_MODE_PACKING");
            }
            
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return PartyName;
    }
    
     public static String gettransport(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT PKG_TRANSPORT_MODE FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_BALE_NO='"+pPartyID+"' ORDER BY PKG_BALE_DATE DESC");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                PartyName=rsTmp.getString("PKG_TRANSPORT_MODE");
            }
            
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return PartyName;
    }
     
     public static boolean CanCancel(String pDocNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean CanCancel=false;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            System.out.println("SELECT DOC_NO FROM PRODUCTION.FELT_REOPEN_BALE_HEADER WHERE DOC_NO='"+pDocNo+"'  AND APPROVED=0 AND CANCELED=0");
            rsTmp=stTmp.executeQuery("SELECT DOC_NO FROM PRODUCTION.FELT_REOPEN_BALE_HEADER WHERE DOC_NO='"+pDocNo+"'  AND APPROVED=0 AND CANCELED=0");
            
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
     
     public static void CancelDoc(String pAmendNo) {
        ResultSet rsTmp=null;
        
        if(CanCancel(pAmendNo)) {
            
            boolean Approved=false;
            //PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_STOCK_HEADER WHERE PIECE_AMEND_STOCK_NO
            try {
                rsTmp=data.getResult("SELECT APPROVED FROM PRODUCTION.FELT_REOPEN_BALE_HEADER WHERE DOC_NO='"+pAmendNo+"' ");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    Approved=rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();
                
                if(!Approved) {
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='"+pAmendNo+"' AND MODULE_ID=740");
                }
                data.Execute("UPDATE PRODUCTION.FELT_REOPEN_BALE_HEADER SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='"+pAmendNo+"'");
              
            }
            catch(Exception e) {
                
            }
        }
        
    }

    public HashMap getPieceList(String pDocNo, String pPartyCode, String pPieceNo, String pBaleNo, String pBaleDate) {
        HashMap hmPieceList = new HashMap();
        String ReOpenSQL = "";
        
        ReOpenSQL = "SELECT * FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_BALE_NO='"+pBaleNo+"' AND PKG_BALE_DATE='"+pBaleDate+"' AND PKG_PIECE_NO='"+pPieceNo+"' ";
        
        System.out.println("ReOpenSQL = " + ReOpenSQL);

        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(ReOpenSQL);
            Ready = true;

            while (resultSet.next()) {
                clsFeltReopenBale piece = new clsFeltReopenBale();
                boolean flag = true;

                if (flag) {

                    piece.setAttribute("DOC_NO", "");
                    piece.setAttribute("BALE_NO", resultSet.getString("PKG_BALE_NO"));
                    piece.setAttribute("BALE_DATE", resultSet.getString("PKG_BALE_DATE"));
                    piece.setAttribute("PIECE_NO", resultSet.getString("PKG_PIECE_NO"));
                    piece.setAttribute("LENGTH", resultSet.getString("PKG_LENGTH"));
                    piece.setAttribute("WIDTH", resultSet.getString("PKG_WIDTH"));
                    piece.setAttribute("GSM", resultSet.getString("PKG_GSM"));
                    piece.setAttribute("SQM", resultSet.getString("PKG_SQM"));
                    piece.setAttribute("SYN_PER", resultSet.getString("PKG_SYN_PER"));
                    piece.setAttribute("STYLE", resultSet.getString("PKG_STYLE"));
                    piece.setAttribute("PRODUCT_CODE", resultSet.getString("PKG_PRODUCT_CODE"));
                    piece.setAttribute("MCN_POSITION_DESC", resultSet.getString("PKG_MCN_POSITION_DESC"));
                    piece.setAttribute("MACHINE_NO", resultSet.getString("PKG_MACHINE_NO"));
                    piece.setAttribute("ORDER_NO", resultSet.getString("PKG_ORDER_NO"));
                    piece.setAttribute("ORDER_DATE", resultSet.getString("PKG_ORDER_DATE"));

                    hmPieceList.put(hmPieceList.size() + 1, piece);

                }
            }
            return hmPieceList;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error on fetch data : " + e.getMessage());
            e.printStackTrace();
            LastError = e.getMessage();
            return hmPieceList;
        }

    }
}
