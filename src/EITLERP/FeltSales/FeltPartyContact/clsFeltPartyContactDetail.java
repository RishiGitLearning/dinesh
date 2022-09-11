/*
 * clsFeltRateMasterDetail.java
 *
 * Created on September 7, 2013, 11:40 AM
 */

package EITLERP.FeltSales.FeltPartyContact;


import java.util.HashMap;
import EITLERP.Variant;
/**
 *
 * @author  Vivek Kumar
 */
 
public class clsFeltPartyContactDetail {
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
    public clsFeltPartyContactDetail() {
        props=new HashMap();    
        props.put("DOC_NO", new Variant(""));
        props.put("DOC_DATE", new Variant(""));
        props.put("PARTY_CODE", new Variant(""));
        props.put("PARTY_NAME", new Variant(""));
        props.put("CORR_ADDRESS", new Variant(""));
        props.put("PHONE_NO", new Variant(""));
        props.put("PIN_CODE", new Variant(""));
        props.put("ZONE", new Variant(""));
        props.put("INCHARGE_CD", new Variant(""));
        props.put("INCHARGE_NAME", new Variant(""));
        props.put("TRANSPORTER_ID", new Variant(""));
        props.put("TRANSPORTER_NAME", new Variant(""));
        props.put("DELIVERY_MODE", new Variant(""));
        props.put("CONTACT_PERSON", new Variant(""));
        props.put("CONT_PERS_DESIGNATION", new Variant(""));
        props.put("MOBILE_NO", new Variant(""));
        props.put("EMAIL", new Variant(""));
        props.put("CONTACT_PERSON_2", new Variant(""));
        props.put("CONT_PERS_DESIGNATION_2", new Variant(""));
        props.put("MOBILE_NO_2", new Variant(""));
        props.put("EMAIL_ID2", new Variant(""));
        props.put("CONTACT_PERSON_3", new Variant(""));
        props.put("CONT_PERS_DESIGNATION_3", new Variant(""));
        props.put("MOBILE_NO_3", new Variant(""));
        props.put("EMAIL_ID3", new Variant(""));
        
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
