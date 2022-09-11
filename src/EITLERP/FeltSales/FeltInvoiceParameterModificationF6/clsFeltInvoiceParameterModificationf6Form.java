/*
 * clsFeltProcessInvoiceVariable.java
 *
 * Created on July 19, 2013, 5:31 PM
 */

package EITLERP.FeltSales.FeltInvoiceParameterModificationF6;

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
import static EITLERP.Production.FeltDiscRateMaster.clsDiscRateMaster.CanCancel;
import EITLERP.Production.clsFeltProductionApprovalFlow;
import EITLERP.SelectFirstFree;
import EITLERP.clsFirstFree;


public class clsFeltInvoiceParameterModificationf6Form {

    static String getBillValue(int gCompanyID, String trim) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String LastError="";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private int FFNo=0;
    public HashMap props;
    public boolean Ready = false;
    //public HashMap hmFeltBaleNoDetails;
        
    //History Related properties
    public boolean HistoryView=false;
    public HashMap hmFeltBaleNoDetails;
    public static int ModuleID = 754;
    
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
    public clsFeltInvoiceParameterModificationf6Form() {
        LastError = "";
        props=new HashMap();
        props.put("DOC_NO", new Variant(""));
        props.put("DOC_DATE", new Variant(""));
        props.put("PROCESSING_DATE", new Variant(""));
        props.put("PARTY_CODE", new Variant(""));
        props.put("PARTY_NAME", new Variant(""));
        props.put("STATION", new Variant(""));
        props.put("TRANSPORT_MODE", new Variant(""));
        props.put("MODE_REOPEN_BALE", new Variant(""));
        props.put("INSURANCE_CODE", new Variant(false));
        props.put("TRANSPORTER_CODE", new Variant(""));
        props.put("BOX_SIZE", new Variant(""));
        
        props.put("BALE_NO", new Variant(""));
        props.put("BALE_DATE", new Variant(""));
        props.put("BILL_VALUE", new Variant(""));
        
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
        
        hmFeltBaleNoDetails=new HashMap();
    }
    
    public boolean LoadData() {
        Ready=false;
        try {
            connection = data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //resultSet=statement.executeQuery("SELECT DOC_NO,PROCESSING_DATE,PARTY_CODE,PARTY_NAME,CHARGE_CODE_NEW,CRITICAL_LIMIT_NEW,REMARKS,INSURANCE_CODE,TRANSPORTER_CODE,WITHOUT_CRITICAL_LIMIT,VEHICLE_NO,ADV_DOC_NO,ADV_AGN_INV_AMT,ADV_AGN_IGST_AMT,ADV_AGN_SGST_AMT,ADV_AGN_CGST_AMT,ADV_AGN_GST_COMP_CESS_AMT,DOC_DATE,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,CANCELED,HIERARCHY_ID,CHANGED,CHANGED_DATE,F6 FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST GROUP BY DOC_NO ORDER BY DOC_NO");
            resultSet=statement.executeQuery("SELECT DOC_NO,PROCESSING_DATE,PARTY_CODE,PARTY_NAME,CHARGE_CODE_NEW,CRITICAL_LIMIT_NEW,REMARKS,INSURANCE_CODE,TRANSPORTER_CODE,WITHOUT_CRITICAL_LIMIT,VEHICLE_NO,ADV_DOC_NO,ADV_AGN_INV_AMT,ADV_AGN_IGST_AMT,ADV_AGN_SGST_AMT,ADV_AGN_CGST_AMT,ADV_AGN_GST_COMP_CESS_AMT,DOC_DATE,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,CANCELED,HIERARCHY_ID,CHANGED,CHANGED_DATE,F6 FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE DOC_NO LIKE 'IPF6%' GROUP BY DOC_NO ORDER BY DOC_NO,PARTY_CODE");
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
        Statement  statementDetail,statementH, statementDetailHistory, statementHistory;
        try {
            String baleDate = "";
            // Packing data history connection
            statementH=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet=statementH.executeQuery("SELECT * FROM  PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE DOC_NO=''");
            
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_INV_PROCESS_VAR_GST_H WHERE DOC_NO=''");
            
            
            
            //--------- Generate Bale no. ---------------------
            setAttribute("DOC_NO",EITLERP.clsFirstFree.getNextFreeNo(2, 754, getAttribute("FFNO").getInt(), true));
            //-------------------------------------------------
//            String str1 = getAttribute("BALE_NO").getString();
//            String[] bno = str1.split(",");
//            
//            for (int i=0;i<bno.length;i++){
//                bno[i] = bno[i].replaceAll("[^\\w]", ",");
//                System.out.println("BALE_NO : "+bno[i]);
//                if(bno[i]=="")
//                {
//                    bno[i] = "0";
//                }
                
//                baleDate = data.getStringValueFromDB("SELECT PKG_BALE_DATE FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_BALE_NO='"+bno[i]+"' AND APPROVED=1 AND CANCELED=0 AND BALE_REOPEN_FLG=0 AND INVOICE_FLG=0 ORDER BY PKG_BALE_DATE DESC");

            for(int i=1;i<=hmFeltBaleNoDetails.size();i++) {
            clsFeltInvoiceParameterModificationf6FormDetail ObjFeltPMDetail = (clsFeltInvoiceParameterModificationf6FormDetail) hmFeltBaleNoDetails.get(Integer.toString(i));

            resultSet.moveToInsertRow();
            resultSet.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSet.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
            resultSet.updateString("PROCESSING_DATE", getAttribute("PROCESSING_DATE").getString());
            resultSet.updateString("BALE_NO",  ObjFeltPMDetail.getAttribute("BALE_NO").getString());
            resultSet.updateString("BALE_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltPMDetail.getAttribute("BALE_DATE").getString()));
            resultSet.updateString("PARTY_CODE", ObjFeltPMDetail.getAttribute("PARTY_CODE").getString());
            resultSet.updateString("PARTY_NAME", ObjFeltPMDetail.getAttribute("PARTY_NAME").getString());
            resultSet.updateString("CHARGE_CODE_NEW", getAttribute("CHARGE_CODE_NEW").getString());
            resultSet.updateFloat("CRITICAL_LIMIT_NEW",(float)getAttribute("CRITICAL_LIMIT_NEW").getVal());
            resultSet.updateBoolean("INSURANCE_CODE", getAttribute("INSURANCE_CODE").getBool());
            resultSet.updateBoolean("WITHOUT_CRITICAL_LIMIT", getAttribute("WITHOUT_CRITICAL_LIMIT").getBool());
            resultSet.updateBoolean("F6", ObjFeltPMDetail.getAttribute("F6").getBool());
            resultSet.updateString("TRANSPORTER_CODE", getAttribute("TRANSPORTER_CODE").getString());
            resultSet.updateString("REMARKS", ObjFeltPMDetail.getAttribute("REMARKS").getString());
            resultSet.updateString("VEHICLE_NO",getAttribute("VEHICLE_NO").getString());
            resultSet.updateString("ADV_DOC_NO",getAttribute("ADV_DOC_NO").getString());
            resultSet.updateFloat("ADV_AGN_INV_AMT",(float)getAttribute("ADV_AGN_INV_AMT").getVal());
            resultSet.updateFloat("ADV_AGN_IGST_AMT",(float)getAttribute("ADV_AGN_IGST_AMT").getVal());
            resultSet.updateFloat("ADV_AGN_SGST_AMT",(float)getAttribute("ADV_AGN_SGST_AMT").getVal());
            resultSet.updateFloat("ADV_AGN_CGST_AMT",(float)getAttribute("ADV_AGN_CGST_AMT").getVal());
            resultSet.updateFloat("ADV_AGN_GST_COMP_CESS_AMT",(float)getAttribute("ADV_AGN_GST_COMP_CESS_AMT").getVal());
            resultSet.updateFloat("BILL_VALUE",(float)ObjFeltPMDetail.getAttribute("BILL_VALUE").getVal());
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
            
            int revisionNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST_H WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
            revisionNo++;
            
            resultSetHistory.moveToInsertRow();
            resultSetHistory.updateInt("REVISION_NO",revisionNo);
            resultSetHistory.updateInt("UPDATED_BY",getAttribute("UPDATED_BY").getInt());
            resultSetHistory.updateString("ENTRY_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSetHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            resultSetHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSetHistory.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
            resultSetHistory.updateString("PROCESSING_DATE", getAttribute("PROCESSING_DATE").getString());
            resultSetHistory.updateString("BALE_NO", ObjFeltPMDetail.getAttribute("BALE_NO").getString());
            resultSetHistory.updateString("BALE_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltPMDetail.getAttribute("BALE_DATE").getString()));
            resultSetHistory.updateString("PARTY_CODE", ObjFeltPMDetail.getAttribute("PARTY_CODE").getString());
            resultSetHistory.updateString("PARTY_NAME", ObjFeltPMDetail.getAttribute("PARTY_NAME").getString());
            resultSetHistory.updateString("CHARGE_CODE_NEW", getAttribute("CHARGE_CODE_NEW").getString());
            resultSetHistory.updateFloat("CRITICAL_LIMIT_NEW",(float)getAttribute("CRITICAL_LIMIT_NEW").getVal());
            resultSetHistory.updateBoolean("INSURANCE_CODE", getAttribute("INSURANCE_CODE").getBool());
            resultSetHistory.updateBoolean("WITHOUT_CRITICAL_LIMIT", getAttribute("WITHOUT_CRITICAL_LIMIT").getBool());
            resultSetHistory.updateBoolean("F6", ObjFeltPMDetail.getAttribute("F6").getBool());
            resultSetHistory.updateString("TRANSPORTER_CODE", getAttribute("TRANSPORTER_CODE").getString());
            resultSetHistory.updateString("REMARKS", ObjFeltPMDetail.getAttribute("REMARKS").getString());
            resultSetHistory.updateString("VEHICLE_NO",getAttribute("VEHICLE_NO").getString());
            resultSetHistory.updateString("ADV_DOC_NO",getAttribute("ADV_DOC_NO").getString());
            resultSetHistory.updateFloat("ADV_AGN_INV_AMT",(float)getAttribute("ADV_AGN_INV_AMT").getVal());
            resultSetHistory.updateFloat("ADV_AGN_IGST_AMT",(float)getAttribute("ADV_AGN_IGST_AMT").getVal());
            resultSetHistory.updateFloat("ADV_AGN_SGST_AMT",(float)getAttribute("ADV_AGN_SGST_AMT").getVal());
            resultSetHistory.updateFloat("ADV_AGN_CGST_AMT",(float)getAttribute("ADV_AGN_CGST_AMT").getVal());
            resultSetHistory.updateFloat("ADV_AGN_GST_COMP_CESS_AMT",(float)getAttribute("ADV_AGN_GST_COMP_CESS_AMT").getVal());
            resultSetHistory.updateFloat("BILL_VALUE",(float)ObjFeltPMDetail.getAttribute("BILL_VALUE").getVal());
            resultSetHistory.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
            resultSetHistory.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSetHistory.updateBoolean("REJECTED",false);
            resultSetHistory.updateString("REJECTED_DATE","0000-00-00");
            resultSetHistory.updateInt("CANCELED",0);
            resultSetHistory.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            resultSetHistory.updateInt("CHANGED",1);
            resultSetHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
             ResultSet rsTmp=data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");
            
            resultSetHistory.updateString("FROM_IP",""+str_split[1]);
            resultSetHistory.insertRow();
            }
           
//            String str1 = getAttribute("BALE_NO").getString();
//            String[] bno = str1.split(",");
//            
//            for (int i=0;i<bno.length;i++){
//                bno[i] = bno[i].replaceAll("[^\\w]", ",");
//                System.out.println("BALE_NO : "+bno[i]);
//                if(bno[i]=="")
//                {
//                    bno[i] = "0";
//                }
//                resultSetDetail.moveToInsertRow();
//            resultSetDetail.updateString("DOC_NO", getAttribute("DOC_NO").getString());
//            resultSetDetail.updateString("PROCESSING_DATE", getAttribute("PROCESSING_DATE").getString());
//            resultSetDetail.updateString("BALE_NO", bno[i]);
//            resultSetDetail.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
//            resultSetDetail.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
//            resultSetDetail.updateString("CHARGE_CODE_NEW", getAttribute("CHARGE_CODE_NEW").getString());
//            resultSetDetail.updateFloat("CRITICAL_LIMIT_NEW",(float)getAttribute("CRITICAL_LIMIT_NEW").getVal());
//            resultSetDetail.updateBoolean("INSURANCE_CODE", getAttribute("INSURANCE_CODE").getBool());
//            resultSetDetail.updateBoolean("WITHOUT_CRITICAL_LIMIT", getAttribute("WITHOUT_CRITICAL_LIMIT").getBool());
//            resultSetDetail.updateString("TRANSPORTER_CODE", getAttribute("TRANSPORTER_CODE").getString());
//            resultSetDetail.updateString("REMARKS", getAttribute("REMARKS").getString());
//            resultSetDetail.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
//            resultSetDetail.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
//            resultSetDetail.updateBoolean("APPROVED",false);
//            resultSetDetail.updateString("APPROVED_DATE","0000-00-00");
//            resultSetDetail.updateBoolean("REJECTED",false);
//            resultSetDetail.updateString("REJECTED_DATE","0000-00-00");
//            resultSetDetail.updateInt("CANCELED",0);
//            resultSetDetail.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
//            resultSetDetail.updateInt("CHANGED",1);
//            resultSetDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
//            resultSetDetail.insertRow();
//            
//            }
            
//            resultSetDetail.moveToInsertRow();
//            resultSetDetail.updateString("DOC_NO", getAttribute("DOC_NO").getString());
//            resultSetDetail.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
//            resultSetDetail.updateString("PROCESSING_DATE", getAttribute("PROCESSING_DATE").getString());
//            resultSetDetail.updateInt("BALE_NO", getAttribute("BALE_NO").getInt());
//            resultSetDetail.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
//            resultSetDetail.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
//            resultSetDetail.updateString("CHARGE_CODE_NEW", getAttribute("CHARGE_CODE_NEW").getString());
//            resultSetDetail.updateFloat("CRITICAL_LIMIT_NEW",(float)getAttribute("CRITICAL_LIMIT_NEW").getVal());
//            resultSetDetail.updateString("REMARKS", getAttribute("REMARKS").getString());
//            resultSetDetail.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
//            resultSetDetail.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
//            resultSetDetail.updateBoolean("APPROVED",false);
//            resultSetDetail.updateString("APPROVED_DATE","0000-00-00");
//            resultSetDetail.updateBoolean("REJECTED",false);
//            resultSetDetail.updateString("REJECTED_DATE","0000-00-00");
//            resultSetDetail.updateInt("CANCELED",0);
//            resultSetDetail.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
//            resultSetDetail.updateInt("CHANGED",1);
//            resultSetDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
//            resultSetDetail.insertRow();
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=754; //Felt Change Bale No
            ObjFeltProductionApprovalFlow.DocNo=getDocumentNo(getAttribute("DOC_NO").getString());
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.FELT_INV_PROCESS_VAR_GST";
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
            resultSetDetail = statementDetail.executeQuery("SELECT * FROM  PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE DOC_NO=''");
           
            // Packing data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_INV_PROCESS_VAR_GST_H WHERE DOC_NO=''");
            
            
            data.Execute("DELETE FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
             
            for(int i=1;i<=hmFeltBaleNoDetails.size();i++) {
            clsFeltInvoiceParameterModificationf6FormDetail ObjFeltPMDetail = (clsFeltInvoiceParameterModificationf6FormDetail) hmFeltBaleNoDetails.get(Integer.toString(i));

            resultSetDetail.moveToInsertRow();
            resultSetDetail.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSetDetail.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
            resultSetDetail.updateString("PROCESSING_DATE", getAttribute("PROCESSING_DATE").getString());
            resultSetDetail.updateString("BALE_NO",  ObjFeltPMDetail.getAttribute("BALE_NO").getString());
            resultSetDetail.updateString("BALE_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltPMDetail.getAttribute("BALE_DATE").getString()));
            resultSetDetail.updateString("PARTY_CODE", ObjFeltPMDetail.getAttribute("PARTY_CODE").getString());
            resultSetDetail.updateString("PARTY_NAME", ObjFeltPMDetail.getAttribute("PARTY_NAME").getString());
            resultSetDetail.updateString("CHARGE_CODE_NEW", getAttribute("CHARGE_CODE_NEW").getString());
            resultSetDetail.updateFloat("CRITICAL_LIMIT_NEW",(float)getAttribute("CRITICAL_LIMIT_NEW").getVal());
            resultSetDetail.updateBoolean("INSURANCE_CODE", getAttribute("INSURANCE_CODE").getBool());
            resultSetDetail.updateBoolean("WITHOUT_CRITICAL_LIMIT", getAttribute("WITHOUT_CRITICAL_LIMIT").getBool());
            resultSetDetail.updateBoolean("F6", ObjFeltPMDetail.getAttribute("F6").getBool());
            resultSetDetail.updateString("TRANSPORTER_CODE", getAttribute("TRANSPORTER_CODE").getString());
            resultSetDetail.updateString("REMARKS", ObjFeltPMDetail.getAttribute("REMARKS").getString());
            resultSetDetail.updateString("VEHICLE_NO",getAttribute("VEHICLE_NO").getString());
            resultSetDetail.updateString("ADV_DOC_NO",getAttribute("ADV_DOC_NO").getString());
            resultSetDetail.updateFloat("ADV_AGN_INV_AMT",(float)getAttribute("ADV_AGN_INV_AMT").getVal());
            resultSetDetail.updateFloat("ADV_AGN_IGST_AMT",(float)getAttribute("ADV_AGN_IGST_AMT").getVal());
            resultSetDetail.updateFloat("ADV_AGN_SGST_AMT",(float)getAttribute("ADV_AGN_SGST_AMT").getVal());
            resultSetDetail.updateFloat("ADV_AGN_CGST_AMT",(float)getAttribute("ADV_AGN_CGST_AMT").getVal());
            resultSetDetail.updateFloat("ADV_AGN_GST_COMP_CESS_AMT",(float)getAttribute("ADV_AGN_GST_COMP_CESS_AMT").getVal());
            resultSetDetail.updateFloat("BILL_VALUE",(float)ObjFeltPMDetail.getAttribute("BILL_VALUE").getVal());
            resultSetDetail.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
            resultSetDetail.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSetDetail.updateBoolean("APPROVED",false);
            resultSetDetail.updateString("APPROVED_DATE","0000-00-00");
            resultSetDetail.updateBoolean("REJECTED",false);
            resultSetDetail.updateString("REJECTED_DATE","0000-00-00");
            resultSetDetail.updateInt("CANCELED",0);
            resultSetDetail.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            resultSetDetail.updateInt("CHANGED",1);
            resultSetDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            resultSetDetail.insertRow();
            
            int revisionNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST_H WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
            revisionNo++;
            
            resultSetHistory.moveToInsertRow();
            resultSetHistory.updateInt("REVISION_NO",revisionNo);
            resultSetHistory.updateInt("UPDATED_BY",getAttribute("UPDATED_BY").getInt());
            resultSetHistory.updateString("ENTRY_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSetHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            resultSetHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSetHistory.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
            resultSetHistory.updateString("PROCESSING_DATE", getAttribute("PROCESSING_DATE").getString());
            resultSetHistory.updateString("BALE_NO", ObjFeltPMDetail.getAttribute("BALE_NO").getString());
            resultSetHistory.updateString("BALE_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltPMDetail.getAttribute("BALE_DATE").getString()));
            resultSetHistory.updateString("PARTY_CODE", ObjFeltPMDetail.getAttribute("PARTY_CODE").getString());
            resultSetHistory.updateString("PARTY_NAME", ObjFeltPMDetail.getAttribute("PARTY_NAME").getString());
            resultSetHistory.updateString("CHARGE_CODE_NEW", getAttribute("CHARGE_CODE_NEW").getString());
            resultSetHistory.updateFloat("CRITICAL_LIMIT_NEW",(float)getAttribute("CRITICAL_LIMIT_NEW").getVal());
            resultSetHistory.updateBoolean("INSURANCE_CODE", getAttribute("INSURANCE_CODE").getBool());
            resultSetHistory.updateBoolean("WITHOUT_CRITICAL_LIMIT", getAttribute("WITHOUT_CRITICAL_LIMIT").getBool());
            resultSetHistory.updateBoolean("F6", ObjFeltPMDetail.getAttribute("F6").getBool());
            resultSetHistory.updateString("TRANSPORTER_CODE", getAttribute("TRANSPORTER_CODE").getString());
            resultSetHistory.updateString("REMARKS", ObjFeltPMDetail.getAttribute("REMARKS").getString());
            resultSetHistory.updateString("VEHICLE_NO",getAttribute("VEHICLE_NO").getString());
            resultSetHistory.updateString("ADV_DOC_NO",getAttribute("ADV_DOC_NO").getString());
            resultSetHistory.updateFloat("ADV_AGN_INV_AMT",(float)getAttribute("ADV_AGN_INV_AMT").getVal());
            resultSetHistory.updateFloat("ADV_AGN_IGST_AMT",(float)getAttribute("ADV_AGN_IGST_AMT").getVal());
            resultSetHistory.updateFloat("ADV_AGN_SGST_AMT",(float)getAttribute("ADV_AGN_SGST_AMT").getVal());
            resultSetHistory.updateFloat("ADV_AGN_CGST_AMT",(float)getAttribute("ADV_AGN_CGST_AMT").getVal());
            resultSetHistory.updateFloat("ADV_AGN_GST_COMP_CESS_AMT",(float)getAttribute("ADV_AGN_GST_COMP_CESS_AMT").getVal());
            resultSetHistory.updateFloat("BILL_VALUE",(float)ObjFeltPMDetail.getAttribute("BILL_VALUE").getVal());
            resultSetHistory.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
            resultSetHistory.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSetHistory.updateBoolean("REJECTED",false);
            resultSetHistory.updateString("REJECTED_DATE","0000-00-00");
            resultSetHistory.updateInt("CANCELED",0);
            resultSetHistory.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            resultSetHistory.updateInt("CHANGED",1);
            resultSetHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
             ResultSet rsTmp=data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");
            
            resultSetHistory.updateString("FROM_IP",""+str_split[1]);
            resultSetHistory.insertRow();
            }
//            
//            String str1 = getAttribute("BALE_NO").getString();
//            String[] bno = str1.split(",");
//            
//            for (int i=0;i<bno.length;i++){
//                bno[i] = bno[i].replaceAll("[^\\w]", ",");
//                System.out.println("BALE_NO : "+bno[i]);
//                if(bno[i]=="")
//                {
//                    bno[i] = "0";
//                }
//                
//            baleDate = data.getStringValueFromDB("SELECT PKG_BALE_DATE FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_BALE_NO='"+bno[i]+"' AND APPROVED=1 AND CANCELED=0 AND BALE_REOPEN_FLG=0 AND INVOICE_FLG=0 ORDER BY PKG_BALE_DATE DESC");
//           
//            
//            resultSet.updateString("DOC_NO", getAttribute("DOC_NO").getString());
//            resultSet.updateString("DOC_DATE",getAttribute("DOC_DATE").getString());
//            resultSet.updateString("PROCESSING_DATE",getAttribute("PROCESSING_DATE").getString());
//            resultSet.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
//            resultSet.updateString("BALE_NO", bno[i]);
//            resultSet.updateString("BALE_DATE", baleDate);
//            resultSet.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
//           // resultSet.updateString("PIECE_NO", getAttribute("PIECE_NO").getString());
//            resultSet.updateString("CHARGE_CODE_NEW", getAttribute("CHARGE_CODE_NEW").getString());
//            resultSet.updateFloat("CRITICAL_LIMIT_NEW",(float)getAttribute("CRITICAL_LIMIT_NEW").getVal());
//            resultSet.updateBoolean("INSURANCE_CODE", getAttribute("INSURANCE_CODE").getBool());
//            resultSet.updateBoolean("WITHOUT_CRITICAL_LIMIT", getAttribute("WITHOUT_CRITICAL_LIMIT").getBool());
//            resultSet.updateBoolean("F6", getAttribute("F6").getBool());
//            resultSet.updateString("TRANSPORTER_CODE", getAttribute("TRANSPORTER_CODE").getString());
//            resultSet.updateString("REMARKS", getAttribute("REMARKS").getString());
//            resultSet.updateString("VEHICLE_NO",getAttribute("VEHICLE_NO").getString());
//            resultSet.updateString("ADV_DOC_NO",getAttribute("ADV_DOC_NO").getString());
//            resultSet.updateFloat("ADV_AGN_INV_AMT",(float)getAttribute("ADV_AGN_INV_AMT").getVal());
//            resultSet.updateFloat("ADV_AGN_IGST_AMT",(float)getAttribute("ADV_AGN_IGST_AMT").getVal());
//            resultSet.updateFloat("ADV_AGN_SGST_AMT",(float)getAttribute("ADV_AGN_SGST_AMT").getVal());
//            resultSet.updateFloat("ADV_AGN_CGST_AMT",(float)getAttribute("ADV_AGN_CGST_AMT").getVal());
//            resultSet.updateFloat("ADV_AGN_GST_COMP_CESS_AMT",(float)getAttribute("ADV_AGN_GST_COMP_CESS_AMT").getVal());
//            resultSet.updateFloat("BILL_VALUE",(float)getAttribute("BILL_VALUE").getVal());
//            resultSet.updateInt("MODIFIED_BY",getAttribute("MODIFIED_BY").getInt());
//            resultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
//            resultSet.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
//            resultSet.updateInt("CHANGED",1);
//            resultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
//            resultSet.updateRow();
//            
//            int revisionNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST_H WHERE  DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
//            revisionNo++;
//            
//            
//            resultSetHistory.moveToInsertRow();
//            resultSetHistory.updateInt("REVISION_NO",revisionNo);
//            resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
//            resultSetHistory.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
//            resultSetHistory.updateString("PROCESSING_DATE", getAttribute("PROCESSING_DATE").getString());
//            resultSetHistory.updateString("BALE_NO", bno[i]);
//            resultSetHistory.updateString("BALE_DATE", baleDate);
//            resultSetHistory.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
//            resultSetHistory.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
//            resultSetHistory.updateString("CHARGE_CODE_NEW", getAttribute("CHARGE_CODE_NEW").getString());
//            resultSetHistory.updateFloat("CRITICAL_LIMIT_NEW",(float)getAttribute("CRITICAL_LIMIT_NEW").getVal());
//            resultSetHistory.updateBoolean("INSURANCE_CODE", getAttribute("INSURANCE_CODE").getBool());
//            resultSetHistory.updateBoolean("WITHOUT_CRITICAL_LIMIT", getAttribute("WITHOUT_CRITICAL_LIMIT").getBool());
//            resultSetHistory.updateBoolean("F6", getAttribute("F6").getBool());
//            resultSetHistory.updateString("TRANSPORTER_CODE", getAttribute("TRANSPORTER_CODE").getString());
//            resultSetHistory.updateString("VEHICLE_NO",getAttribute("VEHICLE_NO").getString());
//            resultSetHistory.updateString("ADV_DOC_NO",getAttribute("ADV_DOC_NO").getString());
//            resultSetHistory.updateFloat("ADV_AGN_INV_AMT",(float)getAttribute("ADV_AGN_INV_AMT").getVal());
//            resultSetHistory.updateFloat("ADV_AGN_IGST_AMT",(float)getAttribute("ADV_AGN_IGST_AMT").getVal());
//            resultSetHistory.updateFloat("ADV_AGN_SGST_AMT",(float)getAttribute("ADV_AGN_SGST_AMT").getVal());
//            resultSetHistory.updateFloat("ADV_AGN_CGST_AMT",(float)getAttribute("ADV_AGN_CGST_AMT").getVal());
//            resultSetHistory.updateFloat("ADV_AGN_GST_COMP_CESS_AMT",(float)getAttribute("ADV_AGN_GST_COMP_CESS_AMT").getVal());
//            resultSetHistory.updateFloat("BILL_VALUE",(float)getAttribute("BILL_VALUE").getVal());
//            resultSetHistory.updateString("REMARKS", getAttribute("REMARKS").getString());
//            resultSetHistory.updateInt("MODIFIED_BY", getAttribute("MODIFIED_BY").getInt());
//            resultSetHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
//            resultSetHistory.updateBoolean("REJECTED",false);
//            resultSetHistory.updateString("REJECTED_DATE","0000-00-00");
//            resultSetHistory.updateInt("CANCELED",0);
//            resultSetHistory.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
//            resultSetHistory.updateInt("CHANGED",1);
//            resultSetHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
//            resultSetHistory.updateInt("UPDATED_BY",getAttribute("UPDATED_BY").getInt());
//            resultSetHistory.updateString("ENTRY_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
//            resultSetHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
//            resultSetHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
//             ResultSet rsTmp=data.getResult("SELECT USER()");
//            rsTmp.first();
//            String str = rsTmp.getString(1);
//            String str_split[] = str.split("@");
//            
//            resultSetHistory.updateString("FROM_IP",""+str_split[1]);
//            resultSetHistory.insertRow();
//            }
//            
            
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=754; //Felt Change Bale No
            ObjFeltProductionApprovalFlow.DocNo=getDocumentNo(getAttribute("DOC_NO").getString());
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.FELT_INV_PROCESS_VAR_GST";
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
                data.Execute("UPDATE PRODUCTION.FELT_INV_PROCESS_VAR_GST SET REJECTED=0,CHANGED=1 WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"' ");
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
            setData();
            resultSetDetail.close();
            statementDetail.close();
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FFELT_INV_PROCESS_VAR_GST WHERE DOC_NO='"+baleNo +"' AND PROCESSING_DATE='"+EITLERPGLOBAL.formatDateDB(baleDate) +"' AND APPROVED=1";
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if(rsTmp.getInt("COUNT")>0){  //Packing is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            }else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=754 AND USER_ID="+userID+" AND DOC_NO='"+ getAttribute("DOC_NO").getString()+"' AND STATUS='W'";
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getInt("COUNT")>0) {
                    strSQL = "DELETE FROM PRODUCTION.FFELT_INV_PROCESS_VAR_GST WHERE DOC_NO='"+baleNo+"' AND PROCESSING_DATE='"+EITLERPGLOBAL.formatDateDB(baleDate)+"'";
                    tmpStmt.executeUpdate(strSQL);
                    strSQL = "DELETE FROM PRODUCTION.FELT_REOPEN_BALE_DETAIL WHERE DOC_NO='"+baleNo+"' AND PROCESSING_DATE='"+EITLERPGLOBAL.formatDateDB(baleDate)+"'";
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE DOC_NO='"+ baleNo +"' AND PROCESSING_DATE='"+ baleDate +"' AND APPROVED=1";
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0){  //Item is Approved
                //Discard the request
                return false;
            }else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=754 AND USER_ID="+userID+" AND DOC_NO='"+baleNo+"' AND STATUS='W'";
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
            String strSql = "SELECT * FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE " + stringFindQuery;
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
            setAttribute("PROCESSING_DATE",resultSet.getString("PROCESSING_DATE"));
            //setAttribute("PARTY_CODE",resultSet.getString("PARTY_CODE"));
            //setAttribute("PARTY_NAME",resultSet.getString("PARTY_NAME"));
          //  setAttribute("BALE_NO",resultSet.getString("BALE_NO"));
           // String blNo = data.getStringValueFromDB("SELECT GROUP_CONCAT(BALE_NO) FROM PRODUCTION.FELT_F6_INVOICE_MODIFICATION_HEADER WHERE DOC_NO='"+resultSet.getString("DOC_NO")+"'");
          //  setAttribute("BALE_NO",blNo);
          //  setAttribute("BALE_DATE",resultSet.getString("BALE_DATE"));
            //setAttribute("CHARGE_CODE_NEW",resultSet.getString("CHARGE_CODE_NEW"));
            //setAttribute("CRITICAL_LIMIT_NEW",resultSet.getString("CRITICAL_LIMIT_NEW"));
            //setAttribute("INSURANCE_CODE",resultSet.getBoolean("INSURANCE_CODE"));
            //setAttribute("WITHOUT_CRITICAL_LIMIT",resultSet.getBoolean("WITHOUT_CRITICAL_LIMIT"));
            //setAttribute("F6",resultSet.getBoolean("F6"));
            //setAttribute("TRANSPORTER_CODE",resultSet.getString("TRANSPORTER_CODE"));
            //setAttribute("VEHICLE_NO",resultSet.getString("VEHICLE_NO"));
            //setAttribute("ADV_DOC_NO",resultSet.getString("ADV_DOC_NO"));
            //setAttribute("ADV_AGN_INV_AMT",resultSet.getString("ADV_AGN_INV_AMT"));
            //setAttribute("ADV_AGN_IGST_AMT",resultSet.getString("ADV_AGN_IGST_AMT"));
            //setAttribute("ADV_AGN_SGST_AMT",resultSet.getString("ADV_AGN_SGST_AMT"));
            //setAttribute("ADV_AGN_CGST_AMT",resultSet.getString("ADV_AGN_CGST_AMT"));
            //setAttribute("ADV_AGN_GST_COMP_CESS_AMT",resultSet.getString("ADV_AGN_GST_COMP_CESS_AMT"));
           // setAttribute("BILL_VALUE",resultSet.getString("BILL_VALUE"));
            setAttribute("REMARKS",resultSet.getString("REMARKS"));
            setAttribute("HIERARCHY_ID",resultSet.getInt("HIERARCHY_ID"));
         
            hmFeltBaleNoDetails.clear();
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE DOC_NO='" + resultSet.getString("DOC_NO") + "' ORDER BY PARTY_CODE");
            
            while (resultSetTemp.next()) {
                serialNoCounter++;
                clsFeltInvoiceParameterModificationf6FormDetail ObjFeltGSTAdvancePaymentEntryForm = new clsFeltInvoiceParameterModificationf6FormDetail();

                ObjFeltGSTAdvancePaymentEntryForm.setAttribute("F6",resultSetTemp.getBoolean("F6"));
                ObjFeltGSTAdvancePaymentEntryForm.setAttribute("PARTY_CODE", resultSetTemp.getString("PARTY_CODE"));
                ObjFeltGSTAdvancePaymentEntryForm.setAttribute("PARTY_NAME", resultSetTemp.getString("PARTY_NAME"));
                ObjFeltGSTAdvancePaymentEntryForm.setAttribute("BALE_NO", resultSetTemp.getString("BALE_NO"));
                ObjFeltGSTAdvancePaymentEntryForm.setAttribute("BALE_DATE", resultSetTemp.getString("BALE_DATE"));
                ObjFeltGSTAdvancePaymentEntryForm.setAttribute("BILL_VALUE", resultSetTemp.getFloat("BILL_VALUE"));
                ObjFeltGSTAdvancePaymentEntryForm.setAttribute("REMARKS", resultSetTemp.getFloat("REMARKS"));
                hmFeltBaleNoDetails.put(Integer.toString(serialNoCounter), ObjFeltGSTAdvancePaymentEntryForm);
         }
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
            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST_H WHERE DOC_DATE='"+ baleDate+"' AND DOC_NO='"+baleNo+"'");
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
            rsTmp=stTmp.executeQuery("SELECT MIN(REVISION_NO) AS REVISION_NO,UPDATED_BY,ENTRY_DATE,APPROVAL_STATUS,APPROVER_REMARKS,FROM_IP FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST_H WHERE DOC_DATE='"+dpDate+"' AND DOC_NO='"+dpNo+"' GROUP BY UPDATED_BY,ENTRY_DATE,APPROVAL_STATUS,APPROVER_REMARKS ORDER BY REVISION_NO,UPDATED_BY,ENTRY_DATE,APPROVAL_STATUS,APPROVER_REMARKS");
            
            while(rsTmp.next()) {
                clsFeltInvoiceParameterModificationf6Form ObjFeltReopenBale=new clsFeltInvoiceParameterModificationf6Form();
                
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
                strSQL="SELECT DISTINCT H.DOC_DATE,H.DOC_NO,D.RECEIVED_DATE,PARTY_CODE FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=754 AND CANCELED=0 ORDER BY RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT DISTINCT H.DOC_DATE,H.DOC_NO,D.RECEIVED_DATE,PARTY_CODE FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=754 AND CANCELED=0 ORDER BY DOC_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT DISTINCT H.DOC_DATE,H.DOC_NO,D.RECEIVED_DATE,PARTY_CODE FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=754 AND CANCELED=0 ORDER BY DOC_NO";
            }
            
            ResultSet rsTmp=data.getConn().createStatement().executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp.next()) {
                
                Counter=Counter+1;
                clsFeltInvoiceParameterModificationf6Form ObjDoc=new clsFeltInvoiceParameterModificationf6Form();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                ObjDoc.setAttribute("DOC_DATE",rsTmp.getString("DOC_DATE"));
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
        int count=data.getIntValueFromDB("SELECT COUNT(PKG_PIECE_NO) FROM PRODUCTION.FELT_REOPEN_BALE_DETAIL WHERE PKG_PIECE_NO='"+pieceNo+"' AND PKG_PROCESSING_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PKG_PROCESSING_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
        if(count>0) return true;
        else return false;
    }
    
    // checks piece no already exist in db in edit mode
    public boolean checkPieceNoInDB(String pieceNo, String baleNo, String baleDate){
        int count=data.getIntValueFromDB("SELECT COUNT(PKG_PIECE_NO) FROM PRODUCTION.FELT_REOPEN_BALE_DETAIL WHERE PKG_PIECE_NO='"+pieceNo+"' AND PKG_PROCESSING_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PKG_PROCESSING_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
        if(count>=1){
            int counter=0;
            try{
                Connection Conn=data.getConn();
                Statement stTmp=Conn.createStatement();
                ResultSet rsTmp=stTmp.executeQuery("SELECT PKG_PROCESSING_DATE,DOC_NO FROM PRODUCTION.FELT_REOPEN_BALE_DETAIL WHERE PKG_PIECE_NO='"+pieceNo+"' AND PKG_PROCESSING_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PKG_PROCESSING_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
                while(rsTmp.next()){
                    if(rsTmp.getString("PKG_PROCESSING_DATE").equals(EITLERPGLOBAL.formatDateDB(baleDate)) && rsTmp.getString("DOC_NO").equals(baleNo)) counter++;
                }
            }catch(SQLException e){e.printStackTrace();}
            if(counter==1 && count>=2) return true;
            else if(counter==1) return false;
            else return true;
        }else return false;
    }
    
    public static String getStation(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT PKG_STATION FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_DOC_NO="+pPartyID);
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
            rsTmp=stTmp.executeQuery("SELECT PKG_PROCESSING_DATE FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_DOC_NO="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                PartyName=rsTmp.getString("PKG_PROCESSING_DATE");
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
            rsTmp=stTmp.executeQuery("SELECT PKG_PARTY_CODE FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_BALE_NO="+pPartyID);
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
            rsTmp=stTmp.executeQuery("SELECT PKG_BOX_SIZE FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_DOC_NO="+pPartyID);
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
            rsTmp=stTmp.executeQuery("SELECT PKG_MODE_PACKING FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_DOC_NO="+pPartyID);
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
            rsTmp=stTmp.executeQuery("SELECT PKG_TRANSPORT_MODE FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_DOC_NO="+pPartyID);
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
     public static String getParyName(int pCompanyID, String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            rsTmp = stTmp.executeQuery("SELECT PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE=" + pPartyID);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                PartyName = rsTmp.getString("PARTY_NAME");
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

        } catch (Exception e) {

        }
        return PartyName;
    }
     public static String getChargeCode(int pCompanyID, String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String ChargeCode = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            rsTmp = stTmp.executeQuery("SELECT CHARGE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE=" + pPartyID);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                ChargeCode = rsTmp.getString("CHARGE_CODE");
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

        } catch (Exception e) {

        }
        return ChargeCode;
    }
     public static String gettransportid(int pCompanyID, String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String ChargeCode = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            rsTmp = stTmp.executeQuery("SELECT TRANSPORTER_ID FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE=" + pPartyID);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                ChargeCode = rsTmp.getString("TRANSPORTER_ID");
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

        } catch (Exception e) {

        }
        return ChargeCode;
    }
     public static String getCriticalLimit(int pCompanyID, String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String ChargeCode = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            rsTmp = stTmp.executeQuery("SELECT AMOUNT_LIMIT FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE=" + pPartyID);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                ChargeCode = rsTmp.getString("AMOUNT_LIMIT");
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

        } catch (Exception e) {

        }
        return ChargeCode;
    }
     
//     public static String getBillValue(String pPartyID) {
//        Connection tmpConn;
//        Statement stTmp;
//        ResultSet rsTmp;
//        String BillValue = "";
//
//        try {
//            tmpConn = data.getConn();
//            stTmp = tmpConn.createStatement();
//            rsTmp = stTmp.executeQuery("SELECT D.INV_VAL FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B,PRODUCTION.FELT_PROFORMA_INVOICE_HEADER C,PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL D WHERE A.PKG_BALE_NO=B.PKG_BALE_NO AND A.PKG_PARTY_CODE=C.PARTY_CD AND B.PKG_PIECE_NO=D.PIECE_NO AND A.PKG_PARTY_CODE=" + pPartyID);
//            rsTmp.first();
//
//            if (rsTmp.getRow() > 0) {
//                BillValue = rsTmp.getString("INV_VAL");
//            }
//
//            //tmpConn.close();
//            stTmp.close();
//            rsTmp.close();
//
//        } catch (Exception e) {
//
//        }
//        return BillValue;
//    }
     
     public static void CancelDoc(String pAmendNo) {
        ResultSet rsTmp=null;
        
        if(CanCancel(pAmendNo)) {
            
            boolean Approved=false;
            
            try {
                rsTmp=data.getResult("SELECT APPROVED FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE DOC_NO='"+pAmendNo+"' ");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    Approved=rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();
                
                if(!Approved) {
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='"+pAmendNo+"' AND MODULE_ID=754");
                }
                data.Execute("UPDATE PRODUCTION.FELT_INV_PROCESS_VAR_GST SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='"+pAmendNo+"'");
                
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
            System.out.println("SELECT DOC_NO FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE DOC_NO='"+pDocNo+"'  AND APPROVED=0 AND CANCELED=0");
            rsTmp=stTmp.executeQuery("SELECT DOC_NO FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE DOC_NO='"+pDocNo+"'  AND APPROVED=0 AND CANCELED=0");
            
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
    
 public static boolean CancelDoc(int pCompanyId, String pDocNo) {

        ResultSet rsTmp = null, rsIndent = null;
        boolean Cancelled = false;

        try {
            if (CanCancel(pCompanyId, pDocNo)) {

                boolean ApprovedDoc = false;

                rsTmp = data.getResult("SELECT APPROVED FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE DOC_NO='" + pDocNo + "'");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    ApprovedDoc = rsTmp.getBoolean("APPROVED");
                }

                if (ApprovedDoc) {
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='" + pDocNo + "' AND MODULE_ID=" + (clsFeltInvoiceParameterModificationf6Form.ModuleID));                    
                }

                //Now Update the header with cancel falg to true
                data.Execute("UPDATE PRODUCTION.FELT_INV_PROCESS_VAR_GST SET APPROVED=0,APPROVED_DATE='0000-00-00',CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='" + pDocNo + "'");
 
                Cancelled = true;
            }
            rsTmp.close();
            rsIndent.close();
        } catch (Exception e) {
        }
        return Cancelled;
    }
 
 public static boolean CanCancel(int pCompanyId, String pDocNo) {
        ResultSet rsTmp = null;
        boolean canCancel = false;

        try {
            rsTmp = data.getResult("SELECT DOC_NO FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE DOC_NO='" + pDocNo + "' AND CANCELED=0");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                canCancel = true;
            }

            rsTmp.close();
        } catch (Exception e) {
        }
        return canCancel;
    }
   
 public static boolean IsFinalDocExist(String pDocNo) {
        return data.IsRecordExist("SELECT DOC_NO FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE DOC_NO='" + pDocNo + "' AND APPROVED=1 AND CANCELED=0");
    }
 
 public static String getBaleDate(String pcode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String partyname="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PKG_BALE_DATE FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_BALE_NO='"+pcode+"' AND APPROVED=1 AND CANCELED=0 AND BALE_REOPEN_FLG=0 AND INVOICE_FLG=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                partyname=rsTmp.getString("PKG_BALE_DATE");
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
    
}
