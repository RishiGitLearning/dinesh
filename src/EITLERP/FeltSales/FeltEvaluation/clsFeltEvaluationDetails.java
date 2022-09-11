/*
 * clsFeltPackingSlipDetails.java
 * This class is used for holding the data of FELT PACKING in HashMap
 * 
 * Created on July 19, 2013, 5:26 PM
 */

package EITLERP.FeltSales.FeltEvaluation;

import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Vivek Kumar
 */
public class clsFeltEvaluationDetails{
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
    public clsFeltEvaluationDetails() {
        props=new HashMap();
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("PARTY_CODE",new Variant(""));
        
        props.put("CANCEL_FLAG",new Variant(""));
        props.put("DELINK_FLAG",new Variant(""));
        props.put("DIVERSION_CLOSE_FLAG",new Variant(""));
        
        props.put("PIECE_NO",new Variant(""));
        props.put("PIECE_STAGE",new Variant(""));
        props.put("MACHINE_NO",new Variant(""));
        props.put("POSITION_NO",new Variant(""));
        props.put("STYLE_CODE",new Variant(""));
        props.put("LENGTH",new Variant(""));
        props.put("WIDTH",new Variant(""));
        props.put("WEIGHT",new Variant(""));
        props.put("GSM",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("OC_MONTH",new Variant(""));
        
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));        
    }
}