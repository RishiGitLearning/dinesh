/*
 * clsFeltMending.java
 *
 * Created on April 24, 2013, 6:20 PM
 */
package EITLERP.Production.FeltMending;

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
public class clsFeltMending {

    public String LastError = "";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public HashMap props;
    public boolean Ready = false;
    //Felt Production Mending Details Collection
    public HashMap hmFeltMendingDetails;

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
     * Creates new DataFeltProductionMending
     */
    public clsFeltMending() {
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

        hmFeltMendingDetails = new HashMap();
    }

    public boolean LoadData() {
        Ready = false;
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery("SELECT DISTINCT PROD_DOC_NO FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'MENDING' AND PROD_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND PROD_DATE<='" + EITLERPGLOBAL.FinToDateDB + "' ORDER BY PROD_DATE");
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
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DATE=''");

            // Production data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.`FELT_PROD_DATA_H` WHERE PROD_DATE=''");

            // Now Insert records into production tables
            for (int i = 1; i <= hmFeltMendingDetails.size(); i++) {
                clsFeltMendingDetails ObjFeltMendingDetails = (clsFeltMendingDetails) hmFeltMendingDetails.get(Integer.toString(i));

                //Insert records into production data table
                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateString("PROD_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()));
                resultSetTemp.updateString("PROD_DOC_NO", getAttribute("PRODUCTION_DOCUMENT_NO").getString());
                resultSetTemp.updateString("PROD_FORM_NO", getAttribute("PRODUCTION_FORM_NO").getString());
                resultSetTemp.updateString("PROD_DEPT", "MENDING");
                resultSetTemp.updateInt("SR_NO", i);
                resultSetTemp.updateString("PROD_PIECE_NO", ObjFeltMendingDetails.getAttribute("PRODUCTION_PIECE_NO").getString());
                resultSetTemp.updateString("PROD_PARTY_CODE", ObjFeltMendingDetails.getAttribute("PRODUCTION_PARTY_CODE").getString());
                resultSetTemp.updateFloat("WEIGHT", (float) EITLERPGLOBAL.round(ObjFeltMendingDetails.getAttribute("WEIGHT").getVal(), 2));
                resultSetTemp.updateString("GROUP", ObjFeltMendingDetails.getAttribute("GROUP").getString());
                resultSetTemp.updateString("REMARKS", ObjFeltMendingDetails.getAttribute("REMARKS").getString());
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
                resultSetHistory.updateString("PROD_DEPT", "MENDING");
                resultSetHistory.updateInt("SR_NO", i);
                resultSetHistory.updateString("PROD_PIECE_NO", ObjFeltMendingDetails.getAttribute("PRODUCTION_PIECE_NO").getString());
                resultSetHistory.updateString("PROD_PARTY_CODE", ObjFeltMendingDetails.getAttribute("PRODUCTION_PARTY_CODE").getString());
                resultSetHistory.updateFloat("WEIGHT", (float) EITLERPGLOBAL.round(ObjFeltMendingDetails.getAttribute("WEIGHT").getVal(), 2));
                resultSetHistory.updateString("GROUP", ObjFeltMendingDetails.getAttribute("GROUP").getString());
                resultSetHistory.updateString("REMARKS", ObjFeltMendingDetails.getAttribute("REMARKS").getString());
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
                        if (ObjFeltMendingDetails.getAttribute("PRODUCTION_PIECE_NO").getString().contains("B")) {
                            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_MND_B_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()) + "',PR_MND_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()) + "' WHERE PR_PIECE_NO+0=" + ObjFeltMendingDetails.getAttribute("PRODUCTION_PIECE_NO").getString() + "+0");
                            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_MENDING_WEIGHT_B=" + (float) EITLERPGLOBAL.round(ObjFeltMendingDetails.getAttribute("WEIGHT").getVal(), 2) + ",WIP_MENDING_WEIGHT=" + (float) EITLERPGLOBAL.round(ObjFeltMendingDetails.getAttribute("WEIGHT").getVal(), 2) + ",WIP_MND_B_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()) + "',WIP_MND_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()) + "' WHERE WIP_EXT_PIECE_NO='" + ObjFeltMendingDetails.getAttribute("PRODUCTION_PIECE_NO").getString() + "'");
                        } else {
                            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_MND_A_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()) + "',PR_MND_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()) + "' WHERE PR_PIECE_NO+0=" + ObjFeltMendingDetails.getAttribute("PRODUCTION_PIECE_NO").getString() + "+0");
                            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_MENDING_WEIGHT_A=" + (float) EITLERPGLOBAL.round(ObjFeltMendingDetails.getAttribute("WEIGHT").getVal(), 2) + ",WIP_MENDING_WEIGHT=" + (float) EITLERPGLOBAL.round(ObjFeltMendingDetails.getAttribute("WEIGHT").getVal(), 2) + ",WIP_MND_A_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()) + "',WIP_MND_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()) + "' WHERE WIP_EXT_PIECE_NO='" + ObjFeltMendingDetails.getAttribute("PRODUCTION_PIECE_NO").getString() + "'");
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = 711; //Felt Mending Entry
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

            // Update Mending Date in Order Master Table To confirm that Mending has completed
            if (ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {
                ObjFeltProductionApprovalFlow.finalApproved = false;
                updateMendingDate(getAttribute("PRODUCTION_DOCUMENT_NO").getString());
//                data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W,"
//                        + "(SELECT WIP_PIECE_NO AS PC,WIP_EXT_PIECE_NO AS PCE,PROD_DATE,WEIGHT FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA "
//                        + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DATE >= '2019-01-01' AND PROD_DEPT ='MENDING' AND CANCELED =0 AND APPROVED =1 "
//                        + "AND WIP_PIECE_AB_FLAG ='AB' AND  WIP_PRIORITY_HOLD_CAN_FLAG IN (0,5) "
//                        + "AND RIGHT(WIP_EXT_PIECE_NO,2) ='-A' AND WEIGHT > 0 AND PROD_DOC_NO='" + getAttribute("PRODUCTION_DOCUMENT_NO").getString() + "' "
//                        + ") AS A "
//                        + "SET  WIP_MND_A_DATE = PROD_DATE,WIP_MENDING_WEIGHT_A =WEIGHT,WIP_EXP_DISPATCH_DATE = PROD_DATE,WIP_HOLD_REASON =WEIGHT "
//                        + "WHERE W.WIP_PRIORITY_HOLD_CAN_FLAG IN (1) AND A.PC = W.WIP_PIECE_NO");
//                data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W,"
//                        + "(SELECT WIP_PIECE_NO AS PC,WIP_EXT_PIECE_NO AS PCE,PROD_DATE,WEIGHT FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA "
//                        + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DATE >= '2019-01-01' AND PROD_DEPT ='MENDING' AND CANCELED =0 AND APPROVED =1 "
//                        + "AND WIP_PIECE_AB_FLAG ='AB' AND  WIP_PRIORITY_HOLD_CAN_FLAG IN (0,5) "
//                        + "AND RIGHT(WIP_EXT_PIECE_NO,2) ='-B' AND WEIGHT > 0 AND PROD_DOC_NO='" + getAttribute("PRODUCTION_DOCUMENT_NO").getString() + "' "
//                        + ") AS A "
//                        + "SET  WIP_MND_B_DATE = PROD_DATE,WIP_MENDING_WEIGHT_B =WEIGHT,WIP_HOLD_DATE =PROD_DATE,WIP_HOLD_REASON =WEIGHT "
//                        + "WHERE W.WIP_PRIORITY_HOLD_CAN_FLAG IN (1) AND A.PC = W.WIP_PIECE_NO");
//                data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W,"
//                        + "(SELECT WIP_PIECE_NO AS PIECE,MAX(PROD_DATE) AS MAXDATE,GROUP_CONCAT(WIP_EXT_PIECE_NO,' (',DATE_FORMAT(PROD_DATE,'%d/%m/%Y'), ') ' ORDER BY WIP_EXT_PIECE_NO SEPARATOR ', ') AS PIECEPROD ,SUM(WEIGHT) AS SUMWEIGHT "
//                        + "FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA "
//                        + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DEPT ='MENDING' AND CANCELED =0 AND APPROVED =1 "
//                        //+ "AND PROD_DOC_NO='" + getAttribute("PRODUCTION_DOCUMENT_NO").getString() + "' "
//                        + "GROUP BY WIP_PIECE_NO "
//                        + ") AS A "
//                        + "SET WIP_MND_DATE = MAXDATE, WIP_MND_LAYER_REMARK = PIECEPROD, WIP_MENDING_WEIGHT = SUMWEIGHT "
//                        + "WHERE W.WIP_PRIORITY_HOLD_CAN_FLAG IN (1,3) AND "
//                        //+ "((WIP_PRIORITY_HOLD_CAN_FLAG=3 AND COALESCE(WIP_PIECE_AB_FLAG,'')='AB') OR (WIP_PRIORITY_HOLD_CAN_FLAG=0 AND COALESCE(WIP_PIECE_AB_FLAG,'')='')) "
//                        + "AND A.PIECE = W.WIP_PIECE_NO AND COALESCE(WIP_PIECE_AB_FLAG,'') ='AB'");
//                data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W,"
//                        + "(SELECT WIP_EXT_PIECE_NO AS PIECE,MAX(PROD_DATE) AS MAXDATE,GROUP_CONCAT(WIP_EXT_PIECE_NO,' (',DATE_FORMAT(PROD_DATE,'%d/%m/%Y'), ') ' ORDER BY WIP_EXT_PIECE_NO SEPARATOR ', ') AS PIECEPROD ,SUM(WEIGHT) AS SUMWEIGHT "
//                        + "FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA "
//                        + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DEPT ='MENDING' AND CANCELED =0 AND APPROVED =1 "
//                        //+ "AND PROD_DOC_NO='" + getAttribute("PRODUCTION_DOCUMENT_NO").getString() + "' "
//                        + "GROUP BY WIP_EXT_PIECE_NO "
//                        + ") AS A "
//                        + "SET WIP_MND_DATE = MAXDATE, WIP_MND_LAYER_REMARK = PIECEPROD, WIP_MENDING_WEIGHT = SUMWEIGHT "
//                        + "WHERE W.WIP_PRIORITY_HOLD_CAN_FLAG IN (0)  AND A.PIECE = W.WIP_EXT_PIECE_NO AND COALESCE(WIP_PIECE_AB_FLAG,'') ='AB'");
//
//                data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W,"
//                        + "(SELECT WIP_PIECE_NO AS PIECE,MAX(PROD_DATE) AS MAXDATE,GROUP_CONCAT(WIP_EXT_PIECE_NO,' (',DATE_FORMAT(PROD_DATE,'%d/%m/%Y'), ') ' ORDER BY WIP_EXT_PIECE_NO SEPARATOR ', ') AS PIECEPROD ,SUM(WEIGHT) AS SUMWEIGHT "
//                        + "FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA "
//                        + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DEPT ='MENDING' AND CANCELED =0 AND APPROVED =1 "
//                        // + "AND PROD_DOC_NO='" + getAttribute("PRODUCTION_DOCUMENT_NO").getString() + "' "
//                        + "GROUP BY WIP_PIECE_NO "
//                        + ") AS A "
//                        + "SET WIP_MND_DATE = MAXDATE, WIP_MND_LAYER_REMARK = PIECEPROD, WIP_MENDING_WEIGHT = SUMWEIGHT "
//                        + "WHERE W.WIP_PRIORITY_HOLD_CAN_FLAG IN (0) AND  A.PIECE = W.WIP_PIECE_NO AND COALESCE(WIP_PIECE_AB_FLAG,'') !='AB'");
//                data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W,"
//                        + "(SELECT WIP_EXT_PIECE_NO AS PIECE,MAX(PROD_DATE) AS MAXDATE,GROUP_CONCAT(WIP_EXT_PIECE_NO,' (',DATE_FORMAT(PROD_DATE,'%d/%m/%Y'), ') ' ORDER BY WIP_EXT_PIECE_NO SEPARATOR ', ') AS PIECEPROD ,SUM(WEIGHT) AS SUMWEIGHT "
//                        + "FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA "
//                        + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DEPT ='MENDING' AND CANCELED =0 AND APPROVED =1 "
//                        // + "AND PROD_DOC_NO='" + getAttribute("PRODUCTION_DOCUMENT_NO").getString() + "' "
//                        + "GROUP BY WIP_EXT_PIECE_NO "
//                        + ") AS A "
//                        + "SET WIP_MND_DATE = MAXDATE, WIP_MND_LAYER_REMARK = PIECEPROD, WIP_MENDING_WEIGHT = SUMWEIGHT "
//                        + "WHERE W.WIP_PRIORITY_HOLD_CAN_FLAG IN (0)  AND A.PIECE = W.WIP_EXT_PIECE_NO AND COALESCE(WIP_PIECE_AB_FLAG,'') !='AB'");

                Production_Date_Updation pd=new Production_Date_Updation();
                
                //Change needling stage to heat setting for special piece
                String strQry = "UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_PIECE_STAGE='HEAT_SETTING' "
                        + "WHERE WIP_PIECE_STAGE='NEEDLING' "
                        + "AND WIP_STATUS='MENDED' AND CONCAT(WIP_PRODUCT_CODE,WIP_STYLE) IN (SELECT CONCAT(PRODUCT_CODE,STYLE_CODE) FROM PRODUCTION.FELT_SALES_STYLE_MASTER WHERE STATUS='ACTIVE' \n"
                        + "AND COALESCE(HEAT_SET_IND,0)=1 ) "
                        + "AND WIP_PRIORITY_HOLD_CAN_FLAG=0 "
                        + "AND COALESCE(WIP_WARP_DATE,'0000-00-00')!='0000-00-00' "
                        + "AND COALESCE(WIP_WVG_DATE,'0000-00-00')!='0000-00-00'  "
                        + "AND COALESCE(WIP_MND_DATE,'0000-00-00')!='0000-00-00'  "
                        + "AND COALESCE(WIP_HEATSET_DATE,'0000-00-00')='0000-00-00'  "                        
                        + "AND COALESCE(WIP_NDL_DATE,'0000-00-00')='0000-00-00'  "                        
                        + "AND COALESCE(WIP_FNSG_DATE,'0000-00-00')='0000-00-00'   "
                        + "AND WIP_EXT_PIECE_NO IN (SELECT PROD_PIECE_NO FROM PRODUCTION.FELT_PROD_DATA "
                        + "WHERE PROD_DOC_NO='"+getAttribute("PRODUCTION_DOCUMENT_NO").getString()+"' AND APPROVED=1 AND CANCELED=0)  ";
                        
                data.Execute(strQry);
                String strQry1="UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PIECE_STAGE='HEAT_SETTING' "
                        + "WHERE PR_PIECE_STAGE='NEEDLING' "
                        + "AND PR_WIP_STATUS='MENDED' AND CONCAT(PR_PRODUCT_CODE,PR_STYLE) IN (SELECT CONCAT(PRODUCT_CODE,STYLE_CODE) FROM PRODUCTION.FELT_SALES_STYLE_MASTER WHERE STATUS='ACTIVE' \n"
                        + "AND COALESCE(HEAT_SET_IND,0)=1 ) "
                        + "AND PR_PRIORITY_HOLD_CAN_FLAG=0 "
                        + "AND COALESCE(PR_REJECTED_FLAG,0)=0 "
                        + "AND COALESCE(PR_WARP_DATE,'0000-00-00')!='0000-00-00' "
                        + "AND COALESCE(PR_WVG_DATE,'0000-00-00')!='0000-00-00'  "
                        + "AND COALESCE(PR_MND_DATE,'0000-00-00')!='0000-00-00'  "
                        + "AND COALESCE(PR_HEATSET_DATE,'0000-00-00')='0000-00-00'  "                        
                        + "AND COALESCE(PR_NDL_DATE,'0000-00-00')='0000-00-00'  "                        
                        + "AND COALESCE(PR_FNSG_DATE,'0000-00-00')='0000-00-00' "
                        + "AND PR_PIECE_NO IN (SELECT DISTINCT SUBSTRING_INDEX(PROD_PIECE_NO, '-', 1) FROM PRODUCTION.FELT_PROD_DATA "
                        + "WHERE PROD_DOC_NO='"+getAttribute("PRODUCTION_DOCUMENT_NO").getString()+"' AND APPROVED=1 AND CANCELED=0) ";
                
                data.Execute(strQry1);
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
            resultSetHistory = statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_PROD_DATA_H WHERE PROD_DEPT = 'MENDING' AND PROD_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()) + "' AND PROD_DOC_NO='" + getAttribute("PRODUCTION_DOCUMENT_NO").getString() + "'");
            resultSetHistory.first();
            if (resultSetHistory.getRow() > 0) {
                revisionNo = resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from production data table before insert
            statementHistory.execute("DELETE FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()) + "' AND PROD_DOC_NO='" + getAttribute("PRODUCTION_DOCUMENT_NO").getString() + "'");

            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.`FELT_PROD_DATA_H` WHERE PROD_DATE=''");

            //Now Update records into production tables
            for (int i = 1; i <= hmFeltMendingDetails.size(); i++) {
                clsFeltMendingDetails ObjFeltMendingDetails = (clsFeltMendingDetails) hmFeltMendingDetails.get(Integer.toString(i));

                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateString("PROD_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()));
                resultSetTemp.updateString("PROD_DOC_NO", getAttribute("PRODUCTION_DOCUMENT_NO").getString());
                resultSetTemp.updateString("PROD_FORM_NO", getAttribute("PRODUCTION_FORM_NO").getString());
                resultSetTemp.updateString("PROD_DEPT", "MENDING");
                resultSetTemp.updateInt("SR_NO", i);
                resultSetTemp.updateString("PROD_PIECE_NO", ObjFeltMendingDetails.getAttribute("PRODUCTION_PIECE_NO").getString());
                resultSetTemp.updateString("PROD_PARTY_CODE", ObjFeltMendingDetails.getAttribute("PRODUCTION_PARTY_CODE").getString());
                resultSetTemp.updateFloat("WEIGHT", (float) EITLERPGLOBAL.round(ObjFeltMendingDetails.getAttribute("WEIGHT").getVal(), 2));
                resultSetTemp.updateString("GROUP", ObjFeltMendingDetails.getAttribute("GROUP").getString());
                resultSetTemp.updateString("REMARKS", ObjFeltMendingDetails.getAttribute("REMARKS").getString());
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
                resultSetHistory.updateString("PROD_DEPT", "MENDING");
                resultSetHistory.updateInt("SR_NO", i);
                resultSetHistory.updateString("PROD_PIECE_NO", ObjFeltMendingDetails.getAttribute("PRODUCTION_PIECE_NO").getString());
                resultSetHistory.updateString("PROD_PARTY_CODE", ObjFeltMendingDetails.getAttribute("PRODUCTION_PARTY_CODE").getString());
                resultSetHistory.updateFloat("WEIGHT", (float) EITLERPGLOBAL.round(ObjFeltMendingDetails.getAttribute("WEIGHT").getVal(), 2));
                resultSetHistory.updateString("GROUP", ObjFeltMendingDetails.getAttribute("GROUP").getString());
                resultSetHistory.updateString("REMARKS", ObjFeltMendingDetails.getAttribute("REMARKS").getString());
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
                        if (ObjFeltMendingDetails.getAttribute("PRODUCTION_PIECE_NO").getString().contains("B")) {
                            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_MND_B_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()) + "',PR_MND_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()) + "' WHERE PR_PIECE_NO+0=" + ObjFeltMendingDetails.getAttribute("PRODUCTION_PIECE_NO").getString() + "+0");
                            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_MENDING_WEIGHT_B=" + (float) EITLERPGLOBAL.round(ObjFeltMendingDetails.getAttribute("WEIGHT").getVal(), 2) + ",WIP_MENDING_WEIGHT=" + (float) EITLERPGLOBAL.round(ObjFeltMendingDetails.getAttribute("WEIGHT").getVal(), 2) + ",WIP_MND_B_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()) + "',WIP_MND_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()) + "' WHERE WIP_EXT_PIECE_NO='" + ObjFeltMendingDetails.getAttribute("PRODUCTION_PIECE_NO").getString() + "'");
                        } else {
                            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_MND_A_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()) + "',PR_MND_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()) + "' WHERE PR_PIECE_NO+0=" + ObjFeltMendingDetails.getAttribute("PRODUCTION_PIECE_NO").getString() + "+0");
                            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_MENDING_WEIGHT_A=" + (float) EITLERPGLOBAL.round(ObjFeltMendingDetails.getAttribute("WEIGHT").getVal(), 2) + ",WIP_MENDING_WEIGHT=" + (float) EITLERPGLOBAL.round(ObjFeltMendingDetails.getAttribute("WEIGHT").getVal(), 2) + ",WIP_MND_A_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()) + "',WIP_MND_DATE='" + EITLERPGLOBAL.formatDateDB(getAttribute("PRODUCTION_DATE").getString()) + "' WHERE WIP_EXT_PIECE_NO='" + ObjFeltMendingDetails.getAttribute("PRODUCTION_PIECE_NO").getString() + "'");
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = 711; //Felt Mending Entry
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
                data.Execute("UPDATE PRODUCTION.`FELT_PROD_DATA` SET REJECTED=0,CHANGED=1 WHERE PROD_DEPT='MENDING' AND PROD_DOC_NO='" + getAttribute("PRODUCTION_DOCUMENT_NO").getString() + "'");
                //Remove Old Records from FELT DOCUMENT APPROVAL TABLE
                //data.Execute("DELETE FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=711 AND DOC_NO='"+getAttribute("PRODUCTION_DOCUMENT_NO").getString()+"'");

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

            // Update Mending Date in Order Master Table To confirm that Mending has completed
            if (ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {
                ObjFeltProductionApprovalFlow.finalApproved = false;
                updateMendingDate(getAttribute("PRODUCTION_DOCUMENT_NO").getString());
//                data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W,"
//                        + "(SELECT WIP_PIECE_NO AS PC,WIP_EXT_PIECE_NO AS PCE,PROD_DATE,WEIGHT FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA "
//                        + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DATE >= '2019-01-01' AND PROD_DEPT ='MENDING' AND CANCELED =0 AND APPROVED =1 "
//                        + "AND WIP_PIECE_AB_FLAG ='AB' AND  WIP_PRIORITY_HOLD_CAN_FLAG IN (0,5) "
//                        + "AND RIGHT(WIP_EXT_PIECE_NO,2) ='-A' AND WEIGHT > 0 AND PROD_DOC_NO='" + getAttribute("PRODUCTION_DOCUMENT_NO").getString() + "' "
//                        + ") AS A "
//                        + "SET  WIP_MND_A_DATE = PROD_DATE,WIP_MENDING_WEIGHT_A =WEIGHT,WIP_EXP_DISPATCH_DATE = PROD_DATE,WIP_HOLD_REASON =WEIGHT "
//                        + "WHERE W.WIP_PRIORITY_HOLD_CAN_FLAG IN (1) AND A.PC = W.WIP_PIECE_NO");
//                data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W,"
//                        + "(SELECT WIP_PIECE_NO AS PC,WIP_EXT_PIECE_NO AS PCE,PROD_DATE,WEIGHT FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA "
//                        + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DATE >= '2019-01-01' AND PROD_DEPT ='MENDING' AND CANCELED =0 AND APPROVED =1 "
//                        + "AND WIP_PIECE_AB_FLAG ='AB' AND  WIP_PRIORITY_HOLD_CAN_FLAG IN (0,5) "
//                        + "AND RIGHT(WIP_EXT_PIECE_NO,2) ='-B' AND WEIGHT > 0 AND PROD_DOC_NO='" + getAttribute("PRODUCTION_DOCUMENT_NO").getString() + "' "
//                        + ") AS A "
//                        + "SET  WIP_MND_B_DATE = PROD_DATE,WIP_MENDING_WEIGHT_B =WEIGHT,WIP_HOLD_DATE =PROD_DATE,WIP_HOLD_REASON =WEIGHT "
//                        + "WHERE W.WIP_PRIORITY_HOLD_CAN_FLAG IN (1) AND A.PC = W.WIP_PIECE_NO");
//                data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W,"
//                        + "(SELECT WIP_PIECE_NO AS PIECE,MAX(PROD_DATE) AS MAXDATE,GROUP_CONCAT(WIP_EXT_PIECE_NO,' (',DATE_FORMAT(PROD_DATE,'%d/%m/%Y'), ') ' ORDER BY WIP_EXT_PIECE_NO SEPARATOR ', ') AS PIECEPROD ,SUM(WEIGHT) AS SUMWEIGHT "
//                        + "FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA "
//                        + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DEPT ='MENDING' AND CANCELED =0 AND APPROVED =1 "
//                        //+ "AND PROD_DOC_NO='" + getAttribute("PRODUCTION_DOCUMENT_NO").getString() + "' "
//                        + "GROUP BY WIP_PIECE_NO "
//                        + ") AS A "
//                        + "SET WIP_MND_DATE = MAXDATE, WIP_MND_LAYER_REMARK = PIECEPROD, WIP_MENDING_WEIGHT = SUMWEIGHT "
//                        + "WHERE W.WIP_PRIORITY_HOLD_CAN_FLAG IN (1,3) AND "
//                        //+ "((WIP_PRIORITY_HOLD_CAN_FLAG=3 AND COALESCE(WIP_PIECE_AB_FLAG,'')='AB') OR (WIP_PRIORITY_HOLD_CAN_FLAG=0 AND COALESCE(WIP_PIECE_AB_FLAG,'')='')) "
//                        + "AND A.PIECE = W.WIP_PIECE_NO AND COALESCE(WIP_PIECE_AB_FLAG,'') ='AB'");
//                data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W,"
//                        + "(SELECT WIP_EXT_PIECE_NO AS PIECE,MAX(PROD_DATE) AS MAXDATE,GROUP_CONCAT(WIP_EXT_PIECE_NO,' (',DATE_FORMAT(PROD_DATE,'%d/%m/%Y'), ') ' ORDER BY WIP_EXT_PIECE_NO SEPARATOR ', ') AS PIECEPROD ,SUM(WEIGHT) AS SUMWEIGHT "
//                        + "FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA "
//                        + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DEPT ='MENDING' AND CANCELED =0 AND APPROVED =1 "
//                        //+ "AND PROD_DOC_NO='" + getAttribute("PRODUCTION_DOCUMENT_NO").getString() + "' "
//                        + "GROUP BY WIP_EXT_PIECE_NO "
//                        + ") AS A "
//                        + "SET WIP_MND_DATE = MAXDATE, WIP_MND_LAYER_REMARK = PIECEPROD, WIP_MENDING_WEIGHT = SUMWEIGHT "
//                        + "WHERE W.WIP_PRIORITY_HOLD_CAN_FLAG IN (0)  AND A.PIECE = W.WIP_EXT_PIECE_NO AND COALESCE(WIP_PIECE_AB_FLAG,'') ='AB'");
//
//                data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W,"
//                        + "(SELECT WIP_PIECE_NO AS PIECE,MAX(PROD_DATE) AS MAXDATE,GROUP_CONCAT(WIP_EXT_PIECE_NO,' (',DATE_FORMAT(PROD_DATE,'%d/%m/%Y'), ') ' ORDER BY WIP_EXT_PIECE_NO SEPARATOR ', ') AS PIECEPROD ,SUM(WEIGHT) AS SUMWEIGHT "
//                        + "FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA "
//                        + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DEPT ='MENDING' AND CANCELED =0 AND APPROVED =1 "
//                        // + "AND PROD_DOC_NO='" + getAttribute("PRODUCTION_DOCUMENT_NO").getString() + "' "
//                        + "GROUP BY WIP_PIECE_NO "
//                        + ") AS A "
//                        + "SET WIP_MND_DATE = MAXDATE, WIP_MND_LAYER_REMARK = PIECEPROD, WIP_MENDING_WEIGHT = SUMWEIGHT "
//                        + "WHERE W.WIP_PRIORITY_HOLD_CAN_FLAG IN (0) AND  A.PIECE = W.WIP_PIECE_NO AND COALESCE(WIP_PIECE_AB_FLAG,'') !='AB'");
//                data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W,"
//                        + "(SELECT WIP_EXT_PIECE_NO AS PIECE,MAX(PROD_DATE) AS MAXDATE,GROUP_CONCAT(WIP_EXT_PIECE_NO,' (',DATE_FORMAT(PROD_DATE,'%d/%m/%Y'), ') ' ORDER BY WIP_EXT_PIECE_NO SEPARATOR ', ') AS PIECEPROD ,SUM(WEIGHT) AS SUMWEIGHT "
//                        + "FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA "
//                        + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DEPT ='MENDING' AND CANCELED =0 AND APPROVED =1 "
//                        // + "AND PROD_DOC_NO='" + getAttribute("PRODUCTION_DOCUMENT_NO").getString() + "' "
//                        + "GROUP BY WIP_EXT_PIECE_NO "
//                        + ") AS A "
//                        + "SET WIP_MND_DATE = MAXDATE, WIP_MND_LAYER_REMARK = PIECEPROD, WIP_MENDING_WEIGHT = SUMWEIGHT "
//                        + "WHERE W.WIP_PRIORITY_HOLD_CAN_FLAG IN (0)  AND A.PIECE = W.WIP_EXT_PIECE_NO AND COALESCE(WIP_PIECE_AB_FLAG,'') !='AB'");
                
                Production_Date_Updation pd=new Production_Date_Updation();
                
                //Change needling stage to heat setting for special piece
                String strQry = "UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_PIECE_STAGE='HEAT_SETTING' "
                        + "WHERE WIP_PIECE_STAGE='NEEDLING' "
                        + "AND WIP_STATUS='MENDED' AND CONCAT(WIP_PRODUCT_CODE,WIP_STYLE) IN (SELECT CONCAT(PRODUCT_CODE,STYLE_CODE) FROM PRODUCTION.FELT_SALES_STYLE_MASTER WHERE STATUS='ACTIVE' \n"
                        + "AND COALESCE(HEAT_SET_IND,0)=1 ) "
                        + "AND WIP_PRIORITY_HOLD_CAN_FLAG=0 "
                        + "AND COALESCE(WIP_WARP_DATE,'0000-00-00')!='0000-00-00' "
                        + "AND COALESCE(WIP_WVG_DATE,'0000-00-00')!='0000-00-00'  "
                        + "AND COALESCE(WIP_MND_DATE,'0000-00-00')!='0000-00-00'  "
                        + "AND COALESCE(WIP_HEATSET_DATE,'0000-00-00')='0000-00-00'  "                        
                        + "AND COALESCE(WIP_NDL_DATE,'0000-00-00')='0000-00-00'  "                        
                        + "AND COALESCE(WIP_FNSG_DATE,'0000-00-00')='0000-00-00'   "
                        + "AND WIP_EXT_PIECE_NO IN (SELECT PROD_PIECE_NO FROM PRODUCTION.FELT_PROD_DATA "
                        + "WHERE PROD_DOC_NO='"+getAttribute("PRODUCTION_DOCUMENT_NO").getString()+"' AND APPROVED=1 AND CANCELED=0)  ";
                        
                data.Execute(strQry);
                String strQry1="UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PIECE_STAGE='HEAT_SETTING' "
                        + "WHERE PR_PIECE_STAGE='NEEDLING' "
                        + "AND PR_WIP_STATUS='MENDED' AND CONCAT(PR_PRODUCT_CODE,PR_STYLE) IN (SELECT CONCAT(PRODUCT_CODE,STYLE_CODE) FROM PRODUCTION.FELT_SALES_STYLE_MASTER WHERE STATUS='ACTIVE' \n"
                        + "AND COALESCE(HEAT_SET_IND,0)=1 ) "
                        + "AND PR_PRIORITY_HOLD_CAN_FLAG=0 "
                        + "AND COALESCE(PR_REJECTED_FLAG,0)=0 "
                        + "AND COALESCE(PR_WARP_DATE,'0000-00-00')!='0000-00-00' "
                        + "AND COALESCE(PR_WVG_DATE,'0000-00-00')!='0000-00-00'  "
                        + "AND COALESCE(PR_MND_DATE,'0000-00-00')!='0000-00-00'  "
                        + "AND COALESCE(PR_HEATSET_DATE,'0000-00-00')='0000-00-00'  "                        
                        + "AND COALESCE(PR_NDL_DATE,'0000-00-00')='0000-00-00'  "                        
                        + "AND COALESCE(PR_FNSG_DATE,'0000-00-00')='0000-00-00' "
                        + "AND PR_PIECE_NO IN (SELECT DISTINCT SUBSTRING_INDEX(PROD_PIECE_NO, '-', 1) FROM PRODUCTION.FELT_PROD_DATA "
                        + "WHERE PROD_DOC_NO='"+getAttribute("PRODUCTION_DOCUMENT_NO").getString()+"' AND APPROVED=1 AND CANCELED=0) ";
                
                data.Execute(strQry1);
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'MENDING' AND PROD_DOC_NO='" + documentNo + "' AND APPROVED=" + 1;
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=711 AND USER_ID=" + userID + " AND DOC_NO='" + documentNo + "' AND STATUS='W'";
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();

                if (rsTmp.getInt("COUNT") > 0) {
                    strSQL = "DELETE FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'MENDING'"
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'MENDING' AND PROD_DOC_NO='" + documentNo + "' AND APPROVED=1";
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=711 AND USER_ID=" + userID + " AND DOC_NO='" + documentNo + "' AND STATUS='W'";
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
            String strSql = "SELECT DISTINCT PROD_DOC_NO FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'MENDING' " + stringFindQuery + "ORDER BY PROD_DATE";
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
            String documentNo = resultSet.getString("PROD_DOC_NO");
            setAttribute("PRODUCTION_DOCUMENT_NO", documentNo);

            totalWeight = data.getDoubleValueFromDB("SELECT SUM(WEIGHT) FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'MENDING' AND MONTH(PROD_DATE)=" + documentNo.substring(7, 9) + " AND YEAR(PROD_DATE)=" + documentNo.substring(3, 7) + " AND APPROVED=1");
            setAttribute("TOTAL_WEIGHT", totalWeight);

            previousWeight = data.getDoubleValueFromDB("SELECT SUM(WEIGHT) FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'MENDING' AND MONTH(PROD_DATE)=" + documentNo.substring(7, 9) + " AND YEAR(PROD_DATE)=" + documentNo.substring(3, 7) + " AND SUBSTRING(PROD_DOC_NO,4,8)<'" + documentNo.substring(3) + "' AND APPROVED=1");
            setAttribute("PREVIOUS_WEIGHT", previousWeight);

            //Now Populate the collection, first clear the collection
            hmFeltMendingDetails.clear();

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'MENDING'  AND PROD_DOC_NO='" + documentNo + "' ORDER BY SR_NO");

            while (resultSetTemp.next()) {
                serialNoCounter++;
                setAttribute("PRODUCTION_DATE", resultSetTemp.getString("PROD_DATE"));
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

                clsFeltMendingDetails ObjFeltMendingDetails = new clsFeltMendingDetails();

                ObjFeltMendingDetails.setAttribute("PRODUCTION_PIECE_NO", resultSetTemp.getString("PROD_PIECE_NO"));
                ObjFeltMendingDetails.setAttribute("PRODUCTION_PARTY_CODE", resultSetTemp.getString("PROD_PARTY_CODE"));
                ObjFeltMendingDetails.setAttribute("WEIGHT", resultSetTemp.getFloat("WEIGHT"));
                ObjFeltMendingDetails.setAttribute("GROUP", resultSetTemp.getString("GROUP"));
                ObjFeltMendingDetails.setAttribute("REMARKS", resultSetTemp.getString("REMARKS"));

                hmFeltMendingDetails.put(Integer.toString(serialNoCounter), ObjFeltMendingDetails);
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
            hmFeltMendingDetails.clear();

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.`FELT_PROD_DATA_H` WHERE PROD_DEPT = 'MENDING' AND PROD_DATE='" + pProductionDate + "' AND PROD_DOC_NO='" + pDocNo + "' AND REVISION_NO='" + resultSet.getInt("REVISION_NO") + "' ORDER BY REVISION_NO");

            while (resultSetTemp.next()) {
                serialNoCounter++;
                setAttribute("PRODUCTION_DOCUMENT_NO", resultSetTemp.getString("PROD_DOC_NO"));
                setAttribute("PRODUCTION_FORM_NO", resultSetTemp.getString("PROD_FORM_NO"));
                setAttribute("UPDATED_BY", resultSetTemp.getInt("UPDATED_BY"));
                setAttribute("ENTRY_DATE", resultSetTemp.getString("ENTRY_DATE"));
                setAttribute("HIERARCHY_ID", resultSetTemp.getInt("HIERARCHY_ID"));

                clsFeltMendingDetails ObjFeltMendingDetails = new clsFeltMendingDetails();

                ObjFeltMendingDetails.setAttribute("PRODUCTION_PIECE_NO", resultSetTemp.getString("PROD_PIECE_NO"));
                ObjFeltMendingDetails.setAttribute("PRODUCTION_PARTY_CODE", resultSetTemp.getString("PROD_PARTY_CODE"));
                ObjFeltMendingDetails.setAttribute("WEIGHT", resultSetTemp.getFloat("WEIGHT"));
                ObjFeltMendingDetails.setAttribute("GROUP", resultSetTemp.getString("GROUP"));
                ObjFeltMendingDetails.setAttribute("REMARKS", resultSetTemp.getString("REMARKS"));

                hmFeltMendingDetails.put(Integer.toString(serialNoCounter), ObjFeltMendingDetails);
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
            rsTmp = stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_PROD_DATA_H WHERE PROD_DEPT = 'MENDING' AND PROD_DATE='" + stringProductionDate + "' AND PROD_DOC_NO='" + productionDocumentNo + "' GROUP BY REVISION_NO");

            while (rsTmp.next()) {
                clsFeltMending ObjFeltMending = new clsFeltMending();

                ObjFeltMending.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                ObjFeltMending.setAttribute("UPDATED_BY", rsTmp.getInt("UPDATED_BY"));
                ObjFeltMending.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                ObjFeltMending.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                ObjFeltMending.setAttribute("APPROVER_REMARKS", rsTmp.getString("APPROVER_REMARKS"));
                ObjFeltMending.setAttribute("FROM_IP", rsTmp.getString("FROM_IP"));

                hmHistoryList.put(Integer.toString(hmHistoryList.size() + 1), ObjFeltMending);
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
            resultSet = statement.executeQuery("SELECT DISTINCT REVISION_NO FROM PRODUCTION.FELT_PROD_DATA_H WHERE PROD_DEPT='MENDING' AND PROD_DATE='" + pProductionDate + "' AND PROD_DOC_NO='" + pDocNo + "'");
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
                strSQL = "SELECT DISTINCT PROD_DOC_NO,PROD_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_PROD_DATA,PRODUCTION.FELT_PROD_DOC_DATA WHERE PROD_DOC_NO=DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=711 AND CANCELED=0 ORDER BY RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT PROD_DOC_NO,PROD_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_PROD_DATA,PRODUCTION.FELT_PROD_DOC_DATA WHERE PROD_DOC_NO=DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=711 AND CANCELED=0 ORDER BY PROD_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT PROD_DOC_NO,PROD_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_PROD_DATA,PRODUCTION.FELT_PROD_DOC_DATA WHERE PROD_DOC_NO=DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=711 AND CANCELED=0 ORDER BY PROD_DOC_NO";
            }

            ResultSet rsTmp = data.getConn().createStatement().executeQuery(strSQL);

            Counter = 0;
            while (rsTmp.next()) {

                Counter = Counter + 1;
                clsFeltMending ObjDoc = new clsFeltMending();

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

    public String[] getPartyCodeAndGroup(String pPieceNo) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        String[] partyCodeAndGroup = new String[2];

        try {
            tmpConn = data.getConn();
            Stmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //rsTmp=Stmt.executeQuery("SELECT PARTY_CD,GRUP FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'+0");

            if (pPieceNo.contains("P") || pPieceNo.contains("V")) {
                pPieceNo = pPieceNo.substring(0, 6);
            } else {
                pPieceNo = pPieceNo.substring(0, 5);
            }

            System.out.println("getPartyName and Group (Mending) : SELECT PR_PARTY_CODE,PR_GROUP,PR_PIECE_STAGE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + pPieceNo + "'");
            rsTmp = Stmt.executeQuery("SELECT PR_PARTY_CODE,PR_GROUP,PR_PIECE_STAGE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + pPieceNo + "'");
            rsTmp.first();

            //  AND PR_PIECE_STAGE!='DIVERTED'
            if (rsTmp.getString("PR_PIECE_STAGE").equals("DIVERTED")) {
                JOptionPane.showMessageDialog(null, "This Piece is Diverted, Please use new Piece Number with P or V");
                return partyCodeAndGroup;
            }

            if (rsTmp.getRow() > 0) {
                partyCodeAndGroup[0] = rsTmp.getString("PR_PARTY_CODE");
                partyCodeAndGroup[1] = rsTmp.getString("PR_GROUP");
            }

            Stmt.close();
            rsTmp.close();

            return partyCodeAndGroup;
        } catch (Exception e) {
            e.printStackTrace();
            return partyCodeAndGroup;
        }
    }

    // Updates Mending Date in Order Master Table To confirm that Mending has completed
    private void updateMendingDate(String documentNo) {
        //data.Execute("UPDATE PRODUCTION.FELT_ORDER_MASTER, PRODUCTION.FELT_PROD_DATA SET MND_DATE=PROD_DATE WHERE PIECE_NO+0=PROD_PIECE_NO+0 AND PARTY_CD=PROD_PARTY_CODE AND PROD_DEPT='MENDING' AND PROD_DOC_NO='"+documentNo+"' AND APPROVED=1");
//        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER, PRODUCTION.FELT_PROD_DATA SET PR_MND_DATE=PROD_DATE,PR_PIECE_STAGE='NEEDLING',PR_WIP_STATUS='MENDED' WHERE PR_PIECE_NO=PROD_PIECE_NO AND PR_PIECE_STAGE!='DIVERTED' AND PR_PARTY_CODE=PROD_PARTY_CODE AND PROD_DEPT='MENDING' AND PROD_DOC_NO='" + documentNo + "' AND APPROVED=1 AND PR_PIECE_STAGE IN ('WEAVING','MENDING','NEEDLING','FINISHING')");
//        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER, PRODUCTION.FELT_PROD_DATA SET PR_MND_DATE=PROD_DATE,PR_PIECE_STAGE='SEAMING',PR_WIP_STATUS='MENDED' WHERE PR_PIECE_NO=PROD_PIECE_NO AND PR_PIECE_STAGE!='DIVERTED' AND PR_PARTY_CODE=PROD_PARTY_CODE AND PROD_DEPT='MENDING' AND PR_PRODUCT_CODE LIKE '7%' AND  PR_PRODUCT_CODE NOT LIKE '729%' AND PROD_DOC_NO='" + documentNo + "' AND APPROVED=1 AND PR_PIECE_STAGE IN ('WEAVING','MENDING','NEEDLING','FINISHING')");
//        data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER, PRODUCTION.FELT_PROD_DATA SET WIP_MND_DATE=PROD_DATE,WIP_PIECE_STAGE='NEEDLING',WIP_STATUS='MENDED' WHERE WIP_EXT_PIECE_NO=PROD_PIECE_NO AND WIP_PIECE_STAGE!='DIVERTED' AND WIP_PARTY_CODE=PROD_PARTY_CODE AND PROD_DEPT='MENDING' AND PROD_DOC_NO='" + documentNo + "' AND APPROVED=1 AND WIP_PIECE_STAGE IN ('WEAVING','MENDING','NEEDLING','FINISHING')");
//        data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER, PRODUCTION.FELT_PROD_DATA SET WIP_MND_DATE=PROD_DATE,WIP_PIECE_STAGE='SEAMING',WIP_STATUS='MENDED' WHERE WIP_EXT_PIECE_NO=PROD_PIECE_NO AND WIP_PIECE_STAGE!='DIVERTED' AND WIP_PARTY_CODE=PROD_PARTY_CODE AND PROD_DEPT='MENDING' AND WIP_PRODUCT_CODE LIKE '7%' AND  WIP_PRODUCT_CODE NOT LIKE '729%' AND PROD_DOC_NO='" + documentNo + "' AND APPROVED=1 AND WIP_PIECE_STAGE IN ('WEAVING','MENDING','NEEDLING','FINISHING')");

//        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER,(SELECT SUBSTRING(PROD_PIECE_NO,1,5) AS PIECE,MAX(PROD_DATE) AS MAXDATE,GROUP_CONCAT(PROD_PIECE_NO,' ( ',DATE_FORMAT(PROD_DATE,\"%d/%m/%Y\"),' ) '  ORDER BY PROD_PIECE_NO SEPARATOR ', ') AS PIECEPROD FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT = 'MENDING'   AND CANCELED =0 AND APPROVED =1 GROUP BY SUBSTRING(PROD_PIECE_NO,1,5)) AS A SET MND_LAYER_REMARK = PIECEPROD,PR_MND_DATE=MAXDATE WHERE PIECE=PR_PIECE_NO  ");
//        data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER,"
//                + "(SELECT PROD_PIECE_NO AS PIECE,MAX(PROD_DATE) AS MAXDATE,"
//                + "GROUP_CONCAT(PROD_PIECE_NO,' ( ',DATE_FORMAT(PROD_DATE,\"%d/%m/%Y\"),' ) '  ORDER BY PROD_PIECE_NO SEPARATOR ', ') AS PIECEPROD "
//                + "FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT = 'MENDING'   AND CANCELED =0 AND APPROVED =1 "
//                + "GROUP BY PROD_PIECE_NO) AS A "
//                + "SET WIP_MND_LAYER_REMARK = PIECEPROD,WIP_MND_DATE=MAXDATE "
//                + "WHERE PIECE=WIP_EXT_PIECE_NO AND COALESCE(WIP_REJECTED_FLAG,0)=0  ");
//
//        data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER,"
//                + "(SELECT PROD_PIECE_NO AS PIECE,MAX(PROD_DATE) AS MAXDATE,"
//                + "GROUP_CONCAT(PROD_PIECE_NO,' ( ',DATE_FORMAT(PROD_DATE,\"%d/%m/%Y\"),' ) '  ORDER BY PROD_PIECE_NO SEPARATOR ', ') AS PIECEPROD "
//                + "FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT = 'MENDING'   AND CANCELED =0 AND APPROVED =1 "
//                + "GROUP BY SUBSTRING(PROD_PIECE_NO,1,5)) AS A "
//                + "SET WIP_MND_LAYER_REMARK = PIECEPROD,WIP_MND_DATE=MAXDATE "
//                + "WHERE LEFT(PIECE,5)=LEFT(WIP_EXT_PIECE_NO,5) AND COALESCE(WIP_REJECTED_FLAG,0)=0 AND "
//                + "((WIP_PRIORITY_HOLD_CAN_FLAG=3 AND COALESCE(WIP_PIECE_AB_FLAG,'')='AB') OR (WIP_PRIORITY_HOLD_CAN_FLAG=0 AND COALESCE(WIP_PIECE_AB_FLAG,'')='')) ");
        data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W, "
                + "( "
                + "SELECT TRIM(WIP_PIECE_NO) AS PIECE,MAX(PROD_DATE) AS MAXDATE,GROUP_CONCAT(WIP_EXT_PIECE_NO,' (',DATE_FORMAT(PROD_DATE,'%d/%m/%Y'), ') ' ORDER BY WIP_EXT_PIECE_NO SEPARATOR ', ') AS PIECEPROD , "
                + "SUM(WEIGHT) AS SUMWEIGHT "
                + "FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA "
                + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DEPT ='MENDING' AND CANCELED =0 AND APPROVED =1  AND COALESCE(WIP_PIECE_AB_FLAG,'') !='AB'  AND WIP_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING') "
                + "GROUP BY TRIM(WIP_PIECE_NO) "
                + ") AS A "
                + "SET WIP_MND_DATE = MAXDATE, WIP_MND_LAYER_REMARK = PIECEPROD, WIP_MENDING_WEIGHT = SUMWEIGHT,WIP_MND_A_DATE = MAXDATE, WIP_MENDING_WEIGHT_A = SUMWEIGHT "
                + "WHERE A.PIECE = TRIM(W.WIP_EXT_PIECE_NO) AND COALESCE(WIP_PIECE_AB_FLAG,'') !='AB'");
        data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W, "
                + "( "
                + "SELECT TRIM(WIP_PIECE_NO) AS PIECE,CONCAT(TRIM(WIP_PIECE_NO),'-AB') AS EXTPIECE,MAX(PROD_DATE) AS MAXDATE,GROUP_CONCAT(WIP_EXT_PIECE_NO,' (',DATE_FORMAT(PROD_DATE,'%d/%m/%Y'), ') ' ORDER BY WIP_EXT_PIECE_NO SEPARATOR ', ') AS PIECEPROD , "
                + "MAX(CASE WHEN RIGHT(WIP_EXT_PIECE_NO,2) ='-A' THEN PROD_DATE ELSE '0000-00-00' END) AS A, "
                + "MAX(CASE WHEN RIGHT(WIP_EXT_PIECE_NO,2) ='-B' THEN PROD_DATE ELSE '0000-00-00' END) AS B, "
                + "SUM(CASE WHEN RIGHT(WIP_EXT_PIECE_NO,2) ='-A' THEN WEIGHT ELSE 0 END) AS AA, "
                + "SUM(CASE WHEN RIGHT(WIP_EXT_PIECE_NO,2) ='-B' THEN WEIGHT ELSE 0 END) AS BB, "
                + "SUM(WEIGHT) AS SUMWEIGHT "
                + "FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA "
                + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DEPT ='MENDING' AND CANCELED =0 AND APPROVED =1  AND COALESCE(WIP_PIECE_AB_FLAG,'') ='AB' AND WIP_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING') "
                + "GROUP BY TRIM(WIP_PIECE_NO) "
                + ") AS A "
                + "SET WIP_MND_DATE = MAXDATE, WIP_MND_LAYER_REMARK = PIECEPROD, WIP_MENDING_WEIGHT = SUMWEIGHT,WIP_MND_A_DATE = A, WIP_MND_B_DATE =B, WIP_MENDING_WEIGHT_A = AA , WIP_MENDING_WEIGHT_B = BB "
                + "WHERE A.EXTPIECE = W.WIP_EXT_PIECE_NO AND COALESCE(WIP_PIECE_AB_FLAG,'') ='AB'");

        data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W, "
                + "( "
                + "SELECT TRIM(WIP_EXT_PIECE_NO) AS EXTPIECE,MAX(PROD_DATE) AS MAXDATE,GROUP_CONCAT(WIP_EXT_PIECE_NO,' (',DATE_FORMAT(PROD_DATE,'%d/%m/%Y'), ') ' ORDER BY WIP_EXT_PIECE_NO SEPARATOR ', ') AS PIECEPROD , "
                + "MAX(CASE WHEN RIGHT(WIP_EXT_PIECE_NO,2) ='-A' THEN PROD_DATE ELSE '0000-00-00' END) AS A, "
                + "MAX(CASE WHEN RIGHT(WIP_EXT_PIECE_NO,2) ='-B' THEN PROD_DATE ELSE '0000-00-00' END) AS B, "
                + "SUM(CASE WHEN RIGHT(WIP_EXT_PIECE_NO,2) ='-A' THEN WEIGHT ELSE 0 END) AS AA, "
                + "SUM(CASE WHEN RIGHT(WIP_EXT_PIECE_NO,2) ='-B' THEN WEIGHT ELSE 0 END) AS BB, "
                + "SUM(WEIGHT) AS SUMWEIGHT "
                + "FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA "
                + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DEPT ='MENDING' AND CANCELED =0 AND APPROVED =1  AND COALESCE(WIP_PIECE_AB_FLAG,'') ='AB'  AND WIP_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING') "
                + "GROUP BY TRIM(WIP_EXT_PIECE_NO) "
                + ") AS A "
                + "SET WIP_MND_DATE = MAXDATE, WIP_MND_LAYER_REMARK = PIECEPROD, WIP_MENDING_WEIGHT = SUMWEIGHT,WIP_MND_A_DATE = A, WIP_MND_B_DATE =B, WIP_MENDING_WEIGHT_A = AA , WIP_MENDING_WEIGHT_B = BB "
                + "WHERE A.EXTPIECE = W.WIP_EXT_PIECE_NO AND COALESCE(WIP_PIECE_AB_FLAG,'') ='AB'");

        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER S, "
                + "( "
                + "SELECT TRIM(WIP_PIECE_NO) AS PIECE,MAX(PROD_DATE) AS MAXDATE,GROUP_CONCAT(WIP_EXT_PIECE_NO,' (',DATE_FORMAT(PROD_DATE,'%d/%m/%Y'), ') ' ORDER BY WIP_EXT_PIECE_NO SEPARATOR ', ') AS PIECEPROD , "
                + "SUM(WEIGHT) AS SUMWEIGHT "
                + "FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA "
                + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DEPT ='MENDING' AND CANCELED =0 AND APPROVED =1  AND COALESCE(WIP_PIECE_AB_FLAG,'') !='AB' AND WIP_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING') "
                + "GROUP BY TRIM(WIP_PIECE_NO) "
                + ") AS A "
                + "SET PR_MND_DATE = MAXDATE, PR_MND_LAYER_REMARK = PIECEPROD, PR_MENDING_WEIGHT = SUMWEIGHT,PR_MND_A_DATE = MAXDATE, PR_MENDING_WEIGHT_A = SUMWEIGHT "
                + "WHERE A.PIECE = TRIM(S.PR_PIECE_NO) AND COALESCE(PR_PIECE_AB_FLAG,'') !='AB'");

        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER S, "
                + "( "
                + "SELECT TRIM(WIP_PIECE_NO) AS PIECE,CONCAT(TRIM(WIP_PIECE_NO),'-AB') AS EXTPIECE,MAX(PROD_DATE) AS MAXDATE,GROUP_CONCAT(WIP_EXT_PIECE_NO,' (',DATE_FORMAT(PROD_DATE,'%d/%m/%Y'), ') ' ORDER BY WIP_EXT_PIECE_NO SEPARATOR ', ') AS PIECEPROD , "
                + "MAX(CASE WHEN RIGHT(WIP_EXT_PIECE_NO,2) ='-A' THEN PROD_DATE ELSE '0000-00-00' END) AS A, "
                + "MAX(CASE WHEN RIGHT(WIP_EXT_PIECE_NO,2) ='-B' THEN PROD_DATE ELSE '0000-00-00' END) AS B, "
                + "SUM(CASE WHEN RIGHT(WIP_EXT_PIECE_NO,2) ='-A' THEN WEIGHT ELSE 0 END) AS AA, "
                + "SUM(CASE WHEN RIGHT(WIP_EXT_PIECE_NO,2) ='-B' THEN WEIGHT ELSE 0 END) AS BB, "
                + "SUM(WEIGHT) AS SUMWEIGHT "
                + "FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA "
                + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DEPT ='MENDING' AND CANCELED =0 AND APPROVED =1  AND COALESCE(WIP_PIECE_AB_FLAG,'') ='AB'    AND WIP_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING') "
                + "GROUP BY TRIM(WIP_PIECE_NO) "
                + ") AS A "
                + "SET PR_MND_DATE = MAXDATE, PR_MND_LAYER_REMARK = PIECEPROD, PR_MENDING_WEIGHT = SUMWEIGHT,PR_MND_A_DATE = A, PR_MND_B_DATE =B, PR_MENDING_WEIGHT_A = AA , PR_MENDING_WEIGHT_B = BB "
                + "WHERE A.PIECE = S.PR_PIECE_NO AND COALESCE(PR_PIECE_AB_FLAG,'') ='AB'");

        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PIECE_STAGE = 'NEEDLING' ,PR_WIP_STATUS = 'MENDED' "
                + "WHERE PR_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5)   "
                + " AND PR_WVG_DATE !='0000-00-00'  AND PR_MND_DATE !='0000-00-00' AND PR_NDL_DATE ='0000-00-00' AND PR_FNSG_DATE ='0000-00-00' AND  PR_SEAM_DATE ='0000-00-00' AND PR_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','0') AND PR_GROUP NOT IN ('HDS','M50','M35')");

        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PIECE_STAGE = 'SEAMING' ,PR_WIP_STATUS = 'MENDED' "
                + "WHERE PR_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5)   "
                + " AND PR_WVG_DATE !='0000-00-00'  AND PR_MND_DATE !='0000-00-00' AND PR_NDL_DATE ='0000-00-00' AND PR_FNSG_DATE ='0000-00-00'AND  PR_SEAM_DATE ='0000-00-00' AND  PR_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','0') AND PR_GROUP IN ('HDS','M50','M35')");

        data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_PIECE_STAGE = 'NEEDLING' ,WIP_STATUS = 'MENDED' "
                + "WHERE WIP_PRIORITY_HOLD_CAN_FLAG IN (0)   "
                + " AND WIP_WVG_DATE !='0000-00-00'  AND WIP_MND_DATE !='0000-00-00' AND WIP_NDL_DATE ='0000-00-00' AND WIP_FNSG_DATE ='0000-00-00' AND  WIP_SEAM_DATE ='0000-00-00' AND WIP_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','0') AND WIP_GROUP NOT IN ('HDS','M50','M35')");

        data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_PIECE_STAGE = 'SEAMING' ,WIP_STATUS = 'MENDED' "
                + "WHERE WIP_PRIORITY_HOLD_CAN_FLAG IN (0)   "
                + " AND WIP_WVG_DATE !='0000-00-00'  AND WIP_MND_DATE !='0000-00-00' AND WIP_NDL_DATE ='0000-00-00' AND WIP_FNSG_DATE ='0000-00-00'AND  WIP_SEAM_DATE ='0000-00-00' AND  WIP_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','0') AND WIP_GROUP IN ('HDS','M50','M35')");
        
        data.Execute("DROP TABLE PRODUCTION.TMP_UPDATE_FLAG_MENDING");
        
        data.Execute("CREATE TABLE PRODUCTION.TMP_UPDATE_FLAG_MENDING "
                + "SELECT WIP_PIECE_NO FROM PRODUCTION.FELT_WIP_PIECE_REGISTER "
                + "WHERE WIP_PIECE_NO IN "
                + "(SELECT LEFT(PROD_PIECE_NO,5) FROM PRODUCTION.FELT_PROD_DATA "
                + "WHERE PROD_DEPT='MENDING' AND PROD_DOC_NO='" + documentNo + "' AND APPROVED=1 AND COALESCE(CANCELED,0)=0) AND WIP_MND_DATE!='0000-00-00' AND COALESCE(WIP_REJECTED_FLAG,0)=0 AND WIP_PRIORITY_HOLD_CAN_FLAG='0' "
                + "GROUP BY WIP_PIECE_NO "
                + "HAVING COUNT(WIP_EXT_PIECE_NO)>=2");
        data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER "
                + "SET WIP_PRIORITY_HOLD_CAN_FLAG=3,WIP_PIECE_STAGE='NEEDLING',WIP_STATUS='MENDED' "
                + "WHERE WIP_PIECE_NO IN (SELECT WIP_PIECE_NO FROM PRODUCTION.TMP_UPDATE_FLAG_MENDING) "
                + "AND WIP_PRIORITY_HOLD_CAN_FLAG=1 AND COALESCE(WIP_REJECTED_FLAG,0)=0");

    }

    // on insert checks piece no already exist in db
    public boolean checkPieceNoInDB(String pPieceNo) {
        int count = data.getIntValueFromDB("SELECT COUNT(PROD_PIECE_NO) FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'MENDING' AND PROD_PIECE_NO='" + pPieceNo + "' AND PROD_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND PROD_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'");
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    // on update checks piece no already exist in db
    public boolean checkPieceNoInDB(String pPieceNo, String pProdDate) {
        int count = data.getIntValueFromDB("SELECT COUNT(PROD_PIECE_NO) FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'MENDING' AND PROD_PIECE_NO='" + pPieceNo + "' AND PROD_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND PROD_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'");
        if (count >= 1) {
            int counter = 0;
            try {
                Connection Conn = data.getConn();
                Statement stTmp = Conn.createStatement();
                ResultSet rsTmp = stTmp.executeQuery("SELECT PROD_DATE FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT='MENDING' AND PROD_PIECE_NO='" + pPieceNo + "' AND PROD_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND PROD_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'");
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

    //checks production document no already exist in db
    public boolean checkProductionDocumentNoInDB(String pProdDocNo) {
        int count = data.getIntValueFromDB("SELECT COUNT(PROD_DOC_NO) FROM PRODUCTION.`FELT_PROD_DATA` WHERE PROD_DEPT = 'MENDING' AND PROD_DOC_NO='" + pProdDocNo + "'");
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
    public TReportWriter.SimpleDataProvider.TTable getReportData(String docNo) {
        TReportWriter.SimpleDataProvider.TTable objData = new TReportWriter.SimpleDataProvider.TTable();
        objData.AddColumn("SR_NO");
        objData.AddColumn("FORM_NO");
        objData.AddColumn("PROD_DATE");
        objData.AddColumn("DOC_NO");
        objData.AddColumn("PIECE_NO");
        objData.AddColumn("STYLE");
        objData.AddColumn("GROUP");
        objData.AddColumn("SYN_PER");
        objData.AddColumn("PARTY_NAME");
        objData.AddColumn("WEIGHT");
        objData.AddColumn("ORDER_LENGTH");
        objData.AddColumn("ORDER_WIDTH");
        objData.AddColumn("ORDER_GSM");
        objData.AddColumn("ORDER_GROUP");
        objData.AddColumn("DAY_WEIGHT");
        objData.AddColumn("PREVIOUS_WEIGHT");
        objData.AddColumn("BASE_WEIGHT");
        objData.AddColumn("CLOTH_WEIGHT");
        objData.AddColumn("TOTAL_WEIGHT");

        try {
            TReportWriter.SimpleDataProvider.TRow objRow = objData.newRow();

            //String str ="SELECT PROD_DATE,PROD_FORM_NO,PROD_DOC_NO,PROD_PIECE_NO,STYLE,GRP,SYN_PER,COALESCE(PARTY_NAME,'') PARTY_NAME, WEIGHT,LNGTH,WIDTH,GSQ,GRUP,PREVIOUS_WEIGHT,DAY_WEIGHT,TOTAL_WEIGHT,BASE_WEIGHT,CLOTH_WEIGHT FROM (SELECT PROD_DATE,PROD_FORM_NO,PROD_DOC_NO,REPLACE(PROD_PIECE_NO,' ','') PROD_PIECE_NO,PROD_PARTY_CODE,WEIGHT,A.GROUP GRP FROM PRODUCTION.FELT_PROD_DATA A WHERE PROD_DOC_NO = '"+docNo+"'  AND PROD_DEPT='MENDING' ) D LEFT JOIN (SELECT REPLACE(BALNK,' ','') AS STYLE,GRUP, GSQ,LNGTH,WIDTH,PRODUCT_CODE,PIECE_NO,PARTY_CD FROM PRODUCTION.FELT_ORDER_MASTER) O ON O.PARTY_CD = D.PROD_PARTY_CODE AND O.PIECE_NO +0 = D.PROD_PIECE_NO +0 LEFT JOIN (SELECT PARTY_CODE, TRIM(CONCAT(PARTY_CODE,',',PARTY_NAME)) AS PARTY_NAME  FROM D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010) AS P ON P.PARTY_CODE = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS PREVIOUS_WEIGHT FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT = 'MENDING' AND MONTH(PROD_DATE)=SUBSTRING('"+docNo+"',8,2) AND YEAR(PROD_DATE)=SUBSTRING('"+docNo+"',4,4) AND SUBSTRING(PROD_DOC_NO,4,8)<SUBSTRING('"+docNo+"',4,8) AND APPROVED=1) AS PW ON O.PARTY_CD = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS DAY_WEIGHT FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT = 'MENDING' AND PROD_DOC_NO='"+docNo+"' AND APPROVED=1) AS DW ON O.PARTY_CD = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS TOTAL_WEIGHT FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT = 'MENDING' AND MONTH(PROD_DATE)=SUBSTRING('"+docNo+"',8,2) AND YEAR(PROD_DATE)=SUBSTRING('"+docNo+"',4,4) AND SUBSTRING(PROD_DOC_NO,4,8)<=SUBSTRING('"+docNo+"',4,8) AND APPROVED=1) AS TW ON O.PARTY_CD = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(A.WEIGHT),0) AS BASE_WEIGHT FROM PRODUCTION.FELT_PROD_DATA A, PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO +0 = PROD_PIECE_NO +0 AND PROD_DEPT='MENDING' AND MONTH(PROD_DATE)=SUBSTRING('"+docNo+"',8,2) AND YEAR(PROD_DATE)=SUBSTRING('"+docNo+"',4,4) AND SUBSTRING(PROD_DOC_NO,4,8)<=SUBSTRING('"+docNo+"',4,8) AND APPROVED=1 AND SUBSTRING(PROD_DOC_NO,1,3)='FMB')  AS BW ON O.PARTY_CD = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(A.WEIGHT),0) AS CLOTH_WEIGHT FROM PRODUCTION.FELT_PROD_DATA A, PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO +0 = PROD_PIECE_NO +0 AND PROD_DEPT='MENDING' AND MONTH(PROD_DATE)=SUBSTRING('"+docNo+"',8,2) AND YEAR(PROD_DATE)=SUBSTRING('"+docNo+"',4,4) AND SUBSTRING(PROD_DOC_NO,4,8)<=SUBSTRING('"+docNo+"',4,8) AND APPROVED=1 AND SUBSTRING(PROD_DOC_NO,1,3)='FMC') AS CW ON O.PARTY_CD = D.PROD_PARTY_CODE LEFT JOIN (SELECT ITEM_CODE,SYN_PER FROM PRODUCTION.FELT_RATE_MASTER) AS R ON SUBSTRING(ITEM_CODE,1,6)=PRODUCT_CODE";
            //String str ="SELECT PROD_DATE,PROD_FORM_NO,PROD_DOC_NO,PROD_PIECE_NO,PR_STYLE,GRP,SYN_PER,COALESCE(PARTY_NAME,'') PARTY_NAME, PR_WEIGHT,PR_LENGTH,PR_WIDTH,PR_GSM,GRUP,PREVIOUS_WEIGHT,DAY_WEIGHT,TOTAL_WEIGHT,BASE_WEIGHT,CLOTH_WEIGHT FROM (SELECT PROD_DATE,PROD_FORM_NO,PROD_DOC_NO,REPLACE(PROD_PIECE_NO,' ','') PROD_PIECE_NO,PROD_PARTY_CODE,WEIGHT,A.GROUP GRP FROM PRODUCTION.FELT_PROD_DATA A WHERE PROD_DOC_NO = '"+docNo+"'  AND PROD_DEPT='MENDING' ) D LEFT JOIN PR_STYLE,PR_GROUP AS GRUP,PR_GSM AS GSQ,PR_LENGTH AS LNGTH,PR_WIDTH AS WIDTH,PR_PRODUCT_CODE AS PRODUCT_CODE,PR_PIECE_NO AS PIECE_NO,PR_PARTY_CODE AS PARTY_CD FROM PRODUCTION.FELT_SALES_PIECE_REGISTER) O ON O.PR_PARTY_CODE = D.PROD_PARTY_CODE AND O.PR_PIECE_NO = D.PROD_PIECE_NO LEFT JOIN (SELECT PARTY_CODE, TRIM(CONCAT(PARTY_CODE,',',PARTY_NAME)) AS PARTY_NAME  FROM D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010) AS P ON P.PARTY_CODE = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS PREVIOUS_WEIGHT FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT = 'MENDING' AND MONTH(PROD_DATE)=SUBSTRING('"+docNo+"',8,2) AND YEAR(PROD_DATE)=SUBSTRING('"+docNo+"',4,4) AND SUBSTRING(PROD_DOC_NO,4,8)<SUBSTRING('"+docNo+"',4,8) AND APPROVED=1) AS PW ON O.PR_PARTY_CODE = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS DAY_WEIGHT FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT = 'MENDING' AND PROD_DOC_NO='"+docNo+"' AND APPROVED=1) AS DW ON O.PR_PARTY_CODE = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS TOTAL_WEIGHT FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT = 'MENDING' AND MONTH(PROD_DATE)=SUBSTRING('"+docNo+"',8,2) AND YEAR(PROD_DATE)=SUBSTRING('"+docNo+"',4,4) AND SUBSTRING(PROD_DOC_NO,4,8)<=SUBSTRING('"+docNo+"',4,8) AND APPROVED=1) AS TW ON O.PR_PARTY_CODE = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(A.WEIGHT),0) AS BASE_WEIGHT FROM PRODUCTION.FELT_PROD_DATA A, PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO = PROD_PIECE_NO AND PROD_DEPT='MENDING' AND MONTH(PROD_DATE)=SUBSTRING('"+docNo+"',8,2) AND YEAR(PROD_DATE)=SUBSTRING('"+docNo+"',4,4) AND SUBSTRING(PROD_DOC_NO,4,8)<=SUBSTRING('"+docNo+"',4,8) AND APPROVED=1 AND SUBSTRING(PROD_DOC_NO,1,3)='FMB')  AS BW ON O.PR_PARTY_CODE = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(A.WEIGHT),0) AS CLOTH_WEIGHT FROM PRODUCTION.FELT_PROD_DATA A, PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO = PROD_PIECE_NO AND PROD_DEPT='MENDING' AND MONTH(PROD_DATE)=SUBSTRING('"+docNo+"',8,2) AND YEAR(PROD_DATE)=SUBSTRING('"+docNo+"',4,4) AND SUBSTRING(PROD_DOC_NO,4,8)<=SUBSTRING('"+docNo+"',4,8) AND APPROVED=1 AND SUBSTRING(PROD_DOC_NO,1,3)='FMC') AS CW ON O.PR_PARTY_CODE = D.PROD_PARTY_CODE LEFT JOIN (SELECT PRODUCT_CODE AS ITEM_CODE,SYN_PER FROM PRODUCTION.FELT_QLT_RATE_MASTER) AS R ON SUBSTRING(ITEM_CODE,1,6)=SUBSTRING(PRODUCT_CODE,1,6)";
            String str = "SELECT PROD_DATE,PROD_FORM_NO,PROD_DOC_NO,PROD_PIECE_NO,STYLE,GRP,SYN_PER,COALESCE(PARTY_NAME,'') PARTY_NAME, WEIGHT,LNGTH,WIDTH,GSQ,GRUP,PREVIOUS_WEIGHT,DAY_WEIGHT,TOTAL_WEIGHT,BASE_WEIGHT,CLOTH_WEIGHT FROM (SELECT PROD_DATE,PROD_FORM_NO,PROD_DOC_NO,REPLACE(PROD_PIECE_NO,' ','') PROD_PIECE_NO,PROD_PARTY_CODE,WEIGHT,A.GROUP GRP FROM PRODUCTION.FELT_PROD_DATA A WHERE PROD_DOC_NO = '" + docNo + "' AND PROD_DEPT='MENDING') D LEFT JOIN (SELECT PR_STYLE AS STYLE,PR_GROUP AS GRUP,PR_GSM AS GSQ,PR_LENGTH AS LNGTH,PR_WIDTH AS WIDTH,PR_PRODUCT_CODE AS PRODUCT_CODE ,PR_PIECE_NO,PR_PARTY_CODE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE (PR_REJECTED_FLAG IN (0) OR PR_REJECTED_FLAG IS NULL)) O ON O.PR_PARTY_CODE = D.PROD_PARTY_CODE AND O.PR_PIECE_NO +0 = D.PROD_PIECE_NO +0 LEFT JOIN (SELECT PARTY_CODE, TRIM(CONCAT(PARTY_CODE,',',PARTY_NAME)) AS PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010) AS P ON P.PARTY_CODE = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS PREVIOUS_WEIGHT FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT = 'MENDING' AND MONTH(PROD_DATE)=SUBSTRING('" + docNo + "',8,2) AND YEAR(PROD_DATE)=SUBSTRING('" + docNo + "',4,4) AND SUBSTRING(PROD_DOC_NO,4,8)<SUBSTRING('" + docNo + "',4,8) AND APPROVED=1) AS PW ON O.PR_PARTY_CODE = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS DAY_WEIGHT FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT = 'MENDING' AND PROD_DOC_NO='" + docNo + "' AND APPROVED=1) AS DW ON O.PR_PARTY_CODE = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(WEIGHT),0) AS TOTAL_WEIGHT FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT = 'MENDING' AND MONTH(PROD_DATE)=SUBSTRING('" + docNo + "',8,2) AND YEAR(PROD_DATE)=SUBSTRING('" + docNo + "',4,4) AND SUBSTRING(PROD_DOC_NO,4,8)<=SUBSTRING('" + docNo + "',4,8) AND APPROVED=1) AS TW ON O.PR_PARTY_CODE = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(A.WEIGHT),0) AS BASE_WEIGHT FROM PRODUCTION.FELT_PROD_DATA A, PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO  = PROD_PIECE_NO AND PROD_DEPT='MENDING' AND MONTH(PROD_DATE)=SUBSTRING('" + docNo + "',8,2) AND YEAR(PROD_DATE)=SUBSTRING('" + docNo + "',4,4) AND SUBSTRING(PROD_DOC_NO,4,8)<=SUBSTRING('" + docNo + "',4,8) AND APPROVED=1 AND SUBSTRING(PROD_DOC_NO,1,3)='FMB')  AS BW ON O.PR_PARTY_CODE = D.PROD_PARTY_CODE LEFT JOIN (SELECT COALESCE(SUM(A.WEIGHT),0) AS CLOTH_WEIGHT FROM PRODUCTION.FELT_PROD_DATA A, PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO = PROD_PIECE_NO AND PROD_DEPT='MENDING' AND MONTH(PROD_DATE)=SUBSTRING('" + docNo + "',8,2) AND YEAR(PROD_DATE)=SUBSTRING('" + docNo + "',4,4) AND SUBSTRING(PROD_DOC_NO,4,8)<=SUBSTRING('" + docNo + "',4,8) AND APPROVED=1 AND SUBSTRING(PROD_DOC_NO,1,3)='FMC') AS CW ON O.PR_PARTY_CODE = D.PROD_PARTY_CODE LEFT JOIN (SELECT PRODUCT_CODE AS ITEM_CODE,SYN_PER FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE APPROVED=1 AND (EFFECTIVE_TO='0000-00-00') OR EFFECTIVE_TO IS NULL) AS R ON SUBSTRING(ITEM_CODE,1,6)=SUBSTRING(PRODUCT_CODE,1,6)";

            ResultSet rsTemp = data.getResult(str);
            int counter = 1;
            while (!rsTemp.isAfterLast()) {
                objRow = objData.newRow();
                objRow.setValue("SR_NO", String.valueOf(counter));
                objRow.setValue("FORM_NO", rsTemp.getString("PROD_FORM_NO"));
                objRow.setValue("PROD_DATE", EITLERPGLOBAL.formatDate(rsTemp.getString("PROD_DATE")));
                objRow.setValue("DOC_NO", rsTemp.getString("PROD_DOC_NO"));
                objRow.setValue("PIECE_NO", rsTemp.getString("PROD_PIECE_NO"));
                objRow.setValue("STYLE", rsTemp.getString("STYLE"));
                objRow.setValue("GROUP", rsTemp.getString("GRP"));
                objRow.setValue("SYN_PER", rsTemp.getString("SYN_PER"));
                objRow.setValue("PARTY_NAME", rsTemp.getString("PARTY_NAME"));
                objRow.setValue("WEIGHT", rsTemp.getString("WEIGHT"));
                objRow.setValue("ORDER_LENGTH", rsTemp.getString("LNGTH"));
                objRow.setValue("ORDER_WIDTH", rsTemp.getString("WIDTH"));
                objRow.setValue("ORDER_GSM", rsTemp.getString("GSQ"));
                objRow.setValue("ORDER_GROUP", rsTemp.getString("GRUP"));
                objRow.setValue("DAY_WEIGHT", rsTemp.getString("DAY_WEIGHT"));
                objRow.setValue("PREVIOUS_WEIGHT", rsTemp.getString("PREVIOUS_WEIGHT"));
                objRow.setValue("BASE_WEIGHT", rsTemp.getString("BASE_WEIGHT"));
                objRow.setValue("CLOTH_WEIGHT", rsTemp.getString("CLOTH_WEIGHT"));
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

}
