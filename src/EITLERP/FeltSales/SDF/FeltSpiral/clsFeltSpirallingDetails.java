/*
 * clsFeltMendingDetails.java
 * This class is used for holding the data of Felt Mending Production in HashMap
 * 
 * Created on April 24, 2013, 6:19 PM
 */

package EITLERP.FeltSales.SDF.FeltSpiral;

import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Vivek Kumar
 */
public class clsFeltSpirallingDetails{
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
    
    public clsFeltSpirallingDetails() {
        props=new HashMap();
        props.put("PRODUCTION_DATE",new Variant(""));
        props.put("PRODUCTION_FORM_NO",new Variant(""));
        props.put("PRODUCTION_DEPARTMENT",new Variant(""));
        props.put("SERIAL_NO",new Variant(0));
        props.put("PRODUCTION_PIECE_NO",new Variant(""));
        props.put("PRODUCTION_PARTY_CODE",new Variant(""));
        props.put("WEIGHT",new Variant(0.00));                
        props.put("GROUP",new Variant(""));
        props.put("REMARKS",new Variant(""));
    }
}