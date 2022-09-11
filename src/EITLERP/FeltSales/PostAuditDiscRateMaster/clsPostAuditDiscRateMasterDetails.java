/*
 * clsFeltAutoPISelectionDetails.java
 * This class is used for holding the data of Felt Production Finishing in HashMap
 * 
 * Created on August 22, 2013, 11:20 AM
 */

package EITLERP.FeltSales.PostAuditDiscRateMaster;

import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Vivek Kumar
 */
public class clsPostAuditDiscRateMasterDetails{
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
    
    
    /** Creates new clsFeltAutoPISelectionDetails */
    public clsPostAuditDiscRateMasterDetails() {
        props=new HashMap();
//        props.put("DOC_NO",new Variant(""));
//        props.put("DOC_DATE",new Variant(""));
        props.put("SELECTED_FLAG",new Variant(""));
        props.put("MASTER_NO",new Variant(""));
        props.put("SANCTION_DATE",new Variant(""));
        props.put("PARTY_CODE", new Variant(""));
        props.put("PARTY_NAME",new Variant(""));
        props.put("GROUP_CODE",new Variant(""));
        props.put("GROUP_NAME",new Variant(""));
        props.put("EFFECTIVE_FROM",new Variant(""));
        props.put("EFFECTIVE_TO",new Variant(""));
        props.put("AUDIT_REMARK",new Variant(""));
        props.put("FINAL_APPROVAL_DATE",new Variant(""));        
        
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("APPROVED",new Variant(0));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED",new Variant(0));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("CANCELED", new Variant(0));        
        props.put("HIERARCHY_ID",new Variant(0));        
    }
}