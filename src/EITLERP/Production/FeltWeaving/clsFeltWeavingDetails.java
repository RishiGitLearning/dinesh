/*
 * clsFeltWeavingDetails.java
 * This class is used for holding the data of Felt Weaving Production in HashMap
 * 
 * Created on March 14, 2013, 1:09 PM
 */

package EITLERP.Production.FeltWeaving;

import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Vivek Kumar
 */
public class clsFeltWeavingDetails{
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
    public clsFeltWeavingDetails() {
        props=new HashMap();
        props.put("PRODUCTION_DATE",new Variant(""));
        props.put("PRODUCTION_FORM_NO",new Variant(""));
        props.put("PRODUCTION_DEPARTMENT",new Variant(""));
        props.put("SERIAL_NO",new Variant(0));
        props.put("PRODUCTION_PIECE_NO",new Variant(""));
        props.put("PRODUCTION_PARTY_CODE",new Variant(""));
        props.put("THORITICAL_WEIGHT",new Variant(0.00));
        props.put("WEIGHT",new Variant(0.00));
        props.put("PICKS_PER_10CMS",new Variant(""));
        props.put("REED_SPACE",new Variant(0.00));
        props.put("LENGTH",new Variant(0.00));
        props.put("LOOM_NO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("WEAVE_DATE",new Variant(""));
        props.put("WARP_NO",new Variant(""));
        props.put("WEAVE_DIFF_DAYS",new Variant(""));
        
        props.put("PICK",new Variant(0.00));
        props.put("PICKMTR",new Variant(0.00));
    }
}