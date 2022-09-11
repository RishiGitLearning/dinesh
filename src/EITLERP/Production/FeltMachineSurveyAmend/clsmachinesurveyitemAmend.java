/*
 *
 * clsMRItem.java
 *
 * Created on April 19, 2004, 2:27 PM
 */

package EITLERP.Production.FeltMachineSurveyAmend;


import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;

/**
 *
 * @author  jadave
 * @version
 */

public class clsmachinesurveyitemAmend {
    
    ;
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
    public clsmachinesurveyitemAmend() {
        props=new HashMap();
        props.put("SR_NO",new Variant(0));
        
        props.put("MM_DOC_NO",new Variant(""));
        props.put("MM_PARTY_CODE",new Variant(""));
        props.put("MM_INCHRGE_NAME",new Variant(""));
        props.put("HIERARCHY_ID",new Variant(""));
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("APPROVED",new Variant(""));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("REJECTED",new Variant(""));
        props.put("REJECTED_DATE",new Variant(""));
        props.put("CANCELED",new Variant(""));
        props.put("CHANGED",new Variant(""));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("REJECTED_REMARKS",new Variant(""));
        

        
        props.put("MM_AMEND_NO",new Variant(""));
        props.put("MM_MACHINE_POSITION",new Variant(""));
        props.put("MM_MACHINE_POSITION_DESC",new Variant(""));
        props.put("MM_COMBINATION_CODE",new Variant(""));
        props.put("MM_PRESS_TYPE",new Variant(""));
        props.put("MM_PRESS_ROLL_DAI_MM",new Variant(""));
        props.put("MM_PRESS_ROLL_FACE_TOTAL_MM",new Variant(""));
        props.put("MM_PRESS_ROLL_FACE_NET_MM",new Variant(""));
        props.put("MM_FELT_ROLL_WIDTH_MM",new Variant(""));
        props.put("MM_PRESS_LOAD",new Variant(""));
        props.put("MM_VACCUM_CAPACITY",new Variant(""));
        props.put("MM_UHLE_BOX",new Variant(""));
        props.put("MM_HP_SHOWER",new Variant(""));
        props.put("MM_LP_SHOWER",new Variant(""));
        props.put("MM_FELT_LENGTH",new Variant(""));
        props.put("MM_FELT_WIDTH",new Variant(""));
        props.put("MM_FELT_GSM",new Variant(""));
        props.put("MM_FELT_WEIGHT",new Variant(""));
        props.put("MM_FELT_TYPE",new Variant(""));
        props.put("MM_FELT_STYLE",new Variant(""));
        props.put("MM_AVG_LIFE",new Variant(""));
        props.put("MM_AVG_PRODUCTION",new Variant(""));
        props.put("MM_FELT_CONSUMPTION",new Variant(""));
        props.put("MM_DINESH_SHARE",new Variant(""));
        props.put("MM_REMARK_DESIGN",new Variant(""));
        props.put("MM_REMARK_GENERAL",new Variant(""));
        props.put("MM_NO_DRYER_CYLINDER",new Variant(""));
        props.put("MM_CYLINDER_DIA_MM",new Variant(""));
        props.put("MM_CYLINDER_FACE_NET_MM",new Variant(""));
        
        props.put("MM_FELT_LIFE",new Variant(""));
        props.put("MM_TPD",new Variant(""));
        props.put("MM_TOTAL_PRODUCTION",new Variant(""));
        props.put("MM_PAPER_FELT",new Variant(""));
        
        props.put("MM_DRIVE_TYPE",new Variant(""));
        props.put("MM_GUIDE_TYPE",new Variant(""));
        props.put("MM_GUIDE_PAM_TYPE",new Variant(""));
        props.put("MM_VENTILATION_TYPE",new Variant(""));
        props.put("MM_FABRIC_LENGTH",new Variant(""));
        props.put("MM_FABRIC_WIDTH",new Variant(""));
        props.put("MM_SIZE_M2",new Variant(""));
        props.put("MM_SCREEN_TYPE",new Variant(""));
        props.put("MM_STYLE_DRY",new Variant(""));
        props.put("MM_CFM_DRY",new Variant(""));
        props.put("MM_AVG_LIFE_DRY",new Variant(""));
        props.put("MM_CONSUMPTION_DRY",new Variant(""));
        props.put("MM_DINESH_SHARE_DRY",new Variant(""));
        props.put("MM_REMARK_DRY",new Variant(""));
        props.put("MM_ITEM_CODE",new Variant(""));
        props.put("MM_GRUP",new Variant(""));
        props.put("MM_POSITION_WISE",new Variant(""));
        props.put("MM_HARDNESS", new Variant(""));
        props.put("MM_FELT_WASHING_CHEMICALS", new Variant(""));
        props.put("MM_VACCUM_IN_UHLE_BOX", new Variant(""));
        props.put("MM_P_NO_TEMP", new Variant(""));
        props.put("MM_M_NO_TEMP", new Variant(""));
        
        props.put("MM_MACHINE_FLOOR",new Variant(""));
        props.put("MM_NUMBER_OF_FORMING_FABRIC",new Variant(""));
        props.put("MM_TYPE_OF_FORMING_FABRIC",new Variant(""));
        props.put("MM_PRESS_ROLL_FOR_1ST_NIP_FACE_LENGTH",new Variant(""));
        props.put("MM_PRESS_ROLL_FOR_2ND_NIP_FACE_LENGTH",new Variant(""));
        props.put("MM_PRESS_ROLL_FOR_3RD_NIP_FACE_LENGTH",new Variant(""));
        props.put("MM_PRESS_ROLL_FOR_4TH_NIP_FACE_LENGTH",new Variant(""));
        props.put("MM_WASH_ROLL_SHOWER",new Variant(""));
        props.put("MM_HP_SHOWER_NOZZLES",new Variant(""));
        props.put("MM_UHLE_BOX_VACUUM",new Variant(""));
        props.put("MM_CHEMICAL_SHOWER",new Variant(""));
        props.put("MM_1ST_LINEAR_NIP_PRESSURE",new Variant(""));
        props.put("MM_2ND_LINEAR_NIP_PRESSURE",new Variant(""));
        props.put("MM_3RD_LINEAR_NIP_PRESSURE",new Variant(""));
        props.put("MM_4TH_LINEAR_NIP_PRESSURE",new Variant(""));
        props.put("MM_LOADING_SYSTEM",new Variant(""));
        props.put("MM_LP_SHOWER_NOZZLES",new Variant(""));
        props.put("MM_1ST_ROLL_MATERIAL",new Variant(""));
        props.put("MM_2ND_ROLL_MATERIAL",new Variant(""));
        props.put("MM_3RD_ROLL_MATERIAL",new Variant(""));
        props.put("MM_4TH_ROLL_MATERIAL",new Variant(""));
        props.put("MM_5TH_ROLL_MATERIAL",new Variant(""));
        props.put("MM_6TH_ROLL_MATERIAL",new Variant(""));
        props.put("MM_7TH_ROLL_MATERIAL",new Variant(""));
        props.put("MM_8TH_ROLL_MATERIAL",new Variant(""));
        props.put("MM_BATT_GSM",new Variant(""));
        props.put("MM_FIBERS_USED",new Variant(""));
        props.put("MM_STRETCH",new Variant(""));
        props.put("MM_MG",new Variant(""));
        props.put("MM_YANKEE",new Variant(""));
        props.put("MM_MG_YANKEE_NIP_LOAD",new Variant(""));
        
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("CHANGED",new Variant(""));
         
    }
    
}
