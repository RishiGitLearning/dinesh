/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.PieceUpdation;

import EITLERP.ComboData;
import EITLERP.EITLERPGLOBAL;
import EITLERP.JavaMail.JMail;
import EITLERP.Production.clsFeltProductionApprovalFlow;
import EITLERP.Variant;
import EITLERP.clsUser;
import EITLERP.data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import javax.swing.JOptionPane;

/**
 *
 * @author DAXESH PRAJAPATI
 */
public class clsPieceUpdation {

    private HashMap props;
    public boolean Ready = false;

    public HashMap hmPieceDetail;

    private ResultSet resultSet;
    private static Connection connection;
    private Statement statement;
    public String LastError = "";

    //History Related properties
    public boolean HistoryView = false;
    private String historyAmendDate = "";
    private String historyAmendID = "";
    private static int ModuleID = 745;

    public HashMap hmFeltSalesPieceUpdateDetails = new HashMap();

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

    public clsPieceUpdation() {
        props = new HashMap();

        props.put("PU_NO", new Variant(""));
        props.put("PU_DATE", new Variant(""));
        props.put("PR_PIECE_NO", new Variant(""));
        props.put("PR_DATE", new Variant(""));
        props.put("PR_ORDER_DATE", new Variant(""));
        props.put("PR_DOC_NO", new Variant(""));
        props.put("PR_MACHINE_NO", new Variant(""));
        props.put("PR_POSITION_NO", new Variant(""));
        props.put("PR_PARTY_CODE", new Variant(""));
        props.put("PR_PRODUCT_CODE", new Variant(""));
        props.put("PR_GROUP", new Variant(""));
        props.put("PR_STYLE", new Variant(""));
        props.put("PR_LENGTH", new Variant(""));
        props.put("PR_WIDTH", new Variant(""));
        props.put("PR_GSM", new Variant(""));
        props.put("PR_THORITICAL_WEIGHT", new Variant(""));
        props.put("PR_SQMTR", new Variant(""));
        props.put("PR_SYN_PER", new Variant(""));
        props.put("PR_REQUESTED_MONTH", new Variant(""));
        props.put("PR_REGION", new Variant(""));
        props.put("PR_INCHARGE", new Variant(""));
        props.put("PR_REFERENCE", new Variant(""));
        props.put("PR_REFERENCE_DATE", new Variant(""));
        props.put("PR_PO_NO", new Variant(""));
        props.put("PR_PO_DATE", new Variant(""));
        props.put("PR_ORDER_REMARK", new Variant(""));
        props.put("PR_PIECE_REMARK", new Variant(""));
        props.put("PR_PIECE_STAGE", new Variant(""));
        props.put("PR_WARP_DATE", new Variant(""));
        props.put("PR_WVG_DATE", new Variant(""));
        props.put("PR_MND_DATE", new Variant(""));
        props.put("PR_NDL_DATE", new Variant(""));
        props.put("PR_FNSG_DATE", new Variant(""));
        props.put("PR_RCV_DATE", new Variant(""));
        props.put("PR_ACTUAL_WEIGHT", new Variant(""));
        props.put("PR_ACTUAL_LENGTH", new Variant(""));
        props.put("PR_ACTUAL_WIDTH", new Variant(""));
        props.put("PR_BALE_NO", new Variant(""));
        props.put("PR_PACKED_DATE", new Variant(""));
        props.put("PR_REJECTED_FLAG", new Variant(""));
        props.put("PR_REJECTED_REMARK", new Variant(""));
        props.put("PR_DIVERSION_FLAG", new Variant(""));
        props.put("PR_DIVERSION_REASON", new Variant(""));
        props.put("PR_EXP_DISPATCH_DATE", new Variant(""));
        props.put("PR_PRIORITY_HOLD_CAN_FLAG", new Variant(""));
        props.put("PR_INVOICE_NO", new Variant(""));
        props.put("PR_INVOICE_DATE", new Variant(""));
        props.put("PR_LR_NO", new Variant(""));
        props.put("PR_LR_DATE", new Variant(""));
        props.put("PR_INVOICE_PARTY", new Variant(""));
        props.put("PR_PARTY_CODE_ORIGINAL", new Variant(""));
        props.put("PR_PIECE_NO_ORIGINAL", new Variant(""));
        props.put("PR_WH_CODE", new Variant(""));
        props.put("PR_INWARD_NO", new Variant(""));
        props.put("PR_RACK_NO", new Variant(""));
        props.put("PR_PIECE_ID", new Variant(""));
        props.put("PR_LOCATION", new Variant(""));
        props.put("PR_HOLD_DATE", new Variant(""));
        props.put("PR_HOLD_REASON", new Variant(""));
        props.put("PR_RELEASE_DATE", new Variant(""));
        props.put("CREATED_BY", new Variant(""));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(""));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("CHANGED", new Variant(""));
        props.put("CHANGED_DATE", new Variant(""));
        props.put("REJECTED", new Variant(""));
        props.put("REJECTED_BY", new Variant(""));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("APPROVED", new Variant(""));
        props.put("APPROVED_BY", new Variant(""));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("CANCELED", new Variant(""));
        props.put("HIERARCHY_ID", new Variant(""));
        props.put("FROM", new Variant(0));
        props.put("TO", new Variant(0));
        props.put("REJECTED_REMARKS", new Variant(""));
        props.put("REMARKS", new Variant(""));
        props.put("APPROVAL_STATUS", new Variant(""));
        props.put("APPROVER_REMARKS", new Variant(0));
        props.put("ENTRY_DATE", new Variant(0));

        hmPieceDetail = new HashMap();
    }

    public HashMap ShowAllData() {
        Ready = false;
        HashMap hmPieceList = new HashMap();

        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP");
            Ready = true;

            while (resultSet.next()) {
                clsPieceUpdation piece = new clsPieceUpdation();

                piece.setAttribute("PR_PIECE_NO", resultSet.getString("PR_PIECE_NO"));
                piece.setAttribute("PR_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_DATE")));
                piece.setAttribute("PR_ORDER_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_ORDER_DATE")));
                piece.setAttribute("PR_DOC_NO", resultSet.getString("PR_DOC_NO"));
                piece.setAttribute("PR_MACHINE_NO", resultSet.getString("PR_MACHINE_NO"));
                piece.setAttribute("PR_POSITION_NO", resultSet.getString("PR_POSITION_NO"));
                piece.setAttribute("PR_PARTY_CODE", resultSet.getString("PR_PARTY_CODE"));
                piece.setAttribute("PR_PRODUCT_CODE", resultSet.getString("PR_PRODUCT_CODE"));
                piece.setAttribute("PR_GROUP", resultSet.getString("PR_GROUP"));
                piece.setAttribute("PR_STYLE", resultSet.getString("PR_STYLE"));
                piece.setAttribute("PR_LENGTH", resultSet.getString("PR_LENGTH"));
                piece.setAttribute("PR_WIDTH", resultSet.getString("PR_WIDTH"));
                piece.setAttribute("PR_GSM", resultSet.getString("PR_GSM"));
                piece.setAttribute("PR_THORITICAL_WEIGHT", resultSet.getString("PR_THORITICAL_WEIGHT"));
                piece.setAttribute("PR_SQMTR", resultSet.getString("PR_SQMTR"));
                piece.setAttribute("PR_SYN_PER", resultSet.getString("PR_SYN_PER"));
                piece.setAttribute("PR_REQUESTED_MONTH", resultSet.getString("PR_REQUESTED_MONTH"));
                piece.setAttribute("PR_REGION", resultSet.getString("PR_REGION"));
                piece.setAttribute("PR_INCHARGE", resultSet.getString("PR_INCHARGE"));
                piece.setAttribute("PR_REFERENCE", resultSet.getString("PR_REFERENCE"));
                piece.setAttribute("PR_REFERENCE_DATE", resultSet.getString("PR_REFERENCE_DATE"));
                piece.setAttribute("PR_PO_NO", resultSet.getString("PR_PO_NO"));
                piece.setAttribute("PR_PO_DATE", resultSet.getString("PR_PO_DATE"));
                piece.setAttribute("PR_ORDER_REMARK", resultSet.getString("PR_ORDER_REMARK"));
                piece.setAttribute("PR_PIECE_REMARK", resultSet.getString("PR_PIECE_REMARK"));
                piece.setAttribute("PR_PIECE_STAGE", resultSet.getString("PR_PIECE_STAGE"));
                piece.setAttribute("PR_WARP_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_WARP_DATE")));
                piece.setAttribute("PR_WVG_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_WVG_DATE")));
                piece.setAttribute("PR_MND_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_MND_DATE")));
                piece.setAttribute("PR_NDL_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_NDL_DATE")));
                piece.setAttribute("PR_FNSG_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_FNSG_DATE")));
                piece.setAttribute("PR_RCV_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_RCV_DATE")));
                piece.setAttribute("PR_ACTUAL_WEIGHT", resultSet.getString("PR_ACTUAL_WEIGHT"));
                piece.setAttribute("PR_ACTUAL_LENGTH", resultSet.getString("PR_ACTUAL_LENGTH"));
                piece.setAttribute("PR_ACTUAL_WIDTH", resultSet.getString("PR_ACTUAL_WIDTH"));
                piece.setAttribute("PR_BALE_NO", resultSet.getString("PR_BALE_NO"));
                piece.setAttribute("PR_PACKED_DATE", resultSet.getString("PR_PACKED_DATE"));
                piece.setAttribute("PR_REJECTED_FLAG", resultSet.getString("PR_REJECTED_FLAG"));
                piece.setAttribute("PR_REJECTED_REMARK", resultSet.getString("PR_REJECTED_REMARK"));
                piece.setAttribute("PR_DIVERSION_FLAG", resultSet.getString("PR_DIVERSION_FLAG"));
                piece.setAttribute("PR_DIVERSION_REASON", resultSet.getString("PR_DIVERSION_REASON"));
                piece.setAttribute("PR_EXP_DISPATCH_DATE", resultSet.getString("PR_EXP_DISPATCH_DATE"));
                piece.setAttribute("PR_PRIORITY_HOLD_CAN_FLAG", resultSet.getString("PR_PRIORITY_HOLD_CAN_FLAG"));
                piece.setAttribute("PR_INVOICE_NO", resultSet.getString("PR_INVOICE_NO"));
                piece.setAttribute("PR_INVOICE_DATE", resultSet.getString("PR_INVOICE_DATE"));
                piece.setAttribute("PR_LR_NO", resultSet.getString("PR_LR_NO"));
                piece.setAttribute("PR_LR_DATE", resultSet.getString("PR_LR_DATE"));
                piece.setAttribute("PR_INVOICE_PARTY", resultSet.getString("PR_INVOICE_PARTY"));
                piece.setAttribute("PR_PARTY_CODE_ORIGINAL", resultSet.getString("PR_PARTY_CODE_ORIGINAL"));
                piece.setAttribute("PR_PIECE_NO_ORIGINAL", resultSet.getString("PR_PIECE_NO_ORIGINAL"));
                piece.setAttribute("PR_WH_CODE", resultSet.getString("PR_WH_CODE"));
                piece.setAttribute("PR_INWARD_NO", resultSet.getString("PR_INWARD_NO"));
                piece.setAttribute("PR_RACK_NO", resultSet.getString("PR_RACK_NO"));
                piece.setAttribute("PR_PIECE_ID", resultSet.getString("PR_PIECE_ID"));
                piece.setAttribute("PR_LOCATION", resultSet.getString("PR_LOCATION"));
                piece.setAttribute("PR_HOLD_DATE", resultSet.getString("PR_HOLD_DATE"));
                piece.setAttribute("PR_HOLD_REASON", resultSet.getString("PR_HOLD_REASON"));
                piece.setAttribute("PR_RELEASE_DATE", resultSet.getString("PR_RELEASE_DATE"));
                piece.setAttribute("CREATED_DATE", resultSet.getString("CREATED_DATE"));
                piece.setAttribute("CREATED_BY", resultSet.getString("CREATED_BY"));
                piece.setAttribute("MODIFIED_DATE", resultSet.getString("MODIFIED_DATE"));
                piece.setAttribute("MODIFIED_BY", resultSet.getString("MODIFIED_BY"));
                piece.setAttribute("HIERARCHY_ID", resultSet.getString("HIERARCHY_ID"));
                piece.setAttribute("APPROVER_BY", resultSet.getString("APPROVER_BY"));
                piece.setAttribute("APPROVER_DATE", resultSet.getString("APPROVER_DATE"));
                piece.setAttribute("APPROVER_REMARK", resultSet.getString("APPROVER_REMARK"));

                hmPieceList.put(hmPieceList.size() + 1, piece);
            }

            return hmPieceList;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Exceoption " + e.getMessage());
            e.printStackTrace();
            LastError = e.getMessage();
            return hmPieceList;
        }
    }

    public HashMap getPieceByParty(String Party_Code) {
        Ready = false;
        //FELT_SALES_PIECE_REGISTER_TEMP WHERE 
        String SQL = "SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER  WHERE PR_PARTY_CODE!='' AND (PR_MACHINE_NO='0' OR PR_POSITION_NO='0' OR PR_MACHINE_NO='' OR PR_POSITION_NO='' OR PR_MACHINE_NO is NULL OR PR_POSITION_NO is NULL )";
        boolean flag = false;

        if (!Party_Code.equals("")) {
            SQL = SQL + " AND PR_PARTY_CODE = '" + Party_Code + "' ";
            flag = true;
        }

        HashMap hmPieceList = new HashMap();

        try {
            connection = data.getConn();
            System.out.println("SQL : " + SQL);
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(SQL);
            Ready = true;

            while (resultSet.next()) {
                clsPieceUpdation piece = new clsPieceUpdation();

                piece.setAttribute("PR_PIECE_NO", resultSet.getString("PR_PIECE_NO"));
                piece.setAttribute("PR_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_DATE")));
                piece.setAttribute("PR_ORDER_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_ORDER_DATE")));
                piece.setAttribute("PR_DOC_NO", resultSet.getString("PR_DOC_NO"));
                piece.setAttribute("PR_MACHINE_NO", resultSet.getString("PR_MACHINE_NO"));
                piece.setAttribute("PR_POSITION_NO", resultSet.getString("PR_POSITION_NO"));
                piece.setAttribute("PR_PARTY_CODE", resultSet.getString("PR_PARTY_CODE"));
                piece.setAttribute("PR_PRODUCT_CODE", resultSet.getString("PR_PRODUCT_CODE"));
                piece.setAttribute("PR_GROUP", resultSet.getString("PR_GROUP"));
                piece.setAttribute("PR_STYLE", resultSet.getString("PR_STYLE"));
                piece.setAttribute("PR_LENGTH", resultSet.getString("PR_LENGTH"));
                piece.setAttribute("PR_WIDTH", resultSet.getString("PR_WIDTH"));
                piece.setAttribute("PR_GSM", resultSet.getString("PR_GSM"));
                piece.setAttribute("PR_THORITICAL_WEIGHT", resultSet.getString("PR_THORITICAL_WEIGHT"));
                piece.setAttribute("PR_SQMTR", resultSet.getString("PR_SQMTR"));
                piece.setAttribute("PR_SYN_PER", resultSet.getString("PR_SYN_PER"));
                piece.setAttribute("PR_REQUESTED_MONTH", resultSet.getString("PR_REQUESTED_MONTH"));
                piece.setAttribute("PR_REGION", resultSet.getString("PR_REGION"));
                piece.setAttribute("PR_INCHARGE", resultSet.getString("PR_INCHARGE"));
                piece.setAttribute("PR_REFERENCE", resultSet.getString("PR_REFERENCE"));
                piece.setAttribute("PR_REFERENCE_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_REFERENCE_DATE")));
                piece.setAttribute("PR_PO_NO", resultSet.getString("PR_PO_NO"));
                piece.setAttribute("PR_PO_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_PO_DATE")));
                piece.setAttribute("PR_ORDER_REMARK", resultSet.getString("PR_ORDER_REMARK"));
                piece.setAttribute("PR_PIECE_REMARK", resultSet.getString("PR_PIECE_REMARK"));
                piece.setAttribute("PR_PIECE_STAGE", resultSet.getString("PR_PIECE_STAGE"));
                piece.setAttribute("PR_WARP_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_WARP_DATE")));
                piece.setAttribute("PR_WVG_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_WVG_DATE")));
                piece.setAttribute("PR_MND_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_MND_DATE")));
                piece.setAttribute("PR_NDL_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_NDL_DATE")));
                piece.setAttribute("PR_FNSG_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_FNSG_DATE")));
                piece.setAttribute("PR_RCV_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_RCV_DATE")));
                piece.setAttribute("PR_ACTUAL_WEIGHT", resultSet.getString("PR_ACTUAL_WEIGHT"));
                piece.setAttribute("PR_ACTUAL_LENGTH", resultSet.getString("PR_ACTUAL_LENGTH"));
                piece.setAttribute("PR_ACTUAL_WIDTH", resultSet.getString("PR_ACTUAL_WIDTH"));
                piece.setAttribute("PR_BALE_NO", resultSet.getString("PR_BALE_NO"));
                piece.setAttribute("PR_PACKED_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_PACKED_DATE")));
                piece.setAttribute("PR_REJECTED_FLAG", resultSet.getString("PR_REJECTED_FLAG"));
                piece.setAttribute("PR_REJECTED_REMARK", resultSet.getString("PR_REJECTED_REMARK"));
                piece.setAttribute("PR_DIVERSION_FLAG", resultSet.getString("PR_DIVERSION_FLAG"));
                piece.setAttribute("PR_DIVERSION_REASON", resultSet.getString("PR_DIVERSION_REASON"));
                piece.setAttribute("PR_EXP_DISPATCH_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_EXP_DISPATCH_DATE")));
                piece.setAttribute("PR_PRIORITY_HOLD_CAN_FLAG", resultSet.getString("PR_PRIORITY_HOLD_CAN_FLAG"));
                piece.setAttribute("PR_INVOICE_NO", resultSet.getString("PR_INVOICE_NO"));
                piece.setAttribute("PR_INVOICE_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_INVOICE_DATE")));
                piece.setAttribute("PR_LR_NO", resultSet.getString("PR_LR_NO"));
                piece.setAttribute("PR_LR_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_LR_DATE")));
                piece.setAttribute("PR_INVOICE_PARTY", resultSet.getString("PR_INVOICE_PARTY"));
                piece.setAttribute("PR_PARTY_CODE_ORIGINAL", resultSet.getString("PR_PARTY_CODE_ORIGINAL"));
                piece.setAttribute("PR_PIECE_NO_ORIGINAL", resultSet.getString("PR_PIECE_NO_ORIGINAL"));
                piece.setAttribute("PR_WH_CODE", resultSet.getString("PR_WH_CODE"));
                piece.setAttribute("PR_INWARD_NO", resultSet.getString("PR_INWARD_NO"));
                piece.setAttribute("PR_RACK_NO", resultSet.getString("PR_RACK_NO"));
                piece.setAttribute("PR_PIECE_ID", resultSet.getString("PR_PIECE_ID"));
                piece.setAttribute("PR_LOCATION", resultSet.getString("PR_LOCATION"));
                piece.setAttribute("PR_HOLD_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_HOLD_DATE")));
                piece.setAttribute("PR_HOLD_REASON", resultSet.getString("PR_HOLD_REASON"));
                piece.setAttribute("PR_RELEASE_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_RELEASE_DATE")));
                piece.setAttribute("PR_PIECE_STAGE", resultSet.getString("PR_PIECE_STAGE"));
                piece.setAttribute("CREATED_DATE", resultSet.getString("CREATED_DATE"));
                piece.setAttribute("CREATED_BY", resultSet.getString("CREATED_BY"));
                piece.setAttribute("MODIFIED_DATE", resultSet.getString("MODIFIED_DATE"));
                piece.setAttribute("MODIFIED_BY", resultSet.getString("MODIFIED_BY"));
                piece.setAttribute("HIERARCHY_ID", resultSet.getString("HIERARCHY_ID"));
//                piece.setAttribute("APPROVER_BY", resultSet.getString("APPROVER_BY"));
//                piece.setAttribute("APPROVER_DATE", resultSet.getString("APPROVER_DATE"));
//                piece.setAttribute("APPROVER_REMARK", resultSet.getString("APPROVER_REMARK"));

                hmPieceList.put(hmPieceList.size() + 1, piece);
            }

            return hmPieceList;
        } catch (Exception e) {
            
            e.printStackTrace();
            LastError = e.getMessage();
            return hmPieceList;
        }
    }

    public static HashMap getAmendReasonList() {
        HashMap List = new HashMap();
        try {
            Connection Conn = data.getConn();
            Statement stTmp = Conn.createStatement();
            int counter = 1;
            ResultSet rsTmp = stTmp.executeQuery("SELECT 0 AS PARA_CODE,'SELECT REASON CODE' AS PARA_DESC UNION ALL SELECT PARA_CODE,PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID = 'PIECE_UPD_REASON' ORDER BY PARA_CODE");
            while (rsTmp.next()) {
                ComboData aData = new ComboData();
                aData.Code = rsTmp.getInt("PARA_CODE");
                aData.Text = rsTmp.getString("PARA_DESC");
                List.put(new Integer(counter++), aData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return List;
    }

    public boolean LoadData() {
        Ready = false;
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP_HEADER ORDER BY PU_NO");
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
        ResultSet resultSetTemp, resultSetHistory, rsHeader, rsHeaderH;
        Statement statementTemp, statementHistory, stHeader, stHeaderH;
        try {
            // Felt Order Updation data connection
            connection = data.getConn();
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP_DETAIL WHERE PU_NO=''");

            // Felt order Updation data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP_DETAIL_H WHERE PU_NO=''");

            stHeader = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHeader = stHeader.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP_HEADER WHERE PU_NO=''");

            stHeaderH = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHeaderH = stHeaderH.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP_HEADER_H WHERE PU_NO=''");

            rsHeader.first();
            rsHeader.moveToInsertRow();

            rsHeader.updateString("PU_NO", getAttribute("PU_NO").getString());
            rsHeader.updateString("PU_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PU_DATE").getString()));
            rsHeader.updateString("PU_REASON", getAttribute("PU_REASON").getString());
            rsHeader.updateString("PU_PARTY_CODE", getAttribute("PU_PARTY_CODE").getString());

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
            rsHeaderH.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateTimeDB());
            rsHeaderH.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
            rsHeaderH.updateString("PU_NO", getAttribute("PU_NO").getString());
            rsHeaderH.updateString("PU_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PU_DATE").getString()));
            rsHeaderH.updateString("PU_REASON", getAttribute("PU_REASON").getString());
            rsHeaderH.updateString("PU_PARTY_CODE", getAttribute("PU_PARTY_CODE").getString());
            rsHeaderH.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            rsHeaderH.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateString("UPDATED_BY", "0");
            rsHeaderH.updateString("UPDATED_DATE", "0000-00-00");
            rsHeaderH.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
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

            ResultSet rsTmp=data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");
            rsHeaderH.updateString("FROM_IP",""+str_split[1]);
            
            rsHeaderH.insertRow();

            //Now Insert records into FELT_CN_TEMP_HEADER & History tables
            for (int i = 1; i <= hmFeltSalesPieceUpdateDetails.size(); i++) {
                clsPieceUpdation ObjFeltSalesOrderDetails = (clsPieceUpdation) hmFeltSalesPieceUpdateDetails.get(Integer.toString(i));

                //Insert records into Felt order Amendment table
                resultSetTemp.moveToInsertRow();

                //resultSetTemp.updateString("S_ORDER_DETAIL_CODE", "0");
                resultSetTemp.updateInt("SR_NO", i);
                resultSetTemp.updateString("PU_NO", getAttribute("PU_NO").getString());
                
                resultSetTemp.updateString("PR_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PR_PIECE_NO").getString());
                resultSetTemp.updateString("PR_MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("PR_MACHINE_NO").getString());
                resultSetTemp.updateString("PR_POSITION_NO", ObjFeltSalesOrderDetails.getAttribute("PR_POSITION_NO").getString());
                resultSetTemp.updateString("PR_GROUP", ObjFeltSalesOrderDetails.getAttribute("PR_GROUP").getString());
                resultSetTemp.updateString("PR_GSM", ObjFeltSalesOrderDetails.getAttribute("PR_GSM").getString());
                
                resultSetTemp.updateInt("CREATED_BY", EITLERPGLOBAL.gNewUserID);
                resultSetTemp.updateString("CREATED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetTemp.updateInt("MODIFIED_BY", 0);
                resultSetTemp.updateString("MODIFIED_DATE", EITLERPGLOBAL.formatDateDB("0000-00-00"));
                resultSetTemp.insertRow();

                //Insert records into Felt Order Amendment History Table
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO", 1);
                resultSetHistory.updateInt("SR_NO", i);
                resultSetHistory.updateString("PR_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PR_PIECE_NO").getString());
                resultSetHistory.updateString("PU_NO", getAttribute("PU_NO").getString());
              
                resultSetHistory.updateString("PR_MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("PR_MACHINE_NO").getString());
                resultSetHistory.updateString("PR_POSITION_NO", ObjFeltSalesOrderDetails.getAttribute("PR_POSITION_NO").getString());
                resultSetHistory.updateString("PR_GROUP", ObjFeltSalesOrderDetails.getAttribute("PR_GROUP").getString());
                resultSetHistory.updateString("PR_GSM", ObjFeltSalesOrderDetails.getAttribute("PR_GSM").getString());
                
                resultSetHistory.updateInt("CREATED_BY", EITLERPGLOBAL.gNewUserID);
                resultSetHistory.updateString("CREATED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateInt("UPDATED_BY", 0);
                resultSetHistory.updateString("UPDATED_DATE", EITLERPGLOBAL.formatDateDB("0000-00-00"));
                resultSetHistory.insertRow();

            }

            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = ModuleID; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("PU_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator = true;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("REJECTED_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "PU_NO";

            //JOptionPane.showMessageDialog(null, "TO : "+(int)getAttribute("TO").getVal()+", Approval Status = "+(String)getAttribute("APPROVAL_STATUS").getObj());
            if ("A".equals((String) getAttribute("APPROVAL_STATUS").getObj())) {
//                    String Subject = "Felt Order Pending Document : "+getAttribute("PU_NO").getInt();
//                    String Message = "Document No : "+getAttribute("PU_NO").getInt()+" is added in your PENDING DOCUMENT"
//                             + "\n\n\n\n SDML-ERP : http://200.0.0.227:8080/SDMLERP";
//                 
//                    String send_to = clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, (int)getAttribute("TO").getVal());
//
//                    System.out.println("USERID : "+(int)getAttribute("TO").getVal()+", send_to : "+send_to);
//                    try{
//                        JMail.SendMail(EITLERPGLOBAL.SMTPHostIP, "sdmlerp@dineshmills.com", send_to, Message, Subject, "sdmlerp@dineshmills.com");
//                    }catch(Exception e)
//                    {
//                        e.printStackTrace();
//                    }
            }

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

        ResultSet resultSetTemp, resultSetHistory, rsHeader, rsHeaderH, rsRegister;
        Statement statementTemp, statementHistory, stHeader, stHeaderH, stRegister;
        int revisionNo;
        try {
            // Production data connection

            // Production data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //Get the Maximum Revision No in History Table.
            resultSetHistory = statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP_DETAIL_H WHERE PU_NO='1'");
            resultSetHistory.first();
            if (resultSetHistory.getRow() > 0) {
                revisionNo = resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from production data table before insert
            //statementHistory.execute("DELETE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP_DETAIL WHERE PU_NO='"+getAttribute("PU_NO").getString()+"'");

            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP_DETAIL_H WHERE PU_NO=''");

            stHeaderH = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHeaderH = stHeaderH.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP_HEADER_H WHERE PU_NO=''");

            //Now Update records into FELT_CN_TEMP_HEADER tables
            resultSet.updateString("PU_NO", getAttribute("PU_NO").getString());
            resultSet.updateString("PU_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PU_DATE").getString()));
            resultSet.updateString("PU_REASON", getAttribute("PU_REASON").getString());
            resultSet.updateString("PU_PARTY_CODE", getAttribute("PU_PARTY_CODE").getString());

            resultSet.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            //resultSet.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            //resultSet.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB((String)getAttribute("CREATED_DATE").getObj()));
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
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP_HEADER_H WHERE PU_NO='" + getAttribute("PU_NO").getString() + "'");

            RevNo++;
            rsHeaderH.moveToInsertRow();
            rsHeaderH.updateString("REVISION_NO", RevNo + "");
            rsHeaderH.updateString("PU_NO", getAttribute("PU_NO").getString());
            rsHeaderH.updateString("PU_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PU_DATE").getString()));
            rsHeaderH.updateString("PU_REASON", getAttribute("PU_REASON").getString());
            rsHeaderH.updateString("PU_PARTY_CODE", getAttribute("PU_PARTY_CODE").getString());

            rsHeaderH.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsHeaderH.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            rsHeaderH.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateTimeDB());
            rsHeaderH.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
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
            rsHeaderH.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
            rsHeaderH.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
            rsHeaderH.updateInt("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
            rsHeaderH.updateString("MODIFIED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            rsHeaderH.updateInt("UPDATED_BY", EITLERPGLOBAL.gNewUserID);
            rsHeaderH.updateString("UPDATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            
            ResultSet rsTmp=data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");
            rsHeaderH.updateString("FROM_IP",""+str_split[1]);
            
            rsHeaderH.insertRow();

            String OrderNo = getAttribute("PU_NO").getString();

            data.Execute("DELETE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP_DETAIL WHERE PU_NO='" + OrderNo + "'");

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP_DETAIL WHERE PU_NO='1'");

            int RevNoH = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP_DETAIL_H WHERE PU_NO='" + getAttribute("PU_NO").getString() + "'");
            RevNoH++;

            for (int i = 1; i <= hmFeltSalesPieceUpdateDetails.size(); i++) {
                clsPieceUpdation ObjFeltSalesOrderDetails = (clsPieceUpdation) hmFeltSalesPieceUpdateDetails.get(Integer.toString(i));

                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateInt("SR_NO", i);
                //resultSetTemp.updateString("PU_NO", ObjFeltSalesOrderDetails.getAttribute("PU_NO").getString()+"");
                resultSetTemp.updateString("PU_NO", getAttribute("PU_NO").getString() + "");
                resultSetTemp.updateString("PR_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PR_PIECE_NO").getString());
                resultSetTemp.updateString("PR_MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("PR_MACHINE_NO").getString());
                resultSetTemp.updateString("PR_POSITION_NO", ObjFeltSalesOrderDetails.getAttribute("PR_POSITION_NO").getString());
                resultSetTemp.updateString("PR_GROUP", ObjFeltSalesOrderDetails.getAttribute("PR_GROUP").getString());
                resultSetTemp.updateString("PR_GSM", ObjFeltSalesOrderDetails.getAttribute("PR_GSM").getString());
                resultSetTemp.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                resultSetTemp.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
                resultSetTemp.updateInt("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
                resultSetTemp.updateString("MODIFIED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                //resultSetTemp.updateBoolean("APPROVED",false);
                //resultSetTemp.updateString("APPROVED_DATE","0000-00-00");
                //resultSetTemp.updateBoolean("REJECTED",false);
                //resultSetTemp.updateString("REJECTED_DATE","0000-00-00");
                //resultSetTemp.updateInt("CANCELED",0);
                //resultSetTemp.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
                //resultSetTemp.updateInt("CHANGED",1);
                //resultSetTemp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetTemp.insertRow();

                resultSetHistory.moveToInsertRow();

                resultSetHistory.updateInt("REVISION_NO", RevNoH);
                resultSetHistory.updateInt("SR_NO", i);

                resultSetHistory.updateString("PU_NO", ObjFeltSalesOrderDetails.getAttribute("PU_NO").getString() + "");
                resultSetHistory.updateString("PR_PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PR_PIECE_NO").getString());
                resultSetHistory.updateString("PU_NO", getAttribute("PU_NO").getString() + "");
                if (getAttribute("PU_REASON").getString().equals("1")) {

                    resultSetHistory.updateString("PR_MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("PR_MACHINE_NO").getString());
                    resultSetHistory.updateString("PR_POSITION_NO", ObjFeltSalesOrderDetails.getAttribute("PR_POSITION_NO").getString());
                    resultSetHistory.updateString("PR_GROUP", ObjFeltSalesOrderDetails.getAttribute("PR_GROUP").getString());
                    resultSetHistory.updateString("PR_GSM", ObjFeltSalesOrderDetails.getAttribute("PR_GSM").getString());
                    //IF FINAL APPROVAL THAN MACHINE AND POSITION UPDATED
                    if (getAttribute("APPROVAL_STATUS").getString().equals("F"))
                    {
                            System.out.println("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_MACHINE_NO='"+ObjFeltSalesOrderDetails.getAttribute("PR_MACHINE_NO").getString()+"', PR_POSITION_NO='"+ObjFeltSalesOrderDetails.getAttribute("PR_POSITION_NO").getString()+"' WHERE PR_PIECE_NO='"+ObjFeltSalesOrderDetails.getAttribute("PR_PIECE_NO").getString()+"'");
                            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_MACHINE_NO='"+ObjFeltSalesOrderDetails.getAttribute("PR_MACHINE_NO").getString()+"', PR_POSITION_NO='"+ObjFeltSalesOrderDetails.getAttribute("PR_POSITION_NO").getString()+"' WHERE PR_PIECE_NO='"+ObjFeltSalesOrderDetails.getAttribute("PR_PIECE_NO").getString()+"'");
                    }
                }
                else if (getAttribute("PU_REASON").getString().equals("8")) {

                    resultSetHistory.updateString("PR_MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("PR_MACHINE_NO").getString());
                    resultSetHistory.updateString("PR_POSITION_NO", ObjFeltSalesOrderDetails.getAttribute("PR_POSITION_NO").getString());
                    resultSetHistory.updateString("PR_GROUP", ObjFeltSalesOrderDetails.getAttribute("PR_GROUP").getString());
                    resultSetHistory.updateString("PR_GSM", ObjFeltSalesOrderDetails.getAttribute("PR_GSM").getString());
                    //IF FINAL APPROVAL THAN MACHINE AND POSITION UPDATED
                    if (getAttribute("APPROVAL_STATUS").getString().equals("F"))
                    {
                            System.out.println("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_GROUP='"+ObjFeltSalesOrderDetails.getAttribute("PR_GROUP").getString()+"', PR_GSM='"+ObjFeltSalesOrderDetails.getAttribute("PR_GSM").getString()+"' WHERE PR_PIECE_NO='"+ObjFeltSalesOrderDetails.getAttribute("PR_PIECE_NO").getString()+"'");
                            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_GROUP='"+ObjFeltSalesOrderDetails.getAttribute("PR_GROUP").getString()+"', PR_GSM='"+ObjFeltSalesOrderDetails.getAttribute("PR_GSM").getString()+"' WHERE PR_PIECE_NO='"+ObjFeltSalesOrderDetails.getAttribute("PR_PIECE_NO").getString()+"'");
                    }
                }
                else {
                    resultSetHistory.updateString("PR_MACHINE_NO", "");
                    resultSetHistory.updateString("PR_POSITION_NO", "");
                    
                }

                resultSetHistory.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                resultSetHistory.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
                resultSetHistory.updateInt("UPDATED_BY", EITLERPGLOBAL.gNewUserID);
                resultSetHistory.updateString("UPDATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                resultSetHistory.insertRow();

                // Final Approval and save to PIECE REGISTER 
                if (getAttribute("APPROVAL_STATUS").getString().equals("F") && !ObjFeltSalesOrderDetails.getAttribute("PU_NO").getString().equals("")) {
//                    stRegister=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
//                    rsRegister=stRegister.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO=''");
//                  
//                    rsRegister.moveToInsertRow();
//
//                    rsRegister.updateString("PR_PIECE_NO",ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
//                    rsRegister.updateString("PR_DATE",EITLERPGLOBAL.getCurrentDateDB());
//                    rsRegister.updateString("PR_ORDER_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("PU_DATE").getString()));
//                    rsRegister.updateString("PR_DOC_NO", ObjFeltSalesOrderDetails.getAttribute("PU_NO").getString()+"");
//                    rsRegister.updateString("PR_MACHINE_NO", ObjFeltSalesOrderDetails.getAttribute("MACHINE_NO").getString());
//                    rsRegister.updateString("PR_POSITION_NO",ObjFeltSalesOrderDetails.getAttribute("POSITION").getString());
//                    rsRegister.updateString("PR_PARTY_CODE",getAttribute("PARTY_CODE").getString());
//                    rsRegister.updateString("PR_PRODUCT_CODE",ObjFeltSalesOrderDetails.getAttribute("PRODUCT_CODE").getString());
//                    rsRegister.updateString("PR_GROUP",ObjFeltSalesOrderDetails.getAttribute("S_GROUP").getString());
//                    rsRegister.updateString("PR_STYLE",ObjFeltSalesOrderDetails.getAttribute("STYLE").getString());
//                    rsRegister.updateString("PR_LENGTH",ObjFeltSalesOrderDetails.getAttribute("LENGTH").getString());
//                    rsRegister.updateString("PR_WIDTH",ObjFeltSalesOrderDetails.getAttribute("WIDTH").getString());
//                    rsRegister.updateString("PR_GSM",ObjFeltSalesOrderDetails.getAttribute("GSM").getString());
//                    rsRegister.updateString("PR_THORITICAL_WEIGHT",ObjFeltSalesOrderDetails.getAttribute("THORITICAL_WIDTH").getString());
//                    rsRegister.updateString("PR_SQMTR",ObjFeltSalesOrderDetails.getAttribute("SQ_MTR").getString());
//                    rsRegister.updateString("PR_SYN_PER",ObjFeltSalesOrderDetails.getAttribute("SYN_PER").getString());
//                    rsRegister.updateString("PR_REQUESTED_MONTH",ObjFeltSalesOrderDetails.getAttribute("REQ_MONTH").getString());
//                    rsRegister.updateString("PR_REGION",getAttribute("REGION").getString());
//                    rsRegister.updateString("PR_INCHARGE",getAttribute("SALES_ENGINEER").getString());
//                    rsRegister.updateString("PR_REFERENCE",getAttribute("REFERENCE").getString());
//                    rsRegister.updateString("PR_REFERENCE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("REFERENCE_DATE").getString()));
//                    rsRegister.updateString("PR_PO_NO",getAttribute("P_O_NO").getString());
//                    rsRegister.updateString("PR_PO_DATE",EITLERPGLOBAL.formatDateDB((String)getAttribute("P_O_DATE").getObj()));
//                    rsRegister.updateString("PR_ORDER_REMARK",getAttribute("REMARK").getString());
//                    rsRegister.updateString("PR_PIECE_REMARK",ObjFeltSalesOrderDetails.getAttribute("REMARK").getString());
//                    rsRegister.updateString("PR_PIECE_STAGE","ORDERED");
//                    rsRegister.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
//                    rsRegister.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
//                    rsRegister.updateString("MODIFIED_BY",EITLERPGLOBAL.gNewUserID+"");
//                    rsRegister.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
//                    rsRegister.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
//                    rsRegister.updateString("APPROVER_BY",EITLERPGLOBAL.gNewUserID+"");
//                    rsRegister.updateString("APPROVER_DATE",EITLERPGLOBAL.getCurrentDateDB());
//                    rsRegister.updateString("APPROVER_REMARK",getAttribute("APPROVER_REMARKS").getString());
//
//                    rsRegister.insertRow();
                }
            }

            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = ModuleID; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("PU_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator = true;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("REJECTED_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "PU_NO";

            if (getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFeltProductionApprovalFlow.To = (int) getAttribute("SEND_DOC_TO").getVal();
                ObjFeltProductionApprovalFlow.ExplicitSendTo = true;
            }
            if (getAttribute("APPROVAL_STATUS").getString().equals("A") || getAttribute("APPROVAL_STATUS").getString().equals("R")) {
//                String Subject = "Felt Piece Updation Pending Document : " + getAttribute("PU_NO").getInt();
//                String Message = "Document No : " + getAttribute("PU_NO").getInt() + " is added in your PENDING DOCUMENT"
//                        + "\n\n\n\n SDML-ERP : http://200.0.0.227:8080/SDMLERP";
//
//                String send_to = clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, (int) getAttribute("TO").getVal());
//
//                System.out.println("USERID : " + (int) getAttribute("TO").getVal() + ", send_to : " + send_to);
//                try {
//                    JMail.SendMail(EITLERPGLOBAL.SMTPHostIP, "sdmlerp@dineshmills.com", send_to, Message, Subject, "sdmlerp@dineshmills.com");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }

            //==== Handling Rejected Documents ==========//
            boolean IsRejected = getAttribute("REJECTED").getBool();

            if (IsRejected) {
                //Remove the Rejected Flag First
                
                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP_HEADER SET REJECTED=0,CHANGED=1 WHERE PU_NO ='" + getAttribute("PU_NO").getString() + "'");
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP_HEADER WHERE  PU_NO='" + documentNo + "' AND APPROVED=" + 1;
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
                    strSQL = "DELETE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP_HEADER WHERE "
                            + " PU_DATE = '" + EITLERPGLOBAL.formatDateDB(stringProductionDate) + "'"
                            + " AND PU_NO ='" + documentNo + "'";

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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP_HEADER WHERE PU_NO='" + orderupdDocumentNo + "' AND APPROVED=1";
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
            String strSql = "SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP_HEADER WHERE  " + stringFindQuery + "";
            System.out.println("STR SQL UPDATION : " + strSql);
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
        try {

            setAttribute("REVISION_NO", 0);
            
            setAttribute("PU_NO", resultSet.getString("PU_NO"));
            setAttribute("PU_DATE", resultSet.getDate("PU_DATE"));
            setAttribute("PU_REASON", resultSet.getString("PU_REASON"));
            setAttribute("PU_PARTY_CODE", resultSet.getString("PU_PARTY_CODE"));
            setAttribute("CREATED_BY", resultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE", resultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY", resultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE", resultSet.getString("MODIFIED_DATE"));
            setAttribute("APPROVED", resultSet.getInt("APPROVED"));
            setAttribute("APPROVED_BY", resultSet.getInt("APPROVED_BY"));
            setAttribute("APPROVED_DATE", resultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED", resultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_BY", resultSet.getBoolean("REJECTED_BY"));
            setAttribute("REJECTED_DATE", resultSet.getString("REJECTED_DATE"));
            setAttribute("CANCELED", resultSet.getInt("CANCELED"));
            setAttribute("HIERARCHY_ID", resultSet.getInt("HIERARCHY_ID"));
            //setAttribute("REJECTED_REMARKS",resultSet.getString("REJECTED_REMARKS"));

            if(HistoryView) {
                RevNo=resultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",resultSet.getInt("REVISION_NO"));
            } 
            
            hmFeltSalesPieceUpdateDetails.clear();

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP_DETAIL WHERE PU_NO='" + resultSet.getString("PU_NO") + "'  ORDER BY PU_NO");

            while (resultSetTemp.next()) {
                serialNoCounter++;

                clsPieceUpdation ObjFeltSalesOrderDetails = new clsPieceUpdation();

                ObjFeltSalesOrderDetails.setAttribute("SR_NO", serialNoCounter);
                ObjFeltSalesOrderDetails.setAttribute("PU_NO", resultSetTemp.getString("PU_NO"));
                ObjFeltSalesOrderDetails.setAttribute("PR_PIECE_NO", resultSetTemp.getString("PR_PIECE_NO"));
                ObjFeltSalesOrderDetails.setAttribute("PR_DATE", resultSetTemp.getString("PR_DATE"));
                ObjFeltSalesOrderDetails.setAttribute("PR_DOC_NO", resultSetTemp.getString("PR_DOC_NO"));
                ObjFeltSalesOrderDetails.setAttribute("PR_MACHINE_NO", resultSetTemp.getString("PR_MACHINE_NO"));
                ObjFeltSalesOrderDetails.setAttribute("PR_POSITION_NO", resultSetTemp.getString("PR_POSITION_NO"));
                //ObjFeltSalesOrderDetails.setAttribute("PR_PARTY_CODE", resultSetTemp.getString("PR_PARTY_CODE"));
                ObjFeltSalesOrderDetails.setAttribute("PR_GROUP", resultSetTemp.getString("PR_GROUP"));
                ObjFeltSalesOrderDetails.setAttribute("PR_GSM", resultSetTemp.getString("PR_GSM"));

                hmFeltSalesPieceUpdateDetails.put(Integer.toString(serialNoCounter), ObjFeltSalesOrderDetails);
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
            hmFeltSalesPieceUpdateDetails.clear();

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP_HEADER_H WHERE PU_NO='" + pDocNo + "'");

            while (resultSetTemp.next()) {
                serialNoCounter++;
                setAttribute("PU_NO", resultSetTemp.getString("PU_NO"));
                setAttribute("UPDATED_BY", resultSetTemp.getString("UPDATED_BY"));
                setAttribute("PU_DATE", resultSetTemp.getString("PU_DATE"));
                setAttribute("HIERARCHY_ID", resultSetTemp.getInt("HIERARCHY_ID"));

                clsPieceUpdation ObjFeltSalesOrderDetails = new clsPieceUpdation();

                //ObjFeltSalesOrderDetails.setAttribute("SR_NO",serialNoCounter);
                // ObjFeltSalesOrderDetails.setAttribute("S_ORDER_DETAIL_CODE",resultSetTemp.getString("S_ORDER_DETAIL_CODE"));
                ObjFeltSalesOrderDetails.setAttribute("PU_NO", resultSetTemp.getString("PU_NO"));
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

                hmFeltSalesPieceUpdateDetails.put(Integer.toString(serialNoCounter), ObjFeltSalesOrderDetails);
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
            System.out.println("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP_HEADER_H WHERE PU_NO='" + productionDocumentNo + "'");
            rsTmp = stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP_HEADER_H WHERE PU_NO='" + productionDocumentNo + "'");
            while (rsTmp.next()) {
                clsPieceUpdation felt_order = new clsPieceUpdation();

                felt_order.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                felt_order.setAttribute("UPDATED_BY", rsTmp.getString("UPDATED_BY"));
                felt_order.setAttribute("UPDATED_DATE", rsTmp.getString("UPDATED_DATE"));
                felt_order.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                felt_order.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                felt_order.setAttribute("REJECTED_REMARKS", rsTmp.getString("REJECTED_REMARKS"));
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

    public boolean ShowHistory(String pProductionDate, String pDocNo) {
        Ready = false;
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP_HEADER_H WHERE PU_NO ='" + pDocNo + "'");
            System.out.println("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP_HEADER_H WHERE PU_NO ='" + pDocNo + "'");    
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
                strSQL = "SELECT DISTINCT PU_NO,PU_DATE,RECEIVED_DATE,PU_PARTY_CODE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP_HEADER_H, PRODUCTION.FELT_PROD_DOC_DATA   WHERE PU_NO=DOC_NO AND STATUS='W' AND MODULE_ID=" + ModuleID + " AND CANCELED=0  AND USER_ID=" + pUserID + " ORDER BY RECEIVED_DATE,PU_NO";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT PU_NO,PU_DATE,RECEIVED_DATE,PU_PARTY_CODE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP_HEADER_H, PRODUCTION.FELT_PROD_DOC_DATA   WHERE PU_NO=DOC_NO AND STATUS='W' AND MODULE_ID=" + ModuleID + " AND CANCELED=0  AND USER_ID=" + pUserID + " ORDER BY RECEIVED_DATE,PU_NO";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT PU_NO,PU_DATE,RECEIVED_DATE,PU_PARTY_CODE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP_HEADER_H, PRODUCTION.FELT_PROD_DOC_DATA   WHERE PU_NO=DOC_NO AND STATUS='W' AND MODULE_ID=" + ModuleID + " AND CANCELED=0  AND USER_ID=" + pUserID + "  ORDER BY PU_NO";
            }

            ResultSet rsTmp = data.getConn().createStatement().executeQuery(strSQL);

            Counter = 0;
            while (rsTmp.next()) {

                Counter = Counter + 1;
                clsPieceUpdation ObjDoc = new clsPieceUpdation();

                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO", rsTmp.getString("PU_NO"));
                ObjDoc.setAttribute("DOC_DATE", rsTmp.getString("PU_DATE"));
                ObjDoc.setAttribute("RECEIVED_DATE", rsTmp.getString("RECEIVED_DATE"));
                ObjDoc.setAttribute("PARTY_CODE", rsTmp.getString("PU_PARTY_CODE"));

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
}
