/*
 * frmRptPendingIsuueList.java
 *
 * Created on January 19, 2005, 1:07 PM
 */

package EITLERP.ReportUI;

/**
 *
 * @author  root
 */

import javax.swing.*;
import java.awt.*;
import java.util.*;
import EITLERP.*;
import java.net.*;

public class frmRptMaterialFinal extends javax.swing.JApplet {
    
    
    /** Initializes the applet frmRptPendingIsuueList */
    public void init() {
        setSize(370,180);
        initComponents();
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        txtPoNo = new javax.swing.JTextField();
        cmdPreview = new javax.swing.JButton();
        cmdExi = new javax.swing.JButton();
        lblPoNo = new javax.swing.JLabel();
        lblSuppName = new javax.swing.JLabel();
        lblItemName = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(0, 153, 204));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("RAW MATERIAL FINAL REPORT");
        jLabel1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setOpaque(true);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(1, 3, 479, 27);

        getContentPane().add(txtPoNo);
        txtPoNo.setBounds(160, 60, 114, 21);

        cmdPreview.setText("Preview");
        cmdPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewActionPerformed(evt);
            }
        });

        getContentPane().add(cmdPreview);
        cmdPreview.setBounds(60, 110, 105, 28);

        cmdExi.setText("Exit");
        cmdExi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExiActionPerformed(evt);
            }
        });

        getContentPane().add(cmdExi);
        cmdExi.setBounds(180, 110, 107, 28);

        lblPoNo.setText("PO NO : ");
        getContentPane().add(lblPoNo);
        lblPoNo.setBounds(90, 60, 60, 20);

        lblSuppName.setPreferredSize(new java.awt.Dimension(100, 50));
        getContentPane().add(lblSuppName);
        lblSuppName.setBounds(240, 170, 230, 20);

        lblItemName.setPreferredSize(new java.awt.Dimension(100, 50));
        getContentPane().add(lblItemName);
        lblItemName.setBounds(240, 130, 230, 20);

    }//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_formMouseClicked
        
    private void cmdExiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExiActionPerformed
        // TODO add your handling code here:
        ((JFrame)getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExiActionPerformed
    
    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        // TODO add your handling code here:
        
         if(txtPoNo.getText().trim().equals("")){
           JOptionPane.showMessageDialog(null,"Please Enter PO No");
         }
         
                     
         String Condition ="";
         String po_no=(txtPoNo.getText().trim());
         
         try {
                System.out.println("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptMaterialFinal.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&PO_NO="+po_no+"&COMPANY_ID="+EITLERPGLOBAL.gCompanyID);
                URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptMaterialFinal.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&PO_NO="+po_no+"&COMPANY_ID="+EITLERPGLOBAL.gCompanyID);
                EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
         } catch(Exception e) {
             JOptionPane.showMessageDialog(null,"Following Error Occur"+e.getMessage());
             e.printStackTrace();
         }
         
     
    }//GEN-LAST:event_cmdPreviewActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cmdExi;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblItemName;
    private javax.swing.JLabel lblPoNo;
    private javax.swing.JLabel lblSuppName;
    private javax.swing.JTextField txtPoNo;
    // End of variables declaration//GEN-END:variables
    
    
}

