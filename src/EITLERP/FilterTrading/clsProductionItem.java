/*
 *
 * clsMRItem.java
 *
 * Created on April 19, 2004, 2:27 PM
 */

package EITLERP.FilterTrading;


import java.util.*;
import EITLERP.*;
 
/**
 *
 * @author  jadave
 * @version 
 */
 
public class clsProductionItem {

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
    public clsProductionItem() {
        props=new HashMap();
        props.put("PRODUCTION_NO", new Variant(""));
        props.put("COMPANY_ID",new Variant(0));
        props.put("PRODUCTION_DATE",new Variant(""));
        props.put("PIECE_NO", new Variant(""));
        props.put("TYPE",new Variant(""));
        
        props.put("QUALITY_CD",new Variant(""));
        props.put("PIECE_NO",new Variant(""));
        props.put("WIDTH",new Variant(0.00));
        props.put("METER",new Variant(0.00));
        props.put("KGS",new Variant(0.00));        
        props.put("FLAG",new Variant(0)); 
        props.put("SQ_METER",new Variant(0.00));
        props.put("NET_METER",new Variant(0.00));         
        
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("CREATED_BY",new Variant(0));
        props.put("APPROVED",new Variant(0));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED",new Variant(0));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("CANCELED", new Variant(0));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        
        
    }

}
