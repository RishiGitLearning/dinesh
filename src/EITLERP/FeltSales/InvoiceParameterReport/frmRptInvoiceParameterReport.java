/*
 * frmRptGRNInfo.java
 *
 * Created on April 16, 2008, 12:01 PM
 */

package EITLERP.FeltSales.InvoiceParameterReport;

/**
 *
 * @author  root
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



public class frmRptInvoiceParameterReport extends javax.swing.JApplet {
    
    private EITLComboModel cmbReceiptTypeModel;
    private EITLComboModel cmbReportTypeModel;
    private TReportEngine objEngine=new TReportEngine();
    private TReportWriter.SimpleDataProvider.TTable objData=new TReportWriter.SimpleDataProvider.TTable();
    private EITLTableCellRenderer RowFormat=new EITLTableCellRenderer();
    //private clsExcelExporter exp = new clsExcelExporter();
    EITLTableModel DataModel= new EITLTableModel();
  //  private TReportEngine objEngine=new TReportEngine();
    
    /** Initializes the applet frmRptGRNInfo */
    
    public void init() {
       // setSize(424,264);
         setSize(500, 200);
        initComponents();
        
        
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
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
        jButton2 = new javax.swing.JButton();

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        getContentPane().setLayout(null);

        jPanel3.setBackground(new java.awt.Color(0, 153, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel3.setLayout(null);

        jLabel6.setText("Felt Invoice Parameter  Report");
        jPanel3.add(jLabel6);
        jLabel6.setBounds(10, 0, 230, 15);

        getContentPane().add(jPanel3);
        jPanel3.setBounds(0, 0, 450, 30);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("From Date :");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(20, 70, 90, 20);

        txtFromDate.setColumns(10);
        getContentPane().add(txtFromDate);
        txtFromDate.setBounds(120, 70, 110, 30);

        jLabel3.setText("To Date :");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(240, 70, 60, 20);

        txtToDate.setColumns(10);
        getContentPane().add(txtToDate);
        txtToDate.setBounds(300, 70, 120, 30);

        jLabel1.setText("Period");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 40, 90, 15);

        jButton1.setText("Report");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(150, 120, 100, 25);

        jButton2.setText("PDF Report");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2);
        jButton2.setBounds(260, 120, 120, 30);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
     ReportShow();  
    }//GEN-LAST:event_jButton1ActionPerformed
    
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
     
        String FromDate = EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim());
        String ToDate = EITLERPGLOBAL.formatDateDB(txtToDate.getText().trim());
        Connection Conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            Conn = data.getConn();
            st = Conn.createStatement();

            HashMap parameterMap = new HashMap();

            ReportRegister rpt = new ReportRegister(parameterMap, Conn);

            String strSQL = "SELECT DOC_NO,PROCESSING_DATE,BALE_NO,PARTY_CODE,PARTY_NAME,CHARGE_CODE_NEW,CRITICAL_LIMIT_NEW,INSURANCE_CODE FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE DOC_DATE>='"+FromDate+"' AND DOC_DATE<='"+ToDate+"' AND APPROVED=1 AND CANCELED=0";
            
            
            rpt.setReportName("/EITLERP/FeltSales/InvoiceParameterReport/Invoice_Parameter.jrxml", 1, strSQL); //productlist is the name of my jasper file.
            rpt.callReport();

        } catch (Exception e) {
            e.printStackTrace();
        } 
            // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed
        
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
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
        
        if(txtFromDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter from date");
            return false;
        } else if(!EITLERPGLOBAL.isDate(txtFromDate.getText())) {
            JOptionPane.showMessageDialog(null,"Invalid From Date in DD/MM/YYYY format.");
            return false;
        }
        
        if(txtToDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter To date");
            return false;
        } else if(!EITLERPGLOBAL.isDate(txtToDate.getText())) {
            JOptionPane.showMessageDialog(null,"Invalid To Date in DD/MM/YYYY format.");
            return false;
        }
        
        return true;
    }
    
   private void ReportShow() {
        
        try {
            
            
            TReportWriter.SimpleDataProvider.TRow objRow;
            TReportWriter.SimpleDataProvider.TTable objReportData=new TReportWriter.SimpleDataProvider.TTable();
            
            
            objReportData.AddColumn("DOC_NO");
            objReportData.AddColumn("PROCESSING_DATE");
            objReportData.AddColumn("BALE_NO");
            objReportData.AddColumn("PARTY_CODE");
            objReportData.AddColumn("CHARGE_CODE_NEW");
            objReportData.AddColumn("CRITICAL_LIMIT_NEW");
            objReportData.AddColumn("TRANSPORTER_CODE");
            objReportData.AddColumn("INSURANCE_CODE");
            
            
            TReportWriter.SimpleDataProvider.TRow objOpeningRow=objReportData.newRow();
            
            objOpeningRow.setValue("DOC_NO","");
            objOpeningRow.setValue("PROCESSING_DATE","");
            objOpeningRow.setValue("BALE_NO","");
            objOpeningRow.setValue("PARTY_CODE","");
            objOpeningRow.setValue("CHARGE_CODE_NEW","");
            objOpeningRow.setValue("CRITICAL_LIMIT_NEW","");
            objOpeningRow.setValue("TRANSPORTER_CODE","");
            objOpeningRow.setValue("INSURANCE_CODE","");
            
            String strSQL="SELECT * FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='"+ EITLERPGLOBAL.formatDateDB(txtFromDate.getText())+"' AND PROCESSING_DATE<='"+EITLERPGLOBAL.formatDateDB(txtToDate.getText())+"' AND APPROVED=1 AND CANCELED=0";
            
            System.out.println(strSQL);
            ResultSet rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            int Counter = 0;
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    Counter ++;
                    objRow=objReportData.newRow();
                    
                    objRow.setValue("DOC_NO",UtilFunctions.getString(rsTmp,"DOC_NO",""));
                    objRow.setValue("PROCESSING_DATE",UtilFunctions.getString(rsTmp,"PROCESSING_DATE",""));
                    objRow.setValue("BALE_NO",UtilFunctions.getString(rsTmp,"BALE_NO",""));
                    objRow.setValue("PARTY_CODE",UtilFunctions.getString(rsTmp,"PARTY_CODE",""));
                    objRow.setValue("CHARGE_CODE_NEW",UtilFunctions.getString(rsTmp,"CHARGE_CODE_NEW",""));
                    objRow.setValue("CRITICAL_LIMIT_NEW",UtilFunctions.getString(rsTmp,"CRITICAL_LIMIT_NEW",""));
                    objRow.setValue("TRANSPORTER_CODE",UtilFunctions.getString(rsTmp,"TRANSPORTER_CODE",""));
                    objRow.setValue("INSURANCE_CODE",UtilFunctions.getString(rsTmp,"INSURANCE_CODE",""));
                    
                    objReportData.AddRow(objRow);
                    
                    rsTmp.next();
                }
            }
            
            int Comp_ID = EITLERPGLOBAL.gCompanyID;
            
            HashMap Parameters=new HashMap();
            Parameters.put("FROM_DATE",txtFromDate.getText().trim());
            Parameters.put("TO_DATE",txtToDate.getText().trim());
            Parameters.put("SYS_DATE", EITLERPGLOBAL.getCurrentDate()+" "+EITLERPGLOBAL.getCurrentTime());
            
            objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/Production/rptInvoiceParameter.rpt",Parameters,objReportData);
            
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
  
    
}
