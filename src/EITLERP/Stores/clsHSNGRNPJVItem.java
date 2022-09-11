/*
 * clsNoDataObject.java
 *
 * Created on April 6, 2004, 9:32 AM
 */

package EITLERP.Stores;
 

import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;

 
/**
 *
 * @author  nrpatel
 * @version
 */
 
public class clsHSNGRNPJVItem {
    
    private HashMap props;
    public boolean Ready = false;
    public HashMap colHSNGRNPJVItems=new HashMap();
    
    
    public Variant getAttribute(String PropName)
 {
     return (Variant) props.get(PropName);
    }
    
    public void setAttribute(String PropName,Object Value)
 {
     props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,int Value)
 {
     props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,long Value)
 {
     props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,double Value)
 {
     props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,float Value)
 {
     props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,boolean Value)
 {
     props.put(PropName,new Variant(Value));
    }
    
    
    /** Creates new clsNoDataObject */
    public clsHSNGRNPJVItem() {
        props=new HashMap();
        props.put("GRN_NO",new Variant(""));
        props.put("GRN_DATE",new Variant(""));
        props.put("HSN_CODE",new Variant(""));
        props.put("INVOICE_AMOUNT",new Variant(0));
        props.put("INVOICE_CGST",new Variant(0));
        props.put("INVOICE_SGST",new Variant(0));
        props.put("INVOICE_IGST",new Variant(0));
        props.put("INVOICE_RCM",new Variant(0));
        props.put("INVOICE_COMPOSITION",new Variant(0));
        props.put("INVOICE_GST_COMP_CESS",new Variant(0));
        props.put("RECEIVED_AMOUNT",new Variant(0));
        props.put("RECEIVED_CGST",new Variant(0));
        props.put("RECEIVED_SGST",new Variant(0));
        props.put("RECEIVED_IGST",new Variant(0));
        props.put("RECEIVED_RCM",new Variant(0));
        props.put("RECEIVED_COMPOSITION",new Variant(0));
        props.put("RECEIVED_GST_COMP_CESS",new Variant(0));
        props.put("DEBIT_NOTE_AMOUNT",new Variant(0));
        props.put("DEBIT_NOTE_CGST",new Variant(0));
        props.put("DEBIT_NOTE_SGST",new Variant(0));
        props.put("DEBIT_NOTE_IGST",new Variant(0));
        props.put("DEBIT_NOTE_RCM",new Variant(0));
        props.put("DEBIT_NOTE_COMPOSITION",new Variant(0));
        props.put("DEBIT_NOTE_GST_COMP_CESS",new Variant(0));
        
        props.put("APPROVED", new Variant(false));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED", new Variant(false));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("HIERARCHY_ID", new Variant(0));
        props.put("CANCELLED", new Variant(false));
        
        props.put("CGST_INPUT_CR_PER",new Variant(0));
        props.put("CGST_INPUT_CR_AMT",new Variant(0));
        props.put("SGST_INPUT_CR_PER",new Variant(0));
        props.put("SGST_INPUT_CR_AMT",new Variant(0));
        props.put("IGST_INPUT_CR_PER",new Variant(0));
        props.put("IGST_INPUT_CR_AMT",new Variant(0));
    }
    
}
