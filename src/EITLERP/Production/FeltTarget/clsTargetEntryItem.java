/*
 *
 * clsMRItem.java
 *
 * Created on April 19, 2004, 2:27 PM
 */

package EITLERP.Production.FeltTarget;


import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
 
/**
 *
 * @author  jadave
 * @version 
 */
 
public class clsTargetEntryItem {

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
    public clsTargetEntryItem() {
        props=new HashMap();
        
        props.put("DOC_NO", new Variant(""));
        props.put("DOC_DATE", new Variant(""));
        props.put("SR_NO",new Variant(0));
        props.put("PARTY_CODE", new Variant(""));
        props.put("PARTY_NAME", new Variant(""));
        props.put("PRODUCT_CODE",new Variant(""));
        props.put("MACHINE_POSITION",new Variant(""));       
        props.put("TGT_Q1",new Variant(0.00));
        props.put("TGT_Q2",new Variant(0.00));
        props.put("TGT_Q3",new Variant(0.00));
        props.put("TGT_Q4",new Variant(0.00));
        props.put("TOTAL_TARGET",new Variant(0.00));
        props.put("TGT_Q1_OLD",new Variant(0.00));
        props.put("TGT_Q2_OLD",new Variant(0.00));
        props.put("TGT_Q3_OLD",new Variant(0.00));
        props.put("TGT_Q4_OLD",new Variant(0.00));
        
        for(int i=2;i<=20;i++)
        {
            props.put("TGT_Q1_OLD_R"+i,new Variant(0.00));
            props.put("TGT_Q2_OLD_R"+i,new Variant(0.00));
            props.put("TGT_Q3_OLD_R"+i,new Variant(0.00));
            props.put("TGT_Q4_OLD_R"+i,new Variant(0.00));
        }
        
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
