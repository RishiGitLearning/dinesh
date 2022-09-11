/*
 * clsSales_Party.java
 *
 * Created on April 3, 2009, 10:22 AM
 */
package SDMLATTPAY.TravelAdvance;

import EITLERP.EITLERPGLOBAL;
import EITLERP.Variant;
import EITLERP.clsFirstFree;
import EITLERP.data;
import SDMLATTPAY.ApprovalFlow;
import java.util.*;
import java.sql.*;
import java.text.DecimalFormat;

/**
 *
 * @author
 */
public class clsTravelEntryAmend {

    public String LastError = "";
    private ResultSet rsResultSet, rsResultSet1;
    private Connection Conn;
    private Statement Stmt;
    DecimalFormat df = new DecimalFormat("##.##");
    private HashMap props;

    public boolean Ready = false;

    private boolean HistoryView = false;
    public static int ModuleID = 859; //72
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
    public clsTravelEntryAmend() {
        LastError = "";
        props = new HashMap();
        //props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("REVISION_NO", new Variant(0));
        props.put("UPDATED_BY", new Variant(0));
        props.put("ENTRY_DATE", new Variant(""));

        props.put("TSD_DOC_NO", new Variant(""));
        props.put("COFF_DOC_DATE", new Variant(""));
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
            rsResultSet = Stmt.executeQuery("SELECT * FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND  GROUP BY TSD_DOC_NO");
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

            rsHistory = stHistory.executeQuery("SELECT * FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND_H WHERE TSD_DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail = stHDetail.executeQuery("SELECT * FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND_H WHERE TSD_DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//

            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");

            Statement tmpStmt;

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery("SELECT * FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND WHERE TSD_DOC_NO='1'");

            //Now Insert records into detail table
            for (int i = 1; i <= colMRItems.size(); i++) {
                clsTravelEntryItem ObjMRItems = (clsTravelEntryItem) colMRItems.get(Integer.toString(i));

                rsResultSet.moveToInsertRow();
                rsResultSet.updateString("TSD_DOC_NO", getAttribute("TSD_DOC_NO").getString());
                rsResultSet.updateString("TSD_DOC_DATE", getAttribute("TSD_DOC_DATE").getString());
                rsResultSet.updateString("TSD_PAY_EMP_NO", getAttribute("TSD_PAY_EMP_NO").getString());
                rsResultSet.updateString("TSD_DEPARTMENT", getAttribute("TSD_DEPARTMENT").getString());
                rsResultSet.updateString("TSD_DESIGNATION", getAttribute("TSD_DESIGNATION").getString());
                rsResultSet.updateString("TSD_LEVEL", getAttribute("TSD_LEVEL").getString());
                rsResultSet.updateString("TSD_NAME", getAttribute("TSD_NAME").getString());
                rsResultSet.updateString("TSD_CATEGORY", getAttribute("TSD_CATEGORY").getString());
                rsResultSet.updateString("TSD_PURPOSE_OF_VISIT", (String) ObjMRItems.getAttribute("TSD_PURPOSE_OF_VISIT").getString());
                rsResultSet.updateString("TSD_AUDIT_REMARK", getAttribute("TSD_AUDIT_REMARK").getString());
                rsResultSet.updateString("TSD_TOTAL_FARE", getAttribute("TSD_TOTAL_FARE").getString());
                rsResultSet.updateString("TSD_TOTAL_EXPENSE", getAttribute("TSD_TOTAL_EXPENSE").getString());
                rsResultSet.updateString("TSD_TOTAL", getAttribute("TSD_TOTAL").getString());
                rsResultSet.updateString("TSD_ADVANCE", getAttribute("TSD_ADVANCE").getString());
                rsResultSet.updateString("TSD_SR_NO", (String) ObjMRItems.getAttribute("TSD_SR_NO").getString());
                rsResultSet.updateString("TSD_START_PLACE", (String) ObjMRItems.getAttribute("TSD_START_PLACE").getString());
                rsResultSet.updateString("TSD_START_DATE", EITLERPGLOBAL.formatDateDB((String) ObjMRItems.getAttribute("TSD_START_DATE").getString()));
                rsResultSet.updateString("TSD_START_TIME", (String) ObjMRItems.getAttribute("TSD_START_TIME").getString());
                rsResultSet.updateString("TSD_TRAVEL_MODE", (String) ObjMRItems.getAttribute("TSD_TRAVEL_MODE").getString());
                rsResultSet.updateString("TSD_FARE", (String) ObjMRItems.getAttribute("TSD_FARE").getString());
                rsResultSet.updateString("TSD_VISIT_PLACE", (String) ObjMRItems.getAttribute("TSD_VISIT_PLACE").getString());
                rsResultSet.updateString("TSD_IS_VISIT_PLACE", (String) ObjMRItems.getAttribute("TSD_IS_VISIT_PLACE").getString());
                rsResultSet.updateString("TSD_ARRIVAL_DATE", EITLERPGLOBAL.formatDateDB((String) ObjMRItems.getAttribute("TSD_ARRIVAL_DATE").getString()));
                rsResultSet.updateString("TSD_ARRIVAL_TIME", (String) ObjMRItems.getAttribute("TSD_ARRIVAL_TIME").getString());
                rsResultSet.updateString("TSD_SEMINAR", (String) ObjMRItems.getAttribute("TSD_SEMINAR").getString());
                rsResultSet.updateString("TSD_DAYS", (String) ObjMRItems.getAttribute("TSD_DAYS").getString());
                rsResultSet.updateString("TSD_RATE", (String) ObjMRItems.getAttribute("TSD_RATE").getString());
                rsResultSet.updateString("TSD_AMOUNT", (String) ObjMRItems.getAttribute("TSD_AMOUNT").getString());
                rsResultSet.updateString("TSD_INFO", (String) ObjMRItems.getAttribute("TSD_INFO").getString());
                rsResultSet.updateString("TSD_REMARK", (String) ObjMRItems.getAttribute("TSD_REMARK").getString());
                rsResultSet.updateString("TSD_REASON", getAttribute("TSD_REASON").getString());
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
                rsHistory.updateString("TSD_DOC_NO", getAttribute("TSD_DOC_NO").getString());
                rsHistory.updateString("TSD_DOC_DATE", getAttribute("TSD_DOC_DATE").getString());
                rsHistory.updateString("TSD_PAY_EMP_NO", getAttribute("TSD_PAY_EMP_NO").getString());
                rsHistory.updateString("TSD_DEPARTMENT", getAttribute("TSD_DEPARTMENT").getString());
                rsHistory.updateString("TSD_DESIGNATION", getAttribute("TSD_DESIGNATION").getString());
                rsHistory.updateString("TSD_LEVEL", getAttribute("TSD_LEVEL").getString());
                rsHistory.updateString("TSD_NAME", getAttribute("TSD_NAME").getString());
                rsHistory.updateString("TSD_CATEGORY", getAttribute("TSD_CATEGORY").getString());
                rsHistory.updateString("TSD_PURPOSE_OF_VISIT", (String) ObjMRItems.getAttribute("TSD_PURPOSE_OF_VISIT").getString());
                rsHistory.updateString("TSD_AUDIT_REMARK", getAttribute("TSD_AUDIT_REMARK").getString());
                rsHistory.updateString("TSD_TOTAL_FARE", getAttribute("TSD_TOTAL_FARE").getString());
                rsHistory.updateString("TSD_TOTAL_EXPENSE", getAttribute("TSD_TOTAL_EXPENSE").getString());
                rsHistory.updateString("TSD_TOTAL", getAttribute("TSD_TOTAL").getString());
                rsHistory.updateString("TSD_ADVANCE", getAttribute("TSD_ADVANCE").getString());
                rsHistory.updateString("TSD_SR_NO", (String) ObjMRItems.getAttribute("TSD_SR_NO").getString());
                rsHistory.updateString("TSD_START_PLACE", (String) ObjMRItems.getAttribute("TSD_START_PLACE").getString());
                rsHistory.updateString("TSD_START_DATE", EITLERPGLOBAL.formatDateDB((String) ObjMRItems.getAttribute("TSD_START_DATE").getString()));
                rsHistory.updateString("TSD_START_TIME", (String) ObjMRItems.getAttribute("TSD_START_TIME").getString());
                rsHistory.updateString("TSD_TRAVEL_MODE", (String) ObjMRItems.getAttribute("TSD_TRAVEL_MODE").getString());
                rsHistory.updateString("TSD_FARE", (String) ObjMRItems.getAttribute("TSD_FARE").getString());
                rsHistory.updateString("TSD_VISIT_PLACE", (String) ObjMRItems.getAttribute("TSD_VISIT_PLACE").getString());
                rsHistory.updateString("TSD_IS_VISIT_PLACE", (String) ObjMRItems.getAttribute("TSD_IS_VISIT_PLACE").getString());
                rsHistory.updateString("TSD_ARRIVAL_DATE", EITLERPGLOBAL.formatDateDB((String) ObjMRItems.getAttribute("TSD_ARRIVAL_DATE").getString()));
                rsHistory.updateString("TSD_ARRIVAL_TIME", (String) ObjMRItems.getAttribute("TSD_ARRIVAL_TIME").getString());
                rsHistory.updateString("TSD_SEMINAR", (String) ObjMRItems.getAttribute("TSD_SEMINAR").getString());
                rsHistory.updateString("TSD_DAYS", (String) ObjMRItems.getAttribute("TSD_DAYS").getString());
                rsHistory.updateString("TSD_RATE", (String) ObjMRItems.getAttribute("TSD_RATE").getString());
                rsHistory.updateString("TSD_AMOUNT", (String) ObjMRItems.getAttribute("TSD_AMOUNT").getString());
                rsHistory.updateString("TSD_INFO", (String) ObjMRItems.getAttribute("TSD_INFO").getString());
                rsHistory.updateString("TSD_REMARK", (String) ObjMRItems.getAttribute("TSD_REMARK").getString());
                rsHistory.updateString("TSD_REASON", getAttribute("TSD_REASON").getString());
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

            ObjFlow.ModuleID = clsTravelEntryAmend.ModuleID;
            ObjFlow.CompanyID = EITLERPGLOBAL.gCompanyID;
            ObjFlow.DocNo = (String) getAttribute("TSD_DOC_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("TSD_DOC_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND";
            ObjFlow.IsCreator = true;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "TSD_DOC_NO";

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
                UpdateLeave();
            }
            if (ObjFlow.Status.equalsIgnoreCase("A")) {
                int updtlv = data.getIntValueFromDB("SELECT COUNT(*) FROM SDMLATTPAY.D_COM_DOC_DATA "
                        + "WHERE DOC_NO='"+(String) getAttribute("TSD_DOC_NO").getObj()+"' AND MODULE_ID="+ModuleID+" AND STATUS='W' AND USER_ID IN (125)");
                if(updtlv==1){
                    UpdateLeave();
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

            rsHistory = stHistory.executeQuery("SELECT * FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND_H WHERE TSD_DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail = stHDetail.executeQuery("SELECT * FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND_H WHERE TSD_DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//

            String theDocNo = getAttribute("TSD_DOC_NO").getString();
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND_H WHERE TSD_DOC_NO='" + (String) getAttribute("TSD_DOC_NO").getObj() + "'");
            RevNo++;
            String RevDocNo = (String) getAttribute("TSD_DOC_NO").getObj();
            if (ApprovalFlow.IsCreator(ModuleID, (String) getAttribute("TSD_DOC_NO").getObj())) {

                String mDocNo = (String) getAttribute("TSD_DOC_NO").getObj();

                data.Execute("DELETE FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND WHERE TSD_DOC_NO='" + mDocNo + "'");

                Statement tmpStmt;

                tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet rsTmp1 = tmpStmt.executeQuery("SELECT * FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND WHERE TSD_DOC_NO='1'");

                //Now Insert records into detail table
                for (int i = 1; i <= colMRItems.size(); i++) {
                    clsTravelEntryItem ObjMRItems = (clsTravelEntryItem) colMRItems.get(Integer.toString(i));
                    rsResultSet.moveToInsertRow();
                    rsResultSet.updateString("TSD_DOC_NO", getAttribute("TSD_DOC_NO").getString());
                    rsResultSet.updateString("TSD_DOC_DATE", getAttribute("TSD_DOC_DATE").getString());
                    rsResultSet.updateString("TSD_PAY_EMP_NO", getAttribute("TSD_PAY_EMP_NO").getString());
                    rsResultSet.updateString("TSD_DEPARTMENT", getAttribute("TSD_DEPARTMENT").getString());
                    rsResultSet.updateString("TSD_DESIGNATION", getAttribute("TSD_DESIGNATION").getString());
                    rsResultSet.updateString("TSD_LEVEL", getAttribute("TSD_LEVEL").getString());
                    rsResultSet.updateString("TSD_NAME", getAttribute("TSD_NAME").getString());
                    rsResultSet.updateString("TSD_CATEGORY", getAttribute("TSD_CATEGORY").getString());
                    rsResultSet.updateString("TSD_PURPOSE_OF_VISIT", (String) ObjMRItems.getAttribute("TSD_PURPOSE_OF_VISIT").getString());
                    rsResultSet.updateString("TSD_AUDIT_REMARK", getAttribute("TSD_AUDIT_REMARK").getString());
                    rsResultSet.updateString("TSD_TOTAL_FARE", getAttribute("TSD_TOTAL_FARE").getString());
                    rsResultSet.updateString("TSD_TOTAL_EXPENSE", getAttribute("TSD_TOTAL_EXPENSE").getString());
                    rsResultSet.updateString("TSD_TOTAL", getAttribute("TSD_TOTAL").getString());
                    rsResultSet.updateString("TSD_ADVANCE", getAttribute("TSD_ADVANCE").getString());
                    rsResultSet.updateString("TSD_SR_NO", (String) ObjMRItems.getAttribute("TSD_SR_NO").getString());
                    rsResultSet.updateString("TSD_START_PLACE", (String) ObjMRItems.getAttribute("TSD_START_PLACE").getString());
                    rsResultSet.updateString("TSD_START_DATE", EITLERPGLOBAL.formatDateDB((String) ObjMRItems.getAttribute("TSD_START_DATE").getString()));
                    rsResultSet.updateString("TSD_START_TIME", (String) ObjMRItems.getAttribute("TSD_START_TIME").getString());
                    rsResultSet.updateString("TSD_TRAVEL_MODE", (String) ObjMRItems.getAttribute("TSD_TRAVEL_MODE").getString());
                    rsResultSet.updateString("TSD_FARE", (String) ObjMRItems.getAttribute("TSD_FARE").getString());
                    rsResultSet.updateString("TSD_VISIT_PLACE", (String) ObjMRItems.getAttribute("TSD_VISIT_PLACE").getString());
                    rsResultSet.updateString("TSD_IS_VISIT_PLACE", (String) ObjMRItems.getAttribute("TSD_IS_VISIT_PLACE").getString());
                    rsResultSet.updateString("TSD_ARRIVAL_DATE", EITLERPGLOBAL.formatDateDB((String) ObjMRItems.getAttribute("TSD_ARRIVAL_DATE").getString()));
                    rsResultSet.updateString("TSD_ARRIVAL_TIME", (String) ObjMRItems.getAttribute("TSD_ARRIVAL_TIME").getString());
                    rsResultSet.updateString("TSD_SEMINAR", (String) ObjMRItems.getAttribute("TSD_SEMINAR").getString());
                    rsResultSet.updateString("TSD_DAYS", (String) ObjMRItems.getAttribute("TSD_DAYS").getString());
                    rsResultSet.updateString("TSD_RATE", (String) ObjMRItems.getAttribute("TSD_RATE").getString());
                    rsResultSet.updateString("TSD_AMOUNT", (String) ObjMRItems.getAttribute("TSD_AMOUNT").getString());
                    rsResultSet.updateString("TSD_INFO", (String) ObjMRItems.getAttribute("TSD_INFO").getString());
                    rsResultSet.updateString("TSD_REMARK", (String) ObjMRItems.getAttribute("TSD_REMARK").getString());
                    rsResultSet.updateString("TSD_REASON", getAttribute("TSD_REASON").getString());
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
                    rsHistory.updateString("TSD_DOC_NO", getAttribute("TSD_DOC_NO").getString());
                    rsHistory.updateString("TSD_DOC_DATE", getAttribute("TSD_DOC_DATE").getString());
                    rsHistory.updateString("TSD_PAY_EMP_NO", getAttribute("TSD_PAY_EMP_NO").getString());
                    rsHistory.updateString("TSD_DEPARTMENT", getAttribute("TSD_DEPARTMENT").getString());
                    rsHistory.updateString("TSD_DESIGNATION", getAttribute("TSD_DESIGNATION").getString());
                    rsHistory.updateString("TSD_LEVEL", getAttribute("TSD_LEVEL").getString());
                    rsHistory.updateString("TSD_NAME", getAttribute("TSD_NAME").getString());
                    rsHistory.updateString("TSD_CATEGORY", getAttribute("TSD_CATEGORY").getString());
                    rsHistory.updateString("TSD_PURPOSE_OF_VISIT", (String) ObjMRItems.getAttribute("TSD_PURPOSE_OF_VISIT").getString());
                    rsHistory.updateString("TSD_AUDIT_REMARK", getAttribute("TSD_AUDIT_REMARK").getString());
                    rsHistory.updateString("TSD_TOTAL_FARE", getAttribute("TSD_TOTAL_FARE").getString());
                    rsHistory.updateString("TSD_TOTAL_EXPENSE", getAttribute("TSD_TOTAL_EXPENSE").getString());
                    rsHistory.updateString("TSD_TOTAL", getAttribute("TSD_TOTAL").getString());
                    rsHistory.updateString("TSD_ADVANCE", getAttribute("TSD_ADVANCE").getString());
                    rsHistory.updateString("TSD_SR_NO", (String) ObjMRItems.getAttribute("TSD_SR_NO").getString());
                    rsHistory.updateString("TSD_START_PLACE", (String) ObjMRItems.getAttribute("TSD_START_PLACE").getString());
                    rsHistory.updateString("TSD_START_DATE", EITLERPGLOBAL.formatDateDB((String) ObjMRItems.getAttribute("TSD_START_DATE").getString()));
                    rsHistory.updateString("TSD_START_TIME", (String) ObjMRItems.getAttribute("TSD_START_TIME").getString());
                    rsHistory.updateString("TSD_TRAVEL_MODE", (String) ObjMRItems.getAttribute("TSD_TRAVEL_MODE").getString());
                    rsHistory.updateString("TSD_FARE", (String) ObjMRItems.getAttribute("TSD_FARE").getString());
                    rsHistory.updateString("TSD_VISIT_PLACE", (String) ObjMRItems.getAttribute("TSD_VISIT_PLACE").getString());
                    rsHistory.updateString("TSD_IS_VISIT_PLACE", (String) ObjMRItems.getAttribute("TSD_IS_VISIT_PLACE").getString());
                    rsHistory.updateString("TSD_ARRIVAL_DATE", EITLERPGLOBAL.formatDateDB((String) ObjMRItems.getAttribute("TSD_ARRIVAL_DATE").getString()));
                    rsHistory.updateString("TSD_ARRIVAL_TIME", (String) ObjMRItems.getAttribute("TSD_ARRIVAL_TIME").getString());
                    rsHistory.updateString("TSD_SEMINAR", (String) ObjMRItems.getAttribute("TSD_SEMINAR").getString());
                    rsHistory.updateString("TSD_DAYS", (String) ObjMRItems.getAttribute("TSD_DAYS").getString());
                    rsHistory.updateString("TSD_RATE", (String) ObjMRItems.getAttribute("TSD_RATE").getString());
                    rsHistory.updateString("TSD_AMOUNT", (String) ObjMRItems.getAttribute("TSD_AMOUNT").getString());
                    rsHistory.updateString("TSD_INFO", (String) ObjMRItems.getAttribute("TSD_INFO").getString());
                    rsHistory.updateString("TSD_REMARK", (String) ObjMRItems.getAttribute("TSD_REMARK").getString());
                    rsHistory.updateString("TSD_REASON", getAttribute("TSD_REASON").getString());
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

                data.Execute("UPDATE SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND "
                        + "SET TSD_AUDIT_REMARK='" + getAttribute("TSD_AUDIT_REMARK").getString() + "' "
                        + "WHERE TSD_DOC_NO='" + getAttribute("TSD_DOC_NO").getString() + "'");
                //========= Inserting Into History =================//
                //Get the Maximum Revision No.
                //========= Inserting Into History =================//
                //Get the Maximum Revision No.
                clsTravelEntryItem ObjMRItems = (clsTravelEntryItem) colMRItems.get(Integer.toString(1));
                rsHistory.moveToInsertRow();
                rsHistory.updateInt("REVISION_NO", RevNo); //Revision No. 1 in case of Insert
                rsHistory.updateInt("UPDATED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                rsHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
                rsHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                rsHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
                rsHistory.updateString("TSD_DOC_NO", getAttribute("TSD_DOC_NO").getString());
                rsHistory.updateString("TSD_DOC_DATE", getAttribute("TSD_DOC_DATE").getString());
                rsHistory.updateString("TSD_PAY_EMP_NO", getAttribute("TSD_PAY_EMP_NO").getString());
                rsHistory.updateString("TSD_DEPARTMENT", getAttribute("TSD_DEPARTMENT").getString());
                rsHistory.updateString("TSD_DESIGNATION", getAttribute("TSD_DESIGNATION").getString());
                rsHistory.updateString("TSD_LEVEL", getAttribute("TSD_LEVEL").getString());
                rsHistory.updateString("TSD_NAME", getAttribute("TSD_NAME").getString());
                rsHistory.updateString("TSD_CATEGORY", getAttribute("TSD_CATEGORY").getString());
                rsHistory.updateString("TSD_PURPOSE_OF_VISIT", (String) ObjMRItems.getAttribute("TSD_PURPOSE_OF_VISIT").getString());
                rsHistory.updateString("TSD_AUDIT_REMARK", getAttribute("TSD_AUDIT_REMARK").getString());
                rsHistory.updateString("TSD_TOTAL_FARE", getAttribute("TSD_TOTAL_FARE").getString());
                rsHistory.updateString("TSD_TOTAL_EXPENSE", getAttribute("TSD_TOTAL_EXPENSE").getString());
                rsHistory.updateString("TSD_TOTAL", getAttribute("TSD_TOTAL").getString());
                rsHistory.updateString("TSD_ADVANCE", getAttribute("TSD_ADVANCE").getString());
                rsHistory.updateString("TSD_SR_NO", (String) ObjMRItems.getAttribute("TSD_SR_NO").getString());
                rsHistory.updateString("TSD_START_PLACE", (String) ObjMRItems.getAttribute("TSD_START_PLACE").getString());
                rsHistory.updateString("TSD_START_DATE", EITLERPGLOBAL.formatDateDB((String) ObjMRItems.getAttribute("TSD_START_DATE").getString()));
                rsHistory.updateString("TSD_START_TIME", (String) ObjMRItems.getAttribute("TSD_START_TIME").getString());
                rsHistory.updateString("TSD_TRAVEL_MODE", (String) ObjMRItems.getAttribute("TSD_TRAVEL_MODE").getString());
                rsHistory.updateString("TSD_FARE", (String) ObjMRItems.getAttribute("TSD_FARE").getString());
                rsHistory.updateString("TSD_VISIT_PLACE", (String) ObjMRItems.getAttribute("TSD_VISIT_PLACE").getString());
                rsHistory.updateString("TSD_IS_VISIT_PLACE", (String) ObjMRItems.getAttribute("TSD_IS_VISIT_PLACE").getString());
                rsHistory.updateString("TSD_ARRIVAL_DATE", EITLERPGLOBAL.formatDateDB((String) ObjMRItems.getAttribute("TSD_ARRIVAL_DATE").getString()));
                rsHistory.updateString("TSD_ARRIVAL_TIME", (String) ObjMRItems.getAttribute("TSD_ARRIVAL_TIME").getString());
                rsHistory.updateString("TSD_SEMINAR", (String) ObjMRItems.getAttribute("TSD_SEMINAR").getString());
                rsHistory.updateString("TSD_DAYS", (String) ObjMRItems.getAttribute("TSD_DAYS").getString());
                rsHistory.updateString("TSD_RATE", (String) ObjMRItems.getAttribute("TSD_RATE").getString());
                rsHistory.updateString("TSD_AMOUNT", (String) ObjMRItems.getAttribute("TSD_AMOUNT").getString());
                rsHistory.updateString("TSD_INFO", (String) ObjMRItems.getAttribute("TSD_INFO").getString());
                rsHistory.updateString("TSD_REMARK", (String) ObjMRItems.getAttribute("TSD_REMARK").getString());
                rsHistory.updateString("TSD_REASON", getAttribute("TSD_REASON").getString());
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

            ObjFlow.ModuleID = clsTravelEntryAmend.ModuleID;
            ObjFlow.CompanyID = EITLERPGLOBAL.gCompanyID;
            ObjFlow.DocNo = (String) getAttribute("TSD_DOC_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("TSD_DOC_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND";
            ObjFlow.IsCreator = false;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "TSD_DOC_NO";

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
                data.Execute("UPDATE SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE TSD_DOC_NO='" + ObjFlow.DocNo + "' ");

                //Remove Old Records from FELT_DOC_DATA
                data.Execute("DELETE FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=" + clsTravelEntryAmend.ModuleID + " AND DOC_NO='" + ObjFlow.DocNo + "'");

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
                UpdateLeave();
            }
            if (ObjFlow.Status.equalsIgnoreCase("A")) {
                int updtlv = data.getIntValueFromDB("SELECT COUNT(*) FROM SDMLATTPAY.D_COM_DOC_DATA "
                        + "WHERE DOC_NO='"+(String) getAttribute("TSD_DOC_NO").getObj()+"' AND MODULE_ID="+ModuleID+" AND STATUS='W' AND USER_ID IN (125)");
                if(updtlv==1){
                    UpdateLeave();
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

            setAttribute("TSD_DOC_NO", rsResultSet.getString("TSD_DOC_NO"));
            setAttribute("TSD_DOC_DATE", rsResultSet.getString("TSD_DOC_DATE"));
            setAttribute("TSD_PAY_EMP_NO", rsResultSet.getString("TSD_PAY_EMP_NO"));
            setAttribute("TSD_DEPARTMENT", rsResultSet.getString("TSD_DEPARTMENT"));
            setAttribute("TSD_DESIGNATION", rsResultSet.getString("TSD_DESIGNATION"));
            setAttribute("TSD_LEVEL", rsResultSet.getString("TSD_LEVEL"));
            setAttribute("TSD_NAME", rsResultSet.getString("TSD_NAME"));
            setAttribute("TSD_CATEGORY", rsResultSet.getString("TSD_CATEGORY"));
            setAttribute("TSD_PURPOSE_OF_VISIT", rsResultSet.getString("TSD_PURPOSE_OF_VISIT"));
            setAttribute("TSD_AUDIT_REMARK", rsResultSet.getString("TSD_AUDIT_REMARK"));
            setAttribute("TSD_TOTAL_FARE", rsResultSet.getString("TSD_TOTAL_FARE"));
            setAttribute("TSD_TOTAL_EXPENSE", rsResultSet.getString("TSD_TOTAL_EXPENSE"));
            setAttribute("TSD_TOTAL", rsResultSet.getString("TSD_TOTAL"));
            setAttribute("TSD_ADVANCE", rsResultSet.getString("TSD_ADVANCE"));
            setAttribute("TSD_REASON", rsResultSet.getString("TSD_REASON"));

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

            colMRItems.clear();

            String mDocNo = (String) getAttribute("TSD_DOC_NO").getObj();

            tmpStmt = tmpConn.createStatement();
            if (HistoryView) {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND_H WHERE TSD_DOC_NO='" + mDocNo + "' AND REVISION_NO=" + RevNo + " ");
            } else {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND WHERE TSD_DOC_NO='" + mDocNo + "' ");
            }
            rsTmp.first();

            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {
                Counter = Counter + 1;
                clsTravelEntryItem ObjMRItems = new clsTravelEntryItem();
                ObjMRItems.setAttribute("TSD_SR_NO", rsTmp.getString("TSD_SR_NO"));
                ObjMRItems.setAttribute("TSD_START_PLACE", rsTmp.getString("TSD_START_PLACE"));
                ObjMRItems.setAttribute("TSD_START_DATE", rsTmp.getString("TSD_START_DATE"));
                ObjMRItems.setAttribute("TSD_START_TIME", rsTmp.getString("TSD_START_TIME"));
                ObjMRItems.setAttribute("TSD_TRAVEL_MODE", rsTmp.getString("TSD_TRAVEL_MODE"));
                ObjMRItems.setAttribute("TSD_FARE", rsTmp.getString("TSD_FARE"));
                ObjMRItems.setAttribute("TSD_VISIT_PLACE", rsTmp.getString("TSD_VISIT_PLACE"));
                ObjMRItems.setAttribute("TSD_IS_VISIT_PLACE", rsTmp.getString("TSD_IS_VISIT_PLACE"));
                ObjMRItems.setAttribute("TSD_ARRIVAL_DATE", rsTmp.getString("TSD_ARRIVAL_DATE"));
                ObjMRItems.setAttribute("TSD_ARRIVAL_TIME", rsTmp.getString("TSD_ARRIVAL_TIME"));
                ObjMRItems.setAttribute("TSD_SEMINAR", rsTmp.getString("TSD_SEMINAR"));
                ObjMRItems.setAttribute("TSD_DAYS", rsTmp.getString("TSD_DAYS"));
                ObjMRItems.setAttribute("TSD_RATE", rsTmp.getString("TSD_RATE"));
                ObjMRItems.setAttribute("TSD_AMOUNT", rsTmp.getString("TSD_AMOUNT"));
                ObjMRItems.setAttribute("TSD_INFO", rsTmp.getString("TSD_INFO"));
                ObjMRItems.setAttribute("TSD_REMARK", rsTmp.getString("TSD_REMARK"));
                ObjMRItems.setAttribute("TSD_PURPOSE_OF_VISIT", rsTmp.getString("TSD_PURPOSE_OF_VISIT"));
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND WHERE TSD_DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=859 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND STATUS='W'";
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
            String strSQL = "SELECT TSD_DOC_NO,TSD_DOC_DATE,REVISION_NO,UPDATED_BY,APPROVAL_STATUS,DATE_FORMAT(ENTRY_DATE,'%d/%m/%Y %H:%i:%S') AS ENTRY_DATE,REJECTED_REMARKS AS APPROVER_REMARKS,FROM_IP FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND_H WHERE TSD_DOC_NO='" + DocNo + "' GROUP BY TSD_DOC_NO,REVISION_NO";
            rsTmp = data.getResult(strSQL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                while (!rsTmp.isAfterLast()) {
                    clsTravelEntryAmend objParty = new clsTravelEntryAmend();

                    objParty.setAttribute("TSD_DOC_NO", rsTmp.getString("TSD_DOC_NO"));
                    objParty.setAttribute("TSD_DOC_DATE", rsTmp.getString("TSD_DOC_DATE"));
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
            strSQL += "SELECT * FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND WHERE " + Condition + "  GROUP BY TSD_DOC_NO";
            System.out.println("Filter Query :" + strSQL);
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery(strSQL);

            if (!rsResultSet.first()) {
                strSQL = "SELECT * FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND   GROUP BY TSD_DOC_NO";
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND WHERE TSD_DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=859 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND TYPE='C' AND STATUS='W'";
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
                strSQL = "SELECT SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND.TSD_DOC_NO,SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND.TSD_DOC_DATE,RECEIVED_DATE FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND,SDMLATTPAY.D_COM_DOC_DATA WHERE SDMLATTPAY.D_COM_DOC_DATA.DOC_NO=SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND.TSD_DOC_NO AND SDMLATTPAY.D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND SDMLATTPAY.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=859 GROUP BY SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND.TSD_DOC_NO ORDER BY SDMLATTPAY.D_COM_DOC_DATA.RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND.TSD_DOC_NO,SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND.TSD_DOC_DATE,RECEIVED_DATE FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND,SDMLATTPAY.D_COM_DOC_DATA WHERE SDMLATTPAY.D_COM_DOC_DATA.DOC_NO=SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND.TSD_DOC_NO AND SDMLATTPAY.D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND SDMLATTPAY.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=859 GROUP BY SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND.TSD_DOC_NO  ORDER BY SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND.TSD_DOC_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND.TSD_DOC_NO,SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND.TSD_DOC_DATE,RECEIVED_DATE FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND,SDMLATTPAY.D_COM_DOC_DATA WHERE SDMLATTPAY.D_COM_DOC_DATA.DOC_NO=SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND.TSD_DOC_NO AND SDMLATTPAY.D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND SDMLATTPAY.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=859 GROUP BY SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND.TSD_DOC_NO  ORDER BY SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND.TSD_DOC_NO";
            }

            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {

                Counter = Counter + 1;
                clsTravelEntryAmend ObjItem = new clsTravelEntryAmend();

                //------------- Header Fields --------------------//
                ObjItem.setAttribute("DOC_NO", rsTmp.getString("TSD_DOC_NO"));
                ObjItem.setAttribute("DOC_DATE", rsTmp.getString("TSD_DOC_DATE"));
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
            rsResultSet = Stmt.executeQuery("SELECT * FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND_H WHERE TSD_DOC_NO='" + pDocNo + "' GROUP BY TSD_DOC_NO ORDER BY REVISION_NO ");
            Ready = true;
            HistoryView = true;
            MoveFirst();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    private void UpdateLeave() {

        String sql;
        sql = "DELETE FROM SDMLATTPAY.ATT_LEAVE_TRN WHERE LVT_DOC_NO LIKE '" + getAttribute("TSD_DOC_NO").getString().substring(0, 12) + "%'";
        System.out.println("Sql:" + sql);
        data.Execute(sql);
        int sh = 0, fh = 0, fd = 0, ld = 0;
        if (getAttribute("TSD_CATEGORY").getObj().toString().equalsIgnoreCase("Within Gujarat [One Day Trip]")) {
            sql = "INSERT INTO SDMLATTPAY.ATT_LEAVE_TRN "
                    + "(LVT_YEAR,LVT_EMPID,LVT_PAY_EMPID,LVT_LEAVE_CODE,LVT_LEAVE_TYPE,LVT_DAYS,LVT_FROMDATE,LVT_TODATE,LVT_REMARK,"
                    + "LVT_MENTION_TIME,"
                    + "APPROVED,APPROVED_DATE,"
                    + "LVT_DOC_NO,LVT_DOC_DATE) "
                    + "SELECT YEAR(TSD_START_DATE),RIGHT(TSD_PAY_EMP_NO,6),TSD_PAY_EMP_NO,'OD',3,1,";
            sql = sql + "MIN(TSD_START_DATE),";
            sql = sql + "MAX(TSD_ARRIVAL_DATE),";
            sql = sql + "'ON DUTY','',APPROVED,APPROVED_DATE,TSD_DOC_NO,TSD_DOC_DATE FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND "
                    + "WHERE TSD_INFO='CURRENT' AND TSD_DOC_NO='" + (String) getAttribute("TSD_DOC_NO").getObj() + "' "
                    + "GROUP BY TSD_DOC_NO";
            System.out.println("Sql:" + sql);
            data.Execute(sql);
        } else {
            sh = data.getIntValueFromDB("SELECT COUNT(*) FROM ("
                    + "SELECT TSD_START_DATE,TSD_START_TIME,TSD_PAY_EMP_NO,EMP_SHIFT_ID,TSD_INFO FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND "
                    + "LEFT JOIN SDMLATTPAY.ATTPAY_EMPMST "
                    + "ON PAY_EMP_NO=TSD_PAY_EMP_NO "
                    + "WHERE TSD_DOC_NO='" + (String) getAttribute("TSD_DOC_NO").getObj() + "' AND TSD_INFO='CURRENT' AND TSD_SR_NO=1 ) AS DD "
                    + "LEFT JOIN SDMLATTPAY.ATT_SHIFT S ON S.SHIFT_ID=EMP_SHIFT_ID "
                    + " WHERE  TSD_START_TIME>=TIME(ADDTIME(CONCAT('2021-01-01 ',SHIFT_IN_TIME,':00'),\"2:00:00\"))  "
                    + " AND TSD_START_TIME<=TIME(ADDTIME(CONCAT('2021-01-01 ',SHIFT_OUT_TIME,':00'),\"-2:00:00\")) ");
            fh = data.getIntValueFromDB("SELECT COUNT(*) FROM ("
                    + "SELECT TSD_PAY_EMP_NO,TSD_ARRIVAL_TIME FROM (SELECT * FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND "
                    + "WHERE TSD_DOC_NO='" + (String) getAttribute("TSD_DOC_NO").getObj() + "' AND TSD_INFO='CURRENT' "
                    + "ORDER BY TSD_SR_NO DESC) AS D "
                    + "GROUP BY TSD_DOC_NO) AS DD "
                    + "LEFT JOIN SDMLATTPAY.ATTPAY_EMPMST  "
                    + "ON PAY_EMP_NO=TSD_PAY_EMP_NO  "
                    + "LEFT JOIN SDMLATTPAY.ATT_SHIFT S ON S.SHIFT_ID=EMP_SHIFT_ID "
                    + "WHERE TSD_ARRIVAL_TIME>=TIME(ADDTIME(CONCAT('2021-01-01 ',SHIFT_IN_TIME,':00'),\"2:00:00\"))  "
                    + " AND TSD_ARRIVAL_TIME<=TIME(ADDTIME(CONCAT('2021-01-01 ',SHIFT_OUT_TIME,':00'),\"-2:00:00\"))");
            fd = data.getIntValueFromDB("SELECT COUNT(*) FROM ("
                    + "SELECT TSD_START_DATE,TSD_START_TIME,TSD_PAY_EMP_NO,EMP_SHIFT_ID FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL "
                    + "LEFT JOIN SDMLATTPAY.ATTPAY_EMPMST "
                    + "ON PAY_EMP_NO=TSD_PAY_EMP_NO "
                    + "WHERE TSD_DOC_NO='" + (String) getAttribute("TSD_DOC_NO").getObj() + "' AND TSD_SR_NO=1 AND TSD_INFO='CURRENT') AS DD "
                    + "LEFT JOIN SDMLATTPAY.ATT_SHIFT S ON S.SHIFT_ID=EMP_SHIFT_ID "
                    + " WHERE TSD_START_TIME>=TIME(ADDTIME(CONCAT('2021-01-01 ',SHIFT_OUT_TIME,':00'),\"-2:00:00\"))");
            ld = data.getIntValueFromDB("SELECT COUNT(*) FROM ("
                    + "SELECT TSD_PAY_EMP_NO,TSD_ARRIVAL_TIME FROM (SELECT * FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND "
                    + "WHERE TSD_DOC_NO='" + (String) getAttribute("TSD_DOC_NO").getObj() + "' AND TSD_INFO='CURRENT' "
                    + "ORDER BY TSD_SR_NO DESC) AS D "
                    + "GROUP BY TSD_DOC_NO) AS DD "
                    + "LEFT JOIN SDMLATTPAY.ATTPAY_EMPMST  "
                    + "ON PAY_EMP_NO=TSD_PAY_EMP_NO  "
                    + "LEFT JOIN SDMLATTPAY.ATT_SHIFT S ON S.SHIFT_ID=EMP_SHIFT_ID "
                    + "WHERE TSD_ARRIVAL_TIME<TIME(ADDTIME(CONCAT('2021-01-01 ',SHIFT_IN_TIME,':00'),\"2:00:00\"))");
            if (sh > 0) {
                sql = "INSERT INTO SDMLATTPAY.ATT_LEAVE_TRN "
                        + "(LVT_YEAR,LVT_EMPID,LVT_PAY_EMPID,LVT_LEAVE_CODE,LVT_LEAVE_TYPE,LVT_DAYS,LVT_FROMDATE,LVT_TODATE,LVT_REMARK,"
                        + "LVT_MENTION_TIME,"
                        + "APPROVED,APPROVED_DATE,"
                        + "LVT_DOC_NO,LVT_DOC_DATE) "
                        + "SELECT YEAR(TSD_START_DATE),RIGHT(TSD_PAY_EMP_NO,6),TSD_PAY_EMP_NO,'OD',3,0.50,TSD_START_DATE,TSD_START_DATE,'ON DUTY',"
                        + "'2ND HALF',APPROVED,APPROVED_DATE,TSD_DOC_NO,TSD_DOC_DATE FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND "
                        + "WHERE TSD_INFO='CURRENT' AND TSD_DOC_NO='" + (String) getAttribute("TSD_DOC_NO").getObj() + "' AND TSD_SR_NO=1";
                System.out.println("SH:" + sql);
                data.Execute(sql);
            }
            if (fh > 0) {
                sql = "INSERT INTO SDMLATTPAY.ATT_LEAVE_TRN "
                        + "(LVT_YEAR,LVT_EMPID,LVT_PAY_EMPID,LVT_LEAVE_CODE,LVT_LEAVE_TYPE,LVT_DAYS,LVT_FROMDATE,LVT_TODATE,LVT_REMARK,"
                        + "LVT_MENTION_TIME,"
                        + "APPROVED,APPROVED_DATE,"
                        + "LVT_DOC_NO,LVT_DOC_DATE) "
                        + "SELECT * FROM (SELECT YEAR(TSD_ARRIVAL_DATE),RIGHT(TSD_PAY_EMP_NO,6),TSD_PAY_EMP_NO,'OD',3,0.50,TSD_ARRIVAL_DATE AS FD,TSD_ARRIVAL_DATE AS TD,'ON DUTY',"
                        + "'1ST HALF',APPROVED,APPROVED_DATE,TSD_DOC_NO,TSD_DOC_DATE FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND "
                        + "WHERE TSD_INFO='CURRENT' AND TSD_DOC_NO='" + (String) getAttribute("TSD_DOC_NO").getObj() + "' "
                        + "ORDER BY TSD_SR_NO DESC) AS DD "
                        + "GROUP BY TSD_DOC_NO";
                System.out.println("FH:" + sql);
                data.Execute(sql);
            }
            sql = "INSERT INTO SDMLATTPAY.ATT_LEAVE_TRN "
                    + "(LVT_YEAR,LVT_EMPID,LVT_PAY_EMPID,LVT_LEAVE_CODE,LVT_LEAVE_TYPE,LVT_DAYS,LVT_FROMDATE,LVT_TODATE,LVT_REMARK,"
                    + "LVT_MENTION_TIME,"
                    + "APPROVED,APPROVED_DATE,"
                    + "LVT_DOC_NO,LVT_DOC_DATE) "
                    + "SELECT YEAR(TSD_START_DATE),RIGHT(TSD_PAY_EMP_NO,6),TSD_PAY_EMP_NO,'OD',3,0,";
            if (sh > 0 || fd > 0) {
                sql = sql + "DATE_ADD(MIN(TSD_START_DATE),INTERVAL 1 DAY),";
            } else {
                sql = sql + "MIN(TSD_START_DATE),";
            }
            if (fh > 0 || ld > 0) {
                sql = sql + "DATE_SUB(MAX(TSD_ARRIVAL_DATE),INTERVAL 1 DAY),";
            } else {
                sql = sql + "MAX(TSD_ARRIVAL_DATE),";
            }
            sql = sql + "'ON DUTY','',APPROVED,APPROVED_DATE,TSD_DOC_NO,TSD_DOC_DATE FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND "
                    + "WHERE TSD_INFO='CURRENT' AND TSD_DOC_NO='" + (String) getAttribute("TSD_DOC_NO").getObj() + "' "
                    + "GROUP BY TSD_DOC_NO";
            System.out.println("Sql:" + sql);
            data.Execute(sql);
        }
        sql = "UPDATE SDMLATTPAY.ATT_LEAVE_TRN SET LVT_DAYS=DATEDIFF(LVT_TODATE,LVT_FROMDATE)+1 "
                + "WHERE LVT_DOC_NO='" + (String) getAttribute("TSD_DOC_NO").getObj() + "' "
                + "AND LVT_MENTION_TIME=''";
        System.out.println("Sql:" + sql);
        data.Execute(sql);
        sql = "DELETE FROM SDMLATTPAY.ATT_LEAVE_TRN WHERE LVT_DAYS=0 AND LVT_DOC_NO='" + (String) getAttribute("TSD_DOC_NO").getObj() + "' "
                + "AND LVT_MENTION_TIME=''";
        System.out.println("Sql:" + sql);
        data.Execute(sql);

        sql = "DELETE FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL WHERE TSD_DOC_NO LIKE '" + getAttribute("TSD_DOC_NO").getString().substring(0, 12) + "%'";
        System.out.println("Sql:" + sql);
        data.Execute(sql);

        sql = "INSERT INTO SDMLATTPAY.TRAVEL_SANCTION_DETAIL "
                + "(TSD_DOC_NO, TSD_DOC_DATE, TSD_PAY_EMP_NO, TSD_DEPARTMENT, TSD_DESIGNATION, TSD_LEVEL, TSD_NAME, TSD_CATEGORY, "
                + "TSD_PURPOSE_OF_VISIT, TSD_AUDIT_REMARK, TSD_SR_NO, TSD_START_PLACE, TSD_START_DATE, TSD_START_TIME, TSD_TRAVEL_MODE, "
                + "TSD_FARE, TSD_VISIT_PLACE, TSD_IS_VISIT_PLACE, TSD_ARRIVAL_DATE, TSD_ARRIVAL_TIME, TSD_SEMINAR,TSD_DAYS,TSD_RATE,TSD_AMOUNT, TSD_TOTAL_FARE, TSD_TOTAL_EXPENSE, "
                + "TSD_TOTAL, TSD_ADVANCE, "
                + "CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, APPROVED, APPROVED_DATE, REJECTED, REJECTED_DATE, CANCELED, "
                + "HIERARCHY_ID, CHANGED, CHANGED_DATE, REJECTED_REMARKS, FROM_IP,TSD_REMARK,TSD_INFO) "
                + "SELECT TSD_DOC_NO, TSD_DOC_DATE, TSD_PAY_EMP_NO, TSD_DEPARTMENT, TSD_DESIGNATION, TSD_LEVEL, TSD_NAME, TSD_CATEGORY, "
                + "TSD_PURPOSE_OF_VISIT, TSD_AUDIT_REMARK, TSD_SR_NO, TSD_START_PLACE, TSD_START_DATE, TSD_START_TIME, TSD_TRAVEL_MODE, "
                + "TSD_FARE, TSD_VISIT_PLACE, TSD_IS_VISIT_PLACE, TSD_ARRIVAL_DATE, TSD_ARRIVAL_TIME, TSD_SEMINAR,TSD_DAYS,TSD_RATE,TSD_AMOUNT, TSD_TOTAL_FARE, TSD_TOTAL_EXPENSE, "
                + "TSD_TOTAL, TSD_ADVANCE, "
                + "CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, APPROVED, APPROVED_DATE, REJECTED, REJECTED_DATE, CANCELED, "
                + "HIERARCHY_ID, CHANGED, CHANGED_DATE, REJECTED_REMARKS, FROM_IP,TSD_REMARK,TSD_INFO "
                + "FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND "
                + "WHERE TSD_INFO!='PREVIOUS' AND TSD_DOC_NO='" + (String) getAttribute("TSD_DOC_NO").getObj() + "' ";

        System.out.println("Sql:" + sql);
        data.Execute(sql);

        sql = "UPDATE SDMLATTPAY.TRAVEL_SANCTION_DETAIL D,SDMLATTPAY.TRAVEL_SANCTION_DETAIL_H H "
                + "SET D.TSD_ADVANCE=H.TSD_ADVANCE,D.TSD_DOC_NO=H.TSD_DOC_NO,D.TSD_DOC_DATE=H.TSD_DOC_DATE,D.MODIFIED_BY=H.MODIFIED_BY,D.MODIFIED_DATE=H.MODIFIED_DATE,"
                + "D.APPROVED=1,D.APPROVED_DATE=H.APPROVED_DATE,D.HIERARCHY_ID=H.HIERARCHY_ID,D.REJECTED_REMARKS=H.REJECTED_REMARKS,"
                + "D.FROM_IP=H.FROM_IP WHERE LEFT(D.TSD_DOC_NO,12)=H.TSD_DOC_NO AND H.APPROVAL_STATUS='F' ";
        System.out.println("Sql:" + sql);
        data.Execute(sql);

    }
    public static void CancelDoc(String pDocNo) {
        ResultSet rsTmp = null;

        if (CanCancel(pDocNo)) {

            boolean Approved = false;

            try {
                rsTmp = data.getResult("SELECT APPROVED FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND WHERE TSD_DOC_NO='" + pDocNo + "' ");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    Approved = rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();

                if (!Approved) {
                    data.Execute("DELETE FROM SDMLATTPAY.D_COM_DOC_DATA WHERE DOC_NO='" + pDocNo + "' AND MODULE_ID="+ModuleID);
                }
                data.Execute("UPDATE SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE TSD_DOC_NO='" + pDocNo + "'");

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
            System.out.println("SELECT TSD_DOC_NO FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND WHERE TSD_DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");
            rsTmp = stTmp.executeQuery("SELECT TSD_DOC_NO FROM SDMLATTPAY.TRAVEL_SANCTION_DETAIL_AMEND WHERE TSD_DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");

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
