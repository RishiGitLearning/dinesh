/*
 * clsFeltAutoPISelectionDetails.java
 * This class is used for holding the data of Felt Production Finishing in HashMap
 * 
 * Created on August 22, 2013, 11:20 AM
 */
package SDMLATTPAY.GatepassSelection;

import java.util.HashMap;
import EITLERP.Variant;

/**
 *
 * @author Vivek Kumar
 */
public class clsGatepassSelectionDetails {

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
    public clsGatepassSelectionDetails() {
        props = new HashMap();
        
        props.put("GP_CONSIDER",new Variant(""));
        props.put("GP_DATE", new Variant(""));
        props.put("GP_EMP_NO", new Variant(""));
        props.put("GP_EMP_NAME", new Variant(""));
        props.put("GP_EMP_DEPT", new Variant(""));
        props.put("GP_EMP_SHIFT", new Variant(""));
        props.put("GP_EMP_DESN", new Variant(""));        
        props.put("GP_TYPE", new Variant(""));
        props.put("GP_TOL", new Variant(""));
        props.put("GP_TOA", new Variant(""));
        props.put("GP_REMARKS", new Variant(""));
        props.put("GP_NATURE_OF_WORK", new Variant(""));
        props.put("GP_DEPT_HEAD", new Variant(""));
        props.put("GP_TOT_HOURS", new Variant(""));
        props.put("GP_TIME_OF_LEAVING", new Variant(""));
        props.put("GP_TIME_OF_ARRIVAL", new Variant(""));
        props.put("GP_CATEGORY", new Variant(""));

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
        props.put("FROM_IP", new Variant(""));
    }
}
