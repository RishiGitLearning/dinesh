/*
 * clsFeltRateMasterDetail.java
 *
 * Created on September 7, 2013, 11:40 AM
 */

package EITLERP.FeltSales.FeltQualityRateMaster;


import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Vivek Kumar
 */
 
public class clsFeltQltRateMasterDetail {
    private HashMap props;    
    
    public Variant getAttribute(String PropName){
        return (Variant) props.get(PropName);
    }
    
    public void setAttribute(String PropName,Object Value){
         props.put(PropName,new Variant(Value));
    }

    public void setAttribute(String PropName,int Value){
         props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,long Value){
         props.put(PropName,new Variant(Value));
    }

    public void setAttribute(String PropName,double Value){
         props.put(PropName,new Variant(Value));
    }

    public void setAttribute(String PropName,float Value){
         props.put(PropName,new Variant(Value));
    }

    public void setAttribute(String PropName,boolean Value){
         props.put(PropName,new Variant(Value));
    }
    
    /** Creates new clsFeltRateMasterDetail */
    public clsFeltQltRateMasterDetail() {
        props=new HashMap();    
        props.put("DOC_NO", new Variant(0));
        props.put("DOC_DATE",new Variant(""));
        props.put("WH_CD",new Variant(""));
        props.put("PRODUCT_CODE",new Variant(""));
        props.put("PRODUCT_DESC",new Variant(""));
        props.put("SYN_PER",new Variant(0));
        props.put("SQM_RATE",new Variant(0));
        props.put("WT_RATE",new Variant(0));
        props.put("EFFECTIVE_FROM",new Variant(""));
        props.put("EFFECTIVE_TO",new Variant(""));
        props.put("CHEM_TRT_IND",new Variant(""));
        props.put("CHEM_TRT_CHRG",new Variant(0));
        props.put("PIN_IND",new Variant(""));
        props.put("PIN_CHRG",new Variant(0));
        props.put("SPR_IND",new Variant(""));
        props.put("SPR_CHRG",new Variant(0));
        props.put("SUR_IND",new Variant(""));
        props.put("SUR_CHRG",new Variant(0));
        props.put("SQM_IND",new Variant(""));
        props.put("SQM_CHRG",new Variant(0));
        props.put("EXC_CAT_IND",new Variant(""));
        props.put("EXC_CAT_CHRG",new Variant(0));
        props.put("FILLE",new Variant(""));
        props.put("CHARGES",new Variant(0));
        props.put("GROUP_NAME",new Variant(""));
        props.put("DIVERSION_GROUP",new Variant(""));
        props.put("CATEGORY",new Variant(""));
        props.put("FABRIC_CATG",new Variant(""));
        props.put("POSITION_CATG",new Variant(""));
        props.put("FELT_CATG",new Variant(""));
        props.put("ACTIVE_FLAG",new Variant(0));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("APPROVED",new Variant(0));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("REJECTED",new Variant(0));
        props.put("REJECTED_DATE",new Variant(""));
        props.put("REJECTED_REMARKS",new Variant(""));
        props.put("CANCELED",new Variant(""));
        props.put("HIERARCHY_ID",new Variant(""));
        props.put("CHANGED",new Variant(""));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("UPDATED_BY",new Variant(0));
        props.put("APPROVAL_STATUS",new Variant(""));
        props.put("ENTRY_DATE",new Variant(""));
        props.put("APPROVER_REMARKS",new Variant(""));
              
    }

}
