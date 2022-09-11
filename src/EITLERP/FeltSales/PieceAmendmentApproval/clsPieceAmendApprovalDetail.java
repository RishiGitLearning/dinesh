/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.PieceAmendmentApproval;

import EITLERP.Variant;
import java.util.HashMap;

/**
 *
 * @author  DAXESH PRAJAPATI
 *
 */
public class clsPieceAmendApprovalDetail {
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
    public clsPieceAmendApprovalDetail() {
        props=new HashMap();
        
        props.put("SELECTED", new Variant(0));
        props.put("PIECE_AMEND_NO", new Variant(""));
        props.put("PIECE_AMEND_DATE", new Variant(""));
        props.put("PIECE_NO", new Variant(""));
        props.put("PRODUCT_CODE", new Variant(""));
        props.put("PRODUCT_DESC", new Variant(""));
        props.put("LENGTH", new Variant(""));
        props.put("LENGTH_UPDATED", new Variant(""));
        props.put("WIDTH", new Variant(""));
        props.put("WIDTH_UPDATED", new Variant(""));
        props.put("GSM", new Variant(""));
        props.put("GSM_UPDATED", new Variant(""));
        props.put("STYLE", new Variant(""));
        props.put("STYLE_UPDATED", new Variant(""));
        props.put("FLET_GROUP", new Variant(""));
        props.put("FLET_GROUP_UPDATED", new Variant(""));
        props.put("WEIGHT", new Variant(""));
        props.put("WEIGHT_UPDATED", new Variant(""));
        props.put("SQMTR", new Variant(""));
        props.put("SQMTR_UPDATED", new Variant(""));
        props.put("PIECE_STAGE", new Variant(""));
        props.put("UNTICK_USER", new Variant(""));
        props.put("UNTICK_REMARK_DESIGN", new Variant(""));
        props.put("UNTICK_REMARK_PRODUCTION", new Variant(""));
	props.put("CREATED_BY",new Variant(""));
	props.put("CREATED_DATE",new Variant(""));
	props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
   }
}