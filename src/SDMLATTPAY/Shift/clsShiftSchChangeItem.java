/*
 *
 * clsMRItem.java
 *
 * Created on April 19, 2004, 2:27 PM
 */
package SDMLATTPAY.Shift;

import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;

/**
 *
 * @author 
 * @version
 */
public class clsShiftSchChangeItem {

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
     * Creates new clsMRItem
     */
    public clsShiftSchChangeItem() {
        props = new HashMap();
        props.put("REVISION_NO", new Variant(0));
        props.put("SSCC_DOC_NO", new Variant(""));
        props.put("SSCC_DOC_DATE", new Variant(""));
        props.put("SSCC_EMPID", new Variant(0.00));
        props.put("SSCC_YEAR", new Variant(0));
        props.put("SSCC_MONTH", new Variant(""));
        props.put("SSCC_SHIFT_CHANGE_DATE", new Variant(""));
        props.put("SSCC_CURRENT_SHIFT", new Variant(""));
        props.put("SSCC_CHANGE_SHIFT", new Variant(""));
        props.put("FROM_IP", new Variant(""));
        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("CHANGED_DATE", new Variant(""));
        props.put("CHANGED", new Variant(0));
    }

}
