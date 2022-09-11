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


public class clsDeptBuyers {
    
    
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
    public clsDeptBuyers() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("SR_NO",new Variant(0));
        props.put("BUYER",new Variant(0));
        props.put("DEPT_ID",new Variant(0));
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        
    }
    
    
    public static HashMap getList() {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        HashMap List=new HashMap();
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT * FROM D_COM_DEPT_BUYERS");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsDeptBuyers ObjItem=new clsDeptBuyers();
                    
                    ObjItem.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                    ObjItem.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                    ObjItem.setAttribute("BUYER",rsTmp.getInt("BUYER"));
                    ObjItem.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
                    
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
    
    
    
    
    
    public static boolean Delete(int pCompanyID,int pSrNo) {
        try {
            data.Execute("DELETE FROM D_COM_DEPT_BUYERS WHERE COMPANY_ID="+pCompanyID+" AND SR_NO="+pSrNo);
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
            clsDeptBuyers ObjItem=(clsDeptBuyers)pObjItem;
            
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_COM_DEPT_BUYERS");
            rsTmp.first();
            
            long NextNo=data.getMaxID(EITLERPGLOBAL.gCompanyID, "D_COM_DEPT_BUYERS", "SR_NO")+1;
            
            rsTmp.moveToInsertRow();
            rsTmp.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsTmp.updateLong("SR_NO",NextNo);
            rsTmp.updateInt("BUYER",(int)ObjItem.getAttribute("BUYER").getVal());
            rsTmp.updateInt("DEPT_ID",(int)ObjItem.getAttribute("DEPT_ID").getVal());
            rsTmp.updateBoolean("CHANGED",true);
            rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsTmp.updateInt("CREATED_BY",EITLERPGLOBAL.gUserID);
            rsTmp.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsTmp.updateInt("MODIFIED_BY",EITLERPGLOBAL.gUserID);
            rsTmp.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
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
