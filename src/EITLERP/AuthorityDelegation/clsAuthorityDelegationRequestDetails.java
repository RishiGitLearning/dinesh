/*
 * clsTimeCorrectinDetails.java
 * This class is used for holding the data of Felt Needling Production in HashMap
 * 
 * Created on April 15, 2013, 5:23 PM
 */

package EITLERP.AuthorityDelegation;

import SDMLATTPAY.leave.*;
import SDMLATTPAY.leave.*;
import EITLERP.Production.FeltNeedling.*;
import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Ashutosh Pathak
 */
public class clsAuthorityDelegationRequestDetails{
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
    public clsAuthorityDelegationRequestDetails() {
        props=new HashMap();
        props.put("AUTH_ACTIVE_MODULE_ID",new Variant(0));
        props.put("AUTH_MODULE_ID",new Variant(0));
        props.put("AUTH_MODULE_DESC",new Variant(""));
        
        props.put("AUTH_NOMINEE1_ID",new Variant(0));
        props.put("AUTH_NOMINEE2_ID",new Variant(0));
        props.put("AUTH_NOMINEE3_ID",new Variant(0));
        props.put("AUTH_NOMINEE1", new Variant(""));
        props.put("AUTH_NOMINEE2", new Variant(""));
        props.put("AUTH_NOMINEE3", new Variant(""));
        props.put("AUTH_PRIORITY", new Variant(""));
        props.put("AUTH_FROM_DATE",new Variant(""));
        props.put("AUTH_TO_DATE",new Variant(""));                
        
        
    }
}