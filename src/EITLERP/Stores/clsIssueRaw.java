/*
 * clsIssue.java
 *
 * Created on April 26, 2004, 1:28 PM
 */
package EITLERP.Stores;

import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.util.*;
import EITLERP.*;
import EITLERP.Finance.UtilFunctions;

/**
 *
 * @author nhpatel
 * @version
 */
public class clsIssueRaw {

    public String LastError = "";
    private ResultSet rsIssue;
    private Connection Conn;
    private Statement Stmt;

    public HashMap colLineItems;

    private HashMap props;

    public boolean Ready = false;

    //History Related properties
    private boolean HistoryView = false;

    //Flag Indicating whether user has entered the document no.
    public boolean UserDocNo = false;

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
     * Creates new clsIssue
     */
    public clsIssueRaw() {
        props = new HashMap();

        props.put("COMPANY_ID", new Variant(0));
        props.put("ISSUE_NO", new Variant(""));
        props.put("ISSUE_DATE", new Variant(""));
        props.put("FOR_STORE", new Variant(""));
        props.put("FOR_DEPT_ID", new Variant(0));
        props.put("ISSUE_TYPE", new Variant(""));
        props.put("BLEND_CODE", new Variant(0));
        props.put("PURPOSE", new Variant(""));
        props.put("APPROVED", new Variant(false));
        props.put("HIERARCHY_ID", new Variant(0));
        props.put("REJECTED", new Variant(false));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("ISSUE_WITH_LOT", new Variant(false));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REMARKS", new Variant(""));
        props.put("CANCELED", new Variant(false));
        props.put("STATUS", new Variant(""));
        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("PREFIX", new Variant(""));
        props.put("DEPT_ID", new Variant(0));
        props.put("REVISION_NO", new Variant(0));
        props.put("FROM", new Variant(0));
        props.put("TO", new Variant(0));
        props.put("REMARKS", new Variant(""));
        props.put("APPROVAL_STATUS", new Variant(""));
        //Create a new object for line items
        colLineItems = new HashMap();
    }

    public boolean LoadData(long pCompanyID) {
        Ready = false;
        try {
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsIssue = Stmt.executeQuery("SELECT * FROM D_INV_ISSUE_HEADER WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND FOR_STORE='R' AND ISSUE_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND ISSUE_DATE<='" + EITLERPGLOBAL.FinToDateDB + "' ORDER BY ISSUE_NO");
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
            rsIssue.close();
        } catch (Exception e) {

        }

    }

    //Navigation Methods
    public boolean MoveFirst() {
        try {
            rsIssue.first();
            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }

    }

    public boolean MoveNext() {
        try {
            if (rsIssue.isAfterLast() || rsIssue.isLast()) {
                //Move pointer at last record
                //If it is beyond eof
                rsIssue.last();
            } else {
                rsIssue.next();
                if (rsIssue.isAfterLast()) {
                    rsIssue.last();
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
            if (rsIssue.isFirst() || rsIssue.isBeforeFirst()) {
                rsIssue.first();
            } else {
                rsIssue.previous();
                if (rsIssue.isBeforeFirst()) {
                    rsIssue.first();
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
            rsIssue.last();
            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean Insert(String pPrefix, String pDocno) {
        Statement stTmp, stHistory, stHDetail, stLot, stHLot;
        ResultSet rsTmp, rsHistory, rsHDetail, rsLot, rsHLot;
        Statement stHeader;
        ResultSet rsHeader;

        String strSQL = "", Matno = "";
        int MatSrno = 0;

        try {

            for (int i = 1; i <= colLineItems.size(); i++) {
                clsIssueGenItem ObjItem = (clsIssueGenItem) colLineItems.get(Integer.toString(i));
                if (ObjItem.getAttribute("ISSUE_VALUE").getDouble() <= 0) {
                    int Conform = JOptionPane.showConfirmDialog(null, "Total Value for the Item:" + ObjItem.getAttribute("ITEM_CODE").getString() + " is Zero \n" + " Do You want To continue...", "Message..", JOptionPane.YES_NO_OPTION);
                    if (Conform != JOptionPane.YES_OPTION) {
                        return false;
                    }
                }
            }

            // ---- History Related Changes ------ //
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHDetail = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHLot = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rsHistory = stHistory.executeQuery("SELECT * FROM D_INV_ISSUE_HEADER_H WHERE ISSUE_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail = stHDetail.executeQuery("SELECT * FROM D_INV_ISSUE_DETAIL_H WHERE ISSUE_NO='1'");
            rsHDetail.first();
            rsHLot = stHLot.executeQuery("SELECT * FROM D_INV_ISSUE_LOT_H WHERE ISSUE_NO='1'");
            rsHLot.first();
            //------------------------------------//

            ApprovalFlow ObjFlow = new ApprovalFlow();
            // Inserting records in Header
            long gCompanyID = (long) getAttribute("COMPANY_ID").getVal();
            setAttribute("COMPANY_ID", (long) getAttribute("COMPANY_ID").getVal());

            //=========== Check the Quantities entered against Rejection.============= //
            for (int i = 1; i <= colLineItems.size(); i++) {
                clsIssueGenItem ObjItem = (clsIssueGenItem) colLineItems.get(Integer.toString(i));

                Matno = (String) ObjItem.getAttribute("REQ_NO").getObj();
                MatSrno = (int) ObjItem.getAttribute("REQ_SRNO").getVal();
                double MatQty = 0;
                double PrevQty = 0; //Previously Entered Qty against RJN
                double CurrentQty = 0; //Currently entered Qty.

                if ((!Matno.trim().equals("")) && (MatSrno > 0)) //Material Req. No. entered
                {
                    //Get the  Rejection Qty.
                    stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    strSQL = "SELECT REQ_QTY AS QTY FROM D_INV_ISSUE_REQ_DETAIL WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID) + " AND REQ_NO='" + Matno + "' AND SR_NO=" + MatSrno;
                    rsTmp = stTmp.executeQuery(strSQL);
                    rsTmp.first();

                    if (rsTmp.getRow() > 0) {
                        MatQty = rsTmp.getDouble("QTY");
                    }

                    //Get Total Qty Entered in GRN Against this MIR No.
                    PrevQty = 0;
                    stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    strSQL = "SELECT SUM(QTY) AS SUMQTY FROM D_INV_ISSUE_DETAIL WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID) + " AND REQ_NO='" + Matno + "' AND REQ_SRNO=" + MatSrno + " AND ISSUE_NO NOT IN(SELECT ISSUE_NO FROM D_INV_ISSUE_HEADER WHERE CANCELED=1) ";
                    rsTmp = stTmp.executeQuery(strSQL);
                    rsTmp.first();

                    if (rsTmp.getRow() > 0) {
                        PrevQty = rsTmp.getDouble("SUMQTY");
                    }

                    CurrentQty = ObjItem.getAttribute("QTY").getVal();

                    if ((CurrentQty + PrevQty) > MatQty) //If total Qty exceeds MIR Qty. Do not allow
                    {
                        LastError = "Total quantity entered " + (CurrentQty + PrevQty) + " exceeds Req No. " + Matno + " Sr. No. " + MatSrno + " qty " + MatQty + ". Please verify the input.";
                        return false;
                    }
                }
            }

            //=========== Check the Quantities entered against MIR============= //
            for (int i = 1; i <= colLineItems.size(); i++) {
                clsIssueGenItem ObjItem = (clsIssueGenItem) colLineItems.get(Integer.toString(i));

                String MIRNo = (String) ObjItem.getAttribute("MIR_NO").getObj();
                int SrNo = (int) ObjItem.getAttribute("MIR_SR_NO").getVal();
                int MIRType = (int) ObjItem.getAttribute("MIR_TYPE").getVal();

                double MIRQty = 0;
                double PrevQty = 0; //Previously Entered Qty against MIR
                double CurrentQty = 0; //Currently entered Qty.

                if ((!MIRNo.trim().equals("")) && (SrNo > 0)) {
                    stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    strSQL = "SELECT QTY FROM D_INV_MIR_DETAIL WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MIR_NO='" + MIRNo + "' AND SR_NO=" + SrNo + " AND MIR_TYPE=" + MIRType;
                    rsTmp = stTmp.executeQuery(strSQL);
                    rsTmp.first();

                    if (rsTmp.getRow() > 0) {
                        MIRQty = rsTmp.getDouble("QTY");
                    }

                    PrevQty = 0;
                    stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    strSQL = "SELECT SUM(QTY) AS SUMQTY FROM D_INV_ISSUE_DETAIL WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MIR_NO='" + MIRNo + "' AND MIR_SR_NO=" + SrNo + " AND MIR_TYPE=" + MIRType;
                    rsTmp = stTmp.executeQuery(strSQL);
                    rsTmp.first();

                    if (rsTmp.getRow() > 0) {
                        PrevQty = rsTmp.getDouble("SUMQTY");
                    }

                    CurrentQty = ObjItem.getAttribute("QTY").getVal();

                    if ((CurrentQty + PrevQty) > MIRQty) //If total Qty exceeds MIR Qty. Do not allow
                    {
                        LastError = "Total quantity entered " + (CurrentQty + PrevQty) + " exceeds MIR No. " + MIRNo + " Sr. No. " + SrNo + " qty " + MIRQty + ". Please verify the input.";
                        //return false;
                    }
                }
            }
            //===========================Completed GRN Check =============================//

            // Update the Stock only after Final Approval //
            String AStatus = (String) getAttribute("APPROVAL_STATUS").getObj();
            String gType = (String) getAttribute("ISSUE_TYPE").getObj();
            boolean LotItem = (boolean) getAttribute("ISSUE_WITH_LOT").getBool();
            String docDate = (String) getAttribute("ISSUE_DATE").getObj();

            //======== Stock Checking ========//
            //if(AStatus.equals("F")) {
            for (int i = 1; i <= colLineItems.size(); i++) {
                clsIssueGenItem ObjItem = (clsIssueGenItem) colLineItems.get(Integer.toString(i));

                String ItemID = (String) ObjItem.getAttribute("ITEM_CODE").getObj();

                clsStockInfo objStock = (new clsItemStock()).getOnHandQtyOn(EITLERPGLOBAL.gCompanyID, ItemID, docDate);
                double OnHandQty = objStock.StockQty;
                double MinQty = clsItem.getMinQty(EITLERPGLOBAL.gCompanyID, ItemID);

                //========= New Code of Allow Excess Issue ===========//
                String MIRNo = (String) ObjItem.getAttribute("MIR_NO").getObj();
                int MIRSrNo = (int) ObjItem.getAttribute("MIR_SR_NO").getVal();
                boolean AllowExcess = false;

                if (!MIRNo.equals("") && MIRSrNo > 0) {
                    //Allow Excess Quantity Issue in case MIR no. is there
                    AllowExcess = true;
                }

                //Forcefully Allow Excess to false
                AllowExcess = false;

                //====================================================//
                if (MinQty > 0) //Minimum level maintained
                {
                    double CurrentQty = ObjItem.getAttribute("QTY").getVal();

                    //Get the sum of all repeated item codes
                    for (int j = 1; j <= colLineItems.size(); j++) {
                        clsIssueGenItem ObjItemEx = (clsIssueGenItem) colLineItems.get(Integer.toString(j));
                        if (ObjItemEx.getAttribute("ITEM_CODE").getObj().toString().equals(ItemID) && j != i) {
                            CurrentQty += ObjItemEx.getAttribute("QTY").getVal();
                        }
                    }

                    if ((OnHandQty - CurrentQty) < MinQty && !AllowExcess) {
                        LastError = "Cannot deduct stock for item " + ItemID + ". Stock level reached at minimum stock level";
                        return false; //Temporaritly suppressed
                    }
                } else {
                    double CurrentQty = ObjItem.getAttribute("QTY").getVal();

                    //Get the sum of all repeated item codes
                    for (int j = 1; j <= colLineItems.size(); j++) {
                        clsIssueGenItem ObjItemEx = (clsIssueGenItem) colLineItems.get(Integer.toString(j));
                        if (ObjItemEx.getAttribute("ITEM_CODE").getObj().toString().equals(ItemID) && j != i) {
                            CurrentQty += ObjItemEx.getAttribute("QTY").getVal();
                        }
                    }

                    if ((OnHandQty - CurrentQty) < 0 && !AllowExcess) {
                        LastError = "Cannot deduct stock for item " + ItemID + ". Stock level reached at zero level. And No MIR No. specified in the issue entry";
                        //return false; //Temporarily COMMENT
                    }

                }

            }
            //}
            //======== Stock checking over ========//

            // Stock Updataion started over here
            //=======================================//
            if (AStatus.equals("F")) {
                //-------- First Update the stock -------------//
                for (int i = 1; i <= colLineItems.size(); i++) {
                    clsIssueGenItem ObjItem = (clsIssueGenItem) colLineItems.get(Integer.toString(i));

                    double TotalZeroValQty = 0;

                    Matno = (String) ObjItem.getAttribute("REQ_NO").getObj();
                    MatSrno = (int) ObjItem.getAttribute("REQ_SRNO").getVal();

                    double Qty = ObjItem.getAttribute("QTY").getVal();
                    String ItemID = (String) ObjItem.getAttribute("ITEM_CODE").getObj();
                    String BOENo = (String) ObjItem.getAttribute("BOE_NO").getObj();
                    String WareHouseID = (String) ObjItem.getAttribute("WAREHOUSE_ID").getObj();
                    String LocationID = (String) ObjItem.getAttribute("LOCATION_ID").getObj();

                    data.Execute("UPDATE D_INV_ISSUE_REQ_DETAIL SET ISSUED_QTY=ISSUED_QTY+" + Qty + ",BAL_QTY=REQ_QTY-ISSUED_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND REQ_NO='" + Matno.trim() + "' AND SR_NO=" + MatSrno);
                    data.Execute("UPDATE D_INV_ISSUE_REQ_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND REQ_NO='" + Matno.trim() + "'");

                    for (int l = 1; l <= ObjItem.colItemLot.size(); l++) {
                        clsIssueLot ObjLot = (clsIssueLot) ObjItem.colItemLot.get(Integer.toString(l));

                        String ItemLotNo = ObjLot.getAttribute("ITEM_LOT_NO").getString();
                        String AutoLotNo = ObjLot.getAttribute("AUTO_LOT_NO").getString();
                        data.Execute("UPDATE D_INV_GRN_LOT SET BALANCE_QTY=BALANCE_QTY-" + Qty + ",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND ITEM_LOT_NO='" + ItemLotNo + "' AND AUTO_LOT_NO='" + AutoLotNo + "'");
                    }

                    //======= Free Allocation =======//
                    int DeptID = (int) ObjItem.getAttribute("DEPT_ID").getVal();
                    String IssueNo = (String) getAttribute("ISSUE_NO").getObj();

                    clsAllocation ObjAllocation = new clsAllocation();
                    ObjAllocation.freeAllocation(EITLERPGLOBAL.gCompanyID, DeptID, ItemID, Qty, IssueNo, i);
                    //=================================================================//

                    //======== New Code of Excess Issue of Item ====================//
                    //=============================================================//
                    clsStockInfo objStock = (new clsItemStock()).getOnHandQtyOn(EITLERPGLOBAL.gCompanyID, ItemID, docDate);
                    double OnHandQty = objStock.StockQty;
                    double ExcessQty = 0;
                    double AddExcess = 0;

                    if (Qty > OnHandQty) {
                        ExcessQty = Qty - OnHandQty;

                        AddExcess = 0;

                        //First find out the duplication of Item Code in Issue Detail
                        for (int a = 1; a <= colLineItems.size(); a++) {
                            clsIssueGenItem ObjTempItem = (clsIssueGenItem) colLineItems.get(Integer.toString(a));

                            String tmpItemID = (String) ObjTempItem.getAttribute("ITEM_CODE").getObj();
                            if (a != i && tmpItemID.trim().equals(ItemID)) {
                                AddExcess = ObjTempItem.getAttribute("EXCESS_ISSUE_QTY").getVal();
                            }
                        }
                        //========================================================//

                        ExcessQty += AddExcess;
                    }

                    //Actual Excess Qty for the Line of Issue
                    if (Qty > OnHandQty) {
                        ExcessQty = Qty - OnHandQty;

                        //Bring down the Issue Qty to On Hand Qty - Do not negate the stock //
                        Qty = OnHandQty;

                    } else {
                        ExcessQty = 0;
                    }

                    //Update the Excess Issue Qty.
                    ObjItem.setAttribute("EXCESS_ISSUE_QTY", ExcessQty);
                    //===================================================================//

                    if (BOENo.trim().equals("")) {
                        BOENo = "X";
                    }

                    Statement tmpStmt;
                    tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

                    //Update the MIR
                    String MIRNo = (String) ObjItem.getAttribute("MIR_NO").getObj();
                    int MIRSrNo = (int) ObjItem.getAttribute("MIR_SR_NO").getVal();
                    int MIRType = (int) ObjItem.getAttribute("MIR_TYPE").getVal();

                    if (!MIRNo.trim().equals("")) {
                        data.Execute("UPDATE D_INV_MIR_DETAIL SET ISSUED_QTY=ISSUED_QTY+" + Qty + ",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MIR_NO='" + MIRNo + "' AND SR_NO=" + MIRSrNo + " AND MIR_TYPE=" + MIRType);
                        data.Execute("UPDATE D_INV_MIR_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MIR_NO='" + MIRNo + "'");
                    }

                    //Check that ITEM_ID+LOT_NO+BOE_NO entry exist in D_INV_ITEM_LOST_MASTER
                    Statement tmpStmt1;
                    tmpStmt1 = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    rsTmp = tmpStmt1.executeQuery("SELECT * FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID=" + Integer.toString(EITLERPGLOBAL.gCompanyID) + " AND ITEM_ID='" + ItemID.trim() + "' AND  BOE_NO='" + BOENo + "' AND WAREHOUSE_ID='" + WareHouseID + "' AND LOCATION_ID='" + LocationID + "' AND LOT_NO='X'");
                    rsTmp.first();

                    //Check that entry exist. Else create a new record
                    if (rsTmp.getRow() <= 0) {
                        rsTmp.moveToInsertRow();
                        rsTmp.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        rsTmp.updateString("ITEM_ID", ItemID);
                        rsTmp.updateString("LOT_NO", "X");
                        rsTmp.updateString("BOE_NO", BOENo);
                        rsTmp.updateString("BOE_SR_NO", (String) ObjItem.getAttribute("BOE_SR_NO").getObj());
                        rsTmp.updateString("WAREHOUSE_ID", WareHouseID);
                        rsTmp.updateString("LOCATION_ID", LocationID);
                        rsTmp.updateBoolean("CHANGED", true);
                        rsTmp.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDate());
                        rsTmp.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                        rsTmp.updateInt("MODIFIED_BY", EITLERPGLOBAL.gUserID);
                        rsTmp.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                        rsTmp.updateInt("CREATED_BY", EITLERPGLOBAL.gUserID);
                        rsTmp.updateDouble("ALLOCATED_QTY", 0);
                        rsTmp.updateDouble("AVAILABLE_QTY", 0);
                        rsTmp.updateDouble("ON_HAND_QTY", 0);
                        rsTmp.updateDouble("REJECTED_QTY", 0);
                        rsTmp.updateDouble("ZERO_VAL_QTY", 0);
                        rsTmp.updateDouble("ZERO_ISSUED_QTY", 0);
                        rsTmp.updateDouble("ZERO_RECEIPT_QTY", 0);
                        rsTmp.updateDouble("ZERO_OPENING_QTY", 0);
                        rsTmp.updateString("LAST_ISSUED_DATE", "0000-00-00");
                        rsTmp.updateString("LAST_RECEIPT_DATE", "0000-00-00");
                        rsTmp.updateDouble("TOTAL_ISSUED_QTY", 0);
                        rsTmp.updateDouble("TOTAL_RECEIPT_QTY", 0);
                        rsTmp.updateDouble("OPENING_RATE", 0);
                        rsTmp.updateDouble("OPENING_QTY", 0);
                        rsTmp.insertRow();
                    }

                    //Check that ITEM_ID+LOT_NO+BOE_NO entry exist in D_INV_ITEM_LOST_MASTER
                    tmpStmt1 = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    rsTmp = tmpStmt1.executeQuery("SELECT * FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID=" + Integer.toString(EITLERPGLOBAL.gCompanyID) + " AND ITEM_ID='" + ItemID.trim() + "' AND  BOE_NO='" + BOENo + "' AND WAREHOUSE_ID='" + WareHouseID + "' AND LOCATION_ID='" + LocationID + "' AND LOT_NO='X'");
                    rsTmp.first();

                    // Updating tables
                    if (clsItem.getMaintainStock(EITLERPGLOBAL.gCompanyID, ItemID)) {
                        //===== New Code of Zero Value Issue Qty ----------//
                        double ActualIssQty = Qty;
                        double ZeroIssQty = 0;

                        if (rsTmp.getRow() > 0) {
                            double ZeroValQty = rsTmp.getDouble("ZERO_VAL_QTY");

                            if (ZeroValQty > Qty) {
                                ActualIssQty = 0;
                                ZeroIssQty = Qty;

                                //Update the Zero Value Qty.
                                rsTmp.updateDouble("ZERO_ISSUED_QTY", rsTmp.getDouble("ZERO_ISSUED_QTY") + Qty);
                                rsTmp.updateDouble("ZERO_VAL_QTY", rsTmp.getDouble("ZERO_VAL_QTY") - Qty);

                                TotalZeroValQty += ZeroIssQty;
                            } else {
                                ActualIssQty = Qty - ZeroValQty;
                                ZeroIssQty = Qty - ActualIssQty;

                                //Update the Zero Value Qty.
                                rsTmp.updateDouble("ZERO_ISSUED_QTY", rsTmp.getDouble("ZERO_ISSUED_QTY") + ZeroIssQty);
                                rsTmp.updateDouble("ZERO_VAL_QTY", 0);

                                TotalZeroValQty += ZeroIssQty;
                            }
                        }
                        //================================================================================//

                        //Now Calculate the Actual Issue Value after Zero Val Qty deduction //
                        double Rate = ObjItem.getAttribute("RATE").getVal();
                        double IssueValue = ActualIssQty * Rate;
                        //ObjItem.setAttribute("ISSUE_VALUE",IssueValue);
                        //================================================================= //

                        rsTmp.updateDouble("TOTAL_ISSUED_QTY", rsTmp.getDouble("TOTAL_ISSUED_QTY") + ActualIssQty);
                        rsTmp.updateString("LAST_ISSUED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                        rsTmp.updateDouble("ON_HAND_QTY", rsTmp.getDouble("ON_HAND_QTY") - Qty);
                        rsTmp.updateDouble("AVAILABLE_QTY", rsTmp.getDouble("AVAILABLE_QTY") - Qty);
                        rsTmp.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                        rsTmp.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                        rsTmp.updateBoolean("CHANGED", true);
                        rsTmp.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                        rsTmp.updateRow();
                    }

                    ObjItem.setAttribute("ZERO_VAL_QTY", TotalZeroValQty);
                    colLineItems.put(Integer.toString(i), ObjItem);
                }
            }

            //--------- Generate New MIR No. ------------
            if (UserDocNo) {
                rsTmp = data.getResult("SELECT ISSUE_NO FROM D_INV_ISSUE_HEADER WHERE ISSUE_NO='" + ((String) getAttribute("ISSUE_NO").getObj()).trim() + "' AND COMPANY_ID=" + EITLERPGLOBAL.gCompanyID);
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    LastError = "Document no. already exist. Please specify other document no.";
                    return false;
                }
            } else {
                setAttribute("ISSUE_NO", clsFirstFree.getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 15, (int) getAttribute("FFNO").getVal(), true));
            }
            //-------------------------------------------------

            rsIssue.moveToInsertRow();
            rsIssue.updateLong("COMPANY_ID", (long) getAttribute("COMPANY_ID").getVal());
            rsIssue.updateString("ISSUE_NO", (String) getAttribute("ISSUE_NO").getObj());
            rsIssue.updateString("ISSUE_DATE", (String) getAttribute("ISSUE_DATE").getObj());
            rsIssue.updateString("FOR_STORE", "R");
            rsIssue.updateLong("FOR_DEPT_ID", (long) getAttribute("FOR_DEPT_ID").getVal());
            rsIssue.updateString("ISSUE_TYPE", (String) getAttribute("ISSUE_TYPE").getObj());
            rsIssue.updateString("BLEND_CODE", (String) getAttribute("BLEND_CODE").getObj());
            rsIssue.updateString("PURPOSE", (String) getAttribute("PURPOSE").getObj());
            rsIssue.updateBoolean("ISSUE_WITH_LOT", (boolean) getAttribute("ISSUE_WITH_LOT").getBool());
            rsIssue.updateString("REMARKS", (String) getAttribute("REMARKS").getObj());
            rsIssue.updateBoolean("CANCELED", (boolean) getAttribute("CANCELED").getBool());
            rsIssue.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            rsIssue.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
            rsIssue.updateLong("MODIFIED_BY", (long) getAttribute("MODIFIED_BY").getVal());
            rsIssue.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
            rsIssue.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsIssue.updateBoolean("CHANGED", true);
            rsIssue.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsIssue.updateBoolean("APPROVED", false);
            rsIssue.updateString("APPROVED_DATE", "0000-00-00");
            rsIssue.updateBoolean("REJECTED", false);
            rsIssue.updateString("REJECTED_DATE", "0000-00-00");
            rsIssue.updateBoolean("CANCELED", false);
            rsIssue.insertRow();

            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO", 1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY", (int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS", (String) getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS", (String) getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID", (long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("ISSUE_NO", (String) getAttribute("ISSUE_NO").getObj());
            rsHistory.updateString("ISSUE_DATE", (String) getAttribute("ISSUE_DATE").getObj());
            rsHistory.updateString("FOR_STORE", "R");
            rsHistory.updateLong("FOR_DEPT_ID", (long) getAttribute("FOR_DEPT_ID").getVal());
            rsHistory.updateString("ISSUE_TYPE", (String) getAttribute("ISSUE_TYPE").getObj());
            rsHistory.updateString("BLEND_CODE", (String) getAttribute("BLEND_CODE").getObj());
            rsHistory.updateString("PURPOSE", (String) getAttribute("PURPOSE").getObj());
            rsHistory.updateBoolean("ISSUE_WITH_LOT", (boolean) getAttribute("ISSUE_WITH_LOT").getBool());
            rsHistory.updateString("REMARKS", (String) getAttribute("REMARKS").getObj());
            rsHistory.updateBoolean("CANCELED", (boolean) getAttribute("CANCELED").getBool());
            rsHistory.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY", (long) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateBoolean("CHANGED", true);
            rsHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED", false);
            rsHistory.updateString("APPROVED_DATE", "0000-00-00");
            rsHistory.updateBoolean("REJECTED", false);
            rsHistory.updateString("REJECTED_DATE", "0000-00-00");
            rsHistory.updateBoolean("CANCELED", false);
            rsHistory.insertRow();

            // Inserting records in ISSUE Details
            Statement tmpStmt;
            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery("SELECT * FROM D_INV_ISSUE_DETAIL WHERE COMPANY_ID=" + Long.toString(gCompanyID) + " AND ISSUE_NO='1'");
            String nIssueno = (String) getAttribute("ISSUE_NO").getObj();
            String nIssuetype = (String) getAttribute("ISSUE_TYPE").getObj();
            long nCompanyID = (long) getAttribute("COMPANY_ID").getVal();
            String nIssue = (String) getAttribute("ISSUE_NO").getObj();

            stLot = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsLot = stLot.executeQuery("SELECT * FROM D_INV_ISSUE_LOT WHERE ISSUE_NO='1'");
            rsLot.first();

            for (int i = 1; i <= colLineItems.size(); i++) {
                clsIssueGenItem ObjItem = (clsIssueGenItem) colLineItems.get(Integer.toString(i));

                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID", nCompanyID);
                rsTmp.updateString("ISSUE_NO", nIssueno);
                rsTmp.updateLong("SR_NO", i);
                rsTmp.updateString("ITEM_CODE", (String) ObjItem.getAttribute("ITEM_CODE").getObj());
                rsTmp.updateString("ITEM_EXTRA_DESC", (String) ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsTmp.updateString("WAREHOUSE_ID", (String) ObjItem.getAttribute("WAREHOUSE_ID").getObj());
                rsTmp.updateString("LOCATION_ID", (String) ObjItem.getAttribute("LOCATION_ID").getObj());
                rsTmp.updateString("BOE_NO", (String) ObjItem.getAttribute("BOE_NO").getObj());
                rsTmp.updateString("BOE_SR_NO", (String) ObjItem.getAttribute("BOE_SR_NO").getObj());
                rsTmp.updateDouble("QTY", ObjItem.getAttribute("QTY").getVal());
                rsTmp.updateDouble("QTY_REQD", ObjItem.getAttribute("QTY_REQD").getDouble());
                rsTmp.updateDouble("EXCESS_ISSUE_QTY", ObjItem.getAttribute("EXCESS_ISSUE_QTY").getVal());
                rsTmp.updateLong("UNIT", (long) ObjItem.getAttribute("UNIT").getVal());
                rsTmp.updateLong("COST_CENTER_ID", (long) ObjItem.getAttribute("COST_CENTER_ID").getVal());
                rsTmp.updateString("SHADE", (String) ObjItem.getAttribute("SHADE").getObj());
                rsTmp.updateString("ISSUE_DESC", (String) ObjItem.getAttribute("ISSUE_DESC").getObj());
                rsTmp.updateString("REQ_NO", (String) ObjItem.getAttribute("REQ_NO").getObj());
                rsTmp.updateLong("REQ_SRNO", (long) ObjItem.getAttribute("REQ_SRNO").getVal());
                rsTmp.updateString("REMARKS", (String) ObjItem.getAttribute("REMARKS").getObj());
                rsTmp.updateString("LF_NO", (String) ObjItem.getAttribute("LF_NO").getObj());
                rsTmp.updateBoolean("CANCELED", (boolean) ObjItem.getAttribute("CANCELED").getBool());
                rsTmp.updateLong("CREATED_BY", (long) ObjItem.getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE", (String) ObjItem.getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY", (long) ObjItem.getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE", (String) ObjItem.getAttribute("MODIFEID_DATE").getObj());
                rsTmp.updateInt("DEPT_ID", (int) ObjItem.getAttribute("DEPT_ID").getVal());
                rsTmp.updateString("GRN_NO", (String) ObjItem.getAttribute("GRN_NO").getObj());
                rsTmp.updateInt("GRN_SR_NO", (int) ObjItem.getAttribute("GRN_SR_NO").getVal());
                rsTmp.updateInt("GRN_TYPE", (int) ObjItem.getAttribute("GRN_TYPE").getVal());
                rsTmp.updateString("MIR_NO", (String) ObjItem.getAttribute("MIR_NO").getObj());
                rsTmp.updateInt("MIR_SR_NO", (int) ObjItem.getAttribute("MIR_SR_NO").getVal());
                rsTmp.updateInt("MIR_TYPE", (int) ObjItem.getAttribute("MIR_TYPE").getVal());
                rsTmp.updateDouble("ZERO_VAL_QTY", ObjItem.getAttribute("ZERO_VAL_QTY").getVal());
                rsTmp.updateDouble("ISSUE_VALUE", ObjItem.getAttribute("ISSUE_VALUE").getVal());
                rsTmp.updateDouble("RATE", ObjItem.getAttribute("RATE").getVal());
                rsTmp.updateDouble("STOCK_QTY", ObjItem.getAttribute("STOCK_QTY").getVal());
                rsTmp.updateBoolean("CHANGED", true);
                rsTmp.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateString("LOT_NO", "X");
                rsTmp.updateString("BLEND_CODE", (String) ObjItem.getAttribute("BLEND_CODE").getObj());
                rsTmp.updateString("MATERIAL_CODE_NO", (String) ObjItem.getAttribute("MATERIAL_CODE_NO").getObj());
                rsTmp.updateString("MFG_PROG_NO", (String) ObjItem.getAttribute("MFG_PROG_NO").getObj());

                rsTmp.insertRow();

                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO", 1);
                rsHDetail.updateLong("COMPANY_ID", nCompanyID);
                rsHDetail.updateString("ISSUE_NO", nIssueno);
                rsHDetail.updateLong("SR_NO", i);
                rsHDetail.updateString("ITEM_CODE", (String) ObjItem.getAttribute("ITEM_CODE").getObj());
                rsHDetail.updateString("ITEM_EXTRA_DESC", (String) ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsHDetail.updateString("WAREHOUSE_ID", (String) ObjItem.getAttribute("WAREHOUSE_ID").getObj());
                rsHDetail.updateString("LOCATION_ID", (String) ObjItem.getAttribute("LOCATION_ID").getObj());
                rsHDetail.updateString("BOE_NO", (String) ObjItem.getAttribute("BOE_NO").getObj());
                rsHDetail.updateString("BOE_SR_NO", (String) ObjItem.getAttribute("BOE_SR_NO").getObj());
                rsHDetail.updateDouble("QTY", ObjItem.getAttribute("QTY").getVal());
                rsHDetail.updateDouble("QTY_REQD", ObjItem.getAttribute("QTY_REQD").getDouble());
                rsHDetail.updateDouble("EXCESS_ISSUE_QTY", ObjItem.getAttribute("EXCESS_ISSUE_QTY").getVal());
                rsHDetail.updateLong("UNIT", (long) ObjItem.getAttribute("UNIT").getVal());
                rsHDetail.updateLong("COST_CENTER_ID", (long) ObjItem.getAttribute("COST_CENTER_ID").getVal());
                rsHDetail.updateString("SHADE", (String) ObjItem.getAttribute("SHADE").getObj());
                rsHDetail.updateString("ISSUE_DESC", (String) ObjItem.getAttribute("ISSUE_DESC").getObj());
                rsHDetail.updateString("REQ_NO", (String) ObjItem.getAttribute("REQ_NO").getObj());
                rsHDetail.updateLong("REQ_SRNO", (long) ObjItem.getAttribute("REQ_SRNO").getVal());
                rsHDetail.updateString("REMARKS", (String) ObjItem.getAttribute("REMARKS").getObj());
                rsHDetail.updateString("LF_NO", (String) ObjItem.getAttribute("LF_NO").getObj());
                rsHDetail.updateBoolean("CANCELED", (boolean) ObjItem.getAttribute("CANCELED").getBool());
                rsHDetail.updateLong("CREATED_BY", (long) ObjItem.getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE", (String) ObjItem.getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY", (long) ObjItem.getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE", (String) ObjItem.getAttribute("MODIFEID_DATE").getObj());
                rsHDetail.updateInt("DEPT_ID", (int) ObjItem.getAttribute("DEPT_ID").getVal());
                rsHDetail.updateString("GRN_NO", (String) ObjItem.getAttribute("GRN_NO").getObj());
                rsHDetail.updateInt("GRN_SR_NO", (int) ObjItem.getAttribute("GRN_SR_NO").getVal());
                rsHDetail.updateInt("GRN_TYPE", (int) ObjItem.getAttribute("GRN_TYPE").getVal());
                rsHDetail.updateString("MIR_NO", (String) ObjItem.getAttribute("MIR_NO").getObj());
                rsHDetail.updateInt("MIR_SR_NO", (int) ObjItem.getAttribute("MIR_SR_NO").getVal());
                rsHDetail.updateInt("MIR_TYPE", (int) ObjItem.getAttribute("MIR_TYPE").getVal());
                rsHDetail.updateDouble("ZERO_VAL_QTY", ObjItem.getAttribute("ZERO_VAL_QTY").getVal());
                rsHDetail.updateDouble("ISSUE_VALUE", ObjItem.getAttribute("ISSUE_VALUE").getVal());
                rsHDetail.updateDouble("RATE", ObjItem.getAttribute("RATE").getVal());
                rsHDetail.updateDouble("STOCK_QTY", ObjItem.getAttribute("STOCK_QTY").getVal());
                rsHDetail.updateBoolean("CHANGED", true);
                rsHDetail.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("LOT_NO", "X");
                rsHDetail.updateString("BLEND_CODE", (String) ObjItem.getAttribute("BLEND_CODE").getObj());
                rsHDetail.updateString("MATERIAL_CODE_NO", (String) ObjItem.getAttribute("MATERIAL_CODE_NO").getObj());
                rsHDetail.updateString("MFG_PROG_NO", (String) ObjItem.getAttribute("MFG_PROG_NO").getObj());
                rsHDetail.insertRow();

                //Now insert lot nos.
                for (int l = 1; l <= ObjItem.colItemLot.size(); l++) {
                    clsIssueLot ObjLot = (clsIssueLot) ObjItem.colItemLot.get(Integer.toString(l));

                    rsLot.moveToInsertRow();
                    rsLot.updateInt("COMPANY_ID", getAttribute("COMPANY_ID").getInt());
                    rsLot.updateString("ISSUE_NO", getAttribute("ISSUE_NO").getString());
                    rsLot.updateInt("ISSUE_SR_NO", i);
                    rsLot.updateInt("ISSUE_TYPE", 2);
                    rsLot.updateInt("SR_NO", l);
                    rsLot.updateString("ITEM_ID", ObjLot.getAttribute("ITEM_ID").getString());
                    rsLot.updateString("ITEM_LOT_NO", ObjLot.getAttribute("ITEM_LOT_NO").getString());
                    rsLot.updateString("AUTO_LOT_NO", ObjLot.getAttribute("AUTO_LOT_NO").getString());
                    rsLot.updateDouble("ISSUED_LOT_QTY", ObjLot.getAttribute("ISSUED_LOT_QTY").getDouble());
                    rsLot.updateDouble("ISSUE_RATE", ObjLot.getAttribute("ISSUE_RATE").getDouble());

                    rsLot.updateBoolean("APPROVED", false);
                    rsLot.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsLot.updateBoolean("CANCELLED", false);
                    rsLot.updateBoolean("CHANGED", true);
                    rsLot.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsLot.insertRow();

                    rsHLot.moveToInsertRow();
                    rsHLot.updateInt("REVISION_NO", 1);
                    rsHLot.updateInt("COMPANY_ID", getAttribute("COMPANY_ID").getInt());
                    rsHLot.updateString("ISSUE_NO", getAttribute("ISSUE_NO").getString());
                    rsHLot.updateInt("ISSUE_SR_NO", i);
                    rsHLot.updateInt("ISSUE_TYPE", 2);
                    rsHLot.updateInt("SR_NO", l);
                    rsHLot.updateString("ITEM_ID", ObjLot.getAttribute("ITEM_ID").getString());
                    rsHLot.updateString("ITEM_LOT_NO", ObjLot.getAttribute("ITEM_LOT_NO").getString());
                    rsHLot.updateString("AUTO_LOT_NO", ObjLot.getAttribute("AUTO_LOT_NO").getString());
                    rsHLot.updateDouble("ISSUED_LOT_QTY", ObjLot.getAttribute("ISSUED_LOT_QTY").getDouble());
                    rsHLot.updateDouble("ISSUE_RATE", ObjLot.getAttribute("ISSUE_RATE").getDouble());
                    rsHLot.updateBoolean("APPROVED", false);
                    rsHLot.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsHLot.updateBoolean("CANCELLED", false);
                    rsHLot.updateBoolean("CHANGED", true);
                    rsHLot.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsHLot.insertRow();
                }
            }

            ObjFlow.CompanyID = (int) getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID = 15; //QUOTATION MODULE ID
            ObjFlow.DocNo = (String) getAttribute("ISSUE_NO").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "D_INV_ISSUE_HEADER";
            ObjFlow.IsCreator = true;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REMARKS").getObj();
            ObjFlow.FieldName = "ISSUE_NO";
            ObjFlow.DocDate = (String) getAttribute("ISSUE_DATE").getObj();
            
            if (ObjFlow.Status.equals("H")) {
                ObjFlow.Status = "A";
                ObjFlow.To = ObjFlow.From;
                ObjFlow.UpdateFlow();
            } else {
                if (!ObjFlow.UpdateFlow()) {
                    LastError = ObjFlow.LastError;
                }
            }

            //===============Lot related =============//
            if (AStatus.equals("F")) {

                String SQL = "SELECT * FROM D_INV_ISSUE_LOT WHERE ISSUE_NO='" + getAttribute("ISSUE_NO").getString() + "' ORDER BY ISSUE_NO,ISSUE_SR_NO,SR_NO";
                ResultSet rsLotUpdate = data.getResult(SQL);
                rsLotUpdate.first();
                while (!rsLotUpdate.isAfterLast()) {
                    int IssueSrNo = rsLotUpdate.getInt("ISSUE_SR_NO");
                    int SrNo = rsLotUpdate.getInt("SR_NO");
                    String UpdateRecord = "UPDATE D_INV_ISSUE_LOT SET APPROVED=1, APPROVED_DATE='" + EITLERPGLOBAL.getCurrentDateDB() + "', "
                            + "CHANGED=1, CHANGED_DATE='" + EITLERPGLOBAL.getCurrentDateDB() + "' WHERE ISSUE_NO='" + getAttribute("ISSUE_NO").getString() + "' AND ISSUE_SR_NO=" + IssueSrNo + " AND SR_NO=" + SrNo + " ";
                    data.Execute(UpdateRecord);

                    UpdateRecord = "UPDATE D_INV_ISSUE_LOT_H SET APPROVED=1, APPROVED_DATE='" + EITLERPGLOBAL.getCurrentDateDB() + "', "
                            + "CHANGED=1, CHANGED_DATE='" + EITLERPGLOBAL.getCurrentDateDB() + "' WHERE ISSUE_NO='" + getAttribute("ISSUE_NO").getString() + "' AND ISSUE_SR_NO=" + IssueSrNo + " AND SR_NO=" + SrNo + " ";
                    data.Execute(UpdateRecord);

                    rsLotUpdate.next();
                }
                SQL = "INSERT INTO PRODUCTION.GIDC_FELT_RM_GRN "
                        + "(GRN_NO,GRN_DATE,ITEM_CODE,ITEM_DESCRIPTION,QTY,UNIT,COST_CENTER_ID,"
                        + "APPROVED,CANCELED,APPROVED_DATE,USED_QTY,WASTE_QTY,AVAILABLE_QTY,ISSUE_SR_NO) "
                        + "SELECT D.ISSUE_NO,D.CREATED_DATE,D.ITEM_CODE,M.ITEM_DESCRIPTION,D.QTY,P.DESC,D.COST_CENTER_ID,"
                        + "1,0,CURDATE(),0.0,0.0,D.QTY,D.SR_NO "
                        + " FROM DINESHMILLS.D_INV_ISSUE_DETAIL D "
                        + " LEFT JOIN DINESHMILLS.D_INV_ITEM_MASTER M "
                        + " ON D.ITEM_CODE=M.ITEM_ID "
                        + " LEFT JOIN DINESHMILLS.D_COM_PARAMETER_MAST P "
                        + " ON D.UNIT=P.PARA_CODE "
                        + " WHERE P.PARA_ID='UNIT' AND ISSUE_NO='" + getAttribute("ISSUE_NO").getString() + "' "
                        + " AND D.COST_CENTER_ID='8888'  "
                        + " ORDER BY D.SR_NO";
                data.Execute(SQL);
                SQL = "UPDATE PRODUCTION.GIDC_FELT_RM_STOCK S,"
                        + "(SELECT ITEM_CODE,SUM(QTY) AS INWARD FROM DINESHMILLS.D_INV_ISSUE_DETAIL "
                        + "WHERE ISSUE_NO='" + getAttribute("ISSUE_NO").getString() + "' "
                        + " AND COST_CENTER_ID='8888' "
                        + "AND ITEM_CODE IN (SELECT ITEM_CODE FROM PRODUCTION.GIDC_FELT_RM_STOCK)"
                        + "GROUP BY ITEM_CODE "
                        + ") I "
                        + "SET S.INWARD=S.INWARD+I.INWARD,S.AVAILABLE=S.AVAILABLE+I.INWARD "
                        + "WHERE S.ITEM_CODE=I.ITEM_CODE ";
                data.Execute(SQL);
                SQL = "INSERT INTO PRODUCTION.GIDC_FELT_RM_STOCK (ITEM_CODE,INWARD,USED,WASTE,AVAILABLE) "
                        + "SELECT ITEM_CODE,SUM(QTY),0,0,SUM(QTY) "
                        + "FROM DINESHMILLS.D_INV_ISSUE_DETAIL "
                        + "WHERE ISSUE_NO='" + getAttribute("ISSUE_NO").getString() + "' "
                        + " AND COST_CENTER_ID='8888' "
                        + "AND ITEM_CODE NOT IN (SELECT ITEM_CODE FROM PRODUCTION.GIDC_FELT_RM_STOCK)"
                        + "GROUP BY ITEM_CODE";
                data.Execute(SQL);

            }
            //====================================================//

            MoveLast();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean Update() {
        Statement stTmp, stHistory, stHDetail, stLot, stHLot;
        ResultSet rsTmp, rsHistory, rsHDetail, rsLot, rsHLot;
        Statement stHeader;
        ResultSet rsHeader;

        String strSQL = "", Matno = "";
        int MatSrno = 0;
        String DocNo = "";

        try {
            for (int i = 1; i <= colLineItems.size(); i++) {
                clsIssueGenItem ObjItem = (clsIssueGenItem) colLineItems.get(Integer.toString(i));
                if (ObjItem.getAttribute("ISSUE_VALUE").getDouble() <= 0) {
                    int Conform = JOptionPane.showConfirmDialog(null, "Total Value for the Item:" + ObjItem.getAttribute("ITEM_CODE").getString() + " is Zero \n" + " Do You want To continue...", "Message..", JOptionPane.YES_NO_OPTION);
                    if (Conform != JOptionPane.YES_OPTION) {
                        return false;
                    }
                }
            }
            String AStatus = (String) getAttribute("APPROVAL_STATUS").getObj();

            // ---- History Related Changes ------ //
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHDetail = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHLot = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rsHistory = stHistory.executeQuery("SELECT * FROM D_INV_ISSUE_HEADER_H WHERE ISSUE_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail = stHDetail.executeQuery("SELECT * FROM D_INV_ISSUE_DETAIL_H WHERE ISSUE_NO='1'");
            rsHDetail.first();
            rsHLot = stHLot.executeQuery("SELECT * FROM D_INV_ISSUE_LOT_H WHERE ISSUE_NO='1'");
            rsHLot.first();

            //------------------------------------//
            DocNo = (String) getAttribute("ISSUE_NO").getObj();

            //No Primary Keys will be updated & Updating of Header starts
            //=========== Check the Quantities entered against Rejection.============= //
            for (int i = 1; i <= colLineItems.size(); i++) {
                clsIssueGenItem ObjItem = (clsIssueGenItem) colLineItems.get(Integer.toString(i));

                Matno = (String) ObjItem.getAttribute("REQ_NO").getObj();
                MatSrno = (int) ObjItem.getAttribute("REQ_SRNO").getVal();
                double MatQty = 0;
                double PrevQty = 0; //Previously Entered Qty against RJN
                double CurrentQty = 0; //Currently entered Qty.

                if ((!Matno.trim().equals("")) && (MatSrno > 0)) //Material Req. No. entered
                {
                    //Get the  Rejection Qty.
                    stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    strSQL = "SELECT REQ_QTY AS QTY FROM D_INV_ISSUE_REQ_DETAIL WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID) + " AND REQ_NO='" + Matno + "' AND SR_NO=" + MatSrno;
                    rsTmp = stTmp.executeQuery(strSQL);
                    rsTmp.first();

                    if (rsTmp.getRow() > 0) {
                        MatQty = rsTmp.getDouble("QTY");
                    }

                    //Get Total Qty Entered in GRN Against this MIR No.
                    PrevQty = 0;
                    stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    strSQL = "SELECT SUM(QTY) AS SUMQTY FROM D_INV_ISSUE_DETAIL WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID) + " AND REQ_NO='" + Matno + "' AND REQ_SRNO=" + MatSrno + " AND ISSUE_NO<>'" + DocNo + "' AND ISSUE_NO NOT IN(SELECT ISSUE_NO FROM D_INV_ISSUE_HEADER WHERE CANCELED=1) ";
                    rsTmp = stTmp.executeQuery(strSQL);
                    rsTmp.first();

                    if (rsTmp.getRow() > 0) {
                        PrevQty = rsTmp.getDouble("SUMQTY");
                    }

                    CurrentQty = ObjItem.getAttribute("QTY").getVal();

                    if ((CurrentQty + PrevQty) > MatQty) //If total Qty exceeds MIR Qty. Do not allow
                    {
                        LastError = "Total quantity entered " + (CurrentQty + PrevQty) + " exceeds Req No. " + Matno + " Sr. No. " + MatSrno + " qty " + MatQty + ". Please verify the input.";
                        return false;
                    }
                }
            }

            String docDate = (String) getAttribute("ISSUE_DATE").getObj();

            //=========== Check the Quantities entered against MIR.============= //
            for (int i = 1; i <= colLineItems.size(); i++) {
                clsIssueGenItem ObjItem = (clsIssueGenItem) colLineItems.get(Integer.toString(i));

                String MIRNo = (String) ObjItem.getAttribute("MIR_NO").getObj();
                int SrNo = (int) ObjItem.getAttribute("MIR_SR_NO").getVal();
                int MIRType = (int) ObjItem.getAttribute("MIR_TYPE").getVal();

                double MIRQty = 0;
                double PrevQty = 0; //Previously Entered Qty against MIR
                double CurrentQty = 0; //Currently entered Qty.

                if ((!MIRNo.trim().equals("")) && (SrNo > 0)) {
                    stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    strSQL = "SELECT QTY FROM D_INV_MIR_DETAIL WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MIR_NO='" + MIRNo + "' AND SR_NO=" + SrNo + " AND MIR_TYPE=" + MIRType;
                    rsTmp = stTmp.executeQuery(strSQL);
                    rsTmp.first();

                    if (rsTmp.getRow() > 0) {
                        MIRQty = rsTmp.getDouble("QTY");
                    }

                    PrevQty = 0;
                    stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    strSQL = "SELECT SUM(QTY) AS SUMQTY FROM D_INV_ISSUE_DETAIL WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MIR_NO='" + MIRNo + "' AND MIR_SR_NO=" + SrNo + " AND MIR_TYPE=" + MIRType + " AND ISSUE_NO<>'" + DocNo + "'";
                    rsTmp = stTmp.executeQuery(strSQL);
                    rsTmp.first();

                    if (rsTmp.getRow() > 0) {
                        PrevQty = rsTmp.getDouble("SUMQTY");
                    }

                    CurrentQty = ObjItem.getAttribute("QTY").getVal();

                    if ((CurrentQty + PrevQty) > MIRQty) //If total Qty exceeds MIR Qty. Do not allow
                    {
                        LastError = "Total quantity entered " + (CurrentQty + PrevQty) + " exceeds MIR No. " + MIRNo + " Sr. No. " + SrNo + " qty " + MIRQty + ". Please verify the input.";
                        //return false;
                    }
                }
            }
            //================================ Completed ============================//

            // Update the Stock only after Final Approval //
            String gType = (String) getAttribute("ISSUE_TYPE").getObj();
            boolean LotItem = (boolean) getAttribute("ISSUE_WITH_LOT").getBool();

            //======== Stock Checking ========//
            //if(AStatus.equals("F")) {
            for (int i = 1; i <= colLineItems.size(); i++) {
                clsIssueGenItem ObjItem = (clsIssueGenItem) colLineItems.get(Integer.toString(i));

                String ItemID = (String) ObjItem.getAttribute("ITEM_CODE").getObj();

                clsStockInfo objStock = (new clsItemStock()).getOnHandQtyOn(EITLERPGLOBAL.gCompanyID, ItemID, docDate);
                double OnHandQty = objStock.StockQty;
                double MinQty = clsItem.getMinQty(EITLERPGLOBAL.gCompanyID, ItemID);

                //========= New Code of Allow Excess Issue ===========//
                String MIRNo = (String) ObjItem.getAttribute("MIR_NO").getObj();
                int MIRSrNo = (int) ObjItem.getAttribute("MIR_SR_NO").getVal();
                boolean AllowExcess = false;

                if (!MIRNo.equals("") && MIRSrNo > 0) {
                    //Allow Excess Quantity Issue in case MIR no. is there
                    AllowExcess = true;
                }

                //Forcefully Allow Excess Qty
                AllowExcess = false;
                //====================================================//

                if (MinQty > 0) //Minimum level maintained
                {
                    double CurrentQty = ObjItem.getAttribute("QTY").getVal();

                    //Get the sum of all repeated item codes
                    for (int j = 1; j <= colLineItems.size(); j++) {
                        clsIssueGenItem ObjItemEx = (clsIssueGenItem) colLineItems.get(Integer.toString(j));
                        if (ObjItemEx.getAttribute("ITEM_CODE").getObj().toString().equals(ItemID) && j != i) {
                            CurrentQty += ObjItemEx.getAttribute("QTY").getVal();
                        }
                    }

                    if (!AStatus.equals("R")) {
                        if ((OnHandQty - CurrentQty) < MinQty && !AllowExcess) {
                            LastError = "Cannot deduct stock for item " + ItemID + ". Stock level reached at minimum stock level";
                            return false; //Temporarily Suppressed
                        }
                    }
                } else {
                    double CurrentQty = ObjItem.getAttribute("QTY").getVal();

                    //Get the sum of all repeated item codes
                    for (int j = 1; j <= colLineItems.size(); j++) {
                        clsIssueGenItem ObjItemEx = (clsIssueGenItem) colLineItems.get(Integer.toString(j));
                        if (ObjItemEx.getAttribute("ITEM_CODE").getObj().toString().equals(ItemID) && j != i) {
                            CurrentQty += ObjItemEx.getAttribute("QTY").getVal();
                        }
                    }

                    if (!AStatus.equals("R")) {
                        if ((OnHandQty - CurrentQty) < 0 && !AllowExcess) {
                            LastError = "Cannot deduct stock for item " + ItemID + ". Stock level reached at zero level. And no MIR no. specified.";
                            return false; //Temporarily suppressed
                        }
                    }
                }
            }
            //}
            //======== Stock checking over ========//

            // Stock Updataion started over here
            //=======================================//
            if (AStatus.equals("F")) {
                //-------- First Update the stock -------------//
                for (int i = 1; i <= colLineItems.size(); i++) {
                    clsIssueGenItem ObjItem = (clsIssueGenItem) colLineItems.get(Integer.toString(i));

                    double TotalZeroValQty = 0;

                    Matno = (String) ObjItem.getAttribute("REQ_NO").getObj();
                    MatSrno = (int) ObjItem.getAttribute("REQ_SRNO").getVal();
                    double Qty = ObjItem.getAttribute("QTY").getVal();
                    String ItemID = (String) ObjItem.getAttribute("ITEM_CODE").getObj();
                    String BOENo = (String) ObjItem.getAttribute("BOE_NO").getObj();
                    String WareHouseID = (String) ObjItem.getAttribute("WAREHOUSE_ID").getObj();
                    String LocationID = (String) ObjItem.getAttribute("LOCATION_ID").getObj();

                    data.Execute("UPDATE D_INV_ISSUE_REQ_DETAIL SET ISSUED_QTY=ISSUED_QTY+" + Qty + ",BAL_QTY=REQ_QTY-ISSUED_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND REQ_NO='" + Matno.trim() + "' AND SR_NO=" + MatSrno);
                    data.Execute("UPDATE D_INV_ISSUE_REQ_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND REQ_NO='" + Matno.trim() + "'");

                    for (int l = 1; l <= ObjItem.colItemLot.size(); l++) {
                        clsIssueLot ObjLot = (clsIssueLot) ObjItem.colItemLot.get(Integer.toString(l));

                        String ItemLotNo = ObjLot.getAttribute("ITEM_LOT_NO").getString();
                        String AutoLotNo = ObjLot.getAttribute("AUTO_LOT_NO").getString();
                        data.Execute("UPDATE D_INV_GRN_LOT SET BALANCE_QTY=BALANCE_QTY-" + Qty + ",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND ITEM_LOT_NO='" + ItemLotNo + "' AND AUTO_LOT_NO='" + AutoLotNo + "'");
                    }
                    //======= Free Allocation =======//
                    int DeptID = (int) ObjItem.getAttribute("DEPT_ID").getVal();
                    String IssueNo = (String) getAttribute("ISSUE_NO").getObj();

                    clsAllocation ObjAllocation = new clsAllocation();
                    ObjAllocation.freeAllocation(EITLERPGLOBAL.gCompanyID, DeptID, ItemID, Qty, IssueNo, i);
                    //=================================================================//

                    //======== New Code of Excess Issue of Item ====================//
                    //=============================================================//
                    clsStockInfo objStock = (new clsItemStock()).getOnHandQtyOn(EITLERPGLOBAL.gCompanyID, ItemID, docDate);
                    double OnHandQty = objStock.StockQty;
                    double ExcessQty = 0;
                    double AddExcess = 0;

                    if (Qty > OnHandQty) {
                        ExcessQty = Qty - OnHandQty;

                        AddExcess = 0;

                        //First find out the duplication of Item Code in Issue Detail
                        for (int a = 1; a <= colLineItems.size(); a++) {
                            clsIssueGenItem ObjTempItem = (clsIssueGenItem) colLineItems.get(Integer.toString(a));

                            String tmpItemID = (String) ObjTempItem.getAttribute("ITEM_CODE").getObj();
                            if (a != i && tmpItemID.trim().equals(ItemID)) {
                                AddExcess = ObjTempItem.getAttribute("EXCESS_ISSUE_QTY").getVal();
                            }
                        }
                        //========================================================//

                        ExcessQty += AddExcess;
                    }

                    //Actual Excess Qty for the Line of Issue
                    if (Qty > OnHandQty) {
                        ExcessQty = Qty - OnHandQty;

                        //Bring down the Issue Qty to On Hand Qty - Do not negate the stock //
                        Qty = OnHandQty;

                    } else {
                        ExcessQty = 0;
                    }

                    //Update the Excess Issue Qty.
                    ObjItem.setAttribute("EXCESS_ISSUE_QTY", ExcessQty);
                    //===================================================================//

                    if (BOENo.trim().equals("")) {
                        BOENo = "X";
                    }

                    //Update the MIR
                    String MIRNo = (String) ObjItem.getAttribute("MIR_NO").getObj();
                    int MIRSrNo = (int) ObjItem.getAttribute("MIR_SR_NO").getVal();
                    int MIRType = (int) ObjItem.getAttribute("MIR_TYPE").getVal();

                    if (!MIRNo.trim().equals("")) {
                        data.Execute("UPDATE D_INV_MIR_DETAIL SET ISSUED_QTY=ISSUED_QTY+" + Qty + ",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MIR_NO='" + MIRNo + "' AND SR_NO=" + MIRSrNo + " AND MIR_TYPE=" + MIRType);
                        data.Execute("UPDATE D_INV_MIR_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MIR_NO='" + MIRNo + "'");
                    }

                    Statement tmpStmt;
                    tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

                    //Check that ITEM_ID+LOT_NO+BOE_NO entry exist in D_INV_ITEM_LOST_MASTER
                    Statement tmpStmt1;
                    tmpStmt1 = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    rsTmp = tmpStmt1.executeQuery("SELECT * FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID=" + Integer.toString(EITLERPGLOBAL.gCompanyID) + " AND ITEM_ID='" + ItemID.trim() + "' AND  BOE_NO='" + BOENo + "' AND WAREHOUSE_ID='" + WareHouseID + "' AND LOCATION_ID='" + LocationID + "' AND LOT_NO='X'");
                    rsTmp.first();

                    //Check that entry exist. Else create a new record
                    if (rsTmp.getRow() <= 0) {
                        rsTmp.moveToInsertRow();
                        rsTmp.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        rsTmp.updateString("ITEM_ID", ItemID);
                        rsTmp.updateString("LOT_NO", "X");
                        rsTmp.updateString("BOE_NO", BOENo);
                        rsTmp.updateString("BOE_SR_NO", (String) ObjItem.getAttribute("BOE_SR_NO").getObj());
                        rsTmp.updateString("WAREHOUSE_ID", WareHouseID);
                        rsTmp.updateString("LOCATION_ID", LocationID);
                        rsTmp.updateBoolean("CHANGED", true);
                        rsTmp.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDate());
                        rsTmp.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                        rsTmp.updateInt("MODIFIED_BY", EITLERPGLOBAL.gUserID);
                        rsTmp.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                        rsTmp.updateInt("CREATED_BY", EITLERPGLOBAL.gUserID);
                        rsTmp.updateDouble("ALLOCATED_QTY", 0);
                        rsTmp.updateDouble("AVAILABLE_QTY", 0);
                        rsTmp.updateDouble("ON_HAND_QTY", 0);
                        rsTmp.updateDouble("REJECTED_QTY", 0);
                        rsTmp.updateDouble("ZERO_VAL_QTY", 0);
                        rsTmp.updateDouble("ZERO_ISSUED_QTY", 0);
                        rsTmp.updateDouble("ZERO_RECEIPT_QTY", 0);
                        rsTmp.updateDouble("ZERO_OPENING_QTY", 0);
                        rsTmp.updateString("LAST_ISSUED_DATE", "0000-00-00");
                        rsTmp.updateString("LAST_RECEIPT_DATE", "0000-00-00");
                        rsTmp.updateDouble("TOTAL_ISSUED_QTY", 0);
                        rsTmp.updateDouble("TOTAL_RECEIPT_QTY", 0);
                        rsTmp.updateDouble("OPENING_RATE", 0);
                        rsTmp.updateDouble("OPENING_QTY", 0);
                        rsTmp.insertRow();
                    }

                    //Again Querying
                    tmpStmt1 = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    rsTmp = tmpStmt1.executeQuery("SELECT * FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID=" + Integer.toString(EITLERPGLOBAL.gCompanyID) + " AND ITEM_ID='" + ItemID.trim() + "' AND  BOE_NO='" + BOENo + "' AND WAREHOUSE_ID='" + WareHouseID + "' AND LOCATION_ID='" + LocationID + "' AND LOT_NO='X'");
                    rsTmp.first();

                    // Updating tables
                    if (clsItem.getMaintainStock(EITLERPGLOBAL.gCompanyID, ItemID)) {
                        double ActualIssQty = Qty;
                        double ZeroIssQty = 0;

                        //===== New Code of Zero Value Issue Qty ----------//
                        if (rsTmp.getRow() > 0) {
                            double ZeroValQty = rsTmp.getDouble("ZERO_VAL_QTY");

                            if (ZeroValQty > Qty) {
                                ActualIssQty = 0;
                                ZeroIssQty = Qty;

                                //Update the Zero Value Qty.
                                rsTmp.updateDouble("ZERO_ISSUED_QTY", rsTmp.getDouble("ZERO_ISSUED_QTY") + Qty);
                                rsTmp.updateDouble("ZERO_VAL_QTY", rsTmp.getDouble("ZERO_VAL_QTY") - Qty);

                                TotalZeroValQty += ZeroIssQty;
                            } else {
                                ActualIssQty = Qty - ZeroValQty;
                                ZeroIssQty = Qty - ActualIssQty;

                                //Update the Zero Value Qty.
                                rsTmp.updateDouble("ZERO_ISSUED_QTY", rsTmp.getDouble("ZERO_ISSUED_QTY") + ZeroIssQty);
                                rsTmp.updateDouble("ZERO_VAL_QTY", 0);

                                TotalZeroValQty += ZeroIssQty;
                            }
                        }
                        //================================================================================//

                        //Now Calculate the Actual Issue Value after Zero Val Qty deduction //
                        double Rate = ObjItem.getAttribute("RATE").getVal();
                        double IssueValue = ActualIssQty * Rate;
                        //ObjItem.setAttribute("ISSUE_VALUE",IssueValue);
                        //================================================================= //

                        rsTmp.updateDouble("TOTAL_ISSUED_QTY", rsTmp.getDouble("TOTAL_ISSUED_QTY") + ActualIssQty);
                        rsTmp.updateString("LAST_ISSUED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                        rsTmp.updateDouble("ON_HAND_QTY", rsTmp.getDouble("ON_HAND_QTY") - Qty);
                        rsTmp.updateDouble("AVAILABLE_QTY", rsTmp.getDouble("AVAILABLE_QTY") - Qty);
                        rsTmp.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                        rsTmp.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                        rsTmp.updateBoolean("CHANGED", true);
                        rsTmp.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                        rsTmp.updateRow();
                    }

                    ObjItem.setAttribute("ZERO_VAL_QTY", TotalZeroValQty);
                    colLineItems.put(Integer.toString(i), ObjItem);
                }
            }

            ApprovalFlow ObjFlow = new ApprovalFlow();

            rsIssue.updateString("FOR_STORE", "R");
            rsIssue.updateLong("FOR_DEPT_ID", (long) getAttribute("FOR_DEPT_ID").getVal());
            rsIssue.updateString("ISSUE_TYPE", (String) getAttribute("ISSUE_TYPE").getObj());
            rsIssue.updateString("BLEND_CODE", (String) getAttribute("BLEND_CODE").getObj());
            rsIssue.updateString("PURPOSE", (String) getAttribute("PURPOSE").getObj());
            rsIssue.updateString("REMARKS", (String) getAttribute("REMARKS").getObj());
            rsIssue.updateBoolean("CANCELED", (boolean) getAttribute("CANCELED").getBool());
            rsIssue.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsIssue.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            rsIssue.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
            rsIssue.updateLong("MODIFIED_BY", (long) getAttribute("MODIFIED_BY").getVal());
            rsIssue.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
            rsIssue.updateBoolean("CHANGED", true);
            rsIssue.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsIssue.updateBoolean("CANCELED", false);
            rsIssue.updateRow();

            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_INV_ISSUE_HEADER_H WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND ISSUE_NO='" + (String) getAttribute("ISSUE_NO").getObj() + "'");
            RevNo++;
            String RevDocNo = (String) getAttribute("ISSUE_NO").getObj();

            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO", RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY", (int) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS", (String) getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS", (String) getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID", (long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("ISSUE_NO", (String) getAttribute("ISSUE_NO").getObj());
            rsHistory.updateString("ISSUE_DATE", (String) getAttribute("ISSUE_DATE").getObj());
            rsHistory.updateString("FOR_STORE", "R");
            rsHistory.updateLong("FOR_DEPT_ID", (long) getAttribute("FOR_DEPT_ID").getVal());
            rsHistory.updateString("ISSUE_TYPE", (String) getAttribute("ISSUE_TYPE").getObj());
            rsHistory.updateString("BLEND_CODE", (String) getAttribute("BLEND_CODE").getObj());
            rsHistory.updateString("PURPOSE", (String) getAttribute("PURPOSE").getObj());
            rsHistory.updateBoolean("ISSUE_WITH_LOT", (boolean) getAttribute("ISSUE_WITH_LOT").getBool());
            rsHistory.updateString("REMARKS", (String) getAttribute("REMARKS").getObj());
            rsHistory.updateBoolean("CANCELED", (boolean) getAttribute("CANCELED").getBool());
            rsHistory.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY", (long) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateBoolean("CHANGED", true);
            rsHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();

            // Inserting records in ISSUE Details
            Statement tmpStmt;
            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String nIssueno = (String) getAttribute("ISSUE_NO").getObj();
            String Del_Query = "DELETE FROM D_INV_ISSUE_DETAIL WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID) + " AND ISSUE_NO='" + nIssueno.trim() + "'";
            data.Execute(Del_Query);

            data.Execute("DELETE FROM D_INV_ISSUE_LOT WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND ISSUE_NO='" + nIssueno + "' AND ISSUE_TYPE=2");

            rsTmp = tmpStmt.executeQuery("SELECT * FROM D_INV_ISSUE_DETAIL WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID) + " AND ISSUE_NO='1'");
            String nIssuetype = (String) getAttribute("ISSUE_TYPE").getObj();
            long nCompanyID = (long) getAttribute("COMPANY_ID").getVal();
            String nIssue = (String) getAttribute("ISSUE_NO").getObj();

            stLot = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsLot = stLot.executeQuery("SELECT * FROM D_INV_ISSUE_LOT WHERE ISSUE_NO='1'");
            rsLot.first();

            for (int i = 1; i <= colLineItems.size(); i++) {
                clsIssueGenItem ObjItem = (clsIssueGenItem) colLineItems.get(Integer.toString(i));
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID", nCompanyID);
                rsTmp.updateString("ISSUE_NO", nIssueno);
                rsTmp.updateLong("SR_NO", i);
                rsTmp.updateString("ITEM_CODE", (String) ObjItem.getAttribute("ITEM_CODE").getObj());
                rsTmp.updateString("ITEM_EXTRA_DESC", (String) ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsTmp.updateString("WAREHOUSE_ID", (String) ObjItem.getAttribute("WAREHOUSE_ID").getObj());
                rsTmp.updateString("LOCATION_ID", (String) ObjItem.getAttribute("LOCATION_ID").getObj());
                rsTmp.updateString("BOE_NO", (String) ObjItem.getAttribute("BOE_NO").getObj());
                rsTmp.updateString("BOE_SR_NO", (String) ObjItem.getAttribute("BOE_SR_NO").getObj());
                rsTmp.updateDouble("QTY", ObjItem.getAttribute("QTY").getVal());
                rsTmp.updateDouble("QTY_REQD", ObjItem.getAttribute("QTY_REQD").getDouble());
                rsTmp.updateDouble("EXCESS_ISSUE_QTY", ObjItem.getAttribute("EXCESS_ISSUE_QTY").getVal());
                rsTmp.updateLong("UNIT", (long) ObjItem.getAttribute("UNIT").getVal());
                rsTmp.updateLong("COST_CENTER_ID", (long) ObjItem.getAttribute("COST_CENTER_ID").getVal());
                rsTmp.updateString("SHADE", (String) ObjItem.getAttribute("SHADE").getObj());
                rsTmp.updateString("ISSUE_DESC", (String) ObjItem.getAttribute("ISSUE_DESC").getObj());
                rsTmp.updateString("REQ_NO", (String) ObjItem.getAttribute("REQ_NO").getObj());
                rsTmp.updateLong("REQ_SRNO", (long) ObjItem.getAttribute("REQ_SRNO").getVal());
                rsTmp.updateString("REMARKS", (String) ObjItem.getAttribute("REMARKS").getObj());
                rsTmp.updateString("LF_NO", (String) ObjItem.getAttribute("LF_NO").getObj());
                rsTmp.updateBoolean("CANCELED", (boolean) ObjItem.getAttribute("CANCELED").getBool());
                rsTmp.updateLong("CREATED_BY", (long) ObjItem.getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE", (String) ObjItem.getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY", (long) ObjItem.getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE", (String) ObjItem.getAttribute("MODIFEID_DATE").getObj());
                rsTmp.updateInt("DEPT_ID", (int) ObjItem.getAttribute("DEPT_ID").getVal());
                rsTmp.updateString("GRN_NO", (String) ObjItem.getAttribute("GRN_NO").getObj());
                rsTmp.updateInt("GRN_SR_NO", (int) ObjItem.getAttribute("GRN_SR_NO").getVal());
                rsTmp.updateInt("GRN_TYPE", (int) ObjItem.getAttribute("GRN_TYPE").getVal());
                rsTmp.updateString("MIR_NO", (String) ObjItem.getAttribute("MIR_NO").getObj());
                rsTmp.updateInt("MIR_SR_NO", (int) ObjItem.getAttribute("MIR_SR_NO").getVal());
                rsTmp.updateInt("MIR_TYPE", (int) ObjItem.getAttribute("MIR_TYPE").getVal());
                rsTmp.updateDouble("ZERO_VAL_QTY", ObjItem.getAttribute("ZERO_VAL_QTY").getVal());
                rsTmp.updateDouble("ISSUE_VALUE", ObjItem.getAttribute("ISSUE_VALUE").getVal());
                rsTmp.updateDouble("RATE", ObjItem.getAttribute("RATE").getVal());
                rsTmp.updateDouble("STOCK_QTY", ObjItem.getAttribute("STOCK_QTY").getVal());
                rsTmp.updateBoolean("CHANGED", true);
                rsTmp.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateString("LOT_NO", "X");
                rsTmp.updateString("BLEND_CODE", (String) ObjItem.getAttribute("BLEND_CODE").getObj());
                rsTmp.updateString("MATERIAL_CODE_NO", (String) ObjItem.getAttribute("MATERIAL_CODE_NO").getObj());
                rsTmp.updateString("MFG_PROG_NO", (String) ObjItem.getAttribute("MFG_PROG_NO").getObj());

                rsTmp.insertRow();

                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO", RevNo);
                rsHDetail.updateLong("COMPANY_ID", nCompanyID);
                rsHDetail.updateString("ISSUE_NO", nIssueno);
                rsHDetail.updateLong("SR_NO", i);
                rsHDetail.updateString("ITEM_CODE", (String) ObjItem.getAttribute("ITEM_CODE").getObj());
                rsHDetail.updateString("ITEM_EXTRA_DESC", (String) ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsHDetail.updateString("WAREHOUSE_ID", (String) ObjItem.getAttribute("WAREHOUSE_ID").getObj());
                rsHDetail.updateString("LOCATION_ID", (String) ObjItem.getAttribute("LOCATION_ID").getObj());
                rsHDetail.updateString("BOE_NO", (String) ObjItem.getAttribute("BOE_NO").getObj());
                rsHDetail.updateString("BOE_SR_NO", (String) ObjItem.getAttribute("BOE_SR_NO").getObj());
                rsHDetail.updateDouble("QTY", ObjItem.getAttribute("QTY").getVal());
                rsHDetail.updateDouble("QTY_REQD", ObjItem.getAttribute("QTY_REQD").getDouble());
                rsHDetail.updateDouble("EXCESS_ISSUE_QTY", ObjItem.getAttribute("EXCESS_ISSUE_QTY").getVal());
                rsHDetail.updateLong("UNIT", (long) ObjItem.getAttribute("UNIT").getVal());
                rsHDetail.updateLong("COST_CENTER_ID", (long) ObjItem.getAttribute("COST_CENTER_ID").getVal());
                rsHDetail.updateString("SHADE", (String) ObjItem.getAttribute("SHADE").getObj());
                rsHDetail.updateString("ISSUE_DESC", (String) ObjItem.getAttribute("ISSUE_DESC").getObj());
                rsHDetail.updateString("REQ_NO", (String) ObjItem.getAttribute("REQ_NO").getObj());
                rsHDetail.updateLong("REQ_SRNO", (long) ObjItem.getAttribute("REQ_SRNO").getVal());
                rsHDetail.updateString("REMARKS", (String) ObjItem.getAttribute("REMARKS").getObj());
                rsHDetail.updateString("LF_NO", (String) ObjItem.getAttribute("LF_NO").getObj());
                rsHDetail.updateBoolean("CANCELED", (boolean) ObjItem.getAttribute("CANCELED").getBool());
                rsHDetail.updateLong("CREATED_BY", (long) ObjItem.getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE", (String) ObjItem.getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY", (long) ObjItem.getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE", (String) ObjItem.getAttribute("MODIFEID_DATE").getObj());
                rsHDetail.updateInt("DEPT_ID", (int) ObjItem.getAttribute("DEPT_ID").getVal());
                rsHDetail.updateString("GRN_NO", (String) ObjItem.getAttribute("GRN_NO").getObj());
                rsHDetail.updateInt("GRN_SR_NO", (int) ObjItem.getAttribute("GRN_SR_NO").getVal());
                rsHDetail.updateInt("GRN_TYPE", (int) ObjItem.getAttribute("GRN_TYPE").getVal());
                rsHDetail.updateString("MIR_NO", (String) ObjItem.getAttribute("MIR_NO").getObj());
                rsHDetail.updateInt("MIR_SR_NO", (int) ObjItem.getAttribute("MIR_SR_NO").getVal());
                rsHDetail.updateInt("MIR_TYPE", (int) ObjItem.getAttribute("MIR_TYPE").getVal());
                rsHDetail.updateDouble("ZERO_VAL_QTY", ObjItem.getAttribute("ZERO_VAL_QTY").getVal());
                rsHDetail.updateDouble("ISSUE_VALUE", ObjItem.getAttribute("ISSUE_VALUE").getVal());
                rsHDetail.updateDouble("RATE", ObjItem.getAttribute("RATE").getVal());
                rsHDetail.updateDouble("STOCK_QTY", ObjItem.getAttribute("STOCK_QTY").getVal());
                rsHDetail.updateBoolean("CHANGED", true);
                rsHDetail.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("LOT_NO", "X");
                rsHDetail.updateString("BLEND_CODE", (String) ObjItem.getAttribute("BLEND_CODE").getObj());
                rsHDetail.updateString("MATERIAL_CODE_NO", (String) ObjItem.getAttribute("MATERIAL_CODE_NO").getObj());
                rsHDetail.updateString("MFG_PROG_NO", (String) ObjItem.getAttribute("MFG_PROG_NO").getObj());
                rsHDetail.insertRow();

                //Now insert lot nos.
                for (int l = 1; l <= ObjItem.colItemLot.size(); l++) {
                    clsIssueLot ObjLot = (clsIssueLot) ObjItem.colItemLot.get(Integer.toString(l));

                    rsLot.moveToInsertRow();
                    rsLot.updateInt("COMPANY_ID", getAttribute("COMPANY_ID").getInt());
                    rsLot.updateString("ISSUE_NO", getAttribute("ISSUE_NO").getString());
                    rsLot.updateInt("ISSUE_SR_NO", i);
                    rsLot.updateInt("ISSUE_TYPE", 2);
                    rsLot.updateInt("SR_NO", l);
                    rsLot.updateString("ITEM_ID", ObjLot.getAttribute("ITEM_ID").getString());
                    rsLot.updateString("ITEM_LOT_NO", ObjLot.getAttribute("ITEM_LOT_NO").getString());
                    rsLot.updateString("AUTO_LOT_NO", ObjLot.getAttribute("AUTO_LOT_NO").getString());
                    rsLot.updateDouble("ISSUED_LOT_QTY", ObjLot.getAttribute("ISSUED_LOT_QTY").getDouble());
                    rsLot.updateDouble("ISSUE_RATE", ObjLot.getAttribute("ISSUE_RATE").getDouble());
                    rsLot.updateBoolean("APPROVED", false);
                    rsLot.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsLot.updateBoolean("CANCELLED", false);
                    rsLot.updateBoolean("CHANGED", true);
                    rsLot.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsLot.insertRow();

                    rsHLot.moveToInsertRow();
                    rsHLot.updateInt("REVISION_NO", RevNo);
                    rsHLot.updateInt("COMPANY_ID", getAttribute("COMPANY_ID").getInt());
                    rsHLot.updateString("ISSUE_NO", getAttribute("ISSUE_NO").getString());
                    rsHLot.updateInt("ISSUE_SR_NO", i);
                    rsHLot.updateInt("ISSUE_TYPE", 2);
                    rsHLot.updateInt("SR_NO", l);
                    rsHLot.updateString("ITEM_ID", ObjLot.getAttribute("ITEM_ID").getString());
                    rsHLot.updateString("ITEM_LOT_NO", ObjLot.getAttribute("ITEM_LOT_NO").getString());
                    rsHLot.updateString("AUTO_LOT_NO", ObjLot.getAttribute("AUTO_LOT_NO").getString());
                    rsHLot.updateDouble("ISSUED_LOT_QTY", ObjLot.getAttribute("ISSUED_LOT_QTY").getDouble());
                    rsHLot.updateDouble("ISSUE_RATE", ObjLot.getAttribute("ISSUE_RATE").getDouble());
                    rsHLot.updateBoolean("APPROVED", false);
                    rsHLot.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsHLot.updateBoolean("CANCELLED", false);
                    rsHLot.updateBoolean("CHANGED", true);
                    rsHLot.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsHLot.insertRow();
                }
            }

            ObjFlow.CompanyID = (int) getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID = 15;
            ObjFlow.DocNo = (String) getAttribute("ISSUE_NO").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "D_INV_ISSUE_HEADER";
            ObjFlow.IsCreator = false;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REMARKS").getObj();
            ObjFlow.FieldName = "ISSUE_NO";
            ObjFlow.DocDate = (String) getAttribute("ISSUE_DATE").getObj();
            //==== Handling Rejected Documents ==========//
            if (AStatus.equals("R")) {
                //ObjFlow.IsCreator=true;
                ObjFlow.To = (int) getAttribute("SEND_DOC_TO").getVal();
                ObjFlow.ExplicitSendTo = true;
            }
            //==========================================//

            //==== Handling Rejected Documents ==========//
            if (ObjFlow.Status.equals("H")) {
                //Nothing to do
            } else {
                if (!ObjFlow.UpdateFlow()) {
                    LastError = ObjFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------

            //===============Lot related =============//
            if (AStatus.equals("F")) {

                String SQL = "SELECT * FROM D_INV_ISSUE_LOT WHERE ISSUE_NO='" + getAttribute("ISSUE_NO").getString() + "' ORDER BY ISSUE_NO,ISSUE_SR_NO,SR_NO";
                ResultSet rsLotUpdate = data.getResult(SQL);
                rsLotUpdate.first();
                while (!rsLotUpdate.isAfterLast()) {
                    int IssueSrNo = rsLotUpdate.getInt("ISSUE_SR_NO");
                    int SrNo = rsLotUpdate.getInt("SR_NO");
                    String UpdateRecord = "UPDATE D_INV_ISSUE_LOT SET APPROVED=1, APPROVED_DATE='" + EITLERPGLOBAL.getCurrentDateDB() + "', "
                            + "CHANGED=1, CHANGED_DATE='" + EITLERPGLOBAL.getCurrentDateDB() + "' WHERE ISSUE_NO='" + getAttribute("ISSUE_NO").getString() + "' AND ISSUE_SR_NO=" + IssueSrNo + " AND SR_NO=" + SrNo + " ";
                    data.Execute(UpdateRecord);

                    UpdateRecord = "UPDATE D_INV_ISSUE_LOT_H SET APPROVED=1, APPROVED_DATE='" + EITLERPGLOBAL.getCurrentDateDB() + "', "
                            + "CHANGED=1, CHANGED_DATE='" + EITLERPGLOBAL.getCurrentDateDB() + "' WHERE ISSUE_NO='" + getAttribute("ISSUE_NO").getString() + "' AND ISSUE_SR_NO=" + IssueSrNo + " AND SR_NO=" + SrNo + " ";
                    data.Execute(UpdateRecord);

                    rsLotUpdate.next();
                }
                SQL = "INSERT INTO PRODUCTION.GIDC_FELT_RM_GRN "
                        + "(GRN_NO,GRN_DATE,ITEM_CODE,ITEM_DESCRIPTION,QTY,UNIT,COST_CENTER_ID,"
                        + "APPROVED,CANCELED,APPROVED_DATE,USED_QTY,WASTE_QTY,AVAILABLE_QTY,ISSUE_SR_NO) "
                        + "SELECT D.ISSUE_NO,D.CREATED_DATE,D.ITEM_CODE,M.ITEM_DESCRIPTION,D.QTY,P.DESC,D.COST_CENTER_ID,"
                        + "1,0,CURDATE(),0.0,0.0,D.QTY,D.SR_NO "
                        + " FROM DINESHMILLS.D_INV_ISSUE_DETAIL D "
                        + " LEFT JOIN DINESHMILLS.D_INV_ITEM_MASTER M "
                        + " ON D.ITEM_CODE=M.ITEM_ID "
                        + " LEFT JOIN DINESHMILLS.D_COM_PARAMETER_MAST P "
                        + " ON D.UNIT=P.PARA_CODE "
                        + " WHERE P.PARA_ID='UNIT' AND ISSUE_NO='" + getAttribute("ISSUE_NO").getString() + "' "
                        + " AND D.COST_CENTER_ID='8888'  "
                        + " ORDER BY D.SR_NO";
                data.Execute(SQL);
                SQL = "UPDATE PRODUCTION.GIDC_FELT_RM_STOCK S,"
                        + "(SELECT ITEM_CODE,SUM(QTY) AS INWARD FROM DINESHMILLS.D_INV_ISSUE_DETAIL "
                        + "WHERE ISSUE_NO='" + getAttribute("ISSUE_NO").getString() + "' "
                        + " AND COST_CENTER_ID='8888' "
                        + "AND ITEM_CODE IN (SELECT ITEM_CODE FROM PRODUCTION.GIDC_FELT_RM_STOCK)"
                        + "GROUP BY ITEM_CODE "
                        + ") I "
                        + "SET S.INWARD=S.INWARD+I.INWARD,S.AVAILABLE=S.AVAILABLE+I.INWARD "
                        + "WHERE S.ITEM_CODE=I.ITEM_CODE ";
                data.Execute(SQL);
                SQL = "INSERT INTO PRODUCTION.GIDC_FELT_RM_STOCK (ITEM_CODE,INWARD,USED,WASTE,AVAILABLE) "
                        + "SELECT ITEM_CODE,SUM(QTY),0,0,SUM(QTY) "
                        + "FROM DINESHMILLS.D_INV_ISSUE_DETAIL "
                        + "WHERE ISSUE_NO='" + getAttribute("ISSUE_NO").getString() + "' "
                        + " AND COST_CENTER_ID='8888' "
                        + "AND ITEM_CODE NOT IN (SELECT ITEM_CODE FROM PRODUCTION.GIDC_FELT_RM_STOCK)"
                        + "GROUP BY ITEM_CODE";
                data.Execute(SQL);

            }
            //====================================================//

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    
    //This routine checks and returns whether the item is deletable or not
    //Criteria is Approved item cannot be deleted
    //and if not approved then user id is checked whether doucment
    //is created by the user. Only creator can delete the document
    public boolean CanDelete(int pCompanyID, String pIssueno, int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";

        try {
            if (HistoryView) {
                return false;
            }

            //First check that document is approved or not
            strSQL = "SELECT COUNT(*) AS COUNT FROM D_INV_ISSUE_HEADER WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND ISSUE_NO='" + pIssueno + "' AND APPROVED=true";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND MODULE_ID=15 AND DOC_NO='" + pIssueno + "' AND USER_ID=" + Integer.toString(pUserID) + " AND TYPE='C' AND STATUS='W'";
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

    //This routine checks and returns whether the item is editable or not
    //Criteria is Approved item cannot be changed
    //and if not approved then user id is checked whether doucment
    //is waiting for his approval.
    public boolean IsEditable(int pCompanyID, String pIssueno, int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";

        try {
            if (HistoryView) {
                return false;
            }

            //First check that document is approved or not
            strSQL = "SELECT COUNT(*) AS COUNT FROM D_INV_ISSUE_HEADER WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND ISSUE_NO='" + pIssueno + "' AND APPROVED=1";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND MODULE_ID=15 AND DOC_NO='" + pIssueno + "' AND USER_ID=" + Integer.toString(pUserID) + " AND STATUS='W'";
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
        try {
            int lCompanyID = (int) getAttribute("COMPANY_ID").getVal();
            String lIssueno = (String) getAttribute("ISSUE_NO").getObj();

            if (CanDelete(lCompanyID, lIssueno, pUserID)) {
                String strQry = "DELETE FROM D_INV_ISSUE_HEADER WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID) + " AND ISSUE_NO='" + lIssueno.trim() + "'";
                data.Execute(strQry);

                strQry = "DELETE FROM D_INV_ISSUE_DETAIL WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID) + " AND ISSUE_NO='" + lIssueno.trim() + "'";
                data.Execute(strQry);

                strQry = "DELETE FROM D_INV_ISSUE_LOT WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID) + " AND ISSUE_NO='" + lIssueno.trim() + "'";
                data.Execute(strQry);
            }
            rsIssue.refreshRow();
            return MoveLast();
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public Object getObject(int pCompanyID, String pDocNo) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND ISSUE_NO='" + pDocNo + "'";
        clsIssue ObjIssue = new clsIssue();
        ObjIssue.Filter(strCondition, pCompanyID);
        return ObjIssue;
    }

    public boolean Filter(String pCondition, long pCompanyID) {
        Ready = false;
        try {
            String strSql = "SELECT * FROM D_INV_ISSUE_HEADER " + pCondition;
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsIssue = Stmt.executeQuery(strSql);

            if (!rsIssue.first()) {
                strSql = "SELECT * FROM D_INV_ISSUE_HEADER WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND FOR_STORE='R' AND ISSUE_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND ISSUE_DATE<='" + EITLERPGLOBAL.FinToDateDB + "' ORDER BY ISSUE_NO";
                rsIssue = Stmt.executeQuery(strSql);
                Ready = true;
                MoveLast();
                return false;
            } else {
                Ready = true;
                MoveLast();
                return true;
            }
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public static HashMap getPendingApprovals(int pCompanyID, int pUserID, int pOrder, int FinYearFrom) {
        String strSQL = "";
        Connection tmpConn;
        tmpConn = data.getConn();
        ResultSet rsTmp;
        Statement tmpStmt;

        HashMap List = new HashMap();
        long Counter = 0;

        try {
            if (pOrder == EITLERPGLOBAL.OnRecivedDate) {
                strSQL = "SELECT D_INV_ISSUE_HEADER.ISSUE_NO,D_INV_ISSUE_HEADER.ISSUE_DATE,RECEIVED_DATE,D_INV_ISSUE_HEADER.FOR_DEPT_ID AS DEPT_ID FROM D_INV_ISSUE_HEADER,D_COM_DOC_DATA WHERE D_INV_ISSUE_HEADER.ISSUE_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_ISSUE_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_ISSUE_HEADER.COMPANY_ID=" + Integer.toString(pCompanyID) + " AND D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=15 ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT D_INV_ISSUE_HEADER.ISSUE_NO,D_INV_ISSUE_HEADER.ISSUE_DATE,RECEIVED_DATE,D_INV_ISSUE_HEADER.FOR_DEPT_ID AS DEPT_ID FROM D_INV_ISSUE_HEADER,D_COM_DOC_DATA WHERE D_INV_ISSUE_HEADER.ISSUE_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_ISSUE_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_ISSUE_HEADER.COMPANY_ID=" + Integer.toString(pCompanyID) + " AND D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=15 ORDER BY D_INV_ISSUE_HEADER.ISSUE_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT D_INV_ISSUE_HEADER.ISSUE_NO,D_INV_ISSUE_HEADER.ISSUE_DATE,RECEIVED_DATE,D_INV_ISSUE_HEADER.FOR_DEPT_ID AS DEPT_ID FROM D_INV_ISSUE_HEADER,D_COM_DOC_DATA WHERE D_INV_ISSUE_HEADER.ISSUE_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_ISSUE_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_ISSUE_HEADER.COMPANY_ID=" + Integer.toString(pCompanyID) + " AND D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=15 ORDER BY D_INV_ISSUE_HEADER.ISSUE_NO";
            }

            //strSQL="SELECT D_INV_ISSUE_HEADER.ISSUE_NO,D_INV_ISSUE_HEADER.ISSUE_DATE FROM D_INV_ISSUE_HEADER,D_COM_DOC_DATA WHERE D_INV_ISSUE_HEADER.ISSUE_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_ISSUE_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_ISSUE_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=15";
            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter = 0;
            while (!rsTmp.isAfterLast()) {
                if (EITLERPGLOBAL.isWithinDate(rsTmp.getString("ISSUE_DATE"), FinYearFrom)) {
                    Counter = Counter + 1;
                    clsIssueRaw ObjDoc = new clsIssueRaw();

                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("ISSUE_NO", rsTmp.getString("ISSUE_NO"));
                    ObjDoc.setAttribute("ISSUE_DATE", rsTmp.getString("ISSUE_DATE"));
                    ObjDoc.setAttribute("RECEIVED_DATE", rsTmp.getString("RECEIVED_DATE"));
                    ObjDoc.setAttribute("DEPT_ID", rsTmp.getInt("DEPT_ID"));
                    // ----------------- End of Header Fields ------------------------------------//

                    //Put the prepared user object into list
                    List.put(Long.toString(Counter), ObjDoc);
                }

                if (!rsTmp.isAfterLast()) {
                    rsTmp.next();
                }
            }//Out While

            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();

        } catch (Exception e) {
        }

        return List;
    }

    public HashMap getList(String pCondition) {
        ResultSet rsTmp, rsLot;
        Statement tmpStmt, stLot;

        ResultSet rsTmp1;
        Statement tmpStmt1;

        HashMap List = new HashMap();
        long Counter = 0;
        int Counter2 = 0;

        try {
            tmpStmt = Conn.createStatement();
            rsTmp = tmpStmt.executeQuery("SELECT * FROM D_INV_ISSUE_HEADER" + pCondition);

            Counter = 0;
            while (rsTmp.next()) {
                Counter = Counter + 1;
                clsIssue ObjIssue = new clsIssue();

                //Populate the user
                ObjIssue.setAttribute("COMPANY_ID", rsTmp.getLong("COMPANY_ID"));
                ObjIssue.setAttribute("ISSUE_NO", rsTmp.getString("ISSUE_NO"));
                ObjIssue.setAttribute("ISSUE_DATE", rsTmp.getString("ISSUE_DATE"));
                ObjIssue.setAttribute("ISSUE_TYPE", rsTmp.getString("ISSUE_TYPE"));
                ObjIssue.setAttribute("FOR_STORE", rsTmp.getLong("FOR_STORE"));
                ObjIssue.setAttribute("FOR_DEPT_ID", rsTmp.getBoolean("FOR_DEPT_ID"));
                ObjIssue.setAttribute("BLEND_CODE", rsTmp.getLong("BLEND_CODE"));
                ObjIssue.setAttribute("PURPOSE", rsTmp.getLong("PURPOSE"));
                ObjIssue.setAttribute("SUPP_ID", rsTmp.getString("SUPP_ID"));
                ObjIssue.setAttribute("APPROVED", rsTmp.getInt("APPROVED"));
                ObjIssue.setAttribute("APPROVED_DATE", rsTmp.getString("APPROVED_DATE"));
                ObjIssue.setAttribute("REJECTED_DATE", rsTmp.getString("REJECTED_DATE"));
                ObjIssue.setAttribute("REJECTED_REMARKS", rsTmp.getString("REJECTED_REMARKS"));
                ObjIssue.setAttribute("REMARKS", rsTmp.getString("REMARKS"));
                ObjIssue.setAttribute("CANCELED", rsTmp.getInt("CANCELED"));
                ObjIssue.setAttribute("CREATED_BY", rsTmp.getLong("CREATED_BY"));
                ObjIssue.setAttribute("CREATED_DATE", rsTmp.getString("CREATED_DATE"));
                ObjIssue.setAttribute("MODIFIED_BY", rsTmp.getLong("MODIFIED_BY"));
                ObjIssue.setAttribute("MODIFIED_DATE", rsTmp.getString("MODIFIED_DATE"));

                //Now Populate the collection
                //first clear the collection
                ObjIssue.colLineItems.clear();

                String mCompanyID = Long.toString((long) ObjIssue.getAttribute("COMPANY_ID").getVal());
                String mIssueno = (String) ObjIssue.getAttribute("ISSUE_NO").getObj();

                tmpStmt1 = Conn.createStatement();
                rsTmp1 = tmpStmt1.executeQuery("SELECT * FROM D_INV_ISSUE_DETAIL WHERE COMPANY_ID=" + mCompanyID + " AND ISSUE_NO='" + mIssueno.trim() + "'");

                Counter = 0;
                while (rsTmp1.next()) {
                    Counter = Counter + 1;
                    clsIssueGenItem ObjItem = new clsIssueGenItem();

                    ObjItem.setAttribute("COMPANY_ID", rsTmp1.getLong("COMPANY_ID"));
                    ObjItem.setAttribute("ISSUE_NO", rsTmp1.getString("ISSUE_NO"));
                    ObjItem.setAttribute("SR_NO", rsTmp1.getLong("SR_NO"));
                    ObjItem.setAttribute("WAREHOUSE_ID", rsTmp1.getString("WAREHOUSE_ID"));
                    ObjItem.setAttribute("LOCATION_ID", rsTmp1.getLong("LOCATION_ID"));
                    ObjItem.setAttribute("ITEM_CODE", rsTmp1.getString("ITEM_CODE"));
                    ObjItem.setAttribute("BOE_NO", rsTmp1.getString("BOE_NO"));
                    ObjItem.setAttribute("BOE_SRNO", rsTmp1.getString("BOE_SRNO"));
                    ObjItem.setAttribute("QTY", rsTmp1.getLong("QTY"));
                    ObjItem.setAttribute("UNIT", rsTmp1.getLong("UNIT"));
                    ObjItem.setAttribute("SHADE", rsTmp1.getString("SHADE"));
                    ObjItem.setAttribute("ISSUE_DESC", rsTmp1.getString("ISSUE_DESC"));
                    ObjItem.setAttribute("RJN_NO", rsTmp1.getString("RJN_NO"));
                    ObjItem.setAttribute("RJN_SRNO", rsTmp1.getLong("RJN_SRNO"));
                    ObjItem.setAttribute("REMARKS", rsTmp1.getString("REMARKS"));
                    ObjItem.setAttribute("CANCELED", rsTmp1.getInt("CANCELED"));
                    ObjItem.setAttribute("REMARKS", rsTmp1.getString("REMARKS"));
                    ObjItem.setAttribute("CREATED_BY", rsTmp1.getLong("CREATED_BY"));
                    ObjItem.setAttribute("CREATED_DATE", rsTmp1.getString("CREATED_DATE"));
                    ObjItem.setAttribute("MODIFIED_BY", rsTmp1.getLong("MODIFIED_BY"));
                    ObjItem.setAttribute("MODIFIED_DATE", rsTmp1.getString("MODIFIED_DATE"));

                    //Now Fetch Lots.
                    stLot = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    rsLot = stLot.executeQuery("SELECT * FROM D_INV_ISSUE_LOT WHERE COMPANY_ID=" + rsTmp1.getLong("COMPANY_ID") + " AND ISSUE_NO='" + rsTmp1.getString("ISSUE_NO") + "' AND ISSUE_SR_NO=" + rsTmp1.getLong("SR_NO"));
                    rsLot.first();

                    //Clear existing data
                    ObjItem.colItemLot.clear();

                    if (rsLot.getRow() > 0) {
                        Counter2 = 0;
                        while (!rsLot.isAfterLast()) {
                            Counter2++;
                            clsIssueReqItemDetail ObjLot = new clsIssueReqItemDetail();

                            ObjLot.setAttribute("COMPANY_ID", rsLot.getInt("COMPANY_ID"));
                            ObjLot.setAttribute("ISSUE_NO", rsLot.getString("ISSUE_NO"));
                            ObjLot.setAttribute("ISSUE_SR_NO", rsLot.getInt("ISSUE_SR_NO"));
                            ObjLot.setAttribute("SR_NO", rsLot.getInt("SR_NO"));
                            ObjLot.setAttribute("ITEM_ID", rsLot.getString("ITEM_ID"));
                            ObjLot.setAttribute("ITEM_LOT_NO", rsLot.getString("ITEM_LOT_NO"));
                            ObjLot.setAttribute("AUTO_LOT_NO", rsLot.getString("AUTO_LOT_NO"));
                            ObjLot.setAttribute("ISSUED_LOT_QTY", rsLot.getDouble("ISSUED_LOT_QTY"));
                            ObjLot.setAttribute("ISSUE_RATE", UtilFunctions.getDouble(rsLot, "ISSUE_RATE", 0));
                            //   ObjLot.setAttribute("ISSUE_RATE",rsLot.getDouble("ISSUE_RATE"));

                            ObjItem.colItemLot.put(Integer.toString(Counter2), ObjLot);

                            rsLot.next();
                        }
                    }
                    //Put into list

                    ObjIssue.colLineItems.put(Long.toString(Counter), ObjItem);
                }// Innser while
                //Put the prepared user object into list
                List.put(Long.toString(Counter), ObjIssue);
            }//Out While

        } catch (Exception e) {
        }

        return List;
    }

    private boolean setData() {
        ResultSet rsTmp, rsLot;
        Connection tmpConn;
        Statement tmpStmt, stLot;

        tmpConn = data.getConn();

        HashMap List = new HashMap();
        long Counter = 0;
        int RevNo = 0, LotCounter = 0;

        try {
            if (HistoryView) {
                RevNo = rsIssue.getInt("REVISION_NO");
                setAttribute("REVISION_NO", rsIssue.getInt("REVISION_NO"));
            } else {
                setAttribute("REVISION_NO", 0);
            }

            setAttribute("COMPANY_ID", rsIssue.getLong("COMPANY_ID"));
            setAttribute("ISSUE_NO", rsIssue.getString("ISSUE_NO"));
            setAttribute("ISSUE_DATE", rsIssue.getString("ISSUE_DATE"));
            setAttribute("ISSUE_TYPE", rsIssue.getString("ISSUE_TYPE"));
            setAttribute("FOR_STORE", rsIssue.getString("FOR_STORE"));
            setAttribute("FOR_DEPT_ID", rsIssue.getInt("FOR_DEPT_ID"));
            setAttribute("BLEND_CODE", rsIssue.getString("BLEND_CODE"));
            setAttribute("PURPOSE", rsIssue.getString("PURPOSE"));
            setAttribute("APPROVED", rsIssue.getInt("APPROVED"));
            setAttribute("APPROVED_DATE", rsIssue.getString("APPROVED_DATE"));
            setAttribute("REJECTED", rsIssue.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE", rsIssue.getString("REJECTED_DATE"));
            setAttribute("REMARKS", rsIssue.getString("REMARKS"));
            setAttribute("ISSUE_WITH_LOT", rsIssue.getBoolean("ISSUE_WITH_LOT"));
            setAttribute("HIERARCHY_ID", rsIssue.getInt("HIERARCHY_ID"));
            setAttribute("CANCELED", rsIssue.getInt("CANCELED"));
            setAttribute("CREATED_BY", rsIssue.getLong("CREATED_BY"));
            setAttribute("CREATED_DATE", rsIssue.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY", rsIssue.getLong("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE", rsIssue.getString("MODIFIED_DATE"));

            //Now Populate the collection
            //first clear the collection
            colLineItems.clear();

            String mCompanyID = Long.toString((long) getAttribute("COMPANY_ID").getVal());
            String mIssueno = (String) getAttribute("ISSUE_NO").getObj();

            tmpStmt = tmpConn.createStatement();
            if (HistoryView) {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM D_INV_ISSUE_DETAIL_H WHERE COMPANY_ID=" + mCompanyID + " AND ISSUE_NO='" + mIssueno.trim() + "' AND REVISION_NO=" + RevNo + " ORDER BY SR_NO");
            } else {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM D_INV_ISSUE_DETAIL WHERE COMPANY_ID=" + mCompanyID + " AND ISSUE_NO='" + mIssueno.trim() + "' ORDER BY SR_NO");
            }

            Counter = 0;
            while (rsTmp.next()) {
                Counter = Counter + 1;
                clsIssueGenItem ObjItem = new clsIssueGenItem();

                ObjItem.setAttribute("COMPANY_ID", rsTmp.getLong("COMPANY_ID"));
                ObjItem.setAttribute("ISSUE_NO", rsTmp.getString("ISSUE_NO"));
                ObjItem.setAttribute("SR_NO", rsTmp.getLong("SR_NO"));
                ObjItem.setAttribute("WAREHOUSE_ID", rsTmp.getString("WAREHOUSE_ID"));
                ObjItem.setAttribute("LOCATION_ID", rsTmp.getString("LOCATION_ID"));
                ObjItem.setAttribute("ITEM_CODE", rsTmp.getString("ITEM_CODE"));
                ObjItem.setAttribute("ITEM_EXTRA_DESC", rsTmp.getString("ITEM_EXTRA_DESC"));
                ObjItem.setAttribute("BOE_NO", rsTmp.getString("BOE_NO"));
                ObjItem.setAttribute("BOE_SR_NO", rsTmp.getString("BOE_SR_NO"));
                ObjItem.setAttribute("QTY", rsTmp.getDouble("QTY"));
                ObjItem.setAttribute("QTY_REQD", rsTmp.getDouble("QTY_REQD"));
                ObjItem.setAttribute("EXCESS_ISSUE_QTY", rsTmp.getDouble("EXCESS_ISSUE_QTY"));
                ObjItem.setAttribute("UNIT", rsTmp.getLong("UNIT"));
                ObjItem.setAttribute("COST_CENTER_ID", rsTmp.getLong("COST_CENTER_ID"));
                ObjItem.setAttribute("SHADE", rsTmp.getString("SHADE"));
                ObjItem.setAttribute("ISSUE_DESC", rsTmp.getString("ISSUE_DESC"));
                ObjItem.setAttribute("REQ_NO", rsTmp.getString("REQ_NO"));
                ObjItem.setAttribute("REQ_SRNO", rsTmp.getLong("REQ_SRNO"));
                ObjItem.setAttribute("REMARKS", rsTmp.getString("REMARKS"));
                ObjItem.setAttribute("CANCELED", rsTmp.getInt("CANCELED"));
                ObjItem.setAttribute("REMARKS", rsTmp.getString("REMARKS"));
                ObjItem.setAttribute("CREATED_BY", rsTmp.getLong("CREATED_BY"));
                ObjItem.setAttribute("CREATED_DATE", rsTmp.getString("CREATED_DATE"));
                ObjItem.setAttribute("MODIFIED_BY", rsTmp.getLong("MODIFIED_BY"));
                ObjItem.setAttribute("MODIFIED_DATE", rsTmp.getString("MODIFIED_DATE"));
                ObjItem.setAttribute("DEPT_ID", rsTmp.getInt("DEPT_ID"));
                ObjItem.setAttribute("GRN_NO", rsTmp.getString("GRN_NO"));
                ObjItem.setAttribute("GRN_SR_NO", rsTmp.getInt("GRN_SR_NO"));
                ObjItem.setAttribute("GRN_TYPE", rsTmp.getInt("GRN_TYPE"));
                ObjItem.setAttribute("MIR_NO", rsTmp.getString("MIR_NO"));
                ObjItem.setAttribute("MIR_SR_NO", rsTmp.getInt("MIR_SR_NO"));
                ObjItem.setAttribute("MIR_TYPE", rsTmp.getInt("MIR_TYPE"));
                ObjItem.setAttribute("ZERO_VAL_QTY", rsTmp.getDouble("ZERO_VAL_QTY"));
                ObjItem.setAttribute("ISSUE_VALUE", rsTmp.getDouble("ISSUE_VALUE"));
                ObjItem.setAttribute("RATE", rsTmp.getDouble("RATE"));
                ObjItem.setAttribute("STOCK_QTY", rsTmp.getDouble("STOCK_QTY"));

                ObjItem.setAttribute("LOT_NO", rsTmp.getString("LOT_NO"));
                ObjItem.setAttribute("BLEND_CODE", rsTmp.getString("BLEND_CODE"));
                ObjItem.setAttribute("MFG_PROG_NO", rsTmp.getString("MFG_PROG_NO"));
                ObjItem.setAttribute("MATERIAL_CODE_NO", rsTmp.getString("MATERIAL_CODE_NO"));

                //== Insert Lot Nos. ==
                stLot = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                if (HistoryView) {
                    rsLot = stLot.executeQuery("SELECT * FROM D_INV_ISSUE_LOT_H WHERE COMPANY_ID=" + mCompanyID + " AND ISSUE_NO='" + mIssueno + "' AND ISSUE_SR_NO=" + rsTmp.getLong("SR_NO") + " AND ISSUE_TYPE=2 AND REVISION_NO=" + RevNo + " ORDER BY SR_NO");
                } else {
                    rsLot = stLot.executeQuery("SELECT * FROM D_INV_ISSUE_LOT WHERE COMPANY_ID=" + mCompanyID + " AND ISSUE_NO='" + mIssueno + "' AND ISSUE_SR_NO=" + rsTmp.getLong("SR_NO") + " AND ISSUE_TYPE=2 ORDER BY SR_NO");
                }

                rsLot.first();

                LotCounter = 0;
                while ((!rsLot.isAfterLast()) && (rsLot.getRow() > 0)) {
                    LotCounter = LotCounter + 1;

                    clsIssueLot ObjLot = new clsIssueLot();
                    ObjLot.setAttribute("COMPANY_ID", rsLot.getInt("COMPANY_ID"));
                    ObjLot.setAttribute("ISSUE_NO", rsLot.getString("ISSUE_NO"));
                    ObjLot.setAttribute("ISSUE_SR_NO", rsLot.getInt("ISSUE_SR_NO"));
                    ObjLot.setAttribute("ISSUE_TYPE", rsLot.getInt("ISSUE_TYPE"));
                    ObjLot.setAttribute("SR_NO", rsLot.getInt("SR_NO"));
                    ObjLot.setAttribute("ITEM_ID", rsLot.getString("ITEM_ID"));
                    ObjLot.setAttribute("ITEM_LOT_NO", rsLot.getString("ITEM_LOT_NO"));
                    ObjLot.setAttribute("AUTO_LOT_NO", rsLot.getString("AUTO_LOT_NO"));
                    ObjLot.setAttribute("ISSUED_LOT_QTY", rsLot.getDouble("ISSUED_LOT_QTY"));
                    ObjLot.setAttribute("ISSUE_RATE", UtilFunctions.getDouble(rsLot, "ISSUE_RATE", 0));
                    // ObjLot.setAttribute("ISSUE_RATE",rsLot.getDouble("ISSUE_RATE"));
                    ObjItem.colItemLot.put(Integer.toString(LotCounter), ObjLot);
                    rsLot.next();
                }

                colLineItems.put(Long.toString(Counter), ObjItem);
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean rollback() {
        try {
            ResultSet rsTmp1;
            Statement Stmt1;
            String nIssueno = (String) getAttribute("ISSUE_NO").getObj();
            String gIssuetype = (String) getAttribute("ISSUE_TYPE").getObj();
            Stmt1 = Conn.createStatement();
            rsTmp1 = Stmt1.executeQuery("SELECT * FROM D_INV_ISSUE_DETAIL WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID) + " AND ISSUE_NO='"
                    + nIssueno.trim() + "'");

            while (rsTmp1.next()) {
                if (gIssuetype == "MAT") {
                    ResultSet rsMat;
                    rsMat = Stmt1.executeQuery("UPDATE D_INV_REQ_DETAIL SET ISSUE_QTY= ISSUE_QTY - "
                            + rsTmp1.getLong("QTY") + ",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID) + " AND REQ_NO='"
                            + rsTmp1.getString("REQ_NO") + "' AND SR_NO=" + rsTmp1.getLong("REQ_SRNO"));
                }
            }

            ResultSet rsItem;
            Statement Stmt2;
            Stmt2 = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsItem = Stmt2.executeQuery("UPDATE D_INV_ITEM_MASTER SET ON_HAND_QTY = ON_HAND_QTY + "
                    + rsTmp1.getLong("QTY") + " , ISSUED_QTY = ISSUED_QTY - "
                    + rsTmp1.getLong("QTY") + ",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID) + " AND ITEM_CODE='"
                    + rsTmp1.getLong("ITEM_CODE") + "'");

            ResultSet rsTmp;
            rsTmp = Stmt1.executeQuery("DELETE FROM D_INV_ISSUE_DETAIL WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID) + " AND ISSUE_NO='"
                    + nIssueno.trim() + "'");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean ShowHistory(int pCompanyID, String pDocNo) {
        Ready = false;
        try {
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsIssue = Stmt.executeQuery("SELECT * FROM D_INV_ISSUE_HEADER_H WHERE COMPANY_ID=" + pCompanyID + " AND ISSUE_NO='" + pDocNo + "'");
            Ready = true;
            HistoryView = true;
            MoveFirst();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public static HashMap getHistoryList(int pCompanyID, String pDocNo) {
        HashMap List = new HashMap();
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp = stTmp.executeQuery("SELECT * FROM D_INV_ISSUE_HEADER_H WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND ISSUE_NO='" + pDocNo + "'");

            while (rsTmp.next()) {
                clsIssueRaw ObjIssue = new clsIssueRaw();

                ObjIssue.setAttribute("ISSUE_NO", rsTmp.getString("ISSUE_NO"));
                ObjIssue.setAttribute("ISSUE_DATE", rsTmp.getString("ISSUE_DATE"));
                ObjIssue.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                ObjIssue.setAttribute("UPDATED_BY", rsTmp.getInt("UPDATED_BY"));
                ObjIssue.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                ObjIssue.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                ObjIssue.setAttribute("APPROVER_REMARKS", rsTmp.getString("APPROVER_REMARKS"));

                List.put(Integer.toString(List.size() + 1), ObjIssue);
            }

            rsTmp.close();
            stTmp.close();
            //tmpConn.close();
            return List;

        } catch (Exception e) {
            return List;
        }
    }

    public static String getDocStatus(int pCompanyID, String pDocNo) {
        ResultSet rsTmp;
        String strMessage = "";

        try {
            //First check that Document exist
            rsTmp = data.getResult("SELECT ISSUE_NO,APPROVED,CANCELED FROM D_INV_ISSUE_HEADER WHERE COMPANY_ID=" + pCompanyID + " AND ISSUE_NO='" + pDocNo + "'");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                if (rsTmp.getBoolean("APPROVED")) {
                    if (rsTmp.getBoolean("CANCELED")) {
                        strMessage = "Document is cancelled";
                    } else {
                        strMessage = "";
                    }
                } else {
                    strMessage = "Document is created but is under approval";
                }

            } else {
                strMessage = "Document does not exist";
            }
            rsTmp.close();
        } catch (Exception e) {
        }

        return strMessage;

    }

    public static boolean IsIssueExist(int pCompanyID, String pDocNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean isExist = false;

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            rsTmp = stTmp.executeQuery("SELECT ISSUE_NO FROM D_INV_ISSUE_HEADER WHERE COMPANY_ID=" + pCompanyID + " AND ISSUE_NO='" + pDocNo + "'");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                isExist = true;
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

            return isExist;

        } catch (Exception e) {
            return isExist;
        }
    }

    public static boolean CanCancel(int pCompanyID, String pDocNo) {
        ResultSet rsTmp = null;
        boolean canCancel = false;

        try {
            rsTmp = data.getResult("SELECT ISSUE_NO FROM D_INV_ISSUE_HEADER WHERE COMPANY_ID=" + pCompanyID + " AND ISSUE_NO='" + pDocNo + "' AND CANCELED=0");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                canCancel = true;
            }

            rsTmp.close();
        } catch (Exception e) {

        }

        return canCancel;

    }

    public static boolean CancelIssue(int pCompanyID, String pDocNo) {

        ResultSet rsTmp = null, rsIndent = null;
        boolean Cancelled = false;

        try {

            if (CanCancel(pCompanyID, pDocNo)) {
                boolean ApprovedDoc = false;

                rsTmp = data.getResult("SELECT APPROVED FROM D_INV_ISSUE_HEADER WHERE COMPANY_ID=" + pCompanyID + " AND ISSUE_NO='" + pDocNo + "'");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    ApprovedDoc = rsTmp.getBoolean("APPROVED");
                }

                if (ApprovedDoc) {
                    rsTmp = data.getResult("SELECT * FROM D_INV_ISSUE_DETAIL WHERE COMPANY_ID=" + pCompanyID + " AND ISSUE_NO='" + pDocNo + "'");
                    rsTmp.first();

                    if (rsTmp.getRow() > 0) {
                        while (!rsTmp.isAfterLast()) {
                            //============== Update P.O. ============ //
                            //Reverse the Stock Effect
                            double Qty = rsTmp.getDouble("QTY");
                            String ItemID = rsTmp.getString("ITEM_CODE");
                            String WarehouseID = rsTmp.getString("WAREHOUSE_ID");
                            String LocationID = rsTmp.getString("LOCATION_ID");
                            String BOENo = rsTmp.getString("BOE_NO");
                            String LOTNo = "X";

                            String strSQL = "UPDATE D_INV_ITEM_LOT_MASTER SET ON_HAND_QTY=ON_HAND_QTY+" + Qty + ",TOTAL_ISSUE_QTY=TOTAL_ISSUE_QTY-" + Qty + ",AVAILABLE_QTY=ON_HAND_QTY-ALLOCATED_QTY WHERE ITEM_ID='" + ItemID + "' AND WAREHOUSE_ID='" + WarehouseID + "' AND LOCATION_ID='" + LocationID + "' AND BOE_NO='" + BOENo + "' AND LOT_NO='" + LOTNo + "'";
                            data.Execute(strSQL);

                            //=============== P.O. Updation Completed ===============//
                            rsTmp.next();
                        }

                    }

                } else {
                    //Remove it from Approval Hierarchy
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID=" + pCompanyID + " AND DOC_NO='" + pDocNo + "' AND MODULE_ID=15");
                }

                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_INV_ISSUE_HEADER SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + pCompanyID + " AND ISSUE_NO='" + pDocNo + "'");
                data.Execute("UPDATE D_INV_ISSUE_LOT SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + pCompanyID + " AND ISSUE_NO='" + pDocNo + "'");

                Cancelled = true;
            }

            rsTmp.close();
            rsIndent.close();
        } catch (Exception e) {

        }

        return Cancelled;
    }

}
