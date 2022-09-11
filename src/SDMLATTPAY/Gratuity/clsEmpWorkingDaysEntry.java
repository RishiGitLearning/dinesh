/*
 *
 * clsMRItem.java
 *
 * Created on April 19, 2004, 2:27 PM
 */
package SDMLATTPAY.Gratuity;

import SDMLATTPAY.COFF.*;
import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;

/**
 *
 * @author 
 * @version
 */
public class clsEmpWorkingDaysEntry {

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
    public clsEmpWorkingDaysEntry() {
        props = new HashMap();                
        props.put("EMP_NO", new Variant(""));
        props.put("FROM_YEAR", new Variant(""));
        props.put("TO_YEAR", new Variant(""));
        props.put("WORKING_DAYS", new Variant(""));
        
    }

}
