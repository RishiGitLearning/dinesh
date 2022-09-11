/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.Order;

import EITLERP.Variant;
import java.util.HashMap;

/**
 *
 * @author  DAXESH PRAJAPATI
 *
 */
public class clsFeltSalesOrderDetails {
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
    public clsFeltSalesOrderDetails() {
        props=new HashMap();
        
        props.put("S_ORDER_NO",new Variant(""));
        props.put("MACHINE_NO",new Variant(""));
        props.put("POSITION",new Variant(""));
        props.put("POSITION_DESC",new Variant(""));
        props.put("PIECE_NO",new Variant(""));
        props.put("PRODUCT_CODE",new Variant(""));
        props.put("PRODUCT_DESC",new Variant(""));
        props.put("S_GROUP",new Variant(""));
        props.put("LENGTH",new Variant(""));
        props.put("WIDTH",new Variant(""));
        props.put("GSM",new Variant(""));
        props.put("THORITICAL_WIDTH",new Variant(""));
        props.put("SQ_MTR",new Variant(""));
        props.put("STYLE",new Variant(""));
        props.put("REQ_MONTH",new Variant(""));
	props.put("SYN_PER",new Variant(""));
	props.put("REMARK",new Variant(""));
	props.put("CREATED_BY",new Variant(""));
	props.put("CREATED_DATE",new Variant(""));
	props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("OV_RATE",new Variant(""));
        props.put("OV_BAS_AMOUNT",new Variant(""));
        props.put("OV_CHEM_TRT_CHG",new Variant(""));
        props.put("OV_SPIRAL_CHG",new Variant(""));
        props.put("OV_PIN_CHG",new Variant(""));
        props.put("OV_SEAM_CHG",new Variant(""));
        props.put("OV_INS_IND",new Variant(""));
        props.put("OV_INS_AMT",new Variant(""));
        props.put("OV_EXCISE",new Variant(""));
        props.put("OV_DISC_PER",new Variant(""));
        props.put("OV_DISC_AMT",new Variant(""));
        props.put("OV_DISC_BASAMT",new Variant(""));
        props.put("OV_AMT",new Variant(""));
 
        props.put("CGST_PER",new Variant(0));
        props.put("CGST_AMT",new Variant(0));
        props.put("SGST_PER",new Variant(0));
        props.put("SGST_AMT",new Variant(0));
        props.put("IGST_PER",new Variant(0));
        props.put("IGST_AMT",new Variant(0));
        props.put("COMPOSITION_PER",new Variant(0));
        props.put("COMPOSITION_AMT",new Variant(0));
        props.put("RCM_PER",new Variant(0));
        props.put("RCM_AMT",new Variant(0));
        props.put("GST_COMPENSATION_CESS_PER",new Variant(0));
        props.put("GST_COMPENSATION_CESS_AMT",new Variant(0));
        
        props.put("PR_BILL_LENGTH",new Variant(""));
        props.put("PR_BILL_WIDTH",new Variant(""));
        props.put("PR_BILL_WEIGHT",new Variant(""));
        props.put("PR_BILL_SQMTR",new Variant(""));
        props.put("PR_BILL_GSM",new Variant(""));
        props.put("PR_BILL_PRODUCT_CODE",new Variant(""));
   }
}