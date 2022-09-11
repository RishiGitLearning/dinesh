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

public class frmRptPendingIndent extends javax.swing.JApplet {
    
    private EITLComboModel cmbDeptModel=new EITLComboModel();
    
    /** Initializes the applet frmRptPendingIndent */
    public void init() {
        setSize(412,242);
        initComponents();
        GenerateCombo();
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
        chkDept = new javax.swing.JCheckBox();
        cmbDept = new javax.swing.JComboBox();
        cmdPreview = new javax.swing.JButton();
        cmdExi = new javax.swing.JButton();
        
        getContentPane().setLayout(null);
        
        jLabel1.setBackground(new java.awt.Color(0, 51, 255));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Pending Indents List");
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
        
        chkDept.setText("Department");
        chkDept.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkDeptItemStateChanged(evt);
            }
        });
        
        getContentPane().add(chkDept);
        chkDept.setBounds(11, 113, 103, 23);
        
        cmbDept.setEnabled(false);
        getContentPane().add(cmbDept);
        cmbDept.setBounds(116, 112, 243, 24);
        
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
        
    }//GEN-END:initComponents

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
        
        String FromDate=EITLERPGLOBAL.formatDateDB(txtFromDate.getText());
        String ToDate=EITLERPGLOBAL.formatDateDB(txtToDate.getText());
        int DeptID=EITLERPGLOBAL.getComboCode(cmbDept);
        
        if(chkDept.isSelected()&&chkFromDate.isSelected()) {
            try {
                //CompanyID
                //FDATE
                //TDATE
                //DEPT
                URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptPIndentBoth.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&FDATE="+FromDate+"&TDATE="+ToDate+"&DEPT="+DeptID);
                EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
            }
            catch(Exception e) {
                JOptionPane.showMessageDialog(null,"Error Previwing "+e.getMessage());
            }
            
        }
        
        if(chkDept.isSelected()&&(!chkFromDate.isSelected())) {
            try {
                //CompanyID
                //DEPT
                URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptPIndentDept.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&DEPT="+DeptID);
                EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
            }
            catch(Exception e) {
                JOptionPane.showMessageDialog(null,"Error Previwing "+e.getMessage());
            }
            
            
        }
        
        if(chkFromDate.isSelected()&&(!chkDept.isSelected())) {
            try {
                //CompanyID
                //FDATE
                //TDATE
                
                URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptPIndentDate.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&FDATE="+FromDate+"&TDATE="+ToDate+"");
                EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
            }
            catch(Exception e) {
                JOptionPane.showMessageDialog(null,"Error Previwing "+e.getMessage());
            }
        }
        
    }//GEN-LAST:event_cmdPreviewActionPerformed
    
    private void chkDeptItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkDeptItemStateChanged
        // TODO add your handling code here:
        cmbDept.setEnabled(chkDept.isSelected());
    }//GEN-LAST:event_chkDeptItemStateChanged
    
    private void chkFromDateItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkFromDateItemStateChanged
        // TODO add your handling code here:
        txtFromDate.setEnabled(chkFromDate.isSelected());
        txtToDate.setEnabled(chkFromDate.isSelected());
        txtFromDate.requestFocus();
    }//GEN-LAST:event_chkFromDateItemStateChanged
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkDept;
    private javax.swing.JCheckBox chkFromDate;
    private javax.swing.JComboBox cmbDept;
    private javax.swing.JButton cmdExi;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtToDate;
    // End of variables declaration//GEN-END:variables
    
    private void GenerateCombo() {
        HashMap List=new HashMap();
        
        //----- Generate Department Combo ------- //
        cmbDeptModel=new EITLComboModel();
        cmbDept.removeAllItems();
        cmbDept.setModel(cmbDeptModel);
        
        List=clsDepartment.getList(" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID);
        for(int i=1;i<=List.size();i++) {
            clsDepartment ObjDept=(clsDepartment) List.get(Integer.toString(i));
            ComboData aData=new ComboData();
            aData.Code=(int) ObjDept.getAttribute("DEPT_ID").getVal();
            aData.Text=(String)ObjDept.getAttribute("DEPT_DESC").getObj();
            cmbDeptModel.addElement(aData);
        }
        //------------------------------ //
    }
    
}


