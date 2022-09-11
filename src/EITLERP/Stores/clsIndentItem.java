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
 
public class clsIndentItem {

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
    public clsIndentItem() {
        props=new HashMap();    
        props.put("COMPANY_ID",new Variant(0));
        props.put("INDENT_NO",new Variant(""));
        props.put("SR_NO",new Variant(0));
        props.put("ITEM_CODE",new Variant(""));
        props.put("ITEM_EXTRA_DESC",new Variant(""));
        props.put("REQ_QTY",new Variant(0));
        props.put("UNIT",new Variant(0));
        props.put("BAL_QTY",new Variant(0));
        props.put("PO_QTY",new Variant(0));
        props.put("TOTAL_REQ_QTY",new Variant(0));
        props.put("ALLOCATED_QTY",new Variant(0));
        props.put("CANCELED",new Variant(false));
        props.put("REQ_QTY",new Variant(""));
        props.put("STOCK_QTY",new Variant(0));
        props.put("NET_AMT",new Variant(0));
        props.put("RATE",new Variant(0));
        props.put("MR_NO",new Variant(""));
        props.put("MR_SR_NO",new Variant(0));
        props.put("REQUIRED_DATE",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("OTHER_COMPANY_STOCK",new Variant(0));
        props.put("LAST_SUPP_ID",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("PEND_INSP_QTY",new Variant(0));
        props.put("PEND_INDENT_QTY",new Variant(0));
        props.put("PEND_PO_QTY",new Variant(0));
        props.put("LAST_PO_NO",new Variant(""));
        props.put("LAST_PO_DATE",new Variant(""));
        props.put("LAST_PO_QTY",new Variant(0));
        props.put("LAST_GRN_NO",new Variant(""));
        props.put("LAST_GRN_DATE",new Variant(""));
        props.put("LAST_GRN_QTY",new Variant(0));
        props.put("LAST_GRN_RATE",new Variant(0));
        props.put("AA_INDENT_QTY",new Variant(0));
    }

}