/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SDMLATTPAY;

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
public class Attendance_Email {

    /**
     * @param args the command line arguments
     */
    private static clsExcelExporter exprt = new clsExcelExporter();

    public static void main(String[] args) {
        // TODO code application logic here
        String sql = "SELECT date_format(PUNCHDATE,'%d/%m/%Y') AS PDATE,DPTNAME AS Department,SUM(COALESCE(CASE WHEN CTG ='STAFF' THEN 1 ELSE 0 END,0)) AS STAFF "
                + "  ,SUM(COALESCE(CASE WHEN CTG ='WORKER' THEN 1 ELSE 0 END,0)) AS WORKER "
                + "  ,SUM(COALESCE(CASE WHEN CTG ='STAFF' THEN 1 ELSE 0 END,0)) + "
                + "  SUM(COALESCE(CASE WHEN CTG ='WORKER' THEN 1 ELSE 0 END,0)) AS TOTAL "
                + "  FROM "
                + "(SELECT PUNCHDATE,DPTID,DPTNAME,SECID,SECNAME,EMPID,EMP_NAME, "
                + "CASE WHEN SECID IN (2,5,9) THEN'STAFF' ELSE 'WORKER' END AS CTG FROM "
                + "(SELECT * FROM ( SELECT *, SEC_TO_TIME(TIME_TO_SEC(GP_FIRST_HALF) + TIME_TO_SEC(GP_SECOND_HALF) + TIME_TO_SEC(LATE_COMING_HRS) + TIME_TO_SEC(LUNCH_LATE_HRS) + TIME_TO_SEC(GP_ADDITIONAL_HRS) ) AS TOTAL_LATE_COMING FROM SDMLATTPAY.ATT_DATA_DAILY_SUMMARY WHERE YEAR(PUNCHDATE)=YYYY AND MONTH(PUNCHDATE)=MM "
                + " AND PUNCHDATE>= CURDATE() AND PUNCHDATE<= CURDATE() ) AS DAS "
                + " "
                + "LEFT JOIN ( SELECT PAY_EMP_NO,EMP_NAME,EMP_DEPARTMENT,EMP_MAIN_CATEGORY,EMP_CATEGORY FROM SDMLATTPAY.ATTPAY_EMPMST ) AS EMP ON DAS.EMPID=EMP.PAY_EMP_NO LEFT JOIN ( SELECT DPTID,NAME AS DPTNAME FROM SDMLATTPAY.ATT_DEPARTMENT_MASTER ) AS DPT ON DPT.DPTID=EMP.EMP_DEPARTMENT LEFT JOIN ( SELECT SECID,NAME AS SECNAME FROM SDMLATTPAY.ATT_MAIN_CATEGORY_MASTER ) AS SEC ON SEC.SECID=DAS.MAIN_CATEGORY LEFT JOIN ( SELECT CTGID,NAME AS CTGNAME FROM SDMLATTPAY.ATT_CATEGORY_MASTER ) AS CTG ON CTG.CTGID=DAS.CATEGORY LEFT JOIN ( SELECT *,'YES' AS SSS FROM SDMLATTPAY.ATT_SPECIAL_SANCTION WHERE APPROVED=1 AND CANCELED=0 ) AS SS ON DAS.EMPID=SS.EMP_CODE AND DAS.PUNCHDATE=SS.A_DATE "
                + "WHERE PUNCHES_NOS+0 !=0 AND MAIN_CATEGORY != 12) AS M) AS N "
                + "GROUP BY PUNCHDATE,DPTID,DPTNAME "
                + " "
                + "UNION ALL "
                + " "
                + " "
                + "SELECT date_format(PUNCHDATE,'%d/%m/%Y'),'TOTAL' AS DPTNAME,SUM(COALESCE(CASE WHEN CTG ='STAFF' THEN 1 ELSE 0 END,0)) AS STAFF "
                + "  ,SUM(COALESCE(CASE WHEN CTG ='WORKER' THEN 1 ELSE 0 END,0)) AS WORKER "
                + "  ,SUM(COALESCE(CASE WHEN CTG ='STAFF' THEN 1 ELSE 0 END,0)) + "
                + "  SUM(COALESCE(CASE WHEN CTG ='WORKER' THEN 1 ELSE 0 END,0)) AS TOTAL "
                + " "
                + "  FROM "
                + " "
                + "(SELECT PUNCHDATE,DPTID,DPTNAME,SECID,SECNAME,EMPID,EMP_NAME, "
                + "CASE WHEN SECID IN (2,5,9) THEN'STAFF' ELSE 'WORKER' END AS CTG FROM "
                + "(SELECT * FROM ( SELECT *, SEC_TO_TIME(TIME_TO_SEC(GP_FIRST_HALF) + TIME_TO_SEC(GP_SECOND_HALF) + TIME_TO_SEC(LATE_COMING_HRS) + TIME_TO_SEC(LUNCH_LATE_HRS) + TIME_TO_SEC(GP_ADDITIONAL_HRS) ) AS TOTAL_LATE_COMING FROM SDMLATTPAY.ATT_DATA_DAILY_SUMMARY WHERE YEAR(PUNCHDATE)=YYYY AND MONTH(PUNCHDATE)=MM "
                + " AND PUNCHDATE>= CURDATE() AND PUNCHDATE<=CURDATE() ) AS DAS "
                + " LEFT JOIN ( SELECT PAY_EMP_NO,EMP_NAME,EMP_DEPARTMENT,EMP_MAIN_CATEGORY,EMP_CATEGORY FROM SDMLATTPAY.ATTPAY_EMPMST ) AS EMP ON DAS.EMPID=EMP.PAY_EMP_NO LEFT JOIN ( SELECT DPTID,NAME AS DPTNAME FROM SDMLATTPAY.ATT_DEPARTMENT_MASTER ) AS DPT ON DPT.DPTID=EMP.EMP_DEPARTMENT LEFT JOIN ( SELECT SECID,NAME AS SECNAME FROM SDMLATTPAY.ATT_MAIN_CATEGORY_MASTER ) AS SEC ON SEC.SECID=DAS.MAIN_CATEGORY LEFT JOIN ( SELECT CTGID,NAME AS CTGNAME FROM SDMLATTPAY.ATT_CATEGORY_MASTER ) AS CTG ON CTG.CTGID=DAS.CATEGORY LEFT JOIN ( SELECT *,'YES' AS SSS FROM SDMLATTPAY.ATT_SPECIAL_SANCTION WHERE APPROVED=1 AND CANCELED=0 ) AS SS ON DAS.EMPID=SS.EMP_CODE AND DAS.PUNCHDATE=SS.A_DATE "
                + "WHERE PUNCHES_NOS+0 !=0 AND MAIN_CATEGORY != 12) AS M) AS N "
                + "GROUP BY PUNCHDATE "
                + "ORDER BY PDATE,Department";
        System.out.println("SQL:" + sql);
        String sql1 = "SELECT @a:=@a+1 AS SR_NO, "
                + "@row_number:=CASE WHEN @db_names=DPTNAME THEN @row_number+1 ELSE 1 END AS DEPT_SR_NO, "
                + "date_format(PUNCHDATE,'%d/%m/%Y') AS PDATE,CASE WHEN SECID IN (2,5,9) THEN'STAFF' ELSE 'WORKER' END AS CTG ,@db_names:=DPTNAME AS  Department, "
                + "EMPID,EMP_NAME,ALL_PUNCHES, "
                + "date_format(SYSDATE(),'%d/%m/%Y %h:%m') AS RUNDATE FROM (SELECT @a:= 0) AS a,(SELECT @row_number:=0,@db_names:='') AS t, "
                + "(SELECT * FROM ( SELECT *, SEC_TO_TIME(TIME_TO_SEC(GP_FIRST_HALF) + TIME_TO_SEC(GP_SECOND_HALF) + TIME_TO_SEC(LATE_COMING_HRS) + TIME_TO_SEC(LUNCH_LATE_HRS) + TIME_TO_SEC(GP_ADDITIONAL_HRS) ) AS TOTAL_LATE_COMING FROM SDMLATTPAY.ATT_DATA_DAILY_SUMMARY WHERE YEAR(PUNCHDATE)=YYYY AND MONTH(PUNCHDATE)=MM "
                + "AND PUNCHDATE>=CURDATE() AND PUNCHDATE<= CURDATE() ) "
                + "AS DAS LEFT JOIN ( SELECT PAY_EMP_NO,EMP_NAME,EMP_DEPARTMENT,EMP_MAIN_CATEGORY,EMP_CATEGORY FROM SDMLATTPAY.ATTPAY_EMPMST ) AS EMP ON DAS.EMPID=EMP.PAY_EMP_NO LEFT JOIN ( SELECT DPTID,NAME AS DPTNAME FROM SDMLATTPAY.ATT_DEPARTMENT_MASTER ) AS DPT ON DPT.DPTID=EMP.EMP_DEPARTMENT LEFT JOIN ( SELECT SECID,NAME AS SECNAME FROM SDMLATTPAY.ATT_MAIN_CATEGORY_MASTER ) AS SEC ON SEC.SECID=DAS.MAIN_CATEGORY LEFT JOIN ( SELECT CTGID,NAME AS CTGNAME FROM SDMLATTPAY.ATT_CATEGORY_MASTER ) AS CTG ON CTG.CTGID=DAS.CATEGORY LEFT JOIN ( SELECT *,'YES' AS SSS FROM SDMLATTPAY.ATT_SPECIAL_SANCTION WHERE APPROVED=1 AND CANCELED=0 ) AS SS ON DAS.EMPID=SS.EMP_CODE AND DAS.PUNCHDATE=SS.A_DATE "
                + "WHERE PUNCHES_NOS+0 !=0 AND MAIN_CATEGORY != 12) AS M "
                + "ORDER BY CTG,Department,EMPID";
        System.out.println("SQL:" + sql1);
        /////String recievers = "rishineekhra@dineshmills.com,dharmendra@dineshmills.com";
        //String recievers = "dharmendra@dineshmills.com";
        String recievers = "sunil@dineshmills.com,manhardave@dineshmills.com,neelanjan@dineshmills.com,rishineekhra@dineshmills.com";
        // String recievers = "aditya@dineshmills.com,soumen@dineshmills.com,vdshanbhag@dineshmills.com";

        exprt.fillData(sql1 + "#" + sql, new File("/Email_Attachment/ATTENDANCE.xls"), "DETAIL#SUMMARY");
        String mdate = data.getStringValueFromDB("SELECT DATE_FORMAT(CURDATE(),'%d/%m/%Y') FROM DUAL");
        String responce = sendNotificationMailWithAttachement(" Attendance For " + mdate, "", recievers, "sdmlerp@dineshmills.com", "/Email_Attachment/ATTENDANCE.xls", "ATTENDANCE.xls");
        System.out.println("Mail Response:" + responce);

//        exprt.fillData(sql1, new File("/Email_Attachment/DATEWISE_DEPTWISE_ATT_DETAIL.xls"), "ATTDTL");
//        responce = sendNotificationMailWithAttachement("Attendance Detail For " + mdate, "", recievers, "sdmlerp@dineshmills.com", "/Email_Attachment/DATEWISE_DEPTWISE_ATT_DETAIL.xls", "DATEWISE_DEPTWISE_ATT_DETAIL.xls");
//        System.out.println("Mail Response:" + responce);

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
