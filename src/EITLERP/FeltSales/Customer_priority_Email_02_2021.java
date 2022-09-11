/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales;

import EITLERP.FeltSales.Reports.clsExcelExporter;
import EITLERP.FeltSales.common.JavaMail;
import EITLERP.data;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author Dharmendra
 */
public class Customer_priority_Email_02_2021 {

    /**
     * @param args the command line arguments
     */
    private static clsExcelExporter exprt = new clsExcelExporter();

    public static void main(String[] args) {
        // TODO code application logic here
        if (data.getIntValueFromDB("SELECT DAY(CURDATE()) FROM DUAL") == 1) {

        }
        if (data.getIntValueFromDB("select SUM(COALESCE(CASE WHEN coalesce(CANCELED,0) =0 AND coalesce(APPROVED,0) =0 THEN 1 ELSE 0 END,0)) AS UNDER_APPROVED from PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                + "where left(doc_no,7)=concat('B2021',right(100+month(curdate()),2))") == 0 && data.getIntValueFromDB("select SUM(COALESCE(CASE WHEN coalesce(CANCELED,0) =0 AND coalesce(APPROVED,0) =1 THEN 1 ELSE 0 END,0)) AS UNDER_APPROVED from PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                        + "where left(doc_no,7)=concat('B2021',right(100+month(curdate()),2))") > 0) {
            if (data.getIntValueFromDB("SELECT COUNT(*) FROM PRODUCTION.FELT_CUSTOMER_PRIORITY_EMAIL_STATUS "
                    + "WHERE left(doc_no,7)=concat('B2021',right(100+month(curdate()),2))") == 0) {
                String sql = "INSERT INTO PRODUCTION.FELT_CUSTOMER_PRIORITY_EMAIL_STATUS (DOC_NO,SEND_DATE) "
                        + "VALUES (concat('B2021',right(100+month(curdate()),2)),NOW())";
                data.Execute(sql);
                String responce, sql1, sql2, sql3, mcurmnth;
                mcurmnth = data.getStringValueFromDB("SELECT UPPER(LEFT(MONTHNAME(CURDATE()),3)) FROM DUAL");
//                String recievers = "rishineekhra@dineshmills.com";
                String recievers = "aditya@dineshmills.com,soumen@dineshmills.com,vdshanbhag@dineshmills.com,rishineekhra@dineshmills.com";

                String mdate = data.getStringValueFromDB("SELECT CONCAT(MONTHNAME(CURDATE()),' - ',YEAR(CURDATE())) FROM DUAL");

                Query_1();
                data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT SET PERCENTAGE  = CASE WHEN CURRENT_MONTH IN (2,3,4) THEN 0.25 WHEN CURRENT_MONTH IN (5,6,7) THEN 0.50 "
                        + "WHEN CURRENT_MONTH IN (8,9,10) THEN 0.75 WHEN CURRENT_MONTH IN (11,12,1) THEN 1 END "
                        + " WHERE  BUDGET_FINYEAR = '2020-2021'");
                Query_2();

                sql = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING as \"Total Pending Order\" ,NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",PENDING - OC_MAY as \"Pending UnConfirmed Order\" ,OC_MAY AS \"Pending Confirmed Order\",DESPATCH as \"Despatched\",OC_MAY+DESPATCH AS \"Pending Confirmed Order + Despatched (OCD)\",PERCENTAGE AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE as \"(Sales Projection + Prev.GR) * " + mcurmnth + "%\",OC_PRIORITY AS \"Priority (BP > OCD) \","
                        + "CTG AS \"Budgeted/Non Bugdeted\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + " FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                        + " WHERE OC_PRIORITY = 'YES' "
                        + " ORDER BY ZONE_INCHARGE,UPN";
                System.out.println("sql:"+sql);
                sql1 = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING as \"Total Pending Order\" ,NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",PENDING - OC_MAY as \"Pending UnConfirmed Order\" ,OC_MAY AS \"Pending Confirmed Order\",DESPATCH as \"Despatched\",OC_MAY+DESPATCH AS \"Pending Confirmed Order + Despatched (OCD)\",PERCENTAGE AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE as \"(Sales Projection + Prev.GR) * " + mcurmnth + "%\",OC_PRIORITY AS \"Priority (BP > OCD) \","
                        + "CTG AS \"Budgeted/Non Bugdeted\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + " FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                        + " ORDER BY ZONE_INCHARGE,UPN";
                System.out.println("sql1:"+sql1);
//                exprt.fillData(sql1 + "#" + sql, new File("/Email_Attachment/CUSTOMER_ORDER_CONFIRMATION_PRIORITY_REPORT.xls"), "ALL#CP");
//                
//                responce = sendNotificationMailWithAttachement("Customer Order Confirmation Priority Report For " + mdate, "", recievers, "sdmlerp@dineshmills.com", "/Email_Attachment/CUSTOMER_ORDER_CONFIRMATION_PRIORITY_REPORT.xls", "CUSTOMER_ORDER_CONFIRMATION_PRIORITY_REPORT.xls");
//                System.out.println("Mail Response:" + responce);

////North Zone Start
//                recievers = "gaurang@dineshmills.com";
                recievers = "mitanglad@dineshmills.com";
                sql = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING as \"Total Pending Order\" ,NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",PENDING - OC_MAY as \"Pending UnConfirmed Order\" ,OC_MAY AS \"Pending Confirmed Order\",DESPATCH as \"Despatched\",OC_MAY+DESPATCH AS \"Pending Confirmed Order + Despatched (OCD)\",PERCENTAGE AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE as \"(Sales Projection + Prev.GR) * " + mcurmnth + "%\",OC_PRIORITY AS \"Priority (BP > OCD) \","
                        + "CTG AS \"Budgeted/Non Bugdeted\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + " FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT WHERE OC_PRIORITY ='YES' AND ZONE_INCHARGE='NORTH' "
                        + " ORDER BY ZONE_INCHARGE,UPN";
                sql1 = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING as \"Total Pending Order\" ,NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",PENDING - OC_MAY as \"Pending UnConfirmed Order\" ,OC_MAY AS \"Pending Confirmed Order\",DESPATCH as \"Despatched\",OC_MAY+DESPATCH AS \"Pending Confirmed Order + Despatched (OCD)\",PERCENTAGE AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE as \"(Sales Projection + Prev.GR) * " + mcurmnth + "%\",OC_PRIORITY AS \"Priority (BP > OCD) \","
                        + "CTG AS \"Budgeted/Non Bugdeted\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + " FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT WHERE ZONE_INCHARGE='NORTH'"
                        + " ORDER BY ZONE_INCHARGE,UPN";

//                exprt.fillData(sql1 + "#" + sql, new File("/Email_Attachment/CUSTOMER_ORDER_CONFIRMATION_PRIORITY_NORTH.xls"), "ALL#CP");
//                responce = sendNotificationMailWithAttachement("Customer Order Confirmation Priority Report For " + mdate, "", recievers, "sdmlerp@dineshmills.com", "/Email_Attachment/CUSTOMER_ORDER_CONFIRMATION_PRIORITY_NORTH.xls", "CUSTOMER_ORDER_CONFIRMATION_PRIORITY_NORTH.xls");
//                System.out.println("Mail Response:" + responce);
////North Zone End
///EastWest Zone Start
//                recievers = "gaurang@dineshmills.com";
                recievers = "chinmoyghosh@dineshmills.com";
                sql = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING as \"Total Pending Order\" ,NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",PENDING - OC_MAY as \"Pending UnConfirmed Order\" ,OC_MAY AS \"Pending Confirmed Order\",DESPATCH as \"Despatched\",OC_MAY+DESPATCH AS \"Pending Confirmed Order + Despatched (OCD)\",PERCENTAGE AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE as \"(Sales Projection + Prev.GR) * " + mcurmnth + "%\",OC_PRIORITY AS \"Priority (BP > OCD) \","
                        + "CTG AS \"Budgeted/Non Bugdeted\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + " FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT WHERE OC_PRIORITY ='YES' AND ZONE_INCHARGE='EAST/WEST' "
                        + " ORDER BY ZONE_INCHARGE,UPN";
                sql1 = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING as \"Total Pending Order\" ,NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",PENDING - OC_MAY as \"Pending UnConfirmed Order\" ,OC_MAY AS \"Pending Confirmed Order\",DESPATCH as \"Despatched\",OC_MAY+DESPATCH AS \"Pending Confirmed Order + Despatched (OCD)\",PERCENTAGE AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE as \"(Sales Projection + Prev.GR) * " + mcurmnth + "%\",OC_PRIORITY AS \"Priority (BP > OCD) \","
                        + "CTG AS \"Budgeted/Non Bugdeted\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + " FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT WHERE ZONE_INCHARGE='EAST/WEST'"
                        + " ORDER BY ZONE_INCHARGE,UPN";

//                exprt.fillData(sql1 + "#" + sql, new File("/Email_Attachment/CUSTOMER_ORDER_CONFIRMATION_PRIORITY_EASTWEST.xls"), "ALL#CP");
//                responce = sendNotificationMailWithAttachement("Customer Order Confirmation Priority Report For " + mdate, "", recievers, "sdmlerp@dineshmills.com", "/Email_Attachment/CUSTOMER_ORDER_CONFIRMATION_PRIORITY_EASTWEST.xls", "CUSTOMER_ORDER_CONFIRMATION_PRIORITY_EASTWEST.xls");
//                System.out.println("Mail Response:" + responce);
///EastWest Zone End
///South Zone Start 
//                recievers = "gaurang@dineshmills.com";
                recievers = "siddharthneogi@dineshmills.com";
                sql = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING as \"Total Pending Order\" ,NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",PENDING - OC_MAY as \"Pending UnConfirmed Order\" ,OC_MAY AS \"Pending Confirmed Order\",DESPATCH as \"Despatched\",OC_MAY+DESPATCH AS \"Pending Confirmed Order + Despatched (OCD)\",PERCENTAGE AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE as \"(Sales Projection + Prev.GR) * " + mcurmnth + "%\",OC_PRIORITY AS \"Priority (BP > OCD) \","
                        + "CTG AS \"Budgeted/Non Bugdeted\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + " FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT WHERE OC_PRIORITY ='YES' AND ZONE_INCHARGE='SOUTH' "
                        + " ORDER BY ZONE_INCHARGE,UPN";
                sql1 = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING as \"Total Pending Order\" ,NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",PENDING - OC_MAY as \"Pending UnConfirmed Order\" ,OC_MAY AS \"Pending Confirmed Order\",DESPATCH as \"Despatched\",OC_MAY+DESPATCH AS \"Pending Confirmed Order + Despatched (OCD)\",PERCENTAGE AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE as \"(Sales Projection + Prev.GR) * " + mcurmnth + "%\",OC_PRIORITY AS \"Priority (BP > OCD) \","
                        + "CTG AS \"Budgeted/Non Bugdeted\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + " FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT WHERE ZONE_INCHARGE='SOUTH'"
                        + " ORDER BY ZONE_INCHARGE,UPN";

//                exprt.fillData(sql1 + "#" + sql, new File("/Email_Attachment/CUSTOMER_ORDER_CONFIRMATION_PRIORITY_SOUTH.xls"), "ALL#CP");
//                responce = sendNotificationMailWithAttachement("Customer Order Confirmation Priority Report For " + mdate, "", recievers, "sdmlerp@dineshmills.com", "/Email_Attachment/CUSTOMER_ORDER_CONFIRMATION_PRIORITY_SOUTH.xls", "CUSTOMER_ORDER_CONFIRMATION_PRIORITY_SOUTH.xls");
//                System.out.println("Mail Response:" + responce);
///South Zone End
///ACNE Start
//                recievers = "gaurang@dineshmills.com";
                recievers = "bakhtyarb@dineshmills.com";
                sql = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING as \"Total Pending Order\" ,NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",PENDING - OC_MAY as \"Pending UnConfirmed Order\" ,OC_MAY AS \"Pending Confirmed Order\",DESPATCH as \"Despatched\",OC_MAY+DESPATCH AS \"Pending Confirmed Order + Despatched (OCD)\",PERCENTAGE AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE as \"(Sales Projection + Prev.GR) * " + mcurmnth + "%\",OC_PRIORITY AS \"Priority (BP > OCD) \","
                        + "CTG AS \"Budgeted/Non Bugdeted\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + " FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT WHERE OC_PRIORITY ='YES' AND ZONE_INCHARGE='ACNE' "
                        + " ORDER BY ZONE_INCHARGE,UPN";
                sql1 = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING as \"Total Pending Order\" ,NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",PENDING - OC_MAY as \"Pending UnConfirmed Order\" ,OC_MAY AS \"Pending Confirmed Order\",DESPATCH as \"Despatched\",OC_MAY+DESPATCH AS \"Pending Confirmed Order + Despatched (OCD)\",PERCENTAGE AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE as \"(Sales Projection + Prev.GR) * " + mcurmnth + "%\",OC_PRIORITY AS \"Priority (BP > OCD) \","
                        + "CTG AS \"Budgeted/Non Bugdeted\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + " FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT WHERE ZONE_INCHARGE='ACNE'"
                        + " ORDER BY ZONE_INCHARGE,UPN";

//                exprt.fillData(sql1 + "#" + sql, new File("/Email_Attachment/CUSTOMER_ORDER_CONFIRMATION_PRIORITY_ACNE.xls"), "ALL#CP");
//                responce = sendNotificationMailWithAttachement("Customer Order Confirmation Priority Report For " + mdate, "", recievers, "sdmlerp@dineshmills.com", "/Email_Attachment/CUSTOMER_ORDER_CONFIRMATION_PRIORITY_ACNE.xls", "CUSTOMER_ORDER_CONFIRMATION_PRIORITY_ACNE.xls");
//                System.out.println("Mail Response:" + responce);
///ACNE End
///Key Client Start
//                recievers = "gaurang@dineshmills.com";
                recievers = "manojgupta@dineshmills.com";
                sql = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING as \"Total Pending Order\" ,NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",PENDING - OC_MAY as \"Pending UnConfirmed Order\" ,OC_MAY AS \"Pending Confirmed Order\",DESPATCH as \"Despatched\",OC_MAY+DESPATCH AS \"Pending Confirmed Order + Despatched (OCD)\",PERCENTAGE AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE as \"(Sales Projection + Prev.GR) * " + mcurmnth + "%\",OC_PRIORITY AS \"Priority (BP > OCD) \","
                        + "CTG AS \"Budgeted/Non Bugdeted\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + " FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT WHERE OC_PRIORITY ='YES' AND ZONE_INCHARGE='KEY CLIENT' "
                        + " ORDER BY ZONE_INCHARGE,UPN";
                sql1 = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING as \"Total Pending Order\" ,NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",PENDING - OC_MAY as \"Pending UnConfirmed Order\" ,OC_MAY AS \"Pending Confirmed Order\",DESPATCH as \"Despatched\",OC_MAY+DESPATCH AS \"Pending Confirmed Order + Despatched (OCD)\",PERCENTAGE AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE as \"(Sales Projection + Prev.GR) * " + mcurmnth + "%\",OC_PRIORITY AS \"Priority (BP > OCD) \","
                        + "CTG AS \"Budgeted/Non Bugdeted\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + " FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT WHERE ZONE_INCHARGE='KEY CLIENT'"
                        + " ORDER BY ZONE_INCHARGE,UPN";

//                exprt.fillData(sql1 + "#" + sql, new File("/Email_Attachment/CUSTOMER_ORDER_CONFIRMATION_PRIORITY_KEYCLIENT.xls"), "ALL#CP");
//                responce = sendNotificationMailWithAttachement("Customer Order Confirmation Priority Report For " + mdate, "", recievers, "sdmlerp@dineshmills.com", "/Email_Attachment/CUSTOMER_ORDER_CONFIRMATION_PRIORITY_KEYCLIENT.xls", "CUSTOMER_ORDER_CONFIRMATION_PRIORITY_KEYCLIENT.xls");
//                System.out.println("Mail Response:" + responce);
///Key Client End
                //For Customer Order Follow-up Report
                Query_1();
                data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                        + "SET PER_SDF  = CASE WHEN CURRENT_MONTH IN (2) THEN 1 WHEN CURRENT_MONTH IN (3) THEN 0.33 "
                        + "WHEN CURRENT_MONTH IN (4) THEN 0.42 WHEN CURRENT_MONTH IN (5) THEN 0.50 "
                        + "WHEN CURRENT_MONTH IN (6) THEN 0.58 WHEN CURRENT_MONTH IN (7) THEN 0.67 "
                        + "WHEN CURRENT_MONTH IN (8) THEN 0.75 WHEN CURRENT_MONTH IN (9) THEN 0.83 "
                        + " WHEN CURRENT_MONTH IN (10) THEN 0.92 WHEN CURRENT_MONTH IN (11,12,1) THEN 1 END "
                        + " WHERE  BUDGET_FINYEAR = '2020-2021'  AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP='SDF' ");
                data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                        + "SET PER_OTHER_SDF  = CASE WHEN CURRENT_MONTH IN (2) THEN 0.33 WHEN CURRENT_MONTH IN (3) THEN 0.42 "
                        + "WHEN CURRENT_MONTH IN (4) THEN 0.50 WHEN CURRENT_MONTH IN (5) THEN 0.58 "
                        + "WHEN CURRENT_MONTH IN (6) THEN 0.67 WHEN CURRENT_MONTH IN (7) THEN 0.75 "
                        + "WHEN CURRENT_MONTH IN (8) THEN 0.83 WHEN CURRENT_MONTH IN (9) THEN 0.92 "
                        + " WHEN CURRENT_MONTH IN (10,11,12,1) THEN 1 END "
                        + " WHERE  BUDGET_FINYEAR = '2020-2021'  AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP!='SDF' ");
                Query2();

//                recievers = "rishineekhra@dineshmills.com";
                recievers = "aditya@dineshmills.com,soumen@dineshmills.com,vdshanbhag@dineshmills.com,rishineekhra@dineshmills.com";
                sql = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PER_SDF AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE_SDF AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND_SDF AS \"Priority PD >SP\","
                        + "SCOPE_QTY_SDF,SCOPE_KG_SDF,SCOPE_VALUE_SDF,"
                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT  WHERE PRIORITY_IND_SDF ='YES' "
                        + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP='SDF' "
                        + "ORDER BY ZONE_INCHARGE,UPN";

                System.out.println("SQLDRP" + sql);
                sql1 = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PER_SDF AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE_SDF AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND_SDF AS \"Priority PD >SP\","
                        + "SCOPE_QTY_SDF,SCOPE_KG_SDF,SCOPE_VALUE_SDF,"
                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                        + " WHERE PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP='SDF' "
                        + "ORDER BY ZONE_INCHARGE,UPN";

                sql2 = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PER_OTHER_SDF AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE_OTHER_SDF AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND_OTHER_SDF AS \"Priority PD >SP\","
                        + "SCOPE_QTY_OTHER_SDF,SCOPE_KG_OTHER_SDF,SCOPE_VALUE_OTHER_SDF,"
                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT  WHERE PRIORITY_IND_OTHER_SDF ='YES' "
                        + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP!='SDF' "
                        + "ORDER BY ZONE_INCHARGE,UPN";

                System.out.println("SQL2" + sql2);
                sql3 = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PER_OTHER_SDF AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE_OTHER_SDF AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND_OTHER_SDF AS \"Priority PD >SP\","
                        + "SCOPE_QTY_OTHER_SDF,SCOPE_KG_OTHER_SDF,SCOPE_VALUE_OTHER_SDF,"
                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT  "
                        + "WHERE   PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP!='SDF' "
                        + "ORDER BY ZONE_INCHARGE,UPN";

                //String
                //recievers = "dharmendra@dineshmills.com";
                exprt.fillData(sql3 + "#" + sql2 + "#" + sql1 + "#" + sql, new File("/Email_Attachment/CUSTOMER_ORDER_FOLLOW_UP_PRIORITY_REPORT.xls"), "ALL_OTHR_SDF#CP_OTHR_SDF#ALL_SDF#CP_SDF");
                responce = sendNotificationMailWithAttachement("Customer Order Follow-up Priority Report For " + mdate, "", recievers, "sdmlerp@dineshmills.com", "/Email_Attachment/CUSTOMER_ORDER_FOLLOW_UP_PRIORITY_REPORT.xls", "CUSTOMER_ORDER_FOLLOW_UP_PRIORITY_REPORT.xls");
                System.out.println("Mail Response:" + responce);

                ////North Zone Start
                sql = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PER_SDF AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE_SDF AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND_SDF AS \"Priority PD >SP\","
                        + "SCOPE_QTY_SDF,SCOPE_KG_SDF,SCOPE_VALUE_SDF,"
                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT  WHERE ZONE_INCHARGE='NORTH' AND PRIORITY_IND_SDF ='YES' "
                        + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP='SDF' "
                        + "ORDER BY ZONE_INCHARGE,UPN";

                System.out.println("SQLDRP" + sql);
                sql1 = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PER_SDF AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE_SDF AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND_SDF AS \"Priority PD >SP\","
                        + "SCOPE_QTY_SDF,SCOPE_KG_SDF,SCOPE_VALUE_SDF,"
                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                        + " WHERE ZONE_INCHARGE='NORTH' AND  PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP='SDF' "
                        + "ORDER BY ZONE_INCHARGE,UPN";

                sql2 = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PER_OTHER_SDF AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE_OTHER_SDF AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND_OTHER_SDF AS \"Priority PD >SP\","
                        + "SCOPE_QTY_OTHER_SDF,SCOPE_KG_OTHER_SDF,SCOPE_VALUE_OTHER_SDF,"
                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT  WHERE ZONE_INCHARGE='NORTH' AND  PRIORITY_IND_OTHER_SDF ='YES' "
                        + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP!='SDF' "
                        + "ORDER BY ZONE_INCHARGE,UPN";

                System.out.println("SQL2" + sql2);
                sql3 = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PER_OTHER_SDF AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE_OTHER_SDF AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND_OTHER_SDF AS \"Priority PD >SP\","
                        + "SCOPE_QTY_OTHER_SDF,SCOPE_KG_OTHER_SDF,SCOPE_VALUE_OTHER_SDF,"
                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT  "
                        + "WHERE ZONE_INCHARGE='NORTH' AND    PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP!='SDF' "
                        + "ORDER BY ZONE_INCHARGE,UPN";
//                sql = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
//                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PERCENTAGE AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND AS \"Priority PD >SP\","
//                        + "SCOPE_QTY,SCOPE_KG,SCOPE_VALUE,"
//                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
//                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT WHERE ZONE_INCHARGE='NORTH' AND PRIORITY_IND ='YES' "
//                        + "ORDER BY ZONE_INCHARGE,UPN";
//                sql1 = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
//                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PERCENTAGE AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND AS \"Priority PD >SP\","
//                        + "SCOPE_QTY,SCOPE_KG,SCOPE_VALUE,"
//                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
//                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT WHERE ZONE_INCHARGE='NORTH' "
//                        + "ORDER BY ZONE_INCHARGE,UPN";

//                recievers = "gaurang@dineshmills.com";
                recievers = "mitanglad@dineshmills.com";
                exprt.fillData(sql3 + "#" + sql2 + "#" + sql1 + "#" + sql, new File("/Email_Attachment/CUSTOMER_ORDER_FOLLOW_UP_PRIORITY_NORTH.xls"), "ALL_OTHR_SDF#CP_OTHR_SDF#ALL_SDF#CP_SDF");
                //exprt.fillData(sql1 + "#" + sql, new File("/Email_Attachment/CUSTOMER_ORDER_FOLLOW_UP_PRIORITY_NORTH.xls"), "ALL#CP");
                responce = sendNotificationMailWithAttachement("Customer Order Follow-up Priority Report For " + mdate, "", recievers, "sdmlerp@dineshmills.com", "/Email_Attachment/CUSTOMER_ORDER_FOLLOW_UP_PRIORITY_NORTH.xls", "CUSTOMER_ORDER_FOLLOW_UP_PRIORITY_NORTH.xls");
                System.out.println("Mail Response:" + responce);

                ////North Zone End
                ///EastWest Zone Start
                sql = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PER_SDF AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE_SDF AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND_SDF AS \"Priority PD >SP\","
                        + "SCOPE_QTY_SDF,SCOPE_KG_SDF,SCOPE_VALUE_SDF,"
                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT  WHERE ZONE_INCHARGE='EAST/WEST' AND PRIORITY_IND_SDF ='YES' "
                        + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP='SDF' "
                        + "ORDER BY ZONE_INCHARGE,UPN";

                System.out.println("SQLDRP" + sql);
                sql1 = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PER_SDF AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE_SDF AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND_SDF AS \"Priority PD >SP\","
                        + "SCOPE_QTY_SDF,SCOPE_KG_SDF,SCOPE_VALUE_SDF,"
                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                        + " WHERE ZONE_INCHARGE='EAST/WEST' AND  PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP='SDF' "
                        + "ORDER BY ZONE_INCHARGE,UPN";

                sql2 = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PER_OTHER_SDF AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE_OTHER_SDF AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND_OTHER_SDF AS \"Priority PD >SP\","
                        + "SCOPE_QTY_OTHER_SDF,SCOPE_KG_OTHER_SDF,SCOPE_VALUE_OTHER_SDF,"
                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT  WHERE ZONE_INCHARGE='EAST/WEST' AND  PRIORITY_IND_OTHER_SDF ='YES' "
                        + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP!='SDF' "
                        + "ORDER BY ZONE_INCHARGE,UPN";

                System.out.println("SQL2" + sql2);
                sql3 = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PER_OTHER_SDF AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE_OTHER_SDF AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND_OTHER_SDF AS \"Priority PD >SP\","
                        + "SCOPE_QTY_OTHER_SDF,SCOPE_KG_OTHER_SDF,SCOPE_VALUE_OTHER_SDF,"
                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT  "
                        + "WHERE ZONE_INCHARGE='EAST/WEST' AND    PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP!='SDF' "
                        + "ORDER BY ZONE_INCHARGE,UPN";

//                sql = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
//                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PERCENTAGE AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND AS \"Priority PD >SP\","
//                        + "SCOPE_QTY,SCOPE_KG,SCOPE_VALUE,"
//                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
//                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT WHERE ZONE_INCHARGE='EAST/WEST' AND PRIORITY_IND ='YES' "
//                        + "ORDER BY ZONE_INCHARGE,UPN";
//                sql1 = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
//                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PERCENTAGE AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND AS \"Priority PD >SP\","
//                        + "SCOPE_QTY,SCOPE_KG,SCOPE_VALUE,"
//                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
//                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT WHERE ZONE_INCHARGE='EAST/WEST' "
//                        + "ORDER BY ZONE_INCHARGE,UPN";
//                recievers = "gaurang@dineshmills.com";
                recievers = "chinmoyghosh@dineshmills.com";
                exprt.fillData(sql3 + "#" + sql2 + "#" + sql1 + "#" + sql, new File("/Email_Attachment/CUSTOMER_ORDER_FOLLOW_UP_PRIORITY_EASTWEST.xls"), "ALL_OTHR_SDF#CP_OTHR_SDF#ALL_SDF#CP_SDF");
                //exprt.fillData(sql1 + "#" + sql, new File("/Email_Attachment/CUSTOMER_ORDER_FOLLOW_UP_PRIORITY_EASTWEST.xls"), "ALL#CP");
                responce = sendNotificationMailWithAttachement("Customer Order Follow-up Priority Report For " + mdate, "", recievers, "sdmlerp@dineshmills.com", "/Email_Attachment/CUSTOMER_ORDER_FOLLOW_UP_PRIORITY_EASTWEST.xls", "CUSTOMER_ORDER_FOLLOW_UP_PRIORITY_EASTWEST.xls");
                System.out.println("Mail Response:" + responce);

                ///EastWest Zone End
                ///South Zone Start
                sql = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PER_SDF AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE_SDF AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND_SDF AS \"Priority PD >SP\","
                        + "SCOPE_QTY_SDF,SCOPE_KG_SDF,SCOPE_VALUE_SDF,"
                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT  WHERE ZONE_INCHARGE='SOUTH' AND PRIORITY_IND_SDF ='YES' "
                        + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP='SDF' "
                        + "ORDER BY ZONE_INCHARGE,UPN";

                System.out.println("SQLDRP" + sql);
                sql1 = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PER_SDF AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE_SDF AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND_SDF AS \"Priority PD >SP\","
                        + "SCOPE_QTY_SDF,SCOPE_KG_SDF,SCOPE_VALUE_SDF,"
                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                        + " WHERE ZONE_INCHARGE='SOUTH' AND  PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP='SDF' "
                        + "ORDER BY ZONE_INCHARGE,UPN";

                sql2 = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PER_OTHER_SDF AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE_OTHER_SDF AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND_OTHER_SDF AS \"Priority PD >SP\","
                        + "SCOPE_QTY_OTHER_SDF,SCOPE_KG_OTHER_SDF,SCOPE_VALUE_OTHER_SDF,"
                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT  WHERE ZONE_INCHARGE='SOUTH' AND  PRIORITY_IND_OTHER_SDF ='YES' "
                        + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP!='SDF' "
                        + "ORDER BY ZONE_INCHARGE,UPN";

                System.out.println("SQL2" + sql2);
                sql3 = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PER_OTHER_SDF AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE_OTHER_SDF AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND_OTHER_SDF AS \"Priority PD >SP\","
                        + "SCOPE_QTY_OTHER_SDF,SCOPE_KG_OTHER_SDF,SCOPE_VALUE_OTHER_SDF,"
                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT  "
                        + "WHERE ZONE_INCHARGE='SOUTH' AND    PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP!='SDF' "
                        + "ORDER BY ZONE_INCHARGE,UPN";

//                sql = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
//                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PERCENTAGE AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND AS \"Priority PD >SP\","
//                        + "SCOPE_QTY,SCOPE_KG,SCOPE_VALUE,"
//                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
//                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT WHERE ZONE_INCHARGE='SOUTH' AND PRIORITY_IND ='YES' "
//                        + "ORDER BY ZONE_INCHARGE,UPN";
//                sql1 = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
//                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PERCENTAGE AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND AS \"Priority PD >SP\","
//                        + "SCOPE_QTY,SCOPE_KG,SCOPE_VALUE,"
//                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
//                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT WHERE ZONE_INCHARGE='SOUTH' "
//                        + "ORDER BY ZONE_INCHARGE,UPN";
//                recievers = "gaurang@dineshmills.com";
                recievers = "siddharthneogi@dineshmills.com";
                exprt.fillData(sql3 + "#" + sql2 + "#" + sql1 + "#" + sql, new File("/Email_Attachment/CUSTOMER_ORDER_FOLLOW_UP_PRIORITY_SOUTH.xls"), "ALL_OTHR_SDF#CP_OTHR_SDF#ALL_SDF#CP_SDF");
                //exprt.fillData(sql1 + "#" + sql, new File("/Email_Attachment/CUSTOMER_ORDER_FOLLOW_UP_PRIORITY_SOUTH.xls"), "ALL#CP");
                responce = sendNotificationMailWithAttachement("Customer Order Follow-up Priority Report For " + mdate, "", recievers, "sdmlerp@dineshmills.com", "/Email_Attachment/CUSTOMER_ORDER_FOLLOW_UP_PRIORITY_SOUTH.xls", "CUSTOMER_ORDER_FOLLOW_UP_PRIORITY_SOUTH.xls");
                System.out.println("Mail Response:" + responce);

                ///South Zone End
                ///ACNE Start
                sql = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PER_SDF AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE_SDF AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND_SDF AS \"Priority PD >SP\","
                        + "SCOPE_QTY_SDF,SCOPE_KG_SDF,SCOPE_VALUE_SDF,"
                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT  WHERE ZONE_INCHARGE='ACNE' AND PRIORITY_IND_SDF ='YES' "
                        + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP='SDF' "
                        + "ORDER BY ZONE_INCHARGE,UPN";

                System.out.println("SQLDRP" + sql);
                sql1 = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PER_SDF AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE_SDF AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND_SDF AS \"Priority PD >SP\","
                        + "SCOPE_QTY_SDF,SCOPE_KG_SDF,SCOPE_VALUE_SDF,"
                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                        + " WHERE ZONE_INCHARGE='ACNE' AND  PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP='SDF' "
                        + "ORDER BY ZONE_INCHARGE,UPN";

                sql2 = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PER_OTHER_SDF AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE_OTHER_SDF AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND_OTHER_SDF AS \"Priority PD >SP\","
                        + "SCOPE_QTY_OTHER_SDF,SCOPE_KG_OTHER_SDF,SCOPE_VALUE_OTHER_SDF,"
                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT  WHERE ZONE_INCHARGE='ACNE' AND  PRIORITY_IND_OTHER_SDF ='YES' "
                        + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP!='SDF' "
                        + "ORDER BY ZONE_INCHARGE,UPN";

                System.out.println("SQL2" + sql2);
                sql3 = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PER_OTHER_SDF AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE_OTHER_SDF AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND_OTHER_SDF AS \"Priority PD >SP\","
                        + "SCOPE_QTY_OTHER_SDF,SCOPE_KG_OTHER_SDF,SCOPE_VALUE_OTHER_SDF,"
                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT  "
                        + "WHERE ZONE_INCHARGE='ACNE' AND    PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP!='SDF' "
                        + "ORDER BY ZONE_INCHARGE,UPN";

//                sql = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
//                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PERCENTAGE AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND AS \"Priority PD >SP\","
//                        + "SCOPE_QTY,SCOPE_KG,SCOPE_VALUE,"
//                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
//                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT WHERE ZONE_INCHARGE='ACNE' AND PRIORITY_IND ='YES' "
//                        + "ORDER BY ZONE_INCHARGE,UPN";
//                sql1 = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
//                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PERCENTAGE AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND AS \"Priority PD >SP\","
//                        + "SCOPE_QTY,SCOPE_KG,SCOPE_VALUE,"
//                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
//                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT WHERE ZONE_INCHARGE='ACNE' "
//                        + "ORDER BY ZONE_INCHARGE,UPN";
//                recievers = "gaurang@dineshmills.com";
                recievers = "bakhtyarb@dineshmills.com";
                exprt.fillData(sql3 + "#" + sql2 + "#" + sql1 + "#" + sql, new File("/Email_Attachment/CUSTOMER_ORDER_FOLLOW_UP_PRIORITY_ACNE.xls"), "ALL_OTHR_SDF#CP_OTHR_SDF#ALL_SDF#CP_SDF");
                //exprt.fillData(sql1 + "#" + sql, new File("/Email_Attachment/CUSTOMER_ORDER_FOLLOW_UP_PRIORITY_ACNE.xls"), "ALL#CP");
                responce = sendNotificationMailWithAttachement("Customer Order Follow-up Priority Report For " + mdate, "", recievers, "sdmlerp@dineshmills.com", "/Email_Attachment/CUSTOMER_ORDER_FOLLOW_UP_PRIORITY_ACNE.xls", "CUSTOMER_ORDER_FOLLOW_UP_PRIORITY_ACNE.xls");
                System.out.println("Mail Response:" + responce);

                ///ACNE End
                ///Key Client Start
                sql = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PER_SDF AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE_SDF AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND_SDF AS \"Priority PD >SP\","
                        + "SCOPE_QTY_SDF,SCOPE_KG_SDF,SCOPE_VALUE_SDF,"
                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT  WHERE ZONE_INCHARGE='KEY CLIENT' AND PRIORITY_IND_SDF ='YES' "
                        + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP='SDF' "
                        + "ORDER BY ZONE_INCHARGE,UPN";

                System.out.println("SQLDRP" + sql);
                sql1 = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PER_SDF AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE_SDF AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND_SDF AS \"Priority PD >SP\","
                        + "SCOPE_QTY_SDF,SCOPE_KG_SDF,SCOPE_VALUE_SDF,"
                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                        + " WHERE ZONE_INCHARGE='KEY CLIENT' AND  PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP='SDF' "
                        + "ORDER BY ZONE_INCHARGE,UPN";

                sql2 = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PER_OTHER_SDF AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE_OTHER_SDF AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND_OTHER_SDF AS \"Priority PD >SP\","
                        + "SCOPE_QTY_OTHER_SDF,SCOPE_KG_OTHER_SDF,SCOPE_VALUE_OTHER_SDF,"
                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT  WHERE ZONE_INCHARGE='KEY CLIENT' AND  PRIORITY_IND_OTHER_SDF ='YES' "
                        + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP!='SDF' "
                        + "ORDER BY ZONE_INCHARGE,UPN";

                System.out.println("SQL2" + sql2);
                sql3 = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",NCEWIP_P_YEAR+NCESTK_P_YEAR AS \"PENDING PROJECTED ORDER\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PER_OTHER_SDF AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE_OTHER_SDF AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND_OTHER_SDF AS \"Priority PD >SP\","
                        + "SCOPE_QTY_OTHER_SDF,SCOPE_KG_OTHER_SDF,SCOPE_VALUE_OTHER_SDF,"
                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT  "
                        + "WHERE ZONE_INCHARGE='KEY CLIENT' AND    PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP!='SDF' "
                        + "ORDER BY ZONE_INCHARGE,UPN";

//                sql = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
//                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PERCENTAGE AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND AS \"Priority PD >SP\","
//                        + "SCOPE_QTY,SCOPE_KG,SCOPE_VALUE,"
//                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
//                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT WHERE ZONE_INCHARGE='KEY CLIENT' AND PRIORITY_IND ='YES' "
//                        + "ORDER BY ZONE_INCHARGE,UPN";
//                sql1 = "SELECT ZONE_INCHARGE AS Zone,CONCAT(\"'\",UPN) AS UPN,SUBSTRING(UPN,7,2) AS \"Machine No\",POSITION_DESC1 AS \"Position Description\",PARTY_CODE,PARTY_NAME,"
//                        + "BUDGET AS \"Current Sales Projection\",COALESCE(PREV_GR,0) AS \"Previous_GR\",PENDING AS \"Total Pending Order\",DESPATCH AS Despatched,PENDING+DESPATCH AS \"Total Pending order + Despatched (PD)\",PERCENTAGE AS \"" + mcurmnth + "%\",BUDGET_PER_VALUE AS \"(Current Sales Projection +Prev.GR ) *" + mcurmnth + "% (SP)\",PRIORITY_IND AS \"Priority PD >SP\","
//                        + "SCOPE_QTY,SCOPE_KG,SCOPE_VALUE,"
//                        + "CTG AS \"Budget(B)/NonBudgeted(NB)\",DATE_FORMAT(SYSDATE(),'%d/%m/%Y %H:%S') AS RUNDATE "
//                        + "FROM PRODUCTION.CUSTOMER_PRIORITY_REPORT WHERE ZONE_INCHARGE='KEY CLIENT' "
//                        + "ORDER BY ZONE_INCHARGE,UPN";
//                recievers = "gaurang@dineshmills.com";
                recievers = "manojgupta@dineshmills.com";
                exprt.fillData(sql3 + "#" + sql2 + "#" + sql1 + "#" + sql, new File("/Email_Attachment/CUSTOMER_ORDER_FOLLOW_UP_PRIORITY_KEYCLIENT.xls"), "ALL_OTHR_SDF#CP_OTHR_SDF#ALL_SDF#CP_SDF");
                //exprt.fillData(sql1 + "#" + sql, new File("/Email_Attachment/CUSTOMER_ORDER_FOLLOW_UP_PRIORITY_KEYCLIENT.xls"), "ALL#CP");
                responce = sendNotificationMailWithAttachement("Customer Order Follow-up Priority Report For " + mdate, "", recievers, "sdmlerp@dineshmills.com", "/Email_Attachment/CUSTOMER_ORDER_FOLLOW_UP_PRIORITY_KEYCLIENT.xls", "CUSTOMER_ORDER_FOLLOW_UP_PRIORITY_KEYCLIENT.xls");
                System.out.println("Mail Response:" + responce);

                ///Key Client End
                //Other than SDF End
            }
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

    public static void writeToZipFile(String path, ZipOutputStream zipStream) throws FileNotFoundException, IOException {
        System.out.println("Writing file : '" + path + "' to zip file");
        File aFile = new File(path);
        FileInputStream fis = new FileInputStream(aFile);
        ZipEntry zipEntry = new ZipEntry(path);
        zipStream.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipStream.write(bytes, 0, length);
        }
        zipStream.closeEntry();
        fis.close();
    }

    public static void Query_1_Old() {
        data.Execute("TRUNCATE TABLE PRODUCTION.CUSTOMER_PRIORITY_REPORT");
        data.Execute("INSERT INTO  PRODUCTION.CUSTOMER_PRIORITY_REPORT (BUDGET_FINYEAR,PARTY_CODE,UPN,CTG,LENGTH,WIDTH,GSM,WEIGHT,SQMTR) "
                + "SELECT DISTINCT '2020-2021'AS FY,PARTY_CODE,UPN,'B',PRESS_LENGTH+DRY_LENGTH AS LENGTH,PRESS_WIDTH+DRY_WIDTH AS WIDTH,PRESS_GSM AS GSM,DRY_WEIGHT+PRESS_WEIGHT AS WEIGHT ,PRESS_SQMTR + DRY_SQMTR FROM PRODUCTION.FELT_BUDGET_DETAIL WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021");
        data.Execute("INSERT INTO  PRODUCTION.CUSTOMER_PRIORITY_REPORT (BUDGET_FINYEAR,PARTY_CODE,UPN,CTG,LENGTH,WIDTH,GSM,WEIGHT,SQMTR) "
                + "SELECT DISTINCT  '2020-2021'AS FY,D.MM_PARTY_CODE,MM_UPN_NO,'NB',MM_FELT_LENGTH+MM_FABRIC_LENGTH,MM_FELT_WIDTH+MM_FABRIC_WIDTH,MM_FELT_GSM,MM_FELT_WEIGHT,MM_SIZE_M2 FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL D,PRODUCTION.FELT_MACHINE_MASTER_HEADER H "
                + "WHERE H.MM_DOC_NO = D.MM_DOC_NO AND POSITION_CLOSE_IND !=1 AND H.APPROVED =1 AND H.CANCELED =0 AND SUBSTRING(D.MM_PARTY_CODE,1,1) =8 "
                + "AND MM_UPN_NO NOT IN (SELECT UPN FROM PRODUCTION.FELT_BUDGET_DETAIL WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021)");

        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,(SELECT PR_UPN,COUNT(PR_UPN) AS PGR,SUM(NET_AMOUNT) AS PGRVAL,SUM(ACTUAL_WEIGHT) AS PGRKG FROM (SELECT PIECE_NO,NET_AMOUNT,ACTUAL_WEIGHT FROM PRODUCTION.FELT_SALES_RETURNS_DETAIL D,PRODUCTION.FELT_SALES_RETURNS_HEADER H "
                + " WHERE H.DOC_NO=D.DOC_NO AND COALESCE(H.APPROVED,0)=1 AND COALESCE(H.CANCELED,0)=0 "
                + " AND INVOICE_DATE<'2020-04-01' AND H.DOC_DATE>='2020-04-01' AND H.DOC_DATE<='2021-03-31') AS A "
                + " LEFT JOIN (SELECT PR_PIECE_NO,PR_UPN FROM PRODUCTION.FELT_SALES_PIECE_REGISTER) AS P "
                + " ON PIECE_NO=PR_PIECE_NO "
                + " GROUP BY PR_UPN) AS G "
                + " SET PREV_GR=PGR,PREV_GR_KG=PGRKG,PREV_GR_VALUE=PGRVAL "
                + " WHERE UPN=PR_UPN");
        data.Execute("INSERT INTO PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + " (PARTY_CODE,PARTY_NAME,BUDGET_FINYEAR,UPN,PREV_GR,PREV_GR_KG,PREV_GR_VALUE,PRIORITY_IND,BUDGET_PER_VALUE) "
                + " SELECT PARTY_CODE,PARTY_NAME,'2020-2021',PR_UPN,COUNT(PR_UPN) AS PGR,SUM(ACTUAL_WEIGHT) AS PGRKG,SUM(NET_AMOUNT) AS PGRVAL,'NO',0 FROM "
                + " (SELECT PIECE_NO,NET_AMOUNT,ACTUAL_WEIGHT,PARTY_CODE,PARTY_NAME FROM PRODUCTION.FELT_SALES_RETURNS_DETAIL D,PRODUCTION.FELT_SALES_RETURNS_HEADER H "
                + " WHERE H.DOC_NO=D.DOC_NO AND COALESCE(H.APPROVED,0)=1 AND COALESCE(H.CANCELED,0)=0 "
                + " AND INVOICE_DATE<'2020-04-01' AND H.DOC_DATE>='2020-04-01' AND H.DOC_DATE<='2021-03-31') AS A "
                + " LEFT JOIN (SELECT PR_PIECE_NO,PR_UPN FROM PRODUCTION.FELT_SALES_PIECE_REGISTER) AS P "
                + " ON PIECE_NO=PR_PIECE_NO "
                + " WHERE PR_UPN NOT IN (SELECT UPN FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE LEFT(DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2))) "
                + " GROUP BY PR_UPN");

        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,DINESHMILLS.D_SAL_PARTY_MASTER P "
                + "SET R.PARTY_NAME = P.PARTY_NAME, R.ZONE = P.INCHARGE_CD,R.CITY=P.CITY_ID,R.ZONE=INCHARGE_CD "
                + "WHERE R.PARTY_CODE = P.PARTY_CODE");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,PRODUCTION.FELT_BUDGET_REVIEW_DETAIL P "
                + "SET R.BUDGET=P.CURRENT_PROJECTION "
                + "WHERE R.UPN = P.UPN AND P.YEAR_FROM=2020 AND COALESCE(APPROVED,0) =1 AND COALESCE(CANCELED,0) =0 AND LEFT(DOC_NO,7)=CONCAT('B2021',RIGHT(100+MONTH(curdate()),2))");

        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + " SET BUDGET_PREV_GR=COALESCE(BUDGET,0)+COALESCE(PREV_GR,0) "
                + " WHERE  BUDGET_FINYEAR = '2020-2021'");;

        data.Execute("UPDATE PRODUCTION.FELT_MACHINE_POSITION_MST M, PRODUCTION.CUSTOMER_PRIORITY_REPORT R "
                + "SET R.POSITION_DESC1 = M.POSITION_DESC "
                + "WHERE RIGHT(UPN,4) = POSITION_DESIGN_NO");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,PRODUCTION.FELT_INCHARGE "
                + "SET R.ZONE_INCHARGE=INCHARGE_NAME "
                + "WHERE ZONE=INCHARGE_CD");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,( "
                + "SELECT PR_UPN,COUNT(PR_PIECE_NO) AS C FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                + "WHERE PR_PIECE_STAGE IN ('PLANNING','BSR','IN STOCK','BOOKING','WEAVING','MENDING','NEEDLING','FINISHING','SPIRALLING','SEAMING','ASSEMBLY','HEAT_SETTING','MARKING','SPLICING')  "
                + "AND COALESCE(PR_DELINK,'')!='OBSOLETE'  "
                + "AND ((PR_REQ_MTH_LAST_DDMMYY >='2020-04-01'  AND PR_REQ_MTH_LAST_DDMMYY <='2021-03-31')  "
                + "OR (PR_CURRENT_SCH_LAST_DDMMYY >='2020-04-30')) "
                + "GROUP BY PR_UPN "
                + ") AS P "
                + "SET PENDING=C "
                + "WHERE R.UPN=P.PR_UPN");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,( "
                + "SELECT PR_UPN,SUM(NO_OF_PIECES) AS C FROM PRODUCTION.FELT_SAL_INVOICE_HEADER I,PRODUCTION.FELT_SALES_PIECE_REGISTER PR WHERE INVOICE_DATE >= '2020-04-01' AND INVOICE_DATE <='2021-03-31' AND APPROVED =1 AND CANCELLED =0 AND I.PIECE_NO = PR.PR_PIECE_NO "
                + " AND PIECE_NO NOT IN (SELECT PIECE_NO FROM PRODUCTION.FELT_SALES_RETURNS_DETAIL D,PRODUCTION.FELT_SALES_RETURNS_HEADER H WHERE H.DOC_NO=D.DOC_NO AND COALESCE(H.APPROVED,0)=1 AND COALESCE(H.CANCELED,0)=0 )"
                + "GROUP BY PR_UPN "
                + ") AS P "
                + "SET DESPATCH=C "
                + "WHERE R.UPN=P.PR_UPN");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT D,(SELECT PR_PARTY_CODE AS PARTY_CODE,PR_MACHINE_NO AS MACHINE_NO,PR_POSITION_NO AS POSITION_NO,"
                + "COUNT(FELT_AMEND_PIECE_NO) AS INVQTY,SUM(FELT_AMEND_EXPORT_INV_AMT) AS INVAMT  FROM PRODUCTION.FELT_PIECE_AMEND,PRODUCTION.FELT_SALES_PIECE_REGISTER "
                + "WHERE FELT_AMEND_REASON =8 AND PR_PIECE_NO = FELT_AMEND_PIECE_NO AND FELT_AMEND_EXPORT_INV_DATE >= '2020-04-01' "
                + "GROUP BY PR_PARTY_CODE,PR_MACHINE_NO,PR_POSITION_NO) AS I "
                + "SET DESPATCH=I.INVQTY "
                + "WHERE  LEFT(D.DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2)) "
                + "AND D.PARTY_CODE=I.PARTY_CODE AND D.MACHINE_NO=I.MACHINE_NO AND D.POSITION_NO=I.POSITION_NO");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,( "
                + "SELECT PR_UPN,COUNT(PR_PIECE_NO) AS C FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                + "WHERE PR_PIECE_STAGE IN ('PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SPIRALLING','SEAMING','ASSEMBLY','IN STOCK','BSR','HEAT_SETTING','MARKING','SPLICING')  "
                + "AND COALESCE(PR_DELINK,'')!='OBSOLETE'  "
                + "AND ((PR_OC_LAST_DDMMYY >= '2020-04-01' AND  PR_OC_LAST_DDMMYY <= '2021-03-31'AND PR_OC_LAST_DDMMYY  != '0000-00-00') "
                + "OR (PR_CURRENT_SCH_LAST_DDMMYY >='2020-04-30')) "
                + "GROUP BY PR_UPN  "
                + ") AS P "
                + "SET OC_MAY=C "
                + "WHERE R.UPN=P.PR_UPN");
        data.Execute("UPDATE  PRODUCTION.CUSTOMER_PRIORITY_REPORT SET BUDGET = PENDING,BUDGET_PREV_GR=PENDING+COALESCE(PREV_GR,0) WHERE  BUDGET_FINYEAR = '2020-2021' AND CTG = 'NB' AND BUDGET =0");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT SET CURRENT_MONTH = MONTH(curdate()) WHERE  BUDGET_FINYEAR = '2020-2021'");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D "
                + "SET R.GROUP=D.GROUP_NAME "
                + "WHERE D.UPN=R.UPN AND LEFT(D.DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2))");
    }

    public static void Query_1() {
        data.Execute("TRUNCATE TABLE PRODUCTION.CUSTOMER_PRIORITY_REPORT");
        data.Execute("INSERT INTO  PRODUCTION.CUSTOMER_PRIORITY_REPORT (BUDGET_FINYEAR,PARTY_CODE,UPN,CTG,LENGTH,WIDTH,GSM,WEIGHT,SQMTR) "
                + "SELECT DISTINCT '2020-2021'AS FY,PARTY_CODE,UPN,'B',PRESS_LENGTH+DRY_LENGTH AS LENGTH,PRESS_WIDTH+DRY_WIDTH AS WIDTH,PRESS_GSM AS GSM,DRY_WEIGHT+PRESS_WEIGHT AS WEIGHT ,PRESS_SQMTR + DRY_SQMTR FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE DOC_NO LIKE (CONCAT('B2021',right(100+month(curdate()),2),'%')) AND DOC_NO LIKE ('%/NSDF%') AND YEAR_FROM = 2021 AND YEAR_TO = 2022");
        data.Execute("INSERT INTO  PRODUCTION.CUSTOMER_PRIORITY_REPORT (BUDGET_FINYEAR,PARTY_CODE,UPN,CTG,LENGTH,WIDTH,GSM,WEIGHT,SQMTR) "
                + "SELECT DISTINCT '2020-2021'AS FY,PARTY_CODE,UPN,'B',PRESS_LENGTH+DRY_LENGTH AS LENGTH,PRESS_WIDTH+DRY_WIDTH AS WIDTH,PRESS_GSM AS GSM,DRY_WEIGHT+PRESS_WEIGHT AS WEIGHT ,PRESS_SQMTR + DRY_SQMTR FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE DOC_NO LIKE (CONCAT('B2021',right(100+month(curdate()),2),'%')) AND DOC_NO LIKE ('%/SDF%') AND YEAR_FROM = 2020 AND YEAR_TO = 2021");
        data.Execute("INSERT INTO  PRODUCTION.CUSTOMER_PRIORITY_REPORT (BUDGET_FINYEAR,PARTY_CODE,UPN,CTG,LENGTH,WIDTH,GSM,WEIGHT,SQMTR) "
                + "SELECT DISTINCT  '2020-2021'AS FY,D.MM_PARTY_CODE,MM_UPN_NO,'NB',MM_FELT_LENGTH+MM_FABRIC_LENGTH,MM_FELT_WIDTH+MM_FABRIC_WIDTH,MM_FELT_GSM,MM_FELT_WEIGHT,MM_SIZE_M2 FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL D,PRODUCTION.FELT_MACHINE_MASTER_HEADER H "
                + "WHERE H.MM_DOC_NO = D.MM_DOC_NO AND POSITION_CLOSE_IND !=1 AND H.APPROVED =1 AND H.CANCELED =0 AND SUBSTRING(D.MM_PARTY_CODE,1,1) =8 "
                + "AND MM_UPN_NO NOT IN (SELECT DISTINCT UPN FROM (SELECT UPN FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE DOC_NO LIKE (CONCAT('B2021',right(100+month(curdate()),2),'%')) AND DOC_NO LIKE ('%/NSDF%') AND YEAR_FROM = 2021 AND YEAR_TO = 2022 UNION ALL SELECT UPN FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE DOC_NO LIKE (CONCAT('B2021',right(100+month(curdate()),2),'%')) AND DOC_NO LIKE ('%/SDF%') AND YEAR_FROM = 2020 AND YEAR_TO = 2021) AS A)");

        
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D "
                + "SET NCEWIP_P_YEAR=NCE_WIP_P_YEAR,NCESTK_P_YEAR=NCE_STK_P_YEAR "
                + "WHERE LEFT(D.DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2)) AND YEAR_FROM=2021 "
                + "AND R.UPN=D.UPN");
        
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,(SELECT PR_UPN,COUNT(PR_UPN) AS PGR,SUM(NET_AMOUNT) AS PGRVAL,SUM(ACTUAL_WEIGHT) AS PGRKG FROM (SELECT PIECE_NO,NET_AMOUNT,ACTUAL_WEIGHT FROM PRODUCTION.FELT_SALES_RETURNS_DETAIL D,PRODUCTION.FELT_SALES_RETURNS_HEADER H "
                + " WHERE H.DOC_NO=D.DOC_NO AND COALESCE(H.APPROVED,0)=1 AND COALESCE(H.CANCELED,0)=0 "
                + " AND INVOICE_DATE<'2020-04-01' AND H.DOC_DATE>='2020-04-01' AND H.DOC_DATE<='2021-03-31') AS A "
                + " LEFT JOIN (SELECT PR_PIECE_NO,PR_UPN,PR_GROUP FROM PRODUCTION.FELT_SALES_PIECE_REGISTER) AS P "
                + " ON PIECE_NO=PR_PIECE_NO "
                + " WHERE PR_GROUP IN ('SDF') "
                + " GROUP BY PR_UPN) AS G "
                + " SET PREV_GR=PGR,PREV_GR_KG=PGRKG,PREV_GR_VALUE=PGRVAL "
                + " WHERE UPN=PR_UPN");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,(SELECT PR_UPN,COUNT(PR_UPN) AS PGR,SUM(NET_AMOUNT) AS PGRVAL,SUM(ACTUAL_WEIGHT) AS PGRKG FROM (SELECT PIECE_NO,NET_AMOUNT,ACTUAL_WEIGHT FROM PRODUCTION.FELT_SALES_RETURNS_DETAIL D,PRODUCTION.FELT_SALES_RETURNS_HEADER H "
                + " WHERE H.DOC_NO=D.DOC_NO AND COALESCE(H.APPROVED,0)=1 AND COALESCE(H.CANCELED,0)=0 "
                + " AND INVOICE_DATE<'2021-04-01' AND H.DOC_DATE>='2021-04-01' AND H.DOC_DATE<='2022-03-31') AS A "
                + " LEFT JOIN (SELECT PR_PIECE_NO,PR_UPN,PR_GROUP FROM PRODUCTION.FELT_SALES_PIECE_REGISTER) AS P "
                + " ON PIECE_NO=PR_PIECE_NO "
                + " WHERE PR_GROUP NOT IN ('SDF') "
                + " GROUP BY PR_UPN) AS G "
                + " SET PREV_GR=PGR,PREV_GR_KG=PGRKG,PREV_GR_VALUE=PGRVAL "
                + " WHERE UPN=PR_UPN");
        
         data.Execute("INSERT INTO PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + " (PARTY_CODE,PARTY_NAME,BUDGET_FINYEAR,UPN,PREV_GR,PREV_GR_KG,PREV_GR_VALUE,PRIORITY_IND,BUDGET_PER_VALUE) "
                + " SELECT PARTY_CODE,PARTY_NAME,'2020-2021',PR_UPN,COUNT(PR_UPN) AS PGR,SUM(ACTUAL_WEIGHT) AS PGRKG,SUM(NET_AMOUNT) AS PGRVAL,'NO',0 FROM "
                + " (SELECT PIECE_NO,NET_AMOUNT,ACTUAL_WEIGHT,PARTY_CODE,PARTY_NAME FROM PRODUCTION.FELT_SALES_RETURNS_DETAIL D,PRODUCTION.FELT_SALES_RETURNS_HEADER H "
                + " WHERE H.DOC_NO=D.DOC_NO AND COALESCE(H.APPROVED,0)=1 AND COALESCE(H.CANCELED,0)=0 "
                + " AND INVOICE_DATE<'2020-04-01' AND H.DOC_DATE>='2020-04-01' AND H.DOC_DATE<='2021-03-31') AS A "
                + " LEFT JOIN (SELECT PR_PIECE_NO,PR_UPN,PR_GROUP FROM PRODUCTION.FELT_SALES_PIECE_REGISTER) AS P "
                + " ON PIECE_NO=PR_PIECE_NO "
                + " WHERE PR_GROUP IN ('SDF') AND PR_UPN NOT IN (SELECT UPN FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE LEFT(DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2)) AND YEAR_FROM=2020) "
                + " GROUP BY PR_UPN");
         data.Execute("INSERT INTO PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + " (PARTY_CODE,PARTY_NAME,BUDGET_FINYEAR,UPN,PREV_GR,PREV_GR_KG,PREV_GR_VALUE,PRIORITY_IND,BUDGET_PER_VALUE) "
                + " SELECT PARTY_CODE,PARTY_NAME,'2020-2021',PR_UPN,COUNT(PR_UPN) AS PGR,SUM(ACTUAL_WEIGHT) AS PGRKG,SUM(NET_AMOUNT) AS PGRVAL,'NO',0 FROM "
                + " (SELECT PIECE_NO,NET_AMOUNT,ACTUAL_WEIGHT,PARTY_CODE,PARTY_NAME FROM PRODUCTION.FELT_SALES_RETURNS_DETAIL D,PRODUCTION.FELT_SALES_RETURNS_HEADER H "
                + " WHERE H.DOC_NO=D.DOC_NO AND COALESCE(H.APPROVED,0)=1 AND COALESCE(H.CANCELED,0)=0 "
                + " AND INVOICE_DATE<'2021-04-01' AND H.DOC_DATE>='2021-04-01' AND H.DOC_DATE<='2022-03-31') AS A "
                + " LEFT JOIN (SELECT PR_PIECE_NO,PR_UPN,PR_GROUP FROM PRODUCTION.FELT_SALES_PIECE_REGISTER) AS P "
                + " ON PIECE_NO=PR_PIECE_NO "
                + " WHERE PR_GROUP NOT IN ('SDF') AND PR_UPN NOT IN (SELECT UPN FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE LEFT(DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2)) AND YEAR_FROM=2021) "
                + " GROUP BY PR_UPN");

        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,DINESHMILLS.D_SAL_PARTY_MASTER P "
                + "SET R.PARTY_NAME = P.PARTY_NAME, R.ZONE = P.INCHARGE_CD,R.CITY=P.CITY_ID,R.ZONE=INCHARGE_CD "
                + "WHERE R.PARTY_CODE = P.PARTY_CODE");

//        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,PRODUCTION.FELT_BUDGET_REVIEW_DETAIL P "
//                + "SET R.BUDGET=P.CURRENT_PROJECTION "
//                + "WHERE R.UPN = P.UPN AND P.YEAR_FROM=2020 AND COALESCE(APPROVED,0) =1 AND COALESCE(CANCELED,0) =0 AND LEFT(DOC_NO,7)=CONCAT('B2021',RIGHT(100+MONTH(curdate()),2))");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,(SELECT UPN,CURRENT_PROJECTION FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE DOC_NO LIKE (CONCAT('B2021',right(100+month(curdate()),2),'%')) AND DOC_NO LIKE ('%/NSDF%') AND YEAR_FROM = 2021 AND YEAR_TO = 2022 AND COALESCE(APPROVED,0) =1 AND COALESCE(CANCELED,0) =0 UNION ALL SELECT UPN,CURRENT_PROJECTION FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE DOC_NO LIKE (CONCAT('B2021',right(100+month(curdate()),2),'%')) AND DOC_NO LIKE ('%/SDF%') AND YEAR_FROM = 2020 AND YEAR_TO = 2021 AND COALESCE(APPROVED,0) =1 AND COALESCE(CANCELED,0) =0) AS P "
                + "SET R.BUDGET=P.CURRENT_PROJECTION "
                + "WHERE R.UPN = P.UPN ");

        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + " SET BUDGET_PREV_GR=COALESCE(BUDGET,0)+COALESCE(PREV_GR,0) "
                + " WHERE  BUDGET_FINYEAR = '2020-2021'");;

        data.Execute("UPDATE PRODUCTION.FELT_MACHINE_POSITION_MST M, PRODUCTION.CUSTOMER_PRIORITY_REPORT R "
                + "SET R.POSITION_DESC1 = M.POSITION_DESC "
                + "WHERE RIGHT(UPN,4) = POSITION_DESIGN_NO");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,PRODUCTION.FELT_INCHARGE "
                + "SET R.ZONE_INCHARGE=INCHARGE_NAME "
                + "WHERE ZONE=INCHARGE_CD");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,( "
                + "SELECT PR_UPN,COUNT(PR_PIECE_NO) AS C FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                + "WHERE PR_GROUP IN ('SDF') AND PR_PIECE_STAGE IN ('PLANNING','BSR','IN STOCK','BOOKING','WEAVING','MENDING','NEEDLING','FINISHING','SPIRALLING','SEAMING','ASSEMBLY','HEAT_SETTING','MARKING','SPLICING')  "
                + "AND COALESCE(PR_DELINK,'')!='OBSOLETE'  "
                + "AND ((PR_REQ_MTH_LAST_DDMMYY >='2020-04-01'  AND PR_REQ_MTH_LAST_DDMMYY <='2021-03-31')  "
                + "OR (PR_CURRENT_SCH_LAST_DDMMYY >='2020-04-30')) "
                + "GROUP BY PR_UPN "
                + ") AS P "
                + "SET PENDING=C "
                + "WHERE R.UPN=P.PR_UPN");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,( "
                + "SELECT PR_UPN,COUNT(PR_PIECE_NO) AS C FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                + "WHERE PR_GROUP NOT IN ('SDF') AND PR_PIECE_STAGE IN ('PLANNING','BSR','IN STOCK','BOOKING','WEAVING','MENDING','NEEDLING','FINISHING','SPIRALLING','SEAMING','ASSEMBLY','HEAT_SETTING','MARKING','SPLICING')  "
                + "AND COALESCE(PR_DELINK,'')!='OBSOLETE'  "
                + "AND ((PR_REQ_MTH_LAST_DDMMYY >='2021-04-01'  AND PR_REQ_MTH_LAST_DDMMYY <='2022-03-31')  "
                + "OR (PR_CURRENT_SCH_LAST_DDMMYY >='2021-04-30')) "
                + "GROUP BY PR_UPN "
                + ") AS P "
                + "SET PENDING=C "
                + "WHERE R.UPN=P.PR_UPN");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R SET PENDING=PENDING+NCEWIP_P_YEAR+NCESTK_P_YEAR");
        
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,( "
                + "SELECT PR_UPN,SUM(NO_OF_PIECES) AS C FROM PRODUCTION.FELT_SAL_INVOICE_HEADER I,PRODUCTION.FELT_SALES_PIECE_REGISTER PR WHERE INVOICE_DATE >= '2020-04-01' AND INVOICE_DATE <='2021-03-31' AND PR_GROUP IN ('SDF') AND APPROVED =1 AND CANCELLED =0 AND I.PIECE_NO = PR.PR_PIECE_NO "
                + " AND PIECE_NO NOT IN (SELECT PIECE_NO FROM PRODUCTION.FELT_SALES_RETURNS_DETAIL D,PRODUCTION.FELT_SALES_RETURNS_HEADER H WHERE H.DOC_NO=D.DOC_NO AND COALESCE(H.APPROVED,0)=1 AND COALESCE(H.CANCELED,0)=0 )"
                + "GROUP BY PR_UPN "
                + ") AS P "
                + "SET DESPATCH=C "
                + "WHERE R.UPN=P.PR_UPN");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT D,(SELECT PR_PARTY_CODE AS PARTY_CODE,PR_MACHINE_NO AS MACHINE_NO,PR_POSITION_NO AS POSITION_NO,"
                + "COUNT(FELT_AMEND_PIECE_NO) AS INVQTY,SUM(FELT_AMEND_EXPORT_INV_AMT) AS INVAMT  FROM PRODUCTION.FELT_PIECE_AMEND,PRODUCTION.FELT_SALES_PIECE_REGISTER "
                + "WHERE FELT_AMEND_REASON =8 AND PR_PIECE_NO = FELT_AMEND_PIECE_NO  AND PR_GROUP IN ('SDF') AND FELT_AMEND_EXPORT_INV_DATE >= '2020-04-01' "
                + "GROUP BY PR_PARTY_CODE,PR_MACHINE_NO,PR_POSITION_NO) AS I "
                + "SET DESPATCH=I.INVQTY "
                + "WHERE  LEFT(D.DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2)) "
                + "AND D.PARTY_CODE=I.PARTY_CODE AND D.MACHINE_NO=I.MACHINE_NO AND D.POSITION_NO=I.POSITION_NO");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,( "
                + "SELECT PR_UPN,SUM(NO_OF_PIECES) AS C FROM PRODUCTION.FELT_SAL_INVOICE_HEADER I,PRODUCTION.FELT_SALES_PIECE_REGISTER PR WHERE INVOICE_DATE >= '2021-04-01' AND INVOICE_DATE <='2022-03-31' AND APPROVED =1 AND CANCELLED =0  AND PR_GROUP NOT IN ('SDF') AND I.PIECE_NO = PR.PR_PIECE_NO "
                + " AND PIECE_NO NOT IN (SELECT PIECE_NO FROM PRODUCTION.FELT_SALES_RETURNS_DETAIL D,PRODUCTION.FELT_SALES_RETURNS_HEADER H WHERE H.DOC_NO=D.DOC_NO AND COALESCE(H.APPROVED,0)=1 AND COALESCE(H.CANCELED,0)=0 )"
                + "GROUP BY PR_UPN "
                + ") AS P "
                + "SET DESPATCH=C "
                + "WHERE R.UPN=P.PR_UPN");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT D,(SELECT PR_PARTY_CODE AS PARTY_CODE,PR_MACHINE_NO AS MACHINE_NO,PR_POSITION_NO AS POSITION_NO,"
                + "COUNT(FELT_AMEND_PIECE_NO) AS INVQTY,SUM(FELT_AMEND_EXPORT_INV_AMT) AS INVAMT  FROM PRODUCTION.FELT_PIECE_AMEND,PRODUCTION.FELT_SALES_PIECE_REGISTER "
                + "WHERE FELT_AMEND_REASON =8  AND PR_GROUP NOT IN ('SDF') AND PR_PIECE_NO = FELT_AMEND_PIECE_NO AND FELT_AMEND_EXPORT_INV_DATE >= '2021-04-01' "
                + "GROUP BY PR_PARTY_CODE,PR_MACHINE_NO,PR_POSITION_NO) AS I "
                + "SET DESPATCH=I.INVQTY "
                + "WHERE  LEFT(D.DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2)) "
                + "AND D.PARTY_CODE=I.PARTY_CODE AND D.MACHINE_NO=I.MACHINE_NO AND D.POSITION_NO=I.POSITION_NO");
        
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,( "
                + "SELECT PR_UPN,COUNT(PR_PIECE_NO) AS C FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                + "WHERE PR_PIECE_STAGE IN ('PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SPIRALLING','SEAMING','ASSEMBLY','IN STOCK','BSR','HEAT_SETTING','MARKING','SPLICING')  "
                + "AND COALESCE(PR_DELINK,'')!='OBSOLETE'  "
                + "AND ((PR_OC_LAST_DDMMYY >= '2020-04-01' AND  PR_OC_LAST_DDMMYY <= '2021-03-31'AND PR_OC_LAST_DDMMYY  != '0000-00-00') "
                + "OR (PR_CURRENT_SCH_LAST_DDMMYY >='2020-04-30')) AND PR_GROUP IN ('SDF') "
                + "GROUP BY PR_UPN  "
                + ") AS P "
                + "SET OC_MAY=C "
                + "WHERE R.UPN=P.PR_UPN");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,( "
                + "SELECT PR_UPN,COUNT(PR_PIECE_NO) AS C FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                + "WHERE PR_PIECE_STAGE IN ('PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SPIRALLING','SEAMING','ASSEMBLY','IN STOCK','BSR','HEAT_SETTING','MARKING','SPLICING')  "
                + "AND COALESCE(PR_DELINK,'')!='OBSOLETE'  "
                + "AND ((PR_OC_LAST_DDMMYY >= '2021-04-01' AND  PR_OC_LAST_DDMMYY <= '2022-03-31'AND PR_OC_LAST_DDMMYY  != '0000-00-00') "
                + "OR (PR_CURRENT_SCH_LAST_DDMMYY >='2021-04-30'))  AND PR_GROUP NOT IN ('SDF') "
                + "GROUP BY PR_UPN  "
                + ") AS P "
                + "SET OC_MAY=C "
                + "WHERE R.UPN=P.PR_UPN");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT SET BUDGET = PENDING,BUDGET_PREV_GR=PENDING+COALESCE(PREV_GR,0) WHERE  BUDGET_FINYEAR = '2020-2021' AND CTG = 'NB' AND BUDGET =0");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT SET CURRENT_MONTH = MONTH(curdate()) WHERE  BUDGET_FINYEAR = '2020-2021'");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D "
                + "SET R.GROUP=D.GROUP_NAME "
                + "WHERE D.UPN=R.UPN AND LEFT(D.DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2))");
    }

    public static void Query_2_Old() {
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET BUDGET_PER_VALUE = PERCENTAGE * (BUDGET_PREV_GR) WHERE  BUDGET_FINYEAR = '2020-2021'");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET BUDGET_PER_VALUE = CEILING(BUDGET_PER_VALUE) WHERE  BUDGET_FINYEAR = '2020-2021'");

        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET OC_PRIORITY  = 'NO' WHERE  BUDGET_FINYEAR = '2020-2021' ");

        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET PRIORITY_IND  = CASE WHEN BUDGET_PER_VALUE > PENDING+DESPATCH THEN 'YES' ELSE 'NO' END "
                + "WHERE  BUDGET_FINYEAR = '2020-2021'");
        //data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
        //        + "SET OC_PRIORITY  = CASE WHEN BUDGET_PER_VALUE > OC_MAY+DESPATCH THEN 'YES' ELSE 'NO' END WHERE  BUDGET_FINYEAR = '2020-2021'");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + " SET OC_PRIORITY  = CASE WHEN BUDGET_PER_VALUE > OC_MAY+DESPATCH THEN 'YES' ELSE 'NO' END "
                + "WHERE  BUDGET_FINYEAR = '2020-2021' AND PENDING - OC_MAY > 0 ");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET SCOPE_QTY=BUDGET_PER_VALUE-(PENDING+DESPATCH) "
                + " WHERE  BUDGET_FINYEAR = '2020-2021'");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET SCOPE_QTY=0 "
                + " WHERE SCOPE_QTY<0 AND BUDGET_FINYEAR = '2020-2021'");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,"
                + "(SELECT MM_UPN_NO,ROUND(((MM_FELT_LENGTH+MM_FABRIC_LENGTH)*(MM_FABRIC_WIDTH+MM_FELT_WIDTH)*MM_FELT_GSM/1000)*1,2) AS KG,MM_FELT_VALUE_WITH_GST FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL) AS D "
                + " SET R.SCOPE_KG=SCOPE_QTY*KG,"
                + " SCOPE_VALUE=SCOPE_QTY*MM_FELT_VALUE_WITH_GST "
                + " WHERE UPN=MM_UPN_NO AND BUDGET_FINYEAR = '2020-2021'");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,(SELECT PR_PARTY_CODE AS PARTY_CODE,PR_MACHINE_NO AS MACHINE_NO,PR_POSITION_NO AS POSITION_NO,"
                + "COUNT(FELT_AMEND_PIECE_NO) AS INVQTY,SUM(FELT_AMEND_EXPORT_INV_AMT) AS INVAMT  FROM PRODUCTION.FELT_PIECE_AMEND,PRODUCTION.FELT_SALES_PIECE_REGISTER "
                + "WHERE FELT_AMEND_REASON =8 AND PR_PIECE_NO = FELT_AMEND_PIECE_NO AND FELT_AMEND_EXPORT_INV_DATE >= '2020-04-01' "
                + "GROUP BY PR_PARTY_CODE,PR_MACHINE_NO,PR_POSITION_NO) AS I "
                + "SET SCOPE_VALUE=INVAMT "
                + "WHERE  LEFT(D.DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2)) AND D.PARTY_CODE=I.PARTY_CODE AND D.MACHINE_NO=I.MACHINE_NO AND D.POSITION_NO=I.POSITION_NO");

    }

    public static void Query_2() {
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET BUDGET_PER_VALUE = PERCENTAGE * (BUDGET_PREV_GR) WHERE  BUDGET_FINYEAR = '2020-2021'");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET BUDGET_PER_VALUE = CEILING(BUDGET_PER_VALUE) WHERE  BUDGET_FINYEAR = '2020-2021'");

        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET OC_PRIORITY  = 'NO' WHERE  BUDGET_FINYEAR = '2020-2021' ");

        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET PRIORITY_IND  = CASE WHEN COALESCE(BUDGET_PER_VALUE,0) > COALESCE(PENDING,0)+COALESCE(DESPATCH,0)+COALESCE(NCEWIP_P_YEAR,0)+COALESCE(NCESTK_P_YEAR,0) THEN 'YES' ELSE 'NO' END "
                + "WHERE  BUDGET_FINYEAR = '2020-2021'");
        //data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
        //        + "SET OC_PRIORITY  = CASE WHEN BUDGET_PER_VALUE > OC_MAY+DESPATCH THEN 'YES' ELSE 'NO' END WHERE  BUDGET_FINYEAR = '2020-2021'");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + " SET OC_PRIORITY  = CASE WHEN COALESCE(BUDGET_PER_VALUE,0) > COALESCE(OC_MAY,0)+COALESCE(DESPATCH,0)+COALESCE(NCEWIP_P_YEAR,0)+COALESCE(NCESTK_P_YEAR,0) THEN 'YES' ELSE 'NO' END "
                + "WHERE  BUDGET_FINYEAR = '2020-2021' AND PENDING - OC_MAY > 0 ");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET SCOPE_QTY=BUDGET_PER_VALUE-(PENDING+DESPATCH+NCEWIP_P_YEAR+NCESTK_P_YEAR) "
                + " WHERE  BUDGET_FINYEAR = '2020-2021'");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET SCOPE_QTY=0 "
                + " WHERE SCOPE_QTY<0 AND BUDGET_FINYEAR = '2020-2021'");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,"
                + "(SELECT MM_UPN_NO,ROUND(((MM_FELT_LENGTH+MM_FABRIC_LENGTH)*(MM_FABRIC_WIDTH+MM_FELT_WIDTH)*MM_FELT_GSM/1000)*1,2) AS KG,MM_FELT_VALUE_WITH_GST FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL) AS D "
                + " SET R.SCOPE_KG=SCOPE_QTY*KG,"
                + " SCOPE_VALUE=SCOPE_QTY*MM_FELT_VALUE_WITH_GST "
                + " WHERE UPN=MM_UPN_NO AND BUDGET_FINYEAR = '2020-2021'");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,(SELECT PR_PARTY_CODE AS PARTY_CODE,PR_MACHINE_NO AS MACHINE_NO,PR_POSITION_NO AS POSITION_NO,"
                + "COUNT(FELT_AMEND_PIECE_NO) AS INVQTY,SUM(FELT_AMEND_EXPORT_INV_AMT) AS INVAMT  FROM PRODUCTION.FELT_PIECE_AMEND,PRODUCTION.FELT_SALES_PIECE_REGISTER "
                + "WHERE PR_GROUP IN ('SDF') AND FELT_AMEND_REASON =8 AND PR_PIECE_NO = FELT_AMEND_PIECE_NO AND FELT_AMEND_EXPORT_INV_DATE >= '2020-04-01' "
                + "GROUP BY PR_PARTY_CODE,PR_MACHINE_NO,PR_POSITION_NO) AS I "
                + "SET SCOPE_VALUE=INVAMT "
                + "WHERE  LEFT(D.DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2)) AND D.PARTY_CODE=I.PARTY_CODE AND D.MACHINE_NO=I.MACHINE_NO AND D.POSITION_NO=I.POSITION_NO");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,(SELECT PR_PARTY_CODE AS PARTY_CODE,PR_MACHINE_NO AS MACHINE_NO,PR_POSITION_NO AS POSITION_NO,"
                + "COUNT(FELT_AMEND_PIECE_NO) AS INVQTY,SUM(FELT_AMEND_EXPORT_INV_AMT) AS INVAMT  FROM PRODUCTION.FELT_PIECE_AMEND,PRODUCTION.FELT_SALES_PIECE_REGISTER "
                + "WHERE PR_GROUP NOT IN ('SDF') AND FELT_AMEND_REASON =8 AND PR_PIECE_NO = FELT_AMEND_PIECE_NO AND FELT_AMEND_EXPORT_INV_DATE >= '2021-04-01' "
                + "GROUP BY PR_PARTY_CODE,PR_MACHINE_NO,PR_POSITION_NO) AS I "
                + "SET SCOPE_VALUE=INVAMT "
                + "WHERE  LEFT(D.DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2)) AND D.PARTY_CODE=I.PARTY_CODE AND D.MACHINE_NO=I.MACHINE_NO AND D.POSITION_NO=I.POSITION_NO");
    }

    public static void Query2_Old() {
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET BUDGET_PER_VALUE_SDF = COALESCE(PER_SDF,0) * (BUDGET_PREV_GR) WHERE  BUDGET_FINYEAR = '2020-2021'"
                + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP='SDF' ");
        System.out.println("1");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET BUDGET_PER_VALUE_SDF = CEILING(BUDGET_PER_VALUE_SDF) WHERE  BUDGET_FINYEAR = '2020-2021' "
                + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP='SDF' ");
        System.out.println("2");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET BUDGET_PER_VALUE_OTHER_SDF = COALESCE(PER_OTHER_SDF,0) * (BUDGET_PREV_GR) WHERE  BUDGET_FINYEAR = '2020-2021' "
                + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP!='SDF' ");
        System.out.println("3");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET BUDGET_PER_VALUE_OTHER_SDF = CEILING(BUDGET_PER_VALUE_OTHER_SDF) WHERE  BUDGET_FINYEAR = '2020-2021' "
                + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP!='SDF' ");
        System.out.println("4");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET OC_PRIORITY_SDF  = 'NO',OC_PRIORITY_OTHER_SDF='NO' WHERE  BUDGET_FINYEAR = '2020-2021' "
                + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP='SDF' ");
        System.out.println("5");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET PRIORITY_IND_SDF  = CASE WHEN BUDGET_PER_VALUE_SDF > PENDING+DESPATCH THEN 'YES' ELSE 'NO' END "
                + "WHERE  BUDGET_FINYEAR = '2020-2021' "
                + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP='SDF' ");
        System.out.println("6");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET PRIORITY_IND_OTHER_SDF  = CASE WHEN BUDGET_PER_VALUE_OTHER_SDF > PENDING+DESPATCH THEN 'YES' ELSE 'NO' END "
                + "WHERE  BUDGET_FINYEAR = '2020-2021' "
                + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP!='SDF' ");
        System.out.println("7");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + " SET OC_PRIORITY_SDF  = CASE WHEN BUDGET_PER_VALUE_SDF > OC_MAY+DESPATCH THEN 'YES' ELSE 'NO' END "
                + "WHERE  BUDGET_FINYEAR = '2020-2021' AND PENDING - OC_MAY > 0  "
                + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP='SDF' ");
        System.out.println("8");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + " SET OC_PRIORITY_OTHER_SDF  = CASE WHEN BUDGET_PER_VALUE_OTHER_SDF > OC_MAY+DESPATCH THEN 'YES' ELSE 'NO' END "
                + "WHERE  BUDGET_FINYEAR = '2020-2021' AND PENDING - OC_MAY > 0 "
                + "  AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP!='SDF' ");
        System.out.println("9");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET SCOPE_QTY_SDF=BUDGET_PER_VALUE_SDF-(PENDING+DESPATCH) "
                + " WHERE  BUDGET_FINYEAR = '2020-2021' "
                + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP='SDF' ");
        System.out.println("10");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET SCOPE_QTY_OTHER_SDF=BUDGET_PER_VALUE_OTHER_SDF-(PENDING+DESPATCH) "
                + " WHERE  BUDGET_FINYEAR = '2020-2021' "
                + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP!='SDF' ");
        System.out.println("11");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET SCOPE_QTY_SDF=0 "
                + " WHERE COALESCE(SCOPE_QTY_SDF,0)<=0 AND BUDGET_FINYEAR = '2020-2021' "
                + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP='SDF' ");
        System.out.println("12");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET SCOPE_QTY_OTHER_SDF=0 "
                + " WHERE COALESCE(SCOPE_QTY_OTHER_SDF,0)<=0 AND BUDGET_FINYEAR = '2020-2021' "
                + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP!='SDF' ");
        System.out.println("13");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,"
                + "(SELECT MM_UPN_NO,ROUND(((MM_FELT_LENGTH+MM_FABRIC_LENGTH)*(MM_FABRIC_WIDTH+MM_FELT_WIDTH)*MM_FELT_GSM/1000)*1,2) AS KG,MM_FELT_VALUE_WITH_GST FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL) AS D "
                + " SET R.SCOPE_KG_SDF=SCOPE_QTY_SDF*KG,"
                + " SCOPE_VALUE_SDF=SCOPE_QTY_SDF*MM_FELT_VALUE_WITH_GST "
                + " WHERE UPN=MM_UPN_NO AND BUDGET_FINYEAR = '2020-2021'"
                + "  AND R.GROUP='SDF' ");
        System.out.println("14");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,"
                + "(SELECT MM_UPN_NO,ROUND(((MM_FELT_LENGTH+MM_FABRIC_LENGTH)*(MM_FABRIC_WIDTH+MM_FELT_WIDTH)*MM_FELT_GSM/1000)*1,2) AS KG,MM_FELT_VALUE_WITH_GST FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL) AS D "
                + " SET R.SCOPE_KG_OTHER_SDF=SCOPE_QTY_OTHER_SDF*KG,"
                + " SCOPE_VALUE_OTHER_SDF=SCOPE_QTY_OTHER_SDF*MM_FELT_VALUE_WITH_GST "
                + " WHERE UPN=MM_UPN_NO AND BUDGET_FINYEAR = '2020-2021' "
                + " AND R.GROUP!='SDF' ");
        System.out.println("15");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,(SELECT PR_PARTY_CODE AS PARTY_CODE,PR_MACHINE_NO AS MACHINE_NO,PR_POSITION_NO AS POSITION_NO,"
                + "COUNT(FELT_AMEND_PIECE_NO) AS INVQTY,SUM(FELT_AMEND_EXPORT_INV_AMT) AS INVAMT  FROM PRODUCTION.FELT_PIECE_AMEND,PRODUCTION.FELT_SALES_PIECE_REGISTER "
                + "WHERE FELT_AMEND_REASON =8 AND PR_PIECE_NO = FELT_AMEND_PIECE_NO AND FELT_AMEND_EXPORT_INV_DATE >= '2020-04-01' "
                + "GROUP BY PR_PARTY_CODE,PR_MACHINE_NO,PR_POSITION_NO) AS I "
                + "SET SCOPE_VALUE_SDF=INVAMT "
                + "WHERE  LEFT(D.DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2)) AND D.PARTY_CODE=I.PARTY_CODE AND D.MACHINE_NO=I.MACHINE_NO AND D.POSITION_NO=I.POSITION_NO "
                + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP='SDF' ");
        System.out.println("16");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,(SELECT PR_PARTY_CODE AS PARTY_CODE,PR_MACHINE_NO AS MACHINE_NO,PR_POSITION_NO AS POSITION_NO,"
                + "COUNT(FELT_AMEND_PIECE_NO) AS INVQTY,SUM(FELT_AMEND_EXPORT_INV_AMT) AS INVAMT  FROM PRODUCTION.FELT_PIECE_AMEND,PRODUCTION.FELT_SALES_PIECE_REGISTER "
                + "WHERE FELT_AMEND_REASON =8 AND PR_PIECE_NO = FELT_AMEND_PIECE_NO AND FELT_AMEND_EXPORT_INV_DATE >= '2020-04-01' "
                + "GROUP BY PR_PARTY_CODE,PR_MACHINE_NO,PR_POSITION_NO) AS I "
                + "SET SCOPE_VALUE_OTHER_SDF=INVAMT "
                + "WHERE  LEFT(D.DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2)) AND D.PARTY_CODE=I.PARTY_CODE AND D.MACHINE_NO=I.MACHINE_NO AND D.POSITION_NO=I.POSITION_NO "
                + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP!='SDF' ");
        System.out.println("17");

    }

    public static void Query2() {
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET BUDGET_PER_VALUE_SDF = COALESCE(PER_SDF,0) * (BUDGET_PREV_GR) WHERE  BUDGET_FINYEAR = '2020-2021'"
                + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP='SDF' ");
//        System.out.println("1");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET BUDGET_PER_VALUE_SDF = CEILING(BUDGET_PER_VALUE_SDF) WHERE  BUDGET_FINYEAR = '2020-2021' "
                + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP='SDF' ");
//        System.out.println("2");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET BUDGET_PER_VALUE_OTHER_SDF = COALESCE(PER_OTHER_SDF,0) * (BUDGET_PREV_GR) WHERE  BUDGET_FINYEAR = '2020-2021' "
                + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP!='SDF' ");
//        System.out.println("3");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET BUDGET_PER_VALUE_OTHER_SDF = CEILING(BUDGET_PER_VALUE_OTHER_SDF) WHERE  BUDGET_FINYEAR = '2020-2021' "
                + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP!='SDF' ");
//        System.out.println("4");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET OC_PRIORITY_SDF  = 'NO',OC_PRIORITY_OTHER_SDF='NO' WHERE  BUDGET_FINYEAR = '2020-2021' "
                + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP='SDF' ");
//        System.out.println("5");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET PRIORITY_IND_SDF  = CASE WHEN BUDGET_PER_VALUE_SDF > PENDING+DESPATCH THEN 'YES' ELSE 'NO' END "
                + "WHERE  BUDGET_FINYEAR = '2020-2021' "
                + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP='SDF' ");
//        System.out.println("6");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET PRIORITY_IND_OTHER_SDF  = CASE WHEN BUDGET_PER_VALUE_OTHER_SDF > PENDING+DESPATCH THEN 'YES' ELSE 'NO' END "
                + "WHERE  BUDGET_FINYEAR = '2020-2021' "
                + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP!='SDF' ");
//        System.out.println("7");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + " SET OC_PRIORITY_SDF  = CASE WHEN BUDGET_PER_VALUE_SDF > OC_MAY+DESPATCH THEN 'YES' ELSE 'NO' END "
                + "WHERE  BUDGET_FINYEAR = '2020-2021' AND PENDING - OC_MAY > 0  "
                + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP='SDF' ");
//        System.out.println("8");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + " SET OC_PRIORITY_OTHER_SDF  = CASE WHEN BUDGET_PER_VALUE_OTHER_SDF > OC_MAY+DESPATCH THEN 'YES' ELSE 'NO' END "
                + "WHERE  BUDGET_FINYEAR = '2020-2021' AND PENDING - OC_MAY > 0 "
                + "  AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP!='SDF' ");
//        System.out.println("9");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET SCOPE_QTY_SDF=BUDGET_PER_VALUE_SDF-(PENDING+DESPATCH) "
                + " WHERE  BUDGET_FINYEAR = '2020-2021' "
                + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP='SDF' ");
//        System.out.println("10");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET SCOPE_QTY_OTHER_SDF=BUDGET_PER_VALUE_OTHER_SDF-(PENDING+DESPATCH) "
                + " WHERE  BUDGET_FINYEAR = '2020-2021' "
                + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP!='SDF' ");
//        System.out.println("11");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET SCOPE_QTY_SDF=0 "
                + " WHERE COALESCE(SCOPE_QTY_SDF,0)<=0 AND BUDGET_FINYEAR = '2020-2021' "
                + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP='SDF' ");
//        System.out.println("12");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT "
                + "SET SCOPE_QTY_OTHER_SDF=0 "
                + " WHERE COALESCE(SCOPE_QTY_OTHER_SDF,0)<=0 AND BUDGET_FINYEAR = '2020-2021' "
                + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP!='SDF' ");
//        System.out.println("13");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,"
                + "(SELECT MM_UPN_NO,ROUND(((MM_FELT_LENGTH+MM_FABRIC_LENGTH)*(MM_FABRIC_WIDTH+MM_FELT_WIDTH)*MM_FELT_GSM/1000)*1,2) AS KG,MM_FELT_VALUE_WITH_GST FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL) AS D "
                + " SET R.SCOPE_KG_SDF=SCOPE_QTY_SDF*KG,"
                + " SCOPE_VALUE_SDF=SCOPE_QTY_SDF*MM_FELT_VALUE_WITH_GST "
                + " WHERE UPN=MM_UPN_NO AND BUDGET_FINYEAR = '2020-2021'"
                + "  AND R.GROUP='SDF' ");
//        System.out.println("14");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,"
                + "(SELECT MM_UPN_NO,ROUND(((MM_FELT_LENGTH+MM_FABRIC_LENGTH)*(MM_FABRIC_WIDTH+MM_FELT_WIDTH)*MM_FELT_GSM/1000)*1,2) AS KG,MM_FELT_VALUE_WITH_GST FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL) AS D "
                + " SET R.SCOPE_KG_OTHER_SDF=SCOPE_QTY_OTHER_SDF*KG,"
                + " SCOPE_VALUE_OTHER_SDF=SCOPE_QTY_OTHER_SDF*MM_FELT_VALUE_WITH_GST "
                + " WHERE UPN=MM_UPN_NO AND BUDGET_FINYEAR = '2020-2021' "
                + " AND R.GROUP!='SDF' ");
//        System.out.println("15");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,(SELECT PR_PARTY_CODE AS PARTY_CODE,PR_MACHINE_NO AS MACHINE_NO,PR_POSITION_NO AS POSITION_NO,"
                + "COUNT(FELT_AMEND_PIECE_NO) AS INVQTY,SUM(FELT_AMEND_EXPORT_INV_AMT) AS INVAMT  FROM PRODUCTION.FELT_PIECE_AMEND,PRODUCTION.FELT_SALES_PIECE_REGISTER "
                + "WHERE FELT_AMEND_REASON =8 AND PR_PIECE_NO = FELT_AMEND_PIECE_NO AND FELT_AMEND_EXPORT_INV_DATE >= '2020-04-01' "
                + "GROUP BY PR_PARTY_CODE,PR_MACHINE_NO,PR_POSITION_NO) AS I "
                + "SET SCOPE_VALUE_SDF=INVAMT "
                + "WHERE  R.PARTY_CODE=I.PARTY_CODE AND R.MACHINE_NO=I.MACHINE_NO AND R.POSITION_NO=I.POSITION_NO "
                + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP='SDF' ");
//        System.out.println("16");
        data.Execute("UPDATE PRODUCTION.CUSTOMER_PRIORITY_REPORT R,(SELECT PR_PARTY_CODE AS PARTY_CODE,PR_MACHINE_NO AS MACHINE_NO,PR_POSITION_NO AS POSITION_NO,"
                + "COUNT(FELT_AMEND_PIECE_NO) AS INVQTY,SUM(FELT_AMEND_EXPORT_INV_AMT) AS INVAMT  FROM PRODUCTION.FELT_PIECE_AMEND,PRODUCTION.FELT_SALES_PIECE_REGISTER "
                + "WHERE FELT_AMEND_REASON =8 AND PR_PIECE_NO = FELT_AMEND_PIECE_NO AND FELT_AMEND_EXPORT_INV_DATE >= '2021-04-01' "
                + "GROUP BY PR_PARTY_CODE,PR_MACHINE_NO,PR_POSITION_NO) AS I "
                + "SET SCOPE_VALUE_OTHER_SDF=INVAMT "
                + "WHERE  R.PARTY_CODE=I.PARTY_CODE AND R.MACHINE_NO=I.MACHINE_NO AND R.POSITION_NO=I.POSITION_NO "
                + " AND PRODUCTION.CUSTOMER_PRIORITY_REPORT.GROUP!='SDF' ");
//        System.out.println("17");

    }

}
