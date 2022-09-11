/*
 * clsIssueGenItem.java
 *
 * Created on April 26, 2004, 10:21 AM
 */

package EITLERP.Stores;

import java.sql.*;
import java.net.*;
import java.util.*;
import EITLERP.*;
  
/**
 *
 * @author  nhpatel
 * @version 
 */ 
public class clsIssueGenItem {
    
    private HashMap props;
    public boolean Ready=false;
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
    
    /** Creates new clsIssueGenItem */
    public clsIssueGenItem() {
        props = new HashMap();
        
        props.put("COMPANY_ID",new Variant(0));
        props.put("ISSUE_NO",new Variant(""));
        props.put("SR_NO",new Variant(0));
        props.put("ITEM_CODE",new Variant(""));
        props.put("ITEM_EXTRA_DESC",new Variant(""));
        props.put("WAREHOUSE_ID",new Variant(""));
        props.put("LOCATION_ID",new Variant(""));
        props.put("BOE_NO",new Variant(""));
        props.put("BOE_SR_NO",new Variant(""));
        props.put("QTY",new Variant(0));
        props.put("SALES_QTY",new Variant(0));
        
        props.put("UNIT",new Variant(0));
        props.put("SHADE",new Variant(""));
        props.put("COST_CENTER_ID",new Variant(0));
        props.put("EXCESS_ISSUE_QTY",new Variant(0));
        props.put("ISSUE_DESC",new Variant(""));
        props.put("QTY_REQD",new Variant(0));
        props.put("ISSUED_QTY",new Variant(0));
        props.put("REQ_NO",new Variant(""));
        props.put("REQ_SRNO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("LF_NO",new Variant(""));
        props.put("CANCELED",new Variant(false));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFEID_DATE",new Variant(""));
        props.put("GRN_NO",new Variant(""));
        props.put("GRN_SR_NO",new Variant(0));
        props.put("GRN_TYPE",new Variant(0));
        props.put("MIR_NO",new Variant(""));
        props.put("MIR_SR_NO",new Variant(0));
        props.put("MIR_TYPE",new Variant(0));
        props.put("ZERO_VAL_QTY",new Variant(0));
        props.put("ISSUE_VALUE",new Variant(0));
        props.put("RATE",new Variant(0));
        props.put("STOCK_QTY",new Variant(0));
        
        props.put("ISSUE_CAT",new Variant(""));
        props.put("ISSUE_CAT_DESC",new Variant(""));
    }

}
