/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales;

import EITLERP.EITLERPGLOBAL;
import static EITLERP.FeltSales.CurrentScheduleSalesPlanNotification.sendNotificationMailWithAttachement;
import EITLERP.FeltSales.Reports.clsExcelExporter;
import java.io.File;
import sdml.felt.commonUI.data;

/**
 *
 * @author root
 */
public class clsWarpingPreparationPlanning {
    static String pMessage = "";
    static String pSubject = "Reminder - Loom wise Beam Fall, Warping Praparation Planning";

    public static void main(String[] args) {
        pMessage = "<html>  ";

        pMessage = pMessage + "Dear Sanjeev Amin/Manoj Rana,<br><br> Today ("+data.getStringValueFromDB("SELECT DAYNAME('"+EITLERPGLOBAL.getCurrentDateDB()+"')")+", "+EITLERPGLOBAL.getCurrentDateDB()+") is due date to prepare loom wise beam fall - warping praparation planning entry form.<br><br><br> To open form <br> ERP --> Felt Sales System --> Production Transaction --> Production Entry --> Loomwise Beam Fall Warp Praparation Planning.</font></html>";

        System.out.println("MESAGE : " + pMessage);
        String recievers = "brdfltweave1@dineshmills.com";
        String CC="aditya@dineshmills.com,abtewary@dineshmills.com,yrpatel@dineshmills.com,amitkanti@dineshmills.com,raghavendra@dineshmills.com,sdmlerp@dineshmills.com";
        String responce = DailyMailNotification.sendNotificationMail(pSubject, pMessage, recievers, CC);
        
    }
    
}
