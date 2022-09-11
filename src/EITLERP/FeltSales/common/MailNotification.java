/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.common;

import static EITLERP.FeltSales.common.JavaMail.SendMail;
import EITLERP.data;

/**
 *
 * @author Dharmendra
 */
public class MailNotification {

    public static String sendNotificationMail(int MODULE_ID, String pSubject, String pMessage, String recievers, String pcc, int HIERARCHY_ID) {
        String responce = data.getStringValueFromDB("SELECT HIERARCHY_ID FROM DINESHMILLS.D_COM_HIERARCHY where HIERARCHY_ID=" + HIERARCHY_ID + " AND MODULE_ID=" + MODULE_ID + "");
        if (responce.equals("")) {
            try {
                SendMail("sdmlerp@dineshmills.com", "Mail Sending Failed due to HIERARCHY ID AND MODULE ID missed matched,<br><br><br> Module Id " + MODULE_ID + " and Hierarchy Id " + HIERARCHY_ID + " missed matched", "", "");
            } catch (Exception e) {
                System.out.println("Error Msg " + e.getMessage());
                e.printStackTrace();
            }
            return "Module Id " + MODULE_ID + " and Hierarchy Id " + HIERARCHY_ID + " missed matched";
        }
        if (HIERARCHY_ID == 0) {
            try {
                SendMail("sdmlerp@dineshmills.com", "Mail Sending Failed due to HIERARCHY ID not set ", "", "");
            } catch (Exception e) {
                System.out.println("Error Msg " + e.getMessage());
                e.printStackTrace();
            }
            return "Hierarchy Id not set, Please set Hierarchy Id";
        }
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
