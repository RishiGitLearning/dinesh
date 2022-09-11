/*
 * clsSales_Party.java
 *
 * Created on April 3, 2009, 10:22 AM
 */
package EITLERP.FeltSales.TrailPiece;

import EITLERP.FeltSales.FeltWarpingBeamOrder.*;
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
 * @author
 */
public class clsTrailPieceDispatchEntry {

    public String LastError = "";
    private ResultSet rsResultSet, rsResultSet1;
    private Connection Conn;
    private Statement Stmt;
    DecimalFormat df = new DecimalFormat("##.##");
    private HashMap props;

    public boolean Ready = false;

    private boolean HistoryView = false;
    public static int ModuleID = 801; //72
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
    public clsTrailPieceDispatchEntry() {
        LastError = "";
        props = new HashMap();
        //props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("REVISION_NO", new Variant(0));
        props.put("UPDATED_BY", new Variant(0));
        props.put("ENTRY_DATE", new Variant(""));

        props.put("DOC_NO", new Variant(""));
        props.put("DOC_DATE", new Variant(""));
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
            rsResultSet = Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_TRAIL_PIECE_DISPATCH  GROUP BY DOC_NO");
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

            rsHistory = stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_TRAIL_PIECE_DISPATCH_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();

            //------------------------------------//
            //--------- Generate Gatepass requisition no.  ------------
            setAttribute("DOC_NO", clsFirstFree.getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 801, (int) getAttribute("FFNO").getVal(), true));
            //-------------------------------------------------

            rsResultSet.first();

            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");

            //Now Insert records into detail table
            for (int i = 1; i <= colMRItems.size(); i++) {
                clsTrailPieceDispatchEntryItem ObjMRItems = (clsTrailPieceDispatchEntryItem) colMRItems.get(Integer.toString(i));

                rsResultSet.moveToInsertRow();
                rsResultSet.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                rsResultSet.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
                rsResultSet.updateString("FT_PIECE_NO", ObjMRItems.getAttribute("FT_PIECE_NO").getString());
                rsResultSet.updateString("MOUNTING_PLAN_DATE", ObjMRItems.getAttribute("MOUNTING_PLAN_DATE").getString());
                rsResultSet.updateString("MOUNTING_ACTUAL_DATE", ObjMRItems.getAttribute("MOUNTING_ACTUAL_DATE").getString());
                rsResultSet.updateDouble("EXPECTED_LIFE_DAYS", ObjMRItems.getAttribute("EXPECTED_LIFE_DAYS").getDouble());
                rsResultSet.updateString("DEMOUNTING_DATE", ObjMRItems.getAttribute("DEMOUNTING_DATE").getString());
                rsResultSet.updateDouble("ACTUAL_LIFE_DAYS", ObjMRItems.getAttribute("ACTUAL_LIFE_DAYS").getDouble());
                rsResultSet.updateString("PERFORMANCE_FEEDBACK", ObjMRItems.getAttribute("PERFORMANCE_FEEDBACK").getString());
                rsResultSet.updateString("REMARKS", ObjMRItems.getAttribute("REMARKS").getString());
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

                rsHistory.moveToInsertRow();
                rsHistory.updateInt("REVISION_NO", 1); //Revision No. 1 in case of Insert
                rsHistory.updateInt("UPDATED_BY", (int) getAttribute("CREATED_BY").getVal());
                rsHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
                rsHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                rsHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
                rsHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                rsHistory.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
                rsHistory.updateString("FT_PIECE_NO", ObjMRItems.getAttribute("FT_PIECE_NO").getString());
                rsHistory.updateString("MOUNTING_PLAN_DATE", ObjMRItems.getAttribute("MOUNTING_PLAN_DATE").getString());
                rsHistory.updateString("MOUNTING_ACTUAL_DATE", ObjMRItems.getAttribute("MOUNTING_ACTUAL_DATE").getString());
                rsHistory.updateDouble("EXPECTED_LIFE_DAYS", ObjMRItems.getAttribute("EXPECTED_LIFE_DAYS").getDouble());
                rsHistory.updateString("DEMOUNTING_DATE", ObjMRItems.getAttribute("DEMOUNTING_DATE").getString());
                rsHistory.updateDouble("ACTUAL_LIFE_DAYS", ObjMRItems.getAttribute("ACTUAL_LIFE_DAYS").getDouble());
                rsHistory.updateString("PERFORMANCE_FEEDBACK", ObjMRItems.getAttribute("PERFORMANCE_FEEDBACK").getString());
                rsHistory.updateString("REMARKS", ObjMRItems.getAttribute("REMARKS").getString());
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
                rsHistory.updateString("FROM_IP", "" + str_split[1]);
                rsHistory.insertRow();
            }

            //===================== Update the Approval Flow ======================//
            setAttribute("FROM", EITLERPGLOBAL.gUserID);

            //ApprovalFlow ObjFlow=new ApprovalFlow();
            clsFeltProductionApprovalFlow ObjFlow = new clsFeltProductionApprovalFlow();

            ObjFlow.ModuleID = clsTrailPieceDispatchEntry.ModuleID;
            ObjFlow.DocNo = (String) getAttribute("DOC_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("DOC_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "PRODUCTION.FELT_TRAIL_PIECE_DISPATCH";
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
            //LoadData(EITLERPGLOBAL.gCompanyID);
            if (ObjFlow.Status.equals("F")) {
                String sql = "UPDATE PRODUCTION.FELT_TRAIL_PIECE_DISPATCH D,PRODUCTION.FELT_TRAIL_PIECE_SELECTION S "
                        + "SET S.MOUNTING_PLAN_DATE=D.MOUNTING_PLAN_DATE,S.MOUNTING_ACTUAL_DATE=D.MOUNTING_ACTUAL_DATE,"
                        + "S.EXPECTED_LIFE_DAYS=D.EXPECTED_LIFE_DAYS,S.ACTUAL_LIFE_DAYS=D.ACTUAL_LIFE_DAYS,"
                        + "S.PERFORMANCE_FEEDBACK=D.PERFORMANCE_FEEDBACK,S.REMARKS=D.REMARKS "
                        + "WHERE D.FT_PIECE_NO=S.FT_PIECE_NO AND "
                        + "D.DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "'";
                data.Execute(sql);
            }

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

            rsHistory = stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_TRAIL_PIECE_DISPATCH_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//

            String theDocNo = getAttribute("DOC_NO").getString();
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_TRAIL_PIECE_DISPATCH_H WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "'");
            RevNo++;

            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");

            String RevDocNo = (String) getAttribute("DOC_NO").getObj();
            if (clsFeltProductionApprovalFlow.IsCreator(ModuleID, (String) getAttribute("DOC_NO").getObj())) {

                String mDocNo = (String) getAttribute("DOC_NO").getObj();

                data.Execute("DELETE FROM PRODUCTION.FELT_TRAIL_PIECE_DISPATCH WHERE DOC_NO='" + mDocNo + "'");

                Statement tmpStmt;

                //Now Insert records into detail table
                for (int i = 1; i <= colMRItems.size(); i++) {
                    clsTrailPieceDispatchEntryItem ObjMRItems = (clsTrailPieceDispatchEntryItem) colMRItems.get(Integer.toString(i));
                    rsResultSet.moveToInsertRow();
                    rsResultSet.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                    rsResultSet.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
                    rsResultSet.updateString("FT_PIECE_NO", ObjMRItems.getAttribute("FT_PIECE_NO").getString());
                    rsResultSet.updateString("MOUNTING_PLAN_DATE", ObjMRItems.getAttribute("MOUNTING_PLAN_DATE").getString());
                    rsResultSet.updateString("MOUNTING_ACTUAL_DATE", ObjMRItems.getAttribute("MOUNTING_ACTUAL_DATE").getString());
                    rsResultSet.updateDouble("EXPECTED_LIFE_DAYS", ObjMRItems.getAttribute("EXPECTED_LIFE_DAYS").getDouble());
                    rsResultSet.updateString("DEMOUNTING_DATE", ObjMRItems.getAttribute("DEMOUNTING_DATE").getString());
                    rsResultSet.updateDouble("ACTUAL_LIFE_DAYS", ObjMRItems.getAttribute("ACTUAL_LIFE_DAYS").getDouble());
                    rsResultSet.updateString("PERFORMANCE_FEEDBACK", ObjMRItems.getAttribute("PERFORMANCE_FEEDBACK").getString());
                    rsResultSet.updateString("REMARKS", ObjMRItems.getAttribute("REMARKS").getString());
                    rsResultSet.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                    rsResultSet.updateInt("MODIFIED_BY", getAttribute("MODIFIED_BY").getInt());
                    rsResultSet.updateString("MODIFIED_DATE", getAttribute("MODIFIED_DATE").getString());
                    rsResultSet.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
                    rsResultSet.updateBoolean("APPROVED", false);
                    rsResultSet.updateString("APPROVED_DATE", "0000-00-00");
                    rsResultSet.updateBoolean("CANCELED", false);
                    rsResultSet.updateBoolean("REJECTED", false);
                    rsResultSet.updateString("REJECTED_DATE", "0000-00-00");
                    rsResultSet.updateBoolean("CHANGED", true);
                    rsResultSet.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

                    rsResultSet.insertRow();

                    rsHistory.moveToInsertRow();
                    rsHistory.updateInt("REVISION_NO", RevNo); //Revision No. 1 in case of Insert
                    rsHistory.updateInt("UPDATED_BY", (int) getAttribute("CREATED_BY").getVal());
                    rsHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
                    rsHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                    rsHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
                    rsHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                    rsHistory.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
                    rsHistory.updateString("FT_PIECE_NO", ObjMRItems.getAttribute("FT_PIECE_NO").getString());
                    rsHistory.updateString("MOUNTING_PLAN_DATE", ObjMRItems.getAttribute("MOUNTING_PLAN_DATE").getString());
                    rsHistory.updateString("MOUNTING_ACTUAL_DATE", ObjMRItems.getAttribute("MOUNTING_ACTUAL_DATE").getString());
                    rsHistory.updateDouble("EXPECTED_LIFE_DAYS", ObjMRItems.getAttribute("EXPECTED_LIFE_DAYS").getDouble());
                    rsHistory.updateString("DEMOUNTING_DATE", ObjMRItems.getAttribute("DEMOUNTING_DATE").getString());
                    rsHistory.updateDouble("ACTUAL_LIFE_DAYS", ObjMRItems.getAttribute("ACTUAL_LIFE_DAYS").getDouble());
                    rsHistory.updateString("PERFORMANCE_FEEDBACK", ObjMRItems.getAttribute("PERFORMANCE_FEEDBACK").getString());
                    rsHistory.updateString("REMARKS", ObjMRItems.getAttribute("REMARKS").getString());
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
                    rsHistory.updateString("FROM_IP", "" + str_split[1]);
                    rsHistory.insertRow();
                }

            } else {
                rsHistory.moveToInsertRow();
                rsHistory.updateInt("REVISION_NO", RevNo); //Revision No. 1 in case of Insert
                rsHistory.updateInt("UPDATED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                rsHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
                rsHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                rsHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
                rsHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                rsHistory.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
                rsHistory.updateString("FT_PIECE_NO", "");
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
                rsHistory.updateString("FROM_IP", "" + str_split[1]);
                rsHistory.insertRow();
            }

            //First remove the old rows
            //======== Update the Approval Flow =========
            setAttribute("FROM", EITLERPGLOBAL.gUserID);

            //ApprovalFlow ObjFlow=new ApprovalFlow();
            clsFeltProductionApprovalFlow ObjFlow = new clsFeltProductionApprovalFlow();

            ObjFlow.ModuleID = clsTrailPieceDispatchEntry.ModuleID;
            ObjFlow.DocNo = (String) getAttribute("DOC_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("DOC_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "PRODUCTION.FELT_TRAIL_PIECE_DISPATCH";
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
                data.Execute("UPDATE PRODUCTION.FELT_TRAIL_PIECE_DISPATCH SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='" + ObjFlow.DocNo + "' ");

                //Remove Old Records from FELT_DOC_DATA
                data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=" + clsTrailPieceDispatchEntry.ModuleID + " AND DOC_NO='" + ObjFlow.DocNo + "'");

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
                String sql = "UPDATE PRODUCTION.FELT_TRAIL_PIECE_DISPATCH D,PRODUCTION.FELT_TRAIL_PIECE_SELECTION S "
                        + "SET S.MOUNTING_PLAN_DATE=D.MOUNTING_PLAN_DATE,S.MOUNTING_ACTUAL_DATE=D.MOUNTING_ACTUAL_DATE,"
                        + "S.EXPECTED_LIFE_DAYS=D.EXPECTED_LIFE_DAYS,S.ACTUAL_LIFE_DAYS=D.ACTUAL_LIFE_DAYS,"
                        + "S.PERFORMANCE_FEEDBACK=D.PERFORMANCE_FEEDBACK,S.REMARKS=D.REMARKS "
                        + "WHERE D.FT_PIECE_NO=S.FT_PIECE_NO AND "
                        + "D.DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "'";
                data.Execute(sql);
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

                rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_TRAIL_PIECE_DISPATCH_H WHERE  DOC_NO='" + mDocNo + "' AND REVISION_NO=" + RevNo + " ");

            } else {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_TRAIL_PIECE_DISPATCH WHERE  DOC_NO='" + mDocNo + "' ");
            }
            rsTmp.first();

            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {
                Counter = Counter + 1;
                clsTrailPieceDispatchEntryItem ObjMRItems = new clsTrailPieceDispatchEntryItem();
                ObjMRItems.setAttribute("FT_PIECE_NO", rsTmp.getString("FT_PIECE_NO"));
                ObjMRItems.setAttribute("MOUNTING_PLAN_DATE", rsTmp.getString("MOUNTING_PLAN_DATE"));
                ObjMRItems.setAttribute("MOUNTING_ACTUAL_DATE", rsTmp.getString("MOUNTING_ACTUAL_DATE"));
                ObjMRItems.setAttribute("EXPECTED_LIFE_DAYS", rsTmp.getString("EXPECTED_LIFE_DAYS"));
                ObjMRItems.setAttribute("DEMOUNTING_DATE", rsTmp.getString("DEMOUNTING_DATE"));
                ObjMRItems.setAttribute("ACTUAL_LIFE_DAYS", rsTmp.getString("ACTUAL_LIFE_DAYS"));
                ObjMRItems.setAttribute("PERFORMANCE_FEEDBACK", rsTmp.getString("PERFORMANCE_FEEDBACK"));
                ObjMRItems.setAttribute("REMARKS", rsTmp.getString("REMARKS"));
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_TRAIL_PIECE_DISPATCH WHERE DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=801 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND STATUS='W'";
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
            String strSQL = "SELECT DOC_NO,DOC_DATE,REVISION_NO,UPDATED_BY,APPROVAL_STATUS,DATE_FORMAT(ENTRY_DATE,'%d/%m/%Y %H:%i:%S') AS ENTRY_DATE,REJECTED_REMARKS AS APPROVER_REMARKS,FROM_IP FROM PRODUCTION.FELT_TRAIL_PIECE_DISPATCH_H WHERE DOC_NO='" + DocNo + "' GROUP BY REVISION_NO,DOC_NO";
            rsTmp = data.getResult(strSQL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                while (!rsTmp.isAfterLast()) {
                    clsTrailPieceDispatchEntry objParty = new clsTrailPieceDispatchEntry();

                    objParty.setAttribute("DOC_NO", rsTmp.getString("DOC_NO"));
                    objParty.setAttribute("CREATED_DATE", rsTmp.getString("DOC_DATE"));
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
            strSQL += "SELECT * FROM PRODUCTION.FELT_TRAIL_PIECE_DISPATCH WHERE " + Condition + " GROUP BY DOC_NO";
            System.out.println("Filter Query :" + strSQL);
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery(strSQL);

            if (!rsResultSet.first()) {
                strSQL = "SELECT * FROM PRODUCTION.FELT_TRAIL_PIECE_DISPATCH GROUP BY DOC_NO ";
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_TRAIL_PIECE_DISPATCH WHERE DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=801 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND TYPE='C' AND STATUS='W'";
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
                strSQL = "SELECT DISTINCT PRODUCTION.FELT_TRAIL_PIECE_DISPATCH.DOC_NO,PRODUCTION.FELT_TRAIL_PIECE_DISPATCH.DOC_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_TRAIL_PIECE_DISPATCH,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_TRAIL_PIECE_DISPATCH.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=801 ORDER BY PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT PRODUCTION.FELT_TRAIL_PIECE_DISPATCH.DOC_NO,PRODUCTION.FELT_TRAIL_PIECE_DISPATCH.DOC_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_TRAIL_PIECE_DISPATCH,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_TRAIL_PIECE_DISPATCH.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=801 ORDER BY PRODUCTION.FELT_TRAIL_PIECE_DISPATCH.CREATED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT PRODUCTION.FELT_TRAIL_PIECE_DISPATCH.DOC_NO,PRODUCTION.FELT_TRAIL_PIECE_DISPATCH.DOC_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_TRAIL_PIECE_DISPATCH,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_TRAIL_PIECE_DISPATCH.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=801 ORDER BY PRODUCTION.FELT_TRAIL_PIECE_DISPATCH.DOC_NO";
            }

            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {

                Counter = Counter + 1;
                clsTrailPieceDispatchEntry ObjItem = new clsTrailPieceDispatchEntry();

                //------------- Header Fields --------------------//
                ObjItem.setAttribute("DOC_NO", rsTmp.getString("DOC_NO"));
                ObjItem.setAttribute("DOC_DATE", rsTmp.getString("DOC_DATE"));
                ObjItem.setAttribute("RECEIVED_DATE", rsTmp.getString("RECEIVED_DATE"));
                ObjItem.setAttribute("DEPT_ID", 0);
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
            rsResultSet = Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_TRAIL_PIECE_DISPATCH_H WHERE DOC_NO='" + pDocNo + "' ORDER BY REVISION_NO");
            Ready = true;
            HistoryView = true;
            MoveFirst();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public static boolean CanCancel(int pCompanyID, String pDocNo) {
        ResultSet rsTmp = null;
        boolean canCancel = false;

        try {
            rsTmp = data.getResult("SELECT DOC_NO FROM PRODUCTION.FELT_TRAIL_PIECE_DISPATCH WHERE  DOC_NO='" + pDocNo + "' AND CANCELED=0");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                canCancel = true;
            }

            rsTmp.close();
        } catch (Exception e) {

        }

        return canCancel;

    }

    public static boolean CancelDoc(int pCompanyID, String pDocNo) {

        ResultSet rsTmp = null, rsIndent = null;
        boolean Cancelled = false;

        try {
            if (CanCancel(pCompanyID, pDocNo)) {

                boolean ApprovedDoc = false;

                rsTmp = data.getResult("SELECT APPROVED FROM PRODUCTION.FELT_TRAIL_PIECE_DISPATCH WHERE DOC_NO='" + pDocNo + "' ");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    ApprovedDoc = rsTmp.getBoolean("APPROVED");
                }

                if (ApprovedDoc) {

                } else {
                    int ModuleID = 799;
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='" + pDocNo + "' AND MODULE_ID=" + (ModuleID));
                }

                //Now Update the header with cancel falg to true
                data.Execute("UPDATE PRODUCTION.FELT_TRAIL_PIECE_DISPATCH SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='" + pDocNo + "' ");

                Cancelled = true;
            }

            rsTmp.close();
            rsIndent.close();
        } catch (Exception e) {

        }

        return Cancelled;
    }
}
