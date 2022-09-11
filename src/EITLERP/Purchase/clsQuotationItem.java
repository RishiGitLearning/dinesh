/*
 * clsQuotationItem.java
 *
 * Created on May 12, 2004, 11:57 AM
 */

package EITLERP.Purchase;

 
import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
  
/**
 *
 * @author  jadave
 * @version
 */

public class clsQuotationItem {
    
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
    
    
    /** Creates new clsQuotationItem */
    public clsQuotationItem() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("QUOT_ID", new Variant(""));
        props.put("SR_NO", new Variant(0));
        props.put("INQUIRY_NO", new Variant(""));
        props.put("INQUIRY_SRNO", new Variant(0));
        props.put("ITEM_CODE", new Variant(""));
        props.put("HSN_SAC_CODE", new Variant(""));
        props.put("MAKE", new Variant(""));
        props.put("UNIT", new Variant(0)); 
        props.put("QTY", new Variant(0.0)); 
        props.put("RATE", new Variant(0.0)); 
        props.put("TOTAL_AMOUNT", new Variant(0.0)); 
        props.put("ACCESS_AMOUNT", new Variant(0.0)); 
        props.put("DELIVERY_DATE",new Variant(""));
        props.put("REMARKS", new Variant(""));        
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant("")); 
        props.put("COLUMN_1_ID", new Variant(0)); 
        props.put("COLUMN_1_FORMULA", new Variant(""));
        props.put("COLUMN_1_PER", new Variant(0.0)); 
        props.put("COLUMN_1_AMT", new Variant(0.0)); 
        props.put("COLUMN_1_CAPTION", new Variant(""));
        props.put("COLUMN_2_ID", new Variant(0)); 
        props.put("COLUMN_2_FORMULA", new Variant(""));
        props.put("COLUMN_2_PER", new Variant(0.0)); 
        props.put("COLUMN_2_AMT", new Variant(0.0)); 
        props.put("COLUMN_2_CAPTION", new Variant(""));
        props.put("COLUMN_3_ID", new Variant(0)); 
        props.put("COLUMN_3_FORMULA", new Variant(""));
        props.put("COLUMN_3_PER", new Variant(0.0)); 
        props.put("COLUMN_3_AMT", new Variant(0.0)); 
        props.put("COLUMN_3_CAPTION", new Variant(""));
        props.put("COLUMN_4_ID", new Variant(0)); 
        props.put("COLUMN_4_FORMULA", new Variant(""));
        props.put("COLUMN_4_PER", new Variant(0.0)); 
        props.put("COLUMN_4_AMT", new Variant(0.0)); 
        props.put("COLUMN_4_CAPTION", new Variant(""));
        props.put("COLUMN_5_ID", new Variant(0)); 
        props.put("COLUMN_5_FORMULA", new Variant(""));
        props.put("COLUMN_5_PER", new Variant(0.0)); 
        props.put("COLUMN_5_AMT", new Variant(0.0));
        props.put("COLUMN_5_CAPTION", new Variant(""));
        props.put("COLUMN_6_ID", new Variant(0)); 
        props.put("COLUMN_6_FORMULA", new Variant(""));
        props.put("COLUMN_6_PER", new Variant(0.0)); 
        props.put("COLUMN_6_AMT", new Variant(0.0)); 
        props.put("COLUMN_6_CAPTION", new Variant(""));
        props.put("COLUMN_7_ID", new Variant(0)); 
        props.put("COLUMN_7_FORMULA", new Variant(""));
        props.put("COLUMN_7_PER", new Variant(0.0)); 
        props.put("COLUMN_7_AMT", new Variant(0.0)); 
        props.put("COLUMN_7_CAPTION", new Variant(""));
        props.put("COLUMN_8_ID", new Variant(0)); 
        props.put("COLUMN_8_FORMULA", new Variant(""));
        props.put("COLUMN_8_PER", new Variant(0.0)); 
        props.put("COLUMN_8_AMT", new Variant(0.0)); 
        props.put("COLUMN_8_CAPTION", new Variant(""));
        props.put("COLUMN_9_ID", new Variant(0)); 
        props.put("COLUMN_9_FORMULA", new Variant(""));
        props.put("COLUMN_9_PER", new Variant(0.0)); 
        props.put("COLUMN_9_AMT", new Variant(0.0)); 
        props.put("COLUMN_9_CAPTION", new Variant(""));
        props.put("COLUMN_10_ID", new Variant(0)); 
        props.put("COLUMN_10_FORMULA", new Variant(""));
        props.put("COLUMN_10_PER", new Variant(0.0)); 
        props.put("COLUMN_10_AMT", new Variant(0.0));    
        props.put("COLUMN_10_CAPTION", new Variant(""));
        props.put("COLUMN_11_ID", new Variant(0)); 
        props.put("COLUMN_11_FORMULA", new Variant(""));
        props.put("COLUMN_11_PER", new Variant(0.0)); 
        props.put("COLUMN_11_AMT", new Variant(0.0)); 
        props.put("COLUMN_11_CAPTION", new Variant(""));
        props.put("COLUMN_12_ID", new Variant(0)); 
        props.put("COLUMN_12_FORMULA", new Variant(""));
        props.put("COLUMN_12_PER", new Variant(0.0)); 
        props.put("COLUMN_12_AMT", new Variant(0.0)); 
        props.put("COLUMN_12_CAPTION", new Variant(""));
        props.put("COLUMN_13_ID", new Variant(0)); 
        props.put("COLUMN_13_FORMULA", new Variant(""));
        props.put("COLUMN_13_PER", new Variant(0.0)); 
        props.put("COLUMN_13_AMT", new Variant(0.0)); 
        props.put("COLUMN_13_CAPTION", new Variant(""));
        props.put("COLUMN_14_ID", new Variant(0)); 
        props.put("COLUMN_14_FORMULA", new Variant(""));
        props.put("COLUMN_14_PER", new Variant(0.0)); 
        props.put("COLUMN_14_AMT", new Variant(0.0)); 
        props.put("COLUMN_14_CAPTION", new Variant(""));
        props.put("COLUMN_15_ID", new Variant(0)); 
        props.put("COLUMN_15_FORMULA", new Variant(""));
        props.put("COLUMN_15_PER", new Variant(0.0)); 
        props.put("COLUMN_15_AMT", new Variant(0.0));
        props.put("COLUMN_15_CAPTION", new Variant(""));
        props.put("COLUMN_16_ID", new Variant(0)); 
        props.put("COLUMN_16_FORMULA", new Variant(""));
        props.put("COLUMN_16_PER", new Variant(0.0)); 
        props.put("COLUMN_16_AMT", new Variant(0.0)); 
        props.put("COLUMN_16_CAPTION", new Variant(""));
        
        props.put("SUPP_ITEM_DESC", new Variant(""));
        props.put("EXCISE_GATEPASS_GIVEN", new Variant(false));
        props.put("PRICE_LIST_NO", new Variant(""));
    }
    
}
