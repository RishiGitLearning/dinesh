/*
 * clsNoDataObject.java
 *
 * Created on April 6, 2004, 9:32 AM
 */

package EITLERP;


import java.util.*;
import java.sql.*;
import java.net.*;
 
/**
 *
 * @author  nrpatel
 * @version
 */ 

public class clsSuppTerms {
    
    private HashMap props;
    public boolean Ready = false;
    
    public Variant getAttribute(String PropName)
 {
     return (Variant) props.get(PropName);
    }
    
    public void setAttribute(String PropName,Object Value)
 {
     props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,int Value)
 {
     props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,long Value)
 {
     props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,double Value)
 {
     props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,float Value)
 {
     props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,boolean Value)
 {
     props.put(PropName,new Variant(Value));
    }
    
    
    /** Creates new clsNoDataObject */
    public clsSuppTerms() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("SUPP_ID",new Variant(0));
        props.put("SR_NO",new Variant(0));
        props.put("TERM_TYPE",new Variant(""));
        props.put("TERM_CODE",new Variant(0));
        props.put("TERM_DAYS",new Variant(0));
        props.put("TERM_DESC",new Variant(""));
    }
    
}
