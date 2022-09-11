/*
 * 
 *
 * 
 */

package EITLERP.FeltSales.GIDC_SDF.NRGP;

import java.sql.*;
import java.net.*;
import java.util.*;
import EITLERP.*;
  
/**
 *
 * @author  nhpatel
 * @version 
 */ 
public class clsNRGPItem_SDML {
    
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
    public clsNRGPItem_SDML()
    {
        props = new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("GATEPASS_NO",new Variant(""));
        props.put("ITEM_CODE",new Variant(""));
        props.put("NRGP_DESC",new Variant(""));
        props.put("QTY",new Variant(0));
        props.put("GATEPASS_ISSUE_NO",new Variant(""));
        props.put("GATEPASS_ISSUE_SR_NO",new Variant(""));
        props.put("RATE",new Variant(0));
        props.put("NET_AMOUNT",new Variant(0));
        props.put("NET_AMOUNT_IN_WORDS",new Variant(""));
    }

}
