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
public class Working_Attendance_CTC_Email {

    /**
     * @param args the command line arguments
     */
    private static clsExcelExporter exprt = new clsExcelExporter();

    public static void main(String[] args) {
        // TODO code application logic here
        String sqlD = "SELECT MS_MM AS 'Month',MS_YYYY AS 'Year',MS_EMPID AS 'EmployeeID',EMP_NAME AS 'Name',DPTNAME AS 'Department',MS_PRESENT_WITHOUT_LC_DAYS AS 'Man Head',PRESENT_DAYS AS 'Present Days',LC_DAYS AS 'LC Days',LWP_DAYS AS 'LWP Days',PL AS 'PL',CL AS 'CL',SL AS 'SL',ESIC AS 'ESIC',PAID_DAYS AS 'Paid Days',MS_CTC_MTH_AMOUNT AS 'Paid Days Amount',MS_CTC_MAN_HEAD_AMOUNT AS 'Man Head Amount',MS_ROKDI_DAYS AS 'Rokadi Days',MS_ROKDI_AMOUNT AS 'Rokadi Amount',CTGNAME AS 'Category' FROM ( SELECT MS_EMPID, WORKING_EMP_DEPT_ID, MS_MM, MS_YYYY, TOTAL_MONTH_DAYS, PAID_DAYS, PRESENT_DAYS, LC_DAYS, LWP_DAYS, PL, CL, SL, OD, LOFF, EOFF, ESIC, WO, P_GP, O_GP, ABST, CO, PLE, CLE, SLE, TPHD, PHDL, HL, DA_DAYS, WOFF, NPH, NWOF, COFFE, NPL, MS_PETROL_LTRS, MS_CATEGORY, MS_MAIN_CATEGORY, MS_PRESENT_WITHOUT_LC_DAYS, MS_CREDIT_DAYS, MS_PREV_CREDIT_DAYS, MS_PH, MS_STAFF_WOFF, MS_WORKER_WOFF, MS_COMPANY_WOFF, MS_ROKDI_DAYS, COALESCE(MS_CTC_MTH_AMOUNT,0) AS MS_CTC_MTH_AMOUNT, COALESCE(MS_CTC_MAN_HEAD_AMOUNT,0) AS MS_CTC_MAN_HEAD_AMOUNT, COALESCE(MS_ROKDI_AMOUNT,0) AS MS_ROKDI_AMOUNT FROM SDMLATTPAY.WORKING_MTH_ATT WHERE 1=1  AND MS_MM= DATE_FORMAT(SUBDATE(CURDATE(), INTERVAL 1 MONTH),'%m')  AND MS_YYYY= DATE_FORMAT(SUBDATE(CURDATE(), INTERVAL 1 MONTH),'%Y') AND SUBSTRING(MS_EMPID,1,5) IN ('BRD00','BRD30','BRD40') AND SUBSTRING(MS_EMPID,1,6) NOT IN ('BRD008') ) AS MAS LEFT JOIN ( SELECT PAY_EMP_NO,EMP_NAME,EMP_DEPARTMENT,EMP_SHIFT_ID,EMP_MAIN_CATEGORY,EMP_CATEGORY FROM SDMLATTPAY.ATTPAY_EMPMST ) AS EMP ON MAS.MS_EMPID=EMP.PAY_EMP_NO LEFT JOIN ( SELECT ERP_DEPT,DPTID,NAME AS DPTNAME FROM SDMLATTPAY.ATT_DEPARTMENT_MASTER ) AS DPT ON DPT.DPTID=MAS.WORKING_EMP_DEPT_ID LEFT JOIN ( SELECT SECID,NAME AS SECNAME FROM SDMLATTPAY.ATT_MAIN_CATEGORY_MASTER ) AS SEC ON SEC.SECID=EMP.EMP_MAIN_CATEGORY LEFT JOIN ( SELECT CTGID,NAME AS CTGNAME FROM SDMLATTPAY.ATT_CATEGORY_MASTER ) AS CTG ON CTG.CTGID=EMP.EMP_CATEGORY LEFT JOIN ( SELECT SHIFT_ID,SHIFT_NAME FROM SDMLATTPAY.ATT_SHIFT ) AS SFT ON SFT.SHIFT_ID=EMP.EMP_SHIFT_ID WHERE 1=1 AND ERP_DEPT IN ('WEAVING','FINISHING','WAREHOUSE','CARDING','NEEDLING','MENDING','YARN STORE','FELTPP','ENGINEERING')  "
                + "UNION ALL "
                + "SELECT MS_MM,MS_YYYY,'TOTAL','TOTAL',DPTNAME,SUM(MS_PRESENT_WITHOUT_LC_DAYS) AS MAN_HEAD,SUM(PRESENT_DAYS),SUM(LC_DAYS),SUM(LWP_DAYS),SUM(PL),SUM(CL),SUM(SL),SUM(ESIC),SUM(PAID_DAYS),SUM(MS_CTC_MTH_AMOUNT),SUM(MS_CTC_MAN_HEAD_AMOUNT),SUM(MS_ROKDI_DAYS),SUM(MS_ROKDI_AMOUNT),'TOTAL' FROM ( SELECT MS_EMPID, WORKING_EMP_DEPT_ID, MS_MM, MS_YYYY, TOTAL_MONTH_DAYS, PAID_DAYS, PRESENT_DAYS, LC_DAYS, LWP_DAYS, PL, CL, SL, OD, LOFF, EOFF, ESIC, WO, P_GP, O_GP, ABST, CO, PLE, CLE, SLE, TPHD, PHDL, HL, DA_DAYS, WOFF, NPH, NWOF, COFFE, NPL, MS_PETROL_LTRS, MS_CATEGORY, MS_MAIN_CATEGORY, MS_PRESENT_WITHOUT_LC_DAYS, MS_CREDIT_DAYS, MS_PREV_CREDIT_DAYS, MS_PH, MS_STAFF_WOFF, MS_WORKER_WOFF, MS_COMPANY_WOFF,MS_ROKDI_DAYS, COALESCE(MS_CTC_MTH_AMOUNT,0) AS MS_CTC_MTH_AMOUNT, COALESCE(MS_CTC_MAN_HEAD_AMOUNT,0) AS MS_CTC_MAN_HEAD_AMOUNT, COALESCE(MS_ROKDI_AMOUNT,0) AS MS_ROKDI_AMOUNT FROM SDMLATTPAY.WORKING_MTH_ATT WHERE 1=1  AND MS_MM= DATE_FORMAT(SUBDATE(CURDATE(), INTERVAL 1 MONTH),'%m')  AND MS_YYYY= DATE_FORMAT(SUBDATE(CURDATE(), INTERVAL 1 MONTH),'%Y') AND SUBSTRING(MS_EMPID,1,5) IN ('BRD00','BRD30','BRD40') AND SUBSTRING(MS_EMPID,1,6) NOT IN ('BRD008')  ) AS MAS LEFT JOIN ( SELECT PAY_EMP_NO,EMP_NAME,EMP_DEPARTMENT,EMP_SHIFT_ID,EMP_MAIN_CATEGORY,EMP_CATEGORY FROM SDMLATTPAY.ATTPAY_EMPMST ) AS EMP ON MAS.MS_EMPID=EMP.PAY_EMP_NO LEFT JOIN ( SELECT ERP_DEPT,DPTID,NAME AS DPTNAME FROM SDMLATTPAY.ATT_DEPARTMENT_MASTER ) AS DPT ON DPT.DPTID=MAS.WORKING_EMP_DEPT_ID LEFT JOIN ( SELECT SECID,NAME AS SECNAME FROM SDMLATTPAY.ATT_MAIN_CATEGORY_MASTER ) AS SEC ON SEC.SECID=EMP.EMP_MAIN_CATEGORY LEFT JOIN ( SELECT CTGID,NAME AS CTGNAME FROM SDMLATTPAY.ATT_CATEGORY_MASTER ) AS CTG ON CTG.CTGID=EMP.EMP_CATEGORY LEFT JOIN ( SELECT SHIFT_ID,SHIFT_NAME FROM SDMLATTPAY.ATT_SHIFT ) AS SFT ON SFT.SHIFT_ID=EMP.EMP_SHIFT_ID WHERE 1=1 AND ERP_DEPT IN ('WEAVING','FINISHING','WAREHOUSE','CARDING','NEEDLING','MENDING','YARN STORE','FELTPP','ENGINEERING')  "
                + "GROUP BY MS_MM,MS_YYYY,DPTNAME "
                + "UNION ALL "
                + "SELECT MS_MM,MS_YYYY,'TOTAL' as e,'TOTAL' as m,'TOTAL' AS DPTNAME,SUM(MS_PRESENT_WITHOUT_LC_DAYS) AS MAN_HEAD,SUM(PRESENT_DAYS),SUM(LC_DAYS),SUM(LWP_DAYS),SUM(PL),SUM(CL),SUM(SL),SUM(ESIC),SUM(PAID_DAYS),SUM(MS_CTC_MTH_AMOUNT),SUM(MS_CTC_MAN_HEAD_AMOUNT),SUM(MS_ROKDI_DAYS),SUM(MS_ROKDI_AMOUNT),'TOTAL' FROM ( SELECT MS_EMPID, WORKING_EMP_DEPT_ID, MS_MM, MS_YYYY, TOTAL_MONTH_DAYS, PAID_DAYS, PRESENT_DAYS, LC_DAYS, LWP_DAYS, PL, CL, SL, OD, LOFF, EOFF, ESIC, WO, P_GP, O_GP, ABST, CO, PLE, CLE, SLE, TPHD, PHDL, HL, DA_DAYS, WOFF, NPH, NWOF, COFFE, NPL, MS_PETROL_LTRS, MS_CATEGORY, MS_MAIN_CATEGORY, MS_PRESENT_WITHOUT_LC_DAYS, MS_CREDIT_DAYS, MS_PREV_CREDIT_DAYS, MS_PH, MS_STAFF_WOFF, MS_WORKER_WOFF, MS_COMPANY_WOFF,MS_ROKDI_DAYS, COALESCE(MS_CTC_MTH_AMOUNT,0) AS MS_CTC_MTH_AMOUNT, COALESCE(MS_CTC_MAN_HEAD_AMOUNT,0) AS MS_CTC_MAN_HEAD_AMOUNT, COALESCE(MS_ROKDI_AMOUNT,0) AS MS_ROKDI_AMOUNT FROM SDMLATTPAY.WORKING_MTH_ATT WHERE 1=1  AND MS_MM= DATE_FORMAT(SUBDATE(CURDATE(), INTERVAL 1 MONTH),'%m')  AND MS_YYYY= DATE_FORMAT(SUBDATE(CURDATE(), INTERVAL 1 MONTH),'%Y') AND SUBSTRING(MS_EMPID,1,5) IN ('BRD00','BRD30','BRD40') AND SUBSTRING(MS_EMPID,1,6) NOT IN ('BRD008')  ) AS MAS LEFT JOIN ( SELECT PAY_EMP_NO,EMP_NAME,EMP_DEPARTMENT,EMP_SHIFT_ID,EMP_MAIN_CATEGORY,EMP_CATEGORY FROM SDMLATTPAY.ATTPAY_EMPMST ) AS EMP ON MAS.MS_EMPID=EMP.PAY_EMP_NO LEFT JOIN ( SELECT ERP_DEPT,DPTID,NAME AS DPTNAME FROM SDMLATTPAY.ATT_DEPARTMENT_MASTER ) AS DPT ON DPT.DPTID=MAS.WORKING_EMP_DEPT_ID LEFT JOIN ( SELECT SECID,NAME AS SECNAME FROM SDMLATTPAY.ATT_MAIN_CATEGORY_MASTER ) AS SEC ON SEC.SECID=EMP.EMP_MAIN_CATEGORY LEFT JOIN ( SELECT CTGID,NAME AS CTGNAME FROM SDMLATTPAY.ATT_CATEGORY_MASTER ) AS CTG ON CTG.CTGID=EMP.EMP_CATEGORY LEFT JOIN ( SELECT SHIFT_ID,SHIFT_NAME FROM SDMLATTPAY.ATT_SHIFT ) AS SFT ON SFT.SHIFT_ID=EMP.EMP_SHIFT_ID WHERE 1=1 AND ERP_DEPT IN ('WEAVING','FINISHING','WAREHOUSE','CARDING','NEEDLING','MENDING','YARN STORE','FELTPP','ENGINEERING')  "
                + "GROUP BY MS_MM,MS_YYYY "
//                + "ORDER BY DPTNAME,MS_EMPID,MS_YYYY,MS_MM  ";
                + "ORDER BY Department,EmployeeID,Year,Month  ";
//        System.out.println("SQL D:" + sqlD);
        String sqlS = "SELECT MS_MM AS 'Month',MS_YYYY AS 'Year','TOTAL' AS '','TOTAL' AS '',DPTNAME AS 'Department',SUM(MS_PRESENT_WITHOUT_LC_DAYS) AS 'Man Head',SUM(PRESENT_DAYS) AS 'Present Days',SUM(LC_DAYS) AS 'LC Days',SUM(LWP_DAYS) AS 'LWP Days',SUM(PL) AS 'PL',SUM(CL) AS 'CL',SUM(SL) AS 'SL',SUM(ESIC) AS 'ESIC',SUM(PAID_DAYS) AS 'Paid Days',SUM(MS_CTC_MTH_AMOUNT) AS 'Paid Days Amount',SUM(MS_CTC_MAN_HEAD_AMOUNT) AS 'Man Head Amount',SUM(MS_ROKDI_DAYS) AS 'Rokadi Days',SUM(MS_ROKDI_AMOUNT) AS 'Rokadi Amount','TOTAL' AS 'Total' FROM ( SELECT MS_EMPID, WORKING_EMP_DEPT_ID, MS_MM, MS_YYYY, TOTAL_MONTH_DAYS, PAID_DAYS, PRESENT_DAYS, LC_DAYS, LWP_DAYS, PL, CL, SL, OD, LOFF, EOFF, ESIC, WO, P_GP, O_GP, ABST, CO, PLE, CLE, SLE, TPHD, PHDL, HL, DA_DAYS, WOFF, NPH, NWOF, COFFE, NPL, MS_PETROL_LTRS, MS_CATEGORY, MS_MAIN_CATEGORY, MS_PRESENT_WITHOUT_LC_DAYS, MS_CREDIT_DAYS, MS_PREV_CREDIT_DAYS, MS_PH, MS_STAFF_WOFF, MS_WORKER_WOFF, MS_COMPANY_WOFF,MS_ROKDI_DAYS, COALESCE(MS_CTC_MTH_AMOUNT,0) AS MS_CTC_MTH_AMOUNT, COALESCE(MS_CTC_MAN_HEAD_AMOUNT,0) AS MS_CTC_MAN_HEAD_AMOUNT, COALESCE(MS_ROKDI_AMOUNT,0) AS MS_ROKDI_AMOUNT FROM SDMLATTPAY.WORKING_MTH_ATT WHERE 1=1  AND MS_MM= DATE_FORMAT(SUBDATE(CURDATE(), INTERVAL 1 MONTH),'%m')  AND MS_YYYY= DATE_FORMAT(SUBDATE(CURDATE(), INTERVAL 1 MONTH),'%Y') AND SUBSTRING(MS_EMPID,1,5) IN ('BRD00','BRD30','BRD40') AND SUBSTRING(MS_EMPID,1,6) NOT IN ('BRD008')  ) AS MAS LEFT JOIN ( SELECT PAY_EMP_NO,EMP_NAME,EMP_DEPARTMENT,EMP_SHIFT_ID,EMP_MAIN_CATEGORY,EMP_CATEGORY FROM SDMLATTPAY.ATTPAY_EMPMST ) AS EMP ON MAS.MS_EMPID=EMP.PAY_EMP_NO LEFT JOIN ( SELECT ERP_DEPT,DPTID,NAME AS DPTNAME FROM SDMLATTPAY.ATT_DEPARTMENT_MASTER ) AS DPT ON DPT.DPTID=MAS.WORKING_EMP_DEPT_ID LEFT JOIN ( SELECT SECID,NAME AS SECNAME FROM SDMLATTPAY.ATT_MAIN_CATEGORY_MASTER ) AS SEC ON SEC.SECID=EMP.EMP_MAIN_CATEGORY LEFT JOIN ( SELECT CTGID,NAME AS CTGNAME FROM SDMLATTPAY.ATT_CATEGORY_MASTER ) AS CTG ON CTG.CTGID=EMP.EMP_CATEGORY LEFT JOIN ( SELECT SHIFT_ID,SHIFT_NAME FROM SDMLATTPAY.ATT_SHIFT ) AS SFT ON SFT.SHIFT_ID=EMP.EMP_SHIFT_ID WHERE 1=1 AND ERP_DEPT IN ('WEAVING','FINISHING','WAREHOUSE','CARDING','NEEDLING','MENDING','YARN STORE','FELTPP','ENGINEERING')  "
                + "GROUP BY MS_MM,MS_YYYY,DPTNAME "
                + "UNION ALL "
                + "SELECT MS_MM,MS_YYYY,'TOTAL' as e,'TOTAL' as m,'TOTAL' AS DPTNAME,SUM(MS_PRESENT_WITHOUT_LC_DAYS) AS MAN_HEAD,SUM(PRESENT_DAYS),SUM(LC_DAYS),SUM(LWP_DAYS),SUM(PL),SUM(CL),SUM(SL),SUM(ESIC),SUM(PAID_DAYS),SUM(MS_CTC_MTH_AMOUNT),SUM(MS_CTC_MAN_HEAD_AMOUNT),SUM(MS_ROKDI_DAYS),SUM(MS_ROKDI_AMOUNT),'TOTAL' FROM ( SELECT MS_EMPID, WORKING_EMP_DEPT_ID, MS_MM, MS_YYYY, TOTAL_MONTH_DAYS, PAID_DAYS, PRESENT_DAYS, LC_DAYS, LWP_DAYS, PL, CL, SL, OD, LOFF, EOFF, ESIC, WO, P_GP, O_GP, ABST, CO, PLE, CLE, SLE, TPHD, PHDL, HL, DA_DAYS, WOFF, NPH, NWOF, COFFE, NPL, MS_PETROL_LTRS, MS_CATEGORY, MS_MAIN_CATEGORY, MS_PRESENT_WITHOUT_LC_DAYS, MS_CREDIT_DAYS, MS_PREV_CREDIT_DAYS, MS_PH, MS_STAFF_WOFF, MS_WORKER_WOFF, MS_COMPANY_WOFF,MS_ROKDI_DAYS, COALESCE(MS_CTC_MTH_AMOUNT,0) AS MS_CTC_MTH_AMOUNT, COALESCE(MS_CTC_MAN_HEAD_AMOUNT,0) AS MS_CTC_MAN_HEAD_AMOUNT, COALESCE(MS_ROKDI_AMOUNT,0) AS MS_ROKDI_AMOUNT FROM SDMLATTPAY.WORKING_MTH_ATT WHERE 1=1  AND MS_MM= DATE_FORMAT(SUBDATE(CURDATE(), INTERVAL 1 MONTH),'%m')  AND MS_YYYY= DATE_FORMAT(SUBDATE(CURDATE(), INTERVAL 1 MONTH),'%Y') AND SUBSTRING(MS_EMPID,1,5) IN ('BRD00','BRD30','BRD40') AND SUBSTRING(MS_EMPID,1,6) NOT IN ('BRD008')  ) AS MAS LEFT JOIN ( SELECT PAY_EMP_NO,EMP_NAME,EMP_DEPARTMENT,EMP_SHIFT_ID,EMP_MAIN_CATEGORY,EMP_CATEGORY FROM SDMLATTPAY.ATTPAY_EMPMST ) AS EMP ON MAS.MS_EMPID=EMP.PAY_EMP_NO LEFT JOIN ( SELECT ERP_DEPT,DPTID,NAME AS DPTNAME FROM SDMLATTPAY.ATT_DEPARTMENT_MASTER ) AS DPT ON DPT.DPTID=MAS.WORKING_EMP_DEPT_ID LEFT JOIN ( SELECT SECID,NAME AS SECNAME FROM SDMLATTPAY.ATT_MAIN_CATEGORY_MASTER ) AS SEC ON SEC.SECID=EMP.EMP_MAIN_CATEGORY LEFT JOIN ( SELECT CTGID,NAME AS CTGNAME FROM SDMLATTPAY.ATT_CATEGORY_MASTER ) AS CTG ON CTG.CTGID=EMP.EMP_CATEGORY LEFT JOIN ( SELECT SHIFT_ID,SHIFT_NAME FROM SDMLATTPAY.ATT_SHIFT ) AS SFT ON SFT.SHIFT_ID=EMP.EMP_SHIFT_ID WHERE 1=1 AND ERP_DEPT IN ('WEAVING','FINISHING','WAREHOUSE','CARDING','NEEDLING','MENDING','YARN STORE','FELTPP','ENGINEERING')  "
                + "GROUP BY MS_MM,MS_YYYY "
//                + "ORDER BY DPTNAME,MS_YYYY,MS_MM ";
                + "ORDER BY Department,Year,Month ";
//        System.out.println("SQL S:" + sqlS);
        
//        String recievers = "rishineekhra@dineshmills.com,gaurang@dineshmills.com";
        String recievers = "amitkanti@dineshmills.com,yrpatel@dineshmills.com,abtewary@dineshmills.com,rakeshdalal@dineshmills.com,hemantprajapati@dineshmills.com,brdfltweave1@dineshmills.com,brdfltneedle@dineshmills.com,balgondapatil@dineshmills.com,raghavendra@dineshmills.com,brdfltfin@dineshmills.com,feltwh@dineshmills.com,ppadhiyar@dineshmills.com,ilaxi_audit@dineshmills.com,rishineekhra@dineshmills.com";

        exprt.fillData(sqlD + "#" + sqlS, new File("/Email_Attachment/WORKING_ATTENDANCE_CTC.xls"), "DETAIL#SUMMARY");
        String mdate = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(SUBDATE(CURDATE(),INTERVAL 1 MONTH),'%b'),' - ',YEAR(SUBDATE(CURDATE(),INTERVAL 1 MONTH))) FROM DUAL");
        String responce = sendNotificationMailWithAttachement("Working Attendance with CTC For " + mdate, "", recievers, "sdmlerp@dineshmills.com", "/Email_Attachment/WORKING_ATTENDANCE_CTC.xls", "WORKING_ATTENDANCE_CTC.xls");
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
