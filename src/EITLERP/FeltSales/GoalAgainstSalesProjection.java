/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales;

import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.Reports.clsExcelExporter;
import EITLERP.FeltSales.common.JavaMail;
import EITLERP.data;
import java.io.File;

/**
 *
 * @author Dharmendra
 */
public class GoalAgainstSalesProjection {

    /**
     * @param args the command line arguments
     */
    private static clsExcelExporter exprt = new clsExcelExporter();

    public static void main(String[] args) {
        // TODO code application logic here

        if (!data.IsRecordExist("SELECT * FROM PRODUCTION.A_CHECK_REPORT_SEND WHERE SEND_DESC='GOAL_AGAINST_SALES_PROJECTION' AND SEND_MONTH=MONTH(CURDATE()) AND SEND_YEAR=YEAR(CURDATE())")
                && !data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE APPROVED=0") //&& EITLERPGLOBAL.getCurrentDay() < 8
                ) {

            String mntFor = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(NOW(),'%b'),'-',YEAR(NOW())) FROM DUAL");

            String pMessage = "Respected Sir, <br><br> Goal Against Sales Projection report. <br> Please find attached document herewith. <br><br>";

            String recievers = "aditya@dineshmills.com";
//            String recievers = "rishineekhra@dineshmills.com";

            String pCC = "rakeshdalal@dineshmills.com,sdmlerp@dineshmills.com";
//            String pCC = "sdmlerp@dineshmills.com";

            String sql1 = "SELECT CASE WHEN GROUP_NAME IN ('HDS') THEN '3.HDS' "
                    + "WHEN  GROUP_NAME IN ('SDF') THEN '4. SDF' "
                    + "WHEN GROUP_NAME IN ('ACNE','FCNE') THEN '2. ACNE' "
                    + "ELSE '1. PRESS' END AS GROUP_NAME, "
                    + "POSITION_NO AS 'POSITION NO',POSITION_DESC AS 'POSITION DESCRIPTION',SUM(GOAL) AS 'GOAL',SUM(CURRENT_PROJECTION) AS 'PROJECTION',SUM(GOAL-CURRENT_PROJECTION) AS 'GOAL' FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                    + "WHERE SUBSTRING(DOC_NO,1,7) = CONCAT('B2122',RIGHT(100+MONTH(CURDATE()),2)) "
                    + "AND GOAL-CURRENT_PROJECTION !=0 "
                    + "GROUP BY GROUP_NAME,POSITION_NO,POSITION_DESC "
                    + "ORDER BY GROUP_NAME,SUM(GOAL-CURRENT_PROJECTION) DESC ";

            String sql2 = "SELECT PARTY_CODE AS 'PARTY CODE',PARTY_NAME AS 'PARTY NAME',UPN AS 'UPN',MACHINE_NO AS 'MACHINE NO',POSITION_DESC AS 'POSITION DESCRIPTION',GROUP_NAME AS 'GROUP NAME',GOAL AS 'GOAL',CURRENT_PROJECTION AS 'PROJECTION',GOAL-CURRENT_PROJECTION AS 'GAP' FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                    + "WHERE SUBSTRING(DOC_NO,1,7) = CONCAT('B2122',RIGHT(100+MONTH(CURDATE()),2)) "
                    + "AND GOAL-CURRENT_PROJECTION !=0 "
                    + "ORDER BY GOAL-CURRENT_PROJECTION DESC ";

            String sql3 = "SELECT PARTY_CODE AS 'PARTY CODE',PARTY_NAME AS 'PARTY NAME',SUM(GOAL) AS 'GOAL',SUM(CURRENT_PROJECTION) AS 'PROJECTION',SUM(GOAL-CURRENT_PROJECTION) AS 'GAP' FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                    + "WHERE SUBSTRING(DOC_NO,1,7) = CONCAT('B2122',RIGHT(100+MONTH(CURDATE()),2)) "
                    + "AND GOAL-CURRENT_PROJECTION !=0 "
                    + "GROUP BY PARTY_CODE,PARTY_NAME "
                    + "ORDER BY SUM(GOAL-CURRENT_PROJECTION) DESC ";

            exprt.fillData(sql3 + "#" + sql2 + "#" + sql1, new File("/Email_Attachment/GoalAgainstSP_" + mntFor + ".xls"), "PartyWise GAP Report#UPN Wise GAP Report#Group Position Wise GAP Report");

            data.Execute("INSERT INTO PRODUCTION.A_CHECK_REPORT_SEND (SEND_DESC, SEND_MONTH, SEND_YEAR) VALUES ('GOAL_AGAINST_SALES_PROJECTION', MONTH(CURDATE()), YEAR(CURDATE()) )");
            String responce = sendNotificationMailWithAttachement("Goal Against Sales Projection " + mntFor + "", pMessage, recievers, pCC, "/Email_Attachment/GoalAgainstSP_" + mntFor + ".xls", "GoalAgainstSP_" + mntFor + ".xls");
            System.out.println("Mail Response:" + responce);

        }

    }

    public static String sendNotificationMailWithAttachement(String pSubject, String pMessage, String recievers, String pcc, String Path, String PFiles) {
        try {
            System.out.println("Recivers : " + recievers);
            System.out.println("pSubject : " + pSubject);
            System.out.println("pMessage : " + pMessage);
            System.out.println("pCC      : " + pcc);
            System.out.println("Files    : " + PFiles);

            JavaMail.SendMailwithAttachment(recievers, pMessage, pSubject, pcc, Path, PFiles);
        } catch (Exception e) {
            System.out.println("Error Msg " + e.getMessage());
            e.printStackTrace();
        }
        return "Mail Sending Done....!";
    }

}
