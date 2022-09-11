/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.AutoPI;

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
public class AutoPITickedNotification {

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

            rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_AUTO_PI_DATA WHERE FINAL_APPROVAL_DATE=CURDATE() AND PI_SELECTED_FLAG=1 AND PI_GENERATED_FLAG=1 AND PI_MAILED_FLAG=0 ORDER BY PARTY_CODE,PIECE_NO ");
            rsData1.first();

            if (rsData1.getRow() > 0) {
                
                while (!rsData1.isAfterLast()) {

                    String proformaNo = data.getStringValueFromDB("SELECT H.PROFORMA_NO FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER H, PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL D "
                            + "WHERE H.PROFORMA_NO=D.PROFORMA_NO AND H.APPROVED=1 AND H.CANCELED=0 "
                            + "AND H.PARTY_CD='" + rsData1.getString("PARTY_CODE") + "' AND D.PIECE_NO='" + rsData1.getString("PIECE_NO") + "' ORDER BY H.PROFORMA_NO DESC");

                    String proformaDt = data.getStringValueFromDB("SELECT H.PROFORMA_DATE FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER H, PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL D "
                            + "WHERE H.PROFORMA_NO=D.PROFORMA_NO AND H.APPROVED=1 AND H.CANCELED=0 "
                            + "AND H.PARTY_CD='" + rsData1.getString("PARTY_CODE") + "' AND D.PIECE_NO='" + rsData1.getString("PIECE_NO") + "' ORDER BY H.PROFORMA_NO DESC");

                    String fileName = proformaNo + "_" + proformaDt + "";

                    Connection Conn = data.getConn();
                    Statement st = Conn.createStatement();

                    HashMap parameterMap = new HashMap();

                    parameterMap.put("PROFORMA_DATE", EITLERPGLOBAL.formatDate(proformaDt));
                    parameterMap.put("PROFORMA_NO", proformaNo);
                    
//                    PDFAutoPI rpt = new PDFAutoPI(parameterMap, Conn);

                    sql = "SELECT A.PROFORMA_NO,A.PROFORMA_DATE,A.NAME,A.STATION,A.CONTACT,"
                            + "A.PHONE,B.SR_NO,B.PIECE_NO,B.POSITION,B.MACHINE_NO,B.ITEM_DESC,"
                            + "B.LNGTH,B.WIDTH,B.GSQ,B.STYLE,B.RATE,B.BAS_AMT,B.AOSD_PER,B.AOSD_AMT,B.DISC_PER,B.DISAMT,"
                            + "B.DISBASAMT,B.EXCISE,B.SEAM_CHG,B.INV_AMT,B.VAT,B.CST,B.SD_AMT,"
                            + "B.TCS_PER,B.TCS_AMT,"
                            + "B.INV_VAL,A.REMARK1,A.REMARK2,A.REMARK3,A.REMARK4,A.REMARK5,B.INV_VAL_WORD,"
                            + "B.IGST_AMT,B.CGST_AMT,B.SGST_AMT,B.INSACC_AMT,B.WEIGHT,B.SQMTR,C.GSTIN_NO,D.PR_SYN_PER,"
                            + "B.UOM,B.SURCHARGE_PER,B.SURCHARGE_RATE,B.GROSS_RATE,B.PO_NO,B.PO_DATE FROM "
                            + "PRODUCTION.FELT_PROFORMA_INVOICE_HEADER AS A,"
                            + "PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL AS B,"
                            + "PRODUCTION.FELT_SALES_PIECE_REGISTER AS D,"
                            + "DINESHMILLS.D_SAL_PARTY_MASTER AS C  "
                            + "WHERE A.PROFORMA_NO=B.PROFORMA_NO "
                            + "AND A.PARTY_CD=C.PARTY_CODE "
                            + "AND B.PIECE_NO=D.PR_PIECE_NO "
                            + "AND A.PROFORMA_NO='" + proformaNo + "' AND A.PROFORMA_DATE='" + proformaDt + "'";
//                    rpt.setReportName("/EITLERP/FeltSales/FeltFinishing/pdfProforma.jrxml", 1, sql, fileName); //productlist is the name of my jasper file.
//                    rpt.callReport();
                    
                    generateReport(parameterMap, Conn, "/EITLERP/FeltSales/FeltFinishing/pdfProforma.jrxml", 1, sql, fileName);
                    System.out.println("PDF Generated");

                    tmpr = data.getResult(sql);

                    String mbody;
                    mbody = "<html><body> Dear Sir,<br>"
                            + "<br><p>We are pleased to inform you that your following felt has been made ready on priority. Our proforma invoice(PI) for the same is now attached herewith to enable you to arrange the payment immediately enabling despatches.</p><br>"
                            + "<table border='1'>"
                            + "<tr><th>PC NO</th><th align='center'> M/C NO </th>"
                            + "<th align='center'> POSITION </th><th align='center'>LENGTH </th>"
                            + "<th align='center'>WIDTH </th>"
                            + "<th align='center'>GSM </th><th align='center'>VALUE(RS)</th></tr>"
                            + "<tr>"
                            + "<td>"
                            + "<p>" + tmpr.getString("PIECE_NO") + "</p>"
                            + "</td>"
                            + "<td>"
                            + "<p>" + tmpr.getString("MACHINE_NO") + "</p>"
                            + "</td>"
                            + "<td>"
                            + "<p>" + tmpr.getString("POSITION") + "</p>"
                            + "</td>"
                            + "<td>"
                            + "<p>" + tmpr.getString("LNGTH") + "</p>"
                            + "</td>"
                            + "<td>"
                            + "<p>" + tmpr.getString("WIDTH") + "</p>"
                            + "</td>"
                            + "<td>"
                            + "<p>" + tmpr.getString("GSQ") + "</p>"
                            + "</td>"
                            + "<td>"
                            + "<p>" + tmpr.getString("INV_VAL") + "</p>"
                            + "</td>"
                            + "</table><p>Given here under are our bank details for your reference & doing the needful</p><table border=1><tr><td><b> (1) </b></td><td><b>ACCOUNT NAME : <font color='red'>SHRI DINESH MILLS LTD</font></b></td></tr><tr><td><b>(2)</b></td><td><b> COMPANY ADDRESS : <font color='red'>PADRA ROAD , VADODARA - 390 020 (GUJARAT )</font></b></td></tr><tr><td><b>(3)</b></td><td><b>ACCOUNT NUMBER : <font color='red'>0033-033-0000-810</font></b></td></tr><tr><td><b>(4)</b></td><td><b>TYPE OF ACCOUNT : <font color='red'>CURRENT A/C</font></b></td></tr><tr><td><b>(5)</b></td><td><b> BANK NAME : <font color='red'>HDFC BANK</font></b></td></tr><tr><td><b>(6)</b></td><td><b> BRANCH ADDRESS : <font color='red'>GROUND FLOOR, KANHA CAPITAL, R C DUTT ROAD, ALKAPURI, VADODARA - 390 007</font></b></td></tr><tr><td><b>(7)</b></td><td><b>IFSC/RTGS CODE OF BANK BRANCH : <font color='red'>HDFC0000033</font></b></td></tr><tr><td><b>(8)</b></td><td><b>MICR CODE : <font color='red'>390240002</font></b></td></tr><tr><td><b>(9)</b></td><td><b>BRANCH CODE: <font color='red'>33</font></b></td></tr></table><p>Thanks & Regards</p><img border='0' src=\'http://200.0.0.227:8080/SDMLERP/EITLERP/Images/FeltImage\' alt='' width='74' height='85'  ></img><p><font size='6' color='blue'><b>Felt sales Division</b></font></p>Office: +91 265 2960060/61/62/63/66|<br>Email: <a href='mailto:felts@dineshmills.com'>felts@dineshmills.com</a> | Website: <a href='http://www.dineshmills.com'>www.dineshmills.com</a><br>P O Box-2501, Padra Road, Vadodara 390 020, Gujarat, India<br><p>The information and attachment(s) contained by this e-mail are confidential, proprietary and legally privileged data of Shri Dinesh Mills Limited that is intended for use only by the addressee. If you are not the intended recipient, you are notified that any dissemination, distribution, or copying of this e-mail is strictly prohibited and requested to delete this e-mail immediately and notify the originator. While this e-mail has been checked for all known viruses, the addressee should also scan for viruses. Internet communications cannot be guaranteed to be timely, secure, error or virus-free as information could be intercepted, corrupted, lost, destroyed, arrive late or incomplete. SHRI DINESH MILLS LTD does not accept liability for any errors or omissions</p><p><img border='0' src=\'http://200.0.0.227:8080/SDMLERP/EITLERP/Images/savetree.jpg\' alt='' width='30' height='25'></img><b>Please consider your environmental responsibility.Do not print this e-mail unless you really need to.</b></p></body></html>";

                    toemail = "felts@dineshmills.com";
//                    sendTo = "gaurang@dineshmills.com,rishineekhra@dineshmills.com";
                    sendTo = data.getStringValueFromDB("SELECT COALESCE(EMAIL,'') FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + rsData1.getString("PARTY_CODE") + "'");

                    try {
                        JavaMail.SendMailwithAttachment_Felts(sendTo, mbody, "Proforma Invoice for " + tmpr.getString("NAME"), toemail, "/Email_Attachment/" + fileName + ".pdf", fileName + ".pdf");
                    } catch (Exception s) {

                    }
                   
                    sql = "UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_ACT_PI_DATE=CURDATE() WHERE PR_PARTY_CODE='" + rsData1.getString("PARTY_CODE") + "' AND PR_PIECE_NO='" + rsData1.getString("PIECE_NO") + "'";
                    data.Execute(sql);

                    sql = "UPDATE PRODUCTION.FELT_AUTO_PI_DATA SET PI_MAILED_FLAG='1' WHERE PARTY_CODE='" + rsData1.getString("PARTY_CODE") + "' AND PIECE_NO='" + rsData1.getString("PIECE_NO") + "'";
                    data.Execute(sql);

                    rsData1.next();
                }
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
                stream = AutoPITickedNotification.class.getClass().getResourceAsStream(reportName);
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
