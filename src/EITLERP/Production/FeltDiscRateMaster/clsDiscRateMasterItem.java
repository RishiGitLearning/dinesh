/*
 *
 * clsMRItem.java
 *
 * Created on April 19, 2004, 2:27 PM
 */

package EITLERP.Production.FeltDiscRateMaster;


import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
 
/**
 *
 * @author  jadave
 * @version 
 */
 
public class clsDiscRateMasterItem {

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
    public clsDiscRateMasterItem() {
        props=new HashMap();
        
        props.put("PARTY_CODE", new Variant(""));
        props.put("PARTY_NAME", new Variant(""));
        props.put("GROUP_CODE", new Variant(""));
        props.put("GROUP_NAME", new Variant(""));
        props.put("MASTER_NO", new Variant(""));
        props.put("TURN_OVER_TARGET", new Variant(""));
        props.put("SANCTION_DATE",new Variant(""));
        props.put("EFFECTIVE_FROM",new Variant(""));
        props.put("EFFECTIVE_TO",new Variant(""));
        
        props.put("SR_NO",new Variant(0));
        props.put("PRODUCT_CODE",new Variant(""));
        props.put("PIECE_NO",new Variant(""));
        props.put("MACHINE_NO",new Variant(""));       
        props.put("MACHINE_POSITION",new Variant(""));       
        props.put("DISC_PER",new Variant(0.00));
        props.put("SEAM_VALUE",new Variant(0));
        props.put("YRED_DISC_PER",new Variant(0.00));
        props.put("YRED_SEAM_VALUE",new Variant(0));
        
        props.put("DIVERSION_FLAG",new Variant(""));       
        
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("UPDATED_BY",new Variant(0));
        props.put("ENTRY_DATE",new Variant(""));
        props.put("CHANGED",new Variant(""));
        props.put("CHANGED_DATE",new Variant(""));   
        
    }

}
