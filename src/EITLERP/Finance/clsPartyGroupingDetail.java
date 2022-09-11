/*
 * clsPolicyChildPartyGrp.java
 *
 * Created on March 6, 2009, 5:33 PM
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
public class clsPartyGroupingDetail {
    
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
    
    /** Creates a new instance of clsPolicyChildPartyGrp */
    public clsPartyGroupingDetail() {
        props=new HashMap();    
        props.put("COMPANY_ID",new Variant(0));
        props.put("SR_NO",new Variant(0));
        props.put("INVOICE_TYPE",new Variant(0));
        props.put("GROUP_MAIN_PARTY",new Variant(""));
        props.put("GROUP_SUB_PARTY",new Variant(""));
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
    }
    
}
