/*
 * clsPolicyPartyFlatPer.java
 *
 * Created on March 29, 2009, 3:32 PM
 */

package EITLERP.Finance;

import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;

/**
 *
 * @author  root
 */
public class clsPolicyPartyFlatPer {
    
    private HashMap props;    
    public boolean Ready = false;

    public HashMap colPolicyParties=new HashMap();
    
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

    /** Creates a new instance of clsPolicyPartyFlatPer */
    public clsPolicyPartyFlatPer() {
        props=new HashMap();    
        props.put("COMPANY_ID",new Variant(0));
        props.put("POLICY_ID",new Variant(""));
        props.put("SR_NO",new Variant(0));        
        props.put("PARTY_ID",new Variant(""));
        props.put("FROM_DATE",new Variant(""));
        props.put("TO_DATE",new Variant(""));
        props.put("PRODUCT_CODE",new Variant(""));
        props.put("FLAT_PERCENTAGE",new Variant(0));
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("CANCELLED",new Variant(0));
        props.put("CHANGED",new Variant(0));
        props.put("CHANGED_DATE",new Variant(""));   
    }
    
}
