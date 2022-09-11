/*
 * clsFeltPackingSlipDetails.java
 * This class is used for holding the data of FELT PACKING in HashMap
 * 
 * Created on July 19, 2013, 5:26 PM
 */

package EITLERP.FeltSales.SalesReturns;

import EITLERP.FeltSales.GroupMaster.*;
import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Vivek Kumar
 */
public class clsFeltSalesReturnsDetails{
    private HashMap props;
    public boolean Ready = false;
    
    public Variant getAttribute(String PropName) {
        return (Variant) props.get(PropName);
    }
    
    public void setAttribute(String PropName,Object Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,int Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,long Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,double Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,float Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,boolean Value) {
        props.put(PropName,new Variant(Value));
    }    
    
    /** Creates new clsFeltPackingDetails */
    public clsFeltSalesReturnsDetails() {
        props=new HashMap();
        props.put("DOC_NO",new Variant(""));
        props.put("PIECE_NO",new Variant(0.00));
        props.put("INVOICE_NO",new Variant(0.00));
        props.put("INVOICE_DATES",new Variant(0.00));
        props.put("PARTY_CODE",new Variant(0.00));
        props.put("PARTY_NAME",new Variant(0.00));
        props.put("WIDTH",new Variant(0.00));
        props.put("LENGTH",new Variant(0.00));
        props.put("TOTAL_GROSS",new Variant(0.00));
        props.put("TOTAL_NET_AMOUNT",new Variant(0.00));
        props.put("GROSS_SQ_MTR",new Variant(0.00));
        props.put("GROSS_KG",new Variant(0.00));
        props.put("GROSS_AMOUNT",new Variant(0.00));
        props.put("TRD_DISCOUNT",new Variant(0.00));
        props.put("NET_AMOUNT",new Variant(0.00));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("NEW_PIECE_NO",new Variant(""));
        props.put("RETURN_CATEGORY",new Variant(""));
        props.put("OC_MONTH",new Variant(""));
        props.put("OBSOLETE_UPN_ASSIGN_STATUS",new Variant(""));
        props.put("SCRAP_REASON",new Variant(""));
        props.put("UNMAPPED_REASON",new Variant(""));
        props.put("CATEGORY_CONDITION",new Variant(""));
        props.put("CONDITION_STATUS",new Variant(""));
    }
}