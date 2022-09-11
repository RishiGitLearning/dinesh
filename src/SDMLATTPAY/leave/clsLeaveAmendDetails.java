/*
 * clsTimeCorrectinDetails.java
 * This class is used for holding the data of Felt Needling Production in HashMap
 * 
 * Created on April 15, 2013, 5:23 PM
 */

package SDMLATTPAY.leave;

import SDMLATTPAY.leave.*;
import EITLERP.Production.FeltNeedling.*;
import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Ashutosh Pathak
 */
public class clsLeaveAmendDetails{
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
    public clsLeaveAmendDetails() {
        props=new HashMap();
        props.put("LVT_LEAVE_CODE",new Variant(""));
        //props.put("LVT_LEAVE_TYPE",new Variant(""));
        props.put("LVT_MENTION_TIME",new Variant(""));
        
        props.put("LVT_DAYS",new Variant(0));
        props.put("LVT_FROMDATE",new Variant(""));
        props.put("LVT_TODATE",new Variant(""));                
        props.put("AMEND_TYPE",new Variant(""));                
        
    }
}