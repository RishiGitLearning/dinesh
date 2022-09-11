/*
 * clsFeltWeavingDetails.java
 * This class is used for holding the data of Felt Weaving Production in HashMap
 * 
 * Created on March 14, 2013, 1:09 PM
 */

package EITLERP.FeltSales.Felt_Consignment;

import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Vivek Kumar
 */
public class clsFeltConsignmentDetails{
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
    public clsFeltConsignmentDetails() {
        props=new HashMap();
        props.put("CON_NO",new Variant(""));
        props.put("CON_DOC_DATE",new Variant(""));
        props.put("CON_PARTY_CODE",new Variant(""));
        props.put("CON_PARTY_NAME",new Variant(""));
        props.put("CON_PIECE_NO",new Variant(""));
        props.put("CON_PRODUCT_CODE",new Variant(""));
        props.put("CON_LENGTH",new Variant(0.00));
        props.put("CON_WIDTH",new Variant(0.00));
        props.put("CON_GSM",new Variant(""));
        props.put("CON_AGREED_DATE",new Variant(""));
        props.put("CON_DATE",new Variant(""));
    }
}