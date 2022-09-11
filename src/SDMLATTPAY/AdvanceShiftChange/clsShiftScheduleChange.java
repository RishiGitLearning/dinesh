/*
 * clsSales_Party.java
 *
 * Created on April 3, 2009, 10:22 AM
 */
package SDMLATTPAY.AdvanceShiftChange;

import EITLERP.EITLERPGLOBAL;
import EITLERP.Variant;
import EITLERP.data;
import SDMLATTPAY.ApprovalFlow;
import java.util.*;
import java.sql.*;
import java.text.DecimalFormat;

/**
 *
 * @author
 */
public class clsShiftScheduleChange {

    public String LastError = "";
    private ResultSet rsResultSet, rsResultSet1;
    private Connection Conn;
    private Statement Stmt;
    DecimalFormat df = new DecimalFormat("##.##");
    private HashMap props;

    public boolean Ready = false;

    private boolean HistoryView = false;
    public static int ModuleID = 866; //72
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
    public clsShiftScheduleChange() {
        LastError = "";
        props = new HashMap();
        //props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("REVISION_NO", new Variant(0));
        props.put("UPDATED_BY", new Variant(0));
        props.put("ENTRY_DATE", new Variant(""));

        props.put("SS_DOC_NO", new Variant(""));
        props.put("SS_DOC_DATE", new Variant(""));
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
            rsResultSet = Stmt.executeQuery("SELECT * FROM SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE   GROUP BY SS_DOC_NO");
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

            rsHistory = stHistory.executeQuery("SELECT * FROM SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE_H WHERE SS_DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail = stHDetail.executeQuery("SELECT * FROM SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE_H WHERE SS_DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//

            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");

            //Now Insert records into detail table
            for (int i = 1; i <= colMRItems.size(); i++) {
                clsShiftScheduleItem ObjMRItems = (clsShiftScheduleItem) colMRItems.get(Integer.toString(i));

                rsResultSet.moveToInsertRow();
                rsResultSet.updateString("SS_DOC_NO", getAttribute("SS_DOC_NO").getString());
                rsResultSet.updateString("SS_DOC_DATE", getAttribute("SS_DOC_DATE").getString());
                rsResultSet.updateString("SS_EMPID", (String) ObjMRItems.getAttribute("SS_EMPID").getString());
                rsResultSet.updateString("SS_SHIFT_FROM_DATE", EITLERPGLOBAL.formatDateDB((String) ObjMRItems.getAttribute("SS_SHIFT_FROM_DATE").getString()));
                rsResultSet.updateString("SS_SHIFT_EFFECT_FROM_DATE", getAttribute("SS_SHIFT_EFFECT_FROM_DATE").getString());
                rsResultSet.updateString("SS_SHIFT_TO_DATE", EITLERPGLOBAL.formatDateDB((String) ObjMRItems.getAttribute("SS_SHIFT_TO_DATE").getString()));
                rsResultSet.updateString("SS_CHANGE_SHIFT", (String) ObjMRItems.getAttribute("SS_CHANGE_SHIFT").getString());
                rsResultSet.updateString("SS_WEEK_OFF", (String) ObjMRItems.getAttribute("SS_WEEK_OFF").getString());
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
                rsHistory.updateString("SS_DOC_NO", getAttribute("SS_DOC_NO").getString());
                rsHistory.updateString("SS_DOC_DATE", getAttribute("SS_DOC_DATE").getString());
                rsHistory.updateString("SS_EMPID", (String) ObjMRItems.getAttribute("SS_EMPID").getString());
                rsHistory.updateString("SS_SHIFT_FROM_DATE", EITLERPGLOBAL.formatDateDB((String) ObjMRItems.getAttribute("SS_SHIFT_FROM_DATE").getString()));
                rsHistory.updateString("SS_SHIFT_EFFECT_FROM_DATE", getAttribute("SS_SHIFT_EFFECT_FROM_DATE").getString());
                rsHistory.updateString("SS_SHIFT_TO_DATE", EITLERPGLOBAL.formatDateDB((String) ObjMRItems.getAttribute("SS_SHIFT_TO_DATE").getString()));
                rsHistory.updateString("SS_CHANGE_SHIFT", (String) ObjMRItems.getAttribute("SS_CHANGE_SHIFT").getString());
                rsHistory.updateString("SS_WEEK_OFF", (String) ObjMRItems.getAttribute("SS_WEEK_OFF").getString());
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

            ObjFlow.ModuleID = clsShiftScheduleChange.ModuleID;
            ObjFlow.CompanyID = EITLERPGLOBAL.gCompanyID;
            ObjFlow.DocNo = (String) getAttribute("SS_DOC_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("SS_DOC_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE";
            ObjFlow.IsCreator = true;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "SS_DOC_NO";

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
                UpdateShift();
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

            rsHistory = stHistory.executeQuery("SELECT * FROM SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE_H WHERE SS_DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail = stHDetail.executeQuery("SELECT * FROM SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE_H WHERE SS_DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//

            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE_H WHERE SS_DOC_NO='" + (String) getAttribute("SS_DOC_NO").getObj() + "'");
            RevNo++;

            if (ApprovalFlow.IsCreator(ModuleID, (String) getAttribute("SS_DOC_NO").getObj())) {

                String mDocNo = (String) getAttribute("SS_DOC_NO").getObj();

                data.Execute("DELETE FROM SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE WHERE SS_DOC_NO='" + mDocNo + "'");

                //Now Insert records into detail table
                for (int i = 1; i <= colMRItems.size(); i++) {
                    clsShiftScheduleItem ObjMRItems = (clsShiftScheduleItem) colMRItems.get(Integer.toString(i));
                    rsResultSet.moveToInsertRow();
                    rsResultSet.updateString("SS_DOC_NO", getAttribute("SS_DOC_NO").getString());
                    rsResultSet.updateString("SS_DOC_DATE", getAttribute("SS_DOC_DATE").getString());
                    rsResultSet.updateString("SS_EMPID", (String) ObjMRItems.getAttribute("SS_EMPID").getString());
                    rsResultSet.updateString("SS_SHIFT_FROM_DATE", EITLERPGLOBAL.formatDateDB((String) ObjMRItems.getAttribute("SS_SHIFT_FROM_DATE").getString()));
                    rsResultSet.updateString("SS_SHIFT_EFFECT_FROM_DATE", getAttribute("SS_SHIFT_EFFECT_FROM_DATE").getString());
                    rsResultSet.updateString("SS_SHIFT_TO_DATE", EITLERPGLOBAL.formatDateDB((String) ObjMRItems.getAttribute("SS_SHIFT_TO_DATE").getString()));
                    rsResultSet.updateString("SS_CHANGE_SHIFT", (String) ObjMRItems.getAttribute("SS_CHANGE_SHIFT").getString());
                    rsResultSet.updateString("SS_WEEK_OFF", (String) ObjMRItems.getAttribute("SS_WEEK_OFF").getString());
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
                    rsResultSet.updateString("FROM_IP", "" + str_split[1]);
                    rsResultSet.insertRow();

                    rsHistory.moveToInsertRow();
                    rsHistory.updateInt("REVISION_NO", RevNo);
                    rsHistory.updateInt("UPDATED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                    rsHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
                    rsHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                    rsHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
                    rsHistory.updateString("SS_DOC_NO", getAttribute("SS_DOC_NO").getString());
                    rsHistory.updateString("SS_DOC_DATE", getAttribute("SS_DOC_DATE").getString());
                    rsHistory.updateString("SS_EMPID", (String) ObjMRItems.getAttribute("SS_EMPID").getString());
                    rsHistory.updateString("SS_SHIFT_FROM_DATE", EITLERPGLOBAL.formatDateDB((String) ObjMRItems.getAttribute("SS_SHIFT_FROM_DATE").getString()));
                    rsHistory.updateString("SS_SHIFT_EFFECT_FROM_DATE", getAttribute("SS_SHIFT_EFFECT_FROM_DATE").getString());
                    rsHistory.updateString("SS_SHIFT_TO_DATE", EITLERPGLOBAL.formatDateDB((String) ObjMRItems.getAttribute("SS_SHIFT_TO_DATE").getString()));
                    rsHistory.updateString("SS_CHANGE_SHIFT", (String) ObjMRItems.getAttribute("SS_CHANGE_SHIFT").getString());
                    rsHistory.updateString("SS_WEEK_OFF", (String) ObjMRItems.getAttribute("SS_WEEK_OFF").getString());
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
                clsShiftScheduleItem ObjMRItems = (clsShiftScheduleItem) colMRItems.get(Integer.toString(1));
                rsHistory.moveToInsertRow();
                rsHistory.updateInt("REVISION_NO", RevNo);
                rsHistory.updateInt("UPDATED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                rsHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
                rsHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                rsHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
                rsHistory.updateString("SS_DOC_NO", getAttribute("SS_DOC_NO").getString());
                rsHistory.updateString("SS_DOC_DATE", getAttribute("SS_DOC_DATE").getString());
                rsHistory.updateString("SS_EMPID", (String) ObjMRItems.getAttribute("SS_EMPID").getString());
                rsHistory.updateString("SS_SHIFT_FROM_DATE", EITLERPGLOBAL.formatDateDB((String) ObjMRItems.getAttribute("SS_SHIFT_FROM_DATE").getString()));
                rsHistory.updateString("SS_SHIFT_EFFECT_FROM_DATE", getAttribute("SS_SHIFT_EFFECT_FROM_DATE").getString());
                rsHistory.updateString("SS_SHIFT_TO_DATE", EITLERPGLOBAL.formatDateDB((String) ObjMRItems.getAttribute("SS_SHIFT_TO_DATE").getString()));
                rsHistory.updateString("SS_CHANGE_SHIFT", (String) ObjMRItems.getAttribute("SS_CHANGE_SHIFT").getString());
                rsHistory.updateString("SS_WEEK_OFF", (String) ObjMRItems.getAttribute("SS_WEEK_OFF").getString());
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

            ObjFlow.ModuleID = clsShiftScheduleChange.ModuleID;
            ObjFlow.CompanyID = EITLERPGLOBAL.gCompanyID;
            ObjFlow.DocNo = (String) getAttribute("SS_DOC_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("SS_DOC_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE";
            ObjFlow.IsCreator = false;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "SS_DOC_NO";

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
                data.Execute("UPDATE SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE SS_DOC_NO='" + ObjFlow.DocNo + "' ");

                //Remove Old Records from FELT_DOC_DATA
                data.Execute("DELETE FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=" + clsShiftScheduleChange.ModuleID + " AND DOC_NO='" + ObjFlow.DocNo + "'");

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
                UpdateShift();
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

            setAttribute("SS_DOC_NO", rsResultSet.getString("SS_DOC_NO"));
            setAttribute("SS_DOC_DATE", rsResultSet.getString("SS_DOC_DATE"));

            setAttribute("CREATED_BY", rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE", rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY", rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE", rsResultSet.getString("MODIFIED_DATE"));
            setAttribute("APPROVED", rsResultSet.getString("APPROVED"));
            setAttribute("APPROVED_DATE", rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED", rsResultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE", rsResultSet.getString("REJECTED_DATE"));
            setAttribute("CANCELED", rsResultSet.getString("CANCELED"));
            setAttribute("HIERARCHY_ID", rsResultSet.getDouble("HIERARCHY_ID"));

            setAttribute("SS_SHIFT_FROM_DATE", rsResultSet.getString("SS_SHIFT_FROM_DATE"));
            setAttribute("SS_SHIFT_TO_DATE", rsResultSet.getString("SS_SHIFT_TO_DATE"));
            colMRItems.clear();

            String mDocNo = (String) getAttribute("SS_DOC_NO").getObj();

            tmpStmt = tmpConn.createStatement();
            if (HistoryView) {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE_H "
                        + "LEFT JOIN (SELECT PAY_EMP_NO,EMP_NAME,EMP_DESIGNATION FROM SDMLATTPAY.ATTPAY_EMPMST) AS M ON SS_EMPID=PAY_EMP_NO  "
                        + "LEFT JOIN (SELECT DSGID,NAME,DESIGNATION_PRIORITY FROM SDMLATTPAY.ATT_DESIGNATION_MASTER) AS D ON DSGID=EMP_DESIGNATION   "
                        + "WHERE SS_DOC_NO='" + mDocNo + "' AND REVISION_NO=" + RevNo + " ORDER BY REVISION_NO,DESIGNATION_PRIORITY,NAME,PAY_EMP_NO ");
            } else {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE S "
                        + "LEFT JOIN (SELECT PAY_EMP_NO,EMP_NAME,EMP_DESIGNATION FROM SDMLATTPAY.ATTPAY_EMPMST) AS M ON SS_EMPID=PAY_EMP_NO  "
                        + "LEFT JOIN (SELECT DSGID,NAME,DESIGNATION_PRIORITY FROM SDMLATTPAY.ATT_DESIGNATION_MASTER) AS D ON DSGID=EMP_DESIGNATION  "
                        + "WHERE SS_DOC_NO='" + mDocNo + "' ORDER BY DESIGNATION_PRIORITY,NAME,PAY_EMP_NO ");
            }
            rsTmp.first();

            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {
                Counter = Counter + 1;
                clsShiftScheduleItem ObjMRItems = new clsShiftScheduleItem();
                ObjMRItems.setAttribute("SS_DOC_NO", rsTmp.getString("SS_DOC_NO"));
                ObjMRItems.setAttribute("SS_DOC_DATE", rsTmp.getString("SS_DOC_DATE"));
                ObjMRItems.setAttribute("SS_EMPID", rsTmp.getString("SS_EMPID"));
                ObjMRItems.setAttribute("SS_EMPNAME", rsTmp.getString("EMP_NAME"));
                ObjMRItems.setAttribute("SS_DESIGNATION", rsTmp.getString("NAME"));
                ObjMRItems.setAttribute("SS_SHIFT_FROM_DATE", rsTmp.getString("SS_SHIFT_FROM_DATE"));
                ObjMRItems.setAttribute("SS_SHIFT_EFFECT_FROM_DATE", rsTmp.getString("SS_SHIFT_EFFECT_FROM_DATE"));
                ObjMRItems.setAttribute("SS_SHIFT_TO_DATE", rsTmp.getString("SS_SHIFT_TO_DATE"));
                ObjMRItems.setAttribute("SS_CHANGE_SHIFT", rsTmp.getString("SS_CHANGE_SHIFT"));
                ObjMRItems.setAttribute("SS_WEEK_OFF", rsTmp.getString("SS_WEEK_OFF"));
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE WHERE SS_DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=866 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND STATUS='W'";
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
            String strSQL = "SELECT SS_DOC_NO,SS_DOC_DATE,REVISION_NO,UPDATED_BY,APPROVAL_STATUS,DATE_FORMAT(ENTRY_DATE,'%d/%m/%Y %H:%i:%S') AS ENTRY_DATE,REJECTED_REMARKS AS APPROVER_REMARKS,FROM_IP FROM SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE_H WHERE SS_DOC_NO='" + DocNo + "' GROUP BY SS_DOC_NO,REVISION_NO";
            rsTmp = data.getResult(strSQL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                while (!rsTmp.isAfterLast()) {
                    clsShiftScheduleChange objParty = new clsShiftScheduleChange();

                    objParty.setAttribute("SS_DOC_NO", rsTmp.getString("SS_DOC_NO"));
                    objParty.setAttribute("SS_DOC_DATE", rsTmp.getString("SS_DOC_DATE"));
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
            strSQL += "SELECT * FROM SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE WHERE " + Condition + "  GROUP BY SS_DOC_NO";
            System.out.println("Filter Query :" + strSQL);
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery(strSQL);

            if (!rsResultSet.first()) {
                strSQL = "SELECT * FROM SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE WHERE  GROUP BY SS_DOC_NO";
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE WHERE SS_DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=866 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND TYPE='C' AND STATUS='W'";
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
                strSQL = "SELECT SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE.SS_DOC_NO,SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE.SS_DOC_DATE,RECEIVED_DATE FROM SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE,SDMLATTPAY.D_COM_DOC_DATA WHERE SDMLATTPAY.D_COM_DOC_DATA.DOC_NO=SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE.SS_DOC_NO AND SDMLATTPAY.D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND SDMLATTPAY.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=866 GROUP BY SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE.SS_DOC_NO ORDER BY SDMLATTPAY.D_COM_DOC_DATA.RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE.SS_DOC_NO,SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE.SS_DOC_DATE,RECEIVED_DATE FROM SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE,SDMLATTPAY.D_COM_DOC_DATA WHERE SDMLATTPAY.D_COM_DOC_DATA.DOC_NO=SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE.SS_DOC_NO AND SDMLATTPAY.D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND SDMLATTPAY.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=866 GROUP BY SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE.SS_DOC_NO  ORDER BY SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE.SS_DOC_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE.SS_DOC_NO,SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE.SS_DOC_DATE,RECEIVED_DATE FROM SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE,SDMLATTPAY.D_COM_DOC_DATA WHERE SDMLATTPAY.D_COM_DOC_DATA.DOC_NO=SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE.SS_DOC_NO AND SDMLATTPAY.D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND SDMLATTPAY.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=866 GROUP BY SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE.SS_DOC_NO  ORDER BY SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE.SS_DOC_NO";
            }

            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {

                Counter = Counter + 1;
                clsShiftScheduleChange ObjItem = new clsShiftScheduleChange();

                //------------- Header Fields --------------------//
                ObjItem.setAttribute("DOC_NO", rsTmp.getString("SS_DOC_NO"));
                ObjItem.setAttribute("DOC_DATE", rsTmp.getString("SS_DOC_DATE"));
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
            rsResultSet = Stmt.executeQuery("SELECT * FROM SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE_H WHERE SS_DOC_NO='" + pDocNo + "' GROUP BY SS_DOC_NO ORDER BY REVISION_NO ");
            Ready = true;
            HistoryView = true;
            MoveFirst();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    private void UpdateShift() {
        try {
            String sql;
            int st, end;
            clsShiftScheduleItem ObjMRItems = (clsShiftScheduleItem) colMRItems.get(Integer.toString(1));

            st = Integer.parseInt(getAttribute("SS_SHIFT_EFFECT_FROM_DATE").getString().substring(8, 10));
            end = Integer.parseInt(ObjMRItems.getAttribute("SS_SHIFT_TO_DATE").getString().substring(0, 2));
            for (int i = st; i <= end; i++) {
                sql = "UPDATE  SDMLATTPAY.ATT_SHIFT_SCHEDULE S, SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE C "
                        + "SET SSC_" + i + "=SS_CHANGE_SHIFT "
                        + "WHERE SS_DOC_NO='"+(String) getAttribute("SS_DOC_NO").getObj()+"' AND SSC_EMPID=SS_EMPID AND SSC_MONTH=MONTH(SS_SHIFT_EFFECT_FROM_DATE) AND SSC_YEAR=YEAR(SS_SHIFT_EFFECT_FROM_DATE) "
                        + "AND LENGTH(SS_CHANGE_SHIFT)>0 AND SSC_" + i + " NOT IN ('WO','HL')";
                System.out.println("sql:" + sql);
                data.Execute(sql);
            }
            sql = "UPDATE SDMLATTPAY.ATT_SHIFTSCHEDULE_CHANGE C,SDMLATTPAY.ATT_SHIFTSCHEDULE S "
                    + "SET S.SS_CHANGE_SHIFT=C.SS_CHANGE_SHIFT "
                    + "WHERE C.SS_DOC_NO='" + getAttribute("SS_DOC_NO").getString() + "' AND LEFT(C.SS_DOC_NO,8)=LEFT(S.SS_DOC_NO,8) AND "
                    + "C.SS_EMPID=S.SS_EMPID";
            data.Execute(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
