/*
 * clsFeltAutoPISelectionDetails.java
 * This class is used for holding the data of Felt Production Finishing in HashMap
 * 
 * Created on August 22, 2013, 11:20 AM
 */
package SDMLATTPAY.RokdiRequest;

import java.util.HashMap;
import EITLERP.Variant;

/**
 *
 * @author Vivek Kumar
 */
public class clsRokdiRequestFormDetails {

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
    public clsRokdiRequestFormDetails() {
        props = new HashMap();
        
        props.put("SR_NO", new Variant(0));
        props.put("EMP_ID", new Variant(""));
        props.put("EMP_NAME", new Variant(""));
        props.put("EMP_DEPT", new Variant(""));
        props.put("EMP_MAIN_CATEGORY", new Variant(""));
        props.put("EMP_SUB_CATEGORY", new Variant(""));
        props.put("REQ_SHIFT", new Variant(""));
        props.put("ROKDI_TYPE", new Variant(""));
        props.put("ROKDI_HOURS", new Variant(""));
        props.put("EMP_REMARK", new Variant(""));
        props.put("ENTRY_FLAG", new Variant(""));
        props.put("AMEND_DOC_NO", new Variant(""));
        props.put("AMEND_DOC_DATE", new Variant(""));

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
