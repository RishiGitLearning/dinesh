/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.SalesPieceClubbing;

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
public class clsSalesPieceClubbing {

    public String LastError = "";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet, rsHeader;
    //private ResultSet rsResultSet,

    public HashMap props;
    public boolean Ready = false;
    //Felt Order Collection
    public HashMap objDToFDetails;

    //History Related properties
    public boolean HistoryView = false;
    private String historyAmendDate = "";
    private String historyAmendID = "";
    private static int ModuleID = 870;

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
    public clsSalesPieceClubbing() {
        LastError = "";
        props = new HashMap();

        props.put("PC_DOC_NO", new Variant(""));
        props.put("PC_DOC_DATE", new Variant(""));
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

        props.put("PC_DOC_NO", new Variant(""));
        props.put("PC_DOC_DATE", new Variant(""));
        props.put("PIECE_NO", new Variant(""));
        props.put("NO_OF_DIVISION", new Variant(""));
        props.put("DIVISION_BY", new Variant(""));
        props.put("REMARK", new Variant(""));

        objDToFDetails = new HashMap();
    }

    public boolean LoadData() {
        Ready = false;
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER ORDER BY PC_DOC_NO");
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
        ResultSet resultSetTemp, resultSetHistory, rsHeader, rsHeaderH;
        Statement statementTemp, statementHistory, stHeader, stHeaderH;
        try {
            // Felt Order Updation data connection
            connection = data.getConn();
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL WHERE PC_DOC_NO='1'");

            // Felt order Updation data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL_H WHERE PC_DOC_NO='1'");

            stHeader = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHeader = stHeader.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER WHERE PC_DOC_NO='1'");

            stHeaderH = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHeaderH = stHeaderH.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER_H WHERE PC_DOC_NO='1'");

            //setAttribute("PC_DOC_NO",);
            rsHeader.first();
            rsHeader.moveToInsertRow();

            rsHeader.updateString("PC_DOC_NO", getAttribute("PC_DOC_NO").getString());
            rsHeader.updateString("PC_DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PC_DOC_DATE").getString()));

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

            rsHeaderH.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsHeaderH.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            rsHeaderH.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
            rsHeaderH.updateString("PC_DOC_NO", getAttribute("PC_DOC_NO").getString());
            rsHeaderH.updateString("PC_DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PC_DOC_DATE").getString()));

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
                clsSalesPieceClibbingDetails ObjFeltSalesOrderDetails = (clsSalesPieceClibbingDetails) objDToFDetails.get(Integer.toString(i));

                //Insert records into Felt order Amendment table
                resultSetTemp.moveToInsertRow();

                resultSetTemp.updateString("PC_DOC_NO", ObjFeltSalesOrderDetails.getAttribute("PC_DOC_NO").getString());
                //EITLERPGLOBAL.formatDateDB(getAttribute("PC_DOC_DATE").getString())
                
                resultSetTemp.updateString("PC_DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PC_DOC_DATE").getString()));
                resultSetTemp.updateString("PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                resultSetTemp.updateString("REMARK", ObjFeltSalesOrderDetails.getAttribute("REMARK").getString());
                resultSetTemp.updateString("REQ_MONTH", ObjFeltSalesOrderDetails.getAttribute("REQ_MONTH").getString());
                resultSetTemp.updateString("OC_MONTH", ObjFeltSalesOrderDetails.getAttribute("OC_MONTH").getString());
                resultSetTemp.updateString("CURRENT_SALES_PLAN", ObjFeltSalesOrderDetails.getAttribute("CURRENT_SALES_PLAN").getString());
                resultSetTemp.updateString("PIECE_STAGE", ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString());
                
                
                resultSetTemp.insertRow();

                //Insert records into Felt Order Amendment History Table
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO", 1);

                resultSetHistory.updateString("PC_DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PC_DOC_DATE").getString()));
                resultSetHistory.updateString("PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                resultSetHistory.updateString("REMARK", ObjFeltSalesOrderDetails.getAttribute("REMARK").getString());
                resultSetHistory.updateString("REQ_MONTH", ObjFeltSalesOrderDetails.getAttribute("REQ_MONTH").getString());
                resultSetHistory.updateString("OC_MONTH", ObjFeltSalesOrderDetails.getAttribute("OC_MONTH").getString());
                resultSetHistory.updateString("CURRENT_SALES_PLAN", ObjFeltSalesOrderDetails.getAttribute("CURRENT_SALES_PLAN").getString());
                resultSetHistory.updateString("PIECE_STAGE", ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString());
                
                resultSetHistory.insertRow();

                // Final Approval and save to PIECE REGISTER 
                if (getAttribute("APPROVAL_STATUS").getString().equals("F") && !ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString().equals("")) {
                   
                    
                            
                }

            }

            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = ModuleID; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("PC_DOC_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator = true;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("REJECTED_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "PC_DOC_NO";

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

        ResultSet resultSetTemp, resultSetHistory, rsHeader, rsHeaderH, rsRegister;
        Statement statementTemp, statementHistory, stHeader, stHeaderH, stRegister;
        int revisionNo;
        try {
            // Production data connection

            // Production data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //Get the Maximum Revision No in History Table.
            resultSetHistory = statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL_H WHERE PC_DOC_NO='" + getAttribute("PC_DOC_NO").getString() + "'");
            resultSetHistory.first();
            if (resultSetHistory.getRow() > 0) {
                revisionNo = resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from production data table before insert
            //statementHistory.execute("DELETE FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL WHERE PC_DOC_NO='"+getAttribute("PC_DOC_NO").getString()+"'");

            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL_H WHERE PC_DOC_NO=''");

            stHeaderH = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHeaderH = stHeaderH.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER_H WHERE PC_DOC_NO=''");

            //Now Update records into FELT_CN_TEMP_HEADER tables
            resultSet.updateString("PC_DOC_NO", getAttribute("PC_DOC_NO").getString());
            resultSet.updateString("PC_DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PC_DOC_DATE").getString()));

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
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER_H WHERE PC_DOC_NO='" + getAttribute("PC_DOC_NO").getString() + "'");

            RevNo++;

            rsHeaderH.moveToInsertRow();

            rsHeaderH.updateString("REVISION_NO", RevNo + "");

            rsHeaderH.updateString("PC_DOC_NO", getAttribute("PC_DOC_NO").getString());
            rsHeaderH.updateString("PC_DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PC_DOC_DATE").getString()));

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
            String OrderNo = getAttribute("PC_DOC_NO").getString();
            data.Execute("DELETE FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL WHERE PC_DOC_NO='" + OrderNo + "'");

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL WHERE PC_DOC_NO='1'");

            int RevNoH = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL_H WHERE PC_DOC_NO='" + getAttribute("PC_DOC_NO").getString() + "'");
            RevNoH++;

            resultSetHistory.moveToInsertRow();

            String Pieces = "";
            String Party = "";
            for (int i = 1; i <= objDToFDetails.size(); i++) {
                clsSalesPieceClibbingDetails ObjFeltSalesOrderDetails = (clsSalesPieceClibbingDetails) objDToFDetails.get(Integer.toString(i));

                resultSetTemp.moveToInsertRow();

                resultSetTemp.updateString("PC_DOC_NO", ObjFeltSalesOrderDetails.getAttribute("PC_DOC_NO").getString());
                resultSetTemp.updateString("PC_DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PC_DOC_DATE").getString()));
                resultSetTemp.updateString("PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                resultSetTemp.updateString("REMARK", ObjFeltSalesOrderDetails.getAttribute("REMARK").getString());
                resultSetTemp.updateString("REQ_MONTH", ObjFeltSalesOrderDetails.getAttribute("REQ_MONTH").getString());
                resultSetTemp.updateString("OC_MONTH", ObjFeltSalesOrderDetails.getAttribute("OC_MONTH").getString());
                resultSetTemp.updateString("CURRENT_SALES_PLAN", ObjFeltSalesOrderDetails.getAttribute("CURRENT_SALES_PLAN").getString());
                resultSetTemp.updateString("PIECE_STAGE", ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString());
                resultSetTemp.updateString("CURRENT_STATUS", "Active");
                resultSetTemp.updateString("USER_ID", EITLERPGLOBAL.gUserID+"");
                
                if(i==1)
                {
                    Pieces = ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString();
                    Party = data.getStringValueFromDB("SELECT PR_PARTY_CODE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='"+ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString()+"'");
                }
                else
                {
                    Pieces = Pieces +","+ ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString();
                }
                
                resultSetTemp.insertRow();

                resultSetHistory.updateInt("REVISION_NO", RevNoH);
                resultSetHistory.updateString("PC_DOC_NO", ObjFeltSalesOrderDetails.getAttribute("PC_DOC_NO").getString());
                resultSetHistory.updateString("PC_DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("PC_DOC_DATE").getString()));
                resultSetHistory.updateString("PIECE_NO", ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString());
                resultSetHistory.updateString("REMARK", ObjFeltSalesOrderDetails.getAttribute("REMARK").getString());
                resultSetHistory.updateString("REQ_MONTH", ObjFeltSalesOrderDetails.getAttribute("REQ_MONTH").getString());
                resultSetHistory.updateString("OC_MONTH", ObjFeltSalesOrderDetails.getAttribute("OC_MONTH").getString());
                resultSetHistory.updateString("CURRENT_SALES_PLAN", ObjFeltSalesOrderDetails.getAttribute("CURRENT_SALES_PLAN").getString());
                resultSetHistory.updateString("PIECE_STAGE", ObjFeltSalesOrderDetails.getAttribute("PIECE_STAGE").getString());
                

                resultSetHistory.insertRow();

                
            }

            // Final Approval and save to PIECE REGISTER 
            if (getAttribute("APPROVAL_STATUS").getString().equals("F") && !Pieces.equals("")) {

                String incharge = data.getStringValueFromDB("SELECT INCHARGE_CD FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+Party+"'");
                data.Execute(" INSERT INTO PRODUCTION.FELT_SALES_FOLLOWUP_CLUBBING_DETAIL "
                    + "(DOC_NO, SR_NO, DOC_MONTH, DOC_YEAR, PIECE_NO, PARTY_CODE, PARTY_NAME, MACHINE_NO, POSITION, UPN, REQUESTED_MONTH, OC_MONTH, CURR_SCH_MONTH, RE_SCH_MONTH, PRODUCT_CODE, BILL_PRODUCT_CODE, PR_GROUP, STYLE, BILL_STYLE, LENGTH, ACTUAL_LENGTH, BILL_LENGTH, WIDTH, ACTUAL_WIDTH, SQMTR, BILL_SQMTR, GSM, BILL_GSM, TENDER_GSM, TH_WEIGHT, TENDER_WEIGHT, ACTUAL_WEIGHT, BILL_WEIGHT, WEAVING_WEIGHT, NEEDLING_WEIGHT, SEAM_WEIGHT, SPLICE_WEIGHT, WEAVING_DATE, MENDING_DATE, NEEDLING_DATE, FINISHING_DATE, INCHARGE, PIECE_STAGE, PIECE_OBSOLETE, OBSOLETE_DATE, REQUESTED_MONTH_DATE, OC_MONTH_DATE, CURR_SCH_MONTH_DATE, RE_SCH_MONTH_DATE, CUR_PIECE_STAGE, OC_FALL_MONTH, OC_FALL_MONTH_DATE, INVOICE_NO, INVOICE_DATE, UNABLE_TO_CONTACT, DATE_OF_COMMUNICATION, MODE_OF_COMMUNICATION, CONTACT_PERSON, CONTACTED_NO, PARTY_JUSTIFICATION, AREA_MANAGER_COMMENT, RESCHEDULED, EXPECTED_DISPATCH_DATE, PREVIOUS_DISPATCH_DATE, EXP_DISPATCH_HISTORY, DATE_COUNTER_MONTH, RESCHEDULED_MONTH_HISTORY, RESCHEDULED_MONTH_COUNT, DELAY_REASON, INVOICE_VALUE, ADDITIONAL_REMARK, FOLLOWUP_DATE, EXPECTED_MONTH_OF_DISPATCH) values "
                    + "('"+getAttribute("PC_DOC_NO").getString()+"', '', '', '', '"+Pieces+"', '"+Party+"', '"+clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, Party)+"', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '"+incharge+"', '', '', '', '', '', '', '', '', '', '', '', '', '', '0000-00-00', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '0000-00-00', '')");
                
                data.Execute("UPDATE PRODUCTION.FELT_SALES_FOLLOWUP_CLUBBING_DETAIL D,PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER H\n" +
                            "SET \n" +
                            "D.EXPECTED_MONTH_OF_DISPATCH=H.LAST_CURR_SCH_MONTH WHERE D.DOC_NO=H.PC_DOC_NO");

            }
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = ModuleID; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("PC_DOC_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator = true;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "PC_DOC_NO";

            if (getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFeltProductionApprovalFlow.To = (int) getAttribute("SEND_DOC_TO").getVal();
                ObjFeltProductionApprovalFlow.ExplicitSendTo = true;
            }
            //if(getAttribute("APPROVAL_STATUS").getString().equals("A") || getAttribute("APPROVAL_STATUS").getString().equals("R")) {
//                    String Subject = "Felt Order Pending Document : "+getAttribute("PC_DOC_NO").getString();
//                    String Message = "Document No : "+getAttribute("PC_DOC_NO").getString()+" is added in your PENDING DOCUMENT"
//                            + "\n\n\n\n SDML-ERP : http://200.0.0.227:8080/SDMLERP";
//                 
//                    String send_to = clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, (int)getAttribute("TO").getVal());
//
//                    System.out.println("USERID : "+(int)getAttribute("TO").getVal()+", send_to : "+send_to);
//                    try{
//                        System.out.println(" Host IP : "+EITLERPGLOBAL.SMTPHostIP);
//                        JMail.SendMail(EITLERPGLOBAL.SMTPHostIP, "sdmlerp@dineshmills.com", send_to, Message, Subject, "sdmlerp@dineshmills.com");
//                    }catch(Exception e)
//                    {
//                        e.printStackTrace();
//                    }
            //}

            //==== Handling Rejected Documents ==========//
            boolean IsRejected = getAttribute("REJECTED").getBool();

            if (IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER SET REJECTED=0,CHANGED=1 WHERE PC_DOC_NO ='" + getAttribute("PC_DOC_NO").getString() + "'");
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
    public String getMonthName(int month) {
        String month_name = "";
        if (month == 1) {
            month_name = "Jan";
        } else if (month == 2) {
            month_name = "Feb";
        } else if (month == 3) {
            month_name = "Mar";
        } else if (month == 4) {
            month_name = "Apr";
        } else if (month == 5) {
            month_name = "May";
        } else if (month == 6) {
            month_name = "Jun";
        } else if (month == 7) {
            month_name = "Jul";
        } else if (month == 8) {
            month_name = "Aug";
        } else if (month == 9) {
            month_name = "Sep";
        } else if (month == 10) {
            month_name = "Oct";
        } else if (month == 11) {
            month_name = "Nov";
        } else if (month == 12) {
            month_name = "Dec";
        }
        return month_name;
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER WHERE  PC_DOC_NO='" + documentNo + "' AND APPROVED=" + 1;
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=" + ModuleID + " AND USER_ID=" + userID + " AND PC_DOC_NO='" + documentNo + "' AND STATUS='W'";
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();

                if (rsTmp.getInt("COUNT") > 0) {
                    strSQL = "DELETE FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER WHERE "
                            + " PC_DOC_DATE = '" + EITLERPGLOBAL.formatDateDB(stringProductionDate) + "'"
                            + " AND PC_DOC_NO ='" + documentNo + "'";

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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER WHERE PC_DOC_NO='" + orderupdDocumentNo + "' AND APPROVED=1";
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
            String strSql = "SELECT * FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER WHERE  " + stringFindQuery + "";
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
        ResultSet resultSetTemp;
        Statement statementTemp;
        int serialNoCounter = 0;
        int RevNo = 0;

        try {

            setAttribute("REVISION_NO", 0);

            setAttribute("PC_DOC_NO", resultSet.getString("PC_DOC_NO"));
            setAttribute("PC_DOC_DATE", resultSet.getDate("PC_DOC_DATE"));

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
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL_H WHERE PC_DOC_NO='" + resultSet.getString("PC_DOC_NO") + "'  AND REVISION_NO=" + RevNo + " ORDER BY SR_NO DESC");
            } else {
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL WHERE PC_DOC_NO='" + resultSet.getString("PC_DOC_NO") + "'  ORDER BY PC_DOC_NO");
            }
            int i=0;
            while (resultSetTemp.next()) {
                serialNoCounter++;
                i++;
                clsSalesPieceClibbingDetails ObjFeltSalesOrderDetails = new clsSalesPieceClibbingDetails();

                ObjFeltSalesOrderDetails.setAttribute("PC_DOC_NO", resultSetTemp.getString("PC_DOC_NO"));
                ObjFeltSalesOrderDetails.setAttribute("SR_NO", i+"");

                
                ObjFeltSalesOrderDetails.setAttribute("PIECE_NO", resultSetTemp.getString("PIECE_NO"));
                ObjFeltSalesOrderDetails.setAttribute("REMARK", resultSetTemp.getString("REMARK"));
                ObjFeltSalesOrderDetails.setAttribute("REQ_MONTH", resultSetTemp.getString("REQ_MONTH"));
                ObjFeltSalesOrderDetails.setAttribute("OC_MONTH", resultSetTemp.getString("OC_MONTH"));
                ObjFeltSalesOrderDetails.setAttribute("CURRENT_SALES_PLAN", resultSetTemp.getString("CURRENT_SALES_PLAN"));
                ObjFeltSalesOrderDetails.setAttribute("PIECE_STAGE", resultSetTemp.getString("PIECE_STAGE"));
                
                objDToFDetails.put(Integer.toString(serialNoCounter), ObjFeltSalesOrderDetails);
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

            //Now Populate the collection, first clear the collection
            objDToFDetails.clear();

            String strSql = "SELECT * FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER_H WHERE PC_DOC_NO = " + pDocNo + "";
            System.out.println("STR SQL : " + strSql);
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery(strSql);
            resultSet.first();
            setAttribute("PC_DOC_NO", resultSet.getString("PC_DOC_NO"));
            setAttribute("PC_DOC_DATE", resultSet.getDate("PC_DOC_DATE"));
            setAttribute("REMARK", resultSet.getString("REMARK"));
            setAttribute("REVISION_NO", resultSet.getString("REVISION_NO"));
            setAttribute("UPDATED_BY", resultSet.getString("UPDATED_BY"));
            setAttribute("PC_DOC_DATE", resultSet.getString("PC_DOC_DATE"));
            setAttribute("HIERARCHY_ID", resultSet.getInt("HIERARCHY_ID"));            

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL_H WHERE PC_DOC_NO='" + pDocNo + "'");

            while (resultSetTemp.next()) {
                serialNoCounter++;

                clsSalesPieceClibbingDetails ObjFeltSalesOrderDetails = new clsSalesPieceClibbingDetails();

                ObjFeltSalesOrderDetails.setAttribute("SR_NO", resultSetTemp.getString("SR_NO"));
                ObjFeltSalesOrderDetails.setAttribute("PC_DOC_NO", resultSetTemp.getString("PC_DOC_NO"));

                ObjFeltSalesOrderDetails.setAttribute("PARTY_CODE", resultSetTemp.getString("PARTY_CODE"));
                ObjFeltSalesOrderDetails.setAttribute("PARTY_NAME", resultSetTemp.getString("PARTY_NAME"));
                ObjFeltSalesOrderDetails.setAttribute("MACHINE_NO", resultSetTemp.getString("MACHINE_NO"));
                ObjFeltSalesOrderDetails.setAttribute("POSITION", resultSetTemp.getString("POSITION"));
                ObjFeltSalesOrderDetails.setAttribute("POSITION_DESC", resultSetTemp.getString("POSITION_DESC"));
                ObjFeltSalesOrderDetails.setAttribute("PIECE_NO", resultSetTemp.getString("PIECE_NO"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT", resultSetTemp.getString("PRODUCT"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT_DESCRIPTION", resultSetTemp.getString("PRODUCT_DESCRIPTION"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT_GROUP", resultSetTemp.getString("PRODUCT_GROUP"));
                ObjFeltSalesOrderDetails.setAttribute("LENGTH", resultSetTemp.getString("LENGTH"));
                ObjFeltSalesOrderDetails.setAttribute("WIDTH", resultSetTemp.getString("WIDTH"));
                ObjFeltSalesOrderDetails.setAttribute("GSM", resultSetTemp.getString("GSM"));
                ObjFeltSalesOrderDetails.setAttribute("THEORTICAL_WEIGHT", resultSetTemp.getString("THEORTICAL_WEIGHT"));
                ObjFeltSalesOrderDetails.setAttribute("SQ_MT", resultSetTemp.getString("SQ_MT"));
                ObjFeltSalesOrderDetails.setAttribute("STYLE", resultSetTemp.getString("STYLE"));
                ObjFeltSalesOrderDetails.setAttribute("REQ_MONTH", resultSetTemp.getString("REQ_MONTH"));
                ObjFeltSalesOrderDetails.setAttribute("SYN_PER", resultSetTemp.getString("SYN_PER"));
                ObjFeltSalesOrderDetails.setAttribute("REMARK", resultSetTemp.getString("REMARK"));
                ObjFeltSalesOrderDetails.setAttribute("BILL_LENGTH", resultSetTemp.getString("BILL_LENGTH"));
                ObjFeltSalesOrderDetails.setAttribute("BILL_WIDTH", resultSetTemp.getString("BILL_WIDTH"));
                ObjFeltSalesOrderDetails.setAttribute("BILL_WEIGHT", resultSetTemp.getString("BILL_WEIGHT"));
                ObjFeltSalesOrderDetails.setAttribute("BILL_SQMTR", resultSetTemp.getString("BILL_SQMTR"));
                ObjFeltSalesOrderDetails.setAttribute("BILL_GSM", resultSetTemp.getString("BILL_GSM"));
                ObjFeltSalesOrderDetails.setAttribute("BILL_PRODUCT_CODE", resultSetTemp.getString("BILL_PRODUCT_CODE"));
                ObjFeltSalesOrderDetails.setAttribute("PIECE_STAGE", resultSetTemp.getString("PIECE_STAGE"));

                objDToFDetails.put(Integer.toString(serialNoCounter), ObjFeltSalesOrderDetails);
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

            rsTmp = stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER_H WHERE PC_DOC_NO='" + productionDocumentNo + "'");
            while (rsTmp.next()) {
                clsSalesPieceClubbing felt_order = new clsSalesPieceClubbing();

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
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER_H WHERE PC_DOC_NO ='" + pDocNo + "'");
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
               // strSQL = "SELECT DISTINCT PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER.PC_DOC_NO,PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER.PC_DOC_DATE,PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER, PRODUCTION.FELT_PROD_DOC_DATA   WHERE PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER.PC_DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.PC_DOC_NO AND STATUS='W' AND MODULE_ID=" + ModuleID + " AND PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER.CANCELED=0  AND USER_ID=" + pUserID + " ORDER BY RECEIVED_DATE,PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER.PC_DOC_NO";
                strSQL = "SELECT DISTINCT H.PC_DOC_NO,H.PC_DOC_DATE,H.PARTY_CODE,PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE,group_concat(PIECE_NO) AS PIECE_NO \n" +
"\n" +
"FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER H,PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL D, PRODUCTION.FELT_PROD_DOC_DATA \n" +
"  \n" +
"WHERE H.PC_DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND STATUS='W' AND MODULE_ID="+ModuleID+" AND H.CANCELED=0  AND USER_ID="+pUserID+" AND H.PC_DOC_NO=D.PC_DOC_NO GROUP BY H.PC_DOC_NO ORDER BY H.PC_DOC_NO";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                //strSQL = "SELECT DISTINCT PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER.PC_DOC_NO,PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER.PC_DOC_DATE,PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER, PRODUCTION.FELT_PROD_DOC_DATA   WHERE PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER.PC_DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.PC_DOC_NO AND STATUS='W' AND MODULE_ID=" + ModuleID + " AND PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER.CANCELED=0  AND USER_ID=" + pUserID + " ORDER BY RECEIVED_DATE,PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER.PC_DOC_NO";
                strSQL = "SELECT DISTINCT H.PC_DOC_NO,H.PC_DOC_DATE,H.PARTY_CODE,PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE,group_concat(PIECE_NO) AS PIECE_NO \n" +
"\n" +
"FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER H,PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL D, PRODUCTION.FELT_PROD_DOC_DATA   \n" +
"\n" +
"WHERE H.PC_DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND STATUS='W' AND MODULE_ID="+ModuleID+" AND H.CANCELED=0  AND FELT_PROD_DOC_DATA.USER_ID="+pUserID+"  AND H.PC_DOC_NO=D.PC_DOC_NO GROUP BY H.PC_DOC_NO ORDER BY RECEIVED_DATE,H.PC_DOC_NO";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                //strSQL = "SELECT DISTINCT PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER.PC_DOC_NO,PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER.PC_DOC_DATE,PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER, PRODUCTION.FELT_PROD_DOC_DATA   WHERE PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER.PC_DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.PC_DOC_NO AND STATUS='W' AND MODULE_ID=" + ModuleID + " AND PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER.CANCELED=0  AND USER_ID=" + pUserID + "  ORDER BY PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER.PC_DOC_NO";
                strSQL = "SELECT DISTINCT H.PC_DOC_NO,H.PC_DOC_DATE,H.PARTY_CODE,PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE ,group_concat(PIECE_NO) AS PIECE_NO\n" +
"\n" +
"FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER H,PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL D, PRODUCTION.FELT_PROD_DOC_DATA  \n" +
"\n" +
" WHERE H.PC_DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND STATUS='W' AND MODULE_ID="+ModuleID+" AND H.CANCELED=0  AND FELT_PROD_DOC_DATA.USER_ID="+pUserID+"   AND H.PC_DOC_NO=D.PC_DOC_NO GROUP BY H.PC_DOC_NO   ORDER BY H.PC_DOC_NO";
            }
            System.out.println("Pen "+strSQL);
            ResultSet rsTmp = data.getConn().createStatement().executeQuery(strSQL);

            Counter = 0;
            while (rsTmp.next()) {

                Counter = Counter + 1;
                clsSalesPieceClubbing ObjDoc = new clsSalesPieceClubbing();

                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO", rsTmp.getString("PC_DOC_NO"));
                ObjDoc.setAttribute("DOC_DATE", rsTmp.getString("PC_DOC_DATE"));
                ObjDoc.setAttribute("RECEIVED_DATE", rsTmp.getString("PC_DOC_DATE"));
                ObjDoc.setAttribute("PIECE_NO", rsTmp.getString("PIECE_NO"));
                ObjDoc.setAttribute("PARTY_CODE", rsTmp.getString("PARTY_CODE"));

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
            System.out.println("SELECT PC_DOC_NO FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER WHERE PC_DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");
            rsTmp = stTmp.executeQuery("SELECT PC_DOC_NO FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER WHERE PC_DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");

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
                rsTmp = data.getResult("SELECT APPROVED FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER WHERE PC_DOC_NO='" + pAmendNo + "' ");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    Approved = rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();

                if (!Approved) {
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE PC_DOC_NO='" + pAmendNo + "' AND MODULE_ID='" + ModuleID + "'");
                }
                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE PC_DOC_NO='" + pAmendNo + "'");

            } catch (Exception e) {

            }
        }

    }
}
