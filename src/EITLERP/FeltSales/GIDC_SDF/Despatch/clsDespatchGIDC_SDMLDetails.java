/*
 * 
 * 
 * 
 * 
 */

package EITLERP.FeltSales.GIDC_SDF.Despatch;

import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author 
 */
public class clsDespatchGIDC_SDMLDetails{
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
    public clsDespatchGIDC_SDMLDetails() {
        props=new HashMap();
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("PARTY_CODE",new Variant(""));
        props.put("PARTY_NAME",new Variant(""));
        
        props.put("SR_NO",new Variant(""));
        props.put("PIECE_NO",new Variant(""));
        props.put("PIECE_STAGE",new Variant(""));
        props.put("STYLE_CODE",new Variant(""));
        props.put("LENGTH",new Variant(""));
        props.put("WIDTH",new Variant(""));
        props.put("WEIGHT",new Variant(""));
        props.put("GSM",new Variant(""));
        props.put("SQMTR",new Variant(""));
        props.put("ACTUAL_LENGTH",new Variant(""));
        props.put("ACTUAL_WIDTH",new Variant(""));
        props.put("ACTUAL_WEIGHT",new Variant(""));
        props.put("WASTE_LENGTH",new Variant(""));
        props.put("WASTE_WEFT",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("RATE",new Variant(""));
        props.put("NET_AMOUNT",new Variant(""));
        props.put("NET_AMOUNT_IN_WORDS",new Variant(""));
        
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));        
    }
}