                                                                                                    /*
 * clsSales_Party.java
 *
 * Created on April 3, 2009, 10:22 AM
 */
package SDMLATTPAY.COFF;

import EITLERP.EITLERPGLOBAL;
import EITLERP.Variant;
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
 * @author ashutosh
 */
public class clsCoffProcess {
                                                                                                            
    public String LastError = "";
    private ResultSet rsResultSet, rsResultSet1;
    private Connection Conn;
    private Statement Stmt;
    DecimalFormat df = new DecimalFormat("##.##");
    private HashMap props;
    public boolean Ready = false;

    private boolean HistoryView = false;
    public static int ModuleID = 818; //72
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
    public clsCoffProcess() {
        LastError = "";
        props = new HashMap();
        //props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("DOC_NO", new Variant(""));
        props.put("DOC_DATE", new Variant(""));
        props.put("FROM_DATE", new Variant(""));
        props.put("TO_DATE", new Variant(""));
        props.put("DA_RATE", new Variant(""));

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
            rsResultSet = Stmt.executeQuery("SELECT * FROM SDMLATTPAY.COFF_PAYMENT_DETAIL  GROUP BY DOC_NO,DOC_DATE  ORDER BY DOC_DATE,DOC_NO ");
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

            rsHDetail = stHDetail.executeQuery("SELECT * FROM SDMLATTPAY.COFF_PAYMENT_DETAIL_H WHERE DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//

            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");

            //rsHistory.updateString("FROM_IP",""+str_split[1]);
            Statement tmpStmt;

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            Conn.setAutoCommit(false);
            rsTmp = tmpStmt.executeQuery("SELECT * FROM SDMLATTPAY.COFF_PAYMENT_DETAIL WHERE DOC_NO='1'");

            sql = "INSERT INTO SDMLATTPAY.COFF_PAYMENT_DETAIL "
                    + "(DOC_NO,DOC_DATE,SR_NO,EMPLOYEE_CODE,COFF_DAYS,BASIC_RATE,P_PAY_RATE,DA_RATE,BASIC_EARN,P_PAY_EARN,DA_EARN,"
                    + "TOTAL_EARN,ADDITIONAL_EARN,PREVIOUS_GROSS,PREVIOUS_PTAX,DIFF_PTAX,REV_STAMP,NET_PAY,"
                    + "REMARKS,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,CANCELED,CANCELED_DATE,"
                    + "CHANGED,CHANGED_DATE,FROM_IP,HIERARCHY_ID,REJECTED_REMARKS,FROM_DATE,TO_DATE,BANK_NAME,BANK_ACCOUNT_NO) ";
            sql = sql + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pstm = Conn.prepareStatement(sql);
            sqlh = "INSERT INTO SDMLATTPAY.COFF_PAYMENT_DETAIL_H "
                    + "(REVISION_NO,UPDATED_BY,APPROVAL_STATUS,ENTRY_DATE,APPROVER_REMARKS,"
                    + "DOC_NO,DOC_DATE,SR_NO,EMPLOYEE_CODE,COFF_DAYS,BASIC_RATE,P_PAY_RATE,DA_RATE,BASIC_EARN,P_PAY_EARN,DA_EARN,"
                    + "TOTAL_EARN,ADDITIONAL_EARN,PREVIOUS_GROSS,PREVIOUS_PTAX,DIFF_PTAX,REV_STAMP,NET_PAY,"
                    + "REMARKS,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,CANCELED,CANCELED_DATE,"
                    + "CHANGED,CHANGED_DATE,FROM_IP,HIERARCHY_ID,REJECTED_REMARKS,FROM_DATE,TO_DATE,BANK_NAME,BANK_ACCOUNT_NO) ";
            sqlh = sqlh + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pstmh = Conn.prepareStatement(sqlh);
            //Now Insert records into detail table
            for (int i = 1; i <= colMRItems.size(); i++) {
                clsCoffProcessItem ObjItems = (clsCoffProcessItem) colMRItems.get(Integer.toString(i));

                pstm.setString(1, (String) getAttribute("DOC_NO").getObj());
                pstm.setString(2, (String) getAttribute("DOC_DATE").getObj());
                pstm.setInt(3, ObjItems.getAttribute("SR_NO").getInt());
                pstm.setString(4, (String) ObjItems.getAttribute("EMPLOYEE_CODE").getObj());
                pstm.setDouble(5, (double) ObjItems.getAttribute("COFF_DAYS").getVal());
                pstm.setDouble(6, (double) ObjItems.getAttribute("BASIC_RATE").getVal());
                pstm.setDouble(7, (double) ObjItems.getAttribute("P_PAY_RATE").getVal());
                pstm.setDouble(8, (double) ObjItems.getAttribute("DA_RATE").getVal());
                pstm.setDouble(9, (double) ObjItems.getAttribute("BASIC_EARN").getVal());
                pstm.setDouble(10, (double) ObjItems.getAttribute("P_PAY_EARN").getVal());
                pstm.setDouble(11, (double) ObjItems.getAttribute("DA_EARN").getVal());
                pstm.setDouble(12, (double) ObjItems.getAttribute("TOTAL_EARN").getVal());
                pstm.setDouble(13, (double) ObjItems.getAttribute("ADDITIONAL_EARN").getVal());
                pstm.setDouble(14, (double) ObjItems.getAttribute("PREVIOUS_GROSS").getVal());
                pstm.setDouble(15, (double) ObjItems.getAttribute("PREVIOUS_PTAX").getVal());
                pstm.setDouble(16, (double) ObjItems.getAttribute("DIFF_PTAX").getVal());
                pstm.setDouble(17, (double) ObjItems.getAttribute("REV_STAMP").getVal());
                pstm.setDouble(18, (double) ObjItems.getAttribute("NET_PAY").getVal());
                pstm.setString(19, (String) ObjItems.getAttribute("REMARKS").getObj());
                pstm.setInt(20, (int) getAttribute("CREATED_BY").getVal());
                pstm.setString(21, (String) getAttribute("CREATED_DATE").getObj());
                pstm.setString(22, null);
                pstm.setString(23, null);
                pstm.setBoolean(24, false);
                pstm.setString(25, "0000-00-00");
                pstm.setBoolean(26, false);
                pstm.setString(27, "0000-00-00");
                pstm.setBoolean(28, false);
                pstm.setString(29, "0000-00-00");
                pstm.setBoolean(30, true);
                pstm.setString(31, EITLERPGLOBAL.getCurrentDateDB());
                pstm.setString(32, str_split[1]);
                pstm.setInt(33, (int) getAttribute("HIERARCHY_ID").getVal());
                pstm.setString(34, getAttribute("REJECTED_REMARKS").getString());
                pstm.setString(35, getAttribute("FROM_DATE").getString());
                pstm.setString(36, getAttribute("TO_DATE").getString());
                pstm.setString(37, (String) ObjItems.getAttribute("BANK_NAME").getObj());
                pstm.setString(38, (String) ObjItems.getAttribute("BANK_ACCOUNT_NO").getObj());
                pstm.addBatch();

                pstmh.setInt(1, 1);
                pstmh.setInt(2, (int) getAttribute("CREATED_BY").getVal());
                pstmh.setString(3, getAttribute("APPROVAL_STATUS").getString());
                pstmh.setString(4, data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                pstmh.setString(5, getAttribute("APPROVER_REMARKS").getString());
                pstmh.setString(6, (String) getAttribute("DOC_NO").getObj());
                pstmh.setString(7, (String) getAttribute("DOC_DATE").getObj());
                pstmh.setInt(8, ObjItems.getAttribute("SR_NO").getInt());
                pstmh.setString(9, (String) ObjItems.getAttribute("EMPLOYEE_CODE").getObj());
                pstmh.setDouble(10, (double) ObjItems.getAttribute("COFF_DAYS").getVal());
                pstmh.setDouble(11, (double) ObjItems.getAttribute("BASIC_RATE").getVal());
                pstmh.setDouble(12, (double) ObjItems.getAttribute("P_PAY_RATE").getVal());
                pstmh.setDouble(13, (double) ObjItems.getAttribute("DA_RATE").getVal());
                pstmh.setDouble(14, (double) ObjItems.getAttribute("BASIC_EARN").getVal());
                pstmh.setDouble(15, (double) ObjItems.getAttribute("P_PAY_EARN").getVal());
                pstmh.setDouble(16, (double) ObjItems.getAttribute("DA_EARN").getVal());
                pstmh.setDouble(17, (double) ObjItems.getAttribute("TOTAL_EARN").getVal());
                pstmh.setDouble(18, (double) ObjItems.getAttribute("ADDITIONAL_EARN").getVal());
                pstmh.setDouble(19, (double) ObjItems.getAttribute("PREVIOUS_GROSS").getVal());
                pstmh.setDouble(20, (double) ObjItems.getAttribute("PREVIOUS_PTAX").getVal());
                pstmh.setDouble(21, (double) ObjItems.getAttribute("DIFF_PTAX").getVal());
                pstmh.setDouble(22, (double) ObjItems.getAttribute("REV_STAMP").getVal());
                pstmh.setDouble(23, (double) ObjItems.getAttribute("NET_PAY").getVal());
                pstmh.setString(24, (String) ObjItems.getAttribute("REMARKS").getObj());
                pstmh.setInt(25, (int) getAttribute("CREATED_BY").getVal());
                pstmh.setString(26, (String) getAttribute("CREATED_DATE").getObj());
                pstmh.setString(27, null);
                pstmh.setString(28, null);
                pstmh.setBoolean(29, false);
                pstmh.setString(30, "0000-00-00");
                pstmh.setBoolean(31, false);
                pstmh.setString(32, "0000-00-00");
                pstmh.setBoolean(33, false);
                pstmh.setString(34, "0000-00-00");
                pstmh.setBoolean(35, true);
                pstmh.setString(36, EITLERPGLOBAL.getCurrentDateDB());
                pstmh.setString(37, str_split[1]);
                pstmh.setInt(38, (int) getAttribute("HIERARCHY_ID").getVal());
                pstmh.setString(39, getAttribute("REJECTED_REMARKS").getString());
                pstmh.setString(40, getAttribute("FROM_DATE").getString());
                pstmh.setString(41, getAttribute("TO_DATE").getString());
                pstmh.setString(42, (String) ObjItems.getAttribute("BANK_NAME").getObj());
                pstmh.setString(43, (String) ObjItems.getAttribute("BANK_ACCOUNT_NO").getObj());
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

            //===================== Update the Approval Flow ======================//
            setAttribute("FROM", EITLERPGLOBAL.gUserID);

            //ApprovalFlow ObjFlow=new ApprovalFlow();
            ApprovalFlow ObjFlow = new ApprovalFlow();

            ObjFlow.CompanyID = EITLERPGLOBAL.gCompanyID;
            ObjFlow.ModuleID = clsCoffProcess.ModuleID;
            ObjFlow.DocNo = (String) getAttribute("DOC_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("DOC_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "SDMLATTPAY.COFF_PAYMENT_DETAIL";
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
            //Final Approved...
            if (ObjFlow.Status.equals("F")) {
                String s = "";
                s = "INSERT INTO SDMLATTPAY.ATT_LEAVE_TRN "
                        + "(LVT_YEAR,LVT_EMPID,LVT_PAY_EMPID,LVT_LEAVE_CODE,LVT_LEAVE_TYPE,LVT_DAYS,LVT_FROMDATE,LVT_TODATE,LVT_REMARK,"
                        + "LVT_DOC_NO,LVT_DOC_DATE,SR_NO) "
                        + "SELECT RIGHT(DOC_NO,4),RIGHT(EMPLOYEE_CODE,6),EMPLOYEE_CODE,'CO',5,"
                        + "COFF_DAYS,FROM_DATE,TO_DATE,'COFF ENCASHMENT',DOC_NO,DOC_DATE,SR_NO FROM SDMLATTPAY.COFF_PAYMENT_DETAIL C "
                        + "WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "'";
                data.Execute(s);
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
            data.Execute("DELETE FROM SDMLATTPAY.COFF_PAYMENT_DETAIL WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "'");
            String AStatus = (String) getAttribute("APPROVAL_STATUS").getObj();
            if (AStatus.equals("R")) {
                Validate = false;
            }
            Validate = true;

            // ---- History Related Changes ------ //
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHDetail = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rsHDetail = stHDetail.executeQuery("SELECT * FROM SDMLATTPAY.COFF_PAYMENT_DETAIL_H WHERE DOC_NO='1'");
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
            rsTmp = tmpStmt.executeQuery("SELECT * FROM SDMLATTPAY.COFF_PAYMENT_DETAIL WHERE DOC_NO='1'");

            sql = "INSERT INTO SDMLATTPAY.COFF_PAYMENT_DETAIL "
                    + "(DOC_NO,DOC_DATE,SR_NO,EMPLOYEE_CODE,COFF_DAYS,BASIC_RATE,P_PAY_RATE,DA_RATE,BASIC_EARN,P_PAY_EARN,DA_EARN,"
                    + "TOTAL_EARN,ADDITIONAL_EARN,PREVIOUS_GROSS,PREVIOUS_PTAX,DIFF_PTAX,REV_STAMP,NET_PAY,"
                    + "REMARKS,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,CANCELED,CANCELED_DATE,"
                    + "CHANGED,CHANGED_DATE,FROM_IP,HIERARCHY_ID,REJECTED_REMARKS,FROM_DATE,TO_DATE,BANK_NAME,BANK_ACCOUNT_NO) ";
            sql = sql + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pstm = Conn.prepareStatement(sql);
            sqlh = "INSERT INTO SDMLATTPAY.COFF_PAYMENT_DETAIL_H "
                    + "(REVISION_NO,UPDATED_BY,APPROVAL_STATUS,ENTRY_DATE,APPROVER_REMARKS,"
                    + "DOC_NO,DOC_DATE,SR_NO,EMPLOYEE_CODE,COFF_DAYS,BASIC_RATE,P_PAY_RATE,DA_RATE,BASIC_EARN,P_PAY_EARN,DA_EARN,"
                    + "TOTAL_EARN,ADDITIONAL_EARN,PREVIOUS_GROSS,PREVIOUS_PTAX,DIFF_PTAX,REV_STAMP,NET_PAY,"
                    + "REMARKS,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,CANCELED,CANCELED_DATE,"
                    + "CHANGED,CHANGED_DATE,FROM_IP,HIERARCHY_ID,REJECTED_REMARKS,FROM_DATE,TO_DATE,BANK_NAME,BANK_ACCOUNT_NO) ";
            sqlh = sqlh + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pstmh = Conn.prepareStatement(sqlh);
            int RevNo = 0;
            //Now Insert records into detail table
            for (int i = 1; i <= colMRItems.size(); i++) {
                clsCoffProcessItem ObjItems = (clsCoffProcessItem) colMRItems.get(Integer.toString(i));
                if (i == 1) {
                    RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM SDMLATTPAY.COFF_PAYMENT_DETAIL_H WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "'");
                    RevNo++;
                }
                pstm.setString(1, (String) getAttribute("DOC_NO").getObj());
                pstm.setString(2, (String) getAttribute("DOC_DATE").getObj());
                pstm.setInt(3, ObjItems.getAttribute("SR_NO").getInt());
                pstm.setString(4, (String) ObjItems.getAttribute("EMPLOYEE_CODE").getObj());
                pstm.setDouble(5, (double) ObjItems.getAttribute("COFF_DAYS").getVal());
                pstm.setDouble(6, (double) ObjItems.getAttribute("BASIC_RATE").getVal());
                pstm.setDouble(7, (double) ObjItems.getAttribute("P_PAY_RATE").getVal());
                pstm.setDouble(8, (double) ObjItems.getAttribute("DA_RATE").getVal());
                pstm.setDouble(9, (double) ObjItems.getAttribute("BASIC_EARN").getVal());
                pstm.setDouble(10, (double) ObjItems.getAttribute("P_PAY_EARN").getVal());
                pstm.setDouble(11, (double) ObjItems.getAttribute("DA_EARN").getVal());
                pstm.setDouble(12, (double) ObjItems.getAttribute("TOTAL_EARN").getVal());
                pstm.setDouble(13, (double) ObjItems.getAttribute("ADDITIONAL_EARN").getVal());
                pstm.setDouble(14, (double) ObjItems.getAttribute("PREVIOUS_GROSS").getVal());
                pstm.setDouble(15, (double) ObjItems.getAttribute("PREVIOUS_PTAX").getVal());
                pstm.setDouble(16, (double) ObjItems.getAttribute("DIFF_PTAX").getVal());
                pstm.setDouble(17, (double) ObjItems.getAttribute("REV_STAMP").getVal());
                pstm.setDouble(18, (double) ObjItems.getAttribute("NET_PAY").getVal());
                pstm.setString(19, (String) ObjItems.getAttribute("REMARKS").getObj());
                pstm.setString(20, null);
                pstm.setString(21, null);
                pstm.setInt(22, (int) getAttribute("MODIFIED_BY").getVal());
                pstm.setString(23, (String) getAttribute("MODIFIED_DATE").getObj());
                pstm.setBoolean(24, false);
                pstm.setString(25, "0000-00-00");
                pstm.setBoolean(26, false);
                pstm.setString(27, "0000-00-00");
                pstm.setBoolean(28, false);
                pstm.setString(29, "0000-00-00");
                pstm.setBoolean(30, true);
                pstm.setString(31, EITLERPGLOBAL.getCurrentDateDB());
                pstm.setString(32, str_split[1]);
                pstm.setInt(33, (int) getAttribute("HIERARCHY_ID").getVal());
                pstm.setString(34, getAttribute("REJECTED_REMARKS").getString());
                pstm.setString(35, getAttribute("FROM_DATE").getString());
                pstm.setString(36, getAttribute("TO_DATE").getString());
                pstm.setString(37, (String) ObjItems.getAttribute("BANK_NAME").getObj());
                pstm.setString(38, (String) ObjItems.getAttribute("BANK_ACCOUNT_NO").getObj());
                pstm.addBatch();

                pstmh.setInt(1, RevNo);
                pstmh.setInt(2, (int) getAttribute("MODIFIED_BY").getVal());
                pstmh.setString(3, getAttribute("APPROVAL_STATUS").getString());
                pstmh.setString(4, data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                pstmh.setString(5, getAttribute("APPROVER_REMARKS").getString());
                pstmh.setString(6, (String) getAttribute("DOC_NO").getObj());
                pstmh.setString(7, (String) getAttribute("DOC_DATE").getObj());
                pstmh.setInt(8, ObjItems.getAttribute("SR_NO").getInt());
                pstmh.setString(9, (String) ObjItems.getAttribute("EMPLOYEE_CODE").getObj());
                pstmh.setDouble(10, (double) ObjItems.getAttribute("COFF_DAYS").getVal());
                pstmh.setDouble(11, (double) ObjItems.getAttribute("BASIC_RATE").getVal());
                pstmh.setDouble(12, (double) ObjItems.getAttribute("P_PAY_RATE").getVal());
                pstmh.setDouble(13, (double) ObjItems.getAttribute("DA_RATE").getVal());
                pstmh.setDouble(14, (double) ObjItems.getAttribute("BASIC_EARN").getVal());
                pstmh.setDouble(15, (double) ObjItems.getAttribute("P_PAY_EARN").getVal());
                pstmh.setDouble(16, (double) ObjItems.getAttribute("DA_EARN").getVal());
                pstmh.setDouble(17, (double) ObjItems.getAttribute("TOTAL_EARN").getVal());
                pstmh.setDouble(18, (double) ObjItems.getAttribute("ADDITIONAL_EARN").getVal());
                pstmh.setDouble(19, (double) ObjItems.getAttribute("PREVIOUS_GROSS").getVal());
                pstmh.setDouble(20, (double) ObjItems.getAttribute("PREVIOUS_PTAX").getVal());
                pstmh.setDouble(21, (double) ObjItems.getAttribute("DIFF_PTAX").getVal());
                pstmh.setDouble(22, (double) ObjItems.getAttribute("REV_STAMP").getVal());
                pstmh.setDouble(23, (double) ObjItems.getAttribute("NET_PAY").getVal());
                pstmh.setString(24, (String) ObjItems.getAttribute("REMARKS").getObj());
                pstmh.setString(25, null);
                pstmh.setString(26, null);
                pstmh.setInt(27, (int) getAttribute("MODIFIED_BY").getVal());
                pstmh.setString(28, (String) getAttribute("MODIFIED_DATE").getObj());
                pstmh.setBoolean(29, false);
                pstmh.setString(30, "0000-00-00");
                pstmh.setBoolean(31, false);
                pstmh.setString(32, "0000-00-00");
                pstmh.setBoolean(33, false);
                pstmh.setString(34, "0000-00-00");
                pstmh.setBoolean(35, true);
                pstmh.setString(36, EITLERPGLOBAL.getCurrentDateDB());
                pstmh.setString(37, str_split[1]);
                pstmh.setInt(38, (int) getAttribute("HIERARCHY_ID").getVal());
                pstmh.setString(39, getAttribute("REJECTED_REMARKS").getString());
                pstmh.setString(40, getAttribute("FROM_DATE").getString());
                pstmh.setString(41, getAttribute("TO_DATE").getString());
                pstmh.setString(42, (String) ObjItems.getAttribute("BANK_NAME").getObj());
                pstmh.setString(43, (String) ObjItems.getAttribute("BANK_ACCOUNT_NO").getObj());
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
            s = "UPDATE SDMLATTPAY.COFF_PAYMENT_DETAIL D,SDMLATTPAY.COFF_PAYMENT_DETAIL_H H "
                    + "SET D.CREATED_BY=H.CREATED_BY ,D.CREATED_DATE=H.CREATED_DATE,D.DOC_DATE=H.DOC_DATE "
                    + "WHERE D.DOC_NO=H.DOC_NO AND REVISION_NO=1 AND "
                    + "D.DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "'";
            data.Execute(s);

            //===================== Update the Approval Flow ======================//
            setAttribute("FROM", EITLERPGLOBAL.gUserID);

            //ApprovalFlow ObjFlow=new ApprovalFlow();
            ApprovalFlow ObjFlow = new ApprovalFlow();

            ObjFlow.CompanyID = EITLERPGLOBAL.gCompanyID;
            ObjFlow.ModuleID = clsCoffProcess.ModuleID;
            ObjFlow.DocNo = (String) getAttribute("DOC_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("DOC_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "SDMLATTPAY.COFF_PAYMENT_DETAIL";
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
                data.Execute("UPDATE SDMLATTPAY.COFF_PAYMENT_DETAIL SET REJECTED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='" + ObjFlow.DocNo + "' ");

                //Remove Old Records from FELT_DOC_DATA
                data.Execute("DELETE FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=" + clsCoffProcess.ModuleID + " AND DOC_NO='" + ObjFlow.DocNo + "'");

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
            //Final Approved...
            if (ObjFlow.Status.equals("F")) {
                String s1 = "";
                s1 = "INSERT INTO SDMLATTPAY.ATT_LEAVE_TRN "
                        + "(LVT_YEAR,LVT_EMPID,LVT_PAY_EMPID,LVT_LEAVE_CODE,LVT_LEAVE_TYPE,LVT_DAYS,LVT_FROMDATE,LVT_TODATE,LVT_REMARK,"
                        + "LVT_DOC_NO,LVT_DOC_DATE,SR_NO) "
                        + "SELECT RIGHT(DOC_NO,4),RIGHT(EMPLOYEE_CODE,6),EMPLOYEE_CODE,'CO',5,"
                        + "COFF_DAYS,FROM_DATE,TO_DATE,'COFF ENCASHMENT',DOC_NO,DOC_DATE,SR_NO FROM SDMLATTPAY.COFF_PAYMENT_DETAIL C "
                        + "WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "'";
                data.Execute(s1);
            }
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
            setAttribute("FROM_DATE", EITLERPGLOBAL.formatDate(rsResultSet.getString("FROM_DATE")));
            setAttribute("TO_DATE", EITLERPGLOBAL.formatDate(rsResultSet.getString("TO_DATE")));
            setAttribute("DA_RATE", rsResultSet.getString("DA_RATE"));

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
                rsTmp = tmpStmt.executeQuery("SELECT * FROM SDMLATTPAY.COFF_PAYMENT_DETAIL_H WHERE DOC_NO='" + mDocNo + "' AND REVISION_NO=" + RevNo + "   ORDER BY DOC_NO");
            } else {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM SDMLATTPAY.COFF_PAYMENT_DETAIL WHERE DOC_NO='" + mDocNo + "'   ORDER BY DOC_NO");
            }
            rsTmp.first();

            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {
                Counter = Counter + 1;
                clsCoffProcessItem ObjItems = new clsCoffProcessItem();
                ObjItems.setAttribute("EMPLOYEE_CODE", rsTmp.getString("EMPLOYEE_CODE"));
                ObjItems.setAttribute("SR_NO", rsTmp.getInt("SR_NO"));
                ObjItems.setAttribute("COFF_DAYS", EITLERPGLOBAL.round(rsTmp.getDouble("COFF_DAYS"), 2));
                ObjItems.setAttribute("BASIC_RATE", EITLERPGLOBAL.round(rsTmp.getDouble("BASIC_RATE"), 2));
                ObjItems.setAttribute("P_PAY_RATE", EITLERPGLOBAL.round(rsTmp.getDouble("P_PAY_RATE"), 2));
                ObjItems.setAttribute("DA_RATE", EITLERPGLOBAL.round(rsTmp.getDouble("DA_RATE"), 2));
                ObjItems.setAttribute("BASIC_EARN", EITLERPGLOBAL.round(rsTmp.getDouble("BASIC_EARN"), 2));
                ObjItems.setAttribute("P_PAY_EARN", EITLERPGLOBAL.round(rsTmp.getDouble("P_PAY_EARN"), 2));
                ObjItems.setAttribute("DA_EARN", EITLERPGLOBAL.round(rsTmp.getDouble("DA_EARN"), 2));
                ObjItems.setAttribute("TOTAL_EARN", EITLERPGLOBAL.round(rsTmp.getDouble("TOTAL_EARN"), 2));
                ObjItems.setAttribute("ADDITIONAL_EARN", EITLERPGLOBAL.round(rsTmp.getDouble("ADDITIONAL_EARN"), 2));
                ObjItems.setAttribute("PREVIOUS_GROSS", EITLERPGLOBAL.round(rsTmp.getDouble("PREVIOUS_GROSS"), 2));
                ObjItems.setAttribute("PREVIOUS_PTAX", EITLERPGLOBAL.round(rsTmp.getDouble("PREVIOUS_PTAX"), 2));
                ObjItems.setAttribute("DIFF_PTAX", EITLERPGLOBAL.round(rsTmp.getDouble("DIFF_PTAX"), 2));
                ObjItems.setAttribute("REV_STAMP", EITLERPGLOBAL.round(rsTmp.getDouble("REV_STAMP"), 2));
                ObjItems.setAttribute("NET_PAY", EITLERPGLOBAL.round(rsTmp.getDouble("NET_PAY"), 2));
                ObjItems.setAttribute("REMARKS", rsTmp.getString("REMARKS"));
                ObjItems.setAttribute("BANK_NAME", rsTmp.getString("BANK_NAME"));
                ObjItems.setAttribute("BANK_ACCOUNT_NO", rsTmp.getString("BANK_ACCOUNT_NO"));
                ObjItems.setAttribute("HIERARCHY_ID", rsTmp.getString("HIERARCHY_ID"));
                ObjItems.setAttribute("CREATED_BY", rsTmp.getLong("CREATED_BY"));
                ObjItems.setAttribute("CREATED_DATE", rsTmp.getString("CREATED_DATE"));
                ObjItems.setAttribute("MODIFIED_BY", rsTmp.getLong("MODIFIED_BY"));
                ObjItems.setAttribute("MODIFIED_DATE", rsTmp.getString("MODIFIED_DATE"));
                ObjItems.setAttribute("APPROVED", rsTmp.getString("APPROVED"));
                ObjItems.setAttribute("APPROVED_DATE", rsTmp.getString("APPROVED_DATE"));
                ObjItems.setAttribute("REJECTED", rsTmp.getString("REJECTED"));
                ObjItems.setAttribute("REJECTED_DATE", rsTmp.getString("REJECTED_DATE"));
                ObjItems.setAttribute("REJECTED_REMARKS", rsTmp.getString("REJECTED_REMARKS"));
                ObjItems.setAttribute("CANCELED", rsTmp.getString("CANCELED"));
                ObjItems.setAttribute("CANCELED_DATE", rsTmp.getString("CANCELED_DATE"));
                ObjItems.setAttribute("CHANGED", rsTmp.getString("CHANGED"));
                ObjItems.setAttribute("CHANGED_DATE", rsTmp.getString("CHANGED_DATE"));

                colMRItems.put(Long.toString(Counter), ObjItems);
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.COFF_PAYMENT_DETAIL WHERE DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=" + ModuleID + " AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND STATUS='W'";
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
            String strSQL = "SELECT * FROM SDMLATTPAY.COFF_PAYMENT_DETAIL_H WHERE DOC_NO='" + DocNo + "' GROUP BY REVISION_NO,DOC_NO ";
            rsTmp = data.getResult(strSQL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                while (!rsTmp.isAfterLast()) {
                    clsCoffProcess objParty = new clsCoffProcess();

                    objParty.setAttribute("DOC_NO", rsTmp.getString("DOC_NO"));
                    objParty.setAttribute("DOC_DATE", rsTmp.getString("DOC_DATE"));
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
            strSQL += "SELECT * FROM SDMLATTPAY.COFF_PAYMENT_DETAIL WHERE " + Condition + " GROUP BY DOC_NO";
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery(strSQL);

            if (!rsResultSet.first()) {
                strSQL = "SELECT * FROM SDMLATTPAY.COFF_PAYMENT_DETAIL WHERE DOC_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND DOC_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'   GROUP BY DOC_NO  ORDER BY DOC_NO";
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.COFF_PAYMENT_DETAIL WHERE DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=" + ModuleID + " AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND TYPE='C' AND STATUS='W'";
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
                String strQry = "DELETE FROM SDMLATTPAY.COFF_PAYMENT_DETAIL WHERE DOC_NO='" + lDocNo + "'";
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
                strSQL = "SELECT DISTINCT SDMLATTPAY.COFF_PAYMENT_DETAIL.DOC_NO,SDMLATTPAY.COFF_PAYMENT_DETAIL.DOC_DATE,RECEIVED_DATE FROM SDMLATTPAY.COFF_PAYMENT_DETAIL,SDMLATTPAY.D_COM_DOC_DATA WHERE SDMLATTPAY.D_COM_DOC_DATA.DOC_NO=SDMLATTPAY.COFF_PAYMENT_DETAIL.DOC_NO AND SDMLATTPAY.D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND SDMLATTPAY.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=" + ModuleID + " ORDER BY SDMLATTPAY.D_COM_DOC_DATA.RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT SDMLATTPAY.COFF_PAYMENT_DETAIL.DOC_NO,SDMLATTPAY.COFF_PAYMENT_DETAIL.DOC_DATE,RECEIVED_DATE FROM SDMLATTPAY.COFF_PAYMENT_DETAIL,SDMLATTPAY.D_COM_DOC_DATA WHERE SDMLATTPAY.D_COM_DOC_DATA.DOC_NO=SDMLATTPAY.COFF_PAYMENT_DETAIL.DOC_NO AND SDMLATTPAY.D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND SDMLATTPAY.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=" + ModuleID + " ORDER BY SDMLATTPAY.COFF_PAYMENT_DETAIL.DOC_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT SDMLATTPAY.COFF_PAYMENT_DETAIL.DOC_NO,SDMLATTPAY.COFF_PAYMENT_DETAIL.DOC_DATE,RECEIVED_DATE FROM SDMLATTPAY.COFF_PAYMENT_DETAIL,SDMLATTPAY.D_COM_DOC_DATA WHERE SDMLATTPAY.D_COM_DOC_DATA.DOC_NO=SDMLATTPAY.COFF_PAYMENT_DETAIL.DOC_NO AND SDMLATTPAY.D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND SDMLATTPAY.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=" + ModuleID + " ORDER BY SDMLATTPAY.COFF_PAYMENT_DETAIL.DOC_NO";
            }

            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {

                Counter = Counter + 1;
                clsCoffProcess ObjItem = new clsCoffProcess();

                //------------- Header Fields --------------------//
                ObjItem.setAttribute("DOC_NO", rsTmp.getString("DOC_NO"));
                ObjItem.setAttribute("DOC_DATE", rsTmp.getString("DOC_DATE"));
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
            rsResultSet = Stmt.executeQuery("SELECT * FROM SDMLATTPAY.COFF_PAYMENT_DETAIL_H WHERE DOC_NO='" + pDocNo + "'  GROUP BY DOC_NO ORDER BY REVISION_NO");
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
