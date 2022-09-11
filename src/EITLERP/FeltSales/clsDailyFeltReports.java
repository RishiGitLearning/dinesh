/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales;

import EITLERP.EITLERPGLOBAL;
import EITLERP.data;
import java.sql.*;
import java.util.*;

/**
 *
 * @author root
 */
public class clsDailyFeltReports {
    
    List fromMon = new ArrayList();
    List toMon = new ArrayList();


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
//        Notification();
        ZonewiseProductwiseNotification();
        ProductwiseZonewiseNotification();
    }
    
    private static void Notification() {
            try {

                String pSubject = "Notification : Felt Sales Daily Report Testing";
                String pMessage = "Hi, <br> <br> Testing of auto sending mails from server";
                String cc = "gaurang@dineshmills.com";
                String prodPcHeader = "";

                pMessage = pMessage + "<table border='1'>"
                        + "<tr>"
                        + "<th align='center'> Col 1 </th>"
                        + "<th align='center'> Col 2 </th>"
                        + "<th align='center'> Col 3 </th>"
                        + "<th align='center'> Col 4 </th>"
                        + "<th align='center'> Col 5 </th>"
                        + "</tr>";

                       pMessage = pMessage + ""
                                + "<tr>"
                                + "<td align='center'> Data </td>"
                                + "<td align='center'> Data </td>"
                                + "<td align='center'> Data </td>"
                                + "<td align='center'> Data </td>"
                                + "<td align='center'> Data </td>"
                                + "</tr>";
                
                pMessage = pMessage + "</table>";

                String recievers = "sdmlerp@dineshmills.com";

                
                pMessage = pMessage + "<br><br>**** This is an auto-generated email from TEST SERVER, please do not reply ****"; //For Test

                System.out.println("Recivers : " + recievers);
                System.out.println("pSubject : " + pSubject);
                System.out.println("pMessage : " + pMessage);

                String responce = DailyMailNotification.sendNotificationMail(pSubject, pMessage, recievers, cc);
                System.out.println("Send Mail Responce : " + responce);

            } catch (Exception e) {
                System.out.println("Error on Mail: " + e.getMessage());
            }
        
    }
    
    private static void ZonewiseProductwiseNotification() {
            try {

                String pSubject = "Zonewise Productwise Felt Sales Daily Dispatch Report";
                String pMessage = "";
                String cc = "gaurang@dineshmills.com,rishineekhra@dineshmills.com,anandkumar.pandya@dineshmills.com";
                String strSQL = "";
                int cnt;
                int cntTotal;
                
                String curDate = data.getStringValueFromDB("SELECT DATE_FORMAT(DATE_SUB(CURDATE(),INTERVAL 1 DAY),\"%d %b %Y\")");
                int curMon = EITLERPGLOBAL.getCurrentMonth();
                int curYr = EITLERPGLOBAL.getCurrentYear();
                
                if (curMon<4) {
                    cnt = curMon+8;
                } else {
                    cnt = curMon-4;
                }
                
                ResultSet rs;
                
                pMessage = pMessage + "<br>Dear All,<br><br>Zonewise Productwise Felt Sales Daily Dispatch Report Details as given below :<br>";

                Connection Conn1;
                Statement stmt1;
                ResultSet rsData1;

                Conn1 = data.getConn();
                stmt1 = Conn1.createStatement();
                rsData1 = stmt1.executeQuery("select DISTINCT YR,LEFT(UPPER(MNTHNM),3) AS MNTHNM,MNTH FROM "
                        + "(select SELECTED_DATE,DAYNAME(SELECTED_DATE) AS ADAY,MONTH(SELECTED_DATE) AS MNTH,MONTHNAME(SELECTED_DATE) AS MNTHNM,YEAR(SELECTED_DATE) AS YR "
                        + "from (select adddate('1970-01-01',t4*10000 + t3*1000 + t2*100 + t1*10 + t0) selected_date "
                        + "from (select 0 t0 union select 1 union select 2 union select 3 union select 4 "
                        + "union select 5 union select 6 union select 7 union select 8 union select 9) t0, "
                        + "(select 0 t1 union select 1 union select 2 union select 3 union select 4 "
                        + "union select 5 union select 6 union select 7 union select 8 union select 9) t1, "
                        + "(select 0 t2 union select 1 union select 2 union select 3 "
                        + "union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t2, "
                        + "(select 0 t3 union select 1 union select 2 union select 3 union select 4 "
                        + "union select 5 union select 6 union select 7 union select 8 union select 9) t3, "
                        + "(select 0 t4 union select 1 union select 2 union select 3 "
                        + "union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t4) v "
                        + "where selected_date between CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01') and CURDATE()) AS Z ");
                                
                strSQL += "SELECT P.INCHARGE_CD, I.INCHARGE_NAME,H.PRODUCT_CODE,Q.GROUP_NAME, ";
                strSQL += "SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW() ,INTERVAL 1 DAY),1,10) THEN 1 ELSE 0 END) AS  DAYCNT , ";
                strSQL += "SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW() ,INTERVAL 1 DAY),1,10) THEN INVOICE_AMT ELSE 0 END) AS  DAYINV , ";
                
                rsData1.first();
                if (rsData1.getRow() > 0) {
                    while (!rsData1.isAfterLast()) {
                        
                        strSQL += "SUM(CASE WHEN EXTRACT( MONTH FROM INVOICE_DATE) = "+rsData1.getString("MNTH")+" THEN 1 ELSE 0 END) AS  "+rsData1.getString("MNTHNM")+"CNT , ";
                        strSQL += "SUM(CASE WHEN EXTRACT( MONTH FROM INVOICE_DATE) = "+rsData1.getString("MNTH")+" THEN INVOICE_AMT ELSE 0 END) AS  "+rsData1.getString("MNTHNM")+"INV , ";
                    
                        rsData1.next();
                    }
                } 
                
                strSQL += "COUNT(INVOICE_NO) AS TOTALCNT, ";
                strSQL += "SUM(INVOICE_AMT) AS TOTAL_INVAMT, ";
//                strSQL += "ROUND((SUM(INVOICE_AMT)/"+(cnt+1)+")*12,0) AS PRORATA_INV_AMT ";
                strSQL += "ROUND(SUM(INVOICE_AMT)/(DATEDIFF(CURDATE() ,CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01')))* ((DATEDIFF(CONCAT(CASE WHEN MONTH(CURDATE())>3 THEN (YEAR(CURDATE())+1) ELSE YEAR(CURDATE()) END,'-03-31'),CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01'))+1) -DATEDIFF(CURDATE() ,CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01'))),0) AS PRORATA_AMT1, ";
                strSQL += "ROUND(SUM(INVOICE_AMT)/(DATEDIFF(CURDATE() ,CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01')))* ((DATEDIFF(CONCAT(CASE WHEN MONTH(CURDATE())>3 THEN (YEAR(CURDATE())+1) ELSE YEAR(CURDATE()) END,'-03-31'),CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01'))+1) -DATEDIFF(CURDATE() ,CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01'))),0)+SUM(INVOICE_AMT) AS PRO_INV2 ";
                strSQL += "FROM ";
                strSQL += "PRODUCTION.FELT_SAL_INVOICE_HEADER H , DINESHMILLS.D_SAL_PARTY_MASTER P,PRODUCTION.FELT_INCHARGE I,PRODUCTION.FELT_QLT_RATE_MASTER Q ";
                strSQL += "WHERE INVOICE_DATE >=CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01') AND INVOICE_DATE <=CURDATE()  AND H.PARTY_CODE = P.PARTY_CODE AND I.INCHARGE_CD = P.INCHARGE_CD AND Q.PRODUCT_CODE = H.PRODUCT_CODE ";
                strSQL += "AND EFFECTIVE_TO ='0000-00-00' ";
                strSQL += "GROUP BY P.INCHARGE_CD,I.INCHARGE_NAME,PRODUCT_CODE,Q.GROUP_NAME ";
                
                strSQL += "UNION ALL ";
                
                strSQL += "SELECT P.INCHARGE_CD, I.INCHARGE_NAME,'TOTAL' AS PRODUCT_CODE,CONCAT('TOTAL-',I.INCHARGE_NAME) AS GROUP_NAME, ";
                strSQL += "SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW() ,INTERVAL 1 DAY),1,10) THEN 1 ELSE 0 END) AS  DAYCNT ,";
                strSQL += "SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW() ,INTERVAL 1 DAY),1,10) THEN INVOICE_AMT ELSE 0 END) AS  DAYINV , ";
                
                rsData1.first();
                if (rsData1.getRow() > 0) {
                    while (!rsData1.isAfterLast()) {
                        
                        strSQL += "SUM(CASE WHEN EXTRACT( MONTH FROM INVOICE_DATE) = "+rsData1.getString("MNTH")+" THEN 1 ELSE 0 END) AS  "+rsData1.getString("MNTHNM")+"CNT , ";
                        strSQL += "SUM(CASE WHEN EXTRACT( MONTH FROM INVOICE_DATE) = "+rsData1.getString("MNTH")+" THEN INVOICE_AMT ELSE 0 END) AS  "+rsData1.getString("MNTHNM")+"INV , ";
                    
                        rsData1.next();
                    }
                } 
                
                strSQL += "COUNT(INVOICE_NO) AS TOTALCNT, ";
                strSQL += "SUM(INVOICE_AMT) AS TOTAL_INVAMT, ";
//                strSQL += "ROUND((SUM(INVOICE_AMT)/"+(cnt+1)+")*12,0) AS PRORATA_INV_AMT ";
                strSQL += "ROUND(SUM(INVOICE_AMT)/(DATEDIFF(CURDATE() ,CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01')))* ((DATEDIFF(CONCAT(CASE WHEN MONTH(CURDATE())>3 THEN (YEAR(CURDATE())+1) ELSE YEAR(CURDATE()) END,'-03-31'),CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01'))+1) -DATEDIFF(CURDATE() ,CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01'))),0) AS PRORATA_AMT1, ";
                strSQL += "ROUND(SUM(INVOICE_AMT)/(DATEDIFF(CURDATE() ,CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01')))* ((DATEDIFF(CONCAT(CASE WHEN MONTH(CURDATE())>3 THEN (YEAR(CURDATE())+1) ELSE YEAR(CURDATE()) END,'-03-31'),CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01'))+1) -DATEDIFF(CURDATE() ,CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01'))),0)+SUM(INVOICE_AMT) AS PRO_INV2 ";
                strSQL += "FROM  ";
                strSQL += "PRODUCTION.FELT_SAL_INVOICE_HEADER H , DINESHMILLS.D_SAL_PARTY_MASTER P,PRODUCTION.FELT_INCHARGE I,PRODUCTION.FELT_QLT_RATE_MASTER Q ";
                strSQL += "WHERE INVOICE_DATE >=CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01') AND INVOICE_DATE <=CURDATE()  AND H.PARTY_CODE = P.PARTY_CODE AND I.INCHARGE_CD = P.INCHARGE_CD AND Q.PRODUCT_CODE = H.PRODUCT_CODE ";
                strSQL += "AND EFFECTIVE_TO ='0000-00-00' ";
                strSQL += "GROUP BY P.INCHARGE_CD,I.INCHARGE_NAME ";
                
                strSQL += "UNION ALL ";
                
                strSQL += "SELECT 'GRAND TOTAL' AS INCHARGE_CD, 'GRAND TOTAL' AS INCHARGE_NAME, 'GRAND TOTAL' AS PRODUCT_CODE,'GRAND TOTAL' AS GROUP_NAME, ";
                strSQL += "SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW() ,INTERVAL 1 DAY),1,10) THEN 1 ELSE 0 END) AS  DAYCNT , ";
                strSQL += "SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW() ,INTERVAL 1 DAY),1,10) THEN INVOICE_AMT ELSE 0 END) AS  DAYINV , ";
                
                rsData1.first();
                if (rsData1.getRow() > 0) {
                    while (!rsData1.isAfterLast()) {
                        
                        strSQL += "SUM(CASE WHEN EXTRACT( MONTH FROM INVOICE_DATE) = "+rsData1.getString("MNTH")+" THEN 1 ELSE 0 END) AS  "+rsData1.getString("MNTHNM")+"CNT , ";
                        strSQL += "SUM(CASE WHEN EXTRACT( MONTH FROM INVOICE_DATE) = "+rsData1.getString("MNTH")+" THEN INVOICE_AMT ELSE 0 END) AS  "+rsData1.getString("MNTHNM")+"INV , ";
                    
                        rsData1.next();
                    }
                }
                
                strSQL += "COUNT(INVOICE_NO) AS TOTALCNT, ";
                strSQL += "SUM(INVOICE_AMT) AS TOTAL_INVAMT, ";
//                strSQL += "ROUND((SUM(INVOICE_AMT)/"+(cnt+1)+")*12,0) AS PRORATA_INV_AMT ";
                strSQL += "ROUND(SUM(INVOICE_AMT)/(DATEDIFF(CURDATE() ,CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01')))* ((DATEDIFF(CONCAT(CASE WHEN MONTH(CURDATE())>3 THEN (YEAR(CURDATE())+1) ELSE YEAR(CURDATE()) END,'-03-31'),CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01'))+1) -DATEDIFF(CURDATE() ,CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01'))),0) AS PRORATA_AMT1, ";
                strSQL += "ROUND(SUM(INVOICE_AMT)/(DATEDIFF(CURDATE() ,CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01')))* ((DATEDIFF(CONCAT(CASE WHEN MONTH(CURDATE())>3 THEN (YEAR(CURDATE())+1) ELSE YEAR(CURDATE()) END,'-03-31'),CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01'))+1) -DATEDIFF(CURDATE() ,CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01'))),0)+SUM(INVOICE_AMT) AS PRO_INV2 ";
                strSQL += "FROM  ";
                strSQL += "PRODUCTION.FELT_SAL_INVOICE_HEADER H , DINESHMILLS.D_SAL_PARTY_MASTER P,PRODUCTION.FELT_INCHARGE I,PRODUCTION.FELT_QLT_RATE_MASTER Q ";
                strSQL += "WHERE INVOICE_DATE >=CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01') AND INVOICE_DATE <=CURDATE()  AND H.PARTY_CODE = P.PARTY_CODE AND I.INCHARGE_CD = P.INCHARGE_CD AND Q.PRODUCT_CODE = H.PRODUCT_CODE ";
                strSQL += "AND EFFECTIVE_TO ='0000-00-00' ";
                strSQL += "ORDER BY INCHARGE_CD,INCHARGE_NAME,PRODUCT_CODE,GROUP_NAME ";
                
//                System.out.println("String StrSQL : "+strSQL);
                
                pMessage = pMessage + "<table border='1'>"
                        + "<tr>"
                        + "<th align='center' colspan='2'> ZONE </th>"
                        + "<th align='center' colspan='2'> PRODUCT </th>"
                        + "<th align='center' colspan='2'> "+curDate+" </th>";
                        
                rsData1.first();
                if (rsData1.getRow() > 0) {
                    while (!rsData1.isAfterLast()) {
                        
                        pMessage = pMessage + "<th align='center' colspan='2'> "+rsData1.getString("MNTHNM")+"-"+rsData1.getString("YR")+" </th>";
                    
                        rsData1.next();
                    }
                }        
                
                pMessage = pMessage + "<th align='center' colspan='2'> ACTUAL INVOICE </th>"
                        + "<th align='center'> PRORATA </th>"
                        + "<th align='center'> PRORATA + ACTUAL INVOICE </th>"
                        + "</tr>";
                
                pMessage = pMessage + "<tr>"
                        + "<th align='center'> CODE </th>"
                        + "<th align='center'> NAME </th>"
                        + "<th align='center'> CODE </th>"
                        + "<th align='center'> GROUP </th>"
                        + "<th align='center'> NO. </th>"
                        + "<th align='center'> AMOUNT </th>";
                        
                rsData1.first();
                if (rsData1.getRow() > 0) {
                    while (!rsData1.isAfterLast()) {
                        
                        pMessage = pMessage + "<th align='center'> NO. </th>";
                        pMessage = pMessage + "<th align='center'> AMOUNT </th>";
                    
                        rsData1.next();
                    }
                }        
                
                pMessage = pMessage + "<th align='center'> NO. </th>"
                        + "<th align='center'> AMOUNT </th>"
                        + "<th align='center'> AMOUNT </th>"
                        + "<th align='center'> AMOUNT </th>"
                        + "</tr>";

                
                ResultSet rsData2 = stmt1.executeQuery(strSQL);
                rsData2.first();
                if (rsData2.getRow() > 0) {
                    while (!rsData2.isAfterLast()) {

                        pMessage = pMessage + ""
                                + "<tr>"
                                + "<td align='right'> " + rsData2.getString("INCHARGE_CD") + " </td>"
                                + "<td align='left'> " + rsData2.getString("INCHARGE_NAME") + " </td>"
                                + "<td align='left'> " + rsData2.getString("PRODUCT_CODE") + " </td>"
                                + "<td align='left'> " + rsData2.getString("GROUP_NAME") + " </td>"
                                + "<td align='right'> " + rsData2.getString("DAYCNT") + " </td>"
                                + "<td align='right'> " + rsData2.getString("DAYINV") + " </td>";
                                int a=7;
                                for (int i=0; i<=cnt; i++) {
                                    pMessage = pMessage + "<td align='right'> " + rsData2.getString(a) + " </td>";
                                    a++;
                                    pMessage = pMessage + "<td align='right'> " + rsData2.getString(a) + " </td>";
                                    a++;
                                }
                                
                        pMessage = pMessage + "<td align='right'> " + rsData2.getString("TOTALCNT") + " </td>"
                                + "<td align='right'> " + rsData2.getString("TOTAL_INVAMT") + " </td>"
                                + "<td align='right'> " + rsData2.getString("PRORATA_AMT1") + " </td>"
                                + "<td align='right'> " + rsData2.getString("PRO_INV2") + " </td>";
                                
                        pMessage = pMessage + "</tr>";
                        rsData2.next();
                    }
                }
                pMessage = pMessage + "</table>";
                
                pMessage += "<br>";

                String recievers = "sdmlerp@dineshmills.com";

//                pMessage = pMessage + "<br><br><br> : Email Send to : <br>";
//                hmSendToList = clsHierarchy.getUserList(EITLERPGLOBAL.gCompanyID, hierarchyId, userId, true);
//                for (int i = 1; i <= hmSendToList.size(); i++) {
//                    clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));
//                    int U_ID = ObjUser.getAttribute("USER_ID").getInt();
//
//                    String to = clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, U_ID);
//
//                    System.out.println("USERID : " + U_ID + ", send_to : " + to);
//                    if (!to.equals("")) {
//                        recievers = recievers + "," + to;
//                        pMessage = pMessage + "<br>" + ObjUser.getAttribute("USER_NAME").getString();
//                    }
//                }
//            recievers = recievers + ",vdshanbhag@dineshmills.com,manoj@dineshmills.com,hcpatel@dineshmills.com,mva@dineshmills.com,atulshah@dineshmills.com,rakeshdalal@dineshmills.com,soumen@dineshmills.com";
//                cc = "aditya@dineshmills.com";

//                pMessage = pMessage + "<br>vdshanbhag@dineshmills.com";
//                pMessage = pMessage + "<br>manoj@dineshmills.com";
//                pMessage = pMessage + "<br>hcpatel@dineshmills.com";
//                pMessage = pMessage + "<br>mva@dineshmills.com";
//                pMessage = pMessage + "<br>atulshah@dineshmills.com";
//                pMessage = pMessage + "<br>rakeshdalal@dineshmills.com";
//                pMessage = pMessage + "<br>soumen@dineshmills.com";

                pMessage = pMessage + "<br><br>**** This is an auto-generated email, please do not reply ****";// For Live
//                pMessage = pMessage + "<br><br>**** This is an auto-generated email from TEST SERVER, please do not reply ****"; //For Test

                System.out.println("Recivers : " + recievers);
                System.out.println("pSubject : " + pSubject);
//                System.out.println("pMessage : " + pMessage);

                String responce = DailyMailNotification.sendNotificationMail(pSubject, pMessage, recievers, cc);
                System.out.println("Send Mail Responce : " + responce);

            } catch (Exception e) {
                System.out.println("Error on Mail: " + e.getMessage());
            }
    }
    
    private static void ProductwiseZonewiseNotification() {
            try {

                String pSubject = "Productwise Zonewise Felt Sales Daily Dispatch Report";
                String pMessage = "";
                String cc = "gaurang@dineshmills.com,rishineekhra@dineshmills.com,anandkumar.pandya@dineshmills.com";
                String strSQL = "";
                int cnt;
                int cntTotal;
                
                String curDate = data.getStringValueFromDB("SELECT DATE_FORMAT(DATE_SUB(CURDATE(),INTERVAL 1 DAY),\"%d %b %Y\")");
                int curMon = EITLERPGLOBAL.getCurrentMonth();
                int curYr = EITLERPGLOBAL.getCurrentYear();
                
                if (curMon<4) {
                    cnt = curMon+8;
                } else {
                    cnt = curMon-4;
                }
                
                ResultSet rs;
                
                pMessage = pMessage + "<br>Dear All,<br><br>Productwise Zonewise Felt Sales Daily Dispatch Report Details as given below :<br>";

                Connection Conn1;
                Statement stmt1;
                ResultSet rsData1;

                Conn1 = data.getConn();
                stmt1 = Conn1.createStatement();
                rsData1 = stmt1.executeQuery("select DISTINCT YR,LEFT(UPPER(MNTHNM),3) AS MNTHNM,MNTH FROM "
                        + "(select SELECTED_DATE,DAYNAME(SELECTED_DATE) AS ADAY,MONTH(SELECTED_DATE) AS MNTH,MONTHNAME(SELECTED_DATE) AS MNTHNM,YEAR(SELECTED_DATE) AS YR "
                        + "from (select adddate('1970-01-01',t4*10000 + t3*1000 + t2*100 + t1*10 + t0) selected_date "
                        + "from (select 0 t0 union select 1 union select 2 union select 3 union select 4 "
                        + "union select 5 union select 6 union select 7 union select 8 union select 9) t0, "
                        + "(select 0 t1 union select 1 union select 2 union select 3 union select 4 "
                        + "union select 5 union select 6 union select 7 union select 8 union select 9) t1, "
                        + "(select 0 t2 union select 1 union select 2 union select 3 "
                        + "union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t2, "
                        + "(select 0 t3 union select 1 union select 2 union select 3 union select 4 "
                        + "union select 5 union select 6 union select 7 union select 8 union select 9) t3, "
                        + "(select 0 t4 union select 1 union select 2 union select 3 "
                        + "union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t4) v "
                        + "where selected_date between CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01') and CURDATE()) AS Z ");
                                
                strSQL += "SELECT H.PRODUCT_CODE,Q.GROUP_NAME,P.INCHARGE_CD, I.INCHARGE_NAME, ";
                strSQL += "SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW() ,INTERVAL 1 DAY),1,10) THEN 1 ELSE 0 END) AS  DAYCNT , ";
                strSQL += "SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW() ,INTERVAL 1 DAY),1,10) THEN INVOICE_AMT ELSE 0 END) AS  DAYINV , ";
                
                rsData1.first();
                if (rsData1.getRow() > 0) {
                    while (!rsData1.isAfterLast()) {
                        
                        strSQL += "SUM(CASE WHEN EXTRACT( MONTH FROM INVOICE_DATE) = "+rsData1.getString("MNTH")+" THEN 1 ELSE 0 END) AS  "+rsData1.getString("MNTHNM")+"CNT , ";
                        strSQL += "SUM(CASE WHEN EXTRACT( MONTH FROM INVOICE_DATE) = "+rsData1.getString("MNTH")+" THEN INVOICE_AMT ELSE 0 END) AS  "+rsData1.getString("MNTHNM")+"INV , ";
                    
                        rsData1.next();
                    }
                } 
                
                strSQL += "COUNT(INVOICE_NO) AS TOTALCNT, ";
                strSQL += "SUM(INVOICE_AMT) AS TOTAL_INVAMT, ";
//                strSQL += "ROUND((SUM(INVOICE_AMT)/"+(cnt+1)+")*12,0) AS PRORATA_INV_AMT ";
                strSQL += "ROUND(SUM(INVOICE_AMT)/(DATEDIFF(CURDATE() ,CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01')))* ((DATEDIFF(CONCAT(CASE WHEN MONTH(CURDATE())>3 THEN (YEAR(CURDATE())+1) ELSE YEAR(CURDATE()) END,'-03-31'),CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01'))+1) -DATEDIFF(CURDATE() ,CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01'))),0) AS PRORATA_AMT1, ";
                strSQL += "ROUND(SUM(INVOICE_AMT)/(DATEDIFF(CURDATE() ,CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01')))* ((DATEDIFF(CONCAT(CASE WHEN MONTH(CURDATE())>3 THEN (YEAR(CURDATE())+1) ELSE YEAR(CURDATE()) END,'-03-31'),CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01'))+1) -DATEDIFF(CURDATE() ,CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01'))),0)+SUM(INVOICE_AMT) AS PRO_INV2 ";
                strSQL += "FROM ";
                strSQL += "PRODUCTION.FELT_SAL_INVOICE_HEADER H , DINESHMILLS.D_SAL_PARTY_MASTER P,PRODUCTION.FELT_INCHARGE I,PRODUCTION.FELT_QLT_RATE_MASTER Q ";
                strSQL += "WHERE INVOICE_DATE >=CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01') AND INVOICE_DATE <=CURDATE()  AND H.PARTY_CODE = P.PARTY_CODE AND I.INCHARGE_CD = P.INCHARGE_CD AND Q.PRODUCT_CODE = H.PRODUCT_CODE ";
                strSQL += "AND EFFECTIVE_TO ='0000-00-00' ";
                strSQL += "GROUP BY PRODUCT_CODE,Q.GROUP_NAME,P.INCHARGE_CD,I.INCHARGE_NAME ";
                
                strSQL += "UNION ALL ";
                
                strSQL += "SELECT H.PRODUCT_CODE,Q.GROUP_NAME,'TOTAL' AS INCHARGE_CD,CONCAT('TOTAL-',H.PRODUCT_CODE) AS  INCHARGE_NAME, ";
                strSQL += "SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW() ,INTERVAL 1 DAY),1,10) THEN 1 ELSE 0 END) AS  DAYCNT ,";
                strSQL += "SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW() ,INTERVAL 1 DAY),1,10) THEN INVOICE_AMT ELSE 0 END) AS  DAYINV , ";
                
                rsData1.first();
                if (rsData1.getRow() > 0) {
                    while (!rsData1.isAfterLast()) {
                        
                        strSQL += "SUM(CASE WHEN EXTRACT( MONTH FROM INVOICE_DATE) = "+rsData1.getString("MNTH")+" THEN 1 ELSE 0 END) AS  "+rsData1.getString("MNTHNM")+"CNT , ";
                        strSQL += "SUM(CASE WHEN EXTRACT( MONTH FROM INVOICE_DATE) = "+rsData1.getString("MNTH")+" THEN INVOICE_AMT ELSE 0 END) AS  "+rsData1.getString("MNTHNM")+"INV , ";
                    
                        rsData1.next();
                    }
                } 
                
                strSQL += "COUNT(INVOICE_NO) AS TOTALCNT, ";
                strSQL += "SUM(INVOICE_AMT) AS TOTAL_INVAMT, ";
//                strSQL += "ROUND((SUM(INVOICE_AMT)/"+(cnt+1)+")*12,0) AS PRORATA_INV_AMT ";
                strSQL += "ROUND(SUM(INVOICE_AMT)/(DATEDIFF(CURDATE() ,CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01')))* ((DATEDIFF(CONCAT(CASE WHEN MONTH(CURDATE())>3 THEN (YEAR(CURDATE())+1) ELSE YEAR(CURDATE()) END,'-03-31'),CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01'))+1) -DATEDIFF(CURDATE() ,CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01'))),0) AS PRORATA_AMT1, ";
                strSQL += "ROUND(SUM(INVOICE_AMT)/(DATEDIFF(CURDATE() ,CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01')))* ((DATEDIFF(CONCAT(CASE WHEN MONTH(CURDATE())>3 THEN (YEAR(CURDATE())+1) ELSE YEAR(CURDATE()) END,'-03-31'),CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01'))+1) -DATEDIFF(CURDATE() ,CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01'))),0)+SUM(INVOICE_AMT) AS PRO_INV2 ";
                strSQL += "FROM  ";
                strSQL += "PRODUCTION.FELT_SAL_INVOICE_HEADER H , DINESHMILLS.D_SAL_PARTY_MASTER P,PRODUCTION.FELT_INCHARGE I,PRODUCTION.FELT_QLT_RATE_MASTER Q ";
                strSQL += "WHERE INVOICE_DATE >=CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01') AND INVOICE_DATE <=CURDATE()  AND H.PARTY_CODE = P.PARTY_CODE AND I.INCHARGE_CD = P.INCHARGE_CD AND Q.PRODUCT_CODE = H.PRODUCT_CODE ";
                strSQL += "AND EFFECTIVE_TO ='0000-00-00' ";
                strSQL += "GROUP BY H.PRODUCT_CODE,Q.GROUP_NAME ";
                
                strSQL += "UNION ALL ";
                
                strSQL += "SELECT 'GRAND TOTAL' AS PRODUCT_CODE,'GRAND TOTAL' AS GROUP_NAME,'GRAND TOTAL' AS INCHARGE_CD, 'GRAND TOTAL' AS INCHARGE_NAME, ";
                strSQL += "SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW() ,INTERVAL 1 DAY),1,10) THEN 1 ELSE 0 END) AS  DAYCNT , ";
                strSQL += "SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW() ,INTERVAL 1 DAY),1,10) THEN INVOICE_AMT ELSE 0 END) AS  DAYINV , ";
                
                rsData1.first();
                if (rsData1.getRow() > 0) {
                    while (!rsData1.isAfterLast()) {
                        
                        strSQL += "SUM(CASE WHEN EXTRACT( MONTH FROM INVOICE_DATE) = "+rsData1.getString("MNTH")+" THEN 1 ELSE 0 END) AS  "+rsData1.getString("MNTHNM")+"CNT , ";
                        strSQL += "SUM(CASE WHEN EXTRACT( MONTH FROM INVOICE_DATE) = "+rsData1.getString("MNTH")+" THEN INVOICE_AMT ELSE 0 END) AS  "+rsData1.getString("MNTHNM")+"INV , ";
                    
                        rsData1.next();
                    }
                }
                
                strSQL += "COUNT(INVOICE_NO) AS TOTALCNT, ";
                strSQL += "SUM(INVOICE_AMT) AS TOTAL_INVAMT, ";
//                strSQL += "ROUND((SUM(INVOICE_AMT)/"+(cnt+1)+")*12,0) AS PRORATA_INV_AMT ";
                strSQL += "ROUND(SUM(INVOICE_AMT)/(DATEDIFF(CURDATE() ,CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01')))* ((DATEDIFF(CONCAT(CASE WHEN MONTH(CURDATE())>3 THEN (YEAR(CURDATE())+1) ELSE YEAR(CURDATE()) END,'-03-31'),CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01'))+1) -DATEDIFF(CURDATE() ,CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01'))),0) AS PRORATA_AMT1, ";
                strSQL += "ROUND(SUM(INVOICE_AMT)/(DATEDIFF(CURDATE() ,CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01')))* ((DATEDIFF(CONCAT(CASE WHEN MONTH(CURDATE())>3 THEN (YEAR(CURDATE())+1) ELSE YEAR(CURDATE()) END,'-03-31'),CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01'))+1) -DATEDIFF(CURDATE() ,CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01'))),0)+SUM(INVOICE_AMT) AS PRO_INV2 ";
                strSQL += "FROM  ";
                strSQL += "PRODUCTION.FELT_SAL_INVOICE_HEADER H , DINESHMILLS.D_SAL_PARTY_MASTER P,PRODUCTION.FELT_INCHARGE I,PRODUCTION.FELT_QLT_RATE_MASTER Q ";
                strSQL += "WHERE INVOICE_DATE >=CONCAT(CASE WHEN MONTH(CURDATE())<4 THEN (YEAR(CURDATE())-1) ELSE YEAR(CURDATE()) END,'-04-01') AND INVOICE_DATE <=CURDATE()  AND H.PARTY_CODE = P.PARTY_CODE AND I.INCHARGE_CD = P.INCHARGE_CD AND Q.PRODUCT_CODE = H.PRODUCT_CODE ";
                strSQL += "AND EFFECTIVE_TO ='0000-00-00' ";
                strSQL += "ORDER BY PRODUCT_CODE,GROUP_NAME,INCHARGE_CD,INCHARGE_NAME ";
                
//                System.out.println("String StrSQL : "+strSQL);
                
                pMessage = pMessage + "<table border='1'>"
                        + "<tr>"
                        + "<th align='center' colspan='2'> PRODUCT </th>"
                        + "<th align='center' colspan='2'> ZONE </th>"
                        + "<th align='center' colspan='2'> "+curDate+" </th>";
                        
                rsData1.first();
                if (rsData1.getRow() > 0) {
                    while (!rsData1.isAfterLast()) {
                        
                        pMessage = pMessage + "<th align='center' colspan='2'> "+rsData1.getString("MNTHNM")+"-"+rsData1.getString("YR")+" </th>";
                    
                        rsData1.next();
                    }
                }        
                
                pMessage = pMessage + "<th align='center' colspan='2'> ACTUAL INVOICE </th>"
                        + "<th align='center'> PRORATA </th>"
                        + "<th align='center'> PRORATA + ACTUAL INVOICE </th>"
                        + "</tr>";
                
                pMessage = pMessage + "<tr>"
                        + "<th align='center'> CODE </th>"
                        + "<th align='center'> GROUP </th>"
                        + "<th align='center'> CODE </th>"
                        + "<th align='center'> NAME </th>"
                        + "<th align='center'> NO. </th>"
                        + "<th align='center'> AMOUNT </th>";
                        
                rsData1.first();
                if (rsData1.getRow() > 0) {
                    while (!rsData1.isAfterLast()) {
                        
                        pMessage = pMessage + "<th align='center'> NO. </th>";
                        pMessage = pMessage + "<th align='center'> AMOUNT </th>";
                    
                        rsData1.next();
                    }
                }        
                
                pMessage = pMessage + "<th align='center'> NO. </th>"
                        + "<th align='center'> AMOUNT </th>"
                        + "<th align='center'> AMOUNT </th>"
                        + "<th align='center'> AMOUNT </th>"
                        + "</tr>";

                
                ResultSet rsData2 = stmt1.executeQuery(strSQL);
                rsData2.first();
                if (rsData2.getRow() > 0) {
                    while (!rsData2.isAfterLast()) {

                        pMessage = pMessage + ""
                                + "<tr>"
                                + "<td align='right'> " + rsData2.getString("PRODUCT_CODE") + " </td>"
                                + "<td align='left'> " + rsData2.getString("GROUP_NAME") + " </td>"
                                + "<td align='left'> " + rsData2.getString("INCHARGE_CD") + " </td>"
                                + "<td align='left'> " + rsData2.getString("INCHARGE_NAME") + " </td>"
                                + "<td align='right'> " + rsData2.getString("DAYCNT") + " </td>"
                                + "<td align='right'> " + rsData2.getString("DAYINV") + " </td>";
                                int a=7;
                                for (int i=0; i<=cnt; i++) {
                                    pMessage = pMessage + "<td align='right'> " + rsData2.getString(a) + " </td>";
                                    a++;
                                    pMessage = pMessage + "<td align='right'> " + rsData2.getString(a) + " </td>";
                                    a++;
                                }
                                
                        pMessage = pMessage + "<td align='right'> " + rsData2.getString("TOTALCNT") + " </td>"
                                + "<td align='right'> " + rsData2.getString("TOTAL_INVAMT") + " </td>"
                                + "<td align='right'> " + rsData2.getString("PRORATA_AMT1") + " </td>"
                                + "<td align='right'> " + rsData2.getString("PRO_INV2") + " </td>";
                                
                        pMessage = pMessage + "</tr>";
                        rsData2.next();
                    }
                }
                pMessage = pMessage + "</table>";
                
                pMessage += "<br>";

                String recievers = "sdmlerp@dineshmills.com";

//                pMessage = pMessage + "<br><br><br> : Email Send to : <br>";
//                hmSendToList = clsHierarchy.getUserList(EITLERPGLOBAL.gCompanyID, hierarchyId, userId, true);
//                for (int i = 1; i <= hmSendToList.size(); i++) {
//                    clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));
//                    int U_ID = ObjUser.getAttribute("USER_ID").getInt();
//
//                    String to = clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, U_ID);
//
//                    System.out.println("USERID : " + U_ID + ", send_to : " + to);
//                    if (!to.equals("")) {
//                        recievers = recievers + "," + to;
//                        pMessage = pMessage + "<br>" + ObjUser.getAttribute("USER_NAME").getString();
//                    }
//                }
//            recievers = recievers + ",vdshanbhag@dineshmills.com,manoj@dineshmills.com,hcpatel@dineshmills.com,mva@dineshmills.com,atulshah@dineshmills.com,rakeshdalal@dineshmills.com,soumen@dineshmills.com";
//                cc = "aditya@dineshmills.com";

//                pMessage = pMessage + "<br>vdshanbhag@dineshmills.com";
//                pMessage = pMessage + "<br>manoj@dineshmills.com";
//                pMessage = pMessage + "<br>hcpatel@dineshmills.com";
//                pMessage = pMessage + "<br>mva@dineshmills.com";
//                pMessage = pMessage + "<br>atulshah@dineshmills.com";
//                pMessage = pMessage + "<br>rakeshdalal@dineshmills.com";
//                pMessage = pMessage + "<br>soumen@dineshmills.com";

                pMessage = pMessage + "<br><br>**** This is an auto-generated email, please do not reply ****";// For Live
//                pMessage = pMessage + "<br><br>**** This is an auto-generated email from TEST SERVER, please do not reply ****"; //For Test

                System.out.println("Recivers : " + recievers);
                System.out.println("pSubject : " + pSubject);
//                System.out.println("pMessage : " + pMessage);

                String responce = DailyMailNotification.sendNotificationMail(pSubject, pMessage, recievers, cc);
                System.out.println("Send Mail Responce : " + responce);

            } catch (Exception e) {
                System.out.println("Error on Mail: " + e.getMessage());
            }
    }
    
}
