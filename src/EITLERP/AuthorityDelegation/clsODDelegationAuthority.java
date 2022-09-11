/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.AuthorityDelegation;

import EITLERP.EITLERPGLOBAL;
import EITLERP.Finance.FinanceGlobal;
import EITLERP.Finance.UtilFunctions;
import EITLERP.Finance.clsVoucher;
import SDMLATTPAY.Data.*;
import EITLERP.data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Dharmendra
 */
public class clsODDelegationAuthority {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      try {           
            data.Execute("DELETE FROM DINESHMILLS.D_COM_AUTHORITY_AUTO WHERE TYPE='OD'");
           /*String strSQL="SELECT DISTINCT EMPID FROM SDMLATTPAY.ATT_DATA_DAILY_SUMMARY WHERE PUNCHDATE=curdate() -INTERVAL 70 DAY  \n"
                   + "AND ARR_TIME+INTERVAL 2 HOUR>=INTIME AND INTIME='0000-00-00'\n"
                   + " AND SUBSTR(EMPID,1,5) IN ('BRD10','BRD20','BRD50','BRD60')";
            */
            String strSQL="",strSQL1="",strSQLNOM1="",strSQLNOM2="",strSQLNOM3="";
            strSQL = "SELECT A.LVT_PAY_EMPID AS EMPID,COALESCE(USER_ID,0) AS USERID FROM\n"
                  + " (SELECT * FROM SDMLATTPAY.ATT_LEAVE_TRN WHERE LVT_LEAVE_CODE IN ('OD','CL','PL','SL') AND CURDATE() BETWEEN LVT_FROMDATE AND LVT_TODATE\n"
                  + " AND SUBSTR(LVT_PAY_EMPID,1,5) IN ('BRD10','BRD20','BRD50','BRD60') \n"
                  + " AND LVT_PAY_EMPID NOT IN ('BRD100002','BRD100003','BRD101485','BRD101864') ) AS A\n"
                  + "  LEFT JOIN\n"
                  + " (SELECT USER_ID,ATTPAY_EMPCODE FROM DINESHMILLS.D_COM_USER_MASTER) B\n"
                  + " ON A.LVT_PAY_EMPID=B.ATTPAY_EMPCODE ";
            
            ResultSet rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    String EmpNo=rsTmp.getString("EMPID");
                    int userID=rsTmp.getInt("USERID");                   
                                       
                    
                    if(userID!=0) {                        
                        ResultSet rsAutoDel= null;                       
                        
                            strSQL1 = "SELECT * FROM DINESHMILLS.D_COM_DELEGATION_AUTHORITY WHERE AUTH_USER_ID="+userID+"";
                            rsAutoDel = data.getResult(strSQL1);
                        
                        rsAutoDel.first();
                        if(rsAutoDel.getRow() > 0 ) {
                            while(!rsAutoDel.isAfterLast()) {
                                
                                int moduleNo = rsAutoDel.getInt("AUTH_MODULE_ID");
                                int nom1No = rsAutoDel.getInt("AUTH_NOMINEE1_ID");
                                int nom2No = rsAutoDel.getInt("AUTH_NOMINEE2_ID");
                                int nom3No = rsAutoDel.getInt("AUTH_NOMINEE3_ID");
                                
                                //if(data.IsRecordExist("SELECT * FROM DINESHMILLS.D_COM_AUTHORITY_AUTO WHERE USER_ID="+userID+" AND MODULE_ID="+moduleNo+"")){
                                //   data.Execute("DELETE FROM DINESHMILLS.D_COM_AUTHORITY_AUTO WHERE USER_ID="+userID+" AND MODULE_ID="+moduleNo+"");
                                //}
//                                if(data.IsRecordExist("SELECT * FROM DINESHMILLS.D_COM_AUTHORITY_AUTO WHERE AUTHORITY_USER_ID="+userID+" AND MODULE_ID="+moduleNo+"")){
//                                   data.Execute("DELETE FROM DINESHMILLS.D_COM_AUTHORITY_AUTO WHERE AUTHORITY_USER_ID="+userID+" AND MODULE_ID="+moduleNo+"");
//                                }
                                
                                if(nom1No!=0) {
                                    strSQLNOM1 = "INSERT INTO DINESHMILLS.D_COM_AUTHORITY_AUTO "
                                            + " VALUES (2," + userID + ","
                                            + "'" + EITLERPGLOBAL.getCurrentDateDB() + "',"
                                            + "'" + EITLERPGLOBAL.getCurrentDateDB() + "',"
                                            + "" + nom1No + ","
                                            + "" + moduleNo + ","
                                            + " 1,"
                                            + "'" + EITLERPGLOBAL.getCurrentDateDB() + "','',"
                                            + "'OD')";
                                    data.Execute(strSQLNOM1);
                                } 
                                if(nom2No!=0) {
                                    strSQLNOM2 = "INSERT INTO DINESHMILLS.D_COM_AUTHORITY_AUTO "
                                            + " VALUES (2," + userID + ","
                                            + "'" + EITLERPGLOBAL.getCurrentDateDB() + "',"
                                            + "'" + EITLERPGLOBAL.getCurrentDateDB() + "',"
                                            + "" + nom2No + ","
                                            + "" + moduleNo + ","
                                            + " 1,"
                                            + "'" + EITLERPGLOBAL.getCurrentDateDB() + "','',"
                                            + "'OD')";
                                    data.Execute(strSQLNOM2);
                                }
                                if(nom3No!=0) {
                                    strSQLNOM3 = "INSERT INTO DINESHMILLS.D_COM_AUTHORITY_AUTO "
                                            + " VALUES (2," + userID + ","
                                            + "'" + EITLERPGLOBAL.getCurrentDateDB() + "',"
                                            + "'" + EITLERPGLOBAL.getCurrentDateDB() + "',"
                                            + "" + nom3No + ","
                                            + "" + moduleNo + ","
                                            + " 1,"
                                            + "'" + EITLERPGLOBAL.getCurrentDateDB() + "','',"
                                            + "'OD')";
                                    data.Execute(strSQLNOM3);                                                                       
                                }
                                                                 
                                rsAutoDel.next();
                            }
                        }
                    }
                    rsTmp.next();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        
        
    }

}
