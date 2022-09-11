/*
 * clsSalesInterestItem.java
 *
 * Created on August 18, 2008, 3:37 PM
 */

package EITLERP.Finance;

import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.util.*;
import EITLERP.*;

/**
 *
 * @author  BHAVESH PATEL
 * @version
 */
public class clsSchJVDetail {
    public String LastError="";
    private ResultSet rsResultSet ;
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
    
    /** Creates new clsSchDetail */
    public clsSchJVDetail() {
        props = new HashMap();
       
        props.put("COMPANY_ID",new Variant(0));
        props.put("DOC_NO",new Variant(""));
        props.put("SR_NO",new Variant(0));
        props.put("GROUP_CODE",new Variant(""));
        props.put("MAIN_ACCOUNT_CODE",new Variant(""));
        props.put("BASE_MAIN_ACCOUNT_CODE",new Variant(""));
        props.put("LINK_CODE",new Variant(""));
        props.put("LINK_NAME",new Variant(""));
        props.put("AMOUNT",new Variant(0));
        props.put("EFFECT",new Variant(""));
        props.put("INDICATOR",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("CHANGED",new Variant(0));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("CANCELLED",new Variant(false));
    }
}