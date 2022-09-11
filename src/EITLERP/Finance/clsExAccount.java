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


public class clsExAccount {
    
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
    public clsExAccount() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("EX_ACCOUNT_CODE",new Variant(""));
        props.put("EX_ACCOUNT_NAME",new Variant(""));
        
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
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FIN_EX_ACCOUNT_MASTER ORDER BY EX_ACCOUNT_CODE");
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
            if(getAttribute("EX_ACCOUNT_CODE").getObj().equals("")) {
                LastError="Please specify extended account code";
                return false;
            }
            
            if(getAttribute("EX_ACCOUNT_NAME").getObj().equals("")) {
                LastError="Please specify extended account name";
                return false;
            }
            
            
            String strSQL="SELECT EX_ACCOUNT_CODE FROM D_FIN_EX_ACCOUNT_MASTER WHERE EX_ACCOUNT_CODE='"+getAttribute("EX_ACCOUNT_CODE").getString()+"' ";
            
            if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                LastError="Extended code already exist.";
                return false;
            }
            
            //Conn.setAutoCommit(false);
            
            rsResultSet.moveToInsertRow();
            rsResultSet.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            rsResultSet.updateString("EX_ACCOUNT_CODE",getAttribute("EX_ACCOUNT_CODE").getString());
            rsResultSet.updateString("EX_ACCOUNT_NAME",getAttribute("EX_ACCOUNT_NAME").getString());
            rsResultSet.updateInt("CHANGED",1);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("ADDRESS",getAttribute("ADDRESS").getString());
            rsResultSet.updateString("CITY",getAttribute("CITY").getString());
            rsResultSet.updateString("PAN_NO",getAttribute("PAN_NO").getString());
            rsResultSet.updateBoolean("DECLARATION_RECEIVE",getAttribute("DECLARATION_RECEIVE").getBool());            
            rsResultSet.insertRow();
            
            
            //Conn.commit();
            //Conn.setAutoCommit(true);
            
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
            
            if(getAttribute("EX_ACCOUNT_NAME").getObj().equals("")) {
                LastError="Please specify extended account name";
                return false;
            }
            
            //Conn.setAutoCommit(false);
            
            rsResultSet.updateInt("CHANGED",1);
            rsResultSet.updateString("EX_ACCOUNT_NAME",getAttribute("EX_ACCOUNT_NAME").getString());
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("ADDRESS",getAttribute("ADDRESS").getString());
            rsResultSet.updateString("CITY",getAttribute("CITY").getString());
            rsResultSet.updateString("PAN_NO",getAttribute("PAN_NO").getString());            
            rsResultSet.updateBoolean("DECLARATION_RECEIVE",getAttribute("DECLARATION_RECEIVE").getBool());            
            rsResultSet.updateRow();
            
            
            //Conn.commit();
            //Conn.setAutoCommit(true);
            
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
    
    
    public boolean CanDelete(int pCompanyID,String ExAccountCode) {
        
        try {
            
            ResultSet rsTmp=data.getResult("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE MAIN_ACCOUNT_CODE LIKE '%"+ExAccountCode+"%'",FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>=0) {
                LastError="Cannot delete the record. It's being used by voucher";
                return false;
            }
            else {
                return true;
            }
        }
        catch(Exception e) {
            LastError= e.getMessage();
            return false;
        }
    }
    
    
    public boolean Delete() {
        try {
            
            String ExAccountCode=getAttribute("EX_ACCOUNT_CODE").getString();
            
            if(CanDelete(EITLERPGLOBAL.gCompanyID,ExAccountCode)) {
                data.Execute("DELETE FROM D_FIN_EX_ACCOUNT_MASTER WHERE EX_ACCOUNT_CODE='"+ExAccountCode+"'",FinanceGlobal.FinURL);
                return true;
            }
            else {
                return false;
            }
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public boolean Filter(String pCondition,int pCompanyID) {
        Ready=false;
        try {
            
            String strSql = "SELECT * FROM D_FIN_EX_ACCOUNT_MASTER " + pCondition ;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_FIN_EX_ACCOUNT_MASTER ORDER BY EX_ACCOUNT_CODE ";
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
            setAttribute("EX_ACCOUNT_CODE",UtilFunctions.getString(rsResultSet,"EX_ACCOUNT_CODE",""));
            setAttribute("EX_ACCOUNT_NAME",UtilFunctions.getString(rsResultSet,"EX_ACCOUNT_NAME",""));
            setAttribute("CHANGED",UtilFunctions.getInt(rsResultSet,"CHANGED",0));
            setAttribute("CHANGED_DATE",UtilFunctions.getString(rsResultSet,"CHANGED_DATE","0000-00-00"));
            setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            setAttribute("CREATED_DATE",UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00"));
            setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            setAttribute("MODIFIED_DATE",UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00"));
            setAttribute("ADDRESS",UtilFunctions.getString(rsResultSet,"ADDRESS",""));
            setAttribute("CITY",UtilFunctions.getString(rsResultSet,"CITY",""));
            setAttribute("PAN_NO",UtilFunctions.getString(rsResultSet,"PAN_NO",""));
            setAttribute("DECLARATION_RECEIVE",UtilFunctions.getBoolean(rsResultSet,"DECLARATION_RECEIVE",false));
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public static String getExAccountName(String MainAccountCode) {
        String ExAccountName="";
        
        try {
            if(MainAccountCode.indexOf(".")!=-1) {
                HashMap list=EITLERPGLOBAL.Split(MainAccountCode, ".");
                String MainCode = list.get("1").toString();
                String ExCode=list.get("2").toString();
                if(ExCode.equals("98") || ExCode.equals("99") || ExCode.equals("31")) {
                    ExAccountName=data.getStringValueFromDB("SELECT ACCOUNT_NAME FROM D_FIN_GL WHERE MAIN_ACCOUNT_CODE='"+MainCode+"'",FinanceGlobal.FinURL);
                } else {
                    ExAccountName=data.getStringValueFromDB("SELECT EX_ACCOUNT_NAME FROM D_FIN_EX_ACCOUNT_MASTER WHERE EX_ACCOUNT_CODE="+ExCode,FinanceGlobal.FinURL);
                }
            }
        }
        catch(Exception e) {
            
        }
        
        return ExAccountName;
    }
}
