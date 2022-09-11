/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SDMLATTPAY.IncrementProposal;

import EITLERP.ComboData;
import EITLERP.EITLERPGLOBAL;
import EITLERP.Variant;
import EITLERP.data;
import EITLERP.frmPendingApprovals;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 *
 * @author DAXESH PRAJAPATI
 *
 */
public class clsIncrementProposal extends frmPendingApprovals {

    public String LastError = "";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet, rsHeader;
    public String UserDepartment = "";
    //private ResultSet rsResultSet,

    public HashMap props;
    public boolean Ready = false;
    //Felt Order Collection
    public HashMap hmIncrementProposalDetails;

    //History Related properties
    public boolean HistoryView = false;
    private String historyAmendDate = "";
    private String historyAmendID = "";
    private static int ModuleID = 849;

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
    public clsIncrementProposal() {
        LastError = "";
        props = new HashMap();
        props.put("IED_DOC_NO", new Variant(""));
        props.put("IED_DOC_DATE", new Variant(""));

        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("PREFIX", new Variant(""));

        props.put("IED_DOC_NO", new Variant(""));
        props.put("IED_DOC_DATE", new Variant(""));
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

        hmIncrementProposalDetails = new HashMap();

    }

    public boolean LoadData(String condition) {
        Ready = false;
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            //System.out.println("");
            resultSet = statement.executeQuery("SELECT * FROM SDMLATTPAY.INCREMENT_ENTRY_DETAIL " + condition);
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
        return true;
    }

    //Updates current record
    public boolean Update() {

        ResultSet rsH;
        Statement stH;

        try {
            // Production data connection

            stH = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsH = stH.executeQuery("SELECT * FROM  SDMLATTPAY.INCREMENT_ENTRY_DETAIL_H WHERE IED_DOC_NO=''");
            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM SDMLATTPAY.INCREMENT_ENTRY_DETAIL_H WHERE IED_DOC_NO='" + getAttribute("IED_DOC_NO").getString() + "'");

            RevNo++;
            for (int i = 1; i <= hmIncrementProposalDetails.size(); i++) {
                clsIncrementProposalDetails Objtems = (clsIncrementProposalDetails) hmIncrementProposalDetails.get(Integer.toString(i));
                rsH.moveToInsertRow();

                rsH.updateString("REVISION_NO", RevNo + "");
                rsH.updateInt("UPDATED_BY", getAttribute("MODIFIED_BY").getInt());
                rsH.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
                rsH.updateString("ENTRY_DATE", getAttribute("MODIFIED_DATE").getString());
                rsH.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
                rsH.updateString("IED_DOC_NO", getAttribute("IED_DOC_NO").getString());
                rsH.updateString("IED_DOC_DATE", getAttribute("IED_DOC_DATE").getString());
                rsH.updateString("IED_HOD_EMP_NO", "");
                rsH.updateString("IED_PAY_EMP_NO", Objtems.getAttribute("IED_PAY_EMP_NO").getString());
                rsH.updateString("IED_DEPARTMENT", Objtems.getAttribute("IED_DEPARTMENT").getString());
                rsH.updateString("IED_EMP_NAME", Objtems.getAttribute("IED_EMP_NAME").getString());
                //rsH.updateString("IED_EMP_DATE_OF_JOING", Objtems.getAttribute("IED_EMP_DATE_OF_JOING").getString());
                //rsH.updateString("IED_LAST_INC_DUE_DATE", Objtems.getAttribute("IED_LAST_INC_DUE_DATE").getString());
                //rsH.updateString("IED_DESIGNATION", Objtems.getAttribute("IED_DESIGNATION").getString());
                //rsH.updateString("IED_QUALIFICATION", Objtems.getAttribute("IED_QUALIFICATION").getString());
                //rsH.updateString("IED_MAIN_CATEGORY", Objtems.getAttribute("IED_MAIN_CATEGORY").getString());
                //rsH.updateString("IED_CATEGORY", Objtems.getAttribute("IED_CATEGORY").getString());
                rsH.updateString("IED_CURRENT_BASIC", Objtems.getAttribute("IED_CURRENT_BASIC").getString());
                rsH.updateString("IED_CURRENT_PERSONAL_PAY", Objtems.getAttribute("IED_CURRENT_PERSONAL_PAY").getString());
                rsH.updateString("IED_CURRENT_HRA", Objtems.getAttribute("IED_CURRENT_HRA").getString());
                rsH.updateString("IED_CURRENT_AWARD_HRA", Objtems.getAttribute("IED_CURRENT_AWARD_HRA").getString());
                rsH.updateString("IED_CURRENT_MAGAZINE", Objtems.getAttribute("IED_CURRENT_MAGAZINE").getString());
                rsH.updateString("IED_CURRENT_ELECTRICITY", Objtems.getAttribute("IED_CURRENT_ELECTRICITY").getString());
                rsH.updateString("IED_CURRENT_PERFORMANCE_ALLOWANCE", Objtems.getAttribute("IED_CURRENT_PERFORMANCE_ALLOWANCE").getString());
                rsH.updateString("IED_CURRENT_CONVEY_ALLOWANCE", Objtems.getAttribute("IED_CURRENT_CONVEY_ALLOWANCE").getString());
                rsH.updateString("IED_CURRENT_MEDICAL_PER", Objtems.getAttribute("IED_CURRENT_MEDICAL_PER").getString());
                rsH.updateString("IED_CURRENT_MEDICAL_AMOUNT", Objtems.getAttribute("IED_CURRENT_MEDICAL_AMOUNT").getString());
                rsH.updateString("IED_CURRENT_INFLATION_BONUS_AMOUNT", Objtems.getAttribute("IED_CURRENT_INFLATION_BONUS_AMOUNT").getString());
                rsH.updateString("IED_CURRENT_PF_AMOUNT", Objtems.getAttribute("IED_CURRENT_PF_AMOUNT").getString());
                rsH.updateString("IED_CURRENT_GRATUITY_AMOUNT", Objtems.getAttribute("IED_CURRENT_GRATUITY_AMOUNT").getString());
//                rsH.updateString("IED_CURRENT_PETROL_LTRS", Objtems.getAttribute("IED_CURRENT_PETROL_LTRS").getString());
//                rsH.updateString("IED_CURRENT_PETROL_AMOUNT", Objtems.getAttribute("IED_CURRENT_PETROL_AMOUNT").getString());
                rsH.updateString("IED_CURRENT_MONTHLY_CTC", Objtems.getAttribute("IED_CURRENT_MONTHLY_CTC").getString());
                rsH.updateString("IED_CURRENT_YEARLY_CTC", Objtems.getAttribute("IED_CURRENT_YEARLY_CTC").getString());
//                rsH.updateString("IED_LAST_INC_BASIC", Objtems.getAttribute("IED_LAST_INC_BASIC").getString());
//                rsH.updateString("IED_LAST_INC_PERSONAL_PAY", Objtems.getAttribute("IED_LAST_INC_PERSONAL_PAY").getString());
//                rsH.updateString("IED_LAST_INC_HRA", Objtems.getAttribute("IED_LAST_INC_HRA").getString());
//                rsH.updateString("IED_LAST_INC_MAGAZINE", Objtems.getAttribute("IED_LAST_INC_MAGAZINE").getString());
//                rsH.updateString("IED_LAST_INC_ELECTRICITY", Objtems.getAttribute("IED_LAST_INC_ELECTRICITY").getString());
//                rsH.updateString("IED_LAST_INC_PERFORMANCE_ALLOWANCE", Objtems.getAttribute("IED_LAST_INC_PERFORMANCE_ALLOWANCE").getString());
//                rsH.updateString("IED_LAST_INC_CONVEY_ALLOWANCE", Objtems.getAttribute("IED_LAST_INC_CONVEY_ALLOWANCE").getString());
//                rsH.updateString("IED_LAST_TOTAL_INCREMENT", Objtems.getAttribute("IED_LAST_TOTAL_INCREMENT").getString());
                rsH.updateString("IED_CURRENT_INC_BASIC", Objtems.getAttribute("IED_CURRENT_INC_BASIC").getString());
                rsH.updateString("IED_CURRENT_INC_PERSONAL_PAY", Objtems.getAttribute("IED_CURRENT_INC_PERSONAL_PAY").getString());
                rsH.updateString("IED_CURRENT_INC_HRA", Objtems.getAttribute("IED_CURRENT_INC_HRA").getString());
                rsH.updateString("IED_CURRENT_INC_MAGAZINE", Objtems.getAttribute("IED_CURRENT_INC_MAGAZINE").getString());
                rsH.updateString("IED_CURRENT_INC_ELECTRICITY", Objtems.getAttribute("IED_CURRENT_INC_ELECTRICITY").getString());
                rsH.updateString("IED_CURRENT_INC_PERFORMANCE_ALLOWANCE", Objtems.getAttribute("IED_CURRENT_INC_PERFORMANCE_ALLOWANCE").getString());
                rsH.updateString("IED_CURRENT_INC_CONVEY_ALLOWANCE", Objtems.getAttribute("IED_CURRENT_INC_CONVEY_ALLOWANCE").getString());
                rsH.updateString("IED_CURRENT_SPECIAL_INCREMENT", Objtems.getAttribute("IED_CURRENT_SPECIAL_INCREMENT").getString());
                rsH.updateString("IED_TOTAL_SPECIAL_INCREMENT", Objtems.getAttribute("IED_TOTAL_SPECIAL_INCREMENT").getString());
//                rsH.updateString("IED_CURRENT_TOTAL_INCREMENT", Objtems.getAttribute("IED_CURRENT_TOTAL_INCREMENT").getString());
                rsH.updateString("IED_REVISED_BASIC", Objtems.getAttribute("IED_REVISED_BASIC").getString());
                rsH.updateString("IED_REVISED_PERSONAL_PAY", Objtems.getAttribute("IED_REVISED_PERSONAL_PAY").getString());
                rsH.updateString("IED_REVISED_HRA", Objtems.getAttribute("IED_REVISED_HRA").getString());
//                rsH.updateString("IED_REVISED_AWARD_HRA", Objtems.getAttribute("IED_REVISED_AWARD_HRA").getString());
                rsH.updateString("IED_REVISED_MAGAZINE", Objtems.getAttribute("IED_REVISED_MAGAZINE").getString());
                rsH.updateString("IED_REVISED_ELECTRICITY", Objtems.getAttribute("IED_REVISED_ELECTRICITY").getString());
                rsH.updateString("IED_REVISED_PERFORMANCE_ALLOWANCE", Objtems.getAttribute("IED_REVISED_PERFORMANCE_ALLOWANCE").getString());
                rsH.updateString("IED_REVISED_CONVEY_ALLOWANCE", Objtems.getAttribute("IED_REVISED_CONVEY_ALLOWANCE").getString());
                rsH.updateString("IED_REVISED_MEDICAL_PER", Objtems.getAttribute("IED_REVISED_MEDICAL_PER").getString());
                rsH.updateString("IED_REVISED_MEDICAL_AMOUNT", Objtems.getAttribute("IED_REVISED_MEDICAL_AMOUNT").getString());
                rsH.updateString("IED_REVISED_INFLATION_BONUS_AMOUNT", Objtems.getAttribute("IED_REVISED_INFLATION_BONUS_AMOUNT").getString());
                rsH.updateString("IED_REVISED_PF_AMOUNT", Objtems.getAttribute("IED_REVISED_PF_AMOUNT").getString());
                rsH.updateString("IED_REVISED_GRATUITY_AMOUNT", Objtems.getAttribute("IED_REVISED_GRATUITY_AMOUNT").getString());
//                rsH.updateString("IED_REVISED_PETROL_LTRS", Objtems.getAttribute("IED_REVISED_PETROL_LTRS").getString());
//                rsH.updateString("IED_REVISED_PETROL_AMOUNT", Objtems.getAttribute("IED_REVISED_PETROL_AMOUNT").getString());
                rsH.updateString("IED_REVISED_MONTHLY_CTC", Objtems.getAttribute("IED_REVISED_MONTHLY_CTC").getString());
                rsH.updateString("IED_REVISED_YEARLY_CTC", Objtems.getAttribute("IED_REVISED_YEARLY_CTC").getString());
                rsH.updateString("IED_REVISED_PER", Objtems.getAttribute("IED_REVISED_PER").getString());
                rsH.updateString("IED_REVISED_CTC_PER", Objtems.getAttribute("IED_REVISED_CTC_PER").getString());
                rsH.updateString("IED_REVISED_SPL_PER", Objtems.getAttribute("IED_REVISED_SPL_PER").getString());
                rsH.updateString("IED_REVISED_CTC_SPL_PER", Objtems.getAttribute("IED_REVISED_CTC_SPL_PER").getString());

                rsH.updateString("IED_TOTAL_INCREMENT", Objtems.getAttribute("IED_TOTAL_INCREMENT").getString());
                rsH.updateString("IED_EMP_GRADE", Objtems.getAttribute("IED_EMP_GRADE").getString());
//                rsH.updateString("CREATED_BY", Objtems.getAttribute("CREATED_BY").getString());
//                rsH.updateString("CREATED_DATE", Objtems.getAttribute("CREATED_DATE").getString());
                rsH.updateInt("MODIFIED_BY", getAttribute("MODIFIED_BY").getInt());
                rsH.updateString("MODIFIED_DATE", getAttribute("MODIFIED_DATE").getString());
//                rsH.updateString("APPROVED", Objtems.getAttribute("APPROVED").getString());
//                rsH.updateString("APPROVED_DATE", Objtems.getAttribute("APPROVED_DATE").getString());
//                rsH.updateString("REJECTED", Objtems.getAttribute("REJECTED").getString());
//                rsH.updateString("REJECTED_DATE", Objtems.getAttribute("REJECTED_DATE").getString());
//                rsH.updateString("CANCELED", Objtems.getAttribute("CANCELED").getString());
                rsH.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
//                rsH.updateString("CHANGED", Objtems.getAttribute("CHANGED").getString());
//                rsH.updateString("CHANGED_DATE", Objtems.getAttribute("CHANGED_DATE").getString());
//                rsH.updateString("REJECTED_REMARKS", Objtems.getAttribute("REJECTED_REMARKS").getString());

                rsH.updateString("IED_CURRENT_SUPER_ANNUATION_AMOUNT", Objtems.getAttribute("IED_CURRENT_SUPER_ANNUATION_AMOUNT").getString());
                rsH.updateString("IED_REVISED_SUPER_ANNUATION_AMOUNT", Objtems.getAttribute("IED_REVISED_SUPER_ANNUATION_AMOUNT").getString());
                rsH.updateString("IED_CURRENT_ESIC_AMOUNT", Objtems.getAttribute("IED_CURRENT_ESIC_AMOUNT").getString());
                rsH.updateString("IED_REVISED_ESIC_AMOUNT", Objtems.getAttribute("IED_REVISED_ESIC_AMOUNT").getString());
                rsH.updateString("IED_CURRENT_DA_INDEX", Objtems.getAttribute("IED_CURRENT_DA_INDEX").getString());
                if (Objtems.getAttribute("IED_PROPOSE_PROMOTION").getString().equalsIgnoreCase("true")) {
                    rsH.updateBoolean("IED_PROPOSE_PROMOTION", true);
                } else {
                    rsH.updateBoolean("IED_PROPOSE_PROMOTION", false);
                }
                if (Objtems.getAttribute("IED_RESIGNATION").getString().equalsIgnoreCase("true")) {
                    rsH.updateBoolean("IED_RESIGNATION", true);
                } else {
                    rsH.updateBoolean("IED_RESIGNATION", false);
                }
                if (Objtems.getAttribute("IED_PROBATION").getString().equalsIgnoreCase("true")) {
                    rsH.updateBoolean("IED_PROBATION", true);
                } else {
                    rsH.updateBoolean("IED_PROBATION", false);
                }
                rsH.updateString("IED_REMARK", Objtems.getAttribute("IED_REMARK").getString());

//                rsH.updateString("IED_DIFF_CTC", Objtems.getAttribute("IED_DIFF_CTC").getString());
                if (getAttribute("APPROVAL_STATUS").getString().equals("F")) {
                    rsH.updateBoolean("APPROVED", true);
                    rsH.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateTimeDB());
                } else {
                    rsH.updateBoolean("APPROVED", false);
                    rsH.updateString("APPROVED_DATE", "0000-00-00");
                }
                rsH.updateBoolean("CANCELED", false);
                rsH.updateBoolean("REJECTED", false);
                rsH.updateString("REJECTED_DATE", "0000-00-00");

                rsH.updateString("FROM_IP", "" + str_split[1]);

                rsH.insertRow();
            }
            data.Execute("UPDATE SDMLATTPAY.INCREMENT_ENTRY_DETAIL_H H,SDMLATTPAY.INCREMENT_ENTRY_DETAIL D "
                    + "SET H.IED_DEPARTMENT=D.IED_DEPARTMENT,H.IED_EMP_DATE_OF_JOING=D.IED_EMP_DATE_OF_JOING,H.IED_LAST_INC_DUE_DATE=D.IED_LAST_INC_DUE_DATE,"
                    + "H.IED_DESIGNATION=D.IED_DESIGNATION,H.IED_QUALIFICATION=D.IED_QUALIFICATION,H.IED_MAIN_CATEGORY=D.IED_MAIN_CATEGORY,"
                    + "H.IED_CATEGORY=D.IED_CATEGORY,H.IED_CURRENT_PETROL_LTRS=D.IED_CURRENT_PETROL_LTRS,H.IED_CURRENT_PETROL_AMOUNT=D.IED_CURRENT_PETROL_AMOUNT,"
                    + "H.IED_LAST_INC_BASIC=D.IED_LAST_INC_BASIC,H.IED_LAST_INC_PERSONAL_PAY=D.IED_LAST_INC_PERSONAL_PAY,H.IED_LAST_INC_HRA=D.IED_LAST_INC_HRA,"
                    + "H.IED_LAST_INC_MAGAZINE=D.IED_LAST_INC_MAGAZINE,H.IED_LAST_INC_ELECTRICITY=D.IED_LAST_INC_ELECTRICITY,"
                    + "H.IED_LAST_INC_PERFORMANCE_ALLOWANCE=D.IED_LAST_INC_PERFORMANCE_ALLOWANCE,H.IED_LAST_INC_CONVEY_ALLOWANCE=D.IED_LAST_INC_CONVEY_ALLOWANCE,"
                    + "H.IED_LAST_TOTAL_INCREMENT=D.IED_LAST_TOTAL_INCREMENT,H.IED_HOD_EMP_NO=D.IED_HOD_EMP_NO,"
                    + "D.IED_TOTAL_INCREMENT=H.IED_TOTAL_INCREMENT,D.IED_EMP_GRADE=H.IED_EMP_GRADE,"
                    + "D.IED_CURRENT_INC_BASIC=H.IED_CURRENT_INC_BASIC,D.IED_CURRENT_INC_PERSONAL_PAY=H.IED_CURRENT_INC_PERSONAL_PAY,"
                    + "D.IED_CURRENT_INC_HRA=H.IED_CURRENT_INC_HRA,D.IED_CURRENT_INC_PERFORMANCE_ALLOWANCE=H.IED_CURRENT_INC_PERFORMANCE_ALLOWANCE,"
                    + "D.IED_CURRENT_INC_CONVEY_ALLOWANCE=H.IED_CURRENT_INC_CONVEY_ALLOWANCE,"
                    + "D.IED_REVISED_BASIC=H.IED_REVISED_BASIC,D.IED_REVISED_PERSONAL_PAY=H.IED_REVISED_PERSONAL_PAY,"
                    + "D.IED_REVISED_HRA=H.IED_REVISED_HRA,D.IED_REVISED_MAGAZINE=H.IED_REVISED_MAGAZINE,D.IED_REVISED_ELECTRICITY=H.IED_REVISED_ELECTRICITY,"
                    + "D.IED_REVISED_PERFORMANCE_ALLOWANCE=H.IED_REVISED_PERFORMANCE_ALLOWANCE,D.IED_REVISED_CONVEY_ALLOWANCE=H.IED_REVISED_CONVEY_ALLOWANCE,"
                    + "D.IED_REVISED_MEDICAL_PER=H.IED_REVISED_MEDICAL_PER,D.IED_REVISED_MEDICAL_AMOUNT=H.IED_REVISED_MEDICAL_AMOUNT,"
                    + "D.IED_REVISED_INFLATION_BONUS_AMOUNT=H.IED_REVISED_INFLATION_BONUS_AMOUNT,D.IED_REVISED_PF_AMOUNT=H.IED_REVISED_PF_AMOUNT,"
                    + "D.IED_REVISED_GRATUITY_AMOUNT=H.IED_REVISED_GRATUITY_AMOUNT,D.IED_REVISED_MONTHLY_CTC=H.IED_REVISED_MONTHLY_CTC,"
                    + "D.IED_REVISED_YEARLY_CTC=H.IED_REVISED_YEARLY_CTC,D.IED_REVISED_SUPER_ANNUATION_AMOUNT=H.IED_REVISED_SUPER_ANNUATION_AMOUNT,"
                    + "D.IED_REVISED_ESIC_AMOUNT=H.IED_REVISED_ESIC_AMOUNT,"
                    + "D.IED_PROPOSE_PROMOTION=H.IED_PROPOSE_PROMOTION,"
                    + "D.IED_RESIGNATION=H.IED_RESIGNATION,"
                    + "D.IED_PROBATION=H.IED_PROBATION,D.IED_REMARK=H.IED_REMARK,"
                    + "D.MODIFIED_BY=H.MODIFIED_BY,D.MODIFIED_DATE=H.MODIFIED_DATE,"
                    + "D.IED_CURRENT_SPECIAL_INCREMENT=H.IED_CURRENT_SPECIAL_INCREMENT,"
                    + "D.IED_TOTAL_SPECIAL_INCREMENT=H.IED_TOTAL_SPECIAL_INCREMENT,"
                    + "D.IED_REVISED_PER=H.IED_REVISED_PER,D.IED_REVISED_SPL_PER=H.IED_REVISED_SPL_PER,"
                    + "D.IED_REVISED_CTC_PER=H.IED_REVISED_CTC_PER,D.IED_REVISED_CTC_SPL_PER=H.IED_REVISED_CTC_SPL_PER "
                    + "where REVISION_NO=" + RevNo + " AND H.IED_DOC_NO='" + getAttribute("IED_DOC_NO").getString() + "' AND H.IED_DOC_NO=D.IED_DOC_NO AND H.IED_PAY_EMP_NO=D.IED_PAY_EMP_NO");

            //======== Update the Approval Flow =========
            SDMLATTPAY.ApprovalFlow ObjApprovalFlow = new SDMLATTPAY.ApprovalFlow();

            ObjApprovalFlow.ModuleID = ModuleID; //
            ObjApprovalFlow.DocNo = getAttribute("IED_DOC_NO").getString();
            ObjApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateTimeDB();
            ObjApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getString();
            ObjApprovalFlow.TableName = "SDMLATTPAY.INCREMENT_ENTRY_DETAIL";
            ObjApprovalFlow.IsCreator = true;
            ObjApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjApprovalFlow.Remarks = getAttribute("FROM_REMARKS").getString();
            ObjApprovalFlow.FieldName = "IED_DOC_NO";
            ObjApprovalFlow.CompanyID = EITLERPGLOBAL.gCompanyID;
            if (getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjApprovalFlow.To = (int) getAttribute("SEND_DOC_TO").getVal();
                ObjApprovalFlow.ExplicitSendTo = true;
            }
            //if(getAttribute("APPROVAL_STATUS").getString().equals("A") || getAttribute("APPROVAL_STATUS").getString().equals("R")) {
//                    String Subject = "Felt Order Pending Document : "+getAttribute("IED_DOC_NO").getString();
//                    String Message = "Document No : "+getAttribute("IED_DOC_NO").getString()+" is added in your PENDING DOCUMENT"
//                            + "\n\n\n\n SDML-ERP : http://200.0.0.230:8080/SDMLERP";
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
                data.Execute("UPDATE SDMLATTPAY.INCREMENT_ENTRY_DETAIL SET REJECTED=0,CHANGED=1 WHERE IED_DOC_NO ='" + getAttribute("IED_DOC_NO").getString() + "'");
                ObjApprovalFlow.IsCreator = true;
            }
            //==========================================//

            if (ObjApprovalFlow.Status.equals("H")) {
                if (IsRejected) {
                    ObjApprovalFlow.Status = "A";
                    ObjApprovalFlow.To = ObjApprovalFlow.From;
                    ObjApprovalFlow.UpdateFlow();
                }
            } else {
                if (!ObjApprovalFlow.UpdateFlow()) {
                    LastError = ObjApprovalFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------

            // Update  in Management Increment System
            if (ObjApprovalFlow.Status.equals("F")) {
                data.Execute("DELETE FROM SDMLATTPAY.INCREMENT_MANAGEMENT_ENTRY_DETAIL "
                        + "WHERE IED_DOC_NO='" + getAttribute("IED_DOC_NO").getString() + "'");
                insertdata(0, EITLERPGLOBAL.gNewUserID);
                insertdata(1, 18);
                insertdata(2, 243);
                insertdata(3, 11);
                insertdata(4, 72);
            }

            setData();

            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    private void insertdata(int srno, int userid) {
        String insertdata = "INSERT INTO SDMLATTPAY.INCREMENT_MANAGEMENT_ENTRY_DETAIL "
                + "(IED_DOC_NO,IED_DOC_DATE,IED_HOD_EMP_NO,IED_PAY_EMP_NO,IED_DEPARTMENT,IED_EMP_NAME,"
                + "IED_EMP_DATE_OF_JOING,IED_LAST_INC_DUE_DATE,IED_DESIGNATION,IED_QUALIFICATION,"
                + "IED_MAIN_CATEGORY,IED_CATEGORY,IED_CURRENT_BASIC,IED_CURRENT_PERSONAL_PAY,IED_CURRENT_HRA,"
                + "IED_CURRENT_AWARD_HRA,IED_CURRENT_MAGAZINE,IED_CURRENT_ELECTRICITY,"
                + "IED_CURRENT_PERFORMANCE_ALLOWANCE,IED_CURRENT_CONVEY_ALLOWANCE,IED_CURRENT_MEDICAL_PER,"
                + "IED_CURRENT_MEDICAL_AMOUNT,IED_CURRENT_INFLATION_BONUS_AMOUNT,IED_CURRENT_PF_AMOUNT,"
                + "IED_CURRENT_GRATUITY_AMOUNT,IED_CURRENT_PETROL_LTRS,IED_CURRENT_PETROL_AMOUNT,"
                + "IED_CURRENT_MONTHLY_CTC,IED_CURRENT_YEARLY_CTC,IED_LAST_INC_BASIC,IED_LAST_INC_PERSONAL_PAY,"
                + "IED_LAST_INC_HRA,IED_LAST_INC_MAGAZINE,IED_LAST_INC_ELECTRICITY,"
                + "IED_LAST_INC_PERFORMANCE_ALLOWANCE,IED_LAST_INC_CONVEY_ALLOWANCE,IED_LAST_TOTAL_INCREMENT,"
                + "IED_CURRENT_INC_BASIC,IED_CURRENT_INC_PERSONAL_PAY,IED_CURRENT_INC_HRA,"
                + "IED_CURRENT_INC_MAGAZINE,IED_CURRENT_INC_ELECTRICITY,IED_CURRENT_INC_PERFORMANCE_ALLOWANCE,"
                + "IED_CURRENT_INC_CONVEY_ALLOWANCE,IED_CURRENT_TOTAL_INCREMENT,IED_REVISED_BASIC,"
                + "IED_REVISED_PERSONAL_PAY,IED_REVISED_HRA,IED_REVISED_AWARD_HRA,IED_REVISED_MAGAZINE,"
                + "IED_REVISED_ELECTRICITY,IED_REVISED_PERFORMANCE_ALLOWANCE,"
                + "IED_REVISED_CONVEY_ALLOWANCE,IED_REVISED_MEDICAL_PER,IED_REVISED_MEDICAL_AMOUNT,"
                + "IED_REVISED_INFLATION_BONUS_AMOUNT,IED_REVISED_PF_AMOUNT,IED_REVISED_GRATUITY_AMOUNT,"
                + "IED_REVISED_PETROL_LTRS,IED_REVISED_PETROL_AMOUNT,IED_REVISED_MONTHLY_CTC,"
                + "IED_REVISED_YEARLY_CTC,IED_REVISED_PER,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,"
                + "APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,CANCELED,HIERARCHY_ID,CHANGED,CHANGED_DATE,"
                + "REJECTED_REMARKS,FROM_IP,IED_CURRENT_SUPER_ANNUATION_AMOUNT,IED_REVISED_SUPER_ANNUATION_AMOUNT,"
                + "IED_CURRENT_ESIC_AMOUNT,IED_REVISED_ESIC_AMOUNT,IED_CURRENT_DA_INDEX,IED_PROPOSE_PROMOTION,"
                + "IED_DIFF_CTC,IED_TOTAL_INCREMENT,IED_EMP_GRADE,IED_REVISED_CTC_PER,IED_REVISED_SPL_PER,"
                + "IED_REVISED_CTC_SPL_PER,IED_CURRENT_SPECIAL_INCREMENT,IED_TOTAL_SPECIAL_INCREMENT,"
                + "IED_SR_NO,IED_UPDATE_BY,IED_RESIGNATION,IED_PROBATION,IED_REMARK)"
                + "SELECT IED_DOC_NO,IED_DOC_DATE,IED_HOD_EMP_NO,IED_PAY_EMP_NO,IED_DEPARTMENT,IED_EMP_NAME,"
                + "IED_EMP_DATE_OF_JOING,IED_LAST_INC_DUE_DATE,IED_DESIGNATION,IED_QUALIFICATION,"
                + "IED_MAIN_CATEGORY,IED_CATEGORY,IED_CURRENT_BASIC,IED_CURRENT_PERSONAL_PAY,IED_CURRENT_HRA,"
                + "IED_CURRENT_AWARD_HRA,IED_CURRENT_MAGAZINE,IED_CURRENT_ELECTRICITY,"
                + "IED_CURRENT_PERFORMANCE_ALLOWANCE,IED_CURRENT_CONVEY_ALLOWANCE,IED_CURRENT_MEDICAL_PER,"
                + "IED_CURRENT_MEDICAL_AMOUNT,IED_CURRENT_INFLATION_BONUS_AMOUNT,IED_CURRENT_PF_AMOUNT,"
                + "IED_CURRENT_GRATUITY_AMOUNT,IED_CURRENT_PETROL_LTRS,IED_CURRENT_PETROL_AMOUNT,"
                + "IED_CURRENT_MONTHLY_CTC,IED_CURRENT_YEARLY_CTC,IED_LAST_INC_BASIC,IED_LAST_INC_PERSONAL_PAY,"
                + "IED_LAST_INC_HRA,IED_LAST_INC_MAGAZINE,IED_LAST_INC_ELECTRICITY,"
                + "IED_LAST_INC_PERFORMANCE_ALLOWANCE,IED_LAST_INC_CONVEY_ALLOWANCE,IED_LAST_TOTAL_INCREMENT,"
                + "IED_CURRENT_INC_BASIC,IED_CURRENT_INC_PERSONAL_PAY,IED_CURRENT_INC_HRA,"
                + "IED_CURRENT_INC_MAGAZINE,IED_CURRENT_INC_ELECTRICITY,IED_CURRENT_INC_PERFORMANCE_ALLOWANCE,"
                + "IED_CURRENT_INC_CONVEY_ALLOWANCE,IED_CURRENT_TOTAL_INCREMENT,IED_REVISED_BASIC,"
                + "IED_REVISED_PERSONAL_PAY,IED_REVISED_HRA,IED_REVISED_AWARD_HRA,IED_REVISED_MAGAZINE,"
                + "IED_REVISED_ELECTRICITY,IED_REVISED_PERFORMANCE_ALLOWANCE,"
                + "IED_REVISED_CONVEY_ALLOWANCE,IED_REVISED_MEDICAL_PER,IED_REVISED_MEDICAL_AMOUNT,"
                + "IED_REVISED_INFLATION_BONUS_AMOUNT,IED_REVISED_PF_AMOUNT,IED_REVISED_GRATUITY_AMOUNT,"
                + "IED_REVISED_PETROL_LTRS,IED_REVISED_PETROL_AMOUNT,IED_REVISED_MONTHLY_CTC,"
                + "IED_REVISED_YEARLY_CTC,IED_REVISED_PER,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,"
                + "APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,CANCELED,HIERARCHY_ID,CHANGED,CHANGED_DATE,"
                + "REJECTED_REMARKS,FROM_IP,IED_CURRENT_SUPER_ANNUATION_AMOUNT,IED_REVISED_SUPER_ANNUATION_AMOUNT,"
                + "IED_CURRENT_ESIC_AMOUNT,IED_REVISED_ESIC_AMOUNT,IED_CURRENT_DA_INDEX,IED_PROPOSE_PROMOTION,"
                + "IED_DIFF_CTC,IED_TOTAL_INCREMENT,IED_EMP_GRADE,IED_REVISED_CTC_PER,IED_REVISED_SPL_PER,"
                + "IED_REVISED_CTC_SPL_PER,IED_CURRENT_SPECIAL_INCREMENT,IED_TOTAL_SPECIAL_INCREMENT," + srno + "," + userid + ","
                + "IED_RESIGNATION,IED_PROBATION,IED_REMARK "
                + "FROM SDMLATTPAY.INCREMENT_ENTRY_DETAIL I "
                + "WHERE IED_DOC_NO='" + getAttribute("IED_DOC_NO").getString() + "'";
        System.out.println("Insert Data:" + insertdata);
        data.Execute(insertdata);
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.INCREMENT_ENTRY_DETAIL WHERE  IED_DOC_NO='" + documentNo + "' AND APPROVED=" + 1;
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=" + ModuleID + " AND USER_ID=" + userID + " AND IED_DOC_NO='" + documentNo + "' AND STATUS='W'";
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();

                if (rsTmp.getInt("COUNT") > 0) {
                    strSQL = "DELETE FROM SDMLATTPAY.INCREMENT_ENTRY_DETAIL WHERE "
                            + " IED_DOC_DATE = '" + EITLERPGLOBAL.formatDateDB(stringProductionDate) + "'"
                            + " AND IED_DOC_NO ='" + documentNo + "'";

                    tmpStmt.executeUpdate(strSQL);
                    //LoadData();
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.INCREMENT_ENTRY_DETAIL WHERE IED_DOC_NO='" + orderupdDocumentNo + "' AND APPROVED=1";
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=" + ModuleID + " AND USER_ID=" + userID + " AND DOC_NO='" + orderupdDocumentNo + "' AND STATUS='W'";
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
            String strSql = "SELECT D.*,M.NAME AS CATEGORY,C.NAME AS SUB_CATEGORY,EMP_GRADE,EMP_DOB FROM SDMLATTPAY.INCREMENT_ENTRY_DETAIL AS D "
                    + "LEFT JOIN SDMLATTPAY.ATT_MAIN_CATEGORY_MASTER AS M ON SECID=IED_MAIN_CATEGORY "
                    + "LEFT JOIN (SELECT PAY_EMP_NO,EMP_GRADE,DATE_FORMAT(EMP_BIRTH_DATE, '%d/%m/%Y') AS EMP_DOB FROM SDMLATTPAY.ATTPAY_EMPMST) AS EM ON EM.PAY_EMP_NO=IED_PAY_EMP_NO "
                    + "LEFT JOIN SDMLATTPAY.ATT_CATEGORY_MASTER AS C ON CTGID=IED_CATEGORY WHERE  " + stringFindQuery + "";
            System.out.println("STR SQL : " + strSql);
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery(strSql);
            if (!resultSet.first()) {
                //LoadData();
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

            setAttribute("IED_DOC_NO", resultSet.getString("IED_DOC_NO"));
            setAttribute("IED_DOC_DATE", resultSet.getString("IED_DOC_DATE"));

            setAttribute("CREATED_BY", resultSet.getString("CREATED_BY"));
            setAttribute("CREATED_DATE", resultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY", resultSet.getString("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE", resultSet.getString("MODIFIED_DATE"));
            setAttribute("APPROVED", resultSet.getString("APPROVED"));
            setAttribute("APPROVED_DATE", resultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED", resultSet.getString("REJECTED"));
            setAttribute("REJECTED_DATE", resultSet.getString("REJECTED_DATE"));
            setAttribute("CANCELED", resultSet.getString("CANCELED"));
            setAttribute("HIERARCHY_ID", resultSet.getString("HIERARCHY_ID"));
            setAttribute("CHANGED", resultSet.getString("CHANGED"));
            setAttribute("CHANGED_DATE", resultSet.getString("CHANGED_DATE"));
            setAttribute("REJECTED_REMARKS", resultSet.getString("REJECTED_REMARKS"));
            setAttribute("FROM_IP", resultSet.getString("FROM_IP"));

            //setAttribute("EMAIL_UPDATE_TO_PM", resultSet.getBoolean("EMAIL_UPDATE_TO_PM"));
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

            UserDepartment = resultSet.getString("IED_DOC_NO").substring(5);
            hmIncrementProposalDetails.clear();

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            if (HistoryView) {

                resultSetTemp = statementTemp.executeQuery("SELECT D.*,M.*,CM.NAME AS CATEGORY,C.NAME AS SUB_CATEGORY,EMP_GRADE,EMP_DOB FROM SDMLATTPAY.INCREMENT_ENTRY_DETAIL_H AS D "
                        + "LEFT JOIN SDMLATTPAY.ATT_MAIN_CATEGORY_MASTER AS CM ON SECID=IED_MAIN_CATEGORY "
                        + "LEFT JOIN SDMLATTPAY.ATT_CATEGORY_MASTER AS C ON CTGID=IED_CATEGORY "
                        + "LEFT JOIN (SELECT PAY_EMP_NO,EMP_GRADE,DATE_FORMAT(EMP_BIRTH_DATE, '%d/%m/%Y') AS EMP_DOB  FROM SDMLATTPAY.ATTPAY_EMPMST) AS EM ON EM.PAY_EMP_NO=IED_PAY_EMP_NO "
                        + "LEFT JOIN (SELECT USER_ID,USER_NAME FROM DINESHMILLS.D_COM_USER_MASTER) AS M ON COALESCE(MODIFIED_BY,0)=USER_ID WHERE IED_DOC_NO='"+resultSet.getString("IED_DOC_NO")+"'    ORDER BY IED_DOC_DATE");
            } else {
                System.out.println("UserDepartment" + UserDepartment);
                resultSetTemp = statementTemp.executeQuery("SELECT D.*,M.*,CM.NAME AS CATEGORY,C.NAME AS SUB_CATEGORY,EMP_GRADE,EMP_DOB FROM SDMLATTPAY.INCREMENT_ENTRY_DETAIL AS D "
                        + "LEFT JOIN SDMLATTPAY.ATT_MAIN_CATEGORY_MASTER AS CM ON SECID=IED_MAIN_CATEGORY "
                        + "LEFT JOIN SDMLATTPAY.ATT_CATEGORY_MASTER AS C ON CTGID=IED_CATEGORY "
                        + "LEFT JOIN (SELECT PAY_EMP_NO,EMP_GRADE,DATE_FORMAT(EMP_BIRTH_DATE, '%d/%m/%Y') AS EMP_DOB FROM SDMLATTPAY.ATTPAY_EMPMST) AS EM ON EM.PAY_EMP_NO=IED_PAY_EMP_NO "
                        + "LEFT JOIN (SELECT USER_ID,USER_NAME FROM DINESHMILLS.D_COM_USER_MASTER) AS M ON COALESCE(MODIFIED_BY,0)=USER_ID WHERE IED_DOC_NO='"+resultSet.getString("IED_DOC_NO")+"'    ORDER BY IED_DOC_DATE");
            }
            while (resultSetTemp.next()) {
                serialNoCounter++;

                clsIncrementProposalDetails ObjIncrementProposalDetails = new clsIncrementProposalDetails();

                //ObjFeltSalesOrderDetails.setAttribute("SR_NO",serialNoCounter);
                // ObjFeltSalesOrderDetails.setAttribute("S_ORDER_DETAIL_CODE",resultSetTemp.getString("S_ORDER_DETAIL_CODE"));
                ObjIncrementProposalDetails.setAttribute("IED_DOC_NO", resultSetTemp.getString("IED_DOC_NO"));
                ObjIncrementProposalDetails.setAttribute("IED_DOC_DATE", resultSetTemp.getString("IED_DOC_DATE"));
                ObjIncrementProposalDetails.setAttribute("USER_NAME", resultSetTemp.getString("USER_NAME"));
                ObjIncrementProposalDetails.setAttribute("CREATED_BY", resultSetTemp.getString("CREATED_BY"));
                ObjIncrementProposalDetails.setAttribute("CREATED_DATE", resultSetTemp.getString("CREATED_DATE"));
                ObjIncrementProposalDetails.setAttribute("MODIFIED_BY", resultSetTemp.getString("MODIFIED_BY"));
                ObjIncrementProposalDetails.setAttribute("MODIFIED_DATE", resultSetTemp.getString("MODIFIED_DATE"));
                ObjIncrementProposalDetails.setAttribute("APPROVED", resultSetTemp.getString("APPROVED"));
                ObjIncrementProposalDetails.setAttribute("APPROVED_DATE", resultSetTemp.getString("APPROVED_DATE"));
                ObjIncrementProposalDetails.setAttribute("REJECTED", resultSetTemp.getString("REJECTED"));
                ObjIncrementProposalDetails.setAttribute("REJECTED_DATE", resultSetTemp.getString("REJECTED_DATE"));
                ObjIncrementProposalDetails.setAttribute("CANCELED", resultSetTemp.getString("CANCELED"));
                ObjIncrementProposalDetails.setAttribute("HIERARCHY_ID", resultSetTemp.getString("HIERARCHY_ID"));
                ObjIncrementProposalDetails.setAttribute("CHANGED", resultSetTemp.getString("CHANGED"));
                ObjIncrementProposalDetails.setAttribute("CHANGED_DATE", resultSetTemp.getString("CHANGED_DATE"));
                ObjIncrementProposalDetails.setAttribute("REJECTED_REMARKS", resultSetTemp.getString("REJECTED_REMARKS"));
                ObjIncrementProposalDetails.setAttribute("FROM_IP", resultSetTemp.getString("FROM_IP"));
                ObjIncrementProposalDetails.setAttribute("IED_HOD_EMP_NO", resultSetTemp.getString("IED_HOD_EMP_NO"));
                ObjIncrementProposalDetails.setAttribute("IED_PAY_EMP_NO", resultSetTemp.getString("IED_PAY_EMP_NO"));
                ObjIncrementProposalDetails.setAttribute("IED_DEPARTMENT", resultSetTemp.getString("IED_DEPARTMENT"));
                ObjIncrementProposalDetails.setAttribute("IED_EMP_NAME", resultSetTemp.getString("IED_EMP_NAME"));
                ObjIncrementProposalDetails.setAttribute("IED_EMP_DATE_OF_JOING", resultSetTemp.getString("IED_EMP_DATE_OF_JOING"));
                ObjIncrementProposalDetails.setAttribute("IED_LAST_INC_DUE_DATE", resultSetTemp.getString("IED_LAST_INC_DUE_DATE"));
                ObjIncrementProposalDetails.setAttribute("IED_DESIGNATION", resultSetTemp.getString("IED_DESIGNATION"));
                ObjIncrementProposalDetails.setAttribute("IED_QUALIFICATION", resultSetTemp.getString("IED_QUALIFICATION"));
                ObjIncrementProposalDetails.setAttribute("CATEGORY", resultSetTemp.getString("CATEGORY"));
                ObjIncrementProposalDetails.setAttribute("SUB_CATEGORY", resultSetTemp.getString("SUB_CATEGORY"));
                ObjIncrementProposalDetails.setAttribute("EMP_GRADE", resultSetTemp.getString("EMP_GRADE"));
                ObjIncrementProposalDetails.setAttribute("EMP_DOB", resultSetTemp.getString("EMP_DOB"));
                ObjIncrementProposalDetails.setAttribute("IED_MAIN_CATEGORY", resultSetTemp.getString("IED_MAIN_CATEGORY"));
                ObjIncrementProposalDetails.setAttribute("IED_CATEGORY", resultSetTemp.getString("IED_CATEGORY"));
                ObjIncrementProposalDetails.setAttribute("IED_CURRENT_BASIC", resultSetTemp.getString("IED_CURRENT_BASIC"));
                ObjIncrementProposalDetails.setAttribute("IED_CURRENT_PERSONAL_PAY", resultSetTemp.getString("IED_CURRENT_PERSONAL_PAY"));
                ObjIncrementProposalDetails.setAttribute("IED_CURRENT_DA_INDEX", resultSetTemp.getString("IED_CURRENT_DA_INDEX"));
                ObjIncrementProposalDetails.setAttribute("IED_CURRENT_ESIC_AMOUNT", resultSetTemp.getString("IED_CURRENT_ESIC_AMOUNT"));
                ObjIncrementProposalDetails.setAttribute("IED_CURRENT_HRA", resultSetTemp.getString("IED_CURRENT_HRA"));
                ObjIncrementProposalDetails.setAttribute("IED_CURRENT_AWARD_HRA", resultSetTemp.getString("IED_CURRENT_AWARD_HRA"));
                ObjIncrementProposalDetails.setAttribute("IED_CURRENT_MAGAZINE", resultSetTemp.getString("IED_CURRENT_MAGAZINE"));
                ObjIncrementProposalDetails.setAttribute("IED_CURRENT_ELECTRICITY", resultSetTemp.getString("IED_CURRENT_ELECTRICITY"));
                ObjIncrementProposalDetails.setAttribute("IED_CURRENT_PERFORMANCE_ALLOWANCE", resultSetTemp.getString("IED_CURRENT_PERFORMANCE_ALLOWANCE"));
                ObjIncrementProposalDetails.setAttribute("IED_CURRENT_CONVEY_ALLOWANCE", resultSetTemp.getString("IED_CURRENT_CONVEY_ALLOWANCE"));
                ObjIncrementProposalDetails.setAttribute("IED_CURRENT_MEDICAL_PER", resultSetTemp.getString("IED_CURRENT_MEDICAL_PER"));
                ObjIncrementProposalDetails.setAttribute("IED_CURRENT_MEDICAL_AMOUNT", resultSetTemp.getString("IED_CURRENT_MEDICAL_AMOUNT"));
                ObjIncrementProposalDetails.setAttribute("IED_CURRENT_INFLATION_BONUS_AMOUNT", resultSetTemp.getString("IED_CURRENT_INFLATION_BONUS_AMOUNT"));
                ObjIncrementProposalDetails.setAttribute("IED_CURRENT_PF_AMOUNT", resultSetTemp.getString("IED_CURRENT_PF_AMOUNT"));
                ObjIncrementProposalDetails.setAttribute("IED_CURRENT_GRATUITY_AMOUNT", resultSetTemp.getString("IED_CURRENT_GRATUITY_AMOUNT"));
                ObjIncrementProposalDetails.setAttribute("IED_CURRENT_PETROL_LTRS", resultSetTemp.getString("IED_CURRENT_PETROL_LTRS"));
                ObjIncrementProposalDetails.setAttribute("IED_CURRENT_PETROL_AMOUNT", resultSetTemp.getString("IED_CURRENT_PETROL_AMOUNT"));
                ObjIncrementProposalDetails.setAttribute("IED_CURRENT_SUPER_ANNUATION_AMOUNT", resultSetTemp.getString("IED_CURRENT_SUPER_ANNUATION_AMOUNT"));
                ObjIncrementProposalDetails.setAttribute("IED_CURRENT_MONTHLY_CTC", resultSetTemp.getString("IED_CURRENT_MONTHLY_CTC"));
                ObjIncrementProposalDetails.setAttribute("IED_CURRENT_YEARLY_CTC", resultSetTemp.getString("IED_CURRENT_YEARLY_CTC"));
                ObjIncrementProposalDetails.setAttribute("IED_LAST_INC_BASIC", resultSetTemp.getString("IED_LAST_INC_BASIC"));
                ObjIncrementProposalDetails.setAttribute("IED_LAST_INC_PERSONAL_PAY", resultSetTemp.getString("IED_LAST_INC_PERSONAL_PAY"));
                ObjIncrementProposalDetails.setAttribute("IED_LAST_INC_HRA", resultSetTemp.getString("IED_LAST_INC_HRA"));
                ObjIncrementProposalDetails.setAttribute("IED_LAST_INC_MAGAZINE", resultSetTemp.getString("IED_LAST_INC_MAGAZINE"));
                ObjIncrementProposalDetails.setAttribute("IED_LAST_INC_ELECTRICITY", resultSetTemp.getString("IED_LAST_INC_ELECTRICITY"));
                ObjIncrementProposalDetails.setAttribute("IED_LAST_INC_PERFORMANCE_ALLOWANCE", resultSetTemp.getString("IED_LAST_INC_PERFORMANCE_ALLOWANCE"));
                ObjIncrementProposalDetails.setAttribute("IED_LAST_INC_CONVEY_ALLOWANCE", resultSetTemp.getString("IED_LAST_INC_CONVEY_ALLOWANCE"));
                ObjIncrementProposalDetails.setAttribute("IED_LAST_TOTAL_INCREMENT", resultSetTemp.getString("IED_LAST_TOTAL_INCREMENT"));
                ObjIncrementProposalDetails.setAttribute("IED_CURRENT_INC_BASIC", resultSetTemp.getString("IED_CURRENT_INC_BASIC"));
                ObjIncrementProposalDetails.setAttribute("IED_CURRENT_INC_PERSONAL_PAY", resultSetTemp.getString("IED_CURRENT_INC_PERSONAL_PAY"));
                ObjIncrementProposalDetails.setAttribute("IED_CURRENT_INC_HRA", resultSetTemp.getString("IED_CURRENT_INC_HRA"));
                ObjIncrementProposalDetails.setAttribute("IED_CURRENT_INC_MAGAZINE", resultSetTemp.getString("IED_CURRENT_INC_MAGAZINE"));
                ObjIncrementProposalDetails.setAttribute("IED_CURRENT_INC_ELECTRICITY", resultSetTemp.getString("IED_CURRENT_INC_ELECTRICITY"));
                ObjIncrementProposalDetails.setAttribute("IED_CURRENT_INC_PERFORMANCE_ALLOWANCE", resultSetTemp.getString("IED_CURRENT_INC_PERFORMANCE_ALLOWANCE"));
                ObjIncrementProposalDetails.setAttribute("IED_CURRENT_INC_CONVEY_ALLOWANCE", resultSetTemp.getString("IED_CURRENT_INC_CONVEY_ALLOWANCE"));
                ObjIncrementProposalDetails.setAttribute("IED_CURRENT_TOTAL_INCREMENT", resultSetTemp.getString("IED_CURRENT_TOTAL_INCREMENT"));
                ObjIncrementProposalDetails.setAttribute("IED_CURRENT_SPECIAL_INCREMENT", resultSetTemp.getString("IED_CURRENT_SPECIAL_INCREMENT"));
                ObjIncrementProposalDetails.setAttribute("IED_TOTAL_SPECIAL_INCREMENT", resultSetTemp.getString("IED_TOTAL_SPECIAL_INCREMENT"));
                ObjIncrementProposalDetails.setAttribute("IED_REVISED_BASIC", resultSetTemp.getString("IED_REVISED_BASIC"));
                ObjIncrementProposalDetails.setAttribute("IED_REVISED_PERSONAL_PAY", resultSetTemp.getString("IED_REVISED_PERSONAL_PAY"));
                ObjIncrementProposalDetails.setAttribute("IED_REVISED_HRA", resultSetTemp.getString("IED_REVISED_HRA"));
                ObjIncrementProposalDetails.setAttribute("IED_REVISED_AWARD_HRA", resultSetTemp.getString("IED_REVISED_AWARD_HRA"));
                ObjIncrementProposalDetails.setAttribute("IED_REVISED_MAGAZINE", resultSetTemp.getString("IED_REVISED_MAGAZINE"));
                ObjIncrementProposalDetails.setAttribute("IED_REVISED_ELECTRICITY", resultSetTemp.getString("IED_REVISED_ELECTRICITY"));
                ObjIncrementProposalDetails.setAttribute("IED_REVISED_PERFORMANCE_ALLOWANCE", resultSetTemp.getString("IED_REVISED_PERFORMANCE_ALLOWANCE"));
                ObjIncrementProposalDetails.setAttribute("IED_REVISED_CONVEY_ALLOWANCE", resultSetTemp.getString("IED_REVISED_CONVEY_ALLOWANCE"));
                ObjIncrementProposalDetails.setAttribute("IED_REVISED_MEDICAL_PER", resultSetTemp.getString("IED_REVISED_MEDICAL_PER"));
                ObjIncrementProposalDetails.setAttribute("IED_REVISED_MEDICAL_AMOUNT", resultSetTemp.getString("IED_REVISED_MEDICAL_AMOUNT"));
                ObjIncrementProposalDetails.setAttribute("IED_REVISED_INFLATION_BONUS_AMOUNT", resultSetTemp.getString("IED_REVISED_INFLATION_BONUS_AMOUNT"));
                ObjIncrementProposalDetails.setAttribute("IED_REVISED_PF_AMOUNT", resultSetTemp.getString("IED_REVISED_PF_AMOUNT"));
                ObjIncrementProposalDetails.setAttribute("IED_PROPOSE_PROMOTION", resultSetTemp.getBoolean("IED_PROPOSE_PROMOTION"));
                ObjIncrementProposalDetails.setAttribute("IED_REVISED_GRATUITY_AMOUNT", resultSetTemp.getString("IED_REVISED_GRATUITY_AMOUNT"));
                ObjIncrementProposalDetails.setAttribute("IED_REVISED_PETROL_LTRS", resultSetTemp.getString("IED_REVISED_PETROL_LTRS"));
                ObjIncrementProposalDetails.setAttribute("IED_REVISED_PETROL_AMOUNT", resultSetTemp.getString("IED_REVISED_PETROL_AMOUNT"));
                ObjIncrementProposalDetails.setAttribute("IED_REVISED_MONTHLY_CTC", resultSetTemp.getString("IED_REVISED_MONTHLY_CTC"));
                ObjIncrementProposalDetails.setAttribute("IED_REVISED_YEARLY_CTC", resultSetTemp.getString("IED_REVISED_YEARLY_CTC"));
                ObjIncrementProposalDetails.setAttribute("IED_REVISED_PER", resultSetTemp.getString("IED_REVISED_PER"));
                ObjIncrementProposalDetails.setAttribute("IED_REVISED_SPL_PER", resultSetTemp.getString("IED_REVISED_SPL_PER"));
                ObjIncrementProposalDetails.setAttribute("IED_REVISED_CTC_PER", resultSetTemp.getString("IED_REVISED_CTC_PER"));
                ObjIncrementProposalDetails.setAttribute("IED_REVISED_CTC_SPL_PER", resultSetTemp.getString("IED_REVISED_CTC_SPL_PER"));
                ObjIncrementProposalDetails.setAttribute("IED_TOTAL_INCREMENT", resultSetTemp.getString("IED_TOTAL_INCREMENT"));
                ObjIncrementProposalDetails.setAttribute("IED_RESIGNATION", resultSetTemp.getBoolean("IED_RESIGNATION"));
                ObjIncrementProposalDetails.setAttribute("IED_PROBATION", resultSetTemp.getBoolean("IED_PROBATION"));
                ObjIncrementProposalDetails.setAttribute("IED_REMARK", resultSetTemp.getString("IED_REMARK"));
                hmIncrementProposalDetails.put(Integer.toString(serialNoCounter), ObjIncrementProposalDetails);
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
            String strSql = "SELECT * FROM SDMLATTPAY.INCREMENT_ENTRY_DETAIL_H WHERE IED_DOC_NO = " + pDocNo + "";
            System.out.println("STR SQL : " + strSql);
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery(strSql);
            resultSet.first();
            setAttribute("IED_DOC_NO", resultSet.getString("IED_DOC_NO"));
            setAttribute("REVISION_NO", resultSet.getString("REVISION_NO"));
            setAttribute("UPDATED_BY", resultSet.getString("UPDATED_BY"));
            setAttribute("IED_DOC_DATE", resultSet.getString("IED_DOC_DATE"));
            setAttribute("HIERARCHY_ID", resultSet.getInt("HIERARCHY_ID"));

            setAttribute("IED_DOC_NO", resultSet.getString("IED_DOC_NO"));
            setAttribute("IED_DOC_DATE", resultSet.getDate("IED_DOC_DATE"));
            setAttribute("FOR_MONTH", resultSet.getString("FOR_MONTH"));
            setAttribute("REMARK", resultSet.getString("REMARK"));

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
            System.out.println("SELECT * FROM SDMLATTPAY.INCREMENT_ENTRY_DETAIL_H WHERE IED_DOC_NO='" + productionDocumentNo + "'");
            rsTmp = stTmp.executeQuery("SELECT * FROM SDMLATTPAY.INCREMENT_ENTRY_DETAIL_H WHERE IED_DOC_NO='" + productionDocumentNo + "' GROUP BY IED_DOC_NO,REVISION_NO");
            while (rsTmp.next()) {
                clsIncrementProposal HISLIST = new clsIncrementProposal();

                HISLIST.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                HISLIST.setAttribute("UPDATED_BY", rsTmp.getString("UPDATED_BY"));
                HISLIST.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                HISLIST.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                HISLIST.setAttribute("APPROVER_REMARKS", rsTmp.getString("APPROVER_REMARKS"));
                HISLIST.setAttribute("FROM_IP", rsTmp.getString("FROM_IP"));

                hmHistoryList.put(Integer.toString(hmHistoryList.size() + 1), HISLIST);
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
            resultSet = statement.executeQuery("SELECT * FROM SDMLATTPAY.INCREMENT_ENTRY_DETAIL_H WHERE IED_DOC_NO ='" + pDocNo + "'");
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
                strSQL = "SELECT DISTINCT IED_DOC_NO,D_COM_DOC_DATA.DOC_DATE,"
                        + "CASE WHEN IED_DOC_NO LIKE '%HOD' THEN 'HOD' ELSE IED_DEPARTMENT END AS IED_DEPARTMENT "
                        + " FROM SDMLATTPAY.INCREMENT_ENTRY_DETAIL, SDMLATTPAY.D_COM_DOC_DATA   WHERE IED_DOC_NO=D_COM_DOC_DATA.DOC_NO AND STATUS='W' AND MODULE_ID=" + ModuleID + " AND CANCELED=0  AND USER_ID=" + pUserID + " ORDER BY RECEIVED_DATE,IED_DOC_NO";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT IED_DOC_NO,D_COM_DOC_DATA.DOC_DATE,"
                        + "CASE WHEN IED_DOC_NO LIKE '%HOD' THEN 'HOD' ELSE IED_DEPARTMENT END AS IED_DEPARTMENT "
                        + " FROM SDMLATTPAY.INCREMENT_ENTRY_DETAIL, SDMLATTPAY.D_COM_DOC_DATA   WHERE IED_DOC_NO=D_COM_DOC_DATA.DOC_NO AND STATUS='W' AND MODULE_ID=" + ModuleID + " AND CANCELED=0  AND USER_ID=" + pUserID + " ORDER BY RECEIVED_DATE,IED_DOC_NO";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT IED_DOC_NO,D_COM_DOC_DATA.DOC_DATE,"
                        + "CASE WHEN IED_DOC_NO LIKE '%HOD' THEN 'HOD' ELSE IED_DEPARTMENT END AS IED_DEPARTMENT "
                        + " FROM SDMLATTPAY.INCREMENT_ENTRY_DETAIL, SDMLATTPAY.D_COM_DOC_DATA   WHERE IED_DOC_NO=D_COM_DOC_DATA.DOC_NO AND STATUS='W' AND MODULE_ID=" + ModuleID + " AND CANCELED=0  AND USER_ID=" + pUserID + "  ORDER BY IED_DOC_NO";
            }
            System.out.println("Pending Doc : " + strSQL);
            ResultSet rsTmp = data.getConn().createStatement().executeQuery(strSQL);

            Counter = 0;
            while (rsTmp.next()) {

                Counter = Counter + 1;
                clsIncrementProposal ObjDoc = new clsIncrementProposal();

                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO", rsTmp.getString("IED_DOC_NO"));
                ObjDoc.setAttribute("DOC_DATE", rsTmp.getString("DOC_DATE"));
                ObjDoc.setAttribute("IED_DEPARTMENT", rsTmp.getString("IED_DEPARTMENT"));

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
            System.out.println("SELECT IED_DOC_NO FROM SDMLATTPAY.INCREMENT_ENTRY_DETAIL WHERE IED_DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");
            rsTmp = stTmp.executeQuery("SELECT IED_DOC_NO FROM SDMLATTPAY.INCREMENT_ENTRY_DETAIL WHERE IED_DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");

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
                rsTmp = data.getResult("SELECT APPROVED FROM SDMLATTPAY.INCREMENT_ENTRY_DETAIL WHERE IED_DOC_NO='" + pAmendNo + "' ");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    Approved = rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();

                if (!Approved) {
                    data.Execute("DELETE FROM SDMLATTPAY.D_COM_DOC_DATA WHERE DOC_NO='" + pAmendNo + "' AND MODULE_ID=849");
                }
                data.Execute("UPDATE SDMLATTPAY.INCREMENT_ENTRY_DETAIL SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE IED_DOC_NO='" + pAmendNo + "'");
                
            } catch (Exception e) {

            }
        }

    }
}
