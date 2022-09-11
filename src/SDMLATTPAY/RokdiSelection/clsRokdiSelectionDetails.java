/*
 * clsRokdiSelectionDetails.java
 * This class is used for holding the data of Felt Production Finishing in HashMap
 * 
 * Created on August 22, 2013, 11:20 AM
 */
package SDMLATTPAY.RokdiSelection;

import java.util.HashMap;
import EITLERP.Variant;

/**
 *
 * @author Vivek Kumar
 */
public class clsRokdiSelectionDetails {

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
    public clsRokdiSelectionDetails() {
        props = new HashMap();
        
        props.put("SR_NO", new Variant(0));
        props.put("ROKDI_CONSIDER", new Variant(""));
        props.put("ROKDI_NOT_CONSIDER", new Variant(""));
        props.put("ROKDI_REMARK", new Variant(""));
        props.put("EMP_ID", new Variant(""));
        props.put("EMP_NAME", new Variant(""));
        props.put("ROKDI_DATE", new Variant(""));
        props.put("ROKDI_SHIFT", new Variant(""));
        props.put("PUNCH_FROM_DATE", new Variant(""));
        props.put("PUNCH_FROM_TIME", new Variant(""));
        props.put("PUNCH_TO_DATE", new Variant(""));
        props.put("PUNCH_TO_TIME", new Variant(""));
        props.put("TIME_DIFFERENCE", new Variant(""));
        props.put("SYS_ROKDI_HOURS", new Variant(""));
        props.put("DEPT_ROKDI_HOURS", new Variant(""));
        props.put("DEPT_ROKDI_REMARK", new Variant(""));
        props.put("TK_ROKDI_HOURS", new Variant(""));
        props.put("TK_ROKDI_REMARK", new Variant(""));
        props.put("AUDIT_ROKDI_REMARK", new Variant(""));
        props.put("EMP_DEPT", new Variant(""));

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
