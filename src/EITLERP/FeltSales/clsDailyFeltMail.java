/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales;

import EITLERP.EITLERPGLOBAL;
import EITLERP.data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

/**
 *
 * @author root
 */
public class clsDailyFeltMail {

    static String pMessage = "";
    static String pSubject = "Daily Total Felt Sales Invoice Notication : For Date " + data.getStringValueFromDB("SELECT DATE_FORMAT(DATE_SUB(CURDATE(),INTERVAL 1 DAY),'%d %b %Y')");

    static String InvFromDateDB = "";
    static String InvToDateDB = "";
    static int FromYear, ToYear;

    public static void main(String[] args) {
        InvFinYear();
        generateMessage();
        String recievers = "felts@dineshmills.com,hemantprajapati@dineshmills.com";
        //String recievers = "rishineekhra@dineshmills.com";
        //String recievers = "sdmlerp@dineshmills.com";
        String responce = DailyMailNotification.sendNotificationMail(pSubject, pMessage, recievers, "sdmlerp@dineshmills.com,rakeshdalal@dineshmills.com");
        //String responce = DailyMailNotification.sendNotificationMail(pSubject, pMessage, recievers, "sdmlerp@dineshmills.com");
    }

    private static void generateMessage() {
        try {
            pMessage = pMessage + "<style> table, th, td {"
                    + "  border: 1px solid black;"
                    + "  border-collapse: collapse;"
                    + "  font-family: 'Arial'"
                    + "} th, td {"
                    + "  padding: 5px;"
                    + "}</style><font face='Arial'>  <br><br><br><b>1. Total Invoices</b>";

            pMessage = pMessage + "<br><br><table border=1>";
            pMessage = pMessage + "<tr style='font-weight:bold'>";
            pMessage = pMessage + "<td colspan=4 align=center>" + data.getStringValueFromDB("SELECT DATE_FORMAT(DATE_SUB(CURDATE(),INTERVAL 1 DAY),'%d %b %Y')") + "</td>";
            pMessage = pMessage + "</tr>";
            pMessage = pMessage + "<tr style='font-weight:bold'>";
            pMessage = pMessage + "<td>NO.</td>";
            pMessage = pMessage + "<td>AMOUNT</td>";
            pMessage = pMessage + "<td>KGS</td>";
            pMessage = pMessage + "<td>SQMTR</td>";
            pMessage = pMessage + "</tr>";

            Connection Conn1;
            Statement stmt1;
            String strSQL = "";
            ResultSet rsData1;
            Conn1 = data.getConn();
            stmt1 = Conn1.createStatement();
            strSQL = "SELECT SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW()  "
                    + ",INTERVAL 1 DAY),1,10) THEN 1 ELSE 0 END) AS  DAYCNT , "
                    + "SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW() "
                    + ",INTERVAL 1 DAY),1,10) THEN INVOICE_AMT ELSE 0 END) AS  DAYINV ,"
                    + "SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW()"
                    + ",INTERVAL 1 DAY),1,10)  AND RATE_UNIT ='KG' THEN ACTUAL_WEIGHT ELSE 0 END)"
                    + "AS  DAYKG,"
                    + "SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW() "
                    + ",INTERVAL 1 DAY),1,10)  AND RATE_UNIT ='MTR' THEN SQMTR ELSE 0 END) AS "
                    + "DAYSQMTR "
                    + "FROM "
                    + "PRODUCTION.FELT_SAL_INVOICE_HEADER H , DINESHMILLS.D_SAL_PARTY_MASTER "
                    + "P,PRODUCTION.FELT_INCHARGE I,PRODUCTION.FELT_QLT_RATE_MASTER Q "
                    + "WHERE H.APPROVED=1 AND H.CANCELLED=0 AND INVOICE_DATE >='" + InvFromDateDB + "'  AND INVOICE_DATE <='" + InvToDateDB + "'  AND "
                    + "H.PARTY_CODE = P.PARTY_CODE AND I.INCHARGE_CD = P.INCHARGE_CD AND "
                    + "Q.PRODUCT_CODE = H.PRODUCT_CODE "
                    + "AND EFFECTIVE_TO ='0000-00-00'";
            System.out.println("SQL " + strSQL);
            rsData1 = stmt1.executeQuery(strSQL);
            System.out.println("");
            rsData1.first();
            if (rsData1.getRow() > 0) {
                while (!rsData1.isAfterLast()) {

                    pMessage = pMessage + "<tr>";
                    pMessage = pMessage + "<td align=right>" + rsData1.getString(1) + "</td>";
                    pMessage = pMessage + "<td align=right>" + rsData1.getString(2) + "</td>";
                    pMessage = pMessage + "<td align=right>" + rsData1.getString(3) + "</td>";
                    pMessage = pMessage + "<td align=right>" + rsData1.getString(4) + "</td>";
                    pMessage = pMessage + "</tr>";

                    rsData1.next();
                }
            }
            pMessage = pMessage + "</table>";

            pMessage = pMessage + "<br><br><br><b>2. Product Wise Total</b>";
            Connection Conn2;
            Statement stmt2;
            ResultSet rsData2;
            Conn2 = data.getConn();
            stmt2 = Conn2.createStatement();
            strSQL = "SELECT * FROM "
                    + " "
                    + "(SELECT "
                    + " "
                    + "CASE WHEN Q.FELT_CATG = 'PRESS' THEN 1 "
                    + " "
                    + "WHEN Q.FELT_CATG = 'ACNE/FCNE' THEN 2 "
                    + " "
                    + "WHEN Q.FELT_CATG = 'HDS' THEN 3 "
                    + " "
                    + "WHEN Q.FELT_CATG = 'SDF' THEN 4 "
                    + " "
                    + "WHEN Q.FELT_CATG = 'OTHERS' THEN 5 END AS CATG_NO, "
                    + " "
                    + "  "
                    + " "
                    + "Q.FELT_CATG, "
                    + " "
                    + "  "
                    + " "
                    + "  "
                    + "COALESCE(BUDGET_TOTAL,0) AS BUDGET_TOTAL,COALESCE(BUDGET_KG,0) AS BUDGET_KG,COALESCE(BUDGET_SQMTR,0) AS BUDGET_SQMTR,COALESCE(BUDGET_AMOUNT,0) AS BUDGET_AMOUNT, "
                    + "SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW() "
                    + ",INTERVAL 1 DAY),1,10) THEN 1 ELSE 0 END) AS  DAYCNT , "
                    + " "
                    + "SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW() "
                    + ",INTERVAL 1 DAY),1,10) THEN INVOICE_AMT ELSE 0 END) AS  DAYINV , "
                    + " "
                    + "SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW() "
                    + ",INTERVAL 1 DAY),1,10)  AND RATE_UNIT ='KG' THEN ACTUAL_WEIGHT ELSE 0 END) "
                    + "AS  DAYKG, "
                    + " "
                    + "SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW() "
                    + ",INTERVAL 1 DAY),1,10)  AND RATE_UNIT ='MTR' THEN SQMTR ELSE 0 END) AS "
                    + "DAYSQMTR , "
                    + " "
                    + "  "
                    + " "
                    + "  "
                    + " "
                    + "SUM(CASE WHEN EXTRACT( MONTH FROM INVOICE_DATE) = EXTRACT(MONTH FROM SUBSTRING(DATE_SUB(NOW() ,INTERVAL 1 DAY),1,10)) THEN 1 ELSE 0 END) AS "
                    + "DECCNT , "
                    + " "
                    + "SUM(CASE WHEN EXTRACT( MONTH FROM INVOICE_DATE) = EXTRACT(MONTH FROM SUBSTRING(DATE_SUB(NOW() ,INTERVAL 1 DAY),1,10)) THEN INVOICE_AMT ELSE 0 "
                    + "END) AS  DECINV , "
                    + " "
                    + "SUM(CASE WHEN EXTRACT( MONTH FROM INVOICE_DATE) = EXTRACT(MONTH FROM SUBSTRING(DATE_SUB(NOW() ,INTERVAL 1 DAY),1,10)) AND RATE_UNIT ='KG' "
                    + "THEN ACTUAL_WEIGHT ELSE 0 END) AS  DECKG , "
                    + " "
                    + "SUM(CASE WHEN EXTRACT( MONTH FROM INVOICE_DATE) = EXTRACT(MONTH FROM SUBSTRING(DATE_SUB(NOW() ,INTERVAL 1 DAY),1,10)) AND RATE_UNIT ='MTR' "
                    + "THEN SQMTR ELSE 0 END) AS  DECSQMTR , "
                    + " "
                    + "  "
                    + " "
                    + "COUNT(INVOICE_NO) AS TOTALCNT, "
                    + " "
                    + "SUM(INVOICE_AMT) AS TOTAL_INVAMT, "
                    + " "
                    + "SUM(CASE WHEN RATE_UNIT ='KG' THEN ACTUAL_WEIGHT ELSE 0 END) AS  INVKG , "
                    + " "
                    + "SUM(CASE WHEN RATE_UNIT ='MTR' THEN SQMTR ELSE 0 END) AS  INVSQMTR "
                    + " "
                    + "  "
                    + " "
                    + "  "
                    + " "
                    + "  "
                    + " "
                    + "  "
                    + " "
                    + "  "
                    + " "
                    + "FROM "
                    + " "
                    + "PRODUCTION.FELT_SAL_INVOICE_HEADER H , DINESHMILLS.D_SAL_PARTY_MASTER "
                    + "P,PRODUCTION.FELT_INCHARGE I,PRODUCTION.FELT_QLT_RATE_MASTER Q "
                    + "LEFT JOIN ("
                    + "SELECT FELT_CATG AS FELT_CAT,SUM(TOTAL) AS BUDGET_TOTAL,SUM(TOTAL_KG) AS BUDGET_KG,SUM(TOTAL_SQMTR) AS BUDGET_SQMTR,SUM(NET_AMOUNT) AS BUDGET_AMOUNT  "
                    + "FROM PRODUCTION.FELT_BUDGET  "
                    + "LEFT JOIN PRODUCTION.FELT_QLT_RATE_MASTER ON PRODUCT_CODE=QUALITY_NO "
                    + "WHERE EFFECTIVE_TO='0000-00-00' AND YEAR_FROM=" + FromYear + " AND YEAR_TO=" + ToYear + "  "
                    + "GROUP BY FELT_CATG) AS BUDGET ON FELT_CATG=FELT_CAT "
                    + "WHERE H.APPROVED=1 AND H.CANCELLED=0 AND INVOICE_DATE >='" + InvFromDateDB + "'  AND INVOICE_DATE <='" + InvToDateDB + "'  AND "
                    + "H.PARTY_CODE = P.PARTY_CODE AND I.INCHARGE_CD = P.INCHARGE_CD AND "
                    + "Q.PRODUCT_CODE = H.PRODUCT_CODE "
                    + " "
                    + "AND EFFECTIVE_TO ='0000-00-00' "
                    + " "
                    + "GROUP BY Q.FELT_CATG) AS V "
                    + " "
                    + "  "
                    + " "
                    + "UNION ALL "
                    + " "
                    + "  "
                    + "SELECT PRODUCT_CODE,GROUP_NAME,COALESCE(BUDGET_TOTAL,0) AS BUDGET_TOTAL,COALESCE(BUDGET_KG,0) AS BUDGET_KG,COALESCE(BUDGET_SQMTR,0) AS BUDGET_SQMTR,COALESCE(BUDGET_AMOUNT,0) AS BUDGET_AMOUNT,DAYCNT,"
                    + "DAYINV,DAYKG,DAYSQMTR,DECCNT,DECINV,DECKG,DECSQMTR,TOTALCNT,TOTAL_INVAMT,INVKG,INVSQMTR FROM ( "
                    + "SELECT 'GRAND TOTAL' AS PRODUCT_CODE,'GRAND TOTAL' AS GROUP_NAME, "
                    + "SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW() "
                    + ",INTERVAL 1 DAY),1,10) THEN 1 ELSE 0 END) AS  DAYCNT , "
                    + " "
                    + "SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW() "
                    + ",INTERVAL 1 DAY),1,10) THEN INVOICE_AMT ELSE 0 END) AS  DAYINV , "
                    + " "
                    + "SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW() "
                    + ",INTERVAL 1 DAY),1,10)  AND RATE_UNIT ='KG' THEN ACTUAL_WEIGHT ELSE 0 END) "
                    + "AS  DAYKG, "
                    + " "
                    + "SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW() "
                    + ",INTERVAL 1 DAY),1,10)  AND RATE_UNIT ='MTR' THEN SQMTR ELSE 0 END) AS "
                    + "DAYSQMTR , "
                    + " "
                    + "  "
                    + " "
                    + "  "
                    + " "
                    + "SUM(CASE WHEN EXTRACT( MONTH FROM INVOICE_DATE) = EXTRACT(MONTH FROM SUBSTRING(DATE_SUB(NOW() ,INTERVAL 1 DAY),1,10)) THEN 1 ELSE 0 END) AS "
                    + "DECCNT , "
                    + " "
                    + "SUM(CASE WHEN EXTRACT( MONTH FROM INVOICE_DATE) = EXTRACT(MONTH FROM SUBSTRING(DATE_SUB(NOW() ,INTERVAL 1 DAY),1,10)) THEN INVOICE_AMT ELSE 0 "
                    + "END) AS  DECINV , "
                    + " "
                    + "SUM(CASE WHEN EXTRACT( MONTH FROM INVOICE_DATE) = EXTRACT(MONTH FROM SUBSTRING(DATE_SUB(NOW() ,INTERVAL 1 DAY),1,10)) AND RATE_UNIT ='KG' "
                    + "THEN ACTUAL_WEIGHT ELSE 0 END) AS  DECKG , "
                    + " "
                    + "SUM(CASE WHEN EXTRACT( MONTH FROM INVOICE_DATE) = EXTRACT(MONTH FROM SUBSTRING(DATE_SUB(NOW() ,INTERVAL 1 DAY),1,10)) AND RATE_UNIT ='MTR' "
                    + "THEN SQMTR ELSE 0 END) AS  DECSQMTR, "
                    + " "
                    + "  "
                    + " "
                    + "  "
                    + " "
                    + "  "
                    + " "
                    + "COUNT(INVOICE_NO) AS TOTALCNT, "
                    + " "
                    + "SUM(INVOICE_AMT) AS TOTAL_INVAMT, "
                    + " "
                    + "SUM(CASE WHEN RATE_UNIT ='KG' THEN ACTUAL_WEIGHT ELSE 0 END) AS  INVKG , "
                    + " "
                    + "SUM(CASE WHEN RATE_UNIT ='MTR' THEN SQMTR ELSE 0 END) AS  INVSQMTR "
                    + " "
                    + "  "
                    + " "
                    + "  "
                    + " "
                    + "FROM "
                    + " "
                    + "PRODUCTION.FELT_SAL_INVOICE_HEADER H , DINESHMILLS.D_SAL_PARTY_MASTER "
                    + "P,PRODUCTION.FELT_INCHARGE I,PRODUCTION.FELT_QLT_RATE_MASTER Q "
                    + "WHERE H.APPROVED=1 AND H.CANCELLED=0 AND INVOICE_DATE >='" + InvFromDateDB + "'  AND INVOICE_DATE <='" + InvToDateDB + "'  AND "
                    + "H.PARTY_CODE = P.PARTY_CODE AND I.INCHARGE_CD = P.INCHARGE_CD AND "
                    + "Q.PRODUCT_CODE = H.PRODUCT_CODE "
                    + " "
                    + "AND EFFECTIVE_TO ='0000-00-00' ) AS BBB  "
                    + " LEFT JOIN (SELECT 'GRAND TOTAL' AS FELT_CAT, 'GRAND TOTAL'  AS I_NAME,SUM(TOTAL) AS BUDGET_TOTAL,SUM(TOTAL_KG) AS BUDGET_KG,SUM(TOTAL_SQMTR) AS BUDGET_SQMTR,SUM(NET_AMOUNT) AS BUDGET_AMOUNT \n"
                    + "FROM PRODUCTION.FELT_BUDGET WHERE YEAR_FROM=" + FromYear + " AND YEAR_TO=" + ToYear + " ) AS BUDGET ON BBB.PRODUCT_CODE=BUDGET.I_NAME"
                    + " "
                    + "ORDER BY CATG_NO "
                    + "";

            System.out.println("SQL " + strSQL);
            rsData2 = stmt2.executeQuery(strSQL);
            ResultSetMetaData rsmd = rsData2.getMetaData();

            pMessage = pMessage + "<br><br><table border=1>";
            pMessage = pMessage + "<tr style='font-weight:bold'>";
            pMessage = pMessage + "<td colspan=2 align=center> PRODUCT </td>";
            pMessage = pMessage + "<td colspan=4 align=center> BUDGET </td>";
            pMessage = pMessage + "<td colspan=4 align=center> " + data.getStringValueFromDB("SELECT DATE_FORMAT(DATE_SUB(CURDATE(),INTERVAL 1 DAY),'%d %b %Y')") + "</td>";
            pMessage = pMessage + "<td colspan=4 align=center> Monthly Total Invoice Till Date</td>";
            pMessage = pMessage + "<td colspan=4 align=center> Yearly Total Invoice Till Date</td>";
            pMessage = pMessage + "</tr>";
            pMessage = pMessage + "<tr style='font-weight:bold'>";
            pMessage = pMessage + "<td align=center> CODE </td>";
            pMessage = pMessage + "<td align=center> GROUP </td>";
            pMessage = pMessage + "<td align=center> Nos. </td>";
            pMessage = pMessage + "<td align=center> Kg </td>";
            pMessage = pMessage + "<td align=center> Sqmtr </td>";
            pMessage = pMessage + "<td align=center> Amount </td>";
            pMessage = pMessage + "<td align=center> NO. </td>";
            pMessage = pMessage + "<td align=center> AMOUNT </td>";
            pMessage = pMessage + "<td align=center> KGS </td>";
            pMessage = pMessage + "<td align=center> SQMTR </td>";
            pMessage = pMessage + "<td align=center> NO. </td>";
            pMessage = pMessage + "<td align=center> AMOUNT </td>";
            pMessage = pMessage + "<td align=center> KGS </td>";
            pMessage = pMessage + "<td align=center> SQMTR </td>";
            pMessage = pMessage + "<td align=center> NO. </td>";
            pMessage = pMessage + "<td align=center> AMOUNT </td>";
            pMessage = pMessage + "<td align=center> KGS </td>";
            pMessage = pMessage + "<td align=center> SQMTR </td>";

            pMessage = pMessage + "</tr>";

            System.out.println("");
            rsData2.first();
            if (rsData2.getRow() > 0) {
                while (!rsData2.isAfterLast()) {
                    pMessage = pMessage + "<tr>";
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        if (i > 2) {
                            pMessage = pMessage + "<td align=right>" + rsData2.getString(i) + "</td>";
                        } else {
                            pMessage = pMessage + "<td>" + rsData2.getString(i) + "</td>";
                        }
                    }
                    pMessage = pMessage + "</tr>";

                    rsData2.next();
                }
            }
            pMessage = pMessage + "</table>";

            //
            Connection Conn3;
            Statement stmt3;
            ResultSet rsData3;
            Conn3 = data.getConn();
            stmt3 = Conn3.createStatement();
            strSQL = "SELECT INCHARGE_CD,INCHARGE_NAME,COALESCE(BUDGET_TOTAL,0) AS BUDGET_TOTAL,COALESCE(BUDGET_KG,0) AS BUDGET_KG,COALESCE(BUDGET_SQMTR,0) AS BUDGET_SQMTR,COALESCE(BUDGET_AMOUNT,0) AS BUDGET_AMOUNT, DAYCNT,"
                    + "DAYINV,DAYKG,DAYSQMTR,DECCNT,DECINV,DECKG,DECSQMTR,TOTALCNT,TOTAL_INVAMT,INVKG,INVSQMTR FROM "
                    + "(SELECT P.INCHARGE_CD, I.INCHARGE_NAME, "
                    + "SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW() "
                    + ",INTERVAL 1 DAY),1,10) THEN 1 ELSE 0 END) AS  DAYCNT , "
                    + " "
                    + "SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW() "
                    + ",INTERVAL 1 DAY),1,10) THEN INVOICE_AMT ELSE 0 END) AS  DAYINV , "
                    + " "
                    + "SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW() "
                    + ",INTERVAL 1 DAY),1,10)  AND RATE_UNIT ='KG' THEN ACTUAL_WEIGHT ELSE 0 END) "
                    + "AS  DAYKG, "
                    + " "
                    + "SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW() "
                    + ",INTERVAL 1 DAY),1,10)  AND RATE_UNIT ='MTR' THEN SQMTR ELSE 0 END) AS "
                    + "DAYSQMTR , "
                    + " "
                    + "  "
                    + " "
                    + "  "
                    + " "
                    + "SUM(CASE WHEN EXTRACT( MONTH FROM INVOICE_DATE) = EXTRACT(MONTH FROM SUBSTRING(DATE_SUB(NOW() ,INTERVAL 1 DAY),1,10)) THEN 1 ELSE 0 END) AS "
                    + "DECCNT , "
                    + " "
                    + "SUM(CASE WHEN EXTRACT( MONTH FROM INVOICE_DATE) = EXTRACT(MONTH FROM SUBSTRING(DATE_SUB(NOW() ,INTERVAL 1 DAY),1,10)) THEN INVOICE_AMT ELSE 0 "
                    + "END) AS  DECINV , "
                    + " "
                    + "SUM(CASE WHEN EXTRACT( MONTH FROM INVOICE_DATE) = EXTRACT(MONTH FROM SUBSTRING(DATE_SUB(NOW() ,INTERVAL 1 DAY),1,10)) AND RATE_UNIT ='KG' "
                    + "THEN ACTUAL_WEIGHT ELSE 0 END) AS  DECKG , "
                    + " "
                    + "SUM(CASE WHEN EXTRACT( MONTH FROM INVOICE_DATE) = EXTRACT(MONTH FROM SUBSTRING(DATE_SUB(NOW() ,INTERVAL 1 DAY),1,10)) AND RATE_UNIT ='MTR' "
                    + "THEN SQMTR ELSE 0 END) AS  DECSQMTR , "
                    + " "
                    + "  "
                    + " "
                    + "COUNT(INVOICE_NO) AS TOTALCNT, "
                    + " "
                    + "SUM(INVOICE_AMT) AS TOTAL_INVAMT, "
                    + " "
                    + "SUM(CASE WHEN RATE_UNIT ='KG' THEN ACTUAL_WEIGHT ELSE 0 END) AS  INVKG , "
                    + " "
                    + "SUM(CASE WHEN RATE_UNIT ='MTR' THEN SQMTR ELSE 0 END) AS  INVSQMTR "
                    + " "
                    + "  "
                    + " "
                    + "  "
                    + " "
                    + "  "
                    + " "
                    + "  "
                    + " "
                    + "  "
                    + " "
                    + "FROM "
                    + " "
                    + "PRODUCTION.FELT_SAL_INVOICE_HEADER H , DINESHMILLS.D_SAL_PARTY_MASTER "
                    + "P,PRODUCTION.FELT_INCHARGE I,PRODUCTION.FELT_QLT_RATE_MASTER Q "
                    + "LEFT JOIN (SELECT M.INCHARGE_CD AS FELT_CAT,INCHARGE_NAME,SUM(TOTAL) AS BUDGET_TOTAL,SUM(TOTAL_KG) AS BUDGET_KG,SUM(TOTAL_SQMTR) AS BUDGET_SQMTR,SUM(NET_AMOUNT) AS BUDGET_AMOUNT "
                    + "FROM PRODUCTION.FELT_BUDGET B "
                    + "LEFT JOIN DINESHMILLS.D_SAL_PARTY_MASTER M ON B.PARTY_CODE=M.PARTY_CODE "
                    + "LEFT JOIN PRODUCTION.FELT_INCHARGE I ON M.INCHARGE_CD=I.INCHARGE_CD "
                    + "GROUP BY I.INCHARGE_CD) AS BUDGET ON INCHARGE_NAME=BUDGET.FELT_CAT  "
                    + "WHERE H.APPROVED=1 AND H.CANCELLED=0 AND INVOICE_DATE >='" + InvFromDateDB + "'  AND INVOICE_DATE <='" + InvToDateDB + "'  AND "
                    + "H.PARTY_CODE = P.PARTY_CODE AND I.INCHARGE_CD = P.INCHARGE_CD AND "
                    + "Q.PRODUCT_CODE = H.PRODUCT_CODE "
                    + " "
                    + "AND EFFECTIVE_TO ='0000-00-00' "
                    + " "
                    + "GROUP BY P.INCHARGE_CD,I.INCHARGE_NAME ) AS AAA "
                    + "LEFT JOIN (SELECT M.INCHARGE_CD AS FELT_CAT,INCHARGE_NAME AS I_NAME,SUM(TOTAL) AS BUDGET_TOTAL,SUM(TOTAL_KG) AS BUDGET_KG,SUM(TOTAL_SQMTR) AS BUDGET_SQMTR,SUM(NET_AMOUNT) AS BUDGET_AMOUNT  "
                    + "FROM PRODUCTION.FELT_BUDGET B "
                    + "LEFT JOIN DINESHMILLS.D_SAL_PARTY_MASTER M ON B.PARTY_CODE=M.PARTY_CODE "
                    + "LEFT JOIN PRODUCTION.FELT_INCHARGE I ON M.INCHARGE_CD=I.INCHARGE_CD "
                    + "WHERE YEAR_FROM=" + FromYear + " AND YEAR_TO=" + ToYear + "  "
                    + "GROUP BY I.INCHARGE_CD) AS BUDGET ON AAA.INCHARGE_NAME=BUDGET.I_NAME "
                    + " "
                    + "  "
                    + " "
                    + "  "
                    + " "
                    + "UNION ALL "
                    + " "
                    + "SELECT INCHARGE_CD,INCHARGE_NAME,COALESCE(BUDGET_TOTAL,0) AS BUDGET_TOTAL,COALESCE(BUDGET_KG,0) AS BUDGET_KG,COALESCE(BUDGET_SQMTR,0) AS BUDGET_SQMTR,COALESCE(BUDGET_AMOUNT,0) AS BUDGET_AMOUNT, DAYCNT,"
                    + "DAYINV,DAYKG,DAYSQMTR,DECCNT,DECINV,DECKG,DECSQMTR,TOTALCNT,TOTAL_INVAMT,INVKG,INVSQMTR FROM ("
                    + "SELECT 'GRAND TOTAL' AS INCHARGE_CD, 'GRAND TOTAL' AS INCHARGE_NAME, "
                    + "SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW() "
                    + ",INTERVAL 1 DAY),1,10) THEN 1 ELSE 0 END) AS  DAYCNT , "
                    + " "
                    + "SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW() "
                    + ",INTERVAL 1 DAY),1,10) THEN INVOICE_AMT ELSE 0 END) AS  DAYINV , "
                    + " "
                    + "SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW() "
                    + ",INTERVAL 1 DAY),1,10)  AND RATE_UNIT ='KG' THEN ACTUAL_WEIGHT ELSE 0 END) "
                    + "AS  DAYKG, "
                    + " "
                    + "SUM(CASE WHEN SUBSTRING(INVOICE_DATE,1,10) = SUBSTRING(DATE_SUB(NOW() "
                    + ",INTERVAL 1 DAY),1,10)  AND RATE_UNIT ='MTR' THEN SQMTR ELSE 0 END) AS "
                    + "DAYSQMTR , "
                    + " "
                    + "  "
                    + " "
                    + "  "
                    + " "
                    + "SUM(CASE WHEN EXTRACT( MONTH FROM INVOICE_DATE) = EXTRACT(MONTH FROM SUBSTRING(DATE_SUB(NOW() ,INTERVAL 1 DAY),1,10)) THEN 1 ELSE 0 END) AS "
                    + "DECCNT , "
                    + " "
                    + "SUM(CASE WHEN EXTRACT( MONTH FROM INVOICE_DATE) = EXTRACT(MONTH FROM SUBSTRING(DATE_SUB(NOW() ,INTERVAL 1 DAY),1,10)) THEN INVOICE_AMT ELSE 0 "
                    + "END) AS  DECINV , "
                    + " "
                    + "SUM(CASE WHEN EXTRACT( MONTH FROM INVOICE_DATE) = EXTRACT(MONTH FROM SUBSTRING(DATE_SUB(NOW() ,INTERVAL 1 DAY),1,10)) AND RATE_UNIT ='KG' "
                    + "THEN ACTUAL_WEIGHT ELSE 0 END) AS  DECKG , "
                    + " "
                    + "SUM(CASE WHEN EXTRACT( MONTH FROM INVOICE_DATE) = EXTRACT(MONTH FROM SUBSTRING(DATE_SUB(NOW() ,INTERVAL 1 DAY),1,10)) AND RATE_UNIT ='MTR' "
                    + "THEN SQMTR ELSE 0 END) AS  DECSQMTR, "
                    + " "
                    + "  "
                    + " "
                    + "  "
                    + " "
                    + "  "
                    + " "
                    + "COUNT(INVOICE_NO) AS TOTALCNT, "
                    + " "
                    + "SUM(INVOICE_AMT) AS TOTAL_INVAMT, "
                    + " "
                    + "SUM(CASE WHEN RATE_UNIT ='KG' THEN ACTUAL_WEIGHT ELSE 0 END) AS  INVKG , "
                    + " "
                    + "SUM(CASE WHEN RATE_UNIT ='MTR' THEN SQMTR ELSE 0 END) AS  INVSQMTR "
                    + " "
                    + "  "
                    + " "
                    + "  "
                    + " "
                    + "FROM "
                    + " "
                    + "PRODUCTION.FELT_SAL_INVOICE_HEADER H , DINESHMILLS.D_SAL_PARTY_MASTER "
                    + "P,PRODUCTION.FELT_INCHARGE I,PRODUCTION.FELT_QLT_RATE_MASTER Q "
                    + "WHERE H.APPROVED=1 AND H.CANCELLED=0 AND INVOICE_DATE >='" + InvFromDateDB + "'  AND INVOICE_DATE <='" + InvToDateDB + "'  AND "
                    + "H.PARTY_CODE = P.PARTY_CODE AND I.INCHARGE_CD = P.INCHARGE_CD AND "
                    + "Q.PRODUCT_CODE = H.PRODUCT_CODE "
                    + " "
                    + "AND EFFECTIVE_TO ='0000-00-00' "
                    + ") AS BBB "
                    + "LEFT JOIN (SELECT 'GRAND TOTAL' AS FELT_CAT, 'GRAND TOTAL'  AS I_NAME,SUM(TOTAL) AS BUDGET_TOTAL,SUM(TOTAL_KG) AS BUDGET_KG,SUM(TOTAL_SQMTR) AS BUDGET_SQMTR,SUM(NET_AMOUNT) AS BUDGET_AMOUNT "
                    + "FROM PRODUCTION.FELT_BUDGET "
                    + "WHERE YEAR_FROM=" + FromYear + " AND YEAR_TO=" + ToYear + "  ) AS BUDGET ON BBB.INCHARGE_NAME=BUDGET.I_NAME "
                    + "ORDER BY INCHARGE_CD,INCHARGE_NAME ";

            pMessage = pMessage + "<br><br><br><b>3. Zonewise Total</b>";

            System.out.println("SQL 123" + strSQL);
            rsData3 = stmt3.executeQuery(strSQL);
            ResultSetMetaData rsmd3 = rsData3.getMetaData();

            pMessage = pMessage + "<br><br><table border=1>";
            pMessage = pMessage + "<tr style='font-weight:bold'>";
            pMessage = pMessage + "<td colspan=2 align=center> ZONE </td>";
            pMessage = pMessage + "<td colspan=4 align=center> BUDGET </td>";
            pMessage = pMessage + "<td colspan=4 align=center> " + data.getStringValueFromDB("SELECT DATE_FORMAT(DATE_SUB(CURDATE(),INTERVAL 1 DAY),'%d %b %Y')") + "</td>";
            pMessage = pMessage + "<td colspan=4 align=center> Monthly Total Invoices Till Date</td>";
            pMessage = pMessage + "<td colspan=4 align=center> Yearly Total Invoices Till Date</td>";
            pMessage = pMessage + "</tr>";
            pMessage = pMessage + "<tr style='font-weight:bold'>";
            pMessage = pMessage + "<td align=center> CODE </td>";
            pMessage = pMessage + "<td align=center> NAME </td>";
            pMessage = pMessage + "<td align=center> Nos. </td>";
            pMessage = pMessage + "<td align=center> Kg </td>";
            pMessage = pMessage + "<td align=center> Sqmtr </td>";
            pMessage = pMessage + "<td align=center> Amount </td>";
            pMessage = pMessage + "<td align=center> NO. </td>";
            pMessage = pMessage + "<td align=center> AMOUNT </td>";
            pMessage = pMessage + "<td align=center> KGS </td>";
            pMessage = pMessage + "<td align=center> SQMTR </td>";
            pMessage = pMessage + "<td align=center> NO. </td>";
            pMessage = pMessage + "<td align=center> AMOUNT </td>";
            pMessage = pMessage + "<td align=center> KGS </td>";
            pMessage = pMessage + "<td align=center> SQMTR </td>";
            pMessage = pMessage + "<td align=center> NO. </td>";
            pMessage = pMessage + "<td align=center> AMOUNT </td>";
            pMessage = pMessage + "<td align=center> KGS </td>";
            pMessage = pMessage + "<td align=center> SQMTR </td>";
            pMessage = pMessage + "</tr>";
            //, , , , , , , , , FEBINV, JANCNT, JANINV, DECCNT, DECINV, NOVCNT, NOVINV, OCTCNT, OCTINV, SEPCNT, SEPINV, AUGCNT, AUGINV, JULCNT, JULINV, JUNCNT, JUNINV, MAYCNT, MAYINV, APRCNT, APRINV

            System.out.println("");
            rsData3.first();
            if (rsData3.getRow() > 0) {
                while (!rsData3.isAfterLast()) {
                    pMessage = pMessage + "<tr>";
                    for (int i = 1; i <= rsmd3.getColumnCount(); i++) {
                        if (i > 2) {
                            pMessage = pMessage + "<td align=right>" + rsData3.getString(i) + "</td>";
                        } else {
                            pMessage = pMessage + "<td>" + rsData3.getString(i) + "</td>";
                        }
                    }
                    pMessage = pMessage + "</tr>";

                    rsData3.next();
                }
            }
            pMessage = pMessage + "</table></font>";

            pMessage = pMessage + "<br><br><br><b>NOTE : Category Description</b>";

            Statement stmt4;
            ResultSet rsData4;
            stmt4 = Conn3.createStatement();
            strSQL = "SELECT "
                    + " "
                    + "CASE WHEN FELT_CATG = 'PRESS' THEN 1 "
                    + " "
                    + "WHEN FELT_CATG = 'ACNE/FCNE' THEN 2 "
                    + " "
                    + "WHEN FELT_CATG = 'HDS' THEN 3 "
                    + " "
                    + "WHEN FELT_CATG = 'SDF' THEN 4 "
                    + " "
                    + "WHEN FELT_CATG = 'OTHERS' THEN 5 END AS CATG_NO,FELT_CATG, "
                    + " "
                    + "GROUP_CONCAT(DISTINCT GROUP_NAME ORDER BY GROUP_NAME) AS PRD_GRP , "
                    + " "
                    + "GROUP_CONCAT(PRODUCT_CODE ORDER BY PRODUCT_CODE) AS PRD "
                    + " "
                    + "  "
                    + " "
                    + "FROM PRODUCTION.FELT_QLT_RATE_MASTER "
                    + " "
                    + "GROUP BY FELT_CATG "
                    + " "
                    + "ORDER BY CATG_NO";
            System.out.println("SQL " + strSQL);
            rsData4 = stmt4.executeQuery(strSQL);
            ResultSetMetaData rsmd4 = rsData4.getMetaData();

            pMessage = pMessage + "<br><br><table border=1  style='font-weight:bold; font-size:10px;'>";
            pMessage = pMessage + "<tr style='font-weight:bold'>";
            pMessage = pMessage + "<td align=center> CATG NO </td>";
            pMessage = pMessage + "<td align=center> FELT CATEGORY </td>";
            pMessage = pMessage + "<td style='max-width: 200px;' align=center> PRODUCT GROUP</td>";
            pMessage = pMessage + "<td style='max-width: 200px;' align=center> PRODUCT CODE</td>";
            pMessage = pMessage + "</tr>";

            System.out.println("");
            rsData4.first();
            if (rsData4.getRow() > 0) {
                while (!rsData4.isAfterLast()) {
                    pMessage = pMessage + "<tr>";
                    for (int i = 1; i <= rsmd4.getColumnCount(); i++) {
                        if (i == 1) {
                            pMessage = pMessage + "<td align=right>" + rsData4.getString(i) + "</td>";
                        } else {
                            pMessage = pMessage + "<td>" + rsData4.getString(i) + "</td>";
                        }

                    }
                    pMessage = pMessage + "</tr>";

                    rsData4.next();
                }
            }
            pMessage = pMessage + "</table></font>";

            System.out.println("MESSAGE : " + pMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void InvFinYear() {
        String CurDate = EITLERPGLOBAL.getCurrentDate();

        if (CurDate.substring(3, 5).endsWith("01") || CurDate.substring(3, 5).endsWith("02") || CurDate.substring(3, 5).endsWith("03")) {
            FromYear = Integer.parseInt(CurDate.substring(6, 10)) - 1;
        } else {
            FromYear = Integer.parseInt(CurDate.substring(6, 10));
        }
        System.out.println("From Year : " + FromYear);

        if (CurDate.substring(3, 5).endsWith("01") || CurDate.substring(3, 5).endsWith("02") || CurDate.substring(3, 5).endsWith("03")) {
            ToYear = Integer.parseInt(CurDate.substring(6, 10));
        } else {
            ToYear = Integer.parseInt(CurDate.substring(6, 10)) + 1;
        }
        System.out.println("To Year : " + ToYear);

        InvFromDateDB = FromYear + "-04-01";
        InvToDateDB = ToYear + "-03-31";
        
        if(CurDate.startsWith("01/04"))
        {
            FromYear = FromYear-1;
            ToYear = ToYear-1;
            InvFromDateDB = FromYear + "-04-01";
            InvToDateDB = ToYear + "-03-31";
        }
    }
}
