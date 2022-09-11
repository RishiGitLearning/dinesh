/*
 * clsDailyAttDataForm.java
 *
 * Created on August 22, 2013, 11:20 AM
 */
package SDMLATTPAY.GatepassSelection;

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
public class clsGatepassSelection {

    public String LastError = "";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public HashMap props;
    public boolean Ready = false;
    //Felt Production Finishing Details Collection
    public HashMap hmDailyAttDataDetails;

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
    public clsGatepassSelection() {
        LastError = "";
        props = new HashMap();
        props.put("DOC_NO", new Variant(""));
        props.put("DOC_DATE", new Variant(""));        
        props.put("GP_MONTH", new Variant(""));
        props.put("GP_YEAR", new Variant(""));
        
        // -- Approval Specific Fields --
        props.put("HIERARCHY_ID", new Variant(0));
        props.put("FROM", new Variant(0));
        props.put("TO", new Variant(0));
        props.put("REMARKS", new Variant(""));
        props.put("APPROVAL_STATUS", new Variant(""));
        props.put("REJECTED_REMARKS", new Variant(""));

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
        props.put("FROM_IP", new Variant(""));

        hmDailyAttDataDetails = new HashMap();
    }

    public boolean LoadData() {
        Ready = false;
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery("SELECT * FROM SDMLATTPAY.GATEPASS_SELECTION_ENTRY GROUP BY DOC_NO ORDER BY CREATED_DATE,DOC_NO");
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
            ResultSet rsIP = data.getResult("SELECT USER()");
            rsIP.first();
            String str = rsIP.getString(1);
            String str_split[] = str.split("@");
            
            // Production data connection
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  SDMLATTPAY.GATEPASS_SELECTION_ENTRY WHERE DOC_NO='1'");

            // Production data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM SDMLATTPAY.GATEPASS_SELECTION_ENTRY_H WHERE DOC_NO='1'");

            setAttribute("DOC_NO", getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 873, 385, true));

            //Now Insert records into production tables
            for (int i = 1; i <= hmDailyAttDataDetails.size(); i++) {
                clsGatepassSelectionDetails ObjDailyAttDataDetails = (clsGatepassSelectionDetails) hmDailyAttDataDetails.get(Integer.toString(i));

                //Insert records into production data table
                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetTemp.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                resultSetTemp.updateString("GP_MONTH", getAttribute("GP_MONTH").getString());
                resultSetTemp.updateString("GP_YEAR", getAttribute("GP_YEAR").getString());
                
                resultSetTemp.updateString("GP_CONSIDER",ObjDailyAttDataDetails.getAttribute("GP_CONSIDER").getString());
                resultSetTemp.updateString("GP_DATE", EITLERPGLOBAL.formatDateDB(ObjDailyAttDataDetails.getAttribute("GP_DATE").getString()));
                resultSetTemp.updateString("GP_EMP_NO",ObjDailyAttDataDetails.getAttribute("GP_EMP_NO").getString());
                resultSetTemp.updateString("GP_EMP_NAME",ObjDailyAttDataDetails.getAttribute("GP_EMP_NAME").getString());
                resultSetTemp.updateString("GP_EMP_DEPT",ObjDailyAttDataDetails.getAttribute("GP_EMP_DEPT").getString());
                resultSetTemp.updateString("GP_EMP_SHIFT",ObjDailyAttDataDetails.getAttribute("GP_EMP_SHIFT").getString());
                resultSetTemp.updateString("GP_EMP_DESN",ObjDailyAttDataDetails.getAttribute("GP_EMP_DESN").getString());
                resultSetTemp.updateString("GP_TYPE",ObjDailyAttDataDetails.getAttribute("GP_TYPE").getString());
                resultSetTemp.updateString("GP_TOL", ObjDailyAttDataDetails.getAttribute("GP_TOL").getString());
                resultSetTemp.updateString("GP_TOA", ObjDailyAttDataDetails.getAttribute("GP_TOA").getString());
                resultSetTemp.updateString("GP_REMARKS",ObjDailyAttDataDetails.getAttribute("GP_REMARKS").getString());
                resultSetTemp.updateString("GP_NATURE_OF_WORK",ObjDailyAttDataDetails.getAttribute("GP_NATURE_OF_WORK").getString());
                resultSetTemp.updateString("GP_DEPT_HEAD",ObjDailyAttDataDetails.getAttribute("GP_DEPT_HEAD").getString());
                resultSetTemp.updateString("GP_TOT_HOURS",ObjDailyAttDataDetails.getAttribute("GP_TOT_HOURS").getString());
                resultSetTemp.updateString("GP_TIME_OF_LEAVING", ObjDailyAttDataDetails.getAttribute("GP_TIME_OF_LEAVING").getString());
                resultSetTemp.updateString("GP_TIME_OF_ARRIVAL", ObjDailyAttDataDetails.getAttribute("GP_TIME_OF_ARRIVAL").getString());
                resultSetTemp.updateString("GP_CATEGORY",ObjDailyAttDataDetails.getAttribute("GP_CATEGORY").getString());
                
                resultSetTemp.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                resultSetTemp.updateString("CREATED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetTemp.updateInt("MODIFIED_BY", 0);
                resultSetTemp.updateString("MODIFIED_DATE", "0000-00-00");
                resultSetTemp.updateBoolean("APPROVED", false);
                resultSetTemp.updateString("APPROVED_DATE", "0000-00-00");
                resultSetTemp.updateBoolean("REJECTED", false);
                resultSetTemp.updateString("REJECTED_DATE", "0000-00-00");
                resultSetTemp.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
                resultSetTemp.updateInt("CANCELED", 0);
                resultSetTemp.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                resultSetTemp.updateInt("CHANGED", 1);
                resultSetTemp.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                resultSetTemp.updateString("FROM_IP", "" + str_split[1]);
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
                resultSetHistory.updateString("GP_MONTH", getAttribute("GP_MONTH").getString());
                resultSetHistory.updateString("GP_YEAR", getAttribute("GP_YEAR").getString());
                
                resultSetHistory.updateString("GP_CONSIDER",ObjDailyAttDataDetails.getAttribute("GP_CONSIDER").getString());
                resultSetHistory.updateString("GP_DATE", EITLERPGLOBAL.formatDateDB(ObjDailyAttDataDetails.getAttribute("GP_DATE").getString()));
                resultSetHistory.updateString("GP_EMP_NO",ObjDailyAttDataDetails.getAttribute("GP_EMP_NO").getString());
                resultSetHistory.updateString("GP_EMP_NAME",ObjDailyAttDataDetails.getAttribute("GP_EMP_NAME").getString());
                resultSetHistory.updateString("GP_EMP_DEPT",ObjDailyAttDataDetails.getAttribute("GP_EMP_DEPT").getString());
                resultSetHistory.updateString("GP_EMP_SHIFT",ObjDailyAttDataDetails.getAttribute("GP_EMP_SHIFT").getString());
                resultSetHistory.updateString("GP_EMP_DESN",ObjDailyAttDataDetails.getAttribute("GP_EMP_DESN").getString());
                resultSetHistory.updateString("GP_TYPE",ObjDailyAttDataDetails.getAttribute("GP_TYPE").getString());
                resultSetHistory.updateString("GP_TOL", ObjDailyAttDataDetails.getAttribute("GP_TOL").getString());
                resultSetHistory.updateString("GP_TOA", ObjDailyAttDataDetails.getAttribute("GP_TOA").getString());
                resultSetHistory.updateString("GP_REMARKS",ObjDailyAttDataDetails.getAttribute("GP_REMARKS").getString());
                resultSetHistory.updateString("GP_NATURE_OF_WORK",ObjDailyAttDataDetails.getAttribute("GP_NATURE_OF_WORK").getString());
                resultSetHistory.updateString("GP_DEPT_HEAD",ObjDailyAttDataDetails.getAttribute("GP_DEPT_HEAD").getString());
                resultSetHistory.updateString("GP_TOT_HOURS",ObjDailyAttDataDetails.getAttribute("GP_TOT_HOURS").getString());
                resultSetHistory.updateString("GP_TIME_OF_LEAVING", ObjDailyAttDataDetails.getAttribute("GP_TIME_OF_LEAVING").getString());
                resultSetHistory.updateString("GP_TIME_OF_ARRIVAL", ObjDailyAttDataDetails.getAttribute("GP_TIME_OF_ARRIVAL").getString());
                resultSetHistory.updateString("GP_CATEGORY",ObjDailyAttDataDetails.getAttribute("GP_CATEGORY").getString());
                
                resultSetHistory.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                resultSetHistory.updateString("CREATED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateInt("MODIFIED_BY", 0);
                resultSetHistory.updateString("MODIFIED_DATE", "0000-00-00");
                resultSetHistory.updateBoolean("APPROVED", false);
                resultSetHistory.updateString("APPROVED_DATE", "0000-00-00");
                resultSetHistory.updateBoolean("REJECTED", false);
                resultSetHistory.updateString("REJECTED_DATE", "0000-00-00");
                resultSetHistory.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
                resultSetHistory.updateInt("CANCELED", 0);
                resultSetHistory.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                resultSetHistory.updateInt("CHANGED", 1);
                resultSetHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                resultSetHistory.updateString("FROM_IP", "" + str_split[1]);
                                
                resultSetHistory.insertRow();
            }

            //======== Update the Approval Flow =========
            SDMLATTPAY.ApprovalFlow ObjFlow = new SDMLATTPAY.ApprovalFlow();
            ObjFlow.ModuleID = 873; 
            ObjFlow.DocNo = getAttribute("DOC_NO").getString();
            ObjFlow.DocDate = EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString());
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "SDMLATTPAY.GATEPASS_SELECTION_ENTRY";
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
            ResultSet rsIP = data.getResult("SELECT USER()");
            rsIP.first();
            String str = rsIP.getString(1);
            String str_split[] = str.split("@");
            
            // Production data connection
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  SDMLATTPAY.GATEPASS_SELECTION_ENTRY WHERE DOC_NO='1'");

            // Production data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //Get the Maximum Revision No in History Table.
            resultSetHistory = statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM SDMLATTPAY.GATEPASS_SELECTION_ENTRY_H WHERE DOC_NO='" + getAttribute("DOC_NO").getString() + "'");
            resultSetHistory.first();
            if (resultSetHistory.getRow() > 0) {
                revisionNo = resultSetHistory.getInt(1);
                revisionNo++;
            }
  
            //delete records from production data table before insert
            statementHistory.execute("DELETE FROM SDMLATTPAY.GATEPASS_SELECTION_ENTRY WHERE DOC_NO='" + getAttribute("DOC_NO").getString() + "'");

            resultSetHistory = statementHistory.executeQuery("SELECT * FROM SDMLATTPAY.GATEPASS_SELECTION_ENTRY_H WHERE DOC_NO='1'");

            //Now Update records into production tables
            for (int i = 1; i <= hmDailyAttDataDetails.size(); i++) {
                clsGatepassSelectionDetails ObjDailyAttDataDetails = (clsGatepassSelectionDetails) hmDailyAttDataDetails.get(Integer.toString(i));

                resultSetTemp.moveToInsertRow();                
                resultSetTemp.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetTemp.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                resultSetTemp.updateString("GP_MONTH", getAttribute("GP_MONTH").getString());
                resultSetTemp.updateString("GP_YEAR", getAttribute("GP_YEAR").getString());
                
                resultSetTemp.updateString("GP_CONSIDER",ObjDailyAttDataDetails.getAttribute("GP_CONSIDER").getString());
                resultSetTemp.updateString("GP_DATE", EITLERPGLOBAL.formatDateDB(ObjDailyAttDataDetails.getAttribute("GP_DATE").getString()));
                resultSetTemp.updateString("GP_EMP_NO",ObjDailyAttDataDetails.getAttribute("GP_EMP_NO").getString());
                resultSetTemp.updateString("GP_EMP_NAME",ObjDailyAttDataDetails.getAttribute("GP_EMP_NAME").getString());
                resultSetTemp.updateString("GP_EMP_DEPT",ObjDailyAttDataDetails.getAttribute("GP_EMP_DEPT").getString());
                resultSetTemp.updateString("GP_EMP_SHIFT",ObjDailyAttDataDetails.getAttribute("GP_EMP_SHIFT").getString());
                resultSetTemp.updateString("GP_EMP_DESN",ObjDailyAttDataDetails.getAttribute("GP_EMP_DESN").getString());
                resultSetTemp.updateString("GP_TYPE",ObjDailyAttDataDetails.getAttribute("GP_TYPE").getString());
                resultSetTemp.updateString("GP_TOL", ObjDailyAttDataDetails.getAttribute("GP_TOL").getString());
                resultSetTemp.updateString("GP_TOA", ObjDailyAttDataDetails.getAttribute("GP_TOA").getString());
                resultSetTemp.updateString("GP_REMARKS",ObjDailyAttDataDetails.getAttribute("GP_REMARKS").getString());
                resultSetTemp.updateString("GP_NATURE_OF_WORK",ObjDailyAttDataDetails.getAttribute("GP_NATURE_OF_WORK").getString());
                resultSetTemp.updateString("GP_DEPT_HEAD",ObjDailyAttDataDetails.getAttribute("GP_DEPT_HEAD").getString());
                resultSetTemp.updateString("GP_TOT_HOURS",ObjDailyAttDataDetails.getAttribute("GP_TOT_HOURS").getString());
                resultSetTemp.updateString("GP_TIME_OF_LEAVING", ObjDailyAttDataDetails.getAttribute("GP_TIME_OF_LEAVING").getString());
                resultSetTemp.updateString("GP_TIME_OF_ARRIVAL", ObjDailyAttDataDetails.getAttribute("GP_TIME_OF_ARRIVAL").getString());
                resultSetTemp.updateString("GP_CATEGORY",ObjDailyAttDataDetails.getAttribute("GP_CATEGORY").getString());
                
                resultSetTemp.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                resultSetTemp.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
                resultSetTemp.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                resultSetTemp.updateString("MODIFIED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetTemp.updateBoolean("APPROVED", false);
                resultSetTemp.updateString("APPROVED_DATE", "0000-00-00");
                resultSetTemp.updateBoolean("REJECTED", false);
                resultSetTemp.updateString("REJECTED_DATE", "0000-00-00");
                resultSetTemp.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
                resultSetTemp.updateInt("CANCELED", 0);
                resultSetTemp.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                resultSetTemp.updateInt("CHANGED", 1);
                resultSetTemp.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());                
                resultSetTemp.updateString("FROM_IP", "" + str_split[1]);                
                
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
                resultSetHistory.updateString("GP_MONTH", getAttribute("GP_MONTH").getString());
                resultSetHistory.updateString("GP_YEAR", getAttribute("GP_YEAR").getString());
                
                resultSetHistory.updateString("GP_CONSIDER",ObjDailyAttDataDetails.getAttribute("GP_CONSIDER").getString());
                resultSetHistory.updateString("GP_DATE", EITLERPGLOBAL.formatDateDB(ObjDailyAttDataDetails.getAttribute("GP_DATE").getString()));
                resultSetHistory.updateString("GP_EMP_NO",ObjDailyAttDataDetails.getAttribute("GP_EMP_NO").getString());
                resultSetHistory.updateString("GP_EMP_NAME",ObjDailyAttDataDetails.getAttribute("GP_EMP_NAME").getString());
                resultSetHistory.updateString("GP_EMP_DEPT",ObjDailyAttDataDetails.getAttribute("GP_EMP_DEPT").getString());
                resultSetHistory.updateString("GP_EMP_SHIFT",ObjDailyAttDataDetails.getAttribute("GP_EMP_SHIFT").getString());
                resultSetHistory.updateString("GP_EMP_DESN",ObjDailyAttDataDetails.getAttribute("GP_EMP_DESN").getString());
                resultSetHistory.updateString("GP_TYPE",ObjDailyAttDataDetails.getAttribute("GP_TYPE").getString());
                resultSetHistory.updateString("GP_TOL", ObjDailyAttDataDetails.getAttribute("GP_TOL").getString());
                resultSetHistory.updateString("GP_TOA", ObjDailyAttDataDetails.getAttribute("GP_TOA").getString());
                resultSetHistory.updateString("GP_REMARKS",ObjDailyAttDataDetails.getAttribute("GP_REMARKS").getString());
                resultSetHistory.updateString("GP_NATURE_OF_WORK",ObjDailyAttDataDetails.getAttribute("GP_NATURE_OF_WORK").getString());
                resultSetHistory.updateString("GP_DEPT_HEAD",ObjDailyAttDataDetails.getAttribute("GP_DEPT_HEAD").getString());
                resultSetHistory.updateString("GP_TOT_HOURS",ObjDailyAttDataDetails.getAttribute("GP_TOT_HOURS").getString());
                resultSetHistory.updateString("GP_TIME_OF_LEAVING", ObjDailyAttDataDetails.getAttribute("GP_TIME_OF_LEAVING").getString());
                resultSetHistory.updateString("GP_TIME_OF_ARRIVAL", ObjDailyAttDataDetails.getAttribute("GP_TIME_OF_ARRIVAL").getString());
                resultSetHistory.updateString("GP_CATEGORY",ObjDailyAttDataDetails.getAttribute("GP_CATEGORY").getString());
                
                resultSetHistory.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                resultSetHistory.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
                resultSetHistory.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                resultSetHistory.updateString("MODIFIED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateBoolean("APPROVED", false);
                resultSetHistory.updateString("APPROVED_DATE", "0000-00-00");
                resultSetHistory.updateBoolean("REJECTED", false);
                resultSetHistory.updateString("REJECTED_DATE", "0000-00-00");
                resultSetHistory.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
                resultSetHistory.updateInt("CANCELED", 0);
                resultSetHistory.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                resultSetHistory.updateInt("CHANGED", 1);
                resultSetHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                resultSetHistory.updateString("FROM_IP", "" + str_split[1]);

                resultSetHistory.insertRow();
            }

            //======== Update the Approval Flow =========
            SDMLATTPAY.ApprovalFlow ObjFlow = new SDMLATTPAY.ApprovalFlow();
            ObjFlow.ModuleID = 873; 
            ObjFlow.DocNo = getAttribute("DOC_NO").getString();
            ObjFlow.DocDate = EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString());
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName = "SDMLATTPAY.GATEPASS_SELECTION_ENTRY";
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
                data.Execute("UPDATE SDMLATTPAY.GATEPASS_SELECTION_ENTRY SET REJECTED=0,CHANGED=1 WHERE DOC_NO='" + getAttribute("DOC_NO").getString() + "'");
                
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.GATEPASS_SELECTION_ENTRY WHERE DOC_NO='" + documentNo + "' AND APPROVED=" + 1;
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=873 AND USER_ID=" + userID + " AND DOC_NO='" + documentNo + "' AND STATUS='W'";
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();

                if (rsTmp.getInt("COUNT") > 0) {
                    strSQL = "DELETE FROM SDMLATTPAY.GATEPASS_SELECTION_ENTRY WHERE DOC_NO='" + documentNo + "'";
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.GATEPASS_SELECTION_ENTRY WHERE DOC_NO='" + documentNo + "' AND APPROVED=1";
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=873 AND USER_ID=" + userID + " AND DOC_NO='" + documentNo + "' AND STATUS='W'";
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
            String strSql = "SELECT * FROM SDMLATTPAY.GATEPASS_SELECTION_ENTRY WHERE 1=1 " + stringFindQuery + " GROUP BY DOC_NO ORDER BY DOC_NO ";
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
            //hmDailyAttDataDetails.clear();
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String str = "SELECT * FROM SDMLATTPAY.GATEPASS_SELECTION_ENTRY WHERE DOC_NO = '" + docNo + "' AND DOC_DATE = '" + docDate + "' ORDER BY DOC_NO,GP_DATE,GP_EMP_NO";
            resultSetTemp = statementTemp.executeQuery(str);
            resultSetTemp.first();
            hmDailyAttDataDetails.clear();

            while (!resultSetTemp.isAfterLast()) {
                serialNoCounter++;
                clsGatepassSelectionDetails ObjDailyAttDataDetails = new clsGatepassSelectionDetails();
                setAttribute("REVISION_NO", 0);
                setAttribute("DOC_NO", resultSetTemp.getString("DOC_NO"));
                setAttribute("DOC_DATE", resultSetTemp.getString("DOC_DATE"));                
                setAttribute("GP_MONTH", resultSetTemp.getString("GP_MONTH"));
                setAttribute("GP_YEAR", resultSetTemp.getString("GP_YEAR"));
                
                ObjDailyAttDataDetails.setAttribute("GP_CONSIDER", resultSetTemp.getString("GP_CONSIDER"));
                ObjDailyAttDataDetails.setAttribute("GP_DATE", EITLERPGLOBAL.formatDate(resultSetTemp.getString("GP_DATE")));
                ObjDailyAttDataDetails.setAttribute("GP_EMP_NO", resultSetTemp.getString("GP_EMP_NO"));
                ObjDailyAttDataDetails.setAttribute("GP_EMP_NAME", resultSetTemp.getString("GP_EMP_NAME"));
                ObjDailyAttDataDetails.setAttribute("GP_EMP_DEPT", resultSetTemp.getString("GP_EMP_DEPT"));
                ObjDailyAttDataDetails.setAttribute("GP_EMP_SHIFT", resultSetTemp.getString("GP_EMP_SHIFT"));
                ObjDailyAttDataDetails.setAttribute("GP_EMP_DESN", resultSetTemp.getString("GP_EMP_DESN"));
                ObjDailyAttDataDetails.setAttribute("GP_TYPE", resultSetTemp.getString("GP_TYPE"));
                ObjDailyAttDataDetails.setAttribute("GP_TOL", resultSetTemp.getString("GP_TOL"));
                ObjDailyAttDataDetails.setAttribute("GP_TOA", resultSetTemp.getString("GP_TOA"));
                ObjDailyAttDataDetails.setAttribute("GP_REMARKS", resultSetTemp.getString("GP_REMARKS"));
                ObjDailyAttDataDetails.setAttribute("GP_NATURE_OF_WORK", resultSetTemp.getString("GP_NATURE_OF_WORK"));
                ObjDailyAttDataDetails.setAttribute("GP_DEPT_HEAD", resultSetTemp.getString("GP_DEPT_HEAD"));
                ObjDailyAttDataDetails.setAttribute("GP_TOT_HOURS", resultSetTemp.getString("GP_TOT_HOURS"));
                ObjDailyAttDataDetails.setAttribute("GP_TIME_OF_LEAVING", resultSetTemp.getString("GP_TIME_OF_LEAVING"));
                ObjDailyAttDataDetails.setAttribute("GP_TIME_OF_ARRIVAL", resultSetTemp.getString("GP_TIME_OF_ARRIVAL"));
                ObjDailyAttDataDetails.setAttribute("GP_CATEGORY", resultSetTemp.getString("GP_CATEGORY"));
                                
                setAttribute("CREATED_BY", resultSetTemp.getInt("CREATED_BY"));
                setAttribute("CREATED_DATE", resultSetTemp.getString("CREATED_DATE"));
                setAttribute("MODIFIED_BY", resultSetTemp.getInt("MODIFIED_BY"));
                setAttribute("MODIFIED_DATE", resultSetTemp.getString("MODIFIED_DATE"));
                setAttribute("APPROVED", resultSet.getInt("APPROVED"));
                setAttribute("APPROVED_DATE", resultSetTemp.getString("APPROVED_DATE"));
                setAttribute("REJECTED", resultSetTemp.getBoolean("REJECTED"));
                setAttribute("REJECTED_DATE", resultSetTemp.getString("REJECTED_DATE"));
                setAttribute("REJECTED_REMARKS", resultSetTemp.getString("REJECTED_REMARKS"));
                setAttribute("CANCELED", resultSetTemp.getInt("CANCELED"));
                setAttribute("HIERARCHY_ID",resultSetTemp.getInt("HIERARCHY_ID"));

                hmDailyAttDataDetails.put(Integer.toString(serialNoCounter), ObjDailyAttDataDetails);
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
            rsTmp = stTmp.executeQuery("SELECT * FROM SDMLATTPAY.GATEPASS_SELECTION_ENTRY_H WHERE DOC_NO='" + DocumentNo + "' GROUP BY REVISION_NO");

            while (rsTmp.next()) {
                clsGatepassSelection ObjDailyAttData = new clsGatepassSelection();

                ObjDailyAttData.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                ObjDailyAttData.setAttribute("UPDATED_BY", rsTmp.getInt("UPDATED_BY"));
                ObjDailyAttData.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                ObjDailyAttData.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                ObjDailyAttData.setAttribute("APPROVER_REMARKS", rsTmp.getString("APPROVER_REMARKS"));
                ObjDailyAttData.setAttribute("FROM_IP", rsTmp.getString("FROM_IP"));

                hmHistoryList.put(Integer.toString(hmHistoryList.size() + 1), ObjDailyAttData);
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
            resultSet = statement.executeQuery("SELECT * FROM SDMLATTPAY.GATEPASS_SELECTION_ENTRY_H WHERE DOC_NO='" + pDocNo + "'");
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
                strSQL = "SELECT DISTINCT MA.DOC_NO,MA.DOC_DATE,SUBSTRING(RECEIVED_DATE,1,10) AS RECEIVED_DATE FROM SDMLATTPAY.GATEPASS_SELECTION_ENTRY MA,SDMLATTPAY.D_COM_DOC_DATA D WHERE MA.DOC_NO=D.DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=873 AND MA.CANCELED=0 ORDER BY RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT MA.DOC_NO,MA.DOC_DATE,SUBSTRING(RECEIVED_DATE,1,10) AS RECEIVED_DATE FROM SDMLATTPAY.GATEPASS_SELECTION_ENTRY MA,SDMLATTPAY.D_COM_DOC_DATA D WHERE MA.DOC_NO=D.DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=873 AND MA.CANCELED=0 ORDER BY MA.DOC_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT MA.DOC_NO,MA.DOC_DATE,SUBSTRING(RECEIVED_DATE,1,10) AS RECEIVED_DATE FROM SDMLATTPAY.GATEPASS_SELECTION_ENTRY MA,SDMLATTPAY.D_COM_DOC_DATA D WHERE MA.DOC_NO=D.DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=873 AND MA.CANCELED=0 ORDER BY MA.DOC_NO";
            }

            ResultSet rsTmp = data.getConn().createStatement().executeQuery(strSQL);

            Counter = 0;
            while (rsTmp.next()) {

                Counter = Counter + 1;
                clsGatepassSelection ObjDoc = new clsGatepassSelection();

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
                rsTmp = data.getResult("SELECT APPROVED FROM SDMLATTPAY.GATEPASS_SELECTION_ENTRY WHERE DOC_NO='" + pDocNo + "' ");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    Approved = rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();

                if (!Approved) {
                    data.Execute("DELETE FROM SDMLATTPAY.D_COM_DOC_DATA WHERE DOC_NO='" + pDocNo + "' AND MODULE_ID=873");
                }
                data.Execute("UPDATE SDMLATTPAY.GATEPASS_SELECTION_ENTRY SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='" + pDocNo + "'");

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
            System.out.println("SELECT DOC_NO FROM SDMLATTPAY.GATEPASS_SELECTION_ENTRY WHERE DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");
            rsTmp = stTmp.executeQuery("SELECT DOC_NO FROM SDMLATTPAY.GATEPASS_SELECTION_ENTRY WHERE DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");

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