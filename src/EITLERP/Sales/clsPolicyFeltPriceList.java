/*
 * clsPolicyFeltPriceList.java
 *
 * Created on April 7, 2009, 11:56 AM
 */

package EITLERP.Sales;

/**
 *
 * @author  root
 */

import java.util.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import EITLERP.Sales.*;

public class clsPolicyFeltPriceList {
    
    private HashMap props;    
    public boolean Ready = false;

    public HashMap colPolicyDetails=new HashMap();
    
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
    
    /** Creates a new instance of clsPolicyFeltPriceList */
    public clsPolicyFeltPriceList() {
        props=new HashMap();    
        props.put("COMPANY_ID",new Variant(0));
        props.put("SR_NO",new Variant(0));
        props.put("WH_CODE",new Variant(""));
        props.put("QUALITY_ID",new Variant(""));
        props.put("QUALITY_DESC",new Variant(""));
        props.put("SYN_PERC",new Variant(""));
        props.put("SQM_RATE",new Variant(0));
        props.put("WT_RATE",new Variant(0));
        props.put("SQM_RATE_O",new Variant(0));
        props.put("WT_RATE_O",new Variant(0));
        props.put("CHEM_TRTIN",new Variant(0));
        props.put("PIN_IND",new Variant(0));
        props.put("SPRL_IND",new Variant(0));
        props.put("SUR_CHGIND",new Variant(0));
        props.put("SQM_IND",new Variant(0));
        props.put("FILLER",new Variant(0));
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("CANCELLED",new Variant(0));
        props.put("CHANGED",new Variant(0));
        props.put("CHANGED_DATE",new Variant(""));  
    }
    
}
