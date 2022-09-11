package EITLERP.Stores;

/*
 * clsIndentItem.java
 *
 * Created on April 6, 2004, 9:32 AM
 */


import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;

/**
 *
 * @author  nhpatel
 * @version 
 */
 
public class clsDecConsumedItem {

    private HashMap props;    
    public boolean Ready = false;

    public HashMap colItemDetail=new HashMap();
    
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
    public clsDecConsumedItem() {
        props=new HashMap();    
        props.put("COMPANY_ID",new Variant(0));
        props.put("DOC_NO",new Variant(""));
        props.put("SR_NO",new Variant(0));
        props.put("DEC_ID",new Variant(""));
        props.put("DEC_SR_NO",new Variant(0));
        props.put("ITEM_DESC",new Variant(""));
        props.put("UNIT_ID",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("DEC_QTY",new Variant(0));
        props.put("CONSUMED_QTY",new Variant(0));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
    }

}