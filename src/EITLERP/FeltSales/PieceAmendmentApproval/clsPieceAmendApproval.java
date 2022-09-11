/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.PieceAmendmentApproval;

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
public class clsPieceAmendApproval {
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
    private static int ModuleID = 750;
    
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
    public clsPieceAmendApproval() {
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
            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_HEADER ORDER BY PIECE_AMEND_NO");
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
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_DETAIL WHERE PIECE_AMEND_NO=''");
            
            // Felt order Updation data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_DETAIL_H WHERE PIECE_AMEND_NO=''");
            
            stHeader=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHeader=stHeader.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_HEADER WHERE PIECE_AMEND_NO=''");
            
            stHeaderH=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHeaderH=stHeaderH.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_HEADER_H WHERE PIECE_AMEND_NO=''");

            //setAttribute("PIECE_AMEND_NO",);
                
            rsHeader.first();
            rsHeader.moveToInsertRow();
            //PIECE_AMEND_NO
            
            rsHeader.updateString("PIECE_AMEND_NO",getAttribute("PIECE_AMEND_NO").getString());
            rsHeader.updateString("PIECE_AMEND_DATE",getAttribute("PIECE_AMEND_DATE").getString());
            rsHeader.updateString("MM_PARTY_CODE",getAttribute("MM_PARTY_CODE").getString());
            rsHeader.updateString("MM_MACHINE_NO",getAttribute("MM_MACHINE_NO").getString());
            rsHeader.updateString("MM_MACHINE_POSITION",getAttribute("MM_MACHINE_POSITION").getString());
                     
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
                clsPieceAmendApprovalDetail ObjFeltSalesOrderDetails = (clsPieceAmendApprovalDetail) hmPieceAmendApprovalDetail.get(Integer.toString(i));
             
                String Doc_No = data.getStringValueFromDB("SELECT H.PIECE_AMEND_NO FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_HEADER H,PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_DETAIL D WHERE H.PIECE_AMEND_NO=D.PIECE_AMEND_NO AND H.APPROVED=0 AND H.CANCELED=0 AND D.PIECE_NO='"+ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString()+"'");
                if(Doc_No.equals(""))
                {
                        //Insert records into Felt order Amendment table
                        resultSetTemp.moveToInsertRow();

                        resultSetTemp.updateString("PIECE_AMEND_NO", getAttribute("PIECE_AMEND_NO").getString());
                        resultSetTemp.updateInt("SELECTED", ObjFeltSalesOrderDetails.getAttribute("SELECTED").getInt());
                        resultSetTemp.updateString("PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                        resultSetTemp.updateString("LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                        resultSetTemp.updateString("LENGTH_UPDATED", ObjFeltSalesOrderDetails.getAttribute("LENGTH_UPDATED").getString());
                        resultSetTemp.updateString("WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                        resultSetTemp.updateString("WIDTH_UPDATED", ObjFeltSalesOrderDetails.getAttribute("WIDTH_UPDATED").getString());
                        resultSetTemp.updateString("GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                        resultSetTemp.updateString("GSM_UPDATED", ObjFeltSalesOrderDetails.getAttribute("GSM_UPDATED").getString());
                        resultSetTemp.updateString("STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                        resultSetTemp.updateString("STYLE_UPDATED", ObjFeltSalesOrderDetails.getAttribute("STYLE_UPDATED").getString());
                        resultSetTemp.updateString("FLET_GROUP", ObjFeltSalesOrderDetails.getAttribute("FLET_GROUP").getString());
                        resultSetTemp.updateString("FLET_GROUP_UPDATED", ObjFeltSalesOrderDetails.getAttribute("FLET_GROUP_UPDATED").getString());
                        resultSetTemp.updateString("WEIGHT", ObjFeltSalesOrderDetails.getAttribute("WEIGHT").getString());
                        resultSetTemp.updateString("WEIGHT_UPDATED", ObjFeltSalesOrderDetails.getAttribute("WEIGHT_UPDATED").getString());
                        resultSetTemp.updateString("SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQMTR").getString());
                        resultSetTemp.updateString("SQMTR_UPDATED", ObjFeltSalesOrderDetails.getAttribute("SQMTR_UPDATED").getString());
                        resultSetTemp.updateString("PIECE_STAGE", ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString());
                        resultSetTemp.updateString("UNTICK_USER", ObjFeltSalesOrderDetails.getAttribute("UNTICK_USER").getString());
                        resultSetTemp.updateString("UNTICK_REMARK_DESIGN", ObjFeltSalesOrderDetails.getAttribute("UNTICK_REMARK_DESIGN").getString());
                        resultSetTemp.updateString("UNTICK_REMARK_PRODUCTION", ObjFeltSalesOrderDetails.getAttribute("UNTICK_REMARK_PRODUCTION").getString());
                        resultSetTemp.updateString("ACTION_TAKEN", ObjFeltSalesOrderDetails.getAttribute("ACTION_TAKEN").getString());

                        resultSetTemp.updateInt("CREATED_BY",EITLERPGLOBAL.gNewUserID);
                        resultSetTemp.updateString("CREATED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));

                        resultSetTemp.insertRow();

                        //Insert records into Felt Order Amendment History Table
                        resultSetHistory.moveToInsertRow();
                        resultSetHistory.updateInt("REVISION_NO",1);
                        resultSetHistory.updateInt("SR_NO",i);
                        resultSetHistory.updateString("PIECE_AMEND_NO", getAttribute("PIECE_AMEND_NO").getString());
                        resultSetHistory.updateInt("SELECTED", ObjFeltSalesOrderDetails.getAttribute("SELECTED").getInt());
                        resultSetHistory.updateString("PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                        resultSetHistory.updateString("LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                        resultSetHistory.updateString("LENGTH_UPDATED", ObjFeltSalesOrderDetails.getAttribute("LENGTH_UPDATED").getString());
                        resultSetHistory.updateString("WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                        resultSetHistory.updateString("WIDTH_UPDATED", ObjFeltSalesOrderDetails.getAttribute("WIDTH_UPDATED").getString());
                        resultSetHistory.updateString("GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                        resultSetHistory.updateString("GSM_UPDATED", ObjFeltSalesOrderDetails.getAttribute("GSM_UPDATED").getString());
                        resultSetHistory.updateString("STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                        resultSetHistory.updateString("STYLE_UPDATED", ObjFeltSalesOrderDetails.getAttribute("STYLE_UPDATED").getString());
                        resultSetHistory.updateString("FLET_GROUP", ObjFeltSalesOrderDetails.getAttribute("FLET_GROUP").getString());
                        resultSetHistory.updateString("FLET_GROUP_UPDATED", ObjFeltSalesOrderDetails.getAttribute("FLET_GROUP_UPDATED").getString());
                        resultSetHistory.updateString("WEIGHT", ObjFeltSalesOrderDetails.getAttribute("WEIGHT").getString());
                        resultSetHistory.updateString("WEIGHT_UPDATED", ObjFeltSalesOrderDetails.getAttribute("WEIGHT_UPDATED").getString());
                        resultSetHistory.updateString("SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQMTR").getString());
                        resultSetHistory.updateString("SQMTR_UPDATED", ObjFeltSalesOrderDetails.getAttribute("SQMTR_UPDATED").getString());
                        resultSetHistory.updateString("PIECE_STAGE", ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString());
                        resultSetHistory.updateString("UNTICK_USER", ObjFeltSalesOrderDetails.getAttribute("UNTICK_USER").getString());
                        resultSetHistory.updateString("UNTICK_REMARK_DESIGN", ObjFeltSalesOrderDetails.getAttribute("UNTICK_REMARK_DESIGN").getString());
                        resultSetHistory.updateString("UNTICK_REMARK_PRODUCTION", ObjFeltSalesOrderDetails.getAttribute("UNTICK_REMARK_PRODUCTION").getString());
                        resultSetHistory.updateString("ACTION_TAKEN", ObjFeltSalesOrderDetails.getAttribute("ACTION_TAKEN").getString());

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
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator=true;
            ObjFeltProductionApprovalFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks=getAttribute("REJECTED_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName="PIECE_AMEND_NO";
            
            //JOptionPane.showMessageDialog(null, "TO : "+(int)getAttribute("TO").getVal()+", Approval Status = "+(String)getAttribute("APPROVAL_STATUS").getObj());
            if("A".equals((String)getAttribute("APPROVAL_STATUS").getObj()))
            {   
//                    String Subject = "Felt Order Pending Document : "+getAttribute("PIECE_AMEND_NO").getString();
//                    String Message = "Document No : "+getAttribute("PIECE_AMEND_NO").getString()+" is added in your PENDING DOCUMENT"
//                             + "\n\n\n\n SDML-ERP : http://200.0.0.227:8080/SDMLERP";
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
            resultSetHistory=statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_DETAIL_H WHERE PIECE_AMEND_NO='"+getAttribute("PIECE_AMEND_NO").getString()+"'");
            resultSetHistory.first();
            if(resultSetHistory.getRow()>0){
                revisionNo=resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from production data table before insert
            //statementHistory.execute("DELETE FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_DETAIL WHERE PIECE_AMEND_NO='"+getAttribute("PIECE_AMEND_NO").getString()+"'");
            
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_DETAIL_H WHERE PIECE_AMEND_NO=''");
            
            stHeaderH=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHeaderH=stHeaderH.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_HEADER_H WHERE PIECE_AMEND_NO=''");
         
            resultSet.updateString("PIECE_AMEND_NO",getAttribute("PIECE_AMEND_NO").getString());
            resultSet.updateString("PIECE_AMEND_DATE",(String)getAttribute("PIECE_AMEND_DATE").getString());
            resultSet.updateString("MM_PARTY_CODE",getAttribute("MM_PARTY_CODE").getString());
            resultSet.updateString("MM_MACHINE_NO",getAttribute("MM_MACHINE_NO").getString());
            resultSet.updateString("MM_MACHINE_POSITION",getAttribute("MM_MACHINE_POSITION").getString());
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
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_HEADER_H WHERE PIECE_AMEND_NO='"+getAttribute("PIECE_AMEND_NO").getString()+"'");
            
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
            data.Execute("DELETE FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_DETAIL WHERE PIECE_AMEND_NO='"+OrderNo+"'");
            
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_DETAIL WHERE PIECE_AMEND_NO=''");
             
            String mail_pieces = "";
            
            for(int i=1;i<=hmPieceAmendApprovalDetail.size();i++) {
                clsPieceAmendApprovalDetail ObjFeltSalesOrderDetails = (clsPieceAmendApprovalDetail) hmPieceAmendApprovalDetail.get(Integer.toString(i));
               
                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateInt("SR_NO",i);
                resultSetTemp.updateString("PIECE_AMEND_NO", getAttribute("PIECE_AMEND_NO").getString()+"");
                resultSetTemp.updateInt("SELECTED", ObjFeltSalesOrderDetails.getAttribute("SELECTED").getInt());
                resultSetTemp.updateString("PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                resultSetTemp.updateString("LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                resultSetTemp.updateString("LENGTH_UPDATED", ObjFeltSalesOrderDetails.getAttribute("LENGTH_UPDATED").getString());
                resultSetTemp.updateString("WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                resultSetTemp.updateString("WIDTH_UPDATED", ObjFeltSalesOrderDetails.getAttribute("WIDTH_UPDATED").getString());
                resultSetTemp.updateString("GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                resultSetTemp.updateString("GSM_UPDATED", ObjFeltSalesOrderDetails.getAttribute("GSM_UPDATED").getString());
                resultSetTemp.updateString("STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                resultSetTemp.updateString("STYLE_UPDATED", ObjFeltSalesOrderDetails.getAttribute("STYLE_UPDATED").getString());
                resultSetTemp.updateString("FLET_GROUP", ObjFeltSalesOrderDetails.getAttribute("FLET_GROUP").getString());
                resultSetTemp.updateString("FLET_GROUP_UPDATED", ObjFeltSalesOrderDetails.getAttribute("FLET_GROUP_UPDATED").getString());
                resultSetTemp.updateString("WEIGHT", ObjFeltSalesOrderDetails.getAttribute("WEIGHT").getString());
                resultSetTemp.updateString("WEIGHT_UPDATED", ObjFeltSalesOrderDetails.getAttribute("WEIGHT_UPDATED").getString());
                resultSetTemp.updateString("SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQMTR").getString());
                resultSetTemp.updateString("SQMTR_UPDATED", ObjFeltSalesOrderDetails.getAttribute("SQMTR_UPDATED").getString());
                resultSetTemp.updateString("PIECE_STAGE", ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString());
                resultSetTemp.updateString("UNTICK_USER", ObjFeltSalesOrderDetails.getAttribute("UNTICK_USER").getString());
                resultSetTemp.updateString("UNTICK_REMARK_DESIGN", ObjFeltSalesOrderDetails.getAttribute("UNTICK_REMARK_DESIGN").getString());
                resultSetTemp.updateString("UNTICK_REMARK_PRODUCTION", ObjFeltSalesOrderDetails.getAttribute("UNTICK_REMARK_PRODUCTION").getString());
                resultSetTemp.updateString("ACTION_TAKEN", ObjFeltSalesOrderDetails.getAttribute("ACTION_TAKEN").getString());
                
                resultSetTemp.updateInt("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
                resultSetTemp.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                resultSetTemp.updateInt("MODIFIED_BY",EITLERPGLOBAL.gNewUserID);
                resultSetTemp.updateString("MODIFIED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                
                resultSetTemp.insertRow();
                
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO",RevNo);
                resultSetHistory.updateInt("SR_NO",i);
                resultSetHistory.updateString("PIECE_AMEND_NO", getAttribute("PIECE_AMEND_NO").getString()+"");
                resultSetHistory.updateInt("SELECTED", ObjFeltSalesOrderDetails.getAttribute("SELECTED").getInt());
                resultSetHistory.updateString("PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                resultSetHistory.updateString("LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                resultSetHistory.updateString("LENGTH_UPDATED", ObjFeltSalesOrderDetails.getAttribute("LENGTH_UPDATED").getString());
                resultSetHistory.updateString("WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                resultSetHistory.updateString("WIDTH_UPDATED", ObjFeltSalesOrderDetails.getAttribute("WIDTH_UPDATED").getString());
                resultSetHistory.updateString("GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                resultSetHistory.updateString("GSM_UPDATED", ObjFeltSalesOrderDetails.getAttribute("GSM_UPDATED").getString());
                resultSetHistory.updateString("STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                resultSetHistory.updateString("STYLE_UPDATED", ObjFeltSalesOrderDetails.getAttribute("STYLE_UPDATED").getString());
                resultSetHistory.updateString("FLET_GROUP", ObjFeltSalesOrderDetails.getAttribute("FLET_GROUP").getString());
                resultSetHistory.updateString("FLET_GROUP_UPDATED", ObjFeltSalesOrderDetails.getAttribute("FLET_GROUP_UPDATED").getString());
                resultSetHistory.updateString("WEIGHT", ObjFeltSalesOrderDetails.getAttribute("WEIGHT").getString());
                resultSetHistory.updateString("WEIGHT_UPDATED", ObjFeltSalesOrderDetails.getAttribute("WEIGHT_UPDATED").getString());
                resultSetHistory.updateString("SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQMTR").getString());
                resultSetHistory.updateString("SQMTR_UPDATED", ObjFeltSalesOrderDetails.getAttribute("SQMTR_UPDATED").getString());
                resultSetHistory.updateString("PIECE_STAGE", ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString());
                resultSetHistory.updateString("UNTICK_USER", ObjFeltSalesOrderDetails.getAttribute("UNTICK_USER").getString());
                resultSetHistory.updateString("UNTICK_REMARK_DESIGN", ObjFeltSalesOrderDetails.getAttribute("UNTICK_REMARK_DESIGN").getString());
                resultSetHistory.updateString("UNTICK_REMARK_PRODUCTION", ObjFeltSalesOrderDetails.getAttribute("UNTICK_REMARK_PRODUCTION").getString());
                resultSetHistory.updateString("ACTION_TAKEN", ObjFeltSalesOrderDetails.getAttribute("ACTION_TAKEN").getString());
                
                
                resultSetHistory.updateInt("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
                resultSetHistory.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                resultSetHistory.updateInt("UPDATED_BY",EITLERPGLOBAL.gNewUserID);
                resultSetHistory.updateString("UPDATED_DATE",EITLERPGLOBAL.getCurrentDateTimeDB());
                resultSetHistory.insertRow();
                
                
                // Final Approval and save to PIECE REGISTER 
                if(getAttribute("APPROVAL_STATUS").getString().equals("F") && !ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString().equals(""))
                {
                    
                    String Piece_Stage = data.getStringValueFromDB("SELECT PR_PIECE_STAGE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO = '"+ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString()+"'");
                    
                    if(Piece_Stage.equals("WEAVING") || Piece_Stage.equals("MENDING") || Piece_Stage.equals("NEEDLING") || Piece_Stage.equals("FINISHING")) 
                    {
                        if(ObjFeltSalesOrderDetails.getAttribute("SELECTED").getInt()==1)
                        {
                              data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET "
                              + "PR_LENGTH='"+ObjFeltSalesOrderDetails.getAttribute("LENGTH_UPDATED").getString()+"',"
                              + "PR_WIDTH='"+ObjFeltSalesOrderDetails.getAttribute("WIDTH_UPDATED").getString()+"',"
                              + "PR_GSM='"+ObjFeltSalesOrderDetails.getAttribute("GSM_UPDATED").getString()+"',"
                              + "PR_THORITICAL_WEIGHT='"+ObjFeltSalesOrderDetails.getAttribute("WEIGHT_UPDATED").getString()+"',"
                              + "PR_SQMTR='"+ObjFeltSalesOrderDetails.getAttribute("SQMTR_UPDATED").getString()+"',"
                              + "PR_STYLE='"+ObjFeltSalesOrderDetails.getAttribute("STYLE_UPDATED").getString()+"' "
                              + "where PR_PIECE_NO='"+ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString()+"'");
                        }
                        else
                        {
                            if(!"".equals(ObjFeltSalesOrderDetails.getAttribute("UNTICK_USER").getString()))
                            {
                                if(clsUser.getDeptID(EITLERPGLOBAL.gCompanyID, Integer.parseInt(ObjFeltSalesOrderDetails.getAttribute("UNTICK_USER").getString())) == 40)
                                {
                                     try{
                                             data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_DELINK = 'DELINK',PR_DELINK_REASON = 'WIP MM-UPDATION' WHERE PR_PIECE_NO='"+ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString()+"'");
                                     }catch(Exception e)
                                     {
                                         e.printStackTrace();
                                     }
                                }
                            }
                        }
                    }
                    else
                    {
                        mail_pieces = mail_pieces + ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString() + ", ";
                    }
                }
            }
            if(!mail_pieces.equals(""))
            {
                try{
                        JavaMail.SendMail("daxesh@dineshmills.com", "\n Piece Not Updated from Machine Master to Piece Register.. \n Due to Pieces not in WIP mode....\n Pieces : "+mail_pieces, "Not Updated Pieces from Machine Master", "sdmlerp@dineshmills.com");
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
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_HEADER";
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
//                            + "\n\n\n\n SDML-ERP : http://200.0.0.227:8080/SDMLERP";
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
                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_HEADER SET REJECTED=0,CHANGED=1 WHERE PIECE_AMEND_NO ='"+getAttribute("PIECE_AMEND_NO").getString()+"'");
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_HEADER WHERE  PIECE_AMEND_NO='"+documentNo +"' AND APPROVED="+1;
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
                    strSQL = "DELETE FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_HEADER WHERE "
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_HEADER WHERE PIECE_AMEND_NO='"+ orderupdDocumentNo +"' AND APPROVED=1";
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
            String strSql = "SELECT * FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_HEADER WHERE  " + stringFindQuery + "";
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
            //System.out.println("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_DETAIL WHERE PIECE_AMEND_NO='"+resultSet.getString("PIECE_AMEND_NO")+"'  ORDER BY PIECE_AMEND_NO");
            
            if(HistoryView) {
                System.out.println("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_DETAIL_H WHERE PIECE_AMEND_NO='"+resultSet.getString("PIECE_AMEND_NO")+"'  AND REVISION_NO="+RevNo+" ORDER BY SR_NO DESC");
                resultSetTemp=statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_DETAIL_H WHERE PIECE_AMEND_NO='"+resultSet.getString("PIECE_AMEND_NO")+"'  AND REVISION_NO="+RevNo+" ORDER BY SR_NO DESC");
            }else {
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_DETAIL WHERE PIECE_AMEND_NO='"+resultSet.getString("PIECE_AMEND_NO")+"'  ORDER BY PIECE_AMEND_NO");
            }
            while(resultSetTemp.next()) {
                serialNoCounter++;
  
                clsPieceAmendApprovalDetail ObjFeltSalesOrderDetails = new clsPieceAmendApprovalDetail();
                 
                ObjFeltSalesOrderDetails.setAttribute("PIECE_AMEND_NO",resultSetTemp.getString("PIECE_AMEND_NO"));
                
                ObjFeltSalesOrderDetails.setAttribute("SELECTED",resultSetTemp.getInt("SELECTED"));
                
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
                
                ObjFeltSalesOrderDetails.setAttribute("UNTICK_USER",resultSetTemp.getString("UNTICK_USER"));
                ObjFeltSalesOrderDetails.setAttribute("UNTICK_REMARK_DESIGN",resultSetTemp.getString("UNTICK_REMARK_DESIGN"));
                ObjFeltSalesOrderDetails.setAttribute("UNTICK_REMARK_PRODUCTION",resultSetTemp.getString("UNTICK_REMARK_PRODUCTION"));
                ObjFeltSalesOrderDetails.setAttribute("ACTION_TAKEN",resultSetTemp.getString("ACTION_TAKEN"));
                
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
            
            String strSql = "SELECT * FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_HEADER_H WHERE PIECE_AMEND_NO = " + pDocNo + "";
            
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
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_DETAIL_H WHERE PIECE_AMEND_NO='"+pDocNo+"'");
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
                
                
                clsPieceAmendApprovalDetail ObjFeltSalesOrderDetails = new clsPieceAmendApprovalDetail();
                
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

            rsTmp=stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_HEADER_H WHERE PIECE_AMEND_NO='"+productionDocumentNo+"'");
            while(rsTmp.next()) {
                clsPieceAmendApproval felt_order = new clsPieceAmendApproval();
                
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
            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_HEADER_H WHERE PIECE_AMEND_NO ='"+pDocNo+"'");
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
                strSQL="SELECT DISTINCT PIECE_AMEND_NO,PIECE_AMEND_DATE,RECEIVED_DATE,MM_PARTY_CODE FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_HEADER, PRODUCTION.FELT_PROD_DOC_DATA   WHERE PIECE_AMEND_NO=DOC_NO AND STATUS='W' AND MODULE_ID="+ModuleID+" AND CANCELED=0  AND USER_ID="+pUserID+" ORDER BY RECEIVED_DATE,PIECE_AMEND_NO";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT DISTINCT PIECE_AMEND_NO,PIECE_AMEND_DATE,RECEIVED_DATE,MM_PARTY_CODE FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_HEADER, PRODUCTION.FELT_PROD_DOC_DATA   WHERE PIECE_AMEND_NO=DOC_NO AND STATUS='W' AND MODULE_ID="+ModuleID+" AND CANCELED=0  AND USER_ID="+pUserID+" ORDER BY RECEIVED_DATE,PIECE_AMEND_NO";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT DISTINCT PIECE_AMEND_NO,PIECE_AMEND_DATE,RECEIVED_DATE,MM_PARTY_CODE FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_HEADER, PRODUCTION.FELT_PROD_DOC_DATA   WHERE PIECE_AMEND_NO=DOC_NO AND STATUS='W' AND MODULE_ID="+ModuleID+" AND CANCELED=0  AND USER_ID="+pUserID+"  ORDER BY PIECE_AMEND_NO";
            }
            
            ResultSet rsTmp=data.getConn().createStatement().executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp.next()) {
                
                Counter=Counter+1;
                clsPieceAmendApproval ObjDoc=new clsPieceAmendApproval();
                
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
        String SQL = "SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE = '"+Party_Code+"' AND PR_MACHINE_NO='"+MachineNo+"' AND PR_POSITION_NO ='"+Position+"' AND PR_PIECE_STAGE IN ('WEAVING','MENDING','NEEDLING','FINISHING')";
        System.out.println("SQL = " + SQL);
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(SQL);
            Ready = true;

            while (resultSet.next()) {
                 clsPieceAmendApproval piece = new clsPieceAmendApproval();
                 String Length="",Width="",Gsm="",Style="",Pr_Group="";
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
                
                        if("".equals(Length) || "".equals(Width) || "".equals(Gsm) || "".equals(Style))
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

                            if(l1 == l2 && w1 == w2 && g1 == g2 && resultSet.getString("PR_STYLE").equals(Style))
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

                                piece.setAttribute("PR_PRODUCT_CODE", resultSet.getString("PR_PRODUCT_CODE"));

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

                                piece.setAttribute("CREATED_DATE", resultSet.getString("CREATED_DATE"));
                                piece.setAttribute("CREATED_BY", resultSet.getString("CREATED_BY"));
                                piece.setAttribute("MODIFIED_DATE", resultSet.getString("MODIFIED_DATE"));
                                piece.setAttribute("MODIFIED_BY", resultSet.getString("MODIFIED_BY"));
                                piece.setAttribute("HIERARCHY_ID", resultSet.getString("HIERARCHY_ID"));
                                piece.setAttribute("APPROVER_BY", resultSet.getString("APPROVER_BY"));
                                piece.setAttribute("APPROVER_DATE", resultSet.getString("APPROVER_DATE"));
                                piece.setAttribute("APPROVER_REMARK", resultSet.getString("APPROVER_REMARK"));

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
    
    public static void CancelDoc(String pAmendNo) {
        ResultSet rsTmp=null;
        
        if(CanCancel(pAmendNo)) {
            
            boolean Approved=false;
            //PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_HEADER WHERE PIECE_AMEND_NO
            try {
                rsTmp=data.getResult("SELECT APPROVED FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_HEADER WHERE PIECE_AMEND_NO='"+pAmendNo+"' ");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    Approved=rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();
                
                if(!Approved) {
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='"+pAmendNo+"' AND MODULE_ID=750");
                }
                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_HEADER SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE PIECE_AMEND_NO='"+pAmendNo+"'");
               
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
            System.out.println("SELECT PIECE_AMEND_NO FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_HEADER WHERE PIECE_AMEND_NO='"+pDocNo+"'  AND APPROVED=0 AND CANCELED=0");
            rsTmp=stTmp.executeQuery("SELECT PIECE_AMEND_NO FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_HEADER WHERE PIECE_AMEND_NO='"+pDocNo+"'  AND APPROVED=0 AND CANCELED=0");
            
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
