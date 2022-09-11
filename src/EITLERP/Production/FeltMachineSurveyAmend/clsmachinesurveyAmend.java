    /*
 * clsmachinesurveyAmend.java
 *
 * Created on March 1,2015
 */
package EITLERP.Production.FeltMachineSurveyAmend;

import java.util.*;
import java.util.Date;
import java.text.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import EITLERP.Finance.*;
import java.text.DecimalFormat;
import java.lang.Double;
import EITLERP.Production.clsFeltProductionApprovalFlow;

/**
 *
 * @author Dharmendra
 */
public class clsmachinesurveyAmend {

    public static void CancelDoc(String pDocNo) {
        ResultSet rsTmp=null;
        
        if(CanCancel(pDocNo)) {
            
            boolean Approved=false;
            
            try {
                rsTmp=data.getResult("SELECT APPROVED FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER WHERE MM_AMEND_NO='"+pDocNo+"' ");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    Approved=rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();
                
                if(!Approved) {
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='"+pDocNo+"' AND MODULE_ID=725");
                }
                data.Execute("UPDATE PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE MM_AMEND_NO='"+pDocNo+"'");
                
            }
            catch(Exception e) {
                
            }
        }
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    

//    public static void CancelDoc(String pDocNo) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

    public String LastError = "";

    private ResultSet rsResultSet, rsResultSet1;
    private Connection Conn;
    private Statement Stmt;
    DecimalFormat df = new DecimalFormat("##.##");
    private HashMap props;
    public boolean Ready = false;

    private boolean HistoryView = false;
    public static int ModuleID = 725; //72
    public HashMap colMRItems;
    private String SelPrefix = ""; //Selected Prefix
    private String SelSuffix = ""; //Selected Prefix
    private int FFNo = 0;

    public Variant getAttribute(String PropName) {
        if (!props.containsKey(PropName)) {
            return new Variant("");
        } else {
            return (Variant) props.get(PropName);
        }
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
     * Creates a new instance of clsSales_Party
     */
    public clsmachinesurveyAmend() {
        LastError = "";
        props = new HashMap();
        props.put("MM_DOC_NO", new Variant(""));
        props.put("MM_PARTY_CODE", new Variant(""));
        props.put("MM_INCHRGE_NAME", new Variant(""));
        props.put("MM_MACHINE_NO", new Variant(""));
        props.put("MM_MACHINE_TYPE_FORAMING", new Variant(""));
        props.put("MM_PAPER_GRADE", new Variant(""));
        props.put("MM_MACHINE_SPEED_RANGE", new Variant(""));
        props.put("MM_PAPER_GSM_RANGE", new Variant(""));
        props.put("MM_MACHINE_TYPE_PRESSING", new Variant(""));
        props.put("MM_FURNISH", new Variant(""));
        props.put("MM_TYPE_OF_FILLER", new Variant(""));
        props.put("MM_WIRE_DETAIL_1", new Variant(""));
        props.put("MM_WIRE_DETAIL_2", new Variant(""));
        props.put("MM_WIRE_DETAIL_3", new Variant(""));
        props.put("MM_WIRE_DETAIL_4", new Variant(""));
        props.put("MM_PAPER_DECKLE_AFTER_WIRE", new Variant(""));
        props.put("MM_PAPER_DECKLE_AFTER_PRESS", new Variant(""));
        props.put("MM_PAPER_DECKLE_AT_POPE_REEL", new Variant(""));
        props.put("MM_DRYER_SECTION", new Variant(""));
        props.put("MM_ZONE", new Variant(""));
        props.put("MM_MACHINE_STATUS", new Variant(""));
        props.put("MM_ZONE_REPRESENTATIVE", new Variant(""));
        props.put("MM_DATE_OF_UPDATE", new Variant(""));
        props.put("MM_TOTAL_DRYER_GROUP", new Variant(""));
        props.put("MM_UNIRUM_GROUP", new Variant(""));
        props.put("MM_CONVENTIONAL_GROUP", new Variant(""));
        props.put("MM_HOOD_TYPE", new Variant(""));
        props.put("MM_SIZE_PRESS", new Variant(""));
        props.put("MM_SIZE_PRESS_POSITION", new Variant(""));
        props.put("MM_SHEET_DRYNESS_SIZE_PRESS", new Variant(""));
        props.put("MM_DRIVE_TYPE",new Variant(""));
        props.put("HIERARCHY_ID", new Variant(0));
        props.put("CREATED_BY", new Variant(""));
        props.put("APPROVED", new Variant(0));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED", new Variant(0));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("CANCELED", new Variant(0));
        // props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("MM_AMEND_NO", new Variant("0"));

        colMRItems = new HashMap();

        // -- Approval Specific Fields --
        props.put("FROM", new Variant(0));
        props.put("TO", new Variant(0));
        props.put("REJECTED_REMARKS", new Variant(""));
        props.put("APPROVAL_STATUS", new Variant(""));
        props.put("SEND_DOC_TO", new Variant(0));
        props.put("APPROVER_REMARKS", new Variant(0));
    }

    /**
     * Load Data. This method loads data from database to Business Object*
     */
    public boolean LoadData(long pCompanyID) {
        Ready = false;
        try {
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            //rsResultSet = Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER ORDER BY MM_DOC_NO");
            rsResultSet = Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER ORDER BY MM_AMEND_NO");
            //    rsResultSet=Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER A JOIN PRODUCTION.FELT_MACHINE_MASTER_AMEND_DETAIL B ON A.MM_PARTY_CODE=B.MM_PARTY_CODE ORDER BY A.MM_DOC_NO,B.SR_NO");
            // WHERE PROFORMA_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PROFORMA_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY PROFORMA_NO");
            HistoryView = false;
            Ready = true;
            MoveLast();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public void Close() {
        try {
            //Conn.close();
            Stmt.close();
            rsResultSet.close();
        } catch (Exception e) {

        }
    }

    //Navigation Methods
    public boolean MoveFirst() {
        try {
            rsResultSet.first();
            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean MoveNext() {
        try {
            if (rsResultSet.isAfterLast() || rsResultSet.isLast()) {
                //Move pointer at last record
                //If it is beyond eof
                rsResultSet.last();
            } else {
                rsResultSet.next();
                if (rsResultSet.isAfterLast()) {
                    rsResultSet.last();
                }
            }
            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean MovePrevious() {
        try {
            if (rsResultSet.isFirst() || rsResultSet.isBeforeFirst()) {
                rsResultSet.first();
            } else {
                rsResultSet.previous();
                if (rsResultSet.isBeforeFirst()) {
                    rsResultSet.first();
                }
            }
            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean MoveLast() {
        try {
            rsResultSet.last();
            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean Insert() {

        Statement stHistory, stHeader, stHDetail;
        ResultSet rsHistory, rsHeader, rsHDetail;

        try {

            //======= Check the requisition date ============//
            java.sql.Date FinFromDate = java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate = java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);

            frmmachinesurveyAmend fms = new frmmachinesurveyAmend();

            // ---- History Related Changes ------ //
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHDetail = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rsHistory = stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER_H WHERE MM_DOC_NO=''"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail = stHDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_DETAIL_H WHERE MM_DOC_NO=''");
            rsHDetail.first();

            //------------------------------------//
            SelectFirstFree aList = new SelectFirstFree();

            aList.ModuleID = 725;
            aList.FirstFreeNo = 214;
            if (aList.ShowList()) {
                SelPrefix = aList.Prefix; //Selected Prefix;
                SelSuffix = aList.Suffix;
                FFNo = aList.FirstFreeNo;
            }
            System.out.println();
            setAttribute("MM_DOC_NO", getAttribute("MM_DOC_NO").getString());
            setAttribute("MM_AMEND_NO", EITLERP.clsFirstFree.getNextFreeNo(2, 725, FFNo, true));
            rsResultSet.first();
            rsResultSet.moveToInsertRow();

            rsResultSet.updateString("MM_AMEND_NO", getAttribute("MM_AMEND_NO").getString());
            rsResultSet.updateString("MM_DOC_NO", getAttribute("MM_DOC_NO").getString());
            rsResultSet.updateString("MM_PARTY_CODE", getAttribute("MM_PARTY_CODE").getString());
            rsResultSet.updateString("MM_PARTY_NAME", getAttribute("MM_PARTY_NAME").getString());
            rsResultSet.updateString("MM_STATION", getAttribute("MM_STATION").getString());
            rsResultSet.updateString("MM_INCHARGE_NAME", getAttribute("MM_INCHARGE_NAME").getString());
            rsResultSet.updateString("MM_MACHINE_NO", getAttribute("MM_MACHINE_NO").getString());
            rsResultSet.updateString("MM_MACHINE_TYPE_FORMING", getAttribute("MM_MACHINE_TYPE_FORMING").getString());
            rsResultSet.updateString("MM_PAPER_GRADE", getAttribute("MM_PAPER_GRADE").getString());
            rsResultSet.updateString("MM_MACHINE_SPEED_RANGE", getAttribute("MM_MACHINE_SPEED_RANGE").getString());
            rsResultSet.updateString("MM_PAPER_GSM_RANGE", getAttribute("MM_PAPER_GSM_RANGE").getString());
            rsResultSet.updateString("MM_MACHINE_TYPE_PRESSING", getAttribute("MM_MACHINE_TYPE_PRESSING").getString());
            rsResultSet.updateString("MM_FURNISH", getAttribute("MM_FURNISH").getString());
            rsResultSet.updateString("MM_TYPE_OF_FILLER", getAttribute("MM_TYPE_OF_FILLER").getString());
            rsResultSet.updateString("MM_WIRE_DETAIL_1", getAttribute("MM_WIRE_DETAIL_1").getString());
            rsResultSet.updateString("MM_WIRE_DETAIL_2", getAttribute("MM_WIRE_DETAIL_2").getString());
            rsResultSet.updateString("MM_WIRE_DETAIL_3", getAttribute("MM_WIRE_DETAIL_3").getString());
            rsResultSet.updateString("MM_WIRE_DETAIL_4", getAttribute("MM_WIRE_DETAIL_4").getString());
            rsResultSet.updateString("MM_PAPER_DECKLE_AFTER_WIRE", getAttribute("MM_PAPER_DECKLE_AFTER_WIRE").getString());
            rsResultSet.updateString("MM_PAPER_DECKLE_AFTER_PRESS", getAttribute("MM_PAPER_DECKLE_AFTER_PRESS").getString());
            rsResultSet.updateString("MM_PAPER_DECKLE_AT_POPE_REEL", getAttribute("MM_PAPER_DECKLE_AT_POPE_REEL").getString());
            rsResultSet.updateString("MM_DRYER_SECTION", getAttribute("MM_DRYER_SECTION").getString());
            rsResultSet.updateString("MM_ZONE", getAttribute("MM_ZONE").getString());
            rsResultSet.updateString("MM_CAPACITY", getAttribute("MM_CAPACITY").getString());
            rsResultSet.updateString("MM_MACHINE_STATUS", getAttribute("MM_MACHINE_STATUS").getString());
            rsResultSet.updateString("MM_ZONE_REPRESENTATIVE", getAttribute("MM_ZONE_REPRESENTATIVE").getString());
            if (EITLERPGLOBAL.formatDateDB(getAttribute("MM_DATE_OF_UPDATE").getString()).equals("")) {
                setAttribute("MM_DATE_OF_UPDATE", getAttribute("MM_DATE_OF_UPDATE").getString());
            } else {
                setAttribute("MM_DATE_OF_UPDATE", EITLERPGLOBAL.formatDateDB(getAttribute("MM_DATE_OF_UPDATE").getString()));
            }
            rsResultSet.updateString("MM_DATE_OF_UPDATE", getAttribute("MM_DATE_OF_UPDATE").getString());
            rsResultSet.updateString("MM_TOTAL_DRYER_GROUP", getAttribute("MM_TOTAL_DRYER_GROUP").getString());
            rsResultSet.updateString("MM_UNIRUM_GROUP", getAttribute("MM_UNIRUM_GROUP").getString());
            rsResultSet.updateString("MM_CONVENTIONAL_GROUP", getAttribute("MM_CONVENTIONAL_GROUP").getString());
            rsResultSet.updateString("MM_HOOD_TYPE", getAttribute("MM_HOOD_TYPE").getString());
            rsResultSet.updateString("MM_SIZE_PRESS", getAttribute("MM_SIZE_PRESS").getString());
            rsResultSet.updateString("MM_SIZE_PRESS_POSITION", getAttribute("MM_SIZE_PRESS_POSITION").getString());
            rsResultSet.updateString("MM_SHEET_DRYNESS_SIZE_PRESS", getAttribute("MM_SHEET_DRYNESS_SIZE_PRESS").getString());
            rsResultSet.updateString("MM_DRIVE_TYPE",getAttribute("MM_DRIVE_TYPE").getString());
            
            rsResultSet.updateString("MM_MACHINE_MAKE",getAttribute("MM_MACHINE_MAKE").getString());
            
            rsResultSet.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsResultSet.updateBoolean("APPROVED", false);
            rsResultSet.updateString("APPROVED_DATE", "0000-00-00");
            rsResultSet.updateBoolean("CANCELED", false);
            rsResultSet.updateBoolean("REJECTED", false);
            rsResultSet.updateString("REJECTED_DATE", "0000-00-00");
            rsResultSet.updateBoolean("CHANGED", true);
            rsResultSet.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

            rsResultSet.insertRow();

            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO", 1); //Revision No. 1 in case of Insert
            rsHistory.updateString("MM_AMEND_NO", getAttribute("MM_AMEND_NO").getString());
            rsHistory.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
            //    rsHistory.updateString("UPDATE_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateInt("UPDATE_BY", (int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
            // rsHistory.updateString("MM_DOC_NO",fms.mdocno);
            rsHistory.updateString("MM_DOC_NO", getAttribute("MM_DOC_NO").getString());
            rsHistory.updateString("MM_PARTY_CODE", getAttribute("MM_PARTY_CODE").getString());
            rsHistory.updateString("MM_PARTY_NAME", getAttribute("MM_PARTY_NAME").getString());
            rsHistory.updateString("MM_STATION", getAttribute("MM_STATION").getString());
            rsHistory.updateString("MM_INCHARGE_NAME", getAttribute("MM_INCHARGE_NAME").getString());
            rsHistory.updateString("MM_MACHINE_NO", getAttribute("MM_MACHINE_NO").getString());
            rsHistory.updateString("MM_MACHINE_TYPE_FORMING", getAttribute("MM_MACHINE_TYPE_FORMING").getString());
            rsHistory.updateString("MM_PAPER_GRADE", getAttribute("MM_PAPER_GRADE").getString());
            rsHistory.updateString("MM_MACHINE_SPEED_RANGE", getAttribute("MM_MACHINE_SPEED_RANGE").getString());
            rsHistory.updateString("MM_PAPER_GSM_RANGE", getAttribute("MM_PAPER_GSM_RANGE").getString());
            rsHistory.updateString("MM_MACHINE_TYPE_PRESSING", getAttribute("MM_MACHINE_TYPE_PRESSING").getString());
            rsHistory.updateString("MM_FURNISH", getAttribute("MM_FURNISH").getString());
            rsHistory.updateString("MM_TYPE_OF_FILLER", getAttribute("MM_TYPE_OF_FILLER").getString());
            rsHistory.updateString("MM_WIRE_DETAIL_1", getAttribute("MM_WIRE_DETAIL_1").getString());
            rsHistory.updateString("MM_WIRE_DETAIL_2", getAttribute("MM_WIRE_DETAIL_2").getString());
            rsHistory.updateString("MM_WIRE_DETAIL_3", getAttribute("MM_WIRE_DETAIL_3").getString());
            rsHistory.updateString("MM_WIRE_DETAIL_4", getAttribute("MM_WIRE_DETAIL_4").getString());
            rsHistory.updateString("MM_PAPER_DECKLE_AFTER_WIRE", getAttribute("MM_PAPER_DECKLE_AFTER_WIRE").getString());
            rsHistory.updateString("MM_PAPER_DECKLE_AFTER_PRESS", getAttribute("MM_PAPER_DECKLE_AFTER_PRESS").getString());
            rsHistory.updateString("MM_PAPER_DECKLE_AT_POPE_REEL", getAttribute("MM_PAPER_DECKLE_AT_POPE_REEL").getString());

            rsHistory.updateString("MM_DRYER_SECTION", getAttribute("MM_DRYER_SECTION").getString());
            rsHistory.updateString("MM_ZONE", getAttribute("MM_ZONE").getString());
            rsHistory.updateString("MM_CAPACITY", getAttribute("MM_CAPACITY").getString());
            rsHistory.updateString("MM_MACHINE_STATUS", getAttribute("MM_MACHINE_STATUS").getString());
            rsHistory.updateString("MM_ZONE_REPRESENTATIVE", getAttribute("MM_ZONE_REPRESENTATIVE").getString());
            if (EITLERPGLOBAL.formatDateDB(getAttribute("MM_DATE_OF_UPDATE").getString()).equals("")) {
                setAttribute("MM_DATE_OF_UPDATE", "0000-00-00");
            } else {
                setAttribute("MM_DATE_OF_UPDATE", EITLERPGLOBAL.formatDateDB(getAttribute("MM_DATE_OF_UPDATE").getString()));
            }
            rsHistory.updateString("MM_DATE_OF_UPDATE", getAttribute("MM_DATE_OF_UPDATE").getString());

            //rsHistory.updateString("MM_DATE_OF_UPDATE",getAttribute("MM_DATE_OF_UPDATE").getString());
            rsHistory.updateString("MM_TOTAL_DRYER_GROUP", getAttribute("MM_TOTAL_DRYER_GROUP").getString());
            rsHistory.updateString("MM_UNIRUM_GROUP", getAttribute("MM_UNIRUM_GROUP").getString());
            rsHistory.updateString("MM_CONVENTIONAL_GROUP", getAttribute("MM_CONVENTIONAL_GROUP").getString());
            rsHistory.updateString("MM_HOOD_TYPE", getAttribute("MM_HOOD_TYPE").getString());
            rsHistory.updateString("MM_SIZE_PRESS", getAttribute("MM_SIZE_PRESS").getString());
            rsHistory.updateString("MM_SIZE_PRESS_POSITION", getAttribute("MM_SIZE_PRESS_POSITION").getString());
            rsHistory.updateString("MM_SHEET_DRYNESS_SIZE_PRESS", getAttribute("MM_SHEET_DRYNESS_SIZE_PRESS").getString());
            rsHistory.updateString("MM_DRIVE_TYPE",getAttribute("MM_DRIVE_TYPE").getString());
            rsHistory.updateString("MM_MACHINE_MAKE",getAttribute("MM_MACHINE_MAKE").getString());
            rsHistory.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            // rsHistory.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsHistory.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());

            //rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            //rsHistory.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsHistory.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            rsHistory.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsHistory.updateBoolean("APPROVED", false);
            rsHistory.updateString("APPROVED_DATE", "0000-00-00");
            rsHistory.updateBoolean("REJECTED", false);
            rsHistory.updateString("REJECTED_DATE", "0000-00-00");
            rsHistory.updateBoolean("CANCELED", false);
            rsHistory.updateBoolean("CHANGED", true);
            rsHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();

            ResultSet rsTmp;
            Statement tmpStmt;

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_DETAIL WHERE MM_DOC_NO='' order by sr_no*1");

            //   String tmpmachinests="";
            // String msurvydt="";
            //   String mdttm="";
            //Now Insert records into detail table
            for (int i = 1; i <= colMRItems.size(); i++) {

                clsmachinesurveyitemAmend ObjMRItems = (clsmachinesurveyitemAmend) colMRItems.get(Integer.toString(i));

                rsTmp.moveToInsertRow();

                rsTmp.updateString("MM_DOC_NO", getAttribute("MM_DOC_NO").getString());
                System.out.println((String) ObjMRItems.getAttribute("MM_MACHINE_POSITION").getString());
                //   rsTmp.updateString("MM_PARTY_CODE",(String)ObjMRItems.getAttribute("MM_MACHINE_POSITION").getString());
                rsTmp.updateString("MM_AMEND_NO", (String) getAttribute("MM_AMEND_NO").getString());
                rsTmp.updateString("SR_NO", (String) ObjMRItems.getAttribute("SR_NO").getString());
                rsTmp.updateString("MM_PARTY_CODE", getAttribute("MM_PARTY_CODE").getString());
                rsTmp.updateString("MM_MACHINE_NO", getAttribute("MM_MACHINE_NO").getString());
                rsTmp.updateString("MM_MACHINE_POSITION", (String) ObjMRItems.getAttribute("MM_MACHINE_POSITION").getString());
                rsTmp.updateString("MM_MACHINE_POSITION_DESC", (String) ObjMRItems.getAttribute("MM_MACHINE_POSITION_DESC").getString());
                rsTmp.updateString("MM_COMBINATION_CODE", (String) ObjMRItems.getAttribute("MM_COMBINATION_CODE").getString());
                rsTmp.updateString("MM_PRESS_TYPE", (String) ObjMRItems.getAttribute("MM_PRESS_TYPE").getString());
                rsTmp.updateString("MM_PRESS_ROLL_DAI_MM", (String) ObjMRItems.getAttribute("MM_PRESS_ROLL_DAI_MM").getString());
                rsTmp.updateString("MM_PRESS_ROLL_FACE_TOTAL_MM", (String) ObjMRItems.getAttribute("MM_PRESS_ROLL_FACE_TOTAL_MM").getString());
                rsTmp.updateString("MM_PRESS_ROLL_FACE_NET_MM", (String) ObjMRItems.getAttribute("MM_PRESS_ROLL_FACE_NET_MM").getString());
                rsTmp.updateString("MM_FELT_ROLL_WIDTH_MM", (String) ObjMRItems.getAttribute("MM_FELT_ROLL_WIDTH_MM").getString());
                rsTmp.updateString("MM_PRESS_LOAD", (String) ObjMRItems.getAttribute("MM_PRESS_LOAD").getString());
                rsTmp.updateString("MM_VACCUM_CAPACITY", (String) ObjMRItems.getAttribute("MM_VACCUM_CAPACITY").getString());
                rsTmp.updateString("MM_UHLE_BOX", (String) ObjMRItems.getAttribute("MM_UHLE_BOX").getString());
                rsTmp.updateString("MM_HP_SHOWER", (String) ObjMRItems.getAttribute("MM_HP_SHOWER").getString());
                rsTmp.updateString("MM_LP_SHOWER", (String) ObjMRItems.getAttribute("MM_LP_SHOWER").getString());
                rsTmp.updateString("MM_FELT_LENGTH", (String) ObjMRItems.getAttribute("MM_FELT_LENGTH").getString());
                rsTmp.updateString("MM_FELT_WIDTH", (String) ObjMRItems.getAttribute("MM_FELT_WIDTH").getString());
                rsTmp.updateString("MM_FELT_GSM", (String) ObjMRItems.getAttribute("MM_FELT_GSM").getString());
                rsTmp.updateString("MM_FELT_WEIGHT", (String) ObjMRItems.getAttribute("MM_FELT_WEIGHT").getString());
                rsTmp.updateString("MM_FELT_TYPE", (String) ObjMRItems.getAttribute("MM_FELT_TYPE").getString());
                rsTmp.updateString("MM_FELT_STYLE", (String) ObjMRItems.getAttribute("MM_FELT_STYLE").getString());
                rsTmp.updateString("MM_AVG_LIFE", (String) ObjMRItems.getAttribute("MM_AVG_LIFE").getString());
                rsTmp.updateString("MM_AVG_PRODUCTION", (String) ObjMRItems.getAttribute("MM_AVG_PRODUCTION").getString());
                rsTmp.updateString("MM_FELT_CONSUMPTION", (String) ObjMRItems.getAttribute("MM_FELT_CONSUMPTION").getString());
                rsTmp.updateString("MM_DINESH_SHARE", (String) ObjMRItems.getAttribute("MM_DINESH_SHARE").getString());
                rsTmp.updateString("MM_REMARK_DESIGN", (String) ObjMRItems.getAttribute("MM_REMARK_DESIGN").getString());
                rsTmp.updateString("MM_REMARK_GENERAL", (String) ObjMRItems.getAttribute("MM_REMARK_GENERAL").getString());
                rsTmp.updateString("MM_NO_DRYER_CYLINDER", (String) ObjMRItems.getAttribute("MM_NO_DRYER_CYLINDER").getString());
                rsTmp.updateString("MM_CYLINDER_DIA_MM", (String) ObjMRItems.getAttribute("MM_CYLINDER_DIA_MM").getString());
                rsTmp.updateString("MM_CYLINDER_FACE_NET_MM", (String) ObjMRItems.getAttribute("MM_CYLINDER_FACE_NET_MM").getString());
                rsTmp.updateString("MM_FELT_LIFE", (String) ObjMRItems.getAttribute("MM_FELT_LIFE").getString());
                rsTmp.updateString("MM_TPD", (String) ObjMRItems.getAttribute("MM_TPD").getString());
                rsTmp.updateString("MM_TOTAL_PRODUCTION", (String) ObjMRItems.getAttribute("MM_TOTAL_PRODUCTION").getString());
                rsTmp.updateString("MM_PAPER_FELT", (String) ObjMRItems.getAttribute("MM_PAPER_FELT").getString());
                rsTmp.updateString("MM_DRIVE_TYPE", (String) ObjMRItems.getAttribute("MM_DRIVE_TYPE").getString());
                rsTmp.updateString("MM_GUIDE_TYPE", (String) ObjMRItems.getAttribute("MM_GUIDE_TYPE").getString());
                rsTmp.updateString("MM_GUIDE_PAM_TYPE", (String) ObjMRItems.getAttribute("MM_GUIDE_PAM_TYPE").getString());
                rsTmp.updateString("MM_VENTILATION_TYPE", (String) ObjMRItems.getAttribute("MM_VENTILATION_TYPE").getString());
                rsTmp.updateString("MM_FABRIC_LENGTH", (String) ObjMRItems.getAttribute("MM_FABRIC_LENGTH").getString());
                rsTmp.updateString("MM_FABRIC_WIDTH", (String) ObjMRItems.getAttribute("MM_FABRIC_WIDTH").getString());
                rsTmp.updateString("MM_SIZE_M2", (String) ObjMRItems.getAttribute("MM_SIZE_M2").getString());
                rsTmp.updateString("MM_SCREEN_TYPE", (String) ObjMRItems.getAttribute("MM_SCREEN_TYPE").getString());
                rsTmp.updateString("MM_STYLE_DRY", (String) ObjMRItems.getAttribute("MM_STYLE_DRY").getString());
                rsTmp.updateString("MM_CFM_DRY", (String) ObjMRItems.getAttribute("MM_CFM_DRY").getString());
                rsTmp.updateString("MM_AVG_LIFE_DRY", (String) ObjMRItems.getAttribute("MM_AVG_LIFE_DRY").getString());
                rsTmp.updateString("MM_CONSUMPTION_DRY", (String) ObjMRItems.getAttribute("MM_CONSUMPTION_DRY").getString());
                rsTmp.updateString("MM_DINESH_SHARE_DRY", (String) ObjMRItems.getAttribute("MM_DINESH_SHARE_DRY").getString());
                rsTmp.updateString("MM_REMARK_DRY", (String) ObjMRItems.getAttribute("MM_REMARK_DRY").getString());
                rsTmp.updateString("MM_ITEM_CODE", (String) ObjMRItems.getAttribute("MM_ITEM_CODE").getString());
                rsTmp.updateString("MM_GRUP", (String) ObjMRItems.getAttribute("MM_GRUP").getString());
                rsTmp.updateString("MM_BASE_GSM", (String) ObjMRItems.getAttribute("MM_BASE_GSM").getString());
                rsTmp.updateString("MM_WEB_GSM", (String) ObjMRItems.getAttribute("MM_WEB_GSM").getString());
                rsTmp.updateString("MM_TOTAL_GSM", (String) ObjMRItems.getAttribute("MM_TOTAL_GSM").getString());
                rsTmp.updateString("MM_POSITION_WISE", (String) ObjMRItems.getAttribute("MM_POSITION_WISE").getString());
                rsTmp.updateString("MM_HARDNESS",(String)ObjMRItems.getAttribute("MM_HARDNESS").getString());
                rsTmp.updateString("MM_FELT_WASHING_CHEMICALS",(String)ObjMRItems.getAttribute("MM_FELT_WASHING_CHEMICALS").getString());
                rsTmp.updateString("MM_VACCUM_IN_UHLE_BOX",(String)ObjMRItems.getAttribute("MM_VACCUM_IN_UHLE_BOX").getString());
                
                rsTmp.updateString("MM_MACHINE_FLOOR",(String)ObjMRItems.getAttribute("MM_MACHINE_FLOOR").getString());
                rsTmp.updateString("MM_NUMBER_OF_FORMING_FABRIC",(String)ObjMRItems.getAttribute("MM_NUMBER_OF_FORMING_FABRIC").getString());
                rsTmp.updateString("MM_TYPE_OF_FORMING_FABRIC",(String)ObjMRItems.getAttribute("MM_TYPE_OF_FORMING_FABRIC").getString());
                rsTmp.updateString("MM_PRESS_ROLL_FOR_1ST_NIP_FACE_LENGTH",(String)ObjMRItems.getAttribute("MM_PRESS_ROLL_FOR_1ST_NIP_FACE_LENGTH").getString());
                rsTmp.updateString("MM_PRESS_ROLL_FOR_2ND_NIP_FACE_LENGTH",(String)ObjMRItems.getAttribute("MM_PRESS_ROLL_FOR_2ND_NIP_FACE_LENGTH").getString());
                rsTmp.updateString("MM_PRESS_ROLL_FOR_3RD_NIP_FACE_LENGTH",(String)ObjMRItems.getAttribute("MM_PRESS_ROLL_FOR_3RD_NIP_FACE_LENGTH").getString());
                rsTmp.updateString("MM_PRESS_ROLL_FOR_4TH_NIP_FACE_LENGTH",(String)ObjMRItems.getAttribute("MM_PRESS_ROLL_FOR_4TH_NIP_FACE_LENGTH").getString());
                rsTmp.updateString("MM_WASH_ROLL_SHOWER",(String)ObjMRItems.getAttribute("MM_WASH_ROLL_SHOWER").getString());
                rsTmp.updateString("MM_HP_SHOWER_NOZZLES",(String)ObjMRItems.getAttribute("MM_HP_SHOWER_NOZZLES").getString());
                rsTmp.updateString("MM_UHLE_BOX_VACUUM",(String)ObjMRItems.getAttribute("MM_UHLE_BOX_VACUUM").getString());
                rsTmp.updateString("MM_CHEMICAL_SHOWER",(String)ObjMRItems.getAttribute("MM_CHEMICAL_SHOWER").getString());
                rsTmp.updateString("MM_1ST_LINEAR_NIP_PRESSURE",(String)ObjMRItems.getAttribute("MM_1ST_LINEAR_NIP_PRESSURE").getString());
                rsTmp.updateString("MM_2ND_LINEAR_NIP_PRESSURE",(String)ObjMRItems.getAttribute("MM_2ND_LINEAR_NIP_PRESSURE").getString());
                rsTmp.updateString("MM_3RD_LINEAR_NIP_PRESSURE",(String)ObjMRItems.getAttribute("MM_3RD_LINEAR_NIP_PRESSURE").getString());
                rsTmp.updateString("MM_4TH_LINEAR_NIP_PRESSURE",(String)ObjMRItems.getAttribute("MM_4TH_LINEAR_NIP_PRESSURE").getString());
                rsTmp.updateString("MM_LOADING_SYSTEM",(String)ObjMRItems.getAttribute("MM_LOADING_SYSTEM").getString());
                rsTmp.updateString("MM_LP_SHOWER_NOZZLES",(String)ObjMRItems.getAttribute("MM_LP_SHOWER_NOZZLES").getString());
                rsTmp.updateString("MM_1ST_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_1ST_ROLL_MATERIAL").getString());
                rsTmp.updateString("MM_2ND_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_2ND_ROLL_MATERIAL").getString());
                rsTmp.updateString("MM_3RD_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_3RD_ROLL_MATERIAL").getString());
                rsTmp.updateString("MM_4TH_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_4TH_ROLL_MATERIAL").getString());
                rsTmp.updateString("MM_5TH_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_5TH_ROLL_MATERIAL").getString());
                rsTmp.updateString("MM_6TH_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_6TH_ROLL_MATERIAL").getString());
                rsTmp.updateString("MM_7TH_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_7TH_ROLL_MATERIAL").getString());
                rsTmp.updateString("MM_8TH_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_8TH_ROLL_MATERIAL").getString());
                rsTmp.updateString("MM_BATT_GSM",(String)ObjMRItems.getAttribute("MM_BATT_GSM").getString());
                rsTmp.updateString("MM_FIBERS_USED",(String)ObjMRItems.getAttribute("MM_FIBERS_USED").getString());
                rsTmp.updateString("MM_STRETCH",(String)ObjMRItems.getAttribute("MM_STRETCH").getString());
                rsTmp.updateString("MM_MG",(String)ObjMRItems.getAttribute("MM_MG").getString());
                rsTmp.updateString("MM_YANKEE",(String)ObjMRItems.getAttribute("MM_YANKEE").getString());
                rsTmp.updateString("MM_MG_YANKEE_NIP_LOAD",(String)ObjMRItems.getAttribute("MM_MG_YANKEE_NIP_LOAD").getString());
                
                rsTmp.updateString("MM_CATEGORY",(String)ObjMRItems.getAttribute("MM_CATEGORY").getString());
                rsTmp.updateString("UC_CODE",(String)ObjMRItems.getAttribute("UC_CODE").getString());
               
                rsTmp.updateString("MM_POSITION_DESIGN_NO",(String)ObjMRItems.getAttribute("MM_POSITION_DESIGN_NO").getString());
                rsTmp.updateString("MM_UPN_NO",(String)ObjMRItems.getAttribute("MM_UPN_NO").getString());
               
                
                //MM_MAX_CIRCUIT_LENGTH
                rsTmp.updateString("MM_MIN_CIRCUIT_LENGTH",(String)ObjMRItems.getAttribute("MM_MIN_CIRCUIT_LENGTH").getString());
                rsTmp.updateString("MM_MAX_CIRCUIT_LENGTH",(String)ObjMRItems.getAttribute("MM_MAX_CIRCUIT_LENGTH").getString());
                
                rsTmp.updateString("GOAL",(String)ObjMRItems.getAttribute("GOAL").getString());
                
                rsTmp.updateString("CREATED_BY", Integer.toString(EITLERPGLOBAL.gNewUserID));
                //rsTmp.updateString("MODIFIED_BY",Integer.toString(EITLERPGLOBAL.gNewUserID));
                rsTmp.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                //rsTmp.updateString("MODIFIED_DATE",(String)ObjMRItems.getAttribute("MODIFIED_DATE").getString());
                rsTmp.insertRow();

                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO", 1);
                rsHDetail.updateString("MM_AMEND_NO", (String) getAttribute("MM_AMEND_NO").getString());
                rsHDetail.updateString("MM_DOC_NO", getAttribute("MM_DOC_NO").getString());
                rsHDetail.updateString("SR_NO", (String) ObjMRItems.getAttribute("SR_NO").getString());
                rsHDetail.updateString("MM_MACHINE_POSITION", (String) ObjMRItems.getAttribute("MM_MACHINE_POSITION").getString());
                rsHDetail.updateString("MM_MACHINE_POSITION_DESC", (String) ObjMRItems.getAttribute("MM_MACHINE_POSITION_DESC").getString());
                rsHDetail.updateString("MM_COMBINATION_CODE", (String) ObjMRItems.getAttribute("MM_COMBINATION_CODE").getString());
                rsHDetail.updateString("MM_PRESS_TYPE", (String) ObjMRItems.getAttribute("MM_PRESS_TYPE").getString());
                rsHDetail.updateString("MM_PRESS_ROLL_DAI_MM", (String) ObjMRItems.getAttribute("MM_PRESS_ROLL_DAI_MM").getString());
                rsHDetail.updateString("MM_PRESS_ROLL_FACE_TOTAL_MM", (String) ObjMRItems.getAttribute("MM_PRESS_ROLL_FACE_TOTAL_MM").getString());
                rsHDetail.updateString("MM_PRESS_ROLL_FACE_NET_MM", (String) ObjMRItems.getAttribute("MM_PRESS_ROLL_FACE_NET_MM").getString());
                rsHDetail.updateString("MM_FELT_ROLL_WIDTH_MM", (String) ObjMRItems.getAttribute("MM_FELT_ROLL_WIDTH_MM").getString());
                rsHDetail.updateString("MM_PRESS_LOAD", (String) ObjMRItems.getAttribute("MM_PRESS_LOAD").getString());
                rsHDetail.updateString("MM_VACCUM_CAPACITY", (String) ObjMRItems.getAttribute("MM_VACCUM_CAPACITY").getString());
                rsHDetail.updateString("MM_UHLE_BOX", (String) ObjMRItems.getAttribute("MM_UHLE_BOX").getString());
                rsHDetail.updateString("MM_HP_SHOWER", (String) ObjMRItems.getAttribute("MM_HP_SHOWER").getString());
                rsHDetail.updateString("MM_LP_SHOWER", (String) ObjMRItems.getAttribute("MM_LP_SHOWER").getString());
                rsHDetail.updateString("MM_FELT_LENGTH", (String) ObjMRItems.getAttribute("MM_FELT_LENGTH").getString());
                rsHDetail.updateString("MM_FELT_WIDTH", (String) ObjMRItems.getAttribute("MM_FELT_WIDTH").getString());
                rsHDetail.updateString("MM_FELT_GSM", (String) ObjMRItems.getAttribute("MM_FELT_GSM").getString());
                rsHDetail.updateString("MM_FELT_WEIGHT", (String) ObjMRItems.getAttribute("MM_FELT_WEIGHT").getString());
                rsHDetail.updateString("MM_FELT_TYPE", (String) ObjMRItems.getAttribute("MM_FELT_TYPE").getString());
                rsHDetail.updateString("MM_FELT_STYLE", (String) ObjMRItems.getAttribute("MM_FELT_STYLE").getString());
                rsHDetail.updateString("MM_AVG_LIFE", (String) ObjMRItems.getAttribute("MM_AVG_LIFE").getString());
                rsHDetail.updateString("MM_AVG_PRODUCTION", (String) ObjMRItems.getAttribute("MM_AVG_PRODUCTION").getString());
                rsHDetail.updateString("MM_FELT_CONSUMPTION", (String) ObjMRItems.getAttribute("MM_FELT_CONSUMPTION").getString());
                rsHDetail.updateString("MM_DINESH_SHARE", (String) ObjMRItems.getAttribute("MM_DINESH_SHARE").getString());
                rsHDetail.updateString("MM_REMARK_DESIGN", (String) ObjMRItems.getAttribute("MM_REMARK_DESIGN").getString());
                rsHDetail.updateString("MM_REMARK_GENERAL", (String) ObjMRItems.getAttribute("MM_REMARK_GENERAL").getString());
                rsHDetail.updateString("MM_NO_DRYER_CYLINDER", (String) ObjMRItems.getAttribute("MM_NO_DRYER_CYLINDER").getString());
                rsHDetail.updateString("MM_CYLINDER_DIA_MM", (String) ObjMRItems.getAttribute("MM_CYLINDER_DIA_MM").getString());
                rsHDetail.updateString("MM_CYLINDER_FACE_NET_MM", (String) ObjMRItems.getAttribute("MM_CYLINDER_FACE_NET_MM").getString());
                rsHDetail.updateString("MM_DRIVE_TYPE", (String) ObjMRItems.getAttribute("MM_DRIVE_TYPE").getString());

                rsHDetail.updateString("MM_FELT_LIFE", (String) ObjMRItems.getAttribute("MM_FELT_LIFE").getString());
                rsHDetail.updateString("MM_TPD", (String) ObjMRItems.getAttribute("MM_TPD").getString());
                rsHDetail.updateString("MM_TOTAL_PRODUCTION", (String) ObjMRItems.getAttribute("MM_TOTAL_PRODUCTION").getString());
                rsHDetail.updateString("MM_PAPER_FELT", (String) ObjMRItems.getAttribute("MM_PAPER_FELT").getString());

                rsHDetail.updateString("MM_GUIDE_TYPE", (String) ObjMRItems.getAttribute("MM_GUIDE_TYPE").getString());
                rsHDetail.updateString("MM_GUIDE_PAM_TYPE", (String) ObjMRItems.getAttribute("MM_GUIDE_PAM_TYPE").getString());
                rsHDetail.updateString("MM_VENTILATION_TYPE", (String) ObjMRItems.getAttribute("MM_VENTILATION_TYPE").getString());
                rsHDetail.updateString("MM_FABRIC_LENGTH", (String) ObjMRItems.getAttribute("MM_FABRIC_LENGTH").getString());
                rsHDetail.updateString("MM_FABRIC_WIDTH", (String) ObjMRItems.getAttribute("MM_FABRIC_WIDTH").getString());
                rsHDetail.updateString("MM_SIZE_M2", (String) ObjMRItems.getAttribute("MM_SIZE_M2").getString());
                rsHDetail.updateString("MM_SCREEN_TYPE", (String) ObjMRItems.getAttribute("MM_SCREEN_TYPE").getString());
                rsHDetail.updateString("MM_STYLE_DRY", (String) ObjMRItems.getAttribute("MM_STYLE_DRY").getString());
                rsHDetail.updateString("MM_CFM_DRY", (String) ObjMRItems.getAttribute("MM_CFM_DRY").getString());
                rsHDetail.updateString("MM_AVG_LIFE_DRY", (String) ObjMRItems.getAttribute("MM_AVG_LIFE_DRY").getString());
                rsHDetail.updateString("MM_CONSUMPTION_DRY", (String) ObjMRItems.getAttribute("MM_CONSUMPTION_DRY").getString());
                rsHDetail.updateString("MM_DINESH_SHARE_DRY", (String) ObjMRItems.getAttribute("MM_DINESH_SHARE_DRY").getString());
                rsHDetail.updateString("MM_REMARK_DRY", (String) ObjMRItems.getAttribute("MM_REMARK_DRY").getString());
                rsHDetail.updateString("MM_ITEM_CODE", (String) ObjMRItems.getAttribute("MM_ITEM_CODE").getString());
                rsHDetail.updateString("MM_GRUP", (String) ObjMRItems.getAttribute("MM_GRUP").getString());
                rsHDetail.updateString("MM_BASE_GSM", (String) ObjMRItems.getAttribute("MM_BASE_GSM").getString());
                rsHDetail.updateString("MM_WEB_GSM", (String) ObjMRItems.getAttribute("MM_WEB_GSM").getString());
                rsHDetail.updateString("MM_TOTAL_GSM", (String) ObjMRItems.getAttribute("MM_TOTAL_GSM").getString());
                rsHDetail.updateString("MM_POSITION_WISE", (String) ObjMRItems.getAttribute("MM_POSITION_WISE").getString());
                rsHDetail.updateString("MM_HARDNESS",(String)ObjMRItems.getAttribute("MM_HARDNESS").getString());
                rsHDetail.updateString("MM_FELT_WASHING_CHEMICALS",(String)ObjMRItems.getAttribute("MM_FELT_WASHING_CHEMICALS").getString());
                rsHDetail.updateString("MM_VACCUM_IN_UHLE_BOX",(String)ObjMRItems.getAttribute("MM_VACCUM_IN_UHLE_BOX").getString());
                
                rsHDetail.updateString("MM_MACHINE_FLOOR",(String)ObjMRItems.getAttribute("MM_MACHINE_FLOOR").getString());
                rsHDetail.updateString("MM_NUMBER_OF_FORMING_FABRIC",(String)ObjMRItems.getAttribute("MM_NUMBER_OF_FORMING_FABRIC").getString());
                rsHDetail.updateString("MM_TYPE_OF_FORMING_FABRIC",(String)ObjMRItems.getAttribute("MM_TYPE_OF_FORMING_FABRIC").getString());
                rsHDetail.updateString("MM_PRESS_ROLL_FOR_1ST_NIP_FACE_LENGTH",(String)ObjMRItems.getAttribute("MM_PRESS_ROLL_FOR_1ST_NIP_FACE_LENGTH").getString());
                rsHDetail.updateString("MM_PRESS_ROLL_FOR_2ND_NIP_FACE_LENGTH",(String)ObjMRItems.getAttribute("MM_PRESS_ROLL_FOR_2ND_NIP_FACE_LENGTH").getString());
                rsHDetail.updateString("MM_PRESS_ROLL_FOR_3RD_NIP_FACE_LENGTH",(String)ObjMRItems.getAttribute("MM_PRESS_ROLL_FOR_3RD_NIP_FACE_LENGTH").getString());
                rsHDetail.updateString("MM_PRESS_ROLL_FOR_4TH_NIP_FACE_LENGTH",(String)ObjMRItems.getAttribute("MM_PRESS_ROLL_FOR_4TH_NIP_FACE_LENGTH").getString());
                rsHDetail.updateString("MM_WASH_ROLL_SHOWER",(String)ObjMRItems.getAttribute("MM_WASH_ROLL_SHOWER").getString());
                rsHDetail.updateString("MM_HP_SHOWER_NOZZLES",(String)ObjMRItems.getAttribute("MM_HP_SHOWER_NOZZLES").getString());
                rsHDetail.updateString("MM_UHLE_BOX_VACUUM",(String)ObjMRItems.getAttribute("MM_UHLE_BOX_VACUUM").getString());
                rsHDetail.updateString("MM_CHEMICAL_SHOWER",(String)ObjMRItems.getAttribute("MM_CHEMICAL_SHOWER").getString());
                rsHDetail.updateString("MM_1ST_LINEAR_NIP_PRESSURE",(String)ObjMRItems.getAttribute("MM_1ST_LINEAR_NIP_PRESSURE").getString());
                rsHDetail.updateString("MM_2ND_LINEAR_NIP_PRESSURE",(String)ObjMRItems.getAttribute("MM_2ND_LINEAR_NIP_PRESSURE").getString());
                rsHDetail.updateString("MM_3RD_LINEAR_NIP_PRESSURE",(String)ObjMRItems.getAttribute("MM_3RD_LINEAR_NIP_PRESSURE").getString());
                rsHDetail.updateString("MM_4TH_LINEAR_NIP_PRESSURE",(String)ObjMRItems.getAttribute("MM_4TH_LINEAR_NIP_PRESSURE").getString());
                rsHDetail.updateString("MM_LOADING_SYSTEM",(String)ObjMRItems.getAttribute("MM_LOADING_SYSTEM").getString());
                rsHDetail.updateString("MM_LP_SHOWER_NOZZLES",(String)ObjMRItems.getAttribute("MM_LP_SHOWER_NOZZLES").getString());
                rsHDetail.updateString("MM_1ST_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_1ST_ROLL_MATERIAL").getString());
                rsHDetail.updateString("MM_2ND_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_2ND_ROLL_MATERIAL").getString());
                rsHDetail.updateString("MM_3RD_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_3RD_ROLL_MATERIAL").getString());
                rsHDetail.updateString("MM_4TH_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_4TH_ROLL_MATERIAL").getString());
                rsHDetail.updateString("MM_5TH_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_5TH_ROLL_MATERIAL").getString());
                rsHDetail.updateString("MM_6TH_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_6TH_ROLL_MATERIAL").getString());
                rsHDetail.updateString("MM_7TH_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_7TH_ROLL_MATERIAL").getString());
                rsHDetail.updateString("MM_8TH_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_8TH_ROLL_MATERIAL").getString());
                rsHDetail.updateString("MM_BATT_GSM",(String)ObjMRItems.getAttribute("MM_BATT_GSM").getString());
                rsHDetail.updateString("MM_FIBERS_USED",(String)ObjMRItems.getAttribute("MM_FIBERS_USED").getString());
                rsHDetail.updateString("MM_STRETCH",(String)ObjMRItems.getAttribute("MM_STRETCH").getString());
                rsHDetail.updateString("MM_MG",(String)ObjMRItems.getAttribute("MM_MG").getString());
                rsHDetail.updateString("MM_YANKEE",(String)ObjMRItems.getAttribute("MM_YANKEE").getString());
                rsHDetail.updateString("MM_MG_YANKEE_NIP_LOAD",(String)ObjMRItems.getAttribute("MM_MG_YANKEE_NIP_LOAD").getString());
                
                rsHDetail.updateString("MM_CATEGORY",(String)ObjMRItems.getAttribute("MM_CATEGORY").getString());
                rsHDetail.updateString("UC_CODE",(String)ObjMRItems.getAttribute("UC_CODE").getString());
                
                
                rsHDetail.updateString("MM_MIN_CIRCUIT_LENGTH",(String)ObjMRItems.getAttribute("MM_MIN_CIRCUIT_LENGTH").getString());
                rsHDetail.updateString("MM_MAX_CIRCUIT_LENGTH",(String)ObjMRItems.getAttribute("MM_MAX_CIRCUIT_LENGTH").getString());
                rsHDetail.updateString("GOAL",(String)ObjMRItems.getAttribute("GOAL").getString());
                
                rsHDetail.updateString("CREATED_BY", Integer.toString(EITLERPGLOBAL.gNewUserID));
                //  mdttm=EITLERPGLOBAL.getCurrentDate()+""+EITLERPGLOBAL.getCurrentTime();
                //  rsHDetail.updateString("CREATED_DATE",mdttm);
                rsHDetail.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

                rsHDetail.insertRow();
            }

            //===================== Update the Approval Flow ======================//
            setAttribute("FROM", EITLERPGLOBAL.gUserID);

            //ApprovalFlow ObjFlow=new ApprovalFlow();
            clsFeltProductionApprovalFlow ObjFlow = new clsFeltProductionApprovalFlow();

            ObjFlow.ModuleID = clsSales_Party.ModuleID;
            ObjFlow.ModuleID = clsmachinesurveyAmend.ModuleID;
            //ObjFlow.DocNo = (String) getAttribute("MM_DOC_NO").getObj();
            ObjFlow.DocNo = (String) getAttribute("MM_AMEND_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("CREATED_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            //ObjFlow.TableName="D_SAL_PARTY_MASTER";
            ObjFlow.TableName = "PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER";
            ObjFlow.IsCreator = true;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "MM_DOC_NO";

            if (ObjFlow.Status.equals("H")) {
                ObjFlow.Status = "A";
                ObjFlow.To = ObjFlow.From;
                ObjFlow.UpdateFlow();
            } else {
                if (!ObjFlow.UpdateFlow()) {
                    LastError = ObjFlow.LastError;
                }
            }

            if (ObjFlow.Status.equals("F")) {
                ObjFlow.DocNo = ObjFlow.DocNo.substring(0, 8);
                System.out.println("MM_DOC_NO = " + ObjFlow.DocNo);
                data.Execute("DELETE FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER WHERE MM_DOC_NO='" + ObjFlow.DocNo + "'");
                //data.Execute("INSERT INTO PRODUCTION.FELT_MACHINE_MASTER_DETAIL(MM_DOC_NO,MM_PARTY_CODE,MM_PARTY_NAME,MM_MACHINE_NO,MM_MACHINE_TYPE_FORMING,MM_PAPER_GRADE,MM_MACHINE_SPEED_RANGE,MM_PAPER_GSM_RANGE,MM_MACHINE_TYPE_PRESSING,MM_FURNISH,MM_TYPE_OF_FILLER,MM_WIRE_DETAIL_1,MM_WIRE_DETAIL_2,MM_WIRE_DETAIL_3,MM_WIRE_DETAIL_4,MM_PAPER_DECKLE_AFTER_WIRE,MM_PAPER_DECKLE_AFTER_PRESS,MM_PAPER_DECKLE_AT_POPE_REEL,MM_DRYER_SECTION,MM_ZONE,MM_CAPACITY,MM_MACHINE_STATUS,MM_ZONE_REPRESENTATIVE,MM_DATE_OF_UPDATE,MM_TOTAL_DRYER_GROUP,MM_UNIRUM_GROUP,MM_CONVENTIONAL_GROUP,MM_HOOD_TYPE,MM_SIZE_PRESS,MM_SIZE_PRESS_POSITION,MM_SHEET_DRYNESS_SIZE_PRESS)(SELECT MM_DOC_NO,MM_PARTY_CODE,MM_PARTY_NAME,MM_MACHINE_NO,MM_MACHINE_TYPE_FORMING,MM_PAPER_GRADE,MM_MACHINE_SPEED_RANGE,MM_PAPER_GSM_RANGE,MM_MACHINE_TYPE_PRESSING,MM_FURNISH,MM_TYPE_OF_FILLER,MM_WIRE_DETAIL_1,MM_WIRE_DETAIL_2,MM_WIRE_DETAIL_3,MM_WIRE_DETAIL_4,MM_PAPER_DECKLE_AFTER_WIRE,MM_PAPER_DECKLE_AFTER_PRESS,MM_PAPER_DECKLE_AT_POPE_REEL,MM_DRYER_SECTION,MM_ZONE,MM_CAPACITY,MM_MACHINE_STATUS,MM_ZONE_REPRESENTATIVE,MM_DATE_OF_UPDATE,MM_TOTAL_DRYER_GROUP,MM_UNIRUM_GROUP,MM_CONVENTIONAL_GROUP,MM_HOOD_TYPE,MM_SIZE_PRESS,MM_SIZE_PRESS_POSITION,MM_SHEET_DRYNESS_SIZE_PRESS FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER WHERE MM_DOC_NO='" + ObjFlow.DocNo + "')");
                //data.Execute("INSERT INTO PRODUCTION.FELT_MACHINE_MASTER_HEADER(MM_DOC_NO,MM_PARTY_CODE,MM_PARTY_NAME,MM_MACHINE_NO,MM_MACHINE_TYPE_FORMING,MM_PAPER_GRADE,MM_MACHINE_SPEED_RANGE,MM_PAPER_GSM_RANGE,MM_MACHINE_TYPE_PRESSING,MM_FURNISH,MM_TYPE_OF_FILLER,MM_WIRE_DETAIL_1,MM_WIRE_DETAIL_2,MM_WIRE_DETAIL_3,MM_WIRE_DETAIL_4,MM_PAPER_DECKLE_AFTER_WIRE,MM_PAPER_DECKLE_AFTER_PRESS,MM_PAPER_DECKLE_AT_POPE_REEL,MM_DRYER_SECTION,MM_ZONE,MM_CAPACITY,MM_MACHINE_STATUS,MM_ZONE_REPRESENTATIVE,MM_DATE_OF_UPDATE,MM_TOTAL_DRYER_GROUP,MM_UNIRUM_GROUP,MM_CONVENTIONAL_GROUP,MM_HOOD_TYPE,MM_SIZE_PRESS,MM_SIZE_PRESS_POSITION,MM_SHEET_DRYNESS_SIZE_PRESS)(SELECT MM_DOC_NO,MM_PARTY_CODE,MM_PARTY_NAME,MM_MACHINE_NO,MM_MACHINE_TYPE_FORMING,MM_PAPER_GRADE,MM_MACHINE_SPEED_RANGE,MM_PAPER_GSM_RANGE,MM_MACHINE_TYPE_PRESSING,MM_FURNISH,MM_TYPE_OF_FILLER,MM_WIRE_DETAIL_1,MM_WIRE_DETAIL_2,MM_WIRE_DETAIL_3,MM_WIRE_DETAIL_4,MM_PAPER_DECKLE_AFTER_WIRE,MM_PAPER_DECKLE_AFTER_PRESS,MM_PAPER_DECKLE_AT_POPE_REEL,MM_DRYER_SECTION,MM_ZONE,MM_CAPACITY,MM_MACHINE_STATUS,MM_ZONE_REPRESENTATIVE,MM_DATE_OF_UPDATE,MM_TOTAL_DRYER_GROUP,MM_UNIRUM_GROUP,MM_CONVENTIONAL_GROUP,MM_HOOD_TYPE,MM_SIZE_PRESS,MM_SIZE_PRESS_POSITION,MM_SHEET_DRYNESS_SIZE_PRESS FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER WHERE MM_AMEND_NO='" + getAttribute("MM_AMEND_NO").getString() + "')");
                data.Execute("INSERT INTO PRODUCTION.FELT_MACHINE_MASTER_HEADER(MM_DOC_NO,MM_PARTY_CODE,MM_PARTY_NAME,MM_INCHARGE_NAME,MM_STATION,MM_MACHINE_NO,MM_MACHINE_TYPE_FORMING,MM_PAPER_GRADE,MM_MACHINE_SPEED_RANGE,MM_PAPER_GSM_RANGE,MM_MACHINE_TYPE_PRESSING,MM_FURNISH,MM_TYPE_OF_FILLER,MM_WIRE_DETAIL_1,MM_WIRE_DETAIL_2,MM_WIRE_DETAIL_3,MM_WIRE_DETAIL_4,MM_PAPER_DECKLE_AFTER_WIRE,MM_PAPER_DECKLE_AFTER_PRESS,MM_PAPER_DECKLE_AT_POPE_REEL,MM_DRYER_SECTION,MM_ZONE,MM_CAPACITY,MM_MACHINE_STATUS,MM_ZONE_REPRESENTATIVE,MM_DATE_OF_UPDATE,MM_TOTAL_DRYER_GROUP,MM_UNIRUM_GROUP,MM_CONVENTIONAL_GROUP,MM_HOOD_TYPE,MM_SIZE_PRESS,MM_SIZE_PRESS_POSITION,MM_SHEET_DRYNESS_SIZE_PRESS,MM_DRIVE_TYPE,MM_MACHINE_MAKE)(SELECT MM_DOC_NO,MM_PARTY_CODE,MM_PARTY_NAME,MM_INCHARGE_NAME,MM_STATION,MM_MACHINE_NO,MM_MACHINE_TYPE_FORMING,MM_PAPER_GRADE,MM_MACHINE_SPEED_RANGE,MM_PAPER_GSM_RANGE,MM_MACHINE_TYPE_PRESSING,MM_FURNISH,MM_TYPE_OF_FILLER,MM_WIRE_DETAIL_1,MM_WIRE_DETAIL_2,MM_WIRE_DETAIL_3,MM_WIRE_DETAIL_4,MM_PAPER_DECKLE_AFTER_WIRE,MM_PAPER_DECKLE_AFTER_PRESS,MM_PAPER_DECKLE_AT_POPE_REEL,MM_DRYER_SECTION,MM_ZONE,MM_CAPACITY,MM_MACHINE_STATUS,MM_ZONE_REPRESENTATIVE,MM_DATE_OF_UPDATE,MM_TOTAL_DRYER_GROUP,MM_UNIRUM_GROUP,MM_CONVENTIONAL_GROUP,MM_HOOD_TYPE,MM_SIZE_PRESS,MM_SIZE_PRESS_POSITION,MM_SHEET_DRYNESS_SIZE_PRESS,MM_DRIVE_TYPE,MM_MACHINE_MAKE FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER WHERE MM_AMEND_NO='" + getAttribute("MM_AMEND_NO").getString() + "' )");
                
                data.Execute("DELETE FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL WHERE MM_DOC_NO='" + ObjFlow.DocNo + "'");
                //data.Execute("INSERT INTO PRODUCTION.FELT_MACHINE_MASTER_DETAIL(SR_NO,MM_DOC_NO,MM_PARTY_CODE,MM_MACHINE_NO,MM_MACHINE_POSITION,MM_MACHINE_POSITION_DESC,MM_COMBINATION_CODE,MM_PRESS_TYPE,MM_PRESS_ROLL_DAI_MM,MM_PRESS_ROLL_FACE_TOTAL_MM,MM_PRESS_ROLL_FACE_NET_MM,MM_PRESS_LOAD,MM_VACCUM_CAPACITY,MM_UHLE_BOX,MM_HP_SHOWER,MM_LP_SHOWER,MM_FELT_LENGTH,MM_FELT_WIDTH,MM_FELT_GSM,MM_FELT_WEIGHT,MM_FELT_TYPE,MM_FELT_STYLE,MM_AVG_LIFE,MM_AVG_PRODUCTION,MM_FELT_CONSUMPTION,MM_DINESH_SHARE,MM_REMARK_DESIGN,MM_REMARK_GENERAL,MM_NO_DRYER_CYLINDER,MM_CYLINDER_DIA_MM,MM_CYLINDER_FACE_NET_MM,MM_DRIVE_TYPE,MM_GUIDE_TYPE,MM_GUIDE_PAM_TYPE,MM_VENTILATION_TYPE,MM_FABRIC_LENGTH,MM_FABRIC_WIDTH,MM_SIZE_M2,MM_SCREEN_TYPE,MM_STYLE_DRY,MM_CFM_DRY,MM_AVG_LIFE_DRY,MM_CONSUMPTION_DRY,MM_DINESH_SHARE_DRY,MM_REMARK_DRY,MM_ITEM_CODE,MM_GRUP,MM_BASE_GSM,MM_WEB_GSM,MM_TOTAL_GSM)(SELECT SR_NO,MM_DOC_NO,MM_PARTY_CODE,MM_MACHINE_NO,MM_MACHINE_POSITION,MM_MACHINE_POSITION_DESC,MM_COMBINATION_CODE,MM_PRESS_TYPE,MM_PRESS_ROLL_DAI_MM,MM_PRESS_ROLL_FACE_TOTAL_MM,MM_PRESS_ROLL_FACE_NET_MM,MM_PRESS_LOAD,MM_VACCUM_CAPACITY,MM_UHLE_BOX,MM_HP_SHOWER,MM_LP_SHOWER,MM_FELT_LENGTH,MM_FELT_WIDTH,MM_FELT_GSM,MM_FELT_WEIGHT,MM_FELT_TYPE,MM_FELT_STYLE,MM_AVG_LIFE,MM_AVG_PRODUCTION,MM_FELT_CONSUMPTION,MM_DINESH_SHARE,MM_REMARK_DESIGN,MM_REMARK_GENERAL,MM_NO_DRYER_CYLINDER,MM_CYLINDER_DIA_MM,MM_CYLINDER_FACE_NET_MM,MM_DRIVE_TYPE,MM_GUIDE_TYPE,MM_GUIDE_PAM_TYPE,MM_VENTILATION_TYPE,MM_FABRIC_LENGTH,MM_FABRIC_WIDTH,MM_SIZE_M2,MM_SCREEN_TYPE,MM_STYLE_DRY,MM_CFM_DRY,MM_AVG_LIFE_DRY,MM_CONSUMPTION_DRY,MM_DINESH_SHARE_DRY,MM_REMARK_DRY,MM_ITEM_CODE,MM_GRUP,MM_BASE_GSM,MM_WEB_GSM,MM_TOTAL_GSM FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_DETAIL WHERE MM_AMEND_NO='" + getAttribute("MM_AMEND_NO").getString() + "')")
                data.Execute("INSERT INTO PRODUCTION.FELT_MACHINE_MASTER_DETAIL(SR_NO,MM_DOC_NO,MM_PARTY_CODE,MM_MACHINE_NO,MM_MACHINE_POSITION,MM_MACHINE_POSITION_DESC,MM_COMBINATION_CODE,MM_PRESS_TYPE,MM_PRESS_ROLL_DAI_MM,MM_PRESS_ROLL_FACE_TOTAL_MM,MM_PRESS_ROLL_FACE_NET_MM,MM_PRESS_LOAD,MM_VACCUM_CAPACITY,MM_UHLE_BOX,MM_HP_SHOWER,MM_LP_SHOWER,MM_FELT_LENGTH,MM_FELT_WIDTH,MM_FELT_GSM,MM_FELT_WEIGHT,MM_FELT_TYPE,MM_FELT_STYLE,MM_AVG_LIFE,MM_AVG_PRODUCTION,MM_FELT_CONSUMPTION,MM_DINESH_SHARE,MM_REMARK_DESIGN,MM_REMARK_GENERAL,MM_NO_DRYER_CYLINDER,MM_CYLINDER_DIA_MM,MM_CYLINDER_FACE_NET_MM,MM_DRIVE_TYPE,MM_GUIDE_TYPE,MM_GUIDE_PAM_TYPE,MM_VENTILATION_TYPE,MM_FABRIC_LENGTH,MM_FABRIC_WIDTH,MM_SIZE_M2,MM_SCREEN_TYPE,MM_STYLE_DRY,MM_CFM_DRY,MM_AVG_LIFE_DRY,MM_CONSUMPTION_DRY,MM_DINESH_SHARE_DRY,MM_REMARK_DRY,MM_ITEM_CODE,MM_GRUP,MM_BASE_GSM,MM_WEB_GSM,MM_TOTAL_GSM,MM_FELT_LIFE,MM_TPD,MM_TOTAL_PRODUCTION,MM_PAPER_FELT,MM_POSITION_WISE,MM_P_NO_TEMP,MM_M_NO_TEMP,MM_VACCUM_IN_UHLE_BOX,MM_FELT_WASHING_CHEMICALS,MM_HARDNESS,MM_MACHINE_FLOOR,MM_TYPE_OF_FORMING_FABRIC,MM_PRESS_ROLL_FOR_1ST_NIP_FACE_LENGTH,MM_PRESS_ROLL_FOR_2ND_NIP_FACE_LENGTH,MM_PRESS_ROLL_FOR_3RD_NIP_FACE_LENGTH,MM_PRESS_ROLL_FOR_4TH_NIP_FACE_LENGTH,MM_WASH_ROLL_SHOWER,MM_HP_SHOWER_NOZZLES,MM_UHLE_BOX_VACUUM,MM_CHEMICAL_SHOWER,MM_1ST_LINEAR_NIP_PRESSURE,MM_2ND_LINEAR_NIP_PRESSURE,MM_3RD_LINEAR_NIP_PRESSURE,MM_4TH_LINEAR_NIP_PRESSURE,MM_LOADING_SYSTEM,MM_LP_SHOWER_NOZZLES,MM_1ST_ROLL_MATERIAL,MM_2ND_ROLL_MATERIAL,MM_3RD_ROLL_MATERIAL,MM_4TH_ROLL_MATERIAL,MM_5TH_ROLL_MATERIAL,MM_6TH_ROLL_MATERIAL,MM_7TH_ROLL_MATERIAL,MM_8TH_ROLL_MATERIAL,MM_BATT_GSM,MM_FIBERS_USED,MM_STRETCH,MM_MG,MM_YANKEE,MM_MG_YANKEE_NIP_LOAD,MM_NUMBER_OF_FORMING_FABRIC)(SELECT SR_NO,MM_DOC_NO,MM_PARTY_CODE,MM_MACHINE_NO,MM_MACHINE_POSITION,MM_MACHINE_POSITION_DESC,MM_COMBINATION_CODE,MM_PRESS_TYPE,MM_PRESS_ROLL_DAI_MM,MM_PRESS_ROLL_FACE_TOTAL_MM,MM_PRESS_ROLL_FACE_NET_MM,MM_PRESS_LOAD,MM_VACCUM_CAPACITY,MM_UHLE_BOX,MM_HP_SHOWER,MM_LP_SHOWER,MM_FELT_LENGTH,MM_FELT_WIDTH,MM_FELT_GSM,MM_FELT_WEIGHT,MM_FELT_TYPE,MM_FELT_STYLE,MM_AVG_LIFE,MM_AVG_PRODUCTION,MM_FELT_CONSUMPTION,MM_DINESH_SHARE,MM_REMARK_DESIGN,MM_REMARK_GENERAL,MM_NO_DRYER_CYLINDER,MM_CYLINDER_DIA_MM,MM_CYLINDER_FACE_NET_MM,MM_DRIVE_TYPE,MM_GUIDE_TYPE,MM_GUIDE_PAM_TYPE,MM_VENTILATION_TYPE,MM_FABRIC_LENGTH,MM_FABRIC_WIDTH,MM_SIZE_M2,MM_SCREEN_TYPE,MM_STYLE_DRY,MM_CFM_DRY,MM_AVG_LIFE_DRY,MM_CONSUMPTION_DRY,MM_DINESH_SHARE_DRY,MM_REMARK_DRY,MM_ITEM_CODE,MM_GRUP,MM_BASE_GSM,MM_WEB_GSM,MM_TOTAL_GSM,MM_FELT_LIFE,MM_TPD,MM_TOTAL_PRODUCTION,MM_PAPER_FELT,MM_POSITION_WISE,MM_P_NO_TEMP,MM_M_NO_TEMP,MM_VACCUM_IN_UHLE_BOX,MM_FELT_WASHING_CHEMICALS,MM_HARDNESS,MM_MACHINE_FLOOR,MM_TYPE_OF_FORMING_FABRIC,MM_PRESS_ROLL_FOR_1ST_NIP_FACE_LENGTH,MM_PRESS_ROLL_FOR_2ND_NIP_FACE_LENGTH,MM_PRESS_ROLL_FOR_3RD_NIP_FACE_LENGTH,MM_PRESS_ROLL_FOR_4TH_NIP_FACE_LENGTH,MM_WASH_ROLL_SHOWER,MM_HP_SHOWER_NOZZLES,MM_UHLE_BOX_VACUUM,MM_CHEMICAL_SHOWER,MM_1ST_LINEAR_NIP_PRESSURE,MM_2ND_LINEAR_NIP_PRESSURE,MM_3RD_LINEAR_NIP_PRESSURE,MM_4TH_LINEAR_NIP_PRESSURE,MM_LOADING_SYSTEM,MM_LP_SHOWER_NOZZLES,MM_1ST_ROLL_MATERIAL,MM_2ND_ROLL_MATERIAL,MM_3RD_ROLL_MATERIAL,MM_4TH_ROLL_MATERIAL,MM_5TH_ROLL_MATERIAL,MM_6TH_ROLL_MATERIAL,MM_7TH_ROLL_MATERIAL,MM_8TH_ROLL_MATERIAL,MM_BATT_GSM,MM_FIBERS_USED,MM_STRETCH,MM_MG,MM_YANKEE,MM_MG_YANKEE_NIP_LOAD,MM_NUMBER_OF_FORMING_FABRIC FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_DETAIL WHERE MM_AMEND_NO='" + getAttribute("MM_AMEND_NO").getString() + "' )");

            }
        //================= Approval Flow Update complete ===================//

            MoveLast();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();

            return false;
        }

    }

    public boolean Update() {

        Statement stHistory, stHeader, stHDetail,tmpStmt;
        ResultSet rsHistory, rsHeader, rsHDetail,rsTmp;
        boolean Validate = true;

        try {

            String AStatus = (String) getAttribute("APPROVAL_STATUS").getObj();
            if (AStatus.equals("R")) {
                Validate = false;
            }
            Validate = true;

            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHDetail = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHistory = stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER_H WHERE MM_DOC_NO=''"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail = stHDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_DETAIL_H WHERE MM_DOC_NO=''");
            rsHDetail.first();
            rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_DETAIL WHERE MM_DOC_NO='' order by sr_no*1");
            rsTmp.first();

            //------------------------------------//
            String RevDocNo = (String) getAttribute("MM_DOC_NO").getObj();

            //rsResultSet.first();
            //rsResultSet.moveToInsertRow();
            rsResultSet.updateString("MM_AMEND_NO", getAttribute("MM_AMEND_NO").getString());
            rsResultSet.updateString("MM_DOC_NO", getAttribute("MM_DOC_NO").getString());
            rsResultSet.updateString("MM_PARTY_CODE", getAttribute("MM_PARTY_CODE").getString());
            rsResultSet.updateString("MM_PARTY_NAME", getAttribute("MM_PARTY_NAME").getString());
            rsResultSet.updateString("MM_STATION", getAttribute("MM_STATION").getString());
            rsResultSet.updateString("MM_INCHARGE_NAME", getAttribute("MM_INCHARGE_NAME").getString());
            rsResultSet.updateString("MM_MACHINE_NO", getAttribute("MM_MACHINE_NO").getString());
            rsResultSet.updateString("MM_MACHINE_TYPE_FORMING", getAttribute("MM_MACHINE_TYPE_FORMING").getString());
            rsResultSet.updateString("MM_PAPER_GRADE", getAttribute("MM_PAPER_GRADE").getString());
            rsResultSet.updateString("MM_MACHINE_SPEED_RANGE", getAttribute("MM_MACHINE_SPEED_RANGE").getString());
            rsResultSet.updateString("MM_PAPER_GSM_RANGE", getAttribute("MM_PAPER_GSM_RANGE").getString());
            rsResultSet.updateString("MM_MACHINE_TYPE_PRESSING", getAttribute("MM_MACHINE_TYPE_PRESSING").getString());
            rsResultSet.updateString("MM_FURNISH", getAttribute("MM_FURNISH").getString());
            rsResultSet.updateString("MM_TYPE_OF_FILLER", getAttribute("MM_TYPE_OF_FILLER").getString());
            rsResultSet.updateString("MM_WIRE_DETAIL_1", getAttribute("MM_WIRE_DETAIL_1").getString());
            rsResultSet.updateString("MM_WIRE_DETAIL_2", getAttribute("MM_WIRE_DETAIL_2").getString());
            rsResultSet.updateString("MM_WIRE_DETAIL_3", getAttribute("MM_WIRE_DETAIL_3").getString());
            rsResultSet.updateString("MM_WIRE_DETAIL_4", getAttribute("MM_WIRE_DETAIL_4").getString());
            rsResultSet.updateString("MM_PAPER_DECKLE_AFTER_WIRE", getAttribute("MM_PAPER_DECKLE_AFTER_WIRE").getString());
            rsResultSet.updateString("MM_PAPER_DECKLE_AFTER_PRESS", getAttribute("MM_PAPER_DECKLE_AFTER_PRESS").getString());
            rsResultSet.updateString("MM_PAPER_DECKLE_AT_POPE_REEL", getAttribute("MM_PAPER_DECKLE_AT_POPE_REEL").getString());
            rsResultSet.updateString("MM_DRYER_SECTION", getAttribute("MM_DRYER_SECTION").getString());
            rsResultSet.updateString("MM_ZONE", getAttribute("MM_ZONE").getString());
            rsResultSet.updateString("MM_CAPACITY", getAttribute("MM_CAPACITY").getString());
            rsResultSet.updateString("MM_MACHINE_STATUS", getAttribute("MM_MACHINE_STATUS").getString());
            rsResultSet.updateString("MM_ZONE_REPRESENTATIVE", getAttribute("MM_ZONE_REPRESENTATIVE").getString());
            if (EITLERPGLOBAL.formatDateDB(getAttribute("MM_DATE_OF_UPDATE").getString()).equals("")) {
                setAttribute("MM_DATE_OF_UPDATE", getAttribute("MM_DATE_OF_UPDATE").getString());
            } else {
                setAttribute("MM_DATE_OF_UPDATE", EITLERPGLOBAL.formatDateDB(getAttribute("MM_DATE_OF_UPDATE").getString()));
            }
            rsResultSet.updateString("MM_DATE_OF_UPDATE", getAttribute("MM_DATE_OF_UPDATE").getString());
            rsResultSet.updateString("MM_TOTAL_DRYER_GROUP", getAttribute("MM_TOTAL_DRYER_GROUP").getString());
            rsResultSet.updateString("MM_UNIRUM_GROUP", getAttribute("MM_UNIRUM_GROUP").getString());
            rsResultSet.updateString("MM_CONVENTIONAL_GROUP", getAttribute("MM_CONVENTIONAL_GROUP").getString());
            rsResultSet.updateString("MM_HOOD_TYPE", getAttribute("MM_HOOD_TYPE").getString());
            rsResultSet.updateString("MM_SIZE_PRESS", getAttribute("MM_SIZE_PRESS").getString());
            rsResultSet.updateString("MM_SIZE_PRESS_POSITION", getAttribute("MM_SIZE_PRESS_POSITION").getString());
            rsResultSet.updateString("MM_SHEET_DRYNESS_SIZE_PRESS", getAttribute("MM_SHEET_DRYNESS_SIZE_PRESS").getString());
            rsResultSet.updateString("MM_DRIVE_TYPE",getAttribute("MM_DRIVE_TYPE").getString());
            
            rsResultSet.updateString("MM_MACHINE_MAKE",getAttribute("MM_MACHINE_MAKE").getString());
            
            rsResultSet.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            //rsResultSet.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            //rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE", getAttribute("MODIFIED_DATE").getString());
            rsResultSet.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsResultSet.updateBoolean("APPROVED", false);
            rsResultSet.updateString("APPROVED_DATE", "0000-00-00");
            rsResultSet.updateBoolean("CANCELED", false);
            rsResultSet.updateBoolean("REJECTED", false);
            rsResultSet.updateString("REJECTED_DATE", "0000-00-00");
            rsResultSet.updateBoolean("CHANGED", true);
            rsResultSet.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateRow();

            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER_H WHERE MM_DOC_NO='" + (String) getAttribute("MM_DOC_NO").getObj() + "'");
            RevNo++;

            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO", RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateInt("UPDATE_BY",(int)getAttribute("MODIFIED_BY").getVal());
            //rsHistory.updateString("UPDATE_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("MM_AMEND_NO", getAttribute("MM_AMEND_NO").getString());
            //rsHistory.updateInt("UPDATE_BY", (int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
            // rsHistory.updateString("MM_DOC_NO",fms.mdocno);
            rsHistory.updateString("MM_DOC_NO", getAttribute("MM_DOC_NO").getString());
            rsHistory.updateString("MM_PARTY_CODE", getAttribute("MM_PARTY_CODE").getString());
            rsHistory.updateString("MM_PARTY_NAME", getAttribute("MM_PARTY_NAME").getString());
            rsHistory.updateString("MM_STATION", getAttribute("MM_STATION").getString());
            rsHistory.updateString("MM_INCHARGE_NAME", getAttribute("MM_INCHARGE_NAME").getString());
            rsHistory.updateString("MM_MACHINE_NO", getAttribute("MM_MACHINE_NO").getString());
            rsHistory.updateString("MM_MACHINE_TYPE_FORMING", getAttribute("MM_MACHINE_TYPE_FORMING").getString());
            rsHistory.updateString("MM_PAPER_GRADE", getAttribute("MM_PAPER_GRADE").getString());
            rsHistory.updateString("MM_MACHINE_SPEED_RANGE", getAttribute("MM_MACHINE_SPEED_RANGE").getString());
            rsHistory.updateString("MM_PAPER_GSM_RANGE", getAttribute("MM_PAPER_GSM_RANGE").getString());
            rsHistory.updateString("MM_MACHINE_TYPE_PRESSING", getAttribute("MM_MACHINE_TYPE_PRESSING").getString());
            rsHistory.updateString("MM_FURNISH", getAttribute("MM_FURNISH").getString());
            rsHistory.updateString("MM_TYPE_OF_FILLER", getAttribute("MM_TYPE_OF_FILLER").getString());
            rsHistory.updateString("MM_WIRE_DETAIL_1", getAttribute("MM_WIRE_DETAIL_1").getString());
            rsHistory.updateString("MM_WIRE_DETAIL_2", getAttribute("MM_WIRE_DETAIL_2").getString());
            rsHistory.updateString("MM_WIRE_DETAIL_3", getAttribute("MM_WIRE_DETAIL_3").getString());
            rsHistory.updateString("MM_WIRE_DETAIL_4", getAttribute("MM_WIRE_DETAIL_4").getString());
            rsHistory.updateString("MM_PAPER_DECKLE_AFTER_WIRE", getAttribute("MM_PAPER_DECKLE_AFTER_WIRE").getString());
            rsHistory.updateString("MM_PAPER_DECKLE_AFTER_PRESS", getAttribute("MM_PAPER_DECKLE_AFTER_PRESS").getString());
            rsHistory.updateString("MM_PAPER_DECKLE_AT_POPE_REEL", getAttribute("MM_PAPER_DECKLE_AT_POPE_REEL").getString());

            rsHistory.updateString("MM_DRYER_SECTION", getAttribute("MM_DRYER_SECTION").getString());
            rsHistory.updateString("MM_ZONE", getAttribute("MM_ZONE").getString());
            rsHistory.updateString("MM_CAPACITY", getAttribute("MM_CAPACITY").getString());
            rsHistory.updateString("MM_MACHINE_STATUS", getAttribute("MM_MACHINE_STATUS").getString());
            rsHistory.updateString("MM_ZONE_REPRESENTATIVE", getAttribute("MM_ZONE_REPRESENTATIVE").getString());
            if (EITLERPGLOBAL.formatDateDB(getAttribute("MM_DATE_OF_UPDATE").getString()).equals("")) {
                setAttribute("MM_DATE_OF_UPDATE", "0000-00-00");
            } else {
                setAttribute("MM_DATE_OF_UPDATE", EITLERPGLOBAL.formatDateDB(getAttribute("MM_DATE_OF_UPDATE").getString()));
            }
            rsHistory.updateString("MM_DATE_OF_UPDATE", getAttribute("MM_DATE_OF_UPDATE").getString());

            rsHistory.updateString("MM_TOTAL_DRYER_GROUP", getAttribute("MM_TOTAL_DRYER_GROUP").getString());
            rsHistory.updateString("MM_UNIRUM_GROUP", getAttribute("MM_UNIRUM_GROUP").getString());
            rsHistory.updateString("MM_CONVENTIONAL_GROUP", getAttribute("MM_CONVENTIONAL_GROUP").getString());
            rsHistory.updateString("MM_HOOD_TYPE", getAttribute("MM_HOOD_TYPE").getString());
            rsHistory.updateString("MM_SIZE_PRESS", getAttribute("MM_SIZE_PRESS").getString());
            rsHistory.updateString("MM_SIZE_PRESS_POSITION", getAttribute("MM_SIZE_PRESS_POSITION").getString());
            rsHistory.updateString("MM_SHEET_DRYNESS_SIZE_PRESS", getAttribute("MM_SHEET_DRYNESS_SIZE_PRESS").getString());
            rsHistory.updateString("MM_DRIVE_TYPE",getAttribute("MM_DRIVE_TYPE").getString());
            
            rsHistory.updateString("MM_MACHINE_MAKE",getAttribute("MM_MACHINE_MAKE").getString());
            
           //sHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            // rsHistory.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            //rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("MODIFIED_DATE", getAttribute("MODIFIED_DATE").getString());
            rsHistory.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            rsHistory.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsHistory.updateBoolean("APPROVED", false);
            rsHistory.updateString("APPROVED_DATE", "0000-00-00");
            rsHistory.updateBoolean("REJECTED", false);
            rsHistory.updateString("REJECTED_DATE", "0000-00-00");
            rsHistory.updateBoolean("CANCELED", false);
            rsHistory.updateBoolean("CHANGED", true);
            rsHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();

//            ResultSet rsTmp;
//            Statement tmpStmt;
//
//            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
//            rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_DETAIL WHERE MM_DOC_NO='' order by sr_no*1");

            //   String tmpmachinests="";
            // String msurvydt="";
            //   String mdttm="";
            
            data.Execute("DELETE FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_DETAIL WHERE MM_AMEND_NO='" + getAttribute("MM_AMEND_NO").getString() + "'");

            //Now Insert records into detail table
             for (int i = 1; i <= colMRItems.size(); i++) {

                clsmachinesurveyitemAmend ObjMRItems = (clsmachinesurveyitemAmend) colMRItems.get(Integer.toString(i));

                rsTmp.moveToInsertRow();

                rsTmp.updateString("MM_DOC_NO", getAttribute("MM_DOC_NO").getString());
                System.out.println((String) ObjMRItems.getAttribute("MM_MACHINE_POSITION").getString());
                //   rsTmp.updateString("MM_PARTY_CODE",(String)ObjMRItems.getAttribute("MM_MACHINE_POSITION").getString());
                rsTmp.updateString("SR_NO", (String) ObjMRItems.getAttribute("SR_NO").getString());
                rsTmp.updateString("MM_AMEND_NO", getAttribute("MM_AMEND_NO").getString());
                rsTmp.updateString("MM_PARTY_CODE", getAttribute("MM_PARTY_CODE").getString());
                rsTmp.updateString("MM_MACHINE_NO", getAttribute("MM_MACHINE_NO").getString());
                rsTmp.updateString("MM_MACHINE_POSITION", (String) ObjMRItems.getAttribute("MM_MACHINE_POSITION").getString());
                rsTmp.updateString("MM_MACHINE_POSITION_DESC", (String) ObjMRItems.getAttribute("MM_MACHINE_POSITION_DESC").getString());
                rsTmp.updateString("MM_COMBINATION_CODE", (String) ObjMRItems.getAttribute("MM_COMBINATION_CODE").getString());
                rsTmp.updateString("MM_PRESS_TYPE", (String) ObjMRItems.getAttribute("MM_PRESS_TYPE").getString());
                rsTmp.updateString("MM_PRESS_ROLL_DAI_MM", (String) ObjMRItems.getAttribute("MM_PRESS_ROLL_DAI_MM").getString());
                rsTmp.updateString("MM_PRESS_ROLL_FACE_TOTAL_MM", (String) ObjMRItems.getAttribute("MM_PRESS_ROLL_FACE_TOTAL_MM").getString());
                rsTmp.updateString("MM_PRESS_ROLL_FACE_NET_MM", (String) ObjMRItems.getAttribute("MM_PRESS_ROLL_FACE_NET_MM").getString());
                rsTmp.updateString("MM_FELT_ROLL_WIDTH_MM", (String) ObjMRItems.getAttribute("MM_FELT_ROLL_WIDTH_MM").getString());
                rsTmp.updateString("MM_PRESS_LOAD", (String) ObjMRItems.getAttribute("MM_PRESS_LOAD").getString());
                rsTmp.updateString("MM_VACCUM_CAPACITY", (String) ObjMRItems.getAttribute("MM_VACCUM_CAPACITY").getString());
                rsTmp.updateString("MM_UHLE_BOX", (String) ObjMRItems.getAttribute("MM_UHLE_BOX").getString());
                rsTmp.updateString("MM_HP_SHOWER", (String) ObjMRItems.getAttribute("MM_HP_SHOWER").getString());
                rsTmp.updateString("MM_LP_SHOWER", (String) ObjMRItems.getAttribute("MM_LP_SHOWER").getString());
                rsTmp.updateString("MM_FELT_LENGTH", (String) ObjMRItems.getAttribute("MM_FELT_LENGTH").getString());
                rsTmp.updateString("MM_FELT_WIDTH", (String) ObjMRItems.getAttribute("MM_FELT_WIDTH").getString());
                rsTmp.updateString("MM_FELT_GSM", (String) ObjMRItems.getAttribute("MM_FELT_GSM").getString());
                rsTmp.updateString("MM_FELT_WEIGHT", (String) ObjMRItems.getAttribute("MM_FELT_WEIGHT").getString());
                rsTmp.updateString("MM_FELT_TYPE", (String) ObjMRItems.getAttribute("MM_FELT_TYPE").getString());
                rsTmp.updateString("MM_FELT_STYLE", (String) ObjMRItems.getAttribute("MM_FELT_STYLE").getString());
                rsTmp.updateString("MM_AVG_LIFE", (String) ObjMRItems.getAttribute("MM_AVG_LIFE").getString());
                rsTmp.updateString("MM_AVG_PRODUCTION", (String) ObjMRItems.getAttribute("MM_AVG_PRODUCTION").getString());
                rsTmp.updateString("MM_FELT_CONSUMPTION", (String) ObjMRItems.getAttribute("MM_FELT_CONSUMPTION").getString());
                rsTmp.updateString("MM_DINESH_SHARE", (String) ObjMRItems.getAttribute("MM_DINESH_SHARE").getString());
                rsTmp.updateString("MM_REMARK_DESIGN", (String) ObjMRItems.getAttribute("MM_REMARK_DESIGN").getString());
                rsTmp.updateString("MM_REMARK_GENERAL", (String) ObjMRItems.getAttribute("MM_REMARK_GENERAL").getString());
                rsTmp.updateString("MM_NO_DRYER_CYLINDER", (String) ObjMRItems.getAttribute("MM_NO_DRYER_CYLINDER").getString());
                rsTmp.updateString("MM_CYLINDER_DIA_MM", (String) ObjMRItems.getAttribute("MM_CYLINDER_DIA_MM").getString());
                rsTmp.updateString("MM_CYLINDER_FACE_NET_MM", (String) ObjMRItems.getAttribute("MM_CYLINDER_FACE_NET_MM").getString());

                rsTmp.updateString("MM_FELT_LIFE", (String) ObjMRItems.getAttribute("MM_FELT_LIFE").getString());
                rsTmp.updateString("MM_TPD", (String) ObjMRItems.getAttribute("MM_TPD").getString());
                rsTmp.updateString("MM_TOTAL_PRODUCTION", (String) ObjMRItems.getAttribute("MM_TOTAL_PRODUCTION").getString());
                rsTmp.updateString("MM_PAPER_FELT", (String) ObjMRItems.getAttribute("MM_PAPER_FELT").getString());

                rsTmp.updateString("MM_DRIVE_TYPE", (String) ObjMRItems.getAttribute("MM_DRIVE_TYPE").getString());
                rsTmp.updateString("MM_GUIDE_TYPE", (String) ObjMRItems.getAttribute("MM_GUIDE_TYPE").getString());
                rsTmp.updateString("MM_GUIDE_PAM_TYPE", (String) ObjMRItems.getAttribute("MM_GUIDE_PAM_TYPE").getString());
                rsTmp.updateString("MM_VENTILATION_TYPE", (String) ObjMRItems.getAttribute("MM_VENTILATION_TYPE").getString());
                rsTmp.updateString("MM_FABRIC_LENGTH", (String) ObjMRItems.getAttribute("MM_FABRIC_LENGTH").getString());
                rsTmp.updateString("MM_FABRIC_WIDTH", (String) ObjMRItems.getAttribute("MM_FABRIC_WIDTH").getString());
                rsTmp.updateString("MM_SIZE_M2", (String) ObjMRItems.getAttribute("MM_SIZE_M2").getString());
                rsTmp.updateString("MM_SCREEN_TYPE", (String) ObjMRItems.getAttribute("MM_SCREEN_TYPE").getString());
                rsTmp.updateString("MM_STYLE_DRY", (String) ObjMRItems.getAttribute("MM_STYLE_DRY").getString());
                rsTmp.updateString("MM_CFM_DRY", (String) ObjMRItems.getAttribute("MM_CFM_DRY").getString());
                rsTmp.updateString("MM_AVG_LIFE_DRY", (String) ObjMRItems.getAttribute("MM_AVG_LIFE_DRY").getString());
                rsTmp.updateString("MM_CONSUMPTION_DRY", (String) ObjMRItems.getAttribute("MM_CONSUMPTION_DRY").getString());
                rsTmp.updateString("MM_DINESH_SHARE_DRY", (String) ObjMRItems.getAttribute("MM_DINESH_SHARE_DRY").getString());
                rsTmp.updateString("MM_REMARK_DRY", (String) ObjMRItems.getAttribute("MM_REMARK_DRY").getString());
                rsTmp.updateString("MM_ITEM_CODE", (String) ObjMRItems.getAttribute("MM_ITEM_CODE").getString());
                rsTmp.updateString("MM_GRUP", (String) ObjMRItems.getAttribute("MM_GRUP").getString());
                rsTmp.updateString("MM_BASE_GSM", (String) ObjMRItems.getAttribute("MM_BASE_GSM").getString());
                rsTmp.updateString("MM_WEB_GSM", (String) ObjMRItems.getAttribute("MM_WEB_GSM").getString());
                rsTmp.updateString("MM_TOTAL_GSM", (String) ObjMRItems.getAttribute("MM_TOTAL_GSM").getString());
                rsTmp.updateString("MM_POSITION_WISE", (String) ObjMRItems.getAttribute("MM_POSITION_WISE").getString());
                rsTmp.updateString("MM_HARDNESS",(String)ObjMRItems.getAttribute("MM_HARDNESS").getString());
                rsTmp.updateString("MM_FELT_WASHING_CHEMICALS",(String)ObjMRItems.getAttribute("MM_FELT_WASHING_CHEMICALS").getString());
                rsTmp.updateString("MM_VACCUM_IN_UHLE_BOX",(String)ObjMRItems.getAttribute("MM_VACCUM_IN_UHLE_BOX").getString());
                
                rsTmp.updateString("MM_MACHINE_FLOOR",(String)ObjMRItems.getAttribute("MM_MACHINE_FLOOR").getString());
                rsTmp.updateString("MM_NUMBER_OF_FORMING_FABRIC",(String)ObjMRItems.getAttribute("MM_NUMBER_OF_FORMING_FABRIC").getString());
                rsTmp.updateString("MM_TYPE_OF_FORMING_FABRIC",(String)ObjMRItems.getAttribute("MM_TYPE_OF_FORMING_FABRIC").getString());
                rsTmp.updateString("MM_PRESS_ROLL_FOR_1ST_NIP_FACE_LENGTH",(String)ObjMRItems.getAttribute("MM_PRESS_ROLL_FOR_1ST_NIP_FACE_LENGTH").getString());
                rsTmp.updateString("MM_PRESS_ROLL_FOR_2ND_NIP_FACE_LENGTH",(String)ObjMRItems.getAttribute("MM_PRESS_ROLL_FOR_2ND_NIP_FACE_LENGTH").getString());
                rsTmp.updateString("MM_PRESS_ROLL_FOR_3RD_NIP_FACE_LENGTH",(String)ObjMRItems.getAttribute("MM_PRESS_ROLL_FOR_3RD_NIP_FACE_LENGTH").getString());
                rsTmp.updateString("MM_PRESS_ROLL_FOR_4TH_NIP_FACE_LENGTH",(String)ObjMRItems.getAttribute("MM_PRESS_ROLL_FOR_4TH_NIP_FACE_LENGTH").getString());
                rsTmp.updateString("MM_WASH_ROLL_SHOWER",(String)ObjMRItems.getAttribute("MM_WASH_ROLL_SHOWER").getString());
                rsTmp.updateString("MM_HP_SHOWER_NOZZLES",(String)ObjMRItems.getAttribute("MM_HP_SHOWER_NOZZLES").getString());
                rsTmp.updateString("MM_UHLE_BOX_VACUUM",(String)ObjMRItems.getAttribute("MM_UHLE_BOX_VACUUM").getString());
                rsTmp.updateString("MM_CHEMICAL_SHOWER",(String)ObjMRItems.getAttribute("MM_CHEMICAL_SHOWER").getString());
                rsTmp.updateString("MM_1ST_LINEAR_NIP_PRESSURE",(String)ObjMRItems.getAttribute("MM_1ST_LINEAR_NIP_PRESSURE").getString());
                rsTmp.updateString("MM_2ND_LINEAR_NIP_PRESSURE",(String)ObjMRItems.getAttribute("MM_2ND_LINEAR_NIP_PRESSURE").getString());
                rsTmp.updateString("MM_3RD_LINEAR_NIP_PRESSURE",(String)ObjMRItems.getAttribute("MM_3RD_LINEAR_NIP_PRESSURE").getString());
                rsTmp.updateString("MM_4TH_LINEAR_NIP_PRESSURE",(String)ObjMRItems.getAttribute("MM_4TH_LINEAR_NIP_PRESSURE").getString());
                rsTmp.updateString("MM_LOADING_SYSTEM",(String)ObjMRItems.getAttribute("MM_LOADING_SYSTEM").getString());
                rsTmp.updateString("MM_LP_SHOWER_NOZZLES",(String)ObjMRItems.getAttribute("MM_LP_SHOWER_NOZZLES").getString());
                rsTmp.updateString("MM_1ST_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_1ST_ROLL_MATERIAL").getString());
                rsTmp.updateString("MM_2ND_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_2ND_ROLL_MATERIAL").getString());
                rsTmp.updateString("MM_3RD_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_3RD_ROLL_MATERIAL").getString());
                rsTmp.updateString("MM_4TH_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_4TH_ROLL_MATERIAL").getString());
                rsTmp.updateString("MM_5TH_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_5TH_ROLL_MATERIAL").getString());
                rsTmp.updateString("MM_6TH_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_6TH_ROLL_MATERIAL").getString());
                rsTmp.updateString("MM_7TH_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_7TH_ROLL_MATERIAL").getString());
                rsTmp.updateString("MM_8TH_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_8TH_ROLL_MATERIAL").getString());
                rsTmp.updateString("MM_BATT_GSM",(String)ObjMRItems.getAttribute("MM_BATT_GSM").getString());
                rsTmp.updateString("MM_FIBERS_USED",(String)ObjMRItems.getAttribute("MM_FIBERS_USED").getString());
                rsTmp.updateString("MM_STRETCH",(String)ObjMRItems.getAttribute("MM_STRETCH").getString());
                rsTmp.updateString("MM_MG",(String)ObjMRItems.getAttribute("MM_MG").getString());
                rsTmp.updateString("MM_YANKEE",(String)ObjMRItems.getAttribute("MM_YANKEE").getString());
                rsTmp.updateString("MM_MG_YANKEE_NIP_LOAD",(String)ObjMRItems.getAttribute("MM_MG_YANKEE_NIP_LOAD").getString());
               
                
                rsTmp.updateString("MM_CATEGORY",(String)ObjMRItems.getAttribute("MM_CATEGORY").getString());
                rsTmp.updateString("UC_CODE",(String)ObjMRItems.getAttribute("UC_CODE").getString());
                
                rsTmp.updateString("MM_POSITION_DESIGN_NO",(String)ObjMRItems.getAttribute("MM_POSITION_DESIGN_NO").getString());
                rsTmp.updateString("MM_UPN_NO",(String)ObjMRItems.getAttribute("MM_UPN_NO").getString());
               
                rsTmp.updateString("MM_MIN_CIRCUIT_LENGTH",(String)ObjMRItems.getAttribute("MM_MIN_CIRCUIT_LENGTH").getString());
                rsTmp.updateString("MM_MAX_CIRCUIT_LENGTH",(String)ObjMRItems.getAttribute("MM_MAX_CIRCUIT_LENGTH").getString());
                
                rsTmp.updateString("GOAL",(String)ObjMRItems.getAttribute("GOAL").getString());
                
                //rsTmp.updateString("CREATED_BY",Integer.toString(EITLERPGLOBAL.gNewUserID));
                rsTmp.updateString("MODIFIED_BY", Integer.toString(EITLERPGLOBAL.gNewUserID));
                //rsTmp.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB()+" "+EITLERPGLOBAL.getCurrentTime());
                rsTmp.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                //rsTmp.updateString("MODIFIED_DATE",(String)ObjMRItems.getAttribute("MODIFIED_DATE").getString());
                rsTmp.insertRow();

                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO", RevNo);
                rsHDetail.updateString("MM_DOC_NO", getAttribute("MM_DOC_NO").getString());
                rsHDetail.updateString("SR_NO", (String) ObjMRItems.getAttribute("SR_NO").getString());
                rsHDetail.updateString("MM_AMEND_NO", (String) getAttribute("MM_AMEND_NO").getString());
                rsHDetail.updateString("MM_MACHINE_POSITION", (String) ObjMRItems.getAttribute("MM_MACHINE_POSITION").getString());
                rsHDetail.updateString("MM_MACHINE_POSITION_DESC", (String) ObjMRItems.getAttribute("MM_MACHINE_POSITION_DESC").getString());
                rsHDetail.updateString("MM_COMBINATION_CODE", (String) ObjMRItems.getAttribute("MM_COMBINATION_CODE").getString());
                rsHDetail.updateString("MM_PRESS_TYPE", (String) ObjMRItems.getAttribute("MM_PRESS_TYPE").getString());
                rsHDetail.updateString("MM_PRESS_ROLL_DAI_MM", (String) ObjMRItems.getAttribute("MM_PRESS_ROLL_DAI_MM").getString());
                rsHDetail.updateString("MM_PRESS_ROLL_FACE_TOTAL_MM", (String) ObjMRItems.getAttribute("MM_PRESS_ROLL_FACE_TOTAL_MM").getString());
                rsHDetail.updateString("MM_PRESS_ROLL_FACE_NET_MM", (String) ObjMRItems.getAttribute("MM_PRESS_ROLL_FACE_NET_MM").getString());
                rsHDetail.updateString("MM_FELT_ROLL_WIDTH_MM", (String) ObjMRItems.getAttribute("MM_FELT_ROLL_WIDTH_MM").getString());
                rsHDetail.updateString("MM_PRESS_LOAD", (String) ObjMRItems.getAttribute("MM_PRESS_LOAD").getString());
                rsHDetail.updateString("MM_VACCUM_CAPACITY", (String) ObjMRItems.getAttribute("MM_VACCUM_CAPACITY").getString());
                rsHDetail.updateString("MM_UHLE_BOX", (String) ObjMRItems.getAttribute("MM_UHLE_BOX").getString());
                rsHDetail.updateString("MM_HP_SHOWER", (String) ObjMRItems.getAttribute("MM_HP_SHOWER").getString());
                rsHDetail.updateString("MM_LP_SHOWER", (String) ObjMRItems.getAttribute("MM_LP_SHOWER").getString());
                rsHDetail.updateString("MM_FELT_LENGTH", (String) ObjMRItems.getAttribute("MM_FELT_LENGTH").getString());
                rsHDetail.updateString("MM_FELT_WIDTH", (String) ObjMRItems.getAttribute("MM_FELT_WIDTH").getString());
                rsHDetail.updateString("MM_FELT_GSM", (String) ObjMRItems.getAttribute("MM_FELT_GSM").getString());
                rsHDetail.updateString("MM_FELT_WEIGHT", (String) ObjMRItems.getAttribute("MM_FELT_WEIGHT").getString());
                rsHDetail.updateString("MM_FELT_TYPE", (String) ObjMRItems.getAttribute("MM_FELT_TYPE").getString());
                rsHDetail.updateString("MM_FELT_STYLE", (String) ObjMRItems.getAttribute("MM_FELT_STYLE").getString());
                rsHDetail.updateString("MM_AVG_LIFE", (String) ObjMRItems.getAttribute("MM_AVG_LIFE").getString());
                rsHDetail.updateString("MM_AVG_PRODUCTION", (String) ObjMRItems.getAttribute("MM_AVG_PRODUCTION").getString());
                rsHDetail.updateString("MM_FELT_CONSUMPTION", (String) ObjMRItems.getAttribute("MM_FELT_CONSUMPTION").getString());
                rsHDetail.updateString("MM_DINESH_SHARE", (String) ObjMRItems.getAttribute("MM_DINESH_SHARE").getString());
                rsHDetail.updateString("MM_REMARK_DESIGN", (String) ObjMRItems.getAttribute("MM_REMARK_DESIGN").getString());
                rsHDetail.updateString("MM_REMARK_GENERAL", (String) ObjMRItems.getAttribute("MM_REMARK_GENERAL").getString());
                rsHDetail.updateString("MM_NO_DRYER_CYLINDER", (String) ObjMRItems.getAttribute("MM_NO_DRYER_CYLINDER").getString());
                rsHDetail.updateString("MM_CYLINDER_DIA_MM", (String) ObjMRItems.getAttribute("MM_CYLINDER_DIA_MM").getString());
                rsHDetail.updateString("MM_CYLINDER_FACE_NET_MM", (String) ObjMRItems.getAttribute("MM_CYLINDER_FACE_NET_MM").getString());

                rsHDetail.updateString("MM_FELT_LIFE", (String) ObjMRItems.getAttribute("MM_FELT_LIFE").getString());
                rsHDetail.updateString("MM_TPD", (String) ObjMRItems.getAttribute("MM_TPD").getString());
                rsHDetail.updateString("MM_TOTAL_PRODUCTION", (String) ObjMRItems.getAttribute("MM_TOTAL_PRODUCTION").getString());
                rsHDetail.updateString("MM_PAPER_FELT", (String) ObjMRItems.getAttribute("MM_PAPER_FELT").getString());

                rsHDetail.updateString("MM_DRIVE_TYPE", (String) ObjMRItems.getAttribute("MM_DRIVE_TYPE").getString());
                rsHDetail.updateString("MM_GUIDE_TYPE", (String) ObjMRItems.getAttribute("MM_GUIDE_TYPE").getString());
                rsHDetail.updateString("MM_GUIDE_PAM_TYPE", (String) ObjMRItems.getAttribute("MM_GUIDE_PAM_TYPE").getString());
                rsHDetail.updateString("MM_VENTILATION_TYPE", (String) ObjMRItems.getAttribute("MM_VENTILATION_TYPE").getString());
                rsHDetail.updateString("MM_FABRIC_LENGTH", (String) ObjMRItems.getAttribute("MM_FABRIC_LENGTH").getString());
                rsHDetail.updateString("MM_FABRIC_WIDTH", (String) ObjMRItems.getAttribute("MM_FABRIC_WIDTH").getString());
                rsHDetail.updateString("MM_SIZE_M2", (String) ObjMRItems.getAttribute("MM_SIZE_M2").getString());
                rsHDetail.updateString("MM_SCREEN_TYPE", (String) ObjMRItems.getAttribute("MM_SCREEN_TYPE").getString());
                rsHDetail.updateString("MM_STYLE_DRY", (String) ObjMRItems.getAttribute("MM_STYLE_DRY").getString());
                rsHDetail.updateString("MM_CFM_DRY", (String) ObjMRItems.getAttribute("MM_CFM_DRY").getString());
                rsHDetail.updateString("MM_AVG_LIFE_DRY", (String) ObjMRItems.getAttribute("MM_AVG_LIFE_DRY").getString());
                rsHDetail.updateString("MM_CONSUMPTION_DRY", (String) ObjMRItems.getAttribute("MM_CONSUMPTION_DRY").getString());
                rsHDetail.updateString("MM_DINESH_SHARE_DRY", (String) ObjMRItems.getAttribute("MM_DINESH_SHARE_DRY").getString());
                rsHDetail.updateString("MM_REMARK_DRY", (String) ObjMRItems.getAttribute("MM_REMARK_DRY").getString());
                rsHDetail.updateString("MM_ITEM_CODE", (String) ObjMRItems.getAttribute("MM_ITEM_CODE").getString());
                rsHDetail.updateString("MM_GRUP", (String) ObjMRItems.getAttribute("MM_GRUP").getString());
                rsHDetail.updateString("MM_BASE_GSM", (String) ObjMRItems.getAttribute("MM_BASE_GSM").getString());
                rsHDetail.updateString("MM_WEB_GSM", (String) ObjMRItems.getAttribute("MM_WEB_GSM").getString());
                rsHDetail.updateString("MM_TOTAL_GSM", (String) ObjMRItems.getAttribute("MM_TOTAL_GSM").getString());

                rsHDetail.updateString("MM_POSITION_WISE", (String) ObjMRItems.getAttribute("MM_POSITION_WISE").getString());
                rsHDetail.updateString("MM_HARDNESS",(String)ObjMRItems.getAttribute("MM_HARDNESS").getString());
                rsHDetail.updateString("MM_FELT_WASHING_CHEMICALS",(String)ObjMRItems.getAttribute("MM_FELT_WASHING_CHEMICALS").getString());
                rsHDetail.updateString("MM_VACCUM_IN_UHLE_BOX",(String)ObjMRItems.getAttribute("MM_VACCUM_IN_UHLE_BOX").getString());
                
                rsHDetail.updateString("MM_MACHINE_FLOOR",(String)ObjMRItems.getAttribute("MM_MACHINE_FLOOR").getString());
                rsHDetail.updateString("MM_NUMBER_OF_FORMING_FABRIC",(String)ObjMRItems.getAttribute("MM_NUMBER_OF_FORMING_FABRIC").getString());
                rsHDetail.updateString("MM_TYPE_OF_FORMING_FABRIC",(String)ObjMRItems.getAttribute("MM_TYPE_OF_FORMING_FABRIC").getString());
                rsHDetail.updateString("MM_PRESS_ROLL_FOR_1ST_NIP_FACE_LENGTH",(String)ObjMRItems.getAttribute("MM_PRESS_ROLL_FOR_1ST_NIP_FACE_LENGTH").getString());
                rsHDetail.updateString("MM_PRESS_ROLL_FOR_2ND_NIP_FACE_LENGTH",(String)ObjMRItems.getAttribute("MM_PRESS_ROLL_FOR_2ND_NIP_FACE_LENGTH").getString());
                rsHDetail.updateString("MM_PRESS_ROLL_FOR_3RD_NIP_FACE_LENGTH",(String)ObjMRItems.getAttribute("MM_PRESS_ROLL_FOR_3RD_NIP_FACE_LENGTH").getString());
                rsHDetail.updateString("MM_PRESS_ROLL_FOR_4TH_NIP_FACE_LENGTH",(String)ObjMRItems.getAttribute("MM_PRESS_ROLL_FOR_4TH_NIP_FACE_LENGTH").getString());
                rsHDetail.updateString("MM_WASH_ROLL_SHOWER",(String)ObjMRItems.getAttribute("MM_WASH_ROLL_SHOWER").getString());
                rsHDetail.updateString("MM_HP_SHOWER_NOZZLES",(String)ObjMRItems.getAttribute("MM_HP_SHOWER_NOZZLES").getString());
                rsHDetail.updateString("MM_UHLE_BOX_VACUUM",(String)ObjMRItems.getAttribute("MM_UHLE_BOX_VACUUM").getString());
                rsHDetail.updateString("MM_CHEMICAL_SHOWER",(String)ObjMRItems.getAttribute("MM_CHEMICAL_SHOWER").getString());
                rsHDetail.updateString("MM_1ST_LINEAR_NIP_PRESSURE",(String)ObjMRItems.getAttribute("MM_1ST_LINEAR_NIP_PRESSURE").getString());
                rsHDetail.updateString("MM_2ND_LINEAR_NIP_PRESSURE",(String)ObjMRItems.getAttribute("MM_2ND_LINEAR_NIP_PRESSURE").getString());
                rsHDetail.updateString("MM_3RD_LINEAR_NIP_PRESSURE",(String)ObjMRItems.getAttribute("MM_3RD_LINEAR_NIP_PRESSURE").getString());
                rsHDetail.updateString("MM_4TH_LINEAR_NIP_PRESSURE",(String)ObjMRItems.getAttribute("MM_4TH_LINEAR_NIP_PRESSURE").getString());
                rsHDetail.updateString("MM_LOADING_SYSTEM",(String)ObjMRItems.getAttribute("MM_LOADING_SYSTEM").getString());
                rsHDetail.updateString("MM_LP_SHOWER_NOZZLES",(String)ObjMRItems.getAttribute("MM_LP_SHOWER_NOZZLES").getString());
                rsHDetail.updateString("MM_1ST_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_1ST_ROLL_MATERIAL").getString());
                rsHDetail.updateString("MM_2ND_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_2ND_ROLL_MATERIAL").getString());
                rsHDetail.updateString("MM_3RD_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_3RD_ROLL_MATERIAL").getString());
                rsHDetail.updateString("MM_4TH_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_4TH_ROLL_MATERIAL").getString());
                rsHDetail.updateString("MM_5TH_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_5TH_ROLL_MATERIAL").getString());
                rsHDetail.updateString("MM_6TH_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_6TH_ROLL_MATERIAL").getString());
                rsHDetail.updateString("MM_7TH_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_7TH_ROLL_MATERIAL").getString());
                rsHDetail.updateString("MM_8TH_ROLL_MATERIAL",(String)ObjMRItems.getAttribute("MM_8TH_ROLL_MATERIAL").getString());
                rsHDetail.updateString("MM_BATT_GSM",(String)ObjMRItems.getAttribute("MM_BATT_GSM").getString());
                rsHDetail.updateString("MM_FIBERS_USED",(String)ObjMRItems.getAttribute("MM_FIBERS_USED").getString());
                rsHDetail.updateString("MM_STRETCH",(String)ObjMRItems.getAttribute("MM_STRETCH").getString());
                rsHDetail.updateString("MM_MG",(String)ObjMRItems.getAttribute("MM_MG").getString());
                rsHDetail.updateString("MM_YANKEE",(String)ObjMRItems.getAttribute("MM_YANKEE").getString());
                rsHDetail.updateString("MM_MG_YANKEE_NIP_LOAD",(String)ObjMRItems.getAttribute("MM_MG_YANKEE_NIP_LOAD").getString());
                
                
                rsHDetail.updateString("MM_CATEGORY",(String)ObjMRItems.getAttribute("MM_CATEGORY").getString());
                rsHDetail.updateString("UC_CODE",(String)ObjMRItems.getAttribute("UC_CODE").getString());
                
                rsHDetail.updateString("MM_MIN_CIRCUIT_LENGTH",(String)ObjMRItems.getAttribute("MM_MIN_CIRCUIT_LENGTH").getString());
                rsHDetail.updateString("MM_MAX_CIRCUIT_LENGTH",(String)ObjMRItems.getAttribute("MM_MAX_CIRCUIT_LENGTH").getString());
                
                rsHDetail.updateString("GOAL",(String)ObjMRItems.getAttribute("GOAL").getString());
                
                rsHDetail.updateString("MODIFIED_BY", Integer.toString(EITLERPGLOBAL.gNewUserID));
                //  mdttm=EITLERPGLOBAL.getCurrentDate()+""+EITLERPGLOBAL.getCurrentTime();
                //  rsHDetail.updateString("CREATED_DATE",mdttm);
                rsHDetail.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

                rsHDetail.insertRow();

            }

            //======== Update the Approval Flow =========
            setAttribute("FROM", EITLERPGLOBAL.gUserID);

            //ApprovalFlow ObjFlow=new ApprovalFlow();
            clsFeltProductionApprovalFlow ObjFlow = new clsFeltProductionApprovalFlow();

            //ObjFlow.ModuleID=clsSales_Party.ModuleID;
            ObjFlow.ModuleID = clsmachinesurveyAmend.ModuleID;
            //ObjFlow.DocNo=(String)getAttribute("PARTY_CODE").getObj();
//            ObjFlow.DocNo = (String) getAttribute("MM_DOC_NO").getObj();
            ObjFlow.DocNo = (String) getAttribute("MM_AMEND_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("CREATED_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER";
            ObjFlow.IsCreator = false;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "MM_AMEND_NO";
            //String qry = "UPDATE FELT_DOC_DATA SET DOC_NO='"+getAttribute("PARTY_CODE").getString()+"' WHERE DOC_NO='"+getAttribute("OLD_PARTY_CODE").getString()+"' AND MODULE_ID="+clsSales_Party.ModuleID;
            //String qry = "UPDATE PRODUCTION.FELT_DOC_DATA SET DOC_NO='"+getAttribute("PARTY_CD").getString()+"' WHERE DOC_NO='"+getAttribute("OLD_PARTY_CODE").getString()+"' AND MODULE_ID="+clsOrderParty.ModuleID;
            //data.Execute(qry);

            //==== Handling Rejected Documents ==========//
            AStatus = (String) getAttribute("APPROVAL_STATUS").getObj();
            if (AStatus.equals("R")) {
                //Remove the Rejected Flag First
                //data.Execute("UPDATE D_INV_INDENT_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from FELT_DOC_DATA
                //data.Execute("DELETE FROM FELT_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=3 AND DOC_NO='"+ObjFlow.DocNo+"'");

                //ObjFlow.IsCreator=true;
                ObjFlow.To = (int) getAttribute("SEND_DOC_TO").getVal();
                ObjFlow.ExplicitSendTo = true;
            }
            //==========================================//

            //==== Handling Rejected Documents ==========//
            boolean IsRejected = getAttribute("REJECTED").getBool();

            if (IsRejected) {
                ObjFlow.DocNo = ObjFlow.DocNo.substring(0, 8);
                System.out.println("MM_DOC_NO = " + ObjFlow.DocNo);
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE MM_DOC_NO='" + ObjFlow.DocNo + "' ");

                //Remove Old Records from FELT_DOC_DATA
                data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=" + clsmachinesurveyAmend.ModuleID + " AND DOC_NO='" + ObjFlow.DocNo + "'");

                ObjFlow.IsCreator = true;
            }
            //==========================================//

            if (ObjFlow.Status.equals("H")) {
                //Don't update the Flow if on hold
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

            if (ObjFlow.Status.equals("F")) {

               // ObjFlow.DocNo = ObjFlow.DocNo.substring(0, 8);
                //System.out.println("MM_DOC_NO = " + ObjFlow.DocNo);
                data.Execute("DELETE FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL WHERE MM_DOC_NO='" +getAttribute("MM_DOC_NO").getString()+ "'");
               //data.Execute("DELETE FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL WHERE MM_PARTY_CODE='" + getAttribute("MM_PARTY_CODE").getString() + "' AND MM_MACHINE_NO='"+getAttribute("MM_MACHINE_NO").getString()+"'");
                //data.Execute("UPDATE PRODUCTION.FELT_MACHINE_MASTER_DETAIL SET SR_NO=,MM_DOC_NO='"+getAttribute("MM_DOC_NO").getString()+"',MM_PARTY_CODE='"+getAttribute("MM_PARTY_CODE").getString()+"',MM_MACHINE_NO='"+getAttribute("MM_MACHINE_NO").getString()+"',MM_MACHINE_POSITION=,MM_MACHINE_POSITION_DESC=,MM_COMBINATION_CODE=,MM_PRESS_TYPE=,MM_PRESS_ROLL_DAI_MM=,MM_PRESS_ROLL_FACE_TOTAL_MM=,MM_PRESS_ROLL_FACE_NET_MM=,MM_FELT_ROLL_WIDTH_MM=,MM_PRESS_LOAD=,MM_VACCUM_CAPACITY=,MM_UHLE_BOX=,MM_HP_SHOWER=,MM_LP_SHOWER=,MM_FELT_LENGTH=,MM_FELT_WIDTH=,MM_FELT_GSM=,MM_FELT_WEIGHT=,MM_FELT_TYPE=,MM_FELT_STYLE=,MM_AVG_LIFE=,MM_AVG_PRODUCTION=,MM_FELT_CONSUMPTION=,MM_DINESH_SHARE=,MM_REMARK_DESIGN=,MM_REMARK_GENERAL=,MM_NO_DRYER_CYLINDER=,MM_CYLINDER_DIA_MM=,MM_CYLINDER_FACE_NET_MM=,MM_DRIVE_TYPE=,MM_GUIDE_TYPE=,MM_GUIDE_PAM_TYPE=,MM_VENTILATION_TYPE=,MM_FABRIC_LENGTH=,MM_FABRIC_WIDTH=,MM_SIZE_M2=,MM_SCREEN_TYP=E,MM_STYLE_DRY=,MM_CFM_DRY=,MM_AVG_LIFE_DRY=,MM_CONSUMPTION_DRY=,MM_DINESH_SHARE_DRY=,MM_REMARK_DRY=,MM_ITEM_CODE=,MM_GRUP=,MM_BASE_GSM=,MM_WEB_GSM=,MM_TOTAL_GSM= WHERE MM_PARTY_CODE='"+getAttribute("MM_PARTY_CODE").getString()+"' AND MM_MACHINE_NO='"+getAttribute("MM_MACHINE_NO").getString()+"'");
                //data.Execute("INSERT INTO PRODUCTION.FELT_MACHINE_MASTER_DETAIL(SR_NO,MM_DOC_NO,MM_PARTY_CODE,MM_MACHINE_NO,MM_MACHINE_POSITION,MM_MACHINE_POSITION_DESC,MM_COMBINATION_CODE,MM_PRESS_TYPE,MM_PRESS_ROLL_DAI_MM,MM_PRESS_ROLL_FACE_TOTAL_MM,MM_PRESS_ROLL_FACE_NET_MM,MM_PRESS_LOAD,MM_VACCUM_CAPACITY,MM_UHLE_BOX,MM_HP_SHOWER,MM_LP_SHOWER,MM_FELT_LENGTH,MM_FELT_WIDTH,MM_FELT_GSM,MM_FELT_WEIGHT,MM_FELT_TYPE,MM_FELT_STYLE,MM_AVG_LIFE,MM_AVG_PRODUCTION,MM_FELT_CONSUMPTION,MM_DINESH_SHARE,MM_REMARK_DESIGN,MM_REMARK_GENERAL,MM_NO_DRYER_CYLINDER,MM_CYLINDER_DIA_MM,MM_CYLINDER_FACE_NET_MM,MM_DRIVE_TYPE,MM_GUIDE_TYPE,MM_GUIDE_PAM_TYPE,MM_VENTILATION_TYPE,MM_FABRIC_LENGTH,MM_FABRIC_WIDTH,MM_SIZE_M2,MM_SCREEN_TYPE,MM_STYLE_DRY,MM_CFM_DRY,MM_AVG_LIFE_DRY,MM_CONSUMPTION_DRY,MM_DINESH_SHARE_DRY,MM_REMARK_DRY,MM_ITEM_CODE,MM_GRUP,MM_BASE_GSM,MM_WEB_GSM,MM_TOTAL_GSM)(SELECT SR_NO,MM_DOC_NO,MM_PARTY_CODE,MM_MACHINE_NO,MM_MACHINE_POSITION,MM_MACHINE_POSITION_DESC,MM_COMBINATION_CODE,MM_PRESS_TYPE,MM_PRESS_ROLL_DAI_MM,MM_PRESS_ROLL_FACE_TOTAL_MM,MM_PRESS_ROLL_FACE_NET_MM,MM_PRESS_LOAD,MM_VACCUM_CAPACITY,MM_UHLE_BOX,MM_HP_SHOWER,MM_LP_SHOWER,MM_FELT_LENGTH,MM_FELT_WIDTH,MM_FELT_GSM,MM_FELT_WEIGHT,MM_FELT_TYPE,MM_FELT_STYLE,MM_AVG_LIFE,MM_AVG_PRODUCTION,MM_FELT_CONSUMPTION,MM_DINESH_SHARE,MM_REMARK_DESIGN,MM_REMARK_GENERAL,MM_NO_DRYER_CYLINDER,MM_CYLINDER_DIA_MM,MM_CYLINDER_FACE_NET_MM,MM_DRIVE_TYPE,MM_GUIDE_TYPE,MM_GUIDE_PAM_TYPE,MM_VENTILATION_TYPE,MM_FABRIC_LENGTH,MM_FABRIC_WIDTH,MM_SIZE_M2,MM_SCREEN_TYPE,MM_STYLE_DRY,MM_CFM_DRY,MM_AVG_LIFE_DRY,MM_CONSUMPTION_DRY,MM_DINESH_SHARE_DRY,MM_REMARK_DRY,MM_ITEM_CODE,MM_GRUP,MM_BASE_GSM,MM_WEB_GSM,MM_TOTAL_GSM FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_DETAIL WHERE MM_PARTY_CODE='" + getAttribute("MM_PARTY_CODE").getString() + "' AND MM_MACHINE_NO='" + getAttribute("MM_MACHINE_NO").getString() + "' )");
               //  data.Execute("INSERT INTO PRODUCTION.FELT_MACHINE_MASTER_DETAIL(SR_NO,MM_DOC_NO,MM_PARTY_CODE,MM_MACHINE_NO,MM_MACHINE_POSITION,MM_MACHINE_POSITION_DESC,MM_COMBINATION_CODE,MM_PRESS_TYPE,MM_PRESS_ROLL_DAI_MM,MM_PRESS_ROLL_FACE_TOTAL_MM,MM_PRESS_ROLL_FACE_NET_MM,MM_PRESS_LOAD,MM_VACCUM_CAPACITY,MM_UHLE_BOX,MM_HP_SHOWER,MM_LP_SHOWER,MM_FELT_LENGTH,MM_FELT_WIDTH,MM_FELT_GSM,MM_FELT_WEIGHT,MM_FELT_TYPE,MM_FELT_STYLE,MM_AVG_LIFE,MM_AVG_PRODUCTION,MM_FELT_CONSUMPTION,MM_DINESH_SHARE,MM_REMARK_DESIGN,MM_REMARK_GENERAL,MM_NO_DRYER_CYLINDER,MM_CYLINDER_DIA_MM,MM_CYLINDER_FACE_NET_MM,MM_DRIVE_TYPE,MM_GUIDE_TYPE,MM_GUIDE_PAM_TYPE,MM_VENTILATION_TYPE,MM_FABRIC_LENGTH,MM_FABRIC_WIDTH,MM_SIZE_M2,MM_SCREEN_TYPE,MM_STYLE_DRY,MM_CFM_DRY,MM_AVG_LIFE_DRY,MM_CONSUMPTION_DRY,MM_DINESH_SHARE_DRY,MM_REMARK_DRY,MM_ITEM_CODE,MM_GRUP,MM_BASE_GSM,MM_WEB_GSM,MM_TOTAL_GSM,MM_FELT_LIFE,MM_TPD,MM_TOTAL_PRODUCTION,MM_PAPER_FELT,MM_POSITION_WISE,MM_P_NO_TEMP,MM_M_NO_TEMP,MM_VACCUM_IN_UHLE_BOX,MM_FELT_WASHING_CHEMICALS,MM_HARDNESS,MM_MACHINE_FLOOR,MM_TYPE_OF_FORMING_FABRIC,MM_PRESS_ROLL_FOR_1ST_NIP_FACE_LENGTH,MM_PRESS_ROLL_FOR_2ND_NIP_FACE_LENGTH,MM_PRESS_ROLL_FOR_3RD_NIP_FACE_LENGTH,MM_PRESS_ROLL_FOR_4TH_NIP_FACE_LENGTH,MM_WASH_ROLL_SHOWER,MM_HP_SHOWER_NOZZLES,MM_UHLE_BOX_VACUUM,MM_CHEMICAL_SHOWER,MM_1ST_LINEAR_NIP_PRESSURE,MM_2ND_LINEAR_NIP_PRESSURE,MM_3RD_LINEAR_NIP_PRESSURE,MM_4TH_LINEAR_NIP_PRESSURE,MM_LOADING_SYSTEM,MM_LP_SHOWER_NOZZLES,MM_1ST_ROLL_MATERIAL,MM_2ND_ROLL_MATERIAL,MM_3RD_ROLL_MATERIAL,MM_4TH_ROLL_MATERIAL,MM_5TH_ROLL_MATERIAL,MM_6TH_ROLL_MATERIAL,MM_7TH_ROLL_MATERIAL,MM_8TH_ROLL_MATERIAL,MM_BATT_GSM,MM_FIBERS_USED,MM_STRETCH,MM_MG,MM_YANKEE,MM_MG_YANKEE_NIP_LOAD,MM_NUMBER_OF_FORMING_FABRIC)(SELECT SR_NO,MM_DOC_NO,MM_PARTY_CODE,MM_MACHINE_NO,MM_MACHINE_POSITION,MM_MACHINE_POSITION_DESC,MM_COMBINATION_CODE,MM_PRESS_TYPE,MM_PRESS_ROLL_DAI_MM,MM_PRESS_ROLL_FACE_TOTAL_MM,MM_PRESS_ROLL_FACE_NET_MM,MM_PRESS_LOAD,MM_VACCUM_CAPACITY,MM_UHLE_BOX,MM_HP_SHOWER,MM_LP_SHOWER,MM_FELT_LENGTH,MM_FELT_WIDTH,MM_FELT_GSM,MM_FELT_WEIGHT,MM_FELT_TYPE,MM_FELT_STYLE,MM_AVG_LIFE,MM_AVG_PRODUCTION,MM_FELT_CONSUMPTION,MM_DINESH_SHARE,MM_REMARK_DESIGN,MM_REMARK_GENERAL,MM_NO_DRYER_CYLINDER,MM_CYLINDER_DIA_MM,MM_CYLINDER_FACE_NET_MM,MM_DRIVE_TYPE,MM_GUIDE_TYPE,MM_GUIDE_PAM_TYPE,MM_VENTILATION_TYPE,MM_FABRIC_LENGTH,MM_FABRIC_WIDTH,MM_SIZE_M2,MM_SCREEN_TYPE,MM_STYLE_DRY,MM_CFM_DRY,MM_AVG_LIFE_DRY,MM_CONSUMPTION_DRY,MM_DINESH_SHARE_DRY,MM_REMARK_DRY,MM_ITEM_CODE,MM_GRUP,MM_BASE_GSM,MM_WEB_GSM,MM_TOTAL_GSM,MM_FELT_LIFE,MM_TPD,MM_TOTAL_PRODUCTION,MM_PAPER_FELT,MM_POSITION_WISE,MM_P_NO_TEMP,MM_M_NO_TEMP,MM_VACCUM_IN_UHLE_BOX,MM_FELT_WASHING_CHEMICALS,MM_HARDNESS,,MM_MACHINE_FLOOR,MM_TYPE_OF_FORMING_FABRIC,MM_PRESS_ROLL_FOR_1ST_NIP_FACE_LENGTH,MM_PRESS_ROLL_FOR_2ND_NIP_FACE_LENGTH,MM_PRESS_ROLL_FOR_3RD_NIP_FACE_LENGTH,MM_PRESS_ROLL_FOR_4TH_NIP_FACE_LENGTH,MM_WASH_ROLL_SHOWER,MM_HP_SHOWER_NOZZLES,MM_UHLE_BOX_VACUUM,MM_CHEMICAL_SHOWER,MM_1ST_LINEAR_NIP_PRESSURE,MM_2ND_LINEAR_NIP_PRESSURE,MM_3RD_LINEAR_NIP_PRESSURE,MM_4TH_LINEAR_NIP_PRESSURE,MM_LOADING_SYSTEM,MM_LP_SHOWER_NOZZLES,MM_1ST_ROLL_MATERIAL,MM_2ND_ROLL_MATERIAL,MM_3RD_ROLL_MATERIAL,MM_4TH_ROLL_MATERIAL,MM_5TH_ROLL_MATERIAL,MM_6TH_ROLL_MATERIAL,MM_7TH_ROLL_MATERIAL,MM_8TH_ROLL_MATERIAL,MM_BATT_GSM,MM_FIBERS_USED,MM_STRETCH,MM_MG,MM_YANKEE,MM_MG_YANKEE_NIP_LOAD,MM_NUMBER_OF_FORMING_FABRIC FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_DETAIL WHERE MM_AMEND_NO='" + getAttribute("MM_AMEND_NO").getString() + "' )");
                data.Execute("INSERT INTO PRODUCTION.FELT_MACHINE_MASTER_DETAIL"
                        + "(SR_NO,MM_DOC_NO,MM_PARTY_CODE,MM_MACHINE_NO,MM_MACHINE_POSITION,MM_MACHINE_POSITION_DESC,MM_COMBINATION_CODE,MM_PRESS_TYPE,MM_PRESS_ROLL_DAI_MM,MM_PRESS_ROLL_FACE_TOTAL_MM,MM_PRESS_ROLL_FACE_NET_MM,MM_PRESS_LOAD,MM_VACCUM_CAPACITY,MM_UHLE_BOX,MM_HP_SHOWER,MM_LP_SHOWER,MM_FELT_LENGTH,MM_FELT_WIDTH,MM_FELT_GSM,MM_FELT_WEIGHT,MM_FELT_TYPE,MM_FELT_STYLE,MM_AVG_LIFE,MM_AVG_PRODUCTION,MM_FELT_CONSUMPTION,MM_DINESH_SHARE,MM_REMARK_DESIGN,MM_REMARK_GENERAL,MM_NO_DRYER_CYLINDER,MM_CYLINDER_DIA_MM,MM_CYLINDER_FACE_NET_MM,MM_DRIVE_TYPE,MM_GUIDE_TYPE,MM_GUIDE_PAM_TYPE,MM_VENTILATION_TYPE,MM_FABRIC_LENGTH,MM_FABRIC_WIDTH,MM_SIZE_M2,MM_SCREEN_TYPE,MM_STYLE_DRY,MM_CFM_DRY,MM_AVG_LIFE_DRY,MM_CONSUMPTION_DRY,MM_DINESH_SHARE_DRY,MM_REMARK_DRY,MM_ITEM_CODE,MM_GRUP,MM_BASE_GSM,MM_WEB_GSM,MM_TOTAL_GSM,MM_FELT_LIFE,MM_TPD,MM_TOTAL_PRODUCTION,MM_PAPER_FELT,MM_POSITION_WISE,MM_P_NO_TEMP,MM_M_NO_TEMP,MM_VACCUM_IN_UHLE_BOX,MM_FELT_WASHING_CHEMICALS,MM_HARDNESS,MM_MACHINE_FLOOR,MM_TYPE_OF_FORMING_FABRIC,MM_PRESS_ROLL_FOR_1ST_NIP_FACE_LENGTH,MM_PRESS_ROLL_FOR_2ND_NIP_FACE_LENGTH,MM_PRESS_ROLL_FOR_3RD_NIP_FACE_LENGTH,MM_PRESS_ROLL_FOR_4TH_NIP_FACE_LENGTH,MM_WASH_ROLL_SHOWER,MM_HP_SHOWER_NOZZLES,MM_UHLE_BOX_VACUUM,MM_CHEMICAL_SHOWER,MM_1ST_LINEAR_NIP_PRESSURE,MM_2ND_LINEAR_NIP_PRESSURE,MM_3RD_LINEAR_NIP_PRESSURE,MM_4TH_LINEAR_NIP_PRESSURE,MM_LOADING_SYSTEM,MM_LP_SHOWER_NOZZLES,MM_1ST_ROLL_MATERIAL,MM_2ND_ROLL_MATERIAL,MM_3RD_ROLL_MATERIAL,MM_4TH_ROLL_MATERIAL,MM_5TH_ROLL_MATERIAL,MM_6TH_ROLL_MATERIAL,MM_7TH_ROLL_MATERIAL,MM_8TH_ROLL_MATERIAL,MM_BATT_GSM,MM_FIBERS_USED,MM_STRETCH,MM_MG,MM_YANKEE,MM_MG_YANKEE_NIP_LOAD,MM_NUMBER_OF_FORMING_FABRIC,MM_CATEGORY,UC_CODE,MM_POSITION_DESIGN_NO,MM_UPN_NO,MM_MAX_CIRCUIT_LENGTH,MM_MIN_CIRCUIT_LENGTH,GOAL)"
                 + "(SELECT SR_NO,MM_DOC_NO,MM_PARTY_CODE,MM_MACHINE_NO,MM_MACHINE_POSITION,MM_MACHINE_POSITION_DESC,MM_COMBINATION_CODE,MM_PRESS_TYPE,MM_PRESS_ROLL_DAI_MM,MM_PRESS_ROLL_FACE_TOTAL_MM,MM_PRESS_ROLL_FACE_NET_MM,MM_PRESS_LOAD,MM_VACCUM_CAPACITY,MM_UHLE_BOX,MM_HP_SHOWER,MM_LP_SHOWER,MM_FELT_LENGTH,MM_FELT_WIDTH,MM_FELT_GSM,MM_FELT_WEIGHT,MM_FELT_TYPE,MM_FELT_STYLE,MM_AVG_LIFE,MM_AVG_PRODUCTION,MM_FELT_CONSUMPTION,MM_DINESH_SHARE,MM_REMARK_DESIGN,MM_REMARK_GENERAL,MM_NO_DRYER_CYLINDER,MM_CYLINDER_DIA_MM,MM_CYLINDER_FACE_NET_MM,MM_DRIVE_TYPE,MM_GUIDE_TYPE,MM_GUIDE_PAM_TYPE,MM_VENTILATION_TYPE,MM_FABRIC_LENGTH,MM_FABRIC_WIDTH,MM_SIZE_M2,MM_SCREEN_TYPE,MM_STYLE_DRY,MM_CFM_DRY,MM_AVG_LIFE_DRY,MM_CONSUMPTION_DRY,MM_DINESH_SHARE_DRY,MM_REMARK_DRY,MM_ITEM_CODE,MM_GRUP,MM_BASE_GSM,MM_WEB_GSM,MM_TOTAL_GSM,MM_FELT_LIFE,MM_TPD,MM_TOTAL_PRODUCTION,MM_PAPER_FELT,MM_POSITION_WISE,MM_P_NO_TEMP,MM_M_NO_TEMP,MM_VACCUM_IN_UHLE_BOX,MM_FELT_WASHING_CHEMICALS,MM_HARDNESS,MM_MACHINE_FLOOR,MM_TYPE_OF_FORMING_FABRIC,MM_PRESS_ROLL_FOR_1ST_NIP_FACE_LENGTH,MM_PRESS_ROLL_FOR_2ND_NIP_FACE_LENGTH,MM_PRESS_ROLL_FOR_3RD_NIP_FACE_LENGTH,MM_PRESS_ROLL_FOR_4TH_NIP_FACE_LENGTH,MM_WASH_ROLL_SHOWER,MM_HP_SHOWER_NOZZLES,MM_UHLE_BOX_VACUUM,MM_CHEMICAL_SHOWER,MM_1ST_LINEAR_NIP_PRESSURE,MM_2ND_LINEAR_NIP_PRESSURE,MM_3RD_LINEAR_NIP_PRESSURE,MM_4TH_LINEAR_NIP_PRESSURE,MM_LOADING_SYSTEM,MM_LP_SHOWER_NOZZLES,MM_1ST_ROLL_MATERIAL,MM_2ND_ROLL_MATERIAL,MM_3RD_ROLL_MATERIAL,MM_4TH_ROLL_MATERIAL,MM_5TH_ROLL_MATERIAL,MM_6TH_ROLL_MATERIAL,MM_7TH_ROLL_MATERIAL,MM_8TH_ROLL_MATERIAL,MM_BATT_GSM,MM_FIBERS_USED,MM_STRETCH,MM_MG,MM_YANKEE,MM_MG_YANKEE_NIP_LOAD,MM_NUMBER_OF_FORMING_FABRIC,MM_CATEGORY,UC_CODE,MM_POSITION_DESIGN_NO,MM_UPN_NO,MM_MAX_CIRCUIT_LENGTH,MM_MIN_CIRCUIT_LENGTH,GOAL FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_DETAIL WHERE MM_AMEND_NO='" + getAttribute("MM_AMEND_NO").getString() + "' )");
                
                data.Execute("DELETE FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER WHERE MM_DOC_NO='" +getAttribute("MM_DOC_NO").getString()+ "'");
                
                data.Execute("INSERT INTO PRODUCTION.FELT_MACHINE_MASTER_HEADER(MM_DOC_NO,MM_PARTY_CODE,MM_PARTY_NAME,MM_INCHARGE_NAME,MM_STATION,MM_MACHINE_NO,MM_MACHINE_TYPE_FORMING,MM_PAPER_GRADE,MM_MACHINE_SPEED_RANGE,MM_PAPER_GSM_RANGE,MM_MACHINE_TYPE_PRESSING,MM_FURNISH,MM_TYPE_OF_FILLER,MM_WIRE_DETAIL_1,MM_WIRE_DETAIL_2,MM_WIRE_DETAIL_3,MM_WIRE_DETAIL_4,MM_PAPER_DECKLE_AFTER_WIRE,MM_PAPER_DECKLE_AFTER_PRESS,MM_PAPER_DECKLE_AT_POPE_REEL,MM_DRYER_SECTION,MM_ZONE,MM_CAPACITY,MM_MACHINE_STATUS,MM_ZONE_REPRESENTATIVE,MM_DATE_OF_UPDATE,MM_TOTAL_DRYER_GROUP,MM_UNIRUM_GROUP,MM_CONVENTIONAL_GROUP,MM_HOOD_TYPE,MM_SIZE_PRESS,MM_SIZE_PRESS_POSITION,MM_SHEET_DRYNESS_SIZE_PRESS,MM_DRIVE_TYPE,MM_MACHINE_MAKE,APPROVED,APPROVED_DATE,CANCELED)(SELECT MM_DOC_NO,MM_PARTY_CODE,MM_PARTY_NAME,MM_INCHARGE_NAME,MM_STATION,MM_MACHINE_NO,MM_MACHINE_TYPE_FORMING,MM_PAPER_GRADE,MM_MACHINE_SPEED_RANGE,MM_PAPER_GSM_RANGE,MM_MACHINE_TYPE_PRESSING,MM_FURNISH,MM_TYPE_OF_FILLER,MM_WIRE_DETAIL_1,MM_WIRE_DETAIL_2,MM_WIRE_DETAIL_3,MM_WIRE_DETAIL_4,MM_PAPER_DECKLE_AFTER_WIRE,MM_PAPER_DECKLE_AFTER_PRESS,MM_PAPER_DECKLE_AT_POPE_REEL,MM_DRYER_SECTION,MM_ZONE,MM_CAPACITY,MM_MACHINE_STATUS,MM_ZONE_REPRESENTATIVE,MM_DATE_OF_UPDATE,MM_TOTAL_DRYER_GROUP,MM_UNIRUM_GROUP,MM_CONVENTIONAL_GROUP,MM_HOOD_TYPE,MM_SIZE_PRESS,MM_SIZE_PRESS_POSITION,MM_SHEET_DRYNESS_SIZE_PRESS,MM_DRIVE_TYPE,MM_MACHINE_MAKE,APPROVED,APPROVED_DATE,CANCELED FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER WHERE MM_AMEND_NO='" + getAttribute("MM_AMEND_NO").getString() + "' )");
            }
            //--------- Approval Flow Update complete -----------
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean setData() {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;

        tmpConn = data.getConn();

        long Counter = 0;
        int RevNo = 0;

        try {
            if (HistoryView) {
                RevNo = rsResultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO", rsResultSet.getInt("REVISION_NO"));
            } else {
                setAttribute("REVISION_NO", 0);
            }
            setAttribute("MM_AMEND_NO", rsResultSet.getString("MM_AMEND_NO"));
            setAttribute("MM_DOC_NO", rsResultSet.getString("MM_DOC_NO"));
            setAttribute("MM_PARTY_CODE", rsResultSet.getString("MM_PARTY_CODE"));
            setAttribute("MM_PARTY_NAME", rsResultSet.getString("MM_PARTY_NAME"));
            setAttribute("MM_STATION", rsResultSet.getString("MM_STATION"));
            setAttribute("MM_INCHARGE_NAME", rsResultSet.getString("MM_INCHARGE_NAME"));
            setAttribute("MM_MACHINE_NO", rsResultSet.getString("MM_MACHINE_NO"));
            setAttribute("MM_MACHINE_TYPE_FORMING", rsResultSet.getString("MM_MACHINE_TYPE_FORMING"));
            setAttribute("MM_PAPER_GRADE", rsResultSet.getString("MM_PAPER_GRADE"));
            setAttribute("MM_MACHINE_SPEED_RANGE", rsResultSet.getString("MM_MACHINE_SPEED_RANGE"));
            setAttribute("MM_PAPER_GSM_RANGE", rsResultSet.getString("MM_PAPER_GSM_RANGE"));
            setAttribute("MM_MACHINE_TYPE_PRESSING", rsResultSet.getString("MM_MACHINE_TYPE_PRESSING"));
            setAttribute("MM_FURNISH", rsResultSet.getString("MM_FURNISH"));
            setAttribute("MM_TYPE_OF_FILLER", rsResultSet.getString("MM_TYPE_OF_FILLER"));
            setAttribute("MM_WIRE_DETAIL_1", rsResultSet.getString("MM_WIRE_DETAIL_1"));
            setAttribute("MM_WIRE_DETAIL_2", rsResultSet.getString("MM_WIRE_DETAIL_2"));
            setAttribute("MM_WIRE_DETAIL_3", rsResultSet.getString("MM_WIRE_DETAIL_3"));
            setAttribute("MM_WIRE_DETAIL_4", rsResultSet.getString("MM_WIRE_DETAIL_4"));
            setAttribute("MM_PAPER_DECKLE_AFTER_WIRE", rsResultSet.getString("MM_PAPER_DECKLE_AFTER_WIRE"));
            setAttribute("MM_PAPER_DECKLE_AFTER_PRESS", rsResultSet.getString("MM_PAPER_DECKLE_AFTER_PRESS"));
            setAttribute("MM_PAPER_DECKLE_AT_POPE_REEL", rsResultSet.getString("MM_PAPER_DECKLE_AT_POPE_REEL"));
            setAttribute("MM_DRYER_SECTION", rsResultSet.getString("MM_DRYER_SECTION"));
            setAttribute("MM_ZONE", rsResultSet.getString("MM_ZONE"));
            setAttribute("MM_CAPACITY", rsResultSet.getString("MM_CAPACITY"));
            setAttribute("MM_MACHINE_STATUS", rsResultSet.getString("MM_MACHINE_STATUS"));
            setAttribute("MM_ZONE_REPRESENTATIVE", rsResultSet.getString("MM_ZONE_REPRESENTATIVE"));
            setAttribute("MM_DATE_OF_UPDATE", EITLERPGLOBAL.formatDate(rsResultSet.getString("MM_DATE_OF_UPDATE")));

            setAttribute("MM_TOTAL_DRYER_GROUP", rsResultSet.getString("MM_TOTAL_DRYER_GROUP"));
            setAttribute("MM_UNIRUM_GROUP", rsResultSet.getString("MM_UNIRUM_GROUP"));
            setAttribute("MM_CONVENTIONAL_GROUP", rsResultSet.getString("MM_CONVENTIONAL_GROUP"));
            setAttribute("MM_HOOD_TYPE", rsResultSet.getString("MM_HOOD_TYPE"));
            setAttribute("MM_SIZE_PRESS", rsResultSet.getString("MM_SIZE_PRESS"));
            setAttribute("MM_SIZE_PRESS_POSITION", rsResultSet.getString("MM_SIZE_PRESS_POSITION"));
            setAttribute("MM_SHEET_DRYNESS_SIZE_PRESS", rsResultSet.getString("MM_SHEET_DRYNESS_SIZE_PRESS"));
            setAttribute("MM_DRIVE_TYPE",rsResultSet.getString("MM_DRIVE_TYPE"));
            setAttribute("MM_MACHINE_MAKE",rsResultSet.getString("MM_MACHINE_MAKE"));
            
            setAttribute("HIERARCHY_ID",rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
             setAttribute("APPROVED_DATE",rsResultSet.getString("APPROVED_DATE"));
            /* 
             setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
             setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDateDB(rsResultSet.getString("CREATED_DATE")));
             setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
             setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
             setAttribute("HIERARCHY_ID",rsResultSet.getInt("HIERARCHY_ID"));
             
             setAttribute("REJECTED",rsResultSet.getBoolean("REJECTED"));
             setAttribute("REJECTED_DATE",rsResultSet.getString("REJECTED_DATE"));
             setAttribute("REJECTED_REMARKS",rsResultSet.getString("REJECTED_REMARKS"));
             setAttribute("CHANGED",rsResultSet.getBoolean("CHANGED"));
             setAttribute("CHANGED_DATE",rsResultSet.getString("CHANGED_DATE"));
             setAttribute("CANCELLED",rsResultSet.getInt("CANCELLED"));
             */

            //Now Populate the collection
            //first clear the collection
            colMRItems.clear();
            //String mdocno = (String) getAttribute("MM_DOC_NO").getObj();
            String mAmendno = (String) getAttribute("MM_AMEND_NO").getObj();
            

            tmpStmt = tmpConn.createStatement();
            if (HistoryView) {
                //rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_DETAIL_H WHERE MM_DOC_NO='" + mdocno + "' AND REVISION_NO=" + RevNo + " ORDER BY MM_DOC_NO,SR_NO*1");
                rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_DETAIL_H WHERE MM_AMEND_NO='" + mAmendno + "' AND REVISION_NO=" + RevNo + " ORDER BY MM_DOC_NO,SR_NO*1");
            } else {
                //rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_DETAIL WHERE MM_DOC_NO='" + mdocno + "' ORDER BY MM_DOC_NO,SR_NO*1");
                rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_DETAIL WHERE MM_AMEND_NO='" + mAmendno + "' ORDER BY MM_DOC_NO,SR_NO*1");
            }

            /*if(HistoryView) {
             rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER_H AS AA WHERE MM_DOC_NO='"+mdocno+"' AND REVISION_NO="+RevNo+" ORDER BY AA.MM_DOC_NO");
             }
             else {
             rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER AS AA WHERE MM_DOC_NO='"+mdocno+"' ORDER BY AA.MM_DOC_NO");
             }*/
            rsTmp.first();

            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {
                Counter = Counter + 1;
                clsmachinesurveyitemAmend ObjMRItems = new clsmachinesurveyitemAmend();
                ObjMRItems.setAttribute("MM_AMEND_NO", rsTmp.getString("MM_AMEND_NO"));
                ObjMRItems.setAttribute("SR_NO", rsTmp.getString("SR_NO"));
                //  ObjMRItems.setAttribute("MM_SR_NO",rsTmp.getString("MM_SR_NO"));
                ObjMRItems.setAttribute("MM_DOC_NO", rsTmp.getString("MM_DOC_NO"));
                ObjMRItems.setAttribute("MM_PARTY_CODE", rsTmp.getString("MM_PARTY_CODE"));
                ObjMRItems.setAttribute("MM_MACHINE_NO", rsTmp.getString("MM_MACHINE_NO"));
                ObjMRItems.setAttribute("MM_MACHINE_POSITION", rsTmp.getString("MM_MACHINE_POSITION"));
                ObjMRItems.setAttribute("MM_MACHINE_POSITION_DESC", rsTmp.getString("MM_MACHINE_POSITION_DESC"));
                ObjMRItems.setAttribute("MM_COMBINATION_CODE", rsTmp.getString("MM_COMBINATION_CODE"));
                ObjMRItems.setAttribute("MM_PRESS_TYPE", rsTmp.getString("MM_PRESS_TYPE"));
                ObjMRItems.setAttribute("MM_PRESS_ROLL_DAI_MM", rsTmp.getString("MM_PRESS_ROLL_DAI_MM"));
                ObjMRItems.setAttribute("MM_PRESS_ROLL_FACE_TOTAL_MM", rsTmp.getString("MM_PRESS_ROLL_FACE_TOTAL_MM"));
                ObjMRItems.setAttribute("MM_PRESS_ROLL_FACE_NET_MM", rsTmp.getString("MM_PRESS_ROLL_FACE_NET_MM"));
                ObjMRItems.setAttribute("MM_FELT_ROLL_WIDTH_MM", rsTmp.getString("MM_FELT_ROLL_WIDTH_MM"));
                ObjMRItems.setAttribute("MM_PRESS_LOAD", rsTmp.getString("MM_PRESS_LOAD"));
                ObjMRItems.setAttribute("MM_VACCUM_CAPACITY", rsTmp.getString("MM_VACCUM_CAPACITY"));
                ObjMRItems.setAttribute("MM_UHLE_BOX", rsTmp.getString("MM_UHLE_BOX"));
                ObjMRItems.setAttribute("MM_HP_SHOWER", rsTmp.getString("MM_HP_SHOWER"));
                ObjMRItems.setAttribute("MM_LP_SHOWER", rsTmp.getString("MM_LP_SHOWER"));
                ObjMRItems.setAttribute("MM_FELT_LENGTH", rsTmp.getString("MM_FELT_LENGTH"));
                ObjMRItems.setAttribute("MM_FELT_WIDTH", rsTmp.getString("MM_FELT_WIDTH"));
                ObjMRItems.setAttribute("MM_FELT_GSM", rsTmp.getString("MM_FELT_GSM"));
                ObjMRItems.setAttribute("MM_FELT_WEIGHT", rsTmp.getString("MM_FELT_WEIGHT"));
                ObjMRItems.setAttribute("MM_FELT_TYPE", rsTmp.getString("MM_FELT_TYPE"));
                ObjMRItems.setAttribute("MM_FELT_STYLE", rsTmp.getString("MM_FELT_STYLE"));
                ObjMRItems.setAttribute("MM_AVG_LIFE", rsTmp.getString("MM_AVG_LIFE"));
                ObjMRItems.setAttribute("MM_AVG_PRODUCTION", rsTmp.getString("MM_AVG_PRODUCTION"));
                ObjMRItems.setAttribute("MM_FELT_CONSUMPTION", rsTmp.getString("MM_FELT_CONSUMPTION"));
                ObjMRItems.setAttribute("MM_DINESH_SHARE", rsTmp.getString("MM_DINESH_SHARE"));
                ObjMRItems.setAttribute("MM_REMARK_DESIGN", rsTmp.getString("MM_REMARK_DESIGN"));
                ObjMRItems.setAttribute("MM_REMARK_GENERAL", rsTmp.getString("MM_REMARK_GENERAL"));

                ObjMRItems.setAttribute("MM_NO_DRYER_CYLINDER", rsTmp.getString("MM_NO_DRYER_CYLINDER"));
                ObjMRItems.setAttribute("MM_CYLINDER_DIA_MM", rsTmp.getString("MM_CYLINDER_DIA_MM"));
                ObjMRItems.setAttribute("MM_CYLINDER_FACE_NET_MM", rsTmp.getString("MM_CYLINDER_FACE_NET_MM"));

                ObjMRItems.setAttribute("MM_FELT_LIFE", rsTmp.getString("MM_FELT_LIFE"));
                ObjMRItems.setAttribute("MM_TPD", rsTmp.getString("MM_TPD"));
                ObjMRItems.setAttribute("MM_TOTAL_PRODUCTION", rsTmp.getString("MM_TOTAL_PRODUCTION"));
                ObjMRItems.setAttribute("MM_PAPER_FELT", rsTmp.getString("MM_PAPER_FELT"));

                ObjMRItems.setAttribute("MM_DRIVE_TYPE", rsTmp.getString("MM_DRIVE_TYPE"));
                ObjMRItems.setAttribute("MM_GUIDE_TYPE", rsTmp.getString("MM_GUIDE_TYPE"));
                ObjMRItems.setAttribute("MM_GUIDE_PAM_TYPE", rsTmp.getString("MM_GUIDE_PAM_TYPE"));
                ObjMRItems.setAttribute("MM_VENTILATION_TYPE", rsTmp.getString("MM_VENTILATION_TYPE"));
                ObjMRItems.setAttribute("MM_FABRIC_LENGTH", rsTmp.getString("MM_FABRIC_LENGTH"));
                ObjMRItems.setAttribute("MM_FABRIC_WIDTH", rsTmp.getString("MM_FABRIC_WIDTH"));
                ObjMRItems.setAttribute("MM_SIZE_M2", rsTmp.getString("MM_SIZE_M2"));
                ObjMRItems.setAttribute("MM_SCREEN_TYPE", rsTmp.getString("MM_SCREEN_TYPE"));
                ObjMRItems.setAttribute("MM_STYLE_DRY", rsTmp.getString("MM_STYLE_DRY"));
                ObjMRItems.setAttribute("MM_CFM_DRY", rsTmp.getString("MM_CFM_DRY"));
                ObjMRItems.setAttribute("MM_AVG_LIFE_DRY", rsTmp.getString("MM_AVG_LIFE_DRY"));
                ObjMRItems.setAttribute("MM_CONSUMPTION_DRY", rsTmp.getString("MM_CONSUMPTION_DRY"));
                ObjMRItems.setAttribute("MM_DINESH_SHARE_DRY", rsTmp.getString("MM_DINESH_SHARE_DRY"));
                ObjMRItems.setAttribute("MM_REMARK_DRY", rsTmp.getString("MM_REMARK_DRY"));
                ObjMRItems.setAttribute("MM_ITEM_CODE", rsTmp.getString("MM_ITEM_CODE"));
                ObjMRItems.setAttribute("MM_GRUP", rsTmp.getString("MM_GRUP"));
                ObjMRItems.setAttribute("MM_BASE_GSM", rsTmp.getString("MM_BASE_GSM"));
                ObjMRItems.setAttribute("MM_WEB_GSM", rsTmp.getString("MM_WEB_GSM"));
                ObjMRItems.setAttribute("MM_TOTAL_GSM", rsTmp.getString("MM_TOTAL_GSM"));
                ObjMRItems.setAttribute("MM_POSITION_WISE", rsTmp.getString("MM_POSITION_WISE"));
                ObjMRItems.setAttribute("MM_HARDNESS",rsTmp.getString("MM_HARDNESS"));
                ObjMRItems.setAttribute("MM_FELT_WASHING_CHEMICALS",rsTmp.getString("MM_FELT_WASHING_CHEMICALS"));
                ObjMRItems.setAttribute("MM_VACCUM_IN_UHLE_BOX",rsTmp.getString("MM_VACCUM_IN_UHLE_BOX"));
                
                ObjMRItems.setAttribute("MM_MACHINE_FLOOR",rsTmp.getString("MM_MACHINE_FLOOR"));
                ObjMRItems.setAttribute("MM_NUMBER_OF_FORMING_FABRIC",rsTmp.getString("MM_NUMBER_OF_FORMING_FABRIC"));
                ObjMRItems.setAttribute("MM_TYPE_OF_FORMING_FABRIC",rsTmp.getString("MM_TYPE_OF_FORMING_FABRIC"));
                ObjMRItems.setAttribute("MM_PRESS_ROLL_FOR_1ST_NIP_FACE_LENGTH",rsTmp.getString("MM_PRESS_ROLL_FOR_1ST_NIP_FACE_LENGTH"));
                ObjMRItems.setAttribute("MM_PRESS_ROLL_FOR_2ND_NIP_FACE_LENGTH",rsTmp.getString("MM_PRESS_ROLL_FOR_2ND_NIP_FACE_LENGTH"));
                ObjMRItems.setAttribute("MM_PRESS_ROLL_FOR_3RD_NIP_FACE_LENGTH",rsTmp.getString("MM_PRESS_ROLL_FOR_3RD_NIP_FACE_LENGTH"));
                ObjMRItems.setAttribute("MM_PRESS_ROLL_FOR_4TH_NIP_FACE_LENGTH",rsTmp.getString("MM_PRESS_ROLL_FOR_4TH_NIP_FACE_LENGTH"));
                ObjMRItems.setAttribute("MM_WASH_ROLL_SHOWER",rsTmp.getString("MM_WASH_ROLL_SHOWER"));
                ObjMRItems.setAttribute("MM_HP_SHOWER_NOZZLES",rsTmp.getString("MM_HP_SHOWER_NOZZLES"));
                ObjMRItems.setAttribute("MM_UHLE_BOX_VACUUM",rsTmp.getString("MM_UHLE_BOX_VACUUM"));
                ObjMRItems.setAttribute("MM_CHEMICAL_SHOWER",rsTmp.getString("MM_CHEMICAL_SHOWER"));
                ObjMRItems.setAttribute("MM_1ST_LINEAR_NIP_PRESSURE",rsTmp.getString("MM_1ST_LINEAR_NIP_PRESSURE"));
                ObjMRItems.setAttribute("MM_2ND_LINEAR_NIP_PRESSURE",rsTmp.getString("MM_2ND_LINEAR_NIP_PRESSURE"));
                ObjMRItems.setAttribute("MM_3RD_LINEAR_NIP_PRESSURE",rsTmp.getString("MM_3RD_LINEAR_NIP_PRESSURE"));
                ObjMRItems.setAttribute("MM_4TH_LINEAR_NIP_PRESSURE",rsTmp.getString("MM_4TH_LINEAR_NIP_PRESSURE"));
                ObjMRItems.setAttribute("MM_LOADING_SYSTEM",rsTmp.getString("MM_LOADING_SYSTEM"));
                ObjMRItems.setAttribute("MM_LP_SHOWER_NOZZLES",rsTmp.getString("MM_LP_SHOWER_NOZZLES"));
                ObjMRItems.setAttribute("MM_1ST_ROLL_MATERIAL",rsTmp.getString("MM_1ST_ROLL_MATERIAL"));
                ObjMRItems.setAttribute("MM_2ND_ROLL_MATERIAL",rsTmp.getString("MM_2ND_ROLL_MATERIAL"));
                ObjMRItems.setAttribute("MM_3RD_ROLL_MATERIAL",rsTmp.getString("MM_3RD_ROLL_MATERIAL"));
                ObjMRItems.setAttribute("MM_4TH_ROLL_MATERIAL",rsTmp.getString("MM_4TH_ROLL_MATERIAL"));
                ObjMRItems.setAttribute("MM_5TH_ROLL_MATERIAL",rsTmp.getString("MM_5TH_ROLL_MATERIAL"));
                ObjMRItems.setAttribute("MM_6TH_ROLL_MATERIAL",rsTmp.getString("MM_6TH_ROLL_MATERIAL"));
                ObjMRItems.setAttribute("MM_7TH_ROLL_MATERIAL",rsTmp.getString("MM_7TH_ROLL_MATERIAL"));
                ObjMRItems.setAttribute("MM_8TH_ROLL_MATERIAL",rsTmp.getString("MM_8TH_ROLL_MATERIAL"));
                ObjMRItems.setAttribute("MM_BATT_GSM",rsTmp.getString("MM_BATT_GSM"));
                ObjMRItems.setAttribute("MM_FIBERS_USED",rsTmp.getString("MM_FIBERS_USED"));
                ObjMRItems.setAttribute("MM_STRETCH",rsTmp.getString("MM_STRETCH"));
                ObjMRItems.setAttribute("MM_MG",rsTmp.getString("MM_MG"));
                ObjMRItems.setAttribute("MM_YANKEE",rsTmp.getString("MM_YANKEE"));
                ObjMRItems.setAttribute("MM_MG_YANKEE_NIP_LOAD",rsTmp.getString("MM_MG_YANKEE_NIP_LOAD"));
                
                ObjMRItems.setAttribute("MM_CATEGORY",rsTmp.getString("MM_CATEGORY"));
                ObjMRItems.setAttribute("UC_CODE",rsTmp.getString("UC_CODE"));
                
                ObjMRItems.setAttribute("MM_POSITION_DESIGN_NO",rsTmp.getString("MM_POSITION_DESIGN_NO"));
                ObjMRItems.setAttribute("MM_UPN_NO",rsTmp.getString("MM_UPN_NO"));
                
                //MM_MAX_CIRCUIT_LENGTH
                ObjMRItems.setAttribute("MM_MIN_CIRCUIT_LENGTH",rsTmp.getString("MM_MIN_CIRCUIT_LENGTH"));
                ObjMRItems.setAttribute("MM_MAX_CIRCUIT_LENGTH",rsTmp.getString("MM_MAX_CIRCUIT_LENGTH"));
                
                ObjMRItems.setAttribute("GOAL",rsTmp.getString("GOAL"));
                
                ObjMRItems.setAttribute("CREATED_BY", rsTmp.getLong("CREATED_BY"));
                ObjMRItems.setAttribute("CREATED_DATE", rsTmp.getString("CREATED_DATE"));
                ObjMRItems.setAttribute("MODIFIED_BY", rsTmp.getLong("MODIFIED_BY"));
                ObjMRItems.setAttribute("MODIFIED_DATE", rsTmp.getString("MODIFIED_DATE"));
                colMRItems.put(Long.toString(Counter), ObjMRItems);
                rsTmp.next();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }

    //This routine checks and returns whether the item is editable or not
    public boolean IsEditable(int pCompanyID, String pDocNo, int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";

        try {
            if (HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER WHERE MM_DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=725 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND STATUS='W'";
                tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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
            return false;
        }

    }

    public static HashMap getHistoryList(int CompanyID, String Docno) {
        HashMap List = new HashMap();
        ResultSet rsTmp;

        try {
            String strSQL = "SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER_H WHERE MM_AMEND_NO='" + Docno + "'";
            rsTmp = data.getResult(strSQL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                while (!rsTmp.isAfterLast()) {
                    clsmachinesurveyAmend objParty = new clsmachinesurveyAmend();

                    objParty.setAttribute("MM_DOC_NO", rsTmp.getString("MM_DOC_NO"));
                    //objParty.setAttribute("PROFORMA_DATE",rsTmp.getString("PROFORMA_DATE"));
                    objParty.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                    objParty.setAttribute("UPDATE_BY", rsTmp.getInt("UPDATE_BY"));
                    objParty.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                    objParty.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                    objParty.setAttribute("APPROVER_REMARKS", rsTmp.getString("APPROVER_REMARKS"));
                    objParty.setAttribute("MM_AMEND_NO", rsTmp.getString("MM_AMEND_NO"));

                    List.put(Integer.toString(List.size() + 1), objParty);
                    rsTmp.next();
                }
            }
            rsTmp.close();
            return List;
        } catch (Exception e) {
            return List;
        }
    }

    public boolean Abc(String Condition1) {
        System.out.println(Condition1);
        return true;
    }

    public boolean Filter(String Condition) {
        Ready = false;
        try {
            String strSQL = "";
           // strSQL += "SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER AS AA LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME,DISPATCH_STATION FROM DINESHMILLS.D_SAL_PARTY_MASTER) AS BB ON AA.MM_PARTY_CODE=BB.PARTY_CODE WHERE " + Condition;
            strSQL += "SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER  WHERE " + Condition;
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            System.out.println("QUery on MMMaster : " + strSQL);
            rsResultSet = Stmt.executeQuery(strSQL);

            if (rsResultSet.first()) {
                Ready = true;
                MoveLast();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    //This routine checks and returns whether the item is deletable or not
    //Criteria is Approved item cannot be deleted
    //and if not approved then user id is checked whether doucment
    //is created by the user. Only creator can delete the document
    public boolean CanDelete(int pCompanyID, String pDocNo, int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";

        try {
            if (HistoryView) {
                return false;
            }

            //First check that document is approved or not
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER WHERE MM_AMEND_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=725 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND TYPE='C' AND STATUS='W'";
                tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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
            return false;
        }

    }

    //Deletes current record
    public boolean Delete(int pUserID) {
        /*try {
         int lCompanyID=EITLERPGLOBAL.gCompanyID;
         String lDocNo=(String)getAttribute("PROFORMA_NO").getObj();
         String strSQL="";
         
         //First check that record is editable
         if(CanDelete(lCompanyID,lDocNo,pUserID)) {
         String strQry = "DELETE FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER WHERE PROFORMA_NO='"+lDocNo+"'";
         data.Execute(strQry);
         strQry = "DELETE FROM PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL WHERE PROFORMA_NO='"+lDocNo+"'";
         data.Execute(strQry);
         
         LoadData(lCompanyID);
         return true;
         }
         else {
         LastError="Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
         return false;
         }
         }
         catch(Exception e) {
         LastError = e.getMessage();
         return false;
         }*/
        return false;
    }

    public Object getObject(int CompanyID, String PartyCode) {
        String strCondition = " WHERE MM_PARTY_CODE='" + PartyCode + "' ";
        clsSales_Party objParty = new clsSales_Party();
        objParty.Filter(strCondition);
        return objParty;
    }

    public static void CancelParty(int pCompanyID, String pPartyCode, String MainCode) {
        ResultSet rsTmp = null;

        if (CanCancelParty(pCompanyID, pPartyCode, MainCode)) {
            boolean Approved = false;
            try {
                rsTmp = data.getResult("SELECT APPROVED FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID=" + pCompanyID + " AND PARTY_CODE='" + pPartyCode + "' AND MAIN_ACCOUNT_CODE='" + MainCode + "' ");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    Approved = rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();

                /*if(!Approved) {
                 data.Execute("DELETE FROM FELT_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pPartyCode+"' AND MODULE_ID="+clsSales_Party.ModuleID);
                 }*/
                data.Execute("UPDATE D_SAL_PARTY_MASTER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + pCompanyID + " AND PARTY_CODE='" + pPartyCode + "' AND MAIN_ACCOUNT_CODE='" + MainCode + "' ");
                data.Execute("DELETE FROM FELT_DOC_DATA WHERE COMPANY_ID=" + pCompanyID + " AND DOC_NO='" + pPartyCode + "' ");
            } catch (Exception e) {
            }
        }
    }

    public static boolean CanCancelParty(int pCompanyID, String pPartyCode, String MainCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean CanCancel = false;

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            rsTmp = stTmp.executeQuery("SELECT PARTY_CODE FROM D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + pPartyCode + "' AND MAIN_ACCOUNT_CODE='" + MainCode + "' AND COMPANY_ID=" + pCompanyID + " AND APPROVED=0 AND CANCELLED=0");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                CanCancel = true;
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

        } catch (Exception e) {

        }

        return CanCancel;
    }

    public static HashMap getPendingApprovals(int pUserID, int pOrder) {
        String strSQL = "";

        HashMap List = new HashMap();
        long Counter = 0;

        try {

            if (pOrder == EITLERPGLOBAL.OnRecivedDate) {
                //strSQL = "SELECT DISTINCT(PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_DOC_NO),MM_PARTY_CODE,RECEIVED_DATE FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + pUserID + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=725 ORDER BY PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE";
                strSQL = "SELECT DISTINCT(PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_AMEND_NO),MM_PARTY_CODE,RECEIVED_DATE FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_AMEND_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + pUserID + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=725 ORDER BY PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                //strSQL = "SELECT DISTINCT(PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_DOC_NO),MM_PARTY_CODE,RECEIVED_DATE FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + pUserID + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=725 ORDER BY PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.CREATED_DATE";
                strSQL = "SELECT DISTINCT(PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_AMEND_NO),MM_PARTY_CODE,RECEIVED_DATE FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_AMEND_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + pUserID + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=725 ORDER BY PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.CREATED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                //strSQL = "SELECT DISTINCT(PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_DOC_NO),MM_PARTY_CODE,RECEIVED_DATE FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + pUserID + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=725 ORDER BY PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_DOC_NO";
                strSQL = "SELECT DISTINCT(PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_AMEND_NO),MM_PARTY_CODE,RECEIVED_DATE FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_AMEND_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + pUserID + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=725 ORDER BY PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_AMEND_NO";
            }

            ResultSet rsTmp = data.getConn().createStatement().executeQuery(strSQL);

            Counter = 0;
            while (rsTmp.next()) {

                Counter = Counter + 1;
                clsmachinesurveyAmend ObjDoc = new clsmachinesurveyAmend();

                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("MM_DOC_NO", rsTmp.getString("MM_AMEND_NO"));
                ObjDoc.setAttribute("MM_PARTY_CODE", rsTmp.getString("MM_PARTY_CODE"));
                ObjDoc.setAttribute("RECEIVED_DATE", rsTmp.getString("RECEIVED_DATE"));
                // ----------------- End of Header Fields ------------------------------------//

                List.put(Long.toString(Counter), ObjDoc);
            }
            rsTmp.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return List;
    }

//    public static HashMap getPendingApprovals(int pCompanyID, int pUserID, int pOrder) {
//        String strSQL = "";
//        Connection tmpConn;
//        tmpConn = data.getConn();
//
//        ResultSet rsTmp;
//        Statement tmpStmt;
//
//        HashMap List = new HashMap();
//        long Counter = 0;
//
//        try {
//            if (pOrder == EITLERPGLOBAL.OnRecivedDate) {
//                strSQL = "SELECT PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_DOC_NO,PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.CREATED_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=725 ORDER BY PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE";
//            }
//
//            if (pOrder == EITLERPGLOBAL.OnDocDate) {
//                strSQL = "SELECT PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_DOC_NO,PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.CREATED_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=725 ORDER BY PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.CREATED_DATE";
//            }
//
//            if (pOrder == EITLERPGLOBAL.OnDocNo) {
//                strSQL = "SELECT PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_DOC_NO,PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.CREATED_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=725 ORDER BY PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_DOC_NO";
//            }
//
//            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
//            rsTmp = tmpStmt.executeQuery(strSQL);
//
//            rsTmp.first();
//            Counter = 0;
//            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {
//
//                Counter = Counter + 1;
//                //clsSales_Party ObjItem=new clsSales_Party();
//                clsmachinesurveyAmend ObjItem = new clsmachinesurveyAmend();
//
//                //------------- Header Fields --------------------//
//                ObjItem.setAttribute("MM_DOC_NO", rsTmp.getString("MM_DOC_NO"));
//                ObjItem.setAttribute("CREATED_DATE", rsTmp.getString("CREATED_DATE"));
//                ObjItem.setAttribute("RECEIVED_DATE", rsTmp.getString("RECEIVED_DATE"));
//                ObjItem.setAttribute("DEPT_ID", rsTmp.getInt("DEPT_ID"));
//                // ----------------- End of Header Fields ------------------------------------//
//
//                //Put the prepared user object into list
//                List.put(Long.toString(Counter), ObjItem);
//
//                if (!rsTmp.isAfterLast()) {
//                    rsTmp.next();
//                }
//            }//Out While
//
//            //tmpConn.close();
//            rsTmp.close();
//            tmpStmt.close();
//
//        } catch (Exception e) {
//        }
//
//        return List;
//    }

    public boolean ShowHistory(int pCompanyID, String pdocno) {
        Ready = false;
        try {
            //Conn=data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsResultSet = Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER_H WHERE MM_AMEND_NO='" + pdocno + "' ORDER BY REVISION_NO");
            Ready = true;
            HistoryView = true;
            MoveFirst();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public static boolean IsPartyExistEx(int pCompanyID, String pPartyCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT PARTY_NAME FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID=" + pCompanyID + " AND PARTY_CODE='" + pPartyCode + "'");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                //tmpConn.close();
                stTmp.close();
                rsTmp.close();

                return true;
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean IsPartyExist(int pCompanyID, String pPartyCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT PARTY_NAME FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID=" + pCompanyID + " AND PARTY_CODE='" + pPartyCode + "' AND BLOCKED='N' ");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                //tmpConn.close();
                stTmp.close();
                rsTmp.close();

                return true;
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getPartyName(int pCompanyID, String pPartyCode, String pURL) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName = "";

        try {
            tmpConn = data.getConn(pURL);
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT PARTY_NAME FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID=" + pCompanyID + " AND PARTY_CODE='" + pPartyCode + "' ");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                PartyName = rsTmp.getString("PARTY_NAME");
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

            return PartyName;
        } catch (Exception e) {
            return "";
        }
    }

    public static double ConvertToNextThousand(double pamount) {
        double returnamt = 0;
        if (pamount <= 1000) {
            return pamount + (1000 - pamount);
        } else {
            if (pamount % 1000 == 0) {
                return pamount;
            } else {
                return pamount + (1000 - (pamount % 1000));
            }
        }

    }

    public static String[] getPiecedetail(String pPartyCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String[] Piecedetail = new String[40];
        String[] error = {"error"};

        try {

            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String strSQL = "SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER AS AA LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME,DISPATCH_STATION FROM DINESHMILLS.D_SAL_PARTY_MASTER) AS BB ON AA.MM_PARTY_CODE=BB.PARTY_CODE LEFT JOIN (SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MST) AS B ON AA.MM_POSITION = B.POSITION_NO WHERE ";
            if (!pPartyCode.equals("")) {
                strSQL += "AA.MM_PARTY_CODE='" + pPartyCode + "'";
            }
            System.out.println("Piece Detail Query :");
            System.out.println(strSQL);
            rsTmp = stTmp.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                Piecedetail[0] = rsTmp.getString("MM_PARTY_CODE");
                Piecedetail[1] = rsTmp.getString("MM_MACHINE_NO");
                Piecedetail[2] = rsTmp.getString("MM_POSITION");
                Piecedetail[3] = rsTmp.getString("MM_POSITION_DESC");
                Piecedetail[4] = rsTmp.getString("MM_COMBINATION_CODE");
                Piecedetail[5] = rsTmp.getString("MM_ORDER_LENGTH");
                Piecedetail[6] = rsTmp.getString("MM_ORDER_WIDTH");
                Piecedetail[7] = rsTmp.getString("MM_ORDER_SIZE");
                Piecedetail[8] = rsTmp.getString("MM_PRESS_TYPE");
                Piecedetail[9] = rsTmp.getString("MM_GSM_RANGE");
                Piecedetail[10] = rsTmp.getString("MM_MAX_FELT_LENGTH");
                Piecedetail[11] = rsTmp.getString("MM_MIN_FELT_LENGTH");
                Piecedetail[12] = rsTmp.getString("MM_LINEAR_NIP_LOAD");
                Piecedetail[13] = rsTmp.getString("MM_PAPERGRADE_CODE");
                Piecedetail[14] = rsTmp.getString("MM_PAPERGRADE_DESC");
                Piecedetail[15] = rsTmp.getString("MM_FURNISH");
                Piecedetail[17] = rsTmp.getString("MM_TYPE");
                Piecedetail[18] = rsTmp.getString("MM_SPEED");
                Piecedetail[19] = rsTmp.getString("MM_SURVEY_DATE");
                Piecedetail[20] = rsTmp.getString("MM_WIRE_LENGTH");
                Piecedetail[21] = rsTmp.getString("MM_WIRE_WIDTH");
                Piecedetail[22] = rsTmp.getString("MM_WIRE_TYPE");
                Piecedetail[23] = rsTmp.getString("MM_TECH_REP");
                Piecedetail[24] = rsTmp.getString("MM_TYPE_OF_FILLER");
                Piecedetail[25] = rsTmp.getString("MM_PAPER_DECKLE");
                Piecedetail[26] = rsTmp.getString("MM_MCH_ACTIVE");
                Piecedetail[27] = rsTmp.getString("MM_LIFE_OF_FELT");
                Piecedetail[28] = rsTmp.getString("MM_CONSUMPTION");
                Piecedetail[29] = rsTmp.getString("MM_DINESH_SHARE");

                //Piecedetail[27]=rsTmp.getString("CREATED_BY");
                //Piecedetail[28]=EITLERPGLOBAL.formatDate(rsTmp.getString("CREATED_DATE"));
                //Piecedetail[28]=rsTmp.getString("MODIFIED_BY");
                //Piecedetail[29]=EITLERPGLOBAL.formatDate(rsTmp.getString("MODIFIED_DATE"));
                Piecedetail[30] = rsTmp.getString("MM_PAPERGRADE");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            return Piecedetail;

        } catch (Exception e) {
            e.printStackTrace();
            return error;
        }
    }

    public static String getPartyName(int pCompanyID, String pPartyCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT PARTY_NAME FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID=" + pCompanyID + " AND PARTY_CODE='" + pPartyCode + "' ");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                PartyName = rsTmp.getString("PARTY_NAME");
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

            return PartyName;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getpositiondesc(String pposition) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String positiondesc = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT POSITION_DESC FROM PRODUCTION.FELT_MACHINE_POSITION_MST WHERE POSITION_NO='" + pposition + "' ");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                positiondesc = rsTmp.getString("POSITION_DESC");
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

            return positiondesc;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getpressStyle(String pposition) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String pressStyle = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                pressStyle = rsTmp.getString("PARA_DESC");
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

            return pressStyle;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getcombinationcode(String pmachine, String pposition) {
        try {
            String mcmbcode = "";
            int mmachine = 100 + (Integer.parseInt(pmachine));
            int mposition = 100 + (Integer.parseInt(pposition));
            mcmbcode = (Integer.toString(mposition).substring(1, 3)) + "" + (Integer.toString(mmachine).substring(1, 3));
            return mcmbcode;
        } catch (Exception e) {
            return "";
        }

    }

    public static HashMap getList(String pCondition) {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;

        HashMap List = new HashMap();
        long Counter = 0;
        long Counter2 = 0;

        tmpConn = data.getConn();

        try {
            tmpStmt = tmpConn.createStatement();

            rsTmp = tmpStmt.executeQuery("SELECT * FROM D_SAL_PARTY_MASTER " + pCondition);

            Counter = 0;
            while (rsTmp.next()) {
                Counter = Counter + 1;
                clsSales_Party ObjParty = new clsSales_Party();
                ObjParty.setAttribute("COMPANY_ID", rsTmp.getInt("COMPANY_ID"));
                ObjParty.setAttribute("PARTY_CODE", rsTmp.getString("PARTY_CODE"));
                //ObjParty.setAttribute("PARTY_TYPE",rsTmp.getInt("PARTY_TYPE"));
                ObjParty.setAttribute("PARTY_NAME", rsTmp.getString("PARTY_NAME"));
                ObjParty.setAttribute("HIERARCHY_ID", rsTmp.getLong("HIERARCHY_ID"));

            }//Outer while

            rsTmp.close();
            //tmpConn.close();
            tmpStmt.close();

        } catch (Exception e) {
            //LastError = e.getMessage();
        }

        return List;
    }

    public static Object getObjectEx(int pCompanyID, String pPartyCode) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND PARTY_CODE='" + pPartyCode + "' ";
        clsSales_Party ObjParty = new clsSales_Party();
        ObjParty.LoadData(pCompanyID);
        ObjParty.Filter(strCondition);
        return ObjParty;
    }

    public static Object getObjectExN(int pCompanyID, String pPartyCode, String MainCode) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND PARTY_CODE='" + pPartyCode + "' AND MAIN_ACCOUNT_CODE=" + MainCode;
        clsSales_Party ObjParty = new clsSales_Party();
        ObjParty.LoadData(pCompanyID);
        ObjParty.Filter(strCondition);
        return ObjParty;
    }

    public static boolean deleteParty(String PartyCode) {
        try {
            //data.Execute("DELETE FROM PRODUCTION.FELT_DOC_DATA WHERE DOC_NO='"+PartyCode+"' AND MODULE_ID='"+clsOrderParty.ModuleID+"' ");
            //data.Execute("DELETE FROM PRODUCTION.FELT_DOC_DATA WHERE PARTY_CODE='"+PartyCode+"' ");
            //data.Execute("DELETE FROM PRODUCTION.FELT_DOC_DATA WHERE PARTY_CODE='"+PartyCode+"' ");
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String getSeasonCode() {
        String SeasonCode = "";
        try {
            SeasonCode = data.getStringValueFromDB("SELECT SEASON_ID FROM D_SAL_SEASON_MASTER WHERE CURDATE() BETWEEN DATE_FROM AND DATE_TO");
        } catch (Exception e) {
            return SeasonCode;
        }
        return SeasonCode;
    }

    /*
     public static boolean getBackup(String PartyCode) {
     Connection tmpConn = null;
     Statement tmpStmt = null;
     ResultSet rsTmp = null;
     int SrNo = 0;
     try {
     tmpConn=data.getConn();
     tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
     rsTmp=tmpStmt.executeQuery("SELECT * FROM D_SAL_PARTY_MASTER_BKUP ORDER BY PARTY_CODE");
     clsSales_Party tmpParty = (clsSales_Party)clsSales_Party.getObjectExN(EITLERPGLOBAL.gCompanyID, PartyCode,"210027");
     SrNo = data.getIntValueFromDB("SELECT MAX(SR_NO) FROM D_SAL_PARTY_MASTER_BKUP")+1;
     rsTmp.first();
     rsTmp.moveToInsertRow();
     rsTmp.updateInt("SR_NO", SrNo);
     rsTmp.updateInt("COMPANY_ID", tmpParty.getAttribute("COMAPNY_ID").getInt());
     rsTmp.updateString("PARTY_CODE", tmpParty.getAttribute("PARTY_CODE").getString());
     rsTmp.updateInt("PARTY_TYPE", tmpParty.getAttribute("PARTY_TYPE").getInt());
     rsTmp.updateString("PARENT_PARTY_CODE", " ");
     rsTmp.updateString("PARTY_NAME",tmpParty.getAttribute("PARTY_NAME").getString());
    
     rsTmp.updateString("SEASON_CODE",tmpParty.getAttribute("SEASON_CODE").getString());
     rsTmp.updateString("REG_DATE",tmpParty.getAttribute("REG_DATE").getString());
    
     rsTmp.updateString("AREA_ID",tmpParty.getAttribute("AREA_ID").getString());
     rsTmp.updateString("ADDRESS1",tmpParty.getAttribute("ADDRESS1").getString());
     rsTmp.updateString("ADDRESS2",tmpParty.getAttribute("ADDRESS2").getString());
     rsTmp.updateString("CITY_ID",tmpParty.getAttribute("CITY_ID").getString());
     rsTmp.updateString("CITY_NAME",tmpParty.getAttribute("CITY_NAME").getString());
     rsTmp.updateString("DISPATCH_STATION",tmpParty.getAttribute("DISPATCH_STATION").getString());
     rsTmp.updateString("DISTRICT",tmpParty.getAttribute("DISTRICT").getString());
     rsTmp.updateString("PINCODE",tmpParty.getAttribute("PINCODE").getString());
     rsTmp.updateString("PHONE_NO",tmpParty.getAttribute("PHONE_NO").getString());
     rsTmp.updateString("MOBILE_NO",tmpParty.getAttribute("MOBILE_NO").getString());
     rsTmp.updateString("EMAIL",tmpParty.getAttribute("EMAIL").getString());
     rsTmp.updateString("URL",tmpParty.getAttribute("URL").getString());
     rsTmp.updateString("CONTACT_PERSON",tmpParty.getAttribute("CONTACT_PERSON").getString());
     rsTmp.updateInt("BANK_ID",tmpParty.getAttribute("BANK_ID").getInt());
     rsTmp.updateString("BANK_NAME",tmpParty.getAttribute("BANK_NAME").getString());
     rsTmp.updateString("BANK_ADDRESS",tmpParty.getAttribute("BANK_ADDRESS").getString());
     rsTmp.updateString("BANK_CITY",tmpParty.getAttribute("BANK_CITY").getString());
     rsTmp.updateString("CHARGE_CODE",tmpParty.getAttribute("CHARGE_CODE").getString());
     rsTmp.updateString("CHARGE_CODE_II",tmpParty.getAttribute("CHARGE_CODE_II").getString());
     rsTmp.updateDouble("CREDIT_DAYS",tmpParty.getAttribute("CREDIT_DAYS").getDouble());
     rsTmp.updateString("DOCUMENT_THROUGH",tmpParty.getAttribute("DOCUMENT_THROUGH").getString());
     rsTmp.updateInt("TRANSPORTER_ID",tmpParty.getAttribute("TRANSPORTER_ID").getInt());
     rsTmp.updateString("TRANSPORTER_NAME",tmpParty.getAttribute("TRANSPORTER_NAME").getString());
     rsTmp.updateDouble("AMOUNT_LIMIT",tmpParty.getAttribute("AMOUNT_LIMIT").getDouble());
     rsTmp.updateString("CST_NO",tmpParty.getAttribute("CST_NO").getString());
     rsTmp.updateString("CST_DATE",tmpParty.getAttribute("CST_DATE").getString());
     rsTmp.updateString("ECC_NO",tmpParty.getAttribute("ECC_NO").getString());
     rsTmp.updateString("ECC_DATE",tmpParty.getAttribute("ECC_DATE").getString());
     rsTmp.updateString("TIN_NO",tmpParty.getAttribute("TIN_NO").getString());
     rsTmp.updateString("TIN_DATE",tmpParty.getAttribute("TIN_DATE").getString());
     rsTmp.updateString("PAN_NO",tmpParty.getAttribute("PAN_NO").getString());
     rsTmp.updateString("PAN_DATE",tmpParty.getAttribute("PAN_DATE").getString());
     rsTmp.updateString("MAIN_ACCOUNT_CODE",tmpParty.getAttribute("MAIN_ACCOUNT_CODE").getString());
     rsTmp.updateString("CATEGORY",tmpParty.getAttribute("CATEGORY").getString());
     rsTmp.updateInt("OTHER_BANK_ID",tmpParty.getAttribute("OTHER_BANK_ID").getInt());
     rsTmp.updateString("OTHER_BANK_NAME",tmpParty.getAttribute("OTHER_BANK_NAME").getString());
     rsTmp.updateString("OTHER_BANK_ADDRESS",tmpParty.getAttribute("OTHER_BANK_ADDRESS").getString());
     rsTmp.updateString("OTHER_BANK_CITY",tmpParty.getAttribute("OTHER_BANK_CITY").getString());
     rsTmp.updateString("INSURANCE_CODE",tmpParty.getAttribute("INSURANCE_CODE").getString());
     rsTmp.updateString("REMARKS",tmpParty.getAttribute("REMARKS").getString());
     rsTmp.updateDouble("DELAY_INTEREST_PER",tmpParty.getAttribute("DELAY_INTEREST_PER").getDouble());
     rsTmp.updateString("ACCOUNT_CODES",tmpParty.getAttribute("ACCOUNT_CODES").getString());
    
     rsTmp.updateString("CREATED_BY",tmpParty.getAttribute("CREATED_BY").getString());
     rsTmp.updateString("CREATED_DATE",tmpParty.getAttribute("CREATED_DATE").getString());
     rsTmp.updateString("MODIFIED_BY",tmpParty.getAttribute("MODIFIED_DATE").getString());
     rsTmp.updateString("MODIFIED_DATE",tmpParty.getAttribute("MODIFIED_DATE").getString());
     rsTmp.updateInt("HIERARCHY_ID",tmpParty.getAttribute("HIERARCHY_ID").getInt());
     rsTmp.updateBoolean("APPROVED",tmpParty.getAttribute("APPROVED").getBool());
     rsTmp.updateString("APPROVED_DATE",tmpParty.getAttribute("APPROVED_DATE").getString());
     rsTmp.updateBoolean("CANCELLED",tmpParty.getAttribute("CANCELLED").getBool());
     rsTmp.updateBoolean("CHANGED",true);
     rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
     rsTmp.updateBoolean("REJECTED",false);
     rsTmp.updateString("REJECTED_DATE","0000-00-00");
     rsTmp.updateString("REJECTED_REMARKS",tmpParty.getAttribute("REJECTED_REMARKS").getString());
     rsTmp.insertRow();
     } catch(Exception e) {
     return false;
     }
     return true;
     }
     */
    
   
    
    public static boolean CanCancel(String pDocNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean CanCancel=false;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            System.out.println("SELECT MM_AMEND_NO FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER WHERE MM_AMEND_NO='"+pDocNo+"'  AND APPROVED=0 AND CANCELED=0");
            rsTmp=stTmp.executeQuery("SELECT MM_AMEND_NO FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER WHERE MM_AMEND_NO='"+pDocNo+"'  AND APPROVED=0 AND CANCELED=0");
            
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                CanCancel=true;
            }
            
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return CanCancel;
    }

}
