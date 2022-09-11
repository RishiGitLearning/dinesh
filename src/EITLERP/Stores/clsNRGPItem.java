/*
 * clsNRGPItem.java
 *
 * Created on April 23, 2004, 1:27 PM
 */

package EITLERP.Stores;

import java.sql.*;
import java.net.*;
import java.util.*;
import EITLERP.*;
  
/**
 *
 * @author  nhpatel
 * @version 
 */ 
public class clsNRGPItem {
    
    private HashMap props;
    public boolean Ready = false;

    public HashMap colItemLot=new HashMap();    
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
    
    /** Creates new clsNRGPItem */
    public clsNRGPItem()
    {
        props = new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("GATEPASS_NO",new Variant(""));
        props.put("SR_NO",new Variant(0));
        props.put("WAREHOUSE_ID",new Variant(""));
        props.put("LOCATION_ID",new Variant(""));
        props.put("ITEM_CODE",new Variant(""));
        props.put("NRGP_DESC",new Variant(""));
        props.put("UNIT",new Variant(0));
        props.put("QTY",new Variant(0));
        props.put("RATE",new Variant(0));
        props.put("RJN_NO",new Variant(""));
        props.put("RJN_SRNO",new Variant(0));
        props.put("GATEPASSREQ_NO",new Variant(""));
        props.put("GATEPASSREQ_SRNO",new Variant(0));
        props.put("DECLARATION_ID",new Variant(""));
        props.put("DECLARATION_SRNO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("COMPANY_ID",new Variant(0));
        props.put("CANCELED",new Variant(false));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("PACKING",new Variant(""));
    }

}
