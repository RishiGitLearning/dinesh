/*
 * clsBusinessObject.java
 *
 * Created on April 5, 2004, 3:02 PM
 */
package sdml.felt.commonUI;

import java.util.*;
import java.sql.*;
import java.net.*;

/**
 *
 * @author 
 * @version
 */
public class clsDocMailer {

    public String LastError = "";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;

    private HashMap props;
    public boolean Ready = false;

    public HashMap colEmail = new HashMap();

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
    public clsDocMailer() {
        props = new HashMap();
        props.put("COMPANY_ID", new Variant(0));
        props.put("DOC_NO", new Variant(0));
        props.put("DOC_DATE", new Variant(""));
        props.put("MODULE_ID", new Variant(0));
        props.put("SENT_BY", new Variant(0));
        props.put("DESCRIPTION", new Variant(""));
        props.put("FROM", new Variant(""));
        props.put("SUBJECT", new Variant(""));
        props.put("MAIL_DOC_NO", new Variant(""));
        props.put("CC", new Variant(""));
        props.put("BCC", new Variant(""));
    }

    public static Object getObject(int pCompanyID, int DocNo) {
        Connection tmpConn = null;
        Statement stTmp = null;
        ResultSet rsTmp;
        clsDocMailer ObjDoc = new clsDocMailer();

        try {

            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            rsTmp = stTmp.executeQuery("SELECT * FROM D_COM_MAIL WHERE COMPANY_ID=" + pCompanyID + " AND DOC_NO=" + DocNo);
            rsTmp.first();
            if (rsTmp.getRow() > 0) {
                ObjDoc.setAttribute("COMPANY_ID", rsTmp.getInt("COMPANY_ID"));
                ObjDoc.setAttribute("DOC_NO", rsTmp.getInt("DOC_NO"));
                ObjDoc.setAttribute("DOC_DATE", rsTmp.getString("DOC_DATE"));
                ObjDoc.setAttribute("MODULE_ID", rsTmp.getInt("MODULE_ID"));
                ObjDoc.setAttribute("SENT_BY", rsTmp.getInt("SENT_BY"));
                ObjDoc.setAttribute("DESCRIPTION", rsTmp.getString("DESCRITION"));
                ObjDoc.setAttribute("FROM", rsTmp.getString("FROM"));
                ObjDoc.setAttribute("SUBJECT", rsTmp.getString("SUBJECT"));
                ObjDoc.setAttribute("MAIL_DOC_NO", rsTmp.getString("MAIL_DOC_NO"));
                ObjDoc.setAttribute("CC", rsTmp.getString("CC"));
                ObjDoc.setAttribute("BCC", rsTmp.getString("BCC"));

                //Now get Line level
                ObjDoc.colEmail.clear();

                stTmp = tmpConn.createStatement();
                rsTmp = stTmp.executeQuery("SELECT * FROM D_COM_MAIL_LIST WHERE COMPANY_ID=" + pCompanyID + " AND DOC_NO=" + DocNo);
                rsTmp.first();

                while (!rsTmp.isAfterLast()) {
                    ObjDoc.colEmail.put(Integer.toString(ObjDoc.colEmail.size() + 1), rsTmp.getString("EMAIL"));
                    rsTmp.next();
                }

                rsTmp.close();
                stTmp.close();
                //tmpConn.close();
            }

            rsTmp.close();
            stTmp.close();
            //tmpConn.close();
        } catch (Exception e) {

        } finally {
            try {
                stTmp.close();
                tmpConn.close();
            } catch (Exception s) {
                s.printStackTrace();
            }
        }

        return ObjDoc;
    }

    public long Insert() {
        Connection tmpConn = null;
        Statement stHeader = null, stEMail = null;
        try {
            tmpConn = data.getConn();
            stHeader = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stEMail = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            ResultSet rsHeader = stHeader.executeQuery("SELECT * FROM D_COM_MAIL");
            ResultSet rsEMail = stEMail.executeQuery("SELECT * FROM D_COM_MAIL_LIST");
            rsEMail.first();
            rsHeader.first();

            long MaxNo = data.getMaxID(SDMLERPGLOBAL.gCompanyID, "D_COM_MAIL", "DOC_NO") + 1;

            rsHeader.moveToInsertRow();
            rsHeader.updateInt("COMPANY_ID", SDMLERPGLOBAL.gCompanyID);
            rsHeader.updateLong("DOC_NO", MaxNo);
            rsHeader.updateString("DOC_DATE", (String) getAttribute("DOC_DATE").getObj());
            rsHeader.updateInt("MODULE_ID", (int) getAttribute("MODULE_ID").getVal());
            rsHeader.updateInt("SENT_BY", (int) getAttribute("SENT_BY").getVal());
            rsHeader.updateString("DESCRIPTION", (String) getAttribute("DESCRIPTION").getObj());
            rsHeader.updateString("FROM", (String) getAttribute("FROM").getObj());
            rsHeader.updateString("SUBJECT", (String) getAttribute("SUBJECT").getObj());
            rsHeader.updateString("MAIL_DOC_NO", (String) getAttribute("MAIL_DOC_NO").getObj());
            rsHeader.updateString("CC", (String) getAttribute("CC").getObj());
            rsHeader.updateString("BCC", (String) getAttribute("BCC").getObj());
            rsHeader.updateBoolean("CHANGED", true);
            rsHeader.updateString("CHANGED_DATE", SDMLERPGLOBAL.getCurrentDateDB());
            rsHeader.insertRow();

            //Inserting EMail addresses
            for (int i = 1; i <= colEmail.size(); i++) {
                String EMail = (String) colEmail.get(Integer.toString(i));

                rsEMail.moveToInsertRow();
                rsEMail.updateInt("COMPANY_ID", SDMLERPGLOBAL.gCompanyID);
                rsEMail.updateLong("DOC_NO", MaxNo);
                rsEMail.updateInt("SR_NO", i);
                rsEMail.updateString("EMAIL", EMail);
                rsEMail.insertRow();
            }

            return MaxNo;
        } catch (Exception e) {
            LastError = e.getMessage();
            return 0;
        } finally {
            try {
                stEMail.close();
                stHeader.close();
                tmpConn.close();
            } catch (Exception s) {
                s.printStackTrace();
            }
        }
    }

}
