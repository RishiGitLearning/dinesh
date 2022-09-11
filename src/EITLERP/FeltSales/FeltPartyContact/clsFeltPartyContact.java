/*
 * clsFeltRateMaster.java
 *
 * Created on September 3, 2013, 5:10 PM
 */
package EITLERP.FeltSales.FeltPartyContact;

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

/**
 *
 * @author Vivek Kumar
 */
public class clsFeltPartyContact {

    public String LastError = "";
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private HashMap props;
    public boolean Ready = false;
    //History Related properties
    public boolean HistoryView = false;
    //Rate Details Collection
    public HashMap hmRateDetails;

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
     * Creates new clsFeltRateMaster
     */
    public clsFeltPartyContact() {
        LastError = "";
        props = new HashMap();

        props.put("DOC_NO", new Variant(""));
        props.put("DOC_DATE", new Variant(""));
        props.put("PARTY_CODE", new Variant(""));
        props.put("PARTY_NAME", new Variant(""));
        props.put("CORR_ADDRESS", new Variant(""));
        props.put("PHONE_NO", new Variant(""));
        props.put("PIN_CODE", new Variant(""));
        props.put("INCHARGE_CD", new Variant(""));
        props.put("INCHARGE_NAME", new Variant(""));
        props.put("ZONE", new Variant(""));
        props.put("TRANSPORTER_ID", new Variant(""));
        props.put("TRANSPORTER_NAME", new Variant(""));
        props.put("DELIVERY_MODE", new Variant(""));
        props.put("CONTACT_PERSON", new Variant(""));
        props.put("CONT_PERS_DESIGNATION", new Variant(""));
        props.put("MOBILE_NO", new Variant(""));
        props.put("EMAIL", new Variant(""));
        props.put("CONTACT_PERSON_2", new Variant(""));
        props.put("CONT_PERS_DESIGNATION_2", new Variant(""));
        props.put("MOBILE_NO_2", new Variant(""));
        props.put("EMAIL_ID2", new Variant(""));
        props.put("CONTACT_PERSON_3", new Variant(""));
        props.put("CONT_PERS_DESIGNATION_3", new Variant(""));
        props.put("MOBILE_NO_3", new Variant(""));
        props.put("EMAIL_ID3", new Variant(""));
        
        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("APPROVED", new Variant(0));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED", new Variant(0));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("REJECTED_REMARKS", new Variant(""));
        props.put("CANCELED", new Variant(""));
        props.put("HIERARCHY_ID", new Variant(""));
        props.put("CHANGED", new Variant(""));
        props.put("CHANGED_DATE", new Variant(""));
        
        // -- Approval Specific Fields --
        props.put("FROM", new Variant(0));
        props.put("TO", new Variant(0));
        props.put("REMARKS", new Variant(""));
        props.put("APPROVAL_STATUS", new Variant(""));

        //Create a new object for clsFeltRateMaster Item collection
        hmRateDetails = new HashMap();

    }

    public boolean LoadData() {
        Ready = false;
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL ORDER BY DOC_NO");
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
            return false;
        }
    }

    public boolean Insert() {
        ResultSet resultSetHistory;
        Statement statementHistory;

        try {
            // ---- Rate Master History Connection ------ //
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval

            setAttribute("DOC_NO", getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 634, 340, true));

            resultSet.moveToInsertRow();
            resultSet.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSet.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            
            resultSet.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            resultSet.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
            resultSet.updateString("CORR_ADDRESS", getAttribute("CORR_ADDRESS").getString());
            resultSet.updateString("PHONE_NO", getAttribute("PHONE_NO").getString());
            resultSet.updateString("PIN_CODE", getAttribute("PIN_CODE").getString());
            resultSet.updateString("ZONE", getAttribute("ZONE").getString());
            resultSet.updateString("INCHARGE_CD", getAttribute("INCHARGE_CD").getString());
            resultSet.updateString("INCHARGE_NAME", getAttribute("INCHARGE_NAME").getString());
            resultSet.updateString("TRANSPORTER_ID", getAttribute("TRANSPORTER_ID").getString());
            resultSet.updateString("TRANSPORTER_NAME", getAttribute("TRANSPORTER_NAME").getString());
            resultSet.updateString("DELIVERY_MODE", getAttribute("DELIVERY_MODE").getString());
            
            resultSet.updateString("CONTACT_PERSON", getAttribute("CONTACT_PERSON").getString());
            resultSet.updateString("CONT_PERS_DESIGNATION", getAttribute("CONT_PERS_DESIGNATION").getString());
            resultSet.updateString("MOBILE_NO", getAttribute("MOBILE_NO").getString());
            resultSet.updateString("EMAIL", getAttribute("EMAIL").getString());
            
            resultSet.updateString("CONTACT_PERSON_2", getAttribute("CONTACT_PERSON_2").getString());
            resultSet.updateString("CONT_PERS_DESIGNATION_2", getAttribute("CONT_PERS_DESIGNATION_2").getString());
            resultSet.updateString("MOBILE_NO_2", getAttribute("MOBILE_NO_2").getString());
            resultSet.updateString("EMAIL_ID2", getAttribute("EMAIL_ID2").getString());
            
            resultSet.updateString("CONTACT_PERSON_3", getAttribute("CONTACT_PERSON_3").getString());
            resultSet.updateString("CONT_PERS_DESIGNATION_3", getAttribute("CONT_PERS_DESIGNATION_3").getString());
            resultSet.updateString("MOBILE_NO_3", getAttribute("MOBILE_NO_3").getString());
            resultSet.updateString("EMAIL_ID3", getAttribute("EMAIL_ID3").getString());
            
            resultSet.updateString("REMARKS", getAttribute("REMARKS").getString());

            resultSet.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
            resultSet.updateString("CREATED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSet.updateInt("MODIFIED_BY", 0);
            resultSet.updateString("MODIFIED_DATE", "0000-00-00");
            resultSet.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            resultSet.updateBoolean("APPROVED", false);
            resultSet.updateString("APPROVED_DATE", "0000-00-00");
            resultSet.updateBoolean("REJECTED", false);
            resultSet.updateString("REJECTED_DATE", "0000-00-00");
            resultSet.updateBoolean("CANCELED", false);
            resultSet.updateBoolean("CHANGED", true);
            resultSet.updateString("CHANGED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSet.insertRow();

            
            //========= Inserting Into History =================//
            resultSetHistory.moveToInsertRow();
            resultSetHistory.updateInt("REVISION_NO", 1); //Revision No. 1 in case of Insert

            resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSetHistory.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            
            resultSetHistory.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            resultSetHistory.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
            resultSetHistory.updateString("CORR_ADDRESS", getAttribute("CORR_ADDRESS").getString());
            resultSetHistory.updateString("PHONE_NO", getAttribute("PHONE_NO").getString());
            resultSetHistory.updateString("PIN_CODE", getAttribute("PIN_CODE").getString());
            resultSetHistory.updateString("ZONE", getAttribute("ZONE").getString());
            resultSetHistory.updateString("INCHARGE_CD", getAttribute("INCHARGE_CD").getString());
            resultSetHistory.updateString("INCHARGE_NAME", getAttribute("INCHARGE_NAME").getString());
            resultSetHistory.updateString("TRANSPORTER_ID", getAttribute("TRANSPORTER_ID").getString());
            resultSetHistory.updateString("TRANSPORTER_NAME", getAttribute("TRANSPORTER_NAME").getString());
            resultSetHistory.updateString("DELIVERY_MODE", getAttribute("DELIVERY_MODE").getString());
            
            resultSetHistory.updateString("CONTACT_PERSON", getAttribute("CONTACT_PERSON").getString());
            resultSetHistory.updateString("CONT_PERS_DESIGNATION", getAttribute("CONT_PERS_DESIGNATION").getString());
            resultSetHistory.updateString("MOBILE_NO", getAttribute("MOBILE_NO").getString());
            resultSetHistory.updateString("EMAIL", getAttribute("EMAIL").getString());
            
            resultSetHistory.updateString("CONTACT_PERSON_2", getAttribute("CONTACT_PERSON_2").getString());
            resultSetHistory.updateString("CONT_PERS_DESIGNATION_2", getAttribute("CONT_PERS_DESIGNATION_2").getString());
            resultSetHistory.updateString("MOBILE_NO_2", getAttribute("MOBILE_NO_2").getString());
            resultSetHistory.updateString("EMAIL_ID2", getAttribute("EMAIL_ID2").getString());
            
            resultSetHistory.updateString("CONTACT_PERSON_3", getAttribute("CONTACT_PERSON_3").getString());
            resultSetHistory.updateString("CONT_PERS_DESIGNATION_3", getAttribute("CONT_PERS_DESIGNATION_3").getString());
            resultSetHistory.updateString("MOBILE_NO_3", getAttribute("MOBILE_NO_3").getString());
            resultSetHistory.updateString("EMAIL_ID3", getAttribute("EMAIL_ID3").getString());
            
            resultSetHistory.updateString("REMARKS", getAttribute("REMARKS").getString());

            resultSetHistory.updateInt("UPDATED_BY", getAttribute("UPDATED_BY").getInt());
            resultSetHistory.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateTimeDB());
            resultSetHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            resultSetHistory.updateString("APPROVER_REMARKS", getAttribute("FROM_REMARKS").getString());
            resultSetHistory.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            resultSetHistory.updateBoolean("CHANGED", true);
            resultSetHistory.updateString("CHANGED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            
            ResultSet rsTmp=data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");
            
            resultSetHistory.updateString("FROM_IP",""+str_split[1]);
            resultSetHistory.insertRow();

            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = 634; //Felt RATE MASTER
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("DOC_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL";
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

            // Update Rate Detail and Rate detail history table
            if (ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {
                ObjFeltProductionApprovalFlow.finalApproved = false;
                data.Execute("UPDATE DINESHMILLS.D_SAL_PARTY_MASTER PM, PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL ED "
                        + "SET PM.CORR_ADDRESS = ED.CORR_ADDRESS, PM.PHONE_NO = ED.PHONE_NO, PM.PINCODE = ED.PIN_CODE, PM.ZONE = ED.ZONE, PM.DELIVERY_MODE = ED.DELIVERY_MODE, "
                        + "PM.INCHARGE_CD = ED.INCHARGE_CD, PM.TRANSPORTER_ID = ED.TRANSPORTER_ID, PM.TRANSPORTER_NAME = ED.TRANSPORTER_NAME, "
                        + "PM.CONTACT_PERSON = ED.CONTACT_PERSON, PM.CONT_PERS_DESIGNATION = ED.CONT_PERS_DESIGNATION, PM.MOBILE_NO = ED.MOBILE_NO, PM.EMAIL = ED.EMAIL, "
                        + "PM.CONTACT_PERSON_2 = ED.CONTACT_PERSON_2, PM.CONT_PERS_DESIGNATION_2 = ED.CONT_PERS_DESIGNATION_2, "
                        + "PM.MOBILE_NO_2 = ED.MOBILE_NO_2, PM.EMAIL_ID2 = ED.EMAIL_ID2, "
                        + "PM.CONTACT_PERSON_3 = ED.CONTACT_PERSON_3, PM.CONT_PERS_DESIGNATION_3 = ED.CONT_PERS_DESIGNATION_3, "
                        + "PM.MOBILE_NO_3 = ED.MOBILE_NO_3, PM.EMAIL_ID3 = ED.EMAIL_ID3 "
                        + "WHERE PM.PARTY_CODE = ED.PARTY_CODE AND ED.DOC_NO ='" + getAttribute("DOC_NO").getString() + "' AND ED.APPROVED = 1 ");
            }

            MoveLast();
            resultSetHistory.close();
            statementHistory.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }

    //Updates current record
    public boolean Update() {
        Statement statementHistory;
        ResultSet resultSetHistory;

        try {
            // ---- Rate Master History Connection ------ //
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval

            resultSet.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            resultSet.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
            resultSet.updateString("CORR_ADDRESS", getAttribute("CORR_ADDRESS").getString());
            resultSet.updateString("PHONE_NO", getAttribute("PHONE_NO").getString());
            resultSet.updateString("PIN_CODE", getAttribute("PIN_CODE").getString());
            resultSet.updateString("ZONE", getAttribute("ZONE").getString());
            resultSet.updateString("INCHARGE_CD", getAttribute("INCHARGE_CD").getString());
            resultSet.updateString("INCHARGE_NAME", getAttribute("INCHARGE_NAME").getString());
            resultSet.updateString("TRANSPORTER_ID", getAttribute("TRANSPORTER_ID").getString());
            resultSet.updateString("TRANSPORTER_NAME", getAttribute("TRANSPORTER_NAME").getString());
            resultSet.updateString("DELIVERY_MODE", getAttribute("DELIVERY_MODE").getString());
            
            resultSet.updateString("CONTACT_PERSON", getAttribute("CONTACT_PERSON").getString());
            resultSet.updateString("CONT_PERS_DESIGNATION", getAttribute("CONT_PERS_DESIGNATION").getString());
            resultSet.updateString("MOBILE_NO", getAttribute("MOBILE_NO").getString());
            resultSet.updateString("EMAIL", getAttribute("EMAIL").getString());
            
            resultSet.updateString("CONTACT_PERSON_2", getAttribute("CONTACT_PERSON_2").getString());
            resultSet.updateString("CONT_PERS_DESIGNATION_2", getAttribute("CONT_PERS_DESIGNATION_2").getString());
            resultSet.updateString("MOBILE_NO_2", getAttribute("MOBILE_NO_2").getString());
            resultSet.updateString("EMAIL_ID2", getAttribute("EMAIL_ID2").getString());
            
            resultSet.updateString("CONTACT_PERSON_3", getAttribute("CONTACT_PERSON_3").getString());
            resultSet.updateString("CONT_PERS_DESIGNATION_3", getAttribute("CONT_PERS_DESIGNATION_3").getString());
            resultSet.updateString("MOBILE_NO_3", getAttribute("MOBILE_NO_3").getString());
            resultSet.updateString("EMAIL_ID3", getAttribute("EMAIL_ID3").getString());
            
            resultSet.updateString("REMARKS", getAttribute("REMARKS").getString());
            
            resultSet.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
            resultSet.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
            resultSet.updateInt("MODIFIED_BY", getAttribute("MODIFIED_BY").getInt());
            resultSet.updateString("MODIFIED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSet.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            resultSet.updateBoolean("CHANGED", true);
            resultSet.updateString("CHANGED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSet.updateBoolean("CANCELED", false);
            resultSet.updateRow();

            //Get the Maximum Revision No.
            int RevNo = data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL_H WHERE DOC_NO='" + getAttribute("DOC_NO").getString() + "'");
            RevNo++;

            resultSetHistory.moveToInsertRow();
            resultSetHistory.updateInt("REVISION_NO", RevNo);

            resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSetHistory.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            resultSetHistory.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            resultSetHistory.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
            resultSetHistory.updateString("CORR_ADDRESS", getAttribute("CORR_ADDRESS").getString());
            resultSetHistory.updateString("PHONE_NO", getAttribute("PHONE_NO").getString());
            resultSetHistory.updateString("PIN_CODE", getAttribute("PIN_CODE").getString());
            resultSetHistory.updateString("ZONE", getAttribute("ZONE").getString());
            resultSetHistory.updateString("INCHARGE_CD", getAttribute("INCHARGE_CD").getString());
            resultSetHistory.updateString("INCHARGE_NAME", getAttribute("INCHARGE_NAME").getString());
            resultSetHistory.updateString("TRANSPORTER_ID", getAttribute("TRANSPORTER_ID").getString());
            resultSetHistory.updateString("TRANSPORTER_NAME", getAttribute("TRANSPORTER_NAME").getString());
            resultSetHistory.updateString("DELIVERY_MODE", getAttribute("DELIVERY_MODE").getString());
            
            resultSetHistory.updateString("CONTACT_PERSON", getAttribute("CONTACT_PERSON").getString());
            resultSetHistory.updateString("CONT_PERS_DESIGNATION", getAttribute("CONT_PERS_DESIGNATION").getString());
            resultSetHistory.updateString("MOBILE_NO", getAttribute("MOBILE_NO").getString());
            resultSetHistory.updateString("EMAIL", getAttribute("EMAIL").getString());
            
            resultSetHistory.updateString("CONTACT_PERSON_2", getAttribute("CONTACT_PERSON_2").getString());
            resultSetHistory.updateString("CONT_PERS_DESIGNATION_2", getAttribute("CONT_PERS_DESIGNATION_2").getString());
            resultSetHistory.updateString("MOBILE_NO_2", getAttribute("MOBILE_NO_2").getString());
            resultSetHistory.updateString("EMAIL_ID2", getAttribute("EMAIL_ID2").getString());
            
            resultSetHistory.updateString("CONTACT_PERSON_3", getAttribute("CONTACT_PERSON_3").getString());
            resultSetHistory.updateString("CONT_PERS_DESIGNATION_3", getAttribute("CONT_PERS_DESIGNATION_3").getString());
            resultSetHistory.updateString("MOBILE_NO_3", getAttribute("MOBILE_NO_3").getString());
            resultSetHistory.updateString("EMAIL_ID3", getAttribute("EMAIL_ID3").getString());
            
            resultSetHistory.updateString("REMARKS", getAttribute("REMARKS").getString());

            resultSetHistory.updateInt("UPDATED_BY", getAttribute("UPDATED_BY").getInt());
            resultSetHistory.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateTimeDB());
            resultSetHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            resultSetHistory.updateString("APPROVER_REMARKS", getAttribute("FROM_REMARKS").getString());
            resultSetHistory.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            resultSetHistory.updateBoolean("CHANGED", true);
            resultSetHistory.updateString("CHANGED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            
            ResultSet rsTmp=data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");
            
            resultSetHistory.updateString("FROM_IP",""+str_split[1]);
            
            resultSetHistory.insertRow();

            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = 634; //Felt RATE MASTER
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("DOC_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString());
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL";
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
                data.Execute("UPDATE PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL SET REJECTED=0,CHANGED=1, CHANGED_DATE=CURRENT_TIMESTAMP WHERE DOC_NO='" + getAttribute("DOC_NO").getString() + "'");
                //Remove Old Records from FELT DOCUMENT APPROVAL TABLE
                //data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=634 AND DOC_NO='"+getAttribute("DOC_NO").getString()+"'");

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

            // Update Rate Detail and Rate detail history table
            if (ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {
                ObjFeltProductionApprovalFlow.finalApproved = false;
                data.Execute("UPDATE DINESHMILLS.D_SAL_PARTY_MASTER PM, PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL ED "
                        + "SET PM.CORR_ADDRESS = ED.CORR_ADDRESS, PM.PHONE_NO = ED.PHONE_NO, PM.PINCODE = ED.PIN_CODE, PM.ZONE = ED.ZONE, PM.DELIVERY_MODE = ED.DELIVERY_MODE, "
                        + "PM.INCHARGE_CD = ED.INCHARGE_CD, PM.TRANSPORTER_ID = ED.TRANSPORTER_ID, PM.TRANSPORTER_NAME = ED.TRANSPORTER_NAME, "
                        + "PM.CONTACT_PERSON = ED.CONTACT_PERSON, PM.CONT_PERS_DESIGNATION = ED.CONT_PERS_DESIGNATION, PM.MOBILE_NO = ED.MOBILE_NO, PM.EMAIL = ED.EMAIL, "
                        + "PM.CONTACT_PERSON_2 = ED.CONTACT_PERSON_2, PM.CONT_PERS_DESIGNATION_2 = ED.CONT_PERS_DESIGNATION_2, "
                        + "PM.MOBILE_NO_2 = ED.MOBILE_NO_2, PM.EMAIL_ID2 = ED.EMAIL_ID2, "
                        + "PM.CONTACT_PERSON_3 = ED.CONTACT_PERSON_3, PM.CONT_PERS_DESIGNATION_3 = ED.CONT_PERS_DESIGNATION_3, "
                        + "PM.MOBILE_NO_3 = ED.MOBILE_NO_3, PM.EMAIL_ID3 = ED.EMAIL_ID3 "
                        + "WHERE PM.PARTY_CODE = ED.PARTY_CODE AND ED.DOC_NO ='" + getAttribute("DOC_NO").getString() + "' AND ED.APPROVED = 1 ");                
            }

            setData();
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
     * This routine checks and returns whether the item is editable or not.
     * Criteria is Approved item cannot be changed and if not approved then user
     * id is checked whether doucment is waiting for his approval.
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL WHERE DOC_NO='" + docNo + "' AND APPROVED=1";
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=634 AND USER_ID=" + userID + " AND DOC_NO='" + getAttribute("DOC_NO").getString() + "' AND STATUS='W'";
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
            String strSql = "SELECT * FROM PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL WHERE " + stringFindQuery;
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

    public Object getObject(String lDocNo) {
        String strCondition = " DOC_NO='" + lDocNo + "' ";
        clsFeltPartyContact ObjFeltPartyContact = new clsFeltPartyContact();
        ObjFeltPartyContact.Filter(strCondition);
        return ObjFeltPartyContact;
    }

    public static Object getObjectEx(String qualityId) {
        String strCondition = " PARTY_CODE='" + qualityId + "' ";
        clsFeltPartyContact ObjFeltPartyContact = new clsFeltPartyContact();
        ObjFeltPartyContact.LoadData();
        ObjFeltPartyContact.Filter(strCondition);
        return ObjFeltPartyContact;
    }

    public boolean setData() {
        ResultSet rsTmp;
        Connection tmpconnection;
        Statement stTemp;

        tmpconnection = data.getConn();

        //HashMap List=new HashMap();
        long Counter = 0;
        int RevNo = 0;

        try {
            if (HistoryView) {
                RevNo = resultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO", resultSet.getInt("REVISION_NO"));
            } else {
                setAttribute("REVISION_NO", 0);
                setAttribute("CREATED_BY", resultSet.getInt("CREATED_BY"));
                setAttribute("CREATED_DATE", resultSet.getString("CREATED_DATE"));
                setAttribute("MODIFIED_BY", resultSet.getInt("MODIFIED_BY"));
                setAttribute("MODIFIED_DATE", resultSet.getString("MODIFIED_DATE"));
                setAttribute("APPROVED", resultSet.getInt("APPROVED"));
                setAttribute("APPROVED_DATE", resultSet.getString("APPROVED_DATE"));
                setAttribute("REJECTED", resultSet.getBoolean("REJECTED"));
                setAttribute("REJECTED_DATE", resultSet.getString("REJECTED_DATE"));
                setAttribute("CANCELED", resultSet.getInt("CANCELED"));
            }

            setAttribute("DOC_NO", resultSet.getString("DOC_NO"));
            setAttribute("DOC_DATE", resultSet.getString("DOC_DATE"));
            setAttribute("PARTY_CODE", resultSet.getString("PARTY_CODE"));
            setAttribute("PARTY_NAME", resultSet.getString("PARTY_NAME"));
            setAttribute("CORR_ADDRESS", resultSet.getString("CORR_ADDRESS"));
            setAttribute("PHONE_NO", resultSet.getString("PHONE_NO"));
            setAttribute("PIN_CODE", resultSet.getString("PIN_CODE"));
            setAttribute("ZONE", resultSet.getString("ZONE"));
            setAttribute("INCHARGE_CD", resultSet.getString("INCHARGE_CD"));
            setAttribute("INCHARGE_NAME", resultSet.getString("INCHARGE_NAME"));
            setAttribute("TRANSPORTER_ID", resultSet.getString("TRANSPORTER_ID"));
            setAttribute("TRANSPORTER_NAME", resultSet.getString("TRANSPORTER_NAME"));
            setAttribute("DELIVERY_MODE", resultSet.getString("DELIVERY_MODE"));
            
            setAttribute("CONTACT_PERSON", resultSet.getString("CONTACT_PERSON"));
            setAttribute("CONT_PERS_DESIGNATION", resultSet.getString("CONT_PERS_DESIGNATION"));
            setAttribute("MOBILE_NO", resultSet.getString("MOBILE_NO"));
            setAttribute("EMAIL", resultSet.getString("EMAIL"));
                        
            setAttribute("CONTACT_PERSON_2", resultSet.getString("CONTACT_PERSON_2"));
            setAttribute("CONT_PERS_DESIGNATION_2", resultSet.getString("CONT_PERS_DESIGNATION_2"));
            setAttribute("MOBILE_NO_2", resultSet.getString("MOBILE_NO_2"));
            setAttribute("EMAIL_ID2", resultSet.getString("EMAIL_ID2"));
            
            setAttribute("CONTACT_PERSON_3", resultSet.getString("CONTACT_PERSON_3"));
            setAttribute("CONT_PERS_DESIGNATION_3", resultSet.getString("CONT_PERS_DESIGNATION_3"));
            setAttribute("MOBILE_NO_3", resultSet.getString("MOBILE_NO_3"));
            setAttribute("EMAIL_ID3", resultSet.getString("EMAIL_ID3"));

            setAttribute("REMARKS", resultSet.getString("REMARKS"));

            setAttribute("HIERARCHY_ID", resultSet.getInt("HIERARCHY_ID"));

            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    public static HashMap getPendingApprovals(int pUserID, int pOrder) {
        String strSQL = "";

        HashMap List = new HashMap();
        long Counter = 0;

        try {

            if (pOrder == EITLERPGLOBAL.OnRecivedDate) {
                strSQL = "SELECT PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL.DOC_NO,PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL.DOC_DATE,PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL.PARTY_CODE,PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL.PARTY_NAME,RECEIVED_DATE FROM PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL.DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + pUserID + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=634 AND CANCELED=0 ORDER BY PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL.DOC_NO,PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL.DOC_DATE,PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL.PARTY_CODE,PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL.PARTY_NAME,RECEIVED_DATE FROM PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL.DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + pUserID + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=634 AND CANCELED=0 ORDER BY PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL.DOC_NO";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL.DOC_NO,PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL.DOC_DATE,PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL.PARTY_CODE,PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL.PARTY_NAME,RECEIVED_DATE FROM PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL.DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + pUserID + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=634 AND CANCELED=0 ORDER BY PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL.DOC_NO";
            }

            ResultSet rsTmp = data.getConn().createStatement().executeQuery(strSQL);

            Counter = 0;
            while (rsTmp.next()) {

                Counter = Counter + 1;
                clsFeltPartyContact ObjDoc = new clsFeltPartyContact();

                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO", rsTmp.getString("DOC_NO"));
                ObjDoc.setAttribute("DOC_DATE", rsTmp.getString("DOC_DATE"));
                ObjDoc.setAttribute("PARTY_CODE", rsTmp.getString("PARTY_CODE"));
                ObjDoc.setAttribute("PARTY_NAME", rsTmp.getString("PARTY_NAME"));
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

    public boolean ShowHistory(String docNo) {
        Ready = false;
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL_H WHERE DOC_NO='" + docNo + "'");
            Ready = true;
            HistoryView = true;
            MoveFirst();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public static HashMap getHistoryList(int CompanyID, String docNo) {
        HashMap hmHistoryList = new HashMap();
        Statement stTmp;
        ResultSet rsTmp;
        try {
            stTmp = data.getConn().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp = stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL_H WHERE DOC_NO='" + docNo + "' ORDER BY REVISION_NO");

            while (rsTmp.next()) {
                clsFeltPartyContact ObjFeltPartyContact = new clsFeltPartyContact();

                ObjFeltPartyContact.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                ObjFeltPartyContact.setAttribute("UPDATED_BY", rsTmp.getInt("UPDATED_BY"));
                ObjFeltPartyContact.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                ObjFeltPartyContact.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                ObjFeltPartyContact.setAttribute("APPROVER_REMARKS", rsTmp.getString("APPROVER_REMARKS"));
                ObjFeltPartyContact.setAttribute("FROM_IP", rsTmp.getString("FROM_IP"));

                hmHistoryList.put(Integer.toString(hmHistoryList.size() + 1), ObjFeltPartyContact);
            }
            rsTmp.close();
            stTmp.close();
            return hmHistoryList;
        } catch (Exception e) {
            e.printStackTrace();
            return hmHistoryList;
        }
    }

    public static String getDocStatus(String docNo) {
        ResultSet rsTmp;
        String strMessage = "";
        try {
            //First check that Document exist
            rsTmp = data.getResult("SELECT DOC_NO,PARTY_CODE,APPROVED,CANCELED FROM PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL WHERE DOC_NO='" + docNo + "'");
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
            e.printStackTrace();
        }

        return strMessage;
    }

    public static boolean CanCancel(int pCompanyID, String docNo) {
        ResultSet rsTmp = null;
        boolean canCancel = false;

        try {
            rsTmp = data.getResult("SELECT DOC_NO,PARTY_CODE FROM PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL WHERE DOC_NO='" + docNo + "' AND APPROVED=0 AND CANCELED=0  ");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                canCancel = true;
            }
            rsTmp.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return canCancel;
    }

    public static boolean CancelDoc(int pCompanyID, String docNo) {
        ResultSet rsTmp = null;
        boolean Cancelled = false;
        try {
            if (CanCancel(pCompanyID, docNo)) {
                boolean ApprovedDoc = false;
                rsTmp = data.getResult("SELECT APPROVED FROM PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL WHERE DOC_NO='" + docNo + "' ");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    ApprovedDoc = rsTmp.getBoolean("APPROVED");
                }

                if (ApprovedDoc) {

                } else {
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='" + docNo + "' AND MODULE_ID=" + 634);
                }

                //Now Update the header with cancel falg to true
                data.Execute("UPDATE PRODUCTION.FELT_PARTY_EXTRA_CONTACT_DETAIL SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURRENT_TIMESTAMP WHERE DOC_NO='" + docNo + "' ");

                Cancelled = true;
            }

            rsTmp.close();
        } catch (Exception e) {

        }
        return Cancelled;
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
    
    public boolean checkPartyCode(String partyCode){
        if(data.getIntValueFromDB("SELECT COUNT(PARTY_CODE) FROM D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE='"+partyCode+"'")==1) return true;
        else return false;
    }
    
    // it creates list of Felt Incharge
    public HashMap getInchargeNameList(){
        HashMap hmInchargeNameList= new HashMap();
        try{
            Connection Conn=data.getConn();
            Statement stTmp=Conn.createStatement();
            int counter=1;
            ResultSet rsTmp=stTmp.executeQuery("SELECT INCHARGE_CD, INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE");
            while(rsTmp.next()){
                ComboData aData=new ComboData();
                aData.Code=rsTmp.getInt("INCHARGE_CD");
                aData.Text=rsTmp.getString("INCHARGE_NAME");
                hmInchargeNameList.put(new Integer(counter++), aData);
            }
        }catch(SQLException e){
            LastError = e.getMessage();
            e.printStackTrace();
        }
        return hmInchargeNameList;
    }
}
