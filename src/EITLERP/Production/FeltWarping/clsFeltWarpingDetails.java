/*
 * clsFeltWarpingDetails.java
 * This class is used for holding the data of Felt Warping Production in HashMap
 * 
 * Created on March 14, 2013, 1:09 PM
 */

package EITLERP.Production.FeltWarping;

import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Vivek Kumar
 */
public class clsFeltWarpingDetails{
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
        
    public clsFeltWarpingDetails() {
        props=new HashMap();
        props.put("PRODUCTION_PIECE_NO",new Variant(""));
        props.put("LOOM_NO",new Variant(0));
        props.put("REMARKS",new Variant(""));
    }
}