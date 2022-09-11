/*
 * frmRptGRNInfo.java
 *
 * Created on April 16, 2008, 12:01 PM
 */
package EITLERP.FeltSales.SalesReturnReport;

/**
 *
 * @author root
 */
import EITLERP.*;
import EITLERP.Finance.*;
import EITLERP.Utils.*;
import EITLERP.Utils.SimpleDataProvider.*;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.net.*;
import java.util.*;
import TReportWriter.*;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.net.URL;
import javax.swing.JOptionPane;
import java.io.*;
import java.sql.ResultSet;
import javax.swing.JTable;

public class frmRptSalesReturnReport extends javax.swing.JApplet {

    private EITLComboModel cmbReceiptTypeModel;
    private EITLComboModel cmbReportTypeModel;
    private TReportEngine objEngine = new TReportEngine();
    private TReportWriter.SimpleDataProvider.TTable objData = new TReportWriter.SimpleDataProvider.TTable();
    private EITLTableCellRenderer RowFormat = new EITLTableCellRenderer();
    //private clsExcelExporter exp = new clsExcelExporter();
    EITLTableModel DataModel = new EITLTableModel();
  //  private TReportEngine objEngine=new TReportEngine();

    /**
     * Initializes the applet frmRptGRNInfo
     */
    public void init() {
        // setSize(424,264);
        setSize(500, 200);
        initComponents();

    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtToDate = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        getContentPane().setLayout(null);

        jPanel3.setBackground(new java.awt.Color(0, 153, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel3.setLayout(null);

        jLabel6.setText("Felt Sales Return  Report");
        jPanel3.add(jLabel6);
        jLabel6.setBounds(10, 0, 230, 17);

        getContentPane().add(jPanel3);
        jPanel3.setBounds(0, 0, 450, 30);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("From Date :");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(20, 70, 90, 20);

        txtFromDate.setColumns(10);
        getContentPane().add(txtFromDate);
        txtFromDate.setBounds(120, 70, 110, 27);

        jLabel3.setText("To Date :");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(240, 70, 60, 20);

        txtToDate.setColumns(10);
        getContentPane().add(txtToDate);
        txtToDate.setBounds(300, 70, 120, 30);

        jLabel1.setText("Period");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 40, 90, 17);

        jButton1.setText("Report");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(210, 120, 100, 29);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        ReportShow();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel3;
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

    private void ReportShow() {

        try {

            TReportWriter.SimpleDataProvider.TRow objRow;
            TReportWriter.SimpleDataProvider.TTable objReportData = new TReportWriter.SimpleDataProvider.TTable();

            objReportData.AddColumn("PIECE_NO");
            objReportData.AddColumn("INVOICE_NO");
            objReportData.AddColumn("INVOICE_DATE");
            objReportData.AddColumn("PARTY_CODE");
            objReportData.AddColumn("PARTY_NAME");
            objReportData.AddColumn("QUALITY_NO");
            objReportData.AddColumn("LENGTH");
            objReportData.AddColumn("WIDTH");
            objReportData.AddColumn("TOTAL_GROSS");
            objReportData.AddColumn("TOTAL_NET_AMOUNT");
            objReportData.AddColumn("GROSS_SQ_MTR");
            objReportData.AddColumn("GROSS_KG");
            objReportData.AddColumn("GROSS_AMOUNT");
            objReportData.AddColumn("TRD_DISCOUNT");
            objReportData.AddColumn("NET_AMOUNT");

            TReportWriter.SimpleDataProvider.TRow objOpeningRow = objReportData.newRow();

            objOpeningRow.setValue("PIECE_NO", "");
            objOpeningRow.setValue("INVOICE_NO", "");
            objOpeningRow.setValue("INVOICE_DATE", "");
            objOpeningRow.setValue("PARTY_CODE", "");
            objOpeningRow.setValue("PARTY_NAME", "");
            objOpeningRow.setValue("QUALITY_NO", "");
            objOpeningRow.setValue("LENGTH", "");
            objOpeningRow.setValue("WIDTH", "");
            objOpeningRow.setValue("TOTAL_GROSS", "");
            objOpeningRow.setValue("TOTAL_NET_AMOUNT", "");
            objOpeningRow.setValue("GROSS_SQ_MTR", "");
            objOpeningRow.setValue("GROSS_KG", "");
            objOpeningRow.setValue("GROSS_AMOUNT", "");
            objOpeningRow.setValue("TRD_DISCOUNT", "");
            objOpeningRow.setValue("NET_AMOUNT", "");

            

            String strSQL = "SELECT * FROM PRODUCTION.FELT_SALES_RETURNS_DETAIL WHERE INVOICE_DATE>='"+ EITLERPGLOBAL.formatDateDB(txtFromDate.getText())+"' AND INVOICE_DATE<='"+EITLERPGLOBAL.formatDateDB(txtToDate.getText())+"'";

            System.out.println(strSQL);
            ResultSet rsTmp = data.getResult(strSQL);
            rsTmp.first();

            int Counter = 0;

            if (rsTmp.getRow() > 0) {
                while (!rsTmp.isAfterLast()) {
                    Counter++;
                    objRow = objReportData.newRow();

                    objRow.setValue("PIECE_NO", UtilFunctions.getString(rsTmp, "PIECE_NO", ""));
                    objRow.setValue("INVOICE_NO", UtilFunctions.getString(rsTmp, "INVOICE_NO", ""));
                    objRow.setValue("INVOICE_DATE", UtilFunctions.getString(rsTmp, "INVOICE_DATE", ""));
                    objRow.setValue("PARTY_CODE", UtilFunctions.getString(rsTmp, "PARTY_CODE", ""));
                    objRow.setValue("PARTY_NAME", UtilFunctions.getString(rsTmp, "PARTY_NAME", ""));
                    objRow.setValue("QUALITY_NO", UtilFunctions.getString(rsTmp, "QUALITY_NO", ""));
                    objRow.setValue("LENGTH", UtilFunctions.getString(rsTmp, "LENGTH", ""));
                    objRow.setValue("WIDTH", UtilFunctions.getString(rsTmp, "WIDTH", ""));
                    objRow.setValue("TOTAL_GROSS", UtilFunctions.getString(rsTmp, "TOTAL_GROSS", ""));
                    objRow.setValue("TOTAL_NET_AMOUNT", UtilFunctions.getString(rsTmp, "TOTAL_NET_AMOUNT", ""));
                    objRow.setValue("GROSS_SQ_MTR", UtilFunctions.getString(rsTmp, "GROSS_SQ_MTR", ""));
                    objRow.setValue("GROSS_KG", UtilFunctions.getString(rsTmp, "GROSS_KG", ""));
                    objRow.setValue("GROSS_AMOUNT", UtilFunctions.getString(rsTmp, "GROSS_AMOUNT", ""));
                    objRow.setValue("TRD_DISCOUNT", UtilFunctions.getString(rsTmp, "TRD_DISCOUNT", ""));
                    objRow.setValue("NET_AMOUNT", UtilFunctions.getString(rsTmp, "NET_AMOUNT", ""));

                    objReportData.AddRow(objRow);

                    rsTmp.next();
                }
            }

            int Comp_ID = EITLERPGLOBAL.gCompanyID;

            HashMap Parameters = new HashMap();
            Parameters.put("FROM_DATE", txtFromDate.getText().trim());
            Parameters.put("TO_DATE", txtToDate.getText().trim());
            Parameters.put("SYS_DATE", EITLERPGLOBAL.getCurrentDate()+" "+EITLERPGLOBAL.getCurrentTime());

            objEngine.PreviewReport("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/Production/rptSalesReturns.rpt", Parameters, objReportData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}