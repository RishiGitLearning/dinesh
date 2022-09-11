/*
 * clsFeltWeavingDetails.java
 * This class is used for holding the data of Felt Weaving Production in HashMap
 * 
 * Created on March 14, 2013, 1:09 PM
 */

package EITLERP.Production.FeltCreditNote;

import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Vivek Kumar
 */
public class clsFeltCreditNoteDetails{
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
    
    
    /** Creates new clsFeltWeavingDetails */
    public clsFeltCreditNoteDetails() {
        props=new HashMap();
        
        props.put("CN_DATE",new Variant(""));
        props.put("CN_FROM_DATE",new Variant(""));
        props.put("CN_TO_DATE",new Variant(""));
        props.put("CN_TYPE",new Variant(""));
        props.put("CN_ID",new Variant(""));
        props.put("SR_NO",new Variant(""));
        props.put("CN_INV_PIECE_NO",new Variant(""));
        props.put("CN_INVOICE_NO",new Variant(""));
        props.put("CN_INVOICE_DATE",new Variant(""));
        props.put("CN_PARTY_CODE",new Variant(""));
        props.put("CN_PARTY_NAME",new Variant(""));
        props.put("CN_INV_PRODUCT_CODE",new Variant(""));
        props.put("CN_PRODUCT_GRUP",new Variant(""));
        props.put("CN_INV_WI_SQMTR",new Variant(""));
        props.put("CN_GROSS_VALUE",new Variant(""));
        props.put("CN_DISC_BILL",new Variant(""));
	props.put("CN_YEAR_END_DISC",new Variant(""));
	props.put("CN_YEAR_END_DISC_RS",new Variant(""));
	props.put("CN_UNADJUSTED_DISC",new Variant(""));
	props.put("CN_UNADJUSTED_RS",new Variant(""));
	props.put("CN_INVOICE_AMT",new Variant(""));
        props.put("CN_RATE",new Variant(""));
        props.put("CN_BASIC_VALUE",new Variant(""));
        props.put("CN_NET_BASIC",new Variant(""));
        props.put("CN_OEM_VALUE",new Variant(""));
        props.put("CN_OEM",new Variant(""));
        props.put("CN_RECD_AMT",new Variant(""));
        props.put("CN_EXT1",new Variant(""));
        props.put("CN_EXT2",new Variant(""));
        props.put("CN_EXT3",new Variant(""));
        props.put("TOTAL_NET_AMOUNT",new Variant(""));
        props.put("VALUE_DATE",new Variant(""));
        props.put("AMOUNT",new Variant(""));
        props.put("VOUCHER",new Variant(""));
        props.put("F6",new Variant(""));
        props.put("SALES_REMARKS",new Variant(""));
        props.put("AUDIT_REMARKS",new Variant(""));
        props.put("INSURANCE",new Variant(""));
        props.put("CST",new Variant(""));
        props.put("EXCISE_DUTY",new Variant(""));
        props.put("COM_YEAR_DISC",new Variant(""));
        props.put("COM_YEAR_AMT",new Variant(""));
        props.put("COM_UDJ_DISC",new Variant(""));
        props.put("COM_UDJ_AMT",new Variant(""));
        props.put("COM_OEM_AMT",new Variant(""));
        props.put("COMPENSATION_AMT",new Variant(""));
        props.put("GOODS_RETURN_AMT",new Variant(""));
        props.put("INSURANCE_CLAIM_AMT",new Variant(""));
        
        props.put("CGST_PER", new Variant(""));
        props.put("CGST_AMT", new Variant(""));
        props.put("SGST_PER", new Variant(""));
        props.put("SGST_AMT", new Variant(""));
        props.put("IGST_PER", new Variant(""));
        props.put("IGST_AMT", new Variant(""));

        props.put("CGST_ITC_PER", new Variant(""));
        props.put("CGST_ITC_AMT", new Variant(""));
        props.put("SGST_ITC_PER", new Variant(""));
        props.put("SGST_ITC_AMT", new Variant(""));
        props.put("IGST_ITC_PER", new Variant(""));
        props.put("IGST_ITC_AMT", new Variant(""));

        props.put("PARTY_REF_DOC_NO", new Variant(""));
        props.put("PARTY_REF_DOC_DATE", new Variant(""));
 
   }
}