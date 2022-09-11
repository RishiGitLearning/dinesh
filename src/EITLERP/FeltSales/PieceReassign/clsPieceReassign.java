/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.PieceReassign;

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
public class clsPieceReassign {
    public String LastError="";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet,rsHeader;
    //private ResultSet rsResultSet,
    
    public HashMap props;
    public boolean Ready = false;
    //Felt Order Collection
    public HashMap hmFeltPieceReassignDetails_existSales;
    public HashMap hmFeltPieceReassignDetails_existWIP;
    
    //History Related properties
    public boolean HistoryView=false;
    private String historyAmendDate="";
    private String historyAmendID="";
    private static int ModuleID = 824;
    
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
    public clsPieceReassign() {
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
        
        hmFeltPieceReassignDetails_existSales=new HashMap();
        hmFeltPieceReassignDetails_existWIP=new HashMap();
        
    }
    
    public boolean LoadData() {
        Ready=false;
        try {
            connection = data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REASSIGN_HEADER ORDER BY DOC_NO");
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
        ResultSet  resultSetTemp,resultSetWIP,resultSetHistory,rsHeader,rsHeaderH;
        Statement  statementTemp,statementWIp, statementHistory,stHeader,stHeaderH;
        try {
            // Felt Order Updation data connection
            connection = data.getConn();
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_REASSIGN_DETAIL WHERE DOC_NO='1'");
            
            statementWIp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetWIP = statementWIp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_REASSIGN_DETAIL_WIP WHERE DOC_NO='1'");
            
            
            // Felt order Updation data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REASSIGN_DETAIL_H WHERE DOC_NO='1'");
            
            stHeader=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHeader=stHeader.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_REASSIGN_HEADER WHERE DOC_NO='1'");
            
            stHeaderH=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHeaderH=stHeaderH.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_REASSIGN_HEADER_H WHERE DOC_NO='1'");

            //setAttribute("DOC_NO",);
                
            rsHeader.first();
            rsHeader.moveToInsertRow();
            
            rsHeader.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsHeader.updateString("DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            
            rsHeader.updateString("PIECE_NO",getAttribute("PIECE_NO").getString());
            
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
            rsHeaderH.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsHeaderH.updateString("DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            
            rsHeaderH.updateString("PIECE_NO",getAttribute("PIECE_NO").getString());
            
            rsHeaderH.updateString("REMARK",getAttribute("REMARK").getString());
            
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
            for(int i=1;i<=hmFeltPieceReassignDetails_existSales.size();i++) {
                clsPieceReassignDetails ObjFeltSalesOrderDetails = (clsPieceReassignDetails) hmFeltPieceReassignDetails_existSales.get(Integer.toString(i));
             
                //Insert records into Felt order Amendment table
                resultSetTemp.moveToInsertRow();
                
                resultSetTemp.updateString("SR_NO", i+"");
                resultSetTemp.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetTemp.updateString("PARTY_CODE", ObjFeltSalesOrderDetails.getAttribute("PARTY_CODE").getString());
                resultSetTemp.updateString("PARTY_NAME", ObjFeltSalesOrderDetails.getAttribute("PARTY_NAME").getString());
                resultSetTemp.updateString("MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                resultSetTemp.updateString("POSITION", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                resultSetTemp.updateString("POSITION_DESC", ObjFeltSalesOrderDetails.getAttribute("POSITION_DESC").getString());
                resultSetTemp.updateString("PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                resultSetTemp.updateString("PRODUCT", ObjFeltSalesOrderDetails.getAttribute("PRODUCT").getString());
                resultSetTemp.updateString("PRODUCT_DESCRIPTION", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_DESCRIPTION").getString());
                resultSetTemp.updateString("PRODUCT_GROUP", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_GROUP").getString());
                resultSetTemp.updateString("LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                resultSetTemp.updateString("WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                resultSetTemp.updateString("GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                resultSetTemp.updateString("THEORTICAL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("THEORTICAL_WEIGHT").getString());
                resultSetTemp.updateString("SQ_MT", ObjFeltSalesOrderDetails.getAttribute("SQ_MT").getString());
                resultSetTemp.updateString("STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                resultSetTemp.updateString("BILL_LENGTH", ObjFeltSalesOrderDetails.getAttribute("BILL_LENGTH").getString());
                resultSetTemp.updateString("BILL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("BILL_WIDTH").getString());
                resultSetTemp.updateString("BILL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("BILL_WEIGHT").getString());
                resultSetTemp.updateString("BILL_SQMTR", ObjFeltSalesOrderDetails.getAttribute("BILL_SQMTR").getString());
                resultSetTemp.updateString("BILL_GSM", ObjFeltSalesOrderDetails.getAttribute("BILL_GSM").getString());
                resultSetTemp.updateString("BILL_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("BILL_PRODUCT_CODE").getString());
                resultSetTemp.updateString("PIECE_STAGE", ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString());
                resultSetTemp.updateString("PR_PIECE_AB_FLAG", ObjFeltSalesOrderDetails.getAttribute("PR_PIECE_AB_FLAG").getString());
                
                
                resultSetTemp.insertRow();
                
                //Insert records into Felt Order Amendment History Table
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO",1);
                
                resultSetHistory.updateString("SR_NO", i+"");
                resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetHistory.updateString("PARTY_CODE", ObjFeltSalesOrderDetails.getAttribute("PARTY_CODE").getString());
                resultSetHistory.updateString("PARTY_NAME", ObjFeltSalesOrderDetails.getAttribute("PARTY_NAME").getString());
                resultSetHistory.updateString("MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                resultSetHistory.updateString("POSITION", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                resultSetHistory.updateString("POSITION_DESC", ObjFeltSalesOrderDetails.getAttribute("POSITION_DESC").getString());
                resultSetHistory.updateString("PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                resultSetHistory.updateString("PRODUCT", ObjFeltSalesOrderDetails.getAttribute("PRODUCT").getString());
                resultSetHistory.updateString("PRODUCT_DESCRIPTION", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_DESCRIPTION").getString());
                resultSetHistory.updateString("PRODUCT_GROUP", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_GROUP").getString());
                resultSetHistory.updateString("LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                resultSetHistory.updateString("WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                resultSetHistory.updateString("GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                resultSetHistory.updateString("THEORTICAL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("THEORTICAL_WEIGHT").getString());
                resultSetHistory.updateString("SQ_MT", ObjFeltSalesOrderDetails.getAttribute("SQ_MT").getString());
                resultSetHistory.updateString("STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                resultSetHistory.updateString("BILL_LENGTH", ObjFeltSalesOrderDetails.getAttribute("BILL_LENGTH").getString());
                resultSetHistory.updateString("BILL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("BILL_WIDTH").getString());
                resultSetHistory.updateString("BILL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("BILL_WEIGHT").getString());
                resultSetHistory.updateString("BILL_SQMTR", ObjFeltSalesOrderDetails.getAttribute("BILL_SQMTR").getString());
                resultSetHistory.updateString("BILL_GSM", ObjFeltSalesOrderDetails.getAttribute("BILL_GSM").getString());
                resultSetHistory.updateString("BILL_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("BILL_PRODUCT_CODE").getString());
                resultSetHistory.updateString("PIECE_STAGE", ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString());
                resultSetHistory.updateString("PR_PIECE_AB_FLAG", ObjFeltSalesOrderDetails.getAttribute("PR_PIECE_AB_FLAG").getString());
                
                resultSetHistory.insertRow();

            }
            
            for(int i=1;i<=hmFeltPieceReassignDetails_existWIP.size();i++) {
                clsPieceReassignDetails ObjFeltSalesOrderDetails = (clsPieceReassignDetails) hmFeltPieceReassignDetails_existWIP.get(Integer.toString(i));
             
                //Insert records into Felt order Amendment table
                resultSetWIP.moveToInsertRow();
                
                
                resultSetWIP.updateString("SR_NO", i+"");
                
                resultSetWIP.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetWIP.updateString("EXT_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("EXT_PIECE_NO").getString());
                
                resultSetWIP.updateString("NEW_ASSIGN_NO", ObjFeltSalesOrderDetails.getAttribute("NEW_ASSIGN_NO").getString());
                resultSetWIP.updateString("CALCEL_PIECE", ObjFeltSalesOrderDetails.getAttribute("CALCEL_PIECE").getString());
                resultSetWIP.updateString("REMARK", ObjFeltSalesOrderDetails.getAttribute("REMARK").getString());
                
                resultSetWIP.updateString("PARTY_CODE", ObjFeltSalesOrderDetails.getAttribute("PARTY_CODE").getString());
                resultSetWIP.updateString("PARTY_NAME", ObjFeltSalesOrderDetails.getAttribute("PARTY_NAME").getString());
                resultSetWIP.updateString("MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                resultSetWIP.updateString("POSITION", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                resultSetWIP.updateString("POSITION_DESC", ObjFeltSalesOrderDetails.getAttribute("POSITION_DESC").getString());
                resultSetWIP.updateString("PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                resultSetWIP.updateString("PRODUCT", ObjFeltSalesOrderDetails.getAttribute("PRODUCT").getString());
                resultSetWIP.updateString("PRODUCT_DESCRIPTION", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_DESCRIPTION").getString());
                resultSetWIP.updateString("PRODUCT_GROUP", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_GROUP").getString());
                resultSetWIP.updateString("LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                resultSetWIP.updateString("WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                resultSetWIP.updateString("GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                resultSetWIP.updateString("THEORTICAL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("THEORTICAL_WEIGHT").getString());
                resultSetWIP.updateString("SQ_MT", ObjFeltSalesOrderDetails.getAttribute("SQ_MT").getString());
                resultSetWIP.updateString("STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                resultSetWIP.updateString("BILL_LENGTH", ObjFeltSalesOrderDetails.getAttribute("BILL_LENGTH").getString());
                resultSetWIP.updateString("BILL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("BILL_WIDTH").getString());
                resultSetWIP.updateString("BILL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("BILL_WEIGHT").getString());
                resultSetWIP.updateString("BILL_SQMTR", ObjFeltSalesOrderDetails.getAttribute("BILL_SQMTR").getString());
                resultSetWIP.updateString("BILL_GSM", ObjFeltSalesOrderDetails.getAttribute("BILL_GSM").getString());
                resultSetWIP.updateString("BILL_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("BILL_PRODUCT_CODE").getString());
                resultSetWIP.updateString("PIECE_STAGE", ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString());
                
                resultSetWIP.insertRow();
            }
            
            
            
            
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=ModuleID; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo=getAttribute("DOC_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.FELT_SALES_PIECE_REASSIGN_HEADER";
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
        
        ResultSet  resultSetTemp,resultSetHistory,rsHeader,rsHeaderH,rsRegister,resultSetWIP,rsUpdateWIP;
        Statement  statementTemp, statementHistory,stHeader,stHeaderH,stRegister,statementWIp,stUpdateWIP;
        int revisionNo;
        try {
            // Production data connection
           
            
            statementWIp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetWIP = statementWIp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_REASSIGN_DETAIL_WIP WHERE DOC_NO='1'");
           
            stUpdateWIP = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsUpdateWIP = stUpdateWIP.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_REASSIGN_DETAIL_WIP WHERE DOC_NO='1'");
            
            
            
            // Production data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            //Get the Maximum Revision No in History Table.
            resultSetHistory=statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_SALES_PIECE_REASSIGN_DETAIL_H WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
            resultSetHistory.first();
            if(resultSetHistory.getRow()>0){
                revisionNo=resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from production data table before insert
            //statementHistory.execute("DELETE FROM PRODUCTION.FELT_SALES_PIECE_REASSIGN_DETAIL WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
            
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REASSIGN_DETAIL_H WHERE DOC_NO=''");
            
            stHeaderH=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHeaderH=stHeaderH.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_REASSIGN_HEADER_H WHERE DOC_NO=''");
            
            //Now Update records into FELT_CN_TEMP_HEADER tables
            
            resultSet.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            resultSet.updateString("DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            resultSet.updateString("PIECE_NO",getAttribute("PIECE_NO").getString());
            
            resultSet.updateString("REMARK",getAttribute("REMARK").getString());
            
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
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_SALES_PIECE_REASSIGN_HEADER_H WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
            
            RevNo++;
           
            rsHeaderH.moveToInsertRow();
            
            rsHeaderH.updateString("REVISION_NO",RevNo+"");
            
            rsHeaderH.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsHeaderH.updateString("DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsHeaderH.updateString("PIECE_NO",getAttribute("PIECE_NO").getString());
            
            rsHeaderH.updateString("REMARK",getAttribute("REMARK").getString());
            
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
            data.Execute("DELETE FROM PRODUCTION.FELT_SALES_PIECE_REASSIGN_DETAIL WHERE DOC_NO='"+OrderNo+"'");
            data.Execute("DELETE FROM PRODUCTION.FELT_SALES_PIECE_REASSIGN_DETAIL_WIP WHERE DOC_NO='"+OrderNo+"'");
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_REASSIGN_DETAIL WHERE DOC_NO='1'");
             
            
            int RevNoH=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_SALES_PIECE_REASSIGN_DETAIL_H WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
            RevNoH++;

            resultSetHistory.moveToInsertRow();
            
            for(int i=1;i<=hmFeltPieceReassignDetails_existSales.size();i++) {
                clsPieceReassignDetails ObjFeltSalesOrderDetails=(clsPieceReassignDetails) hmFeltPieceReassignDetails_existSales.get(Integer.toString(i));
                
                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateInt("SR_NO",i);
                resultSetTemp.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetTemp.updateString("PARTY_CODE", ObjFeltSalesOrderDetails.getAttribute("PARTY_CODE").getString());
                resultSetTemp.updateString("PARTY_NAME", ObjFeltSalesOrderDetails.getAttribute("PARTY_NAME").getString());
                resultSetTemp.updateString("MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                resultSetTemp.updateString("POSITION", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                resultSetTemp.updateString("POSITION_DESC", ObjFeltSalesOrderDetails.getAttribute("POSITION_DESC").getString());
                resultSetTemp.updateString("PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                resultSetTemp.updateString("PRODUCT", ObjFeltSalesOrderDetails.getAttribute("PRODUCT").getString());
                resultSetTemp.updateString("PRODUCT_DESCRIPTION", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_DESCRIPTION").getString());
                resultSetTemp.updateString("PRODUCT_GROUP", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_GROUP").getString());
                resultSetTemp.updateString("LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                resultSetTemp.updateString("WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                resultSetTemp.updateString("GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                resultSetTemp.updateString("THEORTICAL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("THEORTICAL_WEIGHT").getString());
                resultSetTemp.updateString("SQ_MT", ObjFeltSalesOrderDetails.getAttribute("SQ_MT").getString());
                resultSetTemp.updateString("STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                resultSetTemp.updateString("BILL_LENGTH", ObjFeltSalesOrderDetails.getAttribute("BILL_LENGTH").getString());
                resultSetTemp.updateString("BILL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("BILL_WIDTH").getString());
                resultSetTemp.updateString("BILL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("BILL_WEIGHT").getString());
                resultSetTemp.updateString("BILL_SQMTR", ObjFeltSalesOrderDetails.getAttribute("BILL_SQMTR").getString());
                resultSetTemp.updateString("BILL_GSM", ObjFeltSalesOrderDetails.getAttribute("BILL_GSM").getString());
                resultSetTemp.updateString("BILL_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("BILL_PRODUCT_CODE").getString());
                resultSetTemp.updateString("PIECE_STAGE", ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString());
                resultSetTemp.updateString("PR_PIECE_AB_FLAG", ObjFeltSalesOrderDetails.getAttribute("PR_PIECE_AB_FLAG").getString());
                
                resultSetTemp.insertRow();
                
                
                resultSetHistory.updateInt("REVISION_NO",RevNoH);
                resultSetHistory.updateInt("SR_NO",i);
                
                resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                
                resultSetHistory.updateString("PARTY_CODE", ObjFeltSalesOrderDetails.getAttribute("PARTY_CODE").getString());
                resultSetHistory.updateString("PARTY_NAME", ObjFeltSalesOrderDetails.getAttribute("PARTY_NAME").getString());
                resultSetHistory.updateString("MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                resultSetHistory.updateString("POSITION", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                resultSetHistory.updateString("POSITION_DESC", ObjFeltSalesOrderDetails.getAttribute("POSITION_DESC").getString());
                resultSetHistory.updateString("PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                resultSetHistory.updateString("PRODUCT", ObjFeltSalesOrderDetails.getAttribute("PRODUCT").getString());
                resultSetHistory.updateString("PRODUCT_DESCRIPTION", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_DESCRIPTION").getString());
                resultSetHistory.updateString("PRODUCT_GROUP", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_GROUP").getString());
                resultSetHistory.updateString("LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                resultSetHistory.updateString("WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                resultSetHistory.updateString("GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                resultSetHistory.updateString("THEORTICAL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("THEORTICAL_WEIGHT").getString());
                resultSetHistory.updateString("SQ_MT", ObjFeltSalesOrderDetails.getAttribute("SQ_MT").getString());
                resultSetHistory.updateString("STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                resultSetHistory.updateString("BILL_LENGTH", ObjFeltSalesOrderDetails.getAttribute("BILL_LENGTH").getString());
                resultSetHistory.updateString("BILL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("BILL_WIDTH").getString());
                resultSetHistory.updateString("BILL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("BILL_WEIGHT").getString());
                resultSetHistory.updateString("BILL_SQMTR", ObjFeltSalesOrderDetails.getAttribute("BILL_SQMTR").getString());
                resultSetHistory.updateString("BILL_GSM", ObjFeltSalesOrderDetails.getAttribute("BILL_GSM").getString());
                resultSetHistory.updateString("BILL_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("BILL_PRODUCT_CODE").getString());
                resultSetHistory.updateString("PIECE_STAGE", ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString());
                resultSetHistory.updateString("PR_PIECE_AB_FLAG", ObjFeltSalesOrderDetails.getAttribute("PR_PIECE_AB_FLAG").getString());
                
                
                
                resultSetHistory.insertRow();
                
                
                
                // Final Approval and save to PIECE REGISTER 
                if(getAttribute("APPROVAL_STATUS").getString().equals("F") && !ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString().equals(""))
                {
                }
            }
            
            for(int i=1;i<=hmFeltPieceReassignDetails_existWIP.size();i++) {
                clsPieceReassignDetails ObjFeltSalesOrderDetails = (clsPieceReassignDetails) hmFeltPieceReassignDetails_existWIP.get(Integer.toString(i));
             
                //Insert records into Felt order Amendment table
                resultSetWIP.moveToInsertRow();
                
                
                resultSetWIP.updateString("SR_NO", i+"");
                
                resultSetWIP.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetWIP.updateString("EXT_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("EXT_PIECE_NO").getString());
                
                resultSetWIP.updateString("NEW_ASSIGN_NO", ObjFeltSalesOrderDetails.getAttribute("NEW_ASSIGN_NO").getString());
                resultSetWIP.updateString("CALCEL_PIECE", ObjFeltSalesOrderDetails.getAttribute("CALCEL_PIECE").getString());
                resultSetWIP.updateString("REMARK", ObjFeltSalesOrderDetails.getAttribute("REMARK").getString());
                
                resultSetWIP.updateString("PARTY_CODE", ObjFeltSalesOrderDetails.getAttribute("PARTY_CODE").getString());
                resultSetWIP.updateString("PARTY_NAME", ObjFeltSalesOrderDetails.getAttribute("PARTY_NAME").getString());
                resultSetWIP.updateString("MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                resultSetWIP.updateString("POSITION", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                resultSetWIP.updateString("POSITION_DESC", ObjFeltSalesOrderDetails.getAttribute("POSITION_DESC").getString());
                resultSetWIP.updateString("PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                resultSetWIP.updateString("PRODUCT", ObjFeltSalesOrderDetails.getAttribute("PRODUCT").getString());
                resultSetWIP.updateString("PRODUCT_DESCRIPTION", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_DESCRIPTION").getString());
                resultSetWIP.updateString("PRODUCT_GROUP", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_GROUP").getString());
                resultSetWIP.updateString("LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                resultSetWIP.updateString("WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                resultSetWIP.updateString("GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                resultSetWIP.updateString("THEORTICAL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("THEORTICAL_WEIGHT").getString());
                resultSetWIP.updateString("SQ_MT", ObjFeltSalesOrderDetails.getAttribute("SQ_MT").getString());
                resultSetWIP.updateString("STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                resultSetWIP.updateString("BILL_LENGTH", ObjFeltSalesOrderDetails.getAttribute("BILL_LENGTH").getString());
                resultSetWIP.updateString("BILL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("BILL_WIDTH").getString());
                resultSetWIP.updateString("BILL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("BILL_WEIGHT").getString());
                resultSetWIP.updateString("BILL_SQMTR", ObjFeltSalesOrderDetails.getAttribute("BILL_SQMTR").getString());
                resultSetWIP.updateString("BILL_GSM", ObjFeltSalesOrderDetails.getAttribute("BILL_GSM").getString());
                resultSetWIP.updateString("BILL_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("BILL_PRODUCT_CODE").getString());
                resultSetWIP.updateString("PIECE_STAGE", ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString());
                
                resultSetWIP.insertRow();
            }
            
            
            
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=ModuleID; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo=getAttribute("DOC_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=(String)getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.FELT_SALES_PIECE_REASSIGN_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator=true;
            ObjFeltProductionApprovalFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName="DOC_NO";
            
            if(getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFeltProductionApprovalFlow.To=(int)getAttribute("SEND_DOC_TO").getVal();
                ObjFeltProductionApprovalFlow.ExplicitSendTo=true;
            }
            //if(getAttribute("APPROVAL_STATUS").getString().equals("A") || getAttribute("APPROVAL_STATUS").getString().equals("R")) {
//                    String Subject = "Felt Order Pending Document : "+getAttribute("DOC_NO").getString();
//                    String Message = "Document No : "+getAttribute("DOC_NO").getString()+" is added in your PENDING DOCUMENT"
//                            + "\n\n\n\n SDML-ERP : http://200.0.0.227:8080/SDMLERP";
//                 
//                    String send_to = clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, (int)getAttribute("TO").getVal());
//
//                    System.out.println("USERID : "+(int)getAttribute("TO").getVal()+", send_to : "+send_to);
//                    try{
//                        System.out.println(" Host IP : "+EITLERPGLOBAL.SMTPHostIP);
//                        JMail.SendMail(EITLERPGLOBAL.SMTPHostIP, "sdmlerp@dineshmills.com", send_to, Message, Subject, "sdmlerp@dineshmills.com");
//                    }catch(Exception e)
//                    {
//                        e.printStackTrace();
//                    }
            //}
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REASSIGN_HEADER SET REJECTED=0,CHANGED=1 WHERE DOC_NO ='"+getAttribute("DOC_NO").getString()+"'");
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_SALES_PIECE_REASSIGN_HEADER WHERE  DOC_NO='"+documentNo +"' AND APPROVED="+1;
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
                    strSQL = "DELETE FROM PRODUCTION.FELT_SALES_PIECE_REASSIGN_HEADER WHERE "
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_SALES_PIECE_REASSIGN_HEADER WHERE DOC_NO='"+ orderupdDocumentNo +"' AND APPROVED=1";
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
            String strSql = "SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REASSIGN_HEADER WHERE  " + stringFindQuery + "";
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
        ResultSet  resultSetTemp,resultSetExistWIP;
        Statement  statementTemp,statementExistWIP;
        int serialNoCounter=0;
        int RevNo=0;
    
        try {
           
            setAttribute("REVISION_NO",0);
            
            setAttribute("DOC_NO",resultSet.getString("DOC_NO"));
            setAttribute("DOC_DATE",resultSet.getDate("DOC_DATE"));
            setAttribute("PIECE_NO",resultSet.getString("PIECE_NO"));
            setAttribute("REMARK",resultSet.getString("REMARK"));
            
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
            
            hmFeltPieceReassignDetails_existSales.clear();
            hmFeltPieceReassignDetails_existWIP.clear();
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            if(HistoryView) {
                resultSetTemp=statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_REASSIGN_DETAIL_H WHERE DOC_NO='"+resultSet.getString("DOC_NO")+"'  AND REVISION_NO="+RevNo+" ORDER BY SR_NO DESC");
            }else {
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_REASSIGN_DETAIL WHERE DOC_NO='"+resultSet.getString("DOC_NO")+"'  ORDER BY DOC_NO");
            }
            
            statementExistWIP = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            resultSetExistWIP=statementExistWIP.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_REASSIGN_DETAIL_WIP WHERE DOC_NO='"+resultSet.getString("DOC_NO")+"'");
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
  
                clsPieceReassignDetails ObjFeltSalesOrderDetails = new clsPieceReassignDetails();
                
                ObjFeltSalesOrderDetails.setAttribute("SR_NO",resultSetTemp.getString("SR_NO"));
                ObjFeltSalesOrderDetails.setAttribute("DOC_NO",resultSetTemp.getString("DOC_NO"));
                
                ObjFeltSalesOrderDetails.setAttribute("PIECE_NO",resultSetTemp.getString("PIECE_NO"));
                ObjFeltSalesOrderDetails.setAttribute("PR_PIECE_AB_FLAG",resultSetTemp.getString("PR_PIECE_AB_FLAG"));
                ObjFeltSalesOrderDetails.setAttribute("PARTY_CODE",resultSetTemp.getString("PARTY_CODE"));
                ObjFeltSalesOrderDetails.setAttribute("PARTY_NAME",resultSetTemp.getString("PARTY_NAME"));
                ObjFeltSalesOrderDetails.setAttribute("MACHINE_NO",resultSetTemp.getString("MACHINE_NO"));
                ObjFeltSalesOrderDetails.setAttribute("POSITION",resultSetTemp.getString("POSITION"));
                ObjFeltSalesOrderDetails.setAttribute("POSITION_DESC",resultSetTemp.getString("POSITION_DESC"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT",resultSetTemp.getString("PRODUCT"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT_DESCRIPTION",resultSetTemp.getString("PRODUCT_DESCRIPTION"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT_GROUP",resultSetTemp.getString("PRODUCT_GROUP"));
                ObjFeltSalesOrderDetails.setAttribute("LENGTH",resultSetTemp.getString("LENGTH"));
                ObjFeltSalesOrderDetails.setAttribute("WIDTH",resultSetTemp.getString("WIDTH"));
                ObjFeltSalesOrderDetails.setAttribute("GSM",resultSetTemp.getString("GSM"));
                ObjFeltSalesOrderDetails.setAttribute("THEORTICAL_WEIGHT",resultSetTemp.getString("THEORTICAL_WEIGHT"));
                ObjFeltSalesOrderDetails.setAttribute("SQ_MT",resultSetTemp.getString("SQ_MT"));
                ObjFeltSalesOrderDetails.setAttribute("STYLE",resultSetTemp.getString("STYLE"));
                ObjFeltSalesOrderDetails.setAttribute("BILL_LENGTH",resultSetTemp.getString("BILL_LENGTH"));
                ObjFeltSalesOrderDetails.setAttribute("BILL_WIDTH",resultSetTemp.getString("BILL_WIDTH"));
                ObjFeltSalesOrderDetails.setAttribute("BILL_WEIGHT",resultSetTemp.getString("BILL_WEIGHT"));
                ObjFeltSalesOrderDetails.setAttribute("BILL_SQMTR",resultSetTemp.getString("BILL_SQMTR"));
                ObjFeltSalesOrderDetails.setAttribute("BILL_GSM",resultSetTemp.getString("BILL_GSM"));
                ObjFeltSalesOrderDetails.setAttribute("BILL_PRODUCT_CODE",resultSetTemp.getString("BILL_PRODUCT_CODE"));
                ObjFeltSalesOrderDetails.setAttribute("PIECE_STAGE",resultSetTemp.getString("PIECE_STAGE"));
                
                hmFeltPieceReassignDetails_existSales.put(Integer.toString(serialNoCounter),ObjFeltSalesOrderDetails);
            }
            resultSetTemp.close();
            statementTemp.close();
            
            serialNoCounter=0;
            
            while(resultSetExistWIP.next()) {
                serialNoCounter++;
  
                clsPieceReassignDetails ObjFeltSalesOrderDetails = new clsPieceReassignDetails();
                
                ObjFeltSalesOrderDetails.setAttribute("SR_NO",resultSetExistWIP.getString("SR_NO"));
                ObjFeltSalesOrderDetails.setAttribute("DOC_NO",resultSetExistWIP.getString("DOC_NO"));
                
                ObjFeltSalesOrderDetails.setAttribute("PIECE_NO",resultSetExistWIP.getString("PIECE_NO"));
                ObjFeltSalesOrderDetails.setAttribute("EXT_PIECE_NO",resultSetExistWIP.getString("EXT_PIECE_NO"));
                
                ObjFeltSalesOrderDetails.setAttribute("NEW_ASSIGN_NO",resultSetExistWIP.getString("NEW_ASSIGN_NO"));
                ObjFeltSalesOrderDetails.setAttribute("CALCEL_PIECE",resultSetExistWIP.getString("CALCEL_PIECE"));
                ObjFeltSalesOrderDetails.setAttribute("REMARK",resultSetExistWIP.getString("REMARK"));
                
                ObjFeltSalesOrderDetails.setAttribute("PARTY_CODE",resultSetExistWIP.getString("PARTY_CODE"));
                ObjFeltSalesOrderDetails.setAttribute("PARTY_NAME",resultSetExistWIP.getString("PARTY_NAME"));
                ObjFeltSalesOrderDetails.setAttribute("MACHINE_NO",resultSetExistWIP.getString("MACHINE_NO"));
                ObjFeltSalesOrderDetails.setAttribute("POSITION",resultSetExistWIP.getString("POSITION"));
                ObjFeltSalesOrderDetails.setAttribute("POSITION_DESC",resultSetExistWIP.getString("POSITION_DESC"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT",resultSetExistWIP.getString("PRODUCT"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT_DESCRIPTION",resultSetExistWIP.getString("PRODUCT_DESCRIPTION"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT_GROUP",resultSetExistWIP.getString("PRODUCT_GROUP"));
                ObjFeltSalesOrderDetails.setAttribute("LENGTH",resultSetExistWIP.getString("LENGTH"));
                ObjFeltSalesOrderDetails.setAttribute("WIDTH",resultSetExistWIP.getString("WIDTH"));
                ObjFeltSalesOrderDetails.setAttribute("GSM",resultSetExistWIP.getString("GSM"));
                ObjFeltSalesOrderDetails.setAttribute("THEORTICAL_WEIGHT",resultSetExistWIP.getString("THEORTICAL_WEIGHT"));
                ObjFeltSalesOrderDetails.setAttribute("SQ_MT",resultSetExistWIP.getString("SQ_MT"));
                ObjFeltSalesOrderDetails.setAttribute("STYLE",resultSetExistWIP.getString("STYLE"));
                ObjFeltSalesOrderDetails.setAttribute("BILL_LENGTH",resultSetExistWIP.getString("BILL_LENGTH"));
                ObjFeltSalesOrderDetails.setAttribute("BILL_WIDTH",resultSetExistWIP.getString("BILL_WIDTH"));
                ObjFeltSalesOrderDetails.setAttribute("BILL_WEIGHT",resultSetExistWIP.getString("BILL_WEIGHT"));
                ObjFeltSalesOrderDetails.setAttribute("BILL_SQMTR",resultSetExistWIP.getString("BILL_SQMTR"));
                ObjFeltSalesOrderDetails.setAttribute("BILL_GSM",resultSetExistWIP.getString("BILL_GSM"));
                ObjFeltSalesOrderDetails.setAttribute("BILL_PRODUCT_CODE",resultSetExistWIP.getString("BILL_PRODUCT_CODE"));
                ObjFeltSalesOrderDetails.setAttribute("PIECE_STAGE",resultSetExistWIP.getString("PIECE_STAGE"));
                
                hmFeltPieceReassignDetails_existWIP.put(Integer.toString(serialNoCounter),ObjFeltSalesOrderDetails);
            }
            resultSetExistWIP.close();
            statementExistWIP.close();
            
           
            
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
            hmFeltPieceReassignDetails_existSales.clear();
            
            String strSql = "SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REASSIGN_HEADER_H WHERE DOC_NO = " + pDocNo + "";
            System.out.println("STR SQL : "+strSql);
            connection=data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet=statement.executeQuery(strSql);
            resultSet.first();
            setAttribute("DOC_NO",resultSet.getString("DOC_NO"));
            setAttribute("REVISION_NO",resultSet.getString("REVISION_NO"));
                setAttribute("UPDATED_BY",resultSet.getString("UPDATED_BY"));
                setAttribute("DOC_DATE",resultSet.getString("DOC_DATE"));
                setAttribute("HIERARCHY_ID",resultSet.getInt("HIERARCHY_ID"));
                
                setAttribute("DOC_NO",resultSet.getString("DOC_NO"));
                setAttribute("DOC_DATE",resultSet.getDate("DOC_DATE"));
                setAttribute("PIECE_NO",resultSet.getString("PIECE_NO"));
                
                setAttribute("REMARK",resultSet.getString("REMARK"));
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_REASSIGN_DETAIL_H WHERE DOC_NO='"+pDocNo+"'");
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
                
                
                clsPieceReassignDetails ObjFeltSalesOrderDetails = new clsPieceReassignDetails();
                
                ObjFeltSalesOrderDetails.setAttribute("SR_NO",resultSetTemp.getString("SR_NO"));
                ObjFeltSalesOrderDetails.setAttribute("DOC_NO",resultSetTemp.getString("DOC_NO"));
                
                ObjFeltSalesOrderDetails.setAttribute("PARTY_CODE",resultSetTemp.getString("PARTY_CODE"));
                ObjFeltSalesOrderDetails.setAttribute("PARTY_NAME",resultSetTemp.getString("PARTY_NAME"));
                ObjFeltSalesOrderDetails.setAttribute("MACHINE_NO",resultSetTemp.getString("MACHINE_NO"));
                ObjFeltSalesOrderDetails.setAttribute("POSITION",resultSetTemp.getString("POSITION"));
                ObjFeltSalesOrderDetails.setAttribute("POSITION_DESC",resultSetTemp.getString("POSITION_DESC"));
                ObjFeltSalesOrderDetails.setAttribute("PIECE_NO",resultSetTemp.getString("PIECE_NO"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT",resultSetTemp.getString("PRODUCT"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT_DESCRIPTION",resultSetTemp.getString("PRODUCT_DESCRIPTION"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT_GROUP",resultSetTemp.getString("PRODUCT_GROUP"));
                ObjFeltSalesOrderDetails.setAttribute("LENGTH",resultSetTemp.getString("LENGTH"));
                ObjFeltSalesOrderDetails.setAttribute("WIDTH",resultSetTemp.getString("WIDTH"));
                ObjFeltSalesOrderDetails.setAttribute("GSM",resultSetTemp.getString("GSM"));
                ObjFeltSalesOrderDetails.setAttribute("THEORTICAL_WEIGHT",resultSetTemp.getString("THEORTICAL_WEIGHT"));
                ObjFeltSalesOrderDetails.setAttribute("SQ_MT",resultSetTemp.getString("SQ_MT"));
                ObjFeltSalesOrderDetails.setAttribute("STYLE",resultSetTemp.getString("STYLE"));
                ObjFeltSalesOrderDetails.setAttribute("REQ_MONTH",resultSetTemp.getString("REQ_MONTH"));
                ObjFeltSalesOrderDetails.setAttribute("SYN_PER",resultSetTemp.getString("SYN_PER"));
                ObjFeltSalesOrderDetails.setAttribute("REMARK",resultSetTemp.getString("REMARK"));
                ObjFeltSalesOrderDetails.setAttribute("BILL_LENGTH",resultSetTemp.getString("BILL_LENGTH"));
                ObjFeltSalesOrderDetails.setAttribute("BILL_WIDTH",resultSetTemp.getString("BILL_WIDTH"));
                ObjFeltSalesOrderDetails.setAttribute("BILL_WEIGHT",resultSetTemp.getString("BILL_WEIGHT"));
                ObjFeltSalesOrderDetails.setAttribute("BILL_SQMTR",resultSetTemp.getString("BILL_SQMTR"));
                ObjFeltSalesOrderDetails.setAttribute("BILL_GSM",resultSetTemp.getString("BILL_GSM"));
                ObjFeltSalesOrderDetails.setAttribute("BILL_PRODUCT_CODE",resultSetTemp.getString("BILL_PRODUCT_CODE"));
                ObjFeltSalesOrderDetails.setAttribute("PIECE_STAGE",resultSetTemp.getString("PIECE_STAGE"));
               
               
               hmFeltPieceReassignDetails_existSales.put(Integer.toString(serialNoCounter),ObjFeltSalesOrderDetails);
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

            rsTmp=stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REASSIGN_HEADER_H WHERE DOC_NO='"+productionDocumentNo+"'");
            while(rsTmp.next()) {
                clsPieceReassign felt_order = new clsPieceReassign();
                
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
            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REASSIGN_HEADER_H WHERE DOC_NO ='"+pDocNo+"'");
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
                strSQL="SELECT DISTINCT DOC_NO,DOC_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_SALES_PIECE_REASSIGN_HEADER, PRODUCTION.FELT_PROD_DOC_DATA   WHERE DOC_NO=DOC_NO AND STATUS='W' AND MODULE_ID="+ModuleID+" AND CANCELED=0  AND USER_ID="+pUserID+" ORDER BY RECEIVED_DATE,DOC_NO";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT DISTINCT DOC_NO,DOC_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_SALES_PIECE_REASSIGN_HEADER, PRODUCTION.FELT_PROD_DOC_DATA   WHERE DOC_NO=DOC_NO AND STATUS='W' AND MODULE_ID="+ModuleID+" AND CANCELED=0  AND USER_ID="+pUserID+" ORDER BY RECEIVED_DATE,DOC_NO";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT DISTINCT DOC_NO,DOC_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_SALES_PIECE_REASSIGN_HEADER, PRODUCTION.FELT_PROD_DOC_DATA   WHERE DOC_NO=DOC_NO AND STATUS='W' AND MODULE_ID="+ModuleID+" AND CANCELED=0  AND USER_ID="+pUserID+"  ORDER BY DOC_NO";
            }
            
            ResultSet rsTmp=data.getConn().createStatement().executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp.next()) {
                
                Counter=Counter+1;
                clsPieceReassign ObjDoc=new clsPieceReassign();
                
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
