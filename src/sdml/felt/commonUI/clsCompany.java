
/*
 * clsCompany.java
 * Created on March 26, 2004, 10:20 AM
 */
/*
 * @author  
 * @version
 */
package sdml.felt.commonUI;

import java.util.*;
import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;

public class clsCompany {

    public String lastError;
    private HashMap props;

    public boolean Ready = false;

    // Recordset Variables
    private ResultSet rsCompany;
    private Connection conn;
    private Statement stmt;
    //private ResultSetMetaData rsmData;
    //private int numCols;

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
     * Creates new clsCompany - constructure of company class
     */
    public clsCompany() {
        lastError = "";
        props = new HashMap();
        props.put("COMPANY_ID", new Variant(0));
        props.put("COMPANY_NAME", new Variant(""));
        props.put("ADD1", new Variant(""));
        props.put("ADD2", new Variant(""));
        props.put("ADD3", new Variant(""));
        props.put("CITY", new Variant(""));
        props.put("STATE", new Variant(""));
        props.put("PINCODE", new Variant(""));
        props.put("PHONE", new Variant(""));
        props.put("FAX", new Variant(""));
        props.put("E_MAIL", new Variant(""));
        props.put("URL", new Variant(""));
        props.put("ECC_NO", new Variant(""));
        props.put("ECC_DATE", new Variant(""));
        props.put("RANGE1", new Variant(""));
        props.put("COMMISIONRATE", new Variant(""));
        props.put("DIVISION", new Variant(""));
        props.put("CST_NO", new Variant(""));
        props.put("CST_DATE", new Variant(""));
        props.put("LICENSE_NO", new Variant(""));
        props.put("REGISTERATION_NO", new Variant(""));
        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
    }

    boolean LoadData() {
        Ready = false;
        try {
            String strSql = "SELECT * FROM D_COM_COMPANY_MASTER";
            conn = data.getConn();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            if (stmt.execute(strSql)) {
                rsCompany = stmt.getResultSet();
                Ready = MoveFirst();
            } else {
                Ready = false;
            }

            return Ready;
        } catch (Exception e) {
            lastError = e.getMessage();
            return false;
        }
    }

    public void Close() {
        try {
            //conn.close();
            stmt.close();

        } catch (Exception e) {

        }

    }

    boolean MoveFirst() {
        try {
            rsCompany.first();
            return populateprops();
        } catch (Exception e) {
            lastError = e.getMessage();
            return false;
        }
    }

    boolean MovePrevious() {
        try {
            rsCompany.previous();
            return populateprops();

        } catch (Exception e) {
            lastError = e.getMessage();
            return false;
        }
    }

    boolean MoveNext() {
        try {
            rsCompany.next();
            return populateprops();
        } catch (Exception e) {
            lastError = e.getMessage();
            return false;
        }
    }

    boolean MoveLast() {
        try {
            rsCompany.last();
            return populateprops();
        } catch (Exception e) {
            lastError = e.getMessage();
            return false;
        }
    }

    boolean Add() {
        try {
            ResultSet rstemp;
            Statement stmtemp = conn.createStatement();
            rstemp = stmtemp.executeQuery("select MAX(COMPANY_ID) as MAXID from D_COM_COMPANY_MASTER");

            rstemp.first();
            setAttribute("COMPANY_ID", (int) (rstemp.getLong("MAXID") + 1));

            rsCompany.moveToInsertRow();
            rsCompany.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
            rsCompany.updateString("COMPANY_NAME", (String) getAttribute("COMPANY_NAME").getObj());
            rsCompany.updateString("ADD1", (String) getAttribute("ADD1").getObj());
            rsCompany.updateString("ADD2", (String) getAttribute("ADD2").getObj());
            rsCompany.updateString("ADD3", (String) getAttribute("ADD3").getObj());
            rsCompany.updateString("CITY", (String) getAttribute("CITY").getObj());
            rsCompany.updateString("STATE", (String) getAttribute("STATE").getObj());
            /*rsCompany.updateString("PINCODE",(String)getAttribute("PINCODE").getObj());
             rsCompany.updateString("PHONE",(String)getAttribute("PHONE").getObj());
             rsCompany.updateString("FAX",(String)getAttribute("FAX").getObj());
             rsCompany.updateString("E_MAIL",(String)getAttribute("E_MAIL").getObj());
             rsCompany.updateString("URL",(String)getAttribute("URL").getObj());
             */
            rsCompany.updateString("ECC_NO", (String) getAttribute("ECC_NO").getObj());
            rsCompany.updateString("ECC_DATE", (String) getAttribute("ECC_DATE").getObj());
            /*rsCompany.updateString("RANGE",(String)getAttribute("RANGE").getObj());
             rsCompany.updateString("COMMISIONRATE",(String)getAttribute("COMMISIONRATE").getObj());
             rsCompany.updateString("DIVISION",(String)getAttribute("DIVISION").getObj());
             rsCompany.updateString("CST_NO",(String)getAttribute("CST_NO").getObj());
             rsCompany.updateString("CST_DATE",(String)getAttribute("CST_DATE").getObj());
             rsCompany.updateString("GST_NO",(String)getAttribute("GST_NO").getObj());
             rsCompany.updateString("GST_DATE",(String)getAttribute("GST_DATE").getObj());
             rsCompany.updateString("LICENSE_NO",(String)getAttribute("LICENSE_NO").getObj());
             rsCompany.updateString("REGISTERATION_NO",(String)getAttribute("REGISTERATION_NO").getObj());
             rsCompany.updateInt("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
             rsCompany.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
             rsCompany.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
             rsCompany.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
             */
            rsCompany.insertRow();
            return true;
        } catch (Exception e) {
            lastError = e.getMessage();
            return false;
        }
    }

    boolean Edit() {
        try {
            rsCompany.updateString("COMPANY_NAME", (String) getAttribute("COMPANY_NAME").getObj());
            rsCompany.updateString("ADD1", (String) getAttribute("ADD1").getObj());
            rsCompany.updateString("ADD2", (String) getAttribute("ADD2").getObj());
            rsCompany.updateString("ADD3", (String) getAttribute("ADD3").getObj());
            rsCompany.updateString("CITY", (String) getAttribute("CITY").getObj());
            rsCompany.updateString("STATE", (String) getAttribute("STATE").getObj());
            rsCompany.updateString("PINCODE", (String) getAttribute("PINCODE").getObj());
            rsCompany.updateString("PHONE", (String) getAttribute("PHONE").getObj());
            rsCompany.updateString("FAX", (String) getAttribute("FAX").getObj());
            rsCompany.updateString("E_MAIL", (String) getAttribute("E_MAIL").getObj());
            rsCompany.updateString("URL", (String) getAttribute("URL").getObj());
            rsCompany.updateString("ECC_NO", (String) getAttribute("ECC_NO").getObj());
            rsCompany.updateString("ECC_DATE", (String) getAttribute("ECC_DATE").getObj());
            rsCompany.updateString("RANGE1", (String) getAttribute("RANGE1").getObj());
            rsCompany.updateString("COMMISIONRATE", (String) getAttribute("COMMISIONRATE").getObj());
            rsCompany.updateString("DIVISION", (String) getAttribute("DIVISION").getObj());
            rsCompany.updateString("CST_NO", (String) getAttribute("CST_NO").getObj());
            rsCompany.updateString("CST_DATE", (String) getAttribute("CST_DATE").getObj());
            rsCompany.updateString("GST_NO", (String) getAttribute("GST_NO").getObj());
            rsCompany.updateString("GST_DATE", (String) getAttribute("GST_DATE").getObj());
            rsCompany.updateString("LICENSE_NO", (String) getAttribute("LICENSE_NO").getObj());
            rsCompany.updateString("REGISTERATION_NO", (String) getAttribute("REGISTERATION_NO").getObj());
            rsCompany.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
            rsCompany.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
            rsCompany.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
            rsCompany.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());

            rsCompany.updateRow();
            return true;
        } catch (Exception e) {
            lastError = e.getMessage();
            return false;
        }
    }

    boolean Delete() {
        try {
            String strQry = "Delete from D_COM_COMPANY_MASTER where COMPANY_ID=" + getAttribute("COMPANY_ID").getVal();

            if (data.Execute(strQry)) {
                rsCompany.refreshRow();
                return MoveLast();
            } else {
                return false;
            }
        } catch (Exception e) {
            lastError = e.getMessage();
            return false;
        }
    }

    Object getObject(int pCompanyID) {
        String strCondition = " where COMPANY_ID=" + pCompanyID;
        clsCompany objCompany = new clsCompany();
        objCompany.Filter(strCondition);

        return objCompany;
    }

    public static HashMap getList(String pCondition) {
        ResultSet rsTmp;
        Connection tmpConn = null;
        Statement tmpStmt = null;

        tmpConn = data.getCreatedConn();

        HashMap List = new HashMap();
        long Counter = 0;

        try {
            tmpStmt = tmpConn.createStatement();

            rsTmp = tmpStmt.executeQuery("SELECT * FROM RETAIL_SHOP.D_COM_COMPANY_MASTER " + pCondition + " ORDER BY DISPLAY_ORDER");

            Counter = 0;
            while (rsTmp.next()) {
                Counter = Counter + 1;
                clsCompany ObjCompany = new clsCompany();

                ObjCompany.setAttribute("COMPANY_ID", rsTmp.getInt("COMPANY_ID"));
                ObjCompany.setAttribute("COMPANY_NAME", rsTmp.getString("COMPANY_NAME"));
                ObjCompany.setAttribute("ADD1", rsTmp.getString("ADD1"));
                ObjCompany.setAttribute("ADD2", rsTmp.getString("ADD2"));
                ObjCompany.setAttribute("ADD3", rsTmp.getString("ADD3"));
                ObjCompany.setAttribute("CITY", rsTmp.getString("CITY"));
                ObjCompany.setAttribute("STATE", rsTmp.getString("STATE"));
                ObjCompany.setAttribute("PINCODE", rsTmp.getString("PINCODE"));
                ObjCompany.setAttribute("PHONE", rsTmp.getString("PHONE"));
                ObjCompany.setAttribute("FAX", rsTmp.getString("FAX"));
                ObjCompany.setAttribute("E_MAIL", rsTmp.getString("E_MAIL"));
                ObjCompany.setAttribute("URL", rsTmp.getString("URL"));
                ObjCompany.setAttribute("ECC_NO", rsTmp.getString("ECC_NO"));
                //ObjCompany.setAttribute("ECC_DATE",rsTmp.getString("ECC_DATE"));
                ObjCompany.setAttribute("RANGE1", rsTmp.getString("RANGE1"));
                ObjCompany.setAttribute("COMMISIONRATE", rsTmp.getString("COMMISIONRATE"));
                ObjCompany.setAttribute("DIVISION", rsTmp.getString("DIVISION"));
                ObjCompany.setAttribute("CST_NO", rsTmp.getString("CST_NO"));
                //ObjCompany.setAttribute("CST_DATE",rsTmp.getString("CST_DATE"));
                ObjCompany.setAttribute("LICENSE_NO", rsTmp.getString("LICENSE_NO"));
                ObjCompany.setAttribute("REGISTERATION_NO", rsTmp.getString("REGISTERATION_NO"));
                ObjCompany.setAttribute("CREATED_BY", rsTmp.getInt("CREATED_BY"));
                //ObjCompany.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjCompany.setAttribute("MODIFIED_BY", rsTmp.getInt("MODIFIED_BY"));
                //ObjCompany.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));

                List.put(Long.toString(Counter), ObjCompany);
            }

            rsTmp.close();
            //tmpConn.close();
            tmpStmt.close();

        } catch (Exception e) {
            e.printStackTrace();
            //JOptionPane.showMessageDialog(null,e.getMessage());
        }

        return List;
    }

    public static String getCityName(int pCompanyID) {
        ResultSet rsTmp;
        Connection tmpConn = null;
        Statement tmpStmt = null;

        tmpConn = data.getCreatedConn();

        String City = "";

        try {
            tmpStmt = tmpConn.createStatement();

            rsTmp = tmpStmt.executeQuery("SELECT CITY FROM D_COM_COMPANY_MASTER WHERE COMPANY_ID=" + pCompanyID);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                City = rsTmp.getString("CITY");
            }

            rsTmp.close();
            tmpConn.close();
            tmpStmt.close();
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null,e.getMessage());
        }

        return City;
    }

    public static String getCompanyName(int pCompanyID) {
        ResultSet rsTmp;
        Connection tmpConn = null;
        Statement tmpStmt = null;

        tmpConn = data.getCreatedConn();

        String CompanyName = "";

        try {
            tmpStmt = tmpConn.createStatement();

            rsTmp = tmpStmt.executeQuery("SELECT COMPANY_NAME FROM D_COM_COMPANY_MASTER WHERE COMPANY_ID=" + pCompanyID);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                CompanyName = rsTmp.getString("COMPANY_NAME");
            }

            rsTmp.close();
            tmpConn.close();
            tmpStmt.close();
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null,e.getMessage());
        }

        return CompanyName;
    }

    boolean Filter(String pCondition) {
        Ready = false;
        try {
            String strSql = "SELECT * FROM D_COM_COMPANY_MASTER " + pCondition;
            conn = data.getConn();
            stmt = conn.createStatement();

            if (stmt.execute(strSql)) {
                rsCompany = stmt.getResultSet();
                Ready = MoveFirst();
            } else {
                Ready = false;
            }

            return Ready;
        } catch (Exception e) {
            lastError = e.getMessage();
            return false;
        }
    }

    boolean populateprops() {
        try {
            setAttribute("COMPANY_ID", rsCompany.getInt("COMPANY_ID"));
            setAttribute("COMPANY_NAME", rsCompany.getString("COMPANY_NAME"));
            setAttribute("ADD1", rsCompany.getString("ADD1"));
            setAttribute("ADD2", rsCompany.getString("ADD2"));
            setAttribute("ADD3", rsCompany.getString("ADD3"));
            setAttribute("CITY", rsCompany.getString("CITY"));
            setAttribute("STATE", rsCompany.getString("STATE"));
            setAttribute("PINCODE", rsCompany.getString("PINCODE"));
            setAttribute("PHONE", rsCompany.getString("PHONE"));
            setAttribute("FAX", rsCompany.getString("FAX"));
            setAttribute("E_MAIL", rsCompany.getString("E_MAIL"));
            setAttribute("URL", rsCompany.getString("URL"));
            setAttribute("ECC_NO", rsCompany.getString("ECC_NO"));
            setAttribute("ECC_DATE", rsCompany.getString("ECC_DATE"));
            //JOptionPane.showMessageDialog(null,getAttribute("ECC_DATE").getObj());

            setAttribute("RANGE1", rsCompany.getString("RANGE1"));
            setAttribute("COMMISIONRATE", rsCompany.getString("COMMISIONRATE"));
            setAttribute("DIVISION", rsCompany.getString("DIVISION"));
            setAttribute("CST_NO", rsCompany.getString("CST_NO"));
            setAttribute("CST_DATE", rsCompany.getString("CST_DATE"));
            setAttribute("LICENSE_NO", rsCompany.getString("LICENSE_NO"));
            setAttribute("REGISTERATION_NO", rsCompany.getString("REGISTERATION_NO"));
            setAttribute("CREATED_BY", rsCompany.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE", rsCompany.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY", rsCompany.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE", rsCompany.getString("MODIFIED_DATE"));
            return true;
        } catch (Exception e) {
            lastError = e.getMessage();
            return false;
        }
    }
}
