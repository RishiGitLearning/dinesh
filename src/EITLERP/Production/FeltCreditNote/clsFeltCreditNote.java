/*
 * clsFeltCreditNote.java
 *
 * Created on March 12, 2013, 3:10 PM
 */
package EITLERP.Production.FeltCreditNote;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import EITLERP.EITLERPGLOBAL;
import EITLERP.Variant;
import EITLERP.data;
import EITLERP.ComboData;
import EITLERP.Production.clsFeltProductionApprovalFlow;

/**
 *
 * @author Rishi Raj Neekhra
 * @version
 */
public class clsFeltCreditNote extends clsFeltCNAutoPosting {

    public String LastError = "";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet, rsHeader;
    //private ResultSet rsResultSet,

    public HashMap props;
    public boolean Ready = false;
    //Felt Order Collection
    public HashMap hmFeltCreditNoteDetails;

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
    public clsFeltCreditNote() {
        LastError = "";
        props = new HashMap();
        props.put("CN_DATE", new Variant(""));
        props.put("CN_ID", new Variant(""));
        props.put("CN_TYPE", new Variant(""));
        props.put("CN_FROM_DATE", new Variant(""));
        props.put("CN_TO_DATE", new Variant(""));
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
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_CN_TEMP_HEADER ORDER BY CN_DATE");
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
        ResultSet resultSetTemp, resultSetHistory, rsHeader;
        Statement statementTemp, statementHistory, stHeader;
        try {
            // Felt Order Updation data connection
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_CN_TEMP_DETAIL WHERE CN_ID='' order by sr_no*1");

            // Felt order Updation data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_CN_TEMP_DETAIL_H WHERE CN_ID=''");

            stHeader = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHeader = stHeader.executeQuery("SELECT * FROM  PRODUCTION.FELT_CN_TEMP_HEADER_H WHERE CN_ID=''");

            String cnid = getAttribute("CN_ID").getString().substring(0, 2);

            if (cnid.matches("F6")) {
                setAttribute("CN_ID", getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 735, 183, true));
            }
            if (cnid.matches("DF")) {
                setAttribute("CN_ID", getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 735, 184, true));
            }
            if (cnid.matches("OM")) {
                setAttribute("CN_ID", getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 735, 185, true));
            }
            if (cnid.matches("YD")) {
                setAttribute("CN_ID", getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 735, 186, true));
            }
            if (cnid.matches("PD")) {
                setAttribute("CN_ID", getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 735, 187, true));
            }
            if (cnid.matches("CM")) {
                setAttribute("CN_ID", getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 735, 192, true));
            }
            if (cnid.matches("GR")) {
                setAttribute("CN_ID", getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 735, 193, true));
            }
            if (cnid.matches("IC")) {
                setAttribute("CN_ID", getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 735, 194, true));
            }
            //23-05-2020
            if (cnid.matches("DH")) {
                setAttribute("CN_ID", getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 735, 345, true));
            }
            ////
            //05-04-2021
            if (cnid.matches("SP")) {
                setAttribute("CN_ID", getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 735, 373, true));
            }
            ////
            //07-04-2021
            if (cnid.matches("SD")) {
                setAttribute("CN_ID", getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 735, 374, true));
            }
            ////

            //setAttribute("CN_ID",getNextFreeNo(2,735,true));
            resultSet.first();
            resultSet.moveToInsertRow();

            resultSet.updateString("CN_ID", getAttribute("CN_ID").getString());
            resultSet.updateString("CN_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CN_DATE").getString()));
            resultSet.updateString("CN_TYPE", getAttribute("CN_TYPE").getString());
            resultSet.updateString("CN_FROM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CN_FROM_DATE").getString()));
            resultSet.updateString("CN_TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CN_TO_DATE").getString()));
            resultSet.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            resultSet.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            resultSet.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
            resultSet.updateString("MODIFIED_DATE", getAttribute("MODIFIED_DATE").getString());
            resultSet.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            resultSet.updateBoolean("APPROVED", false);
            resultSet.updateString("APPROVED_DATE", "");
            resultSet.updateBoolean("CANCELED", false);
            resultSet.updateBoolean("REJECTED", false);
            resultSet.updateString("REJECTED_DATE", "");
            resultSet.updateBoolean("CHANGED", true);
            resultSet.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

            resultSet.insertRow();

            rsHeader.moveToInsertRow();

            rsHeader.updateInt("REVISION_NO", 1);
            rsHeader.updateInt("UPDATED_BY", (int) getAttribute("UPDATED_BY").getVal());
            rsHeader.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            rsHeader.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeader.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
            rsHeader.updateString("CN_ID", getAttribute("CN_ID").getString());
            rsHeader.updateString("CN_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CN_DATE").getString()));
            rsHeader.updateString("CN_TYPE", getAttribute("CN_TYPE").getString());
            rsHeader.updateString("CN_FROM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CN_FROM_DATE").getString()));
            rsHeader.updateString("CN_TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CN_TO_DATE").getString()));
            rsHeader.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsHeader.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            rsHeader.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeader.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
            rsHeader.updateString("MODIFIED_DATE", getAttribute("MODIFIED_DATE").getString());
            rsHeader.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsHeader.updateBoolean("APPROVED", false);
            rsHeader.updateString("APPROVED_DATE", "");
            rsHeader.updateBoolean("CANCELED", false);
            rsHeader.updateBoolean("REJECTED", false);
            rsHeader.updateString("REJECTED_DATE", "");
            rsHeader.updateBoolean("CHANGED", true);
            rsHeader.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

            rsHeader.insertRow();

            //Now Insert records into FELT_CN_TEMP_HEADER & History tables
            for (int i = 1; i <= hmFeltCreditNoteDetails.size(); i++) {
                clsFeltCreditNoteDetails ObjFeltCreditNoteDetails = (clsFeltCreditNoteDetails) hmFeltCreditNoteDetails.get(Integer.toString(i));

                //Insert records into Felt order Amendment table
                resultSetTemp.moveToInsertRow();

                resultSetTemp.updateString("CN_ID", getAttribute("CN_ID").getString());
                resultSetTemp.updateString("CN_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CN_DATE").getString()));
                resultSetTemp.updateString("CN_TYPE", getAttribute("CN_TYPE").getString());
                resultSetTemp.updateString("CN_FROM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CN_FROM_DATE").getString()));
                resultSetTemp.updateString("CN_TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CN_TO_DATE").getString()));
                resultSetTemp.updateString("SR_NO", ObjFeltCreditNoteDetails.getAttribute("SR_NO").getString());
                resultSetTemp.updateString("CN_INVOICE_NO", ObjFeltCreditNoteDetails.getAttribute("CN_INVOICE_NO").getString());
                resultSetTemp.updateString("CN_INVOICE_DATE", ObjFeltCreditNoteDetails.getAttribute("CN_INVOICE_DATE").getString());
                resultSetTemp.updateString("CN_PARTY_CODE", ObjFeltCreditNoteDetails.getAttribute("CN_PARTY_CODE").getString());
                resultSetTemp.updateString("CN_PARTY_NAME", ObjFeltCreditNoteDetails.getAttribute("CN_PARTY_NAME").getString());
                resultSetTemp.updateString("CN_INV_PIECE_NO", ObjFeltCreditNoteDetails.getAttribute("CN_INV_PIECE_NO").getString());
                resultSetTemp.updateString("CN_INV_PRODUCT_CODE", ObjFeltCreditNoteDetails.getAttribute("CN_INV_PRODUCT_CODE").getString());
                resultSetTemp.updateString("CN_PRODUCT_GRUP", ObjFeltCreditNoteDetails.getAttribute("CN_PRODUCT_GRUP").getString());
                resultSetTemp.updateString("CN_INV_WI_SQMTR", ObjFeltCreditNoteDetails.getAttribute("CN_INV_WI_SQMTR").getString());
                resultSetTemp.updateString("CN_GROSS_VALUE", ObjFeltCreditNoteDetails.getAttribute("CN_GROSS_VALUE").getString());
                resultSetTemp.updateString("CN_DISC_BILL", ObjFeltCreditNoteDetails.getAttribute("CN_DISC_BILL").getString());
                resultSetTemp.updateString("CN_YEAR_END_DISC", ObjFeltCreditNoteDetails.getAttribute("CN_YEAR_END_DISC").getString());
                resultSetTemp.updateString("CN_YEAR_END_DISC_RS", ObjFeltCreditNoteDetails.getAttribute("CN_YEAR_END_DISC_RS").getString());
                resultSetTemp.updateString("CN_UNADJUSTED_DISC", ObjFeltCreditNoteDetails.getAttribute("CN_UNADJUSTED_DISC").getString());
                resultSetTemp.updateString("CN_UNADJUSTED_RS", ObjFeltCreditNoteDetails.getAttribute("CN_UNADJUSTED_RS").getString());
                resultSetTemp.updateString("CN_NET_BASIC", ObjFeltCreditNoteDetails.getAttribute("CN_NET_BASIC").getString());
                resultSetTemp.updateString("CN_OEM_VALUE", ObjFeltCreditNoteDetails.getAttribute("CN_OEM_VALUE").getString());
                resultSetTemp.updateString("CN_OEM", ObjFeltCreditNoteDetails.getAttribute("CN_OEM").getString());
                resultSetTemp.updateString("CN_INVOICE_AMT", ObjFeltCreditNoteDetails.getAttribute("CN_INVOICE_AMT").getString());
                resultSetTemp.updateString("CN_RATE", ObjFeltCreditNoteDetails.getAttribute("CN_RATE").getString());
                resultSetTemp.updateString("CN_BASIC_VALUE", ObjFeltCreditNoteDetails.getAttribute("CN_BASIC_VALUE").getString());
                resultSetTemp.updateString("CN_RECD_AMT", ObjFeltCreditNoteDetails.getAttribute("CN_RECD_AMT").getString());
                resultSetTemp.updateString("CN_EXT1", ObjFeltCreditNoteDetails.getAttribute("CN_EXT1").getString());
                resultSetTemp.updateString("CN_EXT2", ObjFeltCreditNoteDetails.getAttribute("CN_EXT2").getString());
                resultSetTemp.updateString("CN_EXT3", ObjFeltCreditNoteDetails.getAttribute("CN_EXT3").getString());
                resultSetTemp.updateString("TOTAL_NET_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("TOTAL_NET_AMOUNT").getString());
                resultSetTemp.updateString("VALUE_DATE", ObjFeltCreditNoteDetails.getAttribute("VALUE_DATE").getString());
                resultSetTemp.updateString("AMOUNT", ObjFeltCreditNoteDetails.getAttribute("AMOUNT").getString());
                resultSetTemp.updateString("VOUCHER", ObjFeltCreditNoteDetails.getAttribute("VOUCHER").getString());
                resultSetTemp.updateString("F6", ObjFeltCreditNoteDetails.getAttribute("F6").getString());
                resultSetTemp.updateString("SALES_REMARKS", ObjFeltCreditNoteDetails.getAttribute("SALES_REMARKS").getString());
                resultSetTemp.updateString("AUDIT_REMARKS", ObjFeltCreditNoteDetails.getAttribute("AUDIT_REMARKS").getString());
                resultSetTemp.updateString("INSURANCE", ObjFeltCreditNoteDetails.getAttribute("INSURANCE").getString());
                resultSetTemp.updateString("CST", ObjFeltCreditNoteDetails.getAttribute("CST").getString());
                resultSetTemp.updateString("EXCISE_DUTY", ObjFeltCreditNoteDetails.getAttribute("EXCISE_DUTY").getString());
                resultSetTemp.updateString("COM_YEAR_DISC", ObjFeltCreditNoteDetails.getAttribute("COM_YEAR_DISC").getString());
                resultSetTemp.updateString("COM_YEAR_AMT", ObjFeltCreditNoteDetails.getAttribute("COM_YEAR_AMT").getString());
                resultSetTemp.updateString("COM_UDJ_DISC", ObjFeltCreditNoteDetails.getAttribute("COM_UDJ_DISC").getString());
                resultSetTemp.updateString("COM_UDJ_AMT", ObjFeltCreditNoteDetails.getAttribute("COM_UDJ_AMT").getString());
                resultSetTemp.updateString("COM_OEM_AMT", ObjFeltCreditNoteDetails.getAttribute("COM_OEM_AMT").getString());
                resultSetTemp.updateString("COMPENSATION_AMT", ObjFeltCreditNoteDetails.getAttribute("COMPENSATION_AMT").getString());
                resultSetTemp.updateString("GOODS_RETURN_AMT", ObjFeltCreditNoteDetails.getAttribute("GOODS_RETURN_AMT").getString());
                resultSetTemp.updateString("INSURANCE_CLAIM_AMT", ObjFeltCreditNoteDetails.getAttribute("INSURANCE_CLAIM_AMT").getString());
                //resultSetTemp.updateString("INVOICE_AMOUNT",ObjFeltCreditNoteDetails.getAttribute("INVOICE_AMOUNT").getString());
                resultSetTemp.updateString("DAYS1", ObjFeltCreditNoteDetails.getAttribute("DAYS1").getString());
                resultSetTemp.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                resultSetTemp.updateString("CREATED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetTemp.updateInt("MODIFIED_BY", 0);
                resultSetTemp.updateString("MODIFIED_DATE", "");
                resultSetTemp.updateBoolean("APPROVED", false);
                resultSetTemp.updateString("APPROVED_DATE", "");
                resultSetTemp.updateBoolean("REJECTED", false);
                resultSetTemp.updateString("REJECTED_DATE", "");
                resultSetTemp.updateInt("CANCELED", 0);
                resultSetTemp.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                resultSetTemp.updateInt("CHANGED", 1);
                resultSetTemp.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                resultSetTemp.insertRow();

                //Insert records into Felt Order Amendment History Table
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO", 1);
                resultSetHistory.updateString("CN_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CN_DATE").getString()));
                resultSetHistory.updateString("CN_ID", getAttribute("CN_ID").getString());
                resultSetHistory.updateString("CN_TYPE", getAttribute("CN_TYPE").getString());
                resultSetHistory.updateString("CN_FROM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CN_FROM_DATE").getString()));
                resultSetHistory.updateString("CN_TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CN_TO_DATE").getString()));
                resultSetHistory.updateString("SR_NO", ObjFeltCreditNoteDetails.getAttribute("SR_NO").getString());
                resultSetHistory.updateString("CN_INVOICE_NO", ObjFeltCreditNoteDetails.getAttribute("CN_INVOICE_NO").getString());
                resultSetHistory.updateString("CN_INVOICE_DATE", ObjFeltCreditNoteDetails.getAttribute("CN_INVOICE_DATE").getString());
                resultSetHistory.updateString("CN_PARTY_CODE", ObjFeltCreditNoteDetails.getAttribute("CN_PARTY_CODE").getString());
                resultSetHistory.updateString("CN_PARTY_NAME", ObjFeltCreditNoteDetails.getAttribute("CN_PARTY_NAME").getString());
                resultSetHistory.updateString("CN_INV_PIECE_NO", ObjFeltCreditNoteDetails.getAttribute("CN_INV_PIECE_NO").getString());
                resultSetHistory.updateString("CN_INV_PRODUCT_CODE", ObjFeltCreditNoteDetails.getAttribute("CN_INV_PRODUCT_CODE").getString());
                resultSetHistory.updateString("CN_PRODUCT_GRUP", ObjFeltCreditNoteDetails.getAttribute("CN_PRODUCT_GRUP").getString());
                resultSetHistory.updateString("CN_INV_WI_SQMTR", ObjFeltCreditNoteDetails.getAttribute("CN_INV_WI_SQMTR").getString());
                resultSetHistory.updateString("CN_GROSS_VALUE", ObjFeltCreditNoteDetails.getAttribute("CN_GROSS_VALUE").getString());
                resultSetHistory.updateString("CN_DISC_BILL", ObjFeltCreditNoteDetails.getAttribute("CN_DISC_BILL").getString());
                resultSetHistory.updateString("CN_YEAR_END_DISC", ObjFeltCreditNoteDetails.getAttribute("CN_YEAR_END_DISC").getString());
                resultSetHistory.updateString("CN_YEAR_END_DISC_RS", ObjFeltCreditNoteDetails.getAttribute("CN_YEAR_END_DISC_RS").getString());
                resultSetHistory.updateString("CN_UNADJUSTED_DISC", ObjFeltCreditNoteDetails.getAttribute("CN_UNADJUSTED_DISC").getString());
                resultSetHistory.updateString("CN_UNADJUSTED_RS", ObjFeltCreditNoteDetails.getAttribute("CN_UNADJUSTED_RS").getString());
                resultSetHistory.updateString("CN_NET_BASIC", ObjFeltCreditNoteDetails.getAttribute("CN_NET_BASIC").getString());
                resultSetHistory.updateString("CN_OEM_VALUE", ObjFeltCreditNoteDetails.getAttribute("CN_OEM_VALUE").getString());
                resultSetHistory.updateString("CN_OEM", ObjFeltCreditNoteDetails.getAttribute("CN_OEM").getString());
                resultSetHistory.updateString("CN_INVOICE_AMT", ObjFeltCreditNoteDetails.getAttribute("CN_INVOICE_AMT").getString());
                resultSetHistory.updateString("CN_RATE", ObjFeltCreditNoteDetails.getAttribute("CN_RATE").getString());
                resultSetHistory.updateString("CN_BASIC_VALUE", ObjFeltCreditNoteDetails.getAttribute("CN_BASIC_VALUE").getString());
                resultSetHistory.updateString("CN_RECD_AMT", ObjFeltCreditNoteDetails.getAttribute("CN_RECD_AMT").getString());
                resultSetHistory.updateString("CN_EXT1", ObjFeltCreditNoteDetails.getAttribute("CN_EXT1").getString());
                resultSetHistory.updateString("CN_EXT2", ObjFeltCreditNoteDetails.getAttribute("CN_EXT2").getString());
                resultSetHistory.updateString("CN_EXT3", ObjFeltCreditNoteDetails.getAttribute("CN_EXT3").getString());
                resultSetHistory.updateString("TOTAL_NET_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("TOTAL_NET_AMOUNT").getString());
                resultSetHistory.updateString("VALUE_DATE", ObjFeltCreditNoteDetails.getAttribute("VALUE_DATE").getString());
                resultSetHistory.updateString("AMOUNT", ObjFeltCreditNoteDetails.getAttribute("AMOUNT").getString());
                resultSetHistory.updateString("VOUCHER", ObjFeltCreditNoteDetails.getAttribute("VOUCHER").getString());
                resultSetHistory.updateString("F6", ObjFeltCreditNoteDetails.getAttribute("F6").getString());
                resultSetHistory.updateString("SALES_REMARKS", ObjFeltCreditNoteDetails.getAttribute("SALES_REMARKS").getString());
                resultSetHistory.updateString("AUDIT_REMARKS", ObjFeltCreditNoteDetails.getAttribute("AUDIT_REMARKS").getString());
                resultSetHistory.updateString("INSURANCE", ObjFeltCreditNoteDetails.getAttribute("INSURANCE").getString());
                resultSetHistory.updateString("CST", ObjFeltCreditNoteDetails.getAttribute("CST").getString());
                resultSetHistory.updateString("EXCISE_DUTY", ObjFeltCreditNoteDetails.getAttribute("EXCISE_DUTY").getString());
                resultSetHistory.updateString("COM_YEAR_DISC", ObjFeltCreditNoteDetails.getAttribute("COM_YEAR_DISC").getString());
                resultSetHistory.updateString("COM_YEAR_AMT", ObjFeltCreditNoteDetails.getAttribute("COM_YEAR_AMT").getString());
                resultSetHistory.updateString("COM_UDJ_DISC", ObjFeltCreditNoteDetails.getAttribute("COM_UDJ_DISC").getString());
                resultSetHistory.updateString("COM_UDJ_AMT", ObjFeltCreditNoteDetails.getAttribute("COM_UDJ_AMT").getString());
                resultSetHistory.updateString("COM_OEM_AMT", ObjFeltCreditNoteDetails.getAttribute("COM_OEM_AMT").getString());
                resultSetHistory.updateString("COMPENSATION_AMT", ObjFeltCreditNoteDetails.getAttribute("COMPENSATION_AMT").getString());
                resultSetHistory.updateString("GOODS_RETURN_AMT", ObjFeltCreditNoteDetails.getAttribute("GOODS_RETURN_AMT").getString());
                resultSetHistory.updateString("INSURANCE_CLAIM_AMT", ObjFeltCreditNoteDetails.getAttribute("INSURANCE_CLAIM_AMT").getString());
                resultSetHistory.updateString("DAYS1", ObjFeltCreditNoteDetails.getAttribute("DAYS1").getString());
                resultSetHistory.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                resultSetHistory.updateString("CREATED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateInt("UPDATED_BY", (int) getAttribute("UPDATED_BY").getVal());
                resultSetHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
                resultSetHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
                resultSetHistory.updateInt("MODIFIED_BY", 0);
                resultSetHistory.updateString("MODIFIED_DATE", "");
                resultSetHistory.updateBoolean("APPROVED", false);
                resultSetHistory.updateString("APPROVED_DATE", "");
                resultSetHistory.updateBoolean("REJECTED", false);
                resultSetHistory.updateString("REJECTED_DATE", "");
                resultSetHistory.updateInt("CANCELED", 0);
                resultSetHistory.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                resultSetHistory.updateInt("CHANGED", 1);
                resultSetHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                resultSetHistory.insertRow();

            }

            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = 735; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("CN_ID").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.FELT_CN_TEMP_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator = true;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("REJECTED_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "CN_ID";

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
            if (ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {

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

        ResultSet resultSetTemp, resultSetHistory, rsHeader;;
        Statement statementTemp, statementHistory, stHeader;;
        int revisionNo = 1;
        try {
            // Production data connection
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_CN_TEMP_DETAIL WHERE CN_ID='' order by sr_no*1");

            // Production data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //Get the Maximum Revision No in History Table.
            resultSetHistory = statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_CN_TEMP_DETAIL_H WHERE CN_ID='" + getAttribute("CN_ID").getString() + "' order by sr_no*1");
            resultSetHistory.first();
            if (resultSetHistory.getRow() > 0) {
                revisionNo = resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from production data table before insert
            statementHistory.execute("DELETE FROM PRODUCTION.FELT_CN_TEMP_DETAIL WHERE CN_ID='" + getAttribute("CN_ID").getString() + "'");

            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_CN_TEMP_DETAIL_H WHERE CN_ID='" + getAttribute("CN_ID").getString() + "' order by sr_no*1");

            stHeader = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHeader = stHeader.executeQuery("SELECT * FROM  PRODUCTION.FELT_CN_TEMP_HEADER_H WHERE CN_ID=''");

            //Now Update records into FELT_CN_TEMP_HEADER tables
            resultSet.updateString("CN_ID", getAttribute("CN_ID").getString());
            resultSet.updateString("CN_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CN_DATE").getString()));
            resultSet.updateString("CN_TYPE", getAttribute("CN_TYPE").getString());
            resultSet.updateString("CN_FROM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CN_FROM_DATE").getString()));
            resultSet.updateString("CN_TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CN_TO_DATE").getString()));
            resultSet.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            resultSet.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            resultSet.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
            resultSet.updateString("MODIFIED_DATE", getAttribute("MODIFIED_DATE").getString());
            resultSet.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            resultSet.updateBoolean("APPROVED", false);
            resultSet.updateString("APPROVED_DATE", "");
            resultSet.updateBoolean("CANCELED", false);
            resultSet.updateBoolean("REJECTED", false);
            resultSet.updateString("REJECTED_DATE", "");
            resultSet.updateBoolean("CHANGED", true);
            resultSet.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

            resultSet.updateRow();

            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_CN_TEMP_DETAIL_H WHERE CN_ID='" + (String) getAttribute("CN_ID").getObj() + "'");
            RevNo++;
            String RevDocNo = (String) getAttribute("CN_ID").getObj();

            rsHeader.moveToInsertRow();

            rsHeader.updateInt("REVISION_NO", RevNo);
            rsHeader.updateInt("UPDATED_BY", (int) getAttribute("MODIFIED_BY").getVal());
            rsHeader.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            rsHeader.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeader.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
            rsHeader.updateString("CN_ID", getAttribute("CN_ID").getString());
            rsHeader.updateString("CN_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CN_DATE").getString()));
            rsHeader.updateString("CN_TYPE", getAttribute("CN_TYPE").getString());
            rsHeader.updateString("CN_FROM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CN_FROM_DATE").getString()));
            rsHeader.updateString("CN_TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CN_TO_DATE").getString()));
            rsHeader.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsHeader.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            rsHeader.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeader.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
            rsHeader.updateString("MODIFIED_DATE", getAttribute("MODIFIED_DATE").getString());
            rsHeader.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsHeader.updateBoolean("APPROVED", false);
            rsHeader.updateString("APPROVED_DATE", "");
            rsHeader.updateBoolean("CANCELED", false);
            rsHeader.updateBoolean("REJECTED", false);
            rsHeader.updateString("REJECTED_DATE", "");
            rsHeader.updateBoolean("CHANGED", true);
            rsHeader.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

            rsHeader.insertRow();

            for (int i = 1; i <= hmFeltCreditNoteDetails.size(); i++) {
                clsFeltCreditNoteDetails ObjFeltCreditNoteDetails = (clsFeltCreditNoteDetails) hmFeltCreditNoteDetails.get(Integer.toString(i));

                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateString("CN_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CN_DATE").getString()));
                resultSetTemp.updateString("CN_ID", getAttribute("CN_ID").getString());
                resultSetTemp.updateString("CN_TYPE", getAttribute("CN_TYPE").getString());
                resultSetTemp.updateString("CN_FROM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CN_FROM_DATE").getString()));
                resultSetTemp.updateString("CN_TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CN_TO_DATE").getString()));
                resultSetTemp.updateString("SR_NO", ObjFeltCreditNoteDetails.getAttribute("SR_NO").getString());
                resultSetTemp.updateString("CN_INVOICE_NO", ObjFeltCreditNoteDetails.getAttribute("CN_INVOICE_NO").getString());
                resultSetTemp.updateString("CN_INVOICE_DATE", ObjFeltCreditNoteDetails.getAttribute("CN_INVOICE_DATE").getString());
                resultSetTemp.updateString("CN_PARTY_CODE", ObjFeltCreditNoteDetails.getAttribute("CN_PARTY_CODE").getString());
                resultSetTemp.updateString("CN_PARTY_NAME", ObjFeltCreditNoteDetails.getAttribute("CN_PARTY_NAME").getString());
                resultSetTemp.updateString("CN_INV_PIECE_NO", ObjFeltCreditNoteDetails.getAttribute("CN_INV_PIECE_NO").getString());
                resultSetTemp.updateString("CN_INV_PRODUCT_CODE", ObjFeltCreditNoteDetails.getAttribute("CN_INV_PRODUCT_CODE").getString());
                resultSetTemp.updateString("CN_PRODUCT_GRUP", ObjFeltCreditNoteDetails.getAttribute("CN_PRODUCT_GRUP").getString());
                resultSetTemp.updateString("CN_INV_WI_SQMTR", ObjFeltCreditNoteDetails.getAttribute("CN_INV_WI_SQMTR").getString());
                resultSetTemp.updateString("CN_GROSS_VALUE", ObjFeltCreditNoteDetails.getAttribute("CN_GROSS_VALUE").getString());
                resultSetTemp.updateString("CN_DISC_BILL", ObjFeltCreditNoteDetails.getAttribute("CN_DISC_BILL").getString());
                resultSetTemp.updateString("CN_YEAR_END_DISC", ObjFeltCreditNoteDetails.getAttribute("CN_YEAR_END_DISC").getString());
                resultSetTemp.updateString("CN_YEAR_END_DISC_RS", ObjFeltCreditNoteDetails.getAttribute("CN_YEAR_END_DISC_RS").getString());
                resultSetTemp.updateString("CN_UNADJUSTED_DISC", ObjFeltCreditNoteDetails.getAttribute("CN_UNADJUSTED_DISC").getString());
                resultSetTemp.updateString("CN_UNADJUSTED_RS", ObjFeltCreditNoteDetails.getAttribute("CN_UNADJUSTED_RS").getString());
                resultSetTemp.updateString("CN_NET_BASIC", ObjFeltCreditNoteDetails.getAttribute("CN_NET_BASIC").getString());
                resultSetTemp.updateString("CN_OEM_VALUE", ObjFeltCreditNoteDetails.getAttribute("CN_OEM_VALUE").getString());
                resultSetTemp.updateString("CN_OEM", ObjFeltCreditNoteDetails.getAttribute("CN_OEM").getString());
                resultSetTemp.updateString("CN_INVOICE_AMT", ObjFeltCreditNoteDetails.getAttribute("CN_INVOICE_AMT").getString());
                resultSetTemp.updateString("CN_RATE", ObjFeltCreditNoteDetails.getAttribute("CN_RATE").getString());
                resultSetTemp.updateString("CN_BASIC_VALUE", ObjFeltCreditNoteDetails.getAttribute("CN_BASIC_VALUE").getString());
                resultSetTemp.updateString("CN_RECD_AMT", ObjFeltCreditNoteDetails.getAttribute("CN_RECD_AMT").getString());
                resultSetTemp.updateString("CN_EXT1", ObjFeltCreditNoteDetails.getAttribute("CN_EXT1").getString());
                resultSetTemp.updateString("CN_EXT2", ObjFeltCreditNoteDetails.getAttribute("CN_EXT2").getString());
                resultSetTemp.updateString("CN_EXT3", ObjFeltCreditNoteDetails.getAttribute("CN_EXT3").getString());
                resultSetTemp.updateString("TOTAL_NET_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("TOTAL_NET_AMOUNT").getString());
                resultSetTemp.updateString("VALUE_DATE", ObjFeltCreditNoteDetails.getAttribute("VALUE_DATE").getString());
                resultSetTemp.updateString("AMOUNT", ObjFeltCreditNoteDetails.getAttribute("AMOUNT").getString());
                resultSetTemp.updateString("VOUCHER", ObjFeltCreditNoteDetails.getAttribute("VOUCHER").getString());
                resultSetTemp.updateString("F6", ObjFeltCreditNoteDetails.getAttribute("F6").getString());
                resultSetTemp.updateString("SALES_REMARKS", ObjFeltCreditNoteDetails.getAttribute("SALES_REMARKS").getString());
                resultSetTemp.updateString("AUDIT_REMARKS", ObjFeltCreditNoteDetails.getAttribute("AUDIT_REMARKS").getString());
                resultSetTemp.updateString("INSURANCE", ObjFeltCreditNoteDetails.getAttribute("INSURANCE").getString());
                resultSetTemp.updateString("CST", ObjFeltCreditNoteDetails.getAttribute("CST").getString());
                resultSetTemp.updateString("EXCISE_DUTY", ObjFeltCreditNoteDetails.getAttribute("EXCISE_DUTY").getString());
                resultSetTemp.updateString("COM_YEAR_DISC", ObjFeltCreditNoteDetails.getAttribute("COM_YEAR_DISC").getString());
                resultSetTemp.updateString("COM_YEAR_AMT", ObjFeltCreditNoteDetails.getAttribute("COM_YEAR_AMT").getString());
                resultSetTemp.updateString("COM_UDJ_DISC", ObjFeltCreditNoteDetails.getAttribute("COM_UDJ_DISC").getString());
                resultSetTemp.updateString("COM_UDJ_AMT", ObjFeltCreditNoteDetails.getAttribute("COM_UDJ_AMT").getString());
                resultSetTemp.updateString("COM_OEM_AMT", ObjFeltCreditNoteDetails.getAttribute("COM_OEM_AMT").getString());
                resultSetTemp.updateString("COMPENSATION_AMT", ObjFeltCreditNoteDetails.getAttribute("COMPENSATION_AMT").getString());
                resultSetTemp.updateString("GOODS_RETURN_AMT", ObjFeltCreditNoteDetails.getAttribute("GOODS_RETURN_AMT").getString());
                resultSetTemp.updateString("INSURANCE_CLAIM_AMT", ObjFeltCreditNoteDetails.getAttribute("INSURANCE_CLAIM_AMT").getString());
                resultSetTemp.updateString("DAYS1", ObjFeltCreditNoteDetails.getAttribute("DAYS1").getString());
                resultSetTemp.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                resultSetTemp.updateString("CREATED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetTemp.updateInt("MODIFIED_BY", 0);
                resultSetTemp.updateString("MODIFIED_DATE", "");
                resultSetTemp.updateBoolean("APPROVED", false);
                resultSetTemp.updateString("APPROVED_DATE", "");
                resultSetTemp.updateBoolean("REJECTED", false);
                resultSetTemp.updateString("REJECTED_DATE", "");
                resultSetTemp.updateInt("CANCELED", 0);
                resultSetTemp.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                resultSetTemp.updateInt("CHANGED", 1);
                resultSetTemp.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                resultSetTemp.insertRow();

                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO", RevNo);
                resultSetHistory.updateString("CN_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CN_DATE").getString()));
                resultSetHistory.updateString("CN_ID", getAttribute("CN_ID").getString());
                resultSetHistory.updateString("CN_TYPE", getAttribute("CN_TYPE").getString());
                resultSetHistory.updateString("CN_FROM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CN_FROM_DATE").getString()));
                resultSetHistory.updateString("CN_TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("CN_TO_DATE").getString()));
                resultSetHistory.updateString("SR_NO", ObjFeltCreditNoteDetails.getAttribute("SR_NO").getString());
                resultSetHistory.updateString("CN_INVOICE_NO", ObjFeltCreditNoteDetails.getAttribute("CN_INVOICE_NO").getString());
                resultSetHistory.updateString("CN_INVOICE_DATE", ObjFeltCreditNoteDetails.getAttribute("CN_INVOICE_DATE").getString());
                resultSetHistory.updateString("CN_PARTY_CODE", ObjFeltCreditNoteDetails.getAttribute("CN_PARTY_CODE").getString());
                resultSetHistory.updateString("CN_PARTY_NAME", ObjFeltCreditNoteDetails.getAttribute("CN_PARTY_NAME").getString());
                resultSetHistory.updateString("CN_INV_PIECE_NO", ObjFeltCreditNoteDetails.getAttribute("CN_INV_PIECE_NO").getString());
                resultSetHistory.updateString("CN_INV_PRODUCT_CODE", ObjFeltCreditNoteDetails.getAttribute("CN_INV_PRODUCT_CODE").getString());
                resultSetHistory.updateString("CN_PRODUCT_GRUP", ObjFeltCreditNoteDetails.getAttribute("CN_PRODUCT_GRUP").getString());
                resultSetHistory.updateString("CN_INV_WI_SQMTR", ObjFeltCreditNoteDetails.getAttribute("CN_INV_WI_SQMTR").getString());
                resultSetHistory.updateString("CN_GROSS_VALUE", ObjFeltCreditNoteDetails.getAttribute("CN_GROSS_VALUE").getString());
                resultSetHistory.updateString("CN_DISC_BILL", ObjFeltCreditNoteDetails.getAttribute("CN_DISC_BILL").getString());
                resultSetHistory.updateString("CN_YEAR_END_DISC", ObjFeltCreditNoteDetails.getAttribute("CN_YEAR_END_DISC").getString());
                resultSetHistory.updateString("CN_YEAR_END_DISC_RS", ObjFeltCreditNoteDetails.getAttribute("CN_YEAR_END_DISC_RS").getString());
                resultSetHistory.updateString("CN_UNADJUSTED_DISC", ObjFeltCreditNoteDetails.getAttribute("CN_UNADJUSTED_DISC").getString());
                resultSetHistory.updateString("CN_UNADJUSTED_RS", ObjFeltCreditNoteDetails.getAttribute("CN_UNADJUSTED_RS").getString());
                resultSetHistory.updateString("CN_NET_BASIC", ObjFeltCreditNoteDetails.getAttribute("CN_NET_BASIC").getString());
                resultSetHistory.updateString("CN_OEM_VALUE", ObjFeltCreditNoteDetails.getAttribute("CN_OEM_VALUE").getString());
                resultSetHistory.updateString("CN_OEM", ObjFeltCreditNoteDetails.getAttribute("CN_OEM").getString());
                resultSetHistory.updateString("CN_INVOICE_AMT", ObjFeltCreditNoteDetails.getAttribute("CN_INVOICE_AMT").getString());
                resultSetHistory.updateString("CN_RATE", ObjFeltCreditNoteDetails.getAttribute("CN_RATE").getString());
                resultSetHistory.updateString("CN_BASIC_VALUE", ObjFeltCreditNoteDetails.getAttribute("CN_BASIC_VALUE").getString());
                resultSetHistory.updateString("CN_RECD_AMT", ObjFeltCreditNoteDetails.getAttribute("CN_RECD_AMT").getString());
                resultSetHistory.updateString("CN_EXT1", ObjFeltCreditNoteDetails.getAttribute("CN_EXT1").getString());
                resultSetHistory.updateString("CN_EXT2", ObjFeltCreditNoteDetails.getAttribute("CN_EXT2").getString());
                resultSetHistory.updateString("CN_EXT3", ObjFeltCreditNoteDetails.getAttribute("CN_EXT3").getString());
                resultSetHistory.updateString("TOTAL_NET_AMOUNT", ObjFeltCreditNoteDetails.getAttribute("TOTAL_NET_AMOUNT").getString());
                resultSetHistory.updateString("VALUE_DATE", ObjFeltCreditNoteDetails.getAttribute("VALUE_DATE").getString());
                resultSetHistory.updateString("AMOUNT", ObjFeltCreditNoteDetails.getAttribute("AMOUNT").getString());
                resultSetHistory.updateString("VOUCHER", ObjFeltCreditNoteDetails.getAttribute("VOUCHER").getString());
                resultSetHistory.updateString("F6", ObjFeltCreditNoteDetails.getAttribute("F6").getString());
                resultSetHistory.updateString("SALES_REMARKS", ObjFeltCreditNoteDetails.getAttribute("SALES_REMARKS").getString());
                resultSetHistory.updateString("AUDIT_REMARKS", ObjFeltCreditNoteDetails.getAttribute("AUDIT_REMARKS").getString());
                resultSetHistory.updateString("INSURANCE", ObjFeltCreditNoteDetails.getAttribute("INSURANCE").getString());
                resultSetHistory.updateString("CST", ObjFeltCreditNoteDetails.getAttribute("CST").getString());
                resultSetHistory.updateString("EXCISE_DUTY", ObjFeltCreditNoteDetails.getAttribute("EXCISE_DUTY").getString());
                resultSetHistory.updateString("COM_YEAR_DISC", ObjFeltCreditNoteDetails.getAttribute("COM_YEAR_DISC").getString());
                resultSetHistory.updateString("COM_YEAR_AMT", ObjFeltCreditNoteDetails.getAttribute("COM_YEAR_AMT").getString());
                resultSetHistory.updateString("COM_UDJ_DISC", ObjFeltCreditNoteDetails.getAttribute("COM_UDJ_DISC").getString());
                resultSetHistory.updateString("COM_UDJ_AMT", ObjFeltCreditNoteDetails.getAttribute("COM_UDJ_AMT").getString());
                resultSetHistory.updateString("COM_OEM_AMT", ObjFeltCreditNoteDetails.getAttribute("COM_OEM_AMT").getString());
                resultSetHistory.updateString("COMPENSATION_AMT", ObjFeltCreditNoteDetails.getAttribute("COMPENSATION_AMT").getString());
                resultSetHistory.updateString("GOODS_RETURN_AMT", ObjFeltCreditNoteDetails.getAttribute("GOODS_RETURN_AMT").getString());
                resultSetHistory.updateString("INSURANCE_CLAIM_AMT", ObjFeltCreditNoteDetails.getAttribute("INSURANCE_CLAIM_AMT").getString());
                resultSetHistory.updateString("DAYS1", ObjFeltCreditNoteDetails.getAttribute("DAYS1").getString());
                resultSetHistory.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                resultSetHistory.updateString("CREATED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateInt("UPDATED_BY", (int) getAttribute("UPDATED_BY").getVal());
                resultSetHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
                resultSetHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
                resultSetHistory.updateInt("MODIFIED_BY", 0);
                resultSetHistory.updateString("MODIFIED_DATE", "");
                resultSetHistory.updateBoolean("APPROVED", false);
                resultSetHistory.updateString("APPROVED_DATE", "");
                resultSetHistory.updateBoolean("REJECTED", false);
                resultSetHistory.updateString("REJECTED_DATE", "");
                resultSetHistory.updateInt("CANCELED", 0);
                resultSetHistory.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                resultSetHistory.updateInt("CHANGED", 1);
                resultSetHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                resultSetHistory.insertRow();
            }

            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = 735; //Felt Order Updation Module
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("CN_ID").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.FELT_CN_TEMP_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator = false;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("REJECTED_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "CN_ID";

            if (getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFeltProductionApprovalFlow.To = (int) getAttribute("SEND_DOC_TO").getVal();
                ObjFeltProductionApprovalFlow.ExplicitSendTo = true;
            }

            //==== Handling Rejected Documents ==========//
            boolean IsRejected = getAttribute("REJECTED").getBool();

            if (IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.FELT_CN_TEMP_HEADER SET REJECTED=0,CHANGED=1 WHERE CN_ID ='" + getAttribute("CN_ID").getString() + "'");
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
            if (ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {

                if (getAttribute("CN_ID").getString().startsWith("GR")) {
                    GenerateGRCN(getAttribute("CN_ID").getString());
                    GRCNVoucherPosting(getAttribute("CN_ID").getString());
                }

                if (getAttribute("CN_ID").getString().startsWith("CM")) {
                    GenerateCompCN(getAttribute("CN_ID").getString());
                    CompCNVoucherPosting(getAttribute("CN_ID").getString());
                }

                if (getAttribute("CN_ID").getString().startsWith("OM")) {
                    GenerateOEMCN(getAttribute("CN_ID").getString());
                    OEMCNVoucherPosting(getAttribute("CN_ID").getString());
                }

                if (getAttribute("CN_ID").getString().startsWith("IC")) {
                    GenerateInsClmCN(getAttribute("CN_ID").getString());
                    ICCNVoucherPosting(getAttribute("CN_ID").getString());
                }

                ObjFeltProductionApprovalFlow.finalApproved = false;
            }

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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_CN_TEMP_HEADER WHERE  FELT_AMEND_ID='" + documentNo + "' AND APPROVED=" + 1;
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=735 AND USER_ID=" + userID + " AND DOC_NO='" + documentNo + "' AND STATUS='W'";
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();

                if (rsTmp.getInt("COUNT") > 0) {
                    strSQL = "DELETE FROM PRODUCTION.FELT_CN_TEMP_HEADER WHERE "
                            + " AND CN_DATE= '" + EITLERPGLOBAL.formatDateDB(stringProductionDate) + "'"
                            + " AND CN_ID='" + documentNo + "'";
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
    public boolean IsEditable(String orderupdDocumentNo, int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";
        try {
            if (HistoryView) {
                return false;
            }
            tmpStmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //First check that document is approved or not
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_CN_TEMP_HEADER WHERE CN_ID='" + orderupdDocumentNo + "' AND APPROVED=1";
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=735 AND USER_ID=" + userID + " AND DOC_NO='" + orderupdDocumentNo + "' AND STATUS='W'";
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
            // String strSql = "SELECT DISTINCT AMEND_DATE FROM PRODUCTION.FELT_CN_TEMP_HEADER WHERE  " + stringFindQuery + " ORDER BY CN_DATE";
            String strSql = "SELECT * FROM PRODUCTION.FELT_CN_TEMP_HEADER WHERE  " + stringFindQuery + "";
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery(strSql);
            if (resultSet.first()) {
                Ready = true;
                MoveLast();
                return true;
            } else {
                return false;
            }
//            if(!resultSet.first()) {
//                LoadData();
//                Ready=true;
//                return false;
//            }
//            else {
//                Ready=true;
//                MoveLast();
//                return true;
//            }
        } catch (Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean setData() {
        ResultSet resultSetTemp;
        Statement statementTemp;
        int serialNoCounter = 0;
        int RevNo = 0;

        try {

            setAttribute("REVISION_NO", 0);

            setAttribute("CN_DATE", resultSet.getDate("CN_DATE"));
            setAttribute("CN_ID", resultSet.getString("CN_ID"));
            setAttribute("CN_TYPE", resultSet.getString("CN_TYPE"));
            setAttribute("CN_FROM_DATE", resultSet.getDate("CN_FROM_DATE"));
            setAttribute("CN_TO_DATE", resultSet.getDate("CN_TO_DATE"));
            setAttribute("REJECTED_REMARKS", resultSet.getString("REJECTED_REMARKS"));

            hmFeltCreditNoteDetails.clear();

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_CN_TEMP_DETAIL WHERE CN_ID='" + resultSet.getString("CN_ID") + "'  ORDER BY CN_ID,SR_NO*1");

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

                clsFeltCreditNoteDetails ObjFeltCreditNoteDetails = new clsFeltCreditNoteDetails();

                ObjFeltCreditNoteDetails.setAttribute("SR_NO", resultSetTemp.getString("SR_NO"));
                ObjFeltCreditNoteDetails.setAttribute("CN_INV_WI_SQMTR", resultSetTemp.getString("CN_INV_WI_SQMTR"));
                ObjFeltCreditNoteDetails.setAttribute("CN_INVOICE_NO", resultSetTemp.getString("CN_INVOICE_NO"));
                ObjFeltCreditNoteDetails.setAttribute("CN_INVOICE_DATE", resultSetTemp.getString("CN_INVOICE_DATE"));
                ObjFeltCreditNoteDetails.setAttribute("CN_PARTY_CODE", resultSetTemp.getString("CN_PARTY_CODE"));
                ObjFeltCreditNoteDetails.setAttribute("CN_PARTY_NAME", resultSetTemp.getString("CN_PARTY_NAME"));
                ObjFeltCreditNoteDetails.setAttribute("CN_INV_PIECE_NO", resultSetTemp.getString("CN_INV_PIECE_NO"));
                ObjFeltCreditNoteDetails.setAttribute("CN_INV_PRODUCT_CODE", resultSetTemp.getString("CN_INV_PRODUCT_CODE"));
                ObjFeltCreditNoteDetails.setAttribute("CN_PRODUCT_GRUP", resultSetTemp.getString("CN_PRODUCT_GRUP"));
                ObjFeltCreditNoteDetails.setAttribute("CN_GROSS_VALUE", resultSetTemp.getString("CN_GROSS_VALUE"));
                ObjFeltCreditNoteDetails.setAttribute("CN_DISC_BILL", resultSetTemp.getString("CN_DISC_BILL"));
                ObjFeltCreditNoteDetails.setAttribute("CN_YEAR_END_DISC", resultSetTemp.getString("CN_YEAR_END_DISC"));
                ObjFeltCreditNoteDetails.setAttribute("CN_YEAR_END_DISC_RS", resultSetTemp.getString("CN_YEAR_END_DISC_RS"));
                ObjFeltCreditNoteDetails.setAttribute("CN_UNADJUSTED_DISC", resultSetTemp.getString("CN_UNADJUSTED_DISC"));
                ObjFeltCreditNoteDetails.setAttribute("CN_UNADJUSTED_RS", resultSetTemp.getString("CN_UNADJUSTED_RS"));
                ObjFeltCreditNoteDetails.setAttribute("CN_NET_BASIC", resultSetTemp.getString("CN_NET_BASIC"));
                ObjFeltCreditNoteDetails.setAttribute("CN_OEM_VALUE", resultSetTemp.getString("CN_OEM_VALUE"));
                ObjFeltCreditNoteDetails.setAttribute("CN_OEM", resultSetTemp.getString("CN_OEM"));
                ObjFeltCreditNoteDetails.setAttribute("CN_INVOICE_AMT", resultSetTemp.getString("CN_INVOICE_AMT"));
                ObjFeltCreditNoteDetails.setAttribute("CN_RATE", resultSetTemp.getString("CN_RATE"));
                ObjFeltCreditNoteDetails.setAttribute("CN_BASIC_VALUE", resultSetTemp.getString("CN_BASIC_VALUE"));
                ObjFeltCreditNoteDetails.setAttribute("CN_RECD_AMT", resultSetTemp.getString("CN_RECD_AMT"));
                ObjFeltCreditNoteDetails.setAttribute("CN_EXT1", resultSetTemp.getString("CN_EXT1"));
                ObjFeltCreditNoteDetails.setAttribute("CN_EXT2", resultSetTemp.getString("CN_EXT2"));
                ObjFeltCreditNoteDetails.setAttribute("CN_EXT3", resultSetTemp.getString("CN_EXT3"));
                ObjFeltCreditNoteDetails.setAttribute("TOTAL_NET_AMOUNT", resultSetTemp.getString("TOTAL_NET_AMOUNT"));
                ObjFeltCreditNoteDetails.setAttribute("VALUE_DATE", resultSetTemp.getString("VALUE_DATE"));
                ObjFeltCreditNoteDetails.setAttribute("AMOUNT", resultSetTemp.getString("AMOUNT"));
                ObjFeltCreditNoteDetails.setAttribute("VOUCHER", resultSetTemp.getString("VOUCHER"));
                ObjFeltCreditNoteDetails.setAttribute("F6", resultSetTemp.getString("F6"));
                ObjFeltCreditNoteDetails.setAttribute("SALES_REMARKS", resultSetTemp.getString("SALES_REMARKS"));
                ObjFeltCreditNoteDetails.setAttribute("AUDIT_REMARKS", resultSetTemp.getString("AUDIT_REMARKS"));
                ObjFeltCreditNoteDetails.setAttribute("INSURANCE", resultSetTemp.getString("INSURANCE"));
                ObjFeltCreditNoteDetails.setAttribute("CST", resultSetTemp.getString("CST"));
                ObjFeltCreditNoteDetails.setAttribute("EXCISE_DUTY", resultSetTemp.getString("EXCISE_DUTY"));
                ObjFeltCreditNoteDetails.setAttribute("COM_YEAR_DISC", resultSetTemp.getString("COM_YEAR_DISC"));
                ObjFeltCreditNoteDetails.setAttribute("COM_YEAR_AMT", resultSetTemp.getString("COM_YEAR_AMT"));
                ObjFeltCreditNoteDetails.setAttribute("COM_UDJ_DISC", resultSetTemp.getString("COM_UDJ_DISC"));
                ObjFeltCreditNoteDetails.setAttribute("COM_UDJ_AMT", resultSetTemp.getString("COM_UDJ_AMT"));
                ObjFeltCreditNoteDetails.setAttribute("COM_OEM_AMT", resultSetTemp.getString("COM_OEM_AMT"));
                ObjFeltCreditNoteDetails.setAttribute("COMPENSATION_AMT", resultSetTemp.getString("COMPENSATION_AMT"));
                ObjFeltCreditNoteDetails.setAttribute("GOODS_RETURN_AMT", resultSetTemp.getString("GOODS_RETURN_AMT"));
                ObjFeltCreditNoteDetails.setAttribute("INSURANCE_CLAIM_AMT", resultSetTemp.getString("INSURANCE_CLAIM_AMT"));
                ObjFeltCreditNoteDetails.setAttribute("DAYS1", resultSetTemp.getString("DAYS1"));
                hmFeltCreditNoteDetails.put(Integer.toString(serialNoCounter), ObjFeltCreditNoteDetails);
            }
            resultSetTemp.close();
            statementTemp.close();
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
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_CN_TEMP_HEADER_H WHERE CN_ID='" + pDocNo + "'");

            while (resultSetTemp.next()) {
                serialNoCounter++;
                setAttribute("CN_ID", resultSetTemp.getString("CN_ID"));
                setAttribute("UPDATED_BY", resultSetTemp.getInt("UPDATED_BY"));
                setAttribute("ENTRY_DATE", resultSetTemp.getString("ENTRY_DATE"));
                setAttribute("HIERARCHY_ID", resultSetTemp.getInt("HIERARCHY_ID"));

                clsFeltCreditNoteDetails ObjFeltCreditNoteDetails = new clsFeltCreditNoteDetails();

                ObjFeltCreditNoteDetails.setAttribute("SR_NO", resultSetTemp.getString("SR_NO"));
                ObjFeltCreditNoteDetails.setAttribute("CN_INV_PIECE_NO", resultSetTemp.getString("CN_INV_PIECE_NO"));
                ObjFeltCreditNoteDetails.setAttribute("CN_INVOICE_NO", resultSetTemp.getString("CN_INVOICE_NO"));
                ObjFeltCreditNoteDetails.setAttribute("CN_INVOICE_DATE", resultSetTemp.getString("CN_INVOICE_DATE"));
                ObjFeltCreditNoteDetails.setAttribute("CN_PARTY_CODE", resultSetTemp.getString("CN_PARTY_CODE"));
                ObjFeltCreditNoteDetails.setAttribute("CN_PARTY_NAME", resultSetTemp.getString("CN_PARTY_NAME"));
                ObjFeltCreditNoteDetails.setAttribute("CN_INV_PRODUCT_CODE", resultSetTemp.getString("CN_INV_PRODUCT_CODE"));
                ObjFeltCreditNoteDetails.setAttribute("CN_GROSS_VALUE", resultSetTemp.getString("CN_GROSS_VALUE"));
                ObjFeltCreditNoteDetails.setAttribute("CN_DISC_BILL", resultSetTemp.getString("CN_DISC_BILL"));
                ObjFeltCreditNoteDetails.setAttribute("CN_YEAR_END_DISC", resultSetTemp.getString("CN_YEAR_END_DISC"));
                ObjFeltCreditNoteDetails.setAttribute("CN_YEAR_END_DISC_RS", resultSetTemp.getString("CN_YEAR_END_DISC_RS"));
                ObjFeltCreditNoteDetails.setAttribute("CN_UNADJUSTED_DISC", resultSetTemp.getString("CN_UNADJUSTED_DISC"));
                ObjFeltCreditNoteDetails.setAttribute("CN_UNADJUSTED_RS", resultSetTemp.getString("CN_UNADJUSTED_RS"));
                ObjFeltCreditNoteDetails.setAttribute("CN_NET_BASIC", resultSetTemp.getString("CN_NET_BASIC"));
                ObjFeltCreditNoteDetails.setAttribute("CN_OEM_VALUE", resultSetTemp.getString("CN_OEM_VALUE"));
                ObjFeltCreditNoteDetails.setAttribute("CN_OEM", resultSetTemp.getString("CN_OEM"));
                ObjFeltCreditNoteDetails.setAttribute("CN_INV_WI_SQMTR", resultSetTemp.getString("CN_INV_WI_SQMTR"));
                ObjFeltCreditNoteDetails.setAttribute("CN_INVOICE_AMT", resultSetTemp.getString("CN_INVOICE_AMT"));
                ObjFeltCreditNoteDetails.setAttribute("CN_RATE", resultSetTemp.getString("CN_RATE"));
                //ObjFeltCreditNoteDetails.setAttribute("CN_YEAR_END_DISC",resultSetTemp.getString("CN_YEAR_END_DISC"));
                ObjFeltCreditNoteDetails.setAttribute("CN_BASIC_VALUE", resultSetTemp.getString("CN_BASIC_VALUE"));
                // ObjFeltCreditNoteDetails.setAttribute("CN_NET_BASIC",resultSetTemp.getString("CN_NET_BASIC"));
                ObjFeltCreditNoteDetails.setAttribute("CN_RECD_AMT", resultSetTemp.getString("CN_RECD_AMT"));
                ObjFeltCreditNoteDetails.setAttribute("CN_EXT1", resultSetTemp.getString("CN_EXT1"));
                ObjFeltCreditNoteDetails.setAttribute("CN_EXT2", resultSetTemp.getString("CN_EXT2"));
                ObjFeltCreditNoteDetails.setAttribute("CN_EXT3", resultSetTemp.getString("CN_EXT3"));

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

            rsTmp = stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_CN_TEMP_HEADER_H WHERE CN_ID='" + productionDocumentNo + "'");

            while (rsTmp.next()) {
                clsFeltCreditNote ObjFeltCreditNote = new clsFeltCreditNote();

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
            resultSet = statement.executeQuery("SELECT DISTINCT REVISION_NO FROM PRODUCTION.FELT_CN_TEMP_HEADER_H WHERE CN_ID ='" + pDocNo + "'");
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
                strSQL = "SELECT DISTINCT CN_ID,CN_DATE,RECEIVED_DATE,PARA_DESC FROM PRODUCTION.FELT_CN_TEMP_HEADER, PRODUCTION.FELT_PROD_DOC_DATA ,PRODUCTION.FELT_PARAMETER_MASTER  WHERE CN_ID=DOC_NO AND STATUS='W' AND MODULE_ID=735 AND CANCELED=0  AND USER_ID=" + pUserID + "  AND PARA_ID ='CREDIT_NOTE' AND PARA_CODE = CN_TYPE ORDER BY RECEIVED_DATE,CN_ID";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT CN_ID,CN_DATE,RECEIVED_DATE,PARA_DESC FROM PRODUCTION.FELT_CN_TEMP_HEADER, PRODUCTION.FELT_PROD_DOC_DATA ,PRODUCTION.FELT_PARAMETER_MASTER  WHERE CN_ID=DOC_NO AND STATUS='W' AND MODULE_ID=735 AND CANCELED=0  AND USER_ID=" + pUserID + "  AND PARA_ID ='CREDIT_NOTE' AND PARA_CODE = CN_TYPE ORDER BY CN_DATE,CN_ID";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT CN_ID,CN_DATE,RECEIVED_DATE,PARA_DESC FROM PRODUCTION.FELT_CN_TEMP_HEADER, PRODUCTION.FELT_PROD_DOC_DATA ,PRODUCTION.FELT_PARAMETER_MASTER  WHERE CN_ID=DOC_NO AND STATUS='W' AND MODULE_ID=735 AND CANCELED=0  AND USER_ID=" + pUserID + "  AND PARA_ID ='CREDIT_NOTE' AND PARA_CODE = CN_TYPE ORDER BY CN_ID";
            }

            ResultSet rsTmp = data.getConn().createStatement().executeQuery(strSQL);

            Counter = 0;
            while (rsTmp.next()) {

                Counter = Counter + 1;
                clsFeltCreditNote ObjDoc = new clsFeltCreditNote();

                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO", rsTmp.getString("CN_ID"));
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

    public String getInvoiceDate(String pPieceNo) {
//        return data.getStringValueFromDB("SELECT INVOICE_DATE FROM PRODUCTION.FELT_INVOICE_DATA WHERE PR_PIECE_NO='"+pPieceNo+"'");
        //return data.getStringValueFromDB("SELECT INVOICE_DATE FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PIECE_NO LIKE ('%"+pPieceNo+"%') AND APPROVED=1 AND CANCELLED=0");
        return data.getStringValueFromDB("SELECT H.INVOICE_DATE FROM PRODUCTION.FELT_SAL_INVOICE_HEADER H, PRODUCTION.FELT_SAL_INVOICE_DETAIL D WHERE H.INVOICE_NO=D.INVOICE_NO AND H.INVOICE_DATE=SUBSTRING(D.INVOICE_DATE,1,10) AND D.PIECE_NO='" + pPieceNo + "' AND H.APPROVED=1 AND H.CANCELLED=0");

    }

    public String getPartyCode(String pPieceNo) {
//        return data.getStringValueFromDB("SELECT PARTY_CODE FROM PRODUCTION.FELT_INVOICE_DATA WHERE PR_PIECE_NO='"+pPieceNo+"'");
        //return data.getStringValueFromDB("SELECT PARTY_CODE FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PIECE_NO LIKE ('%"+pPieceNo+"%') AND APPROVED=1 AND CANCELLED=0");
        return data.getStringValueFromDB("SELECT H.PARTY_CODE FROM PRODUCTION.FELT_SAL_INVOICE_HEADER H, PRODUCTION.FELT_SAL_INVOICE_DETAIL D WHERE H.INVOICE_NO=D.INVOICE_NO AND H.INVOICE_DATE=SUBSTRING(D.INVOICE_DATE,1,10) AND D.PIECE_NO='" + pPieceNo + "' AND H.APPROVED=1 AND H.CANCELLED=0");

    }

    public String getPartyName(String pPieceNo) {
//        return data.getStringValueFromDB("SELECT PARTY_NAME FROM PRODUCTION.FELT_INVOICE_DATA WHERE PR_PIECE_NO='"+pPieceNo+"'");
        //return data.getStringValueFromDB("SELECT PARTY_NAME FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PIECE_NO LIKE ('%"+pPieceNo+"%') AND APPROVED=1 AND CANCELLED=0");
        return data.getStringValueFromDB("SELECT H.PARTY_NAME FROM PRODUCTION.FELT_SAL_INVOICE_HEADER H, PRODUCTION.FELT_SAL_INVOICE_DETAIL D WHERE H.INVOICE_NO=D.INVOICE_NO AND H.INVOICE_DATE=SUBSTRING(D.INVOICE_DATE,1,10) AND D.PIECE_NO='" + pPieceNo + "' AND H.APPROVED=1 AND H.CANCELLED=0");

    }

    public String getInvoiceNo(String pPieceNo) {
//        return data.getStringValueFromDB("SELECT INVOICE_NO FROM PRODUCTION.FELT_INVOICE_DATA WHERE PR_PIECE_NO='"+pPieceNo+"'");
        //return data.getStringValueFromDB("SELECT INVOICE_NO FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PIECE_NO LIKE ('%"+pPieceNo+"%') AND APPROVED=1 AND CANCELLED=0");
        return data.getStringValueFromDB("SELECT H.INVOICE_NO FROM PRODUCTION.FELT_SAL_INVOICE_HEADER H, PRODUCTION.FELT_SAL_INVOICE_DETAIL D WHERE H.INVOICE_NO=D.INVOICE_NO AND H.INVOICE_DATE=SUBSTRING(D.INVOICE_DATE,1,10) AND D.PIECE_NO='" + pPieceNo + "' AND H.APPROVED=1 AND H.CANCELLED=0");
    }

    public String getProductCode(String pPieceNo) {
//        return data.getStringValueFromDB("SELECT QUALITY_NO FROM PRODUCTION.FELT_INVOICE_DATA WHERE PR_PIECE_NO='"+pPieceNo+"'");
        //return data.getStringValueFromDB("SELECT PRODUCT_CODE FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PIECE_NO LIKE ('%"+pPieceNo+"%') AND APPROVED=1 AND CANCELLED=0");
        return data.getStringValueFromDB("SELECT H.PRODUCT_CODE FROM PRODUCTION.FELT_SAL_INVOICE_HEADER H, PRODUCTION.FELT_SAL_INVOICE_DETAIL D WHERE H.INVOICE_NO=D.INVOICE_NO AND H.INVOICE_DATE=SUBSTRING(D.INVOICE_DATE,1,10) AND D.PIECE_NO='" + pPieceNo + "' AND H.APPROVED=1 AND H.CANCELLED=0");
    }
//       public String getProductGroup(String pProdCode) {
////        return data.getStringValueFromDB("SELECT GRUP FROM PRODUCTION.FELT_RATE_MASTER_NEW WHERE PRODUCT_CODE='"+pPieceNo+"'");
//        return data.getStringValueFromDB("SELECT GROUP_NAME FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE='"+pProdCode+"' ORDER BY DOC_DATE DESC");
//     }

    public String getProductGroup(String pPieceNo) {
//        return data.getStringValueFromDB("SELECT GRUP FROM PRODUCTION.FELT_RATE_MASTER_NEW WHERE PRODUCT_CODE='"+pPieceNo+"'");
        //return data.getStringValueFromDB("SELECT GROUP_NAME FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE='"+pProdCode+"' ORDER BY DOC_DATE DESC");
        return data.getStringValueFromDB("SELECT D.GROUP_NAME FROM PRODUCTION.FELT_SAL_INVOICE_HEADER H, PRODUCTION.FELT_SAL_INVOICE_DETAIL D WHERE H.INVOICE_NO=D.INVOICE_NO AND H.INVOICE_DATE=SUBSTRING(D.INVOICE_DATE,1,10) AND D.PIECE_NO='" + pPieceNo + "' AND H.APPROVED=1 AND H.CANCELLED=0");
    }

    public String getKGS(String pPieceNo) {
//        return data.getStringValueFromDB("SELECT GROSS_KG FROM PRODUCTION.FELT_INVOICE_DATA WHERE PR_PIECE_NO='"+pPieceNo+"'");
        //return data.getStringValueFromDB("SELECT ACTUAL_WEIGHT FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PIECE_NO LIKE ('%"+pPieceNo+"%') AND APPROVED=1 AND CANCELLED=0");
        return data.getStringValueFromDB("SELECT H.ACTUAL_WEIGHT FROM PRODUCTION.FELT_SAL_INVOICE_HEADER H, PRODUCTION.FELT_SAL_INVOICE_DETAIL D WHERE H.INVOICE_NO=D.INVOICE_NO AND H.INVOICE_DATE=SUBSTRING(D.INVOICE_DATE,1,10) AND D.PIECE_NO='" + pPieceNo + "' AND H.APPROVED=1 AND H.CANCELLED=0");
    }

    public String getBasicValue(String pPieceNo) {
//        return data.getStringValueFromDB("SELECT TOTAL_GROSS FROM PRODUCTION.FELT_INVOICE_DATA WHERE PR_PIECE_NO='"+pPieceNo+"'");
        //return data.getStringValueFromDB("SELECT BAS_AMT FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PIECE_NO LIKE ('%"+pPieceNo+"%') AND APPROVED=1 AND CANCELLED=0");
        return data.getStringValueFromDB("SELECT H.BAS_AMT FROM PRODUCTION.FELT_SAL_INVOICE_HEADER H, PRODUCTION.FELT_SAL_INVOICE_DETAIL D WHERE H.INVOICE_NO=D.INVOICE_NO AND H.INVOICE_DATE=SUBSTRING(D.INVOICE_DATE,1,10) AND D.PIECE_NO='" + pPieceNo + "' AND H.APPROVED=1 AND H.CANCELLED=0");
    }

    public String getDisInBill(String pPieceNo) {
//        return data.getStringValueFromDB("SELECT TRD_DISCOUNT FROM PRODUCTION.FELT_INVOICE_DATA WHERE PR_PIECE_NO='"+pPieceNo+"'");
        //return data.getStringValueFromDB("SELECT DISC_AMT FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PIECE_NO LIKE ('%"+pPieceNo+"%') AND APPROVED=1 AND CANCELLED=0");
        return data.getStringValueFromDB("SELECT H.DISC_AMT FROM PRODUCTION.FELT_SAL_INVOICE_HEADER H, PRODUCTION.FELT_SAL_INVOICE_DETAIL D WHERE H.INVOICE_NO=D.INVOICE_NO AND H.INVOICE_DATE=SUBSTRING(D.INVOICE_DATE,1,10) AND D.PIECE_NO='" + pPieceNo + "' AND H.APPROVED=1 AND H.CANCELLED=0");
    }

    public String getUndjectedDisc(String pPieceNo) {
        //return data.getStringValueFromDB("SELECT WORK_DISC_PER FROM PRODUCTION.FELT_UNADJUSTED_TRN_D WHERE PIECE_NO='"+pPieceNo+"' ORDER BY INVOICE_DATE DESC");
        return data.getStringValueFromDB("SELECT D.WORK_DISC_PER FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER H, PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL D WHERE H.UNADJ_ID=D.UNADJ_ID AND H.APPROVED=1 AND H.CANCELLED=0 AND D.PIECE_NO='" + pPieceNo + "' ORDER BY D.INVOICE_DATE DESC");
    }

    public String getyearEndDisc(String pPartyCode, String pPieceNo, String pInvoiceDate) {
        //return data.getStringValueFromDB("SELECT YRED_DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE PARTY_CODE='"+pPartyCode+"' AND PRODUCT_CODE='"+pPieceNo+"' AND APPROVED=1 AND CANCELED=0 ORDER BY MASTER_NO DESC");
        return data.getStringValueFromDB("SELECT YRED_DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE PARTY_CODE='" + pPartyCode + "' AND PRODUCT_CODE='" + pPieceNo + "' AND EFFECTIVE_FROM<='" + pInvoiceDate + "' AND EFFECTIVE_TO>='" + pInvoiceDate + "' AND APPROVED=1 AND CANCELED=0 ORDER BY MASTER_NO DESC");
    }

    public String getUndjectedDiscrs(String pPieceNo) {
        //return data.getStringValueFromDB("SELECT DISC_AMT FROM PRODUCTION.FELT_UNADJUSTED_TRN_D WHERE PIECE_NO='"+pPieceNo+"'");
        return data.getStringValueFromDB("SELECT D.DISC_AMT FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER H, PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL D WHERE H.UNADJ_ID=D.UNADJ_ID AND H.APPROVED=1 AND H.CANCELLED=0 AND D.PIECE_NO='" + pPieceNo + "' ORDER BY D.INVOICE_DATE DESC");
    }

//    public String getYearEndd(String pPieceNo) {
//        return data.getStringValueFromDB("SELECT CN_YEAR_END_DISC FROM PRODUCTION.FELT_CN_TEMP_DETAIL WHERE CN_INV_PIECE_NO='" + pPieceNo + "'");
//    }
//
//    public String getYearEndDiscRs(String pPieceNo) {
//        return data.getStringValueFromDB("SELECT CN_YEAR_END_DISC_RS FROM PRODUCTION.FELT_CN_TEMP_DETAIL WHERE CN_INV_PIECE_NO='" + pPieceNo + "'");
//    }
//
//    public String getUndjested(String pPieceNo) {
//        return data.getStringValueFromDB("SELECT CN_UNADJUSTED_DISC FROM PRODUCTION.FELT_CN_TEMP_DETAIL WHERE CN_INV_PIECE_NO='" + pPieceNo + "'");
//    }
//
//    public String getUndjestediscRs(String pPieceNo) {
//        return data.getStringValueFromDB("SELECT CN_UNADJUSTED_RS FROM PRODUCTION.FELT_CN_TEMP_DETAIL WHERE CN_INV_PIECE_NO='" + pPieceNo + "'");
//    }
//    
//    public String getOemDiscRs(String pPieceNo) {
//        //return data.getStringValueFromDB("SELECT CN_OEM_VALUE FROM PRODUCTION.FELT_CN_TEMP_DETAIL WHERE CN_INV_PIECE_NO='"+pPieceNo+"'");
//        return data.getStringValueFromDB("SELECT COM_OEM_AMT FROM PRODUCTION.FELT_CN_TEMP_DETAIL WHERE CN_INV_PIECE_NO='" + pPieceNo + "'");
//    }

    public String getYearEndd(String pInvNo, String pInvDt) {
        return data.getStringValueFromDB("SELECT CND_PERCENT FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE='YEAREND' AND CND_INVOICE_NO='" + pInvNo + "' AND CND_INVOICE_DATE='" + pInvDt + "' ");
    }

    public String getYearEndDiscRs(String pInvNo, String pInvDt) {
        return data.getStringValueFromDB("SELECT CND_CREDIT_AMOUNT FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE='YEAREND' AND CND_INVOICE_NO='" + pInvNo + "' AND CND_INVOICE_DATE='" + pInvDt + "' ");
    }

    public String getUndjested(String pInvNo, String pInvDt) {
        return data.getStringValueFromDB("SELECT CND_PERCENT FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE='UNADJ' AND CND_INVOICE_NO='" + pInvNo + "' AND CND_INVOICE_DATE='" + pInvDt + "' ");
    }

    public String getUndjestediscRs(String pInvNo, String pInvDt) {
        return data.getStringValueFromDB("SELECT CND_CREDIT_AMOUNT FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE='UNADJ' AND CND_INVOICE_NO='" + pInvNo + "' AND CND_INVOICE_DATE='" + pInvDt + "' ");
    }

    public String getOemDiscRs(String pInvNo, String pInvDt) {
        return data.getStringValueFromDB("SELECT CND_CREDIT_AMOUNT FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE='OEM' AND CND_INVOICE_NO='" + pInvNo + "' AND CND_INVOICE_DATE='" + pInvDt + "' ");
    }

    public String getInsuranceRs(String pPieceNo) {
//         return data.getStringValueFromDB("SELECT INSURANCE FROM PRODUCTION.FELT_INVOICE_DATA WHERE PR_PIECE_NO='"+pPieceNo+"'");
        //return data.getStringValueFromDB("SELECT INSURANCE_AMT FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PIECE_NO LIKE ('%"+pPieceNo+"%') AND APPROVED=1 AND CANCELLED=0");
        return data.getStringValueFromDB("SELECT H.INSURANCE_AMT FROM PRODUCTION.FELT_SAL_INVOICE_HEADER H, PRODUCTION.FELT_SAL_INVOICE_DETAIL D WHERE H.INVOICE_NO=D.INVOICE_NO AND H.INVOICE_DATE=SUBSTRING(D.INVOICE_DATE,1,10) AND D.PIECE_NO='" + pPieceNo + "' AND H.APPROVED=1 AND H.CANCELLED=0");

    }

    public String getCST(String pPieceNo) {
        //return data.getStringValueFromDB("SELECT CST2 FROM DINESHMILLS.D_SAL_INVOICE_HEADER WHERE PIECE_NO='"+pPieceNo+"'");
        return data.getStringValueFromDB("SELECT H.CST2 FROM PRODUCTION.FELT_SAL_INVOICE_HEADER H, PRODUCTION.FELT_SAL_INVOICE_DETAIL D WHERE H.INVOICE_NO=D.INVOICE_NO AND H.INVOICE_DATE=SUBSTRING(D.INVOICE_DATE,1,10) AND D.PIECE_NO='" + pPieceNo + "' AND H.APPROVED=1 AND H.CANCELLED=0");

    }

    public String getExc(String pPieceNo) {
        //return data.getStringValueFromDB("SELECT BASIC_DUTY FROM PRODUCTION.FELT_INVOICE_DATA WHERE PR_PIECE_NO='"+pPieceNo+"' ");
        return data.getStringValueFromDB("SELECT H.EXCISE FROM PRODUCTION.FELT_SAL_INVOICE_HEADER H, PRODUCTION.FELT_SAL_INVOICE_DETAIL D WHERE H.INVOICE_NO=D.INVOICE_NO AND H.INVOICE_DATE=SUBSTRING(D.INVOICE_DATE,1,10) AND D.PIECE_NO='" + pPieceNo + "' AND H.APPROVED=1 AND H.CANCELLED=0");

    }

    public String getInvoiceAmount(String pPieceNo) {
//         return data.getStringValueFromDB("SELECT TOTAL_NET_AMOUNT FROM PRODUCTION.FELT_INVOICE_DATA WHERE PR_PIECE_NO='"+pPieceNo+"'");
        //return data.getStringValueFromDB("SELECT INVOICE_AMT FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PIECE_NO LIKE ('%"+pPieceNo+"%') AND APPROVED=1 AND CANCELLED=0");
        return data.getStringValueFromDB("SELECT H.INVOICE_AMT FROM PRODUCTION.FELT_SAL_INVOICE_HEADER H, PRODUCTION.FELT_SAL_INVOICE_DETAIL D WHERE H.INVOICE_NO=D.INVOICE_NO AND H.INVOICE_DATE=SUBSTRING(D.INVOICE_DATE,1,10) AND D.PIECE_NO='" + pPieceNo + "' AND H.APPROVED=1 AND H.CANCELLED=0");

    }
    
    public String getSalesReturnDoc(String pPieceNo) {
        return data.getStringValueFromDB("SELECT H.DOC_NO FROM PRODUCTION.FELT_SALES_RETURNS_HEADER H, PRODUCTION.FELT_SALES_RETURNS_DETAIL D WHERE H.DOC_NO=D.DOC_NO AND H.APPROVED=1 AND H.CANCELED=0 AND D.PIECE_NO='" + pPieceNo + "' ");
    }
    
    public String getSalesReturnDate(String pPieceNo) {
        return data.getStringValueFromDB("SELECT H.DOC_DATE FROM PRODUCTION.FELT_SALES_RETURNS_HEADER H, PRODUCTION.FELT_SALES_RETURNS_DETAIL D WHERE H.DOC_NO=D.DOC_NO AND H.APPROVED=1 AND H.CANCELED=0 AND D.PIECE_NO='" + pPieceNo + "' ");
    }

    public boolean checkPieceNoInDB1(String pPieceNo, String pAmend_Reason, String pProdindstring, String pAgreedindstring) {
        int count = data.getIntValueFromDB("SELECT COUNT(FLT_PIECE_NO) FROM PRODUCTION.FELT_CN_TEMP_HEADER WHERE FLT_PIECE_NO='" + pPieceNo + "' AND CN_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND CN_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'");
        if (count >= 1) {

            int counter = 0;

            try {
                Connection Conn = data.getConn();
                Statement stTmp = Conn.createStatement();
                ResultSet rsTmp = stTmp.executeQuery("SELECT CN_DATE FROM PRODUCTION.FELT_CN_TEMP_HEADER WHERE FLT_PIECE_NO='" + pPieceNo + "' AND CN_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND CN_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'");
                while (rsTmp.next()) {
                    //                 if(rsTmp.getString("CN_DATE").equals(EITLERPGLOBAL.formatDateDB(pProdDate))) counter++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (counter == 1 && count >= 2) {
                return true;
            } else if (counter == 1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    // checks production date already exist in db
    public boolean checkProductionDateInDB(String pProdDate) {
        int count = data.getIntValueFromDB("SELECT COUNT(CN_DATE) FROM PRODUCTION.FELT_CN_TEMP_HEADER WHERE CN_DATE='" + pProdDate + "'");
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPartyCodeInDB(String pPartyCode) {
        int count = data.getIntValueFromDB("SELECT COUNT(PARTY_CODE) FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE =210010 AND APPROVED =1 AND PARTY_CODE='" + pPartyCode + "'");
        if (count <= 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkProductCodeInDB(String pProductCode) {
        int count = data.getIntValueFromDB("SELECT COUNT(ITEM_CODE) FROM PRODUCTION.FELT_RATE_MASTER WHERE SUBSTRING(ITEM_CODE,1,6)='" + pProductCode + "'");
        if (count <= 0) {
            return true;
        } else {
            return false;
        }
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
            ResultSet rsTmp = stTmp.executeQuery("SELECT USER_ID,USER_NAME FROM PRODUCTION.`FELT_USER` WHERE USER_MODULE='" + pModule + "' AND USER_CATEG='" + category + "' ORDER BY USER_NAME");
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

    public static HashMap getCreditNoteList() {
        HashMap List = new HashMap();
        try {
            Connection Conn = data.getConn();
            Statement stTmp = Conn.createStatement();
            int counter = 1;
            ResultSet rsTmp = stTmp.executeQuery("SELECT 0 AS PARA_CODE,'SELECT CREDIT NOTE' AS PARA_DESC UNION ALL SELECT PARA_CODE,PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID = 'CREDIT_NOTE' ORDER BY PARA_CODE");
            while (rsTmp.next()) {
                ComboData aData = new ComboData();
                aData.Code = rsTmp.getInt("PARA_CODE");
                aData.Text = rsTmp.getString("PARA_DESC");
                List.put(new Integer(counter++), aData);
            }
        } catch (SQLException e) {
            //      LastError = e.getMessage();
            e.printStackTrace();
        }
        return List;
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

    public static boolean CanCancel(int pCompanyID, String pDocNo) {
        ResultSet rsTmp = null;
        boolean canCancel = false;

        try {
            rsTmp = data.getResult("SELECT CN_ID,APPROVED FROM PRODUCTION.FELT_CN_TEMP_HEADER WHERE CN_ID='" + pDocNo + "' AND CANCELED=0");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                if (rsTmp.getBoolean("APPROVED")) {

                } else {
                    canCancel = true;
                }
            }

            rsTmp.close();
        } catch (Exception e) {

        }

        return canCancel;

    }

    public static boolean CancelDoc(int pCompanyID, String pDocNo) {

        ResultSet rsTmp = null, rsIndent = null;
        boolean Cancelled = false;

        try {
            if (CanCancel(pCompanyID, pDocNo)) {

                boolean ApprovedDoc = false;

                rsTmp = data.getResult("SELECT APPROVED FROM PRODUCTION.FELT_CN_TEMP_HEADER WHERE CN_ID='" + pDocNo + "' ");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    ApprovedDoc = rsTmp.getBoolean("APPROVED");
                }

                if (ApprovedDoc) {

                } else {
                    int ModuleID = 735;

                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='" + pDocNo + "' AND MODULE_ID=" + (ModuleID));
                }

                //Now Update the header with cancel falg to true
                data.Execute("UPDATE PRODUCTION.FELT_CN_TEMP_HEADER SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE CN_ID='" + pDocNo + "' ");

                Cancelled = true;
            }

            rsTmp.close();
            rsIndent.close();
        } catch (Exception e) {

        }

        return Cancelled;
    }

}
