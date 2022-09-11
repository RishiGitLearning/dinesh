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
public class CostCenterwiseIssueFinishingNotification {

    /**
     * @param args the command line arguments
     */
    private static clsExcelExporter exprt = new clsExcelExporter();

    public static void main(String[] args) {
        // TODO code application logic here
        //if (EITLERPGLOBAL.getCurrentDay() == 25) {
            String sql = "SELECT D.DEPT_ID,UPPER(D.DEPT_DESC) AS DEPT_NAME, B.COST_CENTER_ID,UPPER(COST_CENTER_NAME) AS CCNAME,ISSUE_CAT,SUM(QTY) ,SUM(B.ISSUE_VALUE) FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B,D_INV_ITEM_MASTER I ,D_COM_COST_CENTER C,D_COM_DEPT_MASTER D  WHERE \n"
                    + "C.COST_CENTER_ID = B.COST_CENTER_ID AND C.DEPT_ID = D.DEPT_ID  AND\n"
                    + "A.COMPANY_ID = '2' AND\n"
                    + "I.COMPANY_ID = '2' AND\n"
                    + "A.ISSUE_NO=B.ISSUE_NO AND\n"
                    + "B.ITEM_CODE=I.ITEM_ID AND\n"
                    + "I.APPROVED=1 AND I.CANCELLED=0 AND\n"
                    + "A.APPROVED=1 AND A.CANCELED=0 AND\n"
                    + "MONTH(A.ISSUE_DATE)=MONTH(CURDATE()- INTERVAL 1 MONTH) AND\n"
                    + "YEAR(A.ISSUE_DATE)=YEAR(CURDATE()- INTERVAL 1 MONTH) \n"                    
                    + "AND B.COST_CENTER_ID NOT LIKE '3%' "
                    + "AND B.ITEM_CODE NOT LIKE ('RM%') AND D.DEPT_ID=38\n"
                    + "GROUP BY D.DEPT_ID,D.DEPT_DESC,B.COST_CENTER_ID ,ISSUE_CAT                     \n"
                    + "UNION ALL\n"
                    + "SELECT D.DEPT_ID,UPPER(D.DEPT_DESC) AS DEPT_NAME, B.COST_CENTER_ID,UPPER(COST_CENTER_NAME) AS CCNAME,CONCAT('TOTAL (' ,UPPER(COST_CENTER_NAME),')' ), SUM(QTY) ,SUM(B.ISSUE_VALUE)\n"
                    + "FROM D_INV_ISSUE_HEADER A,\n"
                    + "D_INV_ISSUE_DETAIL B,\n"
                    + "D_INV_ITEM_MASTER I ,\n"
                    + "D_COM_COST_CENTER C,\n"
                    + "D_COM_DEPT_MASTER D\n"
                    + "WHERE \n"
                    + "C.COST_CENTER_ID = B.COST_CENTER_ID AND C.DEPT_ID = D.DEPT_ID  AND\n"
                    + "A.COMPANY_ID = '2' AND\n"
                    + "I.COMPANY_ID = '2' AND\n"
                    + "A.ISSUE_NO=B.ISSUE_NO AND\n"
                    + "B.ITEM_CODE=I.ITEM_ID AND\n"
                    + "I.APPROVED=1 AND I.CANCELLED=0 AND\n"
                    + "A.APPROVED=1 AND A.CANCELED=0 AND\n"
                    + " B.COST_CENTER_ID NOT LIKE '3%' AND "
                    + "MONTH(A.ISSUE_DATE)=MONTH(CURDATE()- INTERVAL 1 MONTH) AND\n"
                    + "YEAR(A.ISSUE_DATE)=YEAR(CURDATE()- INTERVAL 1 MONTH) AND B.ITEM_CODE NOT LIKE ('RM%') AND D.DEPT_ID=38\n"
                    + "GROUP BY D.DEPT_ID,D.DEPT_DESC,B.COST_CENTER_ID\n"
                    + "UNION ALL\n"
                    + "SELECT D.DEPT_ID,UPPER(D.DEPT_DESC) AS DEPT_NAME, 'TOTAL ' AS COST_CENTER_ID,CONCAT('TOTAL (' ,UPPER(DEPT_DESC),')' ) AS CCNAME,'TOTAL', SUM(QTY) ,SUM(B.ISSUE_VALUE)\n"
                    + "FROM\n"
                    + "D_INV_ISSUE_HEADER A,\n"
                    + "D_INV_ISSUE_DETAIL B,\n"
                    + "D_INV_ITEM_MASTER I ,\n"
                    + "D_COM_COST_CENTER C,\n"
                    + "D_COM_DEPT_MASTER D\n"
                    + "WHERE \n"
                    + "C.COST_CENTER_ID = B.COST_CENTER_ID AND C.DEPT_ID = D.DEPT_ID  AND\n"
                    + "A.COMPANY_ID = '2' AND\n"
                    + "I.COMPANY_ID = '2' AND\n"
                    + "A.ISSUE_NO=B.ISSUE_NO AND\n"
                    + "B.ITEM_CODE=I.ITEM_ID AND\n"
                    + "I.APPROVED=1 AND I.CANCELLED=0 AND\n"
                    + "A.APPROVED=1 AND A.CANCELED=0 AND\n"
                    + " B.COST_CENTER_ID NOT LIKE '3%' AND "
                    + " MONTH(A.ISSUE_DATE)=MONTH(CURDATE()- INTERVAL 1 MONTH) AND\n"
                    + "YEAR(A.ISSUE_DATE)=YEAR(CURDATE()- INTERVAL 1 MONTH)  AND B.ITEM_CODE NOT LIKE ('RM%') AND D.DEPT_ID=38\n"
                    + "GROUP BY D.DEPT_ID,D.DEPT_DESC\n"
                    + "UNION ALL\n"
                    + "SELECT '99.TOTAL' AS DEPT_ID,'GRAND TOTAL 'AS DEPT_NAME, 'TOTAL ' AS COST_CENTER_ID,'TOTAL' AS CCNAME,\n"
                    + "'TOTAL', SUM(QTY) ,SUM(B.ISSUE_VALUE)\n"
                    + "FROM\n"
                    + "D_INV_ISSUE_HEADER A,\n"
                    + "D_INV_ISSUE_DETAIL B,\n"
                    + "D_INV_ITEM_MASTER I ,\n"
                    + "D_COM_COST_CENTER C,\n"
                    + "D_COM_DEPT_MASTER D\n"
                    + "WHERE \n"
                    + "C.COST_CENTER_ID = B.COST_CENTER_ID AND C.DEPT_ID = D.DEPT_ID  AND\n"
                    + "A.COMPANY_ID = '2' AND\n"
                    + "I.COMPANY_ID = '2' AND\n"
                    + "A.ISSUE_NO=B.ISSUE_NO AND\n"
                    + "B.ITEM_CODE=I.ITEM_ID AND\n"
                    + "I.APPROVED=1 AND I.CANCELLED=0 AND\n"
                    + "A.APPROVED=1 AND A.CANCELED=0 AND\n"
                    + " B.COST_CENTER_ID NOT LIKE '3%' AND "
                    + "MONTH(A.ISSUE_DATE)=MONTH(CURDATE()- INTERVAL 1 MONTH) AND\n"
                    + "YEAR(A.ISSUE_DATE)=YEAR(CURDATE()- INTERVAL 1 MONTH) AND B.ITEM_CODE NOT LIKE ('RM%') AND D.DEPT_ID=38\n"
                    + "ORDER BY DEPT_ID+0,DEPT_NAME,COST_CENTER_ID ,ISSUE_CAT";
            //exprt.fillData(sql, new File("/Email_Attachment/party_master_active.xls"), "Active_Party");
            String recievers = "amitkanti@dineshmills.com,yrpatel@dineshmills.com,rakeshdalal@dineshmills.com,brdfltfin@dineshmills.com,hemantprajapati@dineshmills.com";
            String pcc="aditya@dineshmills.com,abtewary@dineshmills.com,rishineekhra@dineshmills.com";
            //String recievers = "ashutosh@dineshmills.com";
            //String pcc = "rishineekhra@dineshmills.com";
            //String recievers = "sdmlerp@dineshmills.com";
            //String responce = sendNotificationMailWithAttachement("Active Party Master", "Active Party Master", recievers, "sdmlerp@dineshmills.com", "/Email_Attachment/party_master_active.xls", "party_master_active.xls");
            //System.out.println("Mail Response:" + responce);

            String sql1 = "SELECT D.DEPT_ID,UPPER(D.DEPT_DESC) AS DEPT_NAME, B.COST_CENTER_ID,UPPER(COST_CENTER_NAME) AS CCNAME,ISSUE_CAT,UPPER(B.ITEM_CODE) AS ITEM,I.ITEM_DESCRIPTION,A.ISSUE_NO, A.ISSUE_DATE,QTY,RATE,B.ISSUE_VALUE\n"
                + "FROM\n"
                + "DINESHMILLS.D_INV_ISSUE_HEADER A,\n"
                + "DINESHMILLS.D_INV_ISSUE_DETAIL B,\n"
                + "DINESHMILLS.D_INV_ITEM_MASTER I ,\n"
                + "DINESHMILLS.D_COM_COST_CENTER C,\n"
                + "DINESHMILLS.D_COM_DEPT_MASTER D\n"
                + "WHERE\n"
                + "C.COST_CENTER_ID = B.COST_CENTER_ID AND C.DEPT_ID = D.DEPT_ID  AND\n"
                + "A.COMPANY_ID = '2' AND\n"
                + "I.COMPANY_ID = '2' AND\n"
                + "A.ISSUE_NO=B.ISSUE_NO AND\n"
                + "B.ITEM_CODE=I.ITEM_ID AND\n"
                + "I.APPROVED=1 AND I.CANCELLED=0 AND\n"
                + "A.APPROVED=1 AND A.CANCELED=0 AND\n"
                + "MONTH(A.ISSUE_DATE)=MONTH(CURDATE()- INTERVAL 1 MONTH) AND\n"
                + "YEAR(A.ISSUE_DATE)=YEAR(CURDATE()- INTERVAL 1 MONTH)\n"
                + "AND B.COST_CENTER_ID NOT LIKE '3%' AND B.ITEM_CODE NOT LIKE ('RM%') AND D.DEPT_ID=38 \n"
                + "UNION ALL\n"
                + "SELECT D.DEPT_ID,UPPER(D.DEPT_DESC) AS DEPT_NAME,'TOTAL' AS COST_CENTER_ID,'TOTAL' AS CCNAME,'TOTAL' AS ISSUE_CAT,'TOTAL' AS ITEM,'TOTAL' AS ITEM_DESCRIPTION,'TOTAL' AS ISSUE_NO,'TOTAL' AS ISSUE_DATE,SUM(QTY),'' AS RATE,SUM(B.ISSUE_VALUE)\n"
                + "FROM\n"
                + "DINESHMILLS.D_INV_ISSUE_HEADER A,\n"
                + "DINESHMILLS.D_INV_ISSUE_DETAIL B,\n"
                + "DINESHMILLS.D_INV_ITEM_MASTER I ,\n"
                + "DINESHMILLS.D_COM_COST_CENTER C,\n"
                + "DINESHMILLS.D_COM_DEPT_MASTER D\n"
                + "WHERE\n"
                + "C.COST_CENTER_ID = B.COST_CENTER_ID AND C.DEPT_ID = D.DEPT_ID  AND\n"
                + "A.COMPANY_ID = '2' AND\n"
                + "I.COMPANY_ID = '2' AND\n"
                + "A.ISSUE_NO=B.ISSUE_NO AND\n"
                + "B.ITEM_CODE=I.ITEM_ID AND\n"
                + "I.APPROVED=1 AND I.CANCELLED=0 AND\n"
                + "A.APPROVED=1 AND A.CANCELED=0 AND\n"
                + "MONTH(A.ISSUE_DATE)=MONTH(CURDATE()- INTERVAL 1 MONTH) AND\n"
                + "YEAR(A.ISSUE_DATE)=YEAR(CURDATE()- INTERVAL 1 MONTH)\n"
                + "AND B.COST_CENTER_ID NOT LIKE '3%' AND B.ITEM_CODE NOT LIKE ('RM%') AND D.DEPT_ID=38 \n"
                + "ORDER BY DEPT_ID,DEPT_NAME,COST_CENTER_ID ,ISSUE_CAT,ITEM,ISSUE_NO";
            String sql2 = "SELECT D.DEPT_ID,UPPER(D.DEPT_DESC) AS DEPT_NAME, B.COST_CENTER_ID,UPPER(COST_CENTER_NAME) AS CCNAME,ISSUE_CAT,SUM(QTY) ,SUM(B.ISSUE_VALUE)\n"
                    + "FROM\n"
                    + "D_INV_ISSUE_HEADER A,\n"
                    + "D_INV_ISSUE_DETAIL B,\n"
                    + "D_INV_ITEM_MASTER I ,\n"
                    + "D_COM_COST_CENTER C,\n"
                    + "D_COM_DEPT_MASTER D\n"
                    + "WHERE \n"
                    + "C.COST_CENTER_ID = B.COST_CENTER_ID AND C.DEPT_ID = D.DEPT_ID  AND\n"
                    + "A.COMPANY_ID = '2' AND\n"
                    + "I.COMPANY_ID = '2' AND\n"
                    + "A.ISSUE_NO=B.ISSUE_NO AND\n"
                    + "B.ITEM_CODE=I.ITEM_ID AND\n"
                    + "I.APPROVED=1 AND I.CANCELLED=0 AND\n"
                    + "A.APPROVED=0 AND A.CANCELED=0 AND\n"
                    + "MONTH(A.ISSUE_DATE)=MONTH(CURDATE()- INTERVAL 1 MONTH) AND\n"
                    + "YEAR(A.ISSUE_DATE)=YEAR(CURDATE()- INTERVAL 1 MONTH) \n"
                    + "AND B.COST_CENTER_ID NOT LIKE '3%' "
                    + "AND B.ITEM_CODE NOT LIKE ('RM%') AND D.DEPT_ID=38\n"
                    + "GROUP BY D.DEPT_ID,D.DEPT_DESC,B.COST_CENTER_ID ,ISSUE_CAT\n"                                        
                    + "UNION ALL\n"
                    + "SELECT D.DEPT_ID,UPPER(D.DEPT_DESC) AS DEPT_NAME, B.COST_CENTER_ID,UPPER(COST_CENTER_NAME) AS CCNAME,\n"
                    + "CONCAT('TOTAL (' ,UPPER(COST_CENTER_NAME),')' ), SUM(QTY) ,SUM(B.ISSUE_VALUE)\n"
                    + "FROM\n"
                    + "D_INV_ISSUE_HEADER A,\n"
                    + "D_INV_ISSUE_DETAIL B,\n"
                    + "D_INV_ITEM_MASTER I ,\n"
                    + "D_COM_COST_CENTER C,\n"
                    + "D_COM_DEPT_MASTER D\n"
                    + "WHERE \n"
                    + "C.COST_CENTER_ID = B.COST_CENTER_ID AND C.DEPT_ID = D.DEPT_ID  AND\n"
                    + "A.COMPANY_ID = '2' AND\n"
                    + "I.COMPANY_ID = '2' AND\n"
                    + "A.ISSUE_NO=B.ISSUE_NO AND\n"
                    + "B.ITEM_CODE=I.ITEM_ID AND\n"
                    + "I.APPROVED=1 AND I.CANCELLED=0 AND\n"
                    + "A.APPROVED=0 AND A.CANCELED=0 AND\n"
                    + "MONTH(A.ISSUE_DATE)=MONTH(CURDATE()- INTERVAL 1 MONTH) AND\n"
                    + "YEAR(A.ISSUE_DATE)=YEAR(CURDATE()- INTERVAL 1 MONTH) \n"
                    + "AND B.COST_CENTER_ID NOT LIKE '3%' "
                    + "AND B.ITEM_CODE NOT LIKE ('RM%') AND D.DEPT_ID=38\n"
                    + "GROUP BY D.DEPT_ID,D.DEPT_DESC,B.COST_CENTER_ID\n"                    
                    + "UNION ALL\n"
                    + "SELECT D.DEPT_ID,UPPER(D.DEPT_DESC) AS DEPT_NAME, 'TOTAL ' AS COST_CENTER_ID,CONCAT('TOTAL (' ,UPPER(DEPT_DESC),')' ) AS CCNAME,\n"
                    + "'TOTAL', SUM(QTY) ,SUM(B.ISSUE_VALUE)\n"
                    + "FROM\n"
                    + "D_INV_ISSUE_HEADER A,\n"
                    + "D_INV_ISSUE_DETAIL B,\n"
                    + "D_INV_ITEM_MASTER I ,\n"
                    + "D_COM_COST_CENTER C,\n"
                    + "D_COM_DEPT_MASTER D\n"
                    + "WHERE \n"
                    + "C.COST_CENTER_ID = B.COST_CENTER_ID AND C.DEPT_ID = D.DEPT_ID  AND\n"
                    + "A.COMPANY_ID = '2' AND\n"
                    + "I.COMPANY_ID = '2' AND\n"
                    + "A.ISSUE_NO=B.ISSUE_NO AND\n"
                    + "B.ITEM_CODE=I.ITEM_ID AND\n"
                    + "I.APPROVED=1 AND I.CANCELLED=0 AND\n"
                    + "A.APPROVED=0 AND A.CANCELED=0 AND\n"
                    + "MONTH(A.ISSUE_DATE)=MONTH(CURDATE()- INTERVAL 1 MONTH) AND\n"
                    + "YEAR(A.ISSUE_DATE)=YEAR(CURDATE()- INTERVAL 1 MONTH) \n"
                    + "AND B.COST_CENTER_ID NOT LIKE '3%' "
                    + "AND B.ITEM_CODE NOT LIKE ('RM%') AND D.DEPT_ID=38\n"
                    + "GROUP BY D.DEPT_ID,D.DEPT_DESC\n"                                                                                
                    + "UNION ALL\n"
                    + "SELECT '99.TOTAL' AS DEPT_ID,'GRAND TOTAL 'AS DEPT_NAME, 'TOTAL ' AS COST_CENTER_ID,'TOTAL' AS CCNAME,\n"
                    + "'TOTAL', SUM(QTY) ,SUM(B.ISSUE_VALUE)\n"
                    + "FROM\n"
                    + "D_INV_ISSUE_HEADER A,\n"
                    + "D_INV_ISSUE_DETAIL B,\n"
                    + "D_INV_ITEM_MASTER I ,\n"
                    + "D_COM_COST_CENTER C,\n"
                    + "D_COM_DEPT_MASTER D\n"
                    + "WHERE \n"
                    + "C.COST_CENTER_ID = B.COST_CENTER_ID AND C.DEPT_ID = D.DEPT_ID  AND\n"
                    + "A.COMPANY_ID = '2' AND\n"
                    + "I.COMPANY_ID = '2' AND\n"
                    + "A.ISSUE_NO=B.ISSUE_NO AND\n"
                    + "B.ITEM_CODE=I.ITEM_ID AND\n"
                    + "I.APPROVED=1 AND I.CANCELLED=0 AND\n"
                    + "A.APPROVED=0 AND A.CANCELED=0 AND\n"
                    + "MONTH(A.ISSUE_DATE)=MONTH(CURDATE()- INTERVAL 1 MONTH) AND\n"
                    + "YEAR(A.ISSUE_DATE)=YEAR(CURDATE()- INTERVAL 1 MONTH) \n"
                    + "AND B.COST_CENTER_ID NOT LIKE '3%' "
                    + "AND B.ITEM_CODE NOT LIKE ('RM%') AND D.DEPT_ID=38\n"                                        
                    + "ORDER BY DEPT_ID+0,DEPT_NAME,COST_CENTER_ID ,ISSUE_CAT";
            String sql3 = "SELECT D.DEPT_ID,UPPER(D.DEPT_DESC) AS DEPT_NAME, B.COST_CENTER_ID,UPPER(COST_CENTER_NAME) AS CCNAME,ISSUE_CAT,UPPER(B.ITEM_CODE) AS ITEM,I.ITEM_DESCRIPTION,A.ISSUE_NO, A.ISSUE_DATE,QTY,RATE,B.ISSUE_VALUE\n"
                + "FROM\n"
                + "DINESHMILLS.D_INV_ISSUE_HEADER A,\n"
                + "DINESHMILLS.D_INV_ISSUE_DETAIL B,\n"
                + "DINESHMILLS.D_INV_ITEM_MASTER I ,\n"
                + "DINESHMILLS.D_COM_COST_CENTER C,\n"
                + "DINESHMILLS.D_COM_DEPT_MASTER D\n"
                + "WHERE\n"
                + "C.COST_CENTER_ID = B.COST_CENTER_ID AND C.DEPT_ID = D.DEPT_ID  AND\n"
                + "A.COMPANY_ID = '2' AND\n"
                + "I.COMPANY_ID = '2' AND\n"
                + "A.ISSUE_NO=B.ISSUE_NO AND\n"
                + "B.ITEM_CODE=I.ITEM_ID AND\n"
                + "I.APPROVED=1 AND I.CANCELLED=0 AND\n"
                + "A.APPROVED=0 AND A.CANCELED=0 AND\n"
                + "MONTH(A.ISSUE_DATE)=MONTH(CURDATE()- INTERVAL 1 MONTH) AND\n"
                + "YEAR(A.ISSUE_DATE)=YEAR(CURDATE()- INTERVAL 1 MONTH)\n"
                + "AND B.COST_CENTER_ID NOT LIKE '3%' AND B.ITEM_CODE NOT LIKE ('RM%') AND D.DEPT_ID=38 \n"
                + "UNION ALL\n"
                + "SELECT D.DEPT_ID,UPPER(D.DEPT_DESC) AS DEPT_NAME,'TOTAL' AS COST_CENTER_ID,'TOTAL' AS CCNAME,'TOTAL' AS ISSUE_CAT,'TOTAL' AS ITEM,'TOTAL' AS ITEM_DESCRIPTION,'TOTAL' AS ISSUE_NO,'TOTAL' AS ISSUE_DATE,SUM(QTY),'' AS RATE,SUM(B.ISSUE_VALUE)\n"
                + "FROM\n"
                + "DINESHMILLS.D_INV_ISSUE_HEADER A,\n"
                + "DINESHMILLS.D_INV_ISSUE_DETAIL B,\n"
                + "DINESHMILLS.D_INV_ITEM_MASTER I ,\n"
                + "DINESHMILLS.D_COM_COST_CENTER C,\n"
                + "DINESHMILLS.D_COM_DEPT_MASTER D\n"
                + "WHERE\n"
                + "C.COST_CENTER_ID = B.COST_CENTER_ID AND C.DEPT_ID = D.DEPT_ID  AND\n"
                + "A.COMPANY_ID = '2' AND\n"
                + "I.COMPANY_ID = '2' AND\n"
                + "A.ISSUE_NO=B.ISSUE_NO AND\n"
                + "B.ITEM_CODE=I.ITEM_ID AND\n"
                + "I.APPROVED=1 AND I.CANCELLED=0 AND\n"
                + "A.APPROVED=0 AND A.CANCELED=0 AND\n"
                + "MONTH(A.ISSUE_DATE)=MONTH(CURDATE()- INTERVAL 1 MONTH) AND\n"
                + "YEAR(A.ISSUE_DATE)=YEAR(CURDATE()- INTERVAL 1 MONTH)\n"
                + "AND B.COST_CENTER_ID NOT LIKE '3%' AND B.ITEM_CODE NOT LIKE ('RM%') AND D.DEPT_ID=38 \n"
                + "ORDER BY DEPT_ID,DEPT_NAME,COST_CENTER_ID ,ISSUE_CAT,ITEM,ISSUE_NO";
            //exprt.fillData(sql, new File("/Email_Attachment/non_party_master_active.xls"), "Non_Active_Party");
            //recievers = "dharmendra@dineshmills.com";
        //String recievers = "rishineekhra@dineshmills.com";
            //String recievers = "sdmlerp@dineshmills.com";
            System.out.println(sql2);
            System.out.println(sql3);
            exprt.fillData(sql3+"#"+sql2+"#"+sql1+"#"+sql, new File("/Email_Attachment/Cost_Centerwise_Issue_Finishing.xls"), "DetailUnapproved#SummaryUnApproved#DetailApproved#SummaryApproved");            
            String responce = sendNotificationMailWithAttachement("Cost Centerwise Issue Finishing", "Cost Centerwise Issue Finishing", recievers, pcc, "/Email_Attachment/Cost_Centerwise_Issue_Finishing.xls", "Cost_Centerwise_Issue_Finishing.xls");
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
