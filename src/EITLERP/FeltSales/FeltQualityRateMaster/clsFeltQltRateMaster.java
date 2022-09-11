/*
 * clsFeltRateMaster.java
 *
 * Created on September 3, 2013, 5:10 PM
 */
package EITLERP.FeltSales.FeltQualityRateMaster;

import EITLERP.Production.FeltRateMaster.*;
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
public class clsFeltQltRateMaster {

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
    public clsFeltQltRateMaster() {
        LastError = "";
        props = new HashMap();

        props.put("DOC_NO", new Variant(0));
        props.put("DOC_DATE", new Variant(""));
        props.put("WH_CD", new Variant(""));
        props.put("PRODUCT_CODE", new Variant(""));
        props.put("PRODUCT_DESC", new Variant(""));
        props.put("SYN_PER", new Variant(0));
        props.put("SQM_RATE", new Variant(0));
        props.put("WT_RATE", new Variant(0));
        props.put("EFFECTIVE_FROM", new Variant(""));
        props.put("EFFECTIVE_TO", new Variant(""));
        props.put("CHEM_TRT_IND", new Variant(""));
        props.put("CHEM_TRT_CHRG", new Variant(0));
        props.put("PIN_IND", new Variant(""));
        props.put("PIN_CHRG", new Variant(0));
        props.put("SPR_IND", new Variant(""));
        props.put("SPR_CHRG", new Variant(0));
        props.put("SUR_IND", new Variant(""));
        props.put("SUR_CHRG", new Variant(0));
        props.put("SQM_IND", new Variant(""));
        props.put("SQM_CHRG", new Variant(0));
        props.put("EXC_CAT_IND", new Variant(""));
        props.put("EXC_CAT_CHRG", new Variant(0));
        props.put("FILLE", new Variant(""));
        props.put("CHARGES", new Variant(0));
        props.put("GROUP_NAME", new Variant(""));
        props.put("DIVERSION_GROUP", new Variant(""));
        props.put("CATEGORY", new Variant(""));
        props.put("FABRIC_CATG", new Variant(""));
        props.put("POSITION_CATG", new Variant(""));
        props.put("FELT_CATG", new Variant(""));
        props.put("ACTIVE_FLAG", new Variant(0));
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
        
        props.put("OLD_RATE", new Variant(0));
        props.put("SURCHARGE_1_PER", new Variant(0));
        props.put("SURCHARGE_2_PER", new Variant(0));
        props.put("SURCHARGE_LENGTH_CRITERIA", new Variant(0));
        props.put("WEIGHT_RATE_CRITERIA", new Variant(0));

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
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_QLT_RATE_MASTER ORDER BY DOC_NO");
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
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_QLT_RATE_MASTER_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval

            setAttribute("DOC_NO", getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 601, 199, true));

            resultSet.moveToInsertRow();
            resultSet.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSet.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            resultSet.updateString("WH_CD", getAttribute("WH_CD").getString());
            resultSet.updateString("PRODUCT_CODE", getAttribute("PRODUCT_CODE").getString());
            resultSet.updateString("PRODUCT_DESC", getAttribute("PRODUCT_DESC").getString());
            resultSet.updateInt("SYN_PER", (int) getAttribute("SYN_PER").getVal());
            resultSet.updateFloat("WT_RATE", (float) getAttribute("WT_RATE").getVal());
            resultSet.updateString("EFFECTIVE_FROM", EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_FROM").getString()));
            if (getAttribute("EFFECTIVE_TO").getString().trim().equalsIgnoreCase("")) {
                resultSet.updateString("EFFECTIVE_TO", "0000-00-00");
            } else {
                resultSet.updateString("EFFECTIVE_TO", EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_TO").getString()));
            }

            resultSet.updateInt("CHEM_TRT_IND", getAttribute("CHEM_TRT_IND").getInt());
            resultSet.updateInt("PIN_IND", getAttribute("PIN_IND").getInt());
            resultSet.updateInt("SPR_IND", getAttribute("SPR_IND").getInt());
            resultSet.updateInt("SUR_IND", getAttribute("SUR_IND").getInt());
            resultSet.updateInt("SQM_IND", getAttribute("SQM_IND").getInt());
            resultSet.updateInt("EXC_CAT_IND", getAttribute("EXC_CAT_IND").getInt());

            resultSet.updateFloat("CHEM_TRT_CHRG", (float) getAttribute("CHEM_TRT_CHRG").getVal());
            resultSet.updateFloat("PIN_CHRG", (float) getAttribute("PIN_CHRG").getVal());
            resultSet.updateFloat("SPR_CHRG", (float) getAttribute("SPR_CHRG").getVal());
            resultSet.updateFloat("SUR_CHRG", (float) getAttribute("SUR_CHRG").getVal());
            resultSet.updateFloat("SQM_CHRG", (float) getAttribute("SQM_CHRG").getVal());
            resultSet.updateFloat("EXC_CAT_CHRG", (float) getAttribute("EXC_CAT_CHRG").getVal());
            
            resultSet.updateFloat("OLD_RATE", (float) getAttribute("OLD_RATE").getVal());
            resultSet.updateFloat("WEIGHT_RATE_CRITERIA", (float) getAttribute("WEIGHT_RATE_CRITERIA").getVal());
            resultSet.updateFloat("SURCHARGE_LENGTH_CRITERIA", (float) getAttribute("SURCHARGE_LENGTH_CRITERIA").getVal());
            resultSet.updateFloat("SURCHARGE_1_PER", (float) getAttribute("SURCHARGE_1_PER").getVal());
            resultSet.updateFloat("SURCHARGE_2_PER", (float) getAttribute("SURCHARGE_2_PER").getVal());

            resultSet.updateString("GROUP_NAME", getAttribute("GROUP_NAME").getString());
            resultSet.updateString("DIVERSION_GROUP", getAttribute("DIVERSION_GROUP").getString());
            resultSet.updateString("REMARKS", getAttribute("REMARKS").getString());

            resultSet.updateInt("CATEGORY", getAttribute("CATEGORY").getInt());
            resultSet.updateInt("FABRIC_CATG", getAttribute("FABRIC_CATG").getInt());
            resultSet.updateInt("POSITION_CATG", getAttribute("POSITION_CATG").getInt());

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

//            
//            resultSet.updateInt("CHEM_TRT_IND", getAttribute("CHEM_TRT_IND").getInt());
//            resultSet.updateInt("PIN_IND",  getAttribute("PIN_IND").getInt());
//            resultSet.updateInt("SPRL_IND", getAttribute("SPRL_IND").getInt());
//            resultSet.updateInt("SUR_IND", getAttribute("SUR_IND").getInt());
//            resultSet.updateInt("SQM_IND", getAttribute("SQM_IND").getInt());
//            resultSet.updateFloat("CHARGES", (float)getAttribute("CHARGES").getVal());
//            resultSet.updateString("GRUP", getAttribute("GROUP").getString());
//            resultSet.updateString("REMARKS", getAttribute("REMARKS").getString());
//            resultSet.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
//            resultSet.updateString("CREATED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
//            resultSet.updateInt("MODIFIED_BY",0);
//            resultSet.updateString("MODIFIED_DATE","");
//            resultSet.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
//            resultSet.updateBoolean("APPROVED",false);
//            resultSet.updateString("APPROVED_DATE","");
//            resultSet.updateBoolean("REJECTED",false);
//            resultSet.updateString("REJECTED_DATE","");
//            resultSet.updateBoolean("CANCELED",false);
//            resultSet.updateBoolean("CHANGED",true);
//            resultSet.updateString("CHANGED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
//            resultSet.insertRow();
            //========= Inserting Into History =================//
            resultSetHistory.moveToInsertRow();
            resultSetHistory.updateInt("REVISION_NO", 1); //Revision No. 1 in case of Insert

            resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSetHistory.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            resultSetHistory.updateString("WH_CD", getAttribute("WH_CD").getString());
            resultSetHistory.updateString("PRODUCT_CODE", getAttribute("PRODUCT_CODE").getString());
            resultSetHistory.updateString("PRODUCT_DESC", getAttribute("PRODUCT_DESC").getString());
            resultSetHistory.updateInt("SYN_PER", (int) getAttribute("SYN_PER").getVal());
            resultSetHistory.updateFloat("WT_RATE", (float) getAttribute("WT_RATE").getVal());
            resultSetHistory.updateString("EFFECTIVE_FROM", EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_FROM").getString()));
            if (getAttribute("EFFECTIVE_TO").getString().trim().equalsIgnoreCase("")) {
                resultSetHistory.updateString("EFFECTIVE_TO", "0000-00-00");
            } else {
                resultSetHistory.updateString("EFFECTIVE_TO", EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_TO").getString()));
            }
            //resultSetHistory.updateString("EFFECTIVE_TO", EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_TO").getString()));

            resultSetHistory.updateInt("CHEM_TRT_IND", getAttribute("CHEM_TRT_IND").getInt());
            resultSetHistory.updateInt("PIN_IND", getAttribute("PIN_IND").getInt());
            resultSetHistory.updateInt("SPR_IND", getAttribute("SPR_IND").getInt());
            resultSetHistory.updateInt("SUR_IND", getAttribute("SUR_IND").getInt());
            resultSetHistory.updateInt("SQM_IND", getAttribute("SQM_IND").getInt());
            resultSetHistory.updateInt("EXC_CAT_IND", getAttribute("EXC_CAT_IND").getInt());

            resultSetHistory.updateFloat("CHEM_TRT_CHRG", (float) getAttribute("CHEM_TRT_CHRG").getVal());
            resultSetHistory.updateFloat("PIN_CHRG", (float) getAttribute("PIN_CHRG").getVal());
            resultSetHistory.updateFloat("SPR_CHRG", (float) getAttribute("SPR_CHRG").getVal());
            resultSetHistory.updateFloat("SUR_CHRG", (float) getAttribute("SUR_CHRG").getVal());
            resultSetHistory.updateFloat("SQM_CHRG", (float) getAttribute("SQM_CHRG").getVal());
            resultSetHistory.updateFloat("EXC_CAT_CHRG", (float) getAttribute("EXC_CAT_CHRG").getVal());

            resultSetHistory.updateFloat("OLD_RATE", (float) getAttribute("OLD_RATE").getVal());
            resultSetHistory.updateFloat("WEIGHT_RATE_CRITERIA", (float) getAttribute("WEIGHT_RATE_CRITERIA").getVal());
            resultSetHistory.updateFloat("SURCHARGE_LENGTH_CRITERIA", (float) getAttribute("SURCHARGE_LENGTH_CRITERIA").getVal());
            resultSetHistory.updateFloat("SURCHARGE_1_PER", (float) getAttribute("SURCHARGE_1_PER").getVal());
            resultSetHistory.updateFloat("SURCHARGE_2_PER", (float) getAttribute("SURCHARGE_2_PER").getVal());
            
            
            resultSetHistory.updateString("GROUP_NAME", getAttribute("GROUP_NAME").getString());
            resultSetHistory.updateString("DIVERSION_GROUP", getAttribute("DIVERSION_GROUP").getString());
            resultSetHistory.updateString("REMARKS", getAttribute("REMARKS").getString());

            resultSetHistory.updateInt("CATEGORY", getAttribute("CATEGORY").getInt());
            resultSetHistory.updateInt("FABRIC_CATG", getAttribute("FABRIC_CATG").getInt());
            resultSetHistory.updateInt("POSITION_CATG", getAttribute("POSITION_CATG").getInt());

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

//            resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
//            resultSetHistory.updateString("PRODUCT_CODE", getAttribute("PRODUCT_CODE").getString());
//            resultSetHistory.updateString("PRODUCT_DESC", getAttribute("PRODUCT_DESC").getString());
//            resultSetHistory.updateFloat("SYN_PER", (float)getAttribute("SYN_PER").getVal());
//            resultSetHistory.updateFloat("SQM_RATE", (float)getAttribute("SQM_RATE").getVal());
//            resultSetHistory.updateFloat("WT_RATE", (float)getAttribute("WT_RATE").getVal());
//            resultSetHistory.updateInt("CHEM_TRT_IND", getAttribute("CHEM_TRT_IND").getInt());
//            resultSetHistory.updateInt("PIN_IND",  getAttribute("PIN_IND").getInt());
//            resultSetHistory.updateInt("SPRL_IND", getAttribute("SPRL_IND").getInt());
//            resultSetHistory.updateInt("SUR_IND", getAttribute("SUR_IND").getInt());
//            resultSetHistory.updateInt("SQM_IND", getAttribute("SQM_IND").getInt());
//            resultSetHistory.updateFloat("CHARGES", (float)getAttribute("CHARGES").getVal());
//            resultSetHistory.updateString("GRUP", getAttribute("GROUP").getString());
//            resultSetHistory.updateString("REMARKS", getAttribute("REMARKS").getString());
//            resultSetHistory.updateInt("UPDATED_BY", getAttribute("UPDATED_BY").getInt());
//            resultSetHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
//            resultSetHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
//            resultSetHistory.updateString("APPROVER_REMARKS", getAttribute("FROM_REMARKS").getString());            
//            resultSetHistory.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
//            resultSetHistory.updateBoolean("CHANGED",true);
//            resultSetHistory.updateString("CHANGED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
//            resultSetHistory.insertRow();            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = 601; //Felt RATE MASTER
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("DOC_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.FELT_QLT_RATE_MASTER";
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
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_QLT_RATE_MASTER_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval

            resultSet.updateString("PRODUCT_DESC", getAttribute("PRODUCT_DESC").getString());
            resultSet.updateInt("SYN_PER", (int) getAttribute("SYN_PER").getVal());
            resultSet.updateFloat("WT_RATE", (float) getAttribute("WT_RATE").getVal());
            resultSet.updateString("EFFECTIVE_FROM", EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_FROM").getString()));
            if (getAttribute("EFFECTIVE_TO").getString().trim().equalsIgnoreCase("")) {
                resultSet.updateString("EFFECTIVE_TO", "0000-00-00");
            } else {
                resultSet.updateString("EFFECTIVE_TO", EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_TO").getString()));
            }
            //resultSet.updateString("EFFECTIVE_TO", EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_TO").getString()));

            resultSet.updateInt("CHEM_TRT_IND", getAttribute("CHEM_TRT_IND").getInt());
            resultSet.updateInt("PIN_IND", getAttribute("PIN_IND").getInt());
            resultSet.updateInt("SPR_IND", getAttribute("SPR_IND").getInt());
            resultSet.updateInt("SUR_IND", getAttribute("SUR_IND").getInt());
            resultSet.updateInt("SQM_IND", getAttribute("SQM_IND").getInt());
            resultSet.updateInt("EXC_CAT_IND", getAttribute("EXC_CAT_IND").getInt());

            resultSet.updateFloat("CHEM_TRT_CHRG", (float) getAttribute("CHEM_TRT_CHRG").getVal());
            resultSet.updateFloat("PIN_CHRG", (float) getAttribute("PIN_CHRG").getVal());
            resultSet.updateFloat("SPR_CHRG", (float) getAttribute("SPR_CHRG").getVal());
            resultSet.updateFloat("SUR_CHRG", (float) getAttribute("SUR_CHRG").getVal());
            resultSet.updateFloat("SQM_CHRG", (float) getAttribute("SQM_CHRG").getVal());
            resultSet.updateFloat("EXC_CAT_CHRG", (float) getAttribute("EXC_CAT_CHRG").getVal());

            resultSet.updateFloat("OLD_RATE", (float) getAttribute("OLD_RATE").getVal());
            resultSet.updateFloat("WEIGHT_RATE_CRITERIA", (float) getAttribute("WEIGHT_RATE_CRITERIA").getVal());
            resultSet.updateFloat("SURCHARGE_LENGTH_CRITERIA", (float) getAttribute("SURCHARGE_LENGTH_CRITERIA").getVal());
            resultSet.updateFloat("SURCHARGE_1_PER", (float) getAttribute("SURCHARGE_1_PER").getVal());
            resultSet.updateFloat("SURCHARGE_2_PER", (float) getAttribute("SURCHARGE_2_PER").getVal());
            
            resultSet.updateString("GROUP_NAME", getAttribute("GROUP_NAME").getString());
            resultSet.updateString("DIVERSION_GROUP", getAttribute("DIVERSION_GROUP").getString());
            resultSet.updateString("REMARKS", getAttribute("REMARKS").getString());

            resultSet.updateInt("CATEGORY", getAttribute("CATEGORY").getInt());
            resultSet.updateInt("FABRIC_CATG", getAttribute("FABRIC_CATG").getInt());
            resultSet.updateInt("POSITION_CATG", getAttribute("POSITION_CATG").getInt());

            resultSet.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
            resultSet.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
            resultSet.updateInt("MODIFIED_BY", getAttribute("MODIFIED_BY").getInt());
            resultSet.updateString("MODIFIED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSet.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            resultSet.updateBoolean("CHANGED", true);
            resultSet.updateString("CHANGED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSet.updateBoolean("CANCELED", false);
            resultSet.updateRow();

//            
//            resultSet.updateFloat("SYN_PER", (float)getAttribute("SYN_PER").getVal());
//            resultSet.updateFloat("SQM_RATE", (float)getAttribute("SQM_RATE").getVal());
//            resultSet.updateFloat("WT_RATE", (float)getAttribute("WT_RATE").getVal());
//            resultSet.updateInt("CHEM_TRT_IND", getAttribute("CHEM_TRT_IND").getInt());
//            resultSet.updateInt("PIN_IND",  getAttribute("PIN_IND").getInt());
//            resultSet.updateInt("SPRL_IND", getAttribute("SPRL_IND").getInt());
//            resultSet.updateInt("SUR_IND", getAttribute("SUR_IND").getInt());
//            resultSet.updateInt("SQM_IND", getAttribute("SQM_IND").getInt());
//            resultSet.updateFloat("CHARGES", (float)getAttribute("CHARGES").getVal());
//            resultSet.updateString("GRUP", getAttribute("GROUP").getString());
//            resultSet.updateString("REMARKS", getAttribute("REMARKS").getString());
//            resultSet.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
//            resultSet.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
//            resultSet.updateInt("MODIFIED_BY", getAttribute("MODIFIED_BY").getInt());
//            resultSet.updateString("MODIFIED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
//            resultSet.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
//            resultSet.updateBoolean("CHANGED",true);
//            resultSet.updateString("CHANGED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
//            resultSet.updateBoolean("CANCELED",false);
//            resultSet.updateRow();
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo = data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_QLT_RATE_MASTER_H WHERE DOC_NO='" + getAttribute("DOC_NO").getString() + "'");
            RevNo++;

            resultSetHistory.moveToInsertRow();
            resultSetHistory.updateInt("REVISION_NO", RevNo);

            resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSetHistory.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            resultSetHistory.updateString("WH_CD", getAttribute("WH_CD").getString());
            resultSetHistory.updateString("PRODUCT_CODE", getAttribute("PRODUCT_CODE").getString());
            resultSetHistory.updateString("PRODUCT_DESC", getAttribute("PRODUCT_DESC").getString());
            resultSetHistory.updateInt("SYN_PER", (int) getAttribute("SYN_PER").getVal());
            resultSetHistory.updateFloat("WT_RATE", (float) getAttribute("WT_RATE").getVal());
            resultSetHistory.updateString("EFFECTIVE_FROM", EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_FROM").getString()));
            if (getAttribute("EFFECTIVE_TO").getString().trim().equalsIgnoreCase("")) {
                resultSetHistory.updateString("EFFECTIVE_TO", "0000-00-00");
            } else {
                resultSetHistory.updateString("EFFECTIVE_TO", EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_TO").getString()));
            }
            //resultSetHistory.updateString("EFFECTIVE_TO", EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_TO").getString()));

            resultSetHistory.updateInt("CHEM_TRT_IND", getAttribute("CHEM_TRT_IND").getInt());
            resultSetHistory.updateInt("PIN_IND", getAttribute("PIN_IND").getInt());
            resultSetHistory.updateInt("SPR_IND", getAttribute("SPR_IND").getInt());
            resultSetHistory.updateInt("SUR_IND", getAttribute("SUR_IND").getInt());
            resultSetHistory.updateInt("SQM_IND", getAttribute("SQM_IND").getInt());
            resultSetHistory.updateInt("EXC_CAT_IND", getAttribute("EXC_CAT_IND").getInt());

            resultSetHistory.updateFloat("CHEM_TRT_CHRG", (float) getAttribute("CHEM_TRT_CHRG").getVal());
            resultSetHistory.updateFloat("PIN_CHRG", (float) getAttribute("PIN_CHRG").getVal());
            resultSetHistory.updateFloat("SPR_CHRG", (float) getAttribute("SPR_CHRG").getVal());
            resultSetHistory.updateFloat("SUR_CHRG", (float) getAttribute("SUR_CHRG").getVal());
            resultSetHistory.updateFloat("SQM_CHRG", (float) getAttribute("SQM_CHRG").getVal());
            resultSetHistory.updateFloat("EXC_CAT_CHRG", (float) getAttribute("EXC_CAT_CHRG").getVal());

            resultSetHistory.updateFloat("OLD_RATE", (float) getAttribute("OLD_RATE").getVal());
            resultSetHistory.updateFloat("WEIGHT_RATE_CRITERIA", (float) getAttribute("WEIGHT_RATE_CRITERIA").getVal());
            resultSetHistory.updateFloat("SURCHARGE_LENGTH_CRITERIA", (float) getAttribute("SURCHARGE_LENGTH_CRITERIA").getVal());
            resultSetHistory.updateFloat("SURCHARGE_1_PER", (float) getAttribute("SURCHARGE_1_PER").getVal());
            resultSetHistory.updateFloat("SURCHARGE_2_PER", (float) getAttribute("SURCHARGE_2_PER").getVal());
            
            resultSetHistory.updateString("GROUP_NAME", getAttribute("GROUP_NAME").getString());
            resultSetHistory.updateString("DIVERSION_GROUP", getAttribute("DIVERSION_GROUP").getString());
            resultSetHistory.updateString("REMARKS", getAttribute("REMARKS").getString());

            resultSetHistory.updateInt("CATEGORY", getAttribute("CATEGORY").getInt());
            resultSetHistory.updateInt("FABRIC_CATG", getAttribute("FABRIC_CATG").getInt());
            resultSetHistory.updateInt("POSITION_CATG", getAttribute("POSITION_CATG").getInt());

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

//            
//            resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
//            resultSetHistory.updateString("PRODUCT_CODE", getAttribute("PRODUCT_CODE").getString());
//            resultSetHistory.updateString("PRODUCT_DESC", getAttribute("PRODUCT_DESC").getString());
//            resultSetHistory.updateFloat("SYN_PER", (float)getAttribute("SYN_PER").getVal());
//            resultSetHistory.updateFloat("SQM_RATE", (float)getAttribute("SQM_RATE").getVal());
//            resultSetHistory.updateFloat("WT_RATE", (float)getAttribute("WT_RATE").getVal());
//            resultSetHistory.updateInt("CHEM_TRT_IND", getAttribute("CHEM_TRT_IND").getInt());
//            resultSetHistory.updateInt("PIN_IND",  getAttribute("PIN_IND").getInt());
//            resultSetHistory.updateInt("SPRL_IND", getAttribute("SPRL_IND").getInt());
//            resultSetHistory.updateInt("SUR_IND", getAttribute("SUR_IND").getInt());
//            resultSetHistory.updateInt("SQM_IND", getAttribute("SQM_IND").getInt());
//            resultSetHistory.updateFloat("CHARGES", (float)getAttribute("CHARGES").getVal());
//            resultSetHistory.updateString("GRUP", getAttribute("GROUP").getString());
//            resultSetHistory.updateString("REMARKS", getAttribute("REMARKS").getString());
//            resultSetHistory.updateInt("UPDATED_BY", getAttribute("UPDATED_BY").getInt());
//            resultSetHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
//            resultSetHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
//            resultSetHistory.updateString("APPROVER_REMARKS", getAttribute("FROM_REMARKS").getString());            
//            resultSetHistory.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
//            resultSetHistory.updateBoolean("CHANGED",true);
//            resultSetHistory.updateString("CHANGED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
//            resultSetHistory.insertRow();
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = 601; //Felt RATE MASTER
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("DOC_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.FELT_QLT_RATE_MASTER";
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
                data.Execute("UPDATE PRODUCTION.FELT_QLT_RATE_MASTER SET REJECTED=0,CHANGED=1, CHANGED_DATE=CURRENT_TIMESTAMP WHERE DOC_NO='" + getAttribute("DOC_NO").getString() + "'");
                //Remove Old Records from FELT DOCUMENT APPROVAL TABLE
                //data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=601 AND DOC_NO='"+getAttribute("DOC_NO").getString()+"'");

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
                //UpdateRateDetail();
                data.Execute("UPDATE PRODUCTION.FELT_QLT_RATE_MASTER SET EFFECTIVE_TO=SUBDATE('" + EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_FROM").getString()) + "',1) WHERE WH_CD='" + getAttribute("WH_CD").getString() + "' AND PRODUCT_CODE='" + getAttribute("PRODUCT_CODE").getString() + "' AND DOC_NO NOT IN ('" + getAttribute("DOC_NO").getString() + "') AND (EFFECTIVE_TO='0000-00-00' OR EFFECTIVE_TO>='" + EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_FROM").getString()) + "') AND APPROVED=1 ");
                System.out.println("UPDATE PRODUCTION.FELT_QLT_RATE_MASTER SET EFFECTIVE_TO=SUBDATE('" + EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_FROM").getString()) + "',1) WHERE WH_CD='" + getAttribute("WH_CD").getString() + "' AND PRODUCT_CODE='" + getAttribute("PRODUCT_CODE").getString() + "' AND DOC_NO NOT IN ('" + getAttribute("DOC_NO").getString() + "') AND (EFFECTIVE_TO='0000-00-00' OR EFFECTIVE_TO>='" + EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_FROM").getString()) + "') AND APPROVED=1");
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

    //Inserts in  Rate details and detail history table
//    public void UpdateRateDetail() {
//        Statement stDetail, stDetailHistory;
//        ResultSet rsDetail, rsDetailHistory;
//        
//        try {
//            // ---- Rate Detail Connection ------ //
//            stDetail=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
//            rsDetail=stDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_RATE_DETAIL WHERE PRODUCT_CODE='1'"); // '1' for restricting all data retrieval
//            
//            // ---- Rate Detail History Connection ------ //
//            stDetailHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
//            rsDetailHistory=stDetailHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_RATE_DETAIL_H WHERE PRODUCT_CODE='1'"); // '1' for restricting all data retrieval
//            
//            //Get the Maximum Revision No.
//            int srNo=data.getIntValueFromDB("SELECT MAX(SR_NO) FROM PRODUCTION.FELT_RATE_DETAIL WHERE PRODUCT_CODE='"+getAttribute("PRODUCT_CODE").getString()+"'");            
//            srNo++;
//            
//            rsDetail.moveToInsertRow();
//            rsDetail.updateInt("SR_NO",srNo);
//            rsDetail.updateString("DOC_NO", getAttribute("DOC_NO").getString());
//            rsDetail.updateString("PRODUCT_CODE", getAttribute("PRODUCT_CODE").getString());
//            rsDetail.updateFloat("SQM_RATE", (float)getAttribute("SQM_RATE").getVal());
//            rsDetail.updateFloat("WT_RATE", (float)getAttribute("WT_RATE").getVal());
//            rsDetail.updateString("RATE_FRMDT", EITLERPGLOBAL.getCurrentDateDB());
//            rsDetail.updateString("RATE_TODT", "");
//            rsDetail.updateFloat("CHARGES", (float)getAttribute("CHARGES").getVal());
//            rsDetail.updateString("CHARGES_FRMDT", EITLERPGLOBAL.getCurrentDateDB());
//            rsDetail.updateString("CHARGES_TODT", "");
//            rsDetail.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
//            rsDetail.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
//            rsDetail.updateInt("MODIFIED_BY", getAttribute("MODIFIED_BY").getInt());
//            rsDetail.updateString("MODIFIED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));            
//            rsDetail.updateBoolean("CHANGED",true);
//            rsDetail.updateString("CHANGED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));            
//            rsDetail.insertRow();
//            
//            //========= Inserting Into History =================//
//            //Get the Maximum Revision No.
//            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_RATE_DETAIL_H WHERE PRODUCT_CODE='"+getAttribute("PRODUCT_CODE").getString()+"'");
//            RevNo++;
//             
//            rsDetailHistory.moveToInsertRow();
//            rsDetailHistory.updateInt("REVISION_NO",RevNo);
//            rsDetailHistory.updateInt("SR_NO",srNo);
//            rsDetailHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
//            rsDetailHistory.updateString("PRODUCT_CODE", getAttribute("PRODUCT_CODE").getString());
//            rsDetailHistory.updateFloat("SQM_RATE", (float)getAttribute("SQM_RATE").getVal());
//            rsDetailHistory.updateFloat("WT_RATE", (float)getAttribute("WT_RATE").getVal());
//            rsDetailHistory.updateString("RATE_FRMDT", EITLERPGLOBAL.getCurrentDateDB());
//            rsDetailHistory.updateString("RATE_TODT", "");
//            rsDetailHistory.updateFloat("CHARGES", (float)getAttribute("CHARGES").getVal());
//            rsDetailHistory.updateString("CHARGES_FRMDT", EITLERPGLOBAL.getCurrentDateDB());
//            rsDetailHistory.updateString("CHARGES_TODT", "");
//            rsDetailHistory.updateInt("UPDATED_BY", getAttribute("UPDATED_BY").getInt());
//            rsDetailHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
//            rsDetailHistory.updateBoolean("CHANGED",true);
//            rsDetailHistory.updateString("CHANGED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
//            rsDetailHistory.insertRow();
//        }catch(Exception e) {
//            LastError = e.getMessage();
//            e.printStackTrace();            
//        }
//    }
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE DOC_NO='" + docNo + "' AND APPROVED=1";
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=601 AND USER_ID=" + userID + " AND DOC_NO='" + getAttribute("DOC_NO").getString() + "' AND STATUS='W'";
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
            String strSql = "SELECT * FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE " + stringFindQuery;
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
        String strCondition = "DOC_NO='" + lDocNo + "'";
        clsFeltQltRateMaster ObjFeltRateMaster = new clsFeltQltRateMaster();
        ObjFeltRateMaster.Filter(strCondition);
        return ObjFeltRateMaster;
    }

    public static Object getObjectEx(String qualityId) {
        String strCondition = "PRODUCT_CODE='" + qualityId + "'";
        clsFeltQltRateMaster ObjFeltRateMaster = new clsFeltQltRateMaster();
        ObjFeltRateMaster.LoadData();
        ObjFeltRateMaster.Filter(strCondition);
        return ObjFeltRateMaster;
    }

    public static HashMap getList(String pCondition) {
        ResultSet rsTmp = null;
        Connection tmpconnection = null;
        Statement stTemp = null;

        ResultSet rsTmp2 = null;
        Statement stTemp2 = null;

        HashMap List = new HashMap();
        long Counter = 0;

        try {
            tmpconnection = data.getConn();
            stTemp = tmpconnection.createStatement();

            rsTmp = stTemp.executeQuery("SELECT * FROM PRODUCTION.FELT_QLT_RATE_MASTER " + pCondition);

            Counter = 0;
            while (rsTmp.next()) {
                Counter = Counter + 1;
                clsFeltQltRateMaster ObjRate = new clsFeltQltRateMaster();
                ObjRate.setAttribute("DOC_NO", rsTmp.getString("DOC_NO"));
                ObjRate.setAttribute("PRODUCT_CODE", rsTmp.getString("PRODUCT_CODE"));
                ObjRate.setAttribute("PRODUCT_DESC", rsTmp.getString("PRODUCT_DESC"));
                ObjRate.setAttribute("SYN_PER", rsTmp.getDouble("SYN_PER"));
                ObjRate.setAttribute("CHEM_TRT_IND", rsTmp.getInt("CHEM_TRT_IND"));
                ObjRate.setAttribute("PIN_IND", rsTmp.getInt("PIN_IND"));
                ObjRate.setAttribute("SPRL_IND", rsTmp.getInt("SPRL_IND"));
                ObjRate.setAttribute("SUR_IND", rsTmp.getInt("SUR_IND"));
                ObjRate.setAttribute("SQM_IND", rsTmp.getInt("SQM_IND"));
                ObjRate.setAttribute("REMARKS", rsTmp.getInt("REMARKS"));

                ObjRate.setAttribute("APPROVED", rsTmp.getInt("APPROVED"));
                ObjRate.setAttribute("APPROVED_DATE", rsTmp.getString("APPROVED_DATE"));
                ObjRate.setAttribute("REJECTED_DATE", rsTmp.getString("REJECTED_DATE"));
                ObjRate.setAttribute("REJECTED", rsTmp.getBoolean("REJECTED"));
                ObjRate.setAttribute("REJECTED_REMARKS", rsTmp.getString("REJECTED_REMARKS"));
                ObjRate.setAttribute("CANCELLED", rsTmp.getInt("CANCELLED"));
                ObjRate.setAttribute("CREATED_BY", rsTmp.getInt("CREATED_BY"));
                ObjRate.setAttribute("CREATED_DATE", rsTmp.getString("CREATED_DATE"));
                ObjRate.setAttribute("MODIFIED_BY", rsTmp.getInt("MODIFIED_BY"));
                ObjRate.setAttribute("MODIFIED_DATE", rsTmp.getString("MODIFIED_DATE"));

                ObjRate.hmRateDetails.clear();
                String mDocNo = Integer.toString((int) ObjRate.getAttribute("DOC_NO").getVal());

                stTemp2 = tmpconnection.createStatement();
                rsTmp2 = stTemp2.executeQuery("SELECT * FROM PRODUCTION.FELT_RATE_DETAIL WHERE DOC_NO='" + mDocNo + "' ");

                Counter = 0;
                while (rsTmp2.next()) {
                    Counter = Counter + 1;
                    clsFeltRateMasterDetail ObjRateDtl = new clsFeltRateMasterDetail();

                    ObjRateDtl.setAttribute("DOC_NO", rsTmp2.getInt("DOC_NO"));
                    ObjRateDtl.setAttribute("SR_NO", rsTmp2.getLong("SR_NO"));
                    ObjRateDtl.setAttribute("PRODUCT_CODE", rsTmp2.getString("PRODUCT_CODE"));
                    ObjRateDtl.setAttribute("PRODUCT_DESC", rsTmp2.getString("PRODUCT_DESC"));
                    ObjRateDtl.setAttribute("SQM_RATE", rsTmp2.getDouble("SQM_RATE"));
                    ObjRateDtl.setAttribute("SQM_RATE_DATE", rsTmp2.getString("SQM_RATE_DATE"));
                    ObjRateDtl.setAttribute("WT_RATE", rsTmp2.getDouble("WT_RATE"));
                    ObjRateDtl.setAttribute("WT_RATE_DATE", rsTmp2.getString("WT_RATE_DATE"));
                    ObjRateDtl.setAttribute("CREATED_BY", rsTmp2.getLong("CREATED_BY"));
                    ObjRateDtl.setAttribute("CREATED_DATE", rsTmp2.getString("CREATED_DATE"));
                    ObjRateDtl.setAttribute("MODIFIED_BY", rsTmp2.getLong("MODIFIED_BY"));
                    ObjRateDtl.setAttribute("MODIFIED_DATE", rsTmp2.getString("MODIFIED_DATE"));

                    ObjRate.hmRateDetails.put(Long.toString(Counter), ObjRateDtl);
                }// Innser while

                List.put(Long.toString(Counter), ObjRate);
            }//Outer while

            rsTmp.close();
            //tmpconnection.close();
            stTemp.close();
            rsTmp2.close();
            stTemp2.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return List;
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
            setAttribute("WH_CD", resultSet.getString("WH_CD"));
            setAttribute("EFFECTIVE_FROM", resultSet.getString("EFFECTIVE_FROM"));
            setAttribute("EFFECTIVE_TO", resultSet.getString("EFFECTIVE_TO"));
            setAttribute("PRODUCT_CODE", resultSet.getString("PRODUCT_CODE"));
            setAttribute("PRODUCT_DESC", resultSet.getString("PRODUCT_DESC"));
            setAttribute("SYN_PER", resultSet.getFloat("SYN_PER"));
            setAttribute("WT_RATE", resultSet.getFloat("WT_RATE"));

            setAttribute("CHEM_TRT_IND", resultSet.getString("CHEM_TRT_IND"));
            setAttribute("PIN_IND", resultSet.getString("PIN_IND"));
            setAttribute("SPR_IND", resultSet.getString("SPR_IND"));
            setAttribute("SUR_IND", resultSet.getString("SUR_IND"));
            setAttribute("SQM_IND", resultSet.getString("SQM_IND"));
            setAttribute("EXC_CAT_IND", resultSet.getString("EXC_CAT_IND"));

            setAttribute("CHEM_TRT_CHRG", resultSet.getFloat("CHEM_TRT_CHRG"));
            setAttribute("PIN_CHRG", resultSet.getFloat("PIN_CHRG"));
            setAttribute("SPR_CHRG", resultSet.getFloat("SPR_CHRG"));
            setAttribute("SUR_CHRG", resultSet.getFloat("SUR_CHRG"));
            setAttribute("SQM_CHRG", resultSet.getFloat("SQM_CHRG"));
            setAttribute("EXC_CAT_CHRG", resultSet.getFloat("EXC_CAT_CHRG"));
            
            setAttribute("OLD_RATE", resultSet.getFloat("OLD_RATE"));
            setAttribute("WEIGHT_RATE_CRITERIA", resultSet.getFloat("WEIGHT_RATE_CRITERIA"));
            setAttribute("SURCHARGE_LENGTH_CRITERIA", resultSet.getFloat("SURCHARGE_LENGTH_CRITERIA"));
            setAttribute("SURCHARGE_1_PER", resultSet.getFloat("SURCHARGE_1_PER"));
            setAttribute("SURCHARGE_2_PER", resultSet.getFloat("SURCHARGE_2_PER"));
            

            setAttribute("GROUP_NAME", resultSet.getString("GROUP_NAME"));
            setAttribute("DIVERSION_GROUP", resultSet.getString("DIVERSION_GROUP"));
            setAttribute("REMARKS", resultSet.getString("REMARKS"));

            setAttribute("CATEGORY", resultSet.getString("CATEGORY"));
            setAttribute("FABRIC_CATG", resultSet.getString("FABRIC_CATG"));
            setAttribute("POSITION_CATG", resultSet.getString("POSITION_CATG"));

            setAttribute("HIERARCHY_ID", resultSet.getInt("HIERARCHY_ID"));

            //Now first clear the collection and Populate it
            hmRateDetails.clear();

            stTemp = tmpconnection.createStatement();
            if (HistoryView) {
                rsTmp = stTemp.executeQuery("SELECT * FROM PRODUCTION.FELT_QLT_RATE_MASTER_H WHERE PRODUCT_CODE='" + resultSet.getString("PRODUCT_CODE") + "' AND REVISION_NO=" + RevNo + " ORDER BY SR_NO DESC");
            } else {
                rsTmp = stTemp.executeQuery("SELECT * FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE='" + resultSet.getString("PRODUCT_CODE") + "' ORDER BY DOC_NO DESC");
            }
            rsTmp.first();

            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {
                Counter = Counter + 1;
                clsFeltQltRateMasterDetail ObjRateDtl = new clsFeltQltRateMasterDetail();

                ObjRateDtl.setAttribute("SR_NO", Counter);
                ObjRateDtl.setAttribute("PRODUCT_CODE", rsTmp.getString("PRODUCT_CODE"));
                ObjRateDtl.setAttribute("EFFECTIVE_FROM", rsTmp.getString("EFFECTIVE_FROM"));
                ObjRateDtl.setAttribute("EFFECTIVE_TO", rsTmp.getString("EFFECTIVE_TO"));
                ObjRateDtl.setAttribute("SQM_RATE", rsTmp.getFloat("SQM_RATE"));
                ObjRateDtl.setAttribute("WT_RATE", rsTmp.getFloat("WT_RATE"));
//                ObjRateDtl.setAttribute("CHARGES",rsTmp.getFloat("CHARGES"));
//                ObjRateDtl.setAttribute("CHARGES_FROM_DATE",rsTmp.getString("CHARGES_FRMDT"));
//                ObjRateDtl.setAttribute("CHARGES_TO_DATE",rsTmp.getString("CHARGES_TODT"));
//                
                hmRateDetails.put(Long.toString(Counter), ObjRateDtl);
                rsTmp.next();
            }

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
                strSQL = "SELECT PRODUCTION.FELT_QLT_RATE_MASTER.DOC_NO,PRODUCTION.FELT_QLT_RATE_MASTER.DOC_DATE,PRODUCTION.FELT_QLT_RATE_MASTER.PRODUCT_CODE,RECEIVED_DATE FROM PRODUCTION.FELT_QLT_RATE_MASTER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_QLT_RATE_MASTER.DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + pUserID + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=601 AND CANCELED=0 ORDER BY PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT PRODUCTION.FELT_QLT_RATE_MASTER.DOC_NO,PRODUCTION.FELT_QLT_RATE_MASTER.DOC_DATE,PRODUCTION.FELT_QLT_RATE_MASTER.PRODUCT_CODE,RECEIVED_DATE FROM PRODUCTION.FELT_QLT_RATE_MASTER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_QLT_RATE_MASTER.DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + pUserID + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=601 AND CANCELED=0 ORDER BY PRODUCTION.FELT_QLT_RATE_MASTER.DOC_NO";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT PRODUCTION.FELT_QLT_RATE_MASTER.DOC_NO,PRODUCTION.FELT_QLT_RATE_MASTER.DOC_DATE,PRODUCTION.FELT_QLT_RATE_MASTER.PRODUCT_CODE,RECEIVED_DATE FROM PRODUCTION.FELT_QLT_RATE_MASTER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_QLT_RATE_MASTER.DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + pUserID + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=601 AND CANCELED=0 ORDER BY PRODUCTION.FELT_QLT_RATE_MASTER.DOC_NO";
            }

            ResultSet rsTmp = data.getConn().createStatement().executeQuery(strSQL);

            Counter = 0;
            while (rsTmp.next()) {

                Counter = Counter + 1;
                clsFeltQltRateMaster ObjDoc = new clsFeltQltRateMaster();

                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO", rsTmp.getString("DOC_NO"));
                ObjDoc.setAttribute("DOC_DATE", rsTmp.getString("DOC_DATE"));
                ObjDoc.setAttribute("PRODUCT_CODE", rsTmp.getString("PRODUCT_CODE"));
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
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_QLT_RATE_MASTER_H WHERE DOC_NO='" + docNo + "'");
            Ready = true;
            HistoryView = true;
            MoveFirst();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

//    public static HashMap getHistoryList(String itemCode, String docNo) {
//        HashMap hmHistoryList=new HashMap();
//        Statement stTmp;
//        ResultSet rsTmp;
//        try {
//            stTmp=data.getConn().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
//            stTmp.setFetchSize(Integer.MIN_VALUE);
//            rsTmp=stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_QLT_RATE_MASTER_H WHERE PRODUCT_CODE='"+itemCode+"' AND DOC_NO='"+docNo+"'");
//            
//            while(rsTmp.next()) {
//                clsFeltQltRateMaster ObjFeltRateMaster=new clsFeltQltRateMaster();
//                
//                ObjFeltRateMaster.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
//                ObjFeltRateMaster.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
//                ObjFeltRateMaster.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
//                ObjFeltRateMaster.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
//                ObjFeltRateMaster.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
//                
//                hmHistoryList.put(Integer.toString(hmHistoryList.size()+1),ObjFeltRateMaster);
//            }
//            rsTmp.close();
//            stTmp.close();
//            return hmHistoryList;
//        }catch(Exception e) {
//            e.printStackTrace();
//            return hmHistoryList;
//        }
//    }
    public static HashMap getHistoryList(int CompanyID, String docNo) {
        HashMap hmHistoryList = new HashMap();
        Statement stTmp;
        ResultSet rsTmp;
        try {
            stTmp = data.getConn().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp = stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_QLT_RATE_MASTER_H WHERE DOC_NO='" + docNo + "' ORDER BY REVISION_NO");

            while (rsTmp.next()) {
                clsFeltQltRateMaster ObjFeltRateMaster = new clsFeltQltRateMaster();

                ObjFeltRateMaster.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                ObjFeltRateMaster.setAttribute("UPDATED_BY", rsTmp.getInt("UPDATED_BY"));
                ObjFeltRateMaster.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                ObjFeltRateMaster.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                ObjFeltRateMaster.setAttribute("APPROVER_REMARKS", rsTmp.getString("APPROVER_REMARKS"));
                ObjFeltRateMaster.setAttribute("FROM_IP", rsTmp.getString("FROM_IP"));

                hmHistoryList.put(Integer.toString(hmHistoryList.size() + 1), ObjFeltRateMaster);
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
            rsTmp = data.getResult("SELECT DOC_NO,PRODUCT_CODE,APPROVED,CANCELLED FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE DOC_NO='" + docNo + "'");
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
            e.printStackTrace();
        }

        return strMessage;
    }

    public static boolean CanCancel(int pCompanyID, String docNo) {
        ResultSet rsTmp = null;
        boolean canCancel = false;

        try {
            rsTmp = data.getResult("SELECT DOC_NO,PRODUCT_CODE FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE DOC_NO='" + docNo + "' AND CANCELLED=0  ");
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
                rsTmp = data.getResult("SELECT APPROVED FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE DOC_NO='" + docNo + "' ");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    ApprovedDoc = rsTmp.getBoolean("APPROVED");
                }

                if (ApprovedDoc) {

                } else {
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='" + docNo + "' AND MODULE_ID=" + 601);
                }

                //Now Update the header with cancel falg to true
                data.Execute("UPDATE PRODUCTION.FELT_QLT_RATE_MASTER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURRENT_TIMESTAMP WHERE DOC_NO='" + docNo + "' ");

                Cancelled = true;
            }

            rsTmp.close();
        } catch (Exception e) {

        }
        return Cancelled;
    }

    // checks Quality no already exist in db
    public boolean checkQualityNo(String qualityNo) {
        if (data.getIntValueFromDB("SELECT COUNT(PRODUCT_CODE) FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE='" + qualityNo + "'") > 0) {
            return true;
        } else {
            return false;
        }
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

    // generates report data
    public TReportWriter.SimpleDataProvider.TTable getReportData() {
        TReportWriter.SimpleDataProvider.TTable objData = new TReportWriter.SimpleDataProvider.TTable();
        objData.AddColumn("DOC_NO");
        objData.AddColumn("DOC_DATE");
        objData.AddColumn("WH_CD");
        objData.AddColumn("PRODUCT_CODE");
        objData.AddColumn("PRODUCT_DESC");
        objData.AddColumn("SYN_PER");
        objData.AddColumn("SQM_RATE");
        objData.AddColumn("WT_RATE");
        objData.AddColumn("EFFECTIVE_FROM");
        objData.AddColumn("EFFECTIVE_TO");
        objData.AddColumn("CHEM_TRT_IND");
        objData.AddColumn("CHEM_TRT_CHRG");
        objData.AddColumn("PIN_IND");
        objData.AddColumn("PIN_CHRG");
        objData.AddColumn("SPR_IND");
        objData.AddColumn("SPR_CHRG");
        objData.AddColumn("SUR_IND");
        objData.AddColumn("SUR_CHRG");
        objData.AddColumn("SQM_IND");
        objData.AddColumn("SQM_CHRG");
        objData.AddColumn("EXC_CAT_IND");
        objData.AddColumn("EXC_CAT_CHRG");
        objData.AddColumn("FILLE");
        objData.AddColumn("CHARGES");
        objData.AddColumn("GROUP_NAME");
        objData.AddColumn("DIVERSION_GROUP");
        objData.AddColumn("CATEGORY");
        objData.AddColumn("FABRIC_CATG");
        objData.AddColumn("POSITION_CATG");
        objData.AddColumn("FELT_CATG");
        objData.AddColumn("ACTIVE_FLAG");
        objData.AddColumn("REMARKS");

        try {
            TReportWriter.SimpleDataProvider.TRow objRow = objData.newRow();

            String str = "SELECT * FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE (EFFECTIVE_TO='0000-00-00' OR EFFECTIVE_TO IS NULL) AND APPROVED=1 AND CANCELED=0 ORDER BY PRODUCT_CODE";

            ResultSet rsTemp = data.getResult(str);
            int counter = 1;
            while (!rsTemp.isAfterLast()) {
                objRow = objData.newRow();
                objRow.setValue("DOC_NO", rsTemp.getString("DOC_NO"));
                objRow.setValue("DOC_DATE", rsTemp.getString("DOC_DATE"));
                objRow.setValue("WH_CD", rsTemp.getString("WH_CD"));
                objRow.setValue("PRODUCT_CODE", rsTemp.getString("PRODUCT_CODE"));
                objRow.setValue("PRODUCT_DESC", rsTemp.getString("PRODUCT_DESC"));
                objRow.setValue("SYN_PER", rsTemp.getString("SYN_PER"));
                objRow.setValue("SQM_RATE", rsTemp.getString("SQM_RATE"));
                objRow.setValue("WT_RATE", rsTemp.getString("WT_RATE"));
                objRow.setValue("EFFECTIVE_FROM", rsTemp.getString("EFFECTIVE_FROM"));
                objRow.setValue("EFFECTIVE_TO", rsTemp.getString("EFFECTIVE_TO"));
                objRow.setValue("CHEM_TRT_IND", rsTemp.getString("CHEM_TRT_IND"));
                objRow.setValue("CHEM_TRT_CHRG", rsTemp.getString("CHEM_TRT_CHRG"));
                objRow.setValue("PIN_IND", rsTemp.getString("PIN_IND"));
                objRow.setValue("PIN_CHRG", rsTemp.getString("PIN_CHRG"));
                objRow.setValue("SPR_IND", rsTemp.getString("SPR_IND"));
                objRow.setValue("SPR_CHRG", rsTemp.getString("SPR_CHRG"));
                objRow.setValue("SUR_IND", rsTemp.getString("SUR_IND"));
                objRow.setValue("SUR_CHRG", rsTemp.getString("SUR_CHRG"));
                objRow.setValue("SQM_IND", rsTemp.getString("SQM_IND"));
                objRow.setValue("SQM_CHRG", rsTemp.getString("SQM_CHRG"));
                objRow.setValue("EXC_CAT_IND", rsTemp.getString("EXC_CAT_IND"));
                objRow.setValue("EXC_CAT_CHRG", rsTemp.getString("EXC_CAT_CHRG"));
                objRow.setValue("FILLE", rsTemp.getString("FILLE"));
                objRow.setValue("CHARGES", rsTemp.getString("CHARGES"));
                objRow.setValue("GROUP_NAME", rsTemp.getString("GROUP_NAME"));
                objRow.setValue("DIVERSION_GROUP", rsTemp.getString("DIVERSION_GROUP"));
                objRow.setValue("CATEGORY", rsTemp.getString("CATEGORY"));
                objRow.setValue("FABRIC_CATG", rsTemp.getString("FABRIC_CATG"));
                objRow.setValue("POSITION_CATG", rsTemp.getString("POSITION_CATG"));
                objRow.setValue("FELT_CATG", rsTemp.getString("FELT_CATG"));
                objRow.setValue("ACTIVE_FLAG", rsTemp.getString("ACTIVE_FLAG"));
                objRow.setValue("REMARKS", rsTemp.getString("REMARKS"));

                objData.AddRow(objRow);
                counter++;
                rsTemp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objData;
    }

}
