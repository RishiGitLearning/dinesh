/*
 * clsFeltNeedlingDetails.java
 * This class is used for holding the data of Felt Needling Production in HashMap
 * 
 * Created on April 15, 2013, 5:23 PM
 */

package EITLERP.Production.FeltNeedling;

import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Vivek Kumar
 */
public class clsFeltNeedlingDetails{
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
    
    
    /** Creates new clsFeltNeedlingDetails */
    public clsFeltNeedlingDetails() {
        props=new HashMap();
        props.put("PRODUCTION_DATE",new Variant(""));
        props.put("PRODUCTION_FORM_NO",new Variant(""));
        props.put("PRODUCTION_DEPARTMENT",new Variant(""));        
        props.put("PRODUCTION_PIECE_NO",new Variant(""));
        props.put("PRODUCTION_PARTY_CODE",new Variant(""));
        props.put("WEIGHT",new Variant(0.00));
        props.put("REMARKS",new Variant(""));
        props.put("LOOM_NO",new Variant(0));        
        
        props.put("HEAT_SETTING_OUT_DATE",new Variant(""));
        props.put("HEAT_SETTING_IN_DATE",new Variant(""));
    }
}