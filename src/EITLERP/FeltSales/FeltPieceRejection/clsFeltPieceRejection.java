/*
 * clsFeltPieceRejection.java
 *
 * Created on March 12, 2013, 3:10 PM
 */
package EITLERP.FeltSales.FeltPieceRejection;

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
public class clsFeltPieceRejection {

    public String LastError = "";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public HashMap props;
    public boolean Ready = false;
    //Felt Order Collection
    public HashMap hmFeltOrderUpdDetails;

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
    public clsFeltPieceRejection() {
        LastError = "";
        props = new HashMap();
        props.put("FELT_AMEND_DATE", new Variant(""));
        props.put("FELT_AMEND_ID", new Variant(""));
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
        props.put("REMARKS", new Variant(""));
        props.put("APPROVAL_STATUS", new Variant(""));

        hmFeltOrderUpdDetails = new HashMap();
    }

    public boolean LoadData() {
        Ready = false;
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery("SELECT FELT_AMEND_ID,FELT_AMEND_DATE,FELT_AMEND_REASON FROM PRODUCTION.FELT_PIECE_REJECTION WHERE FELT_AMEND_DATE >= '" + EITLERPGLOBAL.FinFromDateDB + "' AND FELT_AMEND_DATE<='" + EITLERPGLOBAL.FinToDateDB + "' ORDER BY FELT_AMEND_DATE");
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
        ResultSet resultSetTemp, resultSetHistory;
        Statement statementTemp, statementHistory;
        try {
            // Felt Order Updation data connection
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_PIECE_REJECTION WHERE FELT_AMEND_DATE=''");

            // Felt order Updation data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_PIECE_REJECTION_H WHERE FELT_AMEND_DATE=''");

            // Felt order Updation Last Free No, 
            setAttribute("FELT_AMEND_ID", getNextFreeNo(712, true));

            //Now Insert records into Felt_Order_Amendment & History tables
            for (int i = 1; i <= hmFeltOrderUpdDetails.size(); i++) {
                clsFeltPieceRejectionDetails ObjFeltOrderUpdDetails = (clsFeltPieceRejectionDetails) hmFeltOrderUpdDetails.get(Integer.toString(i));

                //Insert records into Felt order Amendment table
                resultSetTemp.moveToInsertRow();

                resultSetTemp.updateString("FELT_AMEND_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("FELT_AMEND_DATE").getString()));
                resultSetTemp.updateString("FELT_AMEND_ID", getAttribute("FELT_AMEND_ID").getString());
                resultSetTemp.updateString("FELT_AMEND_REASON", getAttribute("FELT_AMEND_REASON").getString());
                resultSetTemp.updateString("FELT_AMEND_PIECE_NO", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_PIECE_NO").getString());
                resultSetTemp.updateString("FELT_AMEND_PARTY_CODE", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_PARTY_CODE").getString());
                resultSetTemp.updateString("FELT_AMEND_PARTY_NAME", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_PARTY_NAME").getString());
                resultSetTemp.updateString("FELT_AMEND_ORDER_DATE", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_ORDER_DATE").getString());
                resultSetTemp.updateString("FELT_AMEND_PRODUCT_CODE", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_PRODUCT_CODE").getString());
                resultSetTemp.updateString("FELT_AMEND_GROUP", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_GROUP").getString());
                resultSetTemp.updateString("FELT_AMEND_STYLE", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_STYLE").getString());
                resultSetTemp.updateString("FELT_AMEND_LENGTH", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_LENGTH").getString());
                resultSetTemp.updateString("FELT_AMEND_WIDTH", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_WIDTH").getString());
                resultSetTemp.updateString("FELT_AMEND_GSM", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_GSM").getString());
                resultSetTemp.updateString("FELT_AMEND_WEIGHT", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_WEIGHT").getString());
                resultSetTemp.updateString("FELT_AMEND_SQMTR", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_SQMTR").getString());
                resultSetTemp.updateString("FELT_AMEND_BILL_LENGTH", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_BILL_LENGTH").getString());
                resultSetTemp.updateString("FELT_AMEND_BILL_WIDTH", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_BILL_WIDTH").getString());
                resultSetTemp.updateString("FELT_AMEND_BILL_GSM", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_BILL_GSM").getString());
                resultSetTemp.updateString("FELT_AMEND_BILL_WEIGHT", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_BILL_WEIGHT").getString());
                resultSetTemp.updateString("FELT_AMEND_BILL_SQMTR", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_BILL_SQMTR").getString());
                resultSetTemp.updateString("FELT_AMEND_BILL_PRODUCT_CODE", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_BILL_PRODUCT_CODE").getString());
                resultSetTemp.updateString("FELT_AMEND_REMARK", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_REMARK").getString());
                resultSetTemp.updateString("FELT_AMEND_MFG_STATUS", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_MFG_STATUS").getString());
                resultSetTemp.updateString("FELT_AMEND_INCHARGE_NAME", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_INCHARGE_NAME").getString());
                resultSetTemp.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                resultSetTemp.updateString("CREATED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetTemp.updateInt("MODIFIED_BY", 0);
                resultSetTemp.updateString("MODIFIED_DATE", "0000-00-00");
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
                resultSetHistory.updateString("FELT_AMEND_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("FELT_AMEND_DATE").getString()));
                resultSetHistory.updateString("FELT_AMEND_ID", getAttribute("FELT_AMEND_ID").getString());
                resultSetHistory.updateString("FELT_AMEND_REASON", getAttribute("FELT_AMEND_REASON").getString());
                resultSetHistory.updateString("FELT_AMEND_PIECE_NO", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_PIECE_NO").getString());
                resultSetHistory.updateString("FELT_AMEND_PARTY_CODE", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_PARTY_CODE").getString());
                resultSetHistory.updateString("FELT_AMEND_PARTY_NAME", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_PARTY_NAME").getString());
                resultSetHistory.updateString("FELT_AMEND_ORDER_DATE", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_ORDER_DATE").getString());
                resultSetHistory.updateString("FELT_AMEND_PRODUCT_CODE", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_PRODUCT_CODE").getString());
                resultSetHistory.updateString("FELT_AMEND_GROUP", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_GROUP").getString());
                resultSetHistory.updateString("FELT_AMEND_STYLE", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_STYLE").getString());
                resultSetHistory.updateString("FELT_AMEND_LENGTH", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_LENGTH").getString());
                resultSetHistory.updateString("FELT_AMEND_WIDTH", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_WIDTH").getString());
                resultSetHistory.updateString("FELT_AMEND_GSM", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_GSM").getString());
                resultSetHistory.updateString("FELT_AMEND_WEIGHT", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_WEIGHT").getString());
                resultSetHistory.updateString("FELT_AMEND_BILL_LENGTH", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_BILL_LENGTH").getString());
                resultSetHistory.updateString("FELT_AMEND_BILL_WIDTH", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_BILL_WIDTH").getString());
                resultSetHistory.updateString("FELT_AMEND_BILL_GSM", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_BILL_GSM").getString());
                resultSetHistory.updateString("FELT_AMEND_BILL_WEIGHT", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_BILL_WEIGHT").getString());
                resultSetHistory.updateString("FELT_AMEND_BILL_SQMTR", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_BILL_SQMTR").getString());
                resultSetHistory.updateString("FELT_AMEND_BILL_PRODUCT_CODE", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_BILL_PRODUCT_CODE").getString());
                resultSetHistory.updateString("FELT_AMEND_REMARK", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_REMARK").getString());
                resultSetHistory.updateString("FELT_AMEND_MFG_STATUS", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_MFG_STATUS").getString());
                resultSetHistory.updateString("FELT_AMEND_INCHARGE_NAME", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_INCHARGE_NAME").getString());
                resultSetHistory.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                resultSetHistory.updateString("CREATED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateInt("UPDATED_BY", (int) getAttribute("UPDATED_BY").getVal());
                resultSetHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
                resultSetHistory.updateString("APPROVER_REMARKS", getAttribute("FROM_REMARKS").getString());

                resultSetHistory.updateInt("MODIFIED_BY", 0);
                resultSetHistory.updateString("MODIFIED_DATE", "0000-00-00");
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

            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = 712; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("FELT_AMEND_ID").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName = "FELT_PIECE_REJECTION";
            ObjFeltProductionApprovalFlow.IsCreator = true;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "FELT_AMEND_ID";

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
                ObjFeltProductionApprovalFlow.finalApproved = false;
                updatefeltorder(getAttribute("FELT_AMEND_ID").getString(), getAttribute("FELT_AMEND_REASON").getString());
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
        ResultSet resultSetTemp, resultSetHistory;
        Statement statementTemp, statementHistory;
        int revisionNo = 1;
        try {
            // Production data connection
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_PIECE_REJECTION WHERE FELT_AMEND_DATE=''");

            // Production data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //Get the Maximum Revision No in History Table.
            resultSetHistory = statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_PIECE_REJECTION_H WHERE FELT_AMEND_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("FELT_AMEND_DATE").getString()) + "' AND FELT_AMEND_ID='" + getAttribute("FELT_AMEND_ID").getString() + "'");
            resultSetHistory.first();
            if (resultSetHistory.getRow() > 0) {
                revisionNo = resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from production data table before insert
            statementHistory.execute("DELETE FROM PRODUCTION.FELT_PIECE_REJECTION WHERE FELT_AMEND_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("FELT_AMEND_DATE").getString()) + "' AND FELT_AMEND_ID='" + getAttribute("FELT_AMEND_ID").getString() + "'");

            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_PIECE_REJECTION_H WHERE FELT_AMEND_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("FELT_AMEND_DATE").getString()) + "' AND FELT_AMEND_ID='" + getAttribute("FELT_AMEND_ID").getString() + "'");

            //Now Update records into Felt_Order_Amendment tables
            for (int i = 1; i <= hmFeltOrderUpdDetails.size(); i++) {
                clsFeltPieceRejectionDetails ObjFeltOrderUpdDetails = (clsFeltPieceRejectionDetails) hmFeltOrderUpdDetails.get(Integer.toString(i));

                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateString("FELT_AMEND_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("FELT_AMEND_DATE").getString()));
                resultSetTemp.updateString("FELT_AMEND_ID", getAttribute("FELT_AMEND_ID").getString());
                resultSetTemp.updateString("FELT_AMEND_REASON", getAttribute("FELT_AMEND_REASON").getString());
                resultSetTemp.updateString("FELT_AMEND_PIECE_NO", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_PIECE_NO").getString());
                resultSetTemp.updateString("FELT_AMEND_PARTY_CODE", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_PARTY_CODE").getString());
                resultSetTemp.updateString("FELT_AMEND_PARTY_NAME", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_PARTY_NAME").getString());
                resultSetTemp.updateString("FELT_AMEND_ORDER_DATE", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_ORDER_DATE").getString());
                resultSetTemp.updateString("FELT_AMEND_PRODUCT_CODE", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_PRODUCT_CODE").getString());
                resultSetTemp.updateString("FELT_AMEND_GROUP", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_GROUP").getString());
                resultSetTemp.updateString("FELT_AMEND_STYLE", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_STYLE").getString());
                resultSetTemp.updateString("FELT_AMEND_LENGTH", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_LENGTH").getString());
                resultSetTemp.updateString("FELT_AMEND_WIDTH", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_WIDTH").getString());
                resultSetTemp.updateString("FELT_AMEND_GSM", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_GSM").getString());
                resultSetTemp.updateString("FELT_AMEND_WEIGHT", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_WEIGHT").getString());
                resultSetTemp.updateString("FELT_AMEND_SQMTR", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_SQMTR").getString());
                resultSetTemp.updateString("FELT_AMEND_BILL_LENGTH", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_BILL_LENGTH").getString());
                resultSetTemp.updateString("FELT_AMEND_BILL_WIDTH", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_BILL_WIDTH").getString());
                resultSetTemp.updateString("FELT_AMEND_BILL_GSM", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_BILL_GSM").getString());
                resultSetTemp.updateString("FELT_AMEND_BILL_WEIGHT", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_BILL_WEIGHT").getString());
                resultSetTemp.updateString("FELT_AMEND_BILL_SQMTR", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_BILL_SQMTR").getString());
                resultSetTemp.updateString("FELT_AMEND_BILL_PRODUCT_CODE", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_BILL_PRODUCT_CODE").getString());
                resultSetTemp.updateString("FELT_AMEND_REMARK", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_REMARK").getString());
                resultSetTemp.updateString("FELT_AMEND_MFG_STATUS", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_MFG_STATUS").getString());
                resultSetTemp.updateString("FELT_AMEND_INCHARGE_NAME", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_INCHARGE_NAME").getString());
                resultSetTemp.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                resultSetTemp.updateString("CREATED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetTemp.updateInt("MODIFIED_BY", 0);
                resultSetTemp.updateString("MODIFIED_DATE", "0000-00-00");
                resultSetTemp.updateBoolean("APPROVED", false);
                resultSetTemp.updateString("APPROVED_DATE", "0000-00-00");
                resultSetTemp.updateBoolean("REJECTED", false);
                resultSetTemp.updateString("REJECTED_DATE", "0000-00-00");
                resultSetTemp.updateInt("CANCELED", 0);
                resultSetTemp.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                resultSetTemp.updateInt("CHANGED", 1);
                resultSetTemp.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                resultSetTemp.insertRow();

                //Insert records into Felt_Order_Amendment_H table
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO", revisionNo);
                resultSetHistory.updateString("FELT_AMEND_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("FELT_AMEND_DATE").getString()));
                resultSetHistory.updateString("FELT_AMEND_ID", getAttribute("FELT_AMEND_ID").getString());
                resultSetHistory.updateString("FELT_AMEND_REASON", getAttribute("FELT_AMEND_REASON").getString());
                resultSetHistory.updateString("FELT_AMEND_PIECE_NO", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_PIECE_NO").getString());
                resultSetHistory.updateString("FELT_AMEND_PARTY_CODE", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_PARTY_CODE").getString());
                resultSetHistory.updateString("FELT_AMEND_PARTY_NAME", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_PARTY_NAME").getString());
                resultSetHistory.updateString("FELT_AMEND_ORDER_DATE", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_ORDER_DATE").getString());
                resultSetHistory.updateString("FELT_AMEND_PRODUCT_CODE", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_PRODUCT_CODE").getString());
                resultSetHistory.updateString("FELT_AMEND_GROUP", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_GROUP").getString());
                resultSetHistory.updateString("FELT_AMEND_STYLE", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_STYLE").getString());
                resultSetHistory.updateString("FELT_AMEND_LENGTH", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_LENGTH").getString());
                resultSetHistory.updateString("FELT_AMEND_WIDTH", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_WIDTH").getString());
                resultSetHistory.updateString("FELT_AMEND_GSM", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_GSM").getString());
                resultSetHistory.updateString("FELT_AMEND_WEIGHT", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_WEIGHT").getString());
                resultSetHistory.updateString("FELT_AMEND_SQMTR", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_SQMTR").getString());
                resultSetHistory.updateString("FELT_AMEND_BILL_LENGTH", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_BILL_LENGTH").getString());
                resultSetHistory.updateString("FELT_AMEND_BILL_WIDTH", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_BILL_WIDTH").getString());
                resultSetHistory.updateString("FELT_AMEND_BILL_GSM", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_BILL_GSM").getString());
                resultSetHistory.updateString("FELT_AMEND_BILL_WEIGHT", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_BILL_WEIGHT").getString());
                resultSetHistory.updateString("FELT_AMEND_BILL_SQMTR", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_BILL_SQMTR").getString());
                resultSetHistory.updateString("FELT_AMEND_BILL_PRODUCT_CODE", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_BILL_PRODUCT_CODE").getString());
                resultSetHistory.updateString("FELT_AMEND_REMARK", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_REMARK").getString());
                resultSetHistory.updateString("FELT_AMEND_MFG_STATUS", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_MFG_STATUS").getString());
                resultSetHistory.updateString("FELT_AMEND_INCHARGE_NAME", ObjFeltOrderUpdDetails.getAttribute("FELT_AMEND_INCHARGE_NAME").getString());
                resultSetHistory.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                resultSetHistory.updateString("CREATED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateInt("UPDATED_BY", (int) getAttribute("UPDATED_BY").getVal());
                resultSetHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
                resultSetHistory.updateString("APPROVER_REMARKS", getAttribute("FROM_REMARKS").getString());

                resultSetHistory.updateInt("MODIFIED_BY", 0);
                resultSetHistory.updateString("MODIFIED_DATE", "0000-00-00");
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

            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = 712; //Felt Order Updation Module
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("FELT_AMEND_ID").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.FELT_PIECE_REJECTION";
            ObjFeltProductionApprovalFlow.IsCreator = false;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "FELT_AMEND_ID";

            if (getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFeltProductionApprovalFlow.To = (int) getAttribute("SEND_DOC_TO").getVal();
                ObjFeltProductionApprovalFlow.ExplicitSendTo = true;
            }

            //==== Handling Rejected Documents ==========//
            boolean IsRejected = getAttribute("REJECTED").getBool();

            if (IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.FELT_PIECE_REJECTION SET REJECTED=0,CHANGED=1 WHERE FELT_AMEND_ID ='" + getAttribute("FELT_AMEND_ID").getString() + "'");

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
//            if (ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {
//                ObjFeltProductionApprovalFlow.finalApproved = false;
//                updatefeltorder(getAttribute("FELT_AMEND_ID").getString(), getAttribute("FELT_AMEND_REASON").getString());
//            }

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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PIECE_REJECTION WHERE  FELT_AMEND_ID='" + documentNo + "' AND APPROVED=" + 1;
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=712 AND USER_ID=" + userID + " AND DOC_NO='" + documentNo + "' AND STATUS='W'";
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();

                if (rsTmp.getInt("COUNT") > 0) {
                    strSQL = "DELETE FROM PRODUCTION.FELT_PIECE_REJECTION WHERE "
                            + " AND FELT_AMEND_DATE= '" + EITLERPGLOBAL.formatDateDB(stringProductionDate) + "'"
                            + " AND FELT_AMEND_ID='" + documentNo + "'";
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PIECE_REJECTION WHERE FELT_AMEND_ID='" + orderupdDocumentNo + "' AND APPROVED=1";
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=712 AND USER_ID=" + userID + " AND DOC_NO='" + orderupdDocumentNo + "' AND STATUS='W'";
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
            String strSql = "SELECT DISTINCT FELT_AMEND_ID,FELT_AMEND_DATE,FELT_AMEND_REASON FROM PRODUCTION.FELT_PIECE_REJECTION WHERE  " + stringFindQuery + "";
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
        int serialNoCounter = 0;
        int RevNo = 0;
        double totalWeight, previousWeight;
        try {
            setAttribute("REVISION_NO", 0);
            setAttribute("FELT_AMEND_DATE", resultSet.getDate("FELT_AMEND_DATE"));
            setAttribute("FELT_AMEND_ID", resultSet.getString("FELT_AMEND_ID"));
            setAttribute("FELT_AMEND_REASON", resultSet.getString("FELT_AMEND_REASON"));

            hmFeltOrderUpdDetails.clear();

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_PIECE_REJECTION WHERE FELT_AMEND_ID = '" + resultSet.getString("FELT_AMEND_ID") + "' AND FELT_AMEND_DATE='" + resultSet.getDate("FELT_AMEND_DATE") + "' ORDER BY FELT_AMEND_ID");

            while (resultSetTemp.next()) {
                serialNoCounter++;
                //              setAttribute("PRODUCTION_FORM_NO",resultSetTemp.getString("PROD_FORM_NO"));
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

                clsFeltPieceRejectionDetails ObjFeltOrderUpdDetails = new clsFeltPieceRejectionDetails();

                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_PIECE_NO", resultSetTemp.getString("FELT_AMEND_PIECE_NO"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_PARTY_CODE", resultSetTemp.getString("FELT_AMEND_PARTY_CODE"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_PARTY_NAME", resultSetTemp.getString("FELT_AMEND_PARTY_NAME"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_ORDER_DATE", resultSetTemp.getString("FELT_AMEND_ORDER_DATE"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_PRODUCT_CODE", resultSetTemp.getString("FELT_AMEND_PRODUCT_CODE"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_GROUP", resultSetTemp.getString("FELT_AMEND_GROUP"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_STYLE", resultSetTemp.getString("FELT_AMEND_STYLE"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_LENGTH", resultSetTemp.getString("FELT_AMEND_LENGTH"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_WIDTH", resultSetTemp.getString("FELT_AMEND_WIDTH"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_GSM", resultSetTemp.getString("FELT_AMEND_GSM"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_WEIGHT", resultSetTemp.getString("FELT_AMEND_WEIGHT"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_SQMTR", resultSetTemp.getString("FELT_AMEND_SQMTR"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_BILL_LENGTH", resultSetTemp.getString("FELT_AMEND_BILL_LENGTH"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_BILL_WIDTH", resultSetTemp.getString("FELT_AMEND_BILL_WIDTH"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_BILL_GSM", resultSetTemp.getString("FELT_AMEND_BILL_GSM"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_BILL_WEIGHT", resultSetTemp.getString("FELT_AMEND_BILL_WEIGHT"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_BILL_SQMTR", resultSetTemp.getString("FELT_AMEND_BILL_SQMTR"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_BILL_PRODUCT_CODE", resultSetTemp.getString("FELT_AMEND_BILL_PRODUCT_CODE"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_REMARK", resultSetTemp.getString("FELT_AMEND_REMARK"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_MFG_STATUS", resultSetTemp.getString("FELT_AMEND_MFG_STATUS"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_INCHARGE_NAME", resultSetTemp.getString("FELT_AMEND_INCHARGE_NAME"));

                hmFeltOrderUpdDetails.put(Integer.toString(serialNoCounter), ObjFeltOrderUpdDetails);
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
            RevNo = resultSet.getInt("REVISION_NO");
            setAttribute("REVISION_NO", resultSet.getInt("REVISION_NO"));

            //Now Populate the collection, first clear the collection
            hmFeltOrderUpdDetails.clear();

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_PIECE_REJECTION_H WHERE FELT_AMEND_DATE='" + pProductionDate + "' AND FELT_AMEND_ID='" + pDocNo + "' AND REVISION_NO='" + resultSet.getInt("REVISION_NO") + "' ORDER BY REVISION_NO");

            while (resultSetTemp.next()) {
                serialNoCounter++;
                setAttribute("FELT_AMEND_ID", resultSetTemp.getString("FELT_AMEND_ID"));
                setAttribute("UPDATED_BY", resultSetTemp.getInt("UPDATED_BY"));
                setAttribute("ENTRY_DATE", resultSetTemp.getString("ENTRY_DATE"));
                setAttribute("HIERARCHY_ID", resultSetTemp.getInt("HIERARCHY_ID"));

                clsFeltPieceRejectionDetails ObjFeltOrderUpdDetails = new clsFeltPieceRejectionDetails();

                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_PIECE_NO", resultSetTemp.getString("FELT_AMEND_PIECE_NO"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_PARTY_CODE", resultSetTemp.getString("FELT_AMEND_PARTY_CODE"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_PARTY_NAME", resultSetTemp.getString("FELT_AMEND_PARTY_NAME"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_ORDER_DATE", resultSetTemp.getString("FELT_AMEND_ORDER_DATE"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_PRODUCT_CODE", resultSetTemp.getString("FELT_AMEND_PRODUCT_CODE"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_GROUP", resultSetTemp.getString("FELT_AMEND_GROUP"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_STYLE", resultSetTemp.getString("FELT_AMEND_STYLE"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_LENGTH", resultSetTemp.getString("FELT_AMEND_LENGTH"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_WIDTH", resultSetTemp.getString("FELT_AMEND_WIDTH"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_GSM", resultSetTemp.getString("FELT_AMEND_GSM"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_WEIGHT", resultSetTemp.getString("FELT_AMEND_WEIGHT"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_SQMTR", resultSetTemp.getString("FELT_AMEND_SQMTR"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_BILL_LENGTH", resultSetTemp.getString("FELT_AMEND_BILL_LENGTH"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_BILL_WIDTH", resultSetTemp.getString("FELT_AMEND_BILL_WIDTH"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_BILL_GSM", resultSetTemp.getString("FELT_AMEND_BILL_GSM"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_BILL_WEIGHT", resultSetTemp.getString("FELT_AMEND_BILL_WEIGHT"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_BILL_SQMTR", resultSetTemp.getString("FELT_AMEND_BILL_SQMTR"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_BILL_PRODUCT_CODE", resultSetTemp.getString("FELT_AMEND_BILL_PRODUCT_CODE"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_REMARK", resultSetTemp.getString("FELT_AMEND_REMARK"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_MFG_STATUS", resultSetTemp.getString("FELT_AMEND_MFG_STATUS"));
                ObjFeltOrderUpdDetails.setAttribute("FELT_AMEND_INCHARGE_NAME", resultSetTemp.getString("FELT_AMEND_INCHARGE_NAME"));

                hmFeltOrderUpdDetails.put(Integer.toString(serialNoCounter), ObjFeltOrderUpdDetails);
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

    public static HashMap getHistoryList(String stringProductionDate, String productionDocumentNo) {
        HashMap hmHistoryList = new HashMap();
        Statement stTmp;
        ResultSet rsTmp;
        String stringProductionDate1 = EITLERPGLOBAL.formatDate(stringProductionDate);

        try {
            stTmp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp = stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_PIECE_REJECTION_H WHERE FELT_AMEND_ID='" + productionDocumentNo + "' GROUP BY REVISION_NO");

            while (rsTmp.next()) {
                clsFeltPieceRejection ObjFeltOrderUpd = new clsFeltPieceRejection();

                ObjFeltOrderUpd.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                ObjFeltOrderUpd.setAttribute("UPDATED_BY", rsTmp.getInt("UPDATED_BY"));
                ObjFeltOrderUpd.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                ObjFeltOrderUpd.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                ObjFeltOrderUpd.setAttribute("APPROVER_REMARKS", rsTmp.getString("APPROVER_REMARKS"));

                hmHistoryList.put(Integer.toString(hmHistoryList.size() + 1), ObjFeltOrderUpd);
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
            resultSet = statement.executeQuery("SELECT DISTINCT REVISION_NO FROM PRODUCTION.FELT_PIECE_REJECTION_H WHERE FELT_AMEND_DATE='" + pProductionDate + "' AND FELT_AMEND_ID ='" + pDocNo + "'");
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
                strSQL = "SELECT DISTINCT FELT_AMEND_ID,FELT_AMEND_DATE,RECEIVED_DATE,PARA_DESC FROM PRODUCTION.FELT_PIECE_REJECTION, PRODUCTION.FELT_PROD_DOC_DATA ,PRODUCTION.FELT_PARAMETER_MASTER  WHERE FELT_AMEND_ID=DOC_NO AND STATUS='W' AND MODULE_ID=712 AND CANCELED=0  AND USER_ID=" + pUserID + "  AND PARA_ID ='PIECE_AMEND' AND PARA_CODE = FELT_AMEND_REASON ORDER BY RECEIVED_DATE,FELT_AMEND_ID";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT FELT_AMEND_ID,FELT_AMEND_DATE,RECEIVED_DATE,PARA_DESC FROM PRODUCTION.FELT_PIECE_REJECTION, PRODUCTION.FELT_PROD_DOC_DATA ,PRODUCTION.FELT_PARAMETER_MASTER  WHERE FELT_AMEND_ID=DOC_NO AND STATUS='W' AND MODULE_ID=712 AND CANCELED=0  AND USER_ID=" + pUserID + "  AND PARA_ID ='PIECE_AMEND' AND PARA_CODE = FELT_AMEND_REASON ORDER BY FELT_AMEND_DATE,FELT_AMEND_ID";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT FELT_AMEND_ID,FELT_AMEND_DATE,RECEIVED_DATE,PARA_DESC FROM PRODUCTION.FELT_PIECE_REJECTION, PRODUCTION.FELT_PROD_DOC_DATA ,PRODUCTION.FELT_PARAMETER_MASTER  WHERE FELT_AMEND_ID=DOC_NO AND STATUS='W' AND MODULE_ID=712 AND CANCELED=0  AND USER_ID=" + pUserID + "  AND PARA_ID ='PIECE_AMEND' AND PARA_CODE = FELT_AMEND_REASON ORDER BY FELT_AMEND_ID";
            }

            ResultSet rsTmp = data.getConn().createStatement().executeQuery(strSQL);

            Counter = 0;
            while (rsTmp.next()) {

                Counter = Counter + 1;
                clsFeltPieceRejection ObjDoc = new clsFeltPieceRejection();

                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO", rsTmp.getString("FELT_AMEND_ID"));
                ObjDoc.setAttribute("DOC_DATE", rsTmp.getString("FELT_AMEND_DATE"));
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

    public String getPartyCode(String pPieceNo) {
        return data.getStringValueFromDB("SELECT PR_PARTY_CODE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + pPieceNo + "'");

    }

    public String getPartyName(String pPieceNo) {
        return data.getStringValueFromDB("SELECT B.PARTY_NAME FROM PRODUCTION.FELT_SALES_PIECE_REGISTER A,DINESHMILLS.D_SAL_PARTY_MASTER B WHERE A.PR_PARTY_CODE=B.PARTY_CODE AND A.PR_PIECE_NO='" + pPieceNo + "'");

    }

    public String getOrderDate(String pPieceNo) {
        return data.getStringValueFromDB("SELECT PR_ORDER_DATE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + pPieceNo + "'");

    }

    public String getProductCode(String pPieceNo) {
        return data.getStringValueFromDB("SELECT PR_PRODUCT_CODE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + pPieceNo + "'");

    }

    public String getGroupCD(String pPieceNo) {
        return data.getStringValueFromDB("SELECT PR_GROUP FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + pPieceNo + "'");
    }

    public String getGroupID(String pProductNo) {
        return data.getStringValueFromDB("SELECT PR_GROUP FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PRODUCT_CODE_CODE='" + pProductNo + "'");

    }

    public String getStyle(String pPieceNo) {
        return data.getStringValueFromDB("SELECT PR_STYLE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + pPieceNo + "'");

    }

    public String getLength(String pPieceNo) {
        return data.getStringValueFromDB("SELECT PR_LENGTH FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + pPieceNo + "'");

    }

    public String getWidth(String pPieceNo) {
        return data.getStringValueFromDB("SELECT PR_WIDTH FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + pPieceNo + "'");

    }

    public String getGSM(String pPieceNo) {
        return data.getStringValueFromDB("SELECT PR_GSM FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + pPieceNo + "'");

    }

    public String getWeight(String pPieceNo) {
        return data.getStringValueFromDB("SELECT PR_THORITICAL_WEIGHT FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + pPieceNo + "'");

    }

    public String getSqmtr(String pPieceNo) {
        return data.getStringValueFromDB("SELECT PR_SQMTR FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + pPieceNo + "'");

    }

    public String getBillLength(String pPieceNo) {
        return data.getStringValueFromDB("SELECT PR_BILL_LENGTH FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + pPieceNo + "'");

    }

    public String getBillWidth(String pPieceNo) {
        return data.getStringValueFromDB("SELECT PR_BILL_WIDTH FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + pPieceNo + "'");

    }

    public String getBillGSM(String pPieceNo) {
        return data.getStringValueFromDB("SELECT PR_BILL_GSM FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + pPieceNo + "'");

    }

    public String getBillWeight(String pPieceNo) {
        return data.getStringValueFromDB("SELECT PR_BILL_WEIGHT FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + pPieceNo + "'");

    }

    public String getBillSqmtr(String pPieceNo) {
        return data.getStringValueFromDB("SELECT PR_BILL_SQMTR FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + pPieceNo + "'");

    }

    public String getBillProductCode(String pPieceNo) {
        return data.getStringValueFromDB("SELECT PR_BILL_PRODUCT_CODE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + pPieceNo + "'");

    }

    public String getmfgstatus(String pPieceNo) {
        return data.getStringValueFromDB("SELECT PR_PIECE_STAGE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + pPieceNo + "'");

    }

    public String getInchargeName(String pPieceNo) {
        return data.getStringValueFromDB("SELECT B.INCHARGE_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER A,PRODUCTION.FELT_INCHARGE B,PRODUCTION.FELT_SALES_PIECE_REGISTER C WHERE A.INCHARGE_CD=B.INCHARGE_CD AND A.PARTY_CODE=C.PR_PARTY_CODE AND C.PR_PIECE_NO='" + pPieceNo + "'");

    }

    // Updates Weaving Date in Order Master Table To confirm that Weaving has completed
    private void updatefeltorder(String documentNo, String Reason) {
        if (Reason.equals("1")) {
            data.Execute("UPDATE PRODUCTION.FELT_PIECE_REJECTION,PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_BILL_LENGTH = FELT_AMEND_BILL_LENGTH,PR_BILL_WIDTH=FELT_AMEND_BILL_WIDTH,PR_BILL_GSM=FELT_AMEND_BILL_GSM,PR_BILL_WEIGHT=FELT_AMEND_BILL_WEIGHT,PR_BILL_SQMTR=ROUND((FELT_AMEND_BILL_LENGTH*FELT_AMEND_BILL_WIDTH),2) WHERE PR_PIECE_NO = FELT_AMEND_PIECE_NO AND APPROVED =1 AND FELT_AMEND_REASON =1 AND FELT_AMEND_ID ='" + documentNo + "'");
        }
        if (Reason.equals("2")) {
            data.Execute("UPDATE PRODUCTION.FELT_PIECE_REJECTION,PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PRIORITY_HOLD_CAN_FLAG = 9  WHERE PR_PIECE_NO = FELT_AMEND_PIECE_NO AND APPROVED =1 AND FELT_AMEND_REASON =2 AND FELT_AMEND_ID ='" + documentNo + "'");
        }
        if (Reason.equals("3")) {
            data.Execute("UPDATE PRODUCTION.FELT_PIECE_REJECTION,PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PRIORITY_HOLD_CAN_FLAG = 11  WHERE PR_PIECE_NO = FELT_AMEND_PIECE_NO AND APPROVED =1 AND FELT_AMEND_REASON =3 AND FELT_AMEND_ID ='" + documentNo + "'");
        }
        if (Reason.equals("4")) {
            data.Execute("UPDATE PRODUCTION.FELT_PIECE_REJECTION,PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PRIORITY_HOLD_CAN_FLAG = 12  WHERE PR_PIECE_NO = FELT_AMEND_PIECE_NO AND APPROVED =1 AND FELT_AMEND_REASON =4 AND FELT_AMEND_ID ='" + documentNo + "'");
        }
        if (Reason.equals("5")) {
            data.Execute("UPDATE PRODUCTION.FELT_PIECE_REJECTION,PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PRIORITY_HOLD_CAN_FLAG = 7  WHERE PR_PIECE_NO = FELT_AMEND_PIECE_NO AND APPROVED =1 AND FELT_AMEND_REASON =5 AND FELT_AMEND_ID ='" + documentNo + "'");
        }
        if (Reason.equals("6")) {
            data.Execute("UPDATE PRODUCTION.FELT_PIECE_REJECTION,PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PRIORITY_HOLD_CAN_FLAG = 8  WHERE PR_PIECE_NO = FELT_AMEND_PIECE_NO AND APPROVED =1 AND FELT_AMEND_REASON =6 AND FELT_AMEND_ID ='" + documentNo + "'");
        }
        if (Reason.equals("7")) {
            data.Execute("UPDATE PRODUCTION.FELT_PIECE_REJECTION,PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PRIORITY_HOLD_CAN_FLAG = 1  WHERE PR_PIECE_NO = FELT_AMEND_PIECE_NO AND APPROVED =1 AND FELT_AMEND_REASON =7 AND FELT_AMEND_ID ='" + documentNo + "'");
        }
        if (Reason.equals("8")) {
            data.Execute("UPDATE PRODUCTION.FELT_PIECE_REJECTION,PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PIECE_STAGE = 'EXP-INVOICE'  WHERE PR_PIECE_NO = FELT_AMEND_PIECE_NO AND APPROVED =1 AND FELT_AMEND_REASON =8 AND FELT_AMEND_ID ='" + documentNo + "'");
        }
        if (Reason.equals("9")) {
            String s;
            s = "UPDATE PRODUCTION.FELT_PIECE_REJECTION,PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_LENGTH = FELT_AMEND_LENGTH,PR_WIDTH=FELT_AMEND_WIDTH,PR_GSM=FELT_AMEND_GSM,PR_THORITICAL_WEIGHT=ROUND((FELT_AMEND_LENGTH*FELT_AMEND_WIDTH*FELT_AMEND_GSM)/1000,1),PR_SQMTR=ROUND((FELT_AMEND_LENGTH*FELT_AMEND_WIDTH),2),PR_BILL_LENGTH = FELT_AMEND_LENGTH,PR_BILL_WIDTH=FELT_AMEND_WIDTH,PR_BILL_GSM=FELT_AMEND_GSM,PR_BILL_SQMTR=ROUND((FELT_AMEND_LENGTH*FELT_AMEND_WIDTH),2) WHERE PR_PIECE_NO = FELT_AMEND_PIECE_NO AND APPROVED =1 AND FELT_AMEND_REASON =9 AND FELT_AMEND_ID ='" + documentNo + "'";
            System.out.println(s);
            data.Execute(s);
        }
        if (Reason.equals("10")) {
            String r;
            r = "UPDATE PRODUCTION.FELT_PIECE_REJECTION,PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PRODUCT_CODE=FELT_AMEND_PRODUCT_CODE,PR_GROUP=FELT_AMEND_GROUP,PR_BILL_PRODUCT_CODE=FELT_AMEND_PRODUCT_CODE WHERE PR_PIECE_NO = FELT_AMEND_PIECE_NO AND APPROVED =1 AND FELT_AMEND_REASON =10 AND FELT_AMEND_ID ='" + documentNo + "'";
            System.out.println(r);
            data.Execute(r);
        }
        
//        if (Reason.equals("11")) {
//            String RJ,JR,RP;
//            RJ = "INSERT INTO PRODUCTION.FELT_SALES_PIECE_REGISTER SELECT CONCAT(TRIM(PR_PIECE_NO),'RM'),PR_DATE,PR_ORDER_DATE,PR_DOC_NO,PR_MACHINE_NO,PR_POSITION_NO,PR_PARTY_CODE,PR_PRODUCT_CODE,PR_GROUP,PR_STYLE,PR_LENGTH,PR_WIDTH,PR_GSM,PR_THORITICAL_WEIGHT,PR_SQMTR,PR_SYN_PER,PR_REQUESTED_MONTH,PR_REGION,PR_INCHARGE,PR_REFERENCE,PR_REFERENCE_DATE,PR_PO_NO,PR_PO_DATE,PR_ORDER_REMARK,PR_PIECE_REMARK,PR_PIECE_STAGE,PR_WARP_DATE,PR_WVG_DATE,PR_MND_DATE,PR_NDL_DATE,PR_FNSG_DATE,PR_RCV_DATE,PR_ACTUAL_WEIGHT,PR_ACTUAL_LENGTH,PR_ACTUAL_WIDTH,PR_BALE_NO,PR_PACKED_DATE,PR_REJECTED_FLAG,PR_REJECTED_REMARK,PR_DIVERSION_FLAG,PR_DIVERSION_REASON,PR_DIVERTED_FLAG,PR_DIVERTED_REASON,PR_EXP_DISPATCH_DATE,PR_PRIORITY_HOLD_CAN_FLAG,PR_INVOICE_NO,PR_INVOICE_DATE,PR_LR_NO,PR_LR_DATE,PR_INVOICE_PARTY,PR_PARTY_CODE_ORIGINAL,PR_PIECE_NO_ORIGINAL,PR_WH_CODE,PR_INWARD_NO,PR_RACK_NO,PR_PIECE_ID,PR_LOCATION,PR_HOLD_DATE,PR_HOLD_REASON,PR_RELEASE_DATE,CREATED_DATE,CREATED_BY,MODIFIED_DATE,MODIFIED_BY,HIERARCHY_ID,APPROVER_BY,APPROVER_DATE,APPROVER_REMARK,BALE_REOPEN_FLG,WVG_LAYER_REMARK,PR_BILL_LENGTH,PR_BILL_WIDTH,PR_BILL_WEIGHT,PR_BILL_SQMTR,PR_BILL_GSM,PR_BILL_PRODUCT_CODE,PR_PKG_DP_NO,PR_PKG_DP_DATE,PR_SALES_RETURNS_NO,PR_SALES_RETURNS_DATE,PR_SALES_RETURNS_REMARKS,PR_SALES_RETURNS_FLG,PR_DELINK,PR_DELINK_REASON FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='"+documentNo+"'";
//            JR = "UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PIECE_STAGE='WEAVING'WHERE PR_PIECE_NO='"+PIECE_NO+"RM'";
//            RP = "UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_DELINK='DELINK',PR_DELINK_REMARKS='"+REMAKRS+"' WHERE PR_PIECE_NO='"+PIECE_NO+"'";
//            System.out.println(RJ);
//            System.out.println(JR);
//            System.out.println(RP);
//            data.Execute(RJ);
//            data.Execute(JR);
//            data.Execute(RP);
//        }
    }

    // private void updateWeavingcounter(String documentNo){
    //   data.Execute("UPDATE PRODUCTION.FELT_PROD_DATA  SET WEAVE_DIFF_DAYS =DATEDIFF(PROD_DATE,WEAVE_DATE)  WHERE PROD_DEPT='WEAVING'  AND PROD_DOC_NO='"+documentNo+"'");
   // }
    // checks piece no already exist in db
    // public boolean checkPieceNoInDB(String pPieceNo){
    //   int count=data.getIntValueFromDB("SELECT COUNT(FELT_PIECE_NO) FROM PRODUCTION.FELT_PIECE_REJECTION WHERE FELT_PIECE_NO='"+pPieceNo+"' AND FELT_AMEND_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND FELT_AMEND_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
    //  if(count>0) return true;
    //  else return false;
    // }
    public boolean checkPieceNoInDB1(String pPieceNo, String pAmend_Reason, String pProdindstring, String pAgreedindstring) {
        int count = data.getIntValueFromDB("SELECT COUNT(FELT_PIECE_NO) FROM PRODUCTION.FELT_PIECE_REJECTION WHERE FELT_PIECE_NO='" + pPieceNo + "' AND FELT_AMEND_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND FELT_AMEND_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'");
        if (count >= 1) {

            int counter = 0;

            try {
                Connection Conn = data.getConn();
                Statement stTmp = Conn.createStatement();
                ResultSet rsTmp = stTmp.executeQuery("SELECT FELT_AMEND_DATE FROM PRODUCTION.FELT_PIECE_REJECTION WHERE FELT_PIECE_NO='" + pPieceNo + "' AND FELT_AMEND_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND FELT_AMEND_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'");
                while (rsTmp.next()) {
                    //                 if(rsTmp.getString("FELT_AMEND_DATE").equals(EITLERPGLOBAL.formatDateDB(pProdDate))) counter++;
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
        int count = data.getIntValueFromDB("SELECT COUNT(FELT_AMEND_DATE) FROM PRODUCTION.FELT_PIECE_REJECTION WHERE FELT_AMEND_DATE='" + pProdDate + "'");
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

    public static HashMap getAmendReasonList() {
        HashMap List = new HashMap();
        try {
            Connection Conn = data.getConn();
            Statement stTmp = Conn.createStatement();
            int counter = 1;
            ResultSet rsTmp = stTmp.executeQuery("SELECT PARA_CODE,PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID = 'PIECE_REJECTION' ORDER BY PARA_CODE");
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

    /* 
    
    
     public static HashMap getAmendReasonList(int pCompanyID,int pUserID) {
       
     Connection tmpConn;
     ResultSet rsTmp;
     Statement tmpStmt;
     HashMap List=new HashMap();        
     tmpConn= data.getConn();
     long Counter=0;
        
     try {
     tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
     rsTmp=tmpStmt.executeQuery("SELECT '0' AS PARA_CODE,'SELECT REASON CODE' AS PARA_DESC UNION ALL SELECT PARA_CODE,PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID = 'ORDER_UPD_REASON' ORDER BY PARA_CODE");
     rsTmp.first();
     Counter=0;
            
     while(!rsTmp.isAfterLast()) {
     Counter=Counter+1;
     clsFeltPieceRejection ObjReason=new clsFeltPieceRejection();
                
     ObjReason.setAttribute("PARA_CODE",rsTmp.getInt("PARA_CODE"));
     ObjReason.setAttribute("PARA_DESC",rsTmp.getString("PARA_DESC"));
                                
     List.put(Long.toString(Counter),ObjReason);
     rsTmp.next();
     }
            
     //tmpConn.close();
     rsTmp.close();
     tmpStmt.close();
            
     return List;
     }
     catch(Exception e) {
     }
     return List;
       
     }*/
    /*   
     // generates report data
     public  TReportWriter.SimpleDataProvider.TTable getReportData(String prodDate) {
     TReportWriter.SimpleDataProvider.TTable objData=new TReportWriter.SimpleDataProvider.TTable();
     objData.AddColumn("SR_NO");
     objData.AddColumn("FORM_NO");
     objData.AddColumn("PROD_DATE");
     objData.AddColumn("DOC_NO");
     objData.AddColumn("PIECE_NO");
     objData.AddColumn("STYLE");
     objData.AddColumn("PARTY_NAME");
     objData.AddColumn("WEIGHT");
     objData.AddColumn("PICKS_PER_10CMS");
     objData.AddColumn("REED_SPACE");
     objData.AddColumn("LENGTH");
     objData.AddColumn("LOOM_NO");
     objData.AddColumn("ORDER_LENGTH");
     objData.AddColumn("ORDER_WIDTH");
     objData.AddColumn("GSM");
     objData.AddColumn("GROUP");
     objData.AddColumn("DAY_WEIGHT");
     objData.AddColumn("PREVIOUS_WEIGHT");
     objData.AddColumn("BASE_WEIGHT");
     objData.AddColumn("CLOTH_WEIGHT");
     objData.AddColumn("TOTAL_WEIGHT");
        
     try{
     TReportWriter.SimpleDataProvider.TRow objRow=objData.newRow();
            
     String str ="SELECT PROD_DATE,PROD_FORM_NO,PROD_PIECE_NO,STYLE, WEIGHT,PICKS_PER_10CMS,REED_SPACE,LENGTH,LOOM_NO,GSQ,LNGTH,WIDTH,PARTY_NAME,PREVIOUS_WEIGHT,DAY_WEIGHT,TOTAL_WEIGHT,BASE_WEIGHT,CLOTH_WEIGHT,GRUP FROM (SELECT PROD_DATE,PROD_FORM_NO,REPLACE(PROD_PIECE_NO,' ','') PROD_PIECE_NO,PROD_PARTY_CODE,WEIGHT,PICKS_PER_10CMS,REED_SPACE,LENGTH,LOOM_NO FROM PRODUCTION.FELT_PROD_DATA A WHERE PROD_DATE = '"+prodDate+"' AND PROD_DEPT='WEAVING') D LEFT JOIN (SELECT REPLACE(BALNK,' ','') AS STYLE,GRUP, GSQ,LNGTH,WIDTH,PRODUCT_CODE,PIECE_NO,PARTY_CD FROM PRODUCTION.FELT_ORDER_MASTER) O ON O.PARTY_CD = D.PROD_PARTY_CODE AND O.PIECE_NO +0 = D.PROD_PIECE_NO +0 LEFT JOIN (SELECT PARTY_CODE,TRIM(PARTY_NAME) PARTY_NAME FROM D_SAL_PARTY_MASTER) AS P ON P.PARTY_CODE = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS PREVIOUS_WEIGHT FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'WEAVING' AND MONTH(PROD_DATE)=MONTH('"+prodDate+"') AND YEAR(PROD_DATE)=YEAR('"+prodDate+"') AND PROD_DATE<'"+prodDate+"' AND APPROVED=1) AS W ON O.PARTY_CD = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS DAY_WEIGHT FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'WEAVING' AND MONTH(PROD_DATE)=MONTH('"+prodDate+"') AND YEAR(PROD_DATE)=YEAR('"+prodDate+"') AND PROD_DATE='"+prodDate+"' AND APPROVED=1) AS DW ON O.PARTY_CD = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS TOTAL_WEIGHT FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'WEAVING' AND MONTH(PROD_DATE)=MONTH('"+prodDate+"') AND YEAR(PROD_DATE)=YEAR('"+prodDate+"') AND PROD_DATE<='"+prodDate+"' AND APPROVED=1) AS T ON O.PARTY_CD = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS BASE_WEIGHT FROM (SELECT BALNK AS STYLE, A.WEIGHT FROM PRODUCTION.FELT_PROD_DATA A, PRODUCTION.FELT_ORDER_MASTER B WHERE PIECE_NO +0 = PROD_PIECE_NO +0 AND PROD_DATE <= '"+prodDate+"' AND PROD_DEPT='WEAVING' AND MONTH(PROD_DATE)=MONTH('"+prodDate+"') AND YEAR(PROD_DATE)=YEAR('"+prodDate+"') AND APPROVED=1) AS O WHERE SUBSTRING(O.STYLE,1,3) IN('XY','XYA','Y')) AS BW ON O.PARTY_CD = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS CLOTH_WEIGHT FROM  (SELECT BALNK AS STYLE, A.WEIGHT FROM PRODUCTION.FELT_PROD_DATA A, PRODUCTION.FELT_ORDER_MASTER B WHERE PIECE_NO +0 = PROD_PIECE_NO +0 AND PROD_DATE <= '"+prodDate+"' AND PROD_DEPT='WEAVING' AND MONTH(PROD_DATE)=MONTH('"+prodDate+"') AND YEAR(PROD_DATE)=YEAR('"+prodDate+"') AND APPROVED=1) AS O WHERE SUBSTRING(O.STYLE,1,3) NOT IN('XY','XYA','Y')) AS CW ON O.PARTY_CD = D.PROD_PARTY_CODE";
     ResultSet  rsTemp =data.getResult(str);
     int counter=1;
     while(!rsTemp.isAfterLast()){
     objRow=objData.newRow();
     objRow.setValue("SR_NO", String.valueOf(counter));
     objRow.setValue("FORM_NO", rsTemp.getString("PROD_FORM_NO"));
     objRow.setValue("PROD_DATE", EITLERPGLOBAL.formatDate(rsTemp.getString("PROD_DATE")));
     objRow.setValue("PIECE_NO", rsTemp.getString("PROD_PIECE_NO"));
     objRow.setValue("STYLE", rsTemp.getString("STYLE"));
     objRow.setValue("PARTY_NAME", rsTemp.getString("PARTY_NAME"));
     objRow.setValue("WEIGHT", rsTemp.getString("WEIGHT"));
     objRow.setValue("PICKS_PER_10CMS",rsTemp.getString("PICKS_PER_10CMS"));
     objRow.setValue("REED_SPACE",rsTemp.getString("REED_SPACE"));
     objRow.setValue("LENGTH", rsTemp.getString("LENGTH"));
     objRow.setValue("LOOM_NO",rsTemp.getString("LOOM_NO"));
     objRow.setValue("ORDER_LENGTH",rsTemp.getString("LNGTH"));
     objRow.setValue("ORDER_WIDTH",rsTemp.getString("WIDTH"));
     objRow.setValue("ORDER_GSM",rsTemp.getString("GSQ"));
     objRow.setValue("ORDER_GROUP", rsTemp.getString("GRUP"));
     objRow.setValue("DAY_WEIGHT", rsTemp.getString("DAY_WEIGHT"));
     objRow.setValue("PREVIOUS_WEIGHT", rsTemp.getString("PREVIOUS_WEIGHT"));
     objRow.setValue("BASE_WEIGHT", rsTemp.getString("BASE_WEIGHT"));
     objRow.setValue("CLOTH_WEIGHT", rsTemp.getString("CLOTH_WEIGHT"));
     objRow.setValue("TOTAL_WEIGHT", rsTemp.getString("TOTAL_WEIGHT"));
                
     objData.AddRow(objRow);
     counter++;
     rsTemp.next();
     }
     }catch(Exception e) {
     e.printStackTrace();
     }
     return objData;
     }
     */
    public static String[] getPiecedetail(String pPieceNo, String pPartyCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String[] Piecedetail = new String[40];
        String[] error = {"error"};

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            //rsTmp=stTmp.executeQuery("SELECT GSQ FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO="+pPieceNo+" AND PARTY_CD='"+pPartyCode+"'");
            String strSQL = "SELECT PIECE_NO,PARTY_CD,ORDER_DATE,PRODUCT_CODE,GRUP,BALNK AS STYLE,LNGTH,WIDTH,GSQ,WEIGHT,DELIV_DATE,COMM_DATE,AGREED_DATE,REV_REQ_DATE,REV_COMM_DATE,REV_AGREED_DATE,REV_REQ_REASON,REV_COMM_REASON,MACHINE_NO,POSITION,REF_NO,CONF_NO,PO_NO,PO_DATE,PO_REMARK,WVG_DATE,MND_DATE,NDL_DATE FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO=" + pPieceNo + " AND PARTY_CD='" + pPartyCode + "'";
            rsTmp = stTmp.executeQuery(strSQL);
            System.out.println(strSQL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {

                Piecedetail[1] = rsTmp.getString("PIECE_NO");
                Piecedetail[2] = rsTmp.getString("PARTY_CD");
                Piecedetail[3] = rsTmp.getString("ORDER_DATE");
                //Piecedetail[4]=rsTmp.getString("PRODUCT_C0DE");
                Piecedetail[5] = rsTmp.getString("GRUP");
                Piecedetail[6] = rsTmp.getString("STYLE");
                Piecedetail[7] = rsTmp.getString("LNGTH");
                Piecedetail[8] = rsTmp.getString("WIDTH");
                Piecedetail[9] = rsTmp.getString("GSQ");
                Piecedetail[10] = rsTmp.getString("WEIGHT");
                Piecedetail[11] = rsTmp.getString("DELIV_DATE");
                Piecedetail[12] = rsTmp.getString("COMM_DATE");
                Piecedetail[13] = rsTmp.getString("WVG_AGREED_DATE");
                Piecedetail[14] = rsTmp.getString("REV_REQ_DATE");
                Piecedetail[15] = rsTmp.getString("REV_COMM_DATE");
                Piecedetail[16] = rsTmp.getString("REV_AGREED_DATE");
                Piecedetail[17] = rsTmp.getString("REV_REQ_REASON");
                Piecedetail[18] = rsTmp.getString("REV_COMM_REASON");
                Piecedetail[19] = rsTmp.getString("MACHINE_NO");
                Piecedetail[20] = rsTmp.getString("POSITION");
                Piecedetail[21] = rsTmp.getString("REF_NO");
                Piecedetail[22] = rsTmp.getString("CONF_NO");
                Piecedetail[23] = rsTmp.getString("PO_NO");
                Piecedetail[24] = rsTmp.getString("PO_DATE");
                Piecedetail[25] = rsTmp.getString("PO_REMARKS");
                Piecedetail[26] = EITLERPGLOBAL.formatDate(rsTmp.getString("WVG_DATE"));
                Piecedetail[27] = EITLERPGLOBAL.formatDate(rsTmp.getString("MND_DATE"));
                Piecedetail[28] = EITLERPGLOBAL.formatDate(rsTmp.getString("NDL_DATE"));
  //              Piecedetail[30]=rsTmp.getString("INCHARGE_NAME");
                /*Piecedetail[31]=rsTmp.getString("CHEM_TRT_IN");
                 Piecedetail[32]=rsTmp.getString("PIN_IND");
                 Piecedetail[33]=rsTmp.getString("CHARGES");
                 Piecedetail[34]=rsTmp.getString("SPR_IND");
                 Piecedetail[35]=rsTmp.getString("SQM_IND");                
                 */
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            return Piecedetail;

        } catch (Exception e) {
            e.printStackTrace();
            return error;
        }
    }

    public static String getNextFreeNo(int pModuleID, boolean UpdateLastNo) {
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
            strSQL = "SELECT LAST_USED_NO,PADDING_BY,NO_LENGTH,PREFIX_CHARS,SUFFIX_CHARS FROM D_COM_FIRSTFREE WHERE COMPANY_ID=2 AND MODULE_ID=712 AND FIRSTFREE_NO=228";
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
                    data.Execute("UPDATE D_COM_FIRSTFREE SET LAST_USED_NO='" + strNewNo.trim() + "',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=2 AND MODULE_ID=712 AND FIRSTFREE_NO=228");
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

}
