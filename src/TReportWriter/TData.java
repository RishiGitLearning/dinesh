/*
 * data.java
 *
 * Created on March 26, 2004, 12:21 PM
 */

package TReportWriter;

import EITLERP.EITLERPGLOBAL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;
//import java.net.*;
//import java.io.*;
//import java.awt.*;
//import javax.swing.*;
import java.util.HashMap;

/**
 *
 * @author  nrpithva
 * @version
 */


public class TData {
    
    public static String LastError="";
    
    public static HashMap connStack=new HashMap();
    
    /** Creates new data */
    public TData() {
    }
    
    
    
    
    public static boolean Execute(String strQry,String dbURL) {
        Connection Conn;
        Statement stmt;
        boolean Result=false;
        
        try {
            Conn=getConn(dbURL);
            stmt = Conn.createStatement();
            Result=stmt.execute(strQry);
            
            //Conn.close();
            stmt.close();
            
            return Result;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    
    public static void SetAutoCommit(String pURL,boolean pValue) {
        try {
            if(connStack.containsKey(pURL)) {
                Connection objConn=(Connection)connStack.get(pURL);
                objConn.setAutoCommit(pValue);
            }
        }
        catch(Exception e) {
            
        }
    }
    
    public static void SetCommit(String pURL) {
        try {
            Connection objConn=(Connection)connStack.get(pURL);
            objConn.commit();
            
        }
        catch(Exception e) {
            
        }
    }
    
    public static void SetRollback(String pURL) {
        try {
            Connection objConn=(Connection)connStack.get(pURL);
            objConn.rollback();
            
        }
        catch(Exception e) {
            
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    public static Connection getConn(String pURL) {
        Connection Conn;
        
        try {
            
            if(connStack.containsKey(pURL)) {
                Conn=(Connection)connStack.get(pURL);
            }
            else {
                
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                DriverManager.setLoginTimeout(1000);
                Conn=DriverManager.getConnection(pURL, EITLERPGLOBAL.DBUserName, EITLERPGLOBAL.DBPassword);
                
                connStack.put(pURL, Conn);
                
            }
            return Conn;
        }
        catch(Exception e) {
            Conn=null;
            return Conn;
        }
    }
    
    
    
    
    public static ResultSet getResult(String pQuery,String pURL) {
        
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp=null;
        
        try {
            tmpConn=getConn(pURL);
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery(pQuery);
            rsTmp.first();
        }
        catch(Exception e) {
            
        }
        
        return rsTmp;
    }
    
    
    
    public static boolean IsRecordExist(String Query,String dbURL) {
        boolean Exist=false;
        
        try {
            Connection objConn=getConn(dbURL);
            Statement stTmp=objConn.createStatement();
            ResultSet rsTmp=stTmp.executeQuery(Query);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                return true;
            }
            else {
                return false;
            }
            
        }
        catch(Exception e) {
            return false;
        }
    }
   
    
}
