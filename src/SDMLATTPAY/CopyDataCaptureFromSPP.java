/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SDMLATTPAY;

import EITLERP.EITLERPGLOBAL;
import java.sql.*;
import sdml.felt.commonUI.data;

/**
 *
 * @author root
 */
public class CopyDataCaptureFromSPP {
    
public static void main(String[] args) {
    
  System.out.println("Copy data capture from SPP table to 227");
  Connection connlocal = null,connremote = null;
  ResultSet rsTmpLocal,rsTmpRemote;
  Statement stlocal = null, stremote = null;
  String urllocal = "jdbc:mysql://200.0.0.227:3306/";
  String urlremote = "jdbc:mysql://200.0.0.229:3307/";
  String dbName = "Ingress";//?zeroDateTimeBehavior=convertToNull
  String driver = "com.mysql.jdbc.Driver";
  String userName = "root"; 
  String password = "att@229";
  String strSQLLocal="",strSQLRemote="";
    try {
        Class.forName(driver).newInstance();
        connlocal = DriverManager.getConnection(urllocal + dbName, "root", EITLERPGLOBAL.DBPassword);
        connremote = DriverManager.getConnection(urlremote + dbName, userName, password);

        stlocal = connlocal.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        stremote = connremote.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

        stlocal.execute("TRUNCATE TABLE Ingress.temp_auditdata");
        
//        strSQLRemote += "SELECT * FROM Ingress.auditdata";
        strSQLRemote += "SELECT * FROM Ingress.auditdata WHERE DATE(checktime)>SUBDATE(CURDATE() , INTERVAL 30 DAY)";  //3
        
        //Copy table
        System.out.println("SQL:" + strSQLRemote);
        rsTmpRemote = stremote.executeQuery(strSQLRemote);
        rsTmpRemote.first();
        while (!rsTmpRemote.isAfterLast()) {
            strSQLLocal = "INSERT INTO Ingress.temp_auditdata VALUES(";
            strSQLLocal += "'" + rsTmpRemote.getString("idAttendance") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("serialno") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("userid") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("verifycode") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("checktime") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("checktype") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("workcode") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("eventType") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("Flag") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("ControllerDoorNo") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("AttendDate") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("AttendSlot") + "', ";
            strSQLLocal += "'" + rsTmpRemote.getString("CtrlSum") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("IsAttend") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("isvalid") + "')";
//            System.out.println(strSQLLocal);
            stlocal.execute(strSQLLocal);
            rsTmpRemote.next();
        }
        
        stlocal.execute("INSERT INTO Ingress.auditdata SELECT * FROM Ingress.temp_auditdata WHERE idAttendance NOT IN (SELECT idAttendance FROM Ingress.auditdata WHERE DATE(checktime)>SUBDATE(CURDATE() , INTERVAL 30 DAY))");  //3
        
        System.out.println("Process Done.");
    } catch (Exception e) {
        e.printStackTrace();
    }
  }
}