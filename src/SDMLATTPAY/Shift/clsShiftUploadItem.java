package SDMLATTPAY.Shift;

import EITLERP.FeltSales.Budget.*;
import EITLERP.Production.ReportUI.*;
import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;

/**
 *
 * @author jadave
 * @version
 */
public class clsShiftUploadItem {

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
    public clsShiftUploadItem() {
        props = new HashMap();
       props.put("SU_DOC_NO", new Variant(""));
        props.put("SU_DOC_DATE", new Variant(""));
        props.put("SU_YEAR", new Variant(""));
        props.put("SU_MONTH", new Variant(""));        
        props.put("SU_EMPID", new Variant(""));
        for(int i=1;i<=31;i++){
            props.put(String.valueOf(i), new Variant(""));
        }
        props.put("REMARKS", new Variant(""));
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
