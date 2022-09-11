/*
 * clsPolicyTurnOverList.java
 *
 * Created on May 26, 2009, 12:15 PM
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
public class clsPolicyTurnOverList {
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
    
    /** Creates a new instance of clsPolicyTurnOverList */
    public clsPolicyTurnOverList() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("PARTY_CODE",new Variant(""));
        props.put("YEAR",new Variant(0));
        props.put("TURNOVER_AMOUNT",new Variant(0));
        
    }
    
    public static HashMap getList(long CompanyID,String PartyCode) {
        
        ResultSet rsTmp;
        HashMap List=new HashMap();
        long Counter=0;
        String str = "";
        double amt=0;
        
        try {
            String ParentParty = data.getStringValueFromDB("SELECT PARENT_PARTY_ID FROM D_SAL_POLICY_PARTY_GROUPING WHERE CHILD_PARTY_ID = '"+ PartyCode +"'");
            String ChildParty="";
            
            if (! ParentParty.trim().equals("")) {
                ResultSet rsParty = data.getResult("SELECT CHILD_PARTY_ID FROM D_SAL_POLICY_PARTY_GROUPING WHERE PARENT_PARTY_ID = '"+ParentParty+"' ");
                if (rsParty.getRow()>0) {
                    rsParty.first();
                    ChildParty = ParentParty;
                    while (! rsParty.isAfterLast()) {
                        ChildParty =  ChildParty + ","+ rsParty.getString("CHILD_PARTY_ID");
                        rsParty.next();
                    }                    
                    
                    str = "SELECT SUBSTR(INVOICE_DATE,1,4) AS YEAR,SUM(NET_AMOUNT) AS TURNOVER_AMOUNT " +
                    " FROM D_SAL_INVOICE_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID + "  AND PARTY_CODE IN ("+ ChildParty +") " +
                    " AND CANCELLED=0 AND SUBSTR(INVOICE_DATE,1,4)<>2009 "+
                    " GROUP BY SUBSTR(INVOICE_DATE,1,4) "+
                    " ORDER BY SUBSTR(INVOICE_DATE,1,4) DESC LIMIT 4";
                    rsTmp=data.getResult(str);
                    rsTmp.first();
                    Counter=0;
                    while(!rsTmp.isAfterLast()) {
                        Counter=Counter+1;
                        clsPolicyTurnOverList ObjList=new clsPolicyTurnOverList();
                        
                        //Populate the user
                        ObjList.setAttribute("PARTY_CODE",ParentParty);
                        ObjList.setAttribute("YEAR",rsTmp.getInt("YEAR"));
                        ObjList.setAttribute("TURNOVER_AMOUNT",rsTmp.getDouble("TURNOVER_AMOUNT"));
                        
                        //Put the prepared user object into list
                        List.put(Long.toString(Counter),ObjList);
                        rsTmp.next();
                    }//Out While
                }
            }
            else {
                str = "SELECT PARTY_CODE,SUBSTR(INVOICE_DATE,1,4) AS YEAR,SUM(NET_AMOUNT) AS TURNOVER_AMOUNT " +
                " FROM D_SAL_INVOICE_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID + "  AND PARTY_CODE='"+ PartyCode +"' " +
                " AND CANCELLED=0 AND SUBSTR(INVOICE_DATE,1,4)<>2009 "+
                " GROUP BY SUBSTR(INVOICE_DATE,1,4) "+
                " ORDER BY SUBSTR(INVOICE_DATE,1,4) DESC LIMIT 4";
                rsTmp=data.getResult(str);
                rsTmp.first();
                Counter=0;
                while(!rsTmp.isAfterLast()) {
                    Counter=Counter+1;
                    clsPolicyTurnOverList ObjList=new clsPolicyTurnOverList();
                    
                    //Populate the user
                    ObjList.setAttribute("PARTY_CODE",rsTmp.getString("PARTY_CODE"));
                    ObjList.setAttribute("YEAR",rsTmp.getInt("YEAR"));
                    ObjList.setAttribute("TURNOVER_AMOUNT",rsTmp.getDouble("TURNOVER_AMOUNT"));
                    
                    //Put the prepared user object into list
                    List.put(Long.toString(Counter),ObjList);
                    rsTmp.next();
                }//Out While
            }
        }
        catch(Exception e) {
            //JOptionPane.showMessageDialog(null,"Error occured"+e.getMessage());
        }
        
        return List;
    }
}
