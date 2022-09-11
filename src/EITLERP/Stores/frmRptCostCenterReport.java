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
import java.sql.*;
import java.util.Date;
import EITLERP.Utils.*;
import java.io.*;
import java.text.*;
import EITLERP.Utils.SimpleDataProvider.*;
import TReportWriter.*;


public class frmRptCostCenterReport extends javax.swing.JApplet {
    
    
    private TReportEngine objEngine=new TReportEngine();
    private TReportWriter.SimpleDataProvider.TTable objData=new TReportWriter.SimpleDataProvider.TTable();
    String CostCenterName="";
    String SupplierName="";
    /** Initializes the applet frmRptPendingIndent */
    public void init() {
        setSize(475,315);
        initComponents();
        
        //if(EITLERPGLOBAL.gCompanyID==3) {
        txtSupplierCode.setText("619452");
        lblSupplierName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID,"619452"));
        //}
        if(EITLERPGLOBAL.gCompanyID==3) {
            txtCostCenter.setText("9452");
            lblCostCenterName.setText(clsCostCenter.getCostCenterName(EITLERPGLOBAL.gCompanyID,Integer.parseInt(txtCostCenter.getText().trim())));
        }
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtToDate = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        PanelIssue = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtCostCenter = new javax.swing.JTextField();
        lblCostCenterName = new javax.swing.JLabel();
        cmdPreview = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        PanelGrn = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtSupplierCode = new javax.swing.JTextField();
        lblSupplierName = new javax.swing.JLabel();
        cmdPreviewGRN = new javax.swing.JButton();
        jProgressBar2 = new javax.swing.JProgressBar();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(0, 51, 255));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("COST CENERWISE ISSUE & SUPPLIERWISE GRN");
        jLabel1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setOpaque(true);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(1, 3, 479, 27);

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12));
        lblStatus.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(0, 260, 470, 22);

        jPanel1.setLayout(null);

        jPanel1.setBorder(new javax.swing.border.EtchedBorder());
        jLabel3.setText("From Date :");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(40, 10, 80, 21);

        txtFromDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtFromDateFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFromDateFocusLost(evt);
            }
        });

        jPanel1.add(txtFromDate);
        txtFromDate.setBounds(120, 10, 114, 21);

        jLabel2.setText("To");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(240, 10, 25, 21);

        txtToDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtToDateFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtToDateFocusLost(evt);
            }
        });

        jPanel1.add(txtToDate);
        txtToDate.setBounds(270, 10, 114, 21);

        jTabbedPane1.setBackground(new java.awt.Color(204, 204, 204));
        PanelIssue.setLayout(null);

        jLabel4.setText("Cost Centers ID :");
        PanelIssue.add(jLabel4);
        jLabel4.setBounds(10, 20, 110, 21);

        txtCostCenter.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCostCenterFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCostCenterFocusLost(evt);
            }
        });
        txtCostCenter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCostCenterKeyPressed(evt);
            }
        });

        PanelIssue.add(txtCostCenter);
        txtCostCenter.setBounds(120, 20, 80, 21);

        lblCostCenterName.setText("...");
        PanelIssue.add(lblCostCenterName);
        lblCostCenterName.setBounds(210, 20, 190, 21);

        cmdPreview.setText("Preview");
        cmdPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewActionPerformed(evt);
            }
        });

        PanelIssue.add(cmdPreview);
        cmdPreview.setBounds(290, 100, 90, 28);

        jProgressBar1.setForeground(new java.awt.Color(153, 153, 255));
        jProgressBar1.setStringPainted(true);
        jProgressBar1.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        PanelIssue.add(jProgressBar1);
        jProgressBar1.setBounds(10, 60, 380, 20);

        jTabbedPane1.addTab("Issue", PanelIssue);

        PanelGrn.setLayout(null);

        jLabel5.setText("Supplier Code :");
        PanelGrn.add(jLabel5);
        jLabel5.setBounds(20, 20, 100, 20);

        txtSupplierCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSupplierCodeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSupplierCodeFocusLost(evt);
            }
        });
        txtSupplierCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSupplierCodeKeyPressed(evt);
            }
        });

        PanelGrn.add(txtSupplierCode);
        txtSupplierCode.setBounds(130, 20, 100, 20);

        PanelGrn.add(lblSupplierName);
        lblSupplierName.setBounds(240, 20, 160, 20);

        cmdPreviewGRN.setText("Preview");
        cmdPreviewGRN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewGRNActionPerformed(evt);
            }
        });

        PanelGrn.add(cmdPreviewGRN);
        cmdPreviewGRN.setBounds(310, 100, 82, 25);

        jProgressBar2.setForeground(new java.awt.Color(153, 153, 255));
        jProgressBar2.setStringPainted(true);
        jProgressBar2.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        PanelGrn.add(jProgressBar2);
        jProgressBar2.setBounds(10, 60, 380, 20);

        jTabbedPane1.addTab("GRN", PanelGrn);

        jPanel1.add(jTabbedPane1);
        jTabbedPane1.setBounds(20, 40, 410, 160);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(10, 40, 450, 210);

    }//GEN-END:initComponents
    
    private void txtToDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToDateFocusLost
        // TODO add your handling code here:
        lblStatus.setText("");
    }//GEN-LAST:event_txtToDateFocusLost
    
    private void txtFromDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromDateFocusLost
        // TODO add your handling code here:
        lblStatus.setText("");
    }//GEN-LAST:event_txtFromDateFocusLost
    
    private void txtToDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToDateFocusGained
        // TODO add your handling code here:
        lblStatus.setText("Please Enter To Date...(dd/mm/yyyy)");
    }//GEN-LAST:event_txtToDateFocusGained
    
    private void txtFromDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromDateFocusGained
        // TODO add your handling code here:
        lblStatus.setText("Please Enter From Date...(dd/mm/yyyy)");
    }//GEN-LAST:event_txtFromDateFocusGained
    
    private void txtSupplierCodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSupplierCodeFocusGained
        // TODO add your handling code here:
        lblStatus.setText("Press F1 for Supplier List...");
    }//GEN-LAST:event_txtSupplierCodeFocusGained
    
    private void txtCostCenterFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCostCenterFocusGained
        // TODO add your handling code here:
        lblStatus.setText("Press F1 for CostCenter List...");
    }//GEN-LAST:event_txtCostCenterFocusGained
    
    private void txtSupplierCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSupplierCodeKeyPressed
        // TODO add your handling code here:
        
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();
            
            aList.SQL="SELECT SUPPLIER_CODE,SUPP_NAME FROM D_COM_SUPP_MASTER WHERE COMPANY_ID='" + EITLERPGLOBAL.gCompanyID + "' AND SUPPLIER_CODE <> ''  ORDER BY SUPPLIER_CODE" ;
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=2;
            
            if(aList.ShowLOV()) {
                txtSupplierCode.setText(aList.ReturnVal);
                lblSupplierName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID,txtSupplierCode.getText().trim()));
            }
        }
        
    }//GEN-LAST:event_txtSupplierCodeKeyPressed
    
    private void txtCostCenterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostCenterKeyPressed
        // TODO add your handling code here:
        
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();
            
            aList.SQL="SELECT COST_CENTER_ID,COST_CENTER_NAME FROM D_COM_COST_CENTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" ORDER BY COST_CENTER_ID ";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=2;
            
            if(aList.ShowLOV()) {
                txtCostCenter.setText(aList.ReturnVal);
                lblCostCenterName.setText(clsCostCenter.getCostCenterName(EITLERPGLOBAL.gCompanyID,Integer.parseInt(txtCostCenter.getText())));
            }
        }
        
    }//GEN-LAST:event_txtCostCenterKeyPressed
    
    private void txtSupplierCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSupplierCodeFocusLost
        // TODO add your handling code here:
        if(!txtSupplierCode.getText().trim().equals("")) {
            SupplierName = clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID,txtSupplierCode.getText().trim());
            if(SupplierName.equals("")) {
                JOptionPane.showMessageDialog(null,"Invalid Supplier Id");
                return;
            }
            else {
                lblSupplierName.setText(SupplierName);
            }
        }
        lblStatus.setText("");
    }//GEN-LAST:event_txtSupplierCodeFocusLost
    
    private void cmdPreviewGRNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewGRNActionPerformed
        // TODO add your handling code here:
        if(txtFromDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter from date");
            return;
        }
        
        if(!EITLERPGLOBAL.isDate(txtFromDate.getText())) {
            JOptionPane.showMessageDialog(null,"Invalid From Date");
            return;
        }
        
        if(txtToDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter to date");
            return;
        }
        if(!EITLERPGLOBAL.isDate(txtToDate.getText())) {
            JOptionPane.showMessageDialog(null,"Invalid To Date");
            return;
        }
        PreviewReportGRN();
    }//GEN-LAST:event_cmdPreviewGRNActionPerformed
    
    private void txtCostCenterFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCostCenterFocusLost
        // TODO add your handling code here:
        if(!txtCostCenter.getText().equals("")) {
            CostCenterName = clsCostCenter.getCostCenterName(EITLERPGLOBAL.gCompanyID,Integer.parseInt(txtCostCenter.getText()));
            if(CostCenterName.trim().equals("")) {
                JOptionPane.showMessageDialog(null,"Invalid Cost Center ID");
                return;
            }
            else {
                lblCostCenterName.setText(CostCenterName);
            }
        }
        lblStatus.setText("");
    }//GEN-LAST:event_txtCostCenterFocusLost
    
    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        // TODO add your handling code here:
        if(txtFromDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter from date");
            return;
        }
        
        if(!EITLERPGLOBAL.isDate(txtFromDate.getText())) {
            JOptionPane.showMessageDialog(null,"Invalid From Date");
            return;
        }
        
        if(txtToDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter to date");
            return;
        }
        if(!EITLERPGLOBAL.isDate(txtToDate.getText())) {
            JOptionPane.showMessageDialog(null,"Invalid To Date");
            return;
        }
        if(!clsCostCenter.IsValidCostCenterCode(EITLERPGLOBAL.gCompanyID,Integer.parseInt(txtCostCenter.getText().trim()))) {
            JOptionPane.showMessageDialog(null,"Invalid Cost Center ID");
            return;
        }
        PreviewReport();
    }//GEN-LAST:event_cmdPreviewActionPerformed
    
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_formMouseClicked
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelGrn;
    private javax.swing.JPanel PanelIssue;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdPreviewGRN;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JProgressBar jProgressBar2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblCostCenterName;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblSupplierName;
    private javax.swing.JTextField txtCostCenter;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtSupplierCode;
    private javax.swing.JTextField txtToDate;
    // End of variables declaration//GEN-END:variables
    
    private void PreviewReportGRN() {
        
        new Thread() {
            
            public void run() {
                
                jProgressBar2.setVisible(true);
                
                
                try{
                    TReportWriter.SimpleDataProvider.TRow objRow;
                    TReportWriter.SimpleDataProvider.TTable objReportData=new TReportWriter.SimpleDataProvider.TTable();
                    
                    
                    objReportData.AddColumn("SR_NO");
                    objReportData.AddColumn("GRN_NO");
                    objReportData.AddColumn("GRN_DATE");
                    objReportData.AddColumn("ITEM_ID");
                    objReportData.AddColumn("ITEM_DESC");
                    objReportData.AddColumn("QTY");
                    objReportData.AddColumn("RATE");
                    objReportData.AddColumn("VALUE");
                    
                    TReportWriter.SimpleDataProvider.TRow objOpeningRow=objReportData.newRow();
                    
                    
                    objOpeningRow.setValue("SR_NO","");
                    objOpeningRow.setValue("GRN_NO","");
                    objOpeningRow.setValue("GRN-DATE","");
                    objOpeningRow.setValue("ITEM_ID","");
                    objOpeningRow.setValue("ITEM_DESC","");
                    objOpeningRow.setValue("QTY","");
                    objOpeningRow.setValue("RATE","");
                    objOpeningRow.setValue("VALUE","");
                    
                    
                    
                    String Qry="SELECT A.GRN_NO,A.GRN_DATE,B.ITEM_ID,C.LOT_ACCEPTED_QTY,B.LANDED_RATE AS RATE, "+
                    "C.LOT_ACCEPTED_QTY * B.LANDED_RATE AS VALUE "+
                    "FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B,D_INV_GRN_LOT C "+
                    "WHERE A.COMPANY_ID = " + EITLERPGLOBAL.gCompanyID + " AND A.GRN_NO = B.GRN_NO  "+
                    "AND A.GRN_NO = C.GRN_NO AND B.ITEM_ID = C.ITEM_ID AND B.SR_NO = C.GRN_SR_NO "+
                    "AND A.APPROVED =1 AND A.CANCELLED = 0 "+
                    "AND A.GRN_DATE>='" + EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim()) + "' AND A.GRN_DATE<='" + EITLERPGLOBAL.formatDateDB(txtToDate.getText().trim()) + "'  "+
                    "AND A.SUPP_ID = '" + txtSupplierCode.getText().trim() + "'" +
                    "ORDER BY B.ITEM_ID,A.GRN_NO";
                    
                    ResultSet rsGRN = data.getResult(Qry);
                    rsGRN.last();
                    jProgressBar2.setMinimum(0);
                    jProgressBar2.setMaximum(rsGRN.getRow());
                    rsGRN.first();
                    int Counter = 1;
                    while(rsGRN.getRow()>0 && !rsGRN.isAfterLast()) {
                        
                        objRow=objReportData.newRow();
                        
                        objRow.setValue("SR_NO",String.valueOf(Counter));
                        objRow.setValue("GRN_NO",rsGRN.getString("GRN_NO"));
                        objRow.setValue("GRN_DATE",EITLERPGLOBAL.formatDate(rsGRN.getString("GRN_DATE")));
                        objRow.setValue("ITEM_ID",rsGRN.getString("ITEM_ID"));
                        objRow.setValue("ITEM_NAME",clsItem.getItemName(EITLERPGLOBAL.gCompanyID,rsGRN.getString("ITEM_ID")));
                        objRow.setValue("QTY",rsGRN.getString("LOT_ACCEPTED_QTY"));
                        objRow.setValue("RATE",rsGRN.getString("RATE"));
                        objRow.setValue("VALUE",String.valueOf(EITLERPGLOBAL.round(rsGRN.getDouble("LOT_ACCEPTED_QTY")*rsGRN.getDouble("RATE"),3) ));
                        
                        objReportData.AddRow(objRow);
                        
                        Counter++;
                        jProgressBar2.setValue(Counter);
                        jProgressBar2.repaint();
                        Thread.sleep(100);
                        rsGRN.next();
                    }
                    
                    HashMap Parameters=new HashMap();
                    Parameters.put("COMPANY_ID",EITLERPGLOBAL.gCompanyID); 
                    String City="";
                    
                    if(EITLERPGLOBAL.gCompanyID==2) {
                        City="BARODA";
                    }
                    else {
                        City="ANKLESHWAR";
                    }
                    
                    Parameters.put("CITY",City);
                    Parameters.put("SYS_DATE","0000-00-00");
                    Parameters.put("SUPP_ID",txtSupplierCode.getText().trim());
                    Parameters.put("SUPP_NAME",SupplierName);
                    Parameters.put("FROM_DATE",txtFromDate.getText().trim());
                    Parameters.put("TO_DATE",txtToDate.getText().trim());
                    Parameters.put("RUN_DATE",EITLERPGLOBAL.getCurrentDate());
                    
                    objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptSupplierGrn.rpt",Parameters,objReportData);
                    
                    
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            };
        }.start();
    }
    
    private void PreviewReport() {
        
        new Thread() {
            
            public void run() {
                
                try{
                    
                    jProgressBar1.setVisible(true);
                    
                    jProgressBar1.setStringPainted(true);
                    
                    TReportWriter.SimpleDataProvider.TRow objRow;
                    TReportWriter.SimpleDataProvider.TTable objReportData=new TReportWriter.SimpleDataProvider.TTable();
                    
                    
                    objReportData.AddColumn("SR_NO");
                    objReportData.AddColumn("ISSUE_NO");
                    objReportData.AddColumn("ISSUE_DATE");
                    objReportData.AddColumn("ITEM_ID");
                    objReportData.AddColumn("ITEM_DESC");
                    objReportData.AddColumn("QTY");
                    objReportData.AddColumn("RATE");
                    objReportData.AddColumn("VALUE");
                    
                    TReportWriter.SimpleDataProvider.TRow objOpeningRow=objReportData.newRow();
                    
                    
                    objOpeningRow.setValue("SR_NO","");
                    objOpeningRow.setValue("ISSUE_NO","");
                    objOpeningRow.setValue("ISSUE-DATE","");
                    objOpeningRow.setValue("ITEM_ID","");
                    objOpeningRow.setValue("ITEM_DESC","");
                    objOpeningRow.setValue("QTY","");
                    objOpeningRow.setValue("RATE","");
                    objOpeningRow.setValue("VALUE","");
                    
                    ResultSet rsRate;
                    //======= Find the last cut-off date stock entry =================//
                    ResultSet rsTmp=data.getResult("SELECT ENTRY_NO,ENTRY_DATE FROM D_COM_OPENING_STOCK_HEADER WHERE ENTRY_DATE<='"+EITLERPGLOBAL.formatDateDB(txtFromDate.getText())+"' ORDER BY ENTRY_DATE DESC");
                    rsTmp.first();
                    long StockEntryNo=0;
                    String StockEntryDate="";
                    if(rsTmp.getRow()>0) {
                        StockEntryNo=rsTmp.getLong("ENTRY_NO");
                        StockEntryDate=rsTmp.getString("ENTRY_DATE");
                    }
                    //================================================================//
                    
                    jProgressBar1.setMinimum(0);
                    
                    
                    
                    String Qry="SELECT A.ISSUE_NO,A.ISSUE_DATE,B.ITEM_CODE,C.AUTO_LOT_NO,C.ISSUED_LOT_QTY FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B,D_INV_ISSUE_LOT C "+
                    "WHERE A.ISSUE_NO = B.ISSUE_NO AND A.ISSUE_NO = C.ISSUE_NO AND B.ITEM_CODE = C.ITEM_ID AND B.SR_NO = C.ISSUE_SR_NO AND A.APPROVED =1 AND A.CANCELED = 0 "+
                    "AND A.COMPANY_ID='" + EITLERPGLOBAL.gCompanyID + "' AND B.COST_CENTER_ID = '"+txtCostCenter.getText().trim()+"' "+
                    "AND A.ISSUE_DATE>='" + EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim()) + "' AND A.ISSUE_DATE<='" + EITLERPGLOBAL.formatDateDB(txtToDate.getText().trim()) + "' "+
                    "ORDER BY B.ITEM_CODE,A.ISSUE_NO ";
                    ResultSet rsIssue = data.getResult(Qry);
                    rsIssue.last();
                    int test = rsIssue.getRow();
                    jProgressBar1.setMaximum(rsIssue.getRow());
                    rsIssue.first();
                    int Counter = 1;
                    while(rsIssue.getRow()>0 && !rsIssue.isAfterLast()) {
                        
                        Qry="SELECT A.LANDED_RATE AS RATE FROM D_INV_GRN_DETAIL A,D_INV_GRN_LOT B "+
                        "WHERE A.GRN_NO = B.GRN_NO AND A.SR_NO = B.GRN_SR_NO  "+
                        "AND B.AUTO_LOT_NO = '" + rsIssue.getString("AUTO_LOT_NO") + "' AND B.ITEM_ID ='" + rsIssue.getString("ITEM_CODE") + "' "+
                        "UNION "+
                        "SELECT OPENING_RATE AS RATE FROM D_COM_OPENING_STOCK_DETAIL  "+
                        "WHERE ENTRY_NO = '" + StockEntryNo + "' AND LOT_NO ='" + rsIssue.getString("AUTO_LOT_NO") + "' AND ITEM_ID = '" + rsIssue.getString("ITEM_CODE") + "' ";
                        
                        rsRate = data.getResult(Qry);
                        
                        objRow=objReportData.newRow();
                        
                        objRow.setValue("SR_NO",String.valueOf(Counter));
                        objRow.setValue("ISSUE_NO",rsIssue.getString("ISSUE_NO"));
                        objRow.setValue("ISSUE_DATE",EITLERPGLOBAL.formatDate(rsIssue.getString("ISSUE_DATE")));
                        objRow.setValue("ITEM_CODE",rsIssue.getString("ITEM_CODE"));
                        objRow.setValue("ITEM_DESC",clsItem.getItemName(EITLERPGLOBAL.gCompanyID,rsIssue.getString("ITEM_CODE")));
                        objRow.setValue("QTY",rsIssue.getString("ISSUED_LOT_QTY"));
                        objRow.setValue("RATE",rsRate.getString("RATE"));
                        objRow.setValue("VALUE",String.valueOf(EITLERPGLOBAL.round(rsIssue.getDouble("ISSUED_LOT_QTY")*rsRate.getDouble("RATE"),3) ));
                        
                        objReportData.AddRow(objRow);
                        
                        Counter++;
                        jProgressBar1.setValue(Counter);
                        jProgressBar1.repaint();
                        Thread.sleep(100);
                        rsIssue.next();
                    }
                    
                    HashMap Parameters=new HashMap();
                    Parameters.put("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    String City="";
                    
                    if(EITLERPGLOBAL.gCompanyID==2) {
                        City="BARODA";
                    }
                    else {
                        City="ANKLESHWAR";
                    }
                    
                    Parameters.put("CITY",City);
                    Parameters.put("SYS_DATE","0000-00-00");
                    Parameters.put("COST_CENTER_ID",txtCostCenter.getText().trim());
                    Parameters.put("COST_CENTER_NAME",CostCenterName);
                    Parameters.put("FROM_DATE",txtFromDate.getText().trim());
                    Parameters.put("TO_DATE",txtToDate.getText().trim());
                    Parameters.put("RUN_DATE",EITLERPGLOBAL.getCurrentDate());
                    
                    objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptCostCenterIssueReport.rpt",Parameters,objReportData);
                    
                    
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
                jProgressBar1.setVisible(false);
            };
        }.start();
        
    }
}

