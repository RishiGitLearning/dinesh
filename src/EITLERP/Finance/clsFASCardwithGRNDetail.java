/*
 * clsMRItem.java
 *
 * Created on April 19, 2004, 2:27 PM
 */

package EITLERP.Finance;


import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;

 
/**
 *
 * @author  jadave
 * @version 
 */
 
public class clsFASCardwithGRNDetail {

    private HashMap props;    
    public boolean Ready = false;
    
    public HashMap colFASItemsDetailEX=new HashMap();
    
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
    public clsFASCardwithGRNDetail() {
        props=new HashMap();    
        props.put("COMPANY_ID",new Variant(0));
        props.put("ASSENT_NO",new Variant(""));
        props.put("SR_NO",new Variant(0));
        props.put("YEAR",new Variant(""));
        props.put("AMOUNT",new Variant(0));
        props.put("SALE_DOC_NO",new Variant(""));
        props.put("SALE_DATE",new Variant(""));
        props.put("SALE_VALUE",new Variant(0));
        props.put("SJ_AMOUNT",new Variant(0));
        
        
    }
}
