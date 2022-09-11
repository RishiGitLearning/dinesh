/*
 *
 * clsMRItem.java
 *
 * Created on April 19, 2004, 2:27 PM
 */

package EITLERP.Production.FeltUnadj;


import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;

/**
 *
 * @author  jadave
 * @version
 */

public class clsFeltUnadjitem {
    
    ;
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
    
    
    /** Creates new clsMRItem */
    public clsFeltUnadjitem() {
        props=new HashMap();
       
   
        props.put("UNADJ_ID",new Variant(""));
        props.put("UNADJ_DATE",new Variant(""));
        props.put("UNADJ_FROM_DATE",new Variant(""));
        props.put("UNADJ_TO_DATE",new Variant(""));
        
        props.put("HIERARCHY_ID",new Variant(""));
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("APPROVED",new Variant(""));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("REJECTED",new Variant(""));
        props.put("REJECTED_DATE",new Variant(""));
        props.put("CANCELLED",new Variant(""));
        props.put("CHANGED",new Variant(""));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("REJECTED_REMARKS",new Variant(""));

        props.put("PARTY_CODE",new Variant(""));
        props.put("PARTY_NAME",new Variant(""));
        props.put("INVOICE_NO",new Variant(""));
        props.put("INVOICE_DATE",new Variant(""));
        props.put("PRODUCT_CODE",new Variant(""));
        props.put("PIECE_NO",new Variant(""));
        props.put("KG",new Variant(""));
        props.put("SQR_MTR",new Variant(""));
        props.put("WIDTH",new Variant(""));
        props.put("LENGTH",new Variant(""));
        props.put("RATE",new Variant(""));
        props.put("INV_BASIC_AMT",new Variant(""));
        props.put("INV_DISC_PER",new Variant(""));
        props.put("SANC_DISC_PER",new Variant(""));
        props.put("WORK_DISC_PER",new Variant(""));
        props.put("INV_SEAM_CHARGES",new Variant(""));
        props.put("SANC_SEAM_CHARGES",new Variant(""));
        props.put("SEAM_PER",new Variant(""));
        props.put("DISC_AMT",new Variant(""));
        props.put("D_REMARK1",new Variant(""));
        props.put("D_REMARK2",new Variant(""));
        props.put("H_REMARK1",new Variant(""));
        props.put("H_REMARK2",new Variant(""));
        
        props.put("IGST_PER",new Variant(""));
        props.put("IGST_AMT",new Variant(""));
        props.put("CGST_PER",new Variant(""));
        props.put("CGST_AMT",new Variant(""));
        props.put("SGST_PER",new Variant(""));
        props.put("SGST_AMT",new Variant(""));
        props.put("TOTAL_DISC_AMT",new Variant(""));
        
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("CHANGED",new Variant(""));
         
    }
    
}
