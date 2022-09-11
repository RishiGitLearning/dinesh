/*
 * clsFeltReopenBaleSlipDetails.java
 * This class is used for holding the data of FELT PACKING in HashMap
 * 
 * Created on July 19, 2013, 5:26 PM
 */

package EITLERP.FeltSales.FeltTransporterWeight;

import EITLERP.FeltSales.ReopenBale12.*;
import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Vivek Kumar
 */
public class clsFeltTransporterWeigthEntryFormDetail{
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
    
    /** Creates new clsFeltReopenBaleDetails */
    public clsFeltTransporterWeigthEntryFormDetail() {
        props=new HashMap();
        props.put("DOC_NO",new Variant(""));
        props.put("PROCESSING_DATE",new Variant(""));
        props.put("BALE_NO",new Variant(""));
        props.put("PIECE_NO",new Variant(0.00));
        props.put("PARTY_CODE",new Variant(0.00));
        props.put("CHARGE_CODE_OLD",new Variant(0.00));
        props.put("CHARGE_CODE_NEW",new Variant(0));
        props.put("CRITICAL_LIMIT_OLD",new Variant(""));
        props.put("CRITICAL_LIMIT_NEW",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));        
    }
}