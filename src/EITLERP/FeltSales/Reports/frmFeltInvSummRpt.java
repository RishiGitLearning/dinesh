/*
 * frmRptInvoiceList.java
 *
 * Created on June 15, 2010, 5:26 PM
 */
package EITLERP.FeltSales.Reports;

//import EITLERP.Sales.ReportsUI.*;
import EITLERP.*;
import javax.swing.*;
import EITLERP.Utils.*;
import TReportWriter.SimpleDataProvider.TRow;
import TReportWriter.SimpleDataProvider.TTable;
import TReportWriter.TReportEngine;
import java.awt.*;
import java.util.*;
import java.net.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author user
 */

public class frmFeltInvSummRpt extends javax.swing.JApplet {

    private EITLComboModel cmbInvoiceTypeModel;
    
    private TReportEngine objEngine = new TReportEngine();

    /**
     * Initializes the applet frmRptInvoiceList
     */
    public void init() {
        setSize(500, 300);
        initComponents();
    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        txtToDate = new javax.swing.JTextField();
        cmdOk = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("From Date :");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 52, 100, 21);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("To Date :");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(10, 80, 100, 21);
        getContentPane().add(txtFromDate);
        txtFromDate.setBounds(115, 50, 100, 33);
        getContentPane().add(txtToDate);
        txtToDate.setBounds(115, 80, 100, 33);

        cmdOk.setText("OK");
        cmdOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOkActionPerformed(evt);
            }
        });
        getContentPane().add(cmdOk);
        cmdOk.setBounds(110, 130, 60, 33);

        cmdCancel.setText("CANCEL");
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });
        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(180, 130, 100, 33);

        jPanel1.setBackground(new java.awt.Color(0, 102, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setMinimumSize(new java.awt.Dimension(1000, 29));
        jPanel1.setLayout(null);

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Felt Invoice Summary Report");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(2, 4, 460, 20);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(2, 2, 470, 29);
    }// </editor-fold>//GEN-END:initComponents

    private void cmdOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdOkActionPerformed
        // TODO add your handling code here:
        if (!Validate()) {
            return;
        } else {
            PreviewReport();
        }
    }//GEN-LAST:event_cmdOkActionPerformed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        // TODO add your handling code here:
        txtFromDate.setText("");
        txtToDate.setText("");
        txtFromDate.requestFocus();
    }//GEN-LAST:event_cmdCancelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdOk;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtToDate;
    // End of variables declaration//GEN-END:variables

    private boolean Validate() {
        //Form level validations
        if (txtFromDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter from date");
            return false;
        } else if (!EITLERPGLOBAL.isDate(txtFromDate.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid From Date in DD/MM/YYYY format.");
            return false;
        }

        if (txtToDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter To date");
            return false;
        } else if (!EITLERPGLOBAL.isDate(txtToDate.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid To Date in DD/MM/YYYY format.");
            return false;
        }

        return true;
    }

    private void PreviewReport() {
        
        Connection Conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            Conn = data.getConn();
            st = Conn.createStatement();

            HashMap parameterMap = new HashMap();

            String fdt = txtFromDate.getText();
            String tdt = txtToDate.getText();
            String FromDate=EITLERPGLOBAL.formatDateDB(txtFromDate.getText());
            String ToDate=EITLERPGLOBAL.formatDateDB(txtToDate.getText());

            parameterMap.put("RUNDATE", EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime());
            parameterMap.put("FROM_DATE", fdt);
            parameterMap.put("TO_DATE", tdt);
            
            ReportRegister rpt = new ReportRegister(parameterMap, Conn);

            String strSQL = "SELECT RIGHT(INVOICE_NO,4) AS INVOICE_NO,INVOICE_DATE,BALE_NO,PARTY_CODE,";
            strSQL += "SUBSTRING(PARTY_NAME,1,22) AS PARTY_NAME,PIECE_NO,CHARGE_CODE,";
            strSQL += "ACTUAL_WEIGHT AS GROSS_QTY,BAS_AMT,CGST_AMT ,SGST_AMT ,IGST_AMT, DISC_AMT,INSURANCE_AMT,NET_AMT,INVOICE_AMT,(CST2+CST5+VAT1+VAT4) AS CST_VAT,TCS_AMT,SEAM_CHG,CHEM_TRT_CHG ";
            strSQL += "FROM PRODUCTION.FELT_SAL_INVOICE_HEADER ";
            strSQL += "WHERE APPROVED=1 AND CANCELLED=0 ";
            strSQL += "AND INVOICE_DATE>='" + FromDate + "' ";
            strSQL += "AND INVOICE_DATE<='" + ToDate + "' ";
            strSQL += "ORDER BY INVOICE_NO,INVOICE_DATE";

            System.out.println("SQL QUERY : " + strSQL);
            rpt.setReportName("/EITLERP/FeltSales/Reports/FeltInvoiceSummRpt.jrxml", 1, strSQL); //productlist is the name of my jasper file.
            rpt.callReport();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
                Conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
