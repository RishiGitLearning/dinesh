/*
 * clsclsCreditNoteProcessDetail.java
 *
 * Created on February 3, 2009, 2:35 PM
 */

package EITLERP.Finance;

import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
 

/**
 *
 * @author  root
 */
public class clsCreditNoteProcessDetail {
    
    private HashMap props;    
    public boolean Ready = false;

    public HashMap colPolicyDetails=new HashMap();
    
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
    
    /** Creates a new instance of clsclsCreditNoteProcessDetail */
    public clsCreditNoteProcessDetail() {
        props=new HashMap();    
        props.put("COMPANY_ID",new Variant(0));
        props.put("DOC_NO",new Variant(""));
        props.put("SR_NO",new Variant(0));
        props.put("INVOICE_NO",new Variant(""));
        props.put("INVOICE_DATE",new Variant(""));
        props.put("PARTY_ID",new Variant(""));
        props.put("PARTY_MAIN_CODE",new Variant(""));
        props.put("AMOUNT",new Variant(0));
        props.put("QUALIFING_AMOUNT",new Variant(0));
        props.put("PERCENTAGE",new Variant(0.0));
        props.put("CREDIT_NOTE_NO",new Variant(""));
        props.put("SEASON_ID",new Variant(""));
        props.put("QUALITY_ID",new Variant(""));
        props.put("PIECE_NO",new Variant(""));
        props.put("DISCOUNT_AMOUNT",new Variant(0));
        props.put("VOUCHER_TYPE",new Variant(0));
        props.put("BOOK_CODE",new Variant(""));
        props.put("DEDUCTION_CODE",new Variant(""));
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("CANCELLED",new Variant(0));
        props.put("CHANGED",new Variant(0));
        props.put("CHANGED_DATE",new Variant(""));  
    }
    
}
