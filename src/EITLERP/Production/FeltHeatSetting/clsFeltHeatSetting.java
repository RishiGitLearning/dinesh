/*
 * clsFeltHeatSetting.java
 *
 * Created on April 15, 2013, 5:23 PM
 */
package EITLERP.Production.FeltHeatSetting;

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
import EITLERP.Production.Production_Date_Updation;
import EITLERP.Production.clsFeltProductionApprovalFlow;
import javax.swing.JOptionPane;

/**
 *
 * @author Vivek Kumar
 * @version
 */
public class clsFeltHeatSetting {

    public String LastError = "";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public HashMap props;
    public boolean Ready = false;
    //Felt Production HeatSetting Details Collection
    public HashMap hmFeltHeatSettingDetails;

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
     * Creates new DataFeltProductionHeatSetting
     */
    public clsFeltHeatSetting() {
        LastError = "";
        props = new HashMap();
        props.put("PRODUCTION_DATE", new Variant(""));
        props.put("PRODUCTION_DOCUMENT_NO", new Variant(""));
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
        props.put("PRODUCTION_SHIFT_NO", new Variant(""));
        
        hmFeltHeatSettingDetails = new HashMap();
    }

    public boolean LoadData() {
        Ready = false;
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            //resultSet = statement.executeQuery("SELECT DISTINCT PROD_DATE FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'HEAT_SETTING' AND PROD_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND PROD_DATE<='" + EITLERPGLOBAL.FinToDateDB + "' ORDER BY PROD_DATE");
            resultSet = statement.executeQuery("SELECT DISTINCT PROD_DOC_NO FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'HEAT_SETTING' AND PROD_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND PROD_DATE<='" + EITLERPGLOBAL.FinToDateDB + "' ORDER BY PROD_DATE,SHIFT_ID");
            HistoryView = false;
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
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DATE=''");

            // Production data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.`FELT_PROD_DATA_H` WHERE PROD_DATE=''");

            //Now Insert records into production tables
            for (int i = 1; i <= hmFeltHeatSettingDetails.size(); i++) {
                clsFeltHeatSettingDetails ObjFeltHeatSettingDetails = (clsFeltHeatSettingDetails) hmFeltHeatSettingDetails.get(Integer.toString(i));

                //Insert records into production data table
                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateString("PROD_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()));
                resultSetTemp.updateString("PROD_DOC_NO", getAttribute("PRODUCTION_DOCUMENT_NO").getString());
                resultSetTemp.updateString("PROD_FORM_NO", getAttribute("PRODUCTION_FORM_NO").getString());
                resultSetTemp.updateString("PROD_DEPT", "HEAT_SETTING");
                resultSetTemp.updateInt("SR_NO", i);
                resultSetTemp.updateString("PROD_PIECE_NO", ObjFeltHeatSettingDetails.getAttribute("PRODUCTION_PIECE_NO").getString());
                resultSetTemp.updateString("PROD_PARTY_CODE", ObjFeltHeatSettingDetails.getAttribute("PRODUCTION_PARTY_CODE").getString());
                resultSetTemp.updateFloat("WEIGHT", (float) EITLERPGLOBAL.round(ObjFeltHeatSettingDetails.getAttribute("WEIGHT").getVal(), 2));
                resultSetTemp.updateString("REMARKS", ObjFeltHeatSettingDetails.getAttribute("REMARKS").getString());
                resultSetTemp.updateString("DRYER", ObjFeltHeatSettingDetails.getAttribute("DRYER").getString());
                resultSetTemp.updateString("SHIFT_ID", ObjFeltHeatSettingDetails.getAttribute("SHIFT_ID").getString());
                
                
                if (ObjFeltHeatSettingDetails.getAttribute("HEAT_SETTING_OUT_DATE").getString().equals("")) {
                    resultSetTemp.updateString("HEAT_SETTING_OUT_DATE", "");
                } else {
                    resultSetTemp.updateString("HEAT_SETTING_OUT_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltHeatSettingDetails.getAttribute("HEAT_SETTING_OUT_DATE").getString()));
                }
                
                if (ObjFeltHeatSettingDetails.getAttribute("HEAT_SETTING_IN_DATE").getString().equals("")) {
                    resultSetTemp.updateString("HEAT_SETTING_IN_DATE", "");
                } else {
                    resultSetTemp.updateString("HEAT_SETTING_IN_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltHeatSettingDetails.getAttribute("HEAT_SETTING_IN_DATE").getString()));
                }
                
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
                resultSetHistory.updateString("PROD_FORM_NO", getAttribute("PRODUCTION_FORM_NO").getString());
                resultSetHistory.updateString("PROD_DEPT", "HEAT_SETTING");
                resultSetHistory.updateInt("SR_NO", i);
                resultSetHistory.updateString("PROD_PIECE_NO", ObjFeltHeatSettingDetails.getAttribute("PRODUCTION_PIECE_NO").getString());
                resultSetHistory.updateString("PROD_PARTY_CODE", ObjFeltHeatSettingDetails.getAttribute("PRODUCTION_PARTY_CODE").getString());
                resultSetHistory.updateFloat("WEIGHT", (float) EITLERPGLOBAL.round(ObjFeltHeatSettingDetails.getAttribute("WEIGHT").getVal(), 2));
                resultSetHistory.updateString("REMARKS", ObjFeltHeatSettingDetails.getAttribute("REMARKS").getString());
                resultSetHistory.updateString("DRYER", ObjFeltHeatSettingDetails.getAttribute("DRYER").getString());
                resultSetHistory.updateString("SHIFT_ID", ObjFeltHeatSettingDetails.getAttribute("SHIFT_ID").getString());
                
                
                if (ObjFeltHeatSettingDetails.getAttribute("HEAT_SETTING_OUT_DATE").getString().equals("")) {
                    resultSetHistory.updateString("HEAT_SETTING_OUT_DATE", "");
                } else {
                    resultSetHistory.updateString("HEAT_SETTING_OUT_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltHeatSettingDetails.getAttribute("HEAT_SETTING_OUT_DATE").getString()));
                }
                
                if (ObjFeltHeatSettingDetails.getAttribute("HEAT_SETTING_IN_DATE").getString().equals("")) {
                    resultSetHistory.updateString("HEAT_SETTING_IN_DATE", "");
                } else {
                    resultSetHistory.updateString("HEAT_SETTING_IN_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltHeatSettingDetails.getAttribute("HEAT_SETTING_IN_DATE").getString()));
                }
                
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
            ObjFeltProductionApprovalFlow.ModuleID = 704; //Felt HeatSetting Entry
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("PRODUCTION_DOCUMENT_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName = "FELT_PROD_DATA";
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

            // Update HeatSetting Date in Order Master Table To confirm that HeatSetting has completed
            if (ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {
                ObjFeltProductionApprovalFlow.finalApproved = false;
                updateHeatSettingDate(getAttribute("PRODUCTION_DOCUMENT_NO").getString());
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
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DATE=''");

            // Production data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //Get the Maximum Revision No in History Table.
            resultSetHistory = statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_PROD_DATA_H WHERE PROD_DEPT = 'HEAT_SETTING' AND PROD_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()) + "' AND PROD_DOC_NO='" + getAttribute("PRODUCTION_DOCUMENT_NO").getString() + "'");
            resultSetHistory.first();
            if (resultSetHistory.getRow() > 0) {
                revisionNo = resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from production data table before insert
            statementHistory.execute("DELETE FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()) + "' AND PROD_DOC_NO='" + getAttribute("PRODUCTION_DOCUMENT_NO").getString() + "'");

            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.`FELT_PROD_DATA_H` WHERE PROD_DATE=''");

            //Now Update records into production tables
            for (int i = 1; i <= hmFeltHeatSettingDetails.size(); i++) {
                clsFeltHeatSettingDetails ObjFeltHeatSettingDetails = (clsFeltHeatSettingDetails) hmFeltHeatSettingDetails.get(Integer.toString(i));

                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateString("PROD_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()));
                resultSetTemp.updateString("PROD_DOC_NO", getAttribute("PRODUCTION_DOCUMENT_NO").getString());
                resultSetTemp.updateString("PROD_FORM_NO", getAttribute("PRODUCTION_FORM_NO").getString());
                resultSetTemp.updateString("PROD_DEPT", "HEAT_SETTING");
                resultSetTemp.updateInt("SR_NO", i);
                resultSetTemp.updateString("PROD_PIECE_NO", ObjFeltHeatSettingDetails.getAttribute("PRODUCTION_PIECE_NO").getString());
                resultSetTemp.updateString("PROD_PARTY_CODE", ObjFeltHeatSettingDetails.getAttribute("PRODUCTION_PARTY_CODE").getString());
                resultSetTemp.updateFloat("WEIGHT", (float) EITLERPGLOBAL.round(ObjFeltHeatSettingDetails.getAttribute("WEIGHT").getVal(), 2));
                resultSetTemp.updateString("REMARKS", ObjFeltHeatSettingDetails.getAttribute("REMARKS").getString());
                resultSetTemp.updateString("DRYER", ObjFeltHeatSettingDetails.getAttribute("DRYER").getString());
                resultSetTemp.updateString("SHIFT_ID", ObjFeltHeatSettingDetails.getAttribute("SHIFT_ID").getString());
                
                
                if (ObjFeltHeatSettingDetails.getAttribute("HEAT_SETTING_OUT_DATE").getString().equals("")) {
                    resultSetTemp.updateString("HEAT_SETTING_OUT_DATE", "");
                } else {
                    resultSetTemp.updateString("HEAT_SETTING_OUT_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltHeatSettingDetails.getAttribute("HEAT_SETTING_OUT_DATE").getString()));
                }
                
                if (ObjFeltHeatSettingDetails.getAttribute("HEAT_SETTING_IN_DATE").getString().equals("")) {
                    resultSetTemp.updateString("HEAT_SETTING_IN_DATE", "");
                } else {
                    resultSetTemp.updateString("HEAT_SETTING_IN_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltHeatSettingDetails.getAttribute("HEAT_SETTING_IN_DATE").getString()));
                }
                
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
                resultSetHistory.updateString("PROD_FORM_NO", getAttribute("PRODUCTION_FORM_NO").getString());
                resultSetHistory.updateString("PROD_DEPT", "HEAT_SETTING");
                resultSetHistory.updateInt("SR_NO", i);
                resultSetHistory.updateString("PROD_PIECE_NO", ObjFeltHeatSettingDetails.getAttribute("PRODUCTION_PIECE_NO").getString());
                resultSetHistory.updateString("PROD_PARTY_CODE", ObjFeltHeatSettingDetails.getAttribute("PRODUCTION_PARTY_CODE").getString());
                resultSetHistory.updateFloat("WEIGHT", (float) EITLERPGLOBAL.round(ObjFeltHeatSettingDetails.getAttribute("WEIGHT").getVal(), 2));
                resultSetHistory.updateString("REMARKS", ObjFeltHeatSettingDetails.getAttribute("REMARKS").getString());
                resultSetHistory.updateString("DRYER", ObjFeltHeatSettingDetails.getAttribute("DRYER").getString());
                resultSetHistory.updateString("SHIFT_ID", ObjFeltHeatSettingDetails.getAttribute("SHIFT_ID").getString());
                
                
                if (ObjFeltHeatSettingDetails.getAttribute("HEAT_SETTING_OUT_DATE").getString().equals("")) {
                    resultSetHistory.updateString("HEAT_SETTING_OUT_DATE", "");
                } else {
                    resultSetHistory.updateString("HEAT_SETTING_OUT_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltHeatSettingDetails.getAttribute("HEAT_SETTING_OUT_DATE").getString()));
                }
                
                if (ObjFeltHeatSettingDetails.getAttribute("HEAT_SETTING_IN_DATE").getString().equals("")) {
                    resultSetHistory.updateString("HEAT_SETTING_IN_DATE", "");
                } else {
                    resultSetHistory.updateString("HEAT_SETTING_IN_DATE", EITLERPGLOBAL.formatDateDB(ObjFeltHeatSettingDetails.getAttribute("HEAT_SETTING_IN_DATE").getString()));
                }
                
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
                        if (ObjFeltHeatSettingDetails.getAttribute("PRODUCTION_PIECE_NO").getString().contains("A") && ObjFeltHeatSettingDetails.getAttribute("PRODUCTION_PIECE_NO").getString().contains("B")) {
                            
                            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_HEATSETTING_WEIGHT='" + EITLERPGLOBAL.round(ObjFeltHeatSettingDetails.getAttribute("WEIGHT").getVal(), 2) + "' WHERE WIP_EXT_PIECE_NO='" + ObjFeltHeatSettingDetails.getAttribute("PRODUCTION_PIECE_NO").getString() + "'");
                            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER R,PRODUCTION.FELT_WIP_PIECE_REGISTER W  "
                                    + "SET PR_HEATSETTING_WEIGHT='" + EITLERPGLOBAL.round(ObjFeltHeatSettingDetails.getAttribute("WEIGHT").getVal(), 2) + "' "
                                    + "WHERE PR_PIECE_NO=WIP_PIECE_NO AND WIP_EXT_PIECE_NO='"+ ObjFeltHeatSettingDetails.getAttribute("PRODUCTION_PIECE_NO").getString()+"'");
                            
                            
                        } else if (ObjFeltHeatSettingDetails.getAttribute("PRODUCTION_PIECE_NO").getString().contains("A")) {
                            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_HEATSETTING_WEIGHT='" + EITLERPGLOBAL.round(ObjFeltHeatSettingDetails.getAttribute("WEIGHT").getVal(), 2) + "',PR_HEATSETTING_WEIGHT_A='" + EITLERPGLOBAL.round(ObjFeltHeatSettingDetails.getAttribute("WEIGHT").getVal(), 2) + "' WHERE PR_PIECE_NO+0=" + ObjFeltHeatSettingDetails.getAttribute("PRODUCTION_PIECE_NO").getString() + "+0");
                            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_HEATSETTING_WEIGHT='" + EITLERPGLOBAL.round(ObjFeltHeatSettingDetails.getAttribute("WEIGHT").getVal(), 2) + "',WIP_HEATSETTING_WEIGHT_A='" + EITLERPGLOBAL.round(ObjFeltHeatSettingDetails.getAttribute("WEIGHT").getVal(), 2) + "' WHERE WIP_EXT_PIECE_NO='" + ObjFeltHeatSettingDetails.getAttribute("PRODUCTION_PIECE_NO").getString() + "'");
                            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_HEATSETTING_WEIGHT=PR_HEATSETTING_WEIGHT_A+PR_HEATSETTING_WEIGHT_B WHERE PR_PIECE_NO+0=" + ObjFeltHeatSettingDetails.getAttribute("PRODUCTION_PIECE_NO").getString() + "+0");
                        } else if (ObjFeltHeatSettingDetails.getAttribute("PRODUCTION_PIECE_NO").getString().contains("B")) {
                            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_HEATSETTING_WEIGHT='" + EITLERPGLOBAL.round(ObjFeltHeatSettingDetails.getAttribute("WEIGHT").getVal(), 2) + "',PR_HEATSETTING_WEIGHT_B='" + EITLERPGLOBAL.round(ObjFeltHeatSettingDetails.getAttribute("WEIGHT").getVal(), 2) + "' WHERE PR_PIECE_NO+0=" + ObjFeltHeatSettingDetails.getAttribute("PRODUCTION_PIECE_NO").getString() + "+0");
                            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_HEATSETTING_WEIGHT='" + EITLERPGLOBAL.round(ObjFeltHeatSettingDetails.getAttribute("WEIGHT").getVal(), 2) + "',WIP_HEATSETTING_WEIGHT_B='" + EITLERPGLOBAL.round(ObjFeltHeatSettingDetails.getAttribute("WEIGHT").getVal(), 2) + "' WHERE WIP_EXT_PIECE_NO='" + ObjFeltHeatSettingDetails.getAttribute("PRODUCTION_PIECE_NO").getString() + "'");
                            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_HEATSETTING_WEIGHT=PR_HEATSETTING_WEIGHT_A+PR_HEATSETTING_WEIGHT_B WHERE PR_PIECE_NO+0=" + ObjFeltHeatSettingDetails.getAttribute("PRODUCTION_PIECE_NO").getString() + "+0");
                        } else {
                            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_HEATSETTING_WEIGHT='" + EITLERPGLOBAL.round(ObjFeltHeatSettingDetails.getAttribute("WEIGHT").getVal(), 2) + "' WHERE PR_PIECE_NO+0=" + ObjFeltHeatSettingDetails.getAttribute("PRODUCTION_PIECE_NO").getString() + "+0");
                            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_HEATSETTING_WEIGHT='" + EITLERPGLOBAL.round(ObjFeltHeatSettingDetails.getAttribute("WEIGHT").getVal(), 2) + "',WIP_HEATSETTING_WEIGHT_A='" + EITLERPGLOBAL.round(ObjFeltHeatSettingDetails.getAttribute("WEIGHT").getVal(), 2) + "' WHERE WIP_EXT_PIECE_NO='" + ObjFeltHeatSettingDetails.getAttribute("PRODUCTION_PIECE_NO").getString() + "'");
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = 704; //Felt Heat Setting Entry
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
                data.Execute("UPDATE PRODUCTION.`FELT_PROD_DATA` SET REJECTED=0,CHANGED=1 WHERE PROD_DEPT='HEAT_SETTING' AND PROD_DOC_NO='" + getAttribute("PRODUCTION_DOCUMENT_NO").getString() + "'");
                //Remove Old Records from FELT DOCUMENT APPROVAL TABLE
                //data.Execute("DELETE FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=704 AND DOC_NO='"+getAttribute("PRODUCTION_DOCUMENT_NO").getString()+"'");

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

            // Update HeatSetting Date in Order Master Table To confirm that HeatSetting has completed
            if (ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {
                ObjFeltProductionApprovalFlow.finalApproved = false;
                updateHeatSettingDate(getAttribute("PRODUCTION_DOCUMENT_NO").getString());
                //Production_Date_Updation pd=new Production_Date_Updation();
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'HEAT_SETTING' AND PROD_DOC_NO='" + documentNo + "' AND APPROVED=" + 1;
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=704 AND USER_ID=" + userID + " AND DOC_NO='" + documentNo + "' AND STATUS='W'";
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();

                if (rsTmp.getInt("COUNT") > 0) {
                    strSQL = "DELETE FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'HEAT_SETTING'"
                            + " AND PROD_DATE= '" + EITLERPGLOBAL.formatDateDB(stringProductionDate) + "'"
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'HEAT_SETTING' AND PROD_DOC_NO='" + weavingDocumentNo + "' AND APPROVED=1";
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=704 AND USER_ID=" + Integer.toString(userID) + " AND DOC_NO='" + weavingDocumentNo + "' AND STATUS='W'";
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
            //String strSql = "SELECT DISTINCT PROD_DATE FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'HEAT_SETTING' " + stringFindQuery + "ORDER BY PROD_DATE";
            String strSql = "SELECT DISTINCT PROD_DOC_NO FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'HEAT_SETTING' " + stringFindQuery + " ORDER BY PROD_DATE,SHIFT_ID";
            System.out.println(strSql);
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
            //setAttribute("PRODUCTION_DATE", resultSet.getString("PROD_DATE"));
            //totalWeight = data.getDoubleValueFromDB("SELECT SUM(WEIGHT) FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'HEAT_SETTING' AND MONTH(PROD_DATE)=MONTH('" + resultSet.getString("PROD_DATE") + "') AND YEAR(PROD_DATE)=YEAR('" + resultSet.getString("PROD_DATE") + "') AND APPROVED=1");          
            
            //setAttribute("TOTAL_WEIGHT", totalWeight);

            //previousWeight = data.getDoubleValueFromDB("SELECT SUM(WEIGHT) FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'HEAT_SETTING' AND MONTH(PROD_DATE)=MONTH('" + resultSet.getString("PROD_DATE") + "') AND YEAR(PROD_DATE)=YEAR('" + resultSet.getString("PROD_DATE") + "') AND PROD_DATE<'" + resultSet.getString("PROD_DATE") + "' AND APPROVED=1");
            //setAttribute("PREVIOUS_WEIGHT", previousWeight);

            //Now Populate the collection, first clear the collection
            hmFeltHeatSettingDetails.clear();

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            //resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'HEAT_SETTING'  AND PROD_DATE='" + resultSet.getString("PROD_DATE") + "' ORDER BY SR_NO");
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'HEAT_SETTING'  AND PROD_DOC_NO='" + resultSet.getString("PROD_DOC_NO") + "' ORDER BY SR_NO");
            
            while (resultSetTemp.next()) {
                serialNoCounter++;
                setAttribute("PRODUCTION_DATE", resultSetTemp.getString("PROD_DATE"));
                totalWeight = data.getDoubleValueFromDB("SELECT SUM(WEIGHT) FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'HEAT_SETTING' AND MONTH(PROD_DATE)=MONTH('" + resultSetTemp.getString("PROD_DATE") + "') AND YEAR(PROD_DATE)=YEAR('" + resultSetTemp.getString("PROD_DATE") + "') AND APPROVED=1");                      
                previousWeight = data.getDoubleValueFromDB("SELECT SUM(WEIGHT) FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'HEAT_SETTING' AND MONTH(PROD_DATE)=MONTH('" + resultSetTemp.getString("PROD_DATE") + "') AND YEAR(PROD_DATE)=YEAR('" + resultSetTemp.getString("PROD_DATE") + "') AND PROD_DATE<'" + resultSetTemp.getString("PROD_DATE") + "' AND APPROVED=1");
                setAttribute("PREVIOUS_WEIGHT", previousWeight);
                setAttribute("TOTAL_WEIGHT", totalWeight);
                setAttribute("PRODUCTION_DOCUMENT_NO", resultSetTemp.getString("PROD_DOC_NO"));
                setAttribute("PRODUCTION_FORM_NO", resultSetTemp.getString("PROD_FORM_NO"));
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
                setAttribute("PRODUCTION_SHIFT_NO", resultSetTemp.getString("SHIFT_ID"));

                clsFeltHeatSettingDetails ObjFeltHeatSettingDetails = new clsFeltHeatSettingDetails();

                ObjFeltHeatSettingDetails.setAttribute("PRODUCTION_PIECE_NO", resultSetTemp.getString("PROD_PIECE_NO"));
                ObjFeltHeatSettingDetails.setAttribute("PRODUCTION_PARTY_CODE", resultSetTemp.getString("PROD_PARTY_CODE"));
                ObjFeltHeatSettingDetails.setAttribute("WEIGHT", resultSetTemp.getFloat("WEIGHT"));
                ObjFeltHeatSettingDetails.setAttribute("REMARKS", resultSetTemp.getString("REMARKS"));
                ObjFeltHeatSettingDetails.setAttribute("SHIFT_ID", resultSetTemp.getString("SHIFT_ID"));
                
                ObjFeltHeatSettingDetails.setAttribute("DRYER", resultSetTemp.getString("DRYER"));
                
                
                ObjFeltHeatSettingDetails.setAttribute("HEAT_SETTING_OUT_DATE", resultSetTemp.getString("HEAT_SETTING_OUT_DATE"));
                ObjFeltHeatSettingDetails.setAttribute("HEAT_SETTING_IN_DATE", resultSetTemp.getString("HEAT_SETTING_IN_DATE"));

                hmFeltHeatSettingDetails.put(Integer.toString(serialNoCounter), ObjFeltHeatSettingDetails);
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
            hmFeltHeatSettingDetails.clear();

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.`FELT_PROD_DATA_H` WHERE PROD_DEPT = 'HEAT_SETTING' AND PROD_DATE='" + pProductionDate + "' AND PROD_DOC_NO='" + pDocNo + "' AND REVISION_NO='" + resultSet.getInt("REVISION_NO") + "' ORDER BY REVISION_NO");

            while (resultSetTemp.next()) {
                serialNoCounter++;
                setAttribute("PRODUCTION_DOCUMENT_NO", resultSetTemp.getString("PROD_DOC_NO"));
                setAttribute("PRODUCTION_FORM_NO", resultSetTemp.getString("PROD_FORM_NO"));
                setAttribute("UPDATED_BY", resultSetTemp.getInt("UPDATED_BY"));
                setAttribute("ENTRY_DATE", resultSetTemp.getString("ENTRY_DATE"));
                setAttribute("HIERARCHY_ID", resultSetTemp.getInt("HIERARCHY_ID"));

                clsFeltHeatSettingDetails ObjFeltHeatSettingDetails = new clsFeltHeatSettingDetails();

                ObjFeltHeatSettingDetails.setAttribute("PRODUCTION_PIECE_NO", resultSetTemp.getString("PROD_PIECE_NO"));
                ObjFeltHeatSettingDetails.setAttribute("PRODUCTION_PARTY_CODE", resultSetTemp.getString("PROD_PARTY_CODE"));
                ObjFeltHeatSettingDetails.setAttribute("WEIGHT", resultSetTemp.getFloat("WEIGHT"));
                ObjFeltHeatSettingDetails.setAttribute("REMARKS", resultSetTemp.getString("REMARKS"));
                ObjFeltHeatSettingDetails.setAttribute("SHIFT_ID", resultSetTemp.getString("SHIFT_ID"));                
                ObjFeltHeatSettingDetails.setAttribute("DRYER", resultSetTemp.getString("DRYER"));
                ObjFeltHeatSettingDetails.setAttribute("HEAT_SETTING_OUT_DATE", resultSetTemp.getString("HEAT_SETTING_OUT_DATE"));
                ObjFeltHeatSettingDetails.setAttribute("HEAT_SETTING_IN_DATE", resultSetTemp.getString("HEAT_SETTING_IN_DATE"));

                hmFeltHeatSettingDetails.put(Integer.toString(serialNoCounter), ObjFeltHeatSettingDetails);
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
            rsTmp = stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_PROD_DATA_H WHERE PROD_DEPT = 'HEAT_SETTING' AND PROD_DATE='" + stringProductionDate + "' AND PROD_DOC_NO='" + productionDocumentNo + "' GROUP BY REVISION_NO");

            while (rsTmp.next()) {
                clsFeltHeatSetting ObjFeltHeatSetting = new clsFeltHeatSetting();

                ObjFeltHeatSetting.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                ObjFeltHeatSetting.setAttribute("UPDATED_BY", rsTmp.getInt("UPDATED_BY"));
                ObjFeltHeatSetting.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                ObjFeltHeatSetting.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                ObjFeltHeatSetting.setAttribute("APPROVER_REMARKS", rsTmp.getString("APPROVER_REMARKS"));
                ObjFeltHeatSetting.setAttribute("FROM_IP", rsTmp.getString("FROM_IP"));

                hmHistoryList.put(Integer.toString(hmHistoryList.size() + 1), ObjFeltHeatSetting);
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
            resultSet = statement.executeQuery("SELECT DISTINCT REVISION_NO FROM PRODUCTION.FELT_PROD_DATA_H WHERE PROD_DEPT='HEAT_SETTING' AND PROD_DATE='" + pProductionDate + "' AND PROD_DOC_NO='" + pDocNo + "'");
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
                strSQL = "SELECT DISTINCT PROD_DOC_NO,PROD_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_PROD_DATA,PRODUCTION.FELT_PROD_DOC_DATA WHERE PROD_DOC_NO=DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=704 AND CANCELED=0 ORDER BY RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT PROD_DOC_NO,PROD_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_PROD_DATA,PRODUCTION.FELT_PROD_DOC_DATA WHERE PROD_DOC_NO=DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=704 AND CANCELED=0 ORDER BY PROD_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT PROD_DOC_NO,PROD_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_PROD_DATA,PRODUCTION.FELT_PROD_DOC_DATA WHERE PROD_DOC_NO=DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=704 AND CANCELED=0 ORDER BY PROD_DOC_NO";
            }

            ResultSet rsTmp = data.getConn().createStatement().executeQuery(strSQL);

            Counter = 0;
            while (rsTmp.next()) {

                Counter = Counter + 1;
                clsFeltHeatSetting ObjDoc = new clsFeltHeatSetting();

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
        //return data.getStringValueFromDB("SELECT PARTY_CD FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");        

        if (pPieceNo.contains("P") || pPieceNo.contains("V")) {
            pPieceNo = pPieceNo.substring(0, 6);
        } else {
            pPieceNo = pPieceNo.substring(0, 5);
        }

        String piece_stage = data.getStringValueFromDB("SELECT PR_PIECE_STAGE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + pPieceNo + "'");
        if (piece_stage.equals("DIVERTED")) {
            JOptionPane.showMessageDialog(null, "This Piece is Diverted, Please use new Piece Number with P or V");
            return "";
        }

        System.out.println("GetPartyName HeatSetting: SELECT PR_PARTY_CODE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + pPieceNo + "'");
        return data.getStringValueFromDB("SELECT PR_PARTY_CODE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + pPieceNo + "'");

    }

    // Updates Heatsetting Date in Order Master Table To confirm that HeatSetting has completed
    private void updateHeatSettingDate(String documentNo) {
        //String strQry="UPDATE PRODUCTION.FELT_ORDER_MASTER,PRODUCTION.FELT_PROD_DATA SET NDL_DATE=PROD_DATE WHERE PIECE_NO+0=PROD_PIECE_NO+0 AND PARTY_CD=PROD_PARTY_CODE AND PROD_DEPT='HEAT_SETTING' AND PROD_DOC_NO='"+documentNo+"' AND APPROVED=1";
        /*
        data.Execute("DROP TABLE PRODUCTION.TMP_UPDATE_FLAG_NEEDLING");
        data.Execute("CREATE TABLE PRODUCTION.TMP_UPDATE_FLAG_NEEDLING "
                + "SELECT WIP_PIECE_NO FROM PRODUCTION.FELT_WIP_PIECE_REGISTER "
                + "WHERE WIP_PIECE_NO IN "
                + "(SELECT DISTINCT LEFT(PROD_PIECE_NO,5) FROM PRODUCTION.FELT_PROD_DATA "
                + "WHERE PROD_DEPT='HEAT_SETTING' AND PROD_DOC_NO='" + documentNo + "' AND APPROVED=1 AND COALESCE(CANCELED,0)=0)  AND COALESCE(WIP_REJECTED_FLAG,0)=0 AND WIP_PRIORITY_HOLD_CAN_FLAG='3' ");
        data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER "
                + "SET WIP_PIECE_STAGE='MERGED',WIP_STATUS='MERGED',WIP_PRIORITY_HOLD_CAN_FLAG=5 "
                + "WHERE WIP_PIECE_NO IN (SELECT WIP_PIECE_NO FROM PRODUCTION.TMP_UPDATE_FLAG_NEEDLING) "
                + "AND WIP_PRIORITY_HOLD_CAN_FLAG=0 AND COALESCE(WIP_REJECTED_FLAG,0)=0");
         */

        String strQry = "";
        
        //data.Execute(strQry);
        strQry = "UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA SET WIP_HEATSET_DATE=PROD_DATE,WIP_PIECE_STAGE='NEEDLING',WIP_STATUS='HEAT_SET',WIP_PRIORITY_HOLD_CAN_FLAG=0 WHERE WIP_EXT_PIECE_NO=PROD_PIECE_NO AND WIP_PIECE_STAGE!='DIVERTED' AND WIP_PARTY_CODE=PROD_PARTY_CODE AND PROD_DEPT='HEAT_SETTING' AND PROD_DOC_NO='" + documentNo + "' AND APPROVED=1";
        data.Execute(strQry);
        data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W, "
                + "( "
                + "SELECT TRIM(WIP_PIECE_NO) AS PIECE,MAX(PROD_DATE) AS MAXDATE,GROUP_CONCAT(WIP_EXT_PIECE_NO,' (',DATE_FORMAT(PROD_DATE,'%d/%m/%Y'), ') ' ORDER BY WIP_EXT_PIECE_NO SEPARATOR ', ') AS PIECEPROD , "
                + "SUM(WEIGHT) AS SUMWEIGHT "
                + "FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA "
                + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DEPT ='HEAT_SETTING' AND CANCELED =0 AND APPROVED =1 AND WIP_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING','HEAT_SETTING') "
                + "GROUP BY TRIM(WIP_PIECE_NO) "
                + ") AS A "
                + "SET WIP_HEATSET_DATE = MAXDATE, WIP_HEATSETTING_WEIGHT = SUMWEIGHT "
                + "WHERE A.PIECE = TRIM(W.WIP_PIECE_NO)");

        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER W, "
                + "( "
                + "SELECT TRIM(WIP_PIECE_NO) AS PIECE,MAX(PROD_DATE) AS MAXDATE,GROUP_CONCAT(WIP_EXT_PIECE_NO,' (',DATE_FORMAT(PROD_DATE,'%d/%m/%Y'), ') ' ORDER BY WIP_EXT_PIECE_NO SEPARATOR ', ') AS PIECEPROD , "
                + "SUM(WEIGHT) AS SUMWEIGHT "
                + "FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA "
                + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DEPT ='HEAT_SETTING' AND CANCELED =0 AND APPROVED =1 AND WIP_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING','HEAT_SETTING') "
                + "GROUP BY TRIM(WIP_PIECE_NO) "
                + ") AS A "
                + "SET PR_HEATSET_DATE = MAXDATE, PR_HEATSETTING_WEIGHT = SUMWEIGHT "
                + "WHERE A.PIECE = TRIM(W.PR_PIECE_NO)");

        data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER "
                + "SET WIP_PIECE_STAGE = 'NEEDLING' , WIP_STATUS = 'HEAT_SET' "
                + "WHERE WIP_PRIORITY_HOLD_CAN_FLAG IN (0)  AND WIP_WVG_DATE !='0000-00-00'  AND WIP_MND_DATE !='0000-00-00' AND WIP_HEATSET_DATE !='0000-00-00' AND WIP_NDL_DATE ='0000-00-00' AND WIP_FNSG_DATE ='0000-00-00' AND  WIP_SEAM_DATE ='0000-00-00' AND WIP_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MARKING','SPLICING','MENDING','HEAT_SETTING','NEEDLING','FINISHING') AND WIP_PIECE_NO NOT LIKE ('%V%')");

        data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER "
                + "SET WIP_PIECE_STAGE = 'NEEDLING' , WIP_STATUS = 'RE-PROCESS' "
                + "WHERE WIP_PRIORITY_HOLD_CAN_FLAG IN (0)  AND WIP_WVG_DATE !='0000-00-00'  AND WIP_MND_DATE !='0000-00-00' AND WIP_HEATSET_DATE !='0000-00-00' AND WIP_NDL_DATE ='0000-00-00' AND WIP_FNSG_DATE ='0000-00-00' AND  WIP_SEAM_DATE ='0000-00-00' AND WIP_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MARKING','SPLICING','MENDING','HEAT_SETTING','NEEDLING','FINISHING') AND WIP_PIECE_NO LIKE ('%V%')");
        
        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER "
                + "SET PR_PIECE_STAGE = 'NEEDLING' , PR_WIP_STATUS = 'HEAT_SET' "
                + "WHERE PR_PRIORITY_HOLD_CAN_FLAG IN (0)  AND PR_WVG_DATE !='0000-00-00'  AND PR_MND_DATE !='0000-00-00' AND PR_HEATSET_DATE !='0000-00-00' AND PR_NDL_DATE ='0000-00-00' AND PR_FNSG_DATE ='0000-00-00' AND  PR_SEAM_DATE ='0000-00-00' AND PR_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MARKING','SPLICING','MENDING','HEAT_SETTING','NEEDLING','FINISHING') AND PR_PIECE_NO NOT LIKE ('%V%')");

        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER "
                + "SET PR_PIECE_STAGE = 'NEEDLING' , PR_WIP_STATUS = 'RE-PROCESS' "
                + "WHERE PR_PRIORITY_HOLD_CAN_FLAG IN (0)  AND PR_WVG_DATE !='0000-00-00'  AND PR_MND_DATE !='0000-00-00' AND PR_HEATSET_DATE !='0000-00-00' AND PR_NDL_DATE ='0000-00-00' AND PR_FNSG_DATE ='0000-00-00' AND  PR_SEAM_DATE ='0000-00-00' AND PR_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MARKING','SPLICING','MENDING','HEAT_SETTING','NEEDLING','FINISHING') AND PR_PIECE_NO LIKE ('%V%')");
    }

    // checks piece no already exist in db
    public boolean checkPieceNoInDB(String pPieceNo) {
        int count = data.getIntValueFromDB("SELECT COUNT(PROD_PIECE_NO) FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'HEAT_SETTING' AND PROD_PIECE_NO='" + pPieceNo + "' AND PROD_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND PROD_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'");
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPieceNoInDB(String pPieceNo, String pProdDocNo) {
    //    public boolean checkPieceNoInDB(String pPieceNo, String pProdDate) {
        int count = data.getIntValueFromDB("SELECT COUNT(PROD_PIECE_NO) FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'HEAT_SETTING' AND PROD_PIECE_NO='" + pPieceNo + "' AND PROD_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND PROD_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'");
        if (count >= 1) {
            int counter = 0;
            try {
                Connection Conn = data.getConn();
                Statement stTmp = Conn.createStatement();
                //ResultSet rsTmp = stTmp.executeQuery("SELECT PROD_DATE FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT='HEAT_SETTING' AND PROD_PIECE_NO='" + pPieceNo + "' AND PROD_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND PROD_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'");
                ResultSet rsTmp = stTmp.executeQuery("SELECT PROD_DOC_NO FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT='HEAT_SETTING' AND PROD_PIECE_NO='" + pPieceNo + "' AND PROD_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND PROD_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'");
                while (rsTmp.next()) {
                    //if (rsTmp.getString("PROD_DATE").equals(EITLERPGLOBAL.formatDateDB(pProdDate))) {
                    if (rsTmp.getString("PROD_DOC_NO").equals(pProdDocNo)) {
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

    //checks production date already exist in db
    public boolean checkProductionDateInDB(String pProdDate) {
        int count = data.getIntValueFromDB("SELECT COUNT(PROD_DATE) FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'HEAT_SETTING' AND PROD_DATE='" + pProdDate + "'");
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
        objData.AddColumn("PROD_DATE");
        objData.AddColumn("DOC_NO");
        objData.AddColumn("PIECE_NO");
        objData.AddColumn("PARTY_NAME");
        objData.AddColumn("WEIGHT");
        objData.AddColumn("ORDER_LENGTH");
        objData.AddColumn("ORDER_WIDTH");
        objData.AddColumn("ORDER_GSM");
        objData.AddColumn("ORDER_GROUP");
        objData.AddColumn("DAY_WEIGHT");
        objData.AddColumn("PREVIOUS_WEIGHT");
        objData.AddColumn("TOTAL_WEIGHT");

        try {
            TReportWriter.SimpleDataProvider.TRow objRow = objData.newRow();

            
            String str = "SELECT PROD_DATE,PROD_FORM_NO,REPLACE(PROD_PIECE_NO,' ','') PROD_PIECE_NO,COALESCE(PARTY_NAME,'') PARTY_NAME, WEIGHT,LNGTH,WIDTH,GSQ,GRUP,PREVIOUS_WEIGHT,DAY_WEIGHT,TOTAL_WEIGHT FROM (SELECT PROD_DATE,PROD_FORM_NO,PROD_PIECE_NO,PROD_PARTY_CODE,WEIGHT FROM PRODUCTION.FELT_PROD_DATA A WHERE PROD_DATE = '" + prodDate + "' AND PROD_DEPT='HEAT_SETTING') D LEFT JOIN (SELECT PR_GROUP AS GRUP, PR_GSM AS GSQ, PR_LENGTH AS LNGTH,PR_WIDTH AS WIDTH,PR_PIECE_NO AS PIECE_NO,PR_PARTY_CODE AS PARTY_CD,PR_PIECE_STAGE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER) O ON O.PARTY_CD = D.PROD_PARTY_CODE AND O.PIECE_NO+0 = D.PROD_PIECE_NO+0 AND O.PR_PIECE_STAGE != 'DIVERTED' LEFT JOIN (SELECT PARTY_CODE,TRIM(PARTY_NAME) PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER) AS P ON P.PARTY_CODE = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS PREVIOUS_WEIGHT FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT = 'HEAT_SETTING' AND MONTH(PROD_DATE)=MONTH('" + prodDate + "') AND YEAR(PROD_DATE)=YEAR('" + prodDate + "') AND PROD_DATE<'" + prodDate + "' AND APPROVED=1) AS W ON O.PARTY_CD = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS DAY_WEIGHT FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT = 'HEAT_SETTING' AND MONTH(PROD_DATE)=MONTH('" + prodDate + "') AND YEAR(PROD_DATE)=YEAR('" + prodDate + "') AND PROD_DATE='" + prodDate + "' AND APPROVED=1) AS DW ON O.PARTY_CD = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS TOTAL_WEIGHT FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT = 'HEAT_SETTING' AND MONTH(PROD_DATE)=MONTH('" + prodDate + "') AND YEAR(PROD_DATE)=YEAR('" + prodDate + "') AND PROD_DATE<='" + prodDate + "' AND APPROVED=1) AS T ON O.PARTY_CD = D.PROD_PARTY_CODE";
            System.out.println("PRINT : " + str);
            ResultSet rsTemp = data.getResult(str);
            int counter = 1;
            while (!rsTemp.isAfterLast()) {
                objRow = objData.newRow();
                objRow.setValue("SR_NO", String.valueOf(counter));
                objRow.setValue("FORM_NO", rsTemp.getString("PROD_FORM_NO"));
                objRow.setValue("PROD_DATE", EITLERPGLOBAL.formatDate(rsTemp.getString("PROD_DATE")));
                objRow.setValue("PIECE_NO", rsTemp.getString("PROD_PIECE_NO"));
                objRow.setValue("PARTY_NAME", rsTemp.getString("PARTY_NAME"));
                objRow.setValue("WEIGHT", rsTemp.getString("WEIGHT"));
                objRow.setValue("ORDER_LENGTH", rsTemp.getString("LNGTH"));
                objRow.setValue("ORDER_WIDTH", rsTemp.getString("WIDTH"));
                objRow.setValue("ORDER_GSM", rsTemp.getString("GSQ"));
                objRow.setValue("ORDER_GROUP", rsTemp.getString("GRUP"));
                objRow.setValue("DAY_WEIGHT", rsTemp.getString("DAY_WEIGHT"));
                objRow.setValue("PREVIOUS_WEIGHT", rsTemp.getString("PREVIOUS_WEIGHT"));
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
        int count=data.getIntValueFromDB("SELECT COUNT(PROD_DOC_NO) FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DOC_NO='"+pProdDocNo+"'");
        if(count>0) return true;
        else return false;
    }
}
