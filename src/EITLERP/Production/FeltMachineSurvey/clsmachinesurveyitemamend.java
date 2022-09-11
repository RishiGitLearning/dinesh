/*
 *
 * clsMRItem.java
 *
 * Created on April 19, 2004, 2:27 PM
 */

package EITLERP.Production.FeltMachineSurvey;


import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;

/**
 *
 * @author  jadave
 * @version
 */

public class clsmachinesurveyitemamend {
    
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
    
    
    /** Creates new clsMRItem */
    public clsmachinesurveyitemamend() {
        props=new HashMap();
        props.put("SR_NO",new Variant(0));
        props.put("MP_AMD_PARTY_CODE",new Variant(""));
        props.put("MP_AMD_MACHINE_NO",new Variant(""));
        props.put("MP_AMD_POSITION",new Variant(""));
        props.put("MP_AMD_POSITION_DESC",new Variant(""));
        props.put("MP_AMD_COMBINATION_CODE", new Variant(""));
        props.put("MP_AMD_ORDER_LENGTH",new Variant(""));
        props.put("MP_AMD_ORDER_WIDTH",new Variant(""));
        props.put("MP_AMD_ORDER_SIZE",new Variant(""));
        props.put("MP_AMD_PRESS_TYPE",new Variant(""));
        props.put("MP_AMD_GSM_RANGE",new Variant(""));
        props.put("MP_AMD_MAX_FELT_LENGTH",new Variant(""));
        props.put("MP_AMD_MIN_FELT_LENGTH",new Variant(""));
        props.put("MP_AMD_LINEAR_NIP_LOAD",new Variant(""));
        props.put("MP_AMD_PAPERGRADE",new Variant(""));
        props.put("MP_AMD_PAPERGRADE_CODE",new Variant(""));
        props.put("MP_AMD_PAPERGRADE_DESC",new Variant(""));
        props.put("MP_AMD_FURNISH",new Variant(""));
        props.put("MP_AMD_TYPE",new Variant(""));
        props.put("MP_AMD_SPEED",new Variant(""));
        props.put("MP_AMD_SURVEY_DATE",new Variant(""));
        props.put("MP_AMD_WIRE_LENGTH",new Variant(""));
        props.put("MP_AMD_WIRE_WIDTH",new Variant(""));
        props.put("MP_AMD_WIRE_TYPE",new Variant(""));
        props.put("MP_AMD_TECH_REP",new Variant(""));
        props.put("MP_AMD_TYPE_OF_FILLER",new Variant(""));
        props.put("MP_AMD_PAPER_DECKLE",new Variant(""));
        props.put("MP_AMD_MCH_ACTIVE",new Variant(""));
        props.put("MP_AMD_NO",new Variant(""));
        props.put("MP_AMD_REASON",new Variant(""));
        props.put("MP_AMD_LIFE_OF_FELT",new Variant(""));
        props.put("MP_AMD_CONSUMPTION",new Variant(""));
        props.put("MP_AMD_DINESH_SHARE",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
    }
    
}
