/*
 * clsFeltWeaving.java
 *
 * Created on March 12, 2013, 3:10 PM
 */
package EITLERP.Production.FeltWeaveout;

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
import javax.swing.JOptionPane;

/**
 *
 * @author Vivek Kumar
 * @version
 */
public class clsFeltWeaveout {

    public String LastError = "";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public HashMap props;
    public boolean Ready = false;
    //Felt Production Weaving Details Collection
    public HashMap hmFeltWeavingDetails;

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
     * Creates new DataFeltProductionWeaving
     */
    public clsFeltWeaveout() {
        LastError = "";
        props = new HashMap();
        props.put("WVOUT_DATE", new Variant(""));
        props.put("WVOUT_DOC_NO", new Variant(""));
        props.put("WVOUT_SHIFT", new Variant(""));
        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));

        props.put("DOC_NO", new Variant(""));
        props.put("DOC_DATE", new Variant(""));
        props.put("RECEIVED_DATE", new Variant(""));
        // -- Approval Specific Fields --
        props.put("HIERARCHY_ID", new Variant(0));
        props.put("FROM", new Variant(0));
        props.put("TO", new Variant(0));
        props.put("REMARKS", new Variant(""));
        props.put("APPROVAL_STATUS", new Variant(""));

        hmFeltWeavingDetails = new HashMap();
    }

    public boolean LoadData() {
        Ready = false;
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            //resultSet = statement.executeQuery("SELECT DISTINCT PROD_DATE FROM PRODUCTION.`FELT_WVOUT_DATA` WHERE PROD_DEPT = 'WEAVING' AND PROD_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND PROD_DATE<='" + EITLERPGLOBAL.FinToDateDB + "' ORDER BY PROD_DATE");
            resultSet=statement.executeQuery("SELECT DISTINCT WVOUT_DOC_NO,WVOUT_DATE,WVOUT_SHIFT FROM PRODUCTION.FELT_WVOUT_DATA WHERE WVOUT_DATE >= '"+EITLERPGLOBAL.FinFromDateDB+"' AND WVOUT_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY WVOUT_DATE,WVOUT_DOC_NO");
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
                setHistoryData(historyProductionDate, historyDocumentNo);
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
                setHistoryData(historyProductionDate, historyDocumentNo);
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
                setHistoryData(historyProductionDate, historyDocumentNo);
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
                setHistoryData(historyProductionDate, historyDocumentNo);
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
            // Production data connection
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.`FELT_WVOUT_DATA` WHERE WVOUT_DATE=''");

            // Production data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.`FELT_WVOUT_DATA_H` WHERE WVOUT_DATE=''");

            //Now Insert records into production tables
            for (int i = 1; i <= hmFeltWeavingDetails.size(); i++) {
                clsFeltWeaveoutDetails ObjFeltProductionWeaveoutDetails = (clsFeltWeaveoutDetails) hmFeltWeavingDetails.get(Integer.toString(i));

                //Insert records into production data table
                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateString("WVOUT_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()));
                resultSetTemp.updateString("WVOUT_DOC_NO", getAttribute("PRODUCTION_DOCUMENT_NO").getString());
                resultSetTemp.updateString("WVOUT_SHIFT", getAttribute("PRODUCTION_FORM_NO").getString());
                resultSetTemp.updateString("PROD_DEPT", "WEAVING");
                resultSetTemp.updateInt("SR_NO", i);
                resultSetTemp.updateString("WVOUT_PIECE_NO", ObjFeltProductionWeaveoutDetails.getAttribute("PRODUCTION_PIECE_NO").getString());
                resultSetTemp.updateString("WVOUT_PARTY_CODE", ObjFeltProductionWeaveoutDetails.getAttribute("PRODUCTION_PARTY_CODE").getString());                
                resultSetTemp.updateString("PICKS_PER_10CMS", ObjFeltProductionWeaveoutDetails.getAttribute("PICKS_PER_10CMS").getString());
                resultSetTemp.updateFloat("REED_SPACE", (float) EITLERPGLOBAL.round(ObjFeltProductionWeaveoutDetails.getAttribute("REED_SPACE").getVal(), 2));
                resultSetTemp.updateFloat("LENGTH", (float) EITLERPGLOBAL.round(ObjFeltProductionWeaveoutDetails.getAttribute("LENGTH").getVal(), 2));
                resultSetTemp.updateInt("LOOM_NO", (int) ObjFeltProductionWeaveoutDetails.getAttribute("LOOM_NO").getVal());
                resultSetTemp.updateString("BEAM_NO", ObjFeltProductionWeaveoutDetails.getAttribute("BEAM_NO").getString());
                resultSetTemp.updateFloat("PICK", (float) EITLERPGLOBAL.round(ObjFeltProductionWeaveoutDetails.getAttribute("PICK").getVal(), 2));
                resultSetTemp.updateFloat("PICKMTR", (float) EITLERPGLOBAL.round(ObjFeltProductionWeaveoutDetails.getAttribute("PICKMTR").getVal(), 2));
                
                resultSetTemp.updateString("GROUP", ObjFeltProductionWeaveoutDetails.getAttribute("GROUP").getString());
                resultSetTemp.updateString("PRODUCT_CODE", ObjFeltProductionWeaveoutDetails.getAttribute("PRODUCT_CODE").getString());
                
                //resultSetTemp.updateString("REMARKS", ObjFeltProductionWeaveoutDetails.getAttribute("REMARKS").getString());
                resultSetTemp.updateString("WEAVE_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltProductionWeaveoutDetails.getAttribute("WEAVE_DATE").getString()));
                resultSetTemp.updateString("WARP_NO", ObjFeltProductionWeaveoutDetails.getAttribute("WARP_NO").getString());
                //resultSetTemp.updateString("WEAVE_DIFF_DAYS", ObjFeltProductionWeaveoutDetails.getAttribute("WEAVE_DIFF_DAYS").getString());
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
                resultSetHistory.updateString("WVOUT_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()));
                resultSetHistory.updateString("WVOUT_DOC_NO", getAttribute("PRODUCTION_DOCUMENT_NO").getString());
                resultSetHistory.updateString("WVOUT_SHIFT", getAttribute("PRODUCTION_FORM_NO").getString());
                resultSetHistory.updateString("PROD_DEPT", "WEAVING");
                resultSetHistory.updateInt("SR_NO", i);
                resultSetHistory.updateString("WVOUT_PIECE_NO", ObjFeltProductionWeaveoutDetails.getAttribute("PRODUCTION_PIECE_NO").getString());
                resultSetHistory.updateString("WVOUT_PARTY_CODE", ObjFeltProductionWeaveoutDetails.getAttribute("PRODUCTION_PARTY_CODE").getString());
                
                resultSetHistory.updateString("PICKS_PER_10CMS", ObjFeltProductionWeaveoutDetails.getAttribute("PICKS_PER_10CMS").getString());
                resultSetHistory.updateFloat("REED_SPACE", (float) EITLERPGLOBAL.round(ObjFeltProductionWeaveoutDetails.getAttribute("REED_SPACE").getVal(), 2));
                resultSetHistory.updateFloat("LENGTH", (float) EITLERPGLOBAL.round(ObjFeltProductionWeaveoutDetails.getAttribute("LENGTH").getVal(), 2));
                resultSetHistory.updateInt("LOOM_NO", (int) ObjFeltProductionWeaveoutDetails.getAttribute("LOOM_NO").getVal());
                resultSetHistory.updateString("GROUP", ObjFeltProductionWeaveoutDetails.getAttribute("GROUP").getString());
                resultSetHistory.updateFloat("PICK", (float) EITLERPGLOBAL.round(ObjFeltProductionWeaveoutDetails.getAttribute("PICK").getVal(), 2));
                resultSetHistory.updateFloat("PICKMTR", (float) EITLERPGLOBAL.round(ObjFeltProductionWeaveoutDetails.getAttribute("PICKMTR").getVal(), 2));
                
                resultSetHistory.updateString("GROUP", ObjFeltProductionWeaveoutDetails.getAttribute("GROUP").getString());
                resultSetHistory.updateString("PRODUCT_CODE", ObjFeltProductionWeaveoutDetails.getAttribute("PRODUCT_CODE").getString());
                //resultSetHistory.updateString("REMARKS", ObjFeltProductionWeaveoutDetails.getAttribute("REMARKS").getString());
                resultSetHistory.updateString("WEAVE_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltProductionWeaveoutDetails.getAttribute("WEAVE_DATE").getString()));
                resultSetHistory.updateString("WARP_NO", ObjFeltProductionWeaveoutDetails.getAttribute("WARP_NO").getString());
                //resultSetHistory.updateString("WEAVE_DIFF_DAYS", ObjFeltProductionWeaveoutDetails.getAttribute("WEAVE_DIFF_DAYS").getString());
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
                
                try {
                    if ("F".equals(getAttribute("APPROVAL_STATUS").getString())) {
                        if (data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL WHERE PIECE_NO='"+ObjFeltProductionWeaveoutDetails.getAttribute("PRODUCTION_PIECE_NO").getString()+"'")) {
                            data.Execute("UPDATE PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL SET WEAVING_DATE=CURRENT_DATE WHERE PIECE_NO='"+ObjFeltProductionWeaveoutDetails.getAttribute("PRODUCTION_PIECE_NO").getString()+"'");                            
                        } else if (data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_DETAIL WHERE PIECE_NO='"+ObjFeltProductionWeaveoutDetails.getAttribute("PRODUCTION_PIECE_NO").getString()+"'")) {
                            data.Execute("UPDATE PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_DETAIL SET WEAVING_DATE=CURRENT_DATE WHERE PIECE_NO='"+ObjFeltProductionWeaveoutDetails.getAttribute("PRODUCTION_PIECE_NO").getString()+"'");                                                        
                        } else {
                            
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                
            }

            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = 703; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("PRODUCTION_DOCUMENT_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName = "FELT_WVOUT_DATA";
            ObjFeltProductionApprovalFlow.IsCreator = true;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "WVOUT_DOC_NO";

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
            if (ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {
                ObjFeltProductionApprovalFlow.finalApproved = false;
                updateWeavingDate(getAttribute("PRODUCTION_DOCUMENT_NO").getString());
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
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.`FELT_WVOUT_DATA` WHERE WVOUT_DATE=''");

            // Production data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //Get the Maximum Revision No in History Table.
            resultSetHistory = statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_WVOUT_DATA_H WHERE PROD_DEPT = 'WEAVING' AND WVOUT_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()) + "' AND WVOUT_DOC_NO='" + getAttribute("PRODUCTION_DOCUMENT_NO").getString() + "'");
            resultSetHistory.first();
            if (resultSetHistory.getRow() > 0) {
                revisionNo = resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from production data table before insert
            statementHistory.execute("DELETE FROM PRODUCTION.`FELT_WVOUT_DATA` WHERE WVOUT_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()) + "' AND WVOUT_DOC_NO='" + getAttribute("PRODUCTION_DOCUMENT_NO").getString() + "'");

            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.`FELT_WVOUT_DATA_H` WHERE WVOUT_DATE=''");

            //Now Update records into production tables
            for (int i = 1; i <= hmFeltWeavingDetails.size(); i++) {
                clsFeltWeaveoutDetails ObjFeltProductionWeaveoutDetails = (clsFeltWeaveoutDetails) hmFeltWeavingDetails.get(Integer.toString(i));

                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateString("WVOUT_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()));
                resultSetTemp.updateString("WVOUT_DOC_NO", getAttribute("PRODUCTION_DOCUMENT_NO").getString());
                resultSetTemp.updateString("WVOUT_SHIFT", getAttribute("PRODUCTION_FORM_NO").getString());
                resultSetTemp.updateString("PROD_DEPT", "WEAVING");
                resultSetTemp.updateInt("SR_NO", i);
                resultSetTemp.updateString("WVOUT_PIECE_NO", ObjFeltProductionWeaveoutDetails.getAttribute("PRODUCTION_PIECE_NO").getString());
                resultSetTemp.updateString("WVOUT_PARTY_CODE", ObjFeltProductionWeaveoutDetails.getAttribute("PRODUCTION_PARTY_CODE").getString());
                
                resultSetTemp.updateString("PICKS_PER_10CMS", ObjFeltProductionWeaveoutDetails.getAttribute("PICKS_PER_10CMS").getString());
                resultSetTemp.updateFloat("REED_SPACE", (float) EITLERPGLOBAL.round(ObjFeltProductionWeaveoutDetails.getAttribute("REED_SPACE").getVal(), 2));
                resultSetTemp.updateFloat("LENGTH", (float) EITLERPGLOBAL.round(ObjFeltProductionWeaveoutDetails.getAttribute("LENGTH").getVal(), 2));                
                resultSetTemp.updateInt("LOOM_NO", (int) ObjFeltProductionWeaveoutDetails.getAttribute("LOOM_NO").getVal());
                resultSetTemp.updateString("BEAM_NO", ObjFeltProductionWeaveoutDetails.getAttribute("BEAM_NO").getString());
                
                resultSetTemp.updateFloat("PICK", (float) EITLERPGLOBAL.round(ObjFeltProductionWeaveoutDetails.getAttribute("PICK").getVal(), 2));                
                resultSetTemp.updateFloat("PICKMTR", (float) EITLERPGLOBAL.round(ObjFeltProductionWeaveoutDetails.getAttribute("PICKMTR").getVal(), 2));                
                
                resultSetTemp.updateString("GROUP", ObjFeltProductionWeaveoutDetails.getAttribute("GROUP").getString());
                resultSetTemp.updateString("PRODUCT_CODE", ObjFeltProductionWeaveoutDetails.getAttribute("PRODUCT_CODE").getString());
                //resultSetTemp.updateString("REMARKS", ObjFeltProductionWeaveoutDetails.getAttribute("REMARKS").getString());
                resultSetTemp.updateString("WEAVE_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltProductionWeaveoutDetails.getAttribute("WEAVE_DATE").getString()));
                resultSetTemp.updateString("WARP_NO", ObjFeltProductionWeaveoutDetails.getAttribute("WARP_NO").getString());
                //resultSetTemp.updateString("WEAVE_DIFF_DAYS", ObjFeltProductionWeaveoutDetails.getAttribute("WEAVE_DIFF_DAYS").getString());
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
                resultSetHistory.updateString("WVOUT_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()));
                resultSetHistory.updateString("WVOUT_DOC_NO", getAttribute("PRODUCTION_DOCUMENT_NO").getString());
                resultSetHistory.updateString("WVOUT_SHIFT", getAttribute("PRODUCTION_FORM_NO").getString());
                resultSetHistory.updateString("PROD_DEPT", "WEAVING");
                resultSetHistory.updateInt("SR_NO", i);
                resultSetHistory.updateString("WVOUT_PIECE_NO", ObjFeltProductionWeaveoutDetails.getAttribute("PRODUCTION_PIECE_NO").getString());
                resultSetHistory.updateString("WVOUT_PARTY_CODE", ObjFeltProductionWeaveoutDetails.getAttribute("PRODUCTION_PARTY_CODE").getString());
                
                resultSetHistory.updateString("PICKS_PER_10CMS", ObjFeltProductionWeaveoutDetails.getAttribute("PICKS_PER_10CMS").getString());
                resultSetHistory.updateFloat("REED_SPACE", (float) EITLERPGLOBAL.round(ObjFeltProductionWeaveoutDetails.getAttribute("REED_SPACE").getVal(), 2));
                resultSetHistory.updateFloat("LENGTH", (float) EITLERPGLOBAL.round(ObjFeltProductionWeaveoutDetails.getAttribute("LENGTH").getVal(), 2));
                resultSetHistory.updateInt("LOOM_NO", (int) ObjFeltProductionWeaveoutDetails.getAttribute("LOOM_NO").getVal());
                resultSetHistory.updateString("BEAM_NO", ObjFeltProductionWeaveoutDetails.getAttribute("BEAM_NO").getString());
                resultSetHistory.updateFloat("PICK", (float) EITLERPGLOBAL.round(ObjFeltProductionWeaveoutDetails.getAttribute("PICK").getVal(), 2));
                resultSetHistory.updateFloat("PICKMTR", (float) EITLERPGLOBAL.round(ObjFeltProductionWeaveoutDetails.getAttribute("PICKMTR").getVal(), 2));
                
                resultSetHistory.updateString("GROUP", ObjFeltProductionWeaveoutDetails.getAttribute("GROUP").getString());
                resultSetHistory.updateString("PRODUCT_CODE", ObjFeltProductionWeaveoutDetails.getAttribute("PRODUCT_CODE").getString());
                //resultSetHistory.updateString("REMARKS", ObjFeltProductionWeaveoutDetails.getAttribute("REMARKS").getString());
                resultSetHistory.updateString("WEAVE_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltProductionWeaveoutDetails.getAttribute("WEAVE_DATE").getString()));
                resultSetHistory.updateString("WARP_NO", ObjFeltProductionWeaveoutDetails.getAttribute("WARP_NO").getString());
                //resultSetHistory.updateString("WEAVE_DIFF_DAYS", ObjFeltProductionWeaveoutDetails.getAttribute("WEAVE_DIFF_DAYS").getString());
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

                try {
                    if ("F".equals(getAttribute("APPROVAL_STATUS").getString())) {
                        if (data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL WHERE PIECE_NO='"+ObjFeltProductionWeaveoutDetails.getAttribute("PRODUCTION_PIECE_NO").getString()+"'")) {
                            data.Execute("UPDATE PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL SET WEAVING_DATE=CURRENT_DATE WHERE PIECE_NO='"+ObjFeltProductionWeaveoutDetails.getAttribute("PRODUCTION_PIECE_NO").getString()+"'");                            
                        } else if (data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_DETAIL WHERE PIECE_NO='"+ObjFeltProductionWeaveoutDetails.getAttribute("PRODUCTION_PIECE_NO").getString()+"'")) {
                            data.Execute("UPDATE PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_DETAIL SET WEAVING_DATE=CURRENT_DATE WHERE PIECE_NO='"+ObjFeltProductionWeaveoutDetails.getAttribute("PRODUCTION_PIECE_NO").getString()+"'");                                                        
                        } else {
                            
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = 703; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("PRODUCTION_DOCUMENT_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.FELT_WVOUT_DATA";
            ObjFeltProductionApprovalFlow.IsCreator = false;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "WVOUT_DOC_NO";

            if (getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFeltProductionApprovalFlow.To = (int) getAttribute("SEND_DOC_TO").getVal();
                ObjFeltProductionApprovalFlow.ExplicitSendTo = true;
            }
            updateWeavingcounter(getAttribute("PRODUCTION_DOCUMENT_NO").getString());
            //==== Handling Rejected Documents ==========//
            boolean IsRejected = getAttribute("REJECTED").getBool();

            if (IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.`FELT_WVOUT_DATA` SET REJECTED=0,CHANGED=1 WHERE PROD_DEPT='WEAVING' AND WVOUT_DOC_NO='" + getAttribute("PRODUCTION_DOCUMENT_NO").getString() + "'");
                //Remove Old Records from FELT DOCUMET APPROVAL TABLE
                //data.Execute("DELETE FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=707 AND DOC_NO='"+getAttribute("PRODUCTION_DOCUMENT_NO").getString()+"'");

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

            // Update Weaving Date in Order Master Table To confirm that Weaving has completed
            if (ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {
                ObjFeltProductionApprovalFlow.finalApproved = false;
                updateWeavingDate(getAttribute("PRODUCTION_DOCUMENT_NO").getString());
//                updateWeavingRemark(EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()), getAttribute("PRODUCTION_DOCUMENT_NO").getString());
//                data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W,"
//                        + "(SELECT WIP_PIECE_NO AS PC,WIP_EXT_PIECE_NO AS PCE,PROD_DATE,WEIGHT FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_WVOUT_DATA "
//                        + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DATE >= '2019-01-01' AND PROD_DEPT ='WEAVING' AND CANCELED =0 AND APPROVED =1 "
//                        + "AND WIP_PIECE_AB_FLAG ='AB' AND  WIP_PRIORITY_HOLD_CAN_FLAG IN (0,5) "
//                        + "AND RIGHT(WIP_EXT_PIECE_NO,2) ='-A' AND WEIGHT > 0 AND PROD_DOC_NO='" + getAttribute("PRODUCTION_DOCUMENT_NO").getString() + "' "
//                        + ") AS A "
//                        + "SET  WIP_WVG_A_DATE = PROD_DATE,WIP_WEAVING_WEIGHT_A =WEIGHT,WIP_EXP_DISPATCH_DATE = PROD_DATE,WIP_HOLD_REASON =WEIGHT "
//                        + "WHERE W.WIP_PRIORITY_HOLD_CAN_FLAG IN (1) AND A.PC = W.WIP_PIECE_NO");
//                data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W,"
//                        + "(SELECT WIP_PIECE_NO AS PC,WIP_EXT_PIECE_NO AS PCE,PROD_DATE,WEIGHT FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_WVOUT_DATA "
//                        + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DATE >= '2019-01-01' AND PROD_DEPT ='WEAVING' AND CANCELED =0 AND APPROVED =1 "
//                        + "AND WIP_PIECE_AB_FLAG ='AB' AND  WIP_PRIORITY_HOLD_CAN_FLAG IN (0,5) "
//                        + "AND RIGHT(WIP_EXT_PIECE_NO,2) ='-B' AND WEIGHT > 0 AND PROD_DOC_NO='" + getAttribute("PRODUCTION_DOCUMENT_NO").getString() + "' "
//                        + ") AS A "
//                        + "SET  WIP_WVG_B_DATE = PROD_DATE,WIP_WEAVING_WEIGHT_B =WEIGHT,WIP_HOLD_DATE =PROD_DATE,WIP_HOLD_REASON =WEIGHT "
//                        + "WHERE W.WIP_PRIORITY_HOLD_CAN_FLAG IN (1) AND A.PC = W.WIP_PIECE_NO");
//                data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W,"
//                        + "(SELECT WIP_PIECE_NO AS PIECE,MAX(PROD_DATE) AS MAXDATE,GROUP_CONCAT(WIP_EXT_PIECE_NO,' (',DATE_FORMAT(PROD_DATE,'%d/%m/%Y'), ') ' ORDER BY WIP_EXT_PIECE_NO SEPARATOR ', ') AS PIECEPROD ,SUM(WEIGHT) AS SUMWEIGHT "
//                        + "FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_WVOUT_DATA "
//                        + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DEPT ='WEAVING' AND CANCELED =0 AND APPROVED =1 "
//                        + "AND PROD_DOC_NO='" + getAttribute("PRODUCTION_DOCUMENT_NO").getString() + "' "
//                        + "GROUP BY WIP_PIECE_NO "
//                        + ") AS A "
//                        + "SET WIP_WVG_DATE = MAXDATE, WIP_WVG_LAYER_REMARK = PIECEPROD, WIP_WEAVING_WEIGHT = SUMWEIGHT "
//                        + "WHERE W.WIP_PRIORITY_HOLD_CAN_FLAG IN (1) AND A.PIECE = W.WIP_PIECE_NO AND COALESCE(WIP_PIECE_AB_FLAG,'') ='AB'");
//                data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W,"
//                        + "(SELECT WIP_PIECE_NO AS PIECE,MAX(PROD_DATE) AS MAXDATE,GROUP_CONCAT(WIP_EXT_PIECE_NO,' (',DATE_FORMAT(PROD_DATE,'%d/%m/%Y'), ') ' ORDER BY WIP_EXT_PIECE_NO SEPARATOR ', ') AS PIECEPROD ,SUM(WEIGHT) AS SUMWEIGHT "
//                        + "FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_WVOUT_DATA "
//                        + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DEPT ='WEAVING' AND CANCELED =0 AND APPROVED =1 "
//                        + "AND PROD_DOC_NO='" + getAttribute("PRODUCTION_DOCUMENT_NO").getString() + "' "
//                        + "GROUP BY WIP_PIECE_NO "
//                        + ") AS A "
//                        + "SET WIP_WVG_DATE = MAXDATE, WIP_WVG_LAYER_REMARK = PIECEPROD, WIP_WEAVING_WEIGHT = SUMWEIGHT "
//                        + "WHERE W.WIP_PRIORITY_HOLD_CAN_FLAG IN (0) AND A.PIECE = W.WIP_PIECE_NO AND COALESCE(WIP_PIECE_AB_FLAG,'') !='AB'");

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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_WVOUT_DATA` WHERE PROD_DEPT = 'WEAVING' AND WVOUT_DOC_NO='" + documentNo + "' AND APPROVED=" + 1;
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=703 AND USER_ID=" + userID + " AND DOC_NO='" + documentNo + "' AND STATUS='W'";
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();

                if (rsTmp.getInt("COUNT") > 0) {
                    strSQL = "DELETE FROM PRODUCTION.`FELT_WVOUT_DATA` WHERE PROD_DEPT = 'WEAVING'"
                            + " AND WVOUT_DATE= '" + EITLERPGLOBAL.formatDateDB(stringProductionDate) + "'"
                            + " AND WVOUT_DOC_NO='" + documentNo + "'";
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
    public boolean IsEditable(String weavingDocumentNo, int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";
        try {
            if (HistoryView) {
                return false;
            }
            tmpStmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //First check that document is approved or not
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_WVOUT_DATA` WHERE PROD_DEPT = 'WEAVING' AND WVOUT_DOC_NO='" + weavingDocumentNo + "' AND APPROVED=1";
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=703 AND USER_ID=" + userID + " AND DOC_NO='" + weavingDocumentNo + "' AND STATUS='W'";
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
            String strSql = "SELECT DISTINCT WVOUT_DATE FROM PRODUCTION.`FELT_WVOUT_DATA` WHERE PROD_DEPT = 'WEAVING' " + stringFindQuery + " ORDER BY WVOUT_DATE";
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
            setAttribute("PRODUCTION_DATE", resultSet.getDate("WVOUT_DATE"));                    

            //Now Populate the collection, first clear the collection
            hmFeltWeavingDetails.clear();

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.`FELT_WVOUT_DATA` WHERE PROD_DEPT = 'WEAVING'  AND WVOUT_DATE='" + resultSet.getDate("WVOUT_DATE") + "' ORDER BY SR_NO");

            while (resultSetTemp.next()) {
                serialNoCounter++;
                setAttribute("PRODUCTION_DOCUMENT_NO", resultSetTemp.getString("WVOUT_DOC_NO"));
                //setAttribute("PRODUCTION_FORM_NO", resultSetTemp.getString("WVOUT_SHIFT"));
                setAttribute("WVOUT_SHIFT", resultSetTemp.getString("WVOUT_SHIFT"));
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

                clsFeltWeaveoutDetails ObjFeltProductionWeavingDetails = new clsFeltWeaveoutDetails();

                ObjFeltProductionWeavingDetails.setAttribute("PRODUCTION_PIECE_NO", resultSetTemp.getString("WVOUT_PIECE_NO"));
                ObjFeltProductionWeavingDetails.setAttribute("PRODUCTION_PARTY_CODE", resultSetTemp.getString("WVOUT_PARTY_CODE"));                
                ObjFeltProductionWeavingDetails.setAttribute("PICKS_PER_10CMS", resultSetTemp.getString("PICKS_PER_10CMS"));
                ObjFeltProductionWeavingDetails.setAttribute("REED_SPACE", resultSetTemp.getFloat("REED_SPACE"));
                ObjFeltProductionWeavingDetails.setAttribute("LENGTH", resultSetTemp.getFloat("LENGTH"));
                ObjFeltProductionWeavingDetails.setAttribute("LOOM_NO", resultSetTemp.getInt("LOOM_NO"));
                ObjFeltProductionWeavingDetails.setAttribute("BEAM_NO", resultSetTemp.getString("BEAM_NO"));
                
                ObjFeltProductionWeavingDetails.setAttribute("PRODUCT_CODE", resultSetTemp.getString("PRODUCT_CODE"));
                ObjFeltProductionWeavingDetails.setAttribute("GROUP", resultSetTemp.getString("GROUP"));
                
                ObjFeltProductionWeavingDetails.setAttribute("PICK", resultSetTemp.getFloat("PICK"));
                ObjFeltProductionWeavingDetails.setAttribute("PICKMTR", resultSetTemp.getFloat("PICKMTR"));
                
                //ObjFeltProductionWeavingDetails.setAttribute("REMARKS", resultSetTemp.getString("REMARKS"));
                ObjFeltProductionWeavingDetails.setAttribute("WEAVE_DATE", resultSetTemp.getString("WEAVE_DATE"));
                ObjFeltProductionWeavingDetails.setAttribute("WARP_NO", resultSetTemp.getString("WARP_NO"));
                ObjFeltProductionWeavingDetails.setAttribute("WEAVE_DIFF_DAYS", resultSetTemp.getString("WEAVE_DIFF_DAYS"));

                hmFeltWeavingDetails.put(Integer.toString(serialNoCounter), ObjFeltProductionWeavingDetails);
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
            hmFeltWeavingDetails.clear();

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.`FELT_WVOUT_DATA_H` WHERE PROD_DEPT = 'WEAVING' AND WVOUT_DATE='" + pProductionDate + "' AND WVOUT_DOC_NO='" + pDocNo + "' AND REVISION_NO='" + resultSet.getInt("REVISION_NO") + "' ORDER BY REVISION_NO");

            while (resultSetTemp.next()) {
                serialNoCounter++;
                setAttribute("PRODUCTION_DOCUMENT_NO", resultSetTemp.getString("WVOUT_DOC_NO"));
                setAttribute("PRODUCTION_FORM_NO", resultSetTemp.getString("WVOUT_SHIFT"));
                setAttribute("UPDATED_BY", resultSetTemp.getInt("UPDATED_BY"));
                setAttribute("ENTRY_DATE", resultSetTemp.getString("ENTRY_DATE"));
                setAttribute("HIERARCHY_ID", resultSetTemp.getInt("HIERARCHY_ID"));

                clsFeltWeaveoutDetails ObjFeltProductionWeavingDetails = new clsFeltWeaveoutDetails();

                ObjFeltProductionWeavingDetails.setAttribute("PRODUCTION_PIECE_NO", resultSetTemp.getString("WVOUT_PIECE_NO"));
                ObjFeltProductionWeavingDetails.setAttribute("PRODUCTION_PARTY_CODE", resultSetTemp.getString("WVOUT_PARTY_CODE"));
                
                ObjFeltProductionWeavingDetails.setAttribute("PICKS_PER_10CMS", resultSetTemp.getString("PICKS_PER_10CMS"));
                ObjFeltProductionWeavingDetails.setAttribute("REED_SPACE", resultSetTemp.getFloat("REED_SPACE"));
                ObjFeltProductionWeavingDetails.setAttribute("LENGTH", resultSetTemp.getFloat("LENGTH"));
                ObjFeltProductionWeavingDetails.setAttribute("LOOM_NO", resultSetTemp.getInt("LOOM_NO"));
                
                ObjFeltProductionWeavingDetails.setAttribute("PICK", resultSetTemp.getFloat("PICK"));
                ObjFeltProductionWeavingDetails.setAttribute("PICKMTR", resultSetTemp.getFloat("PICKMTR"));
                
                ObjFeltProductionWeavingDetails.setAttribute("REMARKS", resultSetTemp.getString("REMARKS"));
                ObjFeltProductionWeavingDetails.setAttribute("WEAVE_DATE", resultSetTemp.getString("WEAVE_DATE"));
                ObjFeltProductionWeavingDetails.setAttribute("WARP_NO", resultSetTemp.getString("WARP_NO"));
                ObjFeltProductionWeavingDetails.setAttribute("WEAVE_DIFF_DAYS", resultSetTemp.getString("WEAVE_DIFF_DAYS"));

                hmFeltWeavingDetails.put(Integer.toString(serialNoCounter), ObjFeltProductionWeavingDetails);
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
        try {
            stTmp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp = stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_WVOUT_DATA_H WHERE PROD_DEPT = 'WEAVING' AND WVOUT_DATE='" + stringProductionDate + "' AND WVOUT_DOC_NO='" + productionDocumentNo + "' GROUP BY REVISION_NO");

            while (rsTmp.next()) {
                clsFeltWeaveout ObjFeltWeaving = new clsFeltWeaveout();

                ObjFeltWeaving.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                ObjFeltWeaving.setAttribute("UPDATED_BY", rsTmp.getInt("UPDATED_BY"));
                ObjFeltWeaving.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                ObjFeltWeaving.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                ObjFeltWeaving.setAttribute("APPROVER_REMARKS", rsTmp.getString("APPROVER_REMARKS"));
                ObjFeltWeaving.setAttribute("FROM_IP", rsTmp.getString("FROM_IP"));

                hmHistoryList.put(Integer.toString(hmHistoryList.size() + 1), ObjFeltWeaving);
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
            resultSet = statement.executeQuery("SELECT DISTINCT REVISION_NO FROM PRODUCTION.FELT_WVOUT_DATA_H WHERE PROD_DEPT='WEAVING' AND WVOUT_DATE='" + pProductionDate + "' AND WVOUT_DOC_NO='" + pDocNo + "'");
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
                strSQL = "SELECT DISTINCT WVOUT_DOC_NO,WVOUT_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_WVOUT_DATA,PRODUCTION.FELT_PROD_DOC_DATA WHERE WVOUT_DOC_NO=DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=703 AND CANCELED=0 ORDER BY RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT WVOUT_DOC_NO,WVOUT_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_WVOUT_DATA,PRODUCTION.FELT_PROD_DOC_DATA WHERE WVOUT_DOC_NO=DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=703 AND CANCELED=0 ORDER BY WVOUT_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT WVOUT_DOC_NO,WVOUT_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_WVOUT_DATA,PRODUCTION.FELT_PROD_DOC_DATA WHERE WVOUT_DOC_NO=DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=703 AND CANCELED=0 ORDER BY WVOUT_DOC_NO";
            }

            ResultSet rsTmp = data.getConn().createStatement().executeQuery(strSQL);

            Counter = 0;
            while (rsTmp.next()) {

                Counter = Counter + 1;
                clsFeltWeaveout ObjDoc = new clsFeltWeaveout();

                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO", rsTmp.getString("WVOUT_DOC_NO"));
                ObjDoc.setAttribute("DOC_DATE", rsTmp.getString("WVOUT_DATE"));
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
        //return data.getStringValueFromDB("SELECT PR_PARTY_CODE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='"+pPieceNo+"'");
        //return data.getStringValueFromDB("SELECT PR_PARTY_CODE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO+0='"+pPieceNo+"'");
        return data.getStringValueFromDB("SELECT WIP_PARTY_CODE FROM PRODUCTION.FELT_WIP_PIECE_REGISTER WHERE WIP_EXT_PIECE_NO='" + pPieceNo + "'");
    }

    // Updates Weaving Date in Order Master Table To confirm that Weaving has completed
    private void updateWeavingDate(String documentNo) {
        
    }

    private void updateWeavingRemark(String documentdate, String documentNo) {
        //data.Execute("UPDATE FELT_ORDER_MASTER,(SELECT SUBSTRING(WVOUT_PIECE_NO,1,5) AS PIECE,MAX(PROD_DATE) AS MAXDATE, GROUP_CONCAT(PROD_PIECE_NO,' ( ',PROD_DATE,' ) '  ORDER BY PROD_PIECE_NO SEPARATOR ', ') AS PIECEPROD FROM FELT_WVOUT_DATA WHERE PROD_DEPT = 'WEAVING' AND CANCELED =0 AND APPROVED =1 GROUP BY SUBSTRING(PROD_PIECE_NO,1,5)) AS A SET WVG_LAYER_REMARK = PIECEPROD,WVG_DATE=MAXDATE WHERE PIECE= ORDER_CODE  AND WVG_DATE >='"+documentdate+"'");
//        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER,(SELECT SUBSTRING(PROD_PIECE_NO,1,5) AS PIECE,MAX(PROD_DATE) AS MAXDATE,GROUP_CONCAT(PROD_PIECE_NO,' ( ',DATE_FORMAT(PROD_DATE,\"%d/%m/%Y\"),' ) '  ORDER BY PROD_PIECE_NO SEPARATOR ', ') AS PIECEPROD FROM PRODUCTION.FELT_WVOUT_DATA WHERE PROD_DEPT = 'WEAVING' AND PROD_DOC_NO='"+documentNo+"' AND CANCELED =0 AND APPROVED =1 GROUP BY SUBSTRING(PROD_PIECE_NO,1,5)) AS A SET WVG_LAYER_REMARK = PIECEPROD,PR_WVG_DATE=MAXDATE WHERE PIECE=PR_PIECE_NO  AND PR_WVG_DATE >='" + documentdate + "'");
//        data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER,(SELECT PROD_PIECE_NO AS PIECE,MAX(PROD_DATE) AS MAXDATE,GROUP_CONCAT(PROD_PIECE_NO,' ( ',DATE_FORMAT(PROD_DATE,\"%d/%m/%Y\"),' ) '  ORDER BY PROD_PIECE_NO SEPARATOR ', ') AS PIECEPROD FROM PRODUCTION.FELT_WVOUT_DATA WHERE PROD_DEPT = 'WEAVING' AND  PROD_DOC_NO='"+documentNo+"' AND CANCELED =0 AND APPROVED =1 GROUP BY SUBSTRING(PROD_PIECE_NO,1,5)) AS A SET WIP_WVG_LAYER_REMARK = PIECEPROD,WIP_WVG_DATE=MAXDATE WHERE LEFT(PIECE,5)=LEFT(WIP_EXT_PIECE_NO,5)  AND WIP_WVG_DATE >='" + documentdate + "'");
    }

    private void updateWeavingcounter(String documentNo) {
        data.Execute("UPDATE PRODUCTION.FELT_WVOUT_DATA  SET WEAVE_DIFF_DAYS =DATEDIFF(WVOUT_DATE,WEAVE_DATE)  WHERE PROD_DEPT='WEAVING'  AND WVOUT_DOC_NO='" + documentNo + "'");
    }

    // checks piece no already exist in db
    public boolean checkPieceNoInDB(String pPieceNo) {
        int count = data.getIntValueFromDB("SELECT COUNT(WVOUT_PIECE_NO) FROM PRODUCTION.`FELT_WVOUT_DATA` WHERE PROD_DEPT = 'WEAVING' AND WVOUT_PIECE_NO='" + pPieceNo + "' AND WVOUT_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND WVOUT_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'");
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPieceNoInDB(String pPieceNo, String pProdDate) {
        int count = data.getIntValueFromDB("SELECT COUNT(WVOUT_PIECE_NO) FROM PRODUCTION.`FELT_WVOUT_DATA` WHERE PROD_DEPT = 'WEAVING' AND WVOUT_PIECE_NO='" + pPieceNo + "' AND WVOUT_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND WVOUT_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'");
        if (count >= 1) {
            int counter = 0;
            try {
                Connection Conn = data.getConn();
                Statement stTmp = Conn.createStatement();
                ResultSet rsTmp = stTmp.executeQuery("SELECT WVOUT_DATE FROM PRODUCTION.`FELT_WVOUT_DATA` WHERE PROD_DEPT='WEAVING' AND WVOUT_PIECE_NO='" + pPieceNo + "' AND WVOUT_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND WVOUT_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'");
                while (rsTmp.next()) {
                    if (rsTmp.getString("WVOUT_DATE").equals(EITLERPGLOBAL.formatDateDB(pProdDate))) {
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
        int count = data.getIntValueFromDB("SELECT COUNT(WVOUT_DATE) FROM PRODUCTION.`FELT_WVOUT_DATA` WHERE PROD_DEPT = 'WEAVING' AND WVOUT_DATE='" + pProdDate + "'");
        if (count > 0) {
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

    // generates report data
    public TReportWriter.SimpleDataProvider.TTable getReportData(String prodDate) {
        TReportWriter.SimpleDataProvider.TTable objData = new TReportWriter.SimpleDataProvider.TTable();
        objData.AddColumn("SR_NO");
        objData.AddColumn("FORM_NO");
        objData.AddColumn("WVOUT_DATE");
        objData.AddColumn("DOC_NO");
        objData.AddColumn("PIECE_NO");
        objData.AddColumn("STYLE");
        objData.AddColumn("PARTY_NAME");
        objData.AddColumn("WEIGHT");
        objData.AddColumn("PICKS_PER_10CMS");
        objData.AddColumn("REED_SPACE");
        objData.AddColumn("LENGTH");
        objData.AddColumn("LOOM_NO");
        objData.AddColumn("LNGTH");
        objData.AddColumn("WIDTH");
        objData.AddColumn("GSQ");
        objData.AddColumn("GRUP");
        objData.AddColumn("DAY_WEIGHT");
        objData.AddColumn("PREVIOUS_WEIGHT");
        objData.AddColumn("BASE_WEIGHT");
        objData.AddColumn("CLOTH_WEIGHT");
        objData.AddColumn("SAMPLE_WEIGHT");
        objData.AddColumn("TOTAL_WEIGHT");

        try {
            TReportWriter.SimpleDataProvider.TRow objRow = objData.newRow();

            //  String str ="SELECT PROD_DATE,WVOUT_SHIFT,PROD_PIECE_NO,STYLE, WEIGHT,PICKS_PER_10CMS,REED_SPACE,LENGTH,LOOM_NO,GSQ,LNGTH,WIDTH,PARTY_NAME,PREVIOUS_WEIGHT,DAY_WEIGHT,TOTAL_WEIGHT,BASE_WEIGHT,CLOTH_WEIGHT,GRUP FROM (SELECT PROD_DATE,WVOUT_SHIFT,REPLACE(PROD_PIECE_NO,' ','') PROD_PIECE_NO,PROD_PARTY_CODE,WEIGHT,PICKS_PER_10CMS,REED_SPACE,LENGTH,LOOM_NO FROM PRODUCTION.FELT_WVOUT_DATA A WHERE PROD_DATE = '"+prodDate+"' AND PROD_DEPT='WEAVING') D LEFT JOIN (SELECT REPLACE(BALNK,' ','') AS STYLE,GRUP, GSQ,LNGTH,WIDTH,PRODUCT_CODE,PIECE_NO,PARTY_CD FROM PRODUCTION.FELT_ORDER_MASTER) O ON O.PARTY_CD = D.PROD_PARTY_CODE AND O.PIECE_NO +0 = D.PROD_PIECE_NO +0 LEFT JOIN (SELECT PARTY_CODE,TRIM(PARTY_NAME) PARTY_NAME FROM D_SAL_PARTY_MASTER) AS P ON P.PARTY_CODE = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS PREVIOUS_WEIGHT FROM PRODUCTION.`FELT_WVOUT_DATA` WHERE PROD_DEPT = 'WEAVING' AND MONTH(PROD_DATE)=MONTH('"+prodDate+"') AND YEAR(PROD_DATE)=YEAR('"+prodDate+"') AND PROD_DATE<'"+prodDate+"' AND APPROVED=1) AS W ON O.PARTY_CD = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS DAY_WEIGHT FROM PRODUCTION.`FELT_WVOUT_DATA` WHERE PROD_DEPT = 'WEAVING' AND MONTH(PROD_DATE)=MONTH('"+prodDate+"') AND YEAR(PROD_DATE)=YEAR('"+prodDate+"') AND PROD_DATE='"+prodDate+"' AND APPROVED=1) AS DW ON O.PARTY_CD = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS TOTAL_WEIGHT FROM PRODUCTION.`FELT_WVOUT_DATA` WHERE PROD_DEPT = 'WEAVING' AND MONTH(PROD_DATE)=MONTH('"+prodDate+"') AND YEAR(PROD_DATE)=YEAR('"+prodDate+"') AND PROD_DATE<='"+prodDate+"' AND APPROVED=1) AS T ON O.PARTY_CD = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS BASE_WEIGHT FROM (SELECT BALNK AS STYLE, A.WEIGHT FROM PRODUCTION.FELT_WVOUT_DATA A, PRODUCTION.FELT_ORDER_MASTER B WHERE PIECE_NO +0 = PROD_PIECE_NO +0 AND PROD_DATE <= '"+prodDate+"' AND PROD_DEPT='WEAVING' AND MONTH(PROD_DATE)=MONTH('"+prodDate+"') AND YEAR(PROD_DATE)=YEAR('"+prodDate+"') AND APPROVED=1) AS O WHERE SUBSTRING(O.STYLE,1,3) IN('XY','XYA','Y')) AS BW ON O.PARTY_CD = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS CLOTH_WEIGHT FROM  (SELECT BALNK AS STYLE, A.WEIGHT FROM PRODUCTION.FELT_WVOUT_DATA A, PRODUCTION.FELT_ORDER_MASTER B WHERE PIECE_NO +0 = PROD_PIECE_NO +0 AND PROD_DATE <= '"+prodDate+"' AND PROD_DEPT='WEAVING' AND MONTH(PROD_DATE)=MONTH('"+prodDate+"') AND YEAR(PROD_DATE)=YEAR('"+prodDate+"') AND APPROVED=1) AS O WHERE SUBSTRING(O.STYLE,1,3) NOT IN('XY','XYA','Y')) AS CW ON O.PARTY_CD = D.PROD_PARTY_CODE";
            // String str ="SELECT * FROM (SELECT PROD_DATE,WVOUT_SHIFT,PROD_DOC_NO,PROD_PIECE_NO,PR_STYLE,PROD_PARTY_CODE,A.WEIGHT,PICKS_PER_10CMS,REED_SPACE,A.LENGTH,LOOM_NO,GSQ,PR_LENGTH,B.WIDTH,PRODUCT_CODE,PARTY_CODE,C.PRIORITY_DESC FROM PRODUCTION.FELT_WVOUT_DATA A,PRODUCTION.FELT_SALES_PIECE_REGISTER  B,PRODUCTION.FELT_PRIORITY_MASTER C WHERE PR_PIECE_NO +0 = PROD_PIECE_NO +0 AND B.PRIORITY=C.PRIORITY_ID AND PROD_DATE = '"+prodDate+"' AND PROD_DEPT='WEAVING') AS O LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME FROM D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010) AS P ON P.PARTY_CODE = O.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS PREVIOUS_WEIGHT FROM PRODUCTION.`FELT_WVOUT_DATA` WHERE PROD_DEPT = 'WEAVING' AND MONTH(PROD_DATE)=MONTH('"+prodDate+"') AND YEAR(PROD_DATE)=YEAR('"+prodDate+"') AND PROD_DATE<'"+prodDate+"' AND APPROVED=1) AS W ON O.PARTY_CD = O.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS DAY_WEIGHT FROM PRODUCTION.`FELT_WVOUT_DATA` WHERE PROD_DEPT = 'WEAVING' AND MONTH(PROD_DATE)=MONTH('"+prodDate+"') AND YEAR(PROD_DATE)=YEAR('"+prodDate+"') AND PROD_DATE='"+prodDate+"' AND APPROVED=1) AS D ON O.PARTY_CODE = O.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS TOTAL_WEIGHT FROM PRODUCTION.`FELT_WVOUT_DATA` WHERE PROD_DEPT = 'WEAVING' AND MONTH(PROD_DATE)=MONTH('"+prodDate+"') AND YEAR(PROD_DATE)=YEAR('"+prodDate+"') AND PROD_DATE<='"+prodDate+"' AND APPROVED=1) AS T ON O.PARTY_CODE = O.PROD_PARTY_CODE LEFT JOIN (SELECT BASE_WEIGHT,CLOTH_WEIGHT FROM (SELECT SUM(COALESCE(CASE WHEN FABRIC_CATG ='BASE'  THEN WEIGHT END,0)) AS BASE_WEIGHT, SUM(COALESCE(CASE WHEN FABRIC_CATG ='CLOTH'  THEN WEIGHT END,0)) AS CLOTH_WEIGHT FROM (SELECT PRODUCT_CODE , A.WEIGHT FROM PRODUCTION.FELT_WVOUT_DATA A, PRODUCTION.FELT_ORDER_MASTER B WHERE PR_PIECE_NO +0 = PROD_PIECE_NO +0 AND PROD_DATE <= '"+prodDate+"' AND PROD_DEPT='WEAVING' AND MONTH(PROD_DATE)=MONTH('"+prodDate+"') AND YEAR(PROD_DATE)=YEAR('"+prodDate+"') AND APPROVED=1) AS O LEFT JOIN (SELECT ITEM_CODE,FABRIC_CATG FROM PRODUCTION.FELT_RATE_MASTER) AS R ON SUBSTRING(ITEM_CODE,1,6)=PRODUCT_CODE) AS O) AS CW ON O.PARTY_CODE = O.PROD_PARTY_CODE LEFT JOIN (SELECT ITEM_CODE,GRUP,FABRIC_CATG FROM PRODUCTION.FELT_RATE_MASTER) AS R ON SUBSTRING(ITEM_CODE,1,6)=PRODUCT_CODE";
            //String str ="SELECT * FROM (SELECT PROD_DATE,WVOUT_SHIFT,PROD_DOC_NO,PROD_PIECE_NO,PR_STYLE AS STYLE,PROD_PARTY_CODE,A.WEIGHT,PICKS_PER_10CMS,REED_SPACE,A.LENGTH,LOOM_NO,PR_GSM AS GSQ,PR_LENGTH AS LNGTH,B.PR_WIDTH AS WIDTH,PR_PRODUCT_CODE AS PRODUCT_CODE,PR_PARTY_CODE AS PARTY_CODE FROM PRODUCTION.FELT_WVOUT_DATA A,PRODUCTION.FELT_SALES_PIECE_REGISTER  B WHERE PR_PIECE_NO = PROD_PIECE_NO AND PROD_DATE = '"+prodDate+"' AND PROD_DEPT='WEAVING') AS O LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010) AS P ON P.PARTY_CODE = O.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS PREVIOUS_WEIGHT FROM PRODUCTION.FELT_WVOUT_DATA WHERE PROD_DEPT = 'WEAVING' AND MONTH(PROD_DATE)=MONTH('"+prodDate+"') AND YEAR(PROD_DATE)=YEAR('"+prodDate+"') AND PROD_DATE<'"+prodDate+"' AND APPROVED=1) AS W ON O.PARTY_CODE = O.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS DAY_WEIGHT FROM PRODUCTION.FELT_WVOUT_DATA WHERE PROD_DEPT = 'WEAVING' AND MONTH(PROD_DATE)=MONTH('"+prodDate+"') AND YEAR(PROD_DATE)=YEAR('"+prodDate+"') AND PROD_DATE='"+prodDate+"' AND APPROVED=1) AS D ON O.PARTY_CODE = O.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS TOTAL_WEIGHT FROM PRODUCTION.FELT_WVOUT_DATA WHERE PROD_DEPT = 'WEAVING' AND MONTH(PROD_DATE)=MONTH('"+prodDate+"') AND YEAR(PROD_DATE)=YEAR('"+prodDate+"') AND PROD_DATE<='"+prodDate+"' AND APPROVED=1) AS T ON O.PARTY_CODE = O.PROD_PARTY_CODE LEFT JOIN (SELECT BASE_WEIGHT,CLOTH_WEIGHT FROM (SELECT SUM(COALESCE(CASE WHEN FABRIC_CATG ='BASE'  THEN WEIGHT END,0)) AS BASE_WEIGHT,SUM(COALESCE(CASE WHEN FABRIC_CATG ='CLOTH'  THEN WEIGHT END,0)) AS CLOTH_WEIGHT FROM (SELECT PR_PRODUCT_CODE AS PRODUCT_CODE , A.WEIGHT FROM PRODUCTION.FELT_WVOUT_DATA A,PRODUCTION.FELT_SALES_PIECE_REGISTER B WHERE PR_PIECE_NO = PROD_PIECE_NO AND PROD_DATE <= '"+prodDate+"' AND PROD_DEPT='WEAVING' AND MONTH(PROD_DATE)=MONTH('"+prodDate+"') AND YEAR(PROD_DATE)=YEAR('"+prodDate+"') AND APPROVED=1) AS O LEFT JOIN (SELECT PRODUCT_CODE AS ITEM_CODE,CASE WHEN FABRIC_CATG=1 THEN 'BASE' ELSE CASE WHEN FABRIC_CATG=2 THEN 'CLOTH' ELSE '' END END AS FABRIC_CATG FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE APPROVED=1 AND (EFFECTIVE_TO='0000-00-00' OR EFFECTIVE_TO IS NULL)) AS R ON SUBSTRING(ITEM_CODE,1,6)=SUBSTRING(PRODUCT_CODE,1,6)) AS O) AS CW ON O.PARTY_CODE = O.PROD_PARTY_CODE LEFT JOIN (SELECT PRODUCT_CODE AS ITEM_CODE,GROUP_NAME AS GRUP,CASE WHEN FABRIC_CATG=1 THEN 'BASE' ELSE CASE WHEN FABRIC_CATG=2 THEN 'CLOTH' ELSE '' END END AS FABRIC_CATG FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE APPROVED=1 AND (EFFECTIVE_TO='0000-00-00' OR EFFECTIVE_TO IS NULL)) AS R ON SUBSTRING(ITEM_CODE,1,6)=SUBSTRING(PRODUCT_CODE,1,6)";
            String str = "SELECT * FROM (SELECT WVOUT_DATE,WVOUT_SHIFT,WVOUT_DOC_NO,WVOUT_PIECE_NO,PR_STYLE AS STYLE,WVOUT_PARTY_CODE,A.WEIGHT,PICKS_PER_10CMS,REED_SPACE,A.LENGTH,LOOM_NO,PR_GSM AS GSQ,PR_LENGTH AS LNGTH,B.PR_WIDTH AS WIDTH,PR_PRODUCT_CODE AS PRODUCT_CODE,PR_PARTY_CODE AS PARTY_CODE FROM PRODUCTION.FELT_WVOUT_DATA A,PRODUCTION.FELT_SALES_PIECE_REGISTER  B WHERE PR_PIECE_NO = SUBSTRING(PROD_PIECE_NO,1,5) AND WVOUT_DATE = '" + prodDate + "' AND PROD_DEPT='WEAVING') AS O LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010) AS P ON P.PARTY_CODE = O.WVOUT_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS PREVIOUS_WEIGHT FROM PRODUCTION.FELT_WVOUT_DATA WHERE PROD_DEPT = 'WEAVING' AND MONTH(WVOUT_DATE)=MONTH('" + prodDate + "') AND YEAR(WVOUT_DATE)=YEAR('" + prodDate + "') AND WVOUT_DATE<'" + prodDate + "' AND APPROVED=1) AS W ON O.PARTY_CODE = O.WVOUT_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS DAY_WEIGHT FROM PRODUCTION.FELT_WVOUT_DATA WHERE PROD_DEPT = 'WEAVING' AND MONTH(WVOUT_DATE)=MONTH('" + prodDate + "') AND YEAR(WVOUT_DATE)=YEAR('" + prodDate + "') AND WVOUT_DATE='" + prodDate + "' AND APPROVED=1) AS D ON O.PARTY_CODE = O.WVOUT_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS TOTAL_WEIGHT FROM PRODUCTION.FELT_WVOUT_DATA WHERE PROD_DEPT = 'WEAVING' AND MONTH(WVOUT_DATE)=MONTH('" + prodDate + "') AND YEAR(WVOUT_DATE)=YEAR('" + prodDate + "') AND WVOUT_DATE<='" + prodDate + "' AND APPROVED=1) AS T ON O.PARTY_CODE = O.WVOUT_PARTY_CODE LEFT JOIN (SELECT BASE_WEIGHT,CLOTH_WEIGHT FROM (SELECT SUM(COALESCE(CASE WHEN FABRIC_CATG ='BASE'  THEN WEIGHT END,0)) AS BASE_WEIGHT,SUM(COALESCE(CASE WHEN FABRIC_CATG ='CLOTH'  THEN WEIGHT END,0)) AS CLOTH_WEIGHT FROM (SELECT PR_PRODUCT_CODE AS PRODUCT_CODE , A.WEIGHT FROM PRODUCTION.FELT_WVOUT_DATA A,PRODUCTION.FELT_SALES_PIECE_REGISTER B WHERE PR_PIECE_NO = SUBSTRING(PROD_PIECE_NO,1,5) AND WVOUT_DATE <= '" + prodDate + "' AND PROD_DEPT='WEAVING' AND MONTH(WVOUT_DATE)=MONTH('" + prodDate + "') AND YEAR(WVOUT_DATE)=YEAR('" + prodDate + "') AND APPROVED=1) AS O LEFT JOIN (SELECT PRODUCT_CODE AS ITEM_CODE,CASE WHEN FABRIC_CATG=1 THEN 'BASE' ELSE CASE WHEN FABRIC_CATG=2 THEN 'CLOTH' ELSE '' END END AS FABRIC_CATG FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE APPROVED=1 AND (EFFECTIVE_TO='0000-00-00' OR EFFECTIVE_TO IS NULL)) AS R ON SUBSTRING(ITEM_CODE,1,6)=SUBSTRING(PRODUCT_CODE,1,6)) AS O) AS CW ON O.PARTY_CODE = O.WVOUT_PARTY_CODE LEFT JOIN (SELECT PRODUCT_CODE AS ITEM_CODE,GROUP_NAME AS GRUP,CASE WHEN FABRIC_CATG=1 THEN 'BASE' ELSE CASE WHEN FABRIC_CATG=2 THEN 'CLOTH' ELSE '' END END AS FABRIC_CATG FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE APPROVED=1 AND (EFFECTIVE_TO='0000-00-00' OR EFFECTIVE_TO IS NULL)) AS R ON SUBSTRING(ITEM_CODE,1,6)=SUBSTRING(PRODUCT_CODE,1,6)";
            
            System.out.println(str);
            ResultSet rsTemp = data.getResult(str);
            int counter = 1;
            while (!rsTemp.isAfterLast()) {
                objRow = objData.newRow();
                objRow.setValue("SR_NO", String.valueOf(counter));
                objRow.setValue("FORM_NO", rsTemp.getString("WVOUT_SHIFT"));
                objRow.setValue("WVOUT_DATE", EITLERPGLOBAL.formatDate(rsTemp.getString("WVOUT_DATE")));
                objRow.setValue("PIECE_NO", rsTemp.getString("WVOUT_PIECE_NO"));
                objRow.setValue("STYLE", rsTemp.getString("STYLE"));
                objRow.setValue("PARTY_NAME", rsTemp.getString("PARTY_NAME"));
                objRow.setValue("WEIGHT", rsTemp.getString("WEIGHT"));
                objRow.setValue("PICKS_PER_10CMS", rsTemp.getString("PICKS_PER_10CMS"));
                objRow.setValue("REED_SPACE", rsTemp.getString("REED_SPACE"));
                objRow.setValue("LENGTH", rsTemp.getString("LENGTH"));
                objRow.setValue("LOOM_NO", rsTemp.getString("LOOM_NO"));
                objRow.setValue("LNGTH", rsTemp.getString("LNGTH"));
                objRow.setValue("WIDTH", rsTemp.getString("WIDTH"));
                objRow.setValue("GSQ", rsTemp.getString("GSQ"));
                objRow.setValue("GRUP", rsTemp.getString("GRUP"));
                objRow.setValue("DAY_WEIGHT", rsTemp.getString("DAY_WEIGHT"));
                objRow.setValue("PREVIOUS_WEIGHT", rsTemp.getString("PREVIOUS_WEIGHT"));
                objRow.setValue("BASE_WEIGHT", rsTemp.getString("BASE_WEIGHT"));
                objRow.setValue("CLOTH_WEIGHT", rsTemp.getString("CLOTH_WEIGHT"));
                objRow.setValue("SAMPLE_WEIGHT", String.valueOf(EITLERPGLOBAL.round(rsTemp.getDouble("TOTAL_WEIGHT") - (rsTemp.getDouble("BASE_WEIGHT") + rsTemp.getDouble("CLOTH_WEIGHT")), 2)));
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
    
    public boolean checkProductionDocumentNoInDB(String pProdDocNo){
        int count=data.getIntValueFromDB("SELECT COUNT(WOUT_DOC_NO) FROM PRODUCTION.FELT_WVOUT_DATA WHERE WOUT_DOC_NO='"+pProdDocNo+"'");
        if(count>0) return true;
        else return false;
    }

}
