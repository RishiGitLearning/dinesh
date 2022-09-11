/*
 * clsFeltAutoPISelectionDetails.java
 * This class is used for holding the data of Felt Production Finishing in HashMap
 * 
 * Created on August 22, 2013, 11:20 AM
 */
package SDMLATTPAY.ManpowerRequisition;

import java.util.HashMap;
import EITLERP.Variant;

/**
 *
 * @author Vivek Kumar
 */
public class clsManpowerRequisitionFormDetails {

    private HashMap props;
    public boolean Ready = false;

    public Variant getAttribute(String PropName) {
        return (Variant) props.get(PropName);
    }

    public void setAttribute(String PropName, Object Value) {
        props.put(PropName, new Variant(Value));
    }

    public void setAttribute(String PropName, int Value) {
        props.put(PropName, new Variant(Value));
    }

    public void setAttribute(String PropName, long Value) {
        props.put(PropName, new Variant(Value));
    }

    public void setAttribute(String PropName, double Value) {
        props.put(PropName, new Variant(Value));
    }

    public void setAttribute(String PropName, float Value) {
        props.put(PropName, new Variant(Value));
    }

    public void setAttribute(String PropName, boolean Value) {
        props.put(PropName, new Variant(Value));
    }

    /**
     * Creates new clsFeltAutoPISelectionDetails
     */
    public clsManpowerRequisitionFormDetails() {
        props = new HashMap();
        
        props.put("EMP_ID", new Variant(""));
        props.put("EMP_NAME", new Variant(""));
        props.put("EMP_DEPT", new Variant(""));
        props.put("EMP_MAIN_CATEGORY", new Variant(""));
        props.put("EMP_SUB_CATEGORY", new Variant(""));
        props.put("SR_NO", new Variant(0));
        props.put("PUNCH_TIME", new Variant(""));
        props.put("EMP_REMARK", new Variant(""));
        
        props.put("EMP_PRESENT", new Variant(""));
        props.put("EMP_ABSENT", new Variant(""));
        props.put("EMP_CL", new Variant(""));
        props.put("EMP_PL", new Variant(""));
        props.put("EMP_ESI", new Variant(""));
        props.put("EMP_EXCESS", new Variant(""));

        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("APPROVED", new Variant(0));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED", new Variant(0));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("CANCELED", new Variant(0));
        props.put("HIERARCHY_ID", new Variant(0));
    }
}
