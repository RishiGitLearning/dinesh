/*
 * clsCalcInterestItem.java
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
 * @author  MRUGESH THAKER
 * @version
 */
public class clsCalcInterestItem {
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
    
    /** Creates new clsInquiryItem */
    public clsCalcInterestItem() {
        props = new HashMap();
        
        props.put("COMPANY_ID",new Variant(0));
        props.put("DOC_NO",new Variant(""));
        props.put("SR_NO",new Variant(0));
        props.put("RECEIPT_NO",new Variant(""));
        props.put("INTEREST_DAYS",new Variant(0));
        props.put("INTEREST_RATE",new Variant(0.0));  //DOUBLE
        props.put("INT_MAIN_ACCOUNT_CODE",new Variant(""));
        props.put("PARTY_CODE",new Variant(""));
        props.put("MAIN_ACCOUNT_CODE",new Variant(""));
        props.put("INTEREST_AMOUNT",new Variant(0.0));  //DOUBLE
        props.put("TDS_AMOUNT",new Variant(0.0));  //DOUBLE
        props.put("NET_INTEREST",new Variant(0.0));  //DOUBLE
        props.put("LEGACY_WARRANT_NO", new Variant(0));
        props.put("WARRANT_NO", new Variant(""));
        props.put("WARRANT_DATE", new Variant(""));
        props.put("MICR_NO", new Variant(0));
        props.put("WARRANT_CLEAR",new Variant(""));
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("CHANGED",new Variant(0));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("CANCELED",new Variant(false));
        
        props.put("INTEREST_CALCULATION_TYPE",new Variant(0));
    }
}