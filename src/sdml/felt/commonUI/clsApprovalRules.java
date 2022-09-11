/*
 * clsMaterialRequisition.java
 *
 * Created on April 19, 2004, 2:35 PM
 */
package sdml.felt.commonUI;

import java.util.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
//import RS.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
//import RS.Finance.*;

public class clsApprovalRules {

    public String LastError = "";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;

    private HashMap props;

    public boolean Ready = false;

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
     * Creates new clsMaterialRequisition
     */
    public clsApprovalRules() {
        LastError = "";
        props = new HashMap();

        props.put("COMPANY_ID", new Variant(SDMLERPGLOBAL.gCompanyID));
        props.put("SR_NO", new Variant(0));
        props.put("MODULE_ID", new Variant(0));
        props.put("RULE_TYPE", new Variant(""));
        props.put("RULE", new Variant(""));
        props.put("RULE_PARAMETER", new Variant(""));
        props.put("RULE_OUTCOME", new Variant(""));
        props.put("CHANGED", new Variant(0));
        props.put("CHANGED_DATE", new Variant(""));
        props.put("CREATED_BY", new Variant(""));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(""));
        props.put("MODIFIED_DATE", new Variant(""));

    }

    public boolean LoadData(long pCompanyID) {
        Ready = false;
        try {
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery("SELECT * FROM D_COM_APPROVAL_RULES WHERE COMPANY_ID=" + pCompanyID + " ORDER BY SR_NO ");
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
        try {

            //Conn.setAutoCommit(false);
            if (!Validate()) {
                return false;
            }

            rsResultSet.moveToInsertRow();
            rsResultSet.updateInt("COMPANY_ID", SDMLERPGLOBAL.gCompanyID);
            rsResultSet.updateInt("MODULE_ID", getAttribute("MODULE_ID").getInt());
            rsResultSet.updateString("RULE_TYPE", getAttribute("RULE_TYPE").getString());
            rsResultSet.updateString("RULE", getAttribute("RULE").getString());
            rsResultSet.updateString("RULE_PARAMETER", getAttribute("RULE_PARAMETER").getString());
            rsResultSet.updateString("RULE_OUTCOME", getAttribute("RULE_OUTCOME").getString());
            rsResultSet.updateInt("CHANGED", 1);
            rsResultSet.updateString("CHANGED_DATE", SDMLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("CREATED_BY", SDMLERPGLOBAL.gLoginID);
            rsResultSet.updateString("CREATED_DATE", SDMLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("MODIFIED_BY", SDMLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE", SDMLERPGLOBAL.getCurrentDateDB());
            rsResultSet.insertRow();

            //Conn.commit();
            //Conn.setAutoCommit(true);
            MoveLast();
            return true;
        } catch (Exception e) {
            try {
                //Conn.rollback();
                //Conn.setAutoCommit(true);
            } catch (Exception c) {
            }

            LastError = e.getMessage();
            return false;
        }
    }

    //Updates current record
    public boolean Update() {
        try {

            //Conn.setAutoCommit(false);
            if (!Validate()) {
                return false;
            }

            rsResultSet.updateInt("MODULE_ID", getAttribute("MODULE_ID").getInt());
            rsResultSet.updateString("RULE_TYPE", getAttribute("RULE_TYPE").getString());
            rsResultSet.updateString("RULE", getAttribute("RULE").getString());
            rsResultSet.updateString("RULE_PARAMETER", getAttribute("RULE_PARAMETER").getString());
            rsResultSet.updateString("RULE_OUTCOME", getAttribute("RULE_OUTCOME").getString());
            rsResultSet.updateInt("CHANGED", 1);
            rsResultSet.updateString("CHANGED_DATE", SDMLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("CREATED_BY", SDMLERPGLOBAL.gLoginID);
            rsResultSet.updateString("CREATED_DATE", SDMLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("MODIFIED_BY", SDMLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE", SDMLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateRow();

            //Conn.commit();
            //Conn.setAutoCommit(true);
            MoveLast();
            return true;
        } catch (Exception e) {
            try {
                //Conn.rollback();
                //Conn.setAutoCommit(true);
            } catch (Exception c) {
            }
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean CanDelete(int pCompanyID, int SrNo) {
        return true;
    }

    public boolean Delete(long SrNo) {
        try {
            data.Execute("DELETE FROM D_COM_APPROVAL_RULES WHERE SR_NO=" + SrNo);
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean Filter(String pCondition, int pCompanyID) {
        Ready = false;
        try {

            String strSql = "SELECT * FROM D_COM_APPROVAL_RULES " + pCondition;
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery(strSql);

            if (!rsResultSet.first()) {
                strSql = "SELECT * FROM D_COM_APPROVAL_RULES WHERE COMPANY_ID=" + pCompanyID + " ORDER BY SR_NO";
                rsResultSet = Stmt.executeQuery(strSql);
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

    public boolean setData() {

        try {

            setAttribute("COMPANY_ID", UtilFunctions.getInt(rsResultSet, "COMPANY_ID", 0));
            setAttribute("SR_NO", UtilFunctions.getLong(rsResultSet, "SR_NO", 0));
            setAttribute("MODULE_ID", UtilFunctions.getInt(rsResultSet, "MODULE_ID", 0));
            setAttribute("RULE_TYPE", UtilFunctions.getString(rsResultSet, "RULE_TYPE", ""));
            setAttribute("RULE", UtilFunctions.getString(rsResultSet, "RULE", ""));
            setAttribute("RULE_PARAMETER", UtilFunctions.getString(rsResultSet, "RULE_PARAMETER", ""));
            setAttribute("RULE_OUTCOME", UtilFunctions.getString(rsResultSet, "RULE_OUTCOME", ""));
            setAttribute("REMARKS", UtilFunctions.getString(rsResultSet, "REMARKS", ""));
            setAttribute("CHANGED", UtilFunctions.getInt(rsResultSet, "CHANGED", 0));
            setAttribute("CHANGED_DATE", UtilFunctions.getString(rsResultSet, "CHANGED_DATE","0000-00-00"));
            setAttribute("CREATED_BY", UtilFunctions.getString(rsResultSet, "CREATED_BY", ""));
            setAttribute("CREATED_DATE", UtilFunctions.getString(rsResultSet, "CREATED_DATE","0000-00-00"));
            setAttribute("MODIFIED_BY", UtilFunctions.getString(rsResultSet, "MODIFIED_BY", ""));
            setAttribute("MODIFIED_DATE", UtilFunctions.getString(rsResultSet, "MODIFIED_DATE","0000-00-00"));

            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }

    }

    private boolean Validate() {
        if (getAttribute("RULE_TYPE").getString().trim().equals("")) {
            LastError = "Please specify rule type";
            return false;
        }

        if (getAttribute("RULE").getString().trim().equals("")) {
            LastError = "Please specify the rule";
            return false;
        }

        if (getAttribute("RULE_OUTCOME").getString().trim().equals("")) {
            LastError = "Please specify the result returned by rule";
            return false;
        }
        return true;
    }

    public static HashMap getApprovalRules(int CompanyID, int ModuleID, String RuleType, String Rule, String Parameter) {
        ResultSet rsTmp;
        HashMap List = new HashMap();

        try {
            rsTmp = data.getResult("SELECT * FROM D_COM_APPROVAL_RULES WHERE COMPANY_ID=" + CompanyID + " AND MODULE_ID=" + ModuleID + " AND RULE_TYPE='" + RuleType + "' AND RULE LIKE '%" + Rule + "%' AND RULE_PARAMETER = '" + Parameter + "'");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                while (!rsTmp.isAfterLast()) {
                    clsApprovalRules objRule = new clsApprovalRules();

                    objRule.setAttribute("COMPANY_ID", UtilFunctions.getInt(rsTmp, "COMPANY_ID", 0));
                    objRule.setAttribute("SR_NO", UtilFunctions.getLong(rsTmp, "SR_NO", 0));
                    objRule.setAttribute("MODULE_ID", UtilFunctions.getInt(rsTmp, "MODULE_ID", 0));
                    objRule.setAttribute("RULE_TYPE", UtilFunctions.getString(rsTmp, "RULE_TYPE", ""));
                    objRule.setAttribute("RULE", UtilFunctions.getString(rsTmp, "RULE", ""));
                    objRule.setAttribute("RULE_PARAMETER", UtilFunctions.getString(rsTmp, "RULE_PARAMETER", ""));
                    objRule.setAttribute("RULE_OUTCOME", UtilFunctions.getString(rsTmp, "RULE_OUTCOME", ""));

                    List.put(Integer.toString(List.size() + 1), objRule);

                    rsTmp.next();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List;
    }

    public static HashMap getApprovalRules(int CompanyID, int ModuleID, String RuleType, String Rule, String Parameter, String dbURL) {
        ResultSet rsTmp;
        HashMap List = new HashMap();

        try {
            rsTmp = data.getResult("SELECT * FROM D_COM_APPROVAL_RULES WHERE COMPANY_ID=" + CompanyID + " AND MODULE_ID=" + ModuleID + " AND RULE_TYPE='" + RuleType + "' AND RULE LIKE '%" + Rule + "%' AND RULE_PARAMETER = '" + Parameter + "'", dbURL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                while (!rsTmp.isAfterLast()) {
                    clsApprovalRules objRule = new clsApprovalRules();

                    objRule.setAttribute("COMPANY_ID", UtilFunctions.getInt(rsTmp, "COMPANY_ID", 0));
                    objRule.setAttribute("SR_NO", UtilFunctions.getLong(rsTmp, "SR_NO", 0));
                    objRule.setAttribute("MODULE_ID", UtilFunctions.getInt(rsTmp, "MODULE_ID", 0));
                    objRule.setAttribute("RULE_TYPE", UtilFunctions.getString(rsTmp, "RULE_TYPE", ""));
                    objRule.setAttribute("RULE", UtilFunctions.getString(rsTmp, "RULE", ""));
                    objRule.setAttribute("RULE_PARAMETER", UtilFunctions.getString(rsTmp, "RULE_PARAMETER", ""));
                    objRule.setAttribute("RULE_OUTCOME", UtilFunctions.getString(rsTmp, "RULE_OUTCOME", ""));

                    List.put(Integer.toString(List.size() + 1), objRule);

                    rsTmp.next();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return List;
    }
}
