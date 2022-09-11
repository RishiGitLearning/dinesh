/*
 * clsFeltInvCvrLtrDetails.java
 * This class is used for holding the data of Felt Production Finishing in HashMap
 * 
 * Created on August 22, 2013, 11:20 AM
 */

package EITLERP.FeltSales.FeltInvCvrLtr;

import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Vivek Kumar
 */
public class clsFeltInvCvrLtrDetails{
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
    
    
    /** Creates new clsFeltInvCvrLtrDetails */
    public clsFeltInvCvrLtrDetails() {
        props=new HashMap();
//        props.put("DOC_NO",new Variant(""));
//        props.put("DOC_DATE",new Variant(""));        
//        props.put("LR_NO",new Variant(""));
//        props.put("LR_DATE",new Variant(""));
        
        props.put("CARRIER",new Variant(""));
        props.put("INVOICE_NO",new Variant(""));
        props.put("INVOICE_DATE",new Variant(""));
        props.put("PIECE_NO",new Variant(""));
        props.put("PARTY_CODE", new Variant(""));
        props.put("PARTY_NAME",new Variant(""));
        props.put("CORR_ADDRESS",new Variant(""));
        props.put("STATION",new Variant(""));
        props.put("MACHINE_NO",new Variant(""));
        props.put("POSITION_NO",new Variant(""));
        props.put("POSITION_DESC",new Variant(""));
        props.put("PRODUCT_CODE",new Variant(""));
        props.put("LENGTH",new Variant(0.00));
        props.put("WIDTH",new Variant(0.00));
        props.put("WEIGHT",new Variant(0.00));
        props.put("REMARKS",new Variant(""));
        props.put("FINAL_APPROVAL_DATE",new Variant(""));        
        
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