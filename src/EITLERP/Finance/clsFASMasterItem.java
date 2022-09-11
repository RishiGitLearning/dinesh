/*
 * clsCalcInterestItem.java
 *
 * Created on August 18, 2008, 3:37 PM
 */

package EITLERP.Finance;

import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.util.*;
import EITLERP.*;

/**
 *
 * @author  MRUGESH THAKER
 * @version
 */
public class clsFASMasterItem {
    public String LastError="";
    private ResultSet rsInterestItem;
    private Connection Conn;
    private Statement Stmt;
    public boolean Ready = false;
    private HashMap props;
    
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
    
    /** Creates new clsInquiryItem */
    public clsFASMasterItem() {
        props = new HashMap();
        
        props.put("COMPANY_ID",new Variant(0));
        props.put("ASSET_NO",new Variant(""));
        props.put("SR_NO",new Variant(0));
        //props.put("ASSET_SR_NO",new Variant(0));
        props.put("YEAR",new Variant(""));
        props.put("DEPT_ID",new Variant(0));
        props.put("ITEM_ID",new Variant(""));
        props.put("AMOUNT",new Variant(0.0));  //DOUBLE
        props.put("ASSET_STATUS",new Variant(""));
        props.put("SALE_VALUE",new Variant(0.0));//double
        props.put("SALE_DATE",new Variant(""));
        props.put("SALE_DOC_NO",new Variant(""));  
        props.put("SJ_NUMBER",new Variant(""));  
        props.put("REMARKS",new Variant(""));  
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("CHANGED",new Variant(0));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("CANCELLED",new Variant(false));
        
        
    }
    
   
}