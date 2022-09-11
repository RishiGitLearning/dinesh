/*
 * clsFeltWeavingDetails.java
 * This class is used for holding the data of Felt Weaving Production in HashMap
 * 
 * Created on March 14, 2013, 1:09 PM
 */

package EITLERP.Production.Felt_Order_Updation;

import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Vivek Kumar
 */
public class clsFeltOrderUpdDetails{
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
    public clsFeltOrderUpdDetails() {
        props=new HashMap();
        props.put("FLT_AMEND_ID",new Variant(""));
        props.put("FLT_AMEND_DATE",new Variant(""));
        props.put("FLT_AMEND_REASON",new Variant(""));
        props.put("FLT_PIECE_NO",new Variant(""));
        props.put("FLT_ORDER_DATE",new Variant(""));
        props.put("FLT_PRODUCT_CODE",new Variant(""));
        props.put("FLT_PARTY_CODE",new Variant(""));
        props.put("FLT_LENGTH",new Variant(0.00));
        props.put("FLT_WIDTH",new Variant(0.00));
        props.put("FLT_GSQ",new Variant(""));
        props.put("FLT_WIEGHT",new Variant(""));
        props.put("FLT_GROUP",new Variant(""));
        props.put("FLT_REQ_DATE",new Variant(""));
        props.put("FLT_COMM_DATE",new Variant(""));
        props.put("FLT_REV_REQ_DATE",new Variant(""));
        props.put("FLT_REV_COMM_DATE",new Variant(""));
        props.put("FLT_REV_REQ_REASON",new Variant(""));
        props.put("FLT_REV_COMM_REASON",new Variant(""));
        props.put("FLT_WVG_AGREED_DATE",new Variant(""));
        props.put("FLT_REV_AGREED_DATE",new Variant(""));
        props.put("FLT_AGREED_REMARK",new Variant(""));
        props.put("FLT_AGREED_IND",new Variant(""));
        props.put("FLT_REMARKS",new Variant(""));
        props.put("FLT_SYTLE",new Variant(""));
        props.put("FLT_PRIORITY",new Variant(""));
        props.put("FLT_PRIORITY_DATE",new Variant(""));
        props.put("FLT_MACHINE_NO",new Variant(""));
        props.put("FLT_POSITION",new Variant(""));
        props.put("FLT_REF_NO",new Variant(""));
        props.put("FLT_CONF_NO",new Variant(""));
        props.put("FLT_PO_NO",new Variant(""));
        props.put("FLT_PO_DATE",new Variant(""));
        props.put("FLT_PO_REMARKS",new Variant(""));
        props.put("FLT_WVG_DATE",new Variant(""));
        props.put("FLT_NDL_DATE",new Variant(""));
        props.put("FLT_MND_DATE",new Variant(""));
        props.put("FLT_NDL_DATE",new Variant(""));
        props.put("FLT_PROD_IND",new Variant(""));
        props.put("FLT_AMEND_REMARK",new Variant(""));
        props.put("FLT_MFG_STATUS",new Variant(""));
   
 
   }
}