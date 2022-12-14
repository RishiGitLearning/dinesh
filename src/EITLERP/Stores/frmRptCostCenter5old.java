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


public class frmRptCostCenter5old extends javax.swing.JApplet {
    
    private EITLComboModel cmbDeptModel=new EITLComboModel();
    private EITLComboModel cmbItemTypeModel=new EITLComboModel();
    
    /** Initializes the applet frmRptPendingIndent */
    public void init() {
        setSize(478,326);
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
        jLabel4 = new javax.swing.JLabel();
        txtCostCenter = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        opgBrief = new javax.swing.JRadioButton();
        opgDetailed = new javax.swing.JRadioButton();
        lblStatus = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cmbItemType = new javax.swing.JComboBox();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(0, 51, 255));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("COST CENERWISE ISSUE");
        jLabel1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setOpaque(true);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(1, 3, 479, 27);

        getContentPane().add(txtFromDate);
        txtFromDate.setBounds(100, 70, 114, 21);

        jLabel2.setText("To");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(220, 70, 25, 15);

        getContentPane().add(txtToDate);
        txtToDate.setBounds(250, 70, 114, 21);

        cmdPreview.setText("Preview");
        cmdPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewActionPerformed(evt);
            }
        });

        getContentPane().add(cmdPreview);
        cmdPreview.setBounds(165, 257, 124, 28);

        cmdExi.setText("Exit");
        cmdExi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExiActionPerformed(evt);
            }
        });

        getContentPane().add(cmdExi);
        cmdExi.setBounds(293, 257, 119, 29);

        jLabel3.setText("From Date");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(20, 70, 72, 15);

        jLabel4.setText("Cost Centers");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(12, 105, 90, 15);

        getContentPane().add(txtCostCenter);
        txtCostCenter.setBounds(100, 100, 257, 21);

        jLabel5.setText("(Enter cost center codes, seperated b comma)");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(100, 130, 284, 15);

        jLabel6.setText("Report Type");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(14, 166, 81, 15);

        opgBrief.setText("Brief");
        buttonGroup1.add(opgBrief);
        getContentPane().add(opgBrief);
        opgBrief.setBounds(102, 163, 114, 23);

        opgDetailed.setText("Detailed");
        buttonGroup1.add(opgDetailed);
        getContentPane().add(opgDetailed);
        opgDetailed.setBounds(218, 163, 114, 23);

        lblStatus.setText("...");
        getContentPane().add(lblStatus);
        lblStatus.setBounds(8, 257, 144, 19);

        jLabel10.setText("Item Type");
        getContentPane().add(jLabel10);
        jLabel10.setBounds(20, 40, 80, 15);

        cmbItemType.setAutoscrolls(true);
        getContentPane().add(cmbItemType);
        cmbItemType.setBounds(100, 40, 190, 24);

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
        
        if(txtFromDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter from date");
            return;
        }
        
        if(txtToDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter to date");
            return;
        }
        
        if(!EITLERPGLOBAL.isDate(txtFromDate.getText())) {
            JOptionPane.showMessageDialog(null,"Invalid From Date");
            return;
        }
        
        if(!EITLERPGLOBAL.isDate(txtFromDate.getText())) {
            JOptionPane.showMessageDialog(null,"Invalid To Date");
            return;
        }
        
        int ItemType=EITLERPGLOBAL.getComboCode(cmbItemType);
        
        if(ItemType==1) {
            GenerateReportGen();
        }
        else if(ItemType==2) {
            GenerateReportRaw();
        }
        
    }//GEN-LAST:event_cmdPreviewActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbItemType;
    private javax.swing.JButton cmdExi;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JRadioButton opgBrief;
    private javax.swing.JRadioButton opgDetailed;
    private javax.swing.JTextField txtCostCenter;
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
    
    private void GenerateReportGen() {
        //lblStatus.setText("Processing...");
        final clsItemStock objItem=new clsItemStock();
        
        //objItem.ProcessDone=false;
        //objItem.ProcessLedger(EITLERPGLOBAL.FinFromDateDB, EITLERPGLOBAL.formatDateDB(txtToDate.getText()));
        
        //new Thread() {
        //    public void run() {
        
        //while(!objItem.ProcessDone) {
        //    lblStatus.setText("Processing ... ");
        //}
        
        lblStatus.setText("");
        String FromDate=EITLERPGLOBAL.formatDateDB(txtFromDate.getText());
        String ToDate=EITLERPGLOBAL.formatDateDB(txtToDate.getText());
        String Condition="";
        
        if(!txtCostCenter.getText().trim().equals("")) {
            Condition=" AND B.COST_CENTER_ID IN ~ "+txtCostCenter.getText()+" ^ ";
        }
        
        
        if(opgBrief.isSelected()) {
            
            try {
                String Fin_FromDate = "";
                Fin_FromDate = "20" + EITLERPGLOBAL.FinancialYear(FromDate).substring(0,2) + "-04-01";
                
                if(EITLERPGLOBAL.gCompanyID==2) //Brd
                {
                    //System.out.println("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptCostCenter5ExBrd.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&FromDate="+FromDate+"&ToDate="+ToDate+"&Condition="+Condition+"&FinFromDate="+EITLERPGLOBAL.FinFromDateDB);
                    //URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptCostCenter5ExBrd.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&FromDate="+FromDate+"&ToDate="+ToDate+"&Condition="+Condition+"&FinFromDate="+EITLERPGLOBAL.FinFromDateDB);
                    
                    URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptCostCenter5ExBrd.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&FromDate="+FromDate+"&ToDate="+ToDate+"&Condition="+Condition+"&FinFromDate="+Fin_FromDate);
                    EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
                }
                
                if(EITLERPGLOBAL.gCompanyID==3) //Ank
                {
                    URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptCostCenter5Ex.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&FromDate="+FromDate+"&ToDate="+ToDate+"&Condition="+Condition+"&FinFromDate="+Fin_FromDate);
                    EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
                }
                
            }
            catch(Exception e) {
                JOptionPane.showMessageDialog(null,"Error Previwing "+e.getMessage());
            }
        }
        
        
        if(opgDetailed.isSelected()) {
            
            try {
                
                if(EITLERPGLOBAL.gCompanyID==2) //Brd
                {
                    URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptCostCenter5Brd.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&FromDate="+FromDate+"&ToDate="+ToDate+"&Condition="+Condition);
                    EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
                }
                
                if(EITLERPGLOBAL.gCompanyID==3) //Ank
                {
                    URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptCostCenter5.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&FromDate="+FromDate+"&ToDate="+ToDate+"&Condition="+Condition);
                    EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
                    
                }
            }
            catch(Exception e) {
                JOptionPane.showMessageDialog(null,"Error Previwing "+e.getMessage());
            }
        }
        
        //};
        //}.start();
    }
    
    private void GenerateReportRaw() {
        lblStatus.setText("");
        String FromDate=EITLERPGLOBAL.formatDateDB(txtFromDate.getText());
        String ToDate=EITLERPGLOBAL.formatDateDB(txtToDate.getText());
        String Condition="";
        
        if(!txtCostCenter.getText().trim().equals("")) {
            Condition=" AND B.COST_CENTER_ID IN ~ "+txtCostCenter.getText()+" ^ ";
        }
        
        
        if(opgBrief.isSelected()) {
            
            try {
                String Fin_FromDate = "";
                Fin_FromDate = "20" + EITLERPGLOBAL.FinancialYear(FromDate).substring(0,2) + "-04-01";
                
                URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptCostCenter5Ex_Raw.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&FromDate="+FromDate+"&ToDate="+ToDate+"&Condition="+Condition+"&FinFromDate="+Fin_FromDate);
                EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
                
            }
            catch(Exception e) {
                JOptionPane.showMessageDialog(null,"Error Previwing "+e.getMessage());
            }
        }
        
        
        if(opgDetailed.isSelected()) {
            
            try {
                
                URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptCostCenter5_Raw.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&FromDate="+FromDate+"&ToDate="+ToDate+"&Condition="+Condition);
                EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
                
            }
            catch(Exception e) {
                JOptionPane.showMessageDialog(null,"Error Previwing "+e.getMessage());
                e.printStackTrace();
            }
        }
        
    }
}


