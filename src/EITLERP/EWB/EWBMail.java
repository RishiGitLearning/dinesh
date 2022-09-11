/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.EWB;

import EITLERP.EITLERPGLOBAL;
import EITLERP.clsHierarchy;
import EITLERP.clsModules;
import EITLERP.clsSales_Party;
import EITLERP.clsUser;
import EITLERP.data;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;

/**
 *
 * @author root
 */
public class EWBMail {

    public static void SendSuitingEWBMail(String pFromDate, String pToDate, String pLocPath)
    throws Exception {
        
        String from = "sdmlerp@dineshmills.com";
        String Password  = "K.0-H%dmc20ks.00";  //Sdml@390020
        
        String pMessage = "";
        String pSubject = "";
        String to = "";
        String bcc = "";
        
        //to = "gaurang@dineshmills.com";
        to = "atulshah@dineshmills.com,rkgarg@dineshmills.com,shitole@dineshmills.com,akhil@dineshmills.com,dkjoshi@dineshmills.com,anuj@dineshmills.com,tusharjadhav@dineshmills.com,nbdas@dineshmills.com";
        pSubject = "STG EWB Data for Date : " + pFromDate + " To : " + pToDate;
        bcc = "rishineekhra@dineshmills.com,sdmlerp@dineshmills.com";

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

        String[] str_bcc = bcc.split(",");
        for (String str1 : str_bcc) {
            if (!str1.trim().equals("")) {
                message.addRecipient(Message.RecipientType.BCC, new InternetAddress(str1));
            }
        }

        message.setSubject(pSubject);
        //message.setText(pMessage);

        MimeBodyPart mbp1 = new MimeBodyPart();
        mbp1.setText(pMessage);
        MimeBodyPart mbp2 = new MimeBodyPart();
        File temp = null;
        File temp1 = null;

        //temp = new File("/root/Desktop/FLT_EWAY_29012018.xls");
        temp = new File(pLocPath);
        boolean ch = temp.createNewFile();
        
//        FileDataSource fds = new FileDataSource("/root/Desktop/FLT_EWAY_29012018.xls");
        FileDataSource fds = new FileDataSource(pLocPath);
        mbp2.setDataHandler(new DataHandler(fds));
        mbp2.setFileName(fds.getName());
        Multipart mp = new MimeMultipart();
        mp.addBodyPart(mbp1);
        mp.addBodyPart(mbp2);
        message.setContent(mp);
        message.saveChanges();
        // Set the Date: header
        message.setSentDate(new java.util.Date());

        // Send message
        Transport tr = session.getTransport("smtp");
        tr.connect(SMTPHostIp, from, Password);
        tr.sendMessage(message, message.getAllRecipients());
        tr.close();


    }
    
    public static void SendFeltEWBMail(String pFromDate, String pToDate, String pLocPath)
    throws Exception {
        
        String from = "sdmlerp@dineshmills.com";
        String Password  = "K.0-H%dmc20ks.00";   //Sdml@390020
        
        String pMessage = "";
        String pSubject = "";
        String to = "";
        String bcc = "";
        
        //to = "gaurang@dineshmills.com";
        to = "felts@dineshmills.com";
        pSubject = "FLT EWB Data for Date : " + pFromDate + " To : " + pToDate;
        bcc = "rishineekhra@dineshmills.com,sdmlerp@dineshmills.com";

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

        String[] str_bcc = bcc.split(",");
        for (String str1 : str_bcc) {
            if (!str1.trim().equals("")) {
                message.addRecipient(Message.RecipientType.BCC, new InternetAddress(str1));
            }
        }

        message.setSubject(pSubject);
        //message.setText(pMessage);

        MimeBodyPart mbp1 = new MimeBodyPart();
        mbp1.setText(pMessage);
        MimeBodyPart mbp2 = new MimeBodyPart();
        File temp = null;
        File temp1 = null;

        //temp = new File("/root/Desktop/FLT_EWAY_29012018.xls");
        temp = new File(pLocPath);
        boolean ch = temp.createNewFile();
        
//        FileDataSource fds = new FileDataSource("/root/Desktop/FLT_EWAY_29012018.xls");
        FileDataSource fds = new FileDataSource(pLocPath);
        mbp2.setDataHandler(new DataHandler(fds));
        mbp2.setFileName(fds.getName());
        Multipart mp = new MimeMultipart();
        mp.addBodyPart(mbp1);
        mp.addBodyPart(mbp2);
        message.setContent(mp);
        message.saveChanges();
        // Set the Date: header
        message.setSentDate(new java.util.Date());

        // Send message
        Transport tr = session.getTransport("smtp");
        tr.connect(SMTPHostIp, from, Password);
        tr.sendMessage(message, message.getAllRecipients());
        tr.close();


    }

    public static void SendFilterFabricEWBMail(String pFromDate, String pToDate, String pLocPath)
    throws Exception {
        
        String from = "sdmlerp@dineshmills.com";
        String Password  = "K.0-H%dmc20ks.00"; //Sdml@390020
        
        String pMessage = "";
        String pSubject = "";
        String to = "";
        String bcc = "";
        
        //to = "gaurang@dineshmills.com";
        to = "sudhirpurohit@dineshmills.com,exportstg@dineshmills.com";
        pSubject = "FF EWB Data for Date : " + pFromDate + " To : " + pToDate;
        bcc = "rishineekhra@dineshmills.com,sdmlerp@dineshmills.com";

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

        String[] str_bcc = bcc.split(",");
        for (String str1 : str_bcc) {
            if (!str1.trim().equals("")) {
                message.addRecipient(Message.RecipientType.BCC, new InternetAddress(str1));            }
        }

        message.setSubject(pSubject);
        //message.setText(pMessage);

        MimeBodyPart mbp1 = new MimeBodyPart();
        mbp1.setText(pMessage);
        MimeBodyPart mbp2 = new MimeBodyPart();
        File temp = null;
        File temp1 = null;

        //temp = new File("/root/Desktop/FLT_EWAY_29012018.xls");
        temp = new File(pLocPath);
        boolean ch = temp.createNewFile();
        
//        FileDataSource fds = new FileDataSource("/root/Desktop/FLT_EWAY_29012018.xls");
        FileDataSource fds = new FileDataSource(pLocPath);
        mbp2.setDataHandler(new DataHandler(fds));
        mbp2.setFileName(fds.getName());
        Multipart mp = new MimeMultipart();
        mp.addBodyPart(mbp1);
        mp.addBodyPart(mbp2);
        message.setContent(mp);
        message.saveChanges();
        // Set the Date: header
        message.setSentDate(new java.util.Date());

        // Send message
        Transport tr = session.getTransport("smtp");
        tr.connect(SMTPHostIp, from, Password);
        tr.sendMessage(message, message.getAllRecipients());
        tr.close();


    }
}
