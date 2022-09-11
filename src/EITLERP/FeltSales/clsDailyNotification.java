/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales;

import EITLERP.EITLERPGLOBAL;
import EITLERP.clsSales_Party;
import EITLERP.data;
import java.sql.*;
import java.util.*;

/**
 *
 * @author root
 */
public class clsDailyNotification {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        OCMonthUpdation();
        OCMonthUpdationSDF();
//        ReqMonthReschedule();
//        ReqMonthRescheduleSDF();
//        AutoCancellation();
//        AutoCancellationSDF();
        SpecialRequestMonth();
    }

    private static void OCMonthUpdation() {
        try {

            //CHANGED 20 MARCH 2020
            //String mntFor = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 90 DAY),'%b'),' - ',YEAR(DATE_ADD(NOW(), INTERVAL 3 MONTH))) FROM DUAL");
            String mntFor = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 60 DAY),'%b'),' - ',YEAR(DATE_ADD(NOW(), INTERVAL 2 MONTH))) FROM DUAL");
            String mntCur = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(NOW(),'%b'),' ',YEAR(NOW())) FROM DUAL");
            String mthUptoDt = data.getStringValueFromDB("SELECT LAST_DAY(CURRENT_DATE) + INTERVAL 1 DAY - INTERVAL 1 MONTH + INTERVAL 24 DAY FROM DUAL");
//            String mthUptoDt = data.getStringValueFromDB("SELECT LAST_DAY(CURRENT_DATE) + INTERVAL 1 DAY - INTERVAL 1 MONTH + INTERVAL 9 DAY FROM DUAL");
            String dayGo = data.getStringValueFromDB("SELECT DAY('" + mthUptoDt + "')-DAY(NOW()) FROM DUAL");
            String cancelOn = data.getStringValueFromDB("SELECT DATE_FORMAT(LAST_DAY(NOW()) + INTERVAL 1 DAY,'%d %b %Y') FROM DUAL");

//            String pSubject = "Update OC Month for " + mntFor + " on or before 25 " + mntCur + " ( " + dayGo + " days to go ) and Reschedule till last day of the Month - All Except SDF";
            String pSubject = "Update OC Month for " + mntFor + " on or before 25 " + mntCur + " ( " + dayGo + " days to go ) - All Except SDF";
//            String pSubject = "Update OC Month for " + mntFor + " on or before 10 " + mntCur + " ( " + dayGo + " days to go ) and Reschedule till last day of the Month - All Except SDF";

            String pMessage = "";
            String cc = "aditya@dineshmills.com,abtewary@dineshmills.com,soumen@dineshmills.com,vdshanbhag@dineshmills.com";
            int SrNo = 1;

            pMessage = pMessage + "<br>Dear All,<br>";
//            pMessage = pMessage + "<br>Order Confirmation for " + mntFor + " is pending for below mention Pieces, Please Update OC Month on or before 25 " + mntCur + " <br>And Reschedule till last day of the Month otherwise piece will be Cancelled (Automatically) on " + cancelOn + ".<br><br>";
            pMessage = pMessage + "<br>Order Confirmation for " + mntFor + " is pending for below mention Pieces, Please Update OC Month on or before 25 " + mntCur + " <br>Otherwise piece will be Rescheduled Automatically to next Month.<br><br>";
//            pMessage = pMessage + "<br>Order Confirmation for " + mntFor + " is pending for below mention Pieces, Please Update OC Month on or before 10 " + mntCur + " <br>And Reschedule till last day of the Month otherwise piece will be Cancelled (Automatically) on " + cancelOn + ".<br><br>";

            Connection Conn;
            Statement stmt;
            ResultSet rsData;

            Conn = data.getConn();
            stmt = Conn.createStatement();
            String strSQL = "SELECT * FROM "
                    + "( "
                    //CHANGED 20 MARCH 2020
                    //+ "SELECT LAST_DAY(DATE_ADD(NOW(), INTERVAL 90 DAY)),DATE_ADD(NOW(), INTERVAL 90 DAY),P.* FROM PRODUCTION.FELT_SALES_PIECE_REGISTER P "
                    + "SELECT LAST_DAY(DATE_ADD(NOW(), INTERVAL 60 DAY)),DATE_ADD(NOW(), INTERVAL 60 DAY),P.* FROM PRODUCTION.FELT_SALES_PIECE_REGISTER P "
                    + "WHERE COALESCE(PR_REQ_MTH_LAST_DDMMYY,'0000-00-00') != '0000-00-00' "
                    + "AND PR_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5) "
                    + "AND PR_PIECE_STAGE ='BOOKING' "
                    + "AND COALESCE(PR_OC_MONTHYEAR,'') ='' "
                    + "AND PR_GROUP != 'SDF' "
                    //CHANGED 20 MARCH 2020
                    //+ "AND LAST_DAY(DATE_ADD(NOW(), INTERVAL 90 DAY)) >= PR_REQ_MTH_LAST_DDMMYY "
                    + "AND LAST_DAY(DATE_ADD(NOW(), INTERVAL 60 DAY)) >= PR_REQ_MTH_LAST_DDMMYY "
                    + ") AS PR "
                    + "LEFT JOIN   "
                    + "(SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER   "
                    + "WHERE MAIN_ACCOUNT_CODE='210010' AND APPROVED=1 AND CANCELLED=0) AS PM   "
                    + "ON PR.PR_PARTY_CODE=PM.PARTY_CODE   "
                    + "LEFT JOIN   "
                    + "(SELECT H.GROUP_CODE,H.GROUP_DESC,D.PARTY_CODE AS GROUP_PARTY_CODE   "
                    + "FROM PRODUCTION.FELT_GROUP_MASTER_HEADER H,PRODUCTION.FELT_GROUP_MASTER_DETAIL D   "
                    + "WHERE H.GROUP_CODE=D.GROUP_CODE   "
                    + "AND H.APPROVED=1 AND H.CANCELED=0) AS GM   "
                    + "ON PR.PR_PARTY_CODE=GM.GROUP_PARTY_CODE  "
                    + "LEFT JOIN   "
                    + "(SELECT DISTINCT PRODUCT_CODE,PRODUCT_DESC FROM PRODUCTION.FELT_QLT_RATE_MASTER   "
                    + "WHERE APPROVED=1 AND CANCELED=0 ORDER BY DOC_NO DESC) AS QM   "
                    + "ON PR.PR_PRODUCT_CODE=QM.PRODUCT_CODE  "
                    + "LEFT JOIN   "
                    + "(SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MST) AS MP   "
                    + "ON PR.PR_POSITION_NO=MP.POSITION_NO  "
                    + "LEFT JOIN   "
                    + "(SELECT * FROM PRODUCTION.FELT_INCHARGE) AS IM   "
                    + "ON PR.PR_INCHARGE=IM.INCHARGE_CD ";
            
            rsData = stmt.executeQuery(strSQL);
            rsData.first();
            
                System.out.println("String StrSQL : "+strSQL);
                
            if (rsData.getRow() > 0) {
            pMessage = pMessage + "<table border='1' cellpadding='10'>"
                    + "<tr>"
                    + "<th align='center'> SrNo </th>"
                    + "<th align='center'> Piece No </th>"
                    + "<th align='center'> Party Code </th>"
                    + "<th align='center'> Party Name </th>"
                    + "<th align='center'> Machine No </th>"
                    + "<th align='center'> Position Desc </th>"
                    + "<th align='center'> Product Code </th>"
                    + "<th align='center'> Product Desc </th>"
                    + "<th align='center'> Group </th>"
                    + "<th align='center'> Style </th>"
                    + "<th align='center'> Length </th>"
                    + "<th align='center'> Width </th>"
                    + "<th align='center'> GSM </th>"
                    + "<th align='center'> Sq.Mtr </th>"
                    + "<th align='center'> Weight </th>"
                    + "<th align='center'> Piece Stage </th>"
                    + "<th align='center'> WIP Status </th>"
                    + "<th align='center'> Req Month </th>"
                    + "<th align='center'> OC Month </th>"
                    + "<th align='center'> Current Schedule Month </th>"
                    + "<th align='center'> Incharge </th>"
                    + "</tr>";

            rsData.first();
            if (rsData.getRow() > 0) {
                while (!rsData.isAfterLast()) {

                    pMessage = pMessage + ""
                            + "<tr>"
                            + "<td align='right'> " + (SrNo++) + " </td>"
                            + "<td align='left'> " + rsData.getString("PR_PIECE_NO") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_PARTY_CODE") + " </td>"
                            + "<td align='left'> " + rsData.getString("PARTY_NAME") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_MACHINE_NO") + " </td>"
                            + "<td align='left'> " + rsData.getString("POSITION_DESC") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_PRODUCT_CODE") + " </td>"
                            + "<td align='left'> " + rsData.getString("PRODUCT_DESC") + " </td>"
                            + "<td align='left'> " + rsData.getString("PR_GROUP") + " </td>"
                            + "<td align='left'> " + rsData.getString("PR_STYLE") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_LENGTH") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_WIDTH") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_GSM") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_SQMTR") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_THORITICAL_WEIGHT") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_PIECE_STAGE") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_WIP_STATUS") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_REQUESTED_MONTH") + " </td>"
                            + "<td align='center'>  </td>"
                            + "<td align='center'>  </td>"
                            + "<td align='left'> " + rsData.getString("INCHARGE_NAME") + " </td>"
                            + "</tr>";

                    rsData.next();
                }
            }
            pMessage = pMessage + "</table>";
            } else {
                pMessage = pMessage + "<br>No Piece Found of Order Confirmation for " + mntFor + ".<br>";
            }

            pMessage += "<br>";

//            String recievers = "sdmlerp@dineshmills.com";
            String recievers = "narendramotiani@dineshmills.com,yrpatel@dineshmills.com,amitkanti@dineshmills.com,feltpp@dineshmills.com,mitanglad@dineshmills.com,jaydeeppandya@dineshmills.com,siddharthneogi@dineshmills.com,anupsinghchauhan@dineshmills.com,sdmlerp@dineshmills.com";
            
            pMessage = pMessage + "<br><br>**** This is an auto-generated email, please do not reply ****"; 

            System.out.println("Recivers : " + recievers);
            System.out.println("pSubject : " + pSubject);
//                System.out.println("pMessage : " + pMessage);

            if (Integer.parseInt(dayGo)>=0 && Integer.parseInt(dayGo)<=5) {
                String responce = DailyMailNotification.sendNotificationMail(pSubject, pMessage, recievers, cc);
                System.out.println("Send Mail Responce : " + responce);
            } else {
                System.out.println("Date not between 20 to 25");
            }

        } catch (Exception e) {
            System.out.println("Error on Mail: " + e.getMessage());
        }
    }
    
    private static void OCMonthUpdationSDF() {
        try {

            //CHANGED 20 MARCH 2020
            //String mntFor = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 60 DAY),'%b'),' - ',YEAR(DATE_ADD(NOW(), INTERVAL 2 MONTH))) FROM DUAL");
            String mntFor = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 30 DAY),'%b'),' - ',YEAR(DATE_ADD(NOW(), INTERVAL 1 MONTH))) FROM DUAL");
            String mntCur = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(NOW(),'%b'),' ',YEAR(NOW())) FROM DUAL");
            String mthUptoDt = data.getStringValueFromDB("SELECT LAST_DAY(CURRENT_DATE) + INTERVAL 1 DAY - INTERVAL 1 MONTH + INTERVAL 24 DAY FROM DUAL");
//            String mthUptoDt = data.getStringValueFromDB("SELECT LAST_DAY(CURRENT_DATE) + INTERVAL 1 DAY - INTERVAL 1 MONTH + INTERVAL 9 DAY FROM DUAL");
            String dayGo = data.getStringValueFromDB("SELECT DAY('" + mthUptoDt + "')-DAY(NOW()) FROM DUAL");
            String cancelOn = data.getStringValueFromDB("SELECT DATE_FORMAT(LAST_DAY(NOW()) + INTERVAL 1 DAY,'%d %b %Y') FROM DUAL");

//            String pSubject = "Update OC Month for " + mntFor + " on or before 25 " + mntCur + " ( " + dayGo + " days to go ) and Reschedule till last day of the Month - Only for SDF";
            String pSubject = "Update OC Month for " + mntFor + " on or before 25 " + mntCur + " ( " + dayGo + " days to go ) - Only for SDF";
//            String pSubject = "Update OC Month for " + mntFor + " on or before 10 " + mntCur + " ( " + dayGo + " days to go ) and Reschedule till last day of the Month - Only for SDF";

            String pMessage = "";
            String cc = "aditya@dineshmills.com,abtewary@dineshmills.com,soumen@dineshmills.com,vdshanbhag@dineshmills.com";
            int SrNo = 1;

            pMessage = pMessage + "<br>Dear All,<br>";
            pMessage = pMessage + "<br>Order Confirmation for " + mntFor + " is pending for below mention Pieces, Please Update OC Month on or before 25 " + mntCur + " <br>Otherwise piece will be Rescheduled Automatically to next Month.<br><br>";
//            pMessage = pMessage + "<br>Order Confirmation for " + mntFor + " is pending for below mention Pieces, Please Update OC Month on or before 10 " + mntCur + " <br>And Reschedule till last day of the Month otherwise piece will be Cancelled (Automatically) on " + cancelOn + ".<br><br>";

            Connection Conn;
            Statement stmt;
            ResultSet rsData;

            Conn = data.getConn();
            stmt = Conn.createStatement();
            String strSQL = "SELECT * FROM "
                    + "( "
                    //CHANGED 20 MARCH 2020
                    //+ "SELECT LAST_DAY(DATE_ADD(NOW(), INTERVAL 60 DAY)),DATE_ADD(NOW(), INTERVAL 60 DAY),P.* FROM PRODUCTION.FELT_SALES_PIECE_REGISTER P "
                    + "SELECT LAST_DAY(DATE_ADD(NOW(), INTERVAL 30 DAY)),DATE_ADD(NOW(), INTERVAL 30 DAY),P.* FROM PRODUCTION.FELT_SALES_PIECE_REGISTER P "
                    + "WHERE COALESCE(PR_REQ_MTH_LAST_DDMMYY,'0000-00-00') != '0000-00-00' "
                    + "AND PR_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5) "
                    + "AND PR_PIECE_STAGE ='BOOKING' "
                    + "AND COALESCE(PR_OC_MONTHYEAR,'') ='' "
                    + "AND PR_GROUP = 'SDF' "
                    //CHANGED 20 MARCH 2020
                    //+ "AND LAST_DAY(DATE_ADD(NOW(), INTERVAL 60 DAY)) >= PR_REQ_MTH_LAST_DDMMYY "
                    + "AND LAST_DAY(DATE_ADD(NOW(), INTERVAL 30 DAY)) >= PR_REQ_MTH_LAST_DDMMYY "
                    + ") AS PR "
                    + "LEFT JOIN   "
                    + "(SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER   "
                    + "WHERE MAIN_ACCOUNT_CODE='210010' AND APPROVED=1 AND CANCELLED=0) AS PM   "
                    + "ON PR.PR_PARTY_CODE=PM.PARTY_CODE   "
                    + "LEFT JOIN   "
                    + "(SELECT H.GROUP_CODE,H.GROUP_DESC,D.PARTY_CODE AS GROUP_PARTY_CODE   "
                    + "FROM PRODUCTION.FELT_GROUP_MASTER_HEADER H,PRODUCTION.FELT_GROUP_MASTER_DETAIL D   "
                    + "WHERE H.GROUP_CODE=D.GROUP_CODE   "
                    + "AND H.APPROVED=1 AND H.CANCELED=0) AS GM   "
                    + "ON PR.PR_PARTY_CODE=GM.GROUP_PARTY_CODE  "
                    + "LEFT JOIN   "
                    + "(SELECT DISTINCT PRODUCT_CODE,PRODUCT_DESC FROM PRODUCTION.FELT_QLT_RATE_MASTER   "
                    + "WHERE APPROVED=1 AND CANCELED=0 ORDER BY DOC_NO DESC) AS QM   "
                    + "ON PR.PR_PRODUCT_CODE=QM.PRODUCT_CODE  "
                    + "LEFT JOIN   "
                    + "(SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MST) AS MP   "
                    + "ON PR.PR_POSITION_NO=MP.POSITION_NO  "
                    + "LEFT JOIN   "
                    + "(SELECT * FROM PRODUCTION.FELT_INCHARGE) AS IM   "
                    + "ON PR.PR_INCHARGE=IM.INCHARGE_CD ";
            
            rsData = stmt.executeQuery(strSQL);
            rsData.first();
            
                System.out.println("String StrSQL : "+strSQL);
                
            if (rsData.getRow() > 0) {
            pMessage = pMessage + "<table border='1' cellpadding='10'>"
                    + "<tr>"
                    + "<th align='center'> SrNo </th>"
                    + "<th align='center'> Piece No </th>"
                    + "<th align='center'> Party Code </th>"
                    + "<th align='center'> Party Name </th>"
                    + "<th align='center'> Machine No </th>"
                    + "<th align='center'> Position Desc </th>"
                    + "<th align='center'> Product Code </th>"
                    + "<th align='center'> Product Desc </th>"
                    + "<th align='center'> Group </th>"
                    + "<th align='center'> Style </th>"
                    + "<th align='center'> Length </th>"
                    + "<th align='center'> Width </th>"
                    + "<th align='center'> GSM </th>"
                    + "<th align='center'> Sq.Mtr </th>"
                    + "<th align='center'> Weight </th>"
                    + "<th align='center'> Piece Stage </th>"
                    + "<th align='center'> WIP Status </th>"
                    + "<th align='center'> Req Month </th>"
                    + "<th align='center'> OC Month </th>"
                    + "<th align='center'> Current Schedule Month </th>"
                    + "<th align='center'> Incharge </th>"
                    + "</tr>";

            rsData.first();
            if (rsData.getRow() > 0) {
                while (!rsData.isAfterLast()) {

                    pMessage = pMessage + ""
                            + "<tr>"
                            + "<td align='right'> " + (SrNo++) + " </td>"
                            + "<td align='left'> " + rsData.getString("PR_PIECE_NO") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_PARTY_CODE") + " </td>"
                            + "<td align='left'> " + rsData.getString("PARTY_NAME") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_MACHINE_NO") + " </td>"
                            + "<td align='left'> " + rsData.getString("POSITION_DESC") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_PRODUCT_CODE") + " </td>"
                            + "<td align='left'> " + rsData.getString("PRODUCT_DESC") + " </td>"
                            + "<td align='left'> " + rsData.getString("PR_GROUP") + " </td>"
                            + "<td align='left'> " + rsData.getString("PR_STYLE") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_LENGTH") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_WIDTH") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_GSM") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_SQMTR") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_THORITICAL_WEIGHT") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_PIECE_STAGE") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_WIP_STATUS") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_REQUESTED_MONTH") + " </td>"
                            + "<td align='center'>  </td>"
                            + "<td align='center'>  </td>"
                            + "<td align='left'> " + rsData.getString("INCHARGE_NAME") + " </td>"
                            + "</tr>";

                    rsData.next();
                }
            }
            pMessage = pMessage + "</table>";
            } else {
                pMessage = pMessage + "<br>No Piece Found of Order Confirmation for " + mntFor + ".<br>";
            }

            pMessage += "<br>";

//            String recievers = "sdmlerp@dineshmills.com";
            String recievers = "narendramotiani@dineshmills.com,yrpatel@dineshmills.com,amitkanti@dineshmills.com,feltpp@dineshmills.com,mitanglad@dineshmills.com,jaydeeppandya@dineshmills.com,siddharthneogi@dineshmills.com,anupsinghchauhan@dineshmills.com,sdmlerp@dineshmills.com";

            pMessage = pMessage + "<br><br>**** This is an auto-generated email, please do not reply ****"; 

            System.out.println("Recivers : " + recievers);
            System.out.println("pSubject : " + pSubject);
//                System.out.println("pMessage : " + pMessage);

            if (Integer.parseInt(dayGo)>=0 && Integer.parseInt(dayGo)<=5) {
                String responce = DailyMailNotification.sendNotificationMail(pSubject, pMessage, recievers, cc);
                System.out.println("Send Mail Responce : " + responce);
            } else {
                System.out.println("Date not between 20 to 25");
            }

        } catch (Exception e) {
            System.out.println("Error on Mail: " + e.getMessage());
        }
    }

    private static void ReqMonthReschedule() {
        try {

            String mntFor = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 90 DAY),'%b'),' - ',YEAR(DATE_ADD(NOW(), INTERVAL 3 MONTH))) FROM DUAL");
            String mntCur = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(NOW(),'%b'),' ',YEAR(NOW())) FROM DUAL");
            String mthUptoDt = data.getStringValueFromDB("SELECT LAST_DAY(CURRENT_DATE) FROM DUAL");
//            String mthUptoDt = data.getStringValueFromDB("SELECT LAST_DAY(CURRENT_DATE) + INTERVAL 1 DAY - INTERVAL 1 MONTH + INTERVAL 17 DAY FROM DUAL");
            String dayGo = data.getStringValueFromDB("SELECT DAY('" + mthUptoDt + "')-DAY(NOW()) FROM DUAL");
            String chkDay = data.getStringValueFromDB("SELECT DAY(NOW()) FROM DUAL");
            String cancelOn = data.getStringValueFromDB("SELECT DATE_FORMAT(LAST_DAY(NOW()) + INTERVAL 1 DAY,'%d %b %Y') FROM DUAL");

            String pSubject = "Reschedule Requested Month of " + mntFor + " on or before last days of " + mntCur + " ( " + dayGo + " days to go ) - All Except SDF";
//            String pSubject = "Reschedule Requested Month of " + mntFor + " on or before 18 of " + mntCur + " ( " + dayGo + " days to go ) - All Except SDF";

            String pMessage = "";
            String cc = "aditya@dineshmills.com,abtewary@dineshmills.com,soumen@dineshmills.com,vdshanbhag@dineshmills.com";
            int SrNo = 1;

            pMessage = pMessage + "<br>Dear All,<br>";
            pMessage = pMessage + "<br>Reschedule of Requested Month for " + mntFor + " is pending (OC Month is Blank) for below mention Pieces, Please reschedule till last day of the Month (" + mntCur + ") otherwise piece will be Cancelled (Automatically) on " + cancelOn + ".<br><br>";
//            pMessage = pMessage + "<br>Reschedule of Requested Month for " + mntFor + " is pending (OC Month is Blank) for below mention Pieces, Please reschedule on or before 18 of the Month (" + mntCur + ") otherwise piece will be Cancelled (Automatically) on 19 " + mntCur + ".<br><br>";

            Connection Conn;
            Statement stmt;
            ResultSet rsData;

            Conn = data.getConn();
            stmt = Conn.createStatement();
            String strSQL = "SELECT * FROM "
                    + "( "
                    + "SELECT LAST_DAY(DATE_ADD(NOW(), INTERVAL 90 DAY)),DATE_ADD(NOW(), INTERVAL 90 DAY),P.* FROM PRODUCTION.FELT_SALES_PIECE_REGISTER P "
                    + "WHERE COALESCE(PR_REQ_MTH_LAST_DDMMYY,'0000-00-00') != '0000-00-00' "
                    + "AND PR_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5) "
                    + "AND PR_PIECE_STAGE ='BOOKING' "
                    + "AND COALESCE(PR_OC_MONTHYEAR,'') ='' "
                    + "AND PR_GROUP != 'SDF' "
                    + "AND LAST_DAY(DATE_ADD(NOW(), INTERVAL 90 DAY)) >= PR_REQ_MTH_LAST_DDMMYY "
                    + ") AS PR "
                    + "LEFT JOIN   "
                    + "(SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER   "
                    + "WHERE MAIN_ACCOUNT_CODE='210010' AND APPROVED=1 AND CANCELLED=0) AS PM   "
                    + "ON PR.PR_PARTY_CODE=PM.PARTY_CODE   "
                    + "LEFT JOIN   "
                    + "(SELECT H.GROUP_CODE,H.GROUP_DESC,D.PARTY_CODE AS GROUP_PARTY_CODE   "
                    + "FROM PRODUCTION.FELT_GROUP_MASTER_HEADER H,PRODUCTION.FELT_GROUP_MASTER_DETAIL D   "
                    + "WHERE H.GROUP_CODE=D.GROUP_CODE   "
                    + "AND H.APPROVED=1 AND H.CANCELED=0) AS GM   "
                    + "ON PR.PR_PARTY_CODE=GM.GROUP_PARTY_CODE  "
                    + "LEFT JOIN   "
                    + "(SELECT DISTINCT PRODUCT_CODE,PRODUCT_DESC FROM PRODUCTION.FELT_QLT_RATE_MASTER   "
                    + "WHERE APPROVED=1 AND CANCELED=0 ORDER BY DOC_NO DESC) AS QM   "
                    + "ON PR.PR_PRODUCT_CODE=QM.PRODUCT_CODE  "
                    + "LEFT JOIN   "
                    + "(SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MST) AS MP   "
                    + "ON PR.PR_POSITION_NO=MP.POSITION_NO  "
                    + "LEFT JOIN   "
                    + "(SELECT * FROM PRODUCTION.FELT_INCHARGE) AS IM   "
                    + "ON PR.PR_INCHARGE=IM.INCHARGE_CD ";
            
            rsData = stmt.executeQuery(strSQL);
            rsData.first();
            
                System.out.println("String StrSQL : "+strSQL);
                
            if (rsData.getRow() > 0) {
            pMessage = pMessage + "<table border='1' cellpadding='10'>"
                    + "<tr>"
                    + "<th align='center'> SrNo </th>"
                    + "<th align='center'> Piece No </th>"
                    + "<th align='center'> Party Code </th>"
                    + "<th align='center'> Party Name </th>"
                    + "<th align='center'> Machine No </th>"
                    + "<th align='center'> Position Desc </th>"
                    + "<th align='center'> Product Code </th>"
                    + "<th align='center'> Product Desc </th>"
                    + "<th align='center'> Group </th>"
                    + "<th align='center'> Style </th>"
                    + "<th align='center'> Length </th>"
                    + "<th align='center'> Width </th>"
                    + "<th align='center'> GSM </th>"
                    + "<th align='center'> Sq.Mtr </th>"
                    + "<th align='center'> Weight </th>"
                    + "<th align='center'> Piece Stage </th>"
                    + "<th align='center'> WIP Status </th>"
                    + "<th align='center'> Req Month </th>"
                    + "<th align='center'> OC Month </th>"
                    + "<th align='center'> Current Schedule Month </th>"
                    + "<th align='center'> Incharge </th>"
                    + "</tr>";

            rsData.first();
            if (rsData.getRow() > 0) {
                while (!rsData.isAfterLast()) {

                    pMessage = pMessage + ""
                            + "<tr>"
                            + "<td align='right'> " + (SrNo++) + " </td>"
                            + "<td align='left'> " + rsData.getString("PR_PIECE_NO") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_PARTY_CODE") + " </td>"
                            + "<td align='left'> " + rsData.getString("PARTY_NAME") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_MACHINE_NO") + " </td>"
                            + "<td align='left'> " + rsData.getString("POSITION_DESC") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_PRODUCT_CODE") + " </td>"
                            + "<td align='left'> " + rsData.getString("PRODUCT_DESC") + " </td>"
                            + "<td align='left'> " + rsData.getString("PR_GROUP") + " </td>"
                            + "<td align='left'> " + rsData.getString("PR_STYLE") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_LENGTH") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_WIDTH") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_GSM") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_SQMTR") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_THORITICAL_WEIGHT") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_PIECE_STAGE") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_WIP_STATUS") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_REQUESTED_MONTH") + " </td>"
                            + "<td align='center'>  </td>"
                            + "<td align='center'>  </td>"
                            + "<td align='left'> " + rsData.getString("INCHARGE_NAME") + " </td>"
                            + "</tr>";

                    rsData.next();
                }
            }
            pMessage = pMessage + "</table>";
            } else {
                pMessage = pMessage + "<br>No Piece Found of Reschedule for Requested Month for " + mntFor + ".<br>";
            }

            pMessage += "<br>";

//            String recievers = "sdmlerp@dineshmills.com";
            String recievers = "narendramotiani@dineshmills.com,yrpatel@dineshmills.com,amitkanti@dineshmills.com,feltpp@dineshmills.com,mitanglad@dineshmills.com,jaydeeppandya@dineshmills.com,siddharthneogi@dineshmills.com,anupsinghchauhan@dineshmills.com,sdmlerp@dineshmills.com";

            pMessage = pMessage + "<br><br>**** This is an auto-generated email, please do not reply ****";

            System.out.println("Recivers : " + recievers);
            System.out.println("pSubject : " + pSubject);
//                System.out.println("pMessage : " + pMessage);

            if (Integer.parseInt(chkDay)>=26) {
//            if (Integer.parseInt(dayGo)>=0 && Integer.parseInt(dayGo)<=1) {
                String responce = DailyMailNotification.sendNotificationMail(pSubject, pMessage, recievers, cc);
                System.out.println("Send Mail Responce : " + responce);
            } else {
                System.out.println("Date not after 25");
            }

        } catch (Exception e) {
            System.out.println("Error on Mail: " + e.getMessage());
        }
    }
    
    private static void ReqMonthRescheduleSDF() {
        try {

            String mntFor = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 60 DAY),'%b'),' - ',YEAR(DATE_ADD(NOW(), INTERVAL 2 MONTH))) FROM DUAL");
            String mntCur = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(NOW(),'%b'),' ',YEAR(NOW())) FROM DUAL");
            String mthUptoDt = data.getStringValueFromDB("SELECT LAST_DAY(CURRENT_DATE) FROM DUAL");
//            String mthUptoDt = data.getStringValueFromDB("SELECT LAST_DAY(CURRENT_DATE) + INTERVAL 1 DAY - INTERVAL 1 MONTH + INTERVAL 17 DAY FROM DUAL");
            String dayGo = data.getStringValueFromDB("SELECT DAY('" + mthUptoDt + "')-DAY(NOW()) FROM DUAL");
            String chkDay = data.getStringValueFromDB("SELECT DAY(NOW()) FROM DUAL");
            String cancelOn = data.getStringValueFromDB("SELECT DATE_FORMAT(LAST_DAY(NOW()) + INTERVAL 1 DAY,'%d %b %Y') FROM DUAL");

            String pSubject = "Reschedule Requested Month of " + mntFor + " on or before last days of " + mntCur + " ( " + dayGo + " days to go ) - Only for SDF";
//            String pSubject = "Reschedule Requested Month of " + mntFor + " on or before 18 of " + mntCur + " ( " + dayGo + " days to go ) - Only for SDF";

            String pMessage = "";
            String cc = "aditya@dineshmills.com,abtewary@dineshmills.com,soumen@dineshmills.com,vdshanbhag@dineshmills.com";
            int SrNo = 1;

            pMessage = pMessage + "<br>Dear All,<br>";
            pMessage = pMessage + "<br>Reschedule of Requested Month for " + mntFor + " is pending (OC Month is Blank) for below mention Pieces, Please reschedule till last day of the Month (" + mntCur + ") otherwise piece will be Cancelled (Automatically) on " + cancelOn + ".<br><br>";
//            pMessage = pMessage + "<br>Reschedule of Requested Month for " + mntFor + " is pending (OC Month is Blank) for below mention Pieces, Please reschedule on or before 18 of the Month (" + mntCur + ") otherwise piece will be Cancelled (Automatically) on 19 " + mntCur + ".<br><br>";

            Connection Conn;
            Statement stmt;
            ResultSet rsData;

            Conn = data.getConn();
            stmt = Conn.createStatement();
            String strSQL = "SELECT * FROM "
                    + "( "
                    + "SELECT LAST_DAY(DATE_ADD(NOW(), INTERVAL 60 DAY)),DATE_ADD(NOW(), INTERVAL 60 DAY),P.* FROM PRODUCTION.FELT_SALES_PIECE_REGISTER P "
                    + "WHERE COALESCE(PR_REQ_MTH_LAST_DDMMYY,'0000-00-00') != '0000-00-00' "
                    + "AND PR_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5) "
                    + "AND PR_PIECE_STAGE ='BOOKING' "
                    + "AND COALESCE(PR_OC_MONTHYEAR,'') ='' "
                    + "AND PR_GROUP = 'SDF' "
                    + "AND LAST_DAY(DATE_ADD(NOW(), INTERVAL 60 DAY)) >= PR_REQ_MTH_LAST_DDMMYY "
                    + ") AS PR "
                    + "LEFT JOIN   "
                    + "(SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER   "
                    + "WHERE MAIN_ACCOUNT_CODE='210010' AND APPROVED=1 AND CANCELLED=0) AS PM   "
                    + "ON PR.PR_PARTY_CODE=PM.PARTY_CODE   "
                    + "LEFT JOIN   "
                    + "(SELECT H.GROUP_CODE,H.GROUP_DESC,D.PARTY_CODE AS GROUP_PARTY_CODE   "
                    + "FROM PRODUCTION.FELT_GROUP_MASTER_HEADER H,PRODUCTION.FELT_GROUP_MASTER_DETAIL D   "
                    + "WHERE H.GROUP_CODE=D.GROUP_CODE   "
                    + "AND H.APPROVED=1 AND H.CANCELED=0) AS GM   "
                    + "ON PR.PR_PARTY_CODE=GM.GROUP_PARTY_CODE  "
                    + "LEFT JOIN   "
                    + "(SELECT DISTINCT PRODUCT_CODE,PRODUCT_DESC FROM PRODUCTION.FELT_QLT_RATE_MASTER   "
                    + "WHERE APPROVED=1 AND CANCELED=0 ORDER BY DOC_NO DESC) AS QM   "
                    + "ON PR.PR_PRODUCT_CODE=QM.PRODUCT_CODE  "
                    + "LEFT JOIN   "
                    + "(SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MST) AS MP   "
                    + "ON PR.PR_POSITION_NO=MP.POSITION_NO  "
                    + "LEFT JOIN   "
                    + "(SELECT * FROM PRODUCTION.FELT_INCHARGE) AS IM   "
                    + "ON PR.PR_INCHARGE=IM.INCHARGE_CD ";
            
            rsData = stmt.executeQuery(strSQL);
            rsData.first();
            
                System.out.println("String StrSQL : "+strSQL);
                
            if (rsData.getRow() > 0) {
            pMessage = pMessage + "<table border='1' cellpadding='10'>"
                    + "<tr>"
                    + "<th align='center'> SrNo </th>"
                    + "<th align='center'> Piece No </th>"
                    + "<th align='center'> Party Code </th>"
                    + "<th align='center'> Party Name </th>"
                    + "<th align='center'> Machine No </th>"
                    + "<th align='center'> Position Desc </th>"
                    + "<th align='center'> Product Code </th>"
                    + "<th align='center'> Product Desc </th>"
                    + "<th align='center'> Group </th>"
                    + "<th align='center'> Style </th>"
                    + "<th align='center'> Length </th>"
                    + "<th align='center'> Width </th>"
                    + "<th align='center'> GSM </th>"
                    + "<th align='center'> Sq.Mtr </th>"
                    + "<th align='center'> Weight </th>"
                    + "<th align='center'> Piece Stage </th>"
                    + "<th align='center'> WIP Status </th>"
                    + "<th align='center'> Req Month </th>"
                    + "<th align='center'> OC Month </th>"
                    + "<th align='center'> Current Schedule Month </th>"
                    + "<th align='center'> Incharge </th>"
                    + "</tr>";

            rsData.first();
            if (rsData.getRow() > 0) {
                while (!rsData.isAfterLast()) {

                    pMessage = pMessage + ""
                            + "<tr>"
                            + "<td align='right'> " + (SrNo++) + " </td>"
                            + "<td align='left'> " + rsData.getString("PR_PIECE_NO") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_PARTY_CODE") + " </td>"
                            + "<td align='left'> " + rsData.getString("PARTY_NAME") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_MACHINE_NO") + " </td>"
                            + "<td align='left'> " + rsData.getString("POSITION_DESC") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_PRODUCT_CODE") + " </td>"
                            + "<td align='left'> " + rsData.getString("PRODUCT_DESC") + " </td>"
                            + "<td align='left'> " + rsData.getString("PR_GROUP") + " </td>"
                            + "<td align='left'> " + rsData.getString("PR_STYLE") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_LENGTH") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_WIDTH") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_GSM") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_SQMTR") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_THORITICAL_WEIGHT") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_PIECE_STAGE") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_WIP_STATUS") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_REQUESTED_MONTH") + " </td>"
                            + "<td align='center'>  </td>"
                            + "<td align='center'>  </td>"
                            + "<td align='left'> " + rsData.getString("INCHARGE_NAME") + " </td>"
                            + "</tr>";

                    rsData.next();
                }
            }
            pMessage = pMessage + "</table>";
            } else {
                pMessage = pMessage + "<br>No Piece Found of Reschedule for Requested Month for " + mntFor + ".<br>";
            }

            pMessage += "<br>";

//            String recievers = "sdmlerp@dineshmills.com";
            String recievers = "narendramotiani@dineshmills.com,yrpatel@dineshmills.com,amitkanti@dineshmills.com,feltpp@dineshmills.com,mitanglad@dineshmills.com,jaydeeppandya@dineshmills.com,siddharthneogi@dineshmills.com,anupsinghchauhan@dineshmills.com,sdmlerp@dineshmills.com";

            pMessage = pMessage + "<br><br>**** This is an auto-generated email, please do not reply ****";

            System.out.println("Recivers : " + recievers);
            System.out.println("pSubject : " + pSubject);
//                System.out.println("pMessage : " + pMessage);

            if (Integer.parseInt(chkDay)>=26) {
//            if (Integer.parseInt(dayGo)>=0 && Integer.parseInt(dayGo)<=1) {
                String responce = DailyMailNotification.sendNotificationMail(pSubject, pMessage, recievers, cc);
                System.out.println("Send Mail Responce : " + responce);
            } else {
                System.out.println("Date not after 25");
            }

        } catch (Exception e) {
            System.out.println("Error on Mail: " + e.getMessage());
        }
    }

    private static void AutoCancellation() {
        try {

            String mntFor = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 2 MONTH),'%b'),' - ',YEAR(DATE_ADD(NOW(), INTERVAL 2 MONTH))) FROM DUAL");
//            String mntFor = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 3 MONTH),'%b'),' - ',YEAR(DATE_ADD(NOW(), INTERVAL 3 MONTH))) FROM DUAL");
//            String mntCur = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(NOW(),'%b'),' ',YEAR(NOW())) FROM DUAL");
//            String mthUptoDt = data.getStringValueFromDB("SELECT LAST_DAY(CURRENT_DATE) + INTERVAL 1 DAY - INTERVAL 1 MONTH + INTERVAL 24 DAY FROM DUAL");
//            String mthUptoDt = data.getStringValueFromDB("SELECT LAST_DAY(CURRENT_DATE) + INTERVAL 1 DAY - INTERVAL 1 MONTH + INTERVAL 9 DAY FROM DUAL");
            String dayGo = data.getStringValueFromDB("SELECT DAY(NOW()) FROM DUAL");
            String cancelOn = data.getStringValueFromDB("SELECT DATE_FORMAT(NOW() ,'%d/%m/%Y %H:%I:%S') FROM DUAL");
            String canceldt = data.getStringValueFromDB("SELECT CURDATE() FROM DUAL");
            String cancelRemark = "AUTO CANCELED ON "+cancelOn;

            String pSubject = "List of Auto Cancelled Pieces , Requested Month is " + mntFor + " (OC Month is Blank) - All Except SDF";
            
            String pMessage = "";
            String cc = "aditya@dineshmills.com,abtewary@dineshmills.com,soumen@dineshmills.com,vdshanbhag@dineshmills.com";
            int SrNo = 1;

            pMessage = pMessage + "<br>Dear All,<br>";
            pMessage = pMessage + "<br>List of Auto Cancelled Pieces, Requested Month is " + mntFor + " (OC Month is Blank).<br><br>";

            Connection Conn;
            Statement stmt;
            ResultSet rsData;

            Conn = data.getConn();
            stmt = Conn.createStatement();
            String strSQL = "SELECT * FROM "
                    + "( "
                    + "SELECT LAST_DAY(DATE_ADD(NOW(), INTERVAL 2 MONTH)),DATE_ADD(NOW(), INTERVAL 2 MONTH),P.* FROM PRODUCTION.FELT_SALES_PIECE_REGISTER P "
//                    + "SELECT LAST_DAY(DATE_ADD(NOW(), INTERVAL 3 MONTH)),DATE_ADD(NOW(), INTERVAL 3 MONTH),P.* FROM PRODUCTION.FELT_SALES_PIECE_REGISTER P "
                    + "WHERE COALESCE(PR_REQ_MTH_LAST_DDMMYY,'0000-00-00') != '0000-00-00' "
                    + "AND PR_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5) "
                    + "AND PR_PIECE_STAGE ='BOOKING' "
                    + "AND COALESCE(PR_OC_MONTHYEAR,'') ='' "
                    + "AND PR_GROUP != 'SDF' "
                    + "AND LAST_DAY(DATE_ADD(NOW(), INTERVAL 2 MONTH)) >= PR_REQ_MTH_LAST_DDMMYY "
//                    + "AND LAST_DAY(DATE_ADD(NOW(), INTERVAL 3 MONTH)) >= PR_REQ_MTH_LAST_DDMMYY "
                    + ") AS PR "
                    + "LEFT JOIN   "
                    + "(SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER   "
                    + "WHERE MAIN_ACCOUNT_CODE='210010' AND APPROVED=1 AND CANCELLED=0) AS PM   "
                    + "ON PR.PR_PARTY_CODE=PM.PARTY_CODE   "
                    + "LEFT JOIN   "
                    + "(SELECT H.GROUP_CODE,H.GROUP_DESC,D.PARTY_CODE AS GROUP_PARTY_CODE   "
                    + "FROM PRODUCTION.FELT_GROUP_MASTER_HEADER H,PRODUCTION.FELT_GROUP_MASTER_DETAIL D   "
                    + "WHERE H.GROUP_CODE=D.GROUP_CODE   "
                    + "AND H.APPROVED=1 AND H.CANCELED=0) AS GM   "
                    + "ON PR.PR_PARTY_CODE=GM.GROUP_PARTY_CODE  "
                    + "LEFT JOIN   "
                    + "(SELECT DISTINCT PRODUCT_CODE,PRODUCT_DESC FROM PRODUCTION.FELT_QLT_RATE_MASTER   "
                    + "WHERE APPROVED=1 AND CANCELED=0 ORDER BY DOC_NO DESC) AS QM   "
                    + "ON PR.PR_PRODUCT_CODE=QM.PRODUCT_CODE  "
                    + "LEFT JOIN   "
                    + "(SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MST) AS MP   "
                    + "ON PR.PR_POSITION_NO=MP.POSITION_NO  "
                    + "LEFT JOIN   "
                    + "(SELECT * FROM PRODUCTION.FELT_INCHARGE) AS IM   "
                    + "ON PR.PR_INCHARGE=IM.INCHARGE_CD ";
            
            rsData = stmt.executeQuery(strSQL);
            rsData.first();
            
                System.out.println("String StrSQL : "+strSQL);
                
            if (rsData.getRow() > 0) {
            pMessage = pMessage + "<table border='1' cellpadding='10'>"
                    + "<tr>"
                    + "<th align='center'> SrNo </th>"
                    + "<th align='center'> Piece No </th>"
                    + "<th align='center'> Party Code </th>"
                    + "<th align='center'> Party Name </th>"
                    + "<th align='center'> Machine No </th>"
                    + "<th align='center'> Position Desc </th>"
                    + "<th align='center'> Product Code </th>"
                    + "<th align='center'> Product Desc </th>"
                    + "<th align='center'> Group </th>"
                    + "<th align='center'> Style </th>"
                    + "<th align='center'> Length </th>"
                    + "<th align='center'> Width </th>"
                    + "<th align='center'> GSM </th>"
                    + "<th align='center'> Sq.Mtr </th>"
                    + "<th align='center'> Weight </th>"
                    + "<th align='center'> Piece Stage </th>"
                    + "<th align='center'> WIP Status </th>"
                    + "<th align='center'> Req Month </th>"
                    + "<th align='center'> OC Month </th>"
                    + "<th align='center'> Current Schedule Month </th>"
                    + "<th align='center'> Incharge </th>"
                    + "</tr>";

            rsData.first();
            if (rsData.getRow() > 0) {
                while (!rsData.isAfterLast()) {
                    
                    if (Integer.parseInt(dayGo)==1) {
//                    if (Integer.parseInt(dayGo)==19) {    
                        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PRIORITY_HOLD_CAN_FLAG = 9, PR_PIECE_STAGE = 'CANCELED', PR_WIP_STATUS = 'CANCELED', PR_CANCELED_DATE = '"+canceldt+"', PR_CANCELED_REMARK = '"+cancelRemark+"' WHERE PR_PIECE_NO = '" + rsData.getString("PR_PIECE_NO") + "' ");
                        data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_PRIORITY_HOLD_CAN_FLAG = 9, WIP_PIECE_STAGE = 'CANCELED', WIP_STATUS = 'CANCELED', WIP_CANCELED_DATE = '"+canceldt+"', WIP_CANCELED_REMARK = '"+cancelRemark+"' WHERE WIP_PIECE_NO = '" + rsData.getString("PR_PIECE_NO") + "' ");
                    }

                    pMessage = pMessage + ""
                            + "<tr>"
                            + "<td align='right'> " + (SrNo++) + " </td>"
                            + "<td align='left'> " + rsData.getString("PR_PIECE_NO") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_PARTY_CODE") + " </td>"
                            + "<td align='left'> " + rsData.getString("PARTY_NAME") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_MACHINE_NO") + " </td>"
                            + "<td align='left'> " + rsData.getString("POSITION_DESC") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_PRODUCT_CODE") + " </td>"
                            + "<td align='left'> " + rsData.getString("PRODUCT_DESC") + " </td>"
                            + "<td align='left'> " + rsData.getString("PR_GROUP") + " </td>"
                            + "<td align='left'> " + rsData.getString("PR_STYLE") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_LENGTH") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_WIDTH") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_GSM") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_SQMTR") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_THORITICAL_WEIGHT") + " </td>"
                            + "<td align='center'> CANCELED </td>"
                            + "<td align='center'> CANCELED </td>"
                            + "<td align='center'> " + rsData.getString("PR_REQUESTED_MONTH") + " </td>"
                            + "<td align='center'>  </td>"
                            + "<td align='center'>  </td>"
                            + "<td align='left'> " + rsData.getString("INCHARGE_NAME") + " </td>"
                            + "</tr>";

                    rsData.next();
                }
            }
            pMessage = pMessage + "</table>";
            } else {
                pMessage = pMessage + "<br>No Piece Found of Auto Cancellation for " + mntFor + ".<br>";
            }

            pMessage += "<br>";

//            String recievers = "sdmlerp@dineshmills.com";
            String recievers = "narendramotiani@dineshmills.com,yrpatel@dineshmills.com,amitkanti@dineshmills.com,feltpp@dineshmills.com,mitanglad@dineshmills.com,jaydeeppandya@dineshmills.com,siddharthneogi@dineshmills.com,anupsinghchauhan@dineshmills.com,sdmlerp@dineshmills.com";

            pMessage = pMessage + "<br><br>**** This is an auto-generated email, please do not reply ****"; 

            System.out.println("Recivers : " + recievers);
            System.out.println("pSubject : " + pSubject);
//                System.out.println("pMessage : " + pMessage);

            if (Integer.parseInt(dayGo)==1) {
//            if (Integer.parseInt(dayGo)==19) {
                String responce = DailyMailNotification.sendNotificationMail(pSubject, pMessage, recievers, cc);
                System.out.println("Send Mail Responce : " + responce);
            } else {
                System.out.println("Date is not 1st of Month");
            }

        } catch (Exception e) {
            System.out.println("Error on Mail: " + e.getMessage());
        }
    }

    private static void AutoCancellationSDF() {
        try {

            String mntFor = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 1 MONTH),'%b'),' - ',YEAR(DATE_ADD(NOW(), INTERVAL 1 MONTH))) FROM DUAL");
//            String mntFor = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 2 MONTH),'%b'),' - ',YEAR(DATE_ADD(NOW(), INTERVAL 2 MONTH))) FROM DUAL");
//            String mntCur = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(NOW(),'%b'),' ',YEAR(NOW())) FROM DUAL");
//            String mthUptoDt = data.getStringValueFromDB("SELECT LAST_DAY(CURRENT_DATE) + INTERVAL 1 DAY - INTERVAL 1 MONTH + INTERVAL 24 DAY FROM DUAL");
//            String mthUptoDt = data.getStringValueFromDB("SELECT LAST_DAY(CURRENT_DATE) + INTERVAL 1 DAY - INTERVAL 1 MONTH + INTERVAL 9 DAY FROM DUAL");
            String dayGo = data.getStringValueFromDB("SELECT DAY(NOW()) FROM DUAL");
            String cancelOn = data.getStringValueFromDB("SELECT DATE_FORMAT(NOW() ,'%d/%m/%Y %H:%I:%S') FROM DUAL");
            String canceldt = data.getStringValueFromDB("SELECT CURDATE() FROM DUAL");
            String cancelRemark = "AUTO CANCELED ON "+cancelOn;

            String pSubject = "List of Auto Cancelled Pieces , Requested Month is " + mntFor + " (OC Month is Blank) - Only for SDF";
            
            String pMessage = "";
            String cc = "aditya@dineshmills.com,abtewary@dineshmills.com,soumen@dineshmills.com,vdshanbhag@dineshmills.com";
            int SrNo = 1;

            pMessage = pMessage + "<br>Dear All,<br>";
            pMessage = pMessage + "<br>List of Auto Cancelled Pieces, Requested Month is " + mntFor + " (OC Month is Blank).<br><br>";

            Connection Conn;
            Statement stmt;
            ResultSet rsData;

            Conn = data.getConn();
            stmt = Conn.createStatement();
            String strSQL = "SELECT * FROM "
                    + "( "
                    + "SELECT LAST_DAY(DATE_ADD(NOW(), INTERVAL 1 MONTH)),DATE_ADD(NOW(), INTERVAL 1 MONTH),P.* FROM PRODUCTION.FELT_SALES_PIECE_REGISTER P "
//                    + "SELECT LAST_DAY(DATE_ADD(NOW(), INTERVAL 2 MONTH)),DATE_ADD(NOW(), INTERVAL 2 MONTH),P.* FROM PRODUCTION.FELT_SALES_PIECE_REGISTER P "
                    + "WHERE COALESCE(PR_REQ_MTH_LAST_DDMMYY,'0000-00-00') != '0000-00-00' "
                    + "AND PR_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5) "
                    + "AND PR_PIECE_STAGE ='BOOKING' "
                    + "AND COALESCE(PR_OC_MONTHYEAR,'') ='' "
                    + "AND PR_GROUP = 'SDF' "
                    + "AND LAST_DAY(DATE_ADD(NOW(), INTERVAL 1 MONTH)) >= PR_REQ_MTH_LAST_DDMMYY "
//                    + "AND LAST_DAY(DATE_ADD(NOW(), INTERVAL 2 MONTH)) >= PR_REQ_MTH_LAST_DDMMYY "
                    + ") AS PR "
                    + "LEFT JOIN   "
                    + "(SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER   "
                    + "WHERE MAIN_ACCOUNT_CODE='210010' AND APPROVED=1 AND CANCELLED=0) AS PM   "
                    + "ON PR.PR_PARTY_CODE=PM.PARTY_CODE   "
                    + "LEFT JOIN   "
                    + "(SELECT H.GROUP_CODE,H.GROUP_DESC,D.PARTY_CODE AS GROUP_PARTY_CODE   "
                    + "FROM PRODUCTION.FELT_GROUP_MASTER_HEADER H,PRODUCTION.FELT_GROUP_MASTER_DETAIL D   "
                    + "WHERE H.GROUP_CODE=D.GROUP_CODE   "
                    + "AND H.APPROVED=1 AND H.CANCELED=0) AS GM   "
                    + "ON PR.PR_PARTY_CODE=GM.GROUP_PARTY_CODE  "
                    + "LEFT JOIN   "
                    + "(SELECT DISTINCT PRODUCT_CODE,PRODUCT_DESC FROM PRODUCTION.FELT_QLT_RATE_MASTER   "
                    + "WHERE APPROVED=1 AND CANCELED=0 ORDER BY DOC_NO DESC) AS QM   "
                    + "ON PR.PR_PRODUCT_CODE=QM.PRODUCT_CODE  "
                    + "LEFT JOIN   "
                    + "(SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MST) AS MP   "
                    + "ON PR.PR_POSITION_NO=MP.POSITION_NO  "
                    + "LEFT JOIN   "
                    + "(SELECT * FROM PRODUCTION.FELT_INCHARGE) AS IM   "
                    + "ON PR.PR_INCHARGE=IM.INCHARGE_CD ";
            
            rsData = stmt.executeQuery(strSQL);
            rsData.first();
            
                System.out.println("String StrSQL : "+strSQL);
                
            if (rsData.getRow() > 0) {
            pMessage = pMessage + "<table border='1' cellpadding='10'>"
                    + "<tr>"
                    + "<th align='center'> SrNo </th>"
                    + "<th align='center'> Piece No </th>"
                    + "<th align='center'> Party Code </th>"
                    + "<th align='center'> Party Name </th>"
                    + "<th align='center'> Machine No </th>"
                    + "<th align='center'> Position Desc </th>"
                    + "<th align='center'> Product Code </th>"
                    + "<th align='center'> Product Desc </th>"
                    + "<th align='center'> Group </th>"
                    + "<th align='center'> Style </th>"
                    + "<th align='center'> Length </th>"
                    + "<th align='center'> Width </th>"
                    + "<th align='center'> GSM </th>"
                    + "<th align='center'> Sq.Mtr </th>"
                    + "<th align='center'> Weight </th>"
                    + "<th align='center'> Piece Stage </th>"
                    + "<th align='center'> WIP Status </th>"
                    + "<th align='center'> Req Month </th>"
                    + "<th align='center'> OC Month </th>"
                    + "<th align='center'> Current Schedule Month </th>"
                    + "<th align='center'> Incharge </th>"
                    + "</tr>";

            rsData.first();
            if (rsData.getRow() > 0) {
                while (!rsData.isAfterLast()) {
                    
                    if (Integer.parseInt(dayGo)==1) {
//                    if (Integer.parseInt(dayGo)==19) {    
                        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PRIORITY_HOLD_CAN_FLAG = 9, PR_PIECE_STAGE = 'CANCELED', PR_WIP_STATUS = 'CANCELED', PR_CANCELED_DATE = '"+canceldt+"', PR_CANCELED_REMARK = '"+cancelRemark+"' WHERE PR_PIECE_NO = '" + rsData.getString("PR_PIECE_NO") + "' ");
                        data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_PRIORITY_HOLD_CAN_FLAG = 9, WIP_PIECE_STAGE = 'CANCELED', WIP_STATUS = 'CANCELED', WIP_CANCELED_DATE = '"+canceldt+"', WIP_CANCELED_REMARK = '"+cancelRemark+"' WHERE WIP_PIECE_NO = '" + rsData.getString("PR_PIECE_NO") + "' ");
                    }

                    pMessage = pMessage + ""
                            + "<tr>"
                            + "<td align='right'> " + (SrNo++) + " </td>"
                            + "<td align='left'> " + rsData.getString("PR_PIECE_NO") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_PARTY_CODE") + " </td>"
                            + "<td align='left'> " + rsData.getString("PARTY_NAME") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_MACHINE_NO") + " </td>"
                            + "<td align='left'> " + rsData.getString("POSITION_DESC") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_PRODUCT_CODE") + " </td>"
                            + "<td align='left'> " + rsData.getString("PRODUCT_DESC") + " </td>"
                            + "<td align='left'> " + rsData.getString("PR_GROUP") + " </td>"
                            + "<td align='left'> " + rsData.getString("PR_STYLE") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_LENGTH") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_WIDTH") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_GSM") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_SQMTR") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_THORITICAL_WEIGHT") + " </td>"
                            + "<td align='center'> CANCELED </td>"
                            + "<td align='center'> CANCELED </td>"
                            + "<td align='center'> " + rsData.getString("PR_REQUESTED_MONTH") + " </td>"
                            + "<td align='center'>  </td>"
                            + "<td align='center'>  </td>"
                            + "<td align='left'> " + rsData.getString("INCHARGE_NAME") + " </td>"
                            + "</tr>";

                    rsData.next();
                }
            }
            pMessage = pMessage + "</table>";
            } else {
                pMessage = pMessage + "<br>No Piece Found of Auto Cancellation for " + mntFor + ".<br>";
            }

            pMessage += "<br>";

//            String recievers = "sdmlerp@dineshmills.com";
            String recievers = "narendramotiani@dineshmills.com,yrpatel@dineshmills.com,amitkanti@dineshmills.com,feltpp@dineshmills.com,mitanglad@dineshmills.com,jaydeeppandya@dineshmills.com,siddharthneogi@dineshmills.com,anupsinghchauhan@dineshmills.com,sdmlerp@dineshmills.com";

            pMessage = pMessage + "<br><br>**** This is an auto-generated email, please do not reply ****";

            System.out.println("Recivers : " + recievers);
            System.out.println("pSubject : " + pSubject);
//                System.out.println("pMessage : " + pMessage);

            if (Integer.parseInt(dayGo)==1) {
//            if (Integer.parseInt(dayGo)==19) {
                String responce = DailyMailNotification.sendNotificationMail(pSubject, pMessage, recievers, cc);
                System.out.println("Send Mail Responce : " + responce);
            } else {
                System.out.println("Date is not 1st of Month");
            }

        } catch (Exception e) {
            System.out.println("Error on Mail: " + e.getMessage());
        }
    }
    
    private static void SpecialRequestMonth() {
        try {

            String mntFor = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(NOW(),'%b'),' - ',YEAR(NOW())) FROM DUAL");
//            String mntFor = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 1 MONTH),'%b'),' - ',YEAR(DATE_ADD(NOW(), INTERVAL 1 MONTH))) FROM DUAL");
            String dayGo = data.getStringValueFromDB("SELECT DAY(NOW()) FROM DUAL");

            String pSubject = "List of Special Request Month/Date for " + mntFor + "";
            
            String pMessage = "";
            String cc = "aditya@dineshmills.com,abtewary@dineshmills.com,soumen@dineshmills.com,vdshanbhag@dineshmills.com";
            int SrNo = 1;

            pMessage = pMessage + "<br>Dear All,<br>";
            pMessage = pMessage + "<br>List of Special Request Month/Date for " + mntFor + ".<br><br>";

            Connection Conn;
            Statement stmt;
            ResultSet rsData;

            Conn = data.getConn();
            stmt = Conn.createStatement();
            String strSQL = "SELECT * FROM "
                    + "( "
                    + "SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
//                    + "SELECT LAST_DAY(DATE_ADD(NOW(), INTERVAL 1 MONTH)),DATE_ADD(NOW(), INTERVAL 1 MONTH),P.* FROM PRODUCTION.FELT_SALES_PIECE_REGISTER P "
                    + "WHERE PR_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5) "
                    + "AND EXTRACT(MONTH FROM PR_SPL_REQUEST_DATE) = EXTRACT(MONTH FROM NOW()) AND EXTRACT(YEAR FROM PR_SPL_REQUEST_DATE) = EXTRACT(YEAR FROM NOW()) "
//                    + "AND EXTRACT(MONTH FROM PR_SPL_REQUEST_DATE) = EXTRACT(MONTH FROM DATE_ADD(NOW(), INTERVAL 1 MONTH)) AND EXTRACT(YEAR FROM PR_SPL_REQUEST_DATE) = EXTRACT(YEAR FROM DATE_ADD(NOW(), INTERVAL 1 MONTH)) "
                    + ") AS PR "
                    + "LEFT JOIN   "
                    + "(SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER   "
                    + "WHERE MAIN_ACCOUNT_CODE='210010' AND APPROVED=1 AND CANCELLED=0) AS PM   "
                    + "ON PR.PR_PARTY_CODE=PM.PARTY_CODE   "
                    + "LEFT JOIN   "
                    + "(SELECT H.GROUP_CODE,H.GROUP_DESC,D.PARTY_CODE AS GROUP_PARTY_CODE   "
                    + "FROM PRODUCTION.FELT_GROUP_MASTER_HEADER H,PRODUCTION.FELT_GROUP_MASTER_DETAIL D   "
                    + "WHERE H.GROUP_CODE=D.GROUP_CODE   "
                    + "AND H.APPROVED=1 AND H.CANCELED=0) AS GM   "
                    + "ON PR.PR_PARTY_CODE=GM.GROUP_PARTY_CODE  "
                    + "LEFT JOIN   "
                    + "(SELECT DISTINCT PRODUCT_CODE,PRODUCT_DESC FROM PRODUCTION.FELT_QLT_RATE_MASTER   "
                    + "WHERE APPROVED=1 AND CANCELED=0 ORDER BY DOC_NO DESC) AS QM   "
                    + "ON PR.PR_PRODUCT_CODE=QM.PRODUCT_CODE  "
                    + "LEFT JOIN   "
                    + "(SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MST) AS MP   "
                    + "ON PR.PR_POSITION_NO=MP.POSITION_NO  "
                    + "LEFT JOIN   "
                    + "(SELECT * FROM PRODUCTION.FELT_INCHARGE) AS IM   "
                    + "ON PR.PR_INCHARGE=IM.INCHARGE_CD ";
            
            rsData = stmt.executeQuery(strSQL);
            rsData.first();
            
                System.out.println("String StrSQL : "+strSQL);
                
            if (rsData.getRow() > 0) {
            pMessage = pMessage + "<table border='1' cellpadding='10'>"
                    + "<tr>"
                    + "<th align='center'> SrNo </th>"
                    + "<th align='center'> Special Request Date </th>"
                    + "<th align='center'> Piece No </th>"
                    + "<th align='center'> Party Code </th>"
                    + "<th align='center'> Party Name </th>"
                    + "<th align='center'> Machine No </th>"
                    + "<th align='center'> Position Desc </th>"
                    + "<th align='center'> Product Code </th>"
                    + "<th align='center'> Product Desc </th>"
                    + "<th align='center'> Group </th>"
                    + "<th align='center'> Style </th>"
                    + "<th align='center'> Length </th>"
                    + "<th align='center'> Width </th>"
                    + "<th align='center'> GSM </th>"
                    + "<th align='center'> Sq.Mtr </th>"
                    + "<th align='center'> Weight </th>"
                    + "<th align='center'> Piece Stage </th>"
                    + "<th align='center'> WIP Status </th>"
                    + "<th align='center'> Req Month </th>"
                    + "<th align='center'> OC Month </th>"
                    + "<th align='center'> Current Schedule Month </th>"
                    + "<th align='center'> Incharge </th>"
                    + "</tr>";

            rsData.first();
            if (rsData.getRow() > 0) {
                while (!rsData.isAfterLast()) {

                    pMessage = pMessage + ""
                            + "<tr>"
                            + "<td align='right'> " + (SrNo++) + " </td>"
                            + "<td align='center'> " + EITLERPGLOBAL.formatDate(rsData.getString("PR_SPL_REQUEST_DATE")) + " </td>"
                            + "<td align='left'> " + rsData.getString("PR_PIECE_NO") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_PARTY_CODE") + " </td>"
                            + "<td align='left'> " + rsData.getString("PARTY_NAME") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_MACHINE_NO") + " </td>"
                            + "<td align='left'> " + rsData.getString("POSITION_DESC") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_PRODUCT_CODE") + " </td>"
                            + "<td align='left'> " + rsData.getString("PRODUCT_DESC") + " </td>"
                            + "<td align='left'> " + rsData.getString("PR_GROUP") + " </td>"
                            + "<td align='left'> " + rsData.getString("PR_STYLE") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_LENGTH") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_WIDTH") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_GSM") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_SQMTR") + " </td>"
                            + "<td align='right'> " + rsData.getString("PR_THORITICAL_WEIGHT") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_PIECE_STAGE") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_WIP_STATUS") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_REQUESTED_MONTH") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_OC_MONTHYEAR") + " </td>"
                            + "<td align='center'> " + rsData.getString("PR_CURRENT_SCH_MONTH") + " </td>"
                            + "<td align='left'> " + rsData.getString("INCHARGE_NAME") + " </td>"
                            + "</tr>";

                    rsData.next();
                }
            }
            pMessage = pMessage + "</table>";
            } else {
                pMessage = pMessage + "<br>No Piece Found of Special Request Month/Date for " + mntFor + ".<br>";
            }

            pMessage += "<br>";

            String recievers = "narendramotiani@dineshmills.com,yrpatel@dineshmills.com,amitkanti@dineshmills.com,feltpp@dineshmills.com,mitanglad@dineshmills.com,jaydeeppandya@dineshmills.com,siddharthneogi@dineshmills.com,anupsinghchauhan@dineshmills.com,sdmlerp@dineshmills.com";

            pMessage = pMessage + "<br><br>**** This is an auto-generated email, please do not reply ****";

            System.out.println("Recivers : " + recievers);
            System.out.println("pSubject : " + pSubject);
//                System.out.println("pMessage : " + pMessage);

            if (Integer.parseInt(dayGo)==1) {
//            if (Integer.parseInt(dayGo)==19) {
                String responce = DailyMailNotification.sendNotificationMail(pSubject, pMessage, recievers, cc);
                System.out.println("Send Mail Responce : " + responce);
            } else {
                System.out.println("Date is not 1st of Month");
            }

        } catch (Exception e) {
            System.out.println("Error on Mail: " + e.getMessage());
        }
    }

    
}
