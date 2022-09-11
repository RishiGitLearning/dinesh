package EITLERP;

/*
 * clsNoDataObject.java
 *
 * Created on April 6, 2004, 9:32 AM
 */


import java.util.*;
import java.sql.*;
import java.net.*;
 
/**
 *
 * @author  nrpatel
 * @version 
 */
 
public class clsUserRights {

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

    
    /** Creates new clsNoDataObject */
    public clsUserRights() {
        props=new HashMap();    
        props.put("COMPANY_ID",new Variant(0));
        props.put("USER_ID",new Variant(0));
        props.put("MENU_ID",new Variant(0));
        props.put("FUNCTION_ID",new Variant(0));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
    }

}
