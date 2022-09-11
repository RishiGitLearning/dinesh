/*
     * clsSales_Party.java
     *
     * Created on April 3, 2009, 10:22 AM
 */
package EITLERP.FeltSales.Perfomainvoice;

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
public class clsProforma {

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
    public clsProforma() {
        LastError = "";
        props = new HashMap();
        //props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("PROFORMA_NO", new Variant(""));
        props.put("PROFORMA_DATE", new Variant(""));
        props.put("PARTY_CD", new Variant(""));
        props.put("NAME", new Variant(""));
        props.put("STATION", new Variant(""));
        props.put("CONTACT", new Variant(""));
        props.put("PHONE", new Variant(""));
        props.put("REMARK1", new Variant(0));
        props.put("REMARK2", new Variant(0));
        props.put("REMARK3", new Variant(0));
        props.put("REMARK4", new Variant(0));
        props.put("REMARK5", new Variant(0));

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
            rsResultSet = Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER WHERE PROFORMA_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND PROFORMA_DATE<='" + EITLERPGLOBAL.FinToDateDB + "' ORDER BY PROFORMA_NO");
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
            java.sql.Date ProformaDate = java.sql.Date.valueOf((String) getAttribute("PROFORMA_DATE").getObj());

            if ((ProformaDate.after(FinFromDate) || ProformaDate.compareTo(FinFromDate) == 0) && (ProformaDate.before(FinToDate) || ProformaDate.compareTo(FinToDate) == 0)) {
                //Within the year
            } else {
                LastError = "Requisition date is not within financial year.";
                return false;
            }
            //=====================================================//

            // ---- History Related Changes ------ //
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHDetail = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rsHistory = stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER_H WHERE PROFORMA_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail = stHDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL_H WHERE PROFORMA_NO='1'");
            rsHDetail.first();
            //------------------------------------//

            //--------- Generate Gatepass requisition no.  ------------
            setAttribute("PROFORMA_NO", clsFirstFree.getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 708, (int) getAttribute("FFNO").getVal(), true));
            //-------------------------------------------------

            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateString("PROFORMA_NO", getAttribute("PROFORMA_NO").getString());
            rsResultSet.updateString("PROFORMA_DATE", getAttribute("PROFORMA_DATE").getString());
            rsResultSet.updateString("PARTY_CD", getAttribute("PARTY_CD").getString());
            rsResultSet.updateString("NAME", getAttribute("NAME").getString());
            rsResultSet.updateString("STATION", getAttribute("STATION").getString());
            rsResultSet.updateString("CONTACT", getAttribute("CONTACT").getString());
            rsResultSet.updateString("PHONE", getAttribute("PHONE").getString());
            rsResultSet.updateString("REMARK1", getAttribute("REMARK1").getString());
            rsResultSet.updateString("REMARK2", getAttribute("REMARK2").getString());
            rsResultSet.updateString("REMARK3", getAttribute("REMARK3").getString());
            rsResultSet.updateString("REMARK4", getAttribute("REMARK4").getString());
            rsResultSet.updateString("REMARK5", getAttribute("REMARK5").getString());
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

            rsResultSet.insertRow();

            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO", 1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY", (int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            rsHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
            rsHistory.updateString("PROFORMA_NO", getAttribute("PROFORMA_NO").getString());
            rsHistory.updateString("PROFORMA_DATE", getAttribute("PROFORMA_DATE").getString());
            rsHistory.updateString("PARTY_CD", getAttribute("PARTY_CD").getString());
            rsHistory.updateString("NAME", getAttribute("NAME").getString());
            rsHistory.updateString("STATION", getAttribute("STATION").getString());
            rsHistory.updateString("CONTACT", getAttribute("CONTACT").getString());
            rsHistory.updateString("PHONE", getAttribute("PHONE").getString());
            rsHistory.updateString("REMARK1", getAttribute("REMARK1").getString());
            rsHistory.updateString("REMARK2", getAttribute("REMARK2").getString());
            rsHistory.updateString("REMARK3", getAttribute("REMARK3").getString());
            rsHistory.updateString("REMARK4", getAttribute("REMARK4").getString());
            rsHistory.updateString("REMARK5", getAttribute("REMARK5").getString());
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

            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");

            rsHistory.updateString("FROM_IP", "" + str_split[1]);

            rsHistory.insertRow();

            System.out.println(1);

            Statement tmpStmt;

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL WHERE PROFORMA_NO='1'");

            //Now Insert records into detail table
            for (int i = 1; i <= colMRItems.size(); i++) {
                clsProformaItem ObjMRItems = (clsProformaItem) colMRItems.get(Integer.toString(i));

                rsTmp.moveToInsertRow();

                rsTmp.updateString("PROFORMA_NO", (String) getAttribute("PROFORMA_NO").getObj());
                rsTmp.updateInt("SR_NO", i);
                rsTmp.updateString("PIECE_NO", (String) ObjMRItems.getAttribute("PIECE_NO").getObj());
                rsTmp.updateString("ITEM_DESC", (String) ObjMRItems.getAttribute("ITEM_DESC").getObj());
                rsTmp.updateString("POSITION", (String) ObjMRItems.getAttribute("POSITION").getObj());
                rsTmp.updateDouble("LNGTH", (double) ObjMRItems.getAttribute("LNGTH").getVal());
                rsTmp.updateDouble("WIDTH", (double) ObjMRItems.getAttribute("WIDTH").getVal());
                rsTmp.updateLong("GSQ", (int) ObjMRItems.getAttribute("GSQ").getVal());
                rsTmp.updateString("STYLE", (String) ObjMRItems.getAttribute("STYLE").getObj());
                rsTmp.updateString("WEIGHT", df.format(ObjMRItems.getAttribute("WEIGHT").getVal()).toString());
                rsTmp.updateDouble("RATE", (double) ObjMRItems.getAttribute("RATE").getVal());
                rsTmp.updateFloat("BAS_AMT", (float) ObjMRItems.getAttribute("BAS_AMT").getVal());
                rsTmp.updateFloat("CHEM_TRT_CHG", (float) ObjMRItems.getAttribute("CHEM_TRT_CHG").getVal());
                rsTmp.updateFloat("PIN_CHG", (float) ObjMRItems.getAttribute("PIN_CHG").getVal());
                rsTmp.updateFloat("SPIRAL_CHG", (float) ObjMRItems.getAttribute("SPIRAL_CHG").getVal());
                rsTmp.updateString("DISC_PER", df.format(ObjMRItems.getAttribute("DISC_PER").getVal()).toString());
                rsTmp.updateFloat("DISAMT", (float) ObjMRItems.getAttribute("DISAMT").getVal());
                rsTmp.updateFloat("DISBASAMT", (float) ObjMRItems.getAttribute("DISBASAMT").getVal()); //Balance Qty will be balance qty
                rsTmp.updateFloat("EXCISE", (float) ObjMRItems.getAttribute("EXCISE").getVal());
                rsTmp.updateFloat("SEAM_CHG", (float) ObjMRItems.getAttribute("SEAM_CHG").getVal());
                rsTmp.updateFloat("SEAM_CHG_PER", (float) ObjMRItems.getAttribute("SEAM_CHG_PER").getVal());
                rsTmp.updateFloat("INSACC_AMT", (float) ObjMRItems.getAttribute("INSACC_AMT").getVal());
                rsTmp.updateFloat("INV_AMT", (float) ObjMRItems.getAttribute("INV_AMT").getVal());
                rsTmp.updateFloat("INS_IND", (float) ObjMRItems.getAttribute("INS_IND").getVal());
                rsTmp.updateString("MACHINE_NO", (String) ObjMRItems.getAttribute("MACHINE_NO").getObj());
                rsTmp.updateString("ORDER_DATE", EITLERPGLOBAL.formatDateDB(ObjMRItems.getAttribute("ORDER_DATE").getString()));
                rsTmp.updateString("INCHARGE_NAME", (String) ObjMRItems.getAttribute("INCHARGE_NAME").getObj());
                rsTmp.updateString("PRODUCT_CD", (String) ObjMRItems.getAttribute("PRODUCT_CD").getObj());
                rsTmp.updateString("PO_NO", (String) ObjMRItems.getAttribute("PO_NO").getObj());
                if (EITLERPGLOBAL.formatDateDB(ObjMRItems.getAttribute("PO_DATE").getString()).equals("")) {
                    ObjMRItems.setAttribute("PO_DATE", "0000-00-00");
                } else {
                    ObjMRItems.setAttribute("PO_DATE", EITLERPGLOBAL.formatDateDB(ObjMRItems.getAttribute("PO_DATE").getString()));
                }
                rsTmp.updateString("PO_DATE", ObjMRItems.getAttribute("PO_DATE").getString());
                rsTmp.updateFloat("VAT", (float) ObjMRItems.getAttribute("VAT").getVal());
                rsTmp.updateFloat("CST", (float) ObjMRItems.getAttribute("CST").getVal());
                rsTmp.updateFloat("SD_AMT", (float) ObjMRItems.getAttribute("SD_AMT").getVal());
                rsTmp.updateFloat("FIVE_PER_WEIGHT", (float) ObjMRItems.getAttribute("FIVE_PER_WEIGHT").getVal());
                rsTmp.updateFloat("PRO_INV_AMT", (float) ObjMRItems.getAttribute("PRO_INV_AMT").getVal());
                rsTmp.updateFloat("INV_VAL", Math.round((float) ObjMRItems.getAttribute("INV_VAL").getVal()));
                rsTmp.updateString("INV_VAL_WORD", (String) ObjMRItems.getAttribute("INV_VAL_WORD").getObj());
                rsTmp.updateFloat("CGST_AMT", (float) ObjMRItems.getAttribute("CGST_AMT").getVal());
                rsTmp.updateFloat("SGST_AMT", (float) ObjMRItems.getAttribute("SGST_AMT").getVal());
                rsTmp.updateFloat("IGST_AMT", (float) ObjMRItems.getAttribute("IGST_AMT").getVal());
                rsTmp.updateFloat("TCS_PER", (float) ObjMRItems.getAttribute("TCS_PER").getVal());
                rsTmp.updateFloat("TCS_AMT", (float) ObjMRItems.getAttribute("TCS_AMT").getVal());
                rsTmp.updateFloat("SQMTR", (float) ObjMRItems.getAttribute("SQMTR").getVal());
                rsTmp.updateString("UOM", (String) ObjMRItems.getAttribute("UOM").getObj());
                rsTmp.updateInt("CREATED_BY", ObjMRItems.getAttribute("CREATED_BY").getInt());
                rsTmp.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED", true);
                rsTmp.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateFloat("AOSD_PER", (float) ObjMRItems.getAttribute("AOSD_PER").getVal());
                rsTmp.updateFloat("AOSD_AMT", (float) ObjMRItems.getAttribute("AOSD_AMT").getVal());
                rsTmp.updateDouble("SURCHARGE_PER", (double) ObjMRItems.getAttribute("SURCHARGE_PER").getVal());
                rsTmp.updateDouble("SURCHARGE_RATE", (double) ObjMRItems.getAttribute("SURCHARGE_RATE").getVal());
                rsTmp.updateDouble("GROSS_RATE", (double) ObjMRItems.getAttribute("GROSS_RATE").getVal());
                rsTmp.insertRow();

                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO", 1);
                rsHDetail.updateString("PROFORMA_NO", (String) getAttribute("PROFORMA_NO").getObj());
                rsHDetail.updateInt("SR_NO", i);
                rsHDetail.updateString("PIECE_NO", (String) ObjMRItems.getAttribute("PIECE_NO").getObj());
                rsHDetail.updateString("ITEM_DESC", (String) ObjMRItems.getAttribute("ITEM_DESC").getObj());
                rsHDetail.updateString("POSITION", (String) ObjMRItems.getAttribute("POSITION").getObj());
                rsHDetail.updateDouble("LNGTH", (double) ObjMRItems.getAttribute("LNGTH").getVal());
                rsHDetail.updateDouble("WIDTH", (double) ObjMRItems.getAttribute("WIDTH").getVal());
                rsHDetail.updateLong("GSQ", (int) ObjMRItems.getAttribute("GSQ").getVal());
                rsHDetail.updateString("STYLE", (String) ObjMRItems.getAttribute("STYLE").getObj());
                rsHDetail.updateString("WEIGHT", df.format(ObjMRItems.getAttribute("WEIGHT").getVal()).toString());
                rsHDetail.updateDouble("RATE", (double) ObjMRItems.getAttribute("RATE").getVal());
                rsHDetail.updateFloat("BAS_AMT", (float) ObjMRItems.getAttribute("BAS_AMT").getVal());
                rsHDetail.updateFloat("CHEM_TRT_CHG", (float) ObjMRItems.getAttribute("CHEM_TRT_CHG").getVal());
                rsHDetail.updateFloat("PIN_CHG", (float) ObjMRItems.getAttribute("PIN_CHG").getVal());
                rsHDetail.updateFloat("SPIRAL_CHG", (float) ObjMRItems.getAttribute("SPIRAL_CHG").getVal());
                rsHDetail.updateString("DISC_PER", df.format(ObjMRItems.getAttribute("DISC_PER").getVal()).toString());
                rsHDetail.updateFloat("DISAMT", (float) ObjMRItems.getAttribute("DISAMT").getVal());
                rsHDetail.updateFloat("DISBASAMT", (float) ObjMRItems.getAttribute("DISBASAMT").getVal()); //Balance Qty will be balance qty
                rsHDetail.updateFloat("EXCISE", (float) ObjMRItems.getAttribute("EXCISE").getVal());
                rsHDetail.updateFloat("SEAM_CHG", (float) ObjMRItems.getAttribute("SEAM_CHG").getVal());
                rsHDetail.updateFloat("SEAM_CHG_PER", (float) ObjMRItems.getAttribute("SEAM_CHG_PER").getVal());
                rsHDetail.updateFloat("INSACC_AMT", (float) ObjMRItems.getAttribute("INSACC_AMT").getVal());
                rsHDetail.updateFloat("INV_AMT", (float) ObjMRItems.getAttribute("INV_AMT").getVal());
                rsHDetail.updateFloat("INS_IND", (float) ObjMRItems.getAttribute("INS_IND").getVal());
                rsHDetail.updateString("MACHINE_NO", (String) ObjMRItems.getAttribute("MACHINE_NO").getObj());
                rsHDetail.updateString("ORDER_DATE", EITLERPGLOBAL.formatDateDB((String) ObjMRItems.getAttribute("ORDER_DATE").getObj()));
                rsHDetail.updateString("INCHARGE_NAME", (String) ObjMRItems.getAttribute("INCHARGE_NAME").getObj());
                rsHDetail.updateString("PRODUCT_CD", (String) ObjMRItems.getAttribute("PRODUCT_CD").getObj());
                rsHDetail.updateString("PO_NO", (String) ObjMRItems.getAttribute("PO_NO").getObj());
                if (EITLERPGLOBAL.formatDateDB(ObjMRItems.getAttribute("PO_DATE").getString()).equals("")) {
                    ObjMRItems.setAttribute("PO_DATE", "0000-00-00");
                } else {
                    ObjMRItems.setAttribute("PO_DATE", EITLERPGLOBAL.formatDateDB(ObjMRItems.getAttribute("PO_DATE").getString()));
                }
                rsHDetail.updateString("PO_DATE", (String) ObjMRItems.getAttribute("PO_DATE").getObj());
                rsHDetail.updateFloat("VAT", (float) ObjMRItems.getAttribute("VAT").getVal());
                rsHDetail.updateFloat("CST", (float) ObjMRItems.getAttribute("CST").getVal());
                rsHDetail.updateFloat("SD_AMT", (float) ObjMRItems.getAttribute("SD_AMT").getVal());
                rsHDetail.updateFloat("FIVE_PER_WEIGHT", (float) ObjMRItems.getAttribute("FIVE_PER_WEIGHT").getVal());
                rsHDetail.updateFloat("PRO_INV_AMT", (float) ObjMRItems.getAttribute("PRO_INV_AMT").getVal());
                rsHDetail.updateFloat("INV_VAL", Math.round((float) ObjMRItems.getAttribute("INV_VAL").getVal()));
                rsHDetail.updateString("INV_VAL_WORD", (String) ObjMRItems.getAttribute("INV_VAL_WORD").getObj());
                rsHDetail.updateFloat("CGST_AMT", (float) ObjMRItems.getAttribute("CGST_AMT").getVal());
                rsHDetail.updateFloat("SGST_AMT", (float) ObjMRItems.getAttribute("SGST_AMT").getVal());
                rsHDetail.updateFloat("IGST_AMT", (float) ObjMRItems.getAttribute("IGST_AMT").getVal());
                rsHDetail.updateFloat("TCS_PER", (float) ObjMRItems.getAttribute("TCS_PER").getVal());
                rsHDetail.updateFloat("TCS_AMT", (float) ObjMRItems.getAttribute("TCS_AMT").getVal());
                rsHDetail.updateFloat("SQMTR", (float) ObjMRItems.getAttribute("SQMTR").getVal());
                rsHDetail.updateString("UOM", (String) ObjMRItems.getAttribute("UOM").getObj());
                rsHDetail.updateInt("CREATED_BY", ObjMRItems.getAttribute("CREATED_BY").getInt());
                rsHDetail.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateBoolean("CHANGED", true);
                rsHDetail.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateFloat("AOSD_PER", (float) ObjMRItems.getAttribute("AOSD_PER").getVal());
                rsHDetail.updateFloat("AOSD_AMT", (float) ObjMRItems.getAttribute("AOSD_AMT").getVal());
                rsHDetail.updateDouble("SURCHARGE_PER", (double) ObjMRItems.getAttribute("SURCHARGE_PER").getVal());
                rsHDetail.updateDouble("SURCHARGE_RATE", (double) ObjMRItems.getAttribute("SURCHARGE_RATE").getVal());
                rsHDetail.updateDouble("GROSS_RATE", (double) ObjMRItems.getAttribute("GROSS_RATE").getVal());
                rsHDetail.insertRow();
            }

            //===================== Update the Approval Flow ======================//
            setAttribute("FROM", EITLERPGLOBAL.gUserID);

            //ApprovalFlow ObjFlow=new ApprovalFlow();
            clsFeltProductionApprovalFlow ObjFlow = new clsFeltProductionApprovalFlow();

            ObjFlow.ModuleID = clsProforma.ModuleID;
            ObjFlow.DocNo = (String) getAttribute("PROFORMA_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("PROFORMA_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "PRODUCTION.FELT_PROFORMA_INVOICE_HEADER";
            ObjFlow.IsCreator = true;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "PROFORMA_NO";

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

            String AStatus = (String) getAttribute("APPROVAL_STATUS").getObj();
            if (AStatus.equals("R")) {
                Validate = false;
            }
            Validate = true;
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate = java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate = java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date ProformaDate = java.sql.Date.valueOf((String) getAttribute("PROFORMA_DATE").getObj());

            if ((ProformaDate.after(FinFromDate) || ProformaDate.compareTo(FinFromDate) == 0) && (ProformaDate.before(FinToDate) || ProformaDate.compareTo(FinToDate) == 0)) {
                //Withing the year
            } else {
                LastError = "Proforma date is not within financial year.";
                return false;
            }
            //=====================================================//

            // ---- History Related Changes ------ //
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHDetail = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rsHistory = stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER_H WHERE PROFORMA_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail = stHDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL_H WHERE PROFORMA_NO='1'");
            rsHDetail.first();
            //------------------------------------//

            String theDocNo = getAttribute("PROFORMA_NO").getString();

            rsResultSet.updateString("PROFORMA_DATE", getAttribute("PROFORMA_DATE").getString());
            rsResultSet.updateString("PARTY_CD", getAttribute("PARTY_CD").getString());
            rsResultSet.updateString("NAME", getAttribute("NAME").getString());
            rsResultSet.updateString("STATION", getAttribute("STATION").getString());
            rsResultSet.updateString("CONTACT", getAttribute("CONTACT").getString());
            rsResultSet.updateString("PHONE", getAttribute("PHONE").getString());
            rsResultSet.updateString("REMARK1", getAttribute("REMARK1").getString());
            rsResultSet.updateString("REMARK2", getAttribute("REMARK2").getString());
            rsResultSet.updateString("REMARK3", getAttribute("REMARK3").getString());
            rsResultSet.updateString("REMARK4", getAttribute("REMARK4").getString());
            rsResultSet.updateString("REMARK5", getAttribute("REMARK5").getString());
            rsResultSet.updateInt("MODIFIED_BY", getAttribute("MODIFIED_BY").getInt());
            rsResultSet.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE", getAttribute("MODIFIED_DATE").getString());
            rsResultSet.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsResultSet.updateBoolean("CHANGED", true);
            rsResultSet.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELED", false);
            rsResultSet.updateRow();

            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER_H WHERE PROFORMA_NO='" + (String) getAttribute("PROFORMA_NO").getObj() + "'");
            RevNo++;
            String RevDocNo = (String) getAttribute("PROFORMA_NO").getObj();

            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO", RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY", (int) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            rsHistory.updateString("APPROVER_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsHistory.updateString("PROFORMA_NO", getAttribute("PROFORMA_NO").getString());
            rsHistory.updateString("PROFORMA_DATE", getAttribute("PROFORMA_DATE").getString());
            rsHistory.updateString("PARTY_CD", getAttribute("PARTY_CD").getString());
            rsHistory.updateString("NAME", getAttribute("NAME").getString());
            rsHistory.updateString("STATION", getAttribute("STATION").getString());
            rsHistory.updateString("CONTACT", getAttribute("CONTACT").getString());
            rsHistory.updateString("PHONE", getAttribute("PHONE").getString());
            rsHistory.updateString("REMARK1", getAttribute("REMARK1").getString());
            rsHistory.updateString("REMARK2", getAttribute("REMARK2").getString());
            rsHistory.updateString("REMARK3", getAttribute("REMARK3").getString());
            rsHistory.updateString("REMARK4", getAttribute("REMARK4").getString());
            rsHistory.updateString("REMARK5", getAttribute("REMARK5").getString());
            rsHistory.updateInt("MODIFIED_BY", getAttribute("MODIFIED_BY").getInt());
            rsHistory.updateString("MODIFIED_DATE", getAttribute("MODIFIED_DATE").getString());
            rsHistory.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            rsHistory.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsHistory.updateBoolean("CHANGED", true);
            rsHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");

            rsHistory.updateString("FROM_IP", "" + str_split[1]);

            rsHistory.insertRow();

            //First remove the old rows
            String mProformaNo = (String) getAttribute("PROFORMA_NO").getObj();

            data.Execute("DELETE FROM PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL WHERE PROFORMA_NO='" + mProformaNo + "'");

            Statement tmpStmt;

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL WHERE PROFORMA_NO='1'");

            //Now Insert records into detail table
            for (int i = 1; i <= colMRItems.size(); i++) {
                clsProformaItem ObjMRItems = (clsProformaItem) colMRItems.get(Integer.toString(i));
                rsTmp.moveToInsertRow();
                rsTmp.updateString("PROFORMA_NO", (String) getAttribute("PROFORMA_NO").getObj());
                rsTmp.updateLong("SR_NO", i);
                rsTmp.updateString("PIECE_NO", (String) ObjMRItems.getAttribute("PIECE_NO").getObj());
                rsTmp.updateString("ITEM_DESC", (String) ObjMRItems.getAttribute("ITEM_DESC").getObj());
                rsTmp.updateString("POSITION", (String) ObjMRItems.getAttribute("POSITION").getObj());
                rsTmp.updateString("LNGTH", Double.toString(ObjMRItems.getAttribute("LNGTH").getVal()));
                rsTmp.updateString("WIDTH", Double.toString(ObjMRItems.getAttribute("WIDTH").getVal()));
                rsTmp.updateInt("GSQ", (int) ObjMRItems.getAttribute("GSQ").getVal());
                rsTmp.updateString("STYLE", (String) ObjMRItems.getAttribute("STYLE").getObj());
                rsTmp.updateString("WEIGHT", Double.toString(ObjMRItems.getAttribute("WEIGHT").getVal()));
                rsTmp.updateString("RATE", Double.toString(ObjMRItems.getAttribute("RATE").getVal()));
                rsTmp.updateFloat("BAS_AMT", (float) ObjMRItems.getAttribute("BAS_AMT").getVal());
                rsTmp.updateString("ORDER_DATE", EITLERPGLOBAL.formatDateDB(ObjMRItems.getAttribute("ORDER_DATE").getString()));
                rsTmp.updateString("INCHARGE_NAME", (String) ObjMRItems.getAttribute("INCHARGE_NAME").getObj());
                rsTmp.updateString("PRIORITY", (String) ObjMRItems.getAttribute("PRIORITY").getObj());
                rsTmp.updateString("ORDER_DATE", EITLERPGLOBAL.formatDateDB(ObjMRItems.getAttribute("ORDER_DATE").getString()));
                rsTmp.updateFloat("CHEM_TRT_CHG", (float) ObjMRItems.getAttribute("CHEM_TRT_CHG").getVal());
                rsTmp.updateFloat("PIN_CHG", (float) ObjMRItems.getAttribute("PIN_CHG").getVal());
                rsTmp.updateFloat("SPIRAL_CHG", (float) ObjMRItems.getAttribute("SPIRAL_CHG").getVal());
                rsTmp.updateString("DISC_PER", Double.toString(ObjMRItems.getAttribute("DISC_PER").getVal()));
                rsTmp.updateFloat("DISAMT", (float) ObjMRItems.getAttribute("DISAMT").getVal());
                rsTmp.updateFloat("DISBASAMT", (float) ObjMRItems.getAttribute("DISBASAMT").getVal());
                rsTmp.updateFloat("EXCISE", (float) ObjMRItems.getAttribute("EXCISE").getVal());
                rsTmp.updateFloat("SEAM_CHG", (float) ObjMRItems.getAttribute("SEAM_CHG").getVal());
                rsTmp.updateFloat("SEAM_CHG_PER", (float) ObjMRItems.getAttribute("SEAM_CHG_PER").getVal());
                rsTmp.updateFloat("INSACC_AMT", (float) ObjMRItems.getAttribute("INSACC_AMT").getVal());
                rsTmp.updateFloat("INV_AMT", (float) ObjMRItems.getAttribute("INV_AMT").getVal());
                rsTmp.updateFloat("INS_IND", (float) ObjMRItems.getAttribute("INS_IND").getVal());
                rsTmp.updateString("MACHINE_NO", (String) ObjMRItems.getAttribute("MACHINE_NO").getObj());
                rsTmp.updateString("ORDER_DATE", EITLERPGLOBAL.formatDateDB(ObjMRItems.getAttribute("ORDER_DATE").getString()));
                rsTmp.updateString("INCHARGE_NAME", (String) ObjMRItems.getAttribute("INCHARGE_NAME").getObj());
                rsTmp.updateString("PRODUCT_CD", (String) ObjMRItems.getAttribute("PRODUCT_CD").getObj());
                rsTmp.updateString("PO_NO", (String) ObjMRItems.getAttribute("PO_NO").getObj());
                if (EITLERPGLOBAL.formatDateDB(ObjMRItems.getAttribute("PO_DATE").getString()).equals("")) {
                    ObjMRItems.setAttribute("PO_DATE", "0000-00-00");
                } else {
                    ObjMRItems.setAttribute("PO_DATE", EITLERPGLOBAL.formatDateDB(ObjMRItems.getAttribute("PO_DATE").getString()));
                }
                rsTmp.updateString("PO_DATE", ObjMRItems.getAttribute("PO_DATE").getString());
                rsTmp.updateFloat("VAT", (float) ObjMRItems.getAttribute("VAT").getVal());
                rsTmp.updateFloat("CST", (float) ObjMRItems.getAttribute("CST").getVal());
                rsTmp.updateFloat("SD_AMT", (float) ObjMRItems.getAttribute("SD_AMT").getVal());
                rsTmp.updateFloat("FIVE_PER_WEIGHT", (float) ObjMRItems.getAttribute("FIVE_PER_WEIGHT").getVal());
                rsTmp.updateFloat("PRO_INV_AMT", (float) ObjMRItems.getAttribute("PRO_INV_AMT").getVal());
                rsTmp.updateFloat("INV_VAL", Math.round((float) ObjMRItems.getAttribute("INV_VAL").getVal()));
                rsTmp.updateString("INV_VAL_WORD", (String) ObjMRItems.getAttribute("INV_VAL_WORD").getObj());
                rsTmp.updateFloat("CGST_AMT", (float) ObjMRItems.getAttribute("CGST_AMT").getVal());
                rsTmp.updateFloat("SGST_AMT", (float) ObjMRItems.getAttribute("SGST_AMT").getVal());
                rsTmp.updateFloat("IGST_AMT", (float) ObjMRItems.getAttribute("IGST_AMT").getVal());
                rsTmp.updateFloat("TCS_PER", (float) ObjMRItems.getAttribute("TCS_PER").getVal());
                rsTmp.updateFloat("TCS_AMT", (float) ObjMRItems.getAttribute("TCS_AMT").getVal());
                rsTmp.updateFloat("SQMTR", (float) ObjMRItems.getAttribute("SQMTR").getVal());
                rsTmp.updateString("UOM", (String) ObjMRItems.getAttribute("UOM").getObj());
                rsTmp.updateInt("MODIFIED_BY", ObjMRItems.getAttribute("MODIFIED_BY").getInt());
                rsTmp.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED", true);
                rsTmp.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateFloat("AOSD_PER", (float) ObjMRItems.getAttribute("AOSD_PER").getVal());
                rsTmp.updateFloat("AOSD_AMT", (float) ObjMRItems.getAttribute("AOSD_AMT").getVal());
                rsTmp.updateDouble("SURCHARGE_PER", (double) ObjMRItems.getAttribute("SURCHARGE_PER").getVal());
                rsTmp.updateDouble("SURCHARGE_RATE", (double) ObjMRItems.getAttribute("SURCHARGE_RATE").getVal());
                rsTmp.updateDouble("GROSS_RATE", (double) ObjMRItems.getAttribute("GROSS_RATE").getVal());
                rsTmp.insertRow();

                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO", RevNo);
                rsHDetail.updateString("PROFORMA_NO", (String) getAttribute("PROFORMA_NO").getObj());
                rsHDetail.updateLong("SR_NO", i);
                rsHDetail.updateString("ORDER_DATE", EITLERPGLOBAL.formatDateDB(ObjMRItems.getAttribute("ORDER_DATE").getString()));
                rsHDetail.updateString("INCHARGE_NAME", (String) ObjMRItems.getAttribute("INCHARGE_NAME").getObj());
                rsHDetail.updateString("PRIORITY", (String) ObjMRItems.getAttribute("PRIORITY").getObj());
                rsHDetail.updateString("PIECE_NO", (String) ObjMRItems.getAttribute("PIECE_NO").getObj());
                rsHDetail.updateString("ORDER_DATE", EITLERPGLOBAL.formatDateDB(ObjMRItems.getAttribute("ORDER_DATE").getString()));
                rsHDetail.updateString("PRODUCT_CD", (String) ObjMRItems.getAttribute("PRODUCT_CD").getObj());
                rsHDetail.updateString("STYLE", (String) ObjMRItems.getAttribute("STYLE").getObj());
                rsHDetail.updateString("LNGTH", Double.toString(ObjMRItems.getAttribute("LNGTH").getVal()));
                rsHDetail.updateString("RCVD_MTR", Double.toString(ObjMRItems.getAttribute("RCVD_MTR").getVal()));
                rsHDetail.updateString("WIDTH", Double.toString(ObjMRItems.getAttribute("WIDTH").getVal()));
                rsHDetail.updateString("RECD_WDTH", Double.toString(ObjMRItems.getAttribute("RECD_WDTH").getVal()));
                rsHDetail.updateInt("GSQ", (int) ObjMRItems.getAttribute("GSQ").getVal());
                rsHDetail.updateString("WEIGHT", Double.toString(ObjMRItems.getAttribute("WEIGHT").getVal()));
                rsHDetail.updateString("RATE", Double.toString(ObjMRItems.getAttribute("RATE").getVal()));
                rsHDetail.updateFloat("BAS_AMT", (float) ObjMRItems.getAttribute("BAS_AMT").getVal());
                rsHDetail.updateFloat("CHEM_TRT_CHG", (float) ObjMRItems.getAttribute("CHEM_TRT_CHG").getVal());
                rsHDetail.updateFloat("PIN_CHG", (float) ObjMRItems.getAttribute("PIN_CHG").getVal());
                rsHDetail.updateFloat("SPIRAL_CHG", (float) ObjMRItems.getAttribute("SPIRAL_CHG").getVal());
                rsHDetail.updateString("DISC_PER", Double.toString(ObjMRItems.getAttribute("DISC_PER").getVal()));
                rsHDetail.updateFloat("DISAMT", (float) ObjMRItems.getAttribute("DISAMT").getVal());
                rsHDetail.updateFloat("DISBASAMT", (float) ObjMRItems.getAttribute("DISBASAMT").getVal());
                rsHDetail.updateFloat("EXCISE", (float) ObjMRItems.getAttribute("EXCISE").getVal());
                rsHDetail.updateFloat("SEAM_CHG", (float) ObjMRItems.getAttribute("SEAM_CHG").getVal());
                rsHDetail.updateFloat("SEAM_CHG_PER", (float) ObjMRItems.getAttribute("SEAM_CHG_PER").getVal());
                rsHDetail.updateFloat("PO_NO", (float) ObjMRItems.getAttribute("PO_NO").getVal());
                if (EITLERPGLOBAL.formatDateDB(ObjMRItems.getAttribute("PO_DATE").getString()).equals("")) {
                    ObjMRItems.setAttribute("PO_DATE", "0000-00-00");
                } else {
                    ObjMRItems.setAttribute("PO_DATE", EITLERPGLOBAL.formatDateDB(ObjMRItems.getAttribute("PO_DATE").getString()));
                }
                rsHDetail.updateString("PO_DATE", ObjMRItems.getAttribute("PO_DATE").getString());
                rsHDetail.updateFloat("INSACC_AMT", (float) ObjMRItems.getAttribute("INSACC_AMT").getVal());
                rsHDetail.updateFloat("INV_AMT", (float) ObjMRItems.getAttribute("INV_AMT").getVal());
                rsHDetail.updateFloat("INS_IND", (float) ObjMRItems.getAttribute("INS_IND").getVal());
                rsHDetail.updateString("MACHINE_NO", (String) ObjMRItems.getAttribute("MACHINE_NO").getObj());
                rsHDetail.updateString("ORDER_DATE", EITLERPGLOBAL.formatDateDB(ObjMRItems.getAttribute("ORDER_DATE").getString()));
                rsHDetail.updateString("POSITION", (String) ObjMRItems.getAttribute("POSITION").getObj());
                rsHDetail.updateString("STATION", (String) ObjMRItems.getAttribute("STATION").getObj());
                rsHDetail.updateString("ITEM_DESC", (String) ObjMRItems.getAttribute("ITEM_DESC").getObj());
                rsHDetail.updateString("SYN_PER", (String) ObjMRItems.getAttribute("SYN_PER").getObj());
                rsHDetail.updateFloat("VAT", (float) ObjMRItems.getAttribute("VAT").getVal());
                rsHDetail.updateFloat("CST", (float) ObjMRItems.getAttribute("CST").getVal());
                rsHDetail.updateFloat("SD_AMT", (float) ObjMRItems.getAttribute("SD_AMT").getVal());
                rsHDetail.updateFloat("FIVE_PER_WEIGHT", (float) ObjMRItems.getAttribute("FIVE_PER_WEIGHT").getVal());
                rsHDetail.updateFloat("PRO_INV_AMT", (float) ObjMRItems.getAttribute("PRO_INV_AMT").getVal());
                rsHDetail.updateFloat("INV_VAL", Math.round((float) ObjMRItems.getAttribute("INV_VAL").getVal()));
                rsHDetail.updateString("INV_VAL_WORD", (String) ObjMRItems.getAttribute("INV_VAL_WORD").getObj());
                rsHDetail.updateFloat("CGST_AMT", (float) ObjMRItems.getAttribute("CGST_AMT").getVal());
                rsHDetail.updateFloat("SGST_AMT", (float) ObjMRItems.getAttribute("SGST_AMT").getVal());
                rsHDetail.updateFloat("IGST_AMT", (float) ObjMRItems.getAttribute("IGST_AMT").getVal());
                rsHDetail.updateFloat("TCS_PER", (float) ObjMRItems.getAttribute("TCS_PER").getVal());
                rsHDetail.updateFloat("TCS_AMT", (float) ObjMRItems.getAttribute("TCS_AMT").getVal());
                rsHDetail.updateFloat("SQMTR", (float) ObjMRItems.getAttribute("SQMTR").getVal());
                rsHDetail.updateString("UOM", (String) ObjMRItems.getAttribute("UOM").getObj());
                rsHDetail.updateInt("MODIFIED_BY", ObjMRItems.getAttribute("MODIFIED_BY").getInt());
                rsHDetail.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateBoolean("CHANGED", true);
                rsHDetail.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateFloat("AOSD_PER", (float) ObjMRItems.getAttribute("AOSD_PER").getVal());
                rsHDetail.updateFloat("AOSD_AMT", (float) ObjMRItems.getAttribute("AOSD_AMT").getVal());
                rsHDetail.updateDouble("SURCHARGE_PER", (double) ObjMRItems.getAttribute("SURCHARGE_PER").getVal());
                rsHDetail.updateDouble("SURCHARGE_RATE", (double) ObjMRItems.getAttribute("SURCHARGE_RATE").getVal());
                rsHDetail.updateDouble("GROSS_RATE", (double) ObjMRItems.getAttribute("GROSS_RATE").getVal());
                rsHDetail.insertRow();
            }

            //======== Update the Approval Flow =========
            setAttribute("FROM", EITLERPGLOBAL.gUserID);

            //ApprovalFlow ObjFlow=new ApprovalFlow();
            clsFeltProductionApprovalFlow ObjFlow = new clsFeltProductionApprovalFlow();

            ObjFlow.ModuleID = clsProforma.ModuleID;
            ObjFlow.DocNo = (String) getAttribute("PROFORMA_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("PROFORMA_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "PRODUCTION.FELT_PROFORMA_INVOICE_HEADER";
            ObjFlow.IsCreator = false;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "PROFORMA_NO";

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
                data.Execute("UPDATE PRODUCTION.FELT_PROFORMA_INVOICE_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE PROFORMA_NO='" + ObjFlow.DocNo + "' ");

                //Remove Old Records from FELT_DOC_DATA
                data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=" + clsProforma.ModuleID + " AND DOC_NO='" + ObjFlow.DocNo + "'");

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

            setAttribute("PROFORMA_NO", rsResultSet.getString("PROFORMA_NO"));
            setAttribute("PROFORMA_DATE", rsResultSet.getString("PROFORMA_DATE"));
            setAttribute("PARTY_CD", rsResultSet.getString("PARTY_CD"));
            setAttribute("NAME", rsResultSet.getString("NAME"));
            setAttribute("STATION", rsResultSet.getString("STATION"));
            setAttribute("CONTACT", rsResultSet.getString("CONTACT"));
            setAttribute("PHONE", rsResultSet.getString("PHONE"));
            setAttribute("REMARK1", rsResultSet.getString("REMARK1"));
            setAttribute("REMARK2", rsResultSet.getString("REMARK2"));
            setAttribute("REMARK3", rsResultSet.getString("REMARK3"));
            setAttribute("REMARK4", rsResultSet.getString("REMARK4"));
            setAttribute("REMARK5", rsResultSet.getString("REMARK5"));

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

            String mProformaNo = (String) getAttribute("PROFORMA_NO").getObj();

            tmpStmt = tmpConn.createStatement();
            if (HistoryView) {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL_H WHERE PROFORMA_NO='" + mProformaNo + "' AND REVISION_NO=" + RevNo + " ORDER BY SR_NO");
            } else {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL WHERE PROFORMA_NO='" + mProformaNo + "' ORDER BY SR_NO");
            }
            rsTmp.first();

            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {
                Counter = Counter + 1;
                clsProformaItem ObjMRItems = new clsProformaItem();
                ObjMRItems.setAttribute("PROFORMA_NO", rsTmp.getString("PROFORMA_NO"));
                ObjMRItems.setAttribute("SR_NO", rsTmp.getLong("SR_NO"));
                ObjMRItems.setAttribute("PRIORITY_DATE", rsTmp.getString("PRIORITY_DATE"));
                ObjMRItems.setAttribute("INCHARGE_NAME", rsTmp.getString("INCHARGE_NAME"));
                ObjMRItems.setAttribute("PRIORITY", rsTmp.getString("PRIORITY"));
                ObjMRItems.setAttribute("PIECE_NO", rsTmp.getString("PIECE_NO"));
                ObjMRItems.setAttribute("ORDER_DATE", rsTmp.getString("ORDER_DATE"));
                ObjMRItems.setAttribute("RCVD_DATE", rsTmp.getString("RCVD_DATE"));
                ObjMRItems.setAttribute("DELIV_DATE", rsTmp.getString("DELIV_DATE"));
                ObjMRItems.setAttribute("COMM_DATE", rsTmp.getString("COMM_DATE"));
                ObjMRItems.setAttribute("PRODUCT_CD", rsTmp.getString("PRODUCT_CD"));
                ObjMRItems.setAttribute("ITEM", rsTmp.getString("ITEM"));
                ObjMRItems.setAttribute("STYLE", rsTmp.getString("STYLE"));
                ObjMRItems.setAttribute("LNGTH", EITLERPGLOBAL.round(rsTmp.getDouble("LNGTH"), 2));
                ObjMRItems.setAttribute("RCVD_MTR", EITLERPGLOBAL.round(rsTmp.getDouble("RCVD_MTR"), 2));
                ObjMRItems.setAttribute("WIDTH", EITLERPGLOBAL.round(rsTmp.getDouble("WIDTH"), 2));
                ObjMRItems.setAttribute("RECD_WDTH", EITLERPGLOBAL.round(rsTmp.getDouble("RECD_WDTH"), 2));
                ObjMRItems.setAttribute("GSQ", rsTmp.getInt("GSQ"));
                ObjMRItems.setAttribute("WEIGHT", rsTmp.getDouble("WEIGHT"));
                ObjMRItems.setAttribute("SQMTR", EITLERPGLOBAL.round(rsTmp.getFloat("SQMTR"), 2));
                ObjMRItems.setAttribute("RECD_KG", EITLERPGLOBAL.round(rsTmp.getFloat("RECD_KG"), 2));
                ObjMRItems.setAttribute("RATE", EITLERPGLOBAL.round(rsTmp.getDouble("RATE"), 2));
                ObjMRItems.setAttribute("BAS_AMT", EITLERPGLOBAL.round(rsTmp.getFloat("BAS_AMT"), 2));
                ObjMRItems.setAttribute("CHEM_TRT_CHG", EITLERPGLOBAL.round(rsTmp.getFloat("CHEM_TRT_CHG"), 2));
                ObjMRItems.setAttribute("PIN_CHG", EITLERPGLOBAL.round(rsTmp.getFloat("PIN_CHG"), 2));
                ObjMRItems.setAttribute("SPIRAL_CHG", EITLERPGLOBAL.round(rsTmp.getFloat("SPIRAL_CHG"), 2));
                ObjMRItems.setAttribute("MEMO_DATE", rsTmp.getString("MEMO_DATE"));
                ObjMRItems.setAttribute("DISC_PER", rsTmp.getDouble("DISC_PER"));
                ObjMRItems.setAttribute("DISAMT", EITLERPGLOBAL.round(rsTmp.getFloat("DISAMT"), 2));
                ObjMRItems.setAttribute("DISBASAMT", EITLERPGLOBAL.round(rsTmp.getFloat("DISBASAMT"), 2));
                ObjMRItems.setAttribute("EXCISE", EITLERPGLOBAL.round(rsTmp.getFloat("EXCISE"), 2));
                ObjMRItems.setAttribute("SEAM_CHG", EITLERPGLOBAL.round(rsTmp.getFloat("SEAM_CHG"), 2)); //SEAM CHG
                ObjMRItems.setAttribute("SEAM_CHG_PER", EITLERPGLOBAL.round(rsTmp.getFloat("SEAM_CHG_PER"), 2));
                ObjMRItems.setAttribute("INSACC_AMT", EITLERPGLOBAL.round(rsTmp.getFloat("INSACC_AMT"), 2));
                ObjMRItems.setAttribute("INV_AMT", EITLERPGLOBAL.round(rsTmp.getFloat("INV_AMT"), 2));
                ObjMRItems.setAttribute("DAYS", rsTmp.getInt("DAYS"));
                ObjMRItems.setAttribute("MACHINE_NO", rsTmp.getString("MACHINE_NO"));
                ObjMRItems.setAttribute("POSITION", rsTmp.getString("POSITION"));
                ObjMRItems.setAttribute("STATION", rsTmp.getString("STATION"));
                ObjMRItems.setAttribute("INSURANCE_CODE", rsTmp.getString("INSURANCE_CODE"));
                ObjMRItems.setAttribute("INS_IND", rsTmp.getString("INS_IND"));
                ObjMRItems.setAttribute("ITEM_DESC", rsTmp.getString("ITEM_DESC"));
                ObjMRItems.setAttribute("SYN_PER", rsTmp.getString("SYN_PER"));
                ObjMRItems.setAttribute("ORDER_DATE", rsTmp.getString("ORDER_DATE"));
                ObjMRItems.setAttribute("PO_NO", rsTmp.getString("PO_NO"));
                ObjMRItems.setAttribute("PO_DATE", rsTmp.getString("PO_DATE"));
                ObjMRItems.setAttribute("VAT", EITLERPGLOBAL.round(rsTmp.getFloat("VAT"), 2));
                ObjMRItems.setAttribute("CST", EITLERPGLOBAL.round(rsTmp.getFloat("CST"), 2));
                ObjMRItems.setAttribute("SD_AMT", EITLERPGLOBAL.round(rsTmp.getFloat("SD_AMT"), 2));
                ObjMRItems.setAttribute("FIVE_PER_WEIGHT", EITLERPGLOBAL.round(rsTmp.getFloat("FIVE_PER_WEIGHT"), 2));
                ObjMRItems.setAttribute("PRO_INV_AMT", EITLERPGLOBAL.round(rsTmp.getFloat("PRO_INV_AMT"), 2));
                ObjMRItems.setAttribute("INV_VAL", EITLERPGLOBAL.round(rsTmp.getFloat("INV_VAL"), 2));
                ObjMRItems.setAttribute("INV_VAL_WORD", rsTmp.getString("INV_VAL_WORD"));
                ObjMRItems.setAttribute("CGST_AMT", EITLERPGLOBAL.round(rsTmp.getFloat("CGST_AMT"), 2));
                ObjMRItems.setAttribute("SGST_AMT", EITLERPGLOBAL.round(rsTmp.getFloat("SGST_AMT"), 2));
                ObjMRItems.setAttribute("IGST_AMT", EITLERPGLOBAL.round(rsTmp.getFloat("IGST_AMT"), 2));
                ObjMRItems.setAttribute("TCS_PER", EITLERPGLOBAL.round(rsTmp.getFloat("TCS_PER"), 3));
                ObjMRItems.setAttribute("TCS_AMT", EITLERPGLOBAL.round(rsTmp.getFloat("TCS_AMT"), 2));
                ObjMRItems.setAttribute("UOM", rsTmp.getString("UOM"));
                ObjMRItems.setAttribute("CREATED_BY", rsTmp.getLong("CREATED_BY"));
                ObjMRItems.setAttribute("CREATED_DATE", rsTmp.getString("CREATED_DATE"));
                ObjMRItems.setAttribute("MODIFIED_BY", rsTmp.getLong("MODIFIED_BY"));
                ObjMRItems.setAttribute("MODIFIED_DATE", rsTmp.getString("MODIFIED_DATE"));
                ObjMRItems.setAttribute("AOSD_PER", EITLERPGLOBAL.round(rsTmp.getFloat("AOSD_PER"), 2));
                ObjMRItems.setAttribute("AOSD_AMT", EITLERPGLOBAL.round(rsTmp.getFloat("AOSD_AMT"), 2));
                ObjMRItems.setAttribute("SURCHARGE_PER", EITLERPGLOBAL.round(rsTmp.getDouble("SURCHARGE_PER"), 2));
                ObjMRItems.setAttribute("SURCHARGE_RATE", EITLERPGLOBAL.round(rsTmp.getDouble("SURCHARGE_RATE"), 2));
                ObjMRItems.setAttribute("GROSS_RATE", EITLERPGLOBAL.round(rsTmp.getDouble("GROSS_RATE"), 2));
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER WHERE PROFORMA_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=708 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND STATUS='W'";
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

    public static HashMap getHistoryList(int CompanyID, String ProformaNo) {
        HashMap List = new HashMap();
        ResultSet rsTmp;

        try {
            String strSQL = "SELECT * FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER_H WHERE PROFORMA_NO='" + ProformaNo + "'";
            rsTmp = data.getResult(strSQL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                while (!rsTmp.isAfterLast()) {
                    clsProforma objParty = new clsProforma();

                    objParty.setAttribute("PROFORMA_NO", rsTmp.getString("PROFORMA_NO"));
                    objParty.setAttribute("PROFORMA_DATE", rsTmp.getString("PROFORMA_DATE"));
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
            strSQL += "SELECT * FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER WHERE " + Condition;
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery(strSQL);

            if (!rsResultSet.first()) {
                strSQL = "SELECT * FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER WHERE PROFORMA_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND PROFORMA_DATE<='" + EITLERPGLOBAL.FinToDateDB + "' ORDER BY PROFORMA_NO";
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER WHERE PROFORMA_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=708 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND TYPE='C' AND STATUS='W'";
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

    public Object getObject(int CompanyID, String PartyCode) {
        String strCondition = " WHERE PROFORMA_NO='" + PartyCode + "' ";
        clsSales_Party objParty = new clsSales_Party();
        objParty.Filter(strCondition);
        return objParty;
    }

    public static void CancelParty(int pCompanyID, String pPartyCode, String MainCode) {
        ResultSet rsTmp = null;

        if (CanCancelParty(pCompanyID, pPartyCode, MainCode)) {
            boolean Approved = false;
            try {
                rsTmp = data.getResult("SELECT APPROVED FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID=" + pCompanyID + " AND PARTY_CODE='" + pPartyCode + "' AND MAIN_ACCOUNT_CODE='" + MainCode + "' ");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    Approved = rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();
                data.Execute("UPDATE D_SAL_PARTY_MASTER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + pCompanyID + " AND PARTY_CODE='" + pPartyCode + "' AND MAIN_ACCOUNT_CODE='" + MainCode + "' ");
                data.Execute("DELETE FROM FELT_DOC_DATA WHERE COMPANY_ID=" + pCompanyID + " AND DOC_NO='" + pPartyCode + "' ");
            } catch (Exception e) {
            }
        }
    }

    public static boolean CanCancelParty(int pCompanyID, String pPartyCode, String MainCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean CanCancel = false;

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            rsTmp = stTmp.executeQuery("SELECT PARTY_CODE FROM D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + pPartyCode + "' AND MAIN_ACCOUNT_CODE='" + MainCode + "' AND COMPANY_ID=" + pCompanyID + " AND APPROVED=0 AND CANCELLED=0");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                CanCancel = true;
            }
            stTmp.close();
            rsTmp.close();
        } catch (Exception e) {
        }
        return CanCancel;
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
                strSQL = "SELECT PRODUCTION.FELT_PROFORMA_INVOICE_HEADER.PROFORMA_NO,PRODUCTION.FELT_PROFORMA_INVOICE_HEADER.PROFORMA_DATE,RECEIVED_DATE,PARTY_CD FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_PROFORMA_INVOICE_HEADER.PROFORMA_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=708 ORDER BY PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT PRODUCTION.FELT_PROFORMA_INVOICE_HEADER.PROFORMA_NO,PRODUCTION.FELT_PROFORMA_INVOICE_HEADER.PROFORMA_DATE,RECEIVED_DATE,PARTY_CD FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_PROFORMA_INVOICE_HEADER.PROFORMA_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=708 ORDER BY PRODUCTION.FELT_PROFORMA_INVOICE_HEADER.PROFORMA_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT PRODUCTION.FELT_PROFORMA_INVOICE_HEADER.PROFORMA_NO,PRODUCTION.FELT_PROFORMA_INVOICE_HEADER.PROFORMA_DATE,RECEIVED_DATE,PARTY_CD FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_PROFORMA_INVOICE_HEADER.PROFORMA_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=708 ORDER BY PRODUCTION.FELT_PROFORMA_INVOICE_HEADER.PROFORMA_NO";
            }

            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {

                Counter = Counter + 1;
                clsProforma ObjItem = new clsProforma();

                //------------- Header Fields --------------------//
                ObjItem.setAttribute("DOC_NO", rsTmp.getString("PROFORMA_NO"));
                ObjItem.setAttribute("DOC_DATE", rsTmp.getString("PROFORMA_DATE"));
                ObjItem.setAttribute("RECEIVED_DATE", rsTmp.getString("RECEIVED_DATE"));
                ObjItem.setAttribute("PARTY_CODE", rsTmp.getInt("PARTY_CD"));
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

    public boolean ShowHistory(int pCompanyID, String pProformaNo) {
        Ready = false;
        try {
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsResultSet = Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER_H WHERE PROFORMA_NO='" + pProformaNo + "' ORDER BY REVISION_NO");
            Ready = true;
            HistoryView = true;
            MoveFirst();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public static boolean IsPartyExistEx(int pCompanyID, String pPartyCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT PARTY_NAME FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID=" + pCompanyID + " AND PARTY_CODE='" + pPartyCode + "'");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                stTmp.close();
                rsTmp.close();

                return true;
            }

            stTmp.close();
            rsTmp.close();

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean IsPartyExist(int pCompanyID, String pPartyCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT PARTY_NAME FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID=" + pCompanyID + " AND PARTY_CODE='" + pPartyCode + "' AND BLOCKED='N' ");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                stTmp.close();
                rsTmp.close();

                return true;
            }
            stTmp.close();
            rsTmp.close();

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getPartyName(int pCompanyID, String pPartyCode, String pURL) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName = "";

        try {
            tmpConn = data.getConn(pURL);
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT PARTY_NAME FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID=" + pCompanyID + " AND PARTY_CODE='" + pPartyCode + "' ");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                PartyName = rsTmp.getString("PARTY_NAME");
            }

            stTmp.close();
            rsTmp.close();

            return PartyName;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getItemName(String pPartyCode, String pPieceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            String strSQL = "SELECT ITEM_DESC FROM PRODUCTION.FELT_RATE_MASTER WHERE SUBSTRING(ITEM_CODE,1,6)= ";
            strSQL += "(SELECT PRODUCT_CODE FROM PRODUCTION.FELT_ORDER_MASTER  WHERE PARTY_CD='" + pPartyCode + "' AND PIECE_NO='" + pPieceNo + "' UNION ALL SELECT SUBSTRING(PRODUCT_CD,1,6) FROM PRODUCTION.FELT_PIECE_REGISTER WHERE PIECE_NO='" + pPieceNo + "' AND PARTY_CODE='" + pPartyCode + "')";
            rsTmp = stTmp.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                PartyName = rsTmp.getString("ITEM_DESC");
            }
            stTmp.close();
            rsTmp.close();

            return PartyName;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getItemPosition(String pPartyCode, String pPieceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Position = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT POSITION FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO=" + pPieceNo + " AND PARTY_CD='" + pPartyCode + "'");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                Position = rsTmp.getString("POSITION");

            }
            stTmp.close();
            rsTmp.close();

            return Position;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getItemLength(String pPartyCode, String pPieceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Length = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT LNGTH FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO=" + pPieceNo + " AND PARTY_CD='" + pPartyCode + "'");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                Length = rsTmp.getString("LNGTH");
            }
            stTmp.close();
            rsTmp.close();
            return Length;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getPinInd(String pItemCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Pinind = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT PIN_IND FROM PRODUCTION.FELT_RATE_MASTER WHERE ITEM_CODE=" + pItemCode + "");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                Pinind = rsTmp.getString("PIN_IND");
            }
            stTmp.close();
            rsTmp.close();
            return Pinind;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getSPRInd(String pItemCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Sprind = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT SPR_IND FROM PRODUCTION.FELT_RATE_MASTER WHERE ITEM_CODE=" + pItemCode + "");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                Sprind = rsTmp.getString("SPR_IND");
            }
            stTmp.close();
            rsTmp.close();
            return Sprind;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getChemTrtIn(String pItemCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Chemtrtin = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT CHEM_TRT_IN FROM PRODUCTION.FELT_RATE_MASTER WHERE ITEM_CODE=" + pItemCode + "");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                Chemtrtin = rsTmp.getString("CHEM_TRT_IN");
            }
            stTmp.close();
            rsTmp.close();
            return Chemtrtin;
        } catch (Exception e) {
            return "";
        }
    }

    public static float getCharges(String pItemCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        float Charges = 0;

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT CHARGES FROM PRODUCTION.FELT_RATE_MASTER WHERE ITEM_CODE=" + pItemCode + "");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                Charges = Float.parseFloat(rsTmp.getString("CHARGES"));
            }
            stTmp.close();
            rsTmp.close();
            return Charges;
        } catch (Exception e) {
            return 0;
        }
    }

    public static String getSqmind(String pItemCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Sqmind = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT SQM_IND FROM PRODUCTION.FELT_RATE_MASTER WHERE ITEM_CODE=" + pItemCode + "");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                Sqmind = rsTmp.getString("SQM_IND");
            }
            stTmp.close();
            rsTmp.close();
            return Sqmind;
        } catch (Exception e) {
            return "";
        }
    }

    public static float getSQMRate(String pItemCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        float sqmrate = 0;

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT SQM_RATE FROM PRODUCTION.FELT_RATE_MASTER WHERE ITEM_CODE=" + pItemCode + "");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                sqmrate = Float.parseFloat(rsTmp.getString("SQM_RATE"));
            }
            stTmp.close();
            rsTmp.close();
            return sqmrate;
        } catch (Exception e) {
            return 0;
        }
    }

    public static float getWTRate(String pItemCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        float wtrate = 0;

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT WT_RATE FROM PRODUCTION.FELT_RATE_MASTER WHERE ITEM_CODE=" + pItemCode + "");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                wtrate = Float.parseFloat(rsTmp.getString("WT_RATE"));
            }
            stTmp.close();
            rsTmp.close();
            return wtrate;
        } catch (Exception e) {
            return 0;
        }
    }

    public static double getItemWidth(String pPartyCode, String pPieceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        double Width = 0.00;

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT WIDTH FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO=" + pPieceNo + " AND PARTY_CD='" + pPartyCode + "'");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                Width = rsTmp.getDouble("WIDTH");
            }
            stTmp.close();
            rsTmp.close();

            return Width;
        } catch (Exception e) {
            return 0;
        }
    }

    public static String getItemStyle(String pPartyCode, String pPieceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT BALNK FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO=" + pPieceNo + " AND PARTY_CD='" + pPartyCode + "'");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                Style = rsTmp.getString("BALNK");
            }
            stTmp.close();
            rsTmp.close();

            return Style;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getNewDisamt(String pDiscper, String pBasamt) {

        double Discper = Double.parseDouble(pDiscper);
        double Basamt = Double.parseDouble(pBasamt);

        double newDisamt = 0.00;
        newDisamt = (Basamt) * (Discper / 100);
        newDisamt = EITLERPGLOBAL.round(newDisamt, 2);
        return Double.toString(newDisamt);
    }

    public static String getWeight(String pLength, String pWidth, String pGsq, String pProduct, String pPieceNo, String pPartyCode) {
        double Length = Double.parseDouble(pLength);
        double Width = Double.parseDouble(pWidth);
        double Gsq = Double.parseDouble(pGsq);
        double Product = Double.parseDouble(pProduct);
        double newWeight = 0.00;
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT RECD_KG FROM PRODUCTION.FELT_PIECE_REGISTER WHERE ORDER_CD=" + pPieceNo + " AND PARTY_CODE='" + pPartyCode + "'");
            rsTmp.first();

            if (Product == 7190110 || Product == 7190210 || Product == 7190310 || Product == 7190410 || Product == 7190510 || Product == 7290000) {
                newWeight = ((Length * Width));
            } else {
                if (rsTmp.getRow() > 0) {
                    newWeight = rsTmp.getFloat("RECD_KG");
                } else {
                    newWeight = 0;
                }
            }

            stTmp.close();
            rsTmp.close();
        } catch (Exception e) {
            return "";
        }

        newWeight = EITLERPGLOBAL.round(newWeight, 2);
        return Double.toString(newWeight);
    }

    public static String getNewWeight(String pLength, String pWidth, String pGsq, String pProduct) {
        double Length = Double.parseDouble(pLength);
        double Width = Double.parseDouble(pWidth);
        double Gsq = Double.parseDouble(pGsq);
        double Product = Double.parseDouble(pProduct);
        double newWeight = 0.00;

        if (Product == 7190110 || Product == 7190210 || Product == 7190310 || Product == 7190410 || Product == 7190510 || Product == 7290000) {
            newWeight = ((Length * Width));
        } else {
            newWeight = (Length * Width * (Gsq / 1000));
        }

        newWeight = EITLERPGLOBAL.round(newWeight, 2);
        return Double.toString(newWeight);
    }

    public static String getBasamt(String pSqmInd, String pLength, String pWidth, float pSQMrate, String pWeight, float pWTrate) {

        double Length = Double.parseDouble(pLength);
        double Width = Double.parseDouble(pWidth);
        double Weight = Double.parseDouble(pWeight);
        double SQMrate = pSQMrate;
        double WTrate = pWTrate;

        double newBasamt = 0.00;
        if (pSqmInd.equals("1")) {
            newBasamt = (Length) * (Width) * (SQMrate);
            newBasamt = EITLERPGLOBAL.round(newBasamt, 2);
        } else {
            newBasamt = 0.00;
            newBasamt = EITLERPGLOBAL.round(newBasamt, 2);
        }
        return Double.toString(newBasamt);

    }

    public static String getNewBasamt(String pSqmInd, String pLength, String pWidth, float pSQMrate, String pWeight, float pWTrate) {

        double Length = Double.parseDouble(pLength);
        double Width = Double.parseDouble(pWidth);
        double Weight = Double.parseDouble(pWeight);
        double SQMrate = pSQMrate;
        double WTrate = pWTrate;

        double newBasamt = 0.00;
        if (pSqmInd.equals("1")) {
            newBasamt = (Length) * (Width) * (SQMrate);
            newBasamt = EITLERPGLOBAL.round(newBasamt, 2);
        } else {
            newBasamt = (Weight) * (WTrate);
            newBasamt = EITLERPGLOBAL.round(newBasamt, 2);
        }
        return Double.toString(newBasamt);

    }

    public static String getNewWPSC(String pChemTrtIn, String pPinInd, String pSprInd, String pWeight, float pCharges, String pWidth, String pChargesDisPer) {

        double Weight = Double.parseDouble(pWeight);
        double Width = Double.parseDouble(pWidth);
        double ChargesDisPer = Double.parseDouble(pChargesDisPer);
        double Charges = pCharges;
        Charges = Charges * ((100 - ChargesDisPer) / 100);

        double newWPSC1 = 0.00;
        double newWPSC2 = 0.00;
        double newWPSC3 = 0.00;
        double newWPSC = 0.00;
        if (pChemTrtIn.equals("1")) {
            newWPSC1 = (Weight) * (Charges);
        } else {
            newWPSC1 = 0;
        }
        if (pPinInd.equals("1")) {
            newWPSC2 = (Width) * (Charges);
        } else {
            newWPSC2 = 0;
        }
        if (pSprInd.equals("1")) {
            newWPSC3 = (Width) * (Charges);
        } else {
            newWPSC3 = 0;
        }

        newWPSC = newWPSC1 + newWPSC2 + newWPSC3;
        newWPSC = EITLERPGLOBAL.round(newWPSC, 2);
        return Double.toString(newWPSC);

    }

    public static String getNewDisBasamt(String pDiscper, String pBasamt) {
        double Discper = Double.parseDouble(pDiscper);
        double Basamt = Double.parseDouble(pBasamt);

        double newDisBasamt = 0.00;
        newDisBasamt = Basamt - ((Basamt) * (Discper / 100));
        newDisBasamt = EITLERPGLOBAL.round(newDisBasamt, 2);
        return Double.toString(newDisBasamt);

    }

    public static String getNewExcise(String pDisbasamt, String pWpsc) {
        double Disbasamt = Double.parseDouble(pDisbasamt);
        double Wpsc = Double.parseDouble(pWpsc);

        double newExcise = 0.00;
        newExcise = ((Disbasamt + Wpsc) * .125);
        DecimalFormat df = new DecimalFormat("##.##");
        return df.format(newExcise);

    }

    public static String getNewInsaccamt(String pInsInd, String pNewDisbasamt, String pNewExcise, String pWPSC) {
        double InsInd = Double.parseDouble(pInsInd);
        double WPSC = Double.parseDouble(pWPSC);
        double NewDisbasamt = Double.parseDouble(pNewDisbasamt);
        double NewExcise = Double.parseDouble(pNewExcise);

        double newInsaccamt = 0.00;
        if (InsInd == 1) {
            newInsaccamt = (ConvertToNextThousand((Math.round(NewDisbasamt + NewExcise + WPSC) + (Math.round(NewDisbasamt + NewExcise + WPSC) * .10))) * 0.0039) + 0.05;
            return Double.toString(newInsaccamt);
        } else {
            return Double.toString(0);
        }
    }

    public static String getNewInvamt(String pInsInd, String pNewDisbasamt, String pNewExcise, String pWPSC, String pNewInsaccamt) {
        double InsInd = Double.parseDouble(pInsInd);
        double WPSC = Double.parseDouble(pWPSC);
        double NewDisbasamt = Double.parseDouble(pNewDisbasamt);
        double NewExcise = Double.parseDouble(pNewExcise);
        double NewInsaccamt = Double.parseDouble(pNewInsaccamt);

        double newInvamt = 0.00;
        if (InsInd == 1) {
            newInvamt = NewDisbasamt + NewExcise + WPSC + NewInsaccamt;
            DecimalFormat df1 = new DecimalFormat("##");
            return df1.format(newInvamt);
        } else {
            newInvamt = NewDisbasamt + NewExcise + WPSC;
            DecimalFormat df2 = new DecimalFormat("##");
            return df2.format(newInvamt);
        }
    }

    public static double ConvertToNextThousand(double pamount) {
        double returnamt = 0;
        if (pamount <= 1000) {
            return pamount + (1000 - pamount);
        } else {
            if (pamount % 1000 == 0) {
                return pamount;
            } else {
                return pamount + (1000 - (pamount % 1000));
            }
        }

    }

    public static int getItemGsq(String pPartyCode, String pPieceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        int Gsq = 0;

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT GSQ FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO=" + pPieceNo + " AND PARTY_CD='" + pPartyCode + "'");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                Gsq = rsTmp.getInt("GSQ");
            }
            stTmp.close();
            rsTmp.close();

            return Gsq;
        } catch (Exception e) {
            return 0;
        }
    }

    public static String getPartyName(int pCompanyID, String pPartyCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT PARTY_NAME FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID=" + pCompanyID + " AND PARTY_CODE='" + pPartyCode + "' ");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                PartyName = rsTmp.getString("PARTY_NAME");
            }

            stTmp.close();
            rsTmp.close();

            return PartyName;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getpiecedec(String ppiecedec) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String positiondesc = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT B.PRODUCT_DESC AS PRODUCT_DESC FROM PRODUCTION.FELT_SALES_PIECE_REGISTER A,PRODUCTION.FELT_QLT_RATE_MASTER B WHERE A.PR_PRODUCT_CODE= B.PRODUCT_CODE AND A.PR_PIECE_NO='" + ppiecedec + "' ");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                positiondesc = rsTmp.getString("PRODUCT_DESC");
            }

            stTmp.close();
            rsTmp.close();

            return positiondesc;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getproductcode(String pproductcode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String positiondesc = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT PR_BILL_PRODUCT_CODE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + pproductcode + "' ");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                positiondesc = rsTmp.getString("PR_BILL_PRODUCT_CODE");
            }

            stTmp.close();
            rsTmp.close();

            return positiondesc;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getposition(String pposition) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String positiondesc = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT B.MM_MACHINE_POSITION_DESC FROM PRODUCTION.FELT_SALES_PIECE_REGISTER A, PRODUCTION.FELT_MACHINE_MASTER_DETAIL B WHERE A.PR_POSITION_NO=B.MM_MACHINE_POSITION AND A.PR_MACHINE_NO=B.MM_MACHINE_NO AND PR_PIECE_NO='" + pposition + "' ");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                positiondesc = rsTmp.getString("MM_MACHINE_POSITION_DESC");
            }

            stTmp.close();
            rsTmp.close();

            return positiondesc;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getlength(String plength) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String positiondesc = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT PR_BILL_LENGTH FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + plength + "' ");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                positiondesc = rsTmp.getString("PR_BILL_LENGTH");
            }

            stTmp.close();
            rsTmp.close();

            return positiondesc;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getwidth(String pwidth) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String positiondesc = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT PR_BILL_WIDTH FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + pwidth + "' ");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                positiondesc = rsTmp.getString("PR_BILL_WIDTH");
            }

            stTmp.close();
            rsTmp.close();

            return positiondesc;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getgsm(String pgsm) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String positiondesc = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT PR_BILL_GSM FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + pgsm + "' ");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                positiondesc = rsTmp.getString("PR_BILL_GSM");
            }

            stTmp.close();
            rsTmp.close();

            return positiondesc;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getstyle(String pstyle) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String positiondesc = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT PR_STYLE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + pstyle + "' ");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                positiondesc = rsTmp.getString("PR_STYLE");
            }

            stTmp.close();
            rsTmp.close();

            return positiondesc;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getweight(String pweight) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String positiondesc = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT PR_BILL_WEIGHT FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + pweight + "' ");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                positiondesc = rsTmp.getString("PR_BILL_WEIGHT");
            }

            stTmp.close();
            rsTmp.close();

            return positiondesc;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getsqmtr(String psqmtr) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String positiondesc = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT PR_BILL_SQMTR FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + psqmtr + "' ");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                positiondesc = rsTmp.getString("PR_BILL_SQMTR");
            }

            stTmp.close();
            rsTmp.close();

            return positiondesc;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getincharge(String pincharge) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String positiondesc = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT PR_INCHARGE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + pincharge + "' ");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                positiondesc = rsTmp.getString("PR_INCHARGE");
            }

            stTmp.close();
            rsTmp.close();

            return positiondesc;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getorderdate(String porderdate) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String positiondesc = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT PR_ORDER_DATE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + porderdate + "' ");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                positiondesc = rsTmp.getString("PR_ORDER_DATE");
            }

            stTmp.close();
            rsTmp.close();

            return positiondesc;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getmachienno(String pmachienno) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String positiondesc = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT PR_MACHINE_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + pmachienno + "' ");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                positiondesc = rsTmp.getString("PR_MACHINE_NO");
            }

            stTmp.close();
            rsTmp.close();

            return positiondesc;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getpono(String ppono) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String positiondesc = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT PR_PO_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + ppono + "' ");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                positiondesc = rsTmp.getString("PR_PO_NO");
            }

            stTmp.close();
            rsTmp.close();

            return positiondesc;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getpodate(String ppodate) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String positiondesc = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT PR_PO_DATE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + ppodate + "' ");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                positiondesc = rsTmp.getString("PR_PO_DATE");
            }

            stTmp.close();
            rsTmp.close();

            return positiondesc;
        } catch (Exception e) {
            return "";
        }
    }

    public static HashMap getList(String pCondition) {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;

        HashMap List = new HashMap();
        long Counter = 0;
        long Counter2 = 0;

        tmpConn = data.getConn();

        try {
            tmpStmt = tmpConn.createStatement();

            rsTmp = tmpStmt.executeQuery("SELECT * FROM D_SAL_PARTY_MASTER " + pCondition);

            Counter = 0;
            while (rsTmp.next()) {
                Counter = Counter + 1;
                clsSales_Party ObjParty = new clsSales_Party();
                ObjParty.setAttribute("COMPANY_ID", rsTmp.getInt("COMPANY_ID"));
                ObjParty.setAttribute("PARTY_CODE", rsTmp.getString("PARTY_CODE"));
                ObjParty.setAttribute("PARTY_TYPE", rsTmp.getInt("PARTY_TYPE"));
                ObjParty.setAttribute("PARTY_NAME", rsTmp.getString("PARTY_NAME"));

                ObjParty.setAttribute("SEASON_CODE", rsTmp.getString("SEASON_CODE"));
                ObjParty.setAttribute("REG_DATE", rsTmp.getString("REG_DATE"));

                ObjParty.setAttribute("AREA_ID", rsTmp.getString("AREA_ID"));
                ObjParty.setAttribute("ADDRESS1", rsTmp.getString("ADDRESS1"));
                ObjParty.setAttribute("ADDRESS2", rsTmp.getString("ADDRESS2"));
                ObjParty.setAttribute("CITY_ID", rsTmp.getString("CITY_ID"));
                ObjParty.setAttribute("CITY_NAME", rsTmp.getString("CITY_NAME"));
                ObjParty.setAttribute("DISPATCH_STATION", rsTmp.getString("DISPATCH_STATION"));
                ObjParty.setAttribute("DISTRICT", rsTmp.getString("DISTRICT"));
                ObjParty.setAttribute("PINCODE", rsTmp.getString("PINCODE"));
                ObjParty.setAttribute("PHONE_NO", rsTmp.getString("PHONE_NO"));

                ObjParty.setAttribute("REMARK1", rsTmp.getString("REMARK1"));
                ObjParty.setAttribute("REMARK2", rsTmp.getString("REMARK2"));
                ObjParty.setAttribute("REMARK3", rsTmp.getString("REMARK3"));
                ObjParty.setAttribute("REMARK4", rsTmp.getString("REMARK4"));
                ObjParty.setAttribute("REMARK5", rsTmp.getString("REMARK5"));

                ObjParty.setAttribute("MOBILE_NO", rsTmp.getString("MOBILE_NO"));
                ObjParty.setAttribute("EMAIL", rsTmp.getString("EMAIL"));
                ObjParty.setAttribute("URL", rsTmp.getString("URL"));
                ObjParty.setAttribute("CONTACT_PERSON", rsTmp.getString("CONTACT_PERSON"));
                ObjParty.setAttribute("BANK_ID", rsTmp.getLong("BANK_ID"));
                ObjParty.setAttribute("BANK_NAME", rsTmp.getString("BANK_NAME"));
                ObjParty.setAttribute("BANK_ADDRESS", rsTmp.getString("BANK_ADDRESS"));
                ObjParty.setAttribute("BANK_CITY", rsTmp.getString("BANK_CITY"));
                ObjParty.setAttribute("CST_NO", rsTmp.getString("CST_NO"));
                ObjParty.setAttribute("CST_DATE", rsTmp.getString("CST_DATE"));
                ObjParty.setAttribute("ECC_NO", rsTmp.getString("ECC_NO"));
                ObjParty.setAttribute("ECC_DATE", rsTmp.getString("ECC_DATE"));
                ObjParty.setAttribute("TIN_NO", rsTmp.getString("TIN_NO"));
                ObjParty.setAttribute("TIN_DATE", rsTmp.getString("TIN_DATE"));
                ObjParty.setAttribute("PAN_NO", rsTmp.getString("PAN_NO"));
                ObjParty.setAttribute("PAN_DATE", rsTmp.getString("PAN_DATE"));
                ObjParty.setAttribute("MAIN_ACCOUNT_CODE", rsTmp.getString("MAIN_ACCOUNT_CODE"));
                ObjParty.setAttribute("CHARGE_CODE", rsTmp.getString("CHARGE_CODE"));
                ObjParty.setAttribute("CHARGE_CODE_II", rsTmp.getString("CHARGE_CODE_II"));
                ObjParty.setAttribute("CREDIT_DAYS", rsTmp.getDouble("CREDIT_DAYS"));
                ObjParty.setAttribute("DOCUMENT_THROUGH", rsTmp.getString("DOCUMENT_THROUGH"));
                ObjParty.setAttribute("TRANSPORTER_ID", rsTmp.getLong("TRANSPORTER_ID"));
                ObjParty.setAttribute("TRANSPORTER_NAME", rsTmp.getString("TRANSPORTER_NAME"));
                ObjParty.setAttribute("AMOUNT_LIMIT", rsTmp.getDouble("AMOUNT_LIMIT"));
                ObjParty.setAttribute("OTHER_BANK_ID", rsTmp.getLong("OTHER_BANK_ID"));
                ObjParty.setAttribute("OTHER_BANK_NAME", rsTmp.getString("OTHER_BANK_NAME"));
                ObjParty.setAttribute("OTHER_BANK_ADDRESS", rsTmp.getString("OTHER_BANK_ADDRESS"));
                ObjParty.setAttribute("OTHER_BANK_CITY", rsTmp.getString("OTHER_BANK_CITY"));
                ObjParty.setAttribute("CATEGORY", rsTmp.getString("CATEGORY"));
                ObjParty.setAttribute("INSURANCE_CODE", rsTmp.getString("INSURANCE_CODE"));
                ObjParty.setAttribute("REMARKS", rsTmp.getString("REMARKS"));
                ObjParty.setAttribute("DELAY_INTEREST_PER", rsTmp.getDouble("DELAY_INTEREST_PER"));
                ObjParty.setAttribute("ACCOUNT_CODES", rsTmp.getString("ACCOUNT_CODES"));

                ObjParty.setAttribute("CREATED_BY", rsTmp.getInt("CREATED_BY"));
                ObjParty.setAttribute("CREATED_DATE", rsTmp.getString("CREATED_DATE"));
                ObjParty.setAttribute("MODIFIED_BY", rsTmp.getInt("MODIFIED_BY"));
                ObjParty.setAttribute("MODIFIED_DATE", rsTmp.getString("MODIFIED_DATE"));

                ObjParty.setAttribute("HIERARCHY_ID", rsTmp.getLong("HIERARCHY_ID"));

            }

            rsTmp.close();
            tmpStmt.close();

        } catch (Exception e) {
        }

        return List;
    }

    public static Object getObjectEx(int pCompanyID, String pPartyCode) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND PARTY_CODE='" + pPartyCode + "' ";
        clsSales_Party ObjParty = new clsSales_Party();
        ObjParty.LoadData(pCompanyID);
        ObjParty.Filter(strCondition);
        return ObjParty;
    }

    public static Object getObjectExN(int pCompanyID, String pPartyCode, String MainCode) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND PARTY_CODE='" + pPartyCode + "' AND MAIN_ACCOUNT_CODE=" + MainCode;
        clsSales_Party ObjParty = new clsSales_Party();
        ObjParty.LoadData(pCompanyID);
        ObjParty.Filter(strCondition);
        return ObjParty;
    }

    public static boolean deleteParty(String PartyCode) {
        try {
            data.Execute("DELETE FROM PRODUCTION.FELT_DOC_DATA WHERE DOC_NO='" + PartyCode + "' AND MODULE_ID='" + clsOrderParty.ModuleID + "' ");
            data.Execute("DELETE FROM PRODUCTION.FELT_DOC_DATA WHERE PARTY_CODE='" + PartyCode + "' ");
            data.Execute("DELETE FROM PRODUCTION.FELT_DOC_DATA WHERE PARTY_CODE='" + PartyCode + "' ");
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String getSeasonCode() {
        String SeasonCode = "";
        try {
            SeasonCode = data.getStringValueFromDB("SELECT SEASON_ID FROM D_SAL_SEASON_MASTER WHERE CURDATE() BETWEEN DATE_FROM AND DATE_TO");
        } catch (Exception e) {
            return SeasonCode;
        }
        return SeasonCode;
    }
}
