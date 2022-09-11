/*
 * clsFeltCreditNote.java
 *
 * Created on March 12, 2013, 3:10 PM
 */
package EITLERP.Suitings.SuitingGR;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import EITLERP.*;
import EITLERP.EITLERPGLOBAL;
import EITLERP.Variant;
import EITLERP.data;
import EITLERP.ComboData;
import EITLERP.Finance.FinanceGlobal;
import EITLERP.Finance.UtilFunctions;
import EITLERP.Finance.clsTRMapping;
import EITLERP.Finance.clsVoucher;
import EITLERP.Finance.clsVoucherItem;
import java.sql.Date;

/**
 *
 * @author Rishi Raj Neekhra
 * @version
 */
public class clsSuitingGR_OLD {

    public String LastError = "";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet, rsHeader;
    //private ResultSet rsResultSet,

    public HashMap props;
    public boolean Ready = false;
    //Felt Order Collection
    public HashMap hmFeltCreditNoteDetails;
    public HashMap hmFeltCreditNoteDetails1 = new HashMap();

    //History Related properties
    public boolean HistoryView = false;
    private String historyAmendDate = "";
    private String historyAmendID = "";

    public Variant getAttribute(String PropName) {
        return (Variant) props.get(PropName);
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
     * Creates new Data Felt Order Updation
     */
    public clsSuitingGR_OLD() {
        LastError = "";
        props = new HashMap();
        props.put("COMPANY_ID", new Variant(0));
        props.put("GR_DATE", new Variant(""));
        props.put("GR_ID", new Variant(""));
        props.put("GR_DESC_NO", new Variant(""));
        props.put("PARTY_CODE", new Variant(""));
        props.put("PARTY_NAME", new Variant(""));
        props.put("CITY", new Variant(""));
        props.put("INWARD_NO", new Variant(""));
        props.put("INWARD_DATE", new Variant(""));
        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("PREFIX", new Variant(""));

        props.put("DOC_NO", new Variant(""));
        props.put("DOC_DATE", new Variant(""));
        props.put("RECEIVED_DATE", new Variant(""));
        // -- Approval Specific Fields --
        props.put("HIERARCHY_ID", new Variant(0));
        props.put("FROM", new Variant(0));
        props.put("TO", new Variant(0));
        props.put("REJECTED_REMARKS", new Variant(""));
        props.put("REMARKS", new Variant(""));
        props.put("APPROVAL_STATUS", new Variant(""));
        props.put("APPROVER_REMARKS", new Variant(0));
        props.put("ENTRY_DATE", new Variant(0));

        hmFeltCreditNoteDetails = new HashMap();
    }

    public boolean LoadData() {
        Ready = false;
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery("SELECT * FROM STGSALES.D_STG_GOODS_RETURNS_HEADER");
            HistoryView = false;
            Ready = true;
            MoveLast();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    public void Close() {
        try {
            statement.close();
            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Navigation Methods
    public boolean MoveFirst() {
        try {
            resultSet.first();
            if (HistoryView) {
                setHistoryData(historyAmendDate, historyAmendID);
            } else {
                setData();
            }
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    public boolean MoveNext() {
        try {
            if (resultSet.isAfterLast() || resultSet.isLast()) {
                //Move pointer at last record if it is beyond eof
                resultSet.last();
            } else {
                resultSet.next();
                if (resultSet.isAfterLast()) {
                    resultSet.last();
                }
            }
            if (HistoryView) {
                setHistoryData(historyAmendDate, historyAmendID);
            } else {
                setData();
            }
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    public boolean MovePrevious() {
        try {
            if (resultSet.isFirst() || resultSet.isBeforeFirst()) {
                resultSet.first();
            } else {
                resultSet.previous();
                if (resultSet.isBeforeFirst()) {
                    resultSet.first();
                }
            }
            if (HistoryView) {
                setHistoryData(historyAmendDate, historyAmendID);
            } else {
                setData();
            }
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    public boolean MoveLast() {
        try {
            resultSet.last();
            if (HistoryView) {
                setHistoryData(historyAmendDate, historyAmendID);
            } else {
                setData();
            }
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    public boolean Insert() {
        ResultSet resultSetTemp, resultSetHistory, rsHeader, rsDetail;
        Statement statementTemp, statementHistory, stHeader, stDetail;
        try {
            // Felt Order Updation data connection
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  STGSALES.D_STG_GOODS_RETURNS_DETAIL WHERE GR_ID='' order by sr_no*1");

            // Felt order Updation data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM STGSALES.D_STG_GOODS_RETURNS_DETAIL_H WHERE GR_ID=''");

            stHeader = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHeader = stHeader.executeQuery("SELECT * FROM  STGSALES.D_STG_GOODS_RETURNS_HEADER_H WHERE GR_ID=''");

            stDetail = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsDetail = stDetail.executeQuery("SELECT * FROM STGSALES.D_SAL_HSN_INVOICE_POSTING WHERE HSN_NO=''");

            String cnid = getAttribute("GR_ID").getString().substring(0, 2);

            setAttribute("GR_ID", getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 757, 199, true));
            //setAttribute("GR_ID",getNextFreeNo(2,757,true));
            resultSet.first();
            resultSet.moveToInsertRow();
            resultSet.updateString("GR_ID", getAttribute("GR_ID").getString());
            resultSet.updateString("GR_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("GR_DATE").getString()));
            resultSet.updateString("GR_DESC_NO", getAttribute("GR_DESC_NO").getString());
            resultSet.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            resultSet.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
            resultSet.updateString("CITY", getAttribute("CITY").getString());
            resultSet.updateString("INWARD_NO", getAttribute("INWARD_NO").getString());
            resultSet.updateString("INWARD_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("INWARD_DATE").getString()));
            resultSet.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            resultSet.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            resultSet.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            resultSet.updateBoolean("APPROVED", false);
            resultSet.updateString("APPROVED_DATE", "0000-00-00");
            resultSet.updateBoolean("CANCELED", false);
            resultSet.updateBoolean("REJECTED", false);
            resultSet.updateString("REJECTED_DATE", "0000-00-00");
            resultSet.updateBoolean("CHANGED", true);
            resultSet.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

            resultSet.insertRow();

            rsHeader.moveToInsertRow();

            rsHeader.updateInt("REVISION_NO", 1);
            rsHeader.updateInt("UPDATED_BY", (int) getAttribute("UPDATED_BY").getVal());
            rsHeader.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            rsHeader.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeader.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
            rsHeader.updateString("GR_ID", getAttribute("GR_ID").getString());
            rsHeader.updateString("GR_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("GR_DATE").getString()));
            rsHeader.updateString("GR_DESC_NO", getAttribute("GR_DESC_NO").getString());
            rsHeader.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsHeader.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
            rsHeader.updateString("CITY", getAttribute("CITY").getString());
            rsHeader.updateString("INWARD_NO", getAttribute("INWARD_NO").getString());
            rsHeader.updateString("INWARD_DATE", getAttribute("INWARD_DATE").getString());
            rsHeader.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsHeader.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            rsHeader.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeader.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsHeader.updateBoolean("APPROVED", false);
            rsHeader.updateString("APPROVED_DATE", "0000-00-00");
            rsHeader.updateBoolean("CANCELED", false);
            rsHeader.updateBoolean("REJECTED", false);
            rsHeader.updateString("REJECTED_DATE", "0000-00-00");
            rsHeader.updateBoolean("CHANGED", true);
            rsHeader.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

            rsHeader.insertRow();

            //Now Insert records into FELT_CN_TEMP_HEADER & History tables
            for (int i = 1; i <= hmFeltCreditNoteDetails.size(); i++) {
                clsSuitingGRDetails ObjFeltCreditNoteDetails = (clsSuitingGRDetails) hmFeltCreditNoteDetails.get(Integer.toString(i));
                //Insert records into Felt order Amendment table
                resultSetTemp.moveToInsertRow();

                resultSetTemp.updateString("GR_ID", getAttribute("GR_ID").getString());
                resultSetTemp.updateString("GR_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("GR_DATE").getString()));
                resultSetTemp.updateString("SR_NO", ObjFeltCreditNoteDetails.getAttribute("SR_NO").getString());
                resultSetTemp.updateString("GR_PIECE_NO", ObjFeltCreditNoteDetails.getAttribute("GR_PIECE_NO").getString());
                resultSetTemp.updateString("GR_INVOICE_NO", ObjFeltCreditNoteDetails.getAttribute("GR_INVOICE_NO").getString());
                resultSetTemp.updateString("GR_INVOICE_DATE", ObjFeltCreditNoteDetails.getAttribute("GR_INVOICE_DATE").getString());
                resultSetTemp.updateString("GR_ALPHA_INVOICE_NO", ObjFeltCreditNoteDetails.getAttribute("GR_ALPHA_INVOICE_NO").getString());
                resultSetTemp.updateString("GR_QUALITY_NO", ObjFeltCreditNoteDetails.getAttribute("GR_QUALITY_NO").getString());
                resultSetTemp.updateString("GR_SHADE", ObjFeltCreditNoteDetails.getAttribute("GR_SHADE").getString());
                resultSetTemp.updateString("GR_UNIT_CODE", ObjFeltCreditNoteDetails.getAttribute("GR_UNIT_CODE").getString());
                resultSetTemp.updateString("GR_GROSS_QTY", ObjFeltCreditNoteDetails.getAttribute("GR_GROSS_QTY").getString());
                resultSetTemp.updateString("GR_NET_QTY", ObjFeltCreditNoteDetails.getAttribute("GR_NET_QTY").getString());
                resultSetTemp.updateString("GR_RATE", ObjFeltCreditNoteDetails.getAttribute("GR_RATE").getString());
                resultSetTemp.updateString("GR_INVOICE_DISC", ObjFeltCreditNoteDetails.getAttribute("GR_INVOICE_DISC").getString());
                resultSetTemp.updateString("GR_ADD_DISC", ObjFeltCreditNoteDetails.getAttribute("GR_ADD_DISC").getString());
                resultSetTemp.updateString("GR_INVOICE_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_INVOICE_AMOUNT").getString());
                resultSetTemp.updateString("GR_SHOT_LENGTH_PER", ObjFeltCreditNoteDetails.getAttribute("GR_SHOT_LENGTH_PER").getString());
                resultSetTemp.updateString("GR_SHOT_LENGTH_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_SHOT_LENGTH_AMOUNT").getString());
                resultSetTemp.updateString("GR_GROSS_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_GROSS_AMOUNT").getString());
                resultSetTemp.updateString("GR_STOCK_LOT_PER", ObjFeltCreditNoteDetails.getAttribute("GR_STOCK_LOT_PER").getString());
                resultSetTemp.updateString("GR_STOCK_LOT_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_STOCK_LOT_AMOUNT").getString());
                resultSetTemp.updateString("GR_REVERSE_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_REVERSE_AMOUNT").getString());
                resultSetTemp.updateString("GR_NET_GR_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_NET_GR_AMOUNT").getString());
                resultSetTemp.updateString("GR_HSN_NO", ObjFeltCreditNoteDetails.getAttribute("GR_HSN_NO").getString());
                resultSetTemp.updateString("GR_CGST_PER", ObjFeltCreditNoteDetails.getAttribute("GR_CGST_PER").getString());
                resultSetTemp.updateString("GR_CGST_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_CGST_AMOUNT").getString());
                resultSetTemp.updateString("GR_SGST_PER", ObjFeltCreditNoteDetails.getAttribute("GR_SGST_PER").getString());
                resultSetTemp.updateString("GR_SGST_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_SGST_AMOUNT").getString());
                resultSetTemp.updateString("GR_IGST_PER", ObjFeltCreditNoteDetails.getAttribute("GR_IGST_PER").getString());
                resultSetTemp.updateString("GR_IGST_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_IGST_AMOUNT").getString());
                resultSetTemp.updateString("GR_GST_COMPOSITION_PER", ObjFeltCreditNoteDetails.getAttribute("GR_GST_COMPOSITION_PER").getString());
                resultSetTemp.updateString("GR_GST_COMPOSITION_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_GST_COMPOSITION_AMOUNT").getString());
                resultSetTemp.updateString("FLAG", ObjFeltCreditNoteDetails.getAttribute("FLAG").getString());
                resultSetTemp.updateString("GR_QTY", ObjFeltCreditNoteDetails.getAttribute("GR_QTY").getString());
                resultSetTemp.updateString("GR_TOTAL_AMT", ObjFeltCreditNoteDetails.getAttribute("GR_TOTAL_AMT").getString());
                resultSetTemp.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                resultSetTemp.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                resultSetTemp.updateBoolean("APPROVED", false);
                resultSetTemp.updateString("APPROVED_DATE", "0000-00-00");
                resultSetTemp.updateBoolean("REJECTED", false);
                resultSetTemp.updateString("REJECTED_DATE", "0000-00-00");
                resultSetTemp.updateInt("CANCELED", 0);
                resultSetTemp.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                resultSetTemp.updateInt("CHANGED", 1);
                resultSetTemp.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                resultSetTemp.insertRow();

                //Insert records into Felt Order Amendment History Table
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO", 1);
                resultSetHistory.updateString("GR_ID", getAttribute("GR_ID").getString());
                resultSetHistory.updateString("GR_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("GR_DATE").getString()));
                resultSetHistory.updateString("SR_NO", ObjFeltCreditNoteDetails.getAttribute("SR_NO").getString());
                resultSetHistory.updateString("GR_PIECE_NO", ObjFeltCreditNoteDetails.getAttribute("GR_PIECE_NO").getString());
                resultSetHistory.updateString("GR_INVOICE_NO", ObjFeltCreditNoteDetails.getAttribute("GR_INVOICE_NO").getString());
                resultSetHistory.updateString("GR_INVOICE_DATE", ObjFeltCreditNoteDetails.getAttribute("GR_INVOICE_DATE").getString());
                resultSetHistory.updateString("GR_ALPHA_INVOICE_NO", ObjFeltCreditNoteDetails.getAttribute("GR_ALPHA_INVOICE_NO").getString());
                resultSetHistory.updateString("GR_QUALITY_NO", ObjFeltCreditNoteDetails.getAttribute("GR_QUALITY_NO").getString());
                resultSetHistory.updateString("GR_SHADE", ObjFeltCreditNoteDetails.getAttribute("GR_SHADE").getString());
                resultSetHistory.updateString("GR_UNIT_CODE", ObjFeltCreditNoteDetails.getAttribute("GR_UNIT_CODE").getString());
                resultSetHistory.updateString("GR_GROSS_QTY", ObjFeltCreditNoteDetails.getAttribute("GR_GROSS_QTY").getString());
                resultSetHistory.updateString("GR_NET_QTY", ObjFeltCreditNoteDetails.getAttribute("GR_NET_QTY").getString());
                resultSetHistory.updateString("GR_RATE", ObjFeltCreditNoteDetails.getAttribute("GR_RATE").getString());
                resultSetHistory.updateString("GR_INVOICE_DISC", ObjFeltCreditNoteDetails.getAttribute("GR_INVOICE_DISC").getString());
                resultSetHistory.updateString("GR_ADD_DISC", ObjFeltCreditNoteDetails.getAttribute("GR_ADD_DISC").getString());
                resultSetHistory.updateString("GR_INVOICE_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_INVOICE_AMOUNT").getString());
                resultSetHistory.updateString("GR_SHOT_LENGTH_PER", ObjFeltCreditNoteDetails.getAttribute("GR_SHOT_LENGTH_PER").getString());
                resultSetHistory.updateString("GR_SHOT_LENGTH_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_SHOT_LENGTH_AMOUNT").getString());
                resultSetHistory.updateString("GR_GROSS_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_GROSS_AMOUNT").getString());
                resultSetHistory.updateString("GR_STOCK_LOT_PER", ObjFeltCreditNoteDetails.getAttribute("GR_STOCK_LOT_PER").getString());
                resultSetHistory.updateString("GR_STOCK_LOT_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_STOCK_LOT_AMOUNT").getString());
                resultSetHistory.updateString("GR_REVERSE_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_REVERSE_AMOUNT").getString());
                resultSetHistory.updateString("GR_NET_GR_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_NET_GR_AMOUNT").getString());
                resultSetHistory.updateString("GR_HSN_NO", ObjFeltCreditNoteDetails.getAttribute("GR_HSN_NO").getString());
                resultSetHistory.updateString("FLAG", ObjFeltCreditNoteDetails.getAttribute("FLAG").getString());
                resultSetHistory.updateString("GR_QTY", ObjFeltCreditNoteDetails.getAttribute("GR_QTY").getString());
                resultSetHistory.updateString("GR_CGST_PER", ObjFeltCreditNoteDetails.getAttribute("GR_CGST_PER").getString());
                resultSetHistory.updateString("GR_CGST_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_CGST_AMOUNT").getString());
                resultSetHistory.updateString("GR_SGST_PER", ObjFeltCreditNoteDetails.getAttribute("GR_SGST_PER").getString());
                resultSetHistory.updateString("GR_SGST_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_SGST_AMOUNT").getString());
                resultSetHistory.updateString("GR_IGST_PER", ObjFeltCreditNoteDetails.getAttribute("GR_IGST_PER").getString());
                resultSetHistory.updateString("GR_IGST_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_IGST_AMOUNT").getString());
                resultSetHistory.updateString("GR_GST_COMPOSITION_PER", ObjFeltCreditNoteDetails.getAttribute("GR_GST_COMPOSITION_PER").getString());
                resultSetHistory.updateString("GR_GST_COMPOSITION_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_GST_COMPOSITION_AMOUNT").getString());
                resultSetHistory.updateString("GR_TOTAL_AMT", ObjFeltCreditNoteDetails.getAttribute("GR_TOTAL_AMT").getString());
                resultSetHistory.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                resultSetHistory.updateString("CREATED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateInt("UPDATED_BY", (int) getAttribute("UPDATED_BY").getVal());
                resultSetHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
                resultSetHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
                resultSetHistory.updateBoolean("APPROVED", false);
                resultSetHistory.updateString("APPROVED_DATE", "0000-00-00");
                resultSetHistory.updateBoolean("REJECTED", false);
                resultSetHistory.updateString("REJECTED_DATE", "0000-00-00");
                resultSetHistory.updateInt("CANCELED", 0);
                resultSetHistory.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                resultSetHistory.updateInt("CHANGED", 1);
                resultSetHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                resultSetHistory.insertRow();

            }

            for (int i = 1; i <= hmFeltCreditNoteDetails1.size(); i++) {

                clsSuitingGRDetails ObjFeltCreditNoteDetails1 = (clsSuitingGRDetails) hmFeltCreditNoteDetails1.get(Integer.toString(i));

                rsDetail.moveToInsertRow();
                rsDetail.updateString("GR_ID", getAttribute("GR_ID").getString());
                rsDetail.updateString("GR_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("GR_DATE").getString()));
                rsDetail.updateString("HSN_NO", ObjFeltCreditNoteDetails1.getAttribute("HSN_NO").getString());
                rsDetail.updateString("INVOICE_NO", ObjFeltCreditNoteDetails1.getAttribute("INVOICE_NO").getString());
                rsDetail.updateString("INVOICE_DATE", ObjFeltCreditNoteDetails1.getAttribute("INVOICE_DATE").getString());
                rsDetail.updateDouble("GR_QTY", (double) ObjFeltCreditNoteDetails1.getAttribute("GR_QTY").getDouble());
                rsDetail.updateDouble("NET_AMOUNT", (double) ObjFeltCreditNoteDetails1.getAttribute("NET_AMOUNT").getDouble());
                rsDetail.updateDouble("NET_GR_AMOUNT", (double) ObjFeltCreditNoteDetails1.getAttribute("NET_GR_AMOUNT").getDouble());
                rsDetail.updateDouble("CGST_AMOUNT", (double) ObjFeltCreditNoteDetails1.getAttribute("CGST_AMOUNT").getDouble());
                rsDetail.updateDouble("SGST_AMOUNT", (double) ObjFeltCreditNoteDetails1.getAttribute("SGST_AMOUNT").getDouble());
                rsDetail.updateDouble("IGST_AMOUNT", (double) ObjFeltCreditNoteDetails1.getAttribute("IGST_AMOUNT").getDouble());
                rsDetail.updateDouble("COMPOSITION_AMOUNT", (double) ObjFeltCreditNoteDetails1.getAttribute("COMPOSITION_AMOUNT").getDouble());
                rsDetail.insertRow();

            }

            //======== Update the Approval Flow =========
            ApprovalFlow ObjFeltProductionApprovalFlow = new ApprovalFlow();
            ObjFeltProductionApprovalFlow.CompanyID = (int) getAttribute("COMPANY_ID").getVal();
            ObjFeltProductionApprovalFlow.ModuleID = 757; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("GR_ID").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName = "STGSALES.D_STG_GOODS_RETURNS_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator = true;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("REJECTED_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "GR_ID";

            if (ObjFeltProductionApprovalFlow.Status.equals("H")) {
                ObjFeltProductionApprovalFlow.Status = "A";
                ObjFeltProductionApprovalFlow.To = ObjFeltProductionApprovalFlow.From;
                ObjFeltProductionApprovalFlow.UpdateFlow();
            } else {
                if (!ObjFeltProductionApprovalFlow.UpdateFlow()) {
                    LastError = ObjFeltProductionApprovalFlow.LastError;
                }
            }

            //--------- Approval Flow Update complete -----------
            // Update  in Order Master Table To confirm that Weaving has completed
            if (ObjFeltProductionApprovalFlow.Status.equals("F")) {

            }

            LoadData();

            resultSetTemp.close();
            statementTemp.close();
            resultSetHistory.close();
            statementHistory.close();
            return true;

        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    //Updates current record
    public boolean Update() {

        ResultSet resultSetTemp, resultSetHistory, rsHeader, rsDetail;
        Statement statementTemp, statementHistory, stHeader, stDetail;
        int revisionNo = 1;
        try {
            // Production data connection
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  STGSALES.D_STG_GOODS_RETURNS_DETAIL WHERE GR_ID=''order by sr_no*1");

            // Production data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //Get the Maximum Revision No in History Table.
            resultSetHistory = statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM STGSALES.D_STG_GOODS_RETURNS_DETAIL_H WHERE GR_ID='" + getAttribute("GR_ID").getString() + "' order by sr_no*1");
            resultSetHistory.first();
            if (resultSetHistory.getRow() > 0) {
                revisionNo = resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from production data table before insert
            statementHistory.execute("DELETE FROM STGSALES.D_STG_GOODS_RETURNS_DETAIL WHERE GR_ID='" + getAttribute("GR_ID").getString() + "'");

            resultSetHistory = statementHistory.executeQuery("SELECT * FROM STGSALES.D_STG_GOODS_RETURNS_DETAIL_H WHERE GR_ID='" + getAttribute("GR_ID").getString() + "' order by sr_no*1");

            stHeader = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHeader = stHeader.executeQuery("SELECT * FROM  STGSALES.D_STG_GOODS_RETURNS_HEADER_H WHERE GR_ID=''");

            //Now Update records into FELT_CN_TEMP_HEADER tables
            resultSet.updateString("GR_ID", getAttribute("GR_ID").getString());
            resultSet.updateString("GR_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("GR_DATE").getString()));
            resultSet.updateString("GR_DESC_NO", getAttribute("GR_DESC_NO").getString());
            resultSet.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            resultSet.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
            resultSet.updateString("CITY", getAttribute("CITY").getString());
            resultSet.updateString("INWARD_NO", getAttribute("INWARD_NO").getString());
            resultSet.updateString("INWARD_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("INWARD_DATE").getString()));
            resultSet.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            resultSet.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            resultSet.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
            resultSet.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            resultSet.updateBoolean("APPROVED", false);
            resultSet.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateBoolean("CANCELED", false);
            resultSet.updateBoolean("REJECTED", false);
            resultSet.updateString("REJECTED_DATE", "0000-00-00");
            resultSet.updateBoolean("CHANGED", true);
            resultSet.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

            resultSet.updateRow();

            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM STGSALES.D_STG_GOODS_RETURNS_DETAIL_H WHERE GR_ID='" + (String) getAttribute("GR_ID").getObj() + "'");
            RevNo++;
            String RevDocNo = (String) getAttribute("GR_ID").getObj();

            rsHeader.moveToInsertRow();

            rsHeader.updateInt("REVISION_NO", RevNo);
            rsHeader.updateInt("UPDATED_BY", (int) getAttribute("MODIFIED_BY").getVal());
            rsHeader.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            rsHeader.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeader.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
            rsHeader.updateString("GR_ID", getAttribute("GR_ID").getString());
            rsHeader.updateString("GR_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("GR_DATE").getString()));
            rsHeader.updateString("GR_DESC_NO", getAttribute("GR_DESC_NO").getString());
            rsHeader.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsHeader.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
            rsHeader.updateString("CITY", getAttribute("CITY").getString());
            rsHeader.updateString("INWARD_NO", getAttribute("INWARD_NO").getString());
            rsHeader.updateString("INWARD_DATE", getAttribute("INWARD_DATE").getString());
            rsHeader.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsHeader.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            rsHeader.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeader.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
            rsHeader.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeader.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsHeader.updateBoolean("APPROVED", false);
            rsHeader.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeader.updateBoolean("CANCELED", false);
            rsHeader.updateBoolean("REJECTED", false);
            rsHeader.updateString("REJECTED_DATE", "0000-00-00");
            rsHeader.updateBoolean("CHANGED", true);
            rsHeader.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

            rsHeader.insertRow();

            for (int i = 1; i <= hmFeltCreditNoteDetails.size(); i++) {
                clsSuitingGRDetails ObjFeltCreditNoteDetails = (clsSuitingGRDetails) hmFeltCreditNoteDetails.get(Integer.toString(i));

                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateString("GR_ID", getAttribute("GR_ID").getString());
                resultSetTemp.updateString("GR_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("GR_DATE").getString()));
                resultSetTemp.updateString("SR_NO", ObjFeltCreditNoteDetails.getAttribute("SR_NO").getString());
                resultSetTemp.updateString("GR_PIECE_NO", ObjFeltCreditNoteDetails.getAttribute("GR_PIECE_NO").getString());
                resultSetTemp.updateString("GR_INVOICE_NO", ObjFeltCreditNoteDetails.getAttribute("GR_INVOICE_NO").getString());
                resultSetTemp.updateString("GR_INVOICE_DATE", ObjFeltCreditNoteDetails.getAttribute("GR_INVOICE_DATE").getString());
                resultSetTemp.updateString("GR_ALPHA_INVOICE_NO", ObjFeltCreditNoteDetails.getAttribute("GR_ALPHA_INVOICE_NO").getString());
                resultSetTemp.updateString("GR_QUALITY_NO", ObjFeltCreditNoteDetails.getAttribute("GR_QUALITY_NO").getString());
                resultSetTemp.updateString("GR_SHADE", ObjFeltCreditNoteDetails.getAttribute("GR_SHADE").getString());
                resultSetTemp.updateString("GR_UNIT_CODE", ObjFeltCreditNoteDetails.getAttribute("GR_UNIT_CODE").getString());
                resultSetTemp.updateString("GR_GROSS_QTY", ObjFeltCreditNoteDetails.getAttribute("GR_GROSS_QTY").getString());
                resultSetTemp.updateString("GR_NET_QTY", ObjFeltCreditNoteDetails.getAttribute("GR_NET_QTY").getString());
                resultSetTemp.updateString("GR_RATE", ObjFeltCreditNoteDetails.getAttribute("GR_RATE").getString());
                resultSetTemp.updateString("GR_INVOICE_DISC", ObjFeltCreditNoteDetails.getAttribute("GR_INVOICE_DISC").getString());
                resultSetTemp.updateString("GR_ADD_DISC", ObjFeltCreditNoteDetails.getAttribute("GR_ADD_DISC").getString());
                resultSetTemp.updateString("GR_INVOICE_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_INVOICE_AMOUNT").getString());
                resultSetTemp.updateString("GR_SHOT_LENGTH_PER", ObjFeltCreditNoteDetails.getAttribute("GR_SHOT_LENGTH_PER").getString());
                resultSetTemp.updateString("GR_SHOT_LENGTH_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_SHOT_LENGTH_AMOUNT").getString());
                resultSetTemp.updateString("GR_GROSS_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_GROSS_AMOUNT").getString());
                resultSetTemp.updateString("GR_STOCK_LOT_PER", ObjFeltCreditNoteDetails.getAttribute("GR_STOCK_LOT_PER").getString());
                resultSetTemp.updateString("GR_STOCK_LOT_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_STOCK_LOT_AMOUNT").getString());
                resultSetTemp.updateString("GR_REVERSE_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_REVERSE_AMOUNT").getString());
                resultSetTemp.updateString("GR_NET_GR_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_NET_GR_AMOUNT").getString());
                resultSetTemp.updateString("GR_HSN_NO", ObjFeltCreditNoteDetails.getAttribute("GR_HSN_NO").getString());
                resultSetTemp.updateString("FLAG", ObjFeltCreditNoteDetails.getAttribute("FLAG").getString());
                resultSetTemp.updateString("GR_QTY", ObjFeltCreditNoteDetails.getAttribute("GR_QTY").getString());
                resultSetTemp.updateString("GR_CGST_PER", ObjFeltCreditNoteDetails.getAttribute("GR_CGST_PER").getString());
                resultSetTemp.updateString("GR_CGST_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_CGST_AMOUNT").getString());
                resultSetTemp.updateString("GR_SGST_PER", ObjFeltCreditNoteDetails.getAttribute("GR_SGST_PER").getString());
                resultSetTemp.updateString("GR_SGST_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_SGST_AMOUNT").getString());
                resultSetTemp.updateString("GR_IGST_PER", ObjFeltCreditNoteDetails.getAttribute("GR_IGST_PER").getString());
                resultSetTemp.updateString("GR_IGST_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_IGST_AMOUNT").getString());
                resultSetTemp.updateString("GR_GST_COMPOSITION_PER", ObjFeltCreditNoteDetails.getAttribute("GR_GST_COMPOSITION_PER").getString());
                resultSetTemp.updateString("GR_GST_COMPOSITION_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_GST_COMPOSITION_AMOUNT").getString());
                resultSetTemp.updateString("GR_TOTAL_AMT", ObjFeltCreditNoteDetails.getAttribute("GR_TOTAL_AMT").getString());
                resultSetTemp.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                resultSetTemp.updateString("CREATED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetTemp.updateInt("MODIFIED_BY", 0);
                resultSetTemp.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                resultSetTemp.updateBoolean("APPROVED", false);
                resultSetTemp.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                resultSetTemp.updateBoolean("REJECTED", false);
                resultSetTemp.updateString("REJECTED_DATE", "00000-00-00");
                resultSetTemp.updateInt("CANCELED", 0);
                resultSetTemp.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                resultSetTemp.updateInt("CHANGED", 1);
                resultSetTemp.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                resultSetTemp.insertRow();

                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO", RevNo);
                resultSetHistory.updateString("GR_ID", getAttribute("GR_ID").getString());
                resultSetHistory.updateString("GR_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("GR_DATE").getString()));
                resultSetHistory.updateString("SR_NO", ObjFeltCreditNoteDetails.getAttribute("SR_NO").getString());
                resultSetHistory.updateString("GR_PIECE_NO", ObjFeltCreditNoteDetails.getAttribute("GR_PIECE_NO").getString());
                resultSetHistory.updateString("GR_INVOICE_NO", ObjFeltCreditNoteDetails.getAttribute("GR_INVOICE_NO").getString());
                resultSetHistory.updateString("GR_INVOICE_DATE", ObjFeltCreditNoteDetails.getAttribute("GR_INVOICE_DATE").getString());
                resultSetHistory.updateString("GR_ALPHA_INVOICE_NO", ObjFeltCreditNoteDetails.getAttribute("GR_ALPHA_INVOICE_NO").getString());
                resultSetHistory.updateString("GR_QUALITY_NO", ObjFeltCreditNoteDetails.getAttribute("GR_QUALITY_NO").getString());
                resultSetHistory.updateString("GR_SHADE", ObjFeltCreditNoteDetails.getAttribute("GR_SHADE").getString());
                resultSetHistory.updateString("GR_UNIT_CODE", ObjFeltCreditNoteDetails.getAttribute("GR_UNIT_CODE").getString());
                resultSetHistory.updateString("GR_GROSS_QTY", ObjFeltCreditNoteDetails.getAttribute("GR_GROSS_QTY").getString());
                resultSetHistory.updateString("GR_NET_QTY", ObjFeltCreditNoteDetails.getAttribute("GR_NET_QTY").getString());
                resultSetHistory.updateString("GR_RATE", ObjFeltCreditNoteDetails.getAttribute("GR_RATE").getString());
                resultSetHistory.updateString("GR_INVOICE_DISC", ObjFeltCreditNoteDetails.getAttribute("GR_INVOICE_DISC").getString());
                resultSetHistory.updateString("GR_ADD_DISC", ObjFeltCreditNoteDetails.getAttribute("GR_ADD_DISC").getString());
                resultSetHistory.updateString("GR_INVOICE_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_INVOICE_AMOUNT").getString());
                resultSetHistory.updateString("GR_SHOT_LENGTH_PER", ObjFeltCreditNoteDetails.getAttribute("GR_SHOT_LENGTH_PER").getString());
                resultSetHistory.updateString("GR_SHOT_LENGTH_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_SHOT_LENGTH_AMOUNT").getString());
                resultSetHistory.updateString("GR_GROSS_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_GROSS_AMOUNT").getString());
                resultSetHistory.updateString("GR_STOCK_LOT_PER", ObjFeltCreditNoteDetails.getAttribute("GR_STOCK_LOT_PER").getString());
                resultSetHistory.updateString("GR_STOCK_LOT_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_STOCK_LOT_AMOUNT").getString());
                resultSetHistory.updateString("GR_REVERSE_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_REVERSE_AMOUNT").getString());
                resultSetHistory.updateString("GR_NET_GR_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_NET_GR_AMOUNT").getString());
                resultSetHistory.updateString("GR_HSN_NO", ObjFeltCreditNoteDetails.getAttribute("GR_HSN_NO").getString());
                resultSetHistory.updateString("FLAG", ObjFeltCreditNoteDetails.getAttribute("FLAG").getString());
                resultSetHistory.updateString("GR_QTY", ObjFeltCreditNoteDetails.getAttribute("GR_QTY").getString());
                resultSetHistory.updateString("GR_CGST_PER", ObjFeltCreditNoteDetails.getAttribute("GR_CGST_PER").getString());
                resultSetHistory.updateString("GR_CGST_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_CGST_AMOUNT").getString());
                resultSetHistory.updateString("GR_SGST_PER", ObjFeltCreditNoteDetails.getAttribute("GR_SGST_PER").getString());
                resultSetHistory.updateString("GR_SGST_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_SGST_AMOUNT").getString());
                resultSetHistory.updateString("GR_IGST_PER", ObjFeltCreditNoteDetails.getAttribute("GR_IGST_PER").getString());
                resultSetHistory.updateString("GR_IGST_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_IGST_AMOUNT").getString());
                resultSetHistory.updateString("GR_GST_COMPOSITION_PER", ObjFeltCreditNoteDetails.getAttribute("GR_GST_COMPOSITION_PER").getString());
                resultSetHistory.updateString("GR_GST_COMPOSITION_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("GR_GST_COMPOSITION_AMOUNT").getString());
                resultSetHistory.updateString("GR_TOTAL_AMT", ObjFeltCreditNoteDetails.getAttribute("GR_TOTAL_AMT").getString());
                resultSetHistory.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                resultSetHistory.updateString("CREATED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateInt("UPDATED_BY", (int) getAttribute("UPDATED_BY").getVal());
                resultSetHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
                resultSetHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
                resultSetHistory.updateInt("MODIFIED_BY", 0);
                resultSetHistory.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                resultSetHistory.updateBoolean("APPROVED", false);
                resultSetHistory.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                resultSetHistory.updateBoolean("REJECTED", false);
                resultSetHistory.updateString("REJECTED_DATE", "0000-00-00");
                resultSetHistory.updateInt("CANCELED", 0);
                resultSetHistory.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                resultSetHistory.updateInt("CHANGED", 1);
                resultSetHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                resultSetHistory.insertRow();

            }

            data.Execute("DELETE FROM STGSALES.D_SAL_HSN_INVOICE_POSTING WHERE GR_ID='" + getAttribute("GR_ID").getString() + "'");

            stDetail = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsDetail = stDetail.executeQuery("SELECT * FROM STGSALES.D_SAL_HSN_INVOICE_POSTING WHERE GR_ID=''");

            for (int i = 1; i <= hmFeltCreditNoteDetails1.size(); i++) {

                clsSuitingGRDetails ObjFeltCreditNoteDetails1 = (clsSuitingGRDetails) hmFeltCreditNoteDetails1.get(Integer.toString(i));

                rsDetail.moveToInsertRow();
                rsDetail.updateString("GR_ID", getAttribute("GR_ID").getString());
                rsDetail.updateString("GR_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("GR_DATE").getString()));
                rsDetail.updateString("HSN_NO", ObjFeltCreditNoteDetails1.getAttribute("HSN_NO").getString());
                rsDetail.updateString("INVOICE_NO", ObjFeltCreditNoteDetails1.getAttribute("INVOICE_NO").getString());
                rsDetail.updateString("INVOICE_DATE", ObjFeltCreditNoteDetails1.getAttribute("INVOICE_DATE").getString());
                rsDetail.updateDouble("GR_QTY", (double) ObjFeltCreditNoteDetails1.getAttribute("GR_QTY").getDouble());
                rsDetail.updateDouble("NET_AMOUNT", (double) ObjFeltCreditNoteDetails1.getAttribute("NET_AMOUNT").getDouble());
                rsDetail.updateDouble("NET_GR_AMOUNT", (double) ObjFeltCreditNoteDetails1.getAttribute("NET_GR_AMOUNT").getDouble());
                rsDetail.updateDouble("CGST_AMOUNT", (double) ObjFeltCreditNoteDetails1.getAttribute("CGST_AMOUNT").getDouble());
                rsDetail.updateDouble("SGST_AMOUNT", (double) ObjFeltCreditNoteDetails1.getAttribute("SGST_AMOUNT").getDouble());
                rsDetail.updateDouble("IGST_AMOUNT", (double) ObjFeltCreditNoteDetails1.getAttribute("IGST_AMOUNT").getDouble());
                rsDetail.updateDouble("COMPOSITION_AMOUNT", (double) ObjFeltCreditNoteDetails1.getAttribute("COMPOSITION_AMOUNT").getDouble());
                rsDetail.insertRow();

            }

            //======== Update the Approval Flow =========
            ApprovalFlow ObjFeltProductionApprovalFlow = new ApprovalFlow();
            ObjFeltProductionApprovalFlow.CompanyID = (int) getAttribute("COMPANY_ID").getVal();
            ObjFeltProductionApprovalFlow.ModuleID = 757; //Felt Order Updation Module
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("GR_ID").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName = "STGSALES.D_STG_GOODS_RETURNS_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator = false;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("REJECTED_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "GR_ID";

            if (getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFeltProductionApprovalFlow.To = (int) getAttribute("SEND_DOC_TO").getVal();
                ObjFeltProductionApprovalFlow.ExplicitSendTo = true;
            }

            //==== Handling Rejected Documents ==========//
            boolean IsRejected = getAttribute("REJECTED").getBool();

            if (IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE STGSALES.D_STG_GOODS_RETURNS_HEADER SET REJECTED=0,CHANGED=1 WHERE GR_ID ='" + getAttribute("GR_ID").getString() + "'");
                ObjFeltProductionApprovalFlow.IsCreator = true;
            }
            //==========================================//

            if (ObjFeltProductionApprovalFlow.Status.equals("H")) {
                if (IsRejected) {
                    ObjFeltProductionApprovalFlow.Status = "A";
                    ObjFeltProductionApprovalFlow.To = ObjFeltProductionApprovalFlow.From;
                    ObjFeltProductionApprovalFlow.UpdateFlow();
                }
            } else {
                if (!ObjFeltProductionApprovalFlow.UpdateFlow()) {
                    LastError = ObjFeltProductionApprovalFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------

            // Update  in Order Master Table To confirm that Weaving has completed
            if (ObjFeltProductionApprovalFlow.Status.equals("F")) {

            }

            //===============Accounts CreditNote Generation=============//
            try {
                if (ObjFeltProductionApprovalFlow.Status.equals("F")) {

                    PostCNHSNGST((int) getAttribute("COMPANY_ID").getVal(), getAttribute("GR_ID").getString(), getAttribute("GR_DATE").getString());

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            //====================================================//

            setData();
            resultSetTemp.close();
            statementTemp.close();
            resultSetHistory.close();
            statementHistory.close();

            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This routine checks whether the item is deletable or not. Criteria is
     * Approved item cannot be delete, and if not approved then user id is
     * checked whether doucment is created by the user. Only creator can delete
     * the document. After checking it deletes the record of selected production
     * date and document no.
     */
    public boolean CanDelete(String documentNo, String stringProductionDate, int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";

        try {
            tmpStmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            strSQL = "SELECT COUNT(*) AS COUNT FROM STGSALES.D_STG_GOODS_RETURNS_HEADER WHERE  FELT_AMEND_ID='" + documentNo + "' AND APPROVED=" + 1;
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM DINESHMILLS.FELT_PROD_DOC_DATA WHERE MODULE_ID=757 AND USER_ID=" + userID + " AND DOC_NO='" + documentNo + "' AND STATUS='W'";
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();

                if (rsTmp.getInt("COUNT") > 0) {
                    strSQL = "DELETE FROM STGSALES.D_STG_GOODS_RETURNS_HEADER WHERE "
                            + " AND CN_DATE= '" + EITLERPGLOBAL.formatDateDB(stringProductionDate) + "'"
                            + " AND GR_ID='" + documentNo + "'";
                    tmpStmt.executeUpdate(strSQL);
                    LoadData();
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            LastError = "Error occured while deleting." + e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This routine checks and returns whether the item is editable or not.
     * Criteria is Approved item cannot be changed and if not approved then user
     * id is checked whether doucment is waiting for his approval.
     */
    public boolean IsEditable(int pCompanyID, String orderupdDocumentNo, int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";
        try {
            if (HistoryView) {
                return false;
            }
            tmpStmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //First check that document is approved or not
            strSQL = "SELECT COUNT(*) AS COUNT FROM STGSALES.D_STG_GOODS_RETURNS_HEADER WHERE GR_ID='" + orderupdDocumentNo + "' AND APPROVED=1";
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND MODULE_ID=757 AND DOC_NO='" + orderupdDocumentNo + "' AND USER_ID=" + Integer.toString(userID) + " AND STATUS='W'";
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
            e.printStackTrace();
            return false;
        }
    }

    public boolean Filter(String stringFindQuery) {
        Ready = false;
        try {
            // String strSql = "SELECT DISTINCT AMEND_DATE FROM STGSALES.D_STG_GOODS_RETURNS_HEADER WHERE  " + stringFindQuery + " ORDER BY CN_DATE";
            String strSql = "SELECT * FROM STGSALES.D_STG_GOODS_RETURNS_HEADER WHERE  " + stringFindQuery + "";
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery(strSql);
            if (!resultSet.first()) {
                LoadData();
                Ready = true;
                return false;
            } else {
                Ready = true;
                MoveLast();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean setData() {
        ResultSet resultSetTemp, resultSetTemp1;
        Statement statementTemp, statementTemp1;
        int serialNoCounter = 0;
        int serialNoCounter1 = 0;
        int RevNo = 0;

        try {

            setAttribute("REVISION_NO", 0);
            setAttribute("COMPANY_ID", 2);
            setAttribute("GR_DATE", resultSet.getDate("GR_DATE"));
            setAttribute("GR_ID", resultSet.getString("GR_ID"));
            setAttribute("GR_DESC_NO", resultSet.getString("GR_DESC_NO"));
            setAttribute("PARTY_CODE", resultSet.getString("PARTY_CODE"));
            setAttribute("PARTY_NAME", resultSet.getString("PARTY_NAME"));
            setAttribute("CITY", resultSet.getString("CITY"));
            setAttribute("INWARD_NO", resultSet.getString("INWARD_NO"));
            //setAttribute("INWARD_DATE",resultSet.getDate("INWARD_DATE"));
            setAttribute("INWARD_DATE", resultSet.getString("INWARD_DATE"));
            setAttribute("REJECTED_REMARKS", resultSet.getString("REJECTED_REMARKS"));

            hmFeltCreditNoteDetails.clear();

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  STGSALES.D_STG_GOODS_RETURNS_DETAIL WHERE GR_ID='" + resultSet.getString("GR_ID") + "'  ORDER BY GR_ID,SR_NO*1");

            while (resultSetTemp.next()) {
                serialNoCounter++;

                setAttribute("CREATED_BY", resultSetTemp.getInt("CREATED_BY"));
                setAttribute("CREATED_DATE", resultSetTemp.getString("CREATED_DATE"));
                setAttribute("MODIFIED_BY", resultSetTemp.getInt("MODIFIED_BY"));
                setAttribute("MODIFIED_DATE", resultSetTemp.getString("MODIFIED_DATE"));
                setAttribute("APPROVED", resultSetTemp.getInt("APPROVED"));
                setAttribute("APPROVED_DATE", resultSetTemp.getString("APPROVED_DATE"));
                setAttribute("REJECTED", resultSetTemp.getBoolean("REJECTED"));
                setAttribute("REJECTED_DATE", resultSetTemp.getString("REJECTED_DATE"));
                setAttribute("CANCELED", resultSetTemp.getInt("CANCELED"));
                setAttribute("HIERARCHY_ID", resultSetTemp.getInt("HIERARCHY_ID"));
                setAttribute("REJECTED_REMARKS", resultSetTemp.getString("REJECTED_REMARKS"));

                clsSuitingGRDetails ObjFeltCreditNoteDetails = new clsSuitingGRDetails();

                ObjFeltCreditNoteDetails.setAttribute("SR_NO", resultSetTemp.getString("SR_NO"));
                ObjFeltCreditNoteDetails.setAttribute("GR_PIECE_NO", resultSetTemp.getString("GR_PIECE_NO"));
                ObjFeltCreditNoteDetails.setAttribute("GR_INVOICE_NO", resultSetTemp.getString("GR_INVOICE_NO"));
                ObjFeltCreditNoteDetails.setAttribute("GR_INVOICE_DATE", resultSetTemp.getString("GR_INVOICE_DATE"));
                ObjFeltCreditNoteDetails.setAttribute("GR_ALPHA_INVOICE_NO", resultSetTemp.getString("GR_ALPHA_INVOICE_NO"));
                ObjFeltCreditNoteDetails.setAttribute("GR_QUALITY_NO", resultSetTemp.getString("GR_QUALITY_NO"));
                ObjFeltCreditNoteDetails.setAttribute("GR_SHADE", resultSetTemp.getString("GR_SHADE"));
                ObjFeltCreditNoteDetails.setAttribute("GR_UNIT_CODE", resultSetTemp.getString("GR_UNIT_CODE"));
                ObjFeltCreditNoteDetails.setAttribute("GR_GROSS_QTY", resultSetTemp.getString("GR_GROSS_QTY"));
                ObjFeltCreditNoteDetails.setAttribute("FLAG", resultSetTemp.getString("FLAG"));
                ObjFeltCreditNoteDetails.setAttribute("GR_NET_QTY", resultSetTemp.getString("GR_NET_QTY"));
                ObjFeltCreditNoteDetails.setAttribute("GR_QTY", resultSetTemp.getString("GR_QTY"));
                ObjFeltCreditNoteDetails.setAttribute("GR_RATE", resultSetTemp.getString("GR_RATE"));
                ObjFeltCreditNoteDetails.setAttribute("GR_INVOICE_DISC", resultSetTemp.getString("GR_INVOICE_DISC"));
                ObjFeltCreditNoteDetails.setAttribute("GR_ADD_DISC", resultSetTemp.getString("GR_ADD_DISC"));
                ObjFeltCreditNoteDetails.setAttribute("GR_INVOICE_AMOUNT", resultSetTemp.getString("GR_INVOICE_AMOUNT"));
                ObjFeltCreditNoteDetails.setAttribute("GR_SHOT_LENGTH_PER", resultSetTemp.getString("GR_SHOT_LENGTH_PER"));
                ObjFeltCreditNoteDetails.setAttribute("GR_SHOT_LENGTH_AMOUNT", resultSetTemp.getString("GR_SHOT_LENGTH_AMOUNT"));
                ObjFeltCreditNoteDetails.setAttribute("GR_GROSS_AMOUNT", resultSetTemp.getString("GR_GROSS_AMOUNT"));
                ObjFeltCreditNoteDetails.setAttribute("GR_STOCK_LOT_PER", resultSetTemp.getString("GR_STOCK_LOT_PER"));
                ObjFeltCreditNoteDetails.setAttribute("GR_STOCK_LOT_AMOUNT", resultSetTemp.getString("GR_STOCK_LOT_AMOUNT"));
                ObjFeltCreditNoteDetails.setAttribute("GR_REVERSE_AMOUNT", resultSetTemp.getString("GR_REVERSE_AMOUNT"));
                ObjFeltCreditNoteDetails.setAttribute("GR_NET_GR_AMOUNT", resultSetTemp.getString("GR_NET_GR_AMOUNT"));
                ObjFeltCreditNoteDetails.setAttribute("GR_HSN_NO", resultSetTemp.getString("GR_HSN_NO"));
                ObjFeltCreditNoteDetails.setAttribute("GR_CGST_PER", resultSetTemp.getString("GR_CGST_PER"));
                ObjFeltCreditNoteDetails.setAttribute("GR_CGST_AMOUNT", resultSetTemp.getString("GR_CGST_AMOUNT"));
                ObjFeltCreditNoteDetails.setAttribute("GR_SGST_PER", resultSetTemp.getString("GR_SGST_PER"));
                ObjFeltCreditNoteDetails.setAttribute("GR_SGST_AMOUNT", resultSetTemp.getString("GR_SGST_AMOUNT"));
                ObjFeltCreditNoteDetails.setAttribute("GR_IGST_PER", resultSetTemp.getString("GR_IGST_PER"));
                ObjFeltCreditNoteDetails.setAttribute("GR_IGST_AMOUNT", resultSetTemp.getString("GR_IGST_AMOUNT"));
                ObjFeltCreditNoteDetails.setAttribute("GR_GST_COMPOSITION_PER", resultSetTemp.getString("GR_GST_COMPOSITION_PER"));
                ObjFeltCreditNoteDetails.setAttribute("GR_GST_COMPOSITION_AMOUNT", resultSetTemp.getString("GR_GST_COMPOSITION_AMOUNT"));
                ObjFeltCreditNoteDetails.setAttribute("GR_TOTAL_AMT", resultSetTemp.getString("GR_TOTAL_AMT"));

//                ObjFeltCreditNoteDetails.setAttribute("HSN_NO",resultSetTemp.getString("HSN_NO"));
//                ObjFeltCreditNoteDetails.setAttribute("INVOICE_NO",resultSetTemp.getString("INVOICE_NO"));
//                ObjFeltCreditNoteDetails.setAttribute("INVOICE_DATE",resultSetTemp.getString("INVOICE_DATE"));
//                ObjFeltCreditNoteDetails.setAttribute("NET_AMOUNT",resultSetTemp.getString("NET_AMOUNT"));
//                ObjFeltCreditNoteDetails.setAttribute("NET_GR_AMOUNT",resultSetTemp.getString("NET_GR_AMOUNT"));
//                ObjFeltCreditNoteDetails.setAttribute("CGST_AMOUNT",resultSetTemp.getString("CGST_AMOUNT"));
//                ObjFeltCreditNoteDetails.setAttribute("SGST_AMOUNT",resultSetTemp.getString("SGST_AMOUNT"));
//                ObjFeltCreditNoteDetails.setAttribute("IGST_AMOUNT",resultSetTemp.getString("IGST_AMOUNT"));
//                ObjFeltCreditNoteDetails.setAttribute("COMPOSITION_AMOUNT",resultSetTemp.getString("COMPOSITION_AMOUNT"));
                hmFeltCreditNoteDetails.put(Integer.toString(serialNoCounter), ObjFeltCreditNoteDetails);
            }

            hmFeltCreditNoteDetails1.clear();
            statementTemp1 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp1 = statementTemp1.executeQuery("SELECT * FROM  STGSALES.D_SAL_HSN_INVOICE_POSTING WHERE GR_ID='" + resultSet.getString("GR_ID") + "'");

            while (resultSetTemp1.next()) {
                serialNoCounter1++;
                clsSuitingGRDetails ObjFeltCreditNoteDetails1 = new clsSuitingGRDetails();

                ObjFeltCreditNoteDetails1.setAttribute("HSN_NO", resultSetTemp1.getString("HSN_NO"));
                ObjFeltCreditNoteDetails1.setAttribute("INVOICE_NO", resultSetTemp1.getString("INVOICE_NO"));
                ObjFeltCreditNoteDetails1.setAttribute("INVOICE_DATE", resultSetTemp1.getString("INVOICE_DATE"));
                ObjFeltCreditNoteDetails1.setAttribute("GR_QTY", resultSetTemp1.getDouble("GR_QTY"));
                ObjFeltCreditNoteDetails1.setAttribute("NET_AMOUNT", resultSetTemp1.getDouble("NET_AMOUNT"));
                ObjFeltCreditNoteDetails1.setAttribute("NET_GR_AMOUNT", resultSetTemp1.getDouble("NET_GR_AMOUNT"));
                ObjFeltCreditNoteDetails1.setAttribute("CGST_AMOUNT", resultSetTemp1.getDouble("CGST_AMOUNT"));
                ObjFeltCreditNoteDetails1.setAttribute("SGST_AMOUNT", resultSetTemp1.getDouble("SGST_AMOUNT"));
                ObjFeltCreditNoteDetails1.setAttribute("IGST_AMOUNT", resultSetTemp1.getDouble("IGST_AMOUNT"));
                ObjFeltCreditNoteDetails1.setAttribute("COMPOSITION_AMOUNT", resultSetTemp1.getDouble("COMPOSITION_AMOUNT"));

                hmFeltCreditNoteDetails1.put(Integer.toString(serialNoCounter1), ObjFeltCreditNoteDetails1);
            }
            resultSetTemp.close();
            statementTemp.close();

            resultSetTemp1.close();
            statementTemp1.close();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    public boolean setHistoryData(String pProductionDate, String pDocNo) {
        ResultSet resultSetTemp;
        Statement statementTemp;
        int serialNoCounter = 0;
        int RevNo = 0;
        try {

            //Now Populate the collection, first clear the collection
            hmFeltCreditNoteDetails.clear();

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  STGSALES.D_STG_GOODS_RETURNS_HEADER_H WHERE GR_ID='" + pDocNo + "'");

            while (resultSetTemp.next()) {
                serialNoCounter++;
                setAttribute("GR_ID", resultSetTemp.getString("GR_ID"));
                setAttribute("UPDATED_BY", resultSetTemp.getInt("UPDATED_BY"));
                setAttribute("ENTRY_DATE", resultSetTemp.getString("ENTRY_DATE"));
                setAttribute("HIERARCHY_ID", resultSetTemp.getInt("HIERARCHY_ID"));

                clsSuitingGRDetails ObjFeltCreditNoteDetails = new clsSuitingGRDetails();

                ObjFeltCreditNoteDetails.setAttribute("SR_NO", resultSetTemp.getString("SR_NO"));
                ObjFeltCreditNoteDetails.setAttribute("GR_PIECE_NO", resultSetTemp.getString("GR_PIECE_NO"));
                ObjFeltCreditNoteDetails.setAttribute("GR_INVOICE_NO", resultSetTemp.getString("GR_INVOICE_NO"));
                ObjFeltCreditNoteDetails.setAttribute("GR_INVOICE_DATE", resultSetTemp.getString("GR_INVOICE_DATE"));
                ObjFeltCreditNoteDetails.setAttribute("GR_ALPHA_INVOICE_NO", resultSetTemp.getString("GR_ALPHA_INVOICE_NO"));
                ObjFeltCreditNoteDetails.setAttribute("GR_QUALITY_NO", resultSetTemp.getString("GR_QUALITY_NO"));
                ObjFeltCreditNoteDetails.setAttribute("GR_SHADE", resultSetTemp.getString("GR_SHADE"));
                ObjFeltCreditNoteDetails.setAttribute("GR_UNIT_CODE", resultSetTemp.getString("GR_UNIT_CODE"));
                ObjFeltCreditNoteDetails.setAttribute("GR_GROSS_QTY", resultSetTemp.getString("GR_GROSS_QTY"));
                ObjFeltCreditNoteDetails.setAttribute("FLAG", resultSetTemp.getString("FLAG"));
                ObjFeltCreditNoteDetails.setAttribute("GR_NET_QTY", resultSetTemp.getString("GR_NET_QTY"));
                ObjFeltCreditNoteDetails.setAttribute("GR_QTY", resultSetTemp.getString("GR_QTY"));
                ObjFeltCreditNoteDetails.setAttribute("GR_RATE", resultSetTemp.getString("GR_RATE"));
                ObjFeltCreditNoteDetails.setAttribute("GR_INVOICE_DISC", resultSetTemp.getString("GR_INVOICE_DISC"));
                ObjFeltCreditNoteDetails.setAttribute("GR_ADD_DISC", resultSetTemp.getString("GR_ADD_DISC"));
                ObjFeltCreditNoteDetails.setAttribute("GR_INVOICE_AMOUNT", resultSetTemp.getString("GR_INVOICE_AMOUNT"));
                ObjFeltCreditNoteDetails.setAttribute("GR_SHOT_LENGTH_PER", resultSetTemp.getString("GR_SHOT_LENGTH_PER"));
                ObjFeltCreditNoteDetails.setAttribute("GR_SHOT_LENGTH_AMOUNT", resultSetTemp.getString("GR_SHOT_LENGTH_AMOUNT"));
                ObjFeltCreditNoteDetails.setAttribute("GR_GROSS_AMOUNT", resultSetTemp.getString("GR_GROSS_AMOUNT"));
                ObjFeltCreditNoteDetails.setAttribute("GR_STOCK_LOT_PER", resultSetTemp.getString("GR_STOCK_LOT_PER"));
                ObjFeltCreditNoteDetails.setAttribute("GR_STOCK_LOT_AMOUNT", resultSetTemp.getString("GR_STOCK_LOT_AMOUNT"));
                ObjFeltCreditNoteDetails.setAttribute("GR_REVERSE_AMOUNT", resultSetTemp.getString("GR_REVERSE_AMOUNT"));
                ObjFeltCreditNoteDetails.setAttribute("GR_NET_GR_AMOUNT", resultSetTemp.getString("GR_NET_GR_AMOUNT"));
                ObjFeltCreditNoteDetails.setAttribute("GR_HSN_NO", resultSetTemp.getString("GR_HSN_NO"));
                ObjFeltCreditNoteDetails.setAttribute("GR_CGST_PER", resultSetTemp.getString("GR_CGST_PER"));
                ObjFeltCreditNoteDetails.setAttribute("GR_CGST_AMOUNT", resultSetTemp.getString("GR_CGST_AMOUNT"));
                ObjFeltCreditNoteDetails.setAttribute("GR_SGST_PER", resultSetTemp.getString("GR_SGST_PER"));
                ObjFeltCreditNoteDetails.setAttribute("GR_SGST_AMOUNT", resultSetTemp.getString("GR_SGST_AMOUNT"));
                ObjFeltCreditNoteDetails.setAttribute("GR_IGST_PER", resultSetTemp.getString("GR_IGST_PER"));
                ObjFeltCreditNoteDetails.setAttribute("GR_IGST_AMOUNT", resultSetTemp.getString("GR_IGST_AMOUNT"));
                ObjFeltCreditNoteDetails.setAttribute("GR_GST_COMPOSITION_PER", resultSetTemp.getString("GR_GST_COMPOSITION_PER"));
                ObjFeltCreditNoteDetails.setAttribute("GR_GST_COMPOSITION_AMOUNT", resultSetTemp.getString("GR_GST_COMPOSITION_AMOUNT"));
                ObjFeltCreditNoteDetails.setAttribute("GR_TOTAL_AMT", resultSetTemp.getString("GR_TOTAL_AMT"));

                hmFeltCreditNoteDetails.put(Integer.toString(serialNoCounter), ObjFeltCreditNoteDetails);
            }
            resultSetTemp.close();
            statementTemp.close();
            resultSet.close();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    public static HashMap getHistoryList(String stringProductionDate, String productionDocumentNo) {
        HashMap hmHistoryList = new HashMap();
        Statement stTmp;
        ResultSet rsTmp;
        String stringProductionDate1 = EITLERPGLOBAL.formatDate(stringProductionDate);

        try {
            stTmp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);

            rsTmp = stTmp.executeQuery("SELECT * FROM STGSALES.D_STG_GOODS_RETURNS_HEADER_H WHERE GR_ID='" + productionDocumentNo + "'");

            while (rsTmp.next()) {
                clsSuitingGR_OLD ObjFeltCreditNote = new clsSuitingGR_OLD();

                ObjFeltCreditNote.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                ObjFeltCreditNote.setAttribute("UPDATED_BY", rsTmp.getInt("UPDATED_BY"));
                ObjFeltCreditNote.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                ObjFeltCreditNote.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                ObjFeltCreditNote.setAttribute("REJECTED_REMARKS", rsTmp.getString("REJECTED_REMARKS"));

                hmHistoryList.put(Integer.toString(hmHistoryList.size() + 1), ObjFeltCreditNote);
            }

            rsTmp.close();
            stTmp.close();
            return hmHistoryList;
        } catch (Exception e) {
            e.printStackTrace();
            return hmHistoryList;
        }
    }

    public boolean ShowHistory(String pProductionDate, String pDocNo) {
        Ready = false;
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery("SELECT DISTINCT REVISION_NO FROM STGSALES.D_STG_GOODS_RETURNS_HEADER_H WHERE GR_ID ='" + pDocNo + "'");
            Ready = true;
            historyAmendDate = pProductionDate;
            historyAmendID = pDocNo;
            HistoryView = true;
            MoveFirst();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    // Find Pending DOCUMENTS
    public static HashMap getPendingApprovals(int pUserID, int pOrder) {
        String strSQL = "";

        HashMap List = new HashMap();
        int Counter = 0;

        try {

            if (pOrder == EITLERPGLOBAL.OnRecivedDate) {
                strSQL = "SELECT DISTINCT GR_ID,CN_DATE,RECEIVED_DATE,PARA_DESC FROM STGSALES.D_STG_GOODS_RETURNS_HEADER, DINESHMILLS.D_COM_DOC_DATA  WHERE GR_ID=DOC_NO AND STATUS='W' AND MODULE_ID=757 AND CANCELED=0  AND USER_ID=" + pUserID + "  AND PARA_ID ='CREDIT_NOTE' AND PARA_CODE = CN_TYPE ORDER BY RECEIVED_DATE,GR_ID";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT GR_ID,CN_DATE,RECEIVED_DATE,PARA_DESC FROM STGSALES.D_STG_GOODS_RETURNS_HEADER, DINESHMILLS.D_COM_DOC_DATA  WHERE GR_ID=DOC_NO AND STATUS='W' AND MODULE_ID=757 AND CANCELED=0  AND USER_ID=" + pUserID + "  AND PARA_ID ='CREDIT_NOTE' AND PARA_CODE = CN_TYPE ORDER BY CN_DATE,GR_ID";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT GR_ID,CN_DATE,RECEIVED_DATE,PARA_DESC FROM STGSALES.D_STG_GOODS_RETURNS_HEADER, DINESHMILLS.D_COM_DOC_DATA  WHERE GR_ID=DOC_NO AND STATUS='W' AND MODULE_ID=757 AND CANCELED=0  AND USER_ID=" + pUserID + "  AND PARA_ID ='CREDIT_NOTE' AND PARA_CODE = CN_TYPE ORDER BY GR_ID";
            }

            ResultSet rsTmp = data.getConn().createStatement().executeQuery(strSQL);

            Counter = 0;
            while (rsTmp.next()) {

                Counter = Counter + 1;
                clsSuitingGR_OLD ObjDoc = new clsSuitingGR_OLD();

                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO", rsTmp.getString("GR_ID"));
                ObjDoc.setAttribute("DOC_DATE", rsTmp.getString("CN_DATE"));
                ObjDoc.setAttribute("RECEIVED_DATE", rsTmp.getString("RECEIVED_DATE"));
                ObjDoc.setAttribute("PARA_DESC", rsTmp.getString("PARA_DESC"));

                // ----------------- End of Header Fields ------------------------------------//
                List.put(Integer.toString(Counter), ObjDoc);
            }
            rsTmp.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return List;
    }

    public String getInvoiceDate(String pPieceNo, String Party_Code) {
        return data.getStringValueFromDB("SELECT INVOICE_DATE FROM DINESHMILLS.D_SAL_INVOICE_DETAIL WHERE PIECE_NO='" + pPieceNo + "' AND PARTY_CODE='" + Party_Code + "'");

    }

    public String getInvoiceNo(String pPieceNo, String Party_Code) {
        return data.getStringValueFromDB("SELECT INVOICE_NO FROM DINESHMILLS.D_SAL_INVOICE_DETAIL WHERE PIECE_NO='" + pPieceNo + "' AND PARTY_CODE='" + Party_Code + "'");
    }

    public String getProductCode(String pPieceNo, String Party_Code, String Invoice_No, String Invoice_Date) {
        System.out.println("SELECT QUALITY_NO FROM DINESHMILLS.D_SAL_INVOICE_DETAIL WHERE PIECE_NO='" + pPieceNo + "' AND PARTY_CODE='" + Party_Code + "'  AND INVOICE_NO='" + Invoice_No + "' AND INVOICE_DATE='" + Invoice_Date + "'");
        return data.getStringValueFromDB("SELECT QUALITY_NO FROM DINESHMILLS.D_SAL_INVOICE_DETAIL WHERE PIECE_NO='" + pPieceNo + "' AND PARTY_CODE='" + Party_Code + "' AND INVOICE_NO='" + Invoice_No + "' AND INVOICE_DATE='" + Invoice_Date + "'");
    }

    public String getAlphaInvoiceNo(String pPieceNo, String Party_Code, String Invoice_No, String Invoice_Date) {
        System.out.println("SELECT CONCAT(AGENT_ALPHA,AGENT_SR_NO) AS AGENT FROM DINESHMILLS.D_SAL_INVOICE_DETAIL WHERE PIECE_NO='" + pPieceNo + "' AND PARTY_CODE='" + Party_Code + "' AND INVOICE_NO='" + Invoice_No + "'  AND INVOICE_DATE='" + Invoice_Date + "'");
        return data.getStringValueFromDB("SELECT CONCAT(AGENT_ALPHA,AGENT_SR_NO) AS AGENT FROM DINESHMILLS.D_SAL_INVOICE_DETAIL WHERE PIECE_NO='" + pPieceNo + "' AND PARTY_CODE='" + Party_Code + "' AND INVOICE_NO='" + Invoice_No + "'  AND INVOICE_DATE='" + Invoice_Date + "'");
    }

    public String getShade(String pPieceNo, String Party_Code, String Invoice_No, String Invoice_Date) {
        return data.getStringValueFromDB("SELECT PATTERN_CODE FROM DINESHMILLS.D_SAL_INVOICE_DETAIL WHERE PIECE_NO='" + pPieceNo + "' AND PARTY_CODE='" + Party_Code + "' AND INVOICE_NO='" + Invoice_No + "' AND INVOICE_DATE='" + Invoice_Date + "'");
    }

    public String getUnitCode(String pPieceNo, String Party_Code, String Invoice_No, String Invoice_Date) {
        return data.getStringValueFromDB("SELECT UNIT_CODE FROM DINESHMILLS.D_SAL_INVOICE_DETAIL WHERE PIECE_NO='" + pPieceNo + "' AND PARTY_CODE='" + Party_Code + "' AND INVOICE_NO='" + Invoice_No + "' AND INVOICE_DATE='" + Invoice_Date + "'");
    }

    public String getGrossQty(String pPieceNo, String Party_Code, String Invoice_No, String Invoice_Date) {
        return data.getStringValueFromDB("SELECT GROSS_QTY FROM DINESHMILLS.D_SAL_INVOICE_DETAIL WHERE PIECE_NO='" + pPieceNo + "' AND PARTY_CODE='" + Party_Code + "' AND INVOICE_NO='" + Invoice_No + "' AND INVOICE_DATE='" + Invoice_Date + "'");
    }

    public String getFlag(String pPieceNo, String Party_Code, String Invoice_No, String Invoice_Date) {
        return data.getStringValueFromDB("SELECT FLAG_DEF_CODE FROM DINESHMILLS.D_SAL_INVOICE_DETAIL WHERE PIECE_NO='" + pPieceNo + "' AND PARTY_CODE='" + Party_Code + "' AND INVOICE_NO='" + Invoice_No + "' AND INVOICE_DATE='" + Invoice_Date + "'");
    }

    public String getPartyCode(String pPieceNo, String Party_Code, String Invoice_No, String Invoice_Date) {
        return data.getStringValueFromDB("SELECT PARTY_CODE FROM DINESHMILLS.D_SAL_INVOICE_DETAIL WHERE PIECE_NO='" + pPieceNo + "' AND PARTY_CODE='" + Party_Code + "' AND INVOICE_NO='" + Invoice_No + "' AND INVOICE_DATE='" + Invoice_Date + "'");
    }

    public String getNetQty(String pPieceNo, String Party_Code, String Invoice_No, String Invoice_Date) {
        return data.getStringValueFromDB("SELECT NET_QTY FROM DINESHMILLS.D_SAL_INVOICE_DETAIL WHERE PIECE_NO='" + pPieceNo + "' AND PARTY_CODE='" + Party_Code + "' AND INVOICE_NO='" + Invoice_No + "' AND INVOICE_DATE='" + Invoice_Date + "'");
    }

    public String getRate(String pPieceNo, String Party_Code, String Invoice_No, String Invoice_Date) {
        return data.getStringValueFromDB("SELECT RATE FROM DINESHMILLS.D_SAL_INVOICE_DETAIL WHERE PIECE_NO='" + pPieceNo + "' AND PARTY_CODE='" + Party_Code + "' AND INVOICE_NO='" + Invoice_No + "' AND INVOICE_DATE='" + Invoice_Date + "'");
    }

    public String getInvoiceDisc(String pPieceNo, String Party_Code, String Invoice_No, String Invoice_Date) {
        return data.getStringValueFromDB("SELECT DEF_DISC_PER FROM DINESHMILLS.D_SAL_INVOICE_DETAIL WHERE PIECE_NO='" + pPieceNo + "' AND PARTY_CODE='" + Party_Code + "' AND INVOICE_NO='" + Invoice_No + "' AND INVOICE_DATE='" + Invoice_Date + "'");
    }

    public String getAddDisc(String pPieceNo, String Party_Code, String Invoice_No, String Invoice_Date) {
        return data.getStringValueFromDB("SELECT ADDL_DISC_PER FROM DINESHMILLS.D_SAL_INVOICE_DETAIL WHERE PIECE_NO='" + pPieceNo + "' AND PARTY_CODE='" + Party_Code + "' AND INVOICE_NO='" + Invoice_No + "' AND INVOICE_DATE='" + Invoice_Date + "'");
    }

    public String getInvoiceAmt(String pPieceNo, String Party_Code, String Invoice_No, String Invoice_Date) {
        return data.getStringValueFromDB("SELECT NET_AMOUNT FROM DINESHMILLS.D_SAL_INVOICE_DETAIL WHERE PIECE_NO='" + pPieceNo + "'  AND PARTY_CODE='" + Party_Code + "' AND INVOICE_NO='" + Invoice_No + "' AND INVOICE_DATE='" + Invoice_Date + "'");
    }

    // it creates list of user name(felt) to be displayed
    public HashMap getUserNameList(int pHierarchyId, int pUserId, String pModule) {
        HashMap hmUserNameList = new HashMap();
        char category = ' ';
        try {
            Connection Conn = data.getConn();
            Statement stTmp = Conn.createStatement();
            ResultSet rsHierarchyRights = stTmp.executeQuery("SELECT CREATOR, APPROVER, FINAL_APPROVER FROM D_COM_HIERARCHY_RIGHTS WHERE COMPANY_ID=2 AND HIERARCHY_ID=" + pHierarchyId + " AND USER_ID=" + pUserId);
            while (rsHierarchyRights.next()) {
                boolean creator = rsHierarchyRights.getBoolean("CREATOR");
                boolean approver = rsHierarchyRights.getBoolean("APPROVER");
                boolean finalApprover = rsHierarchyRights.getBoolean("FINAL_APPROVER");
                if (approver) {
                    category = 'A';
                }
                if (creator) {
                    category = 'C';
                }
                if (finalApprover) {
                    category = 'F';
                }
            }

            int counter = 1;
            ComboData cData = new ComboData();
            cData.Code = 0;
            cData.Text = "Select User";
            hmUserNameList.put(new Integer(counter++), cData);
            ResultSet rsTmp = stTmp.executeQuery("SELECT USER_ID,USER_NAME FROM DINESHMILLS.`FELT_USER` WHERE USER_MODULE='" + pModule + "' AND USER_CATEG='" + category + "' ORDER BY USER_NAME");
            while (rsTmp.next()) {
                ComboData aData = new ComboData();
                aData.Code = rsTmp.getInt("USER_ID");
                aData.Text = rsTmp.getString("USER_NAME");
                hmUserNameList.put(new Integer(counter++), aData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hmUserNameList;
    }

    public static String getNextFreeNo(int pCompanyId, int pModuleID, int pFirstFree, boolean UpdateLastNo) {
        Connection tmpConn;
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";
        String strNewNo = "";
        int lnNewNo = 0;
        String Prefix = "";
        String Suffix = "";

        try {
            tmpConn = data.getConn();
            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //strSQL="SELECT LAST_USED_NO,PADDING_BY,NO_LENGTH FROM D_COM_FIRSTFREE WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND PREFIX_CHARS='"+pPrefix+"' AND SUFFIX_CHARS='"+pSuffix+"'";
            strSQL = "SELECT LAST_USED_NO,PADDING_BY,NO_LENGTH,PREFIX_CHARS,SUFFIX_CHARS FROM D_COM_FIRSTFREE WHERE COMPANY_ID=" + pCompanyId + " AND MODULE_ID=" + pModuleID + " AND FIRSTFREE_NO=" + pFirstFree;
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            if (rsTmp.getRow() > 0) {
                lnNewNo = rsTmp.getInt("LAST_USED_NO") + 1;
                strNewNo = EITLERPGLOBAL.Padding(Integer.toString(lnNewNo), rsTmp.getInt("NO_LENGTH"), rsTmp.getString("PADDING_BY"));
                Prefix = rsTmp.getString("PREFIX_CHARS");
                Suffix = rsTmp.getString("SUFFIX_CHARS");

                if (UpdateLastNo) {
                    //Update last no. in database
                    //data.Execute("UPDATE D_COM_FIRSTFREE SET LAST_USED_NO='"+strNewNo.trim()+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND PREFIX_CHARS='"+pPrefix+"' AND SUFFIX_CHARS='"+pSuffix+"'");
                    data.Execute("UPDATE D_COM_FIRSTFREE SET LAST_USED_NO='" + strNewNo.trim() + "',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + pCompanyId + " AND MODULE_ID=" + pModuleID + " AND FIRSTFREE_NO=" + pFirstFree);
                }

                strNewNo = Prefix + strNewNo + Suffix;

                //tmpConn.close();
                tmpStmt.close();
                rsTmp.close();

                return strNewNo;
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static String getParyName(int pCompanyID, String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            rsTmp = stTmp.executeQuery("SELECT PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE=" + pPartyID);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                PartyName = rsTmp.getString("PARTY_NAME");
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

        } catch (Exception e) {

        }
        return PartyName;
    }

    public static String getStation(int pCompanyID, String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Station = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            rsTmp = stTmp.executeQuery("SELECT CITY_ID FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE=" + pPartyID);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                Station = rsTmp.getString("CITY_ID");
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

        } catch (Exception e) {

        }
        return Station;
    }

    public boolean PostCNHSNGST(int CompanyID, String GRNo, String GRDate) {
        try {
            //Get Object            
            System.out.println("SSSS 0");
            System.out.println("GRN0  " + GRNo);
            System.out.println("GRDate  " + GRDate);

            String item = "";
            String ItemGroup = "";
            String GRNAccountCode = "";
            String HSNSAC_CODE = "";
            String ITEM_DESC = "";
            double CN_INVOICE_AMOUNT = 0;
            double CN_CGST = 0;
            double CN_SGST = 0;
            double CN_IGST = 0;
            double CN_RCM = 0;
            double CN_COMPOSITION = 0;
            double CN_GST_COMPENSATION_CESS = 0;
            double CN_NET_AMOUNT = 0;

            clsVoucher objVoucher = new clsVoucher();

            String PartyCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM STGSALES.D_STG_GOODS_RETURNS_HEADER WHERE GR_ID='" + GRNo + "' AND GR_DATE='" + GRDate + "';");            //String PartyMainCode=clsBook.getBookMainCode(CompanyID, objVoucher.getAttribute("BOOK_CODE").getString());
            //String PartyMainCode = "125019";
            String PartyMainCode = "210027";

            //String InvoiceNo = data.getStringValueFromDB("SELECT INVOICE_NO FROM D_INV_GRN_HEADER WHERE GRN_NO='" + GRNNo + "' AND INVOICE_NO<>'' ");
            //String InvoiceDate = data.getStringValueFromDB("SELECT INVOICE_DATE FROM D_INV_GRN_HEADER WHERE GRN_NO='" + GRNNo + "' AND INVOICE_NO<>'' ");
            String SelPrefix = "";
            String SelSuffix = "";
            int FFNo = 0;

            System.out.println("SSSS 1");
            //****** Prepare Voucher Object ********//

            //  setAttribute("FIN_HIERARCHY_ID",0);
            setAttribute("FIN_HIERARCHY_ID", 1796); //FOR TEST SERVER
            //  setAttribute("FIN_HIERARCHY_ID",1668); // FOR LIVE

            //(1) Select the Hierarchy
            //HashMap List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsVoucher.DebitNoteModuleID, "CHOOSE_HIERARCHY", "PUR_DN_AUTO", "");
            HashMap List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsVoucher.CreditNoteVoucherModuleID, "CHOOSE_HIERARCHY", "CN_AUTO", "");

            if (List.size() > 0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                int HierarchyID = UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                System.out.println("HID 1 1" + HierarchyID);
                setAttribute("FIN_HIERARCHY_ID", HierarchyID);
            }

            ResultSet rsVoucher = data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID=" + clsVoucher.CreditNoteVoucherModuleID);
            rsVoucher.first();

            if (rsVoucher.getRow() > 0) {
                SelPrefix = rsVoucher.getString("PREFIX_CHARS");
                SelSuffix = rsVoucher.getString("SUFFIX_CHARS");
                FFNo = rsVoucher.getInt("FIRSTFREE_NO");
            }

            int VoucherSrNo = 0;

            objVoucher = new clsVoucher();
            objVoucher.LoadDataEx(EITLERPGLOBAL.gCompanyID);

            objVoucher.setAttribute("PREFIX", SelPrefix);
            objVoucher.setAttribute("SUFFIX", SelSuffix);
            objVoucher.setAttribute("FFNO", FFNo);
            objVoucher.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            objVoucher.setAttribute("VOUCHER_DATE", EITLERPGLOBAL.formatDate(GRDate));

            //SUITING
            //objVoucher.setAttribute("BOOK_CODE", "45");
            objVoucher.setAttribute("BOOK_CODE", "13");
            //GRNAccountCode="405021";

            objVoucher.setAttribute("VOUCHER_TYPE", FinanceGlobal.TYPE_CREDIT_NOTE);
            objVoucher.setAttribute("CHEQUE_NO", "");
            objVoucher.setAttribute("CHEQUE_DATE", "0000-00-00");
            objVoucher.setAttribute("BANK_NAME", "");
            objVoucher.setAttribute("PO_NO", "");
            objVoucher.setAttribute("PO_DATE", "0000-00-00");
            objVoucher.setAttribute("INVOICE_NO", "");
            objVoucher.setAttribute("INVOICE_DATE", "0000-00-00");
            objVoucher.setAttribute("GRN_NO", "");
            objVoucher.setAttribute("GRN_DATE", "0000-00-00");
            objVoucher.setAttribute("ST_CATEGORY", "");
            objVoucher.setAttribute("MODULE_ID", clsVoucher.CreditNoteVoucherModuleID);
            objVoucher.setAttribute("REMARKS", "");

            objVoucher.setAttribute("HIERARCHY_ID", getAttribute("FIN_HIERARCHY_ID").getInt());
            int FirstUserID = data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID=" + getAttribute("FIN_HIERARCHY_ID").getInt() + " AND SR_NO=1");
            System.out.println("Hierarchy ID = " + getAttribute("FIN_HIERARCHY_ID").getInt());
            System.out.println("First user ID = " + FirstUserID);

            objVoucher.setAttribute("FROM", FirstUserID);
            objVoucher.setAttribute("TO", FirstUserID);
            objVoucher.setAttribute("FROM_REMARKS", "");
            objVoucher.setAttribute("APPROVAL_STATUS", "H"); //Final Approve

            objVoucher.colVoucherItems.clear();

            String strSQLHSN = "";
            strSQLHSN = "SELECT * FROM STGSALES.D_SAL_HSN_INVOICE_POSTING WHERE GR_ID='" + GRNo + "' AND APPROVED=1 AND CANCELLED=0 ";

            ResultSet rsHSNData = data.getResult(strSQLHSN);
            rsHSNData.first();

            if (rsHSNData.getRow() > 0) {
                while (!rsHSNData.isAfterLast()) {

                    HSNSAC_CODE = rsHSNData.getString("HSN_NO");

                    System.out.println("HSN CODE " + rsHSNData.getString("HSN_NO"));

                    CN_INVOICE_AMOUNT = rsHSNData.getDouble("NET_GR_AMOUNT");
                    CN_CGST = rsHSNData.getDouble("CGST_AMOUNT");
                    CN_SGST = rsHSNData.getDouble("SGST_AMOUNT");
                    CN_IGST = rsHSNData.getDouble("IGST_AMOUNT");
                    CN_RCM = rsHSNData.getDouble("RCM_AMOUNT");

                    //DB_NET_AMOUNT += DB_INVOICE_AMOUNT + DB_CGST + DB_SGST + DB_IGST + DB_RCM + DB_COMPOSITION + DB_GST_COMPENSATION_CESS;
                    CN_NET_AMOUNT += CN_INVOICE_AMOUNT + CN_CGST + CN_SGST + CN_IGST + CN_RCM;

//                    double IGSTPER = data.getDoubleValueFromDB("SELECT DISTINCT COLUMN_5_PER  FROM D_INV_GRN_DETAIL WHERE GRN_NO ='" + GRNo + "' ");
//                    double SGSTPER = data.getDoubleValueFromDB("SELECT DISTINCT COLUMN_4_PER  FROM D_INV_GRN_DETAIL WHERE GRN_NO ='" + GRNo + "' ");
//                    double CGSTPER = data.getDoubleValueFromDB("SELECT DISTINCT COLUMN_3_PER  FROM D_INV_GRN_DETAIL WHERE GRN_NO ='" + GRNo + "' ");
//
                    double IGSTPER = rsHSNData.getDouble("IGST_PER");
                    double SGSTPER = rsHSNData.getDouble("SGST_PER");
                    double CGSTPER = rsHSNData.getDouble("CGST_PER");
//
                    
                    String IGSTPayableCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM FINANCE.D_FIN_GL WHERE GST_CATG ='IGST' AND GST_PERCENT =" + EITLERPGLOBAL.round(IGSTPER, 0));
                    String SGSTPayableCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM FINANCE.D_FIN_GL WHERE GST_CATG ='SGST' AND GST_PERCENT =" + EITLERPGLOBAL.round(SGSTPER, 0));
                    String CGSTPayableCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM FINANCE.D_FIN_GL WHERE GST_CATG ='CGST' AND GST_PERCENT =" + EITLERPGLOBAL.round(CGSTPER, 0));
                    //String IGSTPayableCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM FINANCE.D_FIN_GL WHERE GST_CATG ='IGST' AND GST_PERCENT =5");
                    //String SGSTPayableCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM FINANCE.D_FIN_GL WHERE GST_CATG ='SGST' AND GST_PERCENT =2.5");
                    //String CGSTPayableCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM FINANCE.D_FIN_GL WHERE GST_CATG ='CGST' AND GST_PERCENT =2.5");
//                  
//                    
//                    System.out.println("IGST PER " + IGSTPER);
//                    System.out.println("SGST PER " + SGSTPER);
//                    System.out.println("CGST PER " + CGSTPER);
                    System.out.println("IGST PAYABLE ACCOUNT : " + IGSTPayableCode);
                    System.out.println("SGST PAYABLE ACCOUNT : " + SGSTPayableCode);
                    System.out.println("CGST PAYABLE ACCOUNT : " + CGSTPayableCode);

                    if (IGSTPayableCode.equals("")) {
                        IGSTPayableCode = "<IGST Payable Code>";
                    }
                    if (SGSTPayableCode.equals("")) {
                        SGSTPayableCode = "<SGST Payable Code>";
                    }
                    if (CGSTPayableCode.equals("")) {
                        CGSTPayableCode = "<CGST Payable Code>";
                    }

//                    System.out.println("INVOICE NO " + InvoiceNo);
//                    System.out.println("INVOICE DATE " + InvoiceDate);
//                    
                    System.out.println("CN INVOICE AMT = " + CN_INVOICE_AMOUNT);
                    System.out.println("CN CGST = " + CN_CGST);
                    System.out.println("CN SGST = " + CN_SGST);
                    System.out.println("CN IGST = " + CN_IGST);
                    //System.out.println("CN RCM = " + DB_RCM);
                    System.out.println("CN COMPOSITION = " + CN_COMPOSITION);
                    System.out.println("CN NET AMOUNT = " + CN_NET_AMOUNT);

                    //credit Entries
                    if (CN_INVOICE_AMOUNT > 0) {
                        VoucherSrNo++;
                        clsVoucherItem objVoucherItem = new clsVoucherItem();
                        objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                        objVoucherItem.setAttribute("EFFECT", "C");
                        objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                        objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "210027");
                        objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                        objVoucherItem.setAttribute("AMOUNT", CN_INVOICE_AMOUNT);
                        objVoucherItem.setAttribute("REMARKS", "");
                        objVoucherItem.setAttribute("PO_NO", "");
                        objVoucherItem.setAttribute("PO_DATE", "");
                        objVoucherItem.setAttribute("INVOICE_NO", "");
                        objVoucherItem.setAttribute("INVOICE_DATE", "");
                        objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                        objVoucherItem.setAttribute("GRN_NO", "");
                        objVoucherItem.setAttribute("GRN_DATE", "");
                        objVoucherItem.setAttribute("MODULE_ID", clsVoucher.CreditNoteVoucherModuleID);
                        objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                        objVoucherItem.setAttribute("HSN_SAC_CODE", HSNSAC_CODE);
                        objVoucherItem.setAttribute("ITEM_DESCRIPTION", ITEM_DESC);
                        objVoucherItem.setAttribute("GR_INVOICE_NO", rsHSNData.getString("INVOICE_NO"));
                        objVoucherItem.setAttribute("GR_INVOICE_DATE", EITLERPGLOBAL.formatDate(rsHSNData.getString("INVOICE_DATE")));
                        objVoucherItem.setAttribute("GR_NO", GRNo);
                        objVoucherItem.setAttribute("GR_DATE", GRDate);

                        objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
                    }

                    //(2) Hundi Charges
                    if (CN_RCM > 0) {

                        VoucherSrNo++;
                        clsVoucherItem objVoucherItem = new clsVoucherItem();
                        objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                        objVoucherItem.setAttribute("EFFECT", "C");
                        objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                        objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "450227");
                        objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                        objVoucherItem.setAttribute("AMOUNT", CN_RCM);
                        objVoucherItem.setAttribute("REMARKS", "RCM");
                        objVoucherItem.setAttribute("PO_NO", "");
                        objVoucherItem.setAttribute("PO_DATE", "");
                        objVoucherItem.setAttribute("INVOICE_NO", "");
                        objVoucherItem.setAttribute("INVOICE_DATE", "");
                        objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                        objVoucherItem.setAttribute("GRN_NO", "");
                        objVoucherItem.setAttribute("GRN_DATE", "");
                        objVoucherItem.setAttribute("MODULE_ID", clsVoucher.CreditNoteVoucherModuleID);
                        objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                        objVoucherItem.setAttribute("HSN_SAC_CODE", HSNSAC_CODE);
                        objVoucherItem.setAttribute("ITEM_DESCRIPTION", ITEM_DESC);
                        objVoucherItem.setAttribute("GR_INVOICE_NO", rsHSNData.getString("INVOICE_NO"));
                        objVoucherItem.setAttribute("GR_INVOICE_DATE", EITLERPGLOBAL.formatDate(rsHSNData.getString("INVOICE_DATE")));
                        objVoucherItem.setAttribute("GR_NO", GRNo);
                        objVoucherItem.setAttribute("GR_DATE", GRDate);
                        
                        objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);

                    }

                    //(3) Insurance Charges
//                    if (CN_COMPOSITION > 0) {
//
//                        VoucherSrNo++;
//                        clsVoucherItem objVoucherItem = new clsVoucherItem();
//                        objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
//                        objVoucherItem.setAttribute("EFFECT", "C");
//                        objVoucherItem.setAttribute("ACCOUNT_ID", 1);
//                        objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "427027");
//                        objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
//                        objVoucherItem.setAttribute("AMOUNT", DB_COMPOSITION);
//                        objVoucherItem.setAttribute("REMARKS", "COMPOSITION");
//                        objVoucherItem.setAttribute("PO_NO", PONo);
//                        objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
//                        objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
//                        objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
//                        objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
//                        objVoucherItem.setAttribute("GRN_NO", GRNNo);
//                        objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
//                        objVoucherItem.setAttribute("MODULE_ID", clsVoucher.DebitNoteModuleID);
//                        objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
//                        objVoucherItem.setAttribute("HSN_SAC_CODE", HSNSAC_CODE);
//                        objVoucherItem.setAttribute("ITEM_DESCRIPTION", ITEM_DESC);
//                        objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
//
//                    }

                    //(4) Bank Charges
//                    if (CN_GST_COMPENSATION_CESS > 0) {
//
//                        VoucherSrNo++;
//                        clsVoucherItem objVoucherItem = new clsVoucherItem();
//                        objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
//                        objVoucherItem.setAttribute("EFFECT", "C");
//                        objVoucherItem.setAttribute("ACCOUNT_ID", 1);
//                        objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "450038");
//                        objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
//                        objVoucherItem.setAttribute("AMOUNT", DB_GST_COMPENSATION_CESS);
//                        objVoucherItem.setAttribute("REMARKS", "GST Compensation cess");
//                        objVoucherItem.setAttribute("PO_NO", PONo);
//                        objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
//                        objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
//                        objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
//                        objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
//                        objVoucherItem.setAttribute("GRN_NO", GRNNo);
//                        objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
//                        objVoucherItem.setAttribute("MODULE_ID", clsVoucher.DebitNoteModuleID);
//                        objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
//                        objVoucherItem.setAttribute("HSN_SAC_CODE", HSNSAC_CODE);
//                        objVoucherItem.setAttribute("ITEM_DESCRIPTION", ITEM_DESC);
//                        objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
//
//                    }

                    //(10) GST Amount
                    if (CN_CGST > 0) {

                        VoucherSrNo++;
                        clsVoucherItem objVoucherItem = new clsVoucherItem();
                        objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                        objVoucherItem.setAttribute("EFFECT", "C");
                        objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                        objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", CGSTPayableCode);
                        objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                        objVoucherItem.setAttribute("AMOUNT", CN_CGST);
                        objVoucherItem.setAttribute("REMARKS", "CGST");
                        objVoucherItem.setAttribute("PO_NO", "");
                        objVoucherItem.setAttribute("PO_DATE", "");
                        objVoucherItem.setAttribute("INVOICE_NO", "");
                        objVoucherItem.setAttribute("INVOICE_DATE", "");
                        objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                        objVoucherItem.setAttribute("GRN_NO", "");
                        objVoucherItem.setAttribute("GRN_DATE", "");
                        objVoucherItem.setAttribute("MODULE_ID", clsVoucher.CreditNoteVoucherModuleID);
                        objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                        objVoucherItem.setAttribute("HSN_SAC_CODE", HSNSAC_CODE);
                        objVoucherItem.setAttribute("ITEM_DESCRIPTION", ITEM_DESC);
                        objVoucherItem.setAttribute("GR_INVOICE_NO", rsHSNData.getString("INVOICE_NO"));
                        objVoucherItem.setAttribute("GR_INVOICE_DATE", EITLERPGLOBAL.formatDate(rsHSNData.getString("INVOICE_DATE")));
                        objVoucherItem.setAttribute("GR_NO", GRNo);
                        objVoucherItem.setAttribute("GR_DATE", GRDate);
                        
                        objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
                    }

                    if (CN_SGST > 0) {
                        VoucherSrNo++;
                        clsVoucherItem objVoucherItem = new clsVoucherItem();
                        objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                        objVoucherItem.setAttribute("EFFECT", "C");
                        objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                        objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", SGSTPayableCode);
                        objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                        objVoucherItem.setAttribute("AMOUNT", CN_SGST);
                        objVoucherItem.setAttribute("REMARKS", "SGST");
                        objVoucherItem.setAttribute("PO_NO", "");
                        objVoucherItem.setAttribute("PO_DATE", "");
                        objVoucherItem.setAttribute("INVOICE_NO", "");
                        objVoucherItem.setAttribute("INVOICE_DATE", "");
                        objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                        objVoucherItem.setAttribute("GRN_NO", "");
                        objVoucherItem.setAttribute("GRN_DATE", "");
                        objVoucherItem.setAttribute("MODULE_ID", clsVoucher.CreditNoteVoucherModuleID);
                        objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                        objVoucherItem.setAttribute("HSN_SAC_CODE", HSNSAC_CODE);
                        objVoucherItem.setAttribute("ITEM_DESCRIPTION", ITEM_DESC);
                        objVoucherItem.setAttribute("GR_INVOICE_NO", rsHSNData.getString("INVOICE_NO"));
                        objVoucherItem.setAttribute("GR_INVOICE_DATE", EITLERPGLOBAL.formatDate(rsHSNData.getString("INVOICE_DATE")));
                        objVoucherItem.setAttribute("GR_NO", GRNo);
                        objVoucherItem.setAttribute("GR_DATE", GRDate);
                        
                        objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
                    }

                    if (CN_IGST > 0) {
                        VoucherSrNo++;
                        clsVoucherItem objVoucherItem = new clsVoucherItem();
                        objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                        objVoucherItem.setAttribute("EFFECT", "C");
                        objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                        objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", IGSTPayableCode);
                        objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                        objVoucherItem.setAttribute("AMOUNT", CN_IGST);
                        objVoucherItem.setAttribute("REMARKS", "IGST");
                        objVoucherItem.setAttribute("PO_NO", "");
                        objVoucherItem.setAttribute("PO_DATE", "");
                        objVoucherItem.setAttribute("INVOICE_NO", "");
                        objVoucherItem.setAttribute("INVOICE_DATE", "");
                        objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                        objVoucherItem.setAttribute("GRN_NO", "");
                        objVoucherItem.setAttribute("GRN_DATE", "");
                        objVoucherItem.setAttribute("MODULE_ID", clsVoucher.CreditNoteVoucherModuleID);
                        objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                        objVoucherItem.setAttribute("HSN_SAC_CODE", HSNSAC_CODE);
                        objVoucherItem.setAttribute("ITEM_DESCRIPTION", ITEM_DESC);
                        objVoucherItem.setAttribute("GR_INVOICE_NO", rsHSNData.getString("INVOICE_NO"));
                        objVoucherItem.setAttribute("GR_INVOICE_DATE", EITLERPGLOBAL.formatDate(rsHSNData.getString("INVOICE_DATE")));
                        objVoucherItem.setAttribute("GR_NO", GRNo);
                        objVoucherItem.setAttribute("GR_DATE", GRDate);                        
                        objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
                    }

                    rsHSNData.next();
                }
            }

            //.getAttribute("DB_GROSS_AMT").getVal()
            // double DB_INVOICE_AMOUNT=objGRNGen.getAttribute("DB_INVOICE_AMT").getVal();
            VoucherSrNo++;
            clsVoucherItem objVoucherItem = new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT", "D");
            objVoucherItem.setAttribute("ACCOUNT_ID", 1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", PartyMainCode);
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", PartyCode);
            objVoucherItem.setAttribute("AMOUNT", CN_NET_AMOUNT);
            objVoucherItem.setAttribute("REMARKS", "");
            objVoucherItem.setAttribute("PO_NO", "");
            objVoucherItem.setAttribute("PO_NO", "");
            objVoucherItem.setAttribute("PO_DATE", "");
            objVoucherItem.setAttribute("INVOICE_NO", "");
            objVoucherItem.setAttribute("INVOICE_DATE", "");
            objVoucherItem.setAttribute("GRN_NO", "");
            objVoucherItem.setAttribute("GRN_DATE", "");
            objVoucherItem.setAttribute("MODULE_ID", clsVoucher.CreditNoteVoucherModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
            objVoucherItem.setAttribute("GR_INVOICE_NO", rsHSNData.getString("INVOICE_NO"));
            objVoucherItem.setAttribute("GR_INVOICE_DATE", EITLERPGLOBAL.formatDate(rsHSNData.getString("INVOICE_DATE")));
            objVoucherItem.setAttribute("GR_NO", GRNo);
            objVoucherItem.setAttribute("GR_DATE", GRDate);

            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);

            objVoucher.DoNotValidateAccounts = true;

            if (objVoucher.Insert()) {

                //SJ Posted. Automatically adjust advance receipt amount
                String DNNo = objVoucher.getAttribute("VOUCHER_NO").getString();
                data.Execute("UPDATE D_FIN_VOUCHER_HEADER SET APPROVED=0 AND APPROVED_DATE=CURDATE() WHERE VOUCHER_NO='" + DNNo + "'", FinanceGlobal.FinURL);
                System.out.println("Purchase DebitNote  Posted " + DNNo);
                return true;
            } else {
                LastError = objVoucher.LastError;
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
