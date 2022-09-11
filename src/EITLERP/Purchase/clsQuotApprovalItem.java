/*
 * clsNoDataObject.java
 *
 * Created on April 6, 2004, 9:32 AM
 */

package EITLERP.Purchase;


import java.util.*; 
import java.sql.*;
import java.net.*;
import EITLERP.*;
import EITLERP.Stores.*;
  
/**
 *
 * @author  nrpatel
 * @version
 */

public class clsQuotApprovalItem {
    
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
    
    
    /** Creates new clsNoDataObject */
    public clsQuotApprovalItem() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("APPROVAL_NO",new Variant(0));
        props.put("SR_NO",new Variant(0));
        props.put("INQUIRY_NO",new Variant(""));
        props.put("INQUIRY_SR_NO",new Variant(0));
        props.put("QUOT_ID",new Variant(""));
        props.put("QUOT_SR_NO",new Variant(0));
        props.put("SUPP_ID",new Variant(""));
        props.put("ITEM_CODE",new Variant(""));
        props.put("HSN_SAC_CODE",new Variant(""));
        props.put("MAKE",new Variant(""));
        props.put("PRICE_LIST_NO",new Variant(""));
        props.put("QTY",new Variant(0));
        props.put("RATE",new Variant(0));
        props.put("LAND_COST",new Variant(0));
        props.put("LAST_PO_NO",new Variant(""));
        props.put("LAST_PO_DATE",new Variant(""));
        props.put("LAST_LANDED_RATE",new Variant(0));
        props.put("LAST_PO_RATE",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVED",new Variant(false));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("NET_AMOUNT",new Variant(0));
        props.put("TAX_AMOUNT",new Variant(0));
    }
    
}
