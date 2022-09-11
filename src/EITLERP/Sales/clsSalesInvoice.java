/*
 * clsMaterialRequisition.java
 *
 * Created on April 19, 2004, 2:35 PM
 */
package EITLERP.Sales;

import java.util.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import EITLERP.Finance.*;

/**
 *
 * @author jadave
 * @version
 */
public class clsSalesInvoice {

    public String LastError = "";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;

    private HashMap props;
    public boolean Ready = false;
    //Requisition Material Collection
    public HashMap colInvoiceItems;

    //History Related properties
    private boolean HistoryView = false;

    public static int ModuleID = 80;

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
     * Creates new clsMaterialRequisition
     */
    public clsSalesInvoice() {
        LastError = "";
        props = new HashMap();

        props.put("COMPANY_ID", new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("INVOICE_TYPE", new Variant(0));
        props.put("INVOICE_NO", new Variant(""));
        props.put("INVOICE_DATE", new Variant(""));
        props.put("PARTY_CODE", new Variant(""));
        props.put("QUALITY_NO", new Variant(""));
        props.put("PATTERN_CODE", new Variant(""));
        props.put("PIECE_NO", new Variant(""));
        props.put("AGENT_SR_NO", new Variant(0));
        props.put("STATION_CODE", new Variant(""));
        props.put("PAYMENT_TERM", new Variant(""));
        props.put("BANK_CHARGES", new Variant(0));
        props.put("TRANSPORT_MODE", new Variant(0));
        props.put("PAYMENT_TERM_CODE", new Variant(0));
        props.put("AGENT_LAST_INVOICE", new Variant(""));
        props.put("AGENT_LAST_SR_NO", new Variant(0));
        props.put("FIN_YEAR_FROM", new Variant(0));
        props.put("FIN_YEAR_TO", new Variant(0));
        props.put("WAREHOUSE_CODE", new Variant(0));
        props.put("BALE_NO", new Variant(""));
        props.put("LR_NO", new Variant(""));
        props.put("PACKING_DATE", new Variant(""));
        props.put("EXPORT_CATEGORY", new Variant(""));
        props.put("EXPORT_SUB_CATEGORY", new Variant(""));
        props.put("GATEPASS_NO", new Variant(""));
        props.put("GATEPASS_DATE", new Variant(""));
        props.put("DRAFT_NO", new Variant(""));
        props.put("DRAFT_DATE", new Variant(""));
        props.put("TOTAL_SQ_MTR", new Variant(0));
        props.put("TOTAL_KG", new Variant(0));
        props.put("TOTAL_GROSS_QTY", new Variant(0));
        props.put("TOTAL_NET_QTY", new Variant(0));
        props.put("TOTAL_GROSS_AMOUNT", new Variant(0));
        props.put("TOTAL_NET_AMOUNT", new Variant(0));
        props.put("EXCISABLE_VALUE", new Variant(0));
        props.put("TOTAL_VALUE", new Variant(0));
        props.put("NET_AMOUNT", new Variant(0));
        props.put("IC_TYPE", new Variant(""));
        props.put("QUALITY_INDICATOR", new Variant(""));
        props.put("FILLER", new Variant(""));
        props.put("BANK_CLASS", new Variant(0));
        props.put("BANK_CODE", new Variant(0));
        props.put("HUNDI_NO", new Variant(""));
        props.put("GROSS_WEIGHT", new Variant(0));
        props.put("TRANSPORTER_CODE", new Variant(0));
        props.put("LC_NO", new Variant(""));
        props.put("BNG_INDICATOR", new Variant(""));
        props.put("INS_INDICATOR", new Variant(""));
        props.put("PARTY_NAME", new Variant(""));
        props.put("LENGTH", new Variant(0));
        props.put("WIDTH", new Variant(0));
        props.put("ORDER_NO", new Variant(""));
        props.put("ORDER_DATE", new Variant(""));
        props.put("NO_OF_PIECES", new Variant(0));

        props.put("COLUMN_1_ID", new Variant(0));
        props.put("COLUMN_1_FORMULA", new Variant(""));
        props.put("COLUMN_1_PER", new Variant(0));
        props.put("COLUMN_1_AMT", new Variant(0));
        props.put("COLUMN_1_CAPTION", new Variant(""));

        props.put("COLUMN_2_ID", new Variant(0));
        props.put("COLUMN_2_FORMULA", new Variant(""));
        props.put("COLUMN_2_PER", new Variant(0));
        props.put("COLUMN_2_AMT", new Variant(0));
        props.put("COLUMN_2_CAPTION", new Variant(""));

        props.put("COLUMN_3_ID", new Variant(0));
        props.put("COLUMN_3_FORMULA", new Variant(""));
        props.put("COLUMN_3_PER", new Variant(0));
        props.put("COLUMN_3_AMT", new Variant(0));
        props.put("COLUMN_3_CAPTION", new Variant(""));

        props.put("COLUMN_4_ID", new Variant(0));
        props.put("COLUMN_4_FORMULA", new Variant(""));
        props.put("COLUMN_4_PER", new Variant(0));
        props.put("COLUMN_4_AMT", new Variant(0));
        props.put("COLUMN_4_CAPTION", new Variant(""));

        props.put("COLUMN_5_ID", new Variant(0));
        props.put("COLUMN_5_FORMULA", new Variant(""));
        props.put("COLUMN_5_PER", new Variant(0));
        props.put("COLUMN_5_AMT", new Variant(0));
        props.put("COLUMN_5_CAPTION", new Variant(""));

        props.put("COLUMN_6_ID", new Variant(0));
        props.put("COLUMN_6_FORMULA", new Variant(""));
        props.put("COLUMN_6_PER", new Variant(0));
        props.put("COLUMN_6_AMT", new Variant(0));
        props.put("COLUMN_6_CAPTION", new Variant(""));

        props.put("COLUMN_7_ID", new Variant(0));
        props.put("COLUMN_7_FORMULA", new Variant(""));
        props.put("COLUMN_7_PER", new Variant(0));
        props.put("COLUMN_7_AMT", new Variant(0));
        props.put("COLUMN_7_CAPTION", new Variant(""));

        props.put("COLUMN_8_ID", new Variant(0));
        props.put("COLUMN_8_FORMULA", new Variant(""));
        props.put("COLUMN_8_PER", new Variant(0));
        props.put("COLUMN_8_AMT", new Variant(0));
        props.put("COLUMN_8_CAPTION", new Variant(""));

        props.put("COLUMN_9_ID", new Variant(0));
        props.put("COLUMN_9_FORMULA", new Variant(""));
        props.put("COLUMN_9_PER", new Variant(0));
        props.put("COLUMN_9_AMT", new Variant(0));
        props.put("COLUMN_9_CAPTION", new Variant(""));

        props.put("COLUMN_10_ID", new Variant(0));
        props.put("COLUMN_10_FORMULA", new Variant(""));
        props.put("COLUMN_10_PER", new Variant(0));
        props.put("COLUMN_10_AMT", new Variant(0));
        props.put("COLUMN_10_CAPTION", new Variant(""));

        props.put("COLUMN_11_ID", new Variant(0));
        props.put("COLUMN_11_FORMULA", new Variant(""));
        props.put("COLUMN_11_PER", new Variant(0));
        props.put("COLUMN_11_AMT", new Variant(0));
        props.put("COLUMN_11_CAPTION", new Variant(""));

        props.put("COLUMN_12_ID", new Variant(0));
        props.put("COLUMN_12_FORMULA", new Variant(""));
        props.put("COLUMN_12_PER", new Variant(0));
        props.put("COLUMN_12_AMT", new Variant(0));
        props.put("COLUMN_12_CAPTION", new Variant(""));

        props.put("COLUMN_13_ID", new Variant(0));
        props.put("COLUMN_13_FORMULA", new Variant(""));
        props.put("COLUMN_13_PER", new Variant(0));
        props.put("COLUMN_13_AMT", new Variant(0));
        props.put("COLUMN_13_CAPTION", new Variant(""));

        props.put("COLUMN_14_ID", new Variant(0));
        props.put("COLUMN_14_FORMULA", new Variant(""));
        props.put("COLUMN_14_PER", new Variant(0));
        props.put("COLUMN_14_AMT", new Variant(0));
        props.put("COLUMN_14_CAPTION", new Variant(""));

        props.put("COLUMN_15_ID", new Variant(0));
        props.put("COLUMN_15_FORMULA", new Variant(""));
        props.put("COLUMN_15_PER", new Variant(0));
        props.put("COLUMN_15_AMT", new Variant(0));
        props.put("COLUMN_15_CAPTION", new Variant(""));

        props.put("COLUMN_16_ID", new Variant(0));
        props.put("COLUMN_16_FORMULA", new Variant(""));
        props.put("COLUMN_16_PER", new Variant(0));
        props.put("COLUMN_16_AMT", new Variant(0));
        props.put("COLUMN_16_CAPTION", new Variant(""));

        props.put("COLUMN_17_ID", new Variant(0));
        props.put("COLUMN_17_FORMULA", new Variant(""));
        props.put("COLUMN_17_PER", new Variant(0));
        props.put("COLUMN_17_AMT", new Variant(0));
        props.put("COLUMN_17_CAPTION", new Variant(""));

        props.put("COLUMN_18_ID", new Variant(0));
        props.put("COLUMN_18_FORMULA", new Variant(""));
        props.put("COLUMN_18_PER", new Variant(0));
        props.put("COLUMN_18_AMT", new Variant(0));
        props.put("COLUMN_18_CAPTION", new Variant(""));

        props.put("COLUMN_19_ID", new Variant(0));
        props.put("COLUMN_19_FORMULA", new Variant(""));
        props.put("COLUMN_19_PER", new Variant(0));
        props.put("COLUMN_19_AMT", new Variant(0));
        props.put("COLUMN_19_CAPTION", new Variant(""));

        props.put("COLUMN_20_ID", new Variant(0));
        props.put("COLUMN_20_FORMULA", new Variant(""));
        props.put("COLUMN_20_PER", new Variant(0));
        props.put("COLUMN_20_AMT", new Variant(0));
        props.put("COLUMN_20_CAPTION", new Variant(""));

        props.put("COLUMN_21_ID", new Variant(0));
        props.put("COLUMN_21_FORMULA", new Variant(""));
        props.put("COLUMN_21_PER", new Variant(0));
        props.put("COLUMN_21_AMT", new Variant(0));
        props.put("COLUMN_21_CAPTION", new Variant(""));

        props.put("COLUMN_22_ID", new Variant(0));
        props.put("COLUMN_22_FORMULA", new Variant(""));
        props.put("COLUMN_22_PER", new Variant(0));
        props.put("COLUMN_22_AMT", new Variant(0));
        props.put("COLUMN_22_CAPTION", new Variant(""));

        props.put("COLUMN_23_ID", new Variant(0));
        props.put("COLUMN_23_FORMULA", new Variant(""));
        props.put("COLUMN_23_PER", new Variant(0));
        props.put("COLUMN_23_AMT", new Variant(0));
        props.put("COLUMN_23_CAPTION", new Variant(""));

        props.put("COLUMN_24_ID", new Variant(0));
        props.put("COLUMN_24_FORMULA", new Variant(""));
        props.put("COLUMN_24_PER", new Variant(0));
        props.put("COLUMN_24_AMT", new Variant(0));
        props.put("COLUMN_24_CAPTION", new Variant(""));

        props.put("COLUMN_25_ID", new Variant(0));
        props.put("COLUMN_25_FORMULA", new Variant(""));
        props.put("COLUMN_25_PER", new Variant(0));
        props.put("COLUMN_25_AMT", new Variant(0));
        props.put("COLUMN_25_CAPTION", new Variant(""));

        props.put("APPROVED", new Variant(false));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED", new Variant(false));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("REMARKS", new Variant(""));
        props.put("CANCELLED", new Variant(false));
        props.put("STATUS", new Variant(""));
        props.put("HIERARCHY_ID", new Variant(0));
        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        //  props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("PREFIX", new Variant("")); //For Autonumber generation
        props.put("VAT1", new Variant(0));
        props.put("VAT4", new Variant(0));
        props.put("CST2", new Variant(0));
        props.put("CST5", new Variant(0));
        props.put("SD_AMT", new Variant(0));
        props.put("IGST_AMT", new Variant(0));
        props.put("SGST_AMT", new Variant(0));
        props.put("CGST_AMT", new Variant(0));
        props.put("IGST_PER", new Variant(0));
        props.put("SGST_PER", new Variant(0));
        props.put("CGST_PER", new Variant(0));
        props.put("CASH_DISC_AMT", new Variant(0));

        //Create a new object for MR Item collection
        colInvoiceItems = new HashMap();

        // -- Approval Specific Fields --
        props.put("FROM", new Variant(0));
        props.put("TO", new Variant(0));
        props.put("REMARKS", new Variant(""));
        props.put("APPROVAL_STATUS", new Variant(""));
    }

    public boolean LoadData(long pCompanyID) {
        Ready = false;
        try {
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery("SELECT * FROM D_SAL_INVOICE_HEADER WHERE COMPANY_ID=" + pCompanyID + " AND INVOICE_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND INVOICE_DATE<='" + EITLERPGLOBAL.FinToDateDB + "' ORDER BY INVOICE_NO");
            HistoryView = false;
            Ready = true;
            MoveLast();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public void Close() {
        try {
            //Conn.close();
            Stmt.close();
            rsResultSet.close();
        } catch (Exception e) {

        }
    }

    //Navigation Methods
    public boolean MoveFirst() {
        try {
            rsResultSet.first();
            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }

    }

    public boolean MoveNext() {
        try {
            if (rsResultSet.isAfterLast() || rsResultSet.isLast()) {
                //Move pointer at last record
                //If it is beyond eof
                rsResultSet.last();
            } else {
                rsResultSet.next();
                if (rsResultSet.isAfterLast()) {
                    rsResultSet.last();
                }
            }
            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean MovePrevious() {
        try {
            if (rsResultSet.isFirst() || rsResultSet.isBeforeFirst()) {
                rsResultSet.first();
            } else {
                rsResultSet.previous();
                if (rsResultSet.isBeforeFirst()) {
                    rsResultSet.first();
                }
            }
            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean MoveLast() {
        try {
            rsResultSet.last();
            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public Object getObject(int pCompanyID, String pReqNo, String pReqDate) {
        String strCondition = " WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND INVOICE_NO='" + pReqNo + "' AND INVOICE_DATE='" + pReqDate + "'";
        clsSalesInvoice objInvoice = new clsSalesInvoice();
        objInvoice.Filter(strCondition, pCompanyID);
        return objInvoice;
    }

    public Object getObject(int pCompanyID, String pReqNo, String pReqDate, String dbURL) {
        String strCondition = " WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND INVOICE_NO='" + pReqNo + "' AND INVOICE_DATE='" + pReqDate + "'";
        clsSalesInvoice objInvoice = new clsSalesInvoice();
        objInvoice.Filter(strCondition, pCompanyID, dbURL);
        return objInvoice;
    }

    public boolean Filter(String pCondition, int pCompanyID) {
        Ready = false;
        try {
            String strSql = "SELECT * FROM D_SAL_INVOICE_HEADER " + pCondition;
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery(strSql);

            if (!rsResultSet.first()) {
                strSql = "SELECT * FROM D_SAL_INVOICE_HEADER WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND INVOICE_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND INVOICE_DATE<='" + EITLERPGLOBAL.FinToDateDB + "' ORDER BY INVOICE_NO";
                rsResultSet = Stmt.executeQuery(strSql);
                Ready = true;
                MoveLast();
                return false;
            } else {
                Ready = true;
                MoveLast();
                return true;
            }
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean Filter(String pCondition, int pCompanyID, String dbURL) {
        Ready = false;
        try {
            String strSql = "SELECT * FROM D_SAL_INVOICE_HEADER " + pCondition;
            Conn = data.getConn(dbURL);
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery(strSql);

            if (!rsResultSet.first()) {
                strSql = "SELECT * FROM D_SAL_INVOICE_HEADER WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND INVOICE_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND INVOICE_DATE<='" + EITLERPGLOBAL.FinToDateDB + "' ORDER BY INVOICE_NO";
                rsResultSet = Stmt.executeQuery(strSql);
                Ready = true;
                MoveLast();
                return false;
            } else {
                Ready = true;
                MoveLast();
                return true;
            }
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean setData() {

        long Counter = 0;
        int RevNo = 0;

        try {

            if (HistoryView) {
                RevNo = rsResultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO", rsResultSet.getInt("REVISION_NO"));
            } else {
                setAttribute("REVISION_NO", 0);
            }

            setAttribute("COMPANY_ID", rsResultSet.getInt("COMPANY_ID"));
            setAttribute("INVOICE_TYPE", rsResultSet.getInt("INVOICE_TYPE"));
            setAttribute("INVOICE_NO", rsResultSet.getString("INVOICE_NO"));
            setAttribute("INVOICE_DATE", rsResultSet.getString("INVOICE_DATE"));
            setAttribute("PARTY_CODE", rsResultSet.getString("PARTY_CODE"));
            setAttribute("QUALITY_NO", rsResultSet.getString("QUALITY_NO"));
            setAttribute("PATTERN_CODE", rsResultSet.getString("PATTERN_CODE"));
            setAttribute("PIECE_NO", rsResultSet.getString("PIECE_NO"));
            setAttribute("AGENT_SR_NO", rsResultSet.getInt("AGENT_SR_NO"));
            setAttribute("DUE_DATE", rsResultSet.getString("DUE_DATE"));
            setAttribute("STATION_CODE", rsResultSet.getString("STATION_CODE"));
            setAttribute("PAYMENT_TERM", rsResultSet.getString("PAYMENT_TERM"));
            setAttribute("BANK_CHARGES", rsResultSet.getDouble("BANK_CHARGES"));
            setAttribute("TRANSPORT_MODE", rsResultSet.getInt("TRANSPORT_MODE"));
            setAttribute("PAYMENT_TERM_CODE", rsResultSet.getInt("PAYMENT_TERM_CODE"));
            setAttribute("AGENT_LAST_INVOICE", rsResultSet.getString("AGENT_LAST_INVOICE"));
            setAttribute("AGENT_LAST_SR_NO", rsResultSet.getInt("AGENT_LAST_SR_NO"));
            setAttribute("FIN_YEAR_FROM", rsResultSet.getInt("FIN_YEAR_FROM"));
            setAttribute("FIN_YEAR_TO", rsResultSet.getInt("FIN_YEAR_TO"));
            setAttribute("WAREHOUSE_CODE", rsResultSet.getInt("WAREHOUSE_CODE"));
            setAttribute("BALE_NO", rsResultSet.getString("BALE_NO"));
            setAttribute("LR_NO", rsResultSet.getString("LR_NO"));
            setAttribute("PACKING_DATE", rsResultSet.getString("PACKING_DATE"));
            setAttribute("EXPORT_CATEGORY", rsResultSet.getString("EXPORT_CATEGORY"));
            setAttribute("EXPORT_SUB_CATEGORY", rsResultSet.getString("EXPORT_SUB_CATEGORY"));
            setAttribute("GATEPASS_NO", rsResultSet.getString("GATEPASS_NO"));
            setAttribute("GATEPASS_DATE", rsResultSet.getString("GATEPASS_DATE"));
            setAttribute("DRAFT_NO", rsResultSet.getString("DRAFT_NO"));
            setAttribute("DRAFT_DATE", rsResultSet.getString("DRAFT_DATE"));
            setAttribute("TOTAL_SQ_MTR", rsResultSet.getDouble("TOTAL_SQ_MTR"));
            setAttribute("TOTAL_KG", rsResultSet.getDouble("TOTAL_KG"));
            setAttribute("TOTAL_GROSS_QTY", rsResultSet.getDouble("TOTAL_GROSS_QTY"));
            setAttribute("TOTAL_NET_QTY", rsResultSet.getDouble("TOTAL_NET_QTY"));
            setAttribute("TOTAL_GROSS_AMOUNT", rsResultSet.getDouble("TOTAL_GROSS_AMOUNT"));
            setAttribute("TOTAL_NET_AMOUNT", rsResultSet.getDouble("TOTAL_NET_AMOUNT"));
            setAttribute("EXCISABLE_VALUE", rsResultSet.getDouble("EXCISABLE_VALUE"));
            setAttribute("TOTAL_VALUE", rsResultSet.getDouble("TOTAL_VALUE"));
            setAttribute("NET_AMOUNT", rsResultSet.getDouble("NET_AMOUNT"));
            setAttribute("IC_TYPE", rsResultSet.getString("IC_TYPE"));
            setAttribute("QUALITY_INDICATOR", rsResultSet.getString("QUALITY_INDICATOR"));
            setAttribute("FILLER", rsResultSet.getString("FILLER"));
            setAttribute("BANK_CLASS", rsResultSet.getInt("BANK_CLASS"));
            setAttribute("BANK_CODE", rsResultSet.getInt("BANK_CODE"));
            setAttribute("HUNDI_NO", rsResultSet.getString("HUNDI_NO"));
            setAttribute("GROSS_WEIGHT", rsResultSet.getDouble("GROSS_WEIGHT"));
            setAttribute("TRANSPORTER_CODE", rsResultSet.getInt("TRANSPORTER_CODE"));
            setAttribute("LC_NO", rsResultSet.getString("LC_NO"));
            setAttribute("BNG_INDICATOR", rsResultSet.getString("BNG_INDICATOR"));
            setAttribute("INS_INDICATOR", rsResultSet.getString("INS_INDICATOR"));
            setAttribute("PARTY_NAME", rsResultSet.getString("PARTY_NAME"));
            setAttribute("LENGTH", rsResultSet.getDouble("LENGTH"));
            setAttribute("WIDTH", rsResultSet.getDouble("WIDTH"));
            setAttribute("ORDER_NO", rsResultSet.getString("ORDER_NO"));
            setAttribute("ORDER_DATE", rsResultSet.getString("ORDER_DATE"));
            setAttribute("NO_OF_PIECES", rsResultSet.getDouble("NO_OF_PIECES"));

            setAttribute("COLUMN_1_ID", rsResultSet.getInt("COLUMN_1_ID"));
            setAttribute("COLUMN_1_FORMULA", rsResultSet.getString("COLUMN_1_FORMULA"));
            setAttribute("COLUMN_1_PER", rsResultSet.getDouble("COLUMN_1_PER"));
            setAttribute("COLUMN_1_AMT", rsResultSet.getDouble("COLUMN_1_AMT"));
            setAttribute("COLUMN_1_CAPTION", rsResultSet.getString("COLUMN_1_CAPTION"));

            setAttribute("COLUMN_2_ID", rsResultSet.getInt("COLUMN_2_ID"));
            setAttribute("COLUMN_2_FORMULA", rsResultSet.getString("COLUMN_2_FORMULA"));
            setAttribute("COLUMN_2_PER", rsResultSet.getDouble("COLUMN_2_PER"));
            setAttribute("COLUMN_2_AMT", rsResultSet.getDouble("COLUMN_2_AMT"));
            setAttribute("COLUMN_2_CAPTION", rsResultSet.getString("COLUMN_2_CAPTION"));

            setAttribute("COLUMN_3_ID", rsResultSet.getInt("COLUMN_3_ID"));
            setAttribute("COLUMN_3_FORMULA", rsResultSet.getString("COLUMN_3_FORMULA"));
            setAttribute("COLUMN_3_PER", rsResultSet.getDouble("COLUMN_3_PER"));
            setAttribute("COLUMN_3_AMT", rsResultSet.getDouble("COLUMN_3_AMT"));
            setAttribute("COLUMN_3_CAPTION", rsResultSet.getString("COLUMN_3_CAPTION"));

            setAttribute("COLUMN_4_ID", rsResultSet.getInt("COLUMN_4_ID"));
            setAttribute("COLUMN_4_FORMULA", rsResultSet.getString("COLUMN_4_FORMULA"));
            setAttribute("COLUMN_4_PER", rsResultSet.getDouble("COLUMN_4_PER"));
            setAttribute("COLUMN_4_AMT", rsResultSet.getDouble("COLUMN_4_AMT"));
            setAttribute("COLUMN_4_CAPTION", rsResultSet.getString("COLUMN_4_CAPTION"));

            setAttribute("COLUMN_5_ID", rsResultSet.getInt("COLUMN_5_ID"));
            setAttribute("COLUMN_5_FORMULA", rsResultSet.getString("COLUMN_5_FORMULA"));
            setAttribute("COLUMN_5_PER", rsResultSet.getDouble("COLUMN_5_PER"));
            setAttribute("COLUMN_5_AMT", rsResultSet.getDouble("COLUMN_5_AMT"));
            setAttribute("COLUMN_5_CAPTION", rsResultSet.getString("COLUMN_5_CAPTION"));

            setAttribute("COLUMN_6_ID", rsResultSet.getInt("COLUMN_6_ID"));
            setAttribute("COLUMN_6_FORMULA", rsResultSet.getString("COLUMN_6_FORMULA"));
            setAttribute("COLUMN_6_PER", rsResultSet.getDouble("COLUMN_6_PER"));
            setAttribute("COLUMN_6_AMT", rsResultSet.getDouble("COLUMN_6_AMT"));
            setAttribute("COLUMN_6_CAPTION", rsResultSet.getString("COLUMN_6_CAPTION"));

            setAttribute("COLUMN_7_ID", rsResultSet.getInt("COLUMN_7_ID"));
            setAttribute("COLUMN_7_FORMULA", rsResultSet.getString("COLUMN_7_FORMULA"));
            setAttribute("COLUMN_7_PER", rsResultSet.getDouble("COLUMN_7_PER"));
            setAttribute("COLUMN_7_AMT", rsResultSet.getDouble("COLUMN_7_AMT"));
            setAttribute("COLUMN_7_CAPTION", rsResultSet.getString("COLUMN_7_CAPTION"));

            setAttribute("COLUMN_8_ID", rsResultSet.getInt("COLUMN_8_ID"));
            setAttribute("COLUMN_8_FORMULA", rsResultSet.getString("COLUMN_8_FORMULA"));
            setAttribute("COLUMN_8_PER", rsResultSet.getDouble("COLUMN_8_PER"));
            setAttribute("COLUMN_8_AMT", rsResultSet.getDouble("COLUMN_8_AMT"));
            setAttribute("COLUMN_8_CAPTION", rsResultSet.getString("COLUMN_8_CAPTION"));

            setAttribute("COLUMN_9_ID", rsResultSet.getInt("COLUMN_9_ID"));
            setAttribute("COLUMN_9_FORMULA", rsResultSet.getString("COLUMN_9_FORMULA"));
            setAttribute("COLUMN_9_PER", rsResultSet.getDouble("COLUMN_9_PER"));
            setAttribute("COLUMN_9_AMT", rsResultSet.getDouble("COLUMN_9_AMT"));
            setAttribute("COLUMN_9_CAPTION", rsResultSet.getString("COLUMN_9_CAPTION"));

            setAttribute("COLUMN_10_ID", rsResultSet.getInt("COLUMN_10_ID"));
            setAttribute("COLUMN_10_FORMULA", rsResultSet.getString("COLUMN_10_FORMULA"));
            setAttribute("COLUMN_10_PER", rsResultSet.getDouble("COLUMN_10_PER"));
            setAttribute("COLUMN_10_AMT", rsResultSet.getDouble("COLUMN_10_AMT"));
            setAttribute("COLUMN_10_CAPTION", rsResultSet.getString("COLUMN_10_CAPTION"));

            setAttribute("COLUMN_11_ID", rsResultSet.getInt("COLUMN_11_ID"));
            setAttribute("COLUMN_11_FORMULA", rsResultSet.getString("COLUMN_11_FORMULA"));
            setAttribute("COLUMN_11_PER", rsResultSet.getDouble("COLUMN_11_PER"));
            setAttribute("COLUMN_11_AMT", rsResultSet.getDouble("COLUMN_11_AMT"));
            setAttribute("COLUMN_11_CAPTION", rsResultSet.getString("COLUMN_11_CAPTION"));

            setAttribute("COLUMN_12_ID", rsResultSet.getInt("COLUMN_12_ID"));
            setAttribute("COLUMN_12_FORMULA", rsResultSet.getString("COLUMN_12_FORMULA"));
            setAttribute("COLUMN_12_PER", rsResultSet.getDouble("COLUMN_12_PER"));
            setAttribute("COLUMN_12_AMT", rsResultSet.getDouble("COLUMN_12_AMT"));
            setAttribute("COLUMN_12_CAPTION", rsResultSet.getString("COLUMN_12_CAPTION"));

            setAttribute("COLUMN_13_ID", rsResultSet.getInt("COLUMN_13_ID"));
            setAttribute("COLUMN_13_FORMULA", rsResultSet.getString("COLUMN_13_FORMULA"));
            setAttribute("COLUMN_13_PER", rsResultSet.getDouble("COLUMN_13_PER"));
            setAttribute("COLUMN_13_AMT", rsResultSet.getDouble("COLUMN_13_AMT"));
            setAttribute("COLUMN_13_CAPTION", rsResultSet.getString("COLUMN_13_CAPTION"));

            setAttribute("COLUMN_14_ID", rsResultSet.getInt("COLUMN_14_ID"));
            setAttribute("COLUMN_14_FORMULA", rsResultSet.getString("COLUMN_14_FORMULA"));
            setAttribute("COLUMN_14_PER", rsResultSet.getDouble("COLUMN_14_PER"));
            setAttribute("COLUMN_14_AMT", rsResultSet.getDouble("COLUMN_14_AMT"));
            setAttribute("COLUMN_14_CAPTION", rsResultSet.getString("COLUMN_14_CAPTION"));

            setAttribute("COLUMN_15_ID", rsResultSet.getInt("COLUMN_15_ID"));
            setAttribute("COLUMN_15_FORMULA", rsResultSet.getString("COLUMN_15_FORMULA"));
            setAttribute("COLUMN_15_PER", rsResultSet.getDouble("COLUMN_15_PER"));
            setAttribute("COLUMN_15_AMT", rsResultSet.getDouble("COLUMN_15_AMT"));
            setAttribute("COLUMN_15_CAPTION", rsResultSet.getString("COLUMN_15_CAPTION"));

            setAttribute("COLUMN_16_ID", rsResultSet.getInt("COLUMN_16_ID"));
            setAttribute("COLUMN_16_FORMULA", rsResultSet.getString("COLUMN_16_FORMULA"));
            setAttribute("COLUMN_16_PER", rsResultSet.getDouble("COLUMN_16_PER"));
            setAttribute("COLUMN_16_AMT", rsResultSet.getDouble("COLUMN_16_AMT"));
            setAttribute("COLUMN_16_CAPTION", rsResultSet.getString("COLUMN_16_CAPTION"));

            setAttribute("COLUMN_17_ID", rsResultSet.getInt("COLUMN_17_ID"));
            setAttribute("COLUMN_17_FORMULA", rsResultSet.getString("COLUMN_17_FORMULA"));
            setAttribute("COLUMN_17_PER", rsResultSet.getDouble("COLUMN_17_PER"));
            setAttribute("COLUMN_17_AMT", rsResultSet.getDouble("COLUMN_17_AMT"));
            setAttribute("COLUMN_17_CAPTION", rsResultSet.getString("COLUMN_17_CAPTION"));

            setAttribute("COLUMN_18_ID", rsResultSet.getInt("COLUMN_18_ID"));
            setAttribute("COLUMN_18_FORMULA", rsResultSet.getString("COLUMN_18_FORMULA"));
            setAttribute("COLUMN_18_PER", rsResultSet.getDouble("COLUMN_18_PER"));
            setAttribute("COLUMN_18_AMT", rsResultSet.getDouble("COLUMN_18_AMT"));
            setAttribute("COLUMN_18_CAPTION", rsResultSet.getString("COLUMN_18_CAPTION"));

            setAttribute("COLUMN_19_ID", rsResultSet.getInt("COLUMN_19_ID"));
            setAttribute("COLUMN_19_FORMULA", rsResultSet.getString("COLUMN_19_FORMULA"));
            setAttribute("COLUMN_19_PER", rsResultSet.getDouble("COLUMN_19_PER"));
            setAttribute("COLUMN_19_AMT", rsResultSet.getDouble("COLUMN_19_AMT"));
            setAttribute("COLUMN_19_CAPTION", rsResultSet.getString("COLUMN_19_CAPTION"));

            setAttribute("COLUMN_20_ID", rsResultSet.getInt("COLUMN_20_ID"));
            setAttribute("COLUMN_20_FORMULA", rsResultSet.getString("COLUMN_20_FORMULA"));
            setAttribute("COLUMN_20_PER", rsResultSet.getDouble("COLUMN_20_PER"));
            setAttribute("COLUMN_20_AMT", rsResultSet.getDouble("COLUMN_20_AMT"));
            setAttribute("COLUMN_20_CAPTION", rsResultSet.getString("COLUMN_20_CAPTION"));

            setAttribute("COLUMN_21_ID", rsResultSet.getInt("COLUMN_21_ID"));
            setAttribute("COLUMN_21_FORMULA", rsResultSet.getString("COLUMN_21_FORMULA"));
            setAttribute("COLUMN_21_PER", rsResultSet.getDouble("COLUMN_21_PER"));
            setAttribute("COLUMN_21_AMT", rsResultSet.getDouble("COLUMN_21_AMT"));
            setAttribute("COLUMN_21_CAPTION", rsResultSet.getString("COLUMN_21_CAPTION"));

            setAttribute("COLUMN_22_ID", rsResultSet.getInt("COLUMN_22_ID"));
            setAttribute("COLUMN_22_FORMULA", rsResultSet.getString("COLUMN_22_FORMULA"));
            setAttribute("COLUMN_22_PER", rsResultSet.getDouble("COLUMN_22_PER"));
            setAttribute("COLUMN_22_AMT", rsResultSet.getDouble("COLUMN_22_AMT"));
            setAttribute("COLUMN_22_CAPTION", rsResultSet.getString("COLUMN_22_CAPTION"));

            setAttribute("COLUMN_23_ID", rsResultSet.getInt("COLUMN_23_ID"));
            setAttribute("COLUMN_23_FORMULA", rsResultSet.getString("COLUMN_23_FORMULA"));
            setAttribute("COLUMN_23_PER", rsResultSet.getDouble("COLUMN_23_PER"));
            setAttribute("COLUMN_23_AMT", rsResultSet.getDouble("COLUMN_23_AMT"));
            setAttribute("COLUMN_23_CAPTION", rsResultSet.getString("COLUMN_23_CAPTION"));

            setAttribute("COLUMN_24_ID", rsResultSet.getInt("COLUMN_24_ID"));
            setAttribute("COLUMN_24_FORMULA", rsResultSet.getString("COLUMN_24_FORMULA"));
            setAttribute("COLUMN_24_PER", rsResultSet.getDouble("COLUMN_24_PER"));
            setAttribute("COLUMN_24_AMT", rsResultSet.getDouble("COLUMN_24_AMT"));
            setAttribute("COLUMN_24_CAPTION", rsResultSet.getString("COLUMN_24_CAPTION"));

            setAttribute("COLUMN_25_ID", rsResultSet.getInt("COLUMN_25_ID"));
            setAttribute("COLUMN_25_FORMULA", rsResultSet.getString("COLUMN_25_FORMULA"));
            setAttribute("COLUMN_25_PER", rsResultSet.getDouble("COLUMN_25_PER"));
            setAttribute("COLUMN_25_AMT", rsResultSet.getDouble("COLUMN_25_AMT"));
            setAttribute("COLUMN_25_CAPTION", rsResultSet.getString("COLUMN_25_CAPTION"));

            setAttribute("APPROVED", rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE", rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED", rsResultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE", rsResultSet.getString("REJECTED_DATE"));
            setAttribute("HIERARCHY_ID", rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("CANCELLED", rsResultSet.getInt("CANCELLED"));
            //setAttribute("STATUS",rsResultSet.getString("STATUS"));
            setAttribute("CREATED_BY", rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE", rsResultSet.getString("CREATED_DATE"));
            //            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            // setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            setAttribute("VAT1", rsResultSet.getDouble("VAT1"));
            setAttribute("VAT4", rsResultSet.getDouble("VAT4"));
            setAttribute("CST2", rsResultSet.getDouble("CST2"));
            setAttribute("CST5", rsResultSet.getDouble("CST5"));
            setAttribute("SD_AMT", rsResultSet.getDouble("SD_AMT"));

            setAttribute("CGST_AMT", rsResultSet.getDouble("CGST_AMT"));
            setAttribute("SGST_AMT", rsResultSet.getDouble("SGST_AMT"));
            setAttribute("IGST_AMT", rsResultSet.getDouble("IGST_AMT"));

            setAttribute("CGST_PER", rsResultSet.getDouble("CGST_PER"));
            setAttribute("SGST_PER", rsResultSet.getDouble("SGST_PER"));
            setAttribute("IGST_PER", rsResultSet.getDouble("IGST_PER"));
            setAttribute("CASH_DISC_AMT", rsResultSet.getDouble("CASH_DISC_AMT"));

            colInvoiceItems.clear();
            String InvoiceNo = getAttribute("INVOICE_NO").getString();
            String InvoiceDate = getAttribute("INVOICE_DATE").getString();

            ResultSet rsTmp;
            rsTmp = data.getResult("SELECT * FROM D_SAL_INVOICE_DETAIL WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ORDER BY SR_NO");

            rsTmp.first();

            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {

                Counter = Counter + 1;
                clsSalesInvoiceDetail objItem = new clsSalesInvoiceDetail();

                objItem.setAttribute("COMPANY_ID", UtilFunctions.getInt(rsTmp, "COMPANY_ID", 0));
                objItem.setAttribute("INVOICE_TYPE", UtilFunctions.getInt(rsTmp, "INVOICE_TYPE", 0));
                objItem.setAttribute("INVOICE_NO", UtilFunctions.getString(rsTmp, "INVOICE_NO", ""));
                objItem.setAttribute("SR_NO", UtilFunctions.getInt(rsTmp, "SR_NO", 0));
                objItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp, "INVOICE_DATE", "0000-00-00")));
                objItem.setAttribute("QUALITY_NO", UtilFunctions.getString(rsTmp, "QUALITY_NO", ""));
                objItem.setAttribute("PATTERN_CODE", UtilFunctions.getString(rsTmp, "PATTERN_CODE", ""));
                objItem.setAttribute("PIECE_NO", UtilFunctions.getString(rsTmp, "PIECE_NO", ""));
                objItem.setAttribute("PARTY_CODE", UtilFunctions.getString(rsTmp, "PARTY_CODE", ""));
                objItem.setAttribute("WAREHOUSE_CODE", UtilFunctions.getString(rsTmp, "WAREHOUSE_CODE", ""));
                objItem.setAttribute("UNIT_CODE", UtilFunctions.getString(rsTmp, "UNIT_CODE", ""));
                objItem.setAttribute("FLG_DEF_CODE", UtilFunctions.getString(rsTmp, "FLG_DEF_CODE", ""));
                objItem.setAttribute("RATE", UtilFunctions.getInt(rsTmp, "RATE", 0));
                objItem.setAttribute("TRD_DISC_TYPE", UtilFunctions.getString(rsTmp, "TRD_DISC_TYPE", ""));
                objItem.setAttribute("SEASON_CODE", UtilFunctions.getString(rsTmp, "SEASON_CODE", ""));
                objItem.setAttribute("DEF_DISC_PER", UtilFunctions.getDouble(rsTmp, "DEF_DISC_PER", 0));
                objItem.setAttribute("ADDL_DISC_PER", UtilFunctions.getDouble(rsTmp, "ADDL_DISC_PER", 0));
                objItem.setAttribute("GROSS_SQ_MTR", UtilFunctions.getDouble(rsTmp, "GROSS_SQ_MTR", 0));
                objItem.setAttribute("GROSS_KG", UtilFunctions.getDouble(rsTmp, "GROSS_KG", 0));
                objItem.setAttribute("GROSS_QTY", UtilFunctions.getDouble(rsTmp, "GROSS_QTY", 0));
                objItem.setAttribute("NET_QTY", UtilFunctions.getDouble(rsTmp, "NET_QTY", 0));
                objItem.setAttribute("GROSS_AMOUNT", UtilFunctions.getDouble(rsTmp, "GROSS_AMOUNT", 0));
                objItem.setAttribute("TRD_DISCOUNT", UtilFunctions.getDouble(rsTmp, "TRD_DISCOUNT", 0));
                objItem.setAttribute("DEF_DISCOUNT", UtilFunctions.getDouble(rsTmp, "DEF_DISCOUNT", 0));
                objItem.setAttribute("ADDL_DISCOUNT", UtilFunctions.getDouble(rsTmp, "ADDL_DISCOUNT", 0));
                objItem.setAttribute("NET_AMOUNT", UtilFunctions.getDouble(rsTmp, "NET_AMOUNT", 0));
                objItem.setAttribute("EXCISABLE_VALUE", UtilFunctions.getDouble(rsTmp, "EXCISABLE_VALUE", 0));
                objItem.setAttribute("BALE_NO", UtilFunctions.getString(rsTmp, "BALE_NO", ""));
                objItem.setAttribute("LR_NO", UtilFunctions.getString(rsTmp, "LR_NO", ""));
                objItem.setAttribute("EXPORT_CATEGORY", UtilFunctions.getString(rsTmp, "EXPORT_CATEGORY", ""));
                objItem.setAttribute("FILLER", UtilFunctions.getString(rsTmp, "FILLER", ""));
                objItem.setAttribute("RECEIPT_DATE", UtilFunctions.getString(rsTmp, "RECEIPT_DATE", "0000-00-00"));
                objItem.setAttribute("BASIC_EXC", UtilFunctions.getDouble(rsTmp, "BASIC_EXC", 0));
                objItem.setAttribute("ADDITIONAL_DUTY", UtilFunctions.getDouble(rsTmp, "ADDITIONAL_DUTY", 0));
                objItem.setAttribute("AGENT_SR_NO", UtilFunctions.getInt(rsTmp, "AGENT_SR_NO", 0));
                objItem.setAttribute("AGENT_ALPHA", UtilFunctions.getString(rsTmp, "AGENT_ALPHA", ""));
                objItem.setAttribute("GATEPASS_NO", UtilFunctions.getString(rsTmp, "GATEPASS_NO", ""));
                objItem.setAttribute("REMARKS", UtilFunctions.getString(rsTmp, "REMARKS", ""));
                colInvoiceItems.put(Long.toString(Counter), objItem);
                rsTmp.next();
            }

            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public static String getDocStatus(int pCompanyID, String pDocNo, String pDocDate) {
        ResultSet rsTmp;
        String strMessage = "";

        try {
            //First check that Document exist
            rsTmp = data.getResult("SELECT INVOICE_NO,APPROVED,CANCELLED FROM D_SAL_INVOICE_HEADER WHERE COMPANY_ID=" + pCompanyID + " AND INVOICE_NO='" + pDocNo + "' AND INVOICE_DATE='" + pDocDate + "'");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                if (rsTmp.getBoolean("APPROVED")) {
                    if (rsTmp.getBoolean("CANCELED")) {
                        strMessage = "Document is cancelled";
                    } else {
                        strMessage = "";
                    }
                } else {
                    strMessage = "Document is created but is under approval";
                }

            } else {
                strMessage = "Document does not exist";
            }
            rsTmp.close();
        } catch (Exception e) {
        }

        return strMessage;

    }

    //    public static boolean CanCancel(int pCompanyID,String pDocNo) {
    //        ResultSet rsTmp=null;
    //        boolean canCancel=false;
    //
    //        try {
    //            rsTmp=data.getResult("SELECT INVOICE_NO FROM D_SAL_INVOICE_HEADER WHERE COMPANY_ID="+pCompanyID+" AND INVOICE_NO='"+pDocNo+"' AND CANCELLED=0");
    //            rsTmp.first();
    //
    //            if(rsTmp.getRow()>0) {
    //                canCancel=true;
    //            }
    //
    //            rsTmp.close();
    //        }
    //        catch(Exception e) {
    //
    //
    //        }
    //
    //        return canCancel;
    //
    //    }
    //
    //
    //    public static boolean CancelDoc(int pCompanyID,String pDocNo) {
    //
    //        ResultSet rsTmp=null,rsIndent=null;
    //        boolean Cancelled=false;
    //
    //        try {
    //            if(CanCancel(pCompanyID,pDocNo)) {
    //
    //                boolean ApprovedDoc=false;
    //
    //                rsTmp=data.getResult("SELECT APPROVED FROM D_SAL_INVOICE_HEADER WHERE COMPANY_ID="+pCompanyID+" AND INVOICE_NO='"+pDocNo+"'");
    //                rsTmp.first();
    //
    //                if(rsTmp.getRow()>0) {
    //                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
    //                }
    //
    //
    //                if(ApprovedDoc) {
    //
    //                }
    //                else {
    //
    //                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND MODULE_ID="+(clsSalesInvoice.ModuleID));
    //                }
    //
    //                //Now Update the header with cancel falg to true
    //                data.Execute("UPDATE D_SAL_INVOICE_HEADER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND INVOICE_NO='"+pDocNo+"'");
    //                data.Execute("UPDATE D_SAL_INVOICE_DETAIL SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND INVOICE_NO='"+pDocNo+"'");
    //
    //
    //                //Cancel the SJ associated with this invoice
    //                if(data.IsRecordExist("SELECT A.VOUCHER_NO FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.VOUCHER_TYPE="+FinanceGlobal.TYPE_SALES_JOURNAL+" AND INVOICE_NO='"+pDocNo+"' AND MODULE_ID="+clsSalesInvoice.ModuleID,FinanceGlobal.FinURL))
    //                {
    //                   String VoucherNo=data.getStringValueFromDB("SELECT A.VOUCHER_NO FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.VOUCHER_TYPE="+FinanceGlobal.TYPE_SALES_JOURNAL+" AND INVOICE_NO='"+pDocNo+"' AND MODULE_ID="+clsSalesInvoice.ModuleID,FinanceGlobal.FinURL);
    //                   int VoucherCompanyID=data.getIntValueFromDB("SELECT A.COMPANY_ID FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.VOUCHER_TYPE="+FinanceGlobal.TYPE_SALES_JOURNAL+" AND INVOICE_NO='"+pDocNo+"' AND MODULE_ID="+clsSalesInvoice.ModuleID,FinanceGlobal.FinURL);
    //
    //                   clsVoucher.CancelDoc(VoucherCompanyID, VoucherNo, clsVoucher.getVoucherModuleID(FinanceGlobal.TYPE_SALES_JOURNAL));
    //                }
    //
    //                //Rest advance payment adjustments from the receipt vouchers
    //                ResultSet rsReceipts=data.getResult("SELECT A.VOUCHER_NO FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND (A.VOUCHER_TYPE="+FinanceGlobal.TYPE_RECEIPT+" OR A.VOUCHER_TYPE="+FinanceGlobal.TYPE_JOURNAL+") AND B.INVOICE_NO='"+pDocNo+"' AND B.MODULE_ID="+clsSalesInvoice.ModuleID);
    //                rsReceipts.first();
    //
    //                if(rsReceipts.getRow()>0)
    //                {
    //                  while(!rsReceipts.isAfterLast())
    //                  {
    //
    //                     String VoucherNo=rsReceipts.getString("VOUCHER_NO");
    //
    //                     //Remove the reference of invoice nos.
    //                     data.Execute("UPDATE D_FIN_VOUCHER_DETAIL SET INVOICE_NO='',INVOICE_DATE='',GRN_NO='',GRN_DATE='',MODULE_ID=0,REF_COMPANY_ID=0 WHERE VOUCHER_NO='"+VoucherNo+"' AND INVOICE_NO='"+pDocNo+"' AND MODULE_ID="+clsSalesInvoice.ModuleID,FinanceGlobal.FinURL);
    //                     data.Execute("UPDATE D_FIN_VOUCHER_DETAIL_EX SET INVOICE_NO='',INVOICE_DATE='',GRN_NO='',GRN_DATE='',MODULE_ID=0,REF_COMPANY_ID=0 WHERE VOUCHER_NO='"+VoucherNo+"' AND INVOICE_NO='"+pDocNo+"' AND MODULE_ID="+clsSalesInvoice.ModuleID,FinanceGlobal.FinURL);
    //
    //                     rsReceipts.next();
    //                  }
    //                }
    //
    //                Cancelled=true;
    //            }
    //
    //            rsTmp.close();
    //            rsIndent.close();
    //        }
    //        catch(Exception e) {
    //
    //        }
    //
    //        return Cancelled;
    //    }
    public static boolean CanCancel(int pCompanyID, String pDocNo, String pDocDate) {
        ResultSet rsTmp = null;
        boolean canCancel = false;

        try {
            rsTmp = data.getResult("SELECT INVOICE_NO FROM D_SAL_INVOICE_HEADER WHERE COMPANY_ID=" + pCompanyID + " AND INVOICE_NO='" + pDocNo + "' AND INVOICE_DATE='" + pDocDate + "' AND CANCELLED=0");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                canCancel = true;
            }

            rsTmp.close();
        } catch (Exception e) {
        }
        return canCancel;
    }

    public static boolean CancelDoc(int pCompanyID, String pDocNo, String pDocDate) {

        ResultSet rsTmp = null, rsIndent = null;
        boolean Cancelled = false;

        try {
            if (CanCancel(pCompanyID, pDocNo, pDocDate)) {

                boolean ApprovedDoc = false;

                rsTmp = data.getResult("SELECT APPROVED FROM D_SAL_INVOICE_HEADER WHERE COMPANY_ID=" + pCompanyID + " AND INVOICE_NO='" + pDocNo + "' AND INVOICE_DATE='" + pDocDate + "'");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    ApprovedDoc = rsTmp.getBoolean("APPROVED");
                }

                if (ApprovedDoc) {
                } else {
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID=" + pCompanyID + " AND DOC_NO='" + pDocNo + "' AND MODULE_ID=" + (clsSalesInvoice.ModuleID));
                }

                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_SAL_INVOICE_HEADER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + pCompanyID + " AND INVOICE_NO='" + pDocNo + "' AND INVOICE_DATE='" + pDocDate + "' ");
                data.Execute("UPDATE D_SAL_INVOICE_DETAIL SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + pCompanyID + " AND INVOICE_NO='" + pDocNo + "' AND INVOICE_DATE='" + pDocDate + "' ");

                //Cancel the SJ associated with this invoice
                if (data.IsRecordExist("SELECT A.VOUCHER_NO FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.VOUCHER_TYPE=" + FinanceGlobal.TYPE_SALES_JOURNAL + " AND B.INVOICE_NO='" + pDocNo + "' AND B.INVOICE_DATE='" + pDocDate + "' ", FinanceGlobal.FinURL)) {
                    String VoucherNo = data.getStringValueFromDB("SELECT A.VOUCHER_NO FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.VOUCHER_TYPE=" + FinanceGlobal.TYPE_SALES_JOURNAL + " AND B.INVOICE_NO='" + pDocNo + "' AND B.INVOICE_DATE='" + pDocDate + "' ", FinanceGlobal.FinURL); //AND B.MODULE_ID="+clsSalesInvoice.ModuleID
                    int VoucherCompanyID = data.getIntValueFromDB("SELECT A.COMPANY_ID FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.VOUCHER_TYPE=" + FinanceGlobal.TYPE_SALES_JOURNAL + " AND B.INVOICE_NO='" + pDocNo + "' AND B.INVOICE_DATE='" + pDocDate + "' ", FinanceGlobal.FinURL); //AND B.MODULE_ID="+clsSalesInvoice.ModuleID

                    clsVoucher.CancelDoc(VoucherCompanyID, VoucherNo, clsVoucher.getVoucherModuleID(FinanceGlobal.TYPE_SALES_JOURNAL));
                }

                //Rest advance payment adjustments from the receipt vouchers
                ResultSet rsReceipts = data.getResult("SELECT A.VOUCHER_NO,B.AMOUNT,B.MATCHED_DATE FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B "
                        + "WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND B.EFFECT='C' AND A.VOUCHER_TYPE<>" + FinanceGlobal.TYPE_SALES_JOURNAL + " "
                        + "AND B.INVOICE_DATE='" + pDocDate + "' AND B.INVOICE_NO='" + pDocNo + "' ", FinanceGlobal.FinURL);
                rsReceipts.first();

                if (rsReceipts.getRow() > 0) {
                    while (!rsReceipts.isAfterLast()) {
                        String VoucherNo = rsReceipts.getString("VOUCHER_NO");
                        //Remove the reference of invoice nos.
                        data.Execute("UPDATE D_FIN_VOUCHER_DETAIL SET INVOICE_NO='',INVOICE_DATE='',GRN_NO='',GRN_DATE='',MODULE_ID=0,REF_COMPANY_ID=0,MATCHED=0,MATCHED_DATE='0000-00-00' WHERE VOUCHER_NO='" + VoucherNo + "' AND INVOICE_NO='" + pDocNo + "' AND INVOICE_DATE='" + pDocDate + "' ", FinanceGlobal.FinURL);
                        data.Execute("UPDATE D_FIN_VOUCHER_DETAIL_EX SET INVOICE_NO='',INVOICE_DATE='',GRN_NO='',GRN_DATE='',MODULE_ID=0,REF_COMPANY_ID=0,MATCHED=0,MATCHED_DATE='0000-00-00' WHERE VOUCHER_NO='" + VoucherNo + "' AND INVOICE_NO='" + pDocNo + "' AND INVOICE_DATE='" + pDocDate + "' ", FinanceGlobal.FinURL);
                        data.Execute("UPDATE D_FIN_VOUCHER_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE VOUCHER_NO='" + VoucherNo + "' ", FinanceGlobal.FinURL);
                        rsReceipts.next();
                    }
                }
                Cancelled = true;
            }
            rsTmp.close();
            rsIndent.close();
        } catch (Exception e) {
        }
        return Cancelled;
    }

    public static boolean IsDocExist(int CompanyID, String InvoiceNo, String InvoiceDate) {
        return data.IsRecordExist("SELECT INVOICE_NO FROM D_SAL_INVOICE_HEADER WHERE COMPANY_ID=" + CompanyID + " AND INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' AND CANCELLED=0");
    }

    public static double getInvoiceTotal(int CompanyID, String InvoiceNo, String InvoiceDate) {
        return data.getDoubleValueFromDB("SELECT NET_AMOUNT FROM D_SAL_INVOICE_HEADER WHERE COMPANY_ID=" + CompanyID + " AND INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ");
    }

    public static String getInvoiceDate(int CompanyID, String InvoiceNo) {
        return data.getStringValueFromDB("SELECT INVOICE_DATE FROM D_SAL_INVOICE_HEADER WHERE COMPANY_ID=" + CompanyID + " AND INVOICE_NO='" + InvoiceNo + "'");
    }

    public static String getInvoiceNo(String AgentAlpha) {
        try {

            String[] arrAgentAlpha = AgentAlpha.split("/");

            String AreaID = arrAgentAlpha[0];
            String SrNo = arrAgentAlpha[1];
            int AgentSrNo = UtilFunctions.CInt(SrNo);

            String InvoiceNo = data.getStringValueFromDB("SELECT INVOICE_NO FROM D_SAL_INVOICE_HEADER WHERE AGENT_SR_NO=" + AgentSrNo + " AND PARTY_CODE IN (SELECT PARTY_CODE FROM D_SAL_PARTY_MASTER WHERE AREA_ID='" + AreaID + "')");

            return InvoiceNo;
        } catch (Exception e) {
            return "";
        }
    }

    public boolean PostSJTypeSuiting(int CompanyID, String InvoiceNo, String InvoiceDate, boolean AutoAdj) {
        try {
            //Get Object

            System.out.println("SSSS 0");
            clsSalesInvoice objInvoice = (clsSalesInvoice) (new clsSalesInvoice()).getObject(CompanyID, InvoiceNo, InvoiceDate);

            clsVoucher objVoucher = new clsVoucher();

            double NetAmount = objInvoice.getAttribute("NET_AMOUNT").getDouble();
            double HundiCharges = objInvoice.getAttribute("COLUMN_8_AMT").getDouble();
            double InsuranceCharges = objInvoice.getAttribute("COLUMN_9_AMT").getDouble();
            double BankCharges = objInvoice.getAttribute("COLUMN_10_AMT").getDouble();
            double OtherCharges = objInvoice.getAttribute("COLUMN_12_AMT").getDouble();
            //double TCCharges=objInvoice.getAttribute("COLUMN_7_AMT").getDouble();
            double CashDiscount = objInvoice.getAttribute("COLUMN_19_AMT").getDouble();
            double CashDiscountNew = objInvoice.getAttribute("CASH_DISC_AMT").getDouble();
            System.out.println("Cash discount " + CashDiscountNew);

            double VAT = objInvoice.getAttribute("COLUMN_11_AMT").getDouble();
            double CST5 = objInvoice.getAttribute("COLUMN_13_AMT").getDouble();
            double Cess = 0;
            double CGST = objInvoice.getAttribute("CGST_AMT").getDouble();
            double IGST = objInvoice.getAttribute("IGST_AMT").getDouble();
            double SGST = objInvoice.getAttribute("SGST_AMT").getDouble();

            //double GSTAmount=objInvoice.getAttribute("COLUMN_14_AMT").getDouble();
            //double RoundOff=EITLERPGLOBAL.round((HundiCharges+InsuranceCharges+BankCharges+OtherCharges+TCCharges+CashDiscount+Cess+GSTAmount+VAT+CST5)-NetAmount,2);
            double RoundOff = EITLERPGLOBAL.round((HundiCharges + InsuranceCharges + BankCharges + OtherCharges + CashDiscount + Cess + IGST + CGST + SGST + VAT + CST5) - NetAmount, 2);

            NetAmount += CashDiscount;
            //NetAmount = NetAmount + CashDiscountNew;

            double GrossAmount = NetAmount - (HundiCharges + InsuranceCharges + BankCharges + OtherCharges + Cess + IGST + CGST + SGST + VAT + CST5); //+TCCharges

            String PartyCode = objInvoice.getAttribute("PARTY_CODE").getString();
            //String InvoiceDate=objInvoice.getAttribute("INVOICE_DATE").getString();
            String ICType = objInvoice.getAttribute("IC_TYPE").getString();
            String SalesAccountCode = "";
            int WHCode = objInvoice.getAttribute("WAREHOUSE_CODE").getInt();

            String SelPrefix = "";
            String SelSuffix = "";
            int FFNo = 0;

            System.out.println("SSSS 1");
            //****** Prepare Voucher Object ********//

            setAttribute("FIN_HIERARCHY_ID", 0);

            //(1) Select the Hierarchy
            HashMap List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesInvoice.ModuleID, "CHOOSE_HIERARCHY", "INVOICE_TYPE", "1");

            if (List.size() > 0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                int HierarchyID = UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                setAttribute("FIN_HIERARCHY_ID", HierarchyID);
            }

            ResultSet rsVoucher = data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID=" + clsVoucher.SalesJournalVoucherModuleID);
            rsVoucher.first();

            if (rsVoucher.getRow() > 0) {
                SelPrefix = rsVoucher.getString("PREFIX_CHARS");
                SelSuffix = rsVoucher.getString("SUFFIX_CHARS");
                FFNo = rsVoucher.getInt("FIRSTFREE_NO");
            }

            int VoucherSrNo = 0;

            objVoucher = new clsVoucher();
            objVoucher.LoadDataEx(EITLERPGLOBAL.gCompanyID);

            objVoucher.setAttribute("PREFIX", SelPrefix);
            objVoucher.setAttribute("SUFFIX", SelSuffix);
            objVoucher.setAttribute("FFNO", FFNo);
            objVoucher.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            objVoucher.setAttribute("VOUCHER_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));

            //SUITING
            objVoucher.setAttribute("BOOK_CODE", "01");
            SalesAccountCode = "301017";

            //            if(ICType.equals("1")) {
            //                objVoucher.setAttribute("BOOK_CODE","01");
            //                SalesAccountCode="301017";
            //            }
            //            //FILTER FABRICS
            //            if(ICType.equals("4")) {
            //                objVoucher.setAttribute("BOOK_CODE","11");
            //                SalesAccountCode="301347";
            //            }
            //BLANKET SALES
            //            if(ICType.equals("2")) {
            //                objVoucher.setAttribute("BOOK_CODE","01");
            //                SalesAccountCode="301079";
            //            }
            if (WHCode == 1) {
                objVoucher.setAttribute("BOOK_CODE", "01");
                SalesAccountCode = "301017";
            }
            if (WHCode == 0) {
                objVoucher.setAttribute("BOOK_CODE", "01");
                SalesAccountCode = "301018";
            }

            if (WHCode == 4) {
                objVoucher.setAttribute("BOOK_CODE", "01");
                SalesAccountCode = "301079";
            }

            //SHAWLS SALES
            if (ICType.equals("3")) {
                objVoucher.setAttribute("BOOK_CODE", "01");
                SalesAccountCode = "301134";
            }

            objVoucher.setAttribute("VOUCHER_TYPE", FinanceGlobal.TYPE_SALES_JOURNAL);
            objVoucher.setAttribute("CHEQUE_NO", "");
            objVoucher.setAttribute("CHEQUE_DATE", "0000-00-00");
            objVoucher.setAttribute("BANK_NAME", "");
            objVoucher.setAttribute("PO_NO", "");
            objVoucher.setAttribute("PO_DATE", "0000-00-00");
            objVoucher.setAttribute("INVOICE_NO", "");
            objVoucher.setAttribute("INVOICE_DATE", "0000-00-00");
            objVoucher.setAttribute("GRN_NO", "");
            objVoucher.setAttribute("GRN_DATE", "0000-00-00");
            objVoucher.setAttribute("ST_CATEGORY", "");
            objVoucher.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
            objVoucher.setAttribute("REMARKS", "");

            objVoucher.setAttribute("HIERARCHY_ID", getAttribute("FIN_HIERARCHY_ID").getInt());
            int FirstUserID = data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID=" + getAttribute("FIN_HIERARCHY_ID").getInt() + " AND SR_NO=1");
            objVoucher.setAttribute("FROM", FirstUserID);
            objVoucher.setAttribute("TO", FirstUserID);
            objVoucher.setAttribute("FROM_REMARKS", "");
            objVoucher.setAttribute("APPROVAL_STATUS", "F"); //Final Approve

            objVoucher.colVoucherItems.clear();

            //(1) Entry no.1 => Debit party with net amount
            //String PartyMainCode=clsBook.getBookMainCode(CompanyID, objVoucher.getAttribute("BOOK_CODE").getString());
            String PartyMainCode = "210027";

            //(1) Sales Account
            System.out.println("Gross Amount Cr" + GrossAmount);
            if (GrossAmount > 0) {
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", SalesAccountCode);
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", GrossAmount);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }

            System.out.println("HundiCharges Cr" + HundiCharges);
            //(2) Hundi Charge
            if (HundiCharges > 0) {

                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "450227");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", HundiCharges);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);

            }

            System.out.println("InsuranceCharges Cr" + InsuranceCharges);
            //(3) Insurance Charges
            if (InsuranceCharges > 0) {

                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "427027");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", InsuranceCharges);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);

            }

            System.out.println("Bank Charges Cr " + BankCharges);
            //(4) Bank Charges
            if (BankCharges > 0) {

                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "450038");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", BankCharges);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);

            }

            System.out.println("Other Charges Cr " + OtherCharges);
            //(5) Other Charges
            if (OtherCharges > 0) {

                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "450320");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", OtherCharges);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);

            }

            //(6) TCCharges
            /*if(TCCharges>0) {
             
             VoucherSrNo++;
             clsVoucherItem objVoucherItem=new clsVoucherItem();
             objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
             objVoucherItem.setAttribute("EFFECT","C");
             objVoucherItem.setAttribute("ACCOUNT_ID",1);
             objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","450045");
             objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
             objVoucherItem.setAttribute("AMOUNT",TCCharges);
             objVoucherItem.setAttribute("REMARKS","");
             objVoucherItem.setAttribute("PO_NO","");
             objVoucherItem.setAttribute("PO_DATE","0000-00-00");
             objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
             objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
             objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
             objVoucherItem.setAttribute("GRN_NO","");
             objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
             objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
             objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
             objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
             
             }
             */
            System.out.println("Cess Cr " + Cess);
            //(8) Cess
            if (Cess > 0) {

                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127042");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", Cess);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);

            }

            //(9) GST Amount
            /*
             if(GSTAmount>0) {
             
             VoucherSrNo++;
             clsVoucherItem objVoucherItem=new clsVoucherItem();
             objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
             objVoucherItem.setAttribute("EFFECT","C");
             objVoucherItem.setAttribute("ACCOUNT_ID",1);
             objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","127516");
             objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
             objVoucherItem.setAttribute("AMOUNT",GSTAmount);
             objVoucherItem.setAttribute("REMARKS","");
             objVoucherItem.setAttribute("PO_NO","");
             objVoucherItem.setAttribute("PO_DATE","0000-00-00");
             objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
             objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
             objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
             objVoucherItem.setAttribute("GRN_NO","");
             objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
             objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
             objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
             objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
             
             }
             */
            System.out.println("CST5 Cr " + CST5);
            if (CST5 > 0) {
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127550");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", CST5);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }

            System.out.println("CGST Cr " + CGST);
            //(10) GST Amount
            if (CGST > 0) {

                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127565");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", CGST);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }
            System.out.println("SGST Cr " + SGST);
            if (SGST > 0) {
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127567");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", SGST);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }

            System.out.println("IGST Cr " + IGST);
            if (IGST > 0) {
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127569");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", IGST);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }

            System.out.println("VAT Cr " + VAT);
            if (VAT > 0) {
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127561");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", VAT);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }

            System.out.println("Net Amount Dr " + NetAmount);
            if (NetAmount > 0) {
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "D");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", PartyMainCode);
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", PartyCode);
                objVoucherItem.setAttribute("AMOUNT", NetAmount);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("LINK_NO", clsSalesInvoice.getAgentAlphaSrNo(InvoiceNo, InvoiceDate, PartyCode));
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }
           /*
            if (CashDiscountNew > 0) {
                System.out.println("CashDiscount Cr " + CashDiscountNew);
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", PartyMainCode);
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", PartyCode);
                objVoucherItem.setAttribute("AMOUNT", CashDiscountNew);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("LINK_NO", clsSalesInvoice.getAgentAlphaSrNo(InvoiceNo, InvoiceDate, PartyCode));
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }
            */
            /*
            System.out.println("CashDiscount Dr " + CashDiscountNew);
            if (CashDiscountNew > 0) {
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "D");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "435091");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", CashDiscountNew);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }
*/
            objVoucher.DoNotValidateAccounts = true;

            if (objVoucher.Insert()) {

                //SJ Posted. Automatically adjust advance receipt amount
                String SJNo = objVoucher.getAttribute("VOUCHER_NO").getString();
                data.Execute("UPDATE D_FIN_VOUCHER_HEADER SET APPROVED=1 AND APPROVED_DATE=CURDATE() WHERE VOUCHER_NO='" + SJNo + "'", FinanceGlobal.FinURL);
                System.out.println("Suting SJ Posted " + SJNo);
                if (AutoAdj) {
                    (new clsDrAdjustment()).AutoAdjustReceiptAmount(EITLERPGLOBAL.gCompanyID, SJNo);
                }
                return true;
            } else {
                LastError = objVoucher.LastError;
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean PostSJTypeSuitingold(int CompanyID, String InvoiceNo, String InvoiceDate, boolean AutoAdj) {
        try {
            //Get Object
            clsSalesInvoice objInvoice = (clsSalesInvoice) (new clsSalesInvoice()).getObject(CompanyID, InvoiceNo, InvoiceDate);

            clsVoucher objVoucher = new clsVoucher();

            double NetAmount = objInvoice.getAttribute("NET_AMOUNT").getDouble();
            double HundiCharges = objInvoice.getAttribute("COLUMN_8_AMT").getDouble();
            double InsuranceCharges = objInvoice.getAttribute("COLUMN_9_AMT").getDouble();
            double BankCharges = objInvoice.getAttribute("COLUMN_10_AMT").getDouble();
            double OtherCharges = objInvoice.getAttribute("COLUMN_12_AMT").getDouble();
            double TCCharges = objInvoice.getAttribute("COLUMN_7_AMT").getDouble();
            double CashDiscount = objInvoice.getAttribute("COLUMN_19_AMT").getDouble();
            double VAT = objInvoice.getAttribute("COLUMN_11_AMT").getDouble();
            double CST5 = objInvoice.getAttribute("COLUMN_13_AMT").getDouble();
            double Cess = 0;
            double GSTAmount = objInvoice.getAttribute("COLUMN_14_AMT").getDouble();
            double RoundOff = EITLERPGLOBAL.round((HundiCharges + InsuranceCharges + BankCharges + OtherCharges + TCCharges + CashDiscount + Cess + GSTAmount + VAT + CST5) - NetAmount, 2);

            NetAmount += CashDiscount;

            double GrossAmount = NetAmount - (HundiCharges + InsuranceCharges + BankCharges + OtherCharges + Cess + GSTAmount + VAT + CST5); //+TCCharges

            String PartyCode = objInvoice.getAttribute("PARTY_CODE").getString();
            //String InvoiceDate=objInvoice.getAttribute("INVOICE_DATE").getString();
            String ICType = objInvoice.getAttribute("IC_TYPE").getString();
            String SalesAccountCode = "";
            int WHCode = objInvoice.getAttribute("WAREHOUSE_CODE").getInt();

            String SelPrefix = "";
            String SelSuffix = "";
            int FFNo = 0;

            //****** Prepare Voucher Object ********//
            setAttribute("FIN_HIERARCHY_ID", 0);

            //(1) Select the Hierarchy
            HashMap List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesInvoice.ModuleID, "CHOOSE_HIERARCHY", "INVOICE_TYPE", "1");

            if (List.size() > 0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                int HierarchyID = UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                setAttribute("FIN_HIERARCHY_ID", HierarchyID);
            }

            ResultSet rsVoucher = data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID=" + clsVoucher.SalesJournalVoucherModuleID);
            rsVoucher.first();

            if (rsVoucher.getRow() > 0) {
                SelPrefix = rsVoucher.getString("PREFIX_CHARS");
                SelSuffix = rsVoucher.getString("SUFFIX_CHARS");
                FFNo = rsVoucher.getInt("FIRSTFREE_NO");
            }

            int VoucherSrNo = 0;

            objVoucher = new clsVoucher();
            objVoucher.LoadDataEx(EITLERPGLOBAL.gCompanyID);

            objVoucher.setAttribute("PREFIX", SelPrefix);
            objVoucher.setAttribute("SUFFIX", SelSuffix);
            objVoucher.setAttribute("FFNO", FFNo);
            objVoucher.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            objVoucher.setAttribute("VOUCHER_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));

            //SUITING
            objVoucher.setAttribute("BOOK_CODE", "01");
            SalesAccountCode = "301017";

            //            if(ICType.equals("1")) {
            //                objVoucher.setAttribute("BOOK_CODE","01");
            //                SalesAccountCode="301017";
            //            }
            //            //FILTER FABRICS
            //            if(ICType.equals("4")) {
            //                objVoucher.setAttribute("BOOK_CODE","11");
            //                SalesAccountCode="301347";
            //            }
            //BLANKET SALES
            //            if(ICType.equals("2")) {
            //                objVoucher.setAttribute("BOOK_CODE","01");
            //                SalesAccountCode="301079";
            //            }
            if (WHCode == 1) {
                objVoucher.setAttribute("BOOK_CODE", "01");
                SalesAccountCode = "301017";
            }
            if (WHCode == 0) {
                objVoucher.setAttribute("BOOK_CODE", "01");
                SalesAccountCode = "301018";
            }

            if (WHCode == 4) {
                objVoucher.setAttribute("BOOK_CODE", "01");
                SalesAccountCode = "301079";
            }

            //SHAWLS SALES
            if (ICType.equals("3")) {
                objVoucher.setAttribute("BOOK_CODE", "01");
                SalesAccountCode = "301134";
            }

            objVoucher.setAttribute("VOUCHER_TYPE", FinanceGlobal.TYPE_SALES_JOURNAL);
            objVoucher.setAttribute("CHEQUE_NO", "");
            objVoucher.setAttribute("CHEQUE_DATE", "0000-00-00");
            objVoucher.setAttribute("BANK_NAME", "");
            objVoucher.setAttribute("PO_NO", "");
            objVoucher.setAttribute("PO_DATE", "0000-00-00");
            objVoucher.setAttribute("INVOICE_NO", "");
            objVoucher.setAttribute("INVOICE_DATE", "0000-00-00");
            objVoucher.setAttribute("GRN_NO", "");
            objVoucher.setAttribute("GRN_DATE", "0000-00-00");
            objVoucher.setAttribute("ST_CATEGORY", "");
            objVoucher.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
            objVoucher.setAttribute("REMARKS", "");

            objVoucher.setAttribute("HIERARCHY_ID", getAttribute("FIN_HIERARCHY_ID").getInt());
            int FirstUserID = data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID=" + getAttribute("FIN_HIERARCHY_ID").getInt() + " AND SR_NO=1");
            objVoucher.setAttribute("FROM", FirstUserID);
            objVoucher.setAttribute("TO", FirstUserID);
            objVoucher.setAttribute("FROM_REMARKS", "");
            objVoucher.setAttribute("APPROVAL_STATUS", "F"); //Final Approve

            objVoucher.colVoucherItems.clear();

            //(1) Entry no.1 => Debit party with net amount
            //String PartyMainCode=clsBook.getBookMainCode(CompanyID, objVoucher.getAttribute("BOOK_CODE").getString());
            String PartyMainCode = "210027";

            //(1) Sales Account
            if (GrossAmount > 0) {
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", SalesAccountCode);
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", GrossAmount);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }

            //(2) Hundi Charges
            if (HundiCharges > 0) {

                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "450227");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", HundiCharges);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);

            }

            //(3) Insurance Charges
            if (InsuranceCharges > 0) {

                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "427027");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", InsuranceCharges);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);

            }

            //(4) Bank Charges
            if (BankCharges > 0) {

                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "450038");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", BankCharges);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);

            }

            //(5) Other Charges
            if (OtherCharges > 0) {

                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "450320");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", OtherCharges);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);

            }

            //(6) TCCharges
            /*if(TCCharges>0) {
             
             VoucherSrNo++;
             clsVoucherItem objVoucherItem=new clsVoucherItem();
             objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
             objVoucherItem.setAttribute("EFFECT","C");
             objVoucherItem.setAttribute("ACCOUNT_ID",1);
             objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","450045");
             objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
             objVoucherItem.setAttribute("AMOUNT",TCCharges);
             objVoucherItem.setAttribute("REMARKS","");
             objVoucherItem.setAttribute("PO_NO","");
             objVoucherItem.setAttribute("PO_DATE","0000-00-00");
             objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
             objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
             objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
             objVoucherItem.setAttribute("GRN_NO","");
             objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
             objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
             objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
             objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
             
             }
             */
            //(8) Cess
            if (Cess > 0) {

                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127042");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", Cess);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);

            }

            //(9) GST Amount
            if (GSTAmount > 0) {

                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127516");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", GSTAmount);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);

            }

            if (CST5 > 0) {
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127550");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", CST5);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }

            //(10) GST Amount
            if (VAT > 0) {
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127561");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", VAT);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }

            VoucherSrNo++;
            clsVoucherItem objVoucherItem = new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT", "D");
            objVoucherItem.setAttribute("ACCOUNT_ID", 1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", PartyMainCode);
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", PartyCode);
            objVoucherItem.setAttribute("AMOUNT", NetAmount);
            objVoucherItem.setAttribute("REMARKS", "");
            objVoucherItem.setAttribute("PO_NO", "");
            objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
            objVoucherItem.setAttribute("LINK_NO", clsSalesInvoice.getAgentAlphaSrNo(InvoiceNo, InvoiceDate, PartyCode));
            objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
            objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
            objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
            objVoucherItem.setAttribute("GRN_NO", "");
            objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
            objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);

            objVoucher.DoNotValidateAccounts = true;

            if (objVoucher.Insert()) {

                //SJ Posted. Automatically adjust advance receipt amount
                String SJNo = objVoucher.getAttribute("VOUCHER_NO").getString();
                data.Execute("UPDATE D_FIN_VOUCHER_HEADER SET APPROVED=1 AND APPROVED_DATE=CURDATE() WHERE VOUCHER_NO='" + SJNo + "'", FinanceGlobal.FinURL);
                System.out.println("Suting SJ Posted " + SJNo);
                if (AutoAdj) {
                    (new clsDrAdjustment()).AutoAdjustReceiptAmount(EITLERPGLOBAL.gCompanyID, SJNo);
                }
                return true;
            } else {
                LastError = objVoucher.LastError;
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean PostSJTypeFelt(int CompanyID, String InvoiceNo, String InvoiceDate, boolean AutoAdj) {
        try {
            //Get Object

            System.out.println("SSSS 0");
            clsSalesInvoice objInvoice = (clsSalesInvoice) (new clsSalesInvoice()).getObject(CompanyID, InvoiceNo, InvoiceDate);

            clsVoucher objVoucher = new clsVoucher();

            double NetAmount = objInvoice.getAttribute("NET_AMOUNT").getDouble();
            double NetAmount1 = objInvoice.getAttribute("NET_AMOUNT").getDouble();
            String ch = String.valueOf(objInvoice.getAttribute("PAYMENT_TERM_CODE").getDouble());
            //double HundiCharges=objInvoice.getAttribute("COLUMN_8_AMT").getDouble(); //This field to be removed, in Felt invoices, this field represents percentage of something.
            //double InsuranceCharges=objInvoice.getAttribute("COLUMN_9_AMT").getDouble(); //This field was mis-placed.
            //double OtherCharges=objInvoice.getAttribute("COLUMN_12_AMT").getDouble(); //To be included in Sales Gross Amount, no need to bifurcate
            //double TCCharges=objInvoice.getAttribute("COLUMN_7_AMT").getDouble(); //TCC Charges field is used to store insurance charges

            double InsuranceCharges = objInvoice.getAttribute("COLUMN_9_AMT").getDouble();
            double BankCharges = objInvoice.getAttribute("COLUMN_10_AMT").getDouble();
            double CashDiscount = objInvoice.getAttribute("COLUMN_19_AMT").getDouble();
            double Cess = 0;
            double GSTAmount = objInvoice.getAttribute("COLUMN_14_AMT").getDouble();
            //double VAT1=objInvoice.getAttribute("COLUMN_8_AMT").getDouble();

            /*
             double VAT1=objInvoice.getAttribute("VAT1").getDouble();
             double VAT4=objInvoice.getAttribute("VAT4").getDouble();
             double CST2=objInvoice.getAttribute("CST2").getDouble();
             double CST5=objInvoice.getAttribute("CST5").getDouble();
             double SD_AMT=objInvoice.getAttribute("SD_AMT").getDouble();
             */
            double CGST = objInvoice.getAttribute("CGST_AMT").getDouble();
            double IGST = objInvoice.getAttribute("IGST_AMT").getDouble();
            double SGST = objInvoice.getAttribute("SGST_AMT").getDouble();
            //double CST5=objInvoice.getAttribute("CST5").getDouble();
            double SD_AMT = objInvoice.getAttribute("SD_AMT").getDouble();

            System.out.println("SSSS 000");

            //System.out.println(SD_AMT);
            //double RoundOff=EITLERPGLOBAL.round((InsuranceCharges+BankCharges+CashDiscount+Cess+GSTAmount+VAT1+VAT4+CST2+CST5)-NetAmount,2);
            //double RoundOff=EITLERPGLOBAL.round((InsuranceCharges+BankCharges+CashDiscount+Cess+GSTAmount+VAT1+VAT4+CST2+CST5)-NetAmount,2);
            double RoundOff = EITLERPGLOBAL.round((InsuranceCharges + BankCharges + CashDiscount + Cess + GSTAmount + CGST + SGST + IGST) - NetAmount, 2);

            NetAmount += CashDiscount;
            NetAmount1 += CashDiscount;
            double GrossAmount = NetAmount - (InsuranceCharges + BankCharges + Cess + GSTAmount + CGST + SGST + IGST);

            String PartyCode = objInvoice.getAttribute("PARTY_CODE").getString();
            //String InvoiceDate=objInvoice.getAttribute("INVOICE_DATE").getString();
            String ICType = objInvoice.getAttribute("IC_TYPE").getString();
            String SalesAccountCode = "";

            String SelPrefix = "";
            String SelSuffix = "";
            int FFNo = 0;

            System.out.println("SSSS 1");

            //****** Prepare Voucher Object ********//
            setAttribute("FIN_HIERARCHY_ID", 0);

            //(1) Select the Hierarchy
            HashMap List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesInvoice.ModuleID, "CHOOSE_HIERARCHY", "INVOICE_TYPE", "1");

            if (List.size() > 0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                int HierarchyID = UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                setAttribute("FIN_HIERARCHY_ID", HierarchyID);
            }

            ResultSet rsVoucher = data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID=" + clsVoucher.SalesJournalVoucherModuleID);
            rsVoucher.first();

            if (rsVoucher.getRow() > 0) {
                SelPrefix = rsVoucher.getString("PREFIX_CHARS");
                SelSuffix = rsVoucher.getString("SUFFIX_CHARS");
                FFNo = rsVoucher.getInt("FIRSTFREE_NO");
            }

            int VoucherSrNo = 0;

            objVoucher = new clsVoucher();
            objVoucher.LoadDataEx(EITLERPGLOBAL.gCompanyID);

            objVoucher.setAttribute("PREFIX", SelPrefix);
            objVoucher.setAttribute("SUFFIX", SelSuffix);
            objVoucher.setAttribute("FFNO", FFNo);
            objVoucher.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            objVoucher.setAttribute("VOUCHER_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));

            //SUITING
            objVoucher.setAttribute("BOOK_CODE", "09");
            SalesAccountCode = "301378";

            if (ICType.equals("1")) {
                objVoucher.setAttribute("BOOK_CODE", "01");
                SalesAccountCode = "301378";
            }

            //FILTER FABRICS
            if (ICType.equals("4")) {
                objVoucher.setAttribute("BOOK_CODE", "11");
                SalesAccountCode = "301347";
            }

            //BLANKET SALES
            if (ICType.equals("2")) {
                objVoucher.setAttribute("BOOK_CODE", "01");
                SalesAccountCode = "301079";
            }

            //SHAWLS SALES
            if (ICType.equals("3")) {
                objVoucher.setAttribute("BOOK_CODE", "01");
                SalesAccountCode = "301347";
            }

            System.out.println("SSSS 2");

            System.out.println("Invoice " + InvoiceNo + " 1");
            objVoucher.setAttribute("VOUCHER_TYPE", FinanceGlobal.TYPE_SALES_JOURNAL);
            objVoucher.setAttribute("CHEQUE_NO", "");
            objVoucher.setAttribute("CHEQUE_DATE", "0000-00-00");
            objVoucher.setAttribute("BANK_NAME", "");
            objVoucher.setAttribute("PO_NO", "");
            objVoucher.setAttribute("PO_DATE", "0000-00-00");
            objVoucher.setAttribute("INVOICE_NO", "");
            objVoucher.setAttribute("INVOICE_DATE", "0000-00-00");
            objVoucher.setAttribute("GRN_NO", "");
            objVoucher.setAttribute("GRN_DATE", "0000-00-00");
            objVoucher.setAttribute("ST_CATEGORY", "");
            objVoucher.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
            objVoucher.setAttribute("REMARKS", "");

            objVoucher.setAttribute("HIERARCHY_ID", getAttribute("FIN_HIERARCHY_ID").getInt());
            int FirstUserID = data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID=" + getAttribute("FIN_HIERARCHY_ID").getInt() + " AND SR_NO=1");
            objVoucher.setAttribute("FROM", FirstUserID);
            objVoucher.setAttribute("TO", FirstUserID);
            objVoucher.setAttribute("FROM_REMARKS", "");
            objVoucher.setAttribute("APPROVAL_STATUS", "F"); //Hold Voucher

            objVoucher.colVoucherItems.clear();

            //(1) Entry no.1 => Debit party with net amount
            //String PartyMainCode=clsBook.getBookMainCode(CompanyID, objVoucher.getAttribute("BOOK_CODE").getString());
            String PartyMainCode = "210010";

            System.out.println("Invoice " + InvoiceNo + " 2");
            //(1) Sales Account
            if (GrossAmount > 0) {
                System.out.println("SSSS 3");

                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", SalesAccountCode);
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", GrossAmount);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }
            System.out.println("Invoice " + InvoiceNo + " 3");
            //(3) Insurance Charges
            if (InsuranceCharges > 0) {

                System.out.println("SSSS 4");
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "427027");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", InsuranceCharges);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }
            System.out.println("Invoice " + InvoiceNo + " 4");
            //(4) Bank Charges
            if (BankCharges > 0) {

                System.out.println("SSSS 5");
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "450038");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", BankCharges);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }

            System.out.println("Invoice " + InvoiceNo + " 5");
            //(8) Cess
            if (Cess > 0) {
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127042");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", Cess);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);

            }
            //(9) GST Amount
            if (GSTAmount > 0) {
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127516");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", GSTAmount);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);

            }
            /*
             if(VAT1>0) {
             VoucherSrNo++;
             clsVoucherItem objVoucherItem=new clsVoucherItem();
             objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
             objVoucherItem.setAttribute("EFFECT","C");
             objVoucherItem.setAttribute("ACCOUNT_ID",1);
             objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","127549");
             objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
             objVoucherItem.setAttribute("AMOUNT",VAT1);
             objVoucherItem.setAttribute("REMARKS","");
             objVoucherItem.setAttribute("PO_NO","");
             objVoucherItem.setAttribute("PO_DATE","0000-00-00");
             objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
             objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
             objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
             objVoucherItem.setAttribute("GRN_NO","");
             objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
             objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
             objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
             objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
             }
             if(VAT4>0) {
             VoucherSrNo++;
             clsVoucherItem objVoucherItem=new clsVoucherItem();
             objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
             objVoucherItem.setAttribute("EFFECT","C");
             objVoucherItem.setAttribute("ACCOUNT_ID",1);
             objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","127548");
             objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
             objVoucherItem.setAttribute("AMOUNT",VAT4);
             objVoucherItem.setAttribute("REMARKS","");
             objVoucherItem.setAttribute("PO_NO","");
             objVoucherItem.setAttribute("PO_DATE","0000-00-00");
             objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
             objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
             objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
             objVoucherItem.setAttribute("GRN_NO","");
             objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
             objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
             objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
             objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
             }
             
             if(CST2>0) {
             VoucherSrNo++;
             clsVoucherItem objVoucherItem=new clsVoucherItem();
             objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
             objVoucherItem.setAttribute("EFFECT","C");
             objVoucherItem.setAttribute("ACCOUNT_ID",1);
             objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","127551");
             objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
             objVoucherItem.setAttribute("AMOUNT",CST2);
             objVoucherItem.setAttribute("REMARKS","");
             objVoucherItem.setAttribute("PO_NO","");
             objVoucherItem.setAttribute("PO_DATE","0000-00-00");
             objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
             objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
             objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
             objVoucherItem.setAttribute("GRN_NO","");
             objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
             objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
             objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
             objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
             }
             
             if(CST5>0) {
             VoucherSrNo++;
             clsVoucherItem objVoucherItem=new clsVoucherItem();
             objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
             objVoucherItem.setAttribute("EFFECT","C");
             objVoucherItem.setAttribute("ACCOUNT_ID",1);
             objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","127550");
             objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
             objVoucherItem.setAttribute("AMOUNT",CST5);
             objVoucherItem.setAttribute("REMARKS","");
             objVoucherItem.setAttribute("PO_NO","");
             objVoucherItem.setAttribute("PO_DATE","0000-00-00");
             objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
             objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
             objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
             objVoucherItem.setAttribute("GRN_NO","");
             objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
             objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
             objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
             objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
             }
             */

            if (CGST > 0) {

                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127566");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", CGST);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }

            if (SGST > 0) {
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127568");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", SGST);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }

            if (IGST > 0) {
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127570");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", IGST);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }

            System.out.println("Invoice " + InvoiceNo + " 8");
            if (NetAmount > 0) {
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "D");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", PartyMainCode);
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", PartyCode);
                objVoucherItem.setAttribute("AMOUNT", NetAmount);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }

            /*
             if(SD_AMT>0) {
             System.out.println("Invoice "+InvoiceNo+" 6");
             VoucherSrNo++;
             clsVoucherItem objVoucherItem=new clsVoucherItem();
             //objVoucherItem=new clsVoucherItem();
             objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
             objVoucherItem.setAttribute("EFFECT","C");
             objVoucherItem.setAttribute("ACCOUNT_ID",1);
             objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","132802");
             objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",PartyCode);
             objVoucherItem.setAttribute("AMOUNT",SD_AMT);
             objVoucherItem.setAttribute("REMARKS","SD");
             objVoucherItem.setAttribute("PO_NO","");
             objVoucherItem.setAttribute("PO_DATE","0000-00-00");
             objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
             objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
             objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
             objVoucherItem.setAttribute("GRN_NO","");
             objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
             objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
             objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
             objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
             }
             
             if(SD_AMT>0) {
             System.out.println("Invoice "+InvoiceNo+" 7");
             VoucherSrNo++;
             clsVoucherItem objVoucherItem=new clsVoucherItem();
             //objVoucherItem=new clsVoucherItem();
             objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
             objVoucherItem.setAttribute("EFFECT","D");
             objVoucherItem.setAttribute("ACCOUNT_ID",1);
             objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","132803");
             objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",PartyCode);
             objVoucherItem.setAttribute("AMOUNT",SD_AMT);
             objVoucherItem.setAttribute("REMARKS","SD");
             objVoucherItem.setAttribute("PO_NO","");
             objVoucherItem.setAttribute("PO_DATE","0000-00-00");
             objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
             objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
             objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
             objVoucherItem.setAttribute("GRN_NO","");
             objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
             objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
             objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
             objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
             }
             */
            objVoucher.DoNotValidateAccounts = true;
            System.out.println("Invoice " + InvoiceNo + " 9.");

            if (objVoucher.Insert()) {
                System.out.println("Invoice " + InvoiceNo + " 10");
                //SJ Posted. Automatically adjust advance receipt amount
                String SJNo = objVoucher.getAttribute("VOUCHER_NO").getString();
                data.Execute("UPDATE D_FIN_VOUCHER_HEADER SET APPROVED=1 AND APPROVED_DATE=CURDATE() WHERE VOUCHER_NO='" + SJNo + "'", FinanceGlobal.FinURL);
                System.out.println("Felt Invoice SJ Posted " + SJNo);

                if (AutoAdj) {
                    //    System.out.println("sd amt : "+SD_AMT+" 13");
                    (new clsDrAdjustment()).AutoAdjustReceiptAmount(EITLERPGLOBAL.gCompanyID, SJNo);

                }

                // System.out.println("sd amt : "+SD_AMT+" 14");
                double availableAmount = clsAccount.get09AmountByParty("210010", PartyCode, InvoiceDate);

                // System.out.println("Available Balance for SD " + availableAmount);
                if (availableAmount >= SD_AMT) {
                    if (SD_AMT > 0) {

                        if (AutoAdj) {
                            (new clsDrAdjustment()).AutoAdjustReceiptAmountSD(EITLERPGLOBAL.gCompanyID, SJNo);
                        }

                    }

                    System.out.println("sd amt : " + SD_AMT + " 15");

                    if (SD_AMT > 0 && ch.startsWith("9")) {
                        if (PostVoucher93(CompanyID, InvoiceNo, InvoiceDate)) {
                            System.out.println("sd amt : " + SD_AMT + " 20");

                            System.out.println("SELECT VOUCHER_NO  FROM FINANCE.D_FIN_VOUCHER_DETAIL WHERE INVOICE_NO = '" + InvoiceNo + "'   AND INVOICE_DATE = '" + InvoiceDate + "'  AND MAIN_ACCOUNT_CODE = 132803 AND SUBSTRING(VOUCHER_NO,1,2) ='JV' AND  SUBSTRING(VOUCHER_NO,5,2) =93 ORDER BY VOUCHER_NO,SR_NO  LIMIT 1 ");

                            String SDJVNo = data.getStringValueFromDB("SELECT VOUCHER_NO  FROM FINANCE.D_FIN_VOUCHER_DETAIL WHERE INVOICE_NO = '" + InvoiceNo + "'   AND INVOICE_DATE = '" + InvoiceDate + "'  AND MAIN_ACCOUNT_CODE = 132803 AND SUBSTRING(VOUCHER_NO,1,2) ='JV' AND  SUBSTRING(VOUCHER_NO,5,2) =93 ORDER BY VOUCHER_NO,SR_NO  LIMIT 1 ", FinanceGlobal.FinURL);
                            String RCJVNo = data.getStringValueFromDB("SELECT VOUCHER_NO  FROM FINANCE.D_FIN_VOUCHER_DETAIL WHERE INVOICE_NO = '" + InvoiceNo + "'   AND INVOICE_DATE = '" + InvoiceDate + "'  AND  SUBSTRING(VOUCHER_NO,5,2) NOT IN (93,09) AND AMOUNT = '" + SD_AMT + "' ORDER BY VOUCHER_NO,SR_NO  LIMIT 1 ", FinanceGlobal.FinURL);
                            String RCLINKNo = data.getStringValueFromDB("SELECT LINK_NO   FROM FINANCE.D_FIN_VOUCHER_DETAIL WHERE INVOICE_NO = '" + InvoiceNo + "'   AND INVOICE_DATE = '" + InvoiceDate + "'  AND  SUBSTRING(VOUCHER_NO,5,2) NOT IN (93,09) AND AMOUNT = '" + SD_AMT + "' ORDER BY LINK_NO,SR_NO  LIMIT 1 ", FinanceGlobal.FinURL);
                            String RCValueDate = data.getStringValueFromDB("SELECT VALUE_DATE   FROM FINANCE.D_FIN_VOUCHER_DETAIL WHERE INVOICE_NO = '" + InvoiceNo + "'   AND INVOICE_DATE = '" + InvoiceDate + "'  AND  SUBSTRING(VOUCHER_NO,5,2) NOT IN (93,09) AND AMOUNT = '" + SD_AMT + "' ORDER BY LINK_NO,SR_NO  LIMIT 1 ", FinanceGlobal.FinURL);

                            System.out.println("Auto Voucher No : " + SDJVNo);
                            System.out.println("RC Voucher No : " + RCJVNo);
                            System.out.println("Voucher Link no : " + RCLINKNo);
                            System.out.println("RC Value Date : " + RCValueDate);

                            data.Execute("UPDATE FINANCE.D_FIN_VOUCHER_DETAIL SET GRN_NO = '" + RCJVNo + "' ,GRN_DATE= '" + RCValueDate + "' ,MODULE_ID =89,LINK_NO ='" + RCLINKNo + "',INVOICE_NO= '' ,INVOICE_DATE='0000-00-00',VALUE_DATE = '" + RCValueDate + "' WHERE VOUCHER_NO = '" + SDJVNo + "' AND EFFECT ='D' AND MAIN_ACCOUNT_CODE ='210010'", FinanceGlobal.FinURL);
                            data.Execute("UPDATE FINANCE.D_FIN_VOUCHER_DETAIL SET LINK_NO =CONCAT('" + InvoiceNo + "','/', CASE WHEN EXTRACT(MONTH FROM '" + InvoiceDate + "') IN (4,5,6,7,8,9,10,11,12)   THEN CONCAT(SUBSTRING('" + InvoiceDate + "',3,2) , SUBSTRING('" + InvoiceDate + "',3,2) +1)  WHEN EXTRACT(MONTH FROM '" + InvoiceDate + "') IN (1,2,3)   THEN CONCAT(SUBSTRING('" + InvoiceDate + "',3,2)-1 , SUBSTRING('" + InvoiceDate + "',3,2)) END),MODULE_ID =89 WHERE VOUCHER_NO = '" + SDJVNo + "' AND EFFECT ='C' AND MAIN_ACCOUNT_CODE ='132803'", FinanceGlobal.FinURL);

                            data.Execute("UPDATE FINANCE.D_FIN_VOUCHER_DETAIL_EX SET GRN_NO = '" + RCJVNo + "' ,GRN_DATE= '" + RCValueDate + "' ,MODULE_ID =89,LINK_NO ='" + RCLINKNo + "',INVOICE_NO= '' ,INVOICE_DATE='0000-00-00',VALUE_DATE = '" + RCValueDate + "' WHERE VOUCHER_NO = '" + SDJVNo + "' AND EFFECT ='D' AND MAIN_ACCOUNT_CODE ='210010'", FinanceGlobal.FinURL);
                            data.Execute("UPDATE FINANCE.D_FIN_VOUCHER_DETAIL_EX SET LINK_NO =CONCAT('" + InvoiceNo + "','/', CASE WHEN EXTRACT(MONTH FROM '" + InvoiceDate + "') IN (4,5,6,7,8,9,10,11,12)   THEN CONCAT(SUBSTRING('" + InvoiceDate + "',3,2) , SUBSTRING('" + InvoiceDate + "',3,2) +1)  WHEN EXTRACT(MONTH FROM '" + InvoiceDate + "') IN (1,2,3)   THEN CONCAT(SUBSTRING('" + InvoiceDate + "',3,2)-1 , SUBSTRING('" + InvoiceDate + "',3,2)) END),MODULE_ID =89 WHERE VOUCHER_NO = '" + SDJVNo + "' AND EFFECT ='C' AND MAIN_ACCOUNT_CODE ='132803'", FinanceGlobal.FinURL);

                            // data.Execute("UPDATE FINANCE.D_FIN_VOUCHER_DETAIL SET GRN_NO = '"+SDJVNo+"' ,GRN_DATE= '"+InvoiceDate+"' ,MODULE_ID =89,LINK_NO ='"+RCLINKNo+"',VALUE_DATE = '"+RCValueDate+"',REMARKS='"+InvoiceNo+"',AND INVOICE_NO = ''   AND INVOICE_DATE = '0000-00-00' WHERE VOUCHER_NO = '"+RCJVNo+"' AND AMOUNT ="+SD_AMT+" AND INVOICE_NO = '"+InvoiceNo+"'   AND INVOICE_DATE = '"+InvoiceDate+"'",FinanceGlobal.FinURL);
                            // data.Execute("UPDATE FINANCE.D_FIN_VOUCHER_DETAIL_EX SET GRN_NO = '"+SDJVNo+"' ,GRN_DATE= '"+InvoiceDate+"' ,MODULE_ID =89,LINK_NO ='"+RCLINKNo+"',VALUE_DATE = '"+RCValueDate+"',REMARKS='"+InvoiceNo+"',AND INVOICE_NO = ''   AND INVOICE_DATE = '0000-00-00' WHERE VOUCHER_NO = '"+RCJVNo+"' AND AMOUNT ="+SD_AMT+" AND INVOICE_NO = '"+InvoiceNo+"'   AND INVOICE_DATE = '"+InvoiceDate+"'",FinanceGlobal.FinURL);
                            data.Execute("UPDATE FINANCE.D_FIN_VOUCHER_DETAIL SET GRN_NO = '" + SDJVNo + "' ,GRN_DATE= '" + InvoiceDate + "' ,MODULE_ID =89,LINK_NO ='" + RCLINKNo + "',VALUE_DATE = '" + RCValueDate + "' WHERE VOUCHER_NO = '" + RCJVNo + "' AND AMOUNT =" + SD_AMT + " AND INVOICE_NO = '" + InvoiceNo + "'   AND INVOICE_DATE = '" + InvoiceDate + "'", FinanceGlobal.FinURL);
                            data.Execute("UPDATE FINANCE.D_FIN_VOUCHER_DETAIL_EX SET GRN_NO = '" + SDJVNo + "' ,GRN_DATE= '" + InvoiceDate + "' ,MODULE_ID =89,LINK_NO ='" + RCLINKNo + "',VALUE_DATE = '" + RCValueDate + "' WHERE VOUCHER_NO = '" + RCJVNo + "' AND AMOUNT =" + SD_AMT + " AND INVOICE_NO = '" + InvoiceNo + "'   AND INVOICE_DATE = '" + InvoiceDate + "'", FinanceGlobal.FinURL);

                            data.Execute("UPDATE FINANCE.D_FIN_VOUCHER_DETAIL SET INVOICE_NO = '' ,INVOICE_DATE= '0000-00-00' WHERE VOUCHER_NO = '" + RCJVNo + "' AND AMOUNT =" + SD_AMT + " AND GRN_NO = '" + SDJVNo + "' ", FinanceGlobal.FinURL);
                            data.Execute("UPDATE FINANCE.D_FIN_VOUCHER_DETAIL_EX SET INVOICE_NO = '' ,INVOICE_DATE= '0000-00-00' WHERE VOUCHER_NO = '" + RCJVNo + "' AND AMOUNT =" + SD_AMT + " AND GRN_NO = '" + SDJVNo + "' ", FinanceGlobal.FinURL);

                            //String SDJNo=objVoucher.getAttribute("VOUCHER_NO").getString();
                        }

                    }

                } else {
                    System.out.println("In Sufficienct balance no SD Auto JV will be Posted  ");
                }

                return true;
            } else {
                LastError = objVoucher.LastError;
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    private boolean PostVoucher93(int CompanyID, String InvoiceNo, String InvoiceDate) {
        try {
            clsSalesInvoice objInvoice = (clsSalesInvoice) (new clsSalesInvoice()).getObject(CompanyID, InvoiceNo, InvoiceDate);

            double SD_AMT = objInvoice.getAttribute("SD_AMT").getDouble();
            double NetAmount = objInvoice.getAttribute("NET_AMOUNT").getDouble();
            String PartyCode = objInvoice.getAttribute("PARTY_CODE").getString();

            System.out.println(SD_AMT);

            HashMap List = new HashMap();

            // String ReceiptDate = data.getStringValueFromDB("SELECT RECEIPT_DATE FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            String BookCode = "93";
            /*
             List.clear();
             List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesInterest.ModuleID , "GET_BOOK_CODE", "DEPOSIT_INT_JOURNAL", "");
             if(List.size()>0) {
             //Get the Result of the Rule which would be the hierarchy no.
             clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
             BookCode = objRule.getAttribute("RULE_OUTCOME").getString();
             }
             
             String TDSAccountCode = "";
             
             List.clear();
             List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesInterest.ModuleID , "GET_ACCOUNT_CODE", "TDS_ACCOUNT_CODE", "");
             if(List.size()>0) {
             //Get TDS Account Code
             clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
             TDSAccountCode = objRule.getAttribute("RULE_OUTCOME").getString();
             }
             
             */

            String BankName = data.getStringValueFromDB("SELECT BOOK_NAME FROM D_FIN_BOOK_MASTER WHERE BOOK_CODE='" + BookCode + "' ", FinanceGlobal.FinURL);

            String SelPrefix = "";
            String SelSuffix = "";
            int FFNo = 0;
            /*
             // --- Start Common Elements --- //
             setAttribute("FIN_HIERARCHY_ID",0);
             List.clear();
             List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesInterest.ModuleID , "CHOOSE_HIERARCHY", "DEFAULT", "");
             if(List.size()>0) {
             //Get the Result of the Rule which would be the hierarchy no.
             clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
             int HierarchyID=UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
             setAttribute("FIN_HIERARCHY_ID",HierarchyID);
             }
             
             String InterestExpCode = "";
             List.clear();
             List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesInterest.ModuleID , "GET_ACCOUNT_CODE", "INTEREST_ON_CUSTOMERS_DEPOSIT", "");
             if(List.size()>0) {
             //Get the Result of the Rule which would be the hierarchy no.
             clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
             InterestExpCode=objRule.getAttribute("RULE_OUTCOME").getString().trim();
             }
             
             */

            setAttribute("FIN_HIERARCHY_ID", 0);

            //(1) Select the Hierarchy
            List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsVoucher.JournalVoucherModuleID, "CHOOSE_HIERARCHY", "INVOICE_TYPE", "1");

            if (List.size() > 0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                int HierarchyID = UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                setAttribute("FIN_HIERARCHY_ID", HierarchyID);
            }

            ResultSet rsTmp = data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE PREFIX_CHARS ='JV' AND MODULE_ID=" + clsVoucher.JournalVoucherModuleID);
            rsTmp.first();
            if (rsTmp.getRow() > 0) {
                SelPrefix = rsTmp.getString("PREFIX_CHARS");
                SelSuffix = rsTmp.getString("SUFFIX_CHARS");
                FFNo = rsTmp.getInt("FIRSTFREE_NO");
            }
            // --- End Common Elements --- //

            //--- VOUCHER POSTING FOR BOOK CODE - 24 (i.e. TR - 24) ENTRY NO. 1 - i.e FIRST VOUCHER ---//
            //=======Preparing Voucher Header ============//
            int VoucherSrNo = 0;

            clsVoucher objVoucher = new clsVoucher();
            objVoucher.LoadDataEx(EITLERPGLOBAL.gCompanyID);

            objVoucher.setAttribute("PREFIX", SelPrefix);
            objVoucher.setAttribute("SUFFIX", SelSuffix);
            objVoucher.setAttribute("FFNO", FFNo);
            objVoucher.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            //   objVoucher.setAttribute("VOUCHER_NO","");
            objVoucher.setAttribute("BOOK_CODE", BookCode);
            objVoucher.setAttribute("VOUCHER_TYPE", FinanceGlobal.TYPE_JOURNAL);
            objVoucher.setAttribute("BANK_NAME", BankName);
            objVoucher.setAttribute("CHEQUE_NO", "");
            objVoucher.setAttribute("CHEQUE_DATE", "0000-00-00");
            objVoucher.setAttribute("VOUCHER_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));// interest calc date
            objVoucher.setAttribute("ST_CATEGORY", "");
            objVoucher.setAttribute("MODULE_ID", 0);
            objVoucher.setAttribute("REMARKS", "SECURTY DEPOSIT AMOUNT RS." + SD_AMT + " OF INVOICE NO : " + InvoiceNo + " INVOICE DATE : " + EITLERPGLOBAL.formatDate(InvoiceDate));
            objVoucher.setAttribute("EXCLUDE_IN_ADJ", 1);
            objVoucher.setAttribute("HIERARCHY_ID", getAttribute("FIN_HIERARCHY_ID").getInt());
            int FirstUserID = data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID=" + getAttribute("FIN_HIERARCHY_ID").getInt() + " AND SR_NO=1");
            objVoucher.setAttribute("FROM", FirstUserID);
            objVoucher.setAttribute("TO", FirstUserID);
            objVoucher.setAttribute("FROM_REMARKS", "");
            objVoucher.setAttribute("APPROVAL_STATUS", "F"); //Hold Voucher
            //=======End of  Voucher Header ============//

            // ====== Preparing Voucher Detail =======//
            objVoucher.colVoucherItems.clear();

            VoucherSrNo++;

            clsVoucherItem objVoucherItem = new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT", "C");
            objVoucherItem.setAttribute("ACCOUNT_ID", 1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "132803"); // cd interest transfer - 115160
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", PartyCode);
            objVoucherItem.setAttribute("AMOUNT", SD_AMT);
            objVoucherItem.setAttribute("VALUE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
            objVoucherItem.setAttribute("REMARKS", "");
            objVoucherItem.setAttribute("PO_NO", "");
            objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
            objVoucherItem.setAttribute("REMARKS", "");
            objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
            objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
            objVoucherItem.setAttribute("INVOICE_AMOUNT", NetAmount);
            objVoucherItem.setAttribute("GRN_NO", InvoiceNo);
            objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
            objVoucherItem.setAttribute("MODULE_ID", 89);
            objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);

            VoucherSrNo++;
            objVoucherItem = new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT", "D");
            objVoucherItem.setAttribute("ACCOUNT_ID", 1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "210010"); //Int Main Account Code
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", PartyCode);
            objVoucherItem.setAttribute("AMOUNT", SD_AMT);
            objVoucherItem.setAttribute("VALUE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
            objVoucherItem.setAttribute("PO_NO", "");
            objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
            objVoucherItem.setAttribute("REMARKS", "");
            objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
            objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
            objVoucherItem.setAttribute("INVOICE_AMOUNT", NetAmount);
            objVoucherItem.setAttribute("GRN_NO", InvoiceNo);
            objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
            objVoucherItem.setAttribute("MODULE_ID", 89);
            objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);

            // ====== End of Voucher Detail =======//
            //--- VOUCHER POSTING FOR BOOK CODE - 24 (i.e. TR - 24) ENTRY NO. 1 - i.e FIRST VOUCHER ---//
            System.out.println("sd amt : " + SD_AMT + " 16");
            objVoucher.DoNotValidateAccounts = true;

            if (objVoucher.Insert()) {
                System.out.println("sd amt :  17");

                return true;

            } else {
                System.out.println("sd amt :  18");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean PostSJTypeFilter(int CompanyID, String InvoiceNo, String InvoiceDate, boolean AutoAdj) {
        try {
            //Get Object
            clsSalesInvoice objInvoice = (clsSalesInvoice) (new clsSalesInvoice()).getObject(CompanyID, InvoiceNo, InvoiceDate);

            clsVoucher objVoucher = new clsVoucher();

            double NetAmount = objInvoice.getAttribute("NET_AMOUNT").getDouble();

            //  double HundiCharges=objInvoice.getAttribute("COLUMN_9_AMT").getDouble();
            //  double InsuranceCharges=objInvoice.getAttribute("COLUMN_7_AMT").getDouble();
            double HundiCharges = 0;
            double InsuranceCharges = objInvoice.getAttribute("COLUMN_9_AMT").getDouble();
            double BankCharges = 0;
            //double Kharajat=objInvoice.getAttribute("COLUMN_10_AMT").getDouble()+objInvoice.getAttribute("COLUMN_6_AMT").getDouble();
            double TCCharges = 0;
            //double CashDiscount=objInvoice.getAttribute("COLUMN_11_AMT").getDouble();
            double Cess = objInvoice.getAttribute("COLUMN_3_AMT").getDouble();
            double GSTAmount = 0;
            double VAT1 = objInvoice.getAttribute("VAT1").getDouble();
            double VAT4 = objInvoice.getAttribute("VAT4").getDouble();
            double CST2 = objInvoice.getAttribute("CST2").getDouble();
            double CST5 = objInvoice.getAttribute("CST5").getDouble();

            double CGST = objInvoice.getAttribute("CGST_AMT").getDouble();
            double IGST = objInvoice.getAttribute("IGST_AMT").getDouble();
            double SGST = objInvoice.getAttribute("SGST_AMT").getDouble();

            // NetAmount+=CashDiscount;
            //  double GrossAmount=NetAmount - (HundiCharges+InsuranceCharges+BankCharges+TCCharges+Cess+GSTAmount+VAT1+VAT4+CST2+CST5);
            double GrossAmount = NetAmount - (HundiCharges + InsuranceCharges + BankCharges + TCCharges + Cess + CGST + IGST + SGST);

            String PartyCode = objInvoice.getAttribute("PARTY_CODE").getString();
            //String InvoiceDate=objInvoice.getAttribute("INVOICE_DATE").getString();
            String ICType = objInvoice.getAttribute("IC_TYPE").getString();
            String SalesAccountCode = "";

            String SelPrefix = "";
            String SelSuffix = "";
            int FFNo = 0;

            //****** Prepare Voucher Object ********//
            //(1) Select the Hierarchy
            HashMap List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesInvoice.ModuleID, "CHOOSE_HIERARCHY", "INVOICE_TYPE", "1");

            if (List.size() > 0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                int HierarchyID = UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                setAttribute("FIN_HIERARCHY_ID", HierarchyID);
            }

            ResultSet rsVoucher = data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID=" + clsVoucher.SalesJournalVoucherModuleID);
            rsVoucher.first();

            if (rsVoucher.getRow() > 0) {
                SelPrefix = rsVoucher.getString("PREFIX_CHARS");
                SelSuffix = rsVoucher.getString("SUFFIX_CHARS");
                FFNo = rsVoucher.getInt("FIRSTFREE_NO");
            }

            int VoucherSrNo = 0;

            objVoucher = new clsVoucher();
            objVoucher.LoadDataEx(EITLERPGLOBAL.gCompanyID);

            objVoucher.setAttribute("PREFIX", SelPrefix);
            objVoucher.setAttribute("SUFFIX", SelSuffix);
            objVoucher.setAttribute("FFNO", FFNo);
            objVoucher.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            objVoucher.setAttribute("VOUCHER_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));

            //FELT SALES
            objVoucher.setAttribute("BOOK_CODE", "11");
            SalesAccountCode = "301347";

            objVoucher.setAttribute("VOUCHER_TYPE", FinanceGlobal.TYPE_SALES_JOURNAL);
            objVoucher.setAttribute("CHEQUE_NO", "");
            objVoucher.setAttribute("CHEQUE_DATE", "0000-00-00");
            objVoucher.setAttribute("BANK_NAME", "");
            objVoucher.setAttribute("PO_NO", "");
            objVoucher.setAttribute("PO_DATE", "0000-00-00");
            objVoucher.setAttribute("INVOICE_NO", "");
            objVoucher.setAttribute("INVOICE_DATE", "0000-00-00");
            objVoucher.setAttribute("GRN_NO", "");
            objVoucher.setAttribute("GRN_DATE", "0000-00-00");
            objVoucher.setAttribute("ST_CATEGORY", "");
            objVoucher.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
            objVoucher.setAttribute("REMARKS", "");
            objVoucher.setAttribute("CANCELLED", false);

            objVoucher.setAttribute("HIERARCHY_ID", getAttribute("FIN_HIERARCHY_ID").getInt());
            int FirstUserID = data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID=" + getAttribute("FIN_HIERARCHY_ID").getInt() + " AND SR_NO=1");
            objVoucher.setAttribute("FROM", FirstUserID);
            objVoucher.setAttribute("TO", FirstUserID);
            objVoucher.setAttribute("FROM_REMARKS", "");
            objVoucher.setAttribute("APPROVAL_STATUS", "F"); //Hold Voucher

            objVoucher.colVoucherItems.clear();

            //(1) Entry no.1 => Debit party with net amount
            //String PartyMainCode=clsBook.getBookMainCode(CompanyID, objVoucher.getAttribute("BOOK_CODE").getString());
            String PartyMainCode = "210072";

            //(1) Gross Amount
            if (GrossAmount > 0) {

                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", SalesAccountCode);
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", GrossAmount);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);

            }

            //(2) Hundi Charges
            if (HundiCharges > 0) {

                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "427027");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", HundiCharges);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);

            }

            //(3) Insurance Charges
            if (InsuranceCharges > 0) {

                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "427027");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", InsuranceCharges);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);

            }

            //(4) Bank Charges
            if (BankCharges > 0) {

                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "450038");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", BankCharges);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);

            }

            //(6) TCCharges
            if (TCCharges > 0) {

                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "450045");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", TCCharges);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);

            }

            //(8) Cess
            if (Cess > 0) {

                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127042");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", Cess);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);

            }

            //(9) GST Amount
            if (GSTAmount > 0) {

                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127516");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", GSTAmount);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);

            }

            /*
             if(VAT1>0) {
             VoucherSrNo++;
             clsVoucherItem objVoucherItem=new clsVoucherItem();
             objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
             objVoucherItem.setAttribute("EFFECT","C");
             objVoucherItem.setAttribute("ACCOUNT_ID",1);
             objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","127549");
             objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
             objVoucherItem.setAttribute("AMOUNT",VAT1);
             objVoucherItem.setAttribute("REMARKS","");
             objVoucherItem.setAttribute("PO_NO","");
             objVoucherItem.setAttribute("PO_DATE","0000-00-00");
             objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
             objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
             objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
             objVoucherItem.setAttribute("GRN_NO","");
             objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
             objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
             objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
             objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
             
             }
             
             if(VAT4>0) {
             VoucherSrNo++;
             clsVoucherItem objVoucherItem=new clsVoucherItem();
             objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
             objVoucherItem.setAttribute("EFFECT","C");
             objVoucherItem.setAttribute("ACCOUNT_ID",1);
             objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","127548");
             objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
             objVoucherItem.setAttribute("AMOUNT",VAT4);
             objVoucherItem.setAttribute("REMARKS","");
             objVoucherItem.setAttribute("PO_NO","");
             objVoucherItem.setAttribute("PO_DATE","0000-00-00");
             objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
             objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
             objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
             objVoucherItem.setAttribute("GRN_NO","");
             objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
             objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
             objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
             objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
             
             }
             
             if(CST2>0) {
             VoucherSrNo++;
             clsVoucherItem objVoucherItem=new clsVoucherItem();
             objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
             objVoucherItem.setAttribute("EFFECT","C");
             objVoucherItem.setAttribute("ACCOUNT_ID",1);
             objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","127551");
             objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
             objVoucherItem.setAttribute("AMOUNT",CST2);
             objVoucherItem.setAttribute("REMARKS","");
             objVoucherItem.setAttribute("PO_NO","");
             objVoucherItem.setAttribute("PO_DATE","0000-00-00");
             objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
             objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
             objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
             objVoucherItem.setAttribute("GRN_NO","");
             objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
             objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
             objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
             objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
             
             }
             
             if(CST5>0) {
             VoucherSrNo++;
             clsVoucherItem objVoucherItem=new clsVoucherItem();
             objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
             objVoucherItem.setAttribute("EFFECT","C");
             objVoucherItem.setAttribute("ACCOUNT_ID",1);
             objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","127550");
             objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
             objVoucherItem.setAttribute("AMOUNT",CST5);
             objVoucherItem.setAttribute("REMARKS","");
             objVoucherItem.setAttribute("PO_NO","");
             objVoucherItem.setAttribute("PO_DATE","0000-00-00");
             objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
             objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
             objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
             objVoucherItem.setAttribute("GRN_NO","");
             objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
             objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
             objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
             objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
             
             }
             */
            if (CGST > 0) {

                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127566");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", CGST);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }

            if (SGST > 0) {
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127568");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", SGST);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }

            if (IGST > 0) {
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127570");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", IGST);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
                objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }

            VoucherSrNo++;
            clsVoucherItem objVoucherItem = new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT", "D");
            objVoucherItem.setAttribute("ACCOUNT_ID", 1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", PartyMainCode);
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", PartyCode);
            objVoucherItem.setAttribute("AMOUNT", NetAmount);
            objVoucherItem.setAttribute("REMARKS", "");
            objVoucherItem.setAttribute("PO_NO", "");
            objVoucherItem.setAttribute("PO_DATE", "0000-00-00");
            objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
            objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
            objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
            objVoucherItem.setAttribute("GRN_NO", "");
            objVoucherItem.setAttribute("GRN_DATE", "0000-00-00");
            objVoucherItem.setAttribute("MODULE_ID", clsSalesInvoice.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);

            objVoucher.DoNotValidateAccounts = true;
            if (objVoucher.Insert()) {
                //SJ Posted. Automatically adjust advance receipt amount
                String SJNo = objVoucher.getAttribute("VOUCHER_NO").getString();
                data.Execute("UPDATE D_FIN_VOUCHER_HEADER SET APPROVED=1 AND APPROVED_DATE=CURDATE() WHERE VOUCHER_NO='" + SJNo + "'", FinanceGlobal.FinURL);
                System.out.println("Filter SJ Posted " + SJNo);
                if (AutoAdj) {
                    (new clsDrAdjustment()).AutoAdjustReceiptAmount(EITLERPGLOBAL.gCompanyID, SJNo);
                }
                return true;
            } else {
                LastError = objVoucher.LastError;
                return false;
            }

        } catch (Exception e) {

            return false;
        }

    }

    public static double getTurnoverAmount(String PartyCode, String FromDate, String ToDate) {
        try {
            double NetAmount = data.getDoubleValueFromDB("SELECT SUM(NET_AMOUNT) AS TOTAL_AMOUNT FROM D_SAL_INVOICE_HEADER WHERE PARTY_CODE='" + PartyCode + "' AND INVOICE_DATE>='" + FromDate + "' AND INVOICE_DATE<='" + ToDate + "'");

            return NetAmount;
        } catch (Exception e) {
            return 0;
        }
    }

    //**********************************************************************************************************************//
    //************************************** BACKUP OF ORIGINAL VOUCHER POSTING ROUTINES ***********************************//
    //**********************************************************************************************************************//
    //    public boolean PostSJTypeSuiting(int CompanyID,String InvoiceNo) {
    //        try {
    //            //Get Object
    //            clsSalesInvoice objInvoice=(clsSalesInvoice)(new clsSalesInvoice()).getObject(CompanyID,InvoiceNo);
    //
    //            clsVoucher objVoucher=new clsVoucher();
    //
    //            double NetAmount=objInvoice.getAttribute("NET_AMOUNT").getDouble();
    //
    //            double HundiCharges=objInvoice.getAttribute("COLUMN_8_AMT").getDouble();
    //            double InsuranceCharges=objInvoice.getAttribute("COLUMN_9_AMT").getDouble();
    //            double BankCharges=objInvoice.getAttribute("COLUMN_10_AMT").getDouble();
    //            double OtherCharges=objInvoice.getAttribute("COLUMN_12_AMT").getDouble();
    //            double TCCharges=objInvoice.getAttribute("COLUMN_7_AMT").getDouble();
    //            double CashDiscount=objInvoice.getAttribute("COLUMN_19_AMT").getDouble();
    //            double Cess=0;
    //            double GSTAmount=objInvoice.getAttribute("COLUMN_14_AMT").getDouble();
    //            double RoundOff=EITLERPGLOBAL.round((HundiCharges+InsuranceCharges+BankCharges+OtherCharges+TCCharges+CashDiscount+Cess+GSTAmount)-NetAmount,2);
    //
    //            NetAmount+=CashDiscount;
    //            double GrossAmount=NetAmount-(HundiCharges+InsuranceCharges+BankCharges+OtherCharges+TCCharges+Cess+GSTAmount);
    //
    //            String PartyCode=objInvoice.getAttribute("PARTY_CODE").getString();
    //            String InvoiceDate=objInvoice.getAttribute("INVOICE_DATE").getString();
    //            String ICType=objInvoice.getAttribute("IC_TYPE").getString();
    //            String SalesAccountCode="";
    //
    //            String SelPrefix="";
    //            String SelSuffix="";
    //            int FFNo=0;
    //
    //
    //            //****** Prepare Voucher Object ********//
    //
    //            setAttribute("FIN_HIERARCHY_ID",0);
    //
    //            //(1) Select the Hierarchy
    //            HashMap List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesInvoice.ModuleID, "CHOOSE_HIERARCHY", "INVOICE_TYPE", "1");
    //
    //            if(List.size()>0) {
    //                //Get the Result of the Rule which would be the hierarchy no.
    //                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
    //                int HierarchyID=UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
    //                setAttribute("FIN_HIERARCHY_ID",HierarchyID);
    //            }
    //
    //
    //            ResultSet rsVoucher=data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID="+clsVoucher.SalesJournalVoucherModuleID);
    //            rsVoucher.first();
    //
    //            if(rsVoucher.getRow()>0) {
    //                SelPrefix=rsVoucher.getString("PREFIX_CHARS");
    //                SelSuffix=rsVoucher.getString("SUFFIX_CHARS");
    //                FFNo=rsVoucher.getInt("FIRSTFREE_NO");
    //            }
    //
    //
    //            int VoucherSrNo=0;
    //
    //            objVoucher=new clsVoucher();
    //            objVoucher.LoadDataEx(EITLERPGLOBAL.gCompanyID);
    //
    //            objVoucher.setAttribute("PREFIX",SelPrefix);
    //            objVoucher.setAttribute("SUFFIX",SelSuffix);
    //            objVoucher.setAttribute("FFNO",FFNo);
    //            objVoucher.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
    //            objVoucher.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //
    //            //SUITING
    //            objVoucher.setAttribute("BOOK_CODE","01");
    //            SalesAccountCode="301017";
    //
    //            if(ICType.equals("1")) {
    //                objVoucher.setAttribute("BOOK_CODE","01");
    //                SalesAccountCode="301017";
    //            }
    //
    //            //FILTER FABRICS
    //            if(ICType.equals("4")) {
    //                objVoucher.setAttribute("BOOK_CODE","11");
    //                SalesAccountCode="301347";
    //            }
    //
    //            //BLANKET SALES
    //            if(ICType.equals("2")) {
    //                objVoucher.setAttribute("BOOK_CODE","01");
    //                SalesAccountCode="301079";
    //            }
    //
    //            //SHAWLS SALES
    //            if(ICType.equals("3")) {
    //                objVoucher.setAttribute("BOOK_CODE","01");
    //                SalesAccountCode="301134";
    //            }
    //
    //
    //            objVoucher.setAttribute("VOUCHER_TYPE",FinanceGlobal.TYPE_SALES_JOURNAL);
    //            objVoucher.setAttribute("CHEQUE_NO","");
    //            objVoucher.setAttribute("CHEQUE_DATE","0000-00-00");
    //            objVoucher.setAttribute("BANK_NAME","");
    //            objVoucher.setAttribute("PO_NO","");
    //            objVoucher.setAttribute("PO_DATE","0000-00-00");
    //            objVoucher.setAttribute("INVOICE_NO","");
    //            objVoucher.setAttribute("INVOICE_DATE","0000-00-00");
    //            objVoucher.setAttribute("GRN_NO","");
    //            objVoucher.setAttribute("GRN_DATE","0000-00-00");
    //            objVoucher.setAttribute("ST_CATEGORY","");
    //            objVoucher.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
    //            objVoucher.setAttribute("REMARKS","");
    //
    //            objVoucher.setAttribute("HIERARCHY_ID",getAttribute("FIN_HIERARCHY_ID").getInt());
    //            int FirstUserID=data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID="+getAttribute("FIN_HIERARCHY_ID").getInt()+" AND SR_NO=1");
    //            objVoucher.setAttribute("FROM",FirstUserID);
    //            objVoucher.setAttribute("TO",FirstUserID);
    //            objVoucher.setAttribute("FROM_REMARKS","");
    //            objVoucher.setAttribute("APPROVAL_STATUS","F"); //Final Approve
    //
    //            objVoucher.colVoucherItems.clear();
    //
    //
    //            //(1) Entry no.1 => Debit party with net amount
    //            //String PartyMainCode=clsBook.getBookMainCode(CompanyID, objVoucher.getAttribute("BOOK_CODE").getString());
    //            String PartyMainCode="210027";
    //
    //
    //            //(1) Sales Account
    //            if(GrossAmount>0) {
    //                VoucherSrNo++;
    //                clsVoucherItem objVoucherItem=new clsVoucherItem();
    //                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
    //                objVoucherItem.setAttribute("EFFECT","C");
    //                objVoucherItem.setAttribute("ACCOUNT_ID",1);
    //                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",SalesAccountCode);
    //                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
    //                objVoucherItem.setAttribute("AMOUNT",GrossAmount);
    //                objVoucherItem.setAttribute("REMARKS","");
    //                objVoucherItem.setAttribute("PO_NO","");
    //                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
    //                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
    //                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
    //            }
    //
    //
    //            //(2) Hundi Charges
    //            if(HundiCharges>0) {
    //
    //                VoucherSrNo++;
    //                clsVoucherItem objVoucherItem=new clsVoucherItem();
    //                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
    //                objVoucherItem.setAttribute("EFFECT","C");
    //                objVoucherItem.setAttribute("ACCOUNT_ID",1);
    //                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","450227");
    //                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
    //                objVoucherItem.setAttribute("AMOUNT",HundiCharges);
    //                objVoucherItem.setAttribute("REMARKS","");
    //                objVoucherItem.setAttribute("PO_NO","");
    //                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
    //                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
    //                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
    //
    //            }
    //
    //
    //            //(3) Insurance Charges
    //            if(InsuranceCharges>0) {
    //
    //                VoucherSrNo++;
    //                clsVoucherItem objVoucherItem=new clsVoucherItem();
    //                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
    //                objVoucherItem.setAttribute("EFFECT","C");
    //                objVoucherItem.setAttribute("ACCOUNT_ID",1);
    //                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","427027");
    //                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
    //                objVoucherItem.setAttribute("AMOUNT",InsuranceCharges);
    //                objVoucherItem.setAttribute("REMARKS","");
    //                objVoucherItem.setAttribute("PO_NO","");
    //                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
    //                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
    //                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
    //
    //            }
    //
    //
    //            //(4) Bank Charges
    //            if(BankCharges>0) {
    //
    //                VoucherSrNo++;
    //                clsVoucherItem objVoucherItem=new clsVoucherItem();
    //                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
    //                objVoucherItem.setAttribute("EFFECT","C");
    //                objVoucherItem.setAttribute("ACCOUNT_ID",1);
    //                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","450038");
    //                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
    //                objVoucherItem.setAttribute("AMOUNT",BankCharges);
    //                objVoucherItem.setAttribute("REMARKS","");
    //                objVoucherItem.setAttribute("PO_NO","");
    //                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
    //                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
    //                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
    //
    //            }
    //
    //
    //            //(5) Other Charges
    //            if(OtherCharges>0) {
    //
    //                VoucherSrNo++;
    //                clsVoucherItem objVoucherItem=new clsVoucherItem();
    //                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
    //                objVoucherItem.setAttribute("EFFECT","C");
    //                objVoucherItem.setAttribute("ACCOUNT_ID",1);
    //                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","450320");
    //                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
    //                objVoucherItem.setAttribute("AMOUNT",OtherCharges);
    //                objVoucherItem.setAttribute("REMARKS","");
    //                objVoucherItem.setAttribute("PO_NO","");
    //                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
    //                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
    //                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
    //
    //            }
    //
    //            //(6) TCCharges
    //            if(TCCharges>0) {
    //
    //                VoucherSrNo++;
    //                clsVoucherItem objVoucherItem=new clsVoucherItem();
    //                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
    //                objVoucherItem.setAttribute("EFFECT","C");
    //                objVoucherItem.setAttribute("ACCOUNT_ID",1);
    //                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","450045");
    //                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
    //                objVoucherItem.setAttribute("AMOUNT",TCCharges);
    //                objVoucherItem.setAttribute("REMARKS","");
    //                objVoucherItem.setAttribute("PO_NO","");
    //                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
    //                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
    //                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
    //
    //            }
    //
    //
    //            //(8) Cess
    //            if(Cess>0) {
    //
    //                VoucherSrNo++;
    //                clsVoucherItem objVoucherItem=new clsVoucherItem();
    //                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
    //                objVoucherItem.setAttribute("EFFECT","C");
    //                objVoucherItem.setAttribute("ACCOUNT_ID",1);
    //                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","127042");
    //                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
    //                objVoucherItem.setAttribute("AMOUNT",Cess);
    //                objVoucherItem.setAttribute("REMARKS","");
    //                objVoucherItem.setAttribute("PO_NO","");
    //                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
    //                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
    //                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
    //
    //            }
    //
    //            //(9) GST Amount
    //
    //            if(GSTAmount>0) {
    //
    //                VoucherSrNo++;
    //                clsVoucherItem objVoucherItem=new clsVoucherItem();
    //                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
    //                objVoucherItem.setAttribute("EFFECT","C");
    //                objVoucherItem.setAttribute("ACCOUNT_ID",1);
    //                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","127516");
    //                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
    //                objVoucherItem.setAttribute("AMOUNT",GSTAmount);
    //                objVoucherItem.setAttribute("REMARKS","");
    //                objVoucherItem.setAttribute("PO_NO","");
    //                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
    //                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
    //                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
    //
    //            }
    //
    //
    //            VoucherSrNo++;
    //            clsVoucherItem objVoucherItem=new clsVoucherItem();
    //            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
    //            objVoucherItem.setAttribute("EFFECT","D");
    //            objVoucherItem.setAttribute("ACCOUNT_ID",1);
    //            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",PartyMainCode);
    //            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",PartyCode);
    //            objVoucherItem.setAttribute("AMOUNT",NetAmount);
    //            objVoucherItem.setAttribute("REMARKS","");
    //            objVoucherItem.setAttribute("PO_NO","");
    //            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
    //            objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //            objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //            objVoucherItem.setAttribute("GRN_NO","");
    //            objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
    //            objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
    //            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
    //            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
    //
    //            objVoucher.DoNotValidateAccounts=true;
    //
    //            if(objVoucher.Insert()) {
    //
    //                //SJ Posted. Automatically adjust advance receipt amount
    //                String SJNo=objVoucher.getAttribute("VOUCHER_NO").getString();
    //                //(new clsDrAdjustment()).AutoAdjustReceiptAmount(EITLERPGLOBAL.gCompanyID,SJNo);
    //
    //                return true;
    //            }
    //            else {
    //                LastError=objVoucher.LastError;
    //                return false;
    //            }
    //
    //        }
    //        catch(Exception e) {
    //            e.printStackTrace();
    //            return false;
    //        }
    //
    //
    //    }
    //    public boolean PostSJTypeFelt(int CompanyID,String InvoiceNo) {
    //        try {
    //            //Get Object
    //            clsSalesInvoice objInvoice=(clsSalesInvoice)(new clsSalesInvoice()).getObject(CompanyID,InvoiceNo);
    //
    //            clsVoucher objVoucher=new clsVoucher();
    //
    //            double NetAmount=objInvoice.getAttribute("NET_AMOUNT").getDouble();
    //
    //            double HundiCharges=objInvoice.getAttribute("COLUMN_8_AMT").getDouble();
    //            double InsuranceCharges=objInvoice.getAttribute("COLUMN_9_AMT").getDouble();
    //            double BankCharges=objInvoice.getAttribute("COLUMN_10_AMT").getDouble();
    //            double OtherCharges=objInvoice.getAttribute("COLUMN_12_AMT").getDouble();
    //            double TCCharges=objInvoice.getAttribute("COLUMN_7_AMT").getDouble();
    //            double CashDiscount=objInvoice.getAttribute("COLUMN_19_AMT").getDouble();
    //            double Cess=0;
    //            double GSTAmount=objInvoice.getAttribute("COLUMN_14_AMT").getDouble();
    //            double RoundOff=EITLERPGLOBAL.round((HundiCharges+InsuranceCharges+BankCharges+OtherCharges+TCCharges+CashDiscount+Cess+GSTAmount)-NetAmount,2);
    //
    //            NetAmount+=CashDiscount;
    //            double GrossAmount=NetAmount-(HundiCharges+InsuranceCharges+BankCharges+OtherCharges+TCCharges+Cess+GSTAmount);
    //
    //            String PartyCode=objInvoice.getAttribute("PARTY_CODE").getString();
    //            String InvoiceDate=objInvoice.getAttribute("INVOICE_DATE").getString();
    //            String ICType=objInvoice.getAttribute("IC_TYPE").getString();
    //            String SalesAccountCode="";
    //
    //            String SelPrefix="";
    //            String SelSuffix="";
    //            int FFNo=0;
    //
    //
    //            //****** Prepare Voucher Object ********//
    //
    //            setAttribute("FIN_HIERARCHY_ID",0);
    //
    //            //(1) Select the Hierarchy
    //            HashMap List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesInvoice.ModuleID, "CHOOSE_HIERARCHY", "INVOICE_TYPE", "1");
    //
    //            if(List.size()>0) {
    //                //Get the Result of the Rule which would be the hierarchy no.
    //                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
    //                int HierarchyID=UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
    //                setAttribute("FIN_HIERARCHY_ID",HierarchyID);
    //            }
    //
    //
    //            ResultSet rsVoucher=data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID="+clsVoucher.SalesJournalVoucherModuleID);
    //            rsVoucher.first();
    //
    //            if(rsVoucher.getRow()>0) {
    //                SelPrefix=rsVoucher.getString("PREFIX_CHARS");
    //                SelSuffix=rsVoucher.getString("SUFFIX_CHARS");
    //                FFNo=rsVoucher.getInt("FIRSTFREE_NO");
    //            }
    //
    //
    //            int VoucherSrNo=0;
    //
    //            objVoucher=new clsVoucher();
    //            objVoucher.LoadDataEx(EITLERPGLOBAL.gCompanyID);
    //
    //            objVoucher.setAttribute("PREFIX",SelPrefix);
    //            objVoucher.setAttribute("SUFFIX",SelSuffix);
    //            objVoucher.setAttribute("FFNO",FFNo);
    //            objVoucher.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
    //            objVoucher.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //
    //            //SUITING
    //            objVoucher.setAttribute("BOOK_CODE","09");
    //            SalesAccountCode="301378";
    //
    //            if(ICType.equals("1")) {
    //                objVoucher.setAttribute("BOOK_CODE","01");
    //                SalesAccountCode="301378";
    //            }
    //
    //            //FILTER FABRICS
    //            if(ICType.equals("4")) {
    //                objVoucher.setAttribute("BOOK_CODE","11");
    //                SalesAccountCode="301347";
    //            }
    //
    //            //BLANKET SALES
    //            if(ICType.equals("2")) {
    //                objVoucher.setAttribute("BOOK_CODE","01");
    //                SalesAccountCode="301079";
    //            }
    //
    //            //SHAWLS SALES
    //            if(ICType.equals("3")) {
    //                objVoucher.setAttribute("BOOK_CODE","01");
    //                SalesAccountCode="301347";
    //            }
    //
    //
    //            objVoucher.setAttribute("VOUCHER_TYPE",FinanceGlobal.TYPE_SALES_JOURNAL);
    //            objVoucher.setAttribute("CHEQUE_NO","");
    //            objVoucher.setAttribute("CHEQUE_DATE","0000-00-00");
    //            objVoucher.setAttribute("BANK_NAME","");
    //            objVoucher.setAttribute("PO_NO","");
    //            objVoucher.setAttribute("PO_DATE","0000-00-00");
    //            objVoucher.setAttribute("INVOICE_NO","");
    //            objVoucher.setAttribute("INVOICE_DATE","0000-00-00");
    //            objVoucher.setAttribute("GRN_NO","");
    //            objVoucher.setAttribute("GRN_DATE","0000-00-00");
    //            objVoucher.setAttribute("ST_CATEGORY","");
    //            objVoucher.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
    //            objVoucher.setAttribute("REMARKS","");
    //
    //            objVoucher.setAttribute("HIERARCHY_ID",getAttribute("FIN_HIERARCHY_ID").getInt());
    //            int FirstUserID=data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID="+getAttribute("FIN_HIERARCHY_ID").getInt()+" AND SR_NO=1");
    //            objVoucher.setAttribute("FROM",FirstUserID);
    //            objVoucher.setAttribute("TO",FirstUserID);
    //            objVoucher.setAttribute("FROM_REMARKS","");
    //            objVoucher.setAttribute("APPROVAL_STATUS","F"); //Hold Voucher
    //
    //            objVoucher.colVoucherItems.clear();
    //
    //
    //            //(1) Entry no.1 => Debit party with net amount
    //            //String PartyMainCode=clsBook.getBookMainCode(CompanyID, objVoucher.getAttribute("BOOK_CODE").getString());
    //            String PartyMainCode="210010";
    //
    //
    //            //(1) Sales Account
    //            if(GrossAmount>0) {
    //                VoucherSrNo++;
    //                clsVoucherItem objVoucherItem=new clsVoucherItem();
    //                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
    //                objVoucherItem.setAttribute("EFFECT","C");
    //                objVoucherItem.setAttribute("ACCOUNT_ID",1);
    //                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",SalesAccountCode);
    //                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
    //                objVoucherItem.setAttribute("AMOUNT",GrossAmount);
    //                objVoucherItem.setAttribute("REMARKS","");
    //                objVoucherItem.setAttribute("PO_NO","");
    //                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
    //                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
    //                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
    //            }
    //
    //
    //            //(2) Hundi Charges
    //            if(HundiCharges>0) {
    //
    //                VoucherSrNo++;
    //                clsVoucherItem objVoucherItem=new clsVoucherItem();
    //                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
    //                objVoucherItem.setAttribute("EFFECT","C");
    //                objVoucherItem.setAttribute("ACCOUNT_ID",1);
    //                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","301378");
    //                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
    //                objVoucherItem.setAttribute("AMOUNT",HundiCharges);
    //                objVoucherItem.setAttribute("REMARKS","");
    //                objVoucherItem.setAttribute("PO_NO","");
    //                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
    //                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
    //                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
    //
    //            }
    //
    //
    //            //(3) Insurance Charges
    //            if(InsuranceCharges>0) {
    //
    //                VoucherSrNo++;
    //                clsVoucherItem objVoucherItem=new clsVoucherItem();
    //                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
    //                objVoucherItem.setAttribute("EFFECT","C");
    //                objVoucherItem.setAttribute("ACCOUNT_ID",1);
    //                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","427027");
    //                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
    //                objVoucherItem.setAttribute("AMOUNT",InsuranceCharges);
    //                objVoucherItem.setAttribute("REMARKS","");
    //                objVoucherItem.setAttribute("PO_NO","");
    //                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
    //                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
    //                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
    //
    //            }
    //
    //
    //            //(4) Bank Charges
    //            if(BankCharges>0) {
    //
    //                VoucherSrNo++;
    //                clsVoucherItem objVoucherItem=new clsVoucherItem();
    //                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
    //                objVoucherItem.setAttribute("EFFECT","C");
    //                objVoucherItem.setAttribute("ACCOUNT_ID",1);
    //                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","450038");
    //                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
    //                objVoucherItem.setAttribute("AMOUNT",BankCharges);
    //                objVoucherItem.setAttribute("REMARKS","");
    //                objVoucherItem.setAttribute("PO_NO","");
    //                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
    //                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
    //                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
    //
    //            }
    //
    //
    //            //(5) Other Charges
    //            if(OtherCharges>0) {
    //
    //                VoucherSrNo++;
    //                clsVoucherItem objVoucherItem=new clsVoucherItem();
    //                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
    //                objVoucherItem.setAttribute("EFFECT","C");
    //                objVoucherItem.setAttribute("ACCOUNT_ID",1);
    //                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","301378");
    //                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
    //                objVoucherItem.setAttribute("AMOUNT",OtherCharges);
    //                objVoucherItem.setAttribute("REMARKS","");
    //                objVoucherItem.setAttribute("PO_NO","");
    //                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
    //                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
    //                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
    //
    //            }
    //
    //            //(6) TCCharges
    //            if(TCCharges>0) {
    //
    //                VoucherSrNo++;
    //                clsVoucherItem objVoucherItem=new clsVoucherItem();
    //                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
    //                objVoucherItem.setAttribute("EFFECT","C");
    //                objVoucherItem.setAttribute("ACCOUNT_ID",1);
    //                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","427027");
    //                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
    //                objVoucherItem.setAttribute("AMOUNT",TCCharges);
    //                objVoucherItem.setAttribute("REMARKS","");
    //                objVoucherItem.setAttribute("PO_NO","");
    //                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
    //                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
    //                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
    //
    //            }
    //
    //
    //            //(8) Cess
    //            if(Cess>0) {
    //
    //                VoucherSrNo++;
    //                clsVoucherItem objVoucherItem=new clsVoucherItem();
    //                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
    //                objVoucherItem.setAttribute("EFFECT","C");
    //                objVoucherItem.setAttribute("ACCOUNT_ID",1);
    //                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","127042");
    //                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
    //                objVoucherItem.setAttribute("AMOUNT",Cess);
    //                objVoucherItem.setAttribute("REMARKS","");
    //                objVoucherItem.setAttribute("PO_NO","");
    //                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
    //                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
    //                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
    //
    //            }
    //
    //            //(9) GST Amount
    //
    //            if(GSTAmount>0) {
    //
    //                VoucherSrNo++;
    //                clsVoucherItem objVoucherItem=new clsVoucherItem();
    //                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
    //                objVoucherItem.setAttribute("EFFECT","C");
    //                objVoucherItem.setAttribute("ACCOUNT_ID",1);
    //                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","127516");
    //                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
    //                objVoucherItem.setAttribute("AMOUNT",GSTAmount);
    //                objVoucherItem.setAttribute("REMARKS","");
    //                objVoucherItem.setAttribute("PO_NO","");
    //                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
    //                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
    //                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
    //
    //            }
    //
    //
    //            VoucherSrNo++;
    //            clsVoucherItem objVoucherItem=new clsVoucherItem();
    //            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
    //            objVoucherItem.setAttribute("EFFECT","D");
    //            objVoucherItem.setAttribute("ACCOUNT_ID",1);
    //            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",PartyMainCode);
    //            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",PartyCode);
    //            objVoucherItem.setAttribute("AMOUNT",NetAmount);
    //            objVoucherItem.setAttribute("REMARKS","");
    //            objVoucherItem.setAttribute("PO_NO","");
    //            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
    //            objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //            objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //            objVoucherItem.setAttribute("GRN_NO","");
    //            objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
    //            objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
    //            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
    //            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
    //
    //            objVoucher.DoNotValidateAccounts=true;
    //
    //            if(objVoucher.Insert()) {
    //
    //                //SJ Posted. Automatically adjust advance receipt amount
    //                String SJNo=objVoucher.getAttribute("VOUCHER_NO").getString();
    //                //(new clsDrAdjustment()).AutoAdjustReceiptAmount(EITLERPGLOBAL.gCompanyID,SJNo);
    //
    //                return true;
    //            }
    //            else {
    //                LastError=objVoucher.LastError;
    //                return false;
    //            }
    //
    //        }
    //        catch(Exception e) {
    //            e.printStackTrace();
    //            return false;
    //        }
    //
    //
    //    }
    //    public boolean PostSJTypeFilter(int CompanyID,String InvoiceNo) {
    //        try {
    //            //Get Object
    //            clsSalesInvoice objInvoice=(clsSalesInvoice)(new clsSalesInvoice()).getObject(CompanyID,InvoiceNo);
    //
    //            clsVoucher objVoucher=new clsVoucher();
    //
    //            double NetAmount=objInvoice.getAttribute("NET_AMOUNT").getDouble();
    //
    //            double HundiCharges=objInvoice.getAttribute("COLUMN_9_AMT").getDouble();
    //            double InsuranceCharges=objInvoice.getAttribute("COLUMN_7_AMT").getDouble();
    //            double BankCharges=0;
    //            double Kharajat=objInvoice.getAttribute("COLUMN_10_AMT").getDouble()+objInvoice.getAttribute("COLUMN_6_AMT").getDouble();
    //            double TCCharges=0;
    //            double CashDiscount=objInvoice.getAttribute("COLUMN_11_AMT").getDouble();
    //            double Cess=objInvoice.getAttribute("COLUMN_3_AMT").getDouble();
    //            double GSTAmount=0;
    //
    //            NetAmount+=CashDiscount;
    //            double GrossAmount=NetAmount - (HundiCharges+InsuranceCharges+BankCharges+Kharajat+TCCharges+Cess+GSTAmount);
    //
    //            String PartyCode=objInvoice.getAttribute("PARTY_CODE").getString();
    //            String InvoiceDate=objInvoice.getAttribute("INVOICE_DATE").getString();
    //            String ICType=objInvoice.getAttribute("IC_TYPE").getString();
    //            String SalesAccountCode="";
    //
    //            String SelPrefix="";
    //            String SelSuffix="";
    //            int FFNo=0;
    //
    //
    //            //****** Prepare Voucher Object ********//
    //
    //            //(1) Select the Hierarchy
    //            HashMap List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesInvoice.ModuleID, "CHOOSE_HIERARCHY", "INVOICE_TYPE", "1");
    //
    //            if(List.size()>0) {
    //                //Get the Result of the Rule which would be the hierarchy no.
    //                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
    //                int HierarchyID=UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
    //                setAttribute("FIN_HIERARCHY_ID",HierarchyID);
    //            }
    //
    //
    //            ResultSet rsVoucher=data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID="+clsVoucher.SalesJournalVoucherModuleID);
    //            rsVoucher.first();
    //
    //            if(rsVoucher.getRow()>0) {
    //                SelPrefix=rsVoucher.getString("PREFIX_CHARS");
    //                SelSuffix=rsVoucher.getString("SUFFIX_CHARS");
    //                FFNo=rsVoucher.getInt("FIRSTFREE_NO");
    //            }
    //
    //
    //            int VoucherSrNo=0;
    //
    //            objVoucher=new clsVoucher();
    //            objVoucher.LoadDataEx(EITLERPGLOBAL.gCompanyID);
    //
    //            objVoucher.setAttribute("PREFIX",SelPrefix);
    //            objVoucher.setAttribute("SUFFIX",SelSuffix);
    //            objVoucher.setAttribute("FFNO",FFNo);
    //            objVoucher.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
    //            objVoucher.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //
    //            //FELT SALES
    //            objVoucher.setAttribute("BOOK_CODE","11");
    //            SalesAccountCode="301347";
    //
    //            objVoucher.setAttribute("VOUCHER_TYPE",FinanceGlobal.TYPE_SALES_JOURNAL);
    //            objVoucher.setAttribute("CHEQUE_NO","");
    //            objVoucher.setAttribute("CHEQUE_DATE","0000-00-00");
    //            objVoucher.setAttribute("BANK_NAME","");
    //            objVoucher.setAttribute("PO_NO","");
    //            objVoucher.setAttribute("PO_DATE","0000-00-00");
    //            objVoucher.setAttribute("INVOICE_NO","");
    //            objVoucher.setAttribute("INVOICE_DATE","0000-00-00");
    //            objVoucher.setAttribute("GRN_NO","");
    //            objVoucher.setAttribute("GRN_DATE","0000-00-00");
    //            objVoucher.setAttribute("ST_CATEGORY","");
    //            objVoucher.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
    //            objVoucher.setAttribute("REMARKS","");
    //            objVoucher.setAttribute("CANCELLED",false);
    //
    //            objVoucher.setAttribute("HIERARCHY_ID",getAttribute("FIN_HIERARCHY_ID").getInt());
    //            int FirstUserID=data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID="+getAttribute("FIN_HIERARCHY_ID").getInt()+" AND SR_NO=1");
    //            objVoucher.setAttribute("FROM",FirstUserID);
    //            objVoucher.setAttribute("TO",FirstUserID);
    //            objVoucher.setAttribute("FROM_REMARKS","");
    //            objVoucher.setAttribute("APPROVAL_STATUS","F"); //Hold Voucher
    //
    //            objVoucher.colVoucherItems.clear();
    //
    //
    //            //(1) Entry no.1 => Debit party with net amount
    //            //String PartyMainCode=clsBook.getBookMainCode(CompanyID, objVoucher.getAttribute("BOOK_CODE").getString());
    //
    //            String PartyMainCode="210072";
    //
    //            //(1) Gross Amount
    //            if(GrossAmount>0) {
    //
    //                VoucherSrNo++;
    //                clsVoucherItem objVoucherItem=new clsVoucherItem();
    //                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
    //                objVoucherItem.setAttribute("EFFECT","C");
    //                objVoucherItem.setAttribute("ACCOUNT_ID",1);
    //                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",SalesAccountCode);
    //                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
    //                objVoucherItem.setAttribute("AMOUNT",GrossAmount);
    //                objVoucherItem.setAttribute("REMARKS","");
    //                objVoucherItem.setAttribute("PO_NO","");
    //                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
    //                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
    //                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
    //
    //            }
    //
    //
    //            //(2) Hundi Charges
    //            if(HundiCharges>0) {
    //
    //                VoucherSrNo++;
    //                clsVoucherItem objVoucherItem=new clsVoucherItem();
    //                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
    //                objVoucherItem.setAttribute("EFFECT","C");
    //                objVoucherItem.setAttribute("ACCOUNT_ID",1);
    //                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","450227");
    //                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
    //                objVoucherItem.setAttribute("AMOUNT",HundiCharges);
    //                objVoucherItem.setAttribute("REMARKS","");
    //                objVoucherItem.setAttribute("PO_NO","");
    //                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
    //                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
    //                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
    //
    //            }
    //
    //
    //            //(3) Insurance Charges
    //            if(InsuranceCharges>0) {
    //
    //                VoucherSrNo++;
    //                clsVoucherItem objVoucherItem=new clsVoucherItem();
    //                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
    //                objVoucherItem.setAttribute("EFFECT","C");
    //                objVoucherItem.setAttribute("ACCOUNT_ID",1);
    //                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","427027");
    //                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
    //                objVoucherItem.setAttribute("AMOUNT",InsuranceCharges);
    //                objVoucherItem.setAttribute("REMARKS","");
    //                objVoucherItem.setAttribute("PO_NO","");
    //                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
    //                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
    //                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
    //
    //            }
    //
    //
    //            //(4) Bank Charges
    //            if(BankCharges>0) {
    //
    //                VoucherSrNo++;
    //                clsVoucherItem objVoucherItem=new clsVoucherItem();
    //                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
    //                objVoucherItem.setAttribute("EFFECT","C");
    //                objVoucherItem.setAttribute("ACCOUNT_ID",1);
    //                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","450038");
    //                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
    //                objVoucherItem.setAttribute("AMOUNT",BankCharges);
    //                objVoucherItem.setAttribute("REMARKS","");
    //                objVoucherItem.setAttribute("PO_NO","");
    //                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
    //                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
    //                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
    //
    //            }
    //
    //
    //            //(5) Other Charges
    //            if(Kharajat>0) {
    //
    //                VoucherSrNo++;
    //                clsVoucherItem objVoucherItem=new clsVoucherItem();
    //                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
    //                objVoucherItem.setAttribute("EFFECT","C");
    //                objVoucherItem.setAttribute("ACCOUNT_ID",1);
    //                //objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","409025");
    //                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","301347");
    //                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
    //                objVoucherItem.setAttribute("AMOUNT",Kharajat);
    //                objVoucherItem.setAttribute("REMARKS","");
    //                objVoucherItem.setAttribute("PO_NO","");
    //                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
    //                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
    //                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
    //
    //            }
    //
    //            //(6) TCCharges
    //            if(TCCharges>0) {
    //
    //                VoucherSrNo++;
    //                clsVoucherItem objVoucherItem=new clsVoucherItem();
    //                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
    //                objVoucherItem.setAttribute("EFFECT","C");
    //                objVoucherItem.setAttribute("ACCOUNT_ID",1);
    //                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","450045");
    //                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
    //                objVoucherItem.setAttribute("AMOUNT",TCCharges);
    //                objVoucherItem.setAttribute("REMARKS","");
    //                objVoucherItem.setAttribute("PO_NO","");
    //                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
    //                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
    //                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
    //
    //            }
    //
    //
    //            //(8) Cess
    //            if(Cess>0) {
    //
    //                VoucherSrNo++;
    //                clsVoucherItem objVoucherItem=new clsVoucherItem();
    //                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
    //                objVoucherItem.setAttribute("EFFECT","C");
    //                objVoucherItem.setAttribute("ACCOUNT_ID",1);
    //                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","127042");
    //                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
    //                objVoucherItem.setAttribute("AMOUNT",Cess);
    //                objVoucherItem.setAttribute("REMARKS","");
    //                objVoucherItem.setAttribute("PO_NO","");
    //                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
    //                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
    //                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
    //
    //            }
    //
    //            //(9) GST Amount
    //
    //            if(GSTAmount>0) {
    //
    //                VoucherSrNo++;
    //                clsVoucherItem objVoucherItem=new clsVoucherItem();
    //                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
    //                objVoucherItem.setAttribute("EFFECT","C");
    //                objVoucherItem.setAttribute("ACCOUNT_ID",1);
    //                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","127516");
    //                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
    //                objVoucherItem.setAttribute("AMOUNT",GSTAmount);
    //                objVoucherItem.setAttribute("REMARKS","");
    //                objVoucherItem.setAttribute("PO_NO","");
    //                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
    //                objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
    //                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
    //                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
    //
    //            }
    //
    //
    //            VoucherSrNo++;
    //            clsVoucherItem objVoucherItem=new clsVoucherItem();
    //            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
    //            objVoucherItem.setAttribute("EFFECT","D");
    //            objVoucherItem.setAttribute("ACCOUNT_ID",1);
    //            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",PartyMainCode);
    //            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",PartyCode);
    //            objVoucherItem.setAttribute("AMOUNT",NetAmount);
    //            objVoucherItem.setAttribute("REMARKS","");
    //            objVoucherItem.setAttribute("PO_NO","");
    //            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
    //            objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //            objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //            objVoucherItem.setAttribute("GRN_NO","");
    //            objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
    //            objVoucherItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
    //            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
    //            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
    //
    //            objVoucher.DoNotValidateAccounts=true;
    //            if(objVoucher.Insert()) {
    //
    //                //SJ Posted. Automatically adjust advance receipt amount
    //                String SJNo=objVoucher.getAttribute("VOUCHER_NO").getString();
    //                //(new clsDrAdjustment()).AutoAdjustReceiptAmount(EITLERPGLOBAL.gCompanyID,SJNo);
    //
    //                return true;
    //            }
    //            else {
    //                LastError=objVoucher.LastError;
    //                return false;
    //            }
    //
    //        }
    //        catch(Exception e) {
    //
    //            return false;
    //        }
    //
    //
    //    }
    //**********************************************************************************************************************//
    //**********************************************************************************************************************//
    //**********************************************************************************************************************//
    public static int getInvoiceType(String InvoiceNo, String InvoiceDate) {
        int InvoiceType = 0;
        try {
            InvoiceType = data.getIntValueFromDB("SELECT INVOICE_TYPE FROM DINESHMILLS.D_SAL_INVOICE_HEADER WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ");
        } catch (Exception e) {
            return InvoiceType;
        }
        return InvoiceType;
    }

    public static double getIGSTPER(String InvoiceNo, String InvoiceDate, String MainAccountCode, int InvoiceType, String StateCode) {
        double IGSTPER = 0;

        try {
            IGSTPER = data.getDoubleValueFromDB("SELECT IGST_PER FROM DINESHMILLS.D_SAL_INVOICE_HEADER WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ");

            if (!StateCode.equals("24")) {
                if (InvoiceType == 1) {
                    IGSTPER = 5.00;
                } else {
                    IGSTPER = 12.00;
                }

            }

            System.out.println("IGST_PER = " + IGSTPER);
        } catch (Exception e) {
            return IGSTPER;
        }
        return IGSTPER;
    }

    public static double getCGSTPER(String InvoiceNo, String InvoiceDate, String MainAccountCode, int InvoiceType, String StateCode) {
        double CGSTPER = 0;

        try {
            CGSTPER = data.getDoubleValueFromDB("SELECT CGST_PER FROM DINESHMILLS.D_SAL_INVOICE_HEADER WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ");

            if (StateCode.equals("24")) {
                if (InvoiceType == 1) {
                    CGSTPER = 2.50;
                } else {
                    CGSTPER = 6.00;
                }

            }

            System.out.println("CGST_PER = " + CGSTPER);

        } catch (Exception e) {
            return CGSTPER;
        }
        return CGSTPER;
    }

    public static double getSGSTPER(String InvoiceNo, String InvoiceDate, String MainAccountCode, int InvoiceType, String StateCode) {
        double SGSTPER = 0;

        try {
            SGSTPER = data.getDoubleValueFromDB("SELECT SGST_PER FROM DINESHMILLS.D_SAL_INVOICE_HEADER WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ");

            if (StateCode.equals("24")) {
                if (InvoiceType == 1) {
                    SGSTPER = 2.50;
                } else {
                    SGSTPER = 6.00;
                }

            }

            System.out.println("SGST_PER = " + SGSTPER);

        } catch (Exception e) {
            return SGSTPER;
        }
        return SGSTPER;
    }

    public static String getInvoiceChargeCode(String InvoiceNo, String InvoiceDate) {
        String ChargeCode = "";
        try {
            ChargeCode = data.getStringValueFromDB("SELECT PAYMENT_TERM_CODE FROM DINESHMILLS.D_SAL_INVOICE_HEADER WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ");
        } catch (Exception e) {
            return ChargeCode;
        }
        return ChargeCode;
    }

    public static String getStateCode(String PartyCode, String MainAccountCode) {
        String StateCode = "";
        try {
            StateCode = data.getStringValueFromDB("SELECT STATE_GST_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + PartyCode + "' AND MAIN_ACCOUNT_CODE='" + MainAccountCode + "' ");
        } catch (Exception e) {
            return StateCode;
        }
        return StateCode;
    }

    public static double getInvoicePaidAmount(String InvoiceNo, String InvoiceDate) {
        double InvoicePaidAmount = 0;
        try {
            InvoicePaidAmount = data.getDoubleValueFromDB("SELECT SUM(B.AMOUNT) FROM FINANCE.D_FIN_VOUCHER_HEADER A, FINANCE.D_FIN_VOUCHER_DETAIL B "
                    + "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND B.INVOICE_NO='" + InvoiceNo + "' AND B.INVOICE_DATE='" + InvoiceDate + "' "
                    + "AND B.EFFECT='C' AND B.SUB_ACCOUNT_CODE<>'' "
                    + "AND A.APPROVED=1 AND A.CANCELLED=0 ", FinanceGlobal.FinURL);
        } catch (Exception e) {
            return InvoicePaidAmount;
        }
        return InvoicePaidAmount;
    }

    public static double getInvoiceAmount(String InvoiceNo, String InvoiceDate) {
        double InvoiceAmount = 0;
        try {
            InvoiceAmount = data.getDoubleValueFromDB("SELECT NET_AMOUNT FROM DINESHMILLS.D_SAL_INVOICE_HEADER WHERE INVOICE_NO='" + InvoiceNo + "' "
                    + "AND INVOICE_DATE='" + InvoiceDate + "' AND APPROVED=1 AND CANCELLED=0 ");
        } catch (Exception e) {
            return InvoiceAmount;
        }
        return InvoiceAmount;
    }

    public static double getDebitNotePercentage(String ReceiptDate, int InvoiceType, String ChargeCode, String PartyCode) {
        double InterestPercentage = 0;
        try {
            InterestPercentage = data.getDoubleValueFromDB("SELECT INTEREST_PERCENTAGE FROM DINESHMILLS.D_SAL_CHARGECODE_DEBITNOTE_PERCENTAGE "
                    + "WHERE CHARGE_CODE='" + ChargeCode.substring(0, 1) + "' AND INVOICE_TYPE=" + InvoiceType + " "
                    + "AND '" + ReceiptDate + "' BETWEEN FROM_DATE AND TO_DATE ");
            if (InvoiceType == 1 && (PartyCode.equals("300153") || PartyCode.equals("300157") || PartyCode.equals("300156") || PartyCode.equals("301156") || PartyCode.equals("307753") || PartyCode.equals("307757") || PartyCode.equals("300170") || PartyCode.equals("307756"))) {//PARTYCODE 300170 ADDED BY ASHUTOSH ON 20/02/2014 AS PER REQUIREMNT FROM ATUL SHAH,25/10/2018-307756 As per Requirement by Ketan Dalal
                // RISHI 18/03/2013 OLD CODE        if(InvoiceType==1 && (PartyCode.equals("300153")||PartyCode.equals("300157")||PartyCode.equals("300156")||PartyCode.equals("307753")||PartyCode.equals("307757"))) {/
                //   if(InvoiceType==1 && (PartyCode.equals("300153")||PartyCode.equals("300157")||PartyCode.equals("300170")||PartyCode.equals("300156")||PartyCode.equals("307753")||PartyCode.equals("307757")))/ {
                InterestPercentage = 15.0;
            }
            //        if(InvoiceType==1 && (PartyCode.equals("300170"))) {
            // RISHI 18/03/2013 OLD CODE        if(InvoiceType==1 && (PartyCode.equals("300153")||PartyCode.equals("300157")||PartyCode.equals("300156")||PartyCode.equals("307753")||PartyCode.equals("307757"))) {
            //   if(InvoiceType==1 && (PartyCode.equals("300153")||PartyCode.equals("300157")||PartyCode.equals("300170")||PartyCode.equals("300156")||PartyCode.equals("307753")||PartyCode.equals("307757"))) {
            //         InterestPercentage = 15.0;
            //     }

        } catch (Exception e) {
            return InterestPercentage;
        }
        return InterestPercentage;
    }

    public static boolean canDebitNotePost(int InvoiceType, String ChargeCode) {
        boolean PostDebitNote = false;
        try {
            if (InvoiceType == 1 && (ChargeCode.startsWith("2") || ChargeCode.startsWith("4") || ChargeCode.startsWith("5") || ChargeCode.startsWith("8"))) {
                PostDebitNote = true;
            } else if (InvoiceType == 2 && (ChargeCode.startsWith("2") || ChargeCode.startsWith("8"))) {
                PostDebitNote = true;
            }
        } catch (Exception e) {
            return false;
        }
        return PostDebitNote;
    }

    public static String getAgentAlphaSrNo(String InvoiceNo, String InvoiceDate) {
        String AgentAlphaSrNo = "";
        try {
            int InvoiceType = getInvoiceType(InvoiceNo, InvoiceDate);
            String PartyCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_SAL_INVOICE_HEADER WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ");
            String FinYear = EITLERPGLOBAL.getFinYearStartDate(InvoiceDate).substring(2, 4) + EITLERPGLOBAL.getFinYearEndDate(InvoiceDate).substring(2, 4);
            if (InvoiceType != 1) {
                AgentAlphaSrNo = InvoiceNo.substring(0) + "/" + FinYear;
            } else {
                AgentAlphaSrNo = clsSalesParty.getAgentAlpha(PartyCode);
                AgentAlphaSrNo = AgentAlphaSrNo + "/" + data.getStringValueFromDB("SELECT AGENT_SR_NO FROM D_SAL_INVOICE_HEADER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND INVOICE_NO ='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ");
                AgentAlphaSrNo = AgentAlphaSrNo + "/" + FinYear;
            }
        } catch (Exception e) {
            return AgentAlphaSrNo;
        }
        return AgentAlphaSrNo;
    }

    public static String getAgentAlphaSrNo(String InvoiceNo, String InvoiceDate, String PartyCode) {
        String AgentAlphaSrNo = "";
        try {
            int InvoiceType = getInvoiceType(InvoiceNo, InvoiceDate);
            String FinYear = EITLERPGLOBAL.getFinYearStartDate(InvoiceDate).substring(2, 4) + EITLERPGLOBAL.getFinYearEndDate(InvoiceDate).substring(2, 4);
            if (InvoiceType == 2) {
                AgentAlphaSrNo = InvoiceNo.substring(1) + "/" + FinYear;
            } else if (InvoiceType == 1) {
                AgentAlphaSrNo = clsSalesParty.getAgentAlpha(PartyCode);
                AgentAlphaSrNo = AgentAlphaSrNo + "/" + data.getStringValueFromDB("SELECT AGENT_SR_NO FROM D_SAL_INVOICE_HEADER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND INVOICE_NO ='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ");
                AgentAlphaSrNo = AgentAlphaSrNo + "/" + FinYear;
            }
        } catch (Exception e) {
            return AgentAlphaSrNo;
        }
        return AgentAlphaSrNo;
    }

    public static int getCreditDays(String PartyCode, String MainCode) {
        int CreditDays = 0;
        try {
            CreditDays = data.getIntValueFromDB("SELECT (COALESCE(CREDIT_DAYS,0)+COALESCE(EXTRA_CREDIT_DAYS,0)+COALESCE(GRACE_CREDIT_DAYS,0)) AS CREDIT_DAYS FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE APPROVED=1 AND CANCELLED=0 AND MAIN_ACCOUNT_CODE='" + MainCode + "' AND PARTY_CODE='" + PartyCode + "' ");
        } catch (Exception e) {
            return CreditDays;
        }
        return CreditDays;
    }

}
