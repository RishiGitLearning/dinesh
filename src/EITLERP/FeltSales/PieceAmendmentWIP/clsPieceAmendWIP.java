/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.PieceAmendmentWIP;

import EITLERP.ComboData;
import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.common.JavaMail;
//import EITLERP.FeltSales.common.Order_No_Conversion;
import EITLERP.JavaMail.JMail;
import EITLERP.Production.clsFeltProductionApprovalFlow;
import EITLERP.SelectFirstFree;
import EITLERP.Variant;
import EITLERP.clsFirstFree;
import EITLERP.clsHierarchy;
import EITLERP.clsSales_Party;
import EITLERP.clsUser;
import EITLERP.data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import javax.swing.JOptionPane;

/**
 *
 * @author  DAXESH PRAJAPATI
 *
 */
public class clsPieceAmendWIP {
    public String LastError="";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet,rsHeader;
    //private ResultSet rsResultSet,
    
    public HashMap props;
    public boolean Ready = false;
    //Felt Order Collection
    public HashMap hmPieceAmendApprovalDetail;
    
    //History Related properties
    public boolean HistoryView=false;
    private String historyAmendDate="";
    private String historyAmendID="";
    private static int ModuleID = 774;
    
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
    public clsPieceAmendWIP() {
        LastError = "";
        props=new HashMap();
        
        
        props.put("PIECE_AMEND_NO", new Variant(""));
        props.put("PIECE_AMEND_NO", new Variant(""));
        props.put("PIECE_AMEND_DATE", new Variant(""));
        
        props.put("MM_PARTY_CODE", new Variant(""));
        props.put("MM_MACHINE_NO", new Variant(""));
        props.put("MM_MACHINE_POSITION", new Variant(""));
        
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("UPDATED_BY",new Variant(0));
        props.put("UPDATED_DATE",new Variant(""));
        props.put("PREFIX",new Variant(""));
        
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
        props.put("APPROVED",new Variant(0));
        
        hmPieceAmendApprovalDetail=new HashMap();
    }
    
    public boolean LoadData() {
        Ready=false;
        try {
            connection = data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_AMENDMENT_HEADER_WIP ORDER BY PIECE_AMEND_NO");
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
        ResultSet  resultSetTemp,resultSetHistory,rsHeader,rsHeaderH;
        Statement  statementTemp, statementHistory,stHeader,stHeaderH;
        try {
            // Felt Order Updation data connection
            connection = data.getConn();
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_AMENDMENT_DETAIL_WIP WHERE PIECE_AMEND_NO=''");
            
            // Felt order Updation data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_AMENDMENT_DETAIL_WIP_H WHERE PIECE_AMEND_NO=''");
            
            stHeader=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHeader=stHeader.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_AMENDMENT_HEADER_WIP WHERE PIECE_AMEND_NO=''");
            
            stHeaderH=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHeaderH=stHeaderH.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_AMENDMENT_HEADER_WIP_H WHERE PIECE_AMEND_NO=''");

            //setAttribute("PIECE_AMEND_NO",);
                
            rsHeader.first();
            rsHeader.moveToInsertRow();
            //PIECE_AMEND_NO
            
            rsHeader.updateString("PIECE_AMEND_NO",getAttribute("PIECE_AMEND_NO").getString());
            rsHeader.updateString("PIECE_AMEND_DATE",getAttribute("PIECE_AMEND_DATE").getString());
            rsHeader.updateString("MM_PARTY_CODE",getAttribute("MM_PARTY_CODE").getString());
            rsHeader.updateString("MM_MACHINE_NO",getAttribute("MM_MACHINE_NO").getString());
            rsHeader.updateString("MM_MACHINE_POSITION",getAttribute("MM_MACHINE_POSITION").getString());
            rsHeader.updateString("ENTRY_REASON",getAttribute("ENTRY_REASON").getString());
            //ENTRY_DOCNO
            rsHeader.updateString("ENTRY_DOCNO",getAttribute("ENTRY_DOCNO").getString());
            
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
            rsHeader.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateTimeDB());
            rsHeader.updateString("UPDATED_BY",EITLERPGLOBAL.gNewUserID+"");
            rsHeader.updateString("UPDATED_DATE",EITLERPGLOBAL.getCurrentDateTimeDB());
            rsHeader.insertRow();
            
            
            rsHeaderH.first();
            rsHeaderH.moveToInsertRow();
            
            rsHeaderH.updateInt("REVISION_NO",1);
            
            rsHeaderH.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHeaderH.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHeaderH.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateString("APPROVER_REMARKS",getAttribute("APPROVER_REMARKS").getString());
            
            
            rsHeaderH.updateString("PIECE_AMEND_NO",getAttribute("PIECE_AMEND_NO").getString());
            rsHeaderH.updateString("PIECE_AMEND_DATE",getAttribute("PIECE_AMEND_DATE").getString());
         
            rsHeaderH.updateString("MM_PARTY_CODE",getAttribute("MM_PARTY_CODE").getString());
            rsHeaderH.updateString("MM_MACHINE_NO",getAttribute("MM_MACHINE_NO").getString());
            rsHeaderH.updateString("MM_MACHINE_POSITION",getAttribute("MM_MACHINE_POSITION").getString());
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
            rsHeaderH.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateTimeDB());
            
            ResultSet rsTmp=data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");
            
            rsHeaderH.updateString("FROM_IP",""+str_split[1]);
           
            rsHeaderH.insertRow(); 
            
            //Now Insert records into FELT_CN_TEMP_HEADER & History tables
            for(int i=1;i<=hmPieceAmendApprovalDetail.size();i++) {
                clsPieceAmendWIPDetail ObjFeltSalesOrderDetails = (clsPieceAmendWIPDetail) hmPieceAmendApprovalDetail.get(Integer.toString(i));
             
                String Doc_No = data.getStringValueFromDB("SELECT H.PIECE_AMEND_NO FROM PRODUCTION.FELT_SALES_PIECE_AMENDMENT_HEADER_WIP H,PRODUCTION.FELT_SALES_PIECE_AMENDMENT_DETAIL_WIP D WHERE H.PIECE_AMEND_NO=D.PIECE_AMEND_NO AND H.APPROVED=0 AND H.CANCELED=0 AND D.PIECE_NO='"+ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString()+"'");
                if(Doc_No.equals(""))
                {
                        //Insert records into Felt order Amendment table
                        resultSetTemp.moveToInsertRow();

                        resultSetTemp.updateString("PIECE_AMEND_NO", getAttribute("PIECE_AMEND_NO").getString());
                        //
                        resultSetTemp.updateString("PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                        resultSetTemp.updateString("PROD_REMARKS", ObjFeltSalesOrderDetails.getAttribute("PROD_REMARKS").getString());
                        resultSetTemp.updateString("LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                        resultSetTemp.updateString("LENGTH_UPDATED", ObjFeltSalesOrderDetails.getAttribute("LENGTH_UPDATED").getString());
                        resultSetTemp.updateString("WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                        resultSetTemp.updateString("WIDTH_UPDATED", ObjFeltSalesOrderDetails.getAttribute("WIDTH_UPDATED").getString());
                        resultSetTemp.updateString("GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                        resultSetTemp.updateString("GSM_UPDATED", ObjFeltSalesOrderDetails.getAttribute("GSM_UPDATED").getString());
                        resultSetTemp.updateString("STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                        resultSetTemp.updateString("STYLE_UPDATED", ObjFeltSalesOrderDetails.getAttribute("STYLE_UPDATED").getString());
                        resultSetTemp.updateString("PRODUCTCODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCTCODE").getString());
                        resultSetTemp.updateString("PRODUCTCODE_UPDATED", ObjFeltSalesOrderDetails.getAttribute("PRODUCTCODE_UPDATED").getString());
                        resultSetTemp.updateString("FLET_GROUP", ObjFeltSalesOrderDetails.getAttribute("FLET_GROUP").getString());
                        resultSetTemp.updateString("FLET_GROUP_UPDATED", ObjFeltSalesOrderDetails.getAttribute("FLET_GROUP_UPDATED").getString());
                        resultSetTemp.updateString("WEIGHT", ObjFeltSalesOrderDetails.getAttribute("WEIGHT").getString());
                        resultSetTemp.updateString("WEIGHT_UPDATED", ObjFeltSalesOrderDetails.getAttribute("WEIGHT_UPDATED").getString());
                        resultSetTemp.updateString("SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQMTR").getString());
                        resultSetTemp.updateString("SQMTR_UPDATED", ObjFeltSalesOrderDetails.getAttribute("SQMTR_UPDATED").getString());
                        resultSetTemp.updateString("PIECE_STAGE", ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString());
                        resultSetTemp.updateString("ACTION_TAKEN", ObjFeltSalesOrderDetails.getAttribute("ACTION_TAKEN").getString());
                        resultSetTemp.updateString("REMARKS", ObjFeltSalesOrderDetails.getAttribute("REMARKS").getString());
                        resultSetTemp.updateInt("CHANGE_POSIBILITY", ObjFeltSalesOrderDetails.getAttribute("CHANGE_POSIBILITY").getInt());
                        resultSetTemp.updateInt("DELINK", ObjFeltSalesOrderDetails.getAttribute("DELINK").getInt());
                        resultSetTemp.updateInt("ACTUAL_CHANGE", ObjFeltSalesOrderDetails.getAttribute("ACTUAL_CHANGE").getInt());
                        
                        resultSetTemp.updateString("EXPECTED_DISPATCH", ObjFeltSalesOrderDetails.getAttribute("EXPECTED_DISPATCH").getString());
                        
                        resultSetTemp.updateString("OBSOLETE_UPN_ASSIGN_STATUS", ObjFeltSalesOrderDetails.getAttribute("OBSOLETE_UPN_ASSIGN_STATUS").getString());
                        resultSetTemp.updateString("BASE_GSM", ObjFeltSalesOrderDetails.getAttribute("BASE_GSM").getString());
                        resultSetTemp.updateString("WEB_GSM", ObjFeltSalesOrderDetails.getAttribute("WEB_GSM").getString());
                        resultSetTemp.updateString("WEAVE", ObjFeltSalesOrderDetails.getAttribute("WEAVE").getString());
                        resultSetTemp.updateString("CFM_TARGETTED", ObjFeltSalesOrderDetails.getAttribute("CFM_TARGETTED").getString());
                        resultSetTemp.updateString("LOOM_NO", ObjFeltSalesOrderDetails.getAttribute("LOOM_NO").getString());
                        resultSetTemp.updateString("PAPER_PROD_TYPE", ObjFeltSalesOrderDetails.getAttribute("PAPER_PROD_TYPE").getString());
                        resultSetTemp.updateString("UNMAPPED_REASON", ObjFeltSalesOrderDetails.getAttribute("UNMAPPED_REASON").getString());
                        resultSetTemp.updateString("SCRAP_REASON", ObjFeltSalesOrderDetails.getAttribute("SCRAP_REASON").getString());
                        
                        resultSetTemp.updateInt("CREATED_BY",EITLERPGLOBAL.gNewUserID);
                        resultSetTemp.updateString("CREATED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));

                        resultSetTemp.insertRow();

                        //Insert records into Felt Order Amendment History Table
                        resultSetHistory.moveToInsertRow();
                        resultSetHistory.updateInt("REVISION_NO",1);
                        resultSetHistory.updateInt("SR_NO",i);
                        resultSetHistory.updateString("PIECE_AMEND_NO", getAttribute("PIECE_AMEND_NO").getString());
                       
                        resultSetHistory.updateString("PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                        resultSetHistory.updateString("PROD_REMARKS", ObjFeltSalesOrderDetails.getAttribute("PROD_REMARKS").getString());
                        resultSetHistory.updateString("LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                        resultSetHistory.updateString("LENGTH_UPDATED", ObjFeltSalesOrderDetails.getAttribute("LENGTH_UPDATED").getString());
                        resultSetHistory.updateString("WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                        resultSetHistory.updateString("WIDTH_UPDATED", ObjFeltSalesOrderDetails.getAttribute("WIDTH_UPDATED").getString());
                        resultSetHistory.updateString("GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                        resultSetHistory.updateString("GSM_UPDATED", ObjFeltSalesOrderDetails.getAttribute("GSM_UPDATED").getString());
                        resultSetHistory.updateString("STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                        resultSetHistory.updateString("STYLE_UPDATED", ObjFeltSalesOrderDetails.getAttribute("STYLE_UPDATED").getString());
                        resultSetHistory.updateString("PRODUCTCODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCTCODE").getString());
                        resultSetHistory.updateString("PRODUCTCODE_UPDATED", ObjFeltSalesOrderDetails.getAttribute("PRODUCTCODE_UPDATED").getString());
                        resultSetHistory.updateString("FLET_GROUP", ObjFeltSalesOrderDetails.getAttribute("FLET_GROUP").getString());
                        resultSetHistory.updateString("FLET_GROUP_UPDATED", ObjFeltSalesOrderDetails.getAttribute("FLET_GROUP_UPDATED").getString());
                        resultSetHistory.updateString("WEIGHT", ObjFeltSalesOrderDetails.getAttribute("WEIGHT").getString());
                        resultSetHistory.updateString("WEIGHT_UPDATED", ObjFeltSalesOrderDetails.getAttribute("WEIGHT_UPDATED").getString());
                        resultSetHistory.updateString("SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQMTR").getString());
                        resultSetHistory.updateString("SQMTR_UPDATED", ObjFeltSalesOrderDetails.getAttribute("SQMTR_UPDATED").getString());
                        resultSetHistory.updateString("PIECE_STAGE", ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString());
                        resultSetHistory.updateString("ACTION_TAKEN", ObjFeltSalesOrderDetails.getAttribute("ACTION_TAKEN").getString());
                        resultSetHistory.updateString("REMARKS", ObjFeltSalesOrderDetails.getAttribute("REMARKS").getString());
                        resultSetHistory.updateInt("CHANGE_POSIBILITY", ObjFeltSalesOrderDetails.getAttribute("CHANGE_POSIBILITY").getInt());
                        resultSetHistory.updateInt("DELINK", ObjFeltSalesOrderDetails.getAttribute("DELINK").getInt());
                        resultSetHistory.updateInt("ACTUAL_CHANGE", ObjFeltSalesOrderDetails.getAttribute("ACTUAL_CHANGE").getInt());
                        resultSetHistory.updateString("EXPECTED_DISPATCH", ObjFeltSalesOrderDetails.getAttribute("EXPECTED_DISPATCH").getString());
                        
                        resultSetHistory.updateString("OBSOLETE_UPN_ASSIGN_STATUS", ObjFeltSalesOrderDetails.getAttribute("OBSOLETE_UPN_ASSIGN_STATUS").getString());
                        resultSetHistory.updateString("BASE_GSM", ObjFeltSalesOrderDetails.getAttribute("BASE_GSM").getString());
                        resultSetHistory.updateString("WEB_GSM", ObjFeltSalesOrderDetails.getAttribute("WEB_GSM").getString());
                        resultSetHistory.updateString("WEAVE", ObjFeltSalesOrderDetails.getAttribute("WEAVE").getString());
                        resultSetHistory.updateString("CFM_TARGETTED", ObjFeltSalesOrderDetails.getAttribute("CFM_TARGETTED").getString());
                        resultSetHistory.updateString("LOOM_NO", ObjFeltSalesOrderDetails.getAttribute("LOOM_NO").getString());
                        resultSetHistory.updateString("PAPER_PROD_TYPE", ObjFeltSalesOrderDetails.getAttribute("PAPER_PROD_TYPE").getString());
                        resultSetHistory.updateString("UNMAPPED_REASON", ObjFeltSalesOrderDetails.getAttribute("UNMAPPED_REASON").getString());
                        resultSetHistory.updateString("SCRAP_REASON", ObjFeltSalesOrderDetails.getAttribute("SCRAP_REASON").getString());
                        
                        resultSetHistory.updateInt("CREATED_BY",EITLERPGLOBAL.gNewUserID);
                        resultSetHistory.updateString("CREATED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                        resultSetHistory.updateInt("MODIFIED_BY",0);
                        resultSetHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB("0000-00-00"));
                        resultSetHistory.insertRow();
                }

            }
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=ModuleID; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo=getAttribute("PIECE_AMEND_NO").getString()+"";
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.FELT_SALES_PIECE_AMENDMENT_HEADER_WIP";
            ObjFeltProductionApprovalFlow.IsCreator=true;
            ObjFeltProductionApprovalFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks=getAttribute("REJECTED_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName="PIECE_AMEND_NO";
            
            //JOptionPane.showMessageDialog(null, "TO : "+(int)getAttribute("TO").getVal()+", Approval Status = "+(String)getAttribute("APPROVAL_STATUS").getObj());
            if("A".equals((String)getAttribute("APPROVAL_STATUS").getObj()))
            {   
//                    String Subject = "Felt Order Pending Document : "+getAttribute("PIECE_AMEND_NO").getString();
//                    String Message = "Document No : "+getAttribute("PIECE_AMEND_NO").getString()+" is added in your PENDING DOCUMENT"
//                             + "\n\n\n\n SDML-ERP : http://200.0.0.230:8080/SDMLERP";
//                 
//                    String send_to = clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, (int)getAttribute("TO").getVal());
//
//                    System.out.println("USERID : "+(int)getAttribute("TO").getVal()+", send_to : "+send_to);
//                    try{
//                        JMail.SendMail(EITLERPGLOBAL.SMTPHostIP, "sdmlerp@dineshmills.com", send_to, Message, Subject, "sdmlerp@dineshmills.com");
//                    }catch(Exception e)
//                    {
//                        e.printStackTrace();
//                    }
            }
            
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
//            String msg = e.getMessage();
//            try {
//                JavaMail.SendMail("daxesh@dineshmills.com", "Error on Amend : "+msg,"Machine Amend : ", "sdmlerp@dineshmills.com");
//            } catch (Exception ex) {
//                //Logger.getLogger(frmmachinesurveyAmend.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            LastError= e.getMessage();
//            e.printStackTrace();
            return false;
        }
    }
    
    //Updates current record
    public boolean Update() {
        
        ResultSet  resultSetTemp,resultSetHistory,rsHeader,rsHeaderH,rsRegister;
        Statement  statementTemp, statementHistory,stHeader,stHeaderH,stRegister;
        int revisionNo;
        try {
            // Production data connection
           
            // Production data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            //Get the Maximum Revision No in History Table.
            resultSetHistory=statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_SALES_PIECE_AMENDMENT_DETAIL_WIP_H WHERE PIECE_AMEND_NO='"+getAttribute("PIECE_AMEND_NO").getString()+"'");
            resultSetHistory.first();
            if(resultSetHistory.getRow()>0){
                revisionNo=resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from production data table before insert
            //statementHistory.execute("DELETE FROM PRODUCTION.FELT_SALES_PIECE_AMENDMENT_DETAIL_WIP WHERE PIECE_AMEND_NO='"+getAttribute("PIECE_AMEND_NO").getString()+"'");
            
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_AMENDMENT_DETAIL_WIP_H WHERE PIECE_AMEND_NO=''");
            
            stHeaderH=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHeaderH=stHeaderH.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_AMENDMENT_HEADER_WIP_H WHERE PIECE_AMEND_NO=''");
         
            resultSet.updateString("PIECE_AMEND_NO",getAttribute("PIECE_AMEND_NO").getString());
            resultSet.updateString("PIECE_AMEND_DATE",(String)getAttribute("PIECE_AMEND_DATE").getString());
            resultSet.updateString("MM_PARTY_CODE",getAttribute("MM_PARTY_CODE").getString());
            resultSet.updateString("MM_MACHINE_NO",getAttribute("MM_MACHINE_NO").getString());
            resultSet.updateString("MM_MACHINE_POSITION",getAttribute("MM_MACHINE_POSITION").getString());
            
            resultSet.updateString("ENTRY_REASON",getAttribute("ENTRY_REASON").getString());
            
            resultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            resultSet.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            resultSet.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB((String)getAttribute("CREATED_DATE").getObj()));
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
            resultSet.updateString("UPDATED_BY",EITLERPGLOBAL.gNewUserID+"");
            resultSet.updateString("UPDATED_DATE",EITLERPGLOBAL.getCurrentDateTimeDB());
            
            try{
                    resultSet.updateRow();
            }catch(Exception e)
            {
                System.out.println("Header Updation Failed : "+e.getMessage());
                e.printStackTrace();
            }
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_SALES_PIECE_AMENDMENT_HEADER_WIP_H WHERE PIECE_AMEND_NO='"+getAttribute("PIECE_AMEND_NO").getString()+"'");
            
            RevNo++;
           
            rsHeaderH.moveToInsertRow();
            
            rsHeaderH.updateString("REVISION_NO",RevNo+"");
            rsHeaderH.updateString("PIECE_AMEND_NO",getAttribute("PIECE_AMEND_NO").getString()+"");
            rsHeaderH.updateString("PIECE_AMEND_DATE",getAttribute("PIECE_AMEND_DATE").getString());
            
            rsHeaderH.updateString("MM_PARTY_CODE",getAttribute("MM_PARTY_CODE").getString());
            rsHeaderH.updateString("MM_MACHINE_NO",getAttribute("MM_MACHINE_NO").getString());
            rsHeaderH.updateString("MM_MACHINE_POSITION",getAttribute("MM_MACHINE_POSITION").getString());
            
            rsHeaderH.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHeaderH.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsHeaderH.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateTimeDB());
            rsHeaderH.updateString("UPDATED_BY",EITLERPGLOBAL.gNewUserID+"");
            rsHeaderH.updateString("UPDATED_DATE",EITLERPGLOBAL.getCurrentDateTimeDB());
            rsHeaderH.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            rsHeaderH.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHeaderH.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHeaderH.updateString("APPROVER_REMARKS",getAttribute("APPROVER_REMARKS").getString());
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
            String OrderNo=getAttribute("PIECE_AMEND_NO").getString()+"";
            data.Execute("DELETE FROM PRODUCTION.FELT_SALES_PIECE_AMENDMENT_DETAIL_WIP WHERE PIECE_AMEND_NO='"+OrderNo+"'");
            
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_AMENDMENT_DETAIL_WIP WHERE PIECE_AMEND_NO=''");
             
            String mail_pieces = "";
            String ACTUAL_CHANGE_pieces="",DELINK_pieces="";
            boolean insert_delink_flag = false;
            boolean no_change_flag = false;
            for(int i=1;i<=hmPieceAmendApprovalDetail.size();i++) {
                clsPieceAmendWIPDetail ObjFeltSalesOrderDetails = (clsPieceAmendWIPDetail) hmPieceAmendApprovalDetail.get(Integer.toString(i));
               
                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateInt("SR_NO",i);
                resultSetTemp.updateString("PIECE_AMEND_NO", getAttribute("PIECE_AMEND_NO").getString()+"");
                
                resultSetTemp.updateString("PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                resultSetTemp.updateString("PROD_REMARKS", ObjFeltSalesOrderDetails.getAttribute("PROD_REMARKS").getString());
                resultSetTemp.updateString("LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                resultSetTemp.updateString("LENGTH_UPDATED", ObjFeltSalesOrderDetails.getAttribute("LENGTH_UPDATED").getString());
                resultSetTemp.updateString("WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                resultSetTemp.updateString("WIDTH_UPDATED", ObjFeltSalesOrderDetails.getAttribute("WIDTH_UPDATED").getString());
                resultSetTemp.updateString("GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                resultSetTemp.updateString("GSM_UPDATED", ObjFeltSalesOrderDetails.getAttribute("GSM_UPDATED").getString());
                
                resultSetTemp.updateString("PRODUCTCODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCTCODE").getString());
                resultSetTemp.updateString("PRODUCTCODE_UPDATED", ObjFeltSalesOrderDetails.getAttribute("PRODUCTCODE_UPDATED").getString());
                
                resultSetTemp.updateString("STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                resultSetTemp.updateString("STYLE_UPDATED", ObjFeltSalesOrderDetails.getAttribute("STYLE_UPDATED").getString());
                resultSetTemp.updateString("FLET_GROUP", ObjFeltSalesOrderDetails.getAttribute("FLET_GROUP").getString());
                resultSetTemp.updateString("FLET_GROUP_UPDATED", ObjFeltSalesOrderDetails.getAttribute("FLET_GROUP_UPDATED").getString());
                resultSetTemp.updateString("WEIGHT", ObjFeltSalesOrderDetails.getAttribute("WEIGHT").getString());
                resultSetTemp.updateString("WEIGHT_UPDATED", ObjFeltSalesOrderDetails.getAttribute("WEIGHT_UPDATED").getString());
                resultSetTemp.updateString("SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQMTR").getString());
                resultSetTemp.updateString("SQMTR_UPDATED", ObjFeltSalesOrderDetails.getAttribute("SQMTR_UPDATED").getString());
                resultSetTemp.updateString("PIECE_STAGE", ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString());
                resultSetTemp.updateString("ACTION_TAKEN", ObjFeltSalesOrderDetails.getAttribute("ACTION_TAKEN").getString());
                resultSetTemp.updateString("REMARKS", ObjFeltSalesOrderDetails.getAttribute("REMARKS").getString());
                resultSetTemp.updateInt("CHANGE_POSIBILITY", ObjFeltSalesOrderDetails.getAttribute("CHANGE_POSIBILITY").getInt());
                resultSetTemp.updateInt("DELINK", ObjFeltSalesOrderDetails.getAttribute("DELINK").getInt());
                resultSetTemp.updateInt("ACTUAL_CHANGE", ObjFeltSalesOrderDetails.getAttribute("ACTUAL_CHANGE").getInt());
                        
                resultSetTemp.updateString("EXPECTED_DISPATCH", ObjFeltSalesOrderDetails.getAttribute("EXPECTED_DISPATCH").getString());
                
                resultSetTemp.updateString("OBSOLETE_UPN_ASSIGN_STATUS", ObjFeltSalesOrderDetails.getAttribute("OBSOLETE_UPN_ASSIGN_STATUS").getString());
                resultSetTemp.updateString("BASE_GSM", ObjFeltSalesOrderDetails.getAttribute("BASE_GSM").getString());
                resultSetTemp.updateString("WEB_GSM", ObjFeltSalesOrderDetails.getAttribute("WEB_GSM").getString());
                resultSetTemp.updateString("WEAVE", ObjFeltSalesOrderDetails.getAttribute("WEAVE").getString());
                resultSetTemp.updateString("CFM_TARGETTED", ObjFeltSalesOrderDetails.getAttribute("CFM_TARGETTED").getString());
                resultSetTemp.updateString("LOOM_NO", ObjFeltSalesOrderDetails.getAttribute("LOOM_NO").getString());
                resultSetTemp.updateString("PAPER_PROD_TYPE", ObjFeltSalesOrderDetails.getAttribute("PAPER_PROD_TYPE").getString());
                resultSetTemp.updateString("UNMAPPED_REASON", ObjFeltSalesOrderDetails.getAttribute("UNMAPPED_REASON").getString());
                resultSetTemp.updateString("SCRAP_REASON", ObjFeltSalesOrderDetails.getAttribute("SCRAP_REASON").getString());
                        
                
                resultSetTemp.updateInt("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
                resultSetTemp.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                resultSetTemp.updateInt("MODIFIED_BY",EITLERPGLOBAL.gNewUserID);
                resultSetTemp.updateString("MODIFIED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                
                resultSetTemp.insertRow();
                
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO",RevNo);
                resultSetHistory.updateInt("SR_NO",i);
                resultSetHistory.updateString("PIECE_AMEND_NO", getAttribute("PIECE_AMEND_NO").getString()+"");
               
                resultSetHistory.updateString("PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                resultSetHistory.updateString("PROD_REMARKS", ObjFeltSalesOrderDetails.getAttribute("PROD_REMARKS").getString());
                resultSetHistory.updateString("LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                resultSetHistory.updateString("LENGTH_UPDATED", ObjFeltSalesOrderDetails.getAttribute("LENGTH_UPDATED").getString());
                resultSetHistory.updateString("WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                resultSetHistory.updateString("WIDTH_UPDATED", ObjFeltSalesOrderDetails.getAttribute("WIDTH_UPDATED").getString());
                resultSetHistory.updateString("GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                resultSetHistory.updateString("GSM_UPDATED", ObjFeltSalesOrderDetails.getAttribute("GSM_UPDATED").getString());
                resultSetHistory.updateString("STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                resultSetHistory.updateString("STYLE_UPDATED", ObjFeltSalesOrderDetails.getAttribute("STYLE_UPDATED").getString());
                resultSetHistory.updateString("PRODUCTCODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCTCODE").getString());
                resultSetHistory.updateString("PRODUCTCODE_UPDATED", ObjFeltSalesOrderDetails.getAttribute("PRODUCTCODE_UPDATED").getString());
                resultSetHistory.updateString("FLET_GROUP", ObjFeltSalesOrderDetails.getAttribute("FLET_GROUP").getString());
                resultSetHistory.updateString("FLET_GROUP_UPDATED", ObjFeltSalesOrderDetails.getAttribute("FLET_GROUP_UPDATED").getString());
                resultSetHistory.updateString("WEIGHT", ObjFeltSalesOrderDetails.getAttribute("WEIGHT").getString());
                resultSetHistory.updateString("WEIGHT_UPDATED", ObjFeltSalesOrderDetails.getAttribute("WEIGHT_UPDATED").getString());
                resultSetHistory.updateString("SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQMTR").getString());
                resultSetHistory.updateString("SQMTR_UPDATED", ObjFeltSalesOrderDetails.getAttribute("SQMTR_UPDATED").getString());
                resultSetHistory.updateString("PIECE_STAGE", ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString());
                resultSetHistory.updateString("ACTION_TAKEN", ObjFeltSalesOrderDetails.getAttribute("ACTION_TAKEN").getString());
                resultSetHistory.updateString("REMARKS", ObjFeltSalesOrderDetails.getAttribute("REMARKS").getString());
                resultSetHistory.updateInt("CHANGE_POSIBILITY", ObjFeltSalesOrderDetails.getAttribute("CHANGE_POSIBILITY").getInt());
                resultSetHistory.updateInt("DELINK", ObjFeltSalesOrderDetails.getAttribute("DELINK").getInt());
                resultSetHistory.updateInt("ACTUAL_CHANGE", ObjFeltSalesOrderDetails.getAttribute("ACTUAL_CHANGE").getInt());
                resultSetHistory.updateString("EXPECTED_DISPATCH", ObjFeltSalesOrderDetails.getAttribute("EXPECTED_DISPATCH").getString());
                
                resultSetHistory.updateString("OBSOLETE_UPN_ASSIGN_STATUS", ObjFeltSalesOrderDetails.getAttribute("OBSOLETE_UPN_ASSIGN_STATUS").getString());
                resultSetHistory.updateString("BASE_GSM", ObjFeltSalesOrderDetails.getAttribute("BASE_GSM").getString());
                resultSetHistory.updateString("WEB_GSM", ObjFeltSalesOrderDetails.getAttribute("WEB_GSM").getString());
                resultSetHistory.updateString("WEAVE", ObjFeltSalesOrderDetails.getAttribute("WEAVE").getString());
                resultSetHistory.updateString("CFM_TARGETTED", ObjFeltSalesOrderDetails.getAttribute("CFM_TARGETTED").getString());
                resultSetHistory.updateString("LOOM_NO", ObjFeltSalesOrderDetails.getAttribute("LOOM_NO").getString());
                resultSetHistory.updateString("PAPER_PROD_TYPE", ObjFeltSalesOrderDetails.getAttribute("PAPER_PROD_TYPE").getString());
                resultSetHistory.updateString("UNMAPPED_REASON", ObjFeltSalesOrderDetails.getAttribute("UNMAPPED_REASON").getString());
                resultSetHistory.updateString("SCRAP_REASON", ObjFeltSalesOrderDetails.getAttribute("SCRAP_REASON").getString());
                    
                resultSetHistory.updateInt("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
                resultSetHistory.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                resultSetHistory.updateInt("UPDATED_BY",EITLERPGLOBAL.gNewUserID);
                resultSetHistory.updateString("UPDATED_DATE",EITLERPGLOBAL.getCurrentDateTimeDB());
                resultSetHistory.insertRow();
                
                
                
                // Final Approval and save to PIECE REGISTER 
                if(getAttribute("APPROVAL_STATUS").getString().equals("F") && !ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString().equals(""))
                {
                    if(!ObjFeltSalesOrderDetails.getAttribute("EXPECTED_DISPATCH").getString().equals(""))
                    {
                        try{
                            System.out.println("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_EXPECTED_DISPATCH='"+ObjFeltSalesOrderDetails.getAttribute("EXPECTED_DISPATCH").getString()+"',PR_EXP_DISPATCH_FROM='WIP-REVIEW',PR_EXP_DISPATCH_DOCNO='"+getAttribute("PIECE_AMEND_NO").getString()+"' where PR_PIECE_NO='"+ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString()+"'");    
                            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_EXPECTED_DISPATCH='"+ObjFeltSalesOrderDetails.getAttribute("EXPECTED_DISPATCH").getString()+"',PR_EXP_DISPATCH_FROM='WIP-REVIEW',PR_EXP_DISPATCH_DOCNO='"+getAttribute("PIECE_AMEND_NO").getString()+"' where PR_PIECE_NO='"+ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString()+"'");
                        }catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    
                    String Piece_Stage = data.getStringValueFromDB("SELECT PR_PIECE_STAGE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO = '"+ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString()+"'");
                    
                    
                    if(Piece_Stage.equals("SPIRALLING") || Piece_Stage.equals("ASSEMBLY") || Piece_Stage.equals("BOOKING") || Piece_Stage.equals("PLANNING") || Piece_Stage.equals("WEAVING") || Piece_Stage.equals("MENDING") || Piece_Stage.equals("NEEDLING") || Piece_Stage.equals("FINISHING") || Piece_Stage.equals("SPLICING") || Piece_Stage.equals("SEAMING")) ////,'',''
                    {
                        if(ObjFeltSalesOrderDetails.getAttribute("ACTUAL_CHANGE").getInt() == 1)
                        {
                            String product_code_str1 = "";
                            String product_code_str2 = "";
                            if(!ObjFeltSalesOrderDetails.getAttribute("PRODUCTCODE_UPDATED").getString().equals(""))
                            {
                                product_code_str1 = "PR_PRODUCT_CODE='"+ObjFeltSalesOrderDetails.getAttribute("PRODUCTCODE_UPDATED").getString()+"', ";
                                product_code_str2 = "PR_BILL_PRODUCT_CODE='"+ObjFeltSalesOrderDetails.getAttribute("PRODUCTCODE_UPDATED").getString()+"', ";
                            }
                              data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET "
                              + "PR_LENGTH='"+ObjFeltSalesOrderDetails.getAttribute("LENGTH_UPDATED").getString()+"',"
                              + "PR_WIDTH='"+ObjFeltSalesOrderDetails.getAttribute("WIDTH_UPDATED").getString()+"',"
                              + "PR_GSM='"+ObjFeltSalesOrderDetails.getAttribute("GSM_UPDATED").getString()+"',"
                              + "PR_THORITICAL_WEIGHT='"+ObjFeltSalesOrderDetails.getAttribute("WEIGHT_UPDATED").getString()+"',"
                              + "PR_SQMTR='"+ObjFeltSalesOrderDetails.getAttribute("SQMTR_UPDATED").getString()+"',"
//                              + "PR_STYLE='"+ObjFeltSalesOrderDetails.getAttribute("STYLE_UPDATED").getString()+"',"
                              + product_code_str1
                              + product_code_str2
                              //+ "PR_STYLE='"+ObjFeltSalesOrderDetails.getAttribute("STYLE_UPDATED").getString()+"',"
                              + "PR_BILL_LENGTH='"+ObjFeltSalesOrderDetails.getAttribute("LENGTH_UPDATED").getString()+"',"
                              + "PR_BILL_WIDTH='"+ObjFeltSalesOrderDetails.getAttribute("WIDTH_UPDATED").getString()+"',"
                              + "PR_BILL_GSM='"+ObjFeltSalesOrderDetails.getAttribute("GSM_UPDATED").getString()+"',"
                              + "PR_BILL_WEIGHT='"+ObjFeltSalesOrderDetails.getAttribute("WEIGHT_UPDATED").getString()+"',"
                              //+ "PR_BILL_STYLE='"+ObjFeltSalesOrderDetails.getAttribute("STYLE_UPDATED").getString()+"',"
                              + "PR_BILL_SQMTR='"+ObjFeltSalesOrderDetails.getAttribute("SQMTR_UPDATED").getString()+"' "
                              
                                   
                              + "where PR_PIECE_NO='"+ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString()+"'");
                              
                              ACTUAL_CHANGE_pieces = ACTUAL_CHANGE_pieces + "," +  ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString() ;
                              
                              try{
                                   data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET "
                                                + " WIP_LENGTH='"+ObjFeltSalesOrderDetails.getAttribute("LENGTH_UPDATED").getString()+"',"
                                                + " WIP_WIDTH='"+ObjFeltSalesOrderDetails.getAttribute("WIDTH_UPDATED").getString()+"',"
                                                + " WIP_GSM='"+ObjFeltSalesOrderDetails.getAttribute("GSM_UPDATED").getString()+"',"
                                                + " WIP_THORITICAL_WEIGHT='"+ObjFeltSalesOrderDetails.getAttribute("WEIGHT_UPDATED").getString()+"',"
                                                + " WIP_SQMTR='"+ObjFeltSalesOrderDetails.getAttribute("SQMTR_UPDATED").getString()+"',"
                                                + " WIP_PRODUCT_CODE='"+ObjFeltSalesOrderDetails.getAttribute("PRODUCTCODE_UPDATED").getString()+"', "
                                                + " WIP_BILL_PRODUCT_CODE='"+ObjFeltSalesOrderDetails.getAttribute("PRODUCTCODE_UPDATED").getString()+"', "
                                                //+ " WIP_STYLE='"+ObjFeltSalesOrderDetails.getAttribute("STYLE_UPDATED").getString()+"',"
                                                //+ " WIP_BILL_STYLE='"+ObjFeltSalesOrderDetails.getAttribute("STYLE_UPDATED").getString()+"',"
                                                + " WIP_BILL_LENGTH='"+ObjFeltSalesOrderDetails.getAttribute("LENGTH_UPDATED").getString()+"',"
                                                + " WIP_BILL_WIDTH='"+ObjFeltSalesOrderDetails.getAttribute("WIDTH_UPDATED").getString()+"',"
                                                + " WIP_BILL_GSM='"+ObjFeltSalesOrderDetails.getAttribute("GSM_UPDATED").getString()+"',"
                                                + " WIP_BILL_WEIGHT='"+ObjFeltSalesOrderDetails.getAttribute("WEIGHT_UPDATED").getString()+"',"
                                                + " WIP_BILL_SQMTR='"+ObjFeltSalesOrderDetails.getAttribute("SQMTR_UPDATED").getString()+"' "

                                                + " where WIP_PIECE_NO='"+ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString()+"'");
                                  
                              }catch(Exception e)
                              {
                                  e.printStackTrace();
                              }
                        }
                        else if(ObjFeltSalesOrderDetails.getAttribute("DELINK").getInt() == 1)
                        {
//                            try
//                            {
//                                insert_delink_flag = true;
//                            //        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_DELINK = 'DELINK',PR_DELINK_REASON = 'WIP MM-UPDATION' WHERE PR_PIECE_NO='"+ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString()+"'");
//                                data.Execute("INSERT INTO `PRODUCTION`.`FELT_SALES_PIECE_AMEND_DELINK` (`DOC_DATE`,`PIECE_NO`,'DELINK_FROM',`APPROVED_BY`,`APPROVAL_STATUS`,`APPROVED_DATE`,`CREATED_BY`,`CREATED_DATE`) " +
//                                              " VALUES ('"+EITLERPGLOBAL.getCurrentDateDB()+"','"+ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString()+"','WIP AMEND','0','P','0000-00-00','"+EITLERPGLOBAL.gNewUserID+"','"+EITLERPGLOBAL.gNewUserID+"');");
//                           
//                            }catch(Exception e)
//                            {
//                                e.printStackTrace();
//                            } 
                            
                            //if(EITLERPGLOBAL.gNewUserID==243 && EITLERPGLOBAL.gNewUserID==262)
                            //{
                                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET "
                                        + "PR_DELINK='OBSOLETE',PR_OBSOLETE_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"',PR_DELINK_REASON = 'WIP MM-UPDATION',PR_UPN='"+getAttribute("MM_PARTY_CODE").getString()+"000000' "
                                        + "where PR_PIECE_NO='"+ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString()+"'");
                                DELINK_pieces = DELINK_pieces + "," + ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString();
                            //}
                                
                                //SCRAP DOC 
                                
                                String sql = "", pDocNo = "", pDocDate = "";

                                try {
                                    //MAPPED
                                    if(ObjFeltSalesOrderDetails.getAttribute("OBSOLETE_UPN_ASSIGN_STATUS").getString().equals("MAPPED"))
                                    {
                                        ResultSet rsData = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='"+ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString()+"'");
                                        rsData.first();
                                        System.out.println("INSERT INTO `PRODUCTION`.`OBSOLETE_MAPPED_UNMAPPED_SCRAP`\n" +
                                            "(PIECE_NO,PARTY_CODE,STYLE,PROD_GROUP,GSM,SYN_PER,UPN_ASSIGN_STATUS,UNMAPPED_REASON,SCRAP_REASON,OBSOLETE_SOURCE,ENTRY_DATE,MAPPING_DOC_NO)\n" +
                                            "VALUES\n" +
                                            "('"+ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString()+"',\n" +
                                            "'"+rsData.getString("PR_PARTY_CODE")+"',\n" +//partycode
                                            "'"+rsData.getString("PR_BILL_STYLE")+"',\n" +//style
                                            "'"+rsData.getString("PR_GROUP")+"',\n" +//prod_group
                                            "'"+rsData.getString("PR_BILL_GSM")+"',\n" +//gsm
                                            "'"+rsData.getString("PR_SYN_PER")+"',\n" +//syn per
                                            "'MAPPED',\n" +
                                            "'',\n" +
                                            "'',\n" + 
                                            "'WIP_PIECE_REVIEW',\n" +
                                            "'"+EITLERPGLOBAL.getCurrentDateDB()+"','')");
                                         data.Execute("INSERT INTO `PRODUCTION`.`OBSOLETE_MAPPED_UNMAPPED_SCRAP`\n" +
                                            "(PIECE_NO,PARTY_CODE,STYLE,PROD_GROUP,GSM,SYN_PER,UPN_ASSIGN_STATUS,UNMAPPED_REASON,SCRAP_REASON,OBSOLETE_SOURCE,ENTRY_DATE,MAPPING_DOC_NO)\n" +
                                            "VALUES\n" +
                                            "('"+ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString()+"',\n" +
                                            "'"+rsData.getString("PR_PARTY_CODE")+"',\n" +//partycode
                                            "'"+rsData.getString("PR_BILL_STYLE")+"',\n" +//style
                                            "'"+rsData.getString("PR_GROUP")+"',\n" +//prod_group
                                            "'"+rsData.getString("PR_BILL_GSM")+"',\n" +//gsm
                                            "'"+rsData.getString("PR_SYN_PER")+"',\n" +//syn per
                                            "'MAPPED',\n" +
                                            "'',\n" +
                                            "'',\n" + 
                                            "'WIP_PIECE_REVIEW',\n" +
                                            "'"+EITLERPGLOBAL.getCurrentDateDB()+"','')");
                                         
                                         try {

                                                Connection Conn1;
                                                Statement stmt1;
                                                ResultSet rsData1;
                                                Conn1 = data.getConn();
                                                stmt1 = Conn1.createStatement();
                                                rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.OBSOLETE_MAPPED_UNMAPPED_SCRAP "
                                                        + "WHERE UPN_ASSIGN_STATUS IN ('MAPPED','UNMAPPED') AND COALESCE(MAPPING_DOC_NO,'') IN ('') "
                                                        + "AND PIECE_STAGE NOT IN ('DIVERTED','INVOICED','EXP-INVOICE','SCRAP','DIVIDED','RETURN','JOINED') "
                                                        + " AND PIECE_NO NOT IN (SELECT D.PIECE_NO FROM PRODUCTION.FELT_OBSOLETE_MAPPING_HEADER H,PRODUCTION.FELT_OBSOLETE_MAPPING_DETAIL D WHERE H.APPROVED=0 AND H.DOC_NO=D.DOC_NO)  ");
                                                System.out.println("SELECT * FROM PRODUCTION.OBSOLETE_MAPPED_UNMAPPED_SCRAP "
                                                        + "WHERE UPN_ASSIGN_STATUS IN ('MAPPED') AND COALESCE(MAPPING_DOC_NO,'') IN ('') "
                                                        + "AND PIECE_STAGE NOT IN ('DIVERTED','INVOICED','EXP-INVOICE','SCRAP','DIVIDED','RETURN','JOINED') "
                                                        + " AND PIECE_NO = '"+ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString()+"' AND PIECE_NO NOT IN (SELECT D.PIECE_NO FROM PRODUCTION.FELT_OBSOLETE_MAPPING_HEADER H,PRODUCTION.FELT_OBSOLETE_MAPPING_DETAIL D WHERE H.APPROVED=0 AND H.DOC_NO=D.DOC_NO)  ");
                                                rsData1.first();

                                                if (rsData1.getRow() > 0) {
                                                    pDocNo = clsFirstFree.getNextFreeNo(2, 661, 363, true);
                                                    pDocDate = data.getStringValueFromDB("SELECT CURDATE() FROM DUAL");
                                                    int SrNo = 0;

                                                    sql = "INSERT INTO PRODUCTION.FELT_OBSOLETE_MAPPING_HEADER "
                                                            + "(DOC_NO, DOC_DATE, DOCUMENT_NAME, REMARK, "
                                                            + "CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, CHANGED, CHANGED_DATE, REJECTED, REJECTED_BY, "
                                                            + "REJECTED_DATE, APPROVED, APPROVED_BY, APPROVED_DATE, CANCELED, HIERARCHY_ID) "
                                                            + "VALUES('" + pDocNo + "', '" + pDocDate + "', 'WIP REVIEW "+getAttribute("PIECE_AMEND_NO").getString()+"', 'AUTO GENERATED', "
                                                            + "338, '" + pDocDate + "', 0, '0000-00-00', 1, '" + pDocDate + "', 0, '', "
                                                            + "'0000-00-00', 0, '', '0000-00-00', 0, 4439)";
                                                    System.out.println("Insert Into Header Data :" + sql);
                                                    data.Execute(sql);

                                                    sql = "INSERT INTO PRODUCTION.FELT_OBSOLETE_MAPPING_HEADER_H "
                                                            + "(REVISION_NO, DOC_NO, DOC_DATE, DOCUMENT_NAME, REMARK, "
                                                            + "CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, CHANGED, CHANGED_DATE, REJECTED, REJECTED_BY, "
                                                            + "REJECTED_DATE, APPROVED, APPROVED_BY, APPROVED_DATE, CANCELED, HIERARCHY_ID, "
                                                            + "REJECTED_REMARKS, FROM_IP, UPDATED_BY, UPDATED_DATE, APPROVAL_STATUS, ENTRY_DATE, APPROVER_REMARKS) "
                                                            + "VALUES(1, '" + pDocNo + "', '" + pDocDate + "', 'WIP REVIEW "+getAttribute("PIECE_AMEND_NO").getString()+"', 'AUTO GENERATED', "
                                                            + "338, '" + pDocDate + "', 0, '0000-00-00', 1, '" + pDocDate + "', 0, '', "
                                                            + "'0000-00-00', 0, '', '0000-00-00', 0, 4439, "
                                                            + "'', '200.0.0.227', 338, '" + pDocDate + "', 'W', '" + pDocDate + "', '')";
                                                    System.out.println("Insert Into Header History Data :" + sql);
                                                    data.Execute(sql);

                                                    while (!rsData1.isAfterLast()) {

                                                        String pPieceNo = rsData1.getString("PIECE_NO");
                                                        String pPartyCode = rsData1.getString("PARTY_CODE");
                                                        String pPartyName = clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, pPartyCode);
                                                        String pPieceStage = rsData1.getString("PIECE_STAGE");
                                                        String pUPNStatus = rsData1.getString("UPN_ASSIGN_STATUS");
                                                        SrNo = SrNo + 1;

                                                        sql = "INSERT INTO PRODUCTION.FELT_OBSOLETE_MAPPING_DETAIL "
                                                                + "(SR_NO, DOC_NO, PIECE_NO, PARTY_CODE, PARTY_NAME, UPN, DIVISION_POSSIBILITY, "
                                                                + "UPN1, UPN2, UPN3, UPN4, UPN5, UPN6, UPN7, UPN8, UPN9, UPN10, UPN11, UPN12, UPN13, UPN14, UPN15, UPN16, UPN17, UPN18, UPN19, UPN20, "
                                                                + "REMARK, PIECE_STAGE, UPN_ASSIGN_STATUS) "
                                                                + "SELECT " + SrNo + ", '" + pDocNo + "', PIECE_NO, PARTY_CODE, '" + pPartyName + "', '"+rsData.getString("PR_UPN")+"', 0, "
                                                                + "'', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', "
                                                                + "'', '" + pPieceStage + "', '" + pUPNStatus + "' "
                                                                + "FROM PRODUCTION.OBSOLETE_MAPPED_UNMAPPED_SCRAP "
                                                                + "WHERE MAPPING_DOC_NO='' AND PIECE_NO='" + pPieceNo + "' AND PARTY_CODE='" + pPartyCode + "' ";
                                                        System.out.println("Insert Into Detail Data :" + sql);
                                                        data.Execute(sql);

                                                        sql = "INSERT INTO PRODUCTION.FELT_OBSOLETE_MAPPING_DETAIL_H "
                                                                + "(REVISION_NO, SR_NO, DOC_NO, PIECE_NO, PARTY_CODE, PARTY_NAME, UPN, DIVISION_POSSIBILITY, "
                                                                + "UPN1, UPN2, UPN3, UPN4, UPN5, UPN6, UPN7, UPN8, UPN9, UPN10, UPN11, UPN12, UPN13, UPN14, UPN15, UPN16, UPN17, UPN18, UPN19, UPN20, "
                                                                + "REMARK, UPN_ASSIGN_STATUS) "
                                                                + "SELECT 1, " + SrNo + ", '" + pDocNo + "', PIECE_NO, PARTY_CODE, '" + pPartyName + "', '"+rsData.getString("PR_UPN")+"', 0, "
                                                                + "'', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', "
                                                                + "'', '" + pUPNStatus + "' "
                                                                + "FROM PRODUCTION.OBSOLETE_MAPPED_UNMAPPED_SCRAP "
                                                                + "WHERE MAPPING_DOC_NO='' AND PIECE_NO='" + pPieceNo + "' AND PARTY_CODE='" + pPartyCode + "' ";
                                                        System.out.println("Insert Into Detail History Data :" + sql);
                                                        data.Execute(sql);

                                                        rsData1.next();
                                                    }

                                                    sql = "INSERT INTO PRODUCTION.FELT_PROD_DOC_DATA "
                                                            + "(MODULE_ID,DOC_NO,DOC_DATE,USER_ID,STATUS,"
                                                            + "TYPE,REMARKS,SR_NO,FROM_USER_ID,FROM_REMARKS,RECEIVED_DATE,"
                                                            + "ACTION_DATE,CHANGED,CHANGED_DATE) "
                                                            + "SELECT 661,'" + pDocNo + "','" + pDocDate + "',USER_ID,CASE WHEN CREATOR =1 THEN 'W' ELSE 'P'  END, "
                                                            + "CASE WHEN CREATOR =0 THEN 'A' ELSE 'C'  END,'WIP REVIEW',SR_NO,0,'','" + pDocDate + "',"
                                                            + "'0000-00-00',1,'" + pDocDate + "'  FROM DINESHMILLS.D_COM_HIERARCHY_RIGHTS  WHERE HIERARCHY_ID = 4439 ";
                                                    System.out.println("Insert Into Felt Prod Doc Data :" + sql);
                                                    data.Execute(sql);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                System.out.println("Error while Saving : " + e.getMessage());
                                            }
                                         
                                    }
                                    //START OF SCRAP
                                    if (ObjFeltSalesOrderDetails.getAttribute("OBSOLETE_UPN_ASSIGN_STATUS").getString().equals("SCRAP")) {

                                        pDocNo = clsFirstFree.getNextFreeNo(2, 662, 364, true);
                                        pDocDate = data.getStringValueFromDB("SELECT CURDATE() FROM DUAL");

                                        String pPieceNo = ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString().trim();
                                        String pScrapReason = ObjFeltSalesOrderDetails.getAttribute("SCRAP_REASON").getString();

                                        sql = "INSERT INTO PRODUCTION.FELT_OBSOLETE_SCRAP "
                                                + "(DOC_NO, DOC_DATE, PIECE_NO, REF_ENTRY_FORM, "
                                                + "REF_DOC_NO, REF_DOC_DATE, REF_SCRAP_REASON, "
                                                + "CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, APPROVED, APPROVED_DATE, REJECTED, REJECTED_DATE, CANCELED, "
                                                + "HIERARCHY_ID, CHANGED, CHANGED_DATE) "
                                                + "VALUES('" + pDocNo + "', '" + pDocDate + "', '" + pPieceNo + "', 'WIP_PIECE_REVIEW', "
                                                + "'" + getAttribute("PIECE_AMEND_NO").getString() + "', '" + getAttribute("PIECE_AMEND_DATE").getString() + "', " + pScrapReason + ", "
                                                + "338, '" + pDocDate + "', NULL, NULL, 0, '0000-00-00', 0, '0000-00-00', 0, "
                                                + "4435, 1, '" + pDocDate + "')";
                                        System.out.println("Insert Into Obsolete Piece Scrap :" + sql);
                                        data.Execute(sql);

                                        sql = "INSERT INTO PRODUCTION.FELT_OBSOLETE_SCRAP_H "
                                                + "(REVISION_NO, UPDATED_BY, ENTRY_DATE, APPROVAL_STATUS, APPROVER_REMARKS, "
                                                + "DOC_NO, DOC_DATE, PIECE_NO, REF_ENTRY_FORM, "
                                                + "REF_DOC_NO, REF_DOC_DATE, REF_SCRAP_REASON, "
                                                + "HIERARCHY_ID, CHANGED, CHANGED_DATE) "
                                                + "VALUES (1, 338, '" + pDocDate + "', 'W', '', "
                                                + "'" + pDocNo + "', '" + pDocDate + "', '" + pPieceNo + "', 'WIP_PIECE_REVIEW', "
                                                + "'" + getAttribute("PIECE_AMEND_NO").getString() + "', '" + getAttribute("PIECE_AMEND_DATE").getString() + "', " + pScrapReason + ", "
                                                + "4435, 1, '" + pDocDate + "')";
                                        System.out.println("Insert Into History of Obsolete Piece Scrap :" + sql);
                                        data.Execute(sql);

                                        sql = "INSERT INTO PRODUCTION.FELT_PROD_DOC_DATA "
                                                + "(MODULE_ID,DOC_NO,DOC_DATE,USER_ID,STATUS,"
                                                + "TYPE,REMARKS,SR_NO,FROM_USER_ID,FROM_REMARKS,RECEIVED_DATE,"
                                                + "ACTION_DATE,CHANGED,CHANGED_DATE) "
                                                + "SELECT 662,'" + pDocNo + "','" + pDocDate + "',USER_ID,CASE WHEN CREATOR =1 THEN 'W' ELSE 'P'  END, "
                                                + "CASE WHEN CREATOR =0 THEN 'A' ELSE 'C'  END,'FROM FELT REJECTION',SR_NO,0,'','" + pDocDate + "',"
                                                + "'0000-00-00',1,'" + pDocDate + "'  FROM DINESHMILLS.D_COM_HIERARCHY_RIGHTS  WHERE HIERARCHY_ID = 4435 ";
                                        System.out.println("Insert Into Felt Prod Doc Data :" + sql);
                                        data.Execute(sql);

                                    }
                                    //END OF SCRAP
                                    //UNMAPPED
                                    if(ObjFeltSalesOrderDetails.getAttribute("OBSOLETE_UPN_ASSIGN_STATUS").getString().equals("UNMAPPED"))
                                    {
                                         ResultSet rsData = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='"+ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString()+"'");
                                        rsData.first();
                                        System.out.println("INSERT INTO `PRODUCTION`.`OBSOLETE_MAPPED_UNMAPPED_SCRAP`\n" +
                                            "(PIECE_NO,PARTY_CODE,STYLE,PROD_GROUP,GSM,SYN_PER,UPN_ASSIGN_STATUS,UNMAPPED_REASON,SCRAP_REASON,OBSOLETE_SOURCE,ENTRY_DATE,MAPPING_DOC_NO)\n" +
                                            "VALUES\n" +
                                            "('"+ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString()+"',\n" +
                                            "'"+rsData.getString("PR_PARTY_CODE")+"',\n" +//partycode
                                            "'"+rsData.getString("PR_BILL_STYLE")+"',\n" +//style
                                            "'"+rsData.getString("PR_GROUP")+"',\n" +//prod_group
                                            "'"+rsData.getString("PR_BILL_GSM")+"',\n" +//gsm
                                            "'"+rsData.getString("PR_SYN_PER")+"',\n" +//syn per
                                            "'MAPPED',\n" +
                                            "'',\n" +
                                            "'',\n" + 
                                            "'WIP_PIECE_REVIEW',\n" +
                                            "'"+EITLERPGLOBAL.getCurrentDateDB()+"','')");
                                         data.Execute("INSERT INTO `PRODUCTION`.`OBSOLETE_MAPPED_UNMAPPED_SCRAP`\n" +
                                            "(PIECE_NO,PARTY_CODE,STYLE,PROD_GROUP,GSM,SYN_PER,UPN_ASSIGN_STATUS,UNMAPPED_REASON,SCRAP_REASON,OBSOLETE_SOURCE,ENTRY_DATE,MAPPING_DOC_NO)\n" +
                                            "VALUES\n" +
                                            "('"+ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString()+"',\n" +
                                            "'"+rsData.getString("PR_PARTY_CODE")+"',\n" +//partycode
                                            "'"+rsData.getString("PR_BILL_STYLE")+"',\n" +//style
                                            "'"+rsData.getString("PR_GROUP")+"',\n" +//prod_group
                                            "'"+rsData.getString("PR_BILL_GSM")+"',\n" +//gsm
                                            "'"+rsData.getString("PR_SYN_PER")+"',\n" +//syn per
                                            "'UNMAPPED',\n" +
                                            "'',\n" +
                                            "'',\n" + 
                                            "'WIP_PIECE_REVIEW',\n" +
                                            "'"+EITLERPGLOBAL.getCurrentDateDB()+"','')");
                                         
                                    } 
                                        
                                    
                                } catch (Exception e) {
                                    System.out.println("Error while Saving : " + e.getMessage());
                                }
                            


                                
                        }
                    }
                    else
                    {
                        mail_pieces = mail_pieces + ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString() + ", ";
                    }
                }
            }

            if(!ACTUAL_CHANGE_pieces.equals("") || !DELINK_pieces.equals(""))
            {
                try{
                       JavaMail.SendMail("feltpp@dineshmills.com,feltsalesnotification@dineshmills.com,brdfltdesign@dineshmills.com", "<br> Hello, <br><br> Pieces which are changed using WIP Amend :  "+ACTUAL_CHANGE_pieces+". <br><br> Pieces which are Obsolete using WIP Amend :  "+DELINK_pieces+".<br><br><br>**** This is an auto-generated email, please do not reply ****", "ACTUAL CHANGE and Obsolete Notification from WIP Amend", "sdmlerp@dineshmills.com");
                }
                catch(Exception e)
                {
                    //e.printStackTrace();
                }
            }
            if(!mail_pieces.equals(""))
            {
                try{
                        JavaMail.SendMail("daxesh@dineshmills.com", "<br> Piece Not Updated from Machine Master to Piece Register.. <br> Due to Pieces not in WIP mode....<br> Pieces : "+mail_pieces, "Not Updated Pieces from Machine Master", "sdmlerp@dineshmills.com");
                }
                catch(Exception e)
                {
                    //e.printStackTrace();
                }
            }
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=ModuleID; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo=getAttribute("PIECE_AMEND_NO").getString()+"";
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=(String)getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.FELT_SALES_PIECE_AMENDMENT_HEADER_WIP";
            ObjFeltProductionApprovalFlow.IsCreator=true;
            ObjFeltProductionApprovalFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks=getAttribute("REJECTED_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName="PIECE_AMEND_NO";
            
            if(getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFeltProductionApprovalFlow.To=(int)getAttribute("SEND_DOC_TO").getVal();
                ObjFeltProductionApprovalFlow.ExplicitSendTo=true;
            }
            if(getAttribute("APPROVAL_STATUS").getString().equals("A") || getAttribute("APPROVAL_STATUS").getString().equals("R")) {
//                    String Subject = "Felt Order Pending Document : "+getAttribute("PIECE_AMEND_NO").getString();
//                    String Message = "Document No : "+getAttribute("PIECE_AMEND_NO").getString()+" is added in your PENDING DOCUMENT"
//                            + "<br><br><br><br> SDML-ERP : http://200.0.0.230:8080/SDMLERP";
//                 
//                    String send_to = clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, (int)getAttribute("TO").getVal());
//
//                    System.out.println("USERID : "+(int)getAttribute("TO").getVal()+", send_to : "+send_to);
//                    try{
//                   //     JMail.SendMail(EITLERPGLOBAL.SMTPHostIP, "sdmlerp@dineshmills.com", send_to, Message, Subject, "sdmlerp@dineshmills.com");
//                    }catch(Exception e)
//                    {
//                        e.printStackTrace();
//                    }
            }
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_AMENDMENT_HEADER_WIP SET REJECTED=0,CHANGED=1 WHERE PIECE_AMEND_NO ='"+getAttribute("PIECE_AMEND_NO").getString()+"'");
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_SALES_PIECE_AMENDMENT_HEADER_WIP WHERE  PIECE_AMEND_NO='"+documentNo +"' AND APPROVED="+1;
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
                    strSQL = "DELETE FROM PRODUCTION.FELT_SALES_PIECE_AMENDMENT_HEADER_WIP WHERE "
                    + " PIECE_AMEND_DATE = '"+ EITLERPGLOBAL.formatDateDB(stringProductionDate) +"'"
                    + " AND PIECE_AMEND_NO ='" + documentNo + "'";
                 
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_SALES_PIECE_AMENDMENT_HEADER_WIP WHERE PIECE_AMEND_NO='"+ orderupdDocumentNo +"' AND APPROVED=1";
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
            String strSql = "SELECT * FROM PRODUCTION.FELT_SALES_PIECE_AMENDMENT_HEADER_WIP WHERE  " + stringFindQuery + "";
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
        ResultSet  resultSetTemp;
        Statement  statementTemp;
        int serialNoCounter=0;
        int RevNo=0;
    
        try {
           
            setAttribute("REVISION_NO",0);
            setAttribute("PIECE_AMEND_NO",resultSet.getString("PIECE_AMEND_NO"));
            setAttribute("PIECE_AMEND_DATE",resultSet.getString("PIECE_AMEND_DATE"));
            setAttribute("MM_PARTY_CODE",resultSet.getString("MM_PARTY_CODE"));
            setAttribute("MM_MACHINE_NO",resultSet.getString ("MM_MACHINE_NO"));
            setAttribute("MM_MACHINE_POSITION",resultSet.getString("MM_MACHINE_POSITION"));
            setAttribute("ENTRY_REASON",resultSet.getString("ENTRY_REASON"));
            //ENTRY_DOCNO
            setAttribute("ENTRY_DOCNO",resultSet.getString("ENTRY_DOCNO"));
            
            if(HistoryView) {
                RevNo=resultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",resultSet.getInt("REVISION_NO"));
            }
            
            setAttribute("CREATED_BY",resultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",resultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",resultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",resultSet.getString("MODIFIED_DATE"));
            setAttribute("APPROVED",resultSet.getInt("APPROVED"));
            setAttribute("APPROVED_BY",resultSet.getInt("APPROVED_BY"));
            setAttribute("APPROVED_DATE",resultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",resultSet.getBoolean("REJECTED"));
            //setAttribute("REJECTED_BY",resultSet.getBoolean("REJECTED_BY"));
            setAttribute("REJECTED_DATE",resultSet.getString("REJECTED_DATE"));
            setAttribute("CANCELED",resultSet.getInt("CANCELED"));
            setAttribute("HIERARCHY_ID",resultSet.getInt("HIERARCHY_ID"));
            
            
           // setAttribute("REJECTED_REMARKS",resultSet.getString("REJECTED_REMARKS"));
            
            hmPieceAmendApprovalDetail.clear();
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            //System.out.println("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_AMENDMENT_DETAIL_WIP WHERE PIECE_AMEND_NO='"+resultSet.getString("PIECE_AMEND_NO")+"'  ORDER BY PIECE_AMEND_NO");
            
            if(HistoryView) {
                System.out.println("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_AMENDMENT_DETAIL_WIP_H WHERE PIECE_AMEND_NO='"+resultSet.getString("PIECE_AMEND_NO")+"'  AND REVISION_NO="+RevNo+" ORDER BY SR_NO DESC");
                resultSetTemp=statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_AMENDMENT_DETAIL_WIP_H WHERE PIECE_AMEND_NO='"+resultSet.getString("PIECE_AMEND_NO")+"'  AND REVISION_NO="+RevNo+" ORDER BY SR_NO DESC");
            }else {
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_AMENDMENT_DETAIL_WIP WHERE PIECE_AMEND_NO='"+resultSet.getString("PIECE_AMEND_NO")+"'  ORDER BY PIECE_AMEND_NO");
            }
            while(resultSetTemp.next()) {
                serialNoCounter++;
  
                clsPieceAmendWIPDetail ObjFeltSalesOrderDetails = new clsPieceAmendWIPDetail();
                 
                ObjFeltSalesOrderDetails.setAttribute("PIECE_AMEND_NO",resultSetTemp.getString("PIECE_AMEND_NO"));
                
                ObjFeltSalesOrderDetails.setAttribute("PIECE_NO",resultSetTemp.getString("PIECE_NO"));
                ObjFeltSalesOrderDetails.setAttribute("PROD_REMARKS",resultSetTemp.getString("PROD_REMARKS"));
                ObjFeltSalesOrderDetails.setAttribute("LENGTH",resultSetTemp.getString("LENGTH"));
                ObjFeltSalesOrderDetails.setAttribute("LENGTH_UPDATED",resultSetTemp.getString("LENGTH_UPDATED"));
                ObjFeltSalesOrderDetails.setAttribute("WIDTH",resultSetTemp.getString("WIDTH"));
                ObjFeltSalesOrderDetails.setAttribute("WIDTH_UPDATED",resultSetTemp.getString("WIDTH_UPDATED"));
                ObjFeltSalesOrderDetails.setAttribute("GSM",resultSetTemp.getString("GSM"));
                ObjFeltSalesOrderDetails.setAttribute("GSM_UPDATED",resultSetTemp.getString("GSM_UPDATED"));
                ObjFeltSalesOrderDetails.setAttribute("STYLE",resultSetTemp.getString("STYLE"));
                ObjFeltSalesOrderDetails.setAttribute("STYLE_UPDATED",resultSetTemp.getString("STYLE_UPDATED"));
                ObjFeltSalesOrderDetails.setAttribute("FLET_GROUP",resultSetTemp.getString("FLET_GROUP"));
                ObjFeltSalesOrderDetails.setAttribute("FLET_GROUP_UPDATED",resultSetTemp.getString("FLET_GROUP_UPDATED"));
                ObjFeltSalesOrderDetails.setAttribute("WEIGHT",resultSetTemp.getString("WEIGHT"));
                ObjFeltSalesOrderDetails.setAttribute("WEIGHT_UPDATED",resultSetTemp.getString("WEIGHT_UPDATED"));
                ObjFeltSalesOrderDetails.setAttribute("SQMTR",resultSetTemp.getString("SQMTR"));
                ObjFeltSalesOrderDetails.setAttribute("SQMTR_UPDATED",resultSetTemp.getString("SQMTR_UPDATED"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCTCODE",resultSetTemp.getString("PRODUCTCODE"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCTCODE_UPDATED",resultSetTemp.getString("PRODUCTCODE_UPDATED"));
                ObjFeltSalesOrderDetails.setAttribute("PIECE_STAGE",resultSetTemp.getString("PIECE_STAGE"));
                ObjFeltSalesOrderDetails.setAttribute("CHANGE_POSIBILITY",resultSetTemp.getInt("CHANGE_POSIBILITY"));
                ObjFeltSalesOrderDetails.setAttribute("DELINK",resultSetTemp.getInt("DELINK"));
                ObjFeltSalesOrderDetails.setAttribute("ACTUAL_CHANGE",resultSetTemp.getInt("ACTUAL_CHANGE"));
                ObjFeltSalesOrderDetails.setAttribute("ACTION_TAKEN",resultSetTemp.getString("ACTION_TAKEN"));
                ObjFeltSalesOrderDetails.setAttribute("REMARKS",resultSetTemp.getString("REMARKS"));
                ObjFeltSalesOrderDetails.setAttribute("EXPECTED_DISPATCH", resultSetTemp.getString("EXPECTED_DISPATCH"));
                
                ObjFeltSalesOrderDetails.setAttribute("OBSOLETE_UPN_ASSIGN_STATUS", resultSetTemp.getString("OBSOLETE_UPN_ASSIGN_STATUS"));
                ObjFeltSalesOrderDetails.setAttribute("BASE_GSM", resultSetTemp.getString("BASE_GSM"));
                ObjFeltSalesOrderDetails.setAttribute("WEB_GSM", resultSetTemp.getString("WEB_GSM"));
                ObjFeltSalesOrderDetails.setAttribute("WEAVE", resultSetTemp.getString("WEAVE"));
                ObjFeltSalesOrderDetails.setAttribute("CFM_TARGETTED", resultSetTemp.getString("CFM_TARGETTED"));
                ObjFeltSalesOrderDetails.setAttribute("LOOM_NO", resultSetTemp.getString("LOOM_NO"));
                ObjFeltSalesOrderDetails.setAttribute("PAPER_PROD_TYPE", resultSetTemp.getString("PAPER_PROD_TYPE"));
                ObjFeltSalesOrderDetails.setAttribute("UNMAPPED_REASON", resultSetTemp.getString("UNMAPPED_REASON"));
                ObjFeltSalesOrderDetails.setAttribute("SCRAP_REASON", resultSetTemp.getString("SCRAP_REASON"));
                
                hmPieceAmendApprovalDetail.put(Integer.toString(serialNoCounter),ObjFeltSalesOrderDetails);
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
            hmPieceAmendApprovalDetail.clear();
            
            String strSql = "SELECT * FROM PRODUCTION.FELT_SALES_PIECE_AMENDMENT_HEADER_WIP_H WHERE PIECE_AMEND_NO = " + pDocNo + "";
            
            connection=data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet=statement.executeQuery(strSql);
            resultSet.first();
            setAttribute("PIECE_AMEND_NO",resultSet.getString("PIECE_AMEND_NO"));
            setAttribute("UPDATED_BY",resultSet.getString("UPDATED_BY"));
            setAttribute("PIECE_AMEND_DATE",resultSet.getString("PIECE_AMEND_DATE"));
            setAttribute("HIERARCHY_ID",resultSet.getInt("HIERARCHY_ID"));
            setAttribute("PIECE_AMEND_NO",resultSet.getString("PIECE_AMEND_NO"));
            setAttribute("PIECE_AMEND_DATE",resultSet.getString("PIECE_AMEND_DATE"));
            setAttribute("MM_PARTY_CODE",resultSet.getString("MM_PARTY_CODE"));
            setAttribute("MM_MACHINE_NO",resultSet.getString ("MM_MACHINE_NO"));
            setAttribute("MM_MACHINE_POSITION",resultSet.getString("MM_MACHINE_POSITION"));

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_AMENDMENT_DETAIL_WIP_H WHERE PIECE_AMEND_NO='"+pDocNo+"'");
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
                
                
                clsPieceAmendWIPDetail ObjFeltSalesOrderDetails = new clsPieceAmendWIPDetail();
                
                //ObjFeltSalesOrderDetails.setAttribute("SR_NO",serialNoCounter);
               // ObjFeltSalesOrderDetails.setAttribute("S_ORDER_DETAIL_CODE",resultSetTemp.getString("S_ORDER_DETAIL_CODE"));
                ObjFeltSalesOrderDetails.setAttribute("PIECE_AMEND_NO",resultSetTemp.getString("PIECE_AMEND_NO"));
                ObjFeltSalesOrderDetails.setAttribute("PIECE_AMEND_NO",resultSetTemp.getString("PIECE_AMEND_NO"));
                ObjFeltSalesOrderDetails.setAttribute("PIECE_NO",resultSetTemp.getString("PIECE_NO"));
                ObjFeltSalesOrderDetails.setAttribute("LENGTH",resultSetTemp.getString("LENGTH"));
                ObjFeltSalesOrderDetails.setAttribute("LENGTH_UPDATED",resultSetTemp.getString("LENGTH_UPDATED"));
                ObjFeltSalesOrderDetails.setAttribute("WIDTH",resultSetTemp.getString("WIDTH"));
                ObjFeltSalesOrderDetails.setAttribute("WIDTH_UPDATED",resultSetTemp.getString("WIDTH_UPDATED"));
                ObjFeltSalesOrderDetails.setAttribute("GSM",resultSetTemp.getString("GSM"));
                ObjFeltSalesOrderDetails.setAttribute("GSM_UPDATED",resultSetTemp.getString("GSM_UPDATED"));
                ObjFeltSalesOrderDetails.setAttribute("STYLE",resultSetTemp.getString("STYLE"));
                ObjFeltSalesOrderDetails.setAttribute("STYLE_UPDATED",resultSetTemp.getString("STYLE_UPDATED"));
                ObjFeltSalesOrderDetails.setAttribute("FLET_GROUP",resultSetTemp.getString("FLET_GROUP"));
                ObjFeltSalesOrderDetails.setAttribute("FLET_GROUP_UPDATED",resultSetTemp.getString("FLET_GROUP_UPDATED"));
                ObjFeltSalesOrderDetails.setAttribute("WEIGHT",resultSetTemp.getString("WEIGHT"));
                ObjFeltSalesOrderDetails.setAttribute("WEIGHT_UPDATED",resultSetTemp.getString("WEIGHT_UPDATED"));
                ObjFeltSalesOrderDetails.setAttribute("SQMTR",resultSetTemp.getString("SQMTR"));
                ObjFeltSalesOrderDetails.setAttribute("SQMTR_UPDATED",resultSetTemp.getString("SQMTR_UPDATED"));
                ObjFeltSalesOrderDetails.setAttribute("PIECE_STAGE",resultSetTemp.getString("PIECE_STAGE"));
                
                
               hmPieceAmendApprovalDetail.put(Integer.toString(serialNoCounter),ObjFeltSalesOrderDetails);
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
    //k23  p14
    public static HashMap getHistoryList(String stringProductionDate, String productionDocumentNo) {
        HashMap hmHistoryList=new HashMap();
        Statement stTmp;
        ResultSet rsTmp;
       // String stringProductionDate1 =EITLERPGLOBAL.formatDate(stringProductionDate);
        
        
        try {
            stTmp=connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);

            rsTmp=stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_AMENDMENT_HEADER_WIP_H WHERE PIECE_AMEND_NO='"+productionDocumentNo+"'");
            while(rsTmp.next()) {
                clsPieceAmendWIP felt_order = new clsPieceAmendWIP();
                
                felt_order.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                felt_order.setAttribute("UPDATED_BY",rsTmp.getString("UPDATED_BY"));
                felt_order.setAttribute("UPDATED_DATE",rsTmp.getString("UPDATED_DATE"));
                felt_order.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                felt_order.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                felt_order.setAttribute("REJECTED_REMARKS",rsTmp.getString("REJECTED_REMARKS"));
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
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_AMENDMENT_HEADER_WIP_H WHERE PIECE_AMEND_NO ='"+pDocNo+"'");
            Ready=true;
           // historyAmendDate = pProductionDate;
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
                strSQL="SELECT DISTINCT PIECE_AMEND_NO,PIECE_AMEND_DATE,RECEIVED_DATE,MM_PARTY_CODE FROM PRODUCTION.FELT_SALES_PIECE_AMENDMENT_HEADER_WIP, PRODUCTION.FELT_PROD_DOC_DATA   WHERE PIECE_AMEND_NO=DOC_NO AND STATUS='W' AND MODULE_ID="+ModuleID+" AND CANCELED=0  AND USER_ID="+pUserID+" ORDER BY RECEIVED_DATE,PIECE_AMEND_NO";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT DISTINCT PIECE_AMEND_NO,PIECE_AMEND_DATE,RECEIVED_DATE,MM_PARTY_CODE FROM PRODUCTION.FELT_SALES_PIECE_AMENDMENT_HEADER_WIP, PRODUCTION.FELT_PROD_DOC_DATA   WHERE PIECE_AMEND_NO=DOC_NO AND STATUS='W' AND MODULE_ID="+ModuleID+" AND CANCELED=0  AND USER_ID="+pUserID+" ORDER BY RECEIVED_DATE,PIECE_AMEND_NO";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT DISTINCT PIECE_AMEND_NO,PIECE_AMEND_DATE,RECEIVED_DATE,MM_PARTY_CODE FROM PRODUCTION.FELT_SALES_PIECE_AMENDMENT_HEADER_WIP, PRODUCTION.FELT_PROD_DOC_DATA   WHERE PIECE_AMEND_NO=DOC_NO AND STATUS='W' AND MODULE_ID="+ModuleID+" AND CANCELED=0  AND USER_ID="+pUserID+"  ORDER BY PIECE_AMEND_NO";
            }
            
            ResultSet rsTmp=data.getConn().createStatement().executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp.next()) {
                
                Counter=Counter+1;
                clsPieceAmendWIP ObjDoc=new clsPieceAmendWIP();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO",rsTmp.getString("PIECE_AMEND_NO"));
                ObjDoc.setAttribute("DOC_DATE",rsTmp.getString("PIECE_AMEND_DATE"));
                ObjDoc.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                ObjDoc.setAttribute("PARTY_CODE",rsTmp.getString("MM_PARTY_CODE"));
               
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
  
  public HashMap getPieceList(String Party_Code, String MachineNo, String Position) {
        HashMap hmPieceList = new HashMap();
        if(Integer.parseInt(MachineNo) <= 9)
        {
            MachineNo = "0"+Integer.parseInt(MachineNo);
        }
        if(Integer.parseInt(Position) <= 9)
        {
            Position = "0"+Integer.parseInt(Position);
        }
        String SQL = "SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE = '"+Party_Code+"' AND PR_MACHINE_NO="+MachineNo+" AND PR_POSITION_NO ="+Position+" AND PR_PIECE_STAGE IN ('SPIRALLING','ASSEMBLY','BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SPLICING','SEAMING','HEAT_SETTING','MARKING') AND (PR_DELINK!='OBSOLETE' OR PR_DELINK IS NULL) ";
        System.out.println("SQL = " + SQL);
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(SQL);
            Ready = true;

            while (resultSet.next()) {
                 clsPieceAmendWIP piece = new clsPieceAmendWIP();
                 String Length="",Width="",Gsm="",Style="",Pr_Group="",machine_product_code="";
                 boolean flag= true;
                
                 try{ 
                        String strSQL;
                        ResultSet rsTmp;
                        
                        //strSQL="SELECT D1.MM_MACHINE_NO,D1.MM_MACHINE_POSITION,D1.MM_MACHINE_POSITION_DESC,D1.MM_ITEM_CODE,D1.MM_GRUP,D1.MM_FELT_LENGTH,D1.MM_FELT_WIDTH,D1.MM_FELT_GSM,D1.MM_FELT_STYLE FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL D1 where D1.MM_PARTY_CODE='"+Party_Code+"' AND D1.MM_MACHINE_NO = '"+MachineNo+"' AND D1.MM_MACHINE_POSITION = '"+Integer.parseInt(Position)+"'";
                        strSQL="SELECT D1.MM_MACHINE_NO,D1.MM_MACHINE_POSITION,D1.MM_MACHINE_POSITION_DESC,D1.MM_ITEM_CODE,D1.MM_GRUP,(MM_FELT_LENGTH+MM_FABRIC_LENGTH) AS LENGTH,(MM_FELT_WIDTH+MM_FABRIC_WIDTH) AS WIDTH,MM_FELT_GSM AS GSM,concat(MM_FELT_STYLE,MM_STYLE_DRY) AS STYLE  FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL D1 where D1.MM_PARTY_CODE='"+Party_Code+"' AND D1.MM_MACHINE_NO = '"+MachineNo+"' AND D1.MM_MACHINE_POSITION = '"+Integer.parseInt(Position)+"'";
                        System.out.println(" Check to Machine No Query : "+strSQL);  
                        
                        rsTmp=data.getResult(strSQL);
                        rsTmp.first();

                        Length = rsTmp.getString("LENGTH");
                        Width = rsTmp.getString("WIDTH");
                        Gsm = rsTmp.getString("GSM");
                        Style = rsTmp.getString("STYLE");
                        Pr_Group = rsTmp.getString("MM_GRUP");
                        machine_product_code = rsTmp.getString("MM_ITEM_CODE");
                        if("".equals(Length) || "".equals(Width) || "".equals(Gsm) || "".equals(Style) || "".equals(machine_product_code))
                        {
                            flag=false;
                        }
                        
                }catch(NumberFormatException | SQLException ew)
                {
                    System.out.println("Error on getting data");
                }
               
                if(flag)
                {
                
                            float l1 = Float.parseFloat(resultSet.getString("PR_LENGTH"));
                            float l2 = Float.parseFloat(Length);

                            float w1 = Float.parseFloat(resultSet.getString("PR_WIDTH"));
                            float w2 = Float.parseFloat(Width);

                            float g1 = Float.parseFloat(resultSet.getString("PR_GSM"));
                            float g2 = Float.parseFloat(Gsm);

                            
                            String product_code = resultSet.getString("PR_PRODUCT_CODE");
                            
                            //if(l1 == l2 && w1 == w2 && g1 == g2 && resultSet.getString("PR_STYLE").equals(Style) && machine_product_code.equals(product_code))
                            if(l1 == l2 && w1 == w2 && g1 == g2  && machine_product_code.equals(product_code))
                            {
                                flag=false;
                            }


                            if(flag)
                            {   
                                piece.setAttribute("PIECE_AMEND_NO", "");
                                piece.setAttribute("PIECE_AMEND_DATE","");
                                piece.setAttribute("PIECE_NO", resultSet.getString("PR_PIECE_NO"));
                                piece.setAttribute("MM_MACHINE_NO", resultSet.getString("PR_MACHINE_NO"));
                                piece.setAttribute("MM_MACHINE_POSITION", resultSet.getString("PR_POSITION_NO"));
                                piece.setAttribute("MM_PARTY_CODE", resultSet.getString("PR_PARTY_CODE"));

                                piece.setAttribute("PRODUCTCODE", resultSet.getString("PR_PRODUCT_CODE"));
                                piece.setAttribute("PRODUCTCODE_UPDATED", machine_product_code);
                                
                                piece.setAttribute("LENGTH", resultSet.getString("PR_LENGTH"));
                                piece.setAttribute("LENGTH_UPDATED", ""+l2);

                                piece.setAttribute("WIDTH", resultSet.getString("PR_WIDTH"));
                                piece.setAttribute("WIDTH_UPDATED", ""+w2);

                                piece.setAttribute("GSM", resultSet.getString("PR_GSM"));
                                piece.setAttribute("GSM_UPDATED", ""+g2);

                                piece.setAttribute("STYLE", resultSet.getString("PR_STYLE"));
                                piece.setAttribute("STYLE_UPDATED", ""+Style);

                                piece.setAttribute("FLET_GROUP", resultSet.getString("PR_GROUP"));
                                piece.setAttribute("FLET_GROUP_UPDATED", ""+Pr_Group);

                                float Theoritical_Weigth = ((l2 * w2) * g2) / 1000;

                                float SQMT = (l2 * w2);

                                piece.setAttribute("WEIGHT", resultSet.getString("PR_THORITICAL_WEIGHT"));
                                piece.setAttribute("WEIGHT_UPDATED", ""+Theoritical_Weigth);

                                piece.setAttribute("SQMTR", resultSet.getString("PR_SQMTR"));
                                piece.setAttribute("SQMTR_UPDATED", ""+SQMT);

                                piece.setAttribute("PIECE_STAGE", resultSet.getString("PR_PIECE_STAGE"));
//
//                                piece.setAttribute("CREATED_DATE", resultSet.getString("CREATED_DATE"));
//                                piece.setAttribute("CREATED_BY", resultSet.getString("CREATED_BY"));
//                                piece.setAttribute("MODIFIED_DATE", resultSet.getString("MODIFIED_DATE"));
//                                piece.setAttribute("MODIFIED_BY", resultSet.getString("MODIFIED_BY"));
//                                piece.setAttribute("HIERARCHY_ID", resultSet.getString("HIERARCHY_ID"));
//                                piece.setAttribute("APPROVER_BY", resultSet.getString("APPROVER_BY"));
//                                piece.setAttribute("APPROVER_DATE", resultSet.getString("APPROVER_DATE"));
//                                piece.setAttribute("APPROVER_REMARK", resultSet.getString("APPROVER_REMARK"));

                                hmPieceList.put(hmPieceList.size() + 1, piece);
                            }
               }
            }
            
            return hmPieceList;
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Error on fetch data : " + e.getMessage());
            e.printStackTrace();
            LastError = e.getMessage();
            return hmPieceList;
        }
        
    }
  
  
    public HashMap getPieceList(String Expected_Month) {
        HashMap hmPieceList = new HashMap();
//        if(Integer.parseInt(MachineNo) <= 9)
//        {
//            MachineNo = "0"+Integer.parseInt(MachineNo);
//        }
//        if(Integer.parseInt(Position) <= 9)
//        {
//            Position = "0"+Integer.parseInt(Position);
//        }
        //PR_EXPECTED_DISPATCH
        String SQL = "SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_EXPECTED_DISPATCH = '"+Expected_Month+"'  AND PR_PIECE_STAGE IN ('PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SPLICING','SEAMING')";
        System.out.println("SQL = " + SQL);
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(SQL);
            Ready = true;

            while (resultSet.next()) {
                 clsPieceAmendWIP piece = new clsPieceAmendWIP();
                 
                    piece.setAttribute("PIECE_AMEND_NO", "");
                    piece.setAttribute("PIECE_AMEND_DATE","");
                    piece.setAttribute("PIECE_NO", resultSet.getString("PR_PIECE_NO"));
                    piece.setAttribute("MM_MACHINE_NO", resultSet.getString("PR_MACHINE_NO"));
                    piece.setAttribute("MM_MACHINE_POSITION", resultSet.getString("PR_POSITION_NO"));
                    piece.setAttribute("MM_PARTY_CODE", resultSet.getString("PR_PARTY_CODE"));

                    piece.setAttribute("PRODUCTCODE", resultSet.getString("PR_PRODUCT_CODE"));
                    piece.setAttribute("PRODUCTCODE_UPDATED", "0.00");

                    piece.setAttribute("LENGTH", resultSet.getString("PR_LENGTH"));
                    piece.setAttribute("LENGTH_UPDATED", "0.00");

                    piece.setAttribute("WIDTH", resultSet.getString("PR_WIDTH"));
                    piece.setAttribute("WIDTH_UPDATED", "0.00");

                    piece.setAttribute("GSM", resultSet.getString("PR_GSM"));
                    piece.setAttribute("GSM_UPDATED", "0.00");

                    piece.setAttribute("STYLE", resultSet.getString("PR_STYLE"));
                    piece.setAttribute("STYLE_UPDATED", "0.00");

                    piece.setAttribute("FLET_GROUP", resultSet.getString("PR_GROUP"));
                    piece.setAttribute("FLET_GROUP_UPDATED", "0.00");

                    piece.setAttribute("WEIGHT", resultSet.getString("PR_THORITICAL_WEIGHT"));
                    piece.setAttribute("WEIGHT_UPDATED", "0.00");

                    piece.setAttribute("SQMTR", resultSet.getString("PR_SQMTR"));
                    piece.setAttribute("SQMTR_UPDATED", "0.00");

                    piece.setAttribute("PIECE_STAGE", resultSet.getString("PR_PIECE_STAGE"));

                    hmPieceList.put(hmPieceList.size() + 1, piece);
            }
            return hmPieceList;
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Error on fetch data : " + e.getMessage());
            e.printStackTrace();
            LastError = e.getMessage();
            return hmPieceList;
        }
        
    }
    
    public static void CancelDoc(String pAmendNo) {
        ResultSet rsTmp=null;
        
        if(CanCancel(pAmendNo)) {
            
            boolean Approved=false;
            //PRODUCTION.FELT_SALES_PIECE_AMENDMENT_HEADER_WIP WHERE PIECE_AMEND_NO
            try {
                rsTmp=data.getResult("SELECT APPROVED FROM PRODUCTION.FELT_SALES_PIECE_AMENDMENT_HEADER_WIP WHERE PIECE_AMEND_NO='"+pAmendNo+"' ");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    Approved=rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();
                
                if(!Approved) {
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='"+pAmendNo+"' AND MODULE_ID=774");
                }
                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_AMENDMENT_HEADER_WIP SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE PIECE_AMEND_NO='"+pAmendNo+"'");
               
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
            System.out.println("SELECT PIECE_AMEND_NO FROM PRODUCTION.FELT_SALES_PIECE_AMENDMENT_HEADER_WIP WHERE PIECE_AMEND_NO='"+pDocNo+"'  AND APPROVED=0 AND CANCELED=0");
            rsTmp=stTmp.executeQuery("SELECT PIECE_AMEND_NO FROM PRODUCTION.FELT_SALES_PIECE_AMENDMENT_HEADER_WIP WHERE PIECE_AMEND_NO='"+pDocNo+"'  AND APPROVED=0 AND CANCELED=0");
            
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
}
