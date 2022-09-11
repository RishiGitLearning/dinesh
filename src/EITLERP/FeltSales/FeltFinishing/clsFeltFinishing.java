/*
 * clsFeltFinishing.java
 *
 * Created on August 22, 2013, 11:20 AM
 */
package EITLERP.FeltSales.FeltFinishing;

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
import EITLERP.FeltSales.common.FeltInvCalc;
import EITLERP.FeltSales.common.clsOrderValueCalc;
import EITLERP.Production.clsFeltProductionApprovalFlow;

/**
 *
 * @author Vivek Kumar
 */
public class clsFeltFinishing {

    public String LastError = "";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public HashMap props;
    public boolean Ready = false;
    //Felt Production Finishing Details Collection
    public HashMap hmFeltFinishingDetails;

    //History Related properties
    public boolean HistoryView = false;
    private String historyProductionDate = "";
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

    /**
     * Creates new DataFeltProductionFinishing
     */
    public clsFeltFinishing() {
        LastError = "";
        props = new HashMap();
        props.put("PRODUCTION_DATE", new Variant(""));
        props.put("PRODUCTION_DOCUMENT_NO", new Variant(""));
        props.put("HEADER_REMARK", new Variant(""));
        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        // -- Approval Specific Fields --
        props.put("HIERARCHY_ID", new Variant(0));
        props.put("FROM", new Variant(0));
        props.put("TO", new Variant(0));
        props.put("REMARKS", new Variant(""));
        props.put("APPROVAL_STATUS", new Variant(""));

        props.put("FINAL_APPROVAL_DATE", new Variant(""));

        props.put("APPROVED", new Variant(false));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED", new Variant(false));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("HIERARCHY_ID", new Variant(0));
        props.put("REVISION_NO", new Variant(0));
        props.put("CANCELLED", new Variant(false));

        hmFeltFinishingDetails = new HashMap();
    }

    public boolean LoadData() {
        Ready = false;
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT = 'FELT FINISHING' GROUP BY PROD_DOC_NO ORDER BY PROD_DOC_NO");
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
//            if(HistoryView) setHistoryData(historyProductionDate, historyDocumentNo);
//            else setData();
            setData();
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
//            if(HistoryView) setHistoryData(historyProductionDate, historyDocumentNo);
//            else setData();
            setData();
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
//            if(HistoryView) setHistoryData(historyProductionDate, historyDocumentNo);
//            else setData();
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
//            if(HistoryView) setHistoryData(historyProductionDate, historyDocumentNo);
//            else setData();
            setData();
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
            // Production data connection
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_PROD_DATA WHERE PROD_DOC_NO='1'");

            // Production data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_PROD_DATA_H WHERE PROD_DOC_NO='1'");

            setAttribute("PRODUCTION_DOCUMENT_NO", getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 603, 200, true));

            //Now Insert records into production tables
            for (int i = 1; i <= hmFeltFinishingDetails.size(); i++) {
                clsFeltFinishingDetails ObjFeltFinishingDetails = (clsFeltFinishingDetails) hmFeltFinishingDetails.get(Integer.toString(i));

                //Insert records into production data table
                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateString("PROD_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()));
                resultSetTemp.updateString("PROD_DOC_NO", getAttribute("PRODUCTION_DOCUMENT_NO").getString());
                resultSetTemp.updateString("HEADER_REMARK", getAttribute("HEADER_REMARK").getString());
                resultSetTemp.updateString("PROD_FORM_NO", getAttribute("PRODUCTION_FORM_NO").getString());
                resultSetTemp.updateString("PROD_DEPT", "FELT FINISHING");
                resultSetTemp.updateInt("SR_NO", i);
                resultSetTemp.updateString("PROD_PIECE_NO", ObjFeltFinishingDetails.getAttribute("PRODUCTION_PIECE_NO").getString());
                resultSetTemp.updateString("PROD_PARTY_CODE", ObjFeltFinishingDetails.getAttribute("PRODUCTION_PARTY_CODE").getString());
                resultSetTemp.updateFloat("LENGTH", (float) EITLERPGLOBAL.round(ObjFeltFinishingDetails.getAttribute("LENGTH").getVal(), 2));
                resultSetTemp.updateFloat("WIDTH", (float) EITLERPGLOBAL.round(ObjFeltFinishingDetails.getAttribute("WIDTH").getVal(), 2));
                resultSetTemp.updateFloat("WEIGHT", (float) EITLERPGLOBAL.round(ObjFeltFinishingDetails.getAttribute("WEIGHT").getVal(), 2));
                resultSetTemp.updateFloat("TAG_WEIGHT", (float) EITLERPGLOBAL.round(ObjFeltFinishingDetails.getAttribute("TAG_WEIGHT").getVal(), 2));
                resultSetTemp.updateString("STYLE_CODE", ObjFeltFinishingDetails.getAttribute("STYLE_CODE").getString());
                resultSetTemp.updateString("BILL_STYLE_CODE", ObjFeltFinishingDetails.getAttribute("BILL_STYLE_CODE").getString());
                resultSetTemp.updateString("REMARKS", ObjFeltFinishingDetails.getAttribute("REMARKS").getString());
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

                //Insert records into production data history table
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO", 1);
                resultSetHistory.updateString("PROD_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()));
                resultSetHistory.updateString("PROD_DOC_NO", getAttribute("PRODUCTION_DOCUMENT_NO").getString());
                resultSetHistory.updateString("HEADER_REMARK", getAttribute("HEADER_REMARK").getString());
                resultSetHistory.updateString("PROD_FORM_NO", getAttribute("PRODUCTION_FORM_NO").getString());
                resultSetHistory.updateString("PROD_DEPT", "FELT FINISHING");
                resultSetHistory.updateInt("SR_NO", i);
                resultSetHistory.updateString("PROD_PIECE_NO", ObjFeltFinishingDetails.getAttribute("PRODUCTION_PIECE_NO").getString());
                resultSetHistory.updateString("PROD_PARTY_CODE", ObjFeltFinishingDetails.getAttribute("PRODUCTION_PARTY_CODE").getString());
                resultSetHistory.updateFloat("LENGTH", (float) EITLERPGLOBAL.round(ObjFeltFinishingDetails.getAttribute("LENGTH").getVal(), 2));
                resultSetHistory.updateFloat("WIDTH", (float) EITLERPGLOBAL.round(ObjFeltFinishingDetails.getAttribute("WIDTH").getVal(), 2));
                resultSetHistory.updateFloat("WEIGHT", (float) EITLERPGLOBAL.round(ObjFeltFinishingDetails.getAttribute("WEIGHT").getVal(), 2));
                resultSetHistory.updateFloat("TAG_WEIGHT", (float) EITLERPGLOBAL.round(ObjFeltFinishingDetails.getAttribute("TAG_WEIGHT").getVal(), 2));
                resultSetHistory.updateString("STYLE_CODE", ObjFeltFinishingDetails.getAttribute("STYLE_CODE").getString());
                resultSetHistory.updateString("BILL_STYLE_CODE", ObjFeltFinishingDetails.getAttribute("BILL_STYLE_CODE").getString());
                resultSetHistory.updateString("REMARKS", ObjFeltFinishingDetails.getAttribute("REMARKS").getString());
                resultSetHistory.updateInt("UPDATED_BY", (int) getAttribute("UPDATED_BY").getVal());
                resultSetHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
                resultSetHistory.updateString("APPROVER_REMARKS", getAttribute("FROM_REMARKS").getString());
                resultSetHistory.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                resultSetHistory.updateInt("CHANGED", 1);
                resultSetHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

                ResultSet rsTmp = data.getResult("SELECT USER()");
                rsTmp.first();
                String str = rsTmp.getString(1);
                String str_split[] = str.split("@");

                resultSetHistory.updateString("FROM_IP", "" + str_split[1]);

                resultSetHistory.insertRow();
            }

            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = 603; //Felt Finishing Entry
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("PRODUCTION_DOCUMENT_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.FELT_PROD_DATA";
            ObjFeltProductionApprovalFlow.IsCreator = true;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "PROD_DOC_NO";

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

            // Insert Finishing Date in Piece Register Table To confirm that Finishing has completed
            if (ObjFeltProductionApprovalFlow.Status.equals("H") || ObjFeltProductionApprovalFlow.Status.equals("A")) {
                updatePieceRegisterS(getAttribute("PRODUCTION_DOCUMENT_NO").getString());
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
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_PROD_DATA WHERE PROD_DOC_NO='1'");

            // Production data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //Get the Maximum Revision No in History Table.
            resultSetHistory = statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_PROD_DATA_H WHERE PROD_DEPT = 'FELT FINISHING' AND PROD_DOC_NO='" + getAttribute("PRODUCTION_DOCUMENT_NO").getString() + "'");
            resultSetHistory.first();
            if (resultSetHistory.getRow() > 0) {
                revisionNo = resultSetHistory.getInt(1);
                revisionNo++;
            }

//            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_PROD_DATA_H WHERE PROD_DEPT = 'FELT FINISHING' AND PROD_DOC_NO='"+getAttribute("PRODUCTION_DOCUMENT_NO").getString()+"'");
//            RevNo++;
//            
            //delete records from production data table before insert
            statementHistory.execute("DELETE FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT = 'FELT FINISHING' AND PROD_DOC_NO='" + getAttribute("PRODUCTION_DOCUMENT_NO").getString() + "'");

            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_PROD_DATA_H WHERE PROD_DOC_NO='1'");

            //Now Update records into production tables
            for (int i = 1; i <= hmFeltFinishingDetails.size(); i++) {
                clsFeltFinishingDetails ObjFeltFinishingDetails = (clsFeltFinishingDetails) hmFeltFinishingDetails.get(Integer.toString(i));

                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateString("PROD_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()));
                resultSetTemp.updateString("PROD_DOC_NO", getAttribute("PRODUCTION_DOCUMENT_NO").getString());
                resultSetTemp.updateString("HEADER_REMARK", getAttribute("HEADER_REMARK").getString());
                resultSetTemp.updateString("PROD_FORM_NO", getAttribute("PRODUCTION_FORM_NO").getString());
                resultSetTemp.updateString("PROD_DEPT", "FELT FINISHING");
                resultSetTemp.updateInt("SR_NO", i);
                resultSetTemp.updateString("PROD_PIECE_NO", ObjFeltFinishingDetails.getAttribute("PRODUCTION_PIECE_NO").getString());
                resultSetTemp.updateString("PROD_PARTY_CODE", ObjFeltFinishingDetails.getAttribute("PRODUCTION_PARTY_CODE").getString());
                resultSetTemp.updateFloat("LENGTH", (float) EITLERPGLOBAL.round(ObjFeltFinishingDetails.getAttribute("LENGTH").getVal(), 2));
                resultSetTemp.updateFloat("WIDTH", (float) EITLERPGLOBAL.round(ObjFeltFinishingDetails.getAttribute("WIDTH").getVal(), 2));
                resultSetTemp.updateFloat("WEIGHT", (float) EITLERPGLOBAL.round(ObjFeltFinishingDetails.getAttribute("WEIGHT").getVal(), 2));
                resultSetTemp.updateFloat("TAG_WEIGHT", (float) EITLERPGLOBAL.round(ObjFeltFinishingDetails.getAttribute("TAG_WEIGHT").getVal(), 2));
                resultSetTemp.updateString("STYLE_CODE", ObjFeltFinishingDetails.getAttribute("STYLE_CODE").getString());
                resultSetTemp.updateString("BILL_STYLE_CODE", ObjFeltFinishingDetails.getAttribute("BILL_STYLE_CODE").getString());
                resultSetTemp.updateString("REMARKS", ObjFeltFinishingDetails.getAttribute("REMARKS").getString());
                resultSetTemp.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                resultSetTemp.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
                resultSetTemp.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                resultSetTemp.updateString("MODIFIED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetTemp.updateBoolean("APPROVED", false);
                resultSetTemp.updateString("APPROVED_DATE", "0000-00-00");
                resultSetTemp.updateBoolean("REJECTED", false);
                resultSetTemp.updateString("REJECTED_DATE", "0000-00-00");
                resultSetTemp.updateInt("CANCELED", 0);
                resultSetTemp.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                resultSetTemp.updateInt("CHANGED", 1);
                resultSetTemp.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                resultSetTemp.insertRow();

                //Insert records into production data history table
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO", revisionNo);
                resultSetHistory.updateString("PROD_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()));
                resultSetHistory.updateString("PROD_DOC_NO", getAttribute("PRODUCTION_DOCUMENT_NO").getString());
                resultSetHistory.updateString("HEADER_REMARK", getAttribute("HEADER_REMARK").getString());
                resultSetHistory.updateString("PROD_FORM_NO", getAttribute("PRODUCTION_FORM_NO").getString());
                resultSetHistory.updateString("PROD_DEPT", "FELT FINISHING");
                resultSetHistory.updateInt("SR_NO", i);
                resultSetHistory.updateString("PROD_PIECE_NO", ObjFeltFinishingDetails.getAttribute("PRODUCTION_PIECE_NO").getString());
                resultSetHistory.updateString("PROD_PARTY_CODE", ObjFeltFinishingDetails.getAttribute("PRODUCTION_PARTY_CODE").getString());
                resultSetHistory.updateFloat("LENGTH", (float) EITLERPGLOBAL.round(ObjFeltFinishingDetails.getAttribute("LENGTH").getVal(), 2));
                resultSetHistory.updateFloat("WIDTH", (float) EITLERPGLOBAL.round(ObjFeltFinishingDetails.getAttribute("WIDTH").getVal(), 2));
                resultSetHistory.updateFloat("WEIGHT", (float) EITLERPGLOBAL.round(ObjFeltFinishingDetails.getAttribute("WEIGHT").getVal(), 2));
                resultSetHistory.updateFloat("TAG_WEIGHT", (float) EITLERPGLOBAL.round(ObjFeltFinishingDetails.getAttribute("TAG_WEIGHT").getVal(), 2));
                resultSetHistory.updateString("STYLE_CODE", ObjFeltFinishingDetails.getAttribute("STYLE_CODE").getString());
                resultSetHistory.updateString("BILL_STYLE_CODE", ObjFeltFinishingDetails.getAttribute("BILL_STYLE_CODE").getString());
                resultSetHistory.updateString("REMARKS", ObjFeltFinishingDetails.getAttribute("REMARKS").getString());
                resultSetHistory.updateInt("UPDATED_BY", (int) getAttribute("UPDATED_BY").getVal());
                resultSetHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
                resultSetHistory.updateString("APPROVER_REMARKS", getAttribute("FROM_REMARKS").getString());
                resultSetHistory.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                resultSetHistory.updateInt("CHANGED", 1);
                resultSetHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

                ResultSet rsTmp = data.getResult("SELECT USER()");
                rsTmp.first();
                String str = rsTmp.getString(1);
                String str_split[] = str.split("@");

                resultSetHistory.updateString("FROM_IP", "" + str_split[1]);

                resultSetHistory.insertRow();
            }

            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = 603; //Felt Finishing Entry
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("PRODUCTION_DOCUMENT_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.FELT_PROD_DATA";
            ObjFeltProductionApprovalFlow.IsCreator = false;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "PROD_DOC_NO";

            if (getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFeltProductionApprovalFlow.To = (int) getAttribute("SEND_DOC_TO").getVal();
                ObjFeltProductionApprovalFlow.ExplicitSendTo = true;
            }

            //==== Handling Rejected Documents ==========//
            boolean IsRejected = getAttribute("REJECTED").getBool();

            if (IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.FELT_PROD_DATA SET REJECTED=0,CHANGED=1 WHERE PROD_DEPT='FELT FINISHING' AND PROD_DOC_NO='" + getAttribute("PRODUCTION_DOCUMENT_NO").getString() + "'");
                //Remove Old Records from FELT DOCUMET APPROVAL TABLE
                //data.Execute("DELETE FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=603 AND DOC_NO='"+getAttribute("PRODUCTION_DOCUMENT_NO").getString()+"'");

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

            if (ObjFeltProductionApprovalFlow.Status.equals("H") || ObjFeltProductionApprovalFlow.Status.equals("A")) {
                updatePieceRegisterS(getAttribute("PRODUCTION_DOCUMENT_NO").getString());
            }
            //--------- Approval Flow Update complete -----------

            // Update Finishing Date in Order Master Table To confirm that Finishing has completed
            if (ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {
                ObjFeltProductionApprovalFlow.finalApproved = false;
                updatePieceRegister(getAttribute("PRODUCTION_DOCUMENT_NO").getString());
                try {
                    data.Execute("UPDATE PRODUCTION.FELT_PROD_DATA SET FINAL_APPROVAL_DATE='" + EITLERPGLOBAL.getCurrentDateDB() + "' WHERE PROD_DOC_NO = '" + getAttribute("PRODUCTION_DOCUMENT_NO").getString() + "'");
                } catch (Exception e) {
                    System.out.println("Final Approval Date Not Updated.");
                }
                autoTagging(getAttribute("PRODUCTION_DOCUMENT_NO").getString());
//                System.out.println("update");
//                String sqlStr="UPDATE PRODUCTION.FELT_PROD_DATA P,PRODUCTION.FELT_PROD_DATA_H H SET P.CREATED_BY=H.UPDATED_BY,P.CREATED_DATE=H.ENTRY_DATE WHERE P.PROD_DOC_NO=H.PROD_DOC_NO AND H.REVISION_NO=1";
//                data.Execute(sqlStr);
//                System.out.println("updated");
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT = 'FELT FINISHING' AND PROD_DOC_NO='" + documentNo + "' AND APPROVED=" + 1;
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=603 AND USER_ID=" + userID + " AND DOC_NO='" + documentNo + "' AND STATUS='W'";
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();

                if (rsTmp.getInt("COUNT") > 0) {
                    strSQL = "DELETE FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT = 'FELT FINISHING'"
                            + " AND PROD_DOC_NO='" + documentNo + "'";
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
    public boolean IsEditable(String documentNo, int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";
        try {
            if (HistoryView) {
                return false;
            }
            tmpStmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //First check that document is approved or not
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT = 'FELT FINISHING' AND PROD_DOC_NO='" + documentNo + "' AND APPROVED=1";
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=603 AND USER_ID=" + userID + " AND DOC_NO='" + documentNo + "' AND STATUS='W'";
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
            String strSql = "SELECT * FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT = 'FELT FINISHING' " + stringFindQuery + "ORDER BY PROD_DOC_NO";
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

            //setAttribute("REVISION_NO",0);
            //setAttribute("PRODUCTION_DATE",resultSet.getString("PROD_DATE"));
            String docNo;
            docNo = resultSet.getString("PROD_DOC_NO");
            //totalWeight = data.getDoubleValueFromDB("SELECT SUM(WEIGHT) FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'FINISHING' AND MONTH(PROD_DATE)=MONTH('"+resultSet.getDate("PROD_DATE")+"') AND YEAR(PROD_DATE)=YEAR('"+ resultSet.getDate("PROD_DATE") +"') AND APPROVED=1");
            totalWeight = data.getDoubleValueFromDB("SELECT SUM(TAG_WEIGHT) FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT IN ('FINISHING','FELT FINISHING') AND MONTH(PROD_DATE)=MONTH('" + resultSet.getDate("PROD_DATE") + "') AND YEAR(PROD_DATE)=YEAR('" + resultSet.getDate("PROD_DATE") + "') AND APPROVED=1 AND PROD_PIECE_NO NOT LIKE ('%V%') AND CANCELED=0 ");
            //totalWeight = data.getDoubleValueFromDB("SELECT SUM(WEIGHT) FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT = 'FINISHING' AND MONTH(PROD_DATE)="+docNo.substring(7,9)+" AND YEAR(PROD_DATE)="+docNo.substring(3,7)+" AND APPROVED=1");
            setAttribute("TOTAL_WEIGHT", totalWeight);

            //previousWeight = data.getDoubleValueFromDB("SELECT SUM(WEIGHT) FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'FINISHING' AND MONTH(PROD_DATE)=MONTH('"+resultSet.getDate("PROD_DATE")+"') AND YEAR(PROD_DATE)=YEAR('"+ resultSet.getDate("PROD_DATE") +"') AND PROD_DATE<'"+resultSet.getDate("PROD_DATE")+"' AND APPROVED=1");
            previousWeight = data.getDoubleValueFromDB("SELECT SUM(TAG_WEIGHT) FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT IN ('FINISHING','FELT FINISHING') AND MONTH(PROD_DATE)=MONTH('" + resultSet.getDate("PROD_DATE") + "') AND YEAR(PROD_DATE)=YEAR('" + resultSet.getDate("PROD_DATE") + "') AND PROD_DATE<'" + resultSet.getDate("PROD_DATE") + "' AND APPROVED=1 AND PROD_PIECE_NO NOT LIKE ('%V%') AND CANCELED=0 ");
            //previousWeight = data.getDoubleValueFromDB("SELECT SUM(WEIGHT) FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT = 'FINISHING' AND MONTH(PROD_DATE)="+docNo.substring(7,9)+" AND YEAR(PROD_DATE)="+docNo.substring(3,7)+" AND SUBSTRING(PROD_DOC_NO,4,8)<'"+docNo.substring(3)+"' AND APPROVED=1");
            setAttribute("PREVIOUS_WEIGHT", previousWeight);

            //Now Populate the collection, first clear the collection
            //hmFeltFinishingDetails.clear();
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String str = "SELECT * FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DOC_NO = '" + docNo + "' AND PROD_DEPT = 'FELT FINISHING' ORDER BY PROD_DOC_NO,SR_NO";
            resultSetTemp = statementTemp.executeQuery(str);
            resultSetTemp.first();
            hmFeltFinishingDetails.clear();

            while (!resultSetTemp.isAfterLast()) {
                serialNoCounter++;
                clsFeltFinishingDetails ObjFeltFinishingDetails = new clsFeltFinishingDetails();
                setAttribute("REVISION_NO", 0);
                setAttribute("PRODUCTION_DATE", resultSetTemp.getString("PROD_DATE"));
                setAttribute("PRODUCTION_DOCUMENT_NO", resultSetTemp.getString("PROD_DOC_NO"));
                setAttribute("HEADER_REMARK", resultSetTemp.getString("HEADER_REMARK"));
                setAttribute("HIERARCHY_ID", resultSetTemp.getInt("HIERARCHY_ID"));
                setAttribute("PRODUCTION_FORM_NO", resultSetTemp.getString("PROD_FORM_NO"));

                setAttribute("FINAL_APPROVAL_DATE", resultSetTemp.getString("FINAL_APPROVAL_DATE"));

                ObjFeltFinishingDetails.setAttribute("PRODUCTION_PIECE_NO", resultSetTemp.getString("PROD_PIECE_NO"));
                ObjFeltFinishingDetails.setAttribute("PRODUCTION_PARTY_CODE", resultSetTemp.getString("PROD_PARTY_CODE"));
                ObjFeltFinishingDetails.setAttribute("LENGTH", resultSetTemp.getFloat("LENGTH"));
                ObjFeltFinishingDetails.setAttribute("WIDTH", resultSetTemp.getFloat("WIDTH"));
                ObjFeltFinishingDetails.setAttribute("WEIGHT", resultSetTemp.getFloat("WEIGHT"));
                ObjFeltFinishingDetails.setAttribute("TAG_WEIGHT", resultSetTemp.getFloat("TAG_WEIGHT"));
                ObjFeltFinishingDetails.setAttribute("STYLE_CODE", resultSetTemp.getString("STYLE_CODE"));
                ObjFeltFinishingDetails.setAttribute("BILL_STYLE_CODE", resultSetTemp.getString("BILL_STYLE_CODE"));
                ObjFeltFinishingDetails.setAttribute("REMARKS", resultSetTemp.getString("REMARKS"));

                setAttribute("CREATED_BY", resultSetTemp.getInt("CREATED_BY"));
                setAttribute("CREATED_DATE", resultSetTemp.getString("CREATED_DATE"));
                setAttribute("MODIFIED_BY", resultSetTemp.getInt("MODIFIED_BY"));
                setAttribute("MODIFIED_DATE", resultSetTemp.getString("MODIFIED_DATE"));
                setAttribute("APPROVED", resultSet.getInt("APPROVED"));
                setAttribute("APPROVED_DATE", resultSetTemp.getString("APPROVED_DATE"));
                setAttribute("REJECTED", resultSetTemp.getBoolean("REJECTED"));
                setAttribute("REJECTED_DATE", resultSetTemp.getString("REJECTED_DATE"));
                setAttribute("CANCELED", resultSetTemp.getInt("CANCELED"));
//                ObjFeltFinishingDetails.setAttribute("HIERARCHY_ID",resultSetTemp.getInt("HIERARCHY_ID"));

                hmFeltFinishingDetails.put(Integer.toString(serialNoCounter), ObjFeltFinishingDetails);
                resultSetTemp.next();
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

//    public boolean setHistoryData(String pProductionDate,String pDocNo) {
//        ResultSet  resultSetTemp;
//        Statement  statementTemp;
//        int serialNoCounter=0;
//        int RevNo=0;
//        try {
//            RevNo=resultSet.getInt("REVISION_NO");
//            setAttribute("REVISION_NO",resultSet.getInt("REVISION_NO"));
//            
//            //Now Populate the collection, first clear the collection
//            hmFeltFinishingDetails.clear();
//            
//            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
//            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_PROD_DATA_H WHERE PROD_DEPT = 'FELT FINISHING' AND PROD_DOC_NO='"+pDocNo+"' AND REVISION_NO='"+ resultSet.getInt("REVISION_NO") +"' ORDER BY REVISION_NO");
//            
//            while(resultSetTemp.next()) {
//                serialNoCounter++;
//                setAttribute("PRODUCTION_DOCUMENT_NO",resultSetTemp.getString("PROD_DOC_NO"));
//                setAttribute("PRODUCTION_FORM_NO",resultSetTemp.getString("PROD_FORM_NO"));
//                setAttribute("UPDATED_BY",resultSetTemp.getInt("UPDATED_BY"));
//                setAttribute("ENTRY_DATE",resultSetTemp.getString("ENTRY_DATE"));
//                setAttribute("HIERARCHY_ID",resultSetTemp.getInt("HIERARCHY_ID"));
//                
//                clsFeltFinishingDetails ObjFeltFinishingDetails = new clsFeltFinishingDetails();
//                
//                ObjFeltFinishingDetails.setAttribute("PRODUCTION_PIECE_NO",resultSetTemp.getString("PROD_PIECE_NO"));
//                ObjFeltFinishingDetails.setAttribute("PRODUCTION_PARTY_CODE",resultSetTemp.getString("PROD_PARTY_CODE"));
//                ObjFeltFinishingDetails.setAttribute("LENGTH",resultSetTemp.getFloat("LENGTH"));
//                ObjFeltFinishingDetails.setAttribute("WIDTH",resultSetTemp.getFloat("WIDTH"));
//                ObjFeltFinishingDetails.setAttribute("WEIGHT",resultSetTemp.getFloat("WEIGHT"));
//                ObjFeltFinishingDetails.setAttribute("REMARKS",resultSetTemp.getString("REMARKS"));
//                
//                hmFeltFinishingDetails.put(Integer.toString(serialNoCounter),ObjFeltFinishingDetails);
//            }
//            resultSetTemp.close();
//            statementTemp.close();
//            return true;
//        }
//        catch(Exception e) {
//            LastError = e.getMessage();
//            e.printStackTrace();
//            return false;
//        }
//    }
    public static HashMap getHistoryList(String productionDocumentNo) {
        HashMap hmHistoryList = new HashMap();
        Statement stTmp;
        ResultSet rsTmp;
        try {
            stTmp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp = stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_PROD_DATA_H WHERE PROD_DEPT = 'FELT FINISHING' AND PROD_DOC_NO='" + productionDocumentNo + "' GROUP BY REVISION_NO");

            while (rsTmp.next()) {
                clsFeltFinishing ObjFeltFinishing = new clsFeltFinishing();

                ObjFeltFinishing.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                ObjFeltFinishing.setAttribute("UPDATED_BY", rsTmp.getInt("UPDATED_BY"));
                ObjFeltFinishing.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                ObjFeltFinishing.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                ObjFeltFinishing.setAttribute("APPROVER_REMARKS", rsTmp.getString("APPROVER_REMARKS"));
                ObjFeltFinishing.setAttribute("FROM_IP", rsTmp.getString("FROM_IP"));

                hmHistoryList.put(Integer.toString(hmHistoryList.size() + 1), ObjFeltFinishing);
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
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_PROD_DATA_H WHERE PROD_DEPT='FELT FINISHING' AND PROD_DOC_NO='" + pDocNo + "'");
            Ready = true;
            historyProductionDate = pProductionDate;
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
                strSQL = "SELECT DISTINCT PROD_DOC_NO,PROD_DATE,SUBSTRING(RECEIVED_DATE,1,10) AS RECEIVED_DATE FROM PRODUCTION.FELT_PROD_DATA,PRODUCTION.FELT_PROD_DOC_DATA WHERE PROD_DOC_NO=DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=603 AND CANCELED=0 ORDER BY RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT PROD_DOC_NO,PROD_DATE,SUBSTRING(RECEIVED_DATE,1,10) AS RECEIVED_DATE FROM PRODUCTION.FELT_PROD_DATA,PRODUCTION.FELT_PROD_DOC_DATA WHERE PROD_DOC_NO=DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=603 AND CANCELED=0 ORDER BY PROD_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT PROD_DOC_NO,PROD_DATE,SUBSTRING(RECEIVED_DATE,1,10) AS RECEIVED_DATE FROM PRODUCTION.FELT_PROD_DATA,PRODUCTION.FELT_PROD_DOC_DATA WHERE PROD_DOC_NO=DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=603 AND CANCELED=0 ORDER BY PROD_DOC_NO";
            }

            ResultSet rsTmp = data.getConn().createStatement().executeQuery(strSQL);

            Counter = 0;
            while (rsTmp.next()) {

                Counter = Counter + 1;
                clsFeltFinishing ObjDoc = new clsFeltFinishing();

                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO", rsTmp.getString("PROD_DOC_NO"));
                ObjDoc.setAttribute("DOC_DATE", rsTmp.getString("PROD_DATE"));
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
        return data.getStringValueFromDB("SELECT PARTY_CD FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='" + pPieceNo + "'");
    }

    // Insert into Piece Register Table To confirm that Finishing has completed
    private void updatePieceRegister(String pDocNo) {
        try {
            // Piece Register connection
            //ResultSet rsPR = data.getConn().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO=0");

            //ResultSet rsTemp = data.getConn().createStatement().executeQuery("SELECT PROD_DOC_NO,PROD_PARTY_CODE,PROD_PIECE_NO,PR_PIECE_NO,LENGTH,WIDTH,WEIGHT,PR_STYLE,PR_PRODUCT_CODE FROM (SELECT PROD_DOC_NO,PROD_FORM_NO,REPLACE(PROD_PIECE_NO,' ','') PROD_PIECE_NO,PROD_PARTY_CODE,LENGTH,WIDTH,WEIGHT FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DOC_NO = '"+pDocNo+"' AND PROD_DEPT='FELT FINISHING' AND APPROVED=1) D LEFT JOIN (SELECT REPLACE(BALNK,' ','') AS STYLE,CONCAT(PRODUCT_CODE,'0') PRODUCT_CODE,PIECE_NO,PARTY_CD,ORDER_CODE FROM PRODUCTION.FELT_ORDER_MASTER) O ON O.PARTY_CD = D.PROD_PARTY_CODE AND O.PIECE_NO +0 = D.PROD_PIECE_NO +0");
            ResultSet rsTemp = data.getConn().createStatement().executeQuery("SELECT * FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DOC_NO = '" + pDocNo + "' AND PROD_DEPT='FELT FINISHING'");
            System.out.println("FINAL UPDATAION");
            while (rsTemp.next()) {

                //data.Execute("UPDATE PRODUCTION.FELT_QLT_RATE_MASTER SET EFFECTIVE_TO=SUBDATE('" + EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_FROM").getString()) + "',1) WHERE WH_CD='" + getAttribute("WH_CD").getString() + "' AND PRODUCT_CODE='" + getAttribute("PRODUCT_CODE").getString() + "' AND DOC_NO NOT IN ('" + getAttribute("DOC_NO").getString() + "') AND (EFFECTIVE_TO='0000-00-00' OR EFFECTIVE_TO>='" + EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_FROM").getString()) + "') AND APPROVED=1 ");
                //data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER P,PRODUCTION.FELT_PROD_DATA D SET P.PR_ACTUAL_LENGTH='"+rsTemp.getString("LENGTH")+"',P.PR_ACTUAL_WIDTH='"+rsTemp.getString("WIDTH")+"',P.PR_ACTUAL_WEIGHT='"+rsTemp.getString("WEIGHT")+"',P.PR_FNSG_DATE=CURDATE(),P.PR_RCV_DATE=CURDATE(),P.PR_PIECE_STAGE='IN STOCK' WHERE P.PR_PIECE_NO='"+rsTemp.getString("PROD_PIECE_NO")+"'");
                data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER P,PRODUCTION.FELT_PROD_DATA D SET P.WIP_ACTUAL_LENGTH='" + rsTemp.getString("LENGTH") + "',P.WIP_ACTUAL_WIDTH='" + rsTemp.getString("WIDTH") + "',P.WIP_ACTUAL_WEIGHT='" + rsTemp.getString("WEIGHT") + "',P.WIP_BILL_WEIGHT='" + rsTemp.getString("TAG_WEIGHT") + "',P.WIP_STYLE='" + rsTemp.getString("STYLE_CODE") + "',P.WIP_BILL_STYLE='" + rsTemp.getString("BILL_STYLE_CODE") + "',P.WIP_FNSG_DATE=CURDATE(),P.WIP_PIECE_STAGE='IN STOCK',P.WIP_STATUS='FINISHED',P.WIP_PRIORITY_HOLD_CAN_FLAG=6 WHERE P.WIP_PIECE_NO='" + rsTemp.getString("PROD_PIECE_NO") + "'");
                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER P,PRODUCTION.FELT_PROD_DATA D SET P.PR_ACTUAL_LENGTH='" + rsTemp.getString("LENGTH") + "',P.PR_ACTUAL_WIDTH='" + rsTemp.getString("WIDTH") + "',P.PR_ACTUAL_WEIGHT='" + rsTemp.getString("WEIGHT") + "',P.PR_BILL_WEIGHT='" + rsTemp.getString("TAG_WEIGHT") + "',P.PR_STYLE='" + rsTemp.getString("STYLE_CODE") + "',P.PR_BILL_STYLE='" + rsTemp.getString("BILL_STYLE_CODE") + "',P.PR_FNSG_DATE=CURDATE(),P.PR_RCV_DATE=CURDATE(),P.PR_PIECE_STAGE='IN STOCK',P.PR_WIP_STATUS='FINISHED' WHERE P.PR_PIECE_NO='" + rsTemp.getString("PROD_PIECE_NO") + "'");
                System.out.println("FINAL UPDATAION UPDATED ON PIECE NO : " + rsTemp.getString("PROD_PIECE_NO"));

                String tenderGSM = data.getStringValueFromDB("SELECT PR.WIP_TENDER_GSM FROM PRODUCTION.FELT_WIP_PIECE_REGISTER PR,DINESHMILLS.D_SAL_PARTY_MASTER PM WHERE PR.WIP_PARTY_CODE=PM.PARTY_CODE AND PM.TAGGING_APPROVAL_IND=3 AND PR.WIP_PIECE_NO='" + rsTemp.getString("PROD_PIECE_NO") + "' AND PR.WIP_GROUP NOT IN ('HDS','SDF')");
                // ADDED on 28/01/2019
                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_WIP_PIECE_REGISTER PR,DINESHMILLS.D_SAL_PARTY_MASTER PM WHERE PR.WIP_PARTY_CODE=PM.PARTY_CODE AND PM.TAGGING_APPROVAL_IND=3 AND PR.WIP_PIECE_NO='" + rsTemp.getString("PROD_PIECE_NO") + "' AND PR.WIP_GROUP NOT IN ('HDS','SDF')")) {
                    data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER PR,DINESHMILLS.D_SAL_PARTY_MASTER PM SET PR.WIP_BILL_GSM=PR.WIP_TENDER_GSM WHERE PR.WIP_PARTY_CODE=PM.PARTY_CODE AND PM.TAGGING_APPROVAL_IND=3 AND PR.WIP_PIECE_NO='" + rsTemp.getString("PROD_PIECE_NO") + "' AND PR.WIP_GROUP NOT IN ('HDS','SDF')");
                    data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER PR,DINESHMILLS.D_SAL_PARTY_MASTER PM SET PR.PR_BILL_GSM=PR.PR_TENDER_GSM WHERE PR.PR_PARTY_CODE=PM.PARTY_CODE AND PM.TAGGING_APPROVAL_IND=3 AND PR.PR_PIECE_NO='" + rsTemp.getString("PROD_PIECE_NO") + "' AND PR.PR_GROUP NOT IN ('HDS','SDF')");
                }
                // ADDED on 28/01/2019

                // ADDED on 04/01/2021
                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_WIP_PIECE_REGISTER PR,DINESHMILLS.D_SAL_PARTY_MASTER PM WHERE PR.WIP_PARTY_CODE=PM.PARTY_CODE AND PM.TAGGING_APPROVAL_IND=4 AND PR.WIP_PIECE_NO='" + rsTemp.getString("PROD_PIECE_NO") + "' AND PR.WIP_GROUP NOT IN ('HDS','SDF')")) {
                    double trWt = data.getDoubleValueFromDB("SELECT WIP_THORITICAL_WEIGHT FROM PRODUCTION.FELT_WIP_PIECE_REGISTER WHERE WIP_PIECE_NO='" + rsTemp.getString("PROD_PIECE_NO") + "'");
                    data.Execute("INSERT INTO PRODUCTION.TAGGED_PIECE_DATA (PIECE_NO, PARTY_CODE, ACTUAL_WEIGHT, THEORITICAL_WEIGHT, TAGGED_WEIGHT) VALUES ('" + rsTemp.getString("PROD_PIECE_NO") + "','" + rsTemp.getString("PROD_PARTY_CODE") + "'," + rsTemp.getString("WEIGHT") + "," + trWt + "," + rsTemp.getString("TAG_WEIGHT") + ") ");
                    data.Execute("INSERT INTO PRODUCTION.FELT_FINISHING_PIECE_TAGGING (PIECE_NO, PARTY_CODE, PRODUCT_CODE, LENGTH, WIDTH, WEIGHT, GSM, SQMTR, PIECE_GROUP, STYLE, BILL_PRODUCT_CODE, BILL_LENGTH, BILL_WIDTH, BILL_WEIGHT, BILL_GSM, BILL_SQMTR, ACTUAL_WEIGHT, THEORETICAL_WEIGHT, TAGGED_WEIGHT) SELECT PR_PIECE_NO, PR_PARTY_CODE, PR_PRODUCT_CODE, PR_LENGTH, PR_WIDTH, PR_ACTUAL_WEIGHT, PR_GSM, PR_SQMTR, PR_GROUP, PR_STYLE, PR_BILL_PRODUCT_CODE, PR_BILL_LENGTH, PR_BILL_WIDTH, PR_BILL_WEIGHT, PR_BILL_GSM, PR_BILL_SQMTR, PR_ACTUAL_WEIGHT, PR_THORITICAL_WEIGHT, PR_BILL_WEIGHT FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + rsTemp.getString("PROD_PIECE_NO") + "' ");
                    data.Execute("UPDATE PRODUCTION.FELT_FINISHING_PIECE_TAGGING PT,DINESHMILLS.D_SAL_PARTY_MASTER PM SET PT.PARTY_NAME=PM.PARTY_NAME WHERE PT.PARTY_CODE=PM.PARTY_CODE AND PT.PIECE_NO='" + rsTemp.getString("PROD_PIECE_NO") + "' ");
                    notionalPL(rsTemp.getString("PROD_PIECE_NO").trim());
                }
                // ADDED on 04/01/2021

//                rsPR.moveToInsertRow();
//                rsPR.updateInt("WH_CODE",2);
//                rsPR.updateString("PRODUCT_CD", rsTemp.getString("PRODUCT_CODE"));
//                rsPR.updateString("ORDER_CD", rsTemp.getString("ORDER_CODE"));
//                rsPR.updateString("PIECE_NO", rsTemp.getString("PROD_PIECE_NO"));
//                rsPR.updateString("RCVD_DATE", rsTemp.getString("PROD_DATE"));
//                rsPR.updateString("RCVD_MTR", rsTemp.getString("LENGTH"));
//                rsPR.updateString("RECD_WDTH", rsTemp.getString("WIDTH"));
//                rsPR.updateString("RECD_KG", rsTemp.getString("WEIGHT"));
//                rsPR.updateInt("CREATED_BY", EITLERPGLOBAL.gNewUserID);
//                rsPR.updateString("CREATED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP FROM DUAL"));
//                rsPR.updateString("PARTY_CODE",rsTemp.getString("PROD_PARTY_CODE"));
//                rsPR.updateString("STYLE", rsTemp.getString("STYLE"));
//                rsPR.updateString("REMARK", "IN STOCK");
//                rsPR.updateString("ORDER_NO", rsTemp.getString("PIECE_NO"));
//                rsPR.insertRow();
            }

            rsTemp.close();
            //rsPR.close();
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
        }
    }

    private void updatePieceRegisterS(String pDocNo) {
        try {

            ResultSet rsTemp = data.getConn().createStatement().executeQuery("SELECT * FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DOC_NO = '" + pDocNo + "' AND PROD_DEPT='FELT FINISHING'");
            System.out.println("FINISHING INSERT UPDATAION");
            while (rsTemp.next()) {
                //data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER P,PRODUCTION.FELT_PROD_DATA D SET P.PR_ACTUAL_LENGTH='"+rsTemp.getString("LENGTH")+"',P.PR_ACTUAL_WIDTH='"+rsTemp.getString("WIDTH")+"',P.PR_ACTUAL_WEIGHT='"+rsTemp.getString("WEIGHT")+"',P.PR_BILL_WEIGHT='"+rsTemp.getString("TAG_WEIGHT")+"',P.PR_STYLE='"+rsTemp.getString("STYLE_CODE")+"',P.PR_FNSG_DATE=CURDATE(),P.PR_PIECE_STAGE='FELT FINISHING' WHERE P.PR_PIECE_NO='"+rsTemp.getString("PROD_PIECE_NO")+"'");
//                data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER P,PRODUCTION.FELT_PROD_DATA D SET P.WIP_BILL_WEIGHT='" + rsTemp.getString("TAG_WEIGHT") + "',P.WIP_STYLE='" + rsTemp.getString("STYLE_CODE") + "',P.WIP_BILL_STYLE='" + rsTemp.getString("BILL_STYLE_CODE") + "' WHERE P.WIP_PIECE_NO='" + rsTemp.getString("PROD_PIECE_NO") + "'");
//                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER P,PRODUCTION.FELT_PROD_DATA D SET P.PR_BILL_WEIGHT='" + rsTemp.getString("TAG_WEIGHT") + "',P.PR_STYLE='" + rsTemp.getString("STYLE_CODE") + "',P.PR_BILL_STYLE='" + rsTemp.getString("BILL_STYLE_CODE") + "' WHERE P.PR_PIECE_NO='" + rsTemp.getString("PROD_PIECE_NO") + "'");
                
                data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER P,PRODUCTION.FELT_PROD_DATA D SET P.WIP_STYLE='" + rsTemp.getString("STYLE_CODE") + "',P.WIP_BILL_STYLE='" + rsTemp.getString("BILL_STYLE_CODE") + "' WHERE P.WIP_PIECE_NO='" + rsTemp.getString("PROD_PIECE_NO") + "'");
                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER P,PRODUCTION.FELT_PROD_DATA D SET P.PR_STYLE='" + rsTemp.getString("STYLE_CODE") + "',P.PR_BILL_STYLE='" + rsTemp.getString("BILL_STYLE_CODE") + "' WHERE P.PR_PIECE_NO='" + rsTemp.getString("PROD_PIECE_NO") + "'");
                
                System.out.println("FINISHING INSERT UPDATAION UPDATED ON PIECE NO : " + rsTemp.getString("PROD_PIECE_NO"));
            }

            rsTemp.close();
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
        }
    }

    // checks piece no already exist in db
    public boolean checkPieceNoInDB(String pPieceNo) {
        //int count=data.getIntValueFromDB("SELECT COUNT(PROD_PIECE_NO) FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT = 'FELT FINISHING' AND PROD_PIECE_NO='"+pPieceNo+"'");
        int count = data.getIntValueFromDB("SELECT COUNT(PROD_PIECE_NO) FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT IN ('FINISHING','FELT FINISHING') AND PROD_PIECE_NO='" + pPieceNo + "' AND CANCELED=0");
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPieceNoInDB(String pPieceNo, String pProdDate) {
        //int count=data.getIntValueFromDB("SELECT COUNT(PROD_PIECE_NO) FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT = 'FELT FINISHING' AND PROD_PIECE_NO='"+pPieceNo+"' AND PROD_DATE='"+pProdDate+"'");
        int count = data.getIntValueFromDB("SELECT COUNT(PROD_PIECE_NO) FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT IN ('FINISHING','FELT FINISHING') AND PROD_PIECE_NO='" + pPieceNo + "' AND PROD_DATE='" + pProdDate + "'");
        if (count >= 1) {
            int counter = 0;
            try {
                Connection Conn = data.getConn();
                Statement stTmp = Conn.createStatement();
                //ResultSet rsTmp=stTmp.executeQuery("SELECT PROD_DOC_NO,PROD_DATE FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT='FELT FINISHING' AND PROD_PIECE_NO='"+pPieceNo+"' AND PROD_DATE='"+pProdDate+"'");
                ResultSet rsTmp = stTmp.executeQuery("SELECT PROD_DOC_NO,PROD_DATE FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT IN ('FINISHING','FELT FINISHING') AND PROD_PIECE_NO='" + pPieceNo + "' AND PROD_DATE='" + pProdDate + "'");
                while (rsTmp.next()) {
                    if (rsTmp.getString("PROD_DATE").equals(EITLERPGLOBAL.formatDateDB(pProdDate))) {
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

    // checks production date already exist in db
    public boolean checkProductionDateInDB(String pProdDate) {
        int count = data.getIntValueFromDB("SELECT COUNT(PROD_DATE) FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT IN ('FINISHING','FELT FINISHING') AND PROD_DATE='" + pProdDate + "'");
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    // generates report data
    public TReportWriter.SimpleDataProvider.TTable getReportData(String prodDate) {
        TReportWriter.SimpleDataProvider.TTable objData = new TReportWriter.SimpleDataProvider.TTable();
        objData.AddColumn("SR_NO");
        objData.AddColumn("REPORT_NO");
        objData.AddColumn("PROD_DATE");
        objData.AddColumn("PARTY_CODE");
        objData.AddColumn("PARTY_NAME");
        objData.AddColumn("FELT_NO");
        objData.AddColumn("LENGTH");
        objData.AddColumn("WIDTH");
        objData.AddColumn("WEIGHT");
        objData.AddColumn("SYN_PER");
        objData.AddColumn("STYLE");
        objData.AddColumn("GROUP");
        objData.AddColumn("PRODUCT_CODE");
        objData.AddColumn("ITEM_DESC");
        objData.AddColumn("TOTAL_LENGTH");
        objData.AddColumn("TOTAL_WIDTH");
        objData.AddColumn("TOTAL_WEIGHT");

        try {
            TReportWriter.SimpleDataProvider.TRow objRow = objData.newRow();

            String str = "SELECT PROD_DATE,PROD_FORM_NO,PROD_PARTY_CODE,COALESCE(PARTY_NAME,'') PARTY_NAME,PROD_PIECE_NO,LENGTH,WIDTH,WEIGHT,SYN_PER,STYLE,GRUP,PRODUCT_CODE,ITEM_DESC,TOTAL_LENGTH,TOTAL_WIDTH,TOTAL_WEIGHT FROM (SELECT PROD_DATE,PROD_FORM_NO,REPLACE(PROD_PIECE_NO,' ','') PROD_PIECE_NO,PROD_PARTY_CODE,LENGTH,WIDTH,WEIGHT FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DATE = '" + prodDate + "' AND PROD_DEPT='FELT FINISHING') D LEFT JOIN (SELECT REPLACE(BALNK,' ','') AS STYLE,PRODUCT_CODE,PIECE_NO,PARTY_CD FROM PRODUCTION.FELT_ORDER_MASTER) O ON O.PARTY_CD = D.PROD_PARTY_CODE AND O.PIECE_NO +0 = D.PROD_PIECE_NO +0 LEFT JOIN (SELECT PARTY_CODE, SUBSTRING(TRIM(PARTY_NAME),1,23) PARTY_NAME FROM D_SAL_PARTY_MASTER) AS P ON P.PARTY_CODE = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(LENGTH),0) AS TOTAL_LENGTH FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'FELT FINISHING' AND MONTH(PROD_DATE)=MONTH('" + prodDate + "') AND YEAR(PROD_DATE)=YEAR('" + prodDate + "') AND PROD_DATE<'" + prodDate + "' AND APPROVED=1) AS TL ON O.PARTY_CD = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WIDTH),0) AS TOTAL_WIDTH FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'FELT FINISHING' AND MONTH(PROD_DATE)=MONTH('" + prodDate + "') AND YEAR(PROD_DATE)=YEAR('" + prodDate + "') AND PROD_DATE='" + prodDate + "' AND APPROVED=1) AS TW ON O.PARTY_CD = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS TOTAL_WEIGHT FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'FELT FINISHING' AND MONTH(PROD_DATE)=MONTH('" + prodDate + "') AND YEAR(PROD_DATE)=YEAR('" + prodDate + "') AND PROD_DATE<='" + prodDate + "' AND APPROVED=1) AS WT ON O.PARTY_CD = D.PROD_PARTY_CODE LEFT JOIN (SELECT ITEM_CODE,TRIM(ITEM_DESC) ITEM_DESC, SYN_PER,GRUP FROM PRODUCTION.FELT_RATE_MASTER) AS R ON SUBSTRING(ITEM_CODE,1,6)=PRODUCT_CODE";
            ResultSet rsTemp = data.getResult(str);
            int counter = 1;
            while (!rsTemp.isAfterLast()) {
                objRow = objData.newRow();
                objRow.setValue("SR_NO", String.valueOf(counter));
                objRow.setValue("REPORT_NO", rsTemp.getString("PROD_FORM_NO"));
                objRow.setValue("PROD_DATE", EITLERPGLOBAL.formatDate(rsTemp.getString("PROD_DATE")));
                objRow.setValue("PARTY_CODE", rsTemp.getString("PROD_PARTY_CODE"));
                objRow.setValue("PARTY_NAME", rsTemp.getString("PARTY_NAME"));
                objRow.setValue("FELT_NO", rsTemp.getString("PROD_PIECE_NO"));
                objRow.setValue("LENGTH", rsTemp.getString("LENGTH"));
                objRow.setValue("WIDTH", rsTemp.getString("WIDTH"));
                objRow.setValue("WEIGHT", rsTemp.getString("WEIGHT"));
                objRow.setValue("SYN_PER", rsTemp.getString("SYN_PER"));
                objRow.setValue("STYLE", rsTemp.getString("STYLE"));
                objRow.setValue("GROUP", rsTemp.getString("GRUP"));
                objRow.setValue("PRODUCT_CODE", rsTemp.getString("PRODUCT_CODE"));
                objRow.setValue("ITEM_DESC", rsTemp.getString("ITEM_DESC"));
                objRow.setValue("TOTAL_LENGTH", rsTemp.getString("TOTAL_LENGTH"));
                objRow.setValue("TOTAL_WIDTH", rsTemp.getString("TOTAL_WIDTH"));
                objRow.setValue("TOTAL_WEIGHT", rsTemp.getString("TOTAL_WEIGHT"));
                objData.AddRow(objRow);
                counter++;
                rsTemp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objData;
    }

    // generates report data
    public TReportWriter.SimpleDataProvider.TTable getReportData(String prodNo, String prodDate) {
        TReportWriter.SimpleDataProvider.TTable objData = new TReportWriter.SimpleDataProvider.TTable();
        objData.AddColumn("SR_NO");
        objData.AddColumn("PROD_DATE");
        objData.AddColumn("PROD_DOC_NO");
        objData.AddColumn("PROD_PIECE_NO");
        objData.AddColumn("PR_STYLE");
        objData.AddColumn("PR_PRODUCT_CODE");
        objData.AddColumn("GROUP_NAME");
        objData.AddColumn("PRODUCT_DESC");
        objData.AddColumn("SYN_PER");
        objData.AddColumn("PR_PARTY_CODE");
        objData.AddColumn("PARTY_NAME");
        objData.AddColumn("LENGTH");
        objData.AddColumn("WIDTH");
        objData.AddColumn("WEIGHT");
        objData.AddColumn("TOTAL_LENGTH");
        objData.AddColumn("TOTAL_WEIGHT");
        objData.AddColumn("PR_FELT_RATE");
        objData.AddColumn("PR_FELT_VALUE_WITHOUT_DISCOUNT");

        try {
            TReportWriter.SimpleDataProvider.TRow objRow = objData.newRow();

//            String str ="SELECT 1 AS SR_NO,F.PROD_DATE,RIGHT(F.PROD_DOC_NO,6) AS PROD_DOC_NO,F.PROD_PIECE_NO,P.PR_STYLE,P.PR_PRODUCT_CODE,Q.GROUP_NAME,Q.PRODUCT_DESC,Q.SYN_PER,P.PR_PARTY_CODE,SUBSTRING(S.PARTY_NAME,1,18) AS PARTY_NAME,P.PR_BILL_LENGTH AS LENGTH,P.PR_BILL_WIDTH AS WIDTH,F.WEIGHT AS WEIGHT FROM PRODUCTION.FELT_PROD_DATA F, ";
//            str += "PRODUCTION.FELT_SALES_PIECE_REGISTER P, ";
//            str += "PRODUCTION.FELT_QLT_RATE_MASTER Q, ";
//            str += "DINESHMILLS.D_SAL_PARTY_MASTER S ";
//            str += "WHERE F.PROD_PIECE_NO=P.PR_PIECE_NO ";
//            str += "AND F.PROD_DOC_NO='"+prodNo+"' ";
//            str += "AND F.PROD_DATE='"+prodDate+"' ";
//            str += "AND SUBSTRING(P.PR_PRODUCT_CODE,1,6)=Q.PRODUCT_CODE ";
//            str += "AND S.PARTY_CODE=P.PR_PARTY_CODE ";
//            str += "AND S.MAIN_ACCOUNT_CODE=210010 ";
//            str += "AND F.PROD_DEPT = 'FELT FINISHING' AND F.APPROVED=1 AND F.CANCELED=0 ";
//            str += "AND (Q.EFFECTIVE_TO='0000-00-00' OR Q.EFFECTIVE_TO IS NULL) AND Q.APPROVED=1 AND Q.CANCELED=0 ";
//            str += "AND S.APPROVED=1 AND S.CANCELLED=0 ";
//            String str ="SELECT SR_NO,PROD_DATE,PROD_DOC_NO,PROD_PIECE_NO,PR_STYLE,PR_PRODUCT_CODE,GROUP_NAME,PRODUCT_DESC,SYN_PER,PR_PARTY_CODE,COALESCE(PARTY_NAME,'') AS PARTY_NAME,LENGTH,WIDTH,WEIGHT FROM (";
//            str += "SELECT 1 AS SR_NO,F.PROD_DATE,RIGHT(F.PROD_DOC_NO,6) AS PROD_DOC_NO,F.PROD_PIECE_NO,P.PR_STYLE,P.PR_PRODUCT_CODE,Q.GROUP_NAME,Q.PRODUCT_DESC,Q.SYN_PER,P.PR_PARTY_CODE,P.PR_BILL_LENGTH AS LENGTH,P.PR_BILL_WIDTH AS WIDTH,F.TAG_WEIGHT AS WEIGHT ";
//            str += "FROM PRODUCTION.FELT_PROD_DATA F, PRODUCTION.FELT_SALES_PIECE_REGISTER P, PRODUCTION.FELT_QLT_RATE_MASTER Q ";
//            str += "WHERE F.PROD_PIECE_NO=P.PR_PIECE_NO ";
//            str += "AND F.PROD_DOC_NO='"+prodNo+"' ";
//            str += "AND F.PROD_DATE='"+prodDate+"' ";
//            str += "AND SUBSTRING(P.PR_PRODUCT_CODE,1,6)=Q.PRODUCT_CODE ";
//            str += "AND F.PROD_DEPT = 'FELT FINISHING' ";//AND F.APPROVED=1 AND F.CANCELED=0
//            str += "AND (Q.EFFECTIVE_TO='0000-00-00' OR Q.EFFECTIVE_TO IS NULL) AND Q.APPROVED=1 AND Q.CANCELED=0 ) AS DA ";
//            str += "LEFT JOIN (SELECT PARTY_CODE, SUBSTRING(PARTY_NAME,1,18) AS PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND APPROVED=1 AND CANCELLED=0 ) AS DP ";
//            str += "ON DP.PARTY_CODE = DA.PR_PARTY_CODE ";
            String str = "SELECT SR_NO,PROD_DATE,PROD_DOC_NO,PROD_PIECE_NO,PR_STYLE,PR_PRODUCT_CODE,GROUP_NAME,PRODUCT_DESC,SYN_PER,PR_PARTY_CODE,COALESCE(PARTY_NAME,'') AS PARTY_NAME,LENGTH,WIDTH,WEIGHT,TOTAL_LENGTH,TOTAL_WEIGHT,PR_FELT_RATE,PR_FELT_VALUE_WITHOUT_DISCOUNT  ";
            str += "FROM (SELECT 1 AS SR_NO,F.PROD_DATE,RIGHT(F.PROD_DOC_NO,6) AS PROD_DOC_NO,F.PROD_PIECE_NO,P.PR_BILL_STYLE AS PR_STYLE, ";
            str += "P.PR_BILL_PRODUCT_CODE AS PR_PRODUCT_CODE,Q.GROUP_NAME,Q.PRODUCT_DESC,Q.SYN_PER,P.PR_PARTY_CODE,P.PR_BILL_LENGTH AS LENGTH, ";
            str += "P.PR_BILL_WIDTH AS WIDTH,F.TAG_WEIGHT AS WEIGHT, ";
            str += "COALESCE(P.PR_FELT_RATE,'0') AS PR_FELT_RATE,COALESCE(P.PR_FELT_VALUE_WITHOUT_DISCOUNT,'0') AS PR_FELT_VALUE_WITHOUT_DISCOUNT ";
            str += "FROM PRODUCTION.FELT_PROD_DATA F, PRODUCTION.FELT_SALES_PIECE_REGISTER P, PRODUCTION.FELT_QLT_RATE_MASTER Q ";
            str += "WHERE F.PROD_PIECE_NO=P.PR_PIECE_NO ";
            str += "AND F.PROD_DOC_NO='" + prodNo + "' ";
            str += "AND F.PROD_DATE='" + prodDate + "' ";
            str += "AND SUBSTRING(P.PR_BILL_PRODUCT_CODE,1,6)=Q.PRODUCT_CODE ";
            str += "AND F.PROD_DEPT = 'FELT FINISHING' ";//#AND F.APPROVED=1 AND F.CANCELED=0
            str += "AND (Q.EFFECTIVE_TO='0000-00-00' OR Q.EFFECTIVE_TO IS NULL) AND Q.APPROVED=1 AND Q.CANCELED=0 ) AS DA ";
            str += "LEFT JOIN (SELECT PARTY_CODE, SUBSTRING(PARTY_NAME,1,18) AS PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND APPROVED=1 AND CANCELLED=0 ) AS DP ON DP.PARTY_CODE = DA.PR_PARTY_CODE ";
            str += "LEFT JOIN (SELECT COALESCE(SUM(P.PR_BILL_LENGTH),0) AS TOTAL_LENGTH FROM PRODUCTION.FELT_PROD_DATA D,PRODUCTION.FELT_SALES_PIECE_REGISTER P WHERE D.PROD_DEPT = 'FELT FINISHING' AND MONTH(D.PROD_DATE)=MONTH('" + prodDate + "') AND YEAR(D.PROD_DATE)=YEAR('" + prodDate + "') AND D.PROD_PIECE_NO=P.PR_PIECE_NO AND P.PR_PIECE_NO NOT LIKE '%V%' AND D.PROD_DATE<='" + prodDate + "' AND D.PROD_DOC_NO<='" + prodNo + "' ) AS TL ON DP.PARTY_CODE = DA.PR_PARTY_CODE ";
            str += "LEFT JOIN (SELECT COALESCE(SUM(TAG_WEIGHT),0) AS TOTAL_WEIGHT FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT = 'FELT FINISHING' AND MONTH(PROD_DATE)=MONTH('" + prodDate + "') AND YEAR(PROD_DATE)=YEAR('" + prodDate + "') AND PROD_DATE<='" + prodDate + "' AND PROD_DOC_NO<='" + prodNo + "' AND PROD_PIECE_NO NOT LIKE '%V%' ) AS WT ON DP.PARTY_CODE = DA.PR_PARTY_CODE ";

            ResultSet rsTemp = data.getResult(str);
            int counter = 1;
            while (!rsTemp.isAfterLast()) {
                objRow = objData.newRow();
                objRow.setValue("SR_NO", rsTemp.getString("SR_NO"));
                objRow.setValue("PROD_DOC_NO", rsTemp.getString("PROD_DOC_NO"));
                objRow.setValue("PROD_DATE", EITLERPGLOBAL.formatDate(rsTemp.getString("PROD_DATE")));
                objRow.setValue("PROD_PIECE_NO", rsTemp.getString("PROD_PIECE_NO"));
                objRow.setValue("PR_STYLE", rsTemp.getString("PR_STYLE"));
                objRow.setValue("PR_PRODUCT_CODE", rsTemp.getString("PR_PRODUCT_CODE"));
                objRow.setValue("GROUP_NAME", rsTemp.getString("GROUP_NAME"));
                objRow.setValue("PRODUCT_DESC", rsTemp.getString("PRODUCT_DESC"));
                objRow.setValue("SYN_PER", rsTemp.getString("SYN_PER"));
                objRow.setValue("PR_PARTY_CODE", rsTemp.getString("PR_PARTY_CODE"));
                objRow.setValue("PARTY_NAME", rsTemp.getString("PARTY_NAME"));
                objRow.setValue("LENGTH", rsTemp.getString("LENGTH"));
                objRow.setValue("WIDTH", rsTemp.getString("WIDTH"));
                objRow.setValue("WEIGHT", rsTemp.getString("WEIGHT"));
                objRow.setValue("TOTAL_LENGTH", rsTemp.getString("TOTAL_LENGTH"));
                objRow.setValue("TOTAL_WEIGHT", rsTemp.getString("TOTAL_WEIGHT"));
                objRow.setValue("PR_FELT_RATE", rsTemp.getString("PR_FELT_RATE"));
                objRow.setValue("PR_FELT_VALUE_WITHOUT_DISCOUNT", rsTemp.getString("PR_FELT_VALUE_WITHOUT_DISCOUNT"));

                objData.AddRow(objRow);
                counter++;
                rsTemp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objData;
    }

    public static String getNextFreeNo(int pCompanyID, int pModuleID, int pFirstFreeNo, boolean UpdateLastNo) {
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
            strSQL = "SELECT LAST_USED_NO,PADDING_BY,NO_LENGTH,PREFIX_CHARS,SUFFIX_CHARS FROM D_COM_FIRSTFREE WHERE COMPANY_ID=" + pCompanyID + " AND MODULE_ID=" + pModuleID + " AND FIRSTFREE_NO=" + pFirstFreeNo;
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
                    data.Execute("UPDATE D_COM_FIRSTFREE SET LAST_USED_NO='" + strNewNo.trim() + "',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + pCompanyID + " AND MODULE_ID=" + pModuleID + " AND FIRSTFREE_NO=" + pFirstFreeNo);
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

    public static void CancelDoc(String pAmendNo) {
        ResultSet rsTmp = null;

        if (CanCancel(pAmendNo)) {

            boolean Approved = false;

            try {
                rsTmp = data.getResult("SELECT APPROVED FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DOC_NO='" + pAmendNo + "' ");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    Approved = rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();

                if (!Approved) {
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='" + pAmendNo + "' AND MODULE_ID=603");
                }
                data.Execute("UPDATE PRODUCTION.FELT_PROD_DATA SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE PROD_DOC_NO='" + pAmendNo + "'");

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
            System.out.println("SELECT PROD_DOC_NO FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");
            rsTmp = stTmp.executeQuery("SELECT PROD_DOC_NO FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");

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

    private void autoTagging(String pDocNo) {
        try {
            // Piece Register connection
            //ResultSet rsPR = data.getConn().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO=0");

            //ResultSet rsTemp = data.getConn().createStatement().executeQuery("SELECT PROD_DOC_NO,PROD_PARTY_CODE,PROD_PIECE_NO,PR_PIECE_NO,LENGTH,WIDTH,WEIGHT,PR_STYLE,PR_PRODUCT_CODE FROM (SELECT PROD_DOC_NO,PROD_FORM_NO,REPLACE(PROD_PIECE_NO,' ','') PROD_PIECE_NO,PROD_PARTY_CODE,LENGTH,WIDTH,WEIGHT FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DOC_NO = '"+pDocNo+"' AND PROD_DEPT='FELT FINISHING' AND APPROVED=1) D LEFT JOIN (SELECT REPLACE(BALNK,' ','') AS STYLE,CONCAT(PRODUCT_CODE,'0') PRODUCT_CODE,PIECE_NO,PARTY_CD,ORDER_CODE FROM PRODUCTION.FELT_ORDER_MASTER) O ON O.PARTY_CD = D.PROD_PARTY_CODE AND O.PIECE_NO +0 = D.PROD_PIECE_NO +0");
            ResultSet rsTemp = data.getConn().createStatement().executeQuery("SELECT * FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DOC_NO = '" + pDocNo + "' AND PROD_DEPT='FELT FINISHING'");
            System.out.println("-------------------AUTO TAGGING : START-------------------");
            while (rsTemp.next()) {
                data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER PR,DINESHMILLS.D_SAL_PARTY_MASTER PM SET PR.WIP_BILL_WEIGHT=PR.WIP_THORITICAL_WEIGHT WHERE PR.WIP_PARTY_CODE=PM.PARTY_CODE AND PM.TAGGING_APPROVAL_IND=2 AND PR.WIP_PIECE_NO='" + rsTemp.getString("PROD_PIECE_NO") + "' AND PR.WIP_PIECE_STAGE='IN STOCK' AND PR.WIP_GROUP NOT IN ('HDS','SDF')");
                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER PR,DINESHMILLS.D_SAL_PARTY_MASTER PM SET PR.PR_BILL_WEIGHT=PR.PR_THORITICAL_WEIGHT WHERE PR.PR_PARTY_CODE=PM.PARTY_CODE AND PM.TAGGING_APPROVAL_IND=2 AND PR.PR_PIECE_NO='" + rsTemp.getString("PROD_PIECE_NO") + "' AND PR.PR_PIECE_STAGE='IN STOCK' AND PR.PR_GROUP NOT IN ('HDS','SDF')");
                System.out.println("FINAL UPDATAION UPDATED ON PIECE NO : " + rsTemp.getString("PROD_PIECE_NO"));
            }

            rsTemp.close();
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
        }
    }

    private void notionalPL(String pieceNo) {

        String pPieceNo = pieceNo;
        String pPartyCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM PRODUCTION.FELT_FINISHING_PIECE_TAGGING WHERE PIECE_NO='" + pPieceNo + "'");

        String pBillProdCode = data.getStringValueFromDB("SELECT BILL_PRODUCT_CODE FROM PRODUCTION.FELT_FINISHING_PIECE_TAGGING WHERE PIECE_NO='" + pPieceNo + "'");
        float pBillLength = Float.parseFloat(data.getStringValueFromDB("SELECT BILL_LENGTH FROM PRODUCTION.FELT_FINISHING_PIECE_TAGGING WHERE PIECE_NO='" + pPieceNo + "'"));
        float pBillWidth = Float.parseFloat(data.getStringValueFromDB("SELECT BILL_WIDTH FROM PRODUCTION.FELT_FINISHING_PIECE_TAGGING WHERE PIECE_NO='" + pPieceNo + "'"));
        float pBillWeight = Float.parseFloat(data.getStringValueFromDB("SELECT TAGGED_WEIGHT FROM PRODUCTION.FELT_FINISHING_PIECE_TAGGING WHERE PIECE_NO='" + pPieceNo + "'"));
        float pBillSqmtr = Float.parseFloat(data.getStringValueFromDB("SELECT BILL_SQMTR FROM PRODUCTION.FELT_FINISHING_PIECE_TAGGING WHERE PIECE_NO='" + pPieceNo + "'"));

        String pActualProdCode = data.getStringValueFromDB("SELECT PRODUCT_CODE FROM PRODUCTION.FELT_FINISHING_PIECE_TAGGING WHERE PIECE_NO='" + pPieceNo + "'");
        float pActualLength = Float.parseFloat(data.getStringValueFromDB("SELECT LENGTH FROM PRODUCTION.FELT_FINISHING_PIECE_TAGGING WHERE PIECE_NO='" + pPieceNo + "'"));
        float pActualWidth = Float.parseFloat(data.getStringValueFromDB("SELECT WIDTH FROM PRODUCTION.FELT_FINISHING_PIECE_TAGGING WHERE PIECE_NO='" + pPieceNo + "'"));
        float pActualWeight = Float.parseFloat(data.getStringValueFromDB("SELECT ACTUAL_WEIGHT FROM PRODUCTION.FELT_FINISHING_PIECE_TAGGING WHERE PIECE_NO='" + pPieceNo + "'"));
        float pActualSqmtr = Float.parseFloat(data.getStringValueFromDB("SELECT SQMTR FROM PRODUCTION.FELT_FINISHING_PIECE_TAGGING WHERE PIECE_NO='" + pPieceNo + "'"));

        String pCurDate = EITLERPGLOBAL.getCurrentDateDB();

        String pBillUnit = "", pActualUnit = "";

        float pBRate = 0, pBDiscRate = 0, pBYearEndDiscRate = 0;
        float pARate = 0, pADiscRate = 0, pAYearEndDiscRate = 0;

        double NotionalPL = 0, NetPrice = 0, NetPriceSeam = 0, pBNetPriceUnit = 0, pANetPriceUnit = 0, pBNetSeamPrice = 0, pANetSeamPrice = 0, pKgDiff = 0, pMtrDiff = 0;
        double pBChmTRT = 0, pAChmTRT = 0;
        
        double thWeight = data.getDoubleValueFromDB("SELECT THEORETICAL_WEIGHT FROM PRODUCTION.FELT_FINISHING_PIECE_TAGGING WHERE PIECE_NO='" + pPieceNo + "'");

        FeltInvCalc inv_calc;
        try {
            inv_calc = clsOrderValueCalc.calculateWithoutGSTINNO(pPieceNo, pBillProdCode, pPartyCode, pBillLength, pBillWidth, pBillWeight, pBillSqmtr, pCurDate);

//            pBRate = inv_calc.getFicRate();
            pBRate = inv_calc.getFicGrossRate();
            pBDiscRate = pBRate * (inv_calc.getFicDiscPer() / 100);
            pBYearEndDiscRate = pBRate * (inv_calc.getFicYearEndPer() / 100);
            pBChmTRT = data.getDoubleValueFromDB("SELECT CHEM_TRT_CHRG FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE='" + pBillProdCode + "' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM<='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO>='" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO='0000-00-00') ");
            //pBNetPriceUnit = EITLERPGLOBAL.round((pBRate - pBDiscRate - pBYearEndDiscRate), 2);
            pBNetPriceUnit = EITLERPGLOBAL.round((pBRate - pBDiscRate - pBYearEndDiscRate) + pBChmTRT, 2);
            pBNetSeamPrice = inv_calc.getFicNetSeamUnit();
            pBillUnit = data.getStringValueFromDB("SELECT CASE WHEN SQM_IND=1 THEN 'MTR' ELSE 'KG' END AS UNIT FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE='" + pBillProdCode + "' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM<='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO>='" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO='0000-00-00') ");

            inv_calc = clsOrderValueCalc.calculateWithoutGSTINNO(pPieceNo, pActualProdCode, pPartyCode, pBillLength, pBillWidth, pBillWeight, pBillSqmtr, pCurDate);

//            pARate = inv_calc.getFicRate();
            pARate = inv_calc.getFicGrossRate();
            pADiscRate = pARate * (inv_calc.getFicDiscPer() / 100);
            pAYearEndDiscRate = pARate * (inv_calc.getFicYearEndPer() / 100);
            pAChmTRT = data.getDoubleValueFromDB("SELECT CHEM_TRT_CHRG FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE='" + pActualProdCode + "' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM<='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO>='" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO='0000-00-00') ");
            //pANetPriceUnit = EITLERPGLOBAL.round((pARate - pADiscRate - pAYearEndDiscRate), 2);
            pANetPriceUnit = EITLERPGLOBAL.round((pARate - pADiscRate - pAYearEndDiscRate) + pAChmTRT, 2);
            pANetSeamPrice = inv_calc.getFicNetSeamUnit();
            pActualUnit = data.getStringValueFromDB("SELECT CASE WHEN SQM_IND=1 THEN 'MTR' ELSE 'KG' END AS UNIT FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE='" + pActualProdCode + "' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM<='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO>='" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO='0000-00-00') ");

            pKgDiff = EITLERPGLOBAL.round(pBillWeight - pActualWeight, 2);
            pMtrDiff = EITLERPGLOBAL.round(pBillSqmtr - pActualSqmtr, 2);

            NetPrice = pBNetPriceUnit;
            NetPriceSeam = EITLERPGLOBAL.round(pBNetSeamPrice, 2);
            if (pBillUnit.equals("MTR")) {
                NotionalPL = EITLERPGLOBAL.round((pMtrDiff * pBNetPriceUnit) + (pBillWidth * pBNetSeamPrice), 2);
            }
            if (pBillUnit.equals("KG")) {
                NotionalPL = EITLERPGLOBAL.round((pKgDiff * pBNetPriceUnit) + (pBillWidth * pBNetSeamPrice), 2);
            }

            data.Execute("UPDATE PRODUCTION.FELT_FINISHING_PIECE_TAGGING SET WEIGHT_DIFFERENCE="+EITLERPGLOBAL.round(pKgDiff, 2)+", WEIGHT_DIFFERENCE_PER="+EITLERPGLOBAL.round((pKgDiff*100)/thWeight, 2)+", UNIT_NET_PRICE="+EITLERPGLOBAL.round(NetPrice, 2)+", UNIT_NET_SEAM="+EITLERPGLOBAL.round(NetPriceSeam, 2)+", NOTIONAL_PL_AMT="+EITLERPGLOBAL.round(NotionalPL, 2)+" WHERE PIECE_NO='" + pPieceNo + "' ");
        } catch (Exception e) {
//            System.out.println("Erro on Calculation : " + e.getMessage());
        }

    }

}
