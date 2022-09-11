/*
 * clsSales_Party.java
 *
 * Created on April 3, 2009, 10:22 AM
 */
package SDMLATTPAY.gatepass;

import SDMLATTPAY.Shift.*;
import EITLERP.*;
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
 * @author ashutosh
 */
public class clsGatepass {

    public String LastError = "";
    private ResultSet rsResultSet, rsResultSet1;
    private Connection Conn;
    private Statement Stmt;
    DecimalFormat df = new DecimalFormat("##.##");
    private HashMap props;
    public boolean Ready = false;

    private boolean HistoryView = false;
    public static int ModuleID = 805; //
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
    public clsGatepass() {
        LastError = "";
        props = new HashMap();
        //props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("GP_DOC_NO", new Variant(""));
        props.put("GP_DOC_DATE",new Variant(""));
        props.put("GP_DATE", new Variant(""));
        props.put("GP_EMP_NO", new Variant(""));
        props.put("GP_EMP_NAME", new Variant(""));
        props.put("GP_DEPT", new Variant(""));
        props.put("GP_SHIFT", new Variant(""));
        props.put("GP_DESN", new Variant(""));        
        props.put("GP_TYPE", new Variant(""));
        props.put("GP_TOL", new Variant(""));
        props.put("GP_TOA", new Variant(""));
        props.put("GP_TOT_HOURS", new Variant(""));
        props.put("GP_REMARKS", new Variant(""));
        props.put("GP_NATURE_OF_WORK", new Variant(""));
        props.put("GP_DEPT_HEAD", new Variant(""));
        
        props.put("CREATED_BY", new Variant(""));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(""));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("APPROVED", new Variant(""));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED", new Variant(""));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("CANCELED", new Variant(""));
        props.put("CANCELED_DATE", new Variant(""));
        props.put("CHANGED", new Variant(""));
        props.put("CHANGED_DATE", new Variant(""));
        props.put("FROM_IP", new Variant(""));
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
            rsResultSet = Stmt.executeQuery("SELECT * FROM SDMLATTPAY.GATEPASS_ENTRY ORDER BY GP_DOC_DATE,GP_DOC_NO ");
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
            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");

            // ---- History Related Changes ------ //
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHDetail = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rsHistory = stHistory.executeQuery("SELECT * FROM SDMLATTPAY.GATEPASS_ENTRY_H WHERE GP_DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();

            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateString("GP_DOC_NO", getAttribute("GP_DOC_NO").getString());
            rsResultSet.updateString("GP_DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("GP_DOC_DATE").getString()));
            rsResultSet.updateString("GP_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("GP_DATE").getString()));
            rsResultSet.updateString("GP_EMP_NO", getAttribute("GP_EMP_NO").getString());
            rsResultSet.updateString("GP_EMP_NAME", getAttribute("GP_EMP_NAME").getString());
            rsResultSet.updateString("GP_EMP_DEPT", getAttribute("GP_EMP_DEPT").getString());
            rsResultSet.updateString("GP_EMP_SHIFT", getAttribute("GP_EMP_SHIFT").getString());
            rsResultSet.updateString("GP_EMP_DESN", getAttribute("GP_EMP_DESN").getString());
            rsResultSet.updateString("GP_TYPE", getAttribute("GP_TYPE").getString());
            rsResultSet.updateString("GP_TOL", EITLERPGLOBAL.formatDateTimeDB(getAttribute("GP_TOL").getString()));
            rsResultSet.updateString("GP_TOA", EITLERPGLOBAL.formatDateTimeDB(getAttribute("GP_TOA").getString()));
            rsResultSet.updateString("GP_REMARKS", getAttribute("GP_REMARKS").getString());
            rsResultSet.updateString("GP_NATURE_OF_WORK", getAttribute("GP_NATURE_OF_WORK").getString());
            rsResultSet.updateString("GP_DEPT_HEAD", getAttribute("GP_DEPT_HEAD").getString());
            rsResultSet.updateString("GP_TOT_HOURS", getAttribute("GP_TOT_HOURS").getString());
            
            
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
            
            rsResultSet.updateString("GP_TIME_OF_LEAVING", EITLERPGLOBAL.formatDateTimeDB(getAttribute("GP_TOL").getString()));
            rsResultSet.updateString("GP_TIME_OF_ARRIVAL", EITLERPGLOBAL.formatDateTimeDB(getAttribute("GP_TOA").getString()));
            
            rsResultSet.insertRow();

            //========= Inserting Into History =================//
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM SDMLATTPAY.GATEPASS_ENTRY_H WHERE GP_DOC_NO='" + (String) getAttribute("GP_DOC_NO").getObj() + "'");
            RevNo++;
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO", 1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY", (int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            rsHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
            rsHistory.updateString("GP_DOC_NO", getAttribute("GP_DOC_NO").getString());
            rsHistory.updateString("GP_DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("GP_DOC_DATE").getString()));
            rsHistory.updateString("GP_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("GP_DATE").getString()));
            rsHistory.updateString("GP_EMP_NO", getAttribute("GP_EMP_NO").getString());
            rsHistory.updateString("GP_EMP_NAME", getAttribute("GP_EMP_NAME").getString());
            rsHistory.updateString("GP_EMP_DEPT", getAttribute("GP_EMP_DEPT").getString());            
            rsHistory.updateString("GP_EMP_SHIFT", getAttribute("GP_EMP_SHIFT").getString());
            rsHistory.updateString("GP_EMP_DESN", getAttribute("GP_EMP_DESN").getString());            
            rsHistory.updateString("GP_TYPE", getAttribute("GP_TYPE").getString());
            rsHistory.updateString("GP_TOL", EITLERPGLOBAL.formatDateTimeDB(getAttribute("GP_TOL").getString()));
            rsHistory.updateString("GP_TOA", EITLERPGLOBAL.formatDateTimeDB(getAttribute("GP_TOA").getString()));
            rsHistory.updateString("GP_REMARKS", getAttribute("GP_REMARKS").getString());
            rsHistory.updateString("GP_NATURE_OF_WORK", getAttribute("GP_NATURE_OF_WORK").getString());
            rsHistory.updateString("GP_DEPT_HEAD", getAttribute("GP_DEPT_HEAD").getString());
            rsHistory.updateString("GP_TOT_HOURS", getAttribute("GP_TOT_HOURS").getString());
            
            rsHistory.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
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

            //===================== Update the Approval Flow ======================//
            setAttribute("FROM", EITLERPGLOBAL.gUserID);

            //ApprovalFlow ObjFlow=new ApprovalFlow();
            SDMLATTPAY.ApprovalFlow ObjFlow = new SDMLATTPAY.ApprovalFlow();

            ObjFlow.ModuleID = clsGatepass.ModuleID;
            ObjFlow.DocNo = (String) getAttribute("GP_DOC_NO").getObj();
            //ObjFlow.DocDate = (String) getAttribute("GP_DOC_DATE").getObj();
            ObjFlow.DocDate = EITLERPGLOBAL.formatDateDB(getAttribute("GP_DOC_DATE").getString());
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "SDMLATTPAY.GATEPASS_ENTRY";
            ObjFlow.IsCreator = true;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "GP_DOC_NO";
            ObjFlow.CompanyID = EITLERPGLOBAL.gCompanyID;

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

        try {
            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");
            String AStatus = (String) getAttribute("APPROVAL_STATUS").getObj();
            if (AStatus.equals("R")) {
                Validate = false;
            }
            Validate = true;
            // ---- History Related Changes ------ //
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHDetail = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rsHistory = stHistory.executeQuery("SELECT * FROM SDMLATTPAY.GATEPASS_ENTRY_H WHERE GP_DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//

            String theDocNo = getAttribute("GP_DOC_NO").getString();

            rsResultSet.updateString("GP_DOC_NO", getAttribute("GP_DOC_NO").getString());
            rsResultSet.updateString("GP_DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("GP_DOC_DATE").getString()));
            rsResultSet.updateString("GP_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("GP_DATE").getString()));
            rsResultSet.updateString("GP_EMP_NO", getAttribute("GP_EMP_NO").getString());
            rsResultSet.updateString("GP_EMP_NAME", getAttribute("GP_EMP_NAME").getString());
            rsResultSet.updateString("GP_EMP_DEPT", getAttribute("GP_EMP_DEPT").getString());
            rsResultSet.updateString("GP_EMP_SHIFT", getAttribute("GP_EMP_SHIFT").getString());
            rsResultSet.updateString("GP_EMP_DESN", getAttribute("GP_EMP_DESN").getString());
            rsResultSet.updateString("GP_TYPE", getAttribute("GP_TYPE").getString());
            rsResultSet.updateString("GP_TOL", EITLERPGLOBAL.formatDateTimeDB(getAttribute("GP_TOL").getString()));
            rsResultSet.updateString("GP_TOA", EITLERPGLOBAL.formatDateTimeDB(getAttribute("GP_TOA").getString()));
            rsResultSet.updateString("GP_REMARKS", getAttribute("GP_REMARKS").getString());
            rsResultSet.updateString("GP_NATURE_OF_WORK", getAttribute("GP_NATURE_OF_WORK").getString());
            rsResultSet.updateString("GP_DEPT_HEAD", getAttribute("GP_DEPT_HEAD").getString());
            rsResultSet.updateString("GP_TOT_HOURS", getAttribute("GP_TOT_HOURS").getString());
            
            rsResultSet.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
            rsResultSet.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
            
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
            
            
            rsResultSet.updateString("GP_TIME_OF_LEAVING", EITLERPGLOBAL.formatDateTimeDB(getAttribute("GP_TOL").getString()));
            rsResultSet.updateString("GP_TIME_OF_ARRIVAL", EITLERPGLOBAL.formatDateTimeDB(getAttribute("GP_TOA").getString()));
            
            rsResultSet.updateRow();

            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM SDMLATTPAY.GATEPASS_ENTRY_H WHERE GP_DOC_NO='" + (String) getAttribute("GP_DOC_NO").getObj() + "'");
            RevNo++;
            String RevDocNo = (String) getAttribute("GP_DOC_NO").getObj();

            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO", RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY", (int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            rsHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
            rsHistory.updateString("GP_DOC_NO", getAttribute("GP_DOC_NO").getString());
            rsHistory.updateString("GP_DOC_DATE", getAttribute("GP_DOC_DATE").getString());
            rsHistory.updateString("GP_DATE", getAttribute("GP_DATE").getString());
            rsHistory.updateString("GP_EMP_NO", getAttribute("GP_EMP_NO").getString());
            rsHistory.updateString("GP_EMP_NAME", getAttribute("GP_EMP_NAME").getString());
            rsHistory.updateString("GP_EMP_DEPT", getAttribute("GP_EMP_DEPT").getString());            
            rsHistory.updateString("GP_EMP_SHIFT", getAttribute("GP_EMP_SHIFT").getString());
            rsHistory.updateString("GP_EMP_DESN", getAttribute("GP_EMP_DESN").getString());            
            rsHistory.updateString("GP_TYPE", getAttribute("GP_TYPE").getString());
            rsHistory.updateString("GP_TOL", EITLERPGLOBAL.formatDateTimeDB(getAttribute("GP_TOL").getString()));
            rsHistory.updateString("GP_TOA", EITLERPGLOBAL.formatDateTimeDB(getAttribute("GP_TOA").getString()));
            rsHistory.updateString("GP_REMARKS", getAttribute("GP_REMARKS").getString());
            rsHistory.updateString("GP_NATURE_OF_WORK", getAttribute("GP_NATURE_OF_WORK").getString());
            rsHistory.updateString("GP_TOT_HOURS", getAttribute("GP_TOT_HOURS").getString());
            
            rsHistory.updateInt("MODIFIED_BY", getAttribute("MODIFIED_BY").getInt());
            rsHistory.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
            rsHistory.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
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

            //======== Update the Approval Flow =========
            setAttribute("FROM", EITLERPGLOBAL.gUserID);

            //ApprovalFlow ObjFlow=new ApprovalFlow();
            SDMLATTPAY.ApprovalFlow ObjFlow = new SDMLATTPAY.ApprovalFlow();

            ObjFlow.ModuleID = clsGatepass.ModuleID;
            ObjFlow.DocNo = (String) getAttribute("GP_DOC_NO").getObj();
            //ObjFlow.DocDate = (String) getAttribute("GP_DOC_DATE").getObj();
            ObjFlow.DocDate = EITLERPGLOBAL.formatDateDB(getAttribute("GP_DOC_DATE").getString());
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "SDMLATTPAY.GATEPASS_ENTRY";
            ObjFlow.IsCreator = false;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "GP_DOC_NO";
            ObjFlow.CompanyID = EITLERPGLOBAL.gCompanyID;

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
                data.Execute("UPDATE SDMLATTPAY.GATEPASS_ENTRY SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE GP_DOC_NO='" + ObjFlow.DocNo + "' ");

                //Remove Old Records from FELT_DOC_DATA
                data.Execute("DELETE FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=" + clsGatepass.ModuleID + " AND DOC_NO='" + ObjFlow.DocNo + "'");

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
            //--------- Approval Flow Update complete -----------
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

            setAttribute("GP_DOC_NO", rsResultSet.getString("GP_DOC_NO"));
            setAttribute("GP_DOC_DATE", rsResultSet.getString("GP_DOC_DATE"));
            
            setAttribute("GP_DATE", EITLERPGLOBAL.formatDate(rsResultSet.getString("GP_DATE")));
            setAttribute("GP_EMP_NO", rsResultSet.getString("GP_EMP_NO"));
            setAttribute("GP_EMP_NAME", rsResultSet.getString("GP_EMP_NAME"));
            setAttribute("GP_EMP_DEPT", rsResultSet.getString("GP_EMP_DEPT"));
            setAttribute("GP_EMP_SHIFT", rsResultSet.getString("GP_EMP_SHIFT"));
            setAttribute("GP_EMP_DESN", rsResultSet.getString("GP_EMP_DESN"));
            setAttribute("GP_TYPE", rsResultSet.getString("GP_TYPE"));
            setAttribute("GP_TOL", rsResultSet.getString("GP_TOL"));
            setAttribute("GP_TOA", rsResultSet.getString("GP_TOA"));
            setAttribute("GP_REMARKS", rsResultSet.getString("GP_REMARKS"));
            setAttribute("GP_NATURE_OF_WORK", rsResultSet.getString("GP_NATURE_OF_WORK"));
            setAttribute("GP_DEPT_HEAD", rsResultSet.getString("GP_DEPT_HEAD"));
            setAttribute("GP_TOT_HOURS", rsResultSet.getString("GP_TOT_HOURS"));
            
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.GATEPASS_ENTRY WHERE GP_DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=805 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND STATUS='W'";
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
            String strSQL = "SELECT * FROM SDMLATTPAY.GATEPASS_ENTRY_H WHERE GP_DOC_NO='" + DocNo + "' ";
            rsTmp = data.getResult(strSQL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                while (!rsTmp.isAfterLast()) {
                    clsGatepass objParty = new clsGatepass();

                    objParty.setAttribute("GP_DOC_NO", rsTmp.getString("GP_DOC_NO"));
                    objParty.setAttribute("GP_DOC_DATE", rsTmp.getString("GP_DOC_DATE"));
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
            strSQL += "SELECT * FROM SDMLATTPAY.GATEPASS_ENTRY WHERE " + Condition + " ";
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery(strSQL);

            if (!rsResultSet.first()) {
                strSQL = "SELECT * FROM SDMLATTPAY.GATEPASS_ENTRY WHERE GP_DOC_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND GP_DOC_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'     ORDER BY GP_DOC_NO";
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.GATEPASS_ENTRY WHERE GP_DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=805 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND TYPE='C' AND STATUS='W'";
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
            String lDocNo = (String) getAttribute("GP_DOC_NO").getObj();
            String strSQL = "";

            //First check that record is editable
            if (CanDelete(lCompanyID, lDocNo, pUserID)) {
                String strQry = "DELETE FROM SDMLATTPAY.GATEPASS_ENTRY WHERE GP_DOC_NO='" + lDocNo + "'";
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
                strSQL = "SELECT DISTINCT SDMLATTPAY.GATEPASS_ENTRY.GP_DOC_NO,SDMLATTPAY.GATEPASS_ENTRY.GP_DOC_DATE,RECEIVED_DATE,GP_TYPE,GP_EMP_NO FROM SDMLATTPAY.GATEPASS_ENTRY,SDMLATTPAY.D_COM_DOC_DATA WHERE SDMLATTPAY.D_COM_DOC_DATA.DOC_NO=SDMLATTPAY.GATEPASS_ENTRY.GP_DOC_NO AND SDMLATTPAY.D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND SDMLATTPAY.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=805 ORDER BY SDMLATTPAY.D_COM_DOC_DATA.RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT SDMLATTPAY.GATEPASS_ENTRY.GP_DOC_NO,SDMLATTPAY.GATEPASS_ENTRY.GP_DOC_DATE,RECEIVED_DATE,GP_TYPE,GP_EMP_NO FROM SDMLATTPAY.GATEPASS_ENTRY,SDMLATTPAY.D_COM_DOC_DATA WHERE SDMLATTPAY.D_COM_DOC_DATA.DOC_NO=SDMLATTPAY.GATEPASS_ENTRY.GP_DOC_NO AND SDMLATTPAY.D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND SDMLATTPAY.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=805 ORDER BY SDMLATTPAY.GATEPASS_ENTRY.GP_DOC_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT SDMLATTPAY.GATEPASS_ENTRY.GP_DOC_NO,SDMLATTPAY.GATEPASS_ENTRY.GP_DOC_DATE,RECEIVED_DATE,GP_TYPE,GP_EMP_NO FROM SDMLATTPAY.GATEPASS_ENTRY,SDMLATTPAY.D_COM_DOC_DATA WHERE SDMLATTPAY.D_COM_DOC_DATA.DOC_NO=SDMLATTPAY.GATEPASS_ENTRY.GP_DOC_NO AND SDMLATTPAY.D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND SDMLATTPAY.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=805 ORDER BY SDMLATTPAY.GATEPASS_ENTRY.GP_DOC_NO";
            }
            //System.out.println(strSQL);
            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {

                Counter = Counter + 1;
                clsGatepass ObjItem = new clsGatepass();

                //------------- Header Fields --------------------//
                ObjItem.setAttribute("DOC_NO", rsTmp.getString("GP_DOC_NO"));
                ObjItem.setAttribute("DOC_DATE", rsTmp.getString("GP_DOC_DATE"));
                ObjItem.setAttribute("GP_EMP_NO", rsTmp.getString("GP_EMP_NO"));
                ObjItem.setAttribute("GP_TYPE",rsTmp.getString("GP_TYPE"));                
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
            rsResultSet = Stmt.executeQuery("SELECT * FROM SDMLATTPAY.GATEPASS_ENTRY_H WHERE GP_DOC_NO='" + pDocNo + "'   ORDER BY REVISION_NO");
            Ready = true;
            HistoryView = true;
            MoveFirst();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
        public static boolean CanCancel(String pDocNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean CanCancel=false;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            
            rsTmp=stTmp.executeQuery("SELECT GP_DOC_NO FROM SDMLATTPAY.GATEPASS_ENTRY WHERE GP_DOC_NO='"+pDocNo+"'  AND APPROVED=0 AND CANCELED=0");
            
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                CanCancel=true;
            }
            
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();            
        }
        catch(Exception e) {
            e.printStackTrace();
        }        
        return CanCancel;
    }
    
    public static void CancelDoc(String pAmendNo) {
    
    ResultSet rsTmp=null;
        
        if(CanCancel(pAmendNo)) {
            
            boolean Approved=false;
            
            try {
                rsTmp=data.getResult("SELECT APPROVED FROM SDMLATTPAY.GATEPASS_ENTRY WHERE GP_DOC_NO='"+pAmendNo+"' ");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    Approved=rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();
                
                if(!Approved) {
                    data.Execute("DELETE FROM SDMLATTPAY.D_COM_DOC_DATA WHERE DOC_NO='"+pAmendNo+"' AND MODULE_ID=805");
                }
                data.Execute("UPDATE SDMLATTPAY.GATEPASS_ENTRY SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE GP_DOC_NO='"+pAmendNo+"'");
              
            }
            catch(Exception e) {
               e.printStackTrace();
            }
        }
}
    
}
