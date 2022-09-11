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
 
public class clsIndentItemDetail {
    
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
    public clsIndentItemDetail() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("INDENT_NO",new Variant(""));
        props.put("INDENT_SR_NO",new Variant(0));
        props.put("SR_NO",new Variant(0));
        props.put("POSITION_DESC",new Variant(""));
        props.put("POSITION_NO",new Variant(""));
        props.put("CARD_NO",new Variant(""));
        props.put("NAME",new Variant(""));
        props.put("SHOE_SIZE",new Variant(""));
        props.put("GIVEN_LAST_YEAR",new Variant(false));
        props.put("REMARKS",new Variant(""));
        props.put("NO_ELIGIBLE",new Variant(0));
        props.put("NO_LAST_YEAR",new Variant(0));
        props.put("NO_THIS_YEAR",new Variant(0));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
    }
    
    
    
}
