/* frmFeltSalesInfo.java
 *
 *
 * Created on July 13, 2005, 10:47 AM
 */
package EITLERP.Suitings;

import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.sql.*;
import java.lang.*;
import java.io.File;

//import EITLERP.Production.ReportUI.*;

/**
 *
 * @author Rajpalsinh Jadeja
 */
public class frmFeltSuitingInfo extends javax.swing.JApplet {
    
    String SelUserID ="" ;
    String strSQL ="";
    
    
    private clsExcelExporter exp = new clsExcelExporter();
    private EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
    private EITLTableCellRenderer CellPainter = new EITLTableCellRenderer();
    private EITLTableCellRenderer CellPaint = new EITLTableCellRenderer();
    private EITLTableCellRenderer CellPainterA = new EITLTableCellRenderer();
    
    private EITLTableModel DataModelProductionProcess = new EITLTableModel();
    private EITLTableModel DataModelComplaint = new EITLTableModel();
    private EITLTableModel DataModelPartyWiseSummary = new EITLTableModel();
    //    private EITLTableModel DataModelOutStanding= new EITLTableModel();
    //    private EITLTableModel DataModelStgInvoice= new EITLTableModel();
    
    
    private EITLComboModel cmbOrderModel = new EITLComboModel();
    private EITLComboModel cmbBuyerModel = new EITLComboModel();
    private EITLComboModel cmbComplaintModel = new EITLComboModel();
    private EITLComboModel cmbPartyWiseSummaryModel = new EITLComboModel();
    //    private EITLComboModel cmbOutStandingModel = new EITLComboModel();
    //    private EITLComboModel cmbInvoiceModel = new EITLComboModel();
    
    public void init() {
        System.gc();
        setSize(800, 580);
        initComponents();
        GenerateCombo();
        FormatGridPartywiseMonthwiseProcess();
        FormatGridAgeWise();
        //        FormatGridOutStanding();
        //txtasondate.setText(EITLERPGLOBAL.getCurrentDate());
        txtasondate.setVisible(false);
        jLabel4.setVisible(false);
        FormatGridPartywiseSummary();
        //        FormatGridStgInvoice();
        
    }
    
    private void GenerateCombo() {
        cmbBuyerModel = new EITLComboModel();
        //cmbBuyer.removeAllItems();
        //cmbBuyer.setModel(cmbBuyerModel);
        
        HashMap List = new HashMap();
        List = (new clsUser()).getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID);
        
        for (int i = 1; i <= List.size(); i++) {
            clsUser ObjUser = (clsUser) List.get(Integer.toString(i));
            
            ComboData aData = new ComboData();
            aData.Code = (int) ObjUser.getAttribute("USER_ID").getVal();
            aData.Text = (String) ObjUser.getAttribute("USER_NAME").getObj();
            cmbBuyerModel.addElement(aData);
        }
        cmbOrderModel = new EITLComboModel();
        cmbOrder.removeAllItems();
        cmbOrder.setModel(cmbOrderModel);
        ComboData aData = new ComboData();
        
        aData.Code = 1;
        aData.Text = "PARTY CODE";
        cmbOrderModel.addElement(aData);
        
        aData.Code = 2;
        aData.Text = "PARTY NAME";
        cmbOrderModel.addElement(aData);
        
        aData.Code = 3;
        aData.Text = "DISPATCH STATION";
        cmbOrderModel.addElement(aData);
        
        aData.Code = 4;
        aData.Text = "CHARGE CODE";
        cmbOrderModel.addElement(aData);
        
        aData.Code = 5;
        aData.Text = "YEAR";
        cmbOrderModel.addElement(aData);
        
        aData.Code = 6;
        aData.Text = "MONTH";
        cmbOrderModel.addElement(aData);
        
        //AgeWise
        cmbComplaintModel = new EITLComboModel();
        cmbComplaint.removeAllItems();
        cmbComplaint.setModel(cmbComplaintModel);
        
        aData = new ComboData();
        aData.Code = 1;
        aData.Text = "PARTY CODE";
        cmbComplaintModel.addElement(aData);
        
        aData = new ComboData();
        aData.Code = 2;
        aData.Text = "PARTY NAME";
        cmbComplaintModel.addElement(aData);
        
        aData = new ComboData();
        aData.Code = 3;
        aData.Text = "CHARGE CODE";
        cmbComplaintModel.addElement(aData);
        
        
        cmbPartyWiseSummaryModel = new EITLComboModel();
        cmbPartySummary.removeAllItems();
        cmbPartySummary.setModel(cmbPartyWiseSummaryModel);
        
        aData = new ComboData();
        aData.Code = 1;
        aData.Text = "PARTY CODE";
        cmbPartyWiseSummaryModel.addElement(aData);
        
        aData = new ComboData();
        aData.Code = 2;
        aData.Text = "PARTY NAME";
        cmbPartyWiseSummaryModel.addElement(aData);
        
        aData = new ComboData();
        aData.Code = 3;
        aData.Text = "DISPATCH STATION";
        cmbPartyWiseSummaryModel.addElement(aData);
        
        aData = new ComboData();
        aData.Code = 4;
        aData.Text = "CHARGE CODE";
        cmbPartyWiseSummaryModel.addElement(aData);
        
        
        //        cmbOutStandingModel = new EITLComboModel();
        //        cmbOutStanding.removeAllItems();
        //        cmbOutStanding.setModel(cmbOutStandingModel);
        //
        //        aData = new ComboData();
        //        aData.Code = 1;
        //        aData.Text = "PARTY CODE";
        //        cmbOutStandingModel.addElement(aData);
        //
        //        aData = new ComboData();
        //        aData.Code = 2;
        //        aData.Text = "PARTY NAME";
        //        cmbOutStandingModel.addElement(aData);
        //
        //
        //        cmbInvoiceModel = new EITLComboModel();
        //        cmbInvoice.removeAllItems();
        //        cmbInvoice.setModel(cmbInvoiceModel);
        //
        //        aData = new ComboData();
        //        aData.Code = 1;
        //        aData.Text = "PARTY CODE";
        //        cmbInvoiceModel.addElement(aData);
        //
        //        aData = new ComboData();
        //        aData.Code = 2;
        //        aData.Text = "PARTY NAME";
        //        cmbInvoiceModel.addElement(aData);
        
        
        
    }
    
    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        lblStatus = new javax.swing.JLabel();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        ProductionProcessPanel = new javax.swing.JPanel();
        cmdShowProductionProcess = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableProductionProcess = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        cmbOrder = new javax.swing.JComboBox();
        cmdProductionProcessExporttoExcel = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane19 = new javax.swing.JScrollPane();
        TableComplaint = new javax.swing.JTable();
        cmdShowComplaint = new javax.swing.JButton();
        cmbComplaint = new javax.swing.JComboBox();
        jLabel30 = new javax.swing.JLabel();
        cmdComplaintExportToExcel = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        TablePartySummary = new javax.swing.JScrollPane();
        TablePartyWiseSummary = new javax.swing.JTable();
        cmdShowComplaint1 = new javax.swing.JButton();
        cmbPartySummary = new javax.swing.JComboBox();
        jLabel31 = new javax.swing.JLabel();
        cmdComplaintExportToExcel1 = new javax.swing.JButton();
        txtpartycode = new javax.swing.JTextField();
        txtpartyname = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtdatefrom = new javax.swing.JTextField();
        txtdateto = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        cmdClearAll = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        txtAgentCOde = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtasondate = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtChargeCode = new javax.swing.JTextField();

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12));
        lblStatus.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jMenu1.setText("jMenu1");
        jMenu2.setText("jMenu2");

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jPanel1.setLayout(null);

        jPanel1.setBackground(new java.awt.Color(0, 102, 255));
        jPanel1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setMinimumSize(new java.awt.Dimension(1000, 29));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Suiting Information System  ");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(10, 0, 360, 20);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 730, 30);

        jLabel2.setText("Party Code");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(30, 40, 67, 15);

        jTabbedPane1.setToolTipText("");
        jTabbedPane1.setAutoscrolls(true);
        jTabbedPane1.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseEntered(evt);
            }
        });

        ProductionProcessPanel.setLayout(null);

        ProductionProcessPanel.setBorder(new javax.swing.border.EtchedBorder());
        ProductionProcessPanel.setToolTipText("Production Process");
        ProductionProcessPanel.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        ProductionProcessPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ProductionProcessPanelMouseEntered(evt);
            }
        });

        cmdShowProductionProcess.setText("Show List");
        cmdShowProductionProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowProductionProcessActionPerformed(evt);
            }
        });

        ProductionProcessPanel.add(cmdShowProductionProcess);
        cmdShowProductionProcess.setBounds(510, 10, 118, 25);

        TableProductionProcess.setModel(new javax.swing.table.DefaultTableModel(
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
        TableProductionProcess.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        TableProductionProcess.setMinimumSize(new java.awt.Dimension(1, 64));
        TableProductionProcess.getTableHeader().setFont(new Font("Plain", Font.BOLD, 12));
        TableProductionProcess.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableProductionProcessMouseClicked(evt);
            }
        });

        jScrollPane1.setViewportView(TableProductionProcess);

        ProductionProcessPanel.add(jScrollPane1);
        jScrollPane1.setBounds(10, 70, 700, 290);

        jLabel3.setText("Order By :");
        ProductionProcessPanel.add(jLabel3);
        jLabel3.setBounds(280, 10, 70, 20);

        ProductionProcessPanel.add(cmbOrder);
        cmbOrder.setBounds(350, 10, 140, 30);

        cmdProductionProcessExporttoExcel.setText("Export to Excel");
        cmdProductionProcessExporttoExcel.setMargin(new java.awt.Insets(1, 7, 1, 7));
        cmdProductionProcessExporttoExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdProductionProcessExporttoExcelActionPerformed(evt);
            }
        });

        ProductionProcessPanel.add(cmdProductionProcessExporttoExcel);
        cmdProductionProcessExporttoExcel.setBounds(10, 10, 112, 23);

        jTabbedPane1.addTab("Partywise Monthwise", ProductionProcessPanel);

        jPanel2.setLayout(null);

        TableComplaint.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane19.setViewportView(TableComplaint);

        jPanel2.add(jScrollPane19);
        jScrollPane19.setBounds(0, 70, 710, 290);

        cmdShowComplaint.setText("Show List");
        cmdShowComplaint.setMargin(new java.awt.Insets(2, 4, 2, 4));
        cmdShowComplaint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowComplaintActionPerformed(evt);
            }
        });

        jPanel2.add(cmdShowComplaint);
        cmdShowComplaint.setBounds(490, 20, 90, 25);

        jPanel2.add(cmbComplaint);
        cmbComplaint.setBounds(330, 20, 140, 24);

        jLabel30.setText("Order By");
        jPanel2.add(jLabel30);
        jLabel30.setBounds(270, 20, 60, 20);

        cmdComplaintExportToExcel.setText("Export to Excel");
        cmdComplaintExportToExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdComplaintExportToExcelActionPerformed(evt);
            }
        });

        jPanel2.add(cmdComplaintExportToExcel);
        cmdComplaintExportToExcel.setBounds(10, 20, 140, 25);

        jTabbedPane1.addTab("AgeWise OutStanding", jPanel2);

        jPanel3.setLayout(null);

        TablePartyWiseSummary.setModel(new javax.swing.table.DefaultTableModel(
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
        TablePartySummary.setViewportView(TablePartyWiseSummary);

        jPanel3.add(TablePartySummary);
        TablePartySummary.setBounds(0, 70, 710, 290);

        cmdShowComplaint1.setText("Show List");
        cmdShowComplaint1.setMargin(new java.awt.Insets(2, 4, 2, 4));
        cmdShowComplaint1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowComplaint1ActionPerformed(evt);
            }
        });

        jPanel3.add(cmdShowComplaint1);
        cmdShowComplaint1.setBounds(490, 20, 90, 25);

        jPanel3.add(cmbPartySummary);
        cmbPartySummary.setBounds(330, 20, 140, 24);

        jLabel31.setText("Order By");
        jPanel3.add(jLabel31);
        jLabel31.setBounds(270, 20, 60, 20);

        cmdComplaintExportToExcel1.setText("Export to Excel");
        cmdComplaintExportToExcel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdComplaintExportToExcel1ActionPerformed(evt);
            }
        });

        jPanel3.add(cmdComplaintExportToExcel1);
        cmdComplaintExportToExcel1.setBounds(10, 20, 140, 25);

        jTabbedPane1.addTab("PartyWiseSummary", jPanel3);

        getContentPane().add(jTabbedPane1);
        jTabbedPane1.setBounds(10, 150, 720, 410);
        jTabbedPane1.getAccessibleContext().setAccessibleName("Pending Order Form");

        txtpartycode.setToolTipText("Press F1 key for search Party Code");
        txtpartycode = new JTextFieldHint(new JTextField(),"Search by F1");
        txtpartycode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtpartycodeFocusLost(evt);
            }
        });
        txtpartycode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtpartycodeKeyPressed(evt);
            }
        });

        getContentPane().add(txtpartycode);
        txtpartycode.setBounds(120, 40, 90, 19);

        txtpartyname.setBackground(new java.awt.Color(204, 204, 204));
        txtpartyname = new JTextFieldHint(new JTextField(),"Party Name");
        txtpartyname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtpartynameActionPerformed(evt);
            }
        });

        getContentPane().add(txtpartyname);
        txtpartyname.setBounds(210, 40, 400, 19);

        jLabel13.setText("Date from");
        getContentPane().add(jLabel13);
        jLabel13.setBounds(380, 70, 70, 20);

        txtdatefrom = new JTextFieldHint(new JTextField(),"DD/MM/YYYY");
        txtdatefrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtdatefromActionPerformed(evt);
            }
        });

        getContentPane().add(txtdatefrom);
        txtdatefrom.setBounds(450, 70, 100, 19);

        txtdateto = new JTextFieldHint(new JTextField(),"DD/MM/YYYY");
        getContentPane().add(txtdateto);
        txtdateto.setBounds(590, 70, 100, 19);

        jLabel14.setText("To");
        getContentPane().add(jLabel14);
        jLabel14.setBounds(560, 70, 20, 20);

        cmdClearAll.setText("Reset");
        cmdClearAll.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdClearAll.setMargin(new java.awt.Insets(2, 7, 2, 7));
        cmdClearAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdClearAllActionPerformed(evt);
            }
        });

        getContentPane().add(cmdClearAll);
        cmdClearAll.setBounds(610, 120, 110, 25);

        jLabel17.setText("Agent Code");
        getContentPane().add(jLabel17);
        jLabel17.setBounds(30, 70, 80, 20);

        getContentPane().add(txtAgentCOde);
        txtAgentCOde.setBounds(120, 70, 150, 19);

        jLabel10.setText("(Ex : 15,20,30)");
        getContentPane().add(jLabel10);
        jLabel10.setBounds(280, 70, 100, 20);

        jLabel4.setText("As On Date");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(310, 100, 80, 20);

        txtasondate.setEditable(false);
        txtasondate.setEnabled(false);
        getContentPane().add(txtasondate);
        txtasondate.setBounds(390, 100, 100, 19);

        jLabel5.setText("Charge Code ");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(30, 100, 84, 20);

        getContentPane().add(txtChargeCode);
        txtChargeCode.setBounds(130, 100, 160, 19);

    }//GEN-END:initComponents

    private void cmdComplaintExportToExcel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdComplaintExportToExcel1ActionPerformed
    try {
            
            exp.fillData(TablePartyWiseSummary, new File("/root/Desktop/PartywiseSummary.xls"));
            exp.fillData(TablePartyWiseSummary, new File("D://PartywiseSummary.xls"));
            JOptionPane.showMessageDialog(null, "Data saved at "
            + "'/root/Desktop/PartywiseSummary.xls' successfully in Linux PC or 'D://PartywiseSummary.xls' successfully in Windows PC    ", "Message",
            JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_cmdComplaintExportToExcel1ActionPerformed

    private void cmdShowComplaint1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowComplaint1ActionPerformed
        GeneratePartyWiseSummary();        // TODO add your handling code here:
    }//GEN-LAST:event_cmdShowComplaint1ActionPerformed
    
    private void txtdatefromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtdatefromActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtdatefromActionPerformed
    
    private void cmdClearAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdClearAllActionPerformed
        txtpartyname.setText("");
        txtpartycode.setText("");
        txtdatefrom.setText("");
        txtAgentCOde.setText("");
        // txtasondate.setText("");
        txtChargeCode.setText("");
        
    }//GEN-LAST:event_cmdClearAllActionPerformed
    
    private void txtpartycodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtpartycodeFocusLost
        try {
            if (!txtpartycode.getText().equals("")) {
                String strSQL = "";
                ResultSet rsTmp;
                strSQL = "";
                strSQL += "SELECT PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE = " + txtpartycode.getText().trim() + "";
                rsTmp = data.getResult(strSQL);
                rsTmp.first();
                txtpartyname.setText(rsTmp.getString("PARTY_NAME"));
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_txtpartycodeFocusLost
    
    private void txtpartynameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtpartynameActionPerformed
        
    }//GEN-LAST:event_txtpartynameActionPerformed
    
    private void txtpartycodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtpartycodeKeyPressed
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();
            
            aList.SQL = "SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER  ORDER BY PARTY_NAME";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;
            if (aList.ShowLOV()) {
                txtpartycode.setText(aList.ReturnVal);
                txtpartyname.setText(clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, aList.ReturnVal));
            }
        }
    }//GEN-LAST:event_txtpartycodeKeyPressed
    
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        
    }//GEN-LAST:event_formMouseClicked
    
    private void jTabbedPane1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseEntered
        
    }//GEN-LAST:event_jTabbedPane1MouseEntered
    
    private void cmdComplaintExportToExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdComplaintExportToExcelActionPerformed
        try {
            
            exp.fillData(TableComplaint, new File("/root/Desktop/Agewise.xls"));
            exp.fillData(TableComplaint, new File("D://Agewise.xls"));
            JOptionPane.showMessageDialog(null, "Data saved at "
            + "'/root/Desktop/Agewise.xls' successfully in Linux PC or 'D://Agewise.xls' successfully in Windows PC    ", "Message",
            JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }//GEN-LAST:event_cmdComplaintExportToExcelActionPerformed
    
    private void cmdShowComplaintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowComplaintActionPerformed
        GenerateAgeWise();
    }//GEN-LAST:event_cmdShowComplaintActionPerformed
    
    private void ProductionProcessPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ProductionProcessPanelMouseEntered
        txtpartycode.setEnabled(true);
        txtpartyname.setEnabled(true);
        txtdatefrom.setEnabled(true);
        txtdateto.setEnabled(true);
        
    }//GEN-LAST:event_ProductionProcessPanelMouseEntered
    
    private void cmdProductionProcessExporttoExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdProductionProcessExporttoExcelActionPerformed
        try {
            
            exp.fillData(TableProductionProcess, new File("/root/Desktop/Partywise Monthwise.xls"));
            exp.fillData(TableProductionProcess, new File("D://Partywise Monthwise.xls"));
            JOptionPane.showMessageDialog(null, "Data saved at "
            + "'/root/Desktop/Partywise Monthwise.xls' successfully in Linux PC or 'D://Partywise Monthwise.xls' successfully in Windows PC    ", "Message",
            JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_cmdProductionProcessExporttoExcelActionPerformed
    
    private void TableProductionProcessMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableProductionProcessMouseClicked
        
    }//GEN-LAST:event_TableProductionProcessMouseClicked
    
    private void cmdShowProductionProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowProductionProcessActionPerformed
        
        //PRODUCTION PROCESS SHOW LIST
        GenerateProductionProcess();
        //doProcessing();
    }//GEN-LAST:event_cmdShowProductionProcessActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ProductionProcessPanel;
    private javax.swing.JTable TableComplaint;
    private javax.swing.JScrollPane TablePartySummary;
    private javax.swing.JTable TablePartyWiseSummary;
    private javax.swing.JTable TableProductionProcess;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbComplaint;
    private javax.swing.JComboBox cmbOrder;
    private javax.swing.JComboBox cmbPartySummary;
    private javax.swing.JButton cmdClearAll;
    private javax.swing.JButton cmdComplaintExportToExcel;
    private javax.swing.JButton cmdComplaintExportToExcel1;
    private javax.swing.JButton cmdProductionProcessExporttoExcel;
    private javax.swing.JButton cmdShowComplaint;
    private javax.swing.JButton cmdShowComplaint1;
    private javax.swing.JButton cmdShowProductionProcess;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTextField txtAgentCOde;
    private javax.swing.JTextField txtChargeCode;
    private javax.swing.JTextField txtasondate;
    private javax.swing.JTextField txtdatefrom;
    private javax.swing.JTextField txtdateto;
    private javax.swing.JTextField txtpartycode;
    private javax.swing.JTextField txtpartyname;
    // End of variables declaration//GEN-END:variables
    
    private void FormatGridPartywiseMonthwiseProcess() {
        
        DataModelProductionProcess = new EITLTableModel();
        TableProductionProcess.removeAll();
        TableProductionProcess.setModel(DataModelProductionProcess);
        
        TableProductionProcess.setAutoResizeMode(TableProductionProcess.AUTO_RESIZE_OFF);
        
        //updated on 14/10/2014 by Dhaval
        DataModelProductionProcess.addColumn("Sr.");
        DataModelProductionProcess.addColumn("Party Code");
        DataModelProductionProcess.addColumn("Party Name");
        DataModelProductionProcess.addColumn("Dispatch Station");
        DataModelProductionProcess.addColumn("Charge Code");
        DataModelProductionProcess.addColumn("Year");
        DataModelProductionProcess.addColumn("Month");
        DataModelProductionProcess.addColumn("Invoice Amount");
        DataModelProductionProcess.addColumn("Credit Note Amount");
        DataModelProductionProcess.addColumn("Net Amount");
        
        
        DataModelProductionProcess.TableReadOnly(true);
        TableProductionProcess.getColumnModel().getColumn(0).setMaxWidth(50);
        
    }
    
    private void FormatGridAgeWise() {
        DataModelComplaint = new EITLTableModel();
        TableComplaint.removeAll();
        TableComplaint.setModel(DataModelComplaint);
        
        TableComplaint.setAutoResizeMode(TableComplaint.AUTO_RESIZE_OFF);
        DataModelComplaint.addColumn("Sr.");
        DataModelComplaint.addColumn("Party Code");
        DataModelComplaint.addColumn("Party Name");
        DataModelComplaint.addColumn("Charge Code");
        DataModelComplaint.addColumn("Amount");
        DataModelComplaint.addColumn("0-30 DAYS AMT");
        DataModelComplaint.addColumn("31-60 DAYS AMT");
        DataModelComplaint.addColumn("61-90 DAYS AMT");
        DataModelComplaint.addColumn("91-120 DAYS AMT");
        DataModelComplaint.addColumn("ABOVE 120 DAYS AMT");
        
        DataModelComplaint.TableReadOnly(true);
        TableComplaint.getColumnModel().getColumn(0).setMaxWidth(50);
    }
    
    private void FormatGridPartywiseSummary() {
        
        DataModelPartyWiseSummary = new EITLTableModel();
        TablePartyWiseSummary.removeAll();
        TablePartyWiseSummary.setModel(DataModelPartyWiseSummary);
        
        TablePartyWiseSummary.setAutoResizeMode(TablePartyWiseSummary.AUTO_RESIZE_OFF);
        
        //updated on 14/10/2014 by Dhaval
        DataModelPartyWiseSummary.addColumn("Sr.");
        DataModelPartyWiseSummary.addColumn("Party Code");
        DataModelPartyWiseSummary.addColumn("Party Name");
        DataModelPartyWiseSummary.addColumn("Dispatch Station");
        DataModelPartyWiseSummary.addColumn("Charge Code");
        DataModelPartyWiseSummary.addColumn("Invoice Amount");
        DataModelPartyWiseSummary.addColumn("Credit Note Amount");
        DataModelPartyWiseSummary.addColumn("Net Amount");
        
        
        DataModelPartyWiseSummary.TableReadOnly(true);
        TablePartyWiseSummary.getColumnModel().getColumn(0).setMaxWidth(50);
        
    }
    
    //   private void FormatGridOutStanding() {
    //        DataModelOutStanding = new EITLTableModel();
    //        TableOutStanding.removeAll();
    //        TableOutStanding.setModel(DataModelOutStanding);
    //
    //        TableOutStanding.setAutoResizeMode(TableOutStanding.AUTO_RESIZE_OFF);
    //        DataModelOutStanding.addColumn("Sr.");
    //        DataModelOutStanding.addColumn("Main Code");
    //        DataModelOutStanding.addColumn("Party Code");
    //        DataModelOutStanding.addColumn("Party Name");
    //        DataModelOutStanding.addColumn("Doc Date");
    //        DataModelOutStanding.addColumn("Ref No");
    //        DataModelOutStanding.addColumn("Due Date");
    //        DataModelOutStanding.addColumn("Days");
    //        DataModelOutStanding.addColumn("Amount");
    //        DataModelOutStanding.addColumn("Description");
    //        DataModelOutStanding.addColumn("Days1");
    //
    //
    //        DataModelOutStanding.TableReadOnly(true);
    //        TableOutStanding.getColumnModel().getColumn(0).setMaxWidth(50);
    //    }
    //
    //   private void FormatGridStgInvoice() {
    //        DataModelStgInvoice = new EITLTableModel();
    //        TableInvoice.removeAll();
    //        TableInvoice.setModel(DataModelStgInvoice);
    //
    //        TableOutStanding.setAutoResizeMode(TableInvoice.AUTO_RESIZE_OFF);
    //        DataModelStgInvoice.addColumn("Sr.");
    //        DataModelStgInvoice.addColumn("Party Code");
    //        DataModelStgInvoice.addColumn("Party Name");
    //        DataModelStgInvoice.addColumn("City");
    //        DataModelStgInvoice.addColumn("Product Code");
    //        DataModelStgInvoice.addColumn("Patten Code");
    //        DataModelStgInvoice.addColumn("Piece No");
    //        DataModelStgInvoice.addColumn("Flag Diff Code");
    //        DataModelStgInvoice.addColumn("Unit Code");
    //        DataModelStgInvoice.addColumn("Gross Qty");
    //        DataModelStgInvoice.addColumn("Net Qty");
    //        DataModelStgInvoice.addColumn("Net Amount");
    //        DataModelStgInvoice.addColumn("Agent Last Invoice");
    //        DataModelStgInvoice.addColumn("Agent Sr No");
    //        DataModelStgInvoice.addColumn("Invoice No");
    //        DataModelStgInvoice.addColumn("Invoice Date");
    //        DataModelStgInvoice.addColumn("Bale No");
    //        DataModelStgInvoice.addColumn("Gate Pass No");
    //        DataModelStgInvoice.addColumn("Gate Pass Date");
    //        DataModelStgInvoice.addColumn("Rate");
    //        DataModelStgInvoice.addColumn("Diff Disc Per");
    //        DataModelStgInvoice.addColumn("Addl Disc Per");
    //
    //        DataModelStgInvoice.TableReadOnly(true);
    //
    //      TableInvoice.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
    //
    //    }
    //party wise month wise
    private void GenerateProductionProcess() {
        String strOrderDateFrom = "";
        String strOrderDateTo = "";
        try {
            FormatGridPartywiseMonthwiseProcess(); //clear existing content of table
            SelUserID = txtpartycode.getText().toString();
            strOrderDateFrom = EITLERPGLOBAL.formatDateDB(txtdatefrom.getText());
            strOrderDateTo = EITLERPGLOBAL.formatDateDB(txtdateto.getText());
            ResultSet rsTmp, rsBuyer, rsIndent, rsRIA;
            
            //Rishi 03/03/2015 Excise Duty Change
            strSQL = "SELECT PARTY_CODE,PARTY_NAME,DISPATCH_STATION,CHARGE_CODE,YYYY1,MM1,round(AM.AMT,2) AS AMT,round(B.TOTAL,2) AS TOTAL,round((COALESCE(AM.AMT,0) - COALESCE(B.TOTAL,0)),2) AS TOTAL_AMOUNT,0+B.TOTAL AS TT, NOW() FROM (SELECT A.PARTY_CODE ,B.PARTY_NAME,CITY_ID,DISPATCH_STATION,B.CHARGE_CODE,EXTRACT(YEAR FROM INVOICE_DATE) AS YYYY1 ,EXTRACT(MONTH FROM INVOICE_DATE) AS MM1 ,SUM(TOTAL_NET_AMOUNT) AS AMT FROM DINESHMILLS.D_SAL_INVOICE_HEADER A,DINESHMILLS.D_SAL_PARTY_MASTER B WHERE INVOICE_TYPE =1 AND A.APPROVED =1 AND A.CANCELLED =0 AND WAREHOUSE_CODE !=0 AND TOTAL_NET_AMOUNT/TOTAL_GROSS_QTY >60 AND A.PARTY_CODE = B.PARTY_CODE AND  ";
            System.out.println("strSQL 1 :" + strSQL);
            
            if (!txtpartycode.getText().equals("")) {
                strSQL += "A.PARTY_CODE = " + SelUserID + " AND ";
            }
            if (!txtAgentCOde.getText().equals("")) {
                strSQL += "SUBSTRING(A.PARTY_CODE,1,2) IN (" + txtAgentCOde.getText() + ") AND ";
            }
            
            if (!txtChargeCode.getText().equals("")) {
                strSQL += "B.CHARGE_CODE = " + txtChargeCode.getText() + " AND ";
            }
            
            if (!txtasondate.getText().equals("")) {
                strSQL += "A.INVOICE_DATE <= '" + EITLERPGLOBAL.formatDateDB(txtasondate.getText()) + "' AND ";
            }
            
            if (!txtdatefrom.getText().equals("")) {
                strSQL += "A.INVOICE_DATE >= '" + strOrderDateFrom + "' AND ";
            }
            if (!txtdateto.getText().equals("")) {
                strSQL += "A.INVOICE_DATE <= '" + strOrderDateTo + "' AND ";
            }
            strSQL += "B.CHARGE_CODE IN (2,4,5) GROUP BY  A.PARTY_CODE,B.PARTY_NAME,CITY_ID,DISPATCH_STATION,B.CHARGE_CODE,EXTRACT(YEAR FROM INVOICE_DATE) ,EXTRACT(MONTH FROM INVOICE_DATE)) AS AM LEFT JOIN ( SELECT PP AS PARTY2 ,VYY,VMM,SUM(DRA) AS TOTAL  FROM  (SELECT A.VOUCHER_NO, EXTRACT(YEAR FROM VOUCHER_DATE) AS VYY, EXTRACT(MONTH  FROM VOUCHER_DATE) AS VMM,MAX(SUB_ACCOUNT_CODE) AS PP,SUM(CASE WHEN EFFECT ='C' THEN AMOUNT END) AS CR,SUM(CASE WHEN EFFECT ='D'  THEN AMOUNT END) AS  DR,SUM(COALESCE(CASE WHEN EFFECT ='D'   AND MAIN_ACCOUNT_CODE IN (303019,435046,435053) THEN AMOUNT END,0)) AS DRA,COUNT(COALESCE(CASE WHEN EFFECT ='D'   AND MAIN_ACCOUNT_CODE IN (303019,435046,435053) THEN AMOUNT END,0)) AS DRC,SUM(CASE WHEN EFFECT ='D'  AND MAIN_ACCOUNT_CODE NOT IN (303019,435046,435053)  THEN AMOUNT END) AS DRB FROM FINANCE.D_FIN_VOUCHER_DETAIL A,FINANCE.D_FIN_VOUCHER_HEADER B WHERE A.VOUCHER_NO = B.VOUCHER_NO AND BOOK_CODE = 13 AND B.CANCELLED =0 AND B.APPROVED =1 AND VOUCHER_DATE >= '" + strOrderDateFrom + "' AND VOUCHER_DATE <= '" + strOrderDateTo + "' GROUP BY A.VOUCHER_NO,EXTRACT(YEAR FROM VOUCHER_DATE) , EXTRACT(MONTH  FROM VOUCHER_DATE)) AS A GROUP BY PP,VYY,VMM ) AS B ON AM.PARTY_CODE = B.PARTY2 AND VMM = MM1 AND VYY = YYYY1 WHERE COALESCE(AM.AMT,0) - COALESCE(B.TOTAL,0) != 0 ";
            
            //int Orderby=EITLERPGLOBAL.getComboCode(cmbOrder);
            int Orderby = cmbOrder.getSelectedIndex();
            Orderby++;
            if (Orderby == 1) {
                strSQL += " ORDER BY PARTY_CODE";
            }
            if (Orderby == 2) {
                strSQL += " ORDER BY PARTY_NAME ";
            }
            if (Orderby == 3) {
                strSQL += " ORDER BY DISPATCH_STATION ";
            }
            if (Orderby == 4) {
                strSQL += " ORDER BY CHARGE_CODE";
            }
            if (Orderby == 5) {
                strSQL += " ORDER BY YYYY1 DESC ";
            }
            if (Orderby == 6) {
                strSQL += " ORDER BY MM1 DESC ";
            }
            
            System.out.println("strSQL 2 :" + strSQL);
            rsTmp = data.getResult(strSQL);
            rsTmp.first();
            if (rsTmp.getRow() > 0) {
                int cnt = 0;
                while (!rsTmp.isAfterLast()) {
                    cnt++;
                    //Object[] rowData=new Object[40];
                    Object[] rowData = new Object[16];
                    rowData[0] = Integer.toString(cnt);
                    rowData[1] = rsTmp.getString("PARTY_CODE");
                    rowData[2] = rsTmp.getString("PARTY_NAME");
                    rowData[3] = rsTmp.getString("DISPATCH_STATION");
                    rowData[4] = rsTmp.getString("CHARGE_CODE");
                    rowData[5] = rsTmp.getString("YYYY1");
                    rowData[6] = rsTmp.getString("MM1");
                    rowData[7] = rsTmp.getString("AMT");
                    rowData[8] = rsTmp.getString("TOTAL");
                    rowData[9] = rsTmp.getString("TOTAL_AMOUNT");
                    
                    
                    DataModelProductionProcess.addRow(rowData);
                    
                    rsTmp.next();
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        
    }
    
    private void GenerateAgeWise() {
        String strOrderDateFrom1 = "";
        String strOrderDateTo1 = "";
        try {
            FormatGridAgeWise();  //clear existing content of table
            SelUserID = txtpartycode.getText().toString();
            strOrderDateFrom1 = EITLERPGLOBAL.formatDateDB(txtdatefrom.getText());
            strOrderDateTo1 = EITLERPGLOBAL.formatDateDB(txtdateto.getText());
            
            ResultSet rsTmp, rsBuyer, rsIndent, rsRIA;
            strSQL = "";
            strSQL += "SELECT SUB_ACCOUNT_CODE, PARTY_NAME,CHARGE_CODE,AMT,D1,D2,D3,D4,D5 FROM (SELECT SUB_ACCOUNT_CODE, PARTY_NAME,CHARGE_CODE,SUM(AMT) AS AMT,SUM(COALESCE(CASE WHEN DTT = '0-30DAYS' THEN AMT END,0)) AS D1,SUM(COALESCE(CASE WHEN DTT = '31-60DAYS' THEN AMT END,0)) AS D2,SUM(COALESCE(CASE WHEN DTT = '61-90DAYS' THEN AMT END,0)) AS D3,SUM(COALESCE(CASE WHEN DTT = '91-120DAYS' THEN AMT END,0)) AS D4,SUM(COALESCE(CASE WHEN DTT =' ABOVE120DAYS' THEN AMT END,0)) AS D5 FROM (SELECT A.VOUCHER_NO,SUB_ACCOUNT_CODE, PARTY_NAME,CHARGE_CODE,VOUCHER_DATE,INVOICE_DATE,INVOICE_NO,AMOUNT,MATCHED,MATCHED_DATE,CASE WHEN MATCHED_DATE  > '" + strOrderDateTo1 + "' AND MATCHED =1 THEN AMOUNT WHEN MATCHED_DATE  > '" + strOrderDateTo1 + "' AND MATCHED =0 THEN AMOUNT WHEN MATCHED_DATE  <=  '" + strOrderDateTo1 + "' AND MATCHED =0 THEN AMOUNT WHEN MATCHED_DATE  <= '" + strOrderDateTo1 + "' AND MATCHED =1 THEN 0 END AS PP,CASE WHEN SUBSTRING(A.VOUCHER_NO,1,2) IN ('SJ') THEN AMOUNT WHEN  SUBSTRING(A.VOUCHER_NO,1,2) IN ('CN') THEN -(AMOUNT) END AS AMT,DATEDIFF('" + strOrderDateTo1 + "',VOUCHER_DATE)  AS DT,CASE WHEN DATEDIFF('" + strOrderDateTo1 + "',VOUCHER_DATE)  >=0 AND DATEDIFF('" + strOrderDateTo1 + "',VOUCHER_DATE)  <= 30 THEN '0-30DAYS' WHEN DATEDIFF('" + strOrderDateTo1 + "',VOUCHER_DATE)  >=31 AND DATEDIFF('" + strOrderDateTo1 + "',VOUCHER_DATE)  <=60 THEN '31-60DAYS' WHEN DATEDIFF('" + strOrderDateTo1 + "',VOUCHER_DATE)  >=61 AND DATEDIFF('" + strOrderDateTo1 + "',VOUCHER_DATE)  <=90 THEN '61-90DAYS' WHEN DATEDIFF('" + strOrderDateTo1 + "',VOUCHER_DATE)  >=91 AND DATEDIFF('" + strOrderDateTo1 + "',VOUCHER_DATE)  <=120 THEN '91-120DAYS' WHEN DATEDIFF('" + strOrderDateTo1 + "',VOUCHER_DATE)  >=121 THEN ' ABOVE120DAYS'  END AS DTT FROM FINANCE.D_FIN_VOUCHER_DETAIL A,FINANCE.D_FIN_VOUCHER_HEADER B,DINESHMILLS.D_SAL_PARTY_MASTER P ";
            strSQL += "WHERE ";
            
            if (!txtpartycode.getText().equals("")) {
                strSQL += "A.SUB_ACCOUNT_CODE = " + SelUserID + " AND ";
            }
            
            if (!txtAgentCOde.getText().equals("")) {
                strSQL += "SUBSTRING(A.SUB_ACCOUNT_CODE,1,2) IN (" + txtAgentCOde.getText() + ") AND ";
            }
            
            if (!txtChargeCode.getText().equals("")) {
                strSQL += "CHARGE_CODE = " + txtChargeCode.getText() + " AND ";
            }
            
            if (!txtasondate.getText().equals("")) {
                strSQL += "VOUCHER_DATE <= '" + EITLERPGLOBAL.formatDateDB(txtasondate.getText()) + "' AND ";
            }
            
            if (!txtdatefrom.getText().equals("")) {
                strSQL += "VOUCHER_DATE >= '" + strOrderDateFrom1 + "' AND ";
            }
            
            if (!txtdateto.getText().equals("")) {
                strSQL += "VOUCHER_DATE <= '" + strOrderDateTo1 + "' AND ";
            }
            
            strSQL += "A.VOUCHER_NO = B.VOUCHER_NO AND B.CANCELLED =0 AND B.APPROVED =1  AND A.MAIN_ACCOUNT_CODE = 210027 AND SUBSTRING(A.VOUCHER_NO,1,2) IN ('SJ','CN') AND P.PARTY_CODE = A.SUB_ACCOUNT_CODE AND A.MAIN_ACCOUNT_CODE = P.MAIN_ACCOUNT_CODE AND P.CHARGE_CODE IN (2,4,5,8)) AS A WHERE PP>0 GROUP BY SUB_ACCOUNT_CODE, PARTY_NAME,CHARGE_CODE) AS B";
            
            int Orderby = EITLERPGLOBAL.getComboCode(cmbComplaint);
            if (Orderby == 1) {
                strSQL += " ORDER BY SUB_ACCOUNT_CODE  ";
            }
            if (Orderby == 2) {
                strSQL += " ORDER BY PARTY_NAME  ";
            }
            if (Orderby == 3) {
                strSQL += " ORDER BY CHARGE_CODE  ";
            }
            
            System.out.println(strSQL);
            rsTmp = data.getResult(strSQL);
            rsTmp.first();
            if (rsTmp.getRow() > 0) {
                int cnt = 0;
                while (!rsTmp.isAfterLast()) {
                    cnt++;
                    
                    Object[] rowData = new Object[12];
                    rowData[0] = Integer.toString(cnt);
                    rowData[1] = rsTmp.getString("SUB_ACCOUNT_CODE");
                    rowData[2] = rsTmp.getString("PARTY_NAME");
                    rowData[3] = rsTmp.getString("CHARGE_CODE");
                    rowData[4] = rsTmp.getString("AMT");
                    rowData[5] = rsTmp.getString("D1");
                    rowData[6] = rsTmp.getString("D2");
                    rowData[7] = rsTmp.getString("D3");
                    rowData[8] = rsTmp.getString("D4");
                    rowData[9] = rsTmp.getString("D5");
                    
                    DataModelComplaint.addRow(rowData);
                    rsTmp.next();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    private void GeneratePartyWiseSummary() {
        String strOrderDateFrom = "";
        String strOrderDateTo = "";
        try {
            FormatGridPartywiseSummary(); //clear existing content of table
            SelUserID = txtpartycode.getText().toString();
            strOrderDateFrom = EITLERPGLOBAL.formatDateDB(txtdatefrom.getText());
            strOrderDateTo = EITLERPGLOBAL.formatDateDB(txtdateto.getText());
            ResultSet rsTmp, rsBuyer, rsIndent, rsRIA;
            
            //Rishi 03/03/2015 Excise Duty Change
            strSQL = "SELECT PARTY_CODE,PARTY_NAME,DISPATCH_STATION,CHARGE_CODE,round(AM.AMT,2) AS AMT,round(B.TOTAL,2) AS TOTAL,round((COALESCE(AM.AMT,0) - COALESCE(B.TOTAL,0)),2) AS TOTAL_AMOUNT,0+B.TOTAL AS TT, NOW() FROM (SELECT A.PARTY_CODE ,B.PARTY_NAME,CITY_ID,DISPATCH_STATION,B.CHARGE_CODE,SUM(TOTAL_NET_AMOUNT) AS AMT FROM DINESHMILLS.D_SAL_INVOICE_HEADER A,DINESHMILLS.D_SAL_PARTY_MASTER B WHERE INVOICE_TYPE =1 AND A.APPROVED =1 AND A.CANCELLED =0 AND WAREHOUSE_CODE !=0 AND TOTAL_NET_AMOUNT/TOTAL_GROSS_QTY >60 AND A.PARTY_CODE = B.PARTY_CODE AND  ";
            System.out.println("strSQL 1 :" + strSQL);
            
            if (!txtpartycode.getText().equals("")) {
                strSQL += "A.PARTY_CODE = " + SelUserID + " AND ";
            }
            if (!txtAgentCOde.getText().equals("")) {
                strSQL += "SUBSTRING(A.PARTY_CODE,1,2) IN (" + txtAgentCOde.getText() + ") AND ";
            }
            
            if (!txtChargeCode.getText().equals("")) {
                strSQL += "B.CHARGE_CODE = " + txtChargeCode.getText() + " AND ";
            }
            
            if (!txtasondate.getText().equals("")) {
                strSQL += "A.INVOICE_DATE <= '" + EITLERPGLOBAL.formatDateDB(txtasondate.getText()) + "' AND ";
            }
            
            if (!txtdatefrom.getText().equals("")) {
                strSQL += "A.INVOICE_DATE >= '" + strOrderDateFrom + "' AND ";
            }
            if (!txtdateto.getText().equals("")) {
                strSQL += "A.INVOICE_DATE <= '" + strOrderDateTo + "' AND ";
            }
            strSQL += "B.CHARGE_CODE IN (2,4,5) GROUP BY  A.PARTY_CODE) AS AM LEFT JOIN ( SELECT PP AS PARTY2 ,SUM(DRA) AS TOTAL  FROM  (SELECT A.VOUCHER_NO, MAX(SUB_ACCOUNT_CODE) AS PP,SUM(CASE WHEN EFFECT ='C' THEN AMOUNT END) AS CR,SUM(CASE WHEN EFFECT ='D'  THEN AMOUNT END) AS  DR,SUM(COALESCE(CASE WHEN EFFECT ='D'   AND MAIN_ACCOUNT_CODE IN (303019,435046,435053) THEN AMOUNT END,0)) AS DRA,COUNT(COALESCE(CASE WHEN EFFECT ='D'   AND MAIN_ACCOUNT_CODE IN (303019,435046,435053) THEN AMOUNT END,0)) AS DRC,SUM(CASE WHEN EFFECT ='D'  AND MAIN_ACCOUNT_CODE NOT IN (303019,435046,435053)  THEN AMOUNT END) AS DRB FROM FINANCE.D_FIN_VOUCHER_DETAIL A,FINANCE.D_FIN_VOUCHER_HEADER B WHERE A.VOUCHER_NO = B.VOUCHER_NO AND BOOK_CODE = 13 AND B.CANCELLED =0 AND B.APPROVED =1 AND VOUCHER_DATE >= '" + strOrderDateFrom + "' AND VOUCHER_DATE <= '" + strOrderDateTo + "' GROUP BY A.VOUCHER_NO) AS A GROUP BY PP ) AS B ON AM.PARTY_CODE = B.PARTY2  WHERE COALESCE(AM.AMT,0) - COALESCE(B.TOTAL,0) != 0 ";
            
            //int Orderby=EITLERPGLOBAL.getComboCode(cmbOrder);
            int Orderby = cmbPartySummary.getSelectedIndex();
            Orderby++;
            if (Orderby == 1) {
                strSQL += " ORDER BY PARTY_CODE";
            }
            if (Orderby == 2) {
                strSQL += " ORDER BY PARTY_NAME ";
            }
            if (Orderby == 3) {
                strSQL += " ORDER BY DISPATCH_STATION ";
            }
            if (Orderby == 4) {
                strSQL += " ORDER BY CHARGE_CODE";
            }
            
            System.out.println("strSQL 2 :" + strSQL);
            rsTmp = data.getResult(strSQL);
            rsTmp.first();
            if (rsTmp.getRow() > 0) {
                int cnt = 0;
                while (!rsTmp.isAfterLast()) {
                    cnt++;
                    //Object[] rowData=new Object[40];
                    Object[] rowData = new Object[16];
                    rowData[0] = Integer.toString(cnt);
                    rowData[1] = rsTmp.getString("PARTY_CODE");
                    rowData[2] = rsTmp.getString("PARTY_NAME");
                    rowData[3] = rsTmp.getString("DISPATCH_STATION");
                    rowData[4] = rsTmp.getString("CHARGE_CODE");
                    rowData[5] = rsTmp.getString("AMT");
                    rowData[6] = rsTmp.getString("TOTAL");
                    rowData[7] = rsTmp.getString("TOTAL_AMOUNT");
                    
                    
                    DataModelPartyWiseSummary.addRow(rowData);
                    
                    rsTmp.next();
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        
    }
    
    //    private void Generateoutstanding() {
    //        String strOrderDateFrom1 = "";
    //        String strOrderDateTo1 = "";
    //        try {
    //            FormatGridOutStanding();  //clear existing content of table
    //            SelUserID = txtpartycode.getText().toString();
    //            strOrderDateFrom1 = EITLERPGLOBAL.formatDateDB(txtdatefrom.getText());
    //            strOrderDateTo1 = EITLERPGLOBAL.formatDateDB(txtdateto.getText());
    //
    //            ResultSet rsTmp, rsBuyer, rsIndent, rsRIA;
    //            strSQL = "";
    //            strSQL += "SELECT * FROM (SELECT COLUMN_1 AS MAIN_CODE,COLUMN_2 AS PARTY_CODE,CONCAT_WS(', ',COLUMN_3,DISPATCH_STATION) AS PARTY_NAME,COLUMN_5 AS DOC_DATE,COLUMN_6 AS REF_NO,COLUMN_11 AS DUE_DATE ,COLUMN_12 AS DAYS,COLUMN_15 AS AMOUNT,COLUMN_13 AS DESCRIPTION,COLUMN_14 AS DAYS1 FROM FINANCE.D_FIN_REPORT_TRANSACTION, DINESHMILLS.D_SAL_PARTY_MASTER ";
    //            strSQL += " WHERE  ";
    //
    //            if (!txtpartycode.getText().equals("")) {
    //                strSQL += "PARTY_CODE = " + SelUserID + " AND ";
    //            }
    //
    //            strSQL += "USER_ID =135 AND COLUMN_13='Inv' AND COLUMN_1=210010 AND COLUMN_2 = PARTY_CODE ) AS B UNION ALL SELECT MAIN_CODE,CONCAT(PARTY_CODE,'- TOTAL -'),CONCAT(PARTY_NAME,'- TOTAL -'),'--TOTAL--','--','--','--',SUM(AMOUNT),'TOT_NV--','--' FROM (SELECT COLUMN_1 AS MAIN_CODE,COLUMN_2 AS PARTY_CODE,CONCAT_WS(', ',COLUMN_3,DISPATCH_STATION) AS PARTY_NAME,COLUMN_5 AS DOC_DATE,COLUMN_6 AS REF_NO,COLUMN_11 AS DUE_DATE ,COLUMN_12 AS DAYS,COLUMN_15 AS AMOUNT,COLUMN_13 AS DESCRIPTION,COLUMN_14 AS DAYS1 FROM FINANCE.D_FIN_REPORT_TRANSACTION, DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE = '" + txtpartycode.getText() + "' AND USER_ID =135 AND COLUMN_13='Inv' AND COLUMN_1=210010 AND COLUMN_2 = PARTY_CODE ) AS A GROUP BY MAIN_CODE,PARTY_CODE,PARTY_NAME ";
    //
    //            int Orderby = EITLERPGLOBAL.getComboCode(cmbOutStanding);
    //            if (Orderby == 1) {
    //                strSQL += " ORDER BY PARTY_CODE ";
    //            }
    //
    //
    //
    //            System.out.println(strSQL);
    //            rsTmp = data.getResult(strSQL);
    //            rsTmp.first();
    //            if (rsTmp.getRow() > 0) {
    //                int cnt = 0;
    //                while (!rsTmp.isAfterLast()) {
    //                    cnt++;
    //
    //                    Object[] rowData = new Object[14];
    //                    rowData[0] = Integer.toString(cnt);
    //                    rowData[1] = rsTmp.getString("MAIN_CODE");
    //                    rowData[2] = rsTmp.getString("PARTY_CODE");
    //                    rowData[3] = rsTmp.getString("PARTY_NAME");
    //                    rowData[4] = rsTmp.getString("DOC_DATE");
    //                    rowData[5] = rsTmp.getString("REF_NO");
    //                    rowData[6] = rsTmp.getString("DUE_DATE");
    //                    rowData[7] = rsTmp.getString("DAYS");
    //                    rowData[8] = rsTmp.getString("AMOUNT");
    //                    rowData[9] = rsTmp.getString("DESCRIPTION");
    //                    rowData[10] = rsTmp.getString("DAYS1");
    //
    //
    //                    DataModelOutStanding.addRow(rowData);
    //                    rsTmp.next();
    //                }
    //            }
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //            JOptionPane.showMessageDialog(null, e.getMessage());
    //        }
    //    }
    //
    //    private void GenerateStgInvoice() {
    //        String strOrderDateFrom2 = "";
    //        String strOrderDateTo2 = "";
    //        try {
    //            FormatGridStgInvoice();  //clear existing content of table
    //            SelUserID = txtpartycode.getText().toString();
    //            strOrderDateFrom2 = EITLERPGLOBAL.formatDateDB(txtdatefrom.getText());
    //            strOrderDateTo2 = EITLERPGLOBAL.formatDateDB(txtdateto.getText());
    //
    //            ResultSet rsTmp, rsBuyer, rsIndent, rsRIA;
    //            strSQL = "";
    //            strSQL += "SELECT A.PARTY_CODE,C.PARTY_NAME,C.CITY_ID,B.QUALITY_NO,B.PATTERN_CODE,B.PIECE_NO,B.FLAG_DEF_CODE,UNIT_CODE,GROSS_QTY,GROSS_AMOUNT,NET_QTY,B.NET_AMOUNT,AGENT_LAST_INVOICE,A.AGENT_SR_NO,A.INVOICE_NO,A.INVOICE_DATE,A.BALE_NO,A.GATEPASS_NO,A.GATEPASS_DATE,RATE,B.DEF_DISC_PER,B.ADDL_DISC_PER FROM D_SAL_INVOICE_HEADER A,D_SAL_INVOICE_DETAIL  B,D_SAL_PARTY_MASTER C ";
    //
    //            strSQL += " WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.PARTY_CODE=C.PARTY_CODE ";
    //
    //            if (!txtpartycode.getText().equals("")) {
    //                strSQL += "AND A.PARTY_CODE = " + SelUserID + " AND ";
    //            }
    //
    //            if (!txtdatefrom.getText().equals("")) {
    //                strSQL += "A.INVOICE_DATE >= '" + strOrderDateFrom2 + "' AND ";
    //            }
    //            if (!txtdateto.getText().equals("")) {
    //                strSQL += "A.INVOICE_DATE <= '" + strOrderDateTo2 + "'";
    //            }
    //
    //            int Orderby = EITLERPGLOBAL.getComboCode(cmbInvoice);
    //            if (Orderby == 1) {
    //                strSQL += " ORDER BY A.PARTY_CODE";
    //            }
    //
    //
    //
    //            System.out.println(strSQL);
    //            rsTmp = data.getResult(strSQL);
    //            rsTmp.first();
    //            if (rsTmp.getRow() > 0) {
    //                int cnt = 0;
    //                while (!rsTmp.isAfterLast()) {
    //                    cnt++;
    //
    //                    Object[] rowData = new Object[25];
    //                    rowData[0] = Integer.toString(cnt);
    //                    rowData[1] = rsTmp.getString("PARTY_CODE");
    //                    rowData[2] = rsTmp.getString("PARTY_NAME");
    //                    rowData[3] = rsTmp.getString("CITY_ID");
    //                    rowData[4] = rsTmp.getString("QUALITY_NO");
    //                    rowData[5] = rsTmp.getString("PATTERN_CODE");
    //                    rowData[6] = rsTmp.getString("PIECE_NO");
    //                    rowData[7] = rsTmp.getString("FLAG_DEF_CODE");
    //                    rowData[8] = rsTmp.getString("UNIT_CODE");
    //                    rowData[9] = rsTmp.getString("GROSS_QTY");
    //                    rowData[10] = rsTmp.getString("NET_QTY");
    //                    rowData[11] = rsTmp.getString("NET_AMOUNT");
    //                    rowData[12] = rsTmp.getString("AGENT_LAST_INVOICE");
    //                    rowData[13] = rsTmp.getString("AGENT_SR_NO");
    //                    rowData[14] = rsTmp.getString("INVOICE_NO");
    //                    rowData[15] = EITLERPGLOBAL.formatDate(rsTmp.getString("INVOICE_DATE"));
    //                    rowData[16] = rsTmp.getString("BALE_NO");
    //                    rowData[17] = rsTmp.getString("GATEPASS_NO");
    //                    rowData[18] = EITLERPGLOBAL.formatDate(rsTmp.getString("GATEPASS_DATE"));
    //                    rowData[19] = rsTmp.getString("RATE");
    //                    rowData[20] = rsTmp.getString("DEF_DISC_PER");
    //                    rowData[21] = rsTmp.getString("ADDL_DISC_PER");
    //
    //                    DataModelStgInvoice.addRow(rowData);
    //                    rsTmp.next();
    //                }
    //            }
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //            JOptionPane.showMessageDialog(null, e.getMessage());
    //        }
    //    }
}
