/*
 * clsDeclarationFormItemDetail.java
 *
 * Created on June 1, 2004, 11:33 AM
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
 
public class clsDeclarationFormItemDetail {
    
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
    
    
    /** Creates new clsDeclarationFormItemDetail */
    public clsDeclarationFormItemDetail() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("DECLARATION_ID", new Variant(""));
        props.put("SR_NO", new Variant(0));
        props.put("SRNO", new Variant(0));
        props.put("ITEM_ID",new Variant(""));
        props.put("LOT_NO",new Variant(""));
        props.put("LOT_QTY",new Variant(0.0));
        props.put("TOTAL_ISSUED_QTY",new Variant(0.0));
        props.put("BAL_QTY",new Variant(0.0));
        props.put("CANCELED",new Variant(false));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
    }
    
}
