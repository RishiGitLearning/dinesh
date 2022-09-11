/*
 * clsFeltReopenBaleSlipDetails.java
 * This class is used for holding the data of FELT PACKING in HashMap
 * 
 * Created on July 19, 2013, 5:26 PM
 */

package EITLERP.FeltSales.ReopenBale12;

import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Vivek Kumar
 */
public class clsFeltReopenBaleDetails{
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
    
    /** Creates new clsFeltReopenBaleDetails */
    public clsFeltReopenBaleDetails() {
        props=new HashMap();
        props.put("DOC_NO",new Variant(""));
        props.put("BALE_NO",new Variant(""));
        props.put("BALE_DATE",new Variant(""));
        props.put("PIECE_NO",new Variant(""));
        props.put("LENGTH",new Variant(0.00));
        props.put("WIDTH",new Variant(0.00));
        props.put("WEIGHT",new Variant(0.00));
        props.put("GSM",new Variant(0));
        props.put("SQM",new Variant(""));
        props.put("SYN_PER",new Variant(""));
        props.put("STYLE",new Variant(""));
        props.put("PRODUCT_CODE",new Variant(""));
        props.put("MCN_POSITION_DESC",new Variant(""));
        props.put("MACHINE_NO",new Variant(""));
        props.put("ORDER_NO",new Variant(""));
        props.put("ORDER_DATE",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));        
    }
}