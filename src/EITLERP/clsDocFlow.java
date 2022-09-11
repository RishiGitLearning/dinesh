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
 * @author  nrpithva
 * @version
 */

public class clsDocFlow {
    
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
    public clsDocFlow() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("MODULE_ID",new Variant(0));
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("USER_ID",new Variant(""));
        props.put("STATUS",new Variant(""));
        props.put("TYPE",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("DEPT_ID",new Variant(0));
    }
    
}
