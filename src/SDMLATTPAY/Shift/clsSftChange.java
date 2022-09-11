/*
 * clsSales_Party.java
 *
 * Created on April 3, 2009, 10:22 AM
 */
package SDMLATTPAY.Shift;

import EITLERP.EITLERPGLOBAL;
import EITLERP.Variant;
import EITLERP.clsFirstFree;
import EITLERP.data;
import SDMLATTPAY.ApprovalFlow;
import java.util.*;
import java.util.Date;
import java.text.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.DecimalFormat;
import java.lang.Double;

/**
 *
 * @author
 */
public class clsSftChange {

    public String LastError = "";
    private ResultSet rsResultSet, rsResultSet1;
    private Connection Conn;
    private Statement Stmt;
    DecimalFormat df = new DecimalFormat("##.##");
    private HashMap props;

    public boolean Ready = false;

    private boolean HistoryView = false;
    public static int ModuleID = 804; //72
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
    public clsSftChange() {
        LastError = "";
        props = new HashMap();
        //props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("REVISION_NO", new Variant(0));
        props.put("UPDATED_BY", new Variant(0));
        props.put("ENTRY_DATE", new Variant(""));

        props.put("SSCC_DOC_NO", new Variant(""));
        props.put("SSCC_DOC_DATE", new Variant(""));
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
            rsResultSet = Stmt.executeQuery("SELECT * FROM SDMLATTPAY.ATT_SHIFT_SCH_CHANGE GROUP BY SSCC_DOC_NO ");
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

            rsHistory = stHistory.executeQuery("SELECT * FROM SDMLATTPAY.ATT_SHIFT_SCH_CHANGE_H WHERE SSCC_DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail = stHDetail.executeQuery("SELECT * FROM SDMLATTPAY.ATT_SHIFT_SCH_CHANGE_H WHERE SSCC_DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//

            //--------- Generate Gatepass requisition no.  ------------
            setAttribute("SSCC_DOC_NO", clsFirstFree.getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 804, (int) getAttribute("FFNO").getVal(), true));
            //-------------------------------------------------

            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");

            Statement tmpStmt;

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery("SELECT * FROM SDMLATTPAY.ATT_SHIFT_SCH_CHANGE WHERE SSCC_DOC_NO='1'");

            //Now Insert records into detail table
            for (int i = 1; i <= colMRItems.size(); i++) {
                clsShiftSchChangeItem ObjMRItems = (clsShiftSchChangeItem) colMRItems.get(Integer.toString(i));

                rsResultSet.moveToInsertRow();
                rsResultSet.updateString("SSCC_DOC_NO", getAttribute("SSCC_DOC_NO").getString());
                rsResultSet.updateString("SSCC_DOC_DATE", getAttribute("SSCC_DOC_DATE").getString());
                rsResultSet.updateString("SSCC_EMPID", (String) ObjMRItems.getAttribute("SSCC_EMPID").getString());
                rsResultSet.updateString("SSCC_YEAR", (String) ObjMRItems.getAttribute("SSCC_YEAR").getString());
                rsResultSet.updateString("SSCC_MONTH", (String) ObjMRItems.getAttribute("SSCC_MONTH").getString());
                rsResultSet.updateString("SSCC_SHIFT_CHANGE_DATE", EITLERPGLOBAL.formatDateDB((String) ObjMRItems.getAttribute("SSCC_SHIFT_CHANGE_DATE").getString()));
                rsResultSet.updateString("SSCC_CURRENT_SHIFT", (String) ObjMRItems.getAttribute("SSCC_CURRENT_SHIFT").getString());
                rsResultSet.updateString("SSCC_CHANGE_SHIFT", (String) ObjMRItems.getAttribute("SSCC_CHANGE_SHIFT").getString());
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
                rsResultSet.updateString("FROM_IP", "" + str_split[1]);
                rsResultSet.insertRow();

                rsHistory.moveToInsertRow();
                rsHistory.updateInt("REVISION_NO", 1); //Revision No. 1 in case of Insert
                rsHistory.updateInt("UPDATED_BY", (int) getAttribute("CREATED_BY").getVal());
                rsHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
                rsHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                rsHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
                rsHistory.updateString("SSCC_DOC_NO", getAttribute("SSCC_DOC_NO").getString());
                rsHistory.updateString("SSCC_DOC_DATE", getAttribute("SSCC_DOC_DATE").getString());
                rsHistory.updateString("SSCC_EMPID", (String) ObjMRItems.getAttribute("SSCC_EMPID").getString());
                rsHistory.updateString("SSCC_YEAR", (String) ObjMRItems.getAttribute("SSCC_YEAR").getString());
                rsHistory.updateString("SSCC_MONTH", (String) ObjMRItems.getAttribute("SSCC_MONTH").getString());
                rsHistory.updateString("SSCC_SHIFT_CHANGE_DATE", EITLERPGLOBAL.formatDateDB((String) ObjMRItems.getAttribute("SSCC_SHIFT_CHANGE_DATE").getString()));
                rsHistory.updateString("SSCC_CURRENT_SHIFT", (String) ObjMRItems.getAttribute("SSCC_CURRENT_SHIFT").getString());
                rsHistory.updateString("SSCC_CHANGE_SHIFT", (String) ObjMRItems.getAttribute("SSCC_CHANGE_SHIFT").getString());
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
            ApprovalFlow ObjFlow = new ApprovalFlow();

            ObjFlow.ModuleID = clsSftChange.ModuleID;
            ObjFlow.CompanyID = EITLERPGLOBAL.gCompanyID;
            ObjFlow.DocNo = (String) getAttribute("SSCC_DOC_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("SSCC_DOC_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "SDMLATTPAY.ATT_SHIFT_SCH_CHANGE";
            ObjFlow.IsCreator = true;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "SSCC_DOC_NO";

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
            if (ObjFlow.Status.equals("F")) {
//                PreparedStatement pstm = null;
//                Conn.setAutoCommit(false);
//                String sql = "UPDATE SDMLATTPAY.ATT_SHIFT_SCHEDULE SET ?=? WHERE SSC_EMPID=? AND SSC_YEAR=? AND SSC_MONTH=?";
//                pstm = Conn.prepareStatement(sql);
//                for (int i = 1; i <= colMRItems.size(); i++) {
//                    clsShiftSchChangeItem ObjMRItems = (clsShiftSchChangeItem) colMRItems.get(Integer.toString(i));
//                    pstm.setString(1, "SDMLATTPAY.ATT_SHIFT_SCHEDULE." + ObjMRItems.getAttribute("SSCC_SHIFT_CHANGE_DATE").getString().substring(0, 2));
//                    pstm.setString(2, (String) ObjMRItems.getAttribute("SSCC_CHANGE_SHIFT").getString());
//                    pstm.setString(3, (String) ObjMRItems.getAttribute("SSCC_EMPID").getString());
//                    pstm.setString(4, (String) ObjMRItems.getAttribute("SSCC_YEAR").getString());
//                    pstm.setString(5, (String) ObjMRItems.getAttribute("SSCC_MONTH").getString());
//                    pstm.addBatch();
//                }
//                pstm.executeBatch();
//                Conn.commit();
//                Conn.setAutoCommit(true);
                String sql;
                for (int i = 1; i <= colMRItems.size(); i++) {
                    clsShiftSchChangeItem ObjMRItems1 = (clsShiftSchChangeItem) colMRItems.get(Integer.toString(i));
                    sql = "UPDATE SDMLATTPAY.ATT_SHIFT_SCHEDULE SET "
                            + "SDMLATTPAY.ATT_SHIFT_SCHEDULE.SSC_" + Integer.parseInt(ObjMRItems1.getAttribute("SSCC_SHIFT_CHANGE_DATE").getString().substring(0, 2))
                            + "=" + (String) ObjMRItems1.getAttribute("SSCC_CHANGE_SHIFT").getString()
                            + " WHERE SSC_EMPID=" + (String) ObjMRItems1.getAttribute("SSCC_EMPID").getString() + " "
                            + "AND  SSC_YEAR=" + (String) ObjMRItems1.getAttribute("SSCC_YEAR").getString() + " "
                            + "AND  SSC_MONTH=" + (String) ObjMRItems1.getAttribute("SSCC_MONTH").getString();
                    System.out.println("Update Shift :" + sql);
                    data.Execute(sql);
                }
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

            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");

            // ---- History Related Changes ------ //
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHDetail = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rsHistory = stHistory.executeQuery("SELECT * FROM SDMLATTPAY.ATT_SHIFT_SCH_CHANGE_H WHERE SSCC_DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail = stHDetail.executeQuery("SELECT * FROM SDMLATTPAY.ATT_SHIFT_SCH_CHANGE_H WHERE SSCC_DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//

            String theDocNo = getAttribute("SSCC_DOC_NO").getString();
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM SDMLATTPAY.ATT_SHIFT_SCH_CHANGE_H WHERE SSCC_DOC_NO='" + (String) getAttribute("SSCC_DOC_NO").getObj() + "'");
            RevNo++;
            String RevDocNo = (String) getAttribute("SSCC_DOC_NO").getObj();
            if (ApprovalFlow.IsCreator(ModuleID, (String) getAttribute("SSCC_DOC_NO").getObj())) {

                String mDocNo = (String) getAttribute("SSCC_DOC_NO").getObj();

                data.Execute("DELETE FROM SDMLATTPAY.ATT_SHIFT_SCH_CHANGE WHERE SSCC_DOC_NO='" + mDocNo + "'");

                Statement tmpStmt;

                tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet rsTmp1 = tmpStmt.executeQuery("SELECT * FROM SDMLATTPAY.ATT_SHIFT_SCH_CHANGE WHERE SSCC_DOC_NO='1'");

                //Now Insert records into detail table
                for (int i = 1; i <= colMRItems.size(); i++) {
                    clsShiftSchChangeItem ObjMRItems = (clsShiftSchChangeItem) colMRItems.get(Integer.toString(i));
                    rsResultSet.moveToInsertRow();
                    rsResultSet.updateString("SSCC_DOC_NO", getAttribute("SSCC_DOC_NO").getString());
                    rsResultSet.updateString("SSCC_DOC_DATE", getAttribute("SSCC_DOC_DATE").getString());
                    rsResultSet.updateString("SSCC_EMPID", (String) ObjMRItems.getAttribute("SSCC_EMPID").getString());
                    rsResultSet.updateString("SSCC_YEAR", (String) ObjMRItems.getAttribute("SSCC_YEAR").getString());
                    rsResultSet.updateString("SSCC_MONTH", (String) ObjMRItems.getAttribute("SSCC_MONTH").getString());
                    rsResultSet.updateString("SSCC_SHIFT_CHANGE_DATE", EITLERPGLOBAL.formatDateDB((String) ObjMRItems.getAttribute("SSCC_SHIFT_CHANGE_DATE").getString()));
                    rsResultSet.updateString("SSCC_CURRENT_SHIFT", (String) ObjMRItems.getAttribute("SSCC_CURRENT_SHIFT").getString());
                    rsResultSet.updateString("SSCC_CHANGE_SHIFT", (String) ObjMRItems.getAttribute("SSCC_CHANGE_SHIFT").getString());
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
                    rsResultSet.updateString("FROM_IP", "" + str_split[1]);
                    rsResultSet.insertRow();

                    rsHistory.moveToInsertRow();
                    rsHistory.updateInt("REVISION_NO", RevNo);
                    rsHistory.updateInt("UPDATED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                    rsHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
                    rsHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                    rsHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
                    rsHistory.updateString("SSCC_DOC_NO", getAttribute("SSCC_DOC_NO").getString());
                    rsHistory.updateString("SSCC_DOC_DATE", getAttribute("SSCC_DOC_DATE").getString());
                    rsHistory.updateString("SSCC_EMPID", (String) ObjMRItems.getAttribute("SSCC_EMPID").getString());
                    rsHistory.updateString("SSCC_YEAR", (String) ObjMRItems.getAttribute("SSCC_YEAR").getString());
                    rsHistory.updateString("SSCC_MONTH", (String) ObjMRItems.getAttribute("SSCC_MONTH").getString());
                    rsHistory.updateString("SSCC_SHIFT_CHANGE_DATE", EITLERPGLOBAL.formatDateDB((String) ObjMRItems.getAttribute("SSCC_SHIFT_CHANGE_DATE").getString()));
                    rsHistory.updateString("SSCC_CURRENT_SHIFT", (String) ObjMRItems.getAttribute("SSCC_CURRENT_SHIFT").getString());
                    rsHistory.updateString("SSCC_CHANGE_SHIFT", (String) ObjMRItems.getAttribute("SSCC_CHANGE_SHIFT").getString());
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
                //========= Inserting Into History =================//
                //Get the Maximum Revision No.
                //========= Inserting Into History =================//
                //Get the Maximum Revision No.
                clsShiftSchChangeItem ObjMRItems = (clsShiftSchChangeItem) colMRItems.get(Integer.toString(1));
                rsHistory.moveToInsertRow();
                rsHistory.updateInt("REVISION_NO", RevNo); //Revision No. 1 in case of Insert
                rsHistory.updateInt("UPDATED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                rsHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
                rsHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                rsHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
                rsHistory.updateString("SSCC_DOC_NO", getAttribute("SSCC_DOC_NO").getString());
                rsHistory.updateString("SSCC_DOC_DATE", getAttribute("SSCC_DOC_DATE").getString());
                rsHistory.updateString("SSCC_EMPID", (String) ObjMRItems.getAttribute("SSCC_EMPID").getString());
                rsHistory.updateString("SSCC_YEAR", (String) ObjMRItems.getAttribute("SSCC_YEAR").getString());
                rsHistory.updateString("SSCC_MONTH", (String) ObjMRItems.getAttribute("SSCC_MONTH").getString());
                rsHistory.updateString("SSCC_SHIFT_CHANGE_DATE", EITLERPGLOBAL.formatDateDB((String) ObjMRItems.getAttribute("SSCC_SHIFT_CHANGE_DATE").getString()));
                rsHistory.updateString("SSCC_CURRENT_SHIFT", (String) ObjMRItems.getAttribute("SSCC_CURRENT_SHIFT").getString());
                rsHistory.updateString("SSCC_CHANGE_SHIFT", (String) ObjMRItems.getAttribute("SSCC_CHANGE_SHIFT").getString());
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
            ApprovalFlow ObjFlow = new ApprovalFlow();

            ObjFlow.ModuleID = clsSftChange.ModuleID;
            ObjFlow.CompanyID = EITLERPGLOBAL.gCompanyID;
            ObjFlow.DocNo = (String) getAttribute("SSCC_DOC_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("SSCC_DOC_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "SDMLATTPAY.ATT_SHIFT_SCH_CHANGE";
            ObjFlow.IsCreator = false;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "SSCC_DOC_NO";

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
                data.Execute("UPDATE SDMLATTPAY.ATT_SHIFT_SCH_CHANGE SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='" + ObjFlow.DocNo + "' ");

                //Remove Old Records from FELT_DOC_DATA
                data.Execute("DELETE FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=" + clsSftChange.ModuleID + " AND DOC_NO='" + ObjFlow.DocNo + "'");

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
                String sql;
                for (int i = 1; i <= colMRItems.size(); i++) {
                    clsShiftSchChangeItem ObjMRItems1 = (clsShiftSchChangeItem) colMRItems.get(Integer.toString(i));
                    sql = "UPDATE SDMLATTPAY.ATT_SHIFT_SCHEDULE SET "
                            + "SDMLATTPAY.ATT_SHIFT_SCHEDULE.SSC_" + Integer.parseInt(ObjMRItems1.getAttribute("SSCC_SHIFT_CHANGE_DATE").getString().substring(0, 2))
                            + "=" + (String) ObjMRItems1.getAttribute("SSCC_CHANGE_SHIFT").getString()
                            + " WHERE SSC_EMPID='" + (String) ObjMRItems1.getAttribute("SSCC_EMPID").getString() + "' "
                            + "AND  SSC_YEAR=" + (String) ObjMRItems1.getAttribute("SSCC_YEAR").getString() + " "
                            + "AND  SSC_MONTH=" + (String) ObjMRItems1.getAttribute("SSCC_MONTH").getString();
                    System.out.println("Update Shift :" + sql);
                    data.Execute(sql);
                }
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

            setAttribute("SSCC_DOC_NO", rsResultSet.getString("SSCC_DOC_NO"));
            setAttribute("SSCC_DOC_DATE", rsResultSet.getString("SSCC_DOC_DATE"));
            setAttribute("HIERARCHY_ID", rsResultSet.getDouble("HIERARCHY_ID"));

            colMRItems.clear();

            String mDocNo = (String) getAttribute("SSCC_DOC_NO").getObj();

            tmpStmt = tmpConn.createStatement();
            if (HistoryView) {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM SDMLATTPAY.ATT_SHIFT_SCH_CHANGE_H WHERE SSCC_DOC_NO='" + mDocNo + "' AND REVISION_NO=" + RevNo + " ");
            } else {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM SDMLATTPAY.ATT_SHIFT_SCH_CHANGE WHERE SSCC_DOC_NO='" + mDocNo + "' ");
            }
            rsTmp.first();

            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {
                Counter = Counter + 1;
                clsShiftSchChangeItem ObjMRItems = new clsShiftSchChangeItem();
                ObjMRItems.setAttribute("SSCC_DOC_NO", rsTmp.getString("SSCC_DOC_NO"));
                ObjMRItems.setAttribute("SSCC_DOC_DATE", rsTmp.getString("SSCC_DOC_DATE"));
                ObjMRItems.setAttribute("SSCC_EMPID", rsTmp.getString("SSCC_EMPID"));
                ObjMRItems.setAttribute("SSCC_YEAR", rsTmp.getString("SSCC_YEAR"));
                ObjMRItems.setAttribute("SSCC_MONTH", rsTmp.getString("SSCC_MONTH"));
                ObjMRItems.setAttribute("SSCC_SHIFT_CHANGE_DATE", rsTmp.getString("SSCC_SHIFT_CHANGE_DATE"));
                ObjMRItems.setAttribute("SSCC_CURRENT_SHIFT", rsTmp.getString("SSCC_CURRENT_SHIFT"));
                ObjMRItems.setAttribute("SSCC_CHANGE_SHIFT", rsTmp.getString("SSCC_CHANGE_SHIFT"));
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.ATT_SHIFT_SCH_CHANGE WHERE SSCC_DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=804 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND STATUS='W'";
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
            String strSQL = "SELECT SSCC_DOC_NO,SSCC_DOC_DATE,REVISION_NO,UPDATED_BY,APPROVAL_STATUS,DATE_FORMAT(ENTRY_DATE,'%d/%m/%Y %H:%i:%S') AS ENTRY_DATE,REJECTED_REMARKS AS APPROVER_REMARKS,FROM_IP FROM SDMLATTPAY.ATT_SHIFT_SCH_CHANGE_H WHERE SSCC_DOC_NO='" + DocNo + "'";
            rsTmp = data.getResult(strSQL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                while (!rsTmp.isAfterLast()) {
                    clsSftChange objParty = new clsSftChange();

                    objParty.setAttribute("SSCC_DOC_NO", rsTmp.getString("SSCC_DOC_NO"));
                    objParty.setAttribute("SSCC_DOC_DATE", rsTmp.getString("SSCC_DOC_DATE"));
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
            strSQL += "SELECT * FROM SDMLATTPAY.ATT_SHIFT_SCH_CHANGE WHERE " + Condition + " GROUP BY SSCC_DOC_NO";
            System.out.println("Filter Query :" + strSQL);
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery(strSQL);

            if (!rsResultSet.first()) {
                strSQL = "SELECT * FROM SDMLATTPAY.ATT_SHIFT_SCH_CHANGE GROUP BY SSCC_DOC_NO";
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.ATT_SHIFT_SCH_CHANGE WHERE SSCC_DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=804 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND TYPE='C' AND STATUS='W'";
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
                strSQL = "SELECT SDMLATTPAY.ATT_SHIFT_SCH_CHANGE.SSCC_DOC_NO,SDMLATTPAY.ATT_SHIFT_SCH_CHANGE.SSCC_DOC_DATE,RECEIVED_DATE FROM SDMLATTPAY.ATT_SHIFT_SCH_CHANGE,SDMLATTPAY.D_COM_DOC_DATA WHERE SDMLATTPAY.D_COM_DOC_DATA.DOC_NO=SDMLATTPAY.ATT_SHIFT_SCH_CHANGE.SSCC_DOC_NO AND SDMLATTPAY.D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND SDMLATTPAY.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=804 ORDER BY SDMLATTPAY.D_COM_DOC_DATA.RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT SDMLATTPAY.ATT_SHIFT_SCH_CHANGE.SSCC_DOC_NO,SDMLATTPAY.ATT_SHIFT_SCH_CHANGE.SSCC_DOC_DATE,RECEIVED_DATE FROM SDMLATTPAY.ATT_SHIFT_SCH_CHANGE,SDMLATTPAY.D_COM_DOC_DATA WHERE SDMLATTPAY.D_COM_DOC_DATA.DOC_NO=SDMLATTPAY.ATT_SHIFT_SCH_CHANGE.SSCC_DOC_NO AND SDMLATTPAY.D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND SDMLATTPAY.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=804 ORDER BY SDMLATTPAY.ATT_SHIFT_SCH_CHANGE.SSCC_DOC_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT SDMLATTPAY.ATT_SHIFT_SCH_CHANGE.SSCC_DOC_NO,SDMLATTPAY.ATT_SHIFT_SCH_CHANGE.SSCC_DOC_DATE,RECEIVED_DATE FROM SDMLATTPAY.ATT_SHIFT_SCH_CHANGE,SDMLATTPAY.D_COM_DOC_DATA WHERE SDMLATTPAY.D_COM_DOC_DATA.DOC_NO=SDMLATTPAY.ATT_SHIFT_SCH_CHANGE.SSCC_DOC_NO AND SDMLATTPAY.D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND SDMLATTPAY.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=804 ORDER BY SDMLATTPAY.ATT_SHIFT_SCH_CHANGE.SSCC_DOC_NO";
            }

            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {

                Counter = Counter + 1;
                clsSftChange ObjItem = new clsSftChange();

                //------------- Header Fields --------------------//
                ObjItem.setAttribute("DOC_NO", rsTmp.getString("SSCC_DOC_NO"));
                ObjItem.setAttribute("DOC_DATE", rsTmp.getString("SSCC_DOC_DATE"));
                ObjItem.setAttribute("RECEIVED_DATE", rsTmp.getString("RECEIVED_DATE"));
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
            rsResultSet = Stmt.executeQuery("SELECT * FROM SDMLATTPAY.ATT_SHIFT_SCH_CHANGE_H WHERE DOC_NO='" + pDocNo + "' GROUP BY SSCC_DOC_NO ORDER BY REVISION_NO ");
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
