/*
 * clsMIRRawItemDetail.java
 *
 * Created on May 03, 2004, 9:31 AM
 */

package EITLERP.Stores;


import java.util.*;
import java.net.*;
import EITLERP.*;
 
/**
 *
 * @author  jadave
 * @version
 */
 
public class clsMIRRawItemDetail {
    
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
    
    
    /** Creates new clsMIRGenItemDetail */
    public clsMIRRawItemDetail() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("MIR_NO", new Variant(""));
        props.put("MIR_SR_NO",new Variant(0));
        props.put("SR_NO", new Variant(0));
        props.put("ITEM_ID", new Variant(""));
        props.put("ITEM_LOT_NO",new Variant(""));
        props.put("AUTO_LOT_NO",new Variant(""));
        props.put("LOT_RECEIVED_QTY",new Variant(0));
        props.put("LOT_REJECTED_QTY",new Variant(0));
        props.put("LOT_ACCEPTED_QTY",new Variant(0));
        props.put("MIR_TYPE",new Variant(2));
        props.put("APPROVED",new Variant(0));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("CANCELLED",new Variant(0));
        props.put("CHANGED",new Variant(0));
        props.put("CHANGED_DATE",new Variant(""));
    }
}
