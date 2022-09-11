/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.Utils;

import EITLERP.EITLERPGLOBAL; 
import EITLERP.data;
import java.sql.ResultSet;
import static EITLERP.FeltSales.common.JavaMail.SendMail;
/**
 *
 * @author root
 */
public class clsBirthdayMailNotification {
    
    public static void main(String[] args) {
        String pBody = "", pSubject = "", recievers = "", pcc = "";
        int mbirthday = data.getIntValueFromDB("SELECT COUNT(*) FROM SDMLATTPAY.ATTPAY_EMPMST WHERE DAY(EMP_BIRTH_DATE)=DAY(CURDATE()) "
                + "AND MONTH(EMP_BIRTH_DATE)=MONTH(CURDATE()) AND EMP_LEFT_DATE='0000-00-00'  " 
                + "AND EMP_MAIN_CATEGORY IN(2,5,9) ");
        if (mbirthday > 0) {            
            pSubject = "Birthday for "+data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(CURDATE(),'%D'),' ',UCASE(MONTHNAME(CURDATE()))) FROM DUAL");
            pBody="<p><strong><u><span style=\"font-size:13px;font-family:'times new roman' , 'serif';color:red;\">BIRTHDAY FOR TODAY</span></u></strong><span style=\"font-size:16px;font-family:'times new roman' , 'serif';color:black;\">&nbsp;</span></p>";
            
            pBody += "<p style=\"margin-bottom:0.0001pt;\"><strong><span style=\"font-size:13px;font-family:'arial' , 'sans-serif';\">"+data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(CURDATE(),'%D'),' ',UCASE(MONTHNAME(CURDATE()))) FROM DUAL")+"</span></strong></p>";
            //pBody +="<p style=\"margin-bottom:0.0001pt;\"><strong><span style=\"font-size:13px;font-family:'arial' , 'sans-serif';\">";
            pBody +="<p style=\"margin-bottom:0.0001pt;\">";
            try {
                
                ResultSet rs = data.getResult("SELECT CONCAT(EMP_NAME,'(',ERP_DEPT,')') EMPNAME FROM SDMLATTPAY.ATTPAY_EMPMST M "
                        + "LEFT JOIN SDMLATTPAY.ATT_DEPARTMENT_MASTER D "
                        + "ON DPTID=EMP_DEPARTMENT "
                        + "WHERE DAY(EMP_BIRTH_DATE)=DAY(CURDATE()) "   //CURDATE()
                        + "AND MONTH(EMP_BIRTH_DATE)=MONTH(CURDATE()) AND EMP_LEFT_DATE='0000-00-00'  " //CURDATE()
                        + "AND EMP_MAIN_CATEGORY IN(2,5,9) "
                        + "ORDER BY PAY_EMP_NO");
                rs.first();
                if (rs.getRow() > 0) {
                    while (!rs.isAfterLast()) {     
                        //pBody += rs.getString("EMP_NAME");
                        pBody +="<strong><span style=\"font-size:13px;font-family:'arial' , 'sans-serif';\">"+rs.getString("EMPNAME")+"</span></strong><br/>";
                        //pBody+="+rs.getString(\"EMP_NAME\")+";
                        //pBody+="<br>";                        
                        rs.next();
                    }                                        
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            pBody+="</p>";
            pBody += "<p><strong><em><span style=\"font-size:13px;font-family:'arial' , 'sans-serif';color:#00b050;\">DINESH FAMILY WISHES YOU LOT OF JOY, SUCCESS AND LUCK FOR THE YEAR AHEAD</span></em></strong><span style=\"font-size:13px;font-family:'arial' , 'sans-serif';\">&nbsp;</span></p>\n"
                    + "<p>&nbsp;</p>"                    
                    + "<p><span style=\"font-size:13px;font-family:'tahoma' , 'sans-serif';color:black;\">Thanks and Regards,</span></p>"                    
                    + "<p><strong><span style=\"font-size:13px;font-family:'tahoma' , 'sans-serif';\">Neelanjan Sarkar</span></strong></p>"
                    + "<p><span style=\"font-size:13px;font-family:'tahoma' , 'sans-serif';color:#17365d;\">Manager - Human Resources"
                    + "<br>Shri Dinesh Mills Ltd. Akota Padra Road, Baroda 390020"
                    + "<br>Phone:&nbsp;91 0265 2330060/61/62/63/64/65; Extn: 244<br>URL:</span><span style=\"font-size:13px;font-family:'tahoma' , 'sans-serif';color:#1f497d;\">&nbsp;<a href=\"http://www.dineshmills.com/\" rel=\"nofollow noopener noreferrer\" target=\"_blank\">www.dineshmills.com</a></span></p>";
            
            //recievers = "ashutosh@dineshmills.com";
            recievers = "bharat@dineshmills.com,nimish@dineshmills.com,aditya@dineshmills.com";
            pcc="sunil@dineshmills.com,neelanjan@dineshmills.com,sdmlerp@dineshmills.com";
            //String CC = "aditya@dineshmills.com,abtewary@dineshmills.com,yrpatel@dineshmills.com,amitkanti@dineshmills.com,raghavendra@dineshmills.com";
            //String responce = DailyMailNotification.sendNotificationMail(pSubject, pMessage, recievers, CC);
            try {
                SendMail(recievers, pBody, pSubject, pcc);
            } catch (Exception e) {
                System.out.println("Error Msg " + e.getMessage());
                e.printStackTrace();
            }
        }else{
            System.out.println("No birthday on "+data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(CURDATE(),'%D'),' ',UCASE(MONTHNAME(CURDATE()))) FROM DUAL"));
        }
        
        
        
    }
    
}
