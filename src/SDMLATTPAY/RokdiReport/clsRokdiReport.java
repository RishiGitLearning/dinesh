/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SDMLATTPAY.RokdiReport;

import EITLERP.ComboData;
import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.common.Order_No_Conversion;
import EITLERP.JavaMail.JMail;
import EITLERP.Variant;
import EITLERP.clsHierarchy;
import EITLERP.clsUser;
import EITLERP.data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 *
 * @author DAXESH PRAJAPATI
 *
 */
public class clsRokdiReport {

    public String LastError = "";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet, rsHeader;
    //private ResultSet rsResultSet,

    public HashMap props;
    public boolean Ready = false;
    //Felt Order Collection
    

    //History Related properties
    public boolean HistoryView = false;
    private String historyAmendDate = "";
    private String historyAmendID = "";
    private static int ModuleID = 812;

    public HashMap hmRokdiDetails;
    
    public Variant getAttribute(String PropName) {
        return (Variant) props.get(PropName);
    }

    public void setAttribute(String PropName, Object Value) {
        props.put(PropName, new Variant(Value));
    }

    public void setAttribute(String PropName, int Value) {
        props.put(PropName, new Variant(Value));
    }

    public void setAttribute(String PropName, long Value) {
        props.put(PropName, new Variant(Value));
    }

    public void setAttribute(String PropName, double Value) {
        props.put(PropName, new Variant(Value));
    }

    public void setAttribute(String PropName, float Value) {
        props.put(PropName, new Variant(Value));
    }

    public void setAttribute(String PropName, boolean Value) {
        props.put(PropName, new Variant(Value));
    }

    /**
     * Creates new Data Felt Order Updation
     */
    public clsRokdiReport() {
        LastError = "";
        props = new HashMap();
        props.put("RP_DOC_NO", new Variant(""));
        props.put("RP_DOC_DATE", new Variant(""));
        props.put("REGION", new Variant(""));
        props.put("SALES_ENGINEER", new Variant(""));
        props.put("PARTY_CODE", new Variant(""));
        props.put("PARTY_NAME", new Variant(""));
        props.put("REFERENCE", new Variant(""));
        props.put("REFERENCE_DATE", new Variant(""));
        props.put("P_O_NO", new Variant(""));
        props.put("P_O_DATE", new Variant(""));
        props.put("REMARK", new Variant(""));
        props.put("OLD_PIECE", new Variant(false));
        props.put("TENDER_PARTY", new Variant(false));

        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("PREFIX", new Variant(""));

        props.put("RP_DOC_NO", new Variant(""));
        props.put("RP_DOC_DATE", new Variant(""));
        props.put("MODULE_ID", new Variant(""));
        props.put("USER_ID", new Variant(""));
        props.put("RECEIVED_DATE", new Variant(""));
        // -- Approval Specific Fields --
        props.put("HIERARCHY_ID", new Variant(0));
        props.put("FROM", new Variant(0));
        props.put("TO", new Variant(0));
        props.put("REJECTED_REMARKS", new Variant(""));
        props.put("REMARKS", new Variant(""));
        props.put("APPROVAL_STATUS", new Variant(""));
        props.put("APPROVER_REMARKS", new Variant(0));
        props.put("ENTRY_DATE", new Variant(0));

        props.put("CGST_PER", new Variant(0));
        props.put("CGST_AMT", new Variant(0));
        props.put("SGST_PER", new Variant(0));
        props.put("SGST_AMT", new Variant(0));
        props.put("IGST_PER", new Variant(0));
        props.put("IGST_AMT", new Variant(0));
        props.put("COMPOSITION_PER", new Variant(0));
        props.put("COMPOSITION_AMT", new Variant(0));
        props.put("RCM_PER", new Variant(0));
        props.put("RCM_AMT", new Variant(0));
        props.put("GST_COMPENSATION_CESS_PER", new Variant(0));
        props.put("GST_COMPENSATION_CESS_AMT", new Variant(0));

        hmRokdiDetails = new HashMap();
    }

    public boolean LoadData() {
        Ready = false;
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery("SELECT * FROM SDMLATTPAY.ATT_ROKDI_PUNCH_REPORT_HEADER ORDER BY RP_DOC_DATE");
            HistoryView = false;
            Ready = true;
            MoveLast();

            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    public void Close() {
        try {
            statement.close();
            resultSet.close();
        } catch (Exception e) {
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
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    public boolean MoveNext() {
        try {
            if (resultSet.isAfterLast() || resultSet.isLast()) {
                //Move pointer at last record if it is beyond eof
                resultSet.last();
            } else {
                resultSet.next();
                if (resultSet.isAfterLast()) {
                    resultSet.last();
                }
            }
//            if(HistoryView) setHistoryData(historyAmendDate, historyAmendID);
//            else 

            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    public boolean MovePrevious() {
        try {
            if (resultSet.isFirst() || resultSet.isBeforeFirst()) {
                resultSet.first();
            } else {
                resultSet.previous();
                if (resultSet.isBeforeFirst()) {
                    resultSet.first();
                }
            }
//            if(HistoryView) setHistoryData(historyAmendDate, historyAmendID);
//            else 

            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
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
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    public boolean Insert() {
        ResultSet resultSetTemp, resultSetHistory, rsHeader, rsHeaderH;
        Statement statementTemp, statementHistory, stHeader, stHeaderH;
        try {
            // Felt Order Updation data connection
            connection = data.getConn();
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  SDMLATTPAY.ATT_ROKDI_PUNCH_REPORT_DETAIL WHERE RP_DOC_NO='1'");

            // Felt order Updation data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM SDMLATTPAY.ATT_ROKDI_PUNCH_REPORT_DETAIL_H WHERE RP_DOC_NO='1'");

            stHeader = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHeader = stHeader.executeQuery("SELECT * FROM  SDMLATTPAY.ATT_ROKDI_PUNCH_REPORT_HEADER WHERE RP_DOC_NO='1'");

            stHeaderH = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHeaderH = stHeaderH.executeQuery("SELECT * FROM  SDMLATTPAY.ATT_ROKDI_PUNCH_REPORT_HEADER_H WHERE RP_DOC_NO='1'");

            //setAttribute("RP_DOC_NO",);
            rsHeader.first();
            rsHeader.moveToInsertRow();

            rsHeader.updateString("RP_DOC_NO", getAttribute("RP_DOC_NO").getString());
            rsHeader.updateString("RP_DOC_DATE", getAttribute("RP_DOC_DATE").getString());
            rsHeader.updateString("FROM_DATE", getAttribute("FROM_DATE").getString());
            rsHeader.updateString("TO_DATE", getAttribute("TO_DATE").getString());
            
            
            rsHeader.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeader.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsHeader.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            rsHeader.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeader.updateInt("MODIFIED_BY", 0);
            rsHeader.updateString("MODIFIED_DATE", "0000-00-00");
            rsHeader.updateBoolean("APPROVED", false);
            rsHeader.updateString("APPROVED_DATE", "0000-00-00");
            rsHeader.updateBoolean("CANCELED", false);
            rsHeader.updateBoolean("REJECTED", false);
            rsHeader.updateString("REJECTED_DATE", "0000-00-00");
            rsHeader.updateBoolean("CHANGED", false);
            rsHeader.updateString("CHANGED_DATE", EITLERPGLOBAL.formatDateDB("0000-00-00"));

            rsHeader.insertRow();

            rsHeaderH.first();
            rsHeaderH.moveToInsertRow();

            rsHeaderH.updateInt("REVISION_NO", 1);

            rsHeaderH.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsHeaderH.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            rsHeaderH.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
            rsHeaderH.updateString("RP_DOC_NO", getAttribute("RP_DOC_NO").getString());
            rsHeaderH.updateString("RP_DOC_DATE", getAttribute("RP_DOC_DATE").getString());
            rsHeaderH.updateString("FROM_DATE", getAttribute("FROM_DATE").getString());
            rsHeaderH.updateString("TO_DATE", getAttribute("TO_DATE").getString());
            
            //rsHeaderH.updateBoolean("EMAIL_UPDATE_TO_PM", getAttribute("EMAIL_UPDATE_TO_PM").getBool());

            rsHeaderH.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            rsHeaderH.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateString("UPDATED_BY", "0");
            rsHeaderH.updateString("UPDATED_DATE", EITLERPGLOBAL.getCurrentDateTimeDB());
            rsHeaderH.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsHeaderH.updateString("UPDATED_BY", EITLERPGLOBAL.gNewUserID + "");
            rsHeaderH.updateString("UPDATED_DATE", EITLERPGLOBAL.getCurrentDateTimeDB());
            rsHeaderH.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            rsHeaderH.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateInt("MODIFIED_BY", 0);
            rsHeaderH.updateString("MODIFIED_DATE", "0000-00-00");
            rsHeaderH.updateBoolean("APPROVED", false);
            rsHeaderH.updateString("APPROVED_DATE", "0000-00-00");
            rsHeaderH.updateBoolean("CANCELED", false);
            rsHeaderH.updateBoolean("REJECTED", false);
            rsHeaderH.updateString("REJECTED_DATE", "0000-00-00");
            rsHeaderH.updateBoolean("CHANGED", false);
            rsHeaderH.updateString("CHANGED_DATE", "0000-00-00");

            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");

            rsHeaderH.updateString("FROM_IP", "" + str_split[1]);

            rsHeaderH.insertRow();

            //Now Insert records into FELT_CN_TEMP_HEADER & History tables
            for (int i = 1; i <= hmRokdiDetails.size(); i++) {
                clsRokdiReportDetails ObjRDetails = (clsRokdiReportDetails) hmRokdiDetails.get(Integer.toString(i));

                //Insert records into Felt order Amendment table
                resultSetTemp.moveToInsertRow();

                resultSetTemp.updateInt("SR_NO", i);
                resultSetTemp.updateString("RP_DOC_NO", ObjRDetails.getAttribute("RP_DOC_NO").getString());
                resultSetTemp.updateString("EMP_NO", ObjRDetails.getAttribute("EMP_NO").getString());
                resultSetTemp.updateString("EMP_NAME", ObjRDetails.getAttribute("EMP_NAME").getString());
                resultSetTemp.updateString("DEPARTMENT", ObjRDetails.getAttribute("DEPARTMENT").getString());
                resultSetTemp.updateString("FROM_DATE_DETAIL", ObjRDetails.getAttribute("FROM_DATE_DETAIL").getString());
                resultSetTemp.updateString("FROM_PUNCH", ObjRDetails.getAttribute("FROM_PUNCH").getString());
                resultSetTemp.updateString("TO_DATE_DETAIL", ObjRDetails.getAttribute("TO_DATE_DETAIL").getString());
                resultSetTemp.updateString("TO_PUNCH", ObjRDetails.getAttribute("TO_PUNCH").getString());
                resultSetTemp.updateString("DIFFRENCE", ObjRDetails.getAttribute("DIFFRENCE").getString());
                resultSetTemp.insertRow();
                
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO", 1);
                resultSetHistory.updateInt("SR_NO", i);
                resultSetHistory.updateString("RP_DOC_NO", ObjRDetails.getAttribute("RP_DOC_NO").getString());
                resultSetHistory.updateString("EMP_NO", ObjRDetails.getAttribute("EMP_NO").getString());
                resultSetHistory.updateString("EMP_NAME", ObjRDetails.getAttribute("EMP_NAME").getString());
                resultSetHistory.updateString("DEPARTMENT", ObjRDetails.getAttribute("DEPARTMENT").getString());
                resultSetHistory.updateString("FROM_DATE_DETAIL", ObjRDetails.getAttribute("FROM_DATE_DETAIL").getString());
                resultSetHistory.updateString("FROM_PUNCH", ObjRDetails.getAttribute("FROM_PUNCH").getString());
                resultSetHistory.updateString("TO_DATE_DETAIL", ObjRDetails.getAttribute("TO_DATE_DETAIL").getString());
                resultSetHistory.updateString("TO_PUNCH", ObjRDetails.getAttribute("TO_PUNCH").getString());
                resultSetHistory.updateString("DIFFRENCE", ObjRDetails.getAttribute("DIFFRENCE").getString());
                resultSetHistory.insertRow();
                
                
            }

            //======== Update the Approval Flow =========
            SDMLATTPAY.ApprovalFlow ObjApprovalFlow = new SDMLATTPAY.ApprovalFlow();
            ObjApprovalFlow.ModuleID = ModuleID; //Felt Weaving Entry
            ObjApprovalFlow.DocNo = getAttribute("RP_DOC_NO").getString();
            ObjApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjApprovalFlow.TableName = "SDMLATTPAY.ATTPSDAY_EMPMST";
            ObjApprovalFlow.IsCreator = true;
            ObjApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjApprovalFlow.Remarks = getAttribute("REJECTED_REMARKS").getString();
            ObjApprovalFlow.FieldName = "RP_DOC_NO";
            ObjApprovalFlow.CompanyID = EITLERPGLOBAL.gCompanyID;
            //JOptionPane.showMessageDialog(null, "TO : "+(int)getAttribute("TO").getVal()+", Approval Status = "+(String)getAttribute("APPROVAL_STATUS").getObj());
//            if("A".equals((String)getAttribute("APPROVAL_STATUS").getObj()))
//            {   
//                    String Subject = "Felt Order Pending Document : "+getAttribute("RP_DOC_NO").getString();
//                    String Message = "Document No : "+getAttribute("RP_DOC_NO").getString()+" is added in your PENDING DOCUMENT"
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
//            }
            if (ObjApprovalFlow.Status.equals("H")) {
                ObjApprovalFlow.Status = "A";
                ObjApprovalFlow.To = ObjApprovalFlow.From;
                ObjApprovalFlow.UpdateFlow();
            } else {
                if (!ObjApprovalFlow.UpdateFlow()) {
                    LastError = ObjApprovalFlow.LastError;
                }
            }

            
            LoadData();

            
            return true;

        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    //Updates current record
    public boolean Update() {

        ResultSet resultSetTemp, resultSetHistory, rsHeader, rsHeaderH, rsRegister;
        Statement statementTemp, statementHistory, stHeader, stHeaderH, stRegister;
        int revisionNo;
        try {
            // Production data connection

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  SDMLATTPAY.ATT_ROKDI_PUNCH_REPORT_DETAIL WHERE RP_DOC_NO='1'");

            // Felt order Updation data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM SDMLATTPAY.ATT_ROKDI_PUNCH_REPORT_DETAIL_H WHERE RP_DOC_NO='1'");
            
            stHeaderH = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHeaderH = stHeaderH.executeQuery("SELECT * FROM  SDMLATTPAY.ATT_ROKDI_PUNCH_REPORT_HEADER_H WHERE RP_DOC_NO=''");

            //Now Update records into FELT_CN_TEMP_HEADER tables
            resultSet.updateString("RP_DOC_NO", getAttribute("RP_DOC_NO").getString());
            resultSet.updateString("RP_DOC_DATE", (String) getAttribute("RP_DOC_DATE").getObj());
            
            resultSet.updateString("FROM_DATE", getAttribute("FROM_DATE").getString());
            resultSet.updateString("TO_DATE", getAttribute("TO_DATE").getString());
            
            
            
            resultSet.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            resultSet.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
            resultSet.updateString("MODIFIED_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));

            if (getAttribute("APPROVAL_STATUS").getString().equals("F")) {
                resultSet.updateBoolean("APPROVED", true);
                resultSet.updateString("APPROVED_BY", EITLERPGLOBAL.gNewUserID + "");
                resultSet.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            } else {
                resultSet.updateBoolean("APPROVED", false);
                resultSet.updateString("APPROVED_DATE", "0000-00-00");
            }
            resultSet.updateBoolean("CANCELED", false);
            resultSet.updateBoolean("REJECTED", false);
            resultSet.updateString("REJECTED_DATE", "0000-00-00");
            //resultSet.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            resultSet.updateBoolean("CHANGED", true);
            resultSet.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            try {
                resultSet.updateRow();
            } catch (Exception e) {
                System.out.println("Header Updation Failed : " + e.getMessage());
            }
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM SDMLATTPAY.ATT_ROKDI_PUNCH_REPORT_HEADER_H WHERE RP_DOC_NO='" + getAttribute("RP_DOC_NO").getString() + "'");

            RevNo++;

            rsHeaderH.moveToInsertRow();

            rsHeaderH.updateString("REVISION_NO", RevNo + "");
            rsHeaderH.updateString("RP_DOC_NO", getAttribute("RP_DOC_NO").getString() + "");
            rsHeaderH.updateString("RP_DOC_DATE", getAttribute("RP_DOC_DATE").getString());
           
            rsHeaderH.updateString("FROM_DATE", getAttribute("FROM_DATE").getString());
            rsHeaderH.updateString("TO_DATE", getAttribute("TO_DATE").getString());
            
           
            rsHeaderH.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            rsHeaderH.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
            rsHeaderH.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateString("UPDATED_BY", EITLERPGLOBAL.gNewUserID + "");
            rsHeaderH.updateString("UPDATED_DATE", EITLERPGLOBAL.getCurrentDateTimeDB());
            rsHeaderH.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsHeaderH.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsHeaderH.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            rsHeaderH.updateString("APPROVER_REMARKS", getAttribute("FROM_REMARKS").getString());
            if (getAttribute("APPROVAL_STATUS").getString().equals("F")) {
                rsHeaderH.updateBoolean("APPROVED", true);
                rsHeaderH.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            } else {
                rsHeaderH.updateBoolean("APPROVED", false);
                rsHeaderH.updateString("APPROVED_DATE", "0000-00-00");
            }
            rsHeaderH.updateBoolean("CANCELED", false);
            rsHeaderH.updateBoolean("REJECTED", false);
            rsHeaderH.updateString("REJECTED_DATE", "0000-00-00");
            rsHeaderH.updateBoolean("CHANGED", true);
            rsHeaderH.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");
            rsHeaderH.updateString("FROM_IP", "" + str_split[1]);

            rsHeaderH.insertRow();
           
            //Now Insert records into FELT_CN_TEMP_HEADER & History tables
            for (int i = 1; i <= hmRokdiDetails.size(); i++) {
                clsRokdiReportDetails ObjRDetails = (clsRokdiReportDetails) hmRokdiDetails.get(Integer.toString(i));

                //Insert records into Felt order Amendment table
                resultSetTemp.moveToInsertRow();

                resultSetTemp.updateInt("SR_NO", i);
                resultSetTemp.updateString("RP_DOC_NO", ObjRDetails.getAttribute("RP_DOC_NO").getString());
                resultSetTemp.updateString("EMP_NO", ObjRDetails.getAttribute("EMP_NO").getString());
                resultSetTemp.updateString("EMP_NAME", ObjRDetails.getAttribute("EMP_NAME").getString());
                resultSetTemp.updateString("DEPARTMENT", ObjRDetails.getAttribute("DEPARTMENT").getString());
                resultSetTemp.updateString("FROM_DATE_DETAIL", ObjRDetails.getAttribute("FROM_DATE_DETAIL").getString());
                resultSetTemp.updateString("FROM_PUNCH", ObjRDetails.getAttribute("FROM_PUNCH").getString());
                resultSetTemp.updateString("TO_DATE_DETAIL", ObjRDetails.getAttribute("TO_DATE_DETAIL").getString());
                resultSetTemp.updateString("TO_PUNCH", ObjRDetails.getAttribute("TO_PUNCH").getString());
                resultSetTemp.updateString("DIFFRENCE", ObjRDetails.getAttribute("DIFFRENCE").getString());
                resultSetTemp.insertRow();
                
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO", 1);
                resultSetHistory.updateInt("SR_NO", i);
                resultSetHistory.updateString("EMP_NO", ObjRDetails.getAttribute("EMP_NO").getString());
                resultSetHistory.updateString("EMP_NAME", ObjRDetails.getAttribute("EMP_NAME").getString());
                resultSetHistory.updateString("DEPARTMENT", ObjRDetails.getAttribute("DEPARTMENT").getString());
                resultSetHistory.updateString("FROM_DATE_DETAIL", ObjRDetails.getAttribute("FROM_DATE_DETAIL").getString());
                resultSetHistory.updateString("FROM_PUNCH", ObjRDetails.getAttribute("FROM_PUNCH").getString());
                resultSetHistory.updateString("TO_DATE_DETAIL", ObjRDetails.getAttribute("TO_DATE_DETAIL").getString());
                resultSetHistory.updateString("TO_PUNCH", ObjRDetails.getAttribute("TO_PUNCH").getString());
                resultSetHistory.updateString("DIFFRENCE", ObjRDetails.getAttribute("DIFFRENCE").getString());
                resultSetHistory.insertRow();
                
                
            }

            //======== Update the Approval Flow =========
            SDMLATTPAY.ApprovalFlow ObjApprovalFlow = new SDMLATTPAY.ApprovalFlow();
            
            ObjApprovalFlow.ModuleID = ModuleID; //Felt Weaving Entry
            ObjApprovalFlow.DocNo = getAttribute("RP_DOC_NO").getString();
            ObjApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getString();
            ObjApprovalFlow.TableName = "SDMLATTPAY.ATT_ROKDI_PUNCH_REPORT_HEADER";
            ObjApprovalFlow.IsCreator = true;
            ObjApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjApprovalFlow.Remarks = getAttribute("FROM_REMARKS").getString();
            ObjApprovalFlow.FieldName = "RP_DOC_NO";
            ObjApprovalFlow.CompanyID = EITLERPGLOBAL.gCompanyID;
            if (getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjApprovalFlow.To = (int) getAttribute("SEND_DOC_TO").getVal();
                ObjApprovalFlow.ExplicitSendTo = true;
            }
            //if(getAttribute("APPROVAL_STATUS").getString().equals("A") || getAttribute("APPROVAL_STATUS").getString().equals("R")) {
//                    String Subject = "Felt Order Pending Document : "+getAttribute("RP_DOC_NO").getString();
//                    String Message = "Document No : "+getAttribute("RP_DOC_NO").getString()+" is added in your PENDING DOCUMENT"
//                            + "\n\n\n\n SDML-ERP : http://200.0.0.230:8080/SDMLERP";
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
            boolean IsRejected = getAttribute("REJECTED").getBool();

            if (IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE SDMLATTPAY.ATT_ROKDI_PUNCH_REPORT_HEADER SET REJECTED=0,CHANGED=1 WHERE RP_DOC_NO ='" + getAttribute("RP_DOC_NO").getString() + "'");
                ObjApprovalFlow.IsCreator = true;
            }
            //==========================================//

            if (ObjApprovalFlow.Status.equals("H")) {
                if (IsRejected) {
                    ObjApprovalFlow.Status = "A";
                    ObjApprovalFlow.To = ObjApprovalFlow.From;
                    ObjApprovalFlow.UpdateFlow();
                }
            } else {
                if (!ObjApprovalFlow.UpdateFlow()) {
                    LastError = ObjApprovalFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------

            // Update  in Order Master Table To confirm that Weaving has completed
            if (ObjApprovalFlow.Status.equals("F") ) {

                
            }

            setData();
            
            
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This routine checks whether the item is deletable or not. Criteria is
     * Approved item cannot be delete, and if not approved then user id is
     * checked whether doucment is created by the user. Only creator can delete
     * the document. After checking it deletes the record of selected production
     * date and document no.
     */
    public boolean CanDelete(String documentNo, String stringProductionDate, int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";

        try {
            tmpStmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.ATT_ROKDI_PUNCH_REPORT_HEADER WHERE  RP_DOC_NO='" + documentNo + "' AND APPROVED=" + 1;
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=" + ModuleID + " AND USER_ID=" + userID + " AND RP_DOC_NO='" + documentNo + "' AND STATUS='W'";
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();

                if (rsTmp.getInt("COUNT") > 0) {
                    strSQL = "DELETE FROM SDMLATTPAY.ATT_ROKDI_PUNCH_REPORT_HEADER WHERE "
                            + " RP_DOC_DATE = '" + EITLERPGLOBAL.formatDateDB(stringProductionDate) + "'"
                            + " AND RP_DOC_NO ='" + documentNo + "'";

                    tmpStmt.executeUpdate(strSQL);
                    LoadData();
                    return true;
                } else {
                    LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                    return false;
                }
            }
        } catch (Exception e) {
            LastError = "Error occured while deleting." + e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This routine checks and returns whether the item is editable or not.
     * Criteria is Approved item cannot be changed and if not approved then user
     * id is checked whether doucment is waiting for his approval.
     */
    public boolean IsEditable(String orderupdDocumentNo, int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";
        try {
            if (HistoryView) {
                return false;
            }
            tmpStmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //First check that document is approved or not
            strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.ATT_ROKDI_PUNCH_REPORT_HEADER WHERE RP_DOC_NO='" + orderupdDocumentNo + "' AND APPROVED=1";
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=" + ModuleID + " AND USER_ID=" + userID + " AND DOC_NO='" + orderupdDocumentNo + "' AND STATUS='W'";
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();

                if (rsTmp.getInt("COUNT") > 0) {
                    //Yes document is waiting for this user
                    return true;
                } else {
                    //Document is not editable by this user
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean Filter(String stringFindQuery) {
        Ready = false;
        try {
            // String strSql = "SELECT DISTINCT AMEND_DATE FROM PRODUCTION.FELT_CN_TEMP_HEADER WHERE  " + stringFindQuery + " ORDER BY CN_DATE";
            String strSql = "SELECT * FROM SDMLATTPAY.ATT_ROKDI_PUNCH_REPORT_HEADER WHERE  " + stringFindQuery + "";
            System.out.println("STR SQL : " + strSql);
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery(strSql);
            if (!resultSet.first()) {
                LoadData();
                Ready = true;
                return false;
            } else {
                Ready = true;
                MoveLast();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean setData() {
        ResultSet resultSetTemp;
        Statement statementTemp;
        int serialNoCounter = 0;
        int RevNo = 0;

        try {

            setAttribute("REVISION_NO", 0);

            setAttribute("RP_DOC_NO", resultSet.getString("RP_DOC_NO"));
            setAttribute("RP_DOC_DATE", resultSet.getDate("RP_DOC_DATE"));
            
            setAttribute("FROM_DATE", resultSet.getString("FROM_DATE"));
            setAttribute("TO_DATE", resultSet.getString("TO_DATE"));
            
            
            //setAttribute("EMAIL_UPDATE_TO_PM", resultSet.getBoolean("EMAIL_UPDATE_TO_PM"));
            
            if (HistoryView) {
                RevNo = resultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO", resultSet.getInt("REVISION_NO"));
            }

            setAttribute("CREATED_BY", resultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE", resultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY", resultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE", resultSet.getString("MODIFIED_DATE"));
            setAttribute("APPROVED", resultSet.getInt("APPROVED"));
            //setAttribute("APPROVED_BY",resultSet.getInt("APPROVED_BY"));
            setAttribute("APPROVED_DATE", resultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED", resultSet.getBoolean("REJECTED"));
            //setAttribute("REJECTED_BY",resultSet.getBoolean("REJECTED_BY"));
            setAttribute("REJECTED_DATE", resultSet.getString("REJECTED_DATE"));
            setAttribute("CANCELED", resultSet.getInt("CANCELED"));
            setAttribute("HIERARCHY_ID", resultSet.getInt("HIERARCHY_ID"));
            // setAttribute("REJECTED_REMARKS",resultSet.getString("REJECTED_REMARKS"));

            hmRokdiDetails.clear();

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            if (HistoryView) {
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  SDMLATTPAY.ATT_ROKDI_PUNCH_REPORT_DETAIL_H WHERE RP_DOC_NO='" + resultSet.getString("RP_DOC_NO") + "'  AND REVISION_NO=" + RevNo + " ORDER BY SR_NO DESC");
            } else {
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  SDMLATTPAY.ATT_ROKDI_PUNCH_REPORT_DETAIL WHERE RP_DOC_NO='" + resultSet.getString("RP_DOC_NO") + "'  ORDER BY RP_DOC_NO");
            }
            while (resultSetTemp.next()) {
                serialNoCounter++;

                clsRokdiReportDetails ObjFeltSalesOrderDetails = new clsRokdiReportDetails();

                //ObjFeltSalesOrderDetails.setAttribute("SR_NO",serialNoCounter);
                // ObjFeltSalesOrderDetails.setAttribute("S_ORDER_DETAIL_CODE",resultSetTemp.getString("S_ORDER_DETAIL_CODE"));
                ObjFeltSalesOrderDetails.setAttribute("SR_NO", resultSetTemp.getString("SR_NO"));
                ObjFeltSalesOrderDetails.setAttribute("RP_DOC_NO", resultSetTemp.getString("RP_DOC_NO"));
                ObjFeltSalesOrderDetails.setAttribute("EMP_NO", resultSetTemp.getString("EMP_NO"));
                ObjFeltSalesOrderDetails.setAttribute("EMP_NAME", resultSetTemp.getString("EMP_NAME"));
                ObjFeltSalesOrderDetails.setAttribute("DEPARTMENT", resultSetTemp.getString("DEPARTMENT"));
                ObjFeltSalesOrderDetails.setAttribute("FROM_DATE_DETAIL", resultSetTemp.getString("FROM_DATE_DETAIL"));
                ObjFeltSalesOrderDetails.setAttribute("FROM_PUNCH", resultSetTemp.getString("FROM_PUNCH"));
                ObjFeltSalesOrderDetails.setAttribute("TO_DATE_DETAIL", resultSetTemp.getString("TO_DATE_DETAIL"));
                ObjFeltSalesOrderDetails.setAttribute("TO_PUNCH", resultSetTemp.getString("TO_PUNCH"));
                ObjFeltSalesOrderDetails.setAttribute("DIFFRENCE", resultSetTemp.getString("DIFFRENCE"));
                hmRokdiDetails.put(Integer.toString(serialNoCounter), ObjFeltSalesOrderDetails);
            }
            resultSetTemp.close();
            statementTemp.close();
            
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    public boolean setHistoryData(String pProductionDate, String pDocNo) {
        ResultSet resultSetTemp;
        Statement statementTemp;
        int serialNoCounter = 0;
        int RevNo = 0;
        try {

            //Now Populate the collection, first clear the collection


            String strSql = "SELECT * FROM SDMLATTPAY.ATT_ROKDI_PUNCH_REPORT_HEADER_H WHERE RP_DOC_NO = " + pDocNo + "";
            System.out.println("STR SQL : " + strSql);
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery(strSql);
            resultSet.first();
            setAttribute("RP_DOC_NO", resultSet.getString("RP_DOC_NO"));
            setAttribute("REVISION_NO", resultSet.getString("REVISION_NO"));
            setAttribute("UPDATED_BY", resultSet.getString("UPDATED_BY"));
            setAttribute("RP_DOC_DATE", resultSet.getString("RP_DOC_DATE"));
            setAttribute("HIERARCHY_ID", resultSet.getInt("HIERARCHY_ID"));

            setAttribute("RP_DOC_NO", resultSet.getString("RP_DOC_NO"));
            setAttribute("RP_DOC_DATE", resultSet.getDate("RP_DOC_DATE"));
            setAttribute("FOR_MONTH", resultSet.getString("FOR_MONTH"));
            setAttribute("REMARK", resultSet.getString("REMARK"));

            
            resultSet.close();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    public static HashMap getHistoryList(String stringProductionDate, String productionDocumentNo) {
        HashMap hmHistoryList = new HashMap();
        Statement stTmp;
        ResultSet rsTmp;
        // String stringProductionDate1 =EITLERPGLOBAL.formatDate(stringProductionDate);

        try {
            stTmp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            System.out.println("SELECT * FROM SDMLATTPAY.ATT_ROKDI_PUNCH_REPORT_HEADER_H WHERE RP_DOC_NO='" + productionDocumentNo + "'");
            rsTmp = stTmp.executeQuery("SELECT * FROM SDMLATTPAY.ATT_ROKDI_PUNCH_REPORT_HEADER_H WHERE RP_DOC_NO='" + productionDocumentNo + "'");
            while (rsTmp.next()) {
                clsRokdiReport felt_order = new clsRokdiReport();

                felt_order.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                felt_order.setAttribute("UPDATED_BY", rsTmp.getString("UPDATED_BY"));
                felt_order.setAttribute("UPDATED_DATE", rsTmp.getString("UPDATED_DATE"));
                felt_order.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                felt_order.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                felt_order.setAttribute("APPROVER_REMARKS", rsTmp.getString("APPROVER_REMARKS"));
                felt_order.setAttribute("FROM_IP", rsTmp.getString("FROM_IP"));

               hmHistoryList.put(Integer.toString(hmHistoryList.size()+1),felt_order);
            }

            rsTmp.close();
            stTmp.close();
            return hmHistoryList;
        } catch (Exception e) {
            e.printStackTrace();
            return hmHistoryList;
        }
    }

    public boolean ShowHistory(String pDocNo) {
        Ready = false;
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery("SELECT * FROM SDMLATTPAY.ATT_ROKDI_PUNCH_REPORT_HEADER_H WHERE RP_DOC_NO ='" + pDocNo + "'");
            Ready = true;

            historyAmendID = pDocNo;
            HistoryView = true;
            MoveFirst();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    // Find Pending DOCUMENTS
    public static HashMap getPendingApprovals(int pUserID, int pOrder) {
        String strSQL = "";

        HashMap List = new HashMap();
        int Counter = 0;

        try {

            if (pOrder == EITLERPGLOBAL.OnRecivedDate) {
                strSQL = "SELECT DISTINCT RP_DOC_NO,D_COM_DOC_DATA.RP_DOC_DATE,RECEIVED_DATE FROM SDMLATTPAY.ATT_ROKDI_PUNCH_REPORT_HEADER, SDMLATTPAY.D_COM_DOC_DATA   WHERE RP_DOC_NO=D_COM_DOC_DATA.DOC_NO AND STATUS='W' AND MODULE_ID=" + ModuleID + " AND CANCELED=0  AND USER_ID=" + pUserID + " ORDER BY RECEIVED_DATE,RP_DOC_NO";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT RP_DOC_NO,D_COM_DOC_DATA.RP_DOC_DATE,RECEIVED_DATE FROM SDMLATTPAY.ATT_ROKDI_PUNCH_REPORT_HEADER, SDMLATTPAY.D_COM_DOC_DATA   WHERE RP_DOC_NO=D_COM_DOC_DATA.DOC_NO AND STATUS='W' AND MODULE_ID=" + ModuleID + " AND CANCELED=0  AND USER_ID=" + pUserID + " ORDER BY RECEIVED_DATE,RP_DOC_NO";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT RP_DOC_NO,D_COM_DOC_DATA.RP_DOC_DATE,RECEIVED_DATE FROM SDMLATTPAY.ATT_ROKDI_PUNCH_REPORT_HEADER, SDMLATTPAY.D_COM_DOC_DATA   WHERE RP_DOC_NO=D_COM_DOC_DATA.DOC_NO AND STATUS='W' AND MODULE_ID=" + ModuleID + " AND CANCELED=0  AND USER_ID=" + pUserID + "  ORDER BY RP_DOC_NO";
            }
            //System.out.println("Pending Doc : "+strSQL);
            ResultSet rsTmp = data.getConn().createStatement().executeQuery(strSQL);

            Counter = 0;
            while (rsTmp.next()) {

                Counter = Counter + 1;
                clsRokdiReport ObjDoc = new clsRokdiReport();

                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO", rsTmp.getString("RP_DOC_NO"));
                ObjDoc.setAttribute("RP_DOC_DATE", rsTmp.getString("RP_DOC_DATE"));
                ObjDoc.setAttribute("RECEIVED_DATE", rsTmp.getString("RECEIVED_DATE"));
                
                // ----------------- End of Header Fields ------------------------------------//

                List.put(Integer.toString(Counter), ObjDoc);
            }
            rsTmp.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return List;
    }

    // it creates list of user name(felt) to be displayed
    public HashMap getUserNameList(int pHierarchyId, int pUserId, String pModule) {
        HashMap hmUserNameList = new HashMap();
        char category = ' ';
        try {
            Connection Conn = data.getConn();
            Statement stTmp = Conn.createStatement();
            ResultSet rsHierarchyRights = stTmp.executeQuery("SELECT CREATOR, APPROVER, FINAL_APPROVER FROM D_COM_HIERARCHY_RIGHTS WHERE COMPANY_ID=2 AND HIERARCHY_ID=" + pHierarchyId + " AND USER_ID=" + pUserId);
            while (rsHierarchyRights.next()) {
                boolean creator = rsHierarchyRights.getBoolean("CREATOR");
                boolean approver = rsHierarchyRights.getBoolean("APPROVER");
                boolean finalApprover = rsHierarchyRights.getBoolean("FINAL_APPROVER");
                if (approver) {
                    category = 'A';
                }
                if (creator) {
                    category = 'C';
                }
                if (finalApprover) {
                    category = 'F';
                }
            }

            int counter = 1;
            ComboData cData = new ComboData();
            cData.Code = 0;
            cData.Text = "Select User";
            hmUserNameList.put(new Integer(counter++), cData);
            ResultSet rsTmp = stTmp.executeQuery("SELECT USER_ID,USER_NAME FROM PRODUCTION.`FELT_USER` WHERE USER_MODULE='" + pModule + "' AND USER_CATEG='" + category + "' ORDER BY USER_NAME");
            while (rsTmp.next()) {
                ComboData aData = new ComboData();
                aData.Code = rsTmp.getInt("USER_ID");
                aData.Text = rsTmp.getString("USER_NAME");
                hmUserNameList.put(new Integer(counter++), aData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hmUserNameList;
    }

    
    
    public static String getNextFreeNo(int pCompanyId, int pModuleID, int pFirstFree, boolean UpdateLastNo) {
        Connection tmpConn;
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";
        String strNewNo = "";
        int lnNewNo = 0;
        String Prefix = "";
        String Suffix = "";

        try {
            tmpConn = data.getConn();
            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //strSQL="SELECT LAST_USED_NO,PADDING_BY,NO_LENGTH FROM D_COM_FIRSTFREE WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND PREFIX_CHARS='"+pPrefix+"' AND SUFFIX_CHARS='"+pSuffix+"'";
            strSQL = "SELECT LAST_USED_NO,PADDING_BY,NO_LENGTH,PREFIX_CHARS,SUFFIX_CHARS FROM D_COM_FIRSTFREE WHERE COMPANY_ID=" + pCompanyId + " AND MODULE_ID=" + pModuleID + " AND FIRSTFREE_NO=" + pFirstFree;
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            if (rsTmp.getRow() > 0) {
                lnNewNo = rsTmp.getInt("LAST_USED_NO") + 1;
                strNewNo = EITLERPGLOBAL.Padding(Integer.toString(lnNewNo), rsTmp.getInt("NO_LENGTH"), rsTmp.getString("PADDING_BY"));
                Prefix = rsTmp.getString("PREFIX_CHARS");
                Suffix = rsTmp.getString("SUFFIX_CHARS");

                if (UpdateLastNo) {
                    //Update last no. in database
                    //data.Execute("UPDATE D_COM_FIRSTFREE SET LAST_USED_NO='"+strNewNo.trim()+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND PREFIX_CHARS='"+pPrefix+"' AND SUFFIX_CHARS='"+pSuffix+"'");
                    data.Execute("UPDATE D_COM_FIRSTFREE SET LAST_USED_NO='" + strNewNo.trim() + "',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + pCompanyId + " AND MODULE_ID=" + pModuleID + " AND FIRSTFREE_NO=" + pFirstFree);
                }

                strNewNo = Prefix + strNewNo + Suffix;

                //tmpConn.close();
                tmpStmt.close();
                rsTmp.close();

                return strNewNo;
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean CanCancel(String pDocNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean CanCancel = false;

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            System.out.println("SELECT RP_DOC_NO FROM SDMLATTPAY.ATT_ROKDI_PUNCH_REPORT_HEADER WHERE RP_DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");
            rsTmp = stTmp.executeQuery("SELECT RP_DOC_NO FROM SDMLATTPAY.ATT_ROKDI_PUNCH_REPORT_HEADER WHERE RP_DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");

            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                CanCancel = true;
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return CanCancel;
    }

    public static void CancelDoc(String pAmendNo) {
        ResultSet rsTmp = null;

        if (CanCancel(pAmendNo)) {

            boolean Approved = false;

            try {
                rsTmp = data.getResult("SELECT APPROVED FROM SDMLATTPAY.ATT_ROKDI_PUNCH_REPORT_HEADER WHERE RP_DOC_NO='" + pAmendNo + "' ");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    Approved = rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();

                if (!Approved) {
                    data.Execute("DELETE FROM SDMLATTPAY.D_COM_DOC_DATA WHERE DOC_NO='" + pAmendNo + "' AND MODULE_ID=812");
                }
                data.Execute("UPDATE SDMLATTPAY.ATT_ROKDI_PUNCH_REPORT_HEADER SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE RP_DOC_NO='" + pAmendNo + "'");
                //String Original_Piece_No = data.getStringValueFromDB("SELECT ORIGINAL_PIECE_NO FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION WHERE RP_DOC_NO='"+pAmendNo+"'");
                //data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_DIVERSION_FLAG='READY' WHERE PR_PIECE_NO='"+Original_Piece_No+"'");

            } catch (Exception e) {

            }
        }

    }
}
