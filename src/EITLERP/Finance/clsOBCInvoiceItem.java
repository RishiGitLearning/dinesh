/*
 * clsMRItem.java
 *
 * Created on April 19, 2004, 2:27 PM
 */

package EITLERP.Finance;


import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
 
/**
 *
 * @author  jadave
 * @version 
 */
 
public class clsOBCInvoiceItem {

    private HashMap props;    
    public boolean Ready = false;

    public HashMap colVoucherDetailDocs=new HashMap();
    
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

    
    /** Creates new clsMRItem */
    public clsOBCInvoiceItem() {
        props=new HashMap();    
        props.put("COMPANY_ID",new Variant(0));
        props.put("VOUCHER_NO",new Variant(""));
        props.put("SR_NO",new Variant(0));
        props.put("EFFECT",new Variant("D"));
        props.put("ACCOUNT_ID",new Variant(0));
        props.put("MAIN_ACCOUNT_CODE",new Variant(0));
        props.put("SUB_ACCOUNT_CODE",new Variant(0));
        props.put("AMOUNT",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("CANCELLED",new Variant(0));
        props.put("CHANGED",new Variant(0));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("PO_NO",new Variant(""));
        props.put("PO_DATE",new Variant(""));
        props.put("INVOICE_NO",new Variant(""));
        props.put("INVOICE_DATE",new Variant(""));
        props.put("GRN_NO",new Variant(""));
        props.put("GRN_DATE",new Variant(""));
        props.put("GRN_TYPE",new Variant(1));
        props.put("MODULE_ID",new Variant(0));
        props.put("INVOICE_AMOUNT",new Variant(0));
        props.put("REF_COMPANY_ID",new Variant(0));
        props.put("IS_DEDUCTION",new Variant(0));
        props.put("DEDUCTION_TYPE",new Variant(1));
    }
    

}
