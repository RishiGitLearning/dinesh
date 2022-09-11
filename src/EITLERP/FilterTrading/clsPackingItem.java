/*
 *
 * clsMRItem.java
 *
 * Created on April 19, 2004, 2:27 PM
 */
package EITLERP.FilterTrading;

import java.util.*;
import EITLERP.*;

/**
 *
 * @author jadave
 * @version
 */
public class clsPackingItem {

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
    public clsPackingItem() {
        props = new HashMap();
        props.put("PACKING_NOTE_NO", new Variant(""));
        props.put("PACKING_DATE", new Variant(""));
        props.put("SR_NO", new Variant(0));
        props.put("WH_CODE", new Variant(""));
        props.put("PARTY_CODE", new Variant(""));
        props.put("SALE_NOTE_NO", new Variant(""));
        props.put("STATION", new Variant(""));
        props.put("MODE_OF_TRANSPORT", new Variant(""));

        props.put("QUALITY_CD", new Variant(""));
        props.put("PIECE_NO", new Variant(""));
        props.put("GROSS_METER", new Variant(0.00));
        props.put("FLAG_CD", new Variant(0.00));
        props.put("NET_METER", new Variant(0.00));
        props.put("WIDTH", new Variant(0.00));
        props.put("SQ_METER", new Variant(0));
        props.put("KGS", new Variant(0.00));

        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));

        //props.put("CHEM_TRT_IN",new Variant(""));         
        //props.put("PIN_IND",new Variant(""));
        //props.put("CHARGES",new Variant(""));
        //props.put("SPR_IND",new Variant(""));
        //props.put("SQM_IND",new Variant(""));       
    }

}
