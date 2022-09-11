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
public class clsPolicyChildPartyGrp {
    
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
    public clsPolicyChildPartyGrp() {
        props=new HashMap();    
        props.put("COMPANY_ID",new Variant(0));
        props.put("SR_NO",new Variant(0));
        props.put("PARENT_PARTY_ID",new Variant(""));
        props.put("CHILD_PARTY_ID",new Variant(""));
        props.put("CHILD_PARTY_NAME",new Variant(""));        
    }
    
}
