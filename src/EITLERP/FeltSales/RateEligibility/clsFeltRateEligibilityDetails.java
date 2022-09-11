/*
 * clsFeltAutoPISelectionDetails.java
 * This class is used for holding the data of Felt Production Finishing in HashMap
 * 
 * Created on August 22, 2013, 11:20 AM
 */

package EITLERP.FeltSales.RateEligibility;

import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Vivek Kumar
 */
public class clsFeltRateEligibilityDetails{
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
    
    
    /** Creates new clsFeltAutoPISelectionDetails */
    public clsFeltRateEligibilityDetails() {
        props=new HashMap();
        
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
//        props.put("PIECE_NO_HEADER",new Variant(""));
//        props.put("PARTY_CODE_HEADER", new Variant(""));
//        props.put("PARTY_NAME_HEADER",new Variant(""));
//        props.put("UPN_HEADER",new Variant(""));
//        props.put("GROUP_CODE_HEADER",new Variant(""));
//        props.put("GROUP_NAME_HEADER",new Variant(""));
        props.put("SELECTED_FLAG",new Variant(""));
        props.put("PIECE_NO",new Variant(""));
        props.put("PARTY_CODE", new Variant(""));
        props.put("PARTY_NAME",new Variant(""));
        props.put("UPN",new Variant(""));
        props.put("GROUP_CODE",new Variant(""));
        props.put("GROUP_NAME",new Variant(""));
//        props.put("BOOKING_DATE_FROM",new Variant(""));
//        props.put("BOOKING_DATE_TO",new Variant(""));
//        props.put("DESPATCH_DATE",new Variant(""));
        props.put("MACHINE_NO",new Variant(""));
        props.put("POSITION_NO",new Variant(""));
        props.put("POSITION_DESC",new Variant(""));
        props.put("PRODUCT_CODE",new Variant(""));
        props.put("PRODUCT_GROUP",new Variant(""));
        
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("APPROVED",new Variant(0));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED",new Variant(0));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("CANCELED", new Variant(0));        
        props.put("HIERARCHY_ID",new Variant(0));        
    }
}