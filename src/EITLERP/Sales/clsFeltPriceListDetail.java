/*
 * clsMRItem.java
 *
 * Created on April 19, 2004, 2:27 PM
 */

package EITLERP.Sales;


import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import EITLERP.Finance.*;
import EITLERP.Sales.*;
 
/**
 *
 * @author  jadave
 * @version 
 */
 
public class clsFeltPriceListDetail {

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

    
    /** Creates new clsMRItem */
    public clsFeltPriceListDetail() {
        props=new HashMap();    
        props.put("COMPANY_ID",new Variant(0));
        props.put("DOC_NO", new Variant(0));
        props.put("SR_NO", new Variant(0));
        props.put("QUALITY_ID",new Variant(""));
        props.put("SQM_RATE",new Variant(0));
        props.put("SQM_RATE_DATE",new Variant(""));
        props.put("WT_RATE",new Variant(0));
        props.put("WT_RATE_DATE",new Variant(""));
        props.put("CANCELLED",new Variant(false));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("CHANGED",new Variant(""));
        props.put("CHANGED_DATE",new Variant(""));
        
    }

}
