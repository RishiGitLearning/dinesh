/*
 * clsFeltWeavingDetails.java
 * This class is used for holding the data of Felt Weaving Production in HashMap
 * 
 * Created on March 14, 2013, 1:09 PM
 */

package EITLERP;

import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Vivek Kumar
 */
public class clsInvoiceDateUpdDetails{
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
    public clsInvoiceDateUpdDetails() {
        props=new HashMap();
        props.put("SR_NO",new Variant(""));
        props.put("UPD_NO",new Variant(""));
        props.put("UPD_DATE",new Variant(""));
        props.put("UPD_PARTY_CODE",new Variant(""));
        props.put("UPD_MAIN_CODE",new Variant(""));
        props.put("UPD_INVOICE_NO",new Variant(""));
        props.put("UPD_INVOICE_DATE",new Variant(""));
        props.put("UPD_AGENT_SR_NO",new Variant(0.00));
        props.put("UPD_NET_AMOUNT",new Variant(0.00));
        props.put("UPD_DUE_DATE",new Variant(""));
        props.put("UPD_NEW_DATE",new Variant(""));
        props.put("UPD_NEW_DUE_DATE",new Variant(""));
    }
}