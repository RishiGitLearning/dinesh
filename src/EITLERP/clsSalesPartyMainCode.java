/*
 * clsSalesPartyMainCode.java
 *
 * Created on September 17, 2009, 11:50 AM
 */

package EITLERP;

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
public class clsSalesPartyMainCode {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
    public HashMap colTypeLot=new HashMap();
    
    private boolean HistoryView=false;
    
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
    public clsSalesPartyMainCode() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("SR_NO",new Variant(0));
        props.put("MAIN_ACCOUNT_CODE",new Variant(""));
        props.put("ACCOUNT_NAME",new Variant(""));
        props.put("CANCELLED", new Variant(false));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
    }
    
    public static HashMap getList(String pCondition) {
        
        ResultSet rsTmp;
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            
            rsTmp=data.getResult("SELECT * FROM D_SAL_PARTY_MAINCODE "+pCondition+" ORDER BY SR_NO ");
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsSalesPartyMainCode ObjMainCode=new clsSalesPartyMainCode();
                
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
