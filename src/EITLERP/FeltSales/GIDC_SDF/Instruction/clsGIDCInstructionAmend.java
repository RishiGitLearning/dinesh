/*
 * clsSales_Party.java
 *
 * Created on April 3, 2009, 10:22 AM
 */
package EITLERP.FeltSales.GIDC_SDF.Instruction;

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
public class clsGIDCInstructionAmend {

    public String LastError = "";
    private ResultSet rsResultSet, rsResultSet1;
    private Connection Conn;
    private Statement Stmt;
    DecimalFormat df = new DecimalFormat("##.##");
    private HashMap props;

    public boolean Ready = false;

    private boolean HistoryView = false;
    public static int ModuleID = 791; //72
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
    public clsGIDCInstructionAmend() {
        LastError = "";
        props = new HashMap();
        //props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("REVISION_NO", new Variant(0));
        props.put("UPDATED_BY", new Variant(0));
        props.put("ENTRY_DATE", new Variant(""));
        props.put("AMEND_DOC_NO", new Variant(""));
        props.put("AMEND_DOC_DATE", new Variant(""));

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
            rsResultSet = Stmt.executeQuery("SELECT * FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND WHERE CREATED_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND CREATED_DATE<='" + EITLERPGLOBAL.FinToDateDB + "' GROUP BY AMEND_DOC_NO ORDER BY AMEND_DOC_NO");
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
            rsHDetail = stHDetail.executeQuery("SELECT * FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND_H WHERE AMEND_DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            //--------- Generate Instruction no.  ------------
            //setAttribute("AMEND_DOC_NO", clsFirstFree.getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 791, (int) getAttribute("FFNO").getVal(), true));
            //-------------------------------------------------

            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");

            Statement tmpStmt;

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND WHERE AMEND_DOC_NO='1'");

            //Now Insert records into detail table
            for (int i = 1; i <= colMRItems.size(); i++) {
                clsGIDCInstructionAmendItem ObjMRItems = (clsGIDCInstructionAmendItem) colMRItems.get(Integer.toString(i));

                rsTmp.moveToInsertRow();
                rsTmp.updateString("AMEND_DOC_NO", (String) getAttribute("AMEND_DOC_NO").getObj());
                rsTmp.updateString("AMEND_DOC_DATE", (String) getAttribute("AMEND_DOC_DATE").getObj());
                rsTmp.updateString("DOC_NO", (String) getAttribute("DOC_NO").getObj());
                rsTmp.updateString("DOC_DATE", (String) getAttribute("DOC_DATE").getObj());
                rsTmp.updateObject("SR_NO", ObjMRItems.getAttribute("SR_NO").getObj());
                rsTmp.updateString("PIECE_NO", (String) ObjMRItems.getAttribute("PIECE_NO").getObj());
                rsTmp.updateString("RMCODE_LENGTH", (String) ObjMRItems.getAttribute("RMCODE_LENGTH").getObj());
                rsTmp.updateDouble("REQUIRE_QTY_LENGTH", (double) ObjMRItems.getAttribute("REQUIRE_QTY_LENGTH").getVal());
                rsTmp.updateString("UNIT_LENGTH", "KGS");
                rsTmp.updateString("RMCODE_WEFT", (String) ObjMRItems.getAttribute("RMCODE_WEFT").getObj());
                rsTmp.updateDouble("REQUIRE_QTY_WEFT", (double) ObjMRItems.getAttribute("REQUIRE_QTY_WEFT").getVal());
                rsTmp.updateString("UNIT_WEFT", "KGS");
                rsTmp.updateLong("SEQUANCE_NO", (int) ObjMRItems.getAttribute("SEQUANCE_NO").getVal());
                rsTmp.updateString("PARTY_CODE", (String) ObjMRItems.getAttribute("PARTY_CODE").getObj());
                rsTmp.updateString("PARTY_CODE", (String) ObjMRItems.getAttribute("PARTY_CODE").getObj());
                rsTmp.updateString("PRODUCT_CODE", (String) ObjMRItems.getAttribute("PRODUCT_CODE").getObj());
                rsTmp.updateString("PRODUCT_GROUP", (String) ObjMRItems.getAttribute("PRODUCT_GROUP").getObj());
                rsTmp.updateString("STYLE", (String) ObjMRItems.getAttribute("STYLE").getObj());
                rsTmp.updateDouble("LENGTH", (double) ObjMRItems.getAttribute("LENGTH").getVal());
                rsTmp.updateDouble("WIDTH", (double) ObjMRItems.getAttribute("WIDTH").getVal());
                rsTmp.updateLong("GSM", (int) ObjMRItems.getAttribute("GSM").getVal());
                rsTmp.updateDouble("WEIGHT", (double) ObjMRItems.getAttribute("WEIGHT").getVal());
                rsTmp.updateDouble("GREY_LENGTH", (double) ObjMRItems.getAttribute("GREY_LENGTH").getVal());
                rsTmp.updateDouble("GREY_WIDTH", (double) ObjMRItems.getAttribute("GREY_WIDTH").getVal());
                rsTmp.updateString("FROM_IP", str_split[1]);
                rsTmp.updateString("REMARKS", (String) ObjMRItems.getAttribute("REMARKS").getObj());
                rsTmp.updateString("AMEND_REASON", (String) getAttribute("AMEND_REASON").getObj());
                rsTmp.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                rsTmp.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
                rsTmp.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
                rsTmp.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
                rsTmp.updateBoolean("APPROVED", false);
                rsTmp.updateString("APPROVED_DATE", "0000-00-00");
                rsTmp.updateBoolean("CANCELED", false);
                rsTmp.updateBoolean("REJECTED", false);
                rsTmp.updateString("REJECTED_DATE", "0000-00-00");
                rsTmp.updateBoolean("CHANGED", true);
                rsTmp.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.insertRow();

                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO", 1);
                rsHDetail.updateInt("UPDATED_BY", (int) getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("APPROVAL_STATUS", (String) getAttribute("APPROVAL_STATUS").getObj());
                rsHDetail.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("APPROVER_REMARKS", (String) getAttribute("FROM_REMARKS").getObj());
                rsHDetail.updateString("AMEND_DOC_NO", (String) getAttribute("AMEND_DOC_NO").getObj());
                rsHDetail.updateString("AMEND_DOC_DATE", (String) getAttribute("AMEND_DOC_DATE").getObj());
                rsHDetail.updateString("DOC_NO", (String) getAttribute("DOC_NO").getObj());
                rsHDetail.updateString("DOC_DATE", (String) getAttribute("DOC_DATE").getObj());
                rsHDetail.updateObject("SR_NO", ObjMRItems.getAttribute("SR_NO").getObj());
                rsHDetail.updateString("PIECE_NO", (String) ObjMRItems.getAttribute("PIECE_NO").getObj());
                rsHDetail.updateString("RMCODE_LENGTH", (String) ObjMRItems.getAttribute("RMCODE_LENGTH").getObj());
                rsHDetail.updateDouble("REQUIRE_QTY_LENGTH", (double) ObjMRItems.getAttribute("REQUIRE_QTY_LENGTH").getVal());
                rsHDetail.updateString("UNIT_LENGTH", "KGS");
                rsHDetail.updateString("RMCODE_WEFT", (String) ObjMRItems.getAttribute("RMCODE_WEFT").getObj());
                rsHDetail.updateDouble("REQUIRE_QTY_WEFT", (double) ObjMRItems.getAttribute("REQUIRE_QTY_WEFT").getVal());
                rsHDetail.updateString("UNIT_WEFT", "KGS");
                rsHDetail.updateLong("SEQUANCE_NO", (int) ObjMRItems.getAttribute("SEQUANCE_NO").getVal());
                rsHDetail.updateString("PARTY_CODE", (String) ObjMRItems.getAttribute("PARTY_CODE").getObj());
                rsHDetail.updateString("PARTY_CODE", (String) ObjMRItems.getAttribute("PARTY_CODE").getObj());
                rsHDetail.updateString("PRODUCT_CODE", (String) ObjMRItems.getAttribute("PRODUCT_CODE").getObj());
                rsHDetail.updateString("PRODUCT_GROUP", (String) ObjMRItems.getAttribute("PRODUCT_GROUP").getObj());
                rsHDetail.updateString("STYLE", (String) ObjMRItems.getAttribute("STYLE").getObj());
                rsHDetail.updateDouble("LENGTH", (double) ObjMRItems.getAttribute("LENGTH").getVal());
                rsHDetail.updateDouble("WIDTH", (double) ObjMRItems.getAttribute("WIDTH").getVal());
                rsHDetail.updateLong("GSM", (int) ObjMRItems.getAttribute("GSM").getVal());
                rsHDetail.updateDouble("WEIGHT", (double) ObjMRItems.getAttribute("WEIGHT").getVal());
                rsHDetail.updateDouble("GREY_LENGTH", (double) ObjMRItems.getAttribute("GREY_LENGTH").getVal());
                rsHDetail.updateDouble("GREY_WIDTH", (double) ObjMRItems.getAttribute("GREY_WIDTH").getVal());
                rsHDetail.updateString("FROM_IP", str_split[1]);
                rsHDetail.updateString("REMARKS", (String) ObjMRItems.getAttribute("REMARKS").getObj());
                rsHDetail.updateString("AMEND_REASON", (String) getAttribute("AMEND_REASON").getObj());
                rsHDetail.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                rsHDetail.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
                rsHDetail.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
                rsHDetail.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
                rsHDetail.updateBoolean("APPROVED", false);
                rsHDetail.updateString("APPROVED_DATE", "0000-00-00");
                rsHDetail.updateBoolean("CANCELED", false);
                rsHDetail.updateBoolean("REJECTED", false);
                rsHDetail.updateString("REJECTED_DATE", "0000-00-00");
                rsHDetail.updateBoolean("CHANGED", true);
                rsHDetail.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();

            }

            //===================== Update the Approval Flow ======================//
            setAttribute("FROM", EITLERPGLOBAL.gUserID);

            //ApprovalFlow ObjFlow=new ApprovalFlow();
            clsFeltProductionApprovalFlow ObjFlow = new clsFeltProductionApprovalFlow();

            ObjFlow.ModuleID = clsGIDCInstructionAmend.ModuleID;
            ObjFlow.DocNo = (String) getAttribute("AMEND_DOC_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("AMEND_DOC_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND";
            ObjFlow.IsCreator = true;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "AMEND_DOC_NO";

            if (ObjFlow.Status.equals("H")) {
                ObjFlow.Status = "A";
                ObjFlow.To = ObjFlow.From;
                ObjFlow.UpdateFlow();
            } else {
                if (!ObjFlow.UpdateFlow()) {
                    LastError = ObjFlow.LastError;
                }
            }
            String sql = "";
            if (ObjFlow.Status.equals("F")) {
                sql = "UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER P,PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND D "
                        + "SET PR_PIECE_STAGE='SPIRALLING',PR_WIP_STATUS='WAIT_FOR_SPIRAL',"
                        //+ "SET PR_PIECE_STAGE='GIDC',PR_WIP_STATUS='GIDC',"
                        //+ "PR_WARP_DATE=CURDATE(),
                        + "D.APPROVED=1,D.APPROVED_DATE=CURDATE() "
                        + "WHERE SUBSTRING(P.PR_PIECE_NO,1,5)=SUBSTRING(D.PIECE_NO,1,5) AND "
                        + "D.AMEND_DOC_NO='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "' ";
                        //+ "AND "
                        //+ "(PR_WARP_DATE='' OR PR_WARP_DATE IS NULL OR PR_WARP_DATE='0000-00-00')";
                System.out.println("Final Approval Query :" + sql);
                data.Execute(sql);
                sql = "UPDATE PRODUCTION.GIDC_INSTRUCTION_DETAIL "
                        + "SET INDICATOR='DELETE',INDICATOR_DATE=CURDATE(),"
                        + "INDICATOR_DOC='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "' "
                        + "WHERE PIECE_NO IN ( SELECT D.PIECE_NO "
                        + "FROM (SELECT  * FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL "
                        + "WHERE DOC_NO IN (SELECT DISTINCT DOC_NO FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND "
                        + "WHERE AMEND_DOC_NO='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "')) AS D "
                        + "LEFT JOIN (SELECT * FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND "
                        + " WHERE AMEND_DOC_NO='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "') AS  A "
                        + "ON D.DOC_NO=A.DOC_NO AND D.PIECE_NO=A.PIECE_NO "
                        + "WHERE A.PIECE_NO IS NULL)";
                System.out.println("Deleted Piece Query:" + sql);
                data.Execute(sql);
                sql = "UPDATE PRODUCTION.GIDC_INSTRUCTION_DETAIL_H "
                        + "SET INDICATOR='DELETE',INDICATOR_DATE=CURDATE(),"
                        + "INDICATOR_DOC='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "' "
                        + "WHERE PIECE_NO IN ( SELECT D.PIECE_NO "
                        + "FROM (SELECT  * FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL "
                        + "WHERE DOC_NO IN (SELECT DISTINCT DOC_NO FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND "
                        + "WHERE AMEND_DOC_NO='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "')) AS D "
                        + "LEFT JOIN (SELECT * FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND "
                        + " WHERE AMEND_DOC_NO='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "') AS  A "
                        + "ON D.DOC_NO=A.DOC_NO AND D.PIECE_NO=A.PIECE_NO "
                        + "WHERE A.PIECE_NO IS NULL)";
                System.out.println("Deleted Piece Query History:" + sql);
                data.Execute(sql);
                sql = "UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER P,"
                        + "(SELECT PIECE_NO FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL "
                        + "WHERE PIECE_NO NOT IN (SELECT PIECE_NO FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND "
                        + "WHERE AMEND_DOC_NO='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "') "
                        + "AND DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "') AS D "
                        + " SET PR_PIECE_STAGE='PLANNING',PR_WIP_STATUS='ORDERED' "
                        //+ ",PR_WARP_DATE='0000-00-00' "
                        + " WHERE SUBSTRING(P.PR_PIECE_NO,1,5)=SUBSTRING(D.PIECE_NO,1,5)";
                System.out.println("Piece Stage Change for Delete Piece:" + sql);
                data.Execute(sql);
                sql = "UPDATE PRODUCTION.GIDC_FELT_RM_STOCK S,"
                        + "(SELECT RMCODE_LENGTH,SUM(REQUIRE_QTY_LENGTH) AS QTY FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL "
                        + "WHERE PIECE_NO NOT IN (SELECT PIECE_NO FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND "
                        + "WHERE AMEND_DOC_NO='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "') "
                        + "AND DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "' "
                        + " GROUP BY RMCODE_LENGTH ) AS D "
                        + "SET S.USED=S.USED-D.QTY,"
                        + "S.AVAILABLE=S.AVAILABLE+D.QTY "
                        + "WHERE S.ITEM_CODE=D.RMCODE_LENGTH";
                System.out.println("Delete Piece Length Update Stock:" + sql);
                data.Execute(sql);
                sql = "UPDATE PRODUCTION.GIDC_FELT_RM_STOCK S,"
                        + "(SELECT RMCODE_WEFT,SUM(REQUIRE_QTY_WEFT) AS QTY FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL "
                        + "WHERE PIECE_NO NOT IN (SELECT PIECE_NO FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND "
                        + "WHERE AMEND_DOC_NO='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "') "
                        + "AND DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "' "
                        + " GROUP BY RMCODE_WEFT ) AS D "
                        + "SET S.USED=S.USED-D.QTY,"
                        + "S.AVAILABLE=S.AVAILABLE+D.QTY "
                        + "WHERE S.ITEM_CODE=D.RMCODE_WEFT";
                System.out.println("Delete Piece Weft Update Stock:" + sql);
                data.Execute(sql);

                sql = "INSERT INTO PRODUCTION.GIDC_INSTRUCTION_DETAIL "
                        + "(DOC_NO,DOC_DATE,SR_NO,PIECE_NO,RMCODE_LENGTH,REQUIRE_QTY_LENGTH,UNIT_LENGTH,"
                        + "RMCODE_WEFT,REQUIRE_QTY_WEFT,UNIT_WEFT,SEQUANCE_NO,"
                        + "PARTY_CODE,PRODUCT_CODE,PRODUCT_GROUP,STYLE,LENGTH,WIDTH,GSM,WEIGHT,FROM_IP,REMARKS,"
                        + "CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,CHANGED_DATE,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,REJECTED_REMARKS,CANCELED,CANCELED_DATE,"
                        + "CHANGED,INDICATOR,INDICATOR_DATE,INDICATOR_DOC,GREY_LENGTH,GREY_WIDTH) "
                        + " "
                        + "SELECT DOC_NO,DOC_DATE,SR_NO,PIECE_NO,RMCODE_LENGTH,REQUIRE_QTY_LENGTH,UNIT_LENGTH,"
                        + "RMCODE_WEFT,REQUIRE_QTY_WEFT,UNIT_WEFT,SEQUANCE_NO,"
                        + "PARTY_CODE,PRODUCT_CODE,PRODUCT_GROUP,STYLE,LENGTH,WIDTH,GSM,WEIGHT,FROM_IP,REMARKS,"
                        + "CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,CHANGED_DATE,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,REJECTED_REMARKS,CANCELED,CANCELED_DATE,"
                        + "CHANGED,'ADD',CURDATE(),'" + (String) getAttribute("AMEND_DOC_NO").getObj() + "',GREY_LENGTH,GREY_WIDTH  "
                        + " FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND "
                        + " WHERE PIECE_NO NOT IN (SELECT PIECE_NO FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL "
                        + " WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "') "
                        + " AND AMEND_DOC_NO='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "'";
                System.out.println("Add Piece Query :" + sql);
                data.Execute(sql);
                sql = "INSERT INTO PRODUCTION.GIDC_INSTRUCTION_DETAIL_H "
                        + "(REVISION_NO,UPDATED_BY,ENTRY_DATE,APPROVAL_STATUS,APPROVER_REMARKS,DOC_NO,DOC_DATE,"
                        + "SR_NO,PIECE_NO,RMCODE_LENGTH,REQUIRE_QTY_LENGTH,UNIT_LENGTH,RMCODE_WEFT,REQUIRE_QTY_WEFT,"
                        + "UNIT_WEFT,SEQUANCE_NO,PARTY_CODE,PRODUCT_CODE,PRODUCT_GROUP,STYLE,LENGTH,WIDTH,GSM,WEIGHT,"
                        + "FROM_IP,REMARKS,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,CHANGED_DATE,"
                        + "APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,REJECTED_REMARKS,CANCELED,CANCELED_DATE,"
                        + "CHANGED,INDICATOR,INDICATOR_DATE,INDICATOR_DOC,GREY_LENGTH,GREY_WIDTH) "
                        + " "
                        + "SELECT 999,UPDATED_BY,ENTRY_DATE,APPROVAL_STATUS,APPROVER_REMARKS,DOC_NO,DOC_DATE,"
                        + "SR_NO,PIECE_NO,RMCODE_LENGTH,REQUIRE_QTY_LENGTH,UNIT_LENGTH,RMCODE_WEFT,REQUIRE_QTY_WEFT,"
                        + "UNIT_WEFT,SEQUANCE_NO,PARTY_CODE,PRODUCT_CODE,PRODUCT_GROUP,STYLE,LENGTH,WIDTH,GSM,WEIGHT,"
                        + "FROM_IP,REMARKS,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,CHANGED_DATE,"
                        + "APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,REJECTED_REMARKS,CANCELED,CANCELED_DATE,"
                        + "CHANGED,'ADD',CURDATE(),'" + (String) getAttribute("AMEND_DOC_NO").getObj() + "',GREY_LENGTH,GREY_WIDTH  "
                        + " FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND_H "
                        + " WHERE PIECE_NO NOT IN (SELECT PIECE_NO FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL "
                        + " WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "') "
                        + " AND AMEND_DOC_NO='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "' "
                        + " AND REVISION_NO=1";
                System.out.println("Add Piece Query History :" + sql);
                data.Execute(sql);
                sql = "UPDATE PRODUCTION.GIDC_FELT_RM_STOCK S,"
                        + "(SELECT RMCODE_LENGTH,SUM(REQUIRE_QTY_LENGTH) AS RQTY FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND "
                        + "WHERE PIECE_NO IN (SELECT PIECE_NO FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL "
                        + " WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "' AND INDICATOR='ADD' ) "
                        + " AND AMEND_DOC_NO='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "' "
                        + ") AS D "
                        + "SET S.USED=S.USED+D.RQTY,S.AVAILABLE=S.AVAILABLE-D.RQTY "
                        + "WHERE S.ITEM_CODE=D.RMCODE_LENGTH";
                System.out.println("Add Piece Stock Update Length:" + sql);
                data.Execute(sql);
                sql = "UPDATE PRODUCTION.GIDC_FELT_RM_STOCK S,"
                        + "(SELECT RMCODE_WEFT,SUM(REQUIRE_QTY_WEFT) AS RQTY FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND "
                        + "WHERE PIECE_NO IN (SELECT PIECE_NO FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL "
                        + " WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "' AND INDICATOR='ADD' ) "
                        + " AND AMEND_DOC_NO='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "' "
                        + ") AS D "
                        + "SET S.USED=S.USED+D.RQTY,S.AVAILABLE=S.AVAILABLE-D.RQTY "
                        + "WHERE S.ITEM_CODE=D.RMCODE_WEFT";
                System.out.println("Add Piece Stock Update Weft:" + sql);
                data.Execute(sql);
                int hid = data.getIntValueFromDB("SELECT HIERARCHY_ID FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "' AND (HIERARCHY_ID IS NOT NULL OR HIERARCHY_ID>0)");
                sql = "UPDATE PRODUCTION.GIDC_INSTRUCTION_DETAIL "
                        + "SET HIERARCHY_ID=" + hid + " "
                        + " WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "' "
                        + " AND (HIERARCHY_ID IS NULL OR HIERARCHY_ID=0)";
                System.out.println("Update Hierarchy :" + sql);
                data.Execute(sql);
                sql = "UPDATE PRODUCTION.GIDC_INSTRUCTION_DETAIL_H "
                        + "SET HIERARCHY_ID=" + hid + " "
                        + " WHERE AMEND_DOC_NO='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "' "
                        + " AND (HIERARCHY_ID IS NULL OR HIERARCHY_ID=0) AND REVISION_NO=1";
                System.out.println("Update Hierarchy History :" + sql);
                data.Execute(sql);
                sql = "INSERT INTO PRODUCTION.GIDC_FELT_PIECE_REGISTER "
                        + "(GIDC_PIECE_NO,GIDC_SDF_INST_NO,GIDC_SDF_INST_DATE,GIDC_PARTY_CODE,GIDC_PRODUCT_CODE,"
                        + "GIDC_GROUP,GIDC_STYLE,GIDC_LENGTH,GIDC_WIDTH,GIDC_GSM,GIDC_THORITICAL_WEIGHT,GIDC_STAGE,"
                        + "GIDC_RMCODE_LENGTH,GIDC_RMCODE_LENGTH_WEIGHT,GIDC_RMCODE_WEFT,GIDC_RMCODE_WEFT_WEIGHT) "
                        + " SELECT PIECE_NO,DOC_NO,DOC_DATE,PARTY_CODE,PRODUCT_CODE,"
                        + "PRODUCT_GROUP,STYLE,LENGTH,WIDTH,GSM,WEIGHT,'YET_TO_MFG',"
                        + "RMCODE_LENGTH,REQUIRE_QTY_LENGTH,RMCODE_WEFT,REQUIRE_QTY_WEFT "
                        + " FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND "
                        + " WHERE PIECE_NO NOT IN (SELECT PIECE_NO FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL "
                        + " WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "') "
                        + " AND AMEND_DOC_NO='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "'";
                data.Execute(sql);
                sql = "DELETE FROM PRODUCTION.GIDC_FELT_PIECE_REGISTER "
                        + "WHERE GIDC_PIECE_NO IN "
                        + "(SELECT PIECE_NO FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL "
                        + "WHERE INDICATOR='DELETE' AND INDICATOR_DOC_NO ='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "')";
                data.Execute(sql);

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

            rsHDetail = stHDetail.executeQuery("SELECT * FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND_H WHERE AMEND_DOC_NO='1' ");
            rsHDetail.first();
            //------------------------------------//

            String theDocNo = getAttribute("AMEND_DOC_NO").getString();
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND_H WHERE AMEND_DOC_NO='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "'");
            RevNo++;
            String mDocNo = (String) getAttribute("AMEND_DOC_NO").getObj();
            if (clsFeltProductionApprovalFlow.IsCreator(ModuleID, (String) getAttribute("AMEND_DOC_NO").getObj())) {

                data.Execute("DELETE FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND WHERE AMEND_DOC_NO='" + mDocNo + "'");

                Statement tmpStmt;

                tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet rsTmp1 = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND WHERE AMEND_DOC_NO='1'");

                //Now Insert records into detail table
                for (int i = 1; i <= colMRItems.size(); i++) {
                    clsGIDCInstructionAmendItem ObjMRItems = (clsGIDCInstructionAmendItem) colMRItems.get(Integer.toString(i));
                    rsTmp1.moveToInsertRow();
                    rsTmp1.updateString("AMEND_DOC_NO", (String) getAttribute("AMEND_DOC_NO").getObj());
                    rsTmp1.updateString("AMEND_DOC_DATE", (String) getAttribute("AMEND_DOC_DATE").getObj());
                    rsTmp1.updateString("DOC_NO", (String) getAttribute("DOC_NO").getObj());
                    rsTmp1.updateString("DOC_DATE", (String) getAttribute("DOC_DATE").getObj());
                    rsTmp1.updateObject("SR_NO", ObjMRItems.getAttribute("SR_NO").getObj());
                    rsTmp1.updateString("PIECE_NO", (String) ObjMRItems.getAttribute("PIECE_NO").getObj());
                    rsTmp1.updateString("RMCODE_LENGTH", (String) ObjMRItems.getAttribute("RMCODE_LENGTH").getObj());
                    rsTmp1.updateDouble("REQUIRE_QTY_LENGTH", (double) ObjMRItems.getAttribute("REQUIRE_QTY_LENGTH").getVal());
                    rsTmp1.updateString("UNIT_LENGTH", "KGS");
                    rsTmp1.updateString("RMCODE_WEFT", (String) ObjMRItems.getAttribute("RMCODE_WEFT").getObj());
                    rsTmp1.updateDouble("REQUIRE_QTY_WEFT", (double) ObjMRItems.getAttribute("REQUIRE_QTY_WEFT").getVal());
                    rsTmp1.updateString("UNIT_WEFT", "KGS");
                    rsTmp1.updateLong("SEQUANCE_NO", (int) ObjMRItems.getAttribute("SEQUANCE_NO").getVal());
                    rsTmp1.updateString("PARTY_CODE", (String) ObjMRItems.getAttribute("PARTY_CODE").getObj());
                    rsTmp1.updateString("PARTY_CODE", (String) ObjMRItems.getAttribute("PARTY_CODE").getObj());
                    rsTmp1.updateString("PRODUCT_CODE", (String) ObjMRItems.getAttribute("PRODUCT_CODE").getObj());
                    rsTmp1.updateString("PRODUCT_GROUP", (String) ObjMRItems.getAttribute("PRODUCT_GROUP").getObj());
                    rsTmp1.updateString("STYLE", (String) ObjMRItems.getAttribute("STYLE").getObj());
                    rsTmp1.updateDouble("LENGTH", (double) ObjMRItems.getAttribute("LENGTH").getVal());
                    rsTmp1.updateDouble("WIDTH", (double) ObjMRItems.getAttribute("WIDTH").getVal());
                    rsTmp1.updateLong("GSM", (int) ObjMRItems.getAttribute("GSM").getVal());
                    rsTmp1.updateDouble("WEIGHT", (double) ObjMRItems.getAttribute("WEIGHT").getVal());
                    rsTmp1.updateDouble("GREY_LENGTH", (double) ObjMRItems.getAttribute("GREY_LENGTH").getVal());
                    rsTmp1.updateDouble("GREY_WIDTH", (double) ObjMRItems.getAttribute("GREY_WIDTH").getVal());
                    rsTmp1.updateString("FROM_IP", str_split[1]);
                    rsTmp1.updateString("REMARKS", (String) ObjMRItems.getAttribute("REMARKS").getObj());
                    rsTmp1.updateString("AMEND_REASON", (String) getAttribute("AMEND_REASON").getObj());
                    rsTmp1.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                    rsTmp1.updateInt("MODIFIED_BY", getAttribute("MODIFIED_BY").getInt());
                    rsTmp1.updateString("MODIFIED_DATE", getAttribute("MODIFIED_DATE").getString());
                    rsTmp1.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
                    rsTmp1.updateBoolean("APPROVED", false);
                    rsTmp1.updateString("APPROVED_DATE", "0000-00-00");
                    rsTmp1.updateBoolean("CANCELED", false);
                    rsTmp1.updateBoolean("REJECTED", false);
                    rsTmp1.updateString("REJECTED_DATE", "0000-00-00");
                    rsTmp1.updateBoolean("CHANGED", true);
                    rsTmp1.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsTmp1.insertRow();

                    rsHDetail.moveToInsertRow();
                    rsHDetail.updateInt("REVISION_NO", RevNo);
                    rsHDetail.updateInt("UPDATED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                    rsHDetail.updateString("APPROVAL_STATUS", (String) getAttribute("APPROVAL_STATUS").getObj());
                    rsHDetail.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsHDetail.updateString("APPROVER_REMARKS", (String) getAttribute("FROM_REMARKS").getObj());
                    rsHDetail.updateString("AMEND_DOC_NO", (String) getAttribute("AMEND_DOC_NO").getObj());
                    rsHDetail.updateString("AMEND_DOC_DATE", (String) getAttribute("AMEND_DOC_DATE").getObj());
                    rsHDetail.updateString("DOC_NO", (String) getAttribute("DOC_NO").getObj());
                    rsHDetail.updateString("DOC_DATE", (String) getAttribute("DOC_DATE").getObj());
                    rsHDetail.updateObject("SR_NO", ObjMRItems.getAttribute("SR_NO").getObj());
                    rsHDetail.updateString("PIECE_NO", (String) ObjMRItems.getAttribute("PIECE_NO").getObj());
                    rsHDetail.updateString("RMCODE_LENGTH", (String) ObjMRItems.getAttribute("RMCODE_LENGTH").getObj());
                    rsHDetail.updateDouble("REQUIRE_QTY_LENGTH", (double) ObjMRItems.getAttribute("REQUIRE_QTY_LENGTH").getVal());
                    rsHDetail.updateString("UNIT_LENGTH", "KGS");
                    rsHDetail.updateString("RMCODE_WEFT", (String) ObjMRItems.getAttribute("RMCODE_WEFT").getObj());
                    rsHDetail.updateDouble("REQUIRE_QTY_WEFT", (double) ObjMRItems.getAttribute("REQUIRE_QTY_WEFT").getVal());
                    rsHDetail.updateString("UNIT_WEFT", "KGS");
                    rsHDetail.updateLong("SEQUANCE_NO", (int) ObjMRItems.getAttribute("SEQUANCE_NO").getVal());
                    rsHDetail.updateString("PARTY_CODE", (String) ObjMRItems.getAttribute("PARTY_CODE").getObj());
                    rsHDetail.updateString("PARTY_CODE", (String) ObjMRItems.getAttribute("PARTY_CODE").getObj());
                    rsHDetail.updateString("PRODUCT_CODE", (String) ObjMRItems.getAttribute("PRODUCT_CODE").getObj());
                    rsHDetail.updateString("PRODUCT_GROUP", (String) ObjMRItems.getAttribute("PRODUCT_GROUP").getObj());
                    rsHDetail.updateString("STYLE", (String) ObjMRItems.getAttribute("STYLE").getObj());
                    rsHDetail.updateDouble("LENGTH", (double) ObjMRItems.getAttribute("LENGTH").getVal());
                    rsHDetail.updateDouble("WIDTH", (double) ObjMRItems.getAttribute("WIDTH").getVal());
                    rsHDetail.updateLong("GSM", (int) ObjMRItems.getAttribute("GSM").getVal());
                    rsHDetail.updateDouble("WEIGHT", (double) ObjMRItems.getAttribute("WEIGHT").getVal());
                    rsHDetail.updateDouble("GREY_LENGTH", (double) ObjMRItems.getAttribute("GREY_LENGTH").getVal());
                    rsHDetail.updateDouble("GREY_WIDTH", (double) ObjMRItems.getAttribute("GREY_WIDTH").getVal());
                    rsHDetail.updateString("FROM_IP", str_split[1]);
                    rsHDetail.updateString("REMARKS", (String) ObjMRItems.getAttribute("REMARKS").getObj());
                    rsHDetail.updateString("AMEND_REASON", (String) getAttribute("AMEND_REASON").getObj());
                    rsHDetail.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                    rsHDetail.updateInt("MODIFIED_BY", getAttribute("MODIFIED_BY").getInt());
                    rsHDetail.updateString("MODIFIED_DATE", getAttribute("MODIFIED_DATE").getString());
                    rsHDetail.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
                    rsHDetail.updateBoolean("APPROVED", false);
                    rsHDetail.updateString("APPROVED_DATE", "0000-00-00");
                    rsHDetail.updateBoolean("CANCELED", false);
                    rsHDetail.updateBoolean("REJECTED", false);
                    rsHDetail.updateString("REJECTED_DATE", "0000-00-00");
                    rsHDetail.updateBoolean("CHANGED", true);
                    rsHDetail.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsHDetail.insertRow();
                }

            } else {
                for (int i = 1; i <= colMRItems.size(); i++) {
                    clsGIDCInstructionAmendItem ObjMRItems = (clsGIDCInstructionAmendItem) colMRItems.get(Integer.toString(i));
                    rsHDetail.moveToInsertRow();
                    rsHDetail.updateInt("REVISION_NO", RevNo);
                    rsHDetail.updateInt("UPDATED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                    rsHDetail.updateString("APPROVAL_STATUS", (String) getAttribute("APPROVAL_STATUS").getObj());
                    rsHDetail.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsHDetail.updateString("APPROVER_REMARKS", (String) getAttribute("FROM_REMARKS").getObj());
                    rsHDetail.updateString("AMEND_DOC_NO", (String) getAttribute("AMEND_DOC_NO").getObj());
                    rsHDetail.updateString("AMEND_DOC_DATE", (String) getAttribute("AMEND_DOC_DATE").getObj());
                    rsHDetail.updateString("DOC_NO", (String) getAttribute("DOC_NO").getObj());
                    rsHDetail.updateString("DOC_DATE", (String) getAttribute("DOC_DATE").getObj());
                    rsHDetail.updateObject("SR_NO", ObjMRItems.getAttribute("SR_NO").getObj());
                    rsHDetail.updateString("PIECE_NO", (String) ObjMRItems.getAttribute("PIECE_NO").getObj());
                    rsHDetail.updateString("RMCODE_LENGTH", (String) ObjMRItems.getAttribute("RMCODE_LENGTH").getObj());
                    rsHDetail.updateDouble("REQUIRE_QTY_LENGTH", (double) ObjMRItems.getAttribute("REQUIRE_QTY_LENGTH").getVal());
                    rsHDetail.updateString("UNIT_LENGTH", "KGS");
                    rsHDetail.updateString("RMCODE_WEFT", (String) ObjMRItems.getAttribute("RMCODE_WEFT").getObj());
                    rsHDetail.updateDouble("REQUIRE_QTY_WEFT", (double) ObjMRItems.getAttribute("REQUIRE_QTY_WEFT").getVal());
                    rsHDetail.updateString("UNIT_WEFT", "KGS");
                    rsHDetail.updateLong("SEQUANCE_NO", (int) ObjMRItems.getAttribute("SEQUANCE_NO").getVal());
                    rsHDetail.updateString("PARTY_CODE", (String) ObjMRItems.getAttribute("PARTY_CODE").getObj());
                    rsHDetail.updateString("PARTY_CODE", (String) ObjMRItems.getAttribute("PARTY_CODE").getObj());
                    rsHDetail.updateString("PRODUCT_CODE", (String) ObjMRItems.getAttribute("PRODUCT_CODE").getObj());
                    rsHDetail.updateString("PRODUCT_GROUP", (String) ObjMRItems.getAttribute("PRODUCT_GROUP").getObj());
                    rsHDetail.updateString("STYLE", (String) ObjMRItems.getAttribute("STYLE").getObj());
                    rsHDetail.updateDouble("LENGTH", (double) ObjMRItems.getAttribute("LENGTH").getVal());
                    rsHDetail.updateDouble("WIDTH", (double) ObjMRItems.getAttribute("WIDTH").getVal());
                    rsHDetail.updateLong("GSM", (int) ObjMRItems.getAttribute("GSM").getVal());
                    rsHDetail.updateDouble("WEIGHT", (double) ObjMRItems.getAttribute("WEIGHT").getVal());
                    rsHDetail.updateDouble("GREY_LENGTH", (double) ObjMRItems.getAttribute("GREY_LENGTH").getVal());
                    rsHDetail.updateDouble("GREY_WIDTH", (double) ObjMRItems.getAttribute("GREY_WIDTH").getVal());
                    rsHDetail.updateString("FROM_IP", str_split[1]);
                    rsHDetail.updateString("REMARKS", (String) ObjMRItems.getAttribute("REMARKS").getObj());
                    rsHDetail.updateString("AMEND_REASON", (String) getAttribute("AMEND_REASON").getObj());
                    rsHDetail.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                    rsHDetail.updateInt("MODIFIED_BY", getAttribute("MODIFIED_BY").getInt());
                    rsHDetail.updateString("MODIFIED_DATE", getAttribute("MODIFIED_DATE").getString());
                    rsHDetail.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
                    rsHDetail.updateBoolean("APPROVED", false);
                    rsHDetail.updateString("APPROVED_DATE", "0000-00-00");
                    rsHDetail.updateBoolean("CANCELED", false);
                    rsHDetail.updateBoolean("REJECTED", false);
                    rsHDetail.updateString("REJECTED_DATE", "0000-00-00");
                    rsHDetail.updateBoolean("CHANGED", true);
                    rsHDetail.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsHDetail.insertRow();
                }
            }
            data.Execute("UPDATE PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND D,PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND_H H SET D.CREATED_BY=H.CREATED_BY,D.CREATED_DATE=H.CREATED_DATE WHERE D.AMEND_DOC_NO=H.AMEND_DOC_NO AND H.REVISION_NO=1 AND D.CREATED_BY IS NULL");
            //======== Update the Approval Flow =========
            setAttribute("FROM", EITLERPGLOBAL.gUserID);

            //ApprovalFlow ObjFlow=new ApprovalFlow();
            clsFeltProductionApprovalFlow ObjFlow = new clsFeltProductionApprovalFlow();

            ObjFlow.ModuleID = clsGIDCInstructionAmend.ModuleID;
            ObjFlow.DocNo = (String) getAttribute("AMEND_DOC_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("AMEND_DOC_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND";
            ObjFlow.IsCreator = false;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "AMEND_DOC_NO";

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
                data.Execute("UPDATE PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE AMEND_DOC_NO='" + ObjFlow.DocNo + "' ");

                //Remove Old Records from FELT_DOC_DATA
                data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=" + clsGIDCInstructionAmend.ModuleID + " AND DOC_NO='" + ObjFlow.DocNo + "'");

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
            String sql = "";
            if (ObjFlow.Status.equals("F")) {
                sql = "UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER P,PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND D "
                        + "SET PR_PIECE_STAGE='SPIRALLING',PR_WIP_STATUS='WAIT_FOR_SPIRAL'"
                        //+ "SET PR_PIECE_STAGE='GIDC',PR_WIP_STATUS='GIDC'"
                        //+ ",PR_WARP_DATE=CURDATE()"
                        + ",D.APPROVED=1,D.APPROVED_DATE=CURDATE() "
                        + "WHERE SUBSTRING(P.PR_PIECE_NO,1,5)=SUBSTRING(D.PIECE_NO,1,5) AND "
                        + "D.AMEND_DOC_NO='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "'";
                        //+ " AND "
                        //+ "(PR_WARP_DATE='' OR PR_WARP_DATE IS NULL OR PR_WARP_DATE='0000-00-00')";
                System.out.println("Final Approval Query :" + sql);
                data.Execute(sql);
                sql = "UPDATE PRODUCTION.GIDC_INSTRUCTION_DETAIL "
                        + "SET INDICATOR='DELETE',INDICATOR_DATE=CURDATE(),"
                        + "INDICATOR_DOC='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "' "
                        + "WHERE PIECE_NO IN ( SELECT D.PIECE_NO "
                        + "FROM (SELECT  * FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL "
                        + "WHERE DOC_NO IN (SELECT DISTINCT DOC_NO FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND "
                        + "WHERE AMEND_DOC_NO='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "')) AS D "
                        + "LEFT JOIN (SELECT * FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND "
                        + " WHERE AMEND_DOC_NO='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "') AS  A "
                        + "ON D.DOC_NO=A.DOC_NO AND D.PIECE_NO=A.PIECE_NO "
                        + "WHERE A.PIECE_NO IS NULL)";
                System.out.println("Deleted Piece Query:" + sql);
                data.Execute(sql);
                sql = "UPDATE PRODUCTION.GIDC_INSTRUCTION_DETAIL_H "
                        + "SET INDICATOR='DELETE',INDICATOR_DATE=CURDATE(),"
                        + "INDICATOR_DOC='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "' "
                        + "WHERE PIECE_NO IN ( SELECT D.PIECE_NO "
                        + "FROM (SELECT  * FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL "
                        + "WHERE DOC_NO IN (SELECT DISTINCT DOC_NO FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND "
                        + "WHERE AMEND_DOC_NO='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "')) AS D "
                        + "LEFT JOIN (SELECT * FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND "
                        + " WHERE AMEND_DOC_NO='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "') AS  A "
                        + "ON D.DOC_NO=A.DOC_NO AND D.PIECE_NO=A.PIECE_NO "
                        + "WHERE A.PIECE_NO IS NULL)";
                System.out.println("Deleted Piece Query History:" + sql);
                data.Execute(sql);
                sql = "UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER P,"
                        + "(SELECT PIECE_NO FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL "
                        + "WHERE PIECE_NO NOT IN (SELECT PIECE_NO FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND "
                        + "WHERE AMEND_DOC_NO='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "') "
                        + "AND DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "') AS D "
                        + " SET PR_PIECE_STAGE='PLANNING',PR_WIP_STATUS='ORDERED' "
                        //+ ",PR_WARP_DATE='0000-00-00' "
                        + " WHERE SUBSTRING(P.PR_PIECE_NO,1,5)=SUBSTRING(D.PIECE_NO,1,5)";
                System.out.println("Piece Stage Change for Delete Piece:" + sql);
                data.Execute(sql);
                sql = "UPDATE PRODUCTION.GIDC_FELT_RM_STOCK S,"
                        + "(SELECT RMCODE_LENGTH,SUM(REQUIRE_QTY_LENGTH) AS QTY FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL "
                        + "WHERE PIECE_NO NOT IN (SELECT PIECE_NO FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND "
                        + "WHERE AMEND_DOC_NO='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "') "
                        + "AND DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "' "
                        + " GROUP BY RMCODE_LENGTH ) AS D "
                        + "SET S.USED=S.USED-D.QTY,"
                        + "S.AVAILABLE=S.AVAILABLE+D.QTY "
                        + "WHERE S.ITEM_CODE=D.RMCODE_LENGTH";
                System.out.println("Delete Piece Length Update Stock:" + sql);
                data.Execute(sql);
                sql = "UPDATE PRODUCTION.GIDC_FELT_RM_STOCK S,"
                        + "(SELECT RMCODE_WEFT,SUM(REQUIRE_QTY_WEFT) AS QTY FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL "
                        + "WHERE PIECE_NO NOT IN (SELECT PIECE_NO FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND "
                        + "WHERE AMEND_DOC_NO='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "') "
                        + "AND DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "' "
                        + " GROUP BY RMCODE_WEFT ) AS D "
                        + "SET S.USED=S.USED-D.QTY,"
                        + "S.AVAILABLE=S.AVAILABLE+D.QTY "
                        + "WHERE S.ITEM_CODE=D.RMCODE_WEFT";
                System.out.println("Delete Piece Weft Update Stock:" + sql);
                data.Execute(sql);

                sql = "INSERT INTO PRODUCTION.GIDC_INSTRUCTION_DETAIL "
                        + "(DOC_NO,DOC_DATE,SR_NO,PIECE_NO,RMCODE_LENGTH,REQUIRE_QTY_LENGTH,UNIT_LENGTH,"
                        + "RMCODE_WEFT,REQUIRE_QTY_WEFT,UNIT_WEFT,SEQUANCE_NO,"
                        + "PARTY_CODE,PRODUCT_CODE,PRODUCT_GROUP,STYLE,LENGTH,WIDTH,GSM,WEIGHT,FROM_IP,REMARKS,"
                        + "CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,CHANGED_DATE,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,REJECTED_REMARKS,CANCELED,CANCELED_DATE,"
                        + "CHANGED,INDICATOR,INDICATOR_DATE,INDICATOR_DOC,GREY_LENGTH,GREY_WIDTH) "
                        + " "
                        + "SELECT DOC_NO,DOC_DATE,SR_NO,PIECE_NO,RMCODE_LENGTH,REQUIRE_QTY_LENGTH,UNIT_LENGTH,"
                        + "RMCODE_WEFT,REQUIRE_QTY_WEFT,UNIT_WEFT,SEQUANCE_NO,"
                        + "PARTY_CODE,PRODUCT_CODE,PRODUCT_GROUP,STYLE,LENGTH,WIDTH,GSM,WEIGHT,FROM_IP,REMARKS,"
                        + "CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,CHANGED_DATE,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,REJECTED_REMARKS,CANCELED,CANCELED_DATE,"
                        + "CHANGED,'ADD',CURDATE(),'" + (String) getAttribute("AMEND_DOC_NO").getObj() + "',GREY_LENGTH,GREY_WIDTH  "
                        + " FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND "
                        + " WHERE PIECE_NO NOT IN (SELECT PIECE_NO FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL "
                        + " WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "') "
                        + " AND AMEND_DOC_NO='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "'";
                System.out.println("Add Piece Query :" + sql);
                data.Execute(sql);
                sql = "INSERT INTO PRODUCTION.GIDC_INSTRUCTION_DETAIL_H "
                        + "(REVISION_NO,UPDATED_BY,ENTRY_DATE,APPROVAL_STATUS,APPROVER_REMARKS,DOC_NO,DOC_DATE,"
                        + "SR_NO,PIECE_NO,RMCODE_LENGTH,REQUIRE_QTY_LENGTH,UNIT_LENGTH,RMCODE_WEFT,REQUIRE_QTY_WEFT,"
                        + "UNIT_WEFT,SEQUANCE_NO,PARTY_CODE,PRODUCT_CODE,PRODUCT_GROUP,STYLE,LENGTH,WIDTH,GSM,WEIGHT,"
                        + "FROM_IP,REMARKS,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,CHANGED_DATE,"
                        + "APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,REJECTED_REMARKS,CANCELED,CANCELED_DATE,"
                        + "CHANGED,INDICATOR,INDICATOR_DATE,INDICATOR_DOC,GREY_LENGTH,GREY_WIDTH) "
                        + " "
                        + "SELECT 999,UPDATED_BY,ENTRY_DATE,APPROVAL_STATUS,APPROVER_REMARKS,DOC_NO,DOC_DATE,"
                        + "SR_NO,PIECE_NO,RMCODE_LENGTH,REQUIRE_QTY_LENGTH,UNIT_LENGTH,RMCODE_WEFT,REQUIRE_QTY_WEFT,"
                        + "UNIT_WEFT,SEQUANCE_NO,PARTY_CODE,PRODUCT_CODE,PRODUCT_GROUP,STYLE,LENGTH,WIDTH,GSM,WEIGHT,"
                        + "FROM_IP,REMARKS,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,CHANGED_DATE,"
                        + "APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,REJECTED_REMARKS,CANCELED,CANCELED_DATE,"
                        + "CHANGED,'ADD',CURDATE(),'" + (String) getAttribute("AMEND_DOC_NO").getObj() + "',GREY_LENGTH,GREY_WIDTH  "
                        + " FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND_H "
                        + " WHERE PIECE_NO NOT IN (SELECT PIECE_NO FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL "
                        + " WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "') "
                        + " AND AMEND_DOC_NO='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "' "
                        + " AND REVISION_NO=" + RevNo;
                System.out.println("Add Piece Query History :" + sql);
                data.Execute(sql);
                sql = "UPDATE PRODUCTION.GIDC_FELT_RM_STOCK S,"
                        + "(SELECT RMCODE_LENGTH,SUM(REQUIRE_QTY_LENGTH) AS RQTY FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND "
                        + "WHERE PIECE_NO IN (SELECT PIECE_NO FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL "
                        + " WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "' AND INDICATOR='ADD' ) "
                        + " AND AMEND_DOC_NO='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "' "
                        + ") AS D "
                        + "SET S.USED=S.USED+D.RQTY,S.AVAILABLE=S.AVAILABLE-D.RQTY "
                        + "WHERE S.ITEM_CODE=D.RMCODE_LENGTH";
                System.out.println("Add Piece Stock Update Length:" + sql);
                data.Execute(sql);
                sql = "UPDATE PRODUCTION.GIDC_FELT_RM_STOCK S,"
                        + "(SELECT RMCODE_WEFT,SUM(REQUIRE_QTY_WEFT) AS RQTY FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND "
                        + "WHERE PIECE_NO IN (SELECT PIECE_NO FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL "
                        + " WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "' AND INDICATOR='ADD' ) "
                        + " AND AMEND_DOC_NO='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "' "
                        + ") AS D "
                        + "SET S.USED=S.USED+D.RQTY,S.AVAILABLE=S.AVAILABLE-D.RQTY "
                        + "WHERE S.ITEM_CODE=D.RMCODE_WEFT";
                System.out.println("Add Piece Stock Update Weft:" + sql);
                data.Execute(sql);
                int hid = data.getIntValueFromDB("SELECT HIERARCHY_ID FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "' AND (HIERARCHY_ID IS NOT NULL OR HIERARCHY_ID>0)");
                sql = "UPDATE PRODUCTION.GIDC_INSTRUCTION_DETAIL "
                        + "SET HIERARCHY_ID=" + hid + " "
                        + " WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "' "
                        + " AND (HIERARCHY_ID IS NULL OR HIERARCHY_ID=0)";
                System.out.println("Update Hierarchy :" + sql);
                data.Execute(sql);
                sql = "UPDATE PRODUCTION.GIDC_INSTRUCTION_DETAIL_H "
                        + "SET HIERARCHY_ID=" + hid + " "
                        + " WHERE AMEND_DOC_NO='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "' "
                        + " AND (HIERARCHY_ID IS NULL OR HIERARCHY_ID=0) AND REVISION_NO=999";
                System.out.println("Update Hierarchy History :" + sql);
                data.Execute(sql);
                sql = "INSERT INTO PRODUCTION.GIDC_FELT_PIECE_REGISTER "
                        + "(GIDC_PIECE_NO,GIDC_SDF_INST_NO,GIDC_SDF_INST_DATE,GIDC_PARTY_CODE,GIDC_PRODUCT_CODE,"
                        + "GIDC_GROUP,GIDC_STYLE,GIDC_LENGTH,GIDC_WIDTH,GIDC_GSM,GIDC_THORITICAL_WEIGHT,GIDC_STAGE,"
                        + "GIDC_RMCODE_LENGTH,GIDC_RMCODE_LENGTH_WEIGHT,GIDC_RMCODE_WEFT,GIDC_RMCODE_WEFT_WEIGHT) "
                        + " SELECT PIECE_NO,DOC_NO,DOC_DATE,PARTY_CODE,PRODUCT_CODE,"
                        + "PRODUCT_GROUP,STYLE,LENGTH,WIDTH,GSM,WEIGHT,'YET_TO_MFG',"
                        + "RMCODE_LENGTH,REQUIRE_QTY_LENGTH,RMCODE_WEFT,REQUIRE_QTY_WEFT "
                        + " FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND "
                        + " WHERE PIECE_NO NOT IN (SELECT PIECE_NO FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL "
                        + " WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "') "
                        + " AND AMEND_DOC_NO='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "'";
                data.Execute(sql);
                sql = "DELETE FROM PRODUCTION.GIDC_FELT_PIECE_REGISTER "
                        + "WHERE GIDC_PIECE_NO IN "
                        + "(SELECT PIECE_NO FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL "
                        + "WHERE INDICATOR='DELETE' AND INDICATOR_DOC_NO ='" + (String) getAttribute("AMEND_DOC_NO").getObj() + "')";
                data.Execute(sql);
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

            setAttribute("AMEND_DOC_NO", rsResultSet.getString("AMEND_DOC_NO"));
            setAttribute("AMEND_DOC_DATE", rsResultSet.getString("AMEND_DOC_DATE"));
            setAttribute("AMEND_REASON", rsResultSet.getString("AMEND_REASON"));

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

            String mDocNo = (String) getAttribute("AMEND_DOC_NO").getObj();

            tmpStmt = tmpConn.createStatement();
            if (HistoryView) {
                if (clsFeltProductionApprovalFlow.IsCreator(ModuleID, (String) getAttribute("AMEND_DOC_NO").getObj())) {
                    rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND_H WHERE AMEND_DOC_NO='" + mDocNo + "' AND REVISION_NO=" + RevNo + " ORDER BY SR_NO");
                } else {
                    rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND_H WHERE AMEND_DOC_NO='" + mDocNo + "' AND REVISION_NO=" + RevNo + " ORDER BY SEQUANCE_NO");
                }
            } else {
                if (clsFeltProductionApprovalFlow.IsCreator(ModuleID, (String) getAttribute("AMEND_DOC_NO").getObj())) {
                    rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND WHERE AMEND_DOC_NO='" + mDocNo + "' ORDER BY SR_NO");
                } else {
                    rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND WHERE AMEND_DOC_NO='" + mDocNo + "' ORDER BY SEQUANCE_NO");
                }
            }
            rsTmp.first();

            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {
                Counter = Counter + 1;
                clsGIDCInstructionAmendItem ObjMRItems = new clsGIDCInstructionAmendItem();
                ObjMRItems.setAttribute("AMEND_DOC_NO", rsTmp.getString("AMEND_DOC_NO"));
                ObjMRItems.setAttribute("AMEND_DOC_DATE", rsTmp.getString("AMEND_DOC_DATE"));
                ObjMRItems.setAttribute("DOC_NO", rsTmp.getString("DOC_NO"));
                ObjMRItems.setAttribute("DOC_DATE", rsTmp.getString("DOC_DATE"));
                ObjMRItems.setAttribute("SR_NO", rsTmp.getLong("SR_NO"));
                ObjMRItems.setAttribute("PIECE_NO", rsTmp.getString("PIECE_NO"));
                ObjMRItems.setAttribute("RMCODE_LENGTH", rsTmp.getString("RMCODE_LENGTH"));
                ObjMRItems.setAttribute("REQUIRE_QTY_LENGTH", EITLERPGLOBAL.round(rsTmp.getDouble("REQUIRE_QTY_LENGTH"), 2));
                ObjMRItems.setAttribute("UNIT_LENGTH", "KGS");
                ObjMRItems.setAttribute("RMCODE_WEFT", rsTmp.getString("RMCODE_WEFT"));
                ObjMRItems.setAttribute("REQUIRE_QTY_WEFT", EITLERPGLOBAL.round(rsTmp.getDouble("REQUIRE_QTY_WEFT"), 2));
                ObjMRItems.setAttribute("UNIT_WEFT", "KGS");
                ObjMRItems.setAttribute("SEQUANCE_NO", rsTmp.getInt("SEQUANCE_NO"));
                ObjMRItems.setAttribute("PARTY_CODE", rsTmp.getString("PARTY_CODE"));
                ObjMRItems.setAttribute("PRODUCT_CODE", rsTmp.getString("PRODUCT_CODE"));
                ObjMRItems.setAttribute("PRODUCT_GROUP", rsTmp.getString("PRODUCT_GROUP"));
                ObjMRItems.setAttribute("STYLE", rsTmp.getString("STYLE"));
                ObjMRItems.setAttribute("LENGTH", EITLERPGLOBAL.round(rsTmp.getDouble("LENGTH"), 2));
                ObjMRItems.setAttribute("WIDTH", EITLERPGLOBAL.round(rsTmp.getDouble("WIDTH"), 2));
                ObjMRItems.setAttribute("GSM", EITLERPGLOBAL.round(rsTmp.getDouble("GSM"), 2));
                ObjMRItems.setAttribute("WEIGHT", EITLERPGLOBAL.round(rsTmp.getDouble("WEIGHT"), 2));
                ObjMRItems.setAttribute("GREY_LENGTH", EITLERPGLOBAL.round(rsTmp.getDouble("GREY_LENGTH"), 2));
                ObjMRItems.setAttribute("GREY_WIDTH", EITLERPGLOBAL.round(rsTmp.getDouble("GREY_WIDTH"), 2));
                ObjMRItems.setAttribute("FROM_IP", rsTmp.getString("FROM_IP"));
                ObjMRItems.setAttribute("AMEND_REASON", rsTmp.getString("AMEND_REASON"));
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND WHERE AMEND_DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=791 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND STATUS='W'";
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
            String strSQL = "SELECT AMEND_DOC_NO,CREATED_DATE,REVISION_NO,UPDATED_BY,APPROVAL_STATUS,DATE_FORMAT(ENTRY_DATE,'%d/%m/%Y %H:%i:%S') AS ENTRY_DATE,REJECTED_REMARKS AS APPROVER_REMARKS,FROM_IP FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND_H WHERE AMEND_DOC_NO='" + DocNo + "' GROUP BY AMEND_DOC_NO,REVISION_NO";
            rsTmp = data.getResult(strSQL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                while (!rsTmp.isAfterLast()) {
                    clsGIDCInstructionAmend objParty = new clsGIDCInstructionAmend();

                    objParty.setAttribute("AMEND_DOC_NO", rsTmp.getString("AMEND_DOC_NO"));
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
            strSQL += "SELECT * FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND WHERE " + Condition + " GROUP BY AMEND_DOC_NO";
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery(strSQL);

            if (!rsResultSet.first()) {
                strSQL = "SELECT * FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND WHERE CREATED_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND CREATED_DATE<='" + EITLERPGLOBAL.FinToDateDB + "' GROUP BY AMEND_DOC_NO ORDER BY AMEND_DOC_NO";
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND WHERE AMEND_DOC_NO='" + pDocNo + "' AND (APPROVED=1) GROUP BY AMEND_DOC_NO";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=791 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND TYPE='C' AND STATUS='W'";
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
                strSQL = "SELECT DISTINCT PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND.AMEND_DOC_NO,PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND.CREATED_DATE,RECEIVED_DATE FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND.AMEND_DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=791 ORDER BY PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND.AMEND_DOC_NO,PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND.CREATED_DATE,RECEIVED_DATE FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND.AMEND_DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=791 ORDER BY PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND.CREATED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND.AMEND_DOC_NO,PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND.CREATED_DATE,RECEIVED_DATE FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND.AMEND_DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=791 ORDER BY PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND.AMEND_DOC_NO";
            }

            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {

                Counter = Counter + 1;
                clsGIDCInstructionAmend ObjItem = new clsGIDCInstructionAmend();

                //------------- Header Fields --------------------//
                ObjItem.setAttribute("DOC_NO", rsTmp.getString("AMEND_DOC_NO"));
                ObjItem.setAttribute("DOC_DATE", rsTmp.getString("CREATED_DATE"));
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
            rsResultSet = Stmt.executeQuery("SELECT * FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND_H WHERE AMEND_DOC_NO='" + pDocNo + "' GROUP BY AMEND_DOC_NO,REVISION_NO ORDER BY REVISION_NO");
            Ready = true;
            HistoryView = true;
            MoveFirst();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public static boolean IsAmendmentUnderProcess(String pDocNo) {
        Connection tmpConn;
        Statement stTmp = null;
        ResultSet rsTmp = null;
        boolean IsPresent = false;

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = stTmp.executeQuery("SELECT * FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND WHERE DOC_NO='" + pDocNo + "' AND APPROVED=0 AND CANCELED=0");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                IsPresent = true;
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

            return IsPresent;
        } catch (Exception e) {
            return IsPresent;
        }
    }

    public static void CancelDoc(String pDocNo) {
        ResultSet rsTmp = null;

        if (CanCancel(pDocNo)) {
            boolean Approved = false;

            try {
                rsTmp = data.getResult("SELECT APPROVED FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND WHERE AMEND_DOC_NO='" + pDocNo + "' ");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    Approved = rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();

                if (!Approved) {
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='" + pDocNo + "' AND MODULE_ID=791");
                }
                data.Execute("UPDATE PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE AMEND_DOC_NO='" + pDocNo + "'");

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
            System.out.println("SELECT DOC_NO FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND WHERE AMEND_DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");
            rsTmp = stTmp.executeQuery("SELECT DOC_NO FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND WHERE AMEND_DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");

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
