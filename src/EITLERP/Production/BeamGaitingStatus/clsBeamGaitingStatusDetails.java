/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.Production.BeamGaitingStatus;

import EITLERP.Production.BeamGaitingStatus.*;
import EITLERP.Variant;
import java.util.HashMap;

/**
 *
 * @author  DAXESH PRAJAPATI
 *
 */
public class clsBeamGaitingStatusDetails {
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
    public clsBeamGaitingStatusDetails() {
        props=new HashMap();
        
        props.put("BGS_DOC_NO",new Variant(""));
        props.put("BGS_DOC_DATE",new Variant(""));
        props.put("SR_NO",new Variant(""));
        props.put("PRODUCT_GROUP",new Variant(""));
        props.put("LOOM_NO",new Variant(""));
        props.put("GAITING_STATUS",new Variant(""));
        props.put("BEAM_GETTING_STARTED_ACTUAL",new Variant(""));
        props.put("BEAM_GETTING_DATE_AS_PER_PLAN",new Variant(""));
        props.put("TARGETTED_BEAM_GAITING_COMPLETE",new Variant(""));
        props.put("ACTUAL_BEAM_GAITING_COMPLETION_DATE",new Variant(""));        
        props.put("PI_BEAM_LOADING",new Variant(""));
        props.put("PII_KNOTTING_DRAWING",new Variant(""));
        props.put("PIII_FRWD_KONTS",new Variant(""));
        props.put("PIV_CLOTH_START",new Variant(""));
        props.put("REMARKS",new Variant(""));
        
                
   }
}