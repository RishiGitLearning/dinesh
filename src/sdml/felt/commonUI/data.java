/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdml.felt.commonUI;

import EITLERP.EITLERPGLOBAL;
import java.sql.*;
import java.net.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;

/**
 *
 * @author  nrpithva
 * @version
 */


public class data {
    
    public static String LastError="";
    
    public static HashMap connStack=new HashMap();
    
    /** Creates new data */
    public data() {
    }
    
    public static boolean Execute(String strQry) {
        Connection Conn;
        Statement stmt;
        boolean Result=false;
        
        try {
            Conn=getConn();
            stmt = Conn.createStatement();
            Result=stmt.execute(strQry);
            //JOptionPane.showMessageDialog(null, strQry+" ,Result "+Result);
            
            stmt.close();
            
            return Result;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
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
    
    
    
    public static void SetAutoCommit(boolean pValue) {
        try {
            
            Connection objConn=getConn();
            objConn.setAutoCommit(pValue);
            
        }
        catch(Exception e) {
            
        }
    }
    
    public static void SetCommit() {
        try {
            Connection objConn=getConn();
            objConn.commit();
            
        }
        catch(Exception e) {
            
        }
    }
    
    public static void SetRollback() {
        try {
            Connection objConn=getConn();
            objConn.rollback();
            
        }
        catch(Exception e) {
            
        }
    }
    
    public static long getMaxID(long pCompanyID,String pTable,String pField) {
        try {
            Connection Conn=getConn();
            Statement stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String strSQL="SELECT MAX("+pField+") AS MAXID FROM "+pTable+" WHERE COMPANY_ID="+Long.toString(pCompanyID);
            
            ResultSet rsTmp=stmt.executeQuery(strSQL);
            
            rsTmp.first();
            
            long theNo=rsTmp.getLong("MAXID")+1;
            
            //Conn.close();
            stmt.close();
            rsTmp.close();
            return theNo;
        }
        catch(Exception e) {
            return 1;
        }
    }
    
    public static long getMaxID(long pCompanyID,String pTable,String pField,String dbURL) {
        try {
            
            Connection Conn=getConn(dbURL);
            Statement stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String strSQL="SELECT MAX("+pField+") AS MAXID FROM "+pTable+" WHERE COMPANY_ID="+Long.toString(pCompanyID);
            
            ResultSet rsTmp=stmt.executeQuery(strSQL);
            
            rsTmp.first();
            
            long theNo=rsTmp.getLong("MAXID")+1;
            
            //Conn.close();
            stmt.close();
            rsTmp.close();
            return theNo;
        }
        catch(Exception e) {
            return 1;
        }
    }
    
    public static long getMaxID(String pTable,String pField,String dbURL) {
        try {
            
            Connection Conn=getConn(dbURL);
            Statement stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String strSQL="SELECT MAX("+pField+") AS MAXID FROM "+pTable;
            
            ResultSet rsTmp=stmt.executeQuery(strSQL);
            
            rsTmp.first();
            
            long theNo=rsTmp.getLong("MAXID")+1;
            
            //Conn.close();
            stmt.close();
            rsTmp.close();
            return theNo;
        }
        catch(Exception e) {
            return 1;
        }
    }
    
    
    //Returns the connection
    public static Connection getCreatedConn() {
        Connection Conn;
        try {
            
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            DriverManager.setLoginTimeout(2);
            Conn=DriverManager.getConnection(SDMLERPGLOBAL.DatabaseURL, EITLERPGLOBAL.DBUserName, EITLERPGLOBAL.DBPassword);
            
            return Conn;
        }
        catch(Exception e) {
            e.printStackTrace();
            Conn=null;
            return Conn;
        }
    }
    
//    public static Connection getCreatedConn(String NEW) {
//        Connection Conn;
//        try {
//            
//            Class.forName("com.mysql.jdbc.Driver").newInstance();
//            DriverManager.setLoginTimeout(2);
//            Conn=DriverManager.getConnection(SDMLERPGLOBAL.DatabaseURL_NewDatabase);
//            
//            return Conn;
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//            Conn=null;
//            return Conn;
//        }
////    }
    
    public static Connection getConn() {
        try {
            
            if(SDMLERPGLOBAL.gConn==null) {
                OpenGlobalConnection();
            }
            else {
                if(SDMLERPGLOBAL.gConn.isClosed()) {
                    OpenGlobalConnection();
                }
            }
        }
        catch(Exception e){
            try {
                BufferedWriter aFile=new BufferedWriter(new FileWriter(new File("datalog.txt")));
                aFile.write(SDMLERPGLOBAL.getCurrentDateDB()+SDMLERPGLOBAL.getCurrentTime());
                aFile.write("Data connection failed");
                aFile.close();
            }catch(Exception l){}
            
        }
        return SDMLERPGLOBAL.gConn;
        
    }
    
    
    public static Connection getConnOld() {
        Connection Conn;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Conn=DriverManager.getConnection(SDMLERPGLOBAL.DatabaseURL+SDMLERPGLOBAL.DBUserName, EITLERPGLOBAL.DBUserName, EITLERPGLOBAL.DBPassword);
            return Conn;
        }
        catch(Exception e) {
            Conn=null;
            return Conn;
        }
    }
    
    public static Connection getConn(String pURL) {
        Connection Conn;
        
        try {
            
            if(connStack.containsKey(pURL)) {
                Conn=(Connection)connStack.get(pURL);
            } else {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                DriverManager.setLoginTimeout(1000);
                Conn=DriverManager.getConnection(pURL+SDMLERPGLOBAL.DBUserName, EITLERPGLOBAL.DBUserName, EITLERPGLOBAL.DBPassword);
                connStack.put(pURL, Conn);
            }
            return Conn;
        } catch(Exception e) {
            System.out.println("Failed connection "+pURL+SDMLERPGLOBAL.DBUserName);
            Conn=null;
            return Conn;
        }
    }
    
    public static String getFirstFree(long pCompanyID,int pModuleID,String pPrefix) {
        try {
            Connection Conn=getConn();
            Statement stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String strSQL="SELECT LAST_USED_NO AS LASTID FROM D_COM_FIRSTFREE WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND MODULE_ID=" + Integer.toString(pModuleID) + " AND PREFIX_CHARS='" + pPrefix.trim() + "'";
            
            ResultSet rsTmp=stmt.executeQuery(strSQL);
            rsTmp.first();
            long lLastNo = rsTmp.getLong("LASTID")+1;
            String strLastNo = pPrefix + Long.toString(lLastNo);
            
            stmt.executeUpdate("UPDATE D_COM_FIRSTFREE SET LAST_USED_NO=" + Long.toString(lLastNo) + ",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND MODULE_ID=" + Integer.toString(pModuleID) + " AND PREFIX_CHARS='" + pPrefix.trim() + "'" );
            
            
            //Conn.close();
            stmt.close();
            rsTmp.close();
            
            return strLastNo;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static ResultSet getResult(String pQuery) {
        
        Connection tmpConn;
        Statement stTmpNew=null;
        ResultSet rsTmpNew=null;
        
        try {
            tmpConn=getConn();
            stTmpNew=tmpConn.createStatement();
            System.out.println("Query : "+pQuery);
            rsTmpNew=stTmpNew.executeQuery(pQuery);
            rsTmpNew.first();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return rsTmpNew;
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
    
    
    public static void OpenGlobalConnection() {
        try {
            
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            SDMLERPGLOBAL.gConn=DriverManager.getConnection(SDMLERPGLOBAL.DatabaseURL+SDMLERPGLOBAL.DBUserName, EITLERPGLOBAL.DBUserName, EITLERPGLOBAL.DBPassword);
            
            //SDMLERPGLOBAL.gConn.setTransactionIsolation(
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public static void OpenGlobalConnection(String dbURL) {
        try {
            
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            SDMLERPGLOBAL.gConn=DriverManager.getConnection(dbURL,SDMLERPGLOBAL.DBUserName,SDMLERPGLOBAL.DBPassword);
            
        }
        catch(Exception e) {
            
        }
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
    
    
    public static boolean IsRecordExist(String Query) {
        boolean Exist=false;
        
        try {
            Connection objConn=getConn();
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
    
    public static String getStringValueFromDB(String Query,String dbURL) {
        String strValue="";
        
        try {
            
            Connection objConn=getConn(dbURL);
            Statement stTmp=objConn.createStatement();
            ResultSet rsTmp=stTmp.executeQuery(Query);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                strValue=UtilFunctions.getString(rsTmp, 1, "");
                return strValue;
            }
            else {
                return "";
            }
            
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    public static String getStringValueFromDB(String Query) {
        String strValue="";
        
        try {
            
            Connection objConn=getConn();
            Statement stTmp=objConn.createStatement();
            ResultSet rsTmp=stTmp.executeQuery(Query);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                strValue=UtilFunctions.getString(rsTmp, 1, "");
                return strValue;
            }
            else {
                return "";
            }
            
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static int getIntValueFromDB(String Query,String dbURL) {
        int intValue=0;
        
        try {
            
            Connection objConn=getConn(dbURL);
            Statement stTmp=objConn.createStatement();
            ResultSet rsTmp=stTmp.executeQuery(Query);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                intValue=UtilFunctions.getInt(rsTmp,1, 0);
                return intValue;
            }
            else {
                return 0;
            }
            
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    
    
    public static int getIntValueFromDB(String Query) {
        int intValue=0;
        
        try {
            
            Connection objConn=getConn();
            Statement stTmp=objConn.createStatement();
            ResultSet rsTmp=stTmp.executeQuery(Query);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                intValue=UtilFunctions.getInt(rsTmp,1, 0);
                return intValue;
            }
            else {
                return 0;
            }
            
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    
    public static long getLongValueFromDB(String Query,String dbURL) {
        long longValue=0;
        
        try {
            
            Connection objConn=getConn(dbURL);
            Statement stTmp=objConn.createStatement();
            ResultSet rsTmp=stTmp.executeQuery(Query);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                longValue=UtilFunctions.getLong(rsTmp,1, 0);
                return longValue;
            }
            else {
                return 0;
            }
            
            
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    public static long getLongValueFromDB(String Query) {
        long longValue=0;
        
        try {
            
            Connection objConn=getConn();
            Statement stTmp=objConn.createStatement();
            ResultSet rsTmp=stTmp.executeQuery(Query);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                longValue=UtilFunctions.getLong(rsTmp,1, 0);
                return longValue;
            }
            else {
                return 0;
            }
            
            
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    
    public static double getDoubleValueFromDB(String Query,String dbURL) {
        double doubleValue=0;
        
        try {
            Connection objConn=getConn(dbURL);
            Statement stTmp=objConn.createStatement();
            ResultSet rsTmp=stTmp.executeQuery(Query);
            rsTmp.first();
            
            
            if(rsTmp.getRow()>0) {
                doubleValue=rsTmp.getDouble(1);
                return doubleValue;
            }
            else {
                return 0;
            }
            
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    
    public static double getDoubleValueFromDB(String Query) {
        double doubleValue=0;
        
        try {
            Connection objConn=getConn();
            Statement stTmp=objConn.createStatement();
            ResultSet rsTmp=stTmp.executeQuery(Query);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                doubleValue=UtilFunctions.getDouble(rsTmp,1, 0);
                return doubleValue;
            }
            else {
                return 0;
            }
            
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    public static boolean getBoolean(ResultSet rs,int columnIndex,boolean DefaultValue) {
        try {
            return rs.getBoolean(columnIndex);
        }
        catch(Exception e) {
            return DefaultValue;
        }
    }
    
    
    public static long getLong(ResultSet rs,String columnName,long DefaultValue) {
        try {
            return rs.getLong(columnName);
            
        }
        catch(Exception e) {
            return DefaultValue;
        }
    }
    
    public static boolean getBoolValueFromDB(String Query,String dbURL) {
        boolean boolValue=false;
        try {
            Connection objConn=getConn(dbURL);
            Statement stTmp=objConn.createStatement();
            ResultSet rsTmp=stTmp.executeQuery(Query);
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                boolValue=UtilFunctions.getBoolean(rsTmp,1, false);
                return boolValue;
            }
            else {
                return false;
            }
        }
        catch(Exception e) {
            return false;
        }
    }
    public static boolean getBoolValueFromDB(String Query) {
        boolean boolValue=false;
        try {
            Connection objConn=getConn();
            Statement stTmp=objConn.createStatement();
            ResultSet rsTmp=stTmp.executeQuery(Query);
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                boolValue=UtilFunctions.getBoolean(rsTmp,1, false);
                return boolValue;
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
