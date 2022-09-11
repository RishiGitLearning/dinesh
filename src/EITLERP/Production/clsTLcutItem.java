/*
 *
 * clsMRItem.java
 *
 * Created on April 19, 2004, 2:27 PM
 */

package EITLERP.Production;


import EITLERP.Production.ReportUI.*;
import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
 
/**
 *
 * @author  jadave
 * @version 
 */
 
public class clsTLcutItem {

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

    
    /** Creates new clsMRItem */
    public clsTLcutItem() {
        props=new HashMap();
        props.put("DOC_NO", new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("FILE_NAME",new Variant(""));
        props.put("QUALITY_ID",new Variant(""));
        props.put("SHADE",new Variant(""));        
        props.put("PIECE_NO", new Variant(""));
        props.put("RCVD_DATE",new Variant(""));
        props.put("FLAG_CD",new Variant(""));
        props.put("RACK_NO",new Variant(""));
        props.put("NET_MTR",new Variant(0));
        props.put("BRAND",new Variant(""));
        props.put("SELECTED_PIECE",new Variant(false));
        
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        
        
        
 
    }

}
