 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.common;

import EITLERP.EITLERPGLOBAL;
import EITLERP.clsHierarchy;
import EITLERP.clsModules;
import EITLERP.clsSales_Party;
import EITLERP.clsUser;
import EITLERP.data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;
import javax.mail.*;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author root
 */
public class JavaMail_14122018 {

    public static void SendMail(String to, String pMessage, String pSubject, String cc)
            throws Exception {

        String from = "sdmlerp@dineshmills.com";
        String Password = "K.0-H%dmc20ks.00";//Sdml@390020

        // Get system properties
        Properties props = System.getProperties();

        String SMTPHostIp = "34.206.245.89";

        // Setup mail server
        System.out.println("smtpHost  " + SMTPHostIp);
        props.put("mail.smtp.host", SMTPHostIp);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.reportsuccess", "true");
        // Get session
        Session session
                = Session.getInstance(props);

        // Define message
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));

        String[] str_to = to.split(",");
        for (String str1 : str_to) {
           
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(str1));
        }

        String[] str_cc = cc.split(",");
        for (String str1 : str_cc) {
            if (!str1.trim().equals("")) {
                message.addRecipient(Message.RecipientType.CC, new InternetAddress(str1));
            }
        }

        message.setSubject(pSubject);
//        message.setText(pMessage);
        
        
//        message.setContent(pMessage, "text/html");
        // Create the message part
//        BodyPart messageBodyPart = new MimeBodyPart();
//
//        // Fill the message
//        messageBodyPart.setText(pMessage);
//
//        Multipart multipart = new MimeMultipart();
//        multipart.addBodyPart(messageBodyPart);
//
//        // Put parts in message
//        message.setContent(multipart);
        
        Multipart mp = new MimeMultipart();
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(pMessage, "text/html");
        mp.addBodyPart(htmlPart);
        message.setContent(mp);
        
        
        // Send message
        Transport tr = session.getTransport("smtp");
        tr.connect(SMTPHostIp, from, Password);
        tr.sendMessage(message, message.getAllRecipients());
        tr.close();

    }

    public static String sendFinalApprovalMail(int MODULE_ID, String DOC_NO, String DOC_DATE, String PARTY_CODE, int USER_ID, int HIERARCHY_ID, boolean SEND_TO_ALL) {
        if (USER_ID == 0) {
            try {
                SendMail("sdmlerp@dineshmills.com", "Mail Sending Failed due to USER ID not set", "Mail Not Sent : " + DOC_NO, "");
            } catch (Exception e) {
                //System.out.println("Error Msg "+e.getMessage());
                //e.printStackTrace();
            }
            return "User Id not set, please set User Id";
        }

        String responce = data.getStringValueFromDB("SELECT HIERARCHY_ID FROM DINESHMILLS.D_COM_HIERARCHY where HIERARCHY_ID=" + HIERARCHY_ID + " AND MODULE_ID=" + MODULE_ID + "");
        if (responce.equals("")) {

            try {
                SendMail("sdmlerp@dineshmills.com", "Mail Sending Failed due to HIERARCHY ID AND MODULE ID missed matched,<br><br><br> Module Id " + MODULE_ID + " and Hierarchy Id " + HIERARCHY_ID + " missed matched", "Mail Not Sent : " + DOC_NO, "");
            } catch (Exception e) {
                //System.out.println("Error Msg "+e.getMessage());
                //e.printStackTrace();
            }
            return "Module Id " + MODULE_ID + " and Hierarchy Id " + HIERARCHY_ID + " missed matched";
        }

        if (HIERARCHY_ID == 0) {
            try {
                SendMail("sdmlerp@dineshmills.com", "Mail Sending Failed due to HIERARCHY ID not set ", "Mail Not Sent : " + DOC_NO, "");
            } catch (Exception e) {
                //System.out.println("Error Msg "+e.getMessage());
                //e.printStackTrace();
            }
            return "Hierarchy Id not set, Please set Hierarchy Id";
            //send copy to SDMLERP 

        }

        if (MODULE_ID == 0) {
            try {
                SendMail("sdmlerp@dineshmills.com", "Mail Sending Failed due to MODULE ID not set ", "Mail Not Sent : " + DOC_NO, "");
            } catch (Exception e) {
                //System.out.println("Error Msg "+e.getMessage());
                //e.printStackTrace();
            }
            return "Module not set, Please set Module Id";
        }

        String DOC_TYPE = clsModules.getModuleName(EITLERPGLOBAL.gCompanyID, MODULE_ID);

        String pMessage = "";
        String pSubject = "Final Approved, " + DOC_TYPE + " , DOC NO : " + DOC_NO;
        String cc = "";

        if (MODULE_ID == 80 || MODULE_ID == 715 || MODULE_ID == 741 || MODULE_ID == 724 || MODULE_ID == 725 || MODULE_ID == 763  || MODULE_ID == 774) {
            cc = "feltsalesnotification@dineshmills.com";
        }

        if (MODULE_ID == 622)
        {
            cc = "feltwh@dineshmills.com";
        }
        
        if (PARTY_CODE.equals("")) {
            pMessage = "Document Name : " + DOC_TYPE + ",<br>Document No : " + DOC_NO + ",<br>Document Date : " + DOC_DATE + ",<br><br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br><br><br>";
        } else {
            pMessage = "Document Name : " + DOC_TYPE + ",<br>Document No : " + DOC_NO + ",<br>Document Date : " + DOC_DATE + ",<br>Party Code : " + PARTY_CODE + ",<br>Party Name : " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, PARTY_CODE) + "<br><br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br><br><br>";

            if (MODULE_ID == 604) {
                pMessage = "Diversion No : " + DOC_NO + ",<br>Diversion Date : " + DOC_DATE + ",<br><br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br><br><br>";

                //<br>Party Code : "+PARTY_CODE+",<br>Party Name : "+clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, PARTY_CODE)+"
                try {
                    Connection Conn1;
                    Statement stmt1;
                    ResultSet rsData1;

                    Conn1 = data.getConn();
                    stmt1 = Conn1.createStatement();
                    rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION where SD_ORDER_NO = '" + DOC_NO + "'");
                    rsData1.first();

                    String Existing_Piece_No = rsData1.getString("ORIGINAL_PIECE_NO");

                    pMessage = pMessage + "<br> :: <u>EXISTING PIECE DETAILS</u> ::<br><br>";
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO = '" + Existing_Piece_No + "'");
                        rsData.first();

                        pMessage = pMessage + "<br> PIECE NO : " + rsData.getString("PR_PIECE_NO");
                        pMessage = pMessage + "<br> PARTY CODE : " + rsData.getString("PR_PARTY_CODE") + " - " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData.getString("PR_PARTY_CODE"));
                        pMessage = pMessage + "<br> PRODUCT GROUP : " + rsData.getString("PR_GROUP");
                        //pMessage = pMessage + "<br> PRODUCT CODE : " + rsData.getString("PR_PRODUCT_CODE");
                        //pMessage = pMessage + "<br> ORDER LENGTH : "+rsData.getString("PR_LENGTH")+" ,     ORDER WIDTH :  "+rsData.getString("PR_WIDTH")+" ,    ORDER GSM : "+rsData.getString("PR_GSM");
                        //pMessage = pMessage + "<br> ACTUAL LENGTH : "+rsData.getString("PR_ACTUAL_LENGTH")+" ,     ACTUAL WIDTH : "+rsData.getString("PR_ACTUAL_WIDTH")+" ,     ACTUAL WEIGHT : "+rsData.getString("PR_ACTUAL_WEIGHT");
                        //pMessage = pMessage + "<br> BILL LENGTH : "+rsData.getString("PR_BILL_LENGTH")+" ,     BILL WIDTH : "+rsData.getString("PR_BILL_WIDTH")+" ,     BILL GSM : "+rsData.getString("PR_BILL_GSM")+" ,      BILL WEIGHT : "+rsData.getString("PR_BILL_WEIGHT");

                        pMessage = pMessage + "<br><br>";
                        pMessage = pMessage + "<table border='1'><tr><th></th><th>LENGTH</th><th align='center'> WIDTH </th><th align='center'> GSM </th><th align='center'>WEIGHT</th><th align='center'>PRODUCT CODE</th></tr>";
                        
                        pMessage = pMessage + "<tr>"
                                        + "<td align='center'>"
                                        + "ORDER"
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_LENGTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_WIDTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_GSM") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_THORITICAL_WEIGHT") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_PRODUCT_CODE") + ""
                                        + "</td>"
                                        + "</tr>";
                        
                        pMessage = pMessage + "<tr>"
                                        + "<td align='center'>"
                                        + "ACTUAL"
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_ACTUAL_LENGTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_ACTUAL_WIDTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_ACTUAL_WEIGHT") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "-"
                                        + "</td>"
                                        + "</tr>";
                        
                        pMessage = pMessage + "<tr>"
                                        + "<td align='center'>"
                                        + "BILL"
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_LENGTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_WIDTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_GSM") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_WEIGHT") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_PRODUCT_CODE") + ""
                                        + "</td>"
                                        + "</tr>"
                                        + "</table>";
                        
                        
                        
//                        pMessage = pMessage + "<br>-----------------------------------------------------------------------------";
//                        pMessage = pMessage + "<br>\t       |  LENGTH  |\tWIDTH  |\tGSM  |\tWEIGHT      |";
//                        pMessage = pMessage + "<br>-----------------------------------------------------------------------------";
//                        pMessage = pMessage + "<br>  ORDER\t|   " + rsData.getString("PR_LENGTH") + "  |\t" + rsData.getString("PR_WIDTH") + "  |\t" + rsData.getString("PR_GSM") + "  |\t" + rsData.getString("PR_THORITICAL_WEIGHT") + "\t|";
//                        pMessage = pMessage + "<br> ACTUAL\t|   " + rsData.getString("PR_ACTUAL_LENGTH") + "  |\t" + rsData.getString("PR_ACTUAL_WIDTH") + "  |\t      |\t" + rsData.getString("PR_ACTUAL_WEIGHT") + "\t|";
//                        pMessage = pMessage + "<br>   BILL\t|   " + rsData.getString("PR_BILL_LENGTH") + "  |\t" + rsData.getString("PR_BILL_WIDTH") + "  |\t" + rsData.getString("PR_BILL_GSM") + "  |\t" + rsData.getString("PR_BILL_WEIGHT") + "\t|";
//                        pMessage = pMessage + "<br>-----------------------------------------------------------------------------";

                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }

                    String New_Piece_No = rsData1.getString("D_PIECE_NO");
                    pMessage = pMessage + "<br><br><br>";
                    pMessage = pMessage + "<br> :: <u>ORDER(DIVERSION) PIECE DETAILS</u> ::";
                    pMessage = pMessage + "<br><br><br>";
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO = '" + New_Piece_No + "'");
                        rsData.first();

                        pMessage = pMessage + "<br> PIECE NO : " + rsData.getString("PR_PIECE_NO");
                        pMessage = pMessage + "<br> PARTY CODE : " + rsData.getString("PR_PARTY_CODE") + " - " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData.getString("PR_PARTY_CODE"));
                        pMessage = pMessage + "<br> PRODUCT GROUP : " + rsData.getString("PR_GROUP");
                        //pMessage = pMessage + "<br> PRODUCT CODE : " + rsData.getString("PR_PRODUCT_CODE");
                        //pMessage = pMessage + "<br> ORDER LENGTH : "+rsData.getString("PR_LENGTH")+" ,     ORDER WIDTH :  "+rsData.getString("PR_WIDTH")+" ,    ORDER GSM : "+rsData.getString("PR_GSM");
                        //pMessage = pMessage + "<br> ACTUAL LENGTH : "+rsData.getString("PR_ACTUAL_LENGTH")+" ,     ACTUAL WIDTH : "+rsData.getString("PR_ACTUAL_WIDTH")+" ,     ACTUAL WEIGHT : "+rsData.getString("PR_ACTUAL_WEIGHT");
                        //pMessage = pMessage + "<br> BILL LENGTH : "+rsData.getString("PR_BILL_LENGTH")+" ,     BILL WIDTH : "+rsData.getString("PR_BILL_WIDTH")+" ,     BILL GSM : "+rsData.getString("PR_BILL_GSM")+" ,      BILL WEIGHT : "+rsData.getString("PR_BILL_WEIGHT");

                        pMessage = pMessage + "<br><br>";
                        pMessage = pMessage + "<table border='1'><tr><th></th><th>LENGTH</th><th align='center'> WIDTH </th><th align='center'> GSM </th><th align='center'>WEIGHT</th><th align='center'>PRODUCT CODE</th></tr>";
                        
                        pMessage = pMessage + "<tr>"
                                        + "<td align='center'>"
                                        + "ORDER"
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_LENGTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_WIDTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_GSM") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_THORITICAL_WEIGHT") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_PRODUCT_CODE") + ""
                                        + "</td>"
                                        + "</tr>";
                        
                        pMessage = pMessage + "<tr>"
                                        + "<td align='center'>"
                                        + "ACTUAL"
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_ACTUAL_LENGTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_ACTUAL_WIDTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_ACTUAL_WEIGHT") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "-"
                                        + "</td>"
                                        + "</tr>";
                        
                        pMessage = pMessage + "<tr>"
                                        + "<td align='center'>"
                                        + "BILL"
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_LENGTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_WIDTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_GSM") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_WEIGHT") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_PRODUCT_CODE") + ""
                                        + "</td>"
                                        + "</tr>"
                                        + "</table>";
                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }

                    pMessage = pMessage + "<br><br><br>";
                    pMessage = pMessage + "<br> :: ACTION DETAILS ::";
                    pMessage = pMessage + "<br><br><br>";

                    boolean NOACTION = false;
                    if (("".equals(rsData1.getString("ACTION1")) || rsData1.getString("ACTION1") == null)
                            && ("".equals(rsData1.getString("ACTION2")) || rsData1.getString("ACTION2") == null)
                            && ("".equals(rsData1.getString("ACTION3")) || rsData1.getString("ACTION3") == null)
                            && ("".equals(rsData1.getString("ACTION4")) || rsData1.getString("ACTION4") == null)
                            && ("".equals(rsData1.getString("ACTION5")) || rsData1.getString("ACTION5") == null)
                            && ("".equals(rsData1.getString("ACTION6")) || rsData1.getString("ACTION6") == null)
                            && ("".equals(rsData1.getString("ACTION7")) || rsData1.getString("ACTION7") == null)
                            && ("".equals(rsData1.getString("ACTION8")) || rsData1.getString("ACTION8") == null)
                            && ("".equals(rsData1.getString("ACTION9")) || rsData1.getString("ACTION9") == null)
                            && ("".equals(rsData1.getString("ACTION10")) || rsData1.getString("ACTION10") == null)
                            && ("".equals(rsData1.getString("ACTION11")) || rsData1.getString("ACTION11") == null)
                            && ("".equals(rsData1.getString("ACTION12")) || rsData1.getString("ACTION12") == null)
                            && ("".equals(rsData1.getString("ACTION13")) || rsData1.getString("ACTION13") == null)
                            && ("".equals(rsData1.getString("ACTION14")) || rsData1.getString("ACTION14") == null)
                            && ("".equals(rsData1.getString("ACTION15")) || rsData1.getString("ACTION15") == null)
                            && ("".equals(rsData1.getString("ACTION16")) || rsData1.getString("ACTION16") == null)
                            && ("".equals(rsData1.getString("ACTION17")) || rsData1.getString("ACTION17") == null)) {
                        NOACTION = true;
                        pMessage = pMessage + "<u>NO ACTION</u><br><br>";
                    }

                    if (!NOACTION) {
                        if (!"".equals(rsData1.getString("ACTION1"))) {
                            pMessage = pMessage + "REDUCTION IN WIDTH : " + rsData1.getString("ACTION1") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION2"))) {
                            pMessage = pMessage + "INCREASE IN WIDTH : " + rsData1.getString("ACTION2") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION3"))) {
                            pMessage = pMessage + "REDUCTION IN LENGTH : " + rsData1.getString("ACTION3") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION4"))) {
                            pMessage = pMessage + "INCREASE IN GSM : " + rsData1.getString("ACTION4") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION5"))) {
                            pMessage = pMessage + "DECREASE IN GSM : " + rsData1.getString("ACTION5") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION6"))) {
                            pMessage = pMessage + "SEAMING REQUIRED : " + rsData1.getString("ACTION6") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION7"))) {
                            pMessage = pMessage + "TAGGING STYLE : " + rsData1.getString("ACTION7") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION8"))) {
                            pMessage = pMessage + "TAGGING PRODUCT CODE : " + rsData1.getString("ACTION8") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION9"))) {
                            pMessage = pMessage + "TAGGING LENGTH : " + rsData1.getString("ACTION9") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION10"))) {
                            pMessage = pMessage + "TAGGING WIDTH : " + rsData1.getString("ACTION10") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION11"))) {
                            pMessage = pMessage + "TAGGING GSM : " + rsData1.getString("ACTION11") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION12"))) {
                            pMessage = pMessage + "TAGGING CFM : " + rsData1.getString("ACTION12") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION13"))) {
                            pMessage = pMessage + "TAGGING THICKNESS : " + rsData1.getString("ACTION13") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION14"))) {
                            pMessage = pMessage + "TAGGING WEIGHT : " + rsData1.getString("ACTION14") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION15"))) {
                            pMessage = pMessage + "TRIM TO BE RETAINED BY PRODUCTION : " + rsData1.getString("ACTION15") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION16"))) {
                            pMessage = pMessage + "TRIM TO BE RETAINED BY WH WITH FELT : " + rsData1.getString("ACTION16") + "<br>";
                        }

                    }
                    //
                    pMessage = pMessage + "<br><br>DIVERSION REMARK : " + rsData1.getString("D_REMARK") + " <br><br>";
                } catch (Exception e) {
                    System.out.println("Error on Mail: " + e.getMessage());
                }

            }

            
            
            
            if(MODULE_ID == 602)
            {
                // ORDER ENTRY START 
                
                pMessage = "Felt Order No : " + DOC_NO + ",<br>Order Date : " + DOC_DATE + ",<br><br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br><br><br>";

                //<br>Party Code : "+PARTY_CODE+",<br>Party Name : "+clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, PARTY_CODE)+"
                try {
                    Connection Conn1;
                    Statement stmt1;
                    ResultSet rsData1;

                    Conn1 = data.getConn();
                    stmt1 = Conn1.createStatement();
                    rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_HEADER H,PRODUCTION.FELT_SALES_ORDER_DETAIL D  " +
                                                " where H.S_ORDER_NO=D.S_ORDER_NO " +
                                                " AND H.S_ORDER_NO = '" + DOC_NO + "'");
                    rsData1.first();

                    pMessage = pMessage + "<br><br>";
                   
//                        Connection Conn;
//                        Statement stmt;
//                        ResultSet rsData;

//                        Conn = data.getConn();
//                        stmt = Conn.createStatement();
//                        rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO = '" + Existing_Piece_No + "'");
//                        rsData.first();

                        //pMessage = pMessage + "<br> PIECE NO : " + rsData.getString("PR_PIECE_NO");
                        pMessage = pMessage + "<br> PARTY CODE : " + rsData1.getString("PARTY_CODE") + " - " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData1.getString("PARTY_CODE"));
                        pMessage = pMessage + "<br> REFERENCE : " + rsData1.getString("REFERENCE");
                        pMessage = pMessage + "<br> REFERENCE DATE : " + rsData1.getString("REFERENCE_DATE");
                        pMessage = pMessage + "<br> REMARK : "+rsData1.getString("REMARK");
                        
                        pMessage = pMessage + "<br><br><u>ORDER DETAILS</u><br>";
                        pMessage = pMessage + "<table border='1'>"
                                + "<tr>"
                                + "<th align='center'> PIECE NO </th>"
                                + "<th align='center'> UPN </th>"
                                + "<th align='center'> MACHINE NO </th>"
                                + "<th align='center'> POSITION </th>"
                                + "<th align='center'> POSITION DESC </th>"
                                + "<th align='center'> PRODUCT CODE </th>"
                                + "<th align='center'> GROUP </th>"
                                + "<th align='center'> LENGTH </th>"
                                + "<th align='center'> WIDTH </th>"
                                + "<th align='center'> GSM </th>"
                                + "<th align='center'> WEIGHT </th>"
                                + "<th align='center'> SQMTR </th>"
                                + "<th align='center'> STYLE </th>"
                                + "<th align='center'> REQ MONTH </th>"
                                + "</tr>";
                        
                        rsData1.first();
                       
                        if (rsData1.getRow() > 0) {
                            while (!rsData1.isAfterLast()){ 
                                    pMessage = pMessage + ""
                                        + "<tr>"
                                        + "<td align='center'> "+rsData1.getString("PIECE_NO")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("UPN")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("MACHINE_NO")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("POSITION")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("POSITION_DESC")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("PRODUCT_CODE")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("S_GROUP")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("LENGTH")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("WIDTH")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("GSM")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("THORITICAL_WIDTH")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("SQ_MTR")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("STYLE")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("REQ_MONTH")+" </td>"
                                        + "</tr>";
                                    rsData1.next();
                            }
                        }
                        pMessage = pMessage + "</table>";

                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }
                // ORDER ENTRY END
            }
            
           if(MODULE_ID == 774)
            {
                // WIP Review START 
                
                pMessage = "WIP Piece Review No : " + DOC_NO + ",<br>Review Entry Date : " + DOC_DATE + ",<br><br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br><br><br>";

                try {
                    Connection Conn1;
                    Statement stmt1;
                    ResultSet rsData1;

                    Conn1 = data.getConn();
                    stmt1 = Conn1.createStatement();
                    rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_AMENDMENT_HEADER_WIP H, " +
                                                "PRODUCTION.FELT_SALES_PIECE_AMENDMENT_DETAIL_WIP D " +
                                                "WHERE H.PIECE_AMEND_NO=D.PIECE_AMEND_NO AND H.PIECE_AMEND_NO='" + DOC_NO + "'");
                    rsData1.first();

                    pMessage = pMessage + "<br><br>";
                   
//                        Connection Conn;
//                        Statement stmt;
//                        ResultSet rsData;

//                        Conn = data.getConn();
//                        stmt = Conn.createStatement();
//                        rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO = '" + Existing_Piece_No + "'");
//                        rsData.first();

                        //pMessage = pMessage + "<br> PIECE NO : " + rsData.getString("PR_PIECE_NO");
                        pMessage = pMessage + "<br> PARTY CODE : " + rsData1.getString("MM_PARTY_CODE") + " - " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData1.getString("MM_PARTY_CODE"));
                        pMessage = pMessage + "<br> MACHINE NO : " + rsData1.getString("MM_MACHINE_NO");
                        pMessage = pMessage + "<br> POSITION NO : " + rsData1.getString("MM_MACHINE_POSITION");
                        pMessage = pMessage + "<br> POSITION DESCRIPTION : " + data.getStringValueFromDB("SELECT POSITION_DESC FROM PRODUCTION.FELT_MACHINE_POSITION_MST WHERE POSITION_NO='" + rsData1.getString("MM_MACHINE_POSITION") + "'");
                        String Position_Des_No = data.getStringValueFromDB("SELECT POSITION_DESIGN_NO FROM PRODUCTION.FELT_MACHINE_POSITION_MST where POSITION_NO="+rsData1.getString("MM_MACHINE_POSITION"));
                        pMessage = pMessage + "<br> POSITION DESIGN NO : "+Position_Des_No;
                        pMessage = pMessage + "<br> UPN : "+rsData1.getString("MM_PARTY_CODE")+""+rsData1.getString("MM_MACHINE_NO")+""+Position_Des_No;
                        
                        
                        pMessage = pMessage + "<br><br><u>PIECE DETAILS</u><br>";
                        pMessage = pMessage + "<table border='1'>"
                                + "<tr>"
                                + "<th align='center'> PIECE NO </th>"
                                + "<th align='center'> CHANGE POSIBLE</th>"
                                + "<th align='center'> OBSOLETE</th>"
                                + "<th align='center'> APPLY CHANGE </th>"
                                + "<th align='center'> PROD. REMARK </th>"
                                + "<th align='center'> STYLE </th>"
                                + "<th align='center'> UPDATE STYLE </th>"
                                + "<th align='center'> LENGTH </th>"
                                + "<th align='center'> UPDATED LENGTH </th>"
                                + "<th align='center'> WIDTH </th>"
                                + "<th align='center'> UPDATED WIDTH </th>"
                                + "<th align='center'> GSM </th>"
                                + "<th align='center'> UPDATED GSM </th>"
                                + "<th align='center'> PROD. CODE </th>"
                                + "<th align='center'> UPDATED PROD. CODE </th>"
                                + "<th align='center'> PIECE STAGE </th>"
                                + "<th align='center'> REMARKS </th>"
                                + "<th align='center'> EXPECTED DISPACH </th>"
                                + "</tr>";
                        
                        rsData1.first();
                       
                        if (rsData1.getRow() > 0) {
                            while (!rsData1.isAfterLast()){ 
                                String Change_Posibility = "";
                                String Obsolete = "";
                                String Apply_Chage = "";
                                if("1".equals(rsData1.getString("CHANGE_POSIBILITY")))
                                {
                                    Change_Posibility = "YES";
                                }
                                if("1".equals(rsData1.getString("DELINK")))
                                {
                                    Obsolete = "YES";
                                }
                                if("1".equals(rsData1.getString("ACTUAL_CHANGE")))
                                {
                                    Apply_Chage = "YES";
                                }
                                pMessage = pMessage + ""
                                    + "<tr>"
                                    + "<td align='center'> "+rsData1.getString("PIECE_NO")+" </td>"
                                    + "<td align='center'> "+Change_Posibility+" </td>"
                                    + "<td align='center'> "+Obsolete+" </td>"
                                    + "<td align='center'> "+Apply_Chage+" </td>"
                                    + "<td align='center'> "+rsData1.getString("PROD_REMARKS")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("STYLE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("STYLE_UPDATED")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("LENGTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("LENGTH_UPDATED")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("WIDTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("WIDTH_UPDATED")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("GSM")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("GSM_UPDATED")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("PRODUCTCODE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("PRODUCTCODE_UPDATED")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("PIECE_STAGE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("REMARKS")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("EXPECTED_DISPATCH")+" </td>"
                                    + "</tr>";
                                rsData1.next();
                            }
                        }
                        pMessage = pMessage + "</table>";

                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }
                // WIP Review END
            }
            
            if(MODULE_ID == 763)
            {
                // STOCK Review START 
                
                pMessage = "STOCK Piece Review No : " + DOC_NO + ",<br>Review Entry Date : " + DOC_DATE + ",<br><br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br><br><br>";

                try {
                    Connection Conn1;
                    Statement stmt1;
                    ResultSet rsData1;

                    Conn1 = data.getConn();
                    stmt1 = Conn1.createStatement();
                    rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_STOCK_HEADER H, " +
                                                "PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_STOCK_DETAIL D " +
                                                "WHERE H.PIECE_AMEND_STOCK_NO=D.PIECE_AMEND_STOCK_NO AND H.PIECE_AMEND_STOCK_NO='" + DOC_NO + "'");
                    rsData1.first();

                    pMessage = pMessage + "<br><br>";
                   
//                        Connection Conn;
//                        Statement stmt;
//                        ResultSet rsData;

//                        Conn = data.getConn();
//                        stmt = Conn.createStatement();
//                        rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO = '" + Existing_Piece_No + "'");
//                        rsData.first();

                        //pMessage = pMessage + "<br> PIECE NO : " + rsData.getString("PR_PIECE_NO");
                        pMessage = pMessage + "<br> PARTY CODE : " + rsData1.getString("MM_PARTY_CODE") + " - " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData1.getString("MM_PARTY_CODE"));
                        pMessage = pMessage + "<br> MACHINE NO : " + rsData1.getString("MM_MACHINE_NO");
                        pMessage = pMessage + "<br> POSITION NO : " + rsData1.getString("MM_MACHINE_POSITION");
                        pMessage = pMessage + "<br> POSITION DESCRIPTION : " + data.getStringValueFromDB("SELECT POSITION_DESC FROM PRODUCTION.FELT_MACHINE_POSITION_MST WHERE POSITION_NO='" + rsData1.getString("MM_MACHINE_POSITION") + "'");
                        String Position_Des_No = data.getStringValueFromDB("SELECT POSITION_DESIGN_NO FROM PRODUCTION.FELT_MACHINE_POSITION_MST where POSITION_NO="+rsData1.getString("MM_MACHINE_POSITION"));
                        pMessage = pMessage + "<br> POSITION DESIGN NO : "+Position_Des_No;
                        pMessage = pMessage + "<br> UPN : "+rsData1.getString("MM_PARTY_CODE")+""+rsData1.getString("MM_MACHINE_NO")+""+Position_Des_No;
                        
                        pMessage = pMessage + "<br><br><u>PIECE DETAILS</u><br>";
                        pMessage = pMessage + "<table border='1'>"
                                + "<tr>"
                                + "<th align='center'> PIECE NO </th>"
                                + "<th align='center'> OBSOLETE</th>"
                                + "<th align='center'> STYLE </th>"
                                + "<th align='center'> UPDATE STYLE </th>"
                                + "<th align='center'> LENGTH </th>"
                                + "<th align='center'> UPDATED LENGTH </th>"
                                + "<th align='center'> WIDTH </th>"
                                + "<th align='center'> UPDATED WIDTH </th>"
                                + "<th align='center'> GSM </th>"
                                + "<th align='center'> UPDATED GSM </th>"
                                + "<th align='center'> PIECE STAGE </th>"
                                + "<th align='center'> EXPECTED DISPACH </th>"
                                + "</tr>";
                        
                        rsData1.first();
                       
                        if (rsData1.getRow() > 0) {
                            while (!rsData1.isAfterLast()){ 
                               
                                String Obsolete = "";
                                
                                
                                if("1".equals(rsData1.getString("SELECTED")))
                                {
                                    Obsolete = "YES";
                                }
                                
                                pMessage = pMessage + ""
                                    + "<tr>"
                                    + "<td align='center'> "+rsData1.getString("PIECE_NO")+" </td>"
                                    + "<td align='center'> "+Obsolete+" </td>"
                                    + "<td align='center'> "+rsData1.getString("STYLE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("STYLE_UPDATED")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("LENGTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("LENGTH_UPDATED")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("WIDTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("WIDTH_UPDATED")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("GSM")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("GSM_UPDATED")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("PIECE_STAGE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("EXPECTED_DISPATCH")+" </td>"
                                    + "</tr>";
                                rsData1.next();
                            }
                        }
                        pMessage = pMessage + "</table>";

                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }
                // STOCK Review END
            }
            
            //Felt Stock Tagging
            if(MODULE_ID == 622)
            {
                // Felt Stock Tagging
                
                pMessage = "Felt Stock Tagging Doc No : " + DOC_NO + ",<br> Doc Date : " + DOC_DATE + ",<br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br>";

                try {
                    Connection Conn1;
                    Statement stmt1;
                    ResultSet rsData1;

                    Conn1 = data.getConn();
                    stmt1 = Conn1.createStatement();
                    rsData1 = stmt1.executeQuery("SELECT * FROM  PRODUCTION.FELT_PIECE_AMEND WHERE FELT_AMEND_ID='" + DOC_NO + "'");
                    rsData1.first();

                    pMessage = pMessage + "<br><br>";
                   
                        pMessage = pMessage + "<br> STOCK TAGGING DATE : "+EITLERPGLOBAL.formatDate(rsData1.getString("APPROVED_DATE"));
                        pMessage = pMessage + "<br> Tagging Reason : "+data.getStringValueFromDB("SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID = 'PIECE_AMEND' AND PARA_CODE="+rsData1.getString("FELT_AMEND_REASON"));
                        pMessage = pMessage + "<br><br><u>TAGGING PIECE DETAILS</u><br>";
                        pMessage = pMessage + "<table border='1'>"
                                + "<tr>"
                                + "<th align='center'> Piece No </th>"
                                + "<th align='center'> Party Code </th>"
                                + "<th align='center'> Party Name </th>"
                                + "<th align='center'> Order Date </th>"
                                + "<th align='center'> Product Code </th>"
                                + "<th align='center'> Group </th>"
                                + "<th align='center'> Style </th>"
                                + "<th align='center'> Length </th>"
                                + "<th align='center'> Width </th>"                                
                                + "<th align='center'> GSM </th>"
                                + "<th align='center'> Thoritical Weight </th>"                                
                                + "<th align='center'> Actual Weight </th>"                                
                                + "<th align='center'> Sqmtr </th>"
                                + "<th align='center'> Bill Length </th>"
                                + "<th align='center'> Bill Width </th>"
                                + "<th align='center'> Bill GSM </th>"
                                + "<th align='center'> Bill Weight </th>"                                
                                + "<th align='center'> Bill Sqmtr </th>"
                                + "<th align='center'> Bill Product Code </th>"                                
                                + "<th align='center'> Tag Remark </th>"                           
                                + "<th align='center'> MFG Status </th>"                           
                                + "<th align='center'> Incharge Name </th>"                           
                                + "<th align='center'> Net Price/Unit </th>"                           
                                + "<th align='center'> Net Seam/Unit </th>"                           
                                + "<th align='center'> Notional P/L </th>"                           
                                + "</tr>";
                        
                        rsData1.first();
                       
                        if (rsData1.getRow() > 0) {
                            while (!rsData1.isAfterLast()){ 
                               //ResultSet rsTmp1 = data.getConn().createStatement().executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + rsData1.getString("PROD_PIECE_NO") + "'");
                               //rsTmp1.first();
                                
                                pMessage = pMessage + ""
                                    + "<tr>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_PIECE_NO")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_PARTY_CODE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_PARTY_NAME")+" </td>"
                                    + "<td align='center'> "+EITLERPGLOBAL.formatDate(rsData1.getString("FELT_AMEND_ORDER_DATE"))+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_PRODUCT_CODE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_GROUP")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_STYLE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_LENGTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_WIDTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_GSM")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_WEIGHT")+" </td>"    
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_ACTUAL_WEIGHT")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_SQMTR")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_BILL_LENGTH")+" </td>"    
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_BILL_WIDTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_BILL_GSM")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_BILL_WEIGHT")+" </td>"        
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_BILL_SQMTR")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_BILL_PRODUCT_CODE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_REMARK")+" </td>" 
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_MFG_STATUS")+" </td>" 
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_INCHARGE_NAME")+" </td>" 
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_UNIT_NET_PRICE")+" </td>" 
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_UNIT_NET_SEAM")+" </td>" 
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_NOTIONAL_PL_AMT")+" </td>" 
                                    + "</tr>";
                                rsData1.next();
                            }
                        }
                        pMessage = pMessage + "</table>";

                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }
                // STOCK Tagging END
            }
            //End Felt Stock Tagging
            
            
//GAURANG            
            if (MODULE_ID == 608) {
//                pMessage = "Closure Doc No : " + DOC_NO + ",<br>Closure Doc Date : " + DOC_DATE + ",<br><br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br><br><br>";
//                pMessage = "Closure Doc No : " + DOC_NO + ",<br>Closure Doc Date : " + DOC_DATE + "<br><br><br>";
                
//                pMessage = "<html><body>Closure Doc No : " + DOC_NO + ",<br>Closure Doc Date : " + DOC_DATE + "<br><br><br>";
                pMessage = "Closure Doc No : " + DOC_NO + ",<br>Closure Doc Date : " + DOC_DATE + "<br><br><br>";

                //<br>Party Code : "+PARTY_CODE+",<br>Party Name : "+clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, PARTY_CODE)+"
                try {
                    Connection Conn1;
                    Statement stmt1;
                    ResultSet rsData1;

                    Conn1 = data.getConn();
                    stmt1 = Conn1.createStatement();
                    rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_PARTY_MACHINE_POSITION_CLOSURE WHERE DOC_NO = '" + DOC_NO + "' ");
                    rsData1.first();

                    String partyCd = rsData1.getString("PARTY_CODE");
                    String partyName = rsData1.getString("PARTY_NAME");
                    String machineNo = rsData1.getString("MACHINE_NO");
                    String positionNo = rsData1.getString("POSITION_NO");
                    
//                    pMessage = pMessage + "<br>PARTY CODE : "+partyCd+"    NAME : "+partyName;
                    pMessage = pMessage + "PARTY CODE : "+partyCd+"    NAME : "+partyName+"<br>";
                    if (DOC_NO.startsWith("PMC")){
//                        pMessage = pMessage + "<br>MACHINE NO : "+machineNo;
                        pMessage = pMessage + "MACHINE NO : "+machineNo+"<br>";
                    }
                    if (DOC_NO.startsWith("MPC")){
//                        pMessage = pMessage + "<br>POSITION NO : "+positionNo;
                        pMessage = pMessage + "POSITION NO : "+positionNo+"<br>";
                    }
//                    pMessage = pMessage + "<br>HAS NOW BEEN CLOSED.";
//                    
//                    pMessage = pMessage + "<br>PIECES OF ABOVE MENTION ARE READY FOR DIVERSION.";
//                    
//                    pMessage = pMessage + "<br>LIST OF DIVERSION READY FELTS<br><br>";
                    
                    pMessage = pMessage + "HAS NOW BEEN CLOSED.<br>PIECES OF ABOVE MENTION ARE READY FOR DIVERSION.<br>LIST OF DIVERSION READY FELTS<br><br>";
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        String prSQL = "";
                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        //rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE = '" + partyCd + "' AND PR_PIECE_STAGE IN ('WEAVING','MENDING','NEEDLING','FINISHING','IN STOCK','BSR') ");
                        if (DOC_NO.startsWith("FPC")) {
                            prSQL = "SELECT PR_PIECE_STAGE,COALESCE(PR_PIECE_NO,'') AS PR_PIECE_NO,COALESCE(PR_MACHINE_NO,'') AS PR_MACHINE_NO,COALESCE(PR_POSITION_NO,'') AS PR_POSITION_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE = '" + partyCd + "' AND PR_PIECE_STAGE IN ('WEAVING','MENDING','NEEDLING','FINISHING','IN STOCK','BSR')  ORDER BY PR_PIECE_STAGE,PR_PIECE_NO ";
                        }
                        
                        if (DOC_NO.startsWith("PMC")) {
                            prSQL = "SELECT PR_PIECE_STAGE,COALESCE(PR_PIECE_NO,'') AS PR_PIECE_NO,COALESCE(PR_MACHINE_NO,'') AS PR_MACHINE_NO,COALESCE(PR_POSITION_NO,'') AS PR_POSITION_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE = '" + partyCd + "' AND PR_MACHINE_NO = " + machineNo + " AND PR_PIECE_STAGE IN ('WEAVING','MENDING','NEEDLING','FINISHING','IN STOCK','BSR')  ORDER BY PR_PIECE_STAGE,PR_PIECE_NO ";
                        }
                        
                        if (DOC_NO.startsWith("MPC")) {
                            prSQL = "SELECT PR_PIECE_STAGE,COALESCE(PR_PIECE_NO,'') AS PR_PIECE_NO,COALESCE(PR_MACHINE_NO,'') AS PR_MACHINE_NO,COALESCE(PR_POSITION_NO,'') AS PR_POSITION_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE = '" + partyCd + "' AND PR_MACHINE_NO = " + machineNo + " AND PR_POSITION_NO = " + positionNo + " AND PR_PIECE_STAGE IN ('WEAVING','MENDING','NEEDLING','FINISHING','IN STOCK','BSR')  ORDER BY PR_PIECE_STAGE,PR_PIECE_NO ";
                        }    
                        
                        System.out.println("prSQL : " + prSQL);

                        rsData = stmt.executeQuery(prSQL);
                        rsData.first();
                       
                        if (rsData.getRow() > 0) {
//                            pMessage = pMessage + "<br>-----------------------------------------------------------------------------";
//                            pMessage = pMessage + "<br>\t|  STAGE  |\tPIECE  |\tMACHINE  |\tPOSITION      |";
//                            pMessage = pMessage + "<br>-----------------------------------------------------------------------------";
//                            pMessage = pMessage + "<table border='1'><tr><th>STAGE</th><th>PIECE NO</th><th align='center'> MACHINE NO </th><th align='center'> POSITION </th></tr>";
                            pMessage = pMessage + "<table border='1'>"
                                + "<tr>"
                                + "<th align='center'> STAGE </th>"
                                + "<th align='center'> PIECE NO </th>"
                                + "<th align='center'> MACHINE NO </th>"
                                + "<th align='center'> POSITION </th>"
                                + "</tr>";
                            
                            
                            while (!rsData.isAfterLast()) {
                                pMessage = pMessage + ""
                                    + "<tr>"
                                    + "<td align='center'> "+rsData.getString("PR_PIECE_STAGE")+" </td>"
                                    + "<td align='center'> "+rsData.getString("PR_PIECE_NO")+" </td>"
                                    + "<td align='center'> "+rsData.getString("PR_MACHINE_NO")+" </td>"
                                    + "<td align='center'> "+rsData.getString("PR_POSITION_NO")+" </td>"
                                    + "</tr>";
//                                pMessage = pMessage + "<tr>"
//                                        + "<td>"
//                                        + "<p>" + rsData.getString("PR_PIECE_STAGE") + "</p>"
//                                        + "</td>"
//                                        + "<td>"
//                                        + "<p>" + rsData.getString("PR_PIECE_NO") + "</p>"
//                                        + "</td>"
//                                        + "<td>"
//                                        + "<p>" + rsData.getString("PR_MACHINE_NO") + "</p>"
//                                        + "</td>"
//                                        + "<td>"
//                                        + "<p>" + rsData.getString("PR_POSITION_NO") + "</p>"
//                                        + "</td>"
//                                        + "</tr>";

//                                pMessage = pMessage + "<br>\t|   " + rsData.getString("PR_PIECE_STAGE") + "  |\t" + rsData.getString("PR_PIECE_NO") + "  |\t" + rsData.getString("PR_MACHINE_NO") + "  |\t" + rsData.getString("PR_POSITION_NO") + "\t|";
//                                pMessage = pMessage + "<br>-----------------------------------------------------------------------------";

                                rsData.next();
                            }
                               pMessage = pMessage + "</table>";           
                        } else {
                            pMessage = pMessage + "<br>No Pieces found in Piece Register.<br>";
                        }
                        
//                        pMessage = pMessage + "</table></body></html>";
     
                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }

                } catch (Exception e) {
                    System.out.println("Error on Mail: " + e.getMessage());
                }

            }

//GAURANG            
        }

        if (SEND_TO_ALL) {
            HashMap hmSendToList;

            String recievers = "sdmlerp@dineshmills.com";
                
            if (MODULE_ID == 608) {
                pMessage = pMessage + "<br><br><br> : Email Send to : <br><br>";
                hmSendToList = clsHierarchy.getUserList(EITLERPGLOBAL.gCompanyID, HIERARCHY_ID, USER_ID, true);
                for (int i = 1; i <= hmSendToList.size(); i++) {
                    clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));
                    int U_ID = ObjUser.getAttribute("USER_ID").getInt();

                    String to = clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, U_ID);

                    System.out.println("USERID : " + U_ID + ", send_to : " + to);
                    if (!to.equals("")) {
                        recievers = recievers + "," + to;
                        pMessage = pMessage + ObjUser.getAttribute("USER_NAME").getString()+ "<br>" ;
                    }
                }
                
//                recievers = "sdmlerp@dineshmills.com,rishineekhra@dineshmills.com,gaurang@dineshmills.com,ashutosh@dineshmills.com";
                recievers = recievers + "yrpatel@dineshmills.com,amitkanti@dineshmills.com,feltpp@dineshmills.com,brdflteng@dineshmills.com,abtewary@dineshmills.com,felts@dineshmills.com,brdfltfin@dineshmills.com,feltwh@dineshmills.com";
                
//                pMessage = pMessage + "rishineekhra@dineshmills.com<br>";
//                pMessage = pMessage + "gaurang@dineshmills.com<br>";
//                pMessage = pMessage + "ashutosh@dineshmills.com<br>"; 
                pMessage = pMessage + "yrpatel@dineshmills.com,amitkanti@dineshmills.com<br>";
                pMessage = pMessage + "feltpp@dineshmills.com<br>";
                pMessage = pMessage + "brdflteng@dineshmills.com<br>"; 
                pMessage = pMessage + "abtewary@dineshmills.com<br>";
                pMessage = pMessage + "felts@dineshmills.com<br>";
                pMessage = pMessage + "brdfltfin@dineshmills.com<br>"; 
                pMessage = pMessage + "feltwh@dineshmills.com<br>"; 
                
                pMessage = pMessage + "<br><br><br>**** This is an auto-generated email, please do not reply ****<br><br>";
//                pMessage = pMessage + "</table></body></html>";
                
            } else {
                pMessage = pMessage + "<br><br><br> : Email Send to : <br>";
                hmSendToList = clsHierarchy.getUserList(EITLERPGLOBAL.gCompanyID, HIERARCHY_ID, USER_ID, true);
                for (int i = 1; i <= hmSendToList.size(); i++) {
                    clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));
                    int U_ID = ObjUser.getAttribute("USER_ID").getInt();

                    String to = clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, U_ID);

                    System.out.println("USERID : " + U_ID + ", send_to : " + to);
                    if (!to.equals("")) {
                        recievers = recievers + "," + to;
                        pMessage = pMessage + "<br>" + ObjUser.getAttribute("USER_NAME").getString();
                    }
                }
            }
            
            if (MODULE_ID == 80 || MODULE_ID == 715) {
                //recievers = recievers + "," + "feltsalesnotification@dineshmills.com";
                pMessage = pMessage + "<br>" + "feltsalesnotification@dineshmills.com";
            }
            
            if (MODULE_ID == 604) {
                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 57))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 57);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 57);
                }
                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 59))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 59);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 59);
                }
                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 285))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 285);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 285);
                }
                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 320))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 320);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 320);
                }
                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 19))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 19);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 19);
                }

                recievers = recievers + ",excise@dineshmills.com";
                pMessage = pMessage + "<br>excise@dineshmills.com";
            }
            if (MODULE_ID == 712) {
                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 60))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 60);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 60);
                }

                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 306))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 306);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 306);
                }

                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 57))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 57);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 57);
                }

                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 56))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 56);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 56);
                }

                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 53))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 53);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 53);
                }
            }
            if (MODULE_ID == 763 || MODULE_ID == 774) {
                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 243))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 243);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 243);
                }
                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 28))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 28);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 28);
                }
            }
                
            if (MODULE_ID!=608) {
                pMessage = pMessage + "<br><br><br><br>**** This is an auto-generated email, please do not reply ****";
            }
            
            try {

                System.out.println("Recivers : " + recievers);
                System.out.println("pSubject : " + pSubject);
                System.out.println("pMessage : " + pMessage);

                SendMail(recievers, pMessage, pSubject, cc);
            } catch (Exception e) {
                System.out.println("Error Msg " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            String to = clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, USER_ID);

            System.out.println("SINGLE USER ID : " + USER_ID + ", send_to : " + to);
            if (!to.equals("")) {
                to = to + ",sdmlerp@dineshmills.com";
                try {

                    SendMail(to, pMessage, pSubject, cc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("USER : " + USER_ID + " External Mail Not Found");
                try {
                    SendMail("sdmlerp@dineshmills.com", "Mail Sending Failed due to External Mail not found, USER ID  " + USER_ID, "Mail Not Sent : " + DOC_NO, "");
                } catch (Exception e) {
                        //System.out.println("Error Msg "+e.getMessage());
                    //e.printStackTrace();
                }
            }
        }

        return "Mail Sending Done....!";
    }

    
    
    
    public static String sendPendingMail() {

        String pMessage = "";
        String pSubject = "Pending Documents";
        String cc = "";

        String SQL = "SELECT distinct U.USER_ID FROM PRODUCTION.FELT_PROD_DOC_DATA D,DINESHMILLS.D_COM_MODULES M,DINESHMILLS.D_COM_USER_MASTER U WHERE STATUS ='W' AND D.MODULE_ID = M.MODULE_ID AND  U.USER_ID = D.USER_ID AND DOC_DATE >= '2017-10-01'";

        try {
            Connection tmpConn;
            tmpConn = data.getCreatedConn();

            ResultSet rsTmp;
            Statement tmpStmt;
            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery(SQL);

            rsTmp.first();
            while (!rsTmp.isAfterLast()) {

                int USER_ID = rsTmp.getInt("USER_ID");

                String SQL1 = "SELECT MODULE_DESC,DOC_NO,DOC_DATE,D.REMARKS FROM PRODUCTION.FELT_PROD_DOC_DATA D,DINESHMILLS.D_COM_MODULES M,DINESHMILLS.D_COM_USER_MASTER U WHERE STATUS ='W' AND D.MODULE_ID = M.MODULE_ID AND  U.USER_ID = D.USER_ID AND DOC_DATE >= '2017-10-01' AND U.USER_ID = " + USER_ID;
                Connection tmpConn1;
                tmpConn1 = data.getCreatedConn();

                ResultSet rsTmp1;
                Statement tmpStmt1;
                tmpStmt1 = tmpConn1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                rsTmp1 = tmpStmt1.executeQuery(SQL1);
                System.out.println("SQL1 : " + SQL1);
                rsTmp1.first();
                while (!rsTmp1.isAfterLast()) {

                    String DOC_TYPE = rsTmp1.getString("MODULE_DESC");
                    String DOC_NO = rsTmp1.getString("DOC_NO");
                    String DOC_DATE = rsTmp1.getString("DOC_DATE");
                    String REMARKS = rsTmp1.getString("REMARKS");

                    pMessage = pMessage + "<br><br>" + DOC_TYPE + ", DOC NO : " + DOC_NO + ", DOC DATE : " + DOC_DATE + ",<br>REMARKS : " + REMARKS;
                    rsTmp1.next();
                }

                String to = clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, USER_ID);

                System.out.println(" USER ID : " + USER_ID + ", send_to : " + to);
                if (!to.equals("")) {
                    try {
                        //SendMail(to, pMessage, pSubject, cc);
                        System.out.println("TO : " + to);
                        System.out.println("pMessage : " + pMessage);
                        System.out.println("pSubject : " + pSubject);
                        System.out.println("cc : " + cc);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                rsTmp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Mail Sending Done....!";
    }
    
    /*
    public static String sendFinalApprovalMail_RESEND(int MODULE_ID, String DOC_NO, String DOC_DATE, String PARTY_CODE, int USER_ID, int HIERARCHY_ID, boolean SEND_TO_ALL, int Resend_By) {
        if (USER_ID == 0) {
            try {
                SendMail("sdmlerp@dineshmills.com", "Mail Sending Failed due to USER ID not set", "Mail Not Sent : " + DOC_NO, "");
            } catch (Exception e) {
                //System.out.println("Error Msg "+e.getMessage());
                //e.printStackTrace();
            }
            return "User Id not set, please set User Id";
        }

        String responce = data.getStringValueFromDB("SELECT HIERARCHY_ID FROM DINESHMILLS.D_COM_HIERARCHY where HIERARCHY_ID=" + HIERARCHY_ID + " AND MODULE_ID=" + MODULE_ID + "");
        if (responce.equals("")) {

            try {
                SendMail("sdmlerp@dineshmills.com", "Mail Sending Failed due to HIERARCHY ID AND MODULE ID missed matched,<br><br><br> Module Id " + MODULE_ID + " and Hierarchy Id " + HIERARCHY_ID + " missed matched", "Mail Not Sent : " + DOC_NO, "");
            } catch (Exception e) {
                //System.out.println("Error Msg "+e.getMessage());
                //e.printStackTrace();
            }
            return "Module Id " + MODULE_ID + " and Hierarchy Id " + HIERARCHY_ID + " missed matched";
        }

        if (HIERARCHY_ID == 0) {
            try {
                SendMail("sdmlerp@dineshmills.com", "Mail Sending Failed due to HIERARCHY ID not set ", "Mail Not Sent : " + DOC_NO, "");
            } catch (Exception e) {
                //System.out.println("Error Msg "+e.getMessage());
                //e.printStackTrace();
            }
            return "Hierarchy Id not set, Please set Hierarchy Id";
            //send copy to SDMLERP 

        }

        if (MODULE_ID == 0) {
            try {
                SendMail("sdmlerp@dineshmills.com", "Mail Sending Failed due to MODULE ID not set ", "Mail Not Sent : " + DOC_NO, "");
            } catch (Exception e) {
                //System.out.println("Error Msg "+e.getMessage());
                //e.printStackTrace();
            }
            return "Module not set, Please set Module Id";
        }

        String DOC_TYPE = clsModules.getModuleName(EITLERPGLOBAL.gCompanyID, MODULE_ID);

        String pMessage = "";
        String pSubject = "Final Approved, " + DOC_TYPE + " , DOC NO : " + DOC_NO;
        String cc = "";

        if (MODULE_ID == 80 || MODULE_ID == 715 || MODULE_ID == 741 || MODULE_ID == 724 || MODULE_ID == 725) {
            cc = "feltsalesnotification@dineshmills.com";
        }

        if (MODULE_ID == 622)
        {
            cc = "feltwh@dineshmills.com";
        }
        
        if (PARTY_CODE.equals("")) {
            pMessage = "Document Name : " + DOC_TYPE + ",<br>Document No : " + DOC_NO + ",<br>Document Date : " + DOC_DATE + ",<br><br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br> Document has been Resend by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, Resend_By) + "<br><br><br>";
        } else {
            pMessage = "Document Name : " + DOC_TYPE + ",<br>Document No : " + DOC_NO + ",<br>Document Date : " + DOC_DATE + ",<br>Party Code : " + PARTY_CODE + ",<br>Party Name : " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, PARTY_CODE) + "<br><br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br> Document has been Resend by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, Resend_By) + "<br><br><br>";

            if (MODULE_ID == 604) {
                pMessage = "Diversion No : " + DOC_NO + ",<br>Diversion Date : " + DOC_DATE + ",<br><br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br> Document has been Resend by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, Resend_By) + "<br><br><br>";

                //<br>Party Code : "+PARTY_CODE+",<br>Party Name : "+clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, PARTY_CODE)+"
                try {
                    Connection Conn1;
                    Statement stmt1;
                    ResultSet rsData1;

                    Conn1 = data.getConn();
                    stmt1 = Conn1.createStatement();
                    rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION where SD_ORDER_NO = '" + DOC_NO + "'");
                    rsData1.first();

                    String Existing_Piece_No = rsData1.getString("ORIGINAL_PIECE_NO");

                    pMessage = pMessage + "<br> :: <u>EXISTING PIECE DETAILS</u> ::<br><br>";
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO = '" + Existing_Piece_No + "'");
                        rsData.first();

                        pMessage = pMessage + "<br> PIECE NO : " + rsData.getString("PR_PIECE_NO");
                        pMessage = pMessage + "<br> PARTY CODE : " + rsData.getString("PR_PARTY_CODE") + " - " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData.getString("PR_PARTY_CODE"));
                        pMessage = pMessage + "<br> PRODUCT GROUP : " + rsData.getString("PR_GROUP");
                        //pMessage = pMessage + "<br> PRODUCT CODE : " + rsData.getString("PR_PRODUCT_CODE");
                        //pMessage = pMessage + "<br> ORDER LENGTH : "+rsData.getString("PR_LENGTH")+" ,     ORDER WIDTH :  "+rsData.getString("PR_WIDTH")+" ,    ORDER GSM : "+rsData.getString("PR_GSM");
                        //pMessage = pMessage + "<br> ACTUAL LENGTH : "+rsData.getString("PR_ACTUAL_LENGTH")+" ,     ACTUAL WIDTH : "+rsData.getString("PR_ACTUAL_WIDTH")+" ,     ACTUAL WEIGHT : "+rsData.getString("PR_ACTUAL_WEIGHT");
                        //pMessage = pMessage + "<br> BILL LENGTH : "+rsData.getString("PR_BILL_LENGTH")+" ,     BILL WIDTH : "+rsData.getString("PR_BILL_WIDTH")+" ,     BILL GSM : "+rsData.getString("PR_BILL_GSM")+" ,      BILL WEIGHT : "+rsData.getString("PR_BILL_WEIGHT");

                        pMessage = pMessage + "<br><br>";
                        pMessage = pMessage + "<table border='1'><tr><th></th><th>LENGTH</th><th align='center'> WIDTH </th><th align='center'> GSM </th><th align='center'>WEIGHT</th><th align='center'>PRODUCT CODE</th></tr>";
                        
                        pMessage = pMessage + "<tr>"
                                        + "<td align='center'>"
                                        + "ORDER"
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_LENGTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_WIDTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_GSM") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_THORITICAL_WEIGHT") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_PRODUCT_CODE") + ""
                                        + "</td>"
                                        + "</tr>";
                        
                        pMessage = pMessage + "<tr>"
                                        + "<td align='center'>"
                                        + "ACTUAL"
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_ACTUAL_LENGTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_ACTUAL_WIDTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_ACTUAL_WEIGHT") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "-"
                                        + "</td>"
                                        + "</tr>";
                        
                        pMessage = pMessage + "<tr>"
                                        + "<td align='center'>"
                                        + "BILL"
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_LENGTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_WIDTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_GSM") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_WEIGHT") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_PRODUCT_CODE") + ""
                                        + "</td>"
                                        + "</tr>"
                                        + "</table>";
                        
                        
                        
//                        pMessage = pMessage + "<br>-----------------------------------------------------------------------------";
//                        pMessage = pMessage + "<br>\t       |  LENGTH  |\tWIDTH  |\tGSM  |\tWEIGHT      |";
//                        pMessage = pMessage + "<br>-----------------------------------------------------------------------------";
//                        pMessage = pMessage + "<br>  ORDER\t|   " + rsData.getString("PR_LENGTH") + "  |\t" + rsData.getString("PR_WIDTH") + "  |\t" + rsData.getString("PR_GSM") + "  |\t" + rsData.getString("PR_THORITICAL_WEIGHT") + "\t|";
//                        pMessage = pMessage + "<br> ACTUAL\t|   " + rsData.getString("PR_ACTUAL_LENGTH") + "  |\t" + rsData.getString("PR_ACTUAL_WIDTH") + "  |\t      |\t" + rsData.getString("PR_ACTUAL_WEIGHT") + "\t|";
//                        pMessage = pMessage + "<br>   BILL\t|   " + rsData.getString("PR_BILL_LENGTH") + "  |\t" + rsData.getString("PR_BILL_WIDTH") + "  |\t" + rsData.getString("PR_BILL_GSM") + "  |\t" + rsData.getString("PR_BILL_WEIGHT") + "\t|";
//                        pMessage = pMessage + "<br>-----------------------------------------------------------------------------";

                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }

                    String New_Piece_No = rsData1.getString("D_PIECE_NO");
                    pMessage = pMessage + "<br><br><br>";
                    pMessage = pMessage + "<br> :: <u>ORDER(DIVERSION) PIECE DETAILS</u> ::";
                    pMessage = pMessage + "<br><br><br>";
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO = '" + New_Piece_No + "'");
                        rsData.first();

                        pMessage = pMessage + "<br> PIECE NO : " + rsData.getString("PR_PIECE_NO");
                        pMessage = pMessage + "<br> PARTY CODE : " + rsData.getString("PR_PARTY_CODE") + " - " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData.getString("PR_PARTY_CODE"));
                        pMessage = pMessage + "<br> PRODUCT GROUP : " + rsData.getString("PR_GROUP");
                        //pMessage = pMessage + "<br> PRODUCT CODE : " + rsData.getString("PR_PRODUCT_CODE");
                        //pMessage = pMessage + "<br> ORDER LENGTH : "+rsData.getString("PR_LENGTH")+" ,     ORDER WIDTH :  "+rsData.getString("PR_WIDTH")+" ,    ORDER GSM : "+rsData.getString("PR_GSM");
                        //pMessage = pMessage + "<br> ACTUAL LENGTH : "+rsData.getString("PR_ACTUAL_LENGTH")+" ,     ACTUAL WIDTH : "+rsData.getString("PR_ACTUAL_WIDTH")+" ,     ACTUAL WEIGHT : "+rsData.getString("PR_ACTUAL_WEIGHT");
                        //pMessage = pMessage + "<br> BILL LENGTH : "+rsData.getString("PR_BILL_LENGTH")+" ,     BILL WIDTH : "+rsData.getString("PR_BILL_WIDTH")+" ,     BILL GSM : "+rsData.getString("PR_BILL_GSM")+" ,      BILL WEIGHT : "+rsData.getString("PR_BILL_WEIGHT");

                        pMessage = pMessage + "<br><br>";
                        pMessage = pMessage + "<table border='1'><tr><th></th><th>LENGTH</th><th align='center'> WIDTH </th><th align='center'> GSM </th><th align='center'>WEIGHT</th><th align='center'>PRODUCT CODE</th></tr>";
                        
                        pMessage = pMessage + "<tr>"
                                        + "<td align='center'>"
                                        + "ORDER"
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_LENGTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_WIDTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_GSM") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_THORITICAL_WEIGHT") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_PRODUCT_CODE") + ""
                                        + "</td>"
                                        + "</tr>";
                        
                        pMessage = pMessage + "<tr>"
                                        + "<td align='center'>"
                                        + "ACTUAL"
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_ACTUAL_LENGTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_ACTUAL_WIDTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_ACTUAL_WEIGHT") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "-"
                                        + "</td>"
                                        + "</tr>";
                        
                        pMessage = pMessage + "<tr>"
                                        + "<td align='center'>"
                                        + "BILL"
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_LENGTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_WIDTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_GSM") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_WEIGHT") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_PRODUCT_CODE") + ""
                                        + "</td>"
                                        + "</tr>"
                                        + "</table>";
                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }

                    pMessage = pMessage + "<br><br><br>";
                    pMessage = pMessage + "<br> :: ACTION DETAILS ::";
                    pMessage = pMessage + "<br><br><br>";

                    boolean NOACTION = false;
                    if (("".equals(rsData1.getString("ACTION1")) || rsData1.getString("ACTION1") == null)
                            && ("".equals(rsData1.getString("ACTION2")) || rsData1.getString("ACTION2") == null)
                            && ("".equals(rsData1.getString("ACTION3")) || rsData1.getString("ACTION3") == null)
                            && ("".equals(rsData1.getString("ACTION4")) || rsData1.getString("ACTION4") == null)
                            && ("".equals(rsData1.getString("ACTION5")) || rsData1.getString("ACTION5") == null)
                            && ("".equals(rsData1.getString("ACTION6")) || rsData1.getString("ACTION6") == null)
                            && ("".equals(rsData1.getString("ACTION7")) || rsData1.getString("ACTION7") == null)
                            && ("".equals(rsData1.getString("ACTION8")) || rsData1.getString("ACTION8") == null)
                            && ("".equals(rsData1.getString("ACTION9")) || rsData1.getString("ACTION9") == null)
                            && ("".equals(rsData1.getString("ACTION10")) || rsData1.getString("ACTION10") == null)
                            && ("".equals(rsData1.getString("ACTION11")) || rsData1.getString("ACTION11") == null)
                            && ("".equals(rsData1.getString("ACTION12")) || rsData1.getString("ACTION12") == null)
                            && ("".equals(rsData1.getString("ACTION13")) || rsData1.getString("ACTION13") == null)
                            && ("".equals(rsData1.getString("ACTION14")) || rsData1.getString("ACTION14") == null)
                            && ("".equals(rsData1.getString("ACTION15")) || rsData1.getString("ACTION15") == null)
                            && ("".equals(rsData1.getString("ACTION16")) || rsData1.getString("ACTION16") == null)
                            && ("".equals(rsData1.getString("ACTION17")) || rsData1.getString("ACTION17") == null)) {
                        NOACTION = true;
                        pMessage = pMessage + "<u>NO ACTION</u><br><br>";
                    }

                    if (!NOACTION) {
                        if (!"".equals(rsData1.getString("ACTION1"))) {
                            pMessage = pMessage + "REDUCTION IN WIDTH : " + rsData1.getString("ACTION1") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION2"))) {
                            pMessage = pMessage + "INCREASE IN WIDTH : " + rsData1.getString("ACTION2") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION3"))) {
                            pMessage = pMessage + "REDUCTION IN LENGTH : " + rsData1.getString("ACTION3") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION4"))) {
                            pMessage = pMessage + "INCREASE IN GSM : " + rsData1.getString("ACTION4") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION5"))) {
                            pMessage = pMessage + "DECREASE IN GSM : " + rsData1.getString("ACTION5") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION6"))) {
                            pMessage = pMessage + "SEAMING REQUIRED : " + rsData1.getString("ACTION6") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION7"))) {
                            pMessage = pMessage + "TAGGING STYLE : " + rsData1.getString("ACTION7") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION8"))) {
                            pMessage = pMessage + "TAGGING PRODUCT CODE : " + rsData1.getString("ACTION8") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION9"))) {
                            pMessage = pMessage + "TAGGING LENGTH : " + rsData1.getString("ACTION9") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION10"))) {
                            pMessage = pMessage + "TAGGING WIDTH : " + rsData1.getString("ACTION10") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION11"))) {
                            pMessage = pMessage + "TAGGING GSM : " + rsData1.getString("ACTION11") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION12"))) {
                            pMessage = pMessage + "TAGGING CFM : " + rsData1.getString("ACTION12") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION13"))) {
                            pMessage = pMessage + "TAGGING THICKNESS : " + rsData1.getString("ACTION13") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION14"))) {
                            pMessage = pMessage + "TAGGING WEIGHT : " + rsData1.getString("ACTION14") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION15"))) {
                            pMessage = pMessage + "TRIM TO BE RETAINED BY PRODUCTION : " + rsData1.getString("ACTION15") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION16"))) {
                            pMessage = pMessage + "TRIM TO BE RETAINED BY WH WITH FELT : " + rsData1.getString("ACTION16") + "<br>";
                        }

                    }
                    //
                    pMessage = pMessage + "<br><br>DIVERSION REMARK : " + rsData1.getString("D_REMARK") + " <br><br>";
                } catch (Exception e) {
                    System.out.println("Error on Mail: " + e.getMessage());
                }

            }

            
            
            
            if(MODULE_ID == 602)
            {
                // ORDER ENTRY START 
                
                pMessage = "Felt Order No : " + DOC_NO + ",<br>Order Date : " + DOC_DATE + ",<br><br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br> Document has been Resend by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, Resend_By) + "<br><br><br>";

                //<br>Party Code : "+PARTY_CODE+",<br>Party Name : "+clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, PARTY_CODE)+"
                try {
                    Connection Conn1;
                    Statement stmt1;
                    ResultSet rsData1;

                    Conn1 = data.getConn();
                    stmt1 = Conn1.createStatement();
                    rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_HEADER H,PRODUCTION.FELT_SALES_ORDER_DETAIL D  " +
                                                " where H.S_ORDER_NO=D.S_ORDER_NO " +
                                                " AND H.S_ORDER_NO = '" + DOC_NO + "'");
                    rsData1.first();

                    pMessage = pMessage + "<br><br>";
                   
//                        Connection Conn;
//                        Statement stmt;
//                        ResultSet rsData;

//                        Conn = data.getConn();
//                        stmt = Conn.createStatement();
//                        rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO = '" + Existing_Piece_No + "'");
//                        rsData.first();

                        //pMessage = pMessage + "<br> PIECE NO : " + rsData.getString("PR_PIECE_NO");
                        pMessage = pMessage + "<br> PARTY CODE : " + rsData1.getString("PARTY_CODE") + " - " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData1.getString("PARTY_CODE"));
                        pMessage = pMessage + "<br> REFERENCE : " + rsData1.getString("REFERENCE");
                        pMessage = pMessage + "<br> REFERENCE DATE : " + rsData1.getString("REFERENCE_DATE");
                        pMessage = pMessage + "<br> REMARK : "+rsData1.getString("REMARK");
                        
                        pMessage = pMessage + "<br><br><u>ORDER DETAILS</u><br>";
                        pMessage = pMessage + "<table border='1'>"
                                + "<tr>"
                                + "<th align='center'> PIECE NO </th>"
                                + "<th align='center'> UPN </th>"
                                + "<th align='center'> MACHINE NO </th>"
                                + "<th align='center'> POSITION </th>"
                                + "<th align='center'> POSITION DESC </th>"
                                + "<th align='center'> PRODUCT CODE </th>"
                                + "<th align='center'> GROUP </th>"
                                + "<th align='center'> LENGTH </th>"
                                + "<th align='center'> WIDTH </th>"
                                + "<th align='center'> GSM </th>"
                                + "<th align='center'> WEIGHT </th>"
                                + "<th align='center'> SQMTR </th>"
                                + "<th align='center'> STYLE </th>"
                                + "<th align='center'> REQ MONTH </th>"
                                + "</tr>";
                        
                        rsData1.first();
                       
                        if (rsData1.getRow() > 0) {
                            while (!rsData1.isAfterLast()){ 
                                pMessage = pMessage + ""
                                    + "<tr>"
                                    + "<td align='center'> "+rsData1.getString("PIECE_NO")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("UPN")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MACHINE_NO")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("POSITION")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("POSITION_DESC")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("PRODUCT_CODE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("S_GROUP")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("LENGTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("WIDTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("GSM")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("THORITICAL_WIDTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("SQ_MTR")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("STYLE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("REQ_MONTH")+" </td>"
                                    + "</tr>";
                                rsData1.next();
                            }
                        }
                        pMessage = pMessage + "</table>";

                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }
                // ORDER ENTRY END
            }
            
           if(MODULE_ID == 774)
            {
                // WIP Review START 
                
                pMessage = "WIP Piece Review No : " + DOC_NO + ",<br>Review Entry Date : " + DOC_DATE + ",<br><br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br> Document has been Resend by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, Resend_By) + "<br><br><br>";

                try {
                    Connection Conn1;
                    Statement stmt1;
                    ResultSet rsData1;

                    Conn1 = data.getConn();
                    stmt1 = Conn1.createStatement();
                    rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_AMENDMENT_HEADER_WIP H, " +
                                                "PRODUCTION.FELT_SALES_PIECE_AMENDMENT_DETAIL_WIP D " +
                                                "WHERE H.PIECE_AMEND_NO=D.PIECE_AMEND_NO AND H.PIECE_AMEND_NO='" + DOC_NO + "'");
                    rsData1.first();

                    pMessage = pMessage + "<br><br>";
                   
//                        Connection Conn;
//                        Statement stmt;
//                        ResultSet rsData;

//                        Conn = data.getConn();
//                        stmt = Conn.createStatement();
//                        rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO = '" + Existing_Piece_No + "'");
//                        rsData.first();

                        //pMessage = pMessage + "<br> PIECE NO : " + rsData.getString("PR_PIECE_NO");
                        pMessage = pMessage + "<br> PARTY CODE : " + rsData1.getString("MM_PARTY_CODE") + " - " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData1.getString("MM_PARTY_CODE"));
                        pMessage = pMessage + "<br> MACHINE NO : " + rsData1.getString("MM_MACHINE_NO");
                        pMessage = pMessage + "<br> POSITION NO : " + rsData1.getString("MM_MACHINE_POSITION");
                        pMessage = pMessage + "<br> POSITION DESCRIPTION : " + data.getStringValueFromDB("SELECT POSITION_DESC FROM PRODUCTION.FELT_MACHINE_POSITION_MST WHERE POSITION_NO='" + rsData1.getString("MM_MACHINE_POSITION") + "'");
                        String Position_Des_No = data.getStringValueFromDB("SELECT POSITION_DESIGN_NO FROM PRODUCTION.FELT_MACHINE_POSITION_MST where POSITION_NO="+rsData1.getString("MM_MACHINE_POSITION"));
                        pMessage = pMessage + "<br> POSITION DESIGN NO : "+Position_Des_No;
                        pMessage = pMessage + "<br> UPN : "+rsData1.getString("MM_PARTY_CODE")+""+rsData1.getString("MM_MACHINE_NO")+""+Position_Des_No;
                        
                        
                        pMessage = pMessage + "<br><br><u>PIECE DETAILS</u><br>";
                        pMessage = pMessage + "<table border='1'>"
                                + "<tr>"
                                + "<th align='center'> PIECE NO </th>"
                                + "<th align='center'> CHANGE POSIBLE</th>"
                                + "<th align='center'> OBSOLETE</th>"
                                + "<th align='center'> APPLY CHANGE </th>"
                                + "<th align='center'> PROD. REMARK </th>"
                                + "<th align='center'> STYLE </th>"
                                + "<th align='center'> UPDATE STYLE </th>"
                                + "<th align='center'> LENGTH </th>"
                                + "<th align='center'> UPDATED LENGTH </th>"
                                + "<th align='center'> WIDTH </th>"
                                + "<th align='center'> UPDATED WIDTH </th>"
                                + "<th align='center'> GSM </th>"
                                + "<th align='center'> UPDATED GSM </th>"
                                + "<th align='center'> PROD. CODE </th>"
                                + "<th align='center'> UPDATED PROD. CODE </th>"
                                + "<th align='center'> PIECE STAGE </th>"
                                + "<th align='center'> REMARKS </th>"
                                + "<th align='center'> EXPECTED DISPACH </th>"
                                + "</tr>";
                        
                        rsData1.first();
                       
                        if (rsData1.getRow() > 0) {
                            while (!rsData1.isAfterLast()){ 
                                String Change_Posibility = "";
                                String Obsolete = "";
                                String Apply_Chage = "";
                                if("1".equals(rsData1.getString("CHANGE_POSIBILITY")))
                                {
                                    Change_Posibility = "YES";
                                }
                                if("1".equals(rsData1.getString("DELINK")))
                                {
                                    Obsolete = "YES";
                                }
                                if("1".equals(rsData1.getString("ACTUAL_CHANGE")))
                                {
                                    Apply_Chage = "YES";
                                }
                                pMessage = pMessage + ""
                                    + "<tr>"
                                    + "<td align='center'> "+rsData1.getString("PIECE_NO")+" </td>"
                                    + "<td align='center'> "+Change_Posibility+" </td>"
                                    + "<td align='center'> "+Obsolete+" </td>"
                                    + "<td align='center'> "+Apply_Chage+" </td>"
                                    + "<td align='center'> "+rsData1.getString("PROD_REMARKS")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("STYLE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("STYLE_UPDATED")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("LENGTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("LENGTH_UPDATED")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("WIDTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("WIDTH_UPDATED")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("GSM")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("GSM_UPDATED")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("PRODUCTCODE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("PRODUCTCODE_UPDATED")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("PIECE_STAGE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("REMARKS")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("EXPECTED_DISPATCH")+" </td>"
                                    + "</tr>";
                                rsData1.next();
                            }
                        }
                        pMessage = pMessage + "</table>";

                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }
                // WIP Review END
            }
            
            if(MODULE_ID == 763)
            {
                // WIP Review START 
                
                pMessage = "STOCK Piece Review No : " + DOC_NO + ",<br>Review Entry Date : " + DOC_DATE + ",<br><br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br> Document has been Resend by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, Resend_By) + "<br><br><br>";

                try {
                    Connection Conn1;
                    Statement stmt1;
                    ResultSet rsData1;

                    Conn1 = data.getConn();
                    stmt1 = Conn1.createStatement();
                    rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_STOCK_HEADER H, " +
                                                "PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_STOCK_DETAIL D " +
                                                "WHERE H.PIECE_AMEND_STOCK_NO=D.PIECE_AMEND_STOCK_NO AND H.PIECE_AMEND_STOCK_NO='" + DOC_NO + "'");
                    rsData1.first();

                    pMessage = pMessage + "<br><br>";
                   
//                        Connection Conn;
//                        Statement stmt;
//                        ResultSet rsData;

//                        Conn = data.getConn();
//                        stmt = Conn.createStatement();
//                        rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO = '" + Existing_Piece_No + "'");
//                        rsData.first();

                        //pMessage = pMessage + "<br> PIECE NO : " + rsData.getString("PR_PIECE_NO");
                        pMessage = pMessage + "<br> PARTY CODE : " + rsData1.getString("MM_PARTY_CODE") + " - " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData1.getString("MM_PARTY_CODE"));
                        pMessage = pMessage + "<br> MACHINE NO : " + rsData1.getString("MM_MACHINE_NO");
                        pMessage = pMessage + "<br> POSITION NO : " + rsData1.getString("MM_MACHINE_POSITION");
                        pMessage = pMessage + "<br> POSITION DESCRIPTION : " + data.getStringValueFromDB("SELECT POSITION_DESC FROM PRODUCTION.FELT_MACHINE_POSITION_MST WHERE POSITION_NO='" + rsData1.getString("MM_MACHINE_POSITION") + "'");
                        String Position_Des_No = data.getStringValueFromDB("SELECT POSITION_DESIGN_NO FROM PRODUCTION.FELT_MACHINE_POSITION_MST where POSITION_NO="+rsData1.getString("MM_MACHINE_POSITION"));
                        pMessage = pMessage + "<br> POSITION DESIGN NO : "+Position_Des_No;
                        pMessage = pMessage + "<br> UPN : "+rsData1.getString("MM_PARTY_CODE")+""+rsData1.getString("MM_MACHINE_NO")+""+Position_Des_No;
                        
                        pMessage = pMessage + "<br><br><u>PIECE DETAILS</u><br>";
                        pMessage = pMessage + "<table border='1'>"
                                + "<tr>"
                                + "<th align='center'> PIECE NO </th>"
                                + "<th align='center'> OBSOLETE</th>"
                                + "<th align='center'> STYLE </th>"
                                + "<th align='center'> UPDATE STYLE </th>"
                                + "<th align='center'> LENGTH </th>"
                                + "<th align='center'> UPDATED LENGTH </th>"
                                + "<th align='center'> WIDTH </th>"
                                + "<th align='center'> UPDATED WIDTH </th>"
                                + "<th align='center'> GSM </th>"
                                + "<th align='center'> UPDATED GSM </th>"
                                + "<th align='center'> PIECE STAGE </th>"
                                + "<th align='center'> EXPECTED DISPACH </th>"
                                + "</tr>";
                        
                        rsData1.first();
                       
                        if (rsData1.getRow() > 0) {
                            while (!rsData1.isAfterLast()){ 
                               
                                String Obsolete = "";
                                
                                
                                if("1".equals(rsData1.getString("SELECTED")))
                                {
                                    Obsolete = "YES";
                                }
                                
                                pMessage = pMessage + ""
                                    + "<tr>"
                                    + "<td align='center'> "+rsData1.getString("PIECE_NO")+" </td>"
                                    + "<td align='center'> "+Obsolete+" </td>"
                                    + "<td align='center'> "+rsData1.getString("STYLE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("STYLE_UPDATED")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("LENGTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("LENGTH_UPDATED")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("WIDTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("WIDTH_UPDATED")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("GSM")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("GSM_UPDATED")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("PIECE_STAGE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("EXPECTED_DISPATCH")+" </td>"
                                    + "</tr>";
                                rsData1.next();
                            }
                        }
                        pMessage = pMessage + "</table>";

                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }
                // WIP Review END
            }
            
            
            //Felt Stock Tagging
            if(MODULE_ID == 622)
            {
                // Felt Stock Tagging
                
                pMessage = "Felt Stock Tagging Doc No : " + DOC_NO + ",<br> Doc Date : " + DOC_DATE + ",<br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br>";

                try {
                    Connection Conn1;
                    Statement stmt1;
                    ResultSet rsData1;

                    Conn1 = data.getConn();
                    stmt1 = Conn1.createStatement();
                    rsData1 = stmt1.executeQuery("SELECT * FROM  PRODUCTION.FELT_PIECE_AMEND where FELT_AMEND_ID='" + DOC_NO + "'");
                    rsData1.first();

                    pMessage = pMessage + "<br><br>";
                   
                        pMessage = pMessage + "<br> STOCK TAGGING DATE : "+EITLERPGLOBAL.formatDate(rsData1.getString("APPROVED_DATE"));
                        pMessage = pMessage + "<br> Tagging Reason : "+data.getStringValueFromDB("SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID = 'PIECE_AMEND' AND PARA_CODE="+rsData1.getString("FELT_AMEND_REASON"));
                        pMessage = pMessage + "<br><br><u>TAGGING PIECE DETAILS</u><br>";
                        pMessage = pMessage + "<table border='1'>"
                                + "<tr>"
                                + "<th align='center'> PIECE NO </th>"
                                + "<th align='center'> PARTY CODE</th>"
                                + "<th align='center'> PARTY NAME </th>"
                                + "<th align='center'> PRODUCT CODE </th>"
                                + "<th align='center'> AMEND LENGTH </th>"
                                + "<th align='center'> AMEND WIDTH </th>"
                                + "<th align='center'> AMEND SQMTR </th>"
                                + "<th align='center'> AMEND GSM </th>"
                                + "<th align='center'> AMEND WEIGHT </th>"                                
                                + "<th align='center'> AMEND GROUP NAME </th>"
                                + "<th align='center'> AMEND STYLE CODE </th>"                                
                                + "<th align='center'> AMEND REMARK </th>"                                
                                + "<th align='center'> AMEND BILL LENGTH </th>"
                                + "<th align='center'> AMEND BILL WIDTH </th>"
                                + "<th align='center'> AMEND BILL SQMTR </th>"
                                + "<th align='center'> AMEND BILL GSM </th>"
                                + "<th align='center'> AMEND BILL WEIGHT </th>"                                
                                + "<th align='center'> AMEND BILL PRODUCT CODE </th>"
                                + "<th align='center'> AMEND INCHARGE NAME </th>"                                
                                + "<th align='center'> MFG STATUS </th>"                           
                                + "</tr>";
                        
                        rsData1.first();
                       
                        if (rsData1.getRow() > 0) {
                            while (!rsData1.isAfterLast()){ 
                               //ResultSet rsTmp1 = data.getConn().createStatement().executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + rsData1.getString("PROD_PIECE_NO") + "'");
                               //rsTmp1.first();
                                
                                pMessage = pMessage + ""
                                    + "<tr>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_PIECE_NO")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_PARTY_CODE")+" </td>"
                                    + "<td align='center'> "+clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData1.getString("FELT_AMEND_PARTY_CODE"))+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_PRODUCT_CODE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_LENGTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_WIDTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_SQMTR")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_GSM")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_WEIGHT")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_GROUP")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_STYLE")+" </td>"    
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_REMARK")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_BILL_LENGTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_BILL_WIDTH")+" </td>"    
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_BILL_SQMTR")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_BILL_GSM")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_BILL_WEIGHT")+" </td>"        
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_BILL_PRODUCT_CODE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_INCHARGE_NAME")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_MFG_STATUS")+" </td>" 
                                        
                                    + "</tr>";
                                rsData1.next();
                            }
                        }
                        pMessage = pMessage + "</table>";

                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }
                // STOCK Tagging END
            }
            //End Felt Stock Tagging
            
//GAURANG            
            if (MODULE_ID == 608) {
//                pMessage = "Closure Doc No : " + DOC_NO + ",<br>Closure Doc Date : " + DOC_DATE + ",<br><br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br><br><br>";
//                pMessage = "Closure Doc No : " + DOC_NO + ",<br>Closure Doc Date : " + DOC_DATE + "<br><br><br>";
                
//                pMessage = "<html><body>Closure Doc No : " + DOC_NO + ",<br>Closure Doc Date : " + DOC_DATE + "<br><br><br>";
                pMessage = "Closure Doc No : " + DOC_NO + ",<br>Closure Doc Date : " + DOC_DATE + "<br><br><br>";

                //<br>Party Code : "+PARTY_CODE+",<br>Party Name : "+clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, PARTY_CODE)+"
                try {
                    Connection Conn1;
                    Statement stmt1;
                    ResultSet rsData1;

                    Conn1 = data.getConn();
                    stmt1 = Conn1.createStatement();
                    rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_PARTY_MACHINE_POSITION_CLOSURE WHERE DOC_NO = '" + DOC_NO + "' ");
                    rsData1.first();

                    String partyCd = rsData1.getString("PARTY_CODE");
                    String partyName = rsData1.getString("PARTY_NAME");
                    String machineNo = rsData1.getString("MACHINE_NO");
                    String positionNo = rsData1.getString("POSITION_NO");
                    
//                    pMessage = pMessage + "<br>PARTY CODE : "+partyCd+"    NAME : "+partyName;
                    pMessage = pMessage + "PARTY CODE : "+partyCd+"    NAME : "+partyName+"<br>";
                    if (DOC_NO.startsWith("PMC")){
//                        pMessage = pMessage + "<br>MACHINE NO : "+machineNo;
                        pMessage = pMessage + "MACHINE NO : "+machineNo+"<br>";
                    }
                    if (DOC_NO.startsWith("MPC")){
//                        pMessage = pMessage + "<br>POSITION NO : "+positionNo;
                        pMessage = pMessage + "POSITION NO : "+positionNo+"<br>";
                    }
//                    pMessage = pMessage + "<br>HAS NOW BEEN CLOSED.";
//                    
//                    pMessage = pMessage + "<br>PIECES OF ABOVE MENTION ARE READY FOR DIVERSION.";
//                    
//                    pMessage = pMessage + "<br>LIST OF DIVERSION READY FELTS<br><br>";
                    
                    pMessage = pMessage + "HAS NOW BEEN CLOSED.<br>PIECES OF ABOVE MENTION ARE READY FOR DIVERSION.<br>LIST OF DIVERSION READY FELTS<br><br>";
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        String prSQL = "";
                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        //rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE = '" + partyCd + "' AND PR_PIECE_STAGE IN ('WEAVING','MENDING','NEEDLING','FINISHING','IN STOCK','BSR') ");
                        if (DOC_NO.startsWith("FPC")) {
                            prSQL = "SELECT PR_PIECE_STAGE,COALESCE(PR_PIECE_NO,'') AS PR_PIECE_NO,COALESCE(PR_MACHINE_NO,'') AS PR_MACHINE_NO,COALESCE(PR_POSITION_NO,'') AS PR_POSITION_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE = '" + partyCd + "' AND PR_PIECE_STAGE IN ('WEAVING','MENDING','NEEDLING','FINISHING','IN STOCK','BSR')  ORDER BY PR_PIECE_STAGE,PR_PIECE_NO ";
                        }
                        
                        if (DOC_NO.startsWith("PMC")) {
                            prSQL = "SELECT PR_PIECE_STAGE,COALESCE(PR_PIECE_NO,'') AS PR_PIECE_NO,COALESCE(PR_MACHINE_NO,'') AS PR_MACHINE_NO,COALESCE(PR_POSITION_NO,'') AS PR_POSITION_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE = '" + partyCd + "' AND PR_MACHINE_NO = " + machineNo + " AND PR_PIECE_STAGE IN ('WEAVING','MENDING','NEEDLING','FINISHING','IN STOCK','BSR')  ORDER BY PR_PIECE_STAGE,PR_PIECE_NO ";
                        }
                        
                        if (DOC_NO.startsWith("MPC")) {
                            prSQL = "SELECT PR_PIECE_STAGE,COALESCE(PR_PIECE_NO,'') AS PR_PIECE_NO,COALESCE(PR_MACHINE_NO,'') AS PR_MACHINE_NO,COALESCE(PR_POSITION_NO,'') AS PR_POSITION_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE = '" + partyCd + "' AND PR_MACHINE_NO = " + machineNo + " AND PR_POSITION_NO = " + positionNo + " AND PR_PIECE_STAGE IN ('WEAVING','MENDING','NEEDLING','FINISHING','IN STOCK','BSR')  ORDER BY PR_PIECE_STAGE,PR_PIECE_NO ";
                        }    
                        
                        System.out.println("prSQL : " + prSQL);

                        rsData = stmt.executeQuery(prSQL);
                        rsData.first();
                       
                        if (rsData.getRow() > 0) {
//                            pMessage = pMessage + "<br>-----------------------------------------------------------------------------";
//                            pMessage = pMessage + "<br>\t|  STAGE  |\tPIECE  |\tMACHINE  |\tPOSITION      |";
//                            pMessage = pMessage + "<br>-----------------------------------------------------------------------------";
                            pMessage = pMessage + "<table border='1'><tr><th>STAGE</th><th>PIECE NO</th><th align='center'> MACHINE NO </th><th align='center'> POSITION </th></tr>";
                    
                                                    while (!rsData.isAfterLast()) {
                                pMessage = pMessage + "<tr>"
                                        + "<td>"
                                        + "<p>" + rsData.getString("PR_PIECE_STAGE") + "</p>"
                                        + "</td>"
                                        + "<td>"
                                        + "<p>" + rsData.getString("PR_PIECE_NO") + "</p>"
                                        + "</td>"
                                        + "<td>"
                                        + "<p>" + rsData.getString("PR_MACHINE_NO") + "</p>"
                                        + "</td>"
                                        + "<td>"
                                        + "<p>" + rsData.getString("PR_POSITION_NO") + "</p>"
                                        + "</td>"
                                        + "</tr>";

//                                pMessage = pMessage + "<br>\t|   " + rsData.getString("PR_PIECE_STAGE") + "  |\t" + rsData.getString("PR_PIECE_NO") + "  |\t" + rsData.getString("PR_MACHINE_NO") + "  |\t" + rsData.getString("PR_POSITION_NO") + "\t|";
//                                pMessage = pMessage + "<br>-----------------------------------------------------------------------------";

                                rsData.next();
                            }
                               pMessage = pMessage + "</table><br>";                     
                        } else {
                            pMessage = pMessage + "<br>No Pieces found in Piece Register.<br>";
                        }
                        
//                        pMessage = pMessage + "</table></body></html>";
     
                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }

                } catch (Exception e) {
                    System.out.println("Error on Mail: " + e.getMessage());
                }

            }

//GAURANG            
        }

        if (SEND_TO_ALL) {
            HashMap hmSendToList;

            String recievers = "sdmlerp@dineshmills.com";
                
            if (MODULE_ID == 608) {
                pMessage = pMessage + "<br><br><br> : Email Send to : <br><br>";
                hmSendToList = clsHierarchy.getUserList(EITLERPGLOBAL.gCompanyID, HIERARCHY_ID, USER_ID, true);
                for (int i = 1; i <= hmSendToList.size(); i++) {
                    clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));
                    int U_ID = ObjUser.getAttribute("USER_ID").getInt();

                    String to = clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, U_ID);

                    System.out.println("USERID : " + U_ID + ", send_to : " + to);
                    if (!to.equals("")) {
                        recievers = recievers + "," + to;
                        pMessage = pMessage + ObjUser.getAttribute("USER_NAME").getString()+ "<br>" ;
                    }
                }
                
//                recievers = "sdmlerp@dineshmills.com,rishineekhra@dineshmills.com,gaurang@dineshmills.com,ashutosh@dineshmills.com";
                recievers = recievers + "yrpatel@dineshmills.com,amitkanti@dineshmills.com,feltpp@dineshmills.com,brdflteng@dineshmills.com,abtewary@dineshmills.com,felts@dineshmills.com,brdfltfin@dineshmills.com,feltwh@dineshmills.com";
                
//                pMessage = pMessage + "rishineekhra@dineshmills.com<br>";
//                pMessage = pMessage + "gaurang@dineshmills.com<br>";
//                pMessage = pMessage + "ashutosh@dineshmills.com<br>"; 
                pMessage = pMessage + "yrpatel@dineshmills.com,amitkanti@dineshmills.com<br>";
                pMessage = pMessage + "feltpp@dineshmills.com<br>";
                pMessage = pMessage + "brdflteng@dineshmills.com<br>"; 
                pMessage = pMessage + "abtewary@dineshmills.com<br>";
                pMessage = pMessage + "felts@dineshmills.com<br>";
                pMessage = pMessage + "brdfltfin@dineshmills.com<br>"; 
                pMessage = pMessage + "feltwh@dineshmills.com<br>"; 
                
                pMessage = pMessage + "<br><br><br>**** This is an auto-generated email, please do not reply ****<br><br>";
//                pMessage = pMessage + "</table></body></html>";
                
            } else {
                pMessage = pMessage + "<br><br><br> : Email Send to : <br>";
                hmSendToList = clsHierarchy.getUserList(EITLERPGLOBAL.gCompanyID, HIERARCHY_ID, USER_ID, true);
                for (int i = 1; i <= hmSendToList.size(); i++) {
                    clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));
                    int U_ID = ObjUser.getAttribute("USER_ID").getInt();

                    String to = clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, U_ID);

                    System.out.println("USERID : " + U_ID + ", send_to : " + to);
                    if (!to.equals("")) {
                        recievers = recievers + "," + to;
                        pMessage = pMessage + "<br>" + ObjUser.getAttribute("USER_NAME").getString();
                    }
                }
            }
            
            if (MODULE_ID == 80 || MODULE_ID == 715) {
                //recievers = recievers + "," + "feltsalesnotification@dineshmills.com";
                pMessage = pMessage + "<br>" + "feltsalesnotification@dineshmills.com";
            }
            if (MODULE_ID == 604) {
                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 57))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 57);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 57);
                }
                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 59))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 59);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 59);
                }
                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 285))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 285);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 285);
                }
                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 320))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 320);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 320);
                }
                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 19))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 19);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 19);
                }

                recievers = recievers + ",excise@dineshmills.com";
                pMessage = pMessage + "<br>excise@dineshmills.com";
            }
            if (MODULE_ID == 712) {
                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 60))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 60);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 60);
                }

                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 306))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 306);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 306);
                }

                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 57))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 57);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 57);
                }

                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 56))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 56);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 56);
                }

                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 53))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 53);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 53);
                }
            }
            
            if (MODULE_ID!=608) {
                pMessage = pMessage + "<br><br><br><br>**** This is an auto-generated email, please do not reply ****";
            }
            
            try {

                System.out.println("Recivers : " + recievers);
                System.out.println("pSubject : " + pSubject);
                System.out.println("pMessage : " + pMessage);

                SendMail(recievers, pMessage, pSubject, cc);
            } catch (Exception e) {
                System.out.println("Error Msg " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            String to = clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, USER_ID);

            System.out.println("SINGLE USER ID : " + USER_ID + ", send_to : " + to);
            if (!to.equals("")) {
                to = to + ",sdmlerp@dineshmills.com";
                try {

                    SendMail(to, pMessage, pSubject, cc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("USER : " + USER_ID + " External Mail Not Found");
                try {
                    SendMail("sdmlerp@dineshmills.com", "Mail Sending Failed due to External Mail not found, USER ID  " + USER_ID, "Mail Not Sent : " + DOC_NO, "");
                } catch (Exception e) {
                        //System.out.println("Error Msg "+e.getMessage());
                    //e.printStackTrace();
                }
            }
        }

        return "Mail Sending Done....!";
    }
    */
    public static String sendNotificationMailOfDetail(int MODULE_ID, String DOC_NO, String DOC_DATE, String PARTY_CODE, int USER_ID, int HIERARCHY_ID, boolean SEND_TO_ALL) {
        if (USER_ID == 0) {
            try {
                SendMail("sdmlerp@dineshmills.com", "Mail Sending Failed due to USER ID not set", "Mail Not Sent : " + PARTY_CODE, "");
            } catch (Exception e) {
                System.out.println("Error Msg "+e.getMessage());
                e.printStackTrace();
            }
            return "User Id not set, please set User Id";
        }

        String responce = data.getStringValueFromDB("SELECT HIERARCHY_ID FROM DINESHMILLS.D_COM_HIERARCHY where HIERARCHY_ID=" + HIERARCHY_ID + " AND MODULE_ID=" + MODULE_ID + "");
        if (responce.equals("")) {

            try {
                SendMail("sdmlerp@dineshmills.com", "Mail Sending Failed due to HIERARCHY ID AND MODULE ID missed matched,<br><br><br> Module Id " + MODULE_ID + " and Hierarchy Id " + HIERARCHY_ID + " missed matched", "Mail Not Sent : " + PARTY_CODE, "");
            } catch (Exception e) {
                System.out.println("Error Msg "+e.getMessage());
                e.printStackTrace();
            }
            return "Module Id " + MODULE_ID + " and Hierarchy Id " + HIERARCHY_ID + " missed matched";
        }

        if (HIERARCHY_ID == 0) {
            try {
                SendMail("sdmlerp@dineshmills.com", "Mail Sending Failed due to HIERARCHY ID not set ", "Mail Not Sent : " + PARTY_CODE, "");
            } catch (Exception e) {
                System.out.println("Error Msg "+e.getMessage());
                e.printStackTrace();
            }
            return "Hierarchy Id not set, Please set Hierarchy Id";
            //send copy to SDMLERP 

        }

        if (MODULE_ID == 0) {
            try {
                SendMail("sdmlerp@dineshmills.com", "Mail Sending Failed due to MODULE ID not set ", "Mail Not Sent : " + PARTY_CODE, "");
            } catch (Exception e) {
                //System.out.println("Error Msg "+e.getMessage());
                //e.printStackTrace();
            }
            return "Module not set, Please set Module Id";
        }

        String DOC_TYPE = clsModules.getModuleName(EITLERPGLOBAL.gCompanyID, MODULE_ID);

        String pMessage = "";
        String pSubject="";
        if(MODULE_ID==707 || MODULE_ID==710 || MODULE_ID==711 || MODULE_ID==732 || MODULE_ID==737 || MODULE_ID==738){
            pSubject = "Document Information, " + DOC_TYPE;
        }else if(MODULE_ID==740){
            pSubject = "Document Information, " + DOC_TYPE + " , Bale No : " + PARTY_CODE;
        }else {
            pSubject = "Document Information, " + DOC_TYPE + " , PARTY CODE : " + PARTY_CODE;
        }
        String cc = "";

//        if (MODULE_ID == 72) {
//            cc = "feltsalesnotification@dineshmills.com";
//        }
        
            

            if (MODULE_ID == 72) {
                if(SEND_TO_ALL){
                pMessage = "Document Name : " + DOC_TYPE + ",<br>Party Code : " + PARTY_CODE + ",<br>Party Name : " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, PARTY_CODE) + "<br><br><br> Document has been Approved and Forward  by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br><br><br>";
                }else{
                pMessage = "Document Name : " + DOC_TYPE + ",<br>Party Code : " + PARTY_CODE + ",<br>Party Name : " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, PARTY_CODE) + "<br><br><br> Document has been Final Approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br><br><br>";    
                }
                //<br>Party Code : "+PARTY_CODE+",<br>Party Name : "+clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, PARTY_CODE)+"
                  try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        rsData = stmt.executeQuery("SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE = '" +PARTY_CODE+ "'");
                        rsData.first();
                        
                      pMessage = pMessage + "<br> PARTY CODE : " + rsData.getString("PARTY_CODE") + " - " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData.getString("PARTY_CODE"));
                      int partytype=rsData.getInt("PARTY_TYPE");
                      if(partytype==1){
                          pMessage = pMessage + "<br> PARTY TYPE : Agent ";
                      }else if(partytype==2){
                          pMessage = pMessage + "<br> PARTY TYPE : Retailer ";
                      }else if(partytype==3){
                          pMessage = pMessage + "<br> PARTY TYPE : Wholesaler ";
                      }else if(partytype==4){
                          pMessage = pMessage + "<br> PARTY TYPE : Miscellaneous ";
                      }else if(partytype==5){
                          pMessage = pMessage + "<br> PARTY TYPE : Other ";
                      }else{
                          pMessage = pMessage + "<br> PARTY TYPE :  ";
                      }
                      //pMessage = pMessage + "<br> PARTY TYPE : " + rsData.getString("PARTY_TYPE");
                      pMessage = pMessage + "<br> MAIN CODE TYPE : " + rsData.getString("MAIN_ACCOUNT_CODE");
                      pMessage = pMessage + "<br> ADDRESS : " + rsData.getString("ADDRESS1");
                      pMessage = pMessage + "<br> DISPATCH STATION : " + rsData.getString("DISPATCH_STATION");
                      pMessage = pMessage + "<br> PAN : " + rsData.getString("PAN_NO") + " ,     GST NO : " + rsData.getString("GSTIN_NO");
                      

                        pMessage = pMessage + "<br>";
                        /*
                        pMessage = pMessage + "<table border='1'><tr><th></th><th>LENGTH</th><th align='center'> WIDTH </th><th align='center'> GSM </th><th align='center'>WEIGHT</th></tr>";
                        
                        pMessage = pMessage + "<tr>"
                                        + "<td align='center'>"
                                        + "ORDER"
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_LENGTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_WIDTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_GSM") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_THORITICAL_WEIGHT") + ""
                                        + "</td>"
                                        + "</tr>";
                        
                        pMessage = pMessage + "<tr>"
                                        + "<td align='center'>"
                                        + "ACTUAL"
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_ACTUAL_LENGTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_ACTUAL_WIDTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_ACTUAL_WEIGHT") + ""
                                        + "</td>"
                                        + "</tr>";
                        
                        pMessage = pMessage + "<tr>"
                                        + "<td align='center'>"
                                        + "BILL"
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_LENGTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_WIDTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_GSM") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_WEIGHT") + ""
                                        + "</td>"
                                        + "</tr>"
                                        + "</table>";*/                        
                        
                        
                        pMessage = pMessage + "<br> Charge Code : " + rsData.getString("CHARGE_CODE") + " ,     Charge Code 2 : " + rsData.getString("CHARGE_CODE_II");
                        pMessage = pMessage + "<br> Credit Days : " + rsData.getDouble("CREDIT_DAYS") + " ,     Extra Credit Days : " + rsData.getDouble("EXTRA_CREDIT_DAYS")+ " ,     Grace Credit Days : " + rsData.getDouble("GRACE_CREDIT_DAYS");
                        pMessage = pMessage + "<br> Bank Name : " + rsData.getString("BANK_NAME") + " ,     Bank Address : " + rsData.getString("BANK_ADDRESS");
                        pMessage = pMessage + "<br> Other Bank Name : " + rsData.getString("OTHER_BANK_NAME") + " ,     Other Bank Address : " + rsData.getString("OTHER_BANK_ADDRESS");
                        pMessage = pMessage + "<br> Critical Limit : " + rsData.getDouble("AMOUNT_LIMIT");
                        
                        pMessage = pMessage + "<br>";
                        
                        pMessage = pMessage + "<br> Transporter Code : " + rsData.getString("TRANSPORTER_ID")+ " ,     Transporter Name : " + rsData.getString("TRANSPORTER_NAME");
                        pMessage = pMessage + "<br> Insurance Code : " + rsData.getString("INSURANCE_CODE");
                        pMessage = pMessage + "<br> Remarks : " + rsData.getString("REMARKS");
                        
//                        
                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }
            }            
            
        
            if (MODULE_ID == 149) {
                if(SEND_TO_ALL){
                pMessage = "Document Name : " + DOC_TYPE + ",<br>Document No : " + DOC_NO + ",<br>Document Date : " + DOC_DATE + ",<br><br><br> Document has been Approved and Forward  by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br>";
                }else{
                pMessage = "Document Name : " + DOC_TYPE + ",<br>Document No : " + DOC_NO + ",<br>Document Date : " + DOC_DATE + ",<br><br><br> Document has been Final Approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br>";    
                }
                //pMessage= pMessage + "<br><center><b>Updation Detail:</b></center><br>";
                try {
                        Connection ConnAmend;
                        Statement stmtAmend;
                        ResultSet rsDataAmend;

                        ConnAmend = data.getConn();
                        stmtAmend = ConnAmend.createStatement();
                        rsDataAmend = stmtAmend.executeQuery("SELECT * FROM DINESHMILLS.D_SAL_PARTY_AMEND_MASTER WHERE PARTY_CODE = '"+PARTY_CODE+"' AND AMEND_ID="+DOC_NO+" ");
                        rsDataAmend.first();
                        
                      pMessage = pMessage + "<br><b> Party Code : " + rsDataAmend.getString("PARTY_CODE") + " - " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsDataAmend.getString("PARTY_CODE"))+"</b>";
                      int partytype=rsDataAmend.getInt("PARTY_TYPE");
                      if(partytype==1){
                          pMessage = pMessage + "<br> Party Type : Agent ";
                      }else if(partytype==2){
                          pMessage = pMessage + "<br> Party Type : Retailer ";
                      }else if(partytype==3){
                          pMessage = pMessage + "<br> Party Type : Wholesaler ";
                      }else if(partytype==4){
                          pMessage = pMessage + "<br> Party Type : Miscellaneous ";
                      }else if(partytype==5){
                          pMessage = pMessage + "<br> Party Type : Other ";
                      }else{
                          pMessage = pMessage + "<br> Party Type :  ";
                      }
                      //pMessage = pMessage + "<br> Party Type : " + rsDataAmend.getString("PARTY_TYPE");
                      pMessage = pMessage + "<br> Main Code Type : " + rsDataAmend.getString("MAIN_ACCOUNT_CODE");
                      pMessage = pMessage + "<br> Update Reason : " + rsDataAmend.getString("AMEND_REASON");
//                      pMessage = pMessage + "<br> ADDRESS : " + rsDataAmend.getString("ADDRESS1");
//                      pMessage = pMessage + "<br> DISPATCH STATION : " + rsDataAmend.getString("DISPATCH_STATION");
//                      pMessage = pMessage + "<br> PAN : " + rsDataAmend.getString("PAN_NO") + " ,     GST NO : " + rsDataAmend.getString("GSTIN_NO");                      
//
//                      pMessage = pMessage + "<br>";                        
//                        
//                        pMessage = pMessage + "<br> Charge Code : " + rsDataAmend.getString("CHARGE_CODE") + " ,     Charge Code 2 : " + rsDataAmend.getString("CHARGE_CODE_II");
//                        pMessage = pMessage + "<br> Credit Days : " + rsDataAmend.getDouble("CREDIT_DAYS") + " ,     Extra Credit Days : " + rsDataAmend.getDouble("EXTRA_CREDIT_DAYS")+ " ,     Grace Credit Days : " + rsDataAmend.getDouble("GRACE_CREDIT_DAYS");
//                        pMessage = pMessage + "<br> Bank Name : " + rsDataAmend.getString("BANK_NAME") + " ,     Bank Address : " + rsDataAmend.getString("BANK_ADDRESS");
//                        pMessage = pMessage + "<br> Other Bank Name : " + rsDataAmend.getString("OTHER_BANK_NAME") + " ,     Other Bank Address : " + rsDataAmend.getString("OTHER_BANK_ADDRESS");
//                        pMessage = pMessage + "<br> Critical Limit : " + rsDataAmend.getDouble("AMOUNT_LIMIT");
//                        
//                        pMessage = pMessage + "<br>";                                               
//                        
//                        pMessage = pMessage + "<br> Transporter Code : " + rsDataAmend.getString("TRANSPORTER_ID")+ " ,     Transporter Name : " + rsDataAmend.getString("TRANSPORTER_NAME");
//                        pMessage = pMessage + "<br> Insurance Code : " + rsDataAmend.getString("INSURANCE_CODE");
//                        pMessage = pMessage + "<br> Remarks : " + rsDataAmend.getString("REMARKS");
//                        
//                        pMessage = pMessage + "<br>";                                                 
//                        
//                    } catch (Exception e) {
//                        System.out.println("Error on Mail: " + e.getMessage());
//                    }
                  
                  //pMessage= pMessage + "<br><center><b>Master Detail:</b></center><br>";
                  
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        rsData = stmt.executeQuery("SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE = '" +PARTY_CODE+ "'");
                        rsData.first();
                        
//                      pMessage = pMessage + "<br> PARTY CODE : " + rsData.getString("PARTY_CODE") + " - " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData.getString("PARTY_CODE"));
//                      int partytype=rsData.getInt("PARTY_TYPE");
//                      if(partytype==1){
//                          pMessage = pMessage + "<br> PARTY TYPE : Agent ";
//                      }else if(partytype==2){
//                          pMessage = pMessage + "<br> PARTY TYPE : Retailer ";
//                      }else if(partytype==3){
//                          pMessage = pMessage + "<br> PARTY TYPE : Wholesaler ";
//                      }else if(partytype==4){
//                          pMessage = pMessage + "<br> PARTY TYPE : Miscellaneous ";
//                      }else if(partytype==5){
//                          pMessage = pMessage + "<br> PARTY TYPE : Other ";
//                      }else{
//                          pMessage = pMessage + "<br> PARTY TYPE :  ";
//                      }
//                      //pMessage = pMessage + "<br> PARTY TYPE : " + rsData.getString("PARTY_TYPE");
//                      pMessage = pMessage + "<br> MAIN CODE TYPE : " + rsData.getString("MAIN_ACCOUNT_CODE");
//                      pMessage = pMessage + "<br> ADDRESS : " + rsData.getString("ADDRESS1");
//                      pMessage = pMessage + "<br> DISPATCH STATION : " + rsData.getString("DISPATCH_STATION");
//                      pMessage = pMessage + "<br> PAN : " + rsData.getString("PAN_NO") + " ,     GST NO : " + rsData.getString("GSTIN_NO");
//                      
//                        pMessage = pMessage + "<br>";                                               
//                        
//                        
//                        pMessage = pMessage + "<br> Charge Code : " + rsData.getString("CHARGE_CODE") + " ,     Charge Code 2 : " + rsData.getString("CHARGE_CODE_II");
//                        pMessage = pMessage + "<br> Credit Days : " + rsData.getDouble("CREDIT_DAYS") + " ,     Extra Credit Days : " + rsData.getDouble("EXTRA_CREDIT_DAYS")+ " ,     Grace Credit Days : " + rsData.getDouble("GRACE_CREDIT_DAYS");
//                        pMessage = pMessage + "<br> Bank Name : " + rsData.getString("BANK_NAME") + " ,     Bank Address : " + rsData.getString("BANK_ADDRESS");
//                        pMessage = pMessage + "<br> Other Bank Name : " + rsData.getString("OTHER_BANK_NAME") + " ,     Other Bank Address : " + rsData.getString("OTHER_BANK_ADDRESS");
//                        pMessage = pMessage + "<br> Critical Limit : " + rsData.getDouble("AMOUNT_LIMIT");
//                        
//                        pMessage = pMessage + "<br>";                                               
//                        
//                        pMessage = pMessage + "<br> Transporter Code : " + rsData.getString("TRANSPORTER_ID")+ " ,     Transporter Name : " + rsData.getString("TRANSPORTER_NAME");
//                        pMessage = pMessage + "<br> Insurance Code : " + rsData.getString("INSURANCE_CODE");
//                        pMessage = pMessage + "<br> Remarks : " + rsData.getString("REMARKS");
//                        
//                        pMessage = pMessage + "<br>";                                               
//                     //                        
//                    } catch (Exception e) {
//                        System.out.println("Error on Mail: " + e.getMessage());
//                    }
           pMessage = pMessage + "<br>";                                               
           pMessage = pMessage + "<table border=1>";
           pMessage =pMessage + "<tr><td></td><td align='center'><b>Updation Detail</b></td><td align='center'><b>Master Detail</b></td></tr>"
           +"<tr><td>Address</td><td>"+rsDataAmend.getString("ADDRESS1")+"</td><td>"+rsData.getString("ADDRESS1")+"</td></tr>"
            +"<tr><td>Dispatch Station</td><td>"+rsDataAmend.getString("DISPATCH_STATION")+"</td><td>"+rsData.getString("DISPATCH_STATION")+"</td></tr>"
            +"<tr><td>PAN</td><td>"+rsDataAmend.getString("PAN_NO")+"</td><td>"+rsData.getString("PAN_NO")+"</td></tr>"
            +"<tr><td>GST NO</td><td>"+rsDataAmend.getString("GSTIN_NO")+"</td><td>"+rsData.getString("GSTIN_NO")+"</td></tr>"
            +"<tr><td>Charge Code</td><td>"+rsDataAmend.getString("CHARGE_CODE")+"</td><td>"+rsData.getString("CHARGE_CODE")+"</td></tr>"
            +"<tr><td>Charge Code 2</td><td>"+rsDataAmend.getString("CHARGE_CODE_II")+"</td><td>"+rsData.getString("CHARGE_CODE_II")+"</td></tr>"
            +"<tr><td>Credit Days</td><td>"+rsDataAmend.getDouble("CREDIT_DAYS")+"</td><td>"+rsData.getDouble("CREDIT_DAYS")+"</td></tr>"
            +"<tr><td>Extra Credit Days</td><td>"+rsDataAmend.getDouble("EXTRA_CREDIT_DAYS")+"</td><td>"+rsData.getDouble("EXTRA_CREDIT_DAYS")+"</td></tr>"
            +"<tr><td>Grace Credit Days</td><td>"+rsDataAmend.getDouble("GRACE_CREDIT_DAYS")+"</td><td>"+rsData.getDouble("GRACE_CREDIT_DAYS")+"</td></tr>"
            +"<tr><td>Bank Name</td><td>"+rsDataAmend.getString("BANK_NAME")+"</td><td>"+rsData.getString("BANK_NAME")+"</td></tr>"
            +"<tr><td>Bank Address</td><td>"+rsDataAmend.getString("BANK_ADDRESS")+"</td><td>"+rsData.getString("BANK_ADDRESS")+"</td></tr>"
            +"<tr><td>Other Bank Name</td><td>"+rsDataAmend.getString("OTHER_BANK_NAME")+"</td><td>"+rsData.getString("OTHER_BANK_NAME")+"</td></tr>"
            +"<tr><td>Other Bank Address</td><td>"+rsDataAmend.getString("OTHER_BANK_ADDRESS")+"</td><td>"+rsData.getString("OTHER_BANK_ADDRESS")+"</td></tr>"
            +"<tr><td>Critical Limit</td><td>"+rsDataAmend.getDouble("AMOUNT_LIMIT")+"</td><td>"+rsData.getDouble("AMOUNT_LIMIT")+"</td></tr>"
            +"<tr><td>Transporter Code</td><td>"+rsDataAmend.getString("TRANSPORTER_ID")+"</td><td>"+rsData.getString("TRANSPORTER_ID")+"</td></tr>"
            +"<tr><td>Transporter Name</td><td>"+rsDataAmend.getString("TRANSPORTER_NAME")+"</td><td>"+rsData.getString("TRANSPORTER_NAME")+"</td></tr>"
            +"<tr><td>Insurance Code</td><td>"+rsDataAmend.getString("INSURANCE_CODE")+"</td><td>"+rsData.getString("INSURANCE_CODE")+"</td></tr>"
            +"<tr><td>Remarks</td><td>"+rsDataAmend.getString("REMARKS")+"</td><td>"+rsData.getString("REMARKS")+"</td></tr>"
        +"</table>";
                   
                  
            
        } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }
            }
            //Invoice Parameter modification
            if (MODULE_ID == 754) {
              if(SEND_TO_ALL){
                pMessage = "Document Name : " + DOC_TYPE + ",<br>Document No : " + DOC_NO + ",<br>Document Date : " + DOC_DATE + ",<br><br> Document has been Approved and Forward  by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br>";
                }else{
                pMessage = "Document Name : " + DOC_TYPE + ",<br>Document No : " + DOC_NO + ",<br>Document Date : " + DOC_DATE + ",<br><br> Document has been Final Approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br>";    
                }
                try{
                        Connection Conn,ConnDetail,ConnRemarks;
                        Statement stmt,stmtDetail,stmtRemarks;
                        ResultSet rsData,rsDataDetail,rsDataRemarks;
                        String f6="";

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE DOC_NO='"+DOC_NO+"'");
                        rsData.first();
                        if(rsData.getInt("F6")==1){
                            f6="Yes";
                        }else{
                            f6="No";
                        }
                        
                      pMessage = pMessage + "<table border=1>";  
                      pMessage = pMessage + "<tr><td>Party Code : </td><td>"+rsData.getString("PARTY_CODE")+"</td></tr>";                      
                      pMessage = pMessage + "<tr><td> Party Name : </td><td>" + rsData.getString("PARTY_NAME")+"</td></tr>";
                      pMessage = pMessage + "<tr><td>Critical Limit : </td><td>" + rsData.getString("CRITICAL_LIMIT_NEW")+"</td></tr>";
                      pMessage = pMessage + "<tr><td>Charge Code : </td><td>" + rsData.getString("CHARGE_CODE_NEW")+"</td></tr>";
                      pMessage = pMessage + "<tr><td>F6 : </td><td>"+f6+"</td></tr>";
                      pMessage = pMessage + "<tr><td>Transporter Code : </td><td>" + rsData.getString("TRANSPORTER_CODE")+"</td></tr>";
                      pMessage = pMessage+ "<tr><td>Insurance Code : </td><td>" + rsData.getInt("INSURANCE_CODE")+"</td></tr>";
                      pMessage = pMessage + "<tr><td>Remarks : </td><td>" + rsData.getString("REMARKS")+"</td></tr>";                      
                      pMessage=pMessage + "</table>";           
                      
                      pMessage = pMessage + "<br>";
                      pMessage = pMessage + "<table border=1>";
                      pMessage =pMessage + "<tr><td align='center'><b>Bale No</b></td><td align='center'><b>Bale Date</b></td><td align='center'><b>Bill Value</b></td></tr>" ;
                      ConnDetail = data.getConn();
                        stmtDetail = ConnDetail.createStatement();
                        rsDataDetail = stmtDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE DOC_NO='"+DOC_NO+"'");
                      rsDataDetail.first();
                while (!rsDataDetail.isAfterLast()) {
                                    
                          pMessage =pMessage +"<tr><td>"+rsDataDetail.getString("BALE_NO")+"</td><td>"+EITLERPGLOBAL.formatDate(rsDataDetail.getString("BALE_DATE"))+"</td><td>"+rsDataDetail.getDouble("BILL_VALUE")+"</td></tr>";                      
                    rsDataDetail.next();
                }

           pMessage=pMessage + "</table>";
                      
           
           pMessage = pMessage + "<br>";
                      pMessage = pMessage + "<table border=1>";
                      pMessage = pMessage + "<tr><td align='center'><b>User</b></td><td align='center'><b>Remarks</b></td></tr>" ;
                      ConnRemarks = data.getConn();
                      stmtRemarks = ConnRemarks.createStatement();
                      rsDataRemarks = stmtRemarks.executeQuery("SELECT MIN(REVISION_NO) AS REVISION_NO,UPDATED_BY,ENTRY_DATE,APPROVAL_STATUS,APPROVER_REMARKS,FROM_IP FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST_H WHERE DOC_NO='"+DOC_NO+"' GROUP BY UPDATED_BY,ENTRY_DATE,APPROVAL_STATUS,APPROVER_REMARKS ORDER BY REVISION_NO,UPDATED_BY,ENTRY_DATE,APPROVAL_STATUS,APPROVER_REMARKS");
                      rsDataRemarks.first();
                while (!rsDataRemarks.isAfterLast()) {                                    
                      pMessage = pMessage +"<tr><td>"+clsUser.getUserName(2, rsDataRemarks.getInt("UPDATED_BY"))+"</td><td>"+rsDataRemarks.getString("APPROVER_REMARKS")+"</td></tr>";                      
                    rsDataRemarks.next();
                }

           pMessage=pMessage + "</table>";           
           pMessage = pMessage + "<br><br>";
                    
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
            //Invoice Parameter modification
            
            //Felt Weaving
            if(MODULE_ID == 707)
            {
                // Felt Weaving Start                 
                pMessage = "Felt Weaving Doc No : " + DOC_NO + ",<br> Doc Date : " + DOC_DATE + ",<br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br>";
                try {
                    String diffdays="";
                    Connection Conn1;
                    Statement stmt1;
                    ResultSet rsData1;

                    Conn1 = data.getConn();
                    stmt1 = Conn1.createStatement();
                    rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT = 'WEAVING' AND PROD_DOC_NO='" + DOC_NO + "'");
                    

                    pMessage = pMessage + "<br><br>";
                        //pMessage = pMessage + "<br> WEAVING DATE : "+EITLERPGLOBAL.formatDate(rsData1.getString("APPROVED_DATE"));
                        
                        pMessage = pMessage + "<br><br><u>WEAVING PIECE DETAILS</u><br>";
                        pMessage = pMessage + "<table border='1'>"
                                + "<tr>"
                                + "<th align='center'> PIECE NO </th>"
                                + "<th align='center'> PARTY CODE</th>"
                                //+ "<th align='center'> PARTY NAME </th>"
                                + "<th align='center'> WEIGHT </th>"
                                + "<th align='center'> PICKS/10CMS </th>"
                                + "<th align='center'> REED SPACE </th>"
                                + "<th align='center'> LENGTH </th>"
                                + "<th align='center'> LOOM NO </th>"
                                + "<th align='center'> REMARKS </th>"
                                + "<th align='center'> WEAVE DATE </th>"
                                + "<th align='center'> WARP NO </th>"
                                + "<th align='center'> WEAVE DIFF DAYS </th>"
                                + "</tr>";
                        
                        rsData1.first();                       
                        if (rsData1.getRow() > 0) {
                            while (!rsData1.isAfterLast()){
                                if(rsData1.getString("WEAVE_DIFF_DAYS")==null){
                                           diffdays="";
                                       }else{
                                           diffdays=rsData1.getString("WEAVE_DIFF_DAYS");
                                       }
                                pMessage = pMessage + ""
                                    + "<tr>"
                                    + "<td align='center'> "+rsData1.getString("PROD_PIECE_NO")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("PROD_PARTY_CODE")+" </td>"
                                    //+ "<td align='center'> "+clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData1.getString("PROD_PARTY_CODE"))+" </td>"
                                    + "<td align='center'> "+rsData1.getDouble("WEIGHT")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("PICKS_PER_10CMS")+" </td>"
                                    + "<td align='center'> "+rsData1.getDouble("REED_SPACE")+" </td>"
                                    + "<td align='center'> "+rsData1.getDouble("LENGTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getInt("LOOM_NO")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("REMARKS")+" </td>"
                                    + "<td align='center'> "+EITLERPGLOBAL.formatDate(rsData1.getString("WEAVE_DATE"))+" </td>"
                                    + "<td align='center'> "+rsData1.getString("WARP_NO")+" </td>"
                                       
                                    + "<td align='center'> "+diffdays+" </td>"                                    
                                    + "</tr>";
                                rsData1.next();
                            }
                        }
                        pMessage = pMessage + "</table>";

                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }                
            }
            //End Felt Weaving
            
                        //Felt Mending
            if(MODULE_ID == 711)
            {
                // Felt Mending Start                 
                pMessage = "Felt Mending Doc No : " + DOC_NO + ",<br> Doc Date : " + DOC_DATE + ",<br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br>";
                try {                    
                    Connection Conn1;
                    Statement stmt1;
                    ResultSet rsData1;

                    Conn1 = data.getConn();
                    stmt1 = Conn1.createStatement();
                    rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT = 'MENDING' AND PROD_DOC_NO='" + DOC_NO + "'");
                    

                    pMessage = pMessage + "<br><br>";
                        //pMessage = pMessage + "<br> WEAVING DATE : "+EITLERPGLOBAL.formatDate(rsData1.getString("APPROVED_DATE"));
                        
                        pMessage = pMessage + "<br><br><u>MENDING PIECE DETAILS</u><br>";
                        pMessage = pMessage + "<table border='1'>"
                                + "<tr>"
                                + "<th align='center'> PIECE NO </th>"
                                + "<th align='center'> PARTY CODE</th>"
                                //+ "<th align='center'> PARTY NAME </th>"
                                + "<th align='center'> WEIGHT </th>"
                                + "<th align='center'> GROUP </th>"
                                + "<th align='center'> REMARKS </th>"                                
                                + "</tr>";
                        
                        rsData1.first();                       
                        if (rsData1.getRow() > 0) {
                            while (!rsData1.isAfterLast()){
                                
                                pMessage = pMessage + ""
                                    + "<tr>"
                                    + "<td align='center'> "+rsData1.getString("PROD_PIECE_NO")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("PROD_PARTY_CODE")+" </td>"
                                    //+ "<td align='center'> "+clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData1.getString("PROD_PARTY_CODE"))+" </td>"
                                    + "<td align='center'> "+rsData1.getDouble("WEIGHT")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("GROUP")+" </td>"                                    
                                    + "<td align='center'> "+rsData1.getString("REMARKS")+" </td>"
                                    + "</tr>";
                                rsData1.next();
                            }
                        }
                        pMessage = pMessage + "</table>";

                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }                
            }
            //End Felt Mending
            
            //Felt Needling
            if(MODULE_ID == 710)
            {
                // Felt Mending Start                 
                pMessage = "Felt Needling Doc No : " + DOC_NO + ",<br> Doc Date : " + DOC_DATE + ",<br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br>";
                try {                    
                    Connection Conn1;
                    Statement stmt1;
                    ResultSet rsData1;

                    Conn1 = data.getConn();
                    stmt1 = Conn1.createStatement();
                    rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT = 'NEEDLING' AND PROD_DOC_NO='" + DOC_NO + "'");
                    

                    pMessage = pMessage + "<br><br>";
                        //pMessage = pMessage + "<br> WEAVING DATE : "+EITLERPGLOBAL.formatDate(rsData1.getString("APPROVED_DATE"));
                        
                        pMessage = pMessage + "<br><br><u>NEEDLING PIECE DETAILS</u><br>";
                        pMessage = pMessage + "<table border='1'>"
                                + "<tr>"
                                + "<th align='center'> PIECE NO </th>"
                                + "<th align='center'> PARTY CODE</th>"
                                //+ "<th align='center'> PARTY NAME </th>"
                                + "<th align='center'> WEIGHT </th>"                                
                                + "<th align='center'> REMARKS </th>"                                
                                + "</tr>";
                        
                        rsData1.first();                       
                        if (rsData1.getRow() > 0) {
                            while (!rsData1.isAfterLast()){
                                
                                pMessage = pMessage + ""
                                    + "<tr>"
                                    + "<td align='center'> "+rsData1.getString("PROD_PIECE_NO")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("PROD_PARTY_CODE")+" </td>"
                                    //+ "<td align='center'> "+clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData1.getString("PROD_PARTY_CODE"))+" </td>"
                                    + "<td align='center'> "+rsData1.getDouble("WEIGHT")+" </td>"                                    
                                    + "<td align='center'> "+rsData1.getString("REMARKS")+" </td>"
                                    + "</tr>";
                                rsData1.next();
                            }
                        }
                        pMessage = pMessage + "</table>";

                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }                
            }
            //End Felt Needling
           
            //Felt Year End Discount Entry Form1
            if(MODULE_ID == 737)
            {
                // Felt Year End Disc Entry form1                 
                String PeriodFrom=data.getStringValueFromDB("SELECT YEAR_FROM_DATE FROM PRODUCTION.FELT_YEAR_END_DISCOUNT_HEADER WHERE YEAR_END_ID='" + DOC_NO + "'");
                String PeriodTo=data.getStringValueFromDB("SELECT YEAR_TO_DATE FROM PRODUCTION.FELT_YEAR_END_DISCOUNT_HEADER WHERE YEAR_END_ID='" + DOC_NO + "'");
                pMessage = "Felt Year End Discount ID : " + DOC_NO + ",<br> Disc Date : " + DOC_DATE + " ";
                pMessage= pMessage + "<br>Period From : "+ EITLERPGLOBAL.formatDate(PeriodFrom) +"  To  "+ EITLERPGLOBAL.formatDate(PeriodTo) +" ";
                if(SEND_TO_ALL){
                pMessage= pMessage + "<br><br> Document has been Approved and Forward  by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br>";    
                }else{
                pMessage= pMessage + "<br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br>";
                }
                try {                    
                    Connection Conn1;
                    Statement stmt1;
                    ResultSet rsData1;

                    Conn1 = data.getConn();
                    stmt1 = Conn1.createStatement();
                    rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_YEAR_END_DISCOUNT_DETAIL WHERE YEAR_END_ID='" + DOC_NO + "'");
                    

                    //pMessage = pMessage + "<br><br>";
                        //pMessage = pMessage + "<br> WEAVING DATE : "+EITLERPGLOBAL.formatDate(rsData1.getString("APPROVED_DATE"));
                        
                        pMessage = pMessage + "<br><br><u>FELT YEAR END DISCOUNT DETAILS (Groupwise)</u><br>";
                        pMessage = pMessage + "<table border='1'>"
                                + "<tr>"
                                //+ "<th align='center'> PIECE NO </th>"
                                + "<th align='center'> Party Code</th>"
                                + "<th align='center'> Party Name </th>"
                                + "<th align='center'> Target(Lakhs) </th>"                                
                                + "<th align='center'> Achieved Turn Over </th>"                                
                                + "<th align='center'> Yes/No </th>"                                
                                + "<th align='center'> 2% Achieved Diff </th>"                                
                                + "<th align='center'> Remarks </th>"                                
                                + "</tr>";
                        
                        rsData1.first();                       
                        if (rsData1.getRow() > 0) {
                            while (!rsData1.isAfterLast()){
                                
                                pMessage = pMessage + ""
                                    + "<tr>"
                                    + "<td align='center'> "+rsData1.getString("YEAR_END_PARTY_CODE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("YEAR_END_PARTY_NAME")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("YEAR_END_TURN_OVER")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("YEAR_END_TARGET_ACHIV")+" </td>"                                    
                                    + "<td align='center'> "+rsData1.getString("YEAR_END_YES_NO")+" </td>"
                                    + "<td align='center'> </td>"
                                    + "<td align='center'> "+rsData1.getString("YEAR_END_REMARKS")+" </td>"
                                    + "</tr>";
                                rsData1.next();
                            }
                        }
                        pMessage = pMessage + "</table>";

                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }                
            }
            //End Felt Year End Discount Form1
            
            //Felt Year End Discount Entry Form2
            if(MODULE_ID == 738)
            {
                // Felt Year End Disc Entry form2   
                String PeriodFrom=data.getStringValueFromDB("SELECT FORM2_FROM_DATE FROM PRODUCTION.FELT_YEAR_END_DISCOUNT_HEADER_FORM2 WHERE FORM2_YEAR_END_ID='" + DOC_NO + "'");
                String PeriodTo=data.getStringValueFromDB("SELECT FORM2_TO_DATE FROM PRODUCTION.FELT_YEAR_END_DISCOUNT_HEADER_FORM2 WHERE FORM2_YEAR_END_ID='" + DOC_NO + "'");
                pMessage = "Felt Year End Discount ID : " + DOC_NO + ",<br> Disc Date : " + DOC_DATE + "";
                pMessage= pMessage + "<br>Period From : "+EITLERPGLOBAL.formatDate(PeriodFrom)+"  To  "+EITLERPGLOBAL.formatDate(PeriodTo)+" ";                
                if(SEND_TO_ALL){       
                pMessage = pMessage + "<br><br> Document has been Approved and Forward  by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br>";
                }
                else{
                pMessage = pMessage + "<br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br>";    
                }
                try {                    
                    Connection Conn1;
                    Statement stmt1;
                    ResultSet rsData1;

                    Conn1 = data.getConn();
                    stmt1 = Conn1.createStatement();
                    rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_YEAR_END_DISCOUNT_DETAIL_FORM2 WHERE FORM2_YEAR_END_ID='" + DOC_NO + "'");
                    

                    //pMessage = pMessage + "<br><br>";
                        //pMessage = pMessage + "<br> WEAVING DATE : "+EITLERPGLOBAL.formatDate(rsData1.getString("APPROVED_DATE"));
                        
                        pMessage = pMessage + "<br><br><u>FELT YEAR END DISCOUNT DETAILS (Partywise)</u><br>";
                        pMessage = pMessage + "<table border='1'>"
                                + "<tr>"
                                + "<th align='center'> Group Party Code </th>"
                                + "<th align='center'> Party Code</th>"
                                + "<th align='center'> Party Name </th>"
                                + "<th align='center'> Product Code </th>"
                                + "<th align='center'> Target(Lakhs) </th>"                                
                                + "<th align='center'> Achieved Turn Over </th>"                                
                                + "<th align='center'> Percentage </th>"                                
                                + "<th align='center'> Seam Percentage </th>"                                
                                + "<th align='center'> Yes/No </th>"                                
                                + "<th align='center'> Remarks1 </th>"                                
                                + "<th align='center'> Remarks2 </th>"                                
                                + "<th align='center'> Effective From </th>"                                
                                + "<th align='center'> Effective To </th>"                                
                                + "</tr>";
                        
                        rsData1.first();                       
                        if (rsData1.getRow() > 0) {
                            while (!rsData1.isAfterLast()){
                                
                                pMessage = pMessage + ""
                                    + "<tr>"
                                    + "<td align='center'> "+rsData1.getString("FORM2_YEAR_END_MAIN_PARTY_CODE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FORM2_YEAR_END_PARTY_CODE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FORM2_YEAR_END_PARTY_NAME")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FORM2_YEAR_END_PRODUCT_CODE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FORM2_YEAR_END_TURN_OVER")+" </td>"
                                    + "<td align='center'> "+rsData1.getDouble("FORM2_YEAR_END_TARGET_ACHIV")+" </td>"                                    
                                    + "<td align='center'> "+rsData1.getDouble("FORM2_YEAR_PERCENT")+" </td>"
                                    + "<td align='center'> "+rsData1.getDouble("FORM2_YEAR_SEAM_PERCENT")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FORM2_YEAR_END_YES_NO")+" </td>"                                    
                                    + "<td align='center'> "+rsData1.getString("FORM2_YEAR_END_REMARKS1")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FORM2_YEAR_END_REMARKS2")+" </td>"
                                    + "<td align='center'> "+EITLERPGLOBAL.formatDate(rsData1.getString("FORM2_EFFECTIVE_FROM"))+" </td>"
                                    + "<td align='center'> "+EITLERPGLOBAL.formatDate(rsData1.getString("FORM2_EFFECTIVE_TO"))+" </td>"
                                    + "</tr>";
                                rsData1.next();
                            }
                        }
                        pMessage = pMessage + "</table>";

                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }                
            }
            //End Felt Year End Discount Form2
            
            //Start Felt Unadjusted trn
            if(MODULE_ID == 732)
            {
                
                String PeriodFrom=data.getStringValueFromDB("SELECT UNADJ_FROM_DATE FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER WHERE UNADJ_ID='" + DOC_NO + "'");
                String PeriodTo=data.getStringValueFromDB("SELECT UNADJ_TO_DATE FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER WHERE UNADJ_ID='" + DOC_NO + "'");
                pMessage = "Felt Unadjusted ID : " + DOC_NO + ",<br> Date : " + DOC_DATE + "";
                pMessage= pMessage + "<br>Period From : "+EITLERPGLOBAL.formatDate(PeriodFrom)+"  To  "+EITLERPGLOBAL.formatDate(PeriodTo)+" ";                
                if(SEND_TO_ALL){       
                pMessage = pMessage + "<br><br> Document has been Approved and Forward  by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br>";
                }
                else{
                pMessage = pMessage + "<br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br>";    
                }
                try {                    
                    Connection Conn1,ConnRemarks;
                    Statement stmt1,stmtRemarks;
                    ResultSet rsData1,rsDataRemarks;
                    
                    Conn1 = data.getConn();
                    stmt1 = Conn1.createStatement();
                    rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL WHERE UNADJ_ID='" + DOC_NO + "'");
                    

                    //pMessage = pMessage + "<br><br>";
                        //pMessage = pMessage + "<br> WEAVING DATE : "+EITLERPGLOBAL.formatDate(rsData1.getString("APPROVED_DATE"));
                        
                        pMessage = pMessage + "<br><br><u>UNADJUSTED DETAIL </u><br>";
                        pMessage = pMessage + "<table border='1'>"
                                + "<tr>"
                                + "<th align='center'> Party Name </th>"
                                + "<th align='center'> Party Code</th>"
                                + "<th align='center'> Piece No </th>"
                                + "<th align='center'> Product Code </th>"
                                + "<th align='center'> Invoice No </th>"                                
                                + "<th align='center'> Invoice Date </th>"                                
                                + "<th align='center'> KGS </th>"                                
                                + "<th align='center'> SQR MTR </th>"                                
                                + "<th align='center'> WIDTH </th>"                                
                                + "<th align='center'> LENGTH </th>"                                
                                + "<th align='center'> RATE </th>"                                
                                + "<th align='center'> BASIC AMT </th>"                                
                                + "<th align='center'> INV DISC % </th>"                                
                                + "<th align='center'> SANC DISC % </th>"                                
                                + "<th align='center'> WORKING DISC % </th>"                                
                                + "<th align='center'> INV SEAM CHARGES </th>"                                
                                + "<th align='center'> SANC SEAM % </th>"                                
                                + "<th align='center'> DISC AMT </th>"                                
                                + "<th align='center'> REMARK1 </th>"                                
                                + "<th align='center'> REMARK2 </th>"                                
                                + "</tr>";
                        
                        rsData1.first();                       
                        if (rsData1.getRow() > 0) {
                            while (!rsData1.isAfterLast()){
                                
                                pMessage = pMessage + ""
                                    + "<tr>"
                                    + "<td align='center'> "+rsData1.getString("PARTY_NAME")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("PARTY_CODE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("PIECE_NO")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("PRODUCT_CODE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("INVOICE_NO")+" </td>"
                                    + "<td align='center'> "+EITLERPGLOBAL.formatDate(rsData1.getString("INVOICE_DATE"))+" </td>"                                     
                                    + "<td align='center'> "+rsData1.getDouble("KG")+" </td>"
                                    + "<td align='center'> "+rsData1.getDouble("SQR_MTR")+" </td>"
                                    + "<td align='center'> "+rsData1.getDouble("WIDTH")+" </td>"                                    
                                    + "<td align='center'> "+rsData1.getDouble("LENGTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getDouble("RATE")+" </td>"
                                    + "<td align='center'> "+rsData1.getDouble("INV_BASIC_AMT")+" </td>"
                                    + "<td align='center'> "+rsData1.getDouble("INV_DISC_PER")+" </td>"
                                    + "<td align='center'> "+rsData1.getDouble("SANC_DISC_PER")+" </td>"
                                    + "<td align='center'> "+rsData1.getDouble("WORK_DISC_PER")+" </td>"
                                    + "<td align='center'> "+rsData1.getDouble("INV_SEAM_CHARGES")+" </td>"                                    
                                    + "<td align='center'> "+rsData1.getDouble("SANC_SEAM_CHARGES")+" </td>"
                                    //+ "<td align='center'> "+rsData1.getDouble("SEAM_PER")+" </td>"
                                    + "<td align='center'> "+rsData1.getDouble("DISC_AMT")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("D_REMARK1")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("D_REMARK2")+" </td>"
                                    + "</tr>";
                                rsData1.next();
                            }
                        }
                        pMessage = pMessage + "</table>";
                        
                        pMessage = pMessage + "<br>";
                      pMessage = pMessage + "<table border=1>";
                      pMessage = pMessage + "<tr><td align='center'><b>User</b></td><td align='center'><b>Remarks</b></td></tr>" ;
                      ConnRemarks = data.getConn();
                      stmtRemarks = ConnRemarks.createStatement();
                      //rsDataRemarks = stmtRemarks.executeQuery("SELECT MIN(REVISION_NO) AS REVISION_NO,UPDATED_BY,ENTRY_DATE,APPROVAL_STATUS,APPROVER_REMARKS,FROM_IP FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST_H WHERE DOC_NO='"+DOC_NO+"' GROUP BY UPDATED_BY,ENTRY_DATE,APPROVAL_STATUS,APPROVER_REMARKS ORDER BY REVISION_NO,UPDATED_BY,ENTRY_DATE,APPROVAL_STATUS,APPROVER_REMARKS");
                      rsDataRemarks = stmtRemarks.executeQuery("SELECT * FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER_H WHERE UNADJ_ID='"+DOC_NO+"' ORDER BY REVISION_NO");
                      rsDataRemarks.first();
                while (!rsDataRemarks.isAfterLast()) {                                    
                      pMessage = pMessage +"<tr><td>"+clsUser.getUserName(2, rsDataRemarks.getInt("UPDATE_BY"))+"</td><td>"+rsDataRemarks.getString("APPROVER_REMARKS")+"</td></tr>";                      
                    rsDataRemarks.next();
                }

           pMessage=pMessage + "</table>";           

                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }                
            }
            //End Felt Unadjusted trn 
                        
            
            //Bale Reopen
            if (MODULE_ID == 740) {
              //if(SEND_TO_ALL){
              //pMessage = "Document Name : " + DOC_TYPE + ",<br>Document No : " + DOC_NO + ",<br>Document Date : " + DOC_DATE + ",<br><br><br> Document has been Approved and Forward  by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br>";
                //}else{
                pMessage = "Document Name : " + DOC_TYPE + ",<br>Document No : " + DOC_NO + ",<br><br><br> Document has been Final Approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br>";    
                //}
                try{
                        Connection Conn,ConnDetail,ConnRemarks;
                        Statement stmt,stmtDetail,stmtRemarks;
                        ResultSet rsData,rsDataDetail,rsDataRemarks;
                

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_REOPEN_BALE_HEADER WHERE DOC_NO='"+DOC_NO+"'");
                        rsData.first();
                        
                        
                      pMessage = pMessage + "<table border=1>";  
                      pMessage = pMessage + "<tr><td>Bale No : </td><td>"+rsData.getString("BALE_NO")+"</td></tr>";                      
                      pMessage = pMessage + "<tr><td> Bale Date : </td><td>" + EITLERPGLOBAL.formatDate(rsData.getString("BALE_DATE"))+"</td></tr>";
                      pMessage = pMessage + "<tr><td>Party Code : </td><td>" + rsData.getString("PARTY_CODE")+"</td></tr>";
                      pMessage = pMessage + "<tr><td>Party Name : </td><td>" + rsData.getString("PARTY_NAME")+"</td></tr>";
                      pMessage = pMessage + "<tr><td>Station : </td><td>"+rsData.getString("STATION")+"</td></tr>";
                      pMessage = pMessage + "<tr><td>Transport Mode : </td><td>" + rsData.getString("TRANSPORT_MODE")+"</td></tr>";
                      pMessage = pMessage+ "<tr><td>Mode Of Packing : </td><td>" + rsData.getString("MODE_PACKING")+"</td></tr>";
                      pMessage = pMessage + "<tr><td>Box Size : </td><td>" + rsData.getString("BOX_SIZE")+"</td></tr>";                      
                      pMessage=pMessage + "</table>";           
                      
                      pMessage = pMessage + "<br>";
                      pMessage = pMessage + "<table border='1'>"
                                + "<tr>"
                                + "<th align='center'> Piece No </th>"
                                + "<th align='center'> Length </th>"
                                + "<th align='center'> Width </th>"                                
                                + "<th align='center'> GSM </th>"                                
                                + "<th align='center'> SQM </th>"                                
                                + "<th align='center'> Syn % </th>"                                
                                + "<th align='center'> Style </th>"                                
                                + "<th align='center'> Product Code </th>"                                
                                + "<th align='center'> Position Description </th>"                                
                                + "<th align='center'> Machine No </th>"                                
                                + "<th align='center'> Order No </th>"                                
                                + "<th align='center'> Order Date </th>"                                                                
                                + "</tr>";
                      ConnDetail = data.getConn();
                        stmtDetail = ConnDetail.createStatement();
                        rsDataDetail = stmtDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_REOPEN_BALE_DETAIL WHERE DOC_NO='"+DOC_NO+"'");
                      rsDataDetail.first();
                while (!rsDataDetail.isAfterLast()) {
                          String MachinePosDesc=data.getStringValueFromDB("SELECT POSITION_DESC FROM PRODUCTION.FELT_MACHINE_POSITION_MST WHERE POSITION_NO='"+rsDataDetail.getString("MCN_POSITION_DESC")+"'");
                          System.out.println(MachinePosDesc);
                          pMessage = pMessage + ""
                                    + "<tr>"
                                    + "<td align='center'> "+rsDataDetail.getString("PIECE_NO")+" </td>"
                                    + "<td align='center'> "+rsDataDetail.getString("LENGTH")+" </td>"
                                    + "<td align='center'> "+rsDataDetail.getString("WIDTH")+" </td>"
                                    + "<td align='center'> "+rsDataDetail.getString("GSM")+" </td>"
                                    + "<td align='center'> "+rsDataDetail.getString("SQM")+" </td>"
                                    + "<td align='center'> "+rsDataDetail.getString("SYN_PER")+" </td>"                                     
                                    + "<td align='center'> "+rsDataDetail.getString("STYLE")+" </td>"
                                    + "<td align='center'> "+rsDataDetail.getString("PRODUCT_CODE")+" </td>"
                                    + "<td align='center'> "+MachinePosDesc+" </td>"                                    
                                    + "<td align='center'> "+rsDataDetail.getString("MACHINE_NO")+" </td>"
                                    + "<td align='center'> "+rsDataDetail.getString("ORDER_NO")+" </td>"
                                    + "<td align='center'> "+EITLERPGLOBAL.formatDate(rsDataDetail.getString("ORDER_DATE"))+" </td>"                                    
                                    + "</tr>";
                                
                    rsDataDetail.next();
                }

           pMessage=pMessage + "</table>";
           pMessage = pMessage + "<br><br>";
                    
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
            //Bale Reopen
            

        if (SEND_TO_ALL) {
            HashMap hmSendToList;

            String recievers = "sdmlerp@dineshmills.com";
                
            if (MODULE_ID == 72  || MODULE_ID==149) {
                pMessage = pMessage + "<br><br><br> : Email Send to : <br><br>";
                //hmSendToList = clsHierarchy.getUserList(EITLERPGLOBAL.gCompanyID, HIERARCHY_ID, USER_ID, true);
//                for (int i = 1; i <= hmSendToList.size(); i++) {
//                    clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));
//                    int U_ID = ObjUser.getAttribute("USER_ID").getInt();
//
//                    String to = clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, U_ID);
//
//                    System.out.println("USERID : " + U_ID + ", send_to : " + to);
//                    if (!to.equals("")) {
//                        recievers = recievers + "," + to;
//                        pMessage = pMessage + ObjUser.getAttribute("USER_NAME").getString()+ "<br>" ;
//                    }
//                }
                
                //recievers = "sdmlerp@dineshmills.com,rishineekhra@dineshmills.com,gaurang@dineshmills.com,ashutosh@dineshmills.com";
                recievers = recievers+ "," + "vdshanbhag@dineshmills.com,atulshah@dineshmills.com,sunil@dineshmills.com,hcpatel@dineshmills.com,mva@dineshmills.com,manoj@dineshmills.com,aditya@dineshmills.com";
                
                //pMessage = pMessage + "rishineekhra@dineshmills.com<br>";
                //pMessage = pMessage + "gaurang@dineshmills.com<br>";
                //pMessage = pMessage + "ashutosh@dineshmills.com<br>"; 
                pMessage = pMessage + "vdshanbhag@dineshmills.com<br>";
                pMessage = pMessage + "atulshah@dineshmills.com<br>";
                pMessage = pMessage + "sunil@dineshmills.com<br>"; 
                pMessage = pMessage + "hcpatel@dineshmills.com<br>";
                pMessage = pMessage + "mva@dineshmills.com<br>";
                pMessage = pMessage + "manoj@dineshmills.com<br>"; 
                pMessage = pMessage + "aditya@dineshmills.com<br>"; 
//                
                pMessage = pMessage + "<br><br><br>**** This is an auto-generated email, please do not reply ****<br><br>";
//                pMessage = pMessage + "</table></body></html>";
                
            }
            if(MODULE_ID==754){
                pMessage = pMessage + "<br><br><br> : Email Send to : <br><br>";
                
                //recievers = "sdmlerp@dineshmills.com,rishineekhra@dineshmills.com,gaurang@dineshmills.com,ashutosh@dineshmills.com";
                recievers = recievers+ "," + "vdshanbhag@dineshmills.com,atulshah@dineshmills.com,sunil@dineshmills.com,manoj@dineshmills.com,aditya@dineshmills.com";
                
//                pMessage = pMessage + "rishineekhra@dineshmills.com<br>";
//                pMessage = pMessage + "gaurang@dineshmills.com<br>";
//                pMessage = pMessage + "ashutosh@dineshmills.com<br>"; 
                pMessage = pMessage + "vdshanbhag@dineshmills.com<br>";
                pMessage = pMessage + "atulshah@dineshmills.com<br>";
                pMessage = pMessage + "sunil@dineshmills.com<br>"; 
                //pMessage = pMessage + "hcpatel@dineshmills.com<br>";                
                pMessage = pMessage + "manoj@dineshmills.com<br>"; 
                pMessage = pMessage + "aditya@dineshmills.com<br>"; 
//             
                pMessage = pMessage + "<br><br><br>**** This is an auto-generated email, please do not reply ****<br><br>";

            }
            
            if(MODULE_ID==732 || MODULE_ID==737 || MODULE_ID==738){
                pMessage = pMessage + "<br><br><br> : Email Send to : <br><br>";
                
                //recievers = "sdmlerp@dineshmills.com,rishineekhra@dineshmills.com,gaurang@dineshmills.com,ashutosh@dineshmills.com";
                recievers = recievers+ "," + "manoj@dineshmills.com,vdshanbhag@dineshmills.com,atulshah@dineshmills.com,soumen@dineshmills.com";
                
//                pMessage = pMessage + "rishineekhra@dineshmills.com<br>";
//                pMessage = pMessage + "gaurang@dineshmills.com<br>";
//                pMessage = pMessage + "ashutosh@dineshmills.com<br>"; 
                
                pMessage = pMessage + "manoj@dineshmills.com<br>";
                pMessage = pMessage + "vdshanbhag@dineshmills.com<br>";
                pMessage = pMessage + "atulshah@dineshmills.com<br>"; 
                pMessage = pMessage + "soumen@dineshmills.com<br>";                 
                pMessage = pMessage + "<br><br><br>**** This is an auto-generated email, please do not reply ****<br><br>";

            }
            
            try {

                System.out.println("Recivers : " + recievers);
                System.out.println("pSubject : " + pSubject);
                System.out.println("pMessage : " + pMessage);

                SendMail(recievers, pMessage, pSubject, cc);
            } catch (Exception e) {
                System.out.println("Error Msg " + e.getMessage());
                e.printStackTrace();
            }
        } else {       
            String recievers = "sdmlerp@dineshmills.com";
            
            if (MODULE_ID == 72 || MODULE_ID==149) {
                pMessage = pMessage + "<br><br><br> : Email Send to : <br><br>";
                //recievers = "sdmlerp@dineshmills.com,rishineekhra@dineshmills.com,gaurang@dineshmills.com,ashutosh@dineshmills.com";
                recievers = recievers +"," + "vdshanbhag@dineshmills.com,atulshah@dineshmills.com,manoj@dineshmills.com,aditya@dineshmills.com";
                
                //pMessage = pMessage + "rishineekhra@dineshmills.com<br>";
                //pMessage = pMessage + "gaurang@dineshmills.com<br>";
                //pMessage = pMessage + "ashutosh@dineshmills.com<br>"; 
                pMessage = pMessage + "vdshanbhag@dineshmills.com<br>";
                pMessage = pMessage + "atulshah@dineshmills.com<br>";
                pMessage = pMessage + "manoj@dineshmills.com<br>"; 
                pMessage = pMessage + "aditya@dineshmills.com<br>"; 
//              
                pMessage = pMessage + "<br><br><br>**** This is an auto-generated email, please do not reply ****<br><br>";
//                pMessage = pMessage + "</table></body></html>";
                
            }
            if(MODULE_ID==754){
                pMessage = pMessage + "<br><br><br> : Email Send to : <br><br>";
                
                //recievers = "sdmlerp@dineshmills.com,rishineekhra@dineshmills.com,gaurang@dineshmills.com,ashutosh@dineshmills.com";
                recievers = recievers+ "," + "vdshanbhag@dineshmills.com,atulshah@dineshmills.com,manoj@dineshmills.com,aditya@dineshmills.com";
                
                //pMessage = pMessage + "rishineekhra@dineshmills.com<br>";
                //pMessage = pMessage + "gaurang@dineshmills.com<br>";
                //pMessage = pMessage + "ashutosh@dineshmills.com<br>"; 
                pMessage = pMessage + "vdshanbhag@dineshmills.com<br>";
                pMessage = pMessage + "atulshah@dineshmills.com<br>";
                pMessage = pMessage + "manoj@dineshmills.com<br>"; 
                pMessage = pMessage + "aditya@dineshmills.com<br>"; 
//             
                
                pMessage = pMessage + "<br><br><br>**** This is an auto-generated email, please do not reply ****<br><br>";

            }
            
            if(MODULE_ID==707 || MODULE_ID==710 || MODULE_ID==711){
                pMessage = pMessage + "<br><br><br> : Email Send to : <br><br>";
                
                //recievers = "sdmlerp@dineshmills.com,rishineekhra@dineshmills.com,gaurang@dineshmills.com,ashutosh@dineshmills.com";
                recievers = recievers+ "," + "yrpatel@dineshmills.com,amitkanti@dineshmills.com,abtewary@dineshmills.com,feltsalesnotification@dineshmills.com";
                
//                pMessage = pMessage + "rishineekhra@dineshmills.com<br>";
//                pMessage = pMessage + "gaurang@dineshmills.com<br>";
//                pMessage = pMessage + "ashutosh@dineshmills.com<br>"; 
                pMessage = pMessage + "yrpatel@dineshmills.com,amitkanti@dineshmills.com<br>";
                pMessage = pMessage + "abtewary@dineshmills.com<br>";
                pMessage = pMessage + "feltsalesnotification@dineshmills.com<br>"; 
                
                pMessage = pMessage + "<br><br><br>**** This is an auto-generated email, please do not reply ****<br><br>";
                

            }
            
            if(MODULE_ID==732 || MODULE_ID==737 || MODULE_ID==738){
                pMessage = pMessage + "<br><br><br> : Email Send to : <br><br>";
                
                //recievers = "sdmlerp@dineshmills.com,rishineekhra@dineshmills.com,gaurang@dineshmills.com,ashutosh@dineshmills.com";
                recievers = recievers+ "," + "manoj@dineshmills.com,vdshanbhag@dineshmills.com,atulshah@dineshmills.com,soumen@dineshmills.com";
                
                //pMessage = pMessage + "rishineekhra@dineshmills.com<br>";
                //pMessage = pMessage + "gaurang@dineshmills.com<br>";
                //pMessage = pMessage + "ashutosh@dineshmills.com<br>"; 
                pMessage = pMessage + "manoj@dineshmills.com<br>";
                pMessage = pMessage + "vdshanbhag@dineshmills.com<br>";
                pMessage = pMessage + "atulshah@dineshmills.com<br>"; 
                pMessage = pMessage + "soumen@dineshmills.com<br>"; 
                pMessage = pMessage + "<br><br><br>**** This is an auto-generated email, please do not reply ****<br><br>";

            }
            
            if(MODULE_ID==740){
                pMessage = pMessage + "<br><br><br> : Email Send to : <br><br>";
                
                //recievers = "sdmlerp@dineshmills.com,rishineekhra@dineshmills.com,ashutosh@dineshmills.com";
                recievers = recievers+ "," + "vdshanbhag@dineshmills.com,manoj@dineshmills.com,atulshah@dineshmills.com";
                
                pMessage = pMessage + "vdshanbhag@dineshmills.com<br>";                
                pMessage = pMessage + "manoj@dineshmills.com<br>"; 
                pMessage = pMessage + "atulshah@dineshmills.com<br>";
                pMessage = pMessage + "<br><br><br>**** This is an auto-generated email, please do not reply ****<br><br>";

            }
            
            try {

                System.out.println("Recivers : " + recievers);
                System.out.println("pSubject : " + pSubject);
                System.out.println("pMessage : " + pMessage);

                SendMail(recievers, pMessage, pSubject, cc);
            } catch (Exception e) {
                System.out.println("Error Msg " + e.getMessage());
                e.printStackTrace();
            }   
        }
        return "Mail Sending Done....!";
    }
    
    //Final Approval Notification with Resend Parameter
    public static String sendFinalApprovalMail(int MODULE_ID, String DOC_NO, String DOC_DATE, String PARTY_CODE, int USER_ID, int HIERARCHY_ID, boolean SEND_TO_ALL, int Resend_By) {
        if (USER_ID == 0) {
            try {
                SendMail("sdmlerp@dineshmills.com", "Mail Sending Failed due to USER ID not set", "Mail Not Sent : " + DOC_NO, "");
            } catch (Exception e) {
                //System.out.println("Error Msg "+e.getMessage());
                //e.printStackTrace();
            }
            return "User Id not set, please set User Id";
        }

        String responce = data.getStringValueFromDB("SELECT HIERARCHY_ID FROM DINESHMILLS.D_COM_HIERARCHY where HIERARCHY_ID=" + HIERARCHY_ID + " AND MODULE_ID=" + MODULE_ID + "");
        if (responce.equals("")) {

            try {
                SendMail("sdmlerp@dineshmills.com", "Mail Sending Failed due to HIERARCHY ID AND MODULE ID missed matched,<br><br><br> Module Id " + MODULE_ID + " and Hierarchy Id " + HIERARCHY_ID + " missed matched", "Mail Not Sent : " + DOC_NO, "");
            } catch (Exception e) {
                //System.out.println("Error Msg "+e.getMessage());
                //e.printStackTrace();
            }
            return "Module Id " + MODULE_ID + " and Hierarchy Id " + HIERARCHY_ID + " missed matched";
        }

        if (HIERARCHY_ID == 0) {
            try {
                SendMail("sdmlerp@dineshmills.com", "Mail Sending Failed due to HIERARCHY ID not set ", "Mail Not Sent : " + DOC_NO, "");
            } catch (Exception e) {
                //System.out.println("Error Msg "+e.getMessage());
                //e.printStackTrace();
            }
            return "Hierarchy Id not set, Please set Hierarchy Id";
            //send copy to SDMLERP 

        }

        if (MODULE_ID == 0) {
            try {
                SendMail("sdmlerp@dineshmills.com", "Mail Sending Failed due to MODULE ID not set ", "Mail Not Sent : " + DOC_NO, "");
            } catch (Exception e) {
                //System.out.println("Error Msg "+e.getMessage());
                //e.printStackTrace();
            }
            return "Module not set, Please set Module Id";
        }

        String DOC_TYPE = clsModules.getModuleName(EITLERPGLOBAL.gCompanyID, MODULE_ID);

        String pMessage = "";
        String pSubject = "Final Approved, " + DOC_TYPE + " , DOC NO : " + DOC_NO;
        String cc = "";

        if (MODULE_ID == 80 || MODULE_ID == 715 || MODULE_ID == 741 || MODULE_ID==602 || MODULE_ID==603 || MODULE_ID == 724 || MODULE_ID == 725 || MODULE_ID == 763  || MODULE_ID == 774) {
            cc = "feltsalesnotification@dineshmills.com";
        }
        
        if (MODULE_ID == 622)
        {
            cc = "feltwh@dineshmills.com";
        }
        

        if (PARTY_CODE.equals("")) {
            pMessage = "Document Name : " + DOC_TYPE + ",<br>Document No : " + DOC_NO + ",<br>Document Date : " + DOC_DATE + ",<br><br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br><br><br>";
        } else {
            pMessage = "Document Name : " + DOC_TYPE + ",<br>Document No : " + DOC_NO + ",<br>Document Date : " + DOC_DATE + ",<br>Party Code : " + PARTY_CODE + ",<br>Party Name : " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, PARTY_CODE) + "<br><br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br><br><br>";

            if (MODULE_ID == 604) {
                pMessage = "Diversion No : " + DOC_NO + ",<br>Diversion Date : " + DOC_DATE + ",<br><br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br><br><br>";

                //<br>Party Code : "+PARTY_CODE+",<br>Party Name : "+clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, PARTY_CODE)+"
                try {
                    Connection Conn1;
                    Statement stmt1;
                    ResultSet rsData1;

                    Conn1 = data.getConn();
                    stmt1 = Conn1.createStatement();
                    rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION where SD_ORDER_NO = '" + DOC_NO + "'");
                    rsData1.first();

                    String Existing_Piece_No = rsData1.getString("ORIGINAL_PIECE_NO");

                    pMessage = pMessage + "<br> :: <u>EXISTING PIECE DETAILS</u> ::<br><br>";
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO = '" + Existing_Piece_No + "'");
                        rsData.first();

                        pMessage = pMessage + "<br> PIECE NO : " + rsData.getString("PR_PIECE_NO");
                        pMessage = pMessage + "<br> PARTY CODE : " + rsData.getString("PR_PARTY_CODE") + " - " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData.getString("PR_PARTY_CODE"));
                        pMessage = pMessage + "<br> PRODUCT GROUP : " + rsData.getString("PR_GROUP");
                        //pMessage = pMessage + "<br> PRODUCT CODE : " + rsData.getString("PR_PRODUCT_CODE");
                        //pMessage = pMessage + "<br> ORDER LENGTH : "+rsData.getString("PR_LENGTH")+" ,     ORDER WIDTH :  "+rsData.getString("PR_WIDTH")+" ,    ORDER GSM : "+rsData.getString("PR_GSM");
                        //pMessage = pMessage + "<br> ACTUAL LENGTH : "+rsData.getString("PR_ACTUAL_LENGTH")+" ,     ACTUAL WIDTH : "+rsData.getString("PR_ACTUAL_WIDTH")+" ,     ACTUAL WEIGHT : "+rsData.getString("PR_ACTUAL_WEIGHT");
                        //pMessage = pMessage + "<br> BILL LENGTH : "+rsData.getString("PR_BILL_LENGTH")+" ,     BILL WIDTH : "+rsData.getString("PR_BILL_WIDTH")+" ,     BILL GSM : "+rsData.getString("PR_BILL_GSM")+" ,      BILL WEIGHT : "+rsData.getString("PR_BILL_WEIGHT");

                        pMessage = pMessage + "<br><br>";
                        pMessage = pMessage + "<table border='1'><tr><th></th><th>LENGTH</th><th align='center'> WIDTH </th><th align='center'> GSM </th><th align='center'>WEIGHT</th><th align='center'>PRODUCT CODE</th></tr>";
                        
                        pMessage = pMessage + "<tr>"
                                        + "<td align='center'>"
                                        + "ORDER"
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_LENGTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_WIDTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_GSM") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_THORITICAL_WEIGHT") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_PRODUCT_CODE") + ""
                                        + "</td>"
                                        + "</tr>";
                        
                        pMessage = pMessage + "<tr>"
                                        + "<td align='center'>"
                                        + "ACTUAL"
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_ACTUAL_LENGTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_ACTUAL_WIDTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_ACTUAL_WEIGHT") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "-"
                                        + "</td>"
                                        + "</tr>";
                        
                        pMessage = pMessage + "<tr>"
                                        + "<td align='center'>"
                                        + "BILL"
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_LENGTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_WIDTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_GSM") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_WEIGHT") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_PRODUCT_CODE") + ""
                                        + "</td>"
                                        + "</tr>"
                                        + "</table>";
                        
                        
                        
//                        pMessage = pMessage + "<br>-----------------------------------------------------------------------------";
//                        pMessage = pMessage + "<br>\t       |  LENGTH  |\tWIDTH  |\tGSM  |\tWEIGHT      |";
//                        pMessage = pMessage + "<br>-----------------------------------------------------------------------------";
//                        pMessage = pMessage + "<br>  ORDER\t|   " + rsData.getString("PR_LENGTH") + "  |\t" + rsData.getString("PR_WIDTH") + "  |\t" + rsData.getString("PR_GSM") + "  |\t" + rsData.getString("PR_THORITICAL_WEIGHT") + "\t|";
//                        pMessage = pMessage + "<br> ACTUAL\t|   " + rsData.getString("PR_ACTUAL_LENGTH") + "  |\t" + rsData.getString("PR_ACTUAL_WIDTH") + "  |\t      |\t" + rsData.getString("PR_ACTUAL_WEIGHT") + "\t|";
//                        pMessage = pMessage + "<br>   BILL\t|   " + rsData.getString("PR_BILL_LENGTH") + "  |\t" + rsData.getString("PR_BILL_WIDTH") + "  |\t" + rsData.getString("PR_BILL_GSM") + "  |\t" + rsData.getString("PR_BILL_WEIGHT") + "\t|";
//                        pMessage = pMessage + "<br>-----------------------------------------------------------------------------";

                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }

                    String New_Piece_No = rsData1.getString("D_PIECE_NO");
                    pMessage = pMessage + "<br><br><br>";
                    pMessage = pMessage + "<br> :: <u>ORDER(DIVERSION) PIECE DETAILS</u> ::";
                    pMessage = pMessage + "<br><br><br>";
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO = '" + New_Piece_No + "'");
                        rsData.first();

                        pMessage = pMessage + "<br> PIECE NO : " + rsData.getString("PR_PIECE_NO");
                        pMessage = pMessage + "<br> PARTY CODE : " + rsData.getString("PR_PARTY_CODE") + " - " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData.getString("PR_PARTY_CODE"));
                        pMessage = pMessage + "<br> PRODUCT GROUP : " + rsData.getString("PR_GROUP");
                        //pMessage = pMessage + "<br> PRODUCT CODE : " + rsData.getString("PR_PRODUCT_CODE");
                        //pMessage = pMessage + "<br> ORDER LENGTH : "+rsData.getString("PR_LENGTH")+" ,     ORDER WIDTH :  "+rsData.getString("PR_WIDTH")+" ,    ORDER GSM : "+rsData.getString("PR_GSM");
                        //pMessage = pMessage + "<br> ACTUAL LENGTH : "+rsData.getString("PR_ACTUAL_LENGTH")+" ,     ACTUAL WIDTH : "+rsData.getString("PR_ACTUAL_WIDTH")+" ,     ACTUAL WEIGHT : "+rsData.getString("PR_ACTUAL_WEIGHT");
                        //pMessage = pMessage + "<br> BILL LENGTH : "+rsData.getString("PR_BILL_LENGTH")+" ,     BILL WIDTH : "+rsData.getString("PR_BILL_WIDTH")+" ,     BILL GSM : "+rsData.getString("PR_BILL_GSM")+" ,      BILL WEIGHT : "+rsData.getString("PR_BILL_WEIGHT");

                        pMessage = pMessage + "<br><br>";
                        pMessage = pMessage + "<table border='1'><tr><th></th><th>LENGTH</th><th align='center'> WIDTH </th><th align='center'> GSM </th><th align='center'>WEIGHT</th><th align='center'>PRODUCT CODE</th></tr>";
                        
                        pMessage = pMessage + "<tr>"
                                        + "<td align='center'>"
                                        + "ORDER"
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_LENGTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_WIDTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_GSM") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_THORITICAL_WEIGHT") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_PRODUCT_CODE") + ""
                                        + "</td>"
                                        + "</tr>";
                        
                        pMessage = pMessage + "<tr>"
                                        + "<td align='center'>"
                                        + "ACTUAL"
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_ACTUAL_LENGTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_ACTUAL_WIDTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_ACTUAL_WEIGHT") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "-"
                                        + "</td>"
                                        + "</tr>";
                        
                        pMessage = pMessage + "<tr>"
                                        + "<td align='center'>"
                                        + "BILL"
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_LENGTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_WIDTH") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_GSM") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_WEIGHT") + ""
                                        + "</td>"
                                        + "<td  align='center'>"
                                        + "" + rsData.getString("PR_BILL_PRODUCT_CODE") + ""
                                        + "</td>"
                                        + "</tr>"
                                        + "</table>";
                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }

                    pMessage = pMessage + "<br><br><br>";
                    pMessage = pMessage + "<br> :: ACTION DETAILS ::";
                    pMessage = pMessage + "<br><br><br>";

                    boolean NOACTION = false;
                    if (("".equals(rsData1.getString("ACTION1")) || rsData1.getString("ACTION1") == null)
                            && ("".equals(rsData1.getString("ACTION2")) || rsData1.getString("ACTION2") == null)
                            && ("".equals(rsData1.getString("ACTION3")) || rsData1.getString("ACTION3") == null)
                            && ("".equals(rsData1.getString("ACTION4")) || rsData1.getString("ACTION4") == null)
                            && ("".equals(rsData1.getString("ACTION5")) || rsData1.getString("ACTION5") == null)
                            && ("".equals(rsData1.getString("ACTION6")) || rsData1.getString("ACTION6") == null)
                            && ("".equals(rsData1.getString("ACTION7")) || rsData1.getString("ACTION7") == null)
                            && ("".equals(rsData1.getString("ACTION8")) || rsData1.getString("ACTION8") == null)
                            && ("".equals(rsData1.getString("ACTION9")) || rsData1.getString("ACTION9") == null)
                            && ("".equals(rsData1.getString("ACTION10")) || rsData1.getString("ACTION10") == null)
                            && ("".equals(rsData1.getString("ACTION11")) || rsData1.getString("ACTION11") == null)
                            && ("".equals(rsData1.getString("ACTION12")) || rsData1.getString("ACTION12") == null)
                            && ("".equals(rsData1.getString("ACTION13")) || rsData1.getString("ACTION13") == null)
                            && ("".equals(rsData1.getString("ACTION14")) || rsData1.getString("ACTION14") == null)
                            && ("".equals(rsData1.getString("ACTION15")) || rsData1.getString("ACTION15") == null)
                            && ("".equals(rsData1.getString("ACTION16")) || rsData1.getString("ACTION16") == null)
                            && ("".equals(rsData1.getString("ACTION17")) || rsData1.getString("ACTION17") == null)) {
                        NOACTION = true;
                        pMessage = pMessage + "<u>NO ACTION</u><br><br>";
                    }

                    if (!NOACTION) {
                        if (!"".equals(rsData1.getString("ACTION1"))) {
                            pMessage = pMessage + "REDUCTION IN WIDTH : " + rsData1.getString("ACTION1") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION2"))) {
                            pMessage = pMessage + "INCREASE IN WIDTH : " + rsData1.getString("ACTION2") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION3"))) {
                            pMessage = pMessage + "REDUCTION IN LENGTH : " + rsData1.getString("ACTION3") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION4"))) {
                            pMessage = pMessage + "INCREASE IN GSM : " + rsData1.getString("ACTION4") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION5"))) {
                            pMessage = pMessage + "DECREASE IN GSM : " + rsData1.getString("ACTION5") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION6"))) {
                            pMessage = pMessage + "SEAMING REQUIRED : " + rsData1.getString("ACTION6") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION7"))) {
                            pMessage = pMessage + "TAGGING STYLE : " + rsData1.getString("ACTION7") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION8"))) {
                            pMessage = pMessage + "TAGGING PRODUCT CODE : " + rsData1.getString("ACTION8") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION9"))) {
                            pMessage = pMessage + "TAGGING LENGTH : " + rsData1.getString("ACTION9") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION10"))) {
                            pMessage = pMessage + "TAGGING WIDTH : " + rsData1.getString("ACTION10") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION11"))) {
                            pMessage = pMessage + "TAGGING GSM : " + rsData1.getString("ACTION11") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION12"))) {
                            pMessage = pMessage + "TAGGING CFM : " + rsData1.getString("ACTION12") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION13"))) {
                            pMessage = pMessage + "TAGGING THICKNESS : " + rsData1.getString("ACTION13") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION14"))) {
                            pMessage = pMessage + "TAGGING WEIGHT : " + rsData1.getString("ACTION14") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION15"))) {
                            pMessage = pMessage + "TRIM TO BE RETAINED BY PRODUCTION : " + rsData1.getString("ACTION15") + "<br>";
                        }

                        if (!"".equals(rsData1.getString("ACTION16"))) {
                            pMessage = pMessage + "TRIM TO BE RETAINED BY WH WITH FELT : " + rsData1.getString("ACTION16") + "<br>";
                        }

                    }
                    //
                    pMessage = pMessage + "<br><br>DIVERSION REMARK : " + rsData1.getString("D_REMARK") + " <br><br>";
                } catch (Exception e) {
                    System.out.println("Error on Mail: " + e.getMessage());
                }

            }

            
            
            
            if(MODULE_ID == 602)
            {
                // ORDER ENTRY START 
                
                pMessage = "Felt Order No : " + DOC_NO + ",<br>Order Date : " + DOC_DATE + ",<br><br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br>";

                //<br>Party Code : "+PARTY_CODE+",<br>Party Name : "+clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, PARTY_CODE)+"
                try {
                    Connection Conn1;
                    Statement stmt1;
                    ResultSet rsData1;

                    Conn1 = data.getConn();
                    stmt1 = Conn1.createStatement();
                    rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_HEADER H,PRODUCTION.FELT_SALES_ORDER_DETAIL D  " +
                                                " where H.S_ORDER_NO=D.S_ORDER_NO " +
                                                " AND H.S_ORDER_NO = '" + DOC_NO + "'");
                    rsData1.first();

                    pMessage = pMessage + "<br><br>";
                   
//                        Connection Conn;
//                        Statement stmt;
//                        ResultSet rsData;

//                        Conn = data.getConn();
//                        stmt = Conn.createStatement();
//                        rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO = '" + Existing_Piece_No + "'");
//                        rsData.first();

                        //pMessage = pMessage + "<br> PIECE NO : " + rsData.getString("PR_PIECE_NO");
                        pMessage = pMessage + "<br> PARTY CODE : " + rsData1.getString("PARTY_CODE") + " - " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData1.getString("PARTY_CODE"));
                        pMessage = pMessage + "<br> REFERENCE : " + rsData1.getString("REFERENCE");
                        pMessage = pMessage + "<br> REFERENCE DATE : " + rsData1.getString("REFERENCE_DATE");
                        pMessage = pMessage + "<br> REMARK : "+rsData1.getString("REMARK");
                        
                        pMessage = pMessage + "<br><br><u>ORDER DETAILS</u><br>";
                        pMessage = pMessage + "<table border='1'>"
                                + "<tr>"
                                + "<th align='center'> PIECE NO </th>"
                                + "<th align='center'> UPN </th>"
                                + "<th align='center'> MACHINE NO </th>"
                                + "<th align='center'> POSITION </th>"
                                + "<th align='center'> POSITION DESC </th>"
                                + "<th align='center'> PRODUCT CODE </th>"
                                + "<th align='center'> GROUP </th>"
                                + "<th align='center'> LENGTH </th>"
                                + "<th align='center'> WIDTH </th>"
                                + "<th align='center'> GSM </th>"
                                + "<th align='center'> WEIGHT </th>"
                                + "<th align='center'> SQMTR </th>"
                                + "<th align='center'> STYLE </th>"
                                + "<th align='center'> REQ MONTH </th>"
                                + "</tr>";
                        
                        rsData1.first();
                       
                        if (rsData1.getRow() > 0) {
                            while (!rsData1.isAfterLast()){ 
                                    pMessage = pMessage + ""
                                        + "<tr>"
                                        + "<td align='center'> "+rsData1.getString("PIECE_NO")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("UPN")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("MACHINE_NO")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("POSITION")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("POSITION_DESC")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("PRODUCT_CODE")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("S_GROUP")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("LENGTH")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("WIDTH")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("GSM")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("THORITICAL_WIDTH")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("SQ_MTR")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("STYLE")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("REQ_MONTH")+" </td>"
                                        + "</tr>";
                                    rsData1.next();
                            }
                        }
                        pMessage = pMessage + "</table>";

                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }
                // ORDER ENTRY END
            }
            
           if(MODULE_ID == 774)
            {
                // WIP Review START 
                
                pMessage = "WIP Piece Review No : " + DOC_NO + ",<br>Review Entry Date : " + DOC_DATE + ",<br><br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br>";

                try {
                    Connection Conn1;
                    Statement stmt1;
                    ResultSet rsData1;

                    Conn1 = data.getConn();
                    stmt1 = Conn1.createStatement();
                    rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_AMENDMENT_HEADER_WIP H, " +
                                                "PRODUCTION.FELT_SALES_PIECE_AMENDMENT_DETAIL_WIP D " +
                                                "WHERE H.PIECE_AMEND_NO=D.PIECE_AMEND_NO AND H.PIECE_AMEND_NO='" + DOC_NO + "'");
                    rsData1.first();

                    pMessage = pMessage + "<br><br>";
                   
//                        Connection Conn;
//                        Statement stmt;
//                        ResultSet rsData;

//                        Conn = data.getConn();
//                        stmt = Conn.createStatement();
//                        rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO = '" + Existing_Piece_No + "'");
//                        rsData.first();

                        //pMessage = pMessage + "<br> PIECE NO : " + rsData.getString("PR_PIECE_NO");
                        pMessage = pMessage + "<br> PARTY CODE : " + rsData1.getString("MM_PARTY_CODE") + " - " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData1.getString("MM_PARTY_CODE"));
                        pMessage = pMessage + "<br> MACHINE NO : " + rsData1.getString("MM_MACHINE_NO");
                        pMessage = pMessage + "<br> POSITION NO : " + rsData1.getString("MM_MACHINE_POSITION");
                        pMessage = pMessage + "<br> POSITION DESCRIPTION : " + data.getStringValueFromDB("SELECT POSITION_DESC FROM PRODUCTION.FELT_MACHINE_POSITION_MST WHERE POSITION_NO='" + rsData1.getString("MM_MACHINE_POSITION") + "'");
                        String Position_Des_No = data.getStringValueFromDB("SELECT POSITION_DESIGN_NO FROM PRODUCTION.FELT_MACHINE_POSITION_MST where POSITION_NO="+rsData1.getString("MM_MACHINE_POSITION"));
                        pMessage = pMessage + "<br> POSITION DESIGN NO : "+Position_Des_No;
                        pMessage = pMessage + "<br> UPN : "+rsData1.getString("MM_PARTY_CODE")+""+rsData1.getString("MM_MACHINE_NO")+""+Position_Des_No;
                        pMessage = pMessage + "<br> ENTRY REASON : "+rsData1.getString("ENTRY_REASON");
                        
                        pMessage = pMessage + "<br><br><u>PIECE DETAILS</u><br>";
                        pMessage = pMessage + "<table border='1'>"
                                + "<tr>"
                                + "<th align='center'> PIECE NO </th>"
                                + "<th align='center'> CHANGE POSIBLE</th>"
                                + "<th align='center'> OBSOLETE</th>"
                                + "<th align='center'> APPLY CHANGE </th>"
                                + "<th align='center'> PROD. REMARK </th>"
                                + "<th align='center'> STYLE </th>"
                                + "<th align='center'> UPDATE STYLE </th>"
                                + "<th align='center'> LENGTH </th>"
                                + "<th align='center'> UPDATED LENGTH </th>"
                                + "<th align='center'> WIDTH </th>"
                                + "<th align='center'> UPDATED WIDTH </th>"
                                + "<th align='center'> GSM </th>"
                                + "<th align='center'> UPDATED GSM </th>"
                                + "<th align='center'> PROD. CODE </th>"
                                + "<th align='center'> UPDATED PROD. CODE </th>"
                                + "<th align='center'> PIECE STAGE </th>"
                                + "<th align='center'> REMARKS </th>"
                                + "<th align='center'> EXPECTED DISPACH </th>"
                                + "</tr>";
                        
                        rsData1.first();
                       
                        if (rsData1.getRow() > 0) {
                            while (!rsData1.isAfterLast()){ 
                                String Change_Posibility = "";
                                String Obsolete = "";
                                String Apply_Chage = "";
                                if("1".equals(rsData1.getString("CHANGE_POSIBILITY")))
                                {
                                    Change_Posibility = "YES";
                                }
                                if("1".equals(rsData1.getString("DELINK")))
                                {
                                    Obsolete = "YES";
                                }
                                if("1".equals(rsData1.getString("ACTUAL_CHANGE")))
                                {
                                    Apply_Chage = "YES";
                                }
                                if(rsData1.getString("ENTRY_REASON").equals("MACHINE_MASTER_AMEND"))
                                {
                                    pMessage = pMessage + ""
                                        + "<tr>"
                                        + "<td align='center'> "+rsData1.getString("PIECE_NO")+" </td>"
                                        + "<td align='center'> "+Change_Posibility+" </td>"
                                        + "<td align='center'> "+Obsolete+" </td>"
                                        + "<td align='center'> "+Apply_Chage+" </td>"
                                        + "<td align='center'> "+rsData1.getString("PROD_REMARKS")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("STYLE")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("STYLE_UPDATED")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("LENGTH")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("LENGTH_UPDATED")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("WIDTH")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("WIDTH_UPDATED")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("GSM")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("GSM_UPDATED")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("PRODUCTCODE")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("PRODUCTCODE_UPDATED")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("PIECE_STAGE")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("REMARKS")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("EXPECTED_DISPATCH")+" </td>"
                                        + "</tr>";
                                }
                                else
                                {
                                    pMessage = pMessage + ""
                                        + "<tr>"
                                        + "<td align='center'> "+rsData1.getString("PIECE_NO")+" </td>"
                                        + "<td align='center'> "+Change_Posibility+" </td>"
                                        + "<td align='center'> "+Obsolete+" </td>"
                                        + "<td align='center'> "+Apply_Chage+" </td>"
                                        + "<td align='center'> "+rsData1.getString("PROD_REMARKS")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("STYLE")+" </td>"
                                        + "<td align='center'> - </td>"
                                        + "<td align='center'> "+rsData1.getString("LENGTH")+" </td>"
                                        + "<td align='center'> - </td>"
                                        + "<td align='center'> "+rsData1.getString("WIDTH")+" </td>"
                                        + "<td align='center'> - </td>"
                                        + "<td align='center'> "+rsData1.getString("GSM")+" </td>"
                                        + "<td align='center'> - </td>"
                                        + "<td align='center'> "+rsData1.getString("PRODUCTCODE")+" </td>"
                                        + "<td align='center'> - </td>"
                                        + "<td align='center'> "+rsData1.getString("PIECE_STAGE")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("REMARKS")+" </td>"
                                        + "<td align='center'> "+rsData1.getString("EXPECTED_DISPATCH")+" </td>"
                                        + "</tr>";
                                }
                                rsData1.next();
                            }
                        }
                        pMessage = pMessage + "</table>";

                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }
                // WIP Review END
            }
            
            if(MODULE_ID == 763)
            {
                // STOCK Review START 
                
                pMessage = "STOCK Piece Review No : " + DOC_NO + ",<br>Review Entry Date : " + DOC_DATE + ",<br><br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br>";

                try {
                    Connection Conn1;
                    Statement stmt1;
                    ResultSet rsData1;

                    Conn1 = data.getConn();
                    stmt1 = Conn1.createStatement();
                    rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_STOCK_HEADER H, " +
                                                "PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_STOCK_DETAIL D " +
                                                "WHERE H.PIECE_AMEND_STOCK_NO=D.PIECE_AMEND_STOCK_NO AND H.PIECE_AMEND_STOCK_NO='" + DOC_NO + "'");
                    rsData1.first();

                    pMessage = pMessage + "<br><br>";
                   
//                        Connection Conn;
//                        Statement stmt;
//                        ResultSet rsData;

//                        Conn = data.getConn();
//                        stmt = Conn.createStatement();
//                        rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO = '" + Existing_Piece_No + "'");
//                        rsData.first();

                        //pMessage = pMessage + "<br> PIECE NO : " + rsData.getString("PR_PIECE_NO");
                        pMessage = pMessage + "<br> PARTY CODE : " + rsData1.getString("MM_PARTY_CODE") + " - " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData1.getString("MM_PARTY_CODE"));
                        pMessage = pMessage + "<br> MACHINE NO : " + rsData1.getString("MM_MACHINE_NO");
                        pMessage = pMessage + "<br> POSITION NO : " + rsData1.getString("MM_MACHINE_POSITION");
                        pMessage = pMessage + "<br> POSITION DESCRIPTION : " + data.getStringValueFromDB("SELECT POSITION_DESC FROM PRODUCTION.FELT_MACHINE_POSITION_MST WHERE POSITION_NO='" + rsData1.getString("MM_MACHINE_POSITION") + "'");
                        String Position_Des_No = data.getStringValueFromDB("SELECT POSITION_DESIGN_NO FROM PRODUCTION.FELT_MACHINE_POSITION_MST where POSITION_NO="+rsData1.getString("MM_MACHINE_POSITION"));
                        pMessage = pMessage + "<br> POSITION DESIGN NO : "+Position_Des_No;
                        pMessage = pMessage + "<br> UPN : "+rsData1.getString("MM_PARTY_CODE")+""+rsData1.getString("MM_MACHINE_NO")+""+Position_Des_No;
                        
                        pMessage = pMessage + "<br><br><u>PIECE DETAILS</u><br>";
                        pMessage = pMessage + "<table border='1'>"
                                + "<tr>"
                                + "<th align='center'> PIECE NO </th>"
                                + "<th align='center'> OBSOLETE</th>"
                                + "<th align='center'> STYLE </th>"
                                + "<th align='center'> UPDATE STYLE </th>"
                                + "<th align='center'> LENGTH </th>"
                                + "<th align='center'> UPDATED LENGTH </th>"
                                + "<th align='center'> WIDTH </th>"
                                + "<th align='center'> UPDATED WIDTH </th>"
                                + "<th align='center'> GSM </th>"
                                + "<th align='center'> UPDATED GSM </th>"
                                + "<th align='center'> PIECE STAGE </th>"
                                + "<th align='center'> EXPECTED DISPACH </th>"
                                + "</tr>";
                        
                        rsData1.first();
                       
                        if (rsData1.getRow() > 0) {
                            while (!rsData1.isAfterLast()){ 
                               
                                String Obsolete = "";
                                
                                
                                if("1".equals(rsData1.getString("SELECTED")))
                                {
                                    Obsolete = "YES";
                                }
                                
                                pMessage = pMessage + ""
                                    + "<tr>"
                                    + "<td align='center'> "+rsData1.getString("PIECE_NO")+" </td>"
                                    + "<td align='center'> "+Obsolete+" </td>"
                                    + "<td align='center'> "+rsData1.getString("STYLE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("STYLE_UPDATED")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("LENGTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("LENGTH_UPDATED")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("WIDTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("WIDTH_UPDATED")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("GSM")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("GSM_UPDATED")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("PIECE_STAGE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("EXPECTED_DISPATCH")+" </td>"
                                    + "</tr>";
                                rsData1.next();
                            }
                        }
                        pMessage = pMessage + "</table>";

                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }
                // STOCK Review END
            }
            
            //Felt Finishing
            if(MODULE_ID == 603)
            {
                // Felt Finishing START 
                
                pMessage = "Felt Finishing Doc No : " + DOC_NO + ",<br> Doc Date : " + DOC_DATE + ",<br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br>";

                try {
                    Connection Conn1;
                    Statement stmt1;
                    ResultSet rsData1;

                    Conn1 = data.getConn();
                    stmt1 = Conn1.createStatement();
                    rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_PROD_DATA WHERE PROD_DEPT = 'FELT FINISHING' AND PROD_DOC_NO='" + DOC_NO + "'");
                    rsData1.first();

                    pMessage = pMessage + "<br><br>";
                   
//                        Connection Conn;
//                        Statement stmt;
//                        ResultSet rsData;

//                        Conn = data.getConn();
//                        stmt = Conn.createStatement();
//                        rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO = '" + Existing_Piece_No + "'");
//                        rsData.first();

                        //pMessage = pMessage + "<br> PIECE NO : " + rsData.getString("PR_PIECE_NO");
                        //pMessage = pMessage + "<br> PARTY CODE : " + rsData1.getString("MM_PARTY_CODE") + " - " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData1.getString("MM_PARTY_CODE"));
                        //pMessage = pMessage + "<br> MACHINE NO : " + rsData1.getString("MM_MACHINE_NO");
                        //pMessage = pMessage + "<br> POSITION NO : " + rsData1.getString("MM_MACHINE_POSITION");
                        //pMessage = pMessage + "<br> POSITION DESCRIPTION : " + data.getStringValueFromDB("SELECT POSITION_DESC FROM PRODUCTION.FELT_MACHINE_POSITION_MST WHERE POSITION_NO='" + rsData1.getString("MM_MACHINE_POSITION") + "'");
                        //String Position_Des_No = data.getStringValueFromDB("SELECT POSITION_DESIGN_NO FROM PRODUCTION.FELT_MACHINE_POSITION_MST where POSITION_NO="+rsData1.getString("MM_MACHINE_POSITION"));
                        //pMessage = pMessage + "<br> POSITION DESIGN NO : "+Position_Des_No;
                        pMessage = pMessage + "<br> FINISHING DATE : "+EITLERPGLOBAL.formatDate(rsData1.getString("FINAL_APPROVAL_DATE"));
                        
                        pMessage = pMessage + "<br><br><u>FINISHING PIECE DETAILS</u><br>";
                        pMessage = pMessage + "<table border='1'>"
                                + "<tr>"
                                + "<th align='center'> PIECE NO </th>"
                                + "<th align='center'> PARTY CODE</th>"
                                + "<th align='center'> PARTY NAME </th>"
                                + "<th align='center'> PRODUCT CODE </th>"
                                + "<th align='center'> ORDER NO </th>"
                                + "<th align='center'> ORDER DATE </th>"
                                + "<th align='center'> STYLE CODE </th>"
                                + "<th align='center'> GSM </th>"
                                + "<th align='center'> GROUP NAME </th>"
                                + "<th align='center'> ORDER LENGTH </th>"
                                + "<th align='center'> ORDER WIDTH </th>"
                                + "<th align='center'> FLOOR LENGTH </th>"
                                + "<th align='center'> FLOOR WIDTH </th>"
                                + "<th align='center'> FINISHED WEIGHT </th>"
                                + "<th align='center'> TAG WEIGHT </th>"
                                + "<th align='center'> BILL GSM </th>"
                                + "<th align='center'> BILL SQMTR </th>"
                                + "<th align='center'> BILL LENGTH </th>"
                                + "<th align='center'> BILL WIDTH </th>"
                                + "<th align='center'> BILL WEIGHT </th>"
                                + "</tr>";
                        
                        rsData1.first();
                       
                        if (rsData1.getRow() > 0) {
                            while (!rsData1.isAfterLast()){ 
                               ResultSet rsTmp1 = data.getConn().createStatement().executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + rsData1.getString("PROD_PIECE_NO") + "'");
                               rsTmp1.first();
                                
                                pMessage = pMessage + ""
                                    + "<tr>"
                                    + "<td align='center'> "+rsData1.getString("PROD_PIECE_NO")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("PROD_PARTY_CODE")+" </td>"
                                    + "<td align='center'> "+clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData1.getString("PROD_PARTY_CODE"))+" </td>"
                                    + "<td align='center'> "+rsTmp1.getString("PR_PRODUCT_CODE")+" </td>"
                                    + "<td align='center'> "+rsTmp1.getString("PR_DOC_NO")+" </td>"
                                    + "<td align='center'> "+EITLERPGLOBAL.formatDate(rsTmp1.getString("PR_ORDER_DATE"))+" </td>"
                                    + "<td align='center'> "+rsData1.getString("STYLE_CODE")+" </td>"
                                    + "<td align='center'> "+rsTmp1.getString("PR_GSM")+" </td>"
                                    + "<td align='center'> "+rsTmp1.getString("PR_GROUP")+" </td>"
                                    + "<td align='center'> "+rsTmp1.getString("PR_LENGTH")+" </td>"
                                    + "<td align='center'> "+rsTmp1.getString("PR_WIDTH")+" </td>"
                                    + "<td align='center'> "+rsTmp1.getString("PR_ACTUAL_LENGTH")+" </td>"
                                    + "<td align='center'> "+rsTmp1.getString("PR_ACTUAL_WIDTH")+" </td>"
                                    + "<td align='center'> "+rsTmp1.getString("PR_ACTUAL_WEIGHT")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("TAG_WEIGHT")+" </td>"
                                    + "<td align='center'> "+rsTmp1.getString("PR_BILL_GSM")+" </td>"
                                    + "<td align='center'> "+rsTmp1.getString("PR_BILL_SQMTR")+" </td>"
                                    + "<td align='center'> "+rsTmp1.getString("PR_BILL_LENGTH")+" </td>"
                                    + "<td align='center'> "+rsTmp1.getString("PR_BILL_WIDTH")+" </td>"
                                    + "<td align='center'> "+rsTmp1.getString("PR_BILL_WEIGHT")+" </td>"
                                    + "</tr>";
                                rsData1.next();
                            }
                        }
                        pMessage = pMessage + "</table>";

                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }
                // STOCK Review END
            }
            //End Felt Finishing
            
            //Felt Stock Tagging
            if(MODULE_ID == 622)
            {
                // Felt Stock Tagging
                
                pMessage = "Felt Stock Tagging Doc No : " + DOC_NO + ",<br> Doc Date : " + DOC_DATE + ",<br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br>";

                try {
                    Connection Conn1;
                    Statement stmt1;
                    ResultSet rsData1;

                    Conn1 = data.getConn();
                    stmt1 = Conn1.createStatement();
                    rsData1 = stmt1.executeQuery("SELECT * FROM  PRODUCTION.FELT_PIECE_AMEND where FELT_AMEND_ID='" + DOC_NO + "'");
                    rsData1.first();

                    pMessage = pMessage + "<br><br>";
                   
                        pMessage = pMessage + "<br> STOCK TAGGING DATE : "+EITLERPGLOBAL.formatDate(rsData1.getString("APPROVED_DATE"));
                        pMessage = pMessage + "<br> Tagging Reason : "+data.getStringValueFromDB("SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID = 'PIECE_AMEND' AND PARA_CODE="+rsData1.getString("FELT_AMEND_REASON"));
                        pMessage = pMessage + "<br><br><u>TAGGING PIECE DETAILS</u><br>";
                        pMessage = pMessage + "<table border='1'>"
                                + "<tr>"
                                + "<th align='center'> PIECE NO </th>"
                                + "<th align='center'> PARTY CODE</th>"
                                + "<th align='center'> PARTY NAME </th>"
                                + "<th align='center'> PRODUCT CODE </th>"
                                + "<th align='center'> AMEND LENGTH </th>"
                                + "<th align='center'> AMEND WIDTH </th>"
                                + "<th align='center'> AMEND SQMTR </th>"
                                + "<th align='center'> AMEND GSM </th>"
                                + "<th align='center'> AMEND WEIGHT </th>"                                
                                + "<th align='center'> AMEND GROUP NAME </th>"
                                + "<th align='center'> AMEND STYLE CODE </th>"                                
                                + "<th align='center'> AMEND REMARK </th>"                                
                                + "<th align='center'> AMEND BILL LENGTH </th>"
                                + "<th align='center'> AMEND BILL WIDTH </th>"
                                + "<th align='center'> AMEND BILL SQMTR </th>"
                                + "<th align='center'> AMEND BILL GSM </th>"
                                + "<th align='center'> AMEND BILL WEIGHT </th>"                                
                                + "<th align='center'> AMEND BILL PRODUCT CODE </th>"
                                + "<th align='center'> AMEND INCHARGE NAME </th>"                                
                                + "<th align='center'> MFG STATUS </th>"                           
                                + "</tr>";
                        
                        rsData1.first();
                       
                        if (rsData1.getRow() > 0) {
                            while (!rsData1.isAfterLast()){ 
                               //ResultSet rsTmp1 = data.getConn().createStatement().executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + rsData1.getString("PROD_PIECE_NO") + "'");
                               //rsTmp1.first();
                                
                                pMessage = pMessage + ""
                                    + "<tr>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_PIECE_NO")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_PARTY_CODE")+" </td>"
                                    + "<td align='center'> "+clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData1.getString("FELT_AMEND_PARTY_CODE"))+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_PRODUCT_CODE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_LENGTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_WIDTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_SQMTR")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_GSM")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_WEIGHT")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_GROUP")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_STYLE")+" </td>"    
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_REMARK")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_BILL_LENGTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_BILL_WIDTH")+" </td>"    
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_BILL_SQMTR")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_BILL_GSM")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_BILL_WEIGHT")+" </td>"        
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_BILL_PRODUCT_CODE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_INCHARGE_NAME")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("FELT_AMEND_MFG_STATUS")+" </td>" 
                                        
                                    + "</tr>";
                                rsData1.next();
                            }
                        }
                        pMessage = pMessage + "</table>";

                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }
                // STOCK Tagging END
            }
            //End Felt Stock Tagging
            
            //MACHINE MASTER
            if(MODULE_ID == 724)
            {
                // MACHINE MASTER START 
                
                pMessage = "Machine Master No : " + DOC_NO + ",<br>Entry Date : " + DOC_DATE + ",<br><br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br>";

                try {
                    Connection Conn1;
                    Statement stmt1;
                    ResultSet rsData1;

                    Conn1 = data.getConn();
                    stmt1 = Conn1.createStatement();
                    rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER H,PRODUCTION.FELT_MACHINE_MASTER_DETAIL D WHERE H.MM_DOC_NO=D.MM_DOC_NO and H.MM_DOC_NO='" + DOC_NO + "'");
                    rsData1.first();

                    pMessage = pMessage + "<br><br>";
                   
//                        Connection Conn;
//                        Statement stmt;
//                        ResultSet rsData;

//                        Conn = data.getConn();
//                        stmt = Conn.createStatement();
//                        rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO = '" + Existing_Piece_No + "'");
//                        rsData.first();

                        //pMessage = pMessage + "<br> PIECE NO : " + rsData.getString("PR_PIECE_NO");
                        pMessage = pMessage + "<br> PARTY CODE : " + rsData1.getString("MM_PARTY_CODE") + " - " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData1.getString("MM_PARTY_CODE"));
                        pMessage = pMessage + "<br> MACHINE NO : " + rsData1.getString("MM_MACHINE_NO");
                        pMessage = pMessage + "<br> MACHINE TYPE FORMING : " + rsData1.getString("MM_MACHINE_TYPE_FORMING");
                        pMessage = pMessage + "<br> PAPER GRADE : " + rsData1.getString("MM_PAPER_GRADE");
                        pMessage = pMessage + "<br> MACHINE SPEED RANGE : " + rsData1.getString("MM_MACHINE_SPEED_RANGE");
                        pMessage = pMessage + "<br> PAPER GSM RANGE : " + rsData1.getString("MM_PAPER_GSM_RANGE");
                        pMessage = pMessage + "<br> MACHINE TYPE PRESSING : " + rsData1.getString("MM_MACHINE_TYPE_PRESSING");
                        pMessage = pMessage + "<br> FURNISH : " + rsData1.getString("MM_FURNISH");
                        pMessage = pMessage + "<br> TYPE OF FILLER : " + rsData1.getString("MM_TYPE_OF_FILLER");
                        pMessage = pMessage + "<br> PAPER DECKLE AFTER WIRE : " + rsData1.getString("MM_PAPER_DECKLE_AFTER_WIRE");
                        pMessage = pMessage + "<br> PAPER DECKLE AT POPE REEL : " + rsData1.getString("MM_PAPER_DECKLE_AT_POPE_REEL");
                        pMessage = pMessage + "<br> DRYER SECTION : " + rsData1.getString("MM_DRYER_SECTION");
                        pMessage = pMessage + "<br> ZONE : " + rsData1.getString("MM_ZONE");
                        pMessage = pMessage + "<br> CAPACITY : " + rsData1.getString("MM_CAPACITY");
                        pMessage = pMessage + "<br> MACHINE STATUS : " + rsData1.getString("MM_MACHINE_STATUS");
                        pMessage = pMessage + "<br> DATE OF UPDATE : " + rsData1.getString("MM_DATE_OF_UPDATE");
                        pMessage = pMessage + "<br> TOTAL DRYER GROUP : " + rsData1.getString("MM_TOTAL_DRYER_GROUP  ");
                        pMessage = pMessage + "<br> HOOD TYPE : " + rsData1.getString("MM_HOOD_TYPE");
                        
                        pMessage = pMessage + "<br><br><u>POSITION DETAILS</u><br>";
                        pMessage = pMessage + "<table border='1'>"
                                + "<tr>"
                                + "<th align='center'> MM_MACHINE_POSITION </th>"
                                + "<th align='center'> MM_MACHINE_POSITION_DESC </th>"
                                + "<th align='center'> MM_COMBINATION_CODE </th>"
                                + "<th align='center'> MM_PRESS_TYPE </th>"
                                + "<th align='center'> MM_PRESS_ROLL_DAI_MM </th>"
                                + "<th align='center'> MM_PRESS_ROLL_FACE_TOTAL_MM </th>"
                                + "<th align='center'> MM_FELT_ROLL_WIDTH_MM </th>"
                                + "<th align='center'> MM_PRESS_LOAD </th>"
                                + "<th align='center'> MM_VACCUM_CAPACITY </th>"
                                + "<th align='center'> MM_UHLE_BOX </th>"
                                + "<th align='center'> MM_HP_SHOWER </th>"
                                + "<th align='center'> MM_FELT_LENGTH </th>"
                                + "<th align='center'> MM_FELT_WIDTH </th>"
                                + "<th align='center'> MM_FELT_GSM </th>"
                                + "<th align='center'> MM_FELT_WEIGHT </th>"
                                + "<th align='center'> MM_FELT_TYPE </th>"
                                + "<th align='center'> MM_FELT_STYLE </th>"
                                + "<th align='center'> MM_AVG_LIFE </th>"
                                + "<th align='center'> MM_AVG_PRODUCTION </th>"
                                + "<th align='center'> MM_FELT_CONSUMPTION </th>"
                                + "<th align='center'> MM_DINESH_SHARE </th>"
                                + "<th align='center'> MM_REMARK_DESIGN </th>"
                                + "<th align='center'> MM_REMARK_GENERAL </th>"
                                + "<th align='center'> MM_NO_DRYER_CYLINDER </th>"
                                + "<th align='center'> MM_DRIVE_TYPE </th>"
                                + "<th align='center'> MM_FABRIC_LENGTH </th>"
                                + "<th align='center'> MM_FABRIC_WIDTH </th>"
                                + "<th align='center'> MM_SIZE_M2 </th>"
                                + "<th align='center'> MM_SCREEN_TYPE </th>"
                                + "<th align='center'> MM_STYLE_DRY </th>"
                                + "<th align='center'> MM_REMARK_DRY </th>"
                                + "<th align='center'> MM_ITEM_CODE </th>"
                                + "<th align='center'> MM_GRUP </th>"
                                + "<th align='center'> MM_BASE_GSM </th>"
                                + "<th align='center'> MM_WEB_GSM </th>"
                                + "<th align='center'> MM_TOTAL_GSM </th>"
                                + "<th align='center'> MM_MACHINE_FLOOR </th>"
                                + "<th align='center'> MM_1ST_ROLL_MATERIAL </th>"
                                + "<th align='center'> MM_2ND_ROLL_MATERIAL </th>"
                                + "<th align='center'> MM_3RD_ROLL_MATERIAL </th>"
                                + "<th align='center'> MM_4TH_ROLL_MATERIAL </th>"
                                + "<th align='center'> MM_5TH_ROLL_MATERIAL </th>"
                                + "<th align='center'> MM_6TH_ROLL_MATERIAL </th>"
                                + "<th align='center'> MM_CATEGORY </th>"
                                + "<th align='center'> UC_CODE </th>"
                                + "<th align='center'> MM_POSITION_DESIGN_NO </th>"
                                + "<th align='center'> MM_UPN_NO </th>"
                                + "</tr>";
                        rsData1.first();
                       
                        if (rsData1.getRow() > 0) {
                            while (!rsData1.isAfterLast()){ 
                               
                                pMessage = pMessage + ""
                                    + "<tr>"
                                    + "<td align='center'> "+rsData1.getString("MM_MACHINE_POSITION")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_MACHINE_POSITION_DESC")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_COMBINATION_CODE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_PRESS_TYPE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_PRESS_ROLL_DAI_MM")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_PRESS_ROLL_FACE_TOTAL_MM")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_FELT_ROLL_WIDTH_MM")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_PRESS_LOAD")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_VACCUM_CAPACITY")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_UHLE_BOX")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_HP_SHOWER")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_FELT_LENGTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_FELT_WIDTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_FELT_GSM")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_FELT_WEIGHT")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_FELT_TYPE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_FELT_STYLE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_AVG_LIFE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_AVG_PRODUCTION")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_FELT_CONSUMPTION")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_DINESH_SHARE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_REMARK_DESIGN")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_REMARK_GENERAL")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_NO_DRYER_CYLINDER")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_DRIVE_TYPE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_FABRIC_LENGTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_FABRIC_WIDTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_SIZE_M2")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_SCREEN_TYPE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_STYLE_DRY")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_REMARK_DRY")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_ITEM_CODE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_GRUP")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_BASE_GSM")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_WEB_GSM")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_TOTAL_GSM")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_MACHINE_FLOOR")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_1ST_ROLL_MATERIAL")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_2ND_ROLL_MATERIAL")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_3RD_ROLL_MATERIAL")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_4TH_ROLL_MATERIAL")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_5TH_ROLL_MATERIAL")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_6TH_ROLL_MATERIAL")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_CATEGORY")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("UC_CODE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_POSITION_DESIGN_NO")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_UPN_NO")+" </td>"
                                    + "</tr>";
                                rsData1.next();
                            }
                        }
                        pMessage = pMessage + "</table>";
                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }
                // MACHINE MASTER END
            }
            //END MACHINE MASTER
            
            
            //MACHINE MASTER AMEND
            if(MODULE_ID == 725)
            {
                // MACHINE MASTER AMEND START 
                
                pMessage = "Machine Master Amend No : " + DOC_NO + ",<br>Entry Date : " + DOC_DATE + ",<br><br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br>";

                try {
                    Connection Conn1;
                    Statement stmt1;
                    ResultSet rsData1;

                    Conn1 = data.getConn();
                    stmt1 = Conn1.createStatement();
                    rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER H, PRODUCTION.FELT_MACHINE_MASTER_AMEND_DETAIL D WHERE H.MM_AMEND_NO=D.MM_AMEND_NO and H.MM_AMEND_NO='" + DOC_NO + "'");
                    rsData1.first();

                    pMessage = pMessage + "<br><br>";
                   
//                        Connection Conn;
//                        Statement stmt;
//                        ResultSet rsData;

//                        Conn = data.getConn();
//                        stmt = Conn.createStatement();
//                        rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO = '" + Existing_Piece_No + "'");
//                        rsData.first();

                        //pMessage = pMessage + "<br> PIECE NO : " + rsData.getString("PR_PIECE_NO");
                        pMessage = pMessage + "<br> PARTY CODE : " + rsData1.getString("MM_PARTY_CODE") + " - " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData1.getString("MM_PARTY_CODE"));
                        pMessage = pMessage + "<br> MACHINE NO : " + rsData1.getString("MM_MACHINE_NO");
                        pMessage = pMessage + "<br> MACHINE TYPE FORMING : " + rsData1.getString("MM_MACHINE_TYPE_FORMING");
                        pMessage = pMessage + "<br> PAPER GRADE : " + rsData1.getString("MM_PAPER_GRADE");
                        pMessage = pMessage + "<br> MACHINE SPEED RANGE : " + rsData1.getString("MM_MACHINE_SPEED_RANGE");
                        pMessage = pMessage + "<br> PAPER GSM RANGE : " + rsData1.getString("MM_PAPER_GSM_RANGE");
                        pMessage = pMessage + "<br> MACHINE TYPE PRESSING : " + rsData1.getString("MM_MACHINE_TYPE_PRESSING");
                        pMessage = pMessage + "<br> FURNISH : " + rsData1.getString("MM_FURNISH");
                        pMessage = pMessage + "<br> TYPE OF FILLER : " + rsData1.getString("MM_TYPE_OF_FILLER");
                        pMessage = pMessage + "<br> PAPER DECKLE AFTER WIRE : " + rsData1.getString("MM_PAPER_DECKLE_AFTER_WIRE");
                        pMessage = pMessage + "<br> PAPER DECKLE AT POPE REEL : " + rsData1.getString("MM_PAPER_DECKLE_AT_POPE_REEL");
                        pMessage = pMessage + "<br> DRYER SECTION : " + rsData1.getString("MM_DRYER_SECTION");
                        pMessage = pMessage + "<br> ZONE : " + rsData1.getString("MM_ZONE");
                        pMessage = pMessage + "<br> CAPACITY : " + rsData1.getString("MM_CAPACITY");
                        pMessage = pMessage + "<br> MACHINE STATUS : " + rsData1.getString("MM_MACHINE_STATUS");
                        pMessage = pMessage + "<br> DATE OF UPDATE : " + rsData1.getString("MM_DATE_OF_UPDATE");
                        pMessage = pMessage + "<br> TOTAL DRYER GROUP : " + rsData1.getString("MM_TOTAL_DRYER_GROUP  ");
                        pMessage = pMessage + "<br> HOOD TYPE : " + rsData1.getString("MM_HOOD_TYPE");
                        
                        pMessage = pMessage + "<br><br><u>POSITION DETAILS</u><br>";
                        pMessage = pMessage + "<table border='1'>"
                                + "<tr>"
                                + "<th align='center'> MM_MACHINE_POSITION </th>"
                                + "<th align='center'> MM_MACHINE_POSITION_DESC </th>"
                                + "<th align='center'> MM_COMBINATION_CODE </th>"
                                + "<th align='center'> MM_PRESS_TYPE </th>"
                                + "<th align='center'> MM_PRESS_ROLL_DAI_MM </th>"
                                + "<th align='center'> MM_PRESS_ROLL_FACE_TOTAL_MM </th>"
                                + "<th align='center'> MM_FELT_ROLL_WIDTH_MM </th>"
                                + "<th align='center'> MM_PRESS_LOAD </th>"
                                + "<th align='center'> MM_VACCUM_CAPACITY </th>"
                                + "<th align='center'> MM_UHLE_BOX </th>"
                                + "<th align='center'> MM_HP_SHOWER </th>"
                                + "<th align='center'> MM_FELT_LENGTH </th>"
                                + "<th align='center'> MM_FELT_WIDTH </th>"
                                + "<th align='center'> MM_FELT_GSM </th>"
                                + "<th align='center'> MM_FELT_WEIGHT </th>"
                                + "<th align='center'> MM_FELT_TYPE </th>"
                                + "<th align='center'> MM_FELT_STYLE </th>"
                                + "<th align='center'> MM_AVG_LIFE </th>"
                                + "<th align='center'> MM_AVG_PRODUCTION </th>"
                                + "<th align='center'> MM_FELT_CONSUMPTION </th>"
                                + "<th align='center'> MM_DINESH_SHARE </th>"
                                + "<th align='center'> MM_REMARK_DESIGN </th>"
                                + "<th align='center'> MM_REMARK_GENERAL </th>"
                                + "<th align='center'> MM_NO_DRYER_CYLINDER </th>"
                                + "<th align='center'> MM_DRIVE_TYPE </th>"
                                + "<th align='center'> MM_FABRIC_LENGTH </th>"
                                + "<th align='center'> MM_FABRIC_WIDTH </th>"
                                + "<th align='center'> MM_SIZE_M2 </th>"
                                + "<th align='center'> MM_SCREEN_TYPE </th>"
                                + "<th align='center'> MM_STYLE_DRY </th>"
                                + "<th align='center'> MM_REMARK_DRY </th>"
                                + "<th align='center'> MM_ITEM_CODE </th>"
                                + "<th align='center'> MM_GRUP </th>"
                                + "<th align='center'> MM_BASE_GSM </th>"
                                + "<th align='center'> MM_WEB_GSM </th>"
                                + "<th align='center'> MM_TOTAL_GSM </th>"
                                + "<th align='center'> MM_MACHINE_FLOOR </th>"
                                + "<th align='center'> MM_1ST_ROLL_MATERIAL </th>"
                                + "<th align='center'> MM_2ND_ROLL_MATERIAL </th>"
                                + "<th align='center'> MM_3RD_ROLL_MATERIAL </th>"
                                + "<th align='center'> MM_4TH_ROLL_MATERIAL </th>"
                                + "<th align='center'> MM_5TH_ROLL_MATERIAL </th>"
                                + "<th align='center'> MM_6TH_ROLL_MATERIAL </th>"
                                + "<th align='center'> MM_CATEGORY </th>"
                                + "<th align='center'> UC_CODE </th>"
                                + "<th align='center'> MM_POSITION_DESIGN_NO </th>"
                                + "<th align='center'> MM_UPN_NO </th>"
                                + "</tr>";
                        rsData1.first();
                       
                        if (rsData1.getRow() > 0) {
                            while (!rsData1.isAfterLast()){ 
                               
                                pMessage = pMessage + ""
                                    + "<tr>"
                                    + "<td align='center'> "+rsData1.getString("MM_MACHINE_POSITION")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_MACHINE_POSITION_DESC")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_COMBINATION_CODE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_PRESS_TYPE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_PRESS_ROLL_DAI_MM")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_PRESS_ROLL_FACE_TOTAL_MM")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_FELT_ROLL_WIDTH_MM")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_PRESS_LOAD")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_VACCUM_CAPACITY")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_UHLE_BOX")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_HP_SHOWER")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_FELT_LENGTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_FELT_WIDTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_FELT_GSM")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_FELT_WEIGHT")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_FELT_TYPE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_FELT_STYLE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_AVG_LIFE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_AVG_PRODUCTION")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_FELT_CONSUMPTION")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_DINESH_SHARE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_REMARK_DESIGN")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_REMARK_GENERAL")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_NO_DRYER_CYLINDER")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_DRIVE_TYPE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_FABRIC_LENGTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_FABRIC_WIDTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_SIZE_M2")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_SCREEN_TYPE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_STYLE_DRY")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_REMARK_DRY")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_ITEM_CODE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_GRUP")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_BASE_GSM")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_WEB_GSM")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_TOTAL_GSM")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_MACHINE_FLOOR")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_1ST_ROLL_MATERIAL")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_2ND_ROLL_MATERIAL")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_3RD_ROLL_MATERIAL")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_4TH_ROLL_MATERIAL")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_5TH_ROLL_MATERIAL")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_6TH_ROLL_MATERIAL")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_CATEGORY")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("UC_CODE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_POSITION_DESIGN_NO")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MM_UPN_NO")+" </td>"
                                    + "</tr>";
                                rsData1.next();
                            }
                        }
                        pMessage = pMessage + "</table>";
                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }
                // MACHINE MASTER AMEND END
            }
            //END MACHINE MASTER AMEND 
            
            
            //Felt Manual Budget
            if(MODULE_ID == 768)
            {
                // Felt Manual Budget START 
                
                pMessage = "Felt Manual Budget Doc No : " + DOC_NO + ",<br> Doc Date : " + DOC_DATE + ",<br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br>";

                try {
                    Connection Conn1;
                    Statement stmt1;
                    ResultSet rsData1;

                    Conn1 = data.getConn();
                    stmt1 = Conn1.createStatement();
                    rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_BUDGET_DETAIL WHERE DOC_NO='" + DOC_NO + "'");
                    rsData1.first();

                    pMessage = pMessage + "<br><br>";
                   
                        //pMessage = pMessage + "<br> MANUAL BUDGET DATE : "+EITLERPGLOBAL.formatDate(rsData1.getString("FINAL_APPROVAL_DATE"));
                        
                        pMessage = pMessage + "<br><br><u>MANUAL BUDGET DETAILS</u><br>";
                        pMessage = pMessage + "<table border='1'>"
                                + "<tr>"
                                + "<th align='center'> PARTY CODE</th>"
                                + "<th align='center'> PARTY NAME </th>"
                                + "<th align='center'> MACHINE NO</th>"
                                + "<th align='center'> POSITION </th>"
                                + "<th align='center'> POSITION DESCRIPTION</th>"
                                + "<th align='center'> STYLE </th>"
                                + "<th align='center'> PRESS LENGTH </th>"
                                + "<th align='center'> PRESS WIDTH </th>"
                                + "<th align='center'> PRESS GSM </th>"
                                + "<th align='center'> PRESS WEIGHT </th>"
                                + "<th align='center'> PRESS SQMTR </th>"
                                + "<th align='center'> DRY LENGTH </th>"
                                + "<th align='center'> DRY WIDTH </th>"
                                + "<th align='center'> DRY SQMTR </th>"
                                + "<th align='center'> DRY WEIGHT </th>"
                                + "<th align='center'> QUALITY NO </th>"
                                + "<th align='center'> GROUP NAME </th>"
                                + "<th align='center'> SELLING PRICE </th>"
                                + "<th align='center'> SP DISCOUNT </th>"
                                + "<th align='center'> WIP </th>"
                                + "<th align='center'> STOCK </th>"
                                + "<th align='center'> Q1 </th>"
                                + "<th align='center'> Q1 KG </th>"
                                + "<th align='center'> Q1 SQMTR </th>"
                                + "<th align='center'> Q2 </th>"
                                + "<th align='center'> Q2 KG </th>"
                                + "<th align='center'> Q2 SQMTR </th>"
                                + "<th align='center'> Q3 </th>"
                                + "<th align='center'> Q3 KG </th>"
                                + "<th align='center'> Q3 SQMTR </th>"
                                + "<th align='center'> Q4 </th>"
                                + "<th align='center'> Q4 KG </th>"
                                + "<th align='center'> Q4 SQMTR </th>"
                                + "<th align='center'> TOTAL </th>"
                                + "<th align='center'> TOTAL KG </th>"
                                + "<th align='center'> TOTAL SQMTR </th>"
                                + "<th align='center'> GST PER </th>"
                                + "<th align='center'> GROSS AMT </th>"
                                + "<th align='center'> DISCOUNT </th>"
                                + "<th align='center'> NET AMT </th>"
                                + "<th align='center'> PARTY STATUS </th>"
                                + "<th align='center'> SYSTEM STATUS </th>"
                                + "<th align='center'> SALES REMARK </th>"
                                + "<th align='center'> PP REMARK </th>"
                                + "</tr>";
                        
                        rsData1.first();
                       
                        if (rsData1.getRow() > 0) {
                            while (!rsData1.isAfterLast()){ 
                                
                                pMessage = pMessage + ""
                                    + "<tr>"
                                    + "<td align='center'> "+rsData1.getString("PARTY_CODE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("PARTY_NAME")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("MACHINE_NO")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("POSITION_NO")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("POSITION_DESC")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("STYLE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("PRESS_LENGTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("PRESS_WIDTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("PRESS_GSM")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("PRESS_WEIGHT")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("PRESS_SQMTR")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("DRY_LENGTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("DRY_WIDTH")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("DRY_SQMTR")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("DRY_WEIGHT")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("QUALITY_NO")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("GROUP_NAME")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("SELLING_PRICE")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("SPL_DISCOUNT")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("WIP")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("STOCK")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("Q1")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("Q1KG")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("Q1SQMTR")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("Q2")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("Q2KG")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("Q2SQMTR")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("Q3")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("Q3KG")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("Q3SQMTR")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("Q4")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("Q4KG")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("Q4SQMTR")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("TOTAL")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("TOTAL_KG")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("TOTAL_SQMTR")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("GST_PER")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("GROSS_AMOUNT")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("DISCOUNT_AMOUNT")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("NET_AMOUNT")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("PARTY_STATUS")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("SYSTEM_STATUS")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("REMARKS")+" </td>"
                                    + "<td align='center'> "+rsData1.getString("PP_REMARKS")+" </td>"
                                    + "</tr>";
                                rsData1.next();
                            }
                        }
                        pMessage = pMessage + "</table>";

                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }
                // Felt Manual Budget END
            }
            //End Felt Manual Budget
            
            
//GAURANG            
            if (MODULE_ID == 608) {
//                pMessage = "Closure Doc No : " + DOC_NO + ",<br>Closure Doc Date : " + DOC_DATE + ",<br><br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "<br><br><br>";
//                pMessage = "Closure Doc No : " + DOC_NO + ",<br>Closure Doc Date : " + DOC_DATE + "<br><br><br>";
                
//                pMessage = "<html><body>Closure Doc No : " + DOC_NO + ",<br>Closure Doc Date : " + DOC_DATE + "<br><br><br>";
                pMessage = "Closure Doc No : " + DOC_NO + ",<br>Closure Doc Date : " + DOC_DATE + "<br><br><br>";

                //<br>Party Code : "+PARTY_CODE+",<br>Party Name : "+clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, PARTY_CODE)+"
                try {
                    Connection Conn1;
                    Statement stmt1;
                    ResultSet rsData1;

                    Conn1 = data.getConn();
                    stmt1 = Conn1.createStatement();
                    rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_PARTY_MACHINE_POSITION_CLOSURE WHERE DOC_NO = '" + DOC_NO + "' ");
                    rsData1.first();

                    String partyCd = rsData1.getString("PARTY_CODE");
                    String partyName = rsData1.getString("PARTY_NAME");
                    String machineNo = rsData1.getString("MACHINE_NO");
                    String positionNo = rsData1.getString("POSITION_NO");
                    
//                    pMessage = pMessage + "<br>PARTY CODE : "+partyCd+"    NAME : "+partyName;
                    pMessage = pMessage + "PARTY CODE : "+partyCd+"    NAME : "+partyName+"<br>";
                    if (DOC_NO.startsWith("PMC")){
//                        pMessage = pMessage + "<br>MACHINE NO : "+machineNo;
                        pMessage = pMessage + "MACHINE NO : "+machineNo+"<br>";
                    }
                    if (DOC_NO.startsWith("MPC")){
//                        pMessage = pMessage + "<br>POSITION NO : "+positionNo;
                        pMessage = pMessage + "POSITION NO : "+positionNo+"<br>";
                    }
//                    pMessage = pMessage + "<br>HAS NOW BEEN CLOSED.";
//                    
//                    pMessage = pMessage + "<br>PIECES OF ABOVE MENTION ARE READY FOR DIVERSION.";
//                    
//                    pMessage = pMessage + "<br>LIST OF DIVERSION READY FELTS<br><br>";
                    
                    pMessage = pMessage + "HAS NOW BEEN CLOSED.<br>PIECES OF ABOVE MENTION ARE READY FOR DIVERSION.<br>LIST OF DIVERSION READY FELTS<br><br>";
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        String prSQL = "";
                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        //rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE = '" + partyCd + "' AND PR_PIECE_STAGE IN ('WEAVING','MENDING','NEEDLING','FINISHING','IN STOCK','BSR') ");
                        if (DOC_NO.startsWith("FPC")) {
                            prSQL = "SELECT PR_PIECE_STAGE,COALESCE(PR_PIECE_NO,'') AS PR_PIECE_NO,COALESCE(PR_MACHINE_NO,'') AS PR_MACHINE_NO,COALESCE(PR_POSITION_NO,'') AS PR_POSITION_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE = '" + partyCd + "' AND PR_PIECE_STAGE IN ('WEAVING','MENDING','NEEDLING','FINISHING','IN STOCK','BSR')  ORDER BY PR_PIECE_STAGE,PR_PIECE_NO ";
                        }
                        
                        if (DOC_NO.startsWith("PMC")) {
                            prSQL = "SELECT PR_PIECE_STAGE,COALESCE(PR_PIECE_NO,'') AS PR_PIECE_NO,COALESCE(PR_MACHINE_NO,'') AS PR_MACHINE_NO,COALESCE(PR_POSITION_NO,'') AS PR_POSITION_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE = '" + partyCd + "' AND PR_MACHINE_NO = " + machineNo + " AND PR_PIECE_STAGE IN ('WEAVING','MENDING','NEEDLING','FINISHING','IN STOCK','BSR')  ORDER BY PR_PIECE_STAGE,PR_PIECE_NO ";
                        }
                        
                        if (DOC_NO.startsWith("MPC")) {
                            prSQL = "SELECT PR_PIECE_STAGE,COALESCE(PR_PIECE_NO,'') AS PR_PIECE_NO,COALESCE(PR_MACHINE_NO,'') AS PR_MACHINE_NO,COALESCE(PR_POSITION_NO,'') AS PR_POSITION_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE = '" + partyCd + "' AND PR_MACHINE_NO = " + machineNo + " AND PR_POSITION_NO = " + positionNo + " AND PR_PIECE_STAGE IN ('WEAVING','MENDING','NEEDLING','FINISHING','IN STOCK','BSR')  ORDER BY PR_PIECE_STAGE,PR_PIECE_NO ";
                        }    
                        
                        System.out.println("prSQL : " + prSQL);

                        rsData = stmt.executeQuery(prSQL);
                        rsData.first();
                       
                        if (rsData.getRow() > 0) {
//                            pMessage = pMessage + "<br>-----------------------------------------------------------------------------";
//                            pMessage = pMessage + "<br>\t|  STAGE  |\tPIECE  |\tMACHINE  |\tPOSITION      |";
//                            pMessage = pMessage + "<br>-----------------------------------------------------------------------------";
//                            pMessage = pMessage + "<table border='1'><tr><th>STAGE</th><th>PIECE NO</th><th align='center'> MACHINE NO </th><th align='center'> POSITION </th></tr>";
                            pMessage = pMessage + "<table border='1'>"
                                + "<tr>"
                                + "<th align='center'> STAGE </th>"
                                + "<th align='center'> PIECE NO </th>"
                                + "<th align='center'> MACHINE NO </th>"
                                + "<th align='center'> POSITION </th>"
                                + "</tr>";
                            
                            
                            while (!rsData.isAfterLast()) {
                                pMessage = pMessage + ""
                                    + "<tr>"
                                    + "<td align='center'> "+rsData.getString("PR_PIECE_STAGE")+" </td>"
                                    + "<td align='center'> "+rsData.getString("PR_PIECE_NO")+" </td>"
                                    + "<td align='center'> "+rsData.getString("PR_MACHINE_NO")+" </td>"
                                    + "<td align='center'> "+rsData.getString("PR_POSITION_NO")+" </td>"
                                    + "</tr>";
//                                pMessage = pMessage + "<tr>"
//                                        + "<td>"
//                                        + "<p>" + rsData.getString("PR_PIECE_STAGE") + "</p>"
//                                        + "</td>"
//                                        + "<td>"
//                                        + "<p>" + rsData.getString("PR_PIECE_NO") + "</p>"
//                                        + "</td>"
//                                        + "<td>"
//                                        + "<p>" + rsData.getString("PR_MACHINE_NO") + "</p>"
//                                        + "</td>"
//                                        + "<td>"
//                                        + "<p>" + rsData.getString("PR_POSITION_NO") + "</p>"
//                                        + "</td>"
//                                        + "</tr>";

//                                pMessage = pMessage + "<br>\t|   " + rsData.getString("PR_PIECE_STAGE") + "  |\t" + rsData.getString("PR_PIECE_NO") + "  |\t" + rsData.getString("PR_MACHINE_NO") + "  |\t" + rsData.getString("PR_POSITION_NO") + "\t|";
//                                pMessage = pMessage + "<br>-----------------------------------------------------------------------------";

                                rsData.next();
                            }
                               pMessage = pMessage + "</table>";           
                        } else {
                            pMessage = pMessage + "<br>No Pieces found in Piece Register.<br>";
                        }
                        
//                        pMessage = pMessage + "</table></body></html>";
     
                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }

                } catch (Exception e) {
                    System.out.println("Error on Mail: " + e.getMessage());
                }

            }

//GAURANG            
        }

        if(Resend_By!=0)
        {
            pMessage = pMessage + "<br><br> Document has been Resend by - " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, Resend_By) + "<br>";
        }
        
        if (SEND_TO_ALL) {
            HashMap hmSendToList;

            String recievers = "sdmlerp@dineshmills.com";
                
            if (MODULE_ID == 608) {
                pMessage = pMessage + "<br><br> : Email Send to : <br><br>";
                hmSendToList = clsHierarchy.getUserList(EITLERPGLOBAL.gCompanyID, HIERARCHY_ID, USER_ID, true);
                for (int i = 1; i <= hmSendToList.size(); i++) {
                    clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));
                    int U_ID = ObjUser.getAttribute("USER_ID").getInt();

                    String to = clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, U_ID);

                    System.out.println("USERID : " + U_ID + ", send_to : " + to);
                    if (!to.equals("")) {
                        recievers = recievers + "," + to;
                        pMessage = pMessage + ObjUser.getAttribute("USER_NAME").getString()+ "<br>" ;
                    }
                }
                
//                recievers = "sdmlerp@dineshmills.com,rishineekhra@dineshmills.com,gaurang@dineshmills.com,ashutosh@dineshmills.com";
                recievers = recievers + "yrpatel@dineshmills.com,amitkanti@dineshmills.com,feltpp@dineshmills.com,brdflteng@dineshmills.com,abtewary@dineshmills.com,felts@dineshmills.com,brdfltfin@dineshmills.com,feltwh@dineshmills.com";
                
//                pMessage = pMessage + "rishineekhra@dineshmills.com<br>";
//                pMessage = pMessage + "gaurang@dineshmills.com<br>";
//                pMessage = pMessage + "ashutosh@dineshmills.com<br>"; 
                pMessage = pMessage + "yrpatel@dineshmills.com,amitkanti@dineshmills.com<br>";
                pMessage = pMessage + "feltpp@dineshmills.com<br>";
                pMessage = pMessage + "brdflteng@dineshmills.com<br>"; 
                pMessage = pMessage + "abtewary@dineshmills.com<br>";
                pMessage = pMessage + "felts@dineshmills.com<br>";
                pMessage = pMessage + "brdfltfin@dineshmills.com<br>"; 
                pMessage = pMessage + "feltwh@dineshmills.com<br>"; 
                
                pMessage = pMessage + "<br><br><br>**** This is an auto-generated email, please do not reply ****<br><br>";
//                pMessage = pMessage + "</table></body></html>";
                
            } else {
                pMessage = pMessage + "<br><br> : Email Send to : <br>";
                hmSendToList = clsHierarchy.getUserList(EITLERPGLOBAL.gCompanyID, HIERARCHY_ID, USER_ID, true);
                for (int i = 1; i <= hmSendToList.size(); i++) {
                    clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));
                    int U_ID = ObjUser.getAttribute("USER_ID").getInt();

                    String to = clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, U_ID);

                    System.out.println("USERID : " + U_ID + ", send_to : " + to);
                    if (!to.equals("")) {
                        recievers = recievers + "," + to;
                        pMessage = pMessage + "<br>" + ObjUser.getAttribute("USER_NAME").getString();
                    }
                }
            }
            
            if (MODULE_ID == 80 || MODULE_ID == 715 || MODULE_ID==602 || MODULE_ID==603 || MODULE_ID == 768)  {
                //recievers = recievers + "," + "feltsalesnotification@dineshmills.com";
                pMessage = pMessage + "<br>" + "feltsalesnotification@dineshmills.com";
            }
            if(MODULE_ID == 602 || MODULE_ID == 604 || MODULE_ID == 774 || MODULE_ID == 763)
            {
                recievers = recievers + "," + "brdfltdesign@dineshmills.com";
                pMessage = pMessage + "<br>" + "brdfltdesign@dineshmills.com";
            }
            if (MODULE_ID == 604) {
                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 57))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 57);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 57);
                }
                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 59))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 59);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 59);
                }
                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 285))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 285);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 285);
                }
                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 320))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 320);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 320);
                }
                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 19))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 19);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 19);
                }

                recievers = recievers + ",excise@dineshmills.com";
                pMessage = pMessage + "<br>excise@dineshmills.com";
            }
            if (MODULE_ID == 712) {
                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 60))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 60);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 60);
                }

                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 306))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 306);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 306);
                }

                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 57))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 57);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 57);
                }

                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 56))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 56);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 56);
                }

                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 53))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 53);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 53);
                }
            }
            if (MODULE_ID == 763 || MODULE_ID == 774) {
                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 243))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 243);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 243);
                }
                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 28))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 28);
                    pMessage = pMessage + "<br>" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 28);
                }
            }
                
            if (MODULE_ID!=608) {
                pMessage = pMessage + "<br><br><br><br>**** This is an auto-generated email, please do not reply ****";
            }
            
            try {

                System.out.println("Recivers : " + recievers);
                System.out.println("pSubject : " + pSubject);
                System.out.println("pMessage : " + pMessage);

                SendMail(recievers, pMessage, pSubject, cc);
            } catch (Exception e) {
                System.out.println("Error Msg " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            String to = clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, USER_ID);

            System.out.println("SINGLE USER ID : " + USER_ID + ", send_to : " + to);
            if (!to.equals("")) {
                to = to + ",sdmlerp@dineshmills.com";
                try {

                    SendMail(to, pMessage, pSubject, cc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("USER : " + USER_ID + " External Mail Not Found");
                try {
                    SendMail("sdmlerp@dineshmills.com", "Mail Sending Failed due to External Mail not found, USER ID  " + USER_ID, "Mail Not Sent : " + DOC_NO, "");
                } catch (Exception e) {
                        //System.out.println("Error Msg "+e.getMessage());
                    //e.printStackTrace();
                }
            }
        }

        return "Mail Sending Done....!";
    }
    
}
