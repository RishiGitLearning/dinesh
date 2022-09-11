/*
 * frmRptPendingIndent.java
 *
 * Created on January 19, 2005, 1:07 PM
 */

package EITLERP.Stores;

/**
 *
 * @author  root
 */

import javax.swing.*;
import java.awt.*;
import java.util.*;
import EITLERP.*;
import java.net.*;

public class frmRptRejectedItems extends javax.swing.JApplet {
    
    private EITLComboModel cmbDeptModel=new EITLComboModel();
    
    /** Initializes the applet frmRptPendingIndent */
    public void init() {
        setSize(412,242);
        initComponents();
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jLabel1 = new javax.swing.JLabel();
        chkFromDate = new javax.swing.JCheckBox();
        txtFromDate = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtToDate = new javax.swing.JTextField();
        cmdPreview = new javax.swing.JButton();
        cmdExi = new javax.swing.JButton();
        chkSupp = new javax.swing.JCheckBox();
        txtSuppCode = new javax.swing.JTextField();
        txtSuppName = new javax.swing.JTextField();
        
        getContentPane().setLayout(null);
        
        jLabel1.setBackground(new java.awt.Color(0, 51, 255));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("REJECTED ITEMS LIST");
        jLabel1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setOpaque(true);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(1, 3, 479, 27);
        
        chkFromDate.setText("From Date");
        chkFromDate.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkFromDateItemStateChanged(evt);
            }
        });
        
        getContentPane().add(chkFromDate);
        chkFromDate.setBounds(10, 65, 90, 23);
        
        txtFromDate.setEnabled(false);
        getContentPane().add(txtFromDate);
        txtFromDate.setBounds(102, 66, 114, 21);
        
        jLabel2.setText("To");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(222, 70, 25, 15);
        
        txtToDate.setEnabled(false);
        getContentPane().add(txtToDate);
        txtToDate.setBounds(247, 66, 114, 21);
        
        cmdPreview.setText("Preview");
        cmdPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewActionPerformed(evt);
            }
        });
        
        getContentPane().add(cmdPreview);
        cmdPreview.setBounds(173, 179, 82, 25);
        
        cmdExi.setText("Exit");
        cmdExi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExiActionPerformed(evt);
            }
        });
        
        getContentPane().add(cmdExi);
        cmdExi.setBounds(275, 180, 82, 25);
        
        chkSupp.setText("Supplier");
        chkSupp.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkSuppItemStateChanged(evt);
            }
        });
        
        getContentPane().add(chkSupp);
        chkSupp.setBounds(10, 112, 89, 23);
        
        txtSuppCode.setName("SUPP_ID");
        txtSuppCode.setEnabled(false);
        txtSuppCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSuppCodeFocusLost(evt);
            }
        });
        txtSuppCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSuppCodeKeyPressed(evt);
            }
        });
        
        getContentPane().add(txtSuppCode);
        txtSuppCode.setBounds(104, 114, 115, 19);
        
        txtSuppName.setEditable(false);
        getContentPane().add(txtSuppName);
        txtSuppName.setBounds(104, 137, 283, 19);
        
    }//GEN-END:initComponents

    private void chkSuppItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkSuppItemStateChanged
        // TODO add your handling code here:
        txtSuppCode.setEnabled(chkSupp.isSelected());
    }//GEN-LAST:event_chkSuppItemStateChanged

    private void txtSuppCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSuppCodeFocusLost
        // TODO add your handling code here:
        if(!txtSuppCode.getText().trim().equals("")) {
            txtSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText()));
        }
        
    }//GEN-LAST:event_txtSuppCodeFocusLost

    private void txtSuppCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSuppCodeKeyPressed
        // TODO add your handling code here:
        //=========== Supplier List ===============
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();
            
            aList.SQL="SELECT SUPPLIER_CODE,SUPP_NAME FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND APPROVED=1 ORDER BY SUPP_NAME";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=2;
            
            if(aList.ShowLOV()) {
                txtSuppCode.setText(aList.ReturnVal);
                txtSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID,aList.ReturnVal));
            }
        }
        //=========================================
        
    }//GEN-LAST:event_txtSuppCodeKeyPressed

    private void cmdExiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExiActionPerformed
        // TODO add your handling code here:
        ((JFrame)getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExiActionPerformed
    
    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        // TODO add your handling code here:
        if(chkFromDate.isSelected()) {
            if(txtFromDate.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,"Please enter from date");
                return;
            }
                    
            if(txtToDate.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,"Please enter to date");
                return;
            }
            
            if(!EITLERPGLOBAL.isDate(txtFromDate.getText().trim())) {
                JOptionPane.showMessageDialog(null,"Please enter valid From Date");
                return;
            }
            
            if(!EITLERPGLOBAL.isDate(txtToDate.getText().trim())) {
                JOptionPane.showMessageDialog(null,"Please enter valid To Date");
                return;
            }
        }
        
        if(chkSupp.isSelected())
        {
          if(txtSuppCode.getText().trim().equals(""))  
          {
            JOptionPane.showMessageDialog(null,"Please specify supplier code");
            return;
          }
        }
        
        String FromDate=EITLERPGLOBAL.formatDateDB(txtFromDate.getText());
        String ToDate=EITLERPGLOBAL.formatDateDB(txtToDate.getText());
        String  SuppCode=txtSuppCode.getText();
        
        if(chkSupp.isSelected()&&chkFromDate.isSelected()) {
            try {
                //CompanyID
                //FDATE
                //TDATE
                //SUPPLIER
                URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptRejectedItemsBoth.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&FDATE="+FromDate+"&TDATE="+ToDate+"&SUPPLIER="+SuppCode);
                EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
            }
            catch(Exception e) {
                JOptionPane.showMessageDialog(null,"Error Previwing "+e.getMessage());
            }
            
        }
        
        if(chkSupp.isSelected()&&(!chkFromDate.isSelected())) {
            try {
                //CompanyID
                //SUPPLIER
                URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptRejectedItemsSupp.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&SUPPLIER="+SuppCode);
                EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
            }
            catch(Exception e) {
                JOptionPane.showMessageDialog(null,"Error Previwing "+e.getMessage());
            }
            
            
        }
        
        if(chkFromDate.isSelected()&&(!chkSupp.isSelected())) {
            try {
                //CompanyID
                //FDATE
                //TDATE
                
                URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptRejectedItemsDate.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&FDATE="+FromDate+"&TDATE="+ToDate+"");
                EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
            }
            catch(Exception e) {
                JOptionPane.showMessageDialog(null,"Error Previwing "+e.getMessage());
            }
        }
        
    }//GEN-LAST:event_cmdPreviewActionPerformed
        
    private void chkFromDateItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkFromDateItemStateChanged
        // TODO add your handling code here:
        txtFromDate.setEnabled(chkFromDate.isSelected());
        txtToDate.setEnabled(chkFromDate.isSelected());
        txtFromDate.requestFocus();
    }//GEN-LAST:event_chkFromDateItemStateChanged
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkFromDate;
    private javax.swing.JCheckBox chkSupp;
    private javax.swing.JButton cmdExi;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtSuppCode;
    private javax.swing.JTextField txtSuppName;
    private javax.swing.JTextField txtToDate;
    // End of variables declaration//GEN-END:variables
    
    
}


