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
 
public class clsGRNGenItem {
    
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
    public clsGRNGenItem() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("GRN_NO",new Variant(""));
        props.put("SR_NO",new Variant(0));
        props.put("GRN_TYPE",new Variant(1)); //1 - General Material
        props.put("WAREHOUSE_ID",new Variant(""));
        props.put("LOCATION_ID",new Variant(""));
        props.put("MIR_NO",new Variant(""));
        props.put("MIR_SR_NO",new Variant(0));
        props.put("MIR_TYPE",new Variant(0));
        props.put("PO_NO",new Variant(""));
        props.put("PO_SR_NO",new Variant(0));
        props.put("PO_TYPE",new Variant(0));
        props.put("ITEM_ID",new Variant(""));
        props.put("HSN_SAC_CODE",new Variant(""));
        props.put("BOE_NO",new Variant(""));
        props.put("BOE_SR_NO",new Variant(""));
        props.put("BOE_DATE",new Variant(""));
        props.put("MIR_QTY",new Variant(0));
        props.put("QTY",new Variant(0));
        props.put("REJECTED_QTY",new Variant(0));
        props.put("REJECTED_REASON",new Variant(""));
        props.put("EXCESS_QTY",new Variant(0));
        props.put("UNIT",new Variant(0));
        props.put("RATE",new Variant(0));
        props.put("LANDED_RATE",new Variant(0));
        props.put("TOTAL_AMOUNT",new Variant(0));
        props.put("SHADE",new Variant(""));
        props.put("W_MIE",new Variant(""));
        props.put("NO_CASE",new Variant(""));
        props.put("EXCISE_GATEPASS_GIVEN",new Variant(false));
        props.put("IMPORT_CONCESS",new Variant(false));
        props.put("REMARKS",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("COLUMN_1_ID",new Variant(0));
        props.put("COLUMN_1_FORMULA",new Variant(""));
        props.put("COLUMN_1_PER",new Variant(0));
        props.put("COLUMN_1_AMT",new Variant(0));
        props.put("COLUMN_1_CAPTION",new Variant(""));
        props.put("COLUMN_2_ID",new Variant(0));
        props.put("COLUMN_2_FORMULA",new Variant(""));
        props.put("COLUMN_2_PER",new Variant(0));
        props.put("COLUMN_2_AMT",new Variant(0));
        props.put("COLUMN_2_CAPTION",new Variant(""));
        props.put("COLUMN_3_ID",new Variant(0));
        props.put("COLUMN_3_FORMULA",new Variant(""));
        props.put("COLUMN_3_PER",new Variant(0));
        props.put("COLUMN_3_AMT",new Variant(0));
        props.put("COLUMN_3_CAPTION",new Variant(""));
        props.put("COLUMN_4_ID",new Variant(0));
        props.put("COLUMN_4_FORMULA",new Variant(""));
        props.put("COLUMN_4_PER",new Variant(0));
        props.put("COLUMN_4_AMT",new Variant(0));
        props.put("COLUMN_4_CAPTION",new Variant(""));
        props.put("COLUMN_5_ID",new Variant(0));
        props.put("COLUMN_5_FORMULA",new Variant(""));
        props.put("COLUMN_5_PER",new Variant(0));
        props.put("COLUMN_5_AMT",new Variant(0));
        props.put("COLUMN_5_CAPTION",new Variant(""));
        props.put("COLUMN_6_ID",new Variant(0));
        props.put("COLUMN_6_FORMULA",new Variant(""));
        props.put("COLUMN_6_PER",new Variant(0));
        props.put("COLUMN_6_AMT",new Variant(0));
        props.put("COLUMN_6_CAPTION",new Variant(""));
        props.put("COLUMN_7_ID",new Variant(0));
        props.put("COLUMN_7_FORMULA",new Variant(""));
        props.put("COLUMN_7_PER",new Variant(0));
        props.put("COLUMN_7_AMT",new Variant(0));
        props.put("COLUMN_7_CAPTION",new Variant(""));
        props.put("COLUMN_8_ID",new Variant(0));
        props.put("COLUMN_8_FORMULA",new Variant(""));
        props.put("COLUMN_8_PER",new Variant(0));
        props.put("COLUMN_8_AMT",new Variant(0));
        props.put("COLUMN_8_CAPTION",new Variant(""));
        props.put("COLUMN_9_ID",new Variant(0));
        props.put("COLUMN_9_FORMULA",new Variant(""));
        props.put("COLUMN_9_PER",new Variant(0));
        props.put("COLUMN_9_AMT",new Variant(0));
        props.put("COLUMN_9_CAPTION",new Variant(""));
        props.put("COLUMN_10_ID",new Variant(0));
        props.put("COLUMN_10_FORMULA",new Variant(""));
        props.put("COLUMN_10_PER",new Variant(0));
        props.put("COLUMN_10_AMT",new Variant(0));
        props.put("COLUMN_10_CAPTION",new Variant(""));
        props.put("MATERIAL_CODE",new Variant(""));
        props.put("MATERIAL_DESC",new Variant(""));
        props.put("QUALITY_NO",new Variant(""));
        props.put("PAGE_NO",new Variant(""));
        props.put("EXCESS",new Variant(0));
        props.put("SHORTAGE",new Variant(0));
        props.put("L_F_NO",new Variant(""));
        props.put("CHALAN_QTY",new Variant(0));
        props.put("TOLERANCE_LIMIT",new Variant(0));
        props.put("ISSUED_QTY",new Variant(0));
        props.put("BARCODE_TYPE",new Variant(""));
        props.put("PO_QTY",new Variant(0));
        props.put("RECEIVED_QTY",new Variant(0));
        props.put("BALANCE_PO_QTY",new Variant(0));
        props.put("REJECTED_REASON_ID",new Variant(0));
        props.put("DEPT_ID",new Variant(0));
        props.put("ITEM_EXTRA_DESC",new Variant(""));
        props.put("COLUMN_11_ID",new Variant(0));
        props.put("COLUMN_11_FORMULA",new Variant(""));
        props.put("COLUMN_11_PER",new Variant(0));
        props.put("COLUMN_11_AMT",new Variant(0));
        props.put("COLUMN_11_CAPTION",new Variant(""));
        props.put("COLUMN_12_ID",new Variant(0));
        props.put("COLUMN_12_FORMULA",new Variant(""));
        props.put("COLUMN_12_PER",new Variant(0));
        props.put("COLUMN_12_AMT",new Variant(0));
        props.put("COLUMN_12_CAPTION",new Variant(""));
        props.put("COLUMN_13_ID",new Variant(0));
        props.put("COLUMN_13_FORMULA",new Variant(""));
        props.put("COLUMN_13_PER",new Variant(0));
        props.put("COLUMN_13_AMT",new Variant(0));
        props.put("COLUMN_13_CAPTION",new Variant(""));
        props.put("COLUMN_14_ID",new Variant(0));
        props.put("COLUMN_14_FORMULA",new Variant(""));
        props.put("COLUMN_14_PER",new Variant(0));
        props.put("COLUMN_14_AMT",new Variant(0));
        props.put("COLUMN_14_CAPTION",new Variant(""));
        props.put("COLUMN_15_ID",new Variant(0));
        props.put("COLUMN_15_FORMULA",new Variant(""));
        props.put("COLUMN_15_PER",new Variant(0));
        props.put("COLUMN_15_AMT",new Variant(0));
        props.put("COLUMN_15_CAPTION",new Variant(""));
        props.put("COLUMN_16_ID",new Variant(0));
        props.put("COLUMN_16_FORMULA",new Variant(""));
        props.put("COLUMN_16_PER",new Variant(0));
        props.put("COLUMN_16_AMT",new Variant(0));
        props.put("COLUMN_16_CAPTION",new Variant(""));
        props.put("COLUMN_17_ID",new Variant(0));
        props.put("COLUMN_17_FORMULA",new Variant(""));
        props.put("COLUMN_17_PER",new Variant(0));
        props.put("COLUMN_17_AMT",new Variant(0));
        props.put("COLUMN_17_CAPTION",new Variant(""));
        props.put("COLUMN_18_ID",new Variant(0));
        props.put("COLUMN_18_FORMULA",new Variant(""));
        props.put("COLUMN_18_PER",new Variant(0));
        props.put("COLUMN_18_AMT",new Variant(0));
        props.put("COLUMN_18_CAPTION",new Variant(""));
        props.put("COLUMN_19_ID",new Variant(0));
        props.put("COLUMN_19_FORMULA",new Variant(""));
        props.put("COLUMN_19_PER",new Variant(0));
        props.put("COLUMN_19_AMT",new Variant(0));
        props.put("COLUMN_19_CAPTION",new Variant(""));
        props.put("COLUMN_20_ID",new Variant(0));
        props.put("COLUMN_20_FORMULA",new Variant(""));
        props.put("COLUMN_20_PER",new Variant(0));
        props.put("COLUMN_20_AMT",new Variant(0));
        props.put("COLUMN_20_CAPTION",new Variant(""));
        
        props.put("COLUMN_21_ID",new Variant(0));
        props.put("COLUMN_21_FORMULA",new Variant(""));
        props.put("COLUMN_21_PER",new Variant(0));
        props.put("COLUMN_21_AMT",new Variant(0));
        props.put("COLUMN_21_CAPTION",new Variant(""));
        props.put("COLUMN_22_ID",new Variant(0));
        props.put("COLUMN_22_FORMULA",new Variant(""));
        props.put("COLUMN_22_PER",new Variant(0));
        props.put("COLUMN_22_AMT",new Variant(0));
        props.put("COLUMN_22_CAPTION",new Variant(""));
        props.put("COLUMN_23_ID",new Variant(0));
        props.put("COLUMN_23_FORMULA",new Variant(""));
        props.put("COLUMN_23_PER",new Variant(0));
        props.put("COLUMN_23_AMT",new Variant(0));
        props.put("COLUMN_23_CAPTION",new Variant(""));
        props.put("COLUMN_24_ID",new Variant(0));
        props.put("COLUMN_24_FORMULA",new Variant(""));
        props.put("COLUMN_24_PER",new Variant(0));
        props.put("COLUMN_24_AMT",new Variant(0));
        props.put("COLUMN_24_CAPTION",new Variant(""));
        props.put("COLUMN_25_ID",new Variant(0));
        props.put("COLUMN_25_FORMULA",new Variant(""));
        props.put("COLUMN_25_PER",new Variant(0));
        props.put("COLUMN_25_AMT",new Variant(0));
        props.put("COLUMN_25_CAPTION",new Variant(""));
        props.put("COLUMN_26_ID",new Variant(0));
        props.put("COLUMN_26_FORMULA",new Variant(""));
        props.put("COLUMN_26_PER",new Variant(0));
        props.put("COLUMN_26_AMT",new Variant(0));
        props.put("COLUMN_26_CAPTION",new Variant(""));
        props.put("COLUMN_27_ID",new Variant(0));
        props.put("COLUMN_27_FORMULA",new Variant(""));
        props.put("COLUMN_27_PER",new Variant(0));
        props.put("COLUMN_27_AMT",new Variant(0));
        props.put("COLUMN_27_CAPTION",new Variant(""));
        props.put("COLUMN_28_ID",new Variant(0));
        props.put("COLUMN_28_FORMULA",new Variant(""));
        props.put("COLUMN_28_PER",new Variant(0));
        props.put("COLUMN_28_AMT",new Variant(0));
        props.put("COLUMN_28_CAPTION",new Variant(""));
        props.put("COLUMN_29_ID",new Variant(0));
        props.put("COLUMN_29_FORMULA",new Variant(""));
        props.put("COLUMN_29_PER",new Variant(0));
        props.put("COLUMN_29_AMT",new Variant(0));
        props.put("COLUMN_29_CAPTION",new Variant(""));
        props.put("COLUMN_30_ID",new Variant(0));
        props.put("COLUMN_30_FORMULA",new Variant(""));
        props.put("COLUMN_30_PER",new Variant(0));
        props.put("COLUMN_30_AMT",new Variant(0));
        props.put("COLUMN_30_CAPTION",new Variant(""));
        
        props.put("COLUMN_31_ID",new Variant(0));
        props.put("COLUMN_31_FORMULA",new Variant(""));
        props.put("COLUMN_31_PER",new Variant(0));
        props.put("COLUMN_31_AMT",new Variant(0));
        props.put("COLUMN_31_CAPTION",new Variant(""));
        props.put("COLUMN_32_ID",new Variant(0));
        props.put("COLUMN_32_FORMULA",new Variant(""));
        props.put("COLUMN_32_PER",new Variant(0));
        props.put("COLUMN_32_AMT",new Variant(0));
        props.put("COLUMN_32_CAPTION",new Variant(""));
        props.put("COLUMN_33_ID",new Variant(0));
        props.put("COLUMN_33_FORMULA",new Variant(""));
        props.put("COLUMN_33_PER",new Variant(0));
        props.put("COLUMN_33_AMT",new Variant(0));
        props.put("COLUMN_33_CAPTION",new Variant(""));
        props.put("COLUMN_34_ID",new Variant(0));
        props.put("COLUMN_34_FORMULA",new Variant(""));
        props.put("COLUMN_34_PER",new Variant(0));
        props.put("COLUMN_34_AMT",new Variant(0));
        props.put("COLUMN_34_CAPTION",new Variant(""));
        props.put("COLUMN_35_ID",new Variant(0));
        props.put("COLUMN_35_FORMULA",new Variant(""));
        props.put("COLUMN_35_PER",new Variant(0));
        props.put("COLUMN_35_AMT",new Variant(0));
        props.put("COLUMN_35_CAPTION",new Variant(""));
        props.put("COLUMN_36_ID",new Variant(0));
        props.put("COLUMN_36_FORMULA",new Variant(""));
        props.put("COLUMN_36_PER",new Variant(0));
        props.put("COLUMN_36_AMT",new Variant(0));
        props.put("COLUMN_36_CAPTION",new Variant(""));
        props.put("COLUMN_37_ID",new Variant(0));
        props.put("COLUMN_37_FORMULA",new Variant(""));
        props.put("COLUMN_37_PER",new Variant(0));
        props.put("COLUMN_37_AMT",new Variant(0));
        props.put("COLUMN_37_CAPTION",new Variant(""));
        props.put("COLUMN_38_ID",new Variant(0));
        props.put("COLUMN_38_FORMULA",new Variant(""));
        props.put("COLUMN_38_PER",new Variant(0));
        props.put("COLUMN_38_AMT",new Variant(0));
        props.put("COLUMN_38_CAPTION",new Variant(""));
        props.put("COLUMN_39_ID",new Variant(0));
        props.put("COLUMN_39_FORMULA",new Variant(""));
        props.put("COLUMN_39_PER",new Variant(0));
        props.put("COLUMN_39_AMT",new Variant(0));
        props.put("COLUMN_39_CAPTION",new Variant(""));
        props.put("RND_DEDUCTION_REASON",new Variant(""));
        
    }
    
}
