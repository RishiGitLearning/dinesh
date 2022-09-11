/*
 * Report.java
 *
 * Created on May 29, 2004, 4:48 PM
 */

package EITLERP.Utils;
 
/**
 *
 * @author  nrpithva
 */
 
import javax.swing.*;
import java.awt.*;
import java.io.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.*;
import net.sf.jasperreports.view.save.*;
import net.sf.jasperreports.engine.base.*;
import net.sf.jasperreports.engine.data.*;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.fill.*;
import net.sf.jasperreports.engine.print.*;
import net.sf.jasperreports.engine.util.*;
import net.sf.jasperreports.engine.xml.*;
import net.sf.jasperreports.engine.JasperRunManager;
import EITLERP.*;
import java.util.*;
import java.net.*;

public class Report {
    
    /** Creates a new instance of Report */
    public Report() {
        
            }

public static void ViewReport(String pReport,HashMap pParameters)    
{
        try
        {
            JasperReport jasperReport = JasperManager.loadReport(pReport);
            JasperPrint jasperPrint = JasperManager.fillReport(jasperReport, pParameters, data.getConn());
            JasperViewer.viewReport(jasperPrint,false);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,"Could not load report. Following Error occured \n"+e.getMessage());
        }
}

public static void ViewReport(InputStream pReport,HashMap pParameters)    
{
        try
        {
            JasperReport jasperReport = JasperManager.loadReport(pReport);
            JasperPrint jasperPrint = JasperManager.fillReport(jasperReport, pParameters, data.getConn());
            
            JasperViewer.viewReport(jasperPrint,false);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,"Could not load report. Following Error occured \n"+e.getMessage());
        }
}

public static void ExportReportToPDF(String pXMLFile,String ToFile,HashMap pParameters)    
{
        try
        {
            //JasperReport jasperReport = JasperManager.loadReport(pXMLFile);
            
            JasperManager.runReportToPdfFile(pXMLFile,ToFile,pParameters,data.getConn());
            //JasperPrint jasperPrint = JasperManager.fillReport(jasperReport, pParameters, data.getConn());
            //JasperViewer.viewReport(jasperPrint,false);
            
            
            BufferedReader Read=new BufferedReader(new FileReader(new File(ToFile)));
                        
            
            
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,"Could not load report. Following Error occured \n"+e.getMessage());
        }
}



}
