/*
 * clsFeltPieceAmend.java
 *
 * Created on March 12, 2013, 3:10 PM
 */
package EITLERP.FeltSales.FeltPDCAmend;

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
public class clsFeltPDCAmend {

    public String LastError = "";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public HashMap props;
    public boolean Ready = false;
    //Felt Order Collection
    public HashMap hmFeltPDCDetails;

    public HashMap colPDCBankItems = new HashMap();
    public HashMap colPDCPieceItems = new HashMap();

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
    public clsFeltPDCAmend() {
        LastError = "";
        props = new HashMap();
        props.put("PDC_AMEND_NO", new Variant(""));
        props.put("PDC_AMEND_DATE", new Variant(""));
        props.put("PDC_DOC_NO", new Variant(""));
        props.put("PDC_DOC_DATE", new Variant(""));
        props.put("PDC_PARTY_CODE", new Variant(""));
        props.put("PDC_PARTY_NAME", new Variant(""));
        props.put("PDC_PARTY_CREDIT_DAYS", new Variant(""));
        props.put("PDC_MANUAL_AMOUNT", new Variant(0.00));
        props.put("PDC_CRITICAL_AMOUNT", new Variant(0.00));
        props.put("PDC_CARRY_FORWARD_AMOUNT", new Variant(0.00));
        props.put("PDC_UNADJUSTED_CREDIT", new Variant(0.00));
        props.put("PDC_TOTAL_AMOUNT", new Variant(0.00));
        props.put("PDC_TOTAL_PIECE_AMOUNT", new Variant(0.00));
        props.put("PDC_TOTAL_BALANCE", new Variant(0.00));
        props.put("PDC_REMARK", new Variant(""));
        
        props.put("APPROVED", new Variant(false));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("APPROVED_ON", new Variant(""));
        props.put("REJECTED", new Variant(false));
        props.put("REJECTED_DATE", new Variant(""));
        
        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("PREFIX", new Variant(""));

        // -- Approval Specific Fields --
        props.put("HIERARCHY_ID", new Variant(0));
        props.put("FROM", new Variant(0));
        props.put("TO", new Variant(0));
        props.put("REMARKS", new Variant(""));
        props.put("APPROVAL_STATUS", new Variant(""));

        hmFeltPDCDetails = new HashMap();
    }

    public boolean LoadData() {
        Ready = false;
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_PDC_AMEND_HEADER WHERE PDC_AMEND_DATE >= '" + EITLERPGLOBAL.FinFromDateDB + "' AND PDC_AMEND_DATE<='" + EITLERPGLOBAL.FinToDateDB + "' ORDER BY PDC_AMEND_NO,PDC_AMEND_DATE ");
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
//            if (HistoryView) {
//                setHistoryData(historyAmendDate, historyAmendID);
//            } else {
                setData();
//            }
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
//            if (HistoryView) {
//                setHistoryData(historyAmendDate, historyAmendID);
//            } else {
                setData();
//            }
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
//            if (HistoryView) {
//                setHistoryData(historyAmendDate, historyAmendID);
//            } else {
                setData();
//            }
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
//            if (HistoryView) {
//                setHistoryData(historyAmendDate, historyAmendID);
//            } else {
                setData();
//            }
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    public boolean Insert() {
        ResultSet rsHeader, rsHHeader, rsBankDetail, rsHBankDetail, rsPieceDetail, rsHPieceDetail;
        Statement stHeader, stHHeader, stBankDetail, stHBankDetail, stPieceDetail, stHPieceDetail;
        try {
            // Felt PDC Header data connection
            stHeader = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHeader = stHeader.executeQuery("SELECT * FROM  PRODUCTION.FELT_PDC_AMEND_HEADER WHERE PDC_AMEND_NO=''");
            rsHeader.first();

            // Felt PDC Header data history connection
            stHHeader = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHHeader = stHHeader.executeQuery("SELECT * FROM  PRODUCTION.FELT_PDC_AMEND_HEADER_H WHERE PDC_AMEND_NO=''");
            rsHHeader.first();

//            // Felt PDC Bank Detail data connection
//            stBankDetail = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
//            rsBankDetail = stBankDetail.executeQuery("SELECT * FROM  PRODUCTION.FELT_PDC_BANK_DETAIL WHERE PDC_DOC_NO=''");
//            rsBankDetail.first();
//
//            // Felt PDC Bank Detail data history connection
//            stHBankDetail = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
//            rsHBankDetail = stHBankDetail.executeQuery("SELECT * FROM  PRODUCTION.FELT_PDC_BANK_DETAIL_H WHERE PDC_DOC_NO=''");
//            rsHBankDetail.first();

            // Felt PDC Piece Detail data connection
            stPieceDetail = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsPieceDetail = stPieceDetail.executeQuery("SELECT * FROM  PRODUCTION.FELT_PDC_PIECE_AMEND_DETAIL WHERE PDC_AMEND_NO=''");
            rsPieceDetail.first();

            // Felt PDC Piece Detail data history connection
            stHPieceDetail = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHPieceDetail = stHPieceDetail.executeQuery("SELECT * FROM  PRODUCTION.FELT_PDC_PIECE_AMEND_DETAIL_H WHERE PDC_AMEND_NO=''");
            rsHPieceDetail.first();

            // Felt PDC Last Free No, 
            setAttribute("PDC_AMEND_NO", getNextFreeNo(626, true));

            //Now Insert records into PDC Header & History tables            
            rsHeader.moveToInsertRow();

            rsHeader.updateString("PDC_AMEND_NO", (String) getAttribute("PDC_AMEND_NO").getObj());
            rsHeader.updateString("PDC_AMEND_DATE", (String) getAttribute("PDC_AMEND_DATE").getObj());
            rsHeader.updateString("PDC_DOC_NO", (String) getAttribute("PDC_DOC_NO").getObj());
            rsHeader.updateString("PDC_DOC_DATE", (String) getAttribute("PDC_DOC_DATE").getObj());
            rsHeader.updateString("PDC_PARTY_CODE", (String) getAttribute("PDC_PARTY_CODE").getObj());
            rsHeader.updateString("PDC_PARTY_NAME", (String) getAttribute("PDC_PARTY_NAME").getObj());
            rsHeader.updateString("PDC_PARTY_CREDIT_DAYS", (String) getAttribute("PDC_PARTY_CREDIT_DAYS").getObj());
            rsHeader.updateDouble("PDC_MANUAL_AMOUNT", getAttribute("PDC_MANUAL_AMOUNT").getVal());
            rsHeader.updateDouble("PDC_CRITICAL_AMOUNT", getAttribute("PDC_CRITICAL_AMOUNT").getVal());
            rsHeader.updateDouble("PDC_CARRY_FORWARD_AMOUNT", getAttribute("PDC_CARRY_FORWARD_AMOUNT").getVal());
            rsHeader.updateDouble("PDC_UNADJUSTED_CREDIT", getAttribute("PDC_UNADJUSTED_CREDIT").getVal());
            rsHeader.updateDouble("PDC_TOTAL_AMOUNT", getAttribute("PDC_TOTAL_AMOUNT").getVal());
            rsHeader.updateDouble("PDC_TOTAL_PIECE_AMOUNT", getAttribute("PDC_TOTAL_PIECE_AMOUNT").getVal());
            rsHeader.updateDouble("PDC_TOTAL_BALANCE", getAttribute("PDC_TOTAL_BALANCE").getVal());
            rsHeader.updateString("PDC_REMARK", (String) getAttribute("PDC_REMARK").getObj());

            rsHeader.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
            rsHeader.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
            rsHeader.updateInt("MODIFIED_BY", 0);
            rsHeader.updateString("MODIFIED_DATE", "0000-00-00");
            rsHeader.updateBoolean("APPROVED", false);
            rsHeader.updateString("APPROVED_DATE", "0000-00-00");
            rsHeader.updateBoolean("REJECTED", false);
            rsHeader.updateString("REJECTED_DATE", "0000-00-00");
            rsHeader.updateInt("CANCELED", 0);
            rsHeader.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsHeader.updateInt("CHANGED", 1);
            rsHeader.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

            rsHeader.insertRow();

            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHHeader.moveToInsertRow();

            rsHHeader.updateInt("REVISION_NO", 1); //Revision No. 1 in case of Insert
            rsHHeader.updateInt("UPDATED_BY", (int) getAttribute("CREATED_BY").getVal());
            rsHHeader.updateString("APPROVAL_STATUS", (String) getAttribute("APPROVAL_STATUS").getObj());
            rsHHeader.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHHeader.updateString("APPROVER_REMARKS", (String) getAttribute("FROM_REMARKS").getObj());

            rsHHeader.updateString("PDC_AMEND_NO", (String) getAttribute("PDC_AMEND_NO").getObj());
            rsHHeader.updateString("PDC_AMEND_DATE", (String) getAttribute("PDC_AMEND_DATE").getObj());
            rsHHeader.updateString("PDC_DOC_NO", (String) getAttribute("PDC_DOC_NO").getObj());
            rsHHeader.updateString("PDC_DOC_DATE", (String) getAttribute("PDC_DOC_DATE").getObj());
            rsHHeader.updateString("PDC_PARTY_CODE", (String) getAttribute("PDC_PARTY_CODE").getObj());
            rsHHeader.updateString("PDC_PARTY_NAME", (String) getAttribute("PDC_PARTY_NAME").getObj());
            rsHHeader.updateString("PDC_PARTY_CREDIT_DAYS", (String) getAttribute("PDC_PARTY_CREDIT_DAYS").getObj());
            rsHHeader.updateDouble("PDC_MANUAL_AMOUNT", getAttribute("PDC_MANUAL_AMOUNT").getVal());
            rsHHeader.updateDouble("PDC_CRITICAL_AMOUNT", getAttribute("PDC_CRITICAL_AMOUNT").getVal());
            rsHHeader.updateDouble("PDC_CARRY_FORWARD_AMOUNT", getAttribute("PDC_CARRY_FORWARD_AMOUNT").getVal());
            rsHHeader.updateDouble("PDC_UNADJUSTED_CREDIT", getAttribute("PDC_UNADJUSTED_CREDIT").getVal());
            rsHHeader.updateDouble("PDC_TOTAL_AMOUNT", getAttribute("PDC_TOTAL_AMOUNT").getVal());
            rsHHeader.updateDouble("PDC_TOTAL_PIECE_AMOUNT", getAttribute("PDC_TOTAL_PIECE_AMOUNT").getVal());
            rsHHeader.updateDouble("PDC_TOTAL_BALANCE", getAttribute("PDC_TOTAL_BALANCE").getVal());
            rsHHeader.updateString("PDC_REMARK", (String) getAttribute("PDC_REMARK").getObj());

            rsHHeader.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
            rsHHeader.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
            rsHHeader.updateInt("MODIFIED_BY", 0);
            rsHHeader.updateString("MODIFIED_DATE", "0000-00-00");
            rsHHeader.updateBoolean("APPROVED", false);
            rsHHeader.updateString("APPROVED_DATE", "0000-00-00");
            rsHHeader.updateBoolean("REJECTED", false);
            rsHHeader.updateString("REJECTED_DATE", "0000-00-00");
            rsHHeader.updateInt("CANCELED", 0);
            rsHHeader.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsHHeader.updateInt("CHANGED", 1);
            rsHHeader.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");

            rsHHeader.updateString("FROM_IP", "" + str_split[1]);

            rsHHeader.insertRow();

//            //Now Insert records into PDC Bank Detail & History tables
//            for (int i = 1; i <= colPDCBankItems.size(); i++) {
//                clsFeltPDCBankDetails ObjBankItem = (clsFeltPDCBankDetails) colPDCBankItems.get(Integer.toString(i));
//
//                rsBankDetail.moveToInsertRow();
//
//                rsBankDetail.updateString("PDC_DOC_NO", (String) getAttribute("PDC_DOC_NO").getObj());
//                rsBankDetail.updateString("PDC_BANK_CD", (String) ObjBankItem.getAttribute("PDC_BANK_CD").getObj());
//                rsBankDetail.updateString("PDC_BANK_NAME", (String) ObjBankItem.getAttribute("PDC_BANK_NAME").getObj());
//                rsBankDetail.updateString("PDC_BANK_BRANCH", (String) ObjBankItem.getAttribute("PDC_BANK_BRANCH").getObj());
//                rsBankDetail.updateString("PDC_CHEQUE_NO", (String) ObjBankItem.getAttribute("PDC_CHEQUE_NO").getObj());
//                rsBankDetail.updateString("PDC_CHEQUE_DATE", (String) ObjBankItem.getAttribute("PDC_CHEQUE_DATE").getObj());
//                rsBankDetail.updateDouble("PDC_CHEQUE_AMOUNT", EITLERPGLOBAL.round(ObjBankItem.getAttribute("PDC_CHEQUE_AMOUNT").getVal(), 2));
//                rsBankDetail.updateString("PDC_PHYSICAL_SCANNED", (String) ObjBankItem.getAttribute("PDC_PHYSICAL_SCANNED").getObj());
//                rsBankDetail.updateString("PDC_BILLING_DATE", (String) ObjBankItem.getAttribute("PDC_BILLING_DATE").getObj());
//                rsBankDetail.updateDouble("PDC_UTILIZED_AMOUNT", EITLERPGLOBAL.round(ObjBankItem.getAttribute("PDC_UTILIZED_AMOUNT").getVal(), 2));
//                rsBankDetail.updateDouble("PDC_BALANCE_AMOUNT", EITLERPGLOBAL.round(ObjBankItem.getAttribute("PDC_BALANCE_AMOUNT").getVal(), 2));
//                rsBankDetail.updateString("PDC_CLOSED", (String) ObjBankItem.getAttribute("PDC_CLOSED").getObj());
//
//                rsBankDetail.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
//                rsBankDetail.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
//                rsBankDetail.updateInt("MODIFIED_BY", 0);
//                rsBankDetail.updateString("MODIFIED_DATE", "0000-00-00");
//                rsBankDetail.updateBoolean("APPROVED", false);
//                rsBankDetail.updateString("APPROVED_DATE", "0000-00-00");
//                rsBankDetail.updateBoolean("REJECTED", false);
//                rsBankDetail.updateString("REJECTED_DATE", "0000-00-00");
//                rsBankDetail.updateInt("CANCELED", 0);
//                rsBankDetail.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
//                rsBankDetail.updateInt("CHANGED", 1);
//                rsBankDetail.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
//
//                rsBankDetail.insertRow();
//
//                rsHBankDetail.moveToInsertRow();
//
//                rsHBankDetail.updateInt("REVISION_NO", 1); //Revision No. 1 in case of Insert
//                rsHBankDetail.updateInt("UPDATED_BY", (int) getAttribute("CREATED_BY").getVal());
//                rsHBankDetail.updateString("APPROVAL_STATUS", (String) getAttribute("APPROVAL_STATUS").getObj());
//                rsHBankDetail.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
//                rsHBankDetail.updateString("APPROVER_REMARKS", (String) getAttribute("FROM_REMARKS").getObj());
//
//                rsHBankDetail.updateString("PDC_DOC_NO", (String) getAttribute("PDC_DOC_NO").getObj());
//                rsHBankDetail.updateString("PDC_BANK_CD", (String) ObjBankItem.getAttribute("PDC_BANK_CD").getObj());
//                rsHBankDetail.updateString("PDC_BANK_NAME", (String) ObjBankItem.getAttribute("PDC_BANK_NAME").getObj());
//                rsHBankDetail.updateString("PDC_BANK_BRANCH", (String) ObjBankItem.getAttribute("PDC_BANK_BRANCH").getObj());
//                rsHBankDetail.updateString("PDC_CHEQUE_NO", (String) ObjBankItem.getAttribute("PDC_CHEQUE_NO").getObj());
//                rsHBankDetail.updateString("PDC_CHEQUE_DATE", (String) ObjBankItem.getAttribute("PDC_CHEQUE_DATE").getObj());
//                rsHBankDetail.updateDouble("PDC_CHEQUE_AMOUNT", EITLERPGLOBAL.round(ObjBankItem.getAttribute("PDC_CHEQUE_AMOUNT").getVal(), 2));
//                rsHBankDetail.updateString("PDC_PHYSICAL_SCANNED", (String) ObjBankItem.getAttribute("PDC_PHYSICAL_SCANNED").getObj());
//                rsHBankDetail.updateString("PDC_BILLING_DATE", (String) ObjBankItem.getAttribute("PDC_BILLING_DATE").getObj());
//                rsHBankDetail.updateDouble("PDC_UTILIZED_AMOUNT", EITLERPGLOBAL.round(ObjBankItem.getAttribute("PDC_UTILIZED_AMOUNT").getVal(), 2));
//                rsHBankDetail.updateDouble("PDC_BALANCE_AMOUNT", EITLERPGLOBAL.round(ObjBankItem.getAttribute("PDC_BALANCE_AMOUNT").getVal(), 2));
//                rsHBankDetail.updateString("PDC_CLOSED", (String) ObjBankItem.getAttribute("PDC_CLOSED").getObj());
//
//                rsHBankDetail.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
//                rsHBankDetail.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
//                rsHBankDetail.updateInt("MODIFIED_BY", 0);
//                rsHBankDetail.updateString("MODIFIED_DATE", "0000-00-00");
//                rsHBankDetail.updateBoolean("APPROVED", false);
//                rsHBankDetail.updateString("APPROVED_DATE", "0000-00-00");
//                rsHBankDetail.updateBoolean("REJECTED", false);
//                rsHBankDetail.updateString("REJECTED_DATE", "0000-00-00");
//                rsHBankDetail.updateInt("CANCELED", 0);
//                rsHBankDetail.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
//                rsHBankDetail.updateInt("CHANGED", 1);
//                rsHBankDetail.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
//                ResultSet rsTmp1 = data.getResult("SELECT USER()");
//                rsTmp1.first();
//                String str1 = rsTmp1.getString(1);
//                String str1_split[] = str1.split("@");
//
//                rsHBankDetail.updateString("FROM_IP", "" + str1_split[1]);
//
//                rsHBankDetail.insertRow();
//
//            }

            //Now Insert records into PDC Piece Detail & History tables
            for (int i = 1; i <= colPDCPieceItems.size(); i++) {
                clsFeltPDCPieceAmendDetails ObjPieceItem = (clsFeltPDCPieceAmendDetails) colPDCPieceItems.get(Integer.toString(i));

                rsPieceDetail.moveToInsertRow();

                rsPieceDetail.updateString("PDC_AMEND_NO", (String) getAttribute("PDC_AMEND_NO").getObj());
                rsPieceDetail.updateString("PDC_DOC_NO", (String) getAttribute("PDC_DOC_NO").getObj());
                rsPieceDetail.updateString("PDC_PIECE_NO", (String) ObjPieceItem.getAttribute("PDC_PIECE_NO").getObj());
                rsPieceDetail.updateDouble("PDC_PIECE_AMOUNT", EITLERPGLOBAL.round(ObjPieceItem.getAttribute("PDC_PIECE_AMOUNT").getVal(), 2));
                rsPieceDetail.updateString("PDC_PIECE_STATUS", (String) ObjPieceItem.getAttribute("PDC_PIECE_STATUS").getObj());
                rsPieceDetail.updateString("PDC_PIECE_AMEND_NO", (String) ObjPieceItem.getAttribute("PDC_PIECE_AMEND_NO").getObj());
                rsPieceDetail.updateString("PDC_PIECE_AMEND_DATE", (String) ObjPieceItem.getAttribute("PDC_PIECE_AMEND_DATE").getObj());
                
                rsPieceDetail.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                rsPieceDetail.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                rsPieceDetail.updateInt("MODIFIED_BY", 0);
                rsPieceDetail.updateString("MODIFIED_DATE", "0000-00-00");
                rsPieceDetail.updateBoolean("APPROVED", false);
                rsPieceDetail.updateString("APPROVED_DATE", "0000-00-00");
                rsPieceDetail.updateBoolean("REJECTED", false);
                rsPieceDetail.updateString("REJECTED_DATE", "0000-00-00");
                rsPieceDetail.updateInt("CANCELED", 0);
                rsPieceDetail.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                rsPieceDetail.updateInt("CHANGED", 1);
                rsPieceDetail.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

                rsPieceDetail.insertRow();

                rsHPieceDetail.moveToInsertRow();

                rsHPieceDetail.updateInt("REVISION_NO", 1); //Revision No. 1 in case of Insert
                rsHPieceDetail.updateInt("UPDATED_BY", (int) getAttribute("CREATED_BY").getVal());
                rsHPieceDetail.updateString("APPROVAL_STATUS", (String) getAttribute("APPROVAL_STATUS").getObj());
                rsHPieceDetail.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHPieceDetail.updateString("APPROVER_REMARKS", (String) getAttribute("FROM_REMARKS").getObj());

                rsHPieceDetail.updateString("PDC_AMEND_NO", (String) getAttribute("PDC_AMEND_NO").getObj());
                rsHPieceDetail.updateString("PDC_DOC_NO", (String) getAttribute("PDC_DOC_NO").getObj());
                rsHPieceDetail.updateString("PDC_PIECE_NO", (String) ObjPieceItem.getAttribute("PDC_PIECE_NO").getObj());
                rsHPieceDetail.updateDouble("PDC_PIECE_AMOUNT", EITLERPGLOBAL.round(ObjPieceItem.getAttribute("PDC_PIECE_AMOUNT").getVal(), 2));
                rsHPieceDetail.updateString("PDC_PIECE_STATUS", (String) ObjPieceItem.getAttribute("PDC_PIECE_STATUS").getObj());
                rsHPieceDetail.updateString("PDC_PIECE_AMEND_NO", (String) ObjPieceItem.getAttribute("PDC_PIECE_AMEND_NO").getObj());
                rsHPieceDetail.updateString("PDC_PIECE_AMEND_DATE", (String) ObjPieceItem.getAttribute("PDC_PIECE_AMEND_DATE").getObj());
                
                rsHPieceDetail.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                rsHPieceDetail.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                rsHPieceDetail.updateInt("MODIFIED_BY", 0);
                rsHPieceDetail.updateString("MODIFIED_DATE", "0000-00-00");
                rsHPieceDetail.updateBoolean("APPROVED", false);
                rsHPieceDetail.updateString("APPROVED_DATE", "0000-00-00");
                rsHPieceDetail.updateBoolean("REJECTED", false);
                rsHPieceDetail.updateString("REJECTED_DATE", "0000-00-00");
                rsHPieceDetail.updateInt("CANCELED", 0);
                rsHPieceDetail.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                rsHPieceDetail.updateInt("CHANGED", 1);
                rsHPieceDetail.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                ResultSet rsTmp1 = data.getResult("SELECT USER()");
                rsTmp1.first();
                String str1 = rsTmp1.getString(1);
                String str1_split[] = str1.split("@");

                rsHPieceDetail.updateString("FROM_IP", "" + str1_split[1]);

                rsHPieceDetail.insertRow();
            }

            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = 626; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo = (String) getAttribute("PDC_AMEND_NO").getObj();
            ObjFeltProductionApprovalFlow.DocDate = (String) getAttribute("PDC_AMEND_DATE").getObj();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.FELT_PDC_AMEND_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator = true;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "PDC_AMEND_NO";

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
            // Update in Bank Detail and Piece Detail Table To confirm that PDC has completed
            if (ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {
                ObjFeltProductionApprovalFlow.finalApproved = false;
                //data.Execute("UPDATE PRODUCTION.FELT_PDC_BANK_DETAIL SET APPROVED=1,APPROVED_DATE=CURRENT_TIMESTAMP,CHANGED=1 WHERE PDC_DOC_NO='"+ObjFeltProductionApprovalFlow.DocNo+"'");
                data.Execute("UPDATE PRODUCTION.FELT_PDC_PIECE_AMEND_DETAIL SET APPROVED=1,APPROVED_DATE=CURRENT_TIMESTAMP,CHANGED=1 WHERE PDC_AMEND_NO='"+ObjFeltProductionApprovalFlow.DocNo+"'");
                
                String sql = "UPDATE PRODUCTION.FELT_PDC_PIECE_DETAIL "
                        + "SET PDC_PIECE_STATUS='DELETE',PDC_PIECE_AMEND_DATE='"+(String) getAttribute("PDC_AMEND_DATE").getObj()+"',"
                        + "PDC_PIECE_AMEND_NO='" + (String) getAttribute("PDC_AMEND_NO").getObj() + "' "
                        + "WHERE PDC_PIECE_NO IN ( SELECT D.PDC_PIECE_NO "
                        + "FROM (SELECT  * FROM PRODUCTION.FELT_PDC_PIECE_DETAIL "
                        + "WHERE PDC_DOC_NO IN (SELECT DISTINCT PDC_DOC_NO FROM PRODUCTION.FELT_PDC_PIECE_AMEND_DETAIL "
                        + "WHERE PDC_DOC_NO='" + (String) getAttribute("PDC_DOC_NO").getObj() + "' "
                        + "AND PDC_AMEND_NO='" + (String) getAttribute("PDC_AMEND_NO").getObj() + "')) AS D "
                        + "LEFT JOIN (SELECT * FROM PRODUCTION.FELT_PDC_PIECE_AMEND_DETAIL "
                        + " WHERE PDC_DOC_NO='" + (String) getAttribute("PDC_DOC_NO").getObj() + "' "
                        + "AND PDC_AMEND_NO='" + (String) getAttribute("PDC_AMEND_NO").getObj() + "') AS  A "
                        + "ON D.PDC_DOC_NO=A.PDC_DOC_NO AND D.PDC_PIECE_NO=A.PDC_PIECE_NO "
                        + "WHERE A.PDC_PIECE_NO IS NULL)";
                System.out.println("Deleted Piece Query:" + sql);
                data.Execute(sql);
                sql = "UPDATE PRODUCTION.FELT_PDC_PIECE_DETAIL_H "
                        + "SET PDC_PIECE_STATUS='DELETE',PDC_PIECE_AMEND_DATE='"+(String) getAttribute("PDC_AMEND_DATE").getObj()+"',"
                        + "PDC_PIECE_AMEND_NO='" + (String) getAttribute("PDC_AMEND_NO").getObj() + "' "
                        + "WHERE PDC_PIECE_NO IN ( SELECT D.PDC_PIECE_NO "
                        + "FROM (SELECT  * FROM PRODUCTION.FELT_PDC_PIECE_DETAIL "
                        + "WHERE PDC_DOC_NO IN (SELECT DISTINCT PDC_DOC_NO FROM PRODUCTION.FELT_PDC_PIECE_AMEND_DETAIL "
                        + "WHERE PDC_DOC_NO='" + (String) getAttribute("PDC_DOC_NO").getObj() + "' "
                        + "AND PDC_AMEND_NO='" + (String) getAttribute("PDC_AMEND_NO").getObj() + "')) AS D "
                        + "LEFT JOIN (SELECT * FROM PRODUCTION.FELT_PDC_PIECE_AMEND_DETAIL "
                        + " WHERE PDC_DOC_NO='" + (String) getAttribute("PDC_DOC_NO").getObj() + "' "
                        + "AND PDC_AMEND_NO='" + (String) getAttribute("PDC_AMEND_NO").getObj() + "') AS  A "
                        + "ON D.PDC_DOC_NO=A.PDC_DOC_NO AND D.PDC_PIECE_NO=A.PDC_PIECE_NO "
                        + "WHERE A.PDC_PIECE_NO IS NULL)";
                System.out.println("Deleted Piece Query History:" + sql);
                data.Execute(sql);
                sql = "INSERT INTO PRODUCTION.FELT_PDC_PIECE_DETAIL "
                        + "SELECT BB.PDC_DOC_NO,AA.PDC_PIECE_NO,AA.PDC_PIECE_AMOUNT,'ADD','" + (String) getAttribute("PDC_AMEND_NO").getObj() + "', "
                        + "'" + (String) getAttribute("PDC_AMEND_DATE").getObj() + "',AA.REMARKS,AA.CREATED_BY,AA.CREATED_DATE,AA.MODIFIED_BY,AA.MODIFIED_DATE, "
                        + "AA.APPROVED,AA.APPROVED_DATE,AA.REJECTED,AA.REJECTED_DATE,AA.REJECTED_REMARKS,AA.CANCELED,AA.HIERARCHY_ID, "
                        + "'1',CURDATE() "
                        + " FROM ("
                        + "SELECT D.* "
                        + "FROM (SELECT  * FROM PRODUCTION.FELT_PDC_PIECE_AMEND_DETAIL "
                        + "WHERE PDC_DOC_NO='" + (String) getAttribute("PDC_DOC_NO").getObj() + "' "
                        + "AND PDC_AMEND_NO='" + (String) getAttribute("PDC_AMEND_NO").getObj() + "' "
                        + "AND PDC_DOC_NO IN (SELECT DISTINCT PDC_DOC_NO FROM PRODUCTION.FELT_PDC_PIECE_DETAIL)) AS D "
                        + "LEFT JOIN (SELECT * FROM PRODUCTION.FELT_PDC_PIECE_DETAIL) AS  A "
                        + "ON D.PDC_DOC_NO=A.PDC_DOC_NO AND D.PDC_PIECE_NO=A.PDC_PIECE_NO "
                        + "WHERE A.PDC_PIECE_NO IS NULL) AS AA "
                        + "LEFT JOIN (SELECT DISTINCT PDC_DOC_NO FROM PRODUCTION.FELT_PDC_HEADER) AS BB "
                        + "ON AA.PDC_DOC_NO=BB.PDC_DOC_NO";
                System.out.println("Add Piece Query :" + sql);
                data.Execute(sql);
                sql = "INSERT INTO PRODUCTION.FELT_PDC_PIECE_DETAIL_H "
                        + "SELECT 999,BB.PDC_DOC_NO,AA.PDC_PIECE_NO,AA.PDC_PIECE_AMOUNT,'ADD','" + (String) getAttribute("PDC_AMEND_NO").getObj() + "', "
                        + "'" + (String) getAttribute("PDC_AMEND_DATE").getObj() + "',AA.REMARKS,AA.CREATED_BY,AA.CREATED_DATE,AA.MODIFIED_BY,AA.MODIFIED_DATE, "
                        + "AA.APPROVED,AA.APPROVED_DATE,AA.REJECTED,AA.REJECTED_DATE,AA.REJECTED_REMARKS,AA.CANCELED,AA.HIERARCHY_ID, "
                        + "'1',CURDATE(),'"+(int) getAttribute("CREATED_BY").getVal()+"','"+(String) getAttribute("APPROVAL_STATUS").getObj()+"', "
                        + "'"+EITLERPGLOBAL.getCurrentDateDB()+"','"+(String) getAttribute("FROM_REMARKS").getObj()+"','" + str_split[1] + "' "
                        + " FROM ("
                        + "SELECT D.* "
                        + "FROM (SELECT  * FROM PRODUCTION.FELT_PDC_PIECE_AMEND_DETAIL "
                        + "WHERE PDC_DOC_NO='" + (String) getAttribute("PDC_DOC_NO").getObj() + "' "
                        + "AND PDC_AMEND_NO='" + (String) getAttribute("PDC_AMEND_NO").getObj() + "' "
                        + "AND PDC_DOC_NO IN (SELECT DISTINCT PDC_DOC_NO FROM PRODUCTION.FELT_PDC_PIECE_DETAIL_H)) AS D "
                        + "LEFT JOIN (SELECT * FROM PRODUCTION.FELT_PDC_PIECE_DETAIL_H) AS  A "
                        + "ON D.PDC_DOC_NO=A.PDC_DOC_NO AND D.PDC_PIECE_NO=A.PDC_PIECE_NO "
                        + "WHERE A.PDC_PIECE_NO IS NULL) AS AA "
                        + "LEFT JOIN (SELECT DISTINCT PDC_DOC_NO FROM PRODUCTION.FELT_PDC_HEADER) AS BB "
                        + "ON AA.PDC_DOC_NO=BB.PDC_DOC_NO";
                System.out.println("Add Piece Query History :" + sql);
                data.Execute(sql);

            }

            LoadData();
            rsHeader.close();
            stHeader.close();
            rsHHeader.close();
            stHHeader.close();
//            rsBankDetail.close();
//            stBankDetail.close();
//            rsHBankDetail.close();
//            stHBankDetail.close();
            rsPieceDetail.close();
            stPieceDetail.close();
            rsHPieceDetail.close();
            stHPieceDetail.close();
            return true;

        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    //Updates current record
    public boolean Update() {
        ResultSet rsHHeader, rsBankDetail, rsHBankDetail, rsPieceDetail, rsHPieceDetail;
        Statement stHHeader, stBankDetail, stHBankDetail, stPieceDetail, stHPieceDetail;
        int revisionNo = 1;
        try {
            // Felt PDC Header data history connection
            stHHeader = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHHeader = stHHeader.executeQuery("SELECT * FROM  PRODUCTION.FELT_PDC_AMEND_HEADER_H WHERE PDC_AMEND_NO=''");
            rsHHeader.first();

//            // Felt PDC Bank Detail data connection
//            stBankDetail = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
//            rsBankDetail = stBankDetail.executeQuery("SELECT * FROM  PRODUCTION.FELT_PDC_BANK_DETAIL WHERE PDC_DOC_NO=''");
//            rsBankDetail.first();
//
//            // Felt PDC Bank Detail data history connection
//            stHBankDetail = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
//            rsHBankDetail = stHBankDetail.executeQuery("SELECT * FROM  PRODUCTION.FELT_PDC_BANK_DETAIL_H WHERE PDC_DOC_NO=''");
//            rsHBankDetail.first();

            // Felt PDC Piece Detail data connection
            stPieceDetail = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsPieceDetail = stPieceDetail.executeQuery("SELECT * FROM  PRODUCTION.FELT_PDC_PIECE_AMEND_DETAIL WHERE PDC_AMEND_NO=''");
            rsPieceDetail.first();

            // Felt PDC Piece Detail data history connection
            stHPieceDetail = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHPieceDetail = stHPieceDetail.executeQuery("SELECT * FROM  PRODUCTION.FELT_PDC_PIECE_AMEND_DETAIL_H WHERE PDC_AMEND_NO=''");
            rsHPieceDetail.first();

            //========= Inserting Into Header =================//
            resultSet.updateString("PDC_AMEND_NO", (String) getAttribute("PDC_AMEND_NO").getObj());
            resultSet.updateString("PDC_AMEND_DATE", (String) getAttribute("PDC_AMEND_DATE").getObj());
            resultSet.updateString("PDC_DOC_NO", (String) getAttribute("PDC_DOC_NO").getObj());
            resultSet.updateString("PDC_DOC_DATE", (String) getAttribute("PDC_DOC_DATE").getObj());
            resultSet.updateString("PDC_PARTY_CODE", (String) getAttribute("PDC_PARTY_CODE").getObj());
            resultSet.updateString("PDC_PARTY_NAME", (String) getAttribute("PDC_PARTY_NAME").getObj());
            resultSet.updateString("PDC_PARTY_CREDIT_DAYS", (String) getAttribute("PDC_PARTY_CREDIT_DAYS").getObj());
            resultSet.updateDouble("PDC_MANUAL_AMOUNT", getAttribute("PDC_MANUAL_AMOUNT").getVal());
            resultSet.updateDouble("PDC_CRITICAL_AMOUNT", getAttribute("PDC_CRITICAL_AMOUNT").getVal());
            resultSet.updateDouble("PDC_CARRY_FORWARD_AMOUNT", getAttribute("PDC_CARRY_FORWARD_AMOUNT").getVal());
            resultSet.updateDouble("PDC_UNADJUSTED_CREDIT", getAttribute("PDC_UNADJUSTED_CREDIT").getVal());
            resultSet.updateDouble("PDC_TOTAL_AMOUNT", getAttribute("PDC_TOTAL_AMOUNT").getVal());
            resultSet.updateDouble("PDC_TOTAL_PIECE_AMOUNT", getAttribute("PDC_TOTAL_PIECE_AMOUNT").getVal());
            resultSet.updateDouble("PDC_TOTAL_BALANCE", getAttribute("PDC_TOTAL_BALANCE").getVal());
            resultSet.updateString("PDC_REMARK", (String) getAttribute("PDC_REMARK").getObj());

            resultSet.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
            resultSet.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
            resultSet.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
            resultSet.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
            resultSet.updateBoolean("APPROVED", false);
            resultSet.updateString("APPROVED_DATE", "0000-00-00");
            resultSet.updateBoolean("REJECTED", false);
            resultSet.updateString("REJECTED_DATE", "0000-00-00");
            resultSet.updateInt("CANCELED", 0);
            resultSet.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            resultSet.updateInt("CHANGED", 1);
            resultSet.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

            resultSet.updateRow();

            //========= Inserting Into History =================//
            //Get the Maximum Revision No in History Table.
            revisionNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_PDC_AMEND_HEADER_H WHERE PDC_AMEND_DATE='" + getAttribute("PDC_AMEND_DATE").getString() + "' AND PDC_AMEND_NO='" + getAttribute("PDC_AMEND_NO").getString() + "'");
            revisionNo++;
                        
            rsHHeader.moveToInsertRow();
            rsHHeader.updateInt("REVISION_NO", revisionNo);
            rsHHeader.updateInt("UPDATED_BY", (int) getAttribute("CREATED_BY").getVal());
            rsHHeader.updateString("APPROVAL_STATUS", (String) getAttribute("APPROVAL_STATUS").getObj());
            rsHHeader.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHHeader.updateString("APPROVER_REMARKS", (String) getAttribute("FROM_REMARKS").getObj());

            rsHHeader.updateString("PDC_AMEND_NO", (String) getAttribute("PDC_AMEND_NO").getObj());
            rsHHeader.updateString("PDC_AMEND_DATE", (String) getAttribute("PDC_AMEND_DATE").getObj());
            rsHHeader.updateString("PDC_DOC_NO", (String) getAttribute("PDC_DOC_NO").getObj());
            rsHHeader.updateString("PDC_DOC_DATE", (String) getAttribute("PDC_DOC_DATE").getObj());
            rsHHeader.updateString("PDC_PARTY_CODE", (String) getAttribute("PDC_PARTY_CODE").getObj());
            rsHHeader.updateString("PDC_PARTY_NAME", (String) getAttribute("PDC_PARTY_NAME").getObj());
            rsHHeader.updateString("PDC_PARTY_CREDIT_DAYS", (String) getAttribute("PDC_PARTY_CREDIT_DAYS").getObj());
            rsHHeader.updateDouble("PDC_MANUAL_AMOUNT", getAttribute("PDC_MANUAL_AMOUNT").getVal());
            rsHHeader.updateDouble("PDC_CRITICAL_AMOUNT", getAttribute("PDC_CRITICAL_AMOUNT").getVal());
            rsHHeader.updateDouble("PDC_CARRY_FORWARD_AMOUNT", getAttribute("PDC_CARRY_FORWARD_AMOUNT").getVal());
            rsHHeader.updateDouble("PDC_UNADJUSTED_CREDIT", getAttribute("PDC_UNADJUSTED_CREDIT").getVal());
            rsHHeader.updateDouble("PDC_TOTAL_AMOUNT", getAttribute("PDC_TOTAL_AMOUNT").getVal());
            rsHHeader.updateDouble("PDC_TOTAL_PIECE_AMOUNT", getAttribute("PDC_TOTAL_PIECE_AMOUNT").getVal());
            rsHHeader.updateDouble("PDC_TOTAL_BALANCE", getAttribute("PDC_TOTAL_BALANCE").getVal());
            rsHHeader.updateString("PDC_REMARK", (String) getAttribute("PDC_REMARK").getObj());

            rsHHeader.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
            rsHHeader.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
            rsHHeader.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
            rsHHeader.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
            rsHHeader.updateBoolean("APPROVED", false);
            rsHHeader.updateString("APPROVED_DATE", "0000-00-00");
            rsHHeader.updateBoolean("REJECTED", false);
            rsHHeader.updateString("REJECTED_DATE", "0000-00-00");
            rsHHeader.updateInt("CANCELED", 0);
            rsHHeader.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsHHeader.updateInt("CHANGED", 1);
            rsHHeader.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");

            rsHHeader.updateString("FROM_IP", "" + str_split[1]);

            rsHHeader.insertRow();

            //==== Delete Previous Entries ====//
            String AmendNo = (String) getAttribute("PDC_AMEND_NO").getObj();

//            data.Execute("DELETE FROM PRODUCTION.FELT_PDC_BANK_DETAIL WHERE PDC_DOC_NO='" + DocNo + "' ");
            data.Execute("DELETE FROM PRODUCTION.FELT_PDC_PIECE_AMEND_DETAIL WHERE PDC_AMEND_NO='" + AmendNo + "' ");

//            //Now Insert records into PDC Bank Detail & History tables
//            for (int i = 1; i <= colPDCBankItems.size(); i++) {
//                clsFeltPDCBankDetails ObjBankItem = (clsFeltPDCBankDetails) colPDCBankItems.get(Integer.toString(i));
//
//                rsBankDetail.moveToInsertRow();
//                rsBankDetail.updateString("PDC_DOC_NO", (String) getAttribute("PDC_DOC_NO").getObj());
//                rsBankDetail.updateString("PDC_BANK_CD", (String) ObjBankItem.getAttribute("PDC_BANK_CD").getObj());
//                rsBankDetail.updateString("PDC_BANK_NAME", (String) ObjBankItem.getAttribute("PDC_BANK_NAME").getObj());
//                rsBankDetail.updateString("PDC_BANK_BRANCH", (String) ObjBankItem.getAttribute("PDC_BANK_BRANCH").getObj());
//                rsBankDetail.updateString("PDC_CHEQUE_NO", (String) ObjBankItem.getAttribute("PDC_CHEQUE_NO").getObj());
//                rsBankDetail.updateString("PDC_CHEQUE_DATE", (String) ObjBankItem.getAttribute("PDC_CHEQUE_DATE").getObj());
//                rsBankDetail.updateDouble("PDC_CHEQUE_AMOUNT", EITLERPGLOBAL.round(ObjBankItem.getAttribute("PDC_CHEQUE_AMOUNT").getVal(), 2));
//                rsBankDetail.updateString("PDC_PHYSICAL_SCANNED", (String) ObjBankItem.getAttribute("PDC_PHYSICAL_SCANNED").getObj());
//                rsBankDetail.updateString("PDC_BILLING_DATE", (String) ObjBankItem.getAttribute("PDC_BILLING_DATE").getObj());
//                rsBankDetail.updateDouble("PDC_UTILIZED_AMOUNT", EITLERPGLOBAL.round(ObjBankItem.getAttribute("PDC_UTILIZED_AMOUNT").getVal(), 2));
//                rsBankDetail.updateDouble("PDC_BALANCE_AMOUNT", EITLERPGLOBAL.round(ObjBankItem.getAttribute("PDC_BALANCE_AMOUNT").getVal(), 2));
//                rsBankDetail.updateString("PDC_CLOSED", (String) ObjBankItem.getAttribute("PDC_CLOSED").getObj());
//
//                rsBankDetail.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
//                rsBankDetail.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
//                rsBankDetail.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
//                rsBankDetail.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
//                rsBankDetail.updateBoolean("APPROVED", false);
//                rsBankDetail.updateString("APPROVED_DATE", "0000-00-00");
//                rsBankDetail.updateBoolean("REJECTED", false);
//                rsBankDetail.updateString("REJECTED_DATE", "0000-00-00");
//                rsBankDetail.updateInt("CANCELED", 0);
//                rsBankDetail.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
//                rsBankDetail.updateInt("CHANGED", 1);
//                rsBankDetail.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
//                rsBankDetail.insertRow();
//
//                rsHBankDetail.moveToInsertRow();
//                rsHBankDetail.updateInt("REVISION_NO", revisionNo);
//                rsHBankDetail.updateInt("UPDATED_BY", (int) getAttribute("CREATED_BY").getVal());
//                rsHBankDetail.updateString("APPROVAL_STATUS", (String) getAttribute("APPROVAL_STATUS").getObj());
//                rsHBankDetail.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
//                rsHBankDetail.updateString("APPROVER_REMARKS", (String) getAttribute("FROM_REMARKS").getObj());
//
//                rsHBankDetail.updateString("PDC_DOC_NO", (String) getAttribute("PDC_DOC_NO").getObj());
//                rsHBankDetail.updateString("PDC_BANK_CD", (String) ObjBankItem.getAttribute("PDC_BANK_CD").getObj());
//                rsHBankDetail.updateString("PDC_BANK_NAME", (String) ObjBankItem.getAttribute("PDC_BANK_NAME").getObj());
//                rsHBankDetail.updateString("PDC_BANK_BRANCH", (String) ObjBankItem.getAttribute("PDC_BANK_BRANCH").getObj());
//                rsHBankDetail.updateString("PDC_CHEQUE_NO", (String) ObjBankItem.getAttribute("PDC_CHEQUE_NO").getObj());
//                rsHBankDetail.updateString("PDC_CHEQUE_DATE", (String) ObjBankItem.getAttribute("PDC_CHEQUE_DATE").getObj());
//                rsHBankDetail.updateDouble("PDC_CHEQUE_AMOUNT", EITLERPGLOBAL.round(ObjBankItem.getAttribute("PDC_CHEQUE_AMOUNT").getVal(), 2));
//                rsHBankDetail.updateString("PDC_PHYSICAL_SCANNED", (String) ObjBankItem.getAttribute("PDC_PHYSICAL_SCANNED").getObj());
//                rsHBankDetail.updateString("PDC_BILLING_DATE", (String) ObjBankItem.getAttribute("PDC_BILLING_DATE").getObj());
//                rsHBankDetail.updateDouble("PDC_UTILIZED_AMOUNT", EITLERPGLOBAL.round(ObjBankItem.getAttribute("PDC_UTILIZED_AMOUNT").getVal(), 2));
//                rsHBankDetail.updateDouble("PDC_BALANCE_AMOUNT", EITLERPGLOBAL.round(ObjBankItem.getAttribute("PDC_BALANCE_AMOUNT").getVal(), 2));
//                rsHBankDetail.updateString("PDC_CLOSED", (String) ObjBankItem.getAttribute("PDC_CLOSED").getObj());
//
//                rsHBankDetail.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
//                rsHBankDetail.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
//                rsHBankDetail.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
//                rsHBankDetail.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
//                rsHBankDetail.updateBoolean("APPROVED", false);
//                rsHBankDetail.updateString("APPROVED_DATE", "0000-00-00");
//                rsHBankDetail.updateBoolean("REJECTED", false);
//                rsHBankDetail.updateString("REJECTED_DATE", "0000-00-00");
//                rsHBankDetail.updateInt("CANCELED", 0);
//                rsHBankDetail.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
//                rsHBankDetail.updateInt("CHANGED", 1);
//                rsHBankDetail.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
//                ResultSet rsTmp1 = data.getResult("SELECT USER()");
//                rsTmp1.first();
//                String str1 = rsTmp1.getString(1);
//                String str1_split[] = str1.split("@");
//
//                rsHBankDetail.updateString("FROM_IP", "" + str1_split[1]);
//
//                rsHBankDetail.insertRow();
//
//            }

            //Now Insert records into PDC Piece Detail & History tables
            for (int i = 1; i <= colPDCPieceItems.size(); i++) {
                clsFeltPDCPieceAmendDetails ObjPieceItem = (clsFeltPDCPieceAmendDetails) colPDCPieceItems.get(Integer.toString(i));

                rsPieceDetail.moveToInsertRow();
                rsPieceDetail.updateString("PDC_AMEND_NO", (String) getAttribute("PDC_AMEND_NO").getObj());
                rsPieceDetail.updateString("PDC_DOC_NO", (String) getAttribute("PDC_DOC_NO").getObj());
                rsPieceDetail.updateString("PDC_PIECE_NO", (String) ObjPieceItem.getAttribute("PDC_PIECE_NO").getObj());
                rsPieceDetail.updateDouble("PDC_PIECE_AMOUNT", EITLERPGLOBAL.round(ObjPieceItem.getAttribute("PDC_PIECE_AMOUNT").getVal(), 2));
                rsPieceDetail.updateString("PDC_PIECE_STATUS", (String) ObjPieceItem.getAttribute("PDC_PIECE_STATUS").getObj());
                rsPieceDetail.updateString("PDC_PIECE_AMEND_NO", (String) ObjPieceItem.getAttribute("PDC_PIECE_AMEND_NO").getObj());
                rsPieceDetail.updateString("PDC_PIECE_AMEND_DATE", (String) ObjPieceItem.getAttribute("PDC_PIECE_AMEND_DATE").getObj());

                rsPieceDetail.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                rsPieceDetail.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                rsPieceDetail.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                rsPieceDetail.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                rsPieceDetail.updateBoolean("APPROVED", false);
                rsPieceDetail.updateString("APPROVED_DATE", "0000-00-00");
                rsPieceDetail.updateBoolean("REJECTED", false);
                rsPieceDetail.updateString("REJECTED_DATE", "0000-00-00");
                rsPieceDetail.updateInt("CANCELED", 0);
                rsPieceDetail.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                rsPieceDetail.updateInt("CHANGED", 1);
                rsPieceDetail.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsPieceDetail.insertRow();

                rsHPieceDetail.moveToInsertRow();
                rsHPieceDetail.updateInt("REVISION_NO", revisionNo);
                rsHPieceDetail.updateInt("UPDATED_BY", (int) getAttribute("CREATED_BY").getVal());
                rsHPieceDetail.updateString("APPROVAL_STATUS", (String) getAttribute("APPROVAL_STATUS").getObj());
                rsHPieceDetail.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHPieceDetail.updateString("APPROVER_REMARKS", (String) getAttribute("FROM_REMARKS").getObj());

                rsHPieceDetail.updateString("PDC_AMEND_NO", (String) getAttribute("PDC_AMEND_NO").getObj());
                rsHPieceDetail.updateString("PDC_DOC_NO", (String) getAttribute("PDC_DOC_NO").getObj());
                rsHPieceDetail.updateString("PDC_PIECE_NO", (String) ObjPieceItem.getAttribute("PDC_PIECE_NO").getObj());
                rsHPieceDetail.updateDouble("PDC_PIECE_AMOUNT", EITLERPGLOBAL.round(ObjPieceItem.getAttribute("PDC_PIECE_AMOUNT").getVal(), 2));
                rsHPieceDetail.updateString("PDC_PIECE_STATUS", (String) ObjPieceItem.getAttribute("PDC_PIECE_STATUS").getObj());
                rsHPieceDetail.updateString("PDC_PIECE_AMEND_NO", (String) ObjPieceItem.getAttribute("PDC_PIECE_AMEND_NO").getObj());
                rsHPieceDetail.updateString("PDC_PIECE_AMEND_DATE", (String) ObjPieceItem.getAttribute("PDC_PIECE_AMEND_DATE").getObj());

                rsHPieceDetail.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                rsHPieceDetail.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                rsHPieceDetail.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                rsHPieceDetail.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                rsHPieceDetail.updateBoolean("APPROVED", false);
                rsHPieceDetail.updateString("APPROVED_DATE", "0000-00-00");
                rsHPieceDetail.updateBoolean("REJECTED", false);
                rsHPieceDetail.updateString("REJECTED_DATE", "0000-00-00");
                rsHPieceDetail.updateInt("CANCELED", 0);
                rsHPieceDetail.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                rsHPieceDetail.updateInt("CHANGED", 1);
                rsHPieceDetail.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                ResultSet rsTmp1 = data.getResult("SELECT USER()");
                rsTmp1.first();
                String str1 = rsTmp1.getString(1);
                String str1_split[] = str1.split("@");

                rsHPieceDetail.updateString("FROM_IP", "" + str1_split[1]);

                rsHPieceDetail.insertRow();
            }

            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = 626; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo = (String) getAttribute("PDC_AMEND_NO").getObj();
            ObjFeltProductionApprovalFlow.DocDate = (String) getAttribute("PDC_AMEND_DATE").getObj();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.FELT_PDC_AMEND_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator = true;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "PDC_AMEND_NO";

            if (getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFeltProductionApprovalFlow.To = (int) getAttribute("SEND_DOC_TO").getVal();
                ObjFeltProductionApprovalFlow.ExplicitSendTo = true;
            }

            //==== Handling Rejected Documents ==========//
            boolean IsRejected = getAttribute("REJECTED").getBool();

            if (IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.FELT_PDC_AMEND_HEADER SET REJECTED=0,CHANGED=1 WHERE PDC_AMEND_NO ='" + getAttribute("PDC_AMEND_NO").getString() + "'");

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

            // Update in Bank Detail and Piece Detail Table To confirm that PDC has completed
            if (ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {
                ObjFeltProductionApprovalFlow.finalApproved = false;
                //data.Execute("UPDATE PRODUCTION.FELT_PDC_BANK_DETAIL SET APPROVED=1,APPROVED_DATE=CURRENT_TIMESTAMP,CHANGED=1 WHERE PDC_DOC_NO='"+ObjFeltProductionApprovalFlow.DocNo+"'");
                data.Execute("UPDATE PRODUCTION.FELT_PDC_PIECE_AMEND_DETAIL SET APPROVED=1,APPROVED_DATE=CURRENT_TIMESTAMP,CHANGED=1 WHERE PDC_AMEND_NO='"+ObjFeltProductionApprovalFlow.DocNo+"'");
                
                String sql = "UPDATE PRODUCTION.FELT_PDC_HEADER SET PDC_TOTAL_PIECE_AMOUNT='"+getAttribute("PDC_TOTAL_PIECE_AMOUNT").getVal()+"', PDC_TOTAL_BALANCE='"+getAttribute("PDC_TOTAL_BALANCE").getVal()+"' WHERE PDC_DOC_NO='" + (String) getAttribute("PDC_DOC_NO").getObj() + "' "; 
                System.out.println("Header Query:" + sql);
                data.Execute(sql);
                
                sql = "UPDATE PRODUCTION.FELT_PDC_PIECE_DETAIL "
                        + "SET PDC_PIECE_STATUS='DELETE',PDC_PIECE_AMEND_DATE='"+(String) getAttribute("PDC_AMEND_DATE").getObj()+"',"
                        + "PDC_PIECE_AMEND_NO='" + (String) getAttribute("PDC_AMEND_NO").getObj() + "' "
                        + "WHERE PDC_PIECE_NO IN ( SELECT D.PDC_PIECE_NO "
                        + "FROM (SELECT  * FROM PRODUCTION.FELT_PDC_PIECE_DETAIL "
                        + "WHERE PDC_DOC_NO IN (SELECT DISTINCT PDC_DOC_NO FROM PRODUCTION.FELT_PDC_PIECE_AMEND_DETAIL "
                        + "WHERE PDC_DOC_NO='" + (String) getAttribute("PDC_DOC_NO").getObj() + "' "
                        + "AND PDC_AMEND_NO='" + (String) getAttribute("PDC_AMEND_NO").getObj() + "')) AS D "
                        + "LEFT JOIN (SELECT * FROM PRODUCTION.FELT_PDC_PIECE_AMEND_DETAIL "
                        + " WHERE PDC_DOC_NO='" + (String) getAttribute("PDC_DOC_NO").getObj() + "' "
                        + "AND PDC_AMEND_NO='" + (String) getAttribute("PDC_AMEND_NO").getObj() + "') AS  A "
                        + "ON D.PDC_DOC_NO=A.PDC_DOC_NO AND D.PDC_PIECE_NO=A.PDC_PIECE_NO "
                        + "WHERE A.PDC_PIECE_NO IS NULL)";
                System.out.println("Deleted Piece Query:" + sql);
                data.Execute(sql);
                sql = "UPDATE PRODUCTION.FELT_PDC_PIECE_DETAIL_H "
                        + "SET PDC_PIECE_STATUS='DELETE',PDC_PIECE_AMEND_DATE='"+(String) getAttribute("PDC_AMEND_DATE").getObj()+"',"
                        + "PDC_PIECE_AMEND_NO='" + (String) getAttribute("PDC_AMEND_NO").getObj() + "' "
                        + "WHERE PDC_PIECE_NO IN ( SELECT D.PDC_PIECE_NO "
                        + "FROM (SELECT  * FROM PRODUCTION.FELT_PDC_PIECE_DETAIL "
                        + "WHERE PDC_DOC_NO IN (SELECT DISTINCT PDC_DOC_NO FROM PRODUCTION.FELT_PDC_PIECE_AMEND_DETAIL "
                        + "WHERE PDC_DOC_NO='" + (String) getAttribute("PDC_DOC_NO").getObj() + "' "
                        + "AND PDC_AMEND_NO='" + (String) getAttribute("PDC_AMEND_NO").getObj() + "')) AS D "
                        + "LEFT JOIN (SELECT * FROM PRODUCTION.FELT_PDC_PIECE_AMEND_DETAIL "
                        + " WHERE PDC_DOC_NO='" + (String) getAttribute("PDC_DOC_NO").getObj() + "' "
                        + "AND PDC_AMEND_NO='" + (String) getAttribute("PDC_AMEND_NO").getObj() + "') AS  A "
                        + "ON D.PDC_DOC_NO=A.PDC_DOC_NO AND D.PDC_PIECE_NO=A.PDC_PIECE_NO "
                        + "WHERE A.PDC_PIECE_NO IS NULL)";
                System.out.println("Deleted Piece Query History:" + sql);
                data.Execute(sql);
                sql = "INSERT INTO PRODUCTION.FELT_PDC_PIECE_DETAIL "
                        + "SELECT BB.PDC_DOC_NO,AA.PDC_PIECE_NO,AA.PDC_PIECE_AMOUNT,'ADD','" + (String) getAttribute("PDC_AMEND_NO").getObj() + "', "
                        + "'" + (String) getAttribute("PDC_AMEND_DATE").getObj() + "',AA.REMARKS,AA.CREATED_BY,AA.CREATED_DATE,AA.MODIFIED_BY,AA.MODIFIED_DATE, "
                        + "AA.APPROVED,AA.APPROVED_DATE,AA.REJECTED,AA.REJECTED_DATE,AA.REJECTED_REMARKS,AA.CANCELED,AA.HIERARCHY_ID, "
                        + "'1',CURDATE() "
                        + " FROM ("
                        + "SELECT D.* "
                        + "FROM (SELECT  * FROM PRODUCTION.FELT_PDC_PIECE_AMEND_DETAIL "
                        + "WHERE PDC_DOC_NO='" + (String) getAttribute("PDC_DOC_NO").getObj() + "' "
                        + "AND PDC_AMEND_NO='" + (String) getAttribute("PDC_AMEND_NO").getObj() + "' "
                        + "AND PDC_DOC_NO IN (SELECT DISTINCT PDC_DOC_NO FROM PRODUCTION.FELT_PDC_PIECE_DETAIL)) AS D "
                        + "LEFT JOIN (SELECT * FROM PRODUCTION.FELT_PDC_PIECE_DETAIL) AS  A "
                        + "ON D.PDC_DOC_NO=A.PDC_DOC_NO AND D.PDC_PIECE_NO=A.PDC_PIECE_NO "
                        + "WHERE A.PDC_PIECE_NO IS NULL) AS AA "
                        + "LEFT JOIN (SELECT DISTINCT PDC_DOC_NO FROM PRODUCTION.FELT_PDC_HEADER) AS BB "
                        + "ON AA.PDC_DOC_NO=BB.PDC_DOC_NO";
                System.out.println("Add Piece Query :" + sql);
                data.Execute(sql);
                sql = "INSERT INTO PRODUCTION.FELT_PDC_PIECE_DETAIL_H "
                        + "SELECT 999,BB.PDC_DOC_NO,AA.PDC_PIECE_NO,AA.PDC_PIECE_AMOUNT,'ADD','" + (String) getAttribute("PDC_AMEND_NO").getObj() + "', "
                        + "'" + (String) getAttribute("PDC_AMEND_DATE").getObj() + "',AA.REMARKS,AA.CREATED_BY,AA.CREATED_DATE,AA.MODIFIED_BY,AA.MODIFIED_DATE, "
                        + "AA.APPROVED,AA.APPROVED_DATE,AA.REJECTED,AA.REJECTED_DATE,AA.REJECTED_REMARKS,AA.CANCELED,AA.HIERARCHY_ID, "
                        + "'1',CURDATE(),'"+(int) getAttribute("CREATED_BY").getVal()+"','"+(String) getAttribute("APPROVAL_STATUS").getObj()+"', "
                        + "'"+EITLERPGLOBAL.getCurrentDateDB()+"','"+(String) getAttribute("FROM_REMARKS").getObj()+"','" + str_split[1] + "' "
                        + " FROM ("
                        + "SELECT D.* "
                        + "FROM (SELECT  * FROM PRODUCTION.FELT_PDC_PIECE_AMEND_DETAIL "
                        + "WHERE PDC_DOC_NO='" + (String) getAttribute("PDC_DOC_NO").getObj() + "' "
                        + "AND PDC_AMEND_NO='" + (String) getAttribute("PDC_AMEND_NO").getObj() + "' "
                        + "AND PDC_DOC_NO IN (SELECT DISTINCT PDC_DOC_NO FROM PRODUCTION.FELT_PDC_PIECE_DETAIL_H)) AS D "
                        + "LEFT JOIN (SELECT * FROM PRODUCTION.FELT_PDC_PIECE_DETAIL_H) AS  A "
                        + "ON D.PDC_DOC_NO=A.PDC_DOC_NO AND D.PDC_PIECE_NO=A.PDC_PIECE_NO "
                        + "WHERE A.PDC_PIECE_NO IS NULL) AS AA "
                        + "LEFT JOIN (SELECT DISTINCT PDC_DOC_NO FROM PRODUCTION.FELT_PDC_HEADER) AS BB "
                        + "ON AA.PDC_DOC_NO=BB.PDC_DOC_NO";
                System.out.println("Add Piece Query History :" + sql);
                data.Execute(sql);

            }

            setData();
            rsHHeader.close();
            stHHeader.close();
//            rsBankDetail.close();
//            stBankDetail.close();
//            rsHBankDetail.close();
//            stHBankDetail.close();
            rsPieceDetail.close();
            stPieceDetail.close();
            rsHPieceDetail.close();
            stHPieceDetail.close();
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PDC_AMEND_HEADER WHERE PDC_AMEND_NO='" + documentNo + "' AND APPROVED=" + 1;
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=626 AND USER_ID=" + userID + " AND DOC_NO='" + documentNo + "' AND STATUS='W'";
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();

                if (rsTmp.getInt("COUNT") > 0) {
                    strSQL = "DELETE FROM PRODUCTION.FELT_PDC_AMEND_HEADER WHERE PDC_AMEND_NO='" + documentNo + "'";
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PDC_AMEND_HEADER WHERE PDC_AMEND_NO='" + orderupdDocumentNo + "' AND APPROVED=1";
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=626 AND USER_ID=" + userID + " AND DOC_NO='" + orderupdDocumentNo + "' AND STATUS='W'";
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
            String strSql = "SELECT * FROM PRODUCTION.FELT_PDC_AMEND_HEADER WHERE " + stringFindQuery + "";
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
        Statement stBankDetail, stPieceDetail;
        ResultSet rsBankDetail, rsPieceDetail;
        String AmendNo = "";
        String DocNo = "";
        int BankDetailCounter = 0, PieceDetailCounter = 0, SrNo = 0;
        int RevNo = 0;

        try {
            if (HistoryView) {
                RevNo = resultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO", resultSet.getInt("REVISION_NO"));
            } else {
                setAttribute("REVISION_NO", 0);
            }

            setAttribute("PDC_AMEND_NO", resultSet.getString("PDC_AMEND_NO"));
            setAttribute("PDC_AMEND_DATE", resultSet.getString("PDC_AMEND_DATE"));
            setAttribute("PDC_DOC_NO", resultSet.getString("PDC_DOC_NO"));
            setAttribute("PDC_DOC_DATE", resultSet.getString("PDC_DOC_DATE"));
            setAttribute("PDC_PARTY_CODE", resultSet.getString("PDC_PARTY_CODE"));
            setAttribute("PDC_PARTY_NAME", resultSet.getString("PDC_PARTY_NAME"));
            setAttribute("PDC_PARTY_CREDIT_DAYS", resultSet.getString("PDC_PARTY_CREDIT_DAYS"));
            setAttribute("PDC_MANUAL_AMOUNT", EITLERPGLOBAL.round(resultSet.getDouble("PDC_MANUAL_AMOUNT"), 2));
            setAttribute("PDC_CRITICAL_AMOUNT", EITLERPGLOBAL.round(resultSet.getDouble("PDC_CRITICAL_AMOUNT"), 2));
            setAttribute("PDC_CARRY_FORWARD_AMOUNT", EITLERPGLOBAL.round(resultSet.getDouble("PDC_CARRY_FORWARD_AMOUNT"), 2));
            setAttribute("PDC_UNADJUSTED_CREDIT", EITLERPGLOBAL.round(resultSet.getDouble("PDC_UNADJUSTED_CREDIT"), 2));
            setAttribute("PDC_TOTAL_AMOUNT", EITLERPGLOBAL.round(resultSet.getDouble("PDC_TOTAL_AMOUNT"), 2));
            setAttribute("PDC_TOTAL_PIECE_AMOUNT", EITLERPGLOBAL.round(resultSet.getDouble("PDC_TOTAL_PIECE_AMOUNT"), 2));
            setAttribute("PDC_TOTAL_BALANCE", EITLERPGLOBAL.round(resultSet.getDouble("PDC_TOTAL_BALANCE"), 2));
            setAttribute("PDC_REMARK", resultSet.getString("PDC_REMARK"));

            setAttribute("HIERARCHY_ID", resultSet.getInt("HIERARCHY_ID"));
            setAttribute("CANCELED", resultSet.getInt("CANCELED"));
            setAttribute("APPROVED", resultSet.getInt("APPROVED"));

            setAttribute("CREATED_BY", resultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE", resultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY", resultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE", resultSet.getString("MODIFIED_DATE"));

            colPDCBankItems.clear(); //Clear existing data of Bank Detail

            AmendNo = (String) getAttribute("PDC_AMEND_NO").getObj();
            DocNo = (String) getAttribute("PDC_DOC_NO").getObj();

            stBankDetail = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            if (HistoryView) {
                rsBankDetail = stBankDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_PDC_BANK_DETAIL_H WHERE PDC_DOC_NO='" + DocNo + "' AND REVISION_NO=" + RevNo + " ");
            } else {
                rsBankDetail = stBankDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_PDC_BANK_DETAIL WHERE PDC_DOC_NO='" + DocNo + "' ");
            }

            rsBankDetail.first();
            BankDetailCounter = 0;
            while (!rsBankDetail.isAfterLast() && rsBankDetail.getRow() > 0) {
                BankDetailCounter = BankDetailCounter + 1;
                clsFeltPDCBankDetails ObjBankItem = new clsFeltPDCBankDetails();

                ObjBankItem.setAttribute("PDC_BANK_CD", rsBankDetail.getString("PDC_BANK_CD"));
                ObjBankItem.setAttribute("PDC_BANK_NAME", rsBankDetail.getString("PDC_BANK_NAME"));
                ObjBankItem.setAttribute("PDC_BANK_BRANCH", rsBankDetail.getString("PDC_BANK_BRANCH"));
                ObjBankItem.setAttribute("PDC_CHEQUE_NO", rsBankDetail.getString("PDC_CHEQUE_NO"));
                ObjBankItem.setAttribute("PDC_CHEQUE_DATE", rsBankDetail.getString("PDC_CHEQUE_DATE"));
                ObjBankItem.setAttribute("PDC_CHEQUE_AMOUNT", EITLERPGLOBAL.round(rsBankDetail.getDouble("PDC_CHEQUE_AMOUNT"), 2));
                ObjBankItem.setAttribute("PDC_PHYSICAL_SCANNED", rsBankDetail.getString("PDC_PHYSICAL_SCANNED"));
                ObjBankItem.setAttribute("PDC_BILLING_DATE", rsBankDetail.getString("PDC_BILLING_DATE"));
                ObjBankItem.setAttribute("PDC_UTILIZED_AMOUNT", EITLERPGLOBAL.round(rsBankDetail.getDouble("PDC_UTILIZED_AMOUNT"), 2));
                ObjBankItem.setAttribute("PDC_BALANCE_AMOUNT", EITLERPGLOBAL.round(rsBankDetail.getDouble("PDC_BALANCE_AMOUNT"), 2));
                ObjBankItem.setAttribute("PDC_CLOSED", rsBankDetail.getString("PDC_CLOSED"));

                colPDCBankItems.put(Integer.toString(BankDetailCounter), ObjBankItem);
                rsBankDetail.next();
            }

            colPDCPieceItems.clear(); //Clear existing data of Bank Detail

            stPieceDetail = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            if (HistoryView) {
                rsPieceDetail = stPieceDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_PDC_PIECE_AMEND_DETAIL_H WHERE PDC_AMEND_NO='" + AmendNo + "' AND REVISION_NO=" + RevNo + " ");
            } else {
                rsPieceDetail = stPieceDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_PDC_PIECE_AMEND_DETAIL WHERE PDC_AMEND_NO='" + AmendNo + "' ");
            }

            rsPieceDetail.first();
            PieceDetailCounter = 0;
            while (!rsPieceDetail.isAfterLast() && rsPieceDetail.getRow() > 0) {
                PieceDetailCounter = PieceDetailCounter + 1;
                clsFeltPDCPieceAmendDetails ObjPieceItem = new clsFeltPDCPieceAmendDetails();

                ObjPieceItem.setAttribute("PDC_PIECE_NO", rsPieceDetail.getString("PDC_PIECE_NO"));
                ObjPieceItem.setAttribute("PDC_PIECE_AMOUNT", EITLERPGLOBAL.round(rsPieceDetail.getDouble("PDC_PIECE_AMOUNT"), 2));
                ObjPieceItem.setAttribute("PDC_PIECE_STATUS", rsPieceDetail.getString("PDC_PIECE_STATUS"));
                ObjPieceItem.setAttribute("PDC_PIECE_AMEND_NO", rsPieceDetail.getString("PDC_PIECE_AMEND_NO"));
                ObjPieceItem.setAttribute("PDC_PIECE_AMEND_DATE", rsPieceDetail.getString("PDC_PIECE_AMEND_DATE"));
                
                colPDCPieceItems.put(Integer.toString(PieceDetailCounter), ObjPieceItem);
                rsPieceDetail.next();
            }            
            
//            rsBankDetail.close();
//            stBankDetail.close();
            rsPieceDetail.close();
            stPieceDetail.close();
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
            rsTmp = stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_PDC_AMEND_HEADER_H WHERE PDC_AMEND_NO='" + productionDocumentNo + "' GROUP BY REVISION_NO");

            while (rsTmp.next()) {
                clsFeltPDCAmend ObjFeltOrderUpd = new clsFeltPDCAmend();

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
            resultSet = statement.executeQuery("SELECT DISTINCT REVISION_NO FROM PRODUCTION.FELT_PDC_AMEND_HEADER_H WHERE PDC_AMEND_DATE='" + pProductionDate + "' AND PDC_AMEND_NO ='" + pDocNo + "'");
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
                strSQL = "SELECT DISTINCT PDC_AMEND_NO,PDC_AMEND_DATE,PDC_PARTY_CODE,RECEIVED_DATE FROM PRODUCTION.FELT_PDC_AMEND_HEADER, PRODUCTION.FELT_PROD_DOC_DATA WHERE PDC_AMEND_NO=DOC_NO AND STATUS='W' AND MODULE_ID=626 AND CANCELED=0  AND USER_ID=" + pUserID + " ORDER BY RECEIVED_DATE,PDC_AMEND_NO";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT PDC_AMEND_NO,PDC_AMEND_DATE,PDC_PARTY_CODE,RECEIVED_DATE FROM PRODUCTION.FELT_PDC_AMEND_HEADER, PRODUCTION.FELT_PROD_DOC_DATA WHERE PDC_AMEND_NO=DOC_NO AND STATUS='W' AND MODULE_ID=626 AND CANCELED=0  AND USER_ID=" + pUserID + " ORDER BY PDC_AMEND_DATE,PDC_AMEND_NO";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT PDC_AMEND_NO,PDC_AMEND_DATE,PDC_PARTY_CODE,RECEIVED_DATE FROM PRODUCTION.FELT_PDC_AMEND_HEADER, PRODUCTION.FELT_PROD_DOC_DATA WHERE PDC_AMEND_NO=DOC_NO AND STATUS='W' AND MODULE_ID=626 AND CANCELED=0  AND USER_ID=" + pUserID + " ORDER BY PDC_AMEND_NO";
            }

            ResultSet rsTmp = data.getConn().createStatement().executeQuery(strSQL);

            Counter = 0;
            while (rsTmp.next()) {

                Counter = Counter + 1;
                clsFeltPDCAmend ObjDoc = new clsFeltPDCAmend();

                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO", rsTmp.getString("PDC_AMEND_NO"));
                ObjDoc.setAttribute("DOC_DATE", rsTmp.getString("PDC_AMEND_DATE"));
                ObjDoc.setAttribute("RECEIVED_DATE", rsTmp.getString("RECEIVED_DATE"));
                ObjDoc.setAttribute("PARTY_CODE", rsTmp.getString("PDC_PARTY_CODE"));

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
            strSQL = "SELECT LAST_USED_NO,PADDING_BY,NO_LENGTH,PREFIX_CHARS,SUFFIX_CHARS FROM D_COM_FIRSTFREE WHERE COMPANY_ID=2 AND MODULE_ID=626 AND FIRSTFREE_NO=275";
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
                    data.Execute("UPDATE D_COM_FIRSTFREE SET LAST_USED_NO='" + strNewNo.trim() + "',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=2 AND MODULE_ID=626 AND FIRSTFREE_NO=275");
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
            rsTmp = data.getResult("SELECT PDC_AMEND_NO,APPROVED FROM PRODUCTION.FELT_PDC_AMEND_HEADER WHERE PDC_AMEND_NO='" + pDocNo + "' AND CANCELED=0");
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

                rsTmp = data.getResult("SELECT APPROVED FROM PRODUCTION.FELT_PDC_AMEND_HEADER WHERE PDC_AMEND_NO='" + pDocNo + "' ");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    ApprovedDoc = rsTmp.getBoolean("APPROVED");
                }

                if (ApprovedDoc) {

                } else {
                    int ModuleID = 626;

                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='" + pDocNo + "' AND MODULE_ID=" + (ModuleID));
                }

                //Now Update the header with cancel falg to true
                data.Execute("UPDATE PRODUCTION.FELT_PDC_AMEND_HEADER SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE PDC_AMEND_NO='" + pDocNo + "' ");
                
                //Now Update the bank and Piece detail with cancel falg to true
//                data.Execute("UPDATE PRODUCTION.FELT_PDC_BANK_DETAIL SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE PDC_DOC_NO='" + pDocNo + "' ");
                data.Execute("UPDATE PRODUCTION.FELT_PDC_PIECE_AMEND_DETAIL SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE PDC_AMEND_NO='" + pDocNo + "' ");

                Cancelled = true;
            }

            rsTmp.close();
            rsIndent.close();
        } catch (Exception e) {

        }

        return Cancelled;
    }
    
    public static boolean IsAmendmentUnderProcess(String pPartyCode) {
        Connection tmpConn;
        Statement stTmp = null;
        ResultSet rsTmp = null;
        boolean IsPresent = false;

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_PDC_AMEND_HEADER WHERE PDC_PARTY_CODE='" + pPartyCode + "' AND APPROVED=0 AND CANCELED=0");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                IsPresent = true;
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

            return IsPresent;
        } catch (Exception e) {
            return IsPresent;
        }
    }
}
