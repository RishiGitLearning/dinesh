/*
 * clsFeltWeavingDetails.java
 * This class is used for holding the data of Felt Weaving Production in HashMap
 * 
 * Created on March 14, 2013, 1:09 PM
 */

package EITLERP.FeltSales.LocationAssignment;

import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Vivek Kumar
 */
public class clsFeltLocationAssignmentDetails{
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
    
    
    /** Creates new clsFeltWeavingDetails */
    public clsFeltLocationAssignmentDetails() {
        props=new HashMap();
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("LOCATION_ID",new Variant(""));
        props.put("SR_NO",new Variant(""));
        props.put("PIECE_NO",new Variant(""));
        props.put("ORDER_DATE",new Variant(""));
        props.put("PRODUCT_CODE",new Variant(""));
        props.put("PARTY_CODE",new Variant(""));
        props.put("PARTY_NAME",new Variant(""));
        props.put("LENGTH",new Variant(0.00));
        props.put("WIDTH",new Variant(0.00));
        props.put("GSM",new Variant(""));
        props.put("WEIGHT",new Variant(""));
        props.put("GROUP",new Variant(""));
        props.put("SYTLE",new Variant(""));
        props.put("REMARK",new Variant(""));
        props.put("MFG_STATUS",new Variant(""));
        props.put("SQMTR",new Variant(""));
        props.put("BILL_LENGTH",new Variant(""));
        props.put("BILL_WIDTH",new Variant(""));
        props.put("BILL_GSM",new Variant(""));
        props.put("BILL_WEIGHT",new Variant(""));
        props.put("BILL_SQMTR",new Variant(""));
        props.put("BILL_PRODUCT_CODE",new Variant(""));
        props.put("INCHARGE_NAME",new Variant(""));
 
   }
}