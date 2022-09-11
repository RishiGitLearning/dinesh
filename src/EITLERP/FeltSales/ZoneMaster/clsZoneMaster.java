/*
 * clsSales_Party.java
 *
 * Created on April 3, 2009, 10:22 AM
 */
package EITLERP.FeltSales.ZoneMaster;

import java.util.*;
import java.sql.*;
import EITLERP.*;
import java.text.DecimalFormat;
import EITLERP.Production.clsFeltProductionApprovalFlow;

/**
 *
 * @author ashutosh
 */
public class clsZoneMaster {

    public String LastError = "";
    private ResultSet rsResultSet, rsResultSet1;
    private Connection Conn;
    private Statement Stmt;
    DecimalFormat df = new DecimalFormat("##.##");
    private HashMap props;
    public boolean Ready = false;

    private boolean HistoryView = false;
    public static int ModuleID = 876; //72
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
    public clsZoneMaster() {
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
            rsResultSet = Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_ZONE_MASTER  GROUP BY DOC_NO  ORDER BY CREATED_DATE,DOC_NO ");
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

            rsHDetail = stHDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_ZONE_MASTER_H WHERE DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//

            //--------- Generate Gatepass requisition no.  ------------
            setAttribute("DOC_NO", clsFirstFree.getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 876, (int) getAttribute("FFNO").getVal(), true));
            //-------------------------------------------------
            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");

            //rsHistory.updateString("FROM_IP",""+str_split[1]);
            Statement tmpStmt;

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            Conn.setAutoCommit(false);
            rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_ZONE_MASTER WHERE DOC_NO='1'");

            sql = "INSERT INTO PRODUCTION.FELT_ZONE_MASTER "
                    + "(DOC_NO, DOC_DATE, ZONE_CD, ZONE_NAME, ZONE_CATEGORY, INCHARGE_COMMERCIAL, INCHARGE_TECHNICAL, COORDINATOR, REGIONAL_HEAD, "
                    + "OLD_ZONE_CODE, OLD_ZONE_NAME, OLD_ZONE_CATEGORY, OLD_INCHARGE_COMMERCIAL, OLD_INCHARGE_TECHNICAL, OLD_COORDINATOR, OLD_REGIONAL_HEAD, "
                    + "ZONE_STATUS, CANCELED, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, APPROVED, APPROVED_DATE, REJECTED, REJECTED_DATE, "
                    + "HIERARCHY_ID, CHANGED, CHANGED_DATE, REJECTED_REMARKS) ";
            sql = sql + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pstm = Conn.prepareStatement(sql);
            sqlh = "INSERT INTO PRODUCTION.FELT_ZONE_MASTER_H "
                    + "(DOC_NO, DOC_DATE, ZONE_CD, ZONE_NAME, ZONE_CATEGORY, INCHARGE_COMMERCIAL, INCHARGE_TECHNICAL, COORDINATOR, REGIONAL_HEAD, "
                    + "OLD_ZONE_CODE, OLD_ZONE_NAME, OLD_ZONE_CATEGORY, OLD_INCHARGE_COMMERCIAL, OLD_INCHARGE_TECHNICAL, OLD_COORDINATOR, OLD_REGIONAL_HEAD, "
                    + "ZONE_STATUS, CANCELED, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, APPROVED, APPROVED_DATE, REJECTED, REJECTED_DATE, "
                    + "HIERARCHY_ID, CHANGED, CHANGED_DATE, REJECTED_REMARKS,REVISION_NO, UPDATED_BY, APPROVAL_STATUS, ENTRY_DATE, APPROVER_REMARKS,FROM_IP) ";
            sqlh = sqlh + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pstmh = Conn.prepareStatement(sqlh);
            //Now Insert records into detail table
            for (int i = 1; i <= colMRItems.size(); i++) {
                clsZoneMasterItem ObjMRItems = (clsZoneMasterItem) colMRItems.get(Integer.toString(i));
                pstm.setString(1, (String) ObjMRItems.getAttribute("DOC_NO").getObj());
                setAttribute("DOC_NO", ObjMRItems.getAttribute("DOC_NO").getObj().toString());
                pstm.setString(2, (String) ObjMRItems.getAttribute("DOC_DATE").getObj());
                pstm.setString(3, (String) ObjMRItems.getAttribute("ZONE_CD").getObj());
                pstm.setString(4, (String) ObjMRItems.getAttribute("ZONE_NAME").getObj());
                pstm.setString(5, (String) ObjMRItems.getAttribute("ZONE_CATEGORY").getObj());
                pstm.setString(6, (String) ObjMRItems.getAttribute("INCHARGE_COMMERCIAL").getObj());
                pstm.setString(7, (String) ObjMRItems.getAttribute("INCHARGE_TECHNICAL").getObj());
                pstm.setString(8, (String) ObjMRItems.getAttribute("COORDINATOR").getObj());
                pstm.setString(9, (String) ObjMRItems.getAttribute("REGIONAL_HEAD").getObj());
                pstm.setString(10, (String) ObjMRItems.getAttribute("OLD_ZONE_CODE").getObj());
                pstm.setString(11, (String) ObjMRItems.getAttribute("OLD_ZONE_NAME").getObj());
                pstm.setString(12, (String) ObjMRItems.getAttribute("OLD_ZONE_CATEGORY").getObj());
                pstm.setString(13, (String) ObjMRItems.getAttribute("OLD_INCHARGE_COMMERCIAL").getObj());
                pstm.setString(14, (String) ObjMRItems.getAttribute("OLD_INCHARGE_TECHNICAL").getObj());
                pstm.setString(15, (String) ObjMRItems.getAttribute("OLD_COORDINATOR").getObj());
                pstm.setString(16, (String) ObjMRItems.getAttribute("OLD_REGIONAL_HEAD").getObj());
                pstm.setString(17, (String) ObjMRItems.getAttribute("ZONE_STATUS").getObj());
                pstm.setBoolean(18, false);
                pstm.setInt(19, (int) getAttribute("CREATED_BY").getVal());
                pstm.setString(20, (String) getAttribute("CREATED_DATE").getObj());
                pstm.setString(21, null);
                pstm.setString(22, null);
                pstm.setString(23, getAttribute("APPROVAL_STATUS").getString());
                pstm.setString(24, data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                pstm.setBoolean(25, false);
                pstm.setString(26, "0000-00-00");
                pstm.setInt(27, (int) getAttribute("HIERARCHY_ID").getVal());
                pstm.setBoolean(28, true);
                pstm.setString(29, EITLERPGLOBAL.getCurrentDateDB());
                pstm.setString(30, getAttribute("REJECTED_REMARKS").getString());
                pstm.addBatch();

                pstmh.setString(1, (String) ObjMRItems.getAttribute("DOC_NO").getObj());
                pstmh.setString(2, (String) ObjMRItems.getAttribute("DOC_DATE").getObj());
                pstmh.setString(3, (String) ObjMRItems.getAttribute("ZONE_CD").getObj());
                pstmh.setString(4, (String) ObjMRItems.getAttribute("ZONE_NAME").getObj());
                pstmh.setString(5, (String) ObjMRItems.getAttribute("ZONE_CATEGORY").getObj());
                pstmh.setString(6, (String) ObjMRItems.getAttribute("INCHARGE_COMMERCIAL").getObj());
                pstmh.setString(7, (String) ObjMRItems.getAttribute("INCHARGE_TECHNICAL").getObj());
                pstmh.setString(8, (String) ObjMRItems.getAttribute("COORDINATOR").getObj());
                pstmh.setString(9, (String) ObjMRItems.getAttribute("REGIONAL_HEAD").getObj());
                pstmh.setString(10, (String) ObjMRItems.getAttribute("OLD_ZONE_CODE").getObj());
                pstmh.setString(11, (String) ObjMRItems.getAttribute("OLD_ZONE_NAME").getObj());
                pstmh.setString(12, (String) ObjMRItems.getAttribute("OLD_ZONE_CATEGORY").getObj());
                pstmh.setString(13, (String) ObjMRItems.getAttribute("OLD_INCHARGE_COMMERCIAL").getObj());
                pstmh.setString(14, (String) ObjMRItems.getAttribute("OLD_INCHARGE_TECHNICAL").getObj());
                pstmh.setString(15, (String) ObjMRItems.getAttribute("OLD_COORDINATOR").getObj());
                pstmh.setString(16, (String) ObjMRItems.getAttribute("OLD_REGIONAL_HEAD").getObj());
                pstmh.setString(17, (String) ObjMRItems.getAttribute("ZONE_STATUS").getObj());
                pstmh.setBoolean(18, false);
                pstmh.setInt(19, (int) getAttribute("CREATED_BY").getVal());
                pstmh.setString(20, (String) getAttribute("CREATED_DATE").getObj());
                pstmh.setString(21, null);
                pstmh.setString(22, null);
                pstmh.setString(23, getAttribute("APPROVAL_STATUS").getString());
                pstmh.setString(24, data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                pstmh.setBoolean(25, false);
                pstmh.setString(26, "0000-00-00");
                pstmh.setInt(27, (int) getAttribute("HIERARCHY_ID").getVal());
                pstmh.setBoolean(28, true);
                pstmh.setString(29, EITLERPGLOBAL.getCurrentDateDB());
                pstmh.setString(30, getAttribute("REJECTED_REMARKS").getString());
                pstmh.setInt(31, 1);
                pstmh.setInt(32, (int) getAttribute("CREATED_BY").getVal());
                pstmh.setString(33, getAttribute("APPROVAL_STATUS").getString());
                pstmh.setString(34, data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                pstmh.setString(35, getAttribute("APPROVER_REMARKS").getString());
                pstmh.setString(36, str_split[1]);
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
            setAttribute("FROM", EITLERPGLOBAL.gNewUserID);

            //ApprovalFlow ObjFlow=new ApprovalFlow();
            clsFeltProductionApprovalFlow ObjFlow = new clsFeltProductionApprovalFlow();

            ObjFlow.ModuleID = clsZoneMaster.ModuleID;
            ObjFlow.DocNo = (String) getAttribute("DOC_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("CREATED_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "PRODUCTION.FELT_DETAILFELT_ZONE_MASTER";
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
            
            if (ObjFlow.Status.equalsIgnoreCase("F")) {
                data.Execute("INSERT INTO PRODUCTION.FELT_INCHARGE "
                        + "(INCHARGE_CD, INCHARGE_NAME, INCHARGE_ACTIVE, INCHARGE_CATEGORY, INCHARGE_COMMERCIAL, INCHARGE_TECHNICAL, INCHARGE_COORDINATOR, REGIONAL_HEAD) "
                        + "SELECT  ZONE_CD, ZONE_NAME, 'Y',ZONE_CATEGORY, INCHARGE_COMMERCIAL, INCHARGE_TECHNICAL, COORDINATOR, REGIONAL_HEAD  "
                        + "FROM PRODUCTION.FELT_ZONE_MASTER WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "'  AND ZONE_STATUS='ADD'");
                data.Execute("UPDATE PRODUCTION.FELT_INCHARGE "
                        + "SET INCHARGE_ACTIVE='N' "
                        + "WHERE INCHARGE_CD IN ( "
                        + "SELECT ZONE_CD FROM PRODUCTION.FELT_ZONE_MASTER WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "'  "
                        + "AND ZONE_STATUS='DELETE')");
                data.Execute("UPDATE PRODUCTION.FELT_INCHARGE I,PRODUCTION.FELT_ZONE_MASTER M "
                        + "SET I.INCHARGE_CATEGORY=M.ZONE_CATEGORY,I.INCHARGE_COMMERCIAL=M.INCHARGE_COMMERCIAL,I.INCHARGE_TECHNICAL=M.INCHARGE_TECHNICAL,"
                        + "I.INCHARGE_COORDINATOR=M.COORDINATOR,I.REGIONAL_HEAD=M.REGIONAL_HEAD "
                        + "WHERE M.DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "'  AND ZONE_STATUS='UPDATE' AND I.INCHARGE_CD=M.ZONE_CD");
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
            data.Execute("DELETE FROM PRODUCTION.FELT_ZONE_MASTER WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "'");
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

            rsHDetail = stHDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_ZONE_MASTER_H WHERE DOC_NO='1'");
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
            rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_ZONE_MASTER WHERE DOC_NO='1'");

            sql = "INSERT INTO PRODUCTION.FELT_ZONE_MASTER "
                    + "(DOC_NO, DOC_DATE, ZONE_CD, ZONE_NAME, ZONE_CATEGORY, INCHARGE_COMMERCIAL, INCHARGE_TECHNICAL, COORDINATOR, REGIONAL_HEAD, "
                    + "OLD_ZONE_CODE, OLD_ZONE_NAME, OLD_ZONE_CATEGORY, OLD_INCHARGE_COMMERCIAL, OLD_INCHARGE_TECHNICAL, OLD_COORDINATOR, OLD_REGIONAL_HEAD, "
                    + "ZONE_STATUS, CANCELED, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, APPROVED, APPROVED_DATE, REJECTED, REJECTED_DATE, "
                    + "HIERARCHY_ID, CHANGED, CHANGED_DATE, REJECTED_REMARKS) ";
            sql = sql + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pstm = Conn.prepareStatement(sql);
            sqlh = "INSERT INTO PRODUCTION.FELT_ZONE_MASTER_H "
                    + "(DOC_NO, DOC_DATE, ZONE_CD, ZONE_NAME, ZONE_CATEGORY, INCHARGE_COMMERCIAL, INCHARGE_TECHNICAL, COORDINATOR, REGIONAL_HEAD, "
                    + "OLD_ZONE_CODE, OLD_ZONE_NAME, OLD_ZONE_CATEGORY, OLD_INCHARGE_COMMERCIAL, OLD_INCHARGE_TECHNICAL, OLD_COORDINATOR, OLD_REGIONAL_HEAD, "
                    + "ZONE_STATUS, CANCELED, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, APPROVED, APPROVED_DATE, REJECTED, REJECTED_DATE, "
                    + "HIERARCHY_ID, CHANGED, CHANGED_DATE, REJECTED_REMARKS,REVISION_NO, UPDATED_BY, APPROVAL_STATUS, ENTRY_DATE, APPROVER_REMARKS,FROM_IP) ";
            sqlh = sqlh + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pstmh = Conn.prepareStatement(sqlh);
            int RevNo = 0;
            //Now Insert records into detail table
            for (int i = 1; i <= colMRItems.size(); i++) {
                clsZoneMasterItem ObjMRItems = (clsZoneMasterItem) colMRItems.get(Integer.toString(i));
                if (i == 1) {
                    RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_ZONE_MASTER_H WHERE DOC_NO='" + (String) ObjMRItems.getAttribute("DOC_NO").getObj() + "'");
                    RevNo++;
                }
                pstm.setString(1, (String) ObjMRItems.getAttribute("DOC_NO").getObj());
                setAttribute("DOC_NO", ObjMRItems.getAttribute("DOC_NO").getObj().toString());
                pstm.setString(2, (String) ObjMRItems.getAttribute("DOC_DATE").getObj());
                pstm.setString(3, (String) ObjMRItems.getAttribute("ZONE_CD").getObj());
                pstm.setString(4, (String) ObjMRItems.getAttribute("ZONE_NAME").getObj());
                pstm.setString(5, (String) ObjMRItems.getAttribute("ZONE_CATEGORY").getObj());
                pstm.setString(6, (String) ObjMRItems.getAttribute("INCHARGE_COMMERCIAL").getObj());
                pstm.setString(7, (String) ObjMRItems.getAttribute("INCHARGE_TECHNICAL").getObj());
                pstm.setString(8, (String) ObjMRItems.getAttribute("COORDINATOR").getObj());
                pstm.setString(9, (String) ObjMRItems.getAttribute("REGIONAL_HEAD").getObj());
                pstm.setString(10, (String) ObjMRItems.getAttribute("OLD_ZONE_CODE").getObj());
                pstm.setString(11, (String) ObjMRItems.getAttribute("OLD_ZONE_NAME").getObj());
                pstm.setString(12, (String) ObjMRItems.getAttribute("OLD_ZONE_CATEGORY").getObj());
                pstm.setString(13, (String) ObjMRItems.getAttribute("OLD_INCHARGE_COMMERCIAL").getObj());
                pstm.setString(14, (String) ObjMRItems.getAttribute("OLD_INCHARGE_TECHNICAL").getObj());
                pstm.setString(15, (String) ObjMRItems.getAttribute("OLD_COORDINATOR").getObj());
                pstm.setString(16, (String) ObjMRItems.getAttribute("OLD_REGIONAL_HEAD").getObj());
                pstm.setString(17, (String) ObjMRItems.getAttribute("ZONE_STATUS").getObj());
                pstm.setBoolean(18, false);
                pstm.setInt(19, (int) getAttribute("CREATED_BY").getVal());
                pstm.setString(20, (String) getAttribute("CREATED_DATE").getObj());
                pstm.setString(21, null);
                pstm.setString(22, null);
                pstm.setString(23, getAttribute("APPROVAL_STATUS").getString());
                pstm.setString(24, data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                pstm.setBoolean(25, false);
                pstm.setString(26, "0000-00-00");
                pstm.setInt(27, (int) getAttribute("HIERARCHY_ID").getVal());
                pstm.setBoolean(28, true);
                pstm.setString(29, EITLERPGLOBAL.getCurrentDateDB());
                pstm.setString(30, getAttribute("REJECTED_REMARKS").getString());
                pstm.addBatch();

                pstmh.setString(1, (String) ObjMRItems.getAttribute("DOC_NO").getObj());
                pstmh.setString(2, (String) ObjMRItems.getAttribute("DOC_DATE").getObj());
                pstmh.setString(3, (String) ObjMRItems.getAttribute("ZONE_CD").getObj());
                pstmh.setString(4, (String) ObjMRItems.getAttribute("ZONE_NAME").getObj());
                pstmh.setString(5, (String) ObjMRItems.getAttribute("ZONE_CATEGORY").getObj());
                pstmh.setString(6, (String) ObjMRItems.getAttribute("INCHARGE_COMMERCIAL").getObj());
                pstmh.setString(7, (String) ObjMRItems.getAttribute("INCHARGE_TECHNICAL").getObj());
                pstmh.setString(8, (String) ObjMRItems.getAttribute("COORDINATOR").getObj());
                pstmh.setString(9, (String) ObjMRItems.getAttribute("REGIONAL_HEAD").getObj());
                pstmh.setString(10, (String) ObjMRItems.getAttribute("OLD_ZONE_CODE").getObj());
                pstmh.setString(11, (String) ObjMRItems.getAttribute("OLD_ZONE_NAME").getObj());
                pstmh.setString(12, (String) ObjMRItems.getAttribute("OLD_ZONE_CATEGORY").getObj());
                pstmh.setString(13, (String) ObjMRItems.getAttribute("OLD_INCHARGE_COMMERCIAL").getObj());
                pstmh.setString(14, (String) ObjMRItems.getAttribute("OLD_INCHARGE_TECHNICAL").getObj());
                pstmh.setString(15, (String) ObjMRItems.getAttribute("OLD_COORDINATOR").getObj());
                pstmh.setString(16, (String) ObjMRItems.getAttribute("OLD_REGIONAL_HEAD").getObj());
                pstmh.setString(17, (String) ObjMRItems.getAttribute("ZONE_STATUS").getObj());
                pstmh.setBoolean(18, false);
                pstmh.setInt(19, (int) getAttribute("CREATED_BY").getVal());
                pstmh.setString(20, (String) getAttribute("CREATED_DATE").getObj());
                pstmh.setString(21, null);
                pstmh.setString(22, null);
                pstmh.setString(23, getAttribute("APPROVAL_STATUS").getString());
                pstmh.setString(24, data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                pstmh.setBoolean(25, false);
                pstmh.setString(26, "0000-00-00");
                pstmh.setInt(27, (int) getAttribute("HIERARCHY_ID").getVal());
                pstmh.setBoolean(28, true);
                pstmh.setString(29, EITLERPGLOBAL.getCurrentDateDB());
                pstmh.setString(30, getAttribute("REJECTED_REMARKS").getString());
                pstmh.setInt(31, RevNo);
                pstmh.setInt(32, (int) getAttribute("CREATED_BY").getVal());
                pstmh.setString(33, getAttribute("APPROVAL_STATUS").getString());
                pstmh.setString(34, data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                pstmh.setString(35, getAttribute("APPROVER_REMARKS").getString());
                pstmh.setString(36, str_split[1]);
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
            s = "UPDATE PRODUCTION.FELT_ZONE_MASTER D,PRODUCTION.FELT_ZONE_MASTER_H H "
                    + "SET D.CREATED_BY=H.CREATED_BY,D.CREATED_DATE=H.CREATED_DATE "
                    + "WHERE D.DOC_NO=H.DOC_NO AND REVISION_NO=1 AND "
                    + "D.DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "'";
            data.Execute(s);

            //===================== Update the Approval Flow ======================//
            setAttribute("FROM", EITLERPGLOBAL.gUserID);

            //ApprovalFlow ObjFlow=new ApprovalFlow();
            clsFeltProductionApprovalFlow ObjFlow = new clsFeltProductionApprovalFlow();

            ObjFlow.ModuleID = clsZoneMaster.ModuleID;
            ObjFlow.DocNo = (String) getAttribute("DOC_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("MODIFIED_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "PRODUCTION.FELT_ZONE_MASTER";
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
            //==== Handling Rejected Documents ==========//
            boolean IsRejected = getAttribute("REJECTED").getBool();

            if (IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.FELT_ZONE_MASTER SET REJECTED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='" + ObjFlow.DocNo + "' ");

                //Remove Old Records from FELT_DOC_DATA
                data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=" + clsZoneMaster.ModuleID + " AND DOC_NO='" + ObjFlow.DocNo + "'");

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

            if (ObjFlow.Status.equalsIgnoreCase("F")) {
                data.Execute("INSERT INTO PRODUCTION.FELT_INCHARGE "
                        + "(INCHARGE_CD, INCHARGE_NAME, INCHARGE_ACTIVE, INCHARGE_CATEGORY, INCHARGE_COMMERCIAL, INCHARGE_TECHNICAL, INCHARGE_COORDINATOR, REGIONAL_HEAD) "
                        + "SELECT  ZONE_CD, ZONE_NAME, 'Y',ZONE_CATEGORY, INCHARGE_COMMERCIAL, INCHARGE_TECHNICAL, COORDINATOR, REGIONAL_HEAD  "
                        + "FROM PRODUCTION.FELT_ZONE_MASTER WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "'  AND ZONE_STATUS='ADD'");
                data.Execute("UPDATE PRODUCTION.FELT_INCHARGE "
                        + "SET INCHARGE_ACTIVE='N' "
                        + "WHERE INCHARGE_CD IN ( "
                        + "SELECT ZONE_CD FROM PRODUCTION.FELT_ZONE_MASTER WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "'  "
                        + "AND ZONE_STATUS='DELETE')");
                data.Execute("UPDATE PRODUCTION.FELT_INCHARGE I,PRODUCTION.FELT_ZONE_MASTER M "
                        + "SET I.INCHARGE_CATEGORY=M.ZONE_CATEGORY,I.INCHARGE_COMMERCIAL=M.INCHARGE_COMMERCIAL,I.INCHARGE_TECHNICAL=M.INCHARGE_TECHNICAL,"
                        + "I.INCHARGE_COORDINATOR=M.COORDINATOR,I.REGIONAL_HEAD=M.REGIONAL_HEAD "
                        + "WHERE M.DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "'  AND ZONE_STATUS='UPDATE' AND I.INCHARGE_CD=M.ZONE_CD");
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
                rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_ZONE_MASTER_H WHERE DOC_NO='" + mDocNo + "' AND REVISION_NO=" + RevNo + "  ORDER BY DOC_NO");
            } else {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_ZONE_MASTER WHERE DOC_NO='" + mDocNo + "'  ORDER BY DOC_NO");
            }
            rsTmp.first();

            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {
                Counter = Counter + 1;
                clsZoneMasterItem ObjMRItems = new clsZoneMasterItem();
                ObjMRItems.setAttribute("DOC_NO", rsTmp.getString("DOC_NO"));
                ObjMRItems.setAttribute("DOC_DATE", rsTmp.getString("DOC_DATE"));
                ObjMRItems.setAttribute("ZONE_CD", rsTmp.getString("ZONE_CD"));
                ObjMRItems.setAttribute("ZONE_NAME", rsTmp.getString("ZONE_NAME"));
                ObjMRItems.setAttribute("ZONE_CATEGORY", rsTmp.getString("ZONE_CATEGORY"));
                ObjMRItems.setAttribute("INCHARGE_COMMERCIAL", rsTmp.getString("INCHARGE_COMMERCIAL"));
                ObjMRItems.setAttribute("INCHARGE_TECHNICAL", rsTmp.getString("INCHARGE_TECHNICAL"));
                ObjMRItems.setAttribute("COORDINATOR", rsTmp.getString("COORDINATOR"));
                ObjMRItems.setAttribute("REGIONAL_HEAD", rsTmp.getString("REGIONAL_HEAD"));
                ObjMRItems.setAttribute("OLD_ZONE_CODE", rsTmp.getString("OLD_ZONE_CODE"));
                ObjMRItems.setAttribute("OLD_ZONE_NAME", rsTmp.getString("OLD_ZONE_NAME"));
                ObjMRItems.setAttribute("OLD_ZONE_CATEGORY", rsTmp.getString("OLD_ZONE_CATEGORY"));
                ObjMRItems.setAttribute("OLD_INCHARGE_COMMERCIAL", rsTmp.getString("OLD_INCHARGE_COMMERCIAL"));
                ObjMRItems.setAttribute("OLD_INCHARGE_TECHNICAL", rsTmp.getString("OLD_INCHARGE_TECHNICAL"));
                ObjMRItems.setAttribute("OLD_COORDINATOR", rsTmp.getString("OLD_COORDINATOR"));
                ObjMRItems.setAttribute("OLD_REGIONAL_HEAD", rsTmp.getString("OLD_REGIONAL_HEAD"));
                ObjMRItems.setAttribute("ZONE_STATUS", rsTmp.getString("ZONE_STATUS"));
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_ZONE_MASTER WHERE DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=876 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND STATUS='W'";
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
            String strSQL = "SELECT * FROM PRODUCTION.FELT_ZONE_MASTER_H WHERE DOC_NO='" + DocNo + "' GROUP BY REVISION_NO,DOC_NO ";
            rsTmp = data.getResult(strSQL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                while (!rsTmp.isAfterLast()) {
                    clsZoneMaster objParty = new clsZoneMaster();

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
            strSQL += "SELECT * FROM PRODUCTION.FELT_ZONE_MASTER WHERE " + Condition + " GROUP BY DOC_NO";
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery(strSQL);

            if (!rsResultSet.first()) {
                strSQL = "SELECT * FROM PRODUCTION.FELT_ZONE_MASTER  GROUP BY DOC_NO,YEAR_FROM  ORDER BY DOC_NO";
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_ZONE_MASTER WHERE DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=876 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND TYPE='C' AND STATUS='W'";
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

    public static void CancelDoc(String pAmendNo) {
        ResultSet rsTmp = null;

        if (CanCancel(pAmendNo)) {
            boolean Approved = false;

            try {
                rsTmp = data.getResult("SELECT APPROVED FROM PRODUCTION.FELT_ZONE_MASTER WHERE DOC_NO='" + pAmendNo + "' ");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    Approved = rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();

                if (!Approved) {
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='" + pAmendNo + "' AND MODULE_ID=876");
                }
                data.Execute("UPDATE PRODUCTION.FELT_ZONE_MASTER SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='" + pAmendNo + "'");

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
            System.out.println("SELECT DOC_NO FROM PRODUCTION.FELT_ZONE_MASTER WHERE DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");
            rsTmp = stTmp.executeQuery("SELECT DOC_NO FROM PRODUCTION.FELT_ZONE_MASTER WHERE DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");

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

    //Deletes current record
    public boolean Delete(int pUserID) {
        try {
            int lCompanyID = EITLERPGLOBAL.gCompanyID;
            String lDocNo = (String) getAttribute("DOC_NO").getObj();
            String strSQL = "";

            //First check that record is editable
            if (CanDelete(lCompanyID, lDocNo, pUserID)) {
                String strQry = "DELETE FROM PRODUCTION.FELT_ZONE_MASTER WHERE DOC_NO='" + lDocNo + "'";
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
                strSQL = "SELECT DISTINCT PRODUCTION.FELT_ZONE_MASTER.DOC_NO,PRODUCTION.FELT_ZONE_MASTER.CREATED_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_ZONE_MASTER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_ZONE_MASTER.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=876 ORDER BY PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT PRODUCTION.FELT_ZONE_MASTER.DOC_NO,PRODUCTION.FELT_ZONE_MASTER.CREATED_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_ZONE_MASTER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_ZONE_MASTER.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=876 ORDER BY PRODUCTION.FELT_ZONE_MASTER.CREATED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT PRODUCTION.FELT_ZONE_MASTER.DOC_NO,PRODUCTION.FELT_ZONE_MASTER.CREATED_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_ZONE_MASTER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_ZONE_MASTER.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=876 ORDER BY PRODUCTION.FELT_ZONE_MASTER.DOC_NO";
            }

            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {

                Counter = Counter + 1;
                clsZoneMaster ObjItem = new clsZoneMaster();

                //------------- Header Fields --------------------//
                ObjItem.setAttribute("DOC_NO", rsTmp.getString("DOC_NO"));
                ObjItem.setAttribute("DOC_DATE", rsTmp.getString("CREATED_DATE"));
                ObjItem.setAttribute("RECEIVED_DATE", rsTmp.getString("RECEIVED_DATE"));

//                ObjItem.setAttribute("DOC_DATE", rsTmp.getString("CREATED_DATE"));
//                ObjItem.setAttribute("RECEIVED_DATE", rsTmp.getString("RECEIVED_DATE"));
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
            rsResultSet = Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_ZONE_MASTER_H WHERE DOC_NO='" + pDocNo + "'  GROUP BY DOC_NO ORDER BY REVISION_NO");
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
