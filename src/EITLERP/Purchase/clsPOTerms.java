/*
 * clsNoDataObject.java
 *
 * Created on April 6, 2004, 9:32 AM
 */

package EITLERP.Purchase;

 
import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
  
/** 
 *
 * @author  nrpatel
 * @version
 */

public class clsPOTerms {
    
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
    public clsPOTerms() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("PO_NO",new Variant(""));
        props.put("PO_TYPE",new Variant(0));
        props.put("SR_NO",new Variant(0));
        props.put("TERM_TYPE",new Variant(""));
        props.put("TERM_CODE",new Variant(0));
        props.put("TERM_DESC",new Variant(""));
    }
    
}
