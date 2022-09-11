/*
 * clsMaterialRequisition.java
 *
 * Created on April 19, 2004, 2:35 PM
 */
package EITLERP.FeltSales.FeltInvReport;

import java.util.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import EITLERP.Finance.*;
import EITLERP.Production.clsFeltProductionApprovalFlow;

/**
 *
 * @author jadave
 * @version
 */
public class clsFeltSalesInvoice {

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
    public clsFeltSalesInvoice() {
        LastError = "";
        props = new HashMap();

        props.put("BALE_NO", new Variant(""));
        props.put("PARTY_CODE", new Variant(""));
        props.put("NO_OF_PIECES", new Variant(0));
        props.put("DUE_DATE", new Variant(""));
        props.put("BAS_AMT", new Variant(0));
        props.put("GROSS_AMT", new Variant(0));
        props.put("DISC_AMT", new Variant(0));
        props.put("DISC_BAS_AMT", new Variant(0));
        props.put("EXCISE", new Variant(0));
        props.put("SEAM_CHG", new Variant(0));
        props.put("SEAM_CHG_PER", new Variant(0));
        props.put("INSURANCE_AMT", new Variant(0));
        props.put("BANK_CHARGES", new Variant(0));
        props.put("CESS", new Variant(0));
        props.put("GST_AMT", new Variant(0));
        props.put("CHEM_TRT_CHG", new Variant(0));
        props.put("PIN_CHG", new Variant(0));
        props.put("SPIRAL_CHG", new Variant(0));
        props.put("INS_IND", new Variant(0));
        props.put("CST", new Variant(0));
        props.put("VAT", new Variant(0));
        props.put("SD_AMT", new Variant(0));
        props.put("NET_AMT", new Variant(0));
        props.put("INVOICE_AMT", new Variant(0));
        props.put("SQM_IND", new Variant(0));
        props.put("OUT_STANDING_AMT", new Variant(0));
        props.put("ADV_AMT", new Variant(0));
        props.put("GRP_CRITICAL_LIMIT", new Variant(0));
        props.put("PARTY_CRITICAL_LIMIT", new Variant(0));
        props.put("CHARGE_CODE", new Variant(""));
        props.put("GRP_OUT_STANDING_AMT", new Variant(0));
        props.put("GRP_MAIN_PARTY_CODE", new Variant(""));
        props.put("INV_CRITICAL_LIMIT_AMT", new Variant(0));
        props.put("CRITICAL_LIMIT_AMT", new Variant(0));
        props.put("INDICATOR", new Variant(0));
        props.put("FLAG", new Variant(0));
        props.put("CST2", new Variant(0));
        props.put("CST5", new Variant(0));
        props.put("VAT1", new Variant(0));
        props.put("VAT4", new Variant(0));
        props.put("APPROVED", new Variant(false));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED", new Variant(false));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("HIERARCHY_ID", new Variant(""));
        props.put("CANCELLED", new Variant(false));
        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("REMARKS", new Variant(""));
        props.put("CHANGED", new Variant(""));
        props.put("CHANGED_DATE", new Variant(""));
        props.put("LOT_NO", new Variant(0));
        props.put("TRANSPORTER_CODE", new Variant(0));
        props.put("PACKING_DATE", new Variant(""));
        props.put("GATEPASS_NO", new Variant(""));
        props.put("TAX_INV_NO", new Variant(""));
        props.put("RETAIL_INV_NO", new Variant(""));
        props.put("FINYR", new Variant(""));
        
        props.put("AOSD_AMT", new Variant(0));
        
        props.put("IGST_PER", new Variant(0));
        props.put("IGST_AMT", new Variant(0));
        props.put("CGST_PER", new Variant(0));
        props.put("CGST_AMT", new Variant(0));
        props.put("SGST_PER", new Variant(0));
        props.put("SGST_AMT", new Variant(0));
        props.put("GST_COMP_CESS_PER", new Variant(0));
        props.put("GST_COMP_CESS_AMT", new Variant(0));
        
        props.put("HSN_CODE", new Variant(""));
        props.put("ADV_DOC_NO", new Variant(""));
        props.put("VEHICLE_NO", new Variant(""));
        props.put("ADV_RECEIVED_AMT", new Variant(0));
        props.put("ADV_AGN_INV_AMT", new Variant(0));
        props.put("ADV_AGN_IGST_AMT", new Variant(0));
        props.put("ADV_AGN_SGST_AMT", new Variant(0));
        props.put("ADV_AGN_CGST_AMT", new Variant(0));
        props.put("ADV_AGN_GST_COMP_CESS_AMT", new Variant(0));
        props.put("INVOICE_AMT_IN_WORD", new Variant(""));

        props.put("COMPANY_ID", new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("INVOICE_TYPE", new Variant(0));
        props.put("INVOICE_NO", new Variant(""));
        props.put("INVOICE_DATE", new Variant(""));
        
        props.put("DOCUMENT_THROUGH", new Variant(""));
        props.put("PRODUCT_CODE", new Variant(""));
        props.put("PRODUCT_DESC", new Variant(""));
        props.put("PIECE_NO", new Variant(""));
        props.put("MACHINE_NO", new Variant(""));
        props.put("POSITION_NO", new Variant(""));
        props.put("POSITION_DESC", new Variant(""));
        props.put("STYLE", new Variant(""));
        props.put("LENGTH", new Variant(0));
        props.put("WIDTH", new Variant(0));
        props.put("GSM", new Variant(0));
        props.put("ACTUAL_WEIGHT", new Variant(0));
        props.put("SQMTR", new Variant(0));
        props.put("SYN_PER", new Variant(""));
        props.put("RATE", new Variant(0));
        props.put("RATE_UNIT", new Variant(""));
        props.put("TRANSPORTER_NAME", new Variant(""));
        props.put("DESP_MODE", new Variant(""));
        props.put("PARTY_NAME", new Variant(""));
        props.put("GSTIN_NO", new Variant(""));
        props.put("ADDRESS1", new Variant(""));
        props.put("ADDRESS2", new Variant(""));
        props.put("CITY_NAME", new Variant(""));
        props.put("CITY_ID", new Variant(""));
        props.put("DISPATCH_STATION", new Variant(""));
        props.put("PLACE_OF_SUPPLY", new Variant(""));
        props.put("DISC_PER", new Variant(0));
        props.put("PINCODE", new Variant(""));
        props.put("PARTY_CHARGE_CODE", new Variant(""));
        props.put("PARTY_BANK_NAME", new Variant(""));
        props.put("PARTY_BANK_ADDRESS1", new Variant(""));
        props.put("PARTY_BANK_ADDRESS2", new Variant(""));
        
        props.put("SURCHARGE_PER", new Variant(0));
        props.put("SURCHARGE_RATE", new Variant(0));
        props.put("GROSS_RATE", new Variant(0));
        
        props.put("MOBILE_NO", new Variant(""));
        props.put("DELIVERY_MODE", new Variant(""));
        props.put("MATERIAL_CODE", new Variant(""));
        props.put("TCS_PER", new Variant(0));
        props.put("TCS_AMT", new Variant(0));
        
//        props.put("PARTY_CODE",new Variant(""));
//        props.put("QUALITY_NO",new Variant(""));
//        props.put("PATTERN_CODE",new Variant(""));
//        props.put("PIECE_NO",new Variant(""));
//        props.put("AGENT_SR_NO",new Variant(0));
//        props.put("STATION_CODE",new Variant(""));
//        props.put("PAYMENT_TERM",new Variant(""));
//        props.put("BANK_CHARGES",new Variant(0));
//        props.put("TRANSPORT_MODE",new Variant(0));
//        props.put("PAYMENT_TERM_CODE",new Variant(0));
//        props.put("AGENT_LAST_INVOICE",new Variant(""));
//        props.put("AGENT_LAST_SR_NO",new Variant(0));
//        props.put("FIN_YEAR_FROM",new Variant(0));
//        props.put("FIN_YEAR_TO",new Variant(0));
//        props.put("WAREHOUSE_CODE",new Variant(0));
//        props.put("BALE_NO",new Variant(""));
//        props.put("LR_NO",new Variant(""));
//        props.put("PACKING_DATE",new Variant(""));
//        props.put("EXPORT_CATEGORY",new Variant(""));
//        props.put("EXPORT_SUB_CATEGORY",new Variant(""));
//        props.put("GATEPASS_NO",new Variant(""));
//        props.put("GATEPASS_DATE",new Variant(""));
//        props.put("DRAFT_NO",new Variant(""));
//        props.put("DRAFT_DATE",new Variant(""));
//        props.put("TOTAL_SQ_MTR",new Variant(0));
//        props.put("TOTAL_KG",new Variant(0));
//        props.put("TOTAL_GROSS_QTY",new Variant(0));
//        props.put("TOTAL_NET_QTY",new Variant(0));
//        props.put("TOTAL_GROSS_AMOUNT",new Variant(0));
//        props.put("TOTAL_NET_AMOUNT",new Variant(0));
//        props.put("EXCISABLE_VALUE",new Variant(0));
//        props.put("TOTAL_VALUE",new Variant(0));
//        props.put("NET_AMOUNT",new Variant(0));
//        props.put("IC_TYPE",new Variant(""));
//        props.put("QUALITY_INDICATOR",new Variant(""));
//        props.put("FILLER",new Variant(""));
//        props.put("BANK_CLASS",new Variant(0));
//        props.put("BANK_CODE",new Variant(0));
//        props.put("HUNDI_NO",new Variant(""));
//        props.put("GROSS_WEIGHT",new Variant(0));
//        props.put("TRANSPORTER_CODE",new Variant(0));
//        props.put("LC_NO",new Variant(""));
//        props.put("BNG_INDICATOR",new Variant(""));
//        props.put("INS_INDICATOR",new Variant(""));
//        props.put("PARTY_NAME",new Variant(""));
//        props.put("LENGTH",new Variant(0));
//        props.put("WIDTH",new Variant(0));
//        props.put("ORDER_NO",new Variant(""));
//        props.put("ORDER_DATE",new Variant(""));
//        props.put("NO_OF_PIECES",new Variant(0));
//        
//        props.put("COLUMN_1_ID",new Variant(0));
//        props.put("COLUMN_1_FORMULA",new Variant(""));
//        props.put("COLUMN_1_PER",new Variant(0));
//        props.put("COLUMN_1_AMT",new Variant(0));
//        props.put("COLUMN_1_CAPTION",new Variant(""));
//        
//        props.put("COLUMN_2_ID",new Variant(0));
//        props.put("COLUMN_2_FORMULA",new Variant(""));
//        props.put("COLUMN_2_PER",new Variant(0));
//        props.put("COLUMN_2_AMT",new Variant(0));
//        props.put("COLUMN_2_CAPTION",new Variant(""));
//        
//        props.put("COLUMN_3_ID",new Variant(0));
//        props.put("COLUMN_3_FORMULA",new Variant(""));
//        props.put("COLUMN_3_PER",new Variant(0));
//        props.put("COLUMN_3_AMT",new Variant(0));
//        props.put("COLUMN_3_CAPTION",new Variant(""));
//        
//        props.put("COLUMN_4_ID",new Variant(0));
//        props.put("COLUMN_4_FORMULA",new Variant(""));
//        props.put("COLUMN_4_PER",new Variant(0));
//        props.put("COLUMN_4_AMT",new Variant(0));
//        props.put("COLUMN_4_CAPTION",new Variant(""));
//        
//        props.put("COLUMN_5_ID",new Variant(0));
//        props.put("COLUMN_5_FORMULA",new Variant(""));
//        props.put("COLUMN_5_PER",new Variant(0));
//        props.put("COLUMN_5_AMT",new Variant(0));
//        props.put("COLUMN_5_CAPTION",new Variant(""));
//        
//        props.put("COLUMN_6_ID",new Variant(0));
//        props.put("COLUMN_6_FORMULA",new Variant(""));
//        props.put("COLUMN_6_PER",new Variant(0));
//        props.put("COLUMN_6_AMT",new Variant(0));
//        props.put("COLUMN_6_CAPTION",new Variant(""));
//        
//        props.put("COLUMN_7_ID",new Variant(0));
//        props.put("COLUMN_7_FORMULA",new Variant(""));
//        props.put("COLUMN_7_PER",new Variant(0));
//        props.put("COLUMN_7_AMT",new Variant(0));
//        props.put("COLUMN_7_CAPTION",new Variant(""));
//        
//        props.put("COLUMN_8_ID",new Variant(0));
//        props.put("COLUMN_8_FORMULA",new Variant(""));
//        props.put("COLUMN_8_PER",new Variant(0));
//        props.put("COLUMN_8_AMT",new Variant(0));
//        props.put("COLUMN_8_CAPTION",new Variant(""));
//        
//        props.put("COLUMN_9_ID",new Variant(0));
//        props.put("COLUMN_9_FORMULA",new Variant(""));
//        props.put("COLUMN_9_PER",new Variant(0));
//        props.put("COLUMN_9_AMT",new Variant(0));
//        props.put("COLUMN_9_CAPTION",new Variant(""));
//        
//        props.put("COLUMN_10_ID",new Variant(0));
//        props.put("COLUMN_10_FORMULA",new Variant(""));
//        props.put("COLUMN_10_PER",new Variant(0));
//        props.put("COLUMN_10_AMT",new Variant(0));
//        props.put("COLUMN_10_CAPTION",new Variant(""));
//        
//        props.put("COLUMN_11_ID",new Variant(0));
//        props.put("COLUMN_11_FORMULA",new Variant(""));
//        props.put("COLUMN_11_PER",new Variant(0));
//        props.put("COLUMN_11_AMT",new Variant(0));
//        props.put("COLUMN_11_CAPTION",new Variant(""));
//        
//        props.put("COLUMN_12_ID",new Variant(0));
//        props.put("COLUMN_12_FORMULA",new Variant(""));
//        props.put("COLUMN_12_PER",new Variant(0));
//        props.put("COLUMN_12_AMT",new Variant(0));
//        props.put("COLUMN_12_CAPTION",new Variant(""));
//        
//        props.put("COLUMN_13_ID",new Variant(0));
//        props.put("COLUMN_13_FORMULA",new Variant(""));
//        props.put("COLUMN_13_PER",new Variant(0));
//        props.put("COLUMN_13_AMT",new Variant(0));
//        props.put("COLUMN_13_CAPTION",new Variant(""));
//        
//        props.put("COLUMN_14_ID",new Variant(0));
//        props.put("COLUMN_14_FORMULA",new Variant(""));
//        props.put("COLUMN_14_PER",new Variant(0));
//        props.put("COLUMN_14_AMT",new Variant(0));
//        props.put("COLUMN_14_CAPTION",new Variant(""));
//        
//        props.put("COLUMN_15_ID",new Variant(0));
//        props.put("COLUMN_15_FORMULA",new Variant(""));
//        props.put("COLUMN_15_PER",new Variant(0));
//        props.put("COLUMN_15_AMT",new Variant(0));
//        props.put("COLUMN_15_CAPTION",new Variant(""));
//        
//        props.put("COLUMN_16_ID",new Variant(0));
//        props.put("COLUMN_16_FORMULA",new Variant(""));
//        props.put("COLUMN_16_PER",new Variant(0));
//        props.put("COLUMN_16_AMT",new Variant(0));
//        props.put("COLUMN_16_CAPTION",new Variant(""));
//        
//        props.put("COLUMN_17_ID",new Variant(0));
//        props.put("COLUMN_17_FORMULA",new Variant(""));
//        props.put("COLUMN_17_PER",new Variant(0));
//        props.put("COLUMN_17_AMT",new Variant(0));
//        props.put("COLUMN_17_CAPTION",new Variant(""));
//        
//        props.put("COLUMN_18_ID",new Variant(0));
//        props.put("COLUMN_18_FORMULA",new Variant(""));
//        props.put("COLUMN_18_PER",new Variant(0));
//        props.put("COLUMN_18_AMT",new Variant(0));
//        props.put("COLUMN_18_CAPTION",new Variant(""));
//        
//        props.put("COLUMN_19_ID",new Variant(0));
//        props.put("COLUMN_19_FORMULA",new Variant(""));
//        props.put("COLUMN_19_PER",new Variant(0));
//        props.put("COLUMN_19_AMT",new Variant(0));
//        props.put("COLUMN_19_CAPTION",new Variant(""));
//        
//        props.put("COLUMN_20_ID",new Variant(0));
//        props.put("COLUMN_20_FORMULA",new Variant(""));
//        props.put("COLUMN_20_PER",new Variant(0));
//        props.put("COLUMN_20_AMT",new Variant(0));
//        props.put("COLUMN_20_CAPTION",new Variant(""));
//        
//        props.put("COLUMN_21_ID",new Variant(0));
//        props.put("COLUMN_21_FORMULA",new Variant(""));
//        props.put("COLUMN_21_PER",new Variant(0));
//        props.put("COLUMN_21_AMT",new Variant(0));
//        props.put("COLUMN_21_CAPTION",new Variant(""));
//        
//        props.put("COLUMN_22_ID",new Variant(0));
//        props.put("COLUMN_22_FORMULA",new Variant(""));
//        props.put("COLUMN_22_PER",new Variant(0));
//        props.put("COLUMN_22_AMT",new Variant(0));
//        props.put("COLUMN_22_CAPTION",new Variant(""));
//        
//        props.put("COLUMN_23_ID",new Variant(0));
//        props.put("COLUMN_23_FORMULA",new Variant(""));
//        props.put("COLUMN_23_PER",new Variant(0));
//        props.put("COLUMN_23_AMT",new Variant(0));
//        props.put("COLUMN_23_CAPTION",new Variant(""));
//        
//        props.put("COLUMN_24_ID",new Variant(0));
//        props.put("COLUMN_24_FORMULA",new Variant(""));
//        props.put("COLUMN_24_PER",new Variant(0));
//        props.put("COLUMN_24_AMT",new Variant(0));
//        props.put("COLUMN_24_CAPTION",new Variant(""));
//        
//        props.put("COLUMN_25_ID",new Variant(0));
//        props.put("COLUMN_25_FORMULA",new Variant(""));
//        props.put("COLUMN_25_PER",new Variant(0));
//        props.put("COLUMN_25_AMT",new Variant(0));
//        props.put("COLUMN_25_CAPTION",new Variant(""));
//        
//        
//        props.put("APPROVED",new Variant(false));
//        props.put("APPROVED_DATE", new Variant(""));
//        props.put("REJECTED",new Variant(false));
//        props.put("REJECTED_DATE", new Variant(""));
//        props.put("REMARKS", new Variant(""));
//        props.put("CANCELLED", new Variant(false));
//        props.put("STATUS", new Variant(""));
//        props.put("HIERARCHY_ID",new Variant(0));
//        props.put("CREATED_BY",new Variant(0));
//        props.put("CREATED_DATE",new Variant(""));
//        props.put("MODIFIED_BY",new Variant(0));
//        props.put("MODIFIED_DATE",new Variant(""));
//        props.put("PREFIX",new Variant("")); //For Autonumber generation
//        props.put("VAT1",new Variant(0));
//        props.put("VAT4",new Variant(0));
//        props.put("CST2",new Variant(0));
//        props.put("CST5",new Variant(0));
//        props.put("SD_AMT",new Variant(0));

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
            rsResultSet = Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE COMPANY_ID=" + pCompanyID + " AND SUBSTRING(INVOICE_DATE,1,10)>='" + EITLERPGLOBAL.FinFromDateDB + "' AND SUBSTRING(INVOICE_DATE,1,10)<='" + EITLERPGLOBAL.FinToDateDB + "' ORDER BY INVOICE_DATE,INVOICE_NO");
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
        String strCondition = " WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND INVOICE_NO='" + pReqNo + "' AND SUBSTRING(INVOICE_DATE,1,10)='" + pReqDate + "'";
        clsFeltSalesInvoice objInvoice = new clsFeltSalesInvoice();
        objInvoice.Filter(strCondition, pCompanyID);
        return objInvoice;
    }

    public Object getObject(int pCompanyID, String pReqNo, String pReqDate, String dbURL) {
        String strCondition = " WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND INVOICE_NO='" + pReqNo + "' AND SUBSTRING(INVOICE_DATE,1,10)='" + pReqDate + "'";
        clsFeltSalesInvoice objInvoice = new clsFeltSalesInvoice();
        objInvoice.Filter(strCondition, pCompanyID, dbURL);
        return objInvoice;
    }

    public boolean Filter(String pCondition, int pCompanyID) {
        Ready = false;
        try {
            String strSql = "SELECT * FROM PRODUCTION.FELT_SAL_INVOICE_HEADER " + pCondition;
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery(strSql);

            if (!rsResultSet.first()) {
//                strSql = "SELECT * FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND SUBSTRING(INVOICE_DATE,1,10)>='" + EITLERPGLOBAL.FinFromDateDB + "' AND SUBSTRING(INVOICE_DATE,1,10)<='" + EITLERPGLOBAL.FinToDateDB + "' ORDER BY INVOICE_NO";
                strSql = "SELECT * FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE COMPANY_ID=" + Long.toString(pCompanyID) + "  ORDER BY INVOICE_NO,INVOICE_DATE";
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
            String strSql = "SELECT * FROM PRODUCTION.FELT_SAL_INVOICE_HEADER " + pCondition;
            Conn = data.getConn(dbURL);
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery(strSql);

            if (!rsResultSet.first()) {
                strSql = "SELECT * FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND SUBSTRING(INVOICE_DATE,1,10)>='" + EITLERPGLOBAL.FinFromDateDB + "' AND SUBSTRING(INVOICE_DATE,1,10)<='" + EITLERPGLOBAL.FinToDateDB + "' ORDER BY INVOICE_NO";
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
            setAttribute("BALE_NO", rsResultSet.getString("BALE_NO"));
            setAttribute("PARTY_CODE", rsResultSet.getString("PARTY_CODE"));
            setAttribute("NO_OF_PIECES", rsResultSet.getDouble("NO_OF_PIECES"));
            setAttribute("DUE_DATE", rsResultSet.getString("DUE_DATE"));
            setAttribute("BAS_AMT", rsResultSet.getDouble("BAS_AMT"));
            setAttribute("GROSS_AMT", rsResultSet.getDouble("GROSS_AMT"));
            setAttribute("DISC_AMT", rsResultSet.getDouble("DISC_AMT"));
            setAttribute("DISC_BAS_AMT", rsResultSet.getDouble("DISC_BAS_AMT"));
            setAttribute("EXCISE", rsResultSet.getDouble("EXCISE"));
            setAttribute("SEAM_CHG", rsResultSet.getDouble("SEAM_CHG"));
            setAttribute("SEAM_CHG_PER", rsResultSet.getDouble("SEAM_CHG_PER"));
            setAttribute("INSURANCE_AMT", rsResultSet.getDouble("INSURANCE_AMT"));
            setAttribute("BANK_CHARGES", rsResultSet.getDouble("BANK_CHARGES"));
            setAttribute("CESS", rsResultSet.getDouble("CESS"));
            setAttribute("GST_AMT", rsResultSet.getDouble("GST_AMT"));
            
            setAttribute("AOSD_AMT", rsResultSet.getDouble("AOSD_AMT"));
            
            setAttribute("IGST_PER", rsResultSet.getDouble("IGST_PER"));
            setAttribute("IGST_AMT", rsResultSet.getDouble("IGST_AMT"));
            setAttribute("CGST_PER", rsResultSet.getDouble("CGST_PER"));
            setAttribute("CGST_AMT", rsResultSet.getDouble("CGST_AMT"));
            setAttribute("SGST_PER", rsResultSet.getDouble("SGST_PER"));
            setAttribute("SGST_AMT", rsResultSet.getDouble("SGST_AMT"));
            setAttribute("GST_COMP_CESS_PER", rsResultSet.getDouble("GST_COMP_CESS_PER"));
            setAttribute("GST_COMP_CESS_AMT", rsResultSet.getDouble("GST_COMP_CESS_AMT"));
            
            setAttribute("CHEM_TRT_CHG", rsResultSet.getDouble("CHEM_TRT_CHG"));
            setAttribute("PIN_CHG", rsResultSet.getDouble("PIN_CHG"));
            setAttribute("SPIRAL_CHG", rsResultSet.getDouble("SPIRAL_CHG"));
            setAttribute("INS_IND", rsResultSet.getInt("INS_IND"));
            setAttribute("CST", rsResultSet.getInt("CST"));
            setAttribute("VAT", rsResultSet.getInt("VAT"));
            setAttribute("SD_AMT", rsResultSet.getDouble("SD_AMT"));

            setAttribute("NET_AMT", rsResultSet.getDouble("NET_AMT"));
            setAttribute("INVOICE_AMT", rsResultSet.getDouble("INVOICE_AMT"));
            setAttribute("SQM_IND", rsResultSet.getInt("SQM_IND"));
            setAttribute("OUT_STANDING_AMT", rsResultSet.getDouble("OUT_STANDING_AMT"));
            setAttribute("ADV_AMT", rsResultSet.getDouble("ADV_AMT"));
            setAttribute("GRP_CRITICAL_LIMIT", rsResultSet.getDouble("GRP_CRITICAL_LIMIT"));
            setAttribute("PARTY_CRITICAL_LIMIT", rsResultSet.getDouble("PARTY_CRITICAL_LIMIT"));
            setAttribute("CHARGE_CODE", rsResultSet.getString("CHARGE_CODE"));
            setAttribute("GRP_OUT_STANDING_AMT", rsResultSet.getDouble("GRP_OUT_STANDING_AMT"));
            setAttribute("GRP_MAIN_PARTY_CODE", rsResultSet.getString("GRP_MAIN_PARTY_CODE"));
            setAttribute("INV_CRITICAL_LIMIT_AMT", rsResultSet.getDouble("INV_CRITICAL_LIMIT_AMT"));
            setAttribute("CRITICAL_LIMIT_AMT", rsResultSet.getDouble("CRITICAL_LIMIT_AMT"));
            setAttribute("INDICATOR", rsResultSet.getBoolean("INDICATOR"));
            setAttribute("FLAG", rsResultSet.getBoolean("FLAG"));
            setAttribute("VAT1", rsResultSet.getDouble("VAT1"));
            setAttribute("VAT4", rsResultSet.getDouble("VAT4"));
            setAttribute("CST2", rsResultSet.getDouble("CST2"));
            setAttribute("CST5", rsResultSet.getDouble("CST5"));
            setAttribute("LOT_NO", rsResultSet.getInt("LOT_NO"));
            setAttribute("TRANSPORTER_CODE", rsResultSet.getInt("TRANSPORTER_CODE"));
            setAttribute("PACKING_DATE", rsResultSet.getString("PACKING_DATE"));
            setAttribute("GATEPASS_NO", rsResultSet.getString("GATEPASS_NO"));
            setAttribute("TAX_INV_NO", rsResultSet.getString("TAX_INV_NO"));
            setAttribute("RETAIL_INV_NO", rsResultSet.getString("RETAIL_INV_NO"));
            
            setAttribute("HSN_CODE", rsResultSet.getString("HSN_CODE"));
            setAttribute("ADV_DOC_NO", rsResultSet.getString("ADV_DOC_NO"));
            setAttribute("VEHICLE_NO", rsResultSet.getString("VEHICLE_NO"));
            
            setAttribute("ADV_RECEIVED_AMT", rsResultSet.getDouble("ADV_RECEIVED_AMT"));
            setAttribute("ADV_AGN_INV_AMT", rsResultSet.getDouble("ADV_AGN_INV_AMT"));
            setAttribute("ADV_AGN_IGST_AMT", rsResultSet.getDouble("ADV_AGN_IGST_AMT"));
            setAttribute("ADV_AGN_SGST_AMT", rsResultSet.getDouble("ADV_AGN_SGST_AMT"));
            setAttribute("ADV_AGN_CGST_AMT", rsResultSet.getDouble("ADV_AGN_CGST_AMT"));
            setAttribute("ADV_AGN_GST_COMP_CESS_AMT", rsResultSet.getDouble("ADV_AGN_GST_COMP_CESS_AMT"));

            setAttribute("DOCUMENT_THROUGH", rsResultSet.getString("DOCUMENT_THROUGH"));
            setAttribute("PRODUCT_CODE", rsResultSet.getString("PRODUCT_CODE"));
            setAttribute("PRODUCT_DESC", rsResultSet.getString("PRODUCT_DESC"));
            setAttribute("PIECE_NO", rsResultSet.getString("PIECE_NO"));
            setAttribute("MACHINE_NO", rsResultSet.getString("MACHINE_NO"));
            setAttribute("POSITION_NO", rsResultSet.getString("POSITION_NO"));
            setAttribute("POSITION_DESC", rsResultSet.getString("POSITION_DESC"));
            setAttribute("STYLE", rsResultSet.getString("STYLE"));
            setAttribute("LENGTH", rsResultSet.getFloat("LENGTH"));
            setAttribute("WIDTH", rsResultSet.getFloat("WIDTH"));
            setAttribute("GSM", rsResultSet.getFloat("GSM"));
            setAttribute("ACTUAL_WEIGHT", rsResultSet.getFloat("ACTUAL_WEIGHT"));
            setAttribute("SQMTR", rsResultSet.getFloat("SQMTR"));
            setAttribute("SYN_PER", rsResultSet.getString("SYN_PER"));
            setAttribute("RATE", rsResultSet.getDouble("RATE"));
            setAttribute("RATE_UNIT", rsResultSet.getString("RATE_UNIT"));
            setAttribute("TRANSPORTER_NAME", rsResultSet.getString("TRANSPORTER_NAME"));
            setAttribute("DESP_MODE", rsResultSet.getString("DESP_MODE"));
            setAttribute("PARTY_NAME", rsResultSet.getString("PARTY_NAME"));
            setAttribute("GSTIN_NO", rsResultSet.getString("GSTIN_NO"));
            setAttribute("ADDRESS1", rsResultSet.getString("ADDRESS1"));
            setAttribute("ADDRESS2", rsResultSet.getString("ADDRESS2"));
            setAttribute("CITY_NAME", rsResultSet.getString("CITY_NAME"));
            setAttribute("CITY_ID", rsResultSet.getString("CITY_ID"));
            setAttribute("DISPATCH_STATION", rsResultSet.getString("DISPATCH_STATION"));
            setAttribute("PLACE_OF_SUPPLY", rsResultSet.getString("PLACE_OF_SUPPLY"));
            setAttribute("DISC_PER", rsResultSet.getDouble("DISC_PER"));
            setAttribute("PINCODE", rsResultSet.getString("PINCODE"));
            setAttribute("PARTY_CHARGE_CODE", rsResultSet.getString("PARTY_CHARGE_CODE"));
            setAttribute("PARTY_BANK_NAME", rsResultSet.getString("PARTY_BANK_NAME"));
            setAttribute("PARTY_BANK_ADDRESS1", rsResultSet.getString("PARTY_BANK_ADDRESS1"));
            setAttribute("PARTY_BANK_ADDRESS2", rsResultSet.getString("PARTY_BANK_ADDRESS2"));
            
            setAttribute("SURCHARGE_PER", rsResultSet.getDouble("SURCHARGE_PER"));
            setAttribute("SURCHARGE_RATE", rsResultSet.getDouble("SURCHARGE_RATE"));
            setAttribute("GROSS_RATE", rsResultSet.getDouble("GROSS_RATE"));
            
            setAttribute("MOBILE_NO", rsResultSet.getString("MOBILE_NO"));
            setAttribute("DELIVERY_MODE", rsResultSet.getString("DELIVERY_MODE"));
            setAttribute("MATERIAL_CODE", rsResultSet.getString("MATERIAL_CODE"));
            setAttribute("TCS_PER", rsResultSet.getFloat("TCS_PER"));
            setAttribute("TCS_AMT", rsResultSet.getDouble("TCS_AMT"));

            setAttribute("APPROVED", rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE", rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED", rsResultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE", rsResultSet.getString("REJECTED_DATE"));
            setAttribute("HIERARCHY_ID", rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("CANCELLED", rsResultSet.getInt("CANCELLED"));
            setAttribute("REMARKS", rsResultSet.getString("REMARKS"));
            //setAttribute("STATUS",rsResultSet.getString("STATUS"));
            setAttribute("CREATED_BY", rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE", rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY", rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE", rsResultSet.getString("MODIFIED_DATE"));

            colInvoiceItems.clear();
            String InvoiceNo = getAttribute("INVOICE_NO").getString();
            String InvoiceDate = getAttribute("INVOICE_DATE").getString().substring(0, 10);

            ResultSet rsTmp;
            rsTmp = data.getResult("SELECT * FROM PRODUCTION.FELT_SAL_INVOICE_DETAIL WHERE INVOICE_NO='" + InvoiceNo + "' AND SUBSTRING(INVOICE_DATE,1,10)='" + InvoiceDate + "' ORDER BY BALE_NO,PIECE_NO");

            rsTmp.first();

            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {

                Counter = Counter + 1;
                clsFeltSalesInvoiceDetail objItem = new clsFeltSalesInvoiceDetail();

                objItem.setAttribute("BALE_NO", UtilFunctions.getString(rsTmp, "BALE_NO", ""));
                objItem.setAttribute("PIECE_NO", UtilFunctions.getString(rsTmp, "PIECE_NO", ""));
                objItem.setAttribute("MACHINE_NO", UtilFunctions.getString(rsTmp, "MACHINE_NO", ""));
                objItem.setAttribute("POSITION_NO", UtilFunctions.getString(rsTmp, "POSITION_NO", ""));
                objItem.setAttribute("PARTY_CODE", UtilFunctions.getString(rsTmp, "PARTY_CODE", ""));
                objItem.setAttribute("PRODUCT_CODE", UtilFunctions.getString(rsTmp, "PRODUCT_CODE", ""));
                objItem.setAttribute("GROUP_NAME", UtilFunctions.getString(rsTmp, "GROUP_NAME", ""));
                objItem.setAttribute("STYLE", UtilFunctions.getString(rsTmp, "STYLE", ""));
                objItem.setAttribute("LENGTH", UtilFunctions.getDouble(rsTmp, "LENGTH", 0));
                objItem.setAttribute("WIDTH", UtilFunctions.getDouble(rsTmp, "WIDTH", 0));
                objItem.setAttribute("GSM", UtilFunctions.getDouble(rsTmp, "GSM", 0));
                objItem.setAttribute("THORITICAL_WEIGHT", UtilFunctions.getDouble(rsTmp, "THORITICAL_WEIGHT", 0));
                objItem.setAttribute("SQMTR", UtilFunctions.getDouble(rsTmp, "SQMTR", 0));
                objItem.setAttribute("SYN_PER", UtilFunctions.getString(rsTmp, "SYN_PER", ""));
                objItem.setAttribute("ACTUAL_WEIGHT", UtilFunctions.getDouble(rsTmp, "ACTUAL_WEIGHT", 0));
                objItem.setAttribute("ACTUAL_LENGTH", UtilFunctions.getDouble(rsTmp, "ACTUAL_LENGTH", 0));
                objItem.setAttribute("ACTUAL_WIDTH", UtilFunctions.getDouble(rsTmp, "ACTUAL_WIDTH", 0));
                objItem.setAttribute("INVOICE_NO", UtilFunctions.getString(rsTmp, "INVOICE_NO", ""));
                objItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(rsTmp.getString("INVOICE_DATE").substring(0, 10)));
                objItem.setAttribute("INVOICE_PARTY", UtilFunctions.getString(rsTmp, "INVOICE_PARTY", ""));
                objItem.setAttribute("RATE", UtilFunctions.getDouble(rsTmp, "RATE", 0));
                objItem.setAttribute("BAS_AMT", UtilFunctions.getDouble(rsTmp, "BAS_AMT", 0));
                objItem.setAttribute("DISC_PER", UtilFunctions.getDouble(rsTmp, "DISC_PER", 0));
                objItem.setAttribute("DISC_AMT", UtilFunctions.getDouble(rsTmp, "DISC_AMT", 0));
                objItem.setAttribute("DISC_BAS_AMT", UtilFunctions.getDouble(rsTmp, "DISC_BAS_AMT", 0));
                objItem.setAttribute("EXCISE", UtilFunctions.getDouble(rsTmp, "EXCISE", 0));
                objItem.setAttribute("IGST_PER", UtilFunctions.getDouble(rsTmp, "IGST_PER", 0));
                objItem.setAttribute("IGST_AMT", UtilFunctions.getDouble(rsTmp, "IGST_AMT", 0));
                objItem.setAttribute("CGST_PER", UtilFunctions.getDouble(rsTmp, "CGST_PER", 0));
                objItem.setAttribute("CGST_AMT", UtilFunctions.getDouble(rsTmp, "CGST_AMT", 0));
                objItem.setAttribute("SGST_PER", UtilFunctions.getDouble(rsTmp, "SGST_PER", 0));
                objItem.setAttribute("SGST_AMT", UtilFunctions.getDouble(rsTmp, "SGST_AMT", 0));
                objItem.setAttribute("GST_COMP_CESS_PER", UtilFunctions.getDouble(rsTmp, "GST_COMP_CESS_PER", 0));
                objItem.setAttribute("GST_COMP_CESS_AMT", UtilFunctions.getDouble(rsTmp, "GST_COMP_CESS_AMT", 0));
                objItem.setAttribute("SEAM_CHG", UtilFunctions.getDouble(rsTmp, "SEAM_CHG", 0));
                objItem.setAttribute("SEAM_CHG_PER", UtilFunctions.getDouble(rsTmp, "SEAM_CHG_PER", 0));
                objItem.setAttribute("INSURANCE_AMT", UtilFunctions.getDouble(rsTmp, "INSURANCE_AMT", 0));
                objItem.setAttribute("CHEM_TRT_CHG", UtilFunctions.getDouble(rsTmp, "CHEM_TRT_CHG", 0));
                objItem.setAttribute("PIN_CHG", UtilFunctions.getDouble(rsTmp, "PIN_CHG", 0));
                objItem.setAttribute("SPIRAL_CHG", UtilFunctions.getDouble(rsTmp, "SPIRAL_CHG", 0));
                objItem.setAttribute("INS_IND", UtilFunctions.getDouble(rsTmp, "INS_IND", 0));
                objItem.setAttribute("CST", UtilFunctions.getDouble(rsTmp, "CST", 0));
                objItem.setAttribute("VAT", UtilFunctions.getDouble(rsTmp, "VAT", 0));
                objItem.setAttribute("SD_AMT", UtilFunctions.getDouble(rsTmp, "SD_AMT", 0));
                objItem.setAttribute("INVOICE_AMT", UtilFunctions.getDouble(rsTmp, "INVOICE_AMT", 0));
                objItem.setAttribute("SQM_IND", UtilFunctions.getInt(rsTmp, "SQM_IND", 0));
                objItem.setAttribute("OUT_STANDING_AMT", UtilFunctions.getDouble(rsTmp, "OUT_STANDING_AMT", 0));
                objItem.setAttribute("ADV_AMT", UtilFunctions.getDouble(rsTmp, "ADV_AMT", 0));
                objItem.setAttribute("GRP_CRITICAL_LIMIT", UtilFunctions.getDouble(rsTmp, "GRP_CRITICAL_LIMIT", 0));
                objItem.setAttribute("PARTY_CRITICAL_LIMIT", UtilFunctions.getDouble(rsTmp, "PARTY_CRITICAL_LIMIT", 0));
                objItem.setAttribute("CHARGE_CODE", UtilFunctions.getString(rsTmp, "CHARGE_CODE", ""));
                objItem.setAttribute("GRP_OUT_STANDING_AMT", UtilFunctions.getDouble(rsTmp, "GRP_OUT_STANDING_AMT", 0));
                objItem.setAttribute("GRP_MAIN_PARTY_CODE", UtilFunctions.getString(rsTmp, "GRP_MAIN_PARTY_CODE", ""));
                objItem.setAttribute("INV_CRITICAL_LIMIT_AMT", UtilFunctions.getDouble(rsTmp, "INV_CRITICAL_LIMIT_AMT", 0));
                objItem.setAttribute("CRITICAL_LIMIT_AMT", UtilFunctions.getDouble(rsTmp, "CRITICAL_LIMIT_AMT", 0));
                objItem.setAttribute("INDICATOR", UtilFunctions.getBoolean(rsTmp, "INDICATOR", false));
                objItem.setAttribute("FLAG", UtilFunctions.getBoolean(rsTmp, "FLAG", false));
                objItem.setAttribute("CST2", UtilFunctions.getDouble(rsTmp, "CST2", 0));
                objItem.setAttribute("CST5", UtilFunctions.getDouble(rsTmp, "CST5", 0));
                objItem.setAttribute("VAT1", UtilFunctions.getDouble(rsTmp, "VAT1", 0));
                objItem.setAttribute("VAT4", UtilFunctions.getDouble(rsTmp, "VAT4", 0));
                
                objItem.setAttribute("SURCHARGE_PER", UtilFunctions.getDouble(rsTmp, "SURCHARGE_PER", 0));
                objItem.setAttribute("SURCHARGE_RATE", UtilFunctions.getDouble(rsTmp, "SURCHARGE_RATE", 0));
                objItem.setAttribute("GROSS_RATE", UtilFunctions.getDouble(rsTmp, "GROSS_RATE", 0));
                
                objItem.setAttribute("MATERIAL_CODE", UtilFunctions.getString(rsTmp, "MATERIAL_CODE", ""));
                objItem.setAttribute("TCS_PER", UtilFunctions.getDouble(rsTmp, "TCS_PER", 0));
                objItem.setAttribute("TCS_AMT", UtilFunctions.getDouble(rsTmp, "TCS_AMT", 0));
                
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
            rsTmp = data.getResult("SELECT INVOICE_NO,APPROVED,CANCELLED FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE COMPANY_ID=" + pCompanyID + " AND INVOICE_NO='" + pDocNo + "' AND SUBSTRING(INVOICE_DATE,1,10)='" + pDocDate + "'");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                if (rsTmp.getBoolean("APPROVED")) {
                    if (rsTmp.getBoolean("CANCELLED")) {
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
    //                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND MODULE_ID="+(clsFeltSalesInvoice.ModuleID));
    //                }
    //
    //                //Now Update the header with cancel falg to true
    //                data.Execute("UPDATE D_SAL_INVOICE_HEADER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND INVOICE_NO='"+pDocNo+"'");
    //                data.Execute("UPDATE D_SAL_INVOICE_DETAIL SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND INVOICE_NO='"+pDocNo+"'");
    //
    //
    //                //Cancel the SJ associated with this invoice
    //                if(data.IsRecordExist("SELECT A.VOUCHER_NO FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.VOUCHER_TYPE="+FinanceGlobal.TYPE_SALES_JOURNAL+" AND INVOICE_NO='"+pDocNo+"' AND MODULE_ID="+clsFeltSalesInvoice.ModuleID,FinanceGlobal.FinURL))
    //                {
    //                   String VoucherNo=data.getStringValueFromDB("SELECT A.VOUCHER_NO FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.VOUCHER_TYPE="+FinanceGlobal.TYPE_SALES_JOURNAL+" AND INVOICE_NO='"+pDocNo+"' AND MODULE_ID="+clsFeltSalesInvoice.ModuleID,FinanceGlobal.FinURL);
    //                   int VoucherCompanyID=data.getIntValueFromDB("SELECT A.COMPANY_ID FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.VOUCHER_TYPE="+FinanceGlobal.TYPE_SALES_JOURNAL+" AND INVOICE_NO='"+pDocNo+"' AND MODULE_ID="+clsFeltSalesInvoice.ModuleID,FinanceGlobal.FinURL);
    //
    //                   clsVoucher.CancelDoc(VoucherCompanyID, VoucherNo, clsVoucher.getVoucherModuleID(FinanceGlobal.TYPE_SALES_JOURNAL));
    //                }
    //
    //                //Rest advance payment adjustments from the receipt vouchers
    //                ResultSet rsReceipts=data.getResult("SELECT A.VOUCHER_NO FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND (A.VOUCHER_TYPE="+FinanceGlobal.TYPE_RECEIPT+" OR A.VOUCHER_TYPE="+FinanceGlobal.TYPE_JOURNAL+") AND B.INVOICE_NO='"+pDocNo+"' AND B.MODULE_ID="+clsFeltSalesInvoice.ModuleID);
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
    //                     data.Execute("UPDATE D_FIN_VOUCHER_DETAIL SET INVOICE_NO='',INVOICE_DATE='',GRN_NO='',GRN_DATE='',MODULE_ID=0,REF_COMPANY_ID=0 WHERE VOUCHER_NO='"+VoucherNo+"' AND INVOICE_NO='"+pDocNo+"' AND MODULE_ID="+clsFeltSalesInvoice.ModuleID,FinanceGlobal.FinURL);
    //                     data.Execute("UPDATE D_FIN_VOUCHER_DETAIL_EX SET INVOICE_NO='',INVOICE_DATE='',GRN_NO='',GRN_DATE='',MODULE_ID=0,REF_COMPANY_ID=0 WHERE VOUCHER_NO='"+VoucherNo+"' AND INVOICE_NO='"+pDocNo+"' AND MODULE_ID="+clsFeltSalesInvoice.ModuleID,FinanceGlobal.FinURL);
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
            rsTmp = data.getResult("SELECT INVOICE_NO FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE COMPANY_ID=" + pCompanyID + " AND INVOICE_NO='" + pDocNo + "' AND SUBSTRING(INVOICE_DATE,1,10)='" + pDocDate + "' AND CANCELLED=0");
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

                rsTmp = data.getResult("SELECT APPROVED FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE COMPANY_ID=" + pCompanyID + " AND INVOICE_NO='" + pDocNo + "' AND SUBSTRING(INVOICE_DATE,1,10)='" + pDocDate + "'");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    ApprovedDoc = rsTmp.getBoolean("APPROVED");
                }

                if (ApprovedDoc) {
                } else {
                    //data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='" + pDocNo + "' AND MODULE_ID=" + (clsFeltSalesInvoice.ModuleID));
                    data.Execute("UPDATE PRODUCTION.FELT_PROD_DOC_DATA SET STATUS='N' WHERE DOC_NO='" + pDocNo + "' AND MODULE_ID=" + (clsFeltSalesInvoice.ModuleID));
                }

                //Now Update the header with cancel falg to true
                data.Execute("UPDATE PRODUCTION.FELT_SAL_INVOICE_HEADER SET APPROVED=0,APPROVED_DATE='0000-00-00',CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + pCompanyID + " AND INVOICE_NO='" + pDocNo + "' AND SUBSTRING(INVOICE_DATE,1,10)='" + pDocDate + "' ");
                //data.Execute("UPDATE PRODUCTION.FELT_SAL_INVOICE_DETAIL SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + pCompanyID + " AND INVOICE_NO='" + pDocNo + "' AND INVOICE_DATE='" + pDocDate + "' ");
//--------------------------------------------------------------
                //Cancel the SJ associated with this invoice
                if (data.IsRecordExist("SELECT A.VOUCHER_NO FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.VOUCHER_TYPE=" + FinanceGlobal.TYPE_SALES_JOURNAL + " AND B.INVOICE_NO='" + pDocNo + "' AND B.INVOICE_DATE='" + pDocDate + "' ", FinanceGlobal.FinURL)) {
                    String VoucherNo = data.getStringValueFromDB("SELECT A.VOUCHER_NO FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.VOUCHER_TYPE=" + FinanceGlobal.TYPE_SALES_JOURNAL + " AND B.INVOICE_NO='" + pDocNo + "' AND B.INVOICE_DATE='" + pDocDate + "' ", FinanceGlobal.FinURL); //AND B.MODULE_ID="+clsFeltSalesInvoice.ModuleID
                    int VoucherCompanyID = data.getIntValueFromDB("SELECT A.COMPANY_ID FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.VOUCHER_TYPE=" + FinanceGlobal.TYPE_SALES_JOURNAL + " AND B.INVOICE_NO='" + pDocNo + "' AND B.INVOICE_DATE='" + pDocDate + "' ", FinanceGlobal.FinURL); //AND B.MODULE_ID="+clsFeltSalesInvoice.ModuleID

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
                        data.Execute("UPDATE D_FIN_VOUCHER_DETAIL SET INVOICE_NO='',INVOICE_DATE='',INVOICE_AMOUNT='',GRN_NO='',GRN_DATE='',MODULE_ID=0,REF_COMPANY_ID=0,MATCHED=0,MATCHED_DATE='0000-00-00' WHERE VOUCHER_NO='" + VoucherNo + "' AND INVOICE_NO='" + pDocNo + "' AND INVOICE_DATE='" + pDocDate + "' ", FinanceGlobal.FinURL);
                        data.Execute("UPDATE D_FIN_VOUCHER_DETAIL_EX SET INVOICE_NO='',INVOICE_DATE='',INVOICE_AMOUNT='',GRN_NO='',GRN_DATE='',MODULE_ID=0,REF_COMPANY_ID=0,MATCHED=0,MATCHED_DATE='0000-00-00' WHERE VOUCHER_NO='" + VoucherNo + "' AND INVOICE_NO='" + pDocNo + "' AND INVOICE_DATE='" + pDocDate + "' ", FinanceGlobal.FinURL);
                        data.Execute("UPDATE D_FIN_VOUCHER_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE VOUCHER_NO='" + VoucherNo + "' ", FinanceGlobal.FinURL);
                        
                        //Cancel the JV associated with this invoice & clear reference from RC
                        if(VoucherNo.startsWith("JV")) {
                            
                            data.Execute("UPDATE D_FIN_VOUCHER_DETAIL SET INVOICE_NO='',INVOICE_DATE='',INVOICE_AMOUNT='',GRN_NO='',GRN_DATE='',MODULE_ID=0,REF_COMPANY_ID=0,MATCHED=0,MATCHED_DATE='0000-00-00' WHERE GRN_NO='" + VoucherNo + "' ", FinanceGlobal.FinURL);
                            data.Execute("UPDATE D_FIN_VOUCHER_DETAIL_EX SET INVOICE_NO='',INVOICE_DATE='',INVOICE_AMOUNT='',GRN_NO='',GRN_DATE='',MODULE_ID=0,REF_COMPANY_ID=0,MATCHED=0,MATCHED_DATE='0000-00-00' WHERE GRN_NO='" + VoucherNo + "' ", FinanceGlobal.FinURL);
                          
                            //Now Update the header with cancel falg to true
                            data.Execute("UPDATE D_FIN_VOUCHER_HEADER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + pCompanyID + " AND VOUCHER_NO='" + VoucherNo + "'", FinanceGlobal.FinURL);
                            data.Execute("UPDATE D_FIN_VOUCHER_DETAIL SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + pCompanyID + " AND VOUCHER_NO='" + VoucherNo + "'", FinanceGlobal.FinURL);
                            data.Execute("UPDATE D_FIN_VOUCHER_DETAIL_EX SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + pCompanyID + " AND VOUCHER_NO='" + VoucherNo + "'", FinanceGlobal.FinURL);

                        }
                        
                        rsReceipts.next();
                    }
                }
//-----------------------------------------------------------------
                Cancelled = true;
            }
            rsTmp.close();
            rsIndent.close();
        } catch (Exception e) {
        }
        return Cancelled;
    }

    public static boolean IsDocExist(int CompanyID, String InvoiceNo, String InvoiceDate) {
        return data.IsRecordExist("SELECT INVOICE_NO FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE COMPANY_ID=" + CompanyID + " AND INVOICE_NO='" + InvoiceNo + "' AND SUBSTRING(INVOICE_DATE,1,10)='" + InvoiceDate + "' AND CANCELLED=0");
    }
    
    public static boolean IsFinalDocExist(int CompanyID, String InvoiceNo, String InvoiceDate) {
        return data.IsRecordExist("SELECT INVOICE_NO FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE COMPANY_ID=" + CompanyID + " AND INVOICE_NO='" + InvoiceNo + "' AND SUBSTRING(INVOICE_DATE,1,10)='" + InvoiceDate + "' AND APPROVED=1 AND CANCELLED=0");
    }

    public static double getInvoiceTotal(int CompanyID, String InvoiceNo, String InvoiceDate) {
//        return data.getDoubleValueFromDB("SELECT NET_AMT FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE COMPANY_ID=" + CompanyID + " AND INVOICE_NO='" + InvoiceNo + "' AND SUBSTRING(INVOICE_DATE,1,10)='" + InvoiceDate + "' ");
        return data.getDoubleValueFromDB("SELECT INVOICE_AMT FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE COMPANY_ID=" + CompanyID + " AND INVOICE_NO='" + InvoiceNo + "' AND SUBSTRING(INVOICE_DATE,1,10)='" + InvoiceDate + "' ");
    }

    public static String getInvoiceDate(int CompanyID, String InvoiceNo) {
        return data.getStringValueFromDB("SELECT SUBSTRING(INVOICE_DATE,1,10) AS INVOICE_DATE FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE COMPANY_ID=" + CompanyID + " AND INVOICE_NO='" + InvoiceNo + "'");
    }

    public static String getInvoiceNo(String AgentAlpha) {
        try {

            String[] arrAgentAlpha = AgentAlpha.split("/");

            String AreaID = arrAgentAlpha[0];
            String SrNo = arrAgentAlpha[1];
            int AgentSrNo = UtilFunctions.CInt(SrNo);

            String InvoiceNo = data.getStringValueFromDB("SELECT INVOICE_NO FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE AGENT_SR_NO=" + AgentSrNo + " AND PARTY_CODE IN (SELECT PARTY_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE AREA_ID='" + AreaID + "')");

            return InvoiceNo;
        } catch (Exception e) {
            return "";
        }
    }

//    public boolean PostSJTypeSuiting(int CompanyID,String InvoiceNo,String InvoiceDate,boolean AutoAdj) {
//        try {
//            //Get Object
//            clsFeltSalesInvoice objInvoice=(clsFeltSalesInvoice)(new clsFeltSalesInvoice()).getObject(CompanyID,InvoiceNo,InvoiceDate);
//            
//            clsVoucher objVoucher=new clsVoucher();
//            
//            double NetAmount=objInvoice.getAttribute("NET_AMOUNT").getDouble();
//            double HundiCharges=objInvoice.getAttribute("COLUMN_8_AMT").getDouble();
//            double InsuranceCharges=objInvoice.getAttribute("COLUMN_9_AMT").getDouble();
//            double BankCharges=objInvoice.getAttribute("COLUMN_10_AMT").getDouble();
//            double OtherCharges=objInvoice.getAttribute("COLUMN_12_AMT").getDouble();
//            double TCCharges=objInvoice.getAttribute("COLUMN_7_AMT").getDouble();
//            double CashDiscount=objInvoice.getAttribute("COLUMN_19_AMT").getDouble();
//            double VAT=objInvoice.getAttribute("COLUMN_11_AMT").getDouble();
//            double CST5=objInvoice.getAttribute("COLUMN_13_AMT").getDouble();
//            double Cess=0;
//            double GSTAmount=objInvoice.getAttribute("COLUMN_14_AMT").getDouble();
//            double RoundOff=EITLERPGLOBAL.round((HundiCharges+InsuranceCharges+BankCharges+OtherCharges+TCCharges+CashDiscount+Cess+GSTAmount+VAT+CST5)-NetAmount,2);
//            
//            NetAmount+=CashDiscount;
//            
//            double GrossAmount=NetAmount-(HundiCharges+InsuranceCharges+BankCharges+OtherCharges+Cess+GSTAmount+VAT+CST5); //+TCCharges
//            
//            String PartyCode=objInvoice.getAttribute("PARTY_CODE").getString();
//            //String InvoiceDate=objInvoice.getAttribute("INVOICE_DATE").getString();
//            String ICType=objInvoice.getAttribute("IC_TYPE").getString();
//            String SalesAccountCode="";
//            int WHCode=objInvoice.getAttribute("WAREHOUSE_CODE").getInt();
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
//            HashMap List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsFeltSalesInvoice.ModuleID, "CHOOSE_HIERARCHY", "INVOICE_TYPE", "1");
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
//            //            if(ICType.equals("1")) {
//            //                objVoucher.setAttribute("BOOK_CODE","01");
//            //                SalesAccountCode="301017";
//            //            }
//            
//            //            //FILTER FABRICS
//            //            if(ICType.equals("4")) {
//            //                objVoucher.setAttribute("BOOK_CODE","11");
//            //                SalesAccountCode="301347";
//            //            }
//            
//            //BLANKET SALES
//            //            if(ICType.equals("2")) {
//            //                objVoucher.setAttribute("BOOK_CODE","01");
//            //                SalesAccountCode="301079";
//            //            }
//            
//            
//            if(WHCode==1) {
//                objVoucher.setAttribute("BOOK_CODE","01");
//                SalesAccountCode="301017";
//            }
//            if(WHCode==0) {
//                objVoucher.setAttribute("BOOK_CODE","01");
//                SalesAccountCode="301018";
//            }
//            
//            if(WHCode==4) {
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
//            objVoucher.setAttribute("CHEQUE_DATE","");
//            objVoucher.setAttribute("BANK_NAME","");
//            objVoucher.setAttribute("PO_NO","");
//            objVoucher.setAttribute("PO_DATE","");
//            objVoucher.setAttribute("INVOICE_NO","");
//            objVoucher.setAttribute("INVOICE_DATE","");
//            objVoucher.setAttribute("GRN_NO","");
//            objVoucher.setAttribute("GRN_DATE","");
//            objVoucher.setAttribute("ST_CATEGORY","");
//            objVoucher.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
//                objVoucherItem.setAttribute("PO_DATE","");
//                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
//                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
//                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
//                objVoucherItem.setAttribute("GRN_NO","");
//                objVoucherItem.setAttribute("GRN_DATE","");
//                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
//                objVoucherItem.setAttribute("PO_DATE","");
//                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
//                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
//                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
//                objVoucherItem.setAttribute("GRN_NO","");
//                objVoucherItem.setAttribute("GRN_DATE","");
//                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
//                objVoucherItem.setAttribute("PO_DATE","");
//                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
//                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
//                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
//                objVoucherItem.setAttribute("GRN_NO","");
//                objVoucherItem.setAttribute("GRN_DATE","");
//                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
//                objVoucherItem.setAttribute("PO_DATE","");
//                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
//                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
//                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
//                objVoucherItem.setAttribute("GRN_NO","");
//                objVoucherItem.setAttribute("GRN_DATE","");
//                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
//                objVoucherItem.setAttribute("PO_DATE","");
//                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
//                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
//                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
//                objVoucherItem.setAttribute("GRN_NO","");
//                objVoucherItem.setAttribute("GRN_DATE","");
//                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
//                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
//                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
//                
//            }
//            
//            //(6) TCCharges
//            /*if(TCCharges>0) {
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
//                objVoucherItem.setAttribute("PO_DATE","");
//                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
//                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
//                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
//                objVoucherItem.setAttribute("GRN_NO","");
//                objVoucherItem.setAttribute("GRN_DATE","");
//                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
//                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
//                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
//             
//            }
//             */
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
//                objVoucherItem.setAttribute("PO_DATE","");
//                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
//                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
//                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
//                objVoucherItem.setAttribute("GRN_NO","");
//                objVoucherItem.setAttribute("GRN_DATE","");
//                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
//                objVoucherItem.setAttribute("PO_DATE","");
//                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
//                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
//                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
//                objVoucherItem.setAttribute("GRN_NO","");
//                objVoucherItem.setAttribute("GRN_DATE","");
//                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
//                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
//                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
//                
//            }
//            
//            if(CST5>0) {
//                VoucherSrNo++;
//                clsVoucherItem objVoucherItem=new clsVoucherItem();
//                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
//                objVoucherItem.setAttribute("EFFECT","C");
//                objVoucherItem.setAttribute("ACCOUNT_ID",1);
//                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","127550");
//                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
//                objVoucherItem.setAttribute("AMOUNT",CST5);
//                objVoucherItem.setAttribute("REMARKS","");
//                objVoucherItem.setAttribute("PO_NO","");
//                objVoucherItem.setAttribute("PO_DATE","");
//                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
//                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
//                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
//                objVoucherItem.setAttribute("GRN_NO","");
//                objVoucherItem.setAttribute("GRN_DATE","");
//                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
//                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
//                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
//            }
//            
//            //(10) GST Amount
//            if(VAT>0) {
//                VoucherSrNo++;
//                clsVoucherItem objVoucherItem=new clsVoucherItem();
//                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
//                objVoucherItem.setAttribute("EFFECT","C");
//                objVoucherItem.setAttribute("ACCOUNT_ID",1);
//                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","127561");
//                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
//                objVoucherItem.setAttribute("AMOUNT",VAT);
//                objVoucherItem.setAttribute("REMARKS","");
//                objVoucherItem.setAttribute("PO_NO","");
//                objVoucherItem.setAttribute("PO_DATE","");
//                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
//                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
//                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
//                objVoucherItem.setAttribute("GRN_NO","");
//                objVoucherItem.setAttribute("GRN_DATE","");
//                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
//                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
//                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
//            }
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
//            objVoucherItem.setAttribute("PO_DATE","");
//            objVoucherItem.setAttribute("LINK_NO",clsFeltSalesInvoice.getAgentAlphaSrNo(InvoiceNo,InvoiceDate,PartyCode));
//            objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
//            objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
//            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
//            objVoucherItem.setAttribute("GRN_NO","");
//            objVoucherItem.setAttribute("GRN_DATE","");
//            objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
//            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
//            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
//            
//            objVoucher.DoNotValidateAccounts=true;
//            
//            if(objVoucher.Insert()) {
//                
//                //SJ Posted. Automatically adjust advance receipt amount
//                String SJNo=objVoucher.getAttribute("VOUCHER_NO").getString();
//                data.Execute("UPDATE D_FIN_VOUCHER_HEADER SET APPROVED=1 AND APPROVED_DATE=CURDATE() WHERE VOUCHER_NO='"+SJNo+"'",FinanceGlobal.FinURL);
//                System.out.println("Suting SJ Posted "+SJNo);
//                if(AutoAdj) {
//                    (new clsDrAdjustment()).AutoAdjustReceiptAmount(EITLERPGLOBAL.gCompanyID,SJNo);
//                }
//                return true;
//            } else {
//                LastError=objVoucher.LastError;
//                return false;
//            }
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
    public boolean PostSJTypeFelt(int CompanyID, String InvoiceNo, String InvoiceDate, boolean AutoAdj, String pBaleNo, String pBaleDate) {
        try {
            //Get Object
            clsFeltSalesInvoice objInvoice = (clsFeltSalesInvoice) (new clsFeltSalesInvoice()).getObject(CompanyID, InvoiceNo, InvoiceDate);

            clsVoucher objVoucher = new clsVoucher();

            //double NetAmount = objInvoice.getAttribute("NET_AMT").getDouble();
            double NetAmount = objInvoice.getAttribute("INVOICE_AMT").getDouble();
            double NetAmount1 = objInvoice.getAttribute("NET_AMT").getDouble();
//            String ch = String.valueOf(objInvoice.getAttribute("CHARGE_CODE").getDouble());
            String ch = objInvoice.getAttribute("CHARGE_CODE").getString();
            //double HundiCharges=objInvoice.getAttribute("COLUMN_8_AMT").getDouble(); //This field to be removed, in Felt invoices, this field represents percentage of something.
            //double InsuranceCharges=objInvoice.getAttribute("COLUMN_9_AMT").getDouble(); //This field was mis-placed.
            //double OtherCharges=objInvoice.getAttribute("COLUMN_12_AMT").getDouble(); //To be included in Sales Gross Amount, no need to bifurcate
            //double TCCharges=objInvoice.getAttribute("COLUMN_7_AMT").getDouble(); //TCC Charges field is used to store insurance charges

            double InsuranceCharges = objInvoice.getAttribute("INSURANCE_AMT").getDouble();
            double BankCharges = objInvoice.getAttribute("BANK_CHARGES").getDouble();
            double CashDiscount = 0;
            double Cess = 0;
            double GSTAmount = objInvoice.getAttribute("GST_AMT").getDouble();
            double IGSTAmount = objInvoice.getAttribute("IGST_AMT").getDouble();
            double CGSTAmount = objInvoice.getAttribute("CGST_AMT").getDouble();
            double SGSTAmount = objInvoice.getAttribute("SGST_AMT").getDouble();
            double GSTCompCessAmount = objInvoice.getAttribute("GST_COMP_CESS_AMT").getDouble();
            //double VAT1=objInvoice.getAttribute("COLUMN_8_AMT").getDouble();
            double VAT1 = objInvoice.getAttribute("VAT1").getDouble();
            double VAT4 = objInvoice.getAttribute("VAT4").getDouble();
            double CST2 = objInvoice.getAttribute("CST2").getDouble();
            double CST5 = objInvoice.getAttribute("CST5").getDouble();
            double SD_AMT = objInvoice.getAttribute("SD_AMT").getDouble();
            
            double TCSAmount = objInvoice.getAttribute("TCS_AMT").getDouble();

            System.out.println(SD_AMT);
            //double RoundOff=EITLERPGLOBAL.round((InsuranceCharges+BankCharges+CashDiscount+Cess+GSTAmount+VAT1+VAT4+CST2+CST5)-NetAmount,2);
            //double RoundOff=EITLERPGLOBAL.round((InsuranceCharges+BankCharges+Cess+GSTAmount+VAT1+VAT4+CST2+CST5)-NetAmount,2);

            NetAmount += CashDiscount;
            NetAmount1 += CashDiscount;
            //double GrossAmount = objInvoice.getAttribute("GROSS_AMT").getDouble();
            //double GrossAmount = NetAmount - (InsuranceCharges + BankCharges + Cess + GSTAmount + VAT1 + VAT4 + CST2 + CST5);
            
//            double GrossAmount = NetAmount - (InsuranceCharges + IGSTAmount + CGSTAmount + SGSTAmount + GSTCompCessAmount);
            double GrossAmount = NetAmount - (InsuranceCharges + IGSTAmount + CGSTAmount + SGSTAmount + GSTCompCessAmount + TCSAmount);

            String PartyCode = objInvoice.getAttribute("PARTY_CODE").getString();
            //String InvoiceDate=objInvoice.getAttribute("INVOICE_DATE").getString();
            String ICType = "";
            String SalesAccountCode = "";

            String SelPrefix = "";
            String SelSuffix = "";
            int FFNo = 0;

            //****** Prepare Voucher Object ********//
            setAttribute("FIN_HIERARCHY_ID", 0);

            //(1) Select the Hierarchy
            HashMap List = clsApprovalRules.getApprovalRules(2, clsFeltSalesInvoice.ModuleID, "CHOOSE_HIERARCHY", "INVOICE_TYPE", "1");

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
            objVoucher.LoadDataEx(2);

            objVoucher.setAttribute("PREFIX", SelPrefix);
            objVoucher.setAttribute("SUFFIX", SelSuffix);
            objVoucher.setAttribute("FFNO", FFNo);
            objVoucher.setAttribute("COMPANY_ID", 2);
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

            System.out.println("Invoice " + InvoiceNo + " 1");
            objVoucher.setAttribute("VOUCHER_TYPE", FinanceGlobal.TYPE_SALES_JOURNAL);
            objVoucher.setAttribute("CHEQUE_NO", "");
            objVoucher.setAttribute("CHEQUE_DATE", "");
            objVoucher.setAttribute("BANK_NAME", "");
            objVoucher.setAttribute("PO_NO", "");
            objVoucher.setAttribute("PO_DATE", "");
            objVoucher.setAttribute("INVOICE_NO", "");
            objVoucher.setAttribute("INVOICE_DATE", "");
            objVoucher.setAttribute("GRN_NO", "");
            objVoucher.setAttribute("GRN_DATE", "");
            objVoucher.setAttribute("ST_CATEGORY", "");
            objVoucher.setAttribute("MODULE_ID", clsFeltSalesInvoice.ModuleID);
            objVoucher.setAttribute("REMARKS", "");

            objVoucher.setAttribute("HIERARCHY_ID", getAttribute("FIN_HIERARCHY_ID").getInt());
            int FirstUserID = data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID=" + getAttribute("FIN_HIERARCHY_ID").getInt() + " AND SR_NO=1");
            objVoucher.setAttribute("FROM", FirstUserID);
            objVoucher.setAttribute("TO", FirstUserID);
            objVoucher.setAttribute("FROM_REMARKS", "");
            objVoucher.setAttribute("APPROVAL_STATUS", "F"); //Hold Voucher
//            objVoucher.setAttribute("APPROVAL_STATUS", "H"); //Hold Voucher

            objVoucher.colVoucherItems.clear();

            //(1) Entry no.1 => Debit party with net amount
            //String PartyMainCode=clsBook.getBookMainCode(CompanyID, objVoucher.getAttribute("BOOK_CODE").getString());
            String PartyMainCode = "210010";

            System.out.println("Invoice " + InvoiceNo + " 2");
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
                objVoucherItem.setAttribute("PO_DATE", "");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "");
                objVoucherItem.setAttribute("MODULE_ID", clsFeltSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }
            System.out.println("Invoice " + InvoiceNo + " 3");
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
                objVoucherItem.setAttribute("PO_DATE", "");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "");
                objVoucherItem.setAttribute("MODULE_ID", clsFeltSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }
            System.out.println("Invoice " + InvoiceNo + " 4");
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
                objVoucherItem.setAttribute("PO_DATE", "");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "");
                objVoucherItem.setAttribute("MODULE_ID", clsFeltSalesInvoice.ModuleID);
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
                objVoucherItem.setAttribute("PO_DATE", "");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "");
                objVoucherItem.setAttribute("MODULE_ID", clsFeltSalesInvoice.ModuleID);
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
                objVoucherItem.setAttribute("PO_DATE", "");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "");
                objVoucherItem.setAttribute("MODULE_ID", clsFeltSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);

            }
            
            if (IGSTAmount > 0) {
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127570");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", IGSTAmount);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "");
                objVoucherItem.setAttribute("MODULE_ID", clsFeltSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }
            if (CGSTAmount > 0) {
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127566");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", CGSTAmount);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "");
                objVoucherItem.setAttribute("MODULE_ID", clsFeltSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }

            if (SGSTAmount > 0) {
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127568");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", SGSTAmount);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "");
                objVoucherItem.setAttribute("MODULE_ID", clsFeltSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }

            if (GSTCompCessAmount > 0) {
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "444444");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", GSTCompCessAmount);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "");
                objVoucherItem.setAttribute("MODULE_ID", clsFeltSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }
            
// Added TCS Amount ----------------------------            
            if (TCSAmount > 0) {
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127184");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
//                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", PartyCode);
                objVoucherItem.setAttribute("AMOUNT", TCSAmount);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "");
                objVoucherItem.setAttribute("MODULE_ID", clsFeltSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }
//-----------------------------
            if (VAT1 > 0) {
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127549");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", VAT1);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "");
                objVoucherItem.setAttribute("MODULE_ID", clsFeltSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }
            if (VAT4 > 0) {
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127548");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", VAT4);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "");
                objVoucherItem.setAttribute("MODULE_ID", clsFeltSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }

            if (CST2 > 0) {
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "127551");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", CST2);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "");
                objVoucherItem.setAttribute("MODULE_ID", clsFeltSalesInvoice.ModuleID);
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
                objVoucherItem.setAttribute("PO_DATE", "");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "");
                objVoucherItem.setAttribute("MODULE_ID", clsFeltSalesInvoice.ModuleID);
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
                objVoucherItem.setAttribute("PO_DATE", "");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "");
                objVoucherItem.setAttribute("MODULE_ID", clsFeltSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }

            if (SD_AMT > 0) {
                System.out.println("Invoice " + InvoiceNo + " 6");
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                //objVoucherItem=new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "132802");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", PartyCode);
                objVoucherItem.setAttribute("AMOUNT", SD_AMT);
                objVoucherItem.setAttribute("REMARKS", "SD");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "");
                objVoucherItem.setAttribute("MODULE_ID", clsFeltSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }

            if (SD_AMT > 0) {
                System.out.println("Invoice " + InvoiceNo + " 7");
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                //objVoucherItem=new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "D");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "132803");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", PartyCode);
                objVoucherItem.setAttribute("AMOUNT", SD_AMT);
                objVoucherItem.setAttribute("REMARKS", "SD");
                objVoucherItem.setAttribute("PO_NO", "");
                objVoucherItem.setAttribute("PO_DATE", "");
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", "");
                objVoucherItem.setAttribute("GRN_DATE", "");
                objVoucherItem.setAttribute("MODULE_ID", clsFeltSalesInvoice.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }

            objVoucher.DoNotValidateAccounts = true;
            System.out.println("Invoice " + InvoiceNo + " 9.");
            
            data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+frmFeltInvPro.gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + PartyCode + "', '"+InvoiceNo+"', '"+InvoiceDate+"', '6.1', 'POSTING : VOUCHER OF PARTY "+PartyCode+" FOR "+InvoiceNo+" DATED "+InvoiceDate+" ', '' ) ");

            if (objVoucher.Insert()) {
                System.out.println("Invoice " + InvoiceNo + " 10");
                //SJ Posted. Automatically adjust advance receipt amount
                String SJNo = objVoucher.getAttribute("VOUCHER_NO").getString();
                
                data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+frmFeltInvPro.gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + PartyCode + "', '"+InvoiceNo+"', '"+InvoiceDate+"', '6.2', 'POSTED : VOUCHER NO "+SJNo+" OF PARTY "+PartyCode+" FOR "+InvoiceNo+" DATED "+InvoiceDate+" ', '' ) ");
                
                String strSQL2 = "UPDATE D_FIN_VOUCHER_HEADER SET APPROVED=1,APPROVED_DATE=CURDATE() WHERE VOUCHER_NO='" + SJNo + "'";
                System.out.println("SQL QUERY : "+strSQL2);
                data.Execute(strSQL2, FinanceGlobal.FinURL);
                System.out.println("Felt Invoice SJ Posted " + SJNo);

                if (AutoAdj) {
                    System.out.println("sd amt : " + SD_AMT + " 13");
                    (new clsDrAdjustment()).AutoAdjustReceiptAmount(2, SJNo);

                }

                System.out.println("sd amt : " + SD_AMT + " 14");

                double availableAmount = clsAccount.get09AmountByParty("210010", PartyCode, InvoiceDate);

                System.out.println("Available Balance for SD " + availableAmount);

                if (availableAmount >= SD_AMT) {
                    if (SD_AMT > 0) {

                        if (AutoAdj) {
                            (new clsDrAdjustment()).AutoAdjustReceiptAmountSD(EITLERPGLOBAL.gCompanyID, SJNo);
                        }

                    }

                    System.out.println("sd amt : " + SD_AMT + " 15");

                    if (SD_AMT > 0 && ch.startsWith("09")) {
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
                data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+frmFeltInvPro.gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + PartyCode + "', '"+InvoiceNo+"', '"+InvoiceDate+"', '6.2', 'ERROR : VOUCHER POSTING FAILED OF PARTY "+PartyCode+" FOR "+InvoiceNo+" DATED "+InvoiceDate+" ', '' ) ");
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
            clsFeltSalesInvoice objInvoice = (clsFeltSalesInvoice) (new clsFeltSalesInvoice()).getObject(CompanyID, InvoiceNo, InvoiceDate);

            double SD_AMT = objInvoice.getAttribute("SD_AMT").getDouble();
//            double NetAmount = objInvoice.getAttribute("NET_AMT").getDouble();
            double NetAmount = objInvoice.getAttribute("INVOICE_AMT").getDouble();
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
            objVoucher.setAttribute("CHEQUE_DATE", "");
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
            objVoucherItem.setAttribute("PO_DATE", "");
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
            objVoucherItem.setAttribute("PO_DATE", "");
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

//    public boolean PostSJTypeFilter(int CompanyID,String InvoiceNo,String InvoiceDate, boolean AutoAdj) {
//        try {
//            //Get Object
//            clsFeltSalesInvoice objInvoice=(clsFeltSalesInvoice)(new clsFeltSalesInvoice()).getObject(CompanyID,InvoiceNo,InvoiceDate);
//            
//            clsVoucher objVoucher=new clsVoucher();
//            
//            double NetAmount=objInvoice.getAttribute("NET_AMOUNT").getDouble();
//            
//            double HundiCharges=objInvoice.getAttribute("COLUMN_9_AMT").getDouble();
//            double InsuranceCharges=objInvoice.getAttribute("COLUMN_7_AMT").getDouble();
//            double BankCharges=0;
//            //double Kharajat=objInvoice.getAttribute("COLUMN_10_AMT").getDouble()+objInvoice.getAttribute("COLUMN_6_AMT").getDouble();
//            double TCCharges=0;
//            //double CashDiscount=objInvoice.getAttribute("COLUMN_11_AMT").getDouble();
//            double Cess=objInvoice.getAttribute("COLUMN_3_AMT").getDouble();
//            double GSTAmount=0;
//            double VAT1=objInvoice.getAttribute("VAT1").getDouble();
//            double VAT4=objInvoice.getAttribute("VAT4").getDouble();
//            double CST2=objInvoice.getAttribute("CST2").getDouble();
//            double CST5=objInvoice.getAttribute("CST5").getDouble();
//            
//           // NetAmount+=CashDiscount;
//            double GrossAmount=NetAmount - (HundiCharges+InsuranceCharges+BankCharges+TCCharges+Cess+GSTAmount+VAT1+VAT4+CST2+CST5);
//            
//            String PartyCode=objInvoice.getAttribute("PARTY_CODE").getString();
//            //String InvoiceDate=objInvoice.getAttribute("INVOICE_DATE").getString();
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
//            HashMap List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsFeltSalesInvoice.ModuleID, "CHOOSE_HIERARCHY", "INVOICE_TYPE", "1");
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
//            objVoucher.setAttribute("CHEQUE_DATE","");
//            objVoucher.setAttribute("BANK_NAME","");
//            objVoucher.setAttribute("PO_NO","");
//            objVoucher.setAttribute("PO_DATE","");
//            objVoucher.setAttribute("INVOICE_NO","");
//            objVoucher.setAttribute("INVOICE_DATE","");
//            objVoucher.setAttribute("GRN_NO","");
//            objVoucher.setAttribute("GRN_DATE","");
//            objVoucher.setAttribute("ST_CATEGORY","");
//            objVoucher.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
//                objVoucherItem.setAttribute("PO_DATE","");
//                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
//                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
//                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
//                objVoucherItem.setAttribute("GRN_NO","");
//                objVoucherItem.setAttribute("GRN_DATE","");
//                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
//                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","427027");
//                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
//                objVoucherItem.setAttribute("AMOUNT",HundiCharges);
//                objVoucherItem.setAttribute("REMARKS","");
//                objVoucherItem.setAttribute("PO_NO","");
//                objVoucherItem.setAttribute("PO_DATE","");
//                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
//                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
//                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
//                objVoucherItem.setAttribute("GRN_NO","");
//                objVoucherItem.setAttribute("GRN_DATE","");
//                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
//                objVoucherItem.setAttribute("PO_DATE","");
//                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
//                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
//                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
//                objVoucherItem.setAttribute("GRN_NO","");
//                objVoucherItem.setAttribute("GRN_DATE","");
//                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
//                objVoucherItem.setAttribute("PO_DATE","");
//                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
//                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
//                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
//                objVoucherItem.setAttribute("GRN_NO","");
//                objVoucherItem.setAttribute("GRN_DATE","");
//                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
//                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
//                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
//                
//            }
//            
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
//                objVoucherItem.setAttribute("PO_DATE","");
//                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
//                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
//                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
//                objVoucherItem.setAttribute("GRN_NO","");
//                objVoucherItem.setAttribute("GRN_DATE","");
//                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
//                objVoucherItem.setAttribute("PO_DATE","");
//                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
//                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
//                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
//                objVoucherItem.setAttribute("GRN_NO","");
//                objVoucherItem.setAttribute("GRN_DATE","");
//                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
//                objVoucherItem.setAttribute("PO_DATE","");
//                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
//                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
//                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
//                objVoucherItem.setAttribute("GRN_NO","");
//                objVoucherItem.setAttribute("GRN_DATE","");
//                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
//                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
//                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
//                
//            }
//            
//            if(VAT1>0) {                
//                VoucherSrNo++;
//                clsVoucherItem objVoucherItem=new clsVoucherItem();
//                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
//                objVoucherItem.setAttribute("EFFECT","C");
//                objVoucherItem.setAttribute("ACCOUNT_ID",1);
//                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","127549");
//                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
//                objVoucherItem.setAttribute("AMOUNT",VAT1);
//                objVoucherItem.setAttribute("REMARKS","");
//                objVoucherItem.setAttribute("PO_NO","");
//                objVoucherItem.setAttribute("PO_DATE","");
//                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
//                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
//                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
//                objVoucherItem.setAttribute("GRN_NO","");
//                objVoucherItem.setAttribute("GRN_DATE","");
//                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
//                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
//                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
//                
//            }
//            
//            if(VAT4>0) {                
//                VoucherSrNo++;
//                clsVoucherItem objVoucherItem=new clsVoucherItem();
//                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
//                objVoucherItem.setAttribute("EFFECT","C");
//                objVoucherItem.setAttribute("ACCOUNT_ID",1);
//                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","127548");
//                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
//                objVoucherItem.setAttribute("AMOUNT",VAT4);
//                objVoucherItem.setAttribute("REMARKS","");
//                objVoucherItem.setAttribute("PO_NO","");
//                objVoucherItem.setAttribute("PO_DATE","");
//                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
//                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
//                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
//                objVoucherItem.setAttribute("GRN_NO","");
//                objVoucherItem.setAttribute("GRN_DATE","");
//                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
//                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
//                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
//                
//            }
//            
//            if(CST2>0) {                
//                VoucherSrNo++;
//                clsVoucherItem objVoucherItem=new clsVoucherItem();
//                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
//                objVoucherItem.setAttribute("EFFECT","C");
//                objVoucherItem.setAttribute("ACCOUNT_ID",1);
//                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","127551");
//                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
//                objVoucherItem.setAttribute("AMOUNT",CST2);
//                objVoucherItem.setAttribute("REMARKS","");
//                objVoucherItem.setAttribute("PO_NO","");
//                objVoucherItem.setAttribute("PO_DATE","");
//                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
//                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
//                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
//                objVoucherItem.setAttribute("GRN_NO","");
//                objVoucherItem.setAttribute("GRN_DATE","");
//                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
//                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
//                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
//                
//            }
//
//            if(CST5>0) {                
//                VoucherSrNo++;
//                clsVoucherItem objVoucherItem=new clsVoucherItem();
//                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
//                objVoucherItem.setAttribute("EFFECT","C");
//                objVoucherItem.setAttribute("ACCOUNT_ID",1);
//                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","127550");
//                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
//                objVoucherItem.setAttribute("AMOUNT",CST5);
//                objVoucherItem.setAttribute("REMARKS","");
//                objVoucherItem.setAttribute("PO_NO","");
//                objVoucherItem.setAttribute("PO_DATE","");
//                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
//                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
//                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
//                objVoucherItem.setAttribute("GRN_NO","");
//                objVoucherItem.setAttribute("GRN_DATE","");
//                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
//                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
//                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
//                
//            }
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
//            objVoucherItem.setAttribute("PO_DATE","");
//            objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
//            objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
//            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
//            objVoucherItem.setAttribute("GRN_NO","");
//            objVoucherItem.setAttribute("GRN_DATE","");
//            objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
//            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
//            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
//            
//            objVoucher.DoNotValidateAccounts=true;
//            if(objVoucher.Insert()) {                
//                //SJ Posted. Automatically adjust advance receipt amount
//                String SJNo=objVoucher.getAttribute("VOUCHER_NO").getString();
//                data.Execute("UPDATE D_FIN_VOUCHER_HEADER SET APPROVED=1 AND APPROVED_DATE=CURDATE() WHERE VOUCHER_NO='"+SJNo+"'",FinanceGlobal.FinURL);
//                System.out.println("Filter SJ Posted "+SJNo);
//                if(AutoAdj) {
//                    (new clsDrAdjustment()).AutoAdjustReceiptAmount(EITLERPGLOBAL.gCompanyID,SJNo);
//                }
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
    public static double getTurnoverAmount(String PartyCode, String FromDate, String ToDate) {
        try {
//            double NetAmount = data.getDoubleValueFromDB("SELECT SUM(NET_AMT) AS TOTAL_AMOUNT FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PARTY_CODE='" + PartyCode + "' AND SUBSTRING(INVOICE_DATE,1,10)>='" + FromDate + "' AND SUBSTRING(INVOICE_DATE1,10)<='" + ToDate + "'");
            double NetAmount = data.getDoubleValueFromDB("SELECT SUM(INVOICE_AMT) AS TOTAL_AMOUNT FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PARTY_CODE='" + PartyCode + "' AND SUBSTRING(INVOICE_DATE,1,10)>='" + FromDate + "' AND SUBSTRING(INVOICE_DATE1,10)<='" + ToDate + "'");

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
    //            clsFeltSalesInvoice objInvoice=(clsFeltSalesInvoice)(new clsFeltSalesInvoice()).getObject(CompanyID,InvoiceNo);
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
    //            HashMap List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsFeltSalesInvoice.ModuleID, "CHOOSE_HIERARCHY", "INVOICE_TYPE", "1");
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
    //            objVoucher.setAttribute("CHEQUE_DATE","");
    //            objVoucher.setAttribute("BANK_NAME","");
    //            objVoucher.setAttribute("PO_NO","");
    //            objVoucher.setAttribute("PO_DATE","");
    //            objVoucher.setAttribute("INVOICE_NO","");
    //            objVoucher.setAttribute("INVOICE_DATE","");
    //            objVoucher.setAttribute("GRN_NO","");
    //            objVoucher.setAttribute("GRN_DATE","");
    //            objVoucher.setAttribute("ST_CATEGORY","");
    //            objVoucher.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
    //                objVoucherItem.setAttribute("PO_DATE","");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","");
    //                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
    //                objVoucherItem.setAttribute("PO_DATE","");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","");
    //                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
    //                objVoucherItem.setAttribute("PO_DATE","");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","");
    //                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
    //                objVoucherItem.setAttribute("PO_DATE","");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","");
    //                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
    //                objVoucherItem.setAttribute("PO_DATE","");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","");
    //                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
    //                objVoucherItem.setAttribute("PO_DATE","");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","");
    //                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
    //                objVoucherItem.setAttribute("PO_DATE","");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","");
    //                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
    //                objVoucherItem.setAttribute("PO_DATE","");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","");
    //                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
    //            objVoucherItem.setAttribute("PO_DATE","");
    //            objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //            objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //            objVoucherItem.setAttribute("GRN_NO","");
    //            objVoucherItem.setAttribute("GRN_DATE","");
    //            objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
    //            clsFeltSalesInvoice objInvoice=(clsFeltSalesInvoice)(new clsFeltSalesInvoice()).getObject(CompanyID,InvoiceNo);
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
    //            HashMap List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsFeltSalesInvoice.ModuleID, "CHOOSE_HIERARCHY", "INVOICE_TYPE", "1");
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
    //            objVoucher.setAttribute("CHEQUE_DATE","");
    //            objVoucher.setAttribute("BANK_NAME","");
    //            objVoucher.setAttribute("PO_NO","");
    //            objVoucher.setAttribute("PO_DATE","");
    //            objVoucher.setAttribute("INVOICE_NO","");
    //            objVoucher.setAttribute("INVOICE_DATE","");
    //            objVoucher.setAttribute("GRN_NO","");
    //            objVoucher.setAttribute("GRN_DATE","");
    //            objVoucher.setAttribute("ST_CATEGORY","");
    //            objVoucher.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
    //                objVoucherItem.setAttribute("PO_DATE","");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","");
    //                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
    //                objVoucherItem.setAttribute("PO_DATE","");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","");
    //                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
    //                objVoucherItem.setAttribute("PO_DATE","");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","");
    //                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
    //                objVoucherItem.setAttribute("PO_DATE","");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","");
    //                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
    //                objVoucherItem.setAttribute("PO_DATE","");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","");
    //                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
    //                objVoucherItem.setAttribute("PO_DATE","");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","");
    //                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
    //                objVoucherItem.setAttribute("PO_DATE","");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","");
    //                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
    //                objVoucherItem.setAttribute("PO_DATE","");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","");
    //                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
    //            objVoucherItem.setAttribute("PO_DATE","");
    //            objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //            objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //            objVoucherItem.setAttribute("GRN_NO","");
    //            objVoucherItem.setAttribute("GRN_DATE","");
    //            objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
    //            clsFeltSalesInvoice objInvoice=(clsFeltSalesInvoice)(new clsFeltSalesInvoice()).getObject(CompanyID,InvoiceNo);
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
    //            HashMap List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsFeltSalesInvoice.ModuleID, "CHOOSE_HIERARCHY", "INVOICE_TYPE", "1");
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
    //            objVoucher.setAttribute("CHEQUE_DATE","");
    //            objVoucher.setAttribute("BANK_NAME","");
    //            objVoucher.setAttribute("PO_NO","");
    //            objVoucher.setAttribute("PO_DATE","");
    //            objVoucher.setAttribute("INVOICE_NO","");
    //            objVoucher.setAttribute("INVOICE_DATE","");
    //            objVoucher.setAttribute("GRN_NO","");
    //            objVoucher.setAttribute("GRN_DATE","");
    //            objVoucher.setAttribute("ST_CATEGORY","");
    //            objVoucher.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
    //                objVoucherItem.setAttribute("PO_DATE","");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","");
    //                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
    //                objVoucherItem.setAttribute("PO_DATE","");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","");
    //                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
    //                objVoucherItem.setAttribute("PO_DATE","");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","");
    //                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
    //                objVoucherItem.setAttribute("PO_DATE","");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","");
    //                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
    //                objVoucherItem.setAttribute("PO_DATE","");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","");
    //                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
    //                objVoucherItem.setAttribute("PO_DATE","");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","");
    //                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
    //                objVoucherItem.setAttribute("PO_DATE","");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","");
    //                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
    //                objVoucherItem.setAttribute("PO_DATE","");
    //                objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //                objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //                objVoucherItem.setAttribute("GRN_NO","");
    //                objVoucherItem.setAttribute("GRN_DATE","");
    //                objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
    //            objVoucherItem.setAttribute("PO_DATE","");
    //            objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
    //            objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
    //            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
    //            objVoucherItem.setAttribute("GRN_NO","");
    //            objVoucherItem.setAttribute("GRN_DATE","");
    //            objVoucherItem.setAttribute("MODULE_ID",clsFeltSalesInvoice.ModuleID);
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
            InvoiceType = data.getIntValueFromDB("SELECT INVOICE_TYPE FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE INVOICE_NO='" + InvoiceNo + "' AND SUBSTRING(INVOICE_DATE,1,10)='" + InvoiceDate + "' ");
        } catch (Exception e) {
            return InvoiceType;
        }
        return InvoiceType;
    }

    public static String getInvoiceChargeCode(String InvoiceNo,String InvoiceDate) {
        String ChargeCode="";
        try {
            ChargeCode= data.getStringValueFromDB("SELECT PAYMENT_TERM_CODE FROM DINESHMILLS.D_SAL_INVOICE_HEADER WHERE INVOICE_NO='"+InvoiceNo+"' AND INVOICE_DATE='"+InvoiceDate+"' ");
        } catch(Exception e) {
            return ChargeCode;
        }
        return ChargeCode;
    }
    
    public static String getInvoiceChargeCode_FSH(String InvoiceNo, String InvoiceDate) {
        String ChargeCode = "";
        try {
            ChargeCode = data.getStringValueFromDB("SELECT CHARGE_CODE FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE INVOICE_NO='" + InvoiceNo + "' AND SUBSTRING(INVOICE_DATE,1,10)='" + InvoiceDate + "' ");
        } catch (Exception e) {
            return ChargeCode;
        }
        return ChargeCode;
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
//            InvoiceAmount = data.getDoubleValueFromDB("SELECT NET_AMT FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE INVOICE_NO='" + InvoiceNo + "' "
            InvoiceAmount = data.getDoubleValueFromDB("SELECT INVOICE_AMT FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE INVOICE_NO='" + InvoiceNo + "' "
                    + "AND SUBSTRING(INVOICE_DATE,1,10)='" + InvoiceDate + "' AND APPROVED=1 AND CANCELLED=0 ");
        } catch (Exception e) {
            return InvoiceAmount;
        }
        return InvoiceAmount;
    }

    public static double getDebitNotePercentage(String ReceiptDate, int InvoiceType, String ChargeCode, String PartyCode) {
        double InterestPercentage = 0;
        try {
            InterestPercentage = data.getDoubleValueFromDB("SELECT INTEREST_PERCENTAGE FROM DINESHMILLS.D_SAL_CHARGECODE_DEBITNOTE_PERCENTAGE "
                    + "WHERE CHARGE_CODE='" + ChargeCode + "' AND INVOICE_TYPE=" + InvoiceType + " "
                    + "AND '" + ReceiptDate + "' BETWEEN FROM_DATE AND TO_DATE ");
            if (InvoiceType == 1 && (PartyCode.equals("300153") || PartyCode.equals("300157") || PartyCode.equals("300156") || PartyCode.equals("301156") || PartyCode.equals("307753") || PartyCode.equals("307757") || PartyCode.equals("300170"))) {//PARTYCODE 300170 ADDED BY ASHUTOSH ON 20/02/2014 AS PER REQUIREMNT FROM ATUL SHAH
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
                AgentAlphaSrNo = InvoiceNo.substring(1) + "/" + FinYear;
            } else {
                AgentAlphaSrNo = getAgentAlpha(PartyCode);
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
                AgentAlphaSrNo = getAgentAlpha(PartyCode);
                AgentAlphaSrNo = AgentAlphaSrNo + "/" + data.getStringValueFromDB("SELECT AGENT_SR_NO FROM D_SAL_INVOICE_HEADER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND INVOICE_NO ='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ");
                AgentAlphaSrNo = AgentAlphaSrNo + "/" + FinYear;
            }
        } catch (Exception e) {
            return AgentAlphaSrNo;
        }
        return AgentAlphaSrNo;
    }
    
    public static String getAgentAlpha(String PartyCode) {
        try {
            String AgentCode=PartyCode.substring(0,2)+"0000";
            
            return data.getStringValueFromDB("SELECT TRIM(AREA_ID) AS AREA_ID FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND PARTY_CODE='"+AgentCode+"' AND AREA_ID<>'' ");
        }
        catch(Exception e) {
            return "";
        }
    }

    public static int getCreditDays(String PartyCode, String MainCode) {
        int CreditDays = 0;
        try {
            CreditDays = data.getIntValueFromDB("SELECT (CREDIT_DAYS+EXTRA_CREDIT_DAYS) AS CREDIT_DAYS FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE APPROVED=1 AND CANCELLED=0 AND MAIN_ACCOUNT_CODE='" + MainCode + "' AND PARTY_CODE='" + PartyCode + "' ");
        } catch (Exception e) {
            return CreditDays;
        }
        return CreditDays;
    }

    public boolean IsEditable(int pCompanyID, String pDocNo, String pDocDt, int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";

        try {
            if (HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE INVOICE_NO='" + pDocNo + "' AND INVOICE_DATE='" + pDocDt + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=80 AND DOC_NO='" + pDocNo + "' AND DOC_DATE='" + pDocDt + "' AND USER_ID=" + Integer.toString(pUserID) + " AND STATUS='W'";
                tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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
            return false;
        }

    }

    public static HashMap getHistoryList(int CompanyID, String Docno) {
        HashMap List = new HashMap();
        ResultSet rsTmp;

        try {
            String strSQL = "SELECT * FROM PRODUCTION.FELT_SAL_INVOICE_HEADER_H WHERE INVOICE_NO='" + Docno + "'";
            rsTmp = data.getResult(strSQL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                while (!rsTmp.isAfterLast()) {
                    clsFeltSalesInvoice objParty = new clsFeltSalesInvoice();

                    objParty.setAttribute("INVOICE_NO", rsTmp.getString("INVOICE_NO"));
                    objParty.setAttribute("INVOICE_DATE", rsTmp.getString("INVOICE_DATE"));
                    objParty.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                    objParty.setAttribute("UPDATED_BY", rsTmp.getInt("UPDATED_BY"));
                    objParty.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                    objParty.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                    objParty.setAttribute("APPROVER_REMARKS", rsTmp.getString("APPROVER_REMARKS"));
                    objParty.setAttribute("FROM_IP", rsTmp.getString("FROM_IP"));

                    List.put(Integer.toString(List.size() + 1), objParty);
                    rsTmp.next();
                }
            }
            rsTmp.close();
            return List;
        } catch (Exception e) {
            return List;
        }
    }
    
    public static HashMap getHistoryList(int CompanyID, String DocNo, String DocDate) {
        HashMap List = new HashMap();
        ResultSet rsTmp;

        try {
            String strSQL = "SELECT * FROM PRODUCTION.FELT_SAL_INVOICE_HEADER_H WHERE INVOICE_NO='" + DocNo + "' AND SUBSTRING(INVOICE_DATE,1,10)='" + DocDate + "' ";
            rsTmp = data.getResult(strSQL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                while (!rsTmp.isAfterLast()) {
                    clsFeltSalesInvoice objParty = new clsFeltSalesInvoice();

                    objParty.setAttribute("INVOICE_NO", rsTmp.getString("INVOICE_NO"));
                    objParty.setAttribute("INVOICE_DATE", rsTmp.getString("INVOICE_DATE"));
                    objParty.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                    objParty.setAttribute("UPDATED_BY", rsTmp.getInt("UPDATED_BY"));
                    objParty.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                    objParty.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                    objParty.setAttribute("APPROVER_REMARKS", rsTmp.getString("APPROVER_REMARKS"));
                    objParty.setAttribute("FROM_IP", rsTmp.getString("FROM_IP"));

                    List.put(Integer.toString(List.size() + 1), objParty);
                    rsTmp.next();
                }
            }
            rsTmp.close();
            return List;
        } catch (Exception e) {
            return List;
        }
    }

    public boolean Update() {

        Statement stHistory, stHeader, stHDetail;
        ResultSet rsHistory, rsHeader, rsHDetail;
        boolean Validate = true;

        try {

            String AStatus = (String) getAttribute("APPROVAL_STATUS").getObj();
            if (AStatus.equals("R")) {
                Validate = false;
            }
            Validate = true;

            // ---- History Related Changes ------ //
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHDetail = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rsHistory = stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_SAL_INVOICE_HEADER_H WHERE INVOICE_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();

            //------------------------------------//
            String theDocNo = getAttribute("INVOICE_NO").getString();
            String theDocDate = getAttribute("INVOICE_DATE").getString().substring(0, 10);
            String theBaleNo = getAttribute("BALE_NO").getString();
            String theBaleDate = getAttribute("PACKING_DATE").getString();
            double theInvAmt = (double) getAttribute("INVOICE_AMT").getVal();

            //** --------------------------------**//
            rsResultSet.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
            rsResultSet.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE", getAttribute("MODIFIED_DATE").getString());
            //rsResultSet.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            rsResultSet.updateBoolean("CHANGED", true);
            rsResultSet.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED", false);
            rsResultSet.updateString("CANCEL_REASON", getAttribute("REJECTED_REMARKS").getString());
            rsResultSet.updateRow();

            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_SAL_INVOICE_HEADER_H WHERE INVOICE_NO='" + (String) getAttribute("INVOICE_NO").getObj() + "' AND INVOICE_DATE='"+ (String) getAttribute("INVOICE_DATE").getObj() +"' ");
            RevNo++;
            String RevDocNo = (String) getAttribute("INVOICE_NO").getObj();

            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO", RevNo); //Revision No. 1 in case of Insert
            //rsHistory.updateString("UPDATE_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateInt("UPDATED_BY", (int) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateTimeDB());
            rsHistory.updateString("APPROVER_REMARKS", getAttribute("REJECTED_REMARKS").getString());

            rsHistory.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateInt("INVOICE_TYPE", (int) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("INVOICE_NO", getAttribute("INVOICE_NO").getString());
            rsHistory.updateString("INVOICE_DATE", getAttribute("INVOICE_DATE").getString());
            rsHistory.updateString("BALE_NO", getAttribute("BALE_NO").getString());
            rsHistory.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsHistory.updateDouble("NO_OF_PIECES", (double) getAttribute("NO_OF_PIECES").getVal());
            rsHistory.updateString("DUE_DATE", getAttribute("DUE_DATE").getString());
            rsHistory.updateDouble("BAS_AMT", (double) getAttribute("BAS_AMT").getVal());
            rsHistory.updateDouble("GROSS_AMT", (double) getAttribute("GROSS_AMT").getVal());
            rsHistory.updateDouble("DISC_AMT", (double) getAttribute("DISC_AMT").getVal());
            rsHistory.updateDouble("DISC_BAS_AMT", (double) getAttribute("DISC_BAS_AMT").getVal());
            rsHistory.updateDouble("EXCISE", (double) getAttribute("EXCISE").getVal());
            rsHistory.updateDouble("IGST_PER", (double) getAttribute("IGST_PER").getVal());
            rsHistory.updateDouble("IGST_AMT", (double) getAttribute("IGST_AMT").getVal());
            rsHistory.updateDouble("CGST_PER", (double) getAttribute("CGST_PER").getVal());
            rsHistory.updateDouble("CGST_AMT", (double) getAttribute("CGST_AMT").getVal());
            rsHistory.updateDouble("SGST_PER", (double) getAttribute("SGST_PER").getVal());
            rsHistory.updateDouble("SGST_AMT", (double) getAttribute("SGST_AMT").getVal());
            rsHistory.updateDouble("GST_COMP_CESS_PER", (double) getAttribute("GST_COMP_CESS_PER").getVal());
            rsHistory.updateDouble("GST_COMP_CESS_AMT", (double) getAttribute("GST_COMP_CESS_AMT").getVal());
            rsHistory.updateDouble("SEAM_CHG", (double) getAttribute("SEAM_CHG").getVal());
            rsHistory.updateDouble("SEAM_CHG_PER", (double) getAttribute("SEAM_CHG_PER").getVal());
            rsHistory.updateDouble("INSURANCE_AMT", (double) getAttribute("INSURANCE_AMT").getVal());
            rsHistory.updateDouble("BANK_CHARGES", (double) getAttribute("BANK_CHARGES").getVal());
            rsHistory.updateDouble("CESS", (double) getAttribute("CESS").getVal());
            rsHistory.updateDouble("GST_AMT", (double) getAttribute("GST_AMT").getVal());
            rsHistory.updateDouble("CHEM_TRT_CHG", (double) getAttribute("CHEM_TRT_CHG").getVal());
            rsHistory.updateDouble("PIN_CHG", (double) getAttribute("PIN_CHG").getVal());
            rsHistory.updateDouble("SPIRAL_CHG", (double) getAttribute("SPIRAL_CHG").getVal());
            rsHistory.updateInt("INS_IND", (int) getAttribute("INS_IND").getVal());
            rsHistory.updateInt("CST", (int) getAttribute("CST").getVal());
            rsHistory.updateInt("VAT", (int) getAttribute("VAT").getVal());
            rsHistory.updateDouble("SD_AMT", (double) getAttribute("SD_AMT").getVal());
            rsHistory.updateDouble("NET_AMT", (double) getAttribute("NET_AMT").getVal());
            rsHistory.updateDouble("INVOICE_AMT", (double) getAttribute("INVOICE_AMT").getVal());
            rsHistory.updateInt("SQM_IND", (int) getAttribute("SQM_IND").getVal());
            rsHistory.updateDouble("OUT_STANDING_AMT", (double) getAttribute("OUT_STANDING_AMT").getVal());
            rsHistory.updateDouble("ADV_AMT", (double) getAttribute("ADV_AMT").getVal());
            rsHistory.updateDouble("GRP_CRITICAL_LIMIT", (double) getAttribute("GRP_CRITICAL_LIMIT").getVal());
            rsHistory.updateDouble("PARTY_CRITICAL_LIMIT", (double) getAttribute("PARTY_CRITICAL_LIMIT").getVal());
            rsHistory.updateString("CHARGE_CODE", getAttribute("CHARGE_CODE").getString());
            rsHistory.updateDouble("GRP_OUT_STANDING_AMT", (double) getAttribute("GRP_OUT_STANDING_AMT").getVal());
            rsHistory.updateString("GRP_MAIN_PARTY_CODE", getAttribute("GRP_MAIN_PARTY_CODE").getString());
            rsHistory.updateDouble("INV_CRITICAL_LIMIT_AMT", (double) getAttribute("INV_CRITICAL_LIMIT_AMT").getVal());
            rsHistory.updateDouble("CRITICAL_LIMIT_AMT", (double) getAttribute("CRITICAL_LIMIT_AMT").getVal());
            rsHistory.updateBoolean("INDICATOR", getAttribute("INDICATOR").getBool());
            rsHistory.updateBoolean("FLAG", getAttribute("FLAG").getBool());
            rsHistory.updateDouble("CST2", (double) getAttribute("CST2").getVal());
            rsHistory.updateDouble("CST5", (double) getAttribute("CST5").getVal());
            rsHistory.updateDouble("VAT1", (double) getAttribute("VAT1").getVal());
            rsHistory.updateDouble("VAT4", (double) getAttribute("VAT4").getVal());
            
            rsHistory.updateInt("APPROVED", (int) getAttribute("APPROVED").getVal());
            rsHistory.updateString("APPROVED_DATE", getAttribute("APPROVED_DATE").getString());
            rsHistory.updateInt("CANCELLED", (int) getAttribute("CANCELLED").getVal());
            rsHistory.updateString("REMARKS", getAttribute("REMARKS").getString());
            rsHistory.updateInt("LOT_NO", (int) getAttribute("LOT_NO").getVal());
            rsHistory.updateInt("TRANSPORTER_CODE", (int) getAttribute("TRANSPORTER_CODE").getVal());
            rsHistory.updateString("PACKING_DATE", getAttribute("PACKING_DATE").getString());
            rsHistory.updateString("GATEPASS_NO", getAttribute("GATEPASS_NO").getString());
            rsHistory.updateString("TAX_INV_NO", getAttribute("TAX_INV_NO").getString());
            rsHistory.updateString("RETAIL_INV_NO", getAttribute("RETAIL_INV_NO").getString());
           
            rsHistory.updateString("HSN_CODE", getAttribute("HSN_CODE").getString());
            rsHistory.updateString("ADV_DOC_NO", getAttribute("ADV_DOC_NO").getString());
            rsHistory.updateString("VEHICLE_NO", getAttribute("VEHICLE_NO").getString());

            rsHistory.updateDouble("ADV_RECEIVED_AMT", (double) getAttribute("ADV_RECEIVED_AMT").getVal());
            rsHistory.updateDouble("ADV_AGN_INV_AMT", (double) getAttribute("ADV_AGN_INV_AMT").getVal());
            rsHistory.updateDouble("ADV_AGN_IGST_AMT", (double) getAttribute("ADV_AGN_IGST_AMT").getVal());
            rsHistory.updateDouble("ADV_AGN_SGST_AMT", (double) getAttribute("ADV_AGN_SGST_AMT").getVal());
            rsHistory.updateDouble("ADV_AGN_CGST_AMT", (double) getAttribute("ADV_AGN_CGST_AMT").getVal());
            rsHistory.updateDouble("ADV_AGN_GST_COMP_CESS_AMT", (double) getAttribute("ADV_AGN_GST_COMP_CESS_AMT").getVal());
 
            rsHistory.updateString("DOCUMENT_THROUGH", getAttribute("DOCUMENT_THROUGH").getString());
            rsHistory.updateString("PRODUCT_CODE", getAttribute("PRODUCT_CODE").getString());
            rsHistory.updateString("PRODUCT_DESC", getAttribute("PRODUCT_DESC").getString());
            rsHistory.updateString("PIECE_NO", getAttribute("PIECE_NO").getString());
            rsHistory.updateString("MACHINE_NO", getAttribute("MACHINE_NO").getString());
            rsHistory.updateString("POSITION_NO", getAttribute("POSITION_NO").getString());
            rsHistory.updateString("POSITION_DESC", getAttribute("POSITION_DESC").getString());
            rsHistory.updateString("STYLE", getAttribute("STYLE").getString());
            rsHistory.updateFloat("LENGTH", (float) getAttribute("LENGTH").getVal());
            rsHistory.updateFloat("WIDTH", (float) getAttribute("WIDTH").getVal());
            rsHistory.updateFloat("GSM", (float) getAttribute("GSM").getVal());
            rsHistory.updateFloat("ACTUAL_WEIGHT", (float) getAttribute("ACTUAL_WEIGHT").getVal());
            rsHistory.updateFloat("SQMTR", (float) getAttribute("SQMTR").getVal());
            rsHistory.updateString("SYN_PER", getAttribute("SYN_PER").getString());
            rsHistory.updateDouble("RATE", (double) getAttribute("RATE").getVal());
            rsHistory.updateString("RATE_UNIT", getAttribute("RATE_UNIT").getString());
            rsHistory.updateString("TRANSPORTER_NAME", getAttribute("TRANSPORTER_NAME").getString());
            rsHistory.updateString("DESP_MODE", getAttribute("DESP_MODE").getString());
            rsHistory.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
            rsHistory.updateString("GSTIN_NO", getAttribute("GSTIN_NO").getString());
            rsHistory.updateString("ADDRESS1", getAttribute("ADDRESS1").getString());
            rsHistory.updateString("ADDRESS2", getAttribute("ADDRESS2").getString());
            rsHistory.updateString("CITY_NAME", getAttribute("CITY_NAME").getString());
            rsHistory.updateString("CITY_ID", getAttribute("CITY_ID").getString());
            rsHistory.updateString("DISPATCH_STATION", getAttribute("DISPATCH_STATION").getString());
            rsHistory.updateString("PLACE_OF_SUPPLY", getAttribute("PLACE_OF_SUPPLY").getString());
            rsHistory.updateDouble("DISC_PER", (double) getAttribute("DISC_PER").getVal());
            rsHistory.updateString("PINCODE", getAttribute("PINCODE").getString());
            rsHistory.updateString("PARTY_CHARGE_CODE", getAttribute("PARTY_CHARGE_CODE").getString());
            rsHistory.updateString("PARTY_BANK_NAME", getAttribute("PARTY_BANK_NAME").getString());
            rsHistory.updateString("PARTY_BANK_ADDRESS1", getAttribute("PARTY_BANK_ADDRESS1").getString());
            rsHistory.updateString("PARTY_BANK_ADDRESS2", getAttribute("PARTY_BANK_ADDRESS2").getString());

            rsHistory.updateDouble("SURCHARGE_PER", (double) getAttribute("SURCHARGE_PER").getVal());
            rsHistory.updateDouble("SURCHARGE_RATE", (double) getAttribute("SURCHARGE_RATE").getVal());
            rsHistory.updateDouble("GROSS_RATE", (double) getAttribute("GROSS_RATE").getVal());
            
            rsHistory.updateString("MOBILE_NO", getAttribute("MOBILE_NO").getString());
            rsHistory.updateString("DELIVERY_MODE", getAttribute("DELIVERY_MODE").getString());
            rsHistory.updateString("MATERIAL_CODE", getAttribute("MATERIAL_CODE").getString());
            rsHistory.updateFloat("TCS_PER", (float) getAttribute("TCS_PER").getVal());
            rsHistory.updateDouble("TCS_AMT", (double) getAttribute("TCS_AMT").getVal());
 
            rsHistory.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
            rsHistory.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            rsHistory.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsHistory.updateBoolean("CHANGED", true);
            rsHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            
            ResultSet rsTmp1=data.getResult("SELECT USER()");
            rsTmp1.first();
            String str = rsTmp1.getString(1);
            String str_split[] = str.split("@");
            
            rsHistory.updateString("FROM_IP",""+str_split[1]);
            rsHistory.insertRow();

            //======== Update the Approval Flow =========
            setAttribute("FROM", EITLERPGLOBAL.gUserID);

            //ApprovalFlow ObjFlow=new ApprovalFlow();
            clsFeltProductionApprovalFlow ObjFlow = new clsFeltProductionApprovalFlow();

            //ObjFlow.ModuleID=clsSales_Party.ModuleID;
            ObjFlow.ModuleID = clsFeltSalesInvoice.ModuleID;
            //ObjFlow.DocNo=(String)getAttribute("PARTY_CODE").getObj();
            ObjFlow.DocNo = (String) getAttribute("INVOICE_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("CREATED_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "PRODUCTION.FELT_SAL_INVOICE_HEADER";
            ObjFlow.IsCreator = false;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "INVOICE_NO";
            //String qry = "UPDATE FELT_DOC_DATA SET DOC_NO='"+getAttribute("PARTY_CODE").getString()+"' WHERE DOC_NO='"+getAttribute("OLD_PARTY_CODE").getString()+"' AND MODULE_ID="+clsSales_Party.ModuleID;
            //String qry = "UPDATE PRODUCTION.FELT_DOC_DATA SET DOC_NO='"+getAttribute("PARTY_CD").getString()+"' WHERE DOC_NO='"+getAttribute("OLD_PARTY_CODE").getString()+"' AND MODULE_ID="+clsOrderParty.ModuleID;
            //data.Execute(qry);

            //==== Handling Rejected Documents ==========//
            AStatus = (String) getAttribute("APPROVAL_STATUS").getObj();
            if (AStatus.equals("R")) {
                //Remove the Rejected Flag First
                //data.Execute("UPDATE D_INV_INDENT_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from FELT_DOC_DATA
                //data.Execute("DELETE FROM FELT_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=3 AND DOC_NO='"+ObjFlow.DocNo+"'");

                //ObjFlow.IsCreator=true;
                ObjFlow.To = (int) getAttribute("SEND_DOC_TO").getVal();
                ObjFlow.ExplicitSendTo = true;
            }
            //==========================================//

            //==== Handling Rejected Documents ==========//
            boolean IsRejected = getAttribute("REJECTED").getBool();

            if (IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.FELT_SAL_INVOICE_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE INVOICE_NO='" + ObjFlow.DocNo + "' AND INVOICE_DATE='"+ theDocDate +"' ");

                //Remove Old Records from FELT_DOC_DATA
                data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=" + clsFeltSalesInvoice.ModuleID + " AND DOC_NO='" + ObjFlow.DocNo + "' AND DOC_DATE='"+ theDocDate +"' ");

                ObjFlow.IsCreator = true;
            }
            //==========================================//

            if (ObjFlow.Status.equals("H")) {
                //Don't update the Flow if on hold
                if (IsRejected) {
                    ObjFlow.Status = "A";
                    ObjFlow.To = ObjFlow.From;
                    ObjFlow.UpdateFlow();
                }
            } else {
                if (!ObjFlow.UpdateFlow()) {
                    LastError = ObjFlow.LastError;
                }
            }

            if (ObjFlow.Status.equals("F")) {
                String SJNo = "";
                SJNo = data.getStringValueFromDB("SELECT DISTINCT VOUCHER_NO FROM FINANCE.D_FIN_VOUCHER_DETAIL WHERE INVOICE_NO='"+theDocNo+"' AND INVOICE_DATE='"+theDocDate+"' AND VOUCHER_NO LIKE 'SJ%'");
                data.Execute("UPDATE D_FIN_VOUCHER_HEADER SET APPROVED=1, APPROVED_DATE=CURDATE() WHERE VOUCHER_NO='"+SJNo+"'", FinanceGlobal.FinURL);
                
                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PIECE_STAGE='INVOICED',PR_INVOICE_AMOUNT='"+theInvAmt+"' WHERE PR_INVOICE_NO='"+theDocNo+"' AND PR_INVOICE_DATE='"+theDocDate+"'");
                
                data.Execute("UPDATE FINANCE.D_SAL_GSTR_INVOICE_ERP SET APPROVED=1 WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND INVOICE_NO='"+theDocNo+"' AND INVOICE_DATE='" +theDocDate+ "' ");
                
//                String strSQL = "INSERT INTO DINESHMILLS.D_SAL_INVOICE_HEADER ";
//                strSQL += "(COMPANY_ID,INVOICE_TYPE,INVOICE_NO,INVOICE_DATE,PARTY_CODE,PIECE_NO,DUE_DATE,PAYMENT_TERM_CODE,BALE_NO,TOTAL_GROSS_AMOUNT,TOTAL_NET_AMOUNT, ";
//                strSQL += "NET_AMOUNT,GROSS_WEIGHT,TRANSPORTER_CODE,PARTY_NAME,LENGTH,WIDTH,NO_OF_PIECES,COLUMN_1_AMT,COLUMN_1_CAPTION,COLUMN_3_AMT,COLUMN_3_CAPTION, ";
//                strSQL += "COLUMN_6_AMT,COLUMN_8_PER,COLUMN_8_AMT,COLUMN_8_CAPTION,COLUMN_9_AMT,COLUMN_9_CAPTION,COLUMN_10_CAPTION,COLUMN_11_AMT,COLUMN_12_AMT,COLUMN_13_AMT, ";
//                strSQL += "COLUMN_14_CAPTION,COLUMN_24_AMT,COLUMN_25_AMT,APPROVED,CANCELLED,CHANGED,VAT1,SD_AMT,TOT_INV_SD_AMT,CHANGED_DATE,CGST_PER,CGST_AMT,SGST_PER, ";
//                strSQL += "SGST_AMT,IGST_PER,IGST_AMT) ";
//                //strSQL += "VALUES ";
//                strSQL += "(SELECT COMPANY_ID,INVOICE_TYPE,INVOICE_NO,INVOICE_DATE,PARTY_CODE,PIECE_NO,DUE_DATE,CONCAT(SUBSTRING(CHARGE_CODE,2,1),SUBSTRING(DESP_MODE,2,1)),BALE_NO,BAS_AMT,NET_AMT,INVOICE_AMT,ACTUAL_WEIGHT, ";
//                strSQL += "TRANSPORTER_CODE,PARTY_NAME,LENGTH,WIDTH,NO_OF_PIECES,SGST_AMT,'SGST_AMT',CGST_AMT+IGST_AMT,'IGST_CGST_AMT',0,DISC_PER,0,'DISC_PER',INSURANCE_AMT, ";
//                strSQL += "'INS CHRG','BANK CHRG',0,0,0,'GST AMOUNT',CHEM_TRT_CHG,PIN_CHG,APPROVED,CANCELLED,CHANGED,0,0,0,CHANGED_DATE,CGST_PER,CGST_AMT,SGST_PER,SGST_AMT,IGST_PER,IGST_AMT ";
//                strSQL += "FROM PRODUCTION.FELT_SAL_INVOICE_HEADER ";
//                strSQL += "WHERE INVOICE_NO='"+theDocNo+"' AND INVOICE_DATE='"+theDocDate+"') ";
//                
//                System.out.println("Dineshmills SQL : "+strSQL);
//                data.Execute(strSQL);
//                
//                String strSQLdet = "INSERT INTO DINESHMILLS.D_SAL_INVOICE_DETAIL (COMPANY_ID, INVOICE_TYPE, INVOICE_NO, INVOICE_DATE, QUALITY_NO, PIECE_NO, PARTY_CODE, RATE, GROSS_SQ_MTR, GROSS_KG, GROSS_AMOUNT, TRD_DISCOUNT, NET_AMOUNT, HSN_CODE) ";
//                //strSQLdet += "VALUES ";
//                strSQLdet += "(SELECT 2,2,INVOICE_NO,SUBSTRING(INVOICE_DATE, 1, 10),PRODUCT_CODE,PIECE_NO,PARTY_CODE,RATE,SQMTR,ACTUAL_WEIGHT,BAS_AMT,DISC_AMT,INVOICE_AMT,5911 ";
//                strSQLdet += "FROM PRODUCTION.FELT_SAL_INVOICE_DETAIL ";
//                strSQLdet += "WHERE INVOICE_NO='"+theDocNo+"' AND SUBSTRING(INVOICE_DATE,1,10)='"+theDocDate+"') ";
//                
//                System.out.println("Dineshmills Detail SQL : "+strSQLdet);
//                data.Execute(strSQLdet);
//                
            }

            //--------- Approval Flow Update complete -----------
            
            //==== Handling Cancelled Documents ==========//
            if (AStatus.equals("N")) {
                clsFeltSalesInvoice.CancelDoc(EITLERPGLOBAL.gCompanyID, theDocNo, theDocDate);
                //data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PIECE_STAGE='CANCELED' WHERE PR_INVOICE_NO='"+theDocNo+"' AND PR_INVOICE_DATE='"+theDocDate+"'");
                data.Execute("UPDATE PRODUCTION.FELT_PKG_SLIP_HEADER SET INVOICE_FLG=0 WHERE PKG_BALE_NO='"+theBaleNo+"' AND PKG_BALE_DATE='"+theBaleDate+"'");
                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PIECE_STAGE='BSR',PR_INVOICE_NO='',PR_INVOICE_DATE='0000-00-00',PR_INVOICE_AMOUNT='0' WHERE PR_INVOICE_NO='"+theDocNo+"' AND PR_INVOICE_DATE='"+theDocDate+"'");
                
                String sql = "UPDATE DINESHMILLS.D_SAL_INVOICE_HEADER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND INVOICE_NO='"+theDocNo+"' AND INVOICE_DATE='" +theDocDate+ "' ";
                System.out.println("Changes Dineshmills query : "+sql);
                data.Execute(sql);
                
                data.Execute("UPDATE FINANCE.D_SAL_GSTR_INVOICE_ERP SET CANCELLED=1 WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND INVOICE_NO='"+theDocNo+"' AND INVOICE_DATE='" +theDocDate+ "' ");
                
            }
            
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }
    
    public boolean ShowHistory(int pCompanyID,String pDocNo) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SAL_INVOICE_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND INVOICE_NO='"+pDocNo+"'");
            Ready=true;
            HistoryView=true;
            MoveFirst();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public static HashMap getPendingApprovals(int pUserID, int pOrder) {
        String strSQL = "";

        HashMap List = new HashMap();
        long Counter = 0;

        try {

            if (pOrder == EITLERPGLOBAL.OnRecivedDate) {
                strSQL = "SELECT PRODUCTION.FELT_SAL_INVOICE_HEADER.INVOICE_NO,SUBSTRING(PRODUCTION.FELT_SAL_INVOICE_HEADER.INVOICE_DATE,1,10) AS INVOICE_DATE,RECEIVED_DATE,PRODUCTION.FELT_SAL_INVOICE_HEADER.BALE_NO,PRODUCTION.FELT_SAL_INVOICE_HEADER.PARTY_CODE,PRODUCTION.FELT_SAL_INVOICE_HEADER.PIECE_NO FROM PRODUCTION.FELT_SAL_INVOICE_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_SAL_INVOICE_HEADER.INVOICE_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_SAL_INVOICE_HEADER.INVOICE_DATE=PRODUCTION.FELT_PROD_DOC_DATA.DOC_DATE AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + pUserID + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=80 AND CANCELLED=0 ORDER BY PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT PRODUCTION.FELT_SAL_INVOICE_HEADER.INVOICE_NO,SUBSTRING(PRODUCTION.FELT_SAL_INVOICE_HEADER.INVOICE_DATE,1,10) AS INVOICE_DATE,RECEIVED_DATE,PRODUCTION.FELT_SAL_INVOICE_HEADER.BALE_NO,PRODUCTION.FELT_SAL_INVOICE_HEADER.PARTY_CODE,PRODUCTION.FELT_SAL_INVOICE_HEADER.PIECE_NO FROM PRODUCTION.FELT_SAL_INVOICE_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_SAL_INVOICE_HEADER.INVOICE_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_SAL_INVOICE_HEADER.INVOICE_DATE=PRODUCTION.FELT_PROD_DOC_DATA.DOC_DATE AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + pUserID + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=80 AND CANCELLED=0 ORDER BY PRODUCTION.FELT_SAL_INVOICE_HEADER.INVOICE_NO";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT PRODUCTION.FELT_SAL_INVOICE_HEADER.INVOICE_NO,SUBSTRING(PRODUCTION.FELT_SAL_INVOICE_HEADER.INVOICE_DATE,1,10) AS INVOICE_DATE,RECEIVED_DATE,PRODUCTION.FELT_SAL_INVOICE_HEADER.BALE_NO,PRODUCTION.FELT_SAL_INVOICE_HEADER.PARTY_CODE,PRODUCTION.FELT_SAL_INVOICE_HEADER.PIECE_NO FROM PRODUCTION.FELT_SAL_INVOICE_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_SAL_INVOICE_HEADER.INVOICE_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_SAL_INVOICE_HEADER.INVOICE_DATE=PRODUCTION.FELT_PROD_DOC_DATA.DOC_DATE AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + pUserID + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=80 AND CANCELLED=0 ORDER BY PRODUCTION.FELT_SAL_INVOICE_HEADER.INVOICE_NO";
            }

            ResultSet rsTmp = data.getConn().createStatement().executeQuery(strSQL);

            Counter = 0;
            while (rsTmp.next()) {

                Counter = Counter + 1;
                clsFeltSalesInvoice ObjDoc = new clsFeltSalesInvoice();

                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO", rsTmp.getString("INVOICE_NO"));
                ObjDoc.setAttribute("DOC_DATE", rsTmp.getString("INVOICE_DATE"));
                ObjDoc.setAttribute("RECEIVED_DATE", rsTmp.getString("RECEIVED_DATE"));
                ObjDoc.setAttribute("BALE_NO", rsTmp.getString("BALE_NO"));
                ObjDoc.setAttribute("PARTY_CODE", rsTmp.getString("PARTY_CODE"));
                
                String PartyCode=rsTmp.getString("PARTY_CODE");                
                String PartyName=clsAccount.getAccountName("210010",PartyCode); 
                ObjDoc.setAttribute("PARTY_NAME",PartyName);
                ObjDoc.setAttribute("PIECE_NO", rsTmp.getString("PIECE_NO"));
                // ----------------- End of Header Fields ------------------------------------//

                List.put(Long.toString(Counter), ObjDoc);
            }
            rsTmp.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return List;
    }


}
