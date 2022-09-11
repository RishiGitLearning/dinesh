/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales;

import static EITLERP.FeltSales.common.JavaMail.SendMail;
import EITLERP.data;

/**
 *
 * @author Dharmendra
 */
public class DailyMailNotification {

    public static String sendNotificationMail(String pSubject, String pMessage, String recievers, String pcc) {
        try {
            System.out.println("Recivers : " + recievers);
            System.out.println("pSubject : " + pSubject);
            System.out.println("pMessage : " + pMessage);
            System.out.println("pCC      : " + pcc);

            SendMail(recievers, pMessage, pSubject, pcc);
        } catch (Exception e) {
            System.out.println("Error Msg " + e.getMessage());
            e.printStackTrace();
        }
        return "Mail Sending Done....!";
    }

}
