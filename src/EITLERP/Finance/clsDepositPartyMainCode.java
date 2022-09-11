/*
 * clsSalesPartyMainCode.java
 *
 * Created on September 17, 2009, 11:50 AM
 */

package EITLERP.Finance;

import java.util.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import EITLERP.Finance.*;

/**
 *
 * @author  root
 */
public class clsDepositPartyMainCode {
    private HashMap props;
    public HashMap colTypeLot=new HashMap();
    public Variant getAttribute(String PropName) {
        if(!props.containsKey(PropName)) {
            return new Variant("");
        }
        else {
            return (Variant) props.get(PropName);
        }
    }
    
    public void setAttribute(String PropName,Object Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,int Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,long Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,double Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,float Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,boolean Value) {
        props.put(PropName,new Variant(Value));
    }
    
    /** Creates a new instance of clsSalesPartyMainCode */
    public clsDepositPartyMainCode() {
        props=new HashMap();
        props.put("MAIN_ACCOUNT_CODE",new Variant(""));
        props.put("ACCOUNT_NAME",new Variant(""));
        
    }
    
    public static HashMap getList(String pCondition) {
        
        ResultSet rsTmp;
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            
            rsTmp=data.getResult("SELECT * FROM D_FIN_GL "+pCondition+" ORDER BY MAIN_ACCOUNT_CODE ",FinanceGlobal.FinURL);
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsDepositPartyMainCode ObjMainCode=new clsDepositPartyMainCode();
                
                //Populate the user
                ObjMainCode.setAttribute("MAIN_ACCOUNT_CODE",rsTmp.getString("MAIN_ACCOUNT_CODE"));
                String MainCode = rsTmp.getString("MAIN_ACCOUNT_CODE").trim();
                ObjMainCode.setAttribute("ACCOUNT_NAME",clsAccount.getAccountName(MainCode,""));
                
                //Put the prepared user object into list
                List.put(Long.toString(Counter),ObjMainCode);
                rsTmp.next();
            }//Out While
        }
        catch(Exception e) {
            //JOptionPane.showMessageDialog(null,"Error occured"+e.getMessage());
        }
        
        return List;
    }
    
}
