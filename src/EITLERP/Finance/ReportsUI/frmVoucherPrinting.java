/*
 * frmVoucherPrinting.java
 *
 * Created on January 20, 2008, 4:00 PM
 */

package EITLERP.Finance.ReportsUI;

/**
 *
 * @author  root
 */
import javax.swing.*;
import java.awt.*;
import java.util.*;
import TReportWriter.*;
import TReportWriter.SimpleDataProvider.*;
import java.net.*;
import java.sql.*;
import EITLERP.*;
import EITLERP.Finance.*;
import EITLERP.Finance.Config.*;
import EITLERP.Finance.ReportsUI.*;


public class frmVoucherPrinting extends javax.swing.JApplet {
    
    private TReportEngine objEngine=new TReportEngine();
    private EITLComboModel cmbFormatModel=new EITLComboModel();
    private HashMap TRReports=new HashMap();
    
    /** Initializes the applet frmVoucherPrinting */
    public void init() {
        setSize(424,250);
        initComponents();
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        lblTitle = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtVoucherNoFrom = new javax.swing.JTextField();
        txtVoucherNoTo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtBookCode = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtToDate = new javax.swing.JTextField();
        cmdPrint = new javax.swing.JButton();
        cmdExit = new javax.swing.JButton();
        cmbFormat = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setForeground(java.awt.Color.white);
        lblTitle.setText(" VOUCHER PRINTING");
        lblTitle.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(-2, 1, 666, 25);

        jLabel1.setText("Voucher No. From:");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(9, 40, 120, 15);

        getContentPane().add(txtVoucherNoFrom);
        txtVoucherNoFrom.setBounds(126, 39, 110, 19);

        getContentPane().add(txtVoucherNoTo);
        txtVoucherNoTo.setBounds(273, 40, 110, 19);

        jLabel2.setText("To");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(245, 41, 20, 15);

        jLabel3.setText("Book Code:");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(50, 68, 71, 20);

        txtBookCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBookCodeFocusLost(evt);
            }
        });

        getContentPane().add(txtBookCode);
        txtBookCode.setBounds(126, 69, 110, 19);

        jLabel4.setText("Date From:");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(50, 102, 70, 15);

        getContentPane().add(txtFromDate);
        txtFromDate.setBounds(124, 100, 110, 19);

        jLabel5.setText("To");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(243, 102, 20, 15);

        getContentPane().add(txtToDate);
        txtToDate.setBounds(271, 101, 110, 19);

        cmdPrint.setText("Print");
        cmdPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPrintActionPerformed(evt);
            }
        });

        getContentPane().add(cmdPrint);
        cmdPrint.setBounds(228, 177, 80, 25);

        cmdExit.setText("Exit");
        cmdExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExitActionPerformed(evt);
            }
        });

        getContentPane().add(cmdExit);
        cmdExit.setBounds(313, 177, 80, 25);

        getContentPane().add(cmbFormat);
        cmbFormat.setBounds(125, 137, 260, 24);

        jLabel6.setText("Print Format:");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(39, 141, 90, 15);

    }//GEN-END:initComponents
    
    private void txtBookCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBookCodeFocusLost
        // TODO add your handling code here:
        GenerateFormatList();
    }//GEN-LAST:event_txtBookCodeFocusLost
    
    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        // TODO add your handling code here:
        try {
            ((JFrame)getParent().getParent().getParent().getParent()).dispose();
        }
        catch(Exception e) {
            
        }
        
    }//GEN-LAST:event_cmdExitActionPerformed
    
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_formMouseClicked
    
    private void cmdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintActionPerformed
        // TODO add your handling code here:
        try {
            String strCondition="",strSQL="";
            
            if(txtBookCode.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,"Please specify Book Code");
                return;
            }
            
            if(!txtVoucherNoFrom.getText().trim().equals("")) {
                strCondition+="AND A.VOUCHER_NO>='"+txtVoucherNoFrom.getText()+"' ";
            }
            
            if(!txtVoucherNoTo.getText().trim().equals("")) {
                strCondition+="AND A.VOUCHER_NO<='"+txtVoucherNoTo.getText()+"' ";
            }
            
            if(!txtBookCode.getText().trim().equals("")) {
                strCondition+="AND A.BOOK_CODE='"+txtBookCode.getText()+"' ";
            }
            
            if(!txtFromDate.getText().trim().equals("")) {
                strCondition+="AND A.VOUCHER_DATE>='"+EITLERPGLOBAL.formatDateDB(txtFromDate.getText())+"' ";
            }
            
            if(!txtToDate.getText().trim().equals("")) {
                strCondition+="AND A.VOUCHER_DATE<='"+EITLERPGLOBAL.formatDateDB(txtToDate.getText())+"' ";
            }
            
            strSQL="SELECT DISTINCT(A.VOUCHER_NO) AS VOUCHER_NO FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B WHERE A.VOUCHER_NO=B.VOUCHER_NO AND A.COMPANY_ID=B.COMPANY_ID "+strCondition+" AND A.APPROVED=1 AND A.CANCELLED=0 ORDER BY A.VOUCHER_NO";
            
            clsConfig objConfig=new clsConfig();
            
            TReportWriter.SimpleDataProvider.TTable AllData=new TReportWriter.SimpleDataProvider.TTable();
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            HashMap Parameters=new HashMap();
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    
                    String VoucherNo=rsTmp.getString("VOUCHER_NO");
                    String BookCode=data.getStringValueFromDB("SELECT BOOK_CODE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+VoucherNo+"'",FinanceGlobal.FinURL);
                    
                    objConfig=(clsConfig)TRReports.get(Integer.toString(EITLERPGLOBAL.getComboCode(cmbFormat)));
                    
                    
                    
                    TReportWriter.SimpleDataProvider.TTable objData=clsVoucherReports.getVoucherReport(VoucherNo, objConfig.ProcessType);
                    
                    AllData.AppendTable(objData);
                    
                    rsTmp.next();
                    
                }
                
                //HashMap Parameters=new HashMap();
                objEngine.PreviewReport(objConfig.ReportFileName,Parameters,AllData);
            }
            
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_cmdPrintActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbFormat;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextField txtBookCode;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtToDate;
    private javax.swing.JTextField txtVoucherNoFrom;
    private javax.swing.JTextField txtVoucherNoTo;
    // End of variables declaration//GEN-END:variables
    
    private void GenerateFormatList() {
        try {
            
            cmbFormatModel=new EITLComboModel();
            cmbFormat.removeAllItems();
            cmbFormat.setModel(cmbFormatModel);
            
            TTable objReports=new TTable();
            objReports.AddColumn("ReportName");
            
            TRReports=clsReportsConfig.getReportNamesForTR(txtBookCode.getText().trim());
            
            for(int i=1;i<=TRReports.size();i++) {
                clsConfig objConfig=(clsConfig)TRReports.get(Integer.toString(i));
                
                ComboData objData=new ComboData();
                objData.Text=objConfig.ReportName;
                objData.Code=i;
                
                cmbFormatModel.addElement(objData);
            }
        }
        catch(Exception e) {
            
        }
    }
    
}