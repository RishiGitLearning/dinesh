/*
 * clsFeltWeavingDetails.java
 * This class is used for holding the data of Felt Weaving Production in HashMap
 * 
 * Created on March 14, 2013, 1:09 PM
 */

package EITLERP.FeltSales.FeltPieceAmend;

import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Vivek Kumar
 */
public class clsFeltPieceAmendDetails{
    private HashMap props;
    public boolean Ready = false;
    
    public Variant getAttribute(String PropName) {
        return (Variant) props.get(PropName);
    }
    
    public void setAttribute(String PropName,Object Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,int Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,long Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,double Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,float Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,boolean Value) {
        props.put(PropName,new Variant(Value));
    }
    
    
    /** Creates new clsFeltWeavingDetails */
    public clsFeltPieceAmendDetails() {
        props=new HashMap();
        props.put("FELT_AMEND_ID",new Variant(""));
        props.put("FELT_AMEND_DATE",new Variant(""));
        props.put("FELT_AMEND_REASON",new Variant(""));
        props.put("FELT_AMEND_PIECE_NO",new Variant(""));
        props.put("FELT_AMEND_ORDER_DATE",new Variant(""));
        props.put("FELT_AMEND_PRODUCT_CODE",new Variant(""));
        props.put("FELT_AMEND_PARTY_CODE",new Variant(""));
        props.put("FELT_AMEND_PARTY_NAME",new Variant(""));
        props.put("FELT_AMEND_LENGTH",new Variant(0.00));
        props.put("FELT_AMEND_WIDTH",new Variant(0.00));
        props.put("FELT_AMEND_GSM",new Variant(""));
        props.put("FELT_AMEND_WEIGHT",new Variant(""));
        props.put("FELT_AMEND_ACTUAL_WEIGHT",new Variant(""));
        props.put("FELT_AMEND_GROUP",new Variant(""));
        props.put("FELT_AMEND_SYTLE",new Variant(""));
        props.put("FELT_AMEND_REMARK",new Variant(""));
        props.put("FELT_AMEND_MFG_STATUS",new Variant(""));
        props.put("FELT_AMEND_SQMTR",new Variant(""));
        props.put("FELT_AMEND_BILL_LENGTH",new Variant(""));
        props.put("FELT_AMEND_BILL_WIDTH",new Variant(""));
        props.put("FELT_AMEND_BILL_GSM",new Variant(""));
        props.put("FELT_AMEND_BILL_WEIGHT",new Variant(""));
        props.put("FELT_AMEND_BILL_SQMTR",new Variant(""));
        props.put("FELT_AMEND_BILL_PRODUCT_CODE",new Variant(""));
        props.put("FELT_AMEND_INCHARGE_NAME",new Variant(""));
        props.put("FELT_AMEND_EXPORT_INV_NO",new Variant(""));
        props.put("FELT_AMEND_EXPORT_INV_DATE",new Variant(""));
        props.put("FELT_AMEND_EXPORT_INV_AMT",new Variant(0.00));
        props.put("FELT_AMEND_CURR_UNIT",new Variant(""));
        props.put("FELT_AMEND_CURR_RATE",new Variant(0.00));
        props.put("FELT_AMEND_CURR_AMT",new Variant(0.00));
        props.put("FELT_AMEND_UNIT_NET_PRICE",new Variant(0.00));
        props.put("FELT_AMEND_UNIT_NET_SEAM",new Variant(0.00));
        props.put("FELT_AMEND_NOTIONAL_PL_AMT",new Variant(0.00));
        props.put("FELT_AMEND_PO_NO",new Variant(""));
        props.put("FELT_AMEND_PO_DATE",new Variant(""));        
        props.put("FELT_AMEND_MATERIAL_CODE",new Variant(""));
    }
}
