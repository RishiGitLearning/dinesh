/*
 * clsDailyAttDataForm.java
 *
 * Created on August 22, 2013, 11:20 AM
 */
package SDMLATTPAY.DailyAttDataForm;

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
public class clsDailyAttDataForm {

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
    public clsDailyAttDataForm() {
        LastError = "";
        props = new HashMap();
        props.put("DOC_NO", new Variant(""));
        props.put("DOC_DATE", new Variant(""));
        
        props.put("ATT_DATE", new Variant(""));
        props.put("ATT_DEPT", new Variant(""));
        props.put("ATT_SHIFT", new Variant(""));
        
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

        hmDailyAttDataDetails = new HashMap();
    }

    public boolean LoadData() {
        Ready = false;
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery("SELECT * FROM SDMLATTPAY.DAILY_ATT_FORM_DATA GROUP BY DOC_NO ORDER BY CREATED_DATE,DOC_NO");
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
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  SDMLATTPAY.DAILY_ATT_FORM_DATA WHERE DOC_NO='1'");

            // Production data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM SDMLATTPAY.DAILY_ATT_FORM_DATA_H WHERE DOC_NO='1'");

            setAttribute("DOC_NO", getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 837, 347, true));

            //Now Insert records into production tables
            for (int i = 1; i <= hmDailyAttDataDetails.size(); i++) {
                clsDailyAttDataFormDetails ObjDailyAttDataDetails = (clsDailyAttDataFormDetails) hmDailyAttDataDetails.get(Integer.toString(i));

                //Insert records into production data table
                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetTemp.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                
                resultSetTemp.updateString("ATT_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ATT_DATE").getString()));
                resultSetTemp.updateString("ATT_DEPT", getAttribute("ATT_DEPT").getString());
                resultSetTemp.updateString("ATT_SHIFT", getAttribute("ATT_SHIFT").getString());
                
                resultSetTemp.updateString("SR_NO", ObjDailyAttDataDetails.getAttribute("SR_NO").getString());
                resultSetTemp.updateString("EMP_ID", ObjDailyAttDataDetails.getAttribute("EMP_ID").getString());
                resultSetTemp.updateString("EMP_NAME", ObjDailyAttDataDetails.getAttribute("EMP_NAME").getString());
                resultSetTemp.updateString("EMP_DEPT", ObjDailyAttDataDetails.getAttribute("EMP_DEPT").getString());
                resultSetTemp.updateString("EMP_MAIN_CATEGORY", ObjDailyAttDataDetails.getAttribute("EMP_MAIN_CATEGORY").getString());
                resultSetTemp.updateString("EMP_SUB_CATEGORY", ObjDailyAttDataDetails.getAttribute("EMP_SUB_CATEGORY").getString());
                resultSetTemp.updateString("PUNCH_TIME", ObjDailyAttDataDetails.getAttribute("PUNCH_TIME").getString());
                resultSetTemp.updateString("EMP_REMARK",ObjDailyAttDataDetails.getAttribute("EMP_REMARK").getString());
                
                resultSetTemp.updateString("EMP_PRESENT",ObjDailyAttDataDetails.getAttribute("EMP_PRESENT").getString());
                resultSetTemp.updateString("EMP_ABSENT",ObjDailyAttDataDetails.getAttribute("EMP_ABSENT").getString());
//                resultSetTemp.updateString("EMP_CL",ObjDailyAttDataDetails.getAttribute("EMP_CL").getString());
//                resultSetTemp.updateString("EMP_PL",ObjDailyAttDataDetails.getAttribute("EMP_PL").getString());
//                resultSetTemp.updateString("EMP_ESI",ObjDailyAttDataDetails.getAttribute("EMP_ESI").getString());
                resultSetTemp.updateString("EMP_EXCESS",ObjDailyAttDataDetails.getAttribute("EMP_EXCESS").getString());
                
                resultSetTemp.updateString("EMP_SHIFT",ObjDailyAttDataDetails.getAttribute("EMP_SHIFT").getString());
                
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
                
                resultSetHistory.updateString("ATT_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ATT_DATE").getString()));
                resultSetHistory.updateString("ATT_DEPT", getAttribute("ATT_DEPT").getString());
                resultSetHistory.updateString("ATT_SHIFT", getAttribute("ATT_SHIFT").getString());
                
                resultSetHistory.updateString("SR_NO", ObjDailyAttDataDetails.getAttribute("SR_NO").getString());
                resultSetHistory.updateString("EMP_ID", ObjDailyAttDataDetails.getAttribute("EMP_ID").getString());
                resultSetHistory.updateString("EMP_NAME", ObjDailyAttDataDetails.getAttribute("EMP_NAME").getString());
                resultSetHistory.updateString("EMP_DEPT", ObjDailyAttDataDetails.getAttribute("EMP_DEPT").getString());
                resultSetHistory.updateString("EMP_MAIN_CATEGORY", ObjDailyAttDataDetails.getAttribute("EMP_MAIN_CATEGORY").getString());
                resultSetHistory.updateString("EMP_SUB_CATEGORY", ObjDailyAttDataDetails.getAttribute("EMP_SUB_CATEGORY").getString());
                resultSetHistory.updateString("PUNCH_TIME", ObjDailyAttDataDetails.getAttribute("PUNCH_TIME").getString());
                resultSetHistory.updateString("EMP_REMARK",ObjDailyAttDataDetails.getAttribute("EMP_REMARK").getString());
                
                resultSetHistory.updateString("EMP_PRESENT",ObjDailyAttDataDetails.getAttribute("EMP_PRESENT").getString());
                resultSetHistory.updateString("EMP_ABSENT",ObjDailyAttDataDetails.getAttribute("EMP_ABSENT").getString());
//                resultSetHistory.updateString("EMP_CL",ObjDailyAttDataDetails.getAttribute("EMP_CL").getString());
//                resultSetHistory.updateString("EMP_PL",ObjDailyAttDataDetails.getAttribute("EMP_PL").getString());
//                resultSetHistory.updateString("EMP_ESI",ObjDailyAttDataDetails.getAttribute("EMP_ESI").getString());               
                resultSetHistory.updateString("EMP_EXCESS",ObjDailyAttDataDetails.getAttribute("EMP_EXCESS").getString());
                
                resultSetHistory.updateString("EMP_SHIFT",ObjDailyAttDataDetails.getAttribute("EMP_SHIFT").getString());
                
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
            ObjFlow.ModuleID = 837; 
            ObjFlow.DocNo = getAttribute("DOC_NO").getString();
            ObjFlow.DocDate = EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString());
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "SDMLATTPAY.DAILY_ATT_FORM_DATA";
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
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  SDMLATTPAY.DAILY_ATT_FORM_DATA WHERE DOC_NO='1'");

            // Production data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //Get the Maximum Revision No in History Table.
            resultSetHistory = statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM SDMLATTPAY.DAILY_ATT_FORM_DATA_H WHERE DOC_NO='" + getAttribute("DOC_NO").getString() + "'");
            resultSetHistory.first();
            if (resultSetHistory.getRow() > 0) {
                revisionNo = resultSetHistory.getInt(1);
                revisionNo++;
            }
  
            //delete records from production data table before insert
            statementHistory.execute("DELETE FROM SDMLATTPAY.DAILY_ATT_FORM_DATA WHERE DOC_NO='" + getAttribute("DOC_NO").getString() + "'");

            resultSetHistory = statementHistory.executeQuery("SELECT * FROM SDMLATTPAY.DAILY_ATT_FORM_DATA_H WHERE DOC_NO='1'");

            //Now Update records into production tables
            for (int i = 1; i <= hmDailyAttDataDetails.size(); i++) {
                clsDailyAttDataFormDetails ObjDailyAttDataDetails = (clsDailyAttDataFormDetails) hmDailyAttDataDetails.get(Integer.toString(i));

                resultSetTemp.moveToInsertRow();                
                resultSetTemp.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetTemp.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                
                resultSetTemp.updateString("ATT_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ATT_DATE").getString()));
                resultSetTemp.updateString("ATT_DEPT", getAttribute("ATT_DEPT").getString());
                resultSetTemp.updateString("ATT_SHIFT", getAttribute("ATT_SHIFT").getString());
                
                resultSetTemp.updateString("SR_NO", ObjDailyAttDataDetails.getAttribute("SR_NO").getString());
                resultSetTemp.updateString("EMP_ID", ObjDailyAttDataDetails.getAttribute("EMP_ID").getString());
                resultSetTemp.updateString("EMP_NAME", ObjDailyAttDataDetails.getAttribute("EMP_NAME").getString());
                resultSetTemp.updateString("EMP_DEPT", ObjDailyAttDataDetails.getAttribute("EMP_DEPT").getString());
                resultSetTemp.updateString("EMP_MAIN_CATEGORY", ObjDailyAttDataDetails.getAttribute("EMP_MAIN_CATEGORY").getString());
                resultSetTemp.updateString("EMP_SUB_CATEGORY", ObjDailyAttDataDetails.getAttribute("EMP_SUB_CATEGORY").getString());
                resultSetTemp.updateString("PUNCH_TIME", ObjDailyAttDataDetails.getAttribute("PUNCH_TIME").getString());
                resultSetTemp.updateString("EMP_REMARK",ObjDailyAttDataDetails.getAttribute("EMP_REMARK").getString());
                
                resultSetTemp.updateString("EMP_PRESENT",ObjDailyAttDataDetails.getAttribute("EMP_PRESENT").getString());
                resultSetTemp.updateString("EMP_ABSENT",ObjDailyAttDataDetails.getAttribute("EMP_ABSENT").getString());
//                resultSetTemp.updateString("EMP_CL",ObjDailyAttDataDetails.getAttribute("EMP_CL").getString());
//                resultSetTemp.updateString("EMP_PL",ObjDailyAttDataDetails.getAttribute("EMP_PL").getString());
//                resultSetTemp.updateString("EMP_ESI",ObjDailyAttDataDetails.getAttribute("EMP_ESI").getString());    
                resultSetTemp.updateString("EMP_EXCESS",ObjDailyAttDataDetails.getAttribute("EMP_EXCESS").getString());
                
                resultSetTemp.updateString("EMP_SHIFT",ObjDailyAttDataDetails.getAttribute("EMP_SHIFT").getString());
                
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
                
                resultSetHistory.updateString("ATT_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("ATT_DATE").getString()));
                resultSetHistory.updateString("ATT_DEPT", getAttribute("ATT_DEPT").getString());
                resultSetHistory.updateString("ATT_SHIFT", getAttribute("ATT_SHIFT").getString());
                
                resultSetHistory.updateString("SR_NO", ObjDailyAttDataDetails.getAttribute("SR_NO").getString());
                resultSetHistory.updateString("EMP_ID", ObjDailyAttDataDetails.getAttribute("EMP_ID").getString());
                resultSetHistory.updateString("EMP_NAME", ObjDailyAttDataDetails.getAttribute("EMP_NAME").getString());
                resultSetHistory.updateString("EMP_DEPT", ObjDailyAttDataDetails.getAttribute("EMP_DEPT").getString());
                resultSetHistory.updateString("EMP_MAIN_CATEGORY", ObjDailyAttDataDetails.getAttribute("EMP_MAIN_CATEGORY").getString());
                resultSetHistory.updateString("EMP_SUB_CATEGORY", ObjDailyAttDataDetails.getAttribute("EMP_SUB_CATEGORY").getString());
                resultSetHistory.updateString("PUNCH_TIME", ObjDailyAttDataDetails.getAttribute("PUNCH_TIME").getString());
                resultSetHistory.updateString("EMP_REMARK",ObjDailyAttDataDetails.getAttribute("EMP_REMARK").getString());
                
                resultSetHistory.updateString("EMP_PRESENT",ObjDailyAttDataDetails.getAttribute("EMP_PRESENT").getString());
                resultSetHistory.updateString("EMP_ABSENT",ObjDailyAttDataDetails.getAttribute("EMP_ABSENT").getString());
//                resultSetHistory.updateString("EMP_CL",ObjDailyAttDataDetails.getAttribute("EMP_CL").getString());
//                resultSetHistory.updateString("EMP_PL",ObjDailyAttDataDetails.getAttribute("EMP_PL").getString());
//                resultSetHistory.updateString("EMP_ESI",ObjDailyAttDataDetails.getAttribute("EMP_ESI").getString());       
                resultSetHistory.updateString("EMP_EXCESS",ObjDailyAttDataDetails.getAttribute("EMP_EXCESS").getString());
                
                resultSetHistory.updateString("EMP_SHIFT",ObjDailyAttDataDetails.getAttribute("EMP_SHIFT").getString());
                
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
            ObjFlow.ModuleID = 837; 
            ObjFlow.DocNo = getAttribute("DOC_NO").getString();
            ObjFlow.DocDate = EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString());
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName = "SDMLATTPAY.DAILY_ATT_FORM_DATA";
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
                data.Execute("UPDATE SDMLATTPAY.DAILY_ATT_FORM_DATA SET REJECTED=0,CHANGED=1 WHERE DOC_NO='" + getAttribute("DOC_NO").getString() + "'");
                
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.DAILY_ATT_FORM_DATA WHERE DOC_NO='" + documentNo + "' AND APPROVED=" + 1;
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=837 AND USER_ID=" + userID + " AND DOC_NO='" + documentNo + "' AND STATUS='W'";
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();

                if (rsTmp.getInt("COUNT") > 0) {
                    strSQL = "DELETE FROM SDMLATTPAY.DAILY_ATT_FORM_DATA WHERE DOC_NO='" + documentNo + "'";
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.DAILY_ATT_FORM_DATA WHERE DOC_NO='" + documentNo + "' AND APPROVED=1";
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=837 AND USER_ID=" + userID + " AND DOC_NO='" + documentNo + "' AND STATUS='W'";
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
            String strSql = "SELECT * FROM SDMLATTPAY.DAILY_ATT_FORM_DATA WHERE 1=1 " + stringFindQuery + " GROUP BY DOC_NO ORDER BY DOC_NO ";
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
            String str = "SELECT * FROM SDMLATTPAY.DAILY_ATT_FORM_DATA WHERE DOC_NO = '" + docNo + "' AND DOC_DATE = '" + docDate + "' ORDER BY SR_NO";
            resultSetTemp = statementTemp.executeQuery(str);
            resultSetTemp.first();
            hmDailyAttDataDetails.clear();

            while (!resultSetTemp.isAfterLast()) {
                serialNoCounter++;
                clsDailyAttDataFormDetails ObjDailyAttDataDetails = new clsDailyAttDataFormDetails();
                setAttribute("REVISION_NO", 0);
                setAttribute("DOC_NO", resultSetTemp.getString("DOC_NO"));
                setAttribute("DOC_DATE", resultSetTemp.getString("DOC_DATE"));
                
                setAttribute("ATT_DATE", resultSetTemp.getString("ATT_DATE"));
                setAttribute("ATT_DEPT", resultSetTemp.getString("ATT_DEPT"));
                setAttribute("ATT_SHIFT", resultSetTemp.getString("ATT_SHIFT"));
                
                ObjDailyAttDataDetails.setAttribute("SR_NO", resultSetTemp.getString("SR_NO"));
                ObjDailyAttDataDetails.setAttribute("EMP_ID", resultSetTemp.getString("EMP_ID"));
                ObjDailyAttDataDetails.setAttribute("EMP_NAME", resultSetTemp.getString("EMP_NAME"));
                ObjDailyAttDataDetails.setAttribute("EMP_DEPT", resultSetTemp.getString("EMP_DEPT"));
                ObjDailyAttDataDetails.setAttribute("EMP_MAIN_CATEGORY", resultSetTemp.getString("EMP_MAIN_CATEGORY"));
                ObjDailyAttDataDetails.setAttribute("EMP_SUB_CATEGORY", resultSetTemp.getString("EMP_SUB_CATEGORY"));
                ObjDailyAttDataDetails.setAttribute("PUNCH_TIME", resultSetTemp.getString("PUNCH_TIME"));
                ObjDailyAttDataDetails.setAttribute("EMP_REMARK", resultSetTemp.getString("EMP_REMARK"));
                
                ObjDailyAttDataDetails.setAttribute("EMP_PRESENT", resultSetTemp.getString("EMP_PRESENT"));
                ObjDailyAttDataDetails.setAttribute("EMP_ABSENT", resultSetTemp.getString("EMP_ABSENT"));
//                ObjDailyAttDataDetails.setAttribute("EMP_CL", resultSetTemp.getString("EMP_CL"));
//                ObjDailyAttDataDetails.setAttribute("EMP_PL", resultSetTemp.getString("EMP_PL"));
//                ObjDailyAttDataDetails.setAttribute("EMP_ESI", resultSetTemp.getString("EMP_ESI"));
                ObjDailyAttDataDetails.setAttribute("EMP_EXCESS", resultSetTemp.getString("EMP_EXCESS"));
                
                ObjDailyAttDataDetails.setAttribute("EMP_SHIFT", resultSetTemp.getString("EMP_SHIFT"));
                
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
            rsTmp = stTmp.executeQuery("SELECT * FROM SDMLATTPAY.DAILY_ATT_FORM_DATA_H WHERE DOC_NO='" + DocumentNo + "' GROUP BY REVISION_NO");

            while (rsTmp.next()) {
                clsDailyAttDataForm ObjDailyAttData = new clsDailyAttDataForm();

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
            resultSet = statement.executeQuery("SELECT * FROM SDMLATTPAY.DAILY_ATT_FORM_DATA_H WHERE DOC_NO='" + pDocNo + "'");
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
                strSQL = "SELECT DISTINCT MA.DOC_NO,MA.DOC_DATE,SUBSTRING(RECEIVED_DATE,1,10) AS RECEIVED_DATE FROM SDMLATTPAY.DAILY_ATT_FORM_DATA MA,SDMLATTPAY.D_COM_DOC_DATA D WHERE MA.DOC_NO=D.DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=837 AND MA.CANCELED=0 ORDER BY RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT MA.DOC_NO,MA.DOC_DATE,SUBSTRING(RECEIVED_DATE,1,10) AS RECEIVED_DATE FROM SDMLATTPAY.DAILY_ATT_FORM_DATA MA,SDMLATTPAY.D_COM_DOC_DATA D WHERE MA.DOC_NO=D.DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=837 AND MA.CANCELED=0 ORDER BY MA.DOC_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT MA.DOC_NO,MA.DOC_DATE,SUBSTRING(RECEIVED_DATE,1,10) AS RECEIVED_DATE FROM SDMLATTPAY.DAILY_ATT_FORM_DATA MA,SDMLATTPAY.D_COM_DOC_DATA D WHERE MA.DOC_NO=D.DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=837 AND MA.CANCELED=0 ORDER BY MA.DOC_NO";
            }

            ResultSet rsTmp = data.getConn().createStatement().executeQuery(strSQL);

            Counter = 0;
            while (rsTmp.next()) {

                Counter = Counter + 1;
                clsDailyAttDataForm ObjDoc = new clsDailyAttDataForm();

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
                rsTmp = data.getResult("SELECT APPROVED FROM SDMLATTPAY.DAILY_ATT_FORM_DATA WHERE DOC_NO='" + pDocNo + "' ");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    Approved = rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();

                if (!Approved) {
                    data.Execute("DELETE FROM SDMLATTPAY.D_COM_DOC_DATA WHERE DOC_NO='" + pDocNo + "' AND MODULE_ID=837");
                }
                data.Execute("UPDATE SDMLATTPAY.DAILY_ATT_FORM_DATA SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='" + pDocNo + "'");

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
            System.out.println("SELECT DOC_NO FROM SDMLATTPAY.DAILY_ATT_FORM_DATA WHERE DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");
            rsTmp = stTmp.executeQuery("SELECT DOC_NO FROM SDMLATTPAY.DAILY_ATT_FORM_DATA WHERE DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");

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