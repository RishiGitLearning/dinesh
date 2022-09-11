/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.Production.WarpingStatusOfWeavingLoom;

import EITLERP.Variant;
import java.util.HashMap;

/**
 *
 * @author  DAXESH PRAJAPATI
 *
 */
public class clsWarpingStatusOfWeavingLoomDetails {
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
    public clsWarpingStatusOfWeavingLoomDetails() {
        props=new HashMap();
        
        props.put("SR_NO",new Variant(""));
        props.put("WS_DOC_NO",new Variant(""));
        props.put("WS_DOC_DATE",new Variant(""));
        props.put("PRODUCT_GROUP",new Variant(""));
        props.put("LOOM_NO",new Variant(""));
        props.put("WARPING_STARTED_ACTUAL",new Variant(""));
        props.put("WARPING_DATE_AS_PLAN",new Variant(""));
        props.put("COMPLETION_DATE_AS_ON_TODAYS_STATUS",new Variant(""));
        props.put("PHASE_1_CREELING",new Variant(""));
        props.put("WARPING_STATUS",new Variant(""));
        props.put("WARPING_COMPLETE_TARGET",new Variant(""));
        props.put("PHASE_2_WARPING_ACT_KGS",new Variant(""));
        props.put("PHASE_2_WARPING_OUT_OF_KGS",new Variant(""));
        props.put("PHASE_2_WARPING",new Variant(""));
        props.put("PHASE_3_BEAMING_ACT_KGS",new Variant(""));
        props.put("PHASE_3_BEAMING_OUT_KGS",new Variant(""));
        props.put("PHASE_3_BEAMING",new Variant(""));
        props.put("REMARKS",new Variant(""));
   }
}