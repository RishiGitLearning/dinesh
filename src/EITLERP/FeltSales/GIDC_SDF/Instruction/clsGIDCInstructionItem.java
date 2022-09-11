/*
 *
 * clsMRItem.java
 *
 * Created on April 19, 2004, 2:27 PM
 */
package EITLERP.FeltSales.GIDC_SDF.Instruction;

import EITLERP.FeltSales.FeltWarpingBeamOrder.*;
import EITLERP.Production.ReportUI.*;
import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;

/**
 *
 * @author @version
 */
public class clsGIDCInstructionItem {

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
    public clsGIDCInstructionItem() {
        props = new HashMap();
        props.put("REVISION_NO", new Variant(0));
        props.put("ENTRY_DATE", new Variant(""));
        props.put("APPROVAL_STATUS", new Variant(""));
        props.put("UPDATED_BY", new Variant(0));
        props.put("DOC_NO", new Variant(""));
        props.put("DOC_DATE", new Variant(""));
        props.put("SR_NO", new Variant(0));
        props.put("PIECE_NO", new Variant(""));
        props.put("PARTY_CODE", new Variant(""));
        props.put("RMCODE_LENGTH", new Variant(""));
        props.put("REQUIRE_QTY_LENGTH", new Variant(0.00));
        props.put("UNIT_LENGTH", new Variant(""));
        props.put("RMCODE_WEFT", new Variant(""));
        props.put("REQUIRE_QTY_WEFT", new Variant(0.00));
        props.put("UNIT_WEFT", new Variant(""));
        props.put("PRODUCT_CODE", new Variant(""));
        props.put("PRODUCT_GROUP", new Variant(""));
        props.put("STYLE", new Variant(""));
        props.put("LENGTH", new Variant(0.00));
        props.put("WIDTH", new Variant(0.00));
        props.put("GSM", new Variant(0.00));
        props.put("WEIGHT", new Variant(0.00));
        props.put("SEQUANCE_NO", new Variant(0));
        props.put("GREY_LENGTH", new Variant(0.00));
        props.put("GREY_WIDTH", new Variant(0.00));        
        props.put("INDICATOR", new Variant(""));
        props.put("INDICATOR_DATE", new Variant(""));
        props.put("INDICATOR_DOC", new Variant(""));        
        props.put("FROM_IP", new Variant(""));
        props.put("REMARKS", new Variant(""));
        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("CHANGED_DATE", new Variant(""));
        props.put("CHANGED", new Variant(0));
    }

}
