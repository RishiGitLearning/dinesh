/*
 *
 * clsMRItem.java
 *
 * Created on April 19, 2004, 2:27 PM
 */

package EITLERP.Production.ReportUI;


import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;

/**
 *
 * @author  jadave
 * @version
 */

public class clsFeltF6item {
    
    ;
    private HashMap props;
    public boolean Ready = false;
    
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
    
    
    /** Creates new clsMRItem */
    public clsFeltF6item() {
        props=new HashMap();
       
        
        props.put("F6_FROM_DATE",new Variant(""));
        props.put("F6_TO_DATE",new Variant(""));
        props.put("INVOICE_NO",new Variant(""));
        props.put("INVOICE_DATE",new Variant(""));
        props.put("PARTY_CODE",new Variant(""));
        props.put("PARTY_NAME",new Variant(""));
        props.put("CITY_ID",new Variant(""));
        props.put("TOTAL_NET_AMOUNT",new Variant(""));
        props.put("AMOUNT",new Variant(""));
        props.put("VOUCHER",new Variant(""));
        props.put("VALUE_DATE",new Variant(""));
        props.put("REMARKS1",new Variant(""));
        props.put("REMARKS2",new Variant(""));
        props.put("F6",new Variant(""));
    //    props.put("DELAY_F6",new Variant(""));
        
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("CHANGED",new Variant(""));
         
    }
    
}
