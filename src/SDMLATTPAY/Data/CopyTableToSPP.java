/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SDMLATTPAY.Data;

import EITLERP.EITLERPGLOBAL;
import java.sql.*;

/**
 *
 * @author root
 */
public class CopyTableToSPP {
    
public static void main(String[] args) {
    
  //0 - month
        //1 - year
        
        if(args.length<2) {
            System.out.println("Insufficient arguments. Please specify \n 1. Month \n2. Year ");
            return;
        }
        
        int month=Integer.parseInt(args[0]);
        int year=Integer.parseInt(args[1]);  
  System.out.println("Copy data from one database table to another!");
  Connection connlocal = null,connremote = null;
  ResultSet rsTmpLocal,rsTmpRemote;
  Statement stlocal = null, stremote = null;
  String urllocal = "jdbc:mysql://200.0.0.230:3306/";
  String urlremote = "jdbc:mysql://200.0.0.229:3307/";
  String dbName = "SDMLATTPAY";//?zeroDateTimeBehavior=convertToNull
  String driver = "com.mysql.jdbc.Driver";
  String userName = "root"; 
  String password = "att@229";
  String strSQLLocal="",strSQLRemote="",strSQLRemote1="";
    try {
        Class.forName(driver).newInstance();
        connlocal = DriverManager.getConnection(urllocal + dbName, "root", EITLERPGLOBAL.DBPassword);
        connremote = DriverManager.getConnection(urlremote + dbName, userName, password);

  //stlocal = connlocal.createStatement();
        //stremote=connremote.createStatement();
        stlocal = connlocal.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        stremote = connremote.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

        //stremote.execute("TRUNCATE TABLE SDMLATTPAY.ATT_MTH_SUMMARY_227");
        stremote.execute("DELETE FROM SDMLATTPAY.ATT_MTH_SUMMARY_227 WHERE MS_MM="+month+" AND MS_YYYY="+year+"");
        stremote.execute("DELETE FROM SDMLATTPAY.ATT_MTH_SUMMARY_TO_SPP WHERE MS_MM="+month+" AND MS_YYYY="+year+"");
        
        strSQLLocal += "SELECT * FROM SDMLATTPAY.ATT_MTH_SUMMARY WHERE MS_MM="+month+" AND MS_YYYY="+year+" ";
        //Copy table
        System.out.println("SQL:" + strSQLLocal);
        rsTmpLocal = stlocal.executeQuery(strSQLLocal);
        rsTmpLocal.first();
        while (!rsTmpLocal.isAfterLast()) {
            strSQLRemote = "INSERT INTO SDMLATTPAY.ATT_MTH_SUMMARY_227 VALUES(";
            strSQLRemote += "'" + rsTmpLocal.getString("MS_EMPID") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("MS_MM") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("MS_YYYY") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("TOTAL_MONTH_DAYS") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("PAID_DAYS") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("PRESENT_DAYS") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("LC_DAYS") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("LWP_DAYS") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("PL") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("CL") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SL") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("OD") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("LOFF") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("EOFF") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("ESIC") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("WO") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("P_GP") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("O_GP") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("ABST") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("CO") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("PLE") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("CLE") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SLE") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("TPHD") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("PHDL") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("HL") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("DA_DAYS") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT1_F") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT1_S") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT2_F") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT2_S") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT3_F") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT3_S") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT4_F") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT4_S") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT5_F") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT5_S") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT6_F") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT6_S") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT7_F") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT7_S") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT8_F") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT8_S") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT9_F") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT9_S") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT10_F") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT10_S") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT11_F") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT11_S") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT12_F") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT12_S") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT13_F") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT13_S") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT14_F") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT14_S") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT15_F") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT15_S") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT16_F") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT16_S") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT17_F") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT17_S") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT18_F") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT18_S") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT19_F") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT19_S") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT20_F") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT20_S") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT21_F") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT21_S") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT22_F") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT22_S") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT23_F") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT23_S") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT24_F") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT24_S") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT25_F") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT25_S") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT26_F") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT26_S") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT27_F") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT27_S") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT28_F") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT28_S") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT29_F") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT29_S") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT30_F") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT30_S") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT31_F") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("SFT31_S") + "',";

            strSQLRemote += "'" + rsTmpLocal.getString("WOFF") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("NPH") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("NWOF") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("COFFE") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("NPL") + "',";
            if(rsTmpLocal.getString("MS_PETROL_LTRS")== null){
                strSQLRemote += "'" + 0.00 + "',";
            }else{
                strSQLRemote += "'" + rsTmpLocal.getString("MS_PETROL_LTRS") + "',";
            }
            strSQLRemote += "'" + rsTmpLocal.getString("MS_CATEGORY") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("MS_MAIN_CATEGORY") + "')";
            System.out.println(strSQLRemote);
            stremote.execute(strSQLRemote);
            
            
            strSQLRemote1 = "INSERT INTO SDMLATTPAY.ATT_MTH_SUMMARY_TO_SPP VALUES(";
            strSQLRemote1 += "'" + rsTmpLocal.getString("MS_MM") + "',";
            strSQLRemote1 += "'" + rsTmpLocal.getString("MS_YYYY") + "',";
            strSQLRemote1 += "'" + rsTmpLocal.getString("MS_EMPID") + "',";
            strSQLRemote1 += "'" + rsTmpLocal.getString("TOTAL_MONTH_DAYS") + "',";
            strSQLRemote1 += "'" + rsTmpLocal.getString("PAY_DAYS") + "',";
            strSQLRemote1 += "'" + rsTmpLocal.getString("PRESENT_DAYS") + "',";
            strSQLRemote1 += "'" + rsTmpLocal.getString("LWP_DAYS")+rsTmpLocal.getString("LC_DAYS") + "',";
            strSQLRemote1 += "'" + rsTmpLocal.getString("CO") + "',";
            strSQLRemote1 += "'" + 0 + "',";
            strSQLRemote1 += "'" + rsTmpLocal.getString("OD") + "',";
            strSQLRemote1 += "'" + rsTmpLocal.getString("PL") + "',";
            strSQLRemote1 += "'" + rsTmpLocal.getString("CL") + "',";
            strSQLRemote1 += "'" + rsTmpLocal.getString("SL") + "',";            
            strSQLRemote1 += "'" + rsTmpLocal.getString("LOFF") + "',";
            strSQLRemote1 += "'" + rsTmpLocal.getString("EOFF") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("HL") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("WOFF") + "',";
            strSQLRemote1 += "'" + rsTmpLocal.getString("WO") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("HL") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("NWOF") + "',";
            strSQLRemote += "'" + rsTmpLocal.getString("NPL") + "',";
            strSQLRemote1 += "'" + rsTmpLocal.getString("ESIC") + "',";            
            strSQLRemote1 += "'')";
            //strSQLRemote += "'" + rsTmpLocal.getString("NPH") + "',";            
            //strSQLRemote += "'" + rsTmpLocal.getString("COFFE") + "',";                       

            System.out.println(strSQLRemote1);
            stremote.execute(strSQLRemote1);
            rsTmpLocal.next();
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
  }
}
