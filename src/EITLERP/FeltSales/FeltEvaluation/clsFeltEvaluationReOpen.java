/*
 * clsFeltGroupMaster.java
 *
 * Created on 12, 12, 2016, 5:31 PM
 */
package EITLERP.FeltSales.FeltEvaluation;

import java.util.HashMap;
import java.util.*;
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
import EITLERP.SelectFirstFree;
import EITLERP.clsFirstFree;
import static EITLERP.clsFirstFree.getNextFreeNo;
import javax.swing.JOptionPane;

/**
 * @author Jadeja Rajpalsinh
 */
public class clsFeltEvaluationReOpen {

    public String LastError = "";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private int FFNo = 0;

    public HashMap props;
    public boolean Ready = false;
    public HashMap hmFeltEvaluationDetails;

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
     * Creates new DataFeltGroup
     */
    public clsFeltEvaluationReOpen() {
        LastError = "";
        props = new HashMap();
        props.put("DOC_NO", new Variant(""));
        props.put("DOC_DATE", new Variant(""));
        props.put("PARTY_CODE", new Variant(""));

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

        hmFeltEvaluationDetails = new HashMap();
    }

    public boolean LoadData() {
        Ready = false;
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_EVALUATION_REOPEN GROUP BY DOC_NO ORDER BY DOC_NO");
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
        ResultSet resultSetTemp, resultSetHistory;
        Statement statementTemp, statementHistory;
        try {
            // Production data connection
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_EVALUATION_REOPEN WHERE DOC_NO='1'");

            // Production data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_EVALUATION_REOPEN_H WHERE DOC_NO='1'");

            setAttribute("DOC_NO", getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 612, 260, true));

            //Now Insert records into production tables
            for (int i = 1; i <= hmFeltEvaluationDetails.size(); i++) {
                clsFeltEvaluationDetails ObjFeltEvaluationDetails = (clsFeltEvaluationDetails) hmFeltEvaluationDetails.get(Integer.toString(i));

                //Insert records into production data table
                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                resultSetTemp.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetTemp.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
                resultSetTemp.updateInt("SR_NO", i);

                resultSetTemp.updateString("DELINK_FLAG", ObjFeltEvaluationDetails.getAttribute("DELINK_FLAG").getString());
                resultSetTemp.updateString("DIVERSION_CLOSE_FLAG", ObjFeltEvaluationDetails.getAttribute("DIVERSION_CLOSE_FLAG").getString());
                resultSetTemp.updateString("PIECE_NO", ObjFeltEvaluationDetails.getAttribute("PIECE_NO").getString());
                resultSetTemp.updateString("PIECE_STAGE", ObjFeltEvaluationDetails.getAttribute("PIECE_STAGE").getString());
                resultSetTemp.updateString("MACHINE_NO", ObjFeltEvaluationDetails.getAttribute("MACHINE_NO").getString());
                resultSetTemp.updateString("POSITION_NO", ObjFeltEvaluationDetails.getAttribute("POSITION_NO").getString());
                resultSetTemp.updateString("STYLE_CODE", ObjFeltEvaluationDetails.getAttribute("STYLE_CODE").getString());
                resultSetTemp.updateString("LENGTH", ObjFeltEvaluationDetails.getAttribute("LENGTH").getString());
                resultSetTemp.updateString("WIDTH", ObjFeltEvaluationDetails.getAttribute("WIDTH").getString());
                resultSetTemp.updateString("WEIGHT", ObjFeltEvaluationDetails.getAttribute("WEIGHT").getString());
                resultSetTemp.updateString("GSM", ObjFeltEvaluationDetails.getAttribute("GSM").getString());
                resultSetTemp.updateString("REMARKS", ObjFeltEvaluationDetails.getAttribute("REMARKS").getString());
                resultSetTemp.updateString("OC_MONTH", ObjFeltEvaluationDetails.getAttribute("OC_MONTH").getString());

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

                resultSetHistory.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetHistory.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
                resultSetHistory.updateInt("SR_NO", i);

                resultSetHistory.updateString("DELINK_FLAG", ObjFeltEvaluationDetails.getAttribute("DELINK_FLAG").getString());
                resultSetHistory.updateString("DIVERSION_CLOSE_FLAG", ObjFeltEvaluationDetails.getAttribute("DIVERSION_CLOSE_FLAG").getString());
                resultSetHistory.updateString("PIECE_NO", ObjFeltEvaluationDetails.getAttribute("PIECE_NO").getString());
                resultSetHistory.updateString("PIECE_STAGE", ObjFeltEvaluationDetails.getAttribute("PIECE_STAGE").getString());
                resultSetHistory.updateString("MACHINE_NO", ObjFeltEvaluationDetails.getAttribute("MACHINE_NO").getString());
                resultSetHistory.updateString("POSITION_NO", ObjFeltEvaluationDetails.getAttribute("POSITION_NO").getString());
                resultSetHistory.updateString("STYLE_CODE", ObjFeltEvaluationDetails.getAttribute("STYLE_CODE").getString());
                resultSetHistory.updateString("LENGTH", ObjFeltEvaluationDetails.getAttribute("LENGTH").getString());
                resultSetHistory.updateString("WIDTH", ObjFeltEvaluationDetails.getAttribute("WIDTH").getString());
                resultSetHistory.updateString("WEIGHT", ObjFeltEvaluationDetails.getAttribute("WEIGHT").getString());
                resultSetHistory.updateString("GSM", ObjFeltEvaluationDetails.getAttribute("GSM").getString());
                resultSetHistory.updateString("REMARKS", ObjFeltEvaluationDetails.getAttribute("REMARKS").getString());
                resultSetHistory.updateString("OC_MONTH", ObjFeltEvaluationDetails.getAttribute("OC_MONTH").getString());

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
            ObjFeltProductionApprovalFlow.ModuleID = 612;
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("DOC_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.FELT_SALES_EVALUATION_REOPEN";
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

            // Insert Finishing Date in Piece Register Table To confirm that Finishing has completed
            if (ObjFeltProductionApprovalFlow.Status.equals("H") || ObjFeltProductionApprovalFlow.Status.equals("A")) {
//                updatePieceRegisterS(getAttribute("PRODUCTION_DOCUMENT_NO").getString());
            }
            //ADdata.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER P,PRODUCTION.FELT_PROD_DATA D SET PR_WIP_STATUS='FINISHED' WHERE P.PR_PIECE_NO='"+rsTemp.getString("PROD_PIECE_NO")+"'");
            //data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA SET PR_NDL_DATE=PROD_DATE,PR_PIECE_STAGE='FINISHING',PR_WIP_STATUS='NEEDLED' WHERE PR_PIECE_NO=PROD_PIECE_NO AND PR_PIECE_STAGE!='DIVERTED' AND PR_PARTY_CODE=PROD_PARTY_CODE AND PROD_DEPT='NEEDLING' AND PROD_DOC_NO='"+documentNo+"' AND APPROVED=1");
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
//        int revisionNo = 1;
        try {
            // Production data connection
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_EVALUATION_REOPEN WHERE DOC_NO='1'");

            // Production data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //Get the Maximum Revision No in History Table.
//            resultSetHistory=statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_SALES_EVALUATION_REOPEN_H WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
//            resultSetHistory.first();
//            if(resultSetHistory.getRow()>0){
//                revisionNo=resultSetHistory.getInt(1);
//                revisionNo++;
//            }
//            
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_SALES_EVALUATION_REOPEN_H WHERE DOC_NO='" + getAttribute("DOC_NO").getString() + "'");
            RevNo++;

            //delete records from production data table before insert
            statementHistory.execute("DELETE FROM PRODUCTION.FELT_SALES_EVALUATION_REOPEN WHERE DOC_NO='" + getAttribute("DOC_NO").getString() + "'");

            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_EVALUATION_REOPEN_H WHERE DOC_NO='1'");

            //Now Update records into production tables
            for (int i = 1; i <= hmFeltEvaluationDetails.size(); i++) {
                clsFeltEvaluationDetails ObjFeltEvaluationDetails = (clsFeltEvaluationDetails) hmFeltEvaluationDetails.get(Integer.toString(i));

                //Insert records into production data table
                resultSetTemp.moveToInsertRow();

                resultSetTemp.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                resultSetTemp.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetTemp.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
                resultSetTemp.updateInt("SR_NO", i);

                resultSetTemp.updateString("DELINK_FLAG", ObjFeltEvaluationDetails.getAttribute("DELINK_FLAG").getString());
                resultSetTemp.updateString("DIVERSION_CLOSE_FLAG", ObjFeltEvaluationDetails.getAttribute("DIVERSION_CLOSE_FLAG").getString());
                resultSetTemp.updateString("PIECE_NO", ObjFeltEvaluationDetails.getAttribute("PIECE_NO").getString());
                resultSetTemp.updateString("PIECE_STAGE", ObjFeltEvaluationDetails.getAttribute("PIECE_STAGE").getString());
                resultSetTemp.updateString("MACHINE_NO", ObjFeltEvaluationDetails.getAttribute("MACHINE_NO").getString());
                resultSetTemp.updateString("POSITION_NO", ObjFeltEvaluationDetails.getAttribute("POSITION_NO").getString());
                resultSetTemp.updateString("STYLE_CODE", ObjFeltEvaluationDetails.getAttribute("STYLE_CODE").getString());
                resultSetTemp.updateString("LENGTH", ObjFeltEvaluationDetails.getAttribute("LENGTH").getString());
                resultSetTemp.updateString("WIDTH", ObjFeltEvaluationDetails.getAttribute("WIDTH").getString());
                resultSetTemp.updateString("WEIGHT", ObjFeltEvaluationDetails.getAttribute("WEIGHT").getString());
                resultSetTemp.updateString("GSM", ObjFeltEvaluationDetails.getAttribute("GSM").getString());
                resultSetTemp.updateString("REMARKS", ObjFeltEvaluationDetails.getAttribute("REMARKS").getString());
                resultSetTemp.updateString("OC_MONTH", ObjFeltEvaluationDetails.getAttribute("OC_MONTH").getString());

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
                resultSetHistory.updateInt("REVISION_NO", RevNo);

                resultSetHistory.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetHistory.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
                resultSetHistory.updateInt("SR_NO", i);

                resultSetHistory.updateString("DELINK_FLAG", ObjFeltEvaluationDetails.getAttribute("DELINK_FLAG").getString());
                resultSetHistory.updateString("DIVERSION_CLOSE_FLAG", ObjFeltEvaluationDetails.getAttribute("DIVERSION_CLOSE_FLAG").getString());
                resultSetHistory.updateString("PIECE_NO", ObjFeltEvaluationDetails.getAttribute("PIECE_NO").getString());
                resultSetHistory.updateString("PIECE_STAGE", ObjFeltEvaluationDetails.getAttribute("PIECE_STAGE").getString());
                resultSetHistory.updateString("MACHINE_NO", ObjFeltEvaluationDetails.getAttribute("MACHINE_NO").getString());
                resultSetHistory.updateString("POSITION_NO", ObjFeltEvaluationDetails.getAttribute("POSITION_NO").getString());
                resultSetHistory.updateString("STYLE_CODE", ObjFeltEvaluationDetails.getAttribute("STYLE_CODE").getString());
                resultSetHistory.updateString("LENGTH", ObjFeltEvaluationDetails.getAttribute("LENGTH").getString());
                resultSetHistory.updateString("WIDTH", ObjFeltEvaluationDetails.getAttribute("WIDTH").getString());
                resultSetHistory.updateString("WEIGHT", ObjFeltEvaluationDetails.getAttribute("WEIGHT").getString());
                resultSetHistory.updateString("GSM", ObjFeltEvaluationDetails.getAttribute("GSM").getString());
                resultSetHistory.updateString("REMARKS", ObjFeltEvaluationDetails.getAttribute("REMARKS").getString());
                resultSetHistory.updateString("OC_MONTH", ObjFeltEvaluationDetails.getAttribute("OC_MONTH").getString());

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
            ObjFeltProductionApprovalFlow.ModuleID = 612;
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("DOC_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString());
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.FELT_SALES_EVALUATION_REOPEN";
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
                data.Execute("UPDATE PRODUCTION.FELT_SALES_EVALUATION_REOPEN SET REJECTED=0,CHANGED=1 WHERE DOC_NO='" + getAttribute("DOC_NO").getString() + "'");
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
//                updatePieceRegisterS(getAttribute("PRODUCTION_DOCUMENT_NO").getString());
            }
            //--------- Approval Flow Update complete -----------

            // Update Finishing Date in Order Master Table To confirm that Finishing has completed
            if (ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {
                ObjFeltProductionApprovalFlow.finalApproved = false;
                try {

                    ResultSet rsTemp = data.getConn().createStatement().executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_EVALUATION_REOPEN WHERE DOC_NO = '" + getAttribute("DOC_NO").getString() + "' AND DIVERSION_CLOSE_FLAG='1' ");
                    System.out.println("DIVERSION CLOSE PIECE UPDATION ON FINAL APPROVAL");
                    String sql="";
                    while (rsTemp.next()) {
                        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER P,PRODUCTION.FELT_SALES_EVALUATION_REOPEN D SET P.PR_DIVERSION_FLAG='CLOSED',P.PR_DELINK = '',P.PR_OBSOLETE_DATE = '0000-00-00',P.PR_DELINK_REASON = '',P.PR_OC_MONTHYEAR=D.OC_MONTH,P.PR_CURRENT_SCH_MONTH=D.OC_MONTH,P.PR_PIECE_OC_MONTH_IT_REMARK=CONCAT(COALESCE(P.PR_PIECE_OC_MONTH_IT_REMARK,''),'Retain Piece from Evaluation ReOpen. ') WHERE P.PR_PIECE_NO = '" + rsTemp.getString("PIECE_NO") + "' AND D.DOC_NO = '" + getAttribute("DOC_NO").getString() + "' AND P.PR_PIECE_NO=D.PIECE_NO ");
                        data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER P,PRODUCTION.FELT_SALES_EVALUATION_REOPEN D SET P.WIP_OC_MONTHYEAR=D.OC_MONTH,P.WIP_CURRENT_SCH_MONTH=D.OC_MONTH,P.WIP_PIECE_IT_DEPT_REMARK=CONCAT(COALESCE(P.WIP_PIECE_IT_DEPT_REMARK,''),'Retain Piece from Evaluation ReOpen. ') WHERE P.WIP_PIECE_NO = '" + rsTemp.getString("PIECE_NO") + "' AND D.DOC_NO = '" + getAttribute("DOC_NO").getString() + "' AND P.WIP_PIECE_NO=D.PIECE_NO ");
                        System.out.println("DIVERSION CLOSE PIECE UPDATED ON PIECE NO : " + rsTemp.getString("PIECE_NO"));
                    }
                    
                    ResultSet rsTemp1 = data.getConn().createStatement().executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_EVALUATION_REOPEN WHERE DOC_NO = '" + getAttribute("DOC_NO").getString() + "' AND DELINK_FLAG='1' ");
                    System.out.println("OBSOLETE PIECE UPDATION ON FINAL APPROVAL");
                    while (rsTemp1.next()) {
//                        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER P,PRODUCTION.FELT_SALES_EVALUATION_REOPEN D SET P.PR_DELINK = 'OBSOLETE',P.PR_OBSOLETE_DATE = '"+EITLERPGLOBAL.getCurrentDateDB()+"',P.PR_DELINK_REASON = 'EVALUATION REOPEN' WHERE P.PR_PIECE_NO = '" + rsTemp1.getString("PIECE_NO") + "' "); On 29/01/2020
                        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER P,PRODUCTION.FELT_SALES_EVALUATION_REOPEN D SET P.PR_DELINK = 'OBSOLETE',P.PR_OBSOLETE_DATE = '"+EITLERPGLOBAL.getCurrentDateDB()+"',P.PR_DELINK_REASON = 'EVALUATION REOPEN' WHERE P.PR_PIECE_NO = '" + rsTemp1.getString("PIECE_NO") + "' AND P.PR_DELINK != 'OBSOLETE' ");
                        sql = "INSERT INTO PRODUCTION.OBSOLETE_MAPPED_UNMAPPED_SCRAP (PIECE_NO, PARTY_CODE, STYLE, PROD_GROUP, GSM, "
                                        + "SYN_PER, UPN_ASSIGN_STATUS, UNMAPPED_REASON, SCRAP_REASON, OBSOLETE_SOURCE, ENTRY_DATE) "
                                        + "SELECT PR_PIECE_NO, PR_PARTY_CODE, PR_STYLE, PR_GROUP, PR_GSM, PR_SYN_PER, PR_OBSOLETE_UPN_ASSIGN_STATUS, "
                                        + "PR_UNMAPPED_REASON, PR_SCRAP_REASON, 'EVALUATION REOPEN', CURDATE() "
                                        + "FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO = '" + rsTemp1.getString("PIECE_NO") + "' ";
                        data.Execute(sql);
                        System.out.println("OBSOLETE PIECE UPDATED ON PIECE NO : " + rsTemp.getString("PIECE_NO"));
                    }

                    rsTemp.close();
                    rsTemp1.close();
                } catch (Exception e) {
                    LastError = e.getMessage();
                    e.printStackTrace();
                }
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

    /*
     * This routine checks whether the item is deletable or not.
     * Criteria is Approved item cannot be delete,
     * and if not approved then user id is checked whether doucment
     * is created by the user. Only creator can delete the document.
     * After checking it deletes the record of selected production date and document no.
     */
    public boolean CanDelete(String docNo, int userID) {
        if (HistoryView) {
            return false;
        }
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";

        try {
            tmpStmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_SALES_EVALUATION_REOPEN WHERE DOC_NO='" + docNo + "' AND  APPROVED=1";
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if (rsTmp.getInt("COUNT") > 0) {  //Group is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=612 AND USER_ID=" + userID + " AND DOC_NO='" + getAttribute("DOC_NO").getString() + "' AND STATUS='W'";
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();

                if (rsTmp.getInt("COUNT") > 0) {
                    strSQL = "DELETE FROM PRODUCTION.FELT_SALES_EVALUATION_REOPEN WHERE DOC_NO='" + docNo + "'";
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

    /*
     * This routine checks and returns whether the item is editable or not.
     * Criteria is Approved item cannot be changed and if not approved then user id is
     * checked whether doucment is waiting for his approval.
     */
    public boolean IsEditable(String docNo, int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";
        try {
            if (HistoryView) {
                return false;
            }
            tmpStmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //First check that document is approved or not
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_SALES_EVALUATION_REOPEN WHERE DOC_NO='" + docNo + "' AND APPROVED=1";
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=612 AND USER_ID=" + userID + " AND DOC_NO='" + docNo + "' AND STATUS='W'";
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
            String strSql = "SELECT * FROM PRODUCTION.FELT_SALES_EVALUATION_REOPEN WHERE " + stringFindQuery;
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

            //Now Populate the collection, first clear the collection
            hmFeltEvaluationDetails.clear();

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            if (HistoryView) {
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_EVALUATION_REOPEN_H WHERE DOC_NO='" + resultSet.getString("DOC_NO") + "'  AND REVISION_NO=" + RevNo);
            } else {
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_EVALUATION_REOPEN WHERE DOC_NO='" + resultSet.getString("DOC_NO") + "' ORDER BY DOC_NO,SR_NO ");
            }
            resultSetTemp.first();

            while (!resultSetTemp.isAfterLast()) {
                serialNoCounter++;
                clsFeltEvaluationDetails ObjFeltEvaluationDetails = new clsFeltEvaluationDetails();

                if (HistoryView) {
                    RevNo = resultSetTemp.getInt("REVISION_NO");
                    setAttribute("REVISION_NO", resultSetTemp.getInt("REVISION_NO"));
                } else {
                    setAttribute("REVISION_NO", 0);
                    setAttribute("CREATED_BY", resultSetTemp.getInt("CREATED_BY"));
                    setAttribute("CREATED_DATE", resultSetTemp.getString("CREATED_DATE"));
                    setAttribute("MODIFIED_BY", resultSetTemp.getInt("MODIFIED_BY"));
                    setAttribute("MODIFIED_DATE", resultSetTemp.getString("MODIFIED_DATE"));
                    setAttribute("APPROVED", resultSetTemp.getInt("APPROVED"));
                    setAttribute("APPROVED_DATE", resultSetTemp.getString("APPROVED_DATE"));
                    setAttribute("REJECTED", resultSetTemp.getBoolean("REJECTED"));
                    setAttribute("REJECTED_DATE", resultSetTemp.getString("REJECTED_DATE"));
                    setAttribute("CANCELED", resultSetTemp.getInt("CANCELED"));
                }

                setAttribute("DOC_NO", resultSetTemp.getString("DOC_NO"));
                setAttribute("DOC_DATE", resultSetTemp.getString("DOC_DATE"));
                setAttribute("PARTY_CODE", resultSetTemp.getString("PARTY_CODE"));
                setAttribute("HIERARCHY_ID", resultSetTemp.getInt("HIERARCHY_ID"));

                ObjFeltEvaluationDetails.setAttribute("DELINK_FLAG", resultSetTemp.getString("DELINK_FLAG"));
                ObjFeltEvaluationDetails.setAttribute("DIVERSION_CLOSE_FLAG", resultSetTemp.getString("DIVERSION_CLOSE_FLAG"));
                ObjFeltEvaluationDetails.setAttribute("PIECE_NO", resultSetTemp.getString("PIECE_NO"));
                ObjFeltEvaluationDetails.setAttribute("PIECE_STAGE", resultSetTemp.getString("PIECE_STAGE"));
                ObjFeltEvaluationDetails.setAttribute("MACHINE_NO", resultSetTemp.getString("MACHINE_NO"));
                ObjFeltEvaluationDetails.setAttribute("POSITION_NO", resultSetTemp.getString("POSITION_NO"));
                ObjFeltEvaluationDetails.setAttribute("STYLE_CODE", resultSetTemp.getString("STYLE_CODE"));
                ObjFeltEvaluationDetails.setAttribute("LENGTH", resultSetTemp.getString("LENGTH"));
                ObjFeltEvaluationDetails.setAttribute("WIDTH", resultSetTemp.getString("WIDTH"));
                ObjFeltEvaluationDetails.setAttribute("WEIGHT", resultSetTemp.getString("WEIGHT"));
                ObjFeltEvaluationDetails.setAttribute("GSM", resultSetTemp.getString("GSM"));
                ObjFeltEvaluationDetails.setAttribute("REMARKS", resultSetTemp.getString("REMARKS"));
                ObjFeltEvaluationDetails.setAttribute("OC_MONTH", resultSetTemp.getString("OC_MONTH"));

                hmFeltEvaluationDetails.put(Integer.toString(serialNoCounter), ObjFeltEvaluationDetails);
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

    public boolean ShowHistory(String docNo) {
        Ready = false;
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_EVALUATION_REOPEN_H WHERE DOC_NO='" + docNo + "'");
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
            rsTmp = stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_EVALUATION_REOPEN_H WHERE DOC_NO='" + dpNo + "' GROUP BY REVISION_NO ");

            while (rsTmp.next()) {
                clsFeltEvaluationReOpen ObjFeltGroup = new clsFeltEvaluationReOpen();

                ObjFeltGroup.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                ObjFeltGroup.setAttribute("UPDATED_BY", rsTmp.getInt("UPDATED_BY"));
                ObjFeltGroup.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                ObjFeltGroup.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                ObjFeltGroup.setAttribute("APPROVER_REMARKS", rsTmp.getString("APPROVER_REMARKS"));
                ObjFeltGroup.setAttribute("FROM_IP", rsTmp.getString("FROM_IP"));

                hmHistoryList.put(Integer.toString(hmHistoryList.size() + 1), ObjFeltGroup);
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
        int Counter = 0;

        try {

            if (pOrder == EITLERPGLOBAL.OnRecivedDate) {
                strSQL = "SELECT DISTINCT H.DOC_NO,H.DOC_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_SALES_EVALUATION_REOPEN H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=612 AND CANCELED=0 ORDER BY RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT H.DOC_NO,H.DOC_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_SALES_EVALUATION_REOPEN H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=612 AND CANCELED=0 ORDER BY DOC_NO";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT H.DOC_NO,H.DOC_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_SALES_EVALUATION_REOPEN H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=612 AND CANCELED=0 ORDER BY DOC_NO";
            }

            ResultSet rsTmp = data.getConn().createStatement().executeQuery(strSQL);

            Counter = 0;
            while (rsTmp.next()) {

                Counter = Counter + 1;
                clsFeltEvaluationReOpen ObjDoc = new clsFeltEvaluationReOpen();

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
        int count = data.getIntValueFromDB("SELECT COUNT(PKG_PIECE_NO) FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL WHERE PKG_PIECE_NO='" + pieceNo + "' AND PKG_BALE_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND PKG_BALE_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'");
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    // checks piece no already exist in db in edit mode
    public boolean checkPieceNoInDB(String pieceNo, String baleNo, String baleDate) {
        int count = data.getIntValueFromDB("SELECT COUNT(PKG_PIECE_NO) FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL WHERE PKG_PIECE_NO='" + pieceNo + "' AND PKG_BALE_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND PKG_BALE_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'");
        if (count >= 1) {
            int counter = 0;
            try {
                Connection Conn = data.getConn();
                Statement stTmp = Conn.createStatement();
                ResultSet rsTmp = stTmp.executeQuery("SELECT PKG_BALE_DATE,GROUP_CODE FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL WHERE PKG_PIECE_NO='" + pieceNo + "' AND PKG_BALE_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND PKG_BALE_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'");
                while (rsTmp.next()) {
                    if (rsTmp.getString("PKG_BALE_DATE").equals(EITLERPGLOBAL.formatDateDB(baleDate)) && rsTmp.getString("GROUP_CODE").equals(baleNo)) {
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

    public static String getpartyname(String pcode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String partyname = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + pcode + "' AND MAIN_ACCOUNT_CODE=210010 AND APPROVED=1 AND CANCELLED=0 ");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                partyname = rsTmp.getString("PARTY_NAME");
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

            return partyname;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getcritical(String pcode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String critical = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT AMOUNT_LIMIT FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + pcode + "' AND MAIN_ACCOUNT_CODE=210010 AND APPROVED=1 AND CANCELLED=0");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                critical = rsTmp.getString("AMOUNT_LIMIT");
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

            return critical;
        } catch (Exception e) {
            return "";
        }

    }

    public static void CancelDoc(String pDocNo) {
        ResultSet rsTmp = null;

        if (CanCancel(pDocNo)) {

            boolean Approved = false;

            try {
                rsTmp = data.getResult("SELECT APPROVED FROM PRODUCTION.FELT_SALES_EVALUATION_REOPEN WHERE DOC_NO='" + pDocNo + "' ");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    Approved = rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();

                if (!Approved) {
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='" + pDocNo + "' AND MODULE_ID=612");
                }
                data.Execute("UPDATE PRODUCTION.FELT_SALES_EVALUATION_REOPEN SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='" + pDocNo + "'");

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
            System.out.println("SELECT DOC_NO FROM PRODUCTION.FELT_SALES_EVALUATION_REOPEN WHERE DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");
            rsTmp = stTmp.executeQuery("SELECT DOC_NO FROM PRODUCTION.FELT_SALES_EVALUATION_REOPEN WHERE DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");

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

    public HashMap getPieceList(String pDocNo, String pPartyCode) {
        HashMap hmPieceList = new HashMap();
        String evcSQL = "";
        String mcNo = "";
        String poNo = "";

        if (pDocNo.startsWith("FPA")) {
            evcSQL = "SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE = '" + pPartyCode + "' AND PR_PIECE_STAGE IN ('WEAVING','MENDING','NEEDLING','FINISHING','IN STOCK')  ORDER BY PR_PIECE_STAGE,PR_PIECE_NO ";
        }

        if (pDocNo.startsWith("PMA")) {
            mcNo = data.getStringValueFromDB("SELECT MACHINE_NO FROM PRODUCTION.FELT_PARTY_MACHINE_POSITION_CLOSURE WHERE DOC_NO = '" + pDocNo + "'");
            evcSQL = "SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE = '" + pPartyCode + "' AND PR_MACHINE_NO = " + mcNo + " AND PR_PIECE_STAGE IN ('WEAVING','MENDING','NEEDLING','FINISHING','IN STOCK')  ORDER BY PR_PIECE_STAGE,PR_PIECE_NO ";
        }

        if (pDocNo.startsWith("MPA")) {
            mcNo = data.getStringValueFromDB("SELECT MACHINE_NO FROM PRODUCTION.FELT_PARTY_MACHINE_POSITION_CLOSURE WHERE DOC_NO = '" + pDocNo + "'");
            poNo = data.getStringValueFromDB("SELECT POSITION_NO FROM PRODUCTION.FELT_PARTY_MACHINE_POSITION_CLOSURE WHERE DOC_NO = '" + pDocNo + "'");
            evcSQL = "SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE = '" + pPartyCode + "' AND PR_MACHINE_NO = " + mcNo + " AND PR_POSITION_NO = " + poNo + " AND PR_PIECE_STAGE IN ('WEAVING','MENDING','NEEDLING','FINISHING','IN STOCK')  ORDER BY PR_PIECE_STAGE,PR_PIECE_NO ";
        }

        System.out.println("evcSQL = " + evcSQL);

        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(evcSQL);
            Ready = true;

            while (resultSet.next()) {
                clsFeltEvaluationReOpen piece = new clsFeltEvaluationReOpen();
                boolean flag = true;

                if (flag) {
                    piece.setAttribute("DOC_NO", "");
                    piece.setAttribute("DOC_DATE", "");
                    piece.setAttribute("PARTY_CODE", resultSet.getString("PR_PARTY_CODE"));
                    piece.setAttribute("PIECE_NO", resultSet.getString("PR_PIECE_NO"));
                    piece.setAttribute("PIECE_STAGE", resultSet.getString("PR_PIECE_STAGE"));
                    piece.setAttribute("MACHINE_NO", resultSet.getString("PR_MACHINE_NO"));
                    piece.setAttribute("POSITION_NO", resultSet.getString("PR_POSITION_NO"));
                    piece.setAttribute("STYLE_CODE", resultSet.getString("PR_STYLE"));
                    piece.setAttribute("LENGTH", resultSet.getString("PR_LENGTH"));
                    piece.setAttribute("WIDTH", resultSet.getString("PR_WIDTH"));
                    piece.setAttribute("WEIGHT", resultSet.getString("PR_THORITICAL_WEIGHT"));
                    piece.setAttribute("GSM", resultSet.getString("PR_GSM"));

                    hmPieceList.put(hmPieceList.size() + 1, piece);
                }
            }
            return hmPieceList;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error on fetch data : " + e.getMessage());
            e.printStackTrace();
            LastError = e.getMessage();
            return hmPieceList;
        }

    }

}
