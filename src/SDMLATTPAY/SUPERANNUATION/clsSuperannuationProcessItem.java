package SDMLATTPAY.SUPERANNUATION;

import SDMLATTPAY.COFF.*;
import EITLERP.Variant;
import java.util.*;
import java.sql.*;
import java.net.*;


/**
 *
 * @author jadave
 * @version
 */
public class clsSuperannuationProcessItem {

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
    public clsSuperannuationProcessItem() {
        props = new HashMap();
        props.put("DOC_NO", new Variant(""));
        props.put("DOC_DATE", new Variant(""));
        props.put("COMPANY_CODE", new Variant(""));
        props.put("SR_NO", new Variant(0));
        props.put("EMPLOYEE_CODE", new Variant(""));        
        props.put("DEPT_CD", new Variant(""));                        
        props.put("BASIC_EARN", new Variant(0.0));
        props.put("SUPERANNUATION_PER", new Variant(0.0));
        props.put("SUPERANNUATION_EARN", new Variant(0.0));
        props.put("NET_PAY", new Variant(0.0));
        props.put("REMARKS", new Variant(""));
        props.put("CREATED_BY", new Variant(""));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(""));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("APPROVED", new Variant(""));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED", new Variant(""));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("CANCELED", new Variant(""));
        props.put("CANCELED_DATE", new Variant(""));
        props.put("CHANGED", new Variant(""));
        props.put("CHANGED_DATE", new Variant(""));        
        props.put("FROM_IP", new Variant(""));
        props.put("SA_YEAR", new Variant(""));
    }

}
