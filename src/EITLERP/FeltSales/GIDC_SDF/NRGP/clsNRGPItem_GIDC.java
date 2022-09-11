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
public class clsNRGPItem_GIDC {
    
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
    public clsNRGPItem_GIDC()
    {
        props = new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("GATEPASS_NO",new Variant(""));
        props.put("PIECE_NO",new Variant(""));
        props.put("LENGTH",new Variant(0));
        props.put("WIDTH",new Variant(0));
        props.put("WEIGHT",new Variant(0));
        props.put("SQMTR",new Variant(0));
        props.put("NRGP_DESC",new Variant(""));
        props.put("QTY",new Variant(0));
        props.put("WASTE_LENGTH_QTY",new Variant(0));
        props.put("WASTE_WEFT_QTY",new Variant(0));
        props.put("RATE",new Variant(0));
        props.put("NET_AMOUNT",new Variant(0));
        props.put("NET_AMOUNT_IN_WORDS",new Variant(""));
        props.put("DELIVERY_CHALLAN_NO",new Variant(""));
        props.put("DELIVERY_CHALLAN_DATE",new Variant(""));
    }

}
