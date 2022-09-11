/*
 * clsSalesInvoiceDetail.java
 *
 * Created on January 25, 2009, 11:22 AM
 */
package EITLERP.FeltSales.FeltInvReport;

//import EITLERP.Sales.*;
import java.util.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import java.io.*;
import EITLERP.Sales.*;

/**
 *
 * @author root
 */
public class clsFeltSalesInvoiceDetail {

    private HashMap props;
    public boolean Ready = false;

    public Variant getAttribute(String PropName) {
        return (Variant) props.get(PropName);
    }

    public void setAttribute(String PropName, Object Value) {
        props.put(PropName, new Variant(Value));
    }

    public void setAttribute(String PropName, int Value) {
        props.put(PropName, new Variant(Value));
    }

    public void setAttribute(String PropName, long Value) {
        props.put(PropName, new Variant(Value));
    }

    public void setAttribute(String PropName, double Value) {
        props.put(PropName, new Variant(Value));
    }

    public void setAttribute(String PropName, float Value) {
        props.put(PropName, new Variant(Value));
    }

    public void setAttribute(String PropName, boolean Value) {
        props.put(PropName, new Variant(Value));
    }

    /**
     * Creates a new instance of clsSalesInvoiceDetail
     */
    public clsFeltSalesInvoiceDetail() {
        props = new HashMap();
//        
//        props.put("COMPANY_ID",new Variant(0));
//        props.put("INVOICE_TYPE",new Variant(0));
//        props.put("INVOICE_NO",new Variant(""));
//        props.put("SR_NO",new Variant(0));
//        props.put("INVOICE_DATE",new Variant(""));        
//        props.put("QUALITY_NO",new Variant(""));
//        props.put("PATTERN_CODE",new Variant(""));
//        props.put("PIECE_NO",new Variant(""));
//        props.put("PARTY_CODE",new Variant(""));        
//        props.put("WAREHOUSE_CODE",new Variant(0));
//        props.put("UNIT_CODE",new Variant(""));
//        props.put("FLAG_DEF_CODE",new Variant(""));
//        props.put("RATE",new Variant(0));
//        props.put("TRD_DISC_TYPE",new Variant(""));
//        props.put("SEASON_CODE",new Variant(""));
//        props.put("DEF_DISC_PER",new Variant(0));
//        props.put("ADDL_DISC_PER",new Variant(0));
//        props.put("GROSS_SQ_MTR",new Variant(0));
//        props.put("GROSS_KG",new Variant(0));
//        props.put("GROSS_QTY",new Variant(0));
//        props.put("NET_QTY",new Variant(0));
//        props.put("GROSS_AMOUNT",new Variant(0));
//        props.put("TRD_DISCOUNT",new Variant(0));
//        props.put("DEF_DISCOUNT",new Variant(0));
//        props.put("ADDL_DISCOUNT",new Variant(0));        
//        props.put("NET_AMOUNT",new Variant(0));
//        props.put("EXCISABLE_VALUE",new Variant(0));        
//        props.put("BALE_NO",new Variant(""));
//        props.put("EXPORT_CATEGORY",new Variant(""));
//        props.put("FILLER",new Variant(""));
//        props.put("RECEIPT_DATE",new Variant(""));        
//        props.put("BASIC_EXC",new Variant(0));
//        props.put("ADDITIONAL_DUTY",new Variant(0));
//        props.put("AGENT_SR_NO",new Variant(0));
//        props.put("AGENT_ALPHA",new Variant(""));
//        props.put("GATEPASS_NO",new Variant(""));
//        props.put("REMARKS",new Variant(""));
//        

        props.put("BALE_NO", new Variant(""));
        props.put("PIECE_NO", new Variant(""));
        props.put("MACHINE_NO", new Variant(""));
        props.put("POSITION_NO", new Variant(""));
        props.put("PARTY_CODE", new Variant(""));
        props.put("PRODUCT_CODE", new Variant(""));
        props.put("GROUP_NAME", new Variant(""));
        props.put("STYLE", new Variant(""));
        props.put("LENGTH", new Variant(0));
        props.put("WIDTH", new Variant(0));
        props.put("GSM", new Variant(0));
        props.put("THORITICAL_WEIGHT", new Variant(0));
        props.put("SQMTR", new Variant(0));
        props.put("SYN_PER", new Variant(""));
        props.put("ACTUAL_WEIGHT", new Variant(0));
        props.put("ACTUAL_LENGTH", new Variant(0));
        props.put("ACTUAL_WIDTH", new Variant(0));
        props.put("INVOICE_NO", new Variant(""));
        props.put("INVOICE_DATE", new Variant(""));
        props.put("INVOICE_PARTY", new Variant(""));
        props.put("RATE", new Variant(0));
        props.put("BAS_AMT", new Variant(0));
        props.put("DISC_PER", new Variant(0));
        props.put("DISC_AMT", new Variant(0));
        props.put("DISC_BAS_AMT", new Variant(0));
        props.put("EXCISE", new Variant(0));
        props.put("SEAM_CHG", new Variant(0));
        props.put("SEAM_CHG_PER", new Variant(0));
        props.put("INSURANCE_AMT", new Variant(0));
        props.put("CHEM_TRT_CHG", new Variant(0));
        props.put("PIN_CHG", new Variant(0));
        props.put("SPIRAL_CHG", new Variant(0));
        props.put("INS_IND", new Variant(0));
        props.put("CST", new Variant(0));
        props.put("VAT", new Variant(0));
        props.put("SD_AMT", new Variant(0));
        props.put("INVOICE_AMT", new Variant(0));
        props.put("SQM_IND", new Variant(0));
        props.put("OUT_STANDING_AMT", new Variant(0));
        props.put("ADV_AMT", new Variant(0));
        props.put("GRP_CRITICAL_LIMIT", new Variant(0));
        props.put("PARTY_CRITICAL_LIMIT", new Variant(0));
        props.put("CHARGE_CODE", new Variant(""));
        props.put("GRP_OUT_STANDING_AMT", new Variant(0));
        props.put("GRP_MAIN_PARTY_CODE", new Variant(""));
        props.put("INV_CRITICAL_LIMIT_AMT", new Variant(0));
        props.put("CRITICAL_LIMIT_AMT", new Variant(0));
        props.put("INDICATOR", new Variant(0));
        props.put("FLAG", new Variant(0));
        props.put("CST2", new Variant(0));
        props.put("CST5", new Variant(0));
        props.put("VAT1", new Variant(0));
        props.put("VAT4", new Variant(0));
        props.put("IGST_PER", new Variant(0));
        props.put("IGST_AMT", new Variant(0));
        props.put("CGST_PER", new Variant(0));
        props.put("CGST_AMT", new Variant(0));
        props.put("SGST_PER", new Variant(0));
        props.put("SGST_AMT", new Variant(0));
        props.put("GST_COMP_CESS_PER", new Variant(0));
        props.put("GST_COMP_CESS_AMT", new Variant(0));
        props.put("ADV_DOC_NO", new Variant(""));
        props.put("ADV_RECEIVED_AMT", new Variant(0));
        props.put("ADV_AGN_INV_AMT", new Variant(0));
        props.put("ADV_AGN_IGST_AMT", new Variant(0));
        props.put("ADV_AGN_SGST_AMT", new Variant(0));
        props.put("ADV_AGN_CGST_AMT", new Variant(0));
        props.put("ADV_AGN_GST_COMP_CESS_AMT", new Variant(0));
        
        props.put("SURCHARGE_PER", new Variant(0));
        props.put("SURCHARGE_RATE", new Variant(0));
        props.put("GROSS_RATE", new Variant(0));
        
        props.put("MATERIAL_CODE", new Variant(""));
        props.put("TCS_PER", new Variant(0));
        props.put("TCS_AMT", new Variant(0));
    }

}
