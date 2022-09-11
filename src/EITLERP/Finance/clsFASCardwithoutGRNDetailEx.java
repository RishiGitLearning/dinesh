/*
 * clsMRItem.java
 *
 * Created on April 19, 2004, 2:27 PM
 */

package EITLERP.Finance;


import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;

 
/**
 *
 * @author  jadave
 * @version 
 */
 
public class clsFASCardwithoutGRNDetailEx {

    private HashMap props;    
    public boolean Ready = false;

    public HashMap colVoucherDetailDocs=new HashMap();
    
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
    public clsFASCardwithoutGRNDetailEx() {
        props=new HashMap();    
        props.put("COMPANY_ID",new Variant(""));
        props.put("ASSET_NO",new Variant(""));
        props.put("ITEM_ID",new Variant(""));
        props.put("SR_NO",new Variant(""));
        props.put("DETAIL_SR_NO",new Variant(""));
        props.put("YEAR",new Variant(""));
        props.put("OPENING_BALANCE",new Variant(0));
        props.put("ADDITIONS",new Variant(""));
        props.put("CLOSING_BALANCE",new Variant(0));
        props.put("DEPRECIATION_FROM_DATE",new Variant(""));
        props.put("DEPRECIATION_TO_DATE",new Variant(""));
        props.put("DEPRECIATION_PERCENTAGE",new Variant(0));
        props.put("DEPRECIATION_METHOD",new Variant(""));
        props.put("DEPRECIATION_METHOD_NAME",new Variant(""));
        props.put("DEPRECIATION_FOR_THE_YEAR",new Variant(0));
        props.put("SHIFT_ALLOW_FOR_THE_YEAR",new Variant(""));
        props.put("CUMULATIVE_DEPRECIATION",new Variant(0));
        props.put("WRITTEN_DOWN_VALUE",new Variant(0));
        props.put("ASSET_TYPE",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("SHIFT",new Variant(""));
        props.put("TYPE",new Variant(""));
        
    }
}
