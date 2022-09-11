/*
 * clsFeltPartyExtraDetails.java
 * This class is used for holding the data of Felt Party Extra Details in HashMap
 *
 * Created on June 15, 2013, 1:08 PM
 */

package EITLERP.Production.FeltPartyExtraDetail;

import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Vivek Kumar
 */
class clsFeltPartyExtraDetails{
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
    
    public clsFeltPartyExtraDetails() {
        props=new HashMap();
        props.put("PARTY_CODE",new Variant(""));
        props.put("CONTACT_PERSON",new Variant(""));
        props.put("MOBILE",new Variant(""));
        props.put("LANDLINE",new Variant(""));
        props.put("EMAIL",new Variant(""));
        props.put("FAX",new Variant(""));
        props.put("OFFICE_ADDRESS",new Variant(""));
        props.put("WORK_ADDRESS",new Variant(""));
    }
    
}