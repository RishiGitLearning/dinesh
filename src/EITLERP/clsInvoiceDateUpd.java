/*
 * clsInvoiceDateUpd.java
 *
 * Created on March 12, 2013, 3:10 PM
 */

package EITLERP;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

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

public class clsInvoiceDateUpd {
    
    public String LastError="";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet,resultSet1;
    
    public HashMap props;
    public boolean Ready = false;
    //Felt Order Collection
    public HashMap hmFeltConsignmentDetails;
    //public boolean HistoryView=false;
    //History Related properties
    public boolean HistoryView=false;
    private String historyConsignmentDate="";
    private String historyConsignmentNo="";
    
    public static int ModuleID=736;
    
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
    public clsInvoiceDateUpd() {
        LastError = "";
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("UPD_NO", new Variant(""));
        props.put("UPD_DATE", new Variant(""));
        props.put("UPD_MAIN_CODE", new Variant(""));
        props.put("UPD_PARTY_CODE", new Variant(""));  
       

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
        
        hmFeltConsignmentDetails=new HashMap();
    }
    
    public boolean LoadData() {
        Ready=false;
        try {
            connection = data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //resultSet=statement.executeQuery("SELECT UPD_NO,CON_DATE FROM DINESHMILLS.D_SAL_INVDATA_UPD_LC WHERE CON_DATE >= '"+EITLERPGLOBAL.FinFromDateDB+"' AND CON_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY CON_DATE");
            resultSet=statement.executeQuery("SELECT * FROM DINESHMILLS.D_SAL_INVDATA_UPD_LC ORDER BY UPD_NO");
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
            if(HistoryView) setHistoryData(historyConsignmentDate, historyConsignmentNo);
            else setData();
            setData();
            return true;
        }catch(Exception e) {
            LastError=e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean Insert() {
        
        //Statement stHistory,stHeader,stHDetail;
        //ResultSet rsHistory,rsHeader,rsHDetail;
        
        ResultSet  resultSetTemp,resultSetDetail,resultSetHistory;
        Statement statementTemp,statementDetail,statementHistory;
        
        try {
            // Consignment connection
            connection = data.getConn();
            statementDetail = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetDetail = statementDetail.executeQuery("SELECT * FROM  DINESHMILLS.D_SAL_INVDATA_UPD_LC WHERE UPD_NO='' order by sr_no*1");
            
            //stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            // Consignment data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM  DINESHMILLS.D_SAL_INVDATA_UPD_LC_H WHERE UPD_NO=''");
            
            
            
            
            // Felt order Updation Last Free No,
            setAttribute("UPD_NO",getNextFreeNo(736,true));
            
            
            
            //Now Insert records into FELT_CONSIGNMENT  tables
            for(int i=1;i<=hmFeltConsignmentDetails.size();i++) {
                clsInvoiceDateUpdDetails ObjInvoiceDateUpdDetails = (clsInvoiceDateUpdDetails) hmFeltConsignmentDetails.get(Integer.toString(i));
                
                //Insert records into Consignment detail table
                resultSetDetail.moveToInsertRow();
                resultSetDetail.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                resultSetDetail.updateString("UPD_NO", getAttribute("UPD_NO").getString());
                resultSetDetail.updateString("UPD_DATE",getAttribute("UPD_DATE").getString());
                resultSetDetail.updateString("UPD_MAIN_CODE",getAttribute("UPD_MAIN_CODE").getString());
                resultSetDetail.updateString("UPD_PARTY_CODE",getAttribute("UPD_PARTY_CODE").getString());
                resultSetDetail.updateString("UPD_INVOICE_DATE_H",getAttribute("UPD_INVOICE_DATE_H").getString());
                resultSetDetail.updateString("SR_NO",ObjInvoiceDateUpdDetails.getAttribute("SR_NO").getString());
                resultSetDetail.updateString("UPD_INVOICE_NO",ObjInvoiceDateUpdDetails.getAttribute("UPD_INVOICE_NO").getString());
                resultSetDetail.updateString("UPD_INVOICE_DATE",ObjInvoiceDateUpdDetails.getAttribute("UPD_INVOICE_DATE").getString());
                resultSetDetail.updateString("UPD_AGENT_SR_NO",ObjInvoiceDateUpdDetails.getAttribute("UPD_AGENT_SR_NO").getString());
                resultSetDetail.updateString("UPD_INVOICE_AMT",ObjInvoiceDateUpdDetails.getAttribute("UPD_INVOICE_AMT").getString());
                resultSetDetail.updateString("UPD_DUE_DATE",ObjInvoiceDateUpdDetails.getAttribute("UPD_DUE_DATE").getString());
                resultSetDetail.updateString("UPD_NEW_DATE",ObjInvoiceDateUpdDetails.getAttribute("UPD_NEW_DATE").getString());
                resultSetDetail.updateString("UPD_NEW_DUE_DATE",ObjInvoiceDateUpdDetails.getAttribute("UPD_NEW_DUE_DATE").getString());
                resultSetDetail.updateInt("CREATED_BY",getAttribute("CREATED_BY").getInt());
                resultSetDetail.updateString("CREATED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
//                resultSetDetail.updateInt("MODIFIED_BY",0);
//                resultSetDetail.updateString("MODIFIED_DATE","0000-00-00");
                resultSetDetail.updateInt("CANCELLED",0);
                resultSetDetail.updateInt("CHANGED",1);
                resultSetDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetail.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
                resultSetDetail.insertRow();
                
                
                //Insert records into Felt Consignment History Table
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO",1);
                resultSetHistory.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                resultSetHistory.updateString("UPD_NO", getAttribute("UPD_NO").getString());
                resultSetHistory.updateString("UPD_DATE",getAttribute("UPD_DATE").getString());
                resultSetHistory.updateString("UPD_PARTY_CODE",getAttribute("UPD_PARTY_CODE").getString());
                resultSetHistory.updateString("UPD_INVOICE_DATE_H",getAttribute("UPD_INVOICE_DATE_H").getString());
                resultSetHistory.updateString("SR_NO",ObjInvoiceDateUpdDetails.getAttribute("SR_NO").getString());
                resultSetHistory.updateString("UPD_INVOICE_NO",ObjInvoiceDateUpdDetails.getAttribute("UPD_INVOICE_NO").getString());
                resultSetHistory.updateString("UPD_INVOICE_DATE",ObjInvoiceDateUpdDetails.getAttribute("UPD_INVOICE_DATE").getString());
                resultSetHistory.updateString("UPD_AGENT_SR_NO",ObjInvoiceDateUpdDetails.getAttribute("UPD_AGENT_SR_NO").getString());
                resultSetHistory.updateString("UPD_INVOICE_AMT",ObjInvoiceDateUpdDetails.getAttribute("UPD_INVOICE_AMT").getString());
                resultSetHistory.updateString("UPD_DUE_DATE",ObjInvoiceDateUpdDetails.getAttribute("UPD_DUE_DATE").getString());
                resultSetHistory.updateString("UPD_NEW_DATE",ObjInvoiceDateUpdDetails.getAttribute("UPD_NEW_DATE").getString());
                resultSetHistory.updateString("UPD_NEW_DUE_DATE",ObjInvoiceDateUpdDetails.getAttribute("UPD_NEW_DUE_DATE").getString());
                resultSetHistory.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
                resultSetHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
                resultSetHistory.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
                resultSetHistory.updateString("CREATED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
//                resultSetHistory.updateInt("MODIFIED_BY",0);
//                resultSetHistory.updateString("MODIFIED_DATE","0000-00-00");
                resultSetHistory.updateBoolean("APPROVED",false);
                resultSetHistory.updateString("APPROVED_DATE","0000-00-00");
                resultSetHistory.updateBoolean("REJECTED",false);
                resultSetHistory.updateString("REJECTED_DATE","0000-00-00");
                resultSetHistory.updateInt("CANCELLED",0);
                resultSetHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
                resultSetHistory.updateInt("CHANGED",1);
                resultSetHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetHistory.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
                resultSetHistory.insertRow();
                
            }
            
            //======== Update the Approval Flow =========
            ApprovalFlow ObjFlow=new ApprovalFlow();
            ObjFlow.CompanyID=EITLERPGLOBAL.gCompanyID;
            ObjFlow.ModuleID=736; //Felt Weaving Entry
            ObjFlow.DocNo=getAttribute("UPD_NO").getString();
            ObjFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="DINESHMILLS.D_SAL_INVDATA_UPD_LC";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=getAttribute("REJECTED_REMARKS").getString();
            ObjFlow.FieldName="UPD_NO";
            
            if(ObjFlow.Status.equals("H")) {
                ObjFlow.Status="A";
                ObjFlow.To=ObjFlow.From;
                ObjFlow.UpdateFlow();
            }
            else {
                if(!ObjFlow.UpdateFlow()) {
                    LastError=ObjFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------
            
            LoadData();
            resultSetDetail.close();
            statementDetail.close();
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
        
        ResultSet  resultSetDetail, resultSetTemp, resultSetDetailHistory, resultSetHistory;
        Statement  statementDetail, statementTemp, statementDetailHistory, statementHistory;
        
        int revisionNo =1;
        try {
            // Production data connection
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  DINESHMILLS.D_SAL_INVDATA_UPD_LC WHERE UPD_NO='' ORDER BY sr_no*1");
            
            // Production data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM DINESHMILLS.D_SAL_INVDATA_UPD_LC_H WHERE UPD_NO='"+getAttribute("UPD_NO").getString()+"'");
            
            resultSetHistory.first();
        
            //delete records from production data table before insert
            statementHistory.execute("DELETE FROM DINESHMILLS.D_SAL_INVDATA_UPD_LC WHERE UPD_NO='"+getAttribute("UPD_NO").getString()+"'");
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM DINESHMILLS.D_SAL_INVDATA_UPD_LC_H WHERE UPD_NO='"+getAttribute("UPD_NO").getString()+"'");
            
            
            
            // Consignment detail connection
            statementDetail = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetDetail = statementDetail.executeQuery("SELECT * FROM  DINESHMILLS.D_SAL_INVDATA_UPD_LC WHERE UPD_NO=''");
            
            
            
            
            
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM DINESHMILLS.D_SAL_INVDATA_UPD_LC_H WHERE UPD_NO='"+(String)getAttribute("UPD_NO").getObj()+"'");
                RevNo++;
            //Now Update records into FELT_CONSIGNMENT tables
            for(int i=1;i<=hmFeltConsignmentDetails.size();i++) {
                clsInvoiceDateUpdDetails ObjInvoiceDateUpdDetails=(clsInvoiceDateUpdDetails) hmFeltConsignmentDetails.get(Integer.toString(i));
                
                //Insert records into Consignment detail table
                resultSetDetail.moveToInsertRow();
                resultSetDetail.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                resultSetDetail.updateString("UPD_NO", getAttribute("UPD_NO").getString());
                resultSetDetail.updateString("UPD_DATE",getAttribute("UPD_DATE").getString());
                resultSetDetail.updateString("UPD_INVOICE_DATE_H",getAttribute("UPD_INVOICE_DATE_H").getString());
                resultSetDetail.updateString("UPD_MAIN_CODE",getAttribute("UPD_MAIN_CODE").getString());
                resultSetDetail.updateString("UPD_PARTY_CODE",getAttribute("UPD_PARTY_CODE").getString());
                resultSetDetail.updateString("SR_NO",ObjInvoiceDateUpdDetails.getAttribute("SR_NO").getString());
                resultSetDetail.updateString("UPD_INVOICE_NO",ObjInvoiceDateUpdDetails.getAttribute("UPD_INVOICE_NO").getString());
                resultSetDetail.updateString("UPD_INVOICE_DATE",ObjInvoiceDateUpdDetails.getAttribute("UPD_INVOICE_DATE").getString());
                resultSetDetail.updateString("UPD_INVOICE_AMT",ObjInvoiceDateUpdDetails.getAttribute("UPD_INVOICE_AMT").getString());
                resultSetDetail.updateString("UPD_AGENT_SR_NO",ObjInvoiceDateUpdDetails.getAttribute("UPD_AGENT_SR_NO").getString());
                resultSetDetail.updateString("UPD_DUE_DATE",ObjInvoiceDateUpdDetails.getAttribute("UPD_DUE_DATE").getString());
                resultSetDetail.updateString("UPD_NEW_DATE",ObjInvoiceDateUpdDetails.getAttribute("UPD_NEW_DATE").getString());
                resultSetDetail.updateString("UPD_NEW_DUE_DATE",ObjInvoiceDateUpdDetails.getAttribute("UPD_NEW_DUE_DATE").getString());
//                resultSetDetail.updateInt("CREATED_BY",getAttribute("CREATED_BY").getInt());
//                resultSetDetail.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                resultSetDetail.updateInt("MODIFIED_BY",getAttribute("MODIFIED_BY").getInt());
                resultSetDetail.updateString("MODIFIED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetDetail.updateInt("CANCELLED",0);
                resultSetDetail.updateInt("CHANGED",1);
                resultSetDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetail.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
                resultSetDetail.insertRow();
                
                
                
                String RevDocNo=(String)getAttribute("UPD_NO").getObj();
                
               
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO",RevNo);
                resultSetHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
                resultSetHistory.updateString("UPD_DATE",getAttribute("UPD_DATE").getString());
                resultSetHistory.updateString("UPD_INVOICE_DATE_H",getAttribute("UPD_INVOICE_DATE_H").getString());
                resultSetHistory.updateString("UPD_NO", getAttribute("UPD_NO").getString());
                resultSetHistory.updateString("UPD_MAIN_CODE",getAttribute("UPD_MAIN_CODE").getString());
                resultSetHistory.updateString("UPD_PARTY_CODE",getAttribute("UPD_PARTY_CODE").getString());
                resultSetHistory.updateString("SR_NO",ObjInvoiceDateUpdDetails.getAttribute("SR_NO").getString());
                resultSetHistory.updateString("UPD_INVOICE_NO",ObjInvoiceDateUpdDetails.getAttribute("UPD_INVOICE_NO").getString());
                resultSetHistory.updateString("UPD_INVOICE_DATE",ObjInvoiceDateUpdDetails.getAttribute("UPD_INVOICE_DATE").getString());
                resultSetHistory.updateString("UPD_AGENT_SR_NO",ObjInvoiceDateUpdDetails.getAttribute("UPD_AGENT_SR_NO").getString());
                resultSetHistory.updateString("UPD_INVOICE_AMT",ObjInvoiceDateUpdDetails.getAttribute("UPD_INVOICE_AMT").getString());
                resultSetHistory.updateString("UPD_DUE_DATE",ObjInvoiceDateUpdDetails.getAttribute("UPD_DUE_DATE").getString());
                resultSetHistory.updateString("UPD_NEW_DATE",ObjInvoiceDateUpdDetails.getAttribute("UPD_NEW_DATE").getString());
                resultSetHistory.updateString("UPD_NEW_DUE_DATE",ObjInvoiceDateUpdDetails.getAttribute("UPD_NEW_DUE_DATE").getString());
                resultSetHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
                resultSetHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
//                resultSetHistory.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
//                resultSetHistory.updateString("CREATED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateInt("MODIFIED_BY",0);
                resultSetHistory.updateString("MODIFIED_DATE","0000-00-00");
                resultSetHistory.updateBoolean("APPROVED",false);
                resultSetHistory.updateString("APPROVED_DATE","0000-00-00");
                resultSetHistory.updateBoolean("REJECTED",false);
                resultSetHistory.updateString("REJECTED_DATE","0000-00-00");
                resultSetHistory.updateInt("CANCELLED",0);
                resultSetHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
                resultSetHistory.updateInt("CHANGED",1);
                resultSetHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetHistory.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
                resultSetHistory.insertRow();
            }
            
            //======== Update the Approval Flow =========
            ApprovalFlow ObjFlow=new ApprovalFlow();
            ObjFlow.CompanyID=EITLERPGLOBAL.gCompanyID;
            ObjFlow.ModuleID=736; //InvoiceData Upd Module
            ObjFlow.DocNo=getAttribute("UPD_NO").getString();
            ObjFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="DINESHMILLS.D_SAL_INVDATA_UPD_LC";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=getAttribute("REJECTED_REMARKS").getString();
            ObjFlow.FieldName="UPD_NO";
            
            if(getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFlow.To=(int)getAttribute("SEND_DOC_TO").getVal();
                ObjFlow.ExplicitSendTo=true;
            }
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE DINESHMILLS.D_SAL_INVDATA_UPD_LC SET REJECTED=0,CHANGED=1 WHERE UPD_NO ='"+getAttribute("UPD_NO").getString()+"'");
                //Remove Old Records from FELT DOCUMET APPROVAL TABLE
               data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=736 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                ObjFlow.IsCreator=true;
            }
            //==========================================//
            
             
            if(ObjFlow.Status.equals("H")) {
                //Do nothing
                if(IsRejected) {
                    ObjFlow.Status="A";
                    ObjFlow.To=ObjFlow.From;
                    ObjFlow.UpdateFlow();
                    
                }
            }
            else {
                if(!ObjFlow.UpdateFlow()) {
                    LastError=ObjFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
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
            strSQL="SELECT COUNT(*) AS COUNT FROM DINESHMILLS.D_SAL_INVDATA_UPD_LC WHERE  UPD_NO='"+consignmentNo +"' AND APPROVED="+1;            
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
              //  strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE MODULE_ID=708 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE MODULE_ID=736 AND USER_ID="+userID+" AND DOC_NO='"+ consignmentNo+"' AND STATUS='W'";
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
            String ConNo=(String)getAttribute("UPD_NO").getObj();
            String strSQL="";
            
            //First check that record is editable
            if(CanDelete(lCompanyID,ConNo,pUserID)) {
                String strQry = "DELETE FROM DINESHMILLS.D_SAL_INVDATA_UPD_LC_H WHERE UPD_NO='"+ConNo+"'";
                data.Execute(strQry);
                strQry = "DELETE FROM DINESHMILLS.D_SAL_INVDATA_UPD_LC WHERE UPD_NO='"+ConNo+"'";
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
    public boolean IsEditable(int pCompanyID,String consignmentNo,int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        try {
            if(HistoryView) {
                return false;
            }
            tmpStmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM DINESHMILLS.D_SAL_INVDATA_UPD_LC WHERE UPD_NO='"+ consignmentNo +"' AND APPROVED=1";
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0){  //Item is Approved
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE MODULE_ID=736 AND USER_ID="+userID+" AND DOC_NO='"+ consignmentNo +"' AND STATUS='W'";
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
    
    public boolean Filter(String pCondition,int pCompanyID) {
      //  jOptionPane.showMessageDialog(null,pCondition);
        Ready=false;
        try {
            String strSql = "SELECT * FROM DINESHMILLS.D_SAL_INVDATA_UPD_LC " + pCondition +"group by UPD_NO";
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
            if(HistoryView) {
                RevNo=resultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",resultSet.getInt("REVISION_NO"));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            setAttribute("COMPANY_ID",resultSet.getInt("COMPANY_ID"));
            setAttribute("UPD_NO",resultSet.getString("UPD_NO"));
            setAttribute("UPD_DATE",resultSet.getDate("UPD_DATE"));
            setAttribute("UPD_MAIN_CODE",resultSet.getString("UPD_MAIN_CODE"));
            setAttribute("UPD_PARTY_CODE",resultSet.getString("UPD_PARTY_CODE"));
            setAttribute("UPD_INVOICE_DATE_H",resultSet.getString("UPD_INVOICE_DATE_H"));
            setAttribute("REJECTED_REMARKS",resultSet.getString("REJECTED_REMARKS"));

            hmFeltConsignmentDetails.clear();
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  DINESHMILLS.D_SAL_INVDATA_UPD_LC WHERE UPD_NO = '"+ resultSet.getString("UPD_NO") +"' ORDER BY SR_NO*1");
            
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
                setAttribute("CANCELLED",resultSetTemp.getInt("CANCELLED"));
                setAttribute("HIERARCHY_ID",resultSetTemp.getInt("HIERARCHY_ID"));
                setAttribute("REJECTED_REMARKS",resultSetTemp.getString("REJECTED_REMARKS"));
                
                clsInvoiceDateUpdDetails ObjInvoiceDateUpdDetails = new clsInvoiceDateUpdDetails();
                
                ObjInvoiceDateUpdDetails.setAttribute("SR_NO",resultSetTemp.getString("SR_NO"));
                ObjInvoiceDateUpdDetails.setAttribute("UPD_INVOICE_NO",resultSetTemp.getString("UPD_INVOICE_NO"));
                ObjInvoiceDateUpdDetails.setAttribute("UPD_INVOICE_DATE",resultSetTemp.getString("UPD_INVOICE_DATE"));
                ObjInvoiceDateUpdDetails.setAttribute("UPD_AGENT_SR_NO",resultSetTemp.getString("UPD_AGENT_SR_NO"));
                ObjInvoiceDateUpdDetails.setAttribute("UPD_INVOICE_AMT",resultSetTemp.getString("UPD_INVOICE_AMT"));
                ObjInvoiceDateUpdDetails.setAttribute("UPD_DUE_DATE",resultSetTemp.getString("UPD_DUE_DATE"));
                ObjInvoiceDateUpdDetails.setAttribute("UPD_NEW_DATE",resultSetTemp.getString("UPD_NEW_DATE"));
                ObjInvoiceDateUpdDetails.setAttribute("UPD_NEW_DUE_DATE",resultSetTemp.getString("UPD_NEW_DUE_DATE"));
                
                hmFeltConsignmentDetails.put(Integer.toString(serialNoCounter),ObjInvoiceDateUpdDetails);
            }
          //  resultSet.close();
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
            //hmFeltConsignmentDetails.clear();
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  DINESHMILLS.D_SAL_INVDATA_UPD_LC_H WHERE UPD_NO='"+pDocNo+"' AND REVISION_NO='"+ resultSet.getInt("REVISION_NO") +"' ORDER BY REVISION_NO");
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
                setAttribute("UPD_NO",resultSetTemp.getString("UPD_NO"));
                //          setAttribute("PRODUCTION_FORM_NO",resultSetTemp.getString("PROD_FORM_NO"));
                setAttribute("UPDATED_BY",resultSetTemp.getInt("UPDATED_BY"));
                setAttribute("ENTRY_DATE",resultSetTemp.getString("ENTRY_DATE"));
                setAttribute("HIERARCHY_ID",resultSetTemp.getInt("HIERARCHY_ID"));
                
                clsInvoiceDateUpdDetails ObjInvoiceDateUpdDetails = new clsInvoiceDateUpdDetails();
                
                ObjInvoiceDateUpdDetails.setAttribute("SR_NO",resultSetTemp.getString("SR_NO"));
                ObjInvoiceDateUpdDetails.setAttribute("UPD_INVOICE_NO",resultSetTemp.getString("UPD_INVOICE_NO"));
                ObjInvoiceDateUpdDetails.setAttribute("UPD_INVOICE_DATE",resultSetTemp.getString("UPD_INVOICE_DATE"));
                ObjInvoiceDateUpdDetails.setAttribute("UPD_AGENT_SR_NO",resultSetTemp.getString("UPD_AGENT_SR_NO"));
                ObjInvoiceDateUpdDetails.setAttribute("UPD_INVOICE_AMT",resultSetTemp.getString("UPD_INVOICE_AMT"));
                ObjInvoiceDateUpdDetails.setAttribute("UPD_DUE_DATE",resultSetTemp.getString("UPD_DUE_DATE"));
                ObjInvoiceDateUpdDetails.setAttribute("UPD_NEW_DATE",resultSetTemp.getString("UPD_NEW_DATE"));
                ObjInvoiceDateUpdDetails.setAttribute("UPD_NEW_DUE_DATE",resultSetTemp.getString("UPD_NEW_DUE_DATE"));
                
                hmFeltConsignmentDetails.put(Integer.toString(serialNoCounter),ObjInvoiceDateUpdDetails);
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
    
    public static HashMap getHistoryList(int CompanyID,String Docno) {
        HashMap hmHistoryList=new HashMap();
        Statement stTmp;
        ResultSet rsTmp;
        try {
            stTmp=connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp=stTmp.executeQuery("SELECT * FROM DINESHMILLS.D_SAL_INVDATA_UPD_LC_H WHERE UPD_NO='"+Docno+"' GROUP BY REVISION_NO");
            
            while(rsTmp.next()) {
                clsInvoiceDateUpd ObjInvoiceDateUpd = new clsInvoiceDateUpd();
                
                ObjInvoiceDateUpd.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjInvoiceDateUpd.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjInvoiceDateUpd.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjInvoiceDateUpd.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjInvoiceDateUpd.setAttribute("REJECTED_REMARKS",rsTmp.getString("REJECTED_REMARKS"));
                hmHistoryList.put(Integer.toString(hmHistoryList.size()+1),ObjInvoiceDateUpd);
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
            resultSet=statement.executeQuery("SELECT DISTINCT FROM DINESHMILLS.D_SAL_INVDATA_UPD_LC_H WHERE CON_DOC_DATE='"+ pProductionDate+"' AND UPD_NO ='"+pDocNo+"'");
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
                strSQL="SELECT DISTINCT UPD_NO,RECEIVED_DATE,UPD_DATE,UPD_PARTY_CODE FROM DINESHMILLS.D_SAL_INVDATA_UPD_LC,D_COM_DOC_DATA WHERE UPD_NO=DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=736 AND CANCELLED=0 ORDER BY RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT DISTINCT UPD_NO,RECEIVED_DATE,UPD_DATE,UPD_PARTY_CODE FROM DINESHMILLS.D_SAL_INVDATA_UPD_LC,D_COM_DOC_DATA WHERE UPD_NO=DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=736 AND CANCELLED=0 ORDER BY UPD_NO,UPD_PARTY_CODE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT DISTINCT UPD_NO,RECEIVED_DATE,UPD_DATE,UPD_PARTY_CODE FROM DINESHMILLS.D_SAL_INVDATA_UPD_LC,D_COM_DOC_DATA WHERE UPD_NO=DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=736 AND CANCELLED=0 ORDER BY UPD_NO";
            }
            
            ResultSet rsTmp=data.getConn().createStatement().executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp.next()) {
                
                Counter=Counter+1;
                clsInvoiceDateUpd ObjDoc=new clsInvoiceDateUpd();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO",rsTmp.getString("UPD_NO"));
                ObjDoc.setAttribute("DOC_DATE",rsTmp.getString("UPD_DATE"));
                ObjDoc.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                ObjDoc.setAttribute("UPD_PARTY_CODE",rsTmp.getString("UPD_PARTY_CODE"));
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
    public String getProductCode(String pPieceNo) {
        return data.getStringValueFromDB("SELECT PRODUCT_CODE FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
        
    }
    
    public String getLength(String pPieceNo) {
        return data.getStringValueFromDB("SELECT LNGTH FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
        
    }
    public String getWidth(String pPieceNo) {
        return data.getStringValueFromDB("SELECT WIDTH FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
        
    }
    public String getGSQ(String pPieceNo) {
        return data.getStringValueFromDB("SELECT GSQ FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
        
    }
    
    public String getAgreedDate(String pPieceNo) {
        return data.getStringValueFromDB("SELECT AGREED_DATE FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");
        
    }
    
    
    // Updates Weaving Date in Order Master MasterTable To confirm that Weaving has completed
    // private void updateWeavingDate(String documentNo){
    //   data.Execute("UPDATE PRODUCTION.FELT_ORDER_MASTER,PRODUCTION.FELT_PROD_DATA SET WVG_DATE=PROD_DATE WHERE PIECE_NO+0=PROD_PIECE_NO+0 AND PARTY_CD=PROD_PARTY_CODE AND PROD_DEPT='WEAVING' AND PROD_DOC_NO='"+documentNo+"' AND APPROVED=1");
    
    // }
    
    // private void updateWeavingcounter(String documentNo){
    //   data.Execute("UPDATE PRODUCTION.FELT_PROD_DATA  SET WEAVE_DIFF_DAYS =DATEDIFF(PROD_DATE,WEAVE_DATE)  WHERE PROD_DEPT='WEAVING'  AND PROD_DOC_NO='"+documentNo+"'");
    
    // }
    
    // checks piece no already exist in db
    public boolean checkPieceNoInDB(String pPieceNo){
        int count=data.getIntValueFromDB("SELECT COUNT(CON_PIECE_NO) FROM DINESHMILLS.D_SAL_INVDATA_UPD_LC WHERE CON_PIECE_NO='"+pPieceNo+"' AND CON_DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND CON_DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
        if(count>0) return true;
        else return false;
    }
    
    public boolean checkPieceNoInDB(String pPieceNo, String pProdDate){
        int count=data.getIntValueFromDB("SELECT COUNT(CON_PIECE_NO) FROM DINESHMILLS.D_SAL_INVDATA_UPD_LC WHERE CON_PIECE_NO='"+pPieceNo+"' AND CON_DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND CON_DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
        if(count>=1){
            int counter=0;
            try{
                Connection Conn=data.getConn();
                Statement stTmp=Conn.createStatement();
                ResultSet rsTmp=stTmp.executeQuery("SELECT CON_DATE FROM DINESHMILLS.D_SAL_INVDATA_UPD_LC WHERE CON_PIECE_NO='"+pPieceNo+"' AND CON_DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND CON_DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
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
        int count=data.getIntValueFromDB("SELECT COUNT(CON_DOC_DATE) FROM DINESHMILLS.D_SAL_INVDATA_UPD_LC WHERE CON_DOC_DATE='"+pProdDate+"'");
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
            strSQL="SELECT LAST_USED_NO,PADDING_BY,NO_LENGTH,PREFIX_CHARS,SUFFIX_CHARS FROM D_COM_FIRSTFREE WHERE COMPANY_ID=2 AND MODULE_ID=736 AND FIRSTFREE_NO=190";
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
                    data.Execute("UPDATE D_COM_FIRSTFREE SET LAST_USED_NO='"+strNewNo.trim()+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=2 AND MODULE_ID=736 AND FIRSTFREE_NO=190");
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
     public static HashMap getCreditNoteList() {
        HashMap List= new HashMap();
        try{
            Connection Conn=data.getConn();
            Statement stTmp=Conn.createStatement();
            int counter=1;
            ResultSet rsTmp=stTmp.executeQuery(" SELECT PARA_CODE,PARA_EXT1,PARA_EXT2 FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID = 'INVOICEDATA_UPDATE' ORDER BY PARA_CODE");
            while(rsTmp.next()){
                ComboData aData=new ComboData();
                aData.Code=rsTmp.getInt("PARA_CODE");
                aData.Text=rsTmp.getString("PARA_EXT1")+rsTmp.getString("PARA_EXT2");
                
                List.put(new Integer(counter++), aData);
            }
        }catch(SQLException e){
      //      LastError = e.getMessage();
            e.printStackTrace();
        }
        return List;
    } 
}


