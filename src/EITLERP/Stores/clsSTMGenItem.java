/*
 * clsSTMGenItem.java
 *
 * Created on May 8, 2004, 4:03 PM
 */

package EITLERP.Stores;

 
import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
 
/**
 *
 * @author  jadave
 * @version
 */
 
public class clsSTMGenItem {
    
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
    
    
    /** Creates new clsSTMGenItem */
    public clsSTMGenItem() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID)); 
        props.put("STM_NO", new Variant(""));
        props.put("STM_TYPE",new Variant(0));
        props.put("SR_NO", new Variant(0));
        props.put("WAREHOUSE_ID",new Variant(""));
        props.put("LOCATION_ID",new Variant(""));
        props.put("ITEM_ID",new Variant(""));
        props.put("BOE_NO",new Variant(""));
        props.put("BOE_SR_NO",new Variant(""));
        props.put("BOE_DATE",new Variant(""));
        props.put("QTY",new Variant(0));
        props.put("UNIT",new Variant(0));
        props.put("RATE",new Variant(0));
        props.put("TOTAL_AMOUNT",new Variant(0));
        props.put("NET_AMOUNT",new Variant(0));
        props.put("STM_DESC",new Variant(""));
        props.put("QTY_REQD",new Variant(0));
        props.put("COST_CENTER_ID",new Variant(0));
        props.put("INDENT_NO",new Variant(""));
        props.put("INDENT_SR_NO",new Variant(0));
        props.put("INDENT_COMPANY_ID",new Variant(0));
        props.put("INDENT_COMPANY_YEAR",new Variant(0));
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
        props.put("RECEIVED_QTY",new Variant(0));
        props.put("ZERO_VAL_QTY",new Variant(0));
        props.put("MIR_NO",new Variant(""));
        props.put("MIR_SR_NO",new Variant(0));
        props.put("MIR_TYPE",new Variant(0));
        
    }
    
}
