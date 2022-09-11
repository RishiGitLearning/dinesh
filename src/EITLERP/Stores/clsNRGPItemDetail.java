/*
 * clsNRGPItemDetail.java
 *
 * Created on April 23, 2004, 2:55 PM
 */

package EITLERP.Stores;
import java.sql.*;
import java.net.*;
import java.util.*;
import EITLERP.*;
 
/** 
 *
 * @author  nhpatel
 * @version 
 */ 
public class clsNRGPItemDetail {
    
    private HashMap props;
    private boolean Ready=false;

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
    
    /** Creates new clsNRGPItemDetail */
    public clsNRGPItemDetail() 
    {
       props = new HashMap();
       props.put("COMPANY_ID",new Variant(0));
       props.put("GATEPASS_NO",new Variant(""));
       props.put("SR_NO",new Variant(0));
       props.put("SRNO",new Variant(0));
       props.put("LOT_NO",new Variant(""));
       props.put("LOT_QTY",new Variant(0));
       props.put("CREATED_BY",new Variant(0));
       props.put("CREATED_DATE",new Variant(""));
       props.put("MODIFIED_BY",new Variant(0));
       props.put("MODIFIED_DATE",new Variant(""));
    }

}
