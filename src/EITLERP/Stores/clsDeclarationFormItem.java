/*
 * clsDeclarationFormItem.java
 *
 * Created on May 07, 2004, 3:38 PM
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
 
public class clsDeclarationFormItem {
    
    private HashMap props;
    public boolean Ready = false;
    public HashMap colItemLot=new HashMap();
    
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
    
    
    /** Creates new clsDeclarationFormItem */
    public clsDeclarationFormItem() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("DECLARATION_ID", new Variant(""));
        props.put("SR_NO", new Variant(0));
        props.put("ITEM_CODE", new Variant(""));
        props.put("DECLARATION_DESC", new Variant(""));
        props.put("UNIT", new Variant(0));
        props.put("RECD_QTY", new Variant(0.0));
        props.put("BAL_QTY", new Variant(0.0));
        props.put("RETURNED", new Variant(false));
        props.put("EXP_RETURN_DATE", new Variant(""));
        props.put("RETURNED_DATE", new Variant(""));
        props.put("REMARKS", new Variant(""));
        props.put("CANCELED", new Variant(false));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));         
    }
    
     
   public double getTotalLotQty()
   {
      double lnSum=0;
      
      for(int i=1;i<=colItemLot.size();i++)
      {
         clsDeclarationFormItemDetail ObjLot=(clsDeclarationFormItemDetail)colItemLot.get(Integer.toString(i));
         lnSum=lnSum+ObjLot.getAttribute("LOT_QTY").getVal();
      }
      
      return lnSum;
   }
    
}
