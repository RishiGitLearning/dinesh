/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales;

import EITLERP.*;

import java.sql.Connection;
import java.util.HashMap;
import EITLERP.FeltSales.common.JavaMail;
import java.io.File;
import java.io.InputStream;
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
public class clsWarpingPreparationPlanningMail {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        SendMail_PreparationPlanning();
        SendMail_WarpingStatus();
        SendMail_WarpingPreparationACNE();
        SendMail_WarpingPreparationMNE();
        SendMail_MachneWindingProduction();
        SendMail_BeamGaitingReport();
    }
    public static void SendMail_PreparationPlanning()
    {
        try {
           
            String Doc_No ="";
            Doc_No = data.getStringValueFromDB("SELECT LPP_DOC_NO FROM PRODUCTION.LOOMWISE_BEAM_FALL_WARP_PREPARATION_PLANNING_HEADER WHERE APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"'");
            
            if(!Doc_No.equals(""))
            {
                String pMessage = "<html>  ";

                    pMessage = pMessage + "Dear Sir,<br><br> Loom wise beam fall - warping preparation planning entry for (" + data.getStringValueFromDB("SELECT DAYNAME('"+EITLERPGLOBAL.getCurrentDateDB()+"')") + ", '"+EITLERPGLOBAL.getCurrentDateDB()+"').<br><br>Please find attachment. ";
       
                    //String recievers = "daxesh@dineshmills.com";
                    String recievers = "brdfltweave1@dineshmills.com";
                    
                    HashMap parameterMap = new HashMap();

                    parameterMap.put("LPP_DOC_NO", Doc_No);
             
                    String sql = "SELECT H.AS_ON_DATE,D.* FROM PRODUCTION.LOOMWISE_BEAM_FALL_WARP_PREPARATION_PLANNING_HEADER H,\n" +
                                "PRODUCTION.LOOMWISE_BEAM_FALL_WARP_PREPARATION_PLANNING_DETAIL D WHERE H.LPP_DOC_NO=D.LPP_DOC_NO\n" +
                                "AND D.LPP_DOC_NO='"+Doc_No+"' order by PRODUCT_GROUP";
                    
                    String fileName = "LOOMWISE_BEAM_FALL_WARPING_PLANNING";
                   
                    Connection Conn = data.getConn();
                    generateReport(parameterMap, Conn, "/EITLERP/FeltSales/ProductionReportMail/PreparationPlanning.jrxml", 1, sql, fileName);
                    System.out.println("PDF Generated");
 
                    String pSubject = "Loom wise beam fall - warping preparation planning";
                   
                    //String pcc = "rishineekhra@dineshmills.com";
                    String pcc = "aditya@dineshmills.com,abtewary@dineshmills.com,yrpatel@dineshmills.com,amitkanti@dineshmills.com,raghavendra@dineshmills.com,sdmlerp@dineshmills.com";
                    
                    String Path = "/Email_Attachment/LOOMWISE_BEAM_FALL_WARPING_PLANNING.pdf";
                    String PFiles = "LOOMWISE_BEAM_FALL_WARPING_PLANNING.pdf";

                    JavaMail.SendMailwithAttachment(recievers, pMessage, pSubject, pcc, Path, PFiles);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       
    }
    
    public static void SendMail_WarpingStatus()
    {
        try {
            
            String Doc_No ="";
            Doc_No = data.getStringValueFromDB("SELECT WS_DOC_NO FROM  PRODUCTION.WARPING_STATUS_WEAVING_LOOM_HEADER WHERE APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"'");
            
            if(!Doc_No.equals(""))
            {
                String pMessage = "<html>  ";

                    pMessage = pMessage + "Dear Sir,<br><br> Felt Warping Status of Weaving Loom for (" + data.getStringValueFromDB("SELECT DAYNAME('"+EITLERPGLOBAL.getCurrentDateDB()+"')") + ", '"+EITLERPGLOBAL.getCurrentDateDB()+"').<br><br>Please find attachment. ";
        
                    String recievers = "brdfltweave1@dineshmills.com";
                    //String recievers = "daxesh@dineshmills.com";

                    HashMap parameterMap = new HashMap();

                    parameterMap.put("WS_DOC_NO", Doc_No);
             
                    String sql = "SELECT H.AS_ON_DATE,D.* FROM PRODUCTION.WARPING_STATUS_WEAVING_LOOM_HEADER H,\n" +
                                "PRODUCTION.WARPING_STATUS_WEAVING_LOOM_DETAIL D\n" +
                                "WHERE H.WS_DOC_NO=D.WS_DOC_NO AND H.WS_DOC_NO='"+Doc_No+"'  ORDER BY PRODUCT_GROUP";
                    
                    String fileName = "WARPING_STATUS_WEAVING_LOOM";
                   
                    Connection Conn = data.getConn();
                    generateReport(parameterMap, Conn, "/EITLERP/FeltSales/ProductionReportMail/WarpingStatus.jrxml", 1, sql, fileName);
                    System.out.println("PDF Generated");
 
                    String pSubject = "Felt Warping Status of Weaving Loom";
                   
                    //String pcc = "rishineekhra@dineshmills.com";
                    String pcc = "aditya@dineshmills.com,abtewary@dineshmills.com,yrpatel@dineshmills.com,amitkanti@dineshmills.com,raghavendra@dineshmills.com,sdmlerp@dineshmills.com";
                    
                    String Path = "/Email_Attachment/WARPING_STATUS_WEAVING_LOOM.pdf";
                    String PFiles = "WARPING_STATUS_WEAVING_LOOM.pdf";

                    JavaMail.SendMailwithAttachment(recievers, pMessage, pSubject, pcc, Path, PFiles);
            
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void SendMail_WarpingPreparationACNE()
    {
        try {
            
            String Doc_No ="";
            Doc_No = data.getStringValueFromDB("SELECT WP_ACNE_NO FROM  PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_HEADER WHERE APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"'");
            
            if(!Doc_No.equals(""))
            {
                String pMessage = "<html>  ";

                    pMessage = pMessage + "Dear Sir,<br><br> Felt Warping Preparation of Weaving Looms (ACNE) for (" + data.getStringValueFromDB("SELECT DAYNAME('"+EITLERPGLOBAL.getCurrentDateDB()+"')") + ", '"+EITLERPGLOBAL.getCurrentDateDB()+"').<br><br>Please find attachment. ";
        
                    String recievers = "brdfltweave1@dineshmills.com";
                    //String recievers = "daxesh@dineshmills.com";
                    
                    HashMap parameterMap = new HashMap();

                    parameterMap.put("WP_ACNE_NO", Doc_No);
             
                    String sql = "SELECT H.WP_ACNE_NO,H.AS_ON_DATE,D.* FROM PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_HEADER H,PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_DETAIL D\n" +
                        "WHERE H.WP_ACNE_NO=D.WP_ACNE_NO AND H.WP_ACNE_NO='"+Doc_No+"'";
                    
                    String fileName = "WARPING_PREPARATION_WEAVING_LOOM_ACNE";
                   
                    Connection Conn = data.getConn();
                    generateReport(parameterMap, Conn, "/EITLERP/FeltSales/ProductionReportMail/WarpPreparationPlanningACNE.jrxml", 1, sql, fileName);
                    System.out.println("PDF Generated");
 
                    String pSubject = "Felt Warping Preparation of Weaving Looms ACNE";
                   
                    //String pcc = "rishineekhra@dineshmills.com";
                    String pcc = "aditya@dineshmills.com,abtewary@dineshmills.com,yrpatel@dineshmills.com,amitkanti@dineshmills.com,raghavendra@dineshmills.com,sdmlerp@dineshmills.com";
                    
                    //String pcc = "daxesh@dineshmills.com";
                    String Path = "/Email_Attachment/WARPING_PREPARATION_WEAVING_LOOM_ACNE.pdf";
                    String PFiles = "WARPING_PREPARATION_WEAVING_LOOM_ACNE.pdf";

                    JavaMail.SendMailwithAttachment(recievers, pMessage, pSubject, pcc, Path, PFiles);
            
                    
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void SendMail_WarpingPreparationMNE()
    {
        try {
            
            String Doc_No ="";
            Doc_No = data.getStringValueFromDB("SELECT WP_MNE_NO FROM  PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_MNE_HEADER WHERE APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"'");
            
            if(!Doc_No.equals(""))
            {
                String pMessage = "<html>  ";

                    pMessage = pMessage + "Dear Sir,<br><br> Felt Warping Preparation of Weaving Looms (MNE) for (" + data.getStringValueFromDB("SELECT DAYNAME('"+EITLERPGLOBAL.getCurrentDateDB()+"')") + ", '"+EITLERPGLOBAL.getCurrentDateDB()+"').<br><br>Please find attachment. ";
        
                    String recievers = "brdfltweave1@dineshmills.com";
                    //String recievers = "daxesh@dineshmills.com";

                    HashMap parameterMap = new HashMap();

                    parameterMap.put("WP_MNE_NO", Doc_No);
             
                    String sql = "SELECT H.WP_MNE_NO,H.AS_ON_DATE,D.* FROM PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_MNE_HEADER H,PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_MNE_DETAIL D\n" +
                                    "WHERE H.WP_MNE_NO=D.WP_MNE_NO AND H.WP_MNE_NO='"+Doc_No+"'";
                    
                    
                    String fileName = "WARPING_PREPARATION_WEAVING_LOOM_MNE";
                   
                    Connection Conn = data.getConn();
                    generateReport(parameterMap, Conn, "/EITLERP/FeltSales/ProductionReportMail/WarpPreparationPlanningMNE.jrxml", 1, sql, fileName);
                    System.out.println("PDF Generated");
 
                    String pSubject = "Felt Warping Preparation of Weaving Looms MNE";
                   
                    //String pcc = "rishineekhra@dineshmills.com";
                    String pcc = "aditya@dineshmills.com,abtewary@dineshmills.com,yrpatel@dineshmills.com,amitkanti@dineshmills.com,raghavendra@dineshmills.com,sdmlerp@dineshmills.com";
                    
                    String Path = "/Email_Attachment/WARPING_PREPARATION_WEAVING_LOOM_MNE.pdf";
                    String PFiles = "WARPING_PREPARATION_WEAVING_LOOM_MNE.pdf";

                    JavaMail.SendMailwithAttachment(recievers, pMessage, pSubject, pcc, Path, PFiles);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void SendMail_MachneWindingProduction()
    {
        try {
            
            String Doc_No ="";
            //Doc_No = data.getStringValueFromDB("SELECT WP_MNE_NO FROM  PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_MNE_HEADER WHERE APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"'");
            Doc_No = data.getStringValueFromDB("SELECT * FROM PRODUCTION.WINDING_MACHINES_HEADER WHERE APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"'");
            if(!Doc_No.equals(""))
            {
                String pMessage = "<html>  ";

                    pMessage = pMessage + "Dear Sir,<br><br> Felt Winding Production Report for (" + data.getStringValueFromDB("SELECT DAYNAME('"+EITLERPGLOBAL.getCurrentDateDB()+"')") + ", '"+EITLERPGLOBAL.getCurrentDateDB()+"').<br><br>Please find attachment. ";
        
                    String recievers = "brdfltweave1@dineshmills.com";
                    //String recievers = "daxesh@dineshmills.com";

                    HashMap parameterMap = new HashMap();

                    parameterMap.put("WM_DOC_NO", Doc_No);
             
                    String sql = "SELECT H.AS_ON_DATE,D.* FROM PRODUCTION.WINDING_MACHINES_HEADER H,PRODUCTION.WINDING_MACHINES_DETAIL D\n" +
                                 "WHERE H.WM_DOC_NO=D.WM_DOC_NO AND H.WM_DOC_NO='"+Doc_No+"' ORDER BY WINDING_MACHINE_NAME,ACTUAL_TARGET DESC  ";
                    
                    
                    String fileName = "MACHINE_WINDING_PRODUCTION";
                   
                    Connection Conn = data.getConn();
                    generateReport(parameterMap, Conn, "/EITLERP/FeltSales/ProductionReportMail/Production_data_of_winding_machine.jrxml", 1, sql, fileName);
                    System.out.println("PDF Generated");
 
                    String pSubject = "Felt Winding Production";
                   
                    //String pcc = "rishineekhra@dineshmills.com";
                    String pcc = "aditya@dineshmills.com,abtewary@dineshmills.com,yrpatel@dineshmills.com,amitkanti@dineshmills.com,raghavendra@dineshmills.com,sdmlerp@dineshmills.com";
                    
                    String Path = "/Email_Attachment/MACHINE_WINDING_PRODUCTION.pdf";
                    String PFiles = "MACHINE_WINDING_PRODUCTION.pdf";

                    JavaMail.SendMailwithAttachment(recievers, pMessage, pSubject, pcc, Path, PFiles);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void SendMail_BeamGaitingReport()
    {
        try {
            
            String Doc_No ="";
            //Doc_No = data.getStringValueFromDB("SELECT WP_MNE_NO FROM  PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_MNE_HEADER WHERE APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"'");
            Doc_No = data.getStringValueFromDB("SELECT BGS_DOC_NO FROM PRODUCTION.BEAM_GAITING_STATUS_HEADER WHERE APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"'");
            if(!Doc_No.equals(""))
            {
                String pMessage = "<html>  ";

                    pMessage = pMessage + "Dear Sir,<br><br> Felt Beam Gaiting Status Report for (" + data.getStringValueFromDB("SELECT DAYNAME('"+EITLERPGLOBAL.getCurrentDateDB()+"')") + ", '"+EITLERPGLOBAL.getCurrentDateDB()+"').<br><br>Please find attachment. ";
        
                    String recievers = "brdfltweave1@dineshmills.com";
                    //String recievers = "daxesh@dineshmills.com";

                    HashMap parameterMap = new HashMap();

                    parameterMap.put("BGS_DOC_NO", Doc_No);
             
                    String sql = "SELECT H.BGS_DOC_NO,H.AS_ON_DATE,D.* FROM PRODUCTION.BEAM_GAITING_STATUS_HEADER H,PRODUCTION.BEAM_GAITING_STATUS_DETAIL D \n" +
                                " WHERE H.BGS_DOC_NO=D.BGS_DOC_NO AND H.BGS_DOC_NO='"+Doc_No+"' ORDER BY PRODUCT_GROUP, LOOM_NO ";
                    
                    
                    String fileName = "BEAM_GAITING_STATUS_REPORT";
                   
                    Connection Conn = data.getConn();
                    generateReport(parameterMap, Conn, "/EITLERP/FeltSales/ProductionReportMail/Beam_getting_status_report.jrxml", 1, sql, fileName);
                    System.out.println("PDF Generated");
 
                    String pSubject = "Felt Beam Gaiting Status Report";
                   
                    //String pcc = "rishineekhra@dineshmills.com";
                    String pcc = "aditya@dineshmills.com,abtewary@dineshmills.com,yrpatel@dineshmills.com,amitkanti@dineshmills.com,raghavendra@dineshmills.com,sdmlerp@dineshmills.com";
                    
                    String Path = "/Email_Attachment/BEAM_GAITING_STATUS_REPORT.pdf";
                    String PFiles = "BEAM_GAITING_STATUS_REPORT.pdf";

                    JavaMail.SendMailwithAttachment(recievers, pMessage, pSubject, pcc, Path, PFiles);
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
                stream = clsWarpingPreparationPlanningMail.class.getClass().getResourceAsStream(reportName);
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
