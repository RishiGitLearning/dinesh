/*
 * clsSales_Party.java
 *
 * Created on April 3, 2009, 10:22 AM
 */
package SDMLATTPAY.Shift;

import EITLERP.FeltSales.Budget.*;
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

/**
 *
 * @author ashutosh
 */
public class clsShiftUpload {

    public String LastError = "";
    private ResultSet rsResultSet, rsResultSet1;
    private Connection Conn;
    private Statement Stmt;
    DecimalFormat df = new DecimalFormat("##.##");
    private HashMap props;
    public boolean Ready = false;

    private boolean HistoryView = false;
    public static int ModuleID = 806; //72
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
    public clsShiftUpload() {
        LastError = "";
        props = new HashMap();
        //props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("SU_DOC_NO", new Variant(""));
        props.put("SU_DOC_DATE", new Variant(""));
        props.put("SU_YEAR", new Variant(""));
        props.put("SU_MONTH", new Variant(""));

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
            rsResultSet = Stmt.executeQuery("SELECT * FROM SDMLATTPAY.ATT_SHIFT_UPLOAD  GROUP BY SU_DOC_NO");
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

            rsHDetail = stHDetail.executeQuery("SELECT * FROM SDMLATTPAY.ATT_SHIFT_UPLOAD_H WHERE SU_DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//

            //-------------------------------------------------
            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");

            //rsHistory.updateString("FROM_IP",""+str_split[1]);
            Statement tmpStmt;

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            Conn.setAutoCommit(false);
            rsTmp = tmpStmt.executeQuery("SELECT * FROM SDMLATTPAY.ATT_SHIFT_UPLOAD WHERE SU_DOC_NO='1'");

            sql = "INSERT INTO SDMLATTPAY.ATT_SHIFT_UPLOAD "
                    + "(SU_DOC_NO,SU_DOC_DATE,SU_EMPID,SU_YEAR,SU_MONTH,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD.1,SDMLATTPAY.ATT_SHIFT_UPLOAD.2,SDMLATTPAY.ATT_SHIFT_UPLOAD.3,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD.4,SDMLATTPAY.ATT_SHIFT_UPLOAD.5,SDMLATTPAY.ATT_SHIFT_UPLOAD.6,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD.7,SDMLATTPAY.ATT_SHIFT_UPLOAD.8,SDMLATTPAY.ATT_SHIFT_UPLOAD.9,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD.10,SDMLATTPAY.ATT_SHIFT_UPLOAD.11,SDMLATTPAY.ATT_SHIFT_UPLOAD.12,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD.13,SDMLATTPAY.ATT_SHIFT_UPLOAD.14,SDMLATTPAY.ATT_SHIFT_UPLOAD.15,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD.16,SDMLATTPAY.ATT_SHIFT_UPLOAD.17,SDMLATTPAY.ATT_SHIFT_UPLOAD.18,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD.19,SDMLATTPAY.ATT_SHIFT_UPLOAD.20,SDMLATTPAY.ATT_SHIFT_UPLOAD.21,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD.22,SDMLATTPAY.ATT_SHIFT_UPLOAD.23,SDMLATTPAY.ATT_SHIFT_UPLOAD.24,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD.25,SDMLATTPAY.ATT_SHIFT_UPLOAD.26,SDMLATTPAY.ATT_SHIFT_UPLOAD.27,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD.28,SDMLATTPAY.ATT_SHIFT_UPLOAD.29,SDMLATTPAY.ATT_SHIFT_UPLOAD.30,SDMLATTPAY.ATT_SHIFT_UPLOAD.31,"
                    + "CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,"
                    + "CANCELED,HIERARCHY_ID,CHANGED,CHANGED_DATE,REJECTED_REMARKS,FROM_IP"
                    + ") ";
            sql = sql + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pstm = Conn.prepareStatement(sql);
            sqlh = "INSERT INTO SDMLATTPAY.ATT_SHIFT_UPLOAD_H "
                    + "(REVISION_NO,UPDATED_BY,APPROVAL_STATUS,ENTRY_DATE,APPROVER_REMARKS,SU_DOC_NO,SU_DOC_DATE,SU_EMPID,SU_YEAR,SU_MONTH,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD_H.1,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.2,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.3,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD_H.4,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.5,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.6,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD_H.7,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.8,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.9,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD_H.10,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.11,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.12,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD_H.13,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.14,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.15,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD_H.16,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.17,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.18,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD_H.19,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.20,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.21,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD_H.22,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.23,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.24,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD_H.25,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.26,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.27,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD_H.28,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.29,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.30,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.31,"
                    + "CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,"
                    + "CANCELED,HIERARCHY_ID,CHANGED,CHANGED_DATE,REJECTED_REMARKS,FROM_IP) ";
            sqlh = sqlh + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pstmh = Conn.prepareStatement(sqlh);
            //Now Insert records into detail table
            for (int i = 1; i <= colMRItems.size(); i++) {
                clsShiftUploadItem ObjMRItems = (clsShiftUploadItem) colMRItems.get(Integer.toString(i));

                pstm.setString(1, (String) ObjMRItems.getAttribute("SU_DOC_NO").getObj());
                pstm.setString(2, (String) ObjMRItems.getAttribute("SU_DOC_DATE").getObj());
                pstm.setString(3, (String) ObjMRItems.getAttribute("SU_EMPID").getObj());
                pstm.setString(4, (String) ObjMRItems.getAttribute("SU_YEAR").getObj());
                pstm.setString(5, (String) ObjMRItems.getAttribute("SU_MONTH").getObj());
                for (int j = 1; j <= 31; j++) {
                    pstm.setString(j + 5, (String) ObjMRItems.getAttribute(String.valueOf(j)).getString());
                }

                pstm.setInt(37, (int) getAttribute("CREATED_BY").getVal());
                pstm.setString(38, (String) getAttribute("CREATED_DATE").getObj());
                pstm.setString(39, null);
                pstm.setString(40, null);
                pstm.setBoolean(41, false);
                pstm.setString(42, "0000-00-00");
                pstm.setBoolean(43, false);
                pstm.setString(44, "0000-00-00");
                pstm.setBoolean(45, false);
                pstm.setInt(46, (int) getAttribute("HIERARCHY_ID").getVal());
                pstm.setBoolean(47, true);
                pstm.setString(48, EITLERPGLOBAL.getCurrentDateDB());
                pstm.setString(49, getAttribute("REJECTED_REMARKS").getString());
                pstm.setString(50, str_split[1]);
                pstm.addBatch();

                pstmh.setInt(1, 1);
                pstmh.setInt(2, (int) getAttribute("CREATED_BY").getVal());
                pstmh.setString(3, getAttribute("APPROVAL_STATUS").getString());
                pstmh.setString(4, data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                pstmh.setString(5, getAttribute("APPROVER_REMARKS").getString());
                pstmh.setString(6, (String) ObjMRItems.getAttribute("SU_DOC_NO").getObj());
                pstmh.setString(7, (String) ObjMRItems.getAttribute("SU_DOC_DATE").getObj());
                pstmh.setString(8, (String) ObjMRItems.getAttribute("SU_EMPID").getObj());
                pstmh.setString(9, (String) ObjMRItems.getAttribute("SU_YEAR").getObj());
                pstmh.setString(10, (String) ObjMRItems.getAttribute("SU_MONTH").getObj());
                for (int j = 1; j <= 31; j++) {
                    pstmh.setString(j + 10, (String) ObjMRItems.getAttribute(String.valueOf(j)).getString());
                }

                pstmh.setInt(42, (int) getAttribute("CREATED_BY").getVal());
                pstmh.setString(43, (String) getAttribute("CREATED_DATE").getObj());
                pstmh.setString(44, null);
                pstmh.setString(45, null);
                pstmh.setBoolean(46, false);
                pstmh.setString(47, "0000-00-00");
                pstmh.setBoolean(48, false);
                pstmh.setString(49, "0000-00-00");
                pstmh.setBoolean(50, false);
                pstmh.setInt(51, (int) getAttribute("HIERARCHY_ID").getVal());
                pstmh.setBoolean(52, true);
                pstmh.setString(53, EITLERPGLOBAL.getCurrentDateDB());
                pstmh.setString(54, getAttribute("REJECTED_REMARKS").getString());
                pstmh.setString(55, str_split[1]);

                pstmh.addBatch();
                System.out.println("Query:" + pstm);
                System.out.println("QueryH:" + pstmh);
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
            SDMLATTPAY.ApprovalFlow ObjFlow = new SDMLATTPAY.ApprovalFlow();

            ObjFlow.ModuleID = clsShiftUpload.ModuleID;
            ObjFlow.CompanyID = EITLERPGLOBAL.gCompanyID;
            ObjFlow.DocNo = (String) getAttribute("SU_DOC_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("SU_DOC_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "SDMLATTPAY.ATT_SHIFT_UPLOAD";
            ObjFlow.IsCreator = true;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "SU_DOC_NO";

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
            data.Execute("DELETE FROM SDMLATTPAY.ATT_SHIFT_UPLOAD WHERE SU_DOC_NO='" + (String) getAttribute("SU_DOC_NO").getObj() + "'");
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

            rsHDetail = stHDetail.executeQuery("SELECT * FROM SDMLATTPAY.ATT_SHIFT_UPLOAD_H WHERE SU_DOC_NO='1'");
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
            rsTmp = tmpStmt.executeQuery("SELECT * FROM SDMLATTPAY.ATT_SHIFT_UPLOAD WHERE SU_DOC_NO='1'");

            sql = "INSERT INTO SDMLATTPAY.ATT_SHIFT_UPLOAD "
                    + "(SU_DOC_NO,SU_DOC_DATE,SU_EMPID,SU_YEAR,SU_MONTH,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD.1,SDMLATTPAY.ATT_SHIFT_UPLOAD.2,SDMLATTPAY.ATT_SHIFT_UPLOAD.3,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD.4,SDMLATTPAY.ATT_SHIFT_UPLOAD.5,SDMLATTPAY.ATT_SHIFT_UPLOAD.6,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD.7,SDMLATTPAY.ATT_SHIFT_UPLOAD.8,SDMLATTPAY.ATT_SHIFT_UPLOAD.9,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD.10,SDMLATTPAY.ATT_SHIFT_UPLOAD.11,SDMLATTPAY.ATT_SHIFT_UPLOAD.12,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD.13,SDMLATTPAY.ATT_SHIFT_UPLOAD.14,SDMLATTPAY.ATT_SHIFT_UPLOAD.15,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD.16,SDMLATTPAY.ATT_SHIFT_UPLOAD.17,SDMLATTPAY.ATT_SHIFT_UPLOAD.18,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD.19,SDMLATTPAY.ATT_SHIFT_UPLOAD.20,SDMLATTPAY.ATT_SHIFT_UPLOAD.21,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD.22,SDMLATTPAY.ATT_SHIFT_UPLOAD.23,SDMLATTPAY.ATT_SHIFT_UPLOAD.24,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD.25,SDMLATTPAY.ATT_SHIFT_UPLOAD.26,SDMLATTPAY.ATT_SHIFT_UPLOAD.27,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD.28,SDMLATTPAY.ATT_SHIFT_UPLOAD.29,SDMLATTPAY.ATT_SHIFT_UPLOAD.30,SDMLATTPAY.ATT_SHIFT_UPLOAD.31,"
                    + "CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,"
                    + "CANCELED,HIERARCHY_ID,CHANGED,CHANGED_DATE,REJECTED_REMARKS,FROM_IP"
                    + ") ";
            sql = sql + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pstm = Conn.prepareStatement(sql);
            sqlh = "INSERT INTO SDMLATTPAY.ATT_SHIFT_UPLOAD_H "
                    + "(REVISION_NO,UPDATED_BY,APPROVAL_STATUS,ENTRY_DATE,APPROVER_REMARKS,SU_DOC_NO,SU_DOC_DATE,SU_EMPID,SU_YEAR,SU_MONTH,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD_H.1,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.2,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.3,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD_H.4,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.5,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.6,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD_H.7,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.8,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.9,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD_H.10,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.11,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.12,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD_H.13,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.14,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.15,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD_H.16,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.17,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.18,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD_H.19,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.20,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.21,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD_H.22,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.23,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.24,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD_H.25,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.26,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.27,"
                    + "SDMLATTPAY.ATT_SHIFT_UPLOAD_H.28,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.29,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.30,SDMLATTPAY.ATT_SHIFT_UPLOAD_H.31,"
                    + "CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,"
                    + "CANCELED,HIERARCHY_ID,CHANGED,CHANGED_DATE,REJECTED_REMARKS,FROM_IP) ";
            sqlh = sqlh + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pstmh = Conn.prepareStatement(sqlh);
            int RevNo = 0;
            //Now Insert records into detail table
            for (int i = 1; i <= colMRItems.size(); i++) {
                clsShiftUploadItem ObjMRItems = (clsShiftUploadItem) colMRItems.get(Integer.toString(i));
                if (i == 1) {
                    RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM SDMLATTPAY.ATT_SHIFT_UPLOAD_H WHERE SU_DOC_NO='" + (String) ObjMRItems.getAttribute("SU_DOC_NO").getObj() + "'");
                    RevNo++;
                }
                pstm.setString(1, (String) ObjMRItems.getAttribute("SU_DOC_NO").getObj());
                pstm.setString(2, (String) ObjMRItems.getAttribute("SU_DOC_DATE").getObj());
                pstm.setString(3, (String) ObjMRItems.getAttribute("SU_EMPID").getObj());
                pstm.setString(4, (String) ObjMRItems.getAttribute("SU_YEAR").getObj());
                pstm.setString(5, (String) ObjMRItems.getAttribute("SU_MONTH").getObj());
                for (int j = 1; j <= 31; j++) {
                    pstm.setString(j + 5, (String) ObjMRItems.getAttribute(String.valueOf(j)).getString());
                }
                pstm.setString(37, null);
                pstm.setString(38, null);
                pstm.setInt(39, (int) getAttribute("MODIFIED_BY").getVal());
                pstm.setString(40, (String) getAttribute("MODIFIED_DATE").getObj());
                pstm.setBoolean(41, false);
                pstm.setString(42, "0000-00-00");
                pstm.setBoolean(43, false);
                pstm.setString(44, "0000-00-00");
                pstm.setBoolean(45, false);
                pstm.setInt(46, (int) getAttribute("HIERARCHY_ID").getVal());
                pstm.setBoolean(47, true);
                pstm.setString(48, EITLERPGLOBAL.getCurrentDateDB());
                pstm.setString(49, getAttribute("REJECTED_REMARKS").getString());
                pstm.setString(50, str_split[1]);
                pstm.addBatch();

                pstmh.setInt(1, RevNo);
                pstmh.setInt(2, (int) getAttribute("MODIFIED_BY").getVal());
                pstmh.setString(3, getAttribute("APPROVAL_STATUS").getString());
                pstmh.setString(4, data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                pstmh.setString(5, getAttribute("APPROVER_REMARKS").getString());
                pstmh.setString(6, (String) ObjMRItems.getAttribute("SU_DOC_NO").getObj());
                pstmh.setString(7, (String) ObjMRItems.getAttribute("SU_DOC_DATE").getObj());
                pstmh.setString(8, (String) ObjMRItems.getAttribute("SU_EMPID").getObj());
                pstmh.setString(9, (String) ObjMRItems.getAttribute("SU_YEAR").getObj());
                pstmh.setString(10, (String) ObjMRItems.getAttribute("SU_MONTH").getObj());
                for (int j = 1; j <= 31; j++) {
                    pstmh.setString(j + 10, (String) ObjMRItems.getAttribute(String.valueOf(j)).getString());
                }
                pstmh.setString(42, null);
                pstmh.setString(43, null);
                pstmh.setInt(44, (int) getAttribute("MODIFIED_BY").getVal());
                pstmh.setString(45, (String) getAttribute("MODIFIED_DATE").getObj());
                pstmh.setBoolean(46, false);
                pstmh.setString(47, "0000-00-00");
                pstmh.setBoolean(48, false);
                pstmh.setString(49, "0000-00-00");
                pstmh.setBoolean(50, false);
                pstmh.setInt(51, (int) getAttribute("HIERARCHY_ID").getVal());
                pstmh.setBoolean(52, true);
                pstmh.setString(53, EITLERPGLOBAL.getCurrentDateDB());
                pstmh.setString(54, getAttribute("REJECTED_REMARKS").getString());
                pstmh.setString(55, str_split[1]);

                pstmh.addBatch();
                System.out.println("UQuery:" + pstm);
                System.out.println("UQueryH:" + pstmh);

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
            SDMLATTPAY.ApprovalFlow ObjFlow = new SDMLATTPAY.ApprovalFlow();

            ObjFlow.ModuleID = clsShiftUpload.ModuleID;
            ObjFlow.CompanyID = EITLERPGLOBAL.gCompanyID;
            ObjFlow.DocNo = (String) getAttribute("SU_DOC_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("SU_DOC_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "SDMLATTPAY.ATT_SHIFT_UPLOAD";
            ObjFlow.IsCreator = true;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "SU_DOC_NO";
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
                data.Execute("UPDATE SDMLATTPAY.ATT_SHIFT_UPLOAD SET REJECTED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE SU_DOC_NO='" + ObjFlow.DocNo + "' ");

                //Remove Old Records from FELT_DOC_DATA
                data.Execute("DELETE FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=" + clsShiftUpload.ModuleID + " AND DOC_NO='" + ObjFlow.DocNo + "'");

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

            setAttribute("SU_DOC_NO", rsResultSet.getString("SU_DOC_NO"));
            setAttribute("SU_DOC_DATE", rsResultSet.getString("SU_DOC_DATE"));
            setAttribute("SU_YEAR", rsResultSet.getString("SU_YEAR"));
            setAttribute("SU_MONTH", rsResultSet.getString("SU_MONTH"));

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

            String mDocNo = (String) getAttribute("SU_DOC_NO").getObj();

            tmpStmt = tmpConn.createStatement();
            if (HistoryView) {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM SDMLATTPAY.ATT_SHIFT_UPLOAD_H WHERE SU_DOC_NO='" + mDocNo + "' AND REVISION_NO=" + RevNo + "  ORDER BY SU_DOC_NO");
            } else {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM SDMLATTPAY.ATT_SHIFT_UPLOAD WHERE SU_DOC_NO='" + mDocNo + "'  ORDER BY SU_DOC_NO");
            }
            rsTmp.first();

            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {
                Counter = Counter + 1;
                clsShiftUploadItem ObjMRItems = new clsShiftUploadItem();
                ObjMRItems.setAttribute("SU_DOC_NO", rsTmp.getString("SU_DOC_NO"));
                ObjMRItems.setAttribute("SU_DOC_DATE", rsTmp.getString("SU_DOC_DATE"));
                ObjMRItems.setAttribute("SU_EMPID", rsTmp.getString("SU_EMPID"));
                ObjMRItems.setAttribute("SU_YEAR", rsTmp.getString("SU_YEAR"));
                ObjMRItems.setAttribute("SU_MONTH", rsTmp.getString("SU_MONTH"));

                for (int i = 1; i <= 31; i++) {
                    ObjMRItems.setAttribute(String.valueOf(i), rsTmp.getString(String.valueOf(i)));
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
                ObjMRItems.setAttribute("CHANGED", rsTmp.getString("CHANGED"));
                ObjMRItems.setAttribute("CHANGED_DATE", rsTmp.getString("CHANGED_DATE"));
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.ATT_SHIFT_UPLOAD WHERE SU_DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=806 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND STATUS='W'";
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
            String strSQL = "SELECT * FROM SDMLATTPAY.ATT_SHIFT_UPLOAD_H WHERE SU_DOC_NO='" + DocNo + "' GROUP BY REVISION_NO,SU_DOC_NO ";
            rsTmp = data.getResult(strSQL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                while (!rsTmp.isAfterLast()) {
                    clsShiftUpload objParty = new clsShiftUpload();

                    objParty.setAttribute("SU_DOC_NO", rsTmp.getString("SU_DOC_NO"));
                    objParty.setAttribute("SU_DOC_DATE", rsTmp.getString("SU_DOC_DATE"));
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
            strSQL += "SELECT * FROM SDMLATTPAY.ATT_SHIFT_UPLOAD WHERE " + Condition + " GROUP BY SU_DOC_NO";
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery(strSQL);

            if (!rsResultSet.first()) {
                strSQL = "SELECT * FROM SDMLATTPAY.ATT_SHIFT_UPLOAD  GROUP BY SU_DOC_NO";
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.ATT_SHIFT_UPLOAD WHERE SU_DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=806 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND TYPE='C' AND STATUS='W'";
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
                String strQry = "DELETE FROM SDMLATTPAY.ATT_SHIFT_UPLOAD WHERE SU_DOC_NO='" + lDocNo + "'";
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
                strSQL = "SELECT DISTINCT SDMLATTPAY.ATT_SHIFT_UPLOAD.SU_DOC_NO,SDMLATTPAY.ATT_SHIFT_UPLOAD.SU_DOC_DATE,RECEIVED_DATE FROM SDMLATTPAY.ATT_SHIFT_UPLOAD,SDMLATTPAY.D_COM_DOC_DATA WHERE SDMLATTPAY.D_COM_DOC_DATA.DOC_NO=SDMLATTPAY.ATT_SHIFT_UPLOAD.SU_DOC_NO AND SDMLATTPAY.D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND SDMLATTPAY.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=806 ORDER BY SDMLATTPAY.D_COM_DOC_DATA.RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT SDMLATTPAY.ATT_SHIFT_UPLOAD.SU_DOC_NO,SDMLATTPAY.ATT_SHIFT_UPLOAD.SU_DOC_DATE,RECEIVED_DATE FROM SDMLATTPAY.ATT_SHIFT_UPLOAD,SDMLATTPAY.D_COM_DOC_DATA WHERE SDMLATTPAY.D_COM_DOC_DATA.DOC_NO=SDMLATTPAY.ATT_SHIFT_UPLOAD.SU_DOC_NO AND SDMLATTPAY.D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND SDMLATTPAY.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=806 ORDER BY SDMLATTPAY.ATT_SHIFT_UPLOAD.SU_DOC_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT SDMLATTPAY.ATT_SHIFT_UPLOAD.SU_DOC_NO,SDMLATTPAY.ATT_SHIFT_UPLOAD.SU_DOC_DATE,RECEIVED_DATE FROM SDMLATTPAY.ATT_SHIFT_UPLOAD,SDMLATTPAY.D_COM_DOC_DATA WHERE SDMLATTPAY.D_COM_DOC_DATE.DOC_NO=SDMLATTPAY.ATT_SHIFT_UPLOAD.SU_DOC_NO AND SDMLATTPAY.D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND SDMLATTPAY.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=806 ORDER BY SDMLATTPAY.ATT_SHIFT_UPLOAD.SU_DOC_NO";
            }

            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {

                Counter = Counter + 1;
                clsShiftUpload ObjItem = new clsShiftUpload();

                //------------- Header Fields --------------------//
                ObjItem.setAttribute("DOC_NO", rsTmp.getString("SU_DOC_NO"));
                ObjItem.setAttribute("DOC_DATE", rsTmp.getString("SU_DOC_DATE"));
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
            rsResultSet = Stmt.executeQuery("SELECT * FROM SDMLATTPAY.ATT_SHIFT_UPLOAD_H WHERE SU_DOC_NO='" + pDocNo + "'  GROUP BY SU_DOC_NO ORDER BY REVISION_NO");
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
