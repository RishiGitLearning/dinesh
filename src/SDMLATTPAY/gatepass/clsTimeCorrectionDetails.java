/*
 * clsTimeCorrectinDetails.java
 * This class is used for holding the data of Felt Needling Production in HashMap
 * 
 * Created on April 15, 2013, 5:23 PM
 */

package SDMLATTPAY.gatepass;

import EITLERP.Production.FeltNeedling.*;
import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Ashutosh Pathak
 */
public class clsTimeCorrectionDetails{
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
    
    
    /** Creates new clsTimeCorrectionDetails */
    public clsTimeCorrectionDetails() {
        props=new HashMap();
        props.put("CORRECTION_DOC_NO",new Variant(""));
        props.put("CORRECTION_DOC_DATE",new Variant(""));
        
        props.put("EMP_CODE",new Variant(""));
        props.put("P_TIME",new Variant(""));
        props.put("A_DATE",new Variant(""));        
        props.put("MACHINE",new Variant(""));
        props.put("NEW_TIME",new Variant(""));
        props.put("GATEPASS_TYPE",new Variant(""));
        props.put("ATC_SHIFT", new Variant(""));
        props.put("ATC_FIRST_HALF_GRACE_MINUTE", new Variant(""));
        props.put("ATC_SECOND_HALF_GRACE_MINUTE", new Variant(""));
        
        props.put("LC_TIME",new Variant(""));
        props.put("FIRST_HALF",new Variant(""));
        props.put("SECOND_HALF",new Variant(""));
        props.put("SANCTION_REMARKS",new Variant(""));
        
        props.put("PUNCH",new Variant(""));
        props.put("UPDATED_PUNCH_DATE",new Variant(""));

        props.put("ALL_PUNCHES_ONDATE",new Variant(""));
        props.put("ALL_PUNCHES_BEFOREDATE",new Variant(""));
        props.put("ALL_PUNCHES_AFTERDATE",new Variant(""));
        
        props.put("CORRECTION_REMARK",new Variant(""));
        
    }
}
