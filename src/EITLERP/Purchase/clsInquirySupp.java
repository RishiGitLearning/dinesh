/*
 * clsInquirySupp.java
 *
 * Created on June 4, 2004, 9:10 AM
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
 */
public class clsInquirySupp {
    public String LastError="";
    private ResultSet rsInquirySupp;
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
    
    /** Creates a new instance of clsInquirySupp */
    public clsInquirySupp() {
       props = new HashMap();
        
        props.put("COMPANY_ID",new Variant(0));
        props.put("INQUIRY_NO",new Variant(""));
        props.put("SR_NO",new Variant(0));
        props.put("SUPP_ID",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
    }
    
}
