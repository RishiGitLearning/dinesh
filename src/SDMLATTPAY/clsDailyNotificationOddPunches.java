/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SDMLATTPAY;

import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.DailyMailNotification;
import EITLERP.FeltSales.Reports.clsExcelExporter;
import EITLERP.FeltSales.common.JavaMail;
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
public class clsDailyNotificationOddPunches {

    /**
     * @param args the command line arguments
     */
    private static clsExcelExporter exprt = new clsExcelExporter();

    public static void main(String[] args) {
        // TODO code application logic here
//        OddPunches();

//        String sql1 = "SELECT @a:=@a+1 AS SR_NO,SUPPLY_TYPE,SUB_TYPE,DOC_TYPE,DOC_NO,DATE_FORMAT(DOC_DATE,'%d/%m/%Y') AS DOC_DATE,FROM_OTHER_PARTY_NAME,FROM_GSTIN,FROM_ADDRESS1,FROM_ADDRESS2,FROM_PLACE,FROM_PINCODE,"
//                + "FROM_STATE,TO_OTHER_PARTY_NAME,TO_GSTIN,TO_ADDRESS1,TO_ADDRESS2,TO_PLACE,TO_PINCODE,TO_STATE,PRODUCT,DESCRIPTION,HSN,UNIT,QTY,ASSESSABLE_VALUE,"
//                + "TAX_RATE,CGST_AMOUNT,SGST_AMOUNT,IGST_AMOUNT,CESS_AMOUNT,TRANS_MODE,DISTANCE_LEVEL_KM,TRANSPORTER_NAME,TRANSPORTER_GSTIN_ID,TRANSPORTER_DOC_NO,"
//                + "CASE WHEN TRANSPORTER_DATE='0000-00-00' THEN '' ELSE TRANSPORTER_DATE END AS TRANSPORTER_DATE,VEHICLE_NO,ERROR_LIST "
//                + " FROM (SELECT @a:=0) AS a,DINESHMILLS.D_SAL_EWAY_REPORT "
//                + "WHERE DOC_DATE>=SUBDATE(CURDATE(),1) AND DOC_DATE <= SUBDATE(CURDATE(),1)  AND "
//                + "INVOICE_TYPE=2 AND APPROVED=1 AND CANCELLED=0 "
//                + "ORDER BY TRANSPORTER_ID,TRANSPORTER_NAME,DOC_DATE,DOC_NO";

        String sql = "SELECT EMPID AS 'Emp Pay No',EMP_NAME AS 'Emp Name',DPTNAME AS 'Department',DATE_FORMAT(PUNCHDATE,'%d/%m/%Y') AS 'Punch Date',PUNCHES_NOS AS 'No of Punches',SHIFT AS 'Shift',CONCAT(PRESENT_FIRST,' ',PRESENT_SECOND) AS 'Status',SUBSTRING(INTIME,11,16) AS 'In Time',SUBSTRING(OUTTIME,11,16) AS 'Out Time',ALL_PUNCHES AS 'Punches',SECNAME AS 'Category',CTGNAME AS 'Sub Category' FROM (SELECT * FROM SDMLATTPAY.ATT_DATA_DAILY_SUMMARY WHERE PUNCHES_NOS%2<>0 "
                + " AND MONTH(PUNCHDATE)=MM AND YEAR(PUNCHDATE)=YYYY AND MONTH(PUNCHDATE) = MONTH(CURDATE()) AND YEAR(PUNCHDATE) = YEAR(CURDATE()) AND PUNCHDATE <= SUBDATE(CURDATE(),2) ) AS ODD "
                + "LEFT JOIN ( SELECT PAY_EMP_NO,EMP_NAME,EMP_DEPARTMENT,EMP_MAIN_CATEGORY,EMP_CATEGORY FROM SDMLATTPAY.ATTPAY_EMPMST ) AS EMP "
                + "ON ODD.EMPID=EMP.PAY_EMP_NO "
                + "LEFT JOIN ( SELECT DPTID,NAME AS DPTNAME FROM SDMLATTPAY.ATT_DEPARTMENT_MASTER ) AS DPT "
                + "ON DPT.DPTID=EMP.EMP_DEPARTMENT "
                + "LEFT JOIN ( SELECT SECID,NAME AS SECNAME FROM SDMLATTPAY.ATT_MAIN_CATEGORY_MASTER ) AS SEC "
                + "ON SEC.SECID=ODD.MAIN_CATEGORY "
                + "LEFT JOIN ( SELECT CTGID,NAME AS CTGNAME FROM SDMLATTPAY.ATT_CATEGORY_MASTER ) AS CTG "
                + "ON CTG.CTGID=ODD.CATEGORY "
                + "ORDER BY PUNCHDATE,EMPID ";

        String recievers = "tk@dineshmills.com,dhaval_tk@dineshmills.com,manhardave@dineshmills.com";

        String mdate = data.getStringValueFromDB("SELECT DATE_FORMAT(SUBDATE(CURDATE(),2),'%d/%m/%Y') FROM DUAL");
        String mdate1 = data.getStringValueFromDB("SELECT DATE_FORMAT(SUBDATE(CURDATE(),2),'%Y_%m_%d') FROM DUAL");
        exprt.fillData(sql, new File("/Email_Attachment/Daily_ODD_Punche_Report_" + mdate1 + ".xls"), "ODD_PUNCH");

        String responce = sendNotificationMailWithAttachement("Daily ODD Punche(s) Report for Dt : " + mdate + ".", "<br>Dear All,<br><br>Daily ODD Punche(s) Details given in attachement.<br><br>", recievers, "rishineekhra@dineshmills.com,sdmlerp@dineshmills.com", "/Email_Attachment/Daily_ODD_Punche_Report_" + mdate1 + ".xls", "Daily_ODD_Punche_Report_" + mdate1 + ".xls");
        System.out.println("Mail Response:" + responce);
    }

    private static void OddPunches() {
        try {

            String forDate = data.getStringValueFromDB("SELECT DATE_SUB(CURDATE(), INTERVAL 2 DAY) FROM DUAL");

            String pSubject = "Daily ODD Punche(s) Report for Dt : " + EITLERPGLOBAL.formatDate(forDate) + ".";

            String pMessage = "";
            String cc = "rishineekhra@dineshmills.com";
            int SrNo = 1;

            pMessage = pMessage + "<br>Dear All,<br>";
            pMessage = pMessage + "<br>Daily ODD Punche(s) Details as given below.<br><br>";

            Connection Conn;
            Statement stmt;
            ResultSet rsData;

            Conn = data.getConn();
            stmt = Conn.createStatement();
            String strSQL = "SELECT * FROM (SELECT * FROM SDMLATTPAY.ATT_DATA_DAILY_SUMMARY WHERE PUNCHES_NOS%2<>0 AND MONTH(PUNCHDATE)=MM AND YEAR(PUNCHDATE)=YYYY AND PUNCHDATE='" + forDate + "' ) AS ODD "
                    + "LEFT JOIN ( SELECT PAY_EMP_NO,EMP_NAME,EMP_DEPARTMENT,EMP_MAIN_CATEGORY,EMP_CATEGORY FROM SDMLATTPAY.ATTPAY_EMPMST ) AS EMP "
                    + "ON ODD.EMPID=EMP.PAY_EMP_NO "
                    + "LEFT JOIN ( SELECT DPTID,NAME AS DPTNAME FROM SDMLATTPAY.ATT_DEPARTMENT_MASTER ) AS DPT "
                    + "ON DPT.DPTID=EMP.EMP_DEPARTMENT "
                    + "LEFT JOIN ( SELECT SECID,NAME AS SECNAME FROM SDMLATTPAY.ATT_MAIN_CATEGORY_MASTER ) AS SEC "
                    + "ON SEC.SECID=ODD.MAIN_CATEGORY "
                    + "LEFT JOIN ( SELECT CTGID,NAME AS CTGNAME FROM SDMLATTPAY.ATT_CATEGORY_MASTER ) AS CTG "
                    + "ON CTG.CTGID=ODD.CATEGORY ";

            rsData = stmt.executeQuery(strSQL);
            rsData.first();

            System.out.println("String StrSQL : " + strSQL);

            if (rsData.getRow() > 0) {
                pMessage = pMessage + "<table border='1' cellpadding='10'>"
                        + "<tr>"
                        + "<th align='center'> SrNo </th>"
                        + "<th align='center'> Emp Pay No </th>"
                        + "<th align='center'> Emp Name </th>"
                        + "<th align='center'> Department </th>"
                        + "<th align='center'> Punch Date </th>"
                        + "<th align='center'> No of Punches </th>"
                        + "<th align='center'> Shift </th>"
                        + "<th align='center'> Status </th>"
                        + "<th align='center'> In Time </th>"
                        + "<th align='center'> Out Time </th>"
                        + "<th align='center'> Punches </th>"
                        + "<th align='center'> Category </th>"
                        + "<th align='center'> Sub Category </th>"
                        + "</tr>";

                rsData.first();
                if (rsData.getRow() > 0) {
                    while (!rsData.isAfterLast()) {

                        pMessage = pMessage + ""
                                + "<tr>"
                                + "<td align='right'> " + (SrNo++) + " </td>"
                                + "<td align='left'> " + rsData.getString("EMPID") + " </td>"
                                + "<td align='center'> " + rsData.getString("EMP_NAME") + " </td>"
                                + "<td align='left'> " + rsData.getString("DPTNAME") + " </td>"
                                + "<td align='center'> " + EITLERPGLOBAL.formatDate(rsData.getString("PUNCHDATE")) + " </td>"
                                + "<td align='left'> " + rsData.getString("PUNCHES_NOS") + " </td>"
                                + "<td align='center'> " + rsData.getString("SHIFT") + " </td>"
                                + "<td align='left'> " + (rsData.getString("PRESENT_FIRST") + " " + rsData.getString("PRESENT_SECOND")) + " </td>"
                                + "<td align='left'> " + rsData.getString("INTIME").substring(11, 16) + " </td>"
                                + "<td align='left'> " + rsData.getString("OUTTIME").substring(11, 16) + " </td>"
                                + "<td align='right'> " + rsData.getString("ALL_PUNCHES") + " </td>"
                                + "<td align='right'> " + rsData.getString("SECNAME") + " </td>"
                                + "<td align='right'> " + rsData.getString("CTGNAME") + " </td>"
                                + "</tr>";

                        rsData.next();
                    }
                }
                pMessage = pMessage + "</table>";
            } else {
                pMessage = pMessage + "<br>No Record Found.<br>";
            }

            pMessage += "<br>";

//            String recievers = "sdmlerp@dineshmills.com";
            String recievers = "gaurang@dineshmills.com,sdmlerp@dineshmills.com";

            pMessage = pMessage + "<br><br>**** This is an auto-generated email, please do not reply ****";

//            System.out.println("Recivers : " + recievers);
//            System.out.println("pSubject : " + pSubject);
//            System.out.println("pMessage : " + pMessage);
            String responce = DailyMailNotification.sendNotificationMail(pSubject, pMessage, recievers, cc);
            System.out.println("Send Mail Responce : " + responce);

        } catch (Exception e) {
            System.out.println("Error on Mail: " + e.getMessage());
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

}
