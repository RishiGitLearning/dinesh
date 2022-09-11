/*
 * JMail.java
 *
 * Created on July 7, 2004, 4:26 PM
 */

package EITLERP.JavaMail;

/**
 *
 * @author  root
 */

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;


public class JMail {
    
    
    
    /** Creates a new instance of JMail */
    public JMail() {
    }
    
    public static void SendMail(String smtpHost,String from,String to,String pMessage,String pSubject,String cc)
    throws Exception {
        
       
        // Get system properties
        Properties props = System.getProperties();
        
        // Setup mail server
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.auth", "true");
        
        // Get session
        Session session =
        Session.getInstance(props);
        
        // Define message
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO,
        new InternetAddress(to));
       
        
        if(!cc.trim().equals("")) {
            message.addRecipient(Message.RecipientType.TO,
            new InternetAddress(cc));
        }
        
        message.setSubject(pSubject);
        message.setText(pMessage);
        
        // Send message
        Transport tr=session.getTransport("smtp");
        tr.connect(smtpHost,"sdmlerp@dineshmills.com","password");
        tr.sendMessage(message, message.getAllRecipients());
        tr.close();
        
    }
    
    public static void SendMail(String smtpHost,String from,String to,String pMessage,String pSubject,String cc,String pFileName)
    throws Exception {
        

        
        // Get system properties
        Properties props = System.getProperties();
        
        // Setup mail server
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.auth", "true");
        
        // Get session
        Session session =
        Session.getInstance(props);
        
        // Define message
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO,
        new InternetAddress(to));
        
        
        if(!cc.trim().equals("")) {
            message.addRecipient(Message.RecipientType.TO,
            new InternetAddress(cc));
        }
        
            
        message.setSubject(pSubject);
        
        
        // Create the message part
        BodyPart messageBodyPart = new MimeBodyPart();
        
        // Fill the message
        messageBodyPart.setText(pMessage);
        
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        
        
        // Part two is attachment
        messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(pFileName);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(pFileName);
        multipart.addBodyPart(messageBodyPart);
        
        // Put parts in message
        message.setContent(multipart);
        
        
        // Send message
        Transport tr=session.getTransport("smtp");
        tr.connect(smtpHost,"eitl@dineshmills.com","123456");
        tr.sendMessage(message, message.getAllRecipients());
        tr.close();
    }
    
}



