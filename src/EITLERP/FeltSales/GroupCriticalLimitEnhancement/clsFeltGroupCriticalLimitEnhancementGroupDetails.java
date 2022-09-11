/*
 * clsFeltPackingSlipDetails.java
 * This class is used for holding the data of FELT PACKING in HashMap
 * 
 * Created on July 19, 2013, 5:26 PM
 */

package EITLERP.FeltSales.GroupCriticalLimitEnhancement;

import EITLERP.FeltSales.GroupMaster.*;
import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Vivek Kumar
 */
public class clsFeltGroupCriticalLimitEnhancementGroupDetails{
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
    
    /** Creates new clsFeltPackingDetails */
    public clsFeltGroupCriticalLimitEnhancementGroupDetails() {
        props=new HashMap();
        
        props.put("GROUP_PARTY_CODE",new Variant(""));
        props.put("GROUP_PARTY_NAME",new Variant(""));
        props.put("GROUP_PARTY_CRITICAL_LIMIT",new Variant(""));
        props.put("GROUP_ENHANCE_PARTY_CRITICAL_LIMIT",new Variant(""));
        props.put("GROUP_PARTY_OUTSTANDING",new Variant(0));
        
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));        
    }
}