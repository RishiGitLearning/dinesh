/*
 * clsFeltPackingSlipDetails.java
 * This class is used for holding the data of FELT PACKING in HashMap
 * 
 * Created on July 19, 2013, 5:26 PM
 */

package EITLERP.FeltSales.FeltPacking;

import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Vivek Kumar
 */
public class clsFeltPackingDetails{
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
    public clsFeltPackingDetails() {
        props=new HashMap();
        props.put("PKG_DP_NO",new Variant(""));
        props.put("PKG_BALE_NO",new Variant(""));
        props.put("PKG_BALE_DATE",new Variant(""));
        props.put("PKG_PIECE_NO",new Variant(""));
        props.put("PKG_LENGTH",new Variant(0.00));
        props.put("PKG_WIDTH",new Variant(0.00));
        props.put("PKG_WEIGHT",new Variant(0.00));
        props.put("PKG_GSM",new Variant(0.00));
        props.put("PKG_SQM",new Variant(0.00));
        props.put("PKG_STYLE",new Variant(""));
        props.put("PKG_PRODUCT_CODE",new Variant(""));
        props.put("PKG_PRODUCT_DESC",new Variant(""));
        props.put("PKG_MCN_POSITION_DESC",new Variant(""));
        props.put("PKG_MACHINE_NO",new Variant(""));
        props.put("PKG_ORDER_NO",new Variant(""));
        props.put("PKG_ORDER_DATE",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));        
    }
}