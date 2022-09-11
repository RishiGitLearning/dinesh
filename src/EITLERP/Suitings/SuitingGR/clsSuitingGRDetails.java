/*
 * clsFeltWeavingDetails.java
 * This class is used for holding the data of Felt Weaving Production in HashMap
 * 
 * Created on March 14, 2013, 1:09 PM
 */

package EITLERP.Suitings.SuitingGR;

import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Vivek Kumar
 */
public class clsSuitingGRDetails{
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
    public clsSuitingGRDetails() {
        props=new HashMap();
        
        props.put("GR_ID",new Variant(""));
        props.put("GR_DATE",new Variant(""));
        props.put("GR_DESC_NO",new Variant(""));
        props.put("PARTY_CODE",new Variant(""));
        props.put("PARTY_NAME",new Variant(""));
        props.put("CITY",new Variant(""));
        props.put("INWARD_NO",new Variant(""));
        props.put("INWARD_DATE",new Variant(""));
        props.put("SR_NO",new Variant(""));
        props.put("GR_PIECE_NO",new Variant(""));
        props.put("GR_INVOICE_NO",new Variant(""));
        props.put("GR_INVOICE_DATE",new Variant(""));
        props.put("GR_ALPHA_INVOICE_NO",new Variant(""));
        props.put("GR_QUALITY_NO",new Variant(""));
        props.put("GR_SHADE",new Variant(""));
        props.put("GR_UNIT_CODE",new Variant(""));
        props.put("GR_GROSS_QTY",new Variant(""));
        props.put("FLAG",new Variant(""));
        props.put("GR_NET_QTY",new Variant(""));
        props.put("GR_QTY",new Variant(""));
        props.put("GR_RATE",new Variant(""));
        props.put("GR_INVOICE_DISC",new Variant(""));
        props.put("GR_ADD_DISC",new Variant(""));
	props.put("GR_INVOICE_AMOUNT",new Variant(""));
	props.put("GR_SHOT_LENGTH_PER",new Variant(""));
	props.put("GR_SHOT_LENGTH_AMOUNT",new Variant(""));
	props.put("GR_GROSS_AMOUNT",new Variant(""));
	props.put("GR_STOCK_LOT_PER",new Variant(""));
        props.put("GR_STOCK_LOT_AMOUNT",new Variant(""));
        props.put("GR_REVERSE_AMOUNT",new Variant(""));
        props.put("GR_NET_GR_AMOUNT",new Variant(""));
        props.put("GR_HSN_NO",new Variant(""));
        props.put("GR_CGST_PER",new Variant(""));
        props.put("GR_CGST_AMOUNT",new Variant(""));
        props.put("GR_SGST_PER",new Variant(""));
        props.put("GR_SGST_AMOUNT",new Variant(""));
        props.put("GR_IGST_PER",new Variant(""));
        props.put("GR_IGST_AMOUNT",new Variant(""));
        props.put("GR_RCM_PER",new Variant(""));
        props.put("GR_RCM_AMOUNT",new Variant(""));
        props.put("GR_TOTAL_AMT",new Variant(""));
        props.put("GR_LINK_NO",new Variant(""));
        
        props.put("HSN_NO",new Variant(""));
        props.put("INVOICE_NO",new Variant(""));
        props.put("INVOICE_DATE",new Variant(""));
        props.put("NET_AMOUNT",new Variant(0));
        props.put("NET_GR_AMOUNT",new Variant(0));
        props.put("CGST_AMOUNT",new Variant(0));
        props.put("SGST_AMOUNT",new Variant(0));
        props.put("IGST_AMOUNT",new Variant(0));        
        props.put("COMPOSITION_AMOUNT",new Variant(0));
        props.put("GR_QTY",new Variant(0));
        props.put("TOTAL_AMOUNT",new Variant(0));
        props.put("LINK_NO",new Variant(""));
 
   }
}