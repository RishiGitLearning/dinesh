/*
 * clsNoDataObject.java
 *
 * Created on April 6, 2004, 9:32 AM
 */

package EITLERP.Purchase;


import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
   
/**
 *
 * @author  nrpatel
 * @version
 */

public class clsRateApprovalItem {
    
    private HashMap props;
    public boolean Ready = false;
    
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
    public clsRateApprovalItem() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("APPROVAL_NO",new Variant(""));
        props.put("SR_NO",new Variant(0));
        props.put("ITEM_ID",new Variant(""));
        props.put("ITEM_EXTRA_DESC",new Variant(""));
        props.put("HSN_SAC_CODE",new Variant(""));
        props.put("MAKE",new Variant(""));
        props.put("PRICE_LIST_NO",new Variant(""));
        props.put("SUPP_ID",new Variant(""));
        props.put("QUOT_ID",new Variant(""));
        props.put("QUOT_SR_NO",new Variant(0));
        props.put("LAST_PO_NO",new Variant(""));
        props.put("LAST_PO_DATE",new Variant(""));
        props.put("LAST_PO_RATE",new Variant(0));
        props.put("LAST_LANDED_RATE",new Variant(0));
        props.put("LAST_PO_QTY",new Variant(0));
        props.put("RATE_DIFFERENCE",new Variant(0));
        props.put("RATE_DIFFERENCE_PER",new Variant(0));
        props.put("RATE_DIFFERENCE_RATE",new Variant(0));
        props.put("RATE_DIFFERENCE_PER_RATE",new Variant(0));
        props.put("CURRENT_RATE",new Variant(0));
        props.put("CURRENT_LAND_RATE",new Variant(0));
        props.put("CURRENT_QTY",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("PAYMENT_TERM",new Variant(""));
        props.put("PRICE_BASIS_TERM",new Variant(""));
        
        props.put("IGST_TERM",new Variant(""));
        props.put("CGST_TERM",new Variant(""));
        props.put("SGST_TERM",new Variant(""));
        props.put("COMPOSITION_TERM",new Variant(""));
        props.put("RCM_TERM",new Variant(""));
        props.put("GST_COMPENSATION_CESS_TERM",new Variant(""));
        
        props.put("DISCOUNT_TERM",new Variant(""));
        props.put("EXCISE_TERM",new Variant(""));
        props.put("ST_TERM",new Variant(""));
        props.put("PF_TERM",new Variant(""));
        props.put("FREIGHT_TERM",new Variant(""));
        props.put("OCTROI_TERM",new Variant(""));
        props.put("FOB_TERM",new Variant(""));
        props.put("CIE_TERM",new Variant(""));
        props.put("INSURANCE_TERM",new Variant(""));
        props.put("TCC_TERM",new Variant(""));
        props.put("CENVAT_TERM",new Variant(""));
        props.put("DESPATCH_TERM",new Variant(""));
        props.put("SERVICE_TAX_TERM",new Variant(""));
        props.put("OTHERS_TERM",new Variant(""));
        props.put("UNIT_ID",new Variant(0));   
        props.put("APPROVED",new Variant(false));
    }
    
}
