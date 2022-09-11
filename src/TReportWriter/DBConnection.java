/*
 * DBConnection.java
 *
 * Created on June 26, 2007, 12:33 PM
 */

package TReportWriter;

import EITLERP.EITLERPGLOBAL;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;

/**
 *
 * @author  root
 */

public class DBConnection {
    
    public String ConnectionString="";
    public String UserName="";
    public String Password="";
    
    /** Creates a new instance of DBConnection */
    public DBConnection() {
        
    }
    
    public DBConnection(String dbURL) {
        ConnectionString=dbURL;
    }
    
    
    public java.sql.Connection getConnection(String dbURL) {
        java.sql.Connection objConn;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            objConn=DriverManager.getConnection(dbURL, EITLERPGLOBAL.DBUserName, EITLERPGLOBAL.DBPassword);
            
            return objConn;
        }
        catch(Exception e) {
            
            return null;
        }
        
    }
    
    public java.sql.Connection getConnection() {
        java.sql.Connection objConn;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            objConn=DriverManager.getConnection(ConnectionString, EITLERPGLOBAL.DBUserName, EITLERPGLOBAL.DBPassword);
            return objConn;
        }
        catch(Exception e) {
            return null;
        }
        
    }
    
    
    public ResultSet getResultSet(String Query) {
        try {
            Connection objConn=getConnection();
            Statement stTmp=objConn.createStatement();
            ResultSet rsTmp=stTmp.executeQuery(Query);
            
            return rsTmp;
        }
        catch(Exception e) {
            return null;
        }
    }
    
}
