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
public class StockAgainstSalesProjection {

    /**
     * @param args the command line arguments
     */
    private static clsExcelExporter exprt = new clsExcelExporter();

    public static void main(String[] args) {
        // TODO code application logic here

        if (!data.IsRecordExist("SELECT * FROM PRODUCTION.A_CHECK_REPORT_SEND WHERE SEND_DESC='STOCK_AGAINST_SALES_PROJECTION' AND SEND_MONTH=MONTH(CURDATE()) AND SEND_YEAR=YEAR(CURDATE())")
                && !data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE APPROVED=0") //&& EITLERPGLOBAL.getCurrentDay() < 8
                ) {

            String mntFor = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(NOW(),'%b'),'-',YEAR(NOW())) FROM DUAL");

            String pMessage = "Respected Sir, <br><br> Warehouse Stock before 2 Month. <br> Please find attached document herewith. <br><br>";

            String recievers = "rakeshdalal@dineshmills.com,soumen@dineshmills.com,vdshanbhag@dineshmills.com";//"rishineekhra@dineshmills.com";
            
            String pCC = "aditya@dineshmills.com,sdmlerp@dineshmills.com";

            String sql = "SELECT WPARTY_CODE AS 'Party Code',WPARTY_NAME AS 'Party Name',WPRESS_NOS AS 'W/H Press Nos',WPRESS_KGS AS 'W/H Press Kgs',WPRESS_VALUE AS 'W/H Press Values',WACNE_NOS AS 'W/H ACNE Nos',WACNE_KGS AS 'W/H ACNE Kgs',WACNE_VALUE AS 'W/H ACNE Values',WHDS_NOS AS 'W/H HDS Nos',WHDS_KGS AS 'W/H HDS Sqmtr',WHDS_VALUE AS 'W/H HDS Values',WSDF_NOS AS 'W/H SDF Nos',WSDF_KGS AS 'W/H SDF Sqmtr',WSDF_VALUE AS 'W/H SDF Values',  "
                    + "WTOTAL_NOS AS 'W/H Total Nos',WTOTAL_KGS AS 'W/H Total Kgs',WTOTAL_SQMTRS AS 'W/H Total Sqmtr',WTOTAL_VALUE AS 'W/H Total Values',  "
                    + "SPRESS_NO AS 'SP Press Nos',SPRESS_KG AS 'SP Press Kgs',SPRESS_VALUE AS 'SP Press Values',SACNE_NO AS 'SP ACNE Nos',SACNE_KG AS 'SP ACNE Kgs',SACNE_VALUE AS 'SP ACNE Values',SHDS_NO AS 'SP HDS Nos',SHDS_KG AS 'SP HDS Sqmtr',SHDS_VALUE AS 'SP HDS Values',SSDF_NO AS 'SP SDF Nos',SSDF_KG AS 'SP SDF Sqmtr',SSDF_VALUE AS 'SP SDF Values',  "
                    + "STOTAL_NO AS 'SP Total Nos',STOTAL_KG AS 'SP Total Kgs',STOTAL_SQMTR AS 'SP Total Sqmtr',STOTAL_VALUE AS 'SP Total Values',  "
                    + "ROUND((WTOTAL_VALUE/STOTAL_VALUE)*100,2) AS 'Difference Percentage'  "
                    + "FROM  "
                    + "(SELECT PARTY_CODE AS WPARTY_CODE,PARTY_NAME AS WPARTY_NAME,  "
                    + "SUM(COALESCE(CASE WHEN PRODUCT_GROUP NOT IN ('ACNE','FCNE','HDS','SDF') THEN  1  ELSE 0 END,0)) AS WPRESS_NOS,  "
                    + "SUM(COALESCE(CASE WHEN PRODUCT_GROUP NOT IN ('ACNE','FCNE','HDS','SDF') THEN  WIEGHT  ELSE 0 END,0)) AS WPRESS_KGS,  "
                    + "SUM(COALESCE(CASE WHEN PRODUCT_GROUP NOT IN ('ACNE','FCNE','HDS','SDF') THEN  FELT_VALUE  ELSE 0 END,0)) AS WPRESS_VALUE,  "
                    + "SUM(COALESCE(CASE WHEN PRODUCT_GROUP IN ('ACNE','FCNE') THEN  1  ELSE 0 END,0)) AS WACNE_NOS,  "
                    + "SUM(COALESCE(CASE WHEN PRODUCT_GROUP IN ('ACNE','FCNE') THEN  WIEGHT  ELSE 0 END,0)) AS WACNE_KGS,  "
                    + "SUM(COALESCE(CASE WHEN PRODUCT_GROUP IN ('ACNE','FCNE') THEN  FELT_VALUE  ELSE 0 END,0)) AS WACNE_VALUE,  "
                    + "SUM(COALESCE(CASE WHEN PRODUCT_GROUP IN ('HDS') THEN  1  ELSE 0 END,0)) AS WHDS_NOS,  "
                    + "SUM(COALESCE(CASE WHEN PRODUCT_GROUP IN ('HDS') THEN  SQMTR  ELSE 0 END,0)) AS WHDS_KGS,  "
                    + "SUM(COALESCE(CASE WHEN PRODUCT_GROUP IN ('HDS') THEN  FELT_VALUE  ELSE 0 END,0)) AS WHDS_VALUE,  "
                    + "SUM(COALESCE(CASE WHEN PRODUCT_GROUP IN ('SDF') THEN  1  ELSE 0 END,0)) AS WSDF_NOS,  "
                    + "SUM(COALESCE(CASE WHEN PRODUCT_GROUP IN ('SDF') THEN  SQMTR  ELSE 0 END,0)) AS WSDF_KGS,  "
                    + "SUM(COALESCE(CASE WHEN PRODUCT_GROUP IN ('SDF') THEN  FELT_VALUE  ELSE 0 END,0)) AS WSDF_VALUE,  "
                    + "COUNT(*) AS WTOTAL_NOS,  "
                    + "SUM(COALESCE(CASE WHEN PRODUCT_GROUP NOT IN ('HDS','SDF') THEN  WIEGHT  ELSE 0 END,0)) AS WTOTAL_KGS,  "
                    + "SUM(COALESCE(CASE WHEN PRODUCT_GROUP IN ('HDS','SDF') THEN  SQMTR  ELSE 0 END,0)) AS WTOTAL_SQMTRS,  "
                    + "SUM(COALESCE(FELT_VALUE,0)) AS WTOTAL_VALUE  "
                    + "FROM PRODUCTION.FELT_WH_MTH_CLOSING_PIECE_STOCK WHERE MTH_CLOSING_DATE = LAST_DAY(DATE_SUB(CURDATE(), INTERVAL 1 MONTH))  "
                    + "AND FNSG_DATE <= LAST_DAY(DATE_SUB(CURDATE(), INTERVAL 3 MONTH)) AND COALESCE(OBSOLETE_STATUS,'') !='OBSOLETE'  "
                    + "GROUP BY PARTY_CODE,PARTY_NAME  "
                    + ") AS W  "
                    + "   "
                    + "LEFT JOIN  "
                    + "  "
                    + "(SELECT PARTY_CODE,PARTY_NAME, "
                    + "SUM(COALESCE(CASE WHEN GROUP_NAME IN ('ACNE','FCNE') THEN CURRENT_PROJECTION ELSE 0 END,0)) AS SACNE_NO, "
                    + "SUM(COALESCE(CASE WHEN GROUP_NAME IN ('ACNE','FCNE') THEN round((coalesce(CURRENT_PROJECTION,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) ELSE 0 END,0)) AS SACNE_KG, "
                    + "SUM(COALESCE(CASE WHEN GROUP_NAME IN ('ACNE','FCNE') THEN round(coalesce(CURRENT_PROJECTION_VALUE,0),2) ELSE 0 END,0)) AS SACNE_VALUE, "
                    + ""
                    + "SUM(COALESCE(CASE WHEN GROUP_NAME NOT IN ('ACNE','FCNE','SDF','HDS') THEN CURRENT_PROJECTION ELSE 0 END,0)) AS SPRESS_NO, "
                    + "SUM(COALESCE(CASE WHEN GROUP_NAME NOT IN ('ACNE','FCNE','SDF','HDS') THEN round((coalesce(CURRENT_PROJECTION,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) ELSE 0 END,0)) AS SPRESS_KG, "
                    + "SUM(COALESCE(CASE WHEN GROUP_NAME NOT IN ('ACNE','FCNE','SDF','HDS') THEN round(coalesce(CURRENT_PROJECTION_VALUE,0),2) ELSE 0 END,0)) AS SPRESS_VALUE, "
                    + "  "
                    + "SUM(COALESCE(CASE WHEN GROUP_NAME IN ('HDS') THEN CURRENT_PROJECTION ELSE 0 END,0)) AS SHDS_NO, "
                    + "SUM(COALESCE(CASE WHEN GROUP_NAME IN ('HDS') THEN round((coalesce(CURRENT_PROJECTION,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) ELSE 0 END,0)) AS SHDS_KG, "
                    + "SUM(COALESCE(CASE WHEN GROUP_NAME IN ('HDS') THEN round(coalesce(CURRENT_PROJECTION_VALUE,0),2) ELSE 0 END,0)) AS SHDS_VALUE, "
                    + "  "
                    + "SUM(COALESCE(CASE WHEN GROUP_NAME IN ('SDF') THEN CURRENT_PROJECTION ELSE 0 END,0)) AS SSDF_NO, "
                    + "SUM(COALESCE(CASE WHEN GROUP_NAME IN ('SDF') THEN round((coalesce(CURRENT_PROJECTION,0)*(coalesce(dry_sqmtr,0))),2) ELSE 0 END,0)) AS SSDF_KG, "
                    + "SUM(COALESCE(CASE WHEN GROUP_NAME IN ('SDF') THEN round(coalesce(CURRENT_PROJECTION_VALUE,0),2) ELSE 0 END,0)) AS SSDF_VALUE, "
                    + "  "
                    + "SUM(COALESCE(CURRENT_PROJECTION,0)) AS STOTAL_NO, "
                    + "SUM(COALESCE(round((coalesce(CURRENT_PROJECTION,0)*(coalesce(press_weight,0))),2),0)) AS STOTAL_KG, "
                    + "SUM(COALESCE(round((coalesce(CURRENT_PROJECTION,0)*(coalesce(dry_weight,0))),2),0)) AS STOTAL_SQMTR, "
                    + "  "
                    + "SUM(COALESCE(round(coalesce(CURRENT_PROJECTION_VALUE,0),2),0)) AS STOTAL_VALUE "
                    + "  "
                    + "  FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL,PRODUCTION.FELT_INCHARGE  "
                    + "WHERE YEAR_FROM=YEAR(CURDATE()) AND INCHARGE = INCHARGE_CD "
                    + "and left(doc_no,7)=CONCAT('B2122',RIGHT(100+MONTH(CURDATE()),2)) "
                    + "AND (coalesce(ACTUAL_BUDGET,0)>0 OR coalesce(CURRENT_PROJECTION,0)>0)  "
                    + "  group by PARTY_CODE order by PARTY_CODE ) AS S  "
                    + "  ON S.PARTY_CODE = W.WPARTY_CODE  "
                    + "ORDER BY (WTOTAL_VALUE/STOTAL_VALUE)*100 DESC  "
                    + " ";

            exprt.fillData(sql, new File("/Email_Attachment/StockAgainstSP_" + mntFor + ".xls"), "SP");

            data.Execute("INSERT INTO PRODUCTION.A_CHECK_REPORT_SEND (SEND_DESC, SEND_MONTH, SEND_YEAR) VALUES ('STOCK_AGAINST_SALES_PROJECTION', MONTH(CURDATE()), YEAR(CURDATE()) )");
            String responce = sendNotificationMailWithAttachement("Analysis of Stock Against Sales Projection against SP " + mntFor + "", pMessage, recievers, pCC, "/Email_Attachment/StockAgainstSP_" + mntFor + ".xls", "StockAgainstSP_" + mntFor + ".xls");
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
