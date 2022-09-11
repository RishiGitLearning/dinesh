/*
 * clsFeltWeavingLoomDetails.java
 * This class is used for holding the data of Felt Weaving Production in HashMap
 * 
 * Created on Jan 22, 2015, 11:09 PM
 */

package EITLERP.Production.FeltWeavingLoom;

import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Dhaval Rahevar & Rishi Raj Neekhra
 */
public class clsFeltWeavingLoomDetails{
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

    public clsFeltWeavingLoomDetails() {
        props=new HashMap();
        props.put("WLO_WVGPROD_NO",new Variant(""));
        props.put("WLO_WEAVING_DATE",new Variant(""));
        props.put("WLO_SHIFT",new Variant(""));
        props.put("WLO_LOOM_NO",new Variant(""));
        props.put("WLO_RPM",new Variant(""));
        props.put("WLO_WRAP_NO",new Variant(""));
        props.put("WLO_READ_SPACE",new Variant(""));
        props.put("WLO_GROUP",new Variant(0.00));
        props.put("WLO_ACTUAL_PICS",new Variant(""));
        props.put("WLO_THERICAL_PICS",new Variant(0.00));
        props.put("WLO_TARGATEDEFF",new Variant(""));
        props.put("WLO_NOWARP_TIME",new Variant(""));
        props.put("WLO_NOWEFT",new Variant(""));
        props.put("WLO_NOPOWER_TIME",new Variant(""));
        props.put("WLO_NOAIR",new Variant(""));
        props.put("WLO_BEAMGAITING_TIME",new Variant(""));
        props.put("WLO_NOWEAVER_TIME",new Variant(""));
        props.put("WLO_BEAM_NO",new Variant(""));
        props.put("WLO_CHANGE_TIME",new Variant(""));
        props.put("WLO_OTHER",new Variant(""));
        props.put("WLO_WEAVING_TIME",new Variant(""));
        props.put("WLO_LESS",new Variant(""));
        props.put("WLO_SHORT",new Variant(""));
        props.put("SUPER_NO",new Variant(""));
        props.put("NAME_SUPER",new Variant(""));
        props.put("WLO_WEAVING_CARDNO",new Variant(""));        
        props.put("WLO_REMARKS",new Variant(""));
        props.put("WLO_SHUTTEL_REPAIR_TIME",new Variant(""));
        props.put("WLO_CLOTH_REPAIR_TIME",new Variant(""));
        props.put("WLO_PICKING_REPAIR_TIME",new Variant(""));
        props.put("WLO_TEMPLE_REPAIR_TIME",new Variant(""));
        props.put("WLO_WARP_END_REPAIR_TIME",new Variant(""));
       
        props.put("WLO_CLOTH_REPAIR_TIME",new Variant(""));
        props.put("WLO_SHUTTLE_REPAIR_TIME",new Variant(""));
        props.put("WLO_PICKING_REPAIR_TIME",new Variant(""));
        props.put("WLO_WARP_END_REPAIR_TIME",new Variant(""));
        props.put("WLO_TEMPLE_REPAIR_TIME",new Variant(""));

        props.put("WLO_CLOTH_REPAIR1",new Variant(""));
        props.put("WLO_CLOTH_REPAIR2",new Variant(""));
        props.put("WLO_CLOTH_REPAIR3",new Variant(""));
        props.put("WLO_CLOTH_REPAIR4",new Variant(""));
        props.put("WLO_CLOTH_REPAIR5",new Variant(""));
        props.put("WLO_CLOTH_REPAIR6",new Variant(""));

        props.put("WLO_SHUTTLE_REPAIR1",new Variant(""));
        props.put("WLO_SHUTTLE_REPAIR2",new Variant(""));
        props.put("WLO_SHUTTLE_REPAIR3",new Variant(""));
        props.put("WLO_SHUTTLE_REPAIR4",new Variant(""));
  
        props.put("WLO_PICKING_REPAIR1",new Variant(""));
        props.put("WLO_PICKING_REPAIR2",new Variant(""));
        props.put("WLO_PICKING_REPAIR3",new Variant(""));
        props.put("WLO_PICKING_REPAIR4",new Variant(""));
        props.put("WLO_PICKING_REPAIR5",new Variant(""));
        props.put("WLO_PICKING_REPAIR6",new Variant(""));
        props.put("WLO_PICKING_REPAIR7",new Variant(""));
        props.put("WLO_PICKING_REPAIR8",new Variant(""));
  
        props.put("WLO_WARP_END_REPAIR1",new Variant(""));
        props.put("WLO_WARP_END_REPAIR2",new Variant(""));
        props.put("WLO_WARP_END_REPAIR3",new Variant(""));
        props.put("WLO_WARP_END_REPAIR4",new Variant(""));
        props.put("WLO_WARP_END_REPAIR5",new Variant(""));
  
        props.put("WLO_TEMPLE_REPAIR1",new Variant(""));
        props.put("WLO_TEMPLE_REPAIR2",new Variant(""));
        props.put("WLO_TEMPLE_REPAIR3",new Variant(""));
        props.put("WLO_TEMPLE_REPAIR4",new Variant(""));
        props.put("WLO_TEMPLE_REPAIR5",new Variant(""));
        props.put("WLO_MICS_TIME",new Variant(""));
        props.put("WLO_LOSS_TIME",new Variant(""));
        props.put("WLO_LOSS_TOTAL",new Variant(""));
        
   }
}