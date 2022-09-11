/*
 * clsMRItem.java
 *
 * Created on April 19, 2004, 2:27 PM
 */

package EITLERP.Stores;


import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
 
/**
 *
 * @author  jadave
 * @version 
 */
 
public class clsIssueReqItem {

    private HashMap props;    
    public boolean Ready = false;
    public HashMap colIssueReqLot=new HashMap();
    

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
    public clsIssueReqItem() {
        props=new HashMap();    
        props.put("COMPANY_ID",new Variant(0));
        props.put("REQ_NO", new Variant(""));
        props.put("REQ_TYPE",new Variant(0));
        props.put("SR_NO",new Variant(0));
        props.put("ITEM_CODE",new Variant(""));
        props.put("ITEM_EXTRA_DESC",new Variant(""));
        props.put("COST_CENTER_CODE",new Variant(0));
        props.put("COST_CENTER_NAME",new Variant(""));
        props.put("DEPT_ID",new Variant(0));
        props.put("DEPT_NAME",new Variant(""));
        props.put("REQ_QTY",new Variant(0.00));
        props.put("ISSUED_QTY",new Variant(0.00));
        props.put("BAL_QTY",new Variant(0.00));
        props.put("UNIT_ID",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("CANCELED",new Variant(false));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("REQUIRED_DATE",new Variant(""));
        props.put("MFG_PROG_NO",new Variant(""));
        props.put("BLEND_CODE",new Variant(""));
        
        props.put("ISSUE_CAT",new Variant(""));
        props.put("ISSUE_CAT_DESC",new Variant(""));
    }

}
