/*
 * clsFeltWeavingDetails.java
 * This class is used for holding the data of Felt Weaving Production in HashMap
 * 
 * Created on March 14, 2013, 1:09 PM
 */

package EITLERP.Production.YearEndDiscountForm2;

import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Vivek Kumar
 */
public class clsFeltYearEndDiscDetails2{
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
    public clsFeltYearEndDiscDetails2() {
        props=new HashMap();
        
        props.put("FORM2_YEAR_END_DATE",new Variant(""));
        props.put("FORM2_YEAR_FROM_DATE",new Variant(""));
        props.put("FORM2_YEAR_TO_DATE",new Variant(""));
        props.put("FORM2_YEAR_END_ID",new Variant(""));
        props.put("FORM2_YEAR_END_MAIN_PARTY_CODE",new Variant(""));
        props.put("FORM2_YEAR_END_PARTY_CODE",new Variant(""));
        props.put("FORM2_YEAR_END_PARTY_NAME",new Variant(""));
        props.put("FORM2_YEAR_END_",new Variant(""));
        props.put("FORM2_YEAR_END_TURN_OVER",new Variant(""));
	props.put("FORM2_YEAR_END_TARGET_ACHIV",new Variant(""));
	props.put("FORM2_YEAR_END_YES_NO",new Variant(""));
	props.put("FORM2_YEAR_END_REMARKS1",new Variant(""));
	props.put("FORM2_YEAR_END_REMARKS2",new Variant(""));
        props.put("FORM2_EFFECTIVE_FROM",new Variant(""));
	props.put("FORM2_EFFECTIVE_TO-",new Variant(""));

       }
}