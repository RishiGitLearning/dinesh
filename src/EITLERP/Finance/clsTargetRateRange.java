/*
 * clsTargetRateRange.java
 *
 * Created on March 4, 2009, 3:53 PM
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
public class clsTargetRateRange {
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
    
    /** Creates a new instance of clsTargetRateRange */
    public clsTargetRateRange() {
        props=new HashMap();    
        props.put("COMPANY_ID",new Variant(0));
        props.put("SEASON_ID",new Variant(""));
        props.put("PARTY_ID",new Variant(""));
        props.put("EXMILLRATE_FROM_RANGE",new Variant(0));
        props.put("EXMILLRATE_TO_RANGE",new Variant(0));
        props.put("EXMILLRATE_PERCENTAGE",new Variant(0.0));  
    }
    
}
