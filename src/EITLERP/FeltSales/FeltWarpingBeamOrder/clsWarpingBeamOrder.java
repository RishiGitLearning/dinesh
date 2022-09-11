/*
 * clsSales_Party.java
 *
 * Created on April 3, 2009, 10:22 AM
 */
package EITLERP.FeltSales.FeltWarpingBeamOrder;

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
import EITLERP.Production.Production_Date_Updation;
import java.text.DecimalFormat;
import java.lang.Double;
import EITLERP.Production.clsFeltProductionApprovalFlow;

/**
 *
 * @author
 */
public class clsWarpingBeamOrder {

    public String LastError = "";
    private ResultSet rsResultSet, rsResultSet1;
    private Connection Conn;
    private Statement Stmt;
    DecimalFormat df = new DecimalFormat("##.##");
    private HashMap props;

    public boolean Ready = false;

    private boolean HistoryView = false;
    public static int ModuleID = 777; //72
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
    public clsWarpingBeamOrder() {
        LastError = "";
        props = new HashMap();
        //props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("REVISION_NO", new Variant(0));
        props.put("UPDATED_BY", new Variant(0));
        props.put("ENTRY_DATE", new Variant(""));

        props.put("DOC_NO", new Variant(""));
        props.put("DOC_DATE", new Variant(""));
        props.put("BEAM_NO", new Variant(""));
        props.put("LOOM_NO", new Variant(""));
        props.put("REED_SPACE", new Variant(""));
        props.put("WARP_DETAIL", new Variant(""));
        props.put("WARP_TEX", new Variant(""));
        props.put("ENDS_10_CM", new Variant(0.00));
        props.put("ACTUAL_WARP_RELISATION", new Variant(0.00));
        props.put("WARP_LENGTH", new Variant(0.00));
        props.put("REED_COUNT", new Variant(0.00));
        props.put("FABRIC_REALISATION_PER", new Variant(""));
        props.put("FROM_IP", new Variant(""));
        props.put("CHANGED", new Variant(""));
        props.put("CHANGED_DATE", new Variant(""));

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
            rsResultSet = Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER ORDER BY DOC_NO");
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

        try {

            // ---- History Related Changes ------ //
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHDetail = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rsHistory = stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail = stHDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL_H WHERE DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//

            //--------- Generate Gatepass requisition no.  ------------
            setAttribute("DOC_NO", clsFirstFree.getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 777, (int) getAttribute("FFNO").getVal(), true));
            //-------------------------------------------------

            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            rsResultSet.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
            rsResultSet.updateString("BEAM_NO", getAttribute("BEAM_NO").getString());
            rsResultSet.updateString("LOOM_NO", getAttribute("LOOM_NO").getString());
            rsResultSet.updateString("REED_SPACE", getAttribute("REED_SPACE").getString());
            rsResultSet.updateString("WARP_DETAIL", getAttribute("WARP_DETAIL").getString());
            rsResultSet.updateString("WARP_TEX", getAttribute("WARP_TEX").getString());
            rsResultSet.updateString("ENDS_10_CM", getAttribute("ENDS_10_CM").getString());
            rsResultSet.updateString("ACTUAL_WARP_RELISATION", getAttribute("ACTUAL_WARP_RELISATION").getString());
            rsResultSet.updateString("WARP_LENGTH", getAttribute("WARP_LENGTH").getString());
            rsResultSet.updateString("REED_COUNT", getAttribute("REED_COUNT").getString());
            rsResultSet.updateString("FABRIC_REALISATION_PER", getAttribute("FABRIC_REALISATION_PER").getString());
            rsResultSet.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
            rsResultSet.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
            rsResultSet.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsResultSet.updateBoolean("APPROVED", false);
            rsResultSet.updateString("APPROVED_DATE", "0000-00-00");
            rsResultSet.updateBoolean("CANCELED", false);
            rsResultSet.updateBoolean("REJECTED", false);
            rsResultSet.updateString("REJECTED_DATE", "0000-00-00");
            rsResultSet.updateBoolean("CHANGED", true);
            rsResultSet.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

            rsResultSet.insertRow();

            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO", 1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY", (int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            rsHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
            rsHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            rsHistory.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
            rsHistory.updateString("BEAM_NO", getAttribute("BEAM_NO").getString());
            rsHistory.updateString("LOOM_NO", getAttribute("LOOM_NO").getString());
            rsHistory.updateString("REED_SPACE", getAttribute("REED_SPACE").getString());
            rsHistory.updateString("WARP_DETAIL", getAttribute("WARP_DETAIL").getString());
            rsHistory.updateString("WARP_TEX", getAttribute("WARP_TEX").getString());
            rsHistory.updateString("ENDS_10_CM", getAttribute("ENDS_10_CM").getString());
            rsHistory.updateString("ACTUAL_WARP_RELISATION", getAttribute("ACTUAL_WARP_RELISATION").getString());
            rsHistory.updateString("WARP_LENGTH", getAttribute("WARP_LENGTH").getString());
            rsHistory.updateString("REED_COUNT", getAttribute("REED_COUNT").getString());
            rsHistory.updateString("FABRIC_REALISATION_PER", getAttribute("FABRIC_REALISATION_PER").getString());
            rsHistory.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
            rsHistory.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
            rsHistory.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            rsHistory.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsHistory.updateBoolean("APPROVED", false);
            rsHistory.updateString("APPROVED_DATE", "0000-00-00");
            rsHistory.updateBoolean("REJECTED", false);
            rsHistory.updateString("REJECTED_DATE", "0000-00-00");
            rsHistory.updateBoolean("CANCELED", false);
            rsHistory.updateBoolean("CHANGED", true);
            rsHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");

            rsHistory.updateString("FROM_IP", "" + str_split[1]);

            rsHistory.insertRow();

            System.out.println(1);

            Statement tmpStmt;

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL WHERE DOC_NO='1'");

            //Now Insert records into detail table
            for (int i = 1; i <= colMRItems.size(); i++) {
                clsWarpingBeamOrderItem ObjMRItems = (clsWarpingBeamOrderItem) colMRItems.get(Integer.toString(i));

                rsTmp.moveToInsertRow();

                rsTmp.updateString("DOC_NO", (String) getAttribute("DOC_NO").getObj());
                rsTmp.updateString("BEAM_NO", (String) getAttribute("BEAM_NO").getObj());
                rsTmp.updateString("LOOM_NO", (String) getAttribute("LOOM_NO").getObj());
                rsTmp.updateObject("SR_NO", ObjMRItems.getAttribute("SR_NO").getObj());
                rsTmp.updateString("PIECE_NO", (String) ObjMRItems.getAttribute("PIECE_NO").getObj());
                rsTmp.updateString("PARTY_CODE", (String) ObjMRItems.getAttribute("PARTY_CODE").getObj());
                rsTmp.updateString("PRODUCT_CODE", (String) ObjMRItems.getAttribute("PRODUCT_CODE").getObj());
                rsTmp.updateString("GRUP", (String) ObjMRItems.getAttribute("GRUP").getObj());
                rsTmp.updateString("STYLE", (String) ObjMRItems.getAttribute("STYLE").getObj());
                rsTmp.updateDouble("LENGTH", (double) ObjMRItems.getAttribute("LENGTH").getVal());
                rsTmp.updateDouble("WIDTH", (double) ObjMRItems.getAttribute("WIDTH").getVal());
                rsTmp.updateLong("GSM", (int) ObjMRItems.getAttribute("GSM").getVal());
                rsTmp.updateDouble("WEIGHT", (double) ObjMRItems.getAttribute("WEIGHT").getVal());
                rsTmp.updateLong("AUTO_SEQUENCE", (int) ObjMRItems.getAttribute("AUTO_SEQUENCE").getVal());
                rsTmp.updateLong("SEQUANCE_NO", (int) ObjMRItems.getAttribute("SEQUANCE_NO").getVal());
                rsTmp.updateString("PP_REMARK", (String) ObjMRItems.getAttribute("PP_REMARK").getObj());
                rsTmp.updateDouble("READ_SPACE", (double) ObjMRItems.getAttribute("READ_SPACE").getVal());
                rsTmp.updateDouble("THEORICAL_LENGTH_MTR", (double) ObjMRItems.getAttribute("THEORICAL_LENGTH_MTR").getVal());
                rsTmp.updateDouble("THEORICAL_PICKS_10_CM", (double) ObjMRItems.getAttribute("THEORICAL_PICKS_10_CM").getVal());
                rsTmp.updateDouble("TOTAL_PICKS", (double) ObjMRItems.getAttribute("TOTAL_PICKS").getVal());
                rsTmp.updateDouble("EXPECTED_GREV_SQ_MTR", (double) ObjMRItems.getAttribute("EXPECTED_GREV_SQ_MTR").getVal());
                rsTmp.updateString("WIP_OC_MONTHYEAR", (String) ObjMRItems.getAttribute("WIP_OC_MONTHYEAR").getObj());
                rsTmp.updateString("WIP_OC_LAST_DDMMYY", (String) ObjMRItems.getAttribute("WIP_OC_LAST_DDMMYY").getObj());
                rsTmp.updateString("INDICATOR", "INSERT");
                rsTmp.updateString("INDICATOR_DATE", (String) getAttribute("CREATED_DATE").getObj());
                rsTmp.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
                rsTmp.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED", true);
                rsTmp.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.insertRow();

                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO", 1);
                rsHDetail.updateString("DOC_NO", (String) getAttribute("DOC_NO").getObj());
                rsHDetail.updateString("BEAM_NO", (String) getAttribute("BEAM_NO").getObj());
                rsHDetail.updateString("LOOM_NO", (String) getAttribute("LOOM_NO").getObj());
                rsHDetail.updateObject("SR_NO", ObjMRItems.getAttribute("SR_NO").getObj());
                rsHDetail.updateString("PIECE_NO", (String) ObjMRItems.getAttribute("PIECE_NO").getObj());
                rsHDetail.updateString("PARTY_CODE", (String) ObjMRItems.getAttribute("PARTY_CODE").getObj());
                rsHDetail.updateString("PRODUCT_CODE", (String) ObjMRItems.getAttribute("PRODUCT_CODE").getObj());
                rsHDetail.updateString("GRUP", (String) ObjMRItems.getAttribute("GRUP").getObj());
                rsHDetail.updateString("STYLE", (String) ObjMRItems.getAttribute("STYLE").getObj());
                rsHDetail.updateDouble("LENGTH", (double) ObjMRItems.getAttribute("LENGTH").getVal());
                rsHDetail.updateDouble("WIDTH", (double) ObjMRItems.getAttribute("WIDTH").getVal());
                rsHDetail.updateLong("GSM", (int) ObjMRItems.getAttribute("GSM").getVal());
                rsHDetail.updateDouble("WEIGHT", (double) ObjMRItems.getAttribute("WEIGHT").getVal());
                rsHDetail.updateLong("AUTO_SEQUENCE", (int) ObjMRItems.getAttribute("AUTO_SEQUENCE").getVal());
                rsHDetail.updateLong("SEQUANCE_NO", (int) ObjMRItems.getAttribute("SEQUANCE_NO").getVal());
                rsHDetail.updateString("PP_REMARK", (String) ObjMRItems.getAttribute("PP_REMARK").getObj());
                rsHDetail.updateDouble("READ_SPACE", (double) ObjMRItems.getAttribute("READ_SPACE").getVal());
                rsHDetail.updateDouble("THEORICAL_LENGTH_MTR", (double) ObjMRItems.getAttribute("THEORICAL_LENGTH_MTR").getVal());
                rsHDetail.updateDouble("THEORICAL_PICKS_10_CM", (double) ObjMRItems.getAttribute("THEORICAL_PICKS_10_CM").getVal());
                rsHDetail.updateDouble("TOTAL_PICKS", (double) ObjMRItems.getAttribute("TOTAL_PICKS").getVal());
                rsHDetail.updateDouble("EXPECTED_GREV_SQ_MTR", (double) ObjMRItems.getAttribute("EXPECTED_GREV_SQ_MTR").getVal());
                rsHDetail.updateString("WIP_OC_MONTHYEAR", (String) ObjMRItems.getAttribute("WIP_OC_MONTHYEAR").getObj());
                rsHDetail.updateString("WIP_OC_LAST_DDMMYY", (String) ObjMRItems.getAttribute("WIP_OC_LAST_DDMMYY").getObj());
                rsHDetail.updateString("INDICATOR", "INSERT");
                rsHDetail.updateString("INDICATOR_DATE", (String) getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
                rsHDetail.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateBoolean("CHANGED", true);
                rsHDetail.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
            }

            //===================== Update the Approval Flow ======================//
            setAttribute("FROM", EITLERPGLOBAL.gUserID);

            //ApprovalFlow ObjFlow=new ApprovalFlow();
            clsFeltProductionApprovalFlow ObjFlow = new clsFeltProductionApprovalFlow();

            ObjFlow.ModuleID = clsWarpingBeamOrder.ModuleID;
            ObjFlow.DocNo = (String) getAttribute("DOC_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("CREATED_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER";
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
            String sql = "UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER P,PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL D "
                    + "SET PR_PIECE_AB_FLAG='AB' "
                    + "WHERE SUBSTRING(P.PR_PIECE_NO,1,5)=SUBSTRING(D.PIECE_NO,1,5) AND "
                    + "D.DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "' "
                    + "AND (D.PIECE_NO LIKE '%A%' OR D.PIECE_NO LIKE '%B%')";
            data.Execute(sql);
            sql = "UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER P,PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL D "
                    + "SET WIP_PIECE_AB_FLAG='AB' "
                    + "WHERE P.WIP_EXT_PIECE_NO=D.PIECE_NO AND "
                    + "D.DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "' "
                    + "AND (D.PIECE_NO LIKE '%A%' OR D.PIECE_NO LIKE '%B%')";
            data.Execute(sql);
            //================= Approval Flow Update complete ===================//
            //LoadData(EITLERPGLOBAL.gCompanyID);
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

        try {

            String AStatus = (String) getAttribute("APPROVAL_STATUS").getObj();
            if (AStatus.equals("R")) {
                Validate = false;
            }
            Validate = true;

            // ---- History Related Changes ------ //
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHDetail = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rsHistory = stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail = stHDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL_H WHERE DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//

            String theDocNo = getAttribute("DOC_NO").getString();
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER_H WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "'");
            RevNo++;
            String RevDocNo = (String) getAttribute("DOC_NO").getObj();
            if (clsFeltProductionApprovalFlow.IsCreator(ModuleID, (String) getAttribute("DOC_NO").getObj())) {
                rsResultSet.updateString("BEAM_NO", getAttribute("BEAM_NO").getString());
                rsResultSet.updateString("LOOM_NO", getAttribute("LOOM_NO").getString());
                rsResultSet.updateString("REED_SPACE", getAttribute("REED_SPACE").getString());
                rsResultSet.updateString("WARP_DETAIL", getAttribute("WARP_DETAIL").getString());
                rsResultSet.updateString("WARP_TEX", getAttribute("WARP_TEX").getString());
                rsResultSet.updateString("ENDS_10_CM", getAttribute("ENDS_10_CM").getString());
                rsResultSet.updateString("ACTUAL_WARP_RELISATION", getAttribute("ACTUAL_WARP_RELISATION").getString());
                rsResultSet.updateString("WARP_LENGTH", getAttribute("WARP_LENGTH").getString());
                rsResultSet.updateString("REED_COUNT", getAttribute("REED_COUNT").getString());
                rsResultSet.updateString("FABRIC_REALISATION_PER", getAttribute("FABRIC_REALISATION_PER").getString());
                rsResultSet.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                rsResultSet.updateInt("MODIFIED_BY", getAttribute("MODIFIED_BY").getInt());
                rsResultSet.updateString("MODIFIED_DATE", getAttribute("MODIFIED_DATE").getString());
                rsResultSet.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
                rsResultSet.updateString("REJECTED_DATE", "0000-00-00");
                rsResultSet.updateBoolean("CHANGED", true);
                rsResultSet.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsResultSet.updateBoolean("CANCELED", false);
                rsResultSet.updateRow();

                String mDocNo = (String) getAttribute("DOC_NO").getObj();

                data.Execute("DELETE FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL WHERE DOC_NO='" + mDocNo + "'");

                Statement tmpStmt;

                tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet rsTmp1 = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL WHERE DOC_NO='1'");

                //Now Insert records into detail table
                for (int i = 1; i <= colMRItems.size(); i++) {
                    clsWarpingBeamOrderItem ObjMRItems = (clsWarpingBeamOrderItem) colMRItems.get(Integer.toString(i));
                    rsTmp1.moveToInsertRow();
                    rsTmp1.updateString("DOC_NO", (String) getAttribute("DOC_NO").getObj());
                    rsTmp1.updateString("BEAM_NO", (String) getAttribute("BEAM_NO").getObj());
                    rsTmp1.updateString("LOOM_NO", (String) getAttribute("LOOM_NO").getObj());
                    rsTmp1.updateObject("SR_NO", ObjMRItems.getAttribute("SR_NO").getObj());
                    rsTmp1.updateString("PIECE_NO", (String) ObjMRItems.getAttribute("PIECE_NO").getObj());
                    rsTmp1.updateString("PARTY_CODE", (String) ObjMRItems.getAttribute("PARTY_CODE").getObj());
                    rsTmp1.updateString("PRODUCT_CODE", (String) ObjMRItems.getAttribute("PRODUCT_CODE").getObj());
                    rsTmp1.updateString("GRUP", (String) ObjMRItems.getAttribute("GRUP").getObj());
                    rsTmp1.updateString("STYLE", (String) ObjMRItems.getAttribute("STYLE").getObj());
                    rsTmp1.updateDouble("LENGTH", (double) ObjMRItems.getAttribute("LENGTH").getVal());
                    rsTmp1.updateDouble("WIDTH", (double) ObjMRItems.getAttribute("WIDTH").getVal());
                    rsTmp1.updateLong("GSM", (int) ObjMRItems.getAttribute("GSM").getVal());
                    rsTmp1.updateDouble("WEIGHT", (double) ObjMRItems.getAttribute("WEIGHT").getVal());
                    rsTmp1.updateLong("AUTO_SEQUENCE", (int) ObjMRItems.getAttribute("AUTO_SEQUENCE").getVal());
                    rsTmp1.updateLong("SEQUANCE_NO", (int) ObjMRItems.getAttribute("SEQUANCE_NO").getVal());
                    rsTmp1.updateString("PP_REMARK", (String) ObjMRItems.getAttribute("PP_REMARK").getObj());
                    rsTmp1.updateDouble("READ_SPACE", (double) ObjMRItems.getAttribute("READ_SPACE").getVal());
                    rsTmp1.updateDouble("THEORICAL_LENGTH_MTR", (double) ObjMRItems.getAttribute("THEORICAL_LENGTH_MTR").getVal());
                    rsTmp1.updateDouble("THEORICAL_PICKS_10_CM", (double) ObjMRItems.getAttribute("THEORICAL_PICKS_10_CM").getVal());
                    rsTmp1.updateDouble("TOTAL_PICKS", (double) ObjMRItems.getAttribute("TOTAL_PICKS").getVal());
                    rsTmp1.updateDouble("EXPECTED_GREV_SQ_MTR", (double) ObjMRItems.getAttribute("EXPECTED_GREV_SQ_MTR").getVal());
                    rsTmp1.updateString("WIP_OC_MONTHYEAR", (String) ObjMRItems.getAttribute("WIP_OC_MONTHYEAR").getObj());
                    rsTmp1.updateString("WIP_OC_LAST_DDMMYY", (String) ObjMRItems.getAttribute("WIP_OC_LAST_DDMMYY").getObj());
                    rsTmp1.updateString("INDICATOR", "INSERT");
                    rsTmp1.updateString("INDICATOR_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                    rsTmp1.updateInt("MODIFIED_BY", getAttribute("MODIFIED_BY").getInt());
                    rsTmp1.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                    rsTmp1.updateBoolean("CHANGED", true);
                    rsTmp1.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsTmp1.insertRow();

                    rsHDetail.moveToInsertRow();
                    rsHDetail.updateInt("REVISION_NO", RevNo);
                    rsHDetail.updateString("DOC_NO", (String) getAttribute("DOC_NO").getObj());
                    rsHDetail.updateString("BEAM_NO", (String) getAttribute("BEAM_NO").getObj());
                    rsHDetail.updateString("LOOM_NO", (String) getAttribute("LOOM_NO").getObj());
                    rsHDetail.updateObject("SR_NO", ObjMRItems.getAttribute("SR_NO").getObj());
                    rsHDetail.updateString("PIECE_NO", (String) ObjMRItems.getAttribute("PIECE_NO").getObj());
                    rsHDetail.updateString("PARTY_CODE", (String) ObjMRItems.getAttribute("PARTY_CODE").getObj());
                    rsHDetail.updateString("PRODUCT_CODE", (String) ObjMRItems.getAttribute("PRODUCT_CODE").getObj());
                    rsHDetail.updateString("GRUP", (String) ObjMRItems.getAttribute("GRUP").getObj());
                    rsHDetail.updateString("STYLE", (String) ObjMRItems.getAttribute("STYLE").getObj());
                    rsHDetail.updateDouble("LENGTH", (double) ObjMRItems.getAttribute("LENGTH").getVal());
                    rsHDetail.updateDouble("WIDTH", (double) ObjMRItems.getAttribute("WIDTH").getVal());
                    rsHDetail.updateLong("GSM", (int) ObjMRItems.getAttribute("GSM").getVal());
                    rsHDetail.updateDouble("WEIGHT", (double) ObjMRItems.getAttribute("WEIGHT").getVal());
                    rsHDetail.updateLong("AUTO_SEQUENCE", (int) ObjMRItems.getAttribute("AUTO_SEQUENCE").getVal());
                    rsHDetail.updateLong("SEQUANCE_NO", (int) ObjMRItems.getAttribute("SEQUANCE_NO").getVal());
                    rsHDetail.updateString("PP_REMARK", (String) ObjMRItems.getAttribute("PP_REMARK").getObj());
                    rsHDetail.updateDouble("READ_SPACE", (double) ObjMRItems.getAttribute("READ_SPACE").getVal());
                    rsHDetail.updateDouble("THEORICAL_LENGTH_MTR", (double) ObjMRItems.getAttribute("THEORICAL_LENGTH_MTR").getVal());
                    rsHDetail.updateDouble("THEORICAL_PICKS_10_CM", (double) ObjMRItems.getAttribute("THEORICAL_PICKS_10_CM").getVal());
                    rsHDetail.updateDouble("TOTAL_PICKS", (double) ObjMRItems.getAttribute("TOTAL_PICKS").getVal());
                    rsHDetail.updateDouble("EXPECTED_GREV_SQ_MTR", (double) ObjMRItems.getAttribute("EXPECTED_GREV_SQ_MTR").getVal());
                    rsHDetail.updateString("WIP_OC_MONTHYEAR", (String) ObjMRItems.getAttribute("WIP_OC_MONTHYEAR").getObj());
                    rsHDetail.updateString("WIP_OC_LAST_DDMMYY", (String) ObjMRItems.getAttribute("WIP_OC_LAST_DDMMYY").getObj());
                    rsHDetail.updateString("INDICATOR", "INSERT");
                    rsHDetail.updateString("INDICATOR_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                    rsHDetail.updateInt("MODIFIED_BY", getAttribute("MODIFIED_BY").getInt());
                    rsHDetail.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                    rsHDetail.updateBoolean("CHANGED", true);
                    rsHDetail.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsHDetail.insertRow();
                }

            }

            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO", RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY", (int) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            rsHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
            rsHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            rsHistory.updateString("BEAM_NO", getAttribute("BEAM_NO").getString());
            rsHistory.updateString("LOOM_NO", getAttribute("LOOM_NO").getString());
            rsHistory.updateString("REED_SPACE", getAttribute("REED_SPACE").getString());
            rsHistory.updateString("WARP_DETAIL", getAttribute("WARP_DETAIL").getString());
            rsHistory.updateString("WARP_TEX", getAttribute("WARP_TEX").getString());
            rsHistory.updateString("ENDS_10_CM", getAttribute("ENDS_10_CM").getString());
            rsHistory.updateString("ACTUAL_WARP_RELISATION", getAttribute("ACTUAL_WARP_RELISATION").getString());
            rsHistory.updateString("WARP_LENGTH", getAttribute("WARP_LENGTH").getString());
            rsHistory.updateString("REED_COUNT", getAttribute("REED_COUNT").getString());
            rsHistory.updateString("FABRIC_REALISATION_PER", getAttribute("FABRIC_REALISATION_PER").getString());
            rsHistory.updateInt("MODIFIED_BY", getAttribute("MODIFIED_BY").getInt());
            rsHistory.updateString("MODIFIED_DATE", getAttribute("MODIFIED_DATE").getString());
            rsHistory.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            rsHistory.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsHistory.updateBoolean("APPROVED", false);
            rsHistory.updateString("APPROVED_DATE", "0000-00-00");
            rsHistory.updateBoolean("REJECTED", false);
            rsHistory.updateString("REJECTED_DATE", "0000-00-00");
            rsHistory.updateBoolean("CANCELED", false);
            rsHistory.updateBoolean("CHANGED", true);
            rsHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");

            rsHistory.updateString("FROM_IP", "" + str_split[1]);

            rsHistory.insertRow();

            //First remove the old rows
            //======== Update the Approval Flow =========
            setAttribute("FROM", EITLERPGLOBAL.gUserID);

            //ApprovalFlow ObjFlow=new ApprovalFlow();
            clsFeltProductionApprovalFlow ObjFlow = new clsFeltProductionApprovalFlow();

            ObjFlow.ModuleID = clsWarpingBeamOrder.ModuleID;
            ObjFlow.DocNo = (String) getAttribute("DOC_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("CREATED_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER";
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
            //==========================================//

            //==== Handling Rejected Documents ==========//
            boolean IsRejected = getAttribute("REJECTED").getBool();

            if (IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='" + ObjFlow.DocNo + "' ");

                //Remove Old Records from FELT_DOC_DATA
                data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=" + clsWarpingBeamOrder.ModuleID + " AND DOC_NO='" + ObjFlow.DocNo + "'");

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
            String sql = "UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER P,PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL D "
                    + "SET PR_PIECE_AB_FLAG='AB' "
                    + "WHERE SUBSTRING(P.PR_PIECE_NO,1,5)=SUBSTRING(D.PIECE_NO,1,5) AND "
                    + "D.DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "' "
                    + "AND (D.PIECE_NO LIKE '%A%' OR D.PIECE_NO LIKE '%B%')";
            data.Execute(sql);
            sql = "UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER P,PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL D "
                    + "SET WIP_PIECE_AB_FLAG='AB' "
                    + "WHERE P.PR_PIECE_NO=D.PIECE_NO AND "
                    + "D.DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "' "
                    + "AND (D.PIECE_NO LIKE '%A%' OR D.PIECE_NO LIKE '%B%')";
            data.Execute(sql);
            if (ObjFlow.Status.equals("F")) {
                sql = "UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER P,"
                        + "(SELECT PIECE_NO,DOC_NO,WEIGHT FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL "
                        + "WHERE PIECE_NO NOT LIKE '%A%' AND PIECE_NO NOT LIKE '%B%' "
                        + "AND DOC_NO ='" + (String) getAttribute("DOC_NO").getObj() + "'  ) AS D "
                        + "SET PR_PIECE_STAGE='WEAVING',PR_WIP_STATUS='WARPED',PR_WARP_DATE=CURDATE(),"
                        + "PR_WARP_A_DATE=CURDATE(),"
                        + "PR_WARPING_WEIGHT_A=D.WEIGHT,"
                        + "PR_PIECE_AB_FLAG=NULL "
                        + "WHERE SUBSTRING(P.PR_PIECE_NO,1,5)=SUBSTRING(D.PIECE_NO,1,5)";
                data.Execute(sql);

                sql = "UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER P,"
                        + "(SELECT PIECE_NO,DOC_NO,WEIGHT FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL "
                        + "WHERE PIECE_NO NOT LIKE '%A%' AND PIECE_NO NOT LIKE '%B%' "
                        + "AND DOC_NO ='" + (String) getAttribute("DOC_NO").getObj() + "'  ) AS D "
                        + "SET WIP_PIECE_STAGE='WEAVING',WIP_STATUS='WARPED',WIP_WARP_DATE=CURDATE(),"
                        + "WIP_WARP_A_DATE=CURDATE(),"
                        + "WIP_PIECE_AB_FLAG=NULL, "
                        + "WIP_WARPING_WEIGHT_A=D.WEIGHT,WIP_WARPING_WEIGHT=D.WEIGHT "
                        + "WHERE P.WIP_EXT_PIECE_NO=D.PIECE_NO";
                data.Execute(sql);

                sql = "UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER P,"
                        + "(SELECT PIECE_NO,DOC_NO,WEIGHT FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL "
                        + "WHERE PIECE_NO LIKE '%A%'  "
                        + "AND DOC_NO ='" + (String) getAttribute("DOC_NO").getObj() + "'  ) AS D "
                        + "SET PR_PIECE_STAGE='WEAVING',PR_WIP_STATUS='WARPED',PR_WARP_DATE=CURDATE(),"
                        + "PR_WARP_A_DATE=CURDATE(),"
                        + "PR_WARPING_WEIGHT_A=D.WEIGHT,"
                        + "PR_PIECE_AB_FLAG='AB' "
                        + "WHERE SUBSTRING(P.PR_PIECE_NO,1,5)=SUBSTRING(D.PIECE_NO,1,5)";
                data.Execute(sql);

                sql = "UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER P,"
                        + "(SELECT PIECE_NO,DOC_NO,WEIGHT FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL "
                        + "WHERE  PIECE_NO LIKE '%A%' "
                        + "AND DOC_NO ='" + (String) getAttribute("DOC_NO").getObj() + "'  ) AS D "
                        + "SET WIP_PIECE_STAGE='WEAVING',WIP_STATUS='WARPED',WIP_WARP_DATE=CURDATE(),"
                        + "WIP_WARP_A_DATE=CURDATE(),"
                        + "WIP_PIECE_AB_FLAG='AB', "
                        + "WIP_WARPING_WEIGHT_B=D.WEIGHT,WIP_WARPING_WEIGHT=D.WEIGHT "
                        + "WHERE P.WIP_EXT_PIECE_NO=D.PIECE_NO";
                data.Execute(sql);

                sql = "UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER P,"
                        + "(SELECT PIECE_NO,DOC_NO,WEIGHT FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL "
                        + "WHERE  PIECE_NO LIKE '%B%' "
                        + "AND DOC_NO ='" + (String) getAttribute("DOC_NO").getObj() + "'  ) AS D "
                        + "SET PR_PIECE_STAGE='WEAVING',PR_WIP_STATUS='WARPED',PR_WARP_DATE=CURDATE(),"
                        + "PR_WARP_B_DATE=CURDATE(),"
                        + "PR_WARPING_WEIGHT_B=D.WEIGHT,"
                        + "PR_PIECE_AB_FLAG='AB' "
                        + "WHERE SUBSTRING(P.PR_PIECE_NO,1,5)=SUBSTRING(D.PIECE_NO,1,5)";
                data.Execute(sql);

                sql = "UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER P,"
                        + "(SELECT PIECE_NO,DOC_NO,WEIGHT FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL "
                        + "WHERE  PIECE_NO LIKE '%B%' "
                        + "AND DOC_NO ='" + (String) getAttribute("DOC_NO").getObj() + "'  ) AS D "
                        + "SET WIP_PIECE_STAGE='WEAVING',WIP_STATUS='WARPED',WIP_WARP_DATE=CURDATE(),"
                        + "WIP_WARP_B_DATE=CURDATE(),"
                        + "WIP_PIECE_AB_FLAG='AB', "
                        + "WIP_WARPING_WEIGHT_B=D.WEIGHT,WIP_WARPING_WEIGHT=D.WEIGHT "
                        + "WHERE P.WIP_EXT_PIECE_NO=D.PIECE_NO";
                data.Execute(sql);
                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER, "
                        + "(SELECT PIECE_NO AS PIECE,"
                        + "CASE WHEN INDICATOR ='INSERT' THEN H.APPROVED_DATE  WHEN INDICATOR ='ADD' THEN D.INDICATOR_DATE "
                        + "WHEN COALESCE(INDICATOR,'') ='' THEN H.APPROVED_DATE END AS MAXDATE, "
                        + "GROUP_CONCAT(PIECE_NO,' ( ',CASE WHEN INDICATOR ='INSERT' THEN DATE_FORMAT(H.APPROVED_DATE,'%d/%m/%Y')  "
                        + "WHEN INDICATOR ='ADD' THEN DATE_FORMAT(D.INDICATOR_DATE,'%d/%m/%Y') "
                        + "WHEN COALESCE(INDICATOR,'') ='' THEN DATE_FORMAT(H.APPROVED_DATE,'%d/%m/%Y') END,' ) '  ORDER BY PIECE_NO SEPARATOR ', ') AS PIECEPROD "
                        + "FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL D,"
                        + "PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER H "
                        + "WHERE H.DOC_NO ='" + (String) getAttribute("DOC_NO").getObj() + "' AND H.DOC_NO = D.DOC_NO AND APPROVED =1 AND CANCELED =0  AND "
                        + "INDICATOR NOT IN ('DELETE')  GROUP BY SUBSTRING(PIECE_NO,1,5))  AS B "
                        + "SET PR_WARP_LAYER_REMARK = PIECEPROD, PR_WARP_DATE=MAXDATE, "
                        + "PR_WARP_A_DATE = MAXDATE  WHERE PIECE =PR_PIECE_NO ");
                data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER, "
                        + "(SELECT PIECE_NO AS PIECE,"
                        + "CASE WHEN INDICATOR ='INSERT' THEN H.APPROVED_DATE  WHEN INDICATOR ='ADD' THEN D.INDICATOR_DATE "
                        + "WHEN COALESCE(INDICATOR,'') ='' THEN H.APPROVED_DATE END AS MAXDATE, "
                        + "GROUP_CONCAT(PIECE_NO,' ( ',CASE WHEN INDICATOR ='INSERT' THEN DATE_FORMAT(H.APPROVED_DATE,'%d/%m/%Y')  "
                        + "WHEN INDICATOR ='ADD' THEN DATE_FORMAT(D.INDICATOR_DATE,'%d/%m/%Y') "
                        + "WHEN COALESCE(INDICATOR,'') ='' THEN DATE_FORMAT(H.APPROVED_DATE,'%d/%m/%Y') END,' ) '  ORDER BY PIECE_NO SEPARATOR ', ') AS PIECEPROD "
                        + "FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL D,"
                        + "PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER H "
                        + "WHERE H.DOC_NO ='" + (String) getAttribute("DOC_NO").getObj() + "'  AND H.DOC_NO = D.DOC_NO AND APPROVED =1 AND CANCELED =0  AND "
                        + "INDICATOR NOT IN ('DELETE')  GROUP BY SUBSTRING(PIECE_NO,1,5))  AS B "
                        + "SET WIP_WARP_LAYER_REMARK = PIECEPROD, WIP_WARP_DATE=MAXDATE "
                        + "  WHERE LEFT(PIECE,5) =LEFT(WIP_EXT_PIECE_NO,5) AND COALESCE(WIP_REJECTED_FLAG,0)=0");
                
                Production_Date_Updation pd=new Production_Date_Updation();

            }
            //--------- Approval Flow Update complete -----------
            //LoadData(EITLERPGLOBAL.gCompanyID);
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
            setAttribute("DOC_DATE", rsResultSet.getString("DOC_DATE"));
            setAttribute("BEAM_NO", rsResultSet.getString("BEAM_NO"));
            setAttribute("LOOM_NO", rsResultSet.getString("LOOM_NO"));
            setAttribute("REED_SPACE", rsResultSet.getString("REED_SPACE"));
            setAttribute("WARP_DETAIL", rsResultSet.getString("WARP_DETAIL"));
            setAttribute("WARP_TEX", rsResultSet.getString("WARP_TEX"));
            setAttribute("ENDS_10_CM", rsResultSet.getDouble("ENDS_10_CM"));
            setAttribute("ACTUAL_WARP_RELISATION", rsResultSet.getDouble("ACTUAL_WARP_RELISATION"));
            setAttribute("WARP_LENGTH", rsResultSet.getDouble("WARP_LENGTH"));
            setAttribute("REED_COUNT", rsResultSet.getString("REED_COUNT"));
            setAttribute("FABRIC_REALISATION_PER", rsResultSet.getString("FABRIC_REALISATION_PER"));

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
                //if (clsFeltProductionApprovalFlow.IsCreator(ModuleID, (String) getAttribute("DOC_NO").getObj())) {
                //    rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL_H WHERE (INDICATOR IS NULL OR INDICATOR IN ('INSERT','ADD','')) AND DOC_NO='" + mDocNo + "' AND REVISION_NO=" + RevNo + " ORDER BY SR_NO");
                //} else {
                    rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL_H WHERE (INDICATOR IS NULL OR INDICATOR IN ('INSERT','ADD','')) AND DOC_NO='" + mDocNo + "' AND REVISION_NO=" + RevNo + " ORDER BY SEQUANCE_NO");
                //}
            } else {
               // if (clsFeltProductionApprovalFlow.IsCreator(ModuleID, (String) getAttribute("DOC_NO").getObj())) {
               //     rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL WHERE (INDICATOR IS NULL OR INDICATOR IN ('INSERT','ADD','')) AND DOC_NO='" + mDocNo + "' ORDER BY SR_NO");
               // } else {
                    rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL WHERE (INDICATOR IS NULL OR INDICATOR IN ('INSERT','ADD','')) AND DOC_NO='" + mDocNo + "' ORDER BY SEQUANCE_NO");
                //}
            }
            rsTmp.first();

            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {
                Counter = Counter + 1;
                clsWarpingBeamOrderItem ObjMRItems = new clsWarpingBeamOrderItem();
                ObjMRItems.setAttribute("DOC_NO", rsTmp.getString("DOC_NO"));
                ObjMRItems.setAttribute("SR_NO", rsTmp.getLong("SR_NO"));
                ObjMRItems.setAttribute("BEAM_NO", rsTmp.getString("BEAM_NO"));
                ObjMRItems.setAttribute("LOOM_NO", rsTmp.getString("LOOM_NO"));
                ObjMRItems.setAttribute("PIECE_NO", rsTmp.getString("PIECE_NO"));
                ObjMRItems.setAttribute("PARTY_CODE", rsTmp.getString("PARTY_CODE"));
                ObjMRItems.setAttribute("PRODUCT_CODE", rsTmp.getString("PRODUCT_CODE"));
                ObjMRItems.setAttribute("GRUP", rsTmp.getString("GRUP"));
                ObjMRItems.setAttribute("STYLE", rsTmp.getString("STYLE"));
                ObjMRItems.setAttribute("LENGTH", EITLERPGLOBAL.round(rsTmp.getDouble("LENGTH"), 2));
                ObjMRItems.setAttribute("WIDTH", EITLERPGLOBAL.round(rsTmp.getDouble("WIDTH"), 2));
                ObjMRItems.setAttribute("GSM", EITLERPGLOBAL.round(rsTmp.getDouble("GSM"), 2));
                ObjMRItems.setAttribute("WEIGHT", EITLERPGLOBAL.round(rsTmp.getDouble("WEIGHT"), 2));
                ObjMRItems.setAttribute("SEQUANCE_NO", rsTmp.getInt("SEQUANCE_NO"));
                ObjMRItems.setAttribute("AUTO_SEQUENCE", rsTmp.getInt("AUTO_SEQUENCE"));
                ObjMRItems.setAttribute("READ_SPACE", EITLERPGLOBAL.round(rsTmp.getDouble("READ_SPACE"), 2));
                ObjMRItems.setAttribute("THEORICAL_LENGTH_MTR", EITLERPGLOBAL.round(rsTmp.getDouble("THEORICAL_LENGTH_MTR"), 2));
                ObjMRItems.setAttribute("THEORICAL_PICKS_10_CM", EITLERPGLOBAL.round(rsTmp.getDouble("THEORICAL_PICKS_10_CM"), 2));
                ObjMRItems.setAttribute("TOTAL_PICKS", EITLERPGLOBAL.round(rsTmp.getDouble("TOTAL_PICKS"), 2));
                ObjMRItems.setAttribute("EXPECTED_GREV_SQ_MTR", EITLERPGLOBAL.round(rsTmp.getDouble("EXPECTED_GREV_SQ_MTR"), 2));
                ObjMRItems.setAttribute("WEAVING_SEQUENCE", rsTmp.getInt("WEAVING_SEQUENCE"));
                ObjMRItems.setAttribute("WEAVING_DATE", rsTmp.getString("WEAVING_DATE"));
                ObjMRItems.setAttribute("PP_REMARK", rsTmp.getString("PP_REMARK"));
                ObjMRItems.setAttribute("WIP_OC_MONTHYEAR", rsTmp.getString("WIP_OC_MONTHYEAR"));
                ObjMRItems.setAttribute("WIP_OC_LAST_DDMMYY", rsTmp.getString("WIP_OC_LAST_DDMMYY"));
                ObjMRItems.setAttribute("WEAVING_WEIGHT", EITLERPGLOBAL.round(rsTmp.getDouble("WEAVING_WEIGHT"), 2));
                ObjMRItems.setAttribute("CREATED_BY", rsTmp.getLong("CREATED_BY"));
                ObjMRItems.setAttribute("CREATED_DATE", rsTmp.getString("CREATED_DATE"));
                ObjMRItems.setAttribute("MODIFIED_BY", rsTmp.getLong("MODIFIED_BY"));
                ObjMRItems.setAttribute("MODIFIED_DATE", rsTmp.getString("MODIFIED_DATE"));
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER WHERE DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=777 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND STATUS='W'";
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
            String strSQL = "SELECT DOC_NO,CREATED_DATE,REVISION_NO,UPDATED_BY,APPROVAL_STATUS,DATE_FORMAT(ENTRY_DATE,'%d/%m/%Y %H:%i:%S') AS ENTRY_DATE,REJECTED_REMARKS AS APPROVER_REMARKS,FROM_IP FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER_H WHERE DOC_NO='" + DocNo + "'";
            rsTmp = data.getResult(strSQL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                while (!rsTmp.isAfterLast()) {
                    clsWarpingBeamOrder objParty = new clsWarpingBeamOrder();

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
            strSQL += "SELECT * FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER WHERE " + Condition;
            System.out.println("Filter Query :" + strSQL);
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery(strSQL);

            if (!rsResultSet.first()) {
                strSQL = "SELECT * FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER WHERE CREATED_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND CREATED_DATE<='" + EITLERPGLOBAL.FinToDateDB + "' ORDER BY DOC_NO";
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER WHERE DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=777 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND TYPE='C' AND STATUS='W'";
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
        return false;
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
                strSQL = "SELECT PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER.DOC_NO,PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER.CREATED_DATE,RECEIVED_DATE,BEAM_NO FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=777 ORDER BY PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER.DOC_NO,PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER.CREATED_DATE,RECEIVED_DATE,BEAM_NO FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=777 ORDER BY PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER.CREATED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER.DOC_NO,PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER.CREATED_DATE,RECEIVED_DATE,BEAM_NO FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=777 ORDER BY PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER.DOC_NO";
            }

            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {

                Counter = Counter + 1;
                clsWarpingBeamOrder ObjItem = new clsWarpingBeamOrder();

                //------------- Header Fields --------------------//
                ObjItem.setAttribute("DOC_NO", rsTmp.getString("DOC_NO"));
                ObjItem.setAttribute("DOC_DATE", rsTmp.getString("CREATED_DATE"));
                ObjItem.setAttribute("RECEIVED_DATE", rsTmp.getString("RECEIVED_DATE"));
                ObjItem.setAttribute("BEAM_NO", rsTmp.getInt("BEAM_NO"));
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
            rsResultSet = Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER_H WHERE DOC_NO='" + pDocNo + "' ORDER BY REVISION_NO");
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
