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
 
public class clsVoucherLedgerItemSummary {

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
    public clsVoucherLedgerItemSummary() {
        props=new HashMap();
        props.put("VOUCHER_NO",new Variant(""));
        props.put("VOUCHER_DATE",new Variant(""));
        props.put("BOOK_CODE", new Variant(""));
        props.put("EFFECT",new Variant(""));
        props.put("MAIN_ACCOUNT_CODE",new Variant(""));
        props.put("AMOUNT",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("CHEQUE_NO", new Variant(""));
        props.put("CHEQUE_DATE",new Variant(""));
        props.put("PO_NO",new Variant(""));
        props.put("PO_DATE",new Variant(""));
        props.put("INVOICE_NO",new Variant(""));
        props.put("INVOICE_DATE",new Variant(""));
        props.put("DEBIT_AMOUNT",new Variant(0));
        props.put("CREDIT_AMOUNT",new Variant(0));
        props.put("CLOSING_BALANCE",new Variant(0));
    }
    

}
