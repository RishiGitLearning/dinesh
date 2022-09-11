/*
 * clsInquiryItem.java
 *
 * Created on May 4, 2004, 9:26 AM
 */

package EITLERP.Purchase;

import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.util.*;
import EITLERP.*;
  
/** 
 *
 * @author  nhpatel
 * @version 
 */
public class clsInquiryItem {
    public String LastError="";
    private ResultSet rsInquiryItem;
    private Connection Conn;
    private Statement Stmt;
    public boolean Ready = false; 
    private HashMap props;
    
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
    
    /** Creates new clsInquiryItem */
    public clsInquiryItem() {
        props = new HashMap();
        
        props.put("COMPANY_ID",new Variant(0));
        props.put("INQUIRY_NO",new Variant(""));
        props.put("SR_NO",new Variant(0));
        props.put("ITEM_CODE",new Variant(""));
        props.put("ITEM_EXTRA_DESC",new Variant(""));
        props.put("MAKE",new Variant(""));
        props.put("UNIT",new Variant(0));
        props.put("QTY",new Variant(0));
        props.put("DELIVERY_DATE",new Variant(""));
        props.put("INDENT_NO",new Variant(""));
        props.put("INDENT_SRNO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("CANCELED",new Variant(false));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
    }

}
