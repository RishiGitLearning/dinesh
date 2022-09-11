/*
 *
 * clsMRItem.java
 *
 * Created on April 19, 2004, 2:27 PM
 */
package EITLERP;

import java.util.*;

/**
 *
 * @author 
 */
public class clsimportstmt_hdfcitem {

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
    public clsimportstmt_hdfcitem() {
        props = new HashMap();
        props.put("STATEMENT_ID", new Variant(""));
        props.put("BOOK_DATE", new Variant(""));
        props.put("DESCRIPTION", new Variant(""));
        props.put("LEDGER_BALANCE", new Variant(0));
        props.put("CREDIT", new Variant(0));
        props.put("DEBIT", new Variant(0));        
        props.put("VALUE_DATE", new Variant(""));
        props.put("REFERANCE_NO", new Variant(""));
        props.put("TRANSACTION_BRANCH", new Variant(""));
        props.put("BOOK_CODE", new Variant(""));
        

        props.put("HIERARCHY_ID", new Variant(0));
        props.put("APPROVED", new Variant(0));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED", new Variant(0));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("CANCELLED", new Variant(0));
        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("REJECTED_REASON", new Variant(""));

    }

}
