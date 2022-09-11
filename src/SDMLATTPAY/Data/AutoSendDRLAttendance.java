/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SDMLATTPAY.Data;

import EITLERP.FeltSales.AutoPI.*;
import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.common.JavaMail;
import EITLERP.data;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.*;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 *
 * @author root
 */
public class AutoSendDRLAttendance {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Ticked_Auto_PI_Data();        
    }

    private static void Ticked_Auto_PI_Data() {
        String sql, toemail, sendTo;
        ResultSet tmpr;
        try {
            Connection Conn1;
            Statement stmt1;
            ResultSet rsData1;
            Conn1 = data.getConn();
            stmt1 = Conn1.createStatement();

            String strMonth = data.getStringValueFromDB("SELECT CASE WHEN DAY(CURDATE())=1 THEN DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 MONTH), '%b') ELSE \n"
                    + " DATE_FORMAT(CURDATE(), '%b') END FROM DUAL");
            String month = data.getStringValueFromDB("SELECT CASE WHEN DAY(CURDATE())=1 THEN MONTH(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) ELSE \n"
                    + " MONTH(CURDATE()) END");
            String year = data.getStringValueFromDB("SELECT CASE WHEN DAY(CURDATE())=1 THEN YEAR(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) ELSE \n"
                    + " YEAR(CURDATE()) END");

            String fileName = "DRL_" + strMonth + "_" + year + "";

            Connection Conn = data.getConn();
            Statement st = Conn.createStatement();

            HashMap parameterMap = new HashMap();

            parameterMap.put("strMnth", strMonth);
            parameterMap.put("mnth", month);
            parameterMap.put("yr", year);

//                    PDFAutoPI rpt = new PDFAutoPI(parameterMap, Conn);
            /*sql = "SELECT DISTINCT EMPID, SHIFT, PUNCHDATE, PUNCHES_NOS, ALL_PUNCHES, "
                    + " PRESENT_FIRST, PRESENT_SECOND FROM SDMLATTPAY.ATT_DATA_DAILY_SUMMARY "
                    + " WHERE SUBSTRING(EMPID,1,5)=\"BRD97\" AND YYYY="+year+" AND MM="+month+" ORDER BY EMPID";
            */
            /*sql="SELECT DISTINCT EMPID, SHIFT, PUNCHDATE, PUNCHES_NOS, ALL_PUNCHES, "
                    + " PRESENT_FIRST, PRESENT_SECOND FROM SDMLATTPAY.ATT_DATA_DAILY_SUMMARY "
                    + " WHERE SUBSTRING(EMPID,1,5)=\"BRD97\" AND YYYY="+year+" AND MM="+month+" "
                    + "UNION ALL "
                    + " SELECT EMP_CODE AS EMPID,'' SHIFT,A_DATE AS PUNCHDATE,COUNT(DISTINCT P_TIME) AS PUNCHES_NOS,"
                    + " GROUP_CONCAT(DISTINCT P_TIME ORDER BY P_TIME ASC SEPARATOR ' || ') AS ALL_PUNCHES, "
                    + " '' PRESENT_FIRST,'' PRESENT_SECOND FROM SDMLATTPAY.ATT_DATA WHERE YEAR(A_DATE)="+year+" "
                    + " AND MONTH(A_DATE)="+month+" AND EMP_CODE IN ('BRD910272') "
                    + " GROUP BY EMP_CODE,A_DATE "
                    + " ORDER BY EMPID";
            */
            sql= " SELECT EMP_CODE AS EMPID,'' SHIFT,A_DATE AS PUNCHDATE,COUNT(DISTINCT P_TIME) AS PUNCHES_NOS,"
                    + " GROUP_CONCAT(DISTINCT P_TIME ORDER BY P_TIME ASC SEPARATOR ' || ') AS ALL_PUNCHES, "
                    + " '' PRESENT_FIRST,'' PRESENT_SECOND FROM SDMLATTPAY.ATT_DATA WHERE YEAR(A_DATE)="+year+" "
                    + " AND MONTH(A_DATE)="+month+" AND EMP_CODE IN ('BRD910272') "
                    + " GROUP BY EMP_CODE,A_DATE "
                    + " ORDER BY EMPID";
            
//                    rpt.setReportName("/EITLERP/FeltSales/FeltFinishing/pdfProforma.jrxml", 1, sql, fileName); //productlist is the name of my jasper file.
//                    rpt.callReport();

            generateReport(parameterMap, Conn, "/SDMLATTPAY/Data/DRL_AT_BARODA_ATTENDANCE_REPORT.jrxml", 1, sql, fileName);
            System.out.println("PDF Generated");

            tmpr = data.getResult(sql);

            String mbody;
            mbody = "<html><body> Dear Sir/Madam,<br>"
                    + "<br><p>Please find attendance record </p><br>"
                    + "</body></html>";

            toemail = "rishineekhra@dineshmills.com,ashutosh@dineshmills.com";
            //sendTo = "ashutosh@dineshmills.com";
            sendTo = "patomddrl@dineshremedies.com,edp_drl@dineshremedies.com";

            try {
                JavaMail.SendMailwithAttachment_Felts(sendTo, mbody, "DRL Attendance data at SDML "+strMonth+"-"+year, toemail, "/Email_Attachment/" + fileName + ".pdf", fileName + ".pdf");
            } catch (Exception s) {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void generateReport(HashMap hm, Connection con, String reportName, int rcopy, String msql, String pdffile) {//JasperPrint
        InputStream stream = null;
        try {            
            JasperPrint jasperPrint = null;
            if (hm == null) {
                hm = new HashMap();
            }
            try {
                JasperDesign jd;
                stream = AutoSendDRLAttendance.class.getClass().getResourceAsStream(reportName);
                jd = JRXmlLoader.load(stream);
                JRDesignQuery newquery = new JRDesignQuery();
                newquery.setText(msql);

                jd.setQuery(newquery);
                JasperReport jr = JasperCompileManager.compileReport(jd);
                jasperPrint = JasperFillManager.fillReport(jr, hm, con); // for parameter passing to report
                String printFileName = "D://"+pdffile+".pdf";
                try{
                JasperExportManager.exportReportToPdfFile(jasperPrint, printFileName);
                }catch(Exception d){
                    printFileName="/Email_Attachment/"+pdffile+".pdf";
                    JasperExportManager.exportReportToPdfFile(jasperPrint, printFileName);
                }

            } catch (JRException e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
