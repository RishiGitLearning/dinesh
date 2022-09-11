/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.Order;

import EITLERP.ComboData;
import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.FeltPieceMerging.clsFeltPieceMerging;
import EITLERP.FeltSales.FeltPieceMerging.clsFeltPieceMergingDetails;
import EITLERP.FeltSales.common.Order_No_Conversion;
import EITLERP.JavaMail.JMail;
import EITLERP.Production.clsFeltProductionApprovalFlow;
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

/**
 *
 * @author DAXESH PRAJAPATI
 *
 */
public class clsFeltOrder {

    public String LastError = "";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet, rsHeader;
    //private ResultSet rsResultSet,

    public HashMap props;
    public boolean Ready = false;
    //Felt Order Collection
    public HashMap hmFeltSalesOrderDetails;

    //History Related properties
    public boolean HistoryView = false;
    private String historyAmendDate = "";
    private String historyAmendID = "";
    private static int ModuleID = 602;

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
    public clsFeltOrder() {
        LastError = "";
        props = new HashMap();
        props.put("S_ORDER_NO", new Variant(""));
        props.put("S_ORDER_DATE", new Variant(""));
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

        props.put("DOC_NO", new Variant(""));
        props.put("DOC_DATE", new Variant(""));
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

        hmFeltSalesOrderDetails = new HashMap();
    }

    public boolean LoadData() {
        Ready = false;
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_HEADER ORDER BY S_ORDER_NO");
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
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_ORDER_DETAIL WHERE S_ORDER_NO='1'");

            // Felt order Updation data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DETAIL_H WHERE S_ORDER_NO='1'");

            stHeader = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHeader = stHeader.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_ORDER_HEADER WHERE S_ORDER_NO='1'");

            stHeaderH = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHeaderH = stHeaderH.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_ORDER_HEADER_H WHERE S_ORDER_NO='1'");

            //setAttribute("S_ORDER_NO",);
            rsHeader.first();
            rsHeader.moveToInsertRow();

            rsHeader.updateString("S_ORDER_NO", getAttribute("S_ORDER_NO").getString());
            rsHeader.updateString("S_ORDER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("S_ORDER_DATE").getString()));
            rsHeader.updateString("REGION", getAttribute("REGION").getString());
            rsHeader.updateString("SALES_ENGINEER", getAttribute("SALES_ENGINEER").getString());
            rsHeader.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsHeader.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
            rsHeader.updateString("REFERENCE", getAttribute("REFERENCE").getString());
            rsHeader.updateString("REFERENCE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("REFERENCE_DATE").getString()));
            rsHeader.updateString("P_O_NO", getAttribute("P_O_NO").getString());
            rsHeader.updateString("P_O_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("P_O_DATE").getString()));
            rsHeader.updateString("REMARK", getAttribute("REMARK").getString());
            rsHeader.updateBoolean("OLD_PIECE", getAttribute("OLD_PIECE").getBool());
            rsHeader.updateBoolean("TENDER_PARTY", getAttribute("TENDER_PARTY").getBool());

            rsHeader.updateBoolean("EMAIL_UPDATE_TO_PM", getAttribute("EMAIL_UPDATE_TO_PM").getBool());

            rsHeader.updateString("EXPORT_PAYMENT_TYPE", getAttribute("EXPORT_PAYMENT_TYPE").getString());
            rsHeader.updateString("EXPORT_PAYMENT_DATE", getAttribute("EXPORT_PAYMENT_DATE").getString());
            rsHeader.updateString("EXPORT_PAYMENT_REMARK", getAttribute("EXPORT_PAYMENT_REMARK").getString());
            
            rsHeader.updateString("CONTACT_PERSON_POSITION", getAttribute("CONTACT_PERSON_POSITION").getString());
            rsHeader.updateString("CONTACT_PERSON_DINESH", getAttribute("CONTACT_PERSON_DINESH").getString());
            
            rsHeader.updateString("CONTACT_PERSON", getAttribute("CONTACT_PERSON").getString());
            rsHeader.updateString("EMAIL_ID", getAttribute("EMAIL_ID").getString());
            rsHeader.updateString("PHONE_NUMBER", getAttribute("PHONE_NUMBER").getString());

            rsHeader.updateString("EMAIL_ID2", getAttribute("EMAIL_ID2").getString());
            rsHeader.updateString("EMAIL_ID3", getAttribute("EMAIL_ID3").getString());

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
            rsHeaderH.updateString("S_ORDER_NO", getAttribute("S_ORDER_NO").getString());
            rsHeaderH.updateString("S_ORDER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("S_ORDER_DATE").getString()));
            rsHeaderH.updateString("REGION", getAttribute("REGION").getString());
            rsHeaderH.updateString("SALES_ENGINEER", getAttribute("SALES_ENGINEER").getString());
            rsHeaderH.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsHeaderH.updateString("REFERENCE", getAttribute("REFERENCE").getString());
            rsHeaderH.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
            rsHeaderH.updateString("REFERENCE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("REFERENCE_DATE").getString()));
            rsHeaderH.updateString("P_O_NO", getAttribute("P_O_NO").getString());
            rsHeaderH.updateString("REMARK", getAttribute("REMARK").getString());
            rsHeaderH.updateBoolean("OLD_PIECE", getAttribute("OLD_PIECE").getBool());
            rsHeaderH.updateBoolean("TENDER_PARTY", getAttribute("TENDER_PARTY").getBool());

            rsHeaderH.updateString("EXPORT_PAYMENT_TYPE", getAttribute("EXPORT_PAYMENT_TYPE").getString());
            rsHeaderH.updateString("EXPORT_PAYMENT_DATE", getAttribute("EXPORT_PAYMENT_DATE").getString());
            rsHeaderH.updateString("EXPORT_PAYMENT_REMARK", getAttribute("EXPORT_PAYMENT_REMARK").getString());
            
            rsHeaderH.updateBoolean("EMAIL_UPDATE_TO_PM", getAttribute("EMAIL_UPDATE_TO_PM").getBool());

            rsHeaderH.updateString("CONTACT_PERSON", getAttribute("CONTACT_PERSON").getString());
            rsHeaderH.updateString("EMAIL_ID", getAttribute("EMAIL_ID").getString());
            rsHeaderH.updateString("PHONE_NUMBER", getAttribute("PHONE_NUMBER").getString());

            rsHeaderH.updateString("CONTACT_PERSON_POSITION", getAttribute("CONTACT_PERSON_POSITION").getString());
            rsHeaderH.updateString("CONTACT_PERSON_DINESH", getAttribute("CONTACT_PERSON_DINESH").getString());
            
            rsHeaderH.updateString("EMAIL_ID2", getAttribute("EMAIL_ID2").getString());
            rsHeaderH.updateString("EMAIL_ID3", getAttribute("EMAIL_ID3").getString());

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
            for (int i = 1; i <= hmFeltSalesOrderDetails.size(); i++) {
                clsFeltSalesOrderDetails ObjFeltSalesOrderDetails = (clsFeltSalesOrderDetails) hmFeltSalesOrderDetails.get(Integer.toString(i));

                //Insert records into Felt order Amendment table
                resultSetTemp.moveToInsertRow();

                resultSetTemp.updateString("S_ORDER_DETAIL_CODE", "0");
                resultSetTemp.updateInt("SR_NO", i);
                resultSetTemp.updateString("S_ORDER_NO", ObjFeltSalesOrderDetails.getAttribute("S_ORDER_NO").getString());
                resultSetTemp.updateString("MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                resultSetTemp.updateString("POSITION", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                resultSetTemp.updateString("POSITION_DESC", ObjFeltSalesOrderDetails.getAttribute("POSITION_DESC").getString());
                resultSetTemp.updateString("UPN", ObjFeltSalesOrderDetails.getAttribute("UPN").getString());
                resultSetTemp.updateString("PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                resultSetTemp.updateString("LAYER_TYPE", ObjFeltSalesOrderDetails.getAttribute("LAYER_TYPE").getString());
                //LAYER_TYPE
                resultSetTemp.updateString("PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                resultSetTemp.updateString("PRODUCT_DESC", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_DESC").getString());
                resultSetTemp.updateString("S_GROUP", ObjFeltSalesOrderDetails.getAttribute("S_GROUP").getString());
                resultSetTemp.updateString("LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                resultSetTemp.updateString("WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                resultSetTemp.updateString("GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                resultSetTemp.updateString("THORITICAL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("THORITICAL_WIDTH").getString());
                resultSetTemp.updateString("SQ_MTR", ObjFeltSalesOrderDetails.getAttribute("SQ_MTR").getString());
                resultSetTemp.updateString("STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                resultSetTemp.updateString("REQ_MONTH", ObjFeltSalesOrderDetails.getAttribute("REQ_MONTH").getString());
                resultSetTemp.updateString("SYN_PER", ObjFeltSalesOrderDetails.getAttribute("SYN_PER").getString());
                resultSetTemp.updateString("REMARK", ObjFeltSalesOrderDetails.getAttribute("REMARK").getString());
                resultSetTemp.updateString("OV_RATE", ObjFeltSalesOrderDetails.getAttribute("OV_RATE").getString());
                
                
                resultSetTemp.updateString("SURCHARGE_PER", ObjFeltSalesOrderDetails.getAttribute("SURCHARGE_PER").getString());
                resultSetTemp.updateString("SURCHARGE_RATE", ObjFeltSalesOrderDetails.getAttribute("SURCHARGE_RATE").getString());
                resultSetTemp.updateString("GROSS_RATE", ObjFeltSalesOrderDetails.getAttribute("GROSS_RATE").getString());
                
                resultSetTemp.updateString("OV_BAS_AMOUNT", ObjFeltSalesOrderDetails.getAttribute("OV_BAS_AMOUNT").getString());
                resultSetTemp.updateString("OV_CHEM_TRT_CHG", ObjFeltSalesOrderDetails.getAttribute("OV_CHEM_TRT_CHG").getString());
                resultSetTemp.updateString("OV_SPIRAL_CHG", ObjFeltSalesOrderDetails.getAttribute("OV_SPIRAL_CHG").getString());
                resultSetTemp.updateString("OV_PIN_CHG", ObjFeltSalesOrderDetails.getAttribute("OV_PIN_CHG").getString());
                resultSetTemp.updateString("OV_SEAM_CHG", ObjFeltSalesOrderDetails.getAttribute("OV_SEAM_CHG").getString());
                resultSetTemp.updateString("OV_INS_IND", ObjFeltSalesOrderDetails.getAttribute("OV_INS_IND").getString());
                resultSetTemp.updateString("OV_INS_AMT", ObjFeltSalesOrderDetails.getAttribute("OV_INS_AMT").getString());
                resultSetTemp.updateString("OV_EXCISE", ObjFeltSalesOrderDetails.getAttribute("OV_EXCISE").getString());
                resultSetTemp.updateString("OV_DISC_PER", ObjFeltSalesOrderDetails.getAttribute("OV_DISC_PER").getString());
                resultSetTemp.updateString("OV_DISC_AMT", ObjFeltSalesOrderDetails.getAttribute("OV_DISC_AMT").getString());
                resultSetTemp.updateString("OV_DISC_BASAMT", ObjFeltSalesOrderDetails.getAttribute("OV_DISC_BASAMT").getString());
                resultSetTemp.updateString("OV_AMT", ObjFeltSalesOrderDetails.getAttribute("OV_AMT").getString());
                resultSetTemp.updateInt("CREATED_BY", EITLERPGLOBAL.gNewUserID);
                resultSetTemp.updateString("CREATED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));

                resultSetTemp.updateDouble("CGST_PER", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("CGST_PER").getString()));
                resultSetTemp.updateDouble("CGST_AMT", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("CGST_AMT").getString()));

                resultSetTemp.updateDouble("SGST_PER", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("SGST_PER").getString()));
                resultSetTemp.updateDouble("SGST_AMT", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("SGST_AMT").getString()));

                resultSetTemp.updateDouble("IGST_PER", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("IGST_PER").getString()));
                resultSetTemp.updateDouble("IGST_AMT", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("IGST_AMT").getString()));

                resultSetTemp.updateDouble("RCM_PER", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("RCM_PER").getString()));
                resultSetTemp.updateDouble("RCM_AMT", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("RCM_AMT").getString()));

                resultSetTemp.updateDouble("COMPOSITION_PER", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("COMPOSITION_PER").getString()));
                resultSetTemp.updateDouble("COMPOSITION_AMT", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("COMPOSITION_AMT").getString()));

                resultSetTemp.updateDouble("GST_COMPENSATION_CESS_PER", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("GST_COMPENSATION_CESS_PER").getString()));
                resultSetTemp.updateDouble("GST_COMPENSATION_CESS_AMT", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("GST_COMPENSATION_CESS_AMT").getString()));

                resultSetTemp.updateString("PR_BILL_LENGTH", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_LENGTH").getString());
                resultSetTemp.updateString("PR_BILL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_WIDTH").getString());
                resultSetTemp.updateString("PR_BILL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_WEIGHT").getString());
                resultSetTemp.updateString("PR_BILL_SQMTR", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_SQMTR").getString());
                resultSetTemp.updateString("PR_BILL_GSM", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_GSM").getString());
                resultSetTemp.updateString("PR_BILL_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_PRODUCT_CODE").getString());
                //PR_BILL_STYLE
                resultSetTemp.updateString("PR_BILL_STYLE", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_STYLE").getString());
                //DATE_SLOT
                // resultSetTemp.updateString("DATE_SLOT", ObjFeltSalesOrderDetails.getAttribute("DATE_SLOT").getString());

                resultSetTemp.updateString("TENDER_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("TENDER_WEIGHT").getString());
                resultSetTemp.updateString("TENDER_GSM", ObjFeltSalesOrderDetails.getAttribute("TENDER_GSM").getString());
                resultSetTemp.updateString("DM_REVISION_NO", ObjFeltSalesOrderDetails.getAttribute("DM_REVISION_NO").getString());
                resultSetTemp.updateString("PIECE_MERGE", ObjFeltSalesOrderDetails.getAttribute("PIECE_MERGE").getString());
                resultSetTemp.updateString("DUMMY_REF_NO", ObjFeltSalesOrderDetails.getAttribute("DUMMY_REF_NO").getString());

//                resultSetTemp.updateInt("MODIFIED_BY",0);
//                resultSetTemp.updateString("MODIFIED_DATE","0000-00-00");
//                resultSetTemp.updateBoolean("APPROVED",false);
                //               resultSetTemp.updateString("APPROVED_DATE","0000-00-00");
                //               resultSetTemp.updateBoolean("REJECTED",false);
                //              resultSetTemp.updateString("REJECTED_DATE","0000-00-00");
                //              resultSetTemp.updateInt("CANCELED",0);
                //              resultSetTemp.updateInt("HIERARCHY_ID",(int)ObjFeltSalesOrderDetails.getAttribute("HIERARCHY_ID").getVal());
                //              resultSetTemp.updateInt("CHANGED",1);
                //              resultSetTemp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetTemp.insertRow();

                //Insert records into Felt Order Amendment History Table
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO", 1);
                resultSetHistory.updateInt("SR_NO", i);
                resultSetHistory.updateString("S_ORDER_DETAIL_CODE", "0");
                resultSetHistory.updateString("S_ORDER_NO", ObjFeltSalesOrderDetails.getAttribute("S_ORDER_NO").getString());
                resultSetHistory.updateString("MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                resultSetHistory.updateString("POSITION", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                resultSetHistory.updateString("POSITION_DESC", ObjFeltSalesOrderDetails.getAttribute("POSITION_DESC").getString());
                resultSetHistory.updateString("PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                resultSetHistory.updateString("LAYER_TYPE", ObjFeltSalesOrderDetails.getAttribute("LAYER_TYPE").getString());
                resultSetHistory.updateString("PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                resultSetHistory.updateString("PRODUCT_DESC", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_DESC").getString());
                resultSetHistory.updateString("UPN", ObjFeltSalesOrderDetails.getAttribute("UPN").getString());
                resultSetHistory.updateString("S_GROUP", ObjFeltSalesOrderDetails.getAttribute("S_GROUP").getString());
                resultSetHistory.updateString("LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                resultSetHistory.updateString("WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                resultSetHistory.updateString("GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                resultSetHistory.updateString("THORITICAL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("THORITICAL_WIDTH").getString());
                resultSetHistory.updateString("SQ_MTR", ObjFeltSalesOrderDetails.getAttribute("SQ_MTR").getString());
                resultSetHistory.updateString("STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                resultSetHistory.updateString("REQ_MONTH", ObjFeltSalesOrderDetails.getAttribute("REQ_MONTH").getString());
                resultSetHistory.updateString("SYN_PER", ObjFeltSalesOrderDetails.getAttribute("SYN_PER").getString());
                resultSetHistory.updateString("REMARK", ObjFeltSalesOrderDetails.getAttribute("REMARK").getString());
                resultSetHistory.updateString("OV_RATE", ObjFeltSalesOrderDetails.getAttribute("OV_RATE").getString());
                
                resultSetHistory.updateString("SURCHARGE_PER", ObjFeltSalesOrderDetails.getAttribute("SURCHARGE_PER").getString());
                resultSetHistory.updateString("SURCHARGE_RATE", ObjFeltSalesOrderDetails.getAttribute("SURCHARGE_RATE").getString());
                resultSetHistory.updateString("GROSS_RATE", ObjFeltSalesOrderDetails.getAttribute("GROSS_RATE").getString());
                
                resultSetHistory.updateString("OV_BAS_AMOUNT", ObjFeltSalesOrderDetails.getAttribute("OV_BAS_AMOUNT").getString());
                resultSetHistory.updateString("OV_CHEM_TRT_CHG", ObjFeltSalesOrderDetails.getAttribute("OV_CHEM_TRT_CHG").getString());
                resultSetHistory.updateString("OV_SPIRAL_CHG", ObjFeltSalesOrderDetails.getAttribute("OV_SPIRAL_CHG").getString());
                resultSetHistory.updateString("OV_PIN_CHG", ObjFeltSalesOrderDetails.getAttribute("OV_PIN_CHG").getString());
                resultSetHistory.updateString("OV_SEAM_CHG", ObjFeltSalesOrderDetails.getAttribute("OV_SEAM_CHG").getString());
                resultSetHistory.updateString("OV_INS_IND", ObjFeltSalesOrderDetails.getAttribute("OV_INS_IND").getString());
                resultSetHistory.updateString("OV_INS_AMT", ObjFeltSalesOrderDetails.getAttribute("OV_INS_AMT").getString());
                resultSetHistory.updateString("OV_EXCISE", ObjFeltSalesOrderDetails.getAttribute("OV_EXCISE").getString());
                resultSetHistory.updateString("OV_DISC_PER", ObjFeltSalesOrderDetails.getAttribute("OV_DISC_PER").getString());
                resultSetHistory.updateString("OV_DISC_AMT", ObjFeltSalesOrderDetails.getAttribute("OV_DISC_AMT").getString());
                resultSetHistory.updateString("OV_DISC_BASAMT", ObjFeltSalesOrderDetails.getAttribute("OV_DISC_BASAMT").getString());
                resultSetHistory.updateString("OV_AMT", ObjFeltSalesOrderDetails.getAttribute("OV_AMT").getString());
                resultSetHistory.updateInt("CREATED_BY", EITLERPGLOBAL.gNewUserID);
                resultSetHistory.updateString("CREATED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateInt("MODIFIED_BY", 0);
                resultSetHistory.updateString("MODIFIED_DATE", EITLERPGLOBAL.formatDateDB("0000-00-00"));

                resultSetHistory.updateDouble("CGST_PER", ObjFeltSalesOrderDetails.getAttribute("CGST_PER").getVal());
                resultSetHistory.updateDouble("CGST_AMT", ObjFeltSalesOrderDetails.getAttribute("CGST_AMT").getVal());

                resultSetHistory.updateDouble("SGST_PER", ObjFeltSalesOrderDetails.getAttribute("SGST_PER").getVal());
                resultSetHistory.updateDouble("SGST_AMT", ObjFeltSalesOrderDetails.getAttribute("SGST_AMT").getVal());

                resultSetHistory.updateDouble("IGST_PER", ObjFeltSalesOrderDetails.getAttribute("IGST_PER").getVal());
                resultSetHistory.updateDouble("IGST_AMT", ObjFeltSalesOrderDetails.getAttribute("IGST_AMT").getVal());

                resultSetHistory.updateDouble("RCM_PER", ObjFeltSalesOrderDetails.getAttribute("RCM_PER").getVal());
                resultSetHistory.updateDouble("RCM_AMT", ObjFeltSalesOrderDetails.getAttribute("RCM_AMT").getVal());

                resultSetHistory.updateDouble("COMPOSITION_PER", ObjFeltSalesOrderDetails.getAttribute("COMPOSITION_PER").getVal());
                resultSetHistory.updateDouble("COMPOSITION_AMT", ObjFeltSalesOrderDetails.getAttribute("COMPOSITION_AMT").getVal());

                resultSetHistory.updateDouble("GST_COMPENSATION_CESS_PER", ObjFeltSalesOrderDetails.getAttribute("GST_COMPENSATION_CESS_PER").getVal());
                resultSetHistory.updateDouble("GST_COMPENSATION_CESS_AMT", ObjFeltSalesOrderDetails.getAttribute("GST_COMPENSATION_CESS_AMT").getVal());

                resultSetHistory.updateString("PR_BILL_LENGTH", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_LENGTH").getString());
                resultSetHistory.updateString("PR_BILL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_WIDTH").getString());
                resultSetHistory.updateString("PR_BILL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_WEIGHT").getString());
                resultSetHistory.updateString("PR_BILL_SQMTR", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_SQMTR").getString());
                resultSetHistory.updateString("PR_BILL_GSM", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_GSM").getString());
                resultSetHistory.updateString("PR_BILL_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_PRODUCT_CODE").getString());
                resultSetHistory.updateString("PR_BILL_STYLE", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_STYLE").getString());
                //  resultSetHistory.updateString("DATE_SLOT", ObjFeltSalesOrderDetails.getAttribute("DATE_SLOT").getString());

                resultSetHistory.updateString("TENDER_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("TENDER_WEIGHT").getString());
                resultSetHistory.updateString("TENDER_GSM", ObjFeltSalesOrderDetails.getAttribute("TENDER_GSM").getString());
                resultSetHistory.updateString("DM_REVISION_NO", ObjFeltSalesOrderDetails.getAttribute("DM_REVISION_NO").getString());
                resultSetHistory.updateString("PIECE_MERGE", ObjFeltSalesOrderDetails.getAttribute("PIECE_MERGE").getString());
                resultSetHistory.updateString("DUMMY_REF_NO", ObjFeltSalesOrderDetails.getAttribute("DUMMY_REF_NO").getString());

                resultSetHistory.insertRow();
                
                //change on 21/01/2021
                try{
                    if (getAttribute("PARTY_CODE").getString().startsWith("8")) {
                        String MACHINE_NO = ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString();
                        String POSITION = ObjFeltSalesOrderDetails.getAttribute("POSITION").getString();
                        String Month = data.getStringValueFromDB("SELECT LPAD(MONTH(CURDATE()),2,0) FROM DUAL");
                        String Query_BUDGET = "SELECT * FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL where PARTY_CODE='" + getAttribute("PARTY_CODE").getString() + "' AND MACHINE_NO=" + MACHINE_NO + " AND POSITION_NO=" + POSITION + " AND YEAR_FROM='" + EITLERPGLOBAL.getCurrentFinYear() + "' AND YEAR_TO='" + (EITLERPGLOBAL.getCurrentFinYear() + 1) + "'  AND DOC_NO LIKE '_____"+Month+"%' AND APPROVED=1 AND CURRENT_PROJECTION-(WIP_QTY+STOCK_QTY+DISPATCH_QTY)>0";
                        System.out.println("Query_BUDGET : " + Query_BUDGET);

                        if (!data.IsRecordExist(Query_BUDGET)) {

                            data.Execute("UPDATE PRODUCTION.FELT_SALES_ALLOWED_BOOKING_AGAINST_PROJECTION_MASTER\n" +
                                        "SET UPDATED_ALLOED_QTY=UPDATED_ALLOED_QTY-1 WHERE \n" +
                                        "UPN='"+ObjFeltSalesOrderDetails.getAttribute("UPN").getString()+"' AND MONTH='"+EITLERPGLOBAL.getCurrentMonth()+"' AND YEAR='"+EITLERPGLOBAL.getCurrentYear()+"' ");

                        }
                    }
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }

            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = ModuleID; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("S_ORDER_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.FELT_SALES_ORDER_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator = true;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("REJECTED_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "S_ORDER_NO";

            //JOptionPane.showMessageDialog(null, "TO : "+(int)getAttribute("TO").getVal()+", Approval Status = "+(String)getAttribute("APPROVAL_STATUS").getObj());
//            if("A".equals((String)getAttribute("APPROVAL_STATUS").getObj()))
//            {   
//                    String Subject = "Felt Order Pending Document : "+getAttribute("S_ORDER_NO").getString();
//                    String Message = "Document No : "+getAttribute("S_ORDER_NO").getString()+" is added in your PENDING DOCUMENT"
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
            if (ObjFeltProductionApprovalFlow.Status.equals("H")) {
                ObjFeltProductionApprovalFlow.Status = "A";
                ObjFeltProductionApprovalFlow.To = ObjFeltProductionApprovalFlow.From;
                ObjFeltProductionApprovalFlow.UpdateFlow();
            } else {
                if (!ObjFeltProductionApprovalFlow.UpdateFlow()) {
                    LastError = ObjFeltProductionApprovalFlow.LastError;
                }
            }

            //--------- Approval Flow Update complete -----------
            // Update  in Order Master Table To confirm that Weaving has completed
            if (ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {

            }

            LoadData();

            resultSetTemp.close();
            statementTemp.close();
            resultSetHistory.close();
            statementHistory.close();
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

            // Production data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //Get the Maximum Revision No in History Table.
            resultSetHistory = statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_SALES_ORDER_DETAIL_H WHERE S_ORDER_NO='1'");
            resultSetHistory.first();
            if (resultSetHistory.getRow() > 0) {
                revisionNo = resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from production data table before insert
            //statementHistory.execute("DELETE FROM PRODUCTION.FELT_SALES_ORDER_DETAIL WHERE S_ORDER_NO='"+getAttribute("S_ORDER_NO").getString()+"'");

            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DETAIL_H WHERE S_ORDER_NO=''");

            stHeaderH = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHeaderH = stHeaderH.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_ORDER_HEADER_H WHERE S_ORDER_NO=''");

            //Now Update records into FELT_CN_TEMP_HEADER tables
            resultSet.updateString("S_ORDER_NO", getAttribute("S_ORDER_NO").getString());
            resultSet.updateString("S_ORDER_DATE", EITLERPGLOBAL.formatDateDB((String) getAttribute("S_ORDER_DATE").getObj()));
            resultSet.updateString("REGION", (String) getAttribute("REGION").getObj());
            resultSet.updateString("SALES_ENGINEER", (String) getAttribute("SALES_ENGINEER").getObj());
            resultSet.updateString("PARTY_CODE", (String) getAttribute("PARTY_CODE").getObj());
            resultSet.updateString("PARTY_NAME", (String) getAttribute("PARTY_NAME").getObj());
            resultSet.updateString("REFERENCE", (String) getAttribute("REFERENCE").getObj());
            resultSet.updateString("REFERENCE_DATE", EITLERPGLOBAL.formatDateDB((String) getAttribute("REFERENCE_DATE").getObj()));
            resultSet.updateString("P_O_NO", (String) getAttribute("P_O_NO").getObj());
            resultSet.updateString("P_O_DATE", EITLERPGLOBAL.formatDateDB((String) getAttribute("P_O_DATE").getObj()));
            resultSet.updateString("REMARK", (String) getAttribute("REMARK").getObj());
            resultSet.updateBoolean("OLD_PIECE", getAttribute("OLD_PIECE").getBool());
            resultSet.updateBoolean("TENDER_PARTY", getAttribute("TENDER_PARTY").getBool());

            resultSet.updateBoolean("EMAIL_UPDATE_TO_PM", getAttribute("EMAIL_UPDATE_TO_PM").getBool());
            
            resultSet.updateString("EXPORT_PAYMENT_TYPE", getAttribute("EXPORT_PAYMENT_TYPE").getString());
            resultSet.updateString("EXPORT_PAYMENT_DATE", getAttribute("EXPORT_PAYMENT_DATE").getString());
            resultSet.updateString("EXPORT_PAYMENT_REMARK", getAttribute("EXPORT_PAYMENT_REMARK").getString());
            
            resultSet.updateString("CONTACT_PERSON_POSITION", getAttribute("CONTACT_PERSON_POSITION").getString());
            resultSet.updateString("CONTACT_PERSON_DINESH", getAttribute("CONTACT_PERSON_DINESH").getString());
            
            resultSet.updateString("CONTACT_PERSON", getAttribute("CONTACT_PERSON").getString());
            resultSet.updateString("EMAIL_ID", getAttribute("EMAIL_ID").getString());
            resultSet.updateString("PHONE_NUMBER", getAttribute("PHONE_NUMBER").getString());

            resultSet.updateString("EMAIL_ID2", getAttribute("EMAIL_ID2").getString());
            resultSet.updateString("EMAIL_ID3", getAttribute("EMAIL_ID3").getString());

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
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_SALES_ORDER_HEADER_H WHERE S_ORDER_NO='" + getAttribute("S_ORDER_NO").getString() + "'");

            RevNo++;

            rsHeaderH.moveToInsertRow();

            rsHeaderH.updateString("REVISION_NO", RevNo + "");
            rsHeaderH.updateString("S_ORDER_NO", getAttribute("S_ORDER_NO").getString() + "");
            rsHeaderH.updateString("S_ORDER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("S_ORDER_DATE").getString()));
            rsHeaderH.updateString("REGION", getAttribute("REGION").getString());
            rsHeaderH.updateString("SALES_ENGINEER", getAttribute("SALES_ENGINEER").getString());
            rsHeaderH.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsHeaderH.updateString("REFERENCE", getAttribute("REFERENCE").getString());
            rsHeaderH.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
            rsHeaderH.updateString("REFERENCE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("REFERENCE_DATE").getString()));
            rsHeaderH.updateString("P_O_NO", getAttribute("P_O_NO").getString());
            rsHeaderH.updateString("P_O_DATE", EITLERPGLOBAL.formatDateDB((String) getAttribute("P_O_DATE").getObj()));
            rsHeaderH.updateString("REMARK", getAttribute("REMARK").getString());
            rsHeaderH.updateBoolean("OLD_PIECE", getAttribute("OLD_PIECE").getBool());
            rsHeaderH.updateBoolean("TENDER_PARTY", getAttribute("TENDER_PARTY").getBool());

            rsHeaderH.updateBoolean("EMAIL_UPDATE_TO_PM", getAttribute("EMAIL_UPDATE_TO_PM").getBool());

            rsHeaderH.updateString("EXPORT_PAYMENT_TYPE", getAttribute("EXPORT_PAYMENT_TYPE").getString());
            rsHeaderH.updateString("EXPORT_PAYMENT_DATE", getAttribute("EXPORT_PAYMENT_DATE").getString());
            rsHeaderH.updateString("EXPORT_PAYMENT_REMARK", getAttribute("EXPORT_PAYMENT_REMARK").getString());
            
            rsHeaderH.updateString("CONTACT_PERSON", getAttribute("CONTACT_PERSON").getString());
            rsHeaderH.updateString("EMAIL_ID", getAttribute("EMAIL_ID").getString());
            rsHeaderH.updateString("PHONE_NUMBER", getAttribute("PHONE_NUMBER").getString());

            rsHeaderH.updateString("CONTACT_PERSON_POSITION", getAttribute("CONTACT_PERSON_POSITION").getString());
            rsHeaderH.updateString("CONTACT_PERSON_DINESH", getAttribute("CONTACT_PERSON_DINESH").getString());
            
            rsHeaderH.updateString("EMAIL_ID2", getAttribute("EMAIL_ID2").getString());
            rsHeaderH.updateString("EMAIL_ID3", getAttribute("EMAIL_ID3").getString());

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
            String OrderNo = getAttribute("S_ORDER_NO").getString();
            data.Execute("DELETE FROM PRODUCTION.FELT_SALES_ORDER_DETAIL WHERE S_ORDER_NO='" + OrderNo + "'");

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_ORDER_DETAIL WHERE S_ORDER_NO='1'");

            for (int i = 1; i <= hmFeltSalesOrderDetails.size(); i++) {
                clsFeltSalesOrderDetails ObjFeltSalesOrderDetails = (clsFeltSalesOrderDetails) hmFeltSalesOrderDetails.get(Integer.toString(i));

                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateInt("SR_NO", i);
                resultSetTemp.updateString("S_ORDER_NO", ObjFeltSalesOrderDetails.getAttribute("S_ORDER_NO").getString() + "");
                resultSetTemp.updateString("S_ORDER_DETAIL_CODE", "0");
                resultSetTemp.updateString("MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                resultSetTemp.updateString("POSITION", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                resultSetTemp.updateString("POSITION_DESC", ObjFeltSalesOrderDetails.getAttribute("POSITION_DESC").getString());
                resultSetTemp.updateString("UPN", ObjFeltSalesOrderDetails.getAttribute("UPN").getString());
                resultSetTemp.updateString("PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                resultSetTemp.updateString("LAYER_TYPE", ObjFeltSalesOrderDetails.getAttribute("LAYER_TYPE").getString());
                resultSetTemp.updateString("PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                resultSetTemp.updateString("PRODUCT_DESC", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_DESC").getString());
                resultSetTemp.updateString("S_GROUP", ObjFeltSalesOrderDetails.getAttribute("S_GROUP").getString());
                resultSetTemp.updateString("LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                resultSetTemp.updateString("WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                resultSetTemp.updateString("GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                resultSetTemp.updateString("THORITICAL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("THORITICAL_WIDTH").getString());
                resultSetTemp.updateString("SQ_MTR", ObjFeltSalesOrderDetails.getAttribute("SQ_MTR").getString());
                resultSetTemp.updateString("STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                resultSetTemp.updateString("REQ_MONTH", ObjFeltSalesOrderDetails.getAttribute("REQ_MONTH").getString());
                resultSetTemp.updateString("SYN_PER", ObjFeltSalesOrderDetails.getAttribute("SYN_PER").getString());
                resultSetTemp.updateString("REMARK", ObjFeltSalesOrderDetails.getAttribute("REMARK").getString());
                resultSetTemp.updateString("OV_RATE", ObjFeltSalesOrderDetails.getAttribute("OV_RATE").getString());
                
                resultSetTemp.updateString("SURCHARGE_PER", ObjFeltSalesOrderDetails.getAttribute("SURCHARGE_PER").getString());
                resultSetTemp.updateString("SURCHARGE_RATE", ObjFeltSalesOrderDetails.getAttribute("SURCHARGE_RATE").getString());
                resultSetTemp.updateString("GROSS_RATE", ObjFeltSalesOrderDetails.getAttribute("GROSS_RATE").getString());
                
                resultSetTemp.updateString("OV_BAS_AMOUNT", ObjFeltSalesOrderDetails.getAttribute("OV_BAS_AMOUNT").getString());
                resultSetTemp.updateString("OV_CHEM_TRT_CHG", ObjFeltSalesOrderDetails.getAttribute("OV_CHEM_TRT_CHG").getString());
                resultSetTemp.updateString("OV_SPIRAL_CHG", ObjFeltSalesOrderDetails.getAttribute("OV_SPIRAL_CHG").getString());
                resultSetTemp.updateString("OV_PIN_CHG", ObjFeltSalesOrderDetails.getAttribute("OV_PIN_CHG").getString());
                resultSetTemp.updateString("OV_SEAM_CHG", ObjFeltSalesOrderDetails.getAttribute("OV_SEAM_CHG").getString());
                resultSetTemp.updateString("OV_INS_IND", ObjFeltSalesOrderDetails.getAttribute("OV_INS_IND").getString());
                resultSetTemp.updateString("OV_INS_AMT", ObjFeltSalesOrderDetails.getAttribute("OV_INS_AMT").getString());
                resultSetTemp.updateString("OV_EXCISE", ObjFeltSalesOrderDetails.getAttribute("OV_EXCISE").getString());
                resultSetTemp.updateString("OV_DISC_PER", ObjFeltSalesOrderDetails.getAttribute("OV_DISC_PER").getString());
                resultSetTemp.updateString("OV_DISC_AMT", ObjFeltSalesOrderDetails.getAttribute("OV_DISC_AMT").getString());
                resultSetTemp.updateString("OV_DISC_BASAMT", ObjFeltSalesOrderDetails.getAttribute("OV_DISC_BASAMT").getString());
                resultSetTemp.updateString("OV_AMT", ObjFeltSalesOrderDetails.getAttribute("OV_AMT").getString());
                resultSetTemp.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                resultSetTemp.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
                resultSetTemp.updateInt("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
                resultSetTemp.updateString("MODIFIED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));

                resultSetTemp.updateDouble("CGST_PER", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("CGST_PER").getString()));
                resultSetTemp.updateDouble("CGST_AMT", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("CGST_AMT").getString()));

                resultSetTemp.updateDouble("SGST_PER", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("SGST_PER").getString()));
                resultSetTemp.updateDouble("SGST_AMT", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("SGST_AMT").getString()));

                resultSetTemp.updateDouble("IGST_PER", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("IGST_PER").getString()));
                resultSetTemp.updateDouble("IGST_AMT", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("IGST_AMT").getString()));

                resultSetTemp.updateDouble("RCM_PER", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("RCM_PER").getString()));
                resultSetTemp.updateDouble("RCM_AMT", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("RCM_AMT").getString()));

                resultSetTemp.updateDouble("COMPOSITION_PER", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("COMPOSITION_PER").getString()));
                resultSetTemp.updateDouble("COMPOSITION_AMT", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("COMPOSITION_AMT").getString()));

                resultSetTemp.updateDouble("GST_COMPENSATION_CESS_PER", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("GST_COMPENSATION_CESS_PER").getString()));
                resultSetTemp.updateDouble("GST_COMPENSATION_CESS_AMT", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("GST_COMPENSATION_CESS_AMT").getString()));

                resultSetTemp.updateString("PR_BILL_LENGTH", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_LENGTH").getString());
                resultSetTemp.updateString("PR_BILL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_WIDTH").getString());
                resultSetTemp.updateString("PR_BILL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_WEIGHT").getString());
                resultSetTemp.updateString("PR_BILL_SQMTR", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_SQMTR").getString());
                resultSetTemp.updateString("PR_BILL_GSM", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_GSM").getString());
                resultSetTemp.updateString("PR_BILL_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_PRODUCT_CODE").getString());
                resultSetTemp.updateString("PR_BILL_STYLE", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_STYLE").getString());
                //       resultSetTemp.updateString("DATE_SLOT", ObjFeltSalesOrderDetails.getAttribute("DATE_SLOT").getString());

                resultSetTemp.updateString("TENDER_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("TENDER_WEIGHT").getString());
                resultSetTemp.updateString("TENDER_GSM", ObjFeltSalesOrderDetails.getAttribute("TENDER_GSM").getString());
                resultSetTemp.updateString("DM_REVISION_NO", ObjFeltSalesOrderDetails.getAttribute("DM_REVISION_NO").getString());
                resultSetTemp.updateString("PIECE_MERGE", ObjFeltSalesOrderDetails.getAttribute("PIECE_MERGE").getString());
                resultSetTemp.updateString("DUMMY_REF_NO", ObjFeltSalesOrderDetails.getAttribute("DUMMY_REF_NO").getString());
                resultSetTemp.insertRow();

                //int RevNoH=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_SALES_ORDER_DETAIL_H WHERE S_ORDER_NO='"+getAttribute("S_ORDER_NO").getString()+"'");
                //RevNoH++;
                resultSetHistory.moveToInsertRow();

                resultSetHistory.updateInt("REVISION_NO", RevNo);
                resultSetHistory.updateInt("SR_NO", i);
                resultSetHistory.updateString("S_ORDER_DETAIL_CODE", "0");
                resultSetHistory.updateString("S_ORDER_NO", ObjFeltSalesOrderDetails.getAttribute("S_ORDER_NO").getString());
                resultSetHistory.updateString("MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                resultSetHistory.updateString("POSITION", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                resultSetHistory.updateString("POSITION_DESC", ObjFeltSalesOrderDetails.getAttribute("POSITION_DESC").getString());
                resultSetHistory.updateString("UPN", ObjFeltSalesOrderDetails.getAttribute("UPN").getString());
                resultSetHistory.updateString("PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                resultSetHistory.updateString("LAYER_TYPE", ObjFeltSalesOrderDetails.getAttribute("LAYER_TYPE").getString());
                resultSetHistory.updateString("PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                resultSetHistory.updateString("PRODUCT_DESC", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_DESC").getString());
                resultSetHistory.updateString("S_GROUP", ObjFeltSalesOrderDetails.getAttribute("S_GROUP").getString());
                resultSetHistory.updateString("LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                resultSetHistory.updateString("WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                resultSetHistory.updateString("GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                resultSetHistory.updateString("THORITICAL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("THORITICAL_WIDTH").getString());
                resultSetHistory.updateString("SQ_MTR", ObjFeltSalesOrderDetails.getAttribute("SQ_MTR").getString());
                resultSetHistory.updateString("STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                resultSetHistory.updateString("REQ_MONTH", ObjFeltSalesOrderDetails.getAttribute("REQ_MONTH").getString());
                resultSetHistory.updateString("SYN_PER", ObjFeltSalesOrderDetails.getAttribute("SYN_PER").getString());
                resultSetHistory.updateString("REMARK", ObjFeltSalesOrderDetails.getAttribute("REMARK").getString());
                //JOptionPane.showMessageDialog(null, "OV_RATE = "+ObjFeltSalesOrderDetails.getAttribute("OV_RATE").getString());
                resultSetHistory.updateString("OV_RATE", ObjFeltSalesOrderDetails.getAttribute("OV_RATE").getString());
                
                resultSetHistory.updateString("SURCHARGE_PER", ObjFeltSalesOrderDetails.getAttribute("SURCHARGE_PER").getString());
                resultSetHistory.updateString("SURCHARGE_RATE", ObjFeltSalesOrderDetails.getAttribute("SURCHARGE_RATE").getString());
                resultSetHistory.updateString("GROSS_RATE", ObjFeltSalesOrderDetails.getAttribute("GROSS_RATE").getString());
                
                resultSetHistory.updateString("OV_BAS_AMOUNT", ObjFeltSalesOrderDetails.getAttribute("OV_BAS_AMOUNT").getString());
                resultSetHistory.updateString("OV_CHEM_TRT_CHG", ObjFeltSalesOrderDetails.getAttribute("OV_CHEM_TRT_CHG").getString());
                resultSetHistory.updateString("OV_SPIRAL_CHG", ObjFeltSalesOrderDetails.getAttribute("OV_SPIRAL_CHG").getString());
                resultSetHistory.updateString("OV_PIN_CHG", ObjFeltSalesOrderDetails.getAttribute("OV_PIN_CHG").getString());
                resultSetHistory.updateString("OV_SEAM_CHG", ObjFeltSalesOrderDetails.getAttribute("OV_SEAM_CHG").getString());
                resultSetHistory.updateString("OV_INS_IND", ObjFeltSalesOrderDetails.getAttribute("OV_INS_IND").getString());
                resultSetHistory.updateString("OV_INS_AMT", ObjFeltSalesOrderDetails.getAttribute("OV_INS_AMT").getString());
                resultSetHistory.updateString("OV_EXCISE", ObjFeltSalesOrderDetails.getAttribute("OV_EXCISE").getString());
                resultSetHistory.updateString("OV_DISC_PER", ObjFeltSalesOrderDetails.getAttribute("OV_DISC_PER").getString());
                resultSetHistory.updateString("OV_DISC_AMT", ObjFeltSalesOrderDetails.getAttribute("OV_DISC_AMT").getString());
                resultSetHistory.updateString("OV_DISC_BASAMT", ObjFeltSalesOrderDetails.getAttribute("OV_DISC_BASAMT").getString());
                resultSetHistory.updateString("OV_AMT", ObjFeltSalesOrderDetails.getAttribute("OV_AMT").getString());
                resultSetHistory.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                resultSetHistory.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
                resultSetHistory.updateInt("UPDATED_BY", EITLERPGLOBAL.gNewUserID);
                resultSetHistory.updateString("UPDATED_DATE", EITLERPGLOBAL.getCurrentDateTimeDB());

                resultSetHistory.updateDouble("CGST_PER", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("CGST_PER").getString()));
                resultSetHistory.updateDouble("CGST_AMT", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("CGST_AMT").getString()));

                resultSetHistory.updateDouble("SGST_PER", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("SGST_PER").getString()));
                resultSetHistory.updateDouble("SGST_AMT", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("SGST_AMT").getString()));

                resultSetHistory.updateDouble("IGST_PER", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("IGST_PER").getString()));
                resultSetHistory.updateDouble("IGST_AMT", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("IGST_AMT").getString()));

                resultSetHistory.updateDouble("RCM_PER", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("RCM_PER").getString()));
                resultSetHistory.updateDouble("RCM_AMT", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("RCM_AMT").getString()));

                resultSetHistory.updateDouble("COMPOSITION_PER", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("COMPOSITION_PER").getString()));
                resultSetHistory.updateDouble("COMPOSITION_AMT", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("COMPOSITION_AMT").getString()));

                resultSetHistory.updateDouble("GST_COMPENSATION_CESS_PER", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("GST_COMPENSATION_CESS_PER").getString()));
                resultSetHistory.updateDouble("GST_COMPENSATION_CESS_AMT", Double.valueOf(ObjFeltSalesOrderDetails.getAttribute("GST_COMPENSATION_CESS_AMT").getString()));

                resultSetHistory.updateString("PR_BILL_LENGTH", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_LENGTH").getString());
                resultSetHistory.updateString("PR_BILL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_WIDTH").getString());
                resultSetHistory.updateString("PR_BILL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_WEIGHT").getString());
                resultSetHistory.updateString("PR_BILL_SQMTR", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_SQMTR").getString());
                resultSetHistory.updateString("PR_BILL_GSM", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_GSM").getString());
                resultSetHistory.updateString("PR_BILL_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_PRODUCT_CODE").getString());
                resultSetHistory.updateString("PR_BILL_STYLE", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_STYLE").getString());
                //     resultSetHistory.updateString("DATE_SLOT", ObjFeltSalesOrderDetails.getAttribute("DATE_SLOT").getString());
                resultSetHistory.updateString("TENDER_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("TENDER_WEIGHT").getString());
                resultSetHistory.updateString("TENDER_GSM", ObjFeltSalesOrderDetails.getAttribute("TENDER_GSM").getString());
                resultSetHistory.updateString("DM_REVISION_NO", ObjFeltSalesOrderDetails.getAttribute("DM_REVISION_NO").getString());
                resultSetHistory.updateString("PIECE_MERGE", ObjFeltSalesOrderDetails.getAttribute("PIECE_MERGE").getString());
                resultSetHistory.updateString("DUMMY_REF_NO", ObjFeltSalesOrderDetails.getAttribute("DUMMY_REF_NO").getString());
                resultSetHistory.insertRow();

                // Final Approval and save to PIECE REGISTER 
                if (getAttribute("APPROVAL_STATUS").getString().equals("F") && !ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString().equals("")) {
                    
                    if(ObjFeltSalesOrderDetails.getAttribute("PIECE_MERGE").getString().equals("0"))
                    {
                            try {

                                stRegister = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                rsRegister = stRegister.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO=''");

                                rsRegister.moveToInsertRow();

                                rsRegister.updateString("PR_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                                rsRegister.updateString("PR_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                rsRegister.updateString("PR_ORDER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("S_ORDER_DATE").getString()));
                                rsRegister.updateString("PR_DOC_NO", ObjFeltSalesOrderDetails.getAttribute("S_ORDER_NO").getString());
                                rsRegister.updateString("PR_MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                                rsRegister.updateString("PR_POSITION_NO", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                                rsRegister.updateString("PR_PARTY_CODE", getAttribute("PARTY_CODE").getString());
                                rsRegister.updateString("PR_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                                rsRegister.updateString("PR_UPN", ObjFeltSalesOrderDetails.getAttribute("UPN").getString());
                                rsRegister.updateString("PR_GROUP", ObjFeltSalesOrderDetails.getAttribute("S_GROUP").getString());
                                rsRegister.updateString("PR_STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                                rsRegister.updateString("PR_LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                                rsRegister.updateString("PR_WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                                rsRegister.updateString("PR_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                                rsRegister.updateString("PR_THORITICAL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("THORITICAL_WIDTH").getString());
                                rsRegister.updateString("PR_SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQ_MTR").getString());
                                rsRegister.updateString("PR_SYN_PER", ObjFeltSalesOrderDetails.getAttribute("SYN_PER").getString());
                                rsRegister.updateString("PR_REQUESTED_MONTH", ObjFeltSalesOrderDetails.getAttribute("REQ_MONTH").getString());
                                rsRegister.updateString("PR_REGION", getAttribute("REGION").getString());
                                rsRegister.updateString("PR_INCHARGE", getAttribute("SALES_ENGINEER").getString());
                                rsRegister.updateString("PR_REFERENCE", getAttribute("REFERENCE").getString());
                                rsRegister.updateString("PR_REFERENCE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("REFERENCE_DATE").getString()));
                                rsRegister.updateString("PR_PO_NO", getAttribute("P_O_NO").getString());
                                rsRegister.updateString("PR_PO_DATE", EITLERPGLOBAL.formatDateDB((String) getAttribute("P_O_DATE").getObj()));
                                rsRegister.updateString("PR_ORDER_REMARK", getAttribute("REMARK").getString());
                                rsRegister.updateString("PR_PIECE_REMARK", ObjFeltSalesOrderDetails.getAttribute("REMARK").getString());
                                
                                rsRegister.updateString("PR_DESIGN_REVISION_NO", ObjFeltSalesOrderDetails.getAttribute("DM_REVISION_NO").getString());

                                //rsRegister.updateString("PR_PIECE_STAGE","WEAVING");
                                rsRegister.updateString("PR_PIECE_STAGE", "BOOKING");
                                rsRegister.updateString("PR_WIP_STATUS", "ACCEPTED");

                                rsRegister.updateString("PR_RATE_INDICATOR", "NEW");

                                if (ObjFeltSalesOrderDetails.getAttribute("LAYER_TYPE").getString().equals("WITHOUT_AB")) {
                                    rsRegister.updateString("PR_PRIORITY_HOLD_CAN_FLAG", "0");
                                    rsRegister.updateString("PR_PIECE_AB_FLAG", "");
                                } else if (ObjFeltSalesOrderDetails.getAttribute("LAYER_TYPE").getString().equals("WITH_AB")) {
                                    rsRegister.updateString("PR_PRIORITY_HOLD_CAN_FLAG", "0");
                                    rsRegister.updateString("PR_PIECE_AB_FLAG", "AB");
                                }
                                //
                                rsRegister.updateString("PR_DIVERSION_FLAG", "CLOSED");

                                rsRegister.updateString("PR_BILL_LENGTH", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_LENGTH").getString());
                                rsRegister.updateString("PR_BILL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_WIDTH").getString());
                                rsRegister.updateString("PR_BILL_GSM", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_GSM").getString());
                                rsRegister.updateString("PR_BILL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_WEIGHT").getString());
                                rsRegister.updateString("PR_BILL_SQMTR", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_SQMTR").getString());
                                rsRegister.updateString("PR_BILL_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_PRODUCT_CODE").getString());
                                rsRegister.updateString("PR_BILL_STYLE", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_STYLE").getString());
                                //        rsRegister.updateString("PR_DATE_SLOT",ObjFeltSalesOrderDetails.getAttribute("DATE_SLOT").getString());
                                rsRegister.updateString("PR_TENDER_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("TENDER_WEIGHT").getString());
                                rsRegister.updateString("PR_TENDER_GSM", ObjFeltSalesOrderDetails.getAttribute("TENDER_GSM").getString());

                                rsRegister.updateString("PR_CONTACT_PERSON", getAttribute("CONTACT_PERSON").getString());
                                rsRegister.updateString("PR_EMAIL_ID", getAttribute("EMAIL_ID").getString());
                                rsRegister.updateString("PR_PHONE_NO", getAttribute("PHONE_NUMBER").getString());

                                rsRegister.updateString("PR_WARP_DATE", "0000-00-00");
                                rsRegister.updateString("PR_WVG_DATE", "0000-00-00");
                                rsRegister.updateString("PR_MND_DATE", "0000-00-00");
                                rsRegister.updateString("PR_NDL_DATE", "0000-00-00");
                                rsRegister.updateString("PR_FNSG_DATE", "0000-00-00");
                                rsRegister.updateString("PR_RCV_DATE", "0000-00-00");
                                rsRegister.updateString("PR_PACKED_DATE", "0000-00-00");
                                rsRegister.updateString("PR_EXP_DISPATCH_DATE", "0000-00-00");
                                rsRegister.updateString("PR_INVOICE_DATE", "0000-00-00");
                                rsRegister.updateString("PR_LR_DATE", "0000-00-00");
                                rsRegister.updateString("PR_HOLD_DATE", "0000-00-00");
                                rsRegister.updateString("PR_RELEASE_DATE", "0000-00-00");

                                try {
                                    String UPN = ObjFeltSalesOrderDetails.getAttribute("UPN").getString();
                                    String Available_Piece = data.getStringValueFromDB("SELECT PR_PIECE_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECETRIAL_CATEGORY LIKE 'UPN%' AND PR_UPN='" + UPN + "' AND PR_PIECETRIAL_FLAG=1");
                                    if (!Available_Piece.equals("")) {
                                        rsRegister.updateString("PR_PIECETRIAL_CATEGORY", "UPN ORDER");
                                        rsRegister.updateString("PR_PIECETRIAL_FLAG", "1");
                                    } else if (!data.getStringValueFromDB("SELECT FT_PIECE_NO FROM PRODUCTION.FELT_TRAIL_PIECE_SELECTION WHERE FT_PIECE_NO='" + UPN + "' AND APPROVED=1 AND COALESCE(CANCELED,0)=0 ").equalsIgnoreCase("")) {
                                        rsRegister.updateString("PR_PIECETRIAL_CATEGORY", "UPN ORDER");
                                        rsRegister.updateString("PR_PIECETRIAL_FLAG", "1");
                                    } else {
                                        rsRegister.updateString("PR_PIECETRIAL_CATEGORY", "");
                                        rsRegister.updateString("PR_PIECETRIAL_FLAG", "0");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                rsRegister.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
                                rsRegister.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
                                rsRegister.updateString("MODIFIED_BY", EITLERPGLOBAL.gNewUserID + "");
                                rsRegister.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                rsRegister.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                                rsRegister.updateString("APPROVER_BY", EITLERPGLOBAL.gNewUserID + "");
                                rsRegister.updateString("APPROVER_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                rsRegister.updateString("APPROVER_REMARK", getAttribute("APPROVER_REMARKS").getString());

                                rsRegister.insertRow();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                if (ObjFeltSalesOrderDetails.getAttribute("LAYER_TYPE").getString().equals("WITHOUT_AB")) {
                                    stRegister = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                    rsRegister = stRegister.executeQuery("SELECT * FROM PRODUCTION.FELT_WIP_PIECE_REGISTER where WIP_PIECE_NO=''");

                                    rsRegister.moveToInsertRow();

                                    rsRegister.updateString("WIP_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                                    rsRegister.updateString("WIP_EXT_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                                    rsRegister.updateString("WIP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    rsRegister.updateString("WIP_ORDER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("S_ORDER_DATE").getString()));
                                    rsRegister.updateString("WIP_DOC_NO", ObjFeltSalesOrderDetails.getAttribute("S_ORDER_NO").getString());
                                    rsRegister.updateString("WIP_UPN", ObjFeltSalesOrderDetails.getAttribute("UPN").getString());
                                    rsRegister.updateString("WIP_MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                                    rsRegister.updateString("WIP_POSITION_NO", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                                    rsRegister.updateString("WIP_PARTY_CODE", getAttribute("PARTY_CODE").getString());
                                    rsRegister.updateString("WIP_GROUP", ObjFeltSalesOrderDetails.getAttribute("S_GROUP").getString());
                                    rsRegister.updateString("WIP_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                                    rsRegister.updateString("WIP_BILL_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_PRODUCT_CODE").getString());
                                    rsRegister.updateString("WIP_STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                                    rsRegister.updateString("WIP_BILL_STYLE", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_STYLE").getString());
                                    rsRegister.updateString("WIP_LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                                    rsRegister.updateString("WIP_ACTUAL_LENGTH", "");
                                    rsRegister.updateString("WIP_BILL_LENGTH", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_LENGTH").getString());
                                    rsRegister.updateString("WIP_WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                                    rsRegister.updateString("WIP_ACTUAL_WIDTH", "");
                                    rsRegister.updateString("WIP_BILL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_WIDTH").getString());
                                    rsRegister.updateString("WIP_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                                    rsRegister.updateString("WIP_TENDER_GSM", ObjFeltSalesOrderDetails.getAttribute("TENDER_GSM").getString());
                                    rsRegister.updateString("WIP_BILL_GSM", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_GSM").getString());
                                    rsRegister.updateString("WIP_THORITICAL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("THORITICAL_WIDTH").getString());
                                    rsRegister.updateString("WIP_ACTUAL_WEIGHT", "");
                                    rsRegister.updateString("WIP_TENDER_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("TENDER_WEIGHT").getString());
                                    rsRegister.updateString("WIP_BILL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_WEIGHT").getString());
                                    rsRegister.updateString("WIP_SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQ_MTR").getString());
                                    rsRegister.updateString("WIP_BILL_SQMTR", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_SQMTR").getString());

                                    rsRegister.updateString("WIP_PRIORITY_HOLD_CAN_FLAG", "0");

                                    rsRegister.updateString("WIP_PARTY_CODE_ORIGINAL", "");
                                    rsRegister.updateString("WIP_PIECE_NO_ORIGINAL", "");
                                    rsRegister.updateString("WIP_SYN_PER", ObjFeltSalesOrderDetails.getAttribute("SYN_PER").getString());
                                    rsRegister.updateString("WIP_REQUESTED_MONTH", ObjFeltSalesOrderDetails.getAttribute("REQ_MONTH").getString());
                                    rsRegister.updateString("WIP_REGION", getAttribute("REGION").getString());
                                    rsRegister.updateString("WIP_INCHARGE", getAttribute("SALES_ENGINEER").getString());
                                    rsRegister.updateString("WIP_REFERENCE", getAttribute("REFERENCE").getString());
                                    rsRegister.updateString("WIP_REFERENCE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("REFERENCE_DATE").getString()));
                                    rsRegister.updateString("WIP_PO_NO", getAttribute("P_O_NO").getString());
                                    rsRegister.updateString("WIP_PO_DATE", EITLERPGLOBAL.formatDateDB((String) getAttribute("P_O_DATE").getObj()));
                                    rsRegister.updateString("WIP_ORDER_REMARK", getAttribute("REMARK").getString());
                                    rsRegister.updateString("WIP_PIECE_REMARK", ObjFeltSalesOrderDetails.getAttribute("REMARK").getString());
                                    rsRegister.updateString("WIP_DESIGN_REVISION_NO", ObjFeltSalesOrderDetails.getAttribute("DM_REVISION_NO").getString());
                                    
                                    //ObjFeltSalesOrderDetails.getAttribute("DM_REVISION_NO").getString()
                                    //WIP_DESIGN_REVISION_NO
                                    //rsRegister.updateString("","");
                                    rsRegister.updateString("WIP_FELT_VALUE_WITH_GST", "");
                                    rsRegister.updateString("WIP_FELT_VALUE_WITHOUT_GST", "");
                                    rsRegister.updateString("WIP_FELT_BASE_VALUE", "");
                                    rsRegister.updateString("WIP_DAYS_ORDER_WARPED", "");
                                    rsRegister.updateString("WIP_DIVERSION_FLAG", "");
                                    rsRegister.updateString("WIP_DIVERSION_REASON", "");
                                    rsRegister.updateString("WIP_DIVERTED_FLAG", "CLOSED");
                                    rsRegister.updateString("WIP_DIVERTED_REASON", "");
                                    rsRegister.updateString("WIP_OA_NO", "");
                                    rsRegister.updateString("WIP_OA_DATE", "");
                                    rsRegister.updateString("WIP_OC_NO", "");
                                    rsRegister.updateString("WIP_OC_MONTHYEAR", "");
                                    rsRegister.updateString("WIP_CURRENT_SCH_MONTH", "");

                                    rsRegister.updateString("WIP_PIECE_STAGE", "BOOKING");
                                    rsRegister.updateString("WIP_STATUS", "ACCEPTED");

                                    rsRegister.insertRow();

                                } else if (ObjFeltSalesOrderDetails.getAttribute("LAYER_TYPE").getString().equals("WITH_AB")) {

                                    //1st PIECE DETAIL
                                    stRegister = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                    rsRegister = stRegister.executeQuery("SELECT * FROM PRODUCTION.FELT_WIP_PIECE_REGISTER where WIP_PIECE_NO=''");

                                    rsRegister.moveToInsertRow();

                                    rsRegister.updateString("WIP_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                                    rsRegister.updateString("WIP_EXT_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString() + "-A");
                                    rsRegister.updateString("WIP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    rsRegister.updateString("WIP_ORDER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("S_ORDER_DATE").getString()));
                                    rsRegister.updateString("WIP_DOC_NO", ObjFeltSalesOrderDetails.getAttribute("S_ORDER_NO").getString());
                                    rsRegister.updateString("WIP_UPN", ObjFeltSalesOrderDetails.getAttribute("UPN").getString());
                                    rsRegister.updateString("WIP_MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                                    rsRegister.updateString("WIP_POSITION_NO", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                                    rsRegister.updateString("WIP_PARTY_CODE", getAttribute("PARTY_CODE").getString());
                                    rsRegister.updateString("WIP_GROUP", ObjFeltSalesOrderDetails.getAttribute("S_GROUP").getString());
                                    rsRegister.updateString("WIP_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                                    rsRegister.updateString("WIP_BILL_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_PRODUCT_CODE").getString());
                                    rsRegister.updateString("WIP_STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                                    rsRegister.updateString("WIP_BILL_STYLE", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_STYLE").getString());
                                    rsRegister.updateString("WIP_LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                                    rsRegister.updateString("WIP_ACTUAL_LENGTH", "");
                                    rsRegister.updateString("WIP_BILL_LENGTH", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_LENGTH").getString());
                                    rsRegister.updateString("WIP_WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                                    rsRegister.updateString("WIP_ACTUAL_WIDTH", "");
                                    rsRegister.updateString("WIP_BILL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_WIDTH").getString());
                                    rsRegister.updateString("WIP_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                                    rsRegister.updateString("WIP_TENDER_GSM", ObjFeltSalesOrderDetails.getAttribute("TENDER_GSM").getString());
                                    rsRegister.updateString("WIP_BILL_GSM", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_GSM").getString());
                                    rsRegister.updateString("WIP_THORITICAL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("THORITICAL_WIDTH").getString());
                                    rsRegister.updateString("WIP_ACTUAL_WEIGHT", "");
                                    rsRegister.updateString("WIP_TENDER_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("TENDER_WEIGHT").getString());
                                    rsRegister.updateString("WIP_BILL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_WEIGHT").getString());
                                    rsRegister.updateString("WIP_SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQ_MTR").getString());
                                    rsRegister.updateString("WIP_BILL_SQMTR", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_SQMTR").getString());

                                    rsRegister.updateString("WIP_PRIORITY_HOLD_CAN_FLAG", "0");
                                    rsRegister.updateString("WIP_PIECE_AB_FLAG", "AB");
                                    //
                                    rsRegister.updateString("WIP_DESIGN_REVISION_NO", ObjFeltSalesOrderDetails.getAttribute("DM_REVISION_NO").getString());

                                    rsRegister.updateString("WIP_PARTY_CODE_ORIGINAL", "");
                                    rsRegister.updateString("WIP_PIECE_NO_ORIGINAL", "");
                                    rsRegister.updateString("WIP_SYN_PER", ObjFeltSalesOrderDetails.getAttribute("SYN_PER").getString());
                                    rsRegister.updateString("WIP_REQUESTED_MONTH", ObjFeltSalesOrderDetails.getAttribute("REQ_MONTH").getString());
                                    rsRegister.updateString("WIP_REGION", getAttribute("REGION").getString());
                                    rsRegister.updateString("WIP_INCHARGE", getAttribute("SALES_ENGINEER").getString());
                                    rsRegister.updateString("WIP_REFERENCE", getAttribute("REFERENCE").getString());
                                    rsRegister.updateString("WIP_REFERENCE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("REFERENCE_DATE").getString()));
                                    rsRegister.updateString("WIP_PO_NO", getAttribute("P_O_NO").getString());
                                    rsRegister.updateString("WIP_PO_DATE", EITLERPGLOBAL.formatDateDB((String) getAttribute("P_O_DATE").getObj()));
                                    rsRegister.updateString("WIP_ORDER_REMARK", getAttribute("REMARK").getString());
                                    rsRegister.updateString("WIP_PIECE_REMARK", ObjFeltSalesOrderDetails.getAttribute("REMARK").getString());

                                    //rsRegister.updateString("","");
                                    rsRegister.updateString("WIP_FELT_VALUE_WITH_GST", "");
                                    rsRegister.updateString("WIP_FELT_VALUE_WITHOUT_GST", "");
                                    rsRegister.updateString("WIP_FELT_BASE_VALUE", "");
                                    rsRegister.updateString("WIP_DAYS_ORDER_WARPED", "");
                                    rsRegister.updateString("WIP_DIVERSION_FLAG", "");
                                    rsRegister.updateString("WIP_DIVERSION_REASON", "");
                                    rsRegister.updateString("WIP_DIVERTED_FLAG", "CLOSED");
                                    rsRegister.updateString("WIP_DIVERTED_REASON", "");
                                    rsRegister.updateString("WIP_OA_NO", "");
                                    rsRegister.updateString("WIP_OA_DATE", "");
                                    rsRegister.updateString("WIP_OC_NO", "");
                                    rsRegister.updateString("WIP_OC_MONTHYEAR", "");
                                    rsRegister.updateString("WIP_CURRENT_SCH_MONTH", "");

                                    rsRegister.updateString("WIP_PIECE_STAGE", "BOOKING");
                                    rsRegister.updateString("WIP_STATUS", "ACCEPTED");

                                    rsRegister.insertRow();

                                    //2nd PIECE DETAIL
                                    stRegister = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                    rsRegister = stRegister.executeQuery("SELECT * FROM PRODUCTION.FELT_WIP_PIECE_REGISTER where WIP_PIECE_NO=''");

                                    rsRegister.moveToInsertRow();

                                    rsRegister.updateString("WIP_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                                    rsRegister.updateString("WIP_EXT_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString() + "-B");
                                    rsRegister.updateString("WIP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    rsRegister.updateString("WIP_ORDER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("S_ORDER_DATE").getString()));
                                    rsRegister.updateString("WIP_DOC_NO", ObjFeltSalesOrderDetails.getAttribute("S_ORDER_NO").getString());
                                    rsRegister.updateString("WIP_UPN", ObjFeltSalesOrderDetails.getAttribute("UPN").getString());
                                    rsRegister.updateString("WIP_MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                                    rsRegister.updateString("WIP_POSITION_NO", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                                    rsRegister.updateString("WIP_PARTY_CODE", getAttribute("PARTY_CODE").getString());
                                    rsRegister.updateString("WIP_GROUP", ObjFeltSalesOrderDetails.getAttribute("S_GROUP").getString());
                                    rsRegister.updateString("WIP_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                                    rsRegister.updateString("WIP_BILL_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_PRODUCT_CODE").getString());
                                    rsRegister.updateString("WIP_STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                                    rsRegister.updateString("WIP_BILL_STYLE", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_STYLE").getString());
                                    rsRegister.updateString("WIP_LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                                    rsRegister.updateString("WIP_ACTUAL_LENGTH", "");
                                    rsRegister.updateString("WIP_BILL_LENGTH", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_LENGTH").getString());
                                    rsRegister.updateString("WIP_WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                                    rsRegister.updateString("WIP_ACTUAL_WIDTH", "");
                                    rsRegister.updateString("WIP_BILL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_WIDTH").getString());
                                    rsRegister.updateString("WIP_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                                    rsRegister.updateString("WIP_TENDER_GSM", ObjFeltSalesOrderDetails.getAttribute("TENDER_GSM").getString());
                                    rsRegister.updateString("WIP_BILL_GSM", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_GSM").getString());
                                    rsRegister.updateString("WIP_THORITICAL_WEIGHT", "0");
                                    rsRegister.updateString("WIP_ACTUAL_WEIGHT", "");
                                    rsRegister.updateString("WIP_TENDER_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("TENDER_WEIGHT").getString());
                                    rsRegister.updateString("WIP_BILL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_WEIGHT").getString());
                                    rsRegister.updateString("WIP_SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQ_MTR").getString());
                                    rsRegister.updateString("WIP_BILL_SQMTR", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_SQMTR").getString());

                                    rsRegister.updateString("WIP_DESIGN_REVISION_NO", ObjFeltSalesOrderDetails.getAttribute("DM_REVISION_NO").getString());
                                    
                                    rsRegister.updateString("WIP_PRIORITY_HOLD_CAN_FLAG", "0");
                                    rsRegister.updateString("WIP_PIECE_AB_FLAG", "AB");

                                    rsRegister.updateString("WIP_PARTY_CODE_ORIGINAL", "");
                                    rsRegister.updateString("WIP_PIECE_NO_ORIGINAL", "");
                                    rsRegister.updateString("WIP_SYN_PER", ObjFeltSalesOrderDetails.getAttribute("SYN_PER").getString());
                                    rsRegister.updateString("WIP_REQUESTED_MONTH", ObjFeltSalesOrderDetails.getAttribute("REQ_MONTH").getString());
                                    rsRegister.updateString("WIP_REGION", getAttribute("REGION").getString());
                                    rsRegister.updateString("WIP_INCHARGE", getAttribute("SALES_ENGINEER").getString());
                                    rsRegister.updateString("WIP_REFERENCE", getAttribute("REFERENCE").getString());
                                    rsRegister.updateString("WIP_REFERENCE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("REFERENCE_DATE").getString()));
                                    rsRegister.updateString("WIP_PO_NO", getAttribute("P_O_NO").getString());
                                    rsRegister.updateString("WIP_PO_DATE", EITLERPGLOBAL.formatDateDB((String) getAttribute("P_O_DATE").getObj()));
                                    rsRegister.updateString("WIP_ORDER_REMARK", getAttribute("REMARK").getString());
                                    rsRegister.updateString("WIP_PIECE_REMARK", ObjFeltSalesOrderDetails.getAttribute("REMARK").getString());

                                    //rsRegister.updateString("","");
                                    rsRegister.updateString("WIP_FELT_VALUE_WITH_GST", "");
                                    rsRegister.updateString("WIP_FELT_VALUE_WITHOUT_GST", "");
                                    rsRegister.updateString("WIP_FELT_BASE_VALUE", "");
                                    rsRegister.updateString("WIP_DAYS_ORDER_WARPED", "");
                                    rsRegister.updateString("WIP_DIVERSION_FLAG", "");
                                    rsRegister.updateString("WIP_DIVERSION_REASON", "");
                                    rsRegister.updateString("WIP_DIVERTED_FLAG", "CLOSED");
                                    rsRegister.updateString("WIP_DIVERTED_REASON", "");
                                    rsRegister.updateString("WIP_OA_NO", "");
                                    rsRegister.updateString("WIP_OA_DATE", "");
                                    rsRegister.updateString("WIP_OC_NO", "");
                                    rsRegister.updateString("WIP_OC_MONTHYEAR", "");
                                    rsRegister.updateString("WIP_CURRENT_SCH_MONTH", "");

                                    rsRegister.updateString("WIP_PIECE_STAGE", "BOOKING");
                                    rsRegister.updateString("WIP_STATUS", "ACCEPTED");

                                    rsRegister.insertRow();

                                    //3rd PIECE DETAIL
                                    stRegister = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                    rsRegister = stRegister.executeQuery("SELECT * FROM PRODUCTION.FELT_WIP_PIECE_REGISTER where WIP_PIECE_NO=''");

                                    rsRegister.moveToInsertRow();

                                    rsRegister.updateString("WIP_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                                    rsRegister.updateString("WIP_EXT_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString() + "-AB");
                                    rsRegister.updateString("WIP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                    rsRegister.updateString("WIP_ORDER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("S_ORDER_DATE").getString()));
                                    rsRegister.updateString("WIP_DOC_NO", ObjFeltSalesOrderDetails.getAttribute("S_ORDER_NO").getString());
                                    rsRegister.updateString("WIP_UPN", ObjFeltSalesOrderDetails.getAttribute("UPN").getString());
                                    rsRegister.updateString("WIP_MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                                    rsRegister.updateString("WIP_POSITION_NO", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                                    rsRegister.updateString("WIP_PARTY_CODE", getAttribute("PARTY_CODE").getString());
                                    rsRegister.updateString("WIP_GROUP", ObjFeltSalesOrderDetails.getAttribute("S_GROUP").getString());
                                    rsRegister.updateString("WIP_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                                    rsRegister.updateString("WIP_BILL_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_PRODUCT_CODE").getString());
                                    rsRegister.updateString("WIP_STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                                    rsRegister.updateString("WIP_BILL_STYLE", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_STYLE").getString());
                                    rsRegister.updateString("WIP_LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                                    rsRegister.updateString("WIP_ACTUAL_LENGTH", "");
                                    rsRegister.updateString("WIP_BILL_LENGTH", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_LENGTH").getString());
                                    rsRegister.updateString("WIP_WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                                    rsRegister.updateString("WIP_ACTUAL_WIDTH", "");
                                    rsRegister.updateString("WIP_BILL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_WIDTH").getString());
                                    rsRegister.updateString("WIP_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                                    rsRegister.updateString("WIP_TENDER_GSM", ObjFeltSalesOrderDetails.getAttribute("TENDER_GSM").getString());
                                    rsRegister.updateString("WIP_BILL_GSM", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_GSM").getString());
                                    rsRegister.updateString("WIP_THORITICAL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("THORITICAL_WIDTH").getString());
                                    rsRegister.updateString("WIP_ACTUAL_WEIGHT", "");
                                    rsRegister.updateString("WIP_TENDER_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("TENDER_WEIGHT").getString());
                                    rsRegister.updateString("WIP_BILL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_WEIGHT").getString());
                                    rsRegister.updateString("WIP_SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQ_MTR").getString());
                                    rsRegister.updateString("WIP_BILL_SQMTR", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_SQMTR").getString());

                                    rsRegister.updateString("WIP_DESIGN_REVISION_NO", ObjFeltSalesOrderDetails.getAttribute("DM_REVISION_NO").getString());
                                    
                                    rsRegister.updateString("WIP_PRIORITY_HOLD_CAN_FLAG", "1");
                                    rsRegister.updateString("WIP_PIECE_AB_FLAG", "AB");

                                    rsRegister.updateString("WIP_PARTY_CODE_ORIGINAL", "");
                                    rsRegister.updateString("WIP_PIECE_NO_ORIGINAL", "");
                                    rsRegister.updateString("WIP_SYN_PER", ObjFeltSalesOrderDetails.getAttribute("SYN_PER").getString());
                                    rsRegister.updateString("WIP_REQUESTED_MONTH", ObjFeltSalesOrderDetails.getAttribute("REQ_MONTH").getString());
                                    rsRegister.updateString("WIP_REGION", getAttribute("REGION").getString());
                                    rsRegister.updateString("WIP_INCHARGE", getAttribute("SALES_ENGINEER").getString());
                                    rsRegister.updateString("WIP_REFERENCE", getAttribute("REFERENCE").getString());
                                    rsRegister.updateString("WIP_REFERENCE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("REFERENCE_DATE").getString()));
                                    rsRegister.updateString("WIP_PO_NO", getAttribute("P_O_NO").getString());
                                    rsRegister.updateString("WIP_PO_DATE", EITLERPGLOBAL.formatDateDB((String) getAttribute("P_O_DATE").getObj()));
                                    rsRegister.updateString("WIP_ORDER_REMARK", getAttribute("REMARK").getString());
                                    rsRegister.updateString("WIP_PIECE_REMARK", ObjFeltSalesOrderDetails.getAttribute("REMARK").getString());

                                    //rsRegister.updateString("","");
                                    rsRegister.updateString("WIP_FELT_VALUE_WITH_GST", "");
                                    rsRegister.updateString("WIP_FELT_VALUE_WITHOUT_GST", "");
                                    rsRegister.updateString("WIP_FELT_BASE_VALUE", "");
                                    rsRegister.updateString("WIP_DAYS_ORDER_WARPED", "");
                                    rsRegister.updateString("WIP_DIVERSION_FLAG", "");
                                    rsRegister.updateString("WIP_DIVERSION_REASON", "");
                                    rsRegister.updateString("WIP_DIVERTED_FLAG", "CLOSED");
                                    rsRegister.updateString("WIP_DIVERTED_REASON", "");
                                    rsRegister.updateString("WIP_OA_NO", "");
                                    rsRegister.updateString("WIP_OA_DATE", "");
                                    rsRegister.updateString("WIP_OC_NO", "");
                                    rsRegister.updateString("WIP_OC_MONTHYEAR", "");
                                    rsRegister.updateString("WIP_CURRENT_SCH_MONTH", "");

                                    rsRegister.updateString("WIP_PIECE_STAGE", "BOOKING");
                                    rsRegister.updateString("WIP_STATUS", "ACCEPTED");

                                    rsRegister.insertRow();

                                }

                                /*
                                 rsRegister.updateString("WIP_WARP_LAYER_REMARK","");
                                 rsRegister.updateString("WIP_WARP_DATE","");
                                 rsRegister.updateString("WIP_WARP_A_DATE","");
                                 rsRegister.updateString("WIP_WARP_B_DATE","");
                                 rsRegister.updateString("WIP_WARPING_WEIGHT","");
                                 rsRegister.updateString("WIP_WARPING_WEIGHT_A","");
                                 rsRegister.updateString("WIP_WARPING_WEIGHT_B","");
                                 rsRegister.updateString("WIP_WVG_LAYER_REMARK","");
                                 rsRegister.updateString("WIP_WVG_DATE","");
                                 rsRegister.updateString("WIP_WVG_A_DATE","");
                                 rsRegister.updateString("WIP_WVG_B_DATE","");
                                 rsRegister.updateString("WIP_WEAVING_WEIGHT","");
                                 rsRegister.updateString("WIP_WEAVING_WEIGHT_A","");
                                 rsRegister.updateString("WIP_WEAVING_WEIGHT_B","");
                                 rsRegister.updateString("WIP_MND_LAYER_REMARK","");
                                 rsRegister.updateString("WIP_MND_DATE","");
                                 rsRegister.updateString("WIP_MND_A_DATE","");
                                 rsRegister.updateString("WIP_MND_B_DATE","");
                                 rsRegister.updateString("WIP_MENDING_WEIGHT","");
                                 rsRegister.updateString("WIP_MENDING_WEIGHT_A","");
                                 */
                                //, , , , WIP_OC_LAST_DDMMYY, , PR_REFERENCE, WIP_OC_DATE
                                //   , , , ,  , , , , WIP_EXP_DISPATCH_DATE, WIP_HOLD_DATE, WIP_HOLD_REASON, WIP_RELEASE_DATE, WIP_OBSOLETE, WIP_OBSOLETE_REASON, WIP_OBSOLETE_DATE, WIP_MFG_MONTH, WIP_MFG_YEAR, WIP_MFG_SPILL_OVEVER_REMARK, WIP_WARP_EXECUTE_DATE, WIP_DAYS_WH_STOCK, WIP_DAYS_WH_PACKED, WIP_SCHEDULE_MONTH, WIP_CLOSURE_REOPEN_IND, WIP_CLOSURE_DATE, WIP_CLOSURE_REMARK, WIP_REOPEN_DATE, WIP_REOPEN_REMARK, WIP_DAYS_CURRENT_STAGE, WIP_EXPECTED_DISPATCH, WIP_EXP_DISPATCH_FROM, WIP_EXP_DISPATCH_DOCNO, WIP_ADJUSTABLE_LENGTH, WIP_ADJUSTABLE_WIDTH, WIP_ADJUSTABLE_GSM, WIP_ADJUSTABLE_WEIGHT, WIP_PIECE_IT_DEPT_REMARK, , FELT_WIP_PIECE_REGISTERcol
                                //WIP_DAYS_ORDER_WARPED_STATUS, WIP_DAYS_ORDER_WVG, WIP_DAYS_ORDER_WVG_STATUS, WIP_DAYS_ORDER_MND, WIP_DAYS_ORDER_MND_STATUS, WIP_DAYS_ORDER_NDL, WIP_DAYS_ORDER_NDL_STATUS, WIP_DAYS_ORDER_FNG, WIP_DAYS_ORDER_FNG_STATUS, WIP_DAYS_WRP_WVG, WIP_DAYS_WRP_WVG_STATUS, WIP_DAYS_WVG_MND, WIP_DAYS_WVG_MND_STATUS, WIP_DAYS_MND_NDL, WIP_DAYS_MND_NDL_STATUS, WIP_DAYS_NDL_FNG, WIP_DAYS_NDL_FNG_STATUS, WIP_DAYS_MND_FNG, WIP_DAYS_MND_FNG_STATUS, WIP_DAYS_STATUS, WIP_REJECTED_FLAG, WIP_REJECTED_REMARK,
                                //WIP_MENDING_WEIGHT_B, WIP_NDL_DATE, WIP_NEEDLING_WEIGHT, WIP_SPLICE_DATE, WIP_SPLICE_WEIGHT, WIP_SEAM_DATE, WIP_SEAM_WEIGHT, WIP_FNSG_DATE,
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                    }
                    else
                    {
                        String Doc_No = clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 854, 368, true);
                        clsFeltPieceMerging objDM = new clsFeltPieceMerging();
                        objDM.setAttribute("DOC_NO", Doc_No);
                        objDM.setAttribute("DOC_DATE", EITLERPGLOBAL.formatDate(EITLERPGLOBAL.getCurrentDateDB()));

                        objDM.setAttribute("UPN", ObjFeltSalesOrderDetails.getAttribute("UPN").getString());
                        objDM.setAttribute("PIECE_NO", "");
                        objDM.setAttribute("PARTY_CODE", (String) getAttribute("PARTY_CODE").getObj());
                        objDM.setAttribute("PARTY_NAME", (String) getAttribute("PARTY_NAME").getObj());
                        objDM.setAttribute("LENGTH", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_LENGTH").getString());
                        objDM.setAttribute("WIDTH", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_WIDTH").getString());
                        objDM.setAttribute("GSM", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_GSM").getString());
                        objDM.setAttribute("SQMTR", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_SQMTR").getString());
                        objDM.setAttribute("WEIGHT", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_WEIGHT").getString());
                        objDM.setAttribute("PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_PRODUCT_CODE").getString());
                        objDM.setAttribute("PRODUCT_GROUP", ObjFeltSalesOrderDetails.getAttribute("S_GROUP").getString());
                        objDM.setAttribute("PRODUCT_STYLE", ObjFeltSalesOrderDetails.getAttribute("PR_BILL_STYLE").getString());
                        objDM.setAttribute("EXISTING_PIECE_VALUE", ObjFeltSalesOrderDetails.getAttribute("OV_BAS_AMOUNT").getString());
                        objDM.setAttribute("ORDER_UPN_VALUE", "");
                        objDM.setAttribute("PROFIT_LOSS", "");
                        objDM.setAttribute("ORDER_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                        objDM.setAttribute("ORDER_REF_NO", getAttribute("S_ORDER_NO").getString());

                        objDM.setAttribute("FRESH_BOOKING", "0");

                        objDM.setAttribute("REMARK", "");
                        
                        objDM.setAttribute("MODULE_ID", 854);
                        
                        objDM.setAttribute("USER_ID", 338);

                        //----- Update Approval Specific Fields -----------//
                        objDM.setAttribute("HIERARCHY_ID", 4471);
                        objDM.setAttribute("FROM", 0);
                        objDM.setAttribute("TO", 338);
                        objDM.setAttribute("FROM_REMARKS", "");
                        
                        objDM.setAttribute("APPROVAL_STATUS", "H");
    
                        objDM.setAttribute("CREATED_BY", 338);
                        objDM.setAttribute("CREATED_DATE", EITLERPGLOBAL.getCurrentDateTimeDB());
                        objDM.setAttribute("MODIFIED_BY", 338);
                        objDM.setAttribute("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                        objDM.setAttribute("UPDATED_BY", 338);
                        objDM.setAttribute("UPDATED_DATE", EITLERPGLOBAL.getCurrentDateDB());

                        //======= Set Line part ============
                        try {
                            objDM.hmFeltPieceMergingDetails.clear();
                        } catch (Exception e) {
                            System.out.println("Error on setData : " + e.getMessage());
                            e.printStackTrace();
                        };
                        
                        if(objDM.Insert())
                        {
                            System.out.println("Record Inserted successfully ");
                        }
                    }
                    
                    //Exit
                }
            }

            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = ModuleID; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("S_ORDER_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.FELT_SALES_ORDER_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator = true;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "S_ORDER_NO";

            if (getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFeltProductionApprovalFlow.To = (int) getAttribute("SEND_DOC_TO").getVal();
                ObjFeltProductionApprovalFlow.ExplicitSendTo = true;
            }
            //if(getAttribute("APPROVAL_STATUS").getString().equals("A") || getAttribute("APPROVAL_STATUS").getString().equals("R")) {
//                    String Subject = "Felt Order Pending Document : "+getAttribute("S_ORDER_NO").getString();
//                    String Message = "Document No : "+getAttribute("S_ORDER_NO").getString()+" is added in your PENDING DOCUMENT"
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
                data.Execute("UPDATE PRODUCTION.FELT_SALES_ORDER_HEADER SET REJECTED=0,CHANGED=1 WHERE S_ORDER_NO ='" + getAttribute("S_ORDER_NO").getString() + "'");
                ObjFeltProductionApprovalFlow.IsCreator = true;
            }
            //==========================================//

            if (ObjFeltProductionApprovalFlow.Status.equals("H")) {
                if (IsRejected) {
                    ObjFeltProductionApprovalFlow.Status = "A";
                    ObjFeltProductionApprovalFlow.To = ObjFeltProductionApprovalFlow.From;
                    ObjFeltProductionApprovalFlow.UpdateFlow();
                }
            } else {
                if (!ObjFeltProductionApprovalFlow.UpdateFlow()) {
                    LastError = ObjFeltProductionApprovalFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------

            // Update  in Order Master Table To confirm that Weaving has completed
            if (ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {

                ObjFeltProductionApprovalFlow.finalApproved = false;
                    UpdatePicks();
            }

            setData();
            resultSetTemp.close();
            statementTemp.close();
            resultSetHistory.close();
            statementHistory.close();
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_SALES_ORDER_HEADER WHERE  S_ORDER_NO='" + documentNo + "' AND APPROVED=" + 1;
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=" + ModuleID + " AND USER_ID=" + userID + " AND DOC_NO='" + documentNo + "' AND STATUS='W'";
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();

                if (rsTmp.getInt("COUNT") > 0) {
                    strSQL = "DELETE FROM PRODUCTION.FELT_SALES_ORDER_HEADER WHERE "
                            + " S_ORDER_DATE = '" + EITLERPGLOBAL.formatDateDB(stringProductionDate) + "'"
                            + " AND S_ORDER_NO ='" + documentNo + "'";

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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_SALES_ORDER_HEADER WHERE S_ORDER_NO='" + orderupdDocumentNo + "' AND APPROVED=1";
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=" + ModuleID + " AND USER_ID=" + userID + " AND DOC_NO='" + orderupdDocumentNo + "' AND STATUS='W'";
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
            String strSql = "SELECT * FROM PRODUCTION.FELT_SALES_ORDER_HEADER WHERE  " + stringFindQuery + "";
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

            setAttribute("S_ORDER_NO", resultSet.getString("S_ORDER_NO"));
            setAttribute("S_ORDER_DATE", resultSet.getDate("S_ORDER_DATE"));
            setAttribute("REGION", resultSet.getString("REGION"));
            setAttribute("SALES_ENGINEER", resultSet.getString("SALES_ENGINEER"));
            setAttribute("PARTY_CODE", resultSet.getString("PARTY_CODE"));
            setAttribute("PARTY_NAME", resultSet.getString("PARTY_NAME"));
            setAttribute("REFERENCE", resultSet.getString("REFERENCE"));
            setAttribute("REFERENCE_DATE", resultSet.getDate("REFERENCE_DATE"));
            setAttribute("P_O_NO", resultSet.getString("P_O_NO"));
            setAttribute("P_O_DATE", resultSet.getDate("P_O_DATE"));
            setAttribute("REMARK", resultSet.getString("REMARK"));
            setAttribute("OLD_PIECE", resultSet.getBoolean("OLD_PIECE"));
            setAttribute("TENDER_PARTY", resultSet.getBoolean("TENDER_PARTY"));
            setAttribute("EMAIL_UPDATE_TO_PM", resultSet.getBoolean("EMAIL_UPDATE_TO_PM"));
            
            setAttribute("CONTACT_PERSON_POSITION", resultSet.getString("CONTACT_PERSON_POSITION"));
            setAttribute("CONTACT_PERSON_DINESH", resultSet.getString("CONTACT_PERSON_DINESH"));
            
            setAttribute("EXPORT_PAYMENT_TYPE", resultSet.getString("EXPORT_PAYMENT_TYPE"));
            setAttribute("EXPORT_PAYMENT_DATE", resultSet.getString("EXPORT_PAYMENT_DATE"));
            setAttribute("EXPORT_PAYMENT_REMARK", resultSet.getString("EXPORT_PAYMENT_REMARK"));
            
            setAttribute("CONTACT_PERSON", resultSet.getString("CONTACT_PERSON"));
            setAttribute("EMAIL_ID", resultSet.getString("EMAIL_ID"));
            setAttribute("PHONE_NUMBER", resultSet.getString("PHONE_NUMBER"));
            setAttribute("OA_NO", resultSet.getString("OA_NO"));
            setAttribute("OA_DATE", resultSet.getString("OA_DATE"));

            setAttribute("EMAIL_ID2", resultSet.getString("EMAIL_ID2"));
            setAttribute("EMAIL_ID3", resultSet.getString("EMAIL_ID3"));

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

            hmFeltSalesOrderDetails.clear();

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            if (HistoryView) {
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_ORDER_DETAIL_H WHERE S_ORDER_NO='" + resultSet.getString("S_ORDER_NO") + "'  AND REVISION_NO=" + RevNo + " ORDER BY SR_NO DESC");
            } else {
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_ORDER_DETAIL WHERE S_ORDER_NO='" + resultSet.getString("S_ORDER_NO") + "'  ORDER BY S_ORDER_NO");
            }
            while (resultSetTemp.next()) {
                serialNoCounter++;

                clsFeltSalesOrderDetails ObjFeltSalesOrderDetails = new clsFeltSalesOrderDetails();

                //ObjFeltSalesOrderDetails.setAttribute("SR_NO",serialNoCounter);
                // ObjFeltSalesOrderDetails.setAttribute("S_ORDER_DETAIL_CODE",resultSetTemp.getString("S_ORDER_DETAIL_CODE"));
                ObjFeltSalesOrderDetails.setAttribute("S_ORDER_NO", resultSetTemp.getString("S_ORDER_NO"));
                ObjFeltSalesOrderDetails.setAttribute("MACHINE_NO", resultSetTemp.getString("MACHINE_NO"));
                ObjFeltSalesOrderDetails.setAttribute("POSITION", resultSetTemp.getString("POSITION"));
                ObjFeltSalesOrderDetails.setAttribute("POSITION_DESC", resultSetTemp.getString("POSITION_DESC"));
                ObjFeltSalesOrderDetails.setAttribute("UPN", resultSetTemp.getString("UPN"));
                ObjFeltSalesOrderDetails.setAttribute("PIECE_NO", resultSetTemp.getString("PIECE_NO"));
                ObjFeltSalesOrderDetails.setAttribute("LAYER_TYPE", resultSetTemp.getString("LAYER_TYPE"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT_CODE", resultSetTemp.getString("PRODUCT_CODE"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT_DESC", resultSetTemp.getString("PRODUCT_DESC"));
                ObjFeltSalesOrderDetails.setAttribute("S_GROUP", resultSetTemp.getString("S_GROUP"));
                ObjFeltSalesOrderDetails.setAttribute("LENGTH", resultSetTemp.getString("LENGTH"));
                ObjFeltSalesOrderDetails.setAttribute("WIDTH", resultSetTemp.getString("WIDTH"));
                ObjFeltSalesOrderDetails.setAttribute("GSM", resultSetTemp.getString("GSM"));
                ObjFeltSalesOrderDetails.setAttribute("THORITICAL_WIDTH", resultSetTemp.getString("THORITICAL_WIDTH"));
                ObjFeltSalesOrderDetails.setAttribute("SQ_MTR", resultSetTemp.getString("SQ_MTR"));
                ObjFeltSalesOrderDetails.setAttribute("STYLE", resultSetTemp.getString("STYLE"));
                ObjFeltSalesOrderDetails.setAttribute("REQ_MONTH", resultSetTemp.getString("REQ_MONTH"));
                ObjFeltSalesOrderDetails.setAttribute("SYN_PER", resultSetTemp.getString("SYN_PER"));
                ObjFeltSalesOrderDetails.setAttribute("REMARK", resultSetTemp.getString("REMARK"));
                ObjFeltSalesOrderDetails.setAttribute("OV_RATE", resultSetTemp.getString("OV_RATE"));
                
                ObjFeltSalesOrderDetails.setAttribute("SURCHARGE_PER", resultSetTemp.getString("SURCHARGE_PER"));
                ObjFeltSalesOrderDetails.setAttribute("SURCHARGE_RATE", resultSetTemp.getString("SURCHARGE_RATE"));
                ObjFeltSalesOrderDetails.setAttribute("GROSS_RATE", resultSetTemp.getString("GROSS_RATE"));
                
                ObjFeltSalesOrderDetails.setAttribute("OV_BAS_AMOUNT", resultSetTemp.getString("OV_BAS_AMOUNT"));
                ObjFeltSalesOrderDetails.setAttribute("OV_CHEM_TRT_CHG", resultSetTemp.getString("OV_CHEM_TRT_CHG"));
                ObjFeltSalesOrderDetails.setAttribute("OV_SPIRAL_CHG", resultSetTemp.getString("OV_SPIRAL_CHG"));
                ObjFeltSalesOrderDetails.setAttribute("OV_PIN_CHG", resultSetTemp.getString("OV_PIN_CHG"));
                ObjFeltSalesOrderDetails.setAttribute("OV_SEAM_CHG", resultSetTemp.getString("OV_SEAM_CHG"));
                ObjFeltSalesOrderDetails.setAttribute("OV_INS_IND", resultSetTemp.getString("OV_INS_IND"));
                ObjFeltSalesOrderDetails.setAttribute("OV_INS_AMT", resultSetTemp.getString("OV_INS_AMT"));
                ObjFeltSalesOrderDetails.setAttribute("OV_EXCISE", resultSetTemp.getString("OV_EXCISE"));
                ObjFeltSalesOrderDetails.setAttribute("OV_DISC_PER", resultSetTemp.getString("OV_DISC_PER"));
                ObjFeltSalesOrderDetails.setAttribute("OV_DISC_AMT", resultSetTemp.getString("OV_DISC_AMT"));
                ObjFeltSalesOrderDetails.setAttribute("OV_DISC_BASAMT", resultSetTemp.getString("OV_DISC_BASAMT"));
                ObjFeltSalesOrderDetails.setAttribute("OV_AMT", resultSetTemp.getString("OV_AMT"));

                ObjFeltSalesOrderDetails.setAttribute("CGST_PER", resultSetTemp.getString("CGST_PER"));
                ObjFeltSalesOrderDetails.setAttribute("CGST_AMT", resultSetTemp.getString("CGST_AMT"));

                ObjFeltSalesOrderDetails.setAttribute("SGST_PER", resultSetTemp.getString("SGST_PER"));
                ObjFeltSalesOrderDetails.setAttribute("SGST_AMT", resultSetTemp.getString("SGST_AMT"));

                ObjFeltSalesOrderDetails.setAttribute("IGST_PER", resultSetTemp.getString("IGST_PER"));
                ObjFeltSalesOrderDetails.setAttribute("IGST_AMT", resultSetTemp.getString("IGST_AMT"));

                ObjFeltSalesOrderDetails.setAttribute("RCM_PER", resultSetTemp.getString("RCM_PER"));
                ObjFeltSalesOrderDetails.setAttribute("RCM_AMT", resultSetTemp.getString("RCM_AMT"));

                ObjFeltSalesOrderDetails.setAttribute("COMPOSITION_PER", resultSetTemp.getString("COMPOSITION_PER"));
                ObjFeltSalesOrderDetails.setAttribute("COMPOSITION_AMT", resultSetTemp.getString("COMPOSITION_AMT"));

                ObjFeltSalesOrderDetails.setAttribute("GST_COMPENSATION_CESS_PER", resultSetTemp.getString("GST_COMPENSATION_CESS_PER"));
                ObjFeltSalesOrderDetails.setAttribute("GST_COMPENSATION_CESS_AMT", resultSetTemp.getString("GST_COMPENSATION_CESS_AMT"));

                ObjFeltSalesOrderDetails.setAttribute("PR_BILL_LENGTH", resultSetTemp.getString("PR_BILL_LENGTH"));
                ObjFeltSalesOrderDetails.setAttribute("PR_BILL_WIDTH", resultSetTemp.getString("PR_BILL_WIDTH"));
                ObjFeltSalesOrderDetails.setAttribute("PR_BILL_WEIGHT", resultSetTemp.getString("PR_BILL_WEIGHT"));
                ObjFeltSalesOrderDetails.setAttribute("PR_BILL_SQMTR", resultSetTemp.getString("PR_BILL_SQMTR"));
                ObjFeltSalesOrderDetails.setAttribute("PR_BILL_GSM", resultSetTemp.getString("PR_BILL_GSM"));
                ObjFeltSalesOrderDetails.setAttribute("PR_BILL_PRODUCT_CODE", resultSetTemp.getString("PR_BILL_PRODUCT_CODE"));
                ObjFeltSalesOrderDetails.setAttribute("PR_BILL_STYLE", resultSetTemp.getString("PR_BILL_STYLE"));
//                ObjFeltSalesOrderDetails.setAttribute("DATE_SLOT",resultSetTemp.getString("DATE_SLOT"));

                ObjFeltSalesOrderDetails.setAttribute("TENDER_WEIGHT", resultSetTemp.getString("TENDER_WEIGHT"));
                ObjFeltSalesOrderDetails.setAttribute("TENDER_GSM", resultSetTemp.getString("TENDER_GSM"));
                ObjFeltSalesOrderDetails.setAttribute("DM_REVISION_NO", resultSetTemp.getString("DM_REVISION_NO"));
                ObjFeltSalesOrderDetails.setAttribute("PIECE_MERGE", resultSetTemp.getInt("PIECE_MERGE"));
                ObjFeltSalesOrderDetails.setAttribute("DUMMY_REF_NO", resultSetTemp.getString("DUMMY_REF_NO"));
                
                hmFeltSalesOrderDetails.put(Integer.toString(serialNoCounter), ObjFeltSalesOrderDetails);
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
            hmFeltSalesOrderDetails.clear();

            String strSql = "SELECT * FROM PRODUCTION.FELT_SALES_ORDER_HEADER_H WHERE S_ORDER_NO = " + pDocNo + "";
            System.out.println("STR SQL : " + strSql);
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery(strSql);
            resultSet.first();
            setAttribute("S_ORDER_NO", resultSet.getString("S_ORDER_NO"));
            setAttribute("REVISION_NO", resultSet.getString("REVISION_NO"));
            setAttribute("UPDATED_BY", resultSet.getString("UPDATED_BY"));
            setAttribute("S_ORDER_DATE", resultSet.getString("S_ORDER_DATE"));
            setAttribute("HIERARCHY_ID", resultSet.getInt("HIERARCHY_ID"));

            setAttribute("S_ORDER_NO", resultSet.getString("S_ORDER_NO"));
            setAttribute("S_ORDER_DATE", resultSet.getDate("S_ORDER_DATE"));
            setAttribute("REGION", resultSet.getString("REGION"));
            setAttribute("SALES_ENGINEER", resultSet.getString("SALES_ENGINEER"));
            setAttribute("PARTY_CODE", resultSet.getString("PARTY_CODE"));
            setAttribute("PARTY_NAME", resultSet.getString("PARTY_NAME"));
            setAttribute("REFERENCE", resultSet.getString("REFERENCE"));
            setAttribute("REFERENCE_DATE", resultSet.getDate("REFERENCE_DATE"));
            setAttribute("P_O_NO", resultSet.getString("P_O_NO"));
            setAttribute("P_O_DATE", resultSet.getDate("P_O_DATE"));
            setAttribute("REMARK", resultSet.getString("REMARK"));

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_ORDER_DETAIL_H WHERE S_ORDER_NO='" + pDocNo + "'");

            while (resultSetTemp.next()) {
                serialNoCounter++;

                clsFeltSalesOrderDetails ObjFeltSalesOrderDetails = new clsFeltSalesOrderDetails();

                //ObjFeltSalesOrderDetails.setAttribute("SR_NO",serialNoCounter);
                // ObjFeltSalesOrderDetails.setAttribute("S_ORDER_DETAIL_CODE",resultSetTemp.getString("S_ORDER_DETAIL_CODE"));
                ObjFeltSalesOrderDetails.setAttribute("S_ORDER_NO", resultSetTemp.getString("S_ORDER_NO"));
                ObjFeltSalesOrderDetails.setAttribute("MACHINE_NO", resultSetTemp.getString("MACHINE_NO"));
                ObjFeltSalesOrderDetails.setAttribute("POSITION", resultSetTemp.getString("POSITION"));
                ObjFeltSalesOrderDetails.setAttribute("POSITION_DESC", resultSetTemp.getString("POSITION_DESC"));
                ObjFeltSalesOrderDetails.setAttribute("PIECE_NO", resultSetTemp.getString("PIECE_NO"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT_CODE", resultSetTemp.getString("PRODUCT_CODE"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT_DESC", resultSetTemp.getString("PRODUCT_DESC"));
                ObjFeltSalesOrderDetails.setAttribute("S_GROUP", resultSetTemp.getString("S_GROUP"));
                ObjFeltSalesOrderDetails.setAttribute("LENGTH", resultSetTemp.getString("LENGTH"));
                ObjFeltSalesOrderDetails.setAttribute("WIDTH", resultSetTemp.getString("WIDTH"));
                ObjFeltSalesOrderDetails.setAttribute("GSM", resultSetTemp.getString("GSM"));
                ObjFeltSalesOrderDetails.setAttribute("THORITICAL_WIDTH", resultSetTemp.getString("THORITICAL_WIDTH"));
                ObjFeltSalesOrderDetails.setAttribute("SQ_MTR", resultSetTemp.getString("SQ_MTR"));
                ObjFeltSalesOrderDetails.setAttribute("STYLE", resultSetTemp.getString("STYLE"));
                ObjFeltSalesOrderDetails.setAttribute("REQ_MONTH", resultSetTemp.getString("REQ_MONTH"));
                ObjFeltSalesOrderDetails.setAttribute("SYN_PER", resultSetTemp.getString("SYN_PER"));
                ObjFeltSalesOrderDetails.setAttribute("REMARK", resultSetTemp.getString("REMARK"));
                ObjFeltSalesOrderDetails.setAttribute("OV_RATE", resultSetTemp.getString("OV_RATE"));
                ObjFeltSalesOrderDetails.setAttribute("OV_BAS_AMOUNT", resultSetTemp.getString("OV_BAS_AMOUNT"));
                ObjFeltSalesOrderDetails.setAttribute("OV_CHEM_TRT_CHG", resultSetTemp.getString("OV_CHEM_TRT_CHG"));
                ObjFeltSalesOrderDetails.setAttribute("OV_SPIRAL_CHG", resultSetTemp.getString("OV_SPIRAL_CHG"));
                ObjFeltSalesOrderDetails.setAttribute("OV_PIN_CHG", resultSetTemp.getString("OV_PIN_CHG"));
                ObjFeltSalesOrderDetails.setAttribute("OV_SEAM_CHG", resultSetTemp.getString("OV_SEAM_CHG"));
                ObjFeltSalesOrderDetails.setAttribute("OV_INS_IND", resultSetTemp.getString("OV_INS_IND"));
                ObjFeltSalesOrderDetails.setAttribute("OV_INS_AMT", resultSetTemp.getString("OV_INS_AMT"));
                ObjFeltSalesOrderDetails.setAttribute("OV_EXCISE", resultSetTemp.getString("OV_EXCISE"));
                ObjFeltSalesOrderDetails.setAttribute("OV_DISC_PER", resultSetTemp.getString("OV_DISC_PER"));
                ObjFeltSalesOrderDetails.setAttribute("OV_DISC_AMT", resultSetTemp.getString("OV_DISC_AMT"));
                ObjFeltSalesOrderDetails.setAttribute("OV_DISC_BASAMT", resultSetTemp.getString("OV_DISC_BASAMT"));
                ObjFeltSalesOrderDetails.setAttribute("OV_AMT", resultSetTemp.getString("OV_AMT"));

                hmFeltSalesOrderDetails.put(Integer.toString(serialNoCounter), ObjFeltSalesOrderDetails);
            }
            resultSetTemp.close();
            statementTemp.close();
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

            rsTmp = stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_HEADER_H WHERE S_ORDER_NO='" + productionDocumentNo + "'");
            while (rsTmp.next()) {
                clsFeltOrder felt_order = new clsFeltOrder();

                felt_order.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                felt_order.setAttribute("UPDATED_BY", rsTmp.getString("UPDATED_BY"));
                felt_order.setAttribute("UPDATED_DATE", rsTmp.getString("UPDATED_DATE"));
                felt_order.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                felt_order.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                felt_order.setAttribute("APPROVER_REMARKS", rsTmp.getString("APPROVER_REMARKS"));
                felt_order.setAttribute("FROM_IP", rsTmp.getString("FROM_IP"));

                hmHistoryList.put(Integer.toString(hmHistoryList.size() + 1), felt_order);
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
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_HEADER_H WHERE S_ORDER_NO ='" + pDocNo + "'");
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
                strSQL = "SELECT DISTINCT S_ORDER_NO,S_ORDER_DATE,RECEIVED_DATE,PARTY_CODE FROM PRODUCTION.FELT_SALES_ORDER_HEADER, PRODUCTION.FELT_PROD_DOC_DATA   WHERE S_ORDER_NO=DOC_NO AND STATUS='W' AND MODULE_ID=" + ModuleID + " AND CANCELED=0  AND USER_ID=" + pUserID + " ORDER BY RECEIVED_DATE,S_ORDER_NO";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT S_ORDER_NO,S_ORDER_DATE,RECEIVED_DATE,PARTY_CODE FROM PRODUCTION.FELT_SALES_ORDER_HEADER, PRODUCTION.FELT_PROD_DOC_DATA   WHERE S_ORDER_NO=DOC_NO AND STATUS='W' AND MODULE_ID=" + ModuleID + " AND CANCELED=0  AND USER_ID=" + pUserID + " ORDER BY RECEIVED_DATE,S_ORDER_NO";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT S_ORDER_NO,S_ORDER_DATE,RECEIVED_DATE,PARTY_CODE FROM PRODUCTION.FELT_SALES_ORDER_HEADER, PRODUCTION.FELT_PROD_DOC_DATA   WHERE S_ORDER_NO=DOC_NO AND STATUS='W' AND MODULE_ID=" + ModuleID + " AND CANCELED=0  AND USER_ID=" + pUserID + "  ORDER BY S_ORDER_NO";
            }

            ResultSet rsTmp = data.getConn().createStatement().executeQuery(strSQL);

            Counter = 0;
            while (rsTmp.next()) {

                Counter = Counter + 1;
                clsFeltOrder ObjDoc = new clsFeltOrder();

                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO", rsTmp.getString("S_ORDER_NO"));
                ObjDoc.setAttribute("DOC_DATE", rsTmp.getString("S_ORDER_DATE"));
                ObjDoc.setAttribute("RECEIVED_DATE", rsTmp.getString("RECEIVED_DATE"));
                ObjDoc.setAttribute("PARTY_CODE", rsTmp.getString("PARTY_CODE"));
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
            System.out.println("SELECT S_ORDER_NO FROM PRODUCTION.FELT_SALES_ORDER_HEADER WHERE S_ORDER_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");
            rsTmp = stTmp.executeQuery("SELECT S_ORDER_NO FROM PRODUCTION.FELT_SALES_ORDER_HEADER WHERE S_ORDER_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");

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
                rsTmp = data.getResult("SELECT APPROVED FROM PRODUCTION.FELT_SALES_ORDER_HEADER WHERE S_ORDER_NO='" + pAmendNo + "' ");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    Approved = rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();

                if (!Approved) {
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='" + pAmendNo + "' AND MODULE_ID=602");
                }
                data.Execute("UPDATE PRODUCTION.FELT_SALES_ORDER_HEADER SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE S_ORDER_NO='" + pAmendNo + "'");
                //String Original_Piece_No = data.getStringValueFromDB("SELECT ORIGINAL_PIECE_NO FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION WHERE S_ORDER_NO='"+pAmendNo+"'");
                //data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_DIVERSION_FLAG='READY' WHERE PR_PIECE_NO='"+Original_Piece_No+"'");

            } catch (Exception e) {

            }
        }

    }
    
    public static void UpdatePicks() {
        data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W,PRODUCTION.FELT_DESIGN_MASTER_HEADER D "
                + "SET WIP_DESIGN_THEO_PICKS = THEO_PICKS "
                + "WHERE WIP_UPN = UPN_NO AND D.DESIGN_REVISION_NO = W.WIP_DESIGN_REVISION_NO AND COALESCE( W.WIP_DESIGN_REVISION_NO,0)!=0 "
                + "AND RIGHT(WIP_EXT_PIECE_NO,2) = '-A' AND COALESCE(WIP_DESIGN_THEO_PICKS,0)=0 ");
        
        data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W,PRODUCTION.FELT_DESIGN_MASTER_HEADER D "
                + "SET WIP_DESIGN_THEO_PICKS = THEO_PICKS_MULTILAYER "
                + "WHERE WIP_UPN = UPN_NO AND D.DESIGN_REVISION_NO = W.WIP_DESIGN_REVISION_NO AND COALESCE( W.WIP_DESIGN_REVISION_NO,0)!=0 "
                + "AND RIGHT(WIP_EXT_PIECE_NO,2) = '-B' AND COALESCE(WIP_DESIGN_THEO_PICKS,0)=0 ");
        
        data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W,PRODUCTION.FELT_DESIGN_MASTER_HEADER D "
                + "SET WIP_DESIGN_THEO_PICKS = THEO_PICKS "
                + "WHERE WIP_UPN = UPN_NO AND D.DESIGN_REVISION_NO = W.WIP_DESIGN_REVISION_NO AND COALESCE( W.WIP_DESIGN_REVISION_NO,0)!=0 "
                + "AND WIP_EXT_PIECE_NO = WIP_PIECE_NO  AND COALESCE(WIP_DESIGN_THEO_PICKS,0)=0 ");
        
        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER W,PRODUCTION.FELT_DESIGN_MASTER_HEADER D "
                + "SET PR_DESIGN_THEO_PICKS = COALESCE(THEO_PICKS,0)+COALESCE(THEO_PICKS_MULTILAYER,0) "
                + "WHERE PR_UPN = UPN_NO AND D.DESIGN_REVISION_NO = W.PR_DESIGN_REVISION_NO AND COALESCE( W.PR_DESIGN_REVISION_NO,0)!=0 "
                + "AND COALESCE(PR_DESIGN_THEO_PICKS,0)=0 ");
    }
}
