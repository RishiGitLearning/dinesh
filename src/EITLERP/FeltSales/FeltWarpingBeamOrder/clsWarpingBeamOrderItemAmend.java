/*
 *
 * clsMRItem.java
 *
 * Created on April 19, 2004, 2:27 PM
 */
package EITLERP.FeltSales.FeltWarpingBeamOrder;

import EITLERP.Production.ReportUI.*;
import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;

/**
 *
 * @author @version
 */
public class clsWarpingBeamOrderItemAmend {

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
    public clsWarpingBeamOrderItemAmend() {
        props = new HashMap();
        props.put("REVISION_NO", new Variant(0));
        props.put("DOC_NO", new Variant(""));
        props.put("BEAM_NO", new Variant(""));
        props.put("LOOM_NO", new Variant(0.00));
        props.put("SR_NO", new Variant(0));
        props.put("PIECE_NO", new Variant(""));
        props.put("PARTY_CODE", new Variant(""));
        props.put("PRODUCT_CODE", new Variant(""));
        props.put("GRUP", new Variant(""));
        props.put("STYLE", new Variant(""));
        props.put("LENGTH", new Variant(0.00));
        props.put("WIDTH", new Variant(0.00));
        props.put("GSM", new Variant(0.00));
        props.put("WEIGHT", new Variant(0.00));
        props.put("SEQUANCE_NO", new Variant(0));
        props.put("READ_SPACE", new Variant(0.00));
        props.put("THEORICAL_LENGTH_MTR", new Variant(0.00));
        props.put("THEORICAL_PICKS_10_CM", new Variant(0.00));
        props.put("TOTAL_PICKS", new Variant(0.00));
        props.put("EXPECTED_GREV_SQ_MTR", new Variant(0.00));
        props.put("WEAVING_DATE", new Variant(""));
        props.put("WEAVING_WEIGHT", new Variant(0.0));
        props.put("WEAVING_SEQUENCE", new Variant(0));
        props.put("PP_REMARK", new Variant(""));
        props.put("WIP_OC_MONTHYEAR", new Variant(""));
        props.put("WIP_OC_LAST_DDMMYY", new Variant(""));
        props.put("AUTO_SEQUENCE", new Variant(0));
        props.put("FROM_IP", new Variant(""));
        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("CHANGED_DATE", new Variant(""));
        props.put("CHANGED", new Variant(0));
    }

}
