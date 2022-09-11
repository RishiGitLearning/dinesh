/*
 * clsSales_Party.java
 *
 * Created on April 3, 2009, 10:22 AM
 */
package EITLERP.FeltSales.Budget;

import java.util.*;
import java.sql.*;
import EITLERP.*;
import java.text.DecimalFormat;
import EITLERP.Production.clsFeltProductionApprovalFlow;

/**
 *
 * @author ashutosh
 */
public class clsNewBudgetEntry {

    public String LastError = "";
    private ResultSet rsResultSet, rsResultSet1;
    private Connection Conn;
    private Statement Stmt;
    DecimalFormat df = new DecimalFormat("##.##");
    private HashMap props;
    public boolean Ready = false;

    private boolean HistoryView = false;
    public static int ModuleID = 872; //825 // //72
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
    public clsNewBudgetEntry() {
        LastError = "";
        props = new HashMap();
        //props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("DOC_NO", new Variant(""));
        props.put("YEAR_FROM", new Variant(""));
        props.put("YEAR_TO", new Variant(""));
        props.put("PARTY_NAME", new Variant(""));
        props.put("PATY_GROUP", new Variant(""));
        props.put("received_date", new Variant(""));
        props.put("action_date", new Variant(""));
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
            rsResultSet = Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL  "
                    + " WHERE DOC_NO LIKE 'N%' AND YEAR_FROM=" + EITLERPGLOBAL.FinYearTo + " "
                    + "  GROUP BY DOC_NO,YEAR_FROM  ORDER BY CREATED_DATE,DOC_NO ");
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
        return false;
    }

    public boolean Update() {

        Statement stHistory, stHeader, stHDetail;
        ResultSet rsHistory, rsHeader, rsHDetail;
        boolean Validate = true;
        String sql, sqlh;
        PreparedStatement pstm = null, pstmh = null;
        try {

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

            rsHDetail = stHDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL_H WHERE DOC_NO='1'");
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
            //rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE DOC_NO='1'");
            //int mcurmonth = data.getIntValueFromDB("SELECT MONTH(CURDATE()) FROM DUAL");
            int mcurmonth = Integer.parseInt(getAttribute("DOC_NO").getObj().toString().substring(5, 7));
            //int mcurmonth = 4;
            HashMap mmonth = new HashMap();
            mmonth.put(4, "APR");
            mmonth.put(1, "JAN");
            mmonth.put(2, "FEB");
            mmonth.put(3, "MAR");
            mmonth.put(5, "MAY");
            mmonth.put(6, "JUN");
            mmonth.put(7, "JUL");
            mmonth.put(8, "AUG");
            mmonth.put(9, "SEP");
            mmonth.put(10, "OCT");
            mmonth.put(11, "NOV");
            mmonth.put(12, "DEC");
            sql = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                    + "SET ACTUAL_BUDGET=?,CURRENT_PROJECTION=?,CURRENT_PROJECTION_VALUE=?, ";
            for (int a = 4; a <= 12; a++) {
                sql = sql + mmonth.get(a).toString() + "_BUDGET=?,";
            }
            for (int a = 1; a <= 3; a++) {
                sql = sql + mmonth.get(a).toString() + "_BUDGET=?,";
            }
            sql = sql + "MRF=?,MODIFIED_BY=?,MODIFIED_DATE=? "
                    + "WHERE UPN=? AND DOC_NO=? AND YEAR_FROM=?";

            pstm = Conn.prepareStatement(sql);
            sqlh = "INSERT INTO PRODUCTION.FELT_BUDGET_REVIEW_DETAIL_H "
                    + "(DOC_NO,YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,"
                    + "ACTUAL_BUDGET,CURRENT_PROJECTION,CURRENT_PROJECTION_VALUE,"
                    + "HIERARCHY_ID,MODIFIED_BY,MODIFIED_DATE,REJECTED_REMARKS,REVISION_NO,APPROVAL_STATUS,ENTRY_DATE,"
                    + "APPROVER_REMARKS,FROM_IP,UPDATED_BY,";
            for (int a = 4; a <= 12; a++) {
                sqlh = sqlh + mmonth.get(a).toString() + "_BUDGET,";
            }
            for (int a = 1; a <= 3; a++) {
                sqlh = sqlh + mmonth.get(a).toString() + "_BUDGET,";
            }
            sqlh = sqlh + "MRF,UPN)";
            sqlh = sqlh + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pstmh = Conn.prepareStatement(sqlh);
            int RevNo = 0;
            //Now Insert records into detail table
            for (int i = 1; i <= colMRItems.size(); i++) {
                clsBudgetReviewEntryItem ObjMRItems = (clsBudgetReviewEntryItem) colMRItems.get(Integer.toString(i));
                if (i == 1) {
                    RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL_H WHERE DOC_NO='" + (String) ObjMRItems.getAttribute("DOC_NO").getObj() + "'");
                    RevNo++;
                }

                setAttribute("DOC_NO", ObjMRItems.getAttribute("DOC_NO").getObj().toString());
                pstm.setDouble(1, (double) ObjMRItems.getAttribute("ACTUAL_BUDGET").getVal());
                pstm.setDouble(2, (double) ObjMRItems.getAttribute("CURRENT_PROJECTION").getVal());
                pstm.setDouble(3, (double) ObjMRItems.getAttribute("CURRENT_PROJECTION_VALUE").getVal());
                int stcol = 4;
                for (int a = 4; a <= 12; a++) {
                    pstm.setDouble(stcol, (double) ObjMRItems.getAttribute(mmonth.get(a).toString() + "_BUDGET").getVal());
                    stcol++;
                }
                for (int a = 1; a <= 3; a++) {
                    pstm.setDouble(stcol, (double) ObjMRItems.getAttribute(mmonth.get(a).toString() + "_BUDGET").getVal());
                    stcol++;
                }
                pstm.setString(stcol, ObjMRItems.getAttribute("MRF").getObj().toString());
                stcol++;
                pstm.setInt(stcol, (int) getAttribute("MODIFIED_BY").getVal());
                stcol++;
                pstm.setString(stcol, EITLERPGLOBAL.getCurrentDateTimeDB());
                stcol++;
                pstm.setString(stcol, (String) ObjMRItems.getAttribute("UPN").getObj());
                stcol++;
                pstm.setString(stcol, ObjMRItems.getAttribute("DOC_NO").getObj().toString());
                stcol++;
                pstm.setString(stcol, (String) ObjMRItems.getAttribute("YEAR_FROM").getObj());
                stcol++;

                //System.out.println("Query" + pstm.toString());
                pstm.addBatch();

                pstmh.setString(1, (String) ObjMRItems.getAttribute("DOC_NO").getObj());
                pstmh.setString(2, (String) ObjMRItems.getAttribute("YEAR_FROM").getObj());
                pstmh.setString(3, (String) ObjMRItems.getAttribute("YEAR_TO").getObj());
                pstmh.setString(4, (String) ObjMRItems.getAttribute("PARTY_CODE").getObj());
                pstmh.setString(5, (String) ObjMRItems.getAttribute("PARTY_NAME").getObj());
                pstmh.setString(6, (String) ObjMRItems.getAttribute("MACHINE_NO").getObj());
                pstmh.setString(7, (String) ObjMRItems.getAttribute("POSITION_NO").getObj());
                pstmh.setString(8, (String) ObjMRItems.getAttribute("POSITION_DESC").getObj());
                pstmh.setDouble(9, (double) ObjMRItems.getAttribute("ACTUAL_BUDGET").getVal());
                pstmh.setDouble(10, (double) ObjMRItems.getAttribute("CURRENT_PROJECTION").getVal());
                pstmh.setDouble(11, (double) ObjMRItems.getAttribute("CURRENT_PROJECTION_VALUE").getVal());
                pstmh.setInt(12, (int) getAttribute("HIERARCHY_ID").getVal());
                pstmh.setInt(13, (int) getAttribute("MODIFIED_BY").getVal());
                pstmh.setString(14, (String) getAttribute("MODIFIED_DATE").getObj());
                pstmh.setString(15, getAttribute("REJECTED_REMARKS").getString());
                pstmh.setInt(16, RevNo);
                pstmh.setString(17, getAttribute("APPROVAL_STATUS").getString());
                pstmh.setString(18, data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                pstmh.setString(19, getAttribute("APPROVER_REMARKS").getString());
                pstmh.setString(20, str_split[1]);
                pstmh.setInt(21, (int) getAttribute("MODIFIED_BY").getVal());
                stcol = 22;
                for (int a = 4; a <= 12; a++) {
                    pstmh.setDouble(stcol, (double) ObjMRItems.getAttribute(mmonth.get(a).toString() + "_BUDGET").getVal());
                    stcol++;
                }
                for (int a = 1; a <= 3; a++) {
                    pstmh.setDouble(stcol, (double) ObjMRItems.getAttribute(mmonth.get(a).toString() + "_BUDGET").getVal());
                    stcol++;
                }
                pstmh.setString(stcol, ObjMRItems.getAttribute("MRF").getObj().toString());
                stcol++;
                pstmh.setString(stcol, (String) ObjMRItems.getAttribute("UPN").getObj());
                stcol++;

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
            s = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D,PRODUCTION.FELT_BUDGET_REVIEW_DETAIL_H H "
                    + "SET D.CREATED_BY=H.CREATED_BY,D.CREATED_DATE=H.CREATED_DATE "
                    + "WHERE D.DOC_NO=H.DOC_NO AND REVISION_NO=1 AND "
                    + "D.DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "'";
            data.Execute(s);
            s = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                    + "SET CURRENT_PROJECTION_VALUE=SELLING_PRICE*CURRENT_PROJECTION,"
                    + "KEY1=CONCAT(TRIM(YEAR_FROM),TRIM(YEAR_TO),TRIM(PARTY_CODE),TRIM(MACHINE_NO),TRIM(POSITION_NO)) "
                    + "WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "' ";
            data.Execute(s);
            //Update_Budget((String) getAttribute("DOC_NO").getObj());

            //===================== Update the Approval Flow ======================//
            //setAttribute("FROM", EITLERPGLOBAL.gUserID);
            //ApprovalFlow ObjFlow=new ApprovalFlow();
            clsFeltProductionApprovalFlow ObjFlow = new clsFeltProductionApprovalFlow();

            ObjFlow.ModuleID = clsNewBudgetEntry.ModuleID;
            ObjFlow.DocNo = (String) getAttribute("DOC_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("MODIFIED_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "PRODUCTION.FELT_BUDGET_REVIEW_DETAIL";
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
                data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL SET REJECTED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='" + ObjFlow.DocNo + "' ");

                //Remove Old Records from FELT_DOC_DATA
                data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=" + clsNewBudgetEntry.ModuleID + " AND DOC_NO='" + ObjFlow.DocNo + "'");

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
            setAttribute("MARK_AS_COMPLETE", rsResultSet.getInt("MARK_AS_COMPLETE"));
            colMRItems.clear();

            String mDocNo = (String) getAttribute("DOC_NO").getObj();

            tmpStmt = tmpConn.createStatement();
            if (HistoryView) {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL_H WHERE DOC_NO='" + mDocNo + "' AND REVISION_NO=" + RevNo + "  ORDER BY DOC_NO");
            } else {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE DOC_NO='" + mDocNo + "'  ORDER BY DOC_NO");
            }
            rsTmp.first();

            Counter = 0;
            HashMap mmonth = new HashMap();
            mmonth.put(4, "APR");
            mmonth.put(5, "MAY");
            mmonth.put(6, "JUN");
            mmonth.put(7, "JUL");
            mmonth.put(8, "AUG");
            mmonth.put(9, "SEP");
            mmonth.put(10, "OCT");
            mmonth.put(11, "NOV");
            mmonth.put(12, "DEC");
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {
                Counter = Counter + 1;
                clsBudgetReviewEntryItem ObjMRItems = new clsBudgetReviewEntryItem();
                ObjMRItems.setAttribute("DOC_NO", rsTmp.getString("DOC_NO"));
                ObjMRItems.setAttribute("YEAR_FROM", rsTmp.getString("YEAR_FROM"));
                ObjMRItems.setAttribute("YEAR_TO", rsTmp.getString("YEAR_TO"));
                ObjMRItems.setAttribute("PARTY_CODE", rsTmp.getString("PARTY_CODE"));
                ObjMRItems.setAttribute("PARTY_NAME", rsTmp.getString("PARTY_NAME"));
                ObjMRItems.setAttribute("MACHINE_NO", rsTmp.getString("MACHINE_NO"));
                ObjMRItems.setAttribute("POSITION_NO", rsTmp.getString("POSITION_NO"));
                ObjMRItems.setAttribute("POSITION_DESC", rsTmp.getString("POSITION_DESC"));
                ObjMRItems.setAttribute("STYLE", rsTmp.getString("STYLE"));
                ObjMRItems.setAttribute("PRESS_LENGTH", EITLERPGLOBAL.round(rsTmp.getDouble("PRESS_LENGTH"), 2));
                ObjMRItems.setAttribute("PRESS_WIDTH", EITLERPGLOBAL.round(rsTmp.getDouble("PRESS_WIDTH"), 2));
                ObjMRItems.setAttribute("PRESS_GSM", EITLERPGLOBAL.round(rsTmp.getDouble("PRESS_GSM"), 2));
                ObjMRItems.setAttribute("PRESS_WEIGHT", EITLERPGLOBAL.round(rsTmp.getDouble("PRESS_WEIGHT"), 2));
                ObjMRItems.setAttribute("PRESS_SQMTR", EITLERPGLOBAL.round(rsTmp.getDouble("PRESS_SQMTR"), 2));
                ObjMRItems.setAttribute("DRY_LENGTH", EITLERPGLOBAL.round(rsTmp.getDouble("DRY_LENGTH"), 2));
                ObjMRItems.setAttribute("DRY_WIDTH", EITLERPGLOBAL.round(rsTmp.getDouble("DRY_WIDTH"), 2));
                ObjMRItems.setAttribute("DRY_SQMTR", EITLERPGLOBAL.round(rsTmp.getDouble("DRY_SQMTR"), 2));
                ObjMRItems.setAttribute("DRY_WEIGHT", EITLERPGLOBAL.round(rsTmp.getDouble("DRY_WEIGHT"), 2));
                ObjMRItems.setAttribute("QUALITY_NO", rsTmp.getString("QUALITY_NO"));
                ObjMRItems.setAttribute("GROUP_NAME", rsTmp.getString("GROUP_NAME"));
                ObjMRItems.setAttribute("SELLING_PRICE", EITLERPGLOBAL.round(rsTmp.getDouble("SELLING_PRICE"), 2));
                ObjMRItems.setAttribute("SPL_DISCOUNT", EITLERPGLOBAL.round(rsTmp.getDouble("SPL_DISCOUNT"), 2));
                ObjMRItems.setAttribute("WIP_QTY", EITLERPGLOBAL.round(rsTmp.getDouble("WIP_QTY"), 2));
                ObjMRItems.setAttribute("WIP_VALUE", EITLERPGLOBAL.round(rsTmp.getDouble("WIP_VALUE"), 2));
                ObjMRItems.setAttribute("OBSOLETE_QTY", EITLERPGLOBAL.round(rsTmp.getDouble("WIP_QTY"), 2));
                ObjMRItems.setAttribute("OBSOLETE_VALUE", EITLERPGLOBAL.round(rsTmp.getDouble("WIP_VALUE"), 2));
                ObjMRItems.setAttribute("CANCEL_QTY", EITLERPGLOBAL.round(rsTmp.getDouble("CANCEL_QTY"), 2));
                ObjMRItems.setAttribute("CANCEL_VALUE", EITLERPGLOBAL.round(rsTmp.getDouble("CANCEL_VALUE"), 2));
                ObjMRItems.setAttribute("ACESS_QTY", EITLERPGLOBAL.round(rsTmp.getDouble("ACESS_QTY"), 2));
                ObjMRItems.setAttribute("ACESS_VALUE", EITLERPGLOBAL.round(rsTmp.getDouble("ACESS_VALUE"), 2));
                ObjMRItems.setAttribute("STOCK_QTY", EITLERPGLOBAL.round(rsTmp.getDouble("STOCK_QTY"), 2));
                ObjMRItems.setAttribute("STOCK_VALUE", EITLERPGLOBAL.round(rsTmp.getDouble("STOCK_VALUE"), 2));
                ObjMRItems.setAttribute("CURRENT_PROJECTION", EITLERPGLOBAL.round(rsTmp.getDouble("CURRENT_PROJECTION"), 2));
                ObjMRItems.setAttribute("CURRENT_PROJECTION_VALUE", EITLERPGLOBAL.round(rsTmp.getDouble("CURRENT_PROJECTION_VALUE"), 2));
                ObjMRItems.setAttribute("ACTUAL_BUDGET", EITLERPGLOBAL.round(rsTmp.getDouble("ACTUAL_BUDGET"), 2));
                ObjMRItems.setAttribute("ACTUAL_BUDGET_VALUE", EITLERPGLOBAL.round(rsTmp.getDouble("ACTUAL_BUDGET_VALUE"), 2));
                ObjMRItems.setAttribute("DISPATCH_QTY", EITLERPGLOBAL.round(rsTmp.getDouble("DISPATCH_QTY"), 2));
                ObjMRItems.setAttribute("DISPATCH_VALUE", EITLERPGLOBAL.round(rsTmp.getDouble("DISPATCH_VALUE"), 2));
                ObjMRItems.setAttribute("PENDING_QTY", EITLERPGLOBAL.round(rsTmp.getDouble("PENDING_QTY"), 2));
                ObjMRItems.setAttribute("PENDING_VALUE", EITLERPGLOBAL.round(rsTmp.getDouble("PENDING_VALUE"), 2));
                ObjMRItems.setAttribute("PROJ_OF_ORDER_QTY", EITLERPGLOBAL.round(rsTmp.getDouble("PROJ_OF_ORDER_QTY"), 2));
                ObjMRItems.setAttribute("PROJ_OF_ORDER_VALUE", EITLERPGLOBAL.round(rsTmp.getDouble("PROJ_OF_ORDER_VALUE"), 2));
                ObjMRItems.setAttribute("PARTY_STATUS", rsTmp.getString("PARTY_STATUS"));
                ObjMRItems.setAttribute("SYSTEM_STATUS", rsTmp.getString("SYSTEM_STATUS"));
                ObjMRItems.setAttribute("REMARKS", rsTmp.getString("REMARKS"));
                for (int a = 1; a <= 12; a++) {
                    ObjMRItems.setAttribute(mmonth.get(a) + "_BUDGET", EITLERPGLOBAL.round(rsTmp.getDouble(mmonth.get(a) + "_BUDGET"), 2));
                    ObjMRItems.setAttribute(mmonth.get(a) + "_KG", EITLERPGLOBAL.round(rsTmp.getDouble(mmonth.get(a) + "_KG"), 2));
                    ObjMRItems.setAttribute(mmonth.get(a) + "_SQMTR", EITLERPGLOBAL.round(rsTmp.getDouble(mmonth.get(a) + "_SQMTR"), 2));
                    ObjMRItems.setAttribute(mmonth.get(a) + "_GROSS", EITLERPGLOBAL.round(rsTmp.getDouble(mmonth.get(a) + "_GROSS"), 2));
                    ObjMRItems.setAttribute(mmonth.get(a) + "_DISCOUNT", EITLERPGLOBAL.round(rsTmp.getDouble(mmonth.get(a) + "_DISCOUNT"), 2));
                    ObjMRItems.setAttribute(mmonth.get(a) + "_NET_AMOUNT", EITLERPGLOBAL.round(rsTmp.getDouble(mmonth.get(a) + "_NET_AMOUNT"), 2));
                    ObjMRItems.setAttribute(mmonth.get(a) + "_DOUBTFUL_QTY", EITLERPGLOBAL.round(rsTmp.getDouble(mmonth.get(a) + "_DOUBTFUL_QTY"), 2));
                    ObjMRItems.setAttribute(mmonth.get(a) + "_REMARK", rsTmp.getString(mmonth.get(a) + "_REMARK"));
                }
                ObjMRItems.setAttribute("UPN", rsTmp.getString("UPN"));
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
                ObjMRItems.setAttribute("CANCELED_DATE", rsTmp.getString("CANCELED_DATE"));
                ObjMRItems.setAttribute("CHANGED", rsTmp.getString("CHANGED"));
                ObjMRItems.setAttribute("CHANGED_DATE", rsTmp.getString("CHANGED_DATE"));
                ObjMRItems.setAttribute("INCHARGE", rsTmp.getString("INCHARGE"));
                ObjMRItems.setAttribute("SIZE_CRITERIA", rsTmp.getString("SIZE_CRITERIA"));
                ObjMRItems.setAttribute("PARTY_GROUP", rsTmp.getString("PARTY_GROUP"));
                ObjMRItems.setAttribute("GOAL", EITLERPGLOBAL.round(rsTmp.getDouble("GOAL"), 2));
                ObjMRItems.setAttribute("ADDTIONAL_SCOPE", EITLERPGLOBAL.round(rsTmp.getDouble("ADDTIONAL_SCOPE"), 2));
                ObjMRItems.setAttribute("CURRENT_POTENTIAL", EITLERPGLOBAL.round(rsTmp.getDouble("CURRENT_POTENTIAL"), 2));
                ObjMRItems.setAttribute("NEW_GOAL", EITLERPGLOBAL.round(rsTmp.getDouble("NEW_GOAL"), 2));
                ObjMRItems.setAttribute("CHANGE_QTY", EITLERPGLOBAL.round(rsTmp.getDouble("CHANGE_QTY"), 2));
                ObjMRItems.setAttribute("SCOPE", EITLERPGLOBAL.round(rsTmp.getDouble("SCOPE"), 2));

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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=872 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND STATUS='W'"; //825
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
            String strSQL = "SELECT * FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL_H WHERE DOC_NO='" + DocNo + "' GROUP BY REVISION_NO,DOC_NO ";
            rsTmp = data.getResult(strSQL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                while (!rsTmp.isAfterLast()) {
                    clsNewBudgetEntry objParty = new clsNewBudgetEntry();

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
            strSQL += "SELECT * FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE LEFT(DOC_NO,2)='N2' AND " + Condition + " GROUP BY DOC_NO";

            //strSQL += "SELECT * FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE " + Condition + " GROUP BY DOC_NO";
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery(strSQL);

            if (!rsResultSet.first()) {
                strSQL = "SELECT * FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE CREATED_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND CREATED_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'  AND LEFT(DOC_NO,2)='N2' GROUP BY DOC_NO,YEAR_FROM  ORDER BY DOC_NO";
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=" + clsNewBudgetEntry.ModuleID + " AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND TYPE='C' AND STATUS='W'"; //825
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
                rsTmp = data.getResult("SELECT APPROVED FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE DOC_NO='" + pAmendNo + "' ");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    Approved = rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();

                if (!Approved) {
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='" + pAmendNo + "' AND MODULE_ID=872"); //825
                }
                data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='" + pAmendNo + "'");

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
            //System.out.println("SELECT DOC_NO FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");
            rsTmp = stTmp.executeQuery("SELECT DOC_NO FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");

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
            String lDocNo = (String) getAttribute("PROFORMA_NO").getObj();
            String strSQL = "";

            //First check that record is editable
            if (CanDelete(lCompanyID, lDocNo, pUserID)) {
                String strQry = "DELETE FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER WHERE PROFORMA_NO='" + lDocNo + "'";
                data.Execute(strQry);
                strQry = "DELETE FROM PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL WHERE PROFORMA_NO='" + lDocNo + "'";
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
                strSQL = "SELECT DISTINCT case when received_date='0000-00-00 00:00:00' then date_format(doc_date,'%d/%m/%Y') else date_format(received_date,'%d/%m/%Y %H:%i') end as received_date,case when action_date='0000-00-00 00:00:00' then '' else date_format(action_date,'%d/%m/%Y %H:%i') end as action_date,PRODUCTION.FELT_BUDGET_REVIEW_DETAIL.DOC_NO,PRODUCTION.FELT_BUDGET_REVIEW_DETAIL.PARTY_NAME,PARTY_GROUP FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_BUDGET_REVIEW_DETAIL.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=872 ORDER BY received_date"; // 825
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT case when received_date='0000-00-00 00:00:00' then date_format(doc_date,'%d/%m/%Y') else date_format(received_date,'%d/%m/%Y %H:%i') end as received_date,case when action_date='0000-00-00 00:00:00' then '' else date_format(action_date,'%d/%m/%Y %H:%i') end as action_date,PRODUCTION.FELT_BUDGET_REVIEW_DETAIL.DOC_NO,PRODUCTION.FELT_BUDGET_REVIEW_DETAIL.PARTY_NAME,PARTY_GROUP FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_BUDGET_REVIEW_DETAIL.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=872 ORDER BY action_date"; //825
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT case when received_date='0000-00-00 00:00:00' then date_format(doc_date,'%d/%m/%Y') else date_format(received_date,'%d/%m/%Y %H:%i') end as received_date,case when action_date='0000-00-00 00:00:00' then '' else date_format(action_date,'%d/%m/%Y %H:%i') end as action_date,PRODUCTION.FELT_BUDGET_REVIEW_DETAIL.DOC_NO,PRODUCTION.FELT_BUDGET_REVIEW_DETAIL.PARTY_NAME,PARTY_GROUP FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_BUDGET_REVIEW_DETAIL.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=872 ORDER BY PRODUCTION.FELT_BUDGET_REVIEW_DETAIL.DOC_NO"; //825 
            }

            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {

                Counter = Counter + 1;
                clsNewBudgetEntry ObjItem = new clsNewBudgetEntry();

                //------------- Header Fields --------------------//
                ObjItem.setAttribute("DOC_NO", rsTmp.getString("DOC_NO"));
                ObjItem.setAttribute("PARTY_NAME", rsTmp.getString("PARTY_NAME"));
                ObjItem.setAttribute("PARTY_GROUP", rsTmp.getString("PARTY_GROUP"));
                ObjItem.setAttribute("received_date", rsTmp.getString("received_date"));
                ObjItem.setAttribute("action_date", rsTmp.getString("action_date"));
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
            rsResultSet = Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL_H WHERE DOC_NO='" + pDocNo + "' AND LEFT(DOC_NO,2)='MB' GROUP BY DOC_NO ORDER BY REVISION_NO");
            Ready = true;
            HistoryView = true;
            MoveFirst();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    private void Update_Budget(String mDocNo) {
        String sql;
        sql = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                + "SET TOTAL=COALESCE(Q1,0)+COALESCE(Q2,0)+COALESCE(Q3,0)+COALESCE(Q4,0) "
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                + "SET TOTAL_KG=TOTAL*(COALESCE(PRESS_WEIGHT,0)+COALESCE(DRY_WEIGHT,0)),"
                + "TOTAL_SQMTR=TOTAL*(COALESCE(PRESS_SQMTR,0)+COALESCE(DRY_SQMTR,0)) "
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                + "SET GROSS_AMOUNT=ROUND(((CASE WHEN LEFT(QUALITY_NO,1)='7' THEN TOTAL_SQMTR ELSE TOTAL_KG END)*SELLING_PRICE)*1.12,2) "
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                + "SET DISCOUNT_AMOUNT=ROUND(GROSS_AMOUNT*(SPL_DISCOUNT/100),2) "
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                + "SET NET_AMOUNT=GROSS_AMOUNT-DISCOUNT_AMOUNT"
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                + "SET Q1GROSS=ROUND(((CASE WHEN LEFT(QUALITY_NO,1)='7' THEN (COALESCE(PRESS_SQMTR,0)+COALESCE(DRY_SQMTR,0))*Q1 ELSE (COALESCE(PRESS_WEIGHT,0)+COALESCE(DRY_WEIGHT,0))*Q1 END)*SELLING_PRICE)*1.12,2),"
                + "Q2GROSS=ROUND(((CASE WHEN LEFT(QUALITY_NO,1)='7' THEN (COALESCE(PRESS_SQMTR,0)+COALESCE(DRY_SQMTR,0))*Q2 ELSE (COALESCE(PRESS_WEIGHT,0)+COALESCE(DRY_WEIGHT,0))*Q2 END)*SELLING_PRICE)*1.12,2),"
                + "Q3GROSS=ROUND(((CASE WHEN LEFT(QUALITY_NO,1)='7' THEN (COALESCE(PRESS_SQMTR,0)+COALESCE(DRY_SQMTR,0))*Q3 ELSE (COALESCE(PRESS_WEIGHT,0)+COALESCE(DRY_WEIGHT,0))*Q3 END)*SELLING_PRICE)*1.12,2),"
                + "Q4GROSS=ROUND(((CASE WHEN LEFT(QUALITY_NO,1)='7' THEN (COALESCE(PRESS_SQMTR,0)+COALESCE(DRY_SQMTR,0))*Q4 ELSE (COALESCE(PRESS_WEIGHT,0)+COALESCE(DRY_WEIGHT,0))*Q4 END)*SELLING_PRICE)*1.12,2) "
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                + "SET Q1DISCOUNT=ROUND(Q1GROSS*(SPL_DISCOUNT/100),2),"
                + "Q2DISCOUNT=ROUND(Q2GROSS*(SPL_DISCOUNT/100),2),"
                + "Q3DISCOUNT=ROUND(Q3GROSS*(SPL_DISCOUNT/100),2),"
                + "Q4DISCOUNT=ROUND(Q4GROSS*(SPL_DISCOUNT/100),2)  "
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                + "SET Q1NET_AMOUNT=ROUND(Q1GROSS-Q1DISCOUNT,2),"
                + "Q2NET_AMOUNT=ROUND(Q2GROSS-Q2DISCOUNT,2),"
                + "Q3NET_AMOUNT=ROUND(Q3GROSS-Q3DISCOUNT,2),"
                + "Q4NET_AMOUNT=ROUND(Q4GROSS-Q4DISCOUNT,2) "
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);

        sql = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL_H "
                + "SET TOTAL=COALESCE(Q1,0)+COALESCE(Q2,0)+COALESCE(Q3,0)+COALESCE(Q4,0) "
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL_H "
                + "SET TOTAL_KG=TOTAL*(COALESCE(PRESS_WEIGHT,0)+COALESCE(DRY_WEIGHT,0)),"
                + "TOTAL_SQMTR=TOTAL*(COALESCE(PRESS_SQMTR,0)+COALESCE(DRY_SQMTR,0)) "
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL_H "
                + "SET GROSS_AMOUNT=ROUND(((CASE WHEN LEFT(QUALITY_NO,1)='7' THEN TOTAL_SQMTR ELSE TOTAL_KG END)*SELLING_PRICE)*1.12,2) "
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL_H "
                + "SET DISCOUNT_AMOUNT=ROUND(GROSS_AMOUNT*(SPL_DISCOUNT/100),2) "
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL_H "
                + "SET NET_AMOUNT=GROSS_AMOUNT-DISCOUNT_AMOUNT"
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL_H "
                + "SET Q1GROSS=ROUND(((CASE WHEN LEFT(QUALITY_NO,1)='7' THEN (COALESCE(PRESS_SQMTR,0)+COALESCE(DRY_SQMTR,0))*Q1 ELSE (COALESCE(PRESS_WEIGHT,0)+COALESCE(DRY_WEIGHT,0))*Q1 END)*SELLING_PRICE)*1.12,2),"
                + "Q2GROSS=ROUND(((CASE WHEN LEFT(QUALITY_NO,1)='7' THEN (COALESCE(PRESS_SQMTR,0)+COALESCE(DRY_SQMTR,0))*Q2 ELSE (COALESCE(PRESS_WEIGHT,0)+COALESCE(DRY_WEIGHT,0))*Q2 END)*SELLING_PRICE)*1.12,2),"
                + "Q3GROSS=ROUND(((CASE WHEN LEFT(QUALITY_NO,1)='7' THEN (COALESCE(PRESS_SQMTR,0)+COALESCE(DRY_SQMTR,0))*Q3 ELSE (COALESCE(PRESS_WEIGHT,0)+COALESCE(DRY_WEIGHT,0))*Q3 END)*SELLING_PRICE)*1.12,2),"
                + "Q4GROSS=ROUND(((CASE WHEN LEFT(QUALITY_NO,1)='7' THEN (COALESCE(PRESS_SQMTR,0)+COALESCE(DRY_SQMTR,0))*Q4 ELSE (COALESCE(PRESS_WEIGHT,0)+COALESCE(DRY_WEIGHT,0))*Q4 END)*SELLING_PRICE)*1.12,2) "
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL_H "
                + "SET Q1DISCOUNT=ROUND(Q1GROSS*(SPL_DISCOUNT/100),2),"
                + "Q2DISCOUNT=ROUND(Q2GROSS*(SPL_DISCOUNT/100),2),"
                + "Q3DISCOUNT=ROUND(Q3GROSS*(SPL_DISCOUNT/100),2),"
                + "Q4DISCOUNT=ROUND(Q4GROSS*(SPL_DISCOUNT/100),2)  "
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL_H "
                + "SET Q1NET_AMOUNT=ROUND(Q1GROSS-Q1DISCOUNT,2),"
                + "Q2NET_AMOUNT=ROUND(Q2GROSS-Q2DISCOUNT,2),"
                + "Q3NET_AMOUNT=ROUND(Q3GROSS-Q3DISCOUNT,2),"
                + "Q4NET_AMOUNT=ROUND(Q4GROSS-Q4DISCOUNT,2) "
                + " WHERE DOC_NO='" + mDocNo + "'";
        data.Execute(sql);

    }
}
