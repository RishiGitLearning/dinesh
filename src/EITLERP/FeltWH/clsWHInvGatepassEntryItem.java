/*
 * 
 *
 * 
 */

package EITLERP.FeltWH;

import java.sql.*;
import java.net.*;
import java.util.*;
import EITLERP.*;
  
/**
 *
 * @author  nhpatel
 * @version 
 */ 
public class clsWHInvGatepassEntryItem {
    
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
    public clsWHInvGatepassEntryItem()
    {
        props = new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("GATEPASS_NO",new Variant(""));
        props.put("SR_NO",new Variant(0));
        props.put("WH_INVOICE_NO",new Variant(""));
        props.put("WH_INVOICE_DATE",new Variant(""));
        props.put("WH_PARTY_CODE",new Variant(""));
        props.put("WH_PARTY_NAME",new Variant(""));
        props.put("WH_BALE_NO",new Variant(""));
        props.put("WH_BALE_DATE",new Variant(""));
        props.put("WH_DISPATCH_STATION",new Variant(""));
        props.put("WH_ACTUAL_TRANSPORTER",new Variant(""));
        props.put("WH_TYPE_OF_PACKING",new Variant(""));
    }

}
