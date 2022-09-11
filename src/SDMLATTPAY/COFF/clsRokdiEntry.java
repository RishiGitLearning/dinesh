/*
 * clsSales_Party.java
 *
 * Created on April 3, 2009, 10:22 AM
 */
package SDMLATTPAY.COFF;

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
public class clsRokdiEntry {

    public String LastError = "";
    private ResultSet rsResultSet, rsResultSet1;
    private Connection Conn;
    private Statement Stmt;
    DecimalFormat df = new DecimalFormat("##.##");
    private HashMap props;

    public boolean Ready = false;

    private boolean HistoryView = false;
    public static int ModuleID = 816; //72
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
    public clsRokdiEntry() {
        LastError = "";
        props = new HashMap();
        //props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("REVISION_NO", new Variant(0));
        props.put("UPDATED_BY", new Variant(0));
        props.put("ENTRY_DATE", new Variant(""));

        props.put("COFF_DOC_NO", new Variant(""));
        props.put("COFF_DOC_DATE", new Variant(""));
        props.put("COFF_DATE", new Variant(""));
        props.put("COFF_TYPE", new Variant(""));
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
            rsResultSet = Stmt.executeQuery("SELECT * FROM SDMLATTPAY.ATT_COFF_ROKDI_ENTRY WHERE COFF_DOC_NO LIKE 'R%'   GROUP BY COFF_DOC_NO");
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

            rsHistory = stHistory.executeQuery("SELECT * FROM SDMLATTPAY.ATT_COFF_ROKDI_ENTRY_H WHERE COFF_DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail = stHDetail.executeQuery("SELECT * FROM SDMLATTPAY.ATT_COFF_ROKDI_ENTRY_H WHERE COFF_DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//

            //--------- Generate Gatepass requisition no.  ------------
            setAttribute("COFF_DOC_NO", clsFirstFree.getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 816, (int) getAttribute("FFNO").getVal(), true));
            //-------------------------------------------------

            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");

            Statement tmpStmt;

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery("SELECT * FROM SDMLATTPAY.ATT_COFF_ROKDI_ENTRY WHERE COFF_DOC_NO='1'");

            //Now Insert records into detail table
            for (int i = 1; i <= colMRItems.size(); i++) {
                clsRokdiEntryItem ObjMRItems = (clsRokdiEntryItem) colMRItems.get(Integer.toString(i));

                rsResultSet.moveToInsertRow();
                rsResultSet.updateString("COFF_DOC_NO", getAttribute("COFF_DOC_NO").getString());
                rsResultSet.updateString("COFF_DOC_DATE", getAttribute("COFF_DOC_DATE").getString());
                rsResultSet.updateString("COFF_EMPID", (String) ObjMRItems.getAttribute("COFF_EMPID").getString());
                rsResultSet.updateString("COFF_YEAR", (String) ObjMRItems.getAttribute("COFF_YEAR").getString());
                rsResultSet.updateString("COFF_MONTH", (String) ObjMRItems.getAttribute("COFF_MONTH").getString());
                rsResultSet.updateString("COFF_DATE", EITLERPGLOBAL.formatDateDB((String) ObjMRItems.getAttribute("COFF_DATE").getString()));
                rsResultSet.updateString("COFF_TYPE", (String) ObjMRItems.getAttribute("COFF_TYPE").getString());
                rsResultSet.updateString("COFF_SHIFT", (String) ObjMRItems.getAttribute("COFF_SHIFT").getString());
                rsResultSet.updateString("COFF_OT_SHIFT", (String) ObjMRItems.getAttribute("COFF_OT_SHIFT").getString());
                rsResultSet.updateString("COFF_FROM_TIME", (String) ObjMRItems.getAttribute("COFF_FROM_TIME").getString());
                rsResultSet.updateString("COFF_TO_TIME", (String) ObjMRItems.getAttribute("COFF_TO_TIME").getString());
                rsResultSet.updateString("COFF_HRS", (String) ObjMRItems.getAttribute("COFF_HRS").getString());
                rsResultSet.updateString("COFF_HRS_REPLACEMENT", (String) ObjMRItems.getAttribute("COFF_HRS_REPLACEMENT").getString());
                rsResultSet.updateString("COFF_HRS_EXTRA", (String) ObjMRItems.getAttribute("COFF_HRS_EXTRA").getString());
                rsResultSet.updateString("COFF_PUNCHES", (String) ObjMRItems.getAttribute("COFF_PUNCHES").getString());
                rsResultSet.updateString("ROKDI_PUNCHES", (String) ObjMRItems.getAttribute("ROKDI_PUNCHES").getString());
                rsResultSet.updateString("ROKDI_PUNCHES_NEXT", (String) ObjMRItems.getAttribute("ROKDI_PUNCHES_NEXT").getString());
                rsResultSet.updateString("COFF_PUNCHES_NEXT", (String) ObjMRItems.getAttribute("COFF_PUNCHES_NEXT").getString());
                rsResultSet.updateString("COFF_ROKDI_REMARK", (String) ObjMRItems.getAttribute("COFF_ROKDI_REMARK").getString());
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
                rsHistory.updateString("COFF_DOC_NO", getAttribute("COFF_DOC_NO").getString());
                rsHistory.updateString("COFF_DOC_DATE", getAttribute("COFF_DOC_DATE").getString());
                rsHistory.updateString("COFF_EMPID", (String) ObjMRItems.getAttribute("COFF_EMPID").getString());
                rsHistory.updateString("COFF_YEAR", (String) ObjMRItems.getAttribute("COFF_YEAR").getString());
                rsHistory.updateString("COFF_MONTH", (String) ObjMRItems.getAttribute("COFF_MONTH").getString());
                rsHistory.updateString("COFF_DATE", EITLERPGLOBAL.formatDateDB((String) ObjMRItems.getAttribute("COFF_DATE").getString()));
                rsHistory.updateString("COFF_TYPE", (String) ObjMRItems.getAttribute("COFF_TYPE").getString());
                rsHistory.updateString("COFF_SHIFT", (String) ObjMRItems.getAttribute("COFF_SHIFT").getString());
                rsHistory.updateString("COFF_OT_SHIFT", (String) ObjMRItems.getAttribute("COFF_OT_SHIFT").getString());
                rsHistory.updateString("COFF_FROM_TIME", (String) ObjMRItems.getAttribute("COFF_FROM_TIME").getString());
                rsHistory.updateString("COFF_TO_TIME", (String) ObjMRItems.getAttribute("COFF_TO_TIME").getString());
                rsHistory.updateString("COFF_HRS", (String) ObjMRItems.getAttribute("COFF_HRS").getString());
                rsHistory.updateString("COFF_HRS_REPLACEMENT", (String) ObjMRItems.getAttribute("COFF_HRS_REPLACEMENT").getString());
                rsHistory.updateString("COFF_HRS_EXTRA", (String) ObjMRItems.getAttribute("COFF_HRS_EXTRA").getString());
                rsHistory.updateString("COFF_PUNCHES", (String) ObjMRItems.getAttribute("COFF_PUNCHES").getString());
                rsHistory.updateString("ROKDI_PUNCHES", (String) ObjMRItems.getAttribute("ROKDI_PUNCHES").getString());
                rsHistory.updateString("ROKDI_PUNCHES_NEXT", (String) ObjMRItems.getAttribute("ROKDI_PUNCHES_NEXT").getString());
                rsHistory.updateString("COFF_PUNCHES_NEXT", (String) ObjMRItems.getAttribute("COFF_PUNCHES_NEXT").getString());
                rsHistory.updateString("COFF_ROKDI_REMARK", (String) ObjMRItems.getAttribute("COFF_ROKDI_REMARK").getString());
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

            ObjFlow.ModuleID = clsRokdiEntry.ModuleID;
            ObjFlow.CompanyID = EITLERPGLOBAL.gCompanyID;
            ObjFlow.DocNo = (String) getAttribute("COFF_DOC_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("COFF_DOC_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "SDMLATTPAY.ATT_COFF_ROKDI_ENTRY";
            ObjFlow.IsCreator = true;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "COFF_DOC_NO";

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

            rsHistory = stHistory.executeQuery("SELECT * FROM SDMLATTPAY.ATT_COFF_ROKDI_ENTRY_H WHERE COFF_DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail = stHDetail.executeQuery("SELECT * FROM SDMLATTPAY.ATT_COFF_ROKDI_ENTRY_H WHERE COFF_DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//

            String theDocNo = getAttribute("COFF_DOC_NO").getString();
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM SDMLATTPAY.ATT_COFF_ROKDI_ENTRY_H WHERE COFF_DOC_NO='" + (String) getAttribute("COFF_DOC_NO").getObj() + "'");
            RevNo++;
            String RevDocNo = (String) getAttribute("COFF_DOC_NO").getObj();
            if (ApprovalFlow.IsCreator(ModuleID, (String) getAttribute("COFF_DOC_NO").getObj()) || EITLERPGLOBAL.gNewUserID==303) {

                String mDocNo = (String) getAttribute("COFF_DOC_NO").getObj();

                data.Execute("DELETE FROM SDMLATTPAY.ATT_COFF_ROKDI_ENTRY WHERE COFF_DOC_NO='" + mDocNo + "'");

                Statement tmpStmt;

                tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet rsTmp1 = tmpStmt.executeQuery("SELECT * FROM SDMLATTPAY.ATT_COFF_ROKDI_ENTRY WHERE COFF_DOC_NO='1'");

                //Now Insert records into detail table
                for (int i = 1; i <= colMRItems.size(); i++) {
                    clsRokdiEntryItem ObjMRItems = (clsRokdiEntryItem) colMRItems.get(Integer.toString(i));
                    rsResultSet.moveToInsertRow();
                    rsResultSet.updateString("COFF_DOC_NO", getAttribute("COFF_DOC_NO").getString());
                    rsResultSet.updateString("COFF_DOC_DATE", getAttribute("COFF_DOC_DATE").getString());
                    rsResultSet.updateString("COFF_EMPID", (String) ObjMRItems.getAttribute("COFF_EMPID").getString());
                    rsResultSet.updateString("COFF_YEAR", (String) ObjMRItems.getAttribute("COFF_YEAR").getString());
                    rsResultSet.updateString("COFF_MONTH", (String) ObjMRItems.getAttribute("COFF_MONTH").getString());
                    rsResultSet.updateString("COFF_DATE", EITLERPGLOBAL.formatDateDB((String) ObjMRItems.getAttribute("COFF_DATE").getString()));
                    rsResultSet.updateString("COFF_TYPE", (String) ObjMRItems.getAttribute("COFF_TYPE").getString());
                    rsResultSet.updateString("COFF_SHIFT", (String) ObjMRItems.getAttribute("COFF_SHIFT").getString());
                    rsResultSet.updateString("COFF_OT_SHIFT", (String) ObjMRItems.getAttribute("COFF_OT_SHIFT").getString());
                    rsResultSet.updateString("COFF_FROM_TIME", (String) ObjMRItems.getAttribute("COFF_FROM_TIME").getString());
                    rsResultSet.updateString("COFF_TO_TIME", (String) ObjMRItems.getAttribute("COFF_TO_TIME").getString());
                    rsResultSet.updateString("COFF_HRS", (String) ObjMRItems.getAttribute("COFF_HRS").getString());
                    rsResultSet.updateString("COFF_HRS_REPLACEMENT", (String) ObjMRItems.getAttribute("COFF_HRS_REPLACEMENT").getString());
                    rsResultSet.updateString("COFF_HRS_EXTRA", (String) ObjMRItems.getAttribute("COFF_HRS_EXTRA").getString());
                    rsResultSet.updateString("COFF_PUNCHES", (String) ObjMRItems.getAttribute("COFF_PUNCHES").getString());
                    rsResultSet.updateString("ROKDI_PUNCHES", (String) ObjMRItems.getAttribute("ROKDI_PUNCHES").getString());
                    rsResultSet.updateString("ROKDI_PUNCHES_NEXT", (String) ObjMRItems.getAttribute("ROKDI_PUNCHES_NEXT").getString());
                    rsResultSet.updateString("COFF_PUNCHES_NEXT", (String) ObjMRItems.getAttribute("COFF_PUNCHES_NEXT").getString());
                    rsResultSet.updateString("COFF_ROKDI_REMARK", (String) ObjMRItems.getAttribute("COFF_ROKDI_REMARK").getString());
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
                    rsHistory.updateString("COFF_DOC_NO", getAttribute("COFF_DOC_NO").getString());
                    rsHistory.updateString("COFF_DOC_DATE", getAttribute("COFF_DOC_DATE").getString());
                    rsHistory.updateString("COFF_EMPID", (String) ObjMRItems.getAttribute("COFF_EMPID").getString());
                    rsHistory.updateString("COFF_YEAR", (String) ObjMRItems.getAttribute("COFF_YEAR").getString());
                    rsHistory.updateString("COFF_MONTH", (String) ObjMRItems.getAttribute("COFF_MONTH").getString());
                    rsHistory.updateString("COFF_DATE", EITLERPGLOBAL.formatDateDB((String) ObjMRItems.getAttribute("COFF_DATE").getString()));
                    rsHistory.updateString("COFF_TYPE", (String) ObjMRItems.getAttribute("COFF_TYPE").getString());
                    rsHistory.updateString("COFF_SHIFT", (String) ObjMRItems.getAttribute("COFF_SHIFT").getString());
                    rsHistory.updateString("COFF_OT_SHIFT", (String) ObjMRItems.getAttribute("COFF_OT_SHIFT").getString());
                    rsHistory.updateString("COFF_FROM_TIME", (String) ObjMRItems.getAttribute("COFF_FROM_TIME").getString());
                    rsHistory.updateString("COFF_TO_TIME", (String) ObjMRItems.getAttribute("COFF_TO_TIME").getString());
                    rsHistory.updateString("COFF_HRS", (String) ObjMRItems.getAttribute("COFF_HRS").getString());
                    rsHistory.updateString("COFF_HRS_REPLACEMENT", (String) ObjMRItems.getAttribute("COFF_HRS_REPLACEMENT").getString());
                    rsHistory.updateString("COFF_HRS_EXTRA", (String) ObjMRItems.getAttribute("COFF_HRS_EXTRA").getString());
                    rsHistory.updateString("COFF_PUNCHES", (String) ObjMRItems.getAttribute("COFF_PUNCHES").getString());
                    rsHistory.updateString("ROKDI_PUNCHES", (String) ObjMRItems.getAttribute("ROKDI_PUNCHES").getString());
                    rsHistory.updateString("ROKDI_PUNCHES_NEXT", (String) ObjMRItems.getAttribute("ROKDI_PUNCHES_NEXT").getString());
                    rsHistory.updateString("COFF_PUNCHES_NEXT", (String) ObjMRItems.getAttribute("COFF_PUNCHES_NEXT").getString());
                    rsHistory.updateString("COFF_ROKDI_REMARK", (String) ObjMRItems.getAttribute("COFF_ROKDI_REMARK").getString());
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
                clsRokdiEntryItem ObjMRItems = (clsRokdiEntryItem) colMRItems.get(Integer.toString(1));
                rsHistory.moveToInsertRow();
                rsHistory.updateInt("REVISION_NO", RevNo); //Revision No. 1 in case of Insert
                rsHistory.updateInt("UPDATED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                rsHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
                rsHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                rsHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
                rsHistory.updateString("COFF_DOC_NO", getAttribute("COFF_DOC_NO").getString());
                rsHistory.updateString("COFF_DOC_DATE", getAttribute("COFF_DOC_DATE").getString());
                rsHistory.updateString("COFF_EMPID", (String) ObjMRItems.getAttribute("COFF_EMPID").getString());
                rsHistory.updateString("COFF_YEAR", (String) ObjMRItems.getAttribute("COFF_YEAR").getString());
                rsHistory.updateString("COFF_MONTH", (String) ObjMRItems.getAttribute("COFF_MONTH").getString());
                rsHistory.updateString("COFF_DATE", EITLERPGLOBAL.formatDateDB((String) ObjMRItems.getAttribute("COFF_DATE").getString()));
                rsHistory.updateString("COFF_TYPE", (String) ObjMRItems.getAttribute("COFF_TYPE").getString());
                rsHistory.updateString("COFF_SHIFT", (String) ObjMRItems.getAttribute("COFF_SHIFT").getString());
                rsHistory.updateString("COFF_OT_SHIFT", (String) ObjMRItems.getAttribute("COFF_OT_SHIFT").getString());
                rsHistory.updateString("COFF_FROM_TIME", (String) ObjMRItems.getAttribute("COFF_FROM_TIME").getString());
                rsHistory.updateString("COFF_TO_TIME", (String) ObjMRItems.getAttribute("COFF_TO_TIME").getString());
                rsHistory.updateString("COFF_HRS", (String) ObjMRItems.getAttribute("COFF_HRS").getString());
                rsHistory.updateString("COFF_HRS_REPLACEMENT", (String) ObjMRItems.getAttribute("COFF_HRS_REPLACEMENT").getString());
                rsHistory.updateString("COFF_HRS_EXTRA", (String) ObjMRItems.getAttribute("COFF_HRS_EXTRA").getString());
                rsHistory.updateString("COFF_PUNCHES", (String) ObjMRItems.getAttribute("COFF_PUNCHES").getString());
                rsHistory.updateString("ROKDI_PUNCHES", (String) ObjMRItems.getAttribute("ROKDI_PUNCHES").getString());
                rsHistory.updateString("ROKDI_PUNCHES_NEXT", (String) ObjMRItems.getAttribute("ROKDI_PUNCHES_NEXT").getString());
                rsHistory.updateString("COFF_PUNCHES_NEXT", (String) ObjMRItems.getAttribute("COFF_PUNCHES_NEXT").getString());
                rsHistory.updateString("COFF_ROKDI_REMARK", (String) ObjMRItems.getAttribute("COFF_ROKDI_REMARK").getString());
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

            ObjFlow.ModuleID = clsRokdiEntry.ModuleID;
            ObjFlow.CompanyID = EITLERPGLOBAL.gCompanyID;
            ObjFlow.DocNo = (String) getAttribute("COFF_DOC_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("COFF_DOC_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "SDMLATTPAY.ATT_COFF_ROKDI_ENTRY";
            ObjFlow.IsCreator = false;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "COFF_DOC_NO";

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
                data.Execute("UPDATE SDMLATTPAY.ATT_COFF_ROKDI_ENTRY SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='" + ObjFlow.DocNo + "' ");

                //Remove Old Records from FELT_DOC_DATA
                data.Execute("DELETE FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=" + clsRokdiEntry.ModuleID + " AND DOC_NO='" + ObjFlow.DocNo + "'");

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

            setAttribute("COFF_DOC_NO", rsResultSet.getString("COFF_DOC_NO"));
            setAttribute("COFF_DOC_DATE", rsResultSet.getString("COFF_DOC_DATE"));
            setAttribute("COFF_DATE", rsResultSet.getString("COFF_DATE"));
            setAttribute("COFF_TYPE", rsResultSet.getString("COFF_TYPE"));
            setAttribute("HIERARCHY_ID", rsResultSet.getDouble("HIERARCHY_ID"));

            colMRItems.clear();

            String mDocNo = (String) getAttribute("COFF_DOC_NO").getObj();

            tmpStmt = tmpConn.createStatement();
            if (HistoryView) {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM SDMLATTPAY.ATT_COFF_ROKDI_ENTRY_H WHERE COFF_DOC_NO='" + mDocNo + "' AND REVISION_NO=" + RevNo + " ");
            } else {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM SDMLATTPAY.ATT_COFF_ROKDI_ENTRY WHERE COFF_DOC_NO='" + mDocNo + "' ");
            }
            rsTmp.first();

            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {
                Counter = Counter + 1;
                clsRokdiEntryItem ObjMRItems = new clsRokdiEntryItem();
                ObjMRItems.setAttribute("COFF_DOC_NO", rsTmp.getString("COFF_DOC_NO"));
                ObjMRItems.setAttribute("COFF_DOC_DATE", rsTmp.getString("COFF_DOC_DATE"));
                ObjMRItems.setAttribute("COFF_EMPID", rsTmp.getString("COFF_EMPID"));
                ObjMRItems.setAttribute("COFF_YEAR", rsTmp.getString("COFF_YEAR"));
                ObjMRItems.setAttribute("COFF_MONTH", rsTmp.getString("COFF_MONTH"));
                ObjMRItems.setAttribute("COFF_DATE", rsTmp.getString("COFF_DATE"));
                ObjMRItems.setAttribute("COFF_TYPE", rsTmp.getString("COFF_TYPE"));
                ObjMRItems.setAttribute("COFF_SHIFT", rsTmp.getString("COFF_SHIFT"));
                ObjMRItems.setAttribute("COFF_OT_SHIFT", rsTmp.getString("COFF_OT_SHIFT"));
                ObjMRItems.setAttribute("COFF_FROM_TIME", rsTmp.getString("COFF_FROM_TIME"));
                ObjMRItems.setAttribute("COFF_TO_TIME", rsTmp.getString("COFF_TO_TIME"));
                ObjMRItems.setAttribute("COFF_HRS", rsTmp.getString("COFF_HRS"));
                ObjMRItems.setAttribute("COFF_HRS_REPLACEMENT", rsTmp.getString("COFF_HRS_REPLACEMENT"));
                ObjMRItems.setAttribute("COFF_HRS_EXTRA", rsTmp.getString("COFF_HRS_EXTRA"));
                ObjMRItems.setAttribute("COFF_PUNCHES", rsTmp.getString("COFF_PUNCHES"));
                ObjMRItems.setAttribute("ROKDI_PUNCHES", rsTmp.getString("ROKDI_PUNCHES"));
                ObjMRItems.setAttribute("ROKDI_PUNCHES_NEXT", rsTmp.getString("ROKDI_PUNCHES_NEXT"));
                ObjMRItems.setAttribute("COFF_PUNCHES_NEXT", rsTmp.getString("COFF_PUNCHES_NEXT"));
                ObjMRItems.setAttribute("COFF_ROKDI_REMARK", rsTmp.getString("COFF_ROKDI_REMARK"));
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.ATT_COFF_ROKDI_ENTRY WHERE COFF_DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=816 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND STATUS='W'";
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
            String strSQL = "SELECT COFF_DOC_NO,COFF_DOC_DATE,REVISION_NO,UPDATED_BY,APPROVAL_STATUS,DATE_FORMAT(ENTRY_DATE,'%d/%m/%Y %H:%i:%S') AS ENTRY_DATE,REJECTED_REMARKS AS APPROVER_REMARKS,FROM_IP FROM SDMLATTPAY.ATT_COFF_ROKDI_ENTRY_H WHERE COFF_DOC_NO='" + DocNo + "' GROUP BY COFF_DOC_NO,REVISION_NO";
            rsTmp = data.getResult(strSQL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                while (!rsTmp.isAfterLast()) {
                    clsRokdiEntry objParty = new clsRokdiEntry();

                    objParty.setAttribute("COFF_DOC_NO", rsTmp.getString("COFF_DOC_NO"));
                    objParty.setAttribute("COFF_DOC_DATE", rsTmp.getString("COFF_DOC_DATE"));
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
            strSQL += "SELECT * FROM SDMLATTPAY.ATT_COFF_ROKDI_ENTRY WHERE " + Condition + " AND COFF_DOC_NO LIKE 'R%' GROUP BY COFF_DOC_NO";
            System.out.println("Filter Query :" + strSQL);
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery(strSQL);

            if (!rsResultSet.first()) {
                strSQL = "SELECT * FROM SDMLATTPAY.ATT_COFF_ROKDI_ENTRY WHERE COFF_DOC_NO LIKE 'R%' GROUP BY COFF_DOC_NO";
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.ATT_COFF_ROKDI_ENTRY WHERE COFF_DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=816 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND TYPE='C' AND STATUS='W'";
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
                strSQL = "SELECT SDMLATTPAY.ATT_COFF_ROKDI_ENTRY.COFF_DOC_NO,SDMLATTPAY.ATT_COFF_ROKDI_ENTRY.COFF_DOC_DATE,RECEIVED_DATE FROM SDMLATTPAY.ATT_COFF_ROKDI_ENTRY,SDMLATTPAY.D_COM_DOC_DATA WHERE SDMLATTPAY.D_COM_DOC_DATA.DOC_NO=SDMLATTPAY.ATT_COFF_ROKDI_ENTRY.COFF_DOC_NO AND SDMLATTPAY.D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND SDMLATTPAY.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=816 GROUP BY SDMLATTPAY.ATT_COFF_ROKDI_ENTRY.COFF_DOC_NO ORDER BY SDMLATTPAY.D_COM_DOC_DATA.RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT SDMLATTPAY.ATT_COFF_ROKDI_ENTRY.COFF_DOC_NO,SDMLATTPAY.ATT_COFF_ROKDI_ENTRY.COFF_DOC_DATE,RECEIVED_DATE FROM SDMLATTPAY.ATT_COFF_ROKDI_ENTRY,SDMLATTPAY.D_COM_DOC_DATA WHERE SDMLATTPAY.D_COM_DOC_DATA.DOC_NO=SDMLATTPAY.ATT_COFF_ROKDI_ENTRY.COFF_DOC_NO AND SDMLATTPAY.D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND SDMLATTPAY.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=816 GROUP BY SDMLATTPAY.ATT_COFF_ROKDI_ENTRY.COFF_DOC_NO  ORDER BY SDMLATTPAY.ATT_COFF_ROKDI_ENTRY.COFF_DOC_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT SDMLATTPAY.ATT_COFF_ROKDI_ENTRY.COFF_DOC_NO,SDMLATTPAY.ATT_COFF_ROKDI_ENTRY.COFF_DOC_DATE,RECEIVED_DATE FROM SDMLATTPAY.ATT_COFF_ROKDI_ENTRY,SDMLATTPAY.D_COM_DOC_DATA WHERE SDMLATTPAY.D_COM_DOC_DATA.DOC_NO=SDMLATTPAY.ATT_COFF_ROKDI_ENTRY.COFF_DOC_NO AND SDMLATTPAY.D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND SDMLATTPAY.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=816 GROUP BY SDMLATTPAY.ATT_COFF_ROKDI_ENTRY.COFF_DOC_NO  ORDER BY SDMLATTPAY.ATT_COFF_ROKDI_ENTRY.COFF_DOC_NO";
            }

            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {

                Counter = Counter + 1;
                clsRokdiEntry ObjItem = new clsRokdiEntry();

                //------------- Header Fields --------------------//
                ObjItem.setAttribute("DOC_NO", rsTmp.getString("COFF_DOC_NO"));
                ObjItem.setAttribute("DOC_DATE", rsTmp.getString("COFF_DOC_DATE"));
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
            rsResultSet = Stmt.executeQuery("SELECT * FROM SDMLATTPAY.ATT_COFF_ROKDI_ENTRY_H WHERE DOC_NO='" + pDocNo + "' GROUP BY COFF_DOC_NO ORDER BY REVISION_NO ");
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
