/*
 *
 * clsMRItem.java
 *
 * Created on April 19, 2004, 2:27 PM
 */

package EITLERP.Production.FeltF;


import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;

/**
 *
 * @author  jadave
 * @version
 */

public class clsFeltFitem {
    
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
    public clsFeltFitem() {
        props=new HashMap();
       
   
        props.put("F6_ID",new Variant(""));
        props.put("F6_FROM_DATE",new Variant(""));
        props.put("F6_TO_DATE",new Variant(""));
        
        props.put("HIERARCHY_ID",new Variant(""));
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("APPROVED",new Variant(""));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("REJECTED",new Variant(""));
        props.put("REJECTED_DATE",new Variant(""));
        props.put("CANCELED",new Variant(""));
        props.put("CHANGED",new Variant(""));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("REJECTED_REMARKS",new Variant(""));
        

        
    
        props.put("INVOICE_NO",new Variant(""));
        props.put("INVOICE_DATE",new Variant(""));
        props.put("PARTY_CODE",new Variant(""));
        props.put("PARTY_NAME",new Variant(""));
        props.put("INVOICE_AMT",new Variant(""));
        
        props.put("EXT1",new Variant(""));
        props.put("EXT2",new Variant(""));
        props.put("EXT3",new Variant(""));
        props.put("EXT4",new Variant(""));
        props.put("EXT5",new Variant(""));
        props.put("EXT6",new Variant(""));
        
   
        props.put("DD_CH_NO",new Variant(""));
        props.put("DD_CH_DATE",new Variant(""));
        props.put("DD_CH_AMT",new Variant(""));
        props.put("DD_CH_CLR_DATE",new Variant(""));
        props.put("LATE_DAYS",new Variant(""));
        props.put("UNIT_GRP_VOL_PRE",new Variant(""));
        props.put("UNIT_GRP_VOL_CUR",new Variant(""));
        props.put("UNIT_GRP_VOL_EXP",new Variant(""));
        props.put("REMARKS",new Variant(""));
        
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("CHANGED",new Variant(""));
         
    }
    
}
