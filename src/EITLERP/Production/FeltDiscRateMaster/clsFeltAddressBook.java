/*
 * clsDeptBuyers.java
 *
 * Created on July 13, 2005, 2:34 PM
 */

package EITLERP.Production.FeltDiscRateMaster;

/**
 *
 * @author  root
 */

import java.sql.*;
import java.net.*;
import java.util.*;
import EITLERP.*;


public class clsFeltAddressBook {
    
    
    private HashMap props;
    public boolean Ready = false;
    
    public String LastError="";
    
    public Variant getAttribute(String PropName) {
        return (Variant) props.get(PropName);
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
    
    /** Creates a new instance of clsItemMapping */
    public clsFeltAddressBook() {
        props=new HashMap();
        props.put("PARTY_CODE",new Variant(""));
        props.put("CONTACT_PERSON",new Variant(""));
        props.put("EMAIL",new Variant(""));
    }
    
    
    public static HashMap getList() {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        HashMap List=new HashMap();
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_PARTY_EXTRA_INFO");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsFeltAddressBook ObjItem=new clsFeltAddressBook();
                    ObjItem.setAttribute("PARTY_CODE",rsTmp.getString("PARTY_CODE"));
                    ObjItem.setAttribute("CONTACT_PERSON",rsTmp.getString("CONTACT_PERSON"));
                    ObjItem.setAttribute("EMAIL",rsTmp.getString("EMAIL"));
                    
                    List.put(Integer.toString(List.size()+1),ObjItem);
                    
                    rsTmp.next();
                }
            }
            rsTmp.close();
            stTmp.close();
        }
        catch(Exception e) {
            
        }
        return List;
    }
    

    
    public static HashMap getEMailList() {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        HashMap List=new HashMap();
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_PARTY_EXTRA_INFO ORDER BY EMAIL");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    List.put(Integer.toString(List.size()+1),rsTmp.getString("EMAIL"));
                    
                    rsTmp.next();
                }
            }
            rsTmp.close();
            stTmp.close();
        }
        catch(Exception e) {
            
        }
        return List;
    }
    
    
    
    
    public static boolean Delete(int pCompanyID,int pSrNo) {
        try {
            data.Execute("DELETE FROM PRODUCTION.FELT_PARTY_EXTRA_INFO WHERE PARTY_CODE="+pSrNo);
        }
        catch(Exception e) {
            
        }
        return true;
    }
    
    
    
    public static boolean Insert(Object pObjItem) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            clsFeltAddressBook ObjItem=(clsFeltAddressBook)pObjItem;
            
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_PARTY_EXTRA_INFO");
            rsTmp.first();
            
            rsTmp.moveToInsertRow();            
            rsTmp.updateString("PARTY_CODE",(String)ObjItem.getAttribute("PARTY_CODE").getObj());
            rsTmp.updateString("CONTACT_PERSON",(String)ObjItem.getAttribute("CONTACT_PERSON").getObj());
            rsTmp.updateString("EMAIL",(String)ObjItem.getAttribute("EMAIL").getObj());
            
            rsTmp.insertRow();
            
            rsTmp.close();
            stTmp.close();
            
            return true;
        }
        catch(Exception e) {
            return false;
        }
        
    }
    
}
