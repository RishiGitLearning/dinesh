/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP;

import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.DailyMailNotification;
import EITLERP.clsSales_Party;
import EITLERP.data;
import java.sql.*;
import java.util.*;

/**
 *
 * @author root
 */
public class clsDailyPendingDocNotification {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        PendingDocList();
    }

    private static void PendingDocList() {
        try {

            String userId = "", userName = "", emailId = "";
            String yearFrom = data.getStringValueFromDB("SELECT MIN(YEAR_FROM) FROM DINESHMILLS.D_COM_FIN_YEAR WHERE OPEN_STATUS='O' ");

            Connection Conn, ConnD;
            Statement stmt, stmtD;
            ResultSet rsData, rsDataD;

            Conn = data.getConn();
            stmt = Conn.createStatement();
            rsData = stmt.executeQuery("SELECT USER_ID,UPPER(USER_NAME) AS USER_NAME,EXTERNAL_EMAIL,MODULE_ID,MODULE_DESC,COUNT(*) AS PENDING_DOC "
                    + "FROM ( "
                    + "SELECT D.MODULE_ID,MODULE_DESC,DOC_NO,DOC_DATE,D.USER_ID,USER_NAME,EXTERNAL_EMAIL,RECEIVED_DATE,D.REMARKS AS RUN_DATE "
                    + "FROM PRODUCTION.FELT_PROD_DOC_DATA D,DINESHMILLS.D_COM_MODULES M,DINESHMILLS.D_COM_USER_MASTER U "
                    + "WHERE STATUS ='W'  AND D.MODULE_ID = M.MODULE_ID AND U.USER_ID = D.USER_ID "
                    + "AND (DOC_DATE>='" + yearFrom + "-04-01' OR (DOC_DATE='0000-00-00' AND D.CHANGED_DATE>='" + yearFrom + "-04-01')) "
                    + "UNION ALL "
                    + "SELECT D.MODULE_ID,MODULE_DESC,DOC_NO,DOC_DATE,D.USER_ID,USER_NAME,EXTERNAL_EMAIL,RECEIVED_DATE,D.REMARKS AS RUN_DATE "
                    + "FROM DINESHMILLS.D_COM_DOC_DATA D,DINESHMILLS.D_COM_MODULES M,DINESHMILLS.D_COM_USER_MASTER U "
                    + "WHERE STATUS ='W'  AND D.MODULE_ID = M.MODULE_ID AND U.USER_ID = D.USER_ID AND D.MODULE_ID NOT IN (63) "
                    + "AND (DOC_DATE>='" + yearFrom + "-04-01' OR (DOC_DATE='0000-00-00' AND D.CHANGED_DATE>='" + yearFrom + "-04-01')) "
                    + "UNION ALL "
                    + "SELECT D.MODULE_ID,MODULE_DESC,DOC_NO,DOC_DATE,D.USER_ID,USER_NAME,EXTERNAL_EMAIL,RECEIVED_DATE,D.REMARKS AS RUN_DATE "
                    + "FROM SDMLATTPAY.D_COM_DOC_DATA D,DINESHMILLS.D_COM_MODULES M,DINESHMILLS.D_COM_USER_MASTER U "
                    + "WHERE STATUS ='W'  AND D.MODULE_ID = M.MODULE_ID AND U.USER_ID = D.USER_ID "
                    + "AND (DOC_DATE>='" + yearFrom + "-04-01' OR (DOC_DATE='0000-00-00' AND D.CHANGED_DATE>='" + yearFrom + "-04-01')) "
                    + ") AS AA "
                    + "WHERE TRIM(COALESCE(EXTERNAL_EMAIL,''))!='' "
                    + "GROUP BY EXTERNAL_EMAIL "
                    //                    + "ORDER BY PENDING_DOC DESC LIMIT 5 "
                    + " ");

            rsData.first();
            if (rsData.getRow() > 0) {
                while (!rsData.isAfterLast()) {

                    userId = rsData.getString("USER_ID");
                    userName = rsData.getString("USER_NAME");
                    emailId = rsData.getString("EXTERNAL_EMAIL");
//                    emailId = "gaurang@dineshmills.com";

                    String pSubject = "Notification : Pending Document List for Date - " + EITLERPGLOBAL.getCurrentDate() + " " + data.getStringValueFromDB("SELECT CURTIME()");
                    String pMessage = "";
                    String cc = "sdmlerp@dineshmills.com";
                    int SrNo = 1;

                    pMessage = pMessage + "<br>PLEASE NOTE THAT FOLLOWING LIST OF DOCUMENT(S) ARE PENDING IN YOUR LIST.<br><br>";

                    ConnD = data.getConn();
                    stmtD = ConnD.createStatement();
                    rsDataD = stmtD.executeQuery("SELECT USER_ID,UPPER(USER_NAME) AS USER_NAME,EXTERNAL_EMAIL,MODULE_ID,MODULE_DESC,COUNT(*) AS PENDING_DOC "
                            + "FROM ( "
                            + "SELECT D.MODULE_ID,MODULE_DESC,DOC_NO,DOC_DATE,D.USER_ID,USER_NAME,EXTERNAL_EMAIL,RECEIVED_DATE,D.REMARKS AS RUN_DATE "
                            + "FROM PRODUCTION.FELT_PROD_DOC_DATA D,DINESHMILLS.D_COM_MODULES M,DINESHMILLS.D_COM_USER_MASTER U "
                            + "WHERE STATUS ='W'  AND D.MODULE_ID = M.MODULE_ID AND U.USER_ID = D.USER_ID "
                            + "AND (DOC_DATE>='" + yearFrom + "-04-01' OR (DOC_DATE='0000-00-00' AND D.CHANGED_DATE>='" + yearFrom + "-04-01')) "
                            + "UNION ALL "
                            + "SELECT D.MODULE_ID,MODULE_DESC,DOC_NO,DOC_DATE,D.USER_ID,USER_NAME,EXTERNAL_EMAIL,RECEIVED_DATE,D.REMARKS AS RUN_DATE "
                            + "FROM DINESHMILLS.D_COM_DOC_DATA D,DINESHMILLS.D_COM_MODULES M,DINESHMILLS.D_COM_USER_MASTER U "
                            + "WHERE STATUS ='W'  AND D.MODULE_ID = M.MODULE_ID AND U.USER_ID = D.USER_ID "
                            + "AND (DOC_DATE>='" + yearFrom + "-04-01' OR (DOC_DATE='0000-00-00' AND D.CHANGED_DATE>='" + yearFrom + "-04-01')) "
                            + "UNION ALL "
                            + "SELECT D.MODULE_ID,MODULE_DESC,DOC_NO,DOC_DATE,D.USER_ID,USER_NAME,EXTERNAL_EMAIL,RECEIVED_DATE,D.REMARKS AS RUN_DATE "
                            + "FROM SDMLATTPAY.D_COM_DOC_DATA D,DINESHMILLS.D_COM_MODULES M,DINESHMILLS.D_COM_USER_MASTER U "
                            + "WHERE STATUS ='W'  AND D.MODULE_ID = M.MODULE_ID AND U.USER_ID = D.USER_ID "
                            + "AND (DOC_DATE>='" + yearFrom + "-04-01' OR (DOC_DATE='0000-00-00' AND D.CHANGED_DATE>='" + yearFrom + "-04-01')) "
                            + ") AS AA "
                            //                            + "WHERE TRIM(COALESCE(EXTERNAL_EMAIL,''))!='' AND USER_ID='" + userId + "' AND USER_NAME='" + userName + "' "
                            + "WHERE TRIM(COALESCE(EXTERNAL_EMAIL,''))!='' AND EXTERNAL_EMAIL='" + rsData.getString("EXTERNAL_EMAIL") + "' "
                            + "GROUP BY USER_ID,USER_NAME,MODULE_ID,MODULE_DESC");

                    pMessage = pMessage + "<table border='1'>"
                            + "<tr>"
                            + "<th align='center'> SrNo </th>"
                            + "<th align='center'> USER NAME </th>"
                            + "<th align='center'> MODULE DESC </th>"
                            + "<th align='center'> TOTAL PENDING DOC </th>"
                            + "</tr>";

                    rsDataD.first();
                    if (rsDataD.getRow() > 0) {
                        while (!rsDataD.isAfterLast()) {

                            pMessage = pMessage + ""
                                    + "<tr>"
                                    + "<td align='right'> " + (SrNo++) + " </td>"
                                    + "<td align='left'> " + rsDataD.getString("USER_NAME") + " </td>"
                                    + "<td align='left'> " + rsDataD.getString("MODULE_DESC") + " </td>"
                                    + "<td align='right'> " + rsDataD.getString("PENDING_DOC") + " </td>";
                            pMessage = pMessage + "</tr>";

                            rsDataD.next();
                        }
                    }
                    pMessage = pMessage + "</table>";

                    pMessage += "<br>";

                    String recievers = emailId;

                    pMessage = pMessage + "<br><br>**** This is an auto-generated email, please do not reply ****";// For Live

//                    System.out.println("Recivers : " + recievers);
//                    System.out.println("pSubject : " + pSubject);
//                    System.out.println("pMessage : " + pMessage);
                    String responce = DailyMailNotification.sendNotificationMail(pSubject, pMessage, recievers, cc);
                    System.out.println("Send Mail Responce : " + responce);

                    rsData.next();
                }
            }

        } catch (Exception e) {
            System.out.println("Error on Mail: " + e.getMessage());
        }
    }

}
