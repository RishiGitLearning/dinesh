/*
 * clsFeltPacking.java
 *
 * Created on July 19, 2013, 5:31 PM
 */
package EITLERP.FeltSales.FeltScheme;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import EITLERP.EITLERPGLOBAL;
import EITLERP.Variant;
import EITLERP.data;
import EITLERP.Production.clsFeltProductionApprovalFlow;
import EITLERP.SelectFirstFree;

/**
 * @author Vivek Kumar
 */
public class clsFeltScheme {

    public String LastError = "";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private int FFNo = 0;
    public HashMap props;
    public boolean Ready = false;
    //Felt Packing  Details Collection
    public HashMap hmFeltPackingDetails;

    //History Related properties
    public boolean HistoryView = false;

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
     * Creates new DataFeltPacking
     */
    public clsFeltScheme() {
        LastError = "";
        props = new HashMap();
        props.put("DOC_NO", new Variant(""));
        props.put("DOC_DATE", new Variant(""));
        props.put("PKG_PARTY_CODE", new Variant(""));
        props.put("PKG_PARTY_NAME", new Variant(""));
        props.put("ORDER_FROM_DATE", new Variant(""));
        props.put("ORDER_TO_DATE", new Variant(""));
        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("FIN_YEAR", new Variant(""));
        // -- Approval Specific Fields --
        props.put("HIERARCHY_ID", new Variant(0));
        props.put("FROM", new Variant(0));
        props.put("TO", new Variant(0));
        props.put("REMARKS", new Variant(""));
        props.put("APPROVAL_STATUS", new Variant(""));
        props.put("UPDATED_BY", new Variant(0));
        props.put("ENTRY_DATE", new Variant(""));
        props.put("APPROVER_REMARKS", new Variant(""));
        props.put("CANCELED", new Variant(0));
        props.put("APPROVED", new Variant(0));
        props.put("REJECTED", new Variant(0));
        props.put("FROM_IP", new Variant(""));
        props.put("INVOICE_FLG", new Variant(0));
        props.put("BALE_REOPEN_FLG", new Variant(0));
        props.put("PKG_WH_CD", new Variant(2));

        hmFeltPackingDetails = new HashMap();
    }

    public boolean LoadData() {
        Ready = false;
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_SAL_SCHEME_HEADER ORDER BY DOC_DATE,DOC_NO");
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
            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    public boolean Insert() {
        ResultSet resultSetDetail, resultSetDetailHistory, resultSetHistory;
        Statement statementDetail, statementDetailHistory, statementHistory;
        try {
            // Packing detail connection
            statementDetail = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetDetail = statementDetail.executeQuery("SELECT * FROM  PRODUCTION.FELT_SAL_SCHEME_DETAIL WHERE DOC_NO=''");

            // Packing detail history connection
            statementDetailHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetDetailHistory = statementDetailHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_SAL_SCHEME_DETAIL_H WHERE DOC_NO=''");

            // Packing data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_SAL_SCHEME_HEADER_H WHERE DOC_NO=''");

            //--------- Generate Bale no. ---------------------
            
            String mst = getAttribute("DOC_NO").getString().substring(0,1);
            if(mst.matches("A")){
                setAttribute("DOC_NO",EITLERP.clsFirstFree.getNextFreeNo(2,771,getAttribute("FFNO").getInt(),true));
            }
            if(mst.matches("E")){
                setAttribute("DOC_NO",EITLERP.clsFirstFree.getNextFreeNo(2,771,getAttribute("FFNO").getInt(),true));
            }
            if(mst.matches("R")){
                setAttribute("DOC_NO", EITLERP.clsFirstFree.getNextFreeNo(2, 771, getAttribute("FFNO").getInt(), true));
            }//-------------------------------------------------

            resultSet.moveToInsertRow();
            resultSet.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSet.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            resultSet.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            resultSet.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
            resultSet.updateString("ORDER_FROM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ORDER_FROM_DATE").getString()));
            resultSet.updateString("ORDER_TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ORDER_TO_DATE").getString()));
            resultSet.updateInt("FIN_YEAR", getAttribute("FIN_YEAR").getInt());
            resultSet.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
            resultSet.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateInt("MODIFIED_BY", 0);
            resultSet.updateString("MODIFIED_DATE", "0000-00-00");
            resultSet.updateBoolean("APPROVED", false);
            resultSet.updateString("APPROVED_DATE", "0000-00-00");
            resultSet.updateBoolean("REJECTED", false);
            resultSet.updateString("REJECTED_DATE", "0000-00-00");
            resultSet.updateInt("CANCELED", 0);
            resultSet.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            resultSet.updateInt("CHANGED", 1);
            resultSet.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSet.insertRow();

            //========= Inserting Into Header History =================//
            resultSetHistory.moveToInsertRow();
            resultSetHistory.updateInt("REVISION_NO", 1);
            resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSetHistory.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            resultSetHistory.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            resultSetHistory.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
            resultSetHistory.updateString("ORDER_FROM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ORDER_FROM_DATE").getString()));
            resultSetHistory.updateString("ORDER_TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ORDER_TO_DATE").getString()));
            resultSetHistory.updateInt("FIN_YEAR", getAttribute("FIN_YEAR").getInt());
            resultSetHistory.updateInt("UPDATED_BY", getAttribute("UPDATED_BY").getInt());
            resultSetHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSetHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            resultSetHistory.updateString("APPROVER_REMARKS", getAttribute("FROM_REMARKS").getString());
            resultSetHistory.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            resultSetHistory.updateInt("CHANGED", 1);
            resultSetHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSetHistory.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
            resultSetHistory.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            ResultSet rsTmp=data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");
            
            resultSetHistory.updateString("FROM_IP",""+str_split[1]);
            
            
            resultSetHistory.insertRow();

            for (int i = 1; i <= hmFeltPackingDetails.size(); i++) {
                clsFeltSchemeDetails ObjFeltPackingDetails = (clsFeltSchemeDetails) hmFeltPackingDetails.get(Integer.toString(i));

                //Insert records into Packing detail table
                resultSetDetail.moveToInsertRow();
                resultSetDetail.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetDetail.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                resultSetDetail.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
                resultSetDetail.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
                resultSetDetail.updateString("ORDER_FROM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ORDER_FROM_DATE").getString()));
                resultSetDetail.updateString("ORDER_TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ORDER_TO_DATE").getString()));
                resultSetDetail.updateInt("FIN_YEAR", getAttribute("FIN_YEAR").getInt());
                resultSetDetail.updateString("PIECE_NO", ObjFeltPackingDetails.getAttribute("PIECE_NO").getString());
                resultSetDetail.updateString("MC_POSITION", ObjFeltPackingDetails.getAttribute("MC_POSITION").getString());
                resultSetDetail.updateString("MC_POSITION_DESC", ObjFeltPackingDetails.getAttribute("MC_POSITION_DESC").getString());
                resultSetDetail.updateString("MC_NO", ObjFeltPackingDetails.getAttribute("MC_NO").getString());
                resultSetDetail.updateString("ORDER_NO", ObjFeltPackingDetails.getAttribute("ORDER_NO").getString());
                resultSetDetail.updateString("ORDER_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltPackingDetails.getAttribute("ORDER_DATE").getString()));
                resultSetDetail.updateString("REQ_MONTH", ObjFeltPackingDetails.getAttribute("REQ_MONTH").getString());
                resultSetDetail.updateString("PARTY_CODE1", ObjFeltPackingDetails.getAttribute("PARTY_CODE1").getString());
                resultSetDetail.updateString("PARTY_NAME1", ObjFeltPackingDetails.getAttribute("PARTY_NAME1").getString());
                resultSetDetail.updateString("EMAIL", ObjFeltPackingDetails.getAttribute("EMAIL").getString());
                resultSetDetail.updateString("YEAR", ObjFeltPackingDetails.getAttribute("YEAR").getString());
                resultSetDetail.updateString("PRODUCT_CODE", ObjFeltPackingDetails.getAttribute("PRODUCT_CODE").getString());
                resultSetDetail.updateString("GROUP_NAME", ObjFeltPackingDetails.getAttribute("GROUP_NAME").getString());
                resultSetDetail.updateString("PRODUCT_GROUP_DESC", ObjFeltPackingDetails.getAttribute("PRODUCT_GROUP_DESC").getString());
                resultSetDetail.updateFloat("BASIC_VALUE", (float) ObjFeltPackingDetails.getAttribute("BASIC_VALUE").getVal());
                resultSetDetail.updateFloat("DISCOUNT",(float) ObjFeltPackingDetails.getAttribute("DISCOUNT").getVal());
                resultSetDetail.updateFloat("ORDER_AMT",(float) ObjFeltPackingDetails.getAttribute("ORDER_AMT").getVal());
                resultSetDetail.updateFloat("LENGTH",(float) ObjFeltPackingDetails.getAttribute("LENGTH").getVal());
                resultSetDetail.updateFloat("WIDTH",(float) ObjFeltPackingDetails.getAttribute("WIDTH").getVal());
                resultSetDetail.updateFloat("GSM",(float) ObjFeltPackingDetails.getAttribute("GSM").getVal());
                resultSetDetail.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
                resultSetDetail.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetail.updateInt("MODIFIED_BY", 0);
                resultSetDetail.updateString("MODIFIED_DATE", "0000-00-00");
                resultSetDetail.updateInt("CANCELED", 0);
                resultSetDetail.updateInt("CHANGED", 1);
                resultSetDetail.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetail.insertRow();

                //Insert records into Packing detail history table
                resultSetDetailHistory.moveToInsertRow();
                resultSetDetailHistory.updateInt("REVISION_NO", 1);
                resultSetDetailHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetDetailHistory.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                resultSetDetailHistory.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
                resultSetDetailHistory.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
                resultSetDetailHistory.updateString("ORDER_FROM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ORDER_FROM_DATE").getString()));
                resultSetDetailHistory.updateString("ORDER_TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ORDER_TO_DATE").getString()));
                resultSetDetailHistory.updateInt("FIN_YEAR", getAttribute("FIN_YEAR").getInt());
                resultSetDetailHistory.updateString("MC_POSITION", ObjFeltPackingDetails.getAttribute("MC_POSITION").getString());
                resultSetDetailHistory.updateString("MC_POSITION_DESC", ObjFeltPackingDetails.getAttribute("MC_POSITION_DESC").getString());
                resultSetDetailHistory.updateString("MC_NO", ObjFeltPackingDetails.getAttribute("MC_NO").getString());
                resultSetDetailHistory.updateString("ORDER_NO", ObjFeltPackingDetails.getAttribute("ORDER_NO").getString());
                resultSetDetailHistory.updateString("ORDER_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltPackingDetails.getAttribute("ORDER_DATE").getString()));
                resultSetDetailHistory.updateString("REQ_MONTH", ObjFeltPackingDetails.getAttribute("REQ_MONTH").getString());
                resultSetDetailHistory.updateString("PIECE_NO", ObjFeltPackingDetails.getAttribute("PIECE_NO").getString());
                resultSetDetailHistory.updateString("PARTY_CODE1", ObjFeltPackingDetails.getAttribute("PARTY_CODE1").getString());
                resultSetDetailHistory.updateString("PARTY_NAME1", ObjFeltPackingDetails.getAttribute("PARTY_NAME1").getString());
                resultSetDetailHistory.updateString("EMAIL", ObjFeltPackingDetails.getAttribute("EMAIL").getString());
                resultSetDetailHistory.updateString("YEAR", ObjFeltPackingDetails.getAttribute("YEAR").getString());
                resultSetDetailHistory.updateString("PRODUCT_CODE", ObjFeltPackingDetails.getAttribute("PRODUCT_CODE").getString());
                resultSetDetailHistory.updateString("GROUP_NAME", ObjFeltPackingDetails.getAttribute("GROUP_NAME").getString());
                resultSetDetailHistory.updateString("PRODUCT_GROUP_DESC", ObjFeltPackingDetails.getAttribute("PRODUCT_GROUP_DESC").getString());
                resultSetDetailHistory.updateFloat("BASIC_VALUE", (float) ObjFeltPackingDetails.getAttribute("BASIC_VALUE").getVal());
                resultSetDetailHistory.updateFloat("DISCOUNT",(float) ObjFeltPackingDetails.getAttribute("DISCOUNT").getVal());
                resultSetDetailHistory.updateFloat("ORDER_AMT",(float) ObjFeltPackingDetails.getAttribute("ORDER_AMT").getVal());
                resultSetDetailHistory.updateFloat("LENGTH",(float) ObjFeltPackingDetails.getAttribute("LENGTH").getVal());
                resultSetDetailHistory.updateFloat("WIDTH",(float) ObjFeltPackingDetails.getAttribute("WIDTH").getVal());
                resultSetDetailHistory.updateFloat("GSM",(float) ObjFeltPackingDetails.getAttribute("GSM").getVal());
                resultSetDetailHistory.updateInt("UPDATED_BY", getAttribute("UPDATED_BY").getInt());
                resultSetDetailHistory.updateInt("CHANGED", 1);
                resultSetDetailHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetailHistory.insertRow();
            }

            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = 771; //Felt Packing
            ObjFeltProductionApprovalFlow.DocNo = getDocumentNo(getAttribute("DOC_NO").getString());
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName = "FELT_SAL_SCHEME_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator = true;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "DOC_NO";

            if (ObjFeltProductionApprovalFlow.Status.equals("H")) {
                ObjFeltProductionApprovalFlow.Status = "A";
                ObjFeltProductionApprovalFlow.To = ObjFeltProductionApprovalFlow.From;
                ObjFeltProductionApprovalFlow.UpdateFlow();
                data.Execute("UPDATE PRODUCTION.FELT_SAL_SCHEME_DETAIL A,PRODUCTION.FELT_SALES_PIECE_REGISTER B SET B.PR_DOC_NO='"+getAttribute("DOC_NO").getString()+"',B.PR_DOC_DATE='"+EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString())+"' WHERE B.PR_PIECE_NO=A.PKG_PIECE_NO AND A.DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
            } else {
                if (!ObjFeltProductionApprovalFlow.UpdateFlow()) {
                    LastError = ObjFeltProductionApprovalFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------

            MoveLast();
            resultSetDetail.close();
            statementDetail.close();
            resultSetDetailHistory.close();
            statementDetailHistory.close();
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
        ResultSet resultSetDetail, resultSetDetailHistory, resultSetHistory;
        Statement statementDetail, statementDetailHistory, statementHistory;

        try {
            // Packing detail connection
            statementDetail = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetDetail = statementDetail.executeQuery("SELECT * FROM  PRODUCTION.FELT_SAL_SCHEME_DETAIL WHERE DOC_NO=''");

            // Packing detail history connection
            statementDetailHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetDetailHistory = statementDetailHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_SAL_SCHEME_DETAIL_H WHERE DOC_NO=''");

            // Packing data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_SAL_SCHEME_HEADER_H WHERE DOC_NO=''");

            resultSet.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSet.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            resultSet.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            resultSet.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
            resultSet.updateString("ORDER_FROM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ORDER_FROM_DATE").getString()));
            resultSet.updateString("ORDER_TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ORDER_TO_DATE").getString()));
            resultSet.updateInt("FIN_YEAR", getAttribute("FIN_YEAR").getInt());
            resultSet.updateInt("MODIFIED_BY", getAttribute("MODIFIED_BY").getInt());
            resultSet.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            resultSet.updateInt("CHANGED", 1);
            resultSet.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateRow();

            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int revisionNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_SAL_SCHEME_HEADER_H WHERE DOC_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()) + "' AND DOC_NO='" + getAttribute("DOC_NO").getString() + "'");
            revisionNo++;

            resultSetHistory.moveToInsertRow();
            resultSetHistory.updateInt("REVISION_NO",revisionNo);
            resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSetHistory.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            resultSetHistory.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            resultSetHistory.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
            resultSetHistory.updateString("ORDER_FROM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ORDER_FROM_DATE").getString()));
            resultSetHistory.updateString("ORDER_TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ORDER_TO_DATE").getString()));
            resultSetHistory.updateInt("FIN_YEAR", getAttribute("FIN_YEAR").getInt());
            resultSetHistory.updateInt("UPDATED_BY", getAttribute("UPDATED_BY").getInt());
            resultSetHistory.updateString("ENTRY_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSetHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            resultSetHistory.updateString("APPROVER_REMARKS", getAttribute("FROM_REMARKS").getString());
            resultSetHistory.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            resultSetHistory.updateInt("CHANGED", 1);
            resultSetHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSetHistory.updateInt("MODIFIED_BY", getAttribute("CREATED_BY").getInt());
            resultSetHistory.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB()); 
            ResultSet rsTmp=data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");
            
            resultSetHistory.updateString("FROM_IP",""+str_split[1]);
            resultSetHistory.insertRow();

            //delete records from Packing detail table before insert
            data.Execute("DELETE FROM PRODUCTION.FELT_SAL_SCHEME_DETAIL WHERE DOC_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()) + "' AND DOC_NO='" + getAttribute("DOC_NO").getString() + "'");

            //Now insert records into Packing detail tables
            for (int i = 1; i <= hmFeltPackingDetails.size(); i++) {
                clsFeltSchemeDetails ObjFeltPackingDetails = (clsFeltSchemeDetails) hmFeltPackingDetails.get(Integer.toString(i));

                //Insert records into Packing detail table
                resultSetDetail.moveToInsertRow();
                resultSetDetail.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetDetail.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                resultSetDetail.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
                resultSetDetail.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
                resultSetDetail.updateString("ORDER_FROM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ORDER_FROM_DATE").getString()));
                resultSetDetail.updateString("ORDER_TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ORDER_TO_DATE").getString()));
                resultSetDetail.updateInt("FIN_YEAR", getAttribute("FIN_YEAR").getInt());
                resultSetDetail.updateString("MC_POSITION", ObjFeltPackingDetails.getAttribute("MC_POSITION").getString());
                resultSetDetail.updateString("MC_POSITION_DESC", ObjFeltPackingDetails.getAttribute("MC_POSITION_DESC").getString());
                resultSetDetail.updateString("MC_NO", ObjFeltPackingDetails.getAttribute("MC_NO").getString());
                resultSetDetail.updateString("ORDER_NO", ObjFeltPackingDetails.getAttribute("ORDER_NO").getString());
                resultSetDetail.updateString("ORDER_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltPackingDetails.getAttribute("ORDER_DATE").getString()));
                resultSetDetail.updateString("REQ_MONTH", ObjFeltPackingDetails.getAttribute("REQ_MONTH").getString());
                resultSetDetail.updateString("PIECE_NO", ObjFeltPackingDetails.getAttribute("PIECE_NO").getString());
                resultSetDetail.updateString("PARTY_CODE1", ObjFeltPackingDetails.getAttribute("PARTY_CODE1").getString());
                resultSetDetail.updateString("PARTY_NAME1", ObjFeltPackingDetails.getAttribute("PARTY_NAME1").getString());
                resultSetDetail.updateString("EMAIL", ObjFeltPackingDetails.getAttribute("EMAIL").getString());
                resultSetDetail.updateString("YEAR", ObjFeltPackingDetails.getAttribute("YEAR").getString());
                resultSetDetail.updateString("PRODUCT_CODE", ObjFeltPackingDetails.getAttribute("PRODUCT_CODE").getString());
                resultSetDetail.updateString("GROUP_NAME", ObjFeltPackingDetails.getAttribute("GROUP_NAME").getString());
                resultSetDetail.updateString("PRODUCT_GROUP_DESC", ObjFeltPackingDetails.getAttribute("PRODUCT_GROUP_DESC").getString());
                resultSetDetail.updateFloat("BASIC_VALUE", (float) ObjFeltPackingDetails.getAttribute("BASIC_VALUE").getVal());
                resultSetDetail.updateFloat("DISCOUNT",(float) ObjFeltPackingDetails.getAttribute("DISCOUNT").getVal());
                resultSetDetail.updateFloat("ORDER_AMT",(float) ObjFeltPackingDetails.getAttribute("ORDER_AMT").getVal());
                resultSetDetail.updateFloat("LENGTH",(float) ObjFeltPackingDetails.getAttribute("LENGTH").getVal());
                resultSetDetail.updateFloat("WIDTH",(float) ObjFeltPackingDetails.getAttribute("WIDTH").getVal());
                resultSetDetail.updateFloat("GSM",(float) ObjFeltPackingDetails.getAttribute("GSM").getVal());
                resultSetDetail.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
                resultSetDetail.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetail.updateInt("MODIFIED_BY", getAttribute("MODIFIED_BY").getInt());
                resultSetDetail.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetail.updateInt("CANCELED", 0);
                resultSetDetail.updateInt("CHANGED", 1);
                resultSetDetail.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetail.insertRow();

                //Insert records into Packing detail history table
                resultSetDetailHistory.moveToInsertRow();
                resultSetDetailHistory.updateInt("REVISION_NO", revisionNo);
                resultSetDetailHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetDetailHistory.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                resultSetDetailHistory.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
                resultSetDetailHistory.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
                resultSetDetailHistory.updateString("ORDER_FROM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ORDER_FROM_DATE").getString()));
                resultSetDetailHistory.updateString("ORDER_TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ORDER_TO_DATE").getString()));
                resultSetDetailHistory.updateInt("FIN_YEAR", getAttribute("FIN_YEAR").getInt());
                resultSetDetailHistory.updateString("MC_POSITION", ObjFeltPackingDetails.getAttribute("MC_POSITION").getString());
                resultSetDetailHistory.updateString("MC_POSITION_DESC", ObjFeltPackingDetails.getAttribute("MC_POSITION_DESC").getString());
                resultSetDetailHistory.updateString("MC_NO", ObjFeltPackingDetails.getAttribute("MC_NO").getString());
                resultSetDetailHistory.updateString("ORDER_NO", ObjFeltPackingDetails.getAttribute("ORDER_NO").getString());
                resultSetDetailHistory.updateString("ORDER_DATE",EITLERPGLOBAL.formatDateDB(ObjFeltPackingDetails.getAttribute("ORDER_DATE").getString()));
                resultSetDetailHistory.updateString("REQ_MONTH", ObjFeltPackingDetails.getAttribute("REQ_MONTH").getString());
                resultSetDetailHistory.updateString("PIECE_NO", ObjFeltPackingDetails.getAttribute("PIECE_NO").getString());
                resultSetDetailHistory.updateString("PARTY_CODE1", ObjFeltPackingDetails.getAttribute("PARTY_CODE1").getString());
                resultSetDetailHistory.updateString("PARTY_NAME1", ObjFeltPackingDetails.getAttribute("PARTY_NAME1").getString());
                resultSetDetailHistory.updateString("EMAIL", ObjFeltPackingDetails.getAttribute("EMAIL").getString());
                resultSetDetailHistory.updateString("YEAR", ObjFeltPackingDetails.getAttribute("YEAR").getString());
                resultSetDetailHistory.updateString("PRODUCT_CODE", ObjFeltPackingDetails.getAttribute("PRODUCT_CODE").getString());
                resultSetDetailHistory.updateString("GROUP_NAME", ObjFeltPackingDetails.getAttribute("GROUP_NAME").getString());
                resultSetDetailHistory.updateString("PRODUCT_GROUP_DESC", ObjFeltPackingDetails.getAttribute("PRODUCT_GROUP_DESC").getString());
                resultSetDetailHistory.updateFloat("BASIC_VALUE", (float) ObjFeltPackingDetails.getAttribute("BASIC_VALUE").getVal());
                resultSetDetailHistory.updateFloat("DISCOUNT",(float) ObjFeltPackingDetails.getAttribute("DISCOUNT").getVal());
                resultSetDetailHistory.updateFloat("ORDER_AMT",(float) ObjFeltPackingDetails.getAttribute("ORDER_AMT").getVal());
                resultSetDetailHistory.updateFloat("LENGTH",(float) ObjFeltPackingDetails.getAttribute("LENGTH").getVal());
                resultSetDetailHistory.updateFloat("WIDTH",(float) ObjFeltPackingDetails.getAttribute("WIDTH").getVal());
                resultSetDetailHistory.updateFloat("GSM",(float) ObjFeltPackingDetails.getAttribute("GSM").getVal());
                resultSetDetailHistory.updateInt("UPDATED_BY", getAttribute("UPDATED_BY").getInt());
                resultSetDetailHistory.updateInt("CHANGED", 1);
                resultSetDetailHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetailHistory.insertRow();
            }

            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = 771; //Felt Packing
            ObjFeltProductionApprovalFlow.DocNo = getDocumentNo(getAttribute("DOC_NO").getString());
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.FELT_SAL_SCHEME_HEADER";
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
                data.Execute("UPDATE PRODUCTION.FELT_SAL_SCHEME_HEADER SET REJECTED=0,CHANGED=1 WHERE DOC_NO='" + getAttribute("DOC_NO").getString() + "' AND DOC_DATE='" + getAttribute("DOC_DATE").getString() + "' AND DOC_NO='" + getAttribute("DOC_NO").getString() + "'");

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
            
            if (ObjFeltProductionApprovalFlow.Status.equals("F")) {
                if (getAttribute("DOC_NO").getString().startsWith("A")) {
                    for (int i = 1; i <= hmFeltPackingDetails.size(); i++) {
                        clsFeltSchemeDetails ObjFeltPackingDetails = (clsFeltSchemeDetails) hmFeltPackingDetails.get(Integer.toString(i));
                        data.Execute("INSERT INTO PRODUCTION.ANNUAL_ORDER_INCENTIVE (PIECE_NO,ORDER_NO,ORDER_DATE,REQ_MONTH,PI_DATE,PI_AMT,WH_RCVD_DATE,WH_WEIGHT,LENGTH,WIDTH,GSM,RC_VOUCHER_DATE,RC_VOUCHER_AMT,RC_DETAIL,INVOICE_NO,INVOICE_DATE,INVOICE_AMT,PI_BASED_CN_NO,PI_BASED_CN_AMT,PI_BASED_CN_DATE,DUE_DATE1,DUE_DATE2,YEAR_END_DISC_PER,YEAR_END_DISC_AMT,BILL_DISC,PI_NO,PARTY_CHARGE_CODE,INVOICE_CHARGE_CODE,MC_POSITION_NO,MC_POSITION_DESC,PARTY_CODE,EMAIL,MC_NO,PRODUCT_CODE,PRODUCT_GROUP_DESC,BASIC_VALUE,DISCOUNT,ORDER_AMT,GROUP_NAME,ORDER_FROM_DATE,ORDER_TO_DATE,FIN_YEAR) (SELECT PIECE_NO,ORDER_NO,ORDER_DATE,REQ_MONTH,'0000-00-00','0.00','0000-00-00','0.00',LENGTH,WIDTH,GSM,'0000-00-00','0.00','','','0000-00-00','','','0.00','0000-00-00','0000-00-00','0000-00-00','0.00','0.00','0.00','','','',MC_POSITION,MC_POSITION_DESC,PARTY_CODE1,EMAIL,MC_NO,PRODUCT_CODE,PRODUCT_GROUP_DESC,BASIC_VALUE,DISCOUNT,ORDER_AMT,GROUP_NAME,ORDER_FROM_DATE,ORDER_TO_DATE,YEAR FROM PRODUCTION.FELT_SAL_SCHEME_DETAIL WHERE PIECE_NO='" + ObjFeltPackingDetails.getAttribute("PIECE_NO").getString() + "' AND DOC_NO='"+getAttribute("DOC_NO").getString()+"')");
                        //WHERE PIECE_NO='" + ObjFeltPackingDetails.getAttribute("PIECE_NO").getString() + "')");
                    }
                }
                
                if (getAttribute("DOC_NO").getString().startsWith("R")) {
                    for (int i = 1; i <= hmFeltPackingDetails.size(); i++) {
                        clsFeltSchemeDetails ObjFeltPackingDetails = (clsFeltSchemeDetails) hmFeltPackingDetails.get(Integer.toString(i));
                        data.Execute("DELETE FROM PRODUCTION.ANNUAL_ORDER_INCENTIVE WHERE PIECE_NO='" + ObjFeltPackingDetails.getAttribute("PIECE_NO").getString() + "'");
                    }
                }
                
                if (getAttribute("DOC_NO").getString().startsWith("E")) {
                    for (int i = 1; i <= hmFeltPackingDetails.size(); i++) {
                        clsFeltSchemeDetails ObjFeltPackingDetails = (clsFeltSchemeDetails) hmFeltPackingDetails.get(Integer.toString(i));
                        data.Execute("UPDATE PRODUCTION.FELT_SAL_SCHEME_DETAIL D,PRODUCTION.ANNUAL_ORDER_INCENTIVE I SET I.REQ_MONTH=D.REQ_MONTH,I.EMAIL=D.EMAIL WHERE D.PIECE_NO='" + ObjFeltPackingDetails.getAttribute("PIECE_NO").getString() + "' AND D.PIECE_NO=I.PIECE_NO AND D.DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
                    }
                }
            }
                    

            //--------- Approval Flow Update complete -----------
            setData();
            resultSetDetail.close();
            statementDetail.close();
            resultSetDetailHistory.close();
            statementDetailHistory.close();
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
    public boolean CanDelete(String baleNo, String baleDate, int userID) {
        if (HistoryView) {
            return false;
        }
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";

        try {
            tmpStmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_SAL_SCHEME_HEADER WHERE DOC_NO='" + baleNo + "' AND DOC_DATE='" + EITLERPGLOBAL.formatDateDB(baleDate) + "' AND APPROVED=1";
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if (rsTmp.getInt("COUNT") > 0) {  //Packing is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=771 AND USER_ID=" + userID + " AND DOC_NO='" + getAttribute("DOC_NO").getString() + "' AND STATUS='W'";
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();

                if (rsTmp.getInt("COUNT") > 0) {
                    strSQL = "DELETE FROM PRODUCTION.FELT_SAL_SCHEME_HEADER WHERE DOC_NO='" + baleNo + "' AND DOC_DATE='" + EITLERPGLOBAL.formatDateDB(baleDate) + "'";
                    tmpStmt.executeUpdate(strSQL);
                    strSQL = "DELETE FROM PRODUCTION.FELT_SAL_SCHEME_DETAIL WHERE DOC_NO='" + baleNo + "' AND DOC_DATE='" + EITLERPGLOBAL.formatDateDB(baleDate) + "'";
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
    public boolean IsEditable(String baleNo, String baleDate, int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";
        try {
            if (HistoryView) {
                return false;
            }
            tmpStmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //First check that document is approved or not
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_SAL_SCHEME_HEADER WHERE DOC_NO='" + baleNo + "' AND DOC_DATE='" + baleDate + "' AND APPROVED=1";
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=771 AND USER_ID=" + userID + " AND DOC_NO='" + baleNo + "' AND STATUS='W'";
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

//    public boolean Filter(String stringFindQuery) {
//        Ready = false;
//        try {
//            //String strSql = "SELECT * FROM PRODUCTION.FELT_SAL_SCHEME_HEADER A,PRODUCTION.FELT_SAL_SCHEME_DETAIL B WHERE A.DOC_NO=B.DOC_NO AND " + stringFindQuery;
//            String strSql = "SELECT * FROM PRODUCTION.FELT_SAL_SCHEME_HEADER WHERE " + stringFindQuery;
//            connection = data.getConn();
//            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
//            resultSet = statement.executeQuery(strSql);
//            if (!resultSet.first()) {
//                LoadData();
//                Ready = true;
//                return false;
//            } else {
//                Ready = true;
//                MoveLast();
//                return true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            LastError = e.getMessage();
//            return false;
//        }
//    }
    
    public boolean Filter(String stringFindQuery) {
        Ready = false;
        String dpno="";
        try {
            //String strSql = "SELECT * FROM PRODUCTION.FELT_SAL_SCHEME_HEADER A,PRODUCTION.FELT_SAL_SCHEME_DETAIL B WHERE A.DOC_NO=B.DOC_NO AND " + stringFindQuery;
            if(stringFindQuery.substring(0,13).equalsIgnoreCase(" PIECE_NO")){
                System.out.println("QQQ - SELECT A.DOC_NO FROM PRODUCTION.FELT_SAL_SCHEME_HEADER A,PRODUCTION.FELT_SAL_SCHEME_DETAIL B WHERE A.DOC_NO=B.DOC_NO AND B.PIECE_NO='"+stringFindQuery.substring(16));
                dpno=data.getStringValueFromDB("SELECT A.DOC_NO FROM PRODUCTION.FELT_SAL_SCHEME_HEADER A,PRODUCTION.FELT_SAL_SCHEME_DETAIL B WHERE A.DOC_NO=B.DOC_NO AND B.PIECE_NO='"+stringFindQuery.substring(16));
                stringFindQuery=" DOC_NO="+dpno;
                System.out.println(" stringFindQuery" +stringFindQuery);
            }
            String strSql = "SELECT * FROM PRODUCTION.FELT_SAL_SCHEME_HEADER WHERE " + stringFindQuery;
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
            if (HistoryView) {
                RevNo = resultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO", resultSet.getInt("REVISION_NO"));
            } else {
                setAttribute("REVISION_NO", 0);
                setAttribute("CREATED_BY", resultSet.getInt("CREATED_BY"));
                setAttribute("CREATED_DATE", resultSet.getString("CREATED_DATE"));
                setAttribute("MODIFIED_BY", resultSet.getInt("MODIFIED_BY"));
                setAttribute("MODIFIED_DATE", resultSet.getString("MODIFIED_DATE"));
                setAttribute("APPROVED", resultSet.getInt("APPROVED"));
                setAttribute("APPROVED_DATE", resultSet.getString("APPROVED_DATE"));
                setAttribute("REJECTED", resultSet.getBoolean("REJECTED"));
                setAttribute("REJECTED_DATE", resultSet.getString("REJECTED_DATE"));
                setAttribute("CANCELED", resultSet.getInt("CANCELED"));
            }

            setAttribute("DOC_NO", resultSet.getString("DOC_NO"));
            setAttribute("DOC_DATE", resultSet.getString("DOC_DATE"));
            setAttribute("PARTY_CODE", resultSet.getString("PARTY_CODE"));
            setAttribute("PARTY_NAME", resultSet.getString("PARTY_NAME"));
            setAttribute("ORDER_FROM_DATE", resultSet.getString("ORDER_FROM_DATE"));
            setAttribute("ORDER_TO_DATE", resultSet.getString("ORDER_TO_DATE"));
            setAttribute("FIN_YEAR", resultSet.getInt("FIN_YEAR"));
            setAttribute("HIERARCHY_ID", resultSet.getInt("HIERARCHY_ID"));
            
            //Now Populate the collection, first clear the collection
            hmFeltPackingDetails.clear();

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            if (HistoryView) {
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SAL_SCHEME_DETAIL_H WHERE DOC_NO='" + resultSet.getString("DOC_NO") + "' AND DOC_DATE='" + resultSet.getString("DOC_DATE") + "' AND REVISION_NO=" + RevNo);
            } else {
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SAL_SCHEME_DETAIL WHERE DOC_NO='" + resultSet.getString("DOC_NO") + "' AND DOC_DATE='" + resultSet.getString("DOC_DATE") + "'");
            }

            while (resultSetTemp.next()) {
                serialNoCounter++;
                clsFeltSchemeDetails ObjFeltPackingDetails = new clsFeltSchemeDetails();

                ObjFeltPackingDetails.setAttribute("DOC_NO", resultSet.getString("DOC_NO"));
                ObjFeltPackingDetails.setAttribute("DOC_DATE", resultSet.getString("DOC_DATE"));
                ObjFeltPackingDetails.setAttribute("PARTY_CODE", resultSet.getString("PARTY_CODE"));
                ObjFeltPackingDetails.setAttribute("PARTY_NAME", resultSet.getString("PARTY_NAME"));
                ObjFeltPackingDetails.setAttribute("PIECE_NO", resultSetTemp.getString("PIECE_NO"));
                ObjFeltPackingDetails.setAttribute("MC_POSITION", resultSetTemp.getString("MC_POSITION"));
                ObjFeltPackingDetails.setAttribute("MC_POSITION_DESC", resultSetTemp.getString("MC_POSITION_DESC"));
                ObjFeltPackingDetails.setAttribute("MC_NO", resultSetTemp.getString("MC_NO"));
                ObjFeltPackingDetails.setAttribute("ORDER_NO", resultSetTemp.getString("ORDER_NO"));
                ObjFeltPackingDetails.setAttribute("ORDER_DATE", resultSetTemp.getString("ORDER_DATE"));
                ObjFeltPackingDetails.setAttribute("REQ_MONTH", resultSetTemp.getString("REQ_MONTH"));
                ObjFeltPackingDetails.setAttribute("PARTY_CODE1", resultSetTemp.getString("PARTY_CODE1"));
                ObjFeltPackingDetails.setAttribute("PARTY_NAME1", resultSetTemp.getString("PARTY_NAME1"));
                ObjFeltPackingDetails.setAttribute("EMAIL", resultSetTemp.getString("EMAIL"));
                ObjFeltPackingDetails.setAttribute("YEAR", resultSetTemp.getString("YEAR"));
                ObjFeltPackingDetails.setAttribute("PRODUCT_CODE", resultSetTemp.getString("PRODUCT_CODE"));
                ObjFeltPackingDetails.setAttribute("GROUP_NAME", resultSetTemp.getString("GROUP_NAME"));
                ObjFeltPackingDetails.setAttribute("PRODUCT_GROUP_DESC", resultSetTemp.getString("PRODUCT_GROUP_DESC"));
                ObjFeltPackingDetails.setAttribute("BASIC_VALUE", resultSetTemp.getFloat("BASIC_VALUE"));
                ObjFeltPackingDetails.setAttribute("DISCOUNT", resultSetTemp.getFloat("DISCOUNT"));
                ObjFeltPackingDetails.setAttribute("ORDER_AMT", resultSetTemp.getFloat("ORDER_AMT"));
                ObjFeltPackingDetails.setAttribute("LENGTH", resultSetTemp.getFloat("LENGTH"));
                ObjFeltPackingDetails.setAttribute("WIDTH", resultSetTemp.getFloat("WIDTH"));
                ObjFeltPackingDetails.setAttribute("GSM", resultSetTemp.getFloat("GSM"));
                
                hmFeltPackingDetails.put(Integer.toString(serialNoCounter), ObjFeltPackingDetails);
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

    public boolean ShowHistory(String baleDate, String baleNo) {
        Ready = false;
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_SAL_SCHEME_HEADER_H WHERE DOC_DATE='" + baleDate + "' AND DOC_NO='" + baleNo + "'");
            Ready = true;
            HistoryView = true;
            MoveFirst();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public static HashMap getHistoryList(String dpDate, String dpNo) {
        HashMap hmHistoryList = new HashMap();
        Statement stTmp;
        ResultSet rsTmp;
        try {
            stTmp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp = stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_SAL_SCHEME_HEADER_H WHERE DOC_DATE='" + dpDate + "' AND DOC_NO='" + dpNo + "'");

            while (rsTmp.next()) {
                clsFeltScheme ObjFeltPacking = new clsFeltScheme();

                ObjFeltPacking.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                ObjFeltPacking.setAttribute("UPDATED_BY", rsTmp.getInt("UPDATED_BY"));
                ObjFeltPacking.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                ObjFeltPacking.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                ObjFeltPacking.setAttribute("APPROVER_REMARKS", rsTmp.getString("APPROVER_REMARKS"));
                ObjFeltPacking.setAttribute("FROM_IP", rsTmp.getString("FROM_IP"));
                
                hmHistoryList.put(Integer.toString(hmHistoryList.size() + 1), ObjFeltPacking);
            }
            rsTmp.close();
            stTmp.close();
            return hmHistoryList;
        } catch (Exception e) {
            e.printStackTrace();
            return hmHistoryList;
        }
    }

    // Find Pending DOCUMENTS
    public static HashMap getPendingApprovals(int pUserID, int pOrder) {
        String strSQL = "";

        HashMap List = new HashMap();
        long Counter = 0;

        try {

            if (pOrder == EITLERPGLOBAL.OnRecivedDate) {
                strSQL = "SELECT DOC_DATE,DOC_NO,RECEIVED_DATE FROM PRODUCTION.FELT_SAL_SCHEME_HEADER H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=771 AND CANCELED=0 ORDER BY RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DOC_DATE,DOC_NO,RECEIVED_DATE FROM PRODUCTION.FELT_SAL_SCHEME_HEADER H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=771 AND CANCELED=0 ORDER BY DOC_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DOC_DATE,DOC_NO,RECEIVED_DATE FROM PRODUCTION.FELT_SAL_SCHEME_HEADER H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=771 AND CANCELED=0 ORDER BY DOC_NO";
            }

            ResultSet rsTmp = data.getConn().createStatement().executeQuery(strSQL);

            Counter = 0;
            while (rsTmp.next()) {

                Counter = Counter + 1;
                clsFeltScheme ObjDoc = new clsFeltScheme();

                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO", rsTmp.getString("DOC_NO"));
                ObjDoc.setAttribute("DOC_DATE", rsTmp.getString("DOC_DATE"));
                ObjDoc.setAttribute("RECEIVED_DATE", rsTmp.getString("RECEIVED_DATE"));
                // ----------------- End of Header Fields ------------------------------------//

                List.put(Long.toString(Counter), ObjDoc);
            }
            rsTmp.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return List;
    }

    public String getDocumentNo(String dpNo) {
        return dpNo;
    }

    public String[] getPieceDetails(String pieceNo) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        String[] pieceDetails = new String[6];

        try {
            tmpConn = data.getConn();
            Stmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = Stmt.executeQuery("SELECT RCVD_MTR, RECD_WDTH, RECD_KG,STYLE,GSQ,SYN_PER FROM (SELECT PIECE_NO,RCVD_MTR, RECD_WDTH, RECD_KG, REPLACE(STYLE,' ','') STYLE,PRODUCT_CD FROM PRODUCTION.FELT_PIECE_REGISTER WHERE PIECE_NO+0='" + pieceNo + "' AND REMARK='IN STOCK') P LEFT JOIN (SELECT PIECE_NO,GSQ FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='" + pieceNo + "') O ON P.PIECE_NO+0=O.PIECE_NO+0 LEFT JOIN (SELECT SYN_PER,ITEM_CODE FROM PRODUCTION.FELT_RATE_MASTER) R ON P.PRODUCT_CD=R.ITEM_CODE");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                pieceDetails[0] = rsTmp.getString("RCVD_MTR");
                pieceDetails[1] = rsTmp.getString("RECD_WDTH");
                pieceDetails[2] = rsTmp.getString("RECD_KG");
                pieceDetails[3] = rsTmp.getString("GSQ");
                pieceDetails[4] = rsTmp.getString("SYN_PER");
                pieceDetails[5] = rsTmp.getString("STYLE");
            }
            Stmt.close();
            rsTmp.close();
            return pieceDetails;
        } catch (Exception e) {
            e.printStackTrace();
            return pieceDetails;
        }
    }

    // checks piece no already exist in db in add mode
    public boolean checkPieceNoInDB(String pieceNo) {
        int count = data.getIntValueFromDB("SELECT COUNT(PKG_PIECE_NO) FROM PRODUCTION.FELT_SAL_SCHEME_DETAIL WHERE PKG_PIECE_NO='" + pieceNo + "' AND PKG_BALE_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND PKG_BALE_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'");
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    // checks piece no already exist in db in edit mode
    public boolean checkPieceNoInDB(String pieceNo, String baleNo, String baleDate) {
        int count = data.getIntValueFromDB("SELECT COUNT(PKG_PIECE_NO) FROM PRODUCTION.FELT_SAL_SCHEME_DETAIL WHERE PKG_PIECE_NO='" + pieceNo + "' AND PKG_BALE_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND PKG_BALE_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'");
        if (count >= 1) {
            int counter = 0;
            try {
                Connection Conn = data.getConn();
                Statement stTmp = Conn.createStatement();
                ResultSet rsTmp = stTmp.executeQuery("SELECT PKG_BALE_DATE,DOC_NO FROM PRODUCTION.FELT_SAL_SCHEME_DETAIL WHERE PKG_PIECE_NO='" + pieceNo + "' AND PKG_BALE_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND PKG_BALE_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'");
                while (rsTmp.next()) {
                    if (rsTmp.getString("PKG_BALE_DATE").equals(EITLERPGLOBAL.formatDateDB(baleDate)) && rsTmp.getString("DOC_NO").equals(baleNo)) {
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

    // generates report data
    public static String getParyName(int pCompanyID, String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            rsTmp = stTmp.executeQuery("SELECT PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE=" + pPartyID);
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
    
    public static String getChargeCode(int pCompanyID, String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String ChargeCode = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            rsTmp = stTmp.executeQuery("SELECT CHARGE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE=" + pPartyID);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                ChargeCode = rsTmp.getString("CHARGE_CODE");
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

        } catch (Exception e) {

        }
        return ChargeCode;
    }
    
//    public static String getPositionDesc(int pCompanyID, String pPartyID) {
//        Connection tmpConn;
//        Statement stTmp;
//        ResultSet rsTmp;
//        String ChargeCode = "";
//
//        try {
//            tmpConn = data.getConn();
//            stTmp = tmpConn.createStatement();
//            rsTmp = stTmp.executeQuery("SELECT CHARGE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE=" + pPartyID);
//            rsTmp.first();
//
//            if (rsTmp.getRow() > 0) {
//                ChargeCode = rsTmp.getString("CHARGE_CODE");
//            }
//
//            //tmpConn.close();
//            stTmp.close();
//            rsTmp.close();
//
//        } catch (Exception e) {
//
//        }
//        return ChargeCode;
//    }
    public static void CancelDoc(String pAmendNo) {
        ResultSet rsTmp=null;
        
        if(CanCancel(pAmendNo)) {
            
            boolean Approved=false;
            
            try {
                rsTmp=data.getResult("SELECT APPROVED FROM PRODUCTION.FELT_SAL_SCHEME_HEADER WHERE DOC_NO="+pAmendNo+" AND APPROVED=0");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    Approved=rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();
                
                if(!Approved) {
                    //String purpose
                    String purpose = data.getStringValueFromDB("SELECT PURPOSE FROM DINESHMILLS.D_COM_DOC_CANCEL_REQUEST WHERE MODULE_ID=771 AND DOC_NO='"+pAmendNo+"'");
                    //data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='"+pAmendNo+"' AND MODULE_ID=771");
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='"+pAmendNo+"' AND MODULE_ID=771");
                    data.Execute("UPDATE PRODUCTION.FELT_SAL_SCHEME_HEADER SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE(),PKG_CANCEL_REASON='"+purpose+"' WHERE DOC_NO='"+pAmendNo+"'");
                    data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_DOC_NO='',PR_DOC_DATE='0000-00-00' WHERE PR_DOC_NO='"+pAmendNo+"'");
                }
//                data.Execute("UPDATE PRODUCTION.FELT_SAL_SCHEME_HEADER SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE(),PKG_CANCEL_REASON='"+purpose+"' WHERE DOC_NO='"+pAmendNo+"'");
//                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_DOC_NO='',PR_DOC_DATE='0000-00-00' WHERE PR_DOC_NO='"+pAmendNo+"'");
             }
            catch(Exception e) {
                
            }
        }
        
    }
    public static boolean CanCancel(String pDocNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean CanCancel=false;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT DOC_NO FROM PRODUCTION.FELT_SAL_SCHEME_HEADER WHERE DOC_NO="+pDocNo+"  AND APPROVED=0 AND CANCELED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                CanCancel=true;
            }
            
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        
        return CanCancel;
    }
    
    public static String getOrderDate(String ppiecedec) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String OrderDate="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT A.S_ORDER_DATE FROM PRODUCTION.FELT_SALES_ORDER_HEADER A,PRODUCTION.FELT_SALES_ORDER_DETAIL B WHERE B.PIECE_NO='"+ppiecedec+"'  AND A.S_ORDER_NO=B.S_ORDER_NO AND A.APPROVED=1 AND A.CANCELED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                OrderDate=rsTmp.getString("S_ORDER_DATE");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return OrderDate;
        }
        catch(Exception e) {
            return "";
        }
    }
    
     public static String getOrderDate1(String ppiecedec) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String OrderDate="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT ORDER_DATE FROM PRODUCTION.ANNUAL_ORDER_INCENTIVE WHERE PIECE_NO='"+ppiecedec+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                OrderDate=rsTmp.getString("ORDER_DATE");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return OrderDate;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getOrderNo(String ppiecedec) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String OrderNo="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT ORDER_NO FROM PRODUCTION.ANNUAL_ORDER_INCENTIVE WHERE PIECE_NO='"+ppiecedec+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                OrderNo=rsTmp.getString("ORDER_NO");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return OrderNo;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getPieceNo(String ppiecedec) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String pieceno="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT S_ORDER_NO FROM PRODUCTION.FELT_SALES_ORDER_DETAIL WHERE PIECE_NO='"+ppiecedec+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                pieceno=rsTmp.getString("S_ORDER_NO");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return pieceno;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getMachienPosition(String ppiecedec) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String MachinePosition="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT POSITION FROM PRODUCTION.FELT_SALES_ORDER_DETAIL WHERE PIECE_NO='"+ppiecedec+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                MachinePosition=rsTmp.getString("POSITION");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return MachinePosition;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getMachienPosition1(String ppiecedec) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String MachinePosition="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT MC_POSITION_NO FROM PRODUCTION.ANNUAL_ORDER_INCENTIVE WHERE PIECE_NO='"+ppiecedec+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                MachinePosition=rsTmp.getString("MC_POSITION_NO");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return MachinePosition;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getMachienNo(String ppiecedec) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String MachineNo="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT MACHINE_NO FROM PRODUCTION.FELT_SALES_ORDER_DETAIL WHERE PIECE_NO='"+ppiecedec+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                MachineNo=rsTmp.getString("MACHINE_NO");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return MachineNo;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getMachienNo1(String ppiecedec) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String MachineNo="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT MC_NO FROM PRODUCTION.ANNUAL_ORDER_INCENTIVE WHERE PIECE_NO='"+ppiecedec+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                MachineNo=rsTmp.getString("MC_NO");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return MachineNo;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getMachienPositionDesc(String ppiecedec) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String MachineDesc="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT POSITION_DESC FROM PRODUCTION.FELT_SALES_ORDER_DETAIL WHERE PIECE_NO='"+ppiecedec+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                MachineDesc=rsTmp.getString("POSITION_DESC");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return MachineDesc;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getMachienPositionDesc1(String ppiecedec) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String MachineDesc="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT MC_POSITION_DESC FROM PRODUCTION.ANNUAL_ORDER_INCENTIVE WHERE PIECE_NO='"+ppiecedec+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                MachineDesc=rsTmp.getString("MC_POSITION_DESC");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return MachineDesc;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getReqMonth(String ppiecedec) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String ReqMonth="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT REQ_MONTH FROM PRODUCTION.FELT_SALES_ORDER_DETAIL WHERE PIECE_NO='"+ppiecedec+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                ReqMonth=rsTmp.getString("REQ_MONTH");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return ReqMonth;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getReqMonth1(String ppiecedec) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String ReqMonth="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT REQ_MONTH FROM PRODUCTION.ANNUAL_ORDER_INCENTIVE WHERE PIECE_NO='"+ppiecedec+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                ReqMonth=rsTmp.getString("REQ_MONTH");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return ReqMonth;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getEmail(String ppiecedec) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Email="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT C.EMAIL FROM PRODUCTION.FELT_SALES_ORDER_DETAIL A,PRODUCTION.FELT_SALES_ORDER_HEADER B,DINESHMILLS.D_SAL_PARTY_MASTER C WHERE B.PARTY_CODE=C.PARTY_CODE AND A.S_ORDER_NO=B.S_ORDER_NO AND PIECE_NO='"+ppiecedec+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Email=rsTmp.getString("C.EMAIL");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return Email;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getEmail1(String ppiecedec) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Email="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT EMAIL FROM PRODUCTION.ANNUAL_ORDER_INCENTIVE WHERE PIECE_NO='"+ppiecedec+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Email=rsTmp.getString("EMAIL");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return Email;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getPartyCode(String ppiecedec) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyCode="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PARTY_CODE FROM PRODUCTION.FELT_SALES_ORDER_DETAIL A,PRODUCTION.FELT_SALES_ORDER_HEADER B WHERE A.S_ORDER_NO=B.S_ORDER_NO AND PIECE_NO='"+ppiecedec+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                PartyCode=rsTmp.getString("PARTY_CODE");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return PartyCode;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getPartyCode1(String ppiecedec) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyCode="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PARTY_CODE FROM PRODUCTION.ANNUAL_ORDER_INCENTIVE WHERE PIECE_NO='"+ppiecedec+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                PartyCode=rsTmp.getString("PARTY_CODE");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return PartyCode;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getPartyName(String ppiecedec) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PARTY_NAME FROM PRODUCTION.FELT_SALES_ORDER_DETAIL A,PRODUCTION.FELT_SALES_ORDER_HEADER B WHERE A.S_ORDER_NO=B.S_ORDER_NO AND PIECE_NO='"+ppiecedec+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                PartyName=rsTmp.getString("PARTY_NAME");
            }
            
            stTmp.close();
            rsTmp.close();
            
            return PartyName;
        }
        catch(Exception e) {
            return "";
        }
    }
}
