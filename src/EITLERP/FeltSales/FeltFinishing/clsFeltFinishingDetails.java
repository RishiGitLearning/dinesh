/*
 * clsFeltFinishingDetails.java
 * This class is used for holding the data of Felt Production Finishing in HashMap
 * 
 * Created on August 22, 2013, 11:20 AM
 */

package EITLERP.FeltSales.FeltFinishing;

import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Vivek Kumar
 */
public class clsFeltFinishingDetails{
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
    
    
    /** Creates new clsFeltFinishingDetails */
    public clsFeltFinishingDetails() {
        props=new HashMap();
        props.put("PRODUCTION_DATE",new Variant(""));
        props.put("PRODUCTION_FORM_NO",new Variant(""));
        props.put("HEADER_REMARK", new Variant(""));
        props.put("PRODUCTION_DEPARTMENT",new Variant(""));
        props.put("SERIAL_NO",new Variant(0));
        props.put("PRODUCTION_PIECE_NO",new Variant(""));
        props.put("PRODUCTION_PARTY_CODE",new Variant(""));
        props.put("LENGTH",new Variant(0.00));
        props.put("WIDTH",new Variant(0.00));
        props.put("WEIGHT",new Variant(0.00));
        props.put("TAG_WEIGHT",new Variant(0.00));
        props.put("STYLE_CODE",new Variant(""));
        props.put("BILL_STYLE_CODE",new Variant(""));
        props.put("REMARKS",new Variant(""));
        
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("APPROVED",new Variant(0));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED",new Variant(0));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("CANCELED", new Variant(0));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
    }
}