/*
 * clsHierarchy.java
 *
 * Created on April 5, 2004, 3:02 PM
 */
package sdml.felt.commonUI;

import java.util.*;
import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author nhpatel
 * @version
 */
public class clsHierarchy {

    public String LastError = "";
    private ResultSet rsHierarchy;
    private Connection Conn;
    private Statement Stmt;

    //Hierarchy Rights Collection
    public HashMap colRights;

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
     * Creates new clsHierarchy
     */
    public clsHierarchy() {
        props = new HashMap();
        props.put("COMPANY_ID", new Variant(0));
        props.put("MODULE_ID", new Variant(0));
        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));

        props.put("IS_DEFAULT", new Variant(false));
        props.put("HIERARCHY_NAME", new Variant(""));
        //Create a new object for rights collection
        colRights = new HashMap();
    }

    public boolean LoadData() {
        Ready = false;
        try {
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHierarchy = Stmt.executeQuery("SELECT * FROM D_COM_HIERARCHY WHERE COMPANY_ID=" + Long.toString(SDMLERPGLOBAL.gCompanyID));
            System.out.println("SELECT * FROM D_COM_HIERARCHY WHERE COMPANY_ID=" + Long.toString(SDMLERPGLOBAL.gCompanyID));
            Ready = true;
            MoveFirst();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public boolean LoadData(String pCondition) {
        Ready = false;
        try {
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHierarchy = Stmt.executeQuery("SELECT * FROM D_COM_HIERARCHY WHERE "+pCondition+" AND COMPANY_ID=" + Long.toString(SDMLERPGLOBAL.gCompanyID));
            //System.out.println("SELECT * FROM D_COM_HIERARCHY WHERE "+pCondition+" AND COMPANY_ID=" + Long.toString(SDMLERPGLOBAL.gCompanyID));
            Ready = true;
            MoveFirst();
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
            rsHierarchy.close();
        } catch (Exception e) {

        }

    }

    //Navigation Methods
    public boolean MoveFirst() {
        try {
            rsHierarchy.first();
            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean MoveNext() {
        try {
            if (rsHierarchy.isAfterLast() || rsHierarchy.isLast()) {
                //Move pointer at last record
                //If it is beyond eof
                rsHierarchy.last();
            } else {
                rsHierarchy.next();
                if (rsHierarchy.isAfterLast()) {
                    rsHierarchy.last();
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
            if (rsHierarchy.isFirst() || rsHierarchy.isBeforeFirst()) {
                rsHierarchy.first();
            } else {
                rsHierarchy.previous();
                if (rsHierarchy.isBeforeFirst()) {
                    rsHierarchy.first();
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
            rsHierarchy.last();
            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean Insert() {
        try {
            //get Current set Company ID
            ResultSet rsCount;
            Statement stCount = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            Statement stFA;
            ResultSet rsFA;

            rsHierarchy.first();
            setAttribute("HIERARCHY_ID", data.getMaxID(SDMLERPGLOBAL.gCompanyID, "D_COM_HIERARCHY", "HIERARCHY_ID"));
            setAttribute("COMPANY_ID", SDMLERPGLOBAL.gCompanyID);

            rsHierarchy.moveToInsertRow();
            rsHierarchy.updateLong("COMPANY_ID", (long) getAttribute("COMPANY_ID").getVal());
            rsHierarchy.updateLong("HIERARCHY_ID", (long) getAttribute("HIERARCHY_ID").getVal());
            rsHierarchy.updateLong("MODULE_ID", (long) getAttribute("MODULE_ID").getVal());
            rsHierarchy.updateLong("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
            rsHierarchy.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
            rsHierarchy.updateLong("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
            rsHierarchy.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
            rsHierarchy.updateBoolean("IS_DEFAULT", getAttribute("IS_DEFAULT").getBool());
            rsHierarchy.updateString("HIERARCHY_NAME", (String) getAttribute("HIERARCHY_NAME").getObj());
            rsHierarchy.updateBoolean("CHANGED", true);
            rsHierarchy.updateString("CHANGED_DATE", SDMLERPGLOBAL.getCurrentDateDB());
            rsHierarchy.insertRow();

            ResultSet rsTmp;
            Statement tmpStmt;

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery("SELECT * FROM D_COM_HIERARCHY_RIGHTS WHERE COMPANY_ID=" + Long.toString(SDMLERPGLOBAL.gCompanyID));
            long nHierarchyID = (long) getAttribute("HIERARCHY_ID").getVal();
            long nCompanyID = (long) getAttribute("COMPANY_ID").getVal();

            stFA = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsFA = stFA.executeQuery("SELECT * FROM D_COM_FIELD_ACCESS");

            //Now Insert records into detail table
            for (int i = 1; i <= colRights.size(); i++) {
                clsHierarchyUsers ObjRights = (clsHierarchyUsers) colRights.get(Integer.toString(i));

                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID", nCompanyID);
                rsTmp.updateLong("HIERARCHY_ID", nHierarchyID);
                rsTmp.updateLong("USER_ID", (long) ObjRights.getAttribute("USER_ID").getVal());
                rsTmp.updateLong("SR_NO", i);
                rsTmp.updateBoolean("APPROVER", (boolean) ObjRights.getAttribute("APPROVER").getBool());
                rsTmp.updateBoolean("FINAL_APPROVER", (boolean) ObjRights.getAttribute("FINAL_APPROVER").getBool());
                rsTmp.updateBoolean("CREATOR", (boolean) ObjRights.getAttribute("CREATOR").getBool());
                rsTmp.updateLong("APPROVAL_SEQUENCE", i);
                rsTmp.updateBoolean("SKIP_SEQUENCE", (boolean) ObjRights.getAttribute("SKIP_SEQUENCE").getBool());
                rsTmp.updateBoolean("GRANT_OTHER", (boolean) ObjRights.getAttribute("GRANT_OTHER").getBool());
               
                if (ObjRights.getAttribute("FROM_DATE").getObj().toString().equals("")) {
                    rsTmp.updateString("FROM_DATE", null);
                } else {
                    rsTmp.updateString("FROM_DATE", (String) ObjRights.getAttribute("FROM_DATE").getObj());
                }
                if (ObjRights.getAttribute("TO_DATE").getObj().toString().equals("")) {
                    rsTmp.updateString("TO_DATE", null);
                } else {
                    rsTmp.updateString("TO_DATE", (String) ObjRights.getAttribute("TO_DATE").getObj());
                }
                //rsTmp.updateString("FROM_DATE", (String) ObjRights.getAttribute("FROM_DATE").getObj());
                //rsTmp.updateString("TO_DATE", (String) ObjRights.getAttribute("TO_DATE").getObj());
                rsTmp.updateLong("GRANT_USER_ID", (long) ObjRights.getAttribute("GRANT_USER_ID").getVal());
                rsTmp.updateBoolean("RESTORE", (boolean) ObjRights.getAttribute("RESTORE").getBool());
                rsTmp.updateLong("CREATED_BY", (long) ObjRights.getAttribute("CREATED_BY").getVal());
                
                if (ObjRights.getAttribute("CREATED_DATE").getObj().toString().equals("")) {
                    rsTmp.updateString("CREATED_DATE", null);
                } else {
                    rsTmp.updateString("CREATED_DATE", (String) ObjRights.getAttribute("CREATED_DATE").getObj());
                }
                //rsTmp.updateString("CREATED_DATE", (String) ObjRights.getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY", (long) ObjRights.getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE", (String) ObjRights.getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED", true);
                
                if (ObjRights.getAttribute("MODIFIED_DATE").getObj().toString().equals("")) {
                    rsTmp.updateString("MODIFIED_DATE", null);
                } else {
                    rsTmp.updateString("MODIFIED_DATE", (String) ObjRights.getAttribute("MODIFIED_DATE").getObj());
                }
                //rsTmp.updateString("CHANGED_DATE", SDMLERPGLOBAL.getCurrentDateDB());
                rsTmp.insertRow();

                for (int c = 1; c <= ObjRights.colFieldAccess.size(); c++) {
                    clsFieldAccess ObjFA = (clsFieldAccess) ObjRights.colFieldAccess.get(Integer.toString(c));

                    rsFA.moveToInsertRow();
                    rsFA.updateInt("COMPANY_ID", (int) nCompanyID);
                    rsFA.updateInt("HIERARCHY_ID", (int) nHierarchyID);
                    rsFA.updateInt("USER_ID", (int) ObjRights.getAttribute("USER_ID").getVal());
                    rsFA.updateInt("SR_NO", c);
                    rsFA.updateString("FIELD_TYPE", (String) ObjFA.getAttribute("FIELD_TYPE").getObj());
                    rsFA.updateString("FIELD_NAME", (String) ObjFA.getAttribute("FIELD_NAME").getObj());
                    rsFA.updateBoolean("CHANGED", true);
                    rsFA.updateString("CHANGED_DATE", SDMLERPGLOBAL.getCurrentDateDB());
                    rsFA.insertRow();
                }

            }

            MoveLast();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    //Updates current record
    public boolean Update() {
        try {
            //No Primary Keys will be updated
            //   rsHierarchy.updateLong("MODULE_ID",(long) getAttribute("MODULE_ID").getVal());
            rsHierarchy.updateString("HIERARCHY_NAME", (String) getAttribute("HIERARCHY_NAME").getObj());
            rsHierarchy.updateBoolean("IS_DEFAULT", getAttribute("IS_DEFAULT").getBool());
            rsHierarchy.updateLong("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
            rsHierarchy.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
            rsHierarchy.updateLong("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
            rsHierarchy.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
            rsHierarchy.updateBoolean("CHANGED", true);
            rsHierarchy.updateString("CHANGED_DATE", SDMLERPGLOBAL.getCurrentDateDB());
            rsHierarchy.updateRow();

            //First remove the old rows
            String mCompanyID = Long.toString((long) getAttribute("COMPANY_ID").getVal());
            //String mUserID=Long.toString((long)getAttribute("USER_ID").getVal());
            String mHierarchyID = Long.toString((long) getAttribute("HIERARCHY_ID").getVal());

            data.Execute("DELETE FROM D_COM_HIERARCHY_RIGHTS WHERE COMPANY_ID=" + mCompanyID + " AND HIERARCHY_ID=" + mHierarchyID);
            data.Execute("DELETE FROM D_COM_FIELD_ACCESS WHERE COMPANY_ID=" + mCompanyID + " AND HIERARCHY_ID=" + mHierarchyID);

            ResultSet rsTmp, rsFA;
            Statement tmpStmt, stFA;

            stFA = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsFA = stFA.executeQuery("SELECT * FROM D_COM_FIELD_ACCESS");

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery("SELECT * FROM D_COM_HIERARCHY_RIGHTS WHERE COMPANY_ID=" + Integer.toString(SDMLERPGLOBAL.gCompanyID));
            //long nUserID=(long)getAttribute("USER_ID").getVal();
            long nCompanyID = (long) getAttribute("COMPANY_ID").getVal();
            long nHierarchyID = (long) getAttribute("HIERARCHY_ID").getVal();

            //Now Insert records into detail table
            for (int i = 1; i <= colRights.size(); i++) {
                clsHierarchyUsers ObjRights = (clsHierarchyUsers) colRights.get(Integer.toString(i));
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID", SDMLERPGLOBAL.gCompanyID);
                rsTmp.updateLong("HIERARCHY_ID", nHierarchyID);
                rsTmp.updateLong("USER_ID", (long) ObjRights.getAttribute("USER_ID").getVal());
                rsTmp.updateLong("SR_NO", i);
                rsTmp.updateBoolean("APPROVER", (boolean) ObjRights.getAttribute("APPROVER").getBool());
                rsTmp.updateBoolean("FINAL_APPROVER", (boolean) ObjRights.getAttribute("FINAL_APPROVER").getBool());
                rsTmp.updateBoolean("CREATOR", (boolean) ObjRights.getAttribute("CREATOR").getBool());
                rsTmp.updateLong("APPROVAL_SEQUENCE", i);
                rsTmp.updateBoolean("SKIP_SEQUENCE", (boolean) ObjRights.getAttribute("SKIP_SEQUENCE").getBool());
                rsTmp.updateBoolean("GRANT_OTHER", (boolean) ObjRights.getAttribute("GRANT_OTHER").getBool());
                rsTmp.updateString("FROM_DATE", (String) ObjRights.getAttribute("FROM_DATE").getObj());
                rsTmp.updateString("TO_DATE", (String) ObjRights.getAttribute("TO_DATE").getObj());
                rsTmp.updateLong("GRANT_USER_ID", (long) ObjRights.getAttribute("GRANT_USER_ID").getVal());
                rsTmp.updateBoolean("RESTORE", (boolean) ObjRights.getAttribute("RESTORE").getBool());
                rsTmp.updateLong("CREATED_BY", (long) ObjRights.getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE", (String) ObjRights.getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY", (long) ObjRights.getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE", (String) ObjRights.getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED", true);
                rsTmp.updateString("CHANGED_DATE", (String) ObjRights.getAttribute("CHANGED_DATE").getObj());
                System.out.println("No Error in Query 1");
                rsTmp.insertRow();
                System.out.println("No Error in Query 2");
                
                for (int c = 1; c <= ObjRights.colFieldAccess.size(); c++) {
                    clsFieldAccess ObjFA = (clsFieldAccess) ObjRights.colFieldAccess.get(Integer.toString(c));

                    rsFA.moveToInsertRow();
                    rsFA.updateInt("COMPANY_ID", SDMLERPGLOBAL.gCompanyID);
                    rsFA.updateInt("HIERARCHY_ID", (int) nHierarchyID);
                    rsFA.updateInt("USER_ID", (int) ObjRights.getAttribute("USER_ID").getVal());
                    rsFA.updateInt("SR_NO", c);
                    rsFA.updateString("FIELD_TYPE", (String) ObjFA.getAttribute("FIELD_TYPE").getObj());
                    rsFA.updateString("FIELD_NAME", (String) ObjFA.getAttribute("FIELD_NAME").getObj());
                    rsFA.updateBoolean("CHANGED", true);
                    rsFA.updateString("CHANGED_DATE", SDMLERPGLOBAL.getCurrentDateDB());
                    
                    rsFA.insertRow();
                }

            }

            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error Dsate ,= "+e.getLocalizedMessage());
            LastError = e.getMessage();
            return false;
        }
    }

    //Deletes current record
    public boolean Delete() {
        try {
            String lCompanyID = Long.toString((long) getAttribute("COMPANY_ID").getVal());
            String lHierarchyID = Long.toString((long) getAttribute("HIERARCHY_ID").getVal());

            String strQry = "DELETE FROM D_COM_HIERARCHY WHERE COMPANY_ID=" + lCompanyID + " AND HIERARCHY_ID=" + lHierarchyID;
            data.Execute(strQry);
            strQry = "DELETE FROM D_COM_HIERARCHY_RIGHTS WHERE COMPANY_ID=" + lCompanyID + " AND HIERARCHY_ID=" + lHierarchyID;
            data.Execute(strQry);
            strQry = "DELETE FROM D_COM_FIELD_ACCESS WHERE COMPANY_ID=" + lCompanyID + " AND HIERARCHY_ID=" + lHierarchyID;
            data.Execute(strQry);

            //rsHierarchy.refreshRow();
            return MoveLast();
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public Object getObject1(long pCompanyID, long pHierarchyID) {
        //String strCondition = " WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND HIERARCHY_ID=" + Long.toString(pHierarchyID);
        clsHierarchyUsers ObjHierarchy = new clsHierarchyUsers();
        //   ObjHierarchy.Filter(strCondition);//
        return ObjHierarchy;
    }

    public Object getObjectEx(long pCompanyID, long pHierarchyID) {
        String strCondition = " WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND HIERARCHY_ID=" + Long.toString(pHierarchyID);
        clsHierarchy ObjHierarchy = new clsHierarchy();
        System.out.println("Current Hierarchy Getting : "+strCondition);
        ObjHierarchy.Filter(strCondition);//
        return ObjHierarchy;
    }

    public static HashMap getList(String pCondition) {
        Connection tmpConn;
        ResultSet rsTmp = null, rsFA = null;
        Statement tmpStmt = null, stFA = null;

        ResultSet rsTmp2 = null;
        Statement tmpStmt2 = null;

        HashMap List = new HashMap();
        long Counter = 0;
        long Counter2 = 0;

        try {
            tmpConn = data.getConn();
            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery("SELECT * FROM DINESHMILLS.D_COM_HIERARCHY " + pCondition + " ORDER BY MODULE_ID");

            rsTmp.first();
            Counter = 0;
            while (!rsTmp.isAfterLast()) {
                Counter = Counter + 1;
                clsHierarchy ObjHierarchy = new clsHierarchy();

                //Populate the user
                ObjHierarchy.setAttribute("COMPANY_ID", rsTmp.getLong("COMPANY_ID"));
                ObjHierarchy.setAttribute("HIERARCHY_ID", rsTmp.getLong("HIERARCHY_ID"));
                ObjHierarchy.setAttribute("HIERARCHY_NAME", rsTmp.getString("HIERARCHY_NAME"));
                ObjHierarchy.setAttribute("MODULE_ID", rsTmp.getLong("MODULE_ID"));
                ObjHierarchy.setAttribute("IS_DEFAULT", rsTmp.getBoolean("IS_DEFAULT"));
                ObjHierarchy.setAttribute("CREATED_BY", rsTmp.getLong("CREATED_BY"));
                ObjHierarchy.setAttribute("CREATED_DATE", rsTmp.getString("CREATED_DATE"));
                ObjHierarchy.setAttribute("MODIFIED_BY", rsTmp.getLong("MODIFIED_BY"));
                ObjHierarchy.setAttribute("MODIFIED_DATE", rsTmp.getString("MODIFIED_DATE"));

                //Now Populate the collection
                //first clear the collection
                ObjHierarchy.colRights.clear();

                String mCompanyID = Long.toString((long) ObjHierarchy.getAttribute("COMPANY_ID").getVal());
                String mHierarchyID = Long.toString((long) ObjHierarchy.getAttribute("HIERARCHY_ID").getVal());

                tmpStmt2 = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                rsTmp2 = tmpStmt2.executeQuery("SELECT * FROM DINESHMILLS.D_COM_HIERARCHY_RIGHTS WHERE COMPANY_ID=" + mCompanyID + " AND HIERARCHY_ID=" + mHierarchyID + " ORDER BY APPROVAL_SEQUENCE");

                Counter2 = 0;
                while (rsTmp2.next()) {
                    Counter2 = Counter2 + 1;
                    clsHierarchyUsers ObjRights = new clsHierarchyUsers();

                    ObjRights.setAttribute("COMPANY_ID", rsTmp2.getLong("COMPANY_ID"));
                    ObjRights.setAttribute("USER_ID", rsTmp2.getLong("USER_ID"));
                    ObjRights.setAttribute("SR_NO", rsTmp2.getLong("SR_NO"));
                    ObjRights.setAttribute("HIERARCHY_ID", rsTmp2.getLong("HIERARCHY_ID"));
                    ObjRights.setAttribute("APPROVER", rsTmp2.getBoolean("APPROVER"));
                    System.out.println("Approver = "+ObjRights.getAttribute("APPROVER").getBool()+", HIERARCHY_ID  "+ObjRights.getAttribute("HIERARCHY_ID").getVal()+" , SR_NO : "+ObjRights.getAttribute("SR_NO").getVal()+" , USER_ID : "+ObjRights.getAttribute("USER_ID").getVal());
                    ObjRights.setAttribute("FINAL_APPROVER", rsTmp2.getBoolean("FINAL_APPROVER"));
                    ObjRights.setAttribute("CREATOR", rsTmp2.getBoolean("CREATOR"));
                    ObjRights.setAttribute("APPROVAL_SEQUENCE", rsTmp2.getLong("APPROVAL_SEQUENCE"));
                    ObjRights.setAttribute("SKIP_SEQUENCE", rsTmp2.getBoolean("SKIP_SEQUENCE"));
                    ObjRights.setAttribute("GRANT_OTHER", rsTmp2.getBoolean("GRANT_OTHER"));
                    ObjRights.setAttribute("FROM_DATE", rsTmp2.getString("FROM_DATE"));
                    ObjRights.setAttribute("TO_DATE", rsTmp2.getString("TO_DATE"));
                    ObjRights.setAttribute("GRANT_USER_ID", rsTmp2.getLong("GRANT_USER_ID"));
                    ObjRights.setAttribute("RESTORE", rsTmp2.getBoolean("RESTORE"));
                    ObjRights.setAttribute("CREATED_BY", rsTmp2.getLong("CREATED_BY"));
                    ObjRights.setAttribute("CREATED_DATE", rsTmp2.getString("CREATED_DATE"));
                    ObjRights.setAttribute("MODIFIED_BY", rsTmp2.getLong("MODIFIED_BY"));
                    ObjRights.setAttribute("MODIFIED_DATE", rsTmp2.getString("MODIFIED_DATE"));

                    stFA = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    rsFA = stFA.executeQuery("SELECT * FROM D_COM_FIELD_ACCESS WHERE COMPANY_ID=" + rsTmp2.getInt("COMPANY_ID") + " AND HIERARCHY_ID=" + rsTmp2.getInt("HIERARCHY_ID") + " AND USER_ID=" + rsTmp2.getInt("USER_ID"));
                    rsFA.first();

                    if (rsFA.getRow() > 0) {
                        while (!rsFA.isAfterLast()) {
                            clsFieldAccess ObjFA = new clsFieldAccess();

                            ObjFA.setAttribute("COMPANY_ID", rsFA.getInt("COMPANY_ID"));
                            ObjFA.setAttribute("HIERARCHY_ID", rsFA.getInt("HIERARCHY_ID"));
                            ObjFA.setAttribute("USER_ID", rsFA.getInt("USER_ID"));
                            ObjFA.setAttribute("SR_NO", rsFA.getInt("SR_NO"));
                            ObjFA.setAttribute("FIELD_TYPE", rsFA.getString("FIELD_TYPE"));
                            ObjFA.setAttribute("FIELD_NAME", rsFA.getString("FIELD_NAME"));

                            ObjRights.colFieldAccess.put(Integer.toString(ObjRights.colFieldAccess.size() + 1), ObjFA);
                            rsFA.next();
                        }
                    }
                   // JOptionPane.showMessageDialog(null, "ObjRight User : "+ObjRights.getAttribute("USER_ID").getVal()+", Counter2 : "+Counter2);
                    ObjHierarchy.colRights.put(Long.toString(Counter2), ObjRights);
                }// Innser while

                //Put the prepared user object into list
               // JOptionPane.showMessageDialog(null, "ObjHierarchy Couter : "+Counter);
                List.put(Long.toString(Counter), ObjHierarchy);
                rsTmp.next();
            }//Out While

            //tmpConn.close();
            rsTmp.close();
            rsFA.close();
            tmpStmt.close();
            stFA.close();

            rsTmp2.close();
            tmpStmt2.close();

        } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return List;
    }

    public static HashMap getListEx(String pCondition) {
        Connection tmpConn;
        ResultSet rsTmp;
        Statement tmpStmt;

        ResultSet rsTmp2 = null;
        Statement tmpStmt2 = null;

        HashMap List = new HashMap();
        long Counter = 0;
        long Counter2 = 0;
        String strSQL = "";

        try {
            tmpConn = data.getConn();

            //First find the hierarchy starting with logged in user
            strSQL = "SELECT COUNT(*) AS THECOUNT FROM D_COM_HIERARCHY,D_COM_HIERARCHY_RIGHTS " + pCondition + " AND D_COM_HIERARCHY.COMPANY_ID=D_COM_HIERARCHY_RIGHTS.COMPANY_ID AND D_COM_HIERARCHY.HIERARCHY_ID=D_COM_HIERARCHY_RIGHTS.HIERARCHY_ID AND D_COM_HIERARCHY_RIGHTS.USER_ID=" + SDMLERPGLOBAL.gUserID + " AND SR_NO=1 ORDER BY MODULE_ID";
            System.out.println("SELECT COUNT(*) AS THECOUNT FROM D_COM_HIERARCHY,D_COM_HIERARCHY_RIGHTS " + pCondition + " AND D_COM_HIERARCHY.COMPANY_ID=D_COM_HIERARCHY_RIGHTS.COMPANY_ID AND D_COM_HIERARCHY.HIERARCHY_ID=D_COM_HIERARCHY_RIGHTS.HIERARCHY_ID AND D_COM_HIERARCHY_RIGHTS.USER_ID=" + SDMLERPGLOBAL.gUserID + " AND SR_NO=1 ORDER BY MODULE_ID");
            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("THECOUNT") >= 1) //Hierarchies found starting with logged in user
            {
                //Retain the Query as it is
                strSQL = "SELECT D_COM_HIERARCHY.*  FROM D_COM_HIERARCHY,D_COM_HIERARCHY_RIGHTS " + pCondition + " AND D_COM_HIERARCHY.COMPANY_ID=D_COM_HIERARCHY_RIGHTS.COMPANY_ID AND D_COM_HIERARCHY.HIERARCHY_ID=D_COM_HIERARCHY_RIGHTS.HIERARCHY_ID AND D_COM_HIERARCHY_RIGHTS.USER_ID=" + SDMLERPGLOBAL.gUserID + " AND SR_NO=1 ORDER BY IS_DEFAULT DESC";
            }

            if (rsTmp.getInt("THECOUNT") <= 0) //No hierarchy found starting with logged in user
            {
                return List;
                //Another check
               /*strSQL="SELECT COUNT(*) AS THECOUNT FROM D_COM_HIERARCHY,D_COM_HIERARCHY_RIGHTS "+pCondition+" AND D_COM_HIERARCHY.COMPANY_ID=D_COM_HIERARCHY_RIGHTS.COMPANY_ID AND D_COM_HIERARCHY.HIERARCHY_ID=D_COM_HIERARCHY_RIGHTS.HIERARCHY_ID AND D_COM_HIERARCHY_RIGHTS.USER_ID="+SDMLERPGLOBAL.gUserID+" ORDER BY MODULE_ID";
                 tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                 rsTmp=tmpStmt.executeQuery(strSQL);
                 rsTmp.first();
                
                 if(rsTmp.getInt("THECOUNT")<=0) //No Hierarchies found having logged in user anywhere in the sequence. Refuse...
                 {
                 return List;
                 }
                 strSQL="SELECT D_COM_HIERARCHY.*  FROM D_COM_HIERARCHY,D_COM_HIERARCHY_RIGHTS "+pCondition+" AND D_COM_HIERARCHY.COMPANY_ID=D_COM_HIERARCHY_RIGHTS.COMPANY_ID AND D_COM_HIERARCHY.HIERARCHY_ID=D_COM_HIERARCHY_RIGHTS.HIERARCHY_ID AND D_COM_HIERARCHY_RIGHTS.USER_ID="+SDMLERPGLOBAL.gUserID+" ORDER BY MODULE_ID";*/
            }

            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter = 0;
            while (!rsTmp.isAfterLast()) {
                Counter = Counter + 1;
                clsHierarchy ObjHierarchy = new clsHierarchy();

                //Populate the user
                ObjHierarchy.setAttribute("COMPANY_ID", rsTmp.getLong("COMPANY_ID"));
                ObjHierarchy.setAttribute("HIERARCHY_ID", rsTmp.getLong("HIERARCHY_ID"));
                ObjHierarchy.setAttribute("HIERARCHY_NAME", rsTmp.getString("HIERARCHY_NAME"));
                ObjHierarchy.setAttribute("MODULE_ID", rsTmp.getLong("MODULE_ID"));
                ObjHierarchy.setAttribute("IS_DEFAULT", rsTmp.getBoolean("IS_DEFAULT"));
                ObjHierarchy.setAttribute("CREATED_BY", rsTmp.getLong("CREATED_BY"));
                ObjHierarchy.setAttribute("CREATED_DATE", rsTmp.getString("CREATED_DATE"));
                ObjHierarchy.setAttribute("MODIFIED_BY", rsTmp.getLong("MODIFIED_BY"));
                ObjHierarchy.setAttribute("MODIFIED_DATE", rsTmp.getString("MODIFIED_DATE"));

                //Now Populate the collection
                //first clear the collection
                ObjHierarchy.colRights.clear();

                String mCompanyID = Long.toString((long) ObjHierarchy.getAttribute("COMPANY_ID").getVal());
                String mHierarchyID = Long.toString((long) ObjHierarchy.getAttribute("HIERARCHY_ID").getVal());

                tmpStmt2 = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                rsTmp2 = tmpStmt2.executeQuery("SELECT * FROM D_COM_HIERARCHY_RIGHTS WHERE COMPANY_ID=" + mCompanyID + " AND HIERARCHY_ID=" + mHierarchyID + " ORDER BY APPROVAL_SEQUENCE");

                Counter2 = 0;
                while (rsTmp2.next()) {
                    Counter2 = Counter2 + 1;
                    clsHierarchyUsers ObjRights = new clsHierarchyUsers();

                    ObjRights.setAttribute("COMPANY_ID", rsTmp2.getLong("COMPANY_ID"));
                    ObjRights.setAttribute("USER_ID", rsTmp2.getLong("USER_ID"));
                    ObjRights.setAttribute("SR_NO", rsTmp2.getLong("SR_NO"));
                    ObjRights.setAttribute("HIERARCHY_ID", rsTmp2.getLong("HIERARCHY_ID"));
                    ObjRights.setAttribute("APPROVER", rsTmp2.getBoolean("APPROVER"));
                    ObjRights.setAttribute("FINAL_APPROVER", rsTmp2.getBoolean("FINAL_APPROVER"));
                    ObjRights.setAttribute("CREATOR", rsTmp2.getBoolean("CREATOR"));
                    ObjRights.setAttribute("APPROVAL_SEQUENCE", rsTmp2.getLong("APPROVAL_SEQUENCE"));
                    ObjRights.setAttribute("SKIP_SEQUENCE", rsTmp2.getBoolean("SKIP_SEQUENCE"));
                    ObjRights.setAttribute("GRANT_OTHER", rsTmp2.getBoolean("GRANT_OTHER"));
                    ObjRights.setAttribute("FROM_DATE", rsTmp2.getString("FROM_DATE"));
                    ObjRights.setAttribute("TO_DATE", rsTmp2.getString("TO_DATE"));
                    ObjRights.setAttribute("GRANT_USER_ID", rsTmp2.getLong("GRANT_USER_ID"));
                    ObjRights.setAttribute("RESTORE", rsTmp2.getBoolean("RESTORE"));
                    ObjRights.setAttribute("CREATED_BY", rsTmp2.getLong("CREATED_BY"));
                    ObjRights.setAttribute("CREATED_DATE", rsTmp2.getString("CREATED_DATE"));
                    ObjRights.setAttribute("MODIFIED_BY", rsTmp2.getLong("MODIFIED_BY"));
                    ObjRights.setAttribute("MODIFIED_DATE", rsTmp2.getString("MODIFIED_DATE"));

                    ObjHierarchy.colRights.put(Long.toString(Counter2), ObjRights);
                }// Innser while

                //Put the prepared user object into list
                List.put(Long.toString(Counter), ObjHierarchy);
                rsTmp.next();
            }//Out While

            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();

            rsTmp2.close();
            tmpStmt2.close();

        } catch (Exception e) {

            //JOptionPane.showMessageDialog(null,e.getMessage());
        }

        return List;
    }

    public boolean Filter(String pCondition) {
        Ready = false;
        try {
            String strSql = "SELECT * FROM D_COM_HIERARCHY " + pCondition;
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHierarchy = Stmt.executeQuery(strSql);
            Ready = true;
            MoveFirst();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean Find(int pCompanyId, int pModuleID, boolean pDefault) {
        Ready = false;
        try {
            String strSql = "SELECT * FROM D_COM_HIERARCHY WHERE COMPANY_ID=" + Integer.toString(pCompanyId);
            ResultSet rsHierarchy1;
            Conn = data.getConn();
            Stmt = Conn.createStatement();
            rsHierarchy1 = Stmt.executeQuery(strSql);
            int Nos = 0;
            while (rsHierarchy1.next()) {
                int Module = (int) rsHierarchy1.getLong("MODULE_ID");
                boolean Default = (boolean) rsHierarchy1.getBoolean("IS_DEFAULT");

                if (Module == pModuleID) {
                    if (Default == pDefault) {
                        Nos = Nos + 1;
                        if (Nos > 1) {
                            Ready = true;
                            return true;
                        }
                    }
                }
            }
            Ready = false;
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    private boolean setData() {
        ResultSet rsTmp, rsFA;
        Connection tmpConn;
        Statement tmpStmt, stFA;

        tmpConn = data.getConn();

        HashMap List = new HashMap();
        long Counter = 0;

        try {
            setAttribute("COMPANY_ID", rsHierarchy.getLong("COMPANY_ID"));
            setAttribute("MODULE_ID", rsHierarchy.getLong("MODULE_ID"));
            setAttribute("IS_DEFAULT", rsHierarchy.getBoolean("IS_DEFAULT"));
            setAttribute("HIERARCHY_ID", rsHierarchy.getLong("HIERARCHY_ID"));
            setAttribute("HIERARCHY_NAME", rsHierarchy.getString("HIERARCHY_NAME"));
            setAttribute("CREATED_BY", rsHierarchy.getLong("CREATED_BY"));
            setAttribute("CREATED_DATE", rsHierarchy.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY", rsHierarchy.getLong("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE", rsHierarchy.getString("MODIFIED_DATE"));

            //Now Populate the collection
            //first clear the collection
            colRights.clear();

            String mCompanyID = Long.toString((long) getAttribute("COMPANY_ID").getVal());
            //     String mUserID=Long.toString((long) getAttribute("USER_ID").getVal());
            String mHierarchyID = Long.toString((long) getAttribute("HIERARCHY_ID").getVal());

            tmpStmt = tmpConn.createStatement();
            rsTmp = tmpStmt.executeQuery("SELECT * FROM D_COM_HIERARCHY_RIGHTS WHERE COMPANY_ID=" + mCompanyID + " AND HIERARCHY_ID=" + mHierarchyID + " ORDER BY SR_NO");

            Counter = 0;
            while (rsTmp.next()) {
                Counter = Counter + 1;
                clsHierarchyUsers ObjRights = new clsHierarchyUsers();

                long i = (long) rsTmp.getLong("USER_ID");
                ObjRights.setAttribute("COMPANY_ID", rsTmp.getLong("COMPANY_ID"));
                ObjRights.setAttribute("HIERARHY_ID", rsTmp.getLong("HIERARCHY_ID"));
                ObjRights.setAttribute("USER_ID", rsTmp.getLong("USER_ID"));
                ObjRights.setAttribute("SR_NO", rsTmp.getLong("SR_NO"));
                ObjRights.setAttribute("APPROVER", rsTmp.getBoolean("APPROVER"));
                ObjRights.setAttribute("FINAL_APPROVER", rsTmp.getBoolean("FINAL_APPROVER"));
                ObjRights.setAttribute("CREATOR", rsTmp.getBoolean("CREATOR"));
                ObjRights.setAttribute("APPROVAL_SEQUENCE", rsTmp.getLong("APPROVAL_SEQUENCE"));
                ObjRights.setAttribute("SKIP_SEQUENCE", rsTmp.getBoolean("SKIP_SEQUENCE"));
                ObjRights.setAttribute("GRANT_OTHER", rsTmp.getBoolean("GRANT_OTHER"));
                ObjRights.setAttribute("FROM_DATE", rsTmp.getString("FROM_DATE"));
                ObjRights.setAttribute("TO_DATE", rsTmp.getString("TO_DATE"));
                ObjRights.setAttribute("GRANT_USER_ID", rsTmp.getLong("GRANT_USER_ID"));
                ObjRights.setAttribute("RESTORE", rsTmp.getBoolean("RESTORE"));
                ObjRights.setAttribute("CREATED_BY", rsTmp.getLong("CREATED_BY"));
                ObjRights.setAttribute("CREATED_DATE", rsTmp.getString("CREATED_DATE"));
                ObjRights.setAttribute("MODIFIED_BY", rsTmp.getLong("MODIFIED_BY"));
                ObjRights.setAttribute("MODIFIED_DATE", rsTmp.getString("MODIFIED_DATE"));

                stFA = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rsFA = stFA.executeQuery("SELECT * FROM D_COM_FIELD_ACCESS WHERE COMPANY_ID=" + rsTmp.getInt("COMPANY_ID") + " AND HIERARCHY_ID=" + rsTmp.getInt("HIERARCHY_ID") + " AND USER_ID=" + rsTmp.getInt("USER_ID"));
                rsFA.first();

                if (rsFA.getRow() > 0) {
                    while (!rsFA.isAfterLast() && rsFA.getRow() > 0) {
                        clsFieldAccess ObjFA = new clsFieldAccess();

                        ObjFA.setAttribute("COMPANY_ID", rsFA.getInt("COMPANY_ID"));
                        ObjFA.setAttribute("HIERARCHY_ID", rsFA.getInt("HIERARCHY_ID"));
                        ObjFA.setAttribute("USER_ID", rsFA.getInt("USER_ID"));
                        ObjFA.setAttribute("SR_NO", rsFA.getInt("SR_NO"));
                        ObjFA.setAttribute("FIELD_TYPE", rsFA.getString("FIELD_TYPE"));
                        ObjFA.setAttribute("FIELD_NAME", rsFA.getString("FIELD_NAME"));

                        ObjRights.colFieldAccess.put(Integer.toString(ObjRights.colFieldAccess.size() + 1), ObjFA);
                        rsFA.next();
                    }
                }
               // JOptionPane.showMessageDialog(null, "Data Called : User : "+ObjRights.getAttribute("USER_ID").getVal());
                colRights.put(Long.toString(Counter), ObjRights);
            }
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
            return false;
        }
    }

    public static int getDefaultHierarchy(int pCompanyID) {
        Connection tmpConn;
        Statement tmpStmt;
        ResultSet rsTmp;

        try {
            tmpConn = data.getConn();
            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery("SELECT HIERARCHY_ID FROM D_COM_HIERARCHY WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND IS_DEFAULT=1");
            rsTmp.first();
            if (rsTmp.getRow() > 0) {
                //tmpConn.close();
                tmpStmt.close();
                rsTmp.close();

                return rsTmp.getInt("HIERARCHY_ID");
            } else {
                //tmpConn.close();
                tmpStmt.close();
                rsTmp.close();

                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public static boolean CanSkip(int pCompanyID, int pApprovalNo, int pUserID) {
        Connection tmpConn;
        ResultSet rsTmp;
        Statement tmpStmt;
        boolean blnCanSkip = false;

        try {
            tmpConn = data.getConn();
            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery("SELECT SKIP_SEQUENCE FROM D_COM_HIERARCHY_RIGHTS WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND HIERARCHY_ID=" + Integer.toString(pApprovalNo) + " AND USER_ID=" + Integer.toString(pUserID));
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                blnCanSkip = rsTmp.getBoolean("SKIP_SEQUENCE");
            }
            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();

            return blnCanSkip;
        } catch (Exception e) {
            return blnCanSkip;
        }
    }

    public static boolean CanFinalApprove(int pCompanyID, int pApprovalNo, int pUserID) {
        Connection tmpConn;
        ResultSet rsTmp;
        Statement tmpStmt;
        boolean blnCanFinal = false;

        try {
            tmpConn = data.getConn();
            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery("SELECT FINAL_APPROVER FROM D_COM_HIERARCHY_RIGHTS WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND HIERARCHY_ID=" + Integer.toString(pApprovalNo) + " AND USER_ID=" + Integer.toString(pUserID));
            System.out.println("SELECT FINAL_APPROVER FROM D_COM_HIERARCHY_RIGHTS WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND HIERARCHY_ID=" + Integer.toString(pApprovalNo) + " AND USER_ID=" + Integer.toString(pUserID));
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                blnCanFinal = rsTmp.getBoolean("FINAL_APPROVER");
            }

            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();

            return blnCanFinal;
        } catch (Exception e) {
            return blnCanFinal;
        }
    }

    public static HashMap getUserList(int pCompanyID, int pHierarchyID, int pCurUserID) {
        Connection tmpConn;
        Statement tmpStmt;
        ResultSet rsTmp;
        Statement tmpStmt2;
        ResultSet rsTmp2;

        HashMap List = new HashMap();
        int Counter = 0;
        int UserSrNo = 0;

        try {
            tmpConn = data.getConn();

            //First get the Sequence No. of current user
            tmpStmt2 = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp2 = tmpStmt2.executeQuery("SELECT APPROVAL_SEQUENCE FROM D_COM_HIERARCHY_RIGHTS WHERE COMPANY_ID=" + pCompanyID + " AND HIERARCHY_ID=" + pHierarchyID + " AND USER_ID=" + pCurUserID);
            rsTmp2.first();

            if (rsTmp2.getRow() > 0) {
                UserSrNo = rsTmp2.getInt("APPROVAL_SEQUENCE");
            }

            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery("SELECT * FROM D_COM_HIERARCHY_RIGHTS WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND HIERARCHY_ID=" + Integer.toString(pHierarchyID) + " AND APPROVAL_SEQUENCE>" + UserSrNo + " ORDER BY SR_NO");
            rsTmp.first();

            while (!rsTmp.isAfterLast()) {
                Counter = Counter + 1;

                clsUser ObjUser = new clsUser();

                //Check that Other user has granted the rights
                if (rsTmp.getBoolean("GRANT_OTHER")) {
                    //Get the Date
                    java.sql.Date FromDate = rsTmp.getDate("FROM_DATE");
                    java.sql.Date ToDate = rsTmp.getDate("TO_DATE");
                    java.sql.Date CurrentDate = java.sql.Date.valueOf(SDMLERPGLOBAL.getCurrentDateDB());

                    int UserID = 0;

                    if ((CurrentDate.after(FromDate) || CurrentDate.compareTo(FromDate) == 0) && (CurrentDate.before(ToDate) || CurrentDate.compareTo(ToDate) == 0)) {
                        UserID = rsTmp.getInt("GRANT_USER_ID");
                    } else {
                        UserID = rsTmp.getInt("USER_ID");
                    }

                    ObjUser.setAttribute("USER_ID", UserID);
                } else {
                    ObjUser.setAttribute("USER_ID", rsTmp.getInt("USER_ID"));
                }

                String lUserName = clsUser.getUserName(pCompanyID, (int) ObjUser.getAttribute("USER_ID").getVal());

                if (lUserName.trim().equals("")) {
                    ObjUser.setAttribute("USER_NAME", "To be Decided");
                } else {
                    ObjUser.setAttribute("USER_NAME", lUserName);
                }

                List.put(Integer.toString(Counter), ObjUser);
                rsTmp.next();
            }

            //tmpConn.close();
            tmpStmt.close();
            rsTmp.close();
            tmpStmt2.close();
            rsTmp2.close();

            return List;
        } catch (Exception e) {
            return List;
        }
    }

    public static HashMap getUserList(int pCompanyID, int pHierarchyID, int pCurUserID, boolean All) {
        Connection tmpConn;
        Statement tmpStmt;
        ResultSet rsTmp;
        Statement tmpStmt2;
        ResultSet rsTmp2;

        HashMap List = new HashMap();
        int Counter = 0;
        int UserSrNo = 0;

        try {
            tmpConn = data.getConn();

            //First get the Sequence No. of current user
            tmpStmt2 = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp2 = tmpStmt2.executeQuery("SELECT APPROVAL_SEQUENCE FROM D_COM_HIERARCHY_RIGHTS WHERE COMPANY_ID=" + pCompanyID + " AND HIERARCHY_ID=" + pHierarchyID + " AND USER_ID=" + pCurUserID);
            rsTmp2.first();

            if (rsTmp2.getRow() > 0) {
                UserSrNo = rsTmp2.getInt("APPROVAL_SEQUENCE");
            }

            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery("SELECT * FROM D_COM_HIERARCHY_RIGHTS WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND HIERARCHY_ID=" + Integer.toString(pHierarchyID) + " ORDER BY SR_NO");
            rsTmp.first();

            while (!rsTmp.isAfterLast()) {
                Counter = Counter + 1;

                clsUser ObjUser = new clsUser();

                //Check that Other user has granted the rights
                if (rsTmp.getBoolean("GRANT_OTHER")) {
                    //Get the Date

                    if (SDMLERPGLOBAL.isDate(rsTmp.getString("FROM_DATE")) && SDMLERPGLOBAL.isDate(rsTmp.getString("TO_DATE"))) {
                        java.sql.Date FromDate = rsTmp.getDate("FROM_DATE");
                        java.sql.Date ToDate = rsTmp.getDate("TO_DATE");
                        java.sql.Date CurrentDate = java.sql.Date.valueOf(SDMLERPGLOBAL.getCurrentDateDB());

                        int UserID = 0;

                        if ((CurrentDate.after(FromDate) || CurrentDate.compareTo(FromDate) == 0) && (CurrentDate.before(ToDate) || CurrentDate.compareTo(ToDate) == 0)) {
                            UserID = rsTmp.getInt("GRANT_USER_ID");
                        } else {
                            UserID = rsTmp.getInt("USER_ID");
                        }

                        ObjUser.setAttribute("USER_ID", UserID);
                    } else {
                        ObjUser.setAttribute("USER_ID", rsTmp.getInt("USER_ID"));
                    }
                } else {
                    ObjUser.setAttribute("USER_ID", rsTmp.getInt("USER_ID"));
                }

                String lUserName = clsUser.getUserName(pCompanyID, (int) ObjUser.getAttribute("USER_ID").getVal());

                if (lUserName.trim().equals("")) {
                    ObjUser.setAttribute("USER_NAME", "To be Decided");
                } else {
                    ObjUser.setAttribute("USER_NAME", lUserName);
                }

                List.put(Integer.toString(Counter), ObjUser);
                rsTmp.next();
            }

            //tmpConn.close();
            tmpStmt.close();
            rsTmp.close();
            tmpStmt2.close();
            rsTmp2.close();

            return List;
        } catch (Exception e) {
            return List;
        }
    }

    public static boolean CanEditField(int pCompanyID, int pUserID, int pHierarchyID, String pFieldType, String pFieldName) {
        //Hierarchy contains the Module ID. So we don't need to pass Module ID here.
        try {
            Connection tmpConn;
            Statement stTmp;
            ResultSet rsTmp;

            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = stTmp.executeQuery("SELECT * FROM D_COM_FIELD_ACCESS WHERE COMPANY_ID=" + pCompanyID + " AND HIERARCHY_ID=" + pHierarchyID + " AND USER_ID=" + pUserID + " AND FIELD_TYPE='" + pFieldType + "' AND FIELD_NAME='" + pFieldName + "'");
            rsTmp.first();
            if (rsTmp.getRow() > 0) {
                rsTmp.close();
                stTmp.close();
                //tmpConn.close();
                return true;
            } else {
                rsTmp.close();
                stTmp.close();
                //tmpConn.close();
                return false;
            }
        } catch (Exception e) {
            return false;
        }

    }

    public static boolean IsUserExist(int pCompanyID, int pHierarchyID, int pUserID) {
        ResultSet rsTmp;
        boolean IsExist = false;

        try {
            rsTmp = data.getResult("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE COMPANY_ID=" + pCompanyID + " AND HIERARCHY_ID=" + pHierarchyID + " AND USER_ID=" + pUserID);
            rsTmp.first();
            if (rsTmp.getRow() > 0) {
                IsExist = true;
            }
            rsTmp.close();

        } catch (Exception e) {

        }
        return IsExist;

    }

    public static int getFinalApprover(int pCompanyID, int pHierarchyID) {
        ResultSet rsTmp = null;
        int FinalApprover = 0;

        try {
            rsTmp = data.getResult("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE COMPANY_ID=" + pCompanyID + " AND HIERARCHY_ID=" + pHierarchyID + " AND FINAL_APPROVER=1");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                FinalApprover = rsTmp.getInt("USER_ID");
            }

        } catch (Exception e) {

        }

        return FinalApprover;
    }

    public static int getCreatorID(int pCompanyID, int pHierarchyID) {
        ResultSet rsTmp = null;
        int Creator = 0;

        try {
            rsTmp = data.getResult("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE COMPANY_ID=" + pCompanyID + " AND HIERARCHY_ID=" + pHierarchyID + " AND APPROVAL_SEQUENCE=1");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                Creator = rsTmp.getInt("USER_ID");
            }

        } catch (Exception e) {

        }

        return Creator;
    }

    public static boolean canSendDirectly(int pCompanyID, int pHierarchyID, int pUserID, String pDocNo) {
        ResultSet rsTmp;
        boolean canSend = false;

        try {

            int ModuleID = 0;

            //Get the module ID
            rsTmp = data.getResult("SELECT MODULE_ID FROM D_COM_HIERARCHY WHERE HIERARCHY_ID=" + pHierarchyID + " AND COMPANY_ID=" + pCompanyID);
            rsTmp.first();
            if (rsTmp.getRow() > 0) {
                ModuleID = rsTmp.getInt("MODULE_ID");
            }

            //First check that this user is creator of document
            rsTmp = data.getResult("SELECT USER_ID FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE  MODULE_ID=" + ModuleID + " AND USER_ID=" + pUserID + " AND TYPE='C' AND DOC_NO='" + pDocNo + "'");
            rsTmp.first();
            if (rsTmp.getRow() > 0) {
                //Creator can not send directly to any other user in hierarchy. Discard the request
                canSend = false;
            } else {
                //User is not creator. Then check that all other users' status is 'P' except this user
                rsTmp = data.getResult("SELECT USER_ID FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE  MODULE_ID=" + ModuleID + " AND USER_ID<>" + pUserID + " AND STATUS<>'P' AND DOC_NO='" + pDocNo + "'");
                rsTmp.first();
                if (rsTmp.getRow() > 0) {
                    //This is normal hierarchy flow. Discard the request
                    canSend = false;
                } else {
                    //Yes user can send directly to any other user in this case. i.e. Second pass of approval hierarchy
                    canSend = true;

                }

            }
            rsTmp.close();

        } catch (Exception e) {

        }
        return canSend;

    }

    public static boolean IsSuperiorInHierarchy(int pCompanyID, int pHierarchyID, int pCreatorID, int pCurrentUserID) {
        ResultSet rsTmp;
        boolean IsSuperior = false;

        try {
            int CreatorSrNo = 0;
            int CurrentUserSrNo = 0;

            rsTmp = data.getResult("SELECT SR_NO FROM D_COM_HIERARCHY_RIGHTS WHERE COMPANY_ID=" + pCompanyID + " AND HIERARCHY_ID=" + pHierarchyID + " AND USER_ID=" + pCreatorID);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                CreatorSrNo = rsTmp.getInt("SR_NO");
            }

            rsTmp = data.getResult("SELECT SR_NO FROM D_COM_HIERARCHY_RIGHTS WHERE COMPANY_ID=" + pCompanyID + " AND HIERARCHY_ID=" + pHierarchyID + " AND USER_ID=" + pCurrentUserID);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                CurrentUserSrNo = rsTmp.getInt("SR_NO");
            }

            if ((CurrentUserSrNo > 0 && CreatorSrNo > 0) && (CurrentUserSrNo > CreatorSrNo)) {
                IsSuperior = true;
            }
            rsTmp.close();

        } catch (Exception e) {

        }
        return IsSuperior;

    }

    public static boolean IsCreator(int pCompanyID, int HierarchyID, int pUserID) {
        Connection tmpConn;
        ResultSet rsTmp;
        Statement tmpStmt;
        boolean blnCanFinal = false;

        try {
            tmpConn = data.getConn();
            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery("SELECT CREATOR FROM D_COM_HIERARCHY_RIGHTS WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND HIERARCHY_ID=" + Integer.toString(HierarchyID) + " AND USER_ID=" + Integer.toString(pUserID));
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                blnCanFinal = rsTmp.getBoolean("CREATOR");
            }

            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();

            return blnCanFinal;
        } catch (Exception e) {
            return blnCanFinal;
        }
    }

    public static int getFinalApprover(int pCompanyID, int pHierarchyID, String dbURL) {
        ResultSet rsTmp = null;
        int FinalApprover = 0;

        try {
            rsTmp = data.getResult("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE COMPANY_ID=" + pCompanyID + " AND HIERARCHY_ID=" + pHierarchyID + " AND FINAL_APPROVER=1", dbURL);
            rsTmp.first();
            if (rsTmp.getRow() > 0) {
                FinalApprover = rsTmp.getInt("USER_ID");
            }
        } catch (Exception e) {
        }
        return FinalApprover;
    }
}
