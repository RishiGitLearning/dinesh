/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.Utils;

import EITLERP.EITLERPGLOBAL;
import java.sql.*;

/**
 *
 * @author root
 */
public class CopyTableFromSPP_old {
    
public static void main(String[] args) {
     /*   //0 - month
        //1 - year
        
        if(args.length<2) {
            System.out.println("Insufficient arguments. Please specify \n 1. Month \n2. Year ");
            return;
        }
        
        int month=Integer.parseInt(args[0]);
        int year=Integer.parseInt(args[1]);  
             */
  System.out.println("Copy data from SPP table to another!");
  Connection connlocal = null,connremote = null;
  ResultSet rsTmpLocal,rsTmpRemote;
  Statement stlocal = null, stremote = null;
  String urllocal = "jdbc:mysql://200.0.0.227:3306/";
  String urlremote = "jdbc:mysql://200.0.0.229:3307/";
  String dbName = "SDMLATTPAY";//?zeroDateTimeBehavior=convertToNull
  String driver = "com.mysql.jdbc.Driver";
  String userName = "root"; 
  String password = "att@229";
  String strSQLLocal="",strSQLLocal1="",strSQLRemote="",strSQLRemote1="";
    try {
        Class.forName(driver).newInstance();
        connlocal = DriverManager.getConnection(urllocal + dbName, "root", EITLERPGLOBAL.DBPassword);
        connremote = DriverManager.getConnection(urlremote + dbName, userName, password);

        //stlocal = connlocal.createStatement();
        //stremote=connremote.createStatement();
        stlocal = connlocal.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        stremote = connremote.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

        stlocal.execute("TRUNCATE TABLE SDMLATTPAY.ATTPAY_EMPMST_FROM_SPP");
        
        //stlocal.execute("DELETE FROM SDMLATTPAY.PAYROLL_MTH_SUMMARY_FROM_SPP WHERE PAY_MONTH="+month+" AND PAY_YEAR="+year+"");
        stlocal.execute("DELETE FROM SDMLATTPAY.PAYROLL_MTH_SUMMARY_FROM_SPP WHERE PAY_MONTH=1 AND PAY_YEAR=2020");
        
        strSQLRemote += "SELECT * FROM SDMLATTPAY.ATTPAY_EMPMST_FROM_SPP ";
        strSQLRemote1 += "SELECT * FROM SDMLATTPAY.PAYROLL_MTH_SUMMARY_FROM_SPP WHERE PAY_MONTH=1 AND PAY_YEAR=2020";
        
        //Copy table
        System.out.println("SQL:" + strSQLRemote);
        rsTmpRemote = stremote.executeQuery(strSQLRemote);
        rsTmpRemote.first();
        while (!rsTmpRemote.isAfterLast()) {
            strSQLLocal = "INSERT INTO SDMLATTPAY.ATTPAY_EMPMST_FROM_SPP VALUES(";
            strSQLLocal += "'" + rsTmpRemote.getString("PAY_EMPID") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("EMP_NAME") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("FATHER_NAME") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("MOTHER_NAME") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("SEX") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("DESIGNATION") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("DATE_OF_BIRTH") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("DATE_OF_JOINING") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("DATE_OF_LEAVING") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("REASON_FOR_LEAVING") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("DEPARTMENT") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("CATEGORY") + "', ";
            strSQLLocal += "'" + rsTmpRemote.getString("PERMANENT_ADDRESS") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("PRESENT_ADDRESS") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("BASIC_PAY") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("PERSONAL_PAY") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("ADHOC_PAY") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("DA_INDEX") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("DA") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("MWAGE_RATE") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("BANK_NAME") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("BANK_ACCOUNT_NO") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("PF_NO") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("UAN_NO") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("PAN_NO") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("AADHAR_ID") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("EMAIL_ID") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("MOBILE_NO") + "')";
            System.out.println(strSQLLocal);
            stlocal.execute(strSQLLocal);
            rsTmpRemote.next();
        }
        
        rsTmpRemote = stremote.executeQuery(strSQLRemote1);
        rsTmpRemote.first();
        while (!rsTmpRemote.isAfterLast()) {
            strSQLLocal1 = "INSERT INTO SDMLATTPAY.PAYROLL_MTH_SUMMARY_FROM_SPP VALUES(";
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_EMPCODE") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_MONTH") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_YEAR") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_DATE_OF_JOINING") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_DATE_OF_LEAVING") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_PF_NO") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_ESIC_NO") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_PAN_NO") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_BANK_NAME") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_BANK_ACCOUNT_NO") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_DESIGNATION") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_CATEGORY") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_DEPARTMENT") + "',";            
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_DIVISION") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_GRADE") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_SAL_CALENDER_DAYS") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_WEEKLY_OFF") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("GENERAL_HOLIDAY") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("UAN") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("AADHAR") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAID_DAYS") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("PRESENT_DAYS") + "',";            
            strSQLLocal1 += "'"+ rsTmpRemote.getString("PAID_HOLIDAYS") +"',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("BASIC") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("DA") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("HRA") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("CONVEYANCE") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("ELECTRICITY") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("PERSONAL_PAY") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("MAGAZINE_ALW") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("PERFORMANCE_ALW") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("OTHER_ALW") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("AWARD_HRA") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("DA_DIFF") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("REIMBURSEMENT_ALW") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("ADHOC_PAY") + "',";            
            strSQLLocal1 += "'" + rsTmpRemote.getString("WASH_ALW") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("WASH_MTH") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("BASIC_ADJUSTMENT") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("TOTAL_EARNING") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("PF_AMOUNT") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("ESIC_AMOUNT") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("PROF_TAX_AMOUNT") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("GLWF_AMOUNT") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("OTHER_DED_AMOUNT") + "',";            
            strSQLLocal1 += "'" + rsTmpRemote.getString("REVENUE_AMOUNT") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("SOCIAL_PAY_AMOUNT") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("HDFC_PAY_AMOUNT") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("MLWF_AMOUNT") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("LIP_AMOUNT") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("TOTAL_DEDUCTION") + "',";
            strSQLLocal1 += "'" + rsTmpRemote.getString("NET_AMOUNT") + "',";            
            strSQLLocal1 += "'"+ rsTmpRemote.getString("REMARKS")+"')";
            
            System.out.println(strSQLLocal1);
            stlocal.execute(strSQLLocal1);
            rsTmpRemote.next();
                
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
  }
}
