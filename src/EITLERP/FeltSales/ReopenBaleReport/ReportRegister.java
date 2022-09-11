/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.ReopenBaleReport;

import EITLERP.FeltSales.FeltFinishing.*;
import java.util.ArrayList;
import java.util.Map;

import java.awt.Container;
import java.sql.Connection;
import java.util.HashMap;
import javax.swing.JFrame;

import java.io.InputStream;
import javax.swing.JOptionPane;

import EITLERP.*;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JRViewer;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

//import data;
/**
 *
 * @author 
 */
/**
 *
 * @author 
 */
public class ReportRegister extends JFrame {

    HashMap hm = null;
    Connection con = null;
    String reportName, mbillno, msql;
    int mcopy;

    /**
     * Creates a new instance of Report
     */
    public ReportRegister() {
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public ReportRegister(HashMap map) {
        this.hm = map;
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    }

    public ReportRegister(HashMap map, Connection con) {
        setSize(800, 600);
        this.hm = map;
        this.con = con;
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("FELT PACKING REPORT");

    }

    public void setReportName(String rptName, int rcopy, String rsql) {
        this.reportName = rptName;
        this.mcopy = rcopy;
        this.msql = rsql;
    }

    public void callReport() {
        try {
            JasperPrint jasperPrint = generateReport();

            JRViewer viewer = new JRViewer(jasperPrint);
            Container c = getContentPane();
            c.add(viewer);
            this.setVisible(true);
        } catch (Exception e) {
        }
    }

    public void callConnectionLessReport() {
        JasperPrint jasperPrint = generateEmptyDataSourceReport();
        this.setVisible(true);
    }

    public void closeReport() {
    }

    /**
     * this method will call the report from data source
     */
    public JasperPrint generateReport() {
        InputStream stream = null;
        try {
            if (con == null) {
                try {
                    con = data.getConn();

                } catch (Exception ex) {
                }
            }
            JasperPrint jasperPrint = null;
            if (hm == null) {
                hm = new HashMap();
            }
            try {
                JasperDesign jd;
                stream = this.getClass().getResourceAsStream(reportName);
                jd = JRXmlLoader.load(stream);
                JRDesignQuery newquery = new JRDesignQuery();
                newquery.setText(msql);

                jd.setQuery(newquery);
                JasperReport jr = JasperCompileManager.compileReport(jd);
                jasperPrint = JasperFillManager.fillReport(jr, hm, con);
            } catch (JRException e) {
                e.printStackTrace();
            }
            return jasperPrint;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    /**
     * call this method when your report has an empty data sourc
     *
     * @return e
     */
    public JasperPrint generateEmptyDataSourceReport() {
        try {
            JasperPrint jasperPrint = null;
            if (hm == null) {
                hm = new HashMap();
            }
            try {
                jasperPrint = JasperFillManager.fillReport("E:/" + reportName + ".jasper", hm, new JREmptyDataSource());
            } catch (JRException e) {
                e.printStackTrace();
            }
            return jasperPrint;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

}
