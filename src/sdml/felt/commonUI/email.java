/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdml.felt.commonUI;

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
import javax.swing.JOptionPane;

/**
 *
 * @author root
 */
public class email {

    String smtpHost = "184.106.240.198";
    String From = "rshop@dineshmills.com";//change accordingly  
    final String user = "rshop@dineshmills.com";//change accordingly  
    final String password = "rshop@123#";//change accordingly  

    String Subject = "", MailText = "", File = "", CC = "", BCC = "",TO="";
    

   

    public void sendmail() {
        try {
            
            

                // Get system properties
                Properties props = System.getProperties();

                // Setup mail server
                props.put("mail.smtp.host", smtpHost);
                props.put("mail.smtp.auth", "true");

                // Get session
                Session Objsession = Session.getInstance(props);

                // Define message
                MimeMessage message = new MimeMessage(Objsession);
                message.setFrom(new InternetAddress(From));

                message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(TO));

                if (!CC.trim().equals("")) {
                    message.addRecipients(Message.RecipientType.CC,InternetAddress.parse(CC));
                }

                if (!BCC.trim().equals("")) {
                    message.addRecipients(Message.RecipientType.BCC,InternetAddress.parse(BCC));
                }

                message.setSubject(Subject);

                // Create the message part
                BodyPart messageBodyPart = new MimeBodyPart();

                // Fill the message
                messageBodyPart.setText(MailText);

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
                Transport tr = Objsession.getTransport("smtp");
                tr.connect(smtpHost, user, password);
                tr.sendMessage(message, message.getAllRecipients());
                tr.close();
          
            //===============================================================================//

            System.out.println("Sent message successfully....");
            JOptionPane.showMessageDialog(null, "Sent message successfully....");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error..");
        }
    }

}