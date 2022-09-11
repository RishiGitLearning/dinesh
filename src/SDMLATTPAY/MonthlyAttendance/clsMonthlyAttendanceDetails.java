/*
 * clsFeltAutoPISelectionDetails.java
 * This class is used for holding the data of Felt Production Finishing in HashMap
 * 
 * Created on August 22, 2013, 11:20 AM
 */
package SDMLATTPAY.MonthlyAttendance;

import java.util.HashMap;
import EITLERP.Variant;

/**
 *
 * @author Vivek Kumar
 */
public class clsMonthlyAttendanceDetails {

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
    public clsMonthlyAttendanceDetails() {
        props = new HashMap();
//        props.put("DOC_NO",new Variant(""));
//        props.put("DOC_DATE",new Variant(""));
        props.put("MAS_EMPID", new Variant(""));
        props.put("MAS_EMPNAME", new Variant(""));
        props.put("MAS_EMPDEPT", new Variant(""));
        props.put("MAS_MM", new Variant(""));
        props.put("MAS_YYYY", new Variant(""));
        props.put("TOTAL_MONTH_DAYS", new Variant(""));
        props.put("PAID_DAYS", new Variant(""));
        props.put("PRESENT_DAYS", new Variant(""));
        props.put("LC_DAYS", new Variant(""));
        props.put("LWP_DAYS", new Variant(""));
        props.put("LC_LWP_DAYS", new Variant(""));
        props.put("CO", new Variant(""));
        props.put("OD", new Variant(""));
        props.put("PL", new Variant(""));
        props.put("CL", new Variant(""));
        props.put("SL", new Variant(""));
        props.put("LOFF", new Variant(""));
        props.put("EOFF", new Variant(""));
        props.put("MAS_PH", new Variant(""));
        props.put("MAS_STAFF_WOFF", new Variant(""));
        props.put("NPH", new Variant(""));
        props.put("MAS_WORKER_WOFF", new Variant(""));
        props.put("MAS_COMPANY_WOFF", new Variant(""));
        props.put("NPL", new Variant(""));
        props.put("ESIC", new Variant(""));
        props.put("P_GP", new Variant(""));
        props.put("O_GP", new Variant(""));
        props.put("ABST", new Variant(""));
        props.put("MAS_MAIN_CATEGORY", new Variant(""));
        props.put("MAS_SUB_CATEGORY", new Variant(""));

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
