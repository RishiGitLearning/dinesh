/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.DiversionLoss;

import EITLERP.FeltSales.OrderDiversion.*;
import EITLERP.ComboData;
import EITLERP.EITLERPGLOBAL;
import EITLERP.JavaMail.JMail;
import EITLERP.Production.clsFeltProductionApprovalFlow;
import EITLERP.Variant;
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
 * @author DAXESH PRAJAPATI
 */
public class clsFeltOrderDiversionLoss {

    public String LastError = "";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    //private ResultSet rsResultSet,

    public HashMap props;
    public boolean Ready = false;
    //Felt Order Collection
    public HashMap hmFeltSalesOrderDiversion;

    //History Related properties
    public boolean HistoryView = false;
    private String historyAmendDate = "";
    private String historyAmendID = "";
    private static int ModuleID = 779;

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
    public clsFeltOrderDiversionLoss() {
        LastError = "";
        props = new HashMap();
        props.put("SD_ORDER_NO", new Variant(""));
        props.put("SD_ORDER_DATE", new Variant(""));
        props.put("D_REGION", new Variant(""));
        props.put("D_SALES_ENGINEER", new Variant(""));
        props.put("D_PARTY_CODE", new Variant(""));
        props.put("D_PARTY_NAME", new Variant(""));
        props.put("D_REFERENCE", new Variant(""));
        props.put("D_REFERENCE_DATE", new Variant(""));
        props.put("D_P_O_NO", new Variant(""));
        props.put("D_P_O_DATE", new Variant(""));
        props.put("D_REASON_FOR_DIVERSION", new Variant(""));
        props.put("D_DELIVERY_STATUS_FOR_EXISTING_CLIENT", new Variant(""));
        props.put("D_REMARK", new Variant(""));
        props.put("D_PIECE_NO", new Variant(""));
        props.put("D_PRODUCT_NO", new Variant(""));
        props.put("D_PRODUCT_DESC", new Variant(""));
        props.put("D_MACHINE_NO", new Variant(""));
        props.put("D_POSITION_NO", new Variant(""));
        props.put("D_POSITION_DESC", new Variant(""));
        props.put("D_STYLE_NO", new Variant(""));

        props.put("D_GROUP", new Variant(""));
        props.put("D_LENGTH", new Variant(""));
        props.put("D_GSM", new Variant(""));
        props.put("D_THORITICAL_WEIGHT", new Variant(""));
        props.put("D_SQ_MTR", new Variant(""));
        props.put("D_REQ_MONTH", new Variant(""));
        props.put("D_SYN_PER", new Variant(""));
        props.put("D_OV_RATE", new Variant(""));
        props.put("D_OV_BAS_AMOUNT", new Variant(""));
        props.put("D_OV_CHEM_TRT_CHG", new Variant(""));
        props.put("D_OV_SPIRAL_CHG", new Variant(""));
        props.put("D_OV_PIN_CHG", new Variant(""));
        props.put("D_OV_SEAM_CHG", new Variant(""));
        props.put("D_OV_AMT", new Variant(""));

        props.put("ORIGINAL_PARTY_CODE", new Variant(""));
        props.put("ORIGINAL_PARTY_NAME", new Variant(""));
        props.put("ORIGINAL_PIECE_NO", new Variant(""));
        props.put("ORIGINAL_PRODUCT_NO", new Variant(""));
        props.put("ORIGINAL_MACHINE_NO", new Variant(""));
        props.put("ORIGINAL_POSITION_NO", new Variant(""));
        props.put("ORIGINAL_STYLE_NO", new Variant(""));
        props.put("ORIGINAL_GROUP", new Variant(""));
        props.put("ORIGINAL_LENGTH", new Variant(""));
        props.put("ORIGINAL_GSM", new Variant(""));
        props.put("ORIGINAL_THORITICAL_WEIGHT", new Variant(""));
        props.put("ORIGINAL_SQ_MTR", new Variant(""));
        props.put("ORIGINAL_OV_RATE", new Variant(""));
        props.put("ORIGINAL_OV_AMOUNT", new Variant(""));

        props.put("DEBIT_NOTE_NO", new Variant(""));
        props.put("DEBIT_AMT", new Variant(""));
        props.put("COST_BEARER", new Variant(""));
        props.put("BEARER_PARTY_CODE", new Variant(""));
        props.put("BEARER_PARTY_NAME", new Variant(""));
        props.put("DIFFERENCE_AMT", new Variant(""));
        props.put("L_G_AMT", new Variant(""));

        props.put("CREATED_BY", new Variant(""));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("CHANGED", new Variant(""));
        props.put("CHANGED_DATE", new Variant(""));

        props.put("REJECTED", new Variant(""));
        props.put("REJECTED_BY", new Variant(""));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("APPROVED", new Variant(""));
        props.put("APPROVED_BY", new Variant(""));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("CANCELED", new Variant(""));
        props.put("HIERARCHY_ID", new Variant(""));

        props.put("DOC_NO", new Variant(""));
        props.put("DOC_DATE", new Variant(""));
        props.put("MODULE_ID", new Variant(""));
        props.put("USER_ID", new Variant(""));
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

        props.put("PR_BILL_LENGTH", new Variant(""));
        props.put("PR_BILL_WIDTH", new Variant(""));
        props.put("PR_BILL_WEIGHT", new Variant(""));
        props.put("PR_BILL_SQMTR", new Variant(""));
        props.put("PR_BILL_GSM", new Variant(""));
        props.put("PR_BILL_PRODUCT_CODE", new Variant(""));

        props.put("PR_ACTUAL_WEIGHT", new Variant(""));
        props.put("PR_PIECE_STAGE", new Variant(""));

        props.put("ACTION1", new Variant(""));
        props.put("ACTION2", new Variant(""));
        props.put("ACTION3", new Variant(""));
        props.put("ACTION4", new Variant(""));
        props.put("ACTION5", new Variant(""));
        props.put("ACTION6", new Variant(""));
        props.put("ACTION7", new Variant(""));
        props.put("ACTION8", new Variant(""));

        props.put("ACTION9", new Variant(""));
        props.put("ACTION10", new Variant(""));
        props.put("ACTION11", new Variant(""));
        props.put("ACTION12", new Variant(""));
        props.put("ACTION13", new Variant(""));
        props.put("ACTION14", new Variant(""));
        props.put("ACTION15", new Variant(""));
        props.put("ACTION16", new Variant(""));
        props.put("ACTION17", new Variant(""));

        props.put("DEBITMEMO_NO2", new Variant(""));
        props.put("DEBITMEMO_AMT2", new Variant(""));

        props.put("DEBITMEMO_NO3", new Variant(""));
        props.put("DEBITMEMO_AMT3", new Variant(""));

        props.put("DEBITMEMO_NO4", new Variant(""));
        props.put("DEBITMEMO_AMT4", new Variant(""));

        props.put("DEBITMEMO_NO5", new Variant(""));
        props.put("DEBITMEMO_AMT5", new Variant(""));

        props.put("DEBITMEMO_NO6", new Variant(""));
        props.put("DEBITMEMO_AMT6", new Variant(""));

        props.put("DEBITMEMO_NO7", new Variant(""));
        props.put("DEBITMEMO_AMT7", new Variant(""));

        props.put("DEBITMEMO_NO8", new Variant(""));
        props.put("DEBITMEMO_AMT8", new Variant(""));

        props.put("DEBITMEMO_NO9", new Variant(""));
        props.put("DEBITMEMO_AMT9", new Variant(""));

        props.put("DEBITMEMO_NO10", new Variant(""));
        props.put("DEBITMEMO_AMT10", new Variant(""));

        props.put("NO_ACTION", new Variant(false));

        props.put("DISCOUNT_PER", new Variant(""));
        props.put("DISCOUNT_AMT", new Variant(""));

        hmFeltSalesOrderDiversion = new HashMap();
    }

    public boolean LoadData() {
        Ready = false;
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL ORDER BY SD_ORDER_NO");
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
        ResultSet resultSet, resultSetH;
        Statement statement, statementH;
        try {
            // Felt Order Updation data connection
            connection = data.getConn();

            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL WHERE SD_ORDER_NO=''");

            statementH = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetH = statementH.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL_H WHERE SD_ORDER_NO=''");

            resultSet.first();
            resultSet.moveToInsertRow();

            resultSet.updateString("SD_ORDER_NO", getAttribute("SD_ORDER_NO").getString());
            resultSet.updateString("SD_ORDER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("SD_ORDER_DATE").getString()));
            resultSet.updateString("D_REGION", getAttribute("D_REGION").getString());
            resultSet.updateString("D_SALES_ENGINEER", getAttribute("D_SALES_ENGINEER").getString());
            resultSet.updateString("D_PARTY_CODE", getAttribute("D_PARTY_CODE").getString());
            resultSet.updateString("D_PARTY_NAME", getAttribute("D_PARTY_NAME").getString());
            resultSet.updateString("D_REFERENCE", getAttribute("D_REFERENCE").getString());
            resultSet.updateString("D_REFERENCE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("D_REFERENCE_DATE").getString()));
            resultSet.updateString("D_REASON_FOR_DIVERSION", getAttribute("D_REASON_FOR_DIVERSION").getString());
            resultSet.updateString("D_DELIVERY_STATUS_FOR_EXISTING_CLIENT", getAttribute("D_DELIVERY_STATUS_FOR_EXISTING_CLIENT").getString());
            resultSet.updateString("D_REMARK", getAttribute("D_REMARK").getString());
            resultSet.updateString("D_PIECE_NO", getAttribute("D_PIECE_NO").getString());
            resultSet.updateString("D_PRODUCT_NO", getAttribute("D_PRODUCT_NO").getString());
            resultSet.updateString("D_PRODUCT_DESC", getAttribute("D_PRODUCT_DESC").getString());
            resultSet.updateString("D_MACHINE_NO", getAttribute("D_MACHINE_NO").getString());
            resultSet.updateString("D_POSITION_NO", getAttribute("D_POSITION_NO").getString());
            resultSet.updateString("D_POSITION_DESC", getAttribute("D_POSITION_DESC").getString());
            resultSet.updateString("D_STYLE_NO", getAttribute("D_STYLE_NO").getString());
            resultSet.updateString("D_GROUP", getAttribute("D_GROUP").getString());
            resultSet.updateString("D_LENGTH", getAttribute("D_LENGTH").getString());
            resultSet.updateString("D_WIDTH", getAttribute("D_WIDTH").getString());
            resultSet.updateString("D_GSM", getAttribute("D_GSM").getString());
            resultSet.updateString("D_THORITICAL_WEIGHT", getAttribute("D_THORITICAL_WEIGHT").getString());
            resultSet.updateString("D_SQ_MTR", getAttribute("D_SQ_MTR").getString());
            resultSet.updateString("D_REQ_MONTH", getAttribute("D_REQ_MONTH").getString());
            resultSet.updateString("D_SYN_PER", getAttribute("D_SYN_PER").getString());
            resultSet.updateFloat("D_OV_RATE", Float.parseFloat(getAttribute("D_OV_RATE").getString()));
            resultSet.updateFloat("D_OV_BAS_AMOUNT", Float.parseFloat(getAttribute("D_OV_BAS_AMOUNT").getString()));
            resultSet.updateFloat("D_OV_CHEM_TRT_CHG", Float.parseFloat(getAttribute("D_OV_CHEM_TRT_CHG").getString()));
            resultSet.updateFloat("D_OV_SPIRAL_CHG", Float.parseFloat(getAttribute("D_OV_SPIRAL_CHG").getString()));
            resultSet.updateFloat("D_OV_PIN_CHG", Float.parseFloat(getAttribute("D_OV_PIN_CHG").getString()));
            resultSet.updateFloat("D_OV_SEAM_CHG", Float.parseFloat(getAttribute("D_OV_SEAM_CHG").getString()));
            resultSet.updateFloat("D_OV_AMT", Float.parseFloat(getAttribute("D_OV_AMT").getString()));
            resultSet.updateString("ORIGINAL_PARTY_CODE", getAttribute("ORIGINAL_PARTY_CODE").getString());
            resultSet.updateString("ORIGINAL_PARTY_NAME", getAttribute("ORIGINAL_PARTY_NAME").getString());
            resultSet.updateString("ORIGINAL_PIECE_NO", getAttribute("ORIGINAL_PIECE_NO").getString());
            resultSet.updateString("ORIGINAL_PRODUCT_NO", getAttribute("ORIGINAL_PRODUCT_NO").getString());
            resultSet.updateString("ORIGINAL_MACHINE_NO", getAttribute("ORIGINAL_MACHINE_NO").getString());
            resultSet.updateString("ORIGINAL_POSITION_NO", getAttribute("ORIGINAL_POSITION_NO").getString());
            resultSet.updateString("ORIGINAL_STYLE_NO", getAttribute("ORIGINAL_STYLE_NO").getString());
            resultSet.updateString("ORIGINAL_GROUP", getAttribute("ORIGINAL_GROUP").getString());
            resultSet.updateString("ORIGINAL_LENGTH", getAttribute("ORIGINAL_LENGTH").getString());
            resultSet.updateString("ORIGINAL_WIDTH", getAttribute("ORIGINAL_WIDTH").getString());
            resultSet.updateString("ORIGINAL_GSM", getAttribute("ORIGINAL_GSM").getString());
            resultSet.updateString("ORIGINAL_THORITICAL_WEIGHT", getAttribute("ORIGINAL_THORITICAL_WEIGHT").getString());
            resultSet.updateString("ORIGINAL_SQ_MTR", getAttribute("ORIGINAL_SQ_MTR").getString());
            resultSet.updateFloat("ORIGINAL_OV_RATE", Float.parseFloat(getAttribute("ORIGINAL_OV_RATE").getVal() + ""));
            resultSet.updateFloat("ORIGINAL_OV_AMOUNT", Float.parseFloat(getAttribute("ORIGINAL_OV_AMOUNT").getVal() + ""));
            resultSet.updateString("DEBIT_NOTE_NO", getAttribute("DEBIT_NOTE_NO").getString());
            resultSet.updateString("DEBIT_AMT", getAttribute("DEBIT_AMT").getString());
            resultSet.updateString("COST_BEARER", getAttribute("COST_BEARER").getString());
            resultSet.updateString("BEARER_PARTY_CODE", getAttribute("BEARER_PARTY_CODE").getString());
            resultSet.updateString("BEARER_PARTY_NAME", getAttribute("BEARER_PARTY_NAME").getString());
            resultSet.updateString("DIFFERENCE_AMT", getAttribute("DIFFERENCE_AMT").getString());
            resultSet.updateString("L_G_AMT", "");
            resultSet.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            resultSet.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateInt("MODIFIED_BY", 0);
            resultSet.updateString("MODIFIED_DATE", "0000-00-00");
            resultSet.updateBoolean("CHANGED", false);
            resultSet.updateString("CHANGED_DATE", EITLERPGLOBAL.formatDateDB("0000-00-00"));
            resultSet.updateBoolean("REJECTED", false);
            resultSet.updateString("REJECTED_BY", "");
            resultSet.updateString("REJECTED_DATE", "0000-00-00");
            resultSet.updateBoolean("APPROVED", false);
            resultSet.updateString("APPROVED_BY", "");
            resultSet.updateString("APPROVED_DATE", "0000-00-00");
            resultSet.updateBoolean("CANCELED", false);
            resultSet.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());

            resultSet.updateDouble("CGST_PER", 0.0);
            resultSet.updateDouble("CGST_AMT", 0.0);
            resultSet.updateDouble("SGST_PER", 0.0);
            resultSet.updateDouble("SGST_AMT", 0.0);
            resultSet.updateDouble("IGST_PER", 0.0);
            resultSet.updateDouble("IGST_AMT", 0.0);
            resultSet.updateDouble("RCM_PER", 0);
            resultSet.updateDouble("RCM_AMT", 0);
            resultSet.updateDouble("COMPOSITION_PER", 0);
            resultSet.updateDouble("COMPOSITION_AMT", 0);
            resultSet.updateDouble("GST_COMPENSATION_CESS_PER", 0);
            resultSet.updateDouble("GST_COMPENSATION_CESS_AMT", 0);

            resultSet.updateString("PR_BILL_LENGTH", getAttribute("PR_BILL_LENGTH").getString());
            resultSet.updateString("PR_BILL_WIDTH", getAttribute("PR_BILL_WIDTH").getString());
            resultSet.updateString("PR_BILL_WEIGHT", getAttribute("PR_BILL_WEIGHT").getString());
            resultSet.updateString("PR_BILL_SQMTR", getAttribute("PR_BILL_SQMTR").getString());
            resultSet.updateString("PR_BILL_GSM", getAttribute("PR_BILL_GSM").getString());
            resultSet.updateString("PR_BILL_PRODUCT_CODE", getAttribute("PR_BILL_PRODUCT_CODE").getString());

            resultSet.updateString("BASE_ORDER_AMT", getAttribute("BASE_ORDER_AMT").getString());
            resultSet.updateString("BASE_EXISTING_PIECE_AMT", getAttribute("BASE_EXISTING_PIECE_AMT").getString());

            resultSet.updateString("DISCOUNT_PER", getAttribute("DISCOUNT_PER").getString());
            resultSet.updateString("DISCOUNT_AMT", getAttribute("DISCOUNT_AMT").getString());

            resultSet.updateString("COST_BY_COMPANY_PER", getAttribute("COST_BY_COMPANY_PER").getString());
            resultSet.updateString("COST_BY_COMPANY_AMT", getAttribute("COST_BY_COMPANY_AMT").getString());
            resultSet.updateString("COST_BY_PARTY_PER", getAttribute("COST_BY_PARTY_PER").getString());
            resultSet.updateString("COST_BY_PARTY_AMT", getAttribute("COST_BY_PARTY_AMT").getString());
            
            resultSet.updateString("TH_WEIGHT", getAttribute("TH_WEIGHT").getString());
            resultSet.updateString("MACHINE_POSITION", getAttribute("MACHINE_POSITION").getString());
            
            
            resultSet.insertRow();

            resultSetH.first();
            resultSetH.moveToInsertRow();

            resultSetH.updateInt("REVISION_NO", 1);
            resultSetH.updateString("SD_ORDER_NO", getAttribute("SD_ORDER_NO").getString());
            resultSetH.updateString("SD_ORDER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("SD_ORDER_DATE").getString()));
            resultSetH.updateString("D_REGION", getAttribute("D_REGION").getString());
            resultSetH.updateString("D_SALES_ENGINEER", getAttribute("D_SALES_ENGINEER").getString());
            resultSetH.updateString("D_PARTY_CODE", getAttribute("D_PARTY_CODE").getString());
            resultSetH.updateString("D_PARTY_NAME", getAttribute("D_PARTY_NAME").getString());
            resultSetH.updateString("D_REFERENCE", getAttribute("D_REFERENCE").getString());
            resultSetH.updateString("D_REFERENCE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("D_REFERENCE_DATE").getString()));
            resultSetH.updateString("D_REASON_FOR_DIVERSION", getAttribute("D_REASON_FOR_DIVERSION").getString());
            resultSetH.updateString("D_DELIVERY_STATUS_FOR_EXISTING_CLIENT", getAttribute("D_DELIVERY_STATUS_FOR_EXISTING_CLIENT").getString());
            resultSetH.updateString("D_REMARK", getAttribute("D_REMARK").getString());
            resultSetH.updateString("D_PIECE_NO", getAttribute("D_PIECE_NO").getString());
            resultSetH.updateString("D_PRODUCT_NO", getAttribute("D_PRODUCT_NO").getString());
            resultSetH.updateString("D_PRODUCT_DESC", getAttribute("D_PRODUCT_DESC").getString());
            resultSetH.updateString("D_MACHINE_NO", getAttribute("D_MACHINE_NO").getString());
            resultSetH.updateString("D_POSITION_NO", getAttribute("D_POSITION_NO").getString());
            resultSetH.updateString("D_POSITION_DESC", getAttribute("D_POSITION_DESC").getString());
            resultSetH.updateString("D_STYLE_NO", getAttribute("D_STYLE_NO").getString());
            resultSetH.updateString("D_GROUP", getAttribute("D_GROUP").getString());
            resultSetH.updateString("D_LENGTH", getAttribute("D_LENGTH").getString());
            resultSetH.updateString("D_WIDTH", getAttribute("D_WIDTH").getString());
            resultSetH.updateString("D_GSM", getAttribute("D_GSM").getString());
            resultSetH.updateString("D_THORITICAL_WEIGHT", getAttribute("D_THORITICAL_WEIGHT").getString());
            resultSetH.updateString("D_SQ_MTR", getAttribute("D_SQ_MTR").getString());
            resultSetH.updateString("D_REQ_MONTH", getAttribute("D_REQ_MONTH").getString());
            resultSetH.updateString("D_SYN_PER", getAttribute("D_SYN_PER").getString());
            resultSetH.updateFloat("D_OV_RATE", Float.parseFloat(getAttribute("D_OV_RATE").getString()));
            resultSetH.updateFloat("D_OV_BAS_AMOUNT", Float.parseFloat(getAttribute("D_OV_BAS_AMOUNT").getString()));
            resultSetH.updateFloat("D_OV_CHEM_TRT_CHG", Float.parseFloat(getAttribute("D_OV_CHEM_TRT_CHG").getString()));
            resultSetH.updateFloat("D_OV_SPIRAL_CHG", Float.parseFloat(getAttribute("D_OV_SPIRAL_CHG").getString()));
            resultSetH.updateFloat("D_OV_PIN_CHG", Float.parseFloat(getAttribute("D_OV_PIN_CHG").getString()));
            resultSetH.updateFloat("D_OV_SEAM_CHG", Float.parseFloat(getAttribute("D_OV_SEAM_CHG").getString()));
            resultSetH.updateFloat("D_OV_AMT", Float.parseFloat(getAttribute("D_OV_AMT").getString()));
            resultSetH.updateString("ORIGINAL_PARTY_CODE", getAttribute("ORIGINAL_PARTY_CODE").getString());
            resultSetH.updateString("ORIGINAL_PARTY_NAME", getAttribute("ORIGINAL_PARTY_NAME").getString());
            resultSetH.updateString("ORIGINAL_PIECE_NO", getAttribute("ORIGINAL_PIECE_NO").getString());
            resultSetH.updateString("ORIGINAL_PRODUCT_NO", getAttribute("ORIGINAL_PRODUCT_NO").getString());
            resultSetH.updateString("ORIGINAL_MACHINE_NO", getAttribute("ORIGINAL_MACHINE_NO").getString());
            resultSetH.updateString("ORIGINAL_POSITION_NO", getAttribute("ORIGINAL_POSITION_NO").getString());
            resultSetH.updateString("ORIGINAL_STYLE_NO", getAttribute("ORIGINAL_STYLE_NO").getString());
            resultSetH.updateString("ORIGINAL_GROUP", getAttribute("ORIGINAL_GROUP").getString());
            resultSetH.updateString("ORIGINAL_LENGTH", getAttribute("ORIGINAL_LENGTH").getString());
            resultSetH.updateString("ORIGINAL_WIDTH", getAttribute("ORIGINAL_WIDTH").getString());
            resultSetH.updateString("ORIGINAL_GSM", getAttribute("ORIGINAL_GSM").getString());
            resultSetH.updateString("ORIGINAL_THORITICAL_WEIGHT", getAttribute("ORIGINAL_THORITICAL_WEIGHT").getString());
            resultSetH.updateString("ORIGINAL_SQ_MTR", getAttribute("ORIGINAL_SQ_MTR").getString());
            resultSetH.updateFloat("ORIGINAL_OV_RATE", Float.parseFloat(getAttribute("ORIGINAL_OV_RATE").getVal() + ""));
            resultSetH.updateFloat("ORIGINAL_OV_AMOUNT", Float.parseFloat(getAttribute("ORIGINAL_OV_AMOUNT").getVal() + ""));
            resultSetH.updateString("DEBIT_NOTE_NO", getAttribute("DEBIT_NOTE_NO").getString());
            resultSetH.updateString("DEBIT_AMT", getAttribute("DEBIT_AMT").getString());
            resultSetH.updateString("COST_BEARER", getAttribute("COST_BEARER").getString());
            resultSetH.updateString("BEARER_PARTY_CODE", getAttribute("BEARER_PARTY_CODE").getString());
            resultSetH.updateString("BEARER_PARTY_NAME", getAttribute("BEARER_PARTY_NAME").getString());
            resultSetH.updateString("DIFFERENCE_AMT", getAttribute("DIFFERENCE_AMT").getString());
            resultSetH.updateString("L_G_AMT", getAttribute("L_G_AMT").getString());

            resultSetH.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            resultSetH.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());

            resultSetH.updateInt("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
            resultSetH.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSetH.updateBoolean("CHANGED", false);
            resultSetH.updateString("CHANGED_DATE", EITLERPGLOBAL.formatDateDB("0000-00-00"));
            resultSetH.updateBoolean("REJECTED", false);
            resultSetH.updateString("REJECTED_BY", "");
            resultSetH.updateString("REJECTED_DATE", "0000-00-00");
            resultSetH.updateBoolean("APPROVED", false);
            resultSetH.updateString("APPROVED_BY", "");
            resultSetH.updateString("APPROVED_DATE", "0000-00-00");
            resultSetH.updateBoolean("CANCELED", false);
            resultSetH.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            resultSetH.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            resultSetH.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            resultSetH.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateTimeDB());

            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");

            resultSetH.updateString("FROM_IP", "" + str_split[1]);

            resultSetH.updateDouble("CGST_PER", 0.0);
            resultSetH.updateDouble("CGST_AMT", 0.0);
            resultSetH.updateDouble("SGST_PER", 0.0);
            resultSetH.updateDouble("SGST_AMT", 0.0);
            resultSetH.updateDouble("IGST_PER", 0.0);
            resultSetH.updateDouble("IGST_AMT", 0.0);
            resultSetH.updateDouble("RCM_PER", 0);
            resultSetH.updateDouble("RCM_AMT", 0);
            resultSetH.updateDouble("COMPOSITION_PER", 0);
            resultSetH.updateDouble("COMPOSITION_AMT", 0);
            resultSetH.updateDouble("GST_COMPENSATION_CESS_PER", 0);
            resultSetH.updateDouble("GST_COMPENSATION_CESS_AMT", 0);

            resultSetH.updateString("PR_BILL_LENGTH", getAttribute("PR_BILL_LENGTH").getString());
            resultSetH.updateString("PR_BILL_WIDTH", getAttribute("PR_BILL_WIDTH").getString());
            resultSetH.updateString("PR_BILL_WEIGHT", getAttribute("PR_BILL_WEIGHT").getString());
            resultSetH.updateString("PR_BILL_SQMTR", getAttribute("PR_BILL_SQMTR").getString());
            resultSetH.updateString("PR_BILL_GSM", getAttribute("PR_BILL_GSM").getString());
            resultSetH.updateString("PR_BILL_PRODUCT_CODE", getAttribute("PR_BILL_PRODUCT_CODE").getString());

            resultSetH.updateString("BASE_ORDER_AMT", getAttribute("BASE_ORDER_AMT").getString());
            resultSetH.updateString("BASE_EXISTING_PIECE_AMT", getAttribute("BASE_EXISTING_PIECE_AMT").getString());

            resultSetH.updateString("DISCOUNT_PER", getAttribute("DISCOUNT_PER").getString());
            resultSetH.updateString("DISCOUNT_AMT", getAttribute("DISCOUNT_AMT").getString());

            resultSetH.updateString("COST_BY_COMPANY_PER", getAttribute("COST_BY_COMPANY_PER").getString());
            resultSetH.updateString("COST_BY_COMPANY_AMT", getAttribute("COST_BY_COMPANY_AMT").getString());
            resultSetH.updateString("COST_BY_PARTY_PER", getAttribute("COST_BY_PARTY_PER").getString());
            resultSetH.updateString("COST_BY_PARTY_AMT", getAttribute("COST_BY_PARTY_AMT").getString());
            
            resultSetH.updateString("TH_WEIGHT", getAttribute("TH_WEIGHT").getString());
            resultSetH.updateString("MACHINE_POSITION", getAttribute("MACHINE_POSITION").getString());
            
            resultSetH.insertRow();

            System.out.println("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_DIVERSION_FLAG = '' WHERE PR_PIECE_NO ='" + getAttribute("ORIGINAL_PIECE_NO").getString() + "' ");
            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_DIVERSION_FLAG = '' WHERE PR_PIECE_NO ='" + getAttribute("ORIGINAL_PIECE_NO").getString() + "' ");

            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = ModuleID; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("SD_ORDER_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL";
            ObjFeltProductionApprovalFlow.IsCreator = true;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("REJECTED_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "SD_ORDER_NO";

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

            return true;

        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    //Updates current record
    public boolean Update() {

        ResultSet resultSetH, rsRegister;
        Statement statement, statementH, stRegister;
        try {
            // Felt Order Updation data connection
            connection = data.getConn();

            //   statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //    resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL WHERE SD_ORDER_NO=''");
            statementH = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetH = statementH.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL_H WHERE SD_ORDER_NO=''");

            //  resultSet.first();
            //  resultSet.moveToCurrentRow();
            resultSet.updateString("SD_ORDER_NO", getAttribute("SD_ORDER_NO").getString());
            resultSet.updateString("SD_ORDER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("SD_ORDER_DATE").getString()));
            resultSet.updateString("D_REGION", getAttribute("D_REGION").getString());
            resultSet.updateString("D_SALES_ENGINEER", getAttribute("D_SALES_ENGINEER").getString());
            resultSet.updateString("D_PARTY_CODE", getAttribute("D_PARTY_CODE").getString());
            resultSet.updateString("D_PARTY_NAME", getAttribute("D_PARTY_NAME").getString());
            resultSet.updateString("D_REFERENCE", getAttribute("D_REFERENCE").getString());
            resultSet.updateString("D_REFERENCE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("D_REFERENCE_DATE").getString()));
            resultSet.updateString("D_REASON_FOR_DIVERSION", getAttribute("D_REASON_FOR_DIVERSION").getString());
            resultSet.updateString("D_DELIVERY_STATUS_FOR_EXISTING_CLIENT", getAttribute("D_DELIVERY_STATUS_FOR_EXISTING_CLIENT").getString());
            resultSet.updateString("D_REMARK", getAttribute("D_REMARK").getString());
            resultSet.updateString("D_PIECE_NO", getAttribute("D_PIECE_NO").getString());
            resultSet.updateString("D_PRODUCT_NO", getAttribute("D_PRODUCT_NO").getString());
            resultSet.updateString("D_PRODUCT_DESC", getAttribute("D_PRODUCT_DESC").getString());
            resultSet.updateString("D_MACHINE_NO", getAttribute("D_MACHINE_NO").getString());
            resultSet.updateString("D_POSITION_NO", getAttribute("D_POSITION_NO").getString());
            resultSet.updateString("D_POSITION_DESC", getAttribute("D_POSITION_DESC").getString());
            resultSet.updateString("D_STYLE_NO", getAttribute("D_STYLE_NO").getString());
            resultSet.updateString("D_GROUP", getAttribute("D_GROUP").getString());
            resultSet.updateString("D_LENGTH", getAttribute("D_LENGTH").getString());
            resultSet.updateString("D_WIDTH", getAttribute("D_WIDTH").getString());
            resultSet.updateString("D_GSM", getAttribute("D_GSM").getString());
            resultSet.updateString("D_THORITICAL_WEIGHT", getAttribute("D_THORITICAL_WEIGHT").getString());
            resultSet.updateString("D_SQ_MTR", getAttribute("D_SQ_MTR").getString());
            resultSet.updateString("D_REQ_MONTH", getAttribute("D_REQ_MONTH").getString());
            resultSet.updateString("D_SYN_PER", getAttribute("D_SYN_PER").getString());
            resultSet.updateFloat("D_OV_RATE", Float.parseFloat(getAttribute("D_OV_RATE").getString()));
            resultSet.updateFloat("D_OV_BAS_AMOUNT", Float.parseFloat(getAttribute("D_OV_BAS_AMOUNT").getString()));
            resultSet.updateFloat("D_OV_CHEM_TRT_CHG", Float.parseFloat(getAttribute("D_OV_CHEM_TRT_CHG").getString()));
            resultSet.updateFloat("D_OV_SPIRAL_CHG", Float.parseFloat(getAttribute("D_OV_SPIRAL_CHG").getString()));
            resultSet.updateFloat("D_OV_PIN_CHG", Float.parseFloat(getAttribute("D_OV_PIN_CHG").getString()));
            resultSet.updateFloat("D_OV_SEAM_CHG", Float.parseFloat(getAttribute("D_OV_SEAM_CHG").getString()));
            resultSet.updateFloat("D_OV_AMT", Float.parseFloat(getAttribute("D_OV_AMT").getString()));

            resultSet.updateString("DEBIT_NOTE_NO", getAttribute("DEBIT_NOTE_NO").getString());
            resultSet.updateString("DEBIT_AMT", getAttribute("DEBIT_AMT").getString());
            resultSet.updateString("COST_BEARER", getAttribute("COST_BEARER").getString());
            resultSet.updateString("BEARER_PARTY_CODE", getAttribute("BEARER_PARTY_CODE").getString());
            resultSet.updateString("BEARER_PARTY_NAME", getAttribute("BEARER_PARTY_NAME").getString());
            resultSet.updateString("DIFFERENCE_AMT", getAttribute("DIFFERENCE_AMT").getString());
            resultSet.updateString("L_G_AMT", getAttribute("L_G_AMT").getString());

            resultSet.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            resultSet.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateInt("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
            resultSet.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateBoolean("CHANGED", true);
            resultSet.updateString("CHANGED_DATE", EITLERPGLOBAL.formatDateDB("0000-00-00"));
            resultSet.updateBoolean("REJECTED", false);
            resultSet.updateString("REJECTED_BY", "");
            resultSet.updateString("REJECTED_DATE", "0000-00-00");

            resultSet.updateDouble("CGST_PER", 0.0);
            resultSet.updateDouble("CGST_AMT", 0.0);
            resultSet.updateDouble("SGST_PER", 0.0);
            resultSet.updateDouble("SGST_AMT", 0.0);
            resultSet.updateDouble("IGST_PER", 0.0);
            resultSet.updateDouble("IGST_AMT", 0.0);

            resultSet.updateDouble("RCM_PER", 0);
            resultSet.updateDouble("RCM_AMT", 0);
            resultSet.updateDouble("COMPOSITION_PER", 0);
            resultSet.updateDouble("COMPOSITION_AMT", 0);
            resultSet.updateDouble("GST_COMPENSATION_CESS_PER", 0);
            resultSet.updateDouble("GST_COMPENSATION_CESS_AMT", 0);

            resultSet.updateString("PR_BILL_LENGTH", getAttribute("PR_BILL_LENGTH").getString());
            resultSet.updateString("PR_BILL_WIDTH", getAttribute("PR_BILL_WIDTH").getString());
            resultSet.updateString("PR_BILL_WEIGHT", getAttribute("PR_BILL_WEIGHT").getString());
            resultSet.updateString("PR_BILL_SQMTR", getAttribute("PR_BILL_SQMTR").getString());
            resultSet.updateString("PR_BILL_GSM", getAttribute("PR_BILL_GSM").getString());
            resultSet.updateString("PR_BILL_PRODUCT_CODE", getAttribute("PR_BILL_PRODUCT_CODE").getString());

            resultSet.updateString("BASE_ORDER_AMT", getAttribute("BASE_ORDER_AMT").getString());
            resultSet.updateString("BASE_EXISTING_PIECE_AMT", getAttribute("BASE_EXISTING_PIECE_AMT").getString());

            resultSet.updateString("DISCOUNT_PER", getAttribute("DISCOUNT_PER").getString());
            resultSet.updateString("DISCOUNT_AMT", getAttribute("DISCOUNT_AMT").getString());

            if (getAttribute("APPROVAL_STATUS").getString().equals("F")) {
                resultSet.updateBoolean("APPROVED", true);
                resultSet.updateString("APPROVED_BY", EITLERPGLOBAL.gNewUserID + "");
                resultSet.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            } else {
                resultSet.updateBoolean("APPROVED", false);
                resultSet.updateString("APPROVED_DATE", "0000-00-00");
            }

            resultSet.updateBoolean("CANCELED", false);
            resultSet.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());

            resultSet.updateString("COST_BY_COMPANY_PER", getAttribute("COST_BY_COMPANY_PER").getString());
            resultSet.updateString("COST_BY_COMPANY_AMT", getAttribute("COST_BY_COMPANY_AMT").getString());
            resultSet.updateString("COST_BY_PARTY_PER", getAttribute("COST_BY_PARTY_PER").getString());
            resultSet.updateString("COST_BY_PARTY_AMT", getAttribute("COST_BY_PARTY_AMT").getString());
            
            resultSet.updateString("TH_WEIGHT", getAttribute("TH_WEIGHT").getString());
            resultSet.updateString("MACHINE_POSITION", getAttribute("MACHINE_POSITION").getString());
            
            
            try {
                resultSet.updateRow();
            } catch (Exception e) {
                // JOptionPane.showMessageDialog(null, "Came To Error");
                e.printStackTrace();
            }

            resultSetH.first();
            resultSetH.moveToInsertRow();

            int RevNo = data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL_H WHERE SD_ORDER_NO='" + getAttribute("SD_ORDER_NO").getString() + "'");

            RevNo++;
            //JOptionPane.showMessageDialog(null, "DOC No = "+getAttribute("SD_ORDER_NO").getInt() + " ,New Rev No = "+RevNo);
            resultSetH.updateInt("REVISION_NO", RevNo);

            resultSetH.updateString("SD_ORDER_NO", getAttribute("SD_ORDER_NO").getString());
            resultSetH.updateString("SD_ORDER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("SD_ORDER_DATE").getString()));
            resultSetH.updateString("D_REGION", getAttribute("D_REGION").getString());
            resultSetH.updateString("D_SALES_ENGINEER", getAttribute("D_SALES_ENGINEER").getString());
            resultSetH.updateString("D_PARTY_CODE", getAttribute("D_PARTY_CODE").getString());
            resultSetH.updateString("D_PARTY_NAME", getAttribute("D_PARTY_NAME").getString());
            resultSetH.updateString("D_REFERENCE", getAttribute("D_REFERENCE").getString());
            resultSetH.updateString("D_REFERENCE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("D_REFERENCE_DATE").getString()));
            resultSetH.updateString("D_REASON_FOR_DIVERSION", getAttribute("D_REASON_FOR_DIVERSION").getString());
            resultSetH.updateString("D_DELIVERY_STATUS_FOR_EXISTING_CLIENT", getAttribute("D_DELIVERY_STATUS_FOR_EXISTING_CLIENT").getString());
            resultSetH.updateString("D_REMARK", getAttribute("D_REMARK").getString());
            resultSetH.updateString("D_PIECE_NO", getAttribute("D_PIECE_NO").getString());
            resultSetH.updateString("D_PRODUCT_NO", getAttribute("D_PRODUCT_NO").getString());
            resultSetH.updateString("D_PRODUCT_DESC", getAttribute("D_PRODUCT_DESC").getString());
            resultSetH.updateString("D_MACHINE_NO", getAttribute("D_MACHINE_NO").getString());
            resultSetH.updateString("D_POSITION_NO", getAttribute("D_POSITION_NO").getString());
            resultSetH.updateString("D_POSITION_DESC", getAttribute("D_POSITION_DESC").getString());
            resultSetH.updateString("D_STYLE_NO", getAttribute("D_STYLE_NO").getString());
            resultSetH.updateString("D_GROUP", getAttribute("D_GROUP").getString());
            resultSetH.updateString("D_LENGTH", getAttribute("D_LENGTH").getString());
            resultSetH.updateString("D_WIDTH", getAttribute("D_WIDTH").getString());
            resultSetH.updateString("D_GSM", getAttribute("D_GSM").getString());
            resultSetH.updateString("D_THORITICAL_WEIGHT", getAttribute("D_THORITICAL_WEIGHT").getString());
            resultSetH.updateString("D_SQ_MTR", getAttribute("D_SQ_MTR").getString());
            resultSetH.updateString("D_REQ_MONTH", getAttribute("D_REQ_MONTH").getString());
            resultSetH.updateString("D_SYN_PER", getAttribute("D_SYN_PER").getString());
            resultSetH.updateFloat("D_OV_RATE", Float.parseFloat(getAttribute("D_OV_RATE").getString()));
            resultSetH.updateFloat("D_OV_BAS_AMOUNT", Float.parseFloat(getAttribute("D_OV_BAS_AMOUNT").getString()));
            resultSetH.updateFloat("D_OV_CHEM_TRT_CHG", Float.parseFloat(getAttribute("D_OV_CHEM_TRT_CHG").getString()));
            resultSetH.updateFloat("D_OV_SPIRAL_CHG", Float.parseFloat(getAttribute("D_OV_SPIRAL_CHG").getString()));
            resultSetH.updateFloat("D_OV_PIN_CHG", Float.parseFloat(getAttribute("D_OV_PIN_CHG").getString()));
            resultSetH.updateFloat("D_OV_SEAM_CHG", Float.parseFloat(getAttribute("D_OV_SEAM_CHG").getString()));
            resultSetH.updateFloat("D_OV_AMT", Float.parseFloat(getAttribute("D_OV_AMT").getString()));
            resultSetH.updateString("ORIGINAL_PARTY_CODE", getAttribute("ORIGINAL_PARTY_CODE").getString());
            resultSetH.updateString("ORIGINAL_PARTY_NAME", getAttribute("ORIGINAL_PARTY_NAME").getString());
            resultSetH.updateString("ORIGINAL_PIECE_NO", getAttribute("ORIGINAL_PIECE_NO").getString());
            resultSetH.updateString("ORIGINAL_PRODUCT_NO", getAttribute("ORIGINAL_PRODUCT_NO").getString());
            resultSetH.updateString("ORIGINAL_MACHINE_NO", getAttribute("ORIGINAL_MACHINE_NO").getString());
            resultSetH.updateString("ORIGINAL_POSITION_NO", getAttribute("ORIGINAL_POSITION_NO").getString());
            resultSetH.updateString("ORIGINAL_STYLE_NO", getAttribute("ORIGINAL_STYLE_NO").getString());
            resultSetH.updateString("ORIGINAL_GROUP", getAttribute("ORIGINAL_GROUP").getString());
            resultSetH.updateString("ORIGINAL_LENGTH", getAttribute("ORIGINAL_LENGTH").getString());
            resultSetH.updateString("ORIGINAL_WIDTH", getAttribute("ORIGINAL_WIDTH").getString());
            resultSetH.updateString("ORIGINAL_GSM", getAttribute("ORIGINAL_GSM").getString());
            resultSetH.updateString("ORIGINAL_THORITICAL_WEIGHT", getAttribute("ORIGINAL_THORITICAL_WEIGHT").getString());
            resultSetH.updateString("ORIGINAL_SQ_MTR", getAttribute("ORIGINAL_SQ_MTR").getString());
            resultSetH.updateFloat("ORIGINAL_OV_RATE", Float.parseFloat(getAttribute("ORIGINAL_OV_RATE").getVal() + ""));
            resultSetH.updateFloat("ORIGINAL_OV_AMOUNT", Float.parseFloat(getAttribute("ORIGINAL_OV_AMOUNT").getVal() + ""));
            resultSetH.updateString("DEBIT_NOTE_NO", getAttribute("DEBIT_NOTE_NO").getString());
            resultSetH.updateString("DEBIT_AMT", getAttribute("DEBIT_AMT").getString());
            resultSetH.updateString("COST_BEARER", getAttribute("COST_BEARER").getString());
            resultSetH.updateString("BEARER_PARTY_CODE", getAttribute("BEARER_PARTY_CODE").getString());
            resultSetH.updateString("BEARER_PARTY_NAME", getAttribute("BEARER_PARTY_NAME").getString());
            resultSetH.updateString("DIFFERENCE_AMT", getAttribute("DIFFERENCE_AMT").getString());
            resultSetH.updateString("L_G_AMT", getAttribute("L_G_AMT").getString());

            resultSetH.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            resultSetH.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSetH.updateInt("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
            resultSetH.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSetH.updateBoolean("CHANGED", true);
            resultSetH.updateString("CHANGED_DATE", EITLERPGLOBAL.formatDateDB("0000-00-00"));
            resultSetH.updateBoolean("REJECTED", false);
            resultSetH.updateString("REJECTED_BY", "");
            resultSetH.updateString("REJECTED_DATE", "0000-00-00");

            resultSetH.updateString("CGST_PER", "0.0");
            resultSetH.updateString("CGST_AMT", "0.0");
            resultSetH.updateString("SGST_PER", "0.0");
            resultSetH.updateString("SGST_AMT", "0.0");
            resultSetH.updateString("IGST_PER", "0.0");
            resultSetH.updateString("IGST_AMT", "0.0");

            resultSetH.updateDouble("RCM_PER", 0);
            resultSetH.updateDouble("RCM_AMT", 0);
            resultSetH.updateDouble("COMPOSITION_PER", 0);
            resultSetH.updateDouble("COMPOSITION_AMT", 0);
            resultSetH.updateDouble("GST_COMPENSATION_CESS_PER", 0);
            resultSetH.updateDouble("GST_COMPENSATION_CESS_AMT", 0);

            resultSetH.updateString("PR_BILL_LENGTH", getAttribute("PR_BILL_LENGTH").getString());
            resultSetH.updateString("PR_BILL_WIDTH", getAttribute("PR_BILL_WIDTH").getString());
            resultSetH.updateString("PR_BILL_WEIGHT", getAttribute("PR_BILL_WEIGHT").getString());
            resultSetH.updateString("PR_BILL_SQMTR", getAttribute("PR_BILL_SQMTR").getString());
            resultSetH.updateString("PR_BILL_GSM", getAttribute("PR_BILL_GSM").getString());
            resultSetH.updateString("PR_BILL_PRODUCT_CODE", getAttribute("PR_BILL_PRODUCT_CODE").getString());

            resultSetH.updateString("BASE_ORDER_AMT", getAttribute("BASE_ORDER_AMT").getString());
            resultSetH.updateString("BASE_EXISTING_PIECE_AMT", getAttribute("BASE_EXISTING_PIECE_AMT").getString());

            resultSetH.updateString("DISCOUNT_PER", getAttribute("DISCOUNT_PER").getString());
            resultSetH.updateString("DISCOUNT_AMT", getAttribute("DISCOUNT_AMT").getString());

            if (getAttribute("APPROVAL_STATUS").getString().equals("F")) {
                resultSetH.updateBoolean("APPROVED", true);
                resultSetH.updateString("APPROVED_BY", EITLERPGLOBAL.gNewUserID + "");
                resultSetH.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            } else {
                resultSetH.updateBoolean("APPROVED", false);
                resultSetH.updateString("APPROVED_DATE", "0000-00-00");
            }

            resultSetH.updateBoolean("CANCELED", false);
            resultSetH.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());

            resultSetH.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            resultSetH.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            resultSetH.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateTimeDB());

            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");

            resultSetH.updateString("FROM_IP", "" + str_split[1]);
            
            resultSetH.updateString("COST_BY_COMPANY_PER", getAttribute("COST_BY_COMPANY_PER").getString());
            resultSetH.updateString("COST_BY_COMPANY_AMT", getAttribute("COST_BY_COMPANY_AMT").getString());
            resultSetH.updateString("COST_BY_PARTY_PER", getAttribute("COST_BY_PARTY_PER").getString());
            resultSetH.updateString("COST_BY_PARTY_AMT", getAttribute("COST_BY_PARTY_AMT").getString());
            
            resultSetH.updateString("TH_WEIGHT", getAttribute("TH_WEIGHT").getString());
            resultSetH.updateString("MACHINE_POSITION", getAttribute("MACHINE_POSITION").getString());
            
            resultSetH.insertRow();

            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = ModuleID; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("SD_ORDER_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL";
            ObjFeltProductionApprovalFlow.IsCreator = true;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "SD_ORDER_NO";

            if (getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFeltProductionApprovalFlow.To = (int) getAttribute("SEND_DOC_TO").getVal();
                ObjFeltProductionApprovalFlow.ExplicitSendTo = true;
            }

            //==== Handling Rejected Documents ==========//
            boolean IsRejected = getAttribute("REJECTED").getBool();

            if (IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL SET REJECTED=0,CHANGED=1 WHERE SD_ORDER_NO ='" + getAttribute("SD_ORDER_NO").getString() + "'");
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL WHERE  SD_ORDER_NO='" + documentNo + "' AND APPROVED=" + 1;
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
                    strSQL = "DELETE FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL WHERE "
                            + " AND SD_ORDER_DATE = '" + EITLERPGLOBAL.formatDateDB(stringProductionDate) + "'"
                            + " AND SD_ORDER_NO ='" + documentNo + "'";
                    tmpStmt.executeUpdate(strSQL);
                    LoadData();
                    return true;
                } else {
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL WHERE SD_ORDER_NO='" + orderupdDocumentNo + "' AND APPROVED=1";
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
            String strSql = "SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL WHERE  " + stringFindQuery + "";
            System.out.println("Find Diversion Prior Approval:" + strSql);
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
        int serialNoCounter = 0;
        int RevNo = 0;

        try {
            if (resultSet.getRow() == 0) {
                return false;
            }

            setAttribute("REVISION_NO", 0);

            if (HistoryView) {
                RevNo = resultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO", resultSet.getInt("REVISION_NO"));
            }

            setAttribute("SD_ORDER_NO", resultSet.getString("SD_ORDER_NO"));
            setAttribute("SD_ORDER_DATE", EITLERPGLOBAL.formatDate(resultSet.getDate("SD_ORDER_DATE")));
            setAttribute("D_REGION", resultSet.getString("D_REGION"));
            setAttribute("D_SALES_ENGINEER", resultSet.getString("D_SALES_ENGINEER"));
            setAttribute("D_PARTY_CODE", resultSet.getString("D_PARTY_CODE"));
            setAttribute("D_PARTY_NAME", resultSet.getString("D_PARTY_NAME"));
            setAttribute("D_REFERENCE", resultSet.getString("D_REFERENCE"));
            setAttribute("D_REFERENCE_DATE", resultSet.getString("D_REFERENCE_DATE"));
            setAttribute("D_REASON_FOR_DIVERSION", resultSet.getString("D_REASON_FOR_DIVERSION"));
            setAttribute("D_DELIVERY_STATUS_FOR_EXISTING_CLIENT", resultSet.getString("D_DELIVERY_STATUS_FOR_EXISTING_CLIENT"));
            setAttribute("D_REMARK", resultSet.getString("D_REMARK"));
            setAttribute("D_PIECE_NO", resultSet.getString("D_PIECE_NO"));
            setAttribute("D_PRODUCT_NO", resultSet.getString("D_PRODUCT_NO"));
            setAttribute("D_PRODUCT_DESC", resultSet.getString("D_PRODUCT_DESC"));
            setAttribute("D_MACHINE_NO", resultSet.getString("D_MACHINE_NO"));
            setAttribute("D_POSITION_NO", resultSet.getString("D_POSITION_NO"));
            setAttribute("D_POSITION_DESC", resultSet.getString("D_POSITION_DESC"));
            setAttribute("D_STYLE_NO", resultSet.getString("D_STYLE_NO"));
            setAttribute("D_GROUP", resultSet.getString("D_GROUP"));
            setAttribute("D_LENGTH", resultSet.getString("D_LENGTH"));
            setAttribute("D_WIDTH", resultSet.getString("D_WIDTH"));
            setAttribute("D_GSM", resultSet.getString("D_GSM"));
            setAttribute("D_THORITICAL_WEIGHT", resultSet.getString("D_THORITICAL_WEIGHT"));
            setAttribute("D_SQ_MTR", resultSet.getString("D_SQ_MTR"));
            setAttribute("D_REQ_MONTH", resultSet.getString("D_REQ_MONTH"));
            setAttribute("D_SYN_PER", resultSet.getString("D_SYN_PER"));
            setAttribute("D_OV_RATE", resultSet.getString("D_OV_RATE"));
            setAttribute("D_OV_BAS_AMOUNT", resultSet.getString("D_OV_BAS_AMOUNT"));
            setAttribute("D_OV_CHEM_TRT_CHG", resultSet.getString("D_OV_CHEM_TRT_CHG"));
            setAttribute("D_OV_SPIRAL_CHG", resultSet.getString("D_OV_SPIRAL_CHG"));
            setAttribute("D_OV_PIN_CHG", resultSet.getString("D_OV_PIN_CHG"));
            setAttribute("D_OV_SEAM_CHG", resultSet.getString("D_OV_SEAM_CHG"));
            setAttribute("D_OV_AMT", resultSet.getString("D_OV_AMT"));
            setAttribute("ORIGINAL_PARTY_CODE", resultSet.getString("ORIGINAL_PARTY_CODE"));
            setAttribute("ORIGINAL_PARTY_NAME", resultSet.getString("ORIGINAL_PARTY_NAME"));
            setAttribute("ORIGINAL_PIECE_NO", resultSet.getString("ORIGINAL_PIECE_NO"));
            setAttribute("ORIGINAL_PRODUCT_NO", resultSet.getString("ORIGINAL_PRODUCT_NO"));
            setAttribute("ORIGINAL_MACHINE_NO", resultSet.getString("ORIGINAL_MACHINE_NO"));
            setAttribute("ORIGINAL_POSITION_NO", resultSet.getString("ORIGINAL_POSITION_NO"));
            setAttribute("ORIGINAL_STYLE_NO", resultSet.getString("ORIGINAL_STYLE_NO"));
            setAttribute("ORIGINAL_GROUP", resultSet.getString("ORIGINAL_GROUP"));
            setAttribute("ORIGINAL_LENGTH", resultSet.getString("ORIGINAL_LENGTH"));
            setAttribute("ORIGINAL_WIDTH", resultSet.getString("ORIGINAL_WIDTH"));
            setAttribute("ORIGINAL_GSM", resultSet.getString("ORIGINAL_GSM"));
            setAttribute("ORIGINAL_THORITICAL_WEIGHT", resultSet.getString("ORIGINAL_THORITICAL_WEIGHT"));
            setAttribute("ORIGINAL_SQ_MTR", resultSet.getString("ORIGINAL_SQ_MTR"));
            setAttribute("ORIGINAL_OV_RATE", resultSet.getString("ORIGINAL_OV_RATE"));
            setAttribute("ORIGINAL_OV_AMOUNT", resultSet.getString("ORIGINAL_OV_AMOUNT"));

            setAttribute("DEBIT_NOTE_NO", resultSet.getString("DEBIT_NOTE_NO"));
            setAttribute("DEBIT_AMT", resultSet.getString("DEBIT_AMT"));
            setAttribute("COST_BEARER", resultSet.getString("COST_BEARER"));
            setAttribute("BEARER_PARTY_CODE", resultSet.getString("BEARER_PARTY_CODE"));
            setAttribute("BEARER_PARTY_NAME", resultSet.getString("BEARER_PARTY_NAME"));
            setAttribute("DIFFERENCE_AMT", resultSet.getString("DIFFERENCE_AMT"));

            setAttribute("BASE_ORDER_AMT", resultSet.getString("BASE_ORDER_AMT"));
            setAttribute("BASE_EXISTING_PIECE_AMT", resultSet.getString("BASE_EXISTING_PIECE_AMT"));

            setAttribute("DISCOUNT_PER", resultSet.getString("DISCOUNT_PER"));
            setAttribute("DISCOUNT_AMT", resultSet.getString("DISCOUNT_AMT"));

            setAttribute("CREATED_BY", resultSet.getString("CREATED_BY"));
            setAttribute("CREATED_DATE", resultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY", resultSet.getString("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE", resultSet.getString("MODIFIED_DATE"));
            setAttribute("CHANGED", resultSet.getString("CHANGED"));
            setAttribute("CHANGED_DATE", resultSet.getString("CHANGED_DATE"));
            setAttribute("REJECTED", resultSet.getString("REJECTED"));
            setAttribute("REJECTED_BY", resultSet.getString("REJECTED_BY"));
            setAttribute("REJECTED_DATE", resultSet.getString("REJECTED_DATE"));
            setAttribute("APPROVED", resultSet.getString("APPROVED"));
            setAttribute("APPROVED_BY", resultSet.getString("APPROVED_BY"));
            setAttribute("APPROVED_DATE", resultSet.getString("APPROVED_DATE"));
            setAttribute("CANCELED", resultSet.getString("CANCELED"));
            setAttribute("HIERARCHY_ID", resultSet.getString("HIERARCHY_ID"));

            setAttribute("CGST_PER", resultSet.getString("CGST_PER"));
            setAttribute("CGST_AMT", resultSet.getString("CGST_AMT"));

            setAttribute("SGST_PER", resultSet.getString("SGST_PER"));
            setAttribute("SGST_AMT", resultSet.getString("SGST_AMT"));

            setAttribute("IGST_PER", resultSet.getString("IGST_PER"));
            setAttribute("IGST_AMT", resultSet.getString("IGST_AMT"));

            setAttribute("RCM_PER", resultSet.getString("RCM_PER"));
            setAttribute("RCM_AMT", resultSet.getString("RCM_AMT"));

            setAttribute("COMPOSITION_PER", resultSet.getString("COMPOSITION_PER"));
            setAttribute("COMPOSITION_AMT", resultSet.getString("COMPOSITION_AMT"));

            setAttribute("GST_COMPENSATION_CESS_PER", resultSet.getString("GST_COMPENSATION_CESS_PER"));
            setAttribute("GST_COMPENSATION_CESS_AMT", resultSet.getString("GST_COMPENSATION_CESS_AMT"));

            setAttribute("PR_BILL_LENGTH", resultSet.getString("PR_BILL_LENGTH"));
            setAttribute("PR_BILL_WIDTH", resultSet.getString("PR_BILL_WIDTH"));
            setAttribute("PR_BILL_WEIGHT", resultSet.getString("PR_BILL_WEIGHT"));
            setAttribute("PR_BILL_SQMTR", resultSet.getString("PR_BILL_SQMTR"));
            setAttribute("PR_BILL_GSM", resultSet.getString("PR_BILL_GSM"));
            setAttribute("PR_BILL_PRODUCT_CODE", resultSet.getString("PR_BILL_PRODUCT_CODE"));

            setAttribute("COST_BY_COMPANY_PER", resultSet.getString("COST_BY_COMPANY_PER"));
            setAttribute("COST_BY_COMPANY_AMT", resultSet.getString("COST_BY_COMPANY_AMT"));
            setAttribute("COST_BY_PARTY_PER", resultSet.getString("COST_BY_PARTY_PER"));
            setAttribute("COST_BY_PARTY_AMT", resultSet.getString("COST_BY_PARTY_AMT"));
            
            setAttribute("TH_WEIGHT", resultSet.getString("TH_WEIGHT"));
            setAttribute("MACHINE_POSITION", resultSet.getString("MACHINE_POSITION"));
            
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

        try {
            stTmp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);

            rsTmp = stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL_H WHERE SD_ORDER_NO='" + productionDocumentNo + "'");
            System.out.println("*** data in approval flow : SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL_H WHERE SD_ORDER_NO='" + productionDocumentNo + "'");
            while (rsTmp.next()) {
                clsFeltOrderDiversionLoss felt_orderD = new clsFeltOrderDiversionLoss();

                felt_orderD.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                felt_orderD.setAttribute("UPDATED_BY", rsTmp.getInt("MODIFIED_BY"));
                felt_orderD.setAttribute("UPDATED_DATE", rsTmp.getString("MODIFIED_DATE"));
                felt_orderD.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                felt_orderD.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                felt_orderD.setAttribute("REJECTED_REMARKS", rsTmp.getString("REJECTED_REMARKS"));
                felt_orderD.setAttribute("FROM_IP", rsTmp.getString("FROM_IP"));

                hmHistoryList.put(Integer.toString(hmHistoryList.size() + 1), felt_orderD);
            }
            rsTmp.close();
            stTmp.close();
            return hmHistoryList;
        } catch (Exception e) {
            e.printStackTrace();
            return hmHistoryList;
        }
    }

    public boolean ShowHistory(String pProductionDate, String pDocNo) {
        Ready = false;
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery("SELECT DISTINCT REVISION_NO FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL_H WHERE SD_ORDER_NO ='" + pDocNo + "'");
            Ready = true;
            historyAmendDate = pProductionDate;
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
                strSQL = "SELECT DISTINCT SD_ORDER_NO,SD_ORDER_DATE,RECEIVED_DATE,D_PARTY_CODE FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL, PRODUCTION.FELT_PROD_DOC_DATA   WHERE SD_ORDER_NO=DOC_NO AND STATUS='W' AND MODULE_ID=" + ModuleID + " AND CANCELED=0  AND USER_ID=" + pUserID + " ORDER BY RECEIVED_DATE,SD_ORDER_NO";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT SD_ORDER_NO,SD_ORDER_DATE,RECEIVED_DATE,D_PARTY_CODE FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL, PRODUCTION.FELT_PROD_DOC_DATA   WHERE SD_ORDER_NO=DOC_NO AND STATUS='W' AND MODULE_ID=" + ModuleID + " AND CANCELED=0  AND USER_ID=" + pUserID + " ORDER BY RECEIVED_DATE,SD_ORDER_NO";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT SD_ORDER_NO,SD_ORDER_DATE,RECEIVED_DATE,D_PARTY_CODE FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL, PRODUCTION.FELT_PROD_DOC_DATA   WHERE SD_ORDER_NO=DOC_NO AND STATUS='W' AND MODULE_ID=" + ModuleID + " AND CANCELED=0  AND USER_ID=" + pUserID + "  ORDER BY SD_ORDER_NO";
            }

            ResultSet rsTmp = data.getConn().createStatement().executeQuery(strSQL);

            Counter = 0;
            while (rsTmp.next()) {

                Counter = Counter + 1;
                clsFeltOrderDiversionLoss ObjDoc = new clsFeltOrderDiversionLoss();

                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO", rsTmp.getString("SD_ORDER_NO"));
                ObjDoc.setAttribute("DOC_DATE", rsTmp.getString("SD_ORDER_DATE"));
                ObjDoc.setAttribute("RECEIVED_DATE", rsTmp.getString("RECEIVED_DATE"));
                ObjDoc.setAttribute("PARTY_CODE", rsTmp.getString("D_PARTY_CODE"));

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

    public boolean ShowHistory(String pDocNo) {
        Ready = false;
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL_H WHERE SD_ORDER_NO ='" + pDocNo + "'");
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

    public static void CancelDoc(String pAmendNo) {
        ResultSet rsTmp = null;

        if (CanCancel(pAmendNo)) {

            boolean Approved = false;

            try {
                rsTmp = data.getResult("SELECT APPROVED FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL WHERE SD_ORDER_NO='" + pAmendNo + "' ");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    Approved = rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();

                if (!Approved) {
                      data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='"+pAmendNo+"' AND MODULE_ID=779");
                }
                data.Execute("UPDATE PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE SD_ORDER_NO='" + pAmendNo + "'");
                //String Original_Piece_No = data.getStringValueFromDB("SELECT ORIGINAL_PIECE_NO FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL WHERE SD_ORDER_NO='" + pAmendNo + "'");
                //data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_DIVERSION_FLAG='READY' WHERE PR_PIECE_NO='" + Original_Piece_No + "'");

            } catch (Exception e) {

            }
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
            System.out.println("SELECT SD_ORDER_NO FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL WHERE SD_ORDER_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");
            rsTmp = stTmp.executeQuery("SELECT SD_ORDER_NO FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL WHERE SD_ORDER_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");

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
}
