/*
 * clsSTMRawItemDetail.java
 *
 * Created on May 10, 2004, 2:17 PM
 */

package EITLERP.Stores;


import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
 
/**  
 *
 * @author  jadave
 * @version
 */

public class clsSTMRawItemDetail {
    
    private HashMap props;
    public boolean Ready = false;

    public HashMap colSTMItems=new HashMap();
    
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
    
    
    /** Creates new clsSTMRawItemDetail */
    public clsSTMRawItemDetail() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant());
        props.put("STM_NO", new Variant(""));
        props.put("STM_SR_NO", new Variant(0));
        props.put("SR_NO",new Variant(0));
        props.put("ITEM_LOT_NO",new Variant(""));
        props.put("LOT_QTY",new Variant(0));
        props.put("ZERO_VAL_QTY",new Variant(0));
        props.put("STM_TYPE",new Variant(0));
   }
    
}
