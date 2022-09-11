package EITLERP.FeltSales.Budget;

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
public class clsBudgetManualItem {

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
    public clsBudgetManualItem() {
        props = new HashMap();
        props.put("DOC_NO", new Variant(""));
        props.put("YEAR_FROM", new Variant(""));
        props.put("YEAR_TO", new Variant(""));
        props.put("PARTY_CODE", new Variant(""));
        props.put("PARTY_NAME", new Variant(""));
        props.put("MACHINE_NO", new Variant(""));
        props.put("POSITION_NO", new Variant(""));
        props.put("POSITION_DESC", new Variant(""));
        props.put("STYLE", new Variant(""));
        props.put("QUALITY_NO", new Variant(""));
        props.put("GROUP_NAME", new Variant(""));
        props.put("PRESS_LENGTH", new Variant(0.00));
        props.put("PRESS_WIDTH", new Variant(0.00));
        props.put("PRESS_GSM", new Variant(0.00));
        props.put("PRESS_WEIGHT", new Variant(0.00));
        props.put("PRESS_SQMTR", new Variant(0.00));
        props.put("DRY_LENGTH", new Variant(0.00));
        props.put("DRY_WIDTH", new Variant(0.00));
        props.put("DRY_SQMTR", new Variant(0.00));
        props.put("DRY_WEIGHT", new Variant(0.00));
        props.put("SELLING_PRICE", new Variant(0.00));
        props.put("SPL_DISCOUNT", new Variant(0.00));
        props.put("WIP", new Variant(0.00));
        props.put("STOCK", new Variant(0.00));
        props.put("Q1", new Variant(0.00));
        props.put("Q2", new Variant(0.00));
        props.put("Q3", new Variant(0.00));
        props.put("Q4", new Variant(0.00));
        props.put("Q1KG", new Variant(0.00));
        props.put("Q1SQMTR", new Variant(0.00));
        props.put("Q2KG", new Variant(0.00));
        props.put("Q2SQMTR", new Variant(0.00));
        props.put("Q3KG", new Variant(0.00));
        props.put("Q3SQMTR", new Variant(0.00));
        props.put("Q4KG", new Variant(0.00));
        props.put("Q4SQMTR", new Variant(0.00));
        props.put("TOTAL", new Variant(0.00));
        props.put("TOTAL_KG", new Variant(0.00));
        props.put("TOTAL_SQMTR", new Variant(0.00));
        props.put("GST_PER", new Variant(0.00));
        props.put("GROSS_AMOUNT", new Variant(0.00));
        props.put("DISCOUNT_AMOUNT", new Variant(0.00));
        props.put("NET_AMOUNT", new Variant(0.00));
        props.put("PP_REMARKS", new Variant(""));
        props.put("PARTY_STATUS", new Variant(""));
        props.put("SYSTEM_STATUS", new Variant(""));
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
