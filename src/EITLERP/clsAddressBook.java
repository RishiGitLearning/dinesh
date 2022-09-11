/*
 * clsDeptBuyers.java
 *
 * Created on July 13, 2005, 2:34 PM
 */

package EITLERP;

/**
 *
 * @author  root
 */

import java.sql.*;
import java.net.*;
import java.util.*;


public class clsAddressBook {
    
    
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
    public clsAddressBook() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("SR_NO",new Variant(0));
        props.put("FULL_NAME",new Variant(""));
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
            rsTmp=stTmp.executeQuery("SELECT * FROM D_COM_ADDRESS_BOOK");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsAddressBook ObjItem=new clsAddressBook();
                    
                    ObjItem.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                    ObjItem.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                    ObjItem.setAttribute("FULL_NAME",rsTmp.getString("FULL_NAME"));
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
            rsTmp=stTmp.executeQuery("SELECT * FROM D_COM_ADDRESS_BOOK");
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
            data.Execute("DELETE FROM D_COM_ADDRESS_BOOK WHERE COMPANY_ID="+pCompanyID+" AND SR_NO="+pSrNo);
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
            clsAddressBook ObjItem=(clsAddressBook)pObjItem;
            
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_COM_ADDRESS_BOOK");
            rsTmp.first();
            
            long NextNo=data.getMaxID(EITLERPGLOBAL.gCompanyID, "D_COM_ADDRESS_BOOK", "SR_NO")+1;
            
            rsTmp.moveToInsertRow();
            rsTmp.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsTmp.updateLong("SR_NO",NextNo);
            rsTmp.updateString("FULL_NAME",(String)ObjItem.getAttribute("FULL_NAME").getObj());
            rsTmp.updateString("EMAIL",(String)ObjItem.getAttribute("EMAIL").getObj());
            rsTmp.updateBoolean("CHANGED",true);
            rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
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
