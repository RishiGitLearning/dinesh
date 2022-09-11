/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales;

import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.Reports.clsExcelExporter;
import EITLERP.FeltSales.common.JavaMail;
import java.io.File;

/**
 *
 * @author Dharmendra
 */
public class PartyMasterNotification {

    /**
     * @param args the command line arguments
     */
    private static clsExcelExporter exprt = new clsExcelExporter();

    public static void main(String[] args) {
        // TODO code application logic here
        if (EITLERPGLOBAL.getCurrentDay() == 1) {
            String sql = "SELECT  @a:=@a+1 AS SR_NO,DATE_FORMAT(NOW(),'%d/%m/%Y %H:%i:%s') AS DATE_TIME,COALESCE(GROUP_CODE,'') AS GROUP_CODE,COALESCE(GROUP_DESC,'') AS GROUP_DESCRIPTION,"
                    + "PARTY_CODE,PARTY_NAME,CHARGE_CODE,ADDRESS1,ADDRESS2,PINCODE,CITY_ID AS CITY,GSTIN_NO,DATE_FORMAT(GSTIN_DATE,'%d/%m/%Y') AS GSTIN_DATE,STATE,DISPATCH_STATION,INSURANCE_CODE,INCHARGE_NAME "
                    + "FROM (SELECT @a:= 0) AS a,(SELECT PARTY_CODE,PARTY_NAME,CHARGE_CODE,INCHARGE_CD,"
                    + "ADDRESS1,ADDRESS2,PINCODE,CITY_ID,GSTIN_NO,GSTIN_DATE,STATE,DISPATCH_STATION,INSURANCE_CODE,PARTY_CLOSE_IND,MAIN_ACCOUNT_CODE FROM "
                    + "DINESHMILLS.D_SAL_PARTY_MASTER WHERE COALESCE(PARTY_CLOSE_IND,0)=0 AND MAIN_ACCOUNT_CODE=210010  AND APPROVED=1 AND COALESCE(CANCELLED,0)=0 ) AS JR "
                    + "LEFT JOIN ( SELECT INCHARGE_CD,INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE) AB ON JR.INCHARGE_CD=AB.INCHARGE_CD "
                    + "LEFT JOIN (SELECT D.GROUP_CODE,D.GROUP_DESC,E.PARTY_CODE AS PARTY,E.PARTY_ACTIVE,D.GROUP_CRITICAL_LIMIT,E.CASH_DISC_FLAG,E.YEAR_END_DISC_FLAG "
                    + "FROM PRODUCTION.FELT_GROUP_MASTER_HEADER D,PRODUCTION.FELT_GROUP_MASTER_DETAIL E "
                    + "WHERE D.GROUP_CODE=E.GROUP_CODE AND D.APPROVED=1 AND D.CANCELED=0)  AS JJ ON  JJ.PARTY=JR.PARTY_CODE";
            //exprt.fillData(sql, new File("/Email_Attachment/party_master_active.xls"), "Active_Party");
            String recievers = "rakeshdalal@dineshmills.com,vdshanbhag@dineshmills.com,narendramotiani@dineshmills.com,hemantprajapati@dineshmills.com";
        //String recievers = "rishineekhra@dineshmills.com";
            //String recievers = "sdmlerp@dineshmills.com";
            //String responce = sendNotificationMailWithAttachement("Active Party Master", "Active Party Master", recievers, "sdmlerp@dineshmills.com", "/Email_Attachment/party_master_active.xls", "party_master_active.xls");
            //System.out.println("Mail Response:" + responce);

            String sql1 = "SELECT  @a:=@a+1 AS SR_NO,DATE_FORMAT(NOW(),'%d/%m/%Y %H:%i:%s') AS DATE_TIME,COALESCE(GROUP_CODE,'') AS GROUP_CODE,COALESCE(GROUP_DESC,'') AS GROUP_DESCRIPTION,"
                    + "PARTY_CODE,PARTY_NAME,CHARGE_CODE,ADDRESS1,ADDRESS2,PINCODE,CITY_ID AS CITY,GSTIN_NO,DATE_FORMAT(GSTIN_DATE,'%d/%m/%Y') AS GSTIN_DATE,STATE,DISPATCH_STATION,INSURANCE_CODE,INCHARGE_NAME "
                    + "FROM (SELECT @a:= 0) AS a,(SELECT PARTY_CODE,PARTY_NAME,CHARGE_CODE,INCHARGE_CD,"
                    + "ADDRESS1,ADDRESS2,PINCODE,CITY_ID,GSTIN_NO,GSTIN_DATE,STATE,DISPATCH_STATION,INSURANCE_CODE,PARTY_CLOSE_IND,MAIN_ACCOUNT_CODE FROM "
                    + "DINESHMILLS.D_SAL_PARTY_MASTER WHERE COALESCE(PARTY_CLOSE_IND,0)=1 AND MAIN_ACCOUNT_CODE=210010 AND COALESCE(PARTY_MILL_CLOSED_IND,0)=0  AND APPROVED=1 AND COALESCE(CANCELLED,0)=0 ) AS JR "
                    + "LEFT JOIN ( SELECT INCHARGE_CD,INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE) AB ON JR.INCHARGE_CD=AB.INCHARGE_CD "
                    + "LEFT JOIN (SELECT D.GROUP_CODE,D.GROUP_DESC,E.PARTY_CODE AS PARTY,E.PARTY_ACTIVE,D.GROUP_CRITICAL_LIMIT,E.CASH_DISC_FLAG,E.YEAR_END_DISC_FLAG "
                    + "FROM PRODUCTION.FELT_GROUP_MASTER_HEADER D,PRODUCTION.FELT_GROUP_MASTER_DETAIL E "
                    + "WHERE D.GROUP_CODE=E.GROUP_CODE AND D.APPROVED=1 AND D.CANCELED=0)  AS JJ ON  JJ.PARTY=JR.PARTY_CODE";
            String sql2 = "SELECT  @a:=@a+1 AS SR_NO,DATE_FORMAT(NOW(),'%d/%m/%Y %H:%i:%s') AS DATE_TIME,COALESCE(GROUP_CODE,'') AS GROUP_CODE,COALESCE(GROUP_DESC,'') AS GROUP_DESCRIPTION,"
                    + "PARTY_CODE,PARTY_NAME,CHARGE_CODE,ADDRESS1,ADDRESS2,PINCODE,CITY_ID AS CITY,GSTIN_NO,DATE_FORMAT(GSTIN_DATE,'%d/%m/%Y') AS GSTIN_DATE,STATE,DISPATCH_STATION,INSURANCE_CODE,INCHARGE_NAME "
                    + "FROM (SELECT @a:= 0) AS a,(SELECT PARTY_CODE,PARTY_NAME,CHARGE_CODE,INCHARGE_CD,"
                    + "ADDRESS1,ADDRESS2,PINCODE,CITY_ID,GSTIN_NO,GSTIN_DATE,STATE,DISPATCH_STATION,INSURANCE_CODE,PARTY_CLOSE_IND,MAIN_ACCOUNT_CODE FROM "
                    + "DINESHMILLS.D_SAL_PARTY_MASTER WHERE COALESCE(PARTY_CLOSE_IND,0)=1 AND MAIN_ACCOUNT_CODE=210010 AND COALESCE(PARTY_MILL_CLOSED_IND,0)=1 AND APPROVED=1 AND COALESCE(CANCELLED,0)=0 ) AS JR "
                    + "LEFT JOIN ( SELECT INCHARGE_CD,INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE) AB ON JR.INCHARGE_CD=AB.INCHARGE_CD "
                    + "LEFT JOIN (SELECT D.GROUP_CODE,D.GROUP_DESC,E.PARTY_CODE AS PARTY,E.PARTY_ACTIVE,D.GROUP_CRITICAL_LIMIT,E.CASH_DISC_FLAG,E.YEAR_END_DISC_FLAG "
                    + "FROM PRODUCTION.FELT_GROUP_MASTER_HEADER D,PRODUCTION.FELT_GROUP_MASTER_DETAIL E "
                    + "WHERE D.GROUP_CODE=E.GROUP_CODE AND D.APPROVED=1 AND D.CANCELED=0)  AS JJ ON  JJ.PARTY=JR.PARTY_CODE";
            String sql3 = "SELECT  @a:=@a+1 AS SR_NO,DATE_FORMAT(NOW(),'%d/%m/%Y %H:%i:%s') AS DATE_TIME,COALESCE(GROUP_CODE,'') AS GROUP_CODE,COALESCE(GROUP_DESC,'') AS GROUP_DESCRIPTION,"
                    + "PARTY_CODE,PARTY_NAME,CHARGE_CODE,ADDRESS1,ADDRESS2,PINCODE,CITY_ID AS CITY,GSTIN_NO,DATE_FORMAT(GSTIN_DATE,'%d/%m/%Y') AS GSTIN_DATE,STATE,DISPATCH_STATION,INSURANCE_CODE,INCHARGE_NAME "
                    + "FROM (SELECT @a:= 0) AS a,(SELECT PARTY_CODE,PARTY_NAME,CHARGE_CODE,INCHARGE_CD,"
                    + "ADDRESS1,ADDRESS2,PINCODE,CITY_ID,GSTIN_NO,GSTIN_DATE,STATE,DISPATCH_STATION,INSURANCE_CODE,PARTY_CLOSE_IND,MAIN_ACCOUNT_CODE FROM "
                    + "DINESHMILLS.D_SAL_PARTY_MASTER WHERE COALESCE(PARTY_CLOSE_IND,0)=1 AND MAIN_ACCOUNT_CODE=210010 AND COALESCE(PARTY_MILL_CLOSED_IND,0)=2 AND APPROVED=1 AND COALESCE(CANCELLED,0)=0 ) AS JR "
                    + "LEFT JOIN ( SELECT INCHARGE_CD,INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE) AB ON JR.INCHARGE_CD=AB.INCHARGE_CD "
                    + "LEFT JOIN (SELECT D.GROUP_CODE,D.GROUP_DESC,E.PARTY_CODE AS PARTY,E.PARTY_ACTIVE,D.GROUP_CRITICAL_LIMIT,E.CASH_DISC_FLAG,E.YEAR_END_DISC_FLAG "
                    + "FROM PRODUCTION.FELT_GROUP_MASTER_HEADER D,PRODUCTION.FELT_GROUP_MASTER_DETAIL E "
                    + "WHERE D.GROUP_CODE=E.GROUP_CODE AND D.APPROVED=1 AND D.CANCELED=0)  AS JJ ON  JJ.PARTY=JR.PARTY_CODE";
            //exprt.fillData(sql, new File("/Email_Attachment/non_party_master_active.xls"), "Non_Active_Party");
            //recievers = "dharmendra@dineshmills.com";
        //String recievers = "rishineekhra@dineshmills.com";
            //String recievers = "sdmlerp@dineshmills.com";
            exprt.fillData(sql3+"#"+sql2+"#"+sql1+"#"+sql, new File("/Email_Attachment/party_master.xls"), "Bussine_Close_Due_To_Temporary_Closed#Bussine_Close_Due_To_Mill_Closed#Non_Active_Party#Active_Party");            
            String responce = sendNotificationMailWithAttachement("Party Master", "Party Master", recievers, "aditya@dineshmills.com,sdmlerp@dineshmills.com", "/Email_Attachment/party_master.xls", "party_master.xls");
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
