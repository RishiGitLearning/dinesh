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
public class clsBudgetReviewEntryItem {

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
    public clsBudgetReviewEntryItem() {
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
        props.put("CURRENT_PROJECTION", new Variant(0.00));
        props.put("CURRENT_PROJECTION_VALUE", new Variant(0.00));
        props.put("ACTUAL_BUDGET", new Variant(0.00));
        props.put("ACTUAL_BUDGET_VALUE", new Variant(0.00));
        props.put("DISPATCH_QTY", new Variant(0.00));
        props.put("DISPATCH_VALUE", new Variant(0.00));

        props.put("APR_BUDGET", new Variant(0.00));
        props.put("APR_KG", new Variant(0.00));
        props.put("APR_SQMTR", new Variant(0.00));
        props.put("APR_GROSS", new Variant(0.00));
        props.put("APR_DISCOUNT", new Variant(0.00));
        props.put("APR_NET_AMOUNT", new Variant(0.00));
        props.put("APR_DOUBTFUL_QTY", new Variant(0.00));
        props.put("APR_REMARK", new Variant(0.00));
        props.put("APR_REMARK_DOUBTFUL", new Variant(""));
        props.put("APR_PROPOSED_ACTION_BY_IC", new Variant(""));
        props.put("APR_PROPOSED_ACTION_DOUBTFUL", new Variant(""));

        props.put("MAY_BUDGET", new Variant(0.00));
        props.put("MAY_KG", new Variant(0.00));
        props.put("MAY_SQMTR", new Variant(0.00));
        props.put("MAY_GROSS", new Variant(0.00));
        props.put("MAY_DISCOUNT", new Variant(0.00));
        props.put("MAY_NET_AMOUNT", new Variant(0.00));
        props.put("MAY_DOUBTFUL_QTY", new Variant(0.00));
        props.put("MAY_REMARK", new Variant(0.00));
        props.put("MAY_REMARK_DOUBTFUL", new Variant(""));
        props.put("MAY_PROPOSED_ACTION_BY_IC", new Variant(""));
        props.put("MAY_PROPOSED_ACTION_DOUBTFUL", new Variant(""));

        props.put("JUN_BUDGET", new Variant(0.00));
        props.put("JUN_KG", new Variant(0.00));
        props.put("JUN_SQMTR", new Variant(0.00));
        props.put("JUN_GROSS", new Variant(0.00));
        props.put("JUN_DISCOUNT", new Variant(0.00));
        props.put("JUN_NET_AMOUNT", new Variant(0.00));
        props.put("JUN_REMARK", new Variant(0.00));
        props.put("JUN_REMARK_DOUBTFUL", new Variant(""));
        props.put("JUN_PROPOSED_ACTION_BY_IC", new Variant(""));
        props.put("JUN_PROPOSED_ACTION_DOUBTFUL", new Variant(""));

        props.put("JUL_BUDGET", new Variant(0.00));
        props.put("JUL_KG", new Variant(0.00));
        props.put("JUL_SQMTR", new Variant(0.00));
        props.put("JUL_GROSS", new Variant(0.00));
        props.put("JUL_DISCOUNT", new Variant(0.00));
        props.put("JUL_NET_AMOUNT", new Variant(0.00));
        props.put("JUL_DOUBTFUL_QTY", new Variant(0.00));
        props.put("JUL_REMARK", new Variant(0.00));
        props.put("JUL_REMARK_DOUBTFUL", new Variant(""));
        props.put("JUL_PROPOSED_ACTION_BY_IC", new Variant(""));
        props.put("JUL_PROPOSED_ACTION_DOUBTFUL", new Variant(""));

        props.put("AUG_BUDGET", new Variant(0.00));
        props.put("AUG_KG", new Variant(0.00));
        props.put("AUG_SQMTR", new Variant(0.00));
        props.put("AUG_GROSS", new Variant(0.00));
        props.put("AUG_DISCOUNT", new Variant(0.00));
        props.put("AUG_NET_AMOUNT", new Variant(0.00));
        props.put("AUG_DOUBTFUL_QTY", new Variant(0.00));
        props.put("AUG_REMARK", new Variant(0.00));
        props.put("AUG_REMARK_DOUBTFUL", new Variant(""));
        props.put("AUG_PROPOSED_ACTION_BY_IC", new Variant(""));
        props.put("AUG_PROPOSED_ACTION_DOUBTFUL", new Variant(""));

        props.put("SEP_BUDGET", new Variant(0.00));
        props.put("SEP_KG", new Variant(0.00));
        props.put("SEP_SQMTR", new Variant(0.00));
        props.put("SEP_GROSS", new Variant(0.00));
        props.put("SEP_DISCOUNT", new Variant(0.00));
        props.put("SEP_NET_AMOUNT", new Variant(0.00));
        props.put("SEP_DOUBTFUL_QTY", new Variant(0.00));
        props.put("SEP_REMARK", new Variant(0.00));
        props.put("SEP_REMARK_DOUBTFUL", new Variant(""));
        props.put("SEP_PROPOSED_ACTION_BY_IC", new Variant(""));
        props.put("SEP_PROPOSED_ACTION_DOUBTFUL", new Variant(""));

        props.put("OCT_BUDGET", new Variant(0.00));
        props.put("OCT_KG", new Variant(0.00));
        props.put("OCT_SQMTR", new Variant(0.00));
        props.put("OCT_GROSS", new Variant(0.00));
        props.put("OCT_DISCOUNT", new Variant(0.00));
        props.put("OCT_NET_AMOUNT", new Variant(0.00));
        props.put("OCT_DOUBTFUL_QTY", new Variant(0.00));
        props.put("OCT_REMARK", new Variant(0.00));
        props.put("OCT_REMARK_DOUBTFUL", new Variant(""));
        props.put("OCT_PROPOSED_ACTION_BY_IC", new Variant(""));
        props.put("OCT_PROPOSED_ACTION_DOUBTFUL", new Variant(""));

        props.put("NOV_BUDGET", new Variant(0.00));
        props.put("NOV_KG", new Variant(0.00));
        props.put("NOV_SQMTR", new Variant(0.00));
        props.put("NOV_GROSS", new Variant(0.00));
        props.put("NOV_DISCOUNT", new Variant(0.00));
        props.put("NOV_NET_AMOUNT", new Variant(0.00));
        props.put("NOV_DOUBTFUL_QTY", new Variant(0.00));
        props.put("NOV_REMARK", new Variant(0.00));
        props.put("NOV_REMARK_DOUBTFUL", new Variant(""));
        props.put("NOV_PROPOSED_ACTION_BY_IC", new Variant(""));
        props.put("NOV_PROPOSED_ACTION_DOUBTFUL", new Variant(""));

        props.put("DEC_BUDGET", new Variant(0.00));
        props.put("DEC_KG", new Variant(0.00));
        props.put("DEC_SQMTR", new Variant(0.00));
        props.put("DEC_GROSS", new Variant(0.00));
        props.put("DEC_DISCOUNT", new Variant(0.00));
        props.put("DEC_NET_AMOUNT", new Variant(0.00));
        props.put("DEC_DOUBTFUL_QTY", new Variant(0.00));
        props.put("DEC_REMARK", new Variant(0.00));
        props.put("DEC_REMARK_DOUBTFUL", new Variant(""));
        props.put("DEC_PROPOSED_ACTION_BY_IC", new Variant(""));
        props.put("DEC_PROPOSED_ACTION_DOUBTFUL", new Variant(""));

        props.put("JAN_BUDGET", new Variant(0.00));
        props.put("JAN_KG", new Variant(0.00));
        props.put("JAN_SQMTR", new Variant(0.00));
        props.put("JAN_GROSS", new Variant(0.00));
        props.put("JAN_DISCOUNT", new Variant(0.00));
        props.put("JAN_NET_AMOUNT", new Variant(0.00));
        props.put("JAN_DOUBTFUL_QTY", new Variant(0.00));
        props.put("JAN_REMARK", new Variant(0.00));
        props.put("JAN_REMARK_DOUBTFUL", new Variant(""));
        props.put("JAN_PROPOSED_ACTION_BY_IC", new Variant(""));
        props.put("JAN_PROPOSED_ACTION_DOUBTFUL", new Variant(""));

        props.put("FEB_BUDGET", new Variant(0.00));
        props.put("FEB_KG", new Variant(0.00));
        props.put("FEB_SQMTR", new Variant(0.00));
        props.put("FEB_GROSS", new Variant(0.00));
        props.put("FEB_DISCOUNT", new Variant(0.00));
        props.put("FEB_NET_AMOUNT", new Variant(0.00));
        props.put("FEB_DOUBTFUL_QTY", new Variant(0.00));
        props.put("FEB_REMARK", new Variant(0.00));
        props.put("FEB_REMARK_DOUBTFUL", new Variant(""));
        props.put("FEB_PROPOSED_ACTION_BY_IC", new Variant(""));
        props.put("FEB_PROPOSED_ACTION_DOUBTFUL", new Variant(""));

        props.put("MAR_BUDGET", new Variant(0.00));
        props.put("MAR_KG", new Variant(0.00));
        props.put("MAR_SQMTR", new Variant(0.00));
        props.put("MAR_GROSS", new Variant(0.00));
        props.put("MAR_DISCOUNT", new Variant(0.00));
        props.put("MAR_NET_AMOUNT", new Variant(0.00));
        props.put("MAR_DOUBTFUL_QTY", new Variant(0.00));
        props.put("MAR_REMARK", new Variant(0.00));
        props.put("MAR_REMARK_DOUBTFUL", new Variant(""));
        props.put("MAR_PROPOSED_ACTION_BY_IC", new Variant(""));
        props.put("MAR_PROPOSED_ACTION_DOUBTFUL", new Variant(""));

        props.put("PARTY_STATUS", new Variant(""));
        props.put("SYSTEM_STATUS", new Variant(""));
        props.put("REMARKS", new Variant(""));
        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));

        props.put("PROJECTED_QTY", new Variant(0.00));
        props.put("PROJECTED_VALUE", new Variant(0.00));

        props.put("UPN", new Variant(""));
        props.put("POTENTIAL", new Variant(0.00));
        props.put("PREV_YEAR_QTY", new Variant(0.00));
        props.put("PREV_YEAR_VALUE", new Variant(0.00));
        props.put("PREV_PREV_YEAR_QTY", new Variant(0.00));
        props.put("PREV_PREV_YEAR_VALUE", new Variant(0.00));

        props.put("WIP_QTY", new Variant(0.00));
        props.put("WIP_VALUE", new Variant(0.00));
        props.put("STOCK_QTY", new Variant(0.00));
        props.put("STOCK_VALUE", new Variant(0.00));
        props.put("OBSOLETE_QTY", new Variant(0.00));
        props.put("OBSOLETE_VALUE", new Variant(0.00));
        props.put("ACESS_QTY", new Variant(0.00));
        props.put("ACESS_VALUE", new Variant(0.00));

        props.put("INCHARGE", new Variant(""));
        props.put("SIZE_CRITERIA", new Variant(""));
        props.put("PARTY_GROUP", new Variant(""));

        props.put("CANCEL_QTY", new Variant(0.00));
        props.put("CANCEL_VALUE", new Variant(0.00));

        //props.put("CHEM_TRT_IN",new Variant(""));         
        //props.put("PIN_IND",new Variant(""));
        //props.put("CHARGES",new Variant(""));
        //props.put("SPR_IND",new Variant(""));
        //props.put("SQM_IND",new Variant(""));       
    }

}
