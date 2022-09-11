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
public class clsLcPartyMasterDetails{
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
    public clsLcPartyMasterDetails() {
        props=new HashMap();
        props.put("COPANY_ID",new Variant(""));
        props.put("PARTY_CODE",new Variant(""));
        props.put("LC_NO",new Variant(""));
        props.put("EXP_DATE",new Variant("0000-00-00"));
        props.put("IND",new Variant(""));
        props.put("DISC",new Variant(""));
        props.put("PARTY_NAME",new Variant(""));
        props.put("ADDRESS",new Variant(""));
        props.put("LC_OPENER_CODE",new Variant(""));
        props.put("BANK_ID",new Variant(""));
        props.put("BANK_NAME",new Variant(""));
        props.put("BANK_ADDRESS",new Variant(""));
        props.put("BANK_CITY",new Variant(""));
        props.put("INST1",new Variant(""));
        props.put("INST2",new Variant(""));
        props.put("BNKHUN",new Variant(""));
        props.put("LOCADD1",new Variant(""));
        props.put("LOCADD2",new Variant(""));
        props.put("AMT",new Variant(0.00));
        props.put("BNGDOCIND",new Variant(""));
        props.put("LOCALBANK",new Variant(""));
       
    }
}