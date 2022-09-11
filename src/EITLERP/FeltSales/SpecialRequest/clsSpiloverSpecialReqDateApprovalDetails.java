/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.SpecialRequest;

import EITLERP.Variant;
import java.util.HashMap;

/**
 *
 * @author  DAXESH PRAJAPATI
 *
 */
public class clsSpiloverSpecialReqDateApprovalDetails {
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
    
   
    public clsSpiloverSpecialReqDateApprovalDetails() {
        props=new HashMap();
        
        props.put("OC_DOC_NO",new Variant(""));
        props.put("SR_NO",new Variant(""));
        props.put("PIECE_NO",new Variant(""));
        props.put("OC_NO",new Variant(""));
        props.put("OC_DATE",new Variant(""));
        props.put("OC_MONTH",new Variant(""));
        props.put("PIECE_STAGE",new Variant(""));
        props.put("REQ_MONTH",new Variant(""));
        props.put("REMARK",new Variant(""));
        
        
        
   }
}