/*
 * clsTrialBalalnce.java
 *
 * Created on May 15, 2009, 12:29 PM
 */

package EITLERP.Finance.ReportsUI;

import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.util.*;
import EITLERP.*;
import EITLERP.Finance.*;

/**
 *
 * @author  nitin
 */
public class clsTrialBalance {
    public String LastError="";
    private ResultSet rsInterestItem;
    private Connection Conn;
    private Statement Stmt;
    public boolean Ready = false;
    private HashMap props;
    
    public Variant getAttribute(String PropName) {
        return (Variant) props.get(PropName);
    }
    
    public void setAttribute(String PropName,Object Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,int Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,long Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,double Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,float Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,boolean Value) {
        props.put(PropName,new Variant(Value));
    }
    
    /** Creates a new instance of clsTrialBalalnce */
    public clsTrialBalance() {
        props = new HashMap();
        props.put("COMPANY_ID", new Variant(""));
        props.put("MAIN_ACCOUNT_CODE", new Variant(""));
        props.put("SUB_ACCOUNT_CODE", new Variant(""));
        props.put("ACCOUNT_NAME",new Variant(""));
        props.put("ADDRESS",new Variant(""));
        props.put("DEBIT",new Variant(0.0));
        props.put("CREDIT",new Variant(0.0));
    }
    
}
