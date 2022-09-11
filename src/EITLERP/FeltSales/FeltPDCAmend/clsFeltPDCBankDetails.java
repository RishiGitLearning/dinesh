/*
 * clsFeltWeavingDetails.java
 * This class is used for holding the data of Felt Weaving Production in HashMap
 * 
 * Created on March 14, 2013, 1:09 PM
 */

package EITLERP.FeltSales.FeltPDCAmend;

import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Vivek Kumar
 */
public class clsFeltPDCBankDetails{
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
    public clsFeltPDCBankDetails() {
        props=new HashMap();
        props.put("PDC_DOC_NO",new Variant(""));
        props.put("PDC_BANK_CD",new Variant(""));
        props.put("PDC_BANK_NAME",new Variant(""));
        props.put("PDC_BANK_BRANCH",new Variant(""));
        props.put("PDC_CHEQUE_NO",new Variant(""));
        props.put("PDC_CHEQUE_DATE",new Variant(""));
        props.put("PDC_CHEQUE_AMOUNT",new Variant(0.00));
        props.put("PDC_PHYSICAL_SCANNED",new Variant(""));
        props.put("PDC_BILLING_DATE",new Variant(""));
        props.put("PDC_UTILIZED_AMOUNT",new Variant(0.00));
        props.put("PDC_BALANCE_AMOUNT",new Variant(0.00));
        props.put("PDC_CLOSED",new Variant(""));
        props.put("PDC_REMARKS",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));        
    }
}