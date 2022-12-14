/*
 * frmRptGRNInfo.java
 *
 * Created on April 16, 2008, 12:01 PM
 */

package EITLERP.Finance.ReportsUI;

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
import TReportWriter.*;
import java.util.*;

public class frmRptAdvance extends javax.swing.JApplet {
    
    private TReportEngine objEngine=new TReportEngine();
    
    /** Initializes the applet frmRptGRNInfo */
    public void init() {
        setSize(404,224);
        initComponents();
        
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtToDate = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cmdPreview = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtMainCode = new javax.swing.JTextField();
        txtMainCodeName = new javax.swing.JTextField();
        txtPartyCode = new javax.swing.JTextField();
        txtPartyCodeName = new javax.swing.JTextField();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jPanel3.setLayout(null);

        jPanel3.setBackground(new java.awt.Color(0, 153, 204));
        jPanel3.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel6.setText("PARTY ADVANCE PAYMENT REPORT");
        jPanel3.add(jLabel6);
        jLabel6.setBounds(9, 8, 230, 15);

        getContentPane().add(jPanel3);
        jPanel3.setBounds(1, 2, 800, 30);

        jLabel2.setText("From Date:");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(20, 70, 70, 20);

        txtFromDate.setColumns(10);
        getContentPane().add(txtFromDate);
        txtFromDate.setBounds(100, 70, 90, 19);

        jLabel3.setText("To :");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(200, 70, 30, 20);

        txtToDate.setColumns(10);
        getContentPane().add(txtToDate);
        txtToDate.setBounds(230, 70, 90, 20);

        jLabel1.setText("Period");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(14, 46, 90, 15);

        cmdPreview.setText("Preview");
        cmdPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewActionPerformed(evt);
            }
        });

        getContentPane().add(cmdPreview);
        cmdPreview.setBounds(270, 200, 88, 25);

        jLabel4.setText("Party Code : ");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(20, 130, 78, 15);

        jLabel5.setText("Main Code :");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(20, 100, 80, 15);

        txtMainCode.setColumns(10);
        getContentPane().add(txtMainCode);
        txtMainCode.setBounds(100, 100, 90, 19);

        txtMainCodeName.setColumns(10);
        getContentPane().add(txtMainCodeName);
        txtMainCodeName.setBounds(200, 100, 180, 19);

        txtPartyCode.setColumns(10);
        txtPartyCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPartyCodeKeyPressed(evt);
            }
        });

        getContentPane().add(txtPartyCode);
        txtPartyCode.setBounds(100, 130, 90, 19);

        txtPartyCodeName.setColumns(10);
        getContentPane().add(txtPartyCodeName);
        txtPartyCodeName.setBounds(200, 130, 180, 19);

    }//GEN-END:initComponents

    private void txtPartyCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyCodeKeyPressed
        // TODO add your handling code here:
           if(evt.getKeyCode()==112) {
            
            LOV aList=new LOV();
            
            aList.SQL="SELECT MAIN_ACCOUNT_CODE,PARTY_CODE,PARTY_NAME FROM D_FIN_PARTY_MASTER WHERE APPROVED=1 ORDER BY PARTY_NAME";
            aList.ReturnCol=1;
            aList.SecondCol=2;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=3;
            aList.UseSpecifiedConn=true;
            aList.dbURL=FinanceGlobal.FinURL;
            
            if(aList.ShowLOV()) {
                txtMainCode.setText(aList.ReturnVal);
                txtMainCodeName.setText(clsAccount.getAccountName(aList.ReturnVal, ""));
                txtPartyCode.setText(aList.SecondVal);
                txtPartyCodeName.setText(clsPartyMaster.getAccountName(aList.ReturnVal,aList.SecondVal));
            }
            
        }
    }//GEN-LAST:event_txtPartyCodeKeyPressed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseClicked
    
    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        // TODO add your handling code here:
        GenerateReport();
    }//GEN-LAST:event_cmdPreviewActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdPreview;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtMainCode;
    private javax.swing.JTextField txtMainCodeName;
    private javax.swing.JTextField txtPartyCode;
    private javax.swing.JTextField txtPartyCodeName;
    private javax.swing.JTextField txtToDate;
    // End of variables declaration//GEN-END:variables
    
    private void GenerateReport() {
        
        
            HashMap Parameters=new HashMap();
            Parameters.put("FROM_DATE", txtFromDate.getText());
            Parameters.put("TO_DATE", txtToDate.getText());
            
            TReportWriter.SimpleDataProvider.TTable objData = clsGeneralReports.getAdvancePaymentReport(EITLERPGLOBAL.gCompanyID, txtMainCode.getText(), txtPartyCode.getText(), txtFromDate.getText(), txtToDate.getText());
            objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/finance/rptAdvancePaid.rpt",Parameters , objData);
            
        
       
    }
}
