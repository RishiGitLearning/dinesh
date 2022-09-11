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
import EITLERP.Stores.*;
 
/** 
 *
 * @author  nrpatel
 * @version
 */
 
public class clsInwardItem {
    
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
    public clsInwardItem() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("INWARD_NO",new Variant(0));
        props.put("SR_NO",new Variant(0));
        props.put("ITEM_ID",new Variant(""));
        props.put("GROSS_QTY",new Variant(0));
        props.put("MIR_UPDATED",new Variant(false));
        
    }
    
}
