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
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
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
public class JavaMailNew {

    public static void SendMail(String to, String pMessage, String pSubject, String cc, String File)
            throws Exception {

        String from = "sdmlerp@dineshmills.com";
        String Password = "K.0-H%dmc20ks.00";//Sdml@390020
        //String Password = "Drp@18789";

        // Get system properties
        Properties props = System.getProperties();

        String SMTPHostIp = "34.206.245.89";

        // Setup mail server
        System.out.println("smtpHost  " + SMTPHostIp);
        props.put("mail.smtp.host", SMTPHostIp);
        props.put("SMTPAuth", "true"); 
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
                message.addRecipient(Message.RecipientType.BCC, new InternetAddress(str1));
            }
        }

        message.setSubject(pSubject);
        //message.setText(pMessage);

        // Create the message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(pMessage, "text/html");
        

        // Fill the message
        //messageBodyPart.setText(pMessage);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        // Part two is attachment                
        messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(File);

        messageBodyPart.setDataHandler(new javax.activation.DataHandler(source));
        messageBodyPart.setFileName(File);
        multipart.addBodyPart(messageBodyPart);

        // Put parts in message
        message.setContent(multipart);
        // Send message
        Transport tr = session.getTransport("smtp");
        tr.connect(SMTPHostIp, from, Password);
        tr.sendMessage(message, message.getAllRecipients());
        tr.close();

    }

    public static String sendFinalApprovalMail(int MODULE_ID, String DOC_NO, String DOC_DATE, String PARTY_CODE, int USER_ID, int HIERARCHY_ID, boolean SEND_TO_ALL) {
        if (USER_ID == 0) {
            try {
                SendMail("sdmlerp@dineshmills.com", "Mail Sending Failed due to USER ID not set", "Mail Not Sent : " + DOC_NO, "","");
            } catch (Exception e) {
                //System.out.println("Error Msg "+e.getMessage());
                //e.printStackTrace();
            }
            return "User Id not set, please set User Id";
        }

        String responce = data.getStringValueFromDB("SELECT HIERARCHY_ID FROM DINESHMILLS.D_COM_HIERARCHY where HIERARCHY_ID=" + HIERARCHY_ID + " AND MODULE_ID=" + MODULE_ID + "");
        if (responce.equals("")) {

            try {
                SendMail("sdmlerp@dineshmills.com", "Mail Sending Failed due to HIERARCHY ID AND MODULE ID missed matched,\n\n\n Module Id " + MODULE_ID + " and Hierarchy Id " + HIERARCHY_ID + " missed matched", "Mail Not Sent : " + DOC_NO, "","");
            } catch (Exception e) {
                //System.out.println("Error Msg "+e.getMessage());
                //e.printStackTrace();
            }
            return "Module Id " + MODULE_ID + " and Hierarchy Id " + HIERARCHY_ID + " missed matched";
        }

        if (HIERARCHY_ID == 0) {
            try {
                SendMail("sdmlerp@dineshmills.com", "Mail Sending Failed due to HIERARCHY ID not set ", "Mail Not Sent : " + DOC_NO, "","");
            } catch (Exception e) {
                //System.out.println("Error Msg "+e.getMessage());
                //e.printStackTrace();
            }
            return "Hierarchy Id not set, Please set Hierarchy Id";
            //send copy to SDMLERP 

        }

        if (MODULE_ID == 0) {
            try {
                SendMail("sdmlerp@dineshmills.com", "Mail Sending Failed due to MODULE ID not set ", "Mail Not Sent : " + DOC_NO, "","");
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

        if (PARTY_CODE.equals("")) {
            pMessage = "Document Name : " + DOC_TYPE + ",\nDocument No : " + DOC_NO + ",\nDocument Date : " + DOC_DATE + ",\n\n\n Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "\n\n\n";
        } else {
            pMessage = "Document Name : " + DOC_TYPE + ",\nDocument No : " + DOC_NO + ",\nDocument Date : " + DOC_DATE + ",\nParty Code : " + PARTY_CODE + ",\nParty Name : " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, PARTY_CODE) + "\n\n\n Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "\n\n\n";

            if (MODULE_ID == 604) {
                pMessage = "Diversion No : " + DOC_NO + ",\nDiversion Date : " + DOC_DATE + ",\n\n\n Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, USER_ID) + "\n\n\n";

                //\nParty Code : "+PARTY_CODE+",\nParty Name : "+clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, PARTY_CODE)+"
                try {
                    Connection Conn1;
                    Statement stmt1;
                    ResultSet rsData1;

                    Conn1 = data.getConn();
                    stmt1 = Conn1.createStatement();
                    rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION where SD_ORDER_NO = '" + DOC_NO + "'");
                    rsData1.first();

                    String Existing_Piece_No = rsData1.getString("ORIGINAL_PIECE_NO");

                    pMessage = pMessage + "\n :: EXISTING PIECE DETAILS ::";
                    pMessage = pMessage + "\n    ======================\n\n";
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO = '" + Existing_Piece_No + "'");
                        rsData.first();

                        pMessage = pMessage + "\n PIECE NO : " + rsData.getString("PR_PIECE_NO");
                        pMessage = pMessage + "\n PARTY CODE : " + rsData.getString("PR_PARTY_CODE") + " - " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData.getString("PR_PARTY_CODE"));
                        pMessage = pMessage + "\n PRODUCT GROUP : " + rsData.getString("PR_GROUP");
                        pMessage = pMessage + "\n PRODUCT CODE : " + rsData.getString("PR_PRODUCT_CODE");
                        //pMessage = pMessage + "\n ORDER LENGTH : "+rsData.getString("PR_LENGTH")+" ,     ORDER WIDTH :  "+rsData.getString("PR_WIDTH")+" ,    ORDER GSM : "+rsData.getString("PR_GSM");
                        //pMessage = pMessage + "\n ACTUAL LENGTH : "+rsData.getString("PR_ACTUAL_LENGTH")+" ,     ACTUAL WIDTH : "+rsData.getString("PR_ACTUAL_WIDTH")+" ,     ACTUAL WEIGHT : "+rsData.getString("PR_ACTUAL_WEIGHT");
                        //pMessage = pMessage + "\n BILL LENGTH : "+rsData.getString("PR_BILL_LENGTH")+" ,     BILL WIDTH : "+rsData.getString("PR_BILL_WIDTH")+" ,     BILL GSM : "+rsData.getString("PR_BILL_GSM")+" ,      BILL WEIGHT : "+rsData.getString("PR_BILL_WEIGHT");

                        pMessage = pMessage + "\n-----------------------------------------------------------------------------";
                        pMessage = pMessage + "\n\t       |  LENGTH  |\tWIDTH  |\tGSM  |\tWEIGHT      |";
                        pMessage = pMessage + "\n-----------------------------------------------------------------------------";
                        pMessage = pMessage + "\n  ORDER\t|   " + rsData.getString("PR_LENGTH") + "  |\t" + rsData.getString("PR_WIDTH") + "  |\t" + rsData.getString("PR_GSM") + "  |\t" + rsData.getString("PR_THORITICAL_WEIGHT") + "\t|";
                        pMessage = pMessage + "\n ACTUAL\t|   " + rsData.getString("PR_ACTUAL_LENGTH") + "  |\t" + rsData.getString("PR_ACTUAL_WIDTH") + "  |\t      |\t" + rsData.getString("PR_ACTUAL_WEIGHT") + "\t|";
                        pMessage = pMessage + "\n   BILL\t|   " + rsData.getString("PR_BILL_LENGTH") + "  |\t" + rsData.getString("PR_BILL_WIDTH") + "  |\t" + rsData.getString("PR_BILL_GSM") + "  |\t" + rsData.getString("PR_BILL_WEIGHT") + "\t|";
                        pMessage = pMessage + "\n-----------------------------------------------------------------------------";

                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }

                    String New_Piece_No = rsData1.getString("D_PIECE_NO");
                    pMessage = pMessage + "\n\n\n";
                    pMessage = pMessage + "\n :: ORDER(DIVERSION) PIECE DETAILS ::";
                    pMessage = pMessage + "\n    ==============================\n\n";
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO = '" + New_Piece_No + "'");
                        rsData.first();

                        pMessage = pMessage + "\n PIECE NO : " + rsData.getString("PR_PIECE_NO");
                        pMessage = pMessage + "\n PARTY CODE : " + rsData.getString("PR_PARTY_CODE") + " - " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData.getString("PR_PARTY_CODE"));
                        pMessage = pMessage + "\n PRODUCT GROUP : " + rsData.getString("PR_GROUP");
                        pMessage = pMessage + "\n PRODUCT CODE : " + rsData.getString("PR_PRODUCT_CODE");
                        //pMessage = pMessage + "\n ORDER LENGTH : "+rsData.getString("PR_LENGTH")+" ,     ORDER WIDTH :  "+rsData.getString("PR_WIDTH")+" ,    ORDER GSM : "+rsData.getString("PR_GSM");
                        //pMessage = pMessage + "\n ACTUAL LENGTH : "+rsData.getString("PR_ACTUAL_LENGTH")+" ,     ACTUAL WIDTH : "+rsData.getString("PR_ACTUAL_WIDTH")+" ,     ACTUAL WEIGHT : "+rsData.getString("PR_ACTUAL_WEIGHT");
                        //pMessage = pMessage + "\n BILL LENGTH : "+rsData.getString("PR_BILL_LENGTH")+" ,     BILL WIDTH : "+rsData.getString("PR_BILL_WIDTH")+" ,     BILL GSM : "+rsData.getString("PR_BILL_GSM")+" ,      BILL WEIGHT : "+rsData.getString("PR_BILL_WEIGHT");

                        pMessage = pMessage + "\n-----------------------------------------------------------------------------";
                        pMessage = pMessage + "\n\t       |  LENGTH  |\tWIDTH  |\tGSM  |\tWEIGHT      |";
                        pMessage = pMessage + "\n-----------------------------------------------------------------------------";
                        pMessage = pMessage + "\n  ORDER\t|   " + rsData.getString("PR_LENGTH") + "  |\t" + rsData.getString("PR_WIDTH") + "  |\t" + rsData.getString("PR_GSM") + "  |\t" + rsData.getString("PR_THORITICAL_WEIGHT") + "\t|";
                        pMessage = pMessage + "\n ACTUAL\t|   " + rsData.getString("PR_ACTUAL_LENGTH") + "  |\t" + rsData.getString("PR_ACTUAL_WIDTH") + "  |\t      |\t" + rsData.getString("PR_ACTUAL_WEIGHT") + "\t|";
                        pMessage = pMessage + "\n   BILL\t|   " + rsData.getString("PR_BILL_LENGTH") + "  |\t" + rsData.getString("PR_BILL_WIDTH") + "  |\t" + rsData.getString("PR_BILL_GSM") + "  |\t" + rsData.getString("PR_BILL_WEIGHT") + "\t|";
                        pMessage = pMessage + "\n-----------------------------------------------------------------------------";

                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }

                    pMessage = pMessage + "\n\n\n";
                    pMessage = pMessage + "\n :: ACTION DETAILS ::";
                    pMessage = pMessage + "\n    ==============\n\n";

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
                        pMessage = pMessage + "NO ACTION\n\n";
                    }

                    if (!NOACTION) {
                        if (!"".equals(rsData1.getString("ACTION1"))) {
                            pMessage = pMessage + "REDUCTION IN WIDTH : " + rsData1.getString("ACTION1") + "\n";
                        }

                        if (!"".equals(rsData1.getString("ACTION2"))) {
                            pMessage = pMessage + "INCREASE IN WIDTH : " + rsData1.getString("ACTION2") + "\n";
                        }

                        if (!"".equals(rsData1.getString("ACTION3"))) {
                            pMessage = pMessage + "REDUCTION IN LENGTH : " + rsData1.getString("ACTION3") + "\n";
                        }

                        if (!"".equals(rsData1.getString("ACTION4"))) {
                            pMessage = pMessage + "INCREASE IN GSM : " + rsData1.getString("ACTION4") + "\n";
                        }

                        if (!"".equals(rsData1.getString("ACTION5"))) {
                            pMessage = pMessage + "DECREASE IN GSM : " + rsData1.getString("ACTION5") + "\n";
                        }

                        if (!"".equals(rsData1.getString("ACTION6"))) {
                            pMessage = pMessage + "SEAMING REQUIRED : " + rsData1.getString("ACTION6") + "\n";
                        }

                        if (!"".equals(rsData1.getString("ACTION7"))) {
                            pMessage = pMessage + "TAGGING STYLE : " + rsData1.getString("ACTION7") + "\n";
                        }

                        if (!"".equals(rsData1.getString("ACTION8"))) {
                            pMessage = pMessage + "TAGGING PRODUCT CODE : " + rsData1.getString("ACTION8") + "\n";
                        }

                        if (!"".equals(rsData1.getString("ACTION9"))) {
                            pMessage = pMessage + "TAGGING LENGTH : " + rsData1.getString("ACTION9") + "\n";
                        }

                        if (!"".equals(rsData1.getString("ACTION10"))) {
                            pMessage = pMessage + "TAGGING WIDTH : " + rsData1.getString("ACTION10") + "\n";
                        }

                        if (!"".equals(rsData1.getString("ACTION11"))) {
                            pMessage = pMessage + "TAGGING GSM : " + rsData1.getString("ACTION11") + "\n";
                        }

                        if (!"".equals(rsData1.getString("ACTION12"))) {
                            pMessage = pMessage + "TAGGING CFM : " + rsData1.getString("ACTION12") + "\n";
                        }

                        if (!"".equals(rsData1.getString("ACTION13"))) {
                            pMessage = pMessage + "TAGGING THICKNESS : " + rsData1.getString("ACTION13") + "\n";
                        }

                        if (!"".equals(rsData1.getString("ACTION14"))) {
                            pMessage = pMessage + "TAGGING WEIGHT : " + rsData1.getString("ACTION14") + "\n";
                        }

                        if (!"".equals(rsData1.getString("ACTION15"))) {
                            pMessage = pMessage + "TRIM TO BE RETAINED BY PRODUCTION : " + rsData1.getString("ACTION15") + "\n";
                        }

                        if (!"".equals(rsData1.getString("ACTION16"))) {
                            pMessage = pMessage + "TRIM TO BE RETAINED BY WH WITH FELT : " + rsData1.getString("ACTION16") + "\n";
                        }

                    }
                    //
                    pMessage = pMessage + "\n\nDIVERSION REMARK : " + rsData1.getString("D_REMARK") + " \n\n";
                } catch (Exception e) {
                    System.out.println("Error on Mail: " + e.getMessage());
                }

            }
        }

        if (SEND_TO_ALL) {
            HashMap hmSendToList;

            String recievers = "sdmlerp@dineshmills.com";
            pMessage = pMessage + "\n\n\n : Email Send to : \n";
            hmSendToList = clsHierarchy.getUserList(EITLERPGLOBAL.gCompanyID, HIERARCHY_ID, USER_ID, true);
            for (int i = 1; i <= hmSendToList.size(); i++) {
                clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));
                int U_ID = ObjUser.getAttribute("USER_ID").getInt();

                String to = clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, U_ID);

                System.out.println("USERID : " + U_ID + ", send_to : " + to);
                if (!to.equals("")) {
                    recievers = recievers + "," + to;
                    pMessage = pMessage + "\n" + ObjUser.getAttribute("USER_NAME").getString();
                }
            }

            if (MODULE_ID == 80 || MODULE_ID == 715) {
                //recievers = recievers + "," + "feltsalesnotification@dineshmills.com";
                pMessage = pMessage + "\n" + "feltsalesnotification@dineshmills.com";
            }
            if (MODULE_ID == 604) {
                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 57))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 57);
                    pMessage = pMessage + "\n" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 57);
                }
                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 59))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 59);
                    pMessage = pMessage + "\n" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 59);
                }
                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 285))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 285);
                    pMessage = pMessage + "\n" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 285);
                }
                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 320))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 320);
                    pMessage = pMessage + "\n" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 320);
                }
                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 19))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 19);
                    pMessage = pMessage + "\n" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 19);
                }

                recievers = recievers + ",excise@dineshmills.com";
                pMessage = pMessage + "\nexcise@dineshmills.com";
            }
            if (MODULE_ID == 712) {
                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 60))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 60);
                    pMessage = pMessage + "\n" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 60);
                }

                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 306))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 306);
                    pMessage = pMessage + "\n" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 306);
                }

                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 57))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 57);
                    pMessage = pMessage + "\n" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 57);
                }

                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 56))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 56);
                    pMessage = pMessage + "\n" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 56);
                }

                if (!"".equals(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 53))) {
                    recievers = recievers + "," + clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, 53);
                    pMessage = pMessage + "\n" + clsUser.getUserName(EITLERPGLOBAL.gCompanyID, 53);
                }
            }
            pMessage = pMessage + "\n\n\n\n**** This is an auto-generated email, please do not reply ****";
            try {

                System.out.println("Recivers : " + recievers);
                System.out.println("pSubject : " + pSubject);
                System.out.println("pMessage : " + pMessage);

                SendMail(recievers, pMessage, pSubject, cc,"");
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

                    SendMail(to, pMessage, pSubject, cc,"");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("USER : " + USER_ID + " External Mail Not Found");
                try {
                    SendMail("sdmlerp@dineshmills.com", "Mail Sending Failed due to External Mail not found, USER ID  " + USER_ID, "Mail Not Sent : " + DOC_NO, "","");
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

                    pMessage = pMessage + "\n\n" + DOC_TYPE + ", DOC NO : " + DOC_NO + ", DOC DATE : " + DOC_DATE + ",\nREMARKS : " + REMARKS;
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
}
