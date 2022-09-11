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
public class CopyTableFromSPP {
    
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
  //String urllocal = "jdbc:mysql://200.0.0.230:3306/";
  String urlremote = "jdbc:mysql://200.0.0.229:3307/";
  String dbName = "SDMLATTPAY";//?zeroDateTimeBehavior=convertToNull
  String driver = "com.mysql.jdbc.Driver";
  String userName = "root"; 
  String password = "att@229";
  String strSQLLocal="",strSQLLocal1="",strSQLRemote="",strSQLRemote1="";
    try {
        Class.forName(driver).newInstance();
        connlocal = DriverManager.getConnection(urllocal + dbName, "root",  EITLERPGLOBAL.DBPassword);
        connremote = DriverManager.getConnection(urlremote + dbName, userName, password);

        //stlocal = connlocal.createStatement();
        //stremote=connremote.createStatement();
        stlocal = connlocal.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        stremote = connremote.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

        stlocal.execute("TRUNCATE TABLE SDMLATTPAY.ATTPAY_EMPMST_FROM_SPP");
        
        //stlocal.execute("DELETE FROM SDMLATTPAY.PAYROLL_MTH_SUMMARY_FROM_SPP WHERE PAY_MONTH="+month+" AND PAY_YEAR="+year+"");
        //stlocal.execute("DELETE FROM SDMLATTPAY.PAYROLL_MTH_SUMMARY_FROM_SPP WHERE PAY_MONTH=12 AND PAY_YEAR=2019");
        //stlocal.execute("DELETE FROM SDMLATTPAY.PAYROLL_MTH_SUMMARY_FROM_SPP WHERE PAY_MONTH IN (SELECT CONCAT(MONTH(CURDATE()),',',MONTH(DATE_SUB(CURDATE(),INTERVAL 1 MONTH))) FROM DUAL) AND PAY_YEAR IN (SELECT CONCAT(YEAR(CURDATE()),',',YEAR(DATE_SUB(CURDATE(),INTERVAL 1 MONTH))) FROM DUAL)");
        stlocal.execute("DELETE FROM SDMLATTPAY.PAYROLL_MTH_SUMMARY_FROM_SPP WHERE PAY_YEAR IN (SELECT YEAR(CURDATE()) FROM DUAL) AND PAY_MONTH IN (SELECT MONTH(DATE_SUB(CURDATE(),INTERVAL 1 MONTH)) FROM DUAL)");
        
        strSQLRemote += "SELECT * FROM SDMLATTPAY.ATTPAY_EMPMST_FROM_SPP ";
        //strSQLRemote1 += "SELECT * FROM SDMLATTPAY.PAYROLL_MTH_SUMMARY_FROM_SPP WHERE PAY_MONTH=12 AND PAY_YEAR=2019";
        //strSQLRemote1 += "SELECT * FROM SDMLATTPAY.PAYROLL_MTH_SUMMARY_FROM_SPP WHERE PAY_MONTH IN (SELECT CONCAT(MONTH(CURDATE()),',',MONTH(DATE_SUB(CURDATE(),INTERVAL 1 MONTH))) FROM DUAL) AND PAY_YEAR IN (SELECT CONCAT(YEAR(CURDATE()),',',YEAR(DATE_SUB(CURDATE(),INTERVAL 1 MONTH))) FROM DUAL)";
        //strSQLRemote1 += "SELECT * FROM SDMLATTPAY.PAYROLL_MTH_SUMMARY_FROM_SPP WHERE PAY_MONTH IN (1,2,3,4,5) AND PAY_YEAR IN (2019,2020)";
        //strSQLRemote1 += "SELECT * FROM SDMLATTPAY.PAYROLL_MTH_SUMMARY_FROM_SPP WHERE PAY_MONTH IN (8,7) AND PAY_YEAR IN (2020,2020)";
        strSQLRemote1+="SELECT * FROM SDMLATTPAY.PAYROLL_MTH_SUMMARY_FROM_SPP WHERE PAY_YEAR IN (SELECT YEAR(DATE_SUB(CURDATE(),INTERVAL 1 MONTH)) FROM DUAL) AND PAY_MONTH IN (SELECT MONTH(DATE_SUB(CURDATE(),INTERVAL 1 MONTH)) FROM DUAL)";
        
        //Copy table
        System.out.println("SQL:" + strSQLRemote);
        rsTmpRemote = stremote.executeQuery(strSQLRemote);
        rsTmpRemote.first();
        while (!rsTmpRemote.isAfterLast()) {
            /*strSQLLocal = "INSERT INTO SDMLATTPAY.ATTPAY_EMPMST_FROM_SPP(PAY_EMPID,EMP_NAME,FATHER_NAME,MOTHER_NAME,SEX,DESIGNATION,DATE_OF_BIRTH,DATE_OF_JOINING,DATE_OF_LEAVING,REASON_FOR_LEAVING,DEPARTMENT,CATEGORY,"
                    + "PERMANENT_ADDRESS,"
                    + "PRESENT_ADDRESS,"
                    + "BASIC_PAY,PERSONAL_PAY,ADHOC_PAY,DA_INDEX,DA,RETAINER_FEE,MWAGE_RATE,BANK_NAME,BANK_ACCOUNT_NO,PF_NO,UAN_NO,PAN_NO,AADHAR_ID,EMAIL_ID,MOBILE_NO) VALUES(";
            */
            strSQLLocal = "INSERT INTO SDMLATTPAY.ATTPAY_EMPMST_FROM_SPP VALUES(";
            strSQLLocal += "'" + rsTmpRemote.getString("PAY_EMPID") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("EMP_NAME") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("FATHER_NAME") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("MOTHER_NAME") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("SEX") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("GRADE") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("DESIGNATION") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("DATE_OF_BIRTH") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("DATE_OF_JOINING") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("DATE_OF_LEAVING") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("REASON_FOR_LEAVING") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("DEPARTMENT") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("CATEGORY") + "', ";
            strSQLLocal += "'"+ rsTmpRemote.getString("PERMANENT_ADDRESS").replace("'","''") + "',";  //.replace("'", "\'")
            strSQLLocal += "'" + rsTmpRemote.getString("PRESENT_ADDRESS").replace("'","''" ) + "',";            
            strSQLLocal += "'" + rsTmpRemote.getString("BANK_NAME") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("BANK_ACCOUNT_NO") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("PF_NO") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("UAN_NO") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("PAN_NO") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("AADHAR_ID") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("EMAIL_ID") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("MOBILE_NO") + "',";            
            strSQLLocal += "'" + rsTmpRemote.getString("BASIC_PAY") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("RETAINER_FEE") + "',";                
            strSQLLocal += "'" + rsTmpRemote.getString("PERSONAL_PAY") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("ADHOC_PAY") + "',";
            strSQLLocal += "'" + rsTmpRemote.getString("DA") + "',";                 
            strSQLLocal += "'" + rsTmpRemote.getString("DA_INDEX") + "',";                        
            strSQLLocal += "'" + rsTmpRemote.getString("MWAGE_RATE") + "',";            
                strSQLLocal += "'" + rsTmpRemote.getString("HRA") + "',";
                strSQLLocal += "'" + rsTmpRemote.getString("CONVEYANCE") + "',";
                strSQLLocal += "'" + rsTmpRemote.getString("ELECTRICITY") + "',";
                strSQLLocal += "'" + rsTmpRemote.getString("MAGAZINE") + "',";
                strSQLLocal += "'" + rsTmpRemote.getString("PERFORMANCE_ALW") + "',";
                strSQLLocal += "'" + rsTmpRemote.getString("WASH_ALW") + "',";
                strSQLLocal += "'" + rsTmpRemote.getString("AWARD_HRA") + "',";
                strSQLLocal += "'" + rsTmpRemote.getString("WASH_MONTH") + "',";
                strSQLLocal += "'" + rsTmpRemote.getString("REIM_PERCENT") + "',";
                strSQLLocal += "'" + rsTmpRemote.getString("FURNISHING") + "',";
                strSQLLocal += "'" + rsTmpRemote.getString("PETROL_LITER") + "',";
                strSQLLocal += "'" + rsTmpRemote.getString("SHIFT_ALW") + "',";
                strSQLLocal += "'" + rsTmpRemote.getString("ATD_ALW") + "',";
                strSQLLocal += "'" + rsTmpRemote.getString("PRE_FURNISHING") + "',";
                strSQLLocal += "'" + rsTmpRemote.getString("MED_EDU_HOT_AMT") + "',";
                strSQLLocal += "'" + rsTmpRemote.getString("BONUS_GROSSING") + "',";
                strSQLLocal += "'" + rsTmpRemote.getString("DOC_GRATUITY") + "')";
            System.out.println(strSQLLocal);
            stlocal.execute(strSQLLocal);
            rsTmpRemote.next();
        }
        //data.Execute("", "jdbc:mysql://200.0.0.230:3306/");
        System.out.println("SQL:" + strSQLRemote1);
        rsTmpRemote = stremote.executeQuery(strSQLRemote1);
        rsTmpRemote.first();
        while (!rsTmpRemote.isAfterLast()) {
            strSQLLocal1 = "INSERT INTO SDMLATTPAY.PAYROLL_MTH_SUMMARY_FROM_SPP VALUES(";            
            //strSQLLocal1 = "INSERT INTO SDMLATTPAY.PAYROLL_MTH_SUMMARY_FROM_SPP(PAY_EMPCODE,PAY_MONTH,PAY_YEAR,PAY_DATE_OF_JOINING,PAY_DATE_OF_LEAVING,PAY_PF_NO,PAY_ESIC_NO,PAY_PAN_NO,PAY_BANK_NAME,PAY_BANK_ACCOUNT_NO,PAY_DESIGNATION,PAY_CATEGORY,PAY_DEPARTMENT,PAY_DIVISION,PAY_GRADE,PAY_SAL_CALENDER_DAYS,PAY_WEEKLY_OFF,TDS,UAN,AADHAR,PAID_DAYS,PRESENT_DAYS,PAID_HOLIDAYS,BASIC,DA,HRA,CONVEYANCE,ELECTRICITY,PERSONAL_PAY,MAGAZINE_ALW,PERFORMANCE_ALW,OTHER_ALW,AWARD_HRA,DA_DIFF,MED_EDU_HOT_AMT,ADHOC_PAY,WASH_ALW,WASH_MTH,SALARY_ADJUSTMENT,TOTAL_EARNING,PF_AMOUNT,ESIC_AMOUNT,PROF_TAX_AMOUNT,GLWF_AMOUNT,OTHER_DED_AMOUNT,REVENUE_AMOUNT,SOCIAL_PAY_AMOUNT,HDFC_PAY_AMOUNT,MLWF_AMOUNT,LIP_AMOUNT,TOTAL_DEDUCTION,NET_AMOUNT,REMARKS,RETAINER_FEE) VALUES(";
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
                //strSQLLocal1 += "'" + rsTmpRemote.getString("TDS") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("UAN") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("AADHAR") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("PAID_DAYS") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("PRESENT_DAYS") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("PAID_HOLIDAYS") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("BASIC") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("RETAINER_FEE") + "',";
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
                strSQLLocal1 += "'" + rsTmpRemote.getString("MED_EDU_HOT_AMT") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("ADHOC_PAY") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("WASH_ALW") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("WASH_MTH") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("SALARY_ADJUSTMENT") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("BASIC_SAL_ADJ") + "',";
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
                strSQLLocal1 += "'" + rsTmpRemote.getString("TDS") + "',";                
                strSQLLocal1 += "'" + rsTmpRemote.getString("COVID_19_PAY_CUT") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("UNION_FEE") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("DA_RECOVERY") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("RENT") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("TOTAL_DEDUCTION") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("NET_AMOUNT") + "',";                
                strSQLLocal1 += "'" + rsTmpRemote.getString("REMARKS") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("DA_INDEX") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("SHIFT_ALW") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("ATD_ALW") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("OTH_NONPF") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("OTHER_EARN") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("FURNISHING") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("PETROL_ALLOWANCE") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("PETROL_LITER") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("PETROL_PRICE") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("GROSS_SALARY") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("PREV_GROSS") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("COFF_ENCASH") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("CONV_DAYS") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("PR_ROUND") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("PREV_COMPANY_DA") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("PREV_DIFFERENCE_DA") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("DA_DIFF_AMT") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("DA_DIFF_MUL_DAY") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("A_HRA_DIFF") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("PRE_FURNISH") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("MWAGE_RATE") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("TOTAL_BONUS") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("BONUS_RATE") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("DA_DIFFERENCE_AMT") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("BONUS_GROSSING") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("BONUS_WAGES") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("REIM_CLAIM") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("REIM_TDS") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("BASIC_REIM_ADJUST") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("PH_BONUS") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("PH_AVAILED") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("PL_ESIC") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("TEST1") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("TEST") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("REIM_PERCENT") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("PAID_H") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("DA_AMOUNT") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("RENT_DEDUCTION") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("FINE") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("CANTEEN") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("ELECT") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("SOCIETY_DED") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("SOCIETY_MASTER_AMOUNT") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("HDFC_MASTER_AMOUNT") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("MOBILE_CHARGES") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("LIP_MASTER_AMOUNT") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("UNION_LEVY") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("STATUTORY_DEDUCTION") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("NON_PH") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("BONUS_DAY") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("CON_TRAINING_RATE") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("CON_ACTUAL_RATE") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("CON_BONUS_GROSS") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("CON_BASIC_RATE") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("TDS_RETAINER") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("ARREAR_MEDICAL") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("ARREAR_AMOUNT") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("GRATUITY_CALC") + "',";
                strSQLLocal1 += "'" + rsTmpRemote.getString("GRATUITY_MANUAL") + "')";
            /*strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_EMPCODE") + "',";   //1
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_MONTH") + "',";     //2
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_YEAR") + "',";      //3
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_DATE_OF_JOINING") + "',";  //4
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_DATE_OF_LEAVING") + "',";  //5
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_PF_NO") + "',";  //6
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_ESIC_NO") + "',";  //7
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_PAN_NO") + "',";  //8
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_BANK_NAME") + "',";  //9
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_BANK_ACCOUNT_NO") + "',";  //10
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_DESIGNATION") + "',";  //11
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_CATEGORY") + "',";  //12
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_DEPARTMENT") + "',";   //13          
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_DIVISION") + "',";  //14
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_GRADE") + "',";  //15
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_SAL_CALENDER_DAYS") + "',";  //16
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAY_WEEKLY_OFF") + "',";   //17
            //strSQLLocal1 += "'" + rsTmpRemote.getString("GENERAL_HOLIDAY") + "',"; //TDC
            strSQLLocal1 += "'" + rsTmpRemote.getString("TDS") + "',"; //18//TDC 
            strSQLLocal1 += "'" + rsTmpRemote.getString("UAN") + "',";  //19
            strSQLLocal1 += "'" + rsTmpRemote.getString("AADHAR") + "',";  //22
            strSQLLocal1 += "'" + rsTmpRemote.getString("PAID_DAYS") + "',";  //21
            strSQLLocal1 += "'" + rsTmpRemote.getString("PRESENT_DAYS") + "',";   //22         
            strSQLLocal1 += "'"+ rsTmpRemote.getString("PAID_HOLIDAYS") +"',";  //23
            strSQLLocal1 += "'" + rsTmpRemote.getString("BASIC") + "',";  //24
            strSQLLocal1 += "'" + rsTmpRemote.getString("DA") + "',";  //25
            strSQLLocal1 += "'" + rsTmpRemote.getString("HRA") + "',";  //26
            strSQLLocal1 += "'" + rsTmpRemote.getString("CONVEYANCE") + "',";  //27
            strSQLLocal1 += "'" + rsTmpRemote.getString("ELECTRICITY") + "',";  //28
            strSQLLocal1 += "'" + rsTmpRemote.getString("PERSONAL_PAY") + "',";  //29
            strSQLLocal1 += "'" + rsTmpRemote.getString("MAGAZINE_ALW") + "',";  //30
            strSQLLocal1 += "'" + rsTmpRemote.getString("PERFORMANCE_ALW") + "',";  //31
            strSQLLocal1 += "'" + rsTmpRemote.getString("OTHER_ALW") + "',";  //32
            strSQLLocal1 += "'" + rsTmpRemote.getString("AWARD_HRA") + "',";  //33
            strSQLLocal1 += "'" + rsTmpRemote.getString("DA_DIFF") + "',";  //34
            //strSQLLocal1 += "'" + rsTmpRemote.getString("REIMBURSEMENT_ALW") + "',"; //MED_EDU_HOT_AMT
            strSQLLocal1 += "'" + rsTmpRemote.getString("MED_EDU_HOT_AMT") + "',"; //35//MED_EDU_HOT_AMT
            strSQLLocal1 += "'" + rsTmpRemote.getString("ADHOC_PAY") + "',";     //36       
            strSQLLocal1 += "'" + rsTmpRemote.getString("WASH_ALW") + "',";  //37
            strSQLLocal1 += "'" + rsTmpRemote.getString("WASH_MTH") + "',";   //38
            //strSQLLocal1 += "'" + rsTmpRemote.getString("BASIC_ADJUSTMENT") + "',"; //SALARY_ADJUSTMENT
            strSQLLocal1 += "'" + rsTmpRemote.getString("SALARY_ADJUSTMENT") + "',"; //39 //SALARY_ADJUSTMENT
            strSQLLocal1 += "'" + rsTmpRemote.getString("TOTAL_EARNING") + "',";  //40
            strSQLLocal1 += "'" + rsTmpRemote.getString("PF_AMOUNT") + "',";  //41
            strSQLLocal1 += "'" + rsTmpRemote.getString("ESIC_AMOUNT") + "',";  //42
            strSQLLocal1 += "'" + rsTmpRemote.getString("PROF_TAX_AMOUNT") + "',";  //43
            strSQLLocal1 += "'" + rsTmpRemote.getString("GLWF_AMOUNT") + "',";  //44
            strSQLLocal1 += "'" + rsTmpRemote.getString("OTHER_DED_AMOUNT") + "',";  //45           
            strSQLLocal1 += "'" + rsTmpRemote.getString("REVENUE_AMOUNT") + "',";  //46
            strSQLLocal1 += "'" + rsTmpRemote.getString("SOCIAL_PAY_AMOUNT") + "',";  //47
            strSQLLocal1 += "'" + rsTmpRemote.getString("HDFC_PAY_AMOUNT") + "',";  //48
            strSQLLocal1 += "'" + rsTmpRemote.getString("MLWF_AMOUNT") + "',";   //49
            strSQLLocal1 += "'" + rsTmpRemote.getString("LIP_AMOUNT") + "',";    //50
            strSQLLocal1 += "'" + rsTmpRemote.getString("TOTAL_DEDUCTION") + "',";  //51
            strSQLLocal1 += "'" + rsTmpRemote.getString("NET_AMOUNT") + "',";             //52 
            strSQLLocal1 += "'"+ rsTmpRemote.getString("REMARKS")+"',";   //53
            strSQLLocal1 += "'"+ rsTmpRemote.getString("RETAINER_FEE")+"')";  //54
            */
            System.out.println(strSQLLocal1);
            stlocal.execute(strSQLLocal1);
            rsTmpRemote.next();
                
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
  }
}
