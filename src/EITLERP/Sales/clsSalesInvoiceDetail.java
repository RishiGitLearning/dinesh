/*
 * clsSalesInvoiceDetail.java
 *
 * Created on January 25, 2009, 11:22 AM
 */

package EITLERP.Sales;

import java.util.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import java.io.*;
import EITLERP.Sales.*;

/**
 *
 * @author  root
 */
public class clsSalesInvoiceDetail {
    
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
    
    /** Creates a new instance of clsSalesInvoiceDetail */
    public clsSalesInvoiceDetail() {
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(0));
        props.put("INVOICE_TYPE",new Variant(0));
        props.put("INVOICE_NO",new Variant(""));
        props.put("SR_NO",new Variant(0));
        props.put("INVOICE_DATE",new Variant(""));        
        props.put("QUALITY_NO",new Variant(""));
        props.put("PATTERN_CODE",new Variant(""));
        props.put("PIECE_NO",new Variant(""));
        props.put("PARTY_CODE",new Variant(""));        
        props.put("WAREHOUSE_CODE",new Variant(0));
        props.put("UNIT_CODE",new Variant(""));
        props.put("FLAG_DEF_CODE",new Variant(""));
        props.put("RATE",new Variant(0));
        props.put("TRD_DISC_TYPE",new Variant(""));
        props.put("SEASON_CODE",new Variant(""));
        props.put("DEF_DISC_PER",new Variant(0));
        props.put("ADDL_DISC_PER",new Variant(0));
        props.put("GROSS_SQ_MTR",new Variant(0));
        props.put("GROSS_KG",new Variant(0));
        props.put("GROSS_QTY",new Variant(0));
        props.put("NET_QTY",new Variant(0));
        props.put("GROSS_AMOUNT",new Variant(0));
        props.put("TRD_DISCOUNT",new Variant(0));
        props.put("DEF_DISCOUNT",new Variant(0));
        props.put("ADDL_DISCOUNT",new Variant(0));        
        props.put("NET_AMOUNT",new Variant(0));
        props.put("EXCISABLE_VALUE",new Variant(0));        
        props.put("BALE_NO",new Variant(""));
        props.put("EXPORT_CATEGORY",new Variant(""));
        props.put("FILLER",new Variant(""));
        props.put("RECEIPT_DATE",new Variant(""));        
        props.put("BASIC_EXC",new Variant(0));
        props.put("ADDITIONAL_DUTY",new Variant(0));
        props.put("AGENT_SR_NO",new Variant(0));
        props.put("AGENT_ALPHA",new Variant(""));
        props.put("GATEPASS_NO",new Variant(""));
        props.put("REMARKS",new Variant(""));
        
    }
    
}
