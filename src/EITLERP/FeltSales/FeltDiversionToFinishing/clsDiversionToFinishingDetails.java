/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.FeltDiversionToFinishing;

import EITLERP.Variant;
import java.util.HashMap;

/**
 *
 * @author  DAXESH PRAJAPATI
 *
 */
public class clsDiversionToFinishingDetails {
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
    public clsDiversionToFinishingDetails() {
        props=new HashMap();
        
        props.put("P_D_NO",new Variant(""));
        props.put("SR_NO",new Variant(""));
        props.put("PARTY_CODE",new Variant(""));
        props.put("PARTY_NAME",new Variant(""));
        props.put("MACHINE_NO",new Variant(""));
        props.put("POSITION",new Variant(""));
        props.put("POSITION_DESC",new Variant(""));
        props.put("PIECE_NO",new Variant(""));
        props.put("PRODUCT",new Variant(""));
        props.put("PRODUCT_DESCRIPTION",new Variant(""));
        props.put("PRODUCT_GROUP",new Variant(""));
        props.put("LENGTH",new Variant(""));
        props.put("WIDTH",new Variant(""));
        props.put("GSM",new Variant(""));
        props.put("THEORTICAL_WEIGHT",new Variant(""));
        props.put("SQ_MT",new Variant(""));
        props.put("STYLE",new Variant(""));
        props.put("REQ_MONTH",new Variant(""));
        props.put("SYN_PER",new Variant(""));
        props.put("REMARK",new Variant(""));
        props.put("BILL_LENGTH",new Variant(""));
        props.put("BILL_WIDTH",new Variant(""));
        props.put("BILL_WEIGHT",new Variant(""));
        props.put("BILL_SQMTR",new Variant(""));
        props.put("BILL_GSM",new Variant(""));
        props.put("BILL_PRODUCT_CODE",new Variant(""));
        
        props.put("DIVISION_LENGTH_PER",new Variant(""));
        props.put("DIVISION_WIDTH_PER",new Variant(""));
        
        
        
   }
}