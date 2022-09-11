/*
 * frmPaymentDetails.java
 *
 * Created on August 17, 2007, 3:58 PM
 */

package EITLERP.Finance.ReportsUI;

/**
 *
 * @author  root
 */
import javax.swing.*;
import java.awt.*;
import EITLERP.*;
import java.util.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.event.*;
import java.net.*;
import EITLERP.Utils.*;
import java.io.*;
import EITLERP.Purchase.*;
import EITLERP.Stores.*;
import java.sql.*;
import EITLERP.Finance.*;

public class frmPayables extends javax.swing.JApplet {
    
    private EITLTableModel DataModelPJV=new EITLTableModel();
    private EITLTableModel DataModelPayment=new EITLTableModel();
    private EITLTableCellRenderer RendererPJV=new EITLTableCellRenderer();
    private EITLTableCellRenderer RendererPayment=new EITLTableCellRenderer();
    private EITLComboModel cmbCompanyModel=new EITLComboModel();
    
    private JDialog aDialog;
    public String PartyCode="";
    public String PONo="";
    
    
    /** Initializes the applet frmPaymentDetails */
    public void init() {
        setSize(715,527);
        initComponents();
        
        txtFromDate.setText(EITLERPGLOBAL.FinFromDate);
        txtToDate.setText(EITLERPGLOBAL.FinToDate);
        GenerateCombo();
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        lblTitle = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtPartyCode = new javax.swing.JTextField();
        txtPartyName = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtGRNNo = new javax.swing.JTextField();
        cmdOpenVoucher = new javax.swing.JButton();
        cmdShowPO = new javax.swing.JButton();
        cmdClose = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtToDate = new javax.swing.JTextField();
        Tab = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        TablePJV = new javax.swing.JTable();
        cmdNext = new javax.swing.JButton();
        cmdShowPJV = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        TablePayment = new javax.swing.JTable();
        cmdShowPayment = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmdDisplayAmount = new javax.swing.JButton();
        lblApprovedPayable = new javax.swing.JLabel();
        lblUnApprovedPayable = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        cmbCompany = new javax.swing.JComboBox();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setForeground(java.awt.Color.white);
        lblTitle.setText(" PARTY PAYABLES INFORMATION");
        lblTitle.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(1, 2, 780, 25);

        jLabel15.setText("Party Code");
        getContentPane().add(jLabel15);
        jLabel15.setBounds(11, 40, 80, 15);

        txtPartyCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPartyCodeFocusLost(evt);
            }
        });
        txtPartyCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPartyCodeKeyPressed(evt);
            }
        });

        getContentPane().add(txtPartyCode);
        txtPartyCode.setBounds(85, 38, 110, 19);

        txtPartyName.setText("...");
        getContentPane().add(txtPartyName);
        txtPartyName.setBounds(87, 63, 370, 15);

        jLabel17.setText("GRN No.");
        getContentPane().add(jLabel17);
        jLabel17.setBounds(28, 91, 50, 15);

        getContentPane().add(txtGRNNo);
        txtGRNNo.setBounds(84, 88, 110, 19);

        cmdOpenVoucher.setText("Open Voucher");
        cmdOpenVoucher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOpenVoucherActionPerformed(evt);
            }
        });

        getContentPane().add(cmdOpenVoucher);
        cmdOpenVoucher.setBounds(14, 469, 130, 25);

        cmdShowPO.setText("...");
        cmdShowPO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowPOActionPerformed(evt);
            }
        });

        getContentPane().add(cmdShowPO);
        cmdShowPO.setBounds(198, 88, 30, 20);

        cmdClose.setText("Close");
        cmdClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCloseActionPerformed(evt);
            }
        });

        getContentPane().add(cmdClose);
        cmdClose.setBounds(542, 472, 130, 25);

        jLabel18.setText("From Date");
        getContentPane().add(jLabel18);
        jLabel18.setBounds(8, 127, 70, 15);

        getContentPane().add(txtFromDate);
        txtFromDate.setBounds(84, 124, 110, 19);

        jLabel19.setText("To");
        getContentPane().add(jLabel19);
        jLabel19.setBounds(206, 126, 30, 15);

        getContentPane().add(txtToDate);
        txtToDate.setBounds(235, 124, 110, 19);

        jPanel1.setLayout(null);

        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setLayout(null);

        jPanel4.setBorder(new javax.swing.border.EtchedBorder());
        TablePJV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(TablePJV);

        jPanel4.add(jScrollPane5);
        jScrollPane5.setBounds(5, 5, 630, 200);

        jPanel1.add(jPanel4);
        jPanel4.setBounds(6, 34, 640, 210);

        cmdNext.setText("Next >>");
        cmdNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNextActionPerformed(evt);
            }
        });

        jPanel1.add(cmdNext);
        cmdNext.setBounds(558, 250, 88, 25);

        cmdShowPJV.setText("Show");
        cmdShowPJV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowPJVActionPerformed(evt);
            }
        });

        jPanel1.add(cmdShowPJV);
        cmdShowPJV.setBounds(8, 8, 110, 20);

        Tab.addTab("PJV List", jPanel1);

        jPanel2.setLayout(null);

        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.setLayout(null);

        jPanel5.setBorder(new javax.swing.border.EtchedBorder());
        TablePayment.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane6.setViewportView(TablePayment);

        jPanel5.add(jScrollPane6);
        jScrollPane6.setBounds(5, 5, 630, 220);

        jPanel2.add(jPanel5);
        jPanel5.setBounds(4, 37, 640, 230);

        cmdShowPayment.setText("Show");
        cmdShowPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowPaymentActionPerformed(evt);
            }
        });

        jPanel2.add(cmdShowPayment);
        cmdShowPayment.setBounds(8, 8, 110, 20);

        Tab.addTab("Payment Voucher List", jPanel2);

        jPanel3.setLayout(null);

        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        jLabel1.setText("Total amount payable to the party");
        jPanel3.add(jLabel1);
        jLabel1.setBounds(12, 15, 230, 15);

        cmdDisplayAmount.setText("Click to Display");
        cmdDisplayAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDisplayAmountActionPerformed(evt);
            }
        });

        jPanel3.add(cmdDisplayAmount);
        cmdDisplayAmount.setBounds(244, 11, 140, 25);

        lblApprovedPayable.setFont(new java.awt.Font("Dialog", 1, 14));
        lblApprovedPayable.setForeground(new java.awt.Color(51, 51, 255));
        lblApprovedPayable.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblApprovedPayable.setText("....");
        jPanel3.add(lblApprovedPayable);
        lblApprovedPayable.setBounds(16, 59, 160, 17);

        lblUnApprovedPayable.setFont(new java.awt.Font("Dialog", 1, 14));
        lblUnApprovedPayable.setForeground(new java.awt.Color(153, 153, 153));
        lblUnApprovedPayable.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUnApprovedPayable.setText("....");
        jPanel3.add(lblUnApprovedPayable);
        lblUnApprovedPayable.setBounds(16, 96, 160, 17);

        Tab.addTab("Total Payable", jPanel3);

        getContentPane().add(Tab);
        Tab.setBounds(12, 156, 660, 310);

        jLabel21.setText("GRN Company ");
        getContentPane().add(jLabel21);
        jLabel21.setBounds(246, 91, 100, 15);

        getContentPane().add(cmbCompany);
        cmbCompany.setBounds(350, 89, 260, 20);

    }//GEN-END:initComponents
    
    private void cmdDisplayAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDisplayAmountActionPerformed
        // TODO add your handling code here:
        try {
            lblApprovedPayable.setText("Rs. "+clsAccount.getPartyPayableAmount(EITLERPGLOBAL.gCompanyID, txtPartyCode.getText(), txtGRNNo.getText(), EITLERPGLOBAL.formatDateDB(txtFromDate.getText()), EITLERPGLOBAL.formatDateDB(txtToDate.getText())));
            lblUnApprovedPayable.setText("Rs. "+clsAccount.getPartyPayableAmountAll(EITLERPGLOBAL.gCompanyID, txtPartyCode.getText(), txtGRNNo.getText(), EITLERPGLOBAL.formatDateDB(txtFromDate.getText()), EITLERPGLOBAL.formatDateDB(txtToDate.getText())));
        }
        catch(Exception e) {
            
        }
    }//GEN-LAST:event_cmdDisplayAmountActionPerformed
    
    private void cmdNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextActionPerformed
        // TODO add your handling code here:
        try {
            if(TablePJV.getSelectedRow()>=0) {
                txtGRNNo.setText(DataModelPJV.getValueByVariable("GRN_NO",TablePJV.getSelectedRow()));
                EITLERPGLOBAL.setComboIndex(cmbCompany,UtilFunctions.CInt(DataModelPJV.getValueByVariable("COMPANY_ID",TablePJV.getSelectedRow())));
                
                GenerateGridPayment();
                Tab.setSelectedIndex(1);
            }
        }
        catch(Exception e) {
            
        }
    }//GEN-LAST:event_cmdNextActionPerformed
    
    private void cmdShowPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowPaymentActionPerformed
        // TODO add your handling code here:
        GenerateGridPayment();
    }//GEN-LAST:event_cmdShowPaymentActionPerformed
    
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseClicked
    
    private void cmdCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCloseActionPerformed
        // TODO add your handling code here:
        try {
            ((JFrame)getParent().getParent().getParent().getParent()).dispose();
        }
        catch(Exception e) {
            
        }
        
        try {
            aDialog.dispose();
        }
        catch(Exception e) {
            
        }
        
        
    }//GEN-LAST:event_cmdCloseActionPerformed
    
    private void cmdOpenVoucherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdOpenVoucherActionPerformed
        // TODO add your handling code here:
        try {
            
            if(Tab.getSelectedIndex()==0) {
                
                if(TablePJV.getSelectedRow()>=0) {
                    String DocNo=DataModelPJV.getValueByVariable("VOUCHER_NO", TablePJV.getSelectedRow());
                    AppletFrame aFrame=new AppletFrame("Voucher");
                    aFrame.startAppletEx("EITLERP.Finance.frmVoucher","Voucher");
                    frmVoucher ObjDoc=(frmVoucher) aFrame.ObjApplet;
                    int CompanyID=UtilFunctions.CInt(DataModelPJV.getValueByVariable("COMPANY_ID", TablePJV.getSelectedRow()));
                    ObjDoc.FindEx(CompanyID,DocNo);
                }
                
            }
            else {
                
                if(TablePayment.getSelectedRow()>=0) {
                    String DocNo=DataModelPayment.getValueByVariable("VOUCHER_NO", TablePayment.getSelectedRow());
                    AppletFrame aFrame=new AppletFrame("Voucher");
                    aFrame.startAppletEx("EITLERP.Finance.frmVoucher","Voucher");
                    frmVoucher ObjDoc=(frmVoucher) aFrame.ObjApplet;
                    int CompanyID=UtilFunctions.CInt(DataModelPayment.getValueByVariable("COMPANY_ID", TablePayment.getSelectedRow()));
                    ObjDoc.FindEx(CompanyID,DocNo);
                }
            }
        }
        catch(Exception e) {
            
        }
        
    }//GEN-LAST:event_cmdOpenVoucherActionPerformed
    
    private void cmdShowPOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowPOActionPerformed
        // TODO add your handling code here:
        try {
            if(!txtGRNNo.getText().trim().equals("")) {
                String DocNo=txtGRNNo.getText();
                AppletFrame aFrame=new AppletFrame("GRN");
                aFrame.startAppletEx("EITLERP.Stores.frmGRNGen","GRN");
                frmGRNGen ObjDoc=(frmGRNGen) aFrame.ObjApplet;
                ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
            }
        }
        catch(Exception e) {
            
        }
        
    }//GEN-LAST:event_cmdShowPOActionPerformed
    
    private void txtPartyCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartyCodeFocusLost
        // TODO add your handling code here:
        txtPartyName.setText(clsPartyMaster.getAccountName("",txtPartyCode.getText()));
    }//GEN-LAST:event_txtPartyCodeFocusLost
    
    private void txtPartyCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyCodeKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==112) {
            LOV aList=new LOV();
            
            aList.SQL="SELECT PARTY_CODE,PARTY_NAME FROM D_FIN_PARTY_MASTER WHERE APPROVED=1 ORDER BY PARTY_NAME";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=2;
            aList.UseSpecifiedConn=true;
            aList.dbURL=FinanceGlobal.FinURL;
            
            if(aList.ShowLOV()) {
                txtPartyCode.setText(aList.ReturnVal);
                txtPartyName.setText(clsPartyMaster.getAccountName("",txtPartyCode.getText()));
            }
            
        }
    }//GEN-LAST:event_txtPartyCodeKeyPressed
    
    private void cmdShowPJVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowPJVActionPerformed
        // TODO add your handling code here:
        GenerateGridPJV();
        
        try {
            lblApprovedPayable.setText("Rs. "+clsAccount.getPartyPayableAmount(EITLERPGLOBAL.gCompanyID, txtPartyCode.getText(), txtGRNNo.getText(), EITLERPGLOBAL.formatDateDB(txtFromDate.getText()), EITLERPGLOBAL.formatDateDB(txtToDate.getText())));
            lblUnApprovedPayable.setText("Rs. "+clsAccount.getPartyPayableAmountAll(EITLERPGLOBAL.gCompanyID, txtPartyCode.getText(), txtGRNNo.getText(), EITLERPGLOBAL.formatDateDB(txtFromDate.getText()), EITLERPGLOBAL.formatDateDB(txtToDate.getText())));
        }
        catch(Exception e) {
            
        }
    }//GEN-LAST:event_cmdShowPJVActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane Tab;
    private javax.swing.JTable TablePJV;
    private javax.swing.JTable TablePayment;
    private javax.swing.JComboBox cmbCompany;
    private javax.swing.JButton cmdClose;
    private javax.swing.JButton cmdDisplayAmount;
    private javax.swing.JButton cmdNext;
    private javax.swing.JButton cmdOpenVoucher;
    private javax.swing.JButton cmdShowPJV;
    private javax.swing.JButton cmdShowPO;
    private javax.swing.JButton cmdShowPayment;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JLabel lblApprovedPayable;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUnApprovedPayable;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtGRNNo;
    private javax.swing.JTextField txtPartyCode;
    private javax.swing.JLabel txtPartyName;
    private javax.swing.JTextField txtToDate;
    // End of variables declaration//GEN-END:variables
    
    private void FormatGridPJV() {
        try {
            
            DataModelPJV=new EITLTableModel();
            TablePJV.removeAll();
            
            TablePJV.setModel(DataModelPJV);
            TableColumnModel ColModel=TablePJV.getColumnModel();
            TablePJV.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            
            DataModelPJV.addColumn("Sr."); //0 - Read Only
            DataModelPJV.addColumn("Voucher No."); //1
            DataModelPJV.addColumn("Voucher Date"); //2 //Read Only
            DataModelPJV.addColumn("Amount"); //3
            DataModelPJV.addColumn("Amount Paid"); //3
            DataModelPJV.addColumn("GRN No."); //4
            DataModelPJV.addColumn("GRN Date"); //5
            DataModelPJV.addColumn("PO No."); //6
            DataModelPJV.addColumn("PO Date"); //7
            DataModelPJV.addColumn("Remarks"); //8
            DataModelPJV.addColumn("Due Date"); //8
            DataModelPJV.addColumn("Company Name");
            DataModelPJV.addColumn("Company ID");
            
            DataModelPJV.SetVariable(0,"SR_NO"); //0 - Read Only
            DataModelPJV.SetVariable(1,"VOUCHER_NO"); //1
            DataModelPJV.SetVariable(2,"VOUCHER_DATE"); //2 //Read Only
            DataModelPJV.SetVariable(3,"AMOUNT"); //2 //Read Only
            DataModelPJV.SetVariable(4,"AMOUNT_PAID"); //2 //Read Only
            DataModelPJV.SetVariable(5,"GRN_NO"); //2 //Read Only
            DataModelPJV.SetVariable(6,"GRN_DATE"); //2 //Read Only
            DataModelPJV.SetVariable(7,"PO_NO"); //2 //Read Only
            DataModelPJV.SetVariable(8,"PO_DATE"); //2 //Read Only
            DataModelPJV.SetVariable(9,"REMARKS"); //2 //Read Only
            DataModelPJV.SetVariable(10,"DUE_DATE"); //2 //Read Only
            DataModelPJV.SetVariable(11,"COMPANY_NAME"); //2 //Read Only
            DataModelPJV.SetVariable(12,"COMPANY_ID"); //2 //Read Only
            
            DataModelPJV.TableReadOnly(true);
            
            TablePJV.getColumnModel().getColumn(DataModelPJV.getColFromVariable("VOUCHER_NO")).setCellRenderer(RendererPJV);
            TablePJV.getColumnModel().getColumn(DataModelPJV.getColFromVariable("PO_NO")).setCellRenderer(RendererPJV);
            
        }
        catch(Exception e) {
            
        }
    }
    
    
    private void FormatGridPayment() {
        try {
            
            DataModelPayment=new EITLTableModel();
            TablePayment.removeAll();
            
            TablePayment.setModel(DataModelPayment);
            TableColumnModel ColModel=TablePayment.getColumnModel();
            TablePayment.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            
            DataModelPayment.addColumn("Sr."); //0 - Read Only
            DataModelPayment.addColumn("Voucher No."); //1
            DataModelPayment.addColumn("Voucher Date"); //2 //Read Only
            DataModelPayment.addColumn("Amount"); //3
            DataModelPayment.addColumn("GRN No."); //4
            DataModelPayment.addColumn("GRN Date"); //5
            DataModelPayment.addColumn("PO No."); //6
            DataModelPayment.addColumn("PO Date"); //7
            DataModelPayment.addColumn("Remarks"); //8
            DataModelPayment.addColumn("Due Date"); //8
            DataModelPayment.addColumn("Company Name"); //8
            DataModelPayment.addColumn("Company ID"); //8
            
            DataModelPayment.SetVariable(0,"SR_NO"); //0 - Read Only
            DataModelPayment.SetVariable(1,"VOUCHER_NO"); //1
            DataModelPayment.SetVariable(2,"VOUCHER_DATE"); //2 //Read Only
            DataModelPayment.SetVariable(3,"AMOUNT"); //2 //Read Only
            DataModelPayment.SetVariable(4,"GRN_NO"); //2 //Read Only
            DataModelPayment.SetVariable(5,"GRN_DATE"); //2 //Read Only
            DataModelPayment.SetVariable(6,"PO_NO"); //2 //Read Only
            DataModelPayment.SetVariable(7,"PO_DATE"); //2 //Read Only
            DataModelPayment.SetVariable(8,"REMARKS"); //2 //Read Only
            DataModelPayment.SetVariable(9,"DUE_DATE"); //2 //Read Only
            DataModelPayment.SetVariable(10,"COMPANY_NAME"); //2 //Read Only
            DataModelPayment.SetVariable(11,"COMPANY_ID"); //2 //Read Only
            
            DataModelPayment.TableReadOnly(true);
            
            TablePayment.getColumnModel().getColumn(DataModelPayment.getColFromVariable("VOUCHER_NO")).setCellRenderer(RendererPayment);
        }
        catch(Exception e) {
            
        }
    }
    
    
    private void GenerateGridPJV() {
        try {
            
            RendererPJV.removeBackColors();
            RendererPJV.removeForeColors();
            
            HashMap List=new HashMap();
            
            FormatGridPJV();
            
            List=clsAccount.getPartyPayableDetail(EITLERPGLOBAL.gCompanyID, txtPartyCode.getText(), txtGRNNo.getText(), EITLERPGLOBAL.formatDateDB(txtFromDate.getText()), EITLERPGLOBAL.formatDateDB(txtToDate.getText()));
            
            for(int i=1;i<=List.size();i++) {
                clsVoucher objItem=(clsVoucher)List.get(Integer.toString(i));
                
                Object[] rowData=new Object[13];
                
                rowData[0]=Integer.toString(i);
                rowData[1]=objItem.getAttribute("VOUCHER_NO").getString();
                rowData[2]=objItem.getAttribute("VOUCHER_DATE").getString();
                rowData[3]=Double.toString(objItem.getAttribute("AMOUNT").getDouble());
                rowData[4]=Double.toString(objItem.getAttribute("AMOUNT_PAID").getDouble());
                rowData[5]=objItem.getAttribute("GRN_NO").getString();
                rowData[6]=objItem.getAttribute("GRN_DATE").getString();
                rowData[7]=objItem.getAttribute("PO_NO").getString();
                rowData[8]=objItem.getAttribute("PO_DATE").getString();
                rowData[9]=objItem.getAttribute("REMARKS").getString();
                rowData[10]=objItem.getAttribute("DUE_DATE").getString();
                rowData[11]=clsCompany.getCityName(objItem.getAttribute("COMPANY_ID").getInt());
                rowData[12]=Integer.toString(objItem.getAttribute("COMPANY_ID").getInt());
                
                DataModelPJV.addRow(rowData);
                
                if(objItem.getAttribute("APPROVED").getInt()==1) {
                    RendererPJV.setBackColor(TablePJV.getRowCount()-1, DataModelPJV.getColFromVariable("VOUCHER_NO"), Color.GREEN);
                }
                
                if(!objItem.getAttribute("PO_NO").getString().equals("")) {
                    if(objItem.getAttribute("FULLY_EXECUTED_PO").getInt()==1) {
                        RendererPJV.setBackColor(TablePJV.getRowCount()-1,DataModelPJV.getColFromVariable("PO_NO"),Color.GREEN);
                    }
                    else {
                        RendererPJV.setBackColor(TablePJV.getRowCount()-1,DataModelPJV.getColFromVariable("PO_NO"),Color.CYAN);
                    }
                }
                
            }
        }
        catch(Exception e) {
            
        }
    }
    
    
    private void GenerateGridPayment() {
        
        try {
            
            RendererPayment.removeBackColors();
            RendererPayment.removeForeColors();
            
            HashMap List=new HashMap();
            
            FormatGridPayment();
            
            int CompanyID=EITLERPGLOBAL.getComboCode(cmbCompany);
            
            List=clsAccount.getPartyPaymentDetail(CompanyID, txtPartyCode.getText(), txtGRNNo.getText(), EITLERPGLOBAL.formatDateDB(txtFromDate.getText()), EITLERPGLOBAL.formatDateDB(txtToDate.getText()));
            
            for(int i=1;i<=List.size();i++) {
                clsVoucher objItem=(clsVoucher)List.get(Integer.toString(i));
                
                Object[] rowData=new Object[12];
                
                rowData[0]=Integer.toString(i);
                rowData[1]=objItem.getAttribute("VOUCHER_NO").getString();
                rowData[2]=objItem.getAttribute("VOUCHER_DATE").getString();
                rowData[3]=Double.toString(objItem.getAttribute("AMOUNT").getDouble());
                rowData[4]=objItem.getAttribute("GRN_NO").getString();
                rowData[5]=objItem.getAttribute("GRN_DATE").getString();
                rowData[6]=objItem.getAttribute("PO_NO").getString();
                rowData[7]=objItem.getAttribute("PO_DATE").getString();
                rowData[8]=objItem.getAttribute("REMARKS").getString();
                rowData[9]=objItem.getAttribute("DUE_DATE").getString();
                rowData[10]=clsCompany.getCityName(objItem.getAttribute("COMPANY_ID").getInt());
                rowData[11]=Integer.toString(objItem.getAttribute("COMPANY_ID").getInt());
                
                if(objItem.getAttribute("APPROVED").getInt()==1) {
                    RendererPayment.setBackColor(TablePayment.getRowCount()-1, DataModelPayment.getColFromVariable("VOUCHER_NO"), Color.GREEN);
                }
                
                DataModelPayment.addRow(rowData);
            }
        }
        catch(Exception e) {
            
        }
    }
    
    private Frame findParentFrame(JApplet pApplet) {
        Container c = (Container) pApplet;
        while(c != null) {
            if (c instanceof Frame)
                return (Frame)c;
            
            c = c.getParent();
        }
        return (Frame)null;
    }
    
    
    public void ShowDialog() {
        try {
            setSize(715,527);
            
            Frame f=findParentFrame(this);
            
            initComponents();
            txtPartyCode.setText(this.PartyCode);
            txtPartyName.setText(clsPartyMaster.getAccountName("",txtPartyCode.getText()));
            
            txtFromDate.setText(EITLERPGLOBAL.FinFromDate);
            txtToDate.setText(EITLERPGLOBAL.FinToDate);
            
            GenerateGridPJV();
            
            aDialog=new JDialog(f,"Party Payable Details",true);
            
            aDialog.getContentPane().add("Center",this);
            Dimension appletSize = this.getSize();
            aDialog.setSize(appletSize);
            aDialog.setResizable(false);
            
            //Place it to center of the screen
            Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
            aDialog.setLocation((int)(screenSize.width-appletSize.getWidth())/2,(int)(screenSize.height-appletSize.getHeight())/2);
            
            aDialog.setDefaultCloseOperation(javax.swing.JDialog.DISPOSE_ON_CLOSE);
            aDialog.show();
        }
        catch(Exception e) {
        }
    }

    private void GenerateCombo()
    {
      try
      {
         HashMap List=clsCompany.getList("");
         
         cmbCompanyModel=new EITLComboModel();
         cmbCompany.removeAllItems();
         cmbCompany.setModel(cmbCompanyModel);
         
         for(int i=1;i<=List.size();i++)
         {
           clsCompany objCompany=(clsCompany)List.get(Integer.toString(i));
           
           ComboData objData=new ComboData();
           objData.Code=objCompany.getAttribute("COMPANY_ID").getInt();
           objData.Text=objCompany.getAttribute("COMPANY_NAME").getString();
           
           cmbCompanyModel.addElement(objData);
         }
      }
      catch(Exception e)
      {
          
      }
    }
    
}