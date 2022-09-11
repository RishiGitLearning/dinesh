/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP;

import EITLERP.FeltSales.*;
import EITLERP.EITLERPGLOBAL;
import EITLERP.clsSales_Party;
import EITLERP.data;
import java.sql.*;
import java.util.*;

/**
 *
 * @author root
 */
public class PendingDocMoreThan2Days {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ProductionList();
        FeltList();
        TimeAttendanceList();
    }

    private static void ProductionList() {
        try {

            String pSubject = "Production Modules Pending document More than 2 days";

            String pMessage = "";
            String cc = "";
            int SrNo = 1;

            pMessage = pMessage + "<br>Dear Sir,<br>";
            pMessage = pMessage + "<br>Production Modules Pending document More than 2 days mention below.<br><br>";

            Connection Conn;
            Statement stmt;
            ResultSet rsData;

            Conn = data.getConn();
            stmt = Conn.createStatement();
            String strSQL = "SELECT MODULE_ID,MODULE_DESC,DOC_NO,DOC_DATE,USER_NAME,DATE(RECEIVED_DATE) AS RECEIVED_DATE,DATEDIFF1 AS DATEDIFF_FROM_USER_RECEIVED,DATEDIFF2 "
                    + "DATEDIFF_FROM_DOCUMENT_PREPARED FROM  "
                    + "(SELECT MODULE_DEPT,D.MODULE_ID,MODULE_DESC,DOC_NO,DOC_DATE,D.USER_ID,USER_NAME,RECEIVED_DATE,SUPERIOR_ID,D.REMARKS, "
                    + "DATEDIFF(NOW(),RECEIVED_DATE) AS DATEDIFF1, "
                    + "DATEDIFF(NOW(),DOC_DATE) AS DATEDIFF2, "
                    + "TIMEDIFF(NOW(),RECEIVED_DATE) AS TIMEDIFF1, "
                    + "CASE WHEN DATEDIFF(NOW(),RECEIVED_DATE) >0 THEN DATEDIFF(NOW(),RECEIVED_DATE) ELSE TIMEDIFF(NOW(),RECEIVED_DATE) END AS DIFF3 "
                    + "FROM PRODUCTION.FELT_PROD_DOC_DATA D,DINESHMILLS.D_COM_MODULES M,DINESHMILLS.D_COM_USER_MASTER U WHERE STATUS ='W'  AND D.MODULE_ID = M.MODULE_ID AND U.USER_ID = D.USER_ID AND DOC_DATE >= '2017-07-01'     AND D.USER_ID NOT IN (399) "
                    + "AND MODULE_DEPT IN ('PRODUCTION','FELT DESIGN') "
                    + "AND DATEDIFF(NOW(),RECEIVED_DATE) > 2 "
                    + "UNION ALL "
                    + "SELECT MODULE_DEPT,D.MODULE_ID,CONCAT(MODULE_DESC,'(TOTAL)') AS MODULE_DESC,COUNT(DOC_NO),'TOTAL' AS DOC_DATE,D.USER_ID,USER_NAME,'TOTAL' AS RECEIVED_DATE,'TOTAL' AS SUPERIOR_ID,'TOTAL' AS REMARKS, "
                    + "'TOTAL','TOTAL','TOTAL','TOTAL' "
                    + "FROM PRODUCTION.FELT_PROD_DOC_DATA D,DINESHMILLS.D_COM_MODULES M,DINESHMILLS.D_COM_USER_MASTER U WHERE STATUS ='W'  AND D.MODULE_ID = M.MODULE_ID AND U.USER_ID = D.USER_ID AND DOC_DATE >= '2017-07-01'  AND D.USER_ID NOT IN (399) "
                    + "AND MODULE_DEPT IN ('PRODUCTION','FELT DESIGN') "
                    + "AND DATEDIFF(NOW(),RECEIVED_DATE) > 2 "
                    + "GROUP BY  MODULE_DEPT,D.MODULE_ID,MODULE_DESC,D.USER_ID,USER_NAME) AS M "
                    + "ORDER BY USER_ID,MODULE_DESC DESC";

            rsData = stmt.executeQuery(strSQL);
            rsData.first();

//            System.out.println("String StrSQL : " + strSQL);
            if (rsData.getRow() > 0) {
                pMessage = pMessage + "<table border='1' cellpadding='10'>"
                        + "<tr>"
                        + "<th align='center'> SrNo </th>"
                        + "<th align='center'> User Name </th>"
                        + "<th align='center'> Module Name </th>"
                        + "<th align='center'> Doc No </th>"
                        + "<th align='center'> Doc Date </th>"
                        + "<th align='center'> Received Date </th>"
                        + "<th align='center'> Pending since Received </th>"
                        + "<th align='center'> Pending since Created </th>"
                        + "</tr>";

                rsData.first();
                if (rsData.getRow() > 0) {
                    while (!rsData.isAfterLast()) {

                        pMessage = pMessage + ""
                                + "<tr>"
                                + "<td align='right'> " + (SrNo++) + " </td>"
                                + "<td align='left'> " + rsData.getString("USER_NAME") + " </td>"
                                + "<td align='left'> " + rsData.getString("MODULE_DESC") + " </td>"
                                + "<td align='left'> " + rsData.getString("DOC_NO") + " </td>";
//                                + "<td align='right'> " + rsData.getString("DOC_DATE") + " </td>"
//                                + "<td align='right'> " + rsData.getString("RECEIVED_DATE") + " </td>"
//                                + "<td align='right'> " + rsData.getString("DATEDIFF_FROM_USER_RECEIVED") + " </td>"
//                                + "<td align='right'> " + rsData.getString("DATEDIFF_FROM_DOCUMENT_PREPARED") + " </td>"
                        if (rsData.getString("DOC_DATE") == null || rsData.getString("DOC_DATE").equalsIgnoreCase("TOTAL")) {
                            pMessage = pMessage + "<td align='right'>  </td>";
                        } else {
                            pMessage = pMessage + "<td align='right'> " + EITLERPGLOBAL.formatDate(rsData.getString("DOC_DATE")) + " </td>";
                        }
                        if (rsData.getString("RECEIVED_DATE") == null || rsData.getString("RECEIVED_DATE").equalsIgnoreCase("TOTAL")) {
                            pMessage = pMessage + "<td align='right'>  </td>";
                        } else {
                            pMessage = pMessage + "<td align='right'> " + EITLERPGLOBAL.formatDate(rsData.getString("RECEIVED_DATE")) + " </td>";
                        }
                        if (rsData.getString("DATEDIFF_FROM_USER_RECEIVED") == null || rsData.getString("DATEDIFF_FROM_USER_RECEIVED").equalsIgnoreCase("TOTAL")) {
                            pMessage = pMessage + "<td align='right'>  </td>";
                        } else {
                            pMessage = pMessage + "<td align='right'> " + rsData.getString("DATEDIFF_FROM_USER_RECEIVED") + " </td>";
                        }
                        if (rsData.getString("DATEDIFF_FROM_DOCUMENT_PREPARED") == null || rsData.getString("DATEDIFF_FROM_DOCUMENT_PREPARED").equalsIgnoreCase("TOTAL")) {
                            pMessage = pMessage + "<td align='right'>  </td>";
                        } else {
                            pMessage = pMessage + "<td align='right'> " + rsData.getString("DATEDIFF_FROM_DOCUMENT_PREPARED") + " </td>";
                        }
                        pMessage = pMessage + "</tr>";

                        rsData.next();
                    }
                }
                pMessage = pMessage + "</table>";
            } else {
                pMessage = pMessage + "<br>Found no pending document.<br>";
            }

            pMessage += "<br>";

//            String recievers = "rishineekhra@dineshmills.com";
//            String recievers = "gaurang@dineshmills.com";
            String recievers = "aditya@dineshmills.com,abtewary@dineshmills.com,yrpatel@dineshmills.com,amitkanti@dineshmills.com,sdmlerp@dineshmills.com";

            pMessage = pMessage + "<br><br>**** This is an auto-generated email, please do not reply ****";

//            System.out.println("Recivers : " + recievers);
//            System.out.println("pSubject : " + pSubject);
//            System.out.println("pMessage : " + pMessage);
            String responce = DailyMailNotification.sendNotificationMail(pSubject, pMessage, recievers, cc);
            System.out.println("Send Mail Responce : " + responce);

        } catch (Exception e) {
            System.out.println("Error on Mail: " + e.getMessage());
        }
    }

    private static void FeltList() {
        try {

            String pSubject = "Felt Modules Pending document More than 2 days";

            String pMessage = "";
            String cc = "";
            int SrNo = 1;

            pMessage = pMessage + "<br>Dear Sir,<br>";
            pMessage = pMessage + "<br>Felt Modules Pending document More than 2 days mention below.<br><br>";

            Connection Conn;
            Statement stmt;
            ResultSet rsData;

            Conn = data.getConn();
            stmt = Conn.createStatement();
            String strSQL = "SELECT MODULE_ID,MODULE_DESC,DOC_NO,DOC_DATE,USER_NAME,DATE(RECEIVED_DATE) AS RECEIVED_DATE,DATEDIFF1 AS DATEDIFF_FROM_USER_RECEIVED,DATEDIFF2 "
                    + "DATEDIFF_FROM_DOCUMENT_PREPARED FROM  "
                    + "(SELECT MODULE_DEPT,D.MODULE_ID,MODULE_DESC,DOC_NO,DOC_DATE,D.USER_ID,USER_NAME,RECEIVED_DATE,SUPERIOR_ID,D.REMARKS, "
                    + "DATEDIFF(NOW(),RECEIVED_DATE) AS DATEDIFF1, "
                    + "DATEDIFF(NOW(),DOC_DATE) AS DATEDIFF2, "
                    + "TIMEDIFF(NOW(),RECEIVED_DATE) AS TIMEDIFF1, "
                    + "CASE WHEN DATEDIFF(NOW(),RECEIVED_DATE) >0 THEN DATEDIFF(NOW(),RECEIVED_DATE) ELSE TIMEDIFF(NOW(),RECEIVED_DATE) END AS DIFF3 "
                    + "FROM PRODUCTION.FELT_PROD_DOC_DATA D,DINESHMILLS.D_COM_MODULES M,DINESHMILLS.D_COM_USER_MASTER U WHERE STATUS ='W'  AND D.MODULE_ID = M.MODULE_ID AND U.USER_ID = D.USER_ID AND DOC_DATE >= '2017-07-01'     AND D.USER_ID NOT IN (399) "
                    + "AND MODULE_DEPT = 'FELT SALES' "
                    + "AND DATEDIFF(NOW(),RECEIVED_DATE) > 2 "
                    + "UNION ALL "
                    + "SELECT MODULE_DEPT,D.MODULE_ID,CONCAT(MODULE_DESC,'(TOTAL)') AS MODULE_DESC,COUNT(DOC_NO),'TOTAL' AS DOC_DATE,D.USER_ID,USER_NAME,'TOTAL' AS RECEIVED_DATE,'TOTAL' AS SUPERIOR_ID,'TOTAL' AS REMARKS, "
                    + "'TOTAL','TOTAL','TOTAL','TOTAL' "
                    + "FROM PRODUCTION.FELT_PROD_DOC_DATA D,DINESHMILLS.D_COM_MODULES M,DINESHMILLS.D_COM_USER_MASTER U WHERE STATUS ='W'  AND D.MODULE_ID = M.MODULE_ID AND U.USER_ID = D.USER_ID AND DOC_DATE >= '2017-07-01'  AND D.USER_ID NOT IN (399) "
                    + "AND MODULE_DEPT = 'FELT SALES' "
                    + "AND DATEDIFF(NOW(),RECEIVED_DATE) > 2 "
                    + "GROUP BY  MODULE_DEPT,D.MODULE_ID,MODULE_DESC,D.USER_ID,USER_NAME) AS M "
                    + "ORDER BY USER_ID,MODULE_DESC DESC";

            rsData = stmt.executeQuery(strSQL);
            rsData.first();

//            System.out.println("String StrSQL : " + strSQL);
            if (rsData.getRow() > 0) {
                pMessage = pMessage + "<table border='1' cellpadding='10'>"
                        + "<tr>"
                        + "<th align='center'> SrNo </th>"
                        + "<th align='center'> User Name </th>"
                        + "<th align='center'> Module Name </th>"
                        + "<th align='center'> Doc No </th>"
                        + "<th align='center'> Doc Date </th>"
                        + "<th align='center'> Received Date </th>"
                        + "<th align='center'> Pending since Received </th>"
                        + "<th align='center'> Pending since Created </th>"
                        + "</tr>";

                rsData.first();
                if (rsData.getRow() > 0) {
                    while (!rsData.isAfterLast()) {

                        pMessage = pMessage + ""
                                + "<tr>"
                                + "<td align='right'> " + (SrNo++) + " </td>"
                                + "<td align='left'> " + rsData.getString("USER_NAME") + " </td>"
                                + "<td align='left'> " + rsData.getString("MODULE_DESC") + " </td>"
                                + "<td align='left'> " + rsData.getString("DOC_NO") + " </td>";
//                                + "<td align='right'> " + rsData.getString("DOC_DATE") + " </td>"
//                                + "<td align='right'> " + rsData.getString("RECEIVED_DATE") + " </td>"
//                                + "<td align='right'> " + rsData.getString("DATEDIFF_FROM_USER_RECEIVED") + " </td>"
//                                + "<td align='right'> " + rsData.getString("DATEDIFF_FROM_DOCUMENT_PREPARED") + " </td>"
                        if (rsData.getString("DOC_DATE") == null || rsData.getString("DOC_DATE").equalsIgnoreCase("TOTAL")) {
                            pMessage = pMessage + "<td align='right'>  </td>";
                        } else {
                            pMessage = pMessage + "<td align='right'> " + EITLERPGLOBAL.formatDate(rsData.getString("DOC_DATE")) + " </td>";
                        }
                        if (rsData.getString("RECEIVED_DATE") == null || rsData.getString("RECEIVED_DATE").equalsIgnoreCase("TOTAL")) {
                            pMessage = pMessage + "<td align='right'>  </td>";
                        } else {
                            pMessage = pMessage + "<td align='right'> " + EITLERPGLOBAL.formatDate(rsData.getString("RECEIVED_DATE")) + " </td>";
                        }
                        if (rsData.getString("DATEDIFF_FROM_USER_RECEIVED") == null || rsData.getString("DATEDIFF_FROM_USER_RECEIVED").equalsIgnoreCase("TOTAL")) {
                            pMessage = pMessage + "<td align='right'>  </td>";
                        } else {
                            pMessage = pMessage + "<td align='right'> " + rsData.getString("DATEDIFF_FROM_USER_RECEIVED") + " </td>";
                        }
                        if (rsData.getString("DATEDIFF_FROM_DOCUMENT_PREPARED") == null || rsData.getString("DATEDIFF_FROM_DOCUMENT_PREPARED").equalsIgnoreCase("TOTAL")) {
                            pMessage = pMessage + "<td align='right'>  </td>";
                        } else {
                            pMessage = pMessage + "<td align='right'> " + rsData.getString("DATEDIFF_FROM_DOCUMENT_PREPARED") + " </td>";
                        }
                        pMessage = pMessage + "</tr>";

                        rsData.next();
                    }
                }
                pMessage = pMessage + "</table>";
            } else {
                pMessage = pMessage + "<br>Found no pending document.<br>";
            }

            pMessage += "<br>";

//            String recievers = "rishineekhra@dineshmills.com";
//            String recievers = "gaurang@dineshmills.com";
            String recievers = "aditya@dineshmills.com,vdshanbhag@dineshmills.com,soumen@dineshmills.com,yrpatel@dineshmills.com,amitkanti@dineshmills.com,sdmlerp@dineshmills.com";

            pMessage = pMessage + "<br><br>**** This is an auto-generated email, please do not reply ****";

//            System.out.println("Recivers : " + recievers);
//            System.out.println("pSubject : " + pSubject);
//            System.out.println("pMessage : " + pMessage);
            String responce = DailyMailNotification.sendNotificationMail(pSubject, pMessage, recievers, cc);
            System.out.println("Send Mail Responce : " + responce);

        } catch (Exception e) {
            System.out.println("Error on Mail: " + e.getMessage());
        }
    }

    private static void TimeAttendanceList() {
        try {

            String pSubject = "Time Attendance Modules Pending document More than 2 days";

            String pMessage = "";
            String cc = "";
            int SrNo = 1;

            pMessage = pMessage + "<br>Dear Sir,<br>";
            pMessage = pMessage + "<br>Time Attendance Modules Pending document More than 2 days mention below.<br><br>";

            Connection Conn;
            Statement stmt;
            ResultSet rsData;

            Conn = data.getConn();
            stmt = Conn.createStatement();
            String strSQL = "SELECT MODULE_ID,MODULE_DESC,DOC_NO,DOC_DATE,USER_NAME,DATE(RECEIVED_DATE) AS RECEIVED_DATE,DATEDIFF1 AS DATEDIFF_FROM_USER_RECEIVED,DATEDIFF2 "
                    + "DATEDIFF_FROM_DOCUMENT_PREPARED FROM  "
                    + "(SELECT MODULE_DEPT,D.MODULE_ID,MODULE_DESC,DOC_NO,DOC_DATE,D.USER_ID,USER_NAME,RECEIVED_DATE,SUPERIOR_ID,D.REMARKS, "
                    + "DATEDIFF(NOW(),RECEIVED_DATE) AS DATEDIFF1, "
                    + "DATEDIFF(NOW(),DOC_DATE) AS DATEDIFF2, "
                    + "TIMEDIFF(NOW(),RECEIVED_DATE) AS TIMEDIFF1, "
                    + "CASE WHEN DATEDIFF(NOW(),RECEIVED_DATE) >0 THEN DATEDIFF(NOW(),RECEIVED_DATE) ELSE TIMEDIFF(NOW(),RECEIVED_DATE) END AS DIFF3 "
                    + "FROM SDMLATTPAY.D_COM_DOC_DATA D,DINESHMILLS.D_COM_MODULES M,DINESHMILLS.D_COM_USER_MASTER U WHERE STATUS ='W'  AND D.MODULE_ID = M.MODULE_ID AND U.USER_ID = D.USER_ID AND DOC_DATE >= '2017-07-01'     AND D.USER_ID NOT IN (399) "
                    + "AND MODULE_DEPT = 'TIME_ATTENDANCE' "
                    + "AND DATEDIFF(NOW(),RECEIVED_DATE) > 2 "
                    + "UNION ALL "
                    + "SELECT MODULE_DEPT,D.MODULE_ID,CONCAT(MODULE_DESC,'(TOTAL)') AS MODULE_DESC,COUNT(DOC_NO),'TOTAL' AS DOC_DATE,D.USER_ID,USER_NAME,'TOTAL' AS RECEIVED_DATE,'TOTAL' AS SUPERIOR_ID,'TOTAL' AS REMARKS, "
                    + "'TOTAL','TOTAL','TOTAL','TOTAL' "
                    + "FROM SDMLATTPAY.D_COM_DOC_DATA D,DINESHMILLS.D_COM_MODULES M,DINESHMILLS.D_COM_USER_MASTER U WHERE STATUS ='W'  AND D.MODULE_ID = M.MODULE_ID AND U.USER_ID = D.USER_ID AND DOC_DATE >= '2017-07-01'  AND D.USER_ID NOT IN (399) "
                    + "AND MODULE_DEPT = 'TIME_ATTENDANCE' "
                    + "AND DATEDIFF(NOW(),RECEIVED_DATE) > 2 "
                    + "GROUP BY  MODULE_DEPT,D.MODULE_ID,MODULE_DESC,D.USER_ID,USER_NAME) AS M "
                    + "ORDER BY USER_ID,MODULE_DESC DESC";

            rsData = stmt.executeQuery(strSQL);
            rsData.first();

//            System.out.println("String StrSQL : " + strSQL);
            if (rsData.getRow() > 0) {
                pMessage = pMessage + "<table border='1' cellpadding='10'>"
                        + "<tr>"
                        + "<th align='center'> SrNo </th>"
                        + "<th align='center'> User Name </th>"
                        + "<th align='center'> Module Name </th>"
                        + "<th align='center'> Doc No </th>"
                        + "<th align='center'> Doc Date </th>"
                        + "<th align='center'> Received Date </th>"
                        + "<th align='center'> Pending since Received </th>"
                        + "<th align='center'> Pending since Created </th>"
                        + "</tr>";

                rsData.first();
                if (rsData.getRow() > 0) {
                    while (!rsData.isAfterLast()) {

                        pMessage = pMessage + ""
                                + "<tr>"
                                + "<td align='right'> " + (SrNo++) + " </td>"
                                + "<td align='left'> " + rsData.getString("USER_NAME") + " </td>"
                                + "<td align='left'> " + rsData.getString("MODULE_DESC") + " </td>"
                                + "<td align='left'> " + rsData.getString("DOC_NO") + " </td>";
//                                + "<td align='right'> " + rsData.getString("DOC_DATE") + " </td>"
//                                + "<td align='right'> " + rsData.getString("RECEIVED_DATE") + " </td>"
//                                + "<td align='right'> " + rsData.getString("DATEDIFF_FROM_USER_RECEIVED") + " </td>"
//                                + "<td align='right'> " + rsData.getString("DATEDIFF_FROM_DOCUMENT_PREPARED") + " </td>"
                        if (rsData.getString("DOC_DATE") == null || rsData.getString("DOC_DATE").equalsIgnoreCase("TOTAL")) {
                            pMessage = pMessage + "<td align='right'>  </td>";
                        } else {
                            pMessage = pMessage + "<td align='right'> " + EITLERPGLOBAL.formatDate(rsData.getString("DOC_DATE")) + " </td>";
                        }
                        if (rsData.getString("RECEIVED_DATE") == null || rsData.getString("RECEIVED_DATE").equalsIgnoreCase("TOTAL")) {
                            pMessage = pMessage + "<td align='right'>  </td>";
                        } else {
                            pMessage = pMessage + "<td align='right'> " + EITLERPGLOBAL.formatDate(rsData.getString("RECEIVED_DATE")) + " </td>";
                        }
                        if (rsData.getString("DATEDIFF_FROM_USER_RECEIVED") == null || rsData.getString("DATEDIFF_FROM_USER_RECEIVED").equalsIgnoreCase("TOTAL")) {
                            pMessage = pMessage + "<td align='right'>  </td>";
                        } else {
                            pMessage = pMessage + "<td align='right'> " + rsData.getString("DATEDIFF_FROM_USER_RECEIVED") + " </td>";
                        }
                        if (rsData.getString("DATEDIFF_FROM_DOCUMENT_PREPARED") == null || rsData.getString("DATEDIFF_FROM_DOCUMENT_PREPARED").equalsIgnoreCase("TOTAL")) {
                            pMessage = pMessage + "<td align='right'>  </td>";
                        } else {
                            pMessage = pMessage + "<td align='right'> " + rsData.getString("DATEDIFF_FROM_DOCUMENT_PREPARED") + " </td>";
                        }
                        pMessage = pMessage + "</tr>";

                        rsData.next();
                    }
                }
                pMessage = pMessage + "</table>";
            } else {
                pMessage = pMessage + "<br>Found no pending document.<br>";
            }

            pMessage += "<br>";

//            String recievers = "rishineekhra@dineshmills.com";
//            String recievers = "gaurang@dineshmills.com";
            String recievers = "aditya@dineshmills.com,sunil@dineshmills.com,manhardave@dineshmills.com,vdshanbhag@dineshmills.com,soumen@dineshmills.com,sdmlerp@dineshmills.com";

            pMessage = pMessage + "<br><br>**** This is an auto-generated email, please do not reply ****";

//            System.out.println("Recivers : " + recievers);
//            System.out.println("pSubject : " + pSubject);
//            System.out.println("pMessage : " + pMessage);
            String responce = DailyMailNotification.sendNotificationMail(pSubject, pMessage, recievers, cc);
            System.out.println("Send Mail Responce : " + responce);

        } catch (Exception e) {
            System.out.println("Error on Mail: " + e.getMessage());
        }
    }

}
