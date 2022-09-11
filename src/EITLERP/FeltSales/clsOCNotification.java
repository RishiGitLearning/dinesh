/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales;

import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.common.JavaMail;
import EITLERP.clsSales_Party;
import EITLERP.data;
import java.sql.*;
import java.util.*;

/**
 *
 * @author root
 */
public class clsOCNotification {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        sendOCNotification();
        sendOCNotificationSDF();
    }

    private static void sendOCNotification() {
        
        //String mntFor = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 2 MONTH),'%b'),' - ',YEAR(DATE_ADD(NOW(), INTERVAL 2 MONTH))) FROM DUAL");
        //CHANGED 20 MARCH 2020
        //String mntFor = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 3 MONTH),'%b'),' - ',YEAR(DATE_ADD(NOW(), INTERVAL 3 MONTH))) FROM DUAL");
        String mntFor = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 2 MONTH),'%b'),' - ',YEAR(DATE_ADD(NOW(), INTERVAL 2 MONTH))) FROM DUAL");

        String pMessage = "";
//        String cc = "anandkumar.pandya@dineshmills.com,rishineekhra@dineshmills.com,gaurang@dineshmills.com,feltoc@dineshmills.com";
        String cc = "feltoc@dineshmills.com";

        try {

            Connection Conn1;
            Statement stmt1;
            ResultSet rsData1;
            Conn1 = data.getConn();
            stmt1 = Conn1.createStatement();
//            rsData1 = stmt1.executeQuery("SELECT PR_PARTY_CODE,CONTACT_PERSON,GROUP_CONCAT(DISTINCT EMAIL_ID,EMAIL_ID2,EMAIL_ID3 SEPARATOR ',') AS EMAIL FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_DETAIL D, PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER H, PRODUCTION.FELT_SALES_PIECE_REGISTER R WHERE H.OC_DOC_NO=D.OC_DOC_NO AND APPROVED=1 AND CANCELED=0 AND PR_PIECE_NO=PIECE_NO AND SELECT_PPC=1 AND SELECT_SALES=1 AND MONTH(OC_DATE)=EXTRACT(MONTH FROM SUBDATE(NOW(), INTERVAL 1 MONTH)) AND YEAR(OC_DATE)=EXTRACT(YEAR FROM SUBDATE(NOW(), INTERVAL 1 MONTH)) AND PR_GROUP != 'SDF' GROUP BY PR_PARTY_CODE ORDER BY PR_PARTY_CODE ");
//            rsData1 = stmt1.executeQuery("SELECT PR_PARTY_CODE,CONTACT_PERSON,GROUP_CONCAT(DISTINCT EMAIL_ID,EMAIL_ID2,EMAIL_ID3 SEPARATOR ',') AS EMAIL FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_DETAIL D, PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER H, PRODUCTION.FELT_SALES_PIECE_REGISTER R WHERE H.OC_DOC_NO=D.OC_DOC_NO AND APPROVED=1 AND CANCELED=0 AND PR_PIECE_NO=PIECE_NO AND SELECT_PPC=1 AND SELECT_SALES=1 AND MONTH(OC_DATE)=4 AND YEAR(OC_DATE)=2019 AND PR_PARTY_CODE IN ('811019','811022','811023','811029','811047','811261','812033') AND PR_GROUP != 'SDF' GROUP BY PR_PARTY_CODE ORDER BY PR_PARTY_CODE ");
//            rsData1 = stmt1.executeQuery("SELECT PR_PARTY_CODE,CONTACT_PERSON,GROUP_CONCAT(DISTINCT EMAIL_ID,EMAIL_ID2,EMAIL_ID3 SEPARATOR ',') AS EMAIL FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_DETAIL D, PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER H, PRODUCTION.FELT_SALES_PIECE_REGISTER R WHERE H.OC_DOC_NO=D.OC_DOC_NO AND APPROVED=1 AND CANCELED=0 AND PR_PIECE_NO=PIECE_NO AND SELECT_PPC=1 AND SELECT_SALES=1 AND MONTH(OC_DATE)=EXTRACT(MONTH FROM SUBDATE(NOW(), INTERVAL 3 MONTH)) AND YEAR(OC_DATE)=EXTRACT(YEAR FROM SUBDATE(NOW(), INTERVAL 3 MONTH)) AND PR_GROUP != 'SDF' GROUP BY PR_PARTY_CODE ORDER BY PR_PARTY_CODE ");
            rsData1 = stmt1.executeQuery("SELECT PR_PARTY_CODE,CONTACT_PERSON,GROUP_CONCAT(DISTINCT EMAIL_ID,EMAIL_ID2,EMAIL_ID3 SEPARATOR ',') AS EMAIL FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_DETAIL D, PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER H, PRODUCTION.FELT_SALES_PIECE_REGISTER R WHERE H.OC_DOC_NO=D.OC_DOC_NO AND APPROVED=1 AND CANCELED=0 AND PR_PIECE_NO=PIECE_NO AND SELECT_PPC=1 AND SELECT_SALES=1 AND H.OC_MONTH = '"+mntFor+"' AND FELT_TYPE='All(Except SDF)' AND PR_GROUP != 'SDF' GROUP BY PR_PARTY_CODE ORDER BY PR_PARTY_CODE ");
            rsData1.first();

            if (rsData1.getRow() > 0) {
                while (!rsData1.isAfterLast()) {

                    pMessage = "<br><br>";

                    pMessage = pMessage + "<div style='min-width:1000px;'>\n"
                            + "	<div style=' text-align:center; min-width:1000px;'><u>ORDER CONFIRMATION</u></div>\n"
                            + "	<br><br>\n"
                            + "	<div style=' width:100%; heigth :200px;'>\n"
                            + "	\n"
                            + "		<div style=' width: 100%; float:left;'>\n"
                            + "			To : " + clsSales_Party.getPartyName(2, rsData1.getString("PR_PARTY_CODE")) + "<br>\n"
                            + "			Party code : " + rsData1.getString("PR_PARTY_CODE") + " <br>\n"
                            + "			Email id : " + rsData1.getString("EMAIL") + "   <br>\n"
                            + "			Kind Attn : " + rsData1.getString("CONTACT_PERSON") + "  	\n"
                            + "			<hr>\n"
                            + "		</div>\n"
                            //                            + "		<div style=' width:50%;  float:left;'>\n"
                            //                            + "			Your order ref: " + Reference + "<br>\n"
                            //                            + "			Date : " + EITLERPGLOBAL.formatDate(Reference_Date) + "<br>\n"
                            //                            + "			Our order acknowledgement no & date: " + rsPieces.getString("PR_OA_NO") + " - " + EITLERPGLOBAL.formatDate(rsPieces.getString("PR_OA_DATE")) + " <br>\n"
                            //                            + "			Our order confirmation no & date: " + rsPieces.getString("PR_OC_NO") + " - " + EITLERPGLOBAL.formatDate(rsPieces.getString("PR_OC_DATE")) + " <br>\n"
                            //                            + "			<hr>\n"
                            //                            + "		</div>\n"
                            + "	\n"
                            + "	</div>\n"
                            + "	<div>\n"
                            + "		Dear sir,\n"
                            + "		<p>\n"
                            + "		<t>In continuation of our aforesaid order acknowledgement in connection with your valued PO as mentioned above, We are pleased to inform you that your following felts covered under our said order acknowledgement will be taken up in our current manufacturing plan so as to make the same ready as per the following schedule.\n"
                            + "	</div>\n";

                    pMessage = pMessage + "	<div>\n"
                            + "		<table border='1' width='100%'>\n"
                            + "<tr>"
                            + "<th> SR NO </th>\n"
                            + "<th> M/C NO </th>\n"
                            + "<th> POSITION </th>\n"
                            + "<th> DESCRIPTION </th>\n"
                            + "<th> PIECE NO </th>\n"
                            + "<th> LENGTH </th>\n"
                            + "<th> WIDTH </th>\n"
                            + "<th> GSM </th>\n"
                            + "<th> SCHEDULE </th>\n"
                            + "<th> CONFIRMATION NO </th>\n"
                            + "<th> CONFIRMATION DATE </th>\n"
                            + "<th> REFERENCE </th>\n"
                            + "<th> REFERENCE DATE </th>\n"
                            + "<th> ACKNOWLEDGE NO </th>\n"
                            + "<th> ACKNOWLEDGE DATE </th>\n"
                            + "</tr>";

                    int SrNo = 1;
                    
//                    ResultSet rsPieces = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_DETAIL D, PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER H, PRODUCTION.FELT_SALES_PIECE_REGISTER R WHERE H.OC_DOC_NO=D.OC_DOC_NO AND APPROVED=1 AND CANCELED=0 AND PR_PIECE_NO=PIECE_NO AND SELECT_PPC=1 AND SELECT_SALES=1 AND MONTH(OC_DATE)=EXTRACT(MONTH FROM SUBDATE(NOW(), INTERVAL 1 MONTH)) AND YEAR(OC_DATE)=EXTRACT(YEAR FROM SUBDATE(NOW(), INTERVAL 1 MONTH)) AND PR_GROUP != 'SDF' AND PR_PARTY_CODE='" + rsData1.getString("PR_PARTY_CODE") + "' ORDER BY PIECE_NO ");
//                    ResultSet rsPieces = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_DETAIL D, PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER H, PRODUCTION.FELT_SALES_PIECE_REGISTER R WHERE H.OC_DOC_NO=D.OC_DOC_NO AND APPROVED=1 AND CANCELED=0 AND PR_PIECE_NO=PIECE_NO AND SELECT_PPC=1 AND SELECT_SALES=1 AND MONTH(OC_DATE)=4 AND YEAR(OC_DATE)=2019 AND PR_GROUP != 'SDF' AND PR_PARTY_CODE='" + rsData1.getString("PR_PARTY_CODE") + "' ORDER BY PIECE_NO ");
//                    ResultSet rsPieces = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_DETAIL D, PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER H, PRODUCTION.FELT_SALES_PIECE_REGISTER R WHERE H.OC_DOC_NO=D.OC_DOC_NO AND APPROVED=1 AND CANCELED=0 AND PR_PIECE_NO=PIECE_NO AND SELECT_PPC=1 AND SELECT_SALES=1 AND MONTH(OC_DATE)=EXTRACT(MONTH FROM SUBDATE(NOW(), INTERVAL 3 MONTH)) AND YEAR(OC_DATE)=EXTRACT(YEAR FROM SUBDATE(NOW(), INTERVAL 3 MONTH)) AND PR_GROUP != 'SDF' AND PR_PARTY_CODE='" + rsData1.getString("PR_PARTY_CODE") + "' ORDER BY PIECE_NO ");
                    ResultSet rsPieces = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_DETAIL D, PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER H, PRODUCTION.FELT_SALES_PIECE_REGISTER R WHERE H.OC_DOC_NO=D.OC_DOC_NO AND APPROVED=1 AND CANCELED=0 AND PR_PIECE_NO=PIECE_NO AND SELECT_PPC=1 AND SELECT_SALES=1 AND H.OC_MONTH = '"+mntFor+"' AND FELT_TYPE='All(Except SDF)' AND PR_GROUP != 'SDF' AND PR_PARTY_CODE='" + rsData1.getString("PR_PARTY_CODE") + "' ORDER BY PIECE_NO ");
                    rsPieces.first();

                    if (rsPieces.getRow() > 0) {
                        while (!rsPieces.isAfterLast()) {

                            String Reference;
                            String Reference_Date;
//                            if(rsPieces.getString("PR_PARTY_CODE").equals("P.O.")){ // On 26/04/2019
                            if (rsPieces.getString("PR_REFERENCE").equals("P.O.")) {
                                Reference = "P.O. - " + rsPieces.getString("PR_PO_NO");
                                Reference_Date = rsPieces.getString("PR_PO_DATE");
                            } else {
                                Reference = rsPieces.getString("PR_REFERENCE");
                                Reference_Date = rsPieces.getString("PR_REFERENCE_DATE");
                            }

                            String PositionDesc = data.getStringValueFromDB("SELECT POSITION_DESC FROM PRODUCTION.FELT_MACHINE_POSITION_MST WHERE POSITION_NO=" + rsPieces.getString("PR_POSITION_NO"));
                            String ProductDesc = data.getStringValueFromDB("SELECT PRODUCT_DESC FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE = '" + rsPieces.getString("PR_PRODUCT_CODE") + "' ORDER BY DOC_NO DESC");

                            pMessage = pMessage + "<tr>"
                                    + "<td> " + (SrNo++) + " </td>\n"
                                    + "<td> " + rsPieces.getString("PR_MACHINE_NO") + " </td>\n"
                                    + "<td>" + PositionDesc + "</td>\n"
                                    + "<td>" + ProductDesc + "</td>\n"
                                    + "<td>" + rsPieces.getString("PIECE_NO") + "</td>\n"
                                    + "<td>" + rsPieces.getString("PR_BILL_LENGTH") + "</td>\n"
                                    + "<td>" + rsPieces.getString("PR_BILL_WIDTH") + "</td>\n"
                                    + "<td>" + rsPieces.getString("PR_BILL_GSM") + "</td>\n"
                                    + "<td>" + rsPieces.getString("PR_OC_MONTHYEAR") + "</td>\n"
                                    + "<td>" + rsPieces.getString("OC_NO") + "</td>\n"
                                    + "<td>" + EITLERPGLOBAL.formatDate(rsPieces.getString("OC_DATE")) + "</td>\n"
                                    + "<td>" + Reference + "</td>\n"
                                    + "<td>" + EITLERPGLOBAL.formatDate(Reference_Date) + "</td>\n"
                                    + "<td>" + rsPieces.getString("PR_OA_NO") + "</td>\n"
                                    + "<td>" + EITLERPGLOBAL.formatDate(rsPieces.getString("PR_OA_DATE")) + "</td>\n"
                                    + "</tr>";

                            rsPieces.next();
                        }
                    }

                    pMessage = pMessage + "</table>";
                    pMessage = pMessage + "";
                    pMessage = pMessage + "</div>\n"
                            + "	<p>\n"
                            + "	 \n"
                            + "	You are requested to kindly ensure that the said felt is lifted as soon as it gets ready. The schedules for other felts (if any) covered under your relevant PO would be confirmed later as mentioned in our order acknowledgement.<p>\n"
                            + "	Assuring you of our best services at all times and meanwhile thanking you once again. \n"
                            + "	<p>\n"
                            + "	</div>\n"
                            + "	\n"
                            + "\n"
                            + "</div>";
                    
//                    String recievers = "sdmlerp@dineshmills.com";
                    String recievers = rsData1.getString("EMAIL");
                    

//                    if (EITLERPGLOBAL.getCurrentDay() == 29) {
                    //if (EITLERPGLOBAL.getCurrentDay() == 1) {
                    if (EITLERPGLOBAL.getCurrentDay() == 26) {
//                        String responce = DailyMailNotification.sendNotificationMail(pSubject, pMessage, recievers, cc);
                        JavaMail.SendMailFeltOC(recievers, pMessage, "Notification : Order Confirmation Mail For the Month of : " + mntFor + " ( " + rsData1.getString("PR_PARTY_CODE") + ", " + clsSales_Party.getPartyName(2, rsData1.getString("PR_PARTY_CODE")) + " )", cc);
                    } else {
                        System.out.println("Date is not start of the month");
                    }

                    rsData1.next();

                }
            }

        } catch (Exception e) {
            System.out.println("Error on Mail: " + e.getMessage());
        }
    }
    
    private static void sendOCNotificationSDF() {
        
        //String mntFor = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 1 MONTH),'%b'),' - ',YEAR(DATE_ADD(NOW(), INTERVAL 1 MONTH))) FROM DUAL");
        //CHANGED 20 MARCH 2020
        //String mntFor = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 2 MONTH),'%b'),' - ',YEAR(DATE_ADD(NOW(), INTERVAL 2 MONTH))) FROM DUAL");
        String mntFor = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 1 MONTH),'%b'),' - ',YEAR(DATE_ADD(NOW(), INTERVAL 1 MONTH))) FROM DUAL");
        
        String pMessage = "";
//        String cc = "anandkumar.pandya@dineshmills.com,rishineekhra@dineshmills.com,gaurang@dineshmills.com,feltoc@dineshmills.com";
        String cc = "feltoc@dineshmills.com";

        try {

            Connection Conn1;
            Statement stmt1;
            ResultSet rsData1;
            Conn1 = data.getConn();
            stmt1 = Conn1.createStatement();
//            rsData1 = stmt1.executeQuery("SELECT PR_PARTY_CODE,CONTACT_PERSON,GROUP_CONCAT(DISTINCT EMAIL_ID,EMAIL_ID2,EMAIL_ID3 SEPARATOR ',') AS EMAIL FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_DETAIL D, PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER H, PRODUCTION.FELT_SALES_PIECE_REGISTER R WHERE H.OC_DOC_NO=D.OC_DOC_NO AND APPROVED=1 AND CANCELED=0 AND PR_PIECE_NO=PIECE_NO AND SELECT_PPC=1 AND SELECT_SALES=1 AND MONTH(OC_DATE)=EXTRACT(MONTH FROM SUBDATE(NOW(), INTERVAL 1 MONTH)) AND YEAR(OC_DATE)=EXTRACT(YEAR FROM SUBDATE(NOW(), INTERVAL 1 MONTH)) AND PR_GROUP = 'SDF' GROUP BY PR_PARTY_CODE ORDER BY PR_PARTY_CODE ");
//            rsData1 = stmt1.executeQuery("SELECT PR_PARTY_CODE,CONTACT_PERSON,GROUP_CONCAT(DISTINCT EMAIL_ID,EMAIL_ID2,EMAIL_ID3 SEPARATOR ',') AS EMAIL FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_DETAIL D, PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER H, PRODUCTION.FELT_SALES_PIECE_REGISTER R WHERE H.OC_DOC_NO=D.OC_DOC_NO AND APPROVED=1 AND CANCELED=0 AND PR_PIECE_NO=PIECE_NO AND SELECT_PPC=1 AND SELECT_SALES=1 AND MONTH(OC_DATE)=EXTRACT(MONTH FROM SUBDATE(NOW(), INTERVAL 2 MONTH)) AND YEAR(OC_DATE)=EXTRACT(YEAR FROM SUBDATE(NOW(), INTERVAL 2 MONTH)) AND PR_GROUP = 'SDF' GROUP BY PR_PARTY_CODE ORDER BY PR_PARTY_CODE ");
            rsData1 = stmt1.executeQuery("SELECT PR_PARTY_CODE,CONTACT_PERSON,GROUP_CONCAT(DISTINCT EMAIL_ID,EMAIL_ID2,EMAIL_ID3 SEPARATOR ',') AS EMAIL FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_DETAIL D, PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER H, PRODUCTION.FELT_SALES_PIECE_REGISTER R WHERE H.OC_DOC_NO=D.OC_DOC_NO AND APPROVED=1 AND CANCELED=0 AND PR_PIECE_NO=PIECE_NO AND SELECT_PPC=1 AND SELECT_SALES=1 AND H.OC_MONTH = '"+mntFor+"' AND FELT_TYPE='SDF' AND PR_GROUP = 'SDF' GROUP BY PR_PARTY_CODE ORDER BY PR_PARTY_CODE ");
            rsData1.first();

            if (rsData1.getRow() > 0) {
                while (!rsData1.isAfterLast()) {

                    pMessage = "<br><br>";

                    pMessage = pMessage + "<div style='min-width:1000px;'>\n"
                            + "	<div style=' text-align:center; min-width:1000px;'><u>ORDER CONFIRMATION</u></div>\n"
                            + "	<br><br>\n"
                            + "	<div style=' width:100%; heigth :200px;'>\n"
                            + "	\n"
                            + "		<div style=' width: 100%; float:left;'>\n"
                            + "			To : " + clsSales_Party.getPartyName(2, rsData1.getString("PR_PARTY_CODE")) + "<br>\n"
                            + "			Party code : " + rsData1.getString("PR_PARTY_CODE") + " <br>\n"
                            + "			Email id : " + rsData1.getString("EMAIL") + "   <br>\n"
                            + "			Kind Attn : " + rsData1.getString("CONTACT_PERSON") + "  	\n"
                            + "			<hr>\n"
                            + "		</div>\n"
                            //                            + "		<div style=' width:50%;  float:left;'>\n"
                            //                            + "			Your order ref: " + Reference + "<br>\n"
                            //                            + "			Date : " + EITLERPGLOBAL.formatDate(Reference_Date) + "<br>\n"
                            //                            + "			Our order acknowledgement no & date: " + rsPieces.getString("PR_OA_NO") + " - " + EITLERPGLOBAL.formatDate(rsPieces.getString("PR_OA_DATE")) + " <br>\n"
                            //                            + "			Our order confirmation no & date: " + rsPieces.getString("PR_OC_NO") + " - " + EITLERPGLOBAL.formatDate(rsPieces.getString("PR_OC_DATE")) + " <br>\n"
                            //                            + "			<hr>\n"
                            //                            + "		</div>\n"
                            + "	\n"
                            + "	</div>\n"
                            + "	<div>\n"
                            + "		Dear sir,\n"
                            + "		<p>\n"
                            + "		<t>In continuation of our aforesaid order acknowledgement in connection with your valued PO as mentioned above, We are pleased to inform you that your following felts covered under our said order acknowledgement will be taken up in our current manufacturing plan so as to make the same ready as per the following schedule.\n"
                            + "	</div>\n";

                    pMessage = pMessage + "	<div>\n"
                            + "		<table border='1' width='100%'>\n"
                            + "<tr>"
                            + "<th> SR NO </th>\n"
                            + "<th> M/C NO </th>\n"
                            + "<th> POSITION </th>\n"
                            + "<th> DESCRIPTION </th>\n"
                            + "<th> PIECE NO </th>\n"
                            + "<th> LENGTH </th>\n"
                            + "<th> WIDTH </th>\n"
                            + "<th> GSM </th>\n"
                            + "<th> SCHEDULE </th>\n"
                            + "<th> CONFIRMATION NO </th>\n"
                            + "<th> CONFIRMATION DATE </th>\n"
                            + "<th> REFERENCE </th>\n"
                            + "<th> REFERENCE DATE </th>\n"
                            + "<th> ACKNOWLEDGE NO </th>\n"
                            + "<th> ACKNOWLEDGE DATE </th>\n"
                            + "</tr>";

                    int SrNo = 1;
                    
//                    ResultSet rsPieces = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_DETAIL D, PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER H, PRODUCTION.FELT_SALES_PIECE_REGISTER R WHERE H.OC_DOC_NO=D.OC_DOC_NO AND APPROVED=1 AND CANCELED=0 AND PR_PIECE_NO=PIECE_NO AND SELECT_PPC=1 AND SELECT_SALES=1 AND MONTH(OC_DATE)=EXTRACT(MONTH FROM SUBDATE(NOW(), INTERVAL 1 MONTH)) AND YEAR(OC_DATE)=EXTRACT(YEAR FROM SUBDATE(NOW(), INTERVAL 1 MONTH)) AND PR_GROUP = 'SDF' AND PR_PARTY_CODE='" + rsData1.getString("PR_PARTY_CODE") + "' ORDER BY PIECE_NO ");
//                    ResultSet rsPieces = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_DETAIL D, PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER H, PRODUCTION.FELT_SALES_PIECE_REGISTER R WHERE H.OC_DOC_NO=D.OC_DOC_NO AND APPROVED=1 AND CANCELED=0 AND PR_PIECE_NO=PIECE_NO AND SELECT_PPC=1 AND SELECT_SALES=1 AND MONTH(OC_DATE)=EXTRACT(MONTH FROM SUBDATE(NOW(), INTERVAL 2 MONTH)) AND YEAR(OC_DATE)=EXTRACT(YEAR FROM SUBDATE(NOW(), INTERVAL 2 MONTH)) AND PR_GROUP = 'SDF' AND PR_PARTY_CODE='" + rsData1.getString("PR_PARTY_CODE") + "' ORDER BY PIECE_NO ");
                    ResultSet rsPieces = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_DETAIL D, PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER H, PRODUCTION.FELT_SALES_PIECE_REGISTER R WHERE H.OC_DOC_NO=D.OC_DOC_NO AND APPROVED=1 AND CANCELED=0 AND PR_PIECE_NO=PIECE_NO AND SELECT_PPC=1 AND SELECT_SALES=1 AND H.OC_MONTH = '"+mntFor+"' AND FELT_TYPE='SDF' AND PR_GROUP = 'SDF' AND PR_PARTY_CODE='" + rsData1.getString("PR_PARTY_CODE") + "' ORDER BY PIECE_NO ");
                    rsPieces.first();

                    if (rsPieces.getRow() > 0) {
                        while (!rsPieces.isAfterLast()) {

                            String Reference;
                            String Reference_Date;
//                            if(rsPieces.getString("PR_PARTY_CODE").equals("P.O.")){ // On 26/04/2019
                            if (rsPieces.getString("PR_REFERENCE").equals("P.O.")) {
                                Reference = "P.O. - " + rsPieces.getString("PR_PO_NO");
                                Reference_Date = rsPieces.getString("PR_PO_DATE");
                            } else {
                                Reference = rsPieces.getString("PR_REFERENCE");
                                Reference_Date = rsPieces.getString("PR_REFERENCE_DATE");
                            }

                            String PositionDesc = data.getStringValueFromDB("SELECT POSITION_DESC FROM PRODUCTION.FELT_MACHINE_POSITION_MST WHERE POSITION_NO=" + rsPieces.getString("PR_POSITION_NO"));
                            String ProductDesc = data.getStringValueFromDB("SELECT PRODUCT_DESC FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE = '" + rsPieces.getString("PR_PRODUCT_CODE") + "' ORDER BY DOC_NO DESC");

                            pMessage = pMessage + "<tr>"
                                    + "<td> " + (SrNo++) + " </td>\n"
                                    + "<td> " + rsPieces.getString("PR_MACHINE_NO") + " </td>\n"
                                    + "<td>" + PositionDesc + "</td>\n"
                                    + "<td>" + ProductDesc + "</td>\n"
                                    + "<td>" + rsPieces.getString("PIECE_NO") + "</td>\n"
                                    + "<td>" + rsPieces.getString("PR_BILL_LENGTH") + "</td>\n"
                                    + "<td>" + rsPieces.getString("PR_BILL_WIDTH") + "</td>\n"
                                    + "<td>" + rsPieces.getString("PR_BILL_GSM") + "</td>\n"
                                    + "<td>" + rsPieces.getString("PR_OC_MONTHYEAR") + "</td>\n"
                                    + "<td>" + rsPieces.getString("OC_NO") + "</td>\n"
                                    + "<td>" + EITLERPGLOBAL.formatDate(rsPieces.getString("OC_DATE")) + "</td>\n"
                                    + "<td>" + Reference + "</td>\n"
                                    + "<td>" + EITLERPGLOBAL.formatDate(Reference_Date) + "</td>\n"
                                    + "<td>" + rsPieces.getString("PR_OA_NO") + "</td>\n"
                                    + "<td>" + EITLERPGLOBAL.formatDate(rsPieces.getString("PR_OA_DATE")) + "</td>\n"
                                    + "</tr>";

                            rsPieces.next();
                        }
                    }

                    pMessage = pMessage + "</table>";
                    pMessage = pMessage + "";
                    pMessage = pMessage + "</div>\n"
                            + "	<p>\n"
                            + "	 \n"
                            + "	You are requested to kindly ensure that the said felt is lifted as soon as it gets ready. The schedules for other felts (if any) covered under your relevant PO would be confirmed later as mentioned in our order acknowledgement.<p>\n"
                            + "	Assuring you of our best services at all times and meanwhile thanking you once again. \n"
                            + "	<p>\n"
                            + "	</div>\n"
                            + "	\n"
                            + "\n"
                            + "</div>";
                    
//                    String recievers = "sdmlerp@dineshmills.com";
                    String recievers = rsData1.getString("EMAIL");
                    

//                    if (EITLERPGLOBAL.getCurrentDay() == 29) {
//                    if (EITLERPGLOBAL.getCurrentDay() == 1) {
                    if (EITLERPGLOBAL.getCurrentDay() == 26) {
//                        String responce = DailyMailNotification.sendNotificationMail(pSubject, pMessage, recievers, cc);
                        JavaMail.SendMailFeltOC(recievers, pMessage, "Notification : Order Confirmation Mail For the Month of : " + mntFor + " ( " + rsData1.getString("PR_PARTY_CODE") + ", " + clsSales_Party.getPartyName(2, rsData1.getString("PR_PARTY_CODE")) + " )", cc);
                    } else {
                        System.out.println("Date is not start of the month");
                    }

                    rsData1.next();

                }
            }

        } catch (Exception e) {
            System.out.println("Error on Mail: " + e.getMessage());
        }
    }

}
