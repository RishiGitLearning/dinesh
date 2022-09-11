/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.Stores;

import EITLERP.FeltSales.*;
import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.Reports.clsExcelExporter;
import EITLERP.FeltSales.common.JavaMail;
import java.io.File;

/**
 *
 * @author Dharmendra
 */
public class JobworkFinishingNotification {

    /**
     * @param args the command line arguments
     */
    private static clsExcelExporter exprt = new clsExcelExporter();

    public static void main(String[] args) {
        // TODO code application logic here
        //if (EITLERPGLOBAL.getCurrentDay() == 25) {
            String sql = "SELECT D.DEPT_ID,UPPER(D.DEPT_DESC) AS DEPT_NAME, JOB_CAT,SUM(RECEIVED_QTY) AS RECEIVED_QTY,SUM(B.TOTAL_AMOUNT) AS TOTAL_AMOUNT,NOW()      FROM\n"                
                + "D_INV_JOB_HEADER A,\n"                
                + "D_INV_JOB_DETAIL B,\n"                
                + "D_COM_DEPT_MASTER D\n"                
                + "WHERE \n"                
                + "B.DEPT_ID = D.DEPT_ID  AND\n"                
                + "A.COMPANY_ID = '2' AND\n"                
                + "A.JOB_NO=B.JOB_NO AND\n"                
                + "A.APPROVED=1 AND A.CANCELLED=0 AND\n"    
                + "MONTH(A.JOB_DATE)=MONTH(CURDATE()- INTERVAL 1 MONTH) AND \n"
                + "YEAR(A.JOB_DATE)=YEAR(CURDATE()- INTERVAL 1 MONTH) AND \n"
                + " B.JOB_NO NOT LIKE ('RM%') AND D.DEPT_ID=38\n"                
                + "GROUP BY D.DEPT_ID,UPPER(D.DEPT_DESC), JOB_CAT\n"                
                + "    UNION ALL  \n"                
                + "SELECT D.DEPT_ID,UPPER(D.DEPT_DESC) AS DEPT_NAME, 'TOTAL'  JOB_CAT,SUM(RECEIVED_QTY) AS RECEIVED_QTY,SUM(B.TOTAL_AMOUNT) AS TOTAL_AMOUNT,NOW()      FROM\n"                
                + "D_INV_JOB_HEADER A,\n"                
                + "D_INV_JOB_DETAIL B,\n"                
                + "D_COM_DEPT_MASTER D\n"                
                + "WHERE \n"                
                + "B.DEPT_ID = D.DEPT_ID  AND\n"                
                + "A.COMPANY_ID = '2' AND\n"                
                + "A.JOB_NO=B.JOB_NO AND\n"                
                + "A.APPROVED=1 AND A.CANCELLED=0 AND\n"                
                + "MONTH(A.JOB_DATE)=MONTH(CURDATE()- INTERVAL 1 MONTH) AND\n"
                + "YEAR(A.JOB_DATE)=YEAR(CURDATE()- INTERVAL 1 MONTH) AND \n"
                + " B.JOB_NO NOT LIKE ('RM%') AND D.DEPT_ID=38 \n"                
                + "GROUP BY D.DEPT_ID,UPPER(D.DEPT_DESC)\n"    
                + "    UNION ALL \n"                
                + "SELECT '99. TOTAL' AS DEPT_ID,' GRAND TOTAL' AS DEPT_NAME, 'TOTAL'  JOB_CAT,SUM(RECEIVED_QTY) AS RECEIVED_QTY,SUM(B.TOTAL_AMOUNT) AS TOTAL_AMOUNT,NOW()      FROM\n"                
                + "D_INV_JOB_HEADER A,\n"                
                + "D_INV_JOB_DETAIL B,\n"                
                + "D_COM_DEPT_MASTER D\n"                
                + "WHERE \n"                
                + "B.DEPT_ID = D.DEPT_ID  AND\n"                
                + "A.COMPANY_ID = '2' AND\n"                
                + "A.JOB_NO=B.JOB_NO AND\n"                
                + "A.APPROVED=1 AND A.CANCELLED=0 AND\n"                
                + "MONTH(A.JOB_DATE)=MONTH(CURDATE()- INTERVAL 1 MONTH) AND \n"
                + "YEAR(A.JOB_DATE)=YEAR(CURDATE()- INTERVAL 1 MONTH) AND \n"      
                + " B.JOB_NO NOT LIKE ('RM%') AND D.DEPT_ID=38\n"                
                + " \n"                
                + "ORDER BY DEPT_ID+0,DEPT_NAME,JOB_CAT";
            //exprt.fillData(sql, new File("/Email_Attachment/party_master_active.xls"), "Active_Party");
            String recievers = "amitkanti@dineshmills.com,yrpatel@dineshmills.com,rakeshdalal@dineshmills.com,brdfltfin@dineshmills.com,hemantprajapati@dineshmills.com";
            String pcc="aditya@dineshmills.com,abtewary@dineshmills.com,rishineekhra@dineshmills.com";
            //String recievers = "rakeshdalal@dineshmills.com,vdshanbhag@dineshmills.com,narendramotiani@dineshmills.com";
            //String recievers = "rishineekhra@dineshmills.com";
            //String recievers = "sdmlerp@dineshmills.com";
            //String responce = sendNotificationMailWithAttachement("Active Party Master", "Active Party Master", recievers, "sdmlerp@dineshmills.com", "/Email_Attachment/party_master_active.xls", "party_master_active.xls");
            //System.out.println("Mail Response:" + responce);

            String sql1 = "SELECT D.DEPT_ID,UPPER(D.DEPT_DESC) AS DEPT_NAME, JOB_CAT,UPPER(B.ITEM_ID) AS ITEM,ITEM_EXTRA_DESC,A.JOB_NO, A.JOB_DATE,RECEIVED_QTY,RATE,B.TOTAL_AMOUNT,NOW()     FROM\n"                
                + "D_INV_JOB_HEADER A,\n"                
                + "D_INV_JOB_DETAIL B,\n"                
                + "D_COM_DEPT_MASTER D\n"                
                + "WHERE \n"                
                + "B.DEPT_ID = D.DEPT_ID  AND\n"                
                + "A.COMPANY_ID = '2' AND\n"                
                + "A.JOB_NO=B.JOB_NO AND\n"                
                + "A.APPROVED=1 AND A.CANCELLED=0 AND\n"                
                + "MONTH(A.JOB_DATE)=MONTH(CURDATE()- INTERVAL 1 MONTH) AND \n"                
                + "YEAR(A.JOB_DATE)=YEAR(CURDATE()- INTERVAL 1 MONTH) \n"                    
                + " AND B.JOB_NO NOT LIKE ('RM%')  AND D.DEPT_ID=38\n"    
                + " UNION ALL\n"                
                + " SELECT '99. TOTAL' AS DEPT_ID,'TOTAL' AS DEPT_NAME, 'TOTAL' AS JOB_CAT,'TOTAL' AS ITEM,'TOTAL' ITEM_EXTRA_DESC,'TOTAL' AS JOB_NO, 'TOTAL' AS JOB_DATE,SUM(RECEIVED_QTY) AS RECEIVED_QTY,''AS RATE,SUM(B.TOTAL_AMOUNT) AS TOTAL_AMOUNT,NOW()     FROM\n"                
                + "D_INV_JOB_HEADER A,\n"                
                + "D_INV_JOB_DETAIL B,\n"                
                + "D_COM_DEPT_MASTER D\n"                
                + "WHERE \n"                
                + "B.DEPT_ID = D.DEPT_ID  AND\n"                
                + "A.COMPANY_ID = '2' AND\n"                
                + "A.JOB_NO=B.JOB_NO AND\n"                
                + "A.APPROVED=1 AND A.CANCELLED=0 AND\n"                
                + " MONTH(A.JOB_DATE)=MONTH(CURDATE()- INTERVAL 1 MONTH) AND\n"                
                + "YEAR(A.JOB_DATE)=YEAR(CURDATE()- INTERVAL 1 MONTH) \n"    
                + " AND B.JOB_NO NOT LIKE ('RM%')  AND D.DEPT_ID=38\n"                
                + " ORDER BY DEPT_ID+0,DEPT_NAME,JOB_CAT,JOB_NO\n";
            String sql2 = "SELECT D.DEPT_ID,UPPER(D.DEPT_DESC) AS DEPT_NAME, JOB_CAT,SUM(RECEIVED_QTY) AS RECEIVED_QTY,SUM(B.TOTAL_AMOUNT) AS TOTAL_AMOUNT,NOW()      FROM\n"                
                + "D_INV_JOB_HEADER A,\n"                
                + "D_INV_JOB_DETAIL B,\n"                
                + "D_COM_DEPT_MASTER D\n"                
                + "WHERE \n"                
                + "B.DEPT_ID = D.DEPT_ID  AND\n"                
                + "A.COMPANY_ID = '2' AND\n"                
                + "A.JOB_NO=B.JOB_NO AND\n"                
                + "A.APPROVED=0 AND A.CANCELLED=0 AND\n"    
                + "MONTH(A.JOB_DATE)=MONTH(CURDATE()- INTERVAL 1 MONTH) AND \n"
                + "YEAR(A.JOB_DATE)=YEAR(CURDATE()- INTERVAL 1 MONTH) AND \n"
                + " B.JOB_NO NOT LIKE ('RM%') AND D.DEPT_ID=38\n"                
                + "GROUP BY D.DEPT_ID,UPPER(D.DEPT_DESC), JOB_CAT\n"                
                + "    UNION ALL  \n"                
                + "SELECT D.DEPT_ID,UPPER(D.DEPT_DESC) AS DEPT_NAME, 'TOTAL'  JOB_CAT,SUM(RECEIVED_QTY) AS RECEIVED_QTY,SUM(B.TOTAL_AMOUNT) AS TOTAL_AMOUNT,NOW()      FROM\n"                
                + "D_INV_JOB_HEADER A,\n"                
                + "D_INV_JOB_DETAIL B,\n"                
                + "D_COM_DEPT_MASTER D\n"                
                + "WHERE \n"                
                + "B.DEPT_ID = D.DEPT_ID  AND\n"                
                + "A.COMPANY_ID = '2' AND\n"                
                + "A.JOB_NO=B.JOB_NO AND\n"                
                + "A.APPROVED=0 AND A.CANCELLED=0 AND\n"                
                + "MONTH(A.JOB_DATE)=MONTH(CURDATE()- INTERVAL 1 MONTH) AND\n"
                + "YEAR(A.JOB_DATE)=YEAR(CURDATE()- INTERVAL 1 MONTH) AND \n"
                + " B.JOB_NO NOT LIKE ('RM%') AND D.DEPT_ID=38 \n"                
                + "GROUP BY D.DEPT_ID,UPPER(D.DEPT_DESC)\n"    
                + "    UNION ALL \n"                
                + "SELECT '99. TOTAL' AS DEPT_ID,' GRAND TOTAL' AS DEPT_NAME, 'TOTAL'  JOB_CAT,SUM(RECEIVED_QTY) AS RECEIVED_QTY,SUM(B.TOTAL_AMOUNT) AS TOTAL_AMOUNT,NOW()      FROM\n"                
                + "D_INV_JOB_HEADER A,\n"                
                + "D_INV_JOB_DETAIL B,\n"                
                + "D_COM_DEPT_MASTER D\n"                
                + "WHERE \n"                
                + "B.DEPT_ID = D.DEPT_ID  AND\n"                
                + "A.COMPANY_ID = '2' AND\n"                
                + "A.JOB_NO=B.JOB_NO AND\n"                
                + "A.APPROVED=0 AND A.CANCELLED=0 AND\n"                
                + "MONTH(A.JOB_DATE)=MONTH(CURDATE()- INTERVAL 1 MONTH) AND \n"
                + "YEAR(A.JOB_DATE)=YEAR(CURDATE()- INTERVAL 1 MONTH) AND \n"      
                + " B.JOB_NO NOT LIKE ('RM%') AND D.DEPT_ID=38\n"                                
                + "ORDER BY DEPT_ID+0,DEPT_NAME,JOB_CAT";
            String sql3 = "SELECT D.DEPT_ID,UPPER(D.DEPT_DESC) AS DEPT_NAME, JOB_CAT,UPPER(B.ITEM_ID) AS ITEM,ITEM_EXTRA_DESC,A.JOB_NO, A.JOB_DATE,RECEIVED_QTY,RATE,B.TOTAL_AMOUNT,NOW()     FROM\n"                
                + "D_INV_JOB_HEADER A,\n"                
                + "D_INV_JOB_DETAIL B,\n"                
                + "D_COM_DEPT_MASTER D\n"                
                + "WHERE \n"                
                + "B.DEPT_ID = D.DEPT_ID  AND\n"                
                + "A.COMPANY_ID = '2' AND\n"                
                + "A.JOB_NO=B.JOB_NO AND\n"                
                + "A.APPROVED=0 AND A.CANCELLED=0 AND\n"                
                + "MONTH(A.JOB_DATE)=MONTH(CURDATE()- INTERVAL 1 MONTH) AND \n"                
                + "YEAR(A.JOB_DATE)=YEAR(CURDATE()- INTERVAL 1 MONTH) \n"                    
                + " AND B.JOB_NO NOT LIKE ('RM%')  AND D.DEPT_ID=38\n"    
                + " UNION ALL\n"                
                + " SELECT '99. TOTAL' AS DEPT_ID,'TOTAL' AS DEPT_NAME, 'TOTAL' AS JOB_CAT,'TOTAL' AS ITEM,'TOTAL' ITEM_EXTRA_DESC,'TOTAL' AS JOB_NO, 'TOTAL' AS JOB_DATE,SUM(RECEIVED_QTY) AS RECEIVED_QTY,''AS RATE,SUM(B.TOTAL_AMOUNT) AS TOTAL_AMOUNT,NOW()     FROM\n"                
                + "D_INV_JOB_HEADER A,\n"                
                + "D_INV_JOB_DETAIL B,\n"                
                + "D_COM_DEPT_MASTER D\n"                
                + "WHERE \n"                
                + "B.DEPT_ID = D.DEPT_ID  AND\n"                
                + "A.COMPANY_ID = '2' AND\n"                
                + "A.JOB_NO=B.JOB_NO AND\n"                
                + "A.APPROVED=0 AND A.CANCELLED=0 AND\n"                
                + " MONTH(A.JOB_DATE)=MONTH(CURDATE()- INTERVAL 1 MONTH) AND\n"                
                + "YEAR(A.JOB_DATE)=YEAR(CURDATE()- INTERVAL 1 MONTH) \n"    
                + " AND B.JOB_NO NOT LIKE ('RM%')  AND D.DEPT_ID=38\n"                
                + " ORDER BY DEPT_ID+0,DEPT_NAME,JOB_CAT,JOB_NO\n";
            //exprt.fillData(sql, new File("/Email_Attachment/non_party_master_active.xls"), "Non_Active_Party");
            //recievers = "dharmendra@dineshmills.com";
        //String recievers = "rishineekhra@dineshmills.com";
            //String recievers = "sdmlerp@dineshmills.com";
            System.out.println(sql2);
            System.out.println(sql3);
            exprt.fillData(sql3+"#"+sql2+"#"+sql1+"#"+sql, new File("/Email_Attachment/Jobwork_Finishing.xls"), "DetailUnapproved#SummaryUnApproved#DetailApproved#SummaryApproved");            
            String responce = sendNotificationMailWithAttachement("Jobwork Finishing", "Jobwork Finishing", recievers, pcc, "/Email_Attachment/Jobwork_Finishing.xls", "Jobwork_Finishing.xls");
            System.out.println("Mail Response:" + responce);
        //}

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
