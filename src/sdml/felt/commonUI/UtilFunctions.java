/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdml.felt.commonUI;

/**
 *
 * @author root
 */

import java.sql.*;

public class UtilFunctions {
    
    /** Creates a new instance of UtilFunctions */
    public UtilFunctions() {
        
    }
    
    public static String getString(ResultSet rs,String columnName,String DefaultValue) {
        try {
            if(rs.getString(columnName)==null) {
                return DefaultValue;
            }
            else {
                return rs.getString(columnName);
            }
        }
        catch(Exception e) {
            return DefaultValue;
        }
    }
    
    public static String getString(ResultSet rs,int columnIndex,String DefaultValue) {
        try {
            if(rs.getString(columnIndex)==null) {
                return DefaultValue;
            }
            else {
                return rs.getString(columnIndex);
            }
        }
        catch(Exception e) {
            return DefaultValue;
        }
    }
    
    
    
    
    public static int getInt(ResultSet rs,String columnName,int DefaultValue) {
        try {
            return rs.getInt(columnName);
            
        }
        catch(Exception e) {
            return DefaultValue;
        }
    }
    
    public static int getInt(ResultSet rs,int columnIndex,int DefaultValue) {
        try {
            return rs.getInt(columnIndex);
        }
        catch(Exception e) {
            return DefaultValue;
        }
    }
    
    
    public static boolean getBoolean(ResultSet rs,String columnName,boolean DefaultValue) {
        try {
            return rs.getBoolean(columnName);
        }
        catch(Exception e) {
            return DefaultValue;
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
    
    public static boolean getInt(ResultSet rs,int columnIndex,boolean DefaultValue) {
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
    
    public static long getLong(ResultSet rs,int columnIndex,long DefaultValue) {
        try {
            return rs.getLong(columnIndex);
        }
        catch(Exception e) {
            return DefaultValue;
        }
    }
    
    
    
    public static double getDouble(ResultSet rs,String columnName,double DefaultValue) {
        try {
            return rs.getDouble(columnName);
            
        }
        catch(Exception e) {
            return DefaultValue;
        }
    }
    
    public static double getDouble(ResultSet rs,int columnIndex,double DefaultValue) {
        try {
            return rs.getDouble(columnIndex);
        }
        catch(Exception e) {
            return DefaultValue;
        }
    }
    
    
    public static int CInt(String intValue) {
        int returnValue=0;
        
        try {
            returnValue=Integer.parseInt(intValue);
            
        }
        catch(Exception e) {
            returnValue=0;
        }
        
        return returnValue;
    }
    
    
    public static double CDbl(String dblValue) {
        double returnValue=0;
        
        try {
            returnValue=Double.parseDouble(dblValue);
            
        }
        catch(Exception e) {
            returnValue=0;
        }
        
        return returnValue;
    }
    
    
    public static String[] splitRecord(String fileRecord, int RecordLength) {
        try {
            
            int Counter=0;
            int ArrayLength=(int)SDMLERPGLOBAL.round(fileRecord.length()/RecordLength,0);
            
            String[] Records= new String[ArrayLength];
            
            for(int i=0;i<fileRecord.length();i+=RecordLength) {
                Records[Counter]=fileRecord.substring(i,i+RecordLength);
                Counter++;
            }
            
            return Records;
        }
        catch(Exception e) {
            e.printStackTrace();
            return new String[1];
        }
        
    }
    
//    public static double getDobleFromObject(TReportWriter.SimpleDataProvider.TRow Obj, String Str){
//        double tmpValue=0;
//        if (Obj.getValue(Str).equals("")){
//            return tmpValue;
//        }
//        else {
//            tmpValue =Double.parseDouble(Obj.getValue(Str));
//            return (tmpValue);
//            
//        }
//        
//    }
    
}
