/*
 * clsFeltWeavingDetails.java
 * This class is used for holding the data of Felt Weaving Production in HashMap
 * 
 * Created on March 14, 2013, 1:09 PM
 */

package EITLERP.Production.FeltYearEndDiscountCreditNote;

import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Vivek Kumar
 */
public class clsFeltYearEndDiscDetails{
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
    public clsFeltYearEndDiscDetails() {
        props=new HashMap();
        
        props.put("YEAR_END_DATE",new Variant(""));
        props.put("YEAR_FROM_DATE",new Variant(""));
        props.put("YEAR_TO_DATE",new Variant(""));
        props.put("YEAR_END_ID",new Variant(""));
        props.put("YEAR_END_PARTY_CODE",new Variant(""));
        props.put("YEAR_END_PARTY_NAME",new Variant(""));
        props.put("YEAR_END_TURN_OVER",new Variant(""));
	props.put("YEAR_END_TARGET_ACHIV",new Variant(""));
	props.put("YEAR_END_YES_NO",new Variant(""));
	props.put("YEAR_END_REMARKS",new Variant(""));
	
        
   }
}