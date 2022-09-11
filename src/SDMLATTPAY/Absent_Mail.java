/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SDMLATTPAY;

import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.Reports.clsExcelExporter;
import EITLERP.FeltSales.common.JavaMail;
import EITLERP.data;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author dharmendra
 */
public class Absent_Mail {

    /**
     * @param args the command line arguments
     */
    private static clsExcelExporter exprt = new clsExcelExporter();

    public static void main(String[] args) {
        // TODO code application logic here
        String mcurday;
        mcurday = data.getStringValueFromDB("SELECT DAYNAME(CURDATE()) FROM DUAL");
        //if (mcurday.equalsIgnoreCase("Monday")) {
            try {
                String mstaff = "SELECT @a:=@a+1 AS SR_NO,ABSDATA.* FROM (SELECT @a:=0) AS a,(SELECT EMPID as Employee,COALESCE(EMP_NAME,'') AS NAME,DATE_FORMAT(PUNCHDATE,'%d/%m/%Y') AS PunchDate,COALESCE(SHIFT,'') AS SHIFT,"
                        + "CONCAT(PRESENT_FIRST,PRESENT_SECOND) AS PRESENT,COALESCE(ALL_PUNCHES,'') AS PUNCHES,COALESCE(DPTNAME,'') AS Department,COALESCE(SECNAME,'') AS Category,COALESCE(CTGNAME,'') AS SubCategory "
                        + "FROM ( SELECT * FROM SDMLATTPAY.ATT_DATA_DAILY_SUMMARY "
                        + "WHERE (EMPID LIKE 'BRD1%' OR EMPID LIKE 'BRD2%') AND PUNCHDATE<=DATE_SUB(CURDATE(),INTERVAL 3 DAY) AND PUNCHDATE>=CASE WHEN DATE_SUB(CURDATE(),INTERVAL DAY(CURDATE())-1 DAY)<DATE_SUB(CURDATE(),INTERVAL 9 DAY) THEN "
                        + "DATE_SUB(CURDATE(),INTERVAL DAY(CURDATE())-1 DAY) ELSE DATE_SUB(CURDATE(),INTERVAL 9 DAY) END "
                        + "AND YEAR(PUNCHDATE)=YYYY AND MONTH(PUNCHDATE)=MM AND SHIFT NOT IN (9) AND PRESENT_FIRST='A' AND PRESENT_SECOND='A' AND PUNCHES_NOS=0) AS DAS "
                        + "LEFT JOIN ( SELECT PAY_EMP_NO,EMP_NAME,EMP_DEPARTMENT,EMP_MAIN_CATEGORY,EMP_CATEGORY FROM SDMLATTPAY.ATTPAY_EMPMST ) AS EMP ON DAS.EMPID=EMP.PAY_EMP_NO "
                        + "LEFT JOIN ( SELECT DPTID,NAME AS DPTNAME FROM SDMLATTPAY.ATT_DEPARTMENT_MASTER ) AS DPT ON DPT.DPTID=EMP.EMP_DEPARTMENT "
                        + "LEFT JOIN ( SELECT SECID,NAME AS SECNAME FROM SDMLATTPAY.ATT_MAIN_CATEGORY_MASTER ) AS SEC ON SEC.SECID=DAS.MAIN_CATEGORY "
                        + "LEFT JOIN ( SELECT CTGID,NAME AS CTGNAME FROM SDMLATTPAY.ATT_CATEGORY_MASTER ) AS CTG ON CTG.CTGID=DAS.CATEGORY "
                        + "LEFT JOIN ( SELECT *,'YES' AS SSS FROM SDMLATTPAY.ATT_SPECIAL_SANCTION WHERE APPROVED=1 AND CANCELED=0 ) AS SS ON DAS.EMPID=SS.EMP_CODE AND DAS.PUNCHDATE=SS.A_DATE "
                        //+ "LEFT JOIN ( SELECT * FROM SDMLATTPAY.ATT_LEAVE_ENTRY E WHERE LVT_LEAVE_TYPE = 3 ) AS LV ON DAS.EMPID=LVT_PAY_EMPID AND DAS.PUNCHDATE >= LVT_FROMDATE AND DAS.PUNCHDATE <= LVT_TODATE "
                        //+ "WHERE COALESCE(LVT_LEAVE_CODE,'')='' "
                        + "ORDER BY DPTID,EMPID,PUNCHDATE) AS ABSDATA";
                String mworker = "SELECT @a:=@a+1 AS SR_NO,ABSDATA.* FROM (SELECT @a:=0) AS a,(SELECT EMPID as Employee,COALESCE(EMP_NAME,'') AS NAME,DATE_FORMAT(PUNCHDATE,'%d/%m/%Y') AS PunchDate,COALESCE(SHIFT,'') AS SHIFT,"
                        + "CONCAT(PRESENT_FIRST,PRESENT_SECOND) AS PRESENT,COALESCE(ALL_PUNCHES,'') AS PUNCHES,COALESCE(DPTNAME,'') AS Department,COALESCE(SECNAME,'') AS Category,COALESCE(CTGNAME,'') AS SubCategory "
                        + "FROM ( SELECT * FROM SDMLATTPAY.ATT_DATA_DAILY_SUMMARY "
                        + "WHERE (EMPID LIKE 'BRD3%' OR EMPID LIKE 'BRD4%') AND PUNCHDATE<=DATE_SUB(CURDATE(),INTERVAL 3 DAY) AND PUNCHDATE>=CASE WHEN DATE_SUB(CURDATE(),INTERVAL DAY(CURDATE())-1 DAY)<DATE_SUB(CURDATE(),INTERVAL 9 DAY) THEN "
                        + "DATE_SUB(CURDATE(),INTERVAL DAY(CURDATE())-1 DAY) ELSE DATE_SUB(CURDATE(),INTERVAL 9 DAY) END "
                        + "AND YEAR(PUNCHDATE)=YYYY AND MONTH(PUNCHDATE)=MM AND SHIFT NOT IN (9) AND PRESENT_FIRST='A' AND PRESENT_SECOND='A' AND PUNCHES_NOS=0) AS DAS "
                        + "LEFT JOIN ( SELECT PAY_EMP_NO,EMP_NAME,EMP_DEPARTMENT,EMP_MAIN_CATEGORY,EMP_CATEGORY FROM SDMLATTPAY.ATTPAY_EMPMST ) AS EMP ON DAS.EMPID=EMP.PAY_EMP_NO "
                        + "LEFT JOIN ( SELECT DPTID,NAME AS DPTNAME FROM SDMLATTPAY.ATT_DEPARTMENT_MASTER ) AS DPT ON DPT.DPTID=EMP.EMP_DEPARTMENT "
                        + "LEFT JOIN ( SELECT SECID,NAME AS SECNAME FROM SDMLATTPAY.ATT_MAIN_CATEGORY_MASTER ) AS SEC ON SEC.SECID=DAS.MAIN_CATEGORY "
                        + "LEFT JOIN ( SELECT CTGID,NAME AS CTGNAME FROM SDMLATTPAY.ATT_CATEGORY_MASTER ) AS CTG ON CTG.CTGID=DAS.CATEGORY "
                        + "LEFT JOIN ( SELECT *,'YES' AS SSS FROM SDMLATTPAY.ATT_SPECIAL_SANCTION WHERE APPROVED=1 AND CANCELED=0 ) AS SS ON DAS.EMPID=SS.EMP_CODE AND DAS.PUNCHDATE=SS.A_DATE "
                        //+ "LEFT JOIN ( SELECT * FROM SDMLATTPAY.ATT_LEAVE_ENTRY E WHERE LVT_LEAVE_TYPE = 3 ) AS LV ON DAS.EMPID=LVT_PAY_EMPID AND DAS.PUNCHDATE >= LVT_FROMDATE AND DAS.PUNCHDATE <= LVT_TODATE "
                        //+ "WHERE COALESCE(LVT_LEAVE_CODE,'')='' "
                        + "ORDER BY DPTID,EMPID,PUNCHDATE) AS ABSDATA";
                String mretainer = "SELECT @a:=@a+1 AS SR_NO,ABSDATA.* FROM (SELECT @a:=0) AS a,(SELECT EMPID as Employee,COALESCE(EMP_NAME,'') AS NAME,DATE_FORMAT(PUNCHDATE,'%d/%m/%Y') AS PunchDate,COALESCE(SHIFT,'') AS SHIFT,"
                        + "CONCAT(PRESENT_FIRST,PRESENT_SECOND) AS PRESENT,COALESCE(ALL_PUNCHES,'') AS PUNCHES,COALESCE(DPTNAME,'') AS Department,COALESCE(SECNAME,'') AS Category,COALESCE(CTGNAME,'') AS SubCategory "
                        + "FROM ( SELECT * FROM SDMLATTPAY.ATT_DATA_DAILY_SUMMARY "
                        + "WHERE (EMPID LIKE 'BRD5%' OR EMPID LIKE 'BRD6%') AND PUNCHDATE<=DATE_SUB(CURDATE(),INTERVAL 3 DAY) AND PUNCHDATE>=CASE WHEN DATE_SUB(CURDATE(),INTERVAL DAY(CURDATE())-1 DAY)<DATE_SUB(CURDATE(),INTERVAL 9 DAY) THEN "
                        + "DATE_SUB(CURDATE(),INTERVAL DAY(CURDATE())-1 DAY) ELSE DATE_SUB(CURDATE(),INTERVAL 9 DAY) END "
                        + "AND YEAR(PUNCHDATE)=YYYY AND MONTH(PUNCHDATE)=MM AND SHIFT NOT IN (9) AND PRESENT_FIRST='A' AND PRESENT_SECOND='A' AND PUNCHES_NOS=0) AS DAS "
                        + "LEFT JOIN ( SELECT PAY_EMP_NO,EMP_NAME,EMP_DEPARTMENT,EMP_MAIN_CATEGORY,EMP_CATEGORY FROM SDMLATTPAY.ATTPAY_EMPMST ) AS EMP ON DAS.EMPID=EMP.PAY_EMP_NO "
                        + "LEFT JOIN ( SELECT DPTID,NAME AS DPTNAME FROM SDMLATTPAY.ATT_DEPARTMENT_MASTER ) AS DPT ON DPT.DPTID=EMP.EMP_DEPARTMENT "
                        + "LEFT JOIN ( SELECT SECID,NAME AS SECNAME FROM SDMLATTPAY.ATT_MAIN_CATEGORY_MASTER ) AS SEC ON SEC.SECID=DAS.MAIN_CATEGORY "
                        + "LEFT JOIN ( SELECT CTGID,NAME AS CTGNAME FROM SDMLATTPAY.ATT_CATEGORY_MASTER ) AS CTG ON CTG.CTGID=DAS.CATEGORY "
                        + "LEFT JOIN ( SELECT *,'YES' AS SSS FROM SDMLATTPAY.ATT_SPECIAL_SANCTION WHERE APPROVED=1 AND CANCELED=0 ) AS SS ON DAS.EMPID=SS.EMP_CODE AND DAS.PUNCHDATE=SS.A_DATE "
                        //+ "LEFT JOIN ( SELECT * FROM SDMLATTPAY.ATT_LEAVE_ENTRY E WHERE LVT_LEAVE_TYPE = 3 ) AS LV ON DAS.EMPID=LVT_PAY_EMPID AND DAS.PUNCHDATE >= LVT_FROMDATE AND DAS.PUNCHDATE <= LVT_TODATE "
                        //+ "WHERE COALESCE(LVT_LEAVE_CODE,'')='' "
                        + "ORDER BY DPTID,EMPID,PUNCHDATE) AS ABSDATA";
                String mcontract = "SELECT @a:=@a+1 AS SR_NO,ABSDATA.* FROM (SELECT @a:=0) AS a,(SELECT EMPID as Employee,COALESCE(EMP_NAME,'') AS NAME,DATE_FORMAT(PUNCHDATE,'%d/%m/%Y') AS PunchDate,COALESCE(SHIFT,'') AS SHIFT,"
                        + "CONCAT(PRESENT_FIRST,PRESENT_SECOND) AS PRESENT,COALESCE(ALL_PUNCHES,'') AS PUNCHES,COALESCE(DPTNAME,'') AS Department,COALESCE(SECNAME,'') AS Category,COALESCE(CTGNAME,'') AS SubCategory "
                        + "FROM ( SELECT * FROM SDMLATTPAY.ATT_DATA_DAILY_SUMMARY "
                        + "WHERE (EMPID LIKE 'BRD0%') AND PUNCHDATE<=DATE_SUB(CURDATE(),INTERVAL 3 DAY) AND PUNCHDATE>=CASE WHEN DATE_SUB(CURDATE(),INTERVAL DAY(CURDATE())-1 DAY)<DATE_SUB(CURDATE(),INTERVAL 9 DAY) THEN "
                        + "DATE_SUB(CURDATE(),INTERVAL DAY(CURDATE())-1 DAY) ELSE DATE_SUB(CURDATE(),INTERVAL 9 DAY) END "
                        + "AND YEAR(PUNCHDATE)=YYYY AND MONTH(PUNCHDATE)=MM AND SHIFT NOT IN (9) AND PRESENT_FIRST='A' AND PRESENT_SECOND='A' AND PUNCHES_NOS=0) AS DAS "
                        + "LEFT JOIN ( SELECT PAY_EMP_NO,EMP_NAME,EMP_DEPARTMENT,EMP_MAIN_CATEGORY,EMP_CATEGORY FROM SDMLATTPAY.ATTPAY_EMPMST ) AS EMP ON DAS.EMPID=EMP.PAY_EMP_NO "
                        + "LEFT JOIN ( SELECT DPTID,NAME AS DPTNAME FROM SDMLATTPAY.ATT_DEPARTMENT_MASTER ) AS DPT ON DPT.DPTID=EMP.EMP_DEPARTMENT "
                        + "LEFT JOIN ( SELECT SECID,NAME AS SECNAME FROM SDMLATTPAY.ATT_MAIN_CATEGORY_MASTER ) AS SEC ON SEC.SECID=DAS.MAIN_CATEGORY "
                        + "LEFT JOIN ( SELECT CTGID,NAME AS CTGNAME FROM SDMLATTPAY.ATT_CATEGORY_MASTER ) AS CTG ON CTG.CTGID=DAS.CATEGORY "
                        + "LEFT JOIN ( SELECT *,'YES' AS SSS FROM SDMLATTPAY.ATT_SPECIAL_SANCTION WHERE APPROVED=1 AND CANCELED=0 ) AS SS ON DAS.EMPID=SS.EMP_CODE AND DAS.PUNCHDATE=SS.A_DATE "
                        //+ "LEFT JOIN ( SELECT * FROM SDMLATTPAY.ATT_LEAVE_ENTRY E WHERE LVT_LEAVE_TYPE = 3 ) AS LV ON DAS.EMPID=LVT_PAY_EMPID AND DAS.PUNCHDATE >= LVT_FROMDATE AND DAS.PUNCHDATE <= LVT_TODATE "
                        //+ "WHERE COALESCE(LVT_LEAVE_CODE,'')='' "
                        + "ORDER BY DPTID,EMPID,PUNCHDATE) AS ABSDATA";
                String recievers = "rishineekhra@dineshmills.com";
//            int srNo = 0;
//            String pBody = "Full Day Absent List";
//            ResultSet disdata = data.getResult(mstaff);
//            disdata.first();
//
//            if (disdata.getRow() > 0) {
//                srNo = 0;
//                pBody += "<br>";
//                pBody += "<table border=1>";
//                pBody += "<tr><th align='center'><b>Sr.No.</b></th>"
//                        + "<th align='center'><b>Emp Code</b></th>"
//                        + "<th align='center'><b>Name</b></th>"
//                        + "<th align='center'><b>Date</b></th>"
//                        + "<th align='center'><b>SHIFT</b></th>"
//                        + "<th align='center'><b>Punches</b></th>"
//                        + "<th align='center'><b>Department</b></th>"
//                        + "<th align='center'><b>Category</b></th>"
//                        + "<th align='center'><b>Sub Category</b></th>"
//                        + "</tr>";
//                while (!disdata.isAfterLast()) {
//                    srNo++;
//                    pBody += "<tr>";
//                    pBody += "<td>" + srNo + "</td>";
//                    pBody += "<td>" + disdata.getString("Employee") + "</td>";
//                    pBody += "<td>" + disdata.getString("NAME") + "</td>";
//                    pBody += "<td>" + disdata.getString("PunchDate") + "</td>";
//                    pBody += "<td>" + disdata.getString("SHIFT") + "</td>";
//                    pBody += "<td>" + disdata.getString("PUNCHES") + "</td>";
//                    pBody += "<td>" + disdata.getString("Department") + "</td>";
//                    pBody += "<td>" + disdata.getString("Category") + "</td>";
//                    pBody += "<td>" + disdata.getString("SubCategory") + "</td>";
//                    pBody += "</tr>";
//                    disdata.next();
//                }
//                pBody += "</table>";
//                pBody += "<br><br>";
//                pBody += "<br>";
//            }

                String mdate = data.getStringValueFromDB("SELECT DATE_FORMAT(CASE WHEN DATE_SUB(CURDATE(),INTERVAL DAY(CURDATE())-1 DAY)<DATE_SUB(CURDATE(),INTERVAL 9 DAY) THEN "
                        + "DATE_SUB(CURDATE(),INTERVAL DAY(CURDATE())-1 DAY) ELSE DATE_SUB(CURDATE(),INTERVAL 9 DAY) END,'%d/%m/%Y') FROM DUAL");
                String mdate1 = data.getStringValueFromDB("SELECT DATE_FORMAT(SUBDATE(CURDATE(),3),'%d/%m/%Y') FROM DUAL");
                //exprt.fillData(sql, new File("/Email_Attachment/FullAbsent.xls"), "FAB");
                exprt.fillData(mcontract + "#" + mretainer + "#" + mworker + "#" + mstaff, new File("/Email_Attachment/FullAbsent.xls"), "CONTRACT#RETAINER#WORKER#STAFF");
                exprt.fillData(mcontract + "#" + mretainer + "#" + mworker + "#" + mstaff, new File("E:/FullAbsent.xls"), "CONTRACT#RETAINER#WORKER#STAFF");
                String responce = sendNotificationMailWithAttachement("Full Day Absent From Date " + mdate + " To " + mdate1, "Please Find Attachement...", recievers, "sdmlerp@dineshmills.com", "/Email_Attachment/FullAbsent.xls", "FullAbsent.xls");
                System.out.println("Mail Response:" + responce);

                mstaff = "SELECT @a:=@a+1 AS SR_NO,ABSDATA.* FROM (SELECT @a:=0) AS a,(SELECT EMPID as Employee,COALESCE(EMP_NAME,'') AS NAME,DATE_FORMAT(PUNCHDATE,'%d/%m/%Y') AS PunchDate,COALESCE(SHIFT,'') AS SHIFT,"
                        + "CONCAT(PRESENT_FIRST,PRESENT_SECOND) AS PRESENT,COALESCE(ALL_PUNCHES,'') AS PUNCHES,COALESCE(DPTNAME,'') AS Department,COALESCE(SECNAME,'') AS Category,COALESCE(CTGNAME,'') AS SubCategory,'First Half' AS Absent "
                        + "FROM ( SELECT * FROM SDMLATTPAY.ATT_DATA_DAILY_SUMMARY "
                        + "WHERE (EMPID LIKE 'BRD1%' OR EMPID LIKE 'BRD2%') AND PUNCHDATE<=DATE_SUB(CURDATE(),INTERVAL 3 DAY) AND PUNCHDATE>=CASE WHEN DATE_SUB(CURDATE(),INTERVAL DAY(CURDATE())-1 DAY)<DATE_SUB(CURDATE(),INTERVAL 9 DAY) THEN "
                        + "DATE_SUB(CURDATE(),INTERVAL DAY(CURDATE())-1 DAY) ELSE DATE_SUB(CURDATE(),INTERVAL 9 DAY) END "
                        + "AND YEAR(PUNCHDATE)=YYYY AND MONTH(PUNCHDATE)=MM  AND SHIFT NOT IN (9) AND PRESENT_FIRST='A' AND PRESENT_SECOND!='A') AS DAS "
                        + "LEFT JOIN ( SELECT PAY_EMP_NO,EMP_NAME,EMP_DEPARTMENT,EMP_MAIN_CATEGORY,EMP_CATEGORY FROM SDMLATTPAY.ATTPAY_EMPMST ) AS EMP ON DAS.EMPID=EMP.PAY_EMP_NO "
                        + "LEFT JOIN ( SELECT DPTID,NAME AS DPTNAME FROM SDMLATTPAY.ATT_DEPARTMENT_MASTER ) AS DPT ON DPT.DPTID=EMP.EMP_DEPARTMENT "
                        + "LEFT JOIN ( SELECT SECID,NAME AS SECNAME FROM SDMLATTPAY.ATT_MAIN_CATEGORY_MASTER ) AS SEC ON SEC.SECID=DAS.MAIN_CATEGORY "
                        + "LEFT JOIN ( SELECT CTGID,NAME AS CTGNAME FROM SDMLATTPAY.ATT_CATEGORY_MASTER ) AS CTG ON CTG.CTGID=DAS.CATEGORY "
                        + "LEFT JOIN ( SELECT *,'YES' AS SSS FROM SDMLATTPAY.ATT_SPECIAL_SANCTION WHERE APPROVED=1 AND CANCELED=0 ) AS SS ON DAS.EMPID=SS.EMP_CODE AND DAS.PUNCHDATE=SS.A_DATE "
                        // + "LEFT JOIN ( SELECT * FROM SDMLATTPAY.ATT_LEAVE_ENTRY E WHERE LVT_LEAVE_TYPE = 3 AND LVT_MENTION_TIME LIKE '1ST%') AS LV ON DAS.EMPID=LVT_PAY_EMPID AND DAS.PUNCHDATE >= LVT_FROMDATE AND DAS.PUNCHDATE <= LVT_TODATE "
                        // + "WHERE COALESCE(LVT_LEAVE_CODE,'')='' "
                        + "ORDER BY DPTID,EMPID,PUNCHDATE) AS ABSDATA "
                        + "UNION ALL "
                        + "SELECT @a:=@a+1 AS SR_NO,ABSDATA.* FROM (SELECT @a:=0) AS a,(SELECT EMPID as Employee,COALESCE(EMP_NAME,'') AS NAME,DATE_FORMAT(PUNCHDATE,'%d/%m/%Y') AS PunchDate,COALESCE(SHIFT,'') AS SHIFT,"
                        + "CONCAT(PRESENT_FIRST,PRESENT_SECOND) AS PRESENT,COALESCE(ALL_PUNCHES,'') AS PUNCHES,COALESCE(DPTNAME,'') AS Department,COALESCE(SECNAME,'') AS Category,COALESCE(CTGNAME,'') AS SubCategory,'Second Half' AS Absent "
                        + "FROM ( SELECT * FROM SDMLATTPAY.ATT_DATA_DAILY_SUMMARY "
                        + "WHERE (EMPID LIKE 'BRD1%' OR EMPID LIKE 'BRD2%') AND PUNCHDATE<=DATE_SUB(CURDATE(),INTERVAL 3 DAY) AND PUNCHDATE>=DATE_SUB(CURDATE(),INTERVAL 9 DAY) "
                        + "AND YEAR(PUNCHDATE)=YYYY AND MONTH(PUNCHDATE)=MM  AND SHIFT NOT IN (9) AND PRESENT_FIRST!='A' AND PRESENT_SECOND='A') AS DAS "
                        + "LEFT JOIN ( SELECT PAY_EMP_NO,EMP_NAME,EMP_DEPARTMENT,EMP_MAIN_CATEGORY,EMP_CATEGORY FROM SDMLATTPAY.ATTPAY_EMPMST ) AS EMP ON DAS.EMPID=EMP.PAY_EMP_NO "
                        + "LEFT JOIN ( SELECT DPTID,NAME AS DPTNAME FROM SDMLATTPAY.ATT_DEPARTMENT_MASTER ) AS DPT ON DPT.DPTID=EMP.EMP_DEPARTMENT "
                        + "LEFT JOIN ( SELECT SECID,NAME AS SECNAME FROM SDMLATTPAY.ATT_MAIN_CATEGORY_MASTER ) AS SEC ON SEC.SECID=DAS.MAIN_CATEGORY "
                        + "LEFT JOIN ( SELECT CTGID,NAME AS CTGNAME FROM SDMLATTPAY.ATT_CATEGORY_MASTER ) AS CTG ON CTG.CTGID=DAS.CATEGORY "
                        + "LEFT JOIN ( SELECT *,'YES' AS SSS FROM SDMLATTPAY.ATT_SPECIAL_SANCTION WHERE APPROVED=1 AND CANCELED=0 ) AS SS ON DAS.EMPID=SS.EMP_CODE AND DAS.PUNCHDATE=SS.A_DATE "
                        //+ "LEFT JOIN ( SELECT * FROM SDMLATTPAY.ATT_LEAVE_ENTRY E WHERE LVT_LEAVE_TYPE = 3 AND LVT_MENTION_TIME LIKE '2ND%') AS LV ON DAS.EMPID=LVT_PAY_EMPID AND DAS.PUNCHDATE >= LVT_FROMDATE AND DAS.PUNCHDATE <= LVT_TODATE "
                        //   + "WHERE COALESCE(LVT_LEAVE_CODE,'')='' "
                        + "ORDER BY DPTID,EMPID,PUNCHDATE) AS ABSDATA";
                mworker = "SELECT @a:=@a+1 AS SR_NO,ABSDATA.* FROM (SELECT @a:=0) AS a,(SELECT EMPID as Employee,COALESCE(EMP_NAME,'') AS NAME,DATE_FORMAT(PUNCHDATE,'%d/%m/%Y') AS PunchDate,COALESCE(SHIFT,'') AS SHIFT,"
                        + "CONCAT(PRESENT_FIRST,PRESENT_SECOND) AS PRESENT,COALESCE(ALL_PUNCHES,'') AS PUNCHES,COALESCE(DPTNAME,'') AS Department,COALESCE(SECNAME,'') AS Category,COALESCE(CTGNAME,'') AS SubCategory,'First Half' AS Absent "
                        + "FROM ( SELECT * FROM SDMLATTPAY.ATT_DATA_DAILY_SUMMARY "
                        + "WHERE (EMPID LIKE 'BRD3%' OR EMPID LIKE 'BRD4%') AND PUNCHDATE<=DATE_SUB(CURDATE(),INTERVAL 3 DAY) AND PUNCHDATE>=CASE WHEN DATE_SUB(CURDATE(),INTERVAL DAY(CURDATE())-1 DAY)<DATE_SUB(CURDATE(),INTERVAL 9 DAY) THEN "
                        + "DATE_SUB(CURDATE(),INTERVAL DAY(CURDATE())-1 DAY) ELSE DATE_SUB(CURDATE(),INTERVAL 9 DAY) END "
                        + "AND YEAR(PUNCHDATE)=YYYY AND MONTH(PUNCHDATE)=MM  AND SHIFT NOT IN (9) AND PRESENT_FIRST='A' AND PRESENT_SECOND!='A') AS DAS "
                        + "LEFT JOIN ( SELECT PAY_EMP_NO,EMP_NAME,EMP_DEPARTMENT,EMP_MAIN_CATEGORY,EMP_CATEGORY FROM SDMLATTPAY.ATTPAY_EMPMST ) AS EMP ON DAS.EMPID=EMP.PAY_EMP_NO "
                        + "LEFT JOIN ( SELECT DPTID,NAME AS DPTNAME FROM SDMLATTPAY.ATT_DEPARTMENT_MASTER ) AS DPT ON DPT.DPTID=EMP.EMP_DEPARTMENT "
                        + "LEFT JOIN ( SELECT SECID,NAME AS SECNAME FROM SDMLATTPAY.ATT_MAIN_CATEGORY_MASTER ) AS SEC ON SEC.SECID=DAS.MAIN_CATEGORY "
                        + "LEFT JOIN ( SELECT CTGID,NAME AS CTGNAME FROM SDMLATTPAY.ATT_CATEGORY_MASTER ) AS CTG ON CTG.CTGID=DAS.CATEGORY "
                        + "LEFT JOIN ( SELECT *,'YES' AS SSS FROM SDMLATTPAY.ATT_SPECIAL_SANCTION WHERE APPROVED=1 AND CANCELED=0 ) AS SS ON DAS.EMPID=SS.EMP_CODE AND DAS.PUNCHDATE=SS.A_DATE "
                        // + "LEFT JOIN ( SELECT * FROM SDMLATTPAY.ATT_LEAVE_ENTRY E WHERE LVT_LEAVE_TYPE = 3 AND LVT_MENTION_TIME LIKE '1ST%') AS LV ON DAS.EMPID=LVT_PAY_EMPID AND DAS.PUNCHDATE >= LVT_FROMDATE AND DAS.PUNCHDATE <= LVT_TODATE "
                        // + "WHERE COALESCE(LVT_LEAVE_CODE,'')='' "
                        + "ORDER BY DPTID,EMPID,PUNCHDATE) AS ABSDATA "
                        + "UNION ALL "
                        + "SELECT @a:=@a+1 AS SR_NO,ABSDATA.* FROM (SELECT @a:=0) AS a,(SELECT EMPID as Employee,COALESCE(EMP_NAME,'') AS NAME,DATE_FORMAT(PUNCHDATE,'%d/%m/%Y') AS PunchDate,COALESCE(SHIFT,'') AS SHIFT,"
                        + "CONCAT(PRESENT_FIRST,PRESENT_SECOND) AS PRESENT,COALESCE(ALL_PUNCHES,'') AS PUNCHES,COALESCE(DPTNAME,'') AS Department,COALESCE(SECNAME,'') AS Category,COALESCE(CTGNAME,'') AS SubCategory,'Second Half' AS Absent "
                        + "FROM ( SELECT * FROM SDMLATTPAY.ATT_DATA_DAILY_SUMMARY "
                        + "WHERE (EMPID LIKE 'BRD3%' OR EMPID LIKE 'BRD4%') AND PUNCHDATE<=DATE_SUB(CURDATE(),INTERVAL 3 DAY) AND PUNCHDATE>=DATE_SUB(CURDATE(),INTERVAL 9 DAY) "
                        + "AND YEAR(PUNCHDATE)=YYYY AND MONTH(PUNCHDATE)=MM  AND SHIFT NOT IN (9) AND PRESENT_FIRST!='A' AND PRESENT_SECOND='A') AS DAS "
                        + "LEFT JOIN ( SELECT PAY_EMP_NO,EMP_NAME,EMP_DEPARTMENT,EMP_MAIN_CATEGORY,EMP_CATEGORY FROM SDMLATTPAY.ATTPAY_EMPMST ) AS EMP ON DAS.EMPID=EMP.PAY_EMP_NO "
                        + "LEFT JOIN ( SELECT DPTID,NAME AS DPTNAME FROM SDMLATTPAY.ATT_DEPARTMENT_MASTER ) AS DPT ON DPT.DPTID=EMP.EMP_DEPARTMENT "
                        + "LEFT JOIN ( SELECT SECID,NAME AS SECNAME FROM SDMLATTPAY.ATT_MAIN_CATEGORY_MASTER ) AS SEC ON SEC.SECID=DAS.MAIN_CATEGORY "
                        + "LEFT JOIN ( SELECT CTGID,NAME AS CTGNAME FROM SDMLATTPAY.ATT_CATEGORY_MASTER ) AS CTG ON CTG.CTGID=DAS.CATEGORY "
                        + "LEFT JOIN ( SELECT *,'YES' AS SSS FROM SDMLATTPAY.ATT_SPECIAL_SANCTION WHERE APPROVED=1 AND CANCELED=0 ) AS SS ON DAS.EMPID=SS.EMP_CODE AND DAS.PUNCHDATE=SS.A_DATE "
                        //+ "LEFT JOIN ( SELECT * FROM SDMLATTPAY.ATT_LEAVE_ENTRY E WHERE LVT_LEAVE_TYPE = 3 AND LVT_MENTION_TIME LIKE '2ND%') AS LV ON DAS.EMPID=LVT_PAY_EMPID AND DAS.PUNCHDATE >= LVT_FROMDATE AND DAS.PUNCHDATE <= LVT_TODATE "
                        //   + "WHERE COALESCE(LVT_LEAVE_CODE,'')='' "
                        + "ORDER BY DPTID,EMPID,PUNCHDATE) AS ABSDATA";
                mretainer = "SELECT @a:=@a+1 AS SR_NO,ABSDATA.* FROM (SELECT @a:=0) AS a,(SELECT EMPID as Employee,COALESCE(EMP_NAME,'') AS NAME,DATE_FORMAT(PUNCHDATE,'%d/%m/%Y') AS PunchDate,COALESCE(SHIFT,'') AS SHIFT,"
                        + "CONCAT(PRESENT_FIRST,PRESENT_SECOND) AS PRESENT,COALESCE(ALL_PUNCHES,'') AS PUNCHES,COALESCE(DPTNAME,'') AS Department,COALESCE(SECNAME,'') AS Category,COALESCE(CTGNAME,'') AS SubCategory,'First Half' AS Absent "
                        + "FROM ( SELECT * FROM SDMLATTPAY.ATT_DATA_DAILY_SUMMARY "
                        + "WHERE (EMPID LIKE 'BRD5%' OR EMPID LIKE 'BRD6%') AND PUNCHDATE<=DATE_SUB(CURDATE(),INTERVAL 3 DAY) AND PUNCHDATE>=CASE WHEN DATE_SUB(CURDATE(),INTERVAL DAY(CURDATE())-1 DAY)<DATE_SUB(CURDATE(),INTERVAL 9 DAY) THEN "
                        + "DATE_SUB(CURDATE(),INTERVAL DAY(CURDATE())-1 DAY) ELSE DATE_SUB(CURDATE(),INTERVAL 9 DAY) END "
                        + "AND YEAR(PUNCHDATE)=YYYY AND MONTH(PUNCHDATE)=MM  AND SHIFT NOT IN (9) AND PRESENT_FIRST='A' AND PRESENT_SECOND!='A') AS DAS "
                        + "LEFT JOIN ( SELECT PAY_EMP_NO,EMP_NAME,EMP_DEPARTMENT,EMP_MAIN_CATEGORY,EMP_CATEGORY FROM SDMLATTPAY.ATTPAY_EMPMST ) AS EMP ON DAS.EMPID=EMP.PAY_EMP_NO "
                        + "LEFT JOIN ( SELECT DPTID,NAME AS DPTNAME FROM SDMLATTPAY.ATT_DEPARTMENT_MASTER ) AS DPT ON DPT.DPTID=EMP.EMP_DEPARTMENT "
                        + "LEFT JOIN ( SELECT SECID,NAME AS SECNAME FROM SDMLATTPAY.ATT_MAIN_CATEGORY_MASTER ) AS SEC ON SEC.SECID=DAS.MAIN_CATEGORY "
                        + "LEFT JOIN ( SELECT CTGID,NAME AS CTGNAME FROM SDMLATTPAY.ATT_CATEGORY_MASTER ) AS CTG ON CTG.CTGID=DAS.CATEGORY "
                        + "LEFT JOIN ( SELECT *,'YES' AS SSS FROM SDMLATTPAY.ATT_SPECIAL_SANCTION WHERE APPROVED=1 AND CANCELED=0 ) AS SS ON DAS.EMPID=SS.EMP_CODE AND DAS.PUNCHDATE=SS.A_DATE "
                        // + "LEFT JOIN ( SELECT * FROM SDMLATTPAY.ATT_LEAVE_ENTRY E WHERE LVT_LEAVE_TYPE = 3 AND LVT_MENTION_TIME LIKE '1ST%') AS LV ON DAS.EMPID=LVT_PAY_EMPID AND DAS.PUNCHDATE >= LVT_FROMDATE AND DAS.PUNCHDATE <= LVT_TODATE "
                        // + "WHERE COALESCE(LVT_LEAVE_CODE,'')='' "
                        + "ORDER BY DPTID,EMPID,PUNCHDATE) AS ABSDATA "
                        + "UNION ALL "
                        + "SELECT @a:=@a+1 AS SR_NO,ABSDATA.* FROM (SELECT @a:=0) AS a,(SELECT EMPID as Employee,COALESCE(EMP_NAME,'') AS NAME,DATE_FORMAT(PUNCHDATE,'%d/%m/%Y') AS PunchDate,COALESCE(SHIFT,'') AS SHIFT,"
                        + "CONCAT(PRESENT_FIRST,PRESENT_SECOND) AS PRESENT,COALESCE(ALL_PUNCHES,'') AS PUNCHES,COALESCE(DPTNAME,'') AS Department,COALESCE(SECNAME,'') AS Category,COALESCE(CTGNAME,'') AS SubCategory,'Second Half' AS Absent "
                        + "FROM ( SELECT * FROM SDMLATTPAY.ATT_DATA_DAILY_SUMMARY "
                        + "WHERE (EMPID LIKE 'BRD5%' OR EMPID LIKE 'BRD6%') AND PUNCHDATE<=DATE_SUB(CURDATE(),INTERVAL 3 DAY) AND PUNCHDATE>=DATE_SUB(CURDATE(),INTERVAL 9 DAY) "
                        + "AND YEAR(PUNCHDATE)=YYYY AND MONTH(PUNCHDATE)=MM  AND SHIFT NOT IN (9) AND PRESENT_FIRST!='A' AND PRESENT_SECOND='A') AS DAS "
                        + "LEFT JOIN ( SELECT PAY_EMP_NO,EMP_NAME,EMP_DEPARTMENT,EMP_MAIN_CATEGORY,EMP_CATEGORY FROM SDMLATTPAY.ATTPAY_EMPMST ) AS EMP ON DAS.EMPID=EMP.PAY_EMP_NO "
                        + "LEFT JOIN ( SELECT DPTID,NAME AS DPTNAME FROM SDMLATTPAY.ATT_DEPARTMENT_MASTER ) AS DPT ON DPT.DPTID=EMP.EMP_DEPARTMENT "
                        + "LEFT JOIN ( SELECT SECID,NAME AS SECNAME FROM SDMLATTPAY.ATT_MAIN_CATEGORY_MASTER ) AS SEC ON SEC.SECID=DAS.MAIN_CATEGORY "
                        + "LEFT JOIN ( SELECT CTGID,NAME AS CTGNAME FROM SDMLATTPAY.ATT_CATEGORY_MASTER ) AS CTG ON CTG.CTGID=DAS.CATEGORY "
                        + "LEFT JOIN ( SELECT *,'YES' AS SSS FROM SDMLATTPAY.ATT_SPECIAL_SANCTION WHERE APPROVED=1 AND CANCELED=0 ) AS SS ON DAS.EMPID=SS.EMP_CODE AND DAS.PUNCHDATE=SS.A_DATE "
                        //+ "LEFT JOIN ( SELECT * FROM SDMLATTPAY.ATT_LEAVE_ENTRY E WHERE LVT_LEAVE_TYPE = 3 AND LVT_MENTION_TIME LIKE '2ND%') AS LV ON DAS.EMPID=LVT_PAY_EMPID AND DAS.PUNCHDATE >= LVT_FROMDATE AND DAS.PUNCHDATE <= LVT_TODATE "
                        //   + "WHERE COALESCE(LVT_LEAVE_CODE,'')='' "
                        + "ORDER BY DPTID,EMPID,PUNCHDATE) AS ABSDATA";
                mcontract = "SELECT @a:=@a+1 AS SR_NO,ABSDATA.* FROM (SELECT @a:=0) AS a,(SELECT EMPID as Employee,COALESCE(EMP_NAME,'') AS NAME,DATE_FORMAT(PUNCHDATE,'%d/%m/%Y') AS PunchDate,COALESCE(SHIFT,'') AS SHIFT,"
                        + "CONCAT(PRESENT_FIRST,PRESENT_SECOND) AS PRESENT,COALESCE(ALL_PUNCHES,'') AS PUNCHES,COALESCE(DPTNAME,'') AS Department,COALESCE(SECNAME,'') AS Category,COALESCE(CTGNAME,'') AS SubCategory,'First Half' AS Absent "
                        + "FROM ( SELECT * FROM SDMLATTPAY.ATT_DATA_DAILY_SUMMARY "
                        + "WHERE (EMPID LIKE 'BRD0%') AND PUNCHDATE<=DATE_SUB(CURDATE(),INTERVAL 3 DAY) AND PUNCHDATE>=CASE WHEN DATE_SUB(CURDATE(),INTERVAL DAY(CURDATE())-1 DAY)<DATE_SUB(CURDATE(),INTERVAL 9 DAY) THEN "
                        + "DATE_SUB(CURDATE(),INTERVAL DAY(CURDATE())-1 DAY) ELSE DATE_SUB(CURDATE(),INTERVAL 9 DAY) END "
                        + "AND YEAR(PUNCHDATE)=YYYY AND MONTH(PUNCHDATE)=MM  AND SHIFT NOT IN (9) AND PRESENT_FIRST='A' AND PRESENT_SECOND!='A') AS DAS "
                        + "LEFT JOIN ( SELECT PAY_EMP_NO,EMP_NAME,EMP_DEPARTMENT,EMP_MAIN_CATEGORY,EMP_CATEGORY FROM SDMLATTPAY.ATTPAY_EMPMST ) AS EMP ON DAS.EMPID=EMP.PAY_EMP_NO "
                        + "LEFT JOIN ( SELECT DPTID,NAME AS DPTNAME FROM SDMLATTPAY.ATT_DEPARTMENT_MASTER ) AS DPT ON DPT.DPTID=EMP.EMP_DEPARTMENT "
                        + "LEFT JOIN ( SELECT SECID,NAME AS SECNAME FROM SDMLATTPAY.ATT_MAIN_CATEGORY_MASTER ) AS SEC ON SEC.SECID=DAS.MAIN_CATEGORY "
                        + "LEFT JOIN ( SELECT CTGID,NAME AS CTGNAME FROM SDMLATTPAY.ATT_CATEGORY_MASTER ) AS CTG ON CTG.CTGID=DAS.CATEGORY "
                        + "LEFT JOIN ( SELECT *,'YES' AS SSS FROM SDMLATTPAY.ATT_SPECIAL_SANCTION WHERE APPROVED=1 AND CANCELED=0 ) AS SS ON DAS.EMPID=SS.EMP_CODE AND DAS.PUNCHDATE=SS.A_DATE "
                        // + "LEFT JOIN ( SELECT * FROM SDMLATTPAY.ATT_LEAVE_ENTRY E WHERE LVT_LEAVE_TYPE = 3 AND LVT_MENTION_TIME LIKE '1ST%') AS LV ON DAS.EMPID=LVT_PAY_EMPID AND DAS.PUNCHDATE >= LVT_FROMDATE AND DAS.PUNCHDATE <= LVT_TODATE "
                        // + "WHERE COALESCE(LVT_LEAVE_CODE,'')='' "
                        + "ORDER BY DPTID,EMPID,PUNCHDATE) AS ABSDATA "
                        + "UNION ALL "
                        + "SELECT @a:=@a+1 AS SR_NO,ABSDATA.* FROM (SELECT @a:=0) AS a,(SELECT EMPID as Employee,COALESCE(EMP_NAME,'') AS NAME,DATE_FORMAT(PUNCHDATE,'%d/%m/%Y') AS PunchDate,COALESCE(SHIFT,'') AS SHIFT,"
                        + "CONCAT(PRESENT_FIRST,PRESENT_SECOND) AS PRESENT,COALESCE(ALL_PUNCHES,'') AS PUNCHES,COALESCE(DPTNAME,'') AS Department,COALESCE(SECNAME,'') AS Category,COALESCE(CTGNAME,'') AS SubCategory,'Second Half' AS Absent "
                        + "FROM ( SELECT * FROM SDMLATTPAY.ATT_DATA_DAILY_SUMMARY "
                        + "WHERE (EMPID LIKE 'BRD0%') AND PUNCHDATE<=DATE_SUB(CURDATE(),INTERVAL 3 DAY) AND PUNCHDATE>=DATE_SUB(CURDATE(),INTERVAL 9 DAY) "
                        + "AND YEAR(PUNCHDATE)=YYYY AND MONTH(PUNCHDATE)=MM  AND SHIFT NOT IN (9) AND PRESENT_FIRST!='A' AND PRESENT_SECOND='A') AS DAS "
                        + "LEFT JOIN ( SELECT PAY_EMP_NO,EMP_NAME,EMP_DEPARTMENT,EMP_MAIN_CATEGORY,EMP_CATEGORY FROM SDMLATTPAY.ATTPAY_EMPMST ) AS EMP ON DAS.EMPID=EMP.PAY_EMP_NO "
                        + "LEFT JOIN ( SELECT DPTID,NAME AS DPTNAME FROM SDMLATTPAY.ATT_DEPARTMENT_MASTER ) AS DPT ON DPT.DPTID=EMP.EMP_DEPARTMENT "
                        + "LEFT JOIN ( SELECT SECID,NAME AS SECNAME FROM SDMLATTPAY.ATT_MAIN_CATEGORY_MASTER ) AS SEC ON SEC.SECID=DAS.MAIN_CATEGORY "
                        + "LEFT JOIN ( SELECT CTGID,NAME AS CTGNAME FROM SDMLATTPAY.ATT_CATEGORY_MASTER ) AS CTG ON CTG.CTGID=DAS.CATEGORY "
                        + "LEFT JOIN ( SELECT *,'YES' AS SSS FROM SDMLATTPAY.ATT_SPECIAL_SANCTION WHERE APPROVED=1 AND CANCELED=0 ) AS SS ON DAS.EMPID=SS.EMP_CODE AND DAS.PUNCHDATE=SS.A_DATE "
                        //+ "LEFT JOIN ( SELECT * FROM SDMLATTPAY.ATT_LEAVE_ENTRY E WHERE LVT_LEAVE_TYPE = 3 AND LVT_MENTION_TIME LIKE '2ND%') AS LV ON DAS.EMPID=LVT_PAY_EMPID AND DAS.PUNCHDATE >= LVT_FROMDATE AND DAS.PUNCHDATE <= LVT_TODATE "
                        //   + "WHERE COALESCE(LVT_LEAVE_CODE,'')='' "
                        + "ORDER BY DPTID,EMPID,PUNCHDATE) AS ABSDATA";

                recievers = "rishineekhra@dineshmills.com";
//            pBody = "Half Day Absent List";
//            disdata = data.getResult(mstaff);
//            disdata.first();
//
//            if (disdata.getRow() > 0) {
//                srNo = 0;
//                pBody += "<br>";
//                pBody += "<table border=1>";
//                pBody += "<tr><th align='center'><b>Sr.No.</b></th>"
//                        + "<th align='center'><b>Emp Code</b></th>"
//                        + "<th align='center'><b>Name</b></th>"
//                        + "<th align='center'><b>Date</b></th>"
//                        + "<th align='center'><b>SHIFT</b></th>"
//                        + "<th align='center'><b>Punches</b></th>"
//                        + "<th align='center'><b>Department</b></th>"
//                        + "<th align='center'><b>Category</b></th>"
//                        + "<th align='center'><b>Sub Category</b></th>"
//                        + "<th align='center'><b>Absent</b></th>"
//                        + "</tr>";
//                while (!disdata.isAfterLast()) {
//                    srNo++;
//                    pBody += "<tr>";
//                    pBody += "<td>" + srNo + "</td>";
//                    pBody += "<td>" + disdata.getString("Employee") + "</td>";
//                    pBody += "<td>" + disdata.getString("NAME") + "</td>";
//                    pBody += "<td>" + disdata.getString("PunchDate") + "</td>";
//                    pBody += "<td>" + disdata.getString("SHIFT") + "</td>";
//                    pBody += "<td>" + disdata.getString("PUNCHES") + "</td>";
//                    pBody += "<td>" + disdata.getString("Department") + "</td>";
//                    pBody += "<td>" + disdata.getString("Category") + "</td>";
//                    pBody += "<td>" + disdata.getString("SubCategory") + "</td>";
//                    pBody += "<td>" + disdata.getString("Absent") + "</td>";
//                    pBody += "</tr>";
//                    disdata.next();
//                }
//                pBody += "</table>";
//                pBody += "<br><br>";
//                pBody += "<br>";
//            }
                exprt.fillData(mcontract + "#" + mretainer + "#" + mworker + "#" + mstaff, new File("/Email_Attachment/HalfAbsent.xls"), "CONTRACT#RETAINER#WORKER#STAFF");
                exprt.fillData(mcontract + "#" + mretainer + "#" + mworker + "#" + mstaff, new File("E:/HalfAbsent.xls"), "CONTRACT#RETAINER#WORKER#STAFF");
                responce = sendNotificationMailWithAttachement("Half Day Absent From Date " + mdate + " To " + mdate1, "Please Find Attachement...", recievers, "sdmlerp@dineshmills.com", "/Email_Attachment/HalfAbsent.xls", "HalfAbsent.xls");
                System.out.println("Mail Response:" + responce);

            } catch (Exception e) {

            }
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
