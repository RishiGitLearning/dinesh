/*
 * clsMaterialRequisition.java
 *
 * Created on April 19, 2004, 2:35 PM
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

public class clsDepositTypeMaster {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    
    public boolean Ready = false;
    
    
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
    
    
    /** Creates new clsMaterialRequisition */
    public clsDepositTypeMaster() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("DEPOSIT_TYPE_ID",new Variant(0));        
        props.put("DEPOSIT_TYPE_NAME",new Variant(""));        
        
        props.put("CHANGED",new Variant(0));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        
    }
    
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FD_DEPOSIT_TYPE_MASTER ORDER BY DEPOSIT_TYPE_ID ");
            Ready=true;
            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public void Close() {
        try {
            Stmt.close();
            rsResultSet.close();
        }
        catch(Exception e) {
            
        }
    }
    
    //Navigation Methods
    public boolean MoveFirst() {
        try {
            rsResultSet.first();
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
        
    }
    
    public boolean MoveNext() {
        try {
            if(rsResultSet.isAfterLast()||rsResultSet.isLast()) {
                //Move pointer at last record
                //If it is beyond eof
                rsResultSet.last();
            }
            else {
                rsResultSet.next();
                if(rsResultSet.isAfterLast()) {
                    rsResultSet.last();
                }
            }
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public boolean MovePrevious() {
        try {
            if(rsResultSet.isFirst()||rsResultSet.isBeforeFirst()) {
                rsResultSet.first();
            }
            else {
                rsResultSet.previous();
                if(rsResultSet.isBeforeFirst()) {
                    rsResultSet.first();
                }
            }
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public boolean MoveLast() {
        try {
            rsResultSet.last();
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public boolean Insert() {
        try {

            if(getAttribute("DEPOSIT_TYPE_NAME").getObj().equals("")) {
                LastError="Please specify deposit type name";
                return false;
            }
            
            long NewTypeID=data.getMaxID("D_FD_DEPOSIT_TYPE_MASTER","DEPOSIT_TYPE_ID",FinanceGlobal.FinURL);
            
            //--------- Generate New Internal Deposit Type ID  ------------
            setAttribute("DEPOSIT_TYPE_ID",NewTypeID);
            
            rsResultSet.moveToInsertRow();
            rsResultSet.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            rsResultSet.updateInt("DEPOSIT_TYPE_ID",getAttribute("DEPOSIT_TYPE_ID").getInt());
            rsResultSet.updateString("DEPOSIT_TYPE_NAME",getAttribute("DEPOSIT_TYPE_NAME").getString());
            rsResultSet.updateBoolean("CHANGED",false);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.insertRow();
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            try {
                //Conn.rollback();
                //Conn.setAutoCommit(true);
            }catch(Exception c){}
            
            LastError= e.getMessage();
            return false;
        }
    }
    
    //Updates current record
    public boolean Update() {
        try {
            if(getAttribute("DEPOSIT_TYPE_NAME").getObj().equals("")) {
                LastError="Please specify deposit type name";
                return false;
            }
            
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("DEPOSIT_TYPE_NAME",getAttribute("DEPOSIT_TYPE_NAME").getString());
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateRow();
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            try {
                //Conn.rollback();
                //Conn.setAutoCommit(true);
            }catch(Exception c){}
            LastError= e.getMessage();
            return false;
        }
    }
    
    public boolean Filter(String pCondition,int pCompanyID) {
        Ready=false;
        try {
            
            String strSql = "SELECT * FROM D_FD_DEPOSIT_TYPE_MASTER " + pCondition ;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_FD_DEPOSIT_TYPE_MASTER ORDER BY DEPOSIT_TYPE_ID ";
                rsResultSet=Stmt.executeQuery(strSql);
                Ready=true;
                MoveLast();
                return false;
            }
            else {
                Ready=true;
                MoveLast();
                return true;
            }
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public boolean setData() {
        
        try {
            
            setAttribute("COMPANY_ID",UtilFunctions.getInt(rsResultSet,"COMPANY_ID",0));
            setAttribute("DEPOSIT_TYPE_ID",UtilFunctions.getString(rsResultSet,"DEPOSIT_TYPE_ID",""));
            setAttribute("DEPOSIT_TYPE_NAME",UtilFunctions.getString(rsResultSet,"DEPOSIT_TYPE_NAME",""));
            setAttribute("CHANGED",UtilFunctions.getInt(rsResultSet,"CHANGED",0));
            setAttribute("CHANGED_DATE",UtilFunctions.getString(rsResultSet,"CHANGED_DATE","0000-00-00"));
            setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            setAttribute("CREATED_DATE",UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00"));
            setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            setAttribute("MODIFIED_DATE",UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00"));
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public static String getDepositName(int CompanyID,String TypeCode) {
      return data.getStringValueFromDB("SELECT DEPOSIT_TYPE_NAME FROM D_FD_DEPOSIT_TYPE_MASTER WHERE DEPOSIT_TYPE_ID='"+TypeCode+"'",FinanceGlobal.FinURL);  
    }
        
    public static HashMap getList(String pCondition) {
        ResultSet rsTmp;
        HashMap List=new HashMap();
        long Counter=0;
        try {
            rsTmp=data.getResult("SELECT * FROM D_FD_DEPOSIT_TYPE_MASTER "+pCondition+" ORDER BY DEPOSIT_TYPE_ID",FinanceGlobal.FinURL);            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsDepositTypeMaster ObjTypeMaster=new clsDepositTypeMaster();
                
                //Populate the user
                ObjTypeMaster.setAttribute("DEPOSIT_TYPE_ID",rsTmp.getInt("DEPOSIT_TYPE_ID"));
                ObjTypeMaster.setAttribute("DEPOSIT_TYPE_NAME",rsTmp.getString("DEPOSIT_TYPE_NAME"));
                
                //Put the prepared user object into list
                List.put(Long.toString(Counter),ObjTypeMaster);
                rsTmp.next();
            }//Out While
        }
        catch(Exception e) {
        }
        return List;
    }
}