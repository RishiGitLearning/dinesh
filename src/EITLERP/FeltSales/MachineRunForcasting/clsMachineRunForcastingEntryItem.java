package EITLERP.FeltSales.MachineRunForcasting;

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
public class clsMachineRunForcastingEntryItem {

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
    public clsMachineRunForcastingEntryItem() {
        props = new HashMap();
        props.put("DOC_NO", new Variant(""));
        props.put("YEAR_FROM", new Variant(""));
        props.put("YEAR_TO", new Variant(""));
        props.put("PARTY_CODE", new Variant(""));
        props.put("PARTY_NAME", new Variant(""));
        props.put("MACHINE_NO", new Variant(""));
        props.put("POSITION_NO", new Variant(""));
        props.put("POSITION_DESC", new Variant(""));
        
        props.put("APR_EXP_RD", new Variant(0.00));
        props.put("APR_EXP_RD_REMARK", new Variant(""));
        props.put("APR_ACT_RD", new Variant(0.00));
        props.put("APR_ACT_RD_REMARK", new Variant(""));

        props.put("MAY_EXP_RD", new Variant(0.00));
        props.put("MAY_EXP_RD_REMARK", new Variant(""));
        props.put("MAY_ACT_RD", new Variant(0.00));
        props.put("MAY_ACT_RD_REMARK", new Variant(""));

        props.put("JUN_EXP_RD", new Variant(0.00));
        props.put("JUN_EXP_RD_REMARK", new Variant(""));
        props.put("JUN_ACT_RD", new Variant(0.00));
        props.put("JUN_ACT_RD_REMARK", new Variant(""));

        props.put("JUL_EXP_RD", new Variant(0.00));
        props.put("JUL_EXP_RD_REMARK", new Variant(""));
        props.put("JUL_ACT_RD", new Variant(0.00));
        props.put("JUL_ACT_RD_REMARK", new Variant(""));

        props.put("AUG_EXP_RD", new Variant(0.00));
        props.put("AUG_EXP_RD_REMARK", new Variant(""));
        props.put("AUG_ACT_RD", new Variant(0.00));
        props.put("AUG_ACT_RD_REMARK", new Variant(""));

        props.put("SEP_EXP_RD", new Variant(0.00));
        props.put("SEP_EXP_RD_REMARK", new Variant(""));
        props.put("SEP_ACT_RD", new Variant(0.00));
        props.put("SEP_ACT_RD_REMARK", new Variant(""));

        props.put("OCT_EXP_RD", new Variant(0.00));
        props.put("OCT_EXP_RD_REMARK", new Variant(""));
        props.put("OCT_ACT_RD", new Variant(0.00));
        props.put("OCT_ACT_RD_REMARK", new Variant(""));

        props.put("NOV_EXP_RD", new Variant(0.00));
        props.put("NOV_EXP_RD_REMARK", new Variant(""));
        props.put("NOV_ACT_RD", new Variant(0.00));
        props.put("NOV_ACT_RD_REMARK", new Variant(""));

        props.put("DEC_EXP_RD", new Variant(0.00));
        props.put("DEC_EXP_RD_REMARK", new Variant(""));
        props.put("DEC_ACT_RD", new Variant(0.00));
        props.put("DEC_ACT_RD_REMARK", new Variant(""));

        props.put("JAN_EXP_RD", new Variant(0.00));
        props.put("JAN_EXP_RD_REMARK", new Variant(""));
        props.put("JAN_ACT_RD", new Variant(0.00));
        props.put("JAN_ACT_RD_REMARK", new Variant(""));

        props.put("FEB_EXP_RD", new Variant(0.00));
        props.put("FEB_EXP_RD_REMARK", new Variant(""));
        props.put("FEB_ACT_RD", new Variant(0.00));
        props.put("FEB_ACT_RD_REMARK", new Variant(""));

        props.put("MAR_EXP_RD", new Variant(0.00));
        props.put("MAR_EXP_RD_REMARK", new Variant(""));
        props.put("MAR_ACT_RD", new Variant(0.00));
        props.put("MAR_ACT_RD_REMARK", new Variant(""));
        
        props.put("Q_1_EXP_RD", new Variant(0.00));
        props.put("Q_1_EXP_RD_REMARK", new Variant(""));
        props.put("Q_1_ACT_RD", new Variant(0.00));
        props.put("Q_1_ACT_RD_REMARK", new Variant(""));

        props.put("Q_2_EXP_RD", new Variant(0.00));
        props.put("Q_2_EXP_RD_REMARK", new Variant(""));
        props.put("Q_2_ACT_RD", new Variant(0.00));
        props.put("Q_2_ACT_RD_REMARK", new Variant(""));

        props.put("Q_3_EXP_RD", new Variant(0.00));
        props.put("Q_3_EXP_RD_REMARK", new Variant(""));
        props.put("Q_3_ACT_RD", new Variant(0.00));
        props.put("Q_3_ACT_RD_REMARK", new Variant(""));

        props.put("Q_4_EXP_RD", new Variant(0.00));
        props.put("Q_4_EXP_RD_REMARK", new Variant(""));
        props.put("Q_4_ACT_RD", new Variant(0.00));
        props.put("Q_4_ACT_RD_REMARK", new Variant(""));

        props.put("REMARKS", new Variant(""));
        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));

        props.put("INCHARGE", new Variant(""));
        props.put("PARTY_GROUP", new Variant(""));
    }

}
