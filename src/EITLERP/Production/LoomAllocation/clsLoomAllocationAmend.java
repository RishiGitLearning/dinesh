/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.Production.LoomAllocation;

import EITLERP.ComboData;
import EITLERP.EITLERPGLOBAL;
import EITLERP.Production.clsFeltProductionApprovalFlow;
import EITLERP.Variant;
import EITLERP.clsSales_Party;
import EITLERP.data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 *
 * @author
 *
 */
public class clsLoomAllocationAmend {

    public String LastError = "";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet, rsHeader;
    //private ResultSet rsResultSet,

    public HashMap props;
    public boolean Ready = false;
    //Felt Order Collection
    public HashMap objDToFDetails;
    public HashMap objDToFDetails_ForAdd;
    public HashMap objDToFDetails_ForUpdate;

    //History Related properties
    public boolean HistoryView = false;
    private String historyAmendDate = "";
    private String historyAmendID = "";
    private static int ModuleID = 875;

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
     * Creates new Data Felt Order Updation
     */
    public clsLoomAllocationAmend() {
        LastError = "";
        props = new HashMap();

        props.put("LAA_DOC_NO", new Variant(""));
        props.put("LAA_DOC_DATE", new Variant(""));
        props.put("MODULE_ID", new Variant(""));
        props.put("USER_ID", new Variant(""));
        props.put("RECEIVED_DATE", new Variant(""));
        // -- Approval Specific Fields --
        props.put("HIERARCHY_ID", new Variant(0));
        props.put("FROM", new Variant(0));
        props.put("TO", new Variant(0));
        props.put("REJECTED_REMARKS", new Variant(""));
        props.put("REMARKS", new Variant(""));
        props.put("APPROVAL_STATUS", new Variant(""));
        props.put("APPROVER_REMARKS", new Variant(0));
        props.put("ENTRY_DATE", new Variant(0));

        props.put("LAA_DOC_NO", new Variant(""));
        props.put("LAA_DOC_DATE", new Variant(""));
        props.put("PIECE_NO", new Variant(""));
        props.put("NO_OF_DIVISION", new Variant(""));
        props.put("DIVISION_BY", new Variant(""));
        props.put("REMARK", new Variant(""));

        objDToFDetails = new HashMap();
        objDToFDetails_ForAdd = new HashMap();
        objDToFDetails_ForUpdate = new HashMap();
    }

    public boolean LoadData() {
        Ready = false;
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER ORDER BY LAA_DOC_NO");
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
//            if(HistoryView) setHistoryData(historyAmendDate, historyAmendID);
//            else 

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
//            if(HistoryView) setHistoryData(historyAmendDate, historyAmendID);
//            else 

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
//            if(HistoryView) setHistoryData(historyAmendDate, historyAmendID);
//            else 

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
//            if(HistoryView) setHistoryData(historyAmendDate, historyAmendID);
//            else 

            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    public boolean Insert() {
        ResultSet resultSetTemp, resultSetHistory, rsHeader, rsHeaderH ,resultSetTemp_Add , resultSetTemp_Update;
        Statement statementTemp, statementHistory, stHeader, stHeaderH , statementTemp_Add , statementTemp_Update;
        try {
            // Felt Order Updation data connection
            connection = data.getConn();
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_DETAIL WHERE LAA_DOC_NO='1'");

            // Felt order Updation data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_DETAIL_H WHERE LAA_DOC_NO='1'");

            stHeader = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHeader = stHeader.executeQuery("SELECT * FROM  PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER WHERE LAA_DOC_NO='1'");

            stHeaderH = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHeaderH = stHeaderH.executeQuery("SELECT * FROM  PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER_H WHERE LAA_DOC_NO='1'");

            statementTemp_Add = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp_Add = statementTemp_Add.executeQuery("SELECT * FROM  PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_DETAIL_ADD WHERE LAA_DOC_NO='1'");

            statementTemp_Update = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp_Update = statementTemp_Update.executeQuery("SELECT * FROM  PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_DETAIL_UPDATE WHERE LAA_DOC_NO='1'");

            
            //setAttribute("LAA_DOC_NO",);
            rsHeader.first();
            rsHeader.moveToInsertRow();

            rsHeader.updateString("LAA_DOC_NO", getAttribute("LAA_DOC_NO").getString());
            rsHeader.updateString("LAA_DOC_DATE", getAttribute("LAA_DOC_DATE").getString());

            rsHeader.updateString("REMARK", getAttribute("REMARK").getString());
            
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

            
            rsHeaderH.updateString("REMARK", getAttribute("REMARK").getString());
            
            rsHeaderH.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsHeaderH.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            rsHeaderH.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
            rsHeaderH.updateString("LAA_DOC_NO", getAttribute("LAA_DOC_NO").getString());
            rsHeaderH.updateString("LAA_DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("LAA_DOC_DATE").getString()));

            rsHeaderH.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            rsHeaderH.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());

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

            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");

            rsHeaderH.updateString("FROM_IP", "" + str_split[1]);

            rsHeaderH.insertRow();

            //Now Insert records into FELT_CN_TEMP_HEADER & History tables
            for (int i = 1; i <= objDToFDetails.size(); i++) {
                clsLoomAllocationAmendDetails ObjLoomAllocationAmendDetails = (clsLoomAllocationAmendDetails) objDToFDetails.get(Integer.toString(i));
                
                //Insert records into Felt order Amendment table
                resultSetTemp.moveToInsertRow();

                resultSetTemp.updateString("LAA_DOC_NO", ObjLoomAllocationAmendDetails.getAttribute("LAA_DOC_NO").getString());
                //EITLERPGLOBAL.formatDateDB(getAttribute("LAA_DOC_DATE").getString())
                
                resultSetTemp.updateString("LAA_DOC_DATE", ObjLoomAllocationAmendDetails.getAttribute("LAA_DOC_DATE").getString());
                resultSetTemp.updateString("LOOM_NO", ObjLoomAllocationAmendDetails.getAttribute("LOOM_NO").getString());
                resultSetTemp.updateString("PPC_PRIORITY", ObjLoomAllocationAmendDetails.getAttribute("PPC_PRIORITY").getString());
                resultSetTemp.updateString("STYLE", ObjLoomAllocationAmendDetails.getAttribute("STYLE").getString());
                resultSetTemp.updateString("REED_CODE", ObjLoomAllocationAmendDetails.getAttribute("REED_CODE").getString());
                resultSetTemp.updateString("REED_SPACE_MIN", ObjLoomAllocationAmendDetails.getAttribute("REED_SPACE_MIN").getString());
                resultSetTemp.updateString("REED_SPACE_MAX", ObjLoomAllocationAmendDetails.getAttribute("REED_SPACE_MAX").getString());
                resultSetTemp.updateString("TEX_COUNT", ObjLoomAllocationAmendDetails.getAttribute("TEX_COUNT").getString());
                resultSetTemp.updateString("WARP_YARN_DESC", ObjLoomAllocationAmendDetails.getAttribute("WARP_YARN_DESC").getString());
                resultSetTemp.updateString("ACTUAL_WARP_LENGTH", ObjLoomAllocationAmendDetails.getAttribute("ACTUAL_WARP_LENGTH").getString());
                resultSetTemp.updateString("ACHIEVABLE_WOVEN_LENGTH", ObjLoomAllocationAmendDetails.getAttribute("ACHIEVABLE_WOVEN_LENGTH").getString());
                
                resultSetTemp.insertRow();
                
                //Insert records into Felt Order Amendment History Table
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO", 1);
                resultSetHistory.updateString("LAA_DOC_NO", ObjLoomAllocationAmendDetails.getAttribute("LAA_DOC_NO").getString());
                resultSetHistory.updateString("LAA_DOC_DATE", ObjLoomAllocationAmendDetails.getAttribute("LAA_DOC_DATE").getString());
                resultSetHistory.updateString("LOOM_NO", ObjLoomAllocationAmendDetails.getAttribute("LOOM_NO").getString());
                resultSetHistory.updateString("PPC_PRIORITY", ObjLoomAllocationAmendDetails.getAttribute("PPC_PRIORITY").getString());
                resultSetHistory.updateString("STYLE", ObjLoomAllocationAmendDetails.getAttribute("STYLE").getString());
                resultSetHistory.updateString("REED_CODE", ObjLoomAllocationAmendDetails.getAttribute("REED_CODE").getString());
                resultSetHistory.updateString("REED_SPACE_MIN", ObjLoomAllocationAmendDetails.getAttribute("REED_SPACE_MIN").getString());
                resultSetHistory.updateString("REED_SPACE_MAX", ObjLoomAllocationAmendDetails.getAttribute("REED_SPACE_MAX").getString());
                resultSetHistory.updateString("TEX_COUNT", ObjLoomAllocationAmendDetails.getAttribute("TEX_COUNT").getString());
                resultSetHistory.updateString("WARP_YARN_DESC", ObjLoomAllocationAmendDetails.getAttribute("WARP_YARN_DESC").getString());
                resultSetHistory.updateString("ACTUAL_WARP_LENGTH", ObjLoomAllocationAmendDetails.getAttribute("ACTUAL_WARP_LENGTH").getString());
                resultSetHistory.updateString("ACHIEVABLE_WOVEN_LENGTH", ObjLoomAllocationAmendDetails.getAttribute("ACHIEVABLE_WOVEN_LENGTH").getString());
                
                resultSetHistory.insertRow();

                // Final Approval and save to PIECE REGISTER 
                if (getAttribute("APPROVAL_STATUS").getString().equals("F") && !ObjLoomAllocationAmendDetails.getAttribute("PIECE_NO").getString().equals("")) {
                            
                }
            }

            for (int i = 1; i <= objDToFDetails_ForAdd.size(); i++) {
                clsLoomAllocationAmendDetails ObjLoomAllocationAmendDetails = (clsLoomAllocationAmendDetails) objDToFDetails_ForAdd.get(Integer.toString(i));

                //Insert records into Felt order Amendment table
                resultSetTemp_Add.moveToInsertRow();

                resultSetTemp_Add.updateString("LAA_DOC_NO", ObjLoomAllocationAmendDetails.getAttribute("LAA_DOC_NO").getString());
                resultSetTemp_Add.updateString("LAA_DOC_DATE", ObjLoomAllocationAmendDetails.getAttribute("LAA_DOC_DATE").getString());
                resultSetTemp_Add.updateString("LOOM_NO", ObjLoomAllocationAmendDetails.getAttribute("LOOM_NO").getString());
                resultSetTemp_Add.updateString("PPC_PRIORITY", ObjLoomAllocationAmendDetails.getAttribute("PPC_PRIORITY").getString());
                resultSetTemp_Add.updateString("STYLE", ObjLoomAllocationAmendDetails.getAttribute("STYLE").getString());
                resultSetTemp_Add.updateString("REED_CODE", ObjLoomAllocationAmendDetails.getAttribute("REED_CODE").getString());
                resultSetTemp_Add.updateString("REED_SPACE_MIN", ObjLoomAllocationAmendDetails.getAttribute("REED_SPACE_MIN").getString());
                resultSetTemp_Add.updateString("REED_SPACE_MAX", ObjLoomAllocationAmendDetails.getAttribute("REED_SPACE_MAX").getString());
                resultSetTemp_Add.updateString("TEX_COUNT", ObjLoomAllocationAmendDetails.getAttribute("TEX_COUNT").getString());
                resultSetTemp_Add.updateString("WARP_YARN_DESC", ObjLoomAllocationAmendDetails.getAttribute("WARP_YARN_DESC").getString());
                resultSetTemp_Add.updateString("ACTUAL_WARP_LENGTH", ObjLoomAllocationAmendDetails.getAttribute("ACTUAL_WARP_LENGTH").getString());
                resultSetTemp_Add.updateString("ACHIEVABLE_WOVEN_LENGTH", ObjLoomAllocationAmendDetails.getAttribute("ACHIEVABLE_WOVEN_LENGTH").getString());
                resultSetTemp_Add.insertRow();

                // Final Approval and save to PIECE REGISTER 
                if (getAttribute("APPROVAL_STATUS").getString().equals("F") && !ObjLoomAllocationAmendDetails.getAttribute("PIECE_NO").getString().equals("")) {
                    
                     data.Execute("INSERT INTO PRODUCTION.PPC_LOOM_ALLOCATION_MASTER " +
                                "(LOOM_NO, PPC_PRIORITY, STYLE, REED_CODE, REED_SPACE_MIN, REED_SPACE_MAX, TEX_COUNT, WARP_YARN_DESC, ACTUAL_WARP_LENGTH, ACHIEVABLE_WOVEN_LENGTH) " +
                                "VALUES " +
                                "('"+ObjLoomAllocationAmendDetails.getAttribute("LOOM_NO").getString()+"', "
                                 + "'"+ObjLoomAllocationAmendDetails.getAttribute("PPC_PRIORITY").getString()+"', '"+ObjLoomAllocationAmendDetails.getAttribute("STYLE").getString()+"', '"+ObjLoomAllocationAmendDetails.getAttribute("REED_CODE").getString()+"', '"+ObjLoomAllocationAmendDetails.getAttribute("REED_SPACE_MIN").getString()+"', '"+ObjLoomAllocationAmendDetails.getAttribute("").getString()+"'REED_SPACE_MAX, '"+ObjLoomAllocationAmendDetails.getAttribute("TEX_COUNT").getString()+"', '"+ObjLoomAllocationAmendDetails.getAttribute("WARP_YARN_DESC").getString()+"', '"+ObjLoomAllocationAmendDetails.getAttribute("ACTUAL_WARP_LENGTH").getString()+"', '"+ObjLoomAllocationAmendDetails.getAttribute("ACHIEVABLE_WOVEN_LENGTH").getString()+"')");
                    
                    
                }
            }
            
            for (int i = 1; i <= objDToFDetails_ForUpdate.size(); i++) {
                clsLoomAllocationAmendDetails ObjLoomAllocationAmendDetails = (clsLoomAllocationAmendDetails) objDToFDetails_ForUpdate.get(Integer.toString(i));

                //Insert records into Felt order Amendment table
                resultSetTemp_Update.moveToInsertRow();

                resultSetTemp_Update.updateString("LAA_DOC_NO", ObjLoomAllocationAmendDetails.getAttribute("LAA_DOC_NO").getString());
                resultSetTemp_Update.updateString("LAA_DOC_DATE", ObjLoomAllocationAmendDetails.getAttribute("LAA_DOC_DATE").getString());
                resultSetTemp_Update.updateString("LOOM_NO", ObjLoomAllocationAmendDetails.getAttribute("LOOM_NO").getString());
                resultSetTemp_Update.updateString("PPC_PRIORITY", ObjLoomAllocationAmendDetails.getAttribute("PPC_PRIORITY").getString());
                resultSetTemp_Update.updateString("STYLE", ObjLoomAllocationAmendDetails.getAttribute("STYLE").getString());
                resultSetTemp_Update.updateString("REED_CODE", ObjLoomAllocationAmendDetails.getAttribute("REED_CODE").getString());
                resultSetTemp_Update.updateString("REED_SPACE_MIN", ObjLoomAllocationAmendDetails.getAttribute("REED_SPACE_MIN").getString());
                resultSetTemp_Update.updateString("REED_SPACE_MAX", ObjLoomAllocationAmendDetails.getAttribute("REED_SPACE_MAX").getString());
                resultSetTemp_Update.updateString("TEX_COUNT", ObjLoomAllocationAmendDetails.getAttribute("TEX_COUNT").getString());
                resultSetTemp_Update.updateString("WARP_YARN_DESC", ObjLoomAllocationAmendDetails.getAttribute("WARP_YARN_DESC").getString());
                resultSetTemp_Update.updateString("ACTUAL_WARP_LENGTH", ObjLoomAllocationAmendDetails.getAttribute("ACTUAL_WARP_LENGTH").getString());
                resultSetTemp_Update.updateString("ACHIEVABLE_WOVEN_LENGTH", ObjLoomAllocationAmendDetails.getAttribute("ACHIEVABLE_WOVEN_LENGTH").getString());
                resultSetTemp_Update.updateString("UPDATE_KEY", ObjLoomAllocationAmendDetails.getAttribute("UPDATE_KEY").getString());
                resultSetTemp_Update.insertRow();

                // Final Approval and save to PIECE REGISTER 
                if (getAttribute("APPROVAL_STATUS").getString().equals("F") && !ObjLoomAllocationAmendDetails.getAttribute("PIECE_NO").getString().equals("")) {
                           
                }
            }
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = ModuleID; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("LAA_DOC_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator = true;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("REJECTED_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "LAA_DOC_NO";

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

        ResultSet resultSetTemp, resultSetHistory, rsHeader, rsHeaderH, rsRegister,resultSetTemp_Add,resultSetTemp_Update;
        Statement statementTemp, statementHistory, stHeader, stHeaderH, stRegister,statementTemp_Add,statementTemp_Update;
        int revisionNo;
        try {
            // Production data connection

            // Production data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //Get the Maximum Revision No in History Table.
            resultSetHistory = statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_DETAIL_H WHERE LAA_DOC_NO='" + getAttribute("LAA_DOC_NO").getString() + "'");
            resultSetHistory.first();
            if (resultSetHistory.getRow() > 0) {
                revisionNo = resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from production data table before insert
            //statementHistory.execute("DELETE FROM PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_DETAIL WHERE LAA_DOC_NO='"+getAttribute("LAA_DOC_NO").getString()+"'");

            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_DETAIL_H WHERE LAA_DOC_NO=''");

            stHeaderH = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHeaderH = stHeaderH.executeQuery("SELECT * FROM  PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER_H WHERE LAA_DOC_NO=''");

            
            
            //Now Update records into FELT_CN_TEMP_HEADER tables
            resultSet.updateString("LAA_DOC_NO", getAttribute("LAA_DOC_NO").getString());
            resultSet.updateString("LAA_DOC_DATE", getAttribute("LAA_DOC_DATE").getString());

            resultSet.updateString("REMARK", getAttribute("REMARK").getString());
            
            resultSet.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
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
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER_H WHERE LAA_DOC_NO='" + getAttribute("LAA_DOC_NO").getString() + "'");

            RevNo++;

            rsHeaderH.moveToInsertRow();

            rsHeaderH.updateString("REVISION_NO", RevNo + "");

            rsHeaderH.updateString("LAA_DOC_NO", getAttribute("LAA_DOC_NO").getString());
            rsHeaderH.updateString("LAA_DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("LAA_DOC_DATE").getString()));

            rsHeaderH.updateString("REMARK", getAttribute("REMARK").getString());
            
            rsHeaderH.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            rsHeaderH.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
            rsHeaderH.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateString("MODIFIED_BY", EITLERPGLOBAL.gNewUserID + "");
            rsHeaderH.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateTimeDB());
            rsHeaderH.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsHeaderH.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsHeaderH.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            rsHeaderH.updateString("APPROVER_REMARKS", getAttribute("FROM_REMARKS").getString());
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

            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");
            rsHeaderH.updateString("FROM_IP", "" + str_split[1]);

            rsHeaderH.insertRow();
            String OrderNo = getAttribute("LAA_DOC_NO").getString();
            data.Execute("DELETE FROM PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_DETAIL WHERE LAA_DOC_NO='" + OrderNo + "'");

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_DETAIL WHERE LAA_DOC_NO='1'");

            data.Execute("DELETE FROM PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_DETAIL_ADD WHERE LAA_DOC_NO='" + OrderNo + "'");
            statementTemp_Add = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp_Add = statementTemp_Add.executeQuery("SELECT * FROM  PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_DETAIL_ADD WHERE LAA_DOC_NO='1'");

            data.Execute("DELETE FROM PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_DETAIL_UPDATE WHERE LAA_DOC_NO='" + OrderNo + "'");
            statementTemp_Update = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp_Update = statementTemp_Update.executeQuery("SELECT * FROM  PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_DETAIL_UPDATE WHERE LAA_DOC_NO='1'");

            int RevNoH = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_DETAIL_H WHERE LAA_DOC_NO='" + getAttribute("LAA_DOC_NO").getString() + "'");
            RevNoH++;

            resultSetHistory.moveToInsertRow();

            String Pieces = "";
            String Party = "";
            for (int i = 1; i <= objDToFDetails.size(); i++) {
                clsLoomAllocationAmendDetails ObjLoomAllocationAmendDetails = (clsLoomAllocationAmendDetails) objDToFDetails.get(Integer.toString(i));

                resultSetTemp.moveToInsertRow();

                resultSetTemp.updateString("LAA_DOC_NO", ObjLoomAllocationAmendDetails.getAttribute("LAA_DOC_NO").getString());
                resultSetTemp.updateString("LAA_DOC_DATE", getAttribute("LAA_DOC_DATE").getString());
                resultSetTemp.updateString("LOOM_NO", ObjLoomAllocationAmendDetails.getAttribute("LOOM_NO").getString());
                resultSetTemp.updateString("PPC_PRIORITY", ObjLoomAllocationAmendDetails.getAttribute("PPC_PRIORITY").getString());
                resultSetTemp.updateString("STYLE", ObjLoomAllocationAmendDetails.getAttribute("STYLE").getString());
                resultSetTemp.updateString("REED_CODE", ObjLoomAllocationAmendDetails.getAttribute("REED_CODE").getString());
                resultSetTemp.updateString("REED_SPACE_MIN", ObjLoomAllocationAmendDetails.getAttribute("REED_SPACE_MIN").getString());
                resultSetTemp.updateString("REED_SPACE_MAX", ObjLoomAllocationAmendDetails.getAttribute("REED_SPACE_MAX").getString());
                resultSetTemp.updateString("TEX_COUNT", ObjLoomAllocationAmendDetails.getAttribute("TEX_COUNT").getString());
                resultSetTemp.updateString("WARP_YARN_DESC", ObjLoomAllocationAmendDetails.getAttribute("WARP_YARN_DESC").getString());
                resultSetTemp.updateString("ACTUAL_WARP_LENGTH", ObjLoomAllocationAmendDetails.getAttribute("ACTUAL_WARP_LENGTH").getString());
                resultSetTemp.updateString("ACHIEVABLE_WOVEN_LENGTH", ObjLoomAllocationAmendDetails.getAttribute("ACHIEVABLE_WOVEN_LENGTH").getString());
                
                
                
                resultSetTemp.insertRow();

                resultSetHistory.updateInt("REVISION_NO", RevNoH);
                resultSetHistory.updateString("LAA_DOC_NO", ObjLoomAllocationAmendDetails.getAttribute("LAA_DOC_NO").getString());
                resultSetHistory.updateString("LAA_DOC_DATE", getAttribute("LAA_DOC_DATE").getString());
                resultSetHistory.updateString("LOOM_NO", ObjLoomAllocationAmendDetails.getAttribute("LOOM_NO").getString());
                resultSetHistory.updateString("PPC_PRIORITY", ObjLoomAllocationAmendDetails.getAttribute("PPC_PRIORITY").getString());
                resultSetHistory.updateString("STYLE", ObjLoomAllocationAmendDetails.getAttribute("STYLE").getString());
                resultSetHistory.updateString("REED_CODE", ObjLoomAllocationAmendDetails.getAttribute("REED_CODE").getString());
                resultSetHistory.updateString("REED_SPACE_MIN", ObjLoomAllocationAmendDetails.getAttribute("REED_SPACE_MIN").getString());
                resultSetHistory.updateString("REED_SPACE_MAX", ObjLoomAllocationAmendDetails.getAttribute("REED_SPACE_MAX").getString());
                resultSetHistory.updateString("TEX_COUNT", ObjLoomAllocationAmendDetails.getAttribute("TEX_COUNT").getString());
                resultSetHistory.updateString("WARP_YARN_DESC", ObjLoomAllocationAmendDetails.getAttribute("WARP_YARN_DESC").getString());
                resultSetHistory.updateString("ACTUAL_WARP_LENGTH", ObjLoomAllocationAmendDetails.getAttribute("ACTUAL_WARP_LENGTH").getString());
                resultSetHistory.updateString("ACHIEVABLE_WOVEN_LENGTH", ObjLoomAllocationAmendDetails.getAttribute("ACHIEVABLE_WOVEN_LENGTH").getString());
                
                resultSetHistory.insertRow(); 
                
            }

            
            for (int i = 1; i <= objDToFDetails_ForAdd.size(); i++) {
                clsLoomAllocationAmendDetails ObjLoomAllocationAmendDetails = (clsLoomAllocationAmendDetails) objDToFDetails_ForAdd.get(Integer.toString(i));

                //Insert records into Felt order Amendment table
                resultSetTemp_Add.moveToInsertRow();

                resultSetTemp_Add.updateString("LAA_DOC_NO", ObjLoomAllocationAmendDetails.getAttribute("LAA_DOC_NO").getString());
                resultSetTemp_Add.updateString("LAA_DOC_DATE", getAttribute("LAA_DOC_DATE").getString());
                resultSetTemp_Add.updateString("LOOM_NO", ObjLoomAllocationAmendDetails.getAttribute("LOOM_NO").getString());
                resultSetTemp_Add.updateString("PPC_PRIORITY", ObjLoomAllocationAmendDetails.getAttribute("PPC_PRIORITY").getString());
                resultSetTemp_Add.updateString("STYLE", ObjLoomAllocationAmendDetails.getAttribute("STYLE").getString());
                resultSetTemp_Add.updateString("REED_CODE", ObjLoomAllocationAmendDetails.getAttribute("REED_CODE").getString());
                resultSetTemp_Add.updateString("REED_SPACE_MIN", ObjLoomAllocationAmendDetails.getAttribute("REED_SPACE_MIN").getString());
                resultSetTemp_Add.updateString("REED_SPACE_MAX", ObjLoomAllocationAmendDetails.getAttribute("REED_SPACE_MAX").getString());
                resultSetTemp_Add.updateString("TEX_COUNT", ObjLoomAllocationAmendDetails.getAttribute("TEX_COUNT").getString());
                resultSetTemp_Add.updateString("WARP_YARN_DESC", ObjLoomAllocationAmendDetails.getAttribute("WARP_YARN_DESC").getString());
                resultSetTemp_Add.updateString("ACTUAL_WARP_LENGTH", ObjLoomAllocationAmendDetails.getAttribute("ACTUAL_WARP_LENGTH").getString());
                resultSetTemp_Add.updateString("ACHIEVABLE_WOVEN_LENGTH", ObjLoomAllocationAmendDetails.getAttribute("ACHIEVABLE_WOVEN_LENGTH").getString());
                resultSetTemp_Add.insertRow();

                // Final Approval and save to PIECE REGISTER 
                if (getAttribute("APPROVAL_STATUS").getString().equals("F") && !ObjLoomAllocationAmendDetails.getAttribute("LOOM_NO").getString().equals("")) {
                            
                    System.out.println("INSERT INTO PRODUCTION.PPC_LOOM_ALLOCATION_MASTER " +
                                "(LOOM_NO, PPC_PRIORITY, STYLE, REED_CODE, REED_SPACE_MIN, REED_SPACE_MAX, TEX_COUNT, WARP_YARN_DESC, ACTUAL_WARP_LENGTH, ACHIEVABLE_WOVEN_LENGTH) " +
                                "VALUES " +
                                "('"+ObjLoomAllocationAmendDetails.getAttribute("LOOM_NO").getString()+"', "
                                 + "'"+ObjLoomAllocationAmendDetails.getAttribute("PPC_PRIORITY").getString()+"', '"+ObjLoomAllocationAmendDetails.getAttribute("STYLE").getString()+"', '"+ObjLoomAllocationAmendDetails.getAttribute("REED_CODE").getString()+"', '"+ObjLoomAllocationAmendDetails.getAttribute("REED_SPACE_MIN").getString()+"', '"+ObjLoomAllocationAmendDetails.getAttribute("REED_SPACE_MAX").getString()+"', '"+ObjLoomAllocationAmendDetails.getAttribute("TEX_COUNT").getString()+"', '"+ObjLoomAllocationAmendDetails.getAttribute("WARP_YARN_DESC").getString()+"', '"+ObjLoomAllocationAmendDetails.getAttribute("ACTUAL_WARP_LENGTH").getString()+"', '"+ObjLoomAllocationAmendDetails.getAttribute("ACHIEVABLE_WOVEN_LENGTH").getString()+"')");
                    data.Execute("INSERT INTO PRODUCTION.PPC_LOOM_ALLOCATION_MASTER " +
                                "(LOOM_NO, PPC_PRIORITY, STYLE, REED_CODE, REED_SPACE_MIN, REED_SPACE_MAX, TEX_COUNT, WARP_YARN_DESC, ACTUAL_WARP_LENGTH, ACHIEVABLE_WOVEN_LENGTH) " +
                                "VALUES " +
                                "('"+ObjLoomAllocationAmendDetails.getAttribute("LOOM_NO").getString()+"', "
                                 + "'"+ObjLoomAllocationAmendDetails.getAttribute("PPC_PRIORITY").getString()+"', '"+ObjLoomAllocationAmendDetails.getAttribute("STYLE").getString()+"', '"+ObjLoomAllocationAmendDetails.getAttribute("REED_CODE").getString()+"', '"+ObjLoomAllocationAmendDetails.getAttribute("REED_SPACE_MIN").getString()+"', '"+ObjLoomAllocationAmendDetails.getAttribute("REED_SPACE_MAX").getString()+"', '"+ObjLoomAllocationAmendDetails.getAttribute("TEX_COUNT").getString()+"', '"+ObjLoomAllocationAmendDetails.getAttribute("WARP_YARN_DESC").getString()+"', '"+ObjLoomAllocationAmendDetails.getAttribute("ACTUAL_WARP_LENGTH").getString()+"', '"+ObjLoomAllocationAmendDetails.getAttribute("ACHIEVABLE_WOVEN_LENGTH").getString()+"')");
                    
                }
            }
            
            for (int i = 1; i <= objDToFDetails_ForUpdate.size(); i++) {
                clsLoomAllocationAmendDetails ObjLoomAllocationAmendDetails = (clsLoomAllocationAmendDetails) objDToFDetails_ForUpdate.get(Integer.toString(i));

                //Insert records into Felt order Amendment table
                resultSetTemp_Update.moveToInsertRow();

                resultSetTemp_Update.updateString("LAA_DOC_NO", ObjLoomAllocationAmendDetails.getAttribute("LAA_DOC_NO").getString());
                resultSetTemp_Update.updateString("LAA_DOC_DATE", getAttribute("LAA_DOC_DATE").getString());
                resultSetTemp_Update.updateString("LOOM_NO", ObjLoomAllocationAmendDetails.getAttribute("LOOM_NO").getString());
                resultSetTemp_Update.updateString("PPC_PRIORITY", ObjLoomAllocationAmendDetails.getAttribute("PPC_PRIORITY").getString());
                resultSetTemp_Update.updateString("STYLE", ObjLoomAllocationAmendDetails.getAttribute("STYLE").getString());
                resultSetTemp_Update.updateString("REED_CODE", ObjLoomAllocationAmendDetails.getAttribute("REED_CODE").getString());
                resultSetTemp_Update.updateString("REED_SPACE_MIN", ObjLoomAllocationAmendDetails.getAttribute("REED_SPACE_MIN").getString());
                resultSetTemp_Update.updateString("REED_SPACE_MAX", ObjLoomAllocationAmendDetails.getAttribute("REED_SPACE_MAX").getString());
                resultSetTemp_Update.updateString("TEX_COUNT", ObjLoomAllocationAmendDetails.getAttribute("TEX_COUNT").getString());
                resultSetTemp_Update.updateString("WARP_YARN_DESC", ObjLoomAllocationAmendDetails.getAttribute("WARP_YARN_DESC").getString());
                resultSetTemp_Update.updateString("ACTUAL_WARP_LENGTH", ObjLoomAllocationAmendDetails.getAttribute("ACTUAL_WARP_LENGTH").getString());
                resultSetTemp_Update.updateString("ACHIEVABLE_WOVEN_LENGTH", ObjLoomAllocationAmendDetails.getAttribute("ACHIEVABLE_WOVEN_LENGTH").getString());
                resultSetTemp_Update.updateString("UPDATE_KEY", ObjLoomAllocationAmendDetails.getAttribute("UPDATE_KEY").getString());
                resultSetTemp_Update.insertRow();

                // Final Approval and save to PIECE REGISTER 
                if (getAttribute("APPROVAL_STATUS").getString().equals("F") && !ObjLoomAllocationAmendDetails.getAttribute("LOOM_NO").getString().equals("")) {
                     
                    System.out.println("UPDATE PRODUCTION.PPC_LOOM_ALLOCATION_MASTER " +
                    "SET  " +
                    "LOOM_NO='"+ObjLoomAllocationAmendDetails.getAttribute("LOOM_NO").getString()+"', PPC_PRIORITY='"+ObjLoomAllocationAmendDetails.getAttribute("PPC_PRIORITY").getString()+"', STYLE='"+ObjLoomAllocationAmendDetails.getAttribute("STYLE").getString()+"', REED_CODE='"+ObjLoomAllocationAmendDetails.getAttribute("REED_CODE").getString()+"', REED_SPACE_MIN='"+ObjLoomAllocationAmendDetails.getAttribute("REED_SPACE_MIN").getString()+"', REED_SPACE_MAX='"+ObjLoomAllocationAmendDetails.getAttribute("REED_SPACE_MAX").getString()+"',"
                            + " TEX_COUNT='"+ObjLoomAllocationAmendDetails.getAttribute("TEX_COUNT").getString()+"', WARP_YARN_DESC='"+ObjLoomAllocationAmendDetails.getAttribute("WARP_YARN_DESC").getString()+"' " +
                    ", ACTUAL_WARP_LENGTH='"+ObjLoomAllocationAmendDetails.getAttribute("ACTUAL_WARP_LENGTH").getString()+"', ACHIEVABLE_WOVEN_LENGTH='"+ObjLoomAllocationAmendDetails.getAttribute("ACHIEVABLE_WOVEN_LENGTH").getString()+"' " +
                    "where concat(LOOM_NO,PPC_PRIORITY,STYLE,REED_CODE,REED_SPACE_MIN,REED_SPACE_MAX)='"+ObjLoomAllocationAmendDetails.getAttribute("UPDATE_KEY").getString()+"'");
                    data.Execute("UPDATE PRODUCTION.PPC_LOOM_ALLOCATION_MASTER " +
                    "SET  " +
                    "LOOM_NO='"+ObjLoomAllocationAmendDetails.getAttribute("LOOM_NO").getString()+"', PPC_PRIORITY='"+ObjLoomAllocationAmendDetails.getAttribute("PPC_PRIORITY").getString()+"', STYLE='"+ObjLoomAllocationAmendDetails.getAttribute("STYLE").getString()+"', REED_CODE='"+ObjLoomAllocationAmendDetails.getAttribute("REED_CODE").getString()+"', REED_SPACE_MIN='"+ObjLoomAllocationAmendDetails.getAttribute("REED_SPACE_MIN").getString()+"', REED_SPACE_MAX='"+ObjLoomAllocationAmendDetails.getAttribute("REED_SPACE_MAX").getString()+"',"
                            + " TEX_COUNT='"+ObjLoomAllocationAmendDetails.getAttribute("TEX_COUNT").getString()+"', WARP_YARN_DESC='"+ObjLoomAllocationAmendDetails.getAttribute("WARP_YARN_DESC").getString()+"' " +
                    ", ACTUAL_WARP_LENGTH='"+ObjLoomAllocationAmendDetails.getAttribute("ACTUAL_WARP_LENGTH").getString()+"', ACHIEVABLE_WOVEN_LENGTH='"+ObjLoomAllocationAmendDetails.getAttribute("ACHIEVABLE_WOVEN_LENGTH").getString()+"' " +
                    "where concat(LOOM_NO,PPC_PRIORITY,STYLE,REED_CODE,REED_SPACE_MIN,REED_SPACE_MAX)='"+ObjLoomAllocationAmendDetails.getAttribute("UPDATE_KEY").getString()+"'");
                            
                    
                }
            }
            
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = ModuleID; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("LAA_DOC_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator = true;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "LAA_DOC_NO";

            if (getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFeltProductionApprovalFlow.To = (int) getAttribute("SEND_DOC_TO").getVal();
                ObjFeltProductionApprovalFlow.ExplicitSendTo = true;
            }

            //==== Handling Rejected Documents ==========//
            boolean IsRejected = getAttribute("REJECTED").getBool();

            if (IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER SET REJECTED=0,CHANGED=1 WHERE LAA_DOC_NO ='" + getAttribute("LAA_DOC_NO").getString() + "'");
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER WHERE  LAA_DOC_NO='" + documentNo + "' AND APPROVED=" + 1;
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=" + ModuleID + " AND USER_ID=" + userID + " AND LAA_DOC_NO='" + documentNo + "' AND STATUS='W'";
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();

                if (rsTmp.getInt("COUNT") > 0) {
                    strSQL = "DELETE FROM PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER WHERE "
                            + " LAA_DOC_DATE = '" + EITLERPGLOBAL.formatDateDB(stringProductionDate) + "'"
                            + " AND LAA_DOC_NO ='" + documentNo + "'";

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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER WHERE LAA_DOC_NO='" + orderupdDocumentNo + "' AND APPROVED=1";
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
            String strSql = "SELECT * FROM PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER WHERE  " + stringFindQuery + "";
            System.out.println("STR SQL : " + strSql);
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
        ResultSet resultSetTemp,resultSetTemp_Add,resultSetTemp_Update;
        Statement statementTemp,statementTemp_Add,statementTemp_Update;
        int serialNoCounter = 0;
        int RevNo = 0;

        try {

            setAttribute("REVISION_NO", 0);

            setAttribute("LAA_DOC_NO", resultSet.getString("LAA_DOC_NO"));
            setAttribute("LAA_DOC_DATE", resultSet.getDate("LAA_DOC_DATE"));
            
            setAttribute("REMARK", resultSet.getString("REMARK"));

            if (HistoryView) {
                RevNo = resultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO", resultSet.getInt("REVISION_NO"));
            }

            setAttribute("CREATED_BY", resultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE", resultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY", resultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE", resultSet.getString("MODIFIED_DATE"));
            setAttribute("APPROVED", resultSet.getInt("APPROVED"));
            //setAttribute("APPROVED_BY",resultSet.getInt("APPROVED_BY"));
            setAttribute("APPROVED_DATE", resultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED", resultSet.getBoolean("REJECTED"));
            //setAttribute("REJECTED_BY",resultSet.getBoolean("REJECTED_BY"));
            setAttribute("REJECTED_DATE", resultSet.getString("REJECTED_DATE"));
            setAttribute("CANCELED", resultSet.getInt("CANCELED"));
            setAttribute("HIERARCHY_ID", resultSet.getInt("HIERARCHY_ID"));
           // setAttribute("REJECTED_REMARKS",resultSet.getString("REJECTED_REMARKS"));

            objDToFDetails.clear();

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            if (HistoryView) {
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_DETAIL_H WHERE LAA_DOC_NO='" + resultSet.getString("LAA_DOC_NO") + "'  AND REVISION_NO=" + RevNo + " ORDER BY SR_NO DESC");
            } else {
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_DETAIL WHERE LAA_DOC_NO='" + resultSet.getString("LAA_DOC_NO") + "'  ORDER BY LAA_DOC_NO");
            }
            int i=0;
            while (resultSetTemp.next()) {
                serialNoCounter++;
                i++;
                clsLoomAllocationAmendDetails ObjLoomAllocationAmendDetails = new clsLoomAllocationAmendDetails();

                ObjLoomAllocationAmendDetails.setAttribute("LAA_DOC_NO", resultSetTemp.getString("LAA_DOC_NO"));
                ObjLoomAllocationAmendDetails.setAttribute("SR_NO", i+"");

                ObjLoomAllocationAmendDetails.setAttribute("LAA_DOC_DATE", resultSetTemp.getString("LAA_DOC_DATE"));
                ObjLoomAllocationAmendDetails.setAttribute("LOOM_NO", resultSetTemp.getString("LOOM_NO"));
                ObjLoomAllocationAmendDetails.setAttribute("PPC_PRIORITY", resultSetTemp.getString("PPC_PRIORITY"));
                ObjLoomAllocationAmendDetails.setAttribute("STYLE", resultSetTemp.getString("STYLE"));
                ObjLoomAllocationAmendDetails.setAttribute("REED_CODE", resultSetTemp.getString("REED_CODE"));
                ObjLoomAllocationAmendDetails.setAttribute("REED_SPACE_MIN", resultSetTemp.getString("REED_SPACE_MIN"));
                ObjLoomAllocationAmendDetails.setAttribute("REED_SPACE_MAX", resultSetTemp.getString("REED_SPACE_MAX"));
                ObjLoomAllocationAmendDetails.setAttribute("TEX_COUNT", resultSetTemp.getString("TEX_COUNT"));
                ObjLoomAllocationAmendDetails.setAttribute("WARP_YARN_DESC", resultSetTemp.getString("WARP_YARN_DESC"));
                ObjLoomAllocationAmendDetails.setAttribute("ACTUAL_WARP_LENGTH", resultSetTemp.getString("ACTUAL_WARP_LENGTH"));
                ObjLoomAllocationAmendDetails.setAttribute("ACHIEVABLE_WOVEN_LENGTH", resultSetTemp.getString("ACHIEVABLE_WOVEN_LENGTH"));
                
                objDToFDetails.put(Integer.toString(serialNoCounter), ObjLoomAllocationAmendDetails);
            }
            resultSetTemp.close();
            statementTemp.close();
            
            
            objDToFDetails_ForAdd.clear();
            
            statementTemp_Add = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            if (HistoryView) {
                resultSetTemp_Add = statementTemp_Add.executeQuery("SELECT * FROM  PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_DETAIL_ADD WHERE LAA_DOC_NO='" + resultSet.getString("LAA_DOC_NO") + "'  ORDER BY LAA_DOC_NO");
            } else {
                resultSetTemp_Add = statementTemp_Add.executeQuery("SELECT * FROM  PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_DETAIL_ADD WHERE LAA_DOC_NO='" + resultSet.getString("LAA_DOC_NO") + "'  ORDER BY LAA_DOC_NO");
            }
            i=0;
            while (resultSetTemp_Add.next()) {
                //serialNoCounter++;
                i++;
                serialNoCounter = i;
                clsLoomAllocationAmendDetails ObjLoomAllocationAmendDetails = new clsLoomAllocationAmendDetails();

                ObjLoomAllocationAmendDetails.setAttribute("LAA_DOC_NO", resultSetTemp_Add.getString("LAA_DOC_NO"));
                ObjLoomAllocationAmendDetails.setAttribute("SR_NO", i+"");

                ObjLoomAllocationAmendDetails.setAttribute("LAA_DOC_DATE", resultSetTemp_Add.getString("LAA_DOC_DATE"));
                ObjLoomAllocationAmendDetails.setAttribute("LOOM_NO", resultSetTemp_Add.getString("LOOM_NO"));
                ObjLoomAllocationAmendDetails.setAttribute("PPC_PRIORITY", resultSetTemp_Add.getString("PPC_PRIORITY"));
                ObjLoomAllocationAmendDetails.setAttribute("STYLE", resultSetTemp_Add.getString("STYLE"));
                ObjLoomAllocationAmendDetails.setAttribute("REED_CODE", resultSetTemp_Add.getString("REED_CODE"));
                ObjLoomAllocationAmendDetails.setAttribute("REED_SPACE_MIN", resultSetTemp_Add.getString("REED_SPACE_MIN"));
                ObjLoomAllocationAmendDetails.setAttribute("REED_SPACE_MAX", resultSetTemp_Add.getString("REED_SPACE_MAX"));
                ObjLoomAllocationAmendDetails.setAttribute("TEX_COUNT", resultSetTemp_Add.getString("TEX_COUNT"));
                ObjLoomAllocationAmendDetails.setAttribute("WARP_YARN_DESC", resultSetTemp_Add.getString("WARP_YARN_DESC"));
                ObjLoomAllocationAmendDetails.setAttribute("ACTUAL_WARP_LENGTH", resultSetTemp_Add.getString("ACTUAL_WARP_LENGTH"));
                ObjLoomAllocationAmendDetails.setAttribute("ACHIEVABLE_WOVEN_LENGTH", resultSetTemp_Add.getString("ACHIEVABLE_WOVEN_LENGTH"));
                
                objDToFDetails_ForAdd.put(Integer.toString(serialNoCounter), ObjLoomAllocationAmendDetails);
            }
            resultSetTemp_Add.close();
            statementTemp_Add.close();
            
            
            objDToFDetails_ForUpdate.clear();
            
            statementTemp_Update = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            if (HistoryView) {
                resultSetTemp_Update = statementTemp_Update.executeQuery("SELECT * FROM  PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_DETAIL_UPDATE WHERE LAA_DOC_NO='" + resultSet.getString("LAA_DOC_NO") + "'  ORDER BY LAA_DOC_NO");
            } else {
                resultSetTemp_Update = statementTemp_Update.executeQuery("SELECT * FROM  PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_DETAIL_UPDATE WHERE LAA_DOC_NO='" + resultSet.getString("LAA_DOC_NO") + "'  ORDER BY LAA_DOC_NO");
            }
            i=0;
            while (resultSetTemp_Update.next()) {
                //serialNoCounter++;
                i++;
                serialNoCounter = i;
                clsLoomAllocationAmendDetails ObjLoomAllocationAmendDetails = new clsLoomAllocationAmendDetails();

                ObjLoomAllocationAmendDetails.setAttribute("LAA_DOC_NO", resultSetTemp_Update.getString("LAA_DOC_NO"));
                ObjLoomAllocationAmendDetails.setAttribute("SR_NO", i+"");

                ObjLoomAllocationAmendDetails.setAttribute("LAA_DOC_DATE", resultSetTemp_Update.getString("LAA_DOC_DATE"));
                ObjLoomAllocationAmendDetails.setAttribute("LOOM_NO", resultSetTemp_Update.getString("LOOM_NO"));
                ObjLoomAllocationAmendDetails.setAttribute("PPC_PRIORITY", resultSetTemp_Update.getString("PPC_PRIORITY"));
                ObjLoomAllocationAmendDetails.setAttribute("STYLE", resultSetTemp_Update.getString("STYLE"));
                ObjLoomAllocationAmendDetails.setAttribute("REED_CODE", resultSetTemp_Update.getString("REED_CODE"));
                ObjLoomAllocationAmendDetails.setAttribute("REED_SPACE_MIN", resultSetTemp_Update.getString("REED_SPACE_MIN"));
                ObjLoomAllocationAmendDetails.setAttribute("REED_SPACE_MAX", resultSetTemp_Update.getString("REED_SPACE_MAX"));
                ObjLoomAllocationAmendDetails.setAttribute("TEX_COUNT", resultSetTemp_Update.getString("TEX_COUNT"));
                ObjLoomAllocationAmendDetails.setAttribute("WARP_YARN_DESC", resultSetTemp_Update.getString("WARP_YARN_DESC"));
                ObjLoomAllocationAmendDetails.setAttribute("ACTUAL_WARP_LENGTH", resultSetTemp_Update.getString("ACTUAL_WARP_LENGTH"));
                ObjLoomAllocationAmendDetails.setAttribute("ACHIEVABLE_WOVEN_LENGTH", resultSetTemp_Update.getString("ACHIEVABLE_WOVEN_LENGTH"));
                ObjLoomAllocationAmendDetails.setAttribute("UPDATE_KEY", resultSetTemp_Update.getString("UPDATE_KEY"));
                
                objDToFDetails_ForUpdate.put(Integer.toString(serialNoCounter), ObjLoomAllocationAmendDetails);
            }
            resultSetTemp_Update.close();
            statementTemp_Update.close();
            
            
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
            objDToFDetails.clear();

            String strSql = "SELECT * FROM PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER_H WHERE LAA_DOC_NO = " + pDocNo + "";
            System.out.println("STR SQL : " + strSql);
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery(strSql);
            resultSet.first();
            setAttribute("LAA_DOC_NO", resultSet.getString("LAA_DOC_NO"));
            setAttribute("LAA_DOC_DATE", resultSet.getDate("LAA_DOC_DATE"));
            setAttribute("REMARK", resultSet.getString("REMARK"));
            setAttribute("REVISION_NO", resultSet.getString("REVISION_NO"));
            setAttribute("UPDATED_BY", resultSet.getString("UPDATED_BY"));
            setAttribute("LAA_DOC_DATE", resultSet.getString("LAA_DOC_DATE"));
            setAttribute("HIERARCHY_ID", resultSet.getInt("HIERARCHY_ID"));            

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_DETAIL_H WHERE LAA_DOC_NO='" + pDocNo + "'");

            while (resultSetTemp.next()) {
                serialNoCounter++;

                clsLoomAllocationAmendDetails ObjLoomAllocationAmendDetails = new clsLoomAllocationAmendDetails();

                ObjLoomAllocationAmendDetails.setAttribute("SR_NO", resultSetTemp.getString("SR_NO"));
                ObjLoomAllocationAmendDetails.setAttribute("LAA_DOC_NO", resultSetTemp.getString("LAA_DOC_NO"));

                ObjLoomAllocationAmendDetails.setAttribute("PARTY_CODE", resultSetTemp.getString("PARTY_CODE"));
                ObjLoomAllocationAmendDetails.setAttribute("PARTY_NAME", resultSetTemp.getString("PARTY_NAME"));
                ObjLoomAllocationAmendDetails.setAttribute("MACHINE_NO", resultSetTemp.getString("MACHINE_NO"));
                ObjLoomAllocationAmendDetails.setAttribute("POSITION", resultSetTemp.getString("POSITION"));
                ObjLoomAllocationAmendDetails.setAttribute("POSITION_DESC", resultSetTemp.getString("POSITION_DESC"));
                ObjLoomAllocationAmendDetails.setAttribute("PIECE_NO", resultSetTemp.getString("PIECE_NO"));
                ObjLoomAllocationAmendDetails.setAttribute("PRODUCT", resultSetTemp.getString("PRODUCT"));
                ObjLoomAllocationAmendDetails.setAttribute("PRODUCT_DESCRIPTION", resultSetTemp.getString("PRODUCT_DESCRIPTION"));
                ObjLoomAllocationAmendDetails.setAttribute("PRODUCT_GROUP", resultSetTemp.getString("PRODUCT_GROUP"));
                ObjLoomAllocationAmendDetails.setAttribute("LENGTH", resultSetTemp.getString("LENGTH"));
                ObjLoomAllocationAmendDetails.setAttribute("WIDTH", resultSetTemp.getString("WIDTH"));
                ObjLoomAllocationAmendDetails.setAttribute("GSM", resultSetTemp.getString("GSM"));
                ObjLoomAllocationAmendDetails.setAttribute("THEORTICAL_WEIGHT", resultSetTemp.getString("THEORTICAL_WEIGHT"));
                ObjLoomAllocationAmendDetails.setAttribute("SQ_MT", resultSetTemp.getString("SQ_MT"));
                ObjLoomAllocationAmendDetails.setAttribute("STYLE", resultSetTemp.getString("STYLE"));
                ObjLoomAllocationAmendDetails.setAttribute("REQ_MONTH", resultSetTemp.getString("REQ_MONTH"));
                ObjLoomAllocationAmendDetails.setAttribute("SYN_PER", resultSetTemp.getString("SYN_PER"));
                ObjLoomAllocationAmendDetails.setAttribute("REMARK", resultSetTemp.getString("REMARK"));
                ObjLoomAllocationAmendDetails.setAttribute("BILL_LENGTH", resultSetTemp.getString("BILL_LENGTH"));
                ObjLoomAllocationAmendDetails.setAttribute("BILL_WIDTH", resultSetTemp.getString("BILL_WIDTH"));
                ObjLoomAllocationAmendDetails.setAttribute("BILL_WEIGHT", resultSetTemp.getString("BILL_WEIGHT"));
                ObjLoomAllocationAmendDetails.setAttribute("BILL_SQMTR", resultSetTemp.getString("BILL_SQMTR"));
                ObjLoomAllocationAmendDetails.setAttribute("BILL_GSM", resultSetTemp.getString("BILL_GSM"));
                ObjLoomAllocationAmendDetails.setAttribute("BILL_PRODUCT_CODE", resultSetTemp.getString("BILL_PRODUCT_CODE"));
                ObjLoomAllocationAmendDetails.setAttribute("PIECE_STAGE", resultSetTemp.getString("PIECE_STAGE"));

                objDToFDetails.put(Integer.toString(serialNoCounter), ObjLoomAllocationAmendDetails);
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

            rsTmp = stTmp.executeQuery("SELECT * FROM PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER_H WHERE LAA_DOC_NO='" + productionDocumentNo + "'");
            while (rsTmp.next()) {
                clsLoomAllocationAmend felt_order = new clsLoomAllocationAmend();

                felt_order.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                felt_order.setAttribute("MODIFIED_BY", rsTmp.getString("MODIFIED_BY"));
                felt_order.setAttribute("MODIFIED_DATE", rsTmp.getString("MODIFIED_DATE"));
                felt_order.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                felt_order.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                felt_order.setAttribute("APPROVER_REMARKS", rsTmp.getString("APPROVER_REMARKS"));
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

    public boolean ShowHistory(String pDocNo) {
        Ready = false;
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER_H WHERE LAA_DOC_NO ='" + pDocNo + "'");
            Ready = true;

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
               // strSQL = "SELECT DISTINCT PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER.LAA_DOC_NO,PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER.LAA_DOC_DATE,PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE FROM PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER, PRODUCTION.FELT_PROD_DOC_DATA   WHERE PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER.LAA_DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.LAA_DOC_NO AND STATUS='W' AND MODULE_ID=" + ModuleID + " AND PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER.CANCELED=0  AND USER_ID=" + pUserID + " ORDER BY RECEIVED_DATE,PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER.LAA_DOC_NO";
                strSQL = "SELECT DISTINCT H.LAA_DOC_NO,H.LAA_DOC_DATE,PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE,group_concat(PIECE_NO) AS PIECE_NO \n" +
"\n" +
"FROM PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER H,PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_DETAIL D, PRODUCTION.FELT_PROD_DOC_DATA \n" +
"  \n" +
"WHERE H.LAA_DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.LAA_DOC_NO AND STATUS='W' AND MODULE_ID="+ModuleID+" AND H.CANCELED=0  AND USER_ID="+pUserID+" AND H.LAA_DOC_NO=D.LAA_DOC_NO GROUP BY H.LAA_DOC_NO ORDER BY H.LAA_DOC_NO";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                //strSQL = "SELECT DISTINCT PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER.LAA_DOC_NO,PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER.LAA_DOC_DATE,PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE FROM PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER, PRODUCTION.FELT_PROD_DOC_DATA   WHERE PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER.LAA_DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.LAA_DOC_NO AND STATUS='W' AND MODULE_ID=" + ModuleID + " AND PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER.CANCELED=0  AND USER_ID=" + pUserID + " ORDER BY RECEIVED_DATE,PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER.LAA_DOC_NO";
                strSQL = "SELECT DISTINCT H.LAA_DOC_NO,H.LAA_DOC_DATE,PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE,group_concat(PIECE_NO) AS PIECE_NO \n" +
"\n" +
"FROM PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER H,PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_DETAIL D, PRODUCTION.FELT_PROD_DOC_DATA   \n" +
"\n" +
"WHERE H.LAA_DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.LAA_DOC_NO AND STATUS='W' AND MODULE_ID="+ModuleID+" AND H.CANCELED=0  AND USER_ID="+pUserID+"  AND H.LAA_DOC_NO=D.LAA_DOC_NO GROUP BY H.LAA_DOC_NO ORDER BY RECEIVED_DATE,H.LAA_DOC_NO";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                //strSQL = "SELECT DISTINCT PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER.LAA_DOC_NO,PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER.LAA_DOC_DATE,PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE FROM PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER, PRODUCTION.FELT_PROD_DOC_DATA   WHERE PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER.LAA_DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.LAA_DOC_NO AND STATUS='W' AND MODULE_ID=" + ModuleID + " AND PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER.CANCELED=0  AND USER_ID=" + pUserID + "  ORDER BY PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER.LAA_DOC_NO";
                strSQL = "SELECT DISTINCT H.LAA_DOC_NO,H.LAA_DOC_DATE,PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE ,group_concat(PIECE_NO) AS PIECE_NO\n" +
"\n" +
"FROM PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER H,PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_DETAIL D, PRODUCTION.FELT_PROD_DOC_DATA  \n" +
"\n" +
" WHERE H.LAA_DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.LAA_DOC_NO AND STATUS='W' AND MODULE_ID="+ModuleID+" AND H.CANCELED=0  AND USER_ID="+pUserID+"   AND H.LAA_DOC_NO=D.LAA_DOC_NO GROUP BY H.LAA_DOC_NO   ORDER BY H.LAA_DOC_NO";
            }

            ResultSet rsTmp = data.getConn().createStatement().executeQuery(strSQL);

            Counter = 0;
            while (rsTmp.next()) {

                Counter = Counter + 1;
                clsLoomAllocationAmend ObjDoc = new clsLoomAllocationAmend();

                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("LAA_DOC_NO", rsTmp.getString("LAA_DOC_NO"));
                ObjDoc.setAttribute("LAA_DOC_DATE", rsTmp.getString("LAA_DOC_DATE"));
                ObjDoc.setAttribute("RECEIVED_DATE", rsTmp.getString("RECEIVED_DATE"));
                ObjDoc.setAttribute("PIECE_NO", rsTmp.getString("PIECE_NO"));

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

    public static boolean CanCancel(String pDocNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean CanCancel = false;

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            System.out.println("SELECT LAA_DOC_NO FROM PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER WHERE LAA_DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");
            rsTmp = stTmp.executeQuery("SELECT LAA_DOC_NO FROM PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER WHERE LAA_DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");

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

    public static void CancelDoc(String pAmendNo) {
        ResultSet rsTmp = null;

        if (CanCancel(pAmendNo)) {

            boolean Approved = false;

            try {
                rsTmp = data.getResult("SELECT APPROVED FROM PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER WHERE LAA_DOC_NO='" + pAmendNo + "' ");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    Approved = rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();

                if (!Approved) {
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE LAA_DOC_NO='" + pAmendNo + "' AND MODULE_ID='" + ModuleID + "'");
                }
                data.Execute("UPDATE PRODUCTION.PPC_LOOM_ALLOCATION_MASTER_AMEND_HEADER SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE LAA_DOC_NO='" + pAmendNo + "'");

            } catch (Exception e) {

            }
        }

    }
}
