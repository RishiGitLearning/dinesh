/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.Production.WindingProduction;

import EITLERP.Variant;
import java.util.HashMap;

/**
 *
 * @author  DAXESH PRAJAPATI
 *
 */
public class clsWindingProductionDetails {
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
    public clsWindingProductionDetails() {
        props=new HashMap();
        
        props.put("WM_DOC_NO",new Variant(""));
        props.put("WM_DOC_DATE",new Variant(""));
        props.put("SR_NO",new Variant(""));
        props.put("WINDING_MACHINE_NAME",new Variant(""));
        props.put("ACTUAL_TARGET",new Variant(""));
        props.put("SPINDLES",new Variant(""));
        props.put("SHIFT_A_COUNTER",new Variant(""));
        props.put("SHIFT_A_DOFF",new Variant(""));
        props.put("SHIFT_A_KGS",new Variant(""));
        props.put("SHIFT_B_COUNTER",new Variant(""));
        props.put("SHIFT_B_DOFF",new Variant(""));
        props.put("SHIFT_B_KGS",new Variant(""));
        props.put("SHIFT_C_COUNTER",new Variant(""));
        props.put("SHIFT_C_DOFF",new Variant(""));
        props.put("SHIFT_C_KGS",new Variant(""));
        props.put("DAYS_PRODUCTION_COUNTER",new Variant(""));
        props.put("DAYS_PRODUCTION_DOFF",new Variant(""));
        props.put("DAYS_PRODUCTION_KGS",new Variant(""));
        
               
        
   }
}