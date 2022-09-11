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


public class clsBrokerMaster {
    
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
    public clsBrokerMaster() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("BROKER_CODE",new Variant(""));        
        props.put("BROKER_NAME",new Variant(""));        
        props.put("BROKER_ADDRESS",new Variant(""));        
        props.put("BROKER_CITY",new Variant(""));        
        props.put("BROKER_PINCODE",new Variant(""));        
        
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
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FD_BROKER_MASTER WHERE COMPANY_ID='"+ EITLERPGLOBAL.gCompanyID +"' ORDER BY BROKER_CODE ");
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
            
            //*** Validations ***//
            
            if(getAttribute("BROKER_NAME").getObj().equals("")) {
                LastError="Please specify broker name";
                return false;
            }
            
            
            //long NewBrokerID=data.getMaxID("D_FD_BROKER_MASTER","BROKER_CODE",FinanceGlobal.FinURL);
            
            //--------- Generate New Internal Deposit Type ID  ------------
            //setAttribute("BROKER_CODE",NewBrokerID);
            
            rsResultSet.moveToInsertRow();
            rsResultSet.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            rsResultSet.updateString("BROKER_CODE",getAttribute("BROKER_CODE").getString());
            rsResultSet.updateString("BROKER_NAME",getAttribute("BROKER_NAME").getString());
            rsResultSet.updateString("BROKER_ADDRESS",getAttribute("BROKER_ADDRESS").getString());
            rsResultSet.updateString("BROKER_CITY",getAttribute("BROKER_CITY").getString());
            rsResultSet.updateString("BROKER_PINCODE",getAttribute("BROKER_PINCODE").getString());
            rsResultSet.updateInt("CHANGED",0);
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
            }catch(Exception c){}
            
            LastError= e.getMessage();
            return false;
        }
    }
    
    //Updates current record
    public boolean Update() {
        try {

            if(getAttribute("BROKER_NAME").getObj().equals("")) {
                LastError="Please specify broker name";
                return false;
            }
            
            rsResultSet.updateString("BROKER_CODE",getAttribute("BROKER_CODE").getString());
            rsResultSet.updateString("BROKER_NAME",getAttribute("BROKER_NAME").getString());
            rsResultSet.updateString("BROKER_ADDRESS",getAttribute("BROKER_ADDRESS").getString());
            rsResultSet.updateString("BROKER_CITY",getAttribute("BROKER_CITY").getString());
            rsResultSet.updateString("BROKER_PINCODE",getAttribute("BROKER_PINCODE").getString());
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            //rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            //rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateInt("CHANGED",1);
            rsResultSet.updateRow();
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            try {
            }catch(Exception c){}
            LastError= e.getMessage();
            return false;
        }
    }
    
    public boolean Filter(String pCondition,int pCompanyID) {
        Ready=false;
        try {
            
            String strSql = "SELECT * FROM D_FD_BROKER_MASTER " + pCondition ;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_FD_BROKER_MASTER WHERE COMPANY_ID='"+ EITLERPGLOBAL.gCompanyID +"' ORDER BY BROKER_CODE ";
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
            setAttribute("BROKER_CODE",UtilFunctions.getString(rsResultSet,"BROKER_CODE",""));
            setAttribute("BROKER_NAME",UtilFunctions.getString(rsResultSet,"BROKER_NAME",""));
            setAttribute("BROKER_ADDRESS",UtilFunctions.getString(rsResultSet,"BROKER_ADDRESS",""));
            setAttribute("BROKER_CITY",UtilFunctions.getString(rsResultSet,"BROKER_CITY",""));
            setAttribute("BROKER_PINCODE",UtilFunctions.getString(rsResultSet,"BROKER_PINCODE",""));
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
    
    public static String getBrokerName(int CompanyID,String BrokerCode)
    {
      return data.getStringValueFromDB("SELECT BROKER_NAME FROM D_FD_BROKER_MASTER WHERE COMPANY_ID='"+ CompanyID +"' BROKER_CODE='"+BrokerCode+"'",FinanceGlobal.FinURL);  
    }

        
    public static HashMap getList(String pCondition) {
        
        ResultSet rsTmp;
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            
            rsTmp=data.getResult("SELECT * FROM D_FD_BROKER_MASTER "+pCondition+" ORDER BY BROKER_NAME",FinanceGlobal.FinURL);            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsBrokerMaster ObjBrokerMaster=new clsBrokerMaster();
                
                //Populate the user
                ObjBrokerMaster.setAttribute("BROKER_CODE",rsTmp.getString("BROKER_CODE"));
                ObjBrokerMaster.setAttribute("BROKER_NAME",rsTmp.getString("BROKER_NAME"));
                ObjBrokerMaster.setAttribute("BROKER_ADDRESS",rsTmp.getString("BROKER_ADDRESS"));
                ObjBrokerMaster.setAttribute("BROKER_CITY",rsTmp.getString("BROKER_CITY"));
                ObjBrokerMaster.setAttribute("BROKER_PINCODE",rsTmp.getString("BROKER_PINCODE"));
                
                //Put the prepared user object into list
                List.put(Long.toString(Counter),ObjBrokerMaster);
                rsTmp.next();
            }//Out While
        }
        catch(Exception e) {
            //JOptionPane.showMessageDialog(null,"Error occured"+e.getMessage());
        }
        
        return List;
    }
    
}
