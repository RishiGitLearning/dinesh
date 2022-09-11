/*
 * clsBusinessObject.java
 *
 * Created on April 5, 2004, 3:02 PM
 */
package EITLERP.Purchase;

import java.util.*;
import java.sql.*;
import EITLERP.*;
import EITLERP.Stores.*;
import javax.swing.*;
import EITLERP.Finance.*;

/**
 *
 * @author nrpithva
 * @version
 */
public class clsPOAmendGen {

    public String LastError = "";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;

    private HashMap props;
    public boolean Ready = false;

    public HashMap colPOItems = new HashMap();
    public HashMap colPOTerms = new HashMap();

    public int POType = 1; //Purchase Order Type - Added on 14 June to combine all the classes into one

    //History Related properties
    private boolean HistoryView = false;

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
     * Creates new clsBusinessObject
     */
    public clsPOAmendGen() {
        props = new HashMap();
        props.put("COMPANY_ID", new Variant(0));
        props.put("AMEND_NO", new Variant(""));
        props.put("AMEND_DATE", new Variant(""));
        props.put("AMEND_SR_NO", new Variant(0));
        props.put("LAST_AMENDMENT", new Variant(false));
        props.put("PO_NO", new Variant(""));
        props.put("PO_DATE", new Variant(""));
        props.put("PO_TYPE", new Variant(1)); //1 - General P.O. Amendment
        props.put("PO_REF", new Variant(""));
        props.put("SUPP_ID", new Variant(""));
        props.put("SUPP_NAME", new Variant(""));
        props.put("REF_A", new Variant(""));
        props.put("REF_B", new Variant(""));
        props.put("QUOTATION_NO", new Variant(""));
        props.put("QUOTATION_DATE", new Variant(""));
        props.put("INQUIRY_NO", new Variant(""));
        props.put("INQUIRY_DATE", new Variant(""));
        props.put("BUYER", new Variant(0));
        props.put("PURPOSE", new Variant(""));
        props.put("SUBJECT", new Variant(""));
        props.put("CURRENCY_ID", new Variant(0));
        props.put("CURRENCY_RATE", new Variant(0));
        props.put("TOTAL_AMOUNT", new Variant(0));
        props.put("NET_AMOUNT", new Variant(0));
        props.put("STATUS", new Variant(""));
        props.put("ATTACHEMENT", new Variant(false));
        props.put("ATTACHEMENT_PATH", new Variant(""));
        props.put("REMARKS", new Variant(""));
        props.put("SHIP_ID", new Variant(0));
        props.put("DELIVERY_DATE", new Variant(""));
        props.put("APPROVED", new Variant(false));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED", new Variant(false));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("HIERARCHY_ID", new Variant(0));
        props.put("CANCELLED", new Variant(false));
        props.put("IMPORT_CONCESS", new Variant(false));
        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("COLUMN_1_ID", new Variant(0));
        props.put("COLUMN_1_FORMULA", new Variant(""));
        props.put("COLUMN_1_PER", new Variant(0));
        props.put("COLUMN_1_AMT", new Variant(0));
        props.put("COLUMN_1_CAPTION", new Variant(""));
        props.put("COLUMN_2_ID", new Variant(0));
        props.put("COLUMN_2_FORMULA", new Variant(""));
        props.put("COLUMN_2_PER", new Variant(0));
        props.put("COLUMN_2_AMT", new Variant(0));
        props.put("COLUMN_2_CAPTION", new Variant(""));
        props.put("COLUMN_3_ID", new Variant(0));
        props.put("COLUMN_3_FORMULA", new Variant(""));
        props.put("COLUMN_3_PER", new Variant(0));
        props.put("COLUMN_3_AMT", new Variant(0));
        props.put("COLUMN_3_CAPTION", new Variant(""));
        props.put("COLUMN_4_ID", new Variant(0));
        props.put("COLUMN_4_FORMULA", new Variant(""));
        props.put("COLUMN_4_PER", new Variant(0));
        props.put("COLUMN_4_AMT", new Variant(0));
        props.put("COLUMN_4_CAPTION", new Variant(""));
        props.put("COLUMN_5_ID", new Variant(0));
        props.put("COLUMN_5_FORMULA", new Variant(""));
        props.put("COLUMN_5_PER", new Variant(0));
        props.put("COLUMN_5_AMT", new Variant(0));
        props.put("COLUMN_5_CAPTION", new Variant(""));
        props.put("COLUMN_6_ID", new Variant(0));
        props.put("COLUMN_6_FORMULA", new Variant(""));
        props.put("COLUMN_6_PER", new Variant(0));
        props.put("COLUMN_6_AMT", new Variant(0));
        props.put("COLUMN_6_CAPTION", new Variant(""));
        props.put("COLUMN_7_ID", new Variant(0));
        props.put("COLUMN_7_FORMULA", new Variant(""));
        props.put("COLUMN_7_PER", new Variant(0));
        props.put("COLUMN_7_AMT", new Variant(0));
        props.put("COLUMN_7_CAPTION", new Variant(""));
        props.put("COLUMN_8_ID", new Variant(0));
        props.put("COLUMN_8_FORMULA", new Variant(""));
        props.put("COLUMN_8_PER", new Variant(0));
        props.put("COLUMN_8_AMT", new Variant(0));
        props.put("COLUMN_8_CAPTION", new Variant(""));
        props.put("COLUMN_9_ID", new Variant(0));
        props.put("COLUMN_9_FORMULA", new Variant(""));
        props.put("COLUMN_9_PER", new Variant(0));
        props.put("COLUMN_9_AMT", new Variant(0));
        props.put("COLUMN_9_CAPTION", new Variant(""));
        props.put("COLUMN_10_ID", new Variant(0));
        props.put("COLUMN_10_FORMULA", new Variant(""));
        props.put("COLUMN_10_PER", new Variant(0));
        props.put("COLUMN_10_AMT", new Variant(0));
        props.put("COLUMN_10_CAPTION", new Variant(""));

        props.put("COLUMN_11_ID", new Variant(0));
        props.put("COLUMN_11_FORMULA", new Variant(""));
        props.put("COLUMN_11_PER", new Variant(0));
        props.put("COLUMN_11_AMT", new Variant(0));
        props.put("COLUMN_11_CAPTION", new Variant(""));

        props.put("COLUMN_12_ID", new Variant(0));
        props.put("COLUMN_12_FORMULA", new Variant(""));
        props.put("COLUMN_12_PER", new Variant(0));
        props.put("COLUMN_12_AMT", new Variant(0));
        props.put("COLUMN_12_CAPTION", new Variant(""));

        props.put("COLUMN_13_ID", new Variant(0));
        props.put("COLUMN_13_FORMULA", new Variant(""));
        props.put("COLUMN_13_PER", new Variant(0));
        props.put("COLUMN_13_AMT", new Variant(0));
        props.put("COLUMN_13_CAPTION", new Variant(""));

        props.put("COLUMN_14_ID", new Variant(0));
        props.put("COLUMN_14_FORMULA", new Variant(""));
        props.put("COLUMN_14_PER", new Variant(0));
        props.put("COLUMN_14_AMT", new Variant(0));
        props.put("COLUMN_14_CAPTION", new Variant(""));

        props.put("COLUMN_15_ID", new Variant(0));
        props.put("COLUMN_15_FORMULA", new Variant(""));
        props.put("COLUMN_15_PER", new Variant(0));
        props.put("COLUMN_15_AMT", new Variant(0));
        props.put("COLUMN_15_CAPTION", new Variant(""));

        props.put("COLUMN_16_ID", new Variant(0));
        props.put("COLUMN_16_FORMULA", new Variant(""));
        props.put("COLUMN_16_PER", new Variant(0));
        props.put("COLUMN_16_AMT", new Variant(0));
        props.put("COLUMN_16_CAPTION", new Variant(""));
        props.put("COLUMN_17_ID", new Variant(0));
        props.put("COLUMN_17_FORMULA", new Variant(""));
        props.put("COLUMN_17_PER", new Variant(0));
        props.put("COLUMN_17_AMT", new Variant(0));
        props.put("COLUMN_17_CAPTION", new Variant(""));
        props.put("COLUMN_18_ID", new Variant(0));
        props.put("COLUMN_18_FORMULA", new Variant(""));
        props.put("COLUMN_18_PER", new Variant(0));
        props.put("COLUMN_18_AMT", new Variant(0));
        props.put("COLUMN_18_CAPTION", new Variant(""));
        props.put("COLUMN_19_ID", new Variant(0));
        props.put("COLUMN_19_FORMULA", new Variant(""));
        props.put("COLUMN_19_PER", new Variant(0));
        props.put("COLUMN_19_AMT", new Variant(0));
        props.put("COLUMN_19_CAPTION", new Variant(""));
        props.put("COLUMN_20_ID", new Variant(0));
        props.put("COLUMN_20_FORMULA", new Variant(""));
        props.put("COLUMN_20_PER", new Variant(0));
        props.put("COLUMN_20_AMT", new Variant(0));
        props.put("COLUMN_20_CAPTION", new Variant(""));

        props.put("COLUMN_21_ID", new Variant(0));
        props.put("COLUMN_21_FORMULA", new Variant(""));
        props.put("COLUMN_21_PER", new Variant(0));
        props.put("COLUMN_21_AMT", new Variant(0));
        props.put("COLUMN_21_CAPTION", new Variant(""));

        props.put("PREFIX", new Variant("")); //For Autonumber generation
        props.put("REASON_CODE", new Variant(0));
        props.put("AMEND_REASON", new Variant(""));
        props.put("PRINT_LINE_1", new Variant(""));
        props.put("PRINT_LINE_2", new Variant(""));
        props.put("IMPORT_LICENSE", new Variant(""));
        props.put("REVISION_NO", new Variant(0));
        props.put("TRANSPORT_MODE", new Variant(0));
        props.put("PRICE_BASIS_TERM", new Variant(""));
        props.put("DISCOUNT_TERM", new Variant(""));
        props.put("EXCISE_TERM", new Variant(""));
        props.put("ST_TERM", new Variant(""));
        props.put("PF_TERM", new Variant(""));
        props.put("FREIGHT_TERM", new Variant(""));
        props.put("OCTROI_TERM", new Variant(""));
        props.put("FOB_TERM", new Variant(""));
        props.put("CIE_TERM", new Variant(""));
        props.put("INSURANCE_TERM", new Variant(""));
        props.put("TCC_TERM", new Variant(""));
        props.put("CENVAT_TERM", new Variant(""));
        props.put("DESPATCH_TERM", new Variant(""));
        props.put("SERIVCE_TAX_TERM", new Variant(""));
        props.put("OTHERS_TERM", new Variant(""));
        props.put("FILE_TEXT", new Variant(""));
        props.put("COVERING_TEXT", new Variant(""));
        
        props.put("CGST_TERM",new Variant(""));
        props.put("SGST_TERM",new Variant(""));
        props.put("IGST_TERM",new Variant(""));
        props.put("COMPOSITION_TERM",new Variant(""));
        props.put("RCM_TERM",new Variant(""));
        props.put("GST_COMPENSATION_CESS_TERM",new Variant(""));

        props.put("PAYMENT_TERM", new Variant(""));
        props.put("PAYMENT_CODE", new Variant(0));
        props.put("CR_DAYS", new Variant(0));
        props.put("DEPT_ID", new Variant(0));

        // -- Approval Specific Fields --
        props.put("FROM", new Variant(0));
        props.put("TO", new Variant(0));
        props.put("FROM_REMARKS", new Variant(""));
        props.put("APPROVAL_STATUS", new Variant(""));

        //New Service Contract fields
        props.put("PREMISES_TYPE", new Variant(""));
        props.put("SCOPE", new Variant(""));
        props.put("SERVICE_PERIOD", new Variant(""));
        props.put("SERVICE_FREQUENCY", new Variant(""));
        props.put("CONTRACT_DETAILS", new Variant(""));
        props.put("SERVICE_REPORT", new Variant(""));
        props.put("ESI_TERMS", new Variant(""));
        props.put("TERMINATION_TERMS", new Variant(""));
        props.put("AMOUNT_IN_WORDS", new Variant(""));
        props.put("DIRECTOR_APPROVAL", new Variant(false));
        props.put("DOC_ID", new Variant(0));

    }

    public boolean LoadData(int pCompanyID, int pType) {
        Ready = false;
        POType = pType;

        try {
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery("SELECT * FROM D_PUR_AMEND_HEADER WHERE COMPANY_ID=" + pCompanyID + " AND PO_TYPE=" + POType + " AND AMEND_SR_NO>0 AND AMEND_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND AMEND_DATE<='" + EITLERPGLOBAL.FinToDateDB + "' ORDER BY AMEND_NO");
            Ready = true;
            HistoryView = false;
            MoveLast();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean LoadData(int pCompanyID, int pType, String dbURL) {
        Ready = false;
        try {
            Conn = data.getConn(dbURL);
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery("SELECT * FROM D_PUR_AMEND_HEADER WHERE COMPANY_ID=" + pCompanyID + " AND PO_TYPE=" + pType + " ORDER BY AMEND_NO");
            Ready = true;
            HistoryView = false;
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
        Statement stTmp, stIndent, stItem, stPO, stPOLine, stQuotation, stTerms, stHistory, stHDetail, stHTerms;
        ResultSet rsTmp, rsIndent, rsItem, rsPO, rsPOLine, rsQuotation, rsTerms, rsHistory, rsHDetail, rsHTerms;
        Statement stHeader;
        ResultSet rsHeader;

        String strSQL = "", IndentNo = "", PONo = "", QuotID = "";
        int CompanyID = 0, IndentSrNo = 0, QuotSrNo = 0;
        double Qty = 0;

        try {

            //======= Check the requisition date ============//
            /*java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
             java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
             java.sql.Date DocDate=java.sql.Date.valueOf((String)getAttribute("AMEND_DATE").getObj());
             
             if((DocDate.after(FinFromDate)||DocDate.compareTo(FinFromDate)==0)&&(DocDate.before(FinToDate)||DocDate.compareTo(FinToDate)==0)) {
             //Within the year
             }
             else {
             LastError="Document date is not within financial year.";
             return false;
             }*/
            //=====================================================//
            // ---- History Related Changes ------ //
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHDetail = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHTerms = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rsHistory = stHistory.executeQuery("SELECT * FROM D_PUR_AMEND_HEADER_H WHERE AMEND_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail = stHDetail.executeQuery("SELECT * FROM D_PUR_AMEND_DETAIL_H WHERE AMEND_NO='1'");
            rsHDetail.first();
            rsHTerms = stHTerms.executeQuery("SELECT * FROM D_PUR_AMEND_TERMS_H WHERE AMEND_NO='1'");
            rsHTerms.first();
            //------------------------------------//

            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_PUR_AMEND_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND AMEND_NO='1'");
            ApprovalFlow ObjFlow = new ApprovalFlow();

            CompanyID = (int) getAttribute("COMPANY_ID").getVal();
            PONo = (String) getAttribute("PO_NO").getObj();

            //====== Checking Qty against receipt qty =====//
            for (int i = 1; i <= colPOItems.size(); i++) {
                clsPOAmendItem ObjItem = (clsPOAmendItem) colPOItems.get(Integer.toString(i));
                int lSrNo = (int) ObjItem.getAttribute("SR_NO").getVal();
                double lQty = ObjItem.getAttribute("QTY").getVal();

                stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                strSQL = "SELECT RECD_QTY FROM D_PUR_PO_DETAIL WHERE COMPANY_ID=" + CompanyID + " AND PO_NO='" + PONo + "' AND SR_NO=" + lSrNo + " AND PO_TYPE=" + POType;
                rsTmp = stTmp.executeQuery(strSQL);
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    if (lQty < rsTmp.getDouble("RECD_QTY")) {
                        LastError = "Cannot amend qty of item sr. no." + lSrNo + " to " + lQty + ".\nAlready " + rsTmp.getDouble("RECD_QTY") + " qty have been received.";
                        return false;
                    }
                }
            }
            //----------------------------------------------//

            //=========== Check the Quantities entered against Indent============= //
            for (int i = 1; i <= colPOItems.size(); i++) {
                clsPOAmendItem ObjItem = (clsPOAmendItem) colPOItems.get(Integer.toString(i));
                String ItemID = (String) ObjItem.getAttribute("ITEM_ID").getObj();
                IndentNo = (String) ObjItem.getAttribute("INDENT_NO").getObj();
                IndentSrNo = (int) ObjItem.getAttribute("INDENT_SR_NO").getVal();
                QuotID = (String) ObjItem.getAttribute("QUOT_ID").getObj();
                QuotSrNo = (int) ObjItem.getAttribute("QUOT_SR_NO").getVal();

                double IndentQty = 0;
                double QuotQty = 0;
                double PrevQty = 0; //Previously Entered Qty against Indent
                double CurrentQty = 0; //Currently entered Qty.

                if ((!IndentNo.trim().equals("")) && (IndentSrNo > 0)) //Indent Entered
                {
                    //Get the  Indent Qty.
                    stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    strSQL = "SELECT QTY,ITEM_CODE FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID=" + CompanyID + " AND INDENT_NO='" + IndentNo + "' AND SR_NO=" + IndentSrNo + " ";
                    rsTmp = stTmp.executeQuery(strSQL);
                    rsTmp.first();

                    if (rsTmp.getRow() > 0) {
                        IndentQty = rsTmp.getDouble("QTY");

                        if (!rsTmp.getString("ITEM_CODE").trim().equals(ItemID)) {
                            LastError = "Item Code in PO doesn't match with Indent. Original Item code is " + rsTmp.getString("ITEM_CODE");
                            return false;
                        }

                    }

                    //Get Total Qty Entered in PO Amendment (if any) Against this Indent No.
                    PrevQty = 0;
                    stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    strSQL = "SELECT SUM(QTY) AS SUMQTY FROM D_PUR_PO_DETAIL WHERE COMPANY_ID=" + CompanyID + " AND INDENT_NO='" + IndentNo + "' AND INDENT_SR_NO=" + IndentSrNo + " AND NOT (PO_NO='" + PONo + "' AND PO_TYPE=" + POType + ") AND PO_NO NOT IN (SELECT PO_NO FROM D_PUR_PO_HEADER WHERE CANCELLED=1)";
                    rsTmp = stTmp.executeQuery(strSQL);
                    rsTmp.first();

                    if (rsTmp.getRow() > 0) {
                        PrevQty = rsTmp.getDouble("SUMQTY");
                    }

                    CurrentQty = ObjItem.getAttribute("QTY").getVal();
                    // Below program Modified by ASHUTOSH on 03-01-2019
                    //Also find duplicate entries - made with Ctrl+C functionality
                    for(int d=1;d<=colPOItems.size();d++) {
                        if(d!=i) {
                            //clsPOItem ObjD=(clsPOItem)colPOItems.get(Integer.toString(d));
                            clsPOAmendItem ObjD = (clsPOAmendItem) colPOItems.get(Integer.toString(d));
                            String tmpIndentNo=(String)ObjD.getAttribute("INDENT_NO").getObj();
                            int tmpIndentSrNo=(int)ObjD.getAttribute("INDENT_SR_NO").getVal();
                            
                            if(tmpIndentNo.equals(IndentNo) && tmpIndentSrNo==IndentSrNo) {
                                CurrentQty+=ObjD.getAttribute("QTY").getVal();
                            }
                        }
                    }
                    //===============================================================
                    // --------End of ASHUTOSH program


                    if ((CurrentQty + PrevQty) > IndentQty) //If total Qty exceeds Indent Qty. Do not allow
                    {
                        LastError = "Total quantity entered " + (CurrentQty + PrevQty) + " exceeds Indent No. " + IndentNo + " Sr. No. " + IndentSrNo + " qty " + IndentQty + ". Please verify the input.";
                        return false;
                    }
                }

                //Check the Quotation
                if ((!QuotID.trim().equals("")) && (QuotSrNo > 0)) //Indent Entered
                {
                    //Get the  Indent Qty.
                    stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    strSQL = "SELECT QTY FROM D_PUR_QUOT_DETAIL WHERE COMPANY_ID=" + CompanyID + " AND QUOT_ID='" + QuotID + "' AND SR_NO=" + QuotSrNo + " ";
                    rsTmp = stTmp.executeQuery(strSQL);
                    rsTmp.first();

                    if (rsTmp.getRow() > 0) {
                        QuotQty = rsTmp.getDouble("QTY");
                    }

                    //Get Total Qty Entered in PO Amendment (if any) Against this Quotation No.
                    PrevQty = 0;
                    stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    strSQL = "SELECT SUM(QTY) AS SUMQTY FROM D_PUR_PO_DETAIL WHERE COMPANY_ID=" + CompanyID + " AND QUOT_ID='" + QuotID + "' AND QUOT_SR_NO=" + QuotSrNo + " AND NOT (PO_NO='" + PONo + "' AND PO_TYPE=" + POType + ") AND PO_NO NOT IN (SELECT PO_NO FROM D_PUR_PO_HEADER WHERE CANCELLED=1)";
                    rsTmp = stTmp.executeQuery(strSQL);
                    rsTmp.first();

                    if (rsTmp.getRow() > 0) {
                        PrevQty = rsTmp.getDouble("SUMQTY");
                    }

                    CurrentQty = ObjItem.getAttribute("QTY").getVal();

                    if ((CurrentQty + PrevQty) > QuotQty) //If total Qty exceeds Indent Qty. Do not allow
                    {
                        LastError = "Total quantity entered " + (CurrentQty + PrevQty) + " exceeds Quotation No. " + QuotID + " Sr. No. " + QuotSrNo + " qty " + QuotQty + ". Please verify the input.";
                        return false;
                    }
                }

            }
            //============== Indent Checking Completed ====================//

            //======= Checking with RIA for approved qty =========//
            //===================================================//
            String AStatus = (String) getAttribute("APPROVAL_STATUS").getObj();

            String Messages = "";
            boolean ContinuePO = true;

            if (POType == 1 || POType == 3 || POType == 5 || POType == 9) {
                for (int i = 1; i <= colPOItems.size(); i++) {
                    clsPOAmendItem ObjItem = (clsPOAmendItem) colPOItems.get(Integer.toString(i));
                    IndentNo = (String) ObjItem.getAttribute("INDENT_NO").getObj();
                    IndentSrNo = (int) ObjItem.getAttribute("INDENT_SR_NO").getVal();
                    String ItemID = (String) ObjItem.getAttribute("ITEM_ID").getObj();
                    Qty = ObjItem.getAttribute("QTY").getVal();

                    //First check that RIA is created
                    if ((!IndentNo.trim().equals("")) && IndentSrNo > 0) {
                        String RIANo = clsIndent.getRIANo(EITLERPGLOBAL.gCompanyID, IndentNo, IndentSrNo);
                        if (!RIANo.trim().equals("")) {
                            int RIAStatus = clsRateApproval.getRIAStatus(EITLERPGLOBAL.gCompanyID, RIANo);
                            double RIAQty = clsIndent.getRIAQty(EITLERPGLOBAL.gCompanyID, IndentNo, IndentSrNo);

                            if (RIAStatus == 1) //RIA is made and approved
                            {
                                if (Qty <= RIAQty) {
                                    // APPROVED. CONTINUE PREPARING PURCHASE ORDER
                                } else {
                                    //Qty in Purchase Order getting exceeded to RIA Approved qty.
                                    Messages += "\nOnly " + RIAQty + " is approved for item " + ItemID;
                                    //ContinuePO=false;
                                    ContinuePO = true;
                                }
                            } else {  //RIA under approval. Reject it
                                Messages += "\nRIA No. " + RIANo + " is under approval of item " + ItemID;
                                ContinuePO = false;

                            }

                        } else {
                            //Find the last RIA No.

                            //Following statement will return any last RIA made for the item
                            String PODate = (String) getAttribute("PO_DATE").getObj();

                            RIANo = clsRateApproval.getRIANo(EITLERPGLOBAL.gCompanyID, ItemID, PODate);
                            if (RIANo.trim().equals("")) {
                                //Continue with last RIA No.
                            } else {
                                //Put the code here if PO is to be restricted to make RIA compulsoary.
                            }

                        }
                    } else {
                        //No Indent Reference

                        //Following statement will return any last RIA made for the item
                        String PODate = (String) getAttribute("PO_DATE").getObj();

                        String RIANo = clsRateApproval.getRIANo(EITLERPGLOBAL.gCompanyID, ItemID, PODate);
                        if (!RIANo.trim().equals("")) {
                            int RIAStatus = clsRateApproval.getRIAStatus(EITLERPGLOBAL.gCompanyID, RIANo);
                            double RIAQty = clsRateApproval.getRIAApprovedQty(EITLERPGLOBAL.gCompanyID, RIANo, ItemID);

                            if (RIAStatus == 1) //RIA is made and approved
                            {
                                if (Qty <= RIAQty) {
                                    // APPROVED. CONTINUE PREPARING PURCHASE ORDER
                                } else {
                                    //Qty in Purchase Order getting exceeded to RIA Approved qty.
                                    Messages += "\nOnly " + RIAQty + " is approved for item " + ItemID;
                                    // ContinuePO=false; //---->Change here on 14 May 2006 //
                                    ContinuePO = true;
                                }
                            } else {  //RIA under approval. Reject it
                                Messages += "\nRIA No. " + RIANo + " is under approval of item " + ItemID;
                                ContinuePO = false;

                            }
                        }
                    }

                }

                //RIA Checking will only affec
                if (!AStatus.equals("F")) {
                    if (!ContinuePO) {
                        JOptionPane.showMessageDialog(null, "PO will not be final approved. Following RIAs are not complete" + Messages);
                    }
                } else {
                    if (!ContinuePO) {
                        LastError = "Can not final approve the PO. Following RIAs are not complete" + Messages;
                        return false;
                    }

                }
            }
            //==================================================//
            //=================================================//

            // Update the Stock only after Final Approval //
            if (AStatus.equals("F")) {
                //--- Reverse the Effect of last P.O. --- //
                strSQL = "SELECT * FROM D_PUR_PO_DETAIL WHERE COMPANY_ID=" + CompanyID + " AND PO_NO='" + PONo + "' AND PO_TYPE=" + POType + " ";

                stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                rsTmp = stTmp.executeQuery(strSQL);
                rsTmp.first();

                while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {
                    IndentNo = rsTmp.getString("INDENT_NO");
                    IndentSrNo = rsTmp.getInt("INDENT_SR_NO");
                    QuotID = rsTmp.getString("QUOT_ID");
                    QuotSrNo = rsTmp.getInt("QUOT_SR_NO");
                    Qty = rsTmp.getDouble("QTY");

                    try {
                        // Update Indent
                        data.Execute("UPDATE D_INV_INDENT_DETAIL SET PO_QTY=PO_QTY-" + Qty + ",BAL_QTY=QTY-PO_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + CompanyID + " AND INDENT_NO='" + IndentNo.trim() + "' AND SR_NO=" + IndentSrNo + " ");
                        data.Execute("UPDATE D_INV_INDENT_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + CompanyID + " AND INDENT_NO='" + IndentNo.trim() + "'");

                        // Update Quotation
                        data.Execute("UPDATE D_PUR_QUOT_DETAIL SET PO_QTY=PO_QTY-" + Qty + ",BAL_QTY=QTY-PO_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + CompanyID + " AND QUOT_ID='" + QuotID.trim() + "' AND SR_NO=" + QuotSrNo + " ");
                        data.Execute("UPDATE D_PUR_QUOT_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + CompanyID + " AND QUOT_ID='" + QuotID.trim() + "'");
                    } catch (Exception e) {

                    }
                    rsTmp.next();
                }
                //---------------------------------------//

                //-------- First Update the stock -------------//
                for (int i = 1; i <= colPOItems.size(); i++) {
                    clsPOAmendItem ObjItem = (clsPOAmendItem) colPOItems.get(Integer.toString(i));

                    IndentNo = (String) ObjItem.getAttribute("INDENT_NO").getObj();
                    IndentSrNo = (int) ObjItem.getAttribute("INDENT_SR_NO").getVal();
                    QuotID = (String) ObjItem.getAttribute("QUOT_ID").getObj();
                    QuotSrNo = (int) ObjItem.getAttribute("QUOT_SR_NO").getVal();
                    Qty = ObjItem.getAttribute("QTY").getVal();

                    try {
                        // Update Indent
                        data.Execute("UPDATE D_INV_INDENT_DETAIL SET PO_QTY=PO_QTY+" + Qty + ",BAL_QTY=QTY-PO_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + CompanyID + " AND INDENT_NO='" + IndentNo.trim() + "' AND SR_NO=" + IndentSrNo + " ");
                        data.Execute("UPDATE D_INV_INDENT_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + CompanyID + " AND INDENT_NO='" + IndentNo.trim() + "'");

                        // Update Quotation
                        data.Execute("UPDATE D_PUR_QUOT_DETAIL SET PO_QTY=PO_QTY+" + Qty + ",BAL_QTY=QTY-PO_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + CompanyID + " AND QUOT_ID='" + QuotID.trim() + "' AND SR_NO=" + QuotSrNo + " ");
                        data.Execute("UPDATE D_PUR_QUOT_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + CompanyID + " AND QUOT_ID='" + QuotID.trim() + "'");
                    } catch (Exception e) {

                    }
                } //First for loop
                //=============Updation of stock completed=========================//
            } // End of First Approval Status condition

            //Following code implements new method of handling amendments
            if (AStatus.equals("F")) {
                //Now Get PO Object and Update the PO Table with current Data.
                clsPOGen tmpPO = new clsPOGen();
                tmpPO.POType = POType;

                clsPOGen ObjPO = (clsPOGen) tmpPO.getObject(EITLERPGLOBAL.gCompanyID, PONo);

                //Remove current data of PO
                data.Execute("DELETE FROM D_PUR_PO_HEADER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PO_NO='" + PONo + "' AND PO_TYPE=" + POType);
                data.Execute("DELETE FROM D_PUR_PO_DETAIL WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PO_NO='" + PONo + "' AND PO_TYPE=" + POType);
                data.Execute("DELETE FROM D_PUR_PO_TERMS WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PO_NO='" + PONo + "' AND PO_TYPE=" + POType);

                //Now update the PO Tables with current data
                stPO = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                rsPO = stPO.executeQuery("SELECT * FROM D_PUR_PO_HEADER");

                rsPO.first();
                rsPO.moveToInsertRow();
                rsPO.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
                rsPO.updateString("PO_NO", (String) getAttribute("PO_NO").getObj());
                rsPO.updateLong("DOC_ID", (long) getAttribute("DOC_ID").getVal());
                rsPO.updateString("PO_DATE", (String) getAttribute("PO_DATE").getObj());
                rsPO.updateInt("PO_TYPE", POType);
                rsPO.updateString("PO_REF", (String) getAttribute("PO_REF").getObj());
                rsPO.updateString("SUPP_ID", (String) getAttribute("SUPP_ID").getObj());
                rsPO.updateString("SUPP_NAME", (String) getAttribute("SUPP_NAME").getObj());
                rsPO.updateString("REF_A", (String) getAttribute("REF_A").getObj());
                rsPO.updateString("REF_B", (String) getAttribute("REF_B").getObj());
                rsPO.updateString("QUOTATION_NO", (String) getAttribute("QUOTATION_NO").getObj());
                rsPO.updateString("QUOTATION_DATE", (String) getAttribute("QUOTATION_DATE").getObj());
                rsPO.updateString("INQUIRY_NO", (String) getAttribute("INQUIRY_NO").getObj());
                rsPO.updateString("INQUIRY_DATE", (String) getAttribute("INQUIRY_DATE").getObj());
                rsPO.updateInt("BUYER", (int) getAttribute("BUYER").getVal());
                rsPO.updateInt("TRANSPORT_MODE", (int) getAttribute("TRANSPORT_MODE").getVal());
                rsPO.updateString("PURPOSE", (String) getAttribute("PURPOSE").getObj());
                rsPO.updateString("SUBJECT", (String) getAttribute("SUBJECT").getObj());
                rsPO.updateInt("CURRENCY_ID", (int) getAttribute("CURRENCY_ID").getVal());
                rsPO.updateDouble("CURRENCY_RATE", getAttribute("CURRENCY_RATE").getVal());
                rsPO.updateDouble("TOTAL_AMOUNT", getAttribute("TOTAL_AMOUNT").getVal());
                rsPO.updateDouble("NET_AMOUNT", getAttribute("NET_AMOUNT").getVal());
                rsPO.updateString("STATUS", (String) getAttribute("STATUS").getObj());
                rsPO.updateBoolean("ATTACHEMENT", getAttribute("ATTACHEMENT").getBool());
                rsPO.updateString("ATTACHEMENT_PATH", (String) getAttribute("ATTACHEMENT_PATH").getObj());
                rsPO.updateString("REMARKS", (String) getAttribute("REMARKS").getObj());
                rsPO.updateInt("SHIP_ID", (int) getAttribute("SHIP_ID").getVal());
                rsPO.updateBoolean("CANCELLED", getAttribute("CANCELLED").getBool());
                rsPO.updateBoolean("APPROVED", true);
                rsPO.updateBoolean("REJECTED", false);
                rsPO.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                rsPO.updateLong("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                rsPO.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                //rsPO.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
                //rsPO.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                //Now Custom Columns
                rsPO.updateInt("COLUMN_1_ID", (int) getAttribute("COLUMN_1_ID").getVal());
                rsPO.updateString("COLUMN_1_FORMULA", (String) getAttribute("COLUMN_1_FORMULA").getObj());
                rsPO.updateDouble("COLUMN_1_PER", getAttribute("COLUMN_1_PER").getVal());
                rsPO.updateDouble("COLUMN_1_AMT", getAttribute("COLUMN_1_AMT").getVal());
                rsPO.updateString("COLUMN_1_CAPTION", (String) getAttribute("COLUMN_1_CAPTION").getObj());
                rsPO.updateInt("COLUMN_2_ID", (int) getAttribute("COLUMN_2_ID").getVal());
                rsPO.updateString("COLUMN_2_FORMULA", (String) getAttribute("COLUMN_2_FORMULA").getObj());
                rsPO.updateDouble("COLUMN_2_PER", getAttribute("COLUMN_2_PER").getVal());
                rsPO.updateDouble("COLUMN_2_AMT", getAttribute("COLUMN_2_AMT").getVal());
                rsPO.updateString("COLUMN_2_CAPTION", (String) getAttribute("COLUMN_2_CAPTION").getObj());
                rsPO.updateInt("COLUMN_3_ID", (int) getAttribute("COLUMN_3_ID").getVal());
                rsPO.updateString("COLUMN_3_FORMULA", (String) getAttribute("COLUMN_3_FORMULA").getObj());
                rsPO.updateDouble("COLUMN_3_PER", getAttribute("COLUMN_3_PER").getVal());
                rsPO.updateDouble("COLUMN_3_AMT", getAttribute("COLUMN_3_AMT").getVal());
                rsPO.updateString("COLUMN_3_CAPTION", (String) getAttribute("COLUMN_3_CAPTION").getObj());
                rsPO.updateInt("COLUMN_4_ID", (int) getAttribute("COLUMN_4_ID").getVal());
                rsPO.updateString("COLUMN_4_FORMULA", (String) getAttribute("COLUMN_4_FORMULA").getObj());
                rsPO.updateDouble("COLUMN_4_PER", getAttribute("COLUMN_4_PER").getVal());
                rsPO.updateDouble("COLUMN_4_AMT", getAttribute("COLUMN_4_AMT").getVal());
                rsPO.updateString("COLUMN_4_CAPTION", (String) getAttribute("COLUMN_4_CAPTION").getObj());
                rsPO.updateInt("COLUMN_5_ID", (int) getAttribute("COLUMN_5_ID").getVal());
                rsPO.updateString("COLUMN_5_FORMULA", (String) getAttribute("COLUMN_5_FORMULA").getObj());
                rsPO.updateDouble("COLUMN_5_PER", getAttribute("COLUMN_5_PER").getVal());
                rsPO.updateDouble("COLUMN_5_AMT", getAttribute("COLUMN_5_AMT").getVal());
                rsPO.updateString("COLUMN_5_CAPTION", (String) getAttribute("COLUMN_5_CAPTION").getObj());
                rsPO.updateInt("COLUMN_6_ID", (int) getAttribute("COLUMN_6_ID").getVal());
                rsPO.updateString("COLUMN_6_FORMULA", (String) getAttribute("COLUMN_6_FORMULA").getObj());
                rsPO.updateDouble("COLUMN_6_PER", getAttribute("COLUMN_6_PER").getVal());
                rsPO.updateDouble("COLUMN_6_AMT", getAttribute("COLUMN_6_AMT").getVal());
                rsPO.updateString("COLUMN_6_CAPTION", (String) getAttribute("COLUMN_6_CAPTION").getObj());
                rsPO.updateInt("COLUMN_7_ID", (int) getAttribute("COLUMN_7_ID").getVal());
                rsPO.updateString("COLUMN_7_FORMULA", (String) getAttribute("COLUMN_7_FORMULA").getObj());
                rsPO.updateDouble("COLUMN_7_PER", getAttribute("COLUMN_7_PER").getVal());
                rsPO.updateDouble("COLUMN_7_AMT", getAttribute("COLUMN_7_AMT").getVal());
                rsPO.updateString("COLUMN_7_CAPTION", (String) getAttribute("COLUMN_7_CAPTION").getObj());
                rsPO.updateInt("COLUMN_8_ID", (int) getAttribute("COLUMN_8_ID").getVal());
                rsPO.updateString("COLUMN_8_FORMULA", (String) getAttribute("COLUMN_8_FORMULA").getObj());
                rsPO.updateDouble("COLUMN_8_PER", getAttribute("COLUMN_8_PER").getVal());
                rsPO.updateDouble("COLUMN_8_AMT", getAttribute("COLUMN_8_AMT").getVal());
                rsPO.updateString("COLUMN_8_CAPTION", (String) getAttribute("COLUMN_8_CAPTION").getObj());
                rsPO.updateInt("COLUMN_9_ID", (int) getAttribute("COLUMN_9_ID").getVal());
                rsPO.updateString("COLUMN_9_FORMULA", (String) getAttribute("COLUMN_9_FORMULA").getObj());
                rsPO.updateDouble("COLUMN_9_PER", getAttribute("COLUMN_9_PER").getVal());
                rsPO.updateDouble("COLUMN_9_AMT", getAttribute("COLUMN_9_AMT").getVal());
                rsPO.updateString("COLUMN_9_CAPTION", (String) getAttribute("COLUMN_9_CAPTION").getObj());
                rsPO.updateInt("COLUMN_10_ID", (int) getAttribute("COLUMN_10_ID").getVal());
                rsPO.updateString("COLUMN_10_FORMULA", (String) getAttribute("COLUMN_10_FORMULA").getObj());
                rsPO.updateDouble("COLUMN_10_PER", getAttribute("COLUMN_10_PER").getVal());
                rsPO.updateDouble("COLUMN_10_AMT", getAttribute("COLUMN_10_AMT").getVal());
                rsPO.updateString("COLUMN_10_CAPTION", (String) getAttribute("COLUMN_10_CAPTION").getObj());

                rsPO.updateInt("COLUMN_11_ID", (int) getAttribute("COLUMN_11_ID").getVal());
                rsPO.updateString("COLUMN_11_FORMULA", (String) getAttribute("COLUMN_11_FORMULA").getObj());
                rsPO.updateDouble("COLUMN_11_PER", getAttribute("COLUMN_11_PER").getVal());
                rsPO.updateDouble("COLUMN_11_AMT", getAttribute("COLUMN_11_AMT").getVal());
                rsPO.updateString("COLUMN_11_CAPTION", (String) getAttribute("COLUMN_11_CAPTION").getObj());
                rsPO.updateInt("COLUMN_12_ID", (int) getAttribute("COLUMN_12_ID").getVal());
                rsPO.updateString("COLUMN_12_FORMULA", (String) getAttribute("COLUMN_12_FORMULA").getObj());
                rsPO.updateDouble("COLUMN_12_PER", getAttribute("COLUMN_12_PER").getVal());
                rsPO.updateDouble("COLUMN_12_AMT", getAttribute("COLUMN_12_AMT").getVal());
                rsPO.updateString("COLUMN_12_CAPTION", (String) getAttribute("COLUMN_12_CAPTION").getObj());
                rsPO.updateInt("COLUMN_13_ID", (int) getAttribute("COLUMN_13_ID").getVal());
                rsPO.updateString("COLUMN_13_FORMULA", (String) getAttribute("COLUMN_13_FORMULA").getObj());
                rsPO.updateDouble("COLUMN_13_PER", getAttribute("COLUMN_13_PER").getVal());
                rsPO.updateDouble("COLUMN_13_AMT", getAttribute("COLUMN_13_AMT").getVal());
                rsPO.updateString("COLUMN_13_CAPTION", (String) getAttribute("COLUMN_13_CAPTION").getObj());
                rsPO.updateInt("COLUMN_14_ID", (int) getAttribute("COLUMN_14_ID").getVal());
                rsPO.updateString("COLUMN_14_FORMULA", (String) getAttribute("COLUMN_14_FORMULA").getObj());
                rsPO.updateDouble("COLUMN_14_PER", getAttribute("COLUMN_14_PER").getVal());
                rsPO.updateDouble("COLUMN_14_AMT", getAttribute("COLUMN_14_AMT").getVal());
                rsPO.updateString("COLUMN_14_CAPTION", (String) getAttribute("COLUMN_14_CAPTION").getObj());
                rsPO.updateInt("COLUMN_15_ID", (int) getAttribute("COLUMN_15_ID").getVal());
                rsPO.updateString("COLUMN_15_FORMULA", (String) getAttribute("COLUMN_15_FORMULA").getObj());
                rsPO.updateDouble("COLUMN_15_PER", getAttribute("COLUMN_15_PER").getVal());
                rsPO.updateDouble("COLUMN_15_AMT", getAttribute("COLUMN_15_AMT").getVal());
                rsPO.updateString("COLUMN_15_CAPTION", (String) getAttribute("COLUMN_15_CAPTION").getObj());
                rsPO.updateInt("COLUMN_16_ID", (int) getAttribute("COLUMN_16_ID").getVal());
                rsPO.updateString("COLUMN_16_FORMULA", (String) getAttribute("COLUMN_16_FORMULA").getObj());
                rsPO.updateDouble("COLUMN_16_PER", getAttribute("COLUMN_16_PER").getVal());
                rsPO.updateDouble("COLUMN_16_AMT", getAttribute("COLUMN_16_AMT").getVal());
                rsPO.updateString("COLUMN_16_CAPTION", (String) getAttribute("COLUMN_16_CAPTION").getObj());
                rsPO.updateInt("COLUMN_17_ID", (int) getAttribute("COLUMN_17_ID").getVal());
                rsPO.updateString("COLUMN_17_FORMULA", (String) getAttribute("COLUMN_17_FORMULA").getObj());
                rsPO.updateDouble("COLUMN_17_PER", getAttribute("COLUMN_17_PER").getVal());
                rsPO.updateDouble("COLUMN_17_AMT", getAttribute("COLUMN_17_AMT").getVal());
                rsPO.updateString("COLUMN_17_CAPTION", (String) getAttribute("COLUMN_17_CAPTION").getObj());
                rsPO.updateInt("COLUMN_18_ID", (int) getAttribute("COLUMN_18_ID").getVal());
                rsPO.updateString("COLUMN_18_FORMULA", (String) getAttribute("COLUMN_18_FORMULA").getObj());
                rsPO.updateDouble("COLUMN_18_PER", getAttribute("COLUMN_18_PER").getVal());
                rsPO.updateDouble("COLUMN_18_AMT", getAttribute("COLUMN_18_AMT").getVal());
                rsPO.updateString("COLUMN_18_CAPTION", (String) getAttribute("COLUMN_18_CAPTION").getObj());
                rsPO.updateInt("COLUMN_19_ID", (int) getAttribute("COLUMN_19_ID").getVal());
                rsPO.updateString("COLUMN_19_FORMULA", (String) getAttribute("COLUMN_19_FORMULA").getObj());
                rsPO.updateDouble("COLUMN_19_PER", getAttribute("COLUMN_19_PER").getVal());
                rsPO.updateDouble("COLUMN_19_AMT", getAttribute("COLUMN_19_AMT").getVal());
                rsPO.updateString("COLUMN_19_CAPTION", (String) getAttribute("COLUMN_19_CAPTION").getObj());
                rsPO.updateInt("COLUMN_20_ID", (int) getAttribute("COLUMN_20_ID").getVal());
                rsPO.updateString("COLUMN_20_FORMULA", (String) getAttribute("COLUMN_20_FORMULA").getObj());
                rsPO.updateDouble("COLUMN_20_PER", getAttribute("COLUMN_20_PER").getVal());
                rsPO.updateDouble("COLUMN_20_AMT", getAttribute("COLUMN_20_AMT").getVal());
                rsPO.updateString("COLUMN_20_CAPTION", (String) getAttribute("COLUMN_20_CAPTION").getObj());

                rsPO.updateInt("COLUMN_21_ID", (int) getAttribute("COLUMN_21_ID").getVal());
                rsPO.updateString("COLUMN_21_FORMULA", (String) getAttribute("COLUMN_21_FORMULA").getObj());
                rsPO.updateDouble("COLUMN_21_PER", getAttribute("COLUMN_21_PER").getVal());
                rsPO.updateDouble("COLUMN_21_AMT", getAttribute("COLUMN_21_AMT").getVal());
                rsPO.updateString("COLUMN_21_CAPTION", (String) getAttribute("COLUMN_21_CAPTION").getObj());

                rsPO.updateBoolean("IMPORT_CONCESS", getAttribute("IMPORT_CONCESS").getBool());
                rsPO.updateString("PRINT_LINE_1", (String) getAttribute("PRINT_LINE_1").getObj());
                rsPO.updateString("PRINT_LINE_2", (String) getAttribute("PRINT_LINE_2").getObj());
                rsPO.updateString("IMPORT_LICENSE", (String) getAttribute("IMPORT_LICENSE").getObj());
                rsPO.updateBoolean("CHANGED", true);
                rsPO.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

                rsPO.updateString("PAYMENT_TERM", (String) getAttribute("PAYMENT_TERM").getObj());
                rsPO.updateInt("PAYMENT_CODE", getAttribute("PAYMENT_CODE").getInt());
                rsPO.updateInt("CR_DAYS", getAttribute("CR_DAYS").getInt());
                rsPO.updateInt("DEPT_ID", getAttribute("DEPT_ID").getInt());

                rsPO.updateString("PRICE_BASIS_TERM", (String) getAttribute("PRICE_BASIS_TERM").getObj());
                rsPO.updateString("DISCOUNT_TERM", (String) getAttribute("DISCOUNT_TERM").getObj());
                rsPO.updateString("EXCISE_TERM", (String) getAttribute("EXCISE_TERM").getObj());
                rsPO.updateString("ST_TERM", (String) getAttribute("ST_TERM").getObj());
                rsPO.updateString("PF_TERM", (String) getAttribute("PF_TERM").getObj());
                rsPO.updateString("FREIGHT_TERM", (String) getAttribute("FREIGHT_TERM").getObj());
                rsPO.updateString("OCTROI_TERM", (String) getAttribute("OCTROI_TERM").getObj());
                rsPO.updateString("FOB_TERM", (String) getAttribute("FOB_TERM").getObj());
                rsPO.updateString("CIE_TERM", (String) getAttribute("CIE_TERM").getObj());
                rsPO.updateString("INSURANCE_TERM", (String) getAttribute("INSURANCE_TERM").getObj());
                rsPO.updateString("TCC_TERM", (String) getAttribute("TCC_TERM").getObj());
                rsPO.updateString("CENVAT_TERM", (String) getAttribute("CENVAT_TERM").getObj());
                rsPO.updateString("DESPATCH_TERM", (String) getAttribute("DESPATCH_TERM").getObj());
                rsPO.updateString("SERVICE_TAX_TERM", (String) getAttribute("SERVICE_TAX_TERM").getObj());
                rsPO.updateString("OTHERS_TERM", (String) getAttribute("OTHERS_TERM").getObj());
                rsPO.updateString("COVERING_TEXT", (String) getAttribute("COVERING_TEXT").getObj());
                
                rsPO.updateString("CGST_TERM", (String) getAttribute("CGST_TERM").getObj());
                rsPO.updateString("SGST_TERM", (String) getAttribute("SGST_TERM").getObj());
                rsPO.updateString("IGST_TERM", (String) getAttribute("IGST_TERM").getObj());
                rsPO.updateString("COMPOSITION_TERM", (String) getAttribute("COMPOSITION_TERM").getObj());
                rsPO.updateString("RCM_TERM", (String) getAttribute("RCM_TERM").getObj());
                rsPO.updateString("GST_COMPENSATION_CESS_TERM", (String) getAttribute("GST_COMPENSATION_CESS_TERM").getObj());
                
                rsPO.updateString("PREMISES_TYPE", (String) getAttribute("PREMISES_TYPE").getObj());
                rsPO.updateString("SCOPE", (String) getAttribute("SCOPE").getObj());
                rsPO.updateString("SERVICE_PERIOD", (String) getAttribute("SERVICE_PERIOD").getObj());
                rsPO.updateString("SERVICE_FREQUENCY", (String) getAttribute("SERVICE_FREQUENCY").getObj());
                rsPO.updateString("CONTRACT_DETAILS", (String) getAttribute("CONTRACT_DETAILS").getObj());
                rsPO.updateString("SERVICE_REPORT", (String) getAttribute("SERVICE_REPORT").getObj());
                rsPO.updateString("ESI_TERMS", (String) getAttribute("ESI_TERMS").getObj());
                rsPO.updateString("TERMINATION_TERMS", (String) getAttribute("TERMINATION_TERMS").getObj());
                rsPO.updateString("FILE_TEXT", (String) getAttribute("FILE_TEXT").getObj());
                rsPO.updateString("AMOUNT_IN_WORDS", (String) getAttribute("AMOUNT_IN_WORDS").getObj());
                rsPO.updateString("DIRECTOR_APPROVAL", (String) getAttribute("DIRECTOR_APPROVAL").getObj());
                rsPO.updateString("IMPORTED", (String) getAttribute("IMPORTED").getObj());

                rsPO.insertRow();

                //====== Now turn of P.O. Amendment Items ======
                stPOLine = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                rsPOLine = stPOLine.executeQuery("SELECT * FROM D_PUR_PO_DETAIL WHERE PO_NO='1' ");
                rsPOLine.first();

                for (int i = 1; i <= colPOItems.size(); i++) {
                    clsPOAmendItem ObjItem = (clsPOAmendItem) colPOItems.get(Integer.toString(i));
                    rsPOLine.moveToInsertRow();
                    rsPOLine.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
                    rsPOLine.updateString("PO_NO", (String) getAttribute("PO_NO").getObj());
                    rsPOLine.updateInt("SR_NO", i);
                    rsPOLine.updateInt("PO_TYPE", POType);
                    rsPOLine.updateString("ITEM_ID", (String) ObjItem.getAttribute("ITEM_ID").getObj());
                    rsPOLine.updateString("VENDOR_SHADE", (String) ObjItem.getAttribute("VENDOR_SHADE").getObj());
                    rsPOLine.updateString("SDML_SHADE", (String) ObjItem.getAttribute("SDML_SHADE").getObj());
                    rsPOLine.updateString("ITEM_DESC", (String) ObjItem.getAttribute("ITEM_DESC").getObj());
                    rsPOLine.updateString("HSN_SAC_CODE", (String) ObjItem.getAttribute("HSN_SAC_CODE").getObj());
                    rsPOLine.updateString("MAKE", (String) ObjItem.getAttribute("MAKE").getObj());
                    rsPOLine.updateString("PRICE_LIST_NO", (String) ObjItem.getAttribute("PRICE_LIST_NO").getObj());
                    rsPOLine.updateString("PART_NO", (String) ObjItem.getAttribute("PART_NO").getObj());
                    rsPOLine.updateString("EXCISE_TARRIF_NO", (String) ObjItem.getAttribute("EXCISE_TARRIF_NO").getObj());
                    rsPOLine.updateDouble("QTY", ObjItem.getAttribute("QTY").getVal());
                    rsPOLine.updateDouble("TOLERANCE_LIMIT", ObjItem.getAttribute("TOLERANCE_LIMIT").getVal());
                    rsPOLine.updateDouble("PENDING_QTY", ObjItem.getAttribute("PENDING_QTY").getVal());
                    rsPOLine.updateDouble("RECD_QTY", ObjItem.getAttribute("RECD_QTY").getVal());
                    rsPOLine.updateInt("UNIT", (int) ObjItem.getAttribute("UNIT").getVal());
                    int t = (int) ObjItem.getAttribute("DEPT_ID").getVal();
                    rsPOLine.updateInt("DEPT_ID", (int) ObjItem.getAttribute("DEPT_ID").getVal());
                    rsPOLine.updateDouble("RATE", ObjItem.getAttribute("RATE").getVal());
                    rsPOLine.updateDouble("LANDED_RATE", ObjItem.getAttribute("LANDED_RATE").getVal());
                    rsPOLine.updateDouble("DISC_PER", ObjItem.getAttribute("DISC_PER").getVal());
                    rsPOLine.updateDouble("DISC_AMT", ObjItem.getAttribute("DISC_AMT").getVal());
                    rsPOLine.updateDouble("TOTAL_AMOUNT", ObjItem.getAttribute("TOTAL_AMOUNT").getVal());
                    rsPOLine.updateDouble("NET_AMOUNT", ObjItem.getAttribute("NET_AMOUNT").getVal());
                    rsPOLine.updateString("INDENT_NO", (String) ObjItem.getAttribute("INDENT_NO").getObj());
                    rsPOLine.updateInt("INDENT_SR_NO", (int) ObjItem.getAttribute("INDENT_SR_NO").getVal());
                    rsPOLine.updateString("REFERENCE", (String) ObjItem.getAttribute("REFERENCE").getObj());
                    rsPOLine.updateString("DELIVERY_DATE", (String) ObjItem.getAttribute("DELIVERY_DATE").getObj());
                    rsPOLine.updateString("REMARKS", (String) ObjItem.getAttribute("REMARKS").getObj());
                    rsPOLine.updateLong("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                    rsPOLine.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    //rsPOLine.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
                    //rsPOLine.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                    rsPOLine.updateInt("COLUMN_1_ID", (int) ObjItem.getAttribute("COLUMN_1_ID").getVal());
                    rsPOLine.updateString("COLUMN_1_FORMULA", (String) ObjItem.getAttribute("COLUMN_1_FORMULA").getObj());
                    rsPOLine.updateDouble("COLUMN_1_PER", ObjItem.getAttribute("COLUMN_1_PER").getVal());
                    rsPOLine.updateDouble("COLUMN_1_AMT", ObjItem.getAttribute("COLUMN_1_AMT").getVal());
                    rsPOLine.updateString("COLUMN_1_CAPTION", (String) ObjItem.getAttribute("COLUMN_1_CAPTION").getObj());
                    rsPOLine.updateInt("COLUMN_2_ID", (int) ObjItem.getAttribute("COLUMN_2_ID").getVal());
                    rsPOLine.updateString("COLUMN_2_FORMULA", (String) ObjItem.getAttribute("COLUMN_2_FORMULA").getObj());
                    rsPOLine.updateDouble("COLUMN_2_PER", ObjItem.getAttribute("COLUMN_2_PER").getVal());
                    rsPOLine.updateDouble("COLUMN_2_AMT", ObjItem.getAttribute("COLUMN_2_AMT").getVal());
                    rsPOLine.updateString("COLUMN_2_CAPTION", (String) ObjItem.getAttribute("COLUMN_2_CAPTION").getObj());
                    rsPOLine.updateInt("COLUMN_3_ID", (int) ObjItem.getAttribute("COLUMN_3_ID").getVal());
                    rsPOLine.updateString("COLUMN_3_FORMULA", (String) ObjItem.getAttribute("COLUMN_3_FORMULA").getObj());
                    rsPOLine.updateDouble("COLUMN_3_PER", ObjItem.getAttribute("COLUMN_3_PER").getVal());
                    rsPOLine.updateDouble("COLUMN_3_AMT", ObjItem.getAttribute("COLUMN_3_AMT").getVal());
                    rsPOLine.updateString("COLUMN_3_CAPTION", (String) ObjItem.getAttribute("COLUMN_3_CAPTION").getObj());
                    rsPOLine.updateInt("COLUMN_4_ID", (int) ObjItem.getAttribute("COLUMN_4_ID").getVal());
                    rsPOLine.updateString("COLUMN_4_FORMULA", (String) ObjItem.getAttribute("COLUMN_4_FORMULA").getObj());
                    rsPOLine.updateDouble("COLUMN_4_PER", ObjItem.getAttribute("COLUMN_4_PER").getVal());
                    rsPOLine.updateDouble("COLUMN_4_AMT", ObjItem.getAttribute("COLUMN_4_AMT").getVal());
                    rsPOLine.updateString("COLUMN_4_CAPTION", (String) ObjItem.getAttribute("COLUMN_4_CAPTION").getObj());
                    rsPOLine.updateInt("COLUMN_5_ID", (int) ObjItem.getAttribute("COLUMN_5_ID").getVal());
                    rsPOLine.updateString("COLUMN_5_FORMULA", (String) ObjItem.getAttribute("COLUMN_5_FORMULA").getObj());
                    rsPOLine.updateDouble("COLUMN_5_PER", ObjItem.getAttribute("COLUMN_5_PER").getVal());
                    rsPOLine.updateDouble("COLUMN_5_AMT", ObjItem.getAttribute("COLUMN_5_AMT").getVal());
                    rsPOLine.updateString("COLUMN_5_CAPTION", (String) ObjItem.getAttribute("COLUMN_5_CAPTION").getObj());
                    rsPOLine.updateInt("COLUMN_6_ID", (int) ObjItem.getAttribute("COLUMN_6_ID").getVal());
                    rsPOLine.updateString("COLUMN_6_FORMULA", (String) ObjItem.getAttribute("COLUMN_6_FORMULA").getObj());
                    rsPOLine.updateDouble("COLUMN_6_PER", ObjItem.getAttribute("COLUMN_6_PER").getVal());
                    rsPOLine.updateDouble("COLUMN_6_AMT", ObjItem.getAttribute("COLUMN_6_AMT").getVal());
                    rsPOLine.updateString("COLUMN_6_CAPTION", (String) ObjItem.getAttribute("COLUMN_6_CAPTION").getObj());
                    rsPOLine.updateInt("COLUMN_7_ID", (int) ObjItem.getAttribute("COLUMN_7_ID").getVal());
                    rsPOLine.updateString("COLUMN_7_FORMULA", (String) ObjItem.getAttribute("COLUMN_7_FORMULA").getObj());
                    rsPOLine.updateDouble("COLUMN_7_PER", ObjItem.getAttribute("COLUMN_7_PER").getVal());
                    rsPOLine.updateDouble("COLUMN_7_AMT", ObjItem.getAttribute("COLUMN_7_AMT").getVal());
                    rsPOLine.updateString("COLUMN_7_CAPTION", (String) ObjItem.getAttribute("COLUMN_7_CAPTION").getObj());
                    rsPOLine.updateInt("COLUMN_8_ID", (int) ObjItem.getAttribute("COLUMN_8_ID").getVal());
                    rsPOLine.updateString("COLUMN_8_FORMULA", (String) ObjItem.getAttribute("COLUMN_8_FORMULA").getObj());
                    rsPOLine.updateDouble("COLUMN_8_PER", ObjItem.getAttribute("COLUMN_8_PER").getVal());
                    rsPOLine.updateDouble("COLUMN_8_AMT", ObjItem.getAttribute("COLUMN_8_AMT").getVal());
                    rsPOLine.updateString("COLUMN_8_CAPTION", (String) ObjItem.getAttribute("COLUMN_8_CAPTION").getObj());
                    rsPOLine.updateInt("COLUMN_9_ID", (int) ObjItem.getAttribute("COLUMN_9_ID").getVal());
                    rsPOLine.updateString("COLUMN_9_FORMULA", (String) ObjItem.getAttribute("COLUMN_9_FORMULA").getObj());
                    rsPOLine.updateDouble("COLUMN_9_PER", ObjItem.getAttribute("COLUMN_9_PER").getVal());
                    rsPOLine.updateDouble("COLUMN_9_AMT", ObjItem.getAttribute("COLUMN_9_AMT").getVal());
                    rsPOLine.updateString("COLUMN_9_CAPTION", (String) ObjItem.getAttribute("COLUMN_9_CAPTION").getObj());
                    rsPOLine.updateInt("COLUMN_10_ID", (int) ObjItem.getAttribute("COLUMN_10_ID").getVal());
                    rsPOLine.updateString("COLUMN_10_FORMULA", (String) ObjItem.getAttribute("COLUMN_10_FORMULA").getObj());
                    rsPOLine.updateDouble("COLUMN_10_PER", ObjItem.getAttribute("COLUMN_10_PER").getVal());
                    rsPOLine.updateDouble("COLUMN_10_AMT", ObjItem.getAttribute("COLUMN_10_AMT").getVal());
                    rsPOLine.updateString("COLUMN_10_CAPTION", (String) ObjItem.getAttribute("COLUMN_10_CAPTION").getObj());

                    rsPOLine.updateInt("COLUMN_11_ID", (int) ObjItem.getAttribute("COLUMN_11_ID").getVal());
                    rsPOLine.updateString("COLUMN_11_FORMULA", (String) ObjItem.getAttribute("COLUMN_11_FORMULA").getObj());
                    rsPOLine.updateDouble("COLUMN_11_PER", ObjItem.getAttribute("COLUMN_11_PER").getVal());
                    rsPOLine.updateDouble("COLUMN_11_AMT", ObjItem.getAttribute("COLUMN_11_AMT").getVal());
                    rsPOLine.updateString("COLUMN_11_CAPTION", (String) ObjItem.getAttribute("COLUMN_11_CAPTION").getObj());
                    rsPOLine.updateInt("COLUMN_12_ID", (int) ObjItem.getAttribute("COLUMN_12_ID").getVal());
                    rsPOLine.updateString("COLUMN_12_FORMULA", (String) ObjItem.getAttribute("COLUMN_12_FORMULA").getObj());
                    rsPOLine.updateDouble("COLUMN_12_PER", ObjItem.getAttribute("COLUMN_12_PER").getVal());
                    rsPOLine.updateDouble("COLUMN_12_AMT", ObjItem.getAttribute("COLUMN_12_AMT").getVal());
                    rsPOLine.updateString("COLUMN_12_CAPTION", (String) ObjItem.getAttribute("COLUMN_12_CAPTION").getObj());
                    rsPOLine.updateInt("COLUMN_13_ID", (int) ObjItem.getAttribute("COLUMN_13_ID").getVal());
                    rsPOLine.updateString("COLUMN_13_FORMULA", (String) ObjItem.getAttribute("COLUMN_13_FORMULA").getObj());
                    rsPOLine.updateDouble("COLUMN_13_PER", ObjItem.getAttribute("COLUMN_13_PER").getVal());
                    rsPOLine.updateDouble("COLUMN_13_AMT", ObjItem.getAttribute("COLUMN_13_AMT").getVal());
                    rsPOLine.updateString("COLUMN_13_CAPTION", (String) ObjItem.getAttribute("COLUMN_13_CAPTION").getObj());
                    rsPOLine.updateInt("COLUMN_14_ID", (int) ObjItem.getAttribute("COLUMN_14_ID").getVal());
                    rsPOLine.updateString("COLUMN_14_FORMULA", (String) ObjItem.getAttribute("COLUMN_14_FORMULA").getObj());
                    rsPOLine.updateDouble("COLUMN_14_PER", ObjItem.getAttribute("COLUMN_14_PER").getVal());
                    rsPOLine.updateDouble("COLUMN_14_AMT", ObjItem.getAttribute("COLUMN_14_AMT").getVal());
                    rsPOLine.updateString("COLUMN_14_CAPTION", (String) ObjItem.getAttribute("COLUMN_14_CAPTION").getObj());
                    rsPOLine.updateInt("COLUMN_15_ID", (int) ObjItem.getAttribute("COLUMN_15_ID").getVal());
                    rsPOLine.updateString("COLUMN_15_FORMULA", (String) ObjItem.getAttribute("COLUMN_15_FORMULA").getObj());
                    rsPOLine.updateDouble("COLUMN_15_PER", ObjItem.getAttribute("COLUMN_15_PER").getVal());
                    rsPOLine.updateDouble("COLUMN_15_AMT", ObjItem.getAttribute("COLUMN_15_AMT").getVal());
                    rsPOLine.updateString("COLUMN_15_CAPTION", (String) ObjItem.getAttribute("COLUMN_15_CAPTION").getObj());
                    rsPOLine.updateInt("COLUMN_16_ID", (int) ObjItem.getAttribute("COLUMN_16_ID").getVal());
                    rsPOLine.updateString("COLUMN_16_FORMULA", (String) ObjItem.getAttribute("COLUMN_16_FORMULA").getObj());
                    rsPOLine.updateDouble("COLUMN_16_PER", ObjItem.getAttribute("COLUMN_16_PER").getVal());
                    rsPOLine.updateDouble("COLUMN_16_AMT", ObjItem.getAttribute("COLUMN_16_AMT").getVal());
                    rsPOLine.updateString("COLUMN_16_CAPTION", (String) ObjItem.getAttribute("COLUMN_16_CAPTION").getObj());
                    rsPOLine.updateInt("COLUMN_17_ID", (int) ObjItem.getAttribute("COLUMN_17_ID").getVal());
                    rsPOLine.updateString("COLUMN_17_FORMULA", (String) ObjItem.getAttribute("COLUMN_17_FORMULA").getObj());
                    rsPOLine.updateDouble("COLUMN_17_PER", ObjItem.getAttribute("COLUMN_17_PER").getVal());
                    rsPOLine.updateDouble("COLUMN_17_AMT", ObjItem.getAttribute("COLUMN_17_AMT").getVal());
                    rsPOLine.updateString("COLUMN_17_CAPTION", (String) ObjItem.getAttribute("COLUMN_17_CAPTION").getObj());
                    rsPOLine.updateInt("COLUMN_18_ID", (int) ObjItem.getAttribute("COLUMN_18_ID").getVal());
                    rsPOLine.updateString("COLUMN_18_FORMULA", (String) ObjItem.getAttribute("COLUMN_18_FORMULA").getObj());
                    rsPOLine.updateDouble("COLUMN_18_PER", ObjItem.getAttribute("COLUMN_18_PER").getVal());
                    rsPOLine.updateDouble("COLUMN_18_AMT", ObjItem.getAttribute("COLUMN_18_AMT").getVal());
                    rsPOLine.updateString("COLUMN_18_CAPTION", (String) ObjItem.getAttribute("COLUMN_18_CAPTION").getObj());
                    rsPOLine.updateInt("COLUMN_19_ID", (int) ObjItem.getAttribute("COLUMN_19_ID").getVal());
                    rsPOLine.updateString("COLUMN_19_FORMULA", (String) ObjItem.getAttribute("COLUMN_19_FORMULA").getObj());
                    rsPOLine.updateDouble("COLUMN_19_PER", ObjItem.getAttribute("COLUMN_19_PER").getVal());
                    rsPOLine.updateDouble("COLUMN_19_AMT", ObjItem.getAttribute("COLUMN_19_AMT").getVal());
                    rsPOLine.updateString("COLUMN_19_CAPTION", (String) ObjItem.getAttribute("COLUMN_19_CAPTION").getObj());
                    rsPOLine.updateInt("COLUMN_20_ID", (int) ObjItem.getAttribute("COLUMN_20_ID").getVal());
                    rsPOLine.updateString("COLUMN_20_FORMULA", (String) ObjItem.getAttribute("COLUMN_20_FORMULA").getObj());
                    rsPOLine.updateDouble("COLUMN_20_PER", ObjItem.getAttribute("COLUMN_20_PER").getVal());
                    rsPOLine.updateDouble("COLUMN_20_AMT", ObjItem.getAttribute("COLUMN_20_AMT").getVal());
                    rsPOLine.updateString("COLUMN_20_CAPTION", (String) ObjItem.getAttribute("COLUMN_20_CAPTION").getObj());

                    rsPOLine.updateInt("COLUMN_21_ID", (int) ObjItem.getAttribute("COLUMN_21_ID").getVal());
                    rsPOLine.updateString("COLUMN_21_FORMULA", (String) ObjItem.getAttribute("COLUMN_21_FORMULA").getObj());
                    rsPOLine.updateDouble("COLUMN_21_PER", ObjItem.getAttribute("COLUMN_21_PER").getVal());
                    rsPOLine.updateDouble("COLUMN_21_AMT", ObjItem.getAttribute("COLUMN_21_AMT").getVal());
                    rsPOLine.updateString("COLUMN_21_CAPTION", (String) ObjItem.getAttribute("COLUMN_21_CAPTION").getObj());

                    rsPOLine.updateBoolean("EXCISE_GATEPASS_GIVEN", ObjItem.getAttribute("EXCISE_GATEPASS_GIVEN").getBool());
                    rsPOLine.updateBoolean("IMPORT_CONCESS", ObjItem.getAttribute("IMPORT_CONCESS").getBool());
                    rsPOLine.updateString("QUOT_ID", (String) ObjItem.getAttribute("QUOT_ID").getObj());
                    rsPOLine.updateInt("QUOT_SR_NO", (int) ObjItem.getAttribute("QUOT_SR_NO").getVal());
                    rsPOLine.updateBoolean("CHANGED", true);
                    rsPOLine.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsPOLine.insertRow();
                }

                //====== Now turn of P.O. Terms ======
                stTerms = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                rsTerms = stTerms.executeQuery("SELECT * FROM D_PUR_PO_TERMS WHERE PO_NO='1' ");
                rsTerms.first();

                for (int i = 1; i <= colPOTerms.size(); i++) {
                    clsPOTerms ObjItem = (clsPOTerms) colPOTerms.get(Integer.toString(i));
                    rsTerms.moveToInsertRow();
                    rsTerms.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
                    rsTerms.updateString("PO_NO", (String) getAttribute("PO_NO").getObj());
                    rsTerms.updateInt("SR_NO", i);
                    rsTerms.updateInt("PO_TYPE", POType);
                    rsTerms.updateString("TERM_TYPE", (String) ObjItem.getAttribute("TERM_TYPE").getObj());
                    rsTerms.updateInt("TERM_CODE", (int) ObjItem.getAttribute("TERM_CODE").getVal());
                    rsTerms.updateString("TERM_DESC", (String) ObjItem.getAttribute("TERM_DESC").getObj());
                    rsTerms.updateBoolean("CHANGED", true);
                    rsTerms.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsTerms.insertRow();
                }

                // ---------  Now copy PO Object to current object ------------------//
                setAttribute("COMPANY_ID", (int) ObjPO.getAttribute("COMPANY_ID").getVal());
                setAttribute("PO_NO", (String) ObjPO.getAttribute("PO_NO").getObj());
                setAttribute("DOC_ID", (long) ObjPO.getAttribute("DOC_ID").getVal());
                setAttribute("PO_DATE", (String) ObjPO.getAttribute("PO_DATE").getObj());
                setAttribute("PO_TYPE", POType);
                setAttribute("PO_REF", (String) ObjPO.getAttribute("PO_REF").getObj());
                setAttribute("SUPP_ID", (String) ObjPO.getAttribute("SUPP_ID").getObj());
                setAttribute("SUPP_NAME", (String) ObjPO.getAttribute("SUPP_NAME").getObj());
                setAttribute("REF_A", (String) ObjPO.getAttribute("REF_A").getObj());
                setAttribute("REF_B", (String) ObjPO.getAttribute("REF_B").getObj());
                setAttribute("QUOTATION_NO", (String) ObjPO.getAttribute("QUOTATION_NO").getObj());
                setAttribute("QUOTATION_DATE", (String) ObjPO.getAttribute("QUOTATION_DATE").getObj());
                setAttribute("INQUIRY_NO", (String) ObjPO.getAttribute("INQUIRY_NO").getObj());
                setAttribute("INQUIRY_DATE", (String) ObjPO.getAttribute("INQUIRY_DATE").getObj());
                setAttribute("BUYER", (int) ObjPO.getAttribute("BUYER").getVal());
                setAttribute("TRANSPORT_MODE", (int) ObjPO.getAttribute("TRANSPORT_MODE").getVal());
                setAttribute("PURPOSE", (String) ObjPO.getAttribute("PURPOSE").getObj());
                setAttribute("SUBJECT", (String) ObjPO.getAttribute("SUBJECT").getObj());
                setAttribute("CURRENCY_ID", (int) ObjPO.getAttribute("CURRENCY_ID").getVal());
                setAttribute("CURRENCY_RATE", ObjPO.getAttribute("CURRENCY_RATE").getVal());
                setAttribute("TOTAL_AMOUNT", ObjPO.getAttribute("TOTAL_AMOUNT").getVal());
                setAttribute("NET_AMOUNT", ObjPO.getAttribute("NET_AMOUNT").getVal());
                setAttribute("STATUS", (String) ObjPO.getAttribute("STATUS").getObj());
                setAttribute("ATTACHEMENT", ObjPO.getAttribute("ATTACHEMENT").getBool());
                setAttribute("ATTACHEMENT_PATH", (String) ObjPO.getAttribute("ATTACHEMENT_PATH").getObj());
                setAttribute("REMARKS", (String) ObjPO.getAttribute("REMARKS").getObj());
                setAttribute("SHIP_ID", (int) ObjPO.getAttribute("SHIP_ID").getVal());
                setAttribute("CANCELLED", ObjPO.getAttribute("CANCELLED").getBool());
                //setAttribute("HIERARCHY_ID",(int)ObjPO.getAttribute("HIERARCHY_ID").getVal());
                setAttribute("CREATED_BY", (int) ObjPO.getAttribute("CREATED_BY").getVal());
                setAttribute("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                //setAttribute("MODIFIED_BY",(int) ObjPO.getAttribute("MODIFIED_BY").getVal());
                //setAttribute("MODIFIED_DATE",(String)ObjPO.getAttribute("MODIFIED_DATE").getObj());
                //Now Custom Columns
                setAttribute("COLUMN_1_ID", (int) ObjPO.getAttribute("COLUMN_1_ID").getVal());
                setAttribute("COLUMN_1_FORMULA", (String) ObjPO.getAttribute("COLUMN_1_FORMULA").getObj());
                setAttribute("COLUMN_1_PER", ObjPO.getAttribute("COLUMN_1_PER").getVal());
                setAttribute("COLUMN_1_AMT", ObjPO.getAttribute("COLUMN_1_AMT").getVal());
                setAttribute("COLUMN_1_CAPTION", (String) ObjPO.getAttribute("COLUMN_1_CAPTION").getObj());
                setAttribute("COLUMN_2_ID", (int) ObjPO.getAttribute("COLUMN_2_ID").getVal());
                setAttribute("COLUMN_2_FORMULA", (String) ObjPO.getAttribute("COLUMN_2_FORMULA").getObj());
                setAttribute("COLUMN_2_PER", ObjPO.getAttribute("COLUMN_2_PER").getVal());
                setAttribute("COLUMN_2_AMT", ObjPO.getAttribute("COLUMN_2_AMT").getVal());
                setAttribute("COLUMN_2_CAPTION", (String) ObjPO.getAttribute("COLUMN_2_CAPTION").getObj());
                setAttribute("COLUMN_3_ID", (int) ObjPO.getAttribute("COLUMN_3_ID").getVal());
                setAttribute("COLUMN_3_FORMULA", (String) ObjPO.getAttribute("COLUMN_3_FORMULA").getObj());
                setAttribute("COLUMN_3_PER", ObjPO.getAttribute("COLUMN_3_PER").getVal());
                setAttribute("COLUMN_3_AMT", ObjPO.getAttribute("COLUMN_3_AMT").getVal());
                setAttribute("COLUMN_3_CAPTION", (String) ObjPO.getAttribute("COLUMN_3_CAPTION").getObj());
                setAttribute("COLUMN_4_ID", (int) ObjPO.getAttribute("COLUMN_4_ID").getVal());
                setAttribute("COLUMN_4_FORMULA", (String) ObjPO.getAttribute("COLUMN_4_FORMULA").getObj());
                setAttribute("COLUMN_4_PER", ObjPO.getAttribute("COLUMN_4_PER").getVal());
                setAttribute("COLUMN_4_AMT", ObjPO.getAttribute("COLUMN_4_AMT").getVal());
                setAttribute("COLUMN_4_CAPTION", (String) ObjPO.getAttribute("COLUMN_4_CAPTION").getObj());
                setAttribute("COLUMN_5_ID", (int) ObjPO.getAttribute("COLUMN_5_ID").getVal());
                setAttribute("COLUMN_5_FORMULA", (String) ObjPO.getAttribute("COLUMN_5_FORMULA").getObj());
                setAttribute("COLUMN_5_PER", ObjPO.getAttribute("COLUMN_5_PER").getVal());
                setAttribute("COLUMN_5_AMT", ObjPO.getAttribute("COLUMN_5_AMT").getVal());
                setAttribute("COLUMN_5_CAPTION", (String) ObjPO.getAttribute("COLUMN_5_CAPTION").getObj());
                setAttribute("COLUMN_6_ID", (int) ObjPO.getAttribute("COLUMN_6_ID").getVal());
                setAttribute("COLUMN_6_FORMULA", (String) ObjPO.getAttribute("COLUMN_6_FORMULA").getObj());
                setAttribute("COLUMN_6_PER", ObjPO.getAttribute("COLUMN_6_PER").getVal());
                setAttribute("COLUMN_6_AMT", ObjPO.getAttribute("COLUMN_6_AMT").getVal());
                setAttribute("COLUMN_6_CAPTION", (String) ObjPO.getAttribute("COLUMN_6_CAPTION").getObj());
                setAttribute("COLUMN_7_ID", (int) ObjPO.getAttribute("COLUMN_7_ID").getVal());
                setAttribute("COLUMN_7_FORMULA", (String) ObjPO.getAttribute("COLUMN_7_FORMULA").getObj());
                setAttribute("COLUMN_7_PER", ObjPO.getAttribute("COLUMN_7_PER").getVal());
                setAttribute("COLUMN_7_AMT", ObjPO.getAttribute("COLUMN_7_AMT").getVal());
                setAttribute("COLUMN_7_CAPTION", (String) ObjPO.getAttribute("COLUMN_7_CAPTION").getObj());
                setAttribute("COLUMN_8_ID", (int) ObjPO.getAttribute("COLUMN_8_ID").getVal());
                setAttribute("COLUMN_8_FORMULA", (String) ObjPO.getAttribute("COLUMN_8_FORMULA").getObj());
                setAttribute("COLUMN_8_PER", ObjPO.getAttribute("COLUMN_8_PER").getVal());
                setAttribute("COLUMN_8_AMT", ObjPO.getAttribute("COLUMN_8_AMT").getVal());
                setAttribute("COLUMN_8_CAPTION", (String) ObjPO.getAttribute("COLUMN_8_CAPTION").getObj());
                setAttribute("COLUMN_9_ID", (int) ObjPO.getAttribute("COLUMN_9_ID").getVal());
                setAttribute("COLUMN_9_FORMULA", (String) ObjPO.getAttribute("COLUMN_9_FORMULA").getObj());
                setAttribute("COLUMN_9_PER", ObjPO.getAttribute("COLUMN_9_PER").getVal());
                setAttribute("COLUMN_9_AMT", ObjPO.getAttribute("COLUMN_9_AMT").getVal());
                setAttribute("COLUMN_9_CAPTION", (String) ObjPO.getAttribute("COLUMN_9_CAPTION").getObj());
                setAttribute("COLUMN_10_ID", (int) ObjPO.getAttribute("COLUMN_10_ID").getVal());
                setAttribute("COLUMN_10_FORMULA", (String) ObjPO.getAttribute("COLUMN_10_FORMULA").getObj());
                setAttribute("COLUMN_10_PER", ObjPO.getAttribute("COLUMN_10_PER").getVal());
                setAttribute("COLUMN_10_AMT", ObjPO.getAttribute("COLUMN_10_AMT").getVal());
                setAttribute("COLUMN_10_CAPTION", (String) ObjPO.getAttribute("COLUMN_10_CAPTION").getObj());

                setAttribute("COLUMN_11_ID", (int) ObjPO.getAttribute("COLUMN_11_ID").getVal());
                setAttribute("COLUMN_11_FORMULA", (String) ObjPO.getAttribute("COLUMN_11_FORMULA").getObj());
                setAttribute("COLUMN_11_PER", ObjPO.getAttribute("COLUMN_11_PER").getVal());
                setAttribute("COLUMN_11_AMT", ObjPO.getAttribute("COLUMN_11_AMT").getVal());
                setAttribute("COLUMN_11_CAPTION", (String) ObjPO.getAttribute("COLUMN_11_CAPTION").getObj());
                setAttribute("COLUMN_12_ID", (int) ObjPO.getAttribute("COLUMN_12_ID").getVal());
                setAttribute("COLUMN_12_FORMULA", (String) ObjPO.getAttribute("COLUMN_12_FORMULA").getObj());
                setAttribute("COLUMN_12_PER", ObjPO.getAttribute("COLUMN_12_PER").getVal());
                setAttribute("COLUMN_12_AMT", ObjPO.getAttribute("COLUMN_12_AMT").getVal());
                setAttribute("COLUMN_12_CAPTION", (String) ObjPO.getAttribute("COLUMN_12_CAPTION").getObj());
                setAttribute("COLUMN_13_ID", (int) ObjPO.getAttribute("COLUMN_13_ID").getVal());
                setAttribute("COLUMN_13_FORMULA", (String) ObjPO.getAttribute("COLUMN_13_FORMULA").getObj());
                setAttribute("COLUMN_13_PER", ObjPO.getAttribute("COLUMN_13_PER").getVal());
                setAttribute("COLUMN_13_AMT", ObjPO.getAttribute("COLUMN_13_AMT").getVal());
                setAttribute("COLUMN_13_CAPTION", (String) ObjPO.getAttribute("COLUMN_13_CAPTION").getObj());
                setAttribute("COLUMN_14_ID", (int) ObjPO.getAttribute("COLUMN_14_ID").getVal());
                setAttribute("COLUMN_14_FORMULA", (String) ObjPO.getAttribute("COLUMN_14_FORMULA").getObj());
                setAttribute("COLUMN_14_PER", ObjPO.getAttribute("COLUMN_14_PER").getVal());
                setAttribute("COLUMN_14_AMT", ObjPO.getAttribute("COLUMN_14_AMT").getVal());
                setAttribute("COLUMN_14_CAPTION", (String) ObjPO.getAttribute("COLUMN_14_CAPTION").getObj());
                setAttribute("COLUMN_15_ID", (int) ObjPO.getAttribute("COLUMN_15_ID").getVal());
                setAttribute("COLUMN_15_FORMULA", (String) ObjPO.getAttribute("COLUMN_15_FORMULA").getObj());
                setAttribute("COLUMN_15_PER", ObjPO.getAttribute("COLUMN_15_PER").getVal());
                setAttribute("COLUMN_15_AMT", ObjPO.getAttribute("COLUMN_15_AMT").getVal());
                setAttribute("COLUMN_15_CAPTION", (String) ObjPO.getAttribute("COLUMN_15_CAPTION").getObj());
                
                setAttribute("COLUMN_16_ID", (int) ObjPO.getAttribute("COLUMN_16_ID").getVal());
                setAttribute("COLUMN_16_FORMULA", (String) ObjPO.getAttribute("COLUMN_16_FORMULA").getObj());
                setAttribute("COLUMN_16_PER", ObjPO.getAttribute("COLUMN_16_PER").getVal());
                setAttribute("COLUMN_16_AMT", ObjPO.getAttribute("COLUMN_16_AMT").getVal());
                setAttribute("COLUMN_16_CAPTION", (String) ObjPO.getAttribute("COLUMN_16_CAPTION").getObj());
                setAttribute("COLUMN_17_ID", (int) ObjPO.getAttribute("COLUMN_17_ID").getVal());
                setAttribute("COLUMN_17_FORMULA", (String) ObjPO.getAttribute("COLUMN_17_FORMULA").getObj());
                setAttribute("COLUMN_17_PER", ObjPO.getAttribute("COLUMN_17_PER").getVal());
                setAttribute("COLUMN_17_AMT", ObjPO.getAttribute("COLUMN_17_AMT").getVal());
                setAttribute("COLUMN_17_CAPTION", (String) ObjPO.getAttribute("COLUMN_17_CAPTION").getObj());
                setAttribute("COLUMN_18_ID", (int) ObjPO.getAttribute("COLUMN_18_ID").getVal());
                setAttribute("COLUMN_18_FORMULA", (String) ObjPO.getAttribute("COLUMN_18_FORMULA").getObj());
                setAttribute("COLUMN_18_PER", ObjPO.getAttribute("COLUMN_18_PER").getVal());
                setAttribute("COLUMN_18_AMT", ObjPO.getAttribute("COLUMN_18_AMT").getVal());
                setAttribute("COLUMN_18_CAPTION", (String) ObjPO.getAttribute("COLUMN_18_CAPTION").getObj());
                setAttribute("COLUMN_19_ID", (int) ObjPO.getAttribute("COLUMN_19_ID").getVal());
                setAttribute("COLUMN_19_FORMULA", (String) ObjPO.getAttribute("COLUMN_19_FORMULA").getObj());
                setAttribute("COLUMN_19_PER", ObjPO.getAttribute("COLUMN_19_PER").getVal());
                setAttribute("COLUMN_19_AMT", ObjPO.getAttribute("COLUMN_19_AMT").getVal());
                setAttribute("COLUMN_19_CAPTION", (String) ObjPO.getAttribute("COLUMN_19_CAPTION").getObj());
                setAttribute("COLUMN_20_ID", (int) ObjPO.getAttribute("COLUMN_20_ID").getVal());
                setAttribute("COLUMN_20_FORMULA", (String) ObjPO.getAttribute("COLUMN_20_FORMULA").getObj());
                setAttribute("COLUMN_20_PER", ObjPO.getAttribute("COLUMN_20_PER").getVal());
                setAttribute("COLUMN_20_AMT", ObjPO.getAttribute("COLUMN_20_AMT").getVal());
                setAttribute("COLUMN_20_CAPTION", (String) ObjPO.getAttribute("COLUMN_20_CAPTION").getObj());

                setAttribute("COLUMN_21_ID", (int) ObjPO.getAttribute("COLUMN_21_ID").getVal());
                setAttribute("COLUMN_21_FORMULA", (String) ObjPO.getAttribute("COLUMN_21_FORMULA").getObj());
                setAttribute("COLUMN_21_PER", ObjPO.getAttribute("COLUMN_21_PER").getVal());
                setAttribute("COLUMN_21_AMT", ObjPO.getAttribute("COLUMN_21_AMT").getVal());
                setAttribute("COLUMN_21_CAPTION", (String) ObjPO.getAttribute("COLUMN_21_CAPTION").getObj());
                

                setAttribute("IMPORT_CONCESS", ObjPO.getAttribute("IMPORT_CONCESS").getBool());
                setAttribute("PRINT_LINE_1", ObjPO.getAttribute("PRINT_LINE_1").getObj());
                setAttribute("PRINT_LINE_2", ObjPO.getAttribute("PRINT_LINE_2").getObj());
                setAttribute("IMPORT_LICENSE", ObjPO.getAttribute("IMPORT_LICENSE").getObj());
                setAttribute("PAYMENT_TERM", ObjPO.getAttribute("PAYMENT_TERM").getObj());

                setAttribute("PAYMENT_CODE", ObjPO.getAttribute("PAYMENT_CODE").getInt());
                setAttribute("CR_DAYS", ObjPO.getAttribute("CR_DAYS").getInt());
                setAttribute("DEPT_ID", ObjPO.getAttribute("DEPT_ID").getInt());

                setAttribute("PRICE_BASIS_TERM", ObjPO.getAttribute("PRICE_BASIS_TERM").getObj());
                setAttribute("DISCOUNT_TERM", ObjPO.getAttribute("DISCOUNT_TERM").getObj());
                setAttribute("EXCISE_TERM", ObjPO.getAttribute("EXCISE_TERM").getObj());
                setAttribute("ST_TERM", ObjPO.getAttribute("ST_TERM").getObj());
                setAttribute("PF_TERM", ObjPO.getAttribute("PF_TERM").getObj());
                setAttribute("FREIGHT_TERM", ObjPO.getAttribute("FREIGHT_TERM").getObj());
                setAttribute("OCTROI_TERM", ObjPO.getAttribute("OCTROI_TERM").getObj());
                setAttribute("FOB_TERM", ObjPO.getAttribute("FOB_TERM").getObj());
                setAttribute("CIE_TERM", ObjPO.getAttribute("CIE_TERM").getObj());
                setAttribute("INSURANCE_TERM", ObjPO.getAttribute("INSURANCE_TERM").getObj());
                setAttribute("TCC_TERM", ObjPO.getAttribute("TCC_TERM").getObj());
                setAttribute("CENVAT_TERM", ObjPO.getAttribute("CENVAT_TERM").getObj());
                setAttribute("DESPATCH_TERM", ObjPO.getAttribute("DESPATCH_TERM").getObj());
                setAttribute("SERVICE_TAX_TERM", ObjPO.getAttribute("SERVICE_TAX_TERM").getObj());
                setAttribute("OTHERS_TERM", ObjPO.getAttribute("OTHERS_TERM").getObj());
                
                setAttribute("CGST_TERM", ObjPO.getAttribute("CGST_TERM").getObj());
                setAttribute("SGST_TERM", ObjPO.getAttribute("SGST_TERM").getObj());
                setAttribute("IGST_TERM", ObjPO.getAttribute("IGST_TERM").getObj());
                setAttribute("COMPOSITION_TERM", ObjPO.getAttribute("COMPOSITION_TERM").getObj());
                setAttribute("RCM_TERM", ObjPO.getAttribute("RCM_TERM").getObj());
                setAttribute("GST_COMPENSATION_CESS_TERM", ObjPO.getAttribute("GST_COMPENSATION_CESS_TERM").getObj());
                
                setAttribute("COVERING_TEXT", ObjPO.getAttribute("COVERING_TEXT").getObj());
                setAttribute("PREMISES_TYPE", ObjPO.getAttribute("PREMISES_TYPE").getObj());
                setAttribute("SCOPE", ObjPO.getAttribute("SCOPE").getObj());
                setAttribute("SERVICE_PERIOD", ObjPO.getAttribute("SERVICE_PERIOD").getObj());
                setAttribute("SERVICE_FREQUENCY", ObjPO.getAttribute("SERVICE_FREQUENCY").getObj());
                setAttribute("CONTRACT_DETAILS", ObjPO.getAttribute("CONTRACT_DETAILS").getObj());
                setAttribute("SERVICE_REPORT", ObjPO.getAttribute("SERVICE_REPORT").getObj());
                setAttribute("ESI_TERMS", ObjPO.getAttribute("ESI_TERMS").getObj());
                setAttribute("TERMINATION_TERMS", ObjPO.getAttribute("TERMINATION_TERMS").getObj());
                setAttribute("FILE_TEXT", ObjPO.getAttribute("FILE_TEXT").getObj());
                setAttribute("AMOUNT_IN_WORDS", ObjPO.getAttribute("AMOUNT_IN_WORDS").getObj());
                setAttribute("DIRECTOR_APPROVAL", ObjPO.getAttribute("DIRECTOR_APPROVAL").getBool());
                setAttribute("IMPORTED", ObjPO.getAttribute("IMPORTED").getBool());

                //Clear the collection of this class
                colPOItems.clear();

                for (int i = 1; i <= ObjPO.colPOItems.size(); i++) {
                    clsPOItem ObjItem = (clsPOItem) ObjPO.colPOItems.get(Integer.toString(i));

                    clsPOAmendItem NewItem = new clsPOAmendItem();

                    NewItem.setAttribute("COMPANY_ID", (int) ObjPO.getAttribute("COMPANY_ID").getVal());
                    NewItem.setAttribute("PO_NO", (String) ObjPO.getAttribute("PO_NO").getObj());
                    NewItem.setAttribute("SR_NO", i);
                    NewItem.setAttribute("PO_TYPE", POType);
                    NewItem.setAttribute("ITEM_ID", (String) ObjItem.getAttribute("ITEM_ID").getObj());
                    NewItem.setAttribute("VENDOR_SHADE", (String) ObjItem.getAttribute("VENDOR_SHADE").getObj());
                    NewItem.setAttribute("SDML_SHADE", (String) ObjItem.getAttribute("SDML_SHADE").getObj());
                    NewItem.setAttribute("ITEM_DESC", (String) ObjItem.getAttribute("ITEM_DESC").getObj());
                    NewItem.setAttribute("HSN_SAC_CODE", (String) ObjItem.getAttribute("HSN_SAC_CODE").getObj());
                    NewItem.setAttribute("MAKE", (String) ObjItem.getAttribute("MAKE").getObj());
                    NewItem.setAttribute("PRICE_LIST_NO", (String) ObjItem.getAttribute("PRICE_LIST_NO").getObj());
                    NewItem.setAttribute("PART_NO", (String) ObjItem.getAttribute("PART_NO").getObj());
                    NewItem.setAttribute("EXCISE_TARRIF_NO", (String) ObjItem.getAttribute("EXCISE_TARRIF_NO").getObj());
                    NewItem.setAttribute("QTY", ObjItem.getAttribute("QTY").getVal());
                    NewItem.setAttribute("TOLERANCE_LIMIT", ObjItem.getAttribute("TOLERANCE_LIMIT").getVal());
                    NewItem.setAttribute("PENDING_QTY", ObjItem.getAttribute("PENDING_QTY").getVal());
                    NewItem.setAttribute("RECD_QTY", ObjItem.getAttribute("RECD_QTY").getVal());
                    NewItem.setAttribute("UNIT", (int) ObjItem.getAttribute("UNIT").getVal());

                    NewItem.setAttribute("DEPT_ID", (int) ObjItem.getAttribute("DEPT_ID").getVal());
                    NewItem.setAttribute("RATE", ObjItem.getAttribute("RATE").getVal());
                    NewItem.setAttribute("LANDED_RATE", ObjItem.getAttribute("LANDED_RATE").getVal());
                    NewItem.setAttribute("DISC_PER", ObjItem.getAttribute("DISC_PER").getVal());
                    NewItem.setAttribute("DISC_AMT", ObjItem.getAttribute("DISC_AMT").getVal());
                    NewItem.setAttribute("TOTAL_AMOUNT", ObjItem.getAttribute("TOTAL_AMOUNT").getVal());
                    NewItem.setAttribute("NET_AMOUNT", ObjItem.getAttribute("NET_AMOUNT").getVal());
                    NewItem.setAttribute("INDENT_NO", (String) ObjItem.getAttribute("INDENT_NO").getObj());
                    NewItem.setAttribute("INDENT_SR_NO", (int) ObjItem.getAttribute("INDENT_SR_NO").getVal());
                    NewItem.setAttribute("REFERENCE", (String) ObjItem.getAttribute("REFERENCE").getObj());
                    NewItem.setAttribute("DELIVERY_DATE", (String) ObjItem.getAttribute("DELIVERY_DATE").getObj());
                    NewItem.setAttribute("REMARKS", (String) ObjItem.getAttribute("REMARKS").getObj());
                    NewItem.setAttribute("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                    NewItem.setAttribute("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    //NewItem.setAttribute("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
                    //NewItem.setAttribute("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                    NewItem.setAttribute("COLUMN_1_ID", (int) ObjItem.getAttribute("COLUMN_1_ID").getVal());
                    NewItem.setAttribute("COLUMN_1_FORMULA", (String) ObjItem.getAttribute("COLUMN_1_FORMULA").getObj());
                    NewItem.setAttribute("COLUMN_1_PER", ObjItem.getAttribute("COLUMN_1_PER").getVal());
                    NewItem.setAttribute("COLUMN_1_AMT", ObjItem.getAttribute("COLUMN_1_AMT").getVal());
                    NewItem.setAttribute("COLUMN_1_CAPTION", (String) ObjItem.getAttribute("COLUMN_1_CAPTION").getObj());
                    NewItem.setAttribute("COLUMN_2_ID", (int) ObjItem.getAttribute("COLUMN_2_ID").getVal());
                    NewItem.setAttribute("COLUMN_2_FORMULA", (String) ObjItem.getAttribute("COLUMN_2_FORMULA").getObj());
                    NewItem.setAttribute("COLUMN_2_PER", ObjItem.getAttribute("COLUMN_2_PER").getVal());
                    NewItem.setAttribute("COLUMN_2_AMT", ObjItem.getAttribute("COLUMN_2_AMT").getVal());
                    NewItem.setAttribute("COLUMN_2_CAPTION", (String) ObjItem.getAttribute("COLUMN_2_CAPTION").getObj());
                    NewItem.setAttribute("COLUMN_3_ID", (int) ObjItem.getAttribute("COLUMN_3_ID").getVal());
                    NewItem.setAttribute("COLUMN_3_FORMULA", (String) ObjItem.getAttribute("COLUMN_3_FORMULA").getObj());
                    NewItem.setAttribute("COLUMN_3_PER", ObjItem.getAttribute("COLUMN_3_PER").getVal());
                    NewItem.setAttribute("COLUMN_3_AMT", ObjItem.getAttribute("COLUMN_3_AMT").getVal());
                    NewItem.setAttribute("COLUMN_3_CAPTION", (String) ObjItem.getAttribute("COLUMN_3_CAPTION").getObj());
                    NewItem.setAttribute("COLUMN_4_ID", (int) ObjItem.getAttribute("COLUMN_4_ID").getVal());
                    NewItem.setAttribute("COLUMN_4_FORMULA", (String) ObjItem.getAttribute("COLUMN_4_FORMULA").getObj());
                    NewItem.setAttribute("COLUMN_4_PER", ObjItem.getAttribute("COLUMN_4_PER").getVal());
                    NewItem.setAttribute("COLUMN_4_AMT", ObjItem.getAttribute("COLUMN_4_AMT").getVal());
                    NewItem.setAttribute("COLUMN_4_CAPTION", (String) ObjItem.getAttribute("COLUMN_4_CAPTION").getObj());
                    NewItem.setAttribute("COLUMN_5_ID", (int) ObjItem.getAttribute("COLUMN_5_ID").getVal());
                    NewItem.setAttribute("COLUMN_5_FORMULA", (String) ObjItem.getAttribute("COLUMN_5_FORMULA").getObj());
                    NewItem.setAttribute("COLUMN_5_PER", ObjItem.getAttribute("COLUMN_5_PER").getVal());
                    NewItem.setAttribute("COLUMN_5_AMT", ObjItem.getAttribute("COLUMN_5_AMT").getVal());
                    NewItem.setAttribute("COLUMN_5_CAPTION", (String) ObjItem.getAttribute("COLUMN_5_CAPTION").getObj());
                    NewItem.setAttribute("COLUMN_6_ID", (int) ObjItem.getAttribute("COLUMN_6_ID").getVal());
                    NewItem.setAttribute("COLUMN_6_FORMULA", (String) ObjItem.getAttribute("COLUMN_6_FORMULA").getObj());
                    NewItem.setAttribute("COLUMN_6_PER", ObjItem.getAttribute("COLUMN_6_PER").getVal());
                    NewItem.setAttribute("COLUMN_6_AMT", ObjItem.getAttribute("COLUMN_6_AMT").getVal());
                    NewItem.setAttribute("COLUMN_6_CAPTION", (String) ObjItem.getAttribute("COLUMN_6_CAPTION").getObj());
                    NewItem.setAttribute("COLUMN_7_ID", (int) ObjItem.getAttribute("COLUMN_7_ID").getVal());
                    NewItem.setAttribute("COLUMN_7_FORMULA", (String) ObjItem.getAttribute("COLUMN_7_FORMULA").getObj());
                    NewItem.setAttribute("COLUMN_7_PER", ObjItem.getAttribute("COLUMN_7_PER").getVal());
                    NewItem.setAttribute("COLUMN_7_AMT", ObjItem.getAttribute("COLUMN_7_AMT").getVal());
                    NewItem.setAttribute("COLUMN_7_CAPTION", (String) ObjItem.getAttribute("COLUMN_7_CAPTION").getObj());
                    NewItem.setAttribute("COLUMN_8_ID", (int) ObjItem.getAttribute("COLUMN_8_ID").getVal());
                    NewItem.setAttribute("COLUMN_8_FORMULA", (String) ObjItem.getAttribute("COLUMN_8_FORMULA").getObj());
                    NewItem.setAttribute("COLUMN_8_PER", ObjItem.getAttribute("COLUMN_8_PER").getVal());
                    NewItem.setAttribute("COLUMN_8_AMT", ObjItem.getAttribute("COLUMN_8_AMT").getVal());
                    NewItem.setAttribute("COLUMN_8_CAPTION", (String) ObjItem.getAttribute("COLUMN_8_CAPTION").getObj());
                    NewItem.setAttribute("COLUMN_9_ID", (int) ObjItem.getAttribute("COLUMN_9_ID").getVal());
                    NewItem.setAttribute("COLUMN_9_FORMULA", (String) ObjItem.getAttribute("COLUMN_9_FORMULA").getObj());
                    NewItem.setAttribute("COLUMN_9_PER", ObjItem.getAttribute("COLUMN_9_PER").getVal());
                    NewItem.setAttribute("COLUMN_9_AMT", ObjItem.getAttribute("COLUMN_9_AMT").getVal());
                    NewItem.setAttribute("COLUMN_9_CAPTION", (String) ObjItem.getAttribute("COLUMN_9_CAPTION").getObj());
                    NewItem.setAttribute("COLUMN_10_ID", (int) ObjItem.getAttribute("COLUMN_10_ID").getVal());
                    NewItem.setAttribute("COLUMN_10_FORMULA", (String) ObjItem.getAttribute("COLUMN_10_FORMULA").getObj());
                    NewItem.setAttribute("COLUMN_10_PER", ObjItem.getAttribute("COLUMN_10_PER").getVal());
                    NewItem.setAttribute("COLUMN_10_AMT", ObjItem.getAttribute("COLUMN_10_AMT").getVal());
                    NewItem.setAttribute("COLUMN_10_CAPTION", (String) ObjItem.getAttribute("COLUMN_10_CAPTION").getObj());

                    NewItem.setAttribute("COLUMN_11_ID", (int) ObjItem.getAttribute("COLUMN_11_ID").getVal());
                    NewItem.setAttribute("COLUMN_11_FORMULA", (String) ObjItem.getAttribute("COLUMN_11_FORMULA").getObj());
                    NewItem.setAttribute("COLUMN_11_PER", ObjItem.getAttribute("COLUMN_11_PER").getVal());
                    NewItem.setAttribute("COLUMN_11_AMT", ObjItem.getAttribute("COLUMN_11_AMT").getVal());
                    NewItem.setAttribute("COLUMN_11_CAPTION", (String) ObjItem.getAttribute("COLUMN_11_CAPTION").getObj());
                    NewItem.setAttribute("COLUMN_12_ID", (int) ObjItem.getAttribute("COLUMN_12_ID").getVal());
                    NewItem.setAttribute("COLUMN_12_FORMULA", (String) ObjItem.getAttribute("COLUMN_12_FORMULA").getObj());
                    NewItem.setAttribute("COLUMN_12_PER", ObjItem.getAttribute("COLUMN_12_PER").getVal());
                    NewItem.setAttribute("COLUMN_12_AMT", ObjItem.getAttribute("COLUMN_12_AMT").getVal());
                    NewItem.setAttribute("COLUMN_12_CAPTION", (String) ObjItem.getAttribute("COLUMN_12_CAPTION").getObj());
                    NewItem.setAttribute("COLUMN_13_ID", (int) ObjItem.getAttribute("COLUMN_13_ID").getVal());
                    NewItem.setAttribute("COLUMN_13_FORMULA", (String) ObjItem.getAttribute("COLUMN_13_FORMULA").getObj());
                    NewItem.setAttribute("COLUMN_13_PER", ObjItem.getAttribute("COLUMN_13_PER").getVal());
                    NewItem.setAttribute("COLUMN_13_AMT", ObjItem.getAttribute("COLUMN_13_AMT").getVal());
                    NewItem.setAttribute("COLUMN_13_CAPTION", (String) ObjItem.getAttribute("COLUMN_13_CAPTION").getObj());
                    NewItem.setAttribute("COLUMN_14_ID", (int) ObjItem.getAttribute("COLUMN_14_ID").getVal());
                    NewItem.setAttribute("COLUMN_14_FORMULA", (String) ObjItem.getAttribute("COLUMN_14_FORMULA").getObj());
                    NewItem.setAttribute("COLUMN_14_PER", ObjItem.getAttribute("COLUMN_14_PER").getVal());
                    NewItem.setAttribute("COLUMN_14_AMT", ObjItem.getAttribute("COLUMN_14_AMT").getVal());
                    NewItem.setAttribute("COLUMN_14_CAPTION", (String) ObjItem.getAttribute("COLUMN_14_CAPTION").getObj());
                    NewItem.setAttribute("COLUMN_15_ID", (int) ObjItem.getAttribute("COLUMN_15_ID").getVal());
                    NewItem.setAttribute("COLUMN_15_FORMULA", (String) ObjItem.getAttribute("COLUMN_15_FORMULA").getObj());
                    NewItem.setAttribute("COLUMN_15_PER", ObjItem.getAttribute("COLUMN_15_PER").getVal());
                    NewItem.setAttribute("COLUMN_15_AMT", ObjItem.getAttribute("COLUMN_15_AMT").getVal());
                    NewItem.setAttribute("COLUMN_15_CAPTION", (String) ObjItem.getAttribute("COLUMN_15_CAPTION").getObj());
                    
                    NewItem.setAttribute("COLUMN_16_ID", (int) ObjItem.getAttribute("COLUMN_16_ID").getVal());
                    NewItem.setAttribute("COLUMN_16_FORMULA", (String) ObjItem.getAttribute("COLUMN_16_FORMULA").getObj());
                    NewItem.setAttribute("COLUMN_16_PER", ObjItem.getAttribute("COLUMN_16_PER").getVal());
                    NewItem.setAttribute("COLUMN_16_AMT", ObjItem.getAttribute("COLUMN_16_AMT").getVal());
                    NewItem.setAttribute("COLUMN_16_CAPTION", (String) ObjItem.getAttribute("COLUMN_16_CAPTION").getObj());
                    NewItem.setAttribute("COLUMN_17_ID", (int) ObjItem.getAttribute("COLUMN_17_ID").getVal());
                    NewItem.setAttribute("COLUMN_17_FORMULA", (String) ObjItem.getAttribute("COLUMN_17_FORMULA").getObj());
                    NewItem.setAttribute("COLUMN_17_PER", ObjItem.getAttribute("COLUMN_17_PER").getVal());
                    NewItem.setAttribute("COLUMN_17_AMT", ObjItem.getAttribute("COLUMN_17_AMT").getVal());
                    NewItem.setAttribute("COLUMN_17_CAPTION", (String) ObjItem.getAttribute("COLUMN_17_CAPTION").getObj());
                    NewItem.setAttribute("COLUMN_18_ID", (int) ObjItem.getAttribute("COLUMN_18_ID").getVal());
                    NewItem.setAttribute("COLUMN_18_FORMULA", (String) ObjItem.getAttribute("COLUMN_18_FORMULA").getObj());
                    NewItem.setAttribute("COLUMN_18_PER", ObjItem.getAttribute("COLUMN_18_PER").getVal());
                    NewItem.setAttribute("COLUMN_18_AMT", ObjItem.getAttribute("COLUMN_18_AMT").getVal());
                    NewItem.setAttribute("COLUMN_18_CAPTION", (String) ObjItem.getAttribute("COLUMN_18_CAPTION").getObj());
                    NewItem.setAttribute("COLUMN_19_ID", (int) ObjItem.getAttribute("COLUMN_19_ID").getVal());
                    NewItem.setAttribute("COLUMN_19_FORMULA", (String) ObjItem.getAttribute("COLUMN_19_FORMULA").getObj());
                    NewItem.setAttribute("COLUMN_19_PER", ObjItem.getAttribute("COLUMN_19_PER").getVal());
                    NewItem.setAttribute("COLUMN_19_AMT", ObjItem.getAttribute("COLUMN_19_AMT").getVal());
                    NewItem.setAttribute("COLUMN_19_CAPTION", (String) ObjItem.getAttribute("COLUMN_19_CAPTION").getObj());
                    NewItem.setAttribute("COLUMN_20_ID", (int) ObjItem.getAttribute("COLUMN_20_ID").getVal());
                    NewItem.setAttribute("COLUMN_20_FORMULA", (String) ObjItem.getAttribute("COLUMN_20_FORMULA").getObj());
                    NewItem.setAttribute("COLUMN_20_PER", ObjItem.getAttribute("COLUMN_20_PER").getVal());
                    NewItem.setAttribute("COLUMN_20_AMT", ObjItem.getAttribute("COLUMN_20_AMT").getVal());
                    NewItem.setAttribute("COLUMN_20_CAPTION", (String) ObjItem.getAttribute("COLUMN_20_CAPTION").getObj());

                    NewItem.setAttribute("COLUMN_21_ID", (int) ObjItem.getAttribute("COLUMN_21_ID").getVal());
                    NewItem.setAttribute("COLUMN_21_FORMULA", (String) ObjItem.getAttribute("COLUMN_21_FORMULA").getObj());
                    NewItem.setAttribute("COLUMN_21_PER", ObjItem.getAttribute("COLUMN_21_PER").getVal());
                    NewItem.setAttribute("COLUMN_21_AMT", ObjItem.getAttribute("COLUMN_21_AMT").getVal());
                    NewItem.setAttribute("COLUMN_21_CAPTION", (String) ObjItem.getAttribute("COLUMN_21_CAPTION").getObj());
                    

                    NewItem.setAttribute("EXCISE_GATEPASS_GIVEN", ObjItem.getAttribute("EXCISE_GATEPASS_GIVEN").getBool());
                    NewItem.setAttribute("IMPORT_CONCESS", ObjItem.getAttribute("IMPORT_CONCESS").getBool());
                    NewItem.setAttribute("QUOT_ID", (String) ObjItem.getAttribute("QUOT_ID").getObj());
                    NewItem.setAttribute("QUOT_SR_NO", (int) ObjItem.getAttribute("QUOT_SR_NO").getVal());

                    colPOItems.put(Integer.toString(colPOItems.size() + 1), NewItem);
                }

                //Clear the collection of this class
                colPOTerms.clear();

                for (int i = 1; i <= ObjPO.colPOTerms.size(); i++) {
                    clsPOTerms ObjItem = (clsPOTerms) ObjPO.colPOTerms.get(Integer.toString(i));

                    clsPOTerms NewItem = new clsPOTerms();

                    NewItem.setAttribute("COMPANY_ID", (int) ObjPO.getAttribute("COMPANY_ID").getVal());
                    NewItem.setAttribute("PO_NO", (String) ObjPO.getAttribute("PO_NO").getObj());
                    NewItem.setAttribute("SR_NO", i);
                    NewItem.setAttribute("PO_TYPE", POType);
                    NewItem.setAttribute("TERM_TYPE", (String) ObjItem.getAttribute("TERM_TYPE").getObj());
                    NewItem.setAttribute("TERM_CODE", (int) ObjItem.getAttribute("TERM_CODE").getVal());
                    NewItem.setAttribute("TERM_DESC", (String) ObjItem.getAttribute("TERM_DESC").getObj());

                    colPOTerms.put(Integer.toString(colPOTerms.size() + 1), NewItem);
                }
            }

            //--------- Generate New Amendment No.- PONo./Amendment Sr.no. ------------//
            stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = stTmp.executeQuery("SELECT MAX(AMEND_SR_NO) AS MAXNO FROM D_PUR_AMEND_HEADER WHERE COMPANY_ID=" + CompanyID + " AND PO_NO='" + PONo + "' AND PO_TYPE=" + POType);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                String AmendNo = PONo + "/" + (rsTmp.getInt("MAXNO") + 1);
                int AmendSrNo = rsTmp.getInt("MAXNO") + 1;
                setAttribute("AMEND_NO", AmendNo);
                setAttribute("AMEND_SR_NO", AmendSrNo);
                setAttribute("LAST_AMENDMENT", false);
            }

            if (AStatus.equals("F")) {
                setAttribute("LAST_AMENDMENT", true);
                //-------- First set all Last_Amendment flags to False ------//
                data.Execute("UPDATE D_PUR_AMEND_HEADER SET LAST_AMENDMENT=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + CompanyID + " AND PO_NO='" + PONo + "' AND PO_TYPE=" + POType);
                data.Execute("UPDATE D_PUR_AMEND_DETAIL SET LAST_AMENDMENT=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + CompanyID + " AND PO_NO='" + PONo + "' AND PO_TYPE=" + POType);
                //------------------------------------------------------------------------//
            }
            //------------------------------------------------------------------------//

            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateString("AMEND_NO", (String) getAttribute("AMEND_NO").getObj());
            rsResultSet.updateLong("DOC_ID", (long) getAttribute("DOC_ID").getVal());
            rsResultSet.updateString("AMEND_DATE", (String) getAttribute("AMEND_DATE").getObj());
            rsResultSet.updateInt("AMEND_SR_NO", (int) getAttribute("AMEND_SR_NO").getVal());
            rsResultSet.updateBoolean("LAST_AMENDMENT", getAttribute("LAST_AMENDMENT").getBool());
            rsResultSet.updateString("PO_NO", (String) getAttribute("PO_NO").getObj());
            rsResultSet.updateString("PO_DATE", (String) getAttribute("PO_DATE").getObj());
            rsResultSet.updateInt("PO_TYPE", POType);
            rsResultSet.updateString("PO_REF", (String) getAttribute("PO_REF").getObj());
            rsResultSet.updateString("SUPP_ID", (String) getAttribute("SUPP_ID").getObj());
            rsResultSet.updateString("SUPP_NAME", (String) getAttribute("SUPP_NAME").getObj());
            rsResultSet.updateString("REF_A", (String) getAttribute("REF_A").getObj());
            rsResultSet.updateString("REF_B", (String) getAttribute("REF_B").getObj());
            rsResultSet.updateString("QUOTATION_NO", (String) getAttribute("QUOTATION_NO").getObj());
            rsResultSet.updateString("QUOTATION_DATE", (String) getAttribute("QUOTATION_DATE").getObj());
            rsResultSet.updateString("INQUIRY_NO", (String) getAttribute("INQUIRY_NO").getObj());
            rsResultSet.updateString("INQUIRY_DATE", (String) getAttribute("INQUIRY_DATE").getObj());
            rsResultSet.updateInt("BUYER", (int) getAttribute("BUYER").getVal());
            rsResultSet.updateInt("TRANSPORT_MODE", (int) getAttribute("TRANSPORT_MODE").getVal());
            rsResultSet.updateString("PURPOSE", (String) getAttribute("PURPOSE").getObj());
            rsResultSet.updateString("SUBJECT", (String) getAttribute("SUBJECT").getObj());
            rsResultSet.updateInt("CURRENCY_ID", (int) getAttribute("CURRENCY_ID").getVal());
            rsResultSet.updateDouble("CURRENCY_RATE", getAttribute("CURRENCY_RATE").getVal());
            rsResultSet.updateDouble("TOTAL_AMOUNT", getAttribute("TOTAL_AMOUNT").getVal());
            rsResultSet.updateDouble("NET_AMOUNT", getAttribute("NET_AMOUNT").getVal());
            rsResultSet.updateString("STATUS", (String) getAttribute("STATUS").getObj());
            rsResultSet.updateBoolean("ATTACHEMENT", getAttribute("ATTACHEMENT").getBool());
            rsResultSet.updateString("ATTACHEMENT_PATH", (String) getAttribute("ATTACHEMENT_PATH").getObj());
            rsResultSet.updateString("REMARKS", (String) getAttribute("REMARKS").getObj());
            rsResultSet.updateInt("SHIP_ID", (int) getAttribute("SHIP_ID").getVal());
            rsResultSet.updateBoolean("CANCELLED", getAttribute("CANCELLED").getBool());
            rsResultSet.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateLong("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            //rsResultSet.updateLong("MODIFIED_BY",EITLERPGLOBAL.gNewUserID);
            //rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            //Now Custom Columns
            rsResultSet.updateInt("COLUMN_1_ID", (int) getAttribute("COLUMN_1_ID").getVal());
            rsResultSet.updateString("COLUMN_1_FORMULA", (String) getAttribute("COLUMN_1_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_1_PER", getAttribute("COLUMN_1_PER").getVal());
            rsResultSet.updateDouble("COLUMN_1_AMT", getAttribute("COLUMN_1_AMT").getVal());
            rsResultSet.updateString("COLUMN_1_CAPTION", (String) getAttribute("COLUMN_1_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_2_ID", (int) getAttribute("COLUMN_2_ID").getVal());
            rsResultSet.updateString("COLUMN_2_FORMULA", (String) getAttribute("COLUMN_2_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_2_PER", getAttribute("COLUMN_2_PER").getVal());
            rsResultSet.updateDouble("COLUMN_2_AMT", getAttribute("COLUMN_2_AMT").getVal());
            rsResultSet.updateString("COLUMN_2_CAPTION", (String) getAttribute("COLUMN_2_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_3_ID", (int) getAttribute("COLUMN_3_ID").getVal());
            rsResultSet.updateString("COLUMN_3_FORMULA", (String) getAttribute("COLUMN_3_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_3_PER", getAttribute("COLUMN_3_PER").getVal());
            rsResultSet.updateDouble("COLUMN_3_AMT", getAttribute("COLUMN_3_AMT").getVal());
            rsResultSet.updateString("COLUMN_3_CAPTION", (String) getAttribute("COLUMN_3_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_4_ID", (int) getAttribute("COLUMN_4_ID").getVal());
            rsResultSet.updateString("COLUMN_4_FORMULA", (String) getAttribute("COLUMN_4_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_4_PER", getAttribute("COLUMN_4_PER").getVal());
            rsResultSet.updateDouble("COLUMN_4_AMT", getAttribute("COLUMN_4_AMT").getVal());
            rsResultSet.updateString("COLUMN_4_CAPTION", (String) getAttribute("COLUMN_4_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_5_ID", (int) getAttribute("COLUMN_5_ID").getVal());
            rsResultSet.updateString("COLUMN_5_FORMULA", (String) getAttribute("COLUMN_5_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_5_PER", getAttribute("COLUMN_5_PER").getVal());
            rsResultSet.updateDouble("COLUMN_5_AMT", getAttribute("COLUMN_5_AMT").getVal());
            rsResultSet.updateString("COLUMN_5_CAPTION", (String) getAttribute("COLUMN_5_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_6_ID", (int) getAttribute("COLUMN_6_ID").getVal());
            rsResultSet.updateString("COLUMN_6_FORMULA", (String) getAttribute("COLUMN_6_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_6_PER", getAttribute("COLUMN_6_PER").getVal());
            rsResultSet.updateDouble("COLUMN_6_AMT", getAttribute("COLUMN_6_AMT").getVal());
            rsResultSet.updateString("COLUMN_6_CAPTION", (String) getAttribute("COLUMN_6_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_7_ID", (int) getAttribute("COLUMN_7_ID").getVal());
            rsResultSet.updateString("COLUMN_7_FORMULA", (String) getAttribute("COLUMN_7_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_7_PER", getAttribute("COLUMN_7_PER").getVal());
            rsResultSet.updateDouble("COLUMN_7_AMT", getAttribute("COLUMN_7_AMT").getVal());
            rsResultSet.updateString("COLUMN_7_CAPTION", (String) getAttribute("COLUMN_7_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_8_ID", (int) getAttribute("COLUMN_8_ID").getVal());
            rsResultSet.updateString("COLUMN_8_FORMULA", (String) getAttribute("COLUMN_8_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_8_PER", getAttribute("COLUMN_8_PER").getVal());
            rsResultSet.updateDouble("COLUMN_8_AMT", getAttribute("COLUMN_8_AMT").getVal());
            rsResultSet.updateString("COLUMN_8_CAPTION", (String) getAttribute("COLUMN_8_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_9_ID", (int) getAttribute("COLUMN_9_ID").getVal());
            rsResultSet.updateString("COLUMN_9_FORMULA", (String) getAttribute("COLUMN_9_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_9_PER", getAttribute("COLUMN_9_PER").getVal());
            rsResultSet.updateDouble("COLUMN_9_AMT", getAttribute("COLUMN_9_AMT").getVal());
            rsResultSet.updateString("COLUMN_9_CAPTION", (String) getAttribute("COLUMN_9_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_10_ID", (int) getAttribute("COLUMN_10_ID").getVal());
            rsResultSet.updateString("COLUMN_10_FORMULA", (String) getAttribute("COLUMN_10_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_10_PER", getAttribute("COLUMN_10_PER").getVal());
            rsResultSet.updateDouble("COLUMN_10_AMT", getAttribute("COLUMN_10_AMT").getVal());
            rsResultSet.updateString("COLUMN_10_CAPTION", (String) getAttribute("COLUMN_10_CAPTION").getObj());

            rsResultSet.updateInt("COLUMN_11_ID", (int) getAttribute("COLUMN_11_ID").getVal());
            rsResultSet.updateString("COLUMN_11_FORMULA", (String) getAttribute("COLUMN_11_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_11_PER", getAttribute("COLUMN_11_PER").getVal());
            rsResultSet.updateDouble("COLUMN_11_AMT", getAttribute("COLUMN_11_AMT").getVal());
            rsResultSet.updateString("COLUMN_11_CAPTION", (String) getAttribute("COLUMN_11_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_12_ID", (int) getAttribute("COLUMN_12_ID").getVal());
            rsResultSet.updateString("COLUMN_12_FORMULA", (String) getAttribute("COLUMN_12_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_12_PER", getAttribute("COLUMN_12_PER").getVal());
            rsResultSet.updateDouble("COLUMN_12_AMT", getAttribute("COLUMN_12_AMT").getVal());
            rsResultSet.updateString("COLUMN_12_CAPTION", (String) getAttribute("COLUMN_12_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_13_ID", (int) getAttribute("COLUMN_13_ID").getVal());
            rsResultSet.updateString("COLUMN_13_FORMULA", (String) getAttribute("COLUMN_13_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_13_PER", getAttribute("COLUMN_13_PER").getVal());
            rsResultSet.updateDouble("COLUMN_13_AMT", getAttribute("COLUMN_13_AMT").getVal());
            rsResultSet.updateString("COLUMN_13_CAPTION", (String) getAttribute("COLUMN_13_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_14_ID", (int) getAttribute("COLUMN_14_ID").getVal());
            rsResultSet.updateString("COLUMN_14_FORMULA", (String) getAttribute("COLUMN_14_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_14_PER", getAttribute("COLUMN_14_PER").getVal());
            rsResultSet.updateDouble("COLUMN_14_AMT", getAttribute("COLUMN_14_AMT").getVal());
            rsResultSet.updateString("COLUMN_14_CAPTION", (String) getAttribute("COLUMN_14_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_15_ID", (int) getAttribute("COLUMN_15_ID").getVal());
            rsResultSet.updateString("COLUMN_15_FORMULA", (String) getAttribute("COLUMN_15_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_15_PER", getAttribute("COLUMN_15_PER").getVal());
            rsResultSet.updateDouble("COLUMN_15_AMT", getAttribute("COLUMN_15_AMT").getVal());
            rsResultSet.updateString("COLUMN_15_CAPTION", (String) getAttribute("COLUMN_15_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_16_ID", (int) getAttribute("COLUMN_16_ID").getVal());
            rsResultSet.updateString("COLUMN_16_FORMULA", (String) getAttribute("COLUMN_16_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_16_PER", getAttribute("COLUMN_16_PER").getVal());
            rsResultSet.updateDouble("COLUMN_16_AMT", getAttribute("COLUMN_16_AMT").getVal());
            rsResultSet.updateString("COLUMN_16_CAPTION", (String) getAttribute("COLUMN_16_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_17_ID", (int) getAttribute("COLUMN_17_ID").getVal());
            rsResultSet.updateString("COLUMN_17_FORMULA", (String) getAttribute("COLUMN_17_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_17_PER", getAttribute("COLUMN_17_PER").getVal());
            rsResultSet.updateDouble("COLUMN_17_AMT", getAttribute("COLUMN_17_AMT").getVal());
            rsResultSet.updateString("COLUMN_17_CAPTION", (String) getAttribute("COLUMN_17_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_18_ID", (int) getAttribute("COLUMN_18_ID").getVal());
            rsResultSet.updateString("COLUMN_18_FORMULA", (String) getAttribute("COLUMN_18_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_18_PER", getAttribute("COLUMN_18_PER").getVal());
            rsResultSet.updateDouble("COLUMN_18_AMT", getAttribute("COLUMN_18_AMT").getVal());
            rsResultSet.updateString("COLUMN_18_CAPTION", (String) getAttribute("COLUMN_18_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_19_ID", (int) getAttribute("COLUMN_19_ID").getVal());
            rsResultSet.updateString("COLUMN_19_FORMULA", (String) getAttribute("COLUMN_19_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_19_PER", getAttribute("COLUMN_19_PER").getVal());
            rsResultSet.updateDouble("COLUMN_19_AMT", getAttribute("COLUMN_19_AMT").getVal());
            rsResultSet.updateString("COLUMN_19_CAPTION", (String) getAttribute("COLUMN_19_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_20_ID", (int) getAttribute("COLUMN_20_ID").getVal());
            rsResultSet.updateString("COLUMN_20_FORMULA", (String) getAttribute("COLUMN_20_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_20_PER", getAttribute("COLUMN_20_PER").getVal());
            rsResultSet.updateDouble("COLUMN_20_AMT", getAttribute("COLUMN_20_AMT").getVal());
            rsResultSet.updateString("COLUMN_20_CAPTION", (String) getAttribute("COLUMN_20_CAPTION").getObj());

            rsResultSet.updateInt("COLUMN_21_ID", (int) getAttribute("COLUMN_21_ID").getVal());
            rsResultSet.updateString("COLUMN_21_FORMULA", (String) getAttribute("COLUMN_21_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_21_PER", getAttribute("COLUMN_21_PER").getVal());
            rsResultSet.updateDouble("COLUMN_21_AMT", getAttribute("COLUMN_21_AMT").getVal());
            rsResultSet.updateString("COLUMN_21_CAPTION", (String) getAttribute("COLUMN_21_CAPTION").getObj());
            

            rsResultSet.updateInt("REASON_CODE", (int) getAttribute("REASON_CODE").getVal());
            rsResultSet.updateString("AMEND_REASON", (String) getAttribute("AMEND_REASON").getObj());
            rsResultSet.updateBoolean("IMPORT_CONCESS", getAttribute("IMPORT_CONCESS").getBool());
            rsResultSet.updateString("PRINT_LINE_1", (String) getAttribute("PRINT_LINE_1").getObj());
            rsResultSet.updateString("PRINT_LINE_2", (String) getAttribute("PRINT_LINE_2").getObj());
            rsResultSet.updateString("IMPORT_LICENSE", (String) getAttribute("IMPORT_LICENSE").getObj());
            rsResultSet.updateBoolean("CHANGED", true);
            rsResultSet.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("PAYMENT_TERM", (String) getAttribute("PAYMENT_TERM").getObj());

            rsResultSet.updateInt("PAYMENT_CODE", getAttribute("PAYMENT_CODE").getInt());
            rsResultSet.updateInt("CR_DAYS", getAttribute("CR_DAYS").getInt());
            rsResultSet.updateInt("DEPT_ID", getAttribute("DEPT_ID").getInt());

            rsResultSet.updateString("PRICE_BASIS_TERM", (String) getAttribute("PRICE_BASIS_TERM").getObj());
            rsResultSet.updateString("DISCOUNT_TERM", (String) getAttribute("DISCOUNT_TERM").getObj());
            rsResultSet.updateString("EXCISE_TERM", (String) getAttribute("EXCISE_TERM").getObj());
            rsResultSet.updateString("ST_TERM", (String) getAttribute("ST_TERM").getObj());
            rsResultSet.updateString("PF_TERM", (String) getAttribute("PF_TERM").getObj());
            rsResultSet.updateString("FREIGHT_TERM", (String) getAttribute("FREIGHT_TERM").getObj());
            rsResultSet.updateString("OCTROI_TERM", (String) getAttribute("OCTROI_TERM").getObj());
            rsResultSet.updateString("FOB_TERM", (String) getAttribute("FOB_TERM").getObj());
            rsResultSet.updateString("CIE_TERM", (String) getAttribute("CIE_TERM").getObj());
            rsResultSet.updateString("INSURANCE_TERM", (String) getAttribute("INSURANCE_TERM").getObj());
            rsResultSet.updateString("TCC_TERM", (String) getAttribute("TCC_TERM").getObj());
            rsResultSet.updateString("CENVAT_TERM", (String) getAttribute("CENVAT_TERM").getObj());
            rsResultSet.updateString("DESPATCH_TERM", (String) getAttribute("DESPATCH_TERM").getObj());
            rsResultSet.updateString("SERVICE_TAX_TERM", (String) getAttribute("SERVICE_TAX_TERM").getObj());
            rsResultSet.updateString("OTHERS_TERM", (String) getAttribute("OTHERS_TERM").getObj());
            rsResultSet.updateString("COVERING_TEXT", (String) getAttribute("COVERING_TEXT").getObj());
            
            rsResultSet.updateString("CGST_TERM",(String)getAttribute("CGST_TERM").getObj());
            rsResultSet.updateString("SGST_TERM",(String)getAttribute("SGST_TERM").getObj());
            rsResultSet.updateString("IGST_TERM",(String)getAttribute("IGST_TERM").getObj());
            rsResultSet.updateString("COMPOSITION_TERM",(String)getAttribute("COMPOSITION_TERM").getObj());
            rsResultSet.updateString("RCM_TERM",(String)getAttribute("RCM_TERM").getObj());
            rsResultSet.updateString("GST_COMPENSATION_CESS_TERM",(String)getAttribute("GST_COMPENSATION_CESS_TERM").getObj());

            rsResultSet.updateString("PREMISES_TYPE", (String) getAttribute("PREMISES_TYPE").getObj());
            rsResultSet.updateString("SCOPE", (String) getAttribute("SCOPE").getObj());
            rsResultSet.updateString("SERVICE_PERIOD", (String) getAttribute("SERVICE_PERIOD").getObj());
            rsResultSet.updateString("SERVICE_FREQUENCY", (String) getAttribute("SERVICE_FREQUENCY").getObj());
            rsResultSet.updateString("CONTRACT_DETAILS", (String) getAttribute("CONTRACT_DETAILS").getObj());
            rsResultSet.updateString("SERVICE_REPORT", (String) getAttribute("SERVICE_REPORT").getObj());
            rsResultSet.updateString("ESI_TERMS", (String) getAttribute("ESI_TERMS").getObj());
            rsResultSet.updateString("TERMINATION_TERMS", (String) getAttribute("TERMINATION_TERMS").getObj());
            rsResultSet.updateString("FILE_TEXT", (String) getAttribute("TERMINATION_TERMS").getObj());
            rsResultSet.updateString("AMOUNT_IN_WORDS", (String) getAttribute("AMOUNT_IN_WORDS").getObj());
            rsResultSet.updateBoolean("DIRECTOR_APPROVAL", getAttribute("DIRECTOR_APPROVAL").getBool());
            rsResultSet.updateBoolean("IMPORTED", getAttribute("IMPORTED").getBool());

            rsResultSet.updateBoolean("APPROVED", false);
            rsResultSet.updateString("APPROVED_DATE", "0000-00-00");
            rsResultSet.updateBoolean("REJECTED", false);
            rsResultSet.updateString("REJECTED_DATE", "0000-00-00");
            rsResultSet.updateBoolean("CANCELLED", false);
            rsResultSet.insertRow();

            //History Table
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO", 1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY", (int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS", (String) getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS", (String) getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("AMEND_NO", (String) getAttribute("AMEND_NO").getObj());
            rsHistory.updateLong("DOC_ID", (long) getAttribute("DOC_ID").getVal());
            rsHistory.updateString("AMEND_DATE", (String) getAttribute("AMEND_DATE").getObj());
            rsHistory.updateInt("AMEND_SR_NO", (int) getAttribute("AMEND_SR_NO").getVal());
            rsHistory.updateBoolean("LAST_AMENDMENT", getAttribute("LAST_AMENDMENT").getBool());
            rsHistory.updateString("PO_NO", (String) getAttribute("PO_NO").getObj());
            rsHistory.updateString("PO_DATE", (String) getAttribute("PO_DATE").getObj());
            rsHistory.updateInt("PO_TYPE", POType);
            rsHistory.updateString("PO_REF", (String) getAttribute("PO_REF").getObj());
            rsHistory.updateString("SUPP_ID", (String) getAttribute("SUPP_ID").getObj());
            rsHistory.updateString("SUPP_NAME", (String) getAttribute("SUPP_NAME").getObj());
            rsHistory.updateString("REF_A", (String) getAttribute("REF_A").getObj());
            rsHistory.updateString("REF_B", (String) getAttribute("REF_B").getObj());
            rsHistory.updateString("QUOTATION_NO", (String) getAttribute("QUOTATION_NO").getObj());
            rsHistory.updateString("QUOTATION_DATE", (String) getAttribute("QUOTATION_DATE").getObj());
            rsHistory.updateString("INQUIRY_NO", (String) getAttribute("INQUIRY_NO").getObj());
            rsHistory.updateString("INQUIRY_DATE", (String) getAttribute("INQUIRY_DATE").getObj());
            rsHistory.updateInt("BUYER", (int) getAttribute("BUYER").getVal());
            rsHistory.updateInt("TRANSPORT_MODE", (int) getAttribute("TRANSPORT_MODE").getVal());
            rsHistory.updateString("PURPOSE", (String) getAttribute("PURPOSE").getObj());
            rsHistory.updateString("SUBJECT", (String) getAttribute("SUBJECT").getObj());
            rsHistory.updateInt("CURRENCY_ID", (int) getAttribute("CURRENCY_ID").getVal());
            rsHistory.updateDouble("CURRENCY_RATE", getAttribute("CURRENCY_RATE").getVal());
            rsHistory.updateDouble("TOTAL_AMOUNT", getAttribute("TOTAL_AMOUNT").getVal());
            rsHistory.updateDouble("NET_AMOUNT", getAttribute("NET_AMOUNT").getVal());
            rsHistory.updateString("STATUS", (String) getAttribute("STATUS").getObj());
            rsHistory.updateBoolean("ATTACHEMENT", getAttribute("ATTACHEMENT").getBool());
            rsHistory.updateString("ATTACHEMENT_PATH", (String) getAttribute("ATTACHEMENT_PATH").getObj());
            rsHistory.updateString("REMARKS", (String) getAttribute("REMARKS").getObj());
            rsHistory.updateInt("SHIP_ID", (int) getAttribute("SHIP_ID").getVal());
            rsHistory.updateBoolean("CANCELLED", getAttribute("CANCELLED").getBool());
            rsHistory.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateLong("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            //rsHistory.updateLong("MODIFIED_BY",EITLERPGLOBAL.gNewUserID);
            //rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateInt("COLUMN_1_ID", (int) getAttribute("COLUMN_1_ID").getVal());
            rsHistory.updateString("COLUMN_1_FORMULA", (String) getAttribute("COLUMN_1_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_1_PER", getAttribute("COLUMN_1_PER").getVal());
            rsHistory.updateDouble("COLUMN_1_AMT", getAttribute("COLUMN_1_AMT").getVal());
            rsHistory.updateString("COLUMN_1_CAPTION", (String) getAttribute("COLUMN_1_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_2_ID", (int) getAttribute("COLUMN_2_ID").getVal());
            rsHistory.updateString("COLUMN_2_FORMULA", (String) getAttribute("COLUMN_2_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_2_PER", getAttribute("COLUMN_2_PER").getVal());
            rsHistory.updateDouble("COLUMN_2_AMT", getAttribute("COLUMN_2_AMT").getVal());
            rsHistory.updateString("COLUMN_2_CAPTION", (String) getAttribute("COLUMN_2_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_3_ID", (int) getAttribute("COLUMN_3_ID").getVal());
            rsHistory.updateString("COLUMN_3_FORMULA", (String) getAttribute("COLUMN_3_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_3_PER", getAttribute("COLUMN_3_PER").getVal());
            rsHistory.updateDouble("COLUMN_3_AMT", getAttribute("COLUMN_3_AMT").getVal());
            rsHistory.updateString("COLUMN_3_CAPTION", (String) getAttribute("COLUMN_3_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_4_ID", (int) getAttribute("COLUMN_4_ID").getVal());
            rsHistory.updateString("COLUMN_4_FORMULA", (String) getAttribute("COLUMN_4_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_4_PER", getAttribute("COLUMN_4_PER").getVal());
            rsHistory.updateDouble("COLUMN_4_AMT", getAttribute("COLUMN_4_AMT").getVal());
            rsHistory.updateString("COLUMN_4_CAPTION", (String) getAttribute("COLUMN_4_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_5_ID", (int) getAttribute("COLUMN_5_ID").getVal());
            rsHistory.updateString("COLUMN_5_FORMULA", (String) getAttribute("COLUMN_5_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_5_PER", getAttribute("COLUMN_5_PER").getVal());
            rsHistory.updateDouble("COLUMN_5_AMT", getAttribute("COLUMN_5_AMT").getVal());
            rsHistory.updateString("COLUMN_5_CAPTION", (String) getAttribute("COLUMN_5_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_6_ID", (int) getAttribute("COLUMN_6_ID").getVal());
            rsHistory.updateString("COLUMN_6_FORMULA", (String) getAttribute("COLUMN_6_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_6_PER", getAttribute("COLUMN_6_PER").getVal());
            rsHistory.updateDouble("COLUMN_6_AMT", getAttribute("COLUMN_6_AMT").getVal());
            rsHistory.updateString("COLUMN_6_CAPTION", (String) getAttribute("COLUMN_6_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_7_ID", (int) getAttribute("COLUMN_7_ID").getVal());
            rsHistory.updateString("COLUMN_7_FORMULA", (String) getAttribute("COLUMN_7_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_7_PER", getAttribute("COLUMN_7_PER").getVal());
            rsHistory.updateDouble("COLUMN_7_AMT", getAttribute("COLUMN_7_AMT").getVal());
            rsHistory.updateString("COLUMN_7_CAPTION", (String) getAttribute("COLUMN_7_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_8_ID", (int) getAttribute("COLUMN_8_ID").getVal());
            rsHistory.updateString("COLUMN_8_FORMULA", (String) getAttribute("COLUMN_8_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_8_PER", getAttribute("COLUMN_8_PER").getVal());
            rsHistory.updateDouble("COLUMN_8_AMT", getAttribute("COLUMN_8_AMT").getVal());
            rsHistory.updateString("COLUMN_8_CAPTION", (String) getAttribute("COLUMN_8_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_9_ID", (int) getAttribute("COLUMN_9_ID").getVal());
            rsHistory.updateString("COLUMN_9_FORMULA", (String) getAttribute("COLUMN_9_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_9_PER", getAttribute("COLUMN_9_PER").getVal());
            rsHistory.updateDouble("COLUMN_9_AMT", getAttribute("COLUMN_9_AMT").getVal());
            rsHistory.updateString("COLUMN_9_CAPTION", (String) getAttribute("COLUMN_9_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_10_ID", (int) getAttribute("COLUMN_10_ID").getVal());
            rsHistory.updateString("COLUMN_10_FORMULA", (String) getAttribute("COLUMN_10_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_10_PER", getAttribute("COLUMN_10_PER").getVal());
            rsHistory.updateDouble("COLUMN_10_AMT", getAttribute("COLUMN_10_AMT").getVal());
            rsHistory.updateString("COLUMN_10_CAPTION", (String) getAttribute("COLUMN_10_CAPTION").getObj());

            rsHistory.updateInt("COLUMN_11_ID", (int) getAttribute("COLUMN_11_ID").getVal());
            rsHistory.updateString("COLUMN_11_FORMULA", (String) getAttribute("COLUMN_11_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_11_PER", getAttribute("COLUMN_11_PER").getVal());
            rsHistory.updateDouble("COLUMN_11_AMT", getAttribute("COLUMN_11_AMT").getVal());
            rsHistory.updateString("COLUMN_11_CAPTION", (String) getAttribute("COLUMN_11_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_12_ID", (int) getAttribute("COLUMN_12_ID").getVal());
            rsHistory.updateString("COLUMN_12_FORMULA", (String) getAttribute("COLUMN_12_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_12_PER", getAttribute("COLUMN_12_PER").getVal());
            rsHistory.updateDouble("COLUMN_12_AMT", getAttribute("COLUMN_12_AMT").getVal());
            rsHistory.updateString("COLUMN_12_CAPTION", (String) getAttribute("COLUMN_12_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_13_ID", (int) getAttribute("COLUMN_13_ID").getVal());
            rsHistory.updateString("COLUMN_13_FORMULA", (String) getAttribute("COLUMN_13_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_13_PER", getAttribute("COLUMN_13_PER").getVal());
            rsHistory.updateDouble("COLUMN_13_AMT", getAttribute("COLUMN_13_AMT").getVal());
            rsHistory.updateString("COLUMN_13_CAPTION", (String) getAttribute("COLUMN_13_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_14_ID", (int) getAttribute("COLUMN_14_ID").getVal());
            rsHistory.updateString("COLUMN_14_FORMULA", (String) getAttribute("COLUMN_14_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_14_PER", getAttribute("COLUMN_14_PER").getVal());
            rsHistory.updateDouble("COLUMN_14_AMT", getAttribute("COLUMN_14_AMT").getVal());
            rsHistory.updateString("COLUMN_14_CAPTION", (String) getAttribute("COLUMN_14_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_15_ID", (int) getAttribute("COLUMN_15_ID").getVal());
            rsHistory.updateString("COLUMN_15_FORMULA", (String) getAttribute("COLUMN_15_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_15_PER", getAttribute("COLUMN_15_PER").getVal());
            rsHistory.updateDouble("COLUMN_15_AMT", getAttribute("COLUMN_15_AMT").getVal());
            rsHistory.updateString("COLUMN_15_CAPTION", (String) getAttribute("COLUMN_15_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_16_ID", (int) getAttribute("COLUMN_16_ID").getVal());
            rsHistory.updateString("COLUMN_16_FORMULA", (String) getAttribute("COLUMN_16_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_16_PER", getAttribute("COLUMN_16_PER").getVal());
            rsHistory.updateDouble("COLUMN_16_AMT", getAttribute("COLUMN_16_AMT").getVal());
            rsHistory.updateString("COLUMN_16_CAPTION", (String) getAttribute("COLUMN_16_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_17_ID", (int) getAttribute("COLUMN_17_ID").getVal());
            rsHistory.updateString("COLUMN_17_FORMULA", (String) getAttribute("COLUMN_17_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_17_PER", getAttribute("COLUMN_17_PER").getVal());
            rsHistory.updateDouble("COLUMN_17_AMT", getAttribute("COLUMN_17_AMT").getVal());
            rsHistory.updateString("COLUMN_17_CAPTION", (String) getAttribute("COLUMN_17_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_18_ID", (int) getAttribute("COLUMN_18_ID").getVal());
            rsHistory.updateString("COLUMN_18_FORMULA", (String) getAttribute("COLUMN_18_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_18_PER", getAttribute("COLUMN_18_PER").getVal());
            rsHistory.updateDouble("COLUMN_18_AMT", getAttribute("COLUMN_18_AMT").getVal());
            rsHistory.updateString("COLUMN_18_CAPTION", (String) getAttribute("COLUMN_18_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_19_ID", (int) getAttribute("COLUMN_19_ID").getVal());
            rsHistory.updateString("COLUMN_19_FORMULA", (String) getAttribute("COLUMN_19_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_19_PER", getAttribute("COLUMN_19_PER").getVal());
            rsHistory.updateDouble("COLUMN_19_AMT", getAttribute("COLUMN_19_AMT").getVal());
            rsHistory.updateString("COLUMN_19_CAPTION", (String) getAttribute("COLUMN_19_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_20_ID", (int) getAttribute("COLUMN_20_ID").getVal());
            rsHistory.updateString("COLUMN_20_FORMULA", (String) getAttribute("COLUMN_20_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_20_PER", getAttribute("COLUMN_20_PER").getVal());
            rsHistory.updateDouble("COLUMN_20_AMT", getAttribute("COLUMN_20_AMT").getVal());
            rsHistory.updateString("COLUMN_20_CAPTION", (String) getAttribute("COLUMN_20_CAPTION").getObj());

            rsHistory.updateInt("COLUMN_21_ID", (int) getAttribute("COLUMN_21_ID").getVal());
            rsHistory.updateString("COLUMN_21_FORMULA", (String) getAttribute("COLUMN_21_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_21_PER", getAttribute("COLUMN_21_PER").getVal());
            rsHistory.updateDouble("COLUMN_21_AMT", getAttribute("COLUMN_21_AMT").getVal());
            rsHistory.updateString("COLUMN_21_CAPTION", (String) getAttribute("COLUMN_21_CAPTION").getObj());
            

            rsHistory.updateInt("REASON_CODE", (int) getAttribute("REASON_CODE").getVal());
            rsHistory.updateString("AMEND_REASON", (String) getAttribute("AMEND_REASON").getObj());
            rsHistory.updateBoolean("IMPORT_CONCESS", getAttribute("IMPORT_CONCESS").getBool());
            rsHistory.updateString("PRINT_LINE_1", (String) getAttribute("PRINT_LINE_1").getObj());
            rsHistory.updateString("PRINT_LINE_2", (String) getAttribute("PRINT_LINE_2").getObj());
            rsHistory.updateString("IMPORT_LICENSE", (String) getAttribute("IMPORT_LICENSE").getObj());
            rsHistory.updateBoolean("CHANGED", true);
            rsHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("PAYMENT_TERM", (String) getAttribute("PAYMENT_TERM").getObj());

            rsHistory.updateInt("PAYMENT_CODE", getAttribute("PAYMENT_CODE").getInt());
            rsHistory.updateInt("CR_DAYS", getAttribute("CR_DAYS").getInt());
            rsHistory.updateInt("DEPT_ID", getAttribute("DEPT_ID").getInt());

            rsHistory.updateString("PRICE_BASIS_TERM", (String) getAttribute("PRICE_BASIS_TERM").getObj());
            rsHistory.updateString("DISCOUNT_TERM", (String) getAttribute("DISCOUNT_TERM").getObj());
            rsHistory.updateString("EXCISE_TERM", (String) getAttribute("EXCISE_TERM").getObj());
            rsHistory.updateString("ST_TERM", (String) getAttribute("ST_TERM").getObj());
            rsHistory.updateString("PF_TERM", (String) getAttribute("PF_TERM").getObj());
            rsHistory.updateString("FREIGHT_TERM", (String) getAttribute("FREIGHT_TERM").getObj());
            rsHistory.updateString("OCTROI_TERM", (String) getAttribute("OCTROI_TERM").getObj());
            rsHistory.updateString("FOB_TERM", (String) getAttribute("FOB_TERM").getObj());
            rsHistory.updateString("CIE_TERM", (String) getAttribute("CIE_TERM").getObj());
            rsHistory.updateString("INSURANCE_TERM", (String) getAttribute("INSURANCE_TERM").getObj());
            rsHistory.updateString("TCC_TERM", (String) getAttribute("TCC_TERM").getObj());
            rsHistory.updateString("CENVAT_TERM", (String) getAttribute("CENVAT_TERM").getObj());
            rsHistory.updateString("DESPATCH_TERM", (String) getAttribute("DESPATCH_TERM").getObj());
            rsHistory.updateString("SERVICE_TAX_TERM", (String) getAttribute("SERVICE_TAX_TERM").getObj());
            rsHistory.updateString("OTHERS_TERM", (String) getAttribute("OTHERS_TERM").getObj());
            rsHistory.updateString("COVERING_TEXT", (String) getAttribute("COVERING_TEXT").getObj());
            
            rsHistory.updateString("CGST_TERM",(String)getAttribute("CGST_TERM").getObj());
            rsHistory.updateString("SGST_TERM",(String)getAttribute("SGST_TERM").getObj());
            rsHistory.updateString("IGST_TERM",(String)getAttribute("IGST_TERM").getObj());
            rsHistory.updateString("COMPOSITION_TERM",(String)getAttribute("COMPOSITION_TERM").getObj());
            rsHistory.updateString("RCM_TERM",(String)getAttribute("RCM_TERM").getObj());
            rsHistory.updateString("GST_COMPENSATION_CESS_TERM",(String)getAttribute("GST_COMPENSATION_CESS_TERM").getObj());

            rsHistory.updateString("PREMISES_TYPE", (String) getAttribute("PREMISES_TYPE").getObj());
            rsHistory.updateString("SCOPE", (String) getAttribute("SCOPE").getObj());
            rsHistory.updateString("SERVICE_PERIOD", (String) getAttribute("SERVICE_PERIOD").getObj());
            rsHistory.updateString("SERVICE_FREQUENCY", (String) getAttribute("SERVICE_FREQUENCY").getObj());
            rsHistory.updateString("CONTRACT_DETAILS", (String) getAttribute("CONTRACT_DETAILS").getObj());
            rsHistory.updateString("SERVICE_REPORT", (String) getAttribute("SERVICE_REPORT").getObj());
            rsHistory.updateString("ESI_TERMS", (String) getAttribute("ESI_TERMS").getObj());
            rsHistory.updateString("TERMINATION_TERMS", (String) getAttribute("TERMINATION_TERMS").getObj());
            rsHistory.updateString("FILE_TEXT", (String) getAttribute("FILE_TEXT").getObj());
            rsHistory.updateString("AMOUNT_IN_WORDS", (String) getAttribute("AMOUNT_IN_WORDS").getObj());
            rsHistory.updateBoolean("DIRECTOR_APPROVAL", getAttribute("DIRECTOR_APPROVAL").getBool());
            rsHistory.updateBoolean("IMPORTED", getAttribute("IMPORTED").getBool());

            rsHistory.updateBoolean("APPROVED", false);
            rsHistory.updateString("APPROVED_DATE", "0000-00-00");
            rsHistory.updateBoolean("REJECTED", false);
            rsHistory.updateString("REJECTED_DATE", "0000-00-00");
            rsHistory.updateBoolean("CANCELLED", false);
            rsHistory.insertRow();

            //====== Now turn of P.O. Amendment Items ======
            stItem = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsItem = stItem.executeQuery("SELECT * FROM D_PUR_AMEND_DETAIL WHERE AMEND_NO='1' ");
            rsItem.first();

            for (int i = 1; i <= colPOItems.size(); i++) {
                clsPOAmendItem ObjItem = (clsPOAmendItem) colPOItems.get(Integer.toString(i));

                rsItem.moveToInsertRow();
                rsItem.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
                rsItem.updateString("AMEND_NO", (String) getAttribute("AMEND_NO").getObj());
                rsItem.updateBoolean("LAST_AMENDMENT", getAttribute("LAST_AMENDMENT").getBool());
                rsItem.updateString("PO_NO", (String) getAttribute("PO_NO").getObj());
                rsItem.updateInt("SR_NO", i);
                rsItem.updateInt("PO_TYPE", POType);
                rsItem.updateString("ITEM_ID", (String) ObjItem.getAttribute("ITEM_ID").getObj());
                rsItem.updateString("VENDOR_SHADE", (String) ObjItem.getAttribute("VENDOR_SHADE").getObj());
                rsItem.updateString("SDML_SHADE", (String) ObjItem.getAttribute("SDML_SHADE").getObj());
                rsItem.updateString("ITEM_DESC", (String) ObjItem.getAttribute("ITEM_DESC").getObj());
                rsItem.updateString("HSN_SAC_CODE", (String) ObjItem.getAttribute("HSN_SAC_CODE").getObj());
                rsItem.updateString("MAKE", (String) ObjItem.getAttribute("MAKE").getObj());
                rsItem.updateString("PRICE_LIST_NO", (String) ObjItem.getAttribute("PRICE_LIST_NO").getObj());
                rsItem.updateString("PART_NO", (String) ObjItem.getAttribute("PART_NO").getObj());
                rsItem.updateString("EXCISE_TARRIF_NO", (String) ObjItem.getAttribute("EXCISE_TARRIF_NO").getObj());
                rsItem.updateDouble("QTY", ObjItem.getAttribute("QTY").getVal());
                rsItem.updateDouble("TOLERANCE_LIMIT", ObjItem.getAttribute("TOLERANCE_LIMIT").getVal());
                rsItem.updateDouble("PENDING_QTY", ObjItem.getAttribute("QTY").getVal());
                rsItem.updateDouble("RECD_QTY", 0);
                rsItem.updateInt("UNIT", (int) ObjItem.getAttribute("UNIT").getVal());
                rsItem.updateInt("DEPT_ID", (int) ObjItem.getAttribute("DEPT_ID").getVal());
                rsItem.updateDouble("RATE", ObjItem.getAttribute("RATE").getVal());
                rsItem.updateDouble("LANDED_RATE", ObjItem.getAttribute("LANDED_RATE").getVal());
                rsItem.updateDouble("DISC_PER", ObjItem.getAttribute("DISC_PER").getVal());
                rsItem.updateDouble("DISC_AMT", ObjItem.getAttribute("DISC_AMT").getVal());
                rsItem.updateDouble("TOTAL_AMOUNT", ObjItem.getAttribute("TOTAL_AMOUNT").getVal());
                rsItem.updateDouble("NET_AMOUNT", ObjItem.getAttribute("NET_AMOUNT").getVal());
                rsItem.updateString("INDENT_NO", (String) ObjItem.getAttribute("INDENT_NO").getObj());
                rsItem.updateInt("INDENT_SR_NO", (int) ObjItem.getAttribute("INDENT_SR_NO").getVal());
                rsItem.updateString("REFERENCE", (String) ObjItem.getAttribute("REFERENCE").getObj());
                rsItem.updateString("DELIVERY_DATE", (String) ObjItem.getAttribute("DELIVERY_DATE").getObj());
                rsItem.updateString("REMARKS", (String) ObjItem.getAttribute("REMARKS").getObj());
                rsItem.updateLong("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                rsItem.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                //rsItem.updateLong("MODIFIED_BY",EITLERPGLOBAL.gNewUserID);
                //rsItem.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsItem.updateInt("COLUMN_1_ID", (int) ObjItem.getAttribute("COLUMN_1_ID").getVal());
                rsItem.updateString("COLUMN_1_FORMULA", (String) ObjItem.getAttribute("COLUMN_1_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_1_PER", ObjItem.getAttribute("COLUMN_1_PER").getVal());
                rsItem.updateDouble("COLUMN_1_AMT", ObjItem.getAttribute("COLUMN_1_AMT").getVal());
                rsItem.updateString("COLUMN_1_CAPTION", (String) ObjItem.getAttribute("COLUMN_1_CAPTION").getObj());
                rsItem.updateInt("COLUMN_2_ID", (int) ObjItem.getAttribute("COLUMN_2_ID").getVal());
                rsItem.updateString("COLUMN_2_FORMULA", (String) ObjItem.getAttribute("COLUMN_2_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_2_PER", ObjItem.getAttribute("COLUMN_2_PER").getVal());
                rsItem.updateDouble("COLUMN_2_AMT", ObjItem.getAttribute("COLUMN_2_AMT").getVal());
                rsItem.updateString("COLUMN_2_CAPTION", (String) ObjItem.getAttribute("COLUMN_2_CAPTION").getObj());
                rsItem.updateInt("COLUMN_3_ID", (int) ObjItem.getAttribute("COLUMN_3_ID").getVal());
                rsItem.updateString("COLUMN_3_FORMULA", (String) ObjItem.getAttribute("COLUMN_3_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_3_PER", ObjItem.getAttribute("COLUMN_3_PER").getVal());
                rsItem.updateDouble("COLUMN_3_AMT", ObjItem.getAttribute("COLUMN_3_AMT").getVal());
                rsItem.updateString("COLUMN_3_CAPTION", (String) ObjItem.getAttribute("COLUMN_3_CAPTION").getObj());
                rsItem.updateInt("COLUMN_4_ID", (int) ObjItem.getAttribute("COLUMN_4_ID").getVal());
                rsItem.updateString("COLUMN_4_FORMULA", (String) ObjItem.getAttribute("COLUMN_4_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_4_PER", ObjItem.getAttribute("COLUMN_4_PER").getVal());
                rsItem.updateDouble("COLUMN_4_AMT", ObjItem.getAttribute("COLUMN_4_AMT").getVal());
                rsItem.updateString("COLUMN_4_CAPTION", (String) ObjItem.getAttribute("COLUMN_4_CAPTION").getObj());
                rsItem.updateInt("COLUMN_5_ID", (int) ObjItem.getAttribute("COLUMN_5_ID").getVal());
                rsItem.updateString("COLUMN_5_FORMULA", (String) ObjItem.getAttribute("COLUMN_5_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_5_PER", ObjItem.getAttribute("COLUMN_5_PER").getVal());
                rsItem.updateDouble("COLUMN_5_AMT", ObjItem.getAttribute("COLUMN_5_AMT").getVal());
                rsItem.updateString("COLUMN_5_CAPTION", (String) ObjItem.getAttribute("COLUMN_5_CAPTION").getObj());
                rsItem.updateInt("COLUMN_6_ID", (int) ObjItem.getAttribute("COLUMN_6_ID").getVal());
                rsItem.updateString("COLUMN_6_FORMULA", (String) ObjItem.getAttribute("COLUMN_6_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_6_PER", ObjItem.getAttribute("COLUMN_6_PER").getVal());
                rsItem.updateDouble("COLUMN_6_AMT", ObjItem.getAttribute("COLUMN_6_AMT").getVal());
                rsItem.updateString("COLUMN_6_CAPTION", (String) ObjItem.getAttribute("COLUMN_6_CAPTION").getObj());
                rsItem.updateInt("COLUMN_7_ID", (int) ObjItem.getAttribute("COLUMN_7_ID").getVal());
                rsItem.updateString("COLUMN_7_FORMULA", (String) ObjItem.getAttribute("COLUMN_7_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_7_PER", ObjItem.getAttribute("COLUMN_7_PER").getVal());
                rsItem.updateDouble("COLUMN_7_AMT", ObjItem.getAttribute("COLUMN_7_AMT").getVal());
                rsItem.updateString("COLUMN_7_CAPTION", (String) ObjItem.getAttribute("COLUMN_7_CAPTION").getObj());
                rsItem.updateInt("COLUMN_8_ID", (int) ObjItem.getAttribute("COLUMN_8_ID").getVal());
                rsItem.updateString("COLUMN_8_FORMULA", (String) ObjItem.getAttribute("COLUMN_8_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_8_PER", ObjItem.getAttribute("COLUMN_8_PER").getVal());
                rsItem.updateDouble("COLUMN_8_AMT", ObjItem.getAttribute("COLUMN_8_AMT").getVal());
                rsItem.updateString("COLUMN_8_CAPTION", (String) ObjItem.getAttribute("COLUMN_8_CAPTION").getObj());
                rsItem.updateInt("COLUMN_9_ID", (int) ObjItem.getAttribute("COLUMN_9_ID").getVal());
                rsItem.updateString("COLUMN_9_FORMULA", (String) ObjItem.getAttribute("COLUMN_9_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_9_PER", ObjItem.getAttribute("COLUMN_9_PER").getVal());
                rsItem.updateDouble("COLUMN_9_AMT", ObjItem.getAttribute("COLUMN_9_AMT").getVal());
                rsItem.updateString("COLUMN_9_CAPTION", (String) ObjItem.getAttribute("COLUMN_9_CAPTION").getObj());
                rsItem.updateInt("COLUMN_10_ID", (int) ObjItem.getAttribute("COLUMN_10_ID").getVal());
                rsItem.updateString("COLUMN_10_FORMULA", (String) ObjItem.getAttribute("COLUMN_10_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_10_PER", ObjItem.getAttribute("COLUMN_10_PER").getVal());
                rsItem.updateDouble("COLUMN_10_AMT", ObjItem.getAttribute("COLUMN_10_AMT").getVal());
                rsItem.updateString("COLUMN_10_CAPTION", (String) ObjItem.getAttribute("COLUMN_10_CAPTION").getObj());

                rsItem.updateInt("COLUMN_11_ID", (int) ObjItem.getAttribute("COLUMN_11_ID").getVal());
                rsItem.updateString("COLUMN_11_FORMULA", (String) ObjItem.getAttribute("COLUMN_11_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_11_PER", ObjItem.getAttribute("COLUMN_11_PER").getVal());
                rsItem.updateDouble("COLUMN_11_AMT", ObjItem.getAttribute("COLUMN_11_AMT").getVal());
                rsItem.updateString("COLUMN_11_CAPTION", (String) ObjItem.getAttribute("COLUMN_11_CAPTION").getObj());
                rsItem.updateInt("COLUMN_12_ID", (int) ObjItem.getAttribute("COLUMN_12_ID").getVal());
                rsItem.updateString("COLUMN_12_FORMULA", (String) ObjItem.getAttribute("COLUMN_12_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_12_PER", ObjItem.getAttribute("COLUMN_12_PER").getVal());
                rsItem.updateDouble("COLUMN_12_AMT", ObjItem.getAttribute("COLUMN_12_AMT").getVal());
                rsItem.updateString("COLUMN_12_CAPTION", (String) ObjItem.getAttribute("COLUMN_12_CAPTION").getObj());
                rsItem.updateInt("COLUMN_13_ID", (int) ObjItem.getAttribute("COLUMN_13_ID").getVal());
                rsItem.updateString("COLUMN_13_FORMULA", (String) ObjItem.getAttribute("COLUMN_13_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_13_PER", ObjItem.getAttribute("COLUMN_13_PER").getVal());
                rsItem.updateDouble("COLUMN_13_AMT", ObjItem.getAttribute("COLUMN_13_AMT").getVal());
                rsItem.updateString("COLUMN_13_CAPTION", (String) ObjItem.getAttribute("COLUMN_13_CAPTION").getObj());
                rsItem.updateInt("COLUMN_14_ID", (int) ObjItem.getAttribute("COLUMN_14_ID").getVal());
                rsItem.updateString("COLUMN_14_FORMULA", (String) ObjItem.getAttribute("COLUMN_14_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_14_PER", ObjItem.getAttribute("COLUMN_14_PER").getVal());
                rsItem.updateDouble("COLUMN_14_AMT", ObjItem.getAttribute("COLUMN_14_AMT").getVal());
                rsItem.updateString("COLUMN_14_CAPTION", (String) ObjItem.getAttribute("COLUMN_14_CAPTION").getObj());
                rsItem.updateInt("COLUMN_15_ID", (int) ObjItem.getAttribute("COLUMN_15_ID").getVal());
                rsItem.updateString("COLUMN_15_FORMULA", (String) ObjItem.getAttribute("COLUMN_15_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_15_PER", ObjItem.getAttribute("COLUMN_15_PER").getVal());
                rsItem.updateDouble("COLUMN_15_AMT", ObjItem.getAttribute("COLUMN_15_AMT").getVal());
                rsItem.updateString("COLUMN_15_CAPTION", (String) ObjItem.getAttribute("COLUMN_15_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_16_ID", (int) ObjItem.getAttribute("COLUMN_16_ID").getVal());
                rsItem.updateString("COLUMN_16_FORMULA", (String) ObjItem.getAttribute("COLUMN_16_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_16_PER", ObjItem.getAttribute("COLUMN_16_PER").getVal());
                rsItem.updateDouble("COLUMN_16_AMT", ObjItem.getAttribute("COLUMN_16_AMT").getVal());
                rsItem.updateString("COLUMN_16_CAPTION", (String) ObjItem.getAttribute("COLUMN_16_CAPTION").getObj());
                rsItem.updateInt("COLUMN_17_ID", (int) ObjItem.getAttribute("COLUMN_17_ID").getVal());
                rsItem.updateString("COLUMN_17_FORMULA", (String) ObjItem.getAttribute("COLUMN_17_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_17_PER", ObjItem.getAttribute("COLUMN_17_PER").getVal());
                rsItem.updateDouble("COLUMN_17_AMT", ObjItem.getAttribute("COLUMN_17_AMT").getVal());
                rsItem.updateString("COLUMN_17_CAPTION", (String) ObjItem.getAttribute("COLUMN_17_CAPTION").getObj());
                rsItem.updateInt("COLUMN_18_ID", (int) ObjItem.getAttribute("COLUMN_18_ID").getVal());
                rsItem.updateString("COLUMN_18_FORMULA", (String) ObjItem.getAttribute("COLUMN_18_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_18_PER", ObjItem.getAttribute("COLUMN_18_PER").getVal());
                rsItem.updateDouble("COLUMN_18_AMT", ObjItem.getAttribute("COLUMN_18_AMT").getVal());
                rsItem.updateString("COLUMN_18_CAPTION", (String) ObjItem.getAttribute("COLUMN_18_CAPTION").getObj());
                rsItem.updateInt("COLUMN_19_ID", (int) ObjItem.getAttribute("COLUMN_19_ID").getVal());
                rsItem.updateString("COLUMN_19_FORMULA", (String) ObjItem.getAttribute("COLUMN_19_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_19_PER", ObjItem.getAttribute("COLUMN_19_PER").getVal());
                rsItem.updateDouble("COLUMN_19_AMT", ObjItem.getAttribute("COLUMN_19_AMT").getVal());
                rsItem.updateString("COLUMN_19_CAPTION", (String) ObjItem.getAttribute("COLUMN_19_CAPTION").getObj());
                rsItem.updateInt("COLUMN_20_ID", (int) ObjItem.getAttribute("COLUMN_20_ID").getVal());
                rsItem.updateString("COLUMN_20_FORMULA", (String) ObjItem.getAttribute("COLUMN_20_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_20_PER", ObjItem.getAttribute("COLUMN_20_PER").getVal());
                rsItem.updateDouble("COLUMN_20_AMT", ObjItem.getAttribute("COLUMN_20_AMT").getVal());
                rsItem.updateString("COLUMN_20_CAPTION", (String) ObjItem.getAttribute("COLUMN_20_CAPTION").getObj());

                rsItem.updateInt("COLUMN_21_ID", (int) ObjItem.getAttribute("COLUMN_21_ID").getVal());
                rsItem.updateString("COLUMN_21_FORMULA", (String) ObjItem.getAttribute("COLUMN_21_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_21_PER", ObjItem.getAttribute("COLUMN_21_PER").getVal());
                rsItem.updateDouble("COLUMN_21_AMT", ObjItem.getAttribute("COLUMN_21_AMT").getVal());
                rsItem.updateString("COLUMN_21_CAPTION", (String) ObjItem.getAttribute("COLUMN_21_CAPTION").getObj());
                

                rsItem.updateBoolean("EXCISE_GATEPASS_GIVEN", ObjItem.getAttribute("EXCISE_GATEPASS_GIVEN").getBool());
                rsItem.updateBoolean("IMPORT_CONCESS", ObjItem.getAttribute("IMPORT_CONCESS").getBool());
                rsItem.updateString("QUOT_ID", (String) ObjItem.getAttribute("QUOT_ID").getObj());
                rsItem.updateInt("QUOT_SR_NO", (int) ObjItem.getAttribute("QUOT_SR_NO").getVal());
                rsItem.updateBoolean("CHANGED", true);
                rsItem.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsItem.insertRow();

                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO", 1);
                rsHDetail.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
                rsHDetail.updateString("AMEND_NO", (String) getAttribute("AMEND_NO").getObj());
                rsHDetail.updateBoolean("LAST_AMENDMENT", getAttribute("LAST_AMENDMENT").getBool());
                rsHDetail.updateString("PO_NO", (String) getAttribute("PO_NO").getObj());
                rsHDetail.updateInt("SR_NO", i);
                rsHDetail.updateInt("PO_TYPE", POType);
                rsHDetail.updateString("ITEM_ID", (String) ObjItem.getAttribute("ITEM_ID").getObj());
                rsHDetail.updateString("VENDOR_SHADE", (String) ObjItem.getAttribute("VENDOR_SHADE").getObj());
                rsHDetail.updateString("SDML_SHADE", (String) ObjItem.getAttribute("SDML_SHADE").getObj());
                rsHDetail.updateString("ITEM_DESC", (String) ObjItem.getAttribute("ITEM_DESC").getObj());
                rsHDetail.updateString("HSN_SAC_CODE", (String) ObjItem.getAttribute("HSN_SAC_CODE").getObj());
                rsHDetail.updateString("MAKE", (String) ObjItem.getAttribute("MAKE").getObj());
                rsHDetail.updateString("PRICE_LIST_NO", (String) ObjItem.getAttribute("PRICE_LIST_NO").getObj());
                rsHDetail.updateString("PART_NO", (String) ObjItem.getAttribute("PART_NO").getObj());
                rsHDetail.updateString("EXCISE_TARRIF_NO", (String) ObjItem.getAttribute("EXCISE_TARRIF_NO").getObj());
                rsHDetail.updateDouble("QTY", ObjItem.getAttribute("QTY").getVal());
                rsHDetail.updateDouble("PENDING_QTY", ObjItem.getAttribute("QTY").getVal());
                rsHDetail.updateDouble("RECD_QTY", 0);
                rsHDetail.updateDouble("TOLERANCE_LIMIT", ObjItem.getAttribute("TOLERANCE_LIMIT").getVal());
                rsHDetail.updateInt("UNIT", (int) ObjItem.getAttribute("UNIT").getVal());
                rsHDetail.updateInt("DEPT_ID", (int) ObjItem.getAttribute("DEPT_ID").getVal());
                rsHDetail.updateDouble("RATE", ObjItem.getAttribute("RATE").getVal());
                rsHDetail.updateDouble("LANDED_RATE", ObjItem.getAttribute("LANDED_RATE").getVal());
                rsHDetail.updateDouble("DISC_PER", ObjItem.getAttribute("DISC_PER").getVal());
                rsHDetail.updateDouble("DISC_AMT", ObjItem.getAttribute("DISC_AMT").getVal());
                rsHDetail.updateDouble("TOTAL_AMOUNT", ObjItem.getAttribute("TOTAL_AMOUNT").getVal());
                rsHDetail.updateDouble("NET_AMOUNT", ObjItem.getAttribute("NET_AMOUNT").getVal());
                rsHDetail.updateString("INDENT_NO", (String) ObjItem.getAttribute("INDENT_NO").getObj());
                rsHDetail.updateInt("INDENT_SR_NO", (int) ObjItem.getAttribute("INDENT_SR_NO").getVal());
                rsHDetail.updateString("REFERENCE", (String) ObjItem.getAttribute("REFERENCE").getObj());
                rsHDetail.updateString("DELIVERY_DATE", (String) ObjItem.getAttribute("DELIVERY_DATE").getObj());
                rsHDetail.updateString("REMARKS", (String) ObjItem.getAttribute("REMARKS").getObj());
                rsHDetail.updateLong("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                //rsHDetail.updateLong("MODIFIED_BY",EITLERPGLOBAL.gNewUserID);
                //rsHDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateInt("COLUMN_1_ID", (int) ObjItem.getAttribute("COLUMN_1_ID").getVal());
                rsHDetail.updateString("COLUMN_1_FORMULA", (String) ObjItem.getAttribute("COLUMN_1_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_1_PER", ObjItem.getAttribute("COLUMN_1_PER").getVal());
                rsHDetail.updateDouble("COLUMN_1_AMT", ObjItem.getAttribute("COLUMN_1_AMT").getVal());
                rsHDetail.updateString("COLUMN_1_CAPTION", (String) ObjItem.getAttribute("COLUMN_1_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_2_ID", (int) ObjItem.getAttribute("COLUMN_2_ID").getVal());
                rsHDetail.updateString("COLUMN_2_FORMULA", (String) ObjItem.getAttribute("COLUMN_2_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_2_PER", ObjItem.getAttribute("COLUMN_2_PER").getVal());
                rsHDetail.updateDouble("COLUMN_2_AMT", ObjItem.getAttribute("COLUMN_2_AMT").getVal());
                rsHDetail.updateString("COLUMN_2_CAPTION", (String) ObjItem.getAttribute("COLUMN_2_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_3_ID", (int) ObjItem.getAttribute("COLUMN_3_ID").getVal());
                rsHDetail.updateString("COLUMN_3_FORMULA", (String) ObjItem.getAttribute("COLUMN_3_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_3_PER", ObjItem.getAttribute("COLUMN_3_PER").getVal());
                rsHDetail.updateDouble("COLUMN_3_AMT", ObjItem.getAttribute("COLUMN_3_AMT").getVal());
                rsHDetail.updateString("COLUMN_3_CAPTION", (String) ObjItem.getAttribute("COLUMN_3_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_4_ID", (int) ObjItem.getAttribute("COLUMN_4_ID").getVal());
                rsHDetail.updateString("COLUMN_4_FORMULA", (String) ObjItem.getAttribute("COLUMN_4_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_4_PER", ObjItem.getAttribute("COLUMN_4_PER").getVal());
                rsHDetail.updateDouble("COLUMN_4_AMT", ObjItem.getAttribute("COLUMN_4_AMT").getVal());
                rsHDetail.updateString("COLUMN_4_CAPTION", (String) ObjItem.getAttribute("COLUMN_4_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_5_ID", (int) ObjItem.getAttribute("COLUMN_5_ID").getVal());
                rsHDetail.updateString("COLUMN_5_FORMULA", (String) ObjItem.getAttribute("COLUMN_5_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_5_PER", ObjItem.getAttribute("COLUMN_5_PER").getVal());
                rsHDetail.updateDouble("COLUMN_5_AMT", ObjItem.getAttribute("COLUMN_5_AMT").getVal());
                rsHDetail.updateString("COLUMN_5_CAPTION", (String) ObjItem.getAttribute("COLUMN_5_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_6_ID", (int) ObjItem.getAttribute("COLUMN_6_ID").getVal());
                rsHDetail.updateString("COLUMN_6_FORMULA", (String) ObjItem.getAttribute("COLUMN_6_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_6_PER", ObjItem.getAttribute("COLUMN_6_PER").getVal());
                rsHDetail.updateDouble("COLUMN_6_AMT", ObjItem.getAttribute("COLUMN_6_AMT").getVal());
                rsHDetail.updateString("COLUMN_6_CAPTION", (String) ObjItem.getAttribute("COLUMN_6_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_7_ID", (int) ObjItem.getAttribute("COLUMN_7_ID").getVal());
                rsHDetail.updateString("COLUMN_7_FORMULA", (String) ObjItem.getAttribute("COLUMN_7_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_7_PER", ObjItem.getAttribute("COLUMN_7_PER").getVal());
                rsHDetail.updateDouble("COLUMN_7_AMT", ObjItem.getAttribute("COLUMN_7_AMT").getVal());
                rsHDetail.updateString("COLUMN_7_CAPTION", (String) ObjItem.getAttribute("COLUMN_7_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_8_ID", (int) ObjItem.getAttribute("COLUMN_8_ID").getVal());
                rsHDetail.updateString("COLUMN_8_FORMULA", (String) ObjItem.getAttribute("COLUMN_8_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_8_PER", ObjItem.getAttribute("COLUMN_8_PER").getVal());
                rsHDetail.updateDouble("COLUMN_8_AMT", ObjItem.getAttribute("COLUMN_8_AMT").getVal());
                rsHDetail.updateString("COLUMN_8_CAPTION", (String) ObjItem.getAttribute("COLUMN_8_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_9_ID", (int) ObjItem.getAttribute("COLUMN_9_ID").getVal());
                rsHDetail.updateString("COLUMN_9_FORMULA", (String) ObjItem.getAttribute("COLUMN_9_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_9_PER", ObjItem.getAttribute("COLUMN_9_PER").getVal());
                rsHDetail.updateDouble("COLUMN_9_AMT", ObjItem.getAttribute("COLUMN_9_AMT").getVal());
                rsHDetail.updateString("COLUMN_9_CAPTION", (String) ObjItem.getAttribute("COLUMN_9_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_10_ID", (int) ObjItem.getAttribute("COLUMN_10_ID").getVal());
                rsHDetail.updateString("COLUMN_10_FORMULA", (String) ObjItem.getAttribute("COLUMN_10_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_10_PER", ObjItem.getAttribute("COLUMN_10_PER").getVal());
                rsHDetail.updateDouble("COLUMN_10_AMT", ObjItem.getAttribute("COLUMN_10_AMT").getVal());
                rsHDetail.updateString("COLUMN_10_CAPTION", (String) ObjItem.getAttribute("COLUMN_10_CAPTION").getObj());

                rsHDetail.updateInt("COLUMN_11_ID", (int) ObjItem.getAttribute("COLUMN_11_ID").getVal());
                rsHDetail.updateString("COLUMN_11_FORMULA", (String) ObjItem.getAttribute("COLUMN_11_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_11_PER", ObjItem.getAttribute("COLUMN_11_PER").getVal());
                rsHDetail.updateDouble("COLUMN_11_AMT", ObjItem.getAttribute("COLUMN_11_AMT").getVal());
                rsHDetail.updateString("COLUMN_11_CAPTION", (String) ObjItem.getAttribute("COLUMN_11_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_12_ID", (int) ObjItem.getAttribute("COLUMN_12_ID").getVal());
                rsHDetail.updateString("COLUMN_12_FORMULA", (String) ObjItem.getAttribute("COLUMN_12_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_12_PER", ObjItem.getAttribute("COLUMN_12_PER").getVal());
                rsHDetail.updateDouble("COLUMN_12_AMT", ObjItem.getAttribute("COLUMN_12_AMT").getVal());
                rsHDetail.updateString("COLUMN_12_CAPTION", (String) ObjItem.getAttribute("COLUMN_12_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_13_ID", (int) ObjItem.getAttribute("COLUMN_13_ID").getVal());
                rsHDetail.updateString("COLUMN_13_FORMULA", (String) ObjItem.getAttribute("COLUMN_13_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_13_PER", ObjItem.getAttribute("COLUMN_13_PER").getVal());
                rsHDetail.updateDouble("COLUMN_13_AMT", ObjItem.getAttribute("COLUMN_13_AMT").getVal());
                rsHDetail.updateString("COLUMN_13_CAPTION", (String) ObjItem.getAttribute("COLUMN_13_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_14_ID", (int) ObjItem.getAttribute("COLUMN_14_ID").getVal());
                rsHDetail.updateString("COLUMN_14_FORMULA", (String) ObjItem.getAttribute("COLUMN_14_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_14_PER", ObjItem.getAttribute("COLUMN_14_PER").getVal());
                rsHDetail.updateDouble("COLUMN_14_AMT", ObjItem.getAttribute("COLUMN_14_AMT").getVal());
                rsHDetail.updateString("COLUMN_14_CAPTION", (String) ObjItem.getAttribute("COLUMN_14_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_15_ID", (int) ObjItem.getAttribute("COLUMN_15_ID").getVal());
                rsHDetail.updateString("COLUMN_15_FORMULA", (String) ObjItem.getAttribute("COLUMN_15_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_15_PER", ObjItem.getAttribute("COLUMN_15_PER").getVal());
                rsHDetail.updateDouble("COLUMN_15_AMT", ObjItem.getAttribute("COLUMN_15_AMT").getVal());
                rsHDetail.updateString("COLUMN_15_CAPTION", (String) ObjItem.getAttribute("COLUMN_15_CAPTION").getObj());

                rsHDetail.updateInt("COLUMN_16_ID", (int) ObjItem.getAttribute("COLUMN_16_ID").getVal());
                rsHDetail.updateString("COLUMN_16_FORMULA", (String) ObjItem.getAttribute("COLUMN_16_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_16_PER", ObjItem.getAttribute("COLUMN_16_PER").getVal());
                rsHDetail.updateDouble("COLUMN_16_AMT", ObjItem.getAttribute("COLUMN_16_AMT").getVal());
                rsHDetail.updateString("COLUMN_16_CAPTION", (String) ObjItem.getAttribute("COLUMN_16_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_17_ID", (int) ObjItem.getAttribute("COLUMN_17_ID").getVal());
                rsHDetail.updateString("COLUMN_17_FORMULA", (String) ObjItem.getAttribute("COLUMN_17_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_17_PER", ObjItem.getAttribute("COLUMN_17_PER").getVal());
                rsHDetail.updateDouble("COLUMN_17_AMT", ObjItem.getAttribute("COLUMN_17_AMT").getVal());
                rsHDetail.updateString("COLUMN_17_CAPTION", (String) ObjItem.getAttribute("COLUMN_17_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_18_ID", (int) ObjItem.getAttribute("COLUMN_18_ID").getVal());
                rsHDetail.updateString("COLUMN_18_FORMULA", (String) ObjItem.getAttribute("COLUMN_18_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_18_PER", ObjItem.getAttribute("COLUMN_18_PER").getVal());
                rsHDetail.updateDouble("COLUMN_18_AMT", ObjItem.getAttribute("COLUMN_18_AMT").getVal());
                rsHDetail.updateString("COLUMN_18_CAPTION", (String) ObjItem.getAttribute("COLUMN_18_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_19_ID", (int) ObjItem.getAttribute("COLUMN_19_ID").getVal());
                rsHDetail.updateString("COLUMN_19_FORMULA", (String) ObjItem.getAttribute("COLUMN_19_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_19_PER", ObjItem.getAttribute("COLUMN_19_PER").getVal());
                rsHDetail.updateDouble("COLUMN_19_AMT", ObjItem.getAttribute("COLUMN_19_AMT").getVal());
                rsHDetail.updateString("COLUMN_19_CAPTION", (String) ObjItem.getAttribute("COLUMN_19_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_20_ID", (int) ObjItem.getAttribute("COLUMN_20_ID").getVal());
                rsHDetail.updateString("COLUMN_20_FORMULA", (String) ObjItem.getAttribute("COLUMN_20_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_20_PER", ObjItem.getAttribute("COLUMN_20_PER").getVal());
                rsHDetail.updateDouble("COLUMN_20_AMT", ObjItem.getAttribute("COLUMN_20_AMT").getVal());
                rsHDetail.updateString("COLUMN_20_CAPTION", (String) ObjItem.getAttribute("COLUMN_20_CAPTION").getObj());

                rsHDetail.updateInt("COLUMN_21_ID", (int) ObjItem.getAttribute("COLUMN_21_ID").getVal());
                rsHDetail.updateString("COLUMN_21_FORMULA", (String) ObjItem.getAttribute("COLUMN_21_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_21_PER", ObjItem.getAttribute("COLUMN_21_PER").getVal());
                rsHDetail.updateDouble("COLUMN_21_AMT", ObjItem.getAttribute("COLUMN_21_AMT").getVal());
                rsHDetail.updateString("COLUMN_21_CAPTION", (String) ObjItem.getAttribute("COLUMN_21_CAPTION").getObj());
                
                rsHDetail.updateBoolean("EXCISE_GATEPASS_GIVEN", ObjItem.getAttribute("EXCISE_GATEPASS_GIVEN").getBool());
                rsHDetail.updateBoolean("IMPORT_CONCESS", ObjItem.getAttribute("IMPORT_CONCESS").getBool());
                rsHDetail.updateString("QUOT_ID", (String) ObjItem.getAttribute("QUOT_ID").getObj());
                rsHDetail.updateInt("QUOT_SR_NO", (int) ObjItem.getAttribute("QUOT_SR_NO").getVal());
                rsHDetail.updateBoolean("CHANGED", true);
                rsHDetail.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();

            }

            //====== Now turn of P.O. Terms ======
            stTerms = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTerms = stTerms.executeQuery("SELECT * FROM D_PUR_AMEND_TERMS WHERE AMEND_NO='1'");
            rsTerms.first();

            for (int i = 1; i <= colPOTerms.size(); i++) {
                clsPOTerms ObjItem = (clsPOTerms) colPOTerms.get(Integer.toString(i));
                rsTerms.moveToInsertRow();
                rsTerms.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
                rsTerms.updateString("AMEND_NO", (String) getAttribute("AMEND_NO").getObj());
                rsTerms.updateString("PO_NO", (String) getAttribute("PO_NO").getObj());
                rsTerms.updateInt("SR_NO", i);
                rsTerms.updateInt("PO_TYPE", POType);
                rsTerms.updateString("TERM_TYPE", (String) ObjItem.getAttribute("TERM_TYPE").getObj());
                rsTerms.updateInt("TERM_CODE", (int) ObjItem.getAttribute("TERM_CODE").getVal());
                rsTerms.updateString("TERM_DESC", (String) ObjItem.getAttribute("TERM_DESC").getObj());
                rsTerms.updateBoolean("CHANGED", true);
                rsTerms.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsTerms.insertRow();

                rsHTerms.moveToInsertRow();
                rsHTerms.updateInt("REVISION_NO", 1);
                rsHTerms.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
                rsHTerms.updateString("AMEND_NO", (String) getAttribute("AMEND_NO").getObj());
                rsHTerms.updateString("PO_NO", (String) getAttribute("PO_NO").getObj());
                rsHTerms.updateInt("SR_NO", i);
                rsHTerms.updateInt("PO_TYPE", POType);
                rsHTerms.updateString("TERM_TYPE", (String) ObjItem.getAttribute("TERM_TYPE").getObj());
                rsHTerms.updateInt("TERM_CODE", (int) ObjItem.getAttribute("TERM_CODE").getVal());
                rsHTerms.updateString("TERM_DESC", (String) ObjItem.getAttribute("TERM_DESC").getObj());
                rsHTerms.updateBoolean("CHANGED_DATE", true);
                rsHTerms.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHTerms.insertRow();
            }

            //======== Update the Approval Flow =========
            ObjFlow.CompanyID = (int) getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID = 27 + POType; //P.O. Amendment General

            if (POType == 8) {
                ObjFlow.ModuleID = 47;
            }
            //start add on 110909
            if (POType == 9) {
                ObjFlow.ModuleID = 168;
            }
            //end add on 110909

            ObjFlow.DocNo = (String) getAttribute("AMEND_NO").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "D_PUR_AMEND_HEADER";
            ObjFlow.IsCreator = true;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName = "AMEND_NO";
            ObjFlow.DocDate = (String) getAttribute("AMEND_DATE").getObj();

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
            MoveLast();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }

    //Updates current record
    public boolean Update() {
        Statement stTmp, stIndent, stItem, stPO, stPOLine, stQuotation, stTerms, stHistory, stHDetail, stHTerms;
        ResultSet rsTmp, rsIndent, rsItem, rsPO, rsPOLine, rsQuotation, rsTerms, rsHistory, rsHDetail, rsHTerms;
        Statement stHeader;
        ResultSet rsHeader;

        String strSQL = "", IndentNo = "", PONo = "", AmendNo = "", QuotID = "";
        int CompanyID = 0, IndentSrNo = 0, QuotSrNo = 0;
        double Qty = 0;
        boolean Validate = true;
        int OldHierarchy = 0;

        try {
            String AStatus = (String) getAttribute("APPROVAL_STATUS").getObj();
            if (AStatus.equals("R")) {
                Validate = false;
            }
            Validate = true;
            //======= Check the requisition date ============//
            /*java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
             java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
             java.sql.Date DocDate=java.sql.Date.valueOf((String)getAttribute("AMEND_DATE").getObj());
             
             if((DocDate.after(FinFromDate)||DocDate.compareTo(FinFromDate)==0)&&(DocDate.before(FinToDate)||DocDate.compareTo(FinToDate)==0)) {
             //Withing the year
             }
             else {
             LastError="Document date is not within financial year.";
             return false;
             }*/
            //=====================================================//

            String theDocNo = (String) getAttribute("AMEND_NO").getObj();
            rsTmp = data.getResult("SELECT HIERARCHY_ID FROM D_PUR_AMEND_HEADER WHERE AMEND_NO='" + theDocNo + "'");
            if (rsTmp.getRow() > 0) {
                OldHierarchy = rsTmp.getInt("HIERARCHY_ID");
            }

            // ---- History Related Changes ------ //
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHDetail = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHTerms = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rsHistory = stHistory.executeQuery("SELECT * FROM D_PUR_AMEND_HEADER_H WHERE AMEND_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail = stHDetail.executeQuery("SELECT * FROM D_PUR_AMEND_DETAIL_H WHERE AMEND_NO='1'");
            rsHDetail.first();
            rsHTerms = stHTerms.executeQuery("SELECT * FROM D_PUR_AMEND_TERMS_H WHERE AMEND_NO='1'");
            rsHTerms.first();
            //------------------------------------//

            ApprovalFlow ObjFlow = new ApprovalFlow();

            CompanyID = (int) getAttribute("COMPANY_ID").getVal();
            PONo = (String) getAttribute("PO_NO").getObj();
            AmendNo = (String) getAttribute("AMEND_NO").getObj();

            if (Validate) {
                //====== Checking Qty against receipt qty =====//
                for (int i = 1; i <= colPOItems.size(); i++) {
                    clsPOAmendItem ObjItem = (clsPOAmendItem) colPOItems.get(Integer.toString(i));
                    int lSrNo = (int) ObjItem.getAttribute("SR_NO").getVal();
                    double lQty = ObjItem.getAttribute("QTY").getVal();

                    stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    strSQL = "SELECT RECD_QTY FROM D_PUR_PO_DETAIL WHERE COMPANY_ID=" + CompanyID + " AND PO_NO='" + PONo + "' AND SR_NO=" + lSrNo + " AND PO_TYPE=" + POType;
                    rsTmp = stTmp.executeQuery(strSQL);
                    rsTmp.first();

                    if (rsTmp.getRow() > 0) {
                        if (lQty < rsTmp.getDouble("RECD_QTY")) {
                            LastError = "Cannot amend qty of item sr. no." + lSrNo + " to " + lQty + ".\nAlready " + rsTmp.getDouble("RECD_QTY") + " qty have been received.";
                            return false;
                        }
                    }
                }
                //----------------------------------------------//

                //=========== Check the Quantities entered against Indent============= //
                for (int i = 1; i <= colPOItems.size(); i++) {
                    clsPOAmendItem ObjItem = (clsPOAmendItem) colPOItems.get(Integer.toString(i));
                    String ItemID = (String) ObjItem.getAttribute("ITEM_ID").getObj();
                    IndentNo = (String) ObjItem.getAttribute("INDENT_NO").getObj();
                    IndentSrNo = (int) ObjItem.getAttribute("INDENT_SR_NO").getVal();
                    QuotID = (String) ObjItem.getAttribute("QUOT_ID").getObj();
                    QuotSrNo = (int) ObjItem.getAttribute("QUOT_SR_NO").getVal();

                    double IndentQty = 0;
                    double QuotQty = 0;
                    double PrevQty = 0; //Previously Entered Qty against Indent
                    double CurrentQty = 0; //Currently entered Qty.

                    if ((!IndentNo.trim().equals("")) && (IndentSrNo > 0)) //Indent Entered
                    {
                        //Get the  Indent Qty.
                        stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                        strSQL = "SELECT QTY,ITEM_CODE FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID=" + CompanyID + " AND INDENT_NO='" + IndentNo + "' AND SR_NO=" + IndentSrNo + " ";
                        rsTmp = stTmp.executeQuery(strSQL);
                        rsTmp.first();

                        if (rsTmp.getRow() > 0) {
                            IndentQty = rsTmp.getDouble("QTY");

                            if (!rsTmp.getString("ITEM_CODE").trim().equals(ItemID)) {
                                LastError = "Item Code in PO doesn't match with Indent. Original Item code is " + rsTmp.getString("ITEM_CODE");
                                return false;
                            }

                        }

                        //Get Total Qty Entered in PO Amendment (if any) Against this Indent No.
                        PrevQty = 0;
                        stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                        strSQL = "SELECT SUM(QTY) AS SUMQTY FROM D_PUR_PO_DETAIL WHERE COMPANY_ID=" + CompanyID + " AND INDENT_NO='" + IndentNo + "' AND INDENT_SR_NO=" + IndentSrNo + " AND NOT (PO_NO='" + PONo + "' AND PO_TYPE=" + POType + ") AND PO_NO NOT IN (SELECT PO_NO FROM D_PUR_PO_HEADER WHERE CANCELLED=1)";
                        rsTmp = stTmp.executeQuery(strSQL);
                        rsTmp.first();

                        if (rsTmp.getRow() > 0) {
                            PrevQty = rsTmp.getDouble("SUMQTY");
                        }

                        CurrentQty = ObjItem.getAttribute("QTY").getVal();
                        // Below program Modified by ASHUTOSH on 03-01-2019
                    //Also find duplicate entries - made with Ctrl+C functionality
                    for(int d=1;d<=colPOItems.size();d++) {
                        if(d!=i) {
                            //clsPOItem ObjD=(clsPOItem)colPOItems.get(Integer.toString(d));
                            clsPOAmendItem ObjD = (clsPOAmendItem) colPOItems.get(Integer.toString(d));
                            String tmpIndentNo=(String)ObjD.getAttribute("INDENT_NO").getObj();
                            int tmpIndentSrNo=(int)ObjD.getAttribute("INDENT_SR_NO").getVal();
                            
                            if(tmpIndentNo.equals(IndentNo) && tmpIndentSrNo==IndentSrNo) {
                                CurrentQty+=ObjD.getAttribute("QTY").getVal();
                            }
                        }
                    }
                    //===============================================================
                    // --------End of ASHUTOSH program

                        if ((CurrentQty + PrevQty) > IndentQty) //If total Qty exceeds Indent Qty. Do not allow
                        {
                            LastError = "Total quantity entered " + (CurrentQty + PrevQty) + " exceeds Indent No. " + IndentNo + " Sr. No. " + IndentSrNo + " qty " + IndentQty + ". Please verify the input.";
                            return false;
                        }
                    }

                    //Quotation
                    if ((!QuotID.trim().equals("")) && (QuotSrNo > 0)) //Indent Entered
                    {
                        //Get the  Indent Qty.
                        stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                        strSQL = "SELECT QTY FROM D_PUR_QUOT_DETAIL WHERE COMPANY_ID=" + CompanyID + " AND QUOT_ID='" + QuotID + "' AND SR_NO=" + QuotSrNo + " ";
                        rsTmp = stTmp.executeQuery(strSQL);
                        rsTmp.first();

                        if (rsTmp.getRow() > 0) {
                            QuotQty = rsTmp.getDouble("QTY");
                        }

                        //Get Total Qty Entered in PO Amendment (if any) Against this Indent No.
                        PrevQty = 0;
                        stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                        strSQL = "SELECT SUM(QTY) AS SUMQTY FROM D_PUR_PO_DETAIL WHERE COMPANY_ID=" + CompanyID + " AND QUOT_ID='" + QuotID + "' AND QUOT_SR_NO=" + QuotSrNo + " AND NOT (PO_NO='" + PONo + "' AND PO_TYPE=" + POType + ") AND PO_NO NOT IN(SELECT PO_NO FROM D_PUR_PO_HEADER WHERE CANCELLED=1)";
                        rsTmp = stTmp.executeQuery(strSQL);
                        rsTmp.first();

                        if (rsTmp.getRow() > 0) {
                            PrevQty = rsTmp.getDouble("SUMQTY");
                        }

                        CurrentQty = ObjItem.getAttribute("QTY").getVal();

                        if ((CurrentQty + PrevQty) > QuotQty) //If total Qty exceeds Indent Qty. Do not allow
                        {
                            LastError = "Total quantity entered " + (CurrentQty + PrevQty) + " exceeds Quotation No. " + QuotID + " Sr. No. " + QuotSrNo + " qty " + QuotQty + ". Please verify the input.";
                            return false;
                        }
                    }

                }
                //============== Indent Checking Completed ====================//

                //======= Checking with RIA for approved qty =========//
                //===================================================//
                String Messages = "";
                boolean ContinuePO = true;

                if (POType == 1 || POType == 3 || POType == 5 || POType == 9) {
                    for (int i = 1; i <= colPOItems.size(); i++) {
                        clsPOAmendItem ObjItem = (clsPOAmendItem) colPOItems.get(Integer.toString(i));
                        IndentNo = (String) ObjItem.getAttribute("INDENT_NO").getObj();
                        IndentSrNo = (int) ObjItem.getAttribute("INDENT_SR_NO").getVal();
                        String ItemID = (String) ObjItem.getAttribute("ITEM_ID").getObj();
                        Qty = ObjItem.getAttribute("QTY").getVal();

                        //First check that RIA is created
                        if ((!IndentNo.trim().equals("")) && IndentSrNo > 0) {
                            String RIANo = clsIndent.getRIANo(EITLERPGLOBAL.gCompanyID, IndentNo, IndentSrNo);
                            if (!RIANo.trim().equals("")) {
                                int RIAStatus = clsRateApproval.getRIAStatus(EITLERPGLOBAL.gCompanyID, RIANo);
                                double RIAQty = clsIndent.getRIAQty(EITLERPGLOBAL.gCompanyID, IndentNo, IndentSrNo);

                                if (RIAStatus == 1) //RIA is made and approved
                                {
                                    if (Qty <= RIAQty) {
                                        // APPROVED. CONTINUE PREPARING PURCHASE ORDER
                                    } else {
                                        //Qty in Purchase Order getting exceeded to RIA Approved qty.
                                        Messages += "\nOnly " + RIAQty + " is approved for item " + ItemID;
                                        //ContinuePO=false;
                                        ContinuePO = true;
                                    }
                                } else {  //RIA under approval. Reject it
                                    Messages += "\nRIA No. " + RIANo + " is under approval of item " + ItemID;
                                    ContinuePO = false;

                                }

                            } else {
                                //Find the last RIA No.

                                //Following statement will return any last RIA made for the item
                                String PODate = (String) getAttribute("PO_DATE").getObj();

                                RIANo = clsRateApproval.getRIANo(EITLERPGLOBAL.gCompanyID, ItemID, PODate);
                                if (RIANo.trim().equals("")) {
                                    //Continue with last RIA No.
                                } else {
                                    //Put the code here if PO is to be restricted to make RIA compulsoary.
                                }

                            }
                        } else {
                            //No Indent Reference

                            //Following statement will return any last RIA made for the item
                            String PODate = (String) getAttribute("PO_DATE").getObj();

                            String RIANo = clsRateApproval.getRIANo(EITLERPGLOBAL.gCompanyID, ItemID, PODate);
                            if (!RIANo.trim().equals("")) {
                                int RIAStatus = clsRateApproval.getRIAStatus(EITLERPGLOBAL.gCompanyID, RIANo);
                                double RIAQty = clsRateApproval.getRIAApprovedQty(EITLERPGLOBAL.gCompanyID, RIANo, ItemID);

                                if (RIAStatus == 1) //RIA is made and approved
                                {
                                    if (Qty <= RIAQty) {
                                        // APPROVED. CONTINUE PREPARING PURCHASE ORDER
                                    } else {
                                        //Qty in Purchase Order getting exceeded to RIA Approved qty.
                                        Messages += "\nOnly " + RIAQty + " is approved for item " + ItemID;
                                        // ContinuePO=false; //---->Change here on 14 May 2006 //
                                        ContinuePO = true;
                                    }
                                } else {  //RIA under approval. Reject it
                                    Messages += "\nRIA No. " + RIANo + " is under approval of item " + ItemID;
                                    ContinuePO = false;

                                }
                            }
                        }

                    }

                    //RIA Checking will only affec
                    if (!AStatus.equals("F")) {
                        if (!ContinuePO) {
                            JOptionPane.showMessageDialog(null, "PO will not be final approved. Following RIAs are not complete" + Messages);
                        }
                    } else {
                        if (!ContinuePO) {
                            LastError = "Can not final approve the PO. Following RIAs are not complete" + Messages;
                            return false;
                        }

                    }
                }
                //==================================================//
                //=================================================//

                if (AStatus.equals("F")) {

                    //--- Reverse the Effect of last P.O. --- //
                    strSQL = "SELECT * FROM D_PUR_PO_DETAIL WHERE COMPANY_ID=" + CompanyID + " AND PO_NO='" + PONo + "' AND PO_TYPE=" + POType + " ";

                    stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    rsTmp = stTmp.executeQuery(strSQL);
                    rsTmp.first();

                    while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {
                        IndentNo = rsTmp.getString("INDENT_NO");
                        IndentSrNo = rsTmp.getInt("INDENT_SR_NO");
                        QuotID = rsTmp.getString("QUOT_ID");
                        QuotSrNo = rsTmp.getInt("QUOT_SR_NO");
                        Qty = rsTmp.getDouble("QTY");

                        try {
                            // Update Indent
                            data.Execute("UPDATE D_INV_INDENT_DETAIL SET PO_QTY=PO_QTY-" + Qty + ",BAL_QTY=QTY-PO_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + CompanyID + " AND INDENT_NO='" + IndentNo.trim() + "' AND SR_NO=" + IndentSrNo + " ");
                            data.Execute("UPDATE D_INV_INDENT_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + CompanyID + " AND INDENT_NO='" + IndentNo.trim() + "'");

                            // Update Quotation
                            data.Execute("UPDATE D_PUR_QUOT_DETAIL SET PO_QTY=PO_QTY-" + Qty + ",BAL_QTY=QTY-PO_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + CompanyID + " AND QUOT_ID='" + QuotID.trim() + "' AND SR_NO=" + QuotSrNo + " ");
                            data.Execute("UPDATE D_PUR_QUOT_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + CompanyID + " AND QUOT_ID='" + QuotID.trim() + "'");
                        } catch (Exception e) {

                        }
                        rsTmp.next();
                    }
                    //---------------------------------------//

                    //-------- First Update the stock -------------//
                    for (int i = 1; i <= colPOItems.size(); i++) {
                        clsPOAmendItem ObjItem = (clsPOAmendItem) colPOItems.get(Integer.toString(i));

                        IndentNo = (String) ObjItem.getAttribute("INDENT_NO").getObj();
                        IndentSrNo = (int) ObjItem.getAttribute("INDENT_SR_NO").getVal();
                        QuotID = (String) ObjItem.getAttribute("QUOT_ID").getObj();
                        QuotSrNo = (int) ObjItem.getAttribute("QUOT_SR_NO").getVal();

                        Qty = ObjItem.getAttribute("QTY").getVal();

                        try {
                            // Update Indent
                            data.Execute("UPDATE D_INV_INDENT_DETAIL SET PO_QTY=PO_QTY+" + Qty + ",BAL_QTY=QTY-PO_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + CompanyID + " AND INDENT_NO='" + IndentNo.trim() + "' AND SR_NO=" + IndentSrNo + " ");
                            data.Execute("UPDATE D_INV_INDENT_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + CompanyID + " AND INDENT_NO='" + IndentNo.trim() + "'");

                            // Update Quotation
                            data.Execute("UPDATE D_PUR_QUOT_DETAIL SET PO_QTY=PO_QTY+" + Qty + ",BAL_QTY=QTY-PO_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + CompanyID + " AND QUOT_ID='" + QuotID.trim() + "' AND SR_NO=" + QuotSrNo + " ");
                            data.Execute("UPDATE D_PUR_QUOT_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + CompanyID + " AND QUOT_ID='" + QuotID.trim() + "'");
                        } catch (Exception e) {

                        }
                    } //First for loop
                    //=============Updation of stock completed=========================//
                } // End of First Approval Status condition

                //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                //rsHeader=stHeader.executeQuery("SELECT * FROM D_PUR_AMEND_HEADER WHERE COMPANY_ID="+CompanyID+" AND TRIM(AMEND_NO)='"+AmendNo+"' AND PO_TYPE="+POType);
                //rsHeader.first();
                if (AStatus.equals("F")) {
                    //-------- First set all Last_Amendment flags to False ------//
                    data.Execute("UPDATE D_PUR_AMEND_HEADER SET LAST_AMENDMENT=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + CompanyID + " AND PO_NO='" + PONo + "' AND PO_TYPE=" + POType);
                    data.Execute("UPDATE D_PUR_AMEND_DETAIL SET LAST_AMENDMENT=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + CompanyID + " AND PO_NO='" + PONo + "' AND PO_TYPE=" + POType);
                    //------------------------------------------------------------------------//
                    rsResultSet.updateBoolean("LAST_AMENDMENT", true);
                }

                //Following code implements new method of handling amendments
                if (AStatus.equals("F")) {
                    //Now Get PO Object and Update the PO Table with current Data.
                    clsPOGen tmpPO = new clsPOGen();
                    tmpPO.POType = POType;

                    clsPOGen ObjPO = (clsPOGen) tmpPO.getObject(EITLERPGLOBAL.gCompanyID, PONo);

                    //Remove current data of PO
                    data.Execute("DELETE FROM D_PUR_PO_HEADER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PO_NO='" + PONo + "' AND PO_TYPE=" + POType);
                    data.Execute("DELETE FROM D_PUR_PO_DETAIL WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PO_NO='" + PONo + "' AND PO_TYPE=" + POType);
                    data.Execute("DELETE FROM D_PUR_PO_TERMS WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PO_NO='" + PONo + "' AND PO_TYPE=" + POType);

                    //Now update the PO Tables with current data
                    stPO = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    rsPO = stPO.executeQuery("SELECT * FROM D_PUR_PO_HEADER WHERE PO_NO='1'");

                    rsPO.first();
                    rsPO.moveToInsertRow();
                    rsPO.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
                    rsPO.updateString("PO_NO", (String) getAttribute("PO_NO").getObj());
                    rsPO.updateLong("DOC_ID", (long) getAttribute("DOC_ID").getVal());
                    rsPO.updateString("PO_DATE", (String) getAttribute("PO_DATE").getObj());
                    rsPO.updateInt("PO_TYPE", POType);
                    rsPO.updateString("PO_REF", (String) getAttribute("PO_REF").getObj());
                    rsPO.updateString("SUPP_ID", (String) getAttribute("SUPP_ID").getObj());
                    rsPO.updateString("SUPP_NAME", (String) getAttribute("SUPP_NAME").getObj());
                    rsPO.updateString("REF_A", (String) getAttribute("REF_A").getObj());
                    rsPO.updateString("REF_B", (String) getAttribute("REF_B").getObj());
                    rsPO.updateString("QUOTATION_NO", (String) getAttribute("QUOTATION_NO").getObj());
                    rsPO.updateString("QUOTATION_DATE", (String) getAttribute("QUOTATION_DATE").getObj());
                    rsPO.updateString("INQUIRY_NO", (String) getAttribute("INQUIRY_NO").getObj());
                    rsPO.updateString("INQUIRY_DATE", (String) getAttribute("INQUIRY_DATE").getObj());
                    rsPO.updateInt("BUYER", (int) getAttribute("BUYER").getVal());
                    rsPO.updateInt("TRANSPORT_MODE", (int) getAttribute("TRANSPORT_MODE").getVal());
                    rsPO.updateString("PURPOSE", (String) getAttribute("PURPOSE").getObj());
                    rsPO.updateString("SUBJECT", (String) getAttribute("SUBJECT").getObj());
                    rsPO.updateInt("CURRENCY_ID", (int) getAttribute("CURRENCY_ID").getVal());
                    rsPO.updateDouble("CURRENCY_RATE", getAttribute("CURRENCY_RATE").getVal());
                    rsPO.updateDouble("TOTAL_AMOUNT", getAttribute("TOTAL_AMOUNT").getVal());
                    rsPO.updateDouble("NET_AMOUNT", getAttribute("NET_AMOUNT").getVal());
                    rsPO.updateString("STATUS", (String) getAttribute("STATUS").getObj());
                    rsPO.updateBoolean("ATTACHEMENT", getAttribute("ATTACHEMENT").getBool());
                    rsPO.updateString("ATTACHEMENT_PATH", (String) getAttribute("ATTACHEMENT_PATH").getObj());
                    rsPO.updateString("REMARKS", (String) getAttribute("REMARKS").getObj());
                    rsPO.updateInt("SHIP_ID", (int) getAttribute("SHIP_ID").getVal());
                    rsPO.updateBoolean("CANCELLED", getAttribute("CANCELLED").getBool());
                    rsPO.updateBoolean("APPROVED", true);
                    rsPO.updateBoolean("REJECTED", false);
                    rsPO.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                    //rsPO.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
                    //rsPO.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                    rsPO.updateLong("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                    rsPO.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    //Now Custom Columns
                    rsPO.updateInt("COLUMN_1_ID", (int) getAttribute("COLUMN_1_ID").getVal());
                    rsPO.updateString("COLUMN_1_FORMULA", (String) getAttribute("COLUMN_1_FORMULA").getObj());
                    rsPO.updateDouble("COLUMN_1_PER", getAttribute("COLUMN_1_PER").getVal());
                    rsPO.updateDouble("COLUMN_1_AMT", getAttribute("COLUMN_1_AMT").getVal());
                    rsPO.updateString("COLUMN_1_CAPTION", (String) getAttribute("COLUMN_1_CAPTION").getObj());
                    rsPO.updateInt("COLUMN_2_ID", (int) getAttribute("COLUMN_2_ID").getVal());
                    rsPO.updateString("COLUMN_2_FORMULA", (String) getAttribute("COLUMN_2_FORMULA").getObj());
                    rsPO.updateDouble("COLUMN_2_PER", getAttribute("COLUMN_2_PER").getVal());
                    rsPO.updateDouble("COLUMN_2_AMT", getAttribute("COLUMN_2_AMT").getVal());
                    rsPO.updateString("COLUMN_2_CAPTION", (String) getAttribute("COLUMN_2_CAPTION").getObj());
                    rsPO.updateInt("COLUMN_3_ID", (int) getAttribute("COLUMN_3_ID").getVal());
                    rsPO.updateString("COLUMN_3_FORMULA", (String) getAttribute("COLUMN_3_FORMULA").getObj());
                    rsPO.updateDouble("COLUMN_3_PER", getAttribute("COLUMN_3_PER").getVal());
                    rsPO.updateDouble("COLUMN_3_AMT", getAttribute("COLUMN_3_AMT").getVal());
                    rsPO.updateString("COLUMN_3_CAPTION", (String) getAttribute("COLUMN_3_CAPTION").getObj());
                    rsPO.updateInt("COLUMN_4_ID", (int) getAttribute("COLUMN_4_ID").getVal());
                    rsPO.updateString("COLUMN_4_FORMULA", (String) getAttribute("COLUMN_4_FORMULA").getObj());
                    rsPO.updateDouble("COLUMN_4_PER", getAttribute("COLUMN_4_PER").getVal());
                    rsPO.updateDouble("COLUMN_4_AMT", getAttribute("COLUMN_4_AMT").getVal());
                    rsPO.updateString("COLUMN_4_CAPTION", (String) getAttribute("COLUMN_4_CAPTION").getObj());
                    rsPO.updateInt("COLUMN_5_ID", (int) getAttribute("COLUMN_5_ID").getVal());
                    rsPO.updateString("COLUMN_5_FORMULA", (String) getAttribute("COLUMN_5_FORMULA").getObj());
                    rsPO.updateDouble("COLUMN_5_PER", getAttribute("COLUMN_5_PER").getVal());
                    rsPO.updateDouble("COLUMN_5_AMT", getAttribute("COLUMN_5_AMT").getVal());
                    rsPO.updateString("COLUMN_5_CAPTION", (String) getAttribute("COLUMN_5_CAPTION").getObj());
                    rsPO.updateInt("COLUMN_6_ID", (int) getAttribute("COLUMN_6_ID").getVal());
                    rsPO.updateString("COLUMN_6_FORMULA", (String) getAttribute("COLUMN_6_FORMULA").getObj());
                    rsPO.updateDouble("COLUMN_6_PER", getAttribute("COLUMN_6_PER").getVal());
                    rsPO.updateDouble("COLUMN_6_AMT", getAttribute("COLUMN_6_AMT").getVal());
                    rsPO.updateString("COLUMN_6_CAPTION", (String) getAttribute("COLUMN_6_CAPTION").getObj());
                    rsPO.updateInt("COLUMN_7_ID", (int) getAttribute("COLUMN_7_ID").getVal());
                    rsPO.updateString("COLUMN_7_FORMULA", (String) getAttribute("COLUMN_7_FORMULA").getObj());
                    rsPO.updateDouble("COLUMN_7_PER", getAttribute("COLUMN_7_PER").getVal());
                    rsPO.updateDouble("COLUMN_7_AMT", getAttribute("COLUMN_7_AMT").getVal());
                    rsPO.updateString("COLUMN_7_CAPTION", (String) getAttribute("COLUMN_7_CAPTION").getObj());
                    rsPO.updateInt("COLUMN_8_ID", (int) getAttribute("COLUMN_8_ID").getVal());
                    rsPO.updateString("COLUMN_8_FORMULA", (String) getAttribute("COLUMN_8_FORMULA").getObj());
                    rsPO.updateDouble("COLUMN_8_PER", getAttribute("COLUMN_8_PER").getVal());
                    rsPO.updateDouble("COLUMN_8_AMT", getAttribute("COLUMN_8_AMT").getVal());
                    rsPO.updateString("COLUMN_8_CAPTION", (String) getAttribute("COLUMN_8_CAPTION").getObj());
                    rsPO.updateInt("COLUMN_9_ID", (int) getAttribute("COLUMN_9_ID").getVal());
                    rsPO.updateString("COLUMN_9_FORMULA", (String) getAttribute("COLUMN_9_FORMULA").getObj());
                    rsPO.updateDouble("COLUMN_9_PER", getAttribute("COLUMN_9_PER").getVal());
                    rsPO.updateDouble("COLUMN_9_AMT", getAttribute("COLUMN_9_AMT").getVal());
                    rsPO.updateString("COLUMN_9_CAPTION", (String) getAttribute("COLUMN_9_CAPTION").getObj());
                    rsPO.updateInt("COLUMN_10_ID", (int) getAttribute("COLUMN_10_ID").getVal());
                    rsPO.updateString("COLUMN_10_FORMULA", (String) getAttribute("COLUMN_10_FORMULA").getObj());
                    rsPO.updateDouble("COLUMN_10_PER", getAttribute("COLUMN_10_PER").getVal());
                    rsPO.updateDouble("COLUMN_10_AMT", getAttribute("COLUMN_10_AMT").getVal());
                    rsPO.updateString("COLUMN_10_CAPTION", (String) getAttribute("COLUMN_10_CAPTION").getObj());

                    rsPO.updateInt("COLUMN_11_ID", (int) getAttribute("COLUMN_11_ID").getVal());
                    rsPO.updateString("COLUMN_11_FORMULA", (String) getAttribute("COLUMN_11_FORMULA").getObj());
                    rsPO.updateDouble("COLUMN_11_PER", getAttribute("COLUMN_11_PER").getVal());
                    rsPO.updateDouble("COLUMN_11_AMT", getAttribute("COLUMN_11_AMT").getVal());
                    rsPO.updateString("COLUMN_11_CAPTION", (String) getAttribute("COLUMN_11_CAPTION").getObj());
                    rsPO.updateInt("COLUMN_12_ID", (int) getAttribute("COLUMN_12_ID").getVal());
                    rsPO.updateString("COLUMN_12_FORMULA", (String) getAttribute("COLUMN_12_FORMULA").getObj());
                    rsPO.updateDouble("COLUMN_12_PER", getAttribute("COLUMN_12_PER").getVal());
                    rsPO.updateDouble("COLUMN_12_AMT", getAttribute("COLUMN_12_AMT").getVal());
                    rsPO.updateString("COLUMN_12_CAPTION", (String) getAttribute("COLUMN_12_CAPTION").getObj());
                    rsPO.updateInt("COLUMN_13_ID", (int) getAttribute("COLUMN_13_ID").getVal());
                    rsPO.updateString("COLUMN_13_FORMULA", (String) getAttribute("COLUMN_13_FORMULA").getObj());
                    rsPO.updateDouble("COLUMN_13_PER", getAttribute("COLUMN_13_PER").getVal());
                    rsPO.updateDouble("COLUMN_13_AMT", getAttribute("COLUMN_13_AMT").getVal());
                    rsPO.updateString("COLUMN_13_CAPTION", (String) getAttribute("COLUMN_13_CAPTION").getObj());
                    rsPO.updateInt("COLUMN_14_ID", (int) getAttribute("COLUMN_14_ID").getVal());
                    rsPO.updateString("COLUMN_14_FORMULA", (String) getAttribute("COLUMN_14_FORMULA").getObj());
                    rsPO.updateDouble("COLUMN_14_PER", getAttribute("COLUMN_14_PER").getVal());
                    rsPO.updateDouble("COLUMN_14_AMT", getAttribute("COLUMN_14_AMT").getVal());
                    rsPO.updateString("COLUMN_14_CAPTION", (String) getAttribute("COLUMN_14_CAPTION").getObj());
                    rsPO.updateInt("COLUMN_15_ID", (int) getAttribute("COLUMN_15_ID").getVal());
                    rsPO.updateString("COLUMN_15_FORMULA", (String) getAttribute("COLUMN_15_FORMULA").getObj());
                    rsPO.updateDouble("COLUMN_15_PER", getAttribute("COLUMN_15_PER").getVal());
                    rsPO.updateDouble("COLUMN_15_AMT", getAttribute("COLUMN_15_AMT").getVal());
                    rsPO.updateString("COLUMN_15_CAPTION", (String) getAttribute("COLUMN_15_CAPTION").getObj());
                    
                    rsPO.updateInt("COLUMN_16_ID", (int) getAttribute("COLUMN_16_ID").getVal());
                    rsPO.updateString("COLUMN_16_FORMULA", (String) getAttribute("COLUMN_16_FORMULA").getObj());
                    rsPO.updateDouble("COLUMN_16_PER", getAttribute("COLUMN_16_PER").getVal());
                    rsPO.updateDouble("COLUMN_16_AMT", getAttribute("COLUMN_16_AMT").getVal());
                    rsPO.updateString("COLUMN_16_CAPTION", (String) getAttribute("COLUMN_16_CAPTION").getObj());
                    rsPO.updateInt("COLUMN_17_ID", (int) getAttribute("COLUMN_17_ID").getVal());
                    rsPO.updateString("COLUMN_17_FORMULA", (String) getAttribute("COLUMN_17_FORMULA").getObj());
                    rsPO.updateDouble("COLUMN_17_PER", getAttribute("COLUMN_17_PER").getVal());
                    rsPO.updateDouble("COLUMN_17_AMT", getAttribute("COLUMN_17_AMT").getVal());
                    rsPO.updateString("COLUMN_17_CAPTION", (String) getAttribute("COLUMN_17_CAPTION").getObj());
                    rsPO.updateInt("COLUMN_18_ID", (int) getAttribute("COLUMN_18_ID").getVal());
                    rsPO.updateString("COLUMN_18_FORMULA", (String) getAttribute("COLUMN_18_FORMULA").getObj());
                    rsPO.updateDouble("COLUMN_18_PER", getAttribute("COLUMN_18_PER").getVal());
                    rsPO.updateDouble("COLUMN_18_AMT", getAttribute("COLUMN_18_AMT").getVal());
                    rsPO.updateString("COLUMN_18_CAPTION", (String) getAttribute("COLUMN_18_CAPTION").getObj());
                    rsPO.updateInt("COLUMN_19_ID", (int) getAttribute("COLUMN_19_ID").getVal());
                    rsPO.updateString("COLUMN_19_FORMULA", (String) getAttribute("COLUMN_19_FORMULA").getObj());
                    rsPO.updateDouble("COLUMN_19_PER", getAttribute("COLUMN_19_PER").getVal());
                    rsPO.updateDouble("COLUMN_19_AMT", getAttribute("COLUMN_19_AMT").getVal());
                    rsPO.updateString("COLUMN_19_CAPTION", (String) getAttribute("COLUMN_19_CAPTION").getObj());
                    rsPO.updateInt("COLUMN_20_ID", (int) getAttribute("COLUMN_20_ID").getVal());
                    rsPO.updateString("COLUMN_20_FORMULA", (String) getAttribute("COLUMN_20_FORMULA").getObj());
                    rsPO.updateDouble("COLUMN_20_PER", getAttribute("COLUMN_20_PER").getVal());
                    rsPO.updateDouble("COLUMN_20_AMT", getAttribute("COLUMN_20_AMT").getVal());
                    rsPO.updateString("COLUMN_20_CAPTION", (String) getAttribute("COLUMN_20_CAPTION").getObj());

                    rsPO.updateInt("COLUMN_21_ID", (int) getAttribute("COLUMN_21_ID").getVal());
                    rsPO.updateString("COLUMN_21_FORMULA", (String) getAttribute("COLUMN_21_FORMULA").getObj());
                    rsPO.updateDouble("COLUMN_21_PER", getAttribute("COLUMN_21_PER").getVal());
                    rsPO.updateDouble("COLUMN_21_AMT", getAttribute("COLUMN_21_AMT").getVal());
                    rsPO.updateString("COLUMN_21_CAPTION", (String) getAttribute("COLUMN_21_CAPTION").getObj());
                    

                    rsPO.updateBoolean("IMPORT_CONCESS", getAttribute("IMPORT_CONCESS").getBool());
                    rsPO.updateString("PRINT_LINE_1", (String) getAttribute("PRINT_LINE_1").getObj());
                    rsPO.updateString("PRINT_LINE_2", (String) getAttribute("PRINT_LINE_2").getObj());
                    rsPO.updateString("IMPORT_LICENSE", (String) getAttribute("IMPORT_LICENSE").getObj());
                    rsPO.updateBoolean("CHANGED", true);
                    rsPO.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsPO.updateString("PAYMENT_TERM", (String) getAttribute("PAYMENT_TERM").getObj());

                    rsPO.updateInt("PAYMENT_CODE", getAttribute("PAYMENT_CODE").getInt());
                    rsPO.updateInt("CR_DAYS", getAttribute("CR_DAYS").getInt());
                    rsPO.updateInt("DEPT_ID", getAttribute("DEPT_ID").getInt());

                    rsPO.updateString("PRICE_BASIS_TERM", (String) getAttribute("PRICE_BASIS_TERM").getObj());
                    rsPO.updateString("DISCOUNT_TERM", (String) getAttribute("DISCOUNT_TERM").getObj());
                    rsPO.updateString("EXCISE_TERM", (String) getAttribute("EXCISE_TERM").getObj());
                    rsPO.updateString("ST_TERM", (String) getAttribute("ST_TERM").getObj());
                    rsPO.updateString("PF_TERM", (String) getAttribute("PF_TERM").getObj());
                    rsPO.updateString("FREIGHT_TERM", (String) getAttribute("FREIGHT_TERM").getObj());
                    rsPO.updateString("OCTROI_TERM", (String) getAttribute("OCTROI_TERM").getObj());
                    rsPO.updateString("FOB_TERM", (String) getAttribute("FOB_TERM").getObj());
                    rsPO.updateString("CIE_TERM", (String) getAttribute("CIE_TERM").getObj());
                    rsPO.updateString("INSURANCE_TERM", (String) getAttribute("INSURANCE_TERM").getObj());
                    rsPO.updateString("TCC_TERM", (String) getAttribute("TCC_TERM").getObj());
                    rsPO.updateString("CENVAT_TERM", (String) getAttribute("CENVAT_TERM").getObj());
                    rsPO.updateString("DESPATCH_TERM", (String) getAttribute("DESPATCH_TERM").getObj());
                    rsPO.updateString("SERVICE_TAX_TERM", (String) getAttribute("SERVICE_TAX_TERM").getObj());
                    rsPO.updateString("OTHERS_TERM", (String) getAttribute("OTHERS_TERM").getObj());
                    rsPO.updateString("COVERING_TEXT", (String) getAttribute("COVERING_TEXT").getObj());
                    
                    rsPO.updateString("CGST_TERM", (String) getAttribute("CGST_TERM").getObj());
                    rsPO.updateString("SGST_TERM", (String) getAttribute("SGST_TERM").getObj());
                    rsPO.updateString("IGST_TERM", (String) getAttribute("IGST_TERM").getObj());
                    rsPO.updateString("COMPOSITION_TERM", (String) getAttribute("COMPOSITION_TERM").getObj());
                    rsPO.updateString("RCM_TERM", (String) getAttribute("RCM_TERM").getObj());
                    rsPO.updateString("GST_COMPENSATION_CESS_TERM", (String) getAttribute("GST_COMPENSATION_CESS_TERM").getObj());

                    rsPO.updateString("PREMISES_TYPE", (String) getAttribute("PREMISES_TYPE").getObj());
                    rsPO.updateString("SCOPE", (String) getAttribute("SCOPE").getObj());
                    rsPO.updateString("SERVICE_PERIOD", (String) getAttribute("SERVICE_PERIOD").getObj());
                    rsPO.updateString("SERVICE_FREQUENCY", (String) getAttribute("SERVICE_FREQUENCY").getObj());
                    rsPO.updateString("CONTRACT_DETAILS", (String) getAttribute("CONTRACT_DETAILS").getObj());
                    rsPO.updateString("SERVICE_REPORT", (String) getAttribute("SERVICE_REPORT").getObj());
                    rsPO.updateString("ESI_TERMS", (String) getAttribute("ESI_TERMS").getObj());
                    rsPO.updateString("TERMINATION_TERMS", (String) getAttribute("TERMINATION_TERMS").getObj());
                    rsPO.updateString("FILE_TEXT", (String) getAttribute("FILE_TEXT").getObj());
                    rsPO.updateString("AMOUNT_IN_WORDS", (String) getAttribute("AMOUNT_IN_WORDS").getObj());
                    rsPO.updateBoolean("DIRECTOR_APPROVAL", getAttribute("DIRECTOR_APPROVAL").getBool());
                    rsPO.updateBoolean("IMPORTED", getAttribute("IMPORTED").getBool());

                    rsPO.insertRow();

                    //====== Now turn of P.O. Amendment Items ======
                    stPOLine = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    rsPOLine = stPOLine.executeQuery("SELECT * FROM D_PUR_PO_DETAIL WHERE PO_NO='1' ");
                    rsPOLine.first();

                    for (int i = 1; i <= colPOItems.size(); i++) {
                        clsPOAmendItem ObjItem = (clsPOAmendItem) colPOItems.get(Integer.toString(i));
                        rsPOLine.moveToInsertRow();
                        rsPOLine.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
                        rsPOLine.updateString("PO_NO", (String) getAttribute("PO_NO").getObj());
                        rsPOLine.updateInt("SR_NO", i);
                        rsPOLine.updateInt("PO_TYPE", POType);
                        rsPOLine.updateString("ITEM_ID", (String) ObjItem.getAttribute("ITEM_ID").getObj());
                        rsPOLine.updateString("VENDOR_SHADE", (String) ObjItem.getAttribute("VENDOR_SHADE").getObj());
                        rsPOLine.updateString("SDML_SHADE", (String) ObjItem.getAttribute("SDML_SHADE").getObj());
                        rsPOLine.updateString("ITEM_DESC", (String) ObjItem.getAttribute("ITEM_DESC").getObj());
                        rsPOLine.updateString("HSN_SAC_CODE", (String) ObjItem.getAttribute("HSN_SAC_CODE").getObj());
                        rsPOLine.updateString("MAKE", (String) ObjItem.getAttribute("MAKE").getObj());
                        rsPOLine.updateString("PRICE_LIST_NO", (String) ObjItem.getAttribute("PRICE_LIST_NO").getObj());
                        rsPOLine.updateString("PART_NO", (String) ObjItem.getAttribute("PART_NO").getObj());
                        rsPOLine.updateString("EXCISE_TARRIF_NO", (String) ObjItem.getAttribute("EXCISE_TARRIF_NO").getObj());
                        rsPOLine.updateDouble("QTY", ObjItem.getAttribute("QTY").getVal());
                        rsPOLine.updateDouble("TOLERANCE_LIMIT", ObjItem.getAttribute("TOLERANCE_LIMIT").getVal());
                        rsPOLine.updateDouble("PENDING_QTY", ObjItem.getAttribute("PENDING_QTY").getVal());
                        rsPOLine.updateDouble("RECD_QTY", ObjItem.getAttribute("RECD_QTY").getVal());
                        rsPOLine.updateInt("UNIT", (int) ObjItem.getAttribute("UNIT").getVal());
                        rsPOLine.updateInt("DEPT_ID", (int) ObjItem.getAttribute("DEPT_ID").getVal());
                        rsPOLine.updateDouble("RATE", ObjItem.getAttribute("RATE").getVal());
                        rsPOLine.updateDouble("LANDED_RATE", ObjItem.getAttribute("LANDED_RATE").getVal());
                        rsPOLine.updateDouble("DISC_PER", ObjItem.getAttribute("DISC_PER").getVal());
                        rsPOLine.updateDouble("DISC_AMT", ObjItem.getAttribute("DISC_AMT").getVal());
                        rsPOLine.updateDouble("TOTAL_AMOUNT", ObjItem.getAttribute("TOTAL_AMOUNT").getVal());
                        rsPOLine.updateDouble("NET_AMOUNT", ObjItem.getAttribute("NET_AMOUNT").getVal());
                        rsPOLine.updateString("INDENT_NO", (String) ObjItem.getAttribute("INDENT_NO").getObj());
                        rsPOLine.updateInt("INDENT_SR_NO", (int) ObjItem.getAttribute("INDENT_SR_NO").getVal());
                        rsPOLine.updateString("REFERENCE", (String) ObjItem.getAttribute("REFERENCE").getObj());
                        rsPOLine.updateString("DELIVERY_DATE", (String) ObjItem.getAttribute("DELIVERY_DATE").getObj());
                        rsPOLine.updateString("REMARKS", (String) ObjItem.getAttribute("REMARKS").getObj());
                        //rsPOLine.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
                        //rsPOLine.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                        rsPOLine.updateLong("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                        rsPOLine.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                        rsPOLine.updateInt("COLUMN_1_ID", (int) ObjItem.getAttribute("COLUMN_1_ID").getVal());
                        rsPOLine.updateString("COLUMN_1_FORMULA", (String) ObjItem.getAttribute("COLUMN_1_FORMULA").getObj());
                        rsPOLine.updateDouble("COLUMN_1_PER", ObjItem.getAttribute("COLUMN_1_PER").getVal());
                        rsPOLine.updateDouble("COLUMN_1_AMT", ObjItem.getAttribute("COLUMN_1_AMT").getVal());
                        rsPOLine.updateString("COLUMN_1_CAPTION", (String) ObjItem.getAttribute("COLUMN_1_CAPTION").getObj());
                        rsPOLine.updateInt("COLUMN_2_ID", (int) ObjItem.getAttribute("COLUMN_2_ID").getVal());
                        rsPOLine.updateString("COLUMN_2_FORMULA", (String) ObjItem.getAttribute("COLUMN_2_FORMULA").getObj());
                        rsPOLine.updateDouble("COLUMN_2_PER", ObjItem.getAttribute("COLUMN_2_PER").getVal());
                        rsPOLine.updateDouble("COLUMN_2_AMT", ObjItem.getAttribute("COLUMN_2_AMT").getVal());
                        rsPOLine.updateString("COLUMN_2_CAPTION", (String) ObjItem.getAttribute("COLUMN_2_CAPTION").getObj());
                        rsPOLine.updateInt("COLUMN_3_ID", (int) ObjItem.getAttribute("COLUMN_3_ID").getVal());
                        rsPOLine.updateString("COLUMN_3_FORMULA", (String) ObjItem.getAttribute("COLUMN_3_FORMULA").getObj());
                        rsPOLine.updateDouble("COLUMN_3_PER", ObjItem.getAttribute("COLUMN_3_PER").getVal());
                        rsPOLine.updateDouble("COLUMN_3_AMT", ObjItem.getAttribute("COLUMN_3_AMT").getVal());
                        rsPOLine.updateString("COLUMN_3_CAPTION", (String) ObjItem.getAttribute("COLUMN_3_CAPTION").getObj());
                        rsPOLine.updateInt("COLUMN_4_ID", (int) ObjItem.getAttribute("COLUMN_4_ID").getVal());
                        rsPOLine.updateString("COLUMN_4_FORMULA", (String) ObjItem.getAttribute("COLUMN_4_FORMULA").getObj());
                        rsPOLine.updateDouble("COLUMN_4_PER", ObjItem.getAttribute("COLUMN_4_PER").getVal());
                        rsPOLine.updateDouble("COLUMN_4_AMT", ObjItem.getAttribute("COLUMN_4_AMT").getVal());
                        rsPOLine.updateString("COLUMN_4_CAPTION", (String) ObjItem.getAttribute("COLUMN_4_CAPTION").getObj());
                        rsPOLine.updateInt("COLUMN_5_ID", (int) ObjItem.getAttribute("COLUMN_5_ID").getVal());
                        rsPOLine.updateString("COLUMN_5_FORMULA", (String) ObjItem.getAttribute("COLUMN_5_FORMULA").getObj());
                        rsPOLine.updateDouble("COLUMN_5_PER", ObjItem.getAttribute("COLUMN_5_PER").getVal());
                        rsPOLine.updateDouble("COLUMN_5_AMT", ObjItem.getAttribute("COLUMN_5_AMT").getVal());
                        rsPOLine.updateString("COLUMN_5_CAPTION", (String) ObjItem.getAttribute("COLUMN_5_CAPTION").getObj());
                        rsPOLine.updateInt("COLUMN_6_ID", (int) ObjItem.getAttribute("COLUMN_6_ID").getVal());
                        rsPOLine.updateString("COLUMN_6_FORMULA", (String) ObjItem.getAttribute("COLUMN_6_FORMULA").getObj());
                        rsPOLine.updateDouble("COLUMN_6_PER", ObjItem.getAttribute("COLUMN_6_PER").getVal());
                        rsPOLine.updateDouble("COLUMN_6_AMT", ObjItem.getAttribute("COLUMN_6_AMT").getVal());
                        rsPOLine.updateString("COLUMN_6_CAPTION", (String) ObjItem.getAttribute("COLUMN_6_CAPTION").getObj());
                        rsPOLine.updateInt("COLUMN_7_ID", (int) ObjItem.getAttribute("COLUMN_7_ID").getVal());
                        rsPOLine.updateString("COLUMN_7_FORMULA", (String) ObjItem.getAttribute("COLUMN_7_FORMULA").getObj());
                        rsPOLine.updateDouble("COLUMN_7_PER", ObjItem.getAttribute("COLUMN_7_PER").getVal());
                        rsPOLine.updateDouble("COLUMN_7_AMT", ObjItem.getAttribute("COLUMN_7_AMT").getVal());
                        rsPOLine.updateString("COLUMN_7_CAPTION", (String) ObjItem.getAttribute("COLUMN_7_CAPTION").getObj());
                        rsPOLine.updateInt("COLUMN_8_ID", (int) ObjItem.getAttribute("COLUMN_8_ID").getVal());
                        rsPOLine.updateString("COLUMN_8_FORMULA", (String) ObjItem.getAttribute("COLUMN_8_FORMULA").getObj());
                        rsPOLine.updateDouble("COLUMN_8_PER", ObjItem.getAttribute("COLUMN_8_PER").getVal());
                        rsPOLine.updateDouble("COLUMN_8_AMT", ObjItem.getAttribute("COLUMN_8_AMT").getVal());
                        rsPOLine.updateString("COLUMN_8_CAPTION", (String) ObjItem.getAttribute("COLUMN_8_CAPTION").getObj());
                        rsPOLine.updateInt("COLUMN_9_ID", (int) ObjItem.getAttribute("COLUMN_9_ID").getVal());
                        rsPOLine.updateString("COLUMN_9_FORMULA", (String) ObjItem.getAttribute("COLUMN_9_FORMULA").getObj());
                        rsPOLine.updateDouble("COLUMN_9_PER", ObjItem.getAttribute("COLUMN_9_PER").getVal());
                        rsPOLine.updateDouble("COLUMN_9_AMT", ObjItem.getAttribute("COLUMN_9_AMT").getVal());
                        rsPOLine.updateString("COLUMN_9_CAPTION", (String) ObjItem.getAttribute("COLUMN_9_CAPTION").getObj());
                        rsPOLine.updateInt("COLUMN_10_ID", (int) ObjItem.getAttribute("COLUMN_10_ID").getVal());
                        rsPOLine.updateString("COLUMN_10_FORMULA", (String) ObjItem.getAttribute("COLUMN_10_FORMULA").getObj());
                        rsPOLine.updateDouble("COLUMN_10_PER", ObjItem.getAttribute("COLUMN_10_PER").getVal());
                        rsPOLine.updateDouble("COLUMN_10_AMT", ObjItem.getAttribute("COLUMN_10_AMT").getVal());
                        rsPOLine.updateString("COLUMN_10_CAPTION", (String) ObjItem.getAttribute("COLUMN_10_CAPTION").getObj());

                        rsPOLine.updateInt("COLUMN_11_ID", (int) ObjItem.getAttribute("COLUMN_11_ID").getVal());
                        rsPOLine.updateString("COLUMN_11_FORMULA", (String) ObjItem.getAttribute("COLUMN_11_FORMULA").getObj());
                        rsPOLine.updateDouble("COLUMN_11_PER", ObjItem.getAttribute("COLUMN_11_PER").getVal());
                        rsPOLine.updateDouble("COLUMN_11_AMT", ObjItem.getAttribute("COLUMN_11_AMT").getVal());
                        rsPOLine.updateString("COLUMN_11_CAPTION", (String) ObjItem.getAttribute("COLUMN_11_CAPTION").getObj());
                        rsPOLine.updateInt("COLUMN_12_ID", (int) ObjItem.getAttribute("COLUMN_12_ID").getVal());
                        rsPOLine.updateString("COLUMN_12_FORMULA", (String) ObjItem.getAttribute("COLUMN_12_FORMULA").getObj());
                        rsPOLine.updateDouble("COLUMN_12_PER", ObjItem.getAttribute("COLUMN_12_PER").getVal());
                        rsPOLine.updateDouble("COLUMN_12_AMT", ObjItem.getAttribute("COLUMN_12_AMT").getVal());
                        rsPOLine.updateString("COLUMN_12_CAPTION", (String) ObjItem.getAttribute("COLUMN_12_CAPTION").getObj());
                        rsPOLine.updateInt("COLUMN_13_ID", (int) ObjItem.getAttribute("COLUMN_13_ID").getVal());
                        rsPOLine.updateString("COLUMN_13_FORMULA", (String) ObjItem.getAttribute("COLUMN_13_FORMULA").getObj());
                        rsPOLine.updateDouble("COLUMN_13_PER", ObjItem.getAttribute("COLUMN_13_PER").getVal());
                        rsPOLine.updateDouble("COLUMN_13_AMT", ObjItem.getAttribute("COLUMN_13_AMT").getVal());
                        rsPOLine.updateString("COLUMN_13_CAPTION", (String) ObjItem.getAttribute("COLUMN_13_CAPTION").getObj());
                        rsPOLine.updateInt("COLUMN_14_ID", (int) ObjItem.getAttribute("COLUMN_14_ID").getVal());
                        rsPOLine.updateString("COLUMN_14_FORMULA", (String) ObjItem.getAttribute("COLUMN_14_FORMULA").getObj());
                        rsPOLine.updateDouble("COLUMN_14_PER", ObjItem.getAttribute("COLUMN_14_PER").getVal());
                        rsPOLine.updateDouble("COLUMN_14_AMT", ObjItem.getAttribute("COLUMN_14_AMT").getVal());
                        rsPOLine.updateString("COLUMN_14_CAPTION", (String) ObjItem.getAttribute("COLUMN_14_CAPTION").getObj());
                        rsPOLine.updateInt("COLUMN_15_ID", (int) ObjItem.getAttribute("COLUMN_15_ID").getVal());
                        rsPOLine.updateString("COLUMN_15_FORMULA", (String) ObjItem.getAttribute("COLUMN_15_FORMULA").getObj());
                        rsPOLine.updateDouble("COLUMN_15_PER", ObjItem.getAttribute("COLUMN_15_PER").getVal());
                        rsPOLine.updateDouble("COLUMN_15_AMT", ObjItem.getAttribute("COLUMN_15_AMT").getVal());
                        rsPOLine.updateString("COLUMN_15_CAPTION", (String) ObjItem.getAttribute("COLUMN_15_CAPTION").getObj());
                        
                        rsPOLine.updateInt("COLUMN_16_ID", (int) ObjItem.getAttribute("COLUMN_16_ID").getVal());
                        rsPOLine.updateString("COLUMN_16_FORMULA", (String) ObjItem.getAttribute("COLUMN_16_FORMULA").getObj());
                        rsPOLine.updateDouble("COLUMN_16_PER", ObjItem.getAttribute("COLUMN_16_PER").getVal());
                        rsPOLine.updateDouble("COLUMN_16_AMT", ObjItem.getAttribute("COLUMN_16_AMT").getVal());
                        rsPOLine.updateString("COLUMN_16_CAPTION", (String) ObjItem.getAttribute("COLUMN_16_CAPTION").getObj());
                        rsPOLine.updateInt("COLUMN_17_ID", (int) ObjItem.getAttribute("COLUMN_17_ID").getVal());
                        rsPOLine.updateString("COLUMN_17_FORMULA", (String) ObjItem.getAttribute("COLUMN_17_FORMULA").getObj());
                        rsPOLine.updateDouble("COLUMN_17_PER", ObjItem.getAttribute("COLUMN_17_PER").getVal());
                        rsPOLine.updateDouble("COLUMN_17_AMT", ObjItem.getAttribute("COLUMN_17_AMT").getVal());
                        rsPOLine.updateString("COLUMN_17_CAPTION", (String) ObjItem.getAttribute("COLUMN_17_CAPTION").getObj());
                        rsPOLine.updateInt("COLUMN_18_ID", (int) ObjItem.getAttribute("COLUMN_18_ID").getVal());
                        rsPOLine.updateString("COLUMN_18_FORMULA", (String) ObjItem.getAttribute("COLUMN_18_FORMULA").getObj());
                        rsPOLine.updateDouble("COLUMN_18_PER", ObjItem.getAttribute("COLUMN_18_PER").getVal());
                        rsPOLine.updateDouble("COLUMN_18_AMT", ObjItem.getAttribute("COLUMN_18_AMT").getVal());
                        rsPOLine.updateString("COLUMN_18_CAPTION", (String) ObjItem.getAttribute("COLUMN_18_CAPTION").getObj());
                        rsPOLine.updateInt("COLUMN_19_ID", (int) ObjItem.getAttribute("COLUMN_19_ID").getVal());
                        rsPOLine.updateString("COLUMN_19_FORMULA", (String) ObjItem.getAttribute("COLUMN_19_FORMULA").getObj());
                        rsPOLine.updateDouble("COLUMN_19_PER", ObjItem.getAttribute("COLUMN_19_PER").getVal());
                        rsPOLine.updateDouble("COLUMN_19_AMT", ObjItem.getAttribute("COLUMN_19_AMT").getVal());
                        rsPOLine.updateString("COLUMN_19_CAPTION", (String) ObjItem.getAttribute("COLUMN_19_CAPTION").getObj());
                        rsPOLine.updateInt("COLUMN_20_ID", (int) ObjItem.getAttribute("COLUMN_20_ID").getVal());
                        rsPOLine.updateString("COLUMN_20_FORMULA", (String) ObjItem.getAttribute("COLUMN_20_FORMULA").getObj());
                        rsPOLine.updateDouble("COLUMN_20_PER", ObjItem.getAttribute("COLUMN_20_PER").getVal());
                        rsPOLine.updateDouble("COLUMN_20_AMT", ObjItem.getAttribute("COLUMN_20_AMT").getVal());
                        rsPOLine.updateString("COLUMN_20_CAPTION", (String) ObjItem.getAttribute("COLUMN_20_CAPTION").getObj());

                        rsPOLine.updateInt("COLUMN_21_ID", (int) ObjItem.getAttribute("COLUMN_21_ID").getVal());
                        rsPOLine.updateString("COLUMN_21_FORMULA", (String) ObjItem.getAttribute("COLUMN_21_FORMULA").getObj());
                        rsPOLine.updateDouble("COLUMN_21_PER", ObjItem.getAttribute("COLUMN_21_PER").getVal());
                        rsPOLine.updateDouble("COLUMN_21_AMT", ObjItem.getAttribute("COLUMN_21_AMT").getVal());
                        rsPOLine.updateString("COLUMN_21_CAPTION", (String) ObjItem.getAttribute("COLUMN_21_CAPTION").getObj());
                        

                        rsPOLine.updateBoolean("EXCISE_GATEPASS_GIVEN", ObjItem.getAttribute("EXCISE_GATEPASS_GIVEN").getBool());
                        rsPOLine.updateBoolean("IMPORT_CONCESS", ObjItem.getAttribute("IMPORT_CONCESS").getBool());
                        rsPOLine.updateString("QUOT_ID", (String) ObjItem.getAttribute("QUOT_ID").getObj());
                        rsPOLine.updateInt("QUOT_SR_NO", (int) ObjItem.getAttribute("QUOT_SR_NO").getVal());
                        rsPOLine.updateBoolean("CHANGED", true);
                        rsPOLine.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                        rsPOLine.insertRow();
                    }

                    //====== Now turn of P.O. Terms ======
                    stTerms = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    rsTerms = stTerms.executeQuery("SELECT * FROM D_PUR_PO_TERMS WHERE PO_NO='1' ");
                    rsTerms.first();

                    for (int i = 1; i <= colPOTerms.size(); i++) {
                        clsPOTerms ObjItem = (clsPOTerms) colPOTerms.get(Integer.toString(i));
                        rsTerms.moveToInsertRow();
                        rsTerms.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
                        rsTerms.updateString("PO_NO", (String) getAttribute("PO_NO").getObj());
                        rsTerms.updateInt("SR_NO", i);
                        rsTerms.updateInt("PO_TYPE", POType);
                        rsTerms.updateString("TERM_TYPE", (String) ObjItem.getAttribute("TERM_TYPE").getObj());
                        rsTerms.updateInt("TERM_CODE", (int) ObjItem.getAttribute("TERM_CODE").getVal());
                        rsTerms.updateString("TERM_DESC", (String) ObjItem.getAttribute("TERM_DESC").getObj());
                        rsTerms.updateBoolean("CHANGED", true);
                        rsTerms.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                        rsTerms.insertRow();
                    }

                    // ---------  Now copy PO Object to current object ------------------//
                    setAttribute("COMPANY_ID", (int) ObjPO.getAttribute("COMPANY_ID").getVal());
                    setAttribute("PO_NO", (String) ObjPO.getAttribute("PO_NO").getObj());
                    setAttribute("DOC_ID", (long) ObjPO.getAttribute("DOC_ID").getVal());
                    setAttribute("PO_DATE", (String) ObjPO.getAttribute("PO_DATE").getObj());
                    setAttribute("PO_TYPE", POType);
                    setAttribute("PO_REF", (String) ObjPO.getAttribute("PO_REF").getObj());
                    setAttribute("SUPP_ID", (String) ObjPO.getAttribute("SUPP_ID").getObj());
                    setAttribute("SUPP_NAME", (String) ObjPO.getAttribute("SUPP_NAME").getObj());
                    setAttribute("REF_A", (String) ObjPO.getAttribute("REF_A").getObj());
                    setAttribute("REF_B", (String) ObjPO.getAttribute("REF_B").getObj());
                    setAttribute("QUOTATION_NO", (String) ObjPO.getAttribute("QUOTATION_NO").getObj());
                    setAttribute("QUOTATION_DATE", (String) ObjPO.getAttribute("QUOTATION_DATE").getObj());
                    setAttribute("INQUIRY_NO", (String) ObjPO.getAttribute("INQUIRY_NO").getObj());
                    setAttribute("INQUIRY_DATE", (String) ObjPO.getAttribute("INQUIRY_DATE").getObj());
                    setAttribute("BUYER", (int) ObjPO.getAttribute("BUYER").getVal());
                    setAttribute("TRANSPORT_MODE", (int) ObjPO.getAttribute("TRANSPORT_MODE").getVal());
                    setAttribute("PURPOSE", (String) ObjPO.getAttribute("PURPOSE").getObj());
                    setAttribute("SUBJECT", (String) ObjPO.getAttribute("SUBJECT").getObj());
                    setAttribute("CURRENCY_ID", (int) ObjPO.getAttribute("CURRENCY_ID").getVal());
                    setAttribute("CURRENCY_RATE", ObjPO.getAttribute("CURRENCY_RATE").getVal());
                    setAttribute("TOTAL_AMOUNT", ObjPO.getAttribute("TOTAL_AMOUNT").getVal());
                    setAttribute("NET_AMOUNT", ObjPO.getAttribute("NET_AMOUNT").getVal());
                    setAttribute("STATUS", (String) ObjPO.getAttribute("STATUS").getObj());
                    setAttribute("ATTACHEMENT", ObjPO.getAttribute("ATTACHEMENT").getBool());
                    setAttribute("ATTACHEMENT_PATH", (String) ObjPO.getAttribute("ATTACHEMENT_PATH").getObj());
                    setAttribute("REMARKS", (String) ObjPO.getAttribute("REMARKS").getObj());
                    setAttribute("SHIP_ID", (int) ObjPO.getAttribute("SHIP_ID").getVal());
                    setAttribute("CANCELLED", ObjPO.getAttribute("CANCELLED").getBool());
                    //setAttribute("HIERARCHY_ID",(int)ObjPO.getAttribute("HIERARCHY_ID").getVal());
                    //setAttribute("CREATED_BY",(int) ObjPO.getAttribute("CREATED_BY").getVal());
                    //setAttribute("CREATED_DATE",(String)ObjPO.getAttribute("CREATED_DATE").getObj());
                    setAttribute("MODIFIED_BY", (int) ObjPO.getAttribute("MODIFIED_BY").getVal());
                    setAttribute("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    //Now Custom Columns
                    setAttribute("COLUMN_1_ID", (int) ObjPO.getAttribute("COLUMN_1_ID").getVal());
                    setAttribute("COLUMN_1_FORMULA", (String) ObjPO.getAttribute("COLUMN_1_FORMULA").getObj());
                    setAttribute("COLUMN_1_PER", ObjPO.getAttribute("COLUMN_1_PER").getVal());
                    setAttribute("COLUMN_1_AMT", ObjPO.getAttribute("COLUMN_1_AMT").getVal());
                    setAttribute("COLUMN_1_CAPTION", (String) ObjPO.getAttribute("COLUMN_1_CAPTION").getObj());
                    setAttribute("COLUMN_2_ID", (int) ObjPO.getAttribute("COLUMN_2_ID").getVal());
                    setAttribute("COLUMN_2_FORMULA", (String) ObjPO.getAttribute("COLUMN_2_FORMULA").getObj());
                    setAttribute("COLUMN_2_PER", ObjPO.getAttribute("COLUMN_2_PER").getVal());
                    setAttribute("COLUMN_2_AMT", ObjPO.getAttribute("COLUMN_2_AMT").getVal());
                    setAttribute("COLUMN_2_CAPTION", (String) ObjPO.getAttribute("COLUMN_2_CAPTION").getObj());
                    setAttribute("COLUMN_3_ID", (int) ObjPO.getAttribute("COLUMN_3_ID").getVal());
                    setAttribute("COLUMN_3_FORMULA", (String) ObjPO.getAttribute("COLUMN_3_FORMULA").getObj());
                    setAttribute("COLUMN_3_PER", ObjPO.getAttribute("COLUMN_3_PER").getVal());
                    setAttribute("COLUMN_3_AMT", ObjPO.getAttribute("COLUMN_3_AMT").getVal());
                    setAttribute("COLUMN_3_CAPTION", (String) ObjPO.getAttribute("COLUMN_3_CAPTION").getObj());
                    setAttribute("COLUMN_4_ID", (int) ObjPO.getAttribute("COLUMN_4_ID").getVal());
                    setAttribute("COLUMN_4_FORMULA", (String) ObjPO.getAttribute("COLUMN_4_FORMULA").getObj());
                    setAttribute("COLUMN_4_PER", ObjPO.getAttribute("COLUMN_4_PER").getVal());
                    setAttribute("COLUMN_4_AMT", ObjPO.getAttribute("COLUMN_4_AMT").getVal());
                    setAttribute("COLUMN_4_CAPTION", (String) ObjPO.getAttribute("COLUMN_4_CAPTION").getObj());
                    setAttribute("COLUMN_5_ID", (int) ObjPO.getAttribute("COLUMN_5_ID").getVal());
                    setAttribute("COLUMN_5_FORMULA", (String) ObjPO.getAttribute("COLUMN_5_FORMULA").getObj());
                    setAttribute("COLUMN_5_PER", ObjPO.getAttribute("COLUMN_5_PER").getVal());
                    setAttribute("COLUMN_5_AMT", ObjPO.getAttribute("COLUMN_5_AMT").getVal());
                    setAttribute("COLUMN_5_CAPTION", (String) ObjPO.getAttribute("COLUMN_5_CAPTION").getObj());
                    setAttribute("COLUMN_6_ID", (int) ObjPO.getAttribute("COLUMN_6_ID").getVal());
                    setAttribute("COLUMN_6_FORMULA", (String) ObjPO.getAttribute("COLUMN_6_FORMULA").getObj());
                    setAttribute("COLUMN_6_PER", ObjPO.getAttribute("COLUMN_6_PER").getVal());
                    setAttribute("COLUMN_6_AMT", ObjPO.getAttribute("COLUMN_6_AMT").getVal());
                    setAttribute("COLUMN_6_CAPTION", (String) ObjPO.getAttribute("COLUMN_6_CAPTION").getObj());
                    setAttribute("COLUMN_7_ID", (int) ObjPO.getAttribute("COLUMN_7_ID").getVal());
                    setAttribute("COLUMN_7_FORMULA", (String) ObjPO.getAttribute("COLUMN_7_FORMULA").getObj());
                    setAttribute("COLUMN_7_PER", ObjPO.getAttribute("COLUMN_7_PER").getVal());
                    setAttribute("COLUMN_7_AMT", ObjPO.getAttribute("COLUMN_7_AMT").getVal());
                    setAttribute("COLUMN_7_CAPTION", (String) ObjPO.getAttribute("COLUMN_7_CAPTION").getObj());
                    setAttribute("COLUMN_8_ID", (int) ObjPO.getAttribute("COLUMN_8_ID").getVal());
                    setAttribute("COLUMN_8_FORMULA", (String) ObjPO.getAttribute("COLUMN_8_FORMULA").getObj());
                    setAttribute("COLUMN_8_PER", ObjPO.getAttribute("COLUMN_8_PER").getVal());
                    setAttribute("COLUMN_8_AMT", ObjPO.getAttribute("COLUMN_8_AMT").getVal());
                    setAttribute("COLUMN_8_CAPTION", (String) ObjPO.getAttribute("COLUMN_8_CAPTION").getObj());
                    setAttribute("COLUMN_9_ID", (int) ObjPO.getAttribute("COLUMN_9_ID").getVal());
                    setAttribute("COLUMN_9_FORMULA", (String) ObjPO.getAttribute("COLUMN_9_FORMULA").getObj());
                    setAttribute("COLUMN_9_PER", ObjPO.getAttribute("COLUMN_9_PER").getVal());
                    setAttribute("COLUMN_9_AMT", ObjPO.getAttribute("COLUMN_9_AMT").getVal());
                    setAttribute("COLUMN_9_CAPTION", (String) ObjPO.getAttribute("COLUMN_9_CAPTION").getObj());
                    setAttribute("COLUMN_10_ID", (int) ObjPO.getAttribute("COLUMN_10_ID").getVal());
                    setAttribute("COLUMN_10_FORMULA", (String) ObjPO.getAttribute("COLUMN_10_FORMULA").getObj());
                    setAttribute("COLUMN_10_PER", ObjPO.getAttribute("COLUMN_10_PER").getVal());
                    setAttribute("COLUMN_10_AMT", ObjPO.getAttribute("COLUMN_10_AMT").getVal());
                    setAttribute("COLUMN_10_CAPTION", (String) ObjPO.getAttribute("COLUMN_10_CAPTION").getObj());
                    setAttribute("COLUMN_11_ID", (int) ObjPO.getAttribute("COLUMN_11_ID").getVal());
                    setAttribute("COLUMN_11_FORMULA", (String) ObjPO.getAttribute("COLUMN_11_FORMULA").getObj());
                    setAttribute("COLUMN_11_PER", ObjPO.getAttribute("COLUMN_11_PER").getVal());
                    setAttribute("COLUMN_11_AMT", ObjPO.getAttribute("COLUMN_11_AMT").getVal());
                    setAttribute("COLUMN_11_CAPTION", (String) ObjPO.getAttribute("COLUMN_11_CAPTION").getObj());
                    setAttribute("COLUMN_12_ID", (int) ObjPO.getAttribute("COLUMN_12_ID").getVal());
                    setAttribute("COLUMN_12_FORMULA", (String) ObjPO.getAttribute("COLUMN_12_FORMULA").getObj());
                    setAttribute("COLUMN_12_PER", ObjPO.getAttribute("COLUMN_12_PER").getVal());
                    setAttribute("COLUMN_12_AMT", ObjPO.getAttribute("COLUMN_12_AMT").getVal());
                    setAttribute("COLUMN_12_CAPTION", (String) ObjPO.getAttribute("COLUMN_12_CAPTION").getObj());
                    setAttribute("COLUMN_13_ID", (int) ObjPO.getAttribute("COLUMN_13_ID").getVal());
                    setAttribute("COLUMN_13_FORMULA", (String) ObjPO.getAttribute("COLUMN_13_FORMULA").getObj());
                    setAttribute("COLUMN_13_PER", ObjPO.getAttribute("COLUMN_13_PER").getVal());
                    setAttribute("COLUMN_13_AMT", ObjPO.getAttribute("COLUMN_13_AMT").getVal());
                    setAttribute("COLUMN_13_CAPTION", (String) ObjPO.getAttribute("COLUMN_13_CAPTION").getObj());
                    setAttribute("COLUMN_14_ID", (int) ObjPO.getAttribute("COLUMN_14_ID").getVal());
                    setAttribute("COLUMN_14_FORMULA", (String) ObjPO.getAttribute("COLUMN_14_FORMULA").getObj());
                    setAttribute("COLUMN_14_PER", ObjPO.getAttribute("COLUMN_14_PER").getVal());
                    setAttribute("COLUMN_14_AMT", ObjPO.getAttribute("COLUMN_14_AMT").getVal());
                    setAttribute("COLUMN_14_CAPTION", (String) ObjPO.getAttribute("COLUMN_14_CAPTION").getObj());
                    setAttribute("COLUMN_15_ID", (int) ObjPO.getAttribute("COLUMN_15_ID").getVal());
                    setAttribute("COLUMN_15_FORMULA", (String) ObjPO.getAttribute("COLUMN_15_FORMULA").getObj());
                    setAttribute("COLUMN_15_PER", ObjPO.getAttribute("COLUMN_15_PER").getVal());
                    setAttribute("COLUMN_15_AMT", ObjPO.getAttribute("COLUMN_15_AMT").getVal());
                    setAttribute("COLUMN_15_CAPTION", (String) ObjPO.getAttribute("COLUMN_15_CAPTION").getObj());
                    
                    setAttribute("COLUMN_16_ID", (int) ObjPO.getAttribute("COLUMN_16_ID").getVal());
                    setAttribute("COLUMN_16_FORMULA", (String) ObjPO.getAttribute("COLUMN_16_FORMULA").getObj());
                    setAttribute("COLUMN_16_PER", ObjPO.getAttribute("COLUMN_16_PER").getVal());
                    setAttribute("COLUMN_16_AMT", ObjPO.getAttribute("COLUMN_16_AMT").getVal());
                    setAttribute("COLUMN_16_CAPTION", (String) ObjPO.getAttribute("COLUMN_16_CAPTION").getObj());
                    setAttribute("COLUMN_17_ID", (int) ObjPO.getAttribute("COLUMN_17_ID").getVal());
                    setAttribute("COLUMN_17_FORMULA", (String) ObjPO.getAttribute("COLUMN_17_FORMULA").getObj());
                    setAttribute("COLUMN_17_PER", ObjPO.getAttribute("COLUMN_17_PER").getVal());
                    setAttribute("COLUMN_17_AMT", ObjPO.getAttribute("COLUMN_17_AMT").getVal());
                    setAttribute("COLUMN_17_CAPTION", (String) ObjPO.getAttribute("COLUMN_17_CAPTION").getObj());
                    setAttribute("COLUMN_18_ID", (int) ObjPO.getAttribute("COLUMN_18_ID").getVal());
                    setAttribute("COLUMN_18_FORMULA", (String) ObjPO.getAttribute("COLUMN_18_FORMULA").getObj());
                    setAttribute("COLUMN_18_PER", ObjPO.getAttribute("COLUMN_18_PER").getVal());
                    setAttribute("COLUMN_18_AMT", ObjPO.getAttribute("COLUMN_18_AMT").getVal());
                    setAttribute("COLUMN_18_CAPTION", (String) ObjPO.getAttribute("COLUMN_18_CAPTION").getObj());
                    setAttribute("COLUMN_19_ID", (int) ObjPO.getAttribute("COLUMN_19_ID").getVal());
                    setAttribute("COLUMN_19_FORMULA", (String) ObjPO.getAttribute("COLUMN_19_FORMULA").getObj());
                    setAttribute("COLUMN_19_PER", ObjPO.getAttribute("COLUMN_19_PER").getVal());
                    setAttribute("COLUMN_19_AMT", ObjPO.getAttribute("COLUMN_19_AMT").getVal());
                    setAttribute("COLUMN_19_CAPTION", (String) ObjPO.getAttribute("COLUMN_19_CAPTION").getObj());
                    setAttribute("COLUMN_20_ID", (int) ObjPO.getAttribute("COLUMN_20_ID").getVal());
                    setAttribute("COLUMN_20_FORMULA", (String) ObjPO.getAttribute("COLUMN_20_FORMULA").getObj());
                    setAttribute("COLUMN_20_PER", ObjPO.getAttribute("COLUMN_20_PER").getVal());
                    setAttribute("COLUMN_20_AMT", ObjPO.getAttribute("COLUMN_20_AMT").getVal());
                    setAttribute("COLUMN_20_CAPTION", (String) ObjPO.getAttribute("COLUMN_20_CAPTION").getObj());
                    setAttribute("COLUMN_21_ID", (int) ObjPO.getAttribute("COLUMN_21_ID").getVal());
                    setAttribute("COLUMN_21_FORMULA", (String) ObjPO.getAttribute("COLUMN_21_FORMULA").getObj());
                    setAttribute("COLUMN_21_PER", ObjPO.getAttribute("COLUMN_21_PER").getVal());
                    setAttribute("COLUMN_21_AMT", ObjPO.getAttribute("COLUMN_21_AMT").getVal());
                    setAttribute("COLUMN_21_CAPTION", (String) ObjPO.getAttribute("COLUMN_21_CAPTION").getObj());
                    

                    setAttribute("IMPORT_CONCESS", ObjPO.getAttribute("IMPORT_CONCESS").getBool());
                    setAttribute("PRINT_LINE_1", (String) ObjPO.getAttribute("PRINT_LINE_1").getObj());
                    setAttribute("PRINT_LINE_2", (String) ObjPO.getAttribute("PRINT_LINE_2").getObj());
                    setAttribute("IMPORT_LICENSE", (String) ObjPO.getAttribute("IMPORT_LICENSE").getObj());
                    setAttribute("PAYMENT_TERM", (String) ObjPO.getAttribute("PAYMENT_TERM").getObj());

                    setAttribute("PAYMENT_CODE", ObjPO.getAttribute("PAYMENT_CODE").getInt());
                    setAttribute("CR_DAYS", ObjPO.getAttribute("CR_DAYS").getInt());
                    setAttribute("DEPT_ID", ObjPO.getAttribute("DEPT_ID").getInt());

                    setAttribute("PRICE_BASIS_TERM", (String) ObjPO.getAttribute("PRICE_BASIS_TERM").getObj());
                    setAttribute("DISCOUNT_TERM", (String) ObjPO.getAttribute("DISCOUNT_TERM").getObj());
                    setAttribute("EXCISE_TERM", (String) ObjPO.getAttribute("EXCISE_TERM").getObj());
                    setAttribute("ST_TERM", (String) ObjPO.getAttribute("ST_TERM").getObj());
                    setAttribute("PF_TERM", (String) ObjPO.getAttribute("PF_TERM").getObj());
                    setAttribute("FREIGHT_TERM", (String) ObjPO.getAttribute("FREIGHT_TERM").getObj());
                    setAttribute("OCTROI_TERM", (String) ObjPO.getAttribute("OCTROI_TERM").getObj());
                    setAttribute("FOB_TERM", (String) ObjPO.getAttribute("FOB_TERM").getObj());
                    setAttribute("CIE_TERM", (String) ObjPO.getAttribute("CIE_TERM").getObj());
                    setAttribute("INSURANCE_TERM", (String) ObjPO.getAttribute("INSURANCE_TERM").getObj());
                    setAttribute("TCC_TERM", (String) ObjPO.getAttribute("TCC_TERM").getObj());
                    setAttribute("CENVAT_TERM", (String) ObjPO.getAttribute("CENVAT_TERM").getObj());
                    setAttribute("DESPATCH_TERM", (String) ObjPO.getAttribute("DESPATCH_TERM").getObj());
                    setAttribute("SERVICE_TAX_TERM", (String) ObjPO.getAttribute("SERVICE_TAX_TERM").getObj());
                    setAttribute("OTHERS_TERM", (String) ObjPO.getAttribute("OTHERS_TERM").getObj());
                    setAttribute("COVERING_TEXT", (String) ObjPO.getAttribute("COVERING_TEXT").getObj());
                    
                    setAttribute("CGST_TERM", (String) ObjPO.getAttribute("CGST_TERM").getObj());
                    setAttribute("SGST_TERM", (String) ObjPO.getAttribute("SGST_TERM").getObj());
                    setAttribute("IGST_TERM", (String) ObjPO.getAttribute("IGST_TERM").getObj());
                    setAttribute("COMPOSITION_TERM", (String) ObjPO.getAttribute("COMPOSITION_TERM").getObj());
                    setAttribute("RCM_TERM", (String) ObjPO.getAttribute("RCM_TERM").getObj());
                    setAttribute("GST_COMPENSATION_CESS_TERM", (String) ObjPO.getAttribute("GST_COMPENSATION_CESS_TERM").getObj());

                    setAttribute("PREMISES_TYPE", (String) ObjPO.getAttribute("PREMISES_TYPE").getObj());
                    setAttribute("SCOPE", (String) ObjPO.getAttribute("SCOPE").getObj());
                    setAttribute("SERVICE_PERIOD", (String) ObjPO.getAttribute("SERVICE_PERIOD").getObj());
                    setAttribute("SERVICE_FREQUENCY", (String) ObjPO.getAttribute("SERVICE_FREQUENCY").getObj());
                    setAttribute("CONTRACT_DETAILS", (String) ObjPO.getAttribute("CONTRACT_DETAILS").getObj());
                    setAttribute("SERVICE_REPORT", (String) ObjPO.getAttribute("SERVICE_REPORT").getObj());
                    setAttribute("ESI_TERMS", (String) ObjPO.getAttribute("ESI_TERMS").getObj());
                    setAttribute("TERMINATION_TERMS", (String) ObjPO.getAttribute("TERMINATION_TERMS").getObj());
                    setAttribute("FILE_TEXT", (String) ObjPO.getAttribute("FILE_TEXT").getObj());
                    setAttribute("AMOUNT_IN_WORDS", (String) ObjPO.getAttribute("AMOUNT_IN_WORDS").getObj());
                    setAttribute("DIRECTOR_APPROVAL", ObjPO.getAttribute("DIRECTOR_APPROVAL").getBool());
                    setAttribute("IMPORTED", ObjPO.getAttribute("IMPORTED").getBool());

                    //Clear the collection of this class
                    //colPOItems.clear(); // comments on 09-10-2009 -komal bcz item is not fill up in collection
                    if (ObjPO.colPOItems.size() > 0) { // add condition on 09-10-2009
                        colPOItems.clear();
                    }

                    for (int i = 1; i <= ObjPO.colPOItems.size(); i++) {
                        clsPOItem ObjItem = (clsPOItem) ObjPO.colPOItems.get(Integer.toString(i));

                        clsPOAmendItem NewItem = new clsPOAmendItem();

                        NewItem.setAttribute("COMPANY_ID", (int) ObjPO.getAttribute("COMPANY_ID").getVal());
                        NewItem.setAttribute("PO_NO", (String) ObjPO.getAttribute("PO_NO").getObj());
                        NewItem.setAttribute("SR_NO", i);
                        NewItem.setAttribute("PO_TYPE", POType);
                        NewItem.setAttribute("ITEM_ID", (String) ObjItem.getAttribute("ITEM_ID").getObj());
                        NewItem.setAttribute("VENDOR_SHADE", (String) ObjItem.getAttribute("VENDOR_SHADE").getObj());
                        NewItem.setAttribute("SDML_SHADE", (String) ObjItem.getAttribute("SDML_SHADE").getObj());
                        NewItem.setAttribute("ITEM_DESC", (String) ObjItem.getAttribute("ITEM_DESC").getObj());
                        NewItem.setAttribute("HSN_SAC_CODE", (String) ObjItem.getAttribute("HSN_SAC_CODE").getObj());
                        NewItem.setAttribute("MAKE", (String) ObjItem.getAttribute("MAKE").getObj());
                        NewItem.setAttribute("PRICE_LIST_NO", (String) ObjItem.getAttribute("PRICE_LIST_NO").getObj());
                        NewItem.setAttribute("PART_NO", (String) ObjItem.getAttribute("PART_NO").getObj());
                        NewItem.setAttribute("EXCISE_TARRIF_NO", (String) ObjItem.getAttribute("EXCISE_TARRIF_NO").getObj());
                        NewItem.setAttribute("QTY", ObjItem.getAttribute("QTY").getVal());
                        NewItem.setAttribute("TOLERANCE_LIMIT", ObjItem.getAttribute("TOLERANCE_LIMIT").getVal());
                        NewItem.setAttribute("PENDING_QTY", ObjItem.getAttribute("PENDING_QTY").getVal());
                        NewItem.setAttribute("RECD_QTY", ObjItem.getAttribute("RECD_QTY").getVal());
                        NewItem.setAttribute("UNIT", (int) ObjItem.getAttribute("UNIT").getVal());

                        NewItem.setAttribute("DEPT_ID", (int) ObjItem.getAttribute("DEPT_ID").getVal());
                        NewItem.setAttribute("RATE", ObjItem.getAttribute("RATE").getVal());
                        NewItem.setAttribute("LANDED_RATE", ObjItem.getAttribute("LANDED_RATE").getVal());
                        NewItem.setAttribute("DISC_PER", ObjItem.getAttribute("DISC_PER").getVal());
                        NewItem.setAttribute("DISC_AMT", ObjItem.getAttribute("DISC_AMT").getVal());
                        NewItem.setAttribute("TOTAL_AMOUNT", ObjItem.getAttribute("TOTAL_AMOUNT").getVal());
                        NewItem.setAttribute("NET_AMOUNT", ObjItem.getAttribute("NET_AMOUNT").getVal());
                        NewItem.setAttribute("INDENT_NO", (String) ObjItem.getAttribute("INDENT_NO").getObj());
                        NewItem.setAttribute("INDENT_SR_NO", (int) ObjItem.getAttribute("INDENT_SR_NO").getVal());
                        NewItem.setAttribute("REFERENCE", (String) ObjItem.getAttribute("REFERENCE").getObj());
                        NewItem.setAttribute("DELIVERY_DATE", (String) ObjItem.getAttribute("DELIVERY_DATE").getObj());
                        NewItem.setAttribute("REMARKS", (String) ObjItem.getAttribute("REMARKS").getObj());
                        //NewItem.setAttribute("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
                        //NewItem.setAttribute("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                        NewItem.setAttribute("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                        NewItem.setAttribute("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                        NewItem.setAttribute("COLUMN_1_ID", (int) ObjItem.getAttribute("COLUMN_1_ID").getVal());
                        NewItem.setAttribute("COLUMN_1_FORMULA", (String) ObjItem.getAttribute("COLUMN_1_FORMULA").getObj());
                        NewItem.setAttribute("COLUMN_1_PER", ObjItem.getAttribute("COLUMN_1_PER").getVal());
                        NewItem.setAttribute("COLUMN_1_AMT", ObjItem.getAttribute("COLUMN_1_AMT").getVal());
                        NewItem.setAttribute("COLUMN_1_CAPTION", (String) ObjItem.getAttribute("COLUMN_1_CAPTION").getObj());
                        NewItem.setAttribute("COLUMN_2_ID", (int) ObjItem.getAttribute("COLUMN_2_ID").getVal());
                        NewItem.setAttribute("COLUMN_2_FORMULA", (String) ObjItem.getAttribute("COLUMN_2_FORMULA").getObj());
                        NewItem.setAttribute("COLUMN_2_PER", ObjItem.getAttribute("COLUMN_2_PER").getVal());
                        NewItem.setAttribute("COLUMN_2_AMT", ObjItem.getAttribute("COLUMN_2_AMT").getVal());
                        NewItem.setAttribute("COLUMN_2_CAPTION", (String) ObjItem.getAttribute("COLUMN_2_CAPTION").getObj());
                        NewItem.setAttribute("COLUMN_3_ID", (int) ObjItem.getAttribute("COLUMN_3_ID").getVal());
                        NewItem.setAttribute("COLUMN_3_FORMULA", (String) ObjItem.getAttribute("COLUMN_3_FORMULA").getObj());
                        NewItem.setAttribute("COLUMN_3_PER", ObjItem.getAttribute("COLUMN_3_PER").getVal());
                        NewItem.setAttribute("COLUMN_3_AMT", ObjItem.getAttribute("COLUMN_3_AMT").getVal());
                        NewItem.setAttribute("COLUMN_3_CAPTION", (String) ObjItem.getAttribute("COLUMN_3_CAPTION").getObj());
                        NewItem.setAttribute("COLUMN_4_ID", (int) ObjItem.getAttribute("COLUMN_4_ID").getVal());
                        NewItem.setAttribute("COLUMN_4_FORMULA", (String) ObjItem.getAttribute("COLUMN_4_FORMULA").getObj());
                        NewItem.setAttribute("COLUMN_4_PER", ObjItem.getAttribute("COLUMN_4_PER").getVal());
                        NewItem.setAttribute("COLUMN_4_AMT", ObjItem.getAttribute("COLUMN_4_AMT").getVal());
                        NewItem.setAttribute("COLUMN_4_CAPTION", (String) ObjItem.getAttribute("COLUMN_4_CAPTION").getObj());
                        NewItem.setAttribute("COLUMN_5_ID", (int) ObjItem.getAttribute("COLUMN_5_ID").getVal());
                        NewItem.setAttribute("COLUMN_5_FORMULA", (String) ObjItem.getAttribute("COLUMN_5_FORMULA").getObj());
                        NewItem.setAttribute("COLUMN_5_PER", ObjItem.getAttribute("COLUMN_5_PER").getVal());
                        NewItem.setAttribute("COLUMN_5_AMT", ObjItem.getAttribute("COLUMN_5_AMT").getVal());
                        NewItem.setAttribute("COLUMN_5_CAPTION", (String) ObjItem.getAttribute("COLUMN_5_CAPTION").getObj());
                        NewItem.setAttribute("COLUMN_6_ID", (int) ObjItem.getAttribute("COLUMN_6_ID").getVal());
                        NewItem.setAttribute("COLUMN_6_FORMULA", (String) ObjItem.getAttribute("COLUMN_6_FORMULA").getObj());
                        NewItem.setAttribute("COLUMN_6_PER", ObjItem.getAttribute("COLUMN_6_PER").getVal());
                        NewItem.setAttribute("COLUMN_6_AMT", ObjItem.getAttribute("COLUMN_6_AMT").getVal());
                        NewItem.setAttribute("COLUMN_6_CAPTION", (String) ObjItem.getAttribute("COLUMN_6_CAPTION").getObj());
                        NewItem.setAttribute("COLUMN_7_ID", (int) ObjItem.getAttribute("COLUMN_7_ID").getVal());
                        NewItem.setAttribute("COLUMN_7_FORMULA", (String) ObjItem.getAttribute("COLUMN_7_FORMULA").getObj());
                        NewItem.setAttribute("COLUMN_7_PER", ObjItem.getAttribute("COLUMN_7_PER").getVal());
                        NewItem.setAttribute("COLUMN_7_AMT", ObjItem.getAttribute("COLUMN_7_AMT").getVal());
                        NewItem.setAttribute("COLUMN_7_CAPTION", (String) ObjItem.getAttribute("COLUMN_7_CAPTION").getObj());
                        NewItem.setAttribute("COLUMN_8_ID", (int) ObjItem.getAttribute("COLUMN_8_ID").getVal());
                        NewItem.setAttribute("COLUMN_8_FORMULA", (String) ObjItem.getAttribute("COLUMN_8_FORMULA").getObj());
                        NewItem.setAttribute("COLUMN_8_PER", ObjItem.getAttribute("COLUMN_8_PER").getVal());
                        NewItem.setAttribute("COLUMN_8_AMT", ObjItem.getAttribute("COLUMN_8_AMT").getVal());
                        NewItem.setAttribute("COLUMN_8_CAPTION", (String) ObjItem.getAttribute("COLUMN_8_CAPTION").getObj());
                        NewItem.setAttribute("COLUMN_9_ID", (int) ObjItem.getAttribute("COLUMN_9_ID").getVal());
                        NewItem.setAttribute("COLUMN_9_FORMULA", (String) ObjItem.getAttribute("COLUMN_9_FORMULA").getObj());
                        NewItem.setAttribute("COLUMN_9_PER", ObjItem.getAttribute("COLUMN_9_PER").getVal());
                        NewItem.setAttribute("COLUMN_9_AMT", ObjItem.getAttribute("COLUMN_9_AMT").getVal());
                        NewItem.setAttribute("COLUMN_9_CAPTION", (String) ObjItem.getAttribute("COLUMN_9_CAPTION").getObj());
                        NewItem.setAttribute("COLUMN_10_ID", (int) ObjItem.getAttribute("COLUMN_10_ID").getVal());
                        NewItem.setAttribute("COLUMN_10_FORMULA", (String) ObjItem.getAttribute("COLUMN_10_FORMULA").getObj());
                        NewItem.setAttribute("COLUMN_10_PER", ObjItem.getAttribute("COLUMN_10_PER").getVal());
                        NewItem.setAttribute("COLUMN_10_AMT", ObjItem.getAttribute("COLUMN_10_AMT").getVal());
                        NewItem.setAttribute("COLUMN_10_CAPTION", (String) ObjItem.getAttribute("COLUMN_10_CAPTION").getObj());

                        NewItem.setAttribute("COLUMN_11_ID", (int) ObjItem.getAttribute("COLUMN_11_ID").getVal());
                        NewItem.setAttribute("COLUMN_11_FORMULA", (String) ObjItem.getAttribute("COLUMN_11_FORMULA").getObj());
                        NewItem.setAttribute("COLUMN_11_PER", ObjItem.getAttribute("COLUMN_11_PER").getVal());
                        NewItem.setAttribute("COLUMN_11_AMT", ObjItem.getAttribute("COLUMN_11_AMT").getVal());
                        NewItem.setAttribute("COLUMN_11_CAPTION", (String) ObjItem.getAttribute("COLUMN_11_CAPTION").getObj());
                        NewItem.setAttribute("COLUMN_12_ID", (int) ObjItem.getAttribute("COLUMN_12_ID").getVal());
                        NewItem.setAttribute("COLUMN_12_FORMULA", (String) ObjItem.getAttribute("COLUMN_12_FORMULA").getObj());
                        NewItem.setAttribute("COLUMN_12_PER", ObjItem.getAttribute("COLUMN_12_PER").getVal());
                        NewItem.setAttribute("COLUMN_12_AMT", ObjItem.getAttribute("COLUMN_12_AMT").getVal());
                        NewItem.setAttribute("COLUMN_12_CAPTION", (String) ObjItem.getAttribute("COLUMN_12_CAPTION").getObj());
                        NewItem.setAttribute("COLUMN_13_ID", (int) ObjItem.getAttribute("COLUMN_13_ID").getVal());
                        NewItem.setAttribute("COLUMN_13_FORMULA", (String) ObjItem.getAttribute("COLUMN_13_FORMULA").getObj());
                        NewItem.setAttribute("COLUMN_13_PER", ObjItem.getAttribute("COLUMN_13_PER").getVal());
                        NewItem.setAttribute("COLUMN_13_AMT", ObjItem.getAttribute("COLUMN_13_AMT").getVal());
                        NewItem.setAttribute("COLUMN_13_CAPTION", (String) ObjItem.getAttribute("COLUMN_13_CAPTION").getObj());
                        NewItem.setAttribute("COLUMN_14_ID", (int) ObjItem.getAttribute("COLUMN_14_ID").getVal());
                        NewItem.setAttribute("COLUMN_14_FORMULA", (String) ObjItem.getAttribute("COLUMN_14_FORMULA").getObj());
                        NewItem.setAttribute("COLUMN_14_PER", ObjItem.getAttribute("COLUMN_14_PER").getVal());
                        NewItem.setAttribute("COLUMN_14_AMT", ObjItem.getAttribute("COLUMN_14_AMT").getVal());
                        NewItem.setAttribute("COLUMN_14_CAPTION", (String) ObjItem.getAttribute("COLUMN_14_CAPTION").getObj());
                        NewItem.setAttribute("COLUMN_15_ID", (int) ObjItem.getAttribute("COLUMN_15_ID").getVal());
                        NewItem.setAttribute("COLUMN_15_FORMULA", (String) ObjItem.getAttribute("COLUMN_15_FORMULA").getObj());
                        NewItem.setAttribute("COLUMN_15_PER", ObjItem.getAttribute("COLUMN_15_PER").getVal());
                        NewItem.setAttribute("COLUMN_15_AMT", ObjItem.getAttribute("COLUMN_15_AMT").getVal());
                        NewItem.setAttribute("COLUMN_15_CAPTION", (String) ObjItem.getAttribute("COLUMN_15_CAPTION").getObj());
                        
                        NewItem.setAttribute("COLUMN_16_ID", (int) ObjItem.getAttribute("COLUMN_16_ID").getVal());
                        NewItem.setAttribute("COLUMN_16_FORMULA", (String) ObjItem.getAttribute("COLUMN_16_FORMULA").getObj());
                        NewItem.setAttribute("COLUMN_16_PER", ObjItem.getAttribute("COLUMN_16_PER").getVal());
                        NewItem.setAttribute("COLUMN_16_AMT", ObjItem.getAttribute("COLUMN_16_AMT").getVal());
                        NewItem.setAttribute("COLUMN_16_CAPTION", (String) ObjItem.getAttribute("COLUMN_16_CAPTION").getObj());
                        NewItem.setAttribute("COLUMN_17_ID", (int) ObjItem.getAttribute("COLUMN_17_ID").getVal());
                        NewItem.setAttribute("COLUMN_17_FORMULA", (String) ObjItem.getAttribute("COLUMN_17_FORMULA").getObj());
                        NewItem.setAttribute("COLUMN_17_PER", ObjItem.getAttribute("COLUMN_17_PER").getVal());
                        NewItem.setAttribute("COLUMN_17_AMT", ObjItem.getAttribute("COLUMN_17_AMT").getVal());
                        NewItem.setAttribute("COLUMN_17_CAPTION", (String) ObjItem.getAttribute("COLUMN_17_CAPTION").getObj());
                        NewItem.setAttribute("COLUMN_18_ID", (int) ObjItem.getAttribute("COLUMN_18_ID").getVal());
                        NewItem.setAttribute("COLUMN_18_FORMULA", (String) ObjItem.getAttribute("COLUMN_18_FORMULA").getObj());
                        NewItem.setAttribute("COLUMN_18_PER", ObjItem.getAttribute("COLUMN_18_PER").getVal());
                        NewItem.setAttribute("COLUMN_18_AMT", ObjItem.getAttribute("COLUMN_18_AMT").getVal());
                        NewItem.setAttribute("COLUMN_18_CAPTION", (String) ObjItem.getAttribute("COLUMN_18_CAPTION").getObj());
                        NewItem.setAttribute("COLUMN_19_ID", (int) ObjItem.getAttribute("COLUMN_19_ID").getVal());
                        NewItem.setAttribute("COLUMN_19_FORMULA", (String) ObjItem.getAttribute("COLUMN_19_FORMULA").getObj());
                        NewItem.setAttribute("COLUMN_19_PER", ObjItem.getAttribute("COLUMN_19_PER").getVal());
                        NewItem.setAttribute("COLUMN_19_AMT", ObjItem.getAttribute("COLUMN_19_AMT").getVal());
                        NewItem.setAttribute("COLUMN_19_CAPTION", (String) ObjItem.getAttribute("COLUMN_19_CAPTION").getObj());
                        NewItem.setAttribute("COLUMN_20_ID", (int) ObjItem.getAttribute("COLUMN_20_ID").getVal());
                        NewItem.setAttribute("COLUMN_20_FORMULA", (String) ObjItem.getAttribute("COLUMN_20_FORMULA").getObj());
                        NewItem.setAttribute("COLUMN_20_PER", ObjItem.getAttribute("COLUMN_20_PER").getVal());
                        NewItem.setAttribute("COLUMN_20_AMT", ObjItem.getAttribute("COLUMN_20_AMT").getVal());
                        NewItem.setAttribute("COLUMN_20_CAPTION", (String) ObjItem.getAttribute("COLUMN_20_CAPTION").getObj());

                        NewItem.setAttribute("COLUMN_21_ID", (int) ObjItem.getAttribute("COLUMN_21_ID").getVal());
                        NewItem.setAttribute("COLUMN_21_FORMULA", (String) ObjItem.getAttribute("COLUMN_21_FORMULA").getObj());
                        NewItem.setAttribute("COLUMN_21_PER", ObjItem.getAttribute("COLUMN_21_PER").getVal());
                        NewItem.setAttribute("COLUMN_21_AMT", ObjItem.getAttribute("COLUMN_21_AMT").getVal());
                        NewItem.setAttribute("COLUMN_21_CAPTION", (String) ObjItem.getAttribute("COLUMN_21_CAPTION").getObj());
                        

                        NewItem.setAttribute("EXCISE_GATEPASS_GIVEN", ObjItem.getAttribute("EXCISE_GATEPASS_GIVEN").getBool());
                        NewItem.setAttribute("IMPORT_CONCESS", ObjItem.getAttribute("IMPORT_CONCESS").getBool());
                        NewItem.setAttribute("QUOT_ID", (String) ObjItem.getAttribute("QUOT_ID").getObj());
                        NewItem.setAttribute("QUOT_SR_NO", (int) ObjItem.getAttribute("QUOT_SR_NO").getVal());
                        colPOItems.put(Integer.toString(colPOItems.size() + 1), NewItem);
                    }

                    //Clear the collection of this class
                    colPOTerms.clear();

                    for (int i = 1; i <= ObjPO.colPOTerms.size(); i++) {
                        clsPOTerms ObjItem = (clsPOTerms) ObjPO.colPOTerms.get(Integer.toString(i));

                        clsPOTerms NewItem = new clsPOTerms();

                        NewItem.setAttribute("COMPANY_ID", (int) ObjPO.getAttribute("COMPANY_ID").getVal());
                        NewItem.setAttribute("PO_NO", (String) ObjPO.getAttribute("PO_NO").getObj());
                        NewItem.setAttribute("SR_NO", i);
                        NewItem.setAttribute("PO_TYPE", POType);
                        NewItem.setAttribute("TERM_TYPE", (String) ObjItem.getAttribute("TERM_TYPE").getObj());
                        NewItem.setAttribute("TERM_CODE", (int) ObjItem.getAttribute("TERM_CODE").getVal());
                        NewItem.setAttribute("TERM_DESC", (String) ObjItem.getAttribute("TERM_DESC").getObj());

                        colPOTerms.put(Integer.toString(colPOTerms.size() + 1), NewItem);
                    }
                }
            }

            rsResultSet.updateString("AMEND_DATE", (String) getAttribute("AMEND_DATE").getObj());
            rsResultSet.updateInt("PO_TYPE", POType);
            rsResultSet.updateLong("DOC_ID", (long) getAttribute("DOC_ID").getVal());
            rsResultSet.updateString("PO_REF", (String) getAttribute("PO_REF").getObj());
            rsResultSet.updateString("SUPP_ID", (String) getAttribute("SUPP_ID").getObj());
            rsResultSet.updateString("SUPP_NAME", (String) getAttribute("SUPP_NAME").getObj());
            rsResultSet.updateString("AMEND_REASON", (String) getAttribute("AMEND_REASON").getObj());
            rsResultSet.updateString("REF_A", (String) getAttribute("REF_A").getObj());
            rsResultSet.updateString("REF_B", (String) getAttribute("REF_B").getObj());
            rsResultSet.updateString("QUOTATION_NO", (String) getAttribute("QUOTATION_NO").getObj());
            rsResultSet.updateString("QUOTATION_DATE", (String) getAttribute("QUOTATION_DATE").getObj());
            rsResultSet.updateString("INQUIRY_NO", (String) getAttribute("INQUIRY_NO").getObj());
            rsResultSet.updateString("INQUIRY_DATE", (String) getAttribute("INQUIRY_DATE").getObj());
            rsResultSet.updateInt("BUYER", (int) getAttribute("BUYER").getVal());
            rsResultSet.updateInt("TRANSPORT_MODE", (int) getAttribute("TRANSPORT_MODE").getVal());
            rsResultSet.updateString("PURPOSE", (String) getAttribute("PURPOSE").getObj());
            rsResultSet.updateString("SUBJECT", (String) getAttribute("SUBJECT").getObj());
            rsResultSet.updateInt("CURRENCY_ID", (int) getAttribute("CURRENCY_ID").getVal());
            rsResultSet.updateDouble("CURRENCY_RATE", getAttribute("CURRENCY_RATE").getVal());
            rsResultSet.updateDouble("TOTAL_AMOUNT", getAttribute("TOTAL_AMOUNT").getVal());
            rsResultSet.updateDouble("NET_AMOUNT", getAttribute("NET_AMOUNT").getVal());
            rsResultSet.updateString("STATUS", (String) getAttribute("STATUS").getObj());
            rsResultSet.updateBoolean("ATTACHEMENT", getAttribute("ATTACHEMENT").getBool());
            rsResultSet.updateString("ATTACHEMENT_PATH", (String) getAttribute("ATTACHEMENT_PATH").getObj());
            rsResultSet.updateString("REMARKS", (String) getAttribute("REMARKS").getObj());
            rsResultSet.updateInt("SHIP_ID", (int) getAttribute("SHIP_ID").getVal());
            rsResultSet.updateBoolean("CANCELLED", getAttribute("CANCELLED").getBool());
            rsResultSet.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            //rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            //rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
            rsResultSet.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            //Now Custom Columns
            rsResultSet.updateInt("COLUMN_1_ID", (int) getAttribute("COLUMN_1_ID").getVal());
            rsResultSet.updateString("COLUMN_1_FORMULA", (String) getAttribute("COLUMN_1_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_1_PER", getAttribute("COLUMN_1_PER").getVal());
            rsResultSet.updateDouble("COLUMN_1_AMT", getAttribute("COLUMN_1_AMT").getVal());
            rsResultSet.updateString("COLUMN_1_CAPTION", (String) getAttribute("COLUMN_1_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_2_ID", (int) getAttribute("COLUMN_2_ID").getVal());
            rsResultSet.updateString("COLUMN_2_FORMULA", (String) getAttribute("COLUMN_2_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_2_PER", getAttribute("COLUMN_2_PER").getVal());
            rsResultSet.updateDouble("COLUMN_2_AMT", getAttribute("COLUMN_2_AMT").getVal());
            rsResultSet.updateString("COLUMN_2_CAPTION", (String) getAttribute("COLUMN_2_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_3_ID", (int) getAttribute("COLUMN_3_ID").getVal());
            rsResultSet.updateString("COLUMN_3_FORMULA", (String) getAttribute("COLUMN_3_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_3_PER", getAttribute("COLUMN_3_PER").getVal());
            rsResultSet.updateDouble("COLUMN_3_AMT", getAttribute("COLUMN_3_AMT").getVal());
            rsResultSet.updateString("COLUMN_3_CAPTION", (String) getAttribute("COLUMN_3_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_4_ID", (int) getAttribute("COLUMN_4_ID").getVal());
            rsResultSet.updateString("COLUMN_4_FORMULA", (String) getAttribute("COLUMN_4_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_4_PER", getAttribute("COLUMN_4_PER").getVal());
            rsResultSet.updateDouble("COLUMN_4_AMT", getAttribute("COLUMN_4_AMT").getVal());
            rsResultSet.updateString("COLUMN_4_CAPTION", (String) getAttribute("COLUMN_4_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_5_ID", (int) getAttribute("COLUMN_5_ID").getVal());
            rsResultSet.updateString("COLUMN_5_FORMULA", (String) getAttribute("COLUMN_5_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_5_PER", getAttribute("COLUMN_5_PER").getVal());
            rsResultSet.updateDouble("COLUMN_5_AMT", getAttribute("COLUMN_5_AMT").getVal());
            rsResultSet.updateString("COLUMN_5_CAPTION", (String) getAttribute("COLUMN_5_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_6_ID", (int) getAttribute("COLUMN_6_ID").getVal());
            rsResultSet.updateString("COLUMN_6_FORMULA", (String) getAttribute("COLUMN_6_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_6_PER", getAttribute("COLUMN_6_PER").getVal());
            rsResultSet.updateDouble("COLUMN_6_AMT", getAttribute("COLUMN_6_AMT").getVal());
            rsResultSet.updateString("COLUMN_6_CAPTION", (String) getAttribute("COLUMN_6_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_7_ID", (int) getAttribute("COLUMN_7_ID").getVal());
            rsResultSet.updateString("COLUMN_7_FORMULA", (String) getAttribute("COLUMN_7_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_7_PER", getAttribute("COLUMN_7_PER").getVal());
            rsResultSet.updateDouble("COLUMN_7_AMT", getAttribute("COLUMN_7_AMT").getVal());
            rsResultSet.updateString("COLUMN_7_CAPTION", (String) getAttribute("COLUMN_7_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_8_ID", (int) getAttribute("COLUMN_8_ID").getVal());
            rsResultSet.updateString("COLUMN_8_FORMULA", (String) getAttribute("COLUMN_8_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_8_PER", getAttribute("COLUMN_8_PER").getVal());
            rsResultSet.updateDouble("COLUMN_8_AMT", getAttribute("COLUMN_8_AMT").getVal());
            rsResultSet.updateString("COLUMN_8_CAPTION", (String) getAttribute("COLUMN_8_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_9_ID", (int) getAttribute("COLUMN_9_ID").getVal());
            rsResultSet.updateString("COLUMN_9_FORMULA", (String) getAttribute("COLUMN_9_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_9_PER", getAttribute("COLUMN_9_PER").getVal());
            rsResultSet.updateDouble("COLUMN_9_AMT", getAttribute("COLUMN_9_AMT").getVal());
            rsResultSet.updateString("COLUMN_9_CAPTION", (String) getAttribute("COLUMN_9_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_10_ID", (int) getAttribute("COLUMN_10_ID").getVal());
            rsResultSet.updateString("COLUMN_10_FORMULA", (String) getAttribute("COLUMN_10_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_10_PER", getAttribute("COLUMN_10_PER").getVal());
            rsResultSet.updateDouble("COLUMN_10_AMT", getAttribute("COLUMN_10_AMT").getVal());
            rsResultSet.updateString("COLUMN_10_CAPTION", (String) getAttribute("COLUMN_10_CAPTION").getObj());

            rsResultSet.updateInt("COLUMN_11_ID", (int) getAttribute("COLUMN_11_ID").getVal());
            rsResultSet.updateString("COLUMN_11_FORMULA", (String) getAttribute("COLUMN_11_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_11_PER", getAttribute("COLUMN_11_PER").getVal());
            rsResultSet.updateDouble("COLUMN_11_AMT", getAttribute("COLUMN_11_AMT").getVal());
            rsResultSet.updateString("COLUMN_11_CAPTION", (String) getAttribute("COLUMN_11_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_12_ID", (int) getAttribute("COLUMN_12_ID").getVal());
            rsResultSet.updateString("COLUMN_12_FORMULA", (String) getAttribute("COLUMN_12_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_12_PER", getAttribute("COLUMN_12_PER").getVal());
            rsResultSet.updateDouble("COLUMN_12_AMT", getAttribute("COLUMN_12_AMT").getVal());
            rsResultSet.updateString("COLUMN_12_CAPTION", (String) getAttribute("COLUMN_12_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_13_ID", (int) getAttribute("COLUMN_13_ID").getVal());
            rsResultSet.updateString("COLUMN_13_FORMULA", (String) getAttribute("COLUMN_13_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_13_PER", getAttribute("COLUMN_13_PER").getVal());
            rsResultSet.updateDouble("COLUMN_13_AMT", getAttribute("COLUMN_13_AMT").getVal());
            rsResultSet.updateString("COLUMN_13_CAPTION", (String) getAttribute("COLUMN_13_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_14_ID", (int) getAttribute("COLUMN_14_ID").getVal());
            rsResultSet.updateString("COLUMN_14_FORMULA", (String) getAttribute("COLUMN_14_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_14_PER", getAttribute("COLUMN_14_PER").getVal());
            rsResultSet.updateDouble("COLUMN_14_AMT", getAttribute("COLUMN_14_AMT").getVal());
            rsResultSet.updateString("COLUMN_14_CAPTION", (String) getAttribute("COLUMN_14_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_15_ID", (int) getAttribute("COLUMN_15_ID").getVal());
            rsResultSet.updateString("COLUMN_15_FORMULA", (String) getAttribute("COLUMN_15_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_15_PER", getAttribute("COLUMN_15_PER").getVal());
            rsResultSet.updateDouble("COLUMN_15_AMT", getAttribute("COLUMN_15_AMT").getVal());
            rsResultSet.updateString("COLUMN_15_CAPTION", (String) getAttribute("COLUMN_15_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_16_ID", (int) getAttribute("COLUMN_16_ID").getVal());
            rsResultSet.updateString("COLUMN_16_FORMULA", (String) getAttribute("COLUMN_16_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_16_PER", getAttribute("COLUMN_16_PER").getVal());
            rsResultSet.updateDouble("COLUMN_16_AMT", getAttribute("COLUMN_16_AMT").getVal());
            rsResultSet.updateString("COLUMN_16_CAPTION", (String) getAttribute("COLUMN_16_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_17_ID", (int) getAttribute("COLUMN_17_ID").getVal());
            rsResultSet.updateString("COLUMN_17_FORMULA", (String) getAttribute("COLUMN_17_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_17_PER", getAttribute("COLUMN_17_PER").getVal());
            rsResultSet.updateDouble("COLUMN_17_AMT", getAttribute("COLUMN_17_AMT").getVal());
            rsResultSet.updateString("COLUMN_17_CAPTION", (String) getAttribute("COLUMN_17_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_18_ID", (int) getAttribute("COLUMN_18_ID").getVal());
            rsResultSet.updateString("COLUMN_18_FORMULA", (String) getAttribute("COLUMN_18_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_18_PER", getAttribute("COLUMN_18_PER").getVal());
            rsResultSet.updateDouble("COLUMN_18_AMT", getAttribute("COLUMN_18_AMT").getVal());
            rsResultSet.updateString("COLUMN_18_CAPTION", (String) getAttribute("COLUMN_18_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_19_ID", (int) getAttribute("COLUMN_19_ID").getVal());
            rsResultSet.updateString("COLUMN_19_FORMULA", (String) getAttribute("COLUMN_19_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_19_PER", getAttribute("COLUMN_19_PER").getVal());
            rsResultSet.updateDouble("COLUMN_19_AMT", getAttribute("COLUMN_19_AMT").getVal());
            rsResultSet.updateString("COLUMN_19_CAPTION", (String) getAttribute("COLUMN_19_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_20_ID", (int) getAttribute("COLUMN_20_ID").getVal());
            rsResultSet.updateString("COLUMN_20_FORMULA", (String) getAttribute("COLUMN_20_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_20_PER", getAttribute("COLUMN_20_PER").getVal());
            rsResultSet.updateDouble("COLUMN_20_AMT", getAttribute("COLUMN_20_AMT").getVal());
            rsResultSet.updateString("COLUMN_20_CAPTION", (String) getAttribute("COLUMN_20_CAPTION").getObj());

            rsResultSet.updateInt("COLUMN_21_ID", (int) getAttribute("COLUMN_21_ID").getVal());
            rsResultSet.updateString("COLUMN_21_FORMULA", (String) getAttribute("COLUMN_21_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_21_PER", getAttribute("COLUMN_21_PER").getVal());
            rsResultSet.updateDouble("COLUMN_21_AMT", getAttribute("COLUMN_21_AMT").getVal());
            rsResultSet.updateString("COLUMN_21_CAPTION", (String) getAttribute("COLUMN_21_CAPTION").getObj());
            

            rsResultSet.updateBoolean("IMPORT_CONCESS", getAttribute("IMPORT_CONCESS").getBool());
            rsResultSet.updateString("PRINT_LINE_1", (String) getAttribute("PRINT_LINE_1").getObj());
            rsResultSet.updateString("PRINT_LINE_2", (String) getAttribute("PRINT_LINE_2").getObj());
            rsResultSet.updateString("IMPORT_LICENSE", (String) getAttribute("IMPORT_LICENSE").getObj());
            rsResultSet.updateBoolean("CHANGED", true);
            rsResultSet.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("PAYMENT_TERM", (String) getAttribute("PAYMENT_TERM").getObj());

            rsResultSet.updateInt("PAYMENT_CODE", getAttribute("PAYMENT_CODE").getInt());
            rsResultSet.updateInt("CR_DAYS", getAttribute("CR_DAYS").getInt());
            rsResultSet.updateInt("DEPT_ID", getAttribute("DEPT_ID").getInt());

            rsResultSet.updateString("PRICE_BASIS_TERM", (String) getAttribute("PRICE_BASIS_TERM").getObj());
            rsResultSet.updateString("DISCOUNT_TERM", (String) getAttribute("DISCOUNT_TERM").getObj());
            rsResultSet.updateString("EXCISE_TERM", (String) getAttribute("EXCISE_TERM").getObj());
            rsResultSet.updateString("ST_TERM", (String) getAttribute("ST_TERM").getObj());
            rsResultSet.updateString("PF_TERM", (String) getAttribute("PF_TERM").getObj());
            rsResultSet.updateString("FREIGHT_TERM", (String) getAttribute("FREIGHT_TERM").getObj());
            rsResultSet.updateString("OCTROI_TERM", (String) getAttribute("OCTROI_TERM").getObj());
            rsResultSet.updateString("FOB_TERM", (String) getAttribute("FOB_TERM").getObj());
            rsResultSet.updateString("CIE_TERM", (String) getAttribute("CIE_TERM").getObj());
            rsResultSet.updateString("INSURANCE_TERM", (String) getAttribute("INSURANCE_TERM").getObj());
            rsResultSet.updateString("TCC_TERM", (String) getAttribute("TCC_TERM").getObj());
            rsResultSet.updateString("CENVAT_TERM", (String) getAttribute("CENVAT_TERM").getObj());
            rsResultSet.updateString("DESPATCH_TERM", (String) getAttribute("DESPATCH_TERM").getObj());
            rsResultSet.updateString("SERVICE_TAX_TERM", (String) getAttribute("SERVICE_TAX_TERM").getObj());
            rsResultSet.updateString("OTHERS_TERM", (String) getAttribute("OTHERS_TERM").getObj());
            rsResultSet.updateString("COVERING_TEXT", (String) getAttribute("COVERING_TEXT").getObj());
            
            rsResultSet.updateString("CGST_TERM",(String)getAttribute("CGST_TERM").getObj());
            rsResultSet.updateString("SGST_TERM",(String)getAttribute("SGST_TERM").getObj());
            rsResultSet.updateString("IGST_TERM",(String)getAttribute("IGST_TERM").getObj());
            rsResultSet.updateString("COMPOSITION_TERM",(String)getAttribute("COMPOSITION_TERM").getObj());
            rsResultSet.updateString("RCM_TERM",(String)getAttribute("RCM_TERM").getObj());
            rsResultSet.updateString("GST_COMPENSATION_CESS_TERM",(String)getAttribute("GST_COMPENSATION_CESS_TERM").getObj());

            rsResultSet.updateString("PREMISES_TYPE", (String) getAttribute("PREMISES_TYPE").getObj());
            rsResultSet.updateString("SCOPE", (String) getAttribute("SCOPE").getObj());
            rsResultSet.updateString("SERVICE_PERIOD", (String) getAttribute("SERVICE_PERIOD").getObj());
            rsResultSet.updateString("SERVICE_FREQUENCY", (String) getAttribute("SERVICE_FREQUENCY").getObj());
            rsResultSet.updateString("CONTRACT_DETAILS", (String) getAttribute("CONTRACT_DETAILS").getObj());
            rsResultSet.updateString("SERVICE_REPORT", (String) getAttribute("SERVICE_REPORT").getObj());
            rsResultSet.updateString("ESI_TERMS", (String) getAttribute("ESI_TERMS").getObj());
            rsResultSet.updateString("TERMINATION_TERMS", (String) getAttribute("TERMINATION_TERMS").getObj());
            rsResultSet.updateString("FILE_TEXT", (String) getAttribute("FILE_TEXT").getObj());
            rsResultSet.updateString("AMOUNT_IN_WORDS", (String) getAttribute("AMOUNT_IN_WORDS").getObj());
            rsResultSet.updateBoolean("DIRECTOR_APPROVAL", getAttribute("DIRECTOR_APPROVAL").getBool());
            rsResultSet.updateBoolean("IMPORTED", getAttribute("IMPORTED").getBool());
            rsResultSet.updateBoolean("CANCELLED", false);
            rsResultSet.updateRow();

            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_PUR_AMEND_HEADER_H WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND AMEND_NO='" + (String) getAttribute("AMEND_NO").getObj() + "' AND PO_TYPE=" + POType);
            RevNo++;
            String RevDocNo = (String) getAttribute("AMEND_NO").getObj();

            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO", RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY", EITLERPGLOBAL.gNewUserID);
            rsHistory.updateString("APPROVAL_STATUS", (String) getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS", (String) getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("AMEND_NO", (String) getAttribute("AMEND_NO").getObj());
            rsHistory.updateLong("DOC_ID", (long) getAttribute("DOC_ID").getVal());
            rsHistory.updateString("AMEND_DATE", (String) getAttribute("AMEND_DATE").getObj());
            rsHistory.updateString("AMEND_REASON", (String) getAttribute("AMEND_REASON").getObj());
            rsHistory.updateString("PO_NO", (String) getAttribute("PO_NO").getObj());
            rsHistory.updateString("PO_DATE", (String) getAttribute("PO_DATE").getObj());
            rsHistory.updateInt("PO_TYPE", POType);
            rsHistory.updateString("PO_REF", (String) getAttribute("PO_REF").getObj());
            rsHistory.updateString("SUPP_ID", (String) getAttribute("SUPP_ID").getObj());
            rsHistory.updateString("SUPP_NAME", (String) getAttribute("SUPP_NAME").getObj());
            rsHistory.updateString("REF_A", (String) getAttribute("REF_A").getObj());
            rsHistory.updateString("REF_B", (String) getAttribute("REF_B").getObj());
            rsHistory.updateString("QUOTATION_NO", (String) getAttribute("QUOTATION_NO").getObj());
            rsHistory.updateString("QUOTATION_DATE", (String) getAttribute("QUOTATION_DATE").getObj());
            rsHistory.updateString("INQUIRY_NO", (String) getAttribute("INQUIRY_NO").getObj());
            rsHistory.updateString("INQUIRY_DATE", (String) getAttribute("INQUIRY_DATE").getObj());
            rsHistory.updateInt("BUYER", (int) getAttribute("BUYER").getVal());
            rsHistory.updateInt("TRANSPORT_MODE", (int) getAttribute("TRANSPORT_MODE").getVal());
            rsHistory.updateString("PURPOSE", (String) getAttribute("PURPOSE").getObj());
            rsHistory.updateString("SUBJECT", (String) getAttribute("SUBJECT").getObj());
            rsHistory.updateInt("CURRENCY_ID", (int) getAttribute("CURRENCY_ID").getVal());
            rsHistory.updateDouble("CURRENCY_RATE", getAttribute("CURRENCY_RATE").getVal());
            rsHistory.updateDouble("TOTAL_AMOUNT", getAttribute("TOTAL_AMOUNT").getVal());
            rsHistory.updateDouble("NET_AMOUNT", getAttribute("NET_AMOUNT").getVal());
            rsHistory.updateString("STATUS", (String) getAttribute("STATUS").getObj());
            rsHistory.updateBoolean("ATTACHEMENT", getAttribute("ATTACHEMENT").getBool());
            rsHistory.updateString("ATTACHEMENT_PATH", (String) getAttribute("ATTACHEMENT_PATH").getObj());
            rsHistory.updateString("REMARKS", (String) getAttribute("REMARKS").getObj());
            rsHistory.updateInt("SHIP_ID", (int) getAttribute("SHIP_ID").getVal());
            rsHistory.updateBoolean("CANCELLED", getAttribute("CANCELLED").getBool());
            rsHistory.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            //rsHistory.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            //rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
            rsHistory.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateInt("COLUMN_1_ID", (int) getAttribute("COLUMN_1_ID").getVal());
            rsHistory.updateString("COLUMN_1_FORMULA", (String) getAttribute("COLUMN_1_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_1_PER", getAttribute("COLUMN_1_PER").getVal());
            rsHistory.updateDouble("COLUMN_1_AMT", getAttribute("COLUMN_1_AMT").getVal());
            rsHistory.updateString("COLUMN_1_CAPTION", (String) getAttribute("COLUMN_1_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_2_ID", (int) getAttribute("COLUMN_2_ID").getVal());
            rsHistory.updateString("COLUMN_2_FORMULA", (String) getAttribute("COLUMN_2_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_2_PER", getAttribute("COLUMN_2_PER").getVal());
            rsHistory.updateDouble("COLUMN_2_AMT", getAttribute("COLUMN_2_AMT").getVal());
            rsHistory.updateString("COLUMN_2_CAPTION", (String) getAttribute("COLUMN_2_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_3_ID", (int) getAttribute("COLUMN_3_ID").getVal());
            rsHistory.updateString("COLUMN_3_FORMULA", (String) getAttribute("COLUMN_3_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_3_PER", getAttribute("COLUMN_3_PER").getVal());
            rsHistory.updateDouble("COLUMN_3_AMT", getAttribute("COLUMN_3_AMT").getVal());
            rsHistory.updateString("COLUMN_3_CAPTION", (String) getAttribute("COLUMN_3_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_4_ID", (int) getAttribute("COLUMN_4_ID").getVal());
            rsHistory.updateString("COLUMN_4_FORMULA", (String) getAttribute("COLUMN_4_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_4_PER", getAttribute("COLUMN_4_PER").getVal());
            rsHistory.updateDouble("COLUMN_4_AMT", getAttribute("COLUMN_4_AMT").getVal());
            rsHistory.updateString("COLUMN_4_CAPTION", (String) getAttribute("COLUMN_4_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_5_ID", (int) getAttribute("COLUMN_5_ID").getVal());
            rsHistory.updateString("COLUMN_5_FORMULA", (String) getAttribute("COLUMN_5_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_5_PER", getAttribute("COLUMN_5_PER").getVal());
            rsHistory.updateDouble("COLUMN_5_AMT", getAttribute("COLUMN_5_AMT").getVal());
            rsHistory.updateString("COLUMN_5_CAPTION", (String) getAttribute("COLUMN_5_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_6_ID", (int) getAttribute("COLUMN_6_ID").getVal());
            rsHistory.updateString("COLUMN_6_FORMULA", (String) getAttribute("COLUMN_6_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_6_PER", getAttribute("COLUMN_6_PER").getVal());
            rsHistory.updateDouble("COLUMN_6_AMT", getAttribute("COLUMN_6_AMT").getVal());
            rsHistory.updateString("COLUMN_6_CAPTION", (String) getAttribute("COLUMN_6_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_7_ID", (int) getAttribute("COLUMN_7_ID").getVal());
            rsHistory.updateString("COLUMN_7_FORMULA", (String) getAttribute("COLUMN_7_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_7_PER", getAttribute("COLUMN_7_PER").getVal());
            rsHistory.updateDouble("COLUMN_7_AMT", getAttribute("COLUMN_7_AMT").getVal());
            rsHistory.updateString("COLUMN_7_CAPTION", (String) getAttribute("COLUMN_7_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_8_ID", (int) getAttribute("COLUMN_8_ID").getVal());
            rsHistory.updateString("COLUMN_8_FORMULA", (String) getAttribute("COLUMN_8_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_8_PER", getAttribute("COLUMN_8_PER").getVal());
            rsHistory.updateDouble("COLUMN_8_AMT", getAttribute("COLUMN_8_AMT").getVal());
            rsHistory.updateString("COLUMN_8_CAPTION", (String) getAttribute("COLUMN_8_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_9_ID", (int) getAttribute("COLUMN_9_ID").getVal());
            rsHistory.updateString("COLUMN_9_FORMULA", (String) getAttribute("COLUMN_9_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_9_PER", getAttribute("COLUMN_9_PER").getVal());
            rsHistory.updateDouble("COLUMN_9_AMT", getAttribute("COLUMN_9_AMT").getVal());
            rsHistory.updateString("COLUMN_9_CAPTION", (String) getAttribute("COLUMN_9_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_10_ID", (int) getAttribute("COLUMN_10_ID").getVal());
            rsHistory.updateString("COLUMN_10_FORMULA", (String) getAttribute("COLUMN_10_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_10_PER", getAttribute("COLUMN_10_PER").getVal());
            rsHistory.updateDouble("COLUMN_10_AMT", getAttribute("COLUMN_10_AMT").getVal());
            rsHistory.updateString("COLUMN_10_CAPTION", (String) getAttribute("COLUMN_10_CAPTION").getObj());

            rsHistory.updateInt("COLUMN_11_ID", (int) getAttribute("COLUMN_11_ID").getVal());
            rsHistory.updateString("COLUMN_11_FORMULA", (String) getAttribute("COLUMN_11_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_11_PER", getAttribute("COLUMN_11_PER").getVal());
            rsHistory.updateDouble("COLUMN_11_AMT", getAttribute("COLUMN_11_AMT").getVal());
            rsHistory.updateString("COLUMN_11_CAPTION", (String) getAttribute("COLUMN_11_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_12_ID", (int) getAttribute("COLUMN_12_ID").getVal());
            rsHistory.updateString("COLUMN_12_FORMULA", (String) getAttribute("COLUMN_12_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_12_PER", getAttribute("COLUMN_12_PER").getVal());
            rsHistory.updateDouble("COLUMN_12_AMT", getAttribute("COLUMN_12_AMT").getVal());
            rsHistory.updateString("COLUMN_12_CAPTION", (String) getAttribute("COLUMN_12_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_13_ID", (int) getAttribute("COLUMN_13_ID").getVal());
            rsHistory.updateString("COLUMN_13_FORMULA", (String) getAttribute("COLUMN_13_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_13_PER", getAttribute("COLUMN_13_PER").getVal());
            rsHistory.updateDouble("COLUMN_13_AMT", getAttribute("COLUMN_13_AMT").getVal());
            rsHistory.updateString("COLUMN_13_CAPTION", (String) getAttribute("COLUMN_13_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_14_ID", (int) getAttribute("COLUMN_14_ID").getVal());
            rsHistory.updateString("COLUMN_14_FORMULA", (String) getAttribute("COLUMN_14_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_14_PER", getAttribute("COLUMN_14_PER").getVal());
            rsHistory.updateDouble("COLUMN_14_AMT", getAttribute("COLUMN_14_AMT").getVal());
            rsHistory.updateString("COLUMN_14_CAPTION", (String) getAttribute("COLUMN_14_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_15_ID", (int) getAttribute("COLUMN_15_ID").getVal());
            rsHistory.updateString("COLUMN_15_FORMULA", (String) getAttribute("COLUMN_15_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_15_PER", getAttribute("COLUMN_15_PER").getVal());
            rsHistory.updateDouble("COLUMN_15_AMT", getAttribute("COLUMN_15_AMT").getVal());
            rsHistory.updateString("COLUMN_15_CAPTION", (String) getAttribute("COLUMN_15_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_16_ID", (int) getAttribute("COLUMN_16_ID").getVal());
            rsHistory.updateString("COLUMN_16_FORMULA", (String) getAttribute("COLUMN_16_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_16_PER", getAttribute("COLUMN_16_PER").getVal());
            rsHistory.updateDouble("COLUMN_16_AMT", getAttribute("COLUMN_16_AMT").getVal());
            rsHistory.updateString("COLUMN_16_CAPTION", (String) getAttribute("COLUMN_16_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_17_ID", (int) getAttribute("COLUMN_17_ID").getVal());
            rsHistory.updateString("COLUMN_17_FORMULA", (String) getAttribute("COLUMN_17_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_17_PER", getAttribute("COLUMN_17_PER").getVal());
            rsHistory.updateDouble("COLUMN_17_AMT", getAttribute("COLUMN_17_AMT").getVal());
            rsHistory.updateString("COLUMN_17_CAPTION", (String) getAttribute("COLUMN_17_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_18_ID", (int) getAttribute("COLUMN_18_ID").getVal());
            rsHistory.updateString("COLUMN_18_FORMULA", (String) getAttribute("COLUMN_18_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_18_PER", getAttribute("COLUMN_18_PER").getVal());
            rsHistory.updateDouble("COLUMN_18_AMT", getAttribute("COLUMN_18_AMT").getVal());
            rsHistory.updateString("COLUMN_18_CAPTION", (String) getAttribute("COLUMN_18_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_19_ID", (int) getAttribute("COLUMN_19_ID").getVal());
            rsHistory.updateString("COLUMN_19_FORMULA", (String) getAttribute("COLUMN_19_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_19_PER", getAttribute("COLUMN_19_PER").getVal());
            rsHistory.updateDouble("COLUMN_19_AMT", getAttribute("COLUMN_19_AMT").getVal());
            rsHistory.updateString("COLUMN_19_CAPTION", (String) getAttribute("COLUMN_19_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_20_ID", (int) getAttribute("COLUMN_20_ID").getVal());
            rsHistory.updateString("COLUMN_20_FORMULA", (String) getAttribute("COLUMN_20_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_20_PER", getAttribute("COLUMN_20_PER").getVal());
            rsHistory.updateDouble("COLUMN_20_AMT", getAttribute("COLUMN_20_AMT").getVal());
            rsHistory.updateString("COLUMN_20_CAPTION", (String) getAttribute("COLUMN_20_CAPTION").getObj());

            rsHistory.updateInt("COLUMN_21_ID", (int) getAttribute("COLUMN_21_ID").getVal());
            rsHistory.updateString("COLUMN_21_FORMULA", (String) getAttribute("COLUMN_21_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_21_PER", getAttribute("COLUMN_21_PER").getVal());
            rsHistory.updateDouble("COLUMN_21_AMT", getAttribute("COLUMN_21_AMT").getVal());
            rsHistory.updateString("COLUMN_21_CAPTION", (String) getAttribute("COLUMN_21_CAPTION").getObj());
            

            rsHistory.updateInt("REASON_CODE", (int) getAttribute("REASON_CODE").getVal());
            rsHistory.updateString("AMEND_REASON", (String) getAttribute("AMEND_REASON").getObj());
            rsHistory.updateBoolean("IMPORT_CONCESS", getAttribute("IMPORT_CONCESS").getBool());
            rsHistory.updateString("PRINT_LINE_1", (String) getAttribute("PRINT_LINE_1").getObj());
            rsHistory.updateString("PRINT_LINE_2", (String) getAttribute("PRINT_LINE_2").getObj());
            rsHistory.updateString("IMPORT_LICENSE", (String) getAttribute("IMPORT_LICENSE").getObj());
            rsHistory.updateBoolean("CHANGED", true);
            rsHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("PAYMENT_TERM", (String) getAttribute("PAYMENT_TERM").getObj());

            rsHistory.updateInt("PAYMENT_CODE", getAttribute("PAYMENT_CODE").getInt());
            rsHistory.updateInt("CR_DAYS", getAttribute("CR_DAYS").getInt());
            rsHistory.updateInt("DEPT_ID", getAttribute("DEPT_ID").getInt());

            rsHistory.updateString("PRICE_BASIS_TERM", (String) getAttribute("PRICE_BASIS_TERM").getObj());
            rsHistory.updateString("DISCOUNT_TERM", (String) getAttribute("DISCOUNT_TERM").getObj());
            rsHistory.updateString("EXCISE_TERM", (String) getAttribute("EXCISE_TERM").getObj());
            rsHistory.updateString("ST_TERM", (String) getAttribute("ST_TERM").getObj());
            rsHistory.updateString("PF_TERM", (String) getAttribute("PF_TERM").getObj());
            rsHistory.updateString("FREIGHT_TERM", (String) getAttribute("OCTROI_TERM").getObj());
            rsHistory.updateString("OCTROI_TERM", (String) getAttribute("OCTROI_TERM").getObj());
            rsHistory.updateString("FOB_TERM", (String) getAttribute("FOB_TERM").getObj());
            rsHistory.updateString("CIE_TERM", (String) getAttribute("CIE_TERM").getObj());
            rsHistory.updateString("INSURANCE_TERM", (String) getAttribute("INSURANCE_TERM").getObj());
            rsHistory.updateString("TCC_TERM", (String) getAttribute("TCC_TERM").getObj());
            rsHistory.updateString("CENVAT_TERM", (String) getAttribute("CENVAT_TERM").getObj());
            rsHistory.updateString("DESPATCH_TERM", (String) getAttribute("DESPATCH_TERM").getObj());
            rsHistory.updateString("SERVICE_TAX_TERM", (String) getAttribute("SERVICE_TAX_TERM").getObj());
            rsHistory.updateString("OTHERS_TERM", (String) getAttribute("OTHERS_TERM").getObj());
            
            rsHistory.updateString("CGST_TERM",(String)getAttribute("CGST_TERM").getObj());
            rsHistory.updateString("SGST_TERM",(String)getAttribute("SGST_TERM").getObj());
            rsHistory.updateString("IGST_TERM",(String)getAttribute("IGST_TERM").getObj());
            rsHistory.updateString("COMPOSITION_TERM",(String)getAttribute("COMPOSITION_TERM").getObj());
            rsHistory.updateString("RCM_TERM",(String)getAttribute("RCM_TERM").getObj());
            rsHistory.updateString("GST_COMPENSATION_CESS_TERM",(String)getAttribute("GST_COMPENSATION_CESS_TERM").getObj());
            
            rsHistory.updateString("COVERING_TEXT", (String) getAttribute("COVERING_TEXT").getObj());
            rsHistory.updateString("PREMISES_TYPE", (String) getAttribute("PREMISES_TYPE").getObj());
            rsHistory.updateString("SCOPE", (String) getAttribute("SCOPE").getObj());
            rsHistory.updateString("SERVICE_PERIOD", (String) getAttribute("SERVICE_PERIOD").getObj());
            rsHistory.updateString("SERVICE_FREQUENCY", (String) getAttribute("SERVICE_FREQUENCY").getObj());
            rsHistory.updateString("CONTRACT_DETAILS", (String) getAttribute("CONTRACT_DETAILS").getObj());
            rsHistory.updateString("SERVICE_REPORT", (String) getAttribute("SERVICE_REPORT").getObj());
            rsHistory.updateString("ESI_TERMS", (String) getAttribute("ESI_TERMS").getObj());
            rsHistory.updateString("TERMINATION_TERMS", (String) getAttribute("TERMINATION_TERMS").getObj());
            rsHistory.updateString("FILE_TEXT", (String) getAttribute("FILE_TEXT").getObj());
            rsHistory.updateString("AMOUNT_IN_WORDS", (String) getAttribute("AMOUNT_IN_WORDS").getObj());
            rsHistory.updateBoolean("DIRECTOR_APPROVAL", getAttribute("DIRECTOR_APPROVAL").getBool());
            rsHistory.updateBoolean("IMPORTED", getAttribute("IMPORTED").getBool());

            rsHistory.insertRow();

            //=== Delete old Records ======
            data.Execute("DELETE FROM D_PUR_AMEND_DETAIL WHERE COMPANY_ID=" + CompanyID + " AND AMEND_NO='" + AmendNo + "' AND PO_TYPE=" + POType);

            //====== Now turn of P.O. Items ======
            stItem = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsItem = stItem.executeQuery("SELECT * FROM D_PUR_AMEND_DETAIL WHERE AMEND_NO='1' ");
            rsItem.first();

            for (int i = 1; i <= colPOItems.size(); i++) {
                clsPOAmendItem ObjItem = (clsPOAmendItem) colPOItems.get(Integer.toString(i));

                rsItem.moveToInsertRow();
                rsItem.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
                rsItem.updateString("AMEND_NO", (String) getAttribute("AMEND_NO").getObj());
                rsItem.updateString("PO_NO", (String) getAttribute("PO_NO").getObj());
                rsItem.updateInt("SR_NO", i);
                rsItem.updateInt("PO_TYPE", POType);
                rsItem.updateString("ITEM_ID", (String) ObjItem.getAttribute("ITEM_ID").getObj());
                rsItem.updateString("VENDOR_SHADE", (String) ObjItem.getAttribute("VENDOR_SHADE").getObj());
                rsItem.updateString("SDML_SHADE", (String) ObjItem.getAttribute("SDML_SHADE").getObj());
                rsItem.updateString("ITEM_DESC", (String) ObjItem.getAttribute("ITEM_DESC").getObj());
                rsItem.updateString("HSN_SAC_CODE", (String) ObjItem.getAttribute("HSN_SAC_CODE").getObj());
                rsItem.updateString("MAKE", (String) ObjItem.getAttribute("MAKE").getObj());
                rsItem.updateString("PRICE_LIST_NO", (String) ObjItem.getAttribute("PRICE_LIST_NO").getObj());
                rsItem.updateString("PART_NO", (String) ObjItem.getAttribute("PART_NO").getObj());
                rsItem.updateString("EXCISE_TARRIF_NO", (String) ObjItem.getAttribute("EXCISE_TARRIF_NO").getObj());
                rsItem.updateDouble("QTY", ObjItem.getAttribute("QTY").getVal());
                rsItem.updateDouble("TOLERANCE_LIMIT", ObjItem.getAttribute("TOLERANCE_LIMIT").getVal());
                if (AStatus.equals("F")) {
                    rsItem.updateDouble("PENDING_QTY", ObjItem.getAttribute("QTY").getVal());
                }
                rsItem.updateInt("UNIT", (int) ObjItem.getAttribute("UNIT").getVal());

                rsItem.updateInt("DEPT_ID", (int) ObjItem.getAttribute("DEPT_ID").getVal());
                rsItem.updateDouble("RATE", ObjItem.getAttribute("RATE").getVal());
                rsItem.updateDouble("LANDED_RATE", ObjItem.getAttribute("LANDED_RATE").getVal());
                rsItem.updateDouble("DISC_PER", ObjItem.getAttribute("DISC_PER").getVal());
                rsItem.updateDouble("DISC_AMT", ObjItem.getAttribute("DISC_AMT").getVal());
                rsItem.updateDouble("TOTAL_AMOUNT", ObjItem.getAttribute("TOTAL_AMOUNT").getVal());
                rsItem.updateDouble("NET_AMOUNT", ObjItem.getAttribute("NET_AMOUNT").getVal());
                rsItem.updateString("INDENT_NO", (String) ObjItem.getAttribute("INDENT_NO").getObj());
                rsItem.updateInt("INDENT_SR_NO", (int) ObjItem.getAttribute("INDENT_SR_NO").getVal());
                rsItem.updateString("REFERENCE", (String) ObjItem.getAttribute("REFERENCE").getObj());
                rsItem.updateString("DELIVERY_DATE", (String) ObjItem.getAttribute("DELIVERY_DATE").getObj());
                rsItem.updateString("REMARKS", (String) ObjItem.getAttribute("REMARKS").getObj());
                //rsItem.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
                //rsItem.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsItem.updateLong("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
                rsItem.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsItem.updateInt("COLUMN_1_ID", (int) ObjItem.getAttribute("COLUMN_1_ID").getVal());
                rsItem.updateString("COLUMN_1_FORMULA", (String) ObjItem.getAttribute("COLUMN_1_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_1_PER", ObjItem.getAttribute("COLUMN_1_PER").getVal());
                rsItem.updateDouble("COLUMN_1_AMT", ObjItem.getAttribute("COLUMN_1_AMT").getVal());
                rsItem.updateString("COLUMN_1_CAPTION", (String) ObjItem.getAttribute("COLUMN_1_CAPTION").getObj());
                rsItem.updateInt("COLUMN_2_ID", (int) ObjItem.getAttribute("COLUMN_2_ID").getVal());
                rsItem.updateString("COLUMN_2_FORMULA", (String) ObjItem.getAttribute("COLUMN_2_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_2_PER", ObjItem.getAttribute("COLUMN_2_PER").getVal());
                rsItem.updateDouble("COLUMN_2_AMT", ObjItem.getAttribute("COLUMN_2_AMT").getVal());
                rsItem.updateString("COLUMN_2_CAPTION", (String) ObjItem.getAttribute("COLUMN_2_CAPTION").getObj());
                rsItem.updateInt("COLUMN_3_ID", (int) ObjItem.getAttribute("COLUMN_3_ID").getVal());
                rsItem.updateString("COLUMN_3_FORMULA", (String) ObjItem.getAttribute("COLUMN_3_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_3_PER", ObjItem.getAttribute("COLUMN_3_PER").getVal());
                rsItem.updateDouble("COLUMN_3_AMT", ObjItem.getAttribute("COLUMN_3_AMT").getVal());
                rsItem.updateString("COLUMN_3_CAPTION", (String) ObjItem.getAttribute("COLUMN_3_CAPTION").getObj());
                rsItem.updateInt("COLUMN_4_ID", (int) ObjItem.getAttribute("COLUMN_4_ID").getVal());
                rsItem.updateString("COLUMN_4_FORMULA", (String) ObjItem.getAttribute("COLUMN_4_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_4_PER", ObjItem.getAttribute("COLUMN_4_PER").getVal());
                rsItem.updateDouble("COLUMN_4_AMT", ObjItem.getAttribute("COLUMN_4_AMT").getVal());
                rsItem.updateString("COLUMN_4_CAPTION", (String) ObjItem.getAttribute("COLUMN_4_CAPTION").getObj());
                rsItem.updateInt("COLUMN_5_ID", (int) ObjItem.getAttribute("COLUMN_5_ID").getVal());
                rsItem.updateString("COLUMN_5_FORMULA", (String) ObjItem.getAttribute("COLUMN_5_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_5_PER", ObjItem.getAttribute("COLUMN_5_PER").getVal());
                rsItem.updateDouble("COLUMN_5_AMT", ObjItem.getAttribute("COLUMN_5_AMT").getVal());
                rsItem.updateString("COLUMN_5_CAPTION", (String) ObjItem.getAttribute("COLUMN_5_CAPTION").getObj());
                rsItem.updateInt("COLUMN_6_ID", (int) ObjItem.getAttribute("COLUMN_6_ID").getVal());
                rsItem.updateString("COLUMN_6_FORMULA", (String) ObjItem.getAttribute("COLUMN_6_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_6_PER", ObjItem.getAttribute("COLUMN_6_PER").getVal());
                rsItem.updateDouble("COLUMN_6_AMT", ObjItem.getAttribute("COLUMN_6_AMT").getVal());
                rsItem.updateString("COLUMN_6_CAPTION", (String) ObjItem.getAttribute("COLUMN_6_CAPTION").getObj());
                rsItem.updateInt("COLUMN_7_ID", (int) ObjItem.getAttribute("COLUMN_7_ID").getVal());
                rsItem.updateString("COLUMN_7_FORMULA", (String) ObjItem.getAttribute("COLUMN_7_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_7_PER", ObjItem.getAttribute("COLUMN_7_PER").getVal());
                rsItem.updateDouble("COLUMN_7_AMT", ObjItem.getAttribute("COLUMN_7_AMT").getVal());
                rsItem.updateString("COLUMN_7_CAPTION", (String) ObjItem.getAttribute("COLUMN_7_CAPTION").getObj());
                rsItem.updateInt("COLUMN_8_ID", (int) ObjItem.getAttribute("COLUMN_8_ID").getVal());
                rsItem.updateString("COLUMN_8_FORMULA", (String) ObjItem.getAttribute("COLUMN_8_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_8_PER", ObjItem.getAttribute("COLUMN_8_PER").getVal());
                rsItem.updateDouble("COLUMN_8_AMT", ObjItem.getAttribute("COLUMN_8_AMT").getVal());
                rsItem.updateString("COLUMN_8_CAPTION", (String) ObjItem.getAttribute("COLUMN_8_CAPTION").getObj());
                rsItem.updateInt("COLUMN_9_ID", (int) ObjItem.getAttribute("COLUMN_9_ID").getVal());
                rsItem.updateString("COLUMN_9_FORMULA", (String) ObjItem.getAttribute("COLUMN_9_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_9_PER", ObjItem.getAttribute("COLUMN_9_PER").getVal());
                rsItem.updateDouble("COLUMN_9_AMT", ObjItem.getAttribute("COLUMN_9_AMT").getVal());
                rsItem.updateString("COLUMN_9_CAPTION", (String) ObjItem.getAttribute("COLUMN_9_CAPTION").getObj());
                rsItem.updateInt("COLUMN_10_ID", (int) ObjItem.getAttribute("COLUMN_10_ID").getVal());
                rsItem.updateString("COLUMN_10_FORMULA", (String) ObjItem.getAttribute("COLUMN_10_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_10_PER", ObjItem.getAttribute("COLUMN_10_PER").getVal());
                rsItem.updateDouble("COLUMN_10_AMT", ObjItem.getAttribute("COLUMN_10_AMT").getVal());
                rsItem.updateString("COLUMN_10_CAPTION", (String) ObjItem.getAttribute("COLUMN_10_CAPTION").getObj());

                rsItem.updateInt("COLUMN_11_ID", (int) ObjItem.getAttribute("COLUMN_11_ID").getVal());
                rsItem.updateString("COLUMN_11_FORMULA", (String) ObjItem.getAttribute("COLUMN_11_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_11_PER", ObjItem.getAttribute("COLUMN_11_PER").getVal());
                rsItem.updateDouble("COLUMN_11_AMT", ObjItem.getAttribute("COLUMN_11_AMT").getVal());
                rsItem.updateString("COLUMN_11_CAPTION", (String) ObjItem.getAttribute("COLUMN_11_CAPTION").getObj());
                rsItem.updateInt("COLUMN_12_ID", (int) ObjItem.getAttribute("COLUMN_12_ID").getVal());
                rsItem.updateString("COLUMN_12_FORMULA", (String) ObjItem.getAttribute("COLUMN_12_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_12_PER", ObjItem.getAttribute("COLUMN_12_PER").getVal());
                rsItem.updateDouble("COLUMN_12_AMT", ObjItem.getAttribute("COLUMN_12_AMT").getVal());
                rsItem.updateString("COLUMN_12_CAPTION", (String) ObjItem.getAttribute("COLUMN_12_CAPTION").getObj());
                rsItem.updateInt("COLUMN_13_ID", (int) ObjItem.getAttribute("COLUMN_13_ID").getVal());
                rsItem.updateString("COLUMN_13_FORMULA", (String) ObjItem.getAttribute("COLUMN_13_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_13_PER", ObjItem.getAttribute("COLUMN_13_PER").getVal());
                rsItem.updateDouble("COLUMN_13_AMT", ObjItem.getAttribute("COLUMN_13_AMT").getVal());
                rsItem.updateString("COLUMN_13_CAPTION", (String) ObjItem.getAttribute("COLUMN_13_CAPTION").getObj());
                rsItem.updateInt("COLUMN_14_ID", (int) ObjItem.getAttribute("COLUMN_14_ID").getVal());
                rsItem.updateString("COLUMN_14_FORMULA", (String) ObjItem.getAttribute("COLUMN_14_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_14_PER", ObjItem.getAttribute("COLUMN_14_PER").getVal());
                rsItem.updateDouble("COLUMN_14_AMT", ObjItem.getAttribute("COLUMN_14_AMT").getVal());
                rsItem.updateString("COLUMN_14_CAPTION", (String) ObjItem.getAttribute("COLUMN_14_CAPTION").getObj());
                rsItem.updateInt("COLUMN_15_ID", (int) ObjItem.getAttribute("COLUMN_15_ID").getVal());
                rsItem.updateString("COLUMN_15_FORMULA", (String) ObjItem.getAttribute("COLUMN_15_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_15_PER", ObjItem.getAttribute("COLUMN_15_PER").getVal());
                rsItem.updateDouble("COLUMN_15_AMT", ObjItem.getAttribute("COLUMN_15_AMT").getVal());
                rsItem.updateString("COLUMN_15_CAPTION", (String) ObjItem.getAttribute("COLUMN_15_CAPTION").getObj());

                rsItem.updateInt("COLUMN_16_ID", (int) ObjItem.getAttribute("COLUMN_16_ID").getVal());
                rsItem.updateString("COLUMN_16_FORMULA", (String) ObjItem.getAttribute("COLUMN_16_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_16_PER", ObjItem.getAttribute("COLUMN_16_PER").getVal());
                rsItem.updateDouble("COLUMN_16_AMT", ObjItem.getAttribute("COLUMN_16_AMT").getVal());
                rsItem.updateString("COLUMN_16_CAPTION", (String) ObjItem.getAttribute("COLUMN_16_CAPTION").getObj());
                rsItem.updateInt("COLUMN_17_ID", (int) ObjItem.getAttribute("COLUMN_17_ID").getVal());
                rsItem.updateString("COLUMN_17_FORMULA", (String) ObjItem.getAttribute("COLUMN_17_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_17_PER", ObjItem.getAttribute("COLUMN_17_PER").getVal());
                rsItem.updateDouble("COLUMN_17_AMT", ObjItem.getAttribute("COLUMN_17_AMT").getVal());
                rsItem.updateString("COLUMN_17_CAPTION", (String) ObjItem.getAttribute("COLUMN_17_CAPTION").getObj());
                rsItem.updateInt("COLUMN_18_ID", (int) ObjItem.getAttribute("COLUMN_18_ID").getVal());
                rsItem.updateString("COLUMN_18_FORMULA", (String) ObjItem.getAttribute("COLUMN_18_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_18_PER", ObjItem.getAttribute("COLUMN_18_PER").getVal());
                rsItem.updateDouble("COLUMN_18_AMT", ObjItem.getAttribute("COLUMN_18_AMT").getVal());
                rsItem.updateString("COLUMN_18_CAPTION", (String) ObjItem.getAttribute("COLUMN_18_CAPTION").getObj());
                rsItem.updateInt("COLUMN_19_ID", (int) ObjItem.getAttribute("COLUMN_19_ID").getVal());
                rsItem.updateString("COLUMN_19_FORMULA", (String) ObjItem.getAttribute("COLUMN_19_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_19_PER", ObjItem.getAttribute("COLUMN_19_PER").getVal());
                rsItem.updateDouble("COLUMN_19_AMT", ObjItem.getAttribute("COLUMN_19_AMT").getVal());
                rsItem.updateString("COLUMN_19_CAPTION", (String) ObjItem.getAttribute("COLUMN_19_CAPTION").getObj());
                rsItem.updateInt("COLUMN_20_ID", (int) ObjItem.getAttribute("COLUMN_20_ID").getVal());
                rsItem.updateString("COLUMN_20_FORMULA", (String) ObjItem.getAttribute("COLUMN_20_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_20_PER", ObjItem.getAttribute("COLUMN_20_PER").getVal());
                rsItem.updateDouble("COLUMN_20_AMT", ObjItem.getAttribute("COLUMN_20_AMT").getVal());
                rsItem.updateString("COLUMN_20_CAPTION", (String) ObjItem.getAttribute("COLUMN_20_CAPTION").getObj());

                rsItem.updateInt("COLUMN_21_ID", (int) ObjItem.getAttribute("COLUMN_21_ID").getVal());
                rsItem.updateString("COLUMN_21_FORMULA", (String) ObjItem.getAttribute("COLUMN_21_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_21_PER", ObjItem.getAttribute("COLUMN_21_PER").getVal());
                rsItem.updateDouble("COLUMN_21_AMT", ObjItem.getAttribute("COLUMN_21_AMT").getVal());
                rsItem.updateString("COLUMN_21_CAPTION", (String) ObjItem.getAttribute("COLUMN_21_CAPTION").getObj());
                
                rsItem.updateBoolean("EXCISE_GATEPASS_GIVEN", ObjItem.getAttribute("EXCISE_GATEPASS_GIVEN").getBool());
                rsItem.updateBoolean("IMPORT_CONCESS", ObjItem.getAttribute("IMPORT_CONCESS").getBool());
                rsItem.updateString("QUOT_ID", (String) ObjItem.getAttribute("QUOT_ID").getObj());
                rsItem.updateInt("QUOT_SR_NO", (int) ObjItem.getAttribute("QUOT_SR_NO").getVal());
                rsItem.updateBoolean("CHANGED", true);
                rsItem.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsItem.insertRow();

                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO", RevNo);
                rsHDetail.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
                rsHDetail.updateString("AMEND_NO", (String) getAttribute("AMEND_NO").getObj());
                rsHDetail.updateString("PO_NO", (String) getAttribute("PO_NO").getObj());
                rsHDetail.updateInt("SR_NO", i);
                rsHDetail.updateInt("PO_TYPE", POType);
                rsHDetail.updateString("ITEM_ID", (String) ObjItem.getAttribute("ITEM_ID").getObj());
                rsHDetail.updateString("VENDOR_SHADE", (String) ObjItem.getAttribute("VENDOR_SHADE").getObj());
                rsHDetail.updateString("SDML_SHADE", (String) ObjItem.getAttribute("SDML_SHADE").getObj());
                rsHDetail.updateString("ITEM_DESC", (String) ObjItem.getAttribute("ITEM_DESC").getObj());
                rsHDetail.updateString("HSN_SAC_CODE", (String) ObjItem.getAttribute("HSN_SAC_CODE").getObj());
                rsHDetail.updateString("MAKE", (String) ObjItem.getAttribute("MAKE").getObj());
                rsHDetail.updateString("PRICE_LIST_NO", (String) ObjItem.getAttribute("PRICE_LIST_NO").getObj());
                rsHDetail.updateString("PART_NO", (String) ObjItem.getAttribute("PART_NO").getObj());
                rsHDetail.updateString("EXCISE_TARRIF_NO", (String) ObjItem.getAttribute("EXCISE_TARRIF_NO").getObj());
                rsHDetail.updateDouble("QTY", ObjItem.getAttribute("QTY").getVal());
                rsHDetail.updateDouble("TOLERANCE_LIMIT", ObjItem.getAttribute("TOLERANCE_LIMIT").getVal());
                rsHDetail.updateInt("UNIT", (int) ObjItem.getAttribute("UNIT").getVal());
                rsHDetail.updateInt("DEPT_ID", (int) ObjItem.getAttribute("DEPT_ID").getVal());
                rsHDetail.updateDouble("RATE", ObjItem.getAttribute("RATE").getVal());
                rsHDetail.updateDouble("LANDED_RATE", ObjItem.getAttribute("LANDED_RATE").getVal());
                rsHDetail.updateDouble("DISC_PER", ObjItem.getAttribute("DISC_PER").getVal());
                rsHDetail.updateDouble("DISC_AMT", ObjItem.getAttribute("DISC_AMT").getVal());
                rsHDetail.updateDouble("TOTAL_AMOUNT", ObjItem.getAttribute("TOTAL_AMOUNT").getVal());
                rsHDetail.updateDouble("NET_AMOUNT", ObjItem.getAttribute("NET_AMOUNT").getVal());
                rsHDetail.updateString("INDENT_NO", (String) ObjItem.getAttribute("INDENT_NO").getObj());
                rsHDetail.updateInt("INDENT_SR_NO", (int) ObjItem.getAttribute("INDENT_SR_NO").getVal());
                rsHDetail.updateString("REFERENCE", (String) ObjItem.getAttribute("REFERENCE").getObj());
                rsHDetail.updateString("DELIVERY_DATE", (String) ObjItem.getAttribute("DELIVERY_DATE").getObj());
                rsHDetail.updateString("REMARKS", (String) ObjItem.getAttribute("REMARKS").getObj());
                //rsHDetail.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
                //rsHDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
                rsHDetail.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateInt("COLUMN_1_ID", (int) ObjItem.getAttribute("COLUMN_1_ID").getVal());
                rsHDetail.updateString("COLUMN_1_FORMULA", (String) ObjItem.getAttribute("COLUMN_1_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_1_PER", ObjItem.getAttribute("COLUMN_1_PER").getVal());
                rsHDetail.updateDouble("COLUMN_1_AMT", ObjItem.getAttribute("COLUMN_1_AMT").getVal());
                rsHDetail.updateString("COLUMN_1_CAPTION", (String) ObjItem.getAttribute("COLUMN_1_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_2_ID", (int) ObjItem.getAttribute("COLUMN_2_ID").getVal());
                rsHDetail.updateString("COLUMN_2_FORMULA", (String) ObjItem.getAttribute("COLUMN_2_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_2_PER", ObjItem.getAttribute("COLUMN_2_PER").getVal());
                rsHDetail.updateDouble("COLUMN_2_AMT", ObjItem.getAttribute("COLUMN_2_AMT").getVal());
                rsHDetail.updateString("COLUMN_2_CAPTION", (String) ObjItem.getAttribute("COLUMN_2_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_3_ID", (int) ObjItem.getAttribute("COLUMN_3_ID").getVal());
                rsHDetail.updateString("COLUMN_3_FORMULA", (String) ObjItem.getAttribute("COLUMN_3_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_3_PER", ObjItem.getAttribute("COLUMN_3_PER").getVal());
                rsHDetail.updateDouble("COLUMN_3_AMT", ObjItem.getAttribute("COLUMN_3_AMT").getVal());
                rsHDetail.updateString("COLUMN_3_CAPTION", (String) ObjItem.getAttribute("COLUMN_3_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_4_ID", (int) ObjItem.getAttribute("COLUMN_4_ID").getVal());
                rsHDetail.updateString("COLUMN_4_FORMULA", (String) ObjItem.getAttribute("COLUMN_4_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_4_PER", ObjItem.getAttribute("COLUMN_4_PER").getVal());
                rsHDetail.updateDouble("COLUMN_4_AMT", ObjItem.getAttribute("COLUMN_4_AMT").getVal());
                rsHDetail.updateString("COLUMN_4_CAPTION", (String) ObjItem.getAttribute("COLUMN_4_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_5_ID", (int) ObjItem.getAttribute("COLUMN_5_ID").getVal());
                rsHDetail.updateString("COLUMN_5_FORMULA", (String) ObjItem.getAttribute("COLUMN_5_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_5_PER", ObjItem.getAttribute("COLUMN_5_PER").getVal());
                rsHDetail.updateDouble("COLUMN_5_AMT", ObjItem.getAttribute("COLUMN_5_AMT").getVal());
                rsHDetail.updateString("COLUMN_5_CAPTION", (String) ObjItem.getAttribute("COLUMN_5_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_6_ID", (int) ObjItem.getAttribute("COLUMN_6_ID").getVal());
                rsHDetail.updateString("COLUMN_6_FORMULA", (String) ObjItem.getAttribute("COLUMN_6_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_6_PER", ObjItem.getAttribute("COLUMN_6_PER").getVal());
                rsHDetail.updateDouble("COLUMN_6_AMT", ObjItem.getAttribute("COLUMN_6_AMT").getVal());
                rsHDetail.updateString("COLUMN_6_CAPTION", (String) ObjItem.getAttribute("COLUMN_6_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_7_ID", (int) ObjItem.getAttribute("COLUMN_7_ID").getVal());
                rsHDetail.updateString("COLUMN_7_FORMULA", (String) ObjItem.getAttribute("COLUMN_7_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_7_PER", ObjItem.getAttribute("COLUMN_7_PER").getVal());
                rsHDetail.updateDouble("COLUMN_7_AMT", ObjItem.getAttribute("COLUMN_7_AMT").getVal());
                rsHDetail.updateString("COLUMN_7_CAPTION", (String) ObjItem.getAttribute("COLUMN_7_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_8_ID", (int) ObjItem.getAttribute("COLUMN_8_ID").getVal());
                rsHDetail.updateString("COLUMN_8_FORMULA", (String) ObjItem.getAttribute("COLUMN_8_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_8_PER", ObjItem.getAttribute("COLUMN_8_PER").getVal());
                rsHDetail.updateDouble("COLUMN_8_AMT", ObjItem.getAttribute("COLUMN_8_AMT").getVal());
                rsHDetail.updateString("COLUMN_8_CAPTION", (String) ObjItem.getAttribute("COLUMN_8_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_9_ID", (int) ObjItem.getAttribute("COLUMN_9_ID").getVal());
                rsHDetail.updateString("COLUMN_9_FORMULA", (String) ObjItem.getAttribute("COLUMN_9_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_9_PER", ObjItem.getAttribute("COLUMN_9_PER").getVal());
                rsHDetail.updateDouble("COLUMN_9_AMT", ObjItem.getAttribute("COLUMN_9_AMT").getVal());
                rsHDetail.updateString("COLUMN_9_CAPTION", (String) ObjItem.getAttribute("COLUMN_9_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_10_ID", (int) ObjItem.getAttribute("COLUMN_10_ID").getVal());
                rsHDetail.updateString("COLUMN_10_FORMULA", (String) ObjItem.getAttribute("COLUMN_10_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_10_PER", ObjItem.getAttribute("COLUMN_10_PER").getVal());
                rsHDetail.updateDouble("COLUMN_10_AMT", ObjItem.getAttribute("COLUMN_10_AMT").getVal());
                rsHDetail.updateString("COLUMN_10_CAPTION", (String) ObjItem.getAttribute("COLUMN_10_CAPTION").getObj());

                rsHDetail.updateInt("COLUMN_11_ID", (int) ObjItem.getAttribute("COLUMN_11_ID").getVal());
                rsHDetail.updateString("COLUMN_11_FORMULA", (String) ObjItem.getAttribute("COLUMN_11_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_11_PER", ObjItem.getAttribute("COLUMN_11_PER").getVal());
                rsHDetail.updateDouble("COLUMN_11_AMT", ObjItem.getAttribute("COLUMN_11_AMT").getVal());
                rsHDetail.updateString("COLUMN_11_CAPTION", (String) ObjItem.getAttribute("COLUMN_11_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_12_ID", (int) ObjItem.getAttribute("COLUMN_12_ID").getVal());
                rsHDetail.updateString("COLUMN_12_FORMULA", (String) ObjItem.getAttribute("COLUMN_12_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_12_PER", ObjItem.getAttribute("COLUMN_12_PER").getVal());
                rsHDetail.updateDouble("COLUMN_12_AMT", ObjItem.getAttribute("COLUMN_12_AMT").getVal());
                rsHDetail.updateString("COLUMN_12_CAPTION", (String) ObjItem.getAttribute("COLUMN_12_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_13_ID", (int) ObjItem.getAttribute("COLUMN_13_ID").getVal());
                rsHDetail.updateString("COLUMN_13_FORMULA", (String) ObjItem.getAttribute("COLUMN_13_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_13_PER", ObjItem.getAttribute("COLUMN_13_PER").getVal());
                rsHDetail.updateDouble("COLUMN_13_AMT", ObjItem.getAttribute("COLUMN_13_AMT").getVal());
                rsHDetail.updateString("COLUMN_13_CAPTION", (String) ObjItem.getAttribute("COLUMN_13_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_14_ID", (int) ObjItem.getAttribute("COLUMN_14_ID").getVal());
                rsHDetail.updateString("COLUMN_14_FORMULA", (String) ObjItem.getAttribute("COLUMN_14_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_14_PER", ObjItem.getAttribute("COLUMN_14_PER").getVal());
                rsHDetail.updateDouble("COLUMN_14_AMT", ObjItem.getAttribute("COLUMN_14_AMT").getVal());
                rsHDetail.updateString("COLUMN_14_CAPTION", (String) ObjItem.getAttribute("COLUMN_14_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_15_ID", (int) ObjItem.getAttribute("COLUMN_15_ID").getVal());
                rsHDetail.updateString("COLUMN_15_FORMULA", (String) ObjItem.getAttribute("COLUMN_15_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_15_PER", ObjItem.getAttribute("COLUMN_15_PER").getVal());
                rsHDetail.updateDouble("COLUMN_15_AMT", ObjItem.getAttribute("COLUMN_15_AMT").getVal());
                rsHDetail.updateString("COLUMN_15_CAPTION", (String) ObjItem.getAttribute("COLUMN_15_CAPTION").getObj());

                rsHDetail.updateInt("COLUMN_16_ID", (int) ObjItem.getAttribute("COLUMN_16_ID").getVal());
                rsHDetail.updateString("COLUMN_16_FORMULA", (String) ObjItem.getAttribute("COLUMN_16_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_16_PER", ObjItem.getAttribute("COLUMN_16_PER").getVal());
                rsHDetail.updateDouble("COLUMN_16_AMT", ObjItem.getAttribute("COLUMN_16_AMT").getVal());
                rsHDetail.updateString("COLUMN_16_CAPTION", (String) ObjItem.getAttribute("COLUMN_16_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_17_ID", (int) ObjItem.getAttribute("COLUMN_17_ID").getVal());
                rsHDetail.updateString("COLUMN_17_FORMULA", (String) ObjItem.getAttribute("COLUMN_17_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_17_PER", ObjItem.getAttribute("COLUMN_17_PER").getVal());
                rsHDetail.updateDouble("COLUMN_17_AMT", ObjItem.getAttribute("COLUMN_17_AMT").getVal());
                rsHDetail.updateString("COLUMN_17_CAPTION", (String) ObjItem.getAttribute("COLUMN_17_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_18_ID", (int) ObjItem.getAttribute("COLUMN_18_ID").getVal());
                rsHDetail.updateString("COLUMN_18_FORMULA", (String) ObjItem.getAttribute("COLUMN_18_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_18_PER", ObjItem.getAttribute("COLUMN_18_PER").getVal());
                rsHDetail.updateDouble("COLUMN_18_AMT", ObjItem.getAttribute("COLUMN_18_AMT").getVal());
                rsHDetail.updateString("COLUMN_18_CAPTION", (String) ObjItem.getAttribute("COLUMN_18_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_19_ID", (int) ObjItem.getAttribute("COLUMN_19_ID").getVal());
                rsHDetail.updateString("COLUMN_19_FORMULA", (String) ObjItem.getAttribute("COLUMN_19_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_19_PER", ObjItem.getAttribute("COLUMN_19_PER").getVal());
                rsHDetail.updateDouble("COLUMN_19_AMT", ObjItem.getAttribute("COLUMN_19_AMT").getVal());
                rsHDetail.updateString("COLUMN_19_CAPTION", (String) ObjItem.getAttribute("COLUMN_19_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_20_ID", (int) ObjItem.getAttribute("COLUMN_20_ID").getVal());
                rsHDetail.updateString("COLUMN_20_FORMULA", (String) ObjItem.getAttribute("COLUMN_20_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_20_PER", ObjItem.getAttribute("COLUMN_20_PER").getVal());
                rsHDetail.updateDouble("COLUMN_20_AMT", ObjItem.getAttribute("COLUMN_20_AMT").getVal());
                rsHDetail.updateString("COLUMN_20_CAPTION", (String) ObjItem.getAttribute("COLUMN_20_CAPTION").getObj());

                rsHDetail.updateInt("COLUMN_21_ID", (int) ObjItem.getAttribute("COLUMN_21_ID").getVal());
                rsHDetail.updateString("COLUMN_21_FORMULA", (String) ObjItem.getAttribute("COLUMN_21_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_21_PER", ObjItem.getAttribute("COLUMN_21_PER").getVal());
                rsHDetail.updateDouble("COLUMN_21_AMT", ObjItem.getAttribute("COLUMN_21_AMT").getVal());
                rsHDetail.updateString("COLUMN_21_CAPTION", (String) ObjItem.getAttribute("COLUMN_21_CAPTION").getObj());
                
                rsHDetail.updateBoolean("EXCISE_GATEPASS_GIVEN", ObjItem.getAttribute("EXCISE_GATEPASS_GIVEN").getBool());
                rsHDetail.updateBoolean("IMPORT_CONCESS", ObjItem.getAttribute("IMPORT_CONCESS").getBool());
                rsHDetail.updateString("QUOT_ID", (String) ObjItem.getAttribute("QUOT_ID").getObj());
                rsHDetail.updateInt("QUOT_SR_NO", (int) ObjItem.getAttribute("QUOT_SR_NO").getVal());
                rsHDetail.updateBoolean("CHANGED", true);
                rsHDetail.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
            }

            //=== Delete old Records ======
            data.Execute("DELETE FROM D_PUR_AMEND_TERMS WHERE COMPANY_ID=" + CompanyID + " AND AMEND_NO='" + AmendNo + "' AND PO_TYPE=" + POType);

            //====== Now turn of P.O. Terms ======
            stTerms = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTerms = stTerms.executeQuery("SELECT * FROM D_PUR_AMEND_TERMS WHERE AMEND_NO='1' ");
            rsTerms.first();

            for (int i = 1; i <= colPOTerms.size(); i++) {
                clsPOTerms ObjItem = (clsPOTerms) colPOTerms.get(Integer.toString(i));
                rsTerms.moveToInsertRow();
                rsTerms.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
                rsTerms.updateString("AMEND_NO", (String) getAttribute("AMEND_NO").getObj());
                rsTerms.updateString("PO_NO", (String) getAttribute("PO_NO").getObj());
                rsTerms.updateInt("SR_NO", i);
                rsTerms.updateInt("PO_TYPE", POType);
                rsTerms.updateString("TERM_TYPE", (String) ObjItem.getAttribute("TERM_TYPE").getObj());
                rsTerms.updateInt("TERM_CODE", (int) ObjItem.getAttribute("TERM_CODE").getVal());
                rsTerms.updateString("TERM_DESC", (String) ObjItem.getAttribute("TERM_DESC").getObj());
                rsTerms.updateBoolean("CHANGED", true);
                rsTerms.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsTerms.insertRow();

                rsHTerms.moveToInsertRow();
                rsHTerms.updateInt("REVISION_NO", RevNo);
                rsHTerms.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
                rsHTerms.updateString("AMEND_NO", (String) getAttribute("AMEND_NO").getObj());
                rsHTerms.updateString("PO_NO", (String) getAttribute("PO_NO").getObj());
                rsHTerms.updateInt("SR_NO", i);
                rsHTerms.updateInt("PO_TYPE", POType);
                rsHTerms.updateString("TERM_TYPE", (String) ObjItem.getAttribute("TERM_TYPE").getObj());
                rsHTerms.updateInt("TERM_CODE", (int) ObjItem.getAttribute("TERM_CODE").getVal());
                rsHTerms.updateString("TERM_DESC", (String) ObjItem.getAttribute("TERM_DESC").getObj());
                rsHTerms.updateBoolean("CHANGED", true);
                rsHTerms.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHTerms.insertRow();
            }

            //======== Update the Approval Flow =========
            ObjFlow.CompanyID = (int) getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID = 27 + POType;

            if (POType == 8) {
                ObjFlow.ModuleID = 47;
            }

            //start add on 110909
            if (POType == 9) {
                ObjFlow.ModuleID = 168;
            }
            //end add on 110909

            if (ApprovalFlow.HierarchyUpdateNeeded(EITLERPGLOBAL.gCompanyID, ObjFlow.ModuleID, (String) getAttribute("AMEND_NO").getObj(), (int) getAttribute("HIERARCHY_ID").getVal(), OldHierarchy, EITLERPGLOBAL.gNewUserID, AStatus)) {

                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND DOC_NO='" + (String) getAttribute("AMEND_NO").getObj() + "' AND MODULE_ID=" + ObjFlow.ModuleID);

                ObjFlow.DocNo = (String) getAttribute("AMEND_NO").getObj();
                ObjFlow.From = (int) getAttribute("FROM").getVal();
                ObjFlow.To = (int) getAttribute("TO").getVal();
                ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
                ObjFlow.TableName = "D_PUR_AMEND_HEADER";
                ObjFlow.IsCreator = true;
                ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
                ObjFlow.Remarks = (String) getAttribute("FROM_REMARKS").getObj();
                ObjFlow.FieldName = "AMEND_NO";

                if (ObjFlow.Status.equals("H")) {
                    ObjFlow.Status = "A";
                    ObjFlow.To = ObjFlow.From;
                    ObjFlow.UpdateFlow();
                } else {
                    if (!ObjFlow.UpdateFlow()) {
                        LastError = ObjFlow.LastError;
                    }
                }

            } else {

                ObjFlow.DocNo = (String) getAttribute("AMEND_NO").getObj();
                ObjFlow.From = (int) getAttribute("FROM").getVal();
                ObjFlow.To = (int) getAttribute("TO").getVal();
                ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
                ObjFlow.TableName = "D_PUR_AMEND_HEADER";
                ObjFlow.IsCreator = false;
                ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
                ObjFlow.Remarks = (String) getAttribute("FROM_REMARKS").getObj();
                ObjFlow.FieldName = "AMEND_NO";

                //==== Handling Rejected Documents ==========//
                if (AStatus.equals("R")) {
                    //Remove the Rejected Flag First
                    //data.Execute("UPDATE D_INV_INDENT_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+ObjFlow.DocNo+"'");
                    //Remove Old Records from D_COM_DOC_DATA
                    //data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=3 AND DOC_NO='"+ObjFlow.DocNo+"'");

                    //ObjFlow.IsCreator=true;
                    ObjFlow.To = (int) getAttribute("SEND_DOC_TO").getVal();
                    ObjFlow.ExplicitSendTo = true;
                }
                //==========================================//

                //==== Handling Rejected Documents ==========//
                boolean IsRejected = getAttribute("REJECTED").getBool();

                if (IsRejected) {
                    //Remove the Rejected Flag First
                    data.Execute("UPDATE D_PUR_AMEND_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND AMEND_NO='" + ObjFlow.DocNo + "' AND PO_TYPE=" + POType);
                    //Remove Old Records from D_COM_DOC_DATA
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + (ObjFlow.ModuleID) + " AND DOC_NO='" + ObjFlow.DocNo + "'");

                    ObjFlow.IsCreator = true;
                }
                //==========================================//

                if (ObjFlow.Status.equals("H")) {
                    //Do nothing
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
                //--------- Approval Flow Update complete -----------

            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }

    private void RevertStockEffect() {
        Statement stItemMaster, stItem, stTmp;
        ResultSet rsItemMaster, rsItem, rsTmp;
        String strSQL = "", PONo = "", IndentNo = "";
        int CompanyID = 0, IndentSrNo = 0;
        double Qty = 0;

        try {
            CompanyID = (int) getAttribute("COMPANY_ID").getVal();
            PONo = (String) getAttribute("PO_NO").getObj();

            //Now give reverse effect to Indent Table
            strSQL = "SELECT * FROM D_PUR_PO_DETAIL WHERE COMPANY_ID=" + CompanyID + " AND PO_NO='" + PONo + "' AND PO_TYPE=" + POType;

            stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery(strSQL);
            rsTmp.first();

            while (!rsTmp.isAfterLast()) {
                IndentNo = rsTmp.getString("INDENT_NO");
                IndentSrNo = rsTmp.getInt("INDENT_SR_NO");
                Qty = rsTmp.getDouble("QTY");

                // Update GRN Received Qty
                data.Execute("UPDATE D_INV_INDENT_DETAIL SET PO_QTY=PO_QTY-" + Qty + ",BAL_QTY=QTY-PO_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + CompanyID + " AND INDENT_NO='" + IndentNo.trim() + "' AND SR_NO=" + IndentSrNo + " ");

                rsTmp.next();
            }
        } catch (Exception e) {
        }
    }

    //Deletes current record
    public boolean Delete(int pUserID) {
        try {
            int lCompanyID = (int) getAttribute("COMPANY_ID").getVal();
            String lDocNo = (String) getAttribute("AMEND_NO").getObj();
            String strSQL = "", PONo = "", IndentNo = "", AmendNo = "";
            Statement stTmp;
            ResultSet rsTmp;
            int IndentSrNo = 0;
            double Qty = 0;

            //First check that record is deletable
            if (CanDelete(lCompanyID, lDocNo, pUserID)) {

                //Give reverse effect to Stock
                PONo = (String) getAttribute("PO_NO").getObj();
                AmendNo = (String) getAttribute("AMEND_NO").getObj();

                //--- Reverse the Effect of last P.O. --- //
                strSQL = "SELECT * FROM D_PUR_AMEND_DETAIL WHERE COMPANY_ID=" + lCompanyID + " AND PO_NO='" + PONo + "' AND PO_TYPE=" + POType + " AND LAST_AMENDMENT=1";

                stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                rsTmp = stTmp.executeQuery(strSQL);
                rsTmp.first();

                while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {
                    IndentNo = rsTmp.getString("INDENT_NO");
                    IndentSrNo = rsTmp.getInt("INDENT_SR_NO");
                    Qty = rsTmp.getDouble("QTY");

                    // Update GRN Received Qty
                    data.Execute("UPDATE D_INV_INDENT_DETAIL SET PO_QTY=PO_QTY-" + Qty + ",BAL_QTY=QTY-PO_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + lCompanyID + " AND INDENT_NO='" + IndentNo.trim() + "' AND SR_NO=" + IndentSrNo + " ");

                    rsTmp.next();
                }
                //---------------------------------------//

                data.Execute("DELETE FROM D_PUR_AMEND_HEADER WHERE COMPANY_ID=" + lCompanyID + " AND AMEND_NO='" + lDocNo.trim() + "' AND PO_TYPE=" + POType);
                data.Execute("DELETE FROM D_PUR_AMEND_DETAIL WHERE COMPANY_ID=" + lCompanyID + " AND AMEND_NO='" + lDocNo.trim() + "' AND PO_TYPE=" + POType);

                LoadData(lCompanyID, POType);
                return true;
            } else {
                LastError = "Record cannot be deleted. Either it is Approved/Rejected. Only creator of the document can delete.";
                return false;
            }
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public Object getObject(int pCompanyID, String pAmendNo) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND AMEND_NO='" + pAmendNo + "' AND PO_TYPE=" + POType;
        clsPOAmendGen ObjAmendPO = new clsPOAmendGen();
        ObjAmendPO.Filter(strCondition, pCompanyID);
        return ObjAmendPO;
    }

    public Object getObject(int pCompanyID, String pPONo, int pType, String dbURL) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND PO_NO='" + pPONo + "' AND PO_TYPE=" + pType + " AND LAST_AMENDMENT=1";
        clsPOAmendGen ObjAmendPO = new clsPOAmendGen();
        ObjAmendPO.LoadData(pCompanyID, pType, dbURL);
        ObjAmendPO.Filter(strCondition, pCompanyID, pType, dbURL);
        return ObjAmendPO;
    }

    public boolean Filter(String pCondition, int pCompanyID) {
        Ready = false;
        try {
            String strSql = "SELECT * FROM D_PUR_AMEND_HEADER " + pCondition;
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery(strSql);

            if (!rsResultSet.first()) {
                //strSql = "SELECT * FROM D_PUR_AMEND_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND PO_TYPE="+POType+" AND AMEND_NO='P000006/1' ORDER BY AMEND_NO"; //AND AMEND_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND AMEND_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'
                rsResultSet = Stmt.executeQuery(strSql);
                Ready = true;
                MoveFirst();
                return false;
            } else {
                Ready = true;
                MoveFirst();
                return true;
            }
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean Filter(String pCondition, int pCompanyID, int pType, String dbURL) {
        Ready = false;
        try {
            String strSql = "SELECT * FROM D_PUR_AMEND_HEADER " + pCondition;
            Conn = data.getConn(dbURL);
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery(strSql);

            if (!rsResultSet.first()) {
                strSql = "SELECT * FROM D_PUR_AMEND_HEADER WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND PO_TYPE=" + pType + " ORDER BY AMEND_NO";
                rsResultSet = Stmt.executeQuery(strSql);
                Ready = true;
                MoveFirst();
                return false;
            } else {
                Ready = true;
                MoveFirst();
                return true;
            }
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean setData() {
        Statement stItem, stTmp;
        ResultSet rsItem, rsTmp;
        String PONo = "", AmendNo = "";
        int CompanyID = 0, ItemCounter = 0, SrNo = 0;
        int RevNo = 0;

        CompanyID = (int) getAttribute("COMPANY_ID").getVal();

        try {
            if (HistoryView) {
                RevNo = rsResultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO", rsResultSet.getInt("REVISION_NO"));
            } else {
                setAttribute("REVISION_NO", 0);
            }

            setAttribute("COMPANY_ID", rsResultSet.getInt("COMPANY_ID"));
            setAttribute("AMEND_NO", rsResultSet.getString("AMEND_NO"));
            setAttribute("DOC_ID", rsResultSet.getLong("DOC_ID"));
            setAttribute("AMEND_DATE", rsResultSet.getString("AMEND_DATE"));
            setAttribute("AMEND_SR_NO", rsResultSet.getInt("AMEND_SR_NO"));
            setAttribute("LAST_AMENDMENT", rsResultSet.getBoolean("LAST_AMENDMENT"));
            setAttribute("PO_NO", rsResultSet.getString("PO_NO"));
            setAttribute("PO_DATE", rsResultSet.getString("PO_DATE"));
            setAttribute("PO_TYPE", rsResultSet.getInt("PO_TYPE"));
            setAttribute("PO_REF", rsResultSet.getString("PO_REF"));
            setAttribute("SUPP_ID", rsResultSet.getString("SUPP_ID"));
            setAttribute("SUPP_NAME", rsResultSet.getString("SUPP_NAME"));
            setAttribute("REF_A", rsResultSet.getString("REF_A"));
            setAttribute("REF_B", rsResultSet.getString("REF_B"));
            setAttribute("QUOTATION_NO", rsResultSet.getString("QUOTATION_NO"));
            setAttribute("QUOTATION_DATE", rsResultSet.getString("QUOTATION_DATE"));
            setAttribute("INQUIRY_NO", rsResultSet.getString("INQUIRY_NO"));
            setAttribute("INQUIRY_DATE", rsResultSet.getString("INQUIRY_DATE"));
            setAttribute("BUYER", rsResultSet.getInt("BUYER"));
            setAttribute("TRANSPORT_MODE", rsResultSet.getInt("TRANSPORT_MODE"));
            setAttribute("PURPOSE", rsResultSet.getString("PURPOSE"));
            setAttribute("SUBJECT", rsResultSet.getString("SUBJECT"));
            setAttribute("CURRENCY_ID", rsResultSet.getInt("CURRENCY_ID"));
            setAttribute("CURRENCY_RATE", rsResultSet.getDouble("CURRENCY_RATE"));
            setAttribute("TOTAL_AMOUNT", rsResultSet.getDouble("TOTAL_AMOUNT"));
            setAttribute("NET_AMOUNT", rsResultSet.getDouble("NET_AMOUNT"));
            setAttribute("STATUS", rsResultSet.getString("STATUS"));
            setAttribute("ATTACHEMENT", rsResultSet.getBoolean("ATTACHEMENT"));
            setAttribute("ATTACHEMENT_PATH", rsResultSet.getString("ATTACHEMENT_PATH"));
            setAttribute("REMARKS", rsResultSet.getString("REMARKS"));
            setAttribute("SHIP_ID", rsResultSet.getInt("SHIP_ID"));
            setAttribute("CANCELLED", rsResultSet.getInt("CANCELLED"));
            setAttribute("HIERARCHY_ID", rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("CREATED_BY", rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE", rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY", rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE", rsResultSet.getString("MODIFIED_DATE"));
            setAttribute("COLUMN_1_ID", rsResultSet.getInt("COLUMN_1_ID"));
            setAttribute("COLUMN_1_FORMULA", rsResultSet.getString("COLUMN_1_FORMULA"));
            setAttribute("COLUMN_1_PER", rsResultSet.getDouble("COLUMN_1_PER"));
            setAttribute("COLUMN_1_AMT", rsResultSet.getDouble("COLUMN_1_AMT"));
            setAttribute("COLUMN_1_CAPTION", rsResultSet.getString("COLUMN_1_CAPTION"));
            setAttribute("COLUMN_2_ID", rsResultSet.getInt("COLUMN_2_ID"));
            setAttribute("COLUMN_2_FORMULA", rsResultSet.getString("COLUMN_2_FORMULA"));
            setAttribute("COLUMN_2_PER", rsResultSet.getDouble("COLUMN_2_PER"));
            setAttribute("COLUMN_2_AMT", rsResultSet.getDouble("COLUMN_2_AMT"));
            setAttribute("COLUMN_2_CAPTION", rsResultSet.getString("COLUMN_2_CAPTION"));
            setAttribute("COLUMN_3_ID", rsResultSet.getInt("COLUMN_3_ID"));
            setAttribute("COLUMN_3_FORMULA", rsResultSet.getString("COLUMN_3_FORMULA"));
            setAttribute("COLUMN_3_PER", rsResultSet.getDouble("COLUMN_3_PER"));
            setAttribute("COLUMN_3_AMT", rsResultSet.getDouble("COLUMN_3_AMT"));
            setAttribute("COLUMN_3_CAPTION", rsResultSet.getString("COLUMN_3_CAPTION"));
            setAttribute("COLUMN_4_ID", rsResultSet.getInt("COLUMN_4_ID"));
            setAttribute("COLUMN_4_FORMULA", rsResultSet.getString("COLUMN_4_FORMULA"));
            setAttribute("COLUMN_4_PER", rsResultSet.getDouble("COLUMN_4_PER"));
            setAttribute("COLUMN_4_AMT", rsResultSet.getDouble("COLUMN_4_AMT"));
            setAttribute("COLUMN_4_CAPTION", rsResultSet.getString("COLUMN_4_CAPTION"));
            setAttribute("COLUMN_5_ID", rsResultSet.getInt("COLUMN_5_ID"));
            setAttribute("COLUMN_5_FORMULA", rsResultSet.getString("COLUMN_5_FORMULA"));
            setAttribute("COLUMN_5_PER", rsResultSet.getDouble("COLUMN_5_PER"));
            setAttribute("COLUMN_5_AMT", rsResultSet.getDouble("COLUMN_5_AMT"));
            setAttribute("COLUMN_5_CAPTION", rsResultSet.getString("COLUMN_5_CAPTION"));
            setAttribute("COLUMN_6_ID", rsResultSet.getInt("COLUMN_6_ID"));
            setAttribute("COLUMN_6_FORMULA", rsResultSet.getString("COLUMN_6_FORMULA"));
            setAttribute("COLUMN_6_PER", rsResultSet.getDouble("COLUMN_6_PER"));
            setAttribute("COLUMN_6_AMT", rsResultSet.getDouble("COLUMN_6_AMT"));
            setAttribute("COLUMN_6_CAPTION", rsResultSet.getString("COLUMN_6_CAPTION"));
            setAttribute("COLUMN_7_ID", rsResultSet.getInt("COLUMN_7_ID"));
            setAttribute("COLUMN_7_FORMULA", rsResultSet.getString("COLUMN_7_FORMULA"));
            setAttribute("COLUMN_7_PER", rsResultSet.getDouble("COLUMN_7_PER"));
            setAttribute("COLUMN_7_AMT", rsResultSet.getDouble("COLUMN_7_AMT"));
            setAttribute("COLUMN_7_CAPTION", rsResultSet.getString("COLUMN_7_CAPTION"));
            setAttribute("COLUMN_8_ID", rsResultSet.getInt("COLUMN_8_ID"));
            setAttribute("COLUMN_8_FORMULA", rsResultSet.getString("COLUMN_8_FORMULA"));
            setAttribute("COLUMN_8_PER", rsResultSet.getDouble("COLUMN_8_PER"));
            setAttribute("COLUMN_8_AMT", rsResultSet.getDouble("COLUMN_8_AMT"));
            setAttribute("COLUMN_8_CAPTION", rsResultSet.getString("COLUMN_8_CAPTION"));
            setAttribute("COLUMN_9_ID", rsResultSet.getInt("COLUMN_9_ID"));
            setAttribute("COLUMN_9_FORMULA", rsResultSet.getString("COLUMN_9_FORMULA"));
            setAttribute("COLUMN_9_PER", rsResultSet.getDouble("COLUMN_9_PER"));
            setAttribute("COLUMN_9_AMT", rsResultSet.getDouble("COLUMN_9_AMT"));
            setAttribute("COLUMN_9_CAPTION", rsResultSet.getString("COLUMN_9_CAPTION"));
            setAttribute("COLUMN_10_ID", rsResultSet.getInt("COLUMN_10_ID"));
            setAttribute("COLUMN_10_FORMULA", rsResultSet.getString("COLUMN_10_FORMULA"));
            setAttribute("COLUMN_10_PER", rsResultSet.getDouble("COLUMN_10_PER"));
            setAttribute("COLUMN_10_AMT", rsResultSet.getDouble("COLUMN_10_AMT"));
            setAttribute("COLUMN_10_CAPTION", rsResultSet.getString("COLUMN_10_CAPTION"));

            setAttribute("COLUMN_11_ID", rsResultSet.getInt("COLUMN_11_ID"));
            setAttribute("COLUMN_11_FORMULA", rsResultSet.getString("COLUMN_11_FORMULA"));
            setAttribute("COLUMN_11_PER", rsResultSet.getDouble("COLUMN_11_PER"));
            setAttribute("COLUMN_11_AMT", rsResultSet.getDouble("COLUMN_11_AMT"));
            setAttribute("COLUMN_11_CAPTION", rsResultSet.getString("COLUMN_11_CAPTION"));
            setAttribute("COLUMN_12_ID", rsResultSet.getInt("COLUMN_12_ID"));
            setAttribute("COLUMN_12_FORMULA", rsResultSet.getString("COLUMN_12_FORMULA"));
            setAttribute("COLUMN_12_PER", rsResultSet.getDouble("COLUMN_12_PER"));
            setAttribute("COLUMN_12_AMT", rsResultSet.getDouble("COLUMN_12_AMT"));
            setAttribute("COLUMN_12_CAPTION", rsResultSet.getString("COLUMN_12_CAPTION"));
            setAttribute("COLUMN_13_ID", rsResultSet.getInt("COLUMN_13_ID"));
            setAttribute("COLUMN_13_FORMULA", rsResultSet.getString("COLUMN_13_FORMULA"));
            setAttribute("COLUMN_13_PER", rsResultSet.getDouble("COLUMN_13_PER"));
            setAttribute("COLUMN_13_AMT", rsResultSet.getDouble("COLUMN_13_AMT"));
            setAttribute("COLUMN_13_CAPTION", rsResultSet.getString("COLUMN_13_CAPTION"));
            setAttribute("COLUMN_14_ID", rsResultSet.getInt("COLUMN_14_ID"));
            setAttribute("COLUMN_14_FORMULA", rsResultSet.getString("COLUMN_14_FORMULA"));
            setAttribute("COLUMN_14_PER", rsResultSet.getDouble("COLUMN_14_PER"));
            setAttribute("COLUMN_14_AMT", rsResultSet.getDouble("COLUMN_14_AMT"));
            setAttribute("COLUMN_14_CAPTION", rsResultSet.getString("COLUMN_14_CAPTION"));
            setAttribute("COLUMN_15_ID", rsResultSet.getInt("COLUMN_15_ID"));
            setAttribute("COLUMN_15_FORMULA", rsResultSet.getString("COLUMN_15_FORMULA"));
            setAttribute("COLUMN_15_PER", rsResultSet.getDouble("COLUMN_15_PER"));
            setAttribute("COLUMN_15_AMT", rsResultSet.getDouble("COLUMN_15_AMT"));
            setAttribute("COLUMN_15_CAPTION", rsResultSet.getString("COLUMN_15_CAPTION"));

            setAttribute("COLUMN_16_ID", rsResultSet.getInt("COLUMN_16_ID"));
            setAttribute("COLUMN_16_FORMULA", rsResultSet.getString("COLUMN_16_FORMULA"));
            setAttribute("COLUMN_16_PER", rsResultSet.getDouble("COLUMN_16_PER"));
            setAttribute("COLUMN_16_AMT", rsResultSet.getDouble("COLUMN_16_AMT"));
            setAttribute("COLUMN_16_CAPTION", rsResultSet.getString("COLUMN_16_CAPTION"));
            setAttribute("COLUMN_17_ID", rsResultSet.getInt("COLUMN_17_ID"));
            setAttribute("COLUMN_17_FORMULA", rsResultSet.getString("COLUMN_17_FORMULA"));
            setAttribute("COLUMN_17_PER", rsResultSet.getDouble("COLUMN_17_PER"));
            setAttribute("COLUMN_17_AMT", rsResultSet.getDouble("COLUMN_17_AMT"));
            setAttribute("COLUMN_17_CAPTION", rsResultSet.getString("COLUMN_17_CAPTION"));
            setAttribute("COLUMN_18_ID", rsResultSet.getInt("COLUMN_18_ID"));
            setAttribute("COLUMN_18_FORMULA", rsResultSet.getString("COLUMN_18_FORMULA"));
            setAttribute("COLUMN_18_PER", rsResultSet.getDouble("COLUMN_18_PER"));
            setAttribute("COLUMN_18_AMT", rsResultSet.getDouble("COLUMN_18_AMT"));
            setAttribute("COLUMN_18_CAPTION", rsResultSet.getString("COLUMN_18_CAPTION"));
            setAttribute("COLUMN_19_ID", rsResultSet.getInt("COLUMN_19_ID"));
            setAttribute("COLUMN_19_FORMULA", rsResultSet.getString("COLUMN_19_FORMULA"));
            setAttribute("COLUMN_19_PER", rsResultSet.getDouble("COLUMN_19_PER"));
            setAttribute("COLUMN_19_AMT", rsResultSet.getDouble("COLUMN_19_AMT"));
            setAttribute("COLUMN_19_CAPTION", rsResultSet.getString("COLUMN_19_CAPTION"));
            setAttribute("COLUMN_20_ID", rsResultSet.getInt("COLUMN_20_ID"));
            setAttribute("COLUMN_20_FORMULA", rsResultSet.getString("COLUMN_20_FORMULA"));
            setAttribute("COLUMN_20_PER", rsResultSet.getDouble("COLUMN_20_PER"));
            setAttribute("COLUMN_20_AMT", rsResultSet.getDouble("COLUMN_20_AMT"));
            setAttribute("COLUMN_20_CAPTION", rsResultSet.getString("COLUMN_20_CAPTION"));

            setAttribute("COLUMN_21_ID", rsResultSet.getInt("COLUMN_21_ID"));
            setAttribute("COLUMN_21_FORMULA", rsResultSet.getString("COLUMN_21_FORMULA"));
            setAttribute("COLUMN_21_PER", rsResultSet.getDouble("COLUMN_21_PER"));
            setAttribute("COLUMN_21_AMT", rsResultSet.getDouble("COLUMN_21_AMT"));
            setAttribute("COLUMN_21_CAPTION", rsResultSet.getString("COLUMN_21_CAPTION"));
            
            setAttribute("REASON_CODE", rsResultSet.getInt("REASON_CODE"));
            setAttribute("AMEND_REASON", rsResultSet.getString("AMEND_REASON"));
            setAttribute("IMPORT_CONCESS", rsResultSet.getBoolean("IMPORT_CONCESS"));
            setAttribute("PRINT_LINE_1", rsResultSet.getString("PRINT_LINE_1"));
            setAttribute("PRINT_LINE_2", rsResultSet.getString("PRINT_LINE_2"));
            setAttribute("IMPORT_LICENSE", rsResultSet.getString("IMPORT_LICENSE"));
            setAttribute("PAYMENT_TERM", rsResultSet.getString("PAYMENT_TERM"));

            setAttribute("PAYMENT_CODE", rsResultSet.getInt("PAYMENT_CODE"));
            setAttribute("CR_DAYS", rsResultSet.getInt("CR_DAYS"));
            setAttribute("DEPT_ID", rsResultSet.getInt("DEPT_ID"));

            setAttribute("PRICE_BASIS_TERM", rsResultSet.getString("PRICE_BASIS_TERM"));
            setAttribute("DISCOUNT_TERM", rsResultSet.getString("DISCOUNT_TERM"));
            setAttribute("EXCISE_TERM", rsResultSet.getString("EXCISE_TERM"));
            setAttribute("ST_TERM", rsResultSet.getString("ST_TERM"));
            setAttribute("PF_TERM", rsResultSet.getString("PF_TERM"));
            setAttribute("FREIGHT_TERM", rsResultSet.getString("FREIGHT_TERM"));
            setAttribute("OCTROI_TERM", rsResultSet.getString("OCTROI_TERM"));
            setAttribute("FOB_TERM", rsResultSet.getString("FOB_TERM"));
            setAttribute("CIE_TERM", rsResultSet.getString("CIE_TERM"));
            setAttribute("INSURANCE_TERM", rsResultSet.getString("INSURANCE_TERM"));
            setAttribute("TCC_TERM", rsResultSet.getString("TCC_TERM"));
            setAttribute("CENVAT_TERM", rsResultSet.getString("CENVAT_TERM"));
            setAttribute("DESPATCH_TERM", rsResultSet.getString("DESPATCH_TERM"));
            setAttribute("SERVICE_TAX_TERM", rsResultSet.getString("SERVICE_TAX_TERM"));
            setAttribute("OTHERS_TERM", rsResultSet.getString("OTHERS_TERM"));
            setAttribute("COVERING_TEXT", rsResultSet.getString("COVERING_TEXT"));
            
            setAttribute("CGST_TERM", rsResultSet.getString("CGST_TERM"));
            setAttribute("SGST_TERM", rsResultSet.getString("SGST_TERM"));
            setAttribute("IGST_TERM", rsResultSet.getString("IGST_TERM"));
            setAttribute("COMPOSITION_TERM", rsResultSet.getString("COMPOSITION_TERM"));
            setAttribute("RCM_TERM", rsResultSet.getString("RCM_TERM"));
            setAttribute("GST_COMPENSATION_CESS_TERM", rsResultSet.getString("GST_COMPENSATION_CESS_TERM"));
            
            setAttribute("PREMISES_TYPE", rsResultSet.getString("PREMISES_TYPE"));
            setAttribute("SCOPE", rsResultSet.getString("SCOPE"));
            setAttribute("SERVICE_PERIOD", rsResultSet.getString("SERVICE_PERIOD"));
            setAttribute("SERVICE_FREQUENCY", rsResultSet.getString("SERVICE_FREQUENCY"));
            setAttribute("CONTRACT_DETAILS", rsResultSet.getString("CONTRACT_DETAILS"));
            setAttribute("SERVICE_REPORT", rsResultSet.getString("SERVICE_REPORT"));
            setAttribute("ESI_TERMS", rsResultSet.getString("ESI_TERMS"));
            setAttribute("TERMINATION_TERMS", rsResultSet.getString("TERMINATION_TERMS"));
            setAttribute("FILE_TEXT", rsResultSet.getString("FILE_TEXT"));
            setAttribute("AMOUNT_IN_WORDS", rsResultSet.getString("AMOUNT_IN_WORDS"));
            setAttribute("DIRECTOR_APPROVAL", rsResultSet.getBoolean("DIRECTOR_APPROVAL"));
            setAttribute("IMPORTED", rsResultSet.getBoolean("IMPORTED"));

            setAttribute("APPROVED", rsResultSet.getInt("APPROVED"));
            setAttribute("CANCELLED", rsResultSet.getInt("CANCELLED"));

            colPOItems.clear();

            AmendNo = (String) getAttribute("AMEND_NO").getObj();

            stItem = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            if (HistoryView) {
                rsItem = stItem.executeQuery("SELECT * FROM D_PUR_AMEND_DETAIL_H WHERE COMPANY_ID=" + CompanyID + " AND AMEND_NO='" + AmendNo + "' AND PO_TYPE=" + POType + " AND REVISION_NO=" + RevNo + " ORDER BY SR_NO");
            } else {
                rsItem = stItem.executeQuery("SELECT * FROM D_PUR_AMEND_DETAIL WHERE COMPANY_ID=" + CompanyID + " AND AMEND_NO='" + AmendNo + "' AND PO_TYPE=" + POType + " ORDER BY SR_NO");
            }
            rsItem.first();

            ItemCounter = 0;

            while ((!rsItem.isAfterLast()) && (rsItem.getRow() > 0)) {
                ItemCounter = ItemCounter + 1;

                SrNo = rsItem.getInt("SR_NO"); //Sr. No. of Item Detail
                clsPOAmendItem ObjItem = new clsPOAmendItem();
                ObjItem.setAttribute("COMPANY_ID", rsItem.getInt("COMPANY_ID"));
                ObjItem.setAttribute("AMEND_NO", rsItem.getString("AMEND_NO"));
                ObjItem.setAttribute("LAST_AMENDMENT", rsItem.getBoolean("LAST_AMENDMENT"));
                ObjItem.setAttribute("PO_NO", rsItem.getString("PO_NO"));
                ObjItem.setAttribute("PO_TYPE", rsItem.getInt("PO_TYPE"));
                ObjItem.setAttribute("SR_NO", rsItem.getInt("SR_NO"));
                ObjItem.setAttribute("ITEM_ID", rsItem.getString("ITEM_ID"));
                ObjItem.setAttribute("VENDOR_SHADE", rsItem.getString("VENDOR_SHADE"));
                ObjItem.setAttribute("SDML_SHADE", rsItem.getString("SDML_SHADE"));
                ObjItem.setAttribute("ITEM_DESC", UtilFunctions.getString(rsItem, "ITEM_DESC", ""));
                ObjItem.setAttribute("HSN_SAC_CODE", UtilFunctions.getString(rsItem, "HSN_SAC_CODE", ""));
                ObjItem.setAttribute("MAKE", UtilFunctions.getString(rsItem, "MAKE", ""));
                ObjItem.setAttribute("PRICE_LIST_NO", rsItem.getString("PRICE_LIST_NO"));
                ObjItem.setAttribute("PART_NO", rsItem.getString("PART_NO"));
                ObjItem.setAttribute("EXCISE_TARRIF_NO", rsItem.getString("EXCISE_TARRIF_NO"));
                ObjItem.setAttribute("QTY", rsItem.getDouble("QTY"));
                ObjItem.setAttribute("TOLERANCE_LIMIT", rsItem.getDouble("TOLERANCE_LIMIT"));
                ObjItem.setAttribute("UNIT", rsItem.getInt("UNIT"));
                ObjItem.setAttribute("DEPT_ID", rsItem.getInt("DEPT_ID"));
                ObjItem.setAttribute("RATE", rsItem.getDouble("RATE"));
                ObjItem.setAttribute("LANDED_RATE", rsItem.getDouble("LANDED_RATE"));
                ObjItem.setAttribute("RECD_QTY", rsItem.getDouble("RECD_QTY"));
                ObjItem.setAttribute("PENDING_QTY", rsItem.getDouble("PENDING_QTY"));
                ObjItem.setAttribute("DISC_PER", rsItem.getDouble("DISC_PER"));
                ObjItem.setAttribute("DISC_AMT", rsItem.getDouble("DISC_AMT"));
                ObjItem.setAttribute("TOTAL_AMOUNT", rsItem.getDouble("TOTAL_AMOUNT"));
                ObjItem.setAttribute("NET_AMOUNT", rsItem.getDouble("NET_AMOUNT"));
                ObjItem.setAttribute("INDENT_NO", rsItem.getString("INDENT_NO"));
                ObjItem.setAttribute("INDENT_SR_NO", rsItem.getInt("INDENT_SR_NO"));
                ObjItem.setAttribute("REFERENCE", rsItem.getString("REFERENCE"));
                ObjItem.setAttribute("DELIVERY_DATE", rsItem.getString("DELIVERY_DATE"));
                ObjItem.setAttribute("REMARKS", rsItem.getString("REMARKS"));
                ObjItem.setAttribute("CREATED_BY", rsItem.getInt("CREATED_BY"));
                ObjItem.setAttribute("CREATED_DATE", rsItem.getString("CREATED_DATE"));
                ObjItem.setAttribute("MODIFIED_BY", rsItem.getInt("MODIFIED_BY"));
                ObjItem.setAttribute("MODIFIED_DATE", rsItem.getString("MODIFIED_DATE"));
                ObjItem.setAttribute("COLUMN_1_ID", rsItem.getInt("COLUMN_1_ID"));
                ObjItem.setAttribute("COLUMN_1_FORMULA", rsItem.getString("COLUMN_1_FORMULA"));
                ObjItem.setAttribute("COLUMN_1_PER", rsItem.getDouble("COLUMN_1_PER"));
                ObjItem.setAttribute("COLUMN_1_AMT", rsItem.getDouble("COLUMN_1_AMT"));
                ObjItem.setAttribute("COLUMN_1_CAPTION", rsItem.getString("COLUMN_1_CAPTION"));
                ObjItem.setAttribute("COLUMN_2_ID", rsItem.getInt("COLUMN_2_ID"));
                ObjItem.setAttribute("COLUMN_2_FORMULA", rsItem.getString("COLUMN_2_FORMULA"));
                ObjItem.setAttribute("COLUMN_2_PER", rsItem.getDouble("COLUMN_2_PER"));
                ObjItem.setAttribute("COLUMN_2_AMT", rsItem.getDouble("COLUMN_2_AMT"));
                ObjItem.setAttribute("COLUMN_2_CAPTION", rsItem.getString("COLUMN_2_CAPTION"));
                ObjItem.setAttribute("COLUMN_3_ID", rsItem.getInt("COLUMN_3_ID"));
                ObjItem.setAttribute("COLUMN_3_FORMULA", rsItem.getString("COLUMN_3_FORMULA"));
                ObjItem.setAttribute("COLUMN_3_PER", rsItem.getDouble("COLUMN_3_PER"));
                ObjItem.setAttribute("COLUMN_3_AMT", rsItem.getDouble("COLUMN_3_AMT"));
                ObjItem.setAttribute("COLUMN_3_CAPTION", rsItem.getString("COLUMN_3_CAPTION"));
                ObjItem.setAttribute("COLUMN_4_ID", rsItem.getInt("COLUMN_4_ID"));
                ObjItem.setAttribute("COLUMN_4_FORMULA", rsItem.getString("COLUMN_4_FORMULA"));
                ObjItem.setAttribute("COLUMN_4_PER", rsItem.getDouble("COLUMN_4_PER"));
                ObjItem.setAttribute("COLUMN_4_AMT", rsItem.getDouble("COLUMN_4_AMT"));
                ObjItem.setAttribute("COLUMN_4_CAPTION", rsItem.getString("COLUMN_4_CAPTION"));
                ObjItem.setAttribute("COLUMN_5_ID", rsItem.getInt("COLUMN_5_ID"));
                ObjItem.setAttribute("COLUMN_5_FORMULA", rsItem.getString("COLUMN_5_FORMULA"));
                ObjItem.setAttribute("COLUMN_5_PER", rsItem.getDouble("COLUMN_5_PER"));
                ObjItem.setAttribute("COLUMN_5_AMT", rsItem.getDouble("COLUMN_5_AMT"));
                ObjItem.setAttribute("COLUMN_5_CAPTION", rsItem.getString("COLUMN_5_CAPTION"));
                ObjItem.setAttribute("COLUMN_6_ID", rsItem.getInt("COLUMN_6_ID"));
                ObjItem.setAttribute("COLUMN_6_FORMULA", rsItem.getString("COLUMN_6_FORMULA"));
                ObjItem.setAttribute("COLUMN_6_PER", rsItem.getDouble("COLUMN_6_PER"));
                ObjItem.setAttribute("COLUMN_6_AMT", rsItem.getDouble("COLUMN_6_AMT"));
                ObjItem.setAttribute("COLUMN_6_CAPTION", rsItem.getString("COLUMN_6_CAPTION"));
                ObjItem.setAttribute("COLUMN_7_ID", rsItem.getInt("COLUMN_7_ID"));
                ObjItem.setAttribute("COLUMN_7_FORMULA", rsItem.getString("COLUMN_7_FORMULA"));
                ObjItem.setAttribute("COLUMN_7_PER", rsItem.getDouble("COLUMN_7_PER"));
                ObjItem.setAttribute("COLUMN_7_AMT", rsItem.getDouble("COLUMN_7_AMT"));
                ObjItem.setAttribute("COLUMN_7_CAPTION", rsItem.getString("COLUMN_7_CAPTION"));
                ObjItem.setAttribute("COLUMN_8_ID", rsItem.getInt("COLUMN_8_ID"));
                ObjItem.setAttribute("COLUMN_8_FORMULA", rsItem.getString("COLUMN_8_FORMULA"));
                ObjItem.setAttribute("COLUMN_8_PER", rsItem.getDouble("COLUMN_8_PER"));
                ObjItem.setAttribute("COLUMN_8_AMT", rsItem.getDouble("COLUMN_8_AMT"));
                ObjItem.setAttribute("COLUMN_8_CAPTION", rsItem.getString("COLUMN_8_CAPTION"));
                ObjItem.setAttribute("COLUMN_9_ID", rsItem.getInt("COLUMN_9_ID"));
                ObjItem.setAttribute("COLUMN_9_FORMULA", rsItem.getString("COLUMN_9_FORMULA"));
                ObjItem.setAttribute("COLUMN_9_PER", rsItem.getDouble("COLUMN_9_PER"));
                ObjItem.setAttribute("COLUMN_9_AMT", rsItem.getDouble("COLUMN_9_AMT"));
                ObjItem.setAttribute("COLUMN_9_CAPTION", rsItem.getString("COLUMN_9_CAPTION"));
                ObjItem.setAttribute("COLUMN_10_ID", rsItem.getInt("COLUMN_10_ID"));
                ObjItem.setAttribute("COLUMN_10_FORMULA", rsItem.getString("COLUMN_10_FORMULA"));
                ObjItem.setAttribute("COLUMN_10_PER", rsItem.getDouble("COLUMN_10_PER"));
                ObjItem.setAttribute("COLUMN_10_AMT", rsItem.getDouble("COLUMN_10_AMT"));
                ObjItem.setAttribute("COLUMN_10_CAPTION", rsItem.getString("COLUMN_10_CAPTION"));

                ObjItem.setAttribute("COLUMN_11_ID", rsItem.getInt("COLUMN_11_ID"));
                ObjItem.setAttribute("COLUMN_11_FORMULA", rsItem.getString("COLUMN_11_FORMULA"));
                ObjItem.setAttribute("COLUMN_11_PER", rsItem.getDouble("COLUMN_11_PER"));
                ObjItem.setAttribute("COLUMN_11_AMT", rsItem.getDouble("COLUMN_11_AMT"));
                ObjItem.setAttribute("COLUMN_11_CAPTION", rsItem.getString("COLUMN_11_CAPTION"));
                ObjItem.setAttribute("COLUMN_12_ID", rsItem.getInt("COLUMN_12_ID"));
                ObjItem.setAttribute("COLUMN_12_FORMULA", rsItem.getString("COLUMN_12_FORMULA"));
                ObjItem.setAttribute("COLUMN_12_PER", rsItem.getDouble("COLUMN_12_PER"));
                ObjItem.setAttribute("COLUMN_12_AMT", rsItem.getDouble("COLUMN_12_AMT"));
                ObjItem.setAttribute("COLUMN_12_CAPTION", rsItem.getString("COLUMN_12_CAPTION"));
                ObjItem.setAttribute("COLUMN_13_ID", rsItem.getInt("COLUMN_13_ID"));
                ObjItem.setAttribute("COLUMN_13_FORMULA", rsItem.getString("COLUMN_13_FORMULA"));
                ObjItem.setAttribute("COLUMN_13_PER", rsItem.getDouble("COLUMN_13_PER"));
                ObjItem.setAttribute("COLUMN_13_AMT", rsItem.getDouble("COLUMN_13_AMT"));
                ObjItem.setAttribute("COLUMN_13_CAPTION", rsItem.getString("COLUMN_13_CAPTION"));
                ObjItem.setAttribute("COLUMN_14_ID", rsItem.getInt("COLUMN_14_ID"));
                ObjItem.setAttribute("COLUMN_14_FORMULA", rsItem.getString("COLUMN_14_FORMULA"));
                ObjItem.setAttribute("COLUMN_14_PER", rsItem.getDouble("COLUMN_14_PER"));
                ObjItem.setAttribute("COLUMN_14_AMT", rsItem.getDouble("COLUMN_14_AMT"));
                ObjItem.setAttribute("COLUMN_14_CAPTION", rsItem.getString("COLUMN_14_CAPTION"));
                ObjItem.setAttribute("COLUMN_15_ID", rsItem.getInt("COLUMN_15_ID"));
                ObjItem.setAttribute("COLUMN_15_FORMULA", rsItem.getString("COLUMN_15_FORMULA"));
                ObjItem.setAttribute("COLUMN_15_PER", rsItem.getDouble("COLUMN_15_PER"));
                ObjItem.setAttribute("COLUMN_15_AMT", rsItem.getDouble("COLUMN_15_AMT"));
                ObjItem.setAttribute("COLUMN_15_CAPTION", rsItem.getString("COLUMN_15_CAPTION"));

                ObjItem.setAttribute("COLUMN_16_ID", rsItem.getInt("COLUMN_16_ID"));
                ObjItem.setAttribute("COLUMN_16_FORMULA", rsItem.getString("COLUMN_16_FORMULA"));
                ObjItem.setAttribute("COLUMN_16_PER", rsItem.getDouble("COLUMN_16_PER"));
                ObjItem.setAttribute("COLUMN_16_AMT", rsItem.getDouble("COLUMN_16_AMT"));
                ObjItem.setAttribute("COLUMN_16_CAPTION", rsItem.getString("COLUMN_16_CAPTION"));
                ObjItem.setAttribute("COLUMN_17_ID", rsItem.getInt("COLUMN_17_ID"));
                ObjItem.setAttribute("COLUMN_17_FORMULA", rsItem.getString("COLUMN_17_FORMULA"));
                ObjItem.setAttribute("COLUMN_17_PER", rsItem.getDouble("COLUMN_17_PER"));
                ObjItem.setAttribute("COLUMN_17_AMT", rsItem.getDouble("COLUMN_17_AMT"));
                ObjItem.setAttribute("COLUMN_17_CAPTION", rsItem.getString("COLUMN_17_CAPTION"));
                ObjItem.setAttribute("COLUMN_18_ID", rsItem.getInt("COLUMN_18_ID"));
                ObjItem.setAttribute("COLUMN_18_FORMULA", rsItem.getString("COLUMN_18_FORMULA"));
                ObjItem.setAttribute("COLUMN_18_PER", rsItem.getDouble("COLUMN_18_PER"));
                ObjItem.setAttribute("COLUMN_18_AMT", rsItem.getDouble("COLUMN_18_AMT"));
                ObjItem.setAttribute("COLUMN_18_CAPTION", rsItem.getString("COLUMN_18_CAPTION"));
                ObjItem.setAttribute("COLUMN_19_ID", rsItem.getInt("COLUMN_19_ID"));
                ObjItem.setAttribute("COLUMN_19_FORMULA", rsItem.getString("COLUMN_19_FORMULA"));
                ObjItem.setAttribute("COLUMN_19_PER", rsItem.getDouble("COLUMN_19_PER"));
                ObjItem.setAttribute("COLUMN_19_AMT", rsItem.getDouble("COLUMN_19_AMT"));
                ObjItem.setAttribute("COLUMN_19_CAPTION", rsItem.getString("COLUMN_19_CAPTION"));
                ObjItem.setAttribute("COLUMN_20_ID", rsItem.getInt("COLUMN_20_ID"));
                ObjItem.setAttribute("COLUMN_20_FORMULA", rsItem.getString("COLUMN_20_FORMULA"));
                ObjItem.setAttribute("COLUMN_20_PER", rsItem.getDouble("COLUMN_20_PER"));
                ObjItem.setAttribute("COLUMN_20_AMT", rsItem.getDouble("COLUMN_20_AMT"));
                ObjItem.setAttribute("COLUMN_20_CAPTION", rsItem.getString("COLUMN_20_CAPTION"));

                ObjItem.setAttribute("COLUMN_21_ID", rsItem.getInt("COLUMN_21_ID"));
                ObjItem.setAttribute("COLUMN_21_FORMULA", rsItem.getString("COLUMN_21_FORMULA"));
                ObjItem.setAttribute("COLUMN_21_PER", rsItem.getDouble("COLUMN_21_PER"));
                ObjItem.setAttribute("COLUMN_21_AMT", rsItem.getDouble("COLUMN_21_AMT"));
                ObjItem.setAttribute("COLUMN_21_CAPTION", rsItem.getString("COLUMN_21_CAPTION"));
                
                ObjItem.setAttribute("EXCISE_GATEPASS_GIVEN", rsItem.getBoolean("EXCISE_GATEPASS_GIVEN"));
                ObjItem.setAttribute("IMPORT_CONCESS", rsItem.getBoolean("IMPORT_CONCESS"));
                ObjItem.setAttribute("QUOT_ID", rsItem.getString("QUOT_ID"));
                ObjItem.setAttribute("QUOT_SR_NO", rsItem.getInt("QUOT_SR_NO"));
                colPOItems.put(Integer.toString(ItemCounter), ObjItem);
                rsItem.next();
            }

            colPOTerms.clear();

            AmendNo = (String) getAttribute("AMEND_NO").getObj();

            stItem = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (HistoryView) {
                rsItem = stItem.executeQuery("SELECT * FROM D_PUR_AMEND_TERMS_H WHERE COMPANY_ID=" + CompanyID + " AND AMEND_NO='" + AmendNo + "' AND PO_TYPE=" + POType + " AND REVISION_NO=" + RevNo + " ORDER BY SR_NO");
            } else {
                rsItem = stItem.executeQuery("SELECT * FROM D_PUR_AMEND_TERMS WHERE COMPANY_ID=" + CompanyID + " AND AMEND_NO='" + AmendNo + "' AND PO_TYPE=" + POType + " ORDER BY SR_NO");
            }
            rsItem.first();

            ItemCounter = 0;

            while ((!rsItem.isAfterLast()) && (rsItem.getRow() > 0)) {
                ItemCounter = ItemCounter + 1;

                SrNo = rsItem.getInt("SR_NO"); //Sr. No. of Item Detail
                clsPOTerms ObjItem = new clsPOTerms();
                ObjItem.setAttribute("COMPANY_ID", rsItem.getInt("COMPANY_ID"));
                ObjItem.setAttribute("AMEND_NO", rsItem.getString("AMEND_NO"));
                ObjItem.setAttribute("PO_NO", rsItem.getString("PO_NO"));
                ObjItem.setAttribute("PO_TYPE", rsItem.getInt("PO_TYPE"));
                ObjItem.setAttribute("SR_NO", rsItem.getInt("SR_NO"));
                ObjItem.setAttribute("TERM_TYPE", rsItem.getString("TERM_TYPE"));
                ObjItem.setAttribute("TERM_CODE", rsItem.getInt("TERM_CODE"));
                ObjItem.setAttribute("TERM_DESC", rsItem.getString("TERM_DESC"));

                colPOTerms.put(Integer.toString(ItemCounter), ObjItem);
                rsItem.next();
            }

            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    //This routine checks and returns whether the item is editable or not
    //Criteria is Approved item cannot be changed
    //and if not approved then user id is checked whether doucment
    //is waiting for his approval.
    public boolean IsEditable(int pCompanyID, String pDocNo, int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";
        int ModuleID = 0;

        try {
            if (HistoryView) {
                return false;
            }

            //First check that document is approved or not
            strSQL = "SELECT COUNT(*) AS COUNT FROM D_PUR_AMEND_HEADER WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND AMEND_NO='" + pDocNo + "' AND (APPROVED=1) AND PO_TYPE=" + POType;

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                ModuleID = 27 + POType;

                if (POType == 8) {
                    ModuleID = 47;
                }

                //start add on 110909
                if (POType == 9) {
                    ModuleID = 168;
                }
                //end add on 110909

                strSQL = "SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND MODULE_ID=" + (ModuleID) + " AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND STATUS='W'";
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

    //This routine checks and returns whether the item is deletable or not
    //Criteria is Approved item cannot be deleted
    //and if not approved then user id is checked whether doucment
    //is created by the user. Only creator can delete the document
    public boolean CanDelete(int pCompanyID, String pDocNo, int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";
        int ModuleID = 0;

        try {
            if (HistoryView) {
                return false;
            }

            //First check that document is approved or not
            strSQL = "SELECT COUNT(*) AS COUNT FROM D_PUR_AMEND_HEADER WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND AMEND_NO='" + pDocNo + "' AND (APPROVED=1) AND PO_TYPE=" + POType;

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                ModuleID = 27 + POType;

                if (POType == 8) {
                    ModuleID = 47;
                }

                //start add on 110909
                if (POType == 9) {
                    ModuleID = 168;
                }
                //end add on 110909
                strSQL = "SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND MODULE_ID=" + (ModuleID) + " AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND TYPE='C' AND STATUS='W'";
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

    public static int getMaxAmendSrNo(int pCompanyID, String pPONo, int pType) {
        Connection tmpConn;
        Statement stTmp = null;
        ResultSet rsTmp = null;
        int MaxSrNo = 0;

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = stTmp.executeQuery("SELECT MAX(AMEND_SR_NO) AS MAXNO FROM D_PUR_AMEND_HEADER WHERE COMPANY_ID=" + pCompanyID + " AND PO_NO='" + pPONo + "' AND PO_TYPE=" + pType);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                MaxSrNo = rsTmp.getInt("MAXNO");
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

            return MaxSrNo;
        } catch (Exception e) {
            return 0;
        }
    }

    public static boolean IsAmendmentUnderProcess(int pCompanyID, String pPONo, int pType) {
        Connection tmpConn;
        Statement stTmp = null;
        ResultSet rsTmp = null;
        boolean IsPresent = false;

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = stTmp.executeQuery("SELECT * FROM D_PUR_AMEND_HEADER WHERE COMPANY_ID=" + pCompanyID + " AND PO_NO='" + pPONo + "' AND PO_TYPE=" + pType + " AND APPROVED=0 AND CANCELLED=0");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                IsPresent = true;
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

            return IsPresent;
        } catch (Exception e) {
            return IsPresent;
        }
    }

    public static HashMap getPendingApprovals(int pCompanyID, int pUserID, int pType, int pOrder, int FinYearFrom) {
        String strSQL = "";
        Connection tmpConn;
        tmpConn = data.getConn();

        ResultSet rsTmp = null;
        Statement tmpStmt = null;

        HashMap List = new HashMap();
        long Counter = 0;
        int ModuleID = 0;

        ModuleID = 27 + pType;

        if (pType == 8) {
            ModuleID = 47;
        }
        if (pType == 9) {
            ModuleID = 168;
        }

        try {

            if (pOrder == EITLERPGLOBAL.OnRecivedDate) {
                strSQL = "SELECT D_PUR_AMEND_HEADER.AMEND_NO,D_PUR_AMEND_HEADER.AMEND_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_PUR_AMEND_HEADER,D_COM_DOC_DATA WHERE D_PUR_AMEND_HEADER.AMEND_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_AMEND_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_AMEND_HEADER.COMPANY_ID=" + Integer.toString(pCompanyID) + " AND D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND D_COM_DOC_DATA.STATUS='W' AND D_PUR_AMEND_HEADER.PO_TYPE=" + pType + " AND MODULE_ID=" + (ModuleID) + " ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT D_PUR_AMEND_HEADER.AMEND_NO,D_PUR_AMEND_HEADER.AMEND_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_PUR_AMEND_HEADER,D_COM_DOC_DATA WHERE D_PUR_AMEND_HEADER.AMEND_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_AMEND_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_AMEND_HEADER.COMPANY_ID=" + Integer.toString(pCompanyID) + " AND D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND D_COM_DOC_DATA.STATUS='W' AND D_PUR_AMEND_HEADER.PO_TYPE=" + pType + " AND MODULE_ID=" + (ModuleID) + " ORDER BY D_PUR_AMEND_HEADER.AMEND_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT D_PUR_AMEND_HEADER.AMEND_NO,D_PUR_AMEND_HEADER.AMEND_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_PUR_AMEND_HEADER,D_COM_DOC_DATA WHERE D_PUR_AMEND_HEADER.AMEND_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_AMEND_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_AMEND_HEADER.COMPANY_ID=" + Integer.toString(pCompanyID) + " AND D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND D_COM_DOC_DATA.STATUS='W' AND D_PUR_AMEND_HEADER.PO_TYPE=" + pType + " AND MODULE_ID=" + (ModuleID) + " ORDER BY D_PUR_AMEND_HEADER.AMEND_NO";
            }

            //strSQL="SELECT D_PUR_AMEND_HEADER.AMEND_NO,D_PUR_AMEND_HEADER.AMEND_DATE FROM D_PUR_AMEND_HEADER,D_COM_DOC_DATA WHERE D_PUR_AMEND_HEADER.AMEND_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_AMEND_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_AMEND_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_PUR_AMEND_HEADER.PO_TYPE="+pType+" AND MODULE_ID="+(ModuleID);
            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter = 0;
            while (!rsTmp.isAfterLast()) {

                if (EITLERPGLOBAL.isWithinDate(rsTmp.getString("AMEND_DATE"), FinYearFrom)) {
                    Counter = Counter + 1;
                    clsPOAmendGen ObjDoc = new clsPOAmendGen();

                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("AMEND_NO", rsTmp.getString("AMEND_NO"));
                    ObjDoc.setAttribute("AMEND_DATE", rsTmp.getString("AMEND_DATE"));
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

    public boolean ShowHistory(int pCompanyID, String pDocNo) {
        Ready = false;
        try {
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsResultSet = Stmt.executeQuery("SELECT * FROM D_PUR_AMEND_HEADER_H WHERE COMPANY_ID=" + pCompanyID + " AND AMEND_NO='" + pDocNo + "' AND PO_TYPE=" + POType);
            Ready = true;
            HistoryView = true;
            MoveFirst();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public static HashMap getHistoryList(int pCompanyID, String pDocNo, int pPOType) {
        HashMap List = new HashMap();
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp = stTmp.executeQuery("SELECT * FROM D_PUR_AMEND_HEADER_H WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND AMEND_NO='" + pDocNo + "' AND PO_TYPE=" + pPOType);

            while (rsTmp.next()) {
                clsPOAmendGen ObjPO = new clsPOAmendGen();

                ObjPO.setAttribute("AMEND_NO", rsTmp.getString("AMEND_NO"));
                ObjPO.setAttribute("AMEND_DATE", rsTmp.getString("AMEND_DATE"));
                ObjPO.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                ObjPO.setAttribute("UPDATED_BY", rsTmp.getInt("UPDATED_BY"));
                ObjPO.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                ObjPO.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                ObjPO.setAttribute("APPROVER_REMARKS", rsTmp.getString("APPROVER_REMARKS"));

                List.put(Integer.toString(List.size() + 1), ObjPO);
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
            rsTmp = data.getResult("SELECT AMEND_NO,APPROVED,CANCELLED FROM D_PUR_AMEND_HEADER WHERE COMPANY_ID=" + pCompanyID + " AND AMEND_NO='" + pDocNo + "'");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                if (rsTmp.getBoolean("APPROVED")) {
                    if (rsTmp.getBoolean("CANCELLED")) {
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

    public static boolean CanCancel(int pCompanyID, String pAmendNo) {
        ResultSet rsTmp = null;
        boolean canCancel = false;

        try {
            rsTmp = data.getResult("SELECT AMEND_NO FROM D_PUR_AMEND_HEADER WHERE COMPANY_ID=" + pCompanyID + " AND AMEND_NO='" + pAmendNo + "' AND CANCELLED=0 AND APPROVED=0");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                canCancel = true;
            }

            rsTmp.close();
        } catch (Exception e) {

        }

        return canCancel;

    }

    public static boolean CancelAmendment(int pCompanyID, String pAmendNo) {

        ResultSet rsTmp = null;
        ResultSet rsIndent = null;
        ResultSet rsAmend = null;
        boolean Cancelled = false;

        try {
            if (CanCancel(pCompanyID, pAmendNo)) {

                boolean ApprovedAmendment = false;

                rsTmp = data.getResult("SELECT APPROVED FROM D_PUR_AMEND_HEADER WHERE COMPANY_ID=" + pCompanyID + " AND AMEND_NO='" + pAmendNo + "'");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    ApprovedAmendment = rsTmp.getBoolean("APPROVED");
                }

                if (ApprovedAmendment) {

                } else {

                    int ModuleID = 0;
                    int POType = 0;

                    rsAmend = data.getResult("SELECT PO_TYPE FROM D_PUR_AMEND_HEADER WHERE AMEND_NO='" + pAmendNo + "'");
                    rsAmend.first();

                    if (rsAmend.getRow() > 0) {
                        POType = rsAmend.getInt("PO_TYPE");

                        if (POType >= 1 && POType <= 7) {
                            ModuleID = 27 + POType;
                        }

                        if (POType == 8) {
                            ModuleID = 47;
                        }

                        //start add on 110909
                        if (POType == 9) {
                            ModuleID = 168;
                        }
                        //end add on 110909
                    }

                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID=" + pCompanyID + " AND DOC_NO='" + pAmendNo + "' AND MODULE_ID=" + (ModuleID));
                }

                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_PUR_AMEND_HEADER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + pCompanyID + " AND AMEND_NO='" + pAmendNo + "'");

                Cancelled = true;
            }

            rsTmp.close();
            rsIndent.close();
        } catch (Exception e) {

        }

        return Cancelled;
    }

}
