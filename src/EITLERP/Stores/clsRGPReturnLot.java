/*
 * clsNoDataObject.java
 *
 * Created on April 6, 2004, 9:32 AM
 */

package EITLERP.Stores;


import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
  
/** 
 *
 * @author  nrpatel
 * @version
 */

public class clsRGPReturnLot {
    
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
    public clsRGPReturnLot() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("RETURN_NO",new Variant(""));
        props.put("RETURN_SR_NO",new Variant(0));
        props.put("SR_NO",new Variant(0));
        props.put("LOT_NO",new Variant(""));
        props.put("LOT_QTY",new Variant(0));
    }
    
}
