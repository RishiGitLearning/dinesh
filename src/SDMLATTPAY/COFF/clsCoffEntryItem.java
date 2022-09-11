/*
 *
 * clsMRItem.java
 *
 * Created on April 19, 2004, 2:27 PM
 */
package SDMLATTPAY.COFF;

import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;

/**
 *
 * @author @version
 */
public class clsCoffEntryItem {

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
    public clsCoffEntryItem() {
        props = new HashMap();
        props.put("REVISION_NO", new Variant(0));
        props.put("COFF_DOC_NO", new Variant(""));
        props.put("COFF_DOC_DATE", new Variant(""));
        props.put("COFF_EMPID", new Variant(0.00));
        props.put("COFF_YEAR", new Variant(0));
        props.put("COFF_MONTH", new Variant(""));
        props.put("COFF_DATE", new Variant(""));
        props.put("COFF_SHIFT", new Variant(""));
        props.put("COFF_OT_SHIFT", new Variant(""));
        props.put("COFF_TYPE", new Variant(""));
        props.put("COFF_FROM_TIME", new Variant(""));
        props.put("COFF_TO_TIME", new Variant(""));
        props.put("COFF_HRS", new Variant(""));
        props.put("COFF_HRS_REPLACEMENT", new Variant(""));
        props.put("COFF_HRS_EXTRA", new Variant(""));
        props.put("COFF_PUNCHES", new Variant(""));
        props.put("COFF_PUNCHES_NEXT", new Variant(""));
        props.put("FROM_IP", new Variant(""));
        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("CHANGED_DATE", new Variant(""));
        props.put("CHANGED", new Variant(0));
    }

}
