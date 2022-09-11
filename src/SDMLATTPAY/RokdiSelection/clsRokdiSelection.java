/*
 * clsRokdiSelection.java
 *
 * Created on August 22, 2013, 11:20 AM
 */
package SDMLATTPAY.RokdiSelection;

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
//import EITLERP.Production.clsFeltProductionApprovalFlow;

/**
 *
 * @author Vivek Kumar
 */
public class clsRokdiSelection {

    public String LastError = "";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public HashMap props;
    public boolean Ready = false;
    //Felt Production Finishing Details Collection
    public HashMap hmRokdiSelectionDataDetails;

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
    public clsRokdiSelection() {
        LastError = "";
        props = new HashMap();
        props.put("DOC_NO", new Variant(""));
        props.put("DOC_DATE", new Variant(""));        
        props.put("ROKDI_FROM_DATE", new Variant(""));        
        props.put("ROKDI_TO_DATE", new Variant(""));
        props.put("ROKDI_DEPT", new Variant(""));
        
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
        props.put("CANCELED", new Variant(false));

        hmRokdiSelectionDataDetails = new HashMap();
    }

    public boolean LoadData() {
        Ready = false;
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery("SELECT * FROM SDMLATTPAY.ROKDI_SELECTION_DATA GROUP BY DOC_NO ORDER BY CREATED_DATE,DOC_NO");
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
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  SDMLATTPAY.ROKDI_SELECTION_DATA WHERE DOC_NO='1'");

            // Production data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM SDMLATTPAY.ROKDI_SELECTION_DATA_H WHERE DOC_NO='1'");

            setAttribute("DOC_NO", getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 862, 376, true));

            //Now Insert records into production tables
            for (int i = 1; i <= hmRokdiSelectionDataDetails.size(); i++) {
                clsRokdiSelectionDetails ObjRokdiSelectionDataDetails = (clsRokdiSelectionDetails) hmRokdiSelectionDataDetails.get(Integer.toString(i));

                //Insert records into production data table
                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetTemp.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                
                resultSetTemp.updateString("ROKDI_FROM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ROKDI_FROM_DATE").getString()));
                resultSetTemp.updateString("ROKDI_TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ROKDI_TO_DATE").getString()));
                resultSetTemp.updateString("ROKDI_DEPT", getAttribute("ROKDI_DEPT").getString());
                
                resultSetTemp.updateString("SR_NO", ObjRokdiSelectionDataDetails.getAttribute("SR_NO").getString());                
                resultSetTemp.updateString("ROKDI_CONSIDER",ObjRokdiSelectionDataDetails.getAttribute("ROKDI_CONSIDER").getString());
                resultSetTemp.updateString("ROKDI_NOT_CONSIDER",ObjRokdiSelectionDataDetails.getAttribute("ROKDI_NOT_CONSIDER").getString());
                resultSetTemp.updateString("ROKDI_REMARK",ObjRokdiSelectionDataDetails.getAttribute("ROKDI_REMARK").getString());
                resultSetTemp.updateString("EMP_ID", ObjRokdiSelectionDataDetails.getAttribute("EMP_ID").getString());
                resultSetTemp.updateString("EMP_NAME", ObjRokdiSelectionDataDetails.getAttribute("EMP_NAME").getString());
                resultSetTemp.updateString("EMP_DEPT", ObjRokdiSelectionDataDetails.getAttribute("EMP_DEPT").getString());
                resultSetTemp.updateString("ROKDI_DATE", EITLERPGLOBAL.formatDateDB(ObjRokdiSelectionDataDetails.getAttribute("ROKDI_DATE").getString()));
                resultSetTemp.updateString("ROKDI_SHIFT", ObjRokdiSelectionDataDetails.getAttribute("ROKDI_SHIFT").getString());
                resultSetTemp.updateString("PUNCH_FROM_DATE", EITLERPGLOBAL.formatDateDB(ObjRokdiSelectionDataDetails.getAttribute("PUNCH_FROM_DATE").getString()));
                resultSetTemp.updateString("PUNCH_FROM_TIME", ObjRokdiSelectionDataDetails.getAttribute("PUNCH_FROM_TIME").getString());
                resultSetTemp.updateString("PUNCH_TO_DATE", EITLERPGLOBAL.formatDateDB(ObjRokdiSelectionDataDetails.getAttribute("PUNCH_TO_DATE").getString()));
                resultSetTemp.updateString("PUNCH_TO_TIME", ObjRokdiSelectionDataDetails.getAttribute("PUNCH_TO_TIME").getString());
                resultSetTemp.updateString("TIME_DIFFERENCE",ObjRokdiSelectionDataDetails.getAttribute("TIME_DIFFERENCE").getString());
                resultSetTemp.updateString("SYS_ROKDI_HOURS",ObjRokdiSelectionDataDetails.getAttribute("SYS_ROKDI_HOURS").getString());
                resultSetTemp.updateString("DEPT_ROKDI_HOURS",ObjRokdiSelectionDataDetails.getAttribute("DEPT_ROKDI_HOURS").getString());
                resultSetTemp.updateString("DEPT_ROKDI_REMARK",ObjRokdiSelectionDataDetails.getAttribute("DEPT_ROKDI_REMARK").getString());
                resultSetTemp.updateString("TK_ROKDI_HOURS",ObjRokdiSelectionDataDetails.getAttribute("TK_ROKDI_HOURS").getString());
                resultSetTemp.updateString("TK_ROKDI_REMARK",ObjRokdiSelectionDataDetails.getAttribute("TK_ROKDI_REMARK").getString());
                resultSetTemp.updateString("AUDIT_ROKDI_REMARK",ObjRokdiSelectionDataDetails.getAttribute("AUDIT_ROKDI_REMARK").getString());
                
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
                resultSetHistory.updateInt("UPDATED_BY", (int) getAttribute("UPDATED_BY").getVal());
                resultSetHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
                resultSetHistory.updateString("APPROVER_REMARKS", getAttribute("FROM_REMARKS").getString());
                                
                resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetHistory.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                
                resultSetHistory.updateString("ROKDI_FROM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ROKDI_FROM_DATE").getString()));
                resultSetHistory.updateString("ROKDI_TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ROKDI_TO_DATE").getString()));
                resultSetHistory.updateString("ROKDI_DEPT", getAttribute("ROKDI_DEPT").getString());
                
                resultSetHistory.updateString("SR_NO", ObjRokdiSelectionDataDetails.getAttribute("SR_NO").getString());                
                resultSetHistory.updateString("ROKDI_CONSIDER",ObjRokdiSelectionDataDetails.getAttribute("ROKDI_CONSIDER").getString());
                resultSetHistory.updateString("ROKDI_NOT_CONSIDER",ObjRokdiSelectionDataDetails.getAttribute("ROKDI_NOT_CONSIDER").getString());
                resultSetHistory.updateString("ROKDI_REMARK",ObjRokdiSelectionDataDetails.getAttribute("ROKDI_REMARK").getString());
                resultSetHistory.updateString("EMP_ID", ObjRokdiSelectionDataDetails.getAttribute("EMP_ID").getString());
                resultSetHistory.updateString("EMP_NAME", ObjRokdiSelectionDataDetails.getAttribute("EMP_NAME").getString());
                resultSetHistory.updateString("EMP_DEPT", ObjRokdiSelectionDataDetails.getAttribute("EMP_DEPT").getString());
                resultSetHistory.updateString("ROKDI_DATE", EITLERPGLOBAL.formatDateDB(ObjRokdiSelectionDataDetails.getAttribute("ROKDI_DATE").getString()));
                resultSetHistory.updateString("ROKDI_SHIFT", ObjRokdiSelectionDataDetails.getAttribute("ROKDI_SHIFT").getString());
                resultSetHistory.updateString("PUNCH_FROM_DATE", EITLERPGLOBAL.formatDateDB(ObjRokdiSelectionDataDetails.getAttribute("PUNCH_FROM_DATE").getString()));
                resultSetHistory.updateString("PUNCH_FROM_TIME", ObjRokdiSelectionDataDetails.getAttribute("PUNCH_FROM_TIME").getString());
                resultSetHistory.updateString("PUNCH_TO_DATE", EITLERPGLOBAL.formatDateDB(ObjRokdiSelectionDataDetails.getAttribute("PUNCH_TO_DATE").getString()));
                resultSetHistory.updateString("PUNCH_TO_TIME", ObjRokdiSelectionDataDetails.getAttribute("PUNCH_TO_TIME").getString());
                resultSetHistory.updateString("TIME_DIFFERENCE",ObjRokdiSelectionDataDetails.getAttribute("TIME_DIFFERENCE").getString());
                resultSetHistory.updateString("SYS_ROKDI_HOURS",ObjRokdiSelectionDataDetails.getAttribute("SYS_ROKDI_HOURS").getString());
                resultSetHistory.updateString("DEPT_ROKDI_HOURS",ObjRokdiSelectionDataDetails.getAttribute("DEPT_ROKDI_HOURS").getString());
                resultSetHistory.updateString("DEPT_ROKDI_REMARK",ObjRokdiSelectionDataDetails.getAttribute("DEPT_ROKDI_REMARK").getString());
                resultSetHistory.updateString("TK_ROKDI_HOURS",ObjRokdiSelectionDataDetails.getAttribute("TK_ROKDI_HOURS").getString());
                resultSetHistory.updateString("TK_ROKDI_REMARK",ObjRokdiSelectionDataDetails.getAttribute("TK_ROKDI_REMARK").getString());
                resultSetHistory.updateString("AUDIT_ROKDI_REMARK",ObjRokdiSelectionDataDetails.getAttribute("AUDIT_ROKDI_REMARK").getString());
                
                resultSetHistory.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                resultSetHistory.updateString("CREATED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateInt("MODIFIED_BY", 0);
                resultSetHistory.updateString("MODIFIED_DATE", "0000-00-00");
                resultSetHistory.updateBoolean("APPROVED", false);
                resultSetHistory.updateString("APPROVED_DATE", "0000-00-00");
                resultSetHistory.updateBoolean("REJECTED", false);
                resultSetHistory.updateString("REJECTED_DATE", "0000-00-00");
                resultSetHistory.updateInt("CANCELED", 0);
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
            SDMLATTPAY.ApprovalFlow ObjFlow = new SDMLATTPAY.ApprovalFlow();
            ObjFlow.ModuleID = 862; 
            ObjFlow.DocNo = getAttribute("DOC_NO").getString();
            ObjFlow.DocDate = EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString());
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "SDMLATTPAY.ROKDI_SELECTION_DATA";
            ObjFlow.IsCreator = true;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName = "DOC_NO";
            ObjFlow.CompanyID = EITLERPGLOBAL.gCompanyID;

            if (ObjFlow.Status.equals("H")) {
                ObjFlow.Status = "A";
                ObjFlow.To = ObjFlow.From;
                ObjFlow.UpdateFlow();
            } else {
                if (!ObjFlow.UpdateFlow()) {
                    LastError = ObjFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------

            // Insert Finishing Date in Piece Register Table To confirm that Finishing has completed
            if (ObjFlow.Status.equals("H") || ObjFlow.Status.equals("A")) {
                
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
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  SDMLATTPAY.ROKDI_SELECTION_DATA WHERE DOC_NO='1'");

            // Production data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //Get the Maximum Revision No in History Table.
            resultSetHistory = statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM SDMLATTPAY.ROKDI_SELECTION_DATA_H WHERE DOC_NO='" + getAttribute("DOC_NO").getString() + "'");
            resultSetHistory.first();
            if (resultSetHistory.getRow() > 0) {
                revisionNo = resultSetHistory.getInt(1);
                revisionNo++;
            }
  
            //delete records from production data table before insert
            statementHistory.execute("DELETE FROM SDMLATTPAY.ROKDI_SELECTION_DATA WHERE DOC_NO='" + getAttribute("DOC_NO").getString() + "'");

            resultSetHistory = statementHistory.executeQuery("SELECT * FROM SDMLATTPAY.ROKDI_SELECTION_DATA_H WHERE DOC_NO='1'");

            //Now Update records into production tables
            for (int i = 1; i <= hmRokdiSelectionDataDetails.size(); i++) {
                clsRokdiSelectionDetails ObjRokdiSelectionDataDetails = (clsRokdiSelectionDetails) hmRokdiSelectionDataDetails.get(Integer.toString(i));

                resultSetTemp.moveToInsertRow();                
                resultSetTemp.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetTemp.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                
                resultSetTemp.updateString("ROKDI_FROM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ROKDI_FROM_DATE").getString()));
                resultSetTemp.updateString("ROKDI_TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ROKDI_TO_DATE").getString()));
                resultSetTemp.updateString("ROKDI_DEPT", getAttribute("ROKDI_DEPT").getString());
                
                resultSetTemp.updateString("SR_NO", ObjRokdiSelectionDataDetails.getAttribute("SR_NO").getString());                
                resultSetTemp.updateString("ROKDI_CONSIDER",ObjRokdiSelectionDataDetails.getAttribute("ROKDI_CONSIDER").getString());
                resultSetTemp.updateString("ROKDI_NOT_CONSIDER",ObjRokdiSelectionDataDetails.getAttribute("ROKDI_NOT_CONSIDER").getString());
                resultSetTemp.updateString("ROKDI_REMARK",ObjRokdiSelectionDataDetails.getAttribute("ROKDI_REMARK").getString());
                resultSetTemp.updateString("EMP_ID", ObjRokdiSelectionDataDetails.getAttribute("EMP_ID").getString());
                resultSetTemp.updateString("EMP_NAME", ObjRokdiSelectionDataDetails.getAttribute("EMP_NAME").getString());
                resultSetTemp.updateString("EMP_DEPT", ObjRokdiSelectionDataDetails.getAttribute("EMP_DEPT").getString());
                resultSetTemp.updateString("ROKDI_DATE", EITLERPGLOBAL.formatDateDB(ObjRokdiSelectionDataDetails.getAttribute("ROKDI_DATE").getString()));
                resultSetTemp.updateString("ROKDI_SHIFT", ObjRokdiSelectionDataDetails.getAttribute("ROKDI_SHIFT").getString());
                resultSetTemp.updateString("PUNCH_FROM_DATE", EITLERPGLOBAL.formatDateDB(ObjRokdiSelectionDataDetails.getAttribute("PUNCH_FROM_DATE").getString()));
                resultSetTemp.updateString("PUNCH_FROM_TIME", ObjRokdiSelectionDataDetails.getAttribute("PUNCH_FROM_TIME").getString());
                resultSetTemp.updateString("PUNCH_TO_DATE", EITLERPGLOBAL.formatDateDB(ObjRokdiSelectionDataDetails.getAttribute("PUNCH_TO_DATE").getString()));
                resultSetTemp.updateString("PUNCH_TO_TIME", ObjRokdiSelectionDataDetails.getAttribute("PUNCH_TO_TIME").getString());
                resultSetTemp.updateString("TIME_DIFFERENCE",ObjRokdiSelectionDataDetails.getAttribute("TIME_DIFFERENCE").getString());
                resultSetTemp.updateString("SYS_ROKDI_HOURS",ObjRokdiSelectionDataDetails.getAttribute("SYS_ROKDI_HOURS").getString());
                resultSetTemp.updateString("DEPT_ROKDI_HOURS",ObjRokdiSelectionDataDetails.getAttribute("DEPT_ROKDI_HOURS").getString());
                resultSetTemp.updateString("DEPT_ROKDI_REMARK",ObjRokdiSelectionDataDetails.getAttribute("DEPT_ROKDI_REMARK").getString());
                resultSetTemp.updateString("TK_ROKDI_HOURS",ObjRokdiSelectionDataDetails.getAttribute("TK_ROKDI_HOURS").getString());
                resultSetTemp.updateString("TK_ROKDI_REMARK",ObjRokdiSelectionDataDetails.getAttribute("TK_ROKDI_REMARK").getString());
                resultSetTemp.updateString("AUDIT_ROKDI_REMARK",ObjRokdiSelectionDataDetails.getAttribute("AUDIT_ROKDI_REMARK").getString());
                
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
                resultSetHistory.updateInt("UPDATED_BY", (int) getAttribute("UPDATED_BY").getVal());
                resultSetHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
                resultSetHistory.updateString("APPROVER_REMARKS", getAttribute("FROM_REMARKS").getString());
                
                resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetHistory.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                
                resultSetHistory.updateString("ROKDI_FROM_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ROKDI_FROM_DATE").getString()));
                resultSetHistory.updateString("ROKDI_TO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ROKDI_TO_DATE").getString()));
                resultSetHistory.updateString("ROKDI_DEPT", getAttribute("ROKDI_DEPT").getString());
                
                resultSetHistory.updateString("SR_NO", ObjRokdiSelectionDataDetails.getAttribute("SR_NO").getString());                
                resultSetHistory.updateString("ROKDI_CONSIDER",ObjRokdiSelectionDataDetails.getAttribute("ROKDI_CONSIDER").getString());
                resultSetHistory.updateString("ROKDI_NOT_CONSIDER",ObjRokdiSelectionDataDetails.getAttribute("ROKDI_NOT_CONSIDER").getString());
                resultSetHistory.updateString("ROKDI_REMARK",ObjRokdiSelectionDataDetails.getAttribute("ROKDI_REMARK").getString());
                resultSetHistory.updateString("EMP_ID", ObjRokdiSelectionDataDetails.getAttribute("EMP_ID").getString());
                resultSetHistory.updateString("EMP_NAME", ObjRokdiSelectionDataDetails.getAttribute("EMP_NAME").getString());
                resultSetHistory.updateString("EMP_DEPT", ObjRokdiSelectionDataDetails.getAttribute("EMP_DEPT").getString());
                resultSetHistory.updateString("ROKDI_DATE", EITLERPGLOBAL.formatDateDB(ObjRokdiSelectionDataDetails.getAttribute("ROKDI_DATE").getString()));
                resultSetHistory.updateString("ROKDI_SHIFT", ObjRokdiSelectionDataDetails.getAttribute("ROKDI_SHIFT").getString());
                resultSetHistory.updateString("PUNCH_FROM_DATE", EITLERPGLOBAL.formatDateDB(ObjRokdiSelectionDataDetails.getAttribute("PUNCH_FROM_DATE").getString()));
                resultSetHistory.updateString("PUNCH_FROM_TIME", ObjRokdiSelectionDataDetails.getAttribute("PUNCH_FROM_TIME").getString());
                resultSetHistory.updateString("PUNCH_TO_DATE", EITLERPGLOBAL.formatDateDB(ObjRokdiSelectionDataDetails.getAttribute("PUNCH_TO_DATE").getString()));
                resultSetHistory.updateString("PUNCH_TO_TIME", ObjRokdiSelectionDataDetails.getAttribute("PUNCH_TO_TIME").getString());
                resultSetHistory.updateString("TIME_DIFFERENCE",ObjRokdiSelectionDataDetails.getAttribute("TIME_DIFFERENCE").getString());
                resultSetHistory.updateString("SYS_ROKDI_HOURS",ObjRokdiSelectionDataDetails.getAttribute("SYS_ROKDI_HOURS").getString());
                resultSetHistory.updateString("DEPT_ROKDI_HOURS",ObjRokdiSelectionDataDetails.getAttribute("DEPT_ROKDI_HOURS").getString());
                resultSetHistory.updateString("DEPT_ROKDI_REMARK",ObjRokdiSelectionDataDetails.getAttribute("DEPT_ROKDI_REMARK").getString());
                resultSetHistory.updateString("TK_ROKDI_HOURS",ObjRokdiSelectionDataDetails.getAttribute("TK_ROKDI_HOURS").getString());
                resultSetHistory.updateString("TK_ROKDI_REMARK",ObjRokdiSelectionDataDetails.getAttribute("TK_ROKDI_REMARK").getString());
                resultSetHistory.updateString("AUDIT_ROKDI_REMARK",ObjRokdiSelectionDataDetails.getAttribute("AUDIT_ROKDI_REMARK").getString());
                
                resultSetHistory.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                resultSetHistory.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
                resultSetHistory.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                resultSetHistory.updateString("MODIFIED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateBoolean("APPROVED", false);
                resultSetHistory.updateString("APPROVED_DATE", "0000-00-00");
                resultSetHistory.updateBoolean("REJECTED", false);
                resultSetHistory.updateString("REJECTED_DATE", "0000-00-00");
                resultSetHistory.updateInt("CANCELED", 0);
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
            SDMLATTPAY.ApprovalFlow ObjFlow = new SDMLATTPAY.ApprovalFlow();
            ObjFlow.ModuleID = 862; 
            ObjFlow.DocNo = getAttribute("DOC_NO").getString();
            ObjFlow.DocDate = EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString());
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName = "SDMLATTPAY.ROKDI_SELECTION_DATA";
            ObjFlow.IsCreator = false;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName = "DOC_NO";
            ObjFlow.CompanyID = EITLERPGLOBAL.gCompanyID;

            if (getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFlow.To = (int) getAttribute("SEND_DOC_TO").getVal();
                ObjFlow.ExplicitSendTo = true;
            }

            //==== Handling Rejected Documents ==========//
            boolean IsRejected = getAttribute("REJECTED").getBool();

            if (IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE SDMLATTPAY.ROKDI_SELECTION_DATA SET REJECTED=0,CHANGED=1 WHERE DOC_NO='" + getAttribute("DOC_NO").getString() + "'");
                
                ObjFlow.IsCreator = true;
            }
            //==========================================//

            if (ObjFlow.Status.equals("H")) {
                if (IsRejected) {
                    ObjFlow.Status = "A";
                    ObjFlow.To = ObjFlow.From;
                    ObjFlow.UpdateFlow();
                }
            } else {
                if (!ObjFlow.UpdateFlow()) {
                    LastError = ObjFlow.LastError;
                }
            }

            if (ObjFlow.Status.equals("H") || ObjFlow.Status.equals("A")) {
                
            }
            //--------- Approval Flow Update complete -----------

            // Update Finishing Date in Order Master Table To confirm that Finishing has completed
            if (ObjFlow.Status.equals("F")) {
                
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.ROKDI_SELECTION_DATA WHERE DOC_NO='" + documentNo + "' AND APPROVED=" + 1;
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=862 AND USER_ID=" + userID + " AND DOC_NO='" + documentNo + "' AND STATUS='W'";
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();

                if (rsTmp.getInt("COUNT") > 0) {
                    strSQL = "DELETE FROM SDMLATTPAY.ROKDI_SELECTION_DATA WHERE DOC_NO='" + documentNo + "'";
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.ROKDI_SELECTION_DATA WHERE DOC_NO='" + documentNo + "' AND APPROVED=1";
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=862 AND USER_ID=" + userID + " AND DOC_NO='" + documentNo + "' AND STATUS='W'";
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
            String strSql = "SELECT * FROM SDMLATTPAY.ROKDI_SELECTION_DATA WHERE 1=1 " + stringFindQuery + " GROUP BY DOC_NO ORDER BY DOC_NO ";
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

            //setAttribute("REVISION_NO",0);
            //setAttribute("PRODUCTION_DATE",resultSet.getString("PROD_DATE"));
            String docNo,docDate;
            docNo = resultSet.getString("DOC_NO");
            docDate = resultSet.getString("DOC_DATE");
            
            //Now Populate the collection, first clear the collection
            //hmRokdiSelectionDataDetails.clear();
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String str = "SELECT * FROM SDMLATTPAY.ROKDI_SELECTION_DATA WHERE DOC_NO = '" + docNo + "' AND DOC_DATE = '" + docDate + "' ORDER BY SR_NO";
            resultSetTemp = statementTemp.executeQuery(str);
            resultSetTemp.first();
            hmRokdiSelectionDataDetails.clear();

            while (!resultSetTemp.isAfterLast()) {
                serialNoCounter++;
                clsRokdiSelectionDetails ObjRokdiSelectionDataDetails = new clsRokdiSelectionDetails();
                setAttribute("REVISION_NO", 0);
                setAttribute("DOC_NO", resultSetTemp.getString("DOC_NO"));
                setAttribute("DOC_DATE", resultSetTemp.getString("DOC_DATE"));
                
                setAttribute("ROKDI_FROM_DATE", resultSetTemp.getString("ROKDI_FROM_DATE"));
                setAttribute("ROKDI_TO_DATE", resultSetTemp.getString("ROKDI_TO_DATE"));
                setAttribute("ROKDI_DEPT", resultSetTemp.getString("ROKDI_DEPT"));
                
                ObjRokdiSelectionDataDetails.setAttribute("SR_NO", resultSetTemp.getString("SR_NO"));
                ObjRokdiSelectionDataDetails.setAttribute("ROKDI_CONSIDER", resultSetTemp.getString("ROKDI_CONSIDER"));
                ObjRokdiSelectionDataDetails.setAttribute("ROKDI_NOT_CONSIDER", resultSetTemp.getString("ROKDI_NOT_CONSIDER"));
                ObjRokdiSelectionDataDetails.setAttribute("ROKDI_REMARK", resultSetTemp.getString("ROKDI_REMARK"));
                ObjRokdiSelectionDataDetails.setAttribute("EMP_ID", resultSetTemp.getString("EMP_ID"));
                ObjRokdiSelectionDataDetails.setAttribute("EMP_NAME", resultSetTemp.getString("EMP_NAME"));
                ObjRokdiSelectionDataDetails.setAttribute("EMP_DEPT", resultSetTemp.getString("EMP_DEPT"));
                ObjRokdiSelectionDataDetails.setAttribute("ROKDI_DATE", resultSetTemp.getString("ROKDI_DATE"));
                ObjRokdiSelectionDataDetails.setAttribute("ROKDI_SHIFT", resultSetTemp.getString("ROKDI_SHIFT"));
                ObjRokdiSelectionDataDetails.setAttribute("PUNCH_FROM_DATE", resultSetTemp.getString("PUNCH_FROM_DATE"));
                ObjRokdiSelectionDataDetails.setAttribute("PUNCH_FROM_TIME", resultSetTemp.getString("PUNCH_FROM_TIME"));
                ObjRokdiSelectionDataDetails.setAttribute("PUNCH_TO_DATE", resultSetTemp.getString("PUNCH_TO_DATE"));
                ObjRokdiSelectionDataDetails.setAttribute("PUNCH_TO_TIME", resultSetTemp.getString("PUNCH_TO_TIME"));
                ObjRokdiSelectionDataDetails.setAttribute("TIME_DIFFERENCE", resultSetTemp.getString("TIME_DIFFERENCE"));
                ObjRokdiSelectionDataDetails.setAttribute("SYS_ROKDI_HOURS", resultSetTemp.getString("SYS_ROKDI_HOURS"));
                ObjRokdiSelectionDataDetails.setAttribute("DEPT_ROKDI_HOURS", resultSetTemp.getString("DEPT_ROKDI_HOURS"));
                ObjRokdiSelectionDataDetails.setAttribute("DEPT_ROKDI_REMARK", resultSetTemp.getString("DEPT_ROKDI_REMARK"));
                ObjRokdiSelectionDataDetails.setAttribute("TK_ROKDI_HOURS", resultSetTemp.getString("TK_ROKDI_HOURS"));
                ObjRokdiSelectionDataDetails.setAttribute("TK_ROKDI_REMARK", resultSetTemp.getString("TK_ROKDI_REMARK"));
                ObjRokdiSelectionDataDetails.setAttribute("AUDIT_ROKDI_REMARK", resultSetTemp.getString("AUDIT_ROKDI_REMARK"));
                
                setAttribute("CREATED_BY", resultSetTemp.getInt("CREATED_BY"));
                setAttribute("CREATED_DATE", resultSetTemp.getString("CREATED_DATE"));
                setAttribute("MODIFIED_BY", resultSetTemp.getInt("MODIFIED_BY"));
                setAttribute("MODIFIED_DATE", resultSetTemp.getString("MODIFIED_DATE"));
                setAttribute("APPROVED", resultSet.getInt("APPROVED"));
                setAttribute("APPROVED_DATE", resultSetTemp.getString("APPROVED_DATE"));
                setAttribute("REJECTED", resultSetTemp.getBoolean("REJECTED"));
                setAttribute("REJECTED_DATE", resultSetTemp.getString("REJECTED_DATE"));
                setAttribute("CANCELED", resultSetTemp.getInt("CANCELED"));
                setAttribute("HIERARCHY_ID",resultSetTemp.getInt("HIERARCHY_ID"));

                hmRokdiSelectionDataDetails.put(Integer.toString(serialNoCounter), ObjRokdiSelectionDataDetails);
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
    
    public static HashMap getHistoryList(String DocumentNo) {
        HashMap hmHistoryList = new HashMap();
        Statement stTmp;
        ResultSet rsTmp;
        try {
            stTmp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp = stTmp.executeQuery("SELECT * FROM SDMLATTPAY.ROKDI_SELECTION_DATA_H WHERE DOC_NO='" + DocumentNo + "' GROUP BY REVISION_NO");

            while (rsTmp.next()) {
                clsRokdiSelection ObjRokdiSelectionData = new clsRokdiSelection();

                ObjRokdiSelectionData.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                ObjRokdiSelectionData.setAttribute("UPDATED_BY", rsTmp.getInt("UPDATED_BY"));
                ObjRokdiSelectionData.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                ObjRokdiSelectionData.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                ObjRokdiSelectionData.setAttribute("APPROVER_REMARKS", rsTmp.getString("APPROVER_REMARKS"));
                ObjRokdiSelectionData.setAttribute("FROM_IP", rsTmp.getString("FROM_IP"));

                hmHistoryList.put(Integer.toString(hmHistoryList.size() + 1), ObjRokdiSelectionData);
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
            resultSet = statement.executeQuery("SELECT * FROM SDMLATTPAY.ROKDI_SELECTION_DATA_H WHERE DOC_NO='" + pDocNo + "'");
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
                strSQL = "SELECT DISTINCT MA.DOC_NO,MA.DOC_DATE,SUBSTRING(RECEIVED_DATE,1,10) AS RECEIVED_DATE FROM SDMLATTPAY.ROKDI_SELECTION_DATA MA,SDMLATTPAY.D_COM_DOC_DATA D WHERE MA.DOC_NO=D.DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=862 AND MA.CANCELED=0 ORDER BY RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT MA.DOC_NO,MA.DOC_DATE,SUBSTRING(RECEIVED_DATE,1,10) AS RECEIVED_DATE FROM SDMLATTPAY.ROKDI_SELECTION_DATA MA,SDMLATTPAY.D_COM_DOC_DATA D WHERE MA.DOC_NO=D.DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=862 AND MA.CANCELED=0 ORDER BY MA.DOC_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT MA.DOC_NO,MA.DOC_DATE,SUBSTRING(RECEIVED_DATE,1,10) AS RECEIVED_DATE FROM SDMLATTPAY.ROKDI_SELECTION_DATA MA,SDMLATTPAY.D_COM_DOC_DATA D WHERE MA.DOC_NO=D.DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=862 AND MA.CANCELED=0 ORDER BY MA.DOC_NO";
            }

            ResultSet rsTmp = data.getConn().createStatement().executeQuery(strSQL);

            Counter = 0;
            while (rsTmp.next()) {

                Counter = Counter + 1;
                clsRokdiSelection ObjDoc = new clsRokdiSelection();

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

    public static void CancelDoc(String pDocNo) {
        ResultSet rsTmp = null;

        if (CanCancel(pDocNo)) {

            boolean Approved = false;

            try {
                rsTmp = data.getResult("SELECT APPROVED FROM SDMLATTPAY.ROKDI_SELECTION_DATA WHERE DOC_NO='" + pDocNo + "' ");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    Approved = rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();

                if (!Approved) {
                    data.Execute("DELETE FROM SDMLATTPAY.D_COM_DOC_DATA WHERE DOC_NO='" + pDocNo + "' AND MODULE_ID=862");
                }
                data.Execute("UPDATE SDMLATTPAY.ROKDI_SELECTION_DATA SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='" + pDocNo + "'");

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
            System.out.println("SELECT DOC_NO FROM SDMLATTPAY.ROKDI_SELECTION_DATA WHERE DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");
            rsTmp = stTmp.executeQuery("SELECT DOC_NO FROM SDMLATTPAY.ROKDI_SELECTION_DATA WHERE DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");

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
}