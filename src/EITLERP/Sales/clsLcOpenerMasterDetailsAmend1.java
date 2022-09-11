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
 * @author  Vivek Kumar
 */
public class clsLcOpenerMasterDetailsAmend1{
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
    public clsLcOpenerMasterDetailsAmend1() {
        props=new HashMap();
        props.put("COPANY_ID",new Variant(""));
        props.put("LCO_AMD_NO", new Variant(""));
        props.put("LCO_OPENER_CODE", new Variant(""));
        props.put("LCO_OPENER_NAME", new Variant(""));
        props.put("LCO_ADDRESS1", new Variant(""));
        props.put("LCO_BANK_ID", new Variant(""));  
        props.put("LCO_BANK_NAME", new Variant(""));
        props.put("LCO_BANK_ADDRESS", new Variant(""));
        props.put("LCO_BANK_CITY", new Variant(""));
        props.put("LCO_INST1", new Variant(""));
        props.put("LCO_INST2", new Variant(""));  
        props.put("LCO_BNKHUN", new Variant(""));
        props.put("LCO_LOCADD1", new Variant(""));
        props.put("LCO_LOCADD2", new Variant(""));  
        props.put("LCO_AMT", new Variant(""));
        props.put("LCO_LOCALBANK", new Variant(""));
        props.put("LCO_EXPIRY_DATE",new Variant("0000-00-00"));
       
    }
}