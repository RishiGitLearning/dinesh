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


public class clsTermType {
    
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
    public clsTermType() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("TERM_TYPE_ID",new Variant(0));
        props.put("TERM_TYPE_NAME",new Variant(""));
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
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_COM_TERM_TYPE_MASTER WHERE COMPANY_ID="+pCompanyID+" ORDER BY TERM_TYPE_ID ");
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
            if(getAttribute("TERM_TYPE_NAME").getObj().equals("")) {
                LastError="Please specify term type name";
                return false;
            }
            
            String TermTypeName=getAttribute("TERM_TYPE_NAME").getString();
            // data.getResult(""); /To get Resultset for given Query
            // To check whether any record retrieved, use getRow method
            // Call - ResultSet.first()
            // if ResultSet.getRow()>=0 : Record Found
            // else Not Found
            //*******************//
            
            
            //Conn.setAutoCommit(false);
            
            //Generate New No.
            long TermTypeID=data.getMaxID(EITLERPGLOBAL.gCompanyID,"D_COM_TERM_TYPE_MASTER","TERM_TYPE_ID");
            
            setAttribute("TERM_TYPE_ID",TermTypeID);
            
            rsResultSet.moveToInsertRow();
            rsResultSet.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            rsResultSet.updateLong("TERM_TYPE_ID", TermTypeID);
            rsResultSet.updateString("TERM_TYPE_NAME",getAttribute("TERM_TYPE_NAME").getString());
            rsResultSet.updateInt("CHANGED",1);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
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
            
            
            //Validations
            if(getAttribute("TERM_TYPE_NAME").getObj().equals("")) {
                LastError="Please specify term type name";
                return false;
            }
            
            //Conn.setAutoCommit(false);
            
            rsResultSet.updateString("TERM_TYPE_NAME",getAttribute("TERM_TYPE_NAME").getString());
            rsResultSet.updateInt("CHANGED",1);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
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
    
    
    public boolean CanDelete(int pCompanyID,int TermTypeID) {
        
        try {
            
            ResultSet rsTmp=data.getResult("SELECT TERM_TYPE_ID FROM D_COM_TERM_MASTER WHERE TERM_TYPE_ID="+TermTypeID);
            rsTmp.first();
            
            if(rsTmp.getRow()>=0) {
                LastError="Cannot delete the term type. It's being used by terms master";
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
            int TermTypeID=getAttribute("TERM_TYPE_ID").getInt();
            if(CanDelete(EITLERPGLOBAL.gCompanyID,TermTypeID)) {
                data.Execute("DELETE FROM D_COM_TERM_TYPE_MASTER WHERE TERM_TYPE_ID="+TermTypeID);
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
            
            String strSql = "SELECT * FROM D_COM_TERM_TYPE_MASTER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_COM_TERM_TYPE_MASTER WHERE COMPANY_ID="+pCompanyID+" ORDER BY TERM_TYPE_ID ";
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
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getConn();
        
        long Counter=0;
        int RevNo=0;
        
        try {
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("TERM_TYPE_ID",rsResultSet.getInt("TERM_TYPE_ID"));
            setAttribute("TERM_TYPE_NAME",UtilFunctions.getString(rsResultSet,"TERM_TYPE_NAME",""));
            setAttribute("CHANGED",rsResultSet.getInt("CHANGED"));
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
    
}
