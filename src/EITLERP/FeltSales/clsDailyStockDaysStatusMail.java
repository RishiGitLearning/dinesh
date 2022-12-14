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
public class clsDailyStockDaysStatusMail {

    /**
     * @param args the command line arguments
     */
    private static clsExcelExporter exprt = new clsExcelExporter();

    public static void main(String[] args) {
        // TODO code application logic here

        String pMessage = "Respected Sir, <br><br> These files contains stock list of above 180 days pieces, newly added pieces completed 180 days, despatched or diverted pieces above 180 days and newly added obsolete pieces completed 180 days. <br> Please find attached document herewith <br><br>";

        String recievers = "feltwh@dineshmills.com,soumen@dineshmills.com,vdshanbhag@dineshmills.com";
//            String recievers = "rishineekhra@dineshmills.com";

        String pCC = "sdmlerp@dineshmills.com";
//            String pCC = "sdmlerp@dineshmills.com";

        String sql1 = "SELECT @a:=@a+1 AS 'Srno.', PR_PIECE_STAGE AS 'Piece Stage', GROUP_DESC AS 'Group Name', PR_PARTY_CODE AS 'Party Code', PARTY_NAME AS 'Party Name', PR_PIECE_NO  AS 'Piece No', PR_MACHINE_NO AS 'Machine No', POSITION_DESC AS 'Position Desc', PR_BILL_PRODUCT_CODE AS 'Bill Product Code', PRODUCT_DESC AS 'Product Desc', PR_GROUP AS 'Group', PR_BILL_STYLE AS 'Bill Style', PR_BILL_LENGTH AS 'Bill Length', PR_BILL_WIDTH AS 'Bill Width', PR_BILL_GSM AS 'Bill GSM', PR_BILL_SQMTR AS 'Bill Sq.Mtr', PR_BILL_WEIGHT AS 'Bill Weight', PR_OC_MONTHYEAR AS 'OC MONTH', PR_CURRENT_SCH_MONTH AS 'CURR SCH MONTH', PR_WARP_DATE AS 'WARP DATE', PR_WVG_DATE AS 'WVG DATE', PR_MND_DATE AS 'MND DATE', PR_NDL_DATE AS 'NDL DATE', PR_SEAM_DATE AS 'SEAM DATE', PR_FNSG_DATE AS 'FNSG DATE', PR_INVOICE_NO AS 'Invoice No', PR_INVOICE_DATE AS 'Invoice Date', PR_DIVERTED_DATE AS 'Diverted Date', PR_DELINK AS 'Obsolete Status', PR_OBSOLETE_DATE AS 'Obsolete Date', PR_DELINK_REASON AS 'Obsolete Reason', PR_DAYS_WH_STOCK AS 'Stock Days' FROM (SELECT @a:= 0) AS a,(SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_STAGE IN ('IN STOCK','BSR') AND DATEDIFF(CURDATE(),COALESCE(PR_FNSG_DATE,'1990-01-01'))>=180 AND COALESCE(PR_DELINK,'')='') AS PR LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010' AND APPROVED=1 AND CANCELLED=0) AS PM ON PR.PR_PARTY_CODE=PM.PARTY_CODE LEFT JOIN (SELECT H.GROUP_CODE,H.GROUP_DESC,D.PARTY_CODE AS GROUP_PARTY_CODE FROM  PRODUCTION.FELT_GROUP_MASTER_HEADER H,PRODUCTION.FELT_GROUP_MASTER_DETAIL D WHERE H.GROUP_CODE=D.GROUP_CODE AND H.APPROVED=1 AND H.CANCELED=0) AS GM ON PR.PR_PARTY_CODE=GM.GROUP_PARTY_CODE LEFT JOIN (SELECT DISTINCT PRODUCT_CODE,PRODUCT_DESC FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE APPROVED=1 AND CANCELED=0 ORDER BY DOC_NO DESC) AS QM ON PR.PR_PRODUCT_CODE=QM.PRODUCT_CODE LEFT JOIN (SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MST) AS MP ON PR.PR_POSITION_NO=MP.POSITION_NO LEFT JOIN (SELECT * FROM PRODUCTION.FELT_INCHARGE) AS IM ON PR.PR_INCHARGE=IM.INCHARGE_CD ";

        String sql2 = "SELECT @a:=@a+1 AS 'Srno.', PR_PIECE_STAGE AS 'Piece Stage', GROUP_DESC AS 'Group Name', PR_PARTY_CODE AS 'Party Code', PARTY_NAME AS 'Party Name', PR_PIECE_NO  AS 'Piece No', PR_MACHINE_NO AS 'Machine No', POSITION_DESC AS 'Position Desc', PR_BILL_PRODUCT_CODE AS 'Bill Product Code', PRODUCT_DESC AS 'Product Desc', PR_GROUP AS 'Group', PR_BILL_STYLE AS 'Bill Style', PR_BILL_LENGTH AS 'Bill Length', PR_BILL_WIDTH AS 'Bill Width', PR_BILL_GSM AS 'Bill GSM', PR_BILL_SQMTR AS 'Bill Sq.Mtr', PR_BILL_WEIGHT AS 'Bill Weight', PR_OC_MONTHYEAR AS 'OC MONTH', PR_CURRENT_SCH_MONTH AS 'CURR SCH MONTH', PR_WARP_DATE AS 'WARP DATE', PR_WVG_DATE AS 'WVG DATE', PR_MND_DATE AS 'MND DATE', PR_NDL_DATE AS 'NDL DATE', PR_SEAM_DATE AS 'SEAM DATE', PR_FNSG_DATE AS 'FNSG DATE', PR_INVOICE_NO AS 'Invoice No', PR_INVOICE_DATE AS 'Invoice Date', PR_DIVERTED_DATE AS 'Diverted Date', PR_DELINK AS 'Obsolete Status', PR_OBSOLETE_DATE AS 'Obsolete Date', PR_DELINK_REASON AS 'Obsolete Reason', PR_DAYS_WH_STOCK AS 'Stock Days' FROM (SELECT @a:= 0) AS a,(SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_STAGE IN ('IN STOCK','BSR') AND DATEDIFF(CURDATE(),COALESCE(PR_FNSG_DATE,'1990-01-01'))=180 AND COALESCE(PR_DELINK,'')='') AS PR LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010' AND APPROVED=1 AND CANCELLED=0) AS PM ON PR.PR_PARTY_CODE=PM.PARTY_CODE LEFT JOIN (SELECT H.GROUP_CODE,H.GROUP_DESC,D.PARTY_CODE AS GROUP_PARTY_CODE FROM  PRODUCTION.FELT_GROUP_MASTER_HEADER H,PRODUCTION.FELT_GROUP_MASTER_DETAIL D WHERE H.GROUP_CODE=D.GROUP_CODE AND H.APPROVED=1 AND H.CANCELED=0) AS GM ON PR.PR_PARTY_CODE=GM.GROUP_PARTY_CODE LEFT JOIN (SELECT DISTINCT PRODUCT_CODE,PRODUCT_DESC FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE APPROVED=1 AND CANCELED=0 ORDER BY DOC_NO DESC) AS QM ON PR.PR_PRODUCT_CODE=QM.PRODUCT_CODE LEFT JOIN (SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MST) AS MP ON PR.PR_POSITION_NO=MP.POSITION_NO LEFT JOIN (SELECT * FROM PRODUCTION.FELT_INCHARGE) AS IM ON PR.PR_INCHARGE=IM.INCHARGE_CD ";

        String sql3 = "SELECT @a:=@a+1 AS 'Srno.', PR_PIECE_STAGE AS 'Piece Stage', GROUP_DESC AS 'Group Name', PR_PARTY_CODE AS 'Party Code', PARTY_NAME AS 'Party Name', PR_PIECE_NO  AS 'Piece No', PR_MACHINE_NO AS 'Machine No', POSITION_DESC AS 'Position Desc', PR_BILL_PRODUCT_CODE AS 'Bill Product Code', PRODUCT_DESC AS 'Product Desc', PR_GROUP AS 'Group', PR_BILL_STYLE AS 'Bill Style', PR_BILL_LENGTH AS 'Bill Length', PR_BILL_WIDTH AS 'Bill Width', PR_BILL_GSM AS 'Bill GSM', PR_BILL_SQMTR AS 'Bill Sq.Mtr', PR_BILL_WEIGHT AS 'Bill Weight', PR_OC_MONTHYEAR AS 'OC MONTH', PR_CURRENT_SCH_MONTH AS 'CURR SCH MONTH', PR_WARP_DATE AS 'WARP DATE', PR_WVG_DATE AS 'WVG DATE', PR_MND_DATE AS 'MND DATE', PR_NDL_DATE AS 'NDL DATE', PR_SEAM_DATE AS 'SEAM DATE', PR_FNSG_DATE AS 'FNSG DATE', PR_INVOICE_NO AS 'Invoice No', PR_INVOICE_DATE AS 'Invoice Date', PR_DIVERTED_DATE AS 'Diverted Date', PR_DELINK AS 'Obsolete Status', PR_OBSOLETE_DATE AS 'Obsolete Date', PR_DELINK_REASON AS 'Obsolete Reason', PR_DAYS_WH_STOCK AS 'Stock Days' FROM (SELECT @a:= 0) AS a,(SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_STAGE IN ('INVOICED','EXP-INVOICE') AND PR_DAYS_WH_STOCK >=180 AND COALESCE(PR_DELINK,'')='' AND DATEDIFF(CURDATE(),COALESCE(PR_INVOICE_DATE,'1990-01-01'))=1 UNION ALL SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_STAGE IN ('DIVERTED') AND PR_DAYS_WH_STOCK >=180 AND COALESCE(PR_DELINK,'')='' AND DATEDIFF(CURDATE(),COALESCE(PR_DIVERTED_DATE,'1990-01-01'))=1) AS PR LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010' AND APPROVED=1 AND CANCELLED=0) AS PM ON PR.PR_PARTY_CODE=PM.PARTY_CODE LEFT JOIN (SELECT H.GROUP_CODE,H.GROUP_DESC,D.PARTY_CODE AS GROUP_PARTY_CODE FROM  PRODUCTION.FELT_GROUP_MASTER_HEADER H,PRODUCTION.FELT_GROUP_MASTER_DETAIL D WHERE H.GROUP_CODE=D.GROUP_CODE AND H.APPROVED=1 AND H.CANCELED=0) AS GM ON PR.PR_PARTY_CODE=GM.GROUP_PARTY_CODE LEFT JOIN (SELECT DISTINCT PRODUCT_CODE,PRODUCT_DESC FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE APPROVED=1 AND CANCELED=0 ORDER BY DOC_NO DESC) AS QM ON PR.PR_PRODUCT_CODE=QM.PRODUCT_CODE LEFT JOIN (SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MST) AS MP ON PR.PR_POSITION_NO=MP.POSITION_NO LEFT JOIN (SELECT * FROM PRODUCTION.FELT_INCHARGE) AS IM ON PR.PR_INCHARGE=IM.INCHARGE_CD ";
        
        String sql4 = "SELECT @a:=@a+1 AS 'Srno.', PR_PIECE_STAGE AS 'Piece Stage', GROUP_DESC AS 'Group Name', PR_PARTY_CODE AS 'Party Code', PARTY_NAME AS 'Party Name', PR_PIECE_NO  AS 'Piece No', PR_MACHINE_NO AS 'Machine No', POSITION_DESC AS 'Position Desc', PR_BILL_PRODUCT_CODE AS 'Bill Product Code', PRODUCT_DESC AS 'Product Desc', PR_GROUP AS 'Group', PR_BILL_STYLE AS 'Bill Style', PR_BILL_LENGTH AS 'Bill Length', PR_BILL_WIDTH AS 'Bill Width', PR_BILL_GSM AS 'Bill GSM', PR_BILL_SQMTR AS 'Bill Sq.Mtr', PR_BILL_WEIGHT AS 'Bill Weight', PR_OC_MONTHYEAR AS 'OC MONTH', PR_CURRENT_SCH_MONTH AS 'CURR SCH MONTH', PR_WARP_DATE AS 'WARP DATE', PR_WVG_DATE AS 'WVG DATE', PR_MND_DATE AS 'MND DATE', PR_NDL_DATE AS 'NDL DATE', PR_SEAM_DATE AS 'SEAM DATE', PR_FNSG_DATE AS 'FNSG DATE', PR_INVOICE_NO AS 'Invoice No', PR_INVOICE_DATE AS 'Invoice Date', PR_DIVERTED_DATE AS 'Diverted Date', PR_DELINK AS 'Obsolete Status', PR_OBSOLETE_DATE AS 'Obsolete Date', PR_DELINK_REASON AS 'Obsolete Reason', PR_DAYS_WH_STOCK AS 'Stock Days' FROM (SELECT @a:= 0) AS a,(SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_STAGE IN ('IN STOCK','BSR') AND PR_DAYS_WH_STOCK >=180 AND COALESCE(PR_DELINK,'')='OBSOLETE' AND DATEDIFF(CURDATE(),COALESCE(PR_OBSOLETE_DATE,'1990-01-01'))=1) AS PR LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010' AND APPROVED=1 AND CANCELLED=0) AS PM ON PR.PR_PARTY_CODE=PM.PARTY_CODE LEFT JOIN (SELECT H.GROUP_CODE,H.GROUP_DESC,D.PARTY_CODE AS GROUP_PARTY_CODE FROM  PRODUCTION.FELT_GROUP_MASTER_HEADER H,PRODUCTION.FELT_GROUP_MASTER_DETAIL D WHERE H.GROUP_CODE=D.GROUP_CODE AND H.APPROVED=1 AND H.CANCELED=0) AS GM ON PR.PR_PARTY_CODE=GM.GROUP_PARTY_CODE LEFT JOIN (SELECT DISTINCT PRODUCT_CODE,PRODUCT_DESC FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE APPROVED=1 AND CANCELED=0 ORDER BY DOC_NO DESC) AS QM ON PR.PR_PRODUCT_CODE=QM.PRODUCT_CODE LEFT JOIN (SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MST) AS MP ON PR.PR_POSITION_NO=MP.POSITION_NO LEFT JOIN (SELECT * FROM PRODUCTION.FELT_INCHARGE) AS IM ON PR.PR_INCHARGE=IM.INCHARGE_CD ";

        exprt.fillData(sql4 + "#" + sql3 + "#" + sql2 + "#" + sql1, new File("/Email_Attachment/DailyStockStatusAbove180Days.xls"), "Obsolete#Despatched,Diverted#NewAdded#Above_180");
        String responce = sendNotificationMailWithAttachement("Daily Stock Status above 180 days", pMessage, recievers, pCC, "/Email_Attachment/DailyStockStatusAbove180Days.xls", "DailyStockStatusAbove180Days.xls");
        System.out.println("Mail Response:" + responce);

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
