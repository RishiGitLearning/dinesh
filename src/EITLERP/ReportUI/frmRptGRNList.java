/*
 * frmRptPendingIndent.java
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

public class frmRptGRNList extends javax.swing.JApplet {
    private EITLComboModel cmbItemTypeModel=new EITLComboModel();
    
    
    /** Initializes the applet frmRptPendingIndent */
    public void init() {
        setSize(401,196);
        initComponents();
        GenerateCombo();
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtToDate = new javax.swing.JTextField();
        cmdPreview = new javax.swing.JButton();
        cmdExi = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        chkPending = new javax.swing.JCheckBox();
        cmbItemType = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(153, 153, 255));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText(" GRN LIST");
        jLabel1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setOpaque(true);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(1, 3, 479, 27);

        getContentPane().add(txtFromDate);
        txtFromDate.setBounds(100, 105, 114, 21);

        jLabel2.setText("To");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(220, 105, 25, 15);

        getContentPane().add(txtToDate);
        txtToDate.setBounds(250, 105, 114, 21);

        cmdPreview.setText("Preview");
        cmdPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewActionPerformed(evt);
            }
        });

        getContentPane().add(cmdPreview);
        cmdPreview.setBounds(125, 138, 105, 28);

        cmdExi.setText("Exit");
        cmdExi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExiActionPerformed(evt);
            }
        });

        getContentPane().add(cmdExi);
        cmdExi.setBounds(253, 138, 107, 28);

        jLabel3.setText("From Date");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(20, 105, 76, 15);

        chkPending.setText("Pending GRN");
        getContentPane().add(chkPending);
        chkPending.setBounds(20, 68, 120, 23);

        getContentPane().add(cmbItemType);
        cmbItemType.setBounds(122, 40, 190, 24);

        jLabel10.setText("Item Type");
        getContentPane().add(jLabel10);
        jLabel10.setBounds(26, 40, 80, 15);

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
        int ItemType=EITLERPGLOBAL.getComboCode(cmbItemType);
        
        if(txtFromDate.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Plase Enter From Date");
        }
        
        if(txtToDate.getText().trim().equals("")){
            
            JOptionPane.showMessageDialog(null,"Plase Enter TO Date");
        }
        
        if(EITLERPGLOBAL.compareDate(txtFromDate.getText(),txtFromDate.getText()) ==1 ) {
            JOptionPane.showMessageDialog(null,"From Date must be require greater then or equal to To Date");
            return;
        }
        String Condition ="";
        String Fromdate = EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim());
        String Todate = EITLERPGLOBAL.formatDateDB(txtToDate.getText().trim());
        
        int GrnType = EITLERPGLOBAL.getComboCode(cmbItemType);
        
        if (chkPending.isSelected()) {
            
            try{
                URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptGRNPending.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&FDate="+Fromdate+"&TDate="+Todate+"&ItemType="+ItemType+"&GrnType="+GrnType);
                System.out.println(ReportFile);
                EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
                
                //frmRptGRNDATE.
                
            }
            
            catch(Exception e) {
                JOptionPane.showMessageDialog(null,"Following Error Occur"+e.getMessage());
            }
            
        }
        else {
            try{
                URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptGPRDATEWISE.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&FromDate="+Fromdate+"&ToDate="+Todate+"&GrnType="+GrnType);
                System.out.println(ReportFile);
                EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
                
                //frmRptGRNDATE.
                
            }
            
            catch(Exception e) {
                JOptionPane.showMessageDialog(null,"Following Error Occur"+e.getMessage());
            }
            
        }
        
        
        
    }//GEN-LAST:event_cmdPreviewActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox chkPending;
    private javax.swing.JComboBox cmbItemType;
    private javax.swing.JButton cmdExi;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtToDate;
    // End of variables declaration//GEN-END:variables
    
    
    private void GenerateCombo() {
        try {
            
            cmbItemTypeModel=new EITLComboModel();
            cmbItemType.removeAll();
            cmbItemType.setModel(cmbItemTypeModel);
            
            ComboData objData=new ComboData();
            objData.Code=1;
            objData.Text="General";
            
            cmbItemTypeModel.addElement(objData);
            
            objData=new ComboData();
            objData.Code=2;
            objData.Text="Raw Material";
            
            cmbItemTypeModel.addElement(objData);
            
        }
        catch(Exception e) {
            
        }
        
    }
}


