/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.SampleOrder;

import EITLERP.ComboData;
import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.common.JavaMail;
import EITLERP.Production.clsFeltProductionApprovalFlow;
import EITLERP.Variant;
import EITLERP.clsFirstFree;
import EITLERP.data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 *
 * @author DAXESH PRAJAPATI
 *
 */
public class clsFeltSampleOrder {

    public String LastError = "";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet, rsHeader;
    //private ResultSet rsResultSet,

    public HashMap props;
    public boolean Ready = false;
    //Felt Order Collection
    public HashMap hmFeltSalesOrderDetails;
    public HashMap hmFeltRejectionDetails;

    //History Related properties
    public boolean HistoryView = false;
    private String historyAmendDate = "";
    private String historyAmendID = "";
    private static int ModuleID = 861;

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
    public clsFeltSampleOrder() {
        LastError = "";
        props = new HashMap();
        props.put("DOC_NO", new Variant(""));
        props.put("DOC_DATE", new Variant(""));
        props.put("REGION", new Variant(""));
        props.put("SALES_ENGINEER", new Variant(""));
        props.put("PARTY_CODE", new Variant(""));
        props.put("PARTY_NAME", new Variant(""));
        props.put("REFERENCE", new Variant(""));
        props.put("REFERENCE_DATE", new Variant(""));
        props.put("P_O_NO", new Variant(""));
        props.put("P_O_DATE", new Variant(""));
        props.put("REMARK", new Variant(""));
        props.put("OLD_PIECE", new Variant(false));
        props.put("TENDER_PARTY", new Variant(false));

        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("PREFIX", new Variant(""));

        props.put("DOC_NO", new Variant(""));
        props.put("DOC_DATE", new Variant(""));
        props.put("MODULE_ID", new Variant(""));
        props.put("USER_ID", new Variant(""));
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

        props.put("CGST_PER", new Variant(0));
        props.put("CGST_AMT", new Variant(0));
        props.put("SGST_PER", new Variant(0));
        props.put("SGST_AMT", new Variant(0));
        props.put("IGST_PER", new Variant(0));
        props.put("IGST_AMT", new Variant(0));
        props.put("COMPOSITION_PER", new Variant(0));
        props.put("COMPOSITION_AMT", new Variant(0));
        props.put("RCM_PER", new Variant(0));
        props.put("RCM_AMT", new Variant(0));
        props.put("GST_COMPENSATION_CESS_PER", new Variant(0));
        props.put("GST_COMPENSATION_CESS_AMT", new Variant(0));

        hmFeltSalesOrderDetails = new HashMap();
        hmFeltRejectionDetails = new HashMap();
    }

    public boolean LoadData() {
        Ready = false;
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_SAMPLE_ORDER_HEADER ORDER BY DOC_NO");
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
//            if(HistoryView) setHistoryData(historyAmendDate, historyAmendID);
//            else 

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
//            if(HistoryView) setHistoryData(historyAmendDate, historyAmendID);
//            else 

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
//            if(HistoryView) setHistoryData(historyAmendDate, historyAmendID);
//            else 

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
//            if(HistoryView) setHistoryData(historyAmendDate, historyAmendID);
//            else 

            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    public boolean Insert() {
        ResultSet resultSetTemp, resultSetHistory, rsHeader, rsHeaderH, resultSetRejection;
        Statement statementTemp, statementHistory, stHeader, stHeaderH, statementRejection;
        try {
            // Felt Order Updation data connection
            connection = data.getConn();
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_SAMPLE_ORDER_DETAIL WHERE DOC_NO='1'");

            statementRejection = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetRejection = statementRejection.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_SAMPLE_ORDER_REJECTION_SCRAP_DETAIL WHERE R_DOC_NO='1'");

            // Felt order Updation data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_SAMPLE_ORDER_DETAIL_H WHERE DOC_NO='1'");

            stHeader = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHeader = stHeader.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_SAMPLE_ORDER_HEADER WHERE DOC_NO='1'");

            stHeaderH = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHeaderH = stHeaderH.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_SAMPLE_ORDER_HEADER_H WHERE DOC_NO='1'");

            //setAttribute("DOC_NO",);
            rsHeader.first();
            rsHeader.moveToInsertRow();

            rsHeader.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            rsHeader.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));

            rsHeader.updateString("REMARK", getAttribute("REMARK").getString());

            rsHeader.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsHeader.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());

            rsHeader.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeader.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsHeader.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            rsHeader.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeader.updateInt("MODIFIED_BY", 0);
            rsHeader.updateString("MODIFIED_DATE", "0000-00-00");
            rsHeader.updateBoolean("APPROVED", false);
            rsHeader.updateString("APPROVED_DATE", "0000-00-00");
            rsHeader.updateBoolean("CANCELED", false);
            rsHeader.updateBoolean("REJECTED", false);
            rsHeader.updateString("REJECTED_DATE", "0000-00-00");
            rsHeader.updateBoolean("CHANGED", false);
            rsHeader.updateString("CHANGED_DATE", EITLERPGLOBAL.formatDateDB("0000-00-00"));

            rsHeader.insertRow();

            rsHeaderH.first();
            rsHeaderH.moveToInsertRow();

            rsHeaderH.updateInt("REVISION_NO", 1);

            rsHeaderH.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsHeaderH.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            rsHeaderH.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
            rsHeaderH.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            rsHeaderH.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));

            rsHeaderH.updateString("REMARK", getAttribute("REMARK").getString());

            rsHeaderH.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsHeaderH.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());

            rsHeaderH.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            rsHeaderH.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateString("UPDATED_BY", "0");
            rsHeaderH.updateString("UPDATED_DATE", EITLERPGLOBAL.getCurrentDateTimeDB());
            rsHeaderH.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsHeaderH.updateString("UPDATED_BY", EITLERPGLOBAL.gNewUserID + "");
            rsHeaderH.updateString("UPDATED_DATE", EITLERPGLOBAL.getCurrentDateTimeDB());
            rsHeaderH.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            rsHeaderH.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateInt("MODIFIED_BY", 0);
            rsHeaderH.updateString("MODIFIED_DATE", "0000-00-00");
            rsHeaderH.updateBoolean("APPROVED", false);
            rsHeaderH.updateString("APPROVED_DATE", "0000-00-00");
            rsHeaderH.updateBoolean("CANCELED", false);
            rsHeaderH.updateBoolean("REJECTED", false);
            rsHeaderH.updateString("REJECTED_DATE", "0000-00-00");
            rsHeaderH.updateBoolean("CHANGED", false);
            rsHeaderH.updateString("CHANGED_DATE", "0000-00-00");

            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");

            rsHeaderH.updateString("FROM_IP", "" + str_split[1]);

            rsHeaderH.insertRow();

            //Now Insert records into FELT_CN_TEMP_HEADER & History tables
            for (int i = 1; i <= hmFeltSalesOrderDetails.size(); i++) {
                clsFeltSampleOrderDetails ObjFeltSalesOrderDetails = (clsFeltSampleOrderDetails) hmFeltSalesOrderDetails.get(Integer.toString(i));

                //Insert records into Felt order Amendment table
                resultSetTemp.moveToInsertRow();

                resultSetTemp.updateInt("SR_NO", i);
                resultSetTemp.updateString("DOC_NO", ObjFeltSalesOrderDetails.getAttribute("DOC_NO").getString());
                resultSetTemp.updateString("MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                resultSetTemp.updateString("POSITION", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                resultSetTemp.updateString("POSITION_DESC", ObjFeltSalesOrderDetails.getAttribute("POSITION_DESC").getString());
                resultSetTemp.updateString("PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                resultSetTemp.updateString("LAYER_TYPE", ObjFeltSalesOrderDetails.getAttribute("LAYER_TYPE").getString());
                //LAYER_TYPE
                resultSetTemp.updateString("PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                resultSetTemp.updateString("PRODUCT_DESC", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_DESC").getString());
                resultSetTemp.updateString("S_GROUP", ObjFeltSalesOrderDetails.getAttribute("S_GROUP").getString());
                resultSetTemp.updateString("LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                resultSetTemp.updateString("WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                resultSetTemp.updateString("GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                resultSetTemp.updateString("THORITICAL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("THORITICAL_WIDTH").getString());
                resultSetTemp.updateString("SQ_MTR", ObjFeltSalesOrderDetails.getAttribute("SQ_MTR").getString());
                resultSetTemp.updateString("STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                resultSetTemp.updateString("REQ_MONTH", ObjFeltSalesOrderDetails.getAttribute("REQ_MONTH").getString());
                resultSetTemp.updateString("SYN_PER", ObjFeltSalesOrderDetails.getAttribute("SYN_PER").getString());
                resultSetTemp.updateString("REMARK", ObjFeltSalesOrderDetails.getAttribute("REMARK").getString());

                resultSetTemp.insertRow();

                //Insert records into Felt Order Amendment History Table
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO", 1);
                resultSetHistory.updateInt("SR_NO", i);
                resultSetHistory.updateString("DOC_NO", ObjFeltSalesOrderDetails.getAttribute("DOC_NO").getString());
                resultSetHistory.updateString("MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                resultSetHistory.updateString("POSITION", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                resultSetHistory.updateString("POSITION_DESC", ObjFeltSalesOrderDetails.getAttribute("POSITION_DESC").getString());
                resultSetHistory.updateString("PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                resultSetHistory.updateString("LAYER_TYPE", ObjFeltSalesOrderDetails.getAttribute("LAYER_TYPE").getString());
                resultSetHistory.updateString("PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                resultSetHistory.updateString("PRODUCT_DESC", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_DESC").getString());
                resultSetHistory.updateString("S_GROUP", ObjFeltSalesOrderDetails.getAttribute("S_GROUP").getString());
                resultSetHistory.updateString("LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                resultSetHistory.updateString("WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                resultSetHistory.updateString("GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                resultSetHistory.updateString("THORITICAL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("THORITICAL_WIDTH").getString());
                resultSetHistory.updateString("SQ_MTR", ObjFeltSalesOrderDetails.getAttribute("SQ_MTR").getString());
                resultSetHistory.updateString("STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                resultSetHistory.updateString("REQ_MONTH", ObjFeltSalesOrderDetails.getAttribute("REQ_MONTH").getString());
                resultSetHistory.updateString("SYN_PER", ObjFeltSalesOrderDetails.getAttribute("SYN_PER").getString());
                resultSetHistory.updateString("REMARK", ObjFeltSalesOrderDetails.getAttribute("REMARK").getString());

                resultSetHistory.insertRow();

            }

            for (int i = 1; i <= hmFeltRejectionDetails.size(); i++) {
                clsFeltRejectionDetails ObjRejectionDetails = (clsFeltRejectionDetails) hmFeltRejectionDetails.get(Integer.toString(i));

                //Insert records into Felt order Amendment table
                resultSetRejection.moveToInsertRow();

                resultSetRejection.updateInt("SR_NO", i);
                resultSetRejection.updateString("R_DOC_NO", ObjRejectionDetails.getAttribute("R_DOC_NO").getString());
                resultSetRejection.updateString("R_PIECE_NO", ObjRejectionDetails.getAttribute("R_PIECE_NO").getString());
                resultSetRejection.updateString("R_NEW_PIECE_NO", ObjRejectionDetails.getAttribute("R_NEW_PIECE_NO").getString());
                resultSetRejection.updateString("R_MACHINE_NO", ObjRejectionDetails.getAttribute("R_MACHINE_NO").getString());
                resultSetRejection.updateString("R_POSITION", ObjRejectionDetails.getAttribute("R_POSITION").getString());
                resultSetRejection.updateString("R_POSITION_DESC", ObjRejectionDetails.getAttribute("R_POSITION_DESC").getString());
                resultSetRejection.updateString("R_PIECE_STAGE", ObjRejectionDetails.getAttribute("R_PIECE_STAGE").getString());
                resultSetRejection.updateString("R_NEW_PIECE_STAGE", ObjRejectionDetails.getAttribute("R_NEW_PIECE_STAGE").getString());
                //R_NEW_PIECE_STAGE
                resultSetRejection.updateString("R_LAYER_TYPE", ObjRejectionDetails.getAttribute("R_LAYER_TYPE").getString());
                //LAYER_TYPE
                resultSetRejection.updateString("R_PRODUCT_CODE", ObjRejectionDetails.getAttribute("R_PRODUCT_CODE").getString());
                resultSetRejection.updateString("R_PRODUCT_DESC", ObjRejectionDetails.getAttribute("R_PRODUCT_DESC").getString());
                resultSetRejection.updateString("R_S_GROUP", ObjRejectionDetails.getAttribute("R_S_GROUP").getString());
                resultSetRejection.updateString("R_LENGTH", ObjRejectionDetails.getAttribute("R_LENGTH").getString());
                resultSetRejection.updateString("R_WIDTH", ObjRejectionDetails.getAttribute("R_WIDTH").getString());
                resultSetRejection.updateString("R_GSM", ObjRejectionDetails.getAttribute("R_GSM").getString());
                resultSetRejection.updateString("R_WEIGHT", ObjRejectionDetails.getAttribute("R_WEIGHT").getString());
                resultSetRejection.updateString("R_SQ_MTR", ObjRejectionDetails.getAttribute("R_SQ_MTR").getString());
                resultSetRejection.updateString("R_STYLE", ObjRejectionDetails.getAttribute("R_STYLE").getString());
                resultSetRejection.updateString("R_REQ_MONTH", ObjRejectionDetails.getAttribute("R_REQ_MONTH").getString());
                resultSetRejection.updateString("R_SYN_PER", ObjRejectionDetails.getAttribute("R_SYN_PER").getString());
                resultSetRejection.updateString("R_REMARK", ObjRejectionDetails.getAttribute("R_REMARK").getString());

                resultSetRejection.insertRow();

            }

            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = ModuleID; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("DOC_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.FELT_SALES_SAMPLE_ORDER_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator = true;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("REJECTED_REMARKS").getString();
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

        ResultSet resultSetTemp, resultSetHistory, rsHeader, rsHeaderH, rsRegister, resultSetRejection;
        Statement statementTemp, statementHistory, stHeader, stHeaderH, stRegister, statementRejection;
        int revisionNo;
        try {
            // Production data connection

            // Production data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //Get the Maximum Revision No in History Table.
            resultSetHistory = statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_SALES_SAMPLE_ORDER_DETAIL_H WHERE DOC_NO='1'");
            resultSetHistory.first();
            if (resultSetHistory.getRow() > 0) {
                revisionNo = resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from production data table before insert
            //statementHistory.execute("DELETE FROM PRODUCTION.FELT_SALES_SAMPLE_ORDER_DETAIL WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"'");

            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_SAMPLE_ORDER_DETAIL_H WHERE DOC_NO=''");

            stHeaderH = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHeaderH = stHeaderH.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_SAMPLE_ORDER_HEADER_H WHERE DOC_NO=''");

            //Now Update records into FELT_CN_TEMP_HEADER tables
            resultSet.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSet.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB((String) getAttribute("DOC_DATE").getObj()));
            resultSet.updateString("REMARK", (String) getAttribute("REMARK").getObj());
            resultSet.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            resultSet.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());

            resultSet.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            resultSet.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
            resultSet.updateString("MODIFIED_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));

            if (getAttribute("APPROVAL_STATUS").getString().equals("F")) {
                resultSet.updateBoolean("APPROVED", true);
                resultSet.updateString("APPROVED_BY", EITLERPGLOBAL.gNewUserID + "");
                resultSet.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            } else {
                resultSet.updateBoolean("APPROVED", false);
                resultSet.updateString("APPROVED_DATE", "0000-00-00");
            }
            resultSet.updateBoolean("CANCELED", false);
            resultSet.updateBoolean("REJECTED", false);
            resultSet.updateString("REJECTED_DATE", "0000-00-00");
            //resultSet.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            resultSet.updateBoolean("CHANGED", true);
            resultSet.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            try {
                resultSet.updateRow();
            } catch (Exception e) {
                System.out.println("Header Updation Failed : " + e.getMessage());
            }
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_SALES_SAMPLE_ORDER_HEADER_H WHERE DOC_NO='" + getAttribute("DOC_NO").getString() + "'");

            RevNo++;

            rsHeaderH.moveToInsertRow();

            rsHeaderH.updateString("REVISION_NO", RevNo + "");
            rsHeaderH.updateString("DOC_NO", getAttribute("DOC_NO").getString() + "");
            rsHeaderH.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsHeaderH.updateString("REMARK", getAttribute("REMARK").getString());

            rsHeaderH.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsHeaderH.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());

            rsHeaderH.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            rsHeaderH.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
            rsHeaderH.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateString("UPDATED_BY", EITLERPGLOBAL.gNewUserID + "");
            rsHeaderH.updateString("UPDATED_DATE", EITLERPGLOBAL.getCurrentDateTimeDB());
            rsHeaderH.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsHeaderH.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsHeaderH.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            rsHeaderH.updateString("APPROVER_REMARKS", getAttribute("FROM_REMARKS").getString());
            if (getAttribute("APPROVAL_STATUS").getString().equals("F")) {
                rsHeaderH.updateBoolean("APPROVED", true);
                rsHeaderH.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            } else {
                rsHeaderH.updateBoolean("APPROVED", false);
                rsHeaderH.updateString("APPROVED_DATE", "0000-00-00");
            }
            rsHeaderH.updateBoolean("CANCELED", false);
            rsHeaderH.updateBoolean("REJECTED", false);
            rsHeaderH.updateString("REJECTED_DATE", "0000-00-00");
            rsHeaderH.updateBoolean("CHANGED", true);
            rsHeaderH.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");
            rsHeaderH.updateString("FROM_IP", "" + str_split[1]);

            rsHeaderH.insertRow();
            String OrderNo = getAttribute("DOC_NO").getString();
            data.Execute("DELETE FROM PRODUCTION.FELT_SALES_SAMPLE_ORDER_DETAIL WHERE DOC_NO='" + OrderNo + "'");

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_SAMPLE_ORDER_DETAIL WHERE DOC_NO='1'");

            for (int i = 1; i <= hmFeltSalesOrderDetails.size(); i++) {
                clsFeltSampleOrderDetails ObjFeltSalesOrderDetails = (clsFeltSampleOrderDetails) hmFeltSalesOrderDetails.get(Integer.toString(i));

                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateInt("SR_NO", i);
                resultSetTemp.updateString("DOC_NO", ObjFeltSalesOrderDetails.getAttribute("DOC_NO").getString() + "");
                resultSetTemp.updateString("MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                resultSetTemp.updateString("POSITION", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                resultSetTemp.updateString("POSITION_DESC", ObjFeltSalesOrderDetails.getAttribute("POSITION_DESC").getString());
                resultSetTemp.updateString("PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                resultSetTemp.updateString("LAYER_TYPE", ObjFeltSalesOrderDetails.getAttribute("LAYER_TYPE").getString());
                resultSetTemp.updateString("PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                resultSetTemp.updateString("PRODUCT_DESC", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_DESC").getString());
                resultSetTemp.updateString("S_GROUP", ObjFeltSalesOrderDetails.getAttribute("S_GROUP").getString());
                resultSetTemp.updateString("LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                resultSetTemp.updateString("WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                resultSetTemp.updateString("GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                resultSetTemp.updateString("THORITICAL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("THORITICAL_WIDTH").getString());
                resultSetTemp.updateString("SQ_MTR", ObjFeltSalesOrderDetails.getAttribute("SQ_MTR").getString());
                resultSetTemp.updateString("STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                resultSetTemp.updateString("REQ_MONTH", ObjFeltSalesOrderDetails.getAttribute("REQ_MONTH").getString());
                resultSetTemp.updateString("SYN_PER", ObjFeltSalesOrderDetails.getAttribute("SYN_PER").getString());
                resultSetTemp.updateString("REMARK", ObjFeltSalesOrderDetails.getAttribute("REMARK").getString());
                resultSetTemp.insertRow();

                //int RevNoH=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_SALES_SAMPLE_ORDER_DETAIL_H WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
                //RevNoH++;
                resultSetHistory.moveToInsertRow();

                resultSetHistory.updateInt("REVISION_NO", RevNo);
                resultSetHistory.updateInt("SR_NO", i);
                resultSetHistory.updateString("DOC_NO", ObjFeltSalesOrderDetails.getAttribute("DOC_NO").getString() + "");
                resultSetHistory.updateString("MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                resultSetHistory.updateString("POSITION", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                resultSetHistory.updateString("POSITION_DESC", ObjFeltSalesOrderDetails.getAttribute("POSITION_DESC").getString());
                resultSetHistory.updateString("PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                resultSetHistory.updateString("LAYER_TYPE", ObjFeltSalesOrderDetails.getAttribute("LAYER_TYPE").getString());
                resultSetHistory.updateString("PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                resultSetHistory.updateString("PRODUCT_DESC", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_DESC").getString());
                resultSetHistory.updateString("S_GROUP", ObjFeltSalesOrderDetails.getAttribute("S_GROUP").getString());
                resultSetHistory.updateString("LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                resultSetHistory.updateString("WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                resultSetHistory.updateString("GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                resultSetHistory.updateString("THORITICAL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("THORITICAL_WIDTH").getString());
                resultSetHistory.updateString("SQ_MTR", ObjFeltSalesOrderDetails.getAttribute("SQ_MTR").getString());
                resultSetHistory.updateString("STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                resultSetHistory.updateString("REQ_MONTH", ObjFeltSalesOrderDetails.getAttribute("REQ_MONTH").getString());
                resultSetHistory.updateString("SYN_PER", ObjFeltSalesOrderDetails.getAttribute("SYN_PER").getString());
                resultSetHistory.updateString("REMARK", ObjFeltSalesOrderDetails.getAttribute("REMARK").getString());
                resultSetHistory.insertRow();

                // Final Approval and save to PIECE REGISTER 
                if (getAttribute("APPROVAL_STATUS").getString().equals("F") && !ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString().equals("")) {

                    try {

                        stRegister = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                        rsRegister = stRegister.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO=''");

                        rsRegister.moveToInsertRow();

                        rsRegister.updateString("PR_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                        rsRegister.updateString("PR_DATE", EITLERPGLOBAL.getCurrentDateDB());
                        rsRegister.updateString("PR_ORDER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                        rsRegister.updateString("PR_DOC_NO", ObjFeltSalesOrderDetails.getAttribute("DOC_NO").getString());
                        rsRegister.updateString("PR_MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                        rsRegister.updateString("PR_POSITION_NO", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                        rsRegister.updateString("PR_PARTY_CODE", getAttribute("PARTY_CODE").getString());
                        rsRegister.updateString("PR_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                        rsRegister.updateString("PR_UPN", getAttribute("PARTY_CODE").getString() + ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString() + "0000");
                        rsRegister.updateString("PR_GROUP", ObjFeltSalesOrderDetails.getAttribute("S_GROUP").getString());
                        rsRegister.updateString("PR_STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                        rsRegister.updateString("PR_LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                        rsRegister.updateString("PR_WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                        rsRegister.updateString("PR_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                        rsRegister.updateString("PR_THORITICAL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("THORITICAL_WIDTH").getString());
                        rsRegister.updateString("PR_SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQ_MTR").getString());
                        rsRegister.updateString("PR_SYN_PER", ObjFeltSalesOrderDetails.getAttribute("SYN_PER").getString());
                        rsRegister.updateString("PR_REQUESTED_MONTH", ObjFeltSalesOrderDetails.getAttribute("REQ_MONTH").getString());
                        rsRegister.updateString("PR_REGION", "");
                        rsRegister.updateString("PR_INCHARGE", "");
                        rsRegister.updateString("PR_REFERENCE", "");
                        rsRegister.updateString("PR_REFERENCE_DATE", "");
                        rsRegister.updateString("PR_PO_NO", "");
                        rsRegister.updateString("PR_PO_DATE", "");
                        rsRegister.updateString("PR_ORDER_REMARK", "SAMPLE PIECE");
                        rsRegister.updateString("PR_PIECE_REMARK", ObjFeltSalesOrderDetails.getAttribute("REMARK").getString());

                        //rsRegister.updateString("PR_PIECE_STAGE","WEAVING");
                        rsRegister.updateString("PR_PIECE_STAGE", "PLANNING");
                        rsRegister.updateString("PR_WIP_STATUS", "CONFIRMED");
                        rsRegister.updateString("PR_OC_MONTHYEAR", ObjFeltSalesOrderDetails.getAttribute("REQ_MONTH").getString());

                        rsRegister.updateString("PR_RATE_INDICATOR", "NEW");

                        if (ObjFeltSalesOrderDetails.getAttribute("LAYER_TYPE").getString().equals("WITHOUT_AB")) {
                            rsRegister.updateString("PR_PRIORITY_HOLD_CAN_FLAG", "0");
                            rsRegister.updateString("PR_PIECE_AB_FLAG", "");
                        } else if (ObjFeltSalesOrderDetails.getAttribute("LAYER_TYPE").getString().equals("WITH_AB")) {
                            rsRegister.updateString("PR_PRIORITY_HOLD_CAN_FLAG", "0");
                            rsRegister.updateString("PR_PIECE_AB_FLAG", "AB");
                        }
                        //
                        rsRegister.updateString("PR_DIVERSION_FLAG", "CLOSED");

                        rsRegister.updateString("PR_BILL_LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                        rsRegister.updateString("PR_BILL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                        rsRegister.updateString("PR_BILL_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                        rsRegister.updateString("PR_BILL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("THORITICAL_WIDTH").getString());
                        rsRegister.updateString("PR_BILL_SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQ_MTR").getString());
                        rsRegister.updateString("PR_BILL_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                        rsRegister.updateString("PR_BILL_STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                        //        rsRegister.updateString("PR_DATE_SLOT",ObjFeltSalesOrderDetails.getAttribute("DATE_SLOT").getString());
                        rsRegister.updateString("PR_TENDER_WEIGHT", "");
                        rsRegister.updateString("PR_TENDER_GSM", "");

                        rsRegister.updateString("PR_CONTACT_PERSON", "");
                        rsRegister.updateString("PR_EMAIL_ID", "");
                        rsRegister.updateString("PR_PHONE_NO", "");

                        rsRegister.updateString("PR_WARP_DATE", "0000-00-00");
                        rsRegister.updateString("PR_WVG_DATE", "0000-00-00");
                        rsRegister.updateString("PR_MND_DATE", "0000-00-00");
                        rsRegister.updateString("PR_NDL_DATE", "0000-00-00");
                        rsRegister.updateString("PR_FNSG_DATE", "0000-00-00");
                        rsRegister.updateString("PR_RCV_DATE", "0000-00-00");
                        rsRegister.updateString("PR_PACKED_DATE", "0000-00-00");
                        rsRegister.updateString("PR_EXP_DISPATCH_DATE", "0000-00-00");
                        rsRegister.updateString("PR_INVOICE_DATE", "0000-00-00");
                        rsRegister.updateString("PR_LR_DATE", "0000-00-00");
                        rsRegister.updateString("PR_HOLD_DATE", "0000-00-00");
                        rsRegister.updateString("PR_RELEASE_DATE", "0000-00-00");

                        rsRegister.updateString("PR_PIECETRIAL_CATEGORY", "SAMPLE ORDER");
                        rsRegister.updateString("PR_PIECETRIAL_FLAG", "1");
//                              

                        rsRegister.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
                        rsRegister.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
                        rsRegister.updateString("MODIFIED_BY", EITLERPGLOBAL.gNewUserID + "");
                        rsRegister.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                        rsRegister.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                        rsRegister.updateString("APPROVER_BY", EITLERPGLOBAL.gNewUserID + "");
                        rsRegister.updateString("APPROVER_DATE", EITLERPGLOBAL.getCurrentDateDB());
                        rsRegister.updateString("APPROVER_REMARK", getAttribute("APPROVER_REMARKS").getString());

                        rsRegister.insertRow();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        if (ObjFeltSalesOrderDetails.getAttribute("LAYER_TYPE").getString().equals("WITHOUT_AB")) {
                            stRegister = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                            rsRegister = stRegister.executeQuery("SELECT * FROM PRODUCTION.FELT_WIP_PIECE_REGISTER where WIP_PIECE_NO=''");

                            rsRegister.moveToInsertRow();

                            rsRegister.updateString("WIP_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                            rsRegister.updateString("WIP_EXT_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                            rsRegister.updateString("WIP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                            rsRegister.updateString("WIP_ORDER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                            rsRegister.updateString("WIP_DOC_NO", ObjFeltSalesOrderDetails.getAttribute("DOC_NO").getString());
                            rsRegister.updateString("WIP_UPN", getAttribute("PARTY_CODE").getString() + ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString() + "0000");
                            rsRegister.updateString("WIP_MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                            rsRegister.updateString("WIP_POSITION_NO", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                            rsRegister.updateString("WIP_PARTY_CODE", getAttribute("PARTY_CODE").getString());
                            rsRegister.updateString("WIP_GROUP", ObjFeltSalesOrderDetails.getAttribute("S_GROUP").getString());
                            rsRegister.updateString("WIP_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                            rsRegister.updateString("WIP_BILL_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                            rsRegister.updateString("WIP_STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                            rsRegister.updateString("WIP_BILL_STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                            rsRegister.updateString("WIP_LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                            rsRegister.updateString("WIP_ACTUAL_LENGTH", "");
                            rsRegister.updateString("WIP_BILL_LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                            rsRegister.updateString("WIP_WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                            rsRegister.updateString("WIP_ACTUAL_WIDTH", "");
                            rsRegister.updateString("WIP_BILL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                            rsRegister.updateString("WIP_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                            rsRegister.updateString("WIP_TENDER_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                            rsRegister.updateString("WIP_BILL_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                            rsRegister.updateString("WIP_THORITICAL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("THORITICAL_WIDTH").getString());
                            rsRegister.updateString("WIP_ACTUAL_WEIGHT", "");
                            rsRegister.updateString("WIP_TENDER_WEIGHT", "");
                            rsRegister.updateString("WIP_BILL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("THORITICAL_WIDTH").getString());
                            rsRegister.updateString("WIP_SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQ_MTR").getString());
                            rsRegister.updateString("WIP_BILL_SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQ_MTR").getString());

                            rsRegister.updateString("WIP_PRIORITY_HOLD_CAN_FLAG", "0");

                            rsRegister.updateString("WIP_PARTY_CODE_ORIGINAL", "");
                            rsRegister.updateString("WIP_PIECE_NO_ORIGINAL", "");
                            rsRegister.updateString("WIP_SYN_PER", ObjFeltSalesOrderDetails.getAttribute("SYN_PER").getString());
                            rsRegister.updateString("WIP_REQUESTED_MONTH", ObjFeltSalesOrderDetails.getAttribute("REQ_MONTH").getString());
                            rsRegister.updateString("WIP_REGION", "");
                            rsRegister.updateString("WIP_INCHARGE", "");
                            rsRegister.updateString("WIP_REFERENCE", "");
                            rsRegister.updateString("WIP_REFERENCE_DATE", "");
                            rsRegister.updateString("WIP_PO_NO", "");
                            rsRegister.updateString("WIP_PO_DATE", "");
                            rsRegister.updateString("WIP_ORDER_REMARK", "Sample Order");
                            rsRegister.updateString("WIP_PIECE_REMARK", "Sample Order");

                            //rsRegister.updateString("","");
                            rsRegister.updateString("WIP_FELT_VALUE_WITH_GST", "");
                            rsRegister.updateString("WIP_FELT_VALUE_WITHOUT_GST", "");
                            rsRegister.updateString("WIP_FELT_BASE_VALUE", "");
                            rsRegister.updateString("WIP_DAYS_ORDER_WARPED", "");
                            rsRegister.updateString("WIP_DIVERSION_FLAG", "");
                            rsRegister.updateString("WIP_DIVERSION_REASON", "");
                            rsRegister.updateString("WIP_DIVERTED_FLAG", "CLOSED");
                            rsRegister.updateString("WIP_DIVERTED_REASON", "");
                            rsRegister.updateString("WIP_OA_NO", "");
                            rsRegister.updateString("WIP_OA_DATE", "");
                            rsRegister.updateString("WIP_OC_NO", "");
                            rsRegister.updateString("WIP_OC_MONTHYEAR", "");
                            rsRegister.updateString("WIP_CURRENT_SCH_MONTH", "");

                            rsRegister.updateString("WIP_PIECE_STAGE", "PLANNING");
                            rsRegister.updateString("WIP_STATUS", "CONFIRMED");

                            rsRegister.insertRow();

                        } else if (ObjFeltSalesOrderDetails.getAttribute("LAYER_TYPE").getString().equals("WITH_AB")) {

                            //1st PIECE DETAIL
                            stRegister = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                            rsRegister = stRegister.executeQuery("SELECT * FROM PRODUCTION.FELT_WIP_PIECE_REGISTER where WIP_PIECE_NO=''");

                            rsRegister.moveToInsertRow();

                            rsRegister.updateString("WIP_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                            rsRegister.updateString("WIP_EXT_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString() + "-A");
                            rsRegister.updateString("WIP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                            rsRegister.updateString("WIP_ORDER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                            rsRegister.updateString("WIP_DOC_NO", ObjFeltSalesOrderDetails.getAttribute("DOC_NO").getString());
                            rsRegister.updateString("WIP_UPN", getAttribute("PARTY_CODE").getString() + ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString() + "0000");
                            rsRegister.updateString("WIP_MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                            rsRegister.updateString("WIP_POSITION_NO", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                            rsRegister.updateString("WIP_PARTY_CODE", getAttribute("PARTY_CODE").getString());
                            rsRegister.updateString("WIP_GROUP", ObjFeltSalesOrderDetails.getAttribute("S_GROUP").getString());
                            rsRegister.updateString("WIP_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                            rsRegister.updateString("WIP_BILL_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                            rsRegister.updateString("WIP_STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                            rsRegister.updateString("WIP_BILL_STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                            rsRegister.updateString("WIP_LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                            rsRegister.updateString("WIP_ACTUAL_LENGTH", "");
                            rsRegister.updateString("WIP_BILL_LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                            rsRegister.updateString("WIP_WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                            rsRegister.updateString("WIP_ACTUAL_WIDTH", "");
                            rsRegister.updateString("WIP_BILL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                            rsRegister.updateString("WIP_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                            rsRegister.updateString("WIP_TENDER_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                            rsRegister.updateString("WIP_BILL_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                            rsRegister.updateString("WIP_THORITICAL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("THORITICAL_WIDTH").getString());
                            rsRegister.updateString("WIP_ACTUAL_WEIGHT", "");
                            rsRegister.updateString("WIP_TENDER_WEIGHT", "");
                            rsRegister.updateString("WIP_BILL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("THORITICAL_WIDTH").getString());
                            rsRegister.updateString("WIP_SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQ_MTR").getString());
                            rsRegister.updateString("WIP_BILL_SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQ_MTR").getString());

                            rsRegister.updateString("WIP_PRIORITY_HOLD_CAN_FLAG", "0");
                            rsRegister.updateString("WIP_PIECE_AB_FLAG", "AB");
                            //

                            rsRegister.updateString("WIP_PARTY_CODE_ORIGINAL", "");
                            rsRegister.updateString("WIP_PIECE_NO_ORIGINAL", "");
                            rsRegister.updateString("WIP_SYN_PER", ObjFeltSalesOrderDetails.getAttribute("SYN_PER").getString());
                            rsRegister.updateString("WIP_REQUESTED_MONTH", ObjFeltSalesOrderDetails.getAttribute("REQ_MONTH").getString());
                            rsRegister.updateString("WIP_REGION", "");
                            rsRegister.updateString("WIP_INCHARGE", "");
                            rsRegister.updateString("WIP_REFERENCE", "");
                            rsRegister.updateString("WIP_REFERENCE_DATE", "");
                            rsRegister.updateString("WIP_PO_NO", "");
                            rsRegister.updateString("WIP_PO_DATE", "");
                            rsRegister.updateString("WIP_ORDER_REMARK", "Sample Order");
                            rsRegister.updateString("WIP_PIECE_REMARK", "Sample Order");

                            //rsRegister.updateString("","");
                            rsRegister.updateString("WIP_FELT_VALUE_WITH_GST", "");
                            rsRegister.updateString("WIP_FELT_VALUE_WITHOUT_GST", "");
                            rsRegister.updateString("WIP_FELT_BASE_VALUE", "");
                            rsRegister.updateString("WIP_DAYS_ORDER_WARPED", "");
                            rsRegister.updateString("WIP_DIVERSION_FLAG", "");
                            rsRegister.updateString("WIP_DIVERSION_REASON", "");
                            rsRegister.updateString("WIP_DIVERTED_FLAG", "CLOSED");
                            rsRegister.updateString("WIP_DIVERTED_REASON", "");
                            rsRegister.updateString("WIP_OA_NO", "");
                            rsRegister.updateString("WIP_OA_DATE", "");
                            rsRegister.updateString("WIP_OC_NO", "");
                            rsRegister.updateString("WIP_OC_MONTHYEAR", "");
                            rsRegister.updateString("WIP_CURRENT_SCH_MONTH", "");

                            rsRegister.updateString("WIP_PIECE_STAGE", "PLANNING");
                            rsRegister.updateString("WIP_STATUS", "CONFIRMED");

                            rsRegister.insertRow();

                            //2nd PIECE DETAIL
                            stRegister = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                            rsRegister = stRegister.executeQuery("SELECT * FROM PRODUCTION.FELT_WIP_PIECE_REGISTER where WIP_PIECE_NO=''");

                            rsRegister.moveToInsertRow();

                            rsRegister.updateString("WIP_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                            rsRegister.updateString("WIP_EXT_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString() + "-B");
                            rsRegister.updateString("WIP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                            rsRegister.updateString("WIP_ORDER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                            rsRegister.updateString("WIP_DOC_NO", ObjFeltSalesOrderDetails.getAttribute("DOC_NO").getString());
                            rsRegister.updateString("WIP_UPN", getAttribute("PARTY_CODE").getString() + ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString() + "0000");
                            rsRegister.updateString("WIP_MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                            rsRegister.updateString("WIP_POSITION_NO", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                            rsRegister.updateString("WIP_PARTY_CODE", getAttribute("PARTY_CODE").getString());
                            rsRegister.updateString("WIP_GROUP", ObjFeltSalesOrderDetails.getAttribute("S_GROUP").getString());
                            rsRegister.updateString("WIP_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                            rsRegister.updateString("WIP_BILL_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                            rsRegister.updateString("WIP_STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                            rsRegister.updateString("WIP_BILL_STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                            rsRegister.updateString("WIP_LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                            rsRegister.updateString("WIP_ACTUAL_LENGTH", "");
                            rsRegister.updateString("WIP_BILL_LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                            rsRegister.updateString("WIP_WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                            rsRegister.updateString("WIP_ACTUAL_WIDTH", "");
                            rsRegister.updateString("WIP_BILL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                            rsRegister.updateString("WIP_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                            rsRegister.updateString("WIP_TENDER_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                            rsRegister.updateString("WIP_BILL_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                            rsRegister.updateString("WIP_THORITICAL_WEIGHT", "0");
                            rsRegister.updateString("WIP_ACTUAL_WEIGHT", "");
                            rsRegister.updateString("WIP_TENDER_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("THORITICAL_WIDTH").getString());
                            rsRegister.updateString("WIP_BILL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("THORITICAL_WIDTH").getString());
                            rsRegister.updateString("WIP_SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQ_MTR").getString());
                            rsRegister.updateString("WIP_BILL_SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQ_MTR").getString());

                            rsRegister.updateString("WIP_PRIORITY_HOLD_CAN_FLAG", "0");
                            rsRegister.updateString("WIP_PIECE_AB_FLAG", "AB");

                            rsRegister.updateString("WIP_PARTY_CODE_ORIGINAL", "");
                            rsRegister.updateString("WIP_PIECE_NO_ORIGINAL", "");
                            rsRegister.updateString("WIP_SYN_PER", ObjFeltSalesOrderDetails.getAttribute("SYN_PER").getString());
                            rsRegister.updateString("WIP_REQUESTED_MONTH", ObjFeltSalesOrderDetails.getAttribute("REQ_MONTH").getString());
                            rsRegister.updateString("WIP_REGION", "");
                            rsRegister.updateString("WIP_INCHARGE", "");
                            rsRegister.updateString("WIP_REFERENCE", "");
                            rsRegister.updateString("WIP_REFERENCE_DATE", "");
                            rsRegister.updateString("WIP_PO_NO", "");
                            rsRegister.updateString("WIP_PO_DATE", "");
                            rsRegister.updateString("WIP_ORDER_REMARK", "Sample Order");
                            rsRegister.updateString("WIP_PIECE_REMARK", "Sample Order");

                            //rsRegister.updateString("","");
                            rsRegister.updateString("WIP_FELT_VALUE_WITH_GST", "");
                            rsRegister.updateString("WIP_FELT_VALUE_WITHOUT_GST", "");
                            rsRegister.updateString("WIP_FELT_BASE_VALUE", "");
                            rsRegister.updateString("WIP_DAYS_ORDER_WARPED", "");
                            rsRegister.updateString("WIP_DIVERSION_FLAG", "");
                            rsRegister.updateString("WIP_DIVERSION_REASON", "");
                            rsRegister.updateString("WIP_DIVERTED_FLAG", "CLOSED");
                            rsRegister.updateString("WIP_DIVERTED_REASON", "");
                            rsRegister.updateString("WIP_OA_NO", "");
                            rsRegister.updateString("WIP_OA_DATE", "");
                            rsRegister.updateString("WIP_OC_NO", "");
                            rsRegister.updateString("WIP_OC_MONTHYEAR", "");
                            rsRegister.updateString("WIP_CURRENT_SCH_MONTH", "");

                            rsRegister.updateString("WIP_PIECE_STAGE", "PLANNING");
                            rsRegister.updateString("WIP_STATUS", "CONFIRMED");

                            rsRegister.insertRow();

                            //3rd PIECE DETAIL
                            stRegister = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                            rsRegister = stRegister.executeQuery("SELECT * FROM PRODUCTION.FELT_WIP_PIECE_REGISTER where WIP_PIECE_NO=''");

                            rsRegister.moveToInsertRow();

                            rsRegister.updateString("WIP_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                            rsRegister.updateString("WIP_EXT_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString() + "-AB");
                            rsRegister.updateString("WIP_DATE", EITLERPGLOBAL.getCurrentDateDB());
                            rsRegister.updateString("WIP_ORDER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                            rsRegister.updateString("WIP_DOC_NO", ObjFeltSalesOrderDetails.getAttribute("DOC_NO").getString());
                            rsRegister.updateString("WIP_UPN", getAttribute("PARTY_CODE").getString() + ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString() + "0000");
                            rsRegister.updateString("WIP_MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
                            rsRegister.updateString("WIP_POSITION_NO", ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
                            rsRegister.updateString("WIP_PARTY_CODE", getAttribute("PARTY_CODE").getString());
                            rsRegister.updateString("WIP_GROUP", ObjFeltSalesOrderDetails.getAttribute("S_GROUP").getString());
                            rsRegister.updateString("WIP_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                            rsRegister.updateString("WIP_BILL_PRODUCT_CODE", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
                            rsRegister.updateString("WIP_STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                            rsRegister.updateString("WIP_BILL_STYLE", ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
                            rsRegister.updateString("WIP_LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                            rsRegister.updateString("WIP_ACTUAL_LENGTH", "");
                            rsRegister.updateString("WIP_BILL_LENGTH", ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
                            rsRegister.updateString("WIP_WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                            rsRegister.updateString("WIP_ACTUAL_WIDTH", "");
                            rsRegister.updateString("WIP_BILL_WIDTH", ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
                            rsRegister.updateString("WIP_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                            rsRegister.updateString("WIP_TENDER_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                            rsRegister.updateString("WIP_BILL_GSM", ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
                            rsRegister.updateString("WIP_THORITICAL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("THORITICAL_WIDTH").getString());
                            rsRegister.updateString("WIP_ACTUAL_WEIGHT", "");
                            rsRegister.updateString("WIP_TENDER_WEIGHT", "");
                            rsRegister.updateString("WIP_BILL_WEIGHT", ObjFeltSalesOrderDetails.getAttribute("THORITICAL_WIDTH").getString());
                            rsRegister.updateString("WIP_SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQ_MTR").getString());
                            rsRegister.updateString("WIP_BILL_SQMTR", ObjFeltSalesOrderDetails.getAttribute("SQ_MTR").getString());

                            rsRegister.updateString("WIP_PRIORITY_HOLD_CAN_FLAG", "1");
                            rsRegister.updateString("WIP_PIECE_AB_FLAG", "AB");

                            rsRegister.updateString("WIP_PARTY_CODE_ORIGINAL", "");
                            rsRegister.updateString("WIP_PIECE_NO_ORIGINAL", "");
                            rsRegister.updateString("WIP_SYN_PER", ObjFeltSalesOrderDetails.getAttribute("SYN_PER").getString());
                            rsRegister.updateString("WIP_REQUESTED_MONTH", ObjFeltSalesOrderDetails.getAttribute("REQ_MONTH").getString());
                            rsRegister.updateString("WIP_REGION", "");
                            rsRegister.updateString("WIP_INCHARGE", "");
                            rsRegister.updateString("WIP_REFERENCE", "");
                            rsRegister.updateString("WIP_REFERENCE_DATE", "");
                            rsRegister.updateString("WIP_PO_NO", "");
                            rsRegister.updateString("WIP_PO_DATE", "");
                            rsRegister.updateString("WIP_ORDER_REMARK", "Sample Order");
                            rsRegister.updateString("WIP_PIECE_REMARK", "Sample Order");

                            //rsRegister.updateString("","");
                            rsRegister.updateString("WIP_FELT_VALUE_WITH_GST", "");
                            rsRegister.updateString("WIP_FELT_VALUE_WITHOUT_GST", "");
                            rsRegister.updateString("WIP_FELT_BASE_VALUE", "");
                            rsRegister.updateString("WIP_DAYS_ORDER_WARPED", "");
                            rsRegister.updateString("WIP_DIVERSION_FLAG", "");
                            rsRegister.updateString("WIP_DIVERSION_REASON", "");
                            rsRegister.updateString("WIP_DIVERTED_FLAG", "CLOSED");
                            rsRegister.updateString("WIP_DIVERTED_REASON", "");
                            rsRegister.updateString("WIP_OA_NO", "");
                            rsRegister.updateString("WIP_OA_DATE", "");
                            rsRegister.updateString("WIP_OC_NO", "");
                            rsRegister.updateString("WIP_OC_MONTHYEAR", "");
                            rsRegister.updateString("WIP_CURRENT_SCH_MONTH", "");

                            rsRegister.updateString("WIP_PIECE_STAGE", "PLANNING");
                            rsRegister.updateString("WIP_STATUS", "CONFIRMED");

                            rsRegister.insertRow();

                        }

                        try {

                            data.Execute("INSERT INTO PRODUCTION.FELT_SALES_SAMPLE_ORDER_REGISTER (SAMPLE_PIECE_NO, SAMPLE_ORDER_NO, SAMPLE_ORDER_DATE, SAMPLE_ORDER_REMARK, MAPPED_SCRAP, MAPPED_DATE, UPN1, UPN2, UPN3, UPN4, UPN5, SCRAP_DATE, USER_REMARK, USER_ID, IP_FROM, HOLD_FOR_MAPPING, COMBINATION_UPN) VALUES "
                                    + "('" + ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString() + "'"
                                    + ", '" + ObjFeltSalesOrderDetails.getAttribute("DOC_NO").getString() + "'"
                                    + ", '" + EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()) + "'"
                                    + ", '" + ObjFeltSalesOrderDetails.getAttribute("REMARK").getString() + "'"
                                    + ", ''"
                                    + ", '0000-00-00'"
                                    + ", '', '', '', '', '', '', '', '', '', '0', '0')");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    
                    //Exit
                }
            }

            data.Execute("DELETE FROM PRODUCTION.FELT_SALES_SAMPLE_ORDER_REJECTION_SCRAP_DETAIL WHERE R_DOC_NO='" + OrderNo + "'");

            statementRejection = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetRejection = statementRejection.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_SAMPLE_ORDER_REJECTION_SCRAP_DETAIL WHERE R_DOC_NO='1'");

            for (int i = 1; i <= hmFeltRejectionDetails.size(); i++) {
                clsFeltRejectionDetails ObjFeltRejectionDetails = (clsFeltRejectionDetails) hmFeltRejectionDetails.get(Integer.toString(i));

                resultSetRejection.moveToInsertRow();
                resultSetRejection.updateInt("SR_NO", i);
                resultSetRejection.updateString("R_DOC_NO", ObjFeltRejectionDetails.getAttribute("R_DOC_NO").getString() + "");
                resultSetRejection.updateString("R_PIECE_NO", ObjFeltRejectionDetails.getAttribute("R_PIECE_NO").getString());
                resultSetRejection.updateString("R_NEW_PIECE_NO", ObjFeltRejectionDetails.getAttribute("R_NEW_PIECE_NO").getString());
                resultSetRejection.updateString("R_MACHINE_NO", ObjFeltRejectionDetails.getAttribute("R_MACHINE_NO").getString());
                resultSetRejection.updateString("R_POSITION", ObjFeltRejectionDetails.getAttribute("R_POSITION").getString());
                resultSetRejection.updateString("R_POSITION_DESC", ObjFeltRejectionDetails.getAttribute("R_POSITION_DESC").getString());
                resultSetRejection.updateString("R_PIECE_STAGE", ObjFeltRejectionDetails.getAttribute("R_PIECE_STAGE").getString());
                resultSetRejection.updateString("R_LAYER_TYPE", ObjFeltRejectionDetails.getAttribute("R_LAYER_TYPE").getString());
                resultSetRejection.updateString("R_NEW_PIECE_STAGE", ObjFeltRejectionDetails.getAttribute("R_NEW_PIECE_STAGE").getString());
                resultSetRejection.updateString("R_PRODUCT_CODE", ObjFeltRejectionDetails.getAttribute("R_PRODUCT_CODE").getString());
                resultSetRejection.updateString("R_PRODUCT_DESC", ObjFeltRejectionDetails.getAttribute("R_PRODUCT_DESC").getString());
                resultSetRejection.updateString("R_S_GROUP", ObjFeltRejectionDetails.getAttribute("R_S_GROUP").getString());
                resultSetRejection.updateString("R_LENGTH", ObjFeltRejectionDetails.getAttribute("R_LENGTH").getString());
                resultSetRejection.updateString("R_WIDTH", ObjFeltRejectionDetails.getAttribute("R_WIDTH").getString());
                resultSetRejection.updateString("R_GSM", ObjFeltRejectionDetails.getAttribute("R_GSM").getString());
                resultSetRejection.updateString("R_WEIGHT", ObjFeltRejectionDetails.getAttribute("R_WEIGHT").getString());
                resultSetRejection.updateString("R_SQ_MTR", ObjFeltRejectionDetails.getAttribute("R_SQ_MTR").getString());
                resultSetRejection.updateString("R_STYLE", ObjFeltRejectionDetails.getAttribute("R_STYLE").getString());
                resultSetRejection.updateString("R_REQ_MONTH", ObjFeltRejectionDetails.getAttribute("R_REQ_MONTH").getString());
                resultSetRejection.updateString("R_SYN_PER", ObjFeltRejectionDetails.getAttribute("R_SYN_PER").getString());
                resultSetRejection.updateString("R_REMARK", ObjFeltRejectionDetails.getAttribute("R_REMARK").getString());

                resultSetRejection.insertRow();

                        if (getAttribute("APPROVAL_STATUS").getString().equals("F") && !ObjFeltRejectionDetails.getAttribute("R_PIECE_NO").getString().equals("")) 
                        {

                            data.Execute("INSERT INTO PRODUCTION.FELT_SALES_PIECE_REGISTER "
                                    + "(PR_PIECE_NO, PR_DATE, PR_ORDER_DATE, PR_DOC_NO, PR_PARTY_CODE, PR_MACHINE_NO, PR_POSITION_NO, PR_UPN, PR_PRODUCT_CODE, PR_BILL_PRODUCT_CODE, PR_GROUP, PR_SYN_PER, PR_STYLE, PR_BILL_STYLE, PR_LENGTH, PR_ACTUAL_LENGTH, PR_BILL_LENGTH, PR_WIDTH, PR_ACTUAL_WIDTH, PR_BILL_WIDTH, PR_SQMTR, PR_BILL_SQMTR, PR_GSM, PR_TENDER_GSM, PR_BILL_GSM, PR_THORITICAL_WEIGHT, PR_TENDER_WEIGHT, PR_ACTUAL_WEIGHT, PR_BILL_WEIGHT, PR_FELT_VALUE_WITH_GST, PR_FELT_VALUE_WITHOUT_GST, PR_FELT_BASE_VALUE, PR_INCHARGE, PR_PIECE_AB_FLAG, PR_REQUESTED_MONTH, PR_REQ_MTH_LAST_DDMMYY, PR_REQ_MONTH_ORIGINAL, PR_REQ_MONTH_REMARKS, PR_PRIORITY_HOLD_CAN_FLAG, PR_PIECE_STAGE, PR_WIP_STATUS, PR_GIDC_STATUS, PR_WARP_DATE, PR_WVG_DATE, PR_SPLICE_DATE, PR_MND_DATE, PR_NDL_DATE, PR_SEAM_DATE, PR_FNSG_DATE, PR_RCV_DATE, PR_WARP_A_DATE, PR_WARP_B_DATE, PR_WARP_LAYER_REMARK, PR_WARPING_WEIGHT, PR_WARPING_WEIGHT_A, PR_WARPING_WEIGHT_B, PR_WVG_A_DATE, PR_WVG_B_DATE, WVG_LAYER_REMARK, PR_WEAVING_WEIGHT, PR_WEAVING_WEIGHT_A, PR_WEAVING_WEIGHT_B, PR_SPLICE_WIEGHT, PR_MND_A_DATE, PR_MND_B_DATE, PR_MND_LAYER_REMARK, PR_MENDING_WEIGHT, PR_MENDING_WEIGHT_A, PR_MENDING_WEIGHT_B, PR_NEEDLING_WEIGHT, PR_SEAM_WEIGHT, PR_DIVERSION_FLAG, PR_DIVERSION_REASON, PR_DIVERTED_FLAG, PR_DIVERTED_REASON, PR_DIVERTED_DATE, PR_DIV_BEFORE_STAGE, PR_DIV_NO_CHANGE_DATE, PR_DIV_NO_CHANGE_FLAG, PR_EXP_DISPATCH_DATE, PR_REGION, PR_REFERENCE, PR_REFERENCE_DATE, PR_PO_NO, PR_PO_DATE, PR_ORDER_REMARK, PR_PIECE_REMARK, PR_PKG_DP_NO, PR_PKG_DP_DATE, PR_BALE_NO, PR_PACKED_DATE, PR_INVOICE_NO, PR_INVOICE_DATE, PR_INVOICE_AMOUNT, PR_EXPORT_BL_NO, PR_EXPORT_BL_DATE, PR_EXPORT_UNIT_OF_CURRENCY, PR_EXPORT_EXCHANGE_RATE, PR_EXPORT_INV_AMOUNT, PR_LR_NO, PR_LR_DATE, PR_INVOICE_PARTY, PR_PARTY_CODE_ORIGINAL, PR_PIECE_NO_ORIGINAL, PR_HOLD_DATE, PR_HOLD_REASON, PR_RELEASE_DATE, BALE_REOPEN_FLG, PR_SALES_RETURNS_NO, PR_SALES_RETURNS_DATE, PR_SALES_RETURNS_REMARKS, PR_SALES_RETURNS_FLG, PR_REJECTED_FLAG, PR_REJECTED_REMARK, PR_ADJUSTABLE_LENGTH, PR_ADJUSTABLE_WIDTH, PR_ADJUSTABLE_GSM, PR_ADJUSTABLE_WEIGHT, PR_DELINK, PR_OBSOLETE_DATE, PR_DELINK_REASON, PR_WH_LOCATION_ID, PR_DAYS_ORDER_WARPED, PR_DAYS_ORDER_WARPED_STATUS, PR_DAYS_ORDER_WVG, PR_DAYS_ORDER_WVG_STATUS, PR_DAYS_ORDER_MND, PR_DAYS_ORDER_MND_STATUS, PR_DAYS_ORDER_NDL, PR_DAYS_ORDER_NDL_STATUS, PR_DAYS_ORDER_FNG, PR_DAYS_ORDER_FNG_STATUS, PR_DAYS_WRP_WVG, PR_DAYS_WRP_WVG_STATUS, PR_DAYS_WVG_MND, PR_DAYS_WVG_MND_STATUS, PR_DAYS_MND_NDL, PR_DAYS_MND_NDL_STATUS, PR_DAYS_NDL_FNG, PR_DAYS_NDL_FNG_STATUS, PR_DAYS_MND_FNG, PR_DAYS_MND_FNG_STATUS, PR_DAYS_ORDER_INVOICE, PR_DAYS_ORDER_INVOICE_STATUS, PR_DAYS_STOCK_INVOICE, PR_DAYS_STOCK_INVOICE_STATUS, PR_DAYS_WH_STOCK, PR_DAYS_WH_PACKED, PR_DAYS_STATUS, PR_DAYS_PACK_INVOICE, PR_DAYS_PACK_NOT_INVOICE, PR_DAYS_PACK_NOT_INVOICE_STATUS, PR_DAYS_WH_STOCK_STATUS, PR_DAYS_CURRENT_STAGE, PR_WARP_EXECUTE_DATE, PR_CLOSURE_REOPEN_IND, PR_CLOSURE_DATE, PR_CLOSURE_REMARK, PR_REOPEN_DATE, PR_REOPEN_REMARK, PR_EXPECTED_DISPATCH, PR_EXP_DISPATCH_FROM, PR_EXP_DISPATCH_DOCNO, PE_PIECE_IT_DEPT_REMARK, PR_CONTACT_PERSON, PR_EMAIL_ID, PR_PHONE_NO, PR_OA_MONTHYEAR, PR_OA_NO, PR_OA_DATE, PR_OC_NO, PR_OC_DATE, PR_OC_MONTHYEAR, PR_OC_LAST_DDMMYY, PR_PIECE_OC_MONTH_IT_REMARK, PR_CURRENT_SCH_MONTH, PR_CURRENT_SCH_LAST_DDMMYY, PR_PIECE_CURRENT_MONTH_IT_REMARK, PR_SP_MONTHYEAR, PR_SP_LAST_DDMMYY, PR_SP_REMARKS, PR_EXP_WIP_DELIVERY_DATE, PR_EXP_PI_DATE, PR_ACT_PI_DATE, PR_EXP_DESPATCH_DATE, PR_EXP_PAY_CHQRC_DATE, PR_ACT_PAY_CHQRC_DATE, PR_EXP_PAY_CHQRC_DATE_ORIGINAL, PR_SPL_REQUEST_MONTHYEAR, PR_SPL_REQUEST_DATE, PR_SPL_REQUEST_REMARK, PR_PAYMENT_RCVD_VOUCHER, PR_PAYMENT_SHORT_AMOUNT, PR_PAYMENT_RCVD_AMOUNT, PR_PIECETRIAL_FLAG, PR_PIECETRIAL_CATEGORY, PR_WH_CODE, PR_INWARD_NO, PR_RACK_NO, PR_PIECE_ID, PR_LOCATION, PR_MFG_MONTH, PR_MFG_YEAR, PR_SCHEDULE_MONTH, PR_MFG_SPILL_OVEVER_REMARK, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, HIERARCHY_ID, APPROVER_BY, APPROVER_DATE, APPROVER_REMARK, PR_NEEDLING_WEIGHT_A, PR_NEEDLING_WEIGHT_B, PR_EXP_PAY_CHQRC_REMARKS, PR_EXP_PAY_CHQRC_REMARKS_DATE, PR_EXPORT_PARTY_REMARK, PR_EXPORT_PARTY_ORDERNO, PR_EXPORT_PARTY_PAYMENT_DATE, PR_RATE_INDICATOR, PR_SDF_INSTRUCT_DATE, PR_SDF_SPIRALED_DATE, PR_SDF_ASSEMBLED_DATE, PR_FELT_RATE, PR_FELT_VALUE_WITHOUT_DISCOUNT, PR_RATE_IND_DESPATCH_DATE, PR_SIZE_CRITERIA, PR_PRODUCT_CATG, PR_DFS_IN_DATE, PR_DFS_OUT_DATE, PR_MATERIAL_CODE, PR_OBSOLETE_SOURCE, PR_OBSOLETE_UPN_ASSIGN_STATUS, PR_SCRAP_REASON, PR_UNMAPPED_REASON, PR_WH_EXP_DISPATCH_DATE, PR_WH_EXP_DISPATCH_REMARK,  PR_UNABLE_TO_CONTACT, PR_CONTACTED_PERSON, PR_CONTACTED_NO) "
                                    + " "
                                    + "SELECT '"+ObjFeltRejectionDetails.getAttribute("R_NEW_PIECE_NO").getString()+"', PR_DATE, PR_ORDER_DATE, PR_DOC_NO, '"+getAttribute("PARTY_CODE").getString()+"', '"+ObjFeltRejectionDetails.getAttribute("R_MACHINE_NO").getString()+"', '"+ObjFeltRejectionDetails.getAttribute("R_POSITION").getString()+"', '"+getAttribute("PARTY_CODE").getString()+ObjFeltRejectionDetails.getAttribute("R_MACHINE_NO").getString()+"0000', "
                                    + "'"+ObjFeltRejectionDetails.getAttribute("R_PRODUCT_CODE").getString()+"', '"+ObjFeltRejectionDetails.getAttribute("R_PRODUCT_CODE").getString()+"', "
                                    + "'"+ObjFeltRejectionDetails.getAttribute("R_S_GROUP").getString()+"', '"+ObjFeltRejectionDetails.getAttribute("R_SYN_PER").getString()+"', "
                                    + "'"+ObjFeltRejectionDetails.getAttribute("R_STYLE").getString()+"', '"+ObjFeltRejectionDetails.getAttribute("R_STYLE").getString()+"', "
                                    + "'"+ObjFeltRejectionDetails.getAttribute("R_LENGTH").getString()+"', '', '"+ObjFeltRejectionDetails.getAttribute("R_LENGTH").getString()+"', "
                                    + "'"+ObjFeltRejectionDetails.getAttribute("R_WIDTH").getString()+"', '', '"+ObjFeltRejectionDetails.getAttribute("R_WIDTH").getString()+"', "
                                    + "'"+ObjFeltRejectionDetails.getAttribute("R_SQ_MTR").getString()+"', '"+ObjFeltRejectionDetails.getAttribute("R_SQ_MTR").getString()+"', "
                                    + "'"+ObjFeltRejectionDetails.getAttribute("R_GSM").getString()+"' , '', '"+ObjFeltRejectionDetails.getAttribute("R_GSM").getString()+"', "
                                    + "'"+ObjFeltRejectionDetails.getAttribute("R_WEIGHT").getString()+"', '', '', '"+ObjFeltRejectionDetails.getAttribute("R_WEIGHT").getString()+"', '', '', '', PR_INCHARGE, PR_PIECE_AB_FLAG, '"+ObjFeltRejectionDetails.getAttribute("R_REQ_MONTH").getString()+"', '0000-00-00', PR_REQ_MONTH_ORIGINAL, PR_REQ_MONTH_REMARKS, PR_PRIORITY_HOLD_CAN_FLAG, '"+ObjFeltRejectionDetails.getAttribute("R_NEW_PIECE_STAGE").getString()+"', PR_WIP_STATUS, PR_GIDC_STATUS, PR_WARP_DATE, PR_WVG_DATE, PR_SPLICE_DATE, PR_MND_DATE, PR_NDL_DATE, PR_SEAM_DATE, PR_FNSG_DATE, PR_RCV_DATE, PR_WARP_A_DATE, PR_WARP_B_DATE, PR_WARP_LAYER_REMARK, PR_WARPING_WEIGHT, PR_WARPING_WEIGHT_A, PR_WARPING_WEIGHT_B, PR_WVG_A_DATE, PR_WVG_B_DATE, WVG_LAYER_REMARK, PR_WEAVING_WEIGHT, PR_WEAVING_WEIGHT_A, PR_WEAVING_WEIGHT_B, PR_SPLICE_WIEGHT, PR_MND_A_DATE, PR_MND_B_DATE, PR_MND_LAYER_REMARK, PR_MENDING_WEIGHT, PR_MENDING_WEIGHT_A, PR_MENDING_WEIGHT_B, PR_NEEDLING_WEIGHT, PR_SEAM_WEIGHT, PR_DIVERSION_FLAG, PR_DIVERSION_REASON, PR_DIVERTED_FLAG, PR_DIVERTED_REASON, PR_DIVERTED_DATE, PR_DIV_BEFORE_STAGE, PR_DIV_NO_CHANGE_DATE, PR_DIV_NO_CHANGE_FLAG, PR_EXP_DISPATCH_DATE, PR_REGION, PR_REFERENCE, PR_REFERENCE_DATE, PR_PO_NO, PR_PO_DATE, PR_ORDER_REMARK, PR_PIECE_REMARK, PR_PKG_DP_NO, PR_PKG_DP_DATE, PR_BALE_NO, PR_PACKED_DATE, PR_INVOICE_NO, PR_INVOICE_DATE, PR_INVOICE_AMOUNT, PR_EXPORT_BL_NO, PR_EXPORT_BL_DATE, PR_EXPORT_UNIT_OF_CURRENCY, PR_EXPORT_EXCHANGE_RATE, PR_EXPORT_INV_AMOUNT, PR_LR_NO, PR_LR_DATE, PR_INVOICE_PARTY, PR_PARTY_CODE_ORIGINAL, PR_PIECE_NO_ORIGINAL, PR_HOLD_DATE, PR_HOLD_REASON, PR_RELEASE_DATE, BALE_REOPEN_FLG, PR_SALES_RETURNS_NO, PR_SALES_RETURNS_DATE, PR_SALES_RETURNS_REMARKS, PR_SALES_RETURNS_FLG, PR_REJECTED_FLAG, PR_REJECTED_REMARK, PR_ADJUSTABLE_LENGTH, PR_ADJUSTABLE_WIDTH, PR_ADJUSTABLE_GSM, PR_ADJUSTABLE_WEIGHT, PR_DELINK, PR_OBSOLETE_DATE, PR_DELINK_REASON, PR_WH_LOCATION_ID, PR_DAYS_ORDER_WARPED, PR_DAYS_ORDER_WARPED_STATUS, PR_DAYS_ORDER_WVG, PR_DAYS_ORDER_WVG_STATUS, PR_DAYS_ORDER_MND, PR_DAYS_ORDER_MND_STATUS, PR_DAYS_ORDER_NDL, PR_DAYS_ORDER_NDL_STATUS, PR_DAYS_ORDER_FNG, PR_DAYS_ORDER_FNG_STATUS, PR_DAYS_WRP_WVG, PR_DAYS_WRP_WVG_STATUS, PR_DAYS_WVG_MND, PR_DAYS_WVG_MND_STATUS, PR_DAYS_MND_NDL, PR_DAYS_MND_NDL_STATUS, PR_DAYS_NDL_FNG, PR_DAYS_NDL_FNG_STATUS, PR_DAYS_MND_FNG, PR_DAYS_MND_FNG_STATUS, PR_DAYS_ORDER_INVOICE, PR_DAYS_ORDER_INVOICE_STATUS, PR_DAYS_STOCK_INVOICE, PR_DAYS_STOCK_INVOICE_STATUS, PR_DAYS_WH_STOCK, PR_DAYS_WH_PACKED, PR_DAYS_STATUS, PR_DAYS_PACK_INVOICE, PR_DAYS_PACK_NOT_INVOICE, PR_DAYS_PACK_NOT_INVOICE_STATUS, PR_DAYS_WH_STOCK_STATUS, PR_DAYS_CURRENT_STAGE, PR_WARP_EXECUTE_DATE, PR_CLOSURE_REOPEN_IND, PR_CLOSURE_DATE, PR_CLOSURE_REMARK, PR_REOPEN_DATE, PR_REOPEN_REMARK, PR_EXPECTED_DISPATCH, PR_EXP_DISPATCH_FROM, PR_EXP_DISPATCH_DOCNO, PE_PIECE_IT_DEPT_REMARK, PR_CONTACT_PERSON, PR_EMAIL_ID, PR_PHONE_NO, PR_OA_MONTHYEAR, PR_OA_NO, PR_OA_DATE, PR_OC_NO, PR_OC_DATE, PR_OC_MONTHYEAR, PR_OC_LAST_DDMMYY, PR_PIECE_OC_MONTH_IT_REMARK, PR_CURRENT_SCH_MONTH, PR_CURRENT_SCH_LAST_DDMMYY, PR_PIECE_CURRENT_MONTH_IT_REMARK, PR_SP_MONTHYEAR, PR_SP_LAST_DDMMYY, PR_SP_REMARKS, PR_EXP_WIP_DELIVERY_DATE, PR_EXP_PI_DATE, PR_ACT_PI_DATE, PR_EXP_DESPATCH_DATE, PR_EXP_PAY_CHQRC_DATE, PR_ACT_PAY_CHQRC_DATE, PR_EXP_PAY_CHQRC_DATE_ORIGINAL, PR_SPL_REQUEST_MONTHYEAR, PR_SPL_REQUEST_DATE, PR_SPL_REQUEST_REMARK, PR_PAYMENT_RCVD_VOUCHER, PR_PAYMENT_SHORT_AMOUNT, PR_PAYMENT_RCVD_AMOUNT, PR_PIECETRIAL_FLAG, PR_PIECETRIAL_CATEGORY, PR_WH_CODE, PR_INWARD_NO, PR_RACK_NO, PR_PIECE_ID, PR_LOCATION, PR_MFG_MONTH, PR_MFG_YEAR, PR_SCHEDULE_MONTH, PR_MFG_SPILL_OVEVER_REMARK, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, HIERARCHY_ID, APPROVER_BY, APPROVER_DATE, APPROVER_REMARK, PR_NEEDLING_WEIGHT_A, PR_NEEDLING_WEIGHT_B, PR_EXP_PAY_CHQRC_REMARKS, PR_EXP_PAY_CHQRC_REMARKS_DATE, PR_EXPORT_PARTY_REMARK, PR_EXPORT_PARTY_ORDERNO, PR_EXPORT_PARTY_PAYMENT_DATE, PR_RATE_INDICATOR, PR_SDF_INSTRUCT_DATE, PR_SDF_SPIRALED_DATE, PR_SDF_ASSEMBLED_DATE, PR_FELT_RATE, PR_FELT_VALUE_WITHOUT_DISCOUNT, PR_RATE_IND_DESPATCH_DATE, PR_SIZE_CRITERIA, PR_PRODUCT_CATG, PR_DFS_IN_DATE, PR_DFS_OUT_DATE, PR_MATERIAL_CODE, PR_OBSOLETE_SOURCE, PR_OBSOLETE_UPN_ASSIGN_STATUS, PR_SCRAP_REASON, PR_UNMAPPED_REASON, PR_WH_EXP_DISPATCH_DATE, PR_WH_EXP_DISPATCH_REMARK,  PR_UNABLE_TO_CONTACT, PR_CONTACTED_PERSON, PR_CONTACTED_NO "
                                    + " FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                                    + " WHERE PR_PIECE_NO='"+ ObjFeltRejectionDetails.getAttribute("R_PIECE_NO").getString()+"'");
                            
                            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PIECE_STAGE='MOVE_TO_SAMPLE' WHERE PR_PIECE_NO='"+ObjFeltRejectionDetails.getAttribute("R_PIECE_NO").getString()+"'");
                            
                            data.Execute("INSERT INTO PRODUCTION.FELT_WIP_PIECE_REGISTER"
                                    + "( WIP_PIECE_NO, WIP_EXT_PIECE_NO, WIP_DATE, WIP_ORDER_DATE, WIP_DOC_NO, WIP_UPN, WIP_MACHINE_NO, WIP_POSITION_NO, WIP_PARTY_CODE, WIP_GROUP, WIP_PRODUCT_CODE, WIP_BILL_PRODUCT_CODE, WIP_STYLE, WIP_BILL_STYLE, WIP_LENGTH, WIP_ACTUAL_LENGTH, WIP_BILL_LENGTH, WIP_WIDTH, WIP_ACTUAL_WIDTH, WIP_BILL_WIDTH, WIP_GSM, WIP_TENDER_GSM, WIP_BILL_GSM, WIP_THORITICAL_WEIGHT, WIP_ACTUAL_WEIGHT, WIP_TENDER_WEIGHT, WIP_BILL_WEIGHT, WIP_SQMTR, WIP_BILL_SQMTR, WIP_PRIORITY_HOLD_CAN_FLAG, WIP_PARTY_CODE_ORIGINAL, WIP_PIECE_NO_ORIGINAL, WIP_SYN_PER, WIP_REQUESTED_MONTH, WIP_REQ_MTH_LAST_DDMMYY, WIP_DATE_SLOT, WIP_REGION, WIP_INCHARGE, WIP_REFERENCE, WIP_REFERENCE_DATE, WIP_PO_NO, WIP_PO_DATE, WIP_ORDER_REMARK, WIP_PIECE_REMARK, WIP_PIECE_STAGE, WIP_STATUS, WIP_GIDC_STATUS, WIP_PIECE_AB_FLAG, WIP_WARP_LAYER_REMARK, WIP_WARP_DATE, WIP_WARP_A_DATE, WIP_WARP_B_DATE, WIP_WARPING_WEIGHT, WIP_WARPING_WEIGHT_A, WIP_WARPING_WEIGHT_B, WIP_WVG_LAYER_REMARK, WIP_WVG_DATE, WIP_WVG_A_DATE, WIP_WVG_B_DATE, WIP_WEAVING_WEIGHT, WIP_WEAVING_WEIGHT_A, WIP_WEAVING_WEIGHT_B, WIP_MND_LAYER_REMARK, WIP_MND_DATE, WIP_MND_A_DATE, WIP_MND_B_DATE, WIP_MENDING_WEIGHT, WIP_MENDING_WEIGHT_A, WIP_MENDING_WEIGHT_B, WIP_NDL_DATE, WIP_NEEDLING_WEIGHT, WIP_SPLICE_DATE, WIP_SPLICE_WEIGHT, WIP_SEAM_DATE, WIP_SEAM_WEIGHT, WIP_FNSG_DATE, WIP_FELT_VALUE_WITH_GST, WIP_FELT_VALUE_WITHOUT_GST, WIP_FELT_BASE_VALUE, WIP_DAYS_ORDER_WARPED, WIP_DAYS_ORDER_WARPED_STATUS, WIP_DAYS_ORDER_WVG, WIP_DAYS_ORDER_WVG_STATUS, WIP_DAYS_ORDER_MND, WIP_DAYS_ORDER_MND_STATUS, WIP_DAYS_ORDER_NDL, WIP_DAYS_ORDER_NDL_STATUS, WIP_DAYS_ORDER_FNG, WIP_DAYS_ORDER_FNG_STATUS, WIP_DAYS_WRP_WVG, WIP_DAYS_WRP_WVG_STATUS, WIP_DAYS_WVG_MND, WIP_DAYS_WVG_MND_STATUS, WIP_DAYS_MND_NDL, WIP_DAYS_MND_NDL_STATUS, WIP_DAYS_NDL_FNG, WIP_DAYS_NDL_FNG_STATUS, WIP_DAYS_MND_FNG, WIP_DAYS_MND_FNG_STATUS, WIP_DAYS_STATUS, WIP_REJECTED_FLAG, WIP_REJECTED_REMARK, WIP_DIVERSION_FLAG, WIP_DIVERSION_REASON, WIP_DIVERTED_FLAG, WIP_DIVERTED_REASON, WIP_EXP_DISPATCH_DATE, WIP_HOLD_DATE, WIP_HOLD_REASON, WIP_RELEASE_DATE, WIP_OBSOLETE, WIP_OBSOLETE_REASON, WIP_OBSOLETE_DATE, WIP_MFG_MONTH, WIP_MFG_YEAR, WIP_MFG_SPILL_OVEVER_REMARK, WIP_WARP_EXECUTE_DATE, WIP_DAYS_WH_STOCK, WIP_DAYS_WH_PACKED, WIP_SCHEDULE_MONTH, WIP_CLOSURE_REOPEN_IND, WIP_CLOSURE_DATE, WIP_CLOSURE_REMARK, WIP_REOPEN_DATE, WIP_REOPEN_REMARK, WIP_DAYS_CURRENT_STAGE, WIP_EXPECTED_DISPATCH, WIP_EXP_DISPATCH_FROM, WIP_EXP_DISPATCH_DOCNO, WIP_ADJUSTABLE_LENGTH, WIP_ADJUSTABLE_WIDTH, WIP_ADJUSTABLE_GSM, WIP_ADJUSTABLE_WEIGHT, WIP_PIECE_IT_DEPT_REMARK, WIP_OA_NO, WIP_OA_DATE, WIP_OC_NO, WIP_OC_DATE, WIP_OC_MONTHYEAR, WIP_OC_LAST_DDMMYY, WIP_CURRENT_SCH_MONTH, WIP_CURRENT_SCH_LAST_DDMMYY, WIP_SP_MONTHYEAR, WIP_SP_LAST_DDMMYY, WIP_SP_REMARKS, WIP_SPL_REQUEST_MONTHYEAR, WIP_SPL_REQUEST_DATE, WIP_SPL_REQUEST_REMARK, WIP_EXP_WIP_DELIVERY_DATE, PR_REFERENCE, WIP_SDF_INSTRUCT_DATE, WIP_SDF_SPIRALED_DATE, WIP_SDF_ASSEMBLED_DATE, WIP_MATERIAL_CODE, WIP_OBSOLETE_SOURCE, WIP_OBSOLETE_UPN_ASSIGN_STATUS, WIP_SCRAP_REASON, WIP_UNMAPPED_REASON, WIP_WVG_LOOM_NO, WIP_BEAM_NO, WIP_BEAM_INDICATOR_DATE )"
                                    + ""
                                    + " SELECT '"+ObjFeltRejectionDetails.getAttribute("R_NEW_PIECE_NO").getString()+"', WIP_EXT_PIECE_NO, WIP_DATE, WIP_ORDER_DATE, "
                                    + "WIP_DOC_NO, '"+getAttribute("PARTY_CODE").getString()+ObjFeltRejectionDetails.getAttribute("R_MACHINE_NO").getString()+"', "
                                    + "'"+ObjFeltRejectionDetails.getAttribute("R_MACHINE_NO").getString()+"', '"+ObjFeltRejectionDetails.getAttribute("R_POSITION").getString()+"', '"+getAttribute("PARTY_CODE").getString()+"', "
                                    + "'"+ObjFeltRejectionDetails.getAttribute("R_S_GROUP").getString()+"', "
                                    + "'"+ObjFeltRejectionDetails.getAttribute("R_PRODUCT_CODE").getString()+"', '"+ObjFeltRejectionDetails.getAttribute("R_PRODUCT_CODE").getString()+"', '"+ObjFeltRejectionDetails.getAttribute("R_STYLE").getString()+"', "
                                    + "'"+ObjFeltRejectionDetails.getAttribute("R_STYLE").getString()+"', '"+ObjFeltRejectionDetails.getAttribute("R_LENGTH").getString()+"', WIP_ACTUAL_LENGTH, '"+ObjFeltRejectionDetails.getAttribute("R_LENGTH").getString()+"', "
                                    + "'"+ObjFeltRejectionDetails.getAttribute("R_WIDTH").getString()+"', WIP_ACTUAL_WIDTH, '"+ObjFeltRejectionDetails.getAttribute("R_WIDTH").getString()+"', '"+ObjFeltRejectionDetails.getAttribute("R_GSM").getString()+"', WIP_TENDER_GSM, "
                                    + "'"+ObjFeltRejectionDetails.getAttribute("R_GSM").getString()+"', '"+ObjFeltRejectionDetails.getAttribute("R_WEIGHT").getString()+"', WIP_ACTUAL_WEIGHT, '', "
                                    + "'"+ObjFeltRejectionDetails.getAttribute("R_WEIGHT").getString()+"', '"+ObjFeltRejectionDetails.getAttribute("R_SQ_MTR").getString()+"', '"+ObjFeltRejectionDetails.getAttribute("R_SQ_MTR").getString()+"', WIP_PRIORITY_HOLD_CAN_FLAG, "
                                    + "WIP_PARTY_CODE_ORIGINAL, WIP_PIECE_NO_ORIGINAL, '"+ObjFeltRejectionDetails.getAttribute("R_SYN_PER").getString()+"', "
                                    + "'"+ObjFeltRejectionDetails.getAttribute("R_REQ_MONTH").getString()+"', '0000-00-00', WIP_DATE_SLOT, WIP_REGION, "
                                    + "WIP_INCHARGE, WIP_REFERENCE, WIP_REFERENCE_DATE, WIP_PO_NO, WIP_PO_DATE, "
                                    + "WIP_ORDER_REMARK, WIP_PIECE_REMARK, '"+ObjFeltRejectionDetails.getAttribute("R_NEW_PIECE_STAGE").getString()+"', WIP_STATUS, "
                                    + "WIP_GIDC_STATUS, WIP_PIECE_AB_FLAG, WIP_WARP_LAYER_REMARK, WIP_WARP_DATE, "
                                    + "WIP_WARP_A_DATE, WIP_WARP_B_DATE, WIP_WARPING_WEIGHT, WIP_WARPING_WEIGHT_A, "
                                    + "WIP_WARPING_WEIGHT_B, WIP_WVG_LAYER_REMARK, WIP_WVG_DATE, WIP_WVG_A_DATE, "
                                    + "WIP_WVG_B_DATE, WIP_WEAVING_WEIGHT, WIP_WEAVING_WEIGHT_A, WIP_WEAVING_WEIGHT_B, WIP_MND_LAYER_REMARK, WIP_MND_DATE, WIP_MND_A_DATE, WIP_MND_B_DATE, WIP_MENDING_WEIGHT, WIP_MENDING_WEIGHT_A, WIP_MENDING_WEIGHT_B, WIP_NDL_DATE, WIP_NEEDLING_WEIGHT, WIP_SPLICE_DATE, WIP_SPLICE_WEIGHT, WIP_SEAM_DATE, WIP_SEAM_WEIGHT, WIP_FNSG_DATE, WIP_FELT_VALUE_WITH_GST, WIP_FELT_VALUE_WITHOUT_GST, WIP_FELT_BASE_VALUE, WIP_DAYS_ORDER_WARPED, WIP_DAYS_ORDER_WARPED_STATUS, WIP_DAYS_ORDER_WVG, WIP_DAYS_ORDER_WVG_STATUS, WIP_DAYS_ORDER_MND, WIP_DAYS_ORDER_MND_STATUS, WIP_DAYS_ORDER_NDL, WIP_DAYS_ORDER_NDL_STATUS, WIP_DAYS_ORDER_FNG, WIP_DAYS_ORDER_FNG_STATUS, WIP_DAYS_WRP_WVG, WIP_DAYS_WRP_WVG_STATUS, WIP_DAYS_WVG_MND, WIP_DAYS_WVG_MND_STATUS, WIP_DAYS_MND_NDL, WIP_DAYS_MND_NDL_STATUS, WIP_DAYS_NDL_FNG, WIP_DAYS_NDL_FNG_STATUS, WIP_DAYS_MND_FNG, WIP_DAYS_MND_FNG_STATUS, WIP_DAYS_STATUS, WIP_REJECTED_FLAG, WIP_REJECTED_REMARK, WIP_DIVERSION_FLAG, WIP_DIVERSION_REASON, WIP_DIVERTED_FLAG, WIP_DIVERTED_REASON, WIP_EXP_DISPATCH_DATE, WIP_HOLD_DATE, WIP_HOLD_REASON, WIP_RELEASE_DATE, WIP_OBSOLETE, WIP_OBSOLETE_REASON, WIP_OBSOLETE_DATE, WIP_MFG_MONTH, WIP_MFG_YEAR, WIP_MFG_SPILL_OVEVER_REMARK, WIP_WARP_EXECUTE_DATE, WIP_DAYS_WH_STOCK, WIP_DAYS_WH_PACKED, WIP_SCHEDULE_MONTH, WIP_CLOSURE_REOPEN_IND, WIP_CLOSURE_DATE, WIP_CLOSURE_REMARK, WIP_REOPEN_DATE, WIP_REOPEN_REMARK, WIP_DAYS_CURRENT_STAGE, WIP_EXPECTED_DISPATCH, WIP_EXP_DISPATCH_FROM, WIP_EXP_DISPATCH_DOCNO, WIP_ADJUSTABLE_LENGTH, WIP_ADJUSTABLE_WIDTH, WIP_ADJUSTABLE_GSM, WIP_ADJUSTABLE_WEIGHT, WIP_PIECE_IT_DEPT_REMARK, WIP_OA_NO, WIP_OA_DATE, WIP_OC_NO, WIP_OC_DATE, WIP_OC_MONTHYEAR, WIP_OC_LAST_DDMMYY, WIP_CURRENT_SCH_MONTH, WIP_CURRENT_SCH_LAST_DDMMYY, WIP_SP_MONTHYEAR, WIP_SP_LAST_DDMMYY, WIP_SP_REMARKS, WIP_SPL_REQUEST_MONTHYEAR, WIP_SPL_REQUEST_DATE, WIP_SPL_REQUEST_REMARK, WIP_EXP_WIP_DELIVERY_DATE, PR_REFERENCE, WIP_SDF_INSTRUCT_DATE, WIP_SDF_SPIRALED_DATE, WIP_SDF_ASSEMBLED_DATE, WIP_MATERIAL_CODE, WIP_OBSOLETE_SOURCE, WIP_OBSOLETE_UPN_ASSIGN_STATUS, WIP_SCRAP_REASON, WIP_UNMAPPED_REASON, WIP_WVG_LOOM_NO, WIP_BEAM_NO, WIP_BEAM_INDICATOR_DATE"
                                    + "  FROM PRODUCTION.FELT_WIP_PIECE_REGISTER WHERE WIP_PIECE_NO='"+ObjFeltRejectionDetails.getAttribute("R_PIECE_NO").getString()+"'"
                                    + "");
                            
                            String pEmailId = "sdmlerp@dineshmills.com,brdfltdesign@dineshmills.com";
                            
                            //String Piece_Stage = data.getStringValueFromDB("SELECT PR_PIECE_STAGE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='"+ObjFeltRejectionDetails.getAttribute("R_PIECE_NO").getString()+"'");
                            String Piece_Stage = ObjFeltRejectionDetails.getAttribute("R_NEW_PIECE_STAGE").getString();
                            if(Piece_Stage.equals("WEAVING"))
                            {
                                pEmailId = pEmailId + ",brdfltweave1@dineshmills.com";
                            }
                            else if(Piece_Stage.equals("MENDING"))
                            {
                                pEmailId = pEmailId + ",brdfltweave1@dineshmills.com";
                            }
                            else if(Piece_Stage.equals("NEEDLING"))
                            {
                                pEmailId = pEmailId + ",brdfltweave1@dineshmills.com";
                            }
                            else if(Piece_Stage.equals("FINISHING"))
                            {
                                pEmailId = pEmailId + ",brdfltweave1@dineshmills.com";
                            }
                            
                            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_PIECE_STAGE='MOVE_TO_SAMPLE' WHERE WIP_PIECE_NO='"+ObjFeltRejectionDetails.getAttribute("R_PIECE_NO").getString()+"'");
                            
                            data.Execute("INSERT INTO PRODUCTION.FELT_SALES_SAMPLE_ORDER_REGISTER (SAMPLE_PIECE_NO, SAMPLE_ORDER_NO, SAMPLE_ORDER_DATE, SAMPLE_ORDER_REMARK, MAPPED_SCRAP, MAPPED_DATE, UPN1, UPN2, UPN3, UPN4, UPN5, SCRAP_DATE, USER_REMARK, USER_ID, IP_FROM, HOLD_FOR_MAPPING, COMBINATION_UPN) VALUES "
                                        + "('" + ObjFeltRejectionDetails.getAttribute("R_NEW_PIECE_NO").getString() + "'"
                                        + ", '" + getAttribute("DOC_NO").getString() + "'"
                                        + ", '" + EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()) + "'"
                                        + ", '" + ObjFeltRejectionDetails.getAttribute("R_REMARK").getString() + "'"
                                        + ", ''"
                                        + ", '0000-00-00'"
                                        + ", '', '', '', '', '', '', '', '', '', '0', '0')");

                            
                            String pSubject = "Sample order booked against Piece No : "+ObjFeltRejectionDetails.getAttribute("R_PIECE_NO").getString();
                            String pMessage = "";
                            System.out.println("Subject : "+pSubject);
                            pMessage = pMessage + "<br><br>";

                            pMessage = pMessage + "<div style='min-width:1000px;'>\n" +
                            "	<div style=' text-align:center; min-width:1000px;'><u>SAMPLE ORDER BOOKING</u></div>\n" +
                            "	<br><br>\n" +
                            "	<div style=' width:100%; heigth :200px;'>\n" +
                            "	\n" +
                            "		<div style=' width: 50%; float:left;'>\n" +
                            "                       ORIGINAL PIECE NO : "+ObjFeltRejectionDetails.getAttribute("R_PIECE_NO").getString()+"<br>"+
                            "                       NEW PIECE NO : "+ObjFeltRejectionDetails.getAttribute("R_NEW_PIECE_NO").getString()+"<br>"+
                            "                       MACHINE NO : "+ObjFeltRejectionDetails.getAttribute("R_MACHINE_NO").getString()+"<br>"+         
                            "                       POSITION : "+ObjFeltRejectionDetails.getAttribute("R_POSITION").getString()+"<br>"+         
                            "                       LENGTH : "+ObjFeltRejectionDetails.getAttribute("R_LENGTH").getString()+"<br>"+         
                            "                       WIDTH  : "+ObjFeltRejectionDetails.getAttribute("R_WIDTH").getString()+"<br>"+                 
                            "                       GSM : "+ObjFeltRejectionDetails.getAttribute("R_GSM").getString()+"<br>"+         
                            "                       SQMTR : "+ObjFeltRejectionDetails.getAttribute("R_SQ_MTR").getString()+"<br>"+                 
                            "                       WEIGHT : "+ObjFeltRejectionDetails.getAttribute("R_WEIGHT").getString()+"<br>"+         
                            "                       REQ MONTH : "+ObjFeltRejectionDetails.getAttribute("R_REQ_MONTH").getString()+"<br>"+                 
                            "			<br><br><hr>\n" +
                            "	\n" +
                            "	</div><br><br>\n";
                            System.out.println("Message : "+pMessage);
                            System.out.println("Email : "+pEmailId);
                            
                            String pCC = "daxesh@dineshmills.com";

                            try {

                                JavaMail.SendMailFeltOC(pEmailId, pMessage, pSubject, pCC);
                            } catch (Exception e) {
                                System.out.println("Error Msg "+e.getMessage());
                                e.printStackTrace();
                            }
                        }    
    
            }
                //======== Update the Approval Flow =========
                clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
                ObjFeltProductionApprovalFlow.ModuleID = ModuleID; //Felt Weaving Entry
                ObjFeltProductionApprovalFlow.DocNo = getAttribute("DOC_NO").getString();
                ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
                ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
                ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
                ObjFeltProductionApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getString();
                ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.FELT_SALES_SAMPLE_ORDER_HEADER";
                ObjFeltProductionApprovalFlow.IsCreator = true;
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
                    data.Execute("UPDATE PRODUCTION.FELT_SALES_SAMPLE_ORDER_HEADER SET REJECTED=0,CHANGED=1 WHERE DOC_NO ='" + getAttribute("DOC_NO").getString() + "'");
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
            }catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
        }
        /**
         * This routine checks whether the item is deletable or not. Criteria is
         * Approved item cannot be delete, and if not approved then user id is
         * checked whether doucment is created by the user. Only creator can
         * delete the document. After checking it deletes the record of selected
         * production date and document no.
         */
    
    public boolean CanDelete(String documentNo, String stringProductionDate, int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";

        try {
            tmpStmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_SALES_SAMPLE_ORDER_HEADER WHERE  DOC_NO='" + documentNo + "' AND APPROVED=" + 1;
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=" + ModuleID + " AND USER_ID=" + userID + " AND DOC_NO='" + documentNo + "' AND STATUS='W'";
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();

                if (rsTmp.getInt("COUNT") > 0) {
                    strSQL = "DELETE FROM PRODUCTION.FELT_SALES_SAMPLE_ORDER_HEADER WHERE "
                            + " DOC_DATE = '" + EITLERPGLOBAL.formatDateDB(stringProductionDate) + "'"
                            + " AND DOC_NO ='" + documentNo + "'";

                    tmpStmt.executeUpdate(strSQL);
                    LoadData();
                    return true;
                } else {
                    LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_SALES_SAMPLE_ORDER_HEADER WHERE DOC_NO='" + orderupdDocumentNo + "' AND APPROVED=1";
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=" + ModuleID + " AND USER_ID=" + userID + " AND DOC_NO='" + orderupdDocumentNo + "' AND STATUS='W'";
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
            String strSql = "SELECT * FROM PRODUCTION.FELT_SALES_SAMPLE_ORDER_HEADER WHERE  " + stringFindQuery + "";
            System.out.println("STR SQL : " + strSql);
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

        ResultSet resultSetRejection;
        Statement statementRejection;

        int serialNoCounter = 0;
        int RevNo = 0;

        try {

            setAttribute("REVISION_NO", 0);

            setAttribute("DOC_NO", resultSet.getString("DOC_NO"));
            setAttribute("DOC_DATE", resultSet.getDate("DOC_DATE"));

            setAttribute("REMARK", resultSet.getString("REMARK"));

            setAttribute("PARTY_CODE", resultSet.getString("PARTY_CODE"));
            setAttribute("PARTY_NAME", resultSet.getString("PARTY_NAME"));

            if (HistoryView) {
                RevNo = resultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO", resultSet.getInt("REVISION_NO"));
            }

            setAttribute("CREATED_BY", resultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE", resultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY", resultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE", resultSet.getString("MODIFIED_DATE"));
            setAttribute("APPROVED", resultSet.getInt("APPROVED"));
            //setAttribute("APPROVED_BY",resultSet.getInt("APPROVED_BY"));
            setAttribute("APPROVED_DATE", resultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED", resultSet.getBoolean("REJECTED"));
            //setAttribute("REJECTED_BY",resultSet.getBoolean("REJECTED_BY"));
            setAttribute("REJECTED_DATE", resultSet.getString("REJECTED_DATE"));
            setAttribute("CANCELED", resultSet.getInt("CANCELED"));
            setAttribute("HIERARCHY_ID", resultSet.getInt("HIERARCHY_ID"));
            // setAttribute("REJECTED_REMARKS",resultSet.getString("REJECTED_REMARKS"));

            hmFeltSalesOrderDetails.clear();

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            if (HistoryView) {
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_SAMPLE_ORDER_DETAIL_H WHERE DOC_NO='" + resultSet.getString("DOC_NO") + "'  AND REVISION_NO=" + RevNo + " ORDER BY SR_NO DESC");
            } else {
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_SAMPLE_ORDER_DETAIL WHERE DOC_NO='" + resultSet.getString("DOC_NO") + "'  ORDER BY DOC_NO");
            }
            while (resultSetTemp.next()) {
                serialNoCounter++;

                clsFeltSampleOrderDetails ObjFeltSalesOrderDetails = new clsFeltSampleOrderDetails();

                //ObjFeltSalesOrderDetails.setAttribute("SR_NO",serialNoCounter);
                // ObjFeltSalesOrderDetails.setAttribute("S_ORDER_DETAIL_CODE",resultSetTemp.getString("S_ORDER_DETAIL_CODE"));
                ObjFeltSalesOrderDetails.setAttribute("DOC_NO", resultSetTemp.getString("DOC_NO"));
                ObjFeltSalesOrderDetails.setAttribute("MACHINE_NO", resultSetTemp.getString("MACHINE_NO"));
                ObjFeltSalesOrderDetails.setAttribute("POSITION", resultSetTemp.getString("POSITION"));
                ObjFeltSalesOrderDetails.setAttribute("POSITION_DESC", resultSetTemp.getString("POSITION_DESC"));
                ObjFeltSalesOrderDetails.setAttribute("PIECE_NO", resultSetTemp.getString("PIECE_NO"));
                ObjFeltSalesOrderDetails.setAttribute("LAYER_TYPE", resultSetTemp.getString("LAYER_TYPE"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT_CODE", resultSetTemp.getString("PRODUCT_CODE"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT_DESC", resultSetTemp.getString("PRODUCT_DESC"));
                ObjFeltSalesOrderDetails.setAttribute("S_GROUP", resultSetTemp.getString("S_GROUP"));
                ObjFeltSalesOrderDetails.setAttribute("LENGTH", resultSetTemp.getString("LENGTH"));
                ObjFeltSalesOrderDetails.setAttribute("WIDTH", resultSetTemp.getString("WIDTH"));
                ObjFeltSalesOrderDetails.setAttribute("GSM", resultSetTemp.getString("GSM"));
                ObjFeltSalesOrderDetails.setAttribute("THORITICAL_WIDTH", resultSetTemp.getString("THORITICAL_WIDTH"));
                ObjFeltSalesOrderDetails.setAttribute("SQ_MTR", resultSetTemp.getString("SQ_MTR"));
                ObjFeltSalesOrderDetails.setAttribute("STYLE", resultSetTemp.getString("STYLE"));
                ObjFeltSalesOrderDetails.setAttribute("REQ_MONTH", resultSetTemp.getString("REQ_MONTH"));
                ObjFeltSalesOrderDetails.setAttribute("SYN_PER", resultSetTemp.getString("SYN_PER"));
                ObjFeltSalesOrderDetails.setAttribute("REMARK", resultSetTemp.getString("REMARK"));

                hmFeltSalesOrderDetails.put(Integer.toString(serialNoCounter), ObjFeltSalesOrderDetails);
            }
            resultSetTemp.close();
            statementTemp.close();

            hmFeltRejectionDetails.clear();

            statementRejection = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            resultSetRejection = statementRejection.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_SAMPLE_ORDER_REJECTION_SCRAP_DETAIL WHERE R_DOC_NO='" + resultSet.getString("DOC_NO") + "'  ORDER BY R_DOC_NO");
            serialNoCounter = 0;
            while (resultSetRejection.next()) {
                serialNoCounter++;

                clsFeltRejectionDetails ObjRejectionDetails = new clsFeltRejectionDetails();

                //ObjFeltSalesOrderDetails.setAttribute("SR_NO",serialNoCounter);
                // ObjFeltSalesOrderDetails.setAttribute("S_ORDER_DETAIL_CODE",resultSetTemp.getString("S_ORDER_DETAIL_CODE"));
                ObjRejectionDetails.setAttribute("R_DOC_NO", resultSetRejection.getString("R_DOC_NO"));
                ObjRejectionDetails.setAttribute("R_MACHINE_NO", resultSetRejection.getString("R_MACHINE_NO"));
                ObjRejectionDetails.setAttribute("R_POSITION", resultSetRejection.getString("R_POSITION"));
                ObjRejectionDetails.setAttribute("R_POSITION_DESC", resultSetRejection.getString("R_POSITION_DESC"));
                ObjRejectionDetails.setAttribute("R_PIECE_STAGE", resultSetRejection.getString("R_PIECE_STAGE"));
                ObjRejectionDetails.setAttribute("R_PIECE_NO", resultSetRejection.getString("R_PIECE_NO"));
                ObjRejectionDetails.setAttribute("R_NEW_PIECE_NO", resultSetRejection.getString("R_NEW_PIECE_NO"));
                ObjRejectionDetails.setAttribute("R_NEW_PIECE_STAGE", resultSetRejection.getString("R_NEW_PIECE_STAGE"));
                ObjRejectionDetails.setAttribute("R_LAYER_TYPE", resultSetRejection.getString("R_LAYER_TYPE"));
                ObjRejectionDetails.setAttribute("R_PRODUCT_CODE", resultSetRejection.getString("R_PRODUCT_CODE"));
                ObjRejectionDetails.setAttribute("R_PRODUCT_DESC", resultSetRejection.getString("R_PRODUCT_DESC"));
                ObjRejectionDetails.setAttribute("R_S_GROUP", resultSetRejection.getString("R_S_GROUP"));
                ObjRejectionDetails.setAttribute("R_LENGTH", resultSetRejection.getString("R_LENGTH"));
                ObjRejectionDetails.setAttribute("R_WIDTH", resultSetRejection.getString("R_WIDTH"));
                ObjRejectionDetails.setAttribute("R_GSM", resultSetRejection.getString("R_GSM"));
                ObjRejectionDetails.setAttribute("R_WEIGHT", resultSetRejection.getString("R_WEIGHT"));
                ObjRejectionDetails.setAttribute("R_SQ_MTR", resultSetRejection.getString("R_SQ_MTR"));
                ObjRejectionDetails.setAttribute("R_STYLE", resultSetRejection.getString("R_STYLE"));
                ObjRejectionDetails.setAttribute("R_REQ_MONTH", resultSetRejection.getString("R_REQ_MONTH"));
                ObjRejectionDetails.setAttribute("R_SYN_PER", resultSetRejection.getString("R_SYN_PER"));
                ObjRejectionDetails.setAttribute("R_REMARK", resultSetRejection.getString("R_REMARK"));

                hmFeltRejectionDetails.put(Integer.toString(serialNoCounter), ObjRejectionDetails);
            }
            resultSetRejection.close();
            statementRejection.close();

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
            hmFeltSalesOrderDetails.clear();

            String strSql = "SELECT * FROM PRODUCTION.FELT_SALES_SAMPLE_ORDER_HEADER_H WHERE DOC_NO = " + pDocNo + "";
            System.out.println("STR SQL : " + strSql);
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery(strSql);
            resultSet.first();
            setAttribute("DOC_NO", resultSet.getString("DOC_NO"));
            setAttribute("REVISION_NO", resultSet.getString("REVISION_NO"));
            setAttribute("UPDATED_BY", resultSet.getString("UPDATED_BY"));
            setAttribute("DOC_DATE", resultSet.getString("DOC_DATE"));
            setAttribute("HIERARCHY_ID", resultSet.getInt("HIERARCHY_ID"));

            setAttribute("DOC_NO", resultSet.getString("DOC_NO"));
            setAttribute("DOC_DATE", resultSet.getDate("DOC_DATE"));
            setAttribute("REGION", resultSet.getString("REGION"));
            setAttribute("SALES_ENGINEER", resultSet.getString("SALES_ENGINEER"));
            setAttribute("PARTY_CODE", resultSet.getString("PARTY_CODE"));
            setAttribute("PARTY_NAME", resultSet.getString("PARTY_NAME"));
            setAttribute("REFERENCE", resultSet.getString("REFERENCE"));
            setAttribute("REFERENCE_DATE", resultSet.getDate("REFERENCE_DATE"));
            setAttribute("P_O_NO", resultSet.getString("P_O_NO"));
            setAttribute("P_O_DATE", resultSet.getDate("P_O_DATE"));
            setAttribute("REMARK", resultSet.getString("REMARK"));

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_SAMPLE_ORDER_DETAIL_H WHERE DOC_NO='" + pDocNo + "'");

            while (resultSetTemp.next()) {
                serialNoCounter++;

                clsFeltSampleOrderDetails ObjFeltSalesOrderDetails = new clsFeltSampleOrderDetails();

                //ObjFeltSalesOrderDetails.setAttribute("SR_NO",serialNoCounter);
                // ObjFeltSalesOrderDetails.setAttribute("S_ORDER_DETAIL_CODE",resultSetTemp.getString("S_ORDER_DETAIL_CODE"));
                ObjFeltSalesOrderDetails.setAttribute("DOC_NO", resultSetTemp.getString("DOC_NO"));
                ObjFeltSalesOrderDetails.setAttribute("MACHINE_NO", resultSetTemp.getString("MACHINE_NO"));
                ObjFeltSalesOrderDetails.setAttribute("POSITION", resultSetTemp.getString("POSITION"));
                ObjFeltSalesOrderDetails.setAttribute("POSITION_DESC", resultSetTemp.getString("POSITION_DESC"));
                ObjFeltSalesOrderDetails.setAttribute("PIECE_NO", resultSetTemp.getString("PIECE_NO"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT_CODE", resultSetTemp.getString("PRODUCT_CODE"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT_DESC", resultSetTemp.getString("PRODUCT_DESC"));
                ObjFeltSalesOrderDetails.setAttribute("S_GROUP", resultSetTemp.getString("S_GROUP"));
                ObjFeltSalesOrderDetails.setAttribute("LENGTH", resultSetTemp.getString("LENGTH"));
                ObjFeltSalesOrderDetails.setAttribute("WIDTH", resultSetTemp.getString("WIDTH"));
                ObjFeltSalesOrderDetails.setAttribute("GSM", resultSetTemp.getString("GSM"));
                ObjFeltSalesOrderDetails.setAttribute("THORITICAL_WIDTH", resultSetTemp.getString("THORITICAL_WIDTH"));
                ObjFeltSalesOrderDetails.setAttribute("SQ_MTR", resultSetTemp.getString("SQ_MTR"));
                ObjFeltSalesOrderDetails.setAttribute("STYLE", resultSetTemp.getString("STYLE"));
                ObjFeltSalesOrderDetails.setAttribute("REQ_MONTH", resultSetTemp.getString("REQ_MONTH"));
                ObjFeltSalesOrderDetails.setAttribute("SYN_PER", resultSetTemp.getString("SYN_PER"));
                ObjFeltSalesOrderDetails.setAttribute("REMARK", resultSetTemp.getString("REMARK"));
                ObjFeltSalesOrderDetails.setAttribute("OV_RATE", resultSetTemp.getString("OV_RATE"));
                ObjFeltSalesOrderDetails.setAttribute("OV_BAS_AMOUNT", resultSetTemp.getString("OV_BAS_AMOUNT"));
                ObjFeltSalesOrderDetails.setAttribute("OV_CHEM_TRT_CHG", resultSetTemp.getString("OV_CHEM_TRT_CHG"));
                ObjFeltSalesOrderDetails.setAttribute("OV_SPIRAL_CHG", resultSetTemp.getString("OV_SPIRAL_CHG"));
                ObjFeltSalesOrderDetails.setAttribute("OV_PIN_CHG", resultSetTemp.getString("OV_PIN_CHG"));
                ObjFeltSalesOrderDetails.setAttribute("OV_SEAM_CHG", resultSetTemp.getString("OV_SEAM_CHG"));
                ObjFeltSalesOrderDetails.setAttribute("OV_INS_IND", resultSetTemp.getString("OV_INS_IND"));
                ObjFeltSalesOrderDetails.setAttribute("OV_INS_AMT", resultSetTemp.getString("OV_INS_AMT"));
                ObjFeltSalesOrderDetails.setAttribute("OV_EXCISE", resultSetTemp.getString("OV_EXCISE"));
                ObjFeltSalesOrderDetails.setAttribute("OV_DISC_PER", resultSetTemp.getString("OV_DISC_PER"));
                ObjFeltSalesOrderDetails.setAttribute("OV_DISC_AMT", resultSetTemp.getString("OV_DISC_AMT"));
                ObjFeltSalesOrderDetails.setAttribute("OV_DISC_BASAMT", resultSetTemp.getString("OV_DISC_BASAMT"));
                ObjFeltSalesOrderDetails.setAttribute("OV_AMT", resultSetTemp.getString("OV_AMT"));

                hmFeltSalesOrderDetails.put(Integer.toString(serialNoCounter), ObjFeltSalesOrderDetails);
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
        // String stringProductionDate1 =EITLERPGLOBAL.formatDate(stringProductionDate);

        try {
            stTmp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);

            rsTmp = stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_SAMPLE_ORDER_HEADER_H WHERE DOC_NO='" + productionDocumentNo + "'");
            while (rsTmp.next()) {
                clsFeltSampleOrder felt_order = new clsFeltSampleOrder();

                felt_order.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                felt_order.setAttribute("UPDATED_BY", rsTmp.getString("UPDATED_BY"));
                felt_order.setAttribute("UPDATED_DATE", rsTmp.getString("UPDATED_DATE"));
                felt_order.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                felt_order.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                felt_order.setAttribute("APPROVER_REMARKS", rsTmp.getString("APPROVER_REMARKS"));
                felt_order.setAttribute("FROM_IP", rsTmp.getString("FROM_IP"));

                hmHistoryList.put(Integer.toString(hmHistoryList.size() + 1), felt_order);
            }

            rsTmp.close();
            stTmp.close();
            return hmHistoryList;
        } catch (Exception e) {
            e.printStackTrace();
            return hmHistoryList;
        }
    }

    public boolean ShowHistory(String pDocNo) {
        Ready = false;
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_SAMPLE_ORDER_HEADER_H WHERE DOC_NO ='" + pDocNo + "'");
            Ready = true;

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
                strSQL = "SELECT DISTINCT H.DOC_NO,H.DOC_DATE,RECEIVED_DATE,PARTY_CODE FROM PRODUCTION.FELT_SALES_SAMPLE_ORDER_HEADER H, PRODUCTION.FELT_PROD_DOC_DATA D  WHERE H.DOC_NO=D.DOC_NO AND STATUS='W' AND MODULE_ID=" + ModuleID + " AND CANCELED=0  AND USER_ID=" + pUserID + " ORDER BY RECEIVED_DATE,H.DOC_NO";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT H.DOC_NO,H.DOC_DATE,RECEIVED_DATE,PARTY_CODE FROM PRODUCTION.FELT_SALES_SAMPLE_ORDER_HEADER H, PRODUCTION.FELT_PROD_DOC_DATA D   WHERE H.DOC_NO=D.DOC_NO AND STATUS='W' AND MODULE_ID=" + ModuleID + " AND CANCELED=0  AND USER_ID=" + pUserID + " ORDER BY RECEIVED_DATE,H.DOC_NO";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT H.DOC_NO,H.DOC_DATE,RECEIVED_DATE,PARTY_CODE FROM PRODUCTION.FELT_SALES_SAMPLE_ORDER_HEADER H, PRODUCTION.FELT_PROD_DOC_DATA D   WHERE H.DOC_NO=D.DOC_NO AND STATUS='W' AND MODULE_ID=" + ModuleID + " AND CANCELED=0  AND USER_ID=" + pUserID + "  ORDER BY H.DOC_NO";
            }

            ResultSet rsTmp = data.getConn().createStatement().executeQuery(strSQL);

            Counter = 0;
            while (rsTmp.next()) {

                Counter = Counter + 1;
                clsFeltSampleOrder ObjDoc = new clsFeltSampleOrder();

                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO", rsTmp.getString("DOC_NO"));
                ObjDoc.setAttribute("DOC_DATE", rsTmp.getString("DOC_DATE"));
                ObjDoc.setAttribute("RECEIVED_DATE", rsTmp.getString("RECEIVED_DATE"));
                ObjDoc.setAttribute("PARTY_CODE", rsTmp.getString("PARTY_CODE"));
                // ----------------- End of Header Fields ------------------------------------//

                List.put(Integer.toString(Counter), ObjDoc);
            }
            rsTmp.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return List;
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

    public static boolean CanCancel(String pDocNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean CanCancel = false;

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            System.out.println("SELECT DOC_NO FROM PRODUCTION.FELT_SALES_SAMPLE_ORDER_HEADER WHERE DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");
            rsTmp = stTmp.executeQuery("SELECT DOC_NO FROM PRODUCTION.FELT_SALES_SAMPLE_ORDER_HEADER WHERE DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");

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

    public static void CancelDoc(String pAmendNo) {
        ResultSet rsTmp = null;

        if (CanCancel(pAmendNo)) {

            boolean Approved = false;

            try {
                rsTmp = data.getResult("SELECT APPROVED FROM PRODUCTION.FELT_SALES_SAMPLE_ORDER_HEADER WHERE DOC_NO='" + pAmendNo + "' ");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    Approved = rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();

                if (!Approved) {
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='" + pAmendNo + "' AND MODULE_ID=861");
                }
                data.Execute("UPDATE PRODUCTION.FELT_SALES_SAMPLE_ORDER_HEADER SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='" + pAmendNo + "'");
                //String Original_Piece_No = data.getStringValueFromDB("SELECT ORIGINAL_PIECE_NO FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION WHERE DOC_NO='"+pAmendNo+"'");
                //data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_DIVERSION_FLAG='READY' WHERE PR_PIECE_NO='"+Original_Piece_No+"'");

            } catch (Exception e) {

            }
        }

    }
}
