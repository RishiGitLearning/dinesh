/*
 * clsSales_Party.java
 *
 * Created on April 3, 2009, 10:22 AM
 */
package SDMLATTPAY.Gratuity;

import SDMLATTPAY.Gratuity.*;
import SDMLATTPAY.Shift.*;
import EITLERP.*;
import SDMLATTPAY.Gratuity.clsEmpWorkingDaysEntry;
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
public class clsEmployeeGratuity {

    public String LastError = "";
    private ResultSet rsResultSet, rsResultSet1;
    private Connection Conn;
    private Statement Stmt;
    DecimalFormat df = new DecimalFormat("##.##");
    private HashMap props;
    public boolean Ready = false;

    private boolean HistoryView = false;
    public static int ModuleID = 853; //
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
    public clsEmployeeGratuity() {
        LastError = "";
        props = new HashMap();
        //props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("GRATUIT_DOC_NO", new Variant(""));
        props.put("GRATUIT_DOC_DATE",new Variant(""));
        props.put("GRATUIT_EMP_NO", new Variant(""));
        props.put("GRATUIT_EMP_NAME", new Variant(""));
        props.put("GRATUIT_EMP_DEPT", new Variant(""));
        props.put("GRATUIT_EMP_DOJ", new Variant(""));
        props.put("GRATUIT_EMP_DOL", new Variant(""));
        props.put("GRATUIT_CALC_DATE", new Variant(""));        
        props.put("GRATUIT_PERIOD", new Variant(0));
        props.put("GRATUIT_PERIOD_DAYS", new Variant(0));
        props.put("GRATUIT_AMT", new Variant(0));
        props.put("GRATUIT_TRUST", new Variant(""));
        props.put("GRATUIT_DEDUCT_AMT", new Variant(""));
        props.put("GRATUIT_NET_CHEQUE1_AMT", new Variant(0));
        props.put("DRAWN_BANK", new Variant(""));
        props.put("PAYMENT_MODE", new Variant(""));
        props.put("CHEQUE1_NO", new Variant(""));
        props.put("CHEQUE1_DATE", new Variant(""));
        props.put("CHEQUE2_NO", new Variant(""));
        props.put("CHEQUE2_DATE", new Variant(""));
        props.put("CHEQUE2_AMT", new Variant(0));
        props.put("GRATUIT_EMP_CAT", new Variant(""));
        
        
        props.put("CREATED_BY", new Variant(""));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(""));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("APPROVED", new Variant(""));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED", new Variant(""));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("REJECTED_REMARKS", new Variant(""));
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
            rsResultSet = Stmt.executeQuery("SELECT * FROM SDMLATTPAY.EMP_GRATUITY_DETAIL ORDER BY GRATUIT_DOC_NO ");
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

            rsHistory = stHistory.executeQuery("SELECT * FROM SDMLATTPAY.EMP_GRATUITY_DETAIL_H WHERE GRATUIT_DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();

            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateString("GRATUIT_DOC_NO", getAttribute("GRATUIT_DOC_NO").getString());
            rsResultSet.updateString("GRATUIT_DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("GRATUIT_DOC_DATE").getString()));
            rsResultSet.updateString("GRATUIT_EMP_NO", getAttribute("GRATUIT_EMP_NO").getString());
            rsResultSet.updateString("GRATUIT_EMP_NAME", getAttribute("GRATUIT_EMP_NAME").getString());
            rsResultSet.updateString("GRATUIT_EMP_DEPT", getAttribute("GRATUIT_EMP_DEPT").getString());
            rsResultSet.updateString("GRATUIT_EMP_CAT", getAttribute("GRATUIT_EMP_CAT").getString());
            rsResultSet.updateString("GRATUIT_EMP_DOJ", EITLERPGLOBAL.formatDateDB(getAttribute("GRATUIT_EMP_DOJ").getString()));
            rsResultSet.updateString("GRATUIT_EMP_DOL", EITLERPGLOBAL.formatDateDB(getAttribute("GRATUIT_EMP_DOL").getString()));
            rsResultSet.updateString("GRATUIT_CALC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("GRATUIT_CALC_DATE").getString()));
            rsResultSet.updateString("GRATUIT_PERIOD", getAttribute("GRATUIT_PERIOD").getString());
            rsResultSet.updateString("GRATUIT_PERIOD_DAYS", getAttribute("GRATUIT_PERIOD_DAYS").getString());
            rsResultSet.updateString("GRATUIT_AMT", getAttribute("GRATUIT_AMT").getString());
            rsResultSet.updateString("GRATUIT_TRUST", getAttribute("GRATUIT_TRUST").getString());
            rsResultSet.updateString("GRATUIT_DEDUCT_AMT", getAttribute("GRATUIT_DEDUCT_AMT").getString());
            rsResultSet.updateString("GRATUIT_NET_CHEQUE1_AMT", getAttribute("GRATUIT_NET_CHEQUE1_AMT").getString());
            rsResultSet.updateString("DRAWN_BANK", getAttribute("DRAWN_BANK").getString());
            rsResultSet.updateString("PAYMENT_MODE", getAttribute("PAYMENT_MODE").getString());            
            rsResultSet.updateString("CHEQUE1_NO", getAttribute("CHEQUE1_NO").getString());
            rsResultSet.updateString("CHEQUE1_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CHEQUE1_DATE").getString()));
            rsResultSet.updateString("CHEQUE2_NO", getAttribute("CHEQUE2_NO").getString());
            rsResultSet.updateString("CHEQUE2_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CHEQUE2_DATE").getString()));
            rsResultSet.updateString("CHEQUE2_AMT", getAttribute("CHEQUE2_AMT").getString());
            
            
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

            //========= Inserting Into History =================//
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM SDMLATTPAY.EMP_GRATUITY_DETAIL_H WHERE GRATUIT_DOC_NO='" + (String) getAttribute("GRATUIT_DOC_NO").getObj() + "'");
            RevNo++;
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO", 1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY", (int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            rsHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
            rsHistory.updateString("GRATUIT_DOC_NO", getAttribute("GRATUIT_DOC_NO").getString());
            rsHistory.updateString("GRATUIT_DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("GRATUIT_DOC_DATE").getString()));
            rsHistory.updateString("GRATUIT_EMP_NO", getAttribute("GRATUIT_EMP_NO").getString());
            rsHistory.updateString("GRATUIT_EMP_NAME", getAttribute("GRATUIT_EMP_NAME").getString());
            rsHistory.updateString("GRATUIT_EMP_DEPT", getAttribute("GRATUIT_EMP_DEPT").getString());
            rsHistory.updateString("GRATUIT_EMP_CAT", getAttribute("GRATUIT_EMP_CAT").getString());
            rsHistory.updateString("GRATUIT_EMP_DOJ", EITLERPGLOBAL.formatDateDB(getAttribute("GRATUIT_EMP_DOJ").getString()));
            rsHistory.updateString("GRATUIT_EMP_DOL", EITLERPGLOBAL.formatDateDB(getAttribute("GRATUIT_EMP_DOL").getString()));
            rsHistory.updateString("GRATUIT_CALC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("GRATUIT_CALC_DATE").getString()));
            rsHistory.updateString("GRATUIT_PERIOD", getAttribute("GRATUIT_PERIOD").getString());
            rsHistory.updateString("GRATUIT_PERIOD_DAYS", getAttribute("GRATUIT_PERIOD_DAYS").getString());
            rsHistory.updateString("GRATUIT_AMT", getAttribute("GRATUIT_AMT").getString());
            rsHistory.updateString("GRATUIT_TRUST", getAttribute("GRATUIT_TRU ST").getString());
            rsHistory.updateString("GRATUIT_DEDUCT_AMT", getAttribute("GRATUIT_DEDUCT_AMT").getString());
            rsHistory.updateString("GRATUIT_NET_CHEQUE1_AMT", getAttribute("GRATUIT_NET_CHEQUE1_AMT").getString());
            rsHistory.updateString("DRAWN_BANK", getAttribute("DRAWN_BANK").getString());
            rsHistory.updateString("PAYMENT_MODE", getAttribute("PAYMENT_MODE").getString());            
            rsHistory.updateString("CHEQUE1_NO", getAttribute("CHEQUE1_NO").getString());
            rsHistory.updateString("CHEQUE1_DATE", getAttribute("CHEQUE1_DATE").getString());
            rsHistory.updateString("CHEQUE2_NO", getAttribute("CHEQUE2_NO").getString());
            rsHistory.updateString("CHEQUE2_DATE", getAttribute("CHEQUE2_DATE").getString());
            rsHistory.updateString("CHEQUE2_AMT", getAttribute("CHEQUE2_AMT").getString());
            
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
            
            
            
            Statement tmpStmt;

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery("SELECT * FROM SDMLATTPAY.EMP_WORKINGDAYS_ENTRY WHERE EMP_NO='1'");

            //Now Insert records into detail table
            for (int i = 1; i <= colMRItems.size(); i++) {
                clsEmpWorkingDaysEntry ObjMRItems = (clsEmpWorkingDaysEntry) colMRItems.get(Integer.toString(i));

                rsTmp.moveToInsertRow();
                rsTmp.updateString("EMP_NO", (String) ObjMRItems.getAttribute("EMP_NO").getString());
                rsTmp.updateString("FROM_YEAR", (String) ObjMRItems.getAttribute("FROM_YEAR").getString());
                rsTmp.updateString("TO_YEAR", (String) ObjMRItems.getAttribute("TO_YEAR").getString());
                rsTmp.updateString("WORKING_DAYS", (String) ObjMRItems.getAttribute("WORKING_DAYS").getString());                
                rsTmp.insertRow();
                
            }
            
            //===================== Update the Approval Flow ======================//
            setAttribute("FROM", EITLERPGLOBAL.gUserID);

            //ApprovalFlow ObjFlow=new ApprovalFlow();
            SDMLATTPAY.ApprovalFlow ObjFlow = new SDMLATTPAY.ApprovalFlow();

            ObjFlow.ModuleID = clsEmployeeGratuity.ModuleID;
            ObjFlow.DocNo = (String) getAttribute("GRATUIT_DOC_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("GRATUIT_DOC_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "SDMLATTPAY.EMP_GRATUITY_DETAIL";
            ObjFlow.IsCreator = true;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "GRATUIT_DOC_NO";
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

            rsHistory = stHistory.executeQuery("SELECT * FROM SDMLATTPAY.EMP_GRATUITY_DETAIL_H WHERE GRATUIT_DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//

            String theDocNo = getAttribute("GRATUIT_DOC_NO").getString();

            rsResultSet.updateString("GRATUIT_DOC_NO", getAttribute("GRATUIT_DOC_NO").getString());
            rsResultSet.updateString("GRATUIT_DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("GRATUIT_DOC_DATE").getString()));
            rsResultSet.updateString("GRATUIT_EMP_NO", getAttribute("GRATUIT_EMP_NO").getString());
            rsResultSet.updateString("GRATUIT_EMP_NAME", getAttribute("GRATUIT_EMP_NAME").getString());
            rsResultSet.updateString("GRATUIT_EMP_DEPT", getAttribute("GRATUIT_EMP_DEPT").getString());
            rsResultSet.updateString("GRATUIT_EMP_CAT", getAttribute("GRATUIT_EMP_CAT").getString());
            rsResultSet.updateString("GRATUIT_EMP_DOJ", EITLERPGLOBAL.formatDateDB(getAttribute("GRATUIT_EMP_DOJ").getString()));
            rsResultSet.updateString("GRATUIT_EMP_DOL", EITLERPGLOBAL.formatDateDB(getAttribute("GRATUIT_EMP_DOL").getString()));
            rsResultSet.updateString("GRATUIT_CALC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("GRATUIT_CALC_DATE").getString()));
            rsResultSet.updateString("GRATUIT_PERIOD", getAttribute("GRATUIT_PERIOD").getString());
            rsResultSet.updateString("GRATUIT_PERIOD_DAYS", getAttribute("GRATUIT_PERIOD_DAYS").getString());
            rsResultSet.updateString("GRATUIT_AMT", getAttribute("GRATUIT_AMT").getString());
            rsResultSet.updateString("GRATUIT_TRUST", getAttribute("GRATUIT_TRUST").getString());
            rsResultSet.updateString("GRATUIT_DEDUCT_AMT", getAttribute("GRATUIT_DEDUCT_AMT").getString());
            rsResultSet.updateString("GRATUIT_NET_CHEQUE1_AMT", getAttribute("GRATUIT_NET_CHEQUE1_AMT").getString());
            rsResultSet.updateString("DRAWN_BANK", getAttribute("DRAWN_BANK").getString());
            rsResultSet.updateString("PAYMENT_MODE", getAttribute("PAYMENT_MODE").getString());            
            rsResultSet.updateString("CHEQUE1_NO", getAttribute("CHEQUE1_NO").getString());
            rsResultSet.updateString("CHEQUE1_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CHEQUE1_DATE").getString()));
            rsResultSet.updateString("CHEQUE2_NO", getAttribute("CHEQUE2_NO").getString());
            rsResultSet.updateString("CHEQUE2_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CHEQUE2_DATE").getString()));
            rsResultSet.updateString("CHEQUE2_AMT", getAttribute("CHEQUE2_AMT").getString());
            
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
            rsResultSet.updateRow();

            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM SDMLATTPAY.EMP_GRATUITY_DETAIL_H WHERE GRATUIT_DOC_NO='" + (String) getAttribute("GRATUIT_DOC_NO").getObj() + "'");
            RevNo++;
            String RevDocNo = (String) getAttribute("GRATUIT_DOC_NO").getObj();

            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO", RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY", (int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            rsHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
            rsHistory.updateString("GRATUIT_DOC_NO", getAttribute("GRATUIT_DOC_NO").getString());
            rsHistory.updateString("GRATUIT_DOC_DATE", getAttribute("GRATUIT_DOC_DATE").getString());            
            rsHistory.updateString("GRATUIT_EMP_NO", getAttribute("GRATUIT_EMP_NO").getString());
            rsHistory.updateString("GRATUIT_EMP_NAME", getAttribute("GRATUIT_EMP_NAME").getString());
            rsHistory.updateString("GRATUIT_EMP_DEPT", getAttribute("GRATUIT_EMP_DEPT").getString());
            rsHistory.updateString("GRATUIT_EMP_CAT", getAttribute("GRATUIT_EMP_CAT").getString());
            rsHistory.updateString("GRATUIT_EMP_DOJ", EITLERPGLOBAL.formatDateDB(getAttribute("GRATUIT_EMP_DOJ").getString()));
            rsHistory.updateString("GRATUIT_EMP_DOL", EITLERPGLOBAL.formatDateDB(getAttribute("GRATUIT_EMP_DOL").getString()));
            rsHistory.updateString("GRATUIT_CALC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("GRATUIT_CALC_DATE").getString()));
            rsHistory.updateString("GRATUIT_PERIOD", getAttribute("GRATUIT_PERIOD").getString());
            rsHistory.updateString("GRATUIT_PERIOD_DAYS", getAttribute("GRATUIT_PERIOD_DAYS").getString());
            rsHistory.updateString("GRATUIT_AMT", getAttribute("GRATUIT_AMT").getString());
            rsHistory.updateString("GRATUIT_TRUST", getAttribute("GRATUIT_TRU ST").getString());
            rsHistory.updateString("GRATUIT_DEDUCT_AMT", getAttribute("GRATUIT_DEDUCT_AMT").getString());
            rsHistory.updateString("GRATUIT_NET_CHEQUE1_AMT", getAttribute("GRATUIT_NET_CHEQUE1_AMT").getString());
            rsHistory.updateString("DRAWN_BANK", getAttribute("DRAWN_BANK").getString());
            rsHistory.updateString("PAYMENT_MODE", getAttribute("PAYMENT_MODE").getString());            
            rsHistory.updateString("CHEQUE1_NO", getAttribute("CHEQUE1_NO").getString());
            rsHistory.updateString("CHEQUE1_DATE", getAttribute("CHEQUE1_DATE").getString());
            rsHistory.updateString("CHEQUE2_NO", getAttribute("CHEQUE2_NO").getString());
            rsHistory.updateString("CHEQUE2_DATE", getAttribute("CHEQUE2_DATE").getString());
            rsHistory.updateString("CHEQUE2_AMT", getAttribute("CHEQUE2_AMT").getString());
            
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
            
            Statement tmpStmt;
            boolean updaterec;
            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery("SELECT * FROM SDMLATTPAY.EMP_WORKINGDAYS_ENTRY WHERE EMP_NO='"+getAttribute("GRATUIT_EMP_NO").getString()+"'");
            if (data.IsRecordExist("SELECT * FROM SDMLATTPAY.EMP_WORKINGDAYS_ENTRY WHERE EMP_NO='" + getAttribute("GRATUIT_EMP_NO").getString() + "'")) {
                updaterec = true;
            } else {
                updaterec = false;
            }
            //Now Insert records into detail table
            for (int i = 1; i <= colMRItems.size(); i++) {
                clsEmpWorkingDaysEntry ObjMRItems = (clsEmpWorkingDaysEntry) colMRItems.get(Integer.toString(i));

                rsTmp.moveToInsertRow();
                rsTmp.updateString("EMP_NO", (String) ObjMRItems.getAttribute("EMP_NO").getString());
                rsTmp.updateString("FROM_YEAR", (String) ObjMRItems.getAttribute("FROM_YEAR").getString());
                rsTmp.updateString("TO_YEAR", (String) ObjMRItems.getAttribute("TO_YEAR").getString());
                rsTmp.updateString("WORKING_DAYS", (String) ObjMRItems.getAttribute("WORKING_DAYS").getString());                
                if(updaterec){
                    rsTmp.updateRow();
                }else{
                    rsTmp.insertRow();
                }
                
                
            }

            //======== Update the Approval Flow =========
            setAttribute("FROM", EITLERPGLOBAL.gUserID);

            //ApprovalFlow ObjFlow=new ApprovalFlow();
            SDMLATTPAY.ApprovalFlow ObjFlow = new SDMLATTPAY.ApprovalFlow();

            ObjFlow.ModuleID = clsEmployeeGratuity.ModuleID;
            ObjFlow.DocNo = (String) getAttribute("GRATUIT_DOC_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("GRATUIT_DOC_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "SDMLATTPAY.EMP_GRATUITY_DETAIL";
            ObjFlow.IsCreator = false;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "GRATUIT_DOC_NO";
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
                data.Execute("UPDATE SDMLATTPAY.EMP_GRATUITY_DETAIL SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE GRATUIT_DOC_NO='" + ObjFlow.DocNo + "' ");

                //Remove Old Records from FELT_DOC_DATA
                data.Execute("DELETE FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=" + clsEmployeeGratuity.ModuleID + " AND DOC_NO='" + ObjFlow.DocNo + "'");

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

            setAttribute("GRATUIT_DOC_NO", rsResultSet.getString("GRATUIT_DOC_NO"));
            setAttribute("GRATUIT_DOC_DATE", rsResultSet.getString("GRATUIT_DOC_DATE"));            
            
            setAttribute("GRATUIT_EMP_NO", rsResultSet.getString("GRATUIT_EMP_NO"));
            setAttribute("GRATUIT_EMP_NAME", rsResultSet.getString("GRATUIT_EMP_NAME"));
            setAttribute("GRATUIT_EMP_DEPT", rsResultSet.getString("GRATUIT_EMP_DEPT"));
            setAttribute("GRATUIT_EMP_DOJ", rsResultSet.getString("GRATUIT_EMP_DOJ"));
            setAttribute("GRATUIT_EMP_DOL", rsResultSet.getString("GRATUIT_EMP_DOL"));
            setAttribute("GRATUIT_CALC_DATE", rsResultSet.getString("GRATUIT_CALC_DATE"));
            setAttribute("GRATUIT_PERIOD", rsResultSet.getString("GRATUIT_PERIOD"));
            setAttribute("GRATUIT_PERIOD_DAYS", rsResultSet.getString("GRATUIT_PERIOD_DAYS"));
            setAttribute("GRATUIT_AMT", rsResultSet.getString("GRATUIT_AMT"));
            setAttribute("GRATUIT_TRUST", rsResultSet.getString("GRATUIT_TRUST"));
            setAttribute("GRATUIT_DEDUCT_AMT", rsResultSet.getString("GRATUIT_DEDUCT_AMT"));
            setAttribute("GRATUIT_NET_CHEQUE1_AMT", rsResultSet.getString("GRATUIT_NET_CHEQUE1_AMT"));
            
            
            setAttribute("DRAWN_BANK", rsResultSet.getString("DRAWN_BANK"));
            setAttribute("PAYMENT_MODE", rsResultSet.getString("PAYMENT_MODE"));
            setAttribute("CHEQUE1_NO", rsResultSet.getString("CHEQUE1_NO"));
            setAttribute("CHEQUE1_DATE", rsResultSet.getString("CHEQUE1_DATE"));
            setAttribute("CHEQUE2_NO", rsResultSet.getString("CHEQUE2_NO"));
            setAttribute("CHEQUE2_DATE", rsResultSet.getString("CHEQUE2_DATE"));
            setAttribute("CHEQUE2_AMT", rsResultSet.getString("CHEQUE2_AMT"));
            setAttribute("GRATUIT_EMP_CAT", rsResultSet.getString("GRATUIT_EMP_CAT"));
            
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
            
            
            int count=0;
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);            
            rsTmp=tmpStmt.executeQuery("SELECT * FROM SDMLATTPAY.EMP_WORKINGDAYS_ENTRY WHERE  EMP_NO='"+rsResultSet.getString("GRATUIT_EMP_NO")+"' ORDER BY FROM_YEAR");
            
            rsTmp.first();
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                count++;                
                clsEmpWorkingDaysEntry ObjItem=new clsEmpWorkingDaysEntry();
                ObjItem.setAttribute("FROM_YEAR",rsTmp.getString("FROM_YEAR"));
                ObjItem.setAttribute("TO_YEAR",rsTmp.getString("TO_YEAR"));
                ObjItem.setAttribute("WORKING_DAYS",rsTmp.getString("WORKING_DAYS"));
                colMRItems.put(Integer.toString(count),ObjItem);
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.EMP_GRATUITY_DETAIL WHERE GRATUIT_DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID="+ModuleID+" AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND STATUS='W'";
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
            String strSQL = "SELECT * FROM SDMLATTPAY.EMP_GRATUITY_DETAIL_H WHERE GRATUIT_DOC_NO='" + DocNo + "' ";
            rsTmp = data.getResult(strSQL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                while (!rsTmp.isAfterLast()) {
                    clsEmployeeGratuity objParty = new clsEmployeeGratuity();

                    objParty.setAttribute("GRATUIT_DOC_NO", rsTmp.getString("GRATUIT_DOC_NO"));
                    objParty.setAttribute("GRATUIT_DOC_DATE", rsTmp.getString("GRATUIT_DOC_DATE"));
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
            strSQL += "SELECT * FROM SDMLATTPAY.EMP_GRATUITY_DETAIL WHERE " + Condition + " ";
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery(strSQL);

            if (!rsResultSet.first()) {
                strSQL = "SELECT * FROM SDMLATTPAY.EMP_GRATUITY_DETAIL WHERE GRATUIT_DOC_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND GRATUIT_DOC_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'     ORDER BY GRATUIT_DOC_NO";
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.EMP_GRATUITY_DETAIL WHERE GRATUIT_DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

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
            String lDocNo = (String) getAttribute("GRATUIT_DOC_NO").getObj();
            String strSQL = "";

            //First check that record is editable
            if (CanDelete(lCompanyID, lDocNo, pUserID)) {
                String strQry = "DELETE FROM SDMLATTPAY.EMP_GRATUITY_DETAIL WHERE GRATUIT_DOC_NO='" + lDocNo + "'";
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
                strSQL = "SELECT DISTINCT SDMLATTPAY.EMP_GRATUITY_DETAIL.GRATUIT_DOC_NO,SDMLATTPAY.EMP_GRATUITY_DETAIL.GRATUIT_DOC_DATE,RECEIVED_DATE FROM SDMLATTPAY.EMP_GRATUITY_DETAIL,SDMLATTPAY.D_COM_DOC_DATA WHERE SDMLATTPAY.D_COM_DOC_DATA.DOC_NO=SDMLATTPAY.EMP_GRATUITY_DETAIL.GRATUIT_DOC_NO AND SDMLATTPAY.D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND SDMLATTPAY.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID="+ModuleID+" ORDER BY SDMLATTPAY.D_COM_DOC_DATA.RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT SDMLATTPAY.EMP_GRATUITY_DETAIL.GRATUIT_DOC_NO,SDMLATTPAY.EMP_GRATUITY_DETAIL.GRATUIT_DOC_DATE,RECEIVED_DATE FROM SDMLATTPAY.EMP_GRATUITY_DETAIL,SDMLATTPAY.D_COM_DOC_DATA WHERE SDMLATTPAY.D_COM_DOC_DATA.DOC_NO=SDMLATTPAY.EMP_GRATUITY_DETAIL.GRATUIT_DOC_NO AND SDMLATTPAY.D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND SDMLATTPAY.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID="+ModuleID+" ORDER BY SDMLATTPAY.EMP_GRATUITY_DETAIL.GRATUIT_DOC_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT SDMLATTPAY.EMP_GRATUITY_DETAIL.GRATUIT_DOC_NO,SDMLATTPAY.EMP_GRATUITY_DETAIL.GRATUIT_DOC_DATE,RECEIVED_DATE FROM SDMLATTPAY.EMP_GRATUITY_DETAIL,SDMLATTPAY.D_COM_DOC_DATA WHERE SDMLATTPAY.D_COM_DOC_DATA.DOC_NO=SDMLATTPAY.EMP_GRATUITY_DETAIL.GRATUIT_DOC_NO AND SDMLATTPAY.D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND SDMLATTPAY.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID="+ModuleID+" ORDER BY SDMLATTPAY.EMP_GRATUITY_DETAIL.GRATUIT_DOC_NO";
            }

            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {

                Counter = Counter + 1;
                clsEmployeeGratuity ObjItem = new clsEmployeeGratuity();

                //------------- Header Fields --------------------//
                ObjItem.setAttribute("DOC_NO", rsTmp.getString("GRATUIT_DOC_NO"));
                ObjItem.setAttribute("DOC_DATE", rsTmp.getString("GRATUIT_DOC_DATE"));
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
            rsResultSet = Stmt.executeQuery("SELECT * FROM SDMLATTPAY.EMP_GRATUITY_DETAIL_H WHERE GRATUIT_DOC_NO='" + pDocNo + "'   ORDER BY REVISION_NO");
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
            
            rsTmp=stTmp.executeQuery("SELECT GRATUIT_DOC_NO FROM SDMLATTPAY.EMP_GRATUITY_DETAIL WHERE GRATUIT_DOC_NO='"+pDocNo+"'  AND APPROVED=0 AND CANCELED=0");
            
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
                rsTmp=data.getResult("SELECT APPROVED FROM SDMLATTPAY.EMP_GRATUITY_DETAIL WHERE GRATUIT_DOC_NO='"+pAmendNo+"' ");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    Approved=rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();
                
                if(!Approved) {
                    data.Execute("DELETE FROM SDMLATTPAY.D_COM_DOC_DATA WHERE DOC_NO='"+pAmendNo+"' AND MODULE_ID="+ModuleID+"");
                }
                data.Execute("UPDATE SDMLATTPAY.EMP_GRATUITY_DETAIL SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE GRATUIT_DOC_NO='"+pAmendNo+"'");
              
            }
            catch(Exception e) {
               e.printStackTrace();
            }
        }
}
    
    public static HashMap getTrustList(String pCondition) {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getCreatedConn();
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            tmpStmt=tmpConn.createStatement();
            System.out.println("SELECT * FROM SDMLATTPAY.SOCIETY_TRUST_MASTER "+pCondition);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM SDMLATTPAY.SOCIETY_TRUST_MASTER "+pCondition);
            
            Counter=0;
            while(rsTmp.next()) {
                Counter=Counter+1;
                clsEmployeeGratuity ObjTrust =new clsEmployeeGratuity();
                
                ObjTrust.setAttribute("TRUST_ID",rsTmp.getInt("TRUST_ID"));
                ObjTrust.setAttribute("TRUST_NAME",rsTmp.getString("TRUST_NAME"));
                ObjTrust.setAttribute("BANK_NAME",rsTmp.getString("BANK_NAME"));
                ObjTrust.setAttribute("BANK_BRANCH",rsTmp.getString("BANK_BRANCH"));
                ObjTrust.setAttribute("BANK_ACC_NUMBER",rsTmp.getString("BANK_ACC_NUMBER"));
                
                List.put(Long.toString(Counter),ObjTrust);
            }
            
            rsTmp.close();
            tmpStmt.close();
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return List;
   }
}
