/*
 * clsSalesPartyAmend.java
 *
 * Created on April 3, 2009, 10:22 AM
 */
package EITLERP;

import java.util.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import EITLERP.Finance.*;

/**
 *
 * @author root
 */
public class clsSalesPartyAmend {

    public String LastError = "";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;

    private HashMap props;
    public boolean Ready = false;

    private boolean HistoryView = false;

    public static int ModuleID = 149;

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
     * Creates a new instance of clsSalesPartyAmend
     */
    public clsSalesPartyAmend() {
        LastError = "";
        props = new HashMap();
        props.put("COMPANY_ID", new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("AMEND_ID", new Variant(0));
        props.put("AMEND_DATE", new Variant(""));
        props.put("AMEND_REASON", new Variant(""));
        props.put("PARTY_CODE", new Variant(""));
        props.put("OLD_PARTY_CODE", new Variant(""));
        props.put("PARTY_TYPE", new Variant(0));
        props.put("PARTY_NAME", new Variant(""));

        props.put("SEASON_CODE", new Variant(""));
        props.put("REG_DATE", new Variant(""));

        props.put("AREA_ID", new Variant(""));
        props.put("ADDRESS1", new Variant(""));
        props.put("ADDRESS2", new Variant(""));
        props.put("PINCODE", new Variant(""));
        props.put("CITY_ID", new Variant(""));
        props.put("CITY_NAME", new Variant(""));
        props.put("DISPATCH_STATION", new Variant(""));
        props.put("DISTRICT", new Variant(""));
        props.put("PHONE_NO", new Variant(""));
        props.put("MOBILE_NO", new Variant(""));
        props.put("MOBILE_NO_2", new Variant(""));
        props.put("MOBILE_NO_3", new Variant(""));
        props.put("CORR_ADDRESS", new Variant(""));
        props.put("COUNTRY_ID", new Variant(0)); //new code by vivek on 16/09/2013 to add state & country.
        props.put("STATE_ID", new Variant(0)); //new code by vivek on 16/09/2013 to add state & country.
        props.put("EMAIL", new Variant(""));
        props.put("EMAIL_ID2", new Variant(""));
        props.put("EMAIL_ID3", new Variant(""));
        props.put("URL", new Variant(""));
        props.put("CONTACT_PERSON", new Variant(""));
        props.put("CONTACT_PERSON_2", new Variant(""));
        props.put("CONTACT_PERSON_3", new Variant(""));
        props.put("CONT_PERS_DESIGNATION", new Variant(""));
        props.put("CONT_PERS_DESIGNATION_2", new Variant(""));
        props.put("CONT_PERS_DESIGNATION_3", new Variant(""));
        props.put("BANK_ID", new Variant(0));
        props.put("BANK_NAME", new Variant(""));
        props.put("BANK_ADDRESS", new Variant(""));
        props.put("CREDIT_DAYS", new Variant(0));
        props.put("EXTRA_CREDIT_DAYS", new Variant(0));
        props.put("GRACE_CREDIT_DAYS", new Variant(0));
        props.put("BANK_CITY", new Variant(""));
        props.put("CHARGE_CODE", new Variant(""));
        props.put("CHARGE_CODE_II", new Variant(""));
        props.put("DOCUMENT_THROUGH", new Variant(""));
        props.put("TRANSPORTER_ID", new Variant(0));
        props.put("TRANSPORTER_NAME", new Variant(""));
        props.put("AMOUNT_LIMIT", new Variant(0));
        props.put("PAN_NO", new Variant(""));
        props.put("PAN_DATE", new Variant(""));
        props.put("ECC_NO", new Variant(""));
        props.put("ECC_DATE", new Variant(""));
        props.put("TIN_NO", new Variant(""));
        props.put("TIN_DATE", new Variant(""));
        props.put("CST_NO", new Variant(""));
        props.put("CST_DATE", new Variant(""));
        props.put("GSTIN_NO", new Variant(""));
        props.put("GSTIN_DATE", new Variant(""));
        props.put("MAIN_ACCOUNT_CODE", new Variant(""));
        props.put("CATEGORY", new Variant(""));
        props.put("INSURANCE_CODE", new Variant(""));
        props.put("CREATED_BY", new Variant(""));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(""));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("HIERARCHY_ID", new Variant(0));
        props.put("APPROVED", new Variant(false));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED", new Variant(false));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("CHANGED", new Variant(true));
        props.put("CHANGED_DATE", new Variant(""));
        props.put("CANCELLED", new Variant(0));
        props.put("OTHER_BANK_ID", new Variant(0));
        props.put("OTHER_BANK_NAME", new Variant(""));
        props.put("OTHER_BANK_ADDRESS", new Variant(""));
        props.put("OTHER_BANK_CITY", new Variant(""));
        props.put("REMARKS", new Variant(""));
        props.put("DELAY_INTEREST_PER", new Variant(""));
        props.put("ACCOUNT_CODES", new Variant(""));
        props.put("MARKET_SEGMENT", new Variant(""));
        props.put("PRODUCT_SEGMENT", new Variant(""));
        props.put("CLIENT_CATEGORY", new Variant(""));
        props.put("DESIGNER_INCHARGE", new Variant(""));
        props.put("DISTANCE_KM", new Variant(0));
        props.put("ZONE", new Variant(""));
        props.put("TAGGING_APPROVAL_IND", new Variant(0));
        props.put("PO_NO_REQUIRED", new Variant(0));
        props.put("KEY_CLIENT_IND", new Variant(0));
        
        props.put("DELIVERY_MODE", new Variant(""));

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
            rsResultSet = Stmt.executeQuery("SELECT * FROM D_SAL_PARTY_AMEND_MASTER ORDER BY PARTY_CODE");
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

    public boolean setData() {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;

        tmpConn = data.getConn();

        long Counter = 0;
        int RevNo = 0;

        try {
            if (HistoryView) {
                RevNo = UtilFunctions.getInt(rsResultSet, "REVISION_NO", 0);
                setAttribute("REVISION_NO", RevNo);
            } else {
                setAttribute("REVISION_NO", 0);
            }

            setAttribute("COMPANY_ID", UtilFunctions.getInt(rsResultSet, "COMPANY_ID", 0));
            setAttribute("AMEND_ID", UtilFunctions.getLong(rsResultSet, "AMEND_ID", 0));
            setAttribute("AMEND_DATE", UtilFunctions.getString(rsResultSet, "AMEND_DATE", "0000-00-00"));
            setAttribute("AMEND_REASON", UtilFunctions.getString(rsResultSet, "AMEND_REASON", ""));
            setAttribute("PARTY_CODE", UtilFunctions.getString(rsResultSet, "PARTY_CODE", ""));
            setAttribute("OLD_PARTY_CODE", UtilFunctions.getString(rsResultSet, "PARTY_CODE", ""));
            setAttribute("PARTY_TYPE", UtilFunctions.getInt(rsResultSet, "PARTY_TYPE", 0));
            setAttribute("PARTY_NAME", UtilFunctions.getString(rsResultSet, "PARTY_NAME", ""));
            setAttribute("SEASON_CODE", UtilFunctions.getString(rsResultSet, "SEASON_CODE", ""));
            setAttribute("REG_DATE", UtilFunctions.getString(rsResultSet, "REG_DATE", "0000-00-00"));
            setAttribute("AREA_ID", UtilFunctions.getString(rsResultSet, "AREA_ID", ""));
            setAttribute("ADDRESS1", UtilFunctions.getString(rsResultSet, "ADDRESS1", ""));
            setAttribute("ADDRESS2", UtilFunctions.getString(rsResultSet, "ADDRESS2", ""));
            setAttribute("CITY_ID", UtilFunctions.getString(rsResultSet, "CITY_ID", ""));
            setAttribute("DISPATCH_STATION", UtilFunctions.getString(rsResultSet, "DISPATCH_STATION", ""));
            setAttribute("CITY_NAME", UtilFunctions.getString(rsResultSet, "CITY_NAME", ""));
            setAttribute("DISTRICT", UtilFunctions.getString(rsResultSet, "DISTRICT", ""));
            setAttribute("PINCODE", UtilFunctions.getString(rsResultSet, "PINCODE", ""));
            setAttribute("PHONE_NO", UtilFunctions.getString(rsResultSet, "PHONE_NO", ""));
            setAttribute("MOBILE_NO", UtilFunctions.getString(rsResultSet, "MOBILE_NO", ""));
            setAttribute("MOBILE_NO_2", UtilFunctions.getString(rsResultSet, "MOBILE_NO_2", ""));
            setAttribute("MOBILE_NO_3", UtilFunctions.getString(rsResultSet, "MOBILE_NO_3", ""));
            setAttribute("CORR_ADDRESS", UtilFunctions.getString(rsResultSet, "CORR_ADDRESS", ""));
            setAttribute("STATE_ID", UtilFunctions.getInt(rsResultSet, "STATE_ID", 0)); //new code by vivek on 16/09/2013 to add state & country.
            setAttribute("COUNTRY_ID", UtilFunctions.getInt(rsResultSet, "COUNTRY_ID", 0)); //new code by vivek on 16/09/2013 to add state & country.
            setAttribute("EMAIL", UtilFunctions.getString(rsResultSet, "EMAIL", ""));
            setAttribute("EMAIL_ID2", UtilFunctions.getString(rsResultSet, "EMAIL_ID2", ""));
            setAttribute("EMAIL_ID3", UtilFunctions.getString(rsResultSet, "EMAIL_ID3", ""));
            setAttribute("URL", UtilFunctions.getString(rsResultSet, "URL", ""));
            setAttribute("CONTACT_PERSON", UtilFunctions.getString(rsResultSet, "CONTACT_PERSON", ""));
            setAttribute("CONTACT_PERSON_2", UtilFunctions.getString(rsResultSet, "CONTACT_PERSON_2", ""));
            setAttribute("CONTACT_PERSON_3", UtilFunctions.getString(rsResultSet, "CONTACT_PERSON_3", ""));
            setAttribute("CONT_PERS_DESIGNATION", UtilFunctions.getString(rsResultSet, "CONT_PERS_DESIGNATION", ""));
            setAttribute("CONT_PERS_DESIGNATION_2", UtilFunctions.getString(rsResultSet, "CONT_PERS_DESIGNATION_2", ""));
            setAttribute("CONT_PERS_DESIGNATION_3", UtilFunctions.getString(rsResultSet, "CONT_PERS_DESIGNATION_3", ""));
            setAttribute("BANK_ID", UtilFunctions.getInt(rsResultSet, "BANK_ID", 0));
            setAttribute("BANK_NAME", UtilFunctions.getString(rsResultSet, "BANK_NAME", ""));
            setAttribute("BANK_ADDRESS", UtilFunctions.getString(rsResultSet, "BANK_ADDRESS", ""));
            setAttribute("BANK_CITY", UtilFunctions.getString(rsResultSet, "BANK_CITY", ""));
            setAttribute("CST_NO", UtilFunctions.getString(rsResultSet, "CST_NO", ""));
            setAttribute("CST_DATE", UtilFunctions.getString(rsResultSet, "CST_DATE", "0000-00-00"));
            setAttribute("ECC_NO", UtilFunctions.getString(rsResultSet, "ECC_NO", ""));
            setAttribute("ECC_DATE", UtilFunctions.getString(rsResultSet, "ECC_DATE", "0000-00-00"));
            setAttribute("TIN_NO", UtilFunctions.getString(rsResultSet, "TIN_NO", ""));
            setAttribute("TIN_DATE", UtilFunctions.getString(rsResultSet, "TIN_DATE", "0000-00-00"));
            setAttribute("PAN_NO", UtilFunctions.getString(rsResultSet, "PAN_NO", ""));
            setAttribute("PAN_DATE", UtilFunctions.getString(rsResultSet, "PAN_DATE", "0000-00-00"));
            setAttribute("GSTIN_NO", UtilFunctions.getString(rsResultSet, "GSTIN_NO", ""));
            setAttribute("GSTIN_DATE", UtilFunctions.getString(rsResultSet, "GSTIN_DATE", "0000-00-00"));
            setAttribute("MAIN_ACCOUNT_CODE", UtilFunctions.getString(rsResultSet, "MAIN_ACCOUNT_CODE", ""));
            setAttribute("CATEGORY", UtilFunctions.getString(rsResultSet, "CATEGORY", ""));
            setAttribute("CHARGE_CODE", UtilFunctions.getString(rsResultSet, "CHARGE_CODE", ""));
            setAttribute("CHARGE_CODE_II", UtilFunctions.getString(rsResultSet, "CHARGE_CODE_II", ""));
            setAttribute("CREDIT_DAYS", UtilFunctions.getDouble(rsResultSet, "CREDIT_DAYS", 0));
            setAttribute("EXTRA_CREDIT_DAYS", UtilFunctions.getDouble(rsResultSet, "EXTRA_CREDIT_DAYS", 0));
            setAttribute("GRACE_CREDIT_DAYS", UtilFunctions.getDouble(rsResultSet, "GRACE_CREDIT_DAYS", 0));
            setAttribute("DOCUMENT_THROUGH", UtilFunctions.getString(rsResultSet, "DOCUMENT_THROUGH", ""));
            setAttribute("TRANSPORTER_ID", UtilFunctions.getInt(rsResultSet, "TRANSPORTER_ID", 0));
            setAttribute("TRANSPORTER_NAME", UtilFunctions.getString(rsResultSet, "TRANSPORTER_NAME", ""));
            setAttribute("AMOUNT_LIMIT", UtilFunctions.getDouble(rsResultSet, "AMOUNT_LIMIT", 0));
            setAttribute("OTHER_BANK_ID", UtilFunctions.getInt(rsResultSet, "OTHER_BANK_ID", 0));
            setAttribute("OTHER_BANK_NAME", UtilFunctions.getString(rsResultSet, "OTHER_BANK_NAME", ""));
            setAttribute("OTHER_BANK_ADDRESS", UtilFunctions.getString(rsResultSet, "OTHER_BANK_ADDRESS", ""));
            setAttribute("OTHER_BANK_CITY", UtilFunctions.getString(rsResultSet, "OTHER_BANK_CITY", ""));
            setAttribute("INSURANCE_CODE", UtilFunctions.getString(rsResultSet, "INSURANCE_CODE", ""));
            setAttribute("REMARKS", UtilFunctions.getString(rsResultSet, "REMARKS", ""));
            setAttribute("DELAY_INTEREST_PER", UtilFunctions.getDouble(rsResultSet, "DELAY_INTEREST_PER", 0));
            setAttribute("ACCOUNT_CODES", UtilFunctions.getString(rsResultSet, "ACCOUNT_CODES", ""));
            setAttribute("DO_NOT_ALLOW_INTEREST", UtilFunctions.getInt(rsResultSet, "DO_NOT_ALLOW_INTEREST", 0));

            setAttribute("MARKET_SEGMENT", UtilFunctions.getString(rsResultSet, "MARKET_SEGMENT", ""));
            setAttribute("PRODUCT_SEGMENT", UtilFunctions.getString(rsResultSet, "PRODUCT_SEGMENT", ""));
            setAttribute("CLIENT_CATEGORY", UtilFunctions.getString(rsResultSet, "CLIENT_CATEGORY", ""));
            setAttribute("DESIGNER_INCHARGE", UtilFunctions.getString(rsResultSet, "DESIGNER_INCHARGE", ""));

            setAttribute("INCHARGE_CD", UtilFunctions.getString(rsResultSet, "INCHARGE_CD", ""));
            setAttribute("ZONE", UtilFunctions.getString(rsResultSet, "ZONE", ""));

            String gMainCode = UtilFunctions.getString(rsResultSet, "MAIN_ACCOUNT_CODE", "");
            String gArea = UtilFunctions.getString(rsResultSet, "ZONE", "");
            if (gMainCode.equals("210010") && (gArea.equals("SOUTH") || gArea.equals("NORTH") || gArea.equals("EAST/WEST"))) {
                setAttribute("ZONE", "OTHER");
            } else {
                setAttribute("ZONE", UtilFunctions.getString(rsResultSet, "ZONE", ""));
            }

            setAttribute("DISTANCE_KM", UtilFunctions.getInt(rsResultSet, "DISTANCE_KM", 0));
            setAttribute("TAGGING_APPROVAL_IND", UtilFunctions.getInt(rsResultSet, "TAGGING_APPROVAL_IND", 0));

            setAttribute("PO_NO_REQUIRED", UtilFunctions.getInt(rsResultSet, "PO_NO_REQUIRED", 0));
            setAttribute("KEY_CLIENT_IND", UtilFunctions.getInt(rsResultSet, "KEY_CLIENT_IND", 0));

            setAttribute("DELIVERY_MODE", UtilFunctions.getString(rsResultSet, "DELIVERY_MODE", ""));

            setAttribute("CREATED_BY", UtilFunctions.getString(rsResultSet, "CREATED_BY", ""));
            setAttribute("CREATED_DATE", UtilFunctions.getString(rsResultSet, "CREATED_DATE", "0000-00-00"));
            setAttribute("MODIFIED_BY", UtilFunctions.getString(rsResultSet, "MODIFIED_BY", ""));
            setAttribute("MODIFIED_DATE", UtilFunctions.getString(rsResultSet, "MODIFIED_DATE", "0000-00-00"));

            setAttribute("HIERARCHY_ID", UtilFunctions.getInt(rsResultSet, "HIERARCHY_ID", 0));

            setAttribute("APPROVED", UtilFunctions.getInt(rsResultSet, "APPROVED", 0));
            setAttribute("APPROVED_DATE", UtilFunctions.getString(rsResultSet, "APPROVED_DATE", "0000-00-00"));
            setAttribute("REJECTED", UtilFunctions.getBoolean(rsResultSet, "REJECTED", false));
            setAttribute("REJECTED_DATE", UtilFunctions.getString(rsResultSet, "REJECTED_DATE", "0000-00-00"));
            setAttribute("REJECTED_REMARKS", UtilFunctions.getString(rsResultSet, "REJECTED_REMARKS", ""));

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
    public boolean IsEditable(int CompanyID, String PartyCode, int UserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";

        try {

            if (HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL = "SELECT PARTY_CODE FROM D_SAL_PARTY_AMEND_MASTER WHERE AMEND_ID=" + PartyCode + " AND (APPROVED=1)";

            if (data.IsRecordExist(strSQL)) {
                return false;
            } else {
                strSQL = "SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID=" + CompanyID + " AND MODULE_ID=" + ModuleID + " AND DOC_NO='" + PartyCode + "' AND USER_ID=" + UserID + " AND STATUS='W'";
                if (data.IsRecordExist(strSQL)) {
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

    public static HashMap getHistoryList(int CompanyID, String PartyCode) {
        HashMap List = new HashMap();
        ResultSet rsTmp;

        try {
            String strSQL = "SELECT * FROM D_SAL_PARTY_AMEND_MASTER_H WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND AMEND_ID=" + PartyCode + "";
            rsTmp = data.getResult(strSQL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                while (!rsTmp.isAfterLast()) {
                    clsSalesPartyAmend objParty = new clsSalesPartyAmend();

                    objParty.setAttribute("PARTY_CODE", UtilFunctions.getString(rsTmp, "PARTY_CODE", ""));
                    objParty.setAttribute("REVISION_NO", UtilFunctions.getInt(rsTmp, "REVISION_NO", 0));
                    objParty.setAttribute("UPDATED_BY", UtilFunctions.getString(rsTmp, "UPDATED_BY", ""));
                    objParty.setAttribute("APPROVAL_STATUS", UtilFunctions.getString(rsTmp, "APPROVAL_STATUS", ""));
                    objParty.setAttribute("ENTRY_DATE", EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp, "ENTRY_DATE", "0000-00-00")));
                    objParty.setAttribute("REJECTED_REMARKS", UtilFunctions.getString(rsTmp, "APPROVER_REMARKS", ""));
                    objParty.setAttribute("APPROVER_REMARKS", UtilFunctions.getString(rsTmp, "APPROVER_REMARKS", ""));

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

    public boolean Insert() {

        Statement stHistory, stHeader, stOHeader, stOHistory;
        ResultSet rsHistory, rsHeader, rsTmp, rsOHistory, rsOHeader;

        try {

            // ---- History Related Changes ------ //
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHistory = stHistory.executeQuery("SELECT * FROM D_SAL_PARTY_AMEND_MASTER_H WHERE AMEND_ID=1"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//

            setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            setAttribute("AMEND_ID", data.getMaxID(EITLERPGLOBAL.gCompanyID, "D_SAL_PARTY_AMEND_MASTER", "AMEND_ID"));

            String AStatus = getAttribute("APPROVAL_STATUS").getString();

            if (AStatus.equals("F")) {
                String PartyCode = getAttribute("PARTY_CODE").getString();

                int ClsInd = data.getIntValueFromDB("SELECT PARTY_CLOSE_IND FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PARTY_CODE='" + PartyCode + "'");
                String tcsInd = data.getStringValueFromDB("SELECT COALESCE(TCS_ELIGIBILITY,0) AS TCS_ELIGIBILITY FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PARTY_CODE='" + PartyCode + "'");

                //Delete previous data
                data.Execute("DELETE FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PARTY_CODE='" + PartyCode + "' ");

                stOHeader = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                rsOHeader = stOHeader.executeQuery("SELECT * FROM D_SAL_PARTY_MASTER WHERE PARTY_CODE='1'");
                rsOHeader.first();
                rsOHeader.moveToInsertRow();
                rsOHeader.updateInt("COMPANY_ID", getAttribute("COMPANY_ID").getInt());
                rsOHeader.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
                rsOHeader.updateInt("PARTY_TYPE", getAttribute("PARTY_TYPE").getInt());
                rsOHeader.updateString("PARENT_PARTY_CODE", "");
                rsOHeader.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());

                rsOHeader.updateString("SEASON_CODE", getAttribute("SEASON_CODE").getString());
                rsOHeader.updateString("REG_DATE", getAttribute("REG_DATE").getString());

                rsOHeader.updateString("AREA_ID", getAttribute("AREA_ID").getString());
                rsOHeader.updateString("ADDRESS1", getAttribute("ADDRESS1").getString());
                rsOHeader.updateString("ADDRESS2", getAttribute("ADDRESS2").getString());
                rsOHeader.updateString("CITY_ID", getAttribute("CITY_ID").getString());
                rsOHeader.updateString("DISPATCH_STATION", getAttribute("DISPATCH_STATION").getString());
                rsOHeader.updateString("CITY_NAME", getAttribute("CITY_NAME").getString());
                rsOHeader.updateString("DISTRICT", getAttribute("DISTRICT").getString());
                rsOHeader.updateString("PINCODE", getAttribute("PINCODE").getString());
                rsOHeader.updateString("PHONE_NO", getAttribute("PHONE_NO").getString());
                rsOHeader.updateString("MOBILE_NO", getAttribute("MOBILE_NO").getString());
                rsOHeader.updateString("MOBILE_NO_2", getAttribute("MOBILE_NO_2").getString());
                rsOHeader.updateString("MOBILE_NO_3", getAttribute("MOBILE_NO_3").getString());
                rsOHeader.updateString("CORR_ADDRESS", getAttribute("CORR_ADDRESS").getString());
                rsOHeader.updateInt("STATE_ID", getAttribute("STATE_ID").getInt()); //new code by vivek on 16/09/2013 to add state & country.
                rsOHeader.updateInt("COUNTRY_ID", getAttribute("COUNTRY_ID").getInt()); //new code by vivek on 16/09/2013 to add state & country.
                rsOHeader.updateString("EMAIL", getAttribute("EMAIL").getString());
                rsOHeader.updateString("EMAIL_ID2", getAttribute("EMAIL_ID2").getString());
                rsOHeader.updateString("EMAIL_ID3", getAttribute("EMAIL_ID3").getString());
                rsOHeader.updateString("URL", getAttribute("URL").getString());
                rsOHeader.updateString("CONTACT_PERSON", getAttribute("CONTACT_PERSON").getString());
                rsOHeader.updateString("CONTACT_PERSON_2", getAttribute("CONTACT_PERSON_2").getString());
                rsOHeader.updateString("CONTACT_PERSON_3", getAttribute("CONTACT_PERSON_3").getString());
                rsOHeader.updateString("CONT_PERS_DESIGNATION", getAttribute("CONT_PERS_DESIGNATION").getString());
                rsOHeader.updateString("CONT_PERS_DESIGNATION_2", getAttribute("CONT_PERS_DESIGNATION_2").getString());
                rsOHeader.updateString("CONT_PERS_DESIGNATION_3", getAttribute("CONT_PERS_DESIGNATION_3").getString());
                rsOHeader.updateInt("BANK_ID", getAttribute("BANK_ID").getInt());
                rsOHeader.updateString("BANK_NAME", getAttribute("BANK_NAME").getString());
                rsOHeader.updateString("BANK_ADDRESS", getAttribute("BANK_ADDRESS").getString());
                rsOHeader.updateString("BANK_CITY", getAttribute("BANK_CITY").getString());
                rsOHeader.updateString("CHARGE_CODE", getAttribute("CHARGE_CODE").getString());
                rsOHeader.updateDouble("CREDIT_DAYS", getAttribute("CREDIT_DAYS").getDouble());
                rsOHeader.updateDouble("EXTRA_CREDIT_DAYS", getAttribute("EXTRA_CREDIT_DAYS").getDouble());
                rsOHeader.updateDouble("GRACE_CREDIT_DAYS", getAttribute("GRACE_CREDIT_DAYS").getDouble());
                rsOHeader.updateString("DOCUMENT_THROUGH", getAttribute("DOCUMENT_THROUGH").getString());
                rsOHeader.updateInt("TRANSPORTER_ID", getAttribute("TRANSPORTER_ID").getInt());
                rsOHeader.updateString("TRANSPORTER_NAME", getAttribute("TRANSPORTER_NAME").getString());
                rsOHeader.updateDouble("AMOUNT_LIMIT", getAttribute("AMOUNT_LIMIT").getDouble());
                rsOHeader.updateString("CST_NO", getAttribute("CST_NO").getString());
                rsOHeader.updateString("CST_DATE", getAttribute("CST_DATE").getString());
                rsOHeader.updateString("ECC_NO", getAttribute("ECC_NO").getString());
                rsOHeader.updateString("ECC_DATE", getAttribute("ECC_DATE").getString());
                rsOHeader.updateString("TIN_NO", getAttribute("TIN_NO").getString());
                rsOHeader.updateString("TIN_DATE", getAttribute("TIN_DATE").getString());
                rsOHeader.updateString("PAN_NO", getAttribute("PAN_NO").getString());
                rsOHeader.updateString("PAN_DATE", getAttribute("PAN_DATE").getString());
                rsOHeader.updateString("GSTIN_NO", getAttribute("GSTIN_NO").getString());
                rsOHeader.updateString("GSTIN_DATE", getAttribute("GSTIN_DATE").getString());
                rsOHeader.updateString("MAIN_ACCOUNT_CODE", getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsOHeader.updateString("CATEGORY", getAttribute("CATEGORY").getString());
                rsOHeader.updateString("INSURANCE_CODE", getAttribute("INSURANCE_CODE").getString());
                rsOHeader.updateString("REMARKS", getAttribute("REMARKS").getString());
                rsOHeader.updateDouble("DELAY_INTEREST_PER", getAttribute("DELAY_INTEREST_PER").getDouble());
                rsOHeader.updateString("ACCOUNT_CODES", getAttribute("ACCOUNT_CODES").getString());
                rsOHeader.updateString("MARKET_SEGMENT", getAttribute("MARKET_SEGMENT").getString());
                rsOHeader.updateString("PRODUCT_SEGMENT", getAttribute("PRODUCT_SEGMENT").getString());
                rsOHeader.updateString("CLIENT_CATEGORY", getAttribute("CLIENT_CATEGORY").getString());
                rsOHeader.updateString("DESIGNER_INCHARGE", getAttribute("DESIGNER_INCHARGE").getString());
                rsOHeader.updateString("INCHARGE_CD", getAttribute("INCHARGE_CD").getString());
                rsOHeader.updateString("ZONE", getAttribute("ZONE").getString());

                rsOHeader.updateInt("DO_NOT_ALLOW_INTEREST", getAttribute("DO_NOT_ALLOW_INTEREST").getInt());
                rsOHeader.updateInt("OTHER_BANK_ID", getAttribute("OTHER_BANK_ID").getInt());
                rsOHeader.updateString("OTHER_BANK_NAME", getAttribute("OTHER_BANK_NAME").getString());
                rsOHeader.updateString("OTHER_BANK_ADDRESS", getAttribute("OTHER_BANK_ADDRESS").getString());
                rsOHeader.updateString("OTHER_BANK_CITY", getAttribute("OTHER_BANK_CITY").getString());
                rsOHeader.updateInt("DISTANCE_KM", getAttribute("DISTANCE_KM").getInt());
                rsOHeader.updateInt("TAGGING_APPROVAL_IND", getAttribute("TAGGING_APPROVAL_IND").getInt());

                rsOHeader.updateInt("PO_NO_REQUIRED", getAttribute("PO_NO_REQUIRED").getInt());
                rsOHeader.updateInt("KEY_CLIENT_IND", getAttribute("KEY_CLIENT_IND").getInt());

                rsOHeader.updateInt("PARTY_CLOSE_IND", ClsInd);

                rsOHeader.updateString("DELIVERY_MODE", getAttribute("DELIVERY_MODE").getString());
                
                rsOHeader.updateString("TCS_ELIGIBILITY", tcsInd);

                rsOHeader.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
                rsOHeader.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
                rsOHeader.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
                rsOHeader.updateString("MODIFIED_DATE", getAttribute("MODIFIED_DATE").getString());
                rsOHeader.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
                rsOHeader.updateBoolean("APPROVED", true);
                rsOHeader.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsOHeader.updateBoolean("CANCELLED", false);
                rsOHeader.updateBoolean("CHANGED", true);
                rsOHeader.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsOHeader.updateBoolean("REJECTED", false);
                rsOHeader.updateString("REJECTED_DATE", "0000-00-00");
                rsOHeader.insertRow();

                clsSales_Party ObjParty = (clsSales_Party) clsSales_Party.getObjectEx(EITLERPGLOBAL.gCompanyID, PartyCode);

                //Set older data to current
                setAttribute("COMPANY_ID", ObjParty.getAttribute("COMPANY_ID").getInt());
                setAttribute("PARTY_CODE", ObjParty.getAttribute("PARTY_CODE").getString());
                setAttribute("OLD_PARTY_CODE", ObjParty.getAttribute("PARTY_CODE").getString());
                setAttribute("PARTY_TYPE", ObjParty.getAttribute("PARTY_TYPE").getInt());
                setAttribute("PARTY_NAME", ObjParty.getAttribute("PARTY_NAME").getString());

                setAttribute("SEASON_CODE", ObjParty.getAttribute("SEASON_CODE").getString());
                setAttribute("REG_DATE", ObjParty.getAttribute("REG_DATE").getString());

                setAttribute("AREA_ID", ObjParty.getAttribute("AREA_ID").getString());
                setAttribute("ADDRESS1", ObjParty.getAttribute("ADDRESS1").getString());
                setAttribute("ADDRESS2", ObjParty.getAttribute("ADDRESS2").getString());
                setAttribute("CITY_ID", ObjParty.getAttribute("CITY_ID").getString());
                setAttribute("CITY_NAME", ObjParty.getAttribute("CITY_NAME").getString());
                setAttribute("DISPATCH_STATION", ObjParty.getAttribute("DISPATCH_STATION").getString());
                setAttribute("DISTRICT", ObjParty.getAttribute("DISTRICT").getString());
                setAttribute("PINCODE", ObjParty.getAttribute("PINCODE").getString());
                setAttribute("PHONE_NO", ObjParty.getAttribute("PHONE_NO").getString());
                setAttribute("MOBILE_NO", ObjParty.getAttribute("MOBILE_NO").getString());
                setAttribute("MOBILE_NO_2", ObjParty.getAttribute("MOBILE_NO_2").getString());
                setAttribute("MOBILE_NO_3", ObjParty.getAttribute("MOBILE_NO_3").getString());
                setAttribute("CORR_ADDRESS", ObjParty.getAttribute("CORR_ADDRESS").getString());
                setAttribute("STATE_ID", ObjParty.getAttribute("STATE_ID").getInt()); //new code by vivek on 16/09/2013 to add state & country.
                setAttribute("COUNTRY_ID", ObjParty.getAttribute("COUNTRY_ID").getInt()); //new code by vivek on 16/09/2013 to add state & country.
                setAttribute("EMAIL", ObjParty.getAttribute("EMAIL").getString());
                setAttribute("EMAIL_ID2", ObjParty.getAttribute("EMAIL_ID2").getString());
                setAttribute("EMAIL_ID3", ObjParty.getAttribute("EMAIL_ID3").getString());
                setAttribute("URL", ObjParty.getAttribute("URL").getString());
                setAttribute("CONTACT_PERSON", ObjParty.getAttribute("CONTACT_PERSON").getString());
                setAttribute("CONTACT_PERSON_2", ObjParty.getAttribute("CONTACT_PERSON_2").getString());
                setAttribute("CONTACT_PERSON_3", ObjParty.getAttribute("CONTACT_PERSON_3").getString());
                setAttribute("CONT_PERS_DESIGNATION", ObjParty.getAttribute("CONT_PERS_DESIGNATION").getString());
                setAttribute("CONT_PERS_DESIGNATION_2", ObjParty.getAttribute("CONT_PERS_DESIGNATION_2").getString());
                setAttribute("CONT_PERS_DESIGNATION_3", ObjParty.getAttribute("CONT_PERS_DESIGNATION_3").getString());
                setAttribute("BANK_ID", ObjParty.getAttribute("BANK_ID").getInt());
                setAttribute("BANK_NAME", ObjParty.getAttribute("BANK_NAME").getString());
                setAttribute("BANK_ADDRESS", ObjParty.getAttribute("BANK_ADDRESS").getString());
                setAttribute("BANK_CITY", ObjParty.getAttribute("BANK_CITY").getString());
                setAttribute("CST_NO", ObjParty.getAttribute("CST_NO").getString());
                setAttribute("CST_DATE", ObjParty.getAttribute("CST_DATE").getString());
                setAttribute("ECC_NO", ObjParty.getAttribute("ECC_NO").getString());
                setAttribute("ECC_DATE", ObjParty.getAttribute("ECC_DATE").getString());
                setAttribute("TIN_NO", ObjParty.getAttribute("TIN_NO").getString());
                setAttribute("TIN_DATE", ObjParty.getAttribute("TIN_DATE").getString());
                setAttribute("PAN_NO", UtilFunctions.getString(rsResultSet, "PAN_NO", ""));
                setAttribute("PAN_DATE", UtilFunctions.getString(rsResultSet, "PAN_DATE", "0000-00-00"));
                setAttribute("GSTIN_NO", ObjParty.getAttribute("GSTIN_NO").getString());
                setAttribute("GSTIN_DATE", ObjParty.getAttribute("GSTIN_DATE").getString());
                setAttribute("MAIN_ACCOUNT_CODE", UtilFunctions.getString(rsResultSet, "MAIN_ACCOUNT_CODE", ""));
                setAttribute("CATEGORY", UtilFunctions.getString(rsResultSet, "CATEGORY", ""));
                setAttribute("CHARGE_CODE", UtilFunctions.getString(rsResultSet, "CHARGE_CODE", ""));
                setAttribute("CHARGE_CODE_II", UtilFunctions.getString(rsResultSet, "CHARGE_CODE_II", ""));
                setAttribute("INSURANCE_CODE", UtilFunctions.getString(rsResultSet, "INSURANCE_CODE", ""));
                setAttribute("REMARKS", UtilFunctions.getString(rsResultSet, "REMARKS", ""));
                setAttribute("DELAY_INTEREST_PER", ObjParty.getAttribute("DELAY_INTEREST_PER").getDouble());
                setAttribute("CREDIT_DAYS", ObjParty.getAttribute("CREDIT_DAYS").getDouble());
                setAttribute("EXTRA_CREDIT_DAYS", ObjParty.getAttribute("EXTRA_CREDIT_DAYS").getDouble());
                setAttribute("GRACE_CREDIT_DAYS", ObjParty.getAttribute("GRACE_CREDIT_DAYS").getDouble());
                setAttribute("DOCUMENT_THROUGH", ObjParty.getAttribute("DOCUMENT_THROUGH").getString());
                setAttribute("TRANSPORTER_ID", ObjParty.getAttribute("TRANSPORTER_ID").getInt());
                setAttribute("TRANSPORTER_NAME", ObjParty.getAttribute("TRANSPORTER_NAME").getString());
                setAttribute("AMOUNT_LIMIT", ObjParty.getAttribute("AMOUNT_LIMIT").getDouble());
                setAttribute("OTHER_BANK_ID", ObjParty.getAttribute("OTHER_BANK_ID").getInt());
                setAttribute("OTHER_BANK_NAME", ObjParty.getAttribute("OTHER_BANK_NAME").getString());
                setAttribute("OTHER_BANK_ADDRESS", ObjParty.getAttribute("OTHER_BANK_ADDRESS").getString());
                setAttribute("OTHER_BANK_CITY", ObjParty.getAttribute("OTHER_BANK_CITY").getString());
                setAttribute("ACCOUNT_CODES", ObjParty.getAttribute("ACCOUNT_CODES").getString());
                setAttribute("MARKET_SEGMENT", ObjParty.getAttribute("MARKET_SEGMENT").getString());
                setAttribute("PRODUCT_SEGMENT", ObjParty.getAttribute("PRODUCT_SEGMENT").getString());
                setAttribute("CLIENT_CATEGORY", ObjParty.getAttribute("CLIENT_CATEGORY").getString());
                setAttribute("DESIGNER_INCHARGE", ObjParty.getAttribute("DESIGNER_INCHARGE").getString());
                setAttribute("INCHARGE_CD", ObjParty.getAttribute("INCHARGE_CD").getString());
                setAttribute("DO_NOT_ALLOW_INTEREST", ObjParty.getAttribute("DO_NOT_ALLOW_INTEREST").getInt());
                setAttribute("ZONE", ObjParty.getAttribute("ZONE").getString());

                setAttribute("DISTANCE_KM", ObjParty.getAttribute("DISTANCE_KM").getInt());
                setAttribute("TAGGING_APPROVAL_IND", ObjParty.getAttribute("TAGGING_APPROVAL_IND").getInt());

                setAttribute("PO_NO_REQUIRED", ObjParty.getAttribute("PO_NO_REQUIRED").getInt());
                setAttribute("KEY_CLIENT_IND", ObjParty.getAttribute("KEY_CLIENT_IND").getInt());

                setAttribute("DELIVERY_CODE", ObjParty.getAttribute("DELIVERY_CODE").getString());

                setAttribute("CREATED_BY", ObjParty.getAttribute("CREATED_BY").getString());
                setAttribute("CREATED_DATE", ObjParty.getAttribute("CREATED_DATE").getString());
                setAttribute("MODIFIED_BY", ObjParty.getAttribute("MODIFIED_BY").getString());
                setAttribute("MODIFIED_DATE", ObjParty.getAttribute("MODIFIED_DATE").getString());

                setAttribute("HIERARCHY_ID", ObjParty.getAttribute("HIERARCHY_ID").getInt());

                setAttribute("APPROVED", ObjParty.getAttribute("APPROVED").getInt());
                setAttribute("APPROVED_DATE", ObjParty.getAttribute("APPROVED_DATE").getString());
                setAttribute("REJECTED", ObjParty.getAttribute("REJECTED").getBool());
                setAttribute("REJECTED_DATE", ObjParty.getAttribute("REJECTED_DATE").getString());

            }

            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateInt("COMPANY_ID", getAttribute("COMPANY_ID").getInt());
            rsResultSet.updateLong("AMEND_ID", (long) getAttribute("AMEND_ID").getVal());
            rsResultSet.updateString("AMEND_DATE", (String) getAttribute("AMEND_DATE").getObj());
            rsResultSet.updateString("AMEND_REASON", (String) getAttribute("AMEND_REASON").getObj());
            rsResultSet.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsResultSet.updateInt("PARTY_TYPE", getAttribute("PARTY_TYPE").getInt());
            rsResultSet.updateString("PARENT_PARTY_CODE", "");
            rsResultSet.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());

            rsResultSet.updateString("SEASON_CODE", getAttribute("SEASON_CODE").getString());
            rsResultSet.updateString("REG_DATE", getAttribute("REG_DATE").getString());

            rsResultSet.updateString("AREA_ID", getAttribute("AREA_ID").getString());
            rsResultSet.updateString("ADDRESS1", getAttribute("ADDRESS1").getString());
            rsResultSet.updateString("ADDRESS2", getAttribute("ADDRESS2").getString());
            rsResultSet.updateString("CITY_ID", getAttribute("CITY_ID").getString());
            rsResultSet.updateString("CITY_NAME", getAttribute("CITY_NAME").getString());
            rsResultSet.updateString("DISPATCH_STATION", getAttribute("DISPATCH_STATION").getString());
            rsResultSet.updateString("DISTRICT", getAttribute("DISTRICT").getString());
            rsResultSet.updateString("PINCODE", getAttribute("PINCODE").getString());
            rsResultSet.updateString("PHONE_NO", getAttribute("PHONE_NO").getString());
            rsResultSet.updateString("MOBILE_NO", getAttribute("MOBILE_NO").getString());
            rsResultSet.updateString("MOBILE_NO_2", getAttribute("MOBILE_NO_2").getString());
            rsResultSet.updateString("MOBILE_NO_3", getAttribute("MOBILE_NO_3").getString());
            rsResultSet.updateString("CORR_ADDRESS", getAttribute("CORR_ADDRESS").getString());
            rsResultSet.updateInt("STATE_ID", getAttribute("STATE_ID").getInt()); //new code by vivek on 16/09/2013 to add state & country.
            rsResultSet.updateInt("COUNTRY_ID", getAttribute("COUNTRY_ID").getInt()); //new code by vivek on 16/09/2013 to add state & country.
            rsResultSet.updateString("EMAIL", getAttribute("EMAIL").getString());
            rsResultSet.updateString("EMAIL_ID2", getAttribute("EMAIL_ID2").getString());
            rsResultSet.updateString("EMAIL_ID3", getAttribute("EMAIL_ID3").getString());
            rsResultSet.updateString("URL", getAttribute("URL").getString());
            rsResultSet.updateString("CONTACT_PERSON", getAttribute("CONTACT_PERSON").getString());
            rsResultSet.updateString("CONTACT_PERSON_2", getAttribute("CONTACT_PERSON_2").getString());
            rsResultSet.updateString("CONTACT_PERSON_3", getAttribute("CONTACT_PERSON_3").getString());
            rsResultSet.updateString("CONT_PERS_DESIGNATION", getAttribute("CONT_PERS_DESIGNATION").getString());
            rsResultSet.updateString("CONT_PERS_DESIGNATION_2", getAttribute("CONT_PERS_DESIGNATION_2").getString());
            rsResultSet.updateString("CONT_PERS_DESIGNATION_3", getAttribute("CONT_PERS_DESIGNATION_3").getString());
            rsResultSet.updateInt("BANK_ID", getAttribute("BANK_ID").getInt());
            rsResultSet.updateString("BANK_NAME", getAttribute("BANK_NAME").getString());
            rsResultSet.updateString("BANK_ADDRESS", getAttribute("BANK_ADDRESS").getString());
            rsResultSet.updateString("BANK_CITY", getAttribute("BANK_CITY").getString());
            rsResultSet.updateString("CHARGE_CODE", getAttribute("CHARGE_CODE").getString());
            rsResultSet.updateString("CHARGE_CODE_II", getAttribute("CHARGE_CODE_II").getString());
            rsResultSet.updateDouble("CREDIT_DAYS", getAttribute("CREDIT_DAYS").getDouble());
            rsResultSet.updateDouble("EXTRA_CREDIT_DAYS", getAttribute("EXTRA_CREDIT_DAYS").getDouble());
            rsResultSet.updateDouble("GRACE_CREDIT_DAYS", getAttribute("GRACE_CREDIT_DAYS").getDouble());
            rsResultSet.updateString("DOCUMENT_THROUGH", getAttribute("DOCUMENT_THROUGH").getString());
            rsResultSet.updateInt("TRANSPORTER_ID", getAttribute("TRANSPORTER_ID").getInt());
            rsResultSet.updateString("TRANSPORTER_NAME", getAttribute("TRANSPORTER_NAME").getString());
            rsResultSet.updateDouble("AMOUNT_LIMIT", getAttribute("AMOUNT_LIMIT").getDouble());
            rsResultSet.updateString("CST_NO", getAttribute("CST_NO").getString());
            rsResultSet.updateString("CST_DATE", getAttribute("CST_DATE").getString());
            rsResultSet.updateString("ECC_NO", getAttribute("ECC_NO").getString());
            rsResultSet.updateString("ECC_DATE", getAttribute("ECC_DATE").getString());
            rsResultSet.updateString("TIN_NO", getAttribute("TIN_NO").getString());
            rsResultSet.updateString("TIN_DATE", getAttribute("TIN_DATE").getString());
            rsResultSet.updateString("PAN_NO", getAttribute("PAN_NO").getString());
            rsResultSet.updateString("PAN_DATE", getAttribute("PAN_DATE").getString());
            rsResultSet.updateString("GSTIN_NO", getAttribute("GSTIN_NO").getString());
            rsResultSet.updateString("GSTIN_DATE", getAttribute("GSTIN_DATE").getString());
            rsResultSet.updateString("MAIN_ACCOUNT_CODE", getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsResultSet.updateString("CATEGORY", getAttribute("CATEGORY").getString());
            rsResultSet.updateInt("OTHER_BANK_ID", getAttribute("OTHER_BANK_ID").getInt());
            rsResultSet.updateString("OTHER_BANK_NAME", getAttribute("OTHER_BANK_NAME").getString());
            rsResultSet.updateString("OTHER_BANK_ADDRESS", getAttribute("OTHER_BANK_ADDRESS").getString());
            rsResultSet.updateString("OTHER_BANK_CITY", getAttribute("OTHER_BANK_CITY").getString());
            rsResultSet.updateString("INSURANCE_CODE", getAttribute("INSURANCE_CODE").getString());
            rsResultSet.updateString("REMARKS", getAttribute("REMARKS").getString());
            rsResultSet.updateDouble("DELAY_INTEREST_PER", getAttribute("DELAY_INTEREST_PER").getDouble());
            rsResultSet.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsResultSet.updateString("ACCOUNT_CODES", getAttribute("ACCOUNT_CODES").getString());
            rsResultSet.updateString("MARKET_SEGMENT", getAttribute("MARKET_SEGMENT").getString());
            rsResultSet.updateString("PRODUCT_SEGMENT", getAttribute("PRODUCT_SEGMENT").getString());
            rsResultSet.updateString("CLIENT_CATEGORY", getAttribute("CLIENT_CATEGORY").getString());
            rsResultSet.updateString("DESIGNER_INCHARGE", getAttribute("DESIGNER_INCHARGE").getString());
            rsResultSet.updateInt("DO_NOT_ALLOW_INTEREST", getAttribute("DO_NOT_ALLOW_INTEREST").getInt());
            rsResultSet.updateString("ZONE", getAttribute("ZONE").getString());
            rsResultSet.updateString("INCHARGE_CD", getAttribute("INCHARGE_CD").getString());

            rsResultSet.updateInt("PO_NO_REQUIRED", getAttribute("PO_NO_REQUIRED").getInt());
            rsResultSet.updateInt("KEY_CLIENT_IND", getAttribute("KEY_CLIENT_IND").getInt());

            rsResultSet.updateInt("TAGGING_APPROVAL_IND", getAttribute("TAGGING_APPROVAL_IND").getInt());
            rsResultSet.updateInt("DISTANCE_KM", getAttribute("DISTANCE_KM").getInt());

            rsResultSet.updateString("DELIVERY_MODE", getAttribute("DELIVERY_MODE").getString());

            rsResultSet.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
            rsResultSet.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE", getAttribute("MODIFIED_DATE").getString());
            rsResultSet.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            rsResultSet.updateBoolean("APPROVED", false);
            rsResultSet.updateString("APPROVED_DATE", "0000-00-00");
            rsResultSet.updateBoolean("CANCELLED", false);
            rsResultSet.updateBoolean("CHANGED", true);
            rsResultSet.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("REJECTED", false);
            rsResultSet.updateString("REJECTED_DATE", "0000-00-00");
            rsResultSet.insertRow();

            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO", 1); //Revision No. 1 in case of Insert
            rsHistory.updateString("UPDATED_BY", EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS", getAttribute("REMARKS").getString());
            rsHistory.updateInt("COMPANY_ID", getAttribute("COMPANY_ID").getInt());
            rsHistory.updateLong("AMEND_ID", (long) getAttribute("AMEND_ID").getVal());
            rsHistory.updateString("AMEND_DATE", (String) getAttribute("AMEND_DATE").getObj());
            rsHistory.updateString("AMEND_REASON", (String) getAttribute("AMEND_REASON").getObj());
            rsHistory.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsHistory.updateInt("PARTY_TYPE", getAttribute("PARTY_TYPE").getInt());
            rsHistory.updateString("PARENT_PARTY_CODE", "");
            rsHistory.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());

            rsHistory.updateString("SEASON_CODE", getAttribute("SEASON_CODE").getString());
            rsHistory.updateString("REG_DATE", getAttribute("REG_DATE").getString());

            rsHistory.updateString("AREA_ID", getAttribute("AREA_ID").getString());
            rsHistory.updateString("ADDRESS1", getAttribute("ADDRESS1").getString());
            rsHistory.updateString("ADDRESS2", getAttribute("ADDRESS2").getString());
            rsHistory.updateString("CITY_ID", getAttribute("CITY_ID").getString());
            rsHistory.updateString("CITY_NAME", getAttribute("CITY_NAME").getString());
            rsHistory.updateString("DISPATCH_STATION", getAttribute("DISPATCH_STATION").getString());
            rsHistory.updateString("DISTRICT", getAttribute("DISTRICT").getString());
            rsHistory.updateString("PINCODE", getAttribute("PINCODE").getString());
            rsHistory.updateString("PHONE_NO", getAttribute("PHONE_NO").getString());
            rsHistory.updateString("MOBILE_NO", getAttribute("MOBILE_NO").getString());
            rsHistory.updateString("MOBILE_NO_2", getAttribute("MOBILE_NO_2").getString());
            rsHistory.updateString("MOBILE_NO_3", getAttribute("MOBILE_NO_3").getString());
            rsHistory.updateString("CORR_ADDRESS", getAttribute("CORR_ADDRESS").getString());
            rsHistory.updateInt("STATE_ID", getAttribute("STATE_ID").getInt()); //new code by vivek on 16/09/2013 to add state & country.
            rsHistory.updateInt("COUNTRY_ID", getAttribute("COUNTRY_ID").getInt()); //new code by vivek on 16/09/2013 to add state & country.
            rsHistory.updateString("EMAIL", getAttribute("EMAIL").getString());
            rsHistory.updateString("EMAIL_ID2", getAttribute("EMAIL_ID2").getString());
            rsHistory.updateString("EMAIL_ID3", getAttribute("EMAIL_ID3").getString());
            rsHistory.updateString("URL", getAttribute("URL").getString());
            rsHistory.updateString("CONTACT_PERSON", getAttribute("CONTACT_PERSON").getString());
            rsHistory.updateString("CONTACT_PERSON_2", getAttribute("CONTACT_PERSON_2").getString());
            rsHistory.updateString("CONTACT_PERSON_3", getAttribute("CONTACT_PERSON_3").getString());
            rsHistory.updateString("CONT_PERS_DESIGNATION", getAttribute("CONT_PERS_DESIGNATION").getString());
            rsHistory.updateString("CONT_PERS_DESIGNATION_2", getAttribute("CONT_PERS_DESIGNATION_2").getString());
            rsHistory.updateString("CONT_PERS_DESIGNATION_3", getAttribute("CONT_PERS_DESIGNATION_3").getString());
            rsHistory.updateInt("BANK_ID", getAttribute("BANK_ID").getInt());
            rsHistory.updateString("BANK_NAME", getAttribute("BANK_NAME").getString());
            rsHistory.updateString("BANK_ADDRESS", getAttribute("BANK_ADDRESS").getString());
            rsHistory.updateString("BANK_CITY", getAttribute("BANK_CITY").getString());
            rsHistory.updateString("CHARGE_CODE", getAttribute("CHARGE_CODE").getString());
            rsHistory.updateString("CHARGE_CODE_II", getAttribute("CHARGE_CODE_II").getString());
            rsHistory.updateDouble("CREDIT_DAYS", getAttribute("CREDIT_DAYS").getDouble());
            rsHistory.updateDouble("EXTRA_CREDIT_DAYS", getAttribute("EXTRA_CREDIT_DAYS").getDouble());
            rsHistory.updateDouble("GRACE_CREDIT_DAYS", getAttribute("GRACE_CREDIT_DAYS").getDouble());
            rsHistory.updateString("DOCUMENT_THROUGH", getAttribute("DOCUMENT_THROUGH").getString());
            rsHistory.updateInt("TRANSPORTER_ID", getAttribute("TRANSPORTER_ID").getInt());
            rsHistory.updateString("TRANSPORTER_NAME", getAttribute("TRANSPORTER_NAME").getString());
            rsHistory.updateDouble("AMOUNT_LIMIT", getAttribute("AMOUNT_LIMIT").getDouble());
            rsHistory.updateString("CST_NO", getAttribute("CST_NO").getString());
            rsHistory.updateString("CST_DATE", getAttribute("CST_DATE").getString());
            rsHistory.updateString("ECC_NO", getAttribute("ECC_NO").getString());
            rsHistory.updateString("ECC_DATE", getAttribute("ECC_DATE").getString());
            rsHistory.updateString("GSTIN_NO", getAttribute("GSTIN_NO").getString());
            rsHistory.updateString("GSTIN_DATE", getAttribute("GSTIN_DATE").getString());

            rsHistory.updateString("MAIN_ACCOUNT_CODE", getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsHistory.updateInt("OTHER_BANK_ID", getAttribute("OTHER_BANK_ID").getInt());
            rsHistory.updateString("OTHER_BANK_NAME", getAttribute("OTHER_BANK_NAME").getString());
            rsHistory.updateString("OTHER_BANK_ADDRESS", getAttribute("OTHER_BANK_ADDRESS").getString());
            rsHistory.updateString("OTHER_BANK_CITY", getAttribute("OTHER_BANK_CITY").getString());
            rsHistory.updateString("CATEGORY", getAttribute("CATEGORY").getString());
            rsHistory.updateString("INSURANCE_CODE", getAttribute("INSURANCE_CODE").getString());
            rsHistory.updateString("PAN_NO", getAttribute("PAN_NO").getString());
            rsHistory.updateString("PAN_DATE", getAttribute("PAN_DATE").getString());
            rsHistory.updateString("REMARKS", getAttribute("REMARKS").getString());
            rsHistory.updateDouble("DELAY_INTEREST_PER", getAttribute("DELAY_INTEREST_PER").getDouble());
            rsHistory.updateString("ACCOUNT_CODES", getAttribute("ACCOUNT_CODES").getString());
            rsHistory.updateString("MARKET_SEGMENT", getAttribute("MARKET_SEGMENT").getString());
            rsHistory.updateString("PRODUCT_SEGMENT", getAttribute("PRODUCT_SEGMENT").getString());
            rsHistory.updateString("CLIENT_CATEGORY", getAttribute("CLIENT_CATEGORY").getString());
            rsHistory.updateString("DESIGNER_INCHARGE", getAttribute("DESIGNER_INCHARGE").getString());
            rsHistory.updateInt("DO_NOT_ALLOW_INTEREST", getAttribute("DO_NOT_ALLOW_INTEREST").getInt());
            rsHistory.updateString("INCHARGE_CD", getAttribute("INCHARGE_CD").getString());
            rsHistory.updateString("ZONE", getAttribute("ZONE").getString());
            rsHistory.updateInt("DISTANCE_KM", getAttribute("DISTANCE_KM").getInt());
            rsHistory.updateInt("TAGGING_APPROVAL_IND", getAttribute("TAGGING_APPROVAL_IND").getInt());

            rsHistory.updateInt("PO_NO_REQUIRED", getAttribute("PO_NO_REQUIRED").getInt());
            rsHistory.updateInt("KEY_CLIENT_IND", getAttribute("KEY_CLIENT_IND").getInt());

            rsHistory.updateString("DELIVERY_MODE", getAttribute("DELIVERY_MODE").getString());

            rsHistory.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
            rsHistory.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("MODIFIED_DATE", getAttribute("MODIFIED_DATE").getString());
            rsHistory.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            rsHistory.updateBoolean("APPROVED", false);
            rsHistory.updateString("APPROVED_DATE", "0000-00-00");
            rsHistory.updateBoolean("CANCELLED", false);
            rsHistory.updateBoolean("CHANGED", true);
            rsHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("REJECTED", false);
            rsHistory.updateString("REJECTED_DATE", "0000-00-00");
            rsHistory.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsHistory.insertRow();

            //===================== Update the Approval Flow ======================//
            setAttribute("FROM", EITLERPGLOBAL.gUserID);

            ApprovalFlow ObjFlow = new ApprovalFlow();
            ObjFlow.CompanyID = (int) getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID = clsSalesPartyAmend.ModuleID;
            ObjFlow.DocNo = Long.toString((long) getAttribute("AMEND_ID").getVal());
            ObjFlow.DocDate = (String) getAttribute("AMEND_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "D_SAL_PARTY_AMEND_MASTER";
            ObjFlow.IsCreator = true;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "AMEND_ID";

            if (ObjFlow.Status.equals("H")) {
                ObjFlow.Status = "A";
                ObjFlow.To = ObjFlow.From;
                ObjFlow.UpdateFlow();
            } else {
                if (!ObjFlow.UpdateFlow()) {
                    LastError = ObjFlow.LastError;
                }
            }
            //================= Approval Flow Update complete ===================//

            AStatus = getAttribute("APPROVAL_STATUS").getString();
            //==========Sync Party data with other modules =============//
            if (AStatus.equals("F")) {
                AddPartyToFinance(getAttribute("PARTY_CODE").getString(), getAttribute("ACCOUNT_CODES").getString());
            }
            //==========================================================//

            MoveLast();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean Filter(String Condition) {
        Ready = false;

        try {
            String strSQL = "SELECT * FROM D_SAL_PARTY_AMEND_MASTER " + Condition;
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery(strSQL);

            if (!rsResultSet.first()) {
                setData();
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

    public boolean Update() {

        Statement stHistory, stHeader, stOHeader;
        ResultSet rsHistory, rsHeader, rsOHeader;
        boolean Validate = true;

        try {

            String theDocNo = getAttribute("PARTY_CODE").getString();

            //** Open History Table Connections **//
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHistory = stHistory.executeQuery("SELECT * FROM D_SAL_PARTY_AMEND_MASTER_H WHERE AMEND_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //** --------------------------------**//

            String AStatus = (String) getAttribute("APPROVAL_STATUS").getObj();

            if (AStatus.equals("F")) {
                String PartyCode = getAttribute("PARTY_CODE").getString();

                int ClsInd = data.getIntValueFromDB("SELECT PARTY_CLOSE_IND FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PARTY_CODE='" + PartyCode + "'");
                String tcsInd = data.getStringValueFromDB("SELECT COALESCE(TCS_ELIGIBILITY,0) AS TCS_ELIGIBILITY FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PARTY_CODE='" + PartyCode + "'");

                //Delete previous data
                data.Execute("DELETE FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PARTY_CODE='" + PartyCode + "' ");

                stOHeader = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                rsOHeader = stOHeader.executeQuery("SELECT * FROM D_SAL_PARTY_MASTER WHERE PARTY_CODE='1'");
                rsOHeader.first();
                rsOHeader.moveToInsertRow();
                rsOHeader.updateInt("COMPANY_ID", getAttribute("COMPANY_ID").getInt());
                rsOHeader.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
                rsOHeader.updateInt("PARTY_TYPE", getAttribute("PARTY_TYPE").getInt());
                rsOHeader.updateString("PARENT_PARTY_CODE", "");
                rsOHeader.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());

                rsOHeader.updateString("SEASON_CODE", getAttribute("SEASON_CODE").getString());
                rsOHeader.updateString("REG_DATE", getAttribute("REG_DATE").getString());

                rsOHeader.updateString("AREA_ID", getAttribute("AREA_ID").getString());
                rsOHeader.updateString("ADDRESS1", getAttribute("ADDRESS1").getString());
                rsOHeader.updateString("ADDRESS2", getAttribute("ADDRESS2").getString());
                rsOHeader.updateString("CITY_ID", getAttribute("CITY_ID").getString());
                rsOHeader.updateString("CITY_NAME", getAttribute("CITY_NAME").getString());
                rsOHeader.updateString("DISPATCH_STATION", getAttribute("DISPATCH_STATION").getString());
                rsOHeader.updateString("DISTRICT", getAttribute("DISTRICT").getString());
                rsOHeader.updateString("PINCODE", getAttribute("PINCODE").getString());
                rsOHeader.updateString("PHONE_NO", getAttribute("PHONE_NO").getString());
                rsOHeader.updateString("MOBILE_NO", getAttribute("MOBILE_NO").getString());
                rsOHeader.updateString("MOBILE_NO_2", getAttribute("MOBILE_NO_2").getString());
                rsOHeader.updateString("MOBILE_NO_3", getAttribute("MOBILE_NO_3").getString());
                rsOHeader.updateString("CORR_ADDRESS", getAttribute("CORR_ADDRESS").getString());
                rsOHeader.updateInt("STATE_ID", getAttribute("STATE_ID").getInt()); //new code by vivek on 16/09/2013 to add state & country.
                rsOHeader.updateInt("COUNTRY_ID", getAttribute("COUNTRY_ID").getInt()); //new code by vivek on 16/09/2013 to add state & country.
                rsOHeader.updateString("EMAIL", getAttribute("EMAIL").getString());
                rsOHeader.updateString("EMAIL_ID2", getAttribute("EMAIL_ID2").getString());
                rsOHeader.updateString("EMAIL_ID3", getAttribute("EMAIL_ID3").getString());
                rsOHeader.updateString("URL", getAttribute("URL").getString());
                rsOHeader.updateString("CONTACT_PERSON", getAttribute("CONTACT_PERSON").getString());
                rsOHeader.updateString("CONTACT_PERSON_2", getAttribute("CONTACT_PERSON_2").getString());
                rsOHeader.updateString("CONTACT_PERSON_3", getAttribute("CONTACT_PERSON_3").getString());
                rsOHeader.updateString("CONT_PERS_DESIGNATION", getAttribute("CONT_PERS_DESIGNATION").getString());
                rsOHeader.updateString("CONT_PERS_DESIGNATION_2", getAttribute("CONT_PERS_DESIGNATION_2").getString());
                rsOHeader.updateString("CONT_PERS_DESIGNATION_3", getAttribute("CONT_PERS_DESIGNATION_3").getString());
                rsOHeader.updateInt("BANK_ID", getAttribute("BANK_ID").getInt());
                rsOHeader.updateString("BANK_NAME", getAttribute("BANK_NAME").getString());
                rsOHeader.updateString("BANK_ADDRESS", getAttribute("BANK_ADDRESS").getString());
                rsOHeader.updateString("BANK_CITY", getAttribute("BANK_CITY").getString());
                rsOHeader.updateString("CHARGE_CODE", getAttribute("CHARGE_CODE").getString());
                rsOHeader.updateString("CHARGE_CODE_II", getAttribute("CHARGE_CODE_II").getString());
                rsOHeader.updateDouble("CREDIT_DAYS", getAttribute("CREDIT_DAYS").getDouble());
                rsOHeader.updateDouble("EXTRA_CREDIT_DAYS", getAttribute("EXTRA_CREDIT_DAYS").getDouble());
                rsOHeader.updateDouble("GRACE_CREDIT_DAYS", getAttribute("GRACE_CREDIT_DAYS").getDouble());
                rsOHeader.updateString("DOCUMENT_THROUGH", getAttribute("DOCUMENT_THROUGH").getString());
                rsOHeader.updateInt("TRANSPORTER_ID", getAttribute("TRANSPORTER_ID").getInt());
                rsOHeader.updateString("TRANSPORTER_NAME", getAttribute("TRANSPORTER_NAME").getString());
                rsOHeader.updateDouble("AMOUNT_LIMIT", getAttribute("AMOUNT_LIMIT").getDouble());
                rsOHeader.updateString("CST_NO", getAttribute("CST_NO").getString());
                rsOHeader.updateString("CST_DATE", getAttribute("CST_DATE").getString());
                rsOHeader.updateString("ECC_NO", getAttribute("ECC_NO").getString());
                rsOHeader.updateString("ECC_DATE", getAttribute("ECC_DATE").getString());
                rsOHeader.updateString("TIN_NO", getAttribute("TIN_NO").getString());
                rsOHeader.updateString("TIN_DATE", getAttribute("TIN_DATE").getString());
                rsOHeader.updateString("GSTIN_NO", getAttribute("GSTIN_NO").getString());
                rsOHeader.updateString("GSTIN_DATE", getAttribute("GSTIN_DATE").getString());

                rsOHeader.updateString("MAIN_ACCOUNT_CODE", getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsOHeader.updateInt("OTHER_BANK_ID", getAttribute("OTHER_BANK_ID").getInt());
                rsOHeader.updateString("OTHER_BANK_NAME", getAttribute("OTHER_BANK_NAME").getString());
                rsOHeader.updateString("OTHER_BANK_ADDRESS", getAttribute("OTHER_BANK_ADDRESS").getString());
                rsOHeader.updateString("OTHER_BANK_CITY", getAttribute("OTHER_BANK_CITY").getString());
                rsOHeader.updateString("PAN_NO", getAttribute("PAN_NO").getString());
                rsOHeader.updateString("PAN_DATE", getAttribute("PAN_DATE").getString());
                rsOHeader.updateString("CATEGORY", getAttribute("CATEGORY").getString());
                rsOHeader.updateString("INSURANCE_CODE", getAttribute("INSURANCE_CODE").getString());
                rsOHeader.updateString("REMARKS", getAttribute("REMARKS").getString());
                rsOHeader.updateDouble("DELAY_INTEREST_PER", getAttribute("DELAY_INTEREST_PER").getDouble());
                rsOHeader.updateString("ACCOUNT_CODES", getAttribute("ACCOUNT_CODES").getString());
                rsOHeader.updateString("MARKET_SEGMENT", getAttribute("MARKET_SEGMENT").getString());
                rsOHeader.updateString("PRODUCT_SEGMENT", getAttribute("PRODUCT_SEGMENT").getString());
                rsOHeader.updateString("CLIENT_CATEGORY", getAttribute("CLIENT_CATEGORY").getString());
                rsOHeader.updateString("DESIGNER_INCHARGE", getAttribute("DESIGNER_INCHARGE").getString());
                rsOHeader.updateInt("DO_NOT_ALLOW_INTEREST", getAttribute("DO_NOT_ALLOW_INTEREST").getInt());
                rsOHeader.updateString("INCHARGE_CD", getAttribute("INCHARGE_CD").getString());
                rsOHeader.updateString("ZONE", getAttribute("ZONE").getString());
                rsOHeader.updateInt("DISTANCE_KM", getAttribute("DISTANCE_KM").getInt());
                rsOHeader.updateInt("TAGGING_APPROVAL_IND", getAttribute("TAGGING_APPROVAL_IND").getInt());

                rsOHeader.updateInt("PO_NO_REQUIRED", getAttribute("PO_NO_REQUIRED").getInt());
                rsOHeader.updateInt("KEY_CLIENT_IND", getAttribute("KEY_CLIENT_IND").getInt());

                rsOHeader.updateInt("PARTY_CLOSE_IND", ClsInd);

                rsOHeader.updateString("DELIVERY_MODE", getAttribute("DELIVERY_MODE").getString());
                
                rsOHeader.updateString("TCS_ELIGIBILITY", tcsInd);

                rsOHeader.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
                rsOHeader.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
                rsOHeader.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
                rsOHeader.updateString("MODIFIED_DATE", getAttribute("MODIFIED_DATE").getString());
                rsOHeader.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
                rsOHeader.updateBoolean("APPROVED", true);
                rsOHeader.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsOHeader.updateBoolean("CANCELLED", false);
                rsOHeader.updateBoolean("CHANGED", true);
                rsOHeader.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsOHeader.updateBoolean("REJECTED", false);
                rsOHeader.updateString("REJECTED_DATE", "0000-00-00");
                rsOHeader.insertRow();

                clsSales_Party ObjParty = (clsSales_Party) clsSales_Party.getObjectEx(EITLERPGLOBAL.gCompanyID, PartyCode);

                //Set older data to current
                setAttribute("COMPANY_ID", ObjParty.getAttribute("COMPANY_ID").getInt());
                setAttribute("PARTY_CODE", ObjParty.getAttribute("PARTY_CODE").getString());
                setAttribute("OLD_PARTY_CODE", ObjParty.getAttribute("PARTY_CODE").getString());
                setAttribute("PARTY_TYPE", ObjParty.getAttribute("PARTY_TYPE").getInt());
                setAttribute("PARTY_NAME", ObjParty.getAttribute("PARTY_NAME").getString());

                setAttribute("SEASON_CODE", ObjParty.getAttribute("SEASON_CODE").getString());
                setAttribute("REG_DATE", ObjParty.getAttribute("REG_DATE").getString());

                setAttribute("AREA_ID", ObjParty.getAttribute("AREA_ID").getString());
                setAttribute("ADDRESS1", ObjParty.getAttribute("ADDRESS1").getString());
                setAttribute("ADDRESS2", ObjParty.getAttribute("ADDRESS2").getString());
                setAttribute("CITY_ID", ObjParty.getAttribute("CITY_ID").getString());
                setAttribute("DISPATCH_STATION", ObjParty.getAttribute("DISPATCH_STATION").getString());
                setAttribute("CITY_NAME", ObjParty.getAttribute("CITY_NAME").getString());
                setAttribute("DISTRICT", ObjParty.getAttribute("DISTRICT").getString());
                setAttribute("PINCODE", ObjParty.getAttribute("PINCODE").getString());
                setAttribute("PHONE_NO", ObjParty.getAttribute("PHONE_NO").getString());
                setAttribute("MOBILE_NO", ObjParty.getAttribute("MOBILE_NO").getString());
                setAttribute("MOBILE_NO_2", ObjParty.getAttribute("MOBILE_NO_2").getString());
                setAttribute("MOBILE_NO_3", ObjParty.getAttribute("MOBILE_NO_3").getString());
                setAttribute("CORR_ADDRESS", ObjParty.getAttribute("CORR_ADDRESS").getString());
                setAttribute("STATE_ID", ObjParty.getAttribute("STATE_ID").getInt()); //new code by vivek on 16/09/2013 to add state & country.
                setAttribute("COUNTRY_ID", ObjParty.getAttribute("COUNTRY_ID").getInt()); //new code by vivek on 16/09/2013 to add state & country.
                setAttribute("EMAIL", ObjParty.getAttribute("EMAIL").getString());
                setAttribute("EMAIL_ID2", ObjParty.getAttribute("EMAIL_ID2").getString());
                setAttribute("EMAIL_ID3", ObjParty.getAttribute("EMAIL_ID3").getString());

                setAttribute("URL", ObjParty.getAttribute("URL").getString());
                setAttribute("CONTACT_PERSON", ObjParty.getAttribute("CONTACT_PERSON").getString());
                setAttribute("CONTACT_PERSON_2", ObjParty.getAttribute("CONTACT_PERSON_2").getString());
                setAttribute("CONTACT_PERSON_3", ObjParty.getAttribute("CONTACT_PERSON_3").getString());
                setAttribute("CONT_PERS_DESIGNATION", ObjParty.getAttribute("CONT_PERS_DESIGNATION").getString());
                setAttribute("CONT_PERS_DESIGNATION_2", ObjParty.getAttribute("CONT_PERS_DESIGNATION_2").getString());
                setAttribute("CONT_PERS_DESIGNATION_3", ObjParty.getAttribute("CONT_PERS_DESIGNATION_3").getString());
                setAttribute("BANK_ID", ObjParty.getAttribute("BANK_ID").getInt());
                setAttribute("BANK_NAME", ObjParty.getAttribute("BANK_NAME").getString());
                setAttribute("BANK_ADDRESS", ObjParty.getAttribute("BANK_ADDRESS").getString());
                setAttribute("BANK_CITY", ObjParty.getAttribute("BANK_CITY").getString());
                setAttribute("CST_NO", ObjParty.getAttribute("CST_NO").getString());
                setAttribute("CST_DATE", ObjParty.getAttribute("CST_DATE").getString());
                setAttribute("ECC_NO", ObjParty.getAttribute("ECC_NO").getString());
                setAttribute("ECC_DATE", ObjParty.getAttribute("ECC_DATE").getString());
                setAttribute("TIN_NO", ObjParty.getAttribute("TIN_NO").getString());
                setAttribute("TIN_DATE", ObjParty.getAttribute("TIN_DATE").getString());
                setAttribute("GSTIN_NO", ObjParty.getAttribute("GSTIN_NO").getString());
                setAttribute("GSTIN_DATE", ObjParty.getAttribute("GSTIN_DATE").getString());

                setAttribute("MAIN_ACCOUNT_CODE", ObjParty.getAttribute("MAIN_ACCOUNT_CODE").getString());
                setAttribute("CHARGE_CODE", ObjParty.getAttribute("CHARGE_CODE").getString());
                setAttribute("CHARGE_CODE_II", ObjParty.getAttribute("CHARGE_CODE_II").getString());
                setAttribute("CREDIT_DAYS", ObjParty.getAttribute("CREDIT_DAYS").getDouble());
                setAttribute("EXTRA_CREDIT_DAYS", ObjParty.getAttribute("EXTRA_CREDIT_DAYS").getDouble());
                setAttribute("GRACE_CREDIT_DAYS", ObjParty.getAttribute("GRACE_CREDIT_DAYS").getDouble());
                setAttribute("DOCUMENT_THROUGH", ObjParty.getAttribute("DOCUMENT_THROUGH").getString());
                setAttribute("TRANSPORTER_ID", ObjParty.getAttribute("TRANSPORTER_ID").getInt());
                setAttribute("TRANSPORTER_NAME", ObjParty.getAttribute("TRANSPORTER_NAME").getString());
                setAttribute("AMOUNT_LIMIT", ObjParty.getAttribute("AMOUNT_LIMIT").getDouble());
                setAttribute("OTHER_BANK_ID", ObjParty.getAttribute("OTHER_BANK_ID").getInt());
                setAttribute("OTHER_BANK_NAME", ObjParty.getAttribute("OTHER_BANK_NAME").getString());
                setAttribute("OTHER_BANK_ADDRESS", ObjParty.getAttribute("OTHER_BANK_ADDRESS").getString());
                setAttribute("OTHER_BANK_CITY", ObjParty.getAttribute("OTHER_BANK_CITY").getString());
                setAttribute("PAN_NO", ObjParty.getAttribute("PAN_NO").getString());
                setAttribute("PAN_DATE", ObjParty.getAttribute("PAN_DATE").getString());
                setAttribute("CATEGORY", ObjParty.getAttribute("CATEGORY").getString());
                setAttribute("INSURANCE_CODE", ObjParty.getAttribute("INSURANCE_CODE").getString());
                setAttribute("REMARKS", ObjParty.getAttribute("REMARKS").getString());
                setAttribute("DELAY_INTEREST_PER", ObjParty.getAttribute("DELAY_INTEREST_PER").getDouble());
                setAttribute("ACCOUNT_CODES", ObjParty.getAttribute("ACCOUNT_CODES").getString());
                setAttribute("MARKET_SEGMENT", ObjParty.getAttribute("MARKET_SEGMENT").getString());
                setAttribute("PRODUCT_SEGMENT", ObjParty.getAttribute("PRODUCT_SEGMENT").getString());
                setAttribute("CLIENT_CATEGORY", ObjParty.getAttribute("CLIENT_CATEGORY").getString());
                setAttribute("DESIGNER_INCHARGE", ObjParty.getAttribute("DESIGNER_INCHARGE").getString());
                setAttribute("DO_NOT_ALLOW_INTEREST", ObjParty.getAttribute("DO_NOT_ALLOW_INTEREST").getInt());
                setAttribute("INCHARGE_CD", ObjParty.getAttribute("INCHARGE_CD").getString());
                setAttribute("ZONE", ObjParty.getAttribute("ZONE").getString());
                setAttribute("DISTANCE_KM", ObjParty.getAttribute("DISTANCE_KM").getInt());
                setAttribute("TAGGING_APPROVAL_IND", ObjParty.getAttribute("TAGGING_APPROVAL_IND").getInt());

                setAttribute("PO_NO_REQUIRED", ObjParty.getAttribute("PO_NO_REQUIRED").getInt());
                setAttribute("KEY_CLIENT_IND", ObjParty.getAttribute("KEY_CLIENT_IND").getInt());

                setAttribute("DELIVERY_MODE", ObjParty.getAttribute("DELIVERY_MODE").getString());

                setAttribute("CREATED_BY", ObjParty.getAttribute("CREATED_BY").getString());
                setAttribute("CREATED_DATE", ObjParty.getAttribute("CREATED_DATE").getString());
                setAttribute("MODIFIED_BY", ObjParty.getAttribute("MODIFIED_BY").getString());
                setAttribute("MODIFIED_DATE", ObjParty.getAttribute("MODIFIED_DATE").getString());

                setAttribute("HIERARCHY_ID", ObjParty.getAttribute("HIERARCHY_ID").getInt());

                setAttribute("APPROVED", ObjParty.getAttribute("APPROVED").getInt());
                setAttribute("APPROVED_DATE", ObjParty.getAttribute("APPROVED_DATE").getString());
                setAttribute("REJECTED", ObjParty.getAttribute("REJECTED").getBool());
                setAttribute("REJECTED_DATE", ObjParty.getAttribute("REJECTED_DATE").getString());
            }
            rsResultSet.updateLong("AMEND_ID", (long) getAttribute("AMEND_ID").getVal());
            rsResultSet.updateString("AMEND_DATE", (String) getAttribute("AMEND_DATE").getObj());
            rsResultSet.updateString("AMEND_REASON", (String) getAttribute("AMEND_REASON").getObj());
            rsResultSet.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsResultSet.updateInt("PARTY_TYPE", getAttribute("PARTY_TYPE").getInt());
            rsResultSet.updateString("PARENT_PARTY_CODE", "");
            rsResultSet.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());

            rsResultSet.updateString("SEASON_CODE", getAttribute("SEASON_CODE").getString());
            rsResultSet.updateString("REG_DATE", getAttribute("REG_DATE").getString());

            rsResultSet.updateString("AREA_ID", getAttribute("AREA_ID").getString());
            rsResultSet.updateString("ADDRESS1", getAttribute("ADDRESS1").getString());
            rsResultSet.updateString("ADDRESS2", getAttribute("ADDRESS2").getString());
            rsResultSet.updateString("CITY_ID", getAttribute("CITY_ID").getString());
            rsResultSet.updateString("DISPATCH_STATION", getAttribute("DISPATCH_STATION").getString());
            rsResultSet.updateString("CITY_NAME", getAttribute("CITY_NAME").getString());
            rsResultSet.updateString("DISTRICT", getAttribute("DISTRICT").getString());
            rsResultSet.updateString("PINCODE", getAttribute("PINCODE").getString());
            rsResultSet.updateString("PHONE_NO", getAttribute("PHONE_NO").getString());
            rsResultSet.updateString("MOBILE_NO", getAttribute("MOBILE_NO").getString());
            rsResultSet.updateString("MOBILE_NO_2", getAttribute("MOBILE_NO_2").getString());
            rsResultSet.updateString("MOBILE_NO_3", getAttribute("MOBILE_NO_3").getString());
            rsResultSet.updateString("CORR_ADDRESS", getAttribute("CORR_ADDRESS").getString());
            rsResultSet.updateInt("STATE_ID", getAttribute("STATE_ID").getInt()); //new code by vivek on 16/09/2013 to add state & country.
            rsResultSet.updateInt("COUNTRY_ID", getAttribute("COUNTRY_ID").getInt()); //new code by vivek on 16/09/2013 to add state & country.
            rsResultSet.updateString("EMAIL", getAttribute("EMAIL").getString());
            rsResultSet.updateString("EMAIL_ID2", getAttribute("EMAIL_ID2").getString());
            rsResultSet.updateString("EMAIL_ID3", getAttribute("EMAIL_ID3").getString());

            rsResultSet.updateString("URL", getAttribute("URL").getString());
            rsResultSet.updateString("CONTACT_PERSON", getAttribute("CONTACT_PERSON").getString());
            rsResultSet.updateString("CONTACT_PERSON_2", getAttribute("CONTACT_PERSON_2").getString());
            rsResultSet.updateString("CONTACT_PERSON_3", getAttribute("CONTACT_PERSON_3").getString());
            rsResultSet.updateString("CONT_PERS_DESIGNATION", getAttribute("CONT_PERS_DESIGNATION").getString());
            rsResultSet.updateString("CONT_PERS_DESIGNATION_2", getAttribute("CONT_PERS_DESIGNATION_2").getString());
            rsResultSet.updateString("CONT_PERS_DESIGNATION_3", getAttribute("CONT_PERS_DESIGNATION_3").getString());
            rsResultSet.updateInt("BANK_ID", getAttribute("BANK_ID").getInt());
            rsResultSet.updateString("BANK_NAME", getAttribute("BANK_NAME").getString());
            rsResultSet.updateString("BANK_ADDRESS", getAttribute("BANK_ADDRESS").getString());
            rsResultSet.updateString("BANK_CITY", getAttribute("BANK_CITY").getString());
            rsResultSet.updateString("CHARGE_CODE", getAttribute("CHARGE_CODE").getString());
            rsResultSet.updateString("CHARGE_CODE_II", getAttribute("CHARGE_CODE_II").getString());
            rsResultSet.updateDouble("CREDIT_DAYS", getAttribute("CREDIT_DAYS").getDouble());
            rsResultSet.updateDouble("EXTRA_CREDIT_DAYS", getAttribute("EXTRA_CREDIT_DAYS").getDouble());
            rsResultSet.updateDouble("GRACE_CREDIT_DAYS", getAttribute("GRACE_CREDIT_DAYS").getDouble());
            rsResultSet.updateString("DOCUMENT_THROUGH", getAttribute("DOCUMENT_THROUGH").getString());
            rsResultSet.updateInt("TRANSPORTER_ID", getAttribute("TRANSPORTER_ID").getInt());
            rsResultSet.updateString("TRANSPORTER_NAME", getAttribute("TRANSPORTER_NAME").getString());
            rsResultSet.updateDouble("AMOUNT_LIMIT", getAttribute("AMOUNT_LIMIT").getDouble());
            rsResultSet.updateString("CST_NO", getAttribute("CST_NO").getString());
            rsResultSet.updateString("CST_DATE", getAttribute("CST_DATE").getString());
            rsResultSet.updateString("ECC_NO", getAttribute("ECC_NO").getString());
            rsResultSet.updateString("ECC_DATE", getAttribute("ECC_DATE").getString());
            rsResultSet.updateString("TIN_NO", getAttribute("TIN_NO").getString());
            rsResultSet.updateString("TIN_DATE", getAttribute("TIN_DATE").getString());
            rsResultSet.updateString("PAN_NO", getAttribute("PAN_NO").getString());
            rsResultSet.updateString("PAN_DATE", getAttribute("PAN_DATE").getString());
            rsResultSet.updateString("GSTIN_NO", getAttribute("GSTIN_NO").getString());
            rsResultSet.updateString("GSTIN_DATE", getAttribute("GSTIN_DATE").getString());

            rsResultSet.updateString("CATEGORY", getAttribute("CATEGORY").getString());
            rsResultSet.updateString("INSURANCE_CODE", getAttribute("INSURANCE_CODE").getString());
            rsResultSet.updateString("REMARKS", getAttribute("REMARKS").getString());
            rsResultSet.updateDouble("DELAY_INTEREST_PER", getAttribute("DELAY_INTEREST_PER").getDouble());
            rsResultSet.updateString("ACCOUNT_CODES", getAttribute("ACCOUNT_CODES").getString());
            rsResultSet.updateString("MARKET_SEGMENT", getAttribute("MARKET_SEGMENT").getString());
            rsResultSet.updateString("PRODUCT_SEGMENT", getAttribute("PRODUCT_SEGMENT").getString());
            rsResultSet.updateString("CLIENT_CATEGORY", getAttribute("CLIENT_CATEGORY").getString());
            rsResultSet.updateString("DESIGNER_INCHARGE", getAttribute("DESIGNER_INCHARGE").getString());
            rsResultSet.updateInt("DO_NOT_ALLOW_INTEREST", getAttribute("DO_NOT_ALLOW_INTEREST").getInt());
            rsResultSet.updateString("INCHARGE_CD", getAttribute("INCHARGE_CD").getString());
            rsResultSet.updateString("MAIN_ACCOUNT_CODE", getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsResultSet.updateInt("OTHER_BANK_ID", getAttribute("OTHER_BANK_ID").getInt());
            rsResultSet.updateString("OTHER_BANK_NAME", getAttribute("OTHER_BANK_NAME").getString());
            rsResultSet.updateString("OTHER_BANK_ADDRESS", getAttribute("OTHER_BANK_ADDRESS").getString());
            rsResultSet.updateString("OTHER_BANK_CITY", getAttribute("OTHER_BANK_CITY").getString());

            rsResultSet.updateString("ZONE", getAttribute("ZONE").getString());
            rsResultSet.updateInt("DISTANCE_KM", getAttribute("DISTANCE_KM").getInt());
            rsResultSet.updateInt("TAGGING_APPROVAL_IND", getAttribute("TAGGING_APPROVAL_IND").getInt());

            rsResultSet.updateInt("PO_NO_REQUIRED", getAttribute("PO_NO_REQUIRED").getInt());
            rsResultSet.updateInt("KEY_CLIENT_IND", getAttribute("KEY_CLIENT_IND").getInt());

            rsResultSet.updateString("DELIVERY_MODE", getAttribute("DELIVERY_MODE").getString());

            rsResultSet.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE", getAttribute("MODIFIED_DATE").getString());
            rsResultSet.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            rsResultSet.updateBoolean("CANCELLED", false);
            rsResultSet.updateBoolean("CHANGED", true);
            rsResultSet.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("REJECTED", false);
            rsResultSet.updateString("REJECTED_DATE", "0000-00-00");
            rsResultSet.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsResultSet.updateRow();

            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_SAL_PARTY_AMEND_MASTER_H WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PARTY_CODE='" + (String) getAttribute("PARTY_CODE").getObj() + "'");
            RevNo++;

            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO", RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateString("UPDATED_BY", EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsHistory.updateInt("COMPANY_ID", getAttribute("COMPANY_ID").getInt());
            rsHistory.updateLong("AMEND_ID", (long) getAttribute("AMEND_ID").getVal());
            rsHistory.updateString("AMEND_DATE", (String) getAttribute("AMEND_DATE").getObj());
            rsHistory.updateString("AMEND_REASON", (String) getAttribute("AMEND_REASON").getObj());
            rsHistory.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsHistory.updateInt("PARTY_TYPE", getAttribute("PARTY_TYPE").getInt());
            rsHistory.updateString("PARENT_PARTY_CODE", "");
            rsHistory.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());

            rsHistory.updateString("SEASON_CODE", getAttribute("SEASON_CODE").getString());
            rsHistory.updateString("REG_DATE", getAttribute("REG_DATE").getString());

            rsHistory.updateString("AREA_ID", getAttribute("AREA_ID").getString());
            rsHistory.updateString("ADDRESS1", getAttribute("ADDRESS1").getString());
            rsHistory.updateString("ADDRESS2", getAttribute("ADDRESS2").getString());
            rsHistory.updateString("CITY_ID", getAttribute("CITY_ID").getString());
            rsHistory.updateString("DISPATCH_STATION", getAttribute("DISPATCH_STATION").getString());
            rsHistory.updateString("CITY_NAME", getAttribute("CITY_NAME").getString());
            rsHistory.updateString("DISTRICT", getAttribute("DISTRICT").getString());
            rsHistory.updateString("PINCODE", getAttribute("PINCODE").getString());
            rsHistory.updateString("PHONE_NO", getAttribute("PHONE_NO").getString());
            rsHistory.updateString("MOBILE_NO", getAttribute("MOBILE_NO").getString());
            rsHistory.updateString("MOBILE_NO_2", getAttribute("MOBILE_NO_2").getString());
            rsHistory.updateString("MOBILE_NO_3", getAttribute("MOBILE_NO_3").getString());
            rsHistory.updateString("CORR_ADDRESS", getAttribute("CORR_ADDRESS").getString());
            rsHistory.updateInt("STATE_ID", getAttribute("STATE_ID").getInt()); //new code by vivek on 16/09/2013 to add state & country.
            rsHistory.updateInt("COUNTRY_ID", getAttribute("COUNTRY_ID").getInt()); //new code by vivek on 16/09/2013 to add state & country.
            rsHistory.updateString("EMAIL", getAttribute("EMAIL").getString());
            rsHistory.updateString("EMAIL_ID2", getAttribute("EMAIL_ID2").getString());
            rsHistory.updateString("EMAIL_ID3", getAttribute("EMAIL_ID3").getString());

            rsHistory.updateString("URL", getAttribute("URL").getString());
            rsHistory.updateString("CONTACT_PERSON", getAttribute("CONTACT_PERSON").getString());
            rsHistory.updateString("CONTACT_PERSON_2", getAttribute("CONTACT_PERSON_2").getString());
            rsHistory.updateString("CONTACT_PERSON_3", getAttribute("CONTACT_PERSON_3").getString());
            rsHistory.updateString("CONT_PERS_DESIGNATION", getAttribute("CONT_PERS_DESIGNATION").getString());
            rsHistory.updateString("CONT_PERS_DESIGNATION_2", getAttribute("CONT_PERS_DESIGNATION_2").getString());
            rsHistory.updateString("CONT_PERS_DESIGNATION_3", getAttribute("CONT_PERS_DESIGNATION_3").getString());
            rsHistory.updateInt("BANK_ID", getAttribute("BANK_ID").getInt());
            rsHistory.updateString("BANK_NAME", getAttribute("BANK_NAME").getString());
            rsHistory.updateString("BANK_ADDRESS", getAttribute("BANK_ADDRESS").getString());
            rsHistory.updateString("BANK_CITY", getAttribute("BANK_CITY").getString());
            rsHistory.updateString("CHARGE_CODE", getAttribute("CHARGE_CODE").getString());
            rsHistory.updateString("CHARGE_CODE_II", getAttribute("CHARGE_CODE_II").getString());
            rsHistory.updateDouble("CREDIT_DAYS", getAttribute("CREDIT_DAYS").getDouble());
            rsHistory.updateDouble("EXTRA_CREDIT_DAYS", getAttribute("EXTRA_CREDIT_DAYS").getDouble());
            rsHistory.updateDouble("GRACE_CREDIT_DAYS", getAttribute("GRACE_CREDIT_DAYS").getDouble());
            rsHistory.updateString("DOCUMENT_THROUGH", getAttribute("DOCUMENT_THROUGH").getString());
            rsHistory.updateInt("TRANSPORTER_ID", getAttribute("TRANSPORTER_ID").getInt());
            rsHistory.updateString("TRANSPORTER_NAME", getAttribute("TRANSPORTER_NAME").getString());
            rsHistory.updateDouble("AMOUNT_LIMIT", getAttribute("AMOUNT_LIMIT").getDouble());
            rsHistory.updateString("CST_NO", getAttribute("CST_NO").getString());
            rsHistory.updateString("CST_DATE", getAttribute("CST_DATE").getString());
            rsHistory.updateString("ECC_NO", getAttribute("ECC_NO").getString());
            rsHistory.updateString("ECC_DATE", getAttribute("ECC_DATE").getString());
            rsHistory.updateString("TIN_NO", getAttribute("TIN_NO").getString());
            rsHistory.updateString("TIN_DATE", getAttribute("TIN_DATE").getString());
            rsHistory.updateString("GSTIN_NO", getAttribute("GSTIN_NO").getString());
            rsHistory.updateString("GSTIN_DATE", getAttribute("GSTIN_DATE").getString());

            rsHistory.updateString("MAIN_ACCOUNT_CODE", getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsHistory.updateInt("OTHER_BANK_ID", getAttribute("OTHER_BANK_ID").getInt());
            rsHistory.updateString("OTHER_BANK_NAME", getAttribute("OTHER_BANK_NAME").getString());
            rsHistory.updateString("OTHER_BANK_ADDRESS", getAttribute("OTHER_BANK_ADDRESS").getString());
            rsHistory.updateString("OTHER_BANK_CITY", getAttribute("OTHER_BANK_CITY").getString());
            rsHistory.updateString("PAN_NO", getAttribute("PAN_NO").getString());
            rsHistory.updateString("PAN_DATE", getAttribute("PAN_DATE").getString());
            rsHistory.updateString("CATEGORY", getAttribute("CATEGORY").getString());
            rsHistory.updateString("INSURANCE_CODE", getAttribute("INSURANCE_CODE").getString());
            rsHistory.updateString("REMARKS", getAttribute("REMARKS").getString());
            rsHistory.updateDouble("DELAY_INTEREST_PER", getAttribute("DELAY_INTEREST_PER").getDouble());
            rsHistory.updateString("ACCOUNT_CODES", getAttribute("ACCOUNT_CODES").getString());
            rsHistory.updateString("MARKET_SEGMENT", getAttribute("MARKET_SEGMENT").getString());
            rsHistory.updateString("PRODUCT_SEGMENT", getAttribute("PRODUCT_SEGMENT").getString());
            rsHistory.updateString("CLIENT_CATEGORY", getAttribute("CLIENT_CATEGORY").getString());
            rsHistory.updateString("DESIGNER_INCHARGE", getAttribute("DESIGNER_INCHARGE").getString());
            rsHistory.updateInt("DO_NOT_ALLOW_INTEREST", getAttribute("DO_NOT_ALLOW_INTEREST").getInt());
            rsHistory.updateString("INCHARGE_CD", getAttribute("INCHARGE_CD").getString());
            rsHistory.updateString("ZONE", getAttribute("ZONE").getString());
            rsHistory.updateInt("DISTANCE_KM", getAttribute("DISTANCE_KM").getInt());
            rsHistory.updateInt("TAGGING_APPROVAL_IND", getAttribute("TAGGING_APPROVAL_IND").getInt());

            rsHistory.updateInt("PO_NO_REQUIRED", getAttribute("PO_NO_REQUIRED").getInt());
            rsHistory.updateInt("KEY_CLIENT_IND", getAttribute("KEY_CLIENT_IND").getInt());
            

            rsHistory.updateString("DELIVERY_MODE", getAttribute("DELIVERY_MODE").getString());

            rsHistory.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
            rsHistory.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("MODIFIED_DATE", getAttribute("MODIFIED_DATE").getString());
            rsHistory.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            rsHistory.updateBoolean("APPROVED", false);
            rsHistory.updateString("APPROVED_DATE", "0000-00-00");
            rsHistory.updateBoolean("CANCELLED", false);
            rsHistory.updateBoolean("CHANGED", true);
            rsHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("REJECTED", false);
            rsHistory.updateString("REJECTED_DATE", "0000-00-00");
            rsHistory.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsHistory.insertRow();

            //======== Update the Approval Flow =========
            setAttribute("FROM", EITLERPGLOBAL.gUserID);

            ApprovalFlow ObjFlow = new ApprovalFlow();
            ObjFlow.CompanyID = (int) getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID = clsSalesPartyAmend.ModuleID;
            ObjFlow.DocNo = Long.toString((long) getAttribute("AMEND_ID").getVal());
            ObjFlow.DocDate = (String) getAttribute("AMEND_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "D_SAL_PARTY_AMEND_MASTER";
            ObjFlow.IsCreator = false;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "AMEND_ID";

            //==== Handling Rejected Documents ==========//
            AStatus = (String) getAttribute("APPROVAL_STATUS").getObj();

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
                data.Execute("UPDATE D_SAL_PARTY_AMEND_MASTER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND AMEND_ID='" + ObjFlow.DocNo + "' ");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + clsSalesPartyAmend.ModuleID + " AND DOC_NO='" + ObjFlow.DocNo + "'");

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

            //==========Sync Party data with other modules =============//
            if (AStatus.equals("F")) {
                AddPartyToFinance(getAttribute("PARTY_CODE").getString(), getAttribute("ACCOUNT_CODES").getString());
            }
            //==========================================================//
            //--------- Approval Flow Update complete -----------

            return true;
        } catch (Exception e) {

            LastError = e.getMessage();
            return false;
        }
    }

    public void AddPartyToFinance(String PartyCode, String MainAccCode) {
        //========== Add Party to Finance Party Master ===============//

        clsSalesPartyAmend ObjParty = new clsSalesPartyAmend();
        String sMainCode[] = MainAccCode.trim().split(",");
        String Code = "'";
        /*try {
         ResultSet rsCode = data.getResult("SELECT MAIN_ACCOUNT_CODE FROM D_SAL_PARTY_MAINCODE WHERE CANCELLED=0");
         rsCode.first();
         while (!rsCode.isAfterLast()) {
         Code = Code+rsCode.getString("MAIN_ACCOUNT_CODE").trim()+"','";
         rsCode.next();
         }
         }catch(Exception e) {
         }*/

        try {
            for (int i = 0; i < sMainCode.length; i++) {
                Code = Code + sMainCode[i] + "','";
            }
        } catch (Exception e) {

        }

        if (ObjParty.Filter("WHERE PARTY_CODE='" + PartyCode + "' AND MAIN_ACCOUNT_CODE='" + sMainCode[0].trim() + "' ")) {
            String str = "DELETE FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='" + PartyCode + "' "
                    + " AND MAIN_ACCOUNT_CODE IN (" + Code.substring(0, Code.length() - 2) + ") "; //sMainCode[0].trim()+"',"+
            data.Execute(str, FinanceGlobal.FinURL);

            for (int n = 0; n < sMainCode.length; n++) {
                String Main_Code = sMainCode[n].trim();
                try {
                    Connection FinConn;
                    Statement stFinParty;
                    ResultSet rsFinParty;

                    long Counter = data.getLongValueFromDB("SELECT MAX(PARTY_ID)+1 AS PARTY_ID FROM D_FIN_PARTY_MASTER", FinanceGlobal.FinURL);

                    FinConn = data.getConn(FinanceGlobal.FinURL);

                    stFinParty = FinConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    rsFinParty = stFinParty.executeQuery("SELECT * FROM D_FIN_PARTY_MASTER LIMIT 1");

                    rsFinParty.moveToInsertRow();
                    rsFinParty.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                    rsFinParty.updateString("PARTY_CODE", ObjParty.getAttribute("PARTY_CODE").getString());
                    rsFinParty.updateString("MAIN_ACCOUNT_CODE", Main_Code);
                    rsFinParty.updateLong("PARTY_ID", Counter);
                    rsFinParty.updateString("PARTY_NAME", ObjParty.getAttribute("PARTY_NAME").getString());
                    rsFinParty.updateString("SH_NAME", "");
                    rsFinParty.updateString("REMARKS", ObjParty.getAttribute("REMARKS").getString());
                    rsFinParty.updateString("SH_CODE", "");
                    rsFinParty.updateDouble("CREDIT_DAYS", ObjParty.getAttribute("CREDIT_DAYS").getDouble());
                    rsFinParty.updateDouble("CREDIT_LIMIT", 0);
                    rsFinParty.updateDouble("DEBIT_LIMIT", 0);
                    rsFinParty.updateString("TIN_NO", ObjParty.getAttribute("TIN_NO").getString());
                    rsFinParty.updateString("TIN_DATE", ObjParty.getAttribute("TIN_DATE").getString());
                    rsFinParty.updateString("CST_NO", ObjParty.getAttribute("CST_NO").getString());
                    rsFinParty.updateString("CST_DATE", ObjParty.getAttribute("CST_DATE").getString());
                    rsFinParty.updateString("ECC_NO", ObjParty.getAttribute("ECC_NO").getString());
                    rsFinParty.updateString("ECC_DATE", ObjParty.getAttribute("ECC_DATE").getString());
                    rsFinParty.updateString("GSTIN_NO", ObjParty.getAttribute("GSTIN_NO").getString());
                    rsFinParty.updateString("GSTIN_DATE", ObjParty.getAttribute("GSTIN_DATE").getString());
                    rsFinParty.updateString("SERVICE_TAX_NO", "");
                    rsFinParty.updateString("SERVICE_TAX_DATE", "0000-00-00");
                    rsFinParty.updateString("SSI_NO", "");
                    rsFinParty.updateString("SSI_DATE", "0000-00-00");
                    rsFinParty.updateString("ESI_NO", "");
                    rsFinParty.updateString("ESI_DATE", "0000-00-00");
                    rsFinParty.updateString("ADDRESS", ObjParty.getAttribute("ADDRESS1").getString().trim() + " " + ObjParty.getAttribute("ADDRESS2").getString().trim());
                    rsFinParty.updateString("CITY", ObjParty.getAttribute("CITY_ID").getString());
                    rsFinParty.updateString("PINCODE", ObjParty.getAttribute("PINCODE").getString());
                    //rsFinParty.updateString("STATE",ObjParty.getAttribute("DISTRICT").getString()); //commented by vivek on 16/09/2013 to add state & country.
                    //rsFinParty.updateString("COUNTRY",""); //commented by vivek on 16/09/2013 to add state & country.
                    rsFinParty.updateString("STATE", clsSales_Party.getStateName(ObjParty.getAttribute("STATE_ID").getInt())); //new code by vivek on 16/09/2013 to add state & country.
                    rsFinParty.updateString("COUNTRY", clsSales_Party.getCountryName(ObjParty.getAttribute("COUNTRY_ID").getInt())); //new code by vivek on 16/09/2013 to add state & country.
                    rsFinParty.updateString("PHONE", ObjParty.getAttribute("PHONE_NO").getString());
                    rsFinParty.updateString("FAX", "");
                    rsFinParty.updateString("MOBILE", ObjParty.getAttribute("MOBILE_NO").getString());
                    rsFinParty.updateString("EMAIL", ObjParty.getAttribute("EMAIL").getString());
                    rsFinParty.updateString("URL", ObjParty.getAttribute("URL").getString());

                    rsFinParty.updateInt("DISTANCE_KM", ObjParty.getAttribute("DISTANCE_KM").getInt()); //new code by vivek on 16/09/2013 to add state & country.

                    rsFinParty.updateInt("APPROVED", 1);
                    rsFinParty.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsFinParty.updateInt("REJECTED", 0);
                    rsFinParty.updateString("REJECTED_DATE", "0000-00-00");
                    rsFinParty.updateString("REJECTED_REMARKS", "");

                    rsFinParty.updateInt("HIERARCHY_ID", 0);
                    rsFinParty.updateInt("CANCELLED", 0);
                    rsFinParty.updateInt("BLOCKED", 0);
                    rsFinParty.updateString("PAN_NO", ObjParty.getAttribute("PAN_NO").getString());
                    rsFinParty.updateString("PAN_DATE", ObjParty.getAttribute("PAN_DATE").getString());
                    rsFinParty.updateInt("CHANGED", 0);
                    rsFinParty.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsFinParty.updateString("CREATED_BY", "System");
                    rsFinParty.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsFinParty.updateString("MODIFIED_BY", "System");
                    rsFinParty.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsFinParty.updateDouble("CLOSING_BALANCE", 0);
                    rsFinParty.updateString("CLOSING_EFFECT", "D");
                    rsFinParty.updateInt("PARTY_TYPE", ObjParty.getAttribute("PARTY_TYPE").getInt());
                    rsFinParty.updateString("CHARGE_CODE", ObjParty.getAttribute("CHARGE_CODE").getString());
                    rsFinParty.updateString("CHARGE_CODE_II", ObjParty.getAttribute("CHARGE_CODE_II").getString());
                    rsFinParty.updateInt("KEY_CLIENT_IND", ObjParty.getAttribute("KEY_CLIENT_IND").getInt());
                    rsFinParty.insertRow();

                    rsFinParty.close();
                } catch (Exception p) {
                    p.printStackTrace();
                }
            }
            //============================================================//
        }
    }

    public boolean CanDelete(int CompanyID, String PartyCode, int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";

        try {
            if (HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL = "SELECT COUNT(*) AS COUNT FROM D_SAL_PARTY_AMEND_MASTER WHERE COMPANY_ID=" + Integer.toString(CompanyID) + " AND AMEND_ID=" + PartyCode + " AND APPROVED=1";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID=" + Integer.toString(CompanyID) + " AND MODULE_ID=" + clsSalesPartyAmend.ModuleID + " AND DOC_NO='" + PartyCode + "' AND USER_ID=" + Integer.toString(pUserID) + " AND STATUS='W'";
                tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
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

    public boolean Delete() {
        try {
            String PartyCode = getAttribute("PARTY_CODE").getString();

            if (CanDelete(EITLERPGLOBAL.gCompanyID, PartyCode, EITLERPGLOBAL.gNewUserID)) {
                String strSQL = "DELETE FROM D_SAL_PARTY_AMEND_MASTER WHERE PARTY_CODE='" + PartyCode + "'";
                data.Execute(strSQL);

                LoadData(EITLERPGLOBAL.gCompanyID);
                return true;
            } else {
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false;
            }
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public Object getObject(int CompanyID, String PartyCode) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(CompanyID) + " AND PARTY_CODE='" + PartyCode + "' ";
        clsSalesPartyAmend objParty = new clsSalesPartyAmend();
        objParty.Filter(strCondition);
        return objParty;
    }

    public static void CancelAmendment(int pCompanyID, String pAmendNo) {
        ResultSet rsTmp = null;

        if (CanCancel(pCompanyID, pAmendNo)) {

            boolean Approved = false;

            try {
                rsTmp = data.getResult("SELECT APPROVED FROM D_SAL_PARTY_AMEND_MASTER WHERE COMPANY_ID=" + pCompanyID + " AND AMEND_ID=" + pAmendNo + " ");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    Approved = rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();

                if (!Approved) {
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID=" + pCompanyID + " AND DOC_NO='" + pAmendNo + "' AND MODULE_ID=" + clsSalesPartyAmend.ModuleID);
                }

                data.Execute("UPDATE D_SAL_PARTY_AMEND_MASTER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + pCompanyID + " AND AMEND_ID='" + pAmendNo + "' ");
            } catch (Exception e) {

            }
        }

    }

    public static boolean CanCancel(int pCompanyID, String pAmendNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean CanCancel = false;

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            rsTmp = stTmp.executeQuery("SELECT AMEND_ID FROM D_SAL_PARTY_AMEND_MASTER WHERE COMPANY_ID=" + pCompanyID + " AND AMEND_ID=" + pAmendNo + "  AND APPROVED=0 AND CANCELLED=0");
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

    public static HashMap getPendingApprovals(int pCompanyID, int pUserID, int pOrder) {
        String strSQL = "";
        Connection tmpConn;
        tmpConn = data.getConn();

        ResultSet rsTmp;
        Statement tmpStmt;

        HashMap List = new HashMap();
        long Counter = 0;

        try {
            if (pOrder == EITLERPGLOBAL.OnRecivedDate) {
                strSQL = "SELECT D_SAL_PARTY_AMEND_MASTER.AMEND_ID,D_SAL_PARTY_AMEND_MASTER.PARTY_CODE,D_SAL_PARTY_AMEND_MASTER.PARTY_NAME,RECEIVED_DATE,0 AS DEPT_ID FROM D_SAL_PARTY_AMEND_MASTER,D_COM_DOC_DATA WHERE D_SAL_PARTY_AMEND_MASTER.AMEND_ID=D_COM_DOC_DATA.DOC_NO AND D_SAL_PARTY_AMEND_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_SAL_PARTY_AMEND_MASTER.COMPANY_ID=" + Integer.toString(pCompanyID) + " AND D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=" + clsSalesPartyAmend.ModuleID + " AND D_SAL_PARTY_AMEND_MASTER.CANCELLED=0 ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT D_SAL_PARTY_AMEND_MASTER.AMEND_ID,D_SAL_PARTY_AMEND_MASTER.PARTY_CODE,D_SAL_PARTY_AMEND_MASTER.PARTY_NAME,RECEIVED_DATE,0 AS DEPT_ID FROM D_SAL_PARTY_AMEND_MASTER,D_COM_DOC_DATA WHERE D_SAL_PARTY_AMEND_MASTER.AMEND_ID=D_COM_DOC_DATA.DOC_NO AND D_SAL_PARTY_AMEND_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_SAL_PARTY_AMEND_MASTER.COMPANY_ID=" + Integer.toString(pCompanyID) + " AND D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=" + clsSalesPartyAmend.ModuleID + " AND D_SAL_PARTY_AMEND_MASTER.CANCELLED=0 ORDER BY D_SAL_PARTY_AMEND_MASTER.PARTY_CODE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT D_SAL_PARTY_AMEND_MASTER.AMEND_ID,D_SAL_PARTY_AMEND_MASTER.PARTY_CODE,D_SAL_PARTY_AMEND_MASTER.PARTY_NAME,RECEIVED_DATE,0 AS DEPT_ID FROM D_SAL_PARTY_AMEND_MASTER,D_COM_DOC_DATA WHERE D_SAL_PARTY_AMEND_MASTER.AMEND_ID=D_COM_DOC_DATA.DOC_NO AND D_SAL_PARTY_AMEND_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_SAL_PARTY_AMEND_MASTER.COMPANY_ID=" + Integer.toString(pCompanyID) + " AND D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=" + clsSalesPartyAmend.ModuleID + " AND D_SAL_PARTY_AMEND_MASTER.CANCELLED=0 ORDER BY D_SAL_PARTY_AMEND_MASTER.PARTY_CODE";
            }

            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {
                Counter = Counter + 1;
                clsSalesPartyAmend ObjItem = new clsSalesPartyAmend();

                //------------- Header Fields --------------------//
                ObjItem.setAttribute("AMEND_ID", rsTmp.getInt("AMEND_ID"));
                ObjItem.setAttribute("PARTY_CODE", rsTmp.getString("PARTY_CODE"));
                ObjItem.setAttribute("PARTY_NAME", rsTmp.getString("PARTY_NAME"));
                ObjItem.setAttribute("RECEIVED_DATE", rsTmp.getString("RECEIVED_DATE"));
                // ----------------- End of Header Fields ------------------------------------//

                //Put the prepared user object into list
                List.put(Long.toString(Counter), ObjItem);

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
            //Conn=data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsResultSet = Stmt.executeQuery("SELECT * FROM D_SAL_PARTY_AMEND_MASTER_H WHERE COMPANY_ID=" + pCompanyID + " AND AMEND_ID=" + pDocNo + " ORDER BY REVISION_NO");
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

            rsTmp = tmpStmt.executeQuery("SELECT * FROM D_SAL_PARTY_AMEND_MASTER " + pCondition);

            Counter = 0;
            while (rsTmp.next()) {
                Counter = Counter + 1;
                clsSalesPartyAmend ObjParty = new clsSalesPartyAmend();
                ObjParty.setAttribute("COMPANY_ID", rsTmp.getInt("COMPANY_ID"));
                ObjParty.setAttribute("AMEND_ID", rsTmp.getLong("AMEND_ID"));
                ObjParty.setAttribute("AMEND_DATE", rsTmp.getString("AMEND_DATE"));
                ObjParty.setAttribute("AMEND_REASON", rsTmp.getString("AMEND_REASON"));
                ObjParty.setAttribute("PARTY_CODE", rsTmp.getString("PARTY_CODE"));
                ObjParty.setAttribute("PARTY_TYPE", rsTmp.getInt("PARTY_TYPE"));
                ObjParty.setAttribute("PARTY_NAME", rsTmp.getString("PARTY_NAME"));

                ObjParty.setAttribute("SEASON_CODE", rsTmp.getString("SEASON_CODE"));
                ObjParty.setAttribute("REG_DATE", rsTmp.getString("REG_DATE"));

                ObjParty.setAttribute("AREA_ID", rsTmp.getString("AREA_ID"));
                ObjParty.setAttribute("ADDRESS1", rsTmp.getString("ADDRESS1"));
                ObjParty.setAttribute("ADDRESS2", rsTmp.getString("ADDRESS2"));
                ObjParty.setAttribute("CITY_ID", rsTmp.getString("CITY_ID"));
                ObjParty.setAttribute("DISPATCH_STATION", rsTmp.getString("DISPATCH_STATION"));
                ObjParty.setAttribute("CITY_NAME", rsTmp.getString("CITY_NAME"));
                ObjParty.setAttribute("DISTRICT", rsTmp.getString("DISTRICT"));
                ObjParty.setAttribute("PINCODE", rsTmp.getString("PINCODE"));
                ObjParty.setAttribute("PHONE_NO", rsTmp.getString("PHONE_NO"));
                ObjParty.setAttribute("MOBILE_NO", rsTmp.getString("MOBILE_NO"));
                ObjParty.setAttribute("MOBILE_NO_2", rsTmp.getString("MOBILE_NO_2"));
                ObjParty.setAttribute("MOBILE_NO_3", rsTmp.getString("MOBILE_NO_3"));
                ObjParty.setAttribute("CORR_ADDRESS", rsTmp.getString("CORR_ADDRESS"));
                ObjParty.setAttribute("STATE_ID", rsTmp.getInt("STATE_ID")); //new code by vivek on 16/09/2013 to add state & country.
                ObjParty.setAttribute("COUNTRY_ID", rsTmp.getInt("COUNTRY_ID")); //new code by vivek on 16/09/2013 to add state & country.
                ObjParty.setAttribute("EMAIL", rsTmp.getString("EMAIL"));
                ObjParty.setAttribute("EMAIL_ID2", rsTmp.getString("EMAIL_ID2"));
                ObjParty.setAttribute("EMAIL_ID3", rsTmp.getString("EMAIL_ID3"));

                ObjParty.setAttribute("URL", rsTmp.getString("URL"));
                ObjParty.setAttribute("CONTACT_PERSON", rsTmp.getString("CONTACT_PERSON"));
                ObjParty.setAttribute("CONTACT_PERSON_2", rsTmp.getString("CONTACT_PERSON_2"));
                ObjParty.setAttribute("CONTACT_PERSON_3", rsTmp.getString("CONTACT_PERSON_3"));
                ObjParty.setAttribute("CONT_PERS_DESIGNATION", rsTmp.getString("CONT_PERS_DESIGNATION"));
                ObjParty.setAttribute("CONT_PERS_DESIGNATION_2", rsTmp.getString("CONT_PERS_DESIGNATION_2"));
                ObjParty.setAttribute("CONT_PERS_DESIGNATION_3", rsTmp.getString("CONT_PERS_DESIGNATION_3"));
                ObjParty.setAttribute("BANK_ID", rsTmp.getLong("BANK_ID"));
                ObjParty.setAttribute("BANK_NAME", rsTmp.getString("BANK_NAME"));
                ObjParty.setAttribute("BANK_ADDRESS", rsTmp.getString("BANK_ADDRESS"));
                ObjParty.setAttribute("BANK_CITY", rsTmp.getString("BANK_CITY"));
                ObjParty.setAttribute("CST_NO", rsTmp.getString("CST_NO"));
                ObjParty.setAttribute("CST_DATE", rsTmp.getString("CST_DATE"));
                ObjParty.setAttribute("ECC_NO", rsTmp.getString("ECC_NO"));
                ObjParty.setAttribute("ECC_DATE", rsTmp.getString("ECC_DATE"));
                ObjParty.setAttribute("TIN_NO", rsTmp.getString("TIN_NO"));
                ObjParty.setAttribute("TIN_DATE", rsTmp.getString("TIN_DATE"));
                ObjParty.setAttribute("PAN_NO", rsTmp.getString("PAN_NO"));
                ObjParty.setAttribute("PAN_DATE", rsTmp.getString("PAN_DATE"));
                ObjParty.setAttribute("GSTIN_NO", rsTmp.getString("GSTIN_NO"));
                ObjParty.setAttribute("GSTIN_DATE", rsTmp.getString("GSTIN_DATE"));

                ObjParty.setAttribute("CATEGORY", rsTmp.getString("CATEGORY"));
                ObjParty.setAttribute("INSURANCE_CODE", rsTmp.getString("INSURANCE_CODE"));
                ObjParty.setAttribute("REMARKS", rsTmp.getString("REMARKS"));
                ObjParty.setAttribute("MAIN_ACCOUNT_CODE", rsTmp.getString("MAIN_ACCOUNT_CODE"));
                ObjParty.setAttribute("CHARGE_CODE", rsTmp.getString("CHARGE_CODE"));
                ObjParty.setAttribute("CHARGE_CODE_II", rsTmp.getString("CHARGE_CODE_II"));
                ObjParty.setAttribute("CREDIT_DAYS", rsTmp.getDouble("CREDIT_DAYS"));
                ObjParty.setAttribute("EXTRA_CREDIT_DAYS", rsTmp.getDouble("EXTRA_CREDIT_DAYS"));
                ObjParty.setAttribute("GRACE_CREDIT_DAYS", rsTmp.getDouble("GRACE_CREDIT_DAYS"));
                ObjParty.setAttribute("DOCUMENT_THROUGH", rsTmp.getString("DOCUMENT_THROUGH"));
                ObjParty.setAttribute("TRANSPORTER_ID", rsTmp.getLong("TRANSPORTER_ID"));
                ObjParty.setAttribute("TRANSPORTER_NAME", rsTmp.getString("TRANSPORTER_NAME"));
                ObjParty.setAttribute("AMOUNT_LIMIT", rsTmp.getDouble("AMOUNT_LIMIT"));
                ObjParty.setAttribute("INCHARGE_CD", rsTmp.getString("INCHARGE_CD"));
                ObjParty.setAttribute("ZONE", rsTmp.getString("ZONE"));
                ObjParty.setAttribute("DISTANCE_KM", rsTmp.getInt("DISTANCE_KM"));

                ObjParty.setAttribute("DELIVERY_MODE", rsTmp.getString("DELIVERY_MODE"));

                String gMainCode = UtilFunctions.getString(rsTmp, "MAIN_ACCOUNT_CODE", "");
                String gArea = UtilFunctions.getString(rsTmp, "ZONE", "");
                if (gMainCode.equals("210010") && (gArea.equals("SOUTH") || gArea.equals("NORTH") || gArea.equals("EAST/WEST"))) {
                    ObjParty.setAttribute("ZONE", "OTHER");
                } else {
                    ObjParty.setAttribute("ZONE", UtilFunctions.getString(rsTmp, "ZONE", ""));
                }

                ObjParty.setAttribute("OTHER_BANK_ID", rsTmp.getLong("OTHER_BANK_ID"));
                ObjParty.setAttribute("OTHER_BANK_NAME", rsTmp.getString("OTHER_BANK_NAME"));
                ObjParty.setAttribute("OTHER_BANK_ADDRESS", rsTmp.getString("OTHER_BANK_ADDRESS"));
                ObjParty.setAttribute("OTHER_BANK_CITY", rsTmp.getString("OTHER_BANK_CITY"));
                ObjParty.setAttribute("DELAY_INTEREST_PER", rsTmp.getDouble("DELAY_INTEREST_PER"));
                ObjParty.setAttribute("CREATED_BY", rsTmp.getInt("CREATED_BY"));
                ObjParty.setAttribute("CREATED_DATE", rsTmp.getString("CREATED_DATE"));
                ObjParty.setAttribute("MODIFIED_BY", rsTmp.getInt("MODIFIED_BY"));
                ObjParty.setAttribute("MODIFIED_DATE", rsTmp.getString("MODIFIED_DATE"));

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
        clsSalesPartyAmend ObjParty = new clsSalesPartyAmend();
        ObjParty.LoadData(pCompanyID);
        ObjParty.Filter(strCondition);
        return ObjParty;
    }

    public static int getAmendmentNo(int pCompanyID, String pPartyCode) {
        String strSQL = "";

        ResultSet rsTmp;

        int AmendNo = 0;

        try {
            rsTmp = data.getResult("SELECT COUNT(*) AS THECOUNT FROM D_SAL_PARTY_AMEND_MASTER WHERE COMPANY_ID=" + pCompanyID + " AND PARTY_CODE='" + pPartyCode + "' AND PARTY_CODE IN (SELECT PARTY_CODE FROM D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + pPartyCode + "' AND APPROVED=1)");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                AmendNo = rsTmp.getInt("THECOUNT");
            }

        } catch (Exception e) {
        }

        return AmendNo;
    }

    public static int getUnderApprovalCount(int pCompanyID, String pPartyCode) {
        String strSQL = "";

        ResultSet rsTmp;

        int AmendCount = 0;

        try {
            rsTmp = data.getResult("SELECT COUNT(*) AS THECOUNT FROM D_SAL_PARTY_AMEND_MASTER WHERE COMPANY_ID=" + pCompanyID + " AND PARTY_CODE='" + pPartyCode + "' AND APPROVED=0 AND CANCELLED=0");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                AmendCount = rsTmp.getInt("THECOUNT");
            }

        } catch (Exception e) {
        }

        return AmendCount;
    }

    public static HashMap getUpdationHistory(int pCompanyID, String pPartyCode) {
        String strSQL = "";
        HashMap List = new HashMap();
        ResultSet rsTmp;

        try {

            rsTmp = data.getResult("SELECT AMEND_ID,AMEND_DATE,AMEND_REASON FROM D_SAL_PARTY_AMEND_MASTER WHERE PARTY_CODE='" + pPartyCode + "' AND APPROVED=1 ORDER BY AMEND_ID DESC");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {

                while (!rsTmp.isAfterLast()) {

                    clsSalesPartyAmend ObjParty = new clsSalesPartyAmend();

                    ObjParty.setAttribute("AMEND_ID", rsTmp.getInt("AMEND_ID"));
                    ObjParty.setAttribute("AMEND_DATE", rsTmp.getString("AMEND_DATE"));
                    ObjParty.setAttribute("AMEND_REASON", rsTmp.getString("AMEND_REASON"));

                    List.put(Integer.toString(List.size() + 1), ObjParty);
                    rsTmp.next();
                }
            }

        } catch (Exception e) {
        }

        return List;
    }

}
