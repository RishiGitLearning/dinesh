/*
 *
 * clsMRItem.java
 *
 * Created on April 19, 2004, 2:27 PM
 */
package EITLERP.FeltSales.TrailPiece;

import EITLERP.FeltSales.FeltWarpingBeamOrder.*;
import EITLERP.Production.ReportUI.*;
import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;

/**
 *
 * @author 
 * @version
 */
public class clsTrailPieceEntryItem {

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
    public clsTrailPieceEntryItem() {
        props = new HashMap();
        props.put("REVISION_NO", new Variant(0));
        props.put("DOC_NO", new Variant(""));
        props.put("FT_PIECE_NO", new Variant(""));
        props.put("PIECETRIAL_CATEGORY", new Variant(""));
        props.put("DOC_DATE", new Variant(""));
        props.put("FROM_IP", new Variant(""));
        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("CHANGED_DATE", new Variant(""));
        props.put("CHANGED", new Variant(0));
    }

}
