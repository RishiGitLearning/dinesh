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
import java.util.*;
 
/**
 *
 * @author  nrpithva
 * @version
 */
 
public class clsGPRItem {
    
    private HashMap props;
    public boolean Ready = false;
    
    
    public HashMap colLot=new HashMap();
        
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
    public clsGPRItem() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("GATEPASS_REQ_NO",new Variant(""));
        props.put("SR_NO",new Variant(0));
        props.put("ITEM_CODE",new Variant(""));
        props.put("ITEM_EXTRA_DESC",new Variant(""));
        props.put("QTY",new Variant(0));
        props.put("UNIT",new Variant(0));
        props.put("PACKING",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("DEC_ID",new Variant(""));
        props.put("DEC_SR_NO",new Variant(0));
    }
    
}
