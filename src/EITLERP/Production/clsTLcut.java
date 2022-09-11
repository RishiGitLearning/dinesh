/*
     * clsSales_Party.java
     *
     * Created on April 3, 2009, 10:22 AM
 */
package EITLERP.Production;

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
 * @author ashutosh
 */
public class clsTLcut {

    public String LastError = "";
    private ResultSet rsResultSet, rsResultSet1;
    private Connection Conn;
    private Statement Stmt;
    DecimalFormat df = new DecimalFormat("##.##");
    private HashMap props;
    public boolean Ready = false;

    private boolean HistoryView = false;
    public static int ModuleID = 708; //72
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
    public clsTLcut() {
        LastError = "";
        props = new HashMap();
        //props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("DOC_NO", new Variant(""));
        props.put("DOC_DATE", new Variant(""));
        props.put("FILE_NAME", new Variant(""));

        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("PREFIX", new Variant(""));

        //Create a new object for MR Item collection
        colMRItems = new HashMap();

    }

    /**
     * Load Data. This method loads data from database to Business Object*
     */
    public boolean LoadData(long pCompanyID) {
        Ready = false;
        try {

            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery("SELECT DISTINCT DOC_NO,DOC_DATE,FILE_NAME FROM PRODUCTION.TL_CUT WHERE DOC_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND DOC_DATE<='" + EITLERPGLOBAL.FinToDateDB + "' ORDER BY DOC_NO");
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

            //======= Check the requisition date ============//
            java.sql.Date FinFromDate = java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate = java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);

            //=====================================================//
            // ---- History Related Changes ------ //
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHDetail = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rsHistory = stHistory.executeQuery("SELECT * FROM PRODUCTION.TL_CUT_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail = stHDetail.executeQuery("SELECT * FROM PRODUCTION.TL_CUT_H WHERE DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//

            //--------- Generate Gatepass requisition no.  ------------
            setAttribute("DOC_NO", "TL" + String.valueOf(100000 + data.getIntValueFromDB("SELECT (COALESCE(MAX(RIGHT(DOC_NO,5)),0))+1 AS TLDOC FROM PRODUCTION.TL_CUT")).substring(1));
            //-------------------------------------------------

            ResultSet rsTmp;
            Statement tmpStmt;

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            //rsTmp=tmpStmt.executeQuery("SELECT *,false as Calculate_Weight FROM PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL WHERE PROFORMA_NO='1'");
            rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.TL_CUT WHERE DOC_NO='1'");

            //long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            //Now Insert records into detail table
            for (int i = 1; i <= colMRItems.size(); i++) {
                clsTLcutItem ObjMRItems = (clsTLcutItem) colMRItems.get(Integer.toString(i));

                rsTmp.moveToInsertRow();

                //rsTmp.updateLong("COMPANY_ID",(long)nCompanyID);
                rsTmp.updateString("DOC_NO", (String) ObjMRItems.getAttribute("DOC_NO").getObj());
                rsTmp.updateString("DOC_DATE", (String) ObjMRItems.getAttribute("DOC_DATE").getObj());
                rsTmp.updateString("FILE_NAME", (String) ObjMRItems.getAttribute("FILE_NAME").getObj());
                rsTmp.updateString("QUALITY_ID", (String) ObjMRItems.getAttribute("QUALITY_ID").getObj());
                rsTmp.updateString("SHADE", (String) ObjMRItems.getAttribute("SHADE").getObj());
                rsTmp.updateString("PIECE_NO", (String) ObjMRItems.getAttribute("PIECE_NO").getObj());
                rsTmp.updateString("RCVD_DATE", (String) ObjMRItems.getAttribute("RCVD_DATE").getObj());
                rsTmp.updateString("FLAG_CD", (String) ObjMRItems.getAttribute("FLAG_CD").getObj());
                rsTmp.updateString("RACK_NO", (String) ObjMRItems.getAttribute("RACK_NO").getObj());
                rsTmp.updateDouble("NET_MTR", (double) ObjMRItems.getAttribute("NET_MTR").getVal());
                rsTmp.updateString("BRAND", (String) ObjMRItems.getAttribute("BRAND").getObj());
                rsTmp.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                //rsTmp.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                //rsTmp.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsTmp.insertRow();

                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO", 1);
                rsHDetail.updateInt("UPDATED_BY", (int) getAttribute("CREATED_BY").getVal());                
                rsHDetail.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());

                rsHDetail.updateString("DOC_NO", (String) ObjMRItems.getAttribute("DOC_NO").getObj());
                rsHDetail.updateString("DOC_DATE", (String) ObjMRItems.getAttribute("DOC_DATE").getObj());
                rsHDetail.updateString("FILE_NAME", (String) ObjMRItems.getAttribute("FILE_NAME").getObj());
                rsHDetail.updateString("QUALITY_ID", (String) ObjMRItems.getAttribute("QUALITY_ID").getObj());
                rsHDetail.updateString("SHADE", (String) ObjMRItems.getAttribute("SHADE").getObj());
                rsHDetail.updateString("PIECE_NO", (String) ObjMRItems.getAttribute("PIECE_NO").getObj());
                rsHDetail.updateString("RCVD_DATE", (String) ObjMRItems.getAttribute("RCVD_DATE").getObj());
                rsHDetail.updateString("FLAG_CD", (String) ObjMRItems.getAttribute("FLAG_CD").getObj());
                rsHDetail.updateString("RACK_NO", (String) ObjMRItems.getAttribute("RACK_NO").getObj());
                rsHDetail.updateDouble("NET_MTR", (double) ObjMRItems.getAttribute("NET_MTR").getVal());
                rsHDetail.updateString("BRAND", (String) ObjMRItems.getAttribute("BRAND").getObj());

                rsHDetail.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                //rsHDetail.updateLong("MODIFIED_BY", (long) getAttribute("MODIFIED_BY").getVal());
                //rsHDetail.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());

                rsHDetail.insertRow();
            }
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

            Validate = true;
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate = java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate = java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);

            // ---- History Related Changes ------ //
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHDetail = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rsHistory = stHistory.executeQuery("SELECT * FROM PRODUCTION.TL_CUT_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail = stHDetail.executeQuery("SELECT * FROM PRODUCTION.TL_CUT_H WHERE DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//

            String theDocNo = getAttribute("DOC_NO").getString();

            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.TL_CUT_H WHERE DOC_NO='" + (String) getAttribute("DOC_NO").getObj() + "'");
            RevNo++;

            //First remove the old rows
            String mDocno = (String) getAttribute("DOC_NO").getObj();

            data.Execute("DELETE FROM PRODUCTION.TL_CUT WHERE DOC_NO='" + mDocno + "'");

            ResultSet rsTmp;
            Statement tmpStmt;

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            //rsTmp=tmpStmt.executeQuery("SELECT *,false as Calculate_Weight FROM PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL WHERE PROFORMA_NO='1'");
            rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.TL_CUT WHERE DOC_NO='1'");

            //Now Insert records into detail table
            for (int i = 1; i <= colMRItems.size(); i++) {
                clsTLcutItem ObjMRItems = (clsTLcutItem) colMRItems.get(Integer.toString(i));
                rsTmp.moveToInsertRow();
                rsTmp.updateString("DOC_NO", (String) ObjMRItems.getAttribute("DOC_NO").getObj());
                rsTmp.updateString("DOC_DATE", (String) ObjMRItems.getAttribute("DOC_DATE").getObj());
                rsTmp.updateString("FILE_NAME", (String) ObjMRItems.getAttribute("FILE_NAME").getObj());
                rsTmp.updateString("QUALITY_ID", (String) ObjMRItems.getAttribute("QUALITY_ID").getObj());
                rsTmp.updateString("SHADE", (String) ObjMRItems.getAttribute("SHADE").getObj());
                rsTmp.updateString("PIECE_NO", (String) ObjMRItems.getAttribute("PIECE_NO").getObj());
                rsTmp.updateString("RCVD_DATE", (String) ObjMRItems.getAttribute("RCVD_DATE").getObj());
                rsTmp.updateString("FLAG_CD", (String) ObjMRItems.getAttribute("FLAG_CD").getObj());
                rsTmp.updateString("RACK_NO", (String) ObjMRItems.getAttribute("RACK_NO").getObj());
                rsTmp.updateDouble("NET_MTR", (double) ObjMRItems.getAttribute("NET_MTR").getVal());
                rsTmp.updateString("BRAND", (String) ObjMRItems.getAttribute("BRAND").getObj());
                //rsTmp.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
                //rsTmp.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY", (long) getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                rsTmp.insertRow();

                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO", RevNo);
                rsHDetail.updateInt("UPDATED_BY", (int) getAttribute("MODIFIED_BY").getVal());                
                rsHDetail.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());

                rsHDetail.updateString("DOC_NO", (String) ObjMRItems.getAttribute("DOC_NO").getObj());
                rsHDetail.updateString("DOC_DATE", (String) ObjMRItems.getAttribute("DOC_DATE").getObj());
                rsHDetail.updateString("FILE_NAME", (String) ObjMRItems.getAttribute("FILE_NAME").getObj());
                rsHDetail.updateString("QUALITY_ID", (String) ObjMRItems.getAttribute("QUALITY_ID").getObj());
                rsHDetail.updateString("SHADE", (String) ObjMRItems.getAttribute("SHADE").getObj());
                rsHDetail.updateString("PIECE_NO", (String) ObjMRItems.getAttribute("PIECE_NO").getObj());
                rsHDetail.updateString("RCVD_DATE", (String) ObjMRItems.getAttribute("RCVD_DATE").getObj());
                rsHDetail.updateString("FLAG_CD", (String) ObjMRItems.getAttribute("FLAG_CD").getObj());
                rsHDetail.updateString("RACK_NO", (String) ObjMRItems.getAttribute("RACK_NO").getObj());
                rsHDetail.updateDouble("NET_MTR", (double) ObjMRItems.getAttribute("NET_MTR").getVal());
                rsHDetail.updateString("BRAND", (String) ObjMRItems.getAttribute("BRAND").getObj());

                //rsHDetail.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
                //rsHDetail.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY", (long) getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.insertRow();
            }
            data.Execute("UPDATE PRODUCTION.TL_CUT T,PRODUCTION.TL_CUT_H SET T.CREATED_BY=H.CREATED_BY,T.CREATE_DATE=H.CREATED_DATE WHERE T.DOC_NO='"+theDocNo+"' AND T.DOC_NO=H.DOC_NO AND REVISION_NO=1");
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
            setAttribute("FILE_NAME", rsResultSet.getString("FILE_NAME"));
            //Now Populate the collection
            //first clear the collection
            colMRItems.clear();

            //String mCompanyID=Long.toString( (long) getAttribute("COMPANY_ID").getVal());
            String mDocNo = (String) getAttribute("DOC_NO").getObj();

            tmpStmt = tmpConn.createStatement();
            if (HistoryView) {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.TL_CUT_H WHERE DOC_NO='" + mDocNo + "' AND REVISION_NO=" + RevNo + " ORDER BY QUALITY_ID,SHADE,PIECE_NO");
            } else {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.TL_CUT WHERE DOC_NO='" + mDocNo + "' ORDER BY QUALITY_ID,SHADE,PIECE_NO");
            }
            rsTmp.first();

            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {
                Counter = Counter + 1;
                clsTLcutItem ObjMRItems = new clsTLcutItem();
                ObjMRItems.setAttribute("DOC_NO", rsTmp.getString("DOC_NO"));
                ObjMRItems.setAttribute("DOC_DATE", rsTmp.getString("DOC_DATE"));
                ObjMRItems.setAttribute("FILE_NAME", rsTmp.getString("FILE_NAME"));
                ObjMRItems.setAttribute("QUALITY_ID", rsTmp.getString("QUALITY_ID"));
                ObjMRItems.setAttribute("SHADE", rsTmp.getString("SHADE"));
                ObjMRItems.setAttribute("PIECE_NO", rsTmp.getString("PIECE_NO"));
                ObjMRItems.setAttribute("RCVD_DATE", rsTmp.getString("RCVD_DATE"));
                ObjMRItems.setAttribute("FLAG_CD", rsTmp.getString("FLAG_CD"));
                ObjMRItems.setAttribute("RACK_NO", rsTmp.getString("RACK_NO"));
                ObjMRItems.setAttribute("NET_MTR", EITLERPGLOBAL.round(rsTmp.getDouble("NET_MTR"), 2));
                ObjMRItems.setAttribute("BRAND", rsTmp.getString("BRAND"));
                ObjMRItems.setAttribute("CREATED_BY", rsTmp.getLong("CREATED_BY"));
                ObjMRItems.setAttribute("CREATED_DATE", rsTmp.getString("CREATED_DATE"));
                ObjMRItems.setAttribute("MODIFIED_BY", rsTmp.getLong("MODIFIED_BY"));
                ObjMRItems.setAttribute("MODIFIED_DATE", rsTmp.getString("MODIFIED_DATE"));
                //ObjMRItems.setAttribute("CANCELED",rsTmp.getInt("CANCELED"));                
                colMRItems.put(Long.toString(Counter), ObjMRItems);
                rsTmp.next();
            }
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean Filter(String Condition) {
        Ready = false;
        try {
            String strSQL = "";
            strSQL += "SELECT DISTINCT DOC_NO,DOC_DATE,FILE_NAME FROM PRODUCTION.TL_CUT WHERE " + Condition;
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery(strSQL);

            if (!rsResultSet.first()) {
                strSQL = "SELECT DISTINCT DOC_NO,DOC_DATE,FILE_NAME FROM PRODUCTION.TL_CUT WHERE DOC_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND DOC_DATE<='" + EITLERPGLOBAL.FinToDateDB + "' ORDER BY DOC_NO";
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

    //Deletes current record
    public boolean Delete(int pUserID) {
        try {
            int lCompanyID = EITLERPGLOBAL.gCompanyID;
            String lDocNo = (String) getAttribute("DOC_NO").getObj();
            String strSQL = "";

            //First check that record is editable
            String strQry = "DELETE FROM PRODUCTION.TL_CUT WHERE DOC_NO='" + lDocNo + "'";
            data.Execute(strQry);

            LoadData(lCompanyID);
            return true;

        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
}
