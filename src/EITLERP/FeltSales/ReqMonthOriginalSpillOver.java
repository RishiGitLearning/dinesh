/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales;

import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.common.JavaMail;
import EITLERP.clsExcel_Exporter;
import EITLERP.clsSales_Party;
import EITLERP.data;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author root
 */
public class ReqMonthOriginalSpillOver {

    /**
     * @param args the command line arguments
     */
    private static clsExcel_Exporter exprt = new clsExcel_Exporter();

    public static void main(String[] args) {
        // TODO code application logic here
        if (EITLERPGLOBAL.getCurrentDay() == 11) {
            UpdateDataProcess();
            ReqMonthOriginalNotification();
        }
    }

    private static void UpdateDataProcess() {
//        String mntForNonSDF = data.getStringValueFromDB("SELECT LAST_DAY(DATE_ADD(NOW(),INTERVAL 2 MONTH)) FROM DUAL"); closed on 10/09/2021
        String mntForNonSDF = data.getStringValueFromDB("SELECT LAST_DAY(DATE_ADD(NOW(),INTERVAL 1 MONTH)) FROM DUAL");
        String mntForSDF = data.getStringValueFromDB("SELECT LAST_DAY(DATE_ADD(NOW(),INTERVAL 1 MONTH)) FROM DUAL");

        data.Execute("DELETE FROM PRODUCTION.TMP_REQMTH_SPILLOVER_MTH_CLOSING");

        data.Execute("INSERT INTO PRODUCTION.TMP_REQMTH_SPILLOVER_MTH_CLOSING "
                + "(MTH_CLOSING_DATE,PIECE_STAGE,PIECE_NO,PARTY_CODE,OC_MONTHYEAR,OC_LAST_DDMMYY,REQ_MONTH,REQ_LAST_DDMMYY,REQ_MONTH_ORIGINAL,NEW_REQMTH,NEW_REQMTH_SCH_DDMMYY) "
                + "SELECT SUBSTRING(NOW(),1,10),PR_PIECE_STAGE,PR_PIECE_NO,PR_PARTY_CODE,PR_OC_MONTHYEAR,PR_OC_LAST_DDMMYY,PR_REQUESTED_MONTH, "
                + "PR_REQ_MTH_LAST_DDMMYY,PR_REQ_MONTH_ORIGINAL,PR_REQUESTED_MONTH,'" + mntForNonSDF + "' AS PR_REQ_MTH_LAST_DDMMYY "
                + "FROM PRODUCTION.FELT_SALES_PIECE_REGISTER  WHERE PR_REQ_MTH_LAST_DDMMYY <='" + mntForNonSDF + "' "
                + "AND COALESCE(PR_OC_MONTHYEAR,'') ='' AND PR_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5) "
                + "AND PR_PIECE_STAGE IN ('BOOKING') AND PR_GROUP != 'SDF' AND COALESCE(PR_REQUESTED_MONTH,'') !='' "
                + "AND COALESCE(PR_DELINK,'') != 'OBSOLETE' "
                + "UNION ALL "
                + "SELECT SUBSTRING(NOW(),1,10),PR_PIECE_STAGE,PR_PIECE_NO,PR_PARTY_CODE,PR_OC_MONTHYEAR,PR_OC_LAST_DDMMYY,PR_REQUESTED_MONTH, "
                + "PR_REQ_MTH_LAST_DDMMYY,PR_REQ_MONTH_ORIGINAL,PR_REQUESTED_MONTH,'" + mntForSDF + "' AS PR_REQ_MTH_LAST_DDMMYY "
                + "FROM PRODUCTION.FELT_SALES_PIECE_REGISTER  WHERE PR_REQ_MTH_LAST_DDMMYY <='" + mntForSDF + "' "
                + "AND COALESCE(PR_OC_MONTHYEAR,'') ='' AND PR_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5) "
                + "AND PR_PIECE_STAGE IN ('BOOKING') AND PR_GROUP = 'SDF' AND COALESCE(PR_REQUESTED_MONTH,'') !='' "
                + "AND COALESCE(PR_DELINK,'') != 'OBSOLETE' ");

        data.Execute("UPDATE PRODUCTION.TMP_REQMTH_SPILLOVER_MTH_CLOSING SET NEW_REQMTH_SCH_DDMMYY  = LAST_DAY(DATE_ADD(NEW_REQMTH_SCH_DDMMYY,INTERVAL 1 DAY)) ");

        data.Execute("UPDATE PRODUCTION.TMP_REQMTH_SPILLOVER_MTH_CLOSING SET NEW_REQMTH  = CONCAT(DATE_FORMAT(NEW_REQMTH_SCH_DDMMYY,'%b'),' - ',YEAR(NEW_REQMTH_SCH_DDMMYY)) ");

        data.Execute("UPDATE PRODUCTION.TMP_REQMTH_SPILLOVER_MTH_CLOSING SET REQ_MONTH_ORIGINAL  = REQ_MONTH WHERE COALESCE(REQ_MONTH_ORIGINAL,'') ='' ");

        data.Execute("UPDATE PRODUCTION.TMP_REQMTH_SPILLOVER_MTH_CLOSING ,PRODUCTION.FELT_SALES_PIECE_REGISTER "
                + "SET PR_REQ_MONTH_ORIGINAL = REQ_MONTH "
                + "WHERE PIECE_NO = PR_PIECE_NO "
                + "AND LENGTH(PR_REQ_MONTH_ORIGINAL) IS NULL ");

        data.Execute("UPDATE PRODUCTION.TMP_REQMTH_SPILLOVER_MTH_CLOSING ,PRODUCTION.FELT_SALES_PIECE_REGISTER "
                + "SET PR_REQUESTED_MONTH  = NEW_REQMTH "
                + "WHERE PIECE_NO = PR_PIECE_NO ");
    }

    private static void ReqMonthOriginalNotification() {

        String NonSDF = "", SDF = "";
        String mntCur = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(NOW(),'%b'),' - ',YEAR(NOW())) FROM DUAL");
//        String mntForNonSdf = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 3 MONTH),'%b'),' - ',YEAR(DATE_ADD(NOW(), INTERVAL 3 MONTH))) FROM DUAL"); closed on 10/09/2021
        String mntForNonSdf = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 2 MONTH),'%b'),' - ',YEAR(DATE_ADD(NOW(), INTERVAL 2 MONTH))) FROM DUAL");
        String mntForSdf = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 2 MONTH),'%b'),' - ',YEAR(DATE_ADD(NOW(), INTERVAL 2 MONTH))) FROM DUAL");

        String pSubject = "Requested Month of Last Month's Pieces has been ReSchedule to " + mntForNonSdf + " ( All Except SDLF ) and " + mntForSdf + " ( SDLF )";

        String pMessage = "Respected Sir,<br><br>"
                + "These files contains Last Month's Pieces has been Re-Scheduled to " + mntForNonSdf + " ( All Except SDLF ) and " + mntForSdf + " ( SDLF )<br>"
                + "Please find attached document herewith.";

//        String cc = "gaurang@dineshmills.com,dharmendra@dineshmills.com";
        String cc = "soumen@dineshmills.com,abtewary@dineshmills.com,aditya@dineshmills.com,sdmlerp@dineshmills.com";

        NonSDF = "SELECT @a:=@a+1 AS 'Sr No',PR_PIECE_STAGE AS 'Piece Stage',PR_WIP_STATUS AS 'WIP Status',GROUP_DESC AS 'Group Name',"
                + "PARTY_CODE AS 'Party Code',PARTY_NAME AS 'Party Name',PR_PIECE_NO AS 'Piece No',PR_MACHINE_NO AS 'Machine No',"
                + "POSITION_DESC AS 'Position Desc',PR_UPN AS 'UPN',PR_DOC_NO AS 'Order No',DATE_FORMAT(PR_ORDER_DATE,'%d/%m/%Y') AS 'Order Date',"
                + "PR_BILL_PRODUCT_CODE AS 'Product Code',PRODUCT_DESC AS 'Product Desc',PR_GROUP AS 'Group',PR_BILL_STYLE AS 'Style',"
                + "FORMAT(PR_BILL_LENGTH,2) AS 'Length',FORMAT(PR_BILL_WIDTH,2) AS 'Width',PR_BILL_GSM AS 'GSM',FORMAT(PR_BILL_SQMTR,2) AS 'Sq.Mtr',FORMAT(PR_BILL_WEIGHT,2) AS 'Thortical Weight',"
                + "PR_REQUESTED_MONTH AS 'Req Month',PR_REQ_MONTH_ORIGINAL AS 'Original Req Month',PR_OC_MONTHYEAR AS 'OC Month',"
                + "PR_CURRENT_SCH_MONTH AS 'Curr Sch Month',INCHARGE_NAME AS 'Incharge' FROM "
                + "(select @a:=0) as a, "                
                + "(SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                + "WHERE PR_REQUESTED_MONTH='" + mntForNonSdf + "' AND COALESCE(PR_REQ_MONTH_ORIGINAL,'') !='' "
                + "AND COALESCE(PR_OC_MONTHYEAR,'') ='' AND PR_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5) "
                + "AND PR_PIECE_STAGE IN ('BOOKING') AND PR_GROUP != 'SDF' AND COALESCE(PR_REQUESTED_MONTH,'') !='' "
                + "AND COALESCE(PR_DELINK,'') != 'OBSOLETE' ORDER BY PR_PARTY_CODE,PR_PIECE_NO "
                + ") AS PR "
                + "LEFT JOIN  "
                + "(SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER  "
                + "WHERE MAIN_ACCOUNT_CODE='210010' AND APPROVED=1 AND CANCELLED=0) AS PM  "
                + "ON PR.PR_PARTY_CODE=PM.PARTY_CODE  "
                + "LEFT JOIN  "
                + "(SELECT H.GROUP_CODE,H.GROUP_DESC,D.PARTY_CODE AS GROUP_PARTY_CODE  "
                + "FROM PRODUCTION.FELT_GROUP_MASTER_HEADER H,PRODUCTION.FELT_GROUP_MASTER_DETAIL D  "
                + "WHERE H.GROUP_CODE=D.GROUP_CODE  "
                + "AND H.APPROVED=1 AND H.CANCELED=0) AS GM  "
                + "ON PR.PR_PARTY_CODE=GM.GROUP_PARTY_CODE "
                + "LEFT JOIN  "
                + "(SELECT DISTINCT PRODUCT_CODE,PRODUCT_DESC FROM PRODUCTION.FELT_QLT_RATE_MASTER  "
                + "WHERE APPROVED=1 AND CANCELED=0 ORDER BY DOC_NO DESC) AS QM  "
                + "ON PR.PR_PRODUCT_CODE=QM.PRODUCT_CODE "
                + "LEFT JOIN  "
                + "(SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MST) AS MP  "
                + "ON PR.PR_POSITION_NO=MP.POSITION_NO "
                + "LEFT JOIN  "
                + "(SELECT * FROM PRODUCTION.FELT_INCHARGE) AS IM  "
                + "ON PR.PR_INCHARGE=IM.INCHARGE_CD ";

        SDF = "SELECT @a:=@a+1 AS 'Sr No',PR_PIECE_STAGE AS 'Piece Stage',PR_WIP_STATUS AS 'WIP Status',GROUP_DESC AS 'Group Name',"
                + "PARTY_CODE AS 'Party Code',PARTY_NAME AS 'Party Name',PR_PIECE_NO AS 'Piece No',PR_MACHINE_NO AS 'Machine No',"
                + "POSITION_DESC AS 'Position Desc',PR_UPN AS 'UPN',PR_DOC_NO AS 'Order No',DATE_FORMAT(PR_ORDER_DATE,'%d/%m/%Y') AS 'Order Date',"
                + "PR_BILL_PRODUCT_CODE AS 'Product Code',PRODUCT_DESC AS 'Product Desc',PR_GROUP AS 'Group',PR_BILL_STYLE AS 'Style',"
                + "FORMAT(PR_BILL_LENGTH,2) AS 'Length',FORMAT(PR_BILL_WIDTH,2) AS 'Width',PR_BILL_GSM AS 'GSM',FORMAT(PR_BILL_SQMTR,2) AS 'Sq.Mtr',FORMAT(PR_BILL_WEIGHT,2) AS 'Thortical Weight',"
                + "PR_REQUESTED_MONTH AS 'Req Month',PR_REQ_MONTH_ORIGINAL AS 'Original Req Month',PR_OC_MONTHYEAR AS 'OC Month',"
                + "PR_CURRENT_SCH_MONTH AS 'Curr Sch Month',INCHARGE_NAME AS 'Incharge' FROM "
                + "(select @a:=0) as a, "                
                + "(SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                + "WHERE PR_REQUESTED_MONTH='" + mntForSdf + "' AND COALESCE(PR_REQ_MONTH_ORIGINAL,'') !='' "
                + "AND COALESCE(PR_OC_MONTHYEAR,'') ='' AND PR_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5) "
                + "AND PR_PIECE_STAGE IN ('BOOKING') AND PR_GROUP = 'SDF' AND COALESCE(PR_REQUESTED_MONTH,'') !='' "
                + "AND COALESCE(PR_DELINK,'') != 'OBSOLETE' ORDER BY PR_PARTY_CODE,PR_PIECE_NO "
                + ") AS PR "
                + "LEFT JOIN  "
                + "(SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER  "
                + "WHERE MAIN_ACCOUNT_CODE='210010' AND APPROVED=1 AND CANCELLED=0) AS PM  "
                + "ON PR.PR_PARTY_CODE=PM.PARTY_CODE  "
                + "LEFT JOIN  "
                + "(SELECT H.GROUP_CODE,H.GROUP_DESC,D.PARTY_CODE AS GROUP_PARTY_CODE  "
                + "FROM PRODUCTION.FELT_GROUP_MASTER_HEADER H,PRODUCTION.FELT_GROUP_MASTER_DETAIL D  "
                + "WHERE H.GROUP_CODE=D.GROUP_CODE  "
                + "AND H.APPROVED=1 AND H.CANCELED=0) AS GM  "
                + "ON PR.PR_PARTY_CODE=GM.GROUP_PARTY_CODE "
                + "LEFT JOIN  "
                + "(SELECT DISTINCT PRODUCT_CODE,PRODUCT_DESC FROM PRODUCTION.FELT_QLT_RATE_MASTER  "
                + "WHERE APPROVED=1 AND CANCELED=0 ORDER BY DOC_NO DESC) AS QM  "
                + "ON PR.PR_PRODUCT_CODE=QM.PRODUCT_CODE "
                + "LEFT JOIN  "
                + "(SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MST) AS MP  "
                + "ON PR.PR_POSITION_NO=MP.POSITION_NO "
                + "LEFT JOIN  "
                + "(SELECT * FROM PRODUCTION.FELT_INCHARGE) AS IM  "
                + "ON PR.PR_INCHARGE=IM.INCHARGE_CD ";

//        String recievers = "gaurang@dineshmills.com";
        String recievers = "vdshanbhag@dineshmills.com,feltpp@dineshmills.com,narendramotiani@dineshmills.com,yrpatel@dineshmills.com,amitkanti@dineshmills.com";

        exprt.fillData(SDF + "#" + NonSDF, new File("/Email_Attachment/ReScheduleReqMonth.xls"), "SDF#All-Except SDF");

        String responce = sendNotificationMailWithAttachement(pSubject, pMessage, recievers, cc, "/Email_Attachment/ReScheduleReqMonth.xls", "ReScheduleReqMonth.xls");
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

}
