/*
 * clsFeltExportInvoiceDetails.java
 * This class is used for holding the data of FELT EXPORT INVOICE in HashMap
 * 
 * Created on June 19, 2013, 4:26 PM
 */

package EITLERP.Production.FeltExportInvoice;

import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Vivek Kumar
 */
public class clsFeltExportInvoiceDetails{
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
    
    
    /** Creates new clsFeltExportInvoiceDetails */
    public clsFeltExportInvoiceDetails() {
        props=new HashMap();
        props.put("INVOICE_DATE",new Variant(""));
        props.put("INVOICE_NO",new Variant(""));
        props.put("PIECE_NO",new Variant(""));
        props.put("FELT_SIZE",new Variant(""));
        props.put("WEIGHT",new Variant(0.00));
        props.put("AMOUNT",new Variant(0.00));
        props.put("BALE_NO",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));        
    }
}