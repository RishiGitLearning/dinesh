/*
 * clsFeltPieceAmend.java
 *
 * Created on March 12, 2013, 3:10 PM
 */
package EITLERP.FeltSales.LocationAssignment;

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
public class clsFeltLocationAssignment {

    public String LastError = "";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet;
     private int FFNo = 0;
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
    public clsFeltLocationAssignment() {
        LastError = "";
        props = new HashMap();
        props.put("DOC_DATE", new Variant(""));
        props.put("DOC_NO", new Variant(""));
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
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_LOCATION_ASSIGNMENT ORDER BY DOC_DATE,SR_NO*1");
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
            setData();
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
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_LOCATION_ASSIGNMENT WHERE DOC_DATE='' ORDER BY SR_NO*1");

            // Felt order Updation data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_LOCATION_ASSIGNMENT_H WHERE DOC_DATE=''");

            // Felt order Updation Last Free No, 
            //setAttribute("DOC_NO", EITLERP.clsFirstFree.getNextFreeNo(2, 770, getAttribute("FFNO").getInt(), true));

            //Now Insert records into Felt_Order_Amendment & History tables
            for (int i = 1; i <= hmFeltOrderUpdDetails.size(); i++) {
                clsFeltLocationAssignmentDetails ObjFeltOrderUpdDetails = (clsFeltLocationAssignmentDetails) hmFeltOrderUpdDetails.get(Integer.toString(i));

                //Insert records into Felt order Amendment table
                resultSetTemp.moveToInsertRow();

                resultSetTemp.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                resultSetTemp.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetTemp.updateString("LOCATION_ID", getAttribute("LOCATION_ID").getString());
                resultSetTemp.updateString("SR_NO", ObjFeltOrderUpdDetails.getAttribute("SR_NO").getString());
                resultSetTemp.updateString("PIECE_NO", ObjFeltOrderUpdDetails.getAttribute("PIECE_NO").getString());
                resultSetTemp.updateString("PARTY_CODE", ObjFeltOrderUpdDetails.getAttribute("PARTY_CODE").getString());
                resultSetTemp.updateString("PARTY_NAME", ObjFeltOrderUpdDetails.getAttribute("PARTY_NAME").getString());
                resultSetTemp.updateString("ORDER_DATE", ObjFeltOrderUpdDetails.getAttribute("ORDER_DATE").getString());
                resultSetTemp.updateString("PRODUCT_CODE", ObjFeltOrderUpdDetails.getAttribute("PRODUCT_CODE").getString());
                resultSetTemp.updateString("GROUP", ObjFeltOrderUpdDetails.getAttribute("GROUP").getString());
                resultSetTemp.updateString("STYLE", ObjFeltOrderUpdDetails.getAttribute("STYLE").getString());
                resultSetTemp.updateString("LENGTH", ObjFeltOrderUpdDetails.getAttribute("LENGTH").getString());
                resultSetTemp.updateString("WIDTH", ObjFeltOrderUpdDetails.getAttribute("WIDTH").getString());
                resultSetTemp.updateString("GSM", ObjFeltOrderUpdDetails.getAttribute("GSM").getString());
                resultSetTemp.updateString("WEIGHT", ObjFeltOrderUpdDetails.getAttribute("WEIGHT").getString());
                resultSetTemp.updateString("SQMTR", ObjFeltOrderUpdDetails.getAttribute("SQMTR").getString());
                resultSetTemp.updateString("BILL_LENGTH", ObjFeltOrderUpdDetails.getAttribute("BILL_LENGTH").getString());
                resultSetTemp.updateString("BILL_WIDTH", ObjFeltOrderUpdDetails.getAttribute("BILL_WIDTH").getString());
                resultSetTemp.updateString("BILL_GSM", ObjFeltOrderUpdDetails.getAttribute("BILL_GSM").getString());
                resultSetTemp.updateString("BILL_WEIGHT", ObjFeltOrderUpdDetails.getAttribute("BILL_WEIGHT").getString());
                resultSetTemp.updateString("BILL_SQMTR", ObjFeltOrderUpdDetails.getAttribute("BILL_SQMTR").getString());
                resultSetTemp.updateString("BILL_PRODUCT_CODE", ObjFeltOrderUpdDetails.getAttribute("BILL_PRODUCT_CODE").getString());
                resultSetTemp.updateString("MFG_STATUS", ObjFeltOrderUpdDetails.getAttribute("MFG_STATUS").getString());
                resultSetTemp.updateString("INCHARGE_NAME", ObjFeltOrderUpdDetails.getAttribute("INCHARGE_NAME").getString());
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
                resultSetHistory.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetHistory.updateString("LOCATION_ID", getAttribute("LOCATION_ID").getString());
                resultSetHistory.updateString("SR_NO", ObjFeltOrderUpdDetails.getAttribute("SR_NO").getString());
                resultSetHistory.updateString("PIECE_NO", ObjFeltOrderUpdDetails.getAttribute("PIECE_NO").getString());
                resultSetHistory.updateString("PARTY_CODE", ObjFeltOrderUpdDetails.getAttribute("PARTY_CODE").getString());
                resultSetHistory.updateString("PARTY_NAME", ObjFeltOrderUpdDetails.getAttribute("PARTY_NAME").getString());
                resultSetHistory.updateString("ORDER_DATE", ObjFeltOrderUpdDetails.getAttribute("ORDER_DATE").getString());
                resultSetHistory.updateString("PRODUCT_CODE", ObjFeltOrderUpdDetails.getAttribute("PRODUCT_CODE").getString());
                resultSetHistory.updateString("GROUP", ObjFeltOrderUpdDetails.getAttribute("GROUP").getString());
                resultSetHistory.updateString("STYLE", ObjFeltOrderUpdDetails.getAttribute("STYLE").getString());
                resultSetHistory.updateString("LENGTH", ObjFeltOrderUpdDetails.getAttribute("LENGTH").getString());
                resultSetHistory.updateString("WIDTH", ObjFeltOrderUpdDetails.getAttribute("WIDTH").getString());
                resultSetHistory.updateString("GSM", ObjFeltOrderUpdDetails.getAttribute("GSM").getString());
                resultSetHistory.updateString("WEIGHT", ObjFeltOrderUpdDetails.getAttribute("WEIGHT").getString());
                resultSetHistory.updateString("BILL_LENGTH", ObjFeltOrderUpdDetails.getAttribute("BILL_LENGTH").getString());
                resultSetHistory.updateString("BILL_WIDTH", ObjFeltOrderUpdDetails.getAttribute("BILL_WIDTH").getString());
                resultSetHistory.updateString("BILL_GSM", ObjFeltOrderUpdDetails.getAttribute("BILL_GSM").getString());
                resultSetHistory.updateString("BILL_WEIGHT", ObjFeltOrderUpdDetails.getAttribute("BILL_WEIGHT").getString());
                resultSetHistory.updateString("BILL_SQMTR", ObjFeltOrderUpdDetails.getAttribute("BILL_SQMTR").getString());
                resultSetHistory.updateString("BILL_PRODUCT_CODE", ObjFeltOrderUpdDetails.getAttribute("BILL_PRODUCT_CODE").getString());
                resultSetHistory.updateString("MFG_STATUS", ObjFeltOrderUpdDetails.getAttribute("MFG_STATUS").getString());
                resultSetHistory.updateString("INCHARGE_NAME", ObjFeltOrderUpdDetails.getAttribute("INCHARGE_NAME").getString());
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
            ObjFeltProductionApprovalFlow.ModuleID = 770; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("DOC_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName = "FELT_LOCATION_ASSIGNMENT";
            ObjFeltProductionApprovalFlow.IsCreator = true;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "DOC_NO";

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
               // updatefeltorder(getAttribute("DOC_NO").getString(), getAttribute("LOCATION_ID").getString());
                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_LOCATION_ID='" +getAttribute("LOCATION_ID").getString()  + ",REJECTED=0,CHANGED=1  WHERE PR_PIECE_NO='" + getAttribute("PIECE_NO").getString() + "' ");
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
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_LOCATION_ASSIGNMENT WHERE DOC_DATE='' ORDER BY SR_NO*1");

            // Production data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //Get the Maximum Revision No in History Table.
            resultSetHistory = statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_LOCATION_ASSIGNMENT_H WHERE DOC_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()) + "' AND DOC_NO='" + getAttribute("DOC_NO").getString() + "'");
            resultSetHistory.first();
            if (resultSetHistory.getRow() > 0) {
                revisionNo = resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from production data table before insert
            statementHistory.execute("DELETE FROM PRODUCTION.FELT_LOCATION_ASSIGNMENT WHERE DOC_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()) + "' AND DOC_NO='" + getAttribute("DOC_NO").getString() + "'");

            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_LOCATION_ASSIGNMENT_H WHERE DOC_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()) + "' AND DOC_NO='" + getAttribute("DOC_NO").getString() + "'");

            //Now Update records into Felt_Order_Amendment tables
            for (int i = 1; i <= hmFeltOrderUpdDetails.size(); i++) {
                clsFeltLocationAssignmentDetails ObjFeltOrderUpdDetails = (clsFeltLocationAssignmentDetails) hmFeltOrderUpdDetails.get(Integer.toString(i));

                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                resultSetTemp.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetTemp.updateString("LOCATION_ID", getAttribute("LOCATION_ID").getString());
                resultSetTemp.updateString("SR_NO", ObjFeltOrderUpdDetails.getAttribute("SR_NO").getString());
                resultSetTemp.updateString("PIECE_NO", ObjFeltOrderUpdDetails.getAttribute("PIECE_NO").getString());
                resultSetTemp.updateString("PARTY_CODE", ObjFeltOrderUpdDetails.getAttribute("PARTY_CODE").getString());
                resultSetTemp.updateString("PARTY_NAME", ObjFeltOrderUpdDetails.getAttribute("PARTY_NAME").getString());
                resultSetTemp.updateString("ORDER_DATE", ObjFeltOrderUpdDetails.getAttribute("ORDER_DATE").getString());
                resultSetTemp.updateString("PRODUCT_CODE", ObjFeltOrderUpdDetails.getAttribute("PRODUCT_CODE").getString());
                resultSetTemp.updateString("GROUP", ObjFeltOrderUpdDetails.getAttribute("GROUP").getString());
                resultSetTemp.updateString("STYLE", ObjFeltOrderUpdDetails.getAttribute("STYLE").getString());
                resultSetTemp.updateString("LENGTH", ObjFeltOrderUpdDetails.getAttribute("LENGTH").getString());
                resultSetTemp.updateString("WIDTH", ObjFeltOrderUpdDetails.getAttribute("WIDTH").getString());
                resultSetTemp.updateString("GSM", ObjFeltOrderUpdDetails.getAttribute("GSM").getString());
                resultSetTemp.updateString("WEIGHT", ObjFeltOrderUpdDetails.getAttribute("WEIGHT").getString());
                resultSetTemp.updateString("SQMTR", ObjFeltOrderUpdDetails.getAttribute("SQMTR").getString());
                resultSetTemp.updateString("BILL_LENGTH", ObjFeltOrderUpdDetails.getAttribute("BILL_LENGTH").getString());
                resultSetTemp.updateString("BILL_WIDTH", ObjFeltOrderUpdDetails.getAttribute("BILL_WIDTH").getString());
                resultSetTemp.updateString("BILL_GSM", ObjFeltOrderUpdDetails.getAttribute("BILL_GSM").getString());
                resultSetTemp.updateString("BILL_WEIGHT", ObjFeltOrderUpdDetails.getAttribute("BILL_WEIGHT").getString());
                resultSetTemp.updateString("BILL_SQMTR", ObjFeltOrderUpdDetails.getAttribute("BILL_SQMTR").getString());
                resultSetTemp.updateString("BILL_PRODUCT_CODE", ObjFeltOrderUpdDetails.getAttribute("BILL_PRODUCT_CODE").getString());
                resultSetTemp.updateString("MFG_STATUS", ObjFeltOrderUpdDetails.getAttribute("MFG_STATUS").getString());
                resultSetTemp.updateString("INCHARGE_NAME", ObjFeltOrderUpdDetails.getAttribute("INCHARGE_NAME").getString());
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
                resultSetHistory.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetHistory.updateString("SR_NO", ObjFeltOrderUpdDetails.getAttribute("SR_NO").getString());
                resultSetHistory.updateString("LOCATION_ID", getAttribute("LOCATION_ID").getString());
                resultSetHistory.updateString("PIECE_NO", ObjFeltOrderUpdDetails.getAttribute("PIECE_NO").getString());
                resultSetHistory.updateString("PARTY_CODE", ObjFeltOrderUpdDetails.getAttribute("PARTY_CODE").getString());
                resultSetHistory.updateString("PARTY_NAME", ObjFeltOrderUpdDetails.getAttribute("PARTY_NAME").getString());
                resultSetHistory.updateString("ORDER_DATE", ObjFeltOrderUpdDetails.getAttribute("ORDER_DATE").getString());
                resultSetHistory.updateString("PRODUCT_CODE", ObjFeltOrderUpdDetails.getAttribute("PRODUCT_CODE").getString());
                resultSetHistory.updateString("GROUP", ObjFeltOrderUpdDetails.getAttribute("GROUP").getString());
                resultSetHistory.updateString("STYLE", ObjFeltOrderUpdDetails.getAttribute("STYLE").getString());
                resultSetHistory.updateString("LENGTH", ObjFeltOrderUpdDetails.getAttribute("LENGTH").getString());
                resultSetHistory.updateString("WIDTH", ObjFeltOrderUpdDetails.getAttribute("WIDTH").getString());
                resultSetHistory.updateString("GSM", ObjFeltOrderUpdDetails.getAttribute("GSM").getString());
                resultSetHistory.updateString("WEIGHT", ObjFeltOrderUpdDetails.getAttribute("WEIGHT").getString());
                resultSetHistory.updateString("SQMTR", ObjFeltOrderUpdDetails.getAttribute("SQMTR").getString());
                resultSetHistory.updateString("BILL_LENGTH", ObjFeltOrderUpdDetails.getAttribute("BILL_LENGTH").getString());
                resultSetHistory.updateString("BILL_WIDTH", ObjFeltOrderUpdDetails.getAttribute("BILL_WIDTH").getString());
                resultSetHistory.updateString("BILL_GSM", ObjFeltOrderUpdDetails.getAttribute("BILL_GSM").getString());
                resultSetHistory.updateString("BILL_WEIGHT", ObjFeltOrderUpdDetails.getAttribute("BILL_WEIGHT").getString());
                resultSetHistory.updateString("BILL_SQMTR", ObjFeltOrderUpdDetails.getAttribute("BILL_SQMTR").getString());
                resultSetHistory.updateString("BILL_PRODUCT_CODE", ObjFeltOrderUpdDetails.getAttribute("BILL_PRODUCT_CODE").getString());
                resultSetHistory.updateString("REMARK", ObjFeltOrderUpdDetails.getAttribute("REMARK").getString());
                resultSetHistory.updateString("MFG_STATUS", ObjFeltOrderUpdDetails.getAttribute("MFG_STATUS").getString());
                resultSetHistory.updateString("INCHARGE_NAME", ObjFeltOrderUpdDetails.getAttribute("INCHARGE_NAME").getString());
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
            ObjFeltProductionApprovalFlow.ModuleID = 770; //Felt Order Updation Module
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("DOC_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.FELT_LOCATION_ASSIGNMENT";
            ObjFeltProductionApprovalFlow.IsCreator = false;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "DOC_NO";

            if (getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFeltProductionApprovalFlow.To = (int) getAttribute("SEND_DOC_TO").getVal();
                ObjFeltProductionApprovalFlow.ExplicitSendTo = true;
            }

            //==== Handling Rejected Documents ==========//
            boolean IsRejected = getAttribute("REJECTED").getBool();

            if (IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.FELT_LOCATION_ASSIGNMENT SET REJECTED=0,CHANGED=1 WHERE DOC_NO ='" + getAttribute("DOC_NO").getString() + "'");

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
            if (ObjFeltProductionApprovalFlow.Status.equals("F")){
                for (int i = 1; i <= hmFeltOrderUpdDetails.size(); i++) {
                clsFeltLocationAssignmentDetails ObjFeltOrderUpdDetails = (clsFeltLocationAssignmentDetails) hmFeltOrderUpdDetails.get(Integer.toString(i));

                //updatefeltorder(getAttribute("DOC_NO").getString(), getAttribute("LOCATION_ID").getString());
                data.Execute("UPDATE PRODUCTION.FELT_LOCATION_ASSIGNMENT,PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_WH_LOCATION_ID=LOCATION_ID WHERE PR_PIECE_NO='" + ObjFeltOrderUpdDetails.getAttribute("PIECE_NO").getString() + "' AND PR_PIECE_NO=PIECE_NO AND APPROVED=1");
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
     * the document. After checking it deletes the record of selected production
     * date and document no.
     */
    public boolean CanDelete(String documentNo, String stringProductionDate, int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";

        try {
            tmpStmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_LOCATION_ASSIGNMENT WHERE  DOC_NO='" + documentNo + "' AND APPROVED=" + 1;
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=770 AND USER_ID=" + userID + " AND DOC_NO='" + documentNo + "' AND STATUS='W'";
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();

                if (rsTmp.getInt("COUNT") > 0) {
                    strSQL = "DELETE FROM PRODUCTION.FELT_LOCATION_ASSIGNMENT WHERE "
                            + " AND DOC_DATE= '" + EITLERPGLOBAL.formatDateDB(stringProductionDate) + "'"
                            + " AND DOC_NO='" + documentNo + "'";
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_LOCATION_ASSIGNMENT WHERE DOC_NO='" + orderupdDocumentNo + "' AND APPROVED=1";
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=770 AND USER_ID=" + userID + " AND DOC_NO='" + orderupdDocumentNo + "' AND STATUS='W'";
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
            String strSql = "SELECT * FROM PRODUCTION.FELT_LOCATION_ASSIGNMENT WHERE  " + stringFindQuery + "";
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
            setAttribute("DOC_DATE", resultSet.getDate("DOC_DATE"));
            setAttribute("DOC_NO", resultSet.getString("DOC_NO"));
            setAttribute("LOCATION_ID", resultSet.getString("LOCATION_ID"));

            hmFeltOrderUpdDetails.clear();

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_LOCATION_ASSIGNMENT WHERE DOC_NO = '" + resultSet.getString("DOC_NO") + "' AND DOC_DATE='" + resultSet.getDate("DOC_DATE") + "' ORDER BY DOC_NO,SR_NO*1");

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

                clsFeltLocationAssignmentDetails ObjFeltOrderUpdDetails = new clsFeltLocationAssignmentDetails();
                
                ObjFeltOrderUpdDetails.setAttribute("SR_NO", resultSetTemp.getString("SR_NO"));
                ObjFeltOrderUpdDetails.setAttribute("PIECE_NO", resultSetTemp.getString("PIECE_NO"));
                ObjFeltOrderUpdDetails.setAttribute("PARTY_CODE", resultSetTemp.getString("PARTY_CODE"));
                ObjFeltOrderUpdDetails.setAttribute("PARTY_NAME", resultSetTemp.getString("PARTY_NAME"));
                ObjFeltOrderUpdDetails.setAttribute("ORDER_DATE", resultSetTemp.getString("ORDER_DATE"));
                ObjFeltOrderUpdDetails.setAttribute("PRODUCT_CODE", resultSetTemp.getString("PRODUCT_CODE"));
                ObjFeltOrderUpdDetails.setAttribute("GROUP", resultSetTemp.getString("GROUP"));
                ObjFeltOrderUpdDetails.setAttribute("STYLE", resultSetTemp.getString("STYLE"));
                ObjFeltOrderUpdDetails.setAttribute("LENGTH", resultSetTemp.getString("LENGTH"));
                ObjFeltOrderUpdDetails.setAttribute("WIDTH", resultSetTemp.getString("WIDTH"));
                ObjFeltOrderUpdDetails.setAttribute("GSM", resultSetTemp.getString("GSM"));
                ObjFeltOrderUpdDetails.setAttribute("WEIGHT", resultSetTemp.getString("WEIGHT"));
                ObjFeltOrderUpdDetails.setAttribute("SQMTR", resultSetTemp.getString("SQMTR"));
                ObjFeltOrderUpdDetails.setAttribute("BILL_LENGTH", resultSetTemp.getString("BILL_LENGTH"));
                ObjFeltOrderUpdDetails.setAttribute("BILL_WIDTH", resultSetTemp.getString("BILL_WIDTH"));
                ObjFeltOrderUpdDetails.setAttribute("BILL_GSM", resultSetTemp.getString("BILL_GSM"));
                ObjFeltOrderUpdDetails.setAttribute("BILL_WEIGHT", resultSetTemp.getString("BILL_WEIGHT"));
                ObjFeltOrderUpdDetails.setAttribute("BILL_SQMTR", resultSetTemp.getString("BILL_SQMTR"));
                ObjFeltOrderUpdDetails.setAttribute("BILL_PRODUCT_CODE", resultSetTemp.getString("BILL_PRODUCT_CODE"));
                ObjFeltOrderUpdDetails.setAttribute("REMARK", resultSetTemp.getString("REMARK"));
                ObjFeltOrderUpdDetails.setAttribute("MFG_STATUS", resultSetTemp.getString("MFG_STATUS"));
                ObjFeltOrderUpdDetails.setAttribute("INCHARGE_NAME", resultSetTemp.getString("INCHARGE_NAME"));

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
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_LOCATION_ASSIGNMENT_H WHERE DOC_DATE='" + pProductionDate + "' AND DOC_NO='" + pDocNo + "' AND REVISION_NO='" + resultSet.getInt("REVISION_NO") + "' ORDER BY REVISION_NO");

            while (resultSetTemp.next()) {
                serialNoCounter++;
                setAttribute("DOC_NO", resultSetTemp.getString("DOC_NO"));
                setAttribute("UPDATED_BY", resultSetTemp.getInt("UPDATED_BY"));
                setAttribute("ENTRY_DATE", resultSetTemp.getString("ENTRY_DATE"));
                setAttribute("HIERARCHY_ID", resultSetTemp.getInt("HIERARCHY_ID"));

                clsFeltLocationAssignmentDetails ObjFeltOrderUpdDetails = new clsFeltLocationAssignmentDetails();

                ObjFeltOrderUpdDetails.setAttribute("PIECE_NO", resultSetTemp.getString("PIECE_NO"));
                ObjFeltOrderUpdDetails.setAttribute("PARTY_CODE", resultSetTemp.getString("PARTY_CODE"));
                ObjFeltOrderUpdDetails.setAttribute("PARTY_NAME", resultSetTemp.getString("PARTY_NAME"));
                ObjFeltOrderUpdDetails.setAttribute("ORDER_DATE", resultSetTemp.getString("ORDER_DATE"));
                ObjFeltOrderUpdDetails.setAttribute("PRODUCT_CODE", resultSetTemp.getString("PRODUCT_CODE"));
                ObjFeltOrderUpdDetails.setAttribute("GROUP", resultSetTemp.getString("GROUP"));
                ObjFeltOrderUpdDetails.setAttribute("STYLE", resultSetTemp.getString("STYLE"));
                ObjFeltOrderUpdDetails.setAttribute("LENGTH", resultSetTemp.getString("LENGTH"));
                ObjFeltOrderUpdDetails.setAttribute("WIDTH", resultSetTemp.getString("WIDTH"));
                ObjFeltOrderUpdDetails.setAttribute("GSM", resultSetTemp.getString("GSM"));
                ObjFeltOrderUpdDetails.setAttribute("WEIGHT", resultSetTemp.getString("WEIGHT"));
                ObjFeltOrderUpdDetails.setAttribute("SQMTR", resultSetTemp.getString("SQMTR"));
                ObjFeltOrderUpdDetails.setAttribute("BILL_LENGTH", resultSetTemp.getString("BILL_LENGTH"));
                ObjFeltOrderUpdDetails.setAttribute("BILL_WIDTH", resultSetTemp.getString("BILL_WIDTH"));
                ObjFeltOrderUpdDetails.setAttribute("BILL_GSM", resultSetTemp.getString("BILL_GSM"));
                ObjFeltOrderUpdDetails.setAttribute("BILL_WEIGHT", resultSetTemp.getString("BILL_WEIGHT"));
                ObjFeltOrderUpdDetails.setAttribute("BILL_SQMTR", resultSetTemp.getString("BILL_SQMTR"));
                ObjFeltOrderUpdDetails.setAttribute("BILL_PRODUCT_CODE", resultSetTemp.getString("BILL_PRODUCT_CODE"));
                ObjFeltOrderUpdDetails.setAttribute("REMARK", resultSetTemp.getString("REMARK"));
                ObjFeltOrderUpdDetails.setAttribute("MFG_STATUS", resultSetTemp.getString("MFG_STATUS"));
                ObjFeltOrderUpdDetails.setAttribute("INCHARGE_NAME", resultSetTemp.getString("INCHARGE_NAME"));

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
            rsTmp = stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_LOCATION_ASSIGNMENT_H WHERE DOC_NO='" + productionDocumentNo + "' GROUP BY REVISION_NO");

            while (rsTmp.next()) {
                clsFeltLocationAssignment ObjFeltOrderUpd = new clsFeltLocationAssignment();

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
            resultSet = statement.executeQuery("SELECT DISTINCT REVISION_NO FROM PRODUCTION.FELT_LOCATION_ASSIGNMENT_H WHERE DOC_DATE='" + pProductionDate + "' AND DOC_NO ='" + pDocNo + "'");
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
                strSQL = "SELECT DISTINCT A.DOC_NO,A.DOC_DATE,B.RECEIVED_DATE FROM PRODUCTION.FELT_LOCATION_ASSIGNMENT A,PRODUCTION.FELT_PROD_DOC_DATA B WHERE A.DOC_NO=B.DOC_NO AND STATUS='W' AND MODULE_ID=770 AND CANCELED=0  AND USER_ID=" + pUserID + "  ORDER BY RECEIVED_DATE,DOC_NO";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT A.DOC_NO,A.DOC_DATE,B.RECEIVED_DATE FROM PRODUCTION.FELT_LOCATION_ASSIGNMENT A,PRODUCTION.FELT_PROD_DOC_DATA B WHERE A.DOC_NO=B.DOC_NO AND STATUS='W' AND MODULE_ID=770 AND CANCELED=0  AND USER_ID=" + pUserID + "  ORDER BY DOC_DATE,DOC_NO";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT A.DOC_NO,A.DOC_DATE,B.RECEIVED_DATE FROM PRODUCTION.FELT_LOCATION_ASSIGNMENT A,PRODUCTION.FELT_PROD_DOC_DATA B WHERE A.DOC_NO=B.DOC_NO AND STATUS='W' AND MODULE_ID=770 AND CANCELED=0  AND USER_ID=" + pUserID + "  ORDER BY DOC_NO";
            }

            ResultSet rsTmp = data.getConn().createStatement().executeQuery(strSQL);

            Counter = 0;
            while (rsTmp.next()) {

                Counter = Counter + 1;
                clsFeltLocationAssignment ObjDoc = new clsFeltLocationAssignment();

                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO", rsTmp.getString("DOC_NO"));
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
    

   
    public boolean checkPieceNoInDB1(String pPieceNo, String pAmend_Reason, String pProdindstring, String pAgreedindstring) {
        int count = data.getIntValueFromDB("SELECT COUNT(FELT_PIECE_NO) FROM PRODUCTION.FELT_LOCATION_ASSIGNMENT WHERE FELT_PIECE_NO='" + pPieceNo + "' AND DOC_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND DOC_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'");
        if (count >= 1) {

            int counter = 0;

            try {
                Connection Conn = data.getConn();
                Statement stTmp = Conn.createStatement();
                ResultSet rsTmp = stTmp.executeQuery("SELECT DOC_DATE FROM PRODUCTION.FELT_LOCATION_ASSIGNMENT WHERE FELT_PIECE_NO='" + pPieceNo + "' AND DOC_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND DOC_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'");
                while (rsTmp.next()) {
                    //                 if(rsTmp.getString("DOC_DATE").equals(EITLERPGLOBAL.formatDateDB(pProdDate))) counter++;
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
        int count = data.getIntValueFromDB("SELECT COUNT(DOC_DATE) FROM PRODUCTION.FELT_LOCATION_ASSIGNMENT WHERE DOC_DATE='" + pProdDate + "'");
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
            ResultSet rsTmp = stTmp.executeQuery("SELECT LOCATION_ID FROM PRODUCTION.FELT_WH_LOCATION_MST");
           // ResultSet rsTmp = stTmp.executeQuery("SELECT CONCAT(LOCATION_ID,' - ',LOCATION_DESC) AS LOCATION_ID FROM PRODUCTION.FELT_WH_LOCATION_MST");
            while (rsTmp.next()) {
                ComboData aData = new ComboData();
                aData.Text = rsTmp.getString("LOCATION_ID");
                List.put(new Integer(counter++), aData);
            }
        } catch (SQLException e) {
            //      LastError = e.getMessage();
            e.printStackTrace();
        }
        return List;
    }

    
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
            strSQL = "SELECT LAST_USED_NO,PADDING_BY,NO_LENGTH,PREFIX_CHARS,SUFFIX_CHARS FROM D_COM_FIRSTFREE WHERE COMPANY_ID=2 AND MODULE_ID=770 AND FIRSTFREE_NO=221";
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
                    data.Execute("UPDATE D_COM_FIRSTFREE SET LAST_USED_NO='" + strNewNo.trim() + "',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=2 AND MODULE_ID=770 AND FIRSTFREE_NO=221");
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
