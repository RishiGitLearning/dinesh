/*
 * clsFeltWeavingDetails.java
 * This class is used for holding the data of Felt Weaving Production in HashMap
 * 
 * Created on March 14, 2013, 1:09 PM
 */

package EITLERP.Sales;

import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Ashutosh
 */
public class clsLCPartyDetails{
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
    public clsLCPartyDetails() {
        props=new HashMap();
        props.put("LC_PARTY",new Variant(""));
        props.put("LC_PARTY_NAME",new Variant(""));
        props.put("LC_PARTY_STATE",new Variant(""));
        props.put("LC_CHARGE_CODE",new Variant(""));
        //props.put("LC_REASON",new Variant(""));        
   
 
   }
}