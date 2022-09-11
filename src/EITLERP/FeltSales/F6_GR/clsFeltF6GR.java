/*
 * clsFeltF6GR.java
 *
 * Created on March 12, 2013, 3:10 PM
 */
package EITLERP.FeltSales.F6_GR;

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
public class clsFeltF6GR {

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
    public clsFeltF6GR() {
        LastError = "";
        props = new HashMap();
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("FROM_DATE",new Variant(""));
        props.put("TO_DATE",new Variant(""));
        
        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("PREFIX", new Variant(""));
        props.put("RECEIVED_DATE", new Variant(""));
        // -- Approval Specific Fields --
        props.put("HIERARCHY_ID", new Variant(0));
        props.put("FROM", new Variant(0));
        props.put("TO", new Variant(0));
        props.put("REMARKS", new Variant(""));
        props.put("APPROVAL_STATUS", new Variant(""));
        
        props.put("UPDATED_BY",new Variant(0));
        props.put("ENTRY_DATE",new Variant(""));
        props.put("APPROVER_REMARKS",new Variant(""));
        props.put("CANCELED",new Variant(0));
        props.put("APPROVED",new Variant(0));
        props.put("REJECTED",new Variant(0));

        hmFeltOrderUpdDetails = new HashMap();
    }

    public boolean LoadData() {
        Ready = false;
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery("SELECT DISTINCT DOC_NO,DOC_DATE,FROM_DATE,TO_DATE FROM PRODUCTION.FELT_F6GR_ELIGIBLE_INV_LIST WHERE DOC_DATE >= '" + EITLERPGLOBAL.FinFromDateDB + "' AND DOC_DATE<='" + EITLERPGLOBAL.FinToDateDB + "' ORDER BY DOC_DATE,DOC_NO ");
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
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_F6GR_ELIGIBLE_INV_LIST WHERE DOC_NO=''");

            // Felt order Updation data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_F6GR_ELIGIBLE_INV_LIST_H WHERE DOC_NO=''");

            // Felt order Updation Last Free No, 
            setAttribute("DOC_NO",EITLERP.clsFirstFree.getNextFreeNo(2, 629, 297, true));
            
            //Now Insert records into Felt_Order_Amendment & History tables
            for (int i = 1; i <= hmFeltOrderUpdDetails.size(); i++) {
                clsFeltF6GRDetails ObjFeltOrderUpdDetails = (clsFeltF6GRDetails) hmFeltOrderUpdDetails.get(Integer.toString(i));

                //Insert records into Felt order Amendment table
                resultSetTemp.moveToInsertRow();

                resultSetTemp.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetTemp.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                resultSetTemp.updateString("FROM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("FROM_DATE").getString()));
                resultSetTemp.updateString("TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("TO_DATE").getString()));
                
                resultSetTemp.updateString("INVOICE_NO", ObjFeltOrderUpdDetails.getAttribute("INVOICE_NO").getString());
                resultSetTemp.updateString("INVOICE_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltOrderUpdDetails.getAttribute("INVOICE_DATE").getString()));
                resultSetTemp.updateString("PARTY_CODE", ObjFeltOrderUpdDetails.getAttribute("PARTY_CODE").getString());
                resultSetTemp.updateString("PARTY_NAME", ObjFeltOrderUpdDetails.getAttribute("PARTY_NAME").getString());
                resultSetTemp.updateString("INVOICE_PIECE_NO", ObjFeltOrderUpdDetails.getAttribute("INVOICE_PIECE_NO").getString());
                resultSetTemp.updateString("INVOICE_AMOUNT", ObjFeltOrderUpdDetails.getAttribute("INVOICE_AMOUNT").getString());
                
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
                
                resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetHistory.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                resultSetHistory.updateString("FROM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("FROM_DATE").getString()));
                resultSetHistory.updateString("TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("TO_DATE").getString()));
                
                resultSetHistory.updateString("INVOICE_NO", ObjFeltOrderUpdDetails.getAttribute("INVOICE_NO").getString());
                resultSetHistory.updateString("INVOICE_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltOrderUpdDetails.getAttribute("INVOICE_DATE").getString()));
                resultSetHistory.updateString("PARTY_CODE", ObjFeltOrderUpdDetails.getAttribute("PARTY_CODE").getString());
                resultSetHistory.updateString("PARTY_NAME", ObjFeltOrderUpdDetails.getAttribute("PARTY_NAME").getString());
                resultSetHistory.updateString("INVOICE_PIECE_NO", ObjFeltOrderUpdDetails.getAttribute("INVOICE_PIECE_NO").getString());
                resultSetHistory.updateString("INVOICE_AMOUNT", ObjFeltOrderUpdDetails.getAttribute("INVOICE_AMOUNT").getString());
                
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
            ObjFeltProductionApprovalFlow.ModuleID = 629; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("DOC_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.FELT_F6GR_ELIGIBLE_INV_LIST";
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
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_F6GR_ELIGIBLE_INV_LIST WHERE DOC_NO=''");

            // Production data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //Get the Maximum Revision No in History Table.
            resultSetHistory = statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_F6GR_ELIGIBLE_INV_LIST_H WHERE DOC_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()) + "' AND DOC_NO='" + getAttribute("DOC_NO").getString() + "'");
            resultSetHistory.first();
            if (resultSetHistory.getRow() > 0) {
                revisionNo = resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from production data table before insert
            statementHistory.execute("DELETE FROM PRODUCTION.FELT_F6GR_ELIGIBLE_INV_LIST WHERE DOC_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()) + "' AND DOC_NO='" + getAttribute("DOC_NO").getString() + "'");

            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_F6GR_ELIGIBLE_INV_LIST_H WHERE DOC_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()) + "' AND DOC_NO='" + getAttribute("DOC_NO").getString() + "'");

            //Now Update records into Felt_Order_Amendment tables
            for (int i = 1; i <= hmFeltOrderUpdDetails.size(); i++) {
                clsFeltF6GRDetails ObjFeltOrderUpdDetails = (clsFeltF6GRDetails) hmFeltOrderUpdDetails.get(Integer.toString(i));

                resultSetTemp.moveToInsertRow();
                
                resultSetTemp.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetTemp.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                resultSetTemp.updateString("FROM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("FROM_DATE").getString()));
                resultSetTemp.updateString("TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("TO_DATE").getString()));
                
                resultSetTemp.updateString("INVOICE_NO", ObjFeltOrderUpdDetails.getAttribute("INVOICE_NO").getString());
                resultSetTemp.updateString("INVOICE_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltOrderUpdDetails.getAttribute("INVOICE_DATE").getString()));
                resultSetTemp.updateString("PARTY_CODE", ObjFeltOrderUpdDetails.getAttribute("PARTY_CODE").getString());
                resultSetTemp.updateString("PARTY_NAME", ObjFeltOrderUpdDetails.getAttribute("PARTY_NAME").getString());
                resultSetTemp.updateString("INVOICE_PIECE_NO", ObjFeltOrderUpdDetails.getAttribute("INVOICE_PIECE_NO").getString());
                resultSetTemp.updateString("INVOICE_AMOUNT", ObjFeltOrderUpdDetails.getAttribute("INVOICE_AMOUNT").getString());
                
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
                
                resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetHistory.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                resultSetHistory.updateString("FROM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("FROM_DATE").getString()));
                resultSetHistory.updateString("TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("TO_DATE").getString()));
                
                resultSetHistory.updateString("INVOICE_NO", ObjFeltOrderUpdDetails.getAttribute("INVOICE_NO").getString());
                resultSetHistory.updateString("INVOICE_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltOrderUpdDetails.getAttribute("INVOICE_DATE").getString()));
                resultSetHistory.updateString("PARTY_CODE", ObjFeltOrderUpdDetails.getAttribute("PARTY_CODE").getString());
                resultSetHistory.updateString("PARTY_NAME", ObjFeltOrderUpdDetails.getAttribute("PARTY_NAME").getString());
                resultSetHistory.updateString("INVOICE_PIECE_NO", ObjFeltOrderUpdDetails.getAttribute("INVOICE_PIECE_NO").getString());
                resultSetHistory.updateString("INVOICE_AMOUNT", ObjFeltOrderUpdDetails.getAttribute("INVOICE_AMOUNT").getString());
                
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
            ObjFeltProductionApprovalFlow.ModuleID = 629; //Felt Order Updation Module
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("DOC_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.FELT_F6GR_ELIGIBLE_INV_LIST";
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
                data.Execute("UPDATE PRODUCTION.FELT_F6GR_ELIGIBLE_INV_LIST SET REJECTED=0,CHANGED=1 WHERE DOC_NO ='" + getAttribute("DOC_NO").getString() + "'");

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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_F6GR_ELIGIBLE_INV_LIST WHERE DOC_NO='" + documentNo + "' AND APPROVED=" + 1;
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=629 AND USER_ID=" + userID + " AND DOC_NO='" + documentNo + "' AND STATUS='W'";
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();

                if (rsTmp.getInt("COUNT") > 0) {
                    strSQL = "DELETE FROM PRODUCTION.FELT_F6GR_ELIGIBLE_INV_LIST WHERE DOC_DATE= '" + EITLERPGLOBAL.formatDateDB(stringProductionDate) + "' AND DOC_NO='" + documentNo + "'";
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_F6GR_ELIGIBLE_INV_LIST WHERE DOC_NO='" + orderupdDocumentNo + "' AND APPROVED=1";
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=629 AND USER_ID=" + userID + " AND DOC_NO='" + orderupdDocumentNo + "' AND STATUS='W'";
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
            String strSql = "SELECT DISTINCT DOC_NO,DOC_DATE,FROM_DATE,TO_DATE FROM PRODUCTION.FELT_F6GR_ELIGIBLE_INV_LIST WHERE " + stringFindQuery + "";
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
            setAttribute("DOC_NO", resultSet.getString("DOC_NO"));
            setAttribute("DOC_DATE", resultSet.getDate("DOC_DATE"));
            setAttribute("FROM_DATE", resultSet.getDate("FROM_DATE"));
            setAttribute("TO_DATE", resultSet.getDate("TO_DATE"));

            hmFeltOrderUpdDetails.clear();

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_F6GR_ELIGIBLE_INV_LIST WHERE DOC_NO = '" + resultSet.getString("DOC_NO") + "' AND DOC_DATE='" + resultSet.getDate("DOC_DATE") + "' ORDER BY DOC_NO,DOC_DATE");

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

                clsFeltF6GRDetails ObjFeltOrderUpdDetails = new clsFeltF6GRDetails();

                ObjFeltOrderUpdDetails.setAttribute("INVOICE_NO", resultSetTemp.getString("INVOICE_NO"));
                ObjFeltOrderUpdDetails.setAttribute("INVOICE_DATE", resultSetTemp.getString("INVOICE_DATE"));
                ObjFeltOrderUpdDetails.setAttribute("PARTY_CODE", resultSetTemp.getString("PARTY_CODE"));
                ObjFeltOrderUpdDetails.setAttribute("PARTY_NAME", resultSetTemp.getString("PARTY_NAME"));
                ObjFeltOrderUpdDetails.setAttribute("INVOICE_PIECE_NO", resultSetTemp.getString("INVOICE_PIECE_NO"));
                ObjFeltOrderUpdDetails.setAttribute("INVOICE_AMOUNT", resultSetTemp.getString("INVOICE_AMOUNT"));
                
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
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_F6GR_ELIGIBLE_INV_LIST_H WHERE DOC_DATE='" + pProductionDate + "' AND DOC_NO='" + pDocNo + "' AND REVISION_NO='" + resultSet.getInt("REVISION_NO") + "' ORDER BY REVISION_NO");

            while (resultSetTemp.next()) {
                serialNoCounter++;
                setAttribute("DOC_NO", resultSetTemp.getString("DOC_NO"));
                setAttribute("UPDATED_BY", resultSetTemp.getInt("UPDATED_BY"));
                setAttribute("ENTRY_DATE", resultSetTemp.getString("ENTRY_DATE"));
                setAttribute("HIERARCHY_ID", resultSetTemp.getInt("HIERARCHY_ID"));

                clsFeltF6GRDetails ObjFeltOrderUpdDetails = new clsFeltF6GRDetails();

                ObjFeltOrderUpdDetails.setAttribute("INVOICE_NO", resultSetTemp.getString("INVOICE_NO"));
                ObjFeltOrderUpdDetails.setAttribute("INVOICE_DATE", resultSetTemp.getString("INVOICE_DATE"));
                ObjFeltOrderUpdDetails.setAttribute("PARTY_CODE", resultSetTemp.getString("PARTY_CODE"));
                ObjFeltOrderUpdDetails.setAttribute("PARTY_NAME", resultSetTemp.getString("PARTY_NAME"));
                ObjFeltOrderUpdDetails.setAttribute("INVOICE_PIECE_NO", resultSetTemp.getString("INVOICE_PIECE_NO"));
                ObjFeltOrderUpdDetails.setAttribute("INVOICE_AMOUNT", resultSetTemp.getString("INVOICE_AMOUNT"));
                
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
            rsTmp = stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_F6GR_ELIGIBLE_INV_LIST_H WHERE DOC_NO='" + productionDocumentNo + "' GROUP BY REVISION_NO");

            while (rsTmp.next()) {
                clsFeltF6GR ObjFeltOrderUpd = new clsFeltF6GR();

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
            resultSet = statement.executeQuery("SELECT DISTINCT REVISION_NO FROM PRODUCTION.FELT_F6GR_ELIGIBLE_INV_LIST_H WHERE DOC_DATE='" + pProductionDate + "' AND DOC_NO ='" + pDocNo + "'");
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
                strSQL = "SELECT DISTINCT F6.DOC_NO,F6.DOC_DATE,DD.RECEIVED_DATE FROM PRODUCTION.FELT_F6GR_ELIGIBLE_INV_LIST F6, PRODUCTION.FELT_PROD_DOC_DATA DD WHERE F6.DOC_NO=DD.DOC_NO AND STATUS='W' AND MODULE_ID=629 AND F6.CANCELED=0  AND USER_ID=" + pUserID + "  ORDER BY RECEIVED_DATE,F6.DOC_NO";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT F6.DOC_NO,F6.DOC_DATE,DD.RECEIVED_DATE FROM PRODUCTION.FELT_F6GR_ELIGIBLE_INV_LIST F6, PRODUCTION.FELT_PROD_DOC_DATA DD WHERE F6.DOC_NO=DD.DOC_NO AND STATUS='W' AND MODULE_ID=629 AND F6.CANCELED=0  AND USER_ID=" + pUserID + "  ORDER BY F6.DOC_DATE,F6.DOC_NO";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT F6.DOC_NO,F6.DOC_DATE,DD.RECEIVED_DATE FROM PRODUCTION.FELT_F6GR_ELIGIBLE_INV_LIST F6, PRODUCTION.FELT_PROD_DOC_DATA DD WHERE F6.DOC_NO=DD.DOC_NO AND STATUS='W' AND MODULE_ID=629 AND F6.CANCELED=0  AND USER_ID=" + pUserID + "  ORDER BY F6.DOC_NO";
            }

            ResultSet rsTmp = data.getConn().createStatement().executeQuery(strSQL);

            Counter = 0;
            while (rsTmp.next()) {

                Counter = Counter + 1;
                clsFeltF6GR ObjDoc = new clsFeltF6GR();

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

    public static String getinvoiceno(String pcode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String partyname="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT INVOICE_NO FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PIECE_NO = '"+pcode+"' AND APPROVED=1 AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                partyname=rsTmp.getString("INVOICE_NO");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return partyname;
        }
        catch(Exception e) {
            return "";
        }
    }
       
    public static String getinvoicedate(String pcode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String partyname="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT SUBSTRING(INVOICE_DATE,1,10) AS INVOICE_DATE FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PIECE_NO = '"+pcode+"' AND APPROVED=1 AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                partyname=rsTmp.getString("INVOICE_DATE");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return partyname;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getinvoiceamt(String pcode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String partyname="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT INVOICE_AMT FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PIECE_NO = '"+pcode+"' AND APPROVED=1 AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                partyname=rsTmp.getString("INVOICE_AMT");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return partyname;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getpartycode(String pcode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String partyname="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PARTY_CODE FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PIECE_NO = '"+pcode+"' AND APPROVED=1 AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                partyname=rsTmp.getString("PARTY_CODE");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return partyname;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getpartyname(String pcode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String partyname="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PARTY_NAME FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE PIECE_NO = '"+pcode+"' AND APPROVED=1 AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                partyname=rsTmp.getString("PARTY_NAME");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return partyname;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public boolean checkPieceNoInDB1(String pPieceNo, String pAmend_Reason, String pProdindstring, String pAgreedindstring) {
        int count = data.getIntValueFromDB("SELECT COUNT(INVOICE_PIECE_NO) FROM PRODUCTION.FELT_F6GR_ELIGIBLE_INV_LIST WHERE INVOICE_PIECE_NO='" + pPieceNo + "' ");
        if (count >= 1) {

            int counter = 0;

            try {
                Connection Conn = data.getConn();
                Statement stTmp = Conn.createStatement();
                ResultSet rsTmp = stTmp.executeQuery("SELECT DOC_NO FROM PRODUCTION.FELT_F6GR_ELIGIBLE_INV_LIST WHERE INVOICE_PIECE_NO='" + pPieceNo + "' ");
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
            ResultSet rsTmp = stTmp.executeQuery("SELECT USER_ID,USER_NAME FROM PRODUCTION.FELT_USER WHERE USER_MODULE='" + pModule + "' AND USER_CATEG='" + category + "' ORDER BY USER_NAME");
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

    public static boolean CanCancel(int pCompanyID, String pDocNo) {
        ResultSet rsTmp = null;
        boolean canCancel = false;

        try {
            rsTmp = data.getResult("SELECT DOC_NO,APPROVED FROM PRODUCTION.FELT_F6GR_ELIGIBLE_INV_LIST WHERE DOC_NO='" + pDocNo + "' AND CANCELED=0");
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

                rsTmp = data.getResult("SELECT APPROVED FROM PRODUCTION.FELT_F6GR_ELIGIBLE_INV_LIST WHERE DOC_NO='" + pDocNo + "' ");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    ApprovedDoc = rsTmp.getBoolean("APPROVED");
                }

                if (ApprovedDoc) {

                } else {
                    int ModuleID = 629;
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='" + pDocNo + "' AND MODULE_ID=" + (ModuleID));
                }

                //Now Update the header with cancel falg to true
                data.Execute("UPDATE PRODUCTION.FELT_F6GR_ELIGIBLE_INV_LIST SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='" + pDocNo + "' ");

                Cancelled = true;
            }

            rsTmp.close();
            rsIndent.close();
        } catch (Exception e) {

        }

        return Cancelled;
    }
}
