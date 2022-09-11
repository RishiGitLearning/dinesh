/*
 * clsSchedwiseTrialBalance.java
 *
 * Created on June 12, 2009, 12:41 PM
 */

package EITLERP.Finance.ReportsUI;

import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import EITLERP.Finance.ReportsUI.*;
/**
 *
 * @author  root
 */
public class clsSchedwiseTrialBalance {
    private HashMap props;    
    public boolean Ready = false;

    public HashMap colSchemePeriodDocs=new HashMap();
    
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

    
    /** Creates a new instance of clsSchedwiseTrialBalance */
    public clsSchedwiseTrialBalance() {
        props = new HashMap();
        props.put("MAIN_ACCOUNT_CODE", new Variant(""));
        props.put("SUB_ACCOUNT_CODE", new Variant(""));
        props.put("ACCOUNT_NAME",new Variant(""));
        props.put("DEBIT",new Variant(0.0));
        props.put("CREDIT",new Variant(0.0));
        props.put("LASTYEAR_DEBIT",new Variant(0.0));
        props.put("LASTYEAR_CREDIT",new Variant(0.0));
    }
    
}
