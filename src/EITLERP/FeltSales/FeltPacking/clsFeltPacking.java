/*
 * clsFeltPacking.java
 *
 * Created on July 19, 2013, 5:31 PM
 */
package EITLERP.FeltSales.FeltPacking;

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
public class clsFeltPacking {

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
    public clsFeltPacking() {
        LastError = "";
        props = new HashMap();
        props.put("PKG_DP_NO", new Variant(""));
        props.put("PKG_DP_DATE", new Variant(""));
        props.put("PKG_BALE_NO", new Variant(""));
        props.put("PKG_BALE_DATE", new Variant(""));
        props.put("PKG_PARTY_CODE", new Variant(""));
        props.put("PKG_PARTY_NAME", new Variant(""));
        props.put("PKG_STATION", new Variant(""));
        props.put("PKG_TRANSPORT_MODE", new Variant(""));
        props.put("PKG_MODE_PKG_SLIP", new Variant(""));
        props.put("PKG_BOX_SIZE", new Variant(""));
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
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_PKG_SLIP_HEADER  ORDER BY PKG_DP_NO");
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
            resultSetDetail = statementDetail.executeQuery("SELECT * FROM  PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_DP_NO=''");

            // Packing detail history connection
            statementDetailHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetDetailHistory = statementDetailHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_PKG_SLIP_DETAIL_H WHERE PKG_DP_NO=''");

            // Packing data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_PKG_SLIP_HEADER_H WHERE PKG_DP_NO=''");

            //--------- Generate Bale no. ---------------------
            setAttribute("PKG_DP_NO", EITLERP.clsFirstFree.getNextFreeNo(2, 715, getAttribute("FFNO").getInt(), true));
            //-------------------------------------------------

            resultSet.moveToInsertRow();
            resultSet.updateString("PKG_DP_NO", getAttribute("PKG_DP_NO").getString());
            resultSet.updateString("PKG_DP_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PKG_DP_DATE").getString()));
            resultSet.updateString("PKG_BALE_NO", getAttribute("PKG_BALE_NO").getString());
            String mbldt = "";

            mbldt = EITLERPGLOBAL.formatDateDB(getAttribute("PKG_BALE_DATE").getString());
            if (mbldt.equals("")) {
                mbldt = null;
            }

            resultSet.updateString("PKG_BALE_DATE", mbldt);
            resultSet.updateString("PKG_PARTY_CODE", getAttribute("PKG_PARTY_CODE").getString());
            resultSet.updateString("PKG_PARTY_NAME", getAttribute("PKG_PARTY_NAME").getString());
            resultSet.updateString("PKG_STATION", getAttribute("PKG_STATION").getString());
            resultSet.updateString("PKG_TRANSPORT_MODE", getAttribute("PKG_TRANSPORT_MODE").getString());
            resultSet.updateString("PKG_BOX_SIZE", getAttribute("PKG_BOX_SIZE").getString());
            resultSet.updateString("PKG_MODE_PACKING", getAttribute("PKG_MODE_PACKING").getString());
            resultSet.updateString("PKG_CHARGE_CODE", getAttribute("PKG_CHARGE_CODE").getString());
            resultSet.updateString("PKG_WH_CD", getAttribute("PKG_WH_CD").getString());
            resultSet.updateInt("INVOICE_FLG",0);
            resultSet.updateInt("BALE_REOPEN_FLG",0);
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
            resultSetHistory.updateString("PKG_DP_NO", getAttribute("PKG_DP_NO").getString());
            resultSetHistory.updateString("PKG_DP_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PKG_DP_DATE").getString()));
            resultSetHistory.updateString("PKG_BALE_NO", getAttribute("PKG_BALE_NO").getString());
            mbldt = "";

            mbldt = EITLERPGLOBAL.formatDateDB(getAttribute("PKG_BALE_DATE").getString());
            if (mbldt.equals("")) {
                mbldt = null;
            }
            resultSetHistory.updateString("PKG_BALE_DATE", mbldt);

            resultSetHistory.updateString("PKG_PARTY_CODE", getAttribute("PKG_PARTY_CODE").getString());
            resultSetHistory.updateString("PKG_PARTY_NAME", getAttribute("PKG_PARTY_NAME").getString());
            resultSetHistory.updateString("PKG_STATION", getAttribute("PKG_STATION").getString());
            resultSetHistory.updateString("PKG_TRANSPORT_MODE", getAttribute("PKG_TRANSPORT_MODE").getString());
            resultSetHistory.updateString("PKG_BOX_SIZE", getAttribute("PKG_BOX_SIZE").getString());
            resultSetHistory.updateString("PKG_MODE_PACKING", getAttribute("PKG_MODE_PACKING").getString());
            resultSetHistory.updateString("PKG_CHARGE_CODE", getAttribute("PKG_CHARGE_CODE").getString());
            resultSetHistory.updateString("PKG_WH_CD", getAttribute("PKG_WH_CD").getString());
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
                clsFeltPackingDetails ObjFeltPackingDetails = (clsFeltPackingDetails) hmFeltPackingDetails.get(Integer.toString(i));

                //Insert records into Packing detail table
                resultSetDetail.moveToInsertRow();
                resultSetDetail.updateString("PKG_DP_NO", getAttribute("PKG_DP_NO").getString());
                resultSetDetail.updateString("PKG_DP_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PKG_DP_DATE").getString()));
                resultSetDetail.updateString("PKG_BALE_NO", getAttribute("PKG_BALE_NO").getString());
                mbldt = "";

                mbldt = EITLERPGLOBAL.formatDateDB(getAttribute("PKG_BALE_DATE").getString());
                if (mbldt.equals("")) {
                    mbldt = null;
                }
                resultSetDetail.updateString("PKG_BALE_DATE", mbldt);
                resultSetDetail.updateString("PKG_PIECE_NO", ObjFeltPackingDetails.getAttribute("PKG_PIECE_NO").getString());
                resultSetDetail.updateFloat("PKG_LENGTH", (float) ObjFeltPackingDetails.getAttribute("PKG_LENGTH").getVal());
                resultSetDetail.updateFloat("PKG_WIDTH", (float) ObjFeltPackingDetails.getAttribute("PKG_WIDTH").getVal());
                resultSetDetail.updateFloat("PKG_GSM", (float) ObjFeltPackingDetails.getAttribute("PKG_GSM").getVal());
                resultSetDetail.updateFloat("PKG_WEIGHT", (float) ObjFeltPackingDetails.getAttribute("PKG_WEIGHT").getVal());
                resultSetDetail.updateFloat("PKG_SQM", (float) ObjFeltPackingDetails.getAttribute("PKG_SQM").getVal());
                resultSetDetail.updateFloat("PKG_SYN_PER", (float) ObjFeltPackingDetails.getAttribute("PKG_SYN_PER").getVal());
                resultSetDetail.updateString("PKG_STYLE", ObjFeltPackingDetails.getAttribute("PKG_STYLE").getString());
                resultSetDetail.updateString("PKG_MCN_POSITION_DESC", ObjFeltPackingDetails.getAttribute("PKG_MCN_POSITION_DESC").getString());
                resultSetDetail.updateString("PKG_MACHINE_NO", ObjFeltPackingDetails.getAttribute("PKG_MACHINE_NO").getString());
                resultSetDetail.updateString("PKG_ORDER_NO", ObjFeltPackingDetails.getAttribute("PKG_ORDER_NO").getString());
                if(ObjFeltPackingDetails.getAttribute("PKG_ORDER_DATE").getString().equals("")) {
                    resultSetDetail.updateString("PKG_ORDER_DATE", "0000-00-00");
                } else {
                    resultSetDetail.updateString("PKG_ORDER_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltPackingDetails.getAttribute("PKG_ORDER_DATE").getString()));
                }
//                resultSetDetail.updateString("PKG_ORDER_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltPackingDetails.getAttribute("PKG_ORDER_DATE").getString()));
                resultSetDetail.updateString("PKG_PRODUCT_CODE", ObjFeltPackingDetails.getAttribute("PKG_PRODUCT_CODE").getString());
                resultSetDetail.updateString("PKG_PRODUCT_DESC", ObjFeltPackingDetails.getAttribute("PKG_PRODUCT_DESC").getString());
                resultSetDetail.updateString("PKG_PO_NO", ObjFeltPackingDetails.getAttribute("PKG_PO_NO").getString());
                if(ObjFeltPackingDetails.getAttribute("PKG_PO_DATE").getString().equals("")) {
                    resultSetDetail.updateString("PKG_PO_DATE", "0000-00-00");
                } else {
                    resultSetDetail.updateString("PKG_PO_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltPackingDetails.getAttribute("PKG_PO_DATE").getString()));
                }
                //resultSetDetail.updateString("PKG_PO_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltPackingDetails.getAttribute("PKG_PO_DATE").getString()));
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
                resultSetDetailHistory.updateString("PKG_DP_NO", getAttribute("PKG_DP_NO").getString());
                resultSetDetailHistory.updateString("PKG_DP_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PKG_DP_DATE").getString()));
                resultSetDetailHistory.updateString("PKG_BALE_NO", getAttribute("PKG_BALE_NO").getString());
                mbldt = "";

                mbldt = EITLERPGLOBAL.formatDateDB(getAttribute("PKG_BALE_DATE").getString());
                if (mbldt.equals("")) {
                    mbldt = null;
                }
                resultSetDetailHistory.updateString("PKG_BALE_DATE", mbldt);
                resultSetDetailHistory.updateString("PKG_PIECE_NO", ObjFeltPackingDetails.getAttribute("PKG_PIECE_NO").getString());
                resultSetDetailHistory.updateFloat("PKG_LENGTH", (float) ObjFeltPackingDetails.getAttribute("PKG_LENGTH").getVal());
                resultSetDetailHistory.updateFloat("PKG_WIDTH", (float) ObjFeltPackingDetails.getAttribute("PKG_WIDTH").getVal());
                resultSetDetailHistory.updateFloat("PKG_WEIGHT", (float) ObjFeltPackingDetails.getAttribute("PKG_WEIGHT").getVal());
                resultSetDetailHistory.updateFloat("PKG_GSM", (float) ObjFeltPackingDetails.getAttribute("PKG_GSM").getVal());
                //resultSetDetailHistory.updateFloat("PR_ACTUAL_WEIGHT", (float) ObjFeltPackingDetails.getAttribute("PR_ACTUAL_WEIGHT").getVal());
                resultSetDetailHistory.updateFloat("PKG_SQM", (float) ObjFeltPackingDetails.getAttribute("PKG_SQM").getVal());
                resultSetDetailHistory.updateInt("PKG_SYN_PER", ObjFeltPackingDetails.getAttribute("PKG_SYN_PER").getInt());
                resultSetDetailHistory.updateString("PKG_STYLE", ObjFeltPackingDetails.getAttribute("PKG_STYLE").getString());
                resultSetDetailHistory.updateString("PKG_MCN_POSITION_DESC", ObjFeltPackingDetails.getAttribute("PKG_MCN_POSITION_DESC").getString());
                resultSetDetailHistory.updateString("PKG_MACHINE_NO", ObjFeltPackingDetails.getAttribute("PKG_MACHINE_NO").getString());
                resultSetDetailHistory.updateString("PKG_ORDER_NO", ObjFeltPackingDetails.getAttribute("PKG_ORDER_NO").getString());
                if(ObjFeltPackingDetails.getAttribute("PKG_ORDER_DATE").getString().equals("")) {
                    resultSetDetail.updateString("PKG_ORDER_DATE", "0000-00-00");
                } else {
                    resultSetDetailHistory.updateString("PKG_ORDER_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltPackingDetails.getAttribute("PKG_ORDER_DATE").getString()));
                }
                
                //resultSetDetailHistory.updateString("PKG_ORDER_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltPackingDetails.getAttribute("PKG_ORDER_DATE").getString()));
                resultSetDetailHistory.updateString("PKG_PRODUCT_CODE", ObjFeltPackingDetails.getAttribute("PKG_PRODUCT_CODE").getString());
                resultSetDetailHistory.updateString("PKG_PRODUCT_DESC", ObjFeltPackingDetails.getAttribute("PKG_PRODUCT_DESC").getString());
                resultSetDetailHistory.updateString("PKG_PO_NO", ObjFeltPackingDetails.getAttribute("PKG_PO_NO").getString());
                if(ObjFeltPackingDetails.getAttribute("PKG_PO_DATE").getString().equals("")) {
                    resultSetDetail.updateString("PKG_PO_DATE", "0000-00-00");
                } else {
                    resultSetDetailHistory.updateString("PKG_PO_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltPackingDetails.getAttribute("PKG_PO_DATE").getString()));
                }
                
                //resultSetDetailHistory.updateString("PKG_PO_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltPackingDetails.getAttribute("PKG_PO_DATE").getString()));
                resultSetDetailHistory.updateInt("UPDATED_BY", getAttribute("UPDATED_BY").getInt());
                //resultSetDetailHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("ENTRY_DATE").getString()));
                resultSetDetailHistory.updateInt("CHANGED", 1);
                resultSetDetailHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetailHistory.insertRow();
            }

            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = 715; //Felt Packing
            ObjFeltProductionApprovalFlow.DocNo = getDocumentNo(getAttribute("PKG_DP_NO").getString());
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName = "FELT_PKG_SLIP_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator = true;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "PKG_DP_NO";

            if (ObjFeltProductionApprovalFlow.Status.equals("H")) {
                ObjFeltProductionApprovalFlow.Status = "A";
                ObjFeltProductionApprovalFlow.To = ObjFeltProductionApprovalFlow.From;
                ObjFeltProductionApprovalFlow.UpdateFlow();
                data.Execute("UPDATE PRODUCTION.FELT_PKG_SLIP_DETAIL A,PRODUCTION.FELT_SALES_PIECE_REGISTER B SET B.PR_PKG_DP_NO='"+getAttribute("PKG_DP_NO").getString()+"',B.PR_PKG_DP_DATE='"+EITLERPGLOBAL.formatDateDB(getAttribute("PKG_DP_DATE").getString())+"' WHERE B.PR_PIECE_NO=A.PKG_PIECE_NO AND A.PKG_DP_NO='"+getAttribute("PKG_DP_NO").getString()+"'");
            } else {
                if (!ObjFeltProductionApprovalFlow.UpdateFlow()) {
                    LastError = ObjFeltProductionApprovalFlow.LastError;
                }
            }
          //  data.Execute("UPDATE PRODUCTION.FELT_PKG_SLIP_DETAIL A,PRODUCTION.FELT_SALES_PIECE_REGISTER B SET B.PR_PKG_DP_NO='"+getAttribute("PKG_DP_NO").getString()+"',B.PR_PKG_DP_DATE='"+EITLERPGLOBAL.formatDateDB(getAttribute("PKG_DP_DATE").getString())+"' WHERE B.PR_PIECE_NO=A.PKG_PIECE_NO AND A.PKG_DP_NO='"+getAttribute("PKG_DP_NO").getString()+"'");
            // data.Execute("UPDATE PRODUCTION.FELT_PKG_SLIP_HEADER SET BALE_REOPEN_FLG=0 WHERE PKG_BALE_NO='"+getAttribute("PKG_BALE_NO").getString()+"' ");
            //data.Execute("UPDATE PRODUCTION.FELT_PKG_SLIP_DETAIL SET BALE_REOPEN_FLG=0 WHERE PKG_BALE_NO='"+getAttribute("PKG_BALE_NO").getString()+"' ");
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
            resultSetDetail = statementDetail.executeQuery("SELECT * FROM  PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_DP_NO=''");

            // Packing detail history connection
            statementDetailHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetDetailHistory = statementDetailHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_PKG_SLIP_DETAIL_H WHERE PKG_DP_NO=''");

            // Packing data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_PKG_SLIP_HEADER_H WHERE PKG_DP_NO=''");

            resultSet.updateString("PKG_DP_NO", getAttribute("PKG_DP_NO").getString());
            resultSet.updateString("PKG_DP_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PKG_DP_DATE").getString()));
            resultSet.updateString("PKG_BALE_NO", getAttribute("PKG_BALE_NO").getString());
            String mbldt = "";

            mbldt = EITLERPGLOBAL.formatDateDB(getAttribute("PKG_BALE_DATE").getString());
            if (mbldt.equals("")) {
                mbldt = null;
            }
            resultSet.updateString("PKG_BALE_DATE", mbldt);
            resultSet.updateString("PKG_PARTY_CODE", getAttribute("PKG_PARTY_CODE").getString());
            resultSet.updateString("PKG_PARTY_NAME", getAttribute("PKG_PARTY_NAME").getString());
            resultSet.updateString("PKG_STATION", getAttribute("PKG_STATION").getString());
            resultSet.updateString("PKG_TRANSPORT_MODE", getAttribute("PKG_TRANSPORT_MODE").getString());
            resultSet.updateString("PKG_BOX_SIZE", getAttribute("PKG_BOX_SIZE").getString());
            resultSet.updateString("PKG_MODE_PACKING", getAttribute("PKG_MODE_PACKING").getString());
            resultSet.updateString("PKG_CHARGE_CODE", getAttribute("PKG_CHARGE_CODE").getString());
            resultSet.updateString("PKG_WH_CD", getAttribute("PKG_WH_CD").getString());
            resultSet.updateInt("INVOICE_FLG",0);
            resultSet.updateInt("BALE_REOPEN_FLG",0);
            resultSet.updateInt("MODIFIED_BY", getAttribute("MODIFIED_BY").getInt());
            resultSet.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            resultSet.updateInt("CHANGED", 1);
            resultSet.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateRow();

            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int revisionNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_PKG_SLIP_HEADER_H WHERE PKG_DP_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("PKG_DP_DATE").getString()) + "' AND PKG_DP_NO='" + getAttribute("PKG_DP_NO").getString() + "'");
            revisionNo++;

            resultSetHistory.moveToInsertRow();
            resultSetHistory.updateInt("REVISION_NO", revisionNo);
            resultSetHistory.updateString("PKG_DP_NO", getAttribute("PKG_DP_NO").getString());
            resultSetHistory.updateString("PKG_DP_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PKG_DP_DATE").getString()));
            resultSetHistory.updateString("PKG_BALE_NO", getAttribute("PKG_BALE_NO").getString());
            mbldt = "";

            mbldt = EITLERPGLOBAL.formatDateDB(getAttribute("PKG_BALE_DATE").getString());
            if (mbldt.equals("")) {
                mbldt = null;
            }
            resultSetHistory.updateString("PKG_BALE_DATE", mbldt);
            resultSetHistory.updateString("PKG_PARTY_CODE", getAttribute("PKG_PARTY_CODE").getString());
            resultSetHistory.updateString("PKG_PARTY_NAME", getAttribute("PKG_PARTY_NAME").getString());
            resultSetHistory.updateString("PKG_STATION", getAttribute("PKG_STATION").getString());
            resultSetHistory.updateString("PKG_TRANSPORT_MODE", getAttribute("PKG_TRANSPORT_MODE").getString());
            resultSetHistory.updateString("PKG_BOX_SIZE", getAttribute("PKG_BOX_SIZE").getString());
            resultSetHistory.updateString("PKG_MODE_PACKING", getAttribute("PKG_MODE_PACKING").getString());
            resultSetHistory.updateString("PKG_CHARGE_CODE", getAttribute("PKG_CHARGE_CODE").getString());
            resultSetHistory.updateString("PKG_WH_CD", getAttribute("PKG_WH_CD").getString());
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
            data.Execute("DELETE FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_DP_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("PKG_DP_DATE").getString()) + "' AND PKG_DP_NO='" + getAttribute("PKG_DP_NO").getString() + "'");

            //Now insert records into Packing detail tables
            for (int i = 1; i <= hmFeltPackingDetails.size(); i++) {
                clsFeltPackingDetails ObjFeltPackingDetails = (clsFeltPackingDetails) hmFeltPackingDetails.get(Integer.toString(i));

                //Insert records into Packing detail table
                resultSetDetail.moveToInsertRow();
                resultSetDetail.updateString("PKG_DP_NO", getAttribute("PKG_DP_NO").getString());
                resultSetDetail.updateString("PKG_DP_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PKG_DP_DATE").getString()));
                resultSetDetail.updateString("PKG_BALE_NO", getAttribute("PKG_BALE_NO").getString());
                mbldt = "";

                mbldt = EITLERPGLOBAL.formatDateDB(getAttribute("PKG_BALE_DATE").getString());
                if (mbldt.equals("")) {
                    mbldt = null;
                }
                resultSetDetail.updateString("PKG_BALE_DATE", mbldt);
                resultSetDetail.updateString("PKG_PIECE_NO", ObjFeltPackingDetails.getAttribute("PKG_PIECE_NO").getString());
                resultSetDetail.updateFloat("PKG_LENGTH", (float) ObjFeltPackingDetails.getAttribute("PKG_LENGTH").getVal());
                resultSetDetail.updateFloat("PKG_WIDTH", (float) ObjFeltPackingDetails.getAttribute("PKG_WIDTH").getVal());
                resultSetDetail.updateFloat("PKG_GSM", (float) ObjFeltPackingDetails.getAttribute("PKG_GSM").getVal());
                resultSetDetail.updateFloat("PKG_WEIGHT", (float) ObjFeltPackingDetails.getAttribute("PKG_WEIGHT").getVal());
                resultSetDetail.updateFloat("PKG_SQM", (float) ObjFeltPackingDetails.getAttribute("PKG_SQM").getVal());
                resultSetDetail.updateFloat("PKG_SYN_PER", (float) ObjFeltPackingDetails.getAttribute("PKG_SYN_PER").getVal());
                resultSetDetail.updateString("PKG_STYLE", ObjFeltPackingDetails.getAttribute("PKG_STYLE").getString());
                resultSetDetail.updateString("PKG_MCN_POSITION_DESC", ObjFeltPackingDetails.getAttribute("PKG_MCN_POSITION_DESC").getString());
                resultSetDetail.updateString("PKG_MACHINE_NO", ObjFeltPackingDetails.getAttribute("PKG_MACHINE_NO").getString());
                resultSetDetail.updateString("PKG_ORDER_NO", ObjFeltPackingDetails.getAttribute("PKG_ORDER_NO").getString());
                
                 if(ObjFeltPackingDetails.getAttribute("PKG_ORDER_DATE").getString().equals("")) {
                    resultSetDetail.updateString("PKG_ORDER_DATE", "0000-00-00");
                } else {
                    resultSetDetail.updateString("PKG_ORDER_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltPackingDetails.getAttribute("PKG_ORDER_DATE").getString()));
                }
                
               // resultSetDetail.updateString("PKG_ORDER_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltPackingDetails.getAttribute("PKG_ORDER_DATE").getString()));
                resultSetDetail.updateString("PKG_PRODUCT_CODE", ObjFeltPackingDetails.getAttribute("PKG_PRODUCT_CODE").getString());
                resultSetDetail.updateString("PKG_PRODUCT_DESC", ObjFeltPackingDetails.getAttribute("PKG_PRODUCT_DESC").getString());
                resultSetDetail.updateString("PKG_PO_NO", ObjFeltPackingDetails.getAttribute("PKG_PO_NO").getString());
                
                 if(ObjFeltPackingDetails.getAttribute("PKG_PO_DATE").getString().equals("")) {
                    resultSetDetail.updateString("PKG_PO_DATE", "0000-00-00");
                } else {
                    resultSetDetail.updateString("PKG_PO_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltPackingDetails.getAttribute("PKG_PO_DATE").getString()));
                }
               // resultSetDetail.updateString("PKG_PO_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltPackingDetails.getAttribute("PKG_PO_DATE").getString()));
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
                resultSetDetailHistory.updateString("PKG_DP_NO", getAttribute("PKG_DP_NO").getString());
                resultSetDetailHistory.updateString("PKG_DP_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PKG_DP_DATE").getString()));
                resultSetDetailHistory.updateString("PKG_BALE_NO", getAttribute("PKG_BALE_NO").getString());
                mbldt = "";

                mbldt = EITLERPGLOBAL.formatDateDB(getAttribute("PKG_BALE_DATE").getString());
                if (mbldt.equals("")) {
                    mbldt = null;
                }
                resultSetDetailHistory.updateString("PKG_BALE_DATE", mbldt);
                resultSetDetailHistory.updateString("PKG_PIECE_NO", ObjFeltPackingDetails.getAttribute("PKG_PIECE_NO").getString());
                resultSetDetailHistory.updateFloat("PKG_LENGTH", (float) ObjFeltPackingDetails.getAttribute("PKG_LENGTH").getVal());
                resultSetDetailHistory.updateFloat("PKG_WIDTH", (float) ObjFeltPackingDetails.getAttribute("PKG_WIDTH").getVal());
                //resultSetDetailHistory.updateFloat("PKG_WEIGHT", (float) ObjFeltPackingDetails.getAttribute("PKG_WEIGHT").getVal());
                resultSetDetailHistory.updateFloat("PKG_GSM", (float) ObjFeltPackingDetails.getAttribute("PKG_GSM").getVal());
                resultSetDetailHistory.updateFloat("PKG_WEIGHT", (float) ObjFeltPackingDetails.getAttribute("PKG_WEIGHT").getVal());
                resultSetDetailHistory.updateFloat("PKG_SQM", (float) ObjFeltPackingDetails.getAttribute("PKG_SQM").getVal());
                resultSetDetailHistory.updateFloat("PKG_SYN_PER", (float) ObjFeltPackingDetails.getAttribute("PKG_SYN_PER").getVal());
                resultSetDetailHistory.updateString("PKG_STYLE", ObjFeltPackingDetails.getAttribute("PKG_STYLE").getString());
                resultSetDetailHistory.updateString("PKG_MCN_POSITION_DESC", ObjFeltPackingDetails.getAttribute("PKG_MCN_POSITION_DESC").getString());
                resultSetDetailHistory.updateString("PKG_MACHINE_NO", ObjFeltPackingDetails.getAttribute("PKG_MACHINE_NO").getString());
                resultSetDetailHistory.updateString("PKG_ORDER_NO", ObjFeltPackingDetails.getAttribute("PKG_ORDER_NO").getString());
                
                if(ObjFeltPackingDetails.getAttribute("PKG_ORDER_DATE").getString().equals("")) {
                    resultSetDetail.updateString("PKG_ORDER_DATE", "0000-00-00");
                } else {
                    resultSetDetailHistory.updateString("PKG_ORDER_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltPackingDetails.getAttribute("PKG_ORDER_DATE").getString()));
                }
                
                //resultSetDetailHistory.updateString("PKG_ORDER_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltPackingDetails.getAttribute("PKG_ORDER_DATE").getString()));
                resultSetDetailHistory.updateString("PKG_PRODUCT_CODE", ObjFeltPackingDetails.getAttribute("PKG_PRODUCT_CODE").getString());
                resultSetDetailHistory.updateString("PKG_PRODUCT_DESC", ObjFeltPackingDetails.getAttribute("PKG_PRODUCT_DESC").getString());
                resultSetDetailHistory.updateString("PKG_PO_NO", ObjFeltPackingDetails.getAttribute("PKG_PO_NO").getString());
                
                if(ObjFeltPackingDetails.getAttribute("PKG_PO_DATE").getString().equals("")) {
                    resultSetDetail.updateString("PKG_PO_DATE", "0000-00-00");
                } else {
                    resultSetDetailHistory.updateString("PKG_PO_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltPackingDetails.getAttribute("PKG_PO_DATE").getString()));
                }

                //resultSetDetailHistory.updateString("PKG_PO_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltPackingDetails.getAttribute("PKG_PO_DATE").getString()));
                resultSetDetailHistory.updateInt("UPDATED_BY", getAttribute("UPDATED_BY").getInt());
                //resultSetDetailHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("ENTRY_DATE").getString()));
                resultSetDetailHistory.updateInt("CHANGED", 1);
                resultSetDetailHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetailHistory.insertRow();
            }

            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = 715; //Felt Packing
            ObjFeltProductionApprovalFlow.DocNo = getDocumentNo(getAttribute("PKG_DP_NO").getString());
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.FELT_PKG_SLIP_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator = false;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "PKG_DP_NO";

            if (getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFeltProductionApprovalFlow.To = (int) getAttribute("SEND_DOC_TO").getVal();
                ObjFeltProductionApprovalFlow.ExplicitSendTo = true;
            }

            //==== Handling Rejected Documents ==========//
            boolean IsRejected = getAttribute("REJECTED").getBool();

            if (IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.FELT_PKG_SLIP_HEADER SET REJECTED=0,CHANGED=1 WHERE PKG_DP_NO='" + getAttribute("PKG_DP_NO").getString() + "' AND PKG_DP_DATE='" + getAttribute("PKG_DP_DATE").getString() + "' AND PKG_DP_NO='" + getAttribute("PKG_DP_NO").getString() + "'");
                //Remove Old Records from FELT DOCUMENT APPROVAL TABLE
                //data.Execute("DELETE FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=715 AND DOC_NO='"+getAttribute("DOC_NO").getString()+"'");

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

            SelectFirstFree aList = new SelectFirstFree();
            aList.ModuleID = 715;
            aList.FirstFreeNo = 220;
            FFNo = aList.FirstFreeNo;
            //setAttribute("PKG_BALE_DATE",EITLERPGLOBAL.getCurrentDate());
            //txtBaleDate.setText(EITLERPGLOBAL.getCurrentDate());
            if (ObjFeltProductionApprovalFlow.Status.equals("F")) {
                String bale_no= EITLERP.clsFirstFree.getNextFreeNo(2, 715, FFNo, true);
                setAttribute("PKG_BALE_NO",bale_no);
                data.Execute("UPDATE PRODUCTION.FELT_PKG_SLIP_HEADER SET PKG_BALE_DATE='" + EITLERPGLOBAL.getCurrentDateDB() + "',PKG_BALE_NO=" + bale_no + ",REJECTED=0,CHANGED=1  WHERE PKG_DP_NO='" + getAttribute("PKG_DP_NO").getString() + "' ");
                data.Execute("UPDATE PRODUCTION.FELT_PKG_SLIP_DETAIL SET PKG_BALE_DATE='" + EITLERPGLOBAL.getCurrentDateDB() + "',PKG_BALE_NO=" + bale_no + " WHERE PKG_DP_NO='" + getAttribute("PKG_DP_NO").getString() + "' ");
                System.out.println("sql = 1*");
                data.Execute("UPDATE PRODUCTION.FELT_PKG_SLIP_DETAIL A,PRODUCTION.FELT_SALES_PIECE_REGISTER B SET B.PR_BALE_NO=A.PKG_BALE_NO,B.PR_PACKED_DATE=A.PKG_BALE_DATE,B.PR_PIECE_STAGE='BSR',B.PR_PKG_DP_NO='"+getAttribute("PKG_DP_NO").getString()+"',B.PR_PKG_DP_DATE='"+EITLERPGLOBAL.formatDateDB(getAttribute("PKG_DP_DATE").getString())+"' WHERE B.PR_PIECE_NO=A.PKG_PIECE_NO AND A.PKG_DP_NO='"+getAttribute("PKG_DP_NO").getString()+"'");
//                for (int i = 1; i <= hmFeltPackingDetails.size(); i++) {
//                clsFeltPackingDetails ObjFeltPackingDetails = (clsFeltPackingDetails) hmFeltPackingDetails.get(Integer.toString(i));
//                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PIECE_STAGE='BSR',PR_BALE_NO='"+getAttribute("PKG_BALE_NO").getString()+"' WHERE PR_PIECE_NO='"+ObjFeltPackingDetails.getAttribute("PKG_PIECE_NO").getString()+"'");
//                }
                }
//             System.out.println("1 :UPDATE PRODUCTION.FELT_PKG_SLIP_HEADER SET PKG_BALE_DATE='" + EITLERPGLOBAL.getCurrentDateDB() + "',PKG_BALE_NO=" + EITLERP.clsFirstFree.getNextFreeNo(2, 715, FFNo, false) + ",REJECTED=0,CHANGED=1  WHERE PKG_DP_NO='" + getAttribute("PKG_DP_NO").getString() + "'");
//            System.out.println("2 :UPDATE PRODUCTION.FELT_PKG_SLIP_DETAIL SET PKG_BALE_DATE='" + EITLERPGLOBAL.getCurrentDateDB() + "',PKG_BALE_NO=" + EITLERP.clsFirstFree.getNextFreeNo(2, 715, FFNo, true) + " WHERE PKG_DP_NO='" + getAttribute("PKG_DP_NO").getString() + "' ");
            //    data.Execute("UPDATE PRODUCTION.FELT_PKG_SLIP_DETAIL SET PKG_BALE_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"',PKG_BALE_NO="+EITLERP.clsFirstFree.getNextFreeNo(2, 715, FFNo, true)+" WHERE PKG_DP_NO='"+getAttribute("PKG_DP_NO").getString()+"' ");

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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_DP_NO='" + baleNo + "' AND PKG_DP_DATE='" + EITLERPGLOBAL.formatDateDB(baleDate) + "' AND APPROVED=1";
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if (rsTmp.getInt("COUNT") > 0) {  //Packing is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=715 AND USER_ID=" + userID + " AND DOC_NO='" + getAttribute("DOC_NO").getString() + "' AND STATUS='W'";
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();

                if (rsTmp.getInt("COUNT") > 0) {
                    strSQL = "DELETE FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_DP_NO='" + baleNo + "' AND PKG_DP_DATE='" + EITLERPGLOBAL.formatDateDB(baleDate) + "'";
                    tmpStmt.executeUpdate(strSQL);
                    strSQL = "DELETE FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_DP_NO='" + baleNo + "' AND PKG_DP_DATE='" + EITLERPGLOBAL.formatDateDB(baleDate) + "'";
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_DP_NO='" + baleNo + "' AND PKG_DP_DATE='" + baleDate + "' AND APPROVED=1";
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=715 AND USER_ID=" + userID + " AND DOC_NO='" + baleNo + "' AND STATUS='W'";
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
//            //String strSql = "SELECT * FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_DP_NO=B.PKG_DP_NO AND " + stringFindQuery;
//            String strSql = "SELECT * FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE " + stringFindQuery;
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
            //String strSql = "SELECT * FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_DP_NO=B.PKG_DP_NO AND " + stringFindQuery;
            if(stringFindQuery.substring(0,13).equalsIgnoreCase(" PKG_PIECE_NO")){
//                System.out.println("QQQ - SELECT A.PKG_DP_NO FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_DP_NO=B.PKG_DP_NO AND B.PKG_PIECE_NO='"+stringFindQuery.substring(16));
//                dpno=data.getStringValueFromDB("SELECT A.PKG_DP_NO FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_DP_NO=B.PKG_DP_NO AND B.PKG_PIECE_NO='"+stringFindQuery.substring(16));
//                stringFindQuery=" PKG_DP_NO='"+dpno+"' ";
                
                System.out.println("QQQ - SELECT A.PKG_DP_NO FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_DP_NO=B.PKG_DP_NO AND B.PKG_PIECE_NO='"+stringFindQuery.substring(16));
                dpno=data.getStringValueFromDB("SELECT GROUP_CONCAT(A.PKG_DP_NO) FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_DP_NO=B.PKG_DP_NO AND B.PKG_PIECE_NO='"+stringFindQuery.substring(16));
                stringFindQuery=" ";
                String[] Pieces = dpno.split(",");
                for (int i = 0; i < Pieces.length; i++) {
                    if (i == 0) {
                        stringFindQuery += " (PKG_DP_NO = '" + Pieces[i] + "' ";
                    } else {
                        stringFindQuery += " OR PKG_DP_NO = '" + Pieces[i] + "' ";
                    }
                }
                stringFindQuery += ") ";
                
                System.out.println(" stringFindQuery" +stringFindQuery);
            }
            String strSql = "SELECT * FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE " + stringFindQuery;
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

            setAttribute("PKG_DP_NO", resultSet.getString("PKG_DP_NO"));
            setAttribute("PKG_DP_DATE", resultSet.getString("PKG_DP_DATE"));
            setAttribute("PKG_BALE_NO", resultSet.getString("PKG_BALE_NO"));
            setAttribute("PKG_BALE_DATE", resultSet.getString("PKG_BALE_DATE"));
            setAttribute("PKG_PARTY_CODE", resultSet.getString("PKG_PARTY_CODE"));
            setAttribute("PKG_PARTY_NAME", resultSet.getString("PKG_PARTY_NAME"));
            setAttribute("PKG_STATION", resultSet.getString("PKG_STATION"));
            setAttribute("PKG_TRANSPORT_MODE", resultSet.getString("PKG_TRANSPORT_MODE"));
            setAttribute("PKG_BOX_SIZE", resultSet.getString("PKG_BOX_SIZE"));
            setAttribute("PKG_MODE_PACKING", resultSet.getString("PKG_MODE_PACKING"));
            setAttribute("PKG_CHARGE_CODE", resultSet.getString("PKG_CHARGE_CODE"));
            setAttribute("HIERARCHY_ID", resultSet.getInt("HIERARCHY_ID"));
            
            //Now Populate the collection, first clear the collection
            hmFeltPackingDetails.clear();

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            if (HistoryView) {
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_PKG_SLIP_DETAIL_H WHERE PKG_DP_NO='" + resultSet.getString("PKG_DP_NO") + "' AND PKG_DP_DATE='" + resultSet.getString("PKG_DP_DATE") + "' AND REVISION_NO=" + RevNo);
            } else {
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_DP_NO='" + resultSet.getString("PKG_DP_NO") + "' AND PKG_DP_DATE='" + resultSet.getString("PKG_DP_DATE") + "'");
            }

            while (resultSetTemp.next()) {
                serialNoCounter++;
                clsFeltPackingDetails ObjFeltPackingDetails = new clsFeltPackingDetails();

                ObjFeltPackingDetails.setAttribute("PKG_DP_NO", resultSet.getString("PKG_DP_NO"));
                ObjFeltPackingDetails.setAttribute("PKG_DP_DATE", resultSet.getString("PKG_DP_DATE"));
                ObjFeltPackingDetails.setAttribute("PKG_BALE_NO", resultSet.getString("PKG_BALE_NO"));
                ObjFeltPackingDetails.setAttribute("PKG_BALE_DATE", resultSet.getString("PKG_BALE_DATE"));
                ObjFeltPackingDetails.setAttribute("PKG_PIECE_NO", resultSetTemp.getString("PKG_PIECE_NO"));
                ObjFeltPackingDetails.setAttribute("PKG_LENGTH", resultSetTemp.getFloat("PKG_LENGTH"));
                ObjFeltPackingDetails.setAttribute("PKG_WIDTH", resultSetTemp.getFloat("PKG_WIDTH"));
                ObjFeltPackingDetails.setAttribute("PKG_WEIGHT", resultSetTemp.getFloat("PKG_WEIGHT"));
                ObjFeltPackingDetails.setAttribute("PKG_GSM", resultSetTemp.getInt("PKG_GSM"));
                ObjFeltPackingDetails.setAttribute("PKG_SQM", resultSetTemp.getFloat("PKG_SQM"));
                ObjFeltPackingDetails.setAttribute("PKG_SYN_PER", resultSetTemp.getInt("PKG_SYN_PER"));
                ObjFeltPackingDetails.setAttribute("PKG_STYLE", resultSetTemp.getString("PKG_STYLE"));
                ObjFeltPackingDetails.setAttribute("PKG_PRODUCT_CODE", resultSetTemp.getString("PKG_PRODUCT_CODE"));
                ObjFeltPackingDetails.setAttribute("PKG_PRODUCT_DESC", resultSetTemp.getString("PKG_PRODUCT_DESC"));
                ObjFeltPackingDetails.setAttribute("PKG_MCN_POSITION_DESC", resultSetTemp.getString("PKG_MCN_POSITION_DESC"));
                ObjFeltPackingDetails.setAttribute("PKG_MACHINE_NO", resultSetTemp.getString("PKG_MACHINE_NO"));
                ObjFeltPackingDetails.setAttribute("PKG_ORDER_NO", resultSetTemp.getString("PKG_ORDER_NO"));
                ObjFeltPackingDetails.setAttribute("PKG_ORDER_DATE", resultSetTemp.getString("PKG_ORDER_DATE"));
                ObjFeltPackingDetails.setAttribute("PKG_PO_NO", resultSetTemp.getString("PKG_PO_NO"));
                ObjFeltPackingDetails.setAttribute("PKG_PO_DATE", resultSetTemp.getString("PKG_PO_DATE"));
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
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_PKG_SLIP_HEADER_H WHERE PKG_DP_DATE='" + baleDate + "' AND PKG_DP_NO='" + baleNo + "'");
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
            rsTmp = stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_PKG_SLIP_HEADER_H WHERE PKG_DP_DATE='" + dpDate + "' AND PKG_DP_NO='" + dpNo + "'");

            while (rsTmp.next()) {
                clsFeltPacking ObjFeltPacking = new clsFeltPacking();

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
                strSQL = "SELECT PKG_DP_DATE,PKG_DP_NO,RECEIVED_DATE,PKG_PARTY_CODE FROM PRODUCTION.FELT_PKG_SLIP_HEADER H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.PKG_DP_NO=D.DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=715 AND CANCELED=0 ORDER BY RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT PKG_DP_DATE,PKG_DP_NO,RECEIVED_DATE,PKG_PARTY_CODE FROM PRODUCTION.FELT_PKG_SLIP_HEADER H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.PKG_DP_NO=D.DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=715 AND CANCELED=0 ORDER BY PKG_DP_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT PKG_DP_DATE,PKG_DP_NO,RECEIVED_DATE,PKG_PARTY_CODE FROM PRODUCTION.FELT_PKG_SLIP_HEADER H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.PKG_DP_NO=D.DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=715 AND CANCELED=0 ORDER BY PKG_DP_NO";
            }

            ResultSet rsTmp = data.getConn().createStatement().executeQuery(strSQL);

            Counter = 0;
            while (rsTmp.next()) {

                Counter = Counter + 1;
                clsFeltPacking ObjDoc = new clsFeltPacking();

                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO", rsTmp.getString("PKG_DP_NO"));
                ObjDoc.setAttribute("DOC_DATE", rsTmp.getString("PKG_DP_DATE"));
                ObjDoc.setAttribute("RECEIVED_DATE", rsTmp.getString("RECEIVED_DATE"));
                ObjDoc.setAttribute("PARTY_CODE", rsTmp.getString("PKG_PARTY_CODE"));
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
        int count = data.getIntValueFromDB("SELECT COUNT(PKG_PIECE_NO) FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_PIECE_NO='" + pieceNo + "' AND PKG_BALE_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND PKG_BALE_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'");
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    // checks piece no already exist in db in edit mode
    public boolean checkPieceNoInDB(String pieceNo, String baleNo, String baleDate) {
        int count = data.getIntValueFromDB("SELECT COUNT(PKG_PIECE_NO) FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_PIECE_NO='" + pieceNo + "' AND PKG_BALE_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND PKG_BALE_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'");
        if (count >= 1) {
            int counter = 0;
            try {
                Connection Conn = data.getConn();
                Statement stTmp = Conn.createStatement();
                ResultSet rsTmp = stTmp.executeQuery("SELECT PKG_BALE_DATE,PKG_DP_NO FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_PIECE_NO='" + pieceNo + "' AND PKG_BALE_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND PKG_BALE_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'");
                while (rsTmp.next()) {
                    if (rsTmp.getString("PKG_BALE_DATE").equals(EITLERPGLOBAL.formatDateDB(baleDate)) && rsTmp.getString("PKG_DP_NO").equals(baleNo)) {
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
    
    public static String getStation(int pCompanyID, String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Station = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            rsTmp = stTmp.executeQuery("SELECT DISPATCH_STATION FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE=" + pPartyID);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                Station = rsTmp.getString("DISPATCH_STATION");
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

        } catch (Exception e) {

        }
        return Station;
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
                rsTmp=data.getResult("SELECT APPROVED FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_DP_NO='"+pAmendNo+"' AND APPROVED=0");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    Approved=rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();
                
                if(!Approved) {
                    //String purpose
                    String purpose = data.getStringValueFromDB("SELECT PURPOSE FROM DINESHMILLS.D_COM_DOC_CANCEL_REQUEST WHERE MODULE_ID=715 AND DOC_NO='"+pAmendNo+"'");
                    //data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='"+pAmendNo+"' AND MODULE_ID=715");
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='"+pAmendNo+"' AND MODULE_ID=715");
                    data.Execute("UPDATE PRODUCTION.FELT_PKG_SLIP_HEADER SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE(),PKG_CANCEL_REASON='"+purpose+"' WHERE PKG_DP_NO='"+pAmendNo+"'");
                    data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PKG_DP_NO='',PR_PKG_DP_DATE='0000-00-00' WHERE PR_PKG_DP_NO='"+pAmendNo+"'");
                }
//                data.Execute("UPDATE PRODUCTION.FELT_PKG_SLIP_HEADER SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE(),PKG_CANCEL_REASON='"+purpose+"' WHERE PKG_DP_NO='"+pAmendNo+"'");
//                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PKG_DP_NO='',PR_PKG_DP_DATE='0000-00-00' WHERE PR_PKG_DP_NO='"+pAmendNo+"'");
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
            rsTmp=stTmp.executeQuery("SELECT PKG_DP_NO FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_DP_NO='"+pDocNo+"'  AND APPROVED=0 AND CANCELED=0");
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
}
