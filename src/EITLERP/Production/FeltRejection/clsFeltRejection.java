/*
 * clsFeltRejection.java
 *
 * Created on May 23, 2013, 3:09 PM
 */
package EITLERP.Production.FeltRejection;

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
import EITLERP.Production.FeltUser;
import EITLERP.Production.clsFeltProductionApprovalFlow;

/**
 *
 * @author Vivek Kumar
 * @version
 */
public class clsFeltRejection {

    public String LastError = "";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public HashMap props;
    public boolean Ready = false;

    //History Related properties
    public boolean HistoryView = false;
    private String historyRejectionDate = "";
    private String historyDocumentNo = "";

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

    public void clear() {
        props.clear();
    }

    public clsFeltRejection() {
        LastError = "";
        props = new HashMap();
        props.put("REJECTION_DATE", new Variant(""));
        props.put("REJECTION_FORM_NO", new Variant(""));
        props.put("REJECTION_DEPARTMENT", new Variant(""));
        props.put("PIECE_STAGE", new Variant(""));
        props.put("REJECTION_DOCUMENT_NO", new Variant(""));
        props.put("SERIAL_NO", new Variant(0));
        props.put("REJECTION_PIECE_NO", new Variant(""));
        props.put("REJECTION_NEW_PIECE_NO", new Variant(""));
        props.put("REJECTION_PARTY_CODE", new Variant(""));
        props.put("STYLE", new Variant(""));
        props.put("SYN_PER", new Variant(0.00));
        props.put("GROUP", new Variant(""));
        props.put("REQUIRED_WEIGHT", new Variant(0.00));
        props.put("ACTUAL_WEIGHT", new Variant(0.00));
        props.put("MFG_SIZE", new Variant(0.00));
        props.put("ACTUAL_SIZE", new Variant(0.00));
        props.put("ORDER_LENGTH", new Variant(0.00));
        props.put("ORDER_WIDTH", new Variant(0.00));
        props.put("ORDER_GSM", new Variant(0.00));
        props.put("BILL_WEIGHT", new Variant(0.00));
        props.put("BILL_LENGTH", new Variant(0.00));
        props.put("BILL_WIDTH", new Variant(0.00));
        props.put("BILL_GSM", new Variant(0.00));
        props.put("BILL_WEIGHT", new Variant(0.00));
        props.put("GSM", new Variant(0.00));
        props.put("REASON", new Variant(""));
        props.put("REMARKS", new Variant(""));
        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("APPROVED", new Variant(0));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED", new Variant(false));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("HIERARCHY_ID", new Variant(0));
        props.put("UPDATED_BY", new Variant(0));
        props.put("ENTRY_DATE", new Variant(""));
        props.put("FROM", new Variant(0));
        props.put("TO", new Variant(0));
        props.put("APPROVAL_STATUS", new Variant(""));
        props.put("CANCELLED", new Variant(0));
        props.put("IS_PROCESS", new Variant(true));

        props.put("ADJUSTED_LENGTH", new Variant(0.00));
        props.put("ADJUSTED_WIDTH", new Variant(0.00));
        props.put("ADJUSTED_GSM", new Variant(0.00));
        props.put("ADJUSTED_WEIGHT", new Variant(0.00));

        props.put("BASE_GSM", new Variant(0.00));
        props.put("WEB_GSM", new Variant(0.00));
        props.put("WEAVE", new Variant(""));
        props.put("CFM_TARGETED", new Variant(""));
        props.put("PAPER_PRODUCT_TYPE", new Variant(""));
        props.put("REJECTION_LOCATION", new Variant(""));
        props.put("LOOM_NO", new Variant(""));
        props.put("OBSOLETE_UPN_ASSIGN_STATUS", new Variant(""));
        props.put("SCRAP_REASON", new Variant(""));
        props.put("UNMAPPED_REASON", new Variant(""));
        
        props.put("REJECTION_ORIGINATED_FROM", new Variant(""));
    }

    public boolean LoadData() {
        Ready = false;
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            //resultSet=statement.executeQuery("SELECT DISTINCT REJ_DOC_NO FROM PRODUCTION.`FELT_REJECTION` WHERE REJ_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND REJ_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY REJ_DATE");
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_REJECTION ORDER BY REJ_DATE,REJ_DOC_NO");
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
                setHistoryData(historyRejectionDate, historyDocumentNo);
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
                setHistoryData(historyRejectionDate, historyDocumentNo);
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
                setHistoryData(historyRejectionDate, historyDocumentNo);
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
                setHistoryData(historyRejectionDate, historyDocumentNo);
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
        ResultSet resultSetTemp, resultSetHistory;
        Statement statementTemp, statementHistory;
        try {
            // Rejection data connection
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.`FELT_REJECTION` WHERE REJ_DATE=''");

            // Rejection data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.`FELT_REJECTION_H` WHERE REJ_DATE=''");

            //setAttribute("REJECTION_DOCUMENT_NO", EITLERP.clsFirstFree.getNextFreeNo(2, 764, getAttribute("FFNO").getInt(), true));
            //Now Insert records into rejecction tables
            resultSetTemp.moveToInsertRow();
            resultSetTemp.updateString("REJ_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("REJECTION_DATE").getString()));
            resultSetTemp.updateString("REJ_DOC_NO", getAttribute("REJECTION_DOCUMENT_NO").getString());
            resultSetTemp.updateString("REJ_FORM_NO", getAttribute("REJECTION_FORM_NO").getString());
            resultSetTemp.updateString("REJ_DEPT", getAttribute("REJECTION_DEPARTMENT").getString());
            resultSetTemp.updateString("PIECE_STAGE", getAttribute("PIECE_STAGE").getString());
            resultSetTemp.updateString("REJ_PIECE_NO", getAttribute("REJECTION_PIECE_NO").getString());
            resultSetTemp.updateString("REJ_NEW_PIECE_NO", getAttribute("REJECTION_NEW_PIECE_NO").getString());
            resultSetTemp.updateString("REJ_PARTY_CODE", getAttribute("REJECTION_PARTY_CODE").getString());
            resultSetTemp.updateString("STYLE", getAttribute("STYLE").getString());
            resultSetTemp.updateString("GROUP", getAttribute("GROUP").getString());
            resultSetTemp.updateFloat("SYN_PER", (float) EITLERPGLOBAL.round(getAttribute("SYN_PER").getVal(), 2));
            resultSetTemp.updateFloat("GSM", (float) EITLERPGLOBAL.round(getAttribute("GSM").getVal(), 2));
            resultSetTemp.updateString("MFG_SIZE", getAttribute("MFG_SIZE").getString());
            resultSetTemp.updateString("ACT_SIZE", getAttribute("ACTUAL_SIZE").getString());
            //resultSetTemp.updateFloat("REQ_WEIGHT", (float) EITLERPGLOBAL.round(getAttribute("REQUIRED_WEIGHT").getVal(), 2));
            //resultSetTemp.updateFloat("ACT_WEIGHT", (float) EITLERPGLOBAL.round(getAttribute("ACTUAL_WEIGHT").getVal(), 2));
            resultSetTemp.updateString("REASON", getAttribute("REASON").getString());
            resultSetTemp.updateString("COMMENT", getAttribute("COMMENT").getString());
            resultSetTemp.updateString("ACTION", getAttribute("ACTION").getString());

            resultSetTemp.updateInt("IS_PROCESS", getAttribute("IS_PROCESS").getInt());

            resultSetTemp.updateDouble("ORDER_LENGTH", (double) EITLERPGLOBAL.round(getAttribute("ORDER_LENGTH").getVal(), 2));
            resultSetTemp.updateDouble("ORDER_WIDTH", (double) EITLERPGLOBAL.round(getAttribute("ORDER_WIDTH").getVal(), 2));
            resultSetTemp.updateDouble("ORDER_GSM", (double) EITLERPGLOBAL.round(getAttribute("ORDER_GSM").getVal(), 2));
            resultSetTemp.updateDouble("ORDER_WEIGHT", (double) EITLERPGLOBAL.round(getAttribute("ORDER_WEIGHT").getVal(), 2));

            resultSetTemp.updateDouble("BILL_LENGTH", (double) EITLERPGLOBAL.round(getAttribute("BILL_LENGTH").getVal(), 2));
            resultSetTemp.updateDouble("BILL_WIDTH", (double) EITLERPGLOBAL.round(getAttribute("BILL_WIDTH").getVal(), 2));
            resultSetTemp.updateDouble("BILL_GSM", (double) EITLERPGLOBAL.round(getAttribute("BILL_GSM").getVal(), 2));
            resultSetTemp.updateDouble("BILL_WEIGHT", (double) EITLERPGLOBAL.round(getAttribute("BILL_WEIGHT").getVal(), 2));

            resultSetTemp.updateDouble("ADJUSTED_LENGTH", (double) EITLERPGLOBAL.round(getAttribute("ADJUSTED_LENGTH").getVal(), 2));
            resultSetTemp.updateDouble("ADJUSTED_WIDTH", (double) EITLERPGLOBAL.round(getAttribute("ADJUSTED_WIDTH").getVal(), 2));
            resultSetTemp.updateDouble("ADJUSTED_GSM", (double) EITLERPGLOBAL.round(getAttribute("ADJUSTED_GSM").getVal(), 2));
            resultSetTemp.updateDouble("ADJUSTED_WEIGHT", (double) EITLERPGLOBAL.round(getAttribute("ADJUSTED_WEIGHT").getVal(), 2));

            resultSetTemp.updateDouble("BASE_GSM", (double) EITLERPGLOBAL.round(getAttribute("BASE_GSM").getVal(), 2));
            resultSetTemp.updateDouble("WEB_GSM", (double) EITLERPGLOBAL.round(getAttribute("WEB_GSM").getVal(), 2));
            resultSetTemp.updateString("WEAVE", getAttribute("WEAVE").getString());
            resultSetTemp.updateString("CFM_TARGETED", getAttribute("CFM_TARGETED").getString());
            resultSetTemp.updateString("PAPER_PRODUCT_TYPE", getAttribute("PAPER_PRODUCT_TYPE").getString());
            resultSetTemp.updateString("REJECTION_LOCATION", getAttribute("REJECTION_LOCATION").getString());
            resultSetTemp.updateString("LOOM_NO", getAttribute("LOOM_NO").getString());
            resultSetTemp.updateString("OBSOLETE_UPN_ASSIGN_STATUS", getAttribute("OBSOLETE_UPN_ASSIGN_STATUS").getString());
            resultSetTemp.updateString("SCRAP_REASON", getAttribute("SCRAP_REASON").getString());
            resultSetTemp.updateString("UNMAPPED_REASON", getAttribute("UNMAPPED_REASON").getString());
            
            resultSetTemp.updateString("REJECTION_ORIGINATED_FROM", getAttribute("REJECTION_ORIGINATED_FROM").getString());

            resultSetTemp.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
            resultSetTemp.updateString("CREATED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSetTemp.updateInt("MODIFIED_BY", 0);
            resultSetTemp.updateString("MODIFIED_DATE", "0000-00-00");
            resultSetTemp.updateBoolean("APPROVED", false);
            resultSetTemp.updateString("APPROVED_DATE", "0000-00-00");
            resultSetTemp.updateBoolean("REJECTED", false);
            resultSetTemp.updateString("REJECTED_DATE", "0000-00-00");
            resultSetTemp.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            resultSetTemp.updateInt("CHANGED", 1);
            resultSetTemp.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSetTemp.insertRow();

            //Insert records into rejecction data history table
            resultSetHistory.moveToInsertRow();
            resultSetHistory.updateInt("REVISION_NO", 1);
            resultSetHistory.updateString("REJ_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("REJECTION_DATE").getString()));
            resultSetHistory.updateString("REJ_DOC_NO", getAttribute("REJECTION_DOCUMENT_NO").getString());
            resultSetHistory.updateString("REJ_FORM_NO", getAttribute("REJECTION_FORM_NO").getString());
            resultSetHistory.updateString("REJ_DEPT", getAttribute("REJECTION_DEPARTMENT").getString());
            resultSetHistory.updateString("PIECE_STAGE", getAttribute("PIECE_STAGE").getString());
            resultSetHistory.updateString("REJ_PIECE_NO", getAttribute("REJECTION_PIECE_NO").getString());
            resultSetHistory.updateString("REJ_NEW_PIECE_NO", getAttribute("REJECTION_NEW_PIECE_NO").getString());
            resultSetHistory.updateString("REJ_PARTY_CODE", getAttribute("REJECTION_PARTY_CODE").getString());
            resultSetHistory.updateString("STYLE", getAttribute("STYLE").getString());
            resultSetHistory.updateString("GROUP", getAttribute("GROUP").getString());
            resultSetHistory.updateFloat("SYN_PER", (float) EITLERPGLOBAL.round(getAttribute("SYN_PER").getVal(), 2));
            resultSetHistory.updateFloat("GSM", (float) EITLERPGLOBAL.round(getAttribute("GSM").getVal(), 2));
            resultSetHistory.updateString("MFG_SIZE", getAttribute("MFG_SIZE").getString());
            resultSetHistory.updateString("ACT_SIZE", getAttribute("ACTUAL_SIZE").getString());
            //resultSetHistory.updateFloat("REQ_WEIGHT", (float) EITLERPGLOBAL.round(getAttribute("REQUIRED_WEIGHT").getVal(), 2));
            //resultSetHistory.updateFloat("ACT_WEIGHT", (float) EITLERPGLOBAL.round(getAttribute("ACTUAL_WEIGHT").getVal(), 2));
            resultSetHistory.updateString("REASON", getAttribute("REASON").getString());
            resultSetHistory.updateString("COMMENT", getAttribute("COMMENT").getString());
            resultSetHistory.updateString("ACTION", getAttribute("ACTION").getString());

            resultSetHistory.updateInt("IS_PROCESS", getAttribute("IS_PROCESS").getInt());

            resultSetHistory.updateDouble("ORDER_LENGTH", (double) EITLERPGLOBAL.round(getAttribute("ORDER_LENGTH").getVal(), 2));
            resultSetHistory.updateDouble("ORDER_WIDTH", (double) EITLERPGLOBAL.round(getAttribute("ORDER_WIDTH").getVal(), 2));
            resultSetHistory.updateDouble("ORDER_GSM", (double) EITLERPGLOBAL.round(getAttribute("ORDER_GSM").getVal(), 2));
            resultSetHistory.updateDouble("ORDER_WEIGHT", (double) EITLERPGLOBAL.round(getAttribute("ORDER_WEIGHT").getVal(), 2));

            resultSetHistory.updateDouble("BILL_LENGTH", (double) EITLERPGLOBAL.round(getAttribute("BILL_LENGTH").getVal(), 2));
            resultSetHistory.updateDouble("BILL_WIDTH", (double) EITLERPGLOBAL.round(getAttribute("BILL_WIDTH").getVal(), 2));
            resultSetHistory.updateDouble("BILL_GSM", (double) EITLERPGLOBAL.round(getAttribute("BILL_GSM").getVal(), 2));
            resultSetHistory.updateDouble("BILL_WEIGHT", (double) EITLERPGLOBAL.round(getAttribute("BILL_WEIGHT").getVal(), 2));

            resultSetHistory.updateDouble("ADJUSTED_LENGTH", (double) EITLERPGLOBAL.round(getAttribute("ADJUSTED_LENGTH").getVal(), 2));
            resultSetHistory.updateDouble("ADJUSTED_WIDTH", (double) EITLERPGLOBAL.round(getAttribute("ADJUSTED_WIDTH").getVal(), 2));
            resultSetHistory.updateDouble("ADJUSTED_GSM", (double) EITLERPGLOBAL.round(getAttribute("ADJUSTED_GSM").getVal(), 2));
            resultSetHistory.updateDouble("ADJUSTED_WEIGHT", (double) EITLERPGLOBAL.round(getAttribute("ADJUSTED_WEIGHT").getVal(), 2));

            resultSetHistory.updateDouble("BASE_GSM", (double) EITLERPGLOBAL.round(getAttribute("BASE_GSM").getVal(), 2));
            resultSetHistory.updateDouble("WEB_GSM", (double) EITLERPGLOBAL.round(getAttribute("WEB_GSM").getVal(), 2));
            resultSetHistory.updateString("WEAVE", getAttribute("WEAVE").getString());
            resultSetHistory.updateString("CFM_TARGETED", getAttribute("CFM_TARGETED").getString());
            resultSetHistory.updateString("PAPER_PRODUCT_TYPE", getAttribute("PAPER_PRODUCT_TYPE").getString());
            resultSetHistory.updateString("REJECTION_LOCATION", getAttribute("REJECTION_LOCATION").getString());
            resultSetHistory.updateString("LOOM_NO", getAttribute("LOOM_NO").getString());
            resultSetHistory.updateString("OBSOLETE_UPN_ASSIGN_STATUS", getAttribute("OBSOLETE_UPN_ASSIGN_STATUS").getString());
            resultSetHistory.updateString("SCRAP_REASON", getAttribute("SCRAP_REASON").getString());
            resultSetHistory.updateString("UNMAPPED_REASON", getAttribute("UNMAPPED_REASON").getString());
            
            resultSetHistory.updateString("REJECTION_ORIGINATED_FROM", getAttribute("REJECTION_ORIGINATED_FROM").getString());

            resultSetHistory.updateInt("UPDATED_BY", (int) getAttribute("UPDATED_BY").getVal());
            resultSetHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSetHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            resultSetHistory.updateString("APPROVER_REMARKS", getAttribute("FROM_REMARKS").getString());
            resultSetHistory.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            resultSetHistory.updateInt("CHANGED", 1);
            resultSetHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSetHistory.insertRow();

            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = 764; //Felt Rejection Entry
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("REJECTION_DOCUMENT_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.FELT_REJECTION";
            ObjFeltProductionApprovalFlow.IsCreator = true;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "REJ_DOC_NO";

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
        ResultSet resultSetTemp, resultSetHistory;
        Statement statementTemp, statementHistory;
        int revisionNo = 1;
        try {
            // Rejection data connection
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.`FELT_REJECTION` WHERE REJ_DATE=''");

            // Rejection data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            // Get the Maximum Revision No in History Table.
            resultSetHistory = statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_REJECTION_H WHERE REJ_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("REJECTION_DATE").getString()) + "' AND REJ_DOC_NO='" + getAttribute("REJECTION_DOCUMENT_NO").getString() + "'");
            resultSetHistory.first();
            if (resultSetHistory.getRow() > 0) {
                revisionNo = resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from rejection data table before insert
            statementHistory.execute("DELETE FROM PRODUCTION.`FELT_REJECTION` WHERE REJ_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("REJECTION_DATE").getString()) + "' AND REJ_DOC_NO='" + getAttribute("REJECTION_DOCUMENT_NO").getString() + "'");

            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.`FELT_REJECTION_H` WHERE REJ_DATE=''");

            //Now Update records into rejecction tables
            resultSetTemp.moveToInsertRow();
            resultSetTemp.updateString("REJ_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("REJECTION_DATE").getString()));
            resultSetTemp.updateString("REJ_DOC_NO", getAttribute("REJECTION_DOCUMENT_NO").getString());
            resultSetTemp.updateString("REJ_FORM_NO", getAttribute("REJECTION_FORM_NO").getString());
            resultSetTemp.updateString("REJ_DEPT", getAttribute("REJECTION_DEPARTMENT").getString());
            resultSetTemp.updateString("PIECE_STAGE", getAttribute("PIECE_STAGE").getString());
            resultSetTemp.updateString("REJ_PIECE_NO", getAttribute("REJECTION_PIECE_NO").getString());
            resultSetTemp.updateString("REJ_NEW_PIECE_NO", getAttribute("REJECTION_NEW_PIECE_NO").getString());
            resultSetTemp.updateString("REJ_PARTY_CODE", getAttribute("REJECTION_PARTY_CODE").getString());
            resultSetTemp.updateString("STYLE", getAttribute("STYLE").getString());
            resultSetTemp.updateString("GROUP", getAttribute("GROUP").getString());
            resultSetTemp.updateFloat("SYN_PER", (float) EITLERPGLOBAL.round(getAttribute("SYN_PER").getVal(), 2));
            resultSetTemp.updateFloat("GSM", (float) EITLERPGLOBAL.round(getAttribute("GSM").getVal(), 2));
            resultSetTemp.updateString("MFG_SIZE", getAttribute("MFG_SIZE").getString());
            resultSetTemp.updateString("ACT_SIZE", getAttribute("ACTUAL_SIZE").getString());
            //resultSetTemp.updateFloat("REQ_WEIGHT", (float) EITLERPGLOBAL.round(getAttribute("REQUIRED_WEIGHT").getVal(), 2));
            //resultSetTemp.updateFloat("ACT_WEIGHT", (float) EITLERPGLOBAL.round(getAttribute("ACTUAL_WEIGHT").getVal(), 2));
            resultSetTemp.updateString("REASON", getAttribute("REASON").getString());
            resultSetTemp.updateString("COMMENT", getAttribute("COMMENT").getString());
            resultSetTemp.updateString("ACTION", getAttribute("ACTION").getString());

            resultSetTemp.updateInt("IS_PROCESS", getAttribute("IS_PROCESS").getInt());

            resultSetTemp.updateDouble("ORDER_LENGTH", (double) EITLERPGLOBAL.round(getAttribute("ORDER_LENGTH").getVal(), 2));
            resultSetTemp.updateDouble("ORDER_WIDTH", (double) EITLERPGLOBAL.round(getAttribute("ORDER_WIDTH").getVal(), 2));
            resultSetTemp.updateDouble("ORDER_GSM", (double) EITLERPGLOBAL.round(getAttribute("ORDER_GSM").getVal(), 2));
            resultSetTemp.updateDouble("ORDER_WEIGHT", (double) EITLERPGLOBAL.round(getAttribute("ORDER_WEIGHT").getVal(), 2));

            resultSetTemp.updateDouble("BILL_LENGTH", (double) EITLERPGLOBAL.round(getAttribute("BILL_LENGTH").getVal(), 2));
            resultSetTemp.updateDouble("BILL_WIDTH", (double) EITLERPGLOBAL.round(getAttribute("BILL_WIDTH").getVal(), 2));
            resultSetTemp.updateDouble("BILL_GSM", (double) EITLERPGLOBAL.round(getAttribute("BILL_GSM").getVal(), 2));
            resultSetTemp.updateDouble("BILL_WEIGHT", (double) EITLERPGLOBAL.round(getAttribute("BILL_WEIGHT").getVal(), 2));

            resultSetTemp.updateDouble("ADJUSTED_LENGTH", (double) EITLERPGLOBAL.round(getAttribute("ADJUSTED_LENGTH").getVal(), 2));
            resultSetTemp.updateDouble("ADJUSTED_WIDTH", (double) EITLERPGLOBAL.round(getAttribute("ADJUSTED_WIDTH").getVal(), 2));
            resultSetTemp.updateDouble("ADJUSTED_GSM", (double) EITLERPGLOBAL.round(getAttribute("ADJUSTED_GSM").getVal(), 2));
            resultSetTemp.updateDouble("ADJUSTED_WEIGHT", (double) EITLERPGLOBAL.round(getAttribute("ADJUSTED_WEIGHT").getVal(), 2));

            resultSetTemp.updateDouble("BASE_GSM", (double) EITLERPGLOBAL.round(getAttribute("BASE_GSM").getVal(), 2));
            resultSetTemp.updateDouble("WEB_GSM", (double) EITLERPGLOBAL.round(getAttribute("WEB_GSM").getVal(), 2));
            resultSetTemp.updateString("WEAVE", getAttribute("WEAVE").getString());
            resultSetTemp.updateString("CFM_TARGETED", getAttribute("CFM_TARGETED").getString());
            resultSetTemp.updateString("PAPER_PRODUCT_TYPE", getAttribute("PAPER_PRODUCT_TYPE").getString());
            resultSetTemp.updateString("REJECTION_LOCATION", getAttribute("REJECTION_LOCATION").getString());
            resultSetTemp.updateString("LOOM_NO", getAttribute("LOOM_NO").getString());
            resultSetTemp.updateString("OBSOLETE_UPN_ASSIGN_STATUS", getAttribute("OBSOLETE_UPN_ASSIGN_STATUS").getString());
            resultSetTemp.updateString("SCRAP_REASON", getAttribute("SCRAP_REASON").getString());
            resultSetTemp.updateString("UNMAPPED_REASON", getAttribute("UNMAPPED_REASON").getString());
            
            resultSetTemp.updateString("REJECTION_ORIGINATED_FROM", getAttribute("REJECTION_ORIGINATED_FROM").getString());

            resultSetTemp.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
            resultSetTemp.updateString("CREATED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSetTemp.updateInt("MODIFIED_BY", 0);
            resultSetTemp.updateString("MODIFIED_DATE", "0000-00-00");
            resultSetTemp.updateBoolean("APPROVED", false);
            resultSetTemp.updateString("APPROVED_DATE", "0000-00-00");
            resultSetTemp.updateBoolean("REJECTED", false);
            resultSetTemp.updateString("REJECTED_DATE", "0000-00-00");
            resultSetTemp.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            resultSetTemp.updateInt("CHANGED", 1);
            resultSetTemp.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSetTemp.insertRow();

            //Insert records into rejecction data history table
            resultSetHistory.moveToInsertRow();
            resultSetHistory.updateInt("REVISION_NO", revisionNo);
            resultSetHistory.updateString("REJ_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("REJECTION_DATE").getString()));
            resultSetHistory.updateString("REJ_DOC_NO", getAttribute("REJECTION_DOCUMENT_NO").getString());
            resultSetHistory.updateString("REJ_FORM_NO", getAttribute("REJECTION_FORM_NO").getString());
            resultSetHistory.updateString("REJ_DEPT", getAttribute("REJECTION_DEPARTMENT").getString());
            resultSetHistory.updateString("PIECE_STAGE", getAttribute("PIECE_STAGE").getString());
            resultSetHistory.updateString("REJ_PIECE_NO", getAttribute("REJECTION_PIECE_NO").getString());
            resultSetHistory.updateString("REJ_NEW_PIECE_NO", getAttribute("REJECTION_NEW_PIECE_NO").getString());
            resultSetHistory.updateString("REJ_PARTY_CODE", getAttribute("REJECTION_PARTY_CODE").getString());
            resultSetHistory.updateString("STYLE", getAttribute("STYLE").getString());
            resultSetHistory.updateString("GROUP", getAttribute("GROUP").getString());
            resultSetHistory.updateFloat("SYN_PER", (float) EITLERPGLOBAL.round(getAttribute("SYN_PER").getVal(), 2));
            resultSetHistory.updateFloat("GSM", (float) EITLERPGLOBAL.round(getAttribute("GSM").getVal(), 2));
            resultSetHistory.updateString("MFG_SIZE", getAttribute("MFG_SIZE").getString());
            resultSetHistory.updateString("ACT_SIZE", getAttribute("ACTUAL_SIZE").getString());
            //resultSetHistory.updateFloat("REQ_WEIGHT", (float) EITLERPGLOBAL.round(getAttribute("REQUIRED_WEIGHT").getVal(), 2));
            //resultSetHistory.updateFloat("ACT_WEIGHT", (float) EITLERPGLOBAL.round(getAttribute("ACTUAL_WEIGHT").getVal(), 2));
            resultSetHistory.updateString("REASON", getAttribute("REASON").getString());
            resultSetHistory.updateString("COMMENT", getAttribute("COMMENT").getString());
            resultSetHistory.updateString("ACTION", getAttribute("ACTION").getString());

            resultSetHistory.updateInt("IS_PROCESS", getAttribute("IS_PROCESS").getInt());

            resultSetHistory.updateDouble("ORDER_LENGTH", (double) EITLERPGLOBAL.round(getAttribute("ORDER_LENGTH").getVal(), 2));
            resultSetHistory.updateDouble("ORDER_WIDTH", (double) EITLERPGLOBAL.round(getAttribute("ORDER_WIDTH").getVal(), 2));
            resultSetHistory.updateDouble("ORDER_GSM", (double) EITLERPGLOBAL.round(getAttribute("ORDER_GSM").getVal(), 2));
            resultSetHistory.updateDouble("ORDER_WEIGHT", (double) EITLERPGLOBAL.round(getAttribute("ORDER_WEIGHT").getVal(), 2));

            resultSetHistory.updateDouble("BILL_LENGTH", (double) EITLERPGLOBAL.round(getAttribute("BILL_LENGTH").getVal(), 2));
            resultSetHistory.updateDouble("BILL_WIDTH", (double) EITLERPGLOBAL.round(getAttribute("BILL_WIDTH").getVal(), 2));
            resultSetHistory.updateDouble("BILL_GSM", (double) EITLERPGLOBAL.round(getAttribute("BILL_GSM").getVal(), 2));
            resultSetHistory.updateDouble("BILL_WEIGHT", (double) EITLERPGLOBAL.round(getAttribute("BILL_WEIGHT").getVal(), 2));

            resultSetHistory.updateDouble("ADJUSTED_LENGTH", (double) EITLERPGLOBAL.round(getAttribute("ADJUSTED_LENGTH").getVal(), 2));
            resultSetHistory.updateDouble("ADJUSTED_WIDTH", (double) EITLERPGLOBAL.round(getAttribute("ADJUSTED_WIDTH").getVal(), 2));
            resultSetHistory.updateDouble("ADJUSTED_GSM", (double) EITLERPGLOBAL.round(getAttribute("ADJUSTED_GSM").getVal(), 2));
            resultSetHistory.updateDouble("ADJUSTED_WEIGHT", (double) EITLERPGLOBAL.round(getAttribute("ADJUSTED_WEIGHT").getVal(), 2));

            resultSetHistory.updateDouble("BASE_GSM", (double) EITLERPGLOBAL.round(getAttribute("BASE_GSM").getVal(), 2));
            resultSetHistory.updateDouble("WEB_GSM", (double) EITLERPGLOBAL.round(getAttribute("WEB_GSM").getVal(), 2));
            resultSetHistory.updateString("WEAVE", getAttribute("WEAVE").getString());
            resultSetHistory.updateString("CFM_TARGETED", getAttribute("CFM_TARGETED").getString());
            resultSetHistory.updateString("PAPER_PRODUCT_TYPE", getAttribute("PAPER_PRODUCT_TYPE").getString());
            resultSetHistory.updateString("REJECTION_LOCATION", getAttribute("REJECTION_LOCATION").getString());
            resultSetHistory.updateString("LOOM_NO", getAttribute("LOOM_NO").getString());
            resultSetHistory.updateString("OBSOLETE_UPN_ASSIGN_STATUS", getAttribute("OBSOLETE_UPN_ASSIGN_STATUS").getString());
            resultSetHistory.updateString("SCRAP_REASON", getAttribute("SCRAP_REASON").getString());
            resultSetHistory.updateString("UNMAPPED_REASON", getAttribute("UNMAPPED_REASON").getString());
            
            resultSetHistory.updateString("REJECTION_ORIGINATED_FROM", getAttribute("REJECTION_ORIGINATED_FROM").getString());

            resultSetHistory.updateInt("UPDATED_BY", (int) getAttribute("UPDATED_BY").getVal());
            resultSetHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSetHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            resultSetHistory.updateString("APPROVER_REMARKS", getAttribute("FROM_REMARKS").getString());
            resultSetHistory.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            resultSetHistory.updateInt("CHANGED", 1);
            resultSetHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSetHistory.insertRow();

            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = 764; //Felt Rejection Entry
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("REJECTION_DOCUMENT_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.FELT_REJECTION";
            ObjFeltProductionApprovalFlow.IsCreator = false;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "REJ_DOC_NO";

            if (getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFeltProductionApprovalFlow.To = (int) getAttribute("SEND_DOC_TO").getVal();
                ObjFeltProductionApprovalFlow.ExplicitSendTo = true;
            }

            //==== Handling Rejected Documents ==========//
            boolean IsRejected = getAttribute("REJECTED").getBool();

            if (IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.`FELT_REJECTION` SET REJECTED=0,CHANGED=1 WHERE REJ_DEPT='" + getAttribute("REJECTION_DEPARTMENT").getString() + "' AND REJ_DOC_NO='" + getAttribute("REJECTION_DOCUMENT_NO").getString() + "'");
                //Remove Old Records from FELT DOCUMET APPROVAL TABLE
                //data.Execute("DELETE FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=764 AND DOC_NO='"+getAttribute("REJECTION_DOCUMENT_NO").getString()+"'");

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

            // Update Rejection Date in Order Master Table To confirm that Rejection has completed
            if (ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {
                ObjFeltProductionApprovalFlow.finalApproved = false;
//                updateRejectionDate(getAttribute("REJECTION_DOCUMENT_NO").getString(), getAttribute("REJECTION_DEPARTMENT").getString());

                String PIECE_NO = getAttribute("REJECTION_PIECE_NO").getString();
                String NPIECE_NO = getAttribute("REJECTION_NEW_PIECE_NO").getString();
                String REMAKRS = getAttribute("REASON").getString();
                String InsertQuery = "", UpdtQry = "";

                if (data.getIntValueFromDB("SELECT COUNT(*) FROM PRODUCTION.FELT_WIP_PIECE_REGISTER WHERE WIP_PIECE_NO='" + PIECE_NO + "' AND WIP_EXT_PIECE_NO LIKE '%AB' AND COALESCE(WIP_REJECTED_FLAG,0)=0") > 0) {
                    PIECE_NO = data.getStringValueFromDB("SELECT WIP_EXT_PIECE_NO FROM PRODUCTION.FELT_WIP_PIECE_REGISTER WHERE WIP_PIECE_NO='" + PIECE_NO + "' AND WIP_EXT_PIECE_NO LIKE '%AB' AND COALESCE(WIP_REJECTED_FLAG,0)=0");
                }

                String updtpc = "", updtwpc = "";

                if (NPIECE_NO.trim().length() == 6) {
                    updtpc = PIECE_NO.substring(0, 5);
                } else {
                    updtpc = NPIECE_NO.trim().substring(0, NPIECE_NO.trim().length() - 1);
                }

                if ((PIECE_NO.contains("A") || PIECE_NO.contains("B")) && !PIECE_NO.contains("AB")) {
                    String str_split[] = PIECE_NO.split("-");
                    updtwpc = NPIECE_NO + "-" + str_split[1];
                    if (getAttribute("IS_PROCESS").getInt() == 0) {
                        
                        InsertQuery = "INSERT INTO PRODUCTION.FELT_SALES_PIECE_REGISTER (PR_PIECE_NO,PR_DATE,PR_ORDER_DATE,PR_DOC_NO,PR_MACHINE_NO,PR_POSITION_NO,PR_PARTY_CODE,PR_PRODUCT_CODE,PR_UPN,PR_GROUP,PR_STYLE,PR_LENGTH,PR_WIDTH,PR_GSM,PR_THORITICAL_WEIGHT,PR_SQMTR,PR_SYN_PER,PR_REQUESTED_MONTH,PR_REGION,PR_INCHARGE,PR_REFERENCE,PR_REFERENCE_DATE,PR_PO_NO,PR_PO_DATE,PR_ORDER_REMARK,PR_PIECE_REMARK,PR_PIECE_STAGE,PR_DIVERSION_FLAG,PR_BILL_LENGTH,PR_BILL_WIDTH,PR_BILL_GSM,PR_BILL_WEIGHT,PR_BILL_SQMTR,PR_BILL_PRODUCT_CODE,PR_BILL_STYLE,PR_WARP_DATE,PR_WVG_DATE,PR_MND_DATE,PR_NDL_DATE,PR_FNSG_DATE,PR_RCV_DATE,PR_PACKED_DATE,PR_EXP_DISPATCH_DATE,PR_INVOICE_DATE,PR_LR_DATE,PR_HOLD_DATE,PR_RELEASE_DATE,PR_PRIORITY_HOLD_CAN_FLAG,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,HIERARCHY_ID,APPROVER_BY,APPROVER_DATE,APPROVER_REMARK,PR_WIP_STATUS,PR_CURRENT_SCH_MONTH,PR_CURRENT_SCH_LAST_DDMMYY,PR_SP_MONTHYEAR,PR_SP_LAST_DDMMYY,PR_SP_REMARKS,PR_SPL_REQUEST_DATE,PR_SPL_REQUEST_MONTHYEAR,PR_SPL_REQUEST_REMARK,PR_REQ_MTH_LAST_DDMMYY,PR_OA_NO,PR_OA_DATE,PR_OC_NO,PR_OC_DATE,PR_OC_MONTHYEAR,PR_OC_LAST_DDMMYY,PR_CONTACT_PERSON,PR_EMAIL_ID,PR_TENDER_WEIGHT,PR_TENDER_GSM) SELECT '" + NPIECE_NO + "',PR_DATE,PR_ORDER_DATE,PR_DOC_NO,PR_MACHINE_NO,PR_POSITION_NO,PR_PARTY_CODE,PR_PRODUCT_CODE,PR_UPN,PR_GROUP,PR_STYLE,PR_LENGTH,PR_WIDTH,PR_GSM,PR_THORITICAL_WEIGHT,PR_SQMTR,PR_SYN_PER,PR_REQUESTED_MONTH,PR_REGION,PR_INCHARGE,PR_REFERENCE,PR_REFERENCE_DATE,PR_PO_NO,PR_PO_DATE,PR_ORDER_REMARK,PR_PIECE_REMARK,'CANCELED',PR_DIVERSION_FLAG,PR_BILL_LENGTH,PR_BILL_WIDTH,PR_BILL_GSM,PR_BILL_WEIGHT,PR_BILL_SQMTR,PR_BILL_PRODUCT_CODE,PR_BILL_STYLE,'0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','9',CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,HIERARCHY_ID,APPROVER_BY,APPROVER_DATE,APPROVER_REMARK,'CANCELED',PR_CURRENT_SCH_MONTH,PR_CURRENT_SCH_LAST_DDMMYY,PR_SP_MONTHYEAR,PR_SP_LAST_DDMMYY,PR_SP_REMARKS,PR_SPL_REQUEST_DATE,PR_SPL_REQUEST_MONTHYEAR,PR_SPL_REQUEST_REMARK,PR_REQ_MTH_LAST_DDMMYY,PR_OA_NO,PR_OA_DATE,PR_OC_NO,PR_OC_DATE,PR_OC_MONTHYEAR,PR_OC_LAST_DDMMYY,PR_CONTACT_PERSON,PR_EMAIL_ID,PR_TENDER_WEIGHT,PR_TENDER_GSM FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + updtpc + "'";
                        System.out.println("Insert Query :" + InsertQuery);
                        data.Execute(InsertQuery);

                        data.Execute("DROP TABLE PRODUCTION.TMP_FELT_WIP_PIECE_INSERT");
                        data.Execute("CREATE TABLE PRODUCTION.TMP_FELT_WIP_PIECE_INSERT SELECT * FROM PRODUCTION.FELT_WIP_PIECE_REGISTER WHERE WIP_EXT_PIECE_NO='" + PIECE_NO + "'  AND COALESCE(WIP_REJECTED_FLAG,0)=0");
                        UpdtQry = "UPDATE PRODUCTION.TMP_FELT_WIP_PIECE_INSERT "
                                + "SET WIP_PIECE_NO='" + NPIECE_NO + "',"
                                + "WIP_EXT_PIECE_NO='" + updtwpc + "',"
                                + "WIP_PIECE_STAGE='CANCELED',"
                                + "WIP_WARP_DATE='0000-00-00',WIP_WVG_DATE='0000-00-00',WIP_MND_DATE='0000-00-00',"
                                + "WIP_NDL_DATE='0000-00-00',WIP_FNSG_DATE='0000-00-00',"
                                + "WIP_EXP_DISPATCH_DATE='0000-00-00',"
                                + "WIP_HOLD_DATE='0000-00-00',"
                                + "WIP_RELEASE_DATE='0000-00-00',WIP_PRIORITY_HOLD_CAN_FLAG=9,"
                                + "WIP_WVG_LAYER_REMARK='',WIP_WEAVING_WEIGHT=0.0,WIP_WVG_A_DATE='0000-00-00',"
                                + "WIP_WVG_B_DATE='0000-00-00',WIP_WEAVING_WEIGHT_A=0.0,WIP_WEAVING_WEIGHT_B=0.0,"
                                + "WIP_MND_LAYER_REMARK='',WIP_MENDING_WEIGHT=0.0,WIP_MND_A_DATE='0000-00-00',"
                                + "WIP_MND_B_DATE='0000-00-00',WIP_MENDING_WEIGHT_A=0.0,WIP_MENDING_WEIGHT_B=0.0,"
                                + "WIP_NEEDLING_WEIGHT=0.0,WIP_SPLICE_WEIGHT=0.0,WIP_SEAM_WEIGHT=0.0,"
                                + "WIP_STATUS='CANCELED'";

                        data.Execute(UpdtQry);
                        data.Execute("INSERT INTO PRODUCTION.FELT_WIP_PIECE_REGISTER SELECT * FROM PRODUCTION.TMP_FELT_WIP_PIECE_INSERT");
                        if (PIECE_NO.contains("A")) {
                            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_PIECE_NO='" + NPIECE_NO + "' WHERE WIP_EXT_PIECE_NO='" + str_split[0] + "-B'");
                        }
                        if (PIECE_NO.contains("B")) {
                            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_PIECE_NO='" + NPIECE_NO + "' WHERE WIP_EXT_PIECE_NO='" + str_split[0] + "-A'");
                        }

                    } else {
                        InsertQuery = "INSERT INTO PRODUCTION.FELT_SALES_PIECE_REGISTER (PR_PIECE_NO,PR_DATE,PR_ORDER_DATE,PR_DOC_NO,PR_MACHINE_NO,PR_POSITION_NO,PR_PARTY_CODE,PR_PRODUCT_CODE,PR_UPN,PR_GROUP,PR_STYLE,PR_LENGTH,PR_WIDTH,PR_GSM,PR_THORITICAL_WEIGHT,PR_SQMTR,PR_SYN_PER,PR_REQUESTED_MONTH,PR_REGION,PR_INCHARGE,PR_REFERENCE,PR_REFERENCE_DATE,PR_PO_NO,PR_PO_DATE,PR_ORDER_REMARK,PR_PIECE_REMARK,PR_PIECE_STAGE,PR_DIVERSION_FLAG,PR_BILL_LENGTH,PR_BILL_WIDTH,PR_BILL_GSM,PR_BILL_WEIGHT,PR_BILL_SQMTR,PR_BILL_PRODUCT_CODE,PR_BILL_STYLE,PR_WARP_DATE,PR_WVG_DATE,PR_MND_DATE,PR_NDL_DATE,PR_FNSG_DATE,PR_RCV_DATE,PR_PACKED_DATE,PR_EXP_DISPATCH_DATE,PR_INVOICE_DATE,PR_LR_DATE,PR_HOLD_DATE,PR_RELEASE_DATE,PR_PRIORITY_HOLD_CAN_FLAG,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,HIERARCHY_ID,APPROVER_BY,APPROVER_DATE,APPROVER_REMARK,PR_WIP_STATUS,PR_CURRENT_SCH_MONTH,PR_CURRENT_SCH_LAST_DDMMYY,PR_SP_MONTHYEAR,PR_SP_LAST_DDMMYY,PR_SP_REMARKS,PR_SPL_REQUEST_DATE,PR_SPL_REQUEST_MONTHYEAR,PR_SPL_REQUEST_REMARK,PR_REQ_MTH_LAST_DDMMYY,PR_OA_NO,PR_OA_DATE,PR_OC_NO,PR_OC_DATE,PR_OC_MONTHYEAR,PR_OC_LAST_DDMMYY,PR_CONTACT_PERSON,PR_EMAIL_ID,PR_TENDER_WEIGHT,PR_TENDER_GSM) SELECT '" + NPIECE_NO + "',PR_DATE,PR_ORDER_DATE,PR_DOC_NO,PR_MACHINE_NO,PR_POSITION_NO,PR_PARTY_CODE,PR_PRODUCT_CODE,PR_UPN,PR_GROUP,PR_STYLE,PR_LENGTH,PR_WIDTH,PR_GSM,PR_THORITICAL_WEIGHT,PR_SQMTR,PR_SYN_PER,PR_REQUESTED_MONTH,PR_REGION,PR_INCHARGE,PR_REFERENCE,PR_REFERENCE_DATE,PR_PO_NO,PR_PO_DATE,PR_ORDER_REMARK,PR_PIECE_REMARK,'PLANNING',PR_DIVERSION_FLAG,PR_BILL_LENGTH,PR_BILL_WIDTH,PR_BILL_GSM,PR_BILL_WEIGHT,PR_BILL_SQMTR,PR_BILL_PRODUCT_CODE,PR_BILL_STYLE,'0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','1',CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,HIERARCHY_ID,APPROVER_BY,APPROVER_DATE,APPROVER_REMARK,'ORDERED',PR_CURRENT_SCH_MONTH,PR_CURRENT_SCH_LAST_DDMMYY,PR_SP_MONTHYEAR,PR_SP_LAST_DDMMYY,PR_SP_REMARKS,PR_SPL_REQUEST_DATE,PR_SPL_REQUEST_MONTHYEAR,PR_SPL_REQUEST_REMARK,PR_REQ_MTH_LAST_DDMMYY,PR_OA_NO,PR_OA_DATE,PR_OC_NO,PR_OC_DATE,PR_OC_MONTHYEAR,PR_OC_LAST_DDMMYY,PR_CONTACT_PERSON,PR_EMAIL_ID,PR_TENDER_WEIGHT,PR_TENDER_GSM FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + updtpc + "'";
                        System.out.println("Insert Query :" + InsertQuery);
                        data.Execute(InsertQuery);
                        data.Execute("DROP TABLE PRODUCTION.TMP_FELT_WIP_PIECE_INSERT");
                        data.Execute("CREATE TABLE PRODUCTION.TMP_FELT_WIP_PIECE_INSERT SELECT * FROM PRODUCTION.FELT_WIP_PIECE_REGISTER WHERE WIP_EXT_PIECE_NO='" + PIECE_NO + "' AND COALESCE(WIP_REJECTED_FLAG,0)=0");
                        UpdtQry = "UPDATE PRODUCTION.TMP_FELT_WIP_PIECE_INSERT "
                                + "SET WIP_PIECE_NO='" + NPIECE_NO + "',"
                                + "WIP_EXT_PIECE_NO='" + updtwpc + "',"
                                + "WIP_PIECE_STAGE='PLANNING',"
                                + "WIP_WARP_DATE='0000-00-00',WIP_WVG_DATE='0000-00-00',WIP_MND_DATE='0000-00-00',"
                                + "WIP_NDL_DATE='0000-00-00',WIP_FNSG_DATE='0000-00-00',"
                                + "WIP_EXP_DISPATCH_DATE='0000-00-00',"
                                + "WIP_HOLD_DATE='0000-00-00',"
                                + "WIP_RELEASE_DATE='0000-00-00',WIP_PRIORITY_HOLD_CAN_FLAG=0,"
                                + "WIP_WVG_LAYER_REMARK='',WIP_WEAVING_WEIGHT=0.0,WIP_WVG_A_DATE='0000-00-00',"
                                + "WIP_WVG_B_DATE='0000-00-00',WIP_WEAVING_WEIGHT_A=0.0,WIP_WEAVING_WEIGHT_B=0.0,"
                                + "WIP_MND_LAYER_REMARK='',WIP_MENDING_WEIGHT=0.0,WIP_MND_A_DATE='0000-00-00',"
                                + "WIP_MND_B_DATE='0000-00-00',WIP_MENDING_WEIGHT_A=0.0,WIP_MENDING_WEIGHT_B=0.0,"
                                + "WIP_NEEDLING_WEIGHT=0.0,WIP_SPLICE_WEIGHT=0.0,WIP_SEAM_WEIGHT=0.0,"
                                + "WIP_STATUS='ORDERED'";
                        data.Execute(UpdtQry);
                        data.Execute("INSERT INTO PRODUCTION.FELT_WIP_PIECE_REGISTER SELECT * FROM PRODUCTION.TMP_FELT_WIP_PIECE_INSERT");
                        if (PIECE_NO.contains("A")) {
                            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_PIECE_NO='" + NPIECE_NO + "' WHERE WIP_EXT_PIECE_NO='" + str_split[0] + "-B'");
                            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_PIECE_NO='" + NPIECE_NO + "' WHERE WIP_EXT_PIECE_NO='" + str_split[0] + "-AB'");
                        }
                        if (PIECE_NO.contains("B")) {
                            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_PIECE_NO='" + NPIECE_NO + "' WHERE WIP_EXT_PIECE_NO='" + str_split[0] + "-A'");
                            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_PIECE_NO='" + NPIECE_NO + "' WHERE WIP_EXT_PIECE_NO='" + str_split[0] + "-AB'");
                        }
                    }
                } else if (PIECE_NO.contains("AB")) {
                    String str_split[] = PIECE_NO.split("-");
                    updtwpc = NPIECE_NO + "-" + str_split[1];
                    if (getAttribute("IS_PROCESS").getInt() == 0) {
                        InsertQuery = "INSERT INTO PRODUCTION.FELT_SALES_PIECE_REGISTER (PR_PIECE_NO,PR_DATE,PR_ORDER_DATE,PR_DOC_NO,PR_MACHINE_NO,PR_POSITION_NO,PR_PARTY_CODE,PR_PRODUCT_CODE,PR_UPN,PR_GROUP,PR_STYLE,PR_LENGTH,PR_WIDTH,PR_GSM,PR_THORITICAL_WEIGHT,PR_SQMTR,PR_SYN_PER,PR_REQUESTED_MONTH,PR_REGION,PR_INCHARGE,PR_REFERENCE,PR_REFERENCE_DATE,PR_PO_NO,PR_PO_DATE,PR_ORDER_REMARK,PR_PIECE_REMARK,PR_PIECE_STAGE,PR_DIVERSION_FLAG,PR_BILL_LENGTH,PR_BILL_WIDTH,PR_BILL_GSM,PR_BILL_WEIGHT,PR_BILL_SQMTR,PR_BILL_PRODUCT_CODE,PR_BILL_STYLE,PR_WARP_DATE,PR_WVG_DATE,PR_MND_DATE,PR_NDL_DATE,PR_FNSG_DATE,PR_RCV_DATE,PR_PACKED_DATE,PR_EXP_DISPATCH_DATE,PR_INVOICE_DATE,PR_LR_DATE,PR_HOLD_DATE,PR_RELEASE_DATE,PR_PRIORITY_HOLD_CAN_FLAG,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,HIERARCHY_ID,APPROVER_BY,APPROVER_DATE,APPROVER_REMARK,PR_WIP_STATUS,PR_CURRENT_SCH_MONTH,PR_CURRENT_SCH_LAST_DDMMYY,PR_SP_MONTHYEAR,PR_SP_LAST_DDMMYY,PR_SP_REMARKS,PR_SPL_REQUEST_DATE,PR_SPL_REQUEST_MONTHYEAR,PR_SPL_REQUEST_REMARK,PR_REQ_MTH_LAST_DDMMYY,PR_OA_NO,PR_OA_DATE,PR_OC_NO,PR_OC_DATE,PR_OC_MONTHYEAR,PR_OC_LAST_DDMMYY,PR_CONTACT_PERSON,PR_EMAIL_ID,PR_TENDER_WEIGHT,PR_TENDER_GSM) SELECT '" + NPIECE_NO + "',PR_DATE,PR_ORDER_DATE,PR_DOC_NO,PR_MACHINE_NO,PR_POSITION_NO,PR_PARTY_CODE,PR_PRODUCT_CODE,PR_UPN,PR_GROUP,PR_STYLE,PR_LENGTH,PR_WIDTH,PR_GSM,PR_THORITICAL_WEIGHT,PR_SQMTR,PR_SYN_PER,PR_REQUESTED_MONTH,PR_REGION,PR_INCHARGE,PR_REFERENCE,PR_REFERENCE_DATE,PR_PO_NO,PR_PO_DATE,PR_ORDER_REMARK,PR_PIECE_REMARK,'CANCELED',PR_DIVERSION_FLAG,PR_BILL_LENGTH,PR_BILL_WIDTH,PR_BILL_GSM,PR_BILL_WEIGHT,PR_BILL_SQMTR,PR_BILL_PRODUCT_CODE,PR_BILL_STYLE,'0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','9',CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,HIERARCHY_ID,APPROVER_BY,APPROVER_DATE,APPROVER_REMARK,'CANCELED',PR_CURRENT_SCH_MONTH,PR_CURRENT_SCH_LAST_DDMMYY,PR_SP_MONTHYEAR,PR_SP_LAST_DDMMYY,PR_SP_REMARKS,PR_SPL_REQUEST_DATE,PR_SPL_REQUEST_MONTHYEAR,PR_SPL_REQUEST_REMARK,PR_REQ_MTH_LAST_DDMMYY,PR_OA_NO,PR_OA_DATE,PR_OC_NO,PR_OC_DATE,PR_OC_MONTHYEAR,PR_OC_LAST_DDMMYY,PR_CONTACT_PERSON,PR_EMAIL_ID,PR_TENDER_WEIGHT,PR_TENDER_GSM FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + updtpc + "'";
                        System.out.println("Insert Query :" + InsertQuery);
                        data.Execute(InsertQuery);

                        data.Execute("DROP TABLE PRODUCTION.TMP_FELT_WIP_PIECE_INSERT");
                        data.Execute("CREATE TABLE PRODUCTION.TMP_FELT_WIP_PIECE_INSERT SELECT * FROM PRODUCTION.FELT_WIP_PIECE_REGISTER WHERE WIP_EXT_PIECE_NO='" + PIECE_NO + "' AND COALESCE(WIP_REJECTED_FLAG,0)=0");
                        UpdtQry = "UPDATE PRODUCTION.TMP_FELT_WIP_PIECE_INSERT "
                                + "SET WIP_PIECE_NO='" + NPIECE_NO + "',"
                                + "WIP_EXT_PIECE_NO='" + updtwpc + "',"
                                + "WIP_PIECE_STAGE='CANCELED',"
                                + "WIP_WARP_DATE='0000-00-00',WIP_WVG_DATE='0000-00-00',WIP_MND_DATE='0000-00-00',"
                                + "WIP_NDL_DATE='0000-00-00',WIP_FNSG_DATE='0000-00-00',"
                                + "WIP_EXP_DISPATCH_DATE='0000-00-00',"
                                + "WIP_HOLD_DATE='0000-00-00',"
                                + "WIP_RELEASE_DATE='0000-00-00',WIP_PRIORITY_HOLD_CAN_FLAG=9,"
                                + "WIP_WVG_LAYER_REMARK='',WIP_WEAVING_WEIGHT=0.0,WIP_WVG_A_DATE='0000-00-00',"
                                + "WIP_WVG_B_DATE='0000-00-00',WIP_WEAVING_WEIGHT_A=0.0,WIP_WEAVING_WEIGHT_B=0.0,"
                                + "WIP_MND_LAYER_REMARK='',WIP_MENDING_WEIGHT=0.0,WIP_MND_A_DATE='0000-00-00',"
                                + "WIP_MND_B_DATE='0000-00-00',WIP_MENDING_WEIGHT_A=0.0,WIP_MENDING_WEIGHT_B=0.0,"
                                + "WIP_NEEDLING_WEIGHT=0.0,WIP_SPLICE_WEIGHT=0.0,WIP_SEAM_WEIGHT=0.0,"
                                + "WIP_STATUS='CANCELED'";
                        System.out.println("Update:" + UpdtQry);
                        data.Execute(UpdtQry);
                        data.Execute("INSERT INTO PRODUCTION.FELT_WIP_PIECE_REGISTER SELECT * FROM PRODUCTION.TMP_FELT_WIP_PIECE_INSERT");

                    } else {
                        InsertQuery = "INSERT INTO PRODUCTION.FELT_SALES_PIECE_REGISTER (PR_PIECE_NO,PR_DATE,PR_ORDER_DATE,PR_DOC_NO,PR_MACHINE_NO,PR_POSITION_NO,PR_PARTY_CODE,PR_PRODUCT_CODE,PR_UPN,PR_GROUP,PR_STYLE,PR_LENGTH,PR_WIDTH,PR_GSM,PR_THORITICAL_WEIGHT,PR_SQMTR,PR_SYN_PER,PR_REQUESTED_MONTH,PR_REGION,PR_INCHARGE,PR_REFERENCE,PR_REFERENCE_DATE,PR_PO_NO,PR_PO_DATE,PR_ORDER_REMARK,PR_PIECE_REMARK,PR_PIECE_STAGE,PR_DIVERSION_FLAG,PR_BILL_LENGTH,PR_BILL_WIDTH,PR_BILL_GSM,PR_BILL_WEIGHT,PR_BILL_SQMTR,PR_BILL_PRODUCT_CODE,PR_BILL_STYLE,PR_WARP_DATE,PR_WVG_DATE,PR_MND_DATE,PR_NDL_DATE,PR_FNSG_DATE,PR_RCV_DATE,PR_PACKED_DATE,PR_EXP_DISPATCH_DATE,PR_INVOICE_DATE,PR_LR_DATE,PR_HOLD_DATE,PR_RELEASE_DATE,PR_PRIORITY_HOLD_CAN_FLAG,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,HIERARCHY_ID,APPROVER_BY,APPROVER_DATE,APPROVER_REMARK,PR_WIP_STATUS,PR_CURRENT_SCH_MONTH,PR_CURRENT_SCH_LAST_DDMMYY,PR_SP_MONTHYEAR,PR_SP_LAST_DDMMYY,PR_SP_REMARKS,PR_SPL_REQUEST_DATE,PR_SPL_REQUEST_MONTHYEAR,PR_SPL_REQUEST_REMARK,PR_REQ_MTH_LAST_DDMMYY,PR_OA_NO,PR_OA_DATE,PR_OC_NO,PR_OC_DATE,PR_OC_MONTHYEAR,PR_OC_LAST_DDMMYY,PR_CONTACT_PERSON,PR_EMAIL_ID,PR_TENDER_WEIGHT,PR_TENDER_GSM) SELECT '" + NPIECE_NO + "',PR_DATE,PR_ORDER_DATE,PR_DOC_NO,PR_MACHINE_NO,PR_POSITION_NO,PR_PARTY_CODE,PR_PRODUCT_CODE,PR_UPN,PR_GROUP,PR_STYLE,PR_LENGTH,PR_WIDTH,PR_GSM,PR_THORITICAL_WEIGHT,PR_SQMTR,PR_SYN_PER,PR_REQUESTED_MONTH,PR_REGION,PR_INCHARGE,PR_REFERENCE,PR_REFERENCE_DATE,PR_PO_NO,PR_PO_DATE,PR_ORDER_REMARK,PR_PIECE_REMARK,'PLANNING',PR_DIVERSION_FLAG,PR_BILL_LENGTH,PR_BILL_WIDTH,PR_BILL_GSM,PR_BILL_WEIGHT,PR_BILL_SQMTR,PR_BILL_PRODUCT_CODE,PR_BILL_STYLE,'0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','1',CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,HIERARCHY_ID,APPROVER_BY,APPROVER_DATE,APPROVER_REMARK,'ORDERED',PR_CURRENT_SCH_MONTH,PR_CURRENT_SCH_LAST_DDMMYY,PR_SP_MONTHYEAR,PR_SP_LAST_DDMMYY,PR_SP_REMARKS,PR_SPL_REQUEST_DATE,PR_SPL_REQUEST_MONTHYEAR,PR_SPL_REQUEST_REMARK,PR_REQ_MTH_LAST_DDMMYY,PR_OA_NO,PR_OA_DATE,PR_OC_NO,PR_OC_DATE,PR_OC_MONTHYEAR,PR_OC_LAST_DDMMYY,PR_CONTACT_PERSON,PR_EMAIL_ID,PR_TENDER_WEIGHT,PR_TENDER_GSM FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + updtpc + "'";
                        System.out.println("Insert Query :" + InsertQuery);
                        data.Execute(InsertQuery);
                        data.Execute("DROP TABLE PRODUCTION.TMP_FELT_WIP_PIECE_INSERT");
                        data.Execute("CREATE TABLE PRODUCTION.TMP_FELT_WIP_PIECE_INSERT SELECT * FROM PRODUCTION.FELT_WIP_PIECE_REGISTER WHERE WIP_EXT_PIECE_NO='" + PIECE_NO + "'  AND COALESCE(WIP_REJECTED_FLAG,0)=0");
                        UpdtQry = "UPDATE PRODUCTION.TMP_FELT_WIP_PIECE_INSERT "
                                + "SET WIP_PIECE_NO='" + NPIECE_NO + "',"
                                + "WIP_EXT_PIECE_NO='" + updtwpc + "',"
                                + "WIP_PIECE_STAGE='PLANNING',"
                                + "WIP_WARP_DATE='0000-00-00',WIP_WVG_DATE='0000-00-00',WIP_MND_DATE='0000-00-00',"
                                + "WIP_NDL_DATE='0000-00-00',WIP_FNSG_DATE='0000-00-00',"
                                + "WIP_EXP_DISPATCH_DATE='0000-00-00',"
                                + "WIP_HOLD_DATE='0000-00-00',"
                                + "WIP_RELEASE_DATE='0000-00-00',WIP_PRIORITY_HOLD_CAN_FLAG=1,"
                                + "WIP_WVG_LAYER_REMARK='',WIP_WEAVING_WEIGHT=0.0,WIP_WVG_A_DATE='0000-00-00',"
                                + "WIP_WVG_B_DATE='0000-00-00',WIP_WEAVING_WEIGHT_A=0.0,WIP_WEAVING_WEIGHT_B=0.0,"
                                + "WIP_MND_LAYER_REMARK='',WIP_MENDING_WEIGHT=0.0,WIP_MND_A_DATE='0000-00-00',"
                                + "WIP_MND_B_DATE='0000-00-00',WIP_MENDING_WEIGHT_A=0.0,WIP_MENDING_WEIGHT_B=0.0,"
                                + "WIP_NEEDLING_WEIGHT=0.0,WIP_SPLICE_WEIGHT=0.0,WIP_SEAM_WEIGHT=0.0,"
                                + "WIP_STATUS='ORDERED'";
                        System.out.println("Update:" + UpdtQry);
                        data.Execute(UpdtQry);
                        data.Execute("INSERT INTO PRODUCTION.FELT_WIP_PIECE_REGISTER SELECT * FROM PRODUCTION.TMP_FELT_WIP_PIECE_INSERT");

                        data.Execute("DROP TABLE PRODUCTION.TMP_FELT_WIP_PIECE_INSERT");
                        data.Execute("CREATE TABLE PRODUCTION.TMP_FELT_WIP_PIECE_INSERT SELECT * FROM PRODUCTION.FELT_WIP_PIECE_REGISTER WHERE WIP_EXT_PIECE_NO='" + str_split[0] + "-A' AND COALESCE(WIP_REJECTED_FLAG,0)=0");
                        UpdtQry = "UPDATE PRODUCTION.TMP_FELT_WIP_PIECE_INSERT "
                                + "SET WIP_PIECE_NO='" + NPIECE_NO + "',"
                                + "WIP_EXT_PIECE_NO='" + updtpc + "R-A',"
                                + "WIP_PIECE_STAGE='PLANNING',"
                                + "WIP_WARP_DATE='0000-00-00',WIP_WVG_DATE='0000-00-00',WIP_MND_DATE='0000-00-00',"
                                + "WIP_NDL_DATE='0000-00-00',WIP_FNSG_DATE='0000-00-00',"
                                + "WIP_EXP_DISPATCH_DATE='0000-00-00',"
                                + "WIP_HOLD_DATE='0000-00-00',"
                                + "WIP_RELEASE_DATE='0000-00-00',WIP_PRIORITY_HOLD_CAN_FLAG=0,"
                                + "WIP_WVG_LAYER_REMARK='',WIP_WEAVING_WEIGHT=0.0,WIP_WVG_A_DATE='0000-00-00',"
                                + "WIP_WVG_B_DATE='0000-00-00',WIP_WEAVING_WEIGHT_A=0.0,WIP_WEAVING_WEIGHT_B=0.0,"
                                + "WIP_MND_LAYER_REMARK='',WIP_MENDING_WEIGHT=0.0,WIP_MND_A_DATE='0000-00-00',"
                                + "WIP_MND_B_DATE='0000-00-00',WIP_MENDING_WEIGHT_A=0.0,WIP_MENDING_WEIGHT_B=0.0,"
                                + "WIP_NEEDLING_WEIGHT=0.0,WIP_SPLICE_WEIGHT=0.0,WIP_SEAM_WEIGHT=0.0,"
                                + "WIP_STATUS='ORDERED'";
                        System.out.println("Update:" + UpdtQry);
                        data.Execute(UpdtQry);
                        data.Execute("INSERT INTO PRODUCTION.FELT_WIP_PIECE_REGISTER SELECT * FROM PRODUCTION.TMP_FELT_WIP_PIECE_INSERT");

                        data.Execute("DROP TABLE PRODUCTION.TMP_FELT_WIP_PIECE_INSERT");
                        data.Execute("CREATE TABLE PRODUCTION.TMP_FELT_WIP_PIECE_INSERT SELECT * FROM PRODUCTION.FELT_WIP_PIECE_REGISTER WHERE WIP_EXT_PIECE_NO='" + str_split[0] + "-B' AND COALESCE(WIP_REJECTED_FLAG,0)=0");
                        UpdtQry = "UPDATE PRODUCTION.TMP_FELT_WIP_PIECE_INSERT "
                                + "SET WIP_PIECE_NO='" + NPIECE_NO + "',"
                                + "WIP_EXT_PIECE_NO='" + updtpc + "R-B',"
                                + "WIP_PIECE_STAGE='PLANNING',"
                                + "WIP_WARP_DATE='0000-00-00',WIP_WVG_DATE='0000-00-00',WIP_MND_DATE='0000-00-00',"
                                + "WIP_NDL_DATE='0000-00-00',WIP_FNSG_DATE='0000-00-00',"
                                + "WIP_EXP_DISPATCH_DATE='0000-00-00',"
                                + "WIP_HOLD_DATE='0000-00-00',"
                                + "WIP_RELEASE_DATE='0000-00-00',WIP_PRIORITY_HOLD_CAN_FLAG=0,"
                                + "WIP_WVG_LAYER_REMARK='',WIP_WEAVING_WEIGHT=0.0,WIP_WVG_A_DATE='0000-00-00',"
                                + "WIP_WVG_B_DATE='0000-00-00',WIP_WEAVING_WEIGHT_A=0.0,WIP_WEAVING_WEIGHT_B=0.0,"
                                + "WIP_MND_LAYER_REMARK='',WIP_MENDING_WEIGHT=0.0,WIP_MND_A_DATE='0000-00-00',"
                                + "WIP_MND_B_DATE='0000-00-00',WIP_MENDING_WEIGHT_A=0.0,WIP_MENDING_WEIGHT_B=0.0,"
                                + "WIP_NEEDLING_WEIGHT=0.0,WIP_SPLICE_WEIGHT=0.0,WIP_SEAM_WEIGHT=0.0,"
                                + "WIP_STATUS='ORDERED'";
                        data.Execute(UpdtQry);
                        data.Execute("INSERT INTO PRODUCTION.FELT_WIP_PIECE_REGISTER SELECT * FROM PRODUCTION.TMP_FELT_WIP_PIECE_INSERT");

                    }

                } else {
                    updtwpc = NPIECE_NO;
                    if (getAttribute("IS_PROCESS").getInt() == 0) {
                        InsertQuery = "INSERT INTO PRODUCTION.FELT_SALES_PIECE_REGISTER (PR_PIECE_NO,PR_DATE,PR_ORDER_DATE,PR_DOC_NO,PR_MACHINE_NO,PR_POSITION_NO,PR_PARTY_CODE,PR_PRODUCT_CODE,PR_UPN,PR_GROUP,PR_STYLE,PR_LENGTH,PR_WIDTH,PR_GSM,PR_THORITICAL_WEIGHT,PR_SQMTR,PR_SYN_PER,PR_REQUESTED_MONTH,PR_REGION,PR_INCHARGE,PR_REFERENCE,PR_REFERENCE_DATE,PR_PO_NO,PR_PO_DATE,PR_ORDER_REMARK,PR_PIECE_REMARK,PR_PIECE_STAGE,PR_DIVERSION_FLAG,PR_BILL_LENGTH,PR_BILL_WIDTH,PR_BILL_GSM,PR_BILL_WEIGHT,PR_BILL_SQMTR,PR_BILL_PRODUCT_CODE,PR_BILL_STYLE,PR_WARP_DATE,PR_WVG_DATE,PR_MND_DATE,PR_NDL_DATE,PR_FNSG_DATE,PR_RCV_DATE,PR_PACKED_DATE,PR_EXP_DISPATCH_DATE,PR_INVOICE_DATE,PR_LR_DATE,PR_HOLD_DATE,PR_RELEASE_DATE,PR_PRIORITY_HOLD_CAN_FLAG,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,HIERARCHY_ID,APPROVER_BY,APPROVER_DATE,APPROVER_REMARK,PR_WIP_STATUS,PR_CURRENT_SCH_MONTH,PR_CURRENT_SCH_LAST_DDMMYY,PR_SP_MONTHYEAR,PR_SP_LAST_DDMMYY,PR_SP_REMARKS,PR_SPL_REQUEST_DATE,PR_SPL_REQUEST_MONTHYEAR,PR_SPL_REQUEST_REMARK,PR_REQ_MTH_LAST_DDMMYY,PR_OA_NO,PR_OA_DATE,PR_OC_NO,PR_OC_DATE,PR_OC_MONTHYEAR,PR_OC_LAST_DDMMYY,PR_CONTACT_PERSON,PR_EMAIL_ID,PR_TENDER_WEIGHT,PR_TENDER_GSM) SELECT '" + NPIECE_NO + "',PR_DATE,PR_ORDER_DATE,PR_DOC_NO,PR_MACHINE_NO,PR_POSITION_NO,PR_PARTY_CODE,PR_PRODUCT_CODE,PR_UPN,PR_GROUP,PR_STYLE,PR_LENGTH,PR_WIDTH,PR_GSM,PR_THORITICAL_WEIGHT,PR_SQMTR,PR_SYN_PER,PR_REQUESTED_MONTH,PR_REGION,PR_INCHARGE,PR_REFERENCE,PR_REFERENCE_DATE,PR_PO_NO,PR_PO_DATE,PR_ORDER_REMARK,PR_PIECE_REMARK,'CANCELED',PR_DIVERSION_FLAG,PR_BILL_LENGTH,PR_BILL_WIDTH,PR_BILL_GSM,PR_BILL_WEIGHT,PR_BILL_SQMTR,PR_BILL_PRODUCT_CODE,PR_BILL_STYLE,'0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','9',CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,HIERARCHY_ID,APPROVER_BY,APPROVER_DATE,APPROVER_REMARK,'CANCELED',PR_CURRENT_SCH_MONTH,PR_CURRENT_SCH_LAST_DDMMYY,PR_SP_MONTHYEAR,PR_SP_LAST_DDMMYY,PR_SP_REMARKS,PR_SPL_REQUEST_DATE,PR_SPL_REQUEST_MONTHYEAR,PR_SPL_REQUEST_REMARK,PR_REQ_MTH_LAST_DDMMYY,PR_OA_NO,PR_OA_DATE,PR_OC_NO,PR_OC_DATE,PR_OC_MONTHYEAR,PR_OC_LAST_DDMMYY,PR_CONTACT_PERSON,PR_EMAIL_ID,PR_TENDER_WEIGHT,PR_TENDER_GSM FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + updtpc + "'";
                        System.out.println("Insert Query :" + InsertQuery);
                        data.Execute(InsertQuery);

                        data.Execute("DROP TABLE PRODUCTION.TMP_FELT_WIP_PIECE_INSERT");
                        data.Execute("CREATE TABLE PRODUCTION.TMP_FELT_WIP_PIECE_INSERT SELECT * FROM PRODUCTION.FELT_WIP_PIECE_REGISTER WHERE WIP_EXT_PIECE_NO='" + PIECE_NO + "'  AND COALESCE(WIP_REJECTED_FLAG,0)=0");
                        UpdtQry = "UPDATE PRODUCTION.TMP_FELT_WIP_PIECE_INSERT "
                                + "SET WIP_PIECE_NO='" + NPIECE_NO + "',"
                                + "WIP_EXT_PIECE_NO='" + updtwpc + "',"
                                + "WIP_PIECE_STAGE='CANCELED',"
                                + "WIP_WARP_DATE='0000-00-00',WIP_WVG_DATE='0000-00-00',WIP_MND_DATE='0000-00-00',"
                                + "WIP_NDL_DATE='0000-00-00',WIP_FNSG_DATE='0000-00-00',"
                                + "WIP_EXP_DISPATCH_DATE='0000-00-00',"
                                + "WIP_HOLD_DATE='0000-00-00',"
                                + "WIP_RELEASE_DATE='0000-00-00',WIP_PRIORITY_HOLD_CAN_FLAG=9,"
                                + "WIP_WVG_LAYER_REMARK='',WIP_WEAVING_WEIGHT=0.0,WIP_WVG_A_DATE='0000-00-00',"
                                + "WIP_WVG_B_DATE='0000-00-00',WIP_WEAVING_WEIGHT_A=0.0,WIP_WEAVING_WEIGHT_B=0.0,"
                                + "WIP_MND_LAYER_REMARK='',WIP_MENDING_WEIGHT=0.0,WIP_MND_A_DATE='0000-00-00',"
                                + "WIP_MND_B_DATE='0000-00-00',WIP_MENDING_WEIGHT_A=0.0,WIP_MENDING_WEIGHT_B=0.0,"
                                + "WIP_NEEDLING_WEIGHT=0.0,WIP_SPLICE_WEIGHT=0.0,WIP_SEAM_WEIGHT=0.0,"
                                + "WIP_STATUS='CANCELED'";
                        data.Execute(UpdtQry);
                        data.Execute("INSERT INTO PRODUCTION.FELT_WIP_PIECE_REGISTER SELECT * FROM PRODUCTION.TMP_FELT_WIP_PIECE_INSERT");

                    } else {
                        InsertQuery = "INSERT INTO PRODUCTION.FELT_SALES_PIECE_REGISTER (PR_PIECE_NO,PR_DATE,PR_ORDER_DATE,PR_DOC_NO,PR_MACHINE_NO,PR_POSITION_NO,PR_PARTY_CODE,PR_PRODUCT_CODE,PR_UPN,PR_GROUP,PR_STYLE,PR_LENGTH,PR_WIDTH,PR_GSM,PR_THORITICAL_WEIGHT,PR_SQMTR,PR_SYN_PER,PR_REQUESTED_MONTH,PR_REGION,PR_INCHARGE,PR_REFERENCE,PR_REFERENCE_DATE,PR_PO_NO,PR_PO_DATE,PR_ORDER_REMARK,PR_PIECE_REMARK,PR_PIECE_STAGE,PR_DIVERSION_FLAG,PR_BILL_LENGTH,PR_BILL_WIDTH,PR_BILL_GSM,PR_BILL_WEIGHT,PR_BILL_SQMTR,PR_BILL_PRODUCT_CODE,PR_BILL_STYLE,PR_WARP_DATE,PR_WVG_DATE,PR_MND_DATE,PR_NDL_DATE,PR_FNSG_DATE,PR_RCV_DATE,PR_PACKED_DATE,PR_EXP_DISPATCH_DATE,PR_INVOICE_DATE,PR_LR_DATE,PR_HOLD_DATE,PR_RELEASE_DATE,PR_PRIORITY_HOLD_CAN_FLAG,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,HIERARCHY_ID,APPROVER_BY,APPROVER_DATE,APPROVER_REMARK,PR_WIP_STATUS,PR_CURRENT_SCH_MONTH,PR_CURRENT_SCH_LAST_DDMMYY,PR_SP_MONTHYEAR,PR_SP_LAST_DDMMYY,PR_SP_REMARKS,PR_SPL_REQUEST_DATE,PR_SPL_REQUEST_MONTHYEAR,PR_SPL_REQUEST_REMARK,PR_REQ_MTH_LAST_DDMMYY,PR_OA_NO,PR_OA_DATE,PR_OC_NO,PR_OC_DATE,PR_OC_MONTHYEAR,PR_OC_LAST_DDMMYY,PR_CONTACT_PERSON,PR_EMAIL_ID,PR_TENDER_WEIGHT,PR_TENDER_GSM) SELECT '" + NPIECE_NO + "',PR_DATE,PR_ORDER_DATE,PR_DOC_NO,PR_MACHINE_NO,PR_POSITION_NO,PR_PARTY_CODE,PR_PRODUCT_CODE,PR_UPN,PR_GROUP,PR_STYLE,PR_LENGTH,PR_WIDTH,PR_GSM,PR_THORITICAL_WEIGHT,PR_SQMTR,PR_SYN_PER,PR_REQUESTED_MONTH,PR_REGION,PR_INCHARGE,PR_REFERENCE,PR_REFERENCE_DATE,PR_PO_NO,PR_PO_DATE,PR_ORDER_REMARK,PR_PIECE_REMARK,'PLANNING',PR_DIVERSION_FLAG,PR_BILL_LENGTH,PR_BILL_WIDTH,PR_BILL_GSM,PR_BILL_WEIGHT,PR_BILL_SQMTR,PR_BILL_PRODUCT_CODE,PR_BILL_STYLE,'0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','0000-00-00','1',CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,HIERARCHY_ID,APPROVER_BY,APPROVER_DATE,APPROVER_REMARK,'ORDERED',PR_CURRENT_SCH_MONTH,PR_CURRENT_SCH_LAST_DDMMYY,PR_SP_MONTHYEAR,PR_SP_LAST_DDMMYY,PR_SP_REMARKS,PR_SPL_REQUEST_DATE,PR_SPL_REQUEST_MONTHYEAR,PR_SPL_REQUEST_REMARK,PR_REQ_MTH_LAST_DDMMYY,PR_OA_NO,PR_OA_DATE,PR_OC_NO,PR_OC_DATE,PR_OC_MONTHYEAR,PR_OC_LAST_DDMMYY,PR_CONTACT_PERSON,PR_EMAIL_ID,PR_TENDER_WEIGHT,PR_TENDER_GSM FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + updtpc + "'";
                        System.out.println("Insert Query :" + InsertQuery);
                        data.Execute(InsertQuery);
                        data.Execute("DROP TABLE PRODUCTION.TMP_FELT_WIP_PIECE_INSERT");
                        data.Execute("CREATE TABLE PRODUCTION.TMP_FELT_WIP_PIECE_INSERT SELECT * FROM PRODUCTION.FELT_WIP_PIECE_REGISTER WHERE WIP_EXT_PIECE_NO='" + PIECE_NO + "'  AND COALESCE(WIP_REJECTED_FLAG,0)=0");
                        UpdtQry = "UPDATE PRODUCTION.TMP_FELT_WIP_PIECE_INSERT "
                                + "SET WIP_PIECE_NO='" + NPIECE_NO + "',"
                                + "WIP_EXT_PIECE_NO='" + updtwpc + "',"
                                + "WIP_PIECE_STAGE='PLANNING',"
                                + "WIP_WARP_DATE='0000-00-00',WIP_WVG_DATE='0000-00-00',WIP_MND_DATE='0000-00-00',"
                                + "WIP_NDL_DATE='0000-00-00',WIP_FNSG_DATE='0000-00-00',"
                                + "WIP_EXP_DISPATCH_DATE='0000-00-00',"
                                + "WIP_HOLD_DATE='0000-00-00',"
                                + "WIP_RELEASE_DATE='0000-00-00',WIP_PRIORITY_HOLD_CAN_FLAG=0,"
                                + "WIP_WVG_LAYER_REMARK='',WIP_WEAVING_WEIGHT=0.0,WIP_WVG_A_DATE='0000-00-00',"
                                + "WIP_WVG_B_DATE='0000-00-00',WIP_WEAVING_WEIGHT_A=0.0,WIP_WEAVING_WEIGHT_B=0.0,"
                                + "WIP_MND_LAYER_REMARK='',WIP_MENDING_WEIGHT=0.0,WIP_MND_A_DATE='0000-00-00',"
                                + "WIP_MND_B_DATE='0000-00-00',WIP_MENDING_WEIGHT_A=0.0,WIP_MENDING_WEIGHT_B=0.0,"
                                + "WIP_NEEDLING_WEIGHT=0.0,WIP_SPLICE_WEIGHT=0.0,WIP_SEAM_WEIGHT=0.0,"
                                + "WIP_STATUS='ORDERED'";
                        data.Execute(UpdtQry);
                        data.Execute("INSERT INTO PRODUCTION.FELT_WIP_PIECE_REGISTER SELECT * FROM PRODUCTION.TMP_FELT_WIP_PIECE_INSERT");

                    }
                }

                //String InsertQuery = "INSERT INTO PRODUCTION.FELT_SALES_PIECE_REGISTER (PR_PIECE_NO,PR_DATE,PR_ORDER_DATE,PR_DOC_NO,PR_MACHINE_NO,PR_POSITION_NO,PR_PARTY_CODE,PR_PRODUCT_CODE,PR_GROUP,PR_STYLE,PR_LENGTH,PR_WIDTH,PR_GSM,PR_THORITICAL_WEIGHT,PR_SQMTR,PR_SYN_PER,PR_REQUESTED_MONTH,PR_REGION,PR_INCHARGE,PR_REFERENCE,PR_REFERENCE_DATE,PR_PO_NO,PR_PO_DATE,PR_ORDER_REMARK,PR_PIECE_REMARK,PR_PIECE_STAGE,PR_WARP_DATE,PR_WVG_DATE,PR_MND_DATE,PR_NDL_DATE,PR_FNSG_DATE,PR_RCV_DATE,PR_ACTUAL_WEIGHT,PR_ACTUAL_LENGTH,PR_ACTUAL_WIDTH,PR_BALE_NO,PR_PACKED_DATE,PR_REJECTED_FLAG,PR_REJECTED_REMARK,PR_DIVERSION_FLAG,PR_DIVERSION_REASON,PR_DIVERTED_FLAG,PR_DIVERTED_REASON,PR_EXP_DISPATCH_DATE,PR_PRIORITY_HOLD_CAN_FLAG,PR_INVOICE_NO,PR_INVOICE_DATE,PR_LR_NO,PR_LR_DATE,PR_INVOICE_PARTY,PR_PARTY_CODE_ORIGINAL,PR_PIECE_NO_ORIGINAL,PR_WH_CODE,PR_INWARD_NO,PR_RACK_NO,PR_PIECE_ID,PR_LOCATION,PR_HOLD_DATE,PR_HOLD_REASON,PR_RELEASE_DATE,CREATED_DATE,CREATED_BY,MODIFIED_DATE,MODIFIED_BY,HIERARCHY_ID,APPROVER_BY,APPROVER_DATE,APPROVER_REMARK,BALE_REOPEN_FLG,WVG_LAYER_REMARK,PR_BILL_LENGTH,PR_BILL_WIDTH,PR_BILL_WEIGHT,PR_BILL_SQMTR,PR_BILL_GSM,PR_BILL_PRODUCT_CODE,PR_PKG_DP_NO,PR_PKG_DP_DATE,PR_SALES_RETURNS_NO,PR_SALES_RETURNS_DATE,PR_SALES_RETURNS_REMARKS,PR_SALES_RETURNS_FLG,PR_DELINK,PR_DELINK_REASON)  SELECT CONCAT(TRIM(PR_PIECE_NO),'RM'),PR_DATE,PR_ORDER_DATE,PR_DOC_NO,PR_MACHINE_NO,PR_POSITION_NO,PR_PARTY_CODE,PR_PRODUCT_CODE,PR_GROUP,PR_STYLE,PR_LENGTH,PR_WIDTH,PR_GSM,PR_THORITICAL_WEIGHT,PR_SQMTR,PR_SYN_PER,PR_REQUESTED_MONTH,PR_REGION,PR_INCHARGE,PR_REFERENCE,PR_REFERENCE_DATE,PR_PO_NO,PR_PO_DATE,PR_ORDER_REMARK,PR_PIECE_REMARK,PR_PIECE_STAGE,PR_WARP_DATE,PR_WVG_DATE,PR_MND_DATE,PR_NDL_DATE,PR_FNSG_DATE,PR_RCV_DATE,PR_ACTUAL_WEIGHT,PR_ACTUAL_LENGTH,PR_ACTUAL_WIDTH,PR_BALE_NO,PR_PACKED_DATE,PR_REJECTED_FLAG,PR_REJECTED_REMARK,PR_DIVERSION_FLAG,PR_DIVERSION_REASON,PR_DIVERTED_FLAG,PR_DIVERTED_REASON,PR_EXP_DISPATCH_DATE,PR_PRIORITY_HOLD_CAN_FLAG,PR_INVOICE_NO,PR_INVOICE_DATE,PR_LR_NO,PR_LR_DATE,PR_INVOICE_PARTY,PR_PARTY_CODE_ORIGINAL,PR_PIECE_NO_ORIGINAL,PR_WH_CODE,PR_INWARD_NO,PR_RACK_NO,PR_PIECE_ID,PR_LOCATION,PR_HOLD_DATE,PR_HOLD_REASON,PR_RELEASE_DATE,CREATED_DATE,CREATED_BY,MODIFIED_DATE,MODIFIED_BY,HIERARCHY_ID,APPROVER_BY,APPROVER_DATE,APPROVER_REMARK,BALE_REOPEN_FLG,WVG_LAYER_REMARK,PR_BILL_LENGTH,PR_BILL_WIDTH,PR_BILL_WEIGHT,PR_BILL_SQMTR,PR_BILL_GSM,PR_BILL_PRODUCT_CODE,PR_PKG_DP_NO,PR_PKG_DP_DATE,PR_SALES_RETURNS_NO,PR_SALES_RETURNS_DATE,PR_SALES_RETURNS_REMARKS,PR_SALES_RETURNS_FLG,PR_DELINK,PR_DELINK_REASON FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + PIECE_NO + "'";
                String dlinkrmk = "MFG PIECE REJECTION : PIECE_NO " + PIECE_NO + "  HAS BEEN CONVERTED TO " + NPIECE_NO + ",  " + REMAKRS;

                UpdtQry = "UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER "
                        + "SET PR_ADJUSTABLE_LENGTH=" + (double) EITLERPGLOBAL.round(getAttribute("ADJUSTED_LENGTH").getVal(), 2) + ","
                        + "PR_ADJUSTABLE_WIDTH=" + (double) EITLERPGLOBAL.round(getAttribute("ADJUSTED_WIDTH").getVal(), 2) + ","
                        + "PR_ADJUSTABLE_GSM=" + (double) EITLERPGLOBAL.round(getAttribute("ADJUSTED_GSM").getVal(), 2) + ","
                        + "PR_ADJUSTABLE_WEIGHT=" + (double) EITLERPGLOBAL.round(getAttribute("ADJUSTED_WEIGHT").getVal(), 2) + ","
                        + "PR_REJECTED_FLAG=1,PR_DIVERSION_FLAG='',PR_DIVERSION_REASON='',PR_DELINK='OBSOLETE',PR_DELINK_REASON='" + dlinkrmk + "', "
                        + "PR_REJECTION_ORIGINATED_FROM='" + getAttribute("REJECTION_ORIGINATED_FROM").getString() + "',"
                        + "PR_CURRENT_SCH_MONTH='',PR_CURRENT_SCH_LAST_DDMMYY='',PR_SP_MONTHYEAR='',PR_SP_LAST_DDMMYY='',"
                        + "PR_SP_REMARKS='',PR_SPL_REQUEST_DATE='',PR_SPL_REQUEST_MONTHYEAR='',PR_SPL_REQUEST_REMARK='',"
                        + "PR_OBSOLETE_DATE=CURDATE(),PR_REQ_MTH_LAST_DDMMYY='',PR_REQUESTED_MONTH='',"
                        + "PR_OBSOLETE_SOURCE='MFG REJECTION',"
                        + "PR_OBSOLETE_UPN_ASSIGN_STATUS='" + getAttribute("OBSOLETE_UPN_ASSIGN_STATUS").getString() + "',"
                        + "PR_SCRAP_REASON='" + getAttribute("SCRAP_REASON").getString() + "',"
                        + "PR_UNMAPPED_REASON='" + getAttribute("UNMAPPED_REASON").getString() + "' "
                        + "WHERE PR_PIECE_NO='" + updtpc + "'";
                data.Execute(UpdtQry);
                UpdtQry = "UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER "
                        + "SET WIP_ADJUSTABLE_LENGTH=" + (double) EITLERPGLOBAL.round(getAttribute("ADJUSTED_LENGTH").getVal(), 2) + ","
                        + "WIP_ADJUSTABLE_WIDTH=" + (double) EITLERPGLOBAL.round(getAttribute("ADJUSTED_WIDTH").getVal(), 2) + ","
                        + "WIP_ADJUSTABLE_GSM=" + (double) EITLERPGLOBAL.round(getAttribute("ADJUSTED_GSM").getVal(), 2) + ","
                        + "WIP_ADJUSTABLE_WEIGHT=" + (double) EITLERPGLOBAL.round(getAttribute("ADJUSTED_WEIGHT").getVal(), 2) + ","
                        + "WIP_REJECTED_FLAG=1,WIP_DIVERSION_FLAG='',WIP_DIVERSION_REASON='',WIP_OBSOLETE='OBSOLETE',WIP_OBSOLETE_REASON='" + dlinkrmk + "',"
                        + "WIP_REJECTION_ORIGINATED_FROM='" + getAttribute("REJECTION_ORIGINATED_FROM").getString() + "',"
                        + "WIP_CURRENT_SCH_MONTH='',WIP_CURRENT_SCH_LAST_DDMMYY='',WIP_SP_MONTHYEAR='',WIP_SP_LAST_DDMMYY='',"
                        + "WIP_SP_REMARKS='',WIP_SPL_REQUEST_DATE='',WIP_SPL_REQUEST_MONTHYEAR='',WIP_SPL_REQUEST_REMARK='',"
                        + "WIP_REQ_MTH_LAST_DDMMYY='',WIP_REQUESTED_MONTH='',"
                        + "WIP_OBSOLETE_SOURCE='MFG REJECTION',"
                        + "WIP_OBSOLETE_UPN_ASSIGN_STATUS='" + getAttribute("OBSOLETE_UPN_ASSIGN_STATUS").getString() + "',"
                        + "WIP_SCRAP_REASON='" + getAttribute("SCRAP_REASON").getString() + "',"
                        + "WIP_UNMAPPED_REASON='" + getAttribute("UNMAPPED_REASON").getString() + "' ";
//                        + " WHERE WIP_EXT_PIECE_NO='" + PIECE_NO + "'";
                if(getAttribute("REJECTION_DEPARTMENT").getString().trim().equalsIgnoreCase("FINISHING")) {
                    UpdtQry = UpdtQry + " WHERE WIP_PIECE_NO='" + PIECE_NO + "'";
                } else {
                    UpdtQry = UpdtQry + " WHERE WIP_EXT_PIECE_NO='" + PIECE_NO + "'";
                }
                data.Execute(UpdtQry);
                
                String sql = "INSERT INTO PRODUCTION.OBSOLETE_MAPPED_UNMAPPED_SCRAP (PIECE_NO, PARTY_CODE, STYLE, PROD_GROUP, GSM, "
                        + "SYN_PER, UPN_ASSIGN_STATUS, UNMAPPED_REASON, SCRAP_REASON, OBSOLETE_SOURCE, ENTRY_DATE) "
                        + "SELECT REJ_PIECE_NO, REJ_PARTY_CODE, STYLE, R.GROUP, GSM, SYN_PER, OBSOLETE_UPN_ASSIGN_STATUS, "
                        + "UNMAPPED_REASON, SCRAP_REASON, 'MFG REJECTION', CURDATE() "
                        + "FROM PRODUCTION.FELT_REJECTION R WHERE REJ_PIECE_NO =  '" + PIECE_NO + "' ";
                data.Execute(sql);
                
                if(getAttribute("REJECTION_DEPARTMENT").getString().trim().equalsIgnoreCase("WEAVING")) {
                    data.Execute("UPDATE PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL SET WEAVING_DATE=CURDATE() WHERE PIECE_NO='" + PIECE_NO + "' AND COALESCE(WEAVING_DATE,'0000-00-00')='0000-00-00' ");
                    data.Execute("UPDATE PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_DETAIL SET WEAVING_DATE=CURDATE() WHERE PIECE_NO='" + PIECE_NO + "' AND COALESCE(WEAVING_DATE,'0000-00-00')='0000-00-00' ");
                }
                
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
     * the document. After checking it deletes the record of selected rejecction
     * date and document no.
     */
    public boolean CanDelete(String documentNo, String stringRejectionDate, int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";

        try {
            tmpStmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_REJECTION` WHERE REJ_DOC_NO='" + documentNo + "' AND APPROVED=" + 1;
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=764 AND USER_ID=" + userID + " AND DOC_NO='" + documentNo + "' AND STATUS='W'";
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();

                if (rsTmp.getInt("COUNT") > 0) {
                    strSQL = "DELETE FROM PRODUCTION.`FELT_REJECTION` WHERE REJ_DATE= '" + EITLERPGLOBAL.formatDateDB(stringRejectionDate) + "' AND REJ_DOC_NO='" + documentNo + "'";
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
    public boolean IsEditable(String pRejectionDocumentNo, int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";
        try {
            if (HistoryView) {
                return false;
            }
            tmpStmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //First check that document is approved or not
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_REJECTION` WHERE REJ_DOC_NO='" + pRejectionDocumentNo + "' AND APPROVED=1";
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=764 AND USER_ID=" + userID + " AND DOC_NO='" + pRejectionDocumentNo + "' AND STATUS='W'";
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
            String strSql = "SELECT REJ_DOC_NO FROM PRODUCTION.`FELT_REJECTION` WHERE REJ_DATE!=0 " + stringFindQuery + " ORDER BY REJ_DATE";
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
        ResultSet resultSetTemp;
        Statement statementTemp;
        int RevNo = 0;
        try {
            clear();
            setAttribute("REVISION_NO", 0);
            setAttribute("REJECTION_DOCUMENT_NO", resultSet.getString("REJ_DOC_NO"));

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.`FELT_REJECTION` WHERE REJ_DOC_NO='" + resultSet.getString("REJ_DOC_NO") + "'");

            while (resultSetTemp.next()) {
                setAttribute("REJECTION_DATE", resultSetTemp.getString("REJ_DATE"));
                setAttribute("REJECTION_FORM_NO", resultSetTemp.getString("REJ_FORM_NO"));
                setAttribute("REJECTION_DEPARTMENT", resultSetTemp.getString("REJ_DEPT"));
                setAttribute("PIECE_STAGE", resultSetTemp.getString("PIECE_STAGE"));
                setAttribute("REJECTION_PIECE_NO", resultSetTemp.getString("REJ_PIECE_NO"));
                setAttribute("REJECTION_NEW_PIECE_NO", resultSetTemp.getString("REJ_NEW_PIECE_NO"));
                setAttribute("REJECTION_PARTY_CODE", resultSetTemp.getString("REJ_PARTY_CODE"));
                setAttribute("STYLE", resultSetTemp.getString("STYLE"));
                setAttribute("GROUP", resultSetTemp.getString("GROUP"));
                setAttribute("SYN_PER", resultSetTemp.getFloat("SYN_PER"));
                setAttribute("GSM", resultSetTemp.getFloat("GSM"));
                setAttribute("MFG_SIZE", resultSetTemp.getString("MFG_SIZE"));
                //setAttribute("ACTUAL_SIZE", resultSetTemp.getString("ACT_SIZE"));
                //setAttribute("REQUIRED_WEIGHT", resultSetTemp.getFloat("REQ_WEIGHT"));
                setAttribute("ACTUAL_WEIGHT", resultSetTemp.getFloat("ACT_WEIGHT"));
                setAttribute("REASON", resultSetTemp.getString("REASON"));
                setAttribute("COMMENT", resultSetTemp.getString("COMMENT"));
                setAttribute("ACTION", resultSetTemp.getString("ACTION"));

                setAttribute("IS_PROCESS", resultSetTemp.getInt("IS_PROCESS"));

                setAttribute("ORDER_LENGTH", resultSetTemp.getDouble("ORDER_LENGTH"));
                setAttribute("ORDER_WIDTH", resultSetTemp.getDouble("ORDER_WIDTH"));
                setAttribute("ORDER_GSM", resultSetTemp.getDouble("ORDER_GSM"));
                setAttribute("ORDER_WEIGHT", resultSetTemp.getDouble("ORDER_WEIGHT"));

                setAttribute("BILL_LENGTH", resultSetTemp.getDouble("BILL_LENGTH"));
                setAttribute("BILL_WIDTH", resultSetTemp.getDouble("BILL_WIDTH"));
                setAttribute("BILL_GSM", resultSetTemp.getDouble("BILL_GSM"));
                setAttribute("BILL_WEIGHT", resultSetTemp.getDouble("BILL_WEIGHT"));

                setAttribute("ADJUSTED_LENGTH", resultSetTemp.getDouble("ADJUSTED_LENGTH"));
                setAttribute("ADJUSTED_WIDTH", resultSetTemp.getDouble("ADJUSTED_WIDTH"));
                setAttribute("ADJUSTED_GSM", resultSetTemp.getDouble("ADJUSTED_GSM"));
                setAttribute("ADJUSTED_WEIGHT", resultSetTemp.getDouble("ADJUSTED_WEIGHT"));

                setAttribute("BASE_GSM", resultSetTemp.getDouble("BASE_GSM"));
                setAttribute("WEB_GSM", resultSetTemp.getDouble("WEB_GSM"));
                setAttribute("WEAVE", resultSetTemp.getString("WEAVE"));
                setAttribute("CFM_TARGETED", resultSetTemp.getString("CFM_TARGETED"));
                setAttribute("PAPER_PRODUCT_TYPE", resultSetTemp.getString("PAPER_PRODUCT_TYPE"));
                setAttribute("REJECTION_LOCATION", resultSetTemp.getString("REJECTION_LOCATION"));
                setAttribute("LOOM_NO", resultSetTemp.getString("LOOM_NO"));
                setAttribute("OBSOLETE_UPN_ASSIGN_STATUS", resultSetTemp.getString("OBSOLETE_UPN_ASSIGN_STATUS"));
                setAttribute("SCRAP_REASON", resultSetTemp.getString("SCRAP_REASON"));
                setAttribute("UNMAPPED_REASON", resultSetTemp.getString("UNMAPPED_REASON"));
                
                setAttribute("REJECTION_ORIGINATED_FROM", resultSetTemp.getString("REJECTION_ORIGINATED_FROM"));                

                setAttribute("CREATED_BY", resultSetTemp.getInt("CREATED_BY"));
                setAttribute("CREATED_DATE", resultSetTemp.getString("CREATED_DATE"));
                setAttribute("MODIFIED_BY", resultSetTemp.getInt("MODIFIED_BY"));
                setAttribute("MODIFIED_DATE", resultSetTemp.getString("MODIFIED_DATE"));
                setAttribute("APPROVED", resultSetTemp.getInt("APPROVED"));
                setAttribute("APPROVED_DATE", resultSetTemp.getString("APPROVED_DATE"));
                setAttribute("REJECTED", resultSetTemp.getBoolean("REJECTED"));
                setAttribute("REJECTED_DATE", resultSetTemp.getString("REJECTED_DATE"));
                setAttribute("HIERARCHY_ID", resultSetTemp.getInt("HIERARCHY_ID"));
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

    public boolean setHistoryData(String pRejectionDate, String pDocNo) {
        ResultSet resultSetTemp;
        Statement statementTemp;
        int RevNo = 0;
        try {
            //clear();
            RevNo = resultSet.getInt("REVISION_NO");
            setAttribute("REVISION_NO", RevNo);

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.`FELT_REJECTION_H` WHERE REJ_DATE='" + pRejectionDate + "' AND REJ_DOC_NO='" + pDocNo + "' AND REVISION_NO='" + resultSet.getInt("REVISION_NO") + "' ORDER BY REVISION_NO");

            while (resultSetTemp.next()) {
                setAttribute("REJECTION_DATE", resultSetTemp.getString("REJ_DATE"));
                setAttribute("REJECTION_DOCUMENT_NO", resultSetTemp.getString("REJ_DOC_NO"));
                setAttribute("REJECTION_FORM_NO", resultSetTemp.getString("REJ_FORM_NO"));
                setAttribute("REJECTION_DEPARTMENT", resultSetTemp.getString("REJ_DEPT"));
                setAttribute("PIECE_STAGE", resultSetTemp.getString("PIECE_STAGE"));
                setAttribute("REJECTION_PIECE_NO", resultSetTemp.getString("REJ_PIECE_NO"));
                setAttribute("REJECTION_PARTY_CODE", resultSetTemp.getString("REJ_PARTY_CODE"));
                setAttribute("STYLE", resultSetTemp.getString("STYLE"));
                setAttribute("GROUP", resultSetTemp.getString("GROUP"));
                setAttribute("SYN_PER", resultSetTemp.getFloat("SYN_PER"));
                setAttribute("GSM", resultSetTemp.getFloat("GSM"));
                setAttribute("MFG_SIZE", resultSetTemp.getString("MFG_SIZE"));
                setAttribute("ACTUAL_SIZE", resultSetTemp.getString("ACT_SIZE"));
                setAttribute("REQUIRED_WEIGHT", resultSetTemp.getFloat("REQ_WEIGHT"));
                setAttribute("ACTUAL_WEIGHT", resultSetTemp.getFloat("ACT_WEIGHT"));
                setAttribute("REASON", resultSetTemp.getString("REASON"));
                setAttribute("COMMENT", resultSetTemp.getString("COMMENT"));
                setAttribute("ACTION", resultSetTemp.getString("ACTION"));

                setAttribute("ADJUSTED_LENGTH", resultSetTemp.getDouble("ADJUSTED_LENGTH"));
                setAttribute("ADJUSTED_WIDTH", resultSetTemp.getDouble("ADJUSTED_WIDTH"));
                setAttribute("ADJUSTED_GSM", resultSetTemp.getDouble("ADJUSTED_GSM"));
                setAttribute("ADJUSTED_WEIGHT", resultSetTemp.getDouble("ADJUSTED_WEIGHT"));

                setAttribute("BASE_GSM", resultSetTemp.getDouble("BASE_GSM"));
                setAttribute("WEB_GSM", resultSetTemp.getDouble("WEB_GSM"));
                setAttribute("WEAVE", resultSetTemp.getString("WEAVE"));
                setAttribute("CFM_TARGETED", resultSetTemp.getString("CFM_TARGETED"));
                setAttribute("PAPER_PRODUCT_TYPE", resultSetTemp.getString("PAPER_PRODUCT_TYPE"));
                setAttribute("REJECTION_LOCATION", resultSetTemp.getString("REJECTION_LOCATION"));
                setAttribute("LOOM_NO", resultSetTemp.getString("LOOM_NO"));
                setAttribute("OBSOLETE_UPN_ASSIGN_STATUS", resultSetTemp.getString("OBSOLETE_UPN_ASSIGN_STATUS"));
                setAttribute("SCRAP_REASON", resultSetTemp.getString("SCRAP_REASON"));
                setAttribute("UNMAPPED_REASON", resultSetTemp.getString("UNMAPPED_REASON"));
                
                setAttribute("REJECTION_ORIGINATED_FROM", resultSetTemp.getString("REJECTION_ORIGINATED_FROM"));

                setAttribute("UPDATED_BY", resultSetTemp.getInt("UPDATED_BY"));
                setAttribute("ENTRY_DATE", resultSetTemp.getString("ENTRY_DATE"));
                setAttribute("HIERARCHY_ID", resultSetTemp.getInt("HIERARCHY_ID"));
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

    public static HashMap getHistoryList(String stringRejectionDate, String rejecctionDocumentNo) {
        HashMap hmHistoryList = new HashMap();
        Statement stTmp;
        ResultSet rsTmp;
        try {
            stTmp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp = stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_REJECTION_H WHERE REJ_DATE='" + stringRejectionDate + "' AND REJ_DOC_NO='" + rejecctionDocumentNo + "' GROUP BY REVISION_NO");

            while (rsTmp.next()) {
                clsFeltRejection ObjFeltRejection = new clsFeltRejection();

                ObjFeltRejection.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                ObjFeltRejection.setAttribute("UPDATED_BY", rsTmp.getInt("UPDATED_BY"));
                ObjFeltRejection.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                ObjFeltRejection.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                ObjFeltRejection.setAttribute("APPROVER_REMARKS", rsTmp.getString("APPROVER_REMARKS"));

                hmHistoryList.put(Integer.toString(hmHistoryList.size() + 1), ObjFeltRejection);
            }

            rsTmp.close();
            stTmp.close();
            return hmHistoryList;
        } catch (Exception e) {
            e.printStackTrace();
            return hmHistoryList;
        }
    }

    public boolean ShowHistory(String pRejectionDate, String pDocNo) {
        Ready = false;
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery("SELECT DISTINCT REVISION_NO FROM PRODUCTION.FELT_REJECTION_H WHERE REJ_DATE='" + pRejectionDate + "' AND REJ_DOC_NO='" + pDocNo + "'");
            Ready = true;
            historyRejectionDate = pRejectionDate;
            historyDocumentNo = pDocNo;
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
                strSQL = "SELECT REJ_DOC_NO, DOC_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_REJECTION,PRODUCTION.FELT_PROD_DOC_DATA WHERE REJ_DOC_NO=DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=764 ORDER BY RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT REJ_DOC_NO, DOC_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_REJECTION,PRODUCTION.FELT_PROD_DOC_DATA WHERE REJ_DOC_NO=DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=764 ORDER BY DOC_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT REJ_DOC_NO, DOC_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_REJECTION,PRODUCTION.FELT_PROD_DOC_DATA WHERE REJ_DOC_NO=DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=764 ORDER BY REJ_DOC_NO";
            }

            ResultSet rsTmp = data.getConn().createStatement().executeQuery(strSQL);

            Counter = 0;
            while (rsTmp.next()) {

                Counter = Counter + 1;
                clsFeltRejection ObjDoc = new clsFeltRejection();

                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO", rsTmp.getString("REJ_DOC_NO"));
                ObjDoc.setAttribute("DOC_DATE", rsTmp.getString("DOC_DATE"));
                ObjDoc.setAttribute("RECEIVED_DATE", rsTmp.getString("RECEIVED_DATE"));
                // ----------------- End of Header Fields ------------------------------------//

                List.put(Integer.toString(Counter), ObjDoc);
            }
            rsTmp.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return List;
    }

    public String[] getPieceDetails(String pPieceNo) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        String[] pieceDetails = new String[15];

        try {
            tmpConn = data.getConn();
            Stmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // rsTmp=Stmt.executeQuery("SELECT * FROM (SELECT PARTY_CD,GRUP,REPLACE(BALNK,' ','') AS STYLE, GSQ, PRODUCT_CODE FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"') O LEFT JOIN (SELECT SYN_PER,ITEM_CODE FROM PRODUCTION.FELT_RATE_MASTER) R ON O.PRODUCT_CODE=SUBSTRING(R.ITEM_CODE,1,6)");

            rsTmp = Stmt.executeQuery("SELECT A.WIP_PARTY_CODE,A.WIP_GROUP,A.WIP_STYLE,A.WIP_PRODUCT_CODE,A.WIP_SYN_PER,A.WIP_LENGTH,A.WIP_WIDTH,A.WIP_GSM,A.WIP_THORITICAL_WEIGHT,A.WIP_BILL_LENGTH,A.WIP_BILL_WIDTH,A.WIP_BILL_WEIGHT,WIP_BILL_GSM FROM PRODUCTION.FELT_WIP_PIECE_REGISTER A,DINESHMILLS.D_SAL_PARTY_MASTER B WHERE  A.WIP_PARTY_CODE=B.PARTY_CODE AND A.WIP_EXT_PIECE_NO='" + pPieceNo + "'");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                pieceDetails[0] = rsTmp.getString("WIP_PARTY_CODE");
                pieceDetails[1] = rsTmp.getString("WIP_GROUP");
                pieceDetails[2] = rsTmp.getString("WIP_STYLE");
                pieceDetails[3] = rsTmp.getString("WIP_SYN_PER");
                pieceDetails[4] = rsTmp.getString("WIP_LENGTH");
                pieceDetails[5] = rsTmp.getString("WIP_WIDTH");
                pieceDetails[6] = rsTmp.getString("WIP_GSM");
                pieceDetails[7] = rsTmp.getString("WIP_THORITICAL_WEIGHT");
                pieceDetails[8] = rsTmp.getString("WIP_BILL_LENGTH");
                pieceDetails[9] = rsTmp.getString("WIP_BILL_WIDTH");
                pieceDetails[10] = rsTmp.getString("WIP_BILL_GSM");
                pieceDetails[11] = rsTmp.getString("WIP_BILL_WEIGHT");
            }

            Stmt.close();
            rsTmp.close();

            return pieceDetails;
        } catch (Exception e) {
            e.printStackTrace();
            return pieceDetails;
        }
    }

    // Updates Rejection Date in Order Master Table and CANCELED in Felt Production Table to confirm that Rejection has completed
    private void updateRejectionDate(String pDocumentNo, String pRejectionDept) {
        if (!pRejectionDept.equals("FINISHING")) {
            String orderMasterDate = "";
            if (pRejectionDept.equals("WEAVING")) {
                orderMasterDate = "PR_WVG_DATE";
            } else if (pRejectionDept.equals("MENDING")) {
                orderMasterDate = "PR_MND_DATE";
            } else if (pRejectionDept.equals("NEEDLING")) {
                orderMasterDate = "PR_NDL_DATE";
            }
            if (pRejectionDept.equals("WEAVING")) {
                orderMasterDate = "PR_WVG_DATE";
            }

            if (pRejectionDept.equals("WEAVING")) {
                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER,PRODUCTION.FELT_REJECTION SET " + orderMasterDate + "='0000-00-00' WHERE PR_PIECE_NO+0=REJ_PIECE_NO+0 AND PR_PARTY_CODE=REJ_PARTY_CODE  AND REJ_DOC_NO='" + pDocumentNo + "' AND APPROVED=1");
            } else if (pRejectionDept.equals("MENDING")) {
                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER,PRODUCTION.FELT_REJECTION SET PR_MND_DATE='0000-00-00',PR_PIECE_STAGE='WEAVING' WHERE PR_PIECE_NO+0=REJ_PIECE_NO+0 AND PR_PARTY_CODE=REJ_PARTY_CODE  AND REJ_DOC_NO='" + pDocumentNo + "' AND APPROVED=1");
            } else if (pRejectionDept.equals("NEEDLING")) {
                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER,PRODUCTION.FELT_REJECTION SET PR_NDL_DATE='0000-00-00',PR_PIECE_STAGE='MENDING' WHERE PR_PIECE_NO+0=REJ_PIECE_NO+0 AND PR_PARTY_CODE=REJ_PARTY_CODE  AND REJ_DOC_NO='" + pDocumentNo + "' AND APPROVED=1");
            }
            data.Execute("UPDATE PRODUCTION.FELT_PROD_DATA P,PRODUCTION.FELT_REJECTION R SET CANCELED=1,CHANGED=1 WHERE PROD_PIECE_NO=REJ_PIECE_NO AND PROD_PARTY_CODE=REJ_PARTY_CODE AND REJ_DOC_NO='" + pDocumentNo + "' AND PROD_DEPT='" + pRejectionDept + "' AND R.APPROVED=1 AND P.APPROVED=1 AND PROD_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND PROD_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'");
        }
    }

    // on insert checks piece no already exist in db
    public boolean checkPieceNoInDB(String pPieceNo, String pRejectionDept) {
        int count = data.getIntValueFromDB("SELECT COUNT(REJ_PIECE_NO) FROM PRODUCTION.`FELT_REJECTION` WHERE REJ_DEPT ='" + pRejectionDept + "' AND REJ_PIECE_NO='" + pPieceNo + "' AND REJ_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND REJ_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'");
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    // on update checks piece no already exist in db
    public boolean checkPieceNoInDB(String pPieceNo, String pRejectionDate, String pRejectionDept) {
        int count = data.getIntValueFromDB("SELECT COUNT(REJ_PIECE_NO) FROM PRODUCTION.`FELT_REJECTION` WHERE REJ_DEPT ='" + pRejectionDept + "' AND REJ_PIECE_NO='" + pPieceNo + "' AND REJ_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND REJ_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'");
        if (count >= 1) {
            int counter = 0;
            try {
                Connection Conn = data.getConn();
                Statement stTmp = Conn.createStatement();
                ResultSet rsTmp = stTmp.executeQuery("SELECT REJ_DATE FROM PRODUCTION.`FELT_REJECTION` WHERE REJ_DEPT='" + pRejectionDept + "' AND REJ_PIECE_NO='" + pPieceNo + "' AND REJ_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND REJ_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'");
                while (rsTmp.next()) {
                    if (rsTmp.getString("REJ_DATE").equals(EITLERPGLOBAL.formatDateDB(pRejectionDate))) {
                        counter++;
                    }
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

    // checks rejection date already exist in db
    public boolean checkRejectionDocumentNoInDB(String pRejectionDocumentNo) {
        int count = data.getIntValueFromDB("SELECT COUNT(REJ_DOC_NO) FROM PRODUCTION.`FELT_REJECTION` WHERE REJ_DOC_NO ='" + pRejectionDocumentNo + "'");
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    // it creates list of user name(felt) to be displayed
    public HashMap getUserNameList(int pHierarchyId, int pUserId) {
        HashMap hmUserNameList = new HashMap();
        String category = "";
        try {
            Connection Conn = data.getConn();
            Statement stTmp = Conn.createStatement();
            ResultSet rsHierarchyRights = stTmp.executeQuery("SELECT CREATOR, APPROVER, FINAL_APPROVER, APPROVAL_SEQUENCE FROM D_COM_HIERARCHY_RIGHTS WHERE COMPANY_ID=2 AND HIERARCHY_ID=" + pHierarchyId + " AND USER_ID=" + pUserId);
            while (rsHierarchyRights.next()) {
                boolean creator = rsHierarchyRights.getBoolean("CREATOR");
                boolean approver = rsHierarchyRights.getBoolean("APPROVER");
                boolean finalApprover = rsHierarchyRights.getBoolean("FINAL_APPROVER");
                int approvalSequence = rsHierarchyRights.getInt("APPROVAL_SEQUENCE");
                if (creator) {
                    category = "C";
                }
                if (approver && approvalSequence == 2) {
                    category = "FA";
                }
                if (approver && approvalSequence == 3) {
                    category = "SA";
                }
                if (approver && approvalSequence == 4) {
                    category = "TA";
                }
                if (finalApprover) {
                    category = "F";
                }
            }

            int counter = 1;
            ComboData cData = new ComboData();
            cData.Code = 0;
            cData.Text = "Select User";
            hmUserNameList.put(new Integer(counter++), cData);
            ResultSet rsTmp = stTmp.executeQuery("SELECT USER_ID,USER_NAME FROM PRODUCTION.`FELT_USER` WHERE USER_MODULE='REJECTION' AND USER_CATEG='" + category + "'");
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

    public int getUserApprovalSequence(int pHierarchyId, int pUserId) {
        return data.getIntValueFromDB("SELECT APPROVAL_SEQUENCE FROM D_COM_HIERARCHY_RIGHTS WHERE COMPANY_ID=2 AND HIERARCHY_ID=" + pHierarchyId + " AND USER_ID=" + pUserId);
    }

    public String getDocumentNo(String pRejectionDate, String pFeltDept) {
        String docNo = pFeltDept + pRejectionDate;
        String oldDocNo = data.getStringValueFromDB("SELECT MAX(SUBSTRING(REJ_DOC_NO,4,(LENGTH(REJ_DOC_NO)-3))) FROM PRODUCTION.FELT_REJECTION WHERE SUBSTRING(REJ_DOC_NO,1,3)='" + pFeltDept + "' AND SUBSTRING(REJ_DOC_NO,4,8)='" + pRejectionDate + "'");
        if (oldDocNo.equals("") || oldDocNo.equals(null)) {
            docNo += 1;
        } else {
            int no = Integer.parseInt(oldDocNo.substring(8, oldDocNo.length()));
            docNo += (no + 1);
        }
        return docNo;
    }
}
