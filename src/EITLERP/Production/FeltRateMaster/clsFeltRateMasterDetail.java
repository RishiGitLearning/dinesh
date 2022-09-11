/*
 * clsFeltRateMasterDetail.java
 *
 * Created on September 7, 2013, 11:40 AM
 */

package EITLERP.Production.FeltRateMaster;


import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Vivek Kumar
 */
 
public class clsFeltRateMasterDetail {
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
    public clsFeltRateMasterDetail() {
        props=new HashMap();    
        props.put("DOC_NO", new Variant(0));
        props.put("SR_NO", new Variant(0));
        props.put("PRODUCT_CODE",new Variant(""));
        props.put("PRODUCT_DESC",new Variant(""));
        props.put("SQM_RATE",new Variant(0));
        props.put("WT_RATE",new Variant(0));
        props.put("RATE_FROM_DATE",new Variant(""));
        props.put("RATE_TO_DATE",new Variant(""));
        props.put("CHARGES",new Variant(0));
        props.put("CHARGES_FROM_DATE",new Variant(""));
        props.put("CHARGES_TO_DATE",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("UPDATED_BY",new Variant(0));
        props.put("ENTRY_DATE",new Variant(""));
        props.put("CHANGED",new Variant(""));
        props.put("CHANGED_DATE",new Variant(""));        
    }

}
