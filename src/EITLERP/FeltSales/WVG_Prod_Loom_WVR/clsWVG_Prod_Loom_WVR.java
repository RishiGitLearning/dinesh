/*
     * clsSales_Party.java
     *
     * Created on April 3, 2009, 10:22 AM
 */
package EITLERP.FeltSales.WVG_Prod_Loom_WVR;

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
public class clsWVG_Prod_Loom_WVR {

    public String LastError = "";
    private ResultSet rsResultSet, rsResultSet1;
    private Connection Conn;
    private Statement Stmt;
    DecimalFormat df = new DecimalFormat("##.##");
    private HashMap props;
    public boolean Ready = false;

    private boolean HistoryView = false;
    public static int ModuleID = 820; //72
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
    public clsWVG_Prod_Loom_WVR() {
        LastError = "";
        props = new HashMap();
        //props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("DOC_NO", new Variant(""));
        props.put("YEAR_FROM", new Variant(""));
        props.put("YEAR_TO", new Variant(""));

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
            System.out.println("Load Qry : SELECT * FROM PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL GROUP BY DOC_NO  ORDER BY CREATED_DATE,DOC_NO ");
            rsResultSet = Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL GROUP BY DOC_NO  ORDER BY CREATED_DATE,DOC_NO ");
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

            rsHDetail = stHDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL_H WHERE DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//

            //--------- Generate Gatepass requisition no.  ------------
            setAttribute("DOC_NO", clsFirstFree.getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 820, (int) getAttribute("FFNO").getVal(), true));
            //-------------------------------------------------
            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");

            //rsHistory.updateString("FROM_IP",""+str_split[1]);
            

            
            Conn.setAutoCommit(false);
            

            sql = "INSERT INTO PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL "
                    + "(DOC_NO, DOC_DATE, SHIFT_ID, EMP_NAME, EMP_TYPE, EMP_NO, REG_ROKDI, CATOGORY_WISE_ENG, LOOM_ENG, BEAM_WARP_SR_NO, PRODUCT_GROUP, REED_SPACE_METER, PIECE_NO, WEFT_DETAILS, SHIFT_STARTING, IST_RECESS_OUT, IST_RECESS_IN, IIND_RECESS_OUT, IIND_RECESS_IN, IIIRD_RECESS_OUT, IIIRD_RECESS_IN, SHIFT_END_OUT, GATE_PASS_OUT, GATE_PASS_IN, MC_RPM, PICKS_10CM, START_READING, END_READING, PICK, TOTAL_WEAVE_TIME, NO_WARP_NO_WEFT, NO_POWER_NO_AIR, BEAN_GAITING, RE_BEAM_GAITING, NO_WEAVER, CLOTH_REPAIR_TOTAL, NO_BEAM_READY, Q_CHANGES, MECH_REPAIR, ELE_RO_REPAIR, SHUTTLE_REPAIR_TOTAL, OVER_HAULING, NO_PIRN, OTHER, SELEVEDGE_EDGE_CORD_REPAIR, TOTAL_TIME, REMARK, CARRY_OVER, CLOTH_REPAIR, SHUTTLE_REPAIR, PICK_REPAIR, WARP_END_REPAIR, TEMPLE_REPAIR, MC_STOPPAGES, CR1, CR2, CR3, CR4, CR5, CR6, SR1, SR2, SR3, SR4, PR1, PR2, PR3, PR4, PR5, PR6, PR7, PR8, WR1, WR2, WR3, WR4, WR5, TR1, TR2, TR3, TR4, TR5, HIERARCHY_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, APPROVED, APPROVED_DATE, REJECTED, REJECTED_DATE, REJECTED_REMARKS, CANCELED, CANCELED_DATE, CHANGED, CHANGED_DATE) ";
            sql = sql + "VALUES(?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?)";
            pstm = Conn.prepareStatement(sql);
            sqlh = "INSERT INTO PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL_H "
                    + "(REVISION_NO, UPDATED_BY, APPROVAL_STATUS, ENTRY_DATE, APPROVER_REMARKS, DOC_NO, DOC_DATE, SHIFT_ID, EMP_NAME, EMP_TYPE, EMP_NO, REG_ROKDI, CATOGORY_WISE_ENG, LOOM_ENG, BEAM_WARP_SR_NO, PRODUCT_GROUP, REED_SPACE_METER, PIECE_NO, WEFT_DETAILS, SHIFT_STARTING, IST_RECESS_OUT, IST_RECESS_IN, IIND_RECESS_OUT, IIND_RECESS_IN, IIIRD_RECESS_OUT, IIIRD_RECESS_IN, SHIFT_END_OUT, GATE_PASS_OUT, GATE_PASS_IN, MC_RPM, PICKS_10CM, START_READING, END_READING, PICK, TOTAL_WEAVE_TIME, NO_WARP_NO_WEFT, NO_POWER_NO_AIR, BEAN_GAITING, RE_BEAM_GAITING, NO_WEAVER, CLOTH_REPAIR_TOTAL, NO_BEAM_READY, Q_CHANGES, MECH_REPAIR, ELE_RO_REPAIR, SHUTTLE_REPAIR_TOTAL, OVER_HAULING, NO_PIRN, OTHER, SELEVEDGE_EDGE_CORD_REPAIR, TOTAL_TIME, REMARK, CARRY_OVER, CLOTH_REPAIR, SHUTTLE_REPAIR, PICK_REPAIR, WARP_END_REPAIR, TEMPLE_REPAIR, MC_STOPPAGES, CR1, CR2, CR3, CR4, CR5, CR6, SR1, SR2, SR3, SR4, PR1, PR2, PR3, PR4, PR5, PR6, PR7, PR8, WR1, WR2, WR3, WR4, WR5, TR1, TR2, TR3, TR4, TR5, HIERARCHY_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, APPROVED, APPROVED_DATE, REJECTED, REJECTED_DATE, REJECTED_REMARKS, CANCELED, CANCELED_DATE, CHANGED, CHANGED_DATE, FROM_IP) ";
            sqlh = sqlh + "VALUES(?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?,?,?)";
            pstmh = Conn.prepareStatement(sqlh);
            //Now Insert records into detail table
            for (int i = 1; i <= colMRItems.size(); i++) {
                clsWVG_Prod_Loom_WVRItem ObjMRItems = (clsWVG_Prod_Loom_WVRItem) colMRItems.get(Integer.toString(i));

                pstm.setString(1, (String) ObjMRItems.getAttribute("DOC_NO").getObj());
                setAttribute("DOC_NO", ObjMRItems.getAttribute("DOC_NO").getObj().toString());
                
                pstm.setString(2, (String) ObjMRItems.getAttribute("DOC_DATE").getObj().toString());
                pstm.setString(3, (String) ObjMRItems.getAttribute("SHIFT_ID").getObj().toString());
                pstm.setString(4, (String) ObjMRItems.getAttribute("EMP_NAME").getObj().toString());
                pstm.setString(5, (String) ObjMRItems.getAttribute("EMP_TYPE").getObj().toString());
                pstm.setString(6, (String) ObjMRItems.getAttribute("EMP_NO").getObj().toString());
                pstm.setString(7, (String) ObjMRItems.getAttribute("REG_ROKDI").getObj().toString());
                pstm.setString(8, (String) ObjMRItems.getAttribute("CATOGORY_WISE_ENG").getObj().toString());
                pstm.setString(9, (String) ObjMRItems.getAttribute("LOOM_ENG").getObj().toString());
                pstm.setString(10, (String) ObjMRItems.getAttribute("BEAM_WARP_SR_NO").getObj().toString());
                pstm.setString(11, (String) ObjMRItems.getAttribute("PRODUCT_GROUP").getObj().toString());
                pstm.setString(12, (String) ObjMRItems.getAttribute("REED_SPACE_METER").getObj().toString());
                pstm.setString(13, (String) ObjMRItems.getAttribute("PIECE_NO").getObj().toString());
                pstm.setString(14, (String) ObjMRItems.getAttribute("WEFT_DETAILS").getObj().toString());
                pstm.setString(15, (String) ObjMRItems.getAttribute("SHIFT_STARTING").getObj().toString());
                pstm.setString(16, (String) ObjMRItems.getAttribute("IST_RECESS_OUT").getObj().toString());
                pstm.setString(17, (String) ObjMRItems.getAttribute("IST_RECESS_IN").getObj().toString());
                pstm.setString(18, (String) ObjMRItems.getAttribute("IIND_RECESS_OUT").getObj().toString());
                pstm.setString(19, (String) ObjMRItems.getAttribute("IIND_RECESS_IN").getObj().toString());
                pstm.setString(20, (String) ObjMRItems.getAttribute("IIIRD_RECESS_OUT").getObj().toString());
                pstm.setString(21, (String) ObjMRItems.getAttribute("IIIRD_RECESS_IN").getObj().toString());
                pstm.setString(22, (String) ObjMRItems.getAttribute("SHIFT_END_OUT").getObj().toString());
                pstm.setString(23, (String) ObjMRItems.getAttribute("GATE_PASS_OUT").getObj().toString());
                pstm.setString(24, (String) ObjMRItems.getAttribute("GATE_PASS_IN").getObj().toString());
                pstm.setString(25, (String) ObjMRItems.getAttribute("MC_RPM").getObj().toString());
                pstm.setString(26, (String) ObjMRItems.getAttribute("PICKS_10CM").getObj().toString());
                pstm.setString(27, (String) ObjMRItems.getAttribute("START_READING").getObj().toString());
                pstm.setString(28, (String) ObjMRItems.getAttribute("END_READING").getObj().toString());
                pstm.setString(29, (String) ObjMRItems.getAttribute("PICK").getObj().toString());
                pstm.setString(30, (String) ObjMRItems.getAttribute("TOTAL_WEAVE_TIME").getObj().toString());
                pstm.setString(31, (String) ObjMRItems.getAttribute("NO_WARP_NO_WEFT").getObj().toString());
                pstm.setString(32, (String) ObjMRItems.getAttribute("NO_POWER_NO_AIR").getObj().toString());
                pstm.setString(33, (String) ObjMRItems.getAttribute("BEAN_GAITING").getObj().toString());
                pstm.setString(34, (String) ObjMRItems.getAttribute("RE_BEAM_GAITING").getObj().toString());
                pstm.setString(35, (String) ObjMRItems.getAttribute("NO_WEAVER").getObj().toString());
                pstm.setString(36, (String) ObjMRItems.getAttribute("CLOTH_REPAIR_TOTAL").getObj().toString());
                pstm.setString(37, (String) ObjMRItems.getAttribute("NO_BEAM_READY").getObj().toString());
                pstm.setString(38, (String) ObjMRItems.getAttribute("Q_CHANGES").getObj().toString());
                pstm.setString(39, (String) ObjMRItems.getAttribute("MECH_REPAIR").getObj().toString());
                pstm.setString(40, (String) ObjMRItems.getAttribute("ELE_RO_REPAIR").getObj().toString());
                pstm.setString(41, (String) ObjMRItems.getAttribute("SHUTTLE_REPAIR_TOTAL").getObj().toString());
                pstm.setString(42, (String) ObjMRItems.getAttribute("OVER_HAULING").getObj().toString());
                
                pstm.setString(43, (String) ObjMRItems.getAttribute("NO_PIRN").getObj().toString());
                pstm.setString(44, (String) ObjMRItems.getAttribute("OTHER").getObj().toString());
                
                pstm.setString(45, (String) ObjMRItems.getAttribute("SELEVEDGE_EDGE_CORD_REPAIR").getObj().toString());
                pstm.setString(46, (String) ObjMRItems.getAttribute("TOTAL_TIME").getObj().toString());
                pstm.setString(47, (String) ObjMRItems.getAttribute("REMARK").getObj().toString());
                pstm.setString(48, (String) ObjMRItems.getAttribute("CARRY_OVER").getObj().toString());
                pstm.setString(49, (String) ObjMRItems.getAttribute("CLOTH_REPAIR").getObj().toString());
                pstm.setString(50, (String) ObjMRItems.getAttribute("SHUTTLE_REPAIR").getObj().toString());

                pstm.setString(51, (String) ObjMRItems.getAttribute("PICK_REPAIR").getObj().toString());
                pstm.setString(52, (String) ObjMRItems.getAttribute("WARP_END_REPAIR").getObj().toString());
                pstm.setString(53, (String) ObjMRItems.getAttribute("TEMPLE_REPAIR").getObj().toString());
                pstm.setString(54, (String) ObjMRItems.getAttribute("MC_STOPPAGES").getObj().toString());
                pstm.setString(55, (String) ObjMRItems.getAttribute("CR1").getObj().toString());
                pstm.setString(56, (String) ObjMRItems.getAttribute("CR2").getObj().toString());
                pstm.setString(57, (String) ObjMRItems.getAttribute("CR3").getObj().toString());
                pstm.setString(58, (String) ObjMRItems.getAttribute("CR4").getObj().toString());
                pstm.setString(59, (String) ObjMRItems.getAttribute("CR5").getObj().toString());
                pstm.setString(60, (String) ObjMRItems.getAttribute("CR6").getObj().toString());
                pstm.setString(61, (String) ObjMRItems.getAttribute("SR1").getObj().toString());
                pstm.setString(62, (String) ObjMRItems.getAttribute("SR2").getObj().toString());
                pstm.setString(63, (String) ObjMRItems.getAttribute("SR3").getObj().toString());
                pstm.setString(64, (String) ObjMRItems.getAttribute("SR4").getObj().toString());
                pstm.setString(65, (String) ObjMRItems.getAttribute("PR1").getObj().toString());
                pstm.setString(66, (String) ObjMRItems.getAttribute("PR2").getObj().toString());
                pstm.setString(67, (String) ObjMRItems.getAttribute("PR3").getObj().toString());
                pstm.setString(68, (String) ObjMRItems.getAttribute("PR4").getObj().toString());
                pstm.setString(69, (String) ObjMRItems.getAttribute("PR5").getObj().toString());
                pstm.setString(70, (String) ObjMRItems.getAttribute("PR6").getObj().toString());
                pstm.setString(71, (String) ObjMRItems.getAttribute("PR7").getObj().toString());
                pstm.setString(72, (String) ObjMRItems.getAttribute("PR8").getObj().toString());
                pstm.setString(73, (String) ObjMRItems.getAttribute("WR1").getObj().toString());
                pstm.setString(74, (String) ObjMRItems.getAttribute("WR2").getObj().toString());
                pstm.setString(75, (String) ObjMRItems.getAttribute("WR3").getObj().toString());
                pstm.setString(76, (String) ObjMRItems.getAttribute("WR4").getObj().toString());
                pstm.setString(77, (String) ObjMRItems.getAttribute("WR5").getObj().toString());
                pstm.setString(78, (String) ObjMRItems.getAttribute("TR1").getObj().toString());
                pstm.setString(79, (String) ObjMRItems.getAttribute("TR2").getObj().toString());
                pstm.setString(80, (String) ObjMRItems.getAttribute("TR3").getObj().toString());
                pstm.setString(81, (String) ObjMRItems.getAttribute("TR4").getObj().toString());
                pstm.setString(82, (String) ObjMRItems.getAttribute("TR5").getObj().toString());
                
                pstm.setString(83, getAttribute("HIERARCHY_ID").getVal()+"");
                pstm.setString(84, getAttribute("CREATED_BY").getVal() +"");
                pstm.setString(85, (String) getAttribute("CREATED_DATE").getObj());
                pstm.setString(86, "0");//MODIFIED_BY
                pstm.setString(87, "0000-00-00");//MODIFIED_DATE
                pstm.setString(88, "0");//APPROVED
                pstm.setString(89, "0000-00-00");//APPROVED_DATE
                pstm.setString(90, "0");//REJECTED
                pstm.setString(91, "0000-00-00");//REJECTED_DATE
                pstm.setString(92, getAttribute("REJECTED_REMARKS").getString());
                pstm.setString(93, "0");//CANCELLED
                pstm.setString(94, "0000-00-00");//CANCELLED_DATE
                pstm.setString(95, "0");//CHANGED
                pstm.setString(96, "0000-00-00");//CHANGED_DATE
                pstm.addBatch();
                
                
                
                pstmh.setString(1, "1");
                pstmh.setString(2, EITLERPGLOBAL.gUserID+"");
                pstmh.setString(3, (String) getAttribute("APPROVAL_STATUS").getObj());
                pstmh.setString(4, EITLERPGLOBAL.getCurrentDateDB());
                pstmh.setString(5, (String) getAttribute("REJECTED_REMARKS").getObj());
                
                pstmh.setString(6, (String) ObjMRItems.getAttribute("DOC_NO").getObj());
                setAttribute("DOC_NO", ObjMRItems.getAttribute("DOC_NO").getObj().toString());
                
                pstmh.setString(7, (String) ObjMRItems.getAttribute("DOC_DATE").getObj().toString());
                pstmh.setString(8, (String) ObjMRItems.getAttribute("SHIFT_ID").getObj().toString());
                pstmh.setString(9, (String) ObjMRItems.getAttribute("EMP_NAME").getObj().toString());
                pstmh.setString(10, (String) ObjMRItems.getAttribute("EMP_TYPE").getObj().toString());
                pstmh.setString(11, (String) ObjMRItems.getAttribute("EMP_NO").getObj().toString());
                pstmh.setString(12, (String) ObjMRItems.getAttribute("REG_ROKDI").getObj().toString());
                pstmh.setString(13, (String) ObjMRItems.getAttribute("CATOGORY_WISE_ENG").getObj().toString());
                pstmh.setString(14, (String) ObjMRItems.getAttribute("LOOM_ENG").getObj().toString());
                pstmh.setString(15, (String) ObjMRItems.getAttribute("BEAM_WARP_SR_NO").getObj().toString());
                pstmh.setString(16, (String) ObjMRItems.getAttribute("PRODUCT_GROUP").getObj().toString());
                pstmh.setString(17, (String) ObjMRItems.getAttribute("REED_SPACE_METER").getObj().toString());
                pstmh.setString(18, (String) ObjMRItems.getAttribute("PIECE_NO").getObj().toString());
                pstmh.setString(19, (String) ObjMRItems.getAttribute("WEFT_DETAILS").getObj().toString());
                pstmh.setString(20, (String) ObjMRItems.getAttribute("SHIFT_STARTING").getObj().toString());
                pstmh.setString(21, (String) ObjMRItems.getAttribute("IST_RECESS_OUT").getObj().toString());
                pstmh.setString(22, (String) ObjMRItems.getAttribute("IST_RECESS_IN").getObj().toString());
                pstmh.setString(23, (String) ObjMRItems.getAttribute("IIND_RECESS_OUT").getObj().toString());
                pstmh.setString(24, (String) ObjMRItems.getAttribute("IIND_RECESS_IN").getObj().toString());
                pstmh.setString(25, (String) ObjMRItems.getAttribute("IIIRD_RECESS_OUT").getObj().toString());
                pstmh.setString(26, (String) ObjMRItems.getAttribute("IIIRD_RECESS_IN").getObj().toString());
                pstmh.setString(27, (String) ObjMRItems.getAttribute("SHIFT_END_OUT").getObj().toString());
                pstmh.setString(28, (String) ObjMRItems.getAttribute("GATE_PASS_OUT").getObj().toString());
                pstmh.setString(29, (String) ObjMRItems.getAttribute("GATE_PASS_IN").getObj().toString());
                pstmh.setString(30, (String) ObjMRItems.getAttribute("MC_RPM").getObj().toString());
                pstmh.setString(31, (String) ObjMRItems.getAttribute("PICKS_10CM").getObj().toString());
                pstmh.setString(32, (String) ObjMRItems.getAttribute("START_READING").getObj().toString());
                pstmh.setString(33, (String) ObjMRItems.getAttribute("END_READING").getObj().toString());
                pstmh.setString(34, (String) ObjMRItems.getAttribute("PICK").getObj().toString());
                pstmh.setString(35, (String) ObjMRItems.getAttribute("TOTAL_WEAVE_TIME").getObj().toString());
                pstmh.setString(36, (String) ObjMRItems.getAttribute("NO_WARP_NO_WEFT").getObj().toString());
                pstmh.setString(37, (String) ObjMRItems.getAttribute("NO_POWER_NO_AIR").getObj().toString());
                pstmh.setString(38, (String) ObjMRItems.getAttribute("BEAN_GAITING").getObj().toString());
                pstmh.setString(39, (String) ObjMRItems.getAttribute("RE_BEAM_GAITING").getObj().toString());
                pstmh.setString(40, (String) ObjMRItems.getAttribute("NO_WEAVER").getObj().toString());
                pstmh.setString(41, (String) ObjMRItems.getAttribute("CLOTH_REPAIR_TOTAL").getObj().toString());
                pstmh.setString(42, (String) ObjMRItems.getAttribute("NO_BEAM_READY").getObj().toString());
                pstmh.setString(43, (String) ObjMRItems.getAttribute("Q_CHANGES").getObj().toString());
                pstmh.setString(44, (String) ObjMRItems.getAttribute("MECH_REPAIR").getObj().toString());
                pstmh.setString(45, (String) ObjMRItems.getAttribute("ELE_RO_REPAIR").getObj().toString());
                pstmh.setString(46, (String) ObjMRItems.getAttribute("SHUTTLE_REPAIR_TOTAL").getObj().toString());
                pstmh.setString(47, (String) ObjMRItems.getAttribute("OVER_HAULING").getObj().toString());
                
                pstmh.setString(48, (String) ObjMRItems.getAttribute("NO_PIRN").getObj().toString());
                pstmh.setString(49, (String) ObjMRItems.getAttribute("OTHER").getObj().toString());
                
                pstmh.setString(50, (String) ObjMRItems.getAttribute("SELEVEDGE_EDGE_CORD_REPAIR").getObj().toString());
                pstmh.setString(51, (String) ObjMRItems.getAttribute("TOTAL_TIME").getObj().toString());
                pstmh.setString(52, (String) ObjMRItems.getAttribute("REMARK").getObj().toString());
                pstmh.setString(53, (String) ObjMRItems.getAttribute("CARRY_OVER").getObj().toString());
                pstmh.setString(54, (String) ObjMRItems.getAttribute("CLOTH_REPAIR").getObj().toString());
                pstmh.setString(55, (String) ObjMRItems.getAttribute("SHUTTLE_REPAIR").getObj().toString());

                pstmh.setString(56, (String) ObjMRItems.getAttribute("PICK_REPAIR").getObj().toString());
                pstmh.setString(57, (String) ObjMRItems.getAttribute("WARP_END_REPAIR").getObj().toString());
                pstmh.setString(58, (String) ObjMRItems.getAttribute("TEMPLE_REPAIR").getObj().toString());
                pstmh.setString(59, (String) ObjMRItems.getAttribute("MC_STOPPAGES").getObj().toString());
                pstmh.setString(60, (String) ObjMRItems.getAttribute("CR1").getObj().toString());
                pstmh.setString(61, (String) ObjMRItems.getAttribute("CR2").getObj().toString());
                pstmh.setString(62, (String) ObjMRItems.getAttribute("CR3").getObj().toString());
                pstmh.setString(63, (String) ObjMRItems.getAttribute("CR4").getObj().toString());
                pstmh.setString(64, (String) ObjMRItems.getAttribute("CR5").getObj().toString());
                pstmh.setString(65, (String) ObjMRItems.getAttribute("CR6").getObj().toString());
                pstmh.setString(66, (String) ObjMRItems.getAttribute("SR1").getObj().toString());
                pstmh.setString(67, (String) ObjMRItems.getAttribute("SR2").getObj().toString());
                pstmh.setString(68, (String) ObjMRItems.getAttribute("SR3").getObj().toString());
                pstmh.setString(69, (String) ObjMRItems.getAttribute("SR4").getObj().toString());
                pstmh.setString(70, (String) ObjMRItems.getAttribute("PR1").getObj().toString());
                pstmh.setString(71, (String) ObjMRItems.getAttribute("PR2").getObj().toString());
                pstmh.setString(72, (String) ObjMRItems.getAttribute("PR3").getObj().toString());
                pstmh.setString(73, (String) ObjMRItems.getAttribute("PR4").getObj().toString());
                pstmh.setString(74, (String) ObjMRItems.getAttribute("PR5").getObj().toString());
                pstmh.setString(75, (String) ObjMRItems.getAttribute("PR6").getObj().toString());
                pstmh.setString(76, (String) ObjMRItems.getAttribute("PR7").getObj().toString());
                pstmh.setString(77, (String) ObjMRItems.getAttribute("PR8").getObj().toString());
                pstmh.setString(78, (String) ObjMRItems.getAttribute("WR1").getObj().toString());
                pstmh.setString(79, (String) ObjMRItems.getAttribute("WR2").getObj().toString());
                pstmh.setString(80, (String) ObjMRItems.getAttribute("WR3").getObj().toString());
                pstmh.setString(81, (String) ObjMRItems.getAttribute("WR4").getObj().toString());
                pstmh.setString(82, (String) ObjMRItems.getAttribute("WR5").getObj().toString());
                pstmh.setString(83, (String) ObjMRItems.getAttribute("TR1").getObj().toString());
                pstmh.setString(84, (String) ObjMRItems.getAttribute("TR2").getObj().toString());
                pstmh.setString(85, (String) ObjMRItems.getAttribute("TR3").getObj().toString());
                pstmh.setString(86, (String) ObjMRItems.getAttribute("TR4").getObj().toString());
                pstmh.setString(87, (String) ObjMRItems.getAttribute("TR5").getObj().toString());
                
                pstmh.setString(88, getAttribute("HIERARCHY_ID").getVal()+"");
                pstmh.setString(89, getAttribute("CREATED_BY").getVal() +"");
                pstmh.setString(90, (String) getAttribute("CREATED_DATE").getObj());
                pstmh.setString(91, "0");//MODIFIED_BY
                pstmh.setString(92, "0000-00-00");//MODIFIED_DATE
                pstmh.setString(93, "0");//APPROVED
                pstmh.setString(94, "0000-00-00");//APPROVED_DATE
                pstmh.setString(95, "0");//REJECTED
                pstmh.setString(96, "0000-00-00");//REJECTED_DATE
                pstmh.setString(97, getAttribute("REJECTED_REMARKS").getString());
                pstmh.setString(98, "0");//CANCELLED
                pstmh.setString(99, "0000-00-00");//CANCELLED_DATE
                pstmh.setString(100, "0");//CHANGED
                pstmh.setString(101, "0000-00-00");//CHANGED_DATE
                pstmh.setString(102, str_split[1]);//FROM_IP
                
                
                pstmh.addBatch();

                if ((i + 1) % 1000 == 0) {
                    pstm.executeBatch();
                    pstmh.executeBatch();
                    Conn.commit();
                }
            }

            System.out.println("Pre1 ");
            pstm.executeBatch();
            
            System.out.println("Pre2 ");
            pstmh.executeBatch();
            Conn.commit();
            Conn.setAutoCommit(true);
            //String s;
//            //s = "UPDATE PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL "
//                    + "SET KEY1=CONCAT(TRIM(YEAR_FROM),TRIM(YEAR_TO),TRIM(PARTY_CODE),TRIM(MACHINE_NO),"
//                    + "TRIM(POSITION_NO)) "
//                    + "WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "' ";
            //data.Execute(s);
            
            //===================== Update the Approval Flow ======================//
            setAttribute("FROM", EITLERPGLOBAL.gUserID);

            //ApprovalFlow ObjFlow=new ApprovalFlow();
            clsFeltProductionApprovalFlow ObjFlow = new clsFeltProductionApprovalFlow();

            ObjFlow.ModuleID = clsWVG_Prod_Loom_WVR.ModuleID;
            ObjFlow.DocNo = (String) getAttribute("DOC_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("CREATED_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL";
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
            data.Execute("DELETE FROM PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "'");
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

            rsHDetail = stHDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL_H WHERE DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//

            //--------- Generate Gatepass requisition no.  ------------
            //-------------------------------------------------
            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");

           
            Statement tmpStmt;

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            Conn.setAutoCommit(false);
            rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL WHERE DOC_NO='1'");

            sql = "INSERT INTO PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL "
                    + "(DOC_NO, DOC_DATE, SHIFT_ID, EMP_NAME, EMP_TYPE, EMP_NO, REG_ROKDI, CATOGORY_WISE_ENG, LOOM_ENG, BEAM_WARP_SR_NO, PRODUCT_GROUP, REED_SPACE_METER, PIECE_NO, WEFT_DETAILS, SHIFT_STARTING, IST_RECESS_OUT, IST_RECESS_IN, IIND_RECESS_OUT, IIND_RECESS_IN, IIIRD_RECESS_OUT, IIIRD_RECESS_IN, SHIFT_END_OUT, GATE_PASS_OUT, GATE_PASS_IN, MC_RPM, PICKS_10CM, START_READING, END_READING, PICK, TOTAL_WEAVE_TIME, NO_WARP_NO_WEFT, NO_POWER_NO_AIR, BEAN_GAITING, RE_BEAM_GAITING, NO_WEAVER, CLOTH_REPAIR_TOTAL, NO_BEAM_READY, Q_CHANGES, MECH_REPAIR, ELE_RO_REPAIR, SHUTTLE_REPAIR_TOTAL, OVER_HAULING, NO_PIRN, OTHER, SELEVEDGE_EDGE_CORD_REPAIR, TOTAL_TIME, REMARK, CARRY_OVER, CLOTH_REPAIR, SHUTTLE_REPAIR, PICK_REPAIR, WARP_END_REPAIR, TEMPLE_REPAIR, MC_STOPPAGES, CR1, CR2, CR3, CR4, CR5, CR6, SR1, SR2, SR3, SR4, PR1, PR2, PR3, PR4, PR5, PR6, PR7, PR8, WR1, WR2, WR3, WR4, WR5, TR1, TR2, TR3, TR4, TR5, HIERARCHY_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, APPROVED, APPROVED_DATE, REJECTED, REJECTED_DATE, REJECTED_REMARKS, CANCELED, CANCELED_DATE, CHANGED, CHANGED_DATE) ";
            sql = sql + "VALUES(?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?)";
            pstm = Conn.prepareStatement(sql);
            sqlh = "INSERT INTO PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL_H "
                    + "(REVISION_NO, UPDATED_BY, APPROVAL_STATUS, ENTRY_DATE, APPROVER_REMARKS, DOC_NO, DOC_DATE, SHIFT_ID, EMP_NAME, EMP_TYPE, EMP_NO, REG_ROKDI, CATOGORY_WISE_ENG, LOOM_ENG, BEAM_WARP_SR_NO, PRODUCT_GROUP, REED_SPACE_METER, PIECE_NO, WEFT_DETAILS, SHIFT_STARTING, IST_RECESS_OUT, IST_RECESS_IN, IIND_RECESS_OUT, IIND_RECESS_IN, IIIRD_RECESS_OUT, IIIRD_RECESS_IN, SHIFT_END_OUT, GATE_PASS_OUT, GATE_PASS_IN, MC_RPM, PICKS_10CM, START_READING, END_READING, PICK, TOTAL_WEAVE_TIME, NO_WARP_NO_WEFT, NO_POWER_NO_AIR, BEAN_GAITING, RE_BEAM_GAITING, NO_WEAVER, CLOTH_REPAIR_TOTAL, NO_BEAM_READY, Q_CHANGES, MECH_REPAIR, ELE_RO_REPAIR, SHUTTLE_REPAIR_TOTAL, OVER_HAULING, NO_PIRN, OTHER, SELEVEDGE_EDGE_CORD_REPAIR, TOTAL_TIME, REMARK, CARRY_OVER, CLOTH_REPAIR, SHUTTLE_REPAIR, PICK_REPAIR, WARP_END_REPAIR, TEMPLE_REPAIR, MC_STOPPAGES, CR1, CR2, CR3, CR4, CR5, CR6, SR1, SR2, SR3, SR4, PR1, PR2, PR3, PR4, PR5, PR6, PR7, PR8, WR1, WR2, WR3, WR4, WR5, TR1, TR2, TR3, TR4, TR5, HIERARCHY_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, APPROVED, APPROVED_DATE, REJECTED, REJECTED_DATE, REJECTED_REMARKS, CANCELED, CANCELED_DATE, CHANGED, CHANGED_DATE, FROM_IP) ";
            sqlh = sqlh + "VALUES(?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?,?,?,?,?,?,?)";
            pstmh = Conn.prepareStatement(sqlh);
            int RevNo = 0;
            //Now Insert records into detail table
            for (int i = 1; i <= colMRItems.size(); i++) {
                clsWVG_Prod_Loom_WVRItem ObjMRItems = (clsWVG_Prod_Loom_WVRItem) colMRItems.get(Integer.toString(i));
                if (i == 1) {
                    RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL_H WHERE DOC_NO='" + (String) ObjMRItems.getAttribute("DOC_NO").getObj() + "'");
                    RevNo++;
                }
                pstm.setString(1, (String) ObjMRItems.getAttribute("DOC_NO").getObj());
                setAttribute("DOC_NO", ObjMRItems.getAttribute("DOC_NO").getObj().toString());
                
                pstm.setString(2, (String) ObjMRItems.getAttribute("DOC_DATE").getObj().toString());
                pstm.setString(3, (String) ObjMRItems.getAttribute("SHIFT_ID").getObj().toString());
                pstm.setString(4, (String) ObjMRItems.getAttribute("EMP_NAME").getObj().toString());
                pstm.setString(5, (String) ObjMRItems.getAttribute("EMP_TYPE").getObj().toString());
                pstm.setString(6, (String) ObjMRItems.getAttribute("EMP_NO").getObj().toString());
                pstm.setString(7, (String) ObjMRItems.getAttribute("REG_ROKDI").getObj().toString());
                pstm.setString(8, (String) ObjMRItems.getAttribute("CATOGORY_WISE_ENG").getObj().toString());
                pstm.setString(9, (String) ObjMRItems.getAttribute("LOOM_ENG").getObj().toString());
                pstm.setString(10, (String) ObjMRItems.getAttribute("BEAM_WARP_SR_NO").getObj().toString());
                pstm.setString(11, (String) ObjMRItems.getAttribute("PRODUCT_GROUP").getObj().toString());
                pstm.setString(12, (String) ObjMRItems.getAttribute("REED_SPACE_METER").getObj().toString());
                pstm.setString(13, (String) ObjMRItems.getAttribute("PIECE_NO").getObj().toString());
                pstm.setString(14, (String) ObjMRItems.getAttribute("WEFT_DETAILS").getObj().toString());
                pstm.setString(15, (String) ObjMRItems.getAttribute("SHIFT_STARTING").getObj().toString());
                pstm.setString(16, (String) ObjMRItems.getAttribute("IST_RECESS_OUT").getObj().toString());
                pstm.setString(17, (String) ObjMRItems.getAttribute("IST_RECESS_IN").getObj().toString());
                pstm.setString(18, (String) ObjMRItems.getAttribute("IIND_RECESS_OUT").getObj().toString());
                pstm.setString(19, (String) ObjMRItems.getAttribute("IIND_RECESS_IN").getObj().toString());
                pstm.setString(20, (String) ObjMRItems.getAttribute("IIIRD_RECESS_OUT").getObj().toString());
                pstm.setString(21, (String) ObjMRItems.getAttribute("IIIRD_RECESS_IN").getObj().toString());
                pstm.setString(22, (String) ObjMRItems.getAttribute("SHIFT_END_OUT").getObj().toString());
                pstm.setString(23, (String) ObjMRItems.getAttribute("GATE_PASS_OUT").getObj().toString());
                pstm.setString(24, (String) ObjMRItems.getAttribute("GATE_PASS_IN").getObj().toString());
                pstm.setString(25, (String) ObjMRItems.getAttribute("MC_RPM").getObj().toString());
                pstm.setString(26, (String) ObjMRItems.getAttribute("PICKS_10CM").getObj().toString());
                pstm.setString(27, (String) ObjMRItems.getAttribute("START_READING").getObj().toString());
                pstm.setString(28, (String) ObjMRItems.getAttribute("END_READING").getObj().toString());
                pstm.setString(29, (String) ObjMRItems.getAttribute("PICK").getObj().toString());
                pstm.setString(30, (String) ObjMRItems.getAttribute("TOTAL_WEAVE_TIME").getObj().toString());
                pstm.setString(31, (String) ObjMRItems.getAttribute("NO_WARP_NO_WEFT").getObj().toString());
                pstm.setString(32, (String) ObjMRItems.getAttribute("NO_POWER_NO_AIR").getObj().toString());
                pstm.setString(33, (String) ObjMRItems.getAttribute("BEAN_GAITING").getObj().toString());
                pstm.setString(34, (String) ObjMRItems.getAttribute("RE_BEAM_GAITING").getObj().toString());
                pstm.setString(35, (String) ObjMRItems.getAttribute("NO_WEAVER").getObj().toString());
                pstm.setString(36, (String) ObjMRItems.getAttribute("CLOTH_REPAIR_TOTAL").getObj().toString());
                pstm.setString(37, (String) ObjMRItems.getAttribute("NO_BEAM_READY").getObj().toString());
                pstm.setString(38, (String) ObjMRItems.getAttribute("Q_CHANGES").getObj().toString());
                pstm.setString(39, (String) ObjMRItems.getAttribute("MECH_REPAIR").getObj().toString());
                pstm.setString(40, (String) ObjMRItems.getAttribute("ELE_RO_REPAIR").getObj().toString());
                pstm.setString(41, (String) ObjMRItems.getAttribute("SHUTTLE_REPAIR_TOTAL").getObj().toString());
                pstm.setString(42, (String) ObjMRItems.getAttribute("OVER_HAULING").getObj().toString());
                
                pstm.setString(43, (String) ObjMRItems.getAttribute("NO_PIRN").getObj().toString());
                pstm.setString(44, (String) ObjMRItems.getAttribute("OTHER").getObj().toString());
                
                pstm.setString(45, (String) ObjMRItems.getAttribute("SELEVEDGE_EDGE_CORD_REPAIR").getObj().toString());
                pstm.setString(46, (String) ObjMRItems.getAttribute("TOTAL_TIME").getObj().toString());
                pstm.setString(47, (String) ObjMRItems.getAttribute("REMARK").getObj().toString());
                pstm.setString(48, (String) ObjMRItems.getAttribute("CARRY_OVER").getObj().toString());
                pstm.setString(49, (String) ObjMRItems.getAttribute("CLOTH_REPAIR").getObj().toString());
                pstm.setString(50, (String) ObjMRItems.getAttribute("SHUTTLE_REPAIR").getObj().toString());

                pstm.setString(51, (String) ObjMRItems.getAttribute("PICK_REPAIR").getObj().toString());
                pstm.setString(52, (String) ObjMRItems.getAttribute("WARP_END_REPAIR").getObj().toString());
                pstm.setString(53, (String) ObjMRItems.getAttribute("TEMPLE_REPAIR").getObj().toString());
                pstm.setString(54, (String) ObjMRItems.getAttribute("MC_STOPPAGES").getObj().toString());
                pstm.setString(55, (String) ObjMRItems.getAttribute("CR1").getObj().toString());
                pstm.setString(56, (String) ObjMRItems.getAttribute("CR2").getObj().toString());
                pstm.setString(57, (String) ObjMRItems.getAttribute("CR3").getObj().toString());
                pstm.setString(58, (String) ObjMRItems.getAttribute("CR4").getObj().toString());
                pstm.setString(59, (String) ObjMRItems.getAttribute("CR5").getObj().toString());
                pstm.setString(60, (String) ObjMRItems.getAttribute("CR6").getObj().toString());
                pstm.setString(61, (String) ObjMRItems.getAttribute("SR1").getObj().toString());
                pstm.setString(62, (String) ObjMRItems.getAttribute("SR2").getObj().toString());
                pstm.setString(63, (String) ObjMRItems.getAttribute("SR3").getObj().toString());
                pstm.setString(64, (String) ObjMRItems.getAttribute("SR4").getObj().toString());
                pstm.setString(65, (String) ObjMRItems.getAttribute("PR1").getObj().toString());
                pstm.setString(66, (String) ObjMRItems.getAttribute("PR2").getObj().toString());
                pstm.setString(67, (String) ObjMRItems.getAttribute("PR3").getObj().toString());
                pstm.setString(68, (String) ObjMRItems.getAttribute("PR4").getObj().toString());
                pstm.setString(69, (String) ObjMRItems.getAttribute("PR5").getObj().toString());
                pstm.setString(70, (String) ObjMRItems.getAttribute("PR6").getObj().toString());
                pstm.setString(71, (String) ObjMRItems.getAttribute("PR7").getObj().toString());
                pstm.setString(72, (String) ObjMRItems.getAttribute("PR8").getObj().toString());
                pstm.setString(73, (String) ObjMRItems.getAttribute("WR1").getObj().toString());
                pstm.setString(74, (String) ObjMRItems.getAttribute("WR2").getObj().toString());
                pstm.setString(75, (String) ObjMRItems.getAttribute("WR3").getObj().toString());
                pstm.setString(76, (String) ObjMRItems.getAttribute("WR4").getObj().toString());
                pstm.setString(77, (String) ObjMRItems.getAttribute("WR5").getObj().toString());
                pstm.setString(78, (String) ObjMRItems.getAttribute("TR1").getObj().toString());
                pstm.setString(79, (String) ObjMRItems.getAttribute("TR2").getObj().toString());
                pstm.setString(80, (String) ObjMRItems.getAttribute("TR3").getObj().toString());
                pstm.setString(81, (String) ObjMRItems.getAttribute("TR4").getObj().toString());
                pstm.setString(82, (String) ObjMRItems.getAttribute("TR5").getObj().toString());
                
                pstm.setString(83, getAttribute("HIERARCHY_ID").getVal()+"");
                pstm.setString(84, getAttribute("CREATED_BY").getVal() +"");
                pstm.setString(85, (String) getAttribute("CREATED_DATE").getObj());
                pstm.setString(86, "0");//MODIFIED_BY
                pstm.setString(87, "0000-00-00");//MODIFIED_DATE
                pstm.setString(88, "0");//APPROVED
                pstm.setString(89, "0000-00-00");//APPROVED_DATE
                pstm.setString(90, "0");//REJECTED
                pstm.setString(91, "0000-00-00");//REJECTED_DATE
                pstm.setString(92, getAttribute("REJECTED_REMARKS").getString());
                pstm.setString(93, "0");//CANCELLED
                pstm.setString(94, "0000-00-00");//CANCELLED_DATE
                pstm.setString(95, "0");//CHANGED
                pstm.setString(96, "0000-00-00");//CHANGED_DATE
                pstm.addBatch();
                
                
                
                pstmh.setString(1, RevNo+"");
                pstmh.setString(2, EITLERPGLOBAL.gUserID+"");
                pstmh.setString(3, (String) getAttribute("APPROVAL_STATUS").getObj());
                pstmh.setString(4, EITLERPGLOBAL.getCurrentDateDB());
                pstmh.setString(5, (String) getAttribute("APPROVER_REMARKS").getObj());
                pstmh.setString(6, (String) ObjMRItems.getAttribute("DOC_NO").getObj());
                setAttribute("DOC_NO", ObjMRItems.getAttribute("DOC_NO").getObj().toString());
                
                pstmh.setString(7, (String) ObjMRItems.getAttribute("DOC_DATE").getObj().toString());
                pstmh.setString(8, (String) ObjMRItems.getAttribute("SHIFT_ID").getObj().toString());
                pstmh.setString(9, (String) ObjMRItems.getAttribute("EMP_NAME").getObj().toString());
                pstmh.setString(10, (String) ObjMRItems.getAttribute("EMP_TYPE").getObj().toString());
                pstmh.setString(11, (String) ObjMRItems.getAttribute("EMP_NO").getObj().toString());
                pstmh.setString(12, (String) ObjMRItems.getAttribute("REG_ROKDI").getObj().toString());
                pstmh.setString(13, (String) ObjMRItems.getAttribute("CATOGORY_WISE_ENG").getObj().toString());
                pstmh.setString(14, (String) ObjMRItems.getAttribute("LOOM_ENG").getObj().toString());
                pstmh.setString(15, (String) ObjMRItems.getAttribute("BEAM_WARP_SR_NO").getObj().toString());
                pstmh.setString(16, (String) ObjMRItems.getAttribute("PRODUCT_GROUP").getObj().toString());
                pstmh.setString(17, (String) ObjMRItems.getAttribute("REED_SPACE_METER").getObj().toString());
                pstmh.setString(18, (String) ObjMRItems.getAttribute("PIECE_NO").getObj().toString());
                pstmh.setString(19, (String) ObjMRItems.getAttribute("WEFT_DETAILS").getObj().toString());
                pstmh.setString(20, (String) ObjMRItems.getAttribute("SHIFT_STARTING").getObj().toString());
                pstmh.setString(21, (String) ObjMRItems.getAttribute("IST_RECESS_OUT").getObj().toString());
                pstmh.setString(22, (String) ObjMRItems.getAttribute("IST_RECESS_IN").getObj().toString());
                pstmh.setString(23, (String) ObjMRItems.getAttribute("IIND_RECESS_OUT").getObj().toString());
                pstmh.setString(24, (String) ObjMRItems.getAttribute("IIND_RECESS_IN").getObj().toString());
                pstmh.setString(25, (String) ObjMRItems.getAttribute("IIIRD_RECESS_OUT").getObj().toString());
                pstmh.setString(26, (String) ObjMRItems.getAttribute("IIIRD_RECESS_IN").getObj().toString());
                pstmh.setString(27, (String) ObjMRItems.getAttribute("SHIFT_END_OUT").getObj().toString());
                pstmh.setString(28, (String) ObjMRItems.getAttribute("GATE_PASS_OUT").getObj().toString());
                pstmh.setString(29, (String) ObjMRItems.getAttribute("GATE_PASS_IN").getObj().toString());
                pstmh.setString(30, (String) ObjMRItems.getAttribute("MC_RPM").getObj().toString());
                pstmh.setString(31, (String) ObjMRItems.getAttribute("PICKS_10CM").getObj().toString());
                pstmh.setString(32, (String) ObjMRItems.getAttribute("START_READING").getObj().toString());
                pstmh.setString(33, (String) ObjMRItems.getAttribute("END_READING").getObj().toString());
                pstmh.setString(34, (String) ObjMRItems.getAttribute("PICK").getObj().toString());
                pstmh.setString(35, (String) ObjMRItems.getAttribute("TOTAL_WEAVE_TIME").getObj().toString());
                pstmh.setString(36, (String) ObjMRItems.getAttribute("NO_WARP_NO_WEFT").getObj().toString());
                pstmh.setString(37, (String) ObjMRItems.getAttribute("NO_POWER_NO_AIR").getObj().toString());
                pstmh.setString(38, (String) ObjMRItems.getAttribute("BEAN_GAITING").getObj().toString());
                pstmh.setString(39, (String) ObjMRItems.getAttribute("RE_BEAM_GAITING").getObj().toString());
                pstmh.setString(40, (String) ObjMRItems.getAttribute("NO_WEAVER").getObj().toString());
                pstmh.setString(41, (String) ObjMRItems.getAttribute("CLOTH_REPAIR_TOTAL").getObj().toString());
                pstmh.setString(42, (String) ObjMRItems.getAttribute("NO_BEAM_READY").getObj().toString());
                pstmh.setString(43, (String) ObjMRItems.getAttribute("Q_CHANGES").getObj().toString());
                pstmh.setString(44, (String) ObjMRItems.getAttribute("MECH_REPAIR").getObj().toString());
                pstmh.setString(45, (String) ObjMRItems.getAttribute("ELE_RO_REPAIR").getObj().toString());
                pstmh.setString(46, (String) ObjMRItems.getAttribute("SHUTTLE_REPAIR_TOTAL").getObj().toString());
                pstmh.setString(47, (String) ObjMRItems.getAttribute("OVER_HAULING").getObj().toString());
                
                pstmh.setString(48, (String) ObjMRItems.getAttribute("NO_PIRN").getObj().toString());
                pstmh.setString(49, (String) ObjMRItems.getAttribute("OTHER").getObj().toString());
                
                pstmh.setString(50, (String) ObjMRItems.getAttribute("SELEVEDGE_EDGE_CORD_REPAIR").getObj().toString());
                pstmh.setString(51, (String) ObjMRItems.getAttribute("TOTAL_TIME").getObj().toString());
                pstmh.setString(52, (String) ObjMRItems.getAttribute("REMARK").getObj().toString());
                pstmh.setString(53, (String) ObjMRItems.getAttribute("CARRY_OVER").getObj().toString());
                pstmh.setString(54, (String) ObjMRItems.getAttribute("CLOTH_REPAIR").getObj().toString());
                pstmh.setString(55, (String) ObjMRItems.getAttribute("SHUTTLE_REPAIR").getObj().toString());

                pstmh.setString(56, (String) ObjMRItems.getAttribute("PICK_REPAIR").getObj().toString());
                pstmh.setString(57, (String) ObjMRItems.getAttribute("WARP_END_REPAIR").getObj().toString());
                pstmh.setString(58, (String) ObjMRItems.getAttribute("TEMPLE_REPAIR").getObj().toString());
                pstmh.setString(59, (String) ObjMRItems.getAttribute("MC_STOPPAGES").getObj().toString());
                pstmh.setString(60, (String) ObjMRItems.getAttribute("CR1").getObj().toString());
                pstmh.setString(61, (String) ObjMRItems.getAttribute("CR2").getObj().toString());
                pstmh.setString(62, (String) ObjMRItems.getAttribute("CR3").getObj().toString());
                pstmh.setString(63, (String) ObjMRItems.getAttribute("CR4").getObj().toString());
                pstmh.setString(64, (String) ObjMRItems.getAttribute("CR5").getObj().toString());
                pstmh.setString(65, (String) ObjMRItems.getAttribute("CR6").getObj().toString());
                pstmh.setString(66, (String) ObjMRItems.getAttribute("SR1").getObj().toString());
                pstmh.setString(67, (String) ObjMRItems.getAttribute("SR2").getObj().toString());
                pstmh.setString(68, (String) ObjMRItems.getAttribute("SR3").getObj().toString());
                pstmh.setString(69, (String) ObjMRItems.getAttribute("SR4").getObj().toString());
                pstmh.setString(70, (String) ObjMRItems.getAttribute("PR1").getObj().toString());
                pstmh.setString(71, (String) ObjMRItems.getAttribute("PR2").getObj().toString());
                pstmh.setString(72, (String) ObjMRItems.getAttribute("PR3").getObj().toString());
                pstmh.setString(73, (String) ObjMRItems.getAttribute("PR4").getObj().toString());
                pstmh.setString(74, (String) ObjMRItems.getAttribute("PR5").getObj().toString());
                pstmh.setString(75, (String) ObjMRItems.getAttribute("PR6").getObj().toString());
                pstmh.setString(76, (String) ObjMRItems.getAttribute("PR7").getObj().toString());
                pstmh.setString(77, (String) ObjMRItems.getAttribute("PR8").getObj().toString());
                pstmh.setString(78, (String) ObjMRItems.getAttribute("WR1").getObj().toString());
                pstmh.setString(79, (String) ObjMRItems.getAttribute("WR2").getObj().toString());
                pstmh.setString(80, (String) ObjMRItems.getAttribute("WR3").getObj().toString());
                pstmh.setString(81, (String) ObjMRItems.getAttribute("WR4").getObj().toString());
                pstmh.setString(82, (String) ObjMRItems.getAttribute("WR5").getObj().toString());
                pstmh.setString(83, (String) ObjMRItems.getAttribute("TR1").getObj().toString());
                pstmh.setString(84, (String) ObjMRItems.getAttribute("TR2").getObj().toString());
                pstmh.setString(85, (String) ObjMRItems.getAttribute("TR3").getObj().toString());
                pstmh.setString(86, (String) ObjMRItems.getAttribute("TR4").getObj().toString());
                pstmh.setString(87, (String) ObjMRItems.getAttribute("TR5").getObj().toString());
                
                pstmh.setString(88, getAttribute("HIERARCHY_ID").getVal()+"");
                pstmh.setString(89, getAttribute("CREATED_BY").getVal() +"");
                pstmh.setString(90, (String) getAttribute("CREATED_DATE").getObj());
                pstmh.setString(91, "0");//MODIFIED_BY
                pstmh.setString(92, "0000-00-00");//MODIFIED_DATE
                pstmh.setString(93, "0");//APPROVED
                pstmh.setString(94, "0000-00-00");//APPROVED_DATE
                pstmh.setString(95, "0");//REJECTED
                pstmh.setString(96, "0000-00-00");//REJECTED_DATE
                pstmh.setString(97, getAttribute("REJECTED_REMARKS").getString());
                pstmh.setString(98, "0");//CANCELLED
                pstmh.setString(99, "0000-00-00");//CANCELLED_DATE
                pstmh.setString(100, "0");//CHANGED
                pstmh.setString(101, "0000-00-00");//CHANGED_DATE
                pstmh.setString(102, str_split[1]);//FROM_IP
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
            s = "UPDATE PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL D,PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL_H H "
                    + "SET D.CREATED_BY=H.CREATED_BY ,D.CREATED_DATE=H.CREATED_DATE "
                    + "WHERE D.DOC_NO=H.DOC_NO AND REVISION_NO=1 AND "
                    + "D.DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "'";
            data.Execute(s);
            s = "UPDATE PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL "
                    + "SET KEY1=CONCAT(TRIM(YEAR_FROM),TRIM(YEAR_TO),TRIM(PARTY_CODE),TRIM(MACHINE_NO),"
                    + "TRIM(POSITION_NO)) "
                    + "WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "' ";
            data.Execute(s);

            
            //===================== Update the Approval Flow ======================//
            setAttribute("FROM", EITLERPGLOBAL.gUserID);

            //ApprovalFlow ObjFlow=new ApprovalFlow();
            clsFeltProductionApprovalFlow ObjFlow = new clsFeltProductionApprovalFlow();

            ObjFlow.ModuleID = clsWVG_Prod_Loom_WVR.ModuleID;
            ObjFlow.DocNo = (String) getAttribute("DOC_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("CREATED_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL";
            ObjFlow.IsCreator = true;
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
                data.Execute("UPDATE PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL SET REJECTED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='" + ObjFlow.DocNo + "' ");

                //Remove Old Records from FELT_DOC_DATA
                data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=" + clsWVG_Prod_Loom_WVR.ModuleID + " AND DOC_NO='" + ObjFlow.DocNo + "'");

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
                rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL_H WHERE DOC_NO='" + mDocNo + "' AND REVISION_NO=" + RevNo + "  ORDER BY DOC_NO");
            } else {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL WHERE DOC_NO='" + mDocNo + "'  ORDER BY DOC_NO");
            }
            rsTmp.first();

            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {
                Counter = Counter + 1;
                clsWVG_Prod_Loom_WVRItem ObjMRItems = new clsWVG_Prod_Loom_WVRItem();
                ObjMRItems.setAttribute("DOC_NO", rsTmp.getString("DOC_NO"));
                
                //ObjMRItems.setAttribute("REMARKS", rsTmp.getString("REMARKS"));
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
                
                ObjMRItems.setAttribute("DOC_DATE", rsTmp.getString("DOC_DATE"));
                ObjMRItems.setAttribute("SHIFT_ID", rsTmp.getString("SHIFT_ID"));
                ObjMRItems.setAttribute("EMP_NAME", rsTmp.getString("EMP_NAME"));
                ObjMRItems.setAttribute("EMP_TYPE", rsTmp.getString("EMP_TYPE"));
                ObjMRItems.setAttribute("EMP_NO", rsTmp.getString("EMP_NO"));
                ObjMRItems.setAttribute("REG_ROKDI", rsTmp.getString("REG_ROKDI"));
                ObjMRItems.setAttribute("CATOGORY_WISE_ENG", rsTmp.getString("CATOGORY_WISE_ENG"));
                ObjMRItems.setAttribute("LOOM_ENG", rsTmp.getString("LOOM_ENG"));
                ObjMRItems.setAttribute("BEAM_WARP_SR_NO", rsTmp.getString("BEAM_WARP_SR_NO"));
                ObjMRItems.setAttribute("PRODUCT_GROUP", rsTmp.getString("PRODUCT_GROUP"));
                ObjMRItems.setAttribute("REED_SPACE_METER", rsTmp.getString("REED_SPACE_METER"));
                ObjMRItems.setAttribute("PIECE_NO", rsTmp.getString("PIECE_NO"));
                ObjMRItems.setAttribute("WEFT_DETAILS", rsTmp.getString("WEFT_DETAILS"));
                ObjMRItems.setAttribute("SHIFT_STARTING", rsTmp.getString("SHIFT_STARTING"));
                ObjMRItems.setAttribute("IST_RECESS_OUT", rsTmp.getString("IST_RECESS_OUT"));
                ObjMRItems.setAttribute("IST_RECESS_IN", rsTmp.getString("IST_RECESS_IN"));
                ObjMRItems.setAttribute("IIND_RECESS_OUT", rsTmp.getString("IIND_RECESS_OUT"));
                ObjMRItems.setAttribute("IIND_RECESS_IN", rsTmp.getString("IIND_RECESS_IN"));
                ObjMRItems.setAttribute("IIIRD_RECESS_OUT", rsTmp.getString("IIIRD_RECESS_OUT"));
                ObjMRItems.setAttribute("IIIRD_RECESS_IN", rsTmp.getString("IIIRD_RECESS_IN"));
                ObjMRItems.setAttribute("SHIFT_END_OUT", rsTmp.getString("SHIFT_END_OUT"));
                ObjMRItems.setAttribute("GATE_PASS_OUT", rsTmp.getString("GATE_PASS_OUT"));
                ObjMRItems.setAttribute("GATE_PASS_IN", rsTmp.getString("GATE_PASS_IN"));
                ObjMRItems.setAttribute("MC_RPM", rsTmp.getString("MC_RPM"));
                ObjMRItems.setAttribute("PICKS_10CM", rsTmp.getString("PICKS_10CM"));
                ObjMRItems.setAttribute("START_READING", rsTmp.getString("START_READING"));
                ObjMRItems.setAttribute("END_READING", rsTmp.getString("END_READING"));
                ObjMRItems.setAttribute("PICK", rsTmp.getString("PICK"));
                ObjMRItems.setAttribute("TOTAL_WEAVE_TIME", rsTmp.getString("TOTAL_WEAVE_TIME"));
                ObjMRItems.setAttribute("NO_WARP_NO_WEFT", rsTmp.getString("NO_WARP_NO_WEFT"));
                ObjMRItems.setAttribute("NO_POWER_NO_AIR", rsTmp.getString("NO_POWER_NO_AIR"));
                ObjMRItems.setAttribute("BEAN_GAITING", rsTmp.getString("BEAN_GAITING"));
                ObjMRItems.setAttribute("RE_BEAM_GAITING", rsTmp.getString("RE_BEAM_GAITING"));
                ObjMRItems.setAttribute("NO_WEAVER", rsTmp.getString("NO_WEAVER"));
                ObjMRItems.setAttribute("CLOTH_REPAIR_TOTAL", rsTmp.getString("CLOTH_REPAIR_TOTAL"));
                ObjMRItems.setAttribute("NO_BEAM_READY", rsTmp.getString("NO_BEAM_READY"));
                ObjMRItems.setAttribute("Q_CHANGES", rsTmp.getString("Q_CHANGES"));
                ObjMRItems.setAttribute("MECH_REPAIR", rsTmp.getString("MECH_REPAIR"));
                ObjMRItems.setAttribute("ELE_RO_REPAIR", rsTmp.getString("ELE_RO_REPAIR"));
                ObjMRItems.setAttribute("SHUTTLE_REPAIR_TOTAL", rsTmp.getString("SHUTTLE_REPAIR_TOTAL"));
                ObjMRItems.setAttribute("OVER_HAULING", rsTmp.getString("OVER_HAULING"));
                
                ObjMRItems.setAttribute("NO_PIRN", rsTmp.getString("NO_PIRN"));
                ObjMRItems.setAttribute("OTHER", rsTmp.getString("OTHER"));
                
                ObjMRItems.setAttribute("SELEVEDGE_EDGE_CORD_REPAIR", rsTmp.getString("SELEVEDGE_EDGE_CORD_REPAIR"));
                ObjMRItems.setAttribute("TOTAL_TIME", rsTmp.getString("TOTAL_TIME"));
                ObjMRItems.setAttribute("REMARK", rsTmp.getString("REMARK"));
                ObjMRItems.setAttribute("CARRY_OVER", rsTmp.getString("CARRY_OVER"));
                ObjMRItems.setAttribute("CLOTH_REPAIR", rsTmp.getString("CLOTH_REPAIR"));
                ObjMRItems.setAttribute("SHUTTLE_REPAIR", rsTmp.getString("SHUTTLE_REPAIR"));
                
                ObjMRItems.setAttribute("PICK_REPAIR", rsTmp.getString("PICK_REPAIR"));
                ObjMRItems.setAttribute("WARP_END_REPAIR", rsTmp.getString("WARP_END_REPAIR"));
                ObjMRItems.setAttribute("TEMPLE_REPAIR", rsTmp.getString("TEMPLE_REPAIR"));
                ObjMRItems.setAttribute("MC_STOPPAGES", rsTmp.getString("MC_STOPPAGES"));
                ObjMRItems.setAttribute("CR1", rsTmp.getString("CR1"));
                ObjMRItems.setAttribute("CR2", rsTmp.getString("CR2"));
                ObjMRItems.setAttribute("CR3", rsTmp.getString("CR3"));
                ObjMRItems.setAttribute("CR4", rsTmp.getString("CR4"));
                ObjMRItems.setAttribute("CR5", rsTmp.getString("CR5"));
                ObjMRItems.setAttribute("CR6", rsTmp.getString("CR6"));
                ObjMRItems.setAttribute("SR1", rsTmp.getString("SR1"));
                ObjMRItems.setAttribute("SR2", rsTmp.getString("SR2"));
                ObjMRItems.setAttribute("SR3", rsTmp.getString("SR3"));
                ObjMRItems.setAttribute("SR4", rsTmp.getString("SR4"));
                ObjMRItems.setAttribute("PR1", rsTmp.getString("PR1"));
                ObjMRItems.setAttribute("PR2", rsTmp.getString("PR2"));
                ObjMRItems.setAttribute("PR3", rsTmp.getString("PR3"));
                ObjMRItems.setAttribute("PR4", rsTmp.getString("PR4"));
                ObjMRItems.setAttribute("PR5", rsTmp.getString("PR5"));
                ObjMRItems.setAttribute("PR6", rsTmp.getString("PR6"));
                ObjMRItems.setAttribute("PR7", rsTmp.getString("PR7"));
                ObjMRItems.setAttribute("PR8", rsTmp.getString("PR8"));
                ObjMRItems.setAttribute("WR1", rsTmp.getString("WR1"));
                ObjMRItems.setAttribute("WR2", rsTmp.getString("WR2"));
                ObjMRItems.setAttribute("WR3", rsTmp.getString("WR3"));
                ObjMRItems.setAttribute("WR4", rsTmp.getString("WR4"));
                ObjMRItems.setAttribute("WR5", rsTmp.getString("WR5"));
                ObjMRItems.setAttribute("TR1", rsTmp.getString("TR1"));
                ObjMRItems.setAttribute("TR2", rsTmp.getString("TR2"));
                ObjMRItems.setAttribute("TR3", rsTmp.getString("TR3"));
                ObjMRItems.setAttribute("TR4", rsTmp.getString("TR4"));
                ObjMRItems.setAttribute("TR5", rsTmp.getString("TR5"));
                
                
                colMRItems.put(Long.toString(Counter), ObjMRItems);
                rsTmp.next();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL WHERE DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=820 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND STATUS='W'";
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
            String strSQL = "SELECT * FROM PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL_H WHERE DOC_NO='" + DocNo + "' GROUP BY REVISION_NO,DOC_NO ";
            rsTmp = data.getResult(strSQL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                while (!rsTmp.isAfterLast()) {
                    clsWVG_Prod_Loom_WVR objParty = new clsWVG_Prod_Loom_WVR();

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
            strSQL += "SELECT * FROM PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL WHERE " + Condition + " GROUP BY DOC_NO";
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery(strSQL);

            if (!rsResultSet.first()) {
                strSQL = "SELECT * FROM PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL WHERE CREATED_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND CREATED_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'  AND LEFT(DOC_NO,2)='BU' GROUP BY DOC_NO,YEAR_FROM  ORDER BY DOC_NO";
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL WHERE DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=820 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND TYPE='C' AND STATUS='W'";
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

    //Deletes current record
    public boolean Delete(int pUserID) {
        try {
            int lCompanyID = EITLERPGLOBAL.gCompanyID;
            String lDocNo = (String) getAttribute("DOC_NO").getObj();
            String strSQL = "";

            //First check that record is editable
            if (CanDelete(lCompanyID, lDocNo, pUserID)) {
                String strQry = "DELETE FROM PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL WHERE DOC_NO='" + lDocNo + "'";
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
                strSQL = "SELECT DISTINCT PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL.DOC_NO,PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL.CREATED_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=820 ORDER BY PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL.DOC_NO,PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL.CREATED_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=820 ORDER BY PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL.CREATED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL.DOC_NO,PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL.CREATED_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=820 ORDER BY PRODUCTION.FELT_WVG_PROD_LOOM_WVR_DETAIL.DOC_NO";
            }

            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {

                Counter = Counter + 1;
                clsWVG_Prod_Loom_WVR ObjItem = new clsWVG_Prod_Loom_WVR();

                //------------- Header Fields --------------------//
                ObjItem.setAttribute("DOC_NO", rsTmp.getString("DOC_NO"));
                ObjItem.setAttribute("DOC_DATE", rsTmp.getString("CREATED_DATE"));
                ObjItem.setAttribute("RECEIVED_DATE", rsTmp.getString("RECEIVED_DATE"));
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
            rsResultSet = Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_FELT_WVG_PROD_LOOM_WVR_DETAIL_H WHERE DOC_NO='" + pDocNo + "' AND LEFT(DOC_NO,2)='BU' GROUP BY DOC_NO ORDER BY REVISION_NO");
            Ready = true;
            HistoryView = true;
            MoveFirst();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    
}
