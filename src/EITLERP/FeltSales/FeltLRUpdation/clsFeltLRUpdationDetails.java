/*
 * clsFeltLRUpdationDetails.java
 * This class is used for holding the data of Felt LR Updation in HashMap
 * 
 * July 11, 2013, 12:10 PM
 */

package EITLERP.FeltSales.FeltLRUpdation;

import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Vivek Kumar
 */
public class clsFeltLRUpdationDetails{
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
    
    
    /** Creates new clsFeltLRUpdationDetails */
    public clsFeltLRUpdationDetails() {
        props=new HashMap();
        props.put("INVOICE_DATE",new Variant(""));
        props.put("INVOICE_NO",new Variant(""));
        props.put("LR_NO",new Variant(""));
        props.put("LR_DATE",new Variant(""));
        props.put("CARRIER",new Variant(""));
        props.put("POSITION",new Variant(0));
        props.put("FREIGHT",new Variant(0.00));
        props.put("REMARKS",new Variant(""));
    }
}