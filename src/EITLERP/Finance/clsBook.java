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


public class clsBook {
    
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
    public clsBook() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("BOOK_CODE",new Variant(""));
        props.put("BOOK_TYPE",new Variant(0));
        props.put("BOOK_NAME",new Variant(""));
        props.put("MAIN_ACCOUNT_CODE",new Variant(""));
        
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
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FIN_BOOK_MASTER ORDER BY BOOK_CODE ");
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
            if(getAttribute("BOOK_CODE").getObj().equals("")) {
                LastError="Please specify book code";
                return false;
            }

            if(getAttribute("BOOK_NAME").getObj().equals("")) {
                LastError="Please specify book name";
                return false;
            }
            
            String strSQL="SELECT BOOK_CODE FROM D_FIN_BOOK_MASTER WHERE BOOK_CODE='"+getAttribute("BOOK_CODE").getString()+"' ";
            
            if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL))
            {
              LastError="Book code already exist";
              return false;
            }
            
            //Conn.setAutoCommit(false);
            
            rsResultSet.moveToInsertRow();
            rsResultSet.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            rsResultSet.updateString("BOOK_CODE",getAttribute("BOOK_CODE").getString());
            rsResultSet.updateInt("BOOK_TYPE",getAttribute("BOOK_TYPE").getInt());
            rsResultSet.updateString("BOOK_NAME",getAttribute("BOOK_NAME").getString());
            rsResultSet.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsResultSet.updateInt("CHANGED",1);
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
            if(getAttribute("BOOK_CODE").getObj().equals("")) {
                LastError="Please specify book code";
                return false;
            }

            if(getAttribute("BOOK_NAME").getObj().equals("")) {
                LastError="Please specify book name";
                return false;
            }
            
            rsResultSet.updateInt("CHANGED",1);
            rsResultSet.updateInt("BOOK_TYPE",getAttribute("BOOK_TYPE").getInt());
            rsResultSet.updateString("BOOK_NAME",getAttribute("BOOK_NAME").getString());
            rsResultSet.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
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
            }catch(Exception c){}
            LastError= e.getMessage();
            return false;
        }
    }
    
    
    public boolean CanDelete(int pCompanyID,String BookCode) {
        
        try {
            
            ResultSet rsTmp=data.getResult("SELECT BOOK_CODE FROM D_FIN_VOUCHER_HEADER WHERE BOOK_CODE='"+BookCode+"'",FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>=0) {
                LastError="Cannot delete the term type. It's being used by voucher";
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
            String BookCode=getAttribute("BOOK_CODE").getString();
            
            if(CanDelete(EITLERPGLOBAL.gCompanyID,BookCode)) {
                data.Execute("DELETE FROM D_FIN_BOOK_MASTER WHERE BOOK_CODE='"+BookCode+"'",FinanceGlobal.FinURL);
                return true;
            } else {
                return false;
            }
        } catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public boolean Filter(String pCondition,int pCompanyID) {
        Ready=false;
        try {
            
            String strSql = "SELECT * FROM D_FIN_BOOK_MASTER " + pCondition ;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_FIN_BOOK_MASTER ORDER BY BOOK_CODE ";
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
            setAttribute("BOOK_CODE",UtilFunctions.getString(rsResultSet,"BOOK_CODE",""));
            setAttribute("BOOK_TYPE",UtilFunctions.getInt(rsResultSet,"BOOK_TYPE",0));
            setAttribute("BOOK_NAME",UtilFunctions.getString(rsResultSet,"BOOK_NAME",""));
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
    
    
    public static String getBookName(int CompanyID,String BookCode)
    {
      return data.getStringValueFromDB("SELECT BOOK_NAME FROM D_FIN_BOOK_MASTER WHERE BOOK_CODE='"+BookCode+"'",FinanceGlobal.FinURL);  
    }

    public static String getBookMainCode(int CompanyID,String BookCode)
    {
      return data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_BOOK_MASTER WHERE BOOK_CODE='"+BookCode+"'",FinanceGlobal.FinURL);  
    }

    public static int getBookType(int CompanyID,String BookCode)
    {
      return data.getIntValueFromDB("SELECT BOOK_TYPE FROM D_FIN_BOOK_MASTER WHERE BOOK_CODE='"+BookCode+"'",FinanceGlobal.FinURL);  
    }
    
    public static boolean IsBankTR(String TRCode)
    {
      return data.IsRecordExist("SELECT VOUCHER_TYPE FROM D_FIN_BOOK_MASTER WHERE BOOK_CODE='"+TRCode+"' AND VOUCHER_TYPE=0",FinanceGlobal.FinURL);   
    }
    
}
