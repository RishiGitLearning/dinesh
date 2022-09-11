/*
 * clsSales_Party.java
 *
 * Created on April 3, 2009, 10:22 AM
 */
package EITLERP.FeltSales.MachineRunForcasting;

import EITLERP.Production.ReportUI.*;
import java.util.*;
import java.util.Date;
import java.text.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import EITLERP.Finance.*;
import java.text.DecimalFormat;
import java.lang.Double;
import EITLERP.Production.clsFeltProductionApprovalFlow;

/**
 *
 * @author ashutosh
 */
public class clsMachineRunForcastingEntry {

    public String LastError = "";
    private ResultSet rsResultSet, rsResultSet1;
    private Connection Conn;
    private Statement Stmt;
    DecimalFormat df = new DecimalFormat("##.##");
    private HashMap props;
    public boolean Ready = false;

    private boolean HistoryView = false;
    public static int ModuleID = 851; //825 // //72
    public HashMap colMRItems;

    public Variant getAttribute(String PropName) {
        if (!props.containsKey(PropName)) {
            return new Variant("");
        } else {
            return (Variant) props.get(PropName);
        }
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
     * Creates a new instance of clsSales_Party
     */
    public clsMachineRunForcastingEntry() {
        LastError = "";
        props = new HashMap();
        //props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("DOC_NO", new Variant(""));
        props.put("YEAR_FROM", new Variant(""));
        props.put("YEAR_TO", new Variant(""));
        props.put("PARTY_NAME", new Variant(""));
        props.put("PATY_GROUP", new Variant(""));
        props.put("received_date", new Variant(""));
        props.put("action_date", new Variant(""));
        props.put("HIERARCHY_ID", new Variant(0));
        props.put("CREATED_BY", new Variant(0));
        props.put("APPROVED", new Variant(0));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED", new Variant(0));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("CANCELED", new Variant(0));
        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("PREFIX", new Variant(""));

        //Create a new object for MR Item collection
        colMRItems = new HashMap();

        // -- Approval Specific Fields --
        props.put("FROM", new Variant(0));
        props.put("TO", new Variant(0));
        props.put("REJECTED_REMARKS", new Variant(""));
        props.put("APPROVAL_STATUS", new Variant(""));
        props.put("SEND_DOC_TO", new Variant(0));
        props.put("APPROVER_REMARKS", new Variant(0));
    }

    /**
     * Load Data. This method loads data from database to Business Object*
     */
    public boolean LoadData(long pCompanyID) {
        Ready = false;
        try {

            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL    GROUP BY DOC_NO,YEAR_FROM  ORDER BY CREATED_DATE,DOC_NO ");
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

    public boolean Insert() {

        Statement stHistory, stHeader, stHDetail;
        ResultSet rsHistory, rsHeader, rsHDetail;
        String sql, sqlh;
        PreparedStatement pstm = null, pstmh = null;
        try {

            //======= Check the requisition date ============//
            java.sql.Date FinFromDate = java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate = java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);

            //=====================================================//
            // ---- History Related Changes ------ //
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHDetail = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rsHDetail = stHDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL_H WHERE DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//

            //--------- Generate Gatepass requisition no.  ------------
            setAttribute("DOC_NO", clsFirstFree.getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 851, (int) getAttribute("FFNO").getVal(), true));   //7
            //-------------------------------------------------
            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");

            //rsHistory.updateString("FROM_IP",""+str_split[1]);
            Statement tmpStmt;

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            Conn.setAutoCommit(false);
            rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE DOC_NO='1'");

            sql = "INSERT INTO PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL "
                    + "(DOC_NO,YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,"
                    + "PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,DRY_LENGTH,DRY_WIDTH,DRY_SQMTR,"
                    + "DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,Q1,Q2,Q3,Q4,"
                    + "PARTY_STATUS,REMARKS,HIERARCHY_ID,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,"
                    + "APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,REJECTED_REMARKS,CANCELED,CANCELED_DATE"
                    + ",CHANGED,CHANGED_DATE,Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,Q4KG,Q4SQMTR,TOTAL,"
                    + "GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,STYLE,TOTAL_KG,TOTAL_SQMTR,SYSTEM_STATUS"
                    + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,"
                    + "PREV_PREV_YEAR_VALUE,Q4NET_AMOUNT, "
                    + "WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE, "
                    + "LAST_INVOICE_DATE,AVG_LIFE,PROJECTED_MMYYYY,INCHARGE) ";
            sql = sql + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pstm = Conn.prepareStatement(sql);
            sqlh = "INSERT INTO PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL_H "
                    + "(DOC_NO,YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,"
                    + "PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,DRY_LENGTH,DRY_WIDTH,DRY_SQMTR,"
                    + "DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,"
                    + "Q1,Q2,Q3,Q4,PARTY_STATUS,REMARKS,HIERARCHY_ID,CREATED_BY,CREATED_DATE,"
                    + "MODIFIED_BY,MODIFIED_DATE,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,REJECTED_REMARKS,"
                    + "CANCELED,CANCELED_DATE,CHANGED,CHANGED_DATE,REVISION_NO,UPDATED_BY,APPROVAL_STATUS,"
                    + "ENTRY_DATE,APPROVER_REMARKS,FROM_IP,"
                    + "Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,Q4KG,Q4SQMTR,TOTAL,"
                    + "GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,STYLE,TOTAL_KG,TOTAL_SQMTR,SYSTEM_STATUS"
                    + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,"
                    + "PREV_PREV_YEAR_VALUE,Q4NET_AMOUNT, "
                    + "WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE, "
                    + "LAST_INVOICE_DATE,AVG_LIFE,PROJECTED_MMYYYY,INCHARGE) ";

            sqlh = sqlh + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pstmh = Conn.prepareStatement(sqlh);
            //Now Insert records into detail table
            for (int i = 1; i <= colMRItems.size(); i++) {
                clsMachineRunForcastingEntryItem ObjMRItems = (clsMachineRunForcastingEntryItem) colMRItems.get(Integer.toString(i));

                pstm.setString(1, (String) ObjMRItems.getAttribute("DOC_NO").getObj());
                setAttribute("DOC_NO", ObjMRItems.getAttribute("DOC_NO").getObj().toString());
                pstm.setString(2, (String) ObjMRItems.getAttribute("YEAR_FROM").getObj());
                pstm.setString(3, (String) ObjMRItems.getAttribute("YEAR_TO").getObj());
                pstm.setString(4, (String) ObjMRItems.getAttribute("PARTY_CODE").getObj());
                pstm.setString(5, (String) ObjMRItems.getAttribute("PARTY_NAME").getObj());
                pstm.setString(6, (String) ObjMRItems.getAttribute("MACHINE_NO").getObj());
                pstm.setString(7, (String) ObjMRItems.getAttribute("POSITION_NO").getObj());
                pstm.setString(8, (String) ObjMRItems.getAttribute("POSITION_DESC").getObj());
                pstm.setDouble(9, (double) ObjMRItems.getAttribute("PRESS_LENGTH").getVal());
                pstm.setDouble(10, (double) ObjMRItems.getAttribute("PRESS_WIDTH").getVal());
                pstm.setDouble(11, (double) ObjMRItems.getAttribute("PRESS_GSM").getVal());
                pstm.setDouble(12, (double) ObjMRItems.getAttribute("PRESS_WEIGHT").getVal());
                pstm.setDouble(13, (double) ObjMRItems.getAttribute("PRESS_SQMTR").getVal());
                pstm.setDouble(14, (double) ObjMRItems.getAttribute("DRY_LENGTH").getVal());
                pstm.setDouble(15, (double) ObjMRItems.getAttribute("DRY_WIDTH").getVal());
                pstm.setDouble(16, (double) ObjMRItems.getAttribute("DRY_SQMTR").getVal());
                pstm.setDouble(17, (double) ObjMRItems.getAttribute("DRY_WEIGHT").getVal());
                pstm.setString(18, (String) ObjMRItems.getAttribute("QUALITY_NO").getObj());
                pstm.setString(19, (String) ObjMRItems.getAttribute("GROUP_NAME").getObj());
                pstm.setDouble(20, (double) ObjMRItems.getAttribute("SELLING_PRICE").getVal());
                pstm.setDouble(21, (double) ObjMRItems.getAttribute("SPL_DISCOUNT").getVal());
                pstm.setDouble(22, (double) ObjMRItems.getAttribute("WIP").getVal());
                pstm.setDouble(23, (double) ObjMRItems.getAttribute("STOCK").getVal());
                pstm.setDouble(24, (double) ObjMRItems.getAttribute("Q1").getVal());
                pstm.setDouble(25, (double) ObjMRItems.getAttribute("Q2").getVal());
                pstm.setDouble(26, (double) ObjMRItems.getAttribute("Q3").getVal());
                pstm.setDouble(27, (double) ObjMRItems.getAttribute("Q4").getVal());
                pstm.setString(28, (String) ObjMRItems.getAttribute("PARTY_STATUS").getObj());
                pstm.setString(29, (String) ObjMRItems.getAttribute("REMARKS").getObj());
                pstm.setInt(30, (int) getAttribute("HIERARCHY_ID").getVal());
                pstm.setInt(31, (int) getAttribute("CREATED_BY").getVal());
                pstm.setString(32, (String) getAttribute("CREATED_DATE").getObj());
                pstm.setString(33, null);
                pstm.setString(34, null);
                pstm.setBoolean(35, false);
                pstm.setString(36, "0000-00-00");
                pstm.setBoolean(37, false);
                pstm.setString(38, "0000-00-00");
                pstm.setString(39, getAttribute("REJECTED_REMARKS").getString());
                pstm.setBoolean(40, false);
                pstm.setString(41, "0000-00-00");
                pstm.setBoolean(42, true);
                pstm.setString(43, EITLERPGLOBAL.getCurrentDateDB());
                pstm.setDouble(44, (double) ObjMRItems.getAttribute("Q1KG").getVal());
                pstm.setDouble(45, (double) ObjMRItems.getAttribute("Q1SQMTR").getVal());
                pstm.setDouble(46, (double) ObjMRItems.getAttribute("Q2KG").getVal());
                pstm.setDouble(47, (double) ObjMRItems.getAttribute("Q2SQMTR").getVal());
                pstm.setDouble(48, (double) ObjMRItems.getAttribute("Q3KG").getVal());
                pstm.setDouble(49, (double) ObjMRItems.getAttribute("Q3SQMTR").getVal());
                pstm.setDouble(50, (double) ObjMRItems.getAttribute("Q4KG").getVal());
                pstm.setDouble(51, (double) ObjMRItems.getAttribute("Q4SQMTR").getVal());
                pstm.setDouble(52, (double) ObjMRItems.getAttribute("TOTAL").getVal());
                pstm.setDouble(53, (double) ObjMRItems.getAttribute("GST_PER").getVal());
                pstm.setDouble(54, (double) ObjMRItems.getAttribute("GROSS_AMOUNT").getVal());
                pstm.setDouble(55, (double) ObjMRItems.getAttribute("DISCOUNT_AMOUNT").getVal());
                pstm.setDouble(56, (double) ObjMRItems.getAttribute("NET_AMOUNT").getVal());
                pstm.setString(57, (String) ObjMRItems.getAttribute("PP_REMARKS").getObj());
                pstm.setString(58, (String) ObjMRItems.getAttribute("STYLE").getObj());
                pstm.setDouble(59, (double) ObjMRItems.getAttribute("TOTAL_KG").getVal());
                pstm.setDouble(60, (double) ObjMRItems.getAttribute("TOTAL_SQMTR").getVal());
                pstm.setString(61, (String) ObjMRItems.getAttribute("SYSTEM_STATUS").getObj());
                pstm.setString(62, (String) ObjMRItems.getAttribute("UPN").getObj());
                pstm.setString(63, (String) ObjMRItems.getAttribute("POTENTIAL").getObj());
                pstm.setString(64, (String) ObjMRItems.getAttribute("PREV_YEAR_QTY").getObj());
                pstm.setString(65, (String) ObjMRItems.getAttribute("PREV_YEAR_VALUE").getObj());
                pstm.setString(66, (String) ObjMRItems.getAttribute("PREV_PREV_YEAR_QTY").getObj());
                pstm.setString(67, (String) ObjMRItems.getAttribute("PREV_PREV_YEAR_VALUE").getObj());
                pstm.setDouble(68, (double) ObjMRItems.getAttribute("Q4NET_AMOUNT").getVal());
                pstm.setDouble(69, (double) ObjMRItems.getAttribute("WIP_31_MAR_QTY").getVal());
                pstm.setDouble(70, (double) ObjMRItems.getAttribute("WIP_31_MAR_VALUE").getVal());
                pstm.setDouble(71, (double) ObjMRItems.getAttribute("STOCK_31_MAR_QTY").getVal());
                pstm.setDouble(72, (double) ObjMRItems.getAttribute("STOCK_31_MAR_VALUE").getVal());
                pstm.setString(73, (String) ObjMRItems.getAttribute("LAST_INVOICE_DATE").getObj());
                pstm.setDouble(74, (double) ObjMRItems.getAttribute("AVG_LIFE").getVal());
                pstm.setString(75, (String) ObjMRItems.getAttribute("PROJECTED_MMYYYY").getObj());
                pstm.setString(76, (String) ObjMRItems.getAttribute("INCHARGE").getObj());
                pstm.addBatch();

                pstmh.setString(1, (String) ObjMRItems.getAttribute("DOC_NO").getObj());
                pstmh.setString(2, (String) ObjMRItems.getAttribute("YEAR_FROM").getObj());
                pstmh.setString(3, (String) ObjMRItems.getAttribute("YEAR_TO").getObj());
                pstmh.setString(4, (String) ObjMRItems.getAttribute("PARTY_CODE").getObj());
                pstmh.setString(5, (String) ObjMRItems.getAttribute("PARTY_NAME").getObj());
                pstmh.setString(6, (String) ObjMRItems.getAttribute("MACHINE_NO").getObj());
                pstmh.setString(7, (String) ObjMRItems.getAttribute("POSITION_NO").getObj());
                pstmh.setString(8, (String) ObjMRItems.getAttribute("POSITION_DESC").getObj());
                pstmh.setDouble(9, (double) ObjMRItems.getAttribute("PRESS_LENGTH").getVal());
                pstmh.setDouble(10, (double) ObjMRItems.getAttribute("PRESS_WIDTH").getVal());
                pstmh.setDouble(11, (double) ObjMRItems.getAttribute("PRESS_GSM").getVal());
                pstmh.setDouble(12, (double) ObjMRItems.getAttribute("PRESS_WEIGHT").getVal());
                pstmh.setDouble(13, (double) ObjMRItems.getAttribute("PRESS_SQMTR").getVal());
                pstmh.setDouble(14, (double) ObjMRItems.getAttribute("DRY_LENGTH").getVal());
                pstmh.setDouble(15, (double) ObjMRItems.getAttribute("DRY_WIDTH").getVal());
                pstmh.setDouble(16, (double) ObjMRItems.getAttribute("DRY_SQMTR").getVal());
                pstmh.setDouble(17, (double) ObjMRItems.getAttribute("DRY_WEIGHT").getVal());
                pstmh.setString(18, (String) ObjMRItems.getAttribute("QUALITY_NO").getObj());
                pstmh.setString(19, (String) ObjMRItems.getAttribute("GROUP_NAME").getObj());
                pstmh.setDouble(20, (double) ObjMRItems.getAttribute("SELLING_PRICE").getVal());
                pstmh.setDouble(21, (double) ObjMRItems.getAttribute("SPL_DISCOUNT").getVal());
                pstmh.setDouble(22, (double) ObjMRItems.getAttribute("WIP").getVal());
                pstmh.setDouble(23, (double) ObjMRItems.getAttribute("STOCK").getVal());
                pstmh.setDouble(24, (double) ObjMRItems.getAttribute("Q1").getVal());
                pstmh.setDouble(25, (double) ObjMRItems.getAttribute("Q2").getVal());
                pstmh.setDouble(26, (double) ObjMRItems.getAttribute("Q3").getVal());
                pstmh.setDouble(27, (double) ObjMRItems.getAttribute("Q4").getVal());
                pstmh.setString(28, (String) ObjMRItems.getAttribute("PARTY_STATUS").getObj());
                pstmh.setString(29, (String) ObjMRItems.getAttribute("REMARKS").getObj());
                pstmh.setInt(30, (int) getAttribute("HIERARCHY_ID").getVal());
                pstmh.setInt(31, (int) getAttribute("CREATED_BY").getVal());
                pstmh.setString(32, (String) getAttribute("CREATED_DATE").getObj());
                pstmh.setString(33, null);
                pstmh.setString(34, null);
                pstmh.setBoolean(35, false);
                pstmh.setString(36, "0000-00-00");
                pstmh.setBoolean(37, false);
                pstmh.setString(38, "0000-00-00");
                pstmh.setString(39, getAttribute("REJECTED_REMARKS").getString());
                pstmh.setBoolean(40, false);
                pstmh.setString(41, "0000-00-00");
                pstmh.setBoolean(42, true);
                pstmh.setString(43, EITLERPGLOBAL.getCurrentDateDB());
                pstmh.setInt(44, 1);
                pstmh.setInt(45, (int) getAttribute("CREATED_BY").getVal());
                pstmh.setString(46, getAttribute("APPROVAL_STATUS").getString());
                pstmh.setString(47, data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                pstmh.setString(48, getAttribute("APPROVER_REMARKS").getString());
                pstmh.setString(49, str_split[1]);
                pstmh.setDouble(50, (double) ObjMRItems.getAttribute("Q1KG").getVal());
                pstmh.setDouble(51, (double) ObjMRItems.getAttribute("Q1SQMTR").getVal());
                pstmh.setDouble(52, (double) ObjMRItems.getAttribute("Q2KG").getVal());
                pstmh.setDouble(53, (double) ObjMRItems.getAttribute("Q2SQMTR").getVal());
                pstmh.setDouble(54, (double) ObjMRItems.getAttribute("Q3KG").getVal());
                pstmh.setDouble(55, (double) ObjMRItems.getAttribute("Q3SQMTR").getVal());
                pstmh.setDouble(56, (double) ObjMRItems.getAttribute("Q4KG").getVal());
                pstmh.setDouble(57, (double) ObjMRItems.getAttribute("Q4SQMTR").getVal());
                pstmh.setDouble(58, (double) ObjMRItems.getAttribute("TOTAL").getVal());
                pstmh.setDouble(59, (double) ObjMRItems.getAttribute("GST_PER").getVal());
                pstmh.setDouble(60, (double) ObjMRItems.getAttribute("GROSS_AMOUNT").getVal());
                pstmh.setDouble(61, (double) ObjMRItems.getAttribute("DISCOUNT_AMOUNT").getVal());
                pstmh.setDouble(62, (double) ObjMRItems.getAttribute("NET_AMOUNT").getVal());
                pstmh.setString(63, (String) ObjMRItems.getAttribute("PP_REMARKS").getObj());
                pstmh.setString(64, (String) ObjMRItems.getAttribute("STYLE").getObj());
                pstmh.setDouble(65, (double) ObjMRItems.getAttribute("TOTAL_KG").getVal());
                pstmh.setDouble(66, (double) ObjMRItems.getAttribute("TOTAL_SQMTR").getVal());
                pstmh.setString(67, (String) ObjMRItems.getAttribute("SYSTEM_STATUS").getObj());
                pstmh.setString(68, (String) ObjMRItems.getAttribute("UPN").getObj());
                pstmh.setString(69, (String) ObjMRItems.getAttribute("POTENTIAL").getObj());
                pstmh.setString(70, (String) ObjMRItems.getAttribute("PREV_YEAR_QTY").getObj());
                pstmh.setString(71, (String) ObjMRItems.getAttribute("PREV_YEAR_VALUE").getObj());
                pstmh.setString(72, (String) ObjMRItems.getAttribute("PREV_PREV_YEAR_QTY").getObj());
                pstmh.setString(73, (String) ObjMRItems.getAttribute("PREV_PREV_YEAR_VALUE").getObj());
                pstmh.setDouble(74, (double) ObjMRItems.getAttribute("Q4NET_AMOUNT").getVal());
                pstmh.setDouble(75, (double) ObjMRItems.getAttribute("WIP_31_MAR_QTY").getVal());
                pstmh.setDouble(76, (double) ObjMRItems.getAttribute("WIP_31_MAR_VALUE").getVal());
                pstmh.setDouble(77, (double) ObjMRItems.getAttribute("STOCK_31_MAR_QTY").getVal());
                pstmh.setDouble(78, (double) ObjMRItems.getAttribute("STOCK_31_MAR_VALUE").getVal());
                pstmh.setString(79, (String) ObjMRItems.getAttribute("LAST_INVOICE_DATE").getObj());
                pstmh.setDouble(80, (double) ObjMRItems.getAttribute("AVG_LIFE").getVal());
                pstmh.setString(81, (String) ObjMRItems.getAttribute("PROJECTED_MMYYYY").getObj());
                pstmh.setString(82, (String) ObjMRItems.getAttribute("INCHARGE").getObj());

                pstmh.addBatch();

                if ((i + 1) % 1000 == 0) {
                    pstm.executeBatch();
                    pstmh.executeBatch();
                    Conn.commit();
                }
            }
            pstm.executeBatch();
            pstmh.executeBatch();
            Conn.commit();
            Conn.setAutoCommit(true);
            String s;
            s = "UPDATE PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL "
                    + "SET KEY1=CONCAT(TRIM(YEAR_FROM),TRIM(YEAR_TO),TRIM(PARTY_CODE),TRIM(MACHINE_NO),"
                    + "TRIM(POSITION_NO)) "
                    + "WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "' ";
            data.Execute(s);
//            Update_Budget((String) getAttribute("DOC_NO").getObj());
            //===================== Update the Approval Flow ======================//
            //setAttribute("FROM", EITLERPGLOBAL.gUserID);

            //ApprovalFlow ObjFlow=new ApprovalFlow();
            clsFeltProductionApprovalFlow ObjFlow = new clsFeltProductionApprovalFlow();

            ObjFlow.ModuleID = clsMachineRunForcastingEntry.ModuleID;
            ObjFlow.DocNo = (String) getAttribute("DOC_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("CREATED_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "PRODUCTION.FELT_DETAIL";
            ObjFlow.IsCreator = true;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "DOC_NO";

            if (ObjFlow.Status.equals("H")) {
                ObjFlow.Status = "A";
                ObjFlow.To = ObjFlow.From;
                ObjFlow.UpdateFlow();
            } else {
                if (!ObjFlow.UpdateFlow()) {
                    LastError = ObjFlow.LastError;
                }
            }

            //================= Approval Flow Update complete ===================//
            MoveLast();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();

            return false;
        }

    }

    public boolean Update() {

        Statement stHistory, stHeader, stHDetail;
        ResultSet rsHistory, rsHeader, rsHDetail;
        boolean Validate = true;
        String sql, sqlh;
        PreparedStatement pstm = null, pstmh = null;
        try {

            String AStatus = (String) getAttribute("APPROVAL_STATUS").getObj();
            if (AStatus.equals("R")) {
                Validate = false;
            }
            Validate = true;
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate = java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate = java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);

            // ---- History Related Changes ------ //
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHDetail = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rsHDetail = stHDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL_H WHERE DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//

            //--------- Generate Gatepass requisition no.  ------------
            //-------------------------------------------------
            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");

            //rsHistory.updateString("FROM_IP",""+str_split[1]);
            Statement tmpStmt;

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            Conn.setAutoCommit(false);
            //rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE DOC_NO='1'");
            //int mcurmonth = data.getIntValueFromDB("SELECT MONTH(CURDATE()) FROM DUAL");
//            int mcurmonth = Integer.parseInt(getAttribute("DOC_NO").getObj().toString().substring(7, 9));
            int mcurmonth = Integer.parseInt(getAttribute("DOC_NO").getObj().toString().substring(8, 9));
            //int mcurmonth = 4;
            HashMap mmonth = new HashMap();
//            mmonth.put(1, "JAN");
//            mmonth.put(2, "FEB");
//            mmonth.put(3, "MAR");
//            mmonth.put(4, "APR");
//            mmonth.put(5, "MAY");
//            mmonth.put(6, "JUN");
//            mmonth.put(7, "JUL");
//            mmonth.put(8, "AUG");
//            mmonth.put(9, "SEP");
//            mmonth.put(10, "OCT");
//            mmonth.put(11, "NOV");
//            mmonth.put(12, "DEC");
            
            mmonth.put(1, "Q_1");
            mmonth.put(2, "Q_2");
            mmonth.put(3, "Q_3");
            mmonth.put(4, "Q_4");
            
            sql = "UPDATE PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL "
                    + "SET ";            
//            for (int a = 1; a <= 12; a++) {
//                sql = sql + mmonth.get(a) + "_EXP_RD=?," + mmonth.get(a) + "_EXP_RD_REMARK=?," ;
//                sql = sql + mmonth.get(a) + "_ACT_RD=?," + mmonth.get(a) + "_ACT_RD_REMARK=?," ;
//            } 
            for (int a = 1; a <= 4; a++) {
                sql = sql + mmonth.get(a) + "_EXP_RD=?," + mmonth.get(a) + "_EXP_RD_REMARK=?," ;
                sql = sql + mmonth.get(a) + "_ACT_RD=?," + mmonth.get(a) + "_ACT_RD_REMARK=?," ;
            } 
            sql = sql + "MODIFIED_BY=?,MODIFIED_DATE=? WHERE DOC_NO=? AND MACHINE_NO=? ";
            pstm = Conn.prepareStatement(sql);
            
            sqlh = "INSERT INTO PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL_H "
                    + "(DOC_NO,YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,";
//            for (int a = 1; a <= 12; a++) {
//                sqlh = sqlh + mmonth.get(a) + "_EXP_RD," + mmonth.get(a) + "_EXP_RD_REMARK," ;
//                sqlh = sqlh + mmonth.get(a) + "_ACT_RD," + mmonth.get(a) + "_ACT_RD_REMARK," ;
//            }
            for (int a = 1; a <= 4; a++) {
                sqlh = sqlh + mmonth.get(a) + "_EXP_RD," + mmonth.get(a) + "_EXP_RD_REMARK," ;
                sqlh = sqlh + mmonth.get(a) + "_ACT_RD," + mmonth.get(a) + "_ACT_RD_REMARK," ;
            }
            sqlh = sqlh + "HIERARCHY_ID,MODIFIED_BY,MODIFIED_DATE,REJECTED_REMARKS,REVISION_NO,APPROVAL_STATUS,ENTRY_DATE,APPROVER_REMARKS,FROM_IP,UPDATED_BY) ";

//            sqlh = sqlh + "VALUES(?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?)";
            sqlh = sqlh + "VALUES(?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?)";
            pstmh = Conn.prepareStatement(sqlh);
            
            int RevNo = 0;
            int cntNo = 1;
            int cntHNo = 1;
            //Now Insert records into detail table
            for (int i = 1; i <= colMRItems.size(); i++) {
                clsMachineRunForcastingEntryItem ObjMRItems = (clsMachineRunForcastingEntryItem) colMRItems.get(Integer.toString(i));
                if (i == 1) {
                    RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL_H WHERE DOC_NO='" + (String) ObjMRItems.getAttribute("DOC_NO").getObj() + "'");
                    RevNo++;
                }
                
                cntNo = 1;
                cntHNo = 1;           

                setAttribute("DOC_NO", ObjMRItems.getAttribute("DOC_NO").getObj().toString());
//                for (int a = 1; a <= 12; a++) {
//                    pstm.setDouble(cntNo++, (double) ObjMRItems.getAttribute(mmonth.get(a) + "_EXP_RD").getVal());
//                    pstm.setString(cntNo++, (String) ObjMRItems.getAttribute(mmonth.get(a) + "_EXP_RD_REMARK").getObj());
//                    pstm.setDouble(cntNo++, (double) ObjMRItems.getAttribute(mmonth.get(a) + "_ACT_RD").getVal());
//                    pstm.setString(cntNo++, (String) ObjMRItems.getAttribute(mmonth.get(a) + "_ACT_RD_REMARK").getObj());
//                }
                
                for (int a = 1; a <= 4; a++) {
                    pstm.setDouble(cntNo++, (double) ObjMRItems.getAttribute(mmonth.get(a) + "_EXP_RD").getVal());
                    pstm.setString(cntNo++, (String) ObjMRItems.getAttribute(mmonth.get(a) + "_EXP_RD_REMARK").getObj());
                    pstm.setDouble(cntNo++, (double) ObjMRItems.getAttribute(mmonth.get(a) + "_ACT_RD").getVal());
                    pstm.setString(cntNo++, (String) ObjMRItems.getAttribute(mmonth.get(a) + "_ACT_RD_REMARK").getObj());
                }
            
                pstm.setInt(cntNo++, (int) getAttribute("MODIFIED_BY").getVal());
                pstm.setString(cntNo++, EITLERPGLOBAL.getCurrentDateTimeDB());
                pstm.setString(cntNo++, ObjMRItems.getAttribute("DOC_NO").getObj().toString());
                pstm.setString(cntNo++, ObjMRItems.getAttribute("MACHINE_NO").getObj().toString());

                pstm.addBatch();

                pstmh.setString(cntHNo++, (String) ObjMRItems.getAttribute("DOC_NO").getObj());
                pstmh.setString(cntHNo++, (String) ObjMRItems.getAttribute("YEAR_FROM").getObj());
                pstmh.setString(cntHNo++, (String) ObjMRItems.getAttribute("YEAR_TO").getObj());
                pstmh.setString(cntHNo++, (String) ObjMRItems.getAttribute("PARTY_CODE").getObj());
                pstmh.setString(cntHNo++, (String) ObjMRItems.getAttribute("PARTY_NAME").getObj());
                pstmh.setString(cntHNo++, (String) ObjMRItems.getAttribute("MACHINE_NO").getObj());
//                for (int a = 1; a <= 12; a++) {
//                    pstmh.setDouble(cntHNo++, (double) ObjMRItems.getAttribute(mmonth.get(a) + "_EXP_RD").getVal());
//                    pstmh.setString(cntHNo++, (String) ObjMRItems.getAttribute(mmonth.get(a) + "_EXP_RD_REMARK").getObj());
//                    pstmh.setDouble(cntHNo++, (double) ObjMRItems.getAttribute(mmonth.get(a) + "_ACT_RD").getVal());
//                    pstmh.setString(cntHNo++, (String) ObjMRItems.getAttribute(mmonth.get(a) + "_ACT_RD_REMARK").getObj());
//                }
                
                for (int a = 1; a <= 4; a++) {
                    pstmh.setDouble(cntHNo++, (double) ObjMRItems.getAttribute(mmonth.get(a) + "_EXP_RD").getVal());
                    pstmh.setString(cntHNo++, (String) ObjMRItems.getAttribute(mmonth.get(a) + "_EXP_RD_REMARK").getObj());
                    pstmh.setDouble(cntHNo++, (double) ObjMRItems.getAttribute(mmonth.get(a) + "_ACT_RD").getVal());
                    pstmh.setString(cntHNo++, (String) ObjMRItems.getAttribute(mmonth.get(a) + "_ACT_RD_REMARK").getObj());
                }
            
                pstmh.setInt(cntHNo++, (int) getAttribute("HIERARCHY_ID").getVal());
                pstmh.setInt(cntHNo++, (int) getAttribute("MODIFIED_BY").getVal());
                pstmh.setString(cntHNo++, (String) getAttribute("MODIFIED_DATE").getObj());
                pstmh.setString(cntHNo++, getAttribute("REJECTED_REMARKS").getString());
                pstmh.setInt(cntHNo++, RevNo);
                pstmh.setString(cntHNo++, getAttribute("APPROVAL_STATUS").getString());
                pstmh.setString(cntHNo++, data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                pstmh.setString(cntHNo++, getAttribute("APPROVER_REMARKS").getString());
                pstmh.setString(cntHNo++, str_split[1]);
                pstmh.setInt(cntHNo++, (int) getAttribute("MODIFIED_BY").getVal());
                pstmh.addBatch();
                if ((i + 1) % 1000 == 0) {
                    pstm.executeBatch();
                    pstmh.executeBatch();
                    Conn.commit();
                }
            }
            pstm.executeBatch();
            pstmh.executeBatch();
            Conn.commit();
            Conn.setAutoCommit(true);

            String s;
            s = "UPDATE PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL D,PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL_H H "
                    + "SET D.CREATED_BY=H.CREATED_BY,D.CREATED_DATE=H.CREATED_DATE "
                    + "WHERE D.DOC_NO=H.DOC_NO AND REVISION_NO=1 AND "
                    + "D.DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "'";
            data.Execute(s);
            s = "UPDATE PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL "
                    + "SET KEY1=CONCAT(TRIM(YEAR_FROM),TRIM(YEAR_TO),TRIM(PARTY_CODE),TRIM(MACHINE_NO)) "
                    + "WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "' ";
            data.Execute(s);
            //Update_Budget((String) getAttribute("DOC_NO").getObj());

            //===================== Update the Approval Flow ======================//
            //setAttribute("FROM", EITLERPGLOBAL.gUserID);

            //ApprovalFlow ObjFlow=new ApprovalFlow();
            clsFeltProductionApprovalFlow ObjFlow = new clsFeltProductionApprovalFlow();

            ObjFlow.ModuleID = clsMachineRunForcastingEntry.ModuleID;
            ObjFlow.DocNo = (String) getAttribute("DOC_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("MODIFIED_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL";
            ObjFlow.IsCreator = false;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "DOC_NO";
            //==== Handling Rejected Documents ==========//
            AStatus = (String) getAttribute("APPROVAL_STATUS").getObj();
            if (AStatus.equals("R")) {
                ObjFlow.To = (int) getAttribute("SEND_DOC_TO").getVal();
                ObjFlow.ExplicitSendTo = true;
            }
            //==== Handling Rejected Documents ==========//
            boolean IsRejected = getAttribute("REJECTED").getBool();

            if (IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL SET REJECTED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='" + ObjFlow.DocNo + "' ");

                //Remove Old Records from FELT_DOC_DATA
                data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=" + clsMachineRunForcastingEntry.ModuleID + " AND DOC_NO='" + ObjFlow.DocNo + "'");

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

            //================= Approval Flow Update complete ===================//
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean setData() {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;

        tmpConn = data.getConn();

        long Counter = 0;
        int RevNo = 0;

        try {
            if (HistoryView) {
                RevNo = rsResultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO", rsResultSet.getInt("REVISION_NO"));
            } else {
                setAttribute("REVISION_NO", 0);
            }

            setAttribute("DOC_NO", rsResultSet.getString("DOC_NO"));

            setAttribute("HIERARCHY_ID", rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("CREATED_BY", rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE", rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY", rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE", rsResultSet.getString("MODIFIED_DATE"));
            setAttribute("APPROVED", rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE", rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED", rsResultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE", rsResultSet.getString("REJECTED_DATE"));
            setAttribute("CANCELED", rsResultSet.getInt("CANCELED"));
            setAttribute("REJECTED_REMARKS", rsResultSet.getString("REJECTED_REMARKS"));

            colMRItems.clear();

            String mDocNo = (String) getAttribute("DOC_NO").getObj();

            tmpStmt = tmpConn.createStatement();
            if (HistoryView) {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL_H WHERE DOC_NO='" + mDocNo + "' AND REVISION_NO=" + RevNo + "  ORDER BY DOC_NO");
            } else {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE DOC_NO='" + mDocNo + "'  ORDER BY DOC_NO");
            }
            rsTmp.first();

            Counter = 0;
            HashMap mmonth = new HashMap();
//            mmonth.put(1, "JAN");
//            mmonth.put(2, "FEB");
//            mmonth.put(3, "MAR");
//            mmonth.put(4, "APR");
//            mmonth.put(5, "MAY");
//            mmonth.put(6, "JUN");
//            mmonth.put(7, "JUL");
//            mmonth.put(8, "AUG");
//            mmonth.put(9, "SEP");
//            mmonth.put(10, "OCT");
//            mmonth.put(11, "NOV");
//            mmonth.put(12, "DEC");
            
            mmonth.put(1, "Q_1");
            mmonth.put(2, "Q_2");
            mmonth.put(3, "Q_3");
            mmonth.put(4, "Q_4");
            
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {
                Counter = Counter + 1;
                clsMachineRunForcastingEntryItem ObjMRItems = new clsMachineRunForcastingEntryItem();
                ObjMRItems.setAttribute("DOC_NO", rsTmp.getString("DOC_NO"));
                ObjMRItems.setAttribute("YEAR_FROM", rsTmp.getString("YEAR_FROM"));
                ObjMRItems.setAttribute("YEAR_TO", rsTmp.getString("YEAR_TO"));
                ObjMRItems.setAttribute("PARTY_CODE", rsTmp.getString("PARTY_CODE"));
                ObjMRItems.setAttribute("PARTY_NAME", rsTmp.getString("PARTY_NAME"));
                ObjMRItems.setAttribute("MACHINE_NO", rsTmp.getString("MACHINE_NO"));
                ObjMRItems.setAttribute("POSITION_NO", rsTmp.getString("POSITION_NO"));
                ObjMRItems.setAttribute("POSITION_DESC", rsTmp.getString("POSITION_DESC"));
                ObjMRItems.setAttribute("REMARKS", rsTmp.getString("REMARKS"));
//                for (int a = 1; a <= 12; a++) {
//                    ObjMRItems.setAttribute(mmonth.get(a) + "_EXP_RD", EITLERPGLOBAL.round(rsTmp.getDouble(mmonth.get(a) + "_EXP_RD"), 2));
//                    ObjMRItems.setAttribute(mmonth.get(a) + "_EXP_RD_REMARK", rsTmp.getString(mmonth.get(a) + "_EXP_RD_REMARK"));
//                    ObjMRItems.setAttribute(mmonth.get(a) + "_ACT_RD", EITLERPGLOBAL.round(rsTmp.getDouble(mmonth.get(a) + "_ACT_RD"), 2));
//                    ObjMRItems.setAttribute(mmonth.get(a) + "_ACT_RD_REMARK", rsTmp.getString(mmonth.get(a) + "_ACT_RD_REMARK"));
//                }
                
                for (int a = 1; a <= 4; a++) {
                    ObjMRItems.setAttribute(mmonth.get(a) + "_EXP_RD", EITLERPGLOBAL.round(rsTmp.getDouble(mmonth.get(a) + "_EXP_RD"), 2));
                    ObjMRItems.setAttribute(mmonth.get(a) + "_EXP_RD_REMARK", rsTmp.getString(mmonth.get(a) + "_EXP_RD_REMARK"));
                    ObjMRItems.setAttribute(mmonth.get(a) + "_ACT_RD", EITLERPGLOBAL.round(rsTmp.getDouble(mmonth.get(a) + "_ACT_RD"), 2));
                    ObjMRItems.setAttribute(mmonth.get(a) + "_ACT_RD_REMARK", rsTmp.getString(mmonth.get(a) + "_ACT_RD_REMARK"));
                }
                
                ObjMRItems.setAttribute("HIERARCHY_ID", rsTmp.getString("HIERARCHY_ID"));
                ObjMRItems.setAttribute("CREATED_BY", rsTmp.getLong("CREATED_BY"));
                ObjMRItems.setAttribute("CREATED_DATE", rsTmp.getString("CREATED_DATE"));
                ObjMRItems.setAttribute("MODIFIED_BY", rsTmp.getLong("MODIFIED_BY"));
                ObjMRItems.setAttribute("MODIFIED_DATE", rsTmp.getString("MODIFIED_DATE"));
                ObjMRItems.setAttribute("APPROVED", rsTmp.getString("APPROVED"));
                ObjMRItems.setAttribute("APPROVED_DATE", rsTmp.getString("APPROVED_DATE"));
                ObjMRItems.setAttribute("REJECTED", rsTmp.getString("REJECTED"));
                ObjMRItems.setAttribute("REJECTED_DATE", rsTmp.getString("REJECTED_DATE"));
                ObjMRItems.setAttribute("REJECTED_REMARKS", rsTmp.getString("REJECTED_REMARKS"));
                ObjMRItems.setAttribute("CANCELED", rsTmp.getString("CANCELED"));
                ObjMRItems.setAttribute("CANCELED_DATE", rsTmp.getString("CANCELED_DATE"));
                ObjMRItems.setAttribute("CHANGED", rsTmp.getString("CHANGED"));
                ObjMRItems.setAttribute("CHANGED_DATE", rsTmp.getString("CHANGED_DATE"));
                ObjMRItems.setAttribute("INCHARGE", rsTmp.getString("INCHARGE"));
                ObjMRItems.setAttribute("PARTY_GROUP", rsTmp.getString("PARTY_GROUP"));

                colMRItems.put(Long.toString(Counter), ObjMRItems);
                rsTmp.next();
            }
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean IsEditable(int pCompanyID, String pDocNo, int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";

        try {
            if (HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=851 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND STATUS='W'"; //825
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

    public static HashMap getHistoryList(int CompanyID, String DocNo) {
        HashMap List = new HashMap();
        ResultSet rsTmp;

        try {
            String strSQL = "SELECT * FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL_H WHERE DOC_NO='" + DocNo + "' GROUP BY REVISION_NO,DOC_NO ";
            rsTmp = data.getResult(strSQL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                while (!rsTmp.isAfterLast()) {
                    clsMachineRunForcastingEntry objParty = new clsMachineRunForcastingEntry();

                    objParty.setAttribute("DOC_NO", rsTmp.getString("DOC_NO"));
                    objParty.setAttribute("CREATED_DATE", rsTmp.getString("CREATED_DATE"));
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

    public boolean Filter(String Condition) {
        Ready = false;
        try {
            String strSQL = "";
            strSQL += "SELECT * FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE LEFT(DOC_NO,3)='MRF' AND " + Condition + " GROUP BY DOC_NO";

            //strSQL += "SELECT * FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE " + Condition + " GROUP BY DOC_NO";
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery(strSQL);

            if (!rsResultSet.first()) {
                strSQL = "SELECT * FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE CREATED_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND CREATED_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'  AND LEFT(DOC_NO,3)='MRF' GROUP BY DOC_NO,YEAR_FROM  ORDER BY DOC_NO";
                rsResultSet = Stmt.executeQuery(strSQL);
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

    public boolean CanDelete(int pCompanyID, String pDocNo, int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";

        try {
            if (HistoryView) {
                return false;
            }

            //First check that document is approved or not
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=851 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND TYPE='C' AND STATUS='W'"; //825
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

    public static void CancelDoc(String pAmendNo) {
        ResultSet rsTmp = null;

        if (CanCancel(pAmendNo)) {
            boolean Approved = false;

            try {
                rsTmp = data.getResult("SELECT APPROVED FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE DOC_NO='" + pAmendNo + "' ");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    Approved = rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();

                if (!Approved) {
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='" + pAmendNo + "' AND MODULE_ID=851"); //825
                }
                data.Execute("UPDATE PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='" + pAmendNo + "'");

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
            System.out.println("SELECT DOC_NO FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");
            rsTmp = stTmp.executeQuery("SELECT DOC_NO FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");

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

    //Deletes current record
    public boolean Delete(int pUserID) {
        try {
            int lCompanyID = EITLERPGLOBAL.gCompanyID;
            String lDocNo = (String) getAttribute("DOC_NO").getObj();
            String strSQL = "";

            //First check that record is editable
            if (CanDelete(lCompanyID, lDocNo, pUserID)) {
                String strQry = "DELETE FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE DOC_NO='" + lDocNo + "'";
                data.Execute(strQry);
                strQry = "DELETE FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE DOC_NO='" + lDocNo + "'";
                data.Execute(strQry);

                LoadData(lCompanyID);
                return true;
            } else {
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false;
            }
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public static HashMap getPendingApprovals(int pUserID, int pOrder) {
        String strSQL = "";
        Connection tmpConn;
        tmpConn = data.getConn();

        ResultSet rsTmp;
        Statement tmpStmt;

        HashMap List = new HashMap();
        long Counter = 0;

        try {
            if (pOrder == EITLERPGLOBAL.OnRecivedDate) {
                strSQL = "SELECT DISTINCT case when received_date='0000-00-00 00:00:00' then date_format(doc_date,'%d/%m/%Y') else date_format(received_date,'%d/%m/%Y %H:%i') end as received_date,case when action_date='0000-00-00 00:00:00' then '' else date_format(action_date,'%d/%m/%Y %H:%i') end as action_date,PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL.DOC_NO,PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL.PARTY_NAME,PARTY_GROUP FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=851 ORDER BY received_date"; // 825
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT case when received_date='0000-00-00 00:00:00' then date_format(doc_date,'%d/%m/%Y') else date_format(received_date,'%d/%m/%Y %H:%i') end as received_date,case when action_date='0000-00-00 00:00:00' then '' else date_format(action_date,'%d/%m/%Y %H:%i') end as action_date,PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL.DOC_NO,PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL.PARTY_NAME,PARTY_GROUP FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=851 ORDER BY action_date"; //825
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT case when received_date='0000-00-00 00:00:00' then date_format(doc_date,'%d/%m/%Y') else date_format(received_date,'%d/%m/%Y %H:%i') end as received_date,case when action_date='0000-00-00 00:00:00' then '' else date_format(action_date,'%d/%m/%Y %H:%i') end as action_date,PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL.DOC_NO,PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL.PARTY_NAME,PARTY_GROUP FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=851 ORDER BY PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL.DOC_NO"; //825 
            }

            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {

                Counter = Counter + 1;
                clsMachineRunForcastingEntry ObjItem = new clsMachineRunForcastingEntry();

                //------------- Header Fields --------------------//
                ObjItem.setAttribute("DOC_NO", rsTmp.getString("DOC_NO"));
                ObjItem.setAttribute("PARTY_NAME", rsTmp.getString("PARTY_NAME"));
                ObjItem.setAttribute("PARTY_GROUP", rsTmp.getString("PARTY_GROUP"));
                ObjItem.setAttribute("received_date", rsTmp.getString("received_date"));
                ObjItem.setAttribute("action_date", rsTmp.getString("action_date"));
                //ObjItem.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));                
                // ----------------- End of Header Fields ------------------------------------//

                List.put(Long.toString(Counter), ObjItem);

                if (!rsTmp.isAfterLast()) {
                    rsTmp.next();
                }
            }

            rsTmp.close();
            tmpStmt.close();

        } catch (Exception e) {
        }

        return List;
    }

    public boolean ShowHistory(int pCompanyID, String pDocNo) {
        Ready = false;
        try {
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsResultSet = Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL_H WHERE DOC_NO='" + pDocNo + "' AND LEFT(DOC_NO,3)='MRF' GROUP BY DOC_NO ORDER BY REVISION_NO");
            Ready = true;
            HistoryView = true;
            MoveFirst();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    private void Update_Budget(String mDocNo) {
        String sql;
        sql = "UPDATE PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL "
                + "SET TOTAL=COALESCE(Q1,0)+COALESCE(Q2,0)+COALESCE(Q3,0)+COALESCE(Q4,0) "
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL "
                + "SET TOTAL_KG=TOTAL*(COALESCE(PRESS_WEIGHT,0)+COALESCE(DRY_WEIGHT,0)),"
                + "TOTAL_SQMTR=TOTAL*(COALESCE(PRESS_SQMTR,0)+COALESCE(DRY_SQMTR,0)) "
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL "
                + "SET GROSS_AMOUNT=ROUND(((CASE WHEN LEFT(QUALITY_NO,1)='7' THEN TOTAL_SQMTR ELSE TOTAL_KG END)*SELLING_PRICE)*1.12,2) "
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL "
                + "SET DISCOUNT_AMOUNT=ROUND(GROSS_AMOUNT*(SPL_DISCOUNT/100),2) "
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL "
                + "SET NET_AMOUNT=GROSS_AMOUNT-DISCOUNT_AMOUNT"
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL "
                + "SET Q1GROSS=ROUND(((CASE WHEN LEFT(QUALITY_NO,1)='7' THEN (COALESCE(PRESS_SQMTR,0)+COALESCE(DRY_SQMTR,0))*Q1 ELSE (COALESCE(PRESS_WEIGHT,0)+COALESCE(DRY_WEIGHT,0))*Q1 END)*SELLING_PRICE)*1.12,2),"
                + "Q2GROSS=ROUND(((CASE WHEN LEFT(QUALITY_NO,1)='7' THEN (COALESCE(PRESS_SQMTR,0)+COALESCE(DRY_SQMTR,0))*Q2 ELSE (COALESCE(PRESS_WEIGHT,0)+COALESCE(DRY_WEIGHT,0))*Q2 END)*SELLING_PRICE)*1.12,2),"
                + "Q3GROSS=ROUND(((CASE WHEN LEFT(QUALITY_NO,1)='7' THEN (COALESCE(PRESS_SQMTR,0)+COALESCE(DRY_SQMTR,0))*Q3 ELSE (COALESCE(PRESS_WEIGHT,0)+COALESCE(DRY_WEIGHT,0))*Q3 END)*SELLING_PRICE)*1.12,2),"
                + "Q4GROSS=ROUND(((CASE WHEN LEFT(QUALITY_NO,1)='7' THEN (COALESCE(PRESS_SQMTR,0)+COALESCE(DRY_SQMTR,0))*Q4 ELSE (COALESCE(PRESS_WEIGHT,0)+COALESCE(DRY_WEIGHT,0))*Q4 END)*SELLING_PRICE)*1.12,2) "
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL "
                + "SET Q1DISCOUNT=ROUND(Q1GROSS*(SPL_DISCOUNT/100),2),"
                + "Q2DISCOUNT=ROUND(Q2GROSS*(SPL_DISCOUNT/100),2),"
                + "Q3DISCOUNT=ROUND(Q3GROSS*(SPL_DISCOUNT/100),2),"
                + "Q4DISCOUNT=ROUND(Q4GROSS*(SPL_DISCOUNT/100),2)  "
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL "
                + "SET Q1NET_AMOUNT=ROUND(Q1GROSS-Q1DISCOUNT,2),"
                + "Q2NET_AMOUNT=ROUND(Q2GROSS-Q2DISCOUNT,2),"
                + "Q3NET_AMOUNT=ROUND(Q3GROSS-Q3DISCOUNT,2),"
                + "Q4NET_AMOUNT=ROUND(Q4GROSS-Q4DISCOUNT,2) "
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);

        sql = "UPDATE PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL_H "
                + "SET TOTAL=COALESCE(Q1,0)+COALESCE(Q2,0)+COALESCE(Q3,0)+COALESCE(Q4,0) "
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL_H "
                + "SET TOTAL_KG=TOTAL*(COALESCE(PRESS_WEIGHT,0)+COALESCE(DRY_WEIGHT,0)),"
                + "TOTAL_SQMTR=TOTAL*(COALESCE(PRESS_SQMTR,0)+COALESCE(DRY_SQMTR,0)) "
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL_H "
                + "SET GROSS_AMOUNT=ROUND(((CASE WHEN LEFT(QUALITY_NO,1)='7' THEN TOTAL_SQMTR ELSE TOTAL_KG END)*SELLING_PRICE)*1.12,2) "
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL_H "
                + "SET DISCOUNT_AMOUNT=ROUND(GROSS_AMOUNT*(SPL_DISCOUNT/100),2) "
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL_H "
                + "SET NET_AMOUNT=GROSS_AMOUNT-DISCOUNT_AMOUNT"
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL_H "
                + "SET Q1GROSS=ROUND(((CASE WHEN LEFT(QUALITY_NO,1)='7' THEN (COALESCE(PRESS_SQMTR,0)+COALESCE(DRY_SQMTR,0))*Q1 ELSE (COALESCE(PRESS_WEIGHT,0)+COALESCE(DRY_WEIGHT,0))*Q1 END)*SELLING_PRICE)*1.12,2),"
                + "Q2GROSS=ROUND(((CASE WHEN LEFT(QUALITY_NO,1)='7' THEN (COALESCE(PRESS_SQMTR,0)+COALESCE(DRY_SQMTR,0))*Q2 ELSE (COALESCE(PRESS_WEIGHT,0)+COALESCE(DRY_WEIGHT,0))*Q2 END)*SELLING_PRICE)*1.12,2),"
                + "Q3GROSS=ROUND(((CASE WHEN LEFT(QUALITY_NO,1)='7' THEN (COALESCE(PRESS_SQMTR,0)+COALESCE(DRY_SQMTR,0))*Q3 ELSE (COALESCE(PRESS_WEIGHT,0)+COALESCE(DRY_WEIGHT,0))*Q3 END)*SELLING_PRICE)*1.12,2),"
                + "Q4GROSS=ROUND(((CASE WHEN LEFT(QUALITY_NO,1)='7' THEN (COALESCE(PRESS_SQMTR,0)+COALESCE(DRY_SQMTR,0))*Q4 ELSE (COALESCE(PRESS_WEIGHT,0)+COALESCE(DRY_WEIGHT,0))*Q4 END)*SELLING_PRICE)*1.12,2) "
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL_H "
                + "SET Q1DISCOUNT=ROUND(Q1GROSS*(SPL_DISCOUNT/100),2),"
                + "Q2DISCOUNT=ROUND(Q2GROSS*(SPL_DISCOUNT/100),2),"
                + "Q3DISCOUNT=ROUND(Q3GROSS*(SPL_DISCOUNT/100),2),"
                + "Q4DISCOUNT=ROUND(Q4GROSS*(SPL_DISCOUNT/100),2)  "
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL_H "
                + "SET Q1NET_AMOUNT=ROUND(Q1GROSS-Q1DISCOUNT,2),"
                + "Q2NET_AMOUNT=ROUND(Q2GROSS-Q2DISCOUNT,2),"
                + "Q3NET_AMOUNT=ROUND(Q3GROSS-Q3DISCOUNT,2),"
                + "Q4NET_AMOUNT=ROUND(Q4GROSS-Q4DISCOUNT,2) "
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);

    }
}
