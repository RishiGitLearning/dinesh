/*
 * clsFeltPackingSlipDetails.java
 * This class is used for holding the data of FELT PACKING in HashMap
 * 
 * Created on July 19, 2013, 5:26 PM
 */

package EITLERP.FeltSales.FeltScheme;

import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Vivek Kumar
 */
public class clsFeltSchemeDetails{
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
    
    /** Creates new clsFeltPackingDetails */
    public clsFeltSchemeDetails() {
        props=new HashMap();
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("PARTY_CODE",new Variant(""));
        props.put("PARTY_NAME",new Variant(""));
        props.put("MC_POSITION",new Variant(""));
        props.put("MC_POSITION_DESC",new Variant(""));
        props.put("PARTY_CODE1",new Variant(""));
        props.put("PARTY_NAME1",new Variant(""));
        props.put("MC_NO",new Variant(""));
        props.put("ORDER_NO",new Variant(""));
        props.put("ORDER_DATE",new Variant(""));
//        props.put("ORDER_TO_DATE",new Variant(""));
//        props.put("ORDER_FROM_DATE",new Variant(""));
//        props.put("FIN_YEAR",new Variant(""));
        props.put("REQ_MONTH",new Variant(""));
        props.put("PIECE_NO",new Variant(""));
        props.put("EMAIL",new Variant(""));
        props.put("PRODUCT_CODE",new Variant(""));
        props.put("GROUP_NAME",new Variant(""));
        props.put("PRODUCT_GROUP_DESC",new Variant(""));
        props.put("BASIC_VALUE",new Variant(""));
        props.put("DISCOUNT",new Variant(""));
        props.put("ORDER_AMT",new Variant(""));
        props.put("LENGTH",new Variant(""));
        props.put("WIDTH",new Variant(""));
        props.put("GSM",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));        
    }
}